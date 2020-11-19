import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Table {
	private String name;	
	private String collation;
	private String engine;
	private Integer autoIncrement;

	private Map<String,Field> fields;
	private Map<Integer,Field> positions; //order,Field.
	private Map<String,Key> keys; // Name,Key
	private Map<String,ForeignKey> fks; // Name, ForeignKey

	public Table(String name, String collation, String engine, Integer autoIncrement) {
		this.fields = new HashMap<>();
		this.positions = new HashMap<>();
		this.keys = new HashMap<>();
		this.fks = new HashMap<>();

		this.name = name;
		this.collation = collation;
		this.engine = engine;

		if (autoIncrement > 0)
			this.autoIncrement = autoIncrement;
	}

	public static Table populateFromRS(ResultSet rs) throws SQLException {
		return new Table(rs.getString("TABLE_NAME"),rs.getString("TABLE_COLLATION"), rs.getString("ENGINE"), rs.getInt("AUTO_INCREMENT"));
	}

	public void addFieldFromRS(ResultSet rs) throws SQLException 
	{
		Field f = Field.populateFromRS(rs);
		this.fields.put(f.getName(), f);
		this.positions.put(f.getPosition(),f);
	}

	public void populateIndexesFromRS(ResultSet rs) throws SQLException 
	{
		String indexName = rs.getString("INDEX_NAME");
		if (this.keys.get(indexName) == null)
		{
			this.keys.put(indexName, Key.populateFromRS(rs));
		}
		else
		{
			Key k = this.keys.get(indexName);
			k.addColumnFromRS(rs);		
		}
	}
	
	public void populateForeignKeysFromRS(ResultSet rs) throws SQLException
	{
		String name = rs.getString("CONSTRAINT_NAME");
		if (this.fks.get(name) == null)
		{
			this.fks.put(name, ForeignKey.populateFromRS(rs));
		}
		else
		{
			ForeignKey k = this.fks.get(name);
			k.addColumnFromRS(rs);
		}
	}

	public String getName() {
		return this.name;
	}

	public int getNumFields() {
		return this.fields.size();
	}

	public Map<String,Field> getFields()
	{
		return this.fields;
	}

	public Map<String,Key> getKeys()
	{
		return this.keys;
	}

	public String getCollation()
	{
		return this.collation;
	}

	public String getEngine()
	{
		return this.engine;
	}

	public Integer getAutoIncrement()
	{
		return this.autoIncrement;
	}

	/*
	public static Table populateFromMetaData(ResultSetMetaData tableMd) throws SQLException {
		int numFields = tableMd.getColumnCount();
		Map<String,Field> fields = new HashMap<>();

		for (int i = 0; i < numFields; i++)
			fields.put(tableMd.getColumnName((i + 1)), Field.populateFromMetaData(tableMd, (i + 1)));

		return new Table(tableMd.getTableName(2), fields);
	}

	public static Table populateFromName(String name, Connection conn) throws SQLException
	{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from " + name + " limit 1;");
		ResultSetMetaData tableMd = rs.getMetaData();
		return Table.populateFromMetaData(tableMd);
	}*/

	public boolean hasField(String key) 
	{
		if (this.fields.get(key) != null)
			return true;

		return false;
	}

	public boolean hasKey(String name) {
		if (this.keys.get(name) != null)
			return true;

		return false;
	}

	public Key getKey(String name) {
		return this.keys.get(name);
	}

	public boolean hasForeignKey(String name) {
		if (this.fks.get(name) != null)
			return true;

		return false;
	}

	public ForeignKey getForeignKey(String name) {
		return this.fks.get(name);
	}

	public Map<String, ForeignKey> getForeignKeys() {
		return this.fks;
	}

	public Field getField(String key) {
		return this.fields.get(key);
	}

	public void setEngine(String val) {
		this.engine = val;
	}

	public void setCollation(String val) {
		this.collation = val;
	}

	public void populatePositions() 
	{
		for (Map.Entry<Integer,Field> entry : this.positions.entrySet())
		{
			Field f = entry.getValue();

			int prevPos = f.getPosition() - 1;
			if (prevPos >= 1)
			{
				Field afterField = this.positions.get(prevPos);
				if (afterField != null)
					f.setAfterField(afterField.getName());
			}
		}
	}
}