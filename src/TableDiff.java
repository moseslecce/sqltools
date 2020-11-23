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
	private Map<String,DiffForeignKey> foreignKeys;
	private String action; // CREATE, ALTER, DROP

	public TableDiff(String tableName)
	{
		this.fields = new TreeMap<>();
		this.dropFields = new TreeMap<>();
		this.keys = new TreeMap<>();
		this.foreignKeys = new TreeMap<>();

		this.tableName = tableName;
		this.autoIncrement = null;
		this.engine = null;
		this.collation = null;
	}

	public String getAction()
	{
		return this.action;
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
		this.fields.put(field.getPosition(), new DiffField(field,CompareStatement.OPERATION_ADD));
	}

	public void addDifferentField(Field field)
	{
		this.fields.put(field.getPosition(), new DiffField(field,CompareStatement.OPERATION_CHANGE));
	}

	public void addExtraFields(Field field)
	{
		this.dropFields.put(field.getName(), new DiffField(field,CompareStatement.OPERATION_DROP));
	}

	public void addMissingKey(Key key) 
	{
		this.keys.put(key.getName(), new DiffKey(key,CompareStatement.OPERATION_ADD));
	}

	public void addDifferentKey(Key key)
	{
		this.keys.put(key.getName(), new DiffKey(key,CompareStatement.OPERATION_CHANGE));
	}

	public void addExtraKey(Key key)
	{
		this.keys.put(key.getName(), new DiffKey(key,CompareStatement.OPERATION_DROP));
	}

	public void addMissingForeignKey(ForeignKey fkey)
	{
		this.foreignKeys.put(fkey.getName(), new DiffForeignKey(fkey, CompareStatement.OPERATION_ADD));
	}

	public void addExtraForeignKey(ForeignKey fkey)
	{
		this.foreignKeys.put(fkey.getName(), new DiffForeignKey(fkey, CompareStatement.OPERATION_DROP));
	}

	public void addDifferentForeignKey(ForeignKey fkey)
	{
		this.foreignKeys.put(fkey.getName(), new DiffForeignKey(fkey, CompareStatement.OPERATION_CHANGE));
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

	public Map<String,DiffForeignKey> getForeignKeys()
	{
		return this.foreignKeys;
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

	public void setAction(String action) 
	{
		this.action = action;
	}
}
