package mishadoff.compiler.tokens;

/** Class for abstract Token */
public abstract class Token {

	private int begin;
	private int end;
	private int type;
	private String text;
	
	/** For non typized tokens */
	public Token(int begin, int end, String text) {
		this.begin = begin;
		this.end = end;
		this.text = text;
		this.type = -1;
	}
	
	/** For typized tokens */
	public Token(int begin, int end, String text, int type) {
		this.begin = begin;
		this.end = end;
		this.text = text;
		this.type = type;
	}
	
	/** Returns lexem begin */
	public int getBegin(){
		return begin;
	}
	
	public int getType() {
		return type;
	}
	
	/** Returns lexem end exclude last*/
	public int getEnd(){
		return end;
	}
	
	/** Returns lexem text */
	public String getText(){
		return text;
	}
	
	@Override
	public String toString(){
		String ifTypeString = ((type == -1) ? "" : "[" + getType() + "]" );
		return getClass().getSimpleName() + ifTypeString + " \"" + getText() +
		"\" [" + getBegin() + "," + getEnd() + "]";
	}
}