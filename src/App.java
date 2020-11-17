import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class App {
    static final String DB_URL = "jdbc:mysql://newlab/aberrant?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_DRV = "com.mysql.cj.jdbc.Driver";
    static final String DB_USER = "moses";
    static final String DB_PASSWD = "r3m0teac3zzz";

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;


        /*
        // SSH Tunnel
        JSch jsch = new JSch();
        //jsch.addIdentity("~/.ssh/id_rsa");
        Session session = jsch.getSession(jumpserverUsername, jumpserverHost);
        session.connect();

        // Forward randomly chosen local port through the SSH channel to database host/port
        int forwardedPort = session.setPortForwardingL(0, databaseHost, databasePort);

        String url = "jdbc:mysql://localhost:" + forwardedPort;
        */

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);

            // Do something with the Connection
            stmt = conn.createStatement();

            // DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();
            // rs = meta.getTables(null, null, null, new String[] {"TABLE"});

            /*
            Database db = Database.populateFromMetaData((DatabaseMetaData) conn.getMetaData());
            System.out.println("Tables in the database:");
            for (String str : db.getTables())
                System.out.println(str);
            */

            /*
             * int count = 0; System.out.println("All table names are in test database:");
             * while (rs.next()) { String tblName = rs.getString("TABLE_NAME");
             * System.out.println(tblName); count++; } System.out.println(count +
             * " Rows in set ");
             */

            /*
             * rs = stmt.executeQuery("show create table affiliate_sites"); if(rs.next()) {
             * System.out.println("Table Name: "+rs.getString(1));
             * System.out.println("SQL Code: "+rs.getString(2)); }
             */

            rs = stmt.executeQuery("select * from players limit 1;");
            ResultSetMetaData tableMd = rs.getMetaData();
            Table t = Table.populateFromMetaData(tableMd);
            System.out.println("Table Name : " + t.getName());
            // System.out.println("Table Name : " + tableMd.getTableName(2));
            System.out.println("Field \tsize\tDataType");
            /*
             * for (int i = 0; i < t.getNumFields(); i++) {
             * System.out.print(tableMd.getColumnName(i + 1) + " \t");
             * System.out.print(tableMd.getColumnDisplaySize(i + 1) + "\t");
             * System.out.println(tableMd.getColumnTypeName(i + 1)); }
             */

            Map<String, Field> fields = t.getFields();
            for (Map.Entry<String, Field> entry : fields.entrySet()) {
                String key = entry.getKey();
                Field field = entry.getValue();
                System.out.print(key + " \t");
                //System.out.print(field.getDisplaySize() + "\t");
                System.out.print(field.getPrecision() + "\t");
                System.out.print(field.getScale() + "\t");
                System.out.println(field.getTypeName());
            }

            Table t2 = Table.populateFromName("whitelist", conn);

            TableDiff tdiff = TableCompare.compare(t,t2);

            CompareStatement cs = new CompareStatement(tdiff);
            System.out.println(cs.getSQL());

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally
        {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
        
                rs = null;
            }
        
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore
        
                stmt = null;
            }
        }
    }
}
