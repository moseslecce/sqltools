import java.util.Map;
import java.util.TreeMap;

public class ForeignKeyReference {
	private String referencedTableName = null;
	private Map<Integer,String> columns;
	private Map<Integer,String> refColumns;
	
	public ForeignKeyReference(String referencedTableName,  Map<Integer,String> columns,  Map<Integer,String> refColumns)
	{
		this.columns = new TreeMap<>();
		if (columns != null)
			this.columns = columns;

		this.refColumns = new TreeMap<>();
		if (refColumns != null)
			this.refColumns = refColumns;
	
		this.referencedTableName = referencedTableName;
	}

	public void addColumn(String columnName, Integer seq, String referencedColumnName, Integer refSeq)
	{
		this.columns.put(seq, columnName);
		this.refColumns.put(refSeq, referencedColumnName);
	}

	public String getColumnsForSql() {
		String res = "";
		for (String field : this.columns.values())
			res = res.concat(String.format("`%s`,",field));

		return StringUtil.removeLastCharacter(res);
	}

	public String getReferencedColumnsForSql() {
		String res = "";
		for (String field : this.refColumns.values())
			res = res.concat(String.format("`%s`,",field));

		return StringUtil.removeLastCharacter(res);
	}

	public String getReferencedTableName() {
		return this.referencedTableName;
	}

	public String getExtra() {
		return "ON DELETE SOMETHING ON UPDATE SOMETHINGELSE"; //TODO: Find me
	}
}
