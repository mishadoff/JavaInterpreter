package mishadoff.compiler.nametables;

/**
 * Class describes semantic conflict in checkings
 * @author mishadoff
 *
 */
public class SemanticConflict {
	private Block block1;
	private Block block2;
	
	private int doubleInBlock1;
	private int doubleInBlock2;
	
	private Table table;
	
	public SemanticConflict(Block block1, Block block2, int doubleInBlock1,
			int doubleInBlock2, Table table) {
		this.block1 = block1;
		this.block2 = block2;
		this.doubleInBlock1 = doubleInBlock1;
		this.doubleInBlock2 = doubleInBlock2;
		this.table = table;
	}

	public Block getBlock1() {
		return block1;
	}
	public Block getBlock2() {
		return block2;
	}
	public int getDoubleInBlock1() {
		return doubleInBlock1;
	}
	public int getDoubleInBlock2() {
		return doubleInBlock2;
	}
	public void setBlock1(Block block1) {
		this.block1 = block1;
	}
	public void setBlock2(Block block2) {
		this.block2 = block2;
	}
	public void setDoubleInBlock1(int doubleInBlock1) {
		this.doubleInBlock1 = doubleInBlock1;
	}
	public void setDoubleInBlock2(int doubleInBlock2) {
		this.doubleInBlock2 = doubleInBlock2;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
}
