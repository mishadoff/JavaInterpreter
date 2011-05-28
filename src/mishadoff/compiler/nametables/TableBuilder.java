package mishadoff.compiler.nametables;

import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;

import mishadoff.compiler.tokens.CloseBraceToken;
import mishadoff.compiler.tokens.IdentifierToken;
import mishadoff.compiler.tokens.OpenBraceToken;
import mishadoff.compiler.tokens.ReservedWordToken;
import mishadoff.compiler.tokens.SeparatorToken;
import mishadoff.compiler.tokens.Token;
import mishadoff.compiler.tokens.TypeToken;
import mishadoff.compiler.tokens.VoidToken;

public class TableBuilder {

	List<FunctionDefine> functionDefines = new ArrayList<FunctionDefine>();

	List<Token> lexTokens;
	Block top;

	/**
	 * Method build name tables via tokens from lex
	 * 
	 * @param tokens
	 */
	public void buildNameTables(List<Token> tokens, Block topBlock) {
		lexTokens = tokens;
		top = topBlock;
		final int SIZE = tokens.size();
		final int startBlock = topBlock.getStart();

		// Move to significant id
		int currentTokenIndex = 0;
		while (true) {
			Token token = tokens.get(currentTokenIndex);
			if (token.getBegin() >= startBlock)
				break;
			currentTokenIndex++;
		}

		// Find defines
		for (int j = currentTokenIndex; j < SIZE; j++) {
			Token token = tokens.get(j);
			if (token instanceof IdentifierToken) {
				Token nextToken = tokens.get(j + 1);
				Token previousToken = tokens.get(j - 1);

				if (nextToken instanceof OpenBraceToken) {
					// token is function
					// add this defining to non-trackable for variables
					FunctionDefine fDefine = new FunctionDefine();
					fDefine.setStart(nextToken);
					fDefine.setStartIndex(j+1);
					int endIndex = findFunctionEnd(j + 2);
					fDefine.setEnd(tokens.get(endIndex));
					fDefine.setEndIndex(endIndex);
					functionDefines.add(fDefine);
					if (previousToken instanceof IdentifierToken
							|| previousToken instanceof TypeToken
							|| previousToken instanceof VoidToken) {

						FunctionTableEntry entry = new FunctionTableEntry();
						entry.setIdentifier(token);
						entry.setType(previousToken);

						// TODO compose list of function arguments

						topBlock.addFunctionByCoords(entry);
					}
				} else {
					if (!isInFunctionDefines(token)) {
						if (previousToken instanceof IdentifierToken
								|| previousToken instanceof TypeToken) {
							// cut for vars
							if (tokens.get(j-2) instanceof OpenBraceToken &&
								tokens.get(j-3) instanceof ReservedWordToken &&
								tokens.get(j-3).getType() == 7) {
								System.out.println("*");
							}
							else {
								VariableTableEntry entry = new VariableTableEntry();
								entry.setIdentifier(token);
								entry.setType(previousToken);
								topBlock.addVariableByCoords(entry);
							}
						} else if (previousToken instanceof SeparatorToken
								&& previousToken.getType() == 1) {
							// this token after comma
							int k = j - 2;
							while (true) {
								Token first = tokens.get(k - 1);
								Token second = tokens.get(k);
								// check for type
								if ((first instanceof IdentifierToken || first instanceof TypeToken)
										&& second instanceof IdentifierToken) {
									if (tokens.get(k + 1) instanceof OpenBraceToken) {
									} else {
										VariableTableEntry entry = new VariableTableEntry();
										entry.setIdentifier(token);
										entry.setType(first);
										topBlock.addVariableByCoords(entry);
									}
									break;
								}
								k--;
							}
						}
					}
				}
			}
		}
		
		// second walk: function arguments to next block
		for (FunctionDefine fDefine : functionDefines) {
			for (int i = fDefine.getStartIndex(); i < fDefine.getEndIndex(); i++){
				Token curToken = lexTokens.get(i);
				if (curToken instanceof IdentifierToken){
					Token previousToken = lexTokens.get(i-1);
					if (previousToken instanceof IdentifierToken ||
						previousToken instanceof TypeToken) {
							VariableTableEntry entry = new VariableTableEntry();
							entry.setIdentifier(curToken);
							entry.setType(previousToken);
							top.addVariableToNextBlockFromIndex(entry);
					}
				}
			}
		}
		
		// third walk: for arguments to next block
		for (int i=0; i < lexTokens.size(); i++){
			Token token = lexTokens.get(i);
			if (token instanceof ReservedWordToken &&
				token.getType() == 7){
				Token tempToken = lexTokens.get(i+3);
				if (tempToken instanceof IdentifierToken){
					Token prevToken = lexTokens.get(i+2);
					if (prevToken instanceof IdentifierToken ||
						prevToken instanceof TypeToken){
						VariableTableEntry entry = new VariableTableEntry();
						entry.setIdentifier(tempToken);
						entry.setType(prevToken);
						top.addVariableToNextBlockFromIndex(entry);
					}
				}
			}
		}
	}

	/**
	 * From passed index find the close brace
	 * 
	 * @param start
	 * @return
	 */
	public int findFunctionEnd(int start) {
		int curIndex = start;
		Token curToken;
		int numOfUnbalancedBraces = 1;

		while (true) {
			curToken = lexTokens.get(curIndex);
			if (curToken instanceof CloseBraceToken)
				numOfUnbalancedBraces--;
			if (curToken instanceof OpenBraceToken)
				numOfUnbalancedBraces++;
			if (numOfUnbalancedBraces == 0)
				break;
			curIndex++;
		}
		return curIndex;
	}

	public boolean isInFunctionDefines(Token token) {
		for (FunctionDefine fDefine : functionDefines) {
			if (fDefine.contains(token))
				return true;
		}
		return false;
	}
}
