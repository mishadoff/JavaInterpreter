package mishadoff.compiler.nametables;

import java.util.ArrayList;
import java.util.List;

import mishadoff.compiler.tokens.CloseBraceToken;
import mishadoff.compiler.tokens.IdentifierToken;
import mishadoff.compiler.tokens.OpenBraceToken;
import mishadoff.compiler.tokens.SeparatorToken;
import mishadoff.compiler.tokens.Token;
import mishadoff.compiler.tokens.TypeToken;
import mishadoff.compiler.tokens.VoidToken;

/**
 * Class provides various types of semantic checkings
 * @author mishadoff
 *
 */
public class SemanticChecker {
	
	TableBuilder builder;
	
	List<SemanticConflict> errors = new ArrayList<SemanticConflict>();
	List<Token> undefinedVariables = new ArrayList<Token>();
	List<Token> undefinedFunctions = new ArrayList<Token>();
	
	public SemanticChecker(TableBuilder builder) {
		this.builder = builder;
	}
	
	public boolean checkDoubleThrough(Block topBlock){
		boolean b = true;
		b = (findDoubleInVariableTable(topBlock) && b);
		if (topBlock.getCurrentLevel() == 0) {
			b = (findDoubleInFunctionTable(topBlock) && b);
		}
		b = (checkInnerDefines(topBlock, topBlock) && b);
		for (Block block : topBlock.getInnerBlocks()) {
			b = (checkDoubleThrough(block) && b);
		}
		return b;
	}
	
	/**
	 * Method checks for defining the same identifier in the same
	 * visibility domain.
	 */
	public boolean checkInnerDefines(Block topBlock, Block secondBlock){
		boolean b = true;
		for (Block block : secondBlock.getInnerBlocks()){
			b = (compareTables(topBlock, block) && b);
		}
		for (Block block : secondBlock.getInnerBlocks()) {
			b = (checkInnerDefines(topBlock, block) && b);
		}
		return b;
	}
	
	/**
	 * Find double defines in the same block
	 * Variable table
	 * @param block
	 * @return
	 */
	public boolean findDoubleInVariableTable(Block block){
		boolean b = true;
		for (int i=0; i < block.getVariableTableEntries().size()-1; i++){
			for (int j=i+1; j < block.getVariableTableEntries().size(); j++){
				if (block.getVariableTableEntries().get(i)
					.equals(block.getVariableTableEntries().get(j))){
					errors.add(new SemanticConflict(block, block, i, j, Table.VARIABLE));
					b = false;
				}
			}
		}
		return b;
	}
	
	/**
	 * Find double defines in the same block
	 * Function table
	 * @param block
	 * @return
	 */
	public boolean findDoubleInFunctionTable(Block block){
		boolean b = true;
		for (int i=0; i < block.getFunctionNameTable().size()-1; i++){
			for (int j=i+1; j < block.getFunctionNameTable().size(); j++){
				if (block.getFunctionNameTable().get(i)
					.equals(block.getFunctionNameTable().get(j))){
					errors.add(new SemanticConflict(block, block, i, j, Table.METHOD));
					b = false;
				}
			}
		}
		return b;
	}
	
	/**
	 * Find double defines in two blocks
	 * @param b1
	 * @param b2
	 * @return
	 */
	public boolean compareTables(Block b1, Block b2){
		boolean b = true;
		for (int i=0; i < b1.getVariableTableEntries().size(); i++){
			for (int j=0; j < b2.getVariableTableEntries().size(); j++){
				if (b1.getVariableTableEntries().get(i)
					.equals(b2.getVariableTableEntries().get(j))){
					errors.add(new SemanticConflict(b1, b2, i, j, Table.VARIABLE));
					b = false;
				}
			}
		}
		return b;
	}
	
	public List<SemanticConflict> getErrors(){
		return this.errors;
	}
	
	/**
	 * Method checks if every identifier that uses in program
	 * has correct visibility. Method use through lookup.
	 * @param tokens
	 * @param topBlock
	 * @return
	 */
	public boolean checkVisibility(List<Token> tokens, Block topBlock){
		boolean b = true;
		final int SIZE = tokens.size();
		final int startBlock = topBlock.getStart();

		// Move to significant id
		int currentTokenIndex = 0;
		while (true) {
			Token token = tokens.get(currentTokenIndex);
			if (token.getBegin() >= startBlock)
				break;
			currentTokenIndex++;
		}
		
		for (int j=currentTokenIndex; j < SIZE; j++){
			Token token = tokens.get(j);
			if (token instanceof IdentifierToken) {
				Token nextToken = tokens.get(j+1);
				Token prevToken = tokens.get(j-1);
				if (nextToken instanceof OpenBraceToken) {
					if (prevToken instanceof TypeToken ||
						prevToken instanceof IdentifierToken ||
						prevToken instanceof VoidToken) {
						// function define
					}
					else {
						String name = token.getText();
						int curBlockNum = topBlock.findNumberByToken(token);
						Block testBlock = topBlock.getBlockById(curBlockNum);
						int blockNum = testBlock.findThrough(name);
						if (blockNum == -1) {
							undefinedFunctions.add(token);
							b = false;
						}
					}
				}
				else {
					if (prevToken instanceof IdentifierToken ||
						prevToken instanceof TypeToken ||
						nextToken instanceof IdentifierToken){
						// var define
					}
					else {
						if (!builder.isInFunctionDefines(token)) {
							if (prevToken instanceof SeparatorToken &&
								prevToken.getType() == 1) {
								// var define
							}
							else {
								String name = token.getText();
								int curBlockNum = topBlock.findNumberByToken(token);
								Block testBlock = topBlock.getBlockById(curBlockNum);
								int blockNum = testBlock.findThrough(name);
								if (blockNum == -1) {
									undefinedVariables.add(token);
									b = false;
								}
							}
						}
					}
				}
			}
		}
		
		// second walk: function arguments to next block
		for (FunctionDefine fDefine : builder.functionDefines) {
			for (int i = fDefine.getStartIndex(); i < fDefine.getEndIndex(); i++){
				Token curToken = tokens.get(i);
				if (curToken instanceof IdentifierToken){
					Token previousToken = tokens.get(i-1);
					Token nextToken = tokens.get(i+1);
					if (((previousToken instanceof SeparatorToken &&
						previousToken.getType() == 1) ||
						previousToken instanceof OpenBraceToken) &&
						((nextToken instanceof SeparatorToken &&
						nextToken.getType() == 1) ||
						nextToken instanceof CloseBraceToken)) {
						String name = curToken.getText();
						int curBlockNum = topBlock.findNumberByToken(curToken);
						Block testBlock = topBlock.getBlockById(curBlockNum);
						int blockNum = testBlock.findThrough(name);
						if (blockNum == -1) {
							undefinedVariables.add(curToken);
							b = false;
						}
					}
				}
			}
		}
		
		return b;
	}

	public List<Token> getUndefinedVariables() {
		return undefinedVariables;
	}

	public List<Token> getUndefinedFunctions() {
		return undefinedFunctions;
	}
	
	
}
