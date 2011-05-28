package mishadoff.compiler.nametables;

import mishadoff.compiler.tokens.Token;

/**
 * Entry presents variable defines
 * @author mishadoff
 *
 */
public class VariableTableEntry {
	Token identifier;
	
	public Token getIdentifier() {
		return identifier;
	}
	public Token getType() {
		return type;
	}
	public void setIdentifier(Token identifier) {
		this.identifier = identifier;
	}
	public void setType(Token type) {
		this.type = type;
	}
	Token type;
}
