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
		ResultSet rs2 = null;
		PreparedStatement stmt2 = null;

		ResultSet rs1 = null;
		PreparedStatement stmt1 = null;

		try
		{
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPasswd);

			stmt1 = conn.prepareStatement("select * from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA=?");
			stmt1.setString(1, dbName);
			rs1 = stmt1.executeQuery();

			Table t = null;
			if (rs1.next())
			{
				t = Table.populateFromRS(rs1);
				this.tables.put(t.getName(), t);
			}			
			
			
			stmt2 = conn.prepareStatement("select * from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA=?");
			stmt2.setString(1, dbName);
			rs2 = stmt2.executeQuery();

			while (rs2.next()) {
				//String tblName = rs2.getString("TABLE_NAME");
				//Table t = tables.get(tblName);
				t.updateFromRS(rs2);
			}
		}
		catch (SQLException sqle)
		{
			throw sqle;
		}
		finally
        {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException sqlEx) { }
        
                rs1 = null;
            }

            if (stmt1 != null) {
                try {
                    stmt1.close();
                } catch (SQLException sqlEx) { }
        
                stmt1 = null;
			}
			
			if (rs2 != null) {
                try {
                    rs2.close();
                } catch (SQLException sqlEx) { }
        
                rs2 = null;
            }
        
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException sqlEx) { }
        
                stmt2 = null;
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
