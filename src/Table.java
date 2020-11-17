import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Table {
	private String name;
	private Map<String,Field> fields;
	private String collation;
	private String engine;
	private Integer autoIncrement;

	public Table(String name, String collation, String engine, Integer autoIncrement) {
		this.fields = new HashMap<>();
		this.name = name;
		this.collation = collation;
		this.engine = engine;
		
		if (autoIncrement > 0)
			this.autoIncrement = autoIncrement;
	}

	public static Table populateFromRS(ResultSet rs) throws SQLException {
		return new Table(rs.getString("TABLE_NAME"),rs.getString("TABLE_COLLATION"), rs.getString("ENGINE"), rs.getInt("AUTO_INCREMENT"));
	}

	public void updateFromRS(ResultSet rs) throws SQLException 
	{
		Field f = Field.populateFromRS(rs);
		this.fields.put(f.getName(), f);
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

	public Field getField(String key) {
		return this.fields.get(key);
	}
}