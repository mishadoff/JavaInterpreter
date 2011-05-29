package mishadoff.compiler.nametables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import mishadoff.compiler.tokens.CloseBraceToken;
import mishadoff.compiler.tokens.OpenBraceToken;
import mishadoff.compiler.tokens.ReservedWordToken;
import mishadoff.compiler.tokens.SeparatorToken;
import mishadoff.compiler.tokens.Token;

public class BlockBuilder {
	
	Stack<Integer> openBlocks = new Stack<Integer>();
	int startTop;
	int endTop;
	
	int id = 0;
	
	Block currentBlock = null;
	private List<Block> forBlocks = new ArrayList<Block>();
	
	private int counter = 0;
	List<Token> tokens;
	
	public void buildBlockStructure(List<Token> tokens){
		this.tokens = tokens;
		for (Token token : tokens) {
			if (token instanceof SeparatorToken && token.getType() == 2) {
				openBlock(token.getBegin());
			}
			if (token instanceof SeparatorToken && token.getType() == 3) {
				closeBlock(token.getBegin());
			}
		}
		addBlocksWithSingleFor(tokens);
		reconstructBlockStructure();
	}
	
	public void addBlocksWithSingleFor(List<Token> tokens){
		for(int i=0; i<tokens.size(); i++){
			Token token = tokens.get(i);
			if (token instanceof ReservedWordToken &&
				token.getType() == 7){
				int currentToken = i + 2;
				int numOfUnbalancedBraces = 1;
				while (true){
					Token tempToken = tokens.get(currentToken);
					if (tempToken instanceof OpenBraceToken)
						numOfUnbalancedBraces++;
					if (tempToken instanceof CloseBraceToken)
						numOfUnbalancedBraces--;
					if (numOfUnbalancedBraces == 0) {
						currentToken++;
						break;
					}
					currentToken++;
				}
				Token selectToken = tokens.get(currentToken);
				if (selectToken instanceof SeparatorToken &&
					selectToken.getType() == 2) {
					i = currentToken;
					continue;
				}
				else {
					while (true){
						currentToken++;
						Token tempToken = tokens.get(currentToken);
						if (tempToken instanceof SeparatorToken &&
							tempToken.getType() == 0) {
							// New Block
							Block block = new Block(111, selectToken.getBegin(), tempToken.getEnd());
							forBlocks.add(block);
							break;
						}
					}
				}
			}
		}
	}
	
	public void openBlock(int pos) {
		openBlocks.push(pos);
		if (currentBlock == null) {
			currentBlock = new Block(id++, pos, 0);
			
		}
		else {
			Block block = new Block(id++, pos, 0);
			currentBlock.addBlock(block);
			int level = currentBlock.getCurrentLevel();
			currentBlock = block;
			currentBlock.setCurrentLevel(level+1);
		}
	}
	
	public void closeBlock(int pos) {
		openBlocks.pop();
		currentBlock.setEnd(pos);
		if (openBlocks.size() == 0) {}
		else {
			currentBlock = currentBlock.getParent();
		}
	}
	
	public Block getCurrentBlock() {
		return currentBlock;
	}
	
	public void reconstructBlockStructure(){
		Block topBlock = currentBlock;
		for (Block block : forBlocks) {
			topBlock.addBlockOuter(block);
		}
		resortBlocks(topBlock);
		adjustForBlocks();
		recalculateBlockNumbers(topBlock);
	}
	
	/**
	 * Method extends for blocks
	 */
	private void adjustForBlocks(){
		for (int i=0; i < tokens.size(); i++){
			Token token = tokens.get(i);
			if (token instanceof ReservedWordToken &&
				token.getType() == 7){
				int newStart = -1;
				while (true){
					i++;
					token = tokens.get(i);
					if (token instanceof SeparatorToken &&
						token.getType() == 0){
						newStart = token.getEnd();
						break;
					}
				}
				int blockNum = currentBlock.findNumberByToken(token);
				Block block = currentBlock.getBlockById(blockNum);
				adjustNewStart(block, newStart);
			}
		}
	}
	
	private void adjustNewStart(Block block, int newStart){
		for (Block tempBlock : block.getInnerBlocks()) {
			if (newStart < tempBlock.getStart()){
				tempBlock.setStart(newStart);
				return;
			}
		}
	}
	
	private void resortBlocks(Block topBlock) {
		Collections.sort(topBlock.getInnerBlocks(), new Comparator<Block>() {
			@Override
			public int compare(Block o1, Block o2) {
				return o1.getStart() - o2.getStart();
			}
		});
		for (Block block : topBlock.getInnerBlocks()){
			resortBlocks(block);
		}
	}
	
	private int recalculateBlockNumbers(Block curBlock){
		curBlock.setId(counter++);
		for (Block block : curBlock.getInnerBlocks()) {
			recalculateBlockNumbers(block);
		}
		return 0;
	}
}
