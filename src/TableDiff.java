import java.util.ArrayList;

public class TableDiff {

	private ArrayList<Field> missingFields;
	private String tableName;
	private ArrayList<Field> differentFields;

	public TableDiff(String tableName)
	{
		this.missingFields = new ArrayList<>();
		this.differentFields = new ArrayList<>();
		this.tableName = tableName;
	}

	public void addMissingField(Field field) 
	{
		this.missingFields.add(field);
	}

	public void addDifferentField(Field field)
	{
		this.differentFields.add(field);
	}

	public ArrayList<Field> getMissingFields()
	{
		return this.missingFields;
	}

	public String getTableName() {
		return this.tableName;
	}

	public ArrayList<Field> getDifferentFields() 
	{
		return this.differentFields;
	}
}
