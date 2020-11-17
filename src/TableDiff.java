import java.util.ArrayList;

public class TableDiff {

	private ArrayList<Field> missingFields;
	private ArrayList<Field> differentFields;
	private ArrayList<Field> extraFields;
	private String tableName;
	private Integer autoIncrement;
	private String engine;
	private String collation;
	
	public TableDiff(String tableName)
	{
		this.missingFields = new ArrayList<>();
		this.differentFields = new ArrayList<>();
		this.extraFields = new ArrayList<>();
		this.tableName = tableName;
		this.autoIncrement = null;
		this.engine = null;
		this.collation = null;
	}

	public void setEngine(String val)
	{
		this.engine = val;
	}

	public void setCollation(String val)
	{
		this.collation = val;
	}

	public void setAutoIncrement(Integer val)
	{
		this.autoIncrement = val;
	}

	public void addMissingField(Field field) 
	{
		this.missingFields.add(field);
	}

	public void addDifferentField(Field field)
	{
		this.differentFields.add(field);
	}

	public void addExtraFields(Field field)
	{
		this.extraFields.add(field);
	}

	public ArrayList<Field> getMissingFields()
	{
		return this.missingFields;
	}

	public String getTableName() {
		return this.tableName;
	}

	public ArrayList<Field> getDifferentFields() 
	{
		return this.differentFields;
	}

	public ArrayList<Field> getExtraFields() {
		return this.extraFields;
	}

	public Integer getAutoIncrement() {
		return this.autoIncrement;
	}

	public String getEngine() {
		return this.engine;
	}

	public String getCollation() {
		return this.collation;
	}
}
