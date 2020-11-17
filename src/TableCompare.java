import java.util.Map;

public class TableCompare {
	public static TableDiff compare(Table srcTable, Table destTable)
	{
		// iterate through the fields in t1 and see if each one matches (or exists) in t2.
		TableDiff td = new TableDiff(srcTable.getName());

		if (td.getAutoIncrement() != srcTable.getAutoIncrement())
			td.setAutoIncrement(srcTable.getAutoIncrement());

		if (!destTable.getEngine().equals(srcTable.getEngine()))
			destTable.setEngine(srcTable.getEngine());

		System.out.println("Src collation: " + srcTable.getCollation());
		System.out.println("Dest collation: " + destTable.getCollation());

		if (!destTable.getCollation().equals(srcTable.getCollation()))
			td.setCollation(srcTable.getCollation());

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


		Map<String,Key> keys = srcTable.getKeys();
		for (Map.Entry<String,Key> entry : keys.entrySet())
		{
			String keyName = entry.getKey();
			Key key = entry.getValue();
			if (destTable.hasKey(keyName))
			{
				Key key2 = destTable.getKey(keyName);
				// If the key exists, check that it's identical.
				if (!key.equals(key2))
					td.addDifferentKey(key);
			}
			else
			{
				// the key doesn't exist in the dest, so we need to add it.
				td.addMissingKey(key);
			}
		}

		for (Map.Entry<String, Key> entry : destTable.getKeys().entrySet())
		{
			String keyName = entry.getKey();
			Key key = entry.getValue();
			if (!srcTable.hasKey(keyName))
				td.addExtraKey(key);
		}

		return td;
	}
}
