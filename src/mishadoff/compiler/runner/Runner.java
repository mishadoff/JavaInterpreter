package mishadoff.compiler.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import mishadoff.compiler.grammar.Grammar;
import mishadoff.compiler.grammar.InfoRule;
import mishadoff.compiler.grammar.Rule;
import mishadoff.compiler.grammar.Terminal;
import mishadoff.compiler.nametables.Block;
import mishadoff.compiler.nametables.BlockBuilder;
import mishadoff.compiler.nametables.SemanticChecker;
import mishadoff.compiler.nametables.SemanticConflict;
import mishadoff.compiler.nametables.Table;
import mishadoff.compiler.nametables.TableBuilder;
import mishadoff.compiler.nametables.TableEntry;
import mishadoff.compiler.nametables.VariableTableEntry;
import mishadoff.compiler.parser.*;
import mishadoff.compiler.tokens.BlockCommentToken;
import mishadoff.compiler.tokens.IdentifierToken;
import mishadoff.compiler.tokens.LineCommentToken;
import mishadoff.compiler.tokens.NewLineToken;
import mishadoff.compiler.tokens.TabToken;
import mishadoff.compiler.tokens.Token;
import mishadoff.compiler.tokens.WhiteSpaceToken;
import mishadoff.compiler.utils.Utils;

public class Runner {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		GeneralTokensParser parser = new GeneralTokensParser();
		
		// load parser configurations
		ConfigurationLoader.loadParsers(parser);
		
		String filePath = "resources" + File.separator + "MiniGrammar.java";
		String source = Utils.readFileToString(filePath);
		source += "\n";
		//String source = "d// int a = 3 //\n int a = b; // firt;";
		
		System.out.println("Source: [" + source.length() + "]\n" + source + "\n");

		// parse the source
		parser.parse(source);
		
		// results of parsing
		for (Token token: parser.getTokens()) {
			System.out.println(token);
		}
		
		// complete or incomplete message
		int sourceBeforeLength = source.length();
		int sourceAfterLength = Utils.calculateTokensLength(parser.getTokens());
		
		boolean lexComplete = (sourceAfterLength == sourceBeforeLength);
		
		System.out.println("-----------------------------------------------");
		String resultString = ((lexComplete)  ? "Lex Parsing complete!"
									: "Lex Parsing error at pos " + sourceAfterLength);
		System.out.print("Result: " + resultString);
		
		
		// Syntax Analyzer
		if (lexComplete){
			
			System.out.println("\n-----------------------------------------------");
			// Filtering whitespace tokens
			final List<Token> tokens = new ArrayList<Token>();
			
			for (Token token: parser.getTokens()) {
				if (token instanceof WhiteSpaceToken ||
					token instanceof NewLineToken ||
					token instanceof TabToken ||
					token instanceof BlockCommentToken ||
					token instanceof LineCommentToken)
				continue;	
				tokens.add(token);
			}
			
			// parsing
			Grammar g = new Grammar();
			
			// load rules for grammar
			ConfigurationLoader.loadRules(g);
			
			// convert list of tokens to stack for parser
			Stack<Terminal> stack = Utils.convertTokensToStack(tokens);
			
			// make parsing
			List<Rule> usesRules = g.parse(stack);
			
			InfoRule infoRule = 
				(InfoRule) usesRules.get(usesRules.size()-1);
			usesRules.remove(usesRules.size()-1);
			
			boolean parseComplete = false;
			
			// show rules
			for (Rule rule : usesRules) {
				System.out.println("Using rule: " + rule.getRuleNumber());
			}
			
			System.out.println("-----------------------------------------------");
			System.out.print("Result: ");
			if (infoRule.getText().equals("good")) {
				System.out.println("Parsing complete! [" + usesRules.size() + "]");
				parseComplete = true;
			}
			else {
				System.out.println("Parsing error at " + infoRule.getTokenAt() + " token");
				parseComplete = false;
			}
			
			
			// Building nametable
			if (parseComplete) {
				
				BlockBuilder builder = new BlockBuilder();
				builder.buildBlockStructure(tokens);
			
				System.out.println("-----------------------------------------------");
				System.out.println("Constructing name tables");
				System.out.println("-----------------------------------------------");
				System.out.println("Current block structure: \n");
				System.out.println(builder.getCurrentBlock());
			
				Block topBlock = builder.getCurrentBlock();
				
				TableBuilder tableBuilder = new TableBuilder();
				tableBuilder.buildNameTables(tokens, topBlock);
				
				System.out.println("Name Tables \n");
				
				System.out.println(topBlock.printRecursiveTables());
				
				
				SemanticChecker checker = new SemanticChecker();
				boolean b = checker.checkDoubleThrough(topBlock);
				if (b) {
					System.out.println("All name tables correct");
				}
				else {
					for (SemanticConflict error : checker.getErrors()){
						Block block1 = error.getBlock1();
						Block block2 = error.getBlock2();
						int conflict1 = error.getDoubleInBlock1();
						int conflict2 = error.getDoubleInBlock2();
						Table table = error.getTable();
						if (block1.equals(block2)) {
							TableEntry vEntry;
							if (table == Table.VARIABLE) {
								vEntry = block1.getVariableTableEntries().get(conflict1);
							}
							else {
								vEntry = block1.getFunctionNameTable().get(conflict1);
							}
							
							System.out.println("Conflict in block#" + block1.getId() +
									": " + vEntry.getIdentifier().getText());
						}
						else {
							TableEntry vEntry;
								vEntry = block1.getVariableTableEntries().get(conflict1);

							
							System.out.println("Conflict between block #" + block1.getId() +
									" and block #" + block2.getId() + ": " + vEntry.getIdentifier().getText());
						
						}
						
					}
				}
				
				
				
				
				// TODO check for visibility
				
				if (!b) {
					System.out.println("\n");
					System.out.println(" === INTERACTIVE THROUGH LOOKUP === ");
					System.out.println(" <stop> - exit interactive lookup");
					System.out.println(" <idName>,<blockNum> - finds block");
					Scanner scanner = new Scanner(System.in);
					while (true){
						System.out.print(">");
						String line = scanner.nextLine();
						if (line.equals("stop")) break;
						else {
							StringTokenizer tokenizer = 
								new StringTokenizer(line, ",");
							if (tokenizer.countTokens() != 2) continue;
							String id = tokenizer.nextToken().trim();
							int num = -1;
							try {
								num = Integer.parseInt(tokenizer.nextToken().trim());
							}
							catch (NumberFormatException e) {
								continue;
							}
							
							// all good
							Block testBlock = topBlock.getBlockById(num);
							if (testBlock == null) {
								System.out.println("Block not found!");
							}
							else {
								int blockNum = testBlock.findThrough(id);
								if (blockNum == -1) {
									System.out.println("Identifier not found!");
								}
								else {
									System.out.println("Identifier found in " +
											blockNum + " block");
									TableEntry tEntry = topBlock.getBlockById(blockNum)
														.findIdentifier(id);
									System.out.println(tEntry.getIdentifier().getText() + 
											"   " + tEntry.getType().getText());
								}
							}
						}
					}
				}
				
			}
			
		}
		
	}

}
