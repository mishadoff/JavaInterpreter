package mishadoff.compiler.grammar;

public class InfoRule extends Rule {
	String text;
	int tokenAt;
	
	// -777 code for info rules
	public InfoRule(String text, int tokenAt) {
		super(-777, null, null);
		this.text = text;
		this.tokenAt = tokenAt;
	}
	
	// -777 code for info rules
	public InfoRule(String text) {
		super(-777, null, null);
		this.text = text;
		this.tokenAt = 0;
	}

	public String getText() {
		return text;
	}

	public int getTokenAt() {
		return tokenAt;
	}
	
}
