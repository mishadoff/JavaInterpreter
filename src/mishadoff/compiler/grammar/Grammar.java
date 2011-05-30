package mishadoff.compiler.grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import mishadoff.compiler.codegeneration.Statement;

/**
 * Class describes language grammar
 * @author mishadoff
 */
public class Grammar {
	
	
	private int numOfTerminals = 100;
	private int numOfNonterminals = 100;
	
	List<Statement> statements = new ArrayList<Statement>();
	
	Statement st = new Statement();
	
	/**
	 * Table describes LL(1) parsing rules
	 * first index - code of NonTerminal (0 not uses)
	 * second index - code of Terminal (0 - means null)
	 * value - number of rule for parsing
	 */
	public int[][] table = new int[numOfNonterminals+1][numOfTerminals+2];
	
	{
		// Program
		table[1][1] = 1;
		
		// PackageDecl
		table[2][1] = 2;
		
		// ImportDecl
		table[3][5] = 6;
		
		table[3][6] = 7;
		table[3][10] = 7;
		table[3][11] = 7;
		table[3][12] = 7;
		table[3][13] = 7;
		table[3][14] = 7;
		
		// ClassDecl
		
		table[4][6] = 9;
		table[4][10] = 9;
		table[4][11] = 9;
		table[4][12] = 9;
		table[4][13] = 9;
		table[4][14] = 9;
		
		// FullClassName
		table[5][3] = 3;

		// FullClassNameContinue
		table[6][2] = 5;
		table[6][4] = 4;
		
		// ImportLine
		table[7][5] = 8;
		
		// Modifiers
		table[8][6] = 12;
		table[8][10] = 12;
		table[8][11] = 12;
		table[8][12] = 12;
		table[8][13] = 12;
		table[8][14] = 12;
		table[8][15] = 12;
		table[8][16] = 12;
		table[8][17] = 12;
		table[8][18] = 12;
		table[8][19] = 12;
		
		// Extension
		table[9][9] = 10;
		table[9][7] = 11;
		
		// AccessModifier
		table[11][6] = 16;
		table[11][10] = 13;
		table[11][11] = 15;
		table[11][12] = 14;
		table[11][13] = 16;
		table[11][14] = 16;
		
		table[11][3] = 16;
		table[11][15] = 16;
		table[11][16] = 16;
		table[11][17] = 16;
		table[11][18] = 16;
		table[11][19] = 16;
		
		// StaticModifier
		table[12][6] = 18;
		table[12][13] = 17;
		table[12][14] = 18;
		
		table[12][3] = 18;
		table[12][15] = 18;
		table[12][16] = 18;
		table[12][17] = 18;
		table[12][18] = 18;
		table[12][19] = 18;
		
		// FinalModifier
		table[13][6] = 20;
		table[13][14] = 19;
		
		table[13][3] = 20;
		table[13][15] = 20;
		table[13][16] = 20;
		table[13][17] = 20;
		table[13][18] = 20;
		table[13][19] = 20;
		
		// ClassBody
		table[10][8] = 22;
		table[10][3] = 21;
		table[10][10] = 21;
		table[10][11] = 21;
		table[10][12] = 21;
		table[10][13] = 21;
		table[10][14] = 21;
		table[10][15] = 21;
		table[10][16] = 21;
		table[10][17] = 21;
		table[10][18] = 21;
		table[10][19] = 21;
		
		// ClassMember
		table[14][3] = 23;
		table[14][10] = 23;
		table[14][11] = 23;
		table[14][12] = 23;
		table[14][13] = 23;
		table[14][14] = 23;
		table[14][15] = 23;
		table[14][16] = 23;
		table[14][17] = 23;
		table[14][18] = 23;
		table[14][19] = 23;
		
		// RestOfClassMember
		table[15][3] = 24;
		table[15][15] = 24;
		table[15][16] = 24;
		table[15][17] = 24;
		table[15][18] = 24;
		table[15][19] = 25;
		
		// RestOfClassMember2
		table[17][3] = 31;
		
		// MethodOrAttr
		table[18][2] = 33;
		table[18][20] = 33;
		table[18][27] = 33;
		table[18][23] = 32;
		
		// MethodContinue
		table[19][23] = 34;
		
		// AttrContinue
		table[20][2] = 40;
		table[20][20] = 40;
		table[20][27] = 40;
		
		// MethodParams
		table[21][3] = 35;
		table[21][15] = 35;
		table[21][16] = 35;
		table[21][17] = 35;
		table[21][18] = 35;
		table[21][24] = 38;
		
		// MethodBody
		table[22][7] = 45;
		table[22][8] = 46;
		
		table[22][3] = 45;
		table[22][33] = 45;
		table[22][34] = 45;
		table[22][35] = 45;
		
		table[22][32] = 45;
		table[22][23] = 45;
		
		
		table[22][15] = 45;
		table[22][16] = 45;
		table[22][17] = 45;
		table[22][18] = 45;	

		table[22][22] = 45;
		table[22][48] = 45;
		table[22][26] = 45;
		table[22][21] = 45;
		
		// OneParam
		table[23][3] = 36;
		table[23][15] = 36;
		table[23][16] = 36;
		table[23][17] = 36;
		table[23][18] = 36;
		
		// AssignAttr
		table[25][2] = 41;
		table[25][20] = 41;
		table[25][27] = 42;
		
		// NextAttr
		table[26][20] = 44;
		table[26][2] = 43;
		
		// MethodParamContinue
		table[24][20] = 37;
		table[24][24] = 39;
		
		// Type
		table[16][3] = 26;
			//table[16][15] = 27;
			//table[16][16] = 28;
			//table[16][17] = 29;
			//table[16][18] = 30;
		table[16][15] = 96;
		table[16][16] = 96;
		table[16][17] = 96;
		table[16][18] = 96;
		
		// BasicType
		table[38][15] = 27;
		table[38][16] = 28;
		table[38][17] = 29;
		table[38][18] = 30;
		
		
		// Expression
		table[27][3] = 49;
		table[27][33] = 49;
		table[27][34] = 49;
		table[27][35] = 49;
		
		table[27][32] = 49;
		table[27][23] = 49;
		
		// BlockDecl
		table[29][7] = 48;
		
		// FunctionExt
		table[34][23] = 61;
		
		table[34][24] = 62;
		table[34][36] = 62;
		table[34][37] = 62;
		table[34][38] = 62;
		table[34][39] = 62;
		
		/* test */
		table[34][27] = 62;
		table[34][43] = 62;
		table[34][44] = 62;
		table[34][45] = 62;
		table[34][46] = 62;
		table[34][47] = 62;
		      
		
		table[34][40] = 62;
		table[34][20] = 62;
		table[34][2] = 62;
		table[34][4] = 62;
		table[34][30] = 62;
		table[34][31] = 62;
		
		// ValueContinue
		table[36][24] = 66;
		table[36][20] = 65;
		
		// ExprOp
		table[31][36] = 54;
		table[31][37] = 54;
		table[31][38] = 54;
		table[31][39] = 54;
		table[31][40] = 54;

		/* test */
		table[31][27] = 54;
		table[31][43] = 54;
		table[31][44] = 54;
		table[31][45] = 54;
		table[31][46] = 54;
		table[31][47] = 54;
		
		table[31][20] = 55;
		table[31][2] = 55;
		table[31][31] = 55;
		table[31][24] = 55;
		
		// ArithmOp
		table[32][36] = 67;
		table[32][37] = 68;
		table[32][38] = 69;
		table[32][39] = 70;
		table[32][40] = 71;
		/* test */
		table[32][27] = 74;
		table[32][43] = 75;
		table[32][44] = 76;
		table[32][45] = 77;
		table[32][46] = 78;
		table[32][47] = 79;
		
		
		// Link
		table[37][36] = 60;
		table[37][37] = 60;
		table[37][38] = 60;
		table[37][39] = 60;
		table[37][40] = 60;
		
		/* test */
		table[37][27] = 60;
		table[37][43] = 60;
		table[37][44] = 60;
		table[37][45] = 60;
		table[37][46] = 60;
		table[37][47] = 60;
		
		table[37][20] = 60;
		table[37][2] = 60;
		table[37][24] = 60;
		table[37][31] = 60;
	
		table[37][30] = 59;
		
		table[37][4] = 58;
		
		// MethodValues
		table[35][3] = 64;
		table[35][33] = 64;
		table[35][34] = 64;
		table[35][35] = 64;
		
		table[35][32] = 64;
		table[35][23] = 64;
		
			// test
			table[35][24] = 73;
		
		// Term
		table[30][3] = 53;
		table[30][33] = 50;
		table[30][34] = 51;
		table[30][35] = 52;
		
		table[30][32] = 63;
		table[30][23] = 56;
		
		// Variable
		table[33][3] = 57;
		
		// Statement
		table[28][7] = 47;
		
		table[28][3] = 72;
		table[28][33] = 72;
		table[28][34] = 72;
		table[28][35] = 72;
		
		table[28][32] = 72;
		table[28][23] = 72;
		
		table[28][15] = 84;
		table[28][16] = 84;
		table[28][17] = 84;
		table[28][18] = 84;	
		
		table[28][22] = 80;
		table[28][48] = 81;
		table[28][26] = 82;
		table[28][21] = 83;
		
		// IfBlock
		table[39][22] = 85;
		
		// ElseBlock
		table[45][25] = 86;
		
		// WhileBlock
		table[40][48] = 87;
		
		// ForBlock
		table[41][26] = 88;
		
		// ReturnBlock
		table[42][21] = 89;
		
		// FirstVar
		table[46][3] = 93;
		
		// NextVar
		table[47][2] = 92;
		table[47][20] = 91;
		
		// VarDecl
		table[43][15] = 90;
		table[43][16] = 90;
		table[43][17] = 90;
		table[43][18] = 90;
		
		// BooleanExpression
		table[44][28] = 94;
		table[44][29] = 95;
		
	}
	
