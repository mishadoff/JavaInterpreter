package mishadoff.compiler.parser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mishadoff.compiler.tokens.ReservedWordToken;
import mishadoff.compiler.tokens.Token;

public class ReservedWordParser implements TokenParser{
	
	HashMap<String, Integer> types = new HashMap<String, Integer>();
	
	public ReservedWordParser(){
		types.put("false", 0);
		types.put("true", 1);
		types.put("null", 2);
		types.put("return", 3);
		types.put("new", 4);
		types.put("class", 5);
		types.put("if", 6);
		types.put("for", 7);
		types.put("while", 8);
		//types.put("break", 9);
		//types.put("continue", 10);
		types.put("static", 11);
		types.put("final", 12);
		types.put("package", 13);
		types.put("import", 14);
		types.put("extends", 15);
		types.put("else", 16);
	}
	
	@Override
	public Token parseToken(String source, int fromIndex) throws Exception {
		if (source == null){
			throw new IllegalArgumentException("source is null");
		}
		
		if (fromIndex >= source.length()) return null;
		
		Pattern p = Pattern.compile(".{"+fromIndex+"}\\b(false|true|null|return|" +
				"new|class|if|else|for|while|static|final|" +
				"package|import|extends)\\b.*", Pattern.DOTALL);
		Matcher m = p.matcher(source);
		
		if (!m.matches()) return null;
		
		String mText = m.group(1);
		
		// find type
		Integer type = types.get(mText);
		if (type == null) {
			// Ideal for self exception
			throw new Exception("Illegal type");
		}
		
		Token token = new ReservedWordToken(fromIndex, 
				fromIndex+mText.length(), mText, type);
		return token;
	}

}
