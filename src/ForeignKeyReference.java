

public class ForeignKeyReference {
	private String columnName = null;
	private String referencedTableName = null;
	private String referencedColumnName = null;

	public ForeignKeyReference(String columnName, String referencedTableName, String referencedColumnName)
	{
		this.columnName = columnName;
		this.referencedTableName = referencedTableName;
		this.referencedColumnName = referencedColumnName;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public String getReferencedTableName() {
		return this.referencedTableName;
	}

	public String getReferencedColumnName() {
		return this.referencedColumnName;
	}

	public String getExtra() {
		return "ON DELETE SOMETHING ON UPDATE SOMETHINGELSE"; //TODO: Find me
	}
}
