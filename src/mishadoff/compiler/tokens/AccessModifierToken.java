package mishadoff.compiler.tokens;

public class AccessModifierToken extends Token{

	/* TYPE
	 * 
	 * 0 - public
	 * 1 - private
	 * 2 - protected
	 */
	
	public AccessModifierToken(int begin, int end, String text, int type) {
		super(begin, end, text, type);
	}
	
}
