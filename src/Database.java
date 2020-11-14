import com.mysql.cj.jdbc.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {
	private ArrayList<String> tables;
	public Database(ArrayList<String> tables)
	{
		this.tables = tables;
	}

	public static Database populateFromMetaData(DatabaseMetaData metaData) throws SQLException {
		ResultSet rs = metaData.getTables(null, null, null, new String[] {"TABLE"});

		ArrayList<String> tables = new ArrayList<String>();
		while (rs.next()) {
			String tblName = rs.getString("TABLE_NAME");
			tables.add(tblName);
		}
		
		return new Database(tables);
	}

	public ArrayList<String> getTables()
	{
		return this.tables;
	}
}
