package mishadoff.compiler.nametables;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import mishadoff.compiler.tokens.Token;

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
	
	/* Name tables */
	private List<FunctionTableEntry> functionNameTable = new LinkedList<FunctionTableEntry>();
	private List<VariableTableEntry> variableNameTable = new LinkedList<VariableTableEntry>();
	
	public List<FunctionTableEntry> getFunctionNameTable() {
		return functionNameTable;
	}

	public List<VariableTableEntry> getVariableTableEntries() {
		return variableNameTable;
	}

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
	
	public void addBlockOuter(Block block){
		for (Block innerBlock : innerBlocks) {
			if (innerBlock.start < block.start &&
				innerBlock.end > block.end ){
				innerBlock.addBlockOuter(block);
				return;
			}
		}
		block.setCurrentLevel(this.currentLevel+1);
		this.addBlock(block);
	}
	
	public void addVariableByCoords(VariableTableEntry entry){
		Token token = entry.getIdentifier();
		for (Block block : innerBlocks){
			if (block.contains(token)) {
				block.addVariableByCoords(entry);
				return;
			}
		}
		// If no one inner block does not contain this token
		// add to this block
		variableNameTable.add(entry);
	}
	
	public void addFunctionByCoords(FunctionTableEntry entry){
		Token token = entry.getIdentifier();
		for (Block block : innerBlocks){
			if (block.contains(token)) {
				block.addFunctionByCoords(entry);
				return;
			}
		}
		// If no one inner block does not contain this token
		// add to this block
		functionNameTable.add(entry);
	}
	
	public void addVariable(VariableTableEntry entry){
		variableNameTable.add(entry);
	}
	
	
	public void addVariableToNextBlockFromIndex(VariableTableEntry entry){
		Token token = entry.getIdentifier();
		for (Block block : innerBlocks) {
			if (block.contains(token)) {
				block.addVariableToNextBlockFromIndex(entry);
				return;
			}
		}
		int index = token.getEnd();
		for (Block block : innerBlocks){
			if (index < block.start) {
				block.addVariable(entry);
				break;
			}
		}
		
	}
	
	public boolean contains(Token token){
		if (this.start < token.getBegin() &&
			this.end > token.getEnd())
				return true;
		return false;
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
	
	public void setId(int id) {
		this.id = id;
	}
	
	public List<Block> getInnerBlocks() {
		return innerBlocks;
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
	
	/**
	 * Print function table for ONLY current block
	 * @return
	 */
	public String printFunctionTable() {
		return printFunctionTable(0);
	}
	
	/**
	 * Print function table for ONLY current block with tabs
	 * @return
	 */
	public String printFunctionTable(int numTabs) {
		StringBuilder builder = new StringBuilder();
			for (FunctionTableEntry entry: functionNameTable) {
				builder.append(tabs(numTabs));
				builder.append(entry.getIdentifier().getText() + "   ");
				builder.append(entry.getType().getText() + "   ");
				// TODO for arguments
				builder.append("\n");
			}
		return builder.toString();
	}
	
	/**
	 * Print variable table for ONLY current block
	 * @return
	 */
	public String printVariableTable() {
		return printVariableTable(0);
	}
	/**
	 * Print variable table for ONLY current block with tabs
	 * @return
	 */
	public String printVariableTable(int numTabs) {
		StringBuilder builder = new StringBuilder();
			for (VariableTableEntry entry: variableNameTable) {
				builder.append(tabs(numTabs));
				builder.append(entry.getIdentifier().getText() + "   ");
				builder.append(entry.getType().getText() + "   ");
				builder.append("\n");
			}
		return builder.toString();
	}
	
	/**
	 * Print all tables recursively
	 * @return
	 */
	public String printRecursiveTables(){
		return printRecursiveTables(0);
	}
	
	/**
	 * Print all tables recursively with tabs
	 * @return
	 */
	public String printRecursiveTables(int curLevel){
		StringBuilder builder = new StringBuilder();
		builder.append(tabs(curLevel));
		builder.append("Block #" + id + " [" + start + "," + end + "]\n");
			builder.append(printVariableTable(curLevel));
			builder.append(printFunctionTable(curLevel));
			for (Block block : innerBlocks) {
				builder.append(block.printRecursiveTables(curLevel+1));
			}
		return builder.toString();
	}
	
	public String tabs(int num){
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<num; i++){
			builder.append("\t");
		}
		return builder.toString();
	}
	
}
