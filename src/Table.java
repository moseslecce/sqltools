import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
}