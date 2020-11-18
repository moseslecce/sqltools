import java.util.Map;
import java.util.TreeMap;

public class TableDiff {

	private String tableName;
	private Integer autoIncrement;
	private String engine;
	private String collation;
	private Map<Integer,DiffField> fields; // sorted by the position.
	private Map<String,DiffField> dropFields; // sorted by the key name.
	private Map<String,DiffKey> keys;

	public TableDiff(String tableName)
	{
		this.fields = new TreeMap<>();
		this.dropFields = new TreeMap<>();
		this.keys = new TreeMap<>();

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
		this.fields.put(field.getPosition(), new DiffField(field,"ADD"));
	}

	public void addDifferentField(Field field)
	{
		this.fields.put(field.getPosition(), new DiffField(field,"CHANGE"));
	}

	public void addExtraFields(Field field)
	{
		this.dropFields.put(field.getName(), new DiffField(field,"DROP"));
	}

	public void addMissingKey(Key key) 
	{
		this.keys.put(key.getName(), new DiffKey(key,"ADD"));
	}

	public void addDifferentKey(Key key)
	{
		this.keys.put(key.getName(), new DiffKey(key,"CHANGE"));
	}

	public void addExtraKey(Key key)
	{
		this.keys.put(key.getName(), new DiffKey(key,"DROP"));
	}

	public Map<Integer,DiffField> getFields()
	{
		return this.fields;
	}

	public Map<String,DiffField> getDropFields()
	{
		return this.dropFields;
	}

	public Map<String,DiffKey> getKeys()
	{
		return this.keys;
	}

	public String getTableName() {
		return this.tableName;
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
