package mishadoff.compiler.nametables;

import java.util.ArrayList;
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
	
	public void buildBlockStructure(List<Token> tokens){
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
		recalculateBlockNumbers(topBlock);
	}
	
	private int recalculateBlockNumbers(Block curBlock){
		curBlock.setId(counter++);
		for (Block block : curBlock.getInnerBlocks()) {
			recalculateBlockNumbers(block);
		}
		return 0;
	}
}
