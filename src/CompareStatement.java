import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class CompareStatement {
	private TableDiff tableDiff;

	private static final String OPERATION_DROP = "DROP";
	private static final String OPERATION_ADD = "ADD";
	private static final String OPERATION_CHANGE = "CHANGE";

	public CompareStatement(TableDiff tdiff) 
	{
		this.tableDiff = tdiff;
	}

	public String getSQL() throws IOException
	{
		String string = "";
		try(StringWriter out = new StringWriter();)
		{
			out.write("SET FOREIGN_KEY_CHECKS=0;\n");

			out.write("/* Alter table in destination */\n");
			out.write("ALTER TABLE `");
			out.write(this.tableDiff.getTableName());
			out.write("` \n");

			for (Map.Entry<Integer,DiffField> entry : this.tableDiff.getFields().entrySet())
			{
				DiffField field = entry.getValue();
				this.addColumn(out, field, field.getOperation());
				out.write(",\n");
			}

			for (Map.Entry<String,DiffKey> entry : this.tableDiff.getKeys().entrySet())
			{
				DiffKey key= entry.getValue();

				if (!key.getName().equals("PRIMARY")) // Ignore PRIMARY keys.
				{
					this.addKey(out, key, key.getOperation());
					out.write(",\n");
				}
			}

			if (this.tableDiff.getAutoIncrement() != null)
			{
				out.write("AUTO_INCREMENT=");
				out.write(String.valueOf(this.tableDiff.getAutoIncrement()));
				out.write(" ");
			}

			if (this.tableDiff.getCollation() != null)
			{
				out.write("DEFAULT CHARSET='");
				out.write("utf8"); //TODO: Figure out where this should come from.
				out.write("', COLLATE ='");
				out.write(this.tableDiff.getCollation());
				out.write("' ");
			}

			if (this.tableDiff.getEngine() != null)
			{
				out.write("ENGINE='");			
				out.write(this.tableDiff.getEngine());
				out.write("' ");
			}

			out.write(";\n");

			out.write("SET FOREIGN_KEY_CHECKS=1;");

			string = out.toString();
		}

		return string;
	}

	private void addColumn(StringWriter output, Field field, String operation)
	{
		this._addColumn(output, field.getName(), field.getColumnType(), field.getTypeName(), field.getPrecision(), field.getScale(), field.isNullable(), field.getDefaultValueFormatted(), field.getAfterField(), field.getCollation(), field.isUnsigned(), field.getExtra(), operation);
	}

	private void addKey(StringWriter out, Key key, String operation)
	{
		String keyName = key.getName();
		out.write("\t");
		if (operation == CompareStatement.OPERATION_CHANGE)
		{
			out.write("CHANGE `");
		}
		else if (operation == CompareStatement.OPERATION_ADD)
		{
			out.write("ADD KEY `");
		}
		else if (operation == CompareStatement.OPERATION_DROP)
		{
			out.write("DROP KEY `");
			out.write(keyName);
			out.write("` ");
			return;
		}

		out.write(keyName);
		out.write("` ");
		
		out.write("(");
		out.write(key.getFieldSQL());
		out.write(") ");
	}

	private void _addColumn(StringWriter out, String columnName, String columnType, String fieldType, Integer precision, Integer scale, boolean isNullable, String defaultValue, String afterField, String collation, boolean unsigned, String extra, String operation)
	{
		out.write("\t");
		if (operation == "CHANGE")
		{
			out.write("CHANGE `");
		}
		else if (operation == "ADD")
		{
			out.write("ADD COLUMN `");
		}
		else if (operation == "DROP")
		{
			out.write("DROP COLUMN `");
			out.write(columnName);
			out.write("` ");
			return;
		}

		out.write(columnName);
		out.write("` ");
		if (operation == "CHANGE")
		{
			out.write("`");
			out.write(columnName);
			out.write("` ");
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

		out.write(columnType);
		out.write(" ");

		if (unsigned)
			out.write("UNSIGNED ");

		if (collation != null)
		{
			out.write("COLLATE ");
			out.write(collation);
			out.write(" ");
		}

		if (isNullable)
			out.write("NULL ");
		else
			out.write("NOT NULL ");
		
		if (defaultValue != null)
		{
			out.write("DEFAULT ");
			out.write(defaultValue);
			out.write(" ");
		}

		if (extra != null)
		{
			out.write(extra);
			out.write(" ");
		}

		if (afterField != null)
		{
			out.write("AFTER ");
			out.write("`");
			out.write(afterField);
			out.write("` ");
		}
	}
}
