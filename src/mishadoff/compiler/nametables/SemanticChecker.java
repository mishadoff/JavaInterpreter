package mishadoff.compiler.nametables;

import java.util.ArrayList;
import java.util.List;

/**
 * Class provides various types of semantic checkings
 * @author mishadoff
 *
 */
public class SemanticChecker {
	List<SemanticConflict> errors = new ArrayList<SemanticConflict>();
	
	
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
	
	
	
}
