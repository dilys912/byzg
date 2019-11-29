package nc.ui.ic.pub.pf;

import java.util.Calendar;
import java.util.Date;

import nc.ui.ic.pub.bill.query.ICMultiCorpQryClient;
import nc.vo.ic.ic700.Bill53Const;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.pub.lang.UFDate;

/**
 * 销售参照查询类。
 * 创建日期：(2002-11-20 19:41:34)
 * @author：马万钧
 */
public class ICBillQuery extends ICMultiCorpQryClient implements nc.ui.pub.pf.IinitQueryData{

public ICBillQuery() {
	super();
}

public ICBillQuery(java.awt.Container parent) {
	super(parent);
}

//public ICBillQuery(java.awt.Container parent, String title) {
//	super(parent, title);
//}

//public ICBillQuery(java.awt.Frame parent) {
//	super(parent);
//}
/**
 * SaleOrderQueryCondition 构造子注解。
 * @param parent java.awt.Frame
 * @param title java.lang.String
 */
//public ICBillQuery(java.awt.Frame parent, String title) {
//	super(parent, title);
//}
/**
 * SaleOrderQueryCondition 构造子注解。
 * @param isFixedSet boolean
 */
//public ICBillQuery(boolean isFixedSet) {
//	super(isFixedSet);
//}
/**
 * 获得节点号。
 * 创建日期：(2001-11-27 13:51:07)
 * @return java.lang.String
 */
public String getNodeCode(String strCurrentBillType,String strSourceBillType){
	String nodecode = null;
	//if (strCurrentBillType.equals(nc.ui.so.pub.SaleBillType.SaleReceipt))
		//nodecode = "";
	if (strSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_purchaseIn)){
		if(strCurrentBillType.equals("4C"))
			nodecode="40089907";
		else
			nodecode = "40089901";
		
	}
	
	if (strSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_saleOut) )
		nodecode = "40089902";
	if (strSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_borrowIn) ||strSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_initBorrow))
		nodecode = "40089903";
	if (strSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_lendOut)||strSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_initLend) )
		nodecode = "40089904";
	if (strSourceBillType.equals("3Q"))
		nodecode = "40060204";
	if (strSourceBillType.equals(BillTypeConst.IC_VMI))
		nodecode = "40089906";
	if (strSourceBillType.equals(nc.vo.ic.pub.BillTypeConst.m_otherIn) )
		nodecode = "40089908";	

	return nodecode;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-12-1 12:19:04)
 */
public void initData(java.lang.String pkCorp, java.lang.String operator, java.lang.String funNode, java.lang.String businessType, java.lang.String currentBillType, java.lang.String sourceBilltype, java.lang.Object userObj) throws java.lang.Exception {
	
	String srcnodecode = GenMethod.getNodeCodeByBillType(sourceBilltype);
	if (sourceBilltype.equals(nc.vo.ic.pub.BillTypeConst.m_purchaseIn) && currentBillType.equals("4C"))
		srcnodecode="40080608";
	setTempletID(pkCorp, srcnodecode, operator, businessType,getNodeCode(currentBillType,sourceBilltype));
	
	setCurUserID(operator);
	setLoginCorp(pkCorp);
	setCurFunCode(funNode);
	
	setPowerCorp(false);
	initCorp();

	//update by danxionghui NO 47
	UFDate data=nc.ui.pub.ClientEnvironment.getInstance().getDate();
	Date date=data.toDate();
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    cal.set(Calendar.DAY_OF_MONTH, value);
    Date date1 =cal.getTime();
    UFDate ufDate=new UFDate(date1);
    //update end
	setDefaultValue(
		"head.dbilldate",
		null,
		(nc.ui.pub.ClientEnvironment.getInstance().getDate()) == null
			? null
			: (nc.ui.pub.ClientEnvironment.getInstance().getDate()).toString());
	//入库日期 update by danxionghui NO 47
	setDefaultValue("body.dbizdate",null,(ufDate) == null? null: (ufDate).toString());
//	setDefaultValue("dbizdate",null,(ufDate) == null? null: (ufDate).toString());
	///入库日期 update by danxionghui
	setDefaultValue(
		"dbilldate",
		null,
		(nc.ui.pub.ClientEnvironment.getInstance().getDate()) == null
			? null
			: (nc.ui.pub.ClientEnvironment.getInstance().getDate()).toString());

	hideNormal();
	
	//只有采购订单补货参照采购入库退库时提供多公司查询
	if (!getNodeCode(currentBillType,sourceBilltype).equals("40089901")) {
		hideCorp();
    setPowerRefsOfCorp("pk_corp", nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlgNotByProp(this),null);
	}else {
        //setPowerRefsOfCorp("pk_corp", new String[]{"head.cwarehouseid"},null);		
    setPowerRefsOfCorp("pk_corp", nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlgNotByProp(this),null);
	}
  //nc.ui.ic.pub.tools.GenMethod.setDataPowerFlagByRefName(this, false, new String[]{"存货档案","存货分类"});
  
  if(currentBillType!=null && currentBillType.trim().startsWith("4") && !Bill53Const.cbilltype.equals(currentBillType))
    nc.ui.ic.pub.tools.GenMethod.setDataPowerFlagByRefName(this, false, new String[]{"存货档案","存货分类"});
}

}