	private List<Rule> rules = new ArrayList<Rule>();
	
	public void addRule(Rule r){
		rules.add(r);
	}
	
	public List<Rule> getRules(){
		return rules;
	}
	
	/** Calculate first for all rules */
	public void calculateFirst(){
		for (Rule rule : rules) {
			first(rule);
		} 
	}
	
	/** Calculate array */
	public HashSet<Terminal> first(Rule rule){
		// if lambda rule 
		if (rule.getRightSide() == null) {
			HashSet<Terminal> set = new HashSet<Terminal>();
			set.add(new Terminal(0));
			return set;
		}
		else {
			
		}
		return null;
	}
	
	/**
	 * Parses grammar with LL(1) algorithm
	 * @param input
	 * @return
	 */
	public List<Rule> parse(Stack<Terminal> input){
		List<Rule> usingRules = new ArrayList<Rule>();
		Stack<Symbol> stack = new Stack<Symbol>();
		boolean statementTrackEnabled = false;
		
		//! MODIFIED CODE
		int stackCounter = -1;
		//!
		
		if (input.isEmpty()) {
			System.out.println("Input is empty");
			return null;
		}
		
		// Push End symbol
		stack.push(new Terminal(0));
		
		// Push Start symbol
		stack.push(new NonTerminal(1));

		int parseTokenCount = 0;
		// Try to parse input
		while (true) {
			
			// If magazine is empty
			if (stack.isEmpty()) {
				if (input.isEmpty()) {
					//System.out.println("Parsing complete!");
					usingRules.add(new InfoRule("good"));
				}
				else {
					//System.out.println("Parsing error at " + (parseTokenCount+1) + " token");
					usingRules.add(new InfoRule("bad", parseTokenCount+1));
				}
				return usingRules;
			}
			Symbol stackTop = stack.peek();
			 
			//! MODIFIED CODE
			// 28 - statement
			if (stackCounter == 0) {
				statements.add(st);
				st = new Statement();
				statementTrackEnabled = false;
			}
			//!
			
			
			// If on top of stack is nonterminal
			if (stackTop instanceof NonTerminal) {
				int i = stackTop.getCode();
				
				//! MODIFIED CODE
				if (!statementTrackEnabled) {
					if (i == 39) {
						statementTrackEnabled = true;
						stackCounter = 1;
						st = new Statement();
					}
					if (i == 40) {
						statementTrackEnabled = true;
						stackCounter = 1;
						st = new Statement();
					}
					if (i == 41) {
						statementTrackEnabled = true;
						stackCounter = 1;
						st = new Statement();
					}
					if (i == 28 &&
						(input.peek().getCode() != 7)) {
						statementTrackEnabled = true;
						stackCounter = 1;
						st = new Statement();
					}
				}
				//!
					if (input.isEmpty()) {
						//System.out.println("Parsing error at " + (parseTokenCount+1) + " token");
						usingRules.add(new InfoRule("bad", parseTokenCount+1));
						return usingRules;
					}
				int j = input.peek().getCode();
				if (table[i][j] != 0){
					int rule = table[i][j];
					Rule r = rules.get(rule - 1);
					
					// Change magazine
						stack.pop(); // Delete left part of rule
						if (statementTrackEnabled) stackCounter--;
						
					// Add all right part of rule in inverse order
					for (int k = r.getRightSide().length - 1; k >= 0; k--) {
						stack.push(r.getRightSide()[k]);
						if (statementTrackEnabled) stackCounter++;
					}
					// Increment parse token count
					parseTokenCount++;
					usingRules.add(r);
				}
				else {
					//System.out.println("Parsing error at " + (parseTokenCount+1) + " token");
					usingRules.add(new InfoRule("bad", parseTokenCount+1));
					return usingRules;
				}
			}
			// If on top is terminal
			else if (stackTop instanceof Terminal){
				if (stackTop.getCode() == input.peek().getCode()) {
					stack.pop();
					Terminal t = input.pop();
					st.add(t.token);
					stackCounter--;
				}
				else {
					//System.out.println("Parsing error at " + (parseTokenCount+1) + " token");
					usingRules.add(new InfoRule("bad", parseTokenCount+1));
					return usingRules;
				}
			}
		}
	}

	public List<Statement> getStatements() {
		return statements;
	}
	
}
