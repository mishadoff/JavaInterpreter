package mishadoff.compiler.codegeneration;

import java.util.ArrayList;
import java.util.List;

import mishadoff.compiler.tokens.SeparatorToken;
import mishadoff.compiler.tokens.Token;

/**
 * Class finds statements for code generation.
 * @author mishadoff
 *
 */
public class StatementParser {

	List<Statement> statements;
	
	public void parseToStatements(List<Token> tokens){
		statements = new ArrayList<Statement>();
		
		// move to first {
		int currentBegin = 0;
		for (int i=0; i < tokens.size(); i++){
			Token t = tokens.get(i);
			if (t instanceof SeparatorToken &&
				t.getType() == 2) {
				currentBegin = i+1;
				break;
			}
		}
		
		// parsing
		for (int i = currentBegin; i < tokens.size(); i++){
			
		}
	}
	
	public void deleteEmptyStatements(List<Statement> statements){
		List<Statement> stats = new ArrayList<Statement>();
		for (int i = 0; i < statements.size(); i++){
			if (statements.get(i).getTokens().size() == 0)
				statements.remove(i);
		}
	}
	
	
	
}
