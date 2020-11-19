import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

		if (!destTable.getCollation().equals(srcTable.getCollation()))
			td.setCollation(srcTable.getCollation());

		// Find the different fields (additions, removals and changes)
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
				td.addExtraFields(field); // Adding field: dropme at position: 15 // same position as bingobucks.. overlap.
		}

		// Find the different keys (additions, removals and changes)
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

		// Find the different foreign keys (additions, removals and changes)
		Map<String,ForeignKey> fkeys = srcTable.getForeignKeys();
		for (Map.Entry<String,ForeignKey> entry : fkeys.entrySet())
		{
			String keyName = entry.getKey();
			ForeignKey key = entry.getValue();
			if (destTable.hasForeignKey(keyName))
			{
				ForeignKey key2 = destTable.getForeignKey(keyName);
				// If the key exists, check that it's identical.
				if (!key.equals(key2))
					td.addDifferentForeignKey(key);
			}
			else
			{
				// the key doesn't exist in the dest, so we need to add it.
				td.addMissingForeignKey(key);
			}
		}

		for (Map.Entry<String, ForeignKey> entry : destTable.getForeignKeys().entrySet())
		{
			String keyName = entry.getKey();
			ForeignKey key = entry.getValue();
			if (!srcTable.hasForeignKey(keyName))
				td.addExtraForeignKey(key);
		}

		return td;
	}

	public static Collection<CompareStatement> compareDbs(Database sourceDB, Database destDB) 
	{
		Map<String, Table> sourceTables = sourceDB.getTables();
		Map<String, Table> destTables = destDB.getTables();

		// Find tables in the dest that don't exist in the source (so we should drop them)
		HashSet<String> tablesToDrop = new HashSet<>(sourceTables.keySet());
		tablesToDrop.addAll(destTables.keySet());
		tablesToDrop.removeAll(sourceTables.keySet());
		System.out.println("Tables to drop: " + tablesToDrop);

		// Find missing tables in the source that don't exist in the dest (so we should create them)
		HashSet<String> tablesToCreate = new HashSet<>(destTables.keySet());
		tablesToCreate.addAll(sourceTables.keySet());
		tablesToCreate.removeAll(destTables.keySet());
		System.out.println("Tables to create: " + tablesToCreate);

		HashSet<String> tablesRemaining = new HashSet<>(sourceTables.keySet());
		tablesRemaining.removeAll(tablesToDrop);
		tablesRemaining.removeAll(tablesToCreate);

		System.out.println("Tables remaining: " + tablesRemaining);
		
		ArrayList<CompareStatement> statements = new ArrayList<>();
		for (String tableName : tablesRemaining)
		{
			Table t1 = sourceDB.getTable(tableName);
			Table t2 = destDB.getTable(tableName);
			TableDiff tdiff = TableCompare.compare(t1,t2);
			CompareStatement cs = new CompareStatement(tdiff);
			statements.add(cs);
		}
		
		return statements;
	}
}
