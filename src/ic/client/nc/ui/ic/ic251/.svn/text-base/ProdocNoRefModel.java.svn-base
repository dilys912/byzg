package nc.ui.ic.ic251;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pd.pd2020.ClientUI;
public class ProdocNoRefModel  extends  AbstractRefModel  {
	public ProdocNoRefModel() {
		super();
	}
	public String[] getFieldCode() {
		return new String[]{"a.scddh","a.sjkssj"};
	}
	public String[] getFieldName()
	{
		return new String[]{"生产订单号","实际开工时间"};
	}
	public String[] getHiddenFieldCode() {
		return new String[]{"a.pk_moid"};
	}
	public String getPkFieldCode() {
		return "a.pk_moid";
	}
	public String getRefTitle() {
		return "生产订单号"/*@res "生产线"*/;
	}
	public String getTableName() {
		return "mm_mo a left outer join mm_mokz b on a.pk_moid = b.pk_moid";
	}
 //	public String getWherePart()
   
 	//{
 		//String ProLine= new String();
 	  //  if(!ProLine.equals(""))
 	//	{
 		//  return " mm_mo.pk_corp='" + getPk_corp() + "' and scxid ='"+ProLine+"'";
 	//	}
 	//	return " mm_mo.pk_corp='" + getPk_corp() + "'";
 	//}
 	public String getOrderPart()
 	{
 		return "a.sjkssj";
 	}
 	public String getRefCodeField()
 	{
 		return "a.scddh";
 	}
 	public String getRefNameField()
 	{
 		return "a.scddh";
 	}
}
