package mishadoff.compiler.nametables;

import mishadoff.compiler.tokens.Token;

/**
 * abstract class entry presents function/variable defining
 * @author mishadoff
 *
 */
public abstract class TableEntry {
	protected Token identifier;
	protected Token type;
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TableEntry))
			return false;
		TableEntry other = (TableEntry) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.getText().equals(other.identifier.getText()))
			return false;
		return true;
	}

	
}