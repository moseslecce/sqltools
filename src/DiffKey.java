

public class DiffKey extends Key {

	private String typeOfChange;

	public DiffKey(String name, boolean nonUnique, String indexType) {
		super(name, nonUnique, indexType);
	}

	public DiffKey(Key key, String typeOfChange)
	{
		super(key.getName(), key.isNonUnique(), key.getIndexType());
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
