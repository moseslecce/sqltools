import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class CompareStatement {
	private TableDiff tableDiff;

	public CompareStatement(TableDiff tdiff) 
	{
		this.tableDiff = tdiff;
	}

	public String getSQL() throws IOException
	{
		StringWriter output = new StringWriter();

		output.write("/* Alter table in destination */\n");
		output.write("ALTER TABLE `");
		output.write(this.tableDiff.getTableName());
		output.write("` \n");
		//this.addColumn(output, "my_field", "DECIMAL", "18,4", true, "0.0000","other_field","utf8_general_ci",true,"CHANGE");		

		for (Map.Entry<Integer,DiffField> entry : this.tableDiff.getFields().entrySet())
		{
			DiffField field = entry.getValue();
			this.addColumn(output, field, field.getOperation());
			output.write(",\n");
		}

		if (this.tableDiff.getAutoIncrement() != null)
		{
			output.write("AUTO_INCREMENT=");
			output.write(String.valueOf(this.tableDiff.getAutoIncrement()));
			output.write(" ");
		}

		if (this.tableDiff.getCollation() != null)
		{
			output.write("DEFAULT CHARSET='");
			output.write("utf8"); //TODO: Figure out where this should come from.
			output.write("', COLLATE ='");
			output.write(this.tableDiff.getCollation());
			output.write("' ");
		}

		if (this.tableDiff.getEngine() != null)
		{
			output.write("ENGINE='");			
			output.write(this.tableDiff.getEngine());
			output.write("' ");
		}

		output.write(";");

		String string = output.toString();
		output.close();
		return string;
	}

	private void addColumn(StringWriter output, Field field, String operation)
	{
		this._addColumn(output, field.getName(), field.getColumnType(), field.getTypeName(), field.getPrecision(), field.getScale(), field.isNullable(), field.getDefaultValue(), field.getAfterField(), field.getCollation(), field.isUnsigned(), operation);
	}

	private void _addColumn(StringWriter output, String columnName, String columnType, String fieldType, Integer precision, Integer scale, boolean isNullable, String defaultValue, String afterField, String collation, boolean unsigned, String operation)
	{
		output.write("\t");
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
		if (operation == "CHANGE")
		{
			output.write("`");
			output.write(columnName);
			output.write("` ");
		}

		/*
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
		}*/

		output.write(columnType);
		output.write(" ");

		if (unsigned)
			output.write("UNSIGNED ");

		if (collation != null)
		{
			output.write("COLLATE ");
			output.write(collation);
			output.write(" ");
		}

		if (isNullable)
			output.write("NULL ");
		else
			output.write("NOT NULL ");
		
		if (defaultValue != null)
		{
			output.write("DEFAULT ");
			if (!defaultValue.equals("NULL"))
				output.write("'");
			
			output.write(defaultValue);

			if (!defaultValue.equals("NULL"))
				output.write("'");

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
