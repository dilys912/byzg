package nc.bs.scm.sourcebill;

/**
 *
 
	 采购结算单联查

 
 * 作者：方益
 * @version	最后修改日期(2003-04-15 10:42:58)
 * @see		需要参见的其它类
 * @since		从产品的那一个版本，此类被添加进来。（可选）
 * 修改人 + 修改日期
 * 修改说明
 * 
 * zhwj
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import nc.vo.scm.sourcebill.LightBillVO;

public class SettleBillSourceDMO extends DefaultSourceDMO {
/**
 * SettleBillSourceDMO 构造子注解。
 * @exception javax.naming.NamingException 异常说明。
 * @exception nc.bs.pub.SystemException 异常说明。
 */
public SettleBillSourceDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super();
}
/**
 * SettleBillSourceDMO 构造子注解。
 * @param dbName java.lang.String
 * @exception javax.naming.NamingException 异常说明。
 * @exception nc.bs.pub.SystemException 异常说明。
 */
public SettleBillSourceDMO(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super(dbName);
}
/**
 * 此处插入方法说明。
 * 功能描述:
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:
 * @return java.lang.String
 * @param billType java.lang.String
 */
private String createSQL(String billType) {

	if(!"27".equals(billType))
		return null;
	return
	  "SELECT DISTINCT " +
	  "po_settlebill_b.cinvoiceid, po_settlebill_b.cstockid, ic_general_h.cbilltypecode " +
	  "FROM po_settlebill INNER JOIN "+
	  "po_settlebill_b ON "+
	  "po_settlebill.csettlebillid = po_settlebill_b.csettlebillid LEFT OUTER JOIN "+
	  "ic_general_h ON po_settlebill_b.cstockid = ic_general_h.cgeneralhid "+
	  "WHERE (po_settlebill.csettlebillid = ? and po_settlebill.dr = 0 and  po_settlebill_b.dr = 0)";
	
}

private String createSQLFW(String billType) {

	if(!"27".equals(billType))
		return null;
	return
	  "SELECT DISTINCT ia_bill_b.cbillid, ia_bill_b.cbilltypecode, ia_bill_b.vbillcode FROM ia_bill_b WHERE ia_bill_b.dr = 0 and ia_bill_b.csourcebillid = ?";
	
}
/**
 * 此处插入方法说明。
 * 功能描述: 结算单没有后续单据
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:
 * @return nc.vo.scm.sourcebill.LightBillVO[]
 * @param curBillType java.lang.String
 * @param curBillID java.lang.String
 */
public LightBillVO[] getForwardBills(String curBillType, String curBillID, String forwardBillType) {
	//zhwj
	if("D1".equals(forwardBillType)) {
		return super.getForwardBills(curBillType,curBillID,forwardBillType);
		
	}
	
	String sql = createSQLFW(curBillType);
	if(sql == null)
		return null;
		
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	Vector  v= new Vector();
	try {
		con = getConnection();
		stmt = con.prepareStatement(sql);
		stmt.setString(1,curBillID);		

		rs = stmt.executeQuery();

		while(rs.next()) {

			String storeID = rs.getString(1);
			String storeType = rs.getString(2);
			String code = rs.getString(3);
			
			if(storeID != null ) {
				LightBillVO svo = new LightBillVO();
				svo.setType(storeType);
				svo.setID(storeID);
				svo.setCode(code);
				v.add(svo);
			}
			
		}
	}
	catch (Exception e) {
		e.printStackTrace();
		/**<needn't:cannot get source bill >*/
	}
	finally {
		try {
			if (stmt != null) {
				stmt.close();
			}
		}
		catch (Exception e) {
		}
		try {
			if (con != null) {
				con.close();
			}
		}
		catch (Exception e) {
		}
	}
	LightBillVO[] vos = null;
	if(v.size()>0) {
		vos = new LightBillVO[v.size()];
		v.copyInto(vos);
		for(int i=0; i<vos.length;i++) {
			vos[i].setCode(getBillCode(vos[i].getType(),vos[i].getID()));
		}
	}
	
	return vos;
}
/**
 * 此处插入方法说明。
 * 功能描述:
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:
 * @return nc.vo.scm.sourcebill.LightBillVO[]
 * @param curBillType java.lang.String
 * @param curBillID java.lang.String
 */
public LightBillVO[] getSourceBills(String curBillType, String curBillID) {
	
	/*************************************************************/
	// 保留的系统管理接口：
	beforeCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getSourceBills", new Object[] {curBillType,curBillID});
	/*************************************************************/
	String sql = createSQL(curBillType);
	if(sql == null)
		return null;
		
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	Vector  v= new Vector();
	try {
		con = getConnection();
		stmt = con.prepareStatement(sql);
		stmt.setString(1,curBillID);		

		rs = stmt.executeQuery();

		while(rs.next()) {

			String invoiceID = rs.getString(1);
			String storeID = rs.getString(2);
			String storeType = rs.getString(3);
			
			if(invoiceID != null ) {
				LightBillVO svo = new LightBillVO();
				svo.setType("25");
				svo.setID(invoiceID);
				v.add(svo);
			}
			if(storeID != null ) {
				LightBillVO svo = new LightBillVO();
				svo.setType(storeType);
				svo.setID(storeID);
				v.add(svo);
			}
			
		}
	}
	catch (Exception e) {
		e.printStackTrace();
		/**<needn't:cannot get source bill >*/
	}
	finally {
		try {
			if (stmt != null) {
				stmt.close();
			}
		}
		catch (Exception e) {
		}
		try {
			if (con != null) {
				con.close();
			}
		}
		catch (Exception e) {
		}
	}
	LightBillVO[] vos = null;
	if(v.size()>0) {
		vos = new LightBillVO[v.size()];
		v.copyInto(vos);
		for(int i=0; i<vos.length;i++) {
			vos[i].setCode(getBillCode(vos[i].getType(),vos[i].getID()));
		}
	}
	
	/*************************************************************/
	// 保留的系统管理接口：
	afterCallMethod("nc.bs.scm.sourcebill.SourceBillDMO", "getSourceBills", new Object[] {curBillType,curBillID});
	/*************************************************************/

	return vos;
}
}