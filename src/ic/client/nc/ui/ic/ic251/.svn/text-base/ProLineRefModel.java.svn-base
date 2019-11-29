package nc.ui.ic.ic251;
import nc.ui.bd.ref.AbstractRefModel;

public class ProLineRefModel extends  AbstractRefModel  
{
	
	public ProLineRefModel() {
		super();
	}

	public String[] getFieldCode() {
		return new String[]{"gzzxbm","gzzxmc"};
	}
	public String[] getFieldName()
	{
		return new String[]{"工作中心编码","工作中心名称"};
	}
	public String[] getHiddenFieldCode() {
		return new String[]{"pk_wkid"};
	}
	public String getPkFieldCode() {
		return "pk_wkid";
	}
	public String getRefTitle() {
		return "生产线"/*@res "生产线"*/;
	}
	public String getTableName() {
		return "pd_wk";
	}
 	public String getWherePart()
   
 	{
 		return " pd_wk.pk_corp='" + getPk_corp() + "'";
 	
 	}
   
}
