import java.sql.SQLException;

public class App2 {
    static final String DB_URL = "jdbc:mysql://newlab/aberrant?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_DRV = "com.mysql.cj.jdbc.Driver";
    static final String DB_USER = "moses";
    static final String DB_PASSWD = "r3m0teac3zzz";
    static final String DB_NAME = "aberrant";

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Comparison..");

        try {
            Database db1 = new Database(DB_URL, DB_USER, DB_PASSWD, "synctest1");
            Database db2 = new Database(DB_URL, DB_USER, DB_PASSWD, "synctest2");

            Table t1 = db1.getTable("currency");
            Table t2 = db2.getTable("currency");

            TableDiff tdiff = TableCompare.compare(t1,t2);
            CompareStatement cs = new CompareStatement(tdiff);
            System.out.println(cs.getSQL());

        } catch (SQLException ex) {
            ex.printStackTrace();
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }        
    }
}
