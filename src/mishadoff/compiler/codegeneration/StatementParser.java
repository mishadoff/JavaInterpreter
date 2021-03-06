package mishadoff.compiler.codegeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.sun.xml.internal.bind.v2.runtime.Name;

import mishadoff.compiler.tokens.NewLineToken;
import mishadoff.compiler.tokens.SeparatorToken;
import mishadoff.compiler.tokens.Token;

/**
 * Class finds statements for code generation.
 * @author mishadoff
 *
 */
public class StatementParser {

	List<Statement> statements;
	
	static HashMap<String, Integer> ops = new HashMap<String, Integer>();
	static int NOT_IMPORTANT_PRIORITY = 33;
	
	int numberOfTriads = 0;
	
	{
		// Assign operators
		ops.put("=", 0);
		ops.put("+=", 0);
		ops.put("-=", 0);
		ops.put("*=", 0);
		ops.put("/=", 0);
		ops.put("%=", 0);
		
		ops.put("+", 10);
		ops.put("-", 10);
		
		ops.put("*", 20);
		ops.put("/", 20);
		ops.put("%", 20);
		
		ops.put("<", 5);
		ops.put(">", 5);
		ops.put("<=", 5);
		ops.put(">=", 5);
		ops.put("!=", 5);
		
		ops.put(".", 100);
		
		ops.put("(", NOT_IMPORTANT_PRIORITY); // 
	}
	
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
	
	public void deleteEmptyStatements(List<Statement> stats){
		for (int i = 0; i < stats.size(); i++){
			if (stats.get(i).getTokens().size() == 0) {
				stats.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Delete for statements.
	 * Do not call if exists empty statements.
	 * @param stats
	 */
	public void deleteForStatements(List<Statement> stats){
		for (int i = 0; i < stats.size(); i++){
			if (stats.get(i).getTokens().get(0).getText().equals("for")){
				stats.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Delete while statements.
	 * Do not call if exists empty statements.
	 * @param stats
	 */
	public void deleteWhileStatements(List<Statement> stats){
		for (int i = 0; i < stats.size(); i++){
			if (stats.get(i).getTokens().get(0).getText().equals("while")){
				stats.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Delete for statements.
	 * Do not call if exists empty statements.
	 * @param stats
	 */
	public void deleteIfStatements(List<Statement> stats){
		for (int i = 0; i < stats.size(); i++){
			if (stats.get(i).getTokens().get(0).getText().equals("if")){
				stats.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Transform statement to poliz
	 * @param statement
	 * @return
	 */
	public List<PolizObject> transformToPoliz(Statement statement){
		List<PolizObject> output = new ArrayList<PolizObject>();
		List<Token> inputTokens = statement.getTokens();
		
		Stack<PolizObject> functionStack = new Stack<PolizObject>();
		Stack<PolizObject> stack = new Stack<PolizObject>();
		
		boolean functionBegin = false;
		
		// delete last ;
		if (inputTokens.get(inputTokens.size()-1) instanceof SeparatorToken &&
			inputTokens.get(inputTokens.size()-1).getType() == 0){
			inputTokens.remove(inputTokens.size()-1);
		}
			
		for (int i = 0; i < inputTokens.size(); i++){
			Token token = inputTokens.get(i);
			String tokenString = token.getText();
			
			if (tokenString.equals("(")) {
				stack.push(new PolizObject(tokenString));
			}
			else if (tokenString.equals(")")){
				if (functionBegin) {
					functionStack.peek().decArg();
					functionBegin = false;
				}
				while (!stack.peek().getName().equals("(")){
					if (stack.peek().isFunction()) functionStack.pop();
					output.add(stack.pop());
					// pop f
				}
				stack.pop(); // pop (
			}
			else if (tokenString.equals(",")){
				functionStack.peek().addArg();
				// peek all to func
				while (!stack.peek().isFunction()){
					output.add(stack.pop());
				}
			}
			else if (ops.containsKey(tokenString)) {
				if (!stack.isEmpty()) {
					while (!stack.isEmpty()) {
						PolizObject previousOp = stack.peek();
						if ((previousOp.isFunction()) ||
							(ops.get(previousOp.getName()) == NOT_IMPORTANT_PRIORITY)  ||
							(ops.get(previousOp.getName()) < ops.get(tokenString))) {
							// correct binary operation
							stack.push(new PolizObject(tokenString,PolizType.BIN_OPERATION));
							break;
						}
						else {
							output.add(stack.pop());
						}
					}
				}
				else {
					stack.push(new PolizObject(tokenString, PolizType.BIN_OPERATION));
				}
			}
			else {
				// Check if function
				if (functionBegin) functionBegin = false;
				Token next;
				if (i <= inputTokens.size() - 2) {
					next = inputTokens.get(i+1);
					if (next.getText().equals("(")) {
						stack.push(new PolizObject("("));
						PolizObject function = new PolizObject(tokenString, PolizType.FUNCTION);
						stack.push(function);
						functionStack.push(function);
						function.addArg();
						functionBegin = true;
						i++;
						continue; // ?
					}
				}
				output.add(new PolizObject(tokenString));
			}
		}
		
		// Pop all remain operators
		while (!stack.isEmpty()){
			output.add(stack.pop());
		}
		
		return output;
	}
	
	/**
	 * Converts poliz to triads
	 */
	public List<Triad> transformPolizToTriads(List<PolizObject> poliz){
		List<Triad> triads = new ArrayList<Triad>();
		int currentPolizSize = poliz.size();
		for (int i=0; i < currentPolizSize; i++){
			PolizObject pObject = poliz.get(i);
			if (pObject.isActive()) {
				if (pObject.isUnaryOperator()) {
					throw new NotImplementedException();
				}
				else if (pObject.isBinaryOperator()){
					PolizObject operand1 = poliz.get(i-2);
					PolizObject operand2 = poliz.get(i-1);
					Triad triad = new Triad(
							numberOfTriads,
							pObject.getName(),
							operand1.getName(),
							operand2.getName()
						);
					triads.add(triad);
					// delete all
					poliz.remove(i-2);
					poliz.remove(i-2);
					
					currentPolizSize -= 2;
					
					//poliz.remove(i-2);
					// poliz.ge
					poliz.set(i-2, new PolizObject("@[" + numberOfTriads + "]",
							PolizType.LINK_TO_TRIAD));
					numberOfTriads++;
				}
				else if (pObject.isFunction()){
					int num = pObject.getArgs();
					PolizObject arg;
					for (int k = num; k > 0; k--){
						arg = poliz.get(i-k);
						Triad triad = new Triad(
								numberOfTriads,
								"PUSH",
								arg.getName(),
								""
							);
						triads.add(triad);
						numberOfTriads++;
					}
					Triad triad = new Triad(
							numberOfTriads,
							"CALL",
							pObject.getName(),
							""
						);
					triads.add(triad);
					
					for (int k = num; k > 0; k--){
						poliz.remove(i-num);
					}
					
					currentPolizSize -= num;
					
					poliz.set(i-num, new PolizObject("@[" + numberOfTriads + "]",
							PolizType.LINK_TO_TRIAD));
					
					numberOfTriads++;
					// TODO implement function behaviour
				}
				i = 0;
			}
		}
		return triads;
	}
	
}
