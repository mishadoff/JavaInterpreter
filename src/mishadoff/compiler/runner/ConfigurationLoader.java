package mishadoff.compiler.runner;

import mishadoff.compiler.grammar.Grammar;
import mishadoff.compiler.grammar.NonTerminal;
import mishadoff.compiler.grammar.Rule;
import mishadoff.compiler.grammar.Symbol;
import mishadoff.compiler.grammar.Terminal;
import mishadoff.compiler.parser.*;

public class ConfigurationLoader {
	
	public static void loadParsers(GeneralTokensParser parser){
		parser.addParser(new BlockCommentParser()); // /* Block comments */
		parser.addParser(new LineCommentParser()); // // Line comments
		
		parser.addParser(new WhiteSpaceParser()); // {space}
		parser.addParser(new OpenBraceParser()); // (
		parser.addParser(new CloseBraceParser()); // )
		parser.addParser(new OpenSquareBraceParser()); // [
		parser.addParser(new CloseSquareBraceParser()); // ]
		parser.addParser(new SeparatorParser()); // ; , { }
		parser.addParser(new DoubleConstantParser()); // 2.3
		parser.addParser(new IntConstantParser()); // 312
		parser.addParser(new VoidParser()); // void
		parser.addParser(new TypeParser()); // int|double|boolean|char
		//parser.addParser(new CharConstantParser()); 
		parser.addParser(new TabParser()); // \t
		parser.addParser(new NewLineParser()); // \n
		parser.addParser(new AccessModifierParser()); // public|private|protected
		parser.addParser(new ReservedWordParser()); // reserved words
		
		/* Arythmetic parsers */
		parser.addParser(new PointParser()); // .
		parser.addParser(new PlusPlusParser()); // ++
		parser.addParser(new PlusEqualParser()); // +=
		parser.addParser(new PlusParser()); // +
		parser.addParser(new MinusMinusParser()); // --
		parser.addParser(new MinusEqualParser()); // -=
		parser.addParser(new MinusParser()); // -
		parser.addParser(new EqualEqualParser()); // ==
		parser.addParser(new EqualParser()); // =
		parser.addParser(new LessEqualParser()); // <=
		parser.addParser(new LessParser()); // <
		parser.addParser(new GreaterEqualParser()); // >=
		parser.addParser(new GreaterParser()); // >
		parser.addParser(new MultiplyEqualParser()); // *=
		parser.addParser(new MultiplyParser()); // *
		parser.addParser(new ExclameEqualParser()); // !=
		parser.addParser(new ExclameParser()); // !
		parser.addParser(new DivideEqualParser()); // /=
		parser.addParser(new DivideParser()); // /
		parser.addParser(new ModEqualParser()); // %=
		parser.addParser(new ModParser()); // %
		parser.addParser(new AndParser()); // &&
		parser.addParser(new OrParser()); // ||
		
		// Last parser
		parser.addParser(new IdentifierParser());
	}
	
