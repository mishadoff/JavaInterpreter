package mishadoff.compiler.parser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mishadoff.compiler.tokens.AccessModifierToken;
import mishadoff.compiler.tokens.Token;

public class AccessModifierParser implements TokenParser{
	
	HashMap<String, Integer> types = new HashMap<String, Integer>();
	
	public AccessModifierParser(){
		types.put("public", 0);
		types.put("private", 1);
		types.put("protected", 2);
	}
	
	@Override
	public Token parseToken(String source, int fromIndex) throws Exception {
		if (source == null){
			throw new IllegalArgumentException("source is null");
		}
		
		if (fromIndex >= source.length()) return null;
		
		Pattern p = Pattern.compile(".{"+fromIndex+"}\\b(public|protected|private)\\b.*",
									Pattern.DOTALL);
		Matcher m = p.matcher(source);
		
		if (!m.matches()) return null;
		
		String mText = m.group(1);
		
		// find type
		Integer type = types.get(mText);
		if (type == null) {
			// Ideal for self exception
			throw new Exception("Illegal type");
		}
		
		Token token = new AccessModifierToken(fromIndex, 
				fromIndex+mText.length(), mText, type);
		return token;
	}

}
