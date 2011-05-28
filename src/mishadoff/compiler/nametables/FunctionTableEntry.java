package mishadoff.compiler.nametables;

import java.util.List;

import mishadoff.compiler.tokens.Token;

/**
 * Entry presents function defining
 * @author mishadoff
 *
 */
public class FunctionTableEntry {
	Token identifier;
	Token type;
	int numOfArguments;
	List<Token> listOfTypes;
	
	public Token getIdentifier() {
		return identifier;
	}
	public Token getType() {
		return type;
	}
	public int getNumOfArguments() {
		return numOfArguments;
	}
	public List<Token> getListOfTypes() {
		return listOfTypes;
	}
	public void setIdentifier(Token identifier) {
		this.identifier = identifier;
	}
	public void setType(Token type) {
		this.type = type;
	}
	public void setNumOfArguments(int numOfArguments) {
		this.numOfArguments = numOfArguments;
	}
	public void setListOfTypes(List<Token> listOfTypes) {
		this.listOfTypes = listOfTypes;
	}
	
	
}
