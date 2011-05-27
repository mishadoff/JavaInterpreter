package mishadoff.compiler.nametables;

import java.util.List;
import java.util.Stack;

import mishadoff.compiler.tokens.SeparatorToken;
import mishadoff.compiler.tokens.Token;

public class BlockBuilder {
	
	Stack<Integer> openBlocks = new Stack<Integer>();
	int startTop;
	int endTop;
	
	int id = 0;
	
	Block currentBlock = null;
	
	public void buildBlockStructure(List<Token> tokens){
		for (Token token : tokens) {
			if (token instanceof SeparatorToken && token.getType() == 2) {
				openBlock(token.getBegin());
			}
			if (token instanceof SeparatorToken && token.getType() == 3) {
				closeBlock(token.getBegin());
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
	
}
