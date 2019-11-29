/**
 * 
 */
package nc.ui.bgzg.efpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * @author zhangwj
 *
 */
public class SCDDRefModel extends AbstractRefModel {

	

	
	ClientEnvironment ce=ClientEnvironment.getInstance();
	/**
	 * 
	 */
	public SCDDRefModel() {
		// TODO Auto-generated constructor stub
	}
	
	public SCDDRefModel(String refNodeName){
		setRefNodeName(refNodeName);
	}
	public String[] getFieldCode() {
		// TODO 自动生成方法存根
		return new String[]{
				"code","name","memo","pk_xcys"
        };
	}

	/* （非 Javadoc）
	 * @see nc.ui.bd.ref.IRefModel#getFieldName()
	 */
	public String[] getFieldName() {
		// TODO 自动生成方法存根
		return new String[]{
				"编码","名称","备注","pk"
			};
	}

	/* （非 Javadoc）
	 * @see nc.ui.bd.ref.IRefModel#getRefTitle()
	 */
	public String getRefTitle() {
		// TODO 自动生成方法存根
		return "下厂验收";
	}

	/* （非 Javadoc）
	 * @see nc.ui.bd.ref.IRefModel#getTableName()
	 */
	public String getTableName() {
		// TODO 自动生成方法存根
		return "shht_xcys";
	}

	/* （非 Javadoc）
	 * @see nc.ui.bd.ref.IRefModel#getPkFieldCode()
	 */
	public String getPkFieldCode() {
		// TODO 自动生成方法存根i
		return "pk_xcys";
	}
	
	 public String getWherePart() {
//		return " nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0 and a.cinventoryid='"+ce.getCorporation().getPrimaryKey()+"' and a.vbatchcode='' and a.countbillheadid='' and a.countbillbodyid='' and and a.cbilltypecode='4D'";
		   return " pk_corp='"+ce.getCorporation().getPrimaryKey()+"' and nvl(dr,0)=0 ";
	 }
	 @Override
	public String getRefCodeField() {
		// TODO 自动生成方法存根
		return "code";
	}
	 @Override
	public String getRefNameField() {
		// TODO 自动生成方法存根
		return "name";
	}
}
