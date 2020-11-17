import java.io.IOException;
import java.io.StringWriter;

public class CompareStatement {
	private TableDiff tableDiff;

	public CompareStatement(TableDiff tdiff) 
	{
		this.tableDiff = tdiff;
	}

	public String getSQL() throws IOException
	{
		StringWriter output = new StringWriter();
		output.write("ALTER TABLE `");
		output.write(this.tableDiff.getTableName());
		output.write("` \n");
		//this.addColumn(output, "my_field", "DECIMAL", "18,4", true, "0.0000","other_field","utf8_general_ci",true,"CHANGE");		

		for(Field missing : this.tableDiff.getMissingFields())
		{
			//this.addColumn(output, "field2", "INT", "11", true, "123","my_field",null,true,"ADD");
			this.addColumn(output, missing, "ADD");
			output.write(",\n");
		}

		for (Field diff : this.tableDiff.getDifferentFields())
		{
			this.addColumn(output, diff, "CHANGE");
			output.write(",\n");
		}
		
		for (Field extra : this.tableDiff.getExtraFields())
		{
			this.addColumn(output, extra, "DROP");
			output.write(",\n");			
		}

		output.write(";");

		String string = output.toString();
		output.close();
		return string;
	}

	private void addColumn(StringWriter output, Field field, String operation)
	{
		String afterField = null;
		String collation = null;

		this._addColumn(output, field.getName(), field.getTypeName(), field.getPrecision(), field.getScale(), field.isNullable(), field.getDefaultValue(), afterField, collation, field.isUnsigned(), operation);
	}

	private void _addColumn(StringWriter output, String columnName, String fieldType, Integer precision, Integer scale, boolean isNullable, String defaultValue, String afterField, String collation, boolean unsigned, String operation)
	{	
		if (operation == "CHANGE")
		{
			output.write("CHANGE `");
		}
		else if (operation == "ADD")
		{
			output.write("ADD COLUMN `");
		}
		else if (operation == "DROP")
		{
			output.write("DROP COLUMN `");
			output.write(columnName);
			output.write("` ");
			return;
		}

		output.write(columnName);
		output.write("` ");
		output.write("`");
		output.write(columnName);
		output.write("` ");
		output.write(fieldType);
		output.write(" ");

		if (precision != null)
		{
			output.write("(");
			output.write(String.valueOf(precision));
			if (scale != null)
			{
				output.write(",");
				output.write(String.valueOf(scale));
			}

			output.write(") ");
		}

		if (unsigned)
			output.write("UNSIGNED ");

		if (collation != null)
		{
			output.write("COLLATE ");
			output.write(collation);
			output.write(" ");
		}

		if (!isNullable)
			output.write("NOT NULL ");
		
		if (defaultValue != null)
		{
			output.write("DEFAULT ");
			output.write(defaultValue);
			output.write(" ");
		}

		if (afterField != null)
		{
			output.write("AFTER ");
			output.write("`");
			output.write(afterField);
			output.write("` ");
		}
	}	
}
