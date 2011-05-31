package mishadoff.compiler.codegeneration;

/**
 * Class describes poliz objects
 * @author mishadoff
 *
 */
public class PolizObject {
	private String name;
	private PolizType type;
	private int args = 0;
	
	public PolizObject(String name) {
		this.name = name;
		type = PolizType.SIMPLE;
	}
	
	public PolizObject(String name, PolizType type) {
		this.name = name;
		this.type = type;
	}

	public void addArg(){
		args++;
	}
	
	public void decArg(){
		args--;
	}
	
	public String getName() {
		return name;
	}

	public PolizType getType() {
		return type;
	}

	public int getArgs() {
		return args;
	}
	
	public boolean isFunction(){
		return (type == PolizType.FUNCTION);
	}
	
	public boolean isBinaryOperator(){
		return (type == PolizType.BIN_OPERATION);
	}
	
	public boolean isUnaryOperator(){
		return (type == PolizType.UNARY_OPERATION);
	}
	
	public boolean isActive(){
		return isFunction() || isBinaryOperator() || isUnaryOperator();
	}
	
	@Override
	public String toString() {
		if (type == PolizType.FUNCTION) {
			return getName() + "[" + args + "]";
		}
		else {
			return getName();
		}
	}
	
	
}
