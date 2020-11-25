import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Database 
{
	private Map<String,Table> tables; // hashmap of tablename/tableobj.
	public Database(Map<String,Table> tables)
	{
		this.tables = tables;
	}

	public Database(String dbUrl, String dbUser, String dbPasswd, String dbName) throws SQLException {

		this.tables = new HashMap<>();
		try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPasswd);)
		{
			try(PreparedStatement stmt1 = conn.prepareStatement("select * from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA=?");)
			{
				stmt1.setString(1, dbName);
				try (ResultSet rs1 = stmt1.executeQuery();)
				{
					while (rs1.next())
					{
						Table t = Table.populateFromRS(rs1);
						this.tables.put(t.getName(), t);
					}
				}
			}

			try(PreparedStatement stmt2 = conn.prepareStatement("select * from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA=?");)
			{
				stmt2.setString(1, dbName);
				try (ResultSet rs2 = stmt2.executeQuery();)
				{
					Table t = null;
					while (rs2.next()) {
						String tblName = rs2.getString("TABLE_NAME");
						t = tables.get(tblName);
						t.addFieldFromRS(rs2);						
					}
				}
			}

			for(Table t : this.tables.values())
				t.populatePositions();

			try (PreparedStatement stmt = conn.prepareStatement("select * from INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA=?");)
			{				
				stmt.setString(1, dbName);
				try (ResultSet rs = stmt.executeQuery();)
				{
					while (rs.next())
					{
						String tblName = rs.getString("TABLE_NAME");
						Table t = tables.get(tblName);
						t.populateIndexesFromRS(rs);
					}
				}
			}


			//try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'FOREIGN KEY' and CONSTRAINT_SCHEMA=?");)
			//try (PreparedStatement stmt = conn.prepareStatement("select * from information_schema.key_column_usage where referenced_table_name is not null and table_schema = ?");)		
			String fkSql = "SELECT tb1.ORDINAL_POSITION, tb1.POSITION_IN_UNIQUE_CONSTRAINT, tb1.CONSTRAINT_NAME, tb1.TABLE_NAME, tb1.COLUMN_NAME, tb1.REFERENCED_TABLE_NAME, tb1.REFERENCED_COLUMN_NAME, tb2.MATCH_OPTION, tb2.UPDATE_RULE, tb2.DELETE_RULE FROM information_schema.`KEY_COLUMN_USAGE` AS tb1 INNER JOIN information_schema.REFERENTIAL_CONSTRAINTS AS tb2 ON (tb1.CONSTRAINT_NAME = tb2.CONSTRAINT_NAME  and tb1.table_schema=tb2.CONSTRAINT_SCHEMA) WHERE table_schema = ? AND referenced_column_name IS NOT NULL"; // TODO: fix me as the UPDATE_RULE and DELETE_RULE match may not be correct.

			try (PreparedStatement stmt = conn.prepareStatement(fkSql);)
			{
				stmt.setString(1, dbName);
				try (ResultSet rs = stmt.executeQuery();)
				{
					while (rs.next())
					{
						String tblName = rs.getString("TABLE_NAME");
						Table t = tables.get(tblName);
						t.populateForeignKeysFromRS(rs);
						System.out.println("fks: " + t.getForeignKeys());
					}
				}
			}

		}
		catch (SQLException sqle)
		{
			throw sqle;
		}
	}

	/*
	public static Database populateFromMetaData(Connection conn, DatabaseMetaData metaData) throws SQLException {
		ResultSet rs = metaData.getTables(null, null, null, new String[] {"TABLE"});

		Map<String,Table> tables = new HashMap<>();
		while (rs.next()) {
			String tblName = rs.getString("TABLE_NAME");
			Statement stmt = conn.createStatement();
			ResultSet rs2 = stmt.executeQuery("select * from players limit 1;");
            ResultSetMetaData tableMd = rs2.getMetaData();

			tables.put(tblName, Table.populateFromMetaData(tableMd));
		}
		
		return new Database(tables);
	}*/

	public Map<String,Table> getTables()
	{
		return this.tables;
	}

	public Table getTable(String tableName) 
	{
		return this.tables.get(tableName);
	}
}
