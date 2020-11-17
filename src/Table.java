import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Statement;

public class Table {
	private String name;
	private Map<String,Field> fields;

	public Table(String name, Map<String,Field> fields) {
		this.name = name;
		this.fields = fields;
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
	}

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