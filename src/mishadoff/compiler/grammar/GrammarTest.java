package mishadoff.compiler.grammar;

import java.util.List;
import java.util.Stack;

import mishadoff.compiler.runner.ConfigurationLoader;

public class GrammarTest {

	public static void main(String[] args) {
		Grammar g = new Grammar();
		
		ConfigurationLoader.loadRules(g);
		
		/*
		 * PARSING
		 */
		

		
		Stack<Terminal> source = new Stack<Terminal>();
		// package id.id.id; import id; import id.id;
		source.push(new Terminal(0)); // Push end-symbol
		source.push(new Terminal(2)); // ;
		source.push(new Terminal(3)); // id
		source.push(new Terminal(4)); // .
		source.push(new Terminal(3)); // id
		source.push(new Terminal(5)); // import
		source.push(new Terminal(2)); // ;
		source.push(new Terminal(3)); // id
		source.push(new Terminal(5)); // import
		source.push(new Terminal(2)); // ;
		source.push(new Terminal(3)); // id
		source.push(new Terminal(4)); // .
		source.push(new Terminal(3)); // id
		source.push(new Terminal(4)); // .
		source.push(new Terminal(3)); // id
		source.push(new Terminal(1)); // package
		
		
		List<Rule> usesRules = g.parse(source);
		
		for (Rule rule : usesRules) {
			System.out.println("Using rule: " + rule.getRuleNumber());
		}
		
	}
	
}
