import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class CompareStatement {
	private TableDiff tableDiff;

	public static final String OPERATION_DROP = "DROP";
	public static final String OPERATION_ADD = "ADD";
	public static final String OPERATION_CHANGE = "CHANGE";

	public CompareStatement(TableDiff tdiff) 
	{
		this.tableDiff = tdiff;
	}

	private void addAlterTableStatement(StringWriter out)
	{
		out.write("/* Alter table in destination */\n");
		out.write("ALTER TABLE `");
		out.write(this.tableDiff.getTableName());
		out.write("` \n");
	}

	private void addCreateTableStatement(StringWriter out)
	{
		out.write("/* Create table in destination */\n");
		out.write("CREATE TABLE `");
		out.write(this.tableDiff.getTableName());
		out.write("` \n");
	}

	public String getSQL() throws IOException
	{
		String string = "";
		try(StringWriter out = new StringWriter();)
		{
			out.write("SET FOREIGN_KEY_CHECKS=0;\n");

			if (this.tableDiff.getAction() == TableCompare.ACTION_CREATE)
				this.addCreateTableStatement(out);
			else
				this.addAlterTableStatement(out);			

			for (Map.Entry<Integer,DiffField> entry : this.tableDiff.getFields().entrySet())
			{
				DiffField field = entry.getValue();
				this.addColumn(out, field, field.getOperation());
				out.write(",\n");
			}

			for (Map.Entry<String,DiffField> entry : this.tableDiff.getDropFields().entrySet())
			{
				DiffField field = entry.getValue();
				this.addColumn(out, field, field.getOperation());
				out.write(",\n");
			}

			for (Map.Entry<String,DiffKey> entry : this.tableDiff.getKeys().entrySet())
			{
				DiffKey key = entry.getValue();

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


			for (Map.Entry<String,DiffForeignKey> entry : this.tableDiff.getForeignKeys().entrySet())
			{
				DiffForeignKey key = entry.getValue();

				if (!key.getName().equals("PRIMARY")) // Ignore PRIMARY keys.
				{
					this.addAlterTableStatement(out);
					this.addForeignKey(out, key, key.getOperation());
					out.write(";\n");
				}
			}

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

	private void addForeignKey(StringWriter out, ForeignKey fk, String operation)
	{
		String keyName = fk.getName();
		out.write("\t");

		/*
	     ADD CONSTRAINT `currency_ibfk_1`  
		     FOREIGN KEY (`fkey`) REFERENCES `currency_fkey` (`id`) ON DELETE CASCADE ON UPDATE CASCADE ;
		*/

		if (operation == CompareStatement.OPERATION_DROP || operation == CompareStatement.OPERATION_CHANGE)
		{
			out.write("DROP FOREIGN KEY `");
			out.write(keyName);
			out.write("` ");
		}

		if (operation == CompareStatement.OPERATION_CHANGE)
			out.write(",\n");

		if (operation == CompareStatement.OPERATION_ADD || operation == CompareStatement.OPERATION_CHANGE)
		{
			out.write("ADD CONSTRAINT `");
			out.write(keyName);
			out.write("` \n");
			
			//ForeignKeyReference fkr = key.getColumns().get(key)
			ForeignKeyReference fkr = fk.getKeyReference();

			out.write(" FOREIGN KEY (");

			out.write(fkr.getColumnsForSql());
			out.write(") " );
			out.write("REFERENCES `");
			out.write(fkr.getReferencedTableName());
			out.write("`");

			out.write("(");
			out.write(fkr.getReferencedColumnsForSql());
			out.write(") ");

			out.write(fkr.getExtra());
		}
	}
	

	private void _addColumn(StringWriter out, String columnName, String columnType, String fieldType, Integer precision, Integer scale, boolean isNullable, String defaultValue, String afterField, String collation, boolean unsigned, String extra, String operation)
	{
		out.write("\t");

		if (this.tableDiff.getAction() != TableCompare.ACTION_CREATE)
		{
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
