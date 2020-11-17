import java.sql.ResultSet;
import java.sql.SQLException;

public class Field {
	private String name;
	private String columnType;
	private String typeName;
	private Integer precision = null;
	private Integer scale = null;
	private Integer characterMaxLen = null;
	private boolean isNullable;
	private String colDefault = null;
	private Integer position = null;
	private String afterField = null;

	public Field(String name, Integer position, String columnType, String typeName, Integer precision, Integer scale, Integer characterMaxLen, boolean isNullable, String colDefault) {
		this.name = name;
		this.typeName = typeName;
		this.columnType = columnType;
		this.characterMaxLen = characterMaxLen;
		this.position = position;

		if (precision > 0)
			this.precision = precision;

		if (scale > 0)
			this.scale = scale;

		this.isNullable = isNullable;
		this.colDefault = colDefault;
	}

	/*
	public static Field populateFromMetaData(ResultSetMetaData tableMd, int columnPos) throws SQLException {
		return new Field(tableMd.getColumnName(columnPos), tableMd.getColumnType(columnPos), tableMd.getColumnTypeName(columnPos), tableMd.getPrecision(columnPos), tableMd.getScale(columnPos), tableMd.isNullable(columnPos) != ResultSetMetaData.columnNoNulls);
	}*/

	public Integer getPosition()
	{
		return this.position;
	}

	public static Field populateFromRS(ResultSet rs) throws SQLException {
		return new Field(rs.getString("COLUMN_NAME"), rs.getInt("ORDINAL_POSITION"), rs.getString("COLUMN_TYPE"), rs.getString("DATA_TYPE"), rs.getInt("NUMERIC_PRECISION"), rs.getInt("NUMERIC_SCALE"), rs.getInt("CHARACTER_MAXIMUM_LENGTH"), rs.getString("IS_NULLABLE").equals("YES"), rs.getString("COLUMN_DEFAULT"));
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

	public String getDefaultValue() {
		return this.colDefault;
	}

	//TODO: Implement me.
	public boolean isUnsigned() {
		return false;
	}

	public Integer getPrecision() {
		return this.precision;
	}

	public Integer getScale() {
		return this.scale;
	}

	public Integer getCharacterMaxLen() {
		return this.characterMaxLen;
	}

	public void setAfterField(String val) {
		this.afterField = val;
	}

	public String getAfterField()
	{
		return this.afterField;
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
		return f.getScale() == this.getScale() && f.getPrecision() == this.getPrecision() && f.getDefaultValue() == this.getDefaultValue() && f.getCharacterMaxLen() == this.getCharacterMaxLen() && f.getTypeName().equals(this.getTypeName());
	}
}
