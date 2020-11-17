public class DiffField extends Field {

	private String typeOfChange;

	public DiffField(String name, Integer position, String columnType, String typeName, Integer precision,
			Integer scale, Integer characterMaxLen, boolean isNullable, String colDefault, String collation, String afterField) {
		super(name, position, columnType, typeName, precision, scale, characterMaxLen, isNullable, colDefault, collation, afterField);
	}

	public DiffField(Field field, String typeOfChange)
	{
		super(field.getName(), field.getPosition(), field.getColumnType(), field.getTypeName(), field.getPrecision(), field.getScale(), field.getCharacterMaxLen(), field.isNullable(), field.getDefaultValue(), field.getCollation(), field.getAfterField());
		this.typeOfChange = typeOfChange;
	}

	public void setTypeOfChange(String val)
	{
		this.typeOfChange = val;
	}
	
	public String getOperation()
	{
		return this.typeOfChange;
	}	
}
