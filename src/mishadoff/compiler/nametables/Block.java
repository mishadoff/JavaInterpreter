package mishadoff.compiler.nametables;

import java.util.ArrayList;
import java.util.List;

/**
 * Class presents block of visibility in program
 * @author mishadoff
 */
public class Block {
	private int id;
	
	/** Coords of block */
	private int start;
	private int end;
	
	private int currentLevel = 0;
	
	public Block(int id, int start, int end) {
		this.id = id;
		this.start = start;
		this.end = end;
	}
	
	private Block parent = null;
	private List<Block> innerBlocks = new ArrayList<Block>();
	
	public void addBlock(Block block){
		innerBlocks.add(block);
		block.setParent(this);
	}
	
	private void setParent(Block parent) {
		this.parent = parent;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	
	public Block getParent() {
		return parent;
	}
	
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Block #" + id + " [" + start + "," + end + "]\n");
			for (Block block : innerBlocks) {
				for (int i=0; i<currentLevel; i++)
					builder.append("\t");
				builder.append("\t" + block.toString());
			}
		
		return builder.toString();
	}
	
	
}
