package mishadoff.compiler.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mishadoff.compiler.tokens.IdentifierToken;
import mishadoff.compiler.tokens.Token;

public class IdentifierParser implements TokenParser {

	@Override
	public Token parseToken(String source, int fromIndex) {
		if (source == null){
			throw new IllegalArgumentException("source is null");
		}
		
		if (fromIndex >= source.length()) return null;
		
		Pattern p = Pattern.compile(".{"+fromIndex+"}\\b([a-zA-Z]{1}[0-9a-zA-Z]{0,31})\\b.*",
				Pattern.DOTALL);
		Matcher m = p.matcher(source);
		
		if (!m.matches()) return null;
		
		String mText = m.group(1);
		Token token = new IdentifierToken(fromIndex, fromIndex+mText.length(), mText);
		return token;
	}
	
}
