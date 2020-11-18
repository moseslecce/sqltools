import java.util.Map;

public class DiffKey extends Key {

	private String typeOfChange;

	public DiffKey(String name, boolean nonUnique, String indexType, Map<Integer,String> columns) {
		super(name, nonUnique, indexType, columns);
	}

	public DiffKey(Key key, String typeOfChange)
	{
		super(key.getName(), key.isNonUnique(), key.getIndexType(), key.getColumns());
		this.typeOfChange = typeOfChange;
	}

	public void setTypeOfChange(String val)
	{
		this.typeOfChange = val;
	}
	
	public String getOperation()
	{
		return this.typeOfChange;
	}
}
