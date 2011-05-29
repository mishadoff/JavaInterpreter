package mishadoff.compiler.nametables;

import java.util.List;

import mishadoff.compiler.tokens.Token;

/**
 * Entry presents function defining
 * @author mishadoff
 *
 */
public class FunctionTableEntry extends TableEntry {
	int numOfArguments;
	List<Token> listOfTypes;
	
	public int getNumOfArguments() {
		return numOfArguments;
	}
	public List<Token> getListOfTypes() {
		return listOfTypes;
	}
	public void setNumOfArguments(int numOfArguments) {
		this.numOfArguments = numOfArguments;
	}
	public void setListOfTypes(List<Token> listOfTypes) {
		this.listOfTypes = listOfTypes;
	}
	
	
}
