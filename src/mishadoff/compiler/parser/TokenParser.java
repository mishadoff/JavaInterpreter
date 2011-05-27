package mishadoff.compiler.parser;

import mishadoff.compiler.tokens.Token;

public interface TokenParser {

	/**
	 * Parses source string and returns Token
	 * @param source - source code
	 * @param fromIndex - begin index for parsing
	 * @return - Token if founded, else null
	 * @throws Exception 
	 */
	public Token parseToken(String source, int fromIndex) throws Exception;
	
}
