package mishadoff.compiler.codegeneration;

import java.util.ArrayList;
import java.util.List;

import mishadoff.compiler.tokens.Token;

/**
 * Class describe one statement for code generation.
 * @author mishadoff
 *
 */
public class Statement {
	List<Token> tokens;

	public Statement() {
		tokens = new ArrayList<Token>();
	}
	
	public Statement(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	public void add(Token token){
		tokens.add(token);
	}

	public List<Token> getTokens() {
		return tokens;
	}

}
