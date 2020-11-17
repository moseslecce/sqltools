import java.sql.ResultSet;
import java.sql.SQLException;

public class Field {
	private String name;
	private String columnType;
	private String typeName;
	private Integer precision = null;
	private Integer scale = null;
	private boolean isNullable;

	public Field(String name, String columnType, String typeName, Integer precision, Integer scale, boolean isNullable) {
		this.name = name;
		this.typeName = typeName;
		this.columnType = columnType;

		if (precision > 0)
			this.precision = precision;

		if (scale > 0)
			this.scale = scale;

		this.isNullable = isNullable;
	}

	/*
	public static Field populateFromMetaData(ResultSetMetaData tableMd, int columnPos) throws SQLException {
		return new Field(tableMd.getColumnName(columnPos), tableMd.getColumnType(columnPos), tableMd.getColumnTypeName(columnPos), tableMd.getPrecision(columnPos), tableMd.getScale(columnPos), tableMd.isNullable(columnPos) != ResultSetMetaData.columnNoNulls);
	}*/

	public static Field populateFromRS(ResultSet rs) throws SQLException {
		return new Field(rs.getString("COLUMN_NAME"), rs.getString("COLUMN_TYPE"), rs.getString("DATA_TYPE"), rs.getInt("NUMERIC_PRECISION"), rs.getInt("NUMERIC_SCALE"), rs.getString("IS_NULLABLE").equals("YES"));
	}

	public String getName()
	{
		return this.name;
	}

	public String getColumnType()
	{
		return this.columnType;
	}

	public String getTypeName()
	{
		return this.typeName;
	}

	public boolean isNullable() {
		return this.isNullable;
	}

	//@TODO: Implement me.
	public String getDefaultValue() {
		return "NULL";
	}

	//@TODO: Implement me.
	public boolean isUnsigned() {
		return false;
	}

	public Integer getPrecision() {
		return this.precision;
	}

	public Integer getScale() {
		return this.scale;
	}

	@Override
    public boolean equals(Object o) { 
		// If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
		}
		
		if (!(o instanceof Field)) { 
            return false; 
		}
		
		Field f = (Field) o;
		return f.getScale() == this.getScale() && f.getPrecision() == this.getPrecision() && f.getDefaultValue() == this.getDefaultValue() && f.getTypeName().equals(this.getTypeName());
	}
}
