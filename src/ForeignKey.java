import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class ForeignKey {
	
	private String name;
	private Map<Integer,ForeignKeyReference> columns;
	private ForeignKeyReference keyRef;

	public ForeignKey(String name, Map<Integer,ForeignKeyReference> columns, ForeignKeyReference keyRef)
	{
		this.name = name;
		this.keyRef = keyRef;
		this.columns = new TreeMap<>();
		if (columns != null)
			this.columns = columns;

	}

	public static ForeignKey populateFromRS(ResultSet rs) throws SQLException {
		ForeignKey fk = new ForeignKey(rs.getString("CONSTRAINT_NAME"),null,null);
		fk.addColumnFromRS(rs);
		return fk;
	}

	public void addColumnFromRS(ResultSet rs) throws SQLException {
		this.addColumn(rs.getInt("ORDINAL_POSITION"),rs.getString("COLUMN_NAME"), rs.getString("REFERENCED_TABLE_NAME"), rs.getInt("POSITION_IN_UNIQUE_CONSTRAINT"), rs.getString("REFERENCED_COLUMN_NAME"));
	}

	public void addColumn(Integer seq, String columnName, String referencedTableName, Integer refSeq, String referencedColumnName)
	{
		this.keyRef = new ForeignKeyReference(referencedTableName, null, null);
		this.keyRef.addColumn(columnName, seq, referencedColumnName, refSeq);
		this.columns.put(seq,this.keyRef);
	}

	public String getName()
	{
		return this.name;
	}

	public Map<Integer, ForeignKeyReference> getColumns()
	{
		return this.columns;
	}

	@Override
    public String toString() { 
        return String.format(" cols: %s", this.columns); 
    }

	public ForeignKeyReference getKeyReference() {
		return this.keyRef;
	} 
}
