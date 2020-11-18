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
