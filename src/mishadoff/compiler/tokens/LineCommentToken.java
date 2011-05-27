package mishadoff.compiler.tokens;

public class LineCommentToken extends Token {

	public LineCommentToken(int begin, int end, String text) {
		super(begin, end, text);
	}

}
