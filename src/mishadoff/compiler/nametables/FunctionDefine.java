package mishadoff.compiler.nametables;

import mishadoff.compiler.tokens.Token;

public class FunctionDefine {
	
	private Token start;
	private Token end;
	
	/* Relations with global list of tokens for optimization */
	private int startIndex;
	private int endIndex;
	
	public Token getStart() {
		return start;
	}
	public Token getEnd() {
		return end;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public void setStart(Token start) {
		this.start = start;
	}
	public void setEnd(Token end) {
		this.end = end;
	}
	
	public boolean contains(Token t){
		if (start.getBegin() <= t.getBegin() && end.getEnd() >= t.getEnd())
			return true;
		return false;
	}
	
	
}
