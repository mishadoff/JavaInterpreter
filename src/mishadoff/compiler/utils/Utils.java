package mishadoff.compiler.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import javax.jws.Oneway;

import mishadoff.compiler.grammar.Terminal;
import mishadoff.compiler.tokens.AccessModifierToken;
import mishadoff.compiler.tokens.CharConstantToken;
import mishadoff.compiler.tokens.CloseBraceToken;
import mishadoff.compiler.tokens.CloseSquareBraceToken;
import mishadoff.compiler.tokens.DivideEqualToken;
import mishadoff.compiler.tokens.DivideToken;
import mishadoff.compiler.tokens.DoubleConstantToken;
import mishadoff.compiler.tokens.EqualToken;
import mishadoff.compiler.tokens.IdentifierToken;
import mishadoff.compiler.tokens.IntConstantToken;
import mishadoff.compiler.tokens.MinusEqualToken;
import mishadoff.compiler.tokens.MinusMinusToken;
import mishadoff.compiler.tokens.MinusToken;
import mishadoff.compiler.tokens.ModEqualToken;
import mishadoff.compiler.tokens.ModToken;
import mishadoff.compiler.tokens.MultiplyEqualToken;
import mishadoff.compiler.tokens.MultiplyToken;
import mishadoff.compiler.tokens.OpenBraceToken;
import mishadoff.compiler.tokens.OpenSquareBraceToken;
import mishadoff.compiler.tokens.PlusEqualToken;
import mishadoff.compiler.tokens.PlusPlusToken;
import mishadoff.compiler.tokens.PlusToken;
import mishadoff.compiler.tokens.PointToken;
import mishadoff.compiler.tokens.ReservedWordToken;
import mishadoff.compiler.tokens.SeparatorToken;
import mishadoff.compiler.tokens.Token;
import mishadoff.compiler.tokens.TypeToken;
import mishadoff.compiler.tokens.VoidToken;

public class Utils {

	public static String readFileToString(String filePath) throws IOException{
		StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
	}
	
	public static int calculateTokensLength(List<Token> tokens){
		int length = 0;
		for (Token t : tokens){
			length += (t.getEnd() - t.getBegin());
		}
		return length;
	}
	
	/**
	 * Makes association between tokens and stack for LL(1)
	 * @param tokens
	 * @return
	 */
	public static Stack<Terminal> convertTokensToStack(List<Token> tokens){
		Stack<Terminal> source = new Stack<Terminal>();
		
		// Push end of stack
		source.push(new Terminal(0,null));
		
		ListIterator<Token> it = tokens.listIterator(tokens.size());
		while (it.hasPrevious()){
			Token token = it.previous();
			
			if (token instanceof IdentifierToken) {
				source.push(new Terminal(3, token)); // id
			}
			else if (token instanceof IntConstantToken) {
				source.push(new Terminal(33, token));	// intConst
			}
			else if (token instanceof DoubleConstantToken) {
				source.push(new Terminal(34, token));	// doubleConst
			}
			else if (token instanceof CharConstantToken) {
				source.push(new Terminal(35, token));	// charConst
			}
			else if (token instanceof PointToken) {
				source.push(new Terminal(4, token));	// .
			}
			else if (token instanceof VoidToken) {
				source.push(new Terminal(19, token));	// void
			}
			else if (token instanceof OpenBraceToken) {
				source.push(new Terminal(23, token));	// (
			}
			else if (token instanceof CloseBraceToken) {
				source.push(new Terminal(24, token));	// )
			}
			else if (token instanceof EqualToken) {
				source.push(new Terminal(27, token));	// =
			}
			else if (token instanceof PlusToken) {
				source.push(new Terminal(36, token));	// +
			}
			else if (token instanceof MinusToken) {
				source.push(new Terminal(37, token));	// -
			}
			else if (token instanceof MultiplyToken) {
				source.push(new Terminal(38, token));	// *
			}
			else if (token instanceof DivideToken) {
				source.push(new Terminal(39, token));	// /
			}
			else if (token instanceof ModToken) {
				source.push(new Terminal(40, token));	// %
			}
			else if (token instanceof PlusPlusToken) {
				source.push(new Terminal(41, token));	// ++
			}
			else if (token instanceof MinusMinusToken) {
				source.push(new Terminal(42, token));	// --
			}
			else if (token instanceof PlusEqualToken) {
				source.push(new Terminal(43, token));	// +=
			}
			else if (token instanceof MinusEqualToken) {
				source.push(new Terminal(44, token));	// -=
			}
			else if (token instanceof MultiplyEqualToken) {
				source.push(new Terminal(45, token));	// *=
			}
			else if (token instanceof DivideEqualToken) {
				source.push(new Terminal(46, token));	// /=
			}
			else if (token instanceof ModEqualToken) {
				source.push(new Terminal(47, token));	// %=
			}
			else if (token instanceof OpenSquareBraceToken) {
				source.push(new Terminal(30, token));	// [
			}
			else if (token instanceof CloseSquareBraceToken) {
				source.push(new Terminal(31, token));	// ]
			}
			else if (token instanceof AccessModifierToken) {
				switch (token.getType()) {
					case 0: { source.push(new Terminal(10, token)); break;} // public
					case 1: { source.push(new Terminal(11, token)); break;} // private
					case 2: { source.push(new Terminal(12, token)); break;} // protected
				}
			}
			else if (token instanceof ReservedWordToken) {
				switch (token.getType()) {
					case  0: { source.push(new Terminal(29, token)); break;} // false
					case  1:{ source.push(new Terminal(28, token)); break;} // true
					case  3:{ source.push(new Terminal(21, token)); break;} // return	
					case  4: { source.push(new Terminal(32, token)); break;} // new
					case  5: { source.push(new Terminal(6, token)); break;} // class
					case  6:{ source.push(new Terminal(22, token)); break;} // if
					case  7:{ source.push(new Terminal(26, token)); break;} // for
					case  8:{ source.push(new Terminal(48, token)); break;} // while
					case 11: { source.push(new Terminal(13, token)); break;} // static
					case 12: { source.push(new Terminal(14, token)); break;} // final
					case 13: { source.push(new Terminal(1, token)); break;} // package
					case 14: { source.push(new Terminal(5, token)); break;} // import
					case 15: { source.push(new Terminal(9, token)); break;} // extends
					case 16: { source.push(new Terminal(25, token)); break; } // else
				}
			}
			else if (token instanceof SeparatorToken) {
				switch (token.getType()) {
					case 0: { source.push(new Terminal(2, token)); break;} // ;
					case 1: { source.push(new Terminal(20, token)); break;} // ,
					case 2: { source.push(new Terminal(7, token)); break;} // {
					case 3: { source.push(new Terminal(8, token)); break;} // }
				}
			}
			else if (token instanceof TypeToken) {
				switch (token.getType()) {
					case 0: { source.push(new Terminal(15, token)); break;} // int
					case 1: { source.push(new Terminal(16, token)); break;} // double
					case 2: { source.push(new Terminal(18, token)); break;} // boolean
					case 3: { source.push(new Terminal(17, token)); break;} // char
				}
			}
			
			
		}
		
		return source;
	}
	
}
