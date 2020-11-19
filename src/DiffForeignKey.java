import java.util.Map;

public class DiffForeignKey extends ForeignKey {

	private String typeOfChange;

	public DiffForeignKey(String name, Map<Integer, ForeignKeyReference> columns, ForeignKeyReference keyRef) {
		super(name, columns, keyRef);		
	}

	public DiffForeignKey(ForeignKey fk, String typeOfChange)
	{
		super(fk.getName(), fk.getColumns(), fk.getKeyReference());
		this.typeOfChange = typeOfChange;
	}
	
	public void setTypeOfChange(String val)
	{
		this.typeOfChange = val;
	}
	
	public String getOperation()
	{
		return this.typeOfChange;
	}
}