	/**
	 * Nature order
	 * @param g
	 */
	public static void loadRules(Grammar g){
		// Program -> PackageDecl ImportDecl ClassDecl
		g.addRule(new Rule(
				1,
				new NonTerminal(1),
				new Symbol[]{new NonTerminal(2), new NonTerminal(3), new NonTerminal(4)}
			));
		
		// PackageDecl -> package FullClassName ;
		g.addRule(new Rule(
				2,
				new NonTerminal(2),
				new Symbol[]{new Terminal(1), new NonTerminal(5), new Terminal(2)}
			));
		
		// FullClassName -> id FullClassNameContinue
		g.addRule(new Rule(
				3,
				new NonTerminal(5),
				new Symbol[]{new Terminal(3), new NonTerminal(6)}
			));
		
		// FullClassNameContinue -> . id FullClassNameContinue 
		g.addRule(new Rule(
				4,
				new NonTerminal(6),
				new Symbol[]{new Terminal(4), new Terminal(3), new NonTerminal(6)}
			));
		
		// FullClassNameContinue -> null
		g.addRule(new Rule(
				5,
				new NonTerminal(6),
				new Symbol[]{}
			));
		
		// ImportDecl -> ImportLine ImportDecl
		g.addRule(new Rule(
				6,
				new NonTerminal(3),
				new Symbol[]{new NonTerminal(7), new NonTerminal(3)}
			));
		
		// ImportDecl -> null
		g.addRule(new Rule(
				7,
				new NonTerminal(3),
				new Symbol[]{}
			));
		
		// ImportLine -> import FullClassName ;
		g.addRule(new Rule(
				8,
				new NonTerminal(7),
				new Symbol[]{new Terminal(5), new NonTerminal(5), new Terminal(2)}
			));
		
		// ClassDecl -> Modifiers class id Extension { ClassBody }
		g.addRule(new Rule(
				9,
				new NonTerminal(4),
				new Symbol[]{new NonTerminal(8), new Terminal(6), new Terminal(3),
							 new NonTerminal(9), new Terminal(7), new NonTerminal(10),
							 new Terminal(8)}
			));
		
		// Extension -> extends id
		g.addRule(new Rule(
				10,
				new NonTerminal(9),
				new Symbol[]{new Terminal(9), new Terminal(3)}
			));
		
		// Extension -> null
		g.addRule(new Rule(
				11,
				new NonTerminal(9),
				new Symbol[]{}
			));
		
		// Modifiers -> AccessModifier StaticModifier FinalModifier
		g.addRule(new Rule(
				12,
				new NonTerminal(8),
				new Symbol[]{new NonTerminal(11), new NonTerminal(12), new NonTerminal(13)}
			));

		// AccessModifier -> public
		g.addRule(new Rule(
				13,
				new NonTerminal(11),
				new Symbol[]{new Terminal(10)}
			));
		
		// AccessModifier -> protected
		g.addRule(new Rule(
				14,
				new NonTerminal(11),
				new Symbol[]{new Terminal(12)}
			));
		
		// AccessModifier -> private
		g.addRule(new Rule(
				15,
				new NonTerminal(11),
				new Symbol[]{new Terminal(11)}
			));
		
		// AccessModifier -> null
		g.addRule(new Rule(
				16,
				new NonTerminal(11),
				new Symbol[]{}
			));
		
		// StaticModifier -> static
		g.addRule(new Rule(
				17,
				new NonTerminal(12),
				new Symbol[]{new Terminal(13)}
			));
		
		// StaticModifier -> null
		g.addRule(new Rule(
				18,
				new NonTerminal(12),
				new Symbol[]{}
			));
		
		// FinalModifier -> final
		g.addRule(new Rule(
				19,
				new NonTerminal(13),
				new Symbol[]{new Terminal(14)}
			));
		
		// FinalModifier -> null
		g.addRule(new Rule(
				20,
				new NonTerminal(13),
				new Symbol[]{}
			));
		
		// ClassBody -> ClassMember ClassBody
		g.addRule(new Rule(
				21,
				new NonTerminal(10),
				new Symbol[]{new NonTerminal(14), new NonTerminal(10)}
			));
		
		// ClassBody -> null
		g.addRule(new Rule(
				22,
				new NonTerminal(10),
				new Symbol[]{}
			));
		
		// ClassMember -> Modifiers RestOfClassMember
		g.addRule(new Rule(
				23,
				new NonTerminal(14),
				new Symbol[]{new NonTerminal(8), new NonTerminal(15)}
			));
		
		// RestOfClassMember -> Type RestOfClassMember2
		g.addRule(new Rule(
				24,
				new NonTerminal(15),
				new Symbol[]{new NonTerminal(16), new NonTerminal(17)}
			));
		
		// RestOfClassMember -> void RestOfClassMember2
		g.addRule(new Rule(
				25,
				new NonTerminal(15),
				new Symbol[]{new Terminal(19), new NonTerminal(17)}
			));
		
		// Type -> id
		g.addRule(new Rule(
				26,
				new NonTerminal(16),
				new Symbol[]{new Terminal(3)}
			));
		
		// BasicType -> int
		g.addRule(new Rule(
				27,
				new NonTerminal(38),
				new Symbol[]{new Terminal(15)}
			));
		
		// BasicType -> double
		g.addRule(new Rule(
				28,
				new NonTerminal(38),
				new Symbol[]{new Terminal(16)}
			));
		
		// BasicType -> char
		g.addRule(new Rule(
				29,
				new NonTerminal(38),
				new Symbol[]{new Terminal(17)}
			));
		
		// BasicType -> boolean
		g.addRule(new Rule(
				30,
				new NonTerminal(38),
				new Symbol[]{new Terminal(18)}
			));
		
		// RestOfClassMember2 -> id MethodOrAttr
		g.addRule(new Rule(
				31,
				new NonTerminal(17),
				new Symbol[]{new Terminal(3), new NonTerminal(18)}
			));
		
		// MethodOrAttr -> MethodContinue
		g.addRule(new Rule(
				32,
				new NonTerminal(18),
				new Symbol[]{new NonTerminal(19)}
			));
		
		// MethodOrAttr -> AttrContinue
		g.addRule(new Rule(
				33,
				new NonTerminal(18),
				new Symbol[]{new NonTerminal(20)}
			));
		
		// MethodContinue -> (MethodParams){MethodBody}
		g.addRule(new Rule(
				34,
				new NonTerminal(19),
				new Symbol[]{new Terminal(23), new NonTerminal(21), new Terminal(24),
							 new Terminal(7), new NonTerminal(22), new Terminal(8)}
			));
		
		// MethodParams -> OneParam MethodParamContinue
		g.addRule(new Rule(
				35,
				new NonTerminal(21),
				new Symbol[]{new NonTerminal(23), new NonTerminal(24)}
			));
		
		// OneParam -> Type id
		g.addRule(new Rule(
				36,
				new NonTerminal(23),
				new Symbol[]{new NonTerminal(16), new Terminal(3)}
			));
		
		// MethodParamContinue -> , OneParam MethodParamContinue
		g.addRule(new Rule(
				37,
				new NonTerminal(24),
				new Symbol[]{new Terminal(20), new NonTerminal(23), new NonTerminal(24)}
			));
		
		// MethodParams -> null
		g.addRule(new Rule(
				38,
				new NonTerminal(21),
				new Symbol[]{}
			));
		
		// MethodParamContinue -> null
		g.addRule(new Rule(
				39,
				new NonTerminal(24),
				new Symbol[]{}
			));
		
		// AttrContinue -> AssignAttr NextAttr
		g.addRule(new Rule(
				40,
				new NonTerminal(20),
				new Symbol[]{new NonTerminal(25), new NonTerminal(26)}
			));
		
		// AssignAttr -> null
		g.addRule(new Rule(
				41,
				new NonTerminal(25),
				new Symbol[]{}
			));
		
		// AssignAttr -> = Expression
		g.addRule(new Rule(
				42,
				new NonTerminal(25),
				new Symbol[]{new Terminal(27), new NonTerminal(27)}
			));
		
		// NextAttr -> ;
		g.addRule(new Rule(
				43,
				new NonTerminal(26),
				new Symbol[]{new Terminal(2)}
			));
		
		// NextAttr -> , id AttrContinue
		g.addRule(new Rule(
				44,
				new NonTerminal(26),
				new Symbol[]{new Terminal(20), new Terminal(3), new NonTerminal(20)}
			));
		
		// MethodBody -> Statement MethodBody
		g.addRule(new Rule(
				45,
				new NonTerminal(22),
				new Symbol[]{new NonTerminal(28), new NonTerminal(22)}
			));
		
		// MethodBody -> null
		g.addRule(new Rule(
				46,
				new NonTerminal(22),
				new Symbol[]{}
			));
		
		// Statement -> BlockDecl
		g.addRule(new Rule(
				47,
				new NonTerminal(28),
				new Symbol[]{new NonTerminal(29)}
			));
		
		// BlockDecl -> { MethodBody }
		g.addRule(new Rule(
				48,
				new NonTerminal(29),
				new Symbol[]{new Terminal(7), new NonTerminal(22), new Terminal(8)}
			));
		
		// Expression -> Term ExprOp
		g.addRule(new Rule(
				49,
				new NonTerminal(27),
				new Symbol[]{new NonTerminal(30), new NonTerminal(31)}
			));
		
		// Term -> intConst
		g.addRule(new Rule(
				50,
				new NonTerminal(30),
				new Symbol[]{new Terminal(33)}
			));
		
		// Term -> doubleConst
		g.addRule(new Rule(
				51,
				new NonTerminal(30),
				new Symbol[]{new Terminal(34)}
			));
		
		// Term -> charConst
		g.addRule(new Rule(
				52,
				new NonTerminal(30),
				new Symbol[]{new Terminal(35)}
			));
		
		// Term -> Variable
		g.addRule(new Rule(
				53,
				new NonTerminal(30),
				new Symbol[]{new NonTerminal(33)}
			));
		
		// ExprOp -> ArithmOp Term ExprOp
		g.addRule(new Rule(
				54,
				new NonTerminal(31),
				new Symbol[]{new NonTerminal(32), new NonTerminal(30), new NonTerminal(31)}
			));
		
		// ExprOp -> null
		g.addRule(new Rule(
				55,
				new NonTerminal(31),
				new Symbol[]{}
			));
		
		// Term -> ( Expression )
		g.addRule(new Rule(
				56,
				new NonTerminal(30),
				new Symbol[]{new Terminal(23), new NonTerminal(27), new Terminal(24)}
			));
		
		// Variable -> id FunctionExt Link
		g.addRule(new Rule(
				57,
				new NonTerminal(33),
				new Symbol[]{new Terminal(3), new NonTerminal(34), new NonTerminal(37)}
			));
		
		// Link -> . id FunctionExt Link
		g.addRule(new Rule(
				58,
				new NonTerminal(37),
				new Symbol[]{new Terminal(4), new Terminal(3), new NonTerminal(34), new NonTerminal(37)}
			));
		
		// Link -> [ Expression ] Link
		g.addRule(new Rule(
				59,
				new NonTerminal(37),
				new Symbol[]{new Terminal(30), new NonTerminal(27), new Terminal(31), new NonTerminal(37)}
			));
		
		// Link -> null
		g.addRule(new Rule(
				60,
				new NonTerminal(37),
				new Symbol[]{}
			));
		
		// FunctionExt -> ( MethodValues )
		g.addRule(new Rule(
				61,
				new NonTerminal(34),
				new Symbol[]{new Terminal(23), new NonTerminal(35), new Terminal(24)}
			));
		
		// FunctionExt -> null
		g.addRule(new Rule(
				62,
				new NonTerminal(34),
				new Symbol[]{}
			));
		
		// Term -> new id ( MethodValues )
		g.addRule(new Rule(
				63,
				new NonTerminal(30),
				new Symbol[]{new Terminal(32), new Terminal(3), new Terminal(23),
							 new NonTerminal(35), new Terminal(24)}
			));

		// MethodValues -> Expression ValueContinue
		g.addRule(new Rule(
				64,
				new NonTerminal(35),
				new Symbol[]{new NonTerminal(27), new NonTerminal(36)}
			));
		
		// ValueContinue -> , Expression ValueContinue
		g.addRule(new Rule(
				65,
				new NonTerminal(36),
				new Symbol[]{new Terminal(20), new NonTerminal(27), new NonTerminal(36)}
			));
		
		// ValueContinue -> null
		g.addRule(new Rule(
				66,
				new NonTerminal(36),
				new Symbol[]{}
			));
		
		// ArithmOp -> +
		g.addRule(new Rule(
				67,
				new NonTerminal(32),
				new Symbol[]{new Terminal(36)}
			));
		
		// ArithmOp -> -
		g.addRule(new Rule(
				68,
				new NonTerminal(32),
				new Symbol[]{new Terminal(37)}
			));
		
		// ArithmOp -> *
		g.addRule(new Rule(
				69,
				new NonTerminal(32),
				new Symbol[]{new Terminal(38)}
			));
		
		// ArithmOp -> /
		g.addRule(new Rule(
				70,
				new NonTerminal(32),
				new Symbol[]{new Terminal(39)}
			));
		
		// ArithmOp -> %
		g.addRule(new Rule(
				71,
				new NonTerminal(32),
				new Symbol[]{new Terminal(40)}
			));
		
		// Statement -> Expression ;
		g.addRule(new Rule(
				72,
				new NonTerminal(28),
				new Symbol[]{new NonTerminal(27), new Terminal(2)}
			));
		
		// MethodValues -> null
		g.addRule(new Rule(
				73,
				new NonTerminal(35),
				new Symbol[]{}
			));
		
		/* test */
		// ArithmOp -> =
		g.addRule(new Rule(
				74,
				new NonTerminal(32),
				new Symbol[]{new Terminal(27)}
			));
		
		// ArithmOp -> +=
		g.addRule(new Rule(
				75,
				new NonTerminal(32),
				new Symbol[]{new Terminal(43)}
			));
		
		// ArithmOp -> -=
		g.addRule(new Rule(
				76,
				new NonTerminal(32),
				new Symbol[]{new Terminal(44)}
			));
		
		// ArithmOp -> *=
		g.addRule(new Rule(
				77,
				new NonTerminal(32),
				new Symbol[]{new Terminal(45)}
			));
		
		// ArithmOp -> /=
		g.addRule(new Rule(
				78,
				new NonTerminal(32),
				new Symbol[]{new Terminal(46)}
			));
		
		// ArithmOp -> %=
		g.addRule(new Rule(
				79,
				new NonTerminal(32),
				new Symbol[]{new Terminal(47)}
			));
		
		// Statement -> IfBlock
		g.addRule(new Rule(
				80,
				new NonTerminal(28),
				new Symbol[]{new NonTerminal(39)}
			));
		
		// Statement -> WhileBlock
		g.addRule(new Rule(
				81,
				new NonTerminal(28),
				new Symbol[]{new NonTerminal(40)}
			));
		
		// Statement -> ForBlock
		g.addRule(new Rule(
				82,
				new NonTerminal(28),
				new Symbol[]{new NonTerminal(41)}
			));
		
		// Statement -> ReturnBlock
		g.addRule(new Rule(
				83,
				new NonTerminal(28),
				new Symbol[]{new NonTerminal(42)}
			));
		
		// Statement -> VarDecl
		g.addRule(new Rule(
				84,
				new NonTerminal(28),
				new Symbol[]{new NonTerminal(43)}
			));
		
		// IfBlock -> if ( BoolExpr ) Statement ElseBlock
		g.addRule(new Rule(
				85,
				new NonTerminal(39),
				new Symbol[]{new Terminal(22), new Terminal(23), new NonTerminal(44),
							 new Terminal(24), new NonTerminal(28), new NonTerminal(45)}
			));
		
		// ElseBlock -> else Statement
		g.addRule(new Rule(
				86,
				new NonTerminal(45),
				new Symbol[]{new Terminal(25), new NonTerminal(28)}
			));
		
		// WhileBlock -> while ( BoolExpr ) Statement
		g.addRule(new Rule(
				87,
				new NonTerminal(40),
				new Symbol[]{new Terminal(48), new Terminal(23), new NonTerminal(44),
							 new Terminal(24), new NonTerminal(28)}
			));
		
		// ForBlock -> for (Type id = Expression; BoolExpr; Expression) Statement
		g.addRule(new Rule(
				88,
				new NonTerminal(41),
				new Symbol[]{new Terminal(26), new Terminal(23), new NonTerminal(16), 
							 new Terminal(3), new Terminal(27), new NonTerminal(27),
							 new Terminal(2), new NonTerminal(44), new Terminal(2),
							 new NonTerminal(27), new Terminal(24), new NonTerminal(28)}
			));
		
		// ReturnBlock -> return Expression ;
		g.addRule(new Rule(
				89,
				new NonTerminal(42),
				new Symbol[]{new Terminal(21), new NonTerminal(27), new Terminal(2)}
			));
		
		// VarDecl -> BasicType FirstVar NextVar ;
		g.addRule(new Rule(
				90,
				new NonTerminal(43),
				new Symbol[]{new NonTerminal(38), new NonTerminal(46),
							 new NonTerminal(47), new Terminal(2)}
			));
		
		// NextVar -> , FirstVar NextVar
		g.addRule(new Rule(
				91,
				new NonTerminal(47),
				new Symbol[]{new Terminal(20), new NonTerminal(46),
							 new NonTerminal(47)}
			));
		
		// NextVar -> null
		g.addRule(new Rule(
				92,
				new NonTerminal(47),
				new Symbol[]{}
			));
		
		// FirstVar -> id AssignAttr
		g.addRule(new Rule(
				93,
				new NonTerminal(46),
				new Symbol[]{new Terminal(3), new NonTerminal(25)}
			));
		
		// BoolExpr -> true
		g.addRule(new Rule(
				94,
				new NonTerminal(44),
				new Symbol[]{new Terminal(28)}
			));
		
		// BoolExpr -> false
		g.addRule(new Rule(
				95,
				new NonTerminal(44),
				new Symbol[]{new Terminal(29)}
			));
		
		// Type -> BasicType
		g.addRule(new Rule(
				96,
				new NonTerminal(16),
				new Symbol[]{new NonTerminal(38)}
			));
		
	}
	
}
