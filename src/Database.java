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
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;

		try
		{
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPasswd);
			//Statement stmt = conn.createStatement();
			stmt = conn.prepareStatement("select * from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA=?");
			stmt.setString(1, dbName);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String tblName = rs.getString("TABLE_NAME");				
				if (this.tables.get(tblName) == null)
				{
					this.tables.put(tblName, Table.populateFromRS(rs));
				}
				else
				{
					Table t = tables.get(tblName);
					t.updateFromRS(rs);
				}
			}
		}
		catch (SQLException sqle)
		{
			throw sqle;
		}
		finally
        {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { }
        
                rs = null;
            }
        
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { }
        
                stmt = null;
            }
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
