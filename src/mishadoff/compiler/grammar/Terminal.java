package mishadoff.compiler.grammar;

import mishadoff.compiler.tokens.Token;

public class Terminal extends Symbol{

	Token token;
	
	public Terminal(int code) {
		super(code);
	}
	
	public Terminal(int code, Token token) {
		super(code);
		this.token = token;
	}

}
