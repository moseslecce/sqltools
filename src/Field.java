import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Field {
	private String name;
	private int displaySize;
	private String typeName;

	public Field(String name, int displaySize, String typeName) {
		this.name = name;
		this.displaySize = displaySize;
		this.typeName = typeName;
	}

	public static Field populateFromMetaData(ResultSetMetaData tableMd, int columnPos) throws SQLException {
		return new Field(tableMd.getColumnName(columnPos), tableMd.getColumnDisplaySize(columnPos), tableMd.getColumnTypeName(columnPos));
	}

	public String getName()
	{
		return this.name;
	}

	public int getDisplaySize()
	{
		return this.displaySize;
	}

	public String getTypeName()
	{
		return this.typeName;
	}
}
