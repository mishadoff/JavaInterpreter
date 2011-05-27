package mishadoff.compiler.parser;

import java.util.ArrayList;
import java.util.List;

import mishadoff.compiler.tokens.Token;

public class GeneralTokensParser {

	private List<TokenParser> parsers = new ArrayList<TokenParser>();
	private List<Token> result;
	private int errorPos;
	
	/** Returns all parsers */
	public List<TokenParser> getParsers() {
		return parsers;
	}
	
	public void addParser(TokenParser tokenParser){
		parsers.add(tokenParser);
	}
	
	/** Returns all tokens */
	public List<Token> getTokens() {
		return result;
	}
	
	public int getDifference(){
		return errorPos;
	}
	
	/** Main parser 
	 * @throws Exception */
	public void parse(String source) throws Exception{
		// Begin parsing from start index
		int pos = 0;
		result = new ArrayList<Token>();
		
		// For all text
		while (true){
			Token token = null;
			
			// Sequentially look all parsers
			for(TokenParser parser : parsers){
				if (parser == null) continue;
				
				// try to parse token
				token = parser.parseToken(source, pos);
				
				// if token was found stopping to parse this source
				if (token != null){
					pos = token.getEnd();
					break;
				}
			}
			
			// No one parser not found lexem - error or end
			if (token == null) {
				break;
			}
			
			// Token was found - add it to result array
			result.add(token);
		}
	}
	
	
	
}
