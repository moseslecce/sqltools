import java.util.Map;

public class TableCompare {
	public static TableDiff compare(Table srcTable, Table destTable)
	{
		// iterate through the fields in t1 and see if each one matches (or exists) in t2.
		TableDiff td = new TableDiff(srcTable.getName());

		Map<String, Field> fields = srcTable.getFields();

		for (Map.Entry<String, Field> entry : fields.entrySet())
		{
			String key = entry.getKey();
			Field field = entry.getValue();

			if (destTable.hasField(key))
			{
				// If the field exists, check that it's identical.
				Field field2 = destTable.getField(key);

				if (!field.equals(field2))
					td.addDifferentField(field);
			}
			else
			{
				// the field doesn't exist in the dest, so we need to add it.
				td.addMissingField(field);
			}
		}

		for (Map.Entry<String, Field> entry : destTable.getFields().entrySet())
		{
			String key = entry.getKey();
			Field field = entry.getValue();
			if (!srcTable.hasField(key))
				td.addExtraFields(field);
		}

		return td;
	}
}
