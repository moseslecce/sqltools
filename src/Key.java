import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class Key {
	private String name;
	private boolean nonUnique;
	private Map<Integer,String> columns;
	private String indexType;

	public Key(String name, boolean nonUnique, String indexType, Map<Integer,String> columns)
	{
		this.columns = new TreeMap<>();
		if (columns != null)
			this.columns = columns;

		this.name = name;
		this.nonUnique = nonUnique;
		this.indexType = indexType;
	}

	public static Key populateFromRS(ResultSet rs) throws SQLException {
		Key k = new Key(rs.getString("INDEX_NAME"), rs.getBoolean("NON_UNIQUE"), rs.getString("INDEX_TYPE"), null);
		k.addColumnFromRS(rs);
		return k;
	}

	public String getName()
	{
		return this.name;		
	}

	public String getIndexType()
	{
		return this.indexType;
	}

	public void addColumn(Integer seq, String val)
	{
		this.columns.put(seq,val);
	}

	public Map<Integer, String> getColumns()
	{
		return this.columns;
	}

	public boolean isNonUnique()
	{
		return this.nonUnique;
	}

	public String getFieldSQL() {
		String res = "";
		for (String field : this.columns.values())
			res = res.concat(String.format("`%s`,",field));

		return StringUtil.removeLastCharacter(res);
	}

	public void addColumnFromRS(ResultSet rs) throws SQLException {
		this.addColumn(rs.getInt("SEQ_IN_INDEX"),rs.getString("COLUMN_NAME"));
	}

	@Override
    public String toString() { 
        return String.format(" cols: %s", this.columns); 
    } 
}
