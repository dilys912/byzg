package nc.ui.pi.invoice;

///////JAVA
import java.util.*;
//////VO
import nc.vo.pi.*;
import nc.vo.pub.query.*;
import nc.vo.pub.util.*;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pub.SCMEnv;
//////UI
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.*;
import nc.ui.pi.pub.*;
import nc.ui.bd.ref.*;
import nc.ui.pub.pf.*;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.MultiCorpQueryClient;

/**
 * 入库单->发票的查询框
 * 作者：王印芬
 * @version   最后修改日期
 * @see         需要参见的其它类
 * @since		从产品的那一个版本，此类被添加进来。（可选）
 */
public class InvFrmStoQueDlg extends MultiCorpQueryClient implements IinitQueryData{
  //虚拟公司名称
  public final static String CORPKEY = "ic_general_corp";

  /*
   * 公司关联的档案
   * 注意，
   * 另外，客户档案、业务类型 是UAP暂未支持数据权限
   * 
   * 部门、人员、库存组织、仓库(ic_general_h.cwarehouseid)暂取消数据权限控制 20070413
   * 
  */
   public final static String[] REFKEYS = new String[]{
//     "bd_deptdoc.deptcode",//部门档案,
//     "ic_general_h.cdptid",//部门档案,
//     "ic_general_h.cwarehouseid",//仓库档案,
     "ic_general_h.cproviderid",//供应商档案,
     "po_order.cgiveinvoicevendor",
     "o_order_b.cprojectid",//项目
     "bd_invcl.invclasscode",//存货分类
     "bd_invbasdoc.invcode"//存货档案
//     "bd_psndoc.psncode",//人员档案,
//     "ic_general_h.cbizid",//人员档案,
//     "ic_general_h.cwhsmanagerid",//人员档案,
   };

	//当前业务类型
	private java.lang.String m_strBizType;
	//来源单据类型
	private java.lang.String m_strBillType;
/**
 * InvFrmStoQueDlg 构造子注解。
 * @param parent java.awt.Container
 */
public InvFrmStoQueDlg(
	java.awt.Container parent) {

	super(parent);
  // 权限控制：设置参照和公司关系 SINCE V5.1
  initCorpRefs();

}
/**
 * InvFrmOrdQueDlg 构造子注解。
 * @param parent java.awt.Container
 */
public InvFrmStoQueDlg(
	java.awt.Container parent,
	String pk_corp,
	String operator,
	String nodecode,
	String strBizType,
	String strCurrBillType,
	String strBillType,
	Object obUser) {

	super(parent);
	setBizType(strBizType);
	setBillType(strBillType);
	m_pkCorp = pk_corp;
	m_operator = operator;
	m_sourceBillType = strBillType;

	try {
		initData(
			pk_corp,
			operator,
			nodecode,
			strBizType,
			strCurrBillType,
			strBillType,
			obUser);

	} catch (Exception e) {
		SCMEnv.out(e);

	}
  // 权限控制：设置参照和公司关系 SINCE V5.1
  initCorpRefs();

}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-9-28 9:43:30)
 * @return java.lang.String
 */
private java.lang.String getBillType() {
	return m_strBillType;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-9-28 9:43:30)
 * @return java.lang.String
 */
private java.lang.String getBizType() {
	return m_strBizType;
}
/**
 * 根据查询框的常用条件得到常用条件限制字符串
 * @param          参数说明
 * @return          返回值
 * @exception    异常描述
 * @see              需要参见的其它内容
 * @since            从类的那一个版本，此方法被添加进来。（可选）
 *
 */

//暂时取出所有该单位的发票,并同时显示第一张
private NormalCondVO[] getNormalCondVOs() {
	//发票日期，供应商，存货，部门，业务员,存货分类，地区分类
	//是否期初，是否审批，是否费用

	//查询条件VO
	NormalCondVO[]	vos = new	NormalCondVO[3] ;
	vos[0] = new	NormalCondVO("公司",getPk_corp()) ;
	vos[1] = new	NormalCondVO("业务类型",getBizType()) ;
	vos[2] = new	NormalCondVO("单据类型",getBillType()) ;

	return vos ;
}
/**
 * 得到公司主键－－默认参照使用。
 * 创建日期：(2001-8-17 11:17:03)
 * @return java.lang.String
 */
private String getPk_corp() {
	return ClientEnvironment.getInstance().getCorporation().getPk_corp();
}
/**
 * 得到根据入库单生成发票的查询条件
 *  @param          参数说明
 *  @return          返回值
 *  @exception    异常描述
 *  @see              需要参见的其它内容
 *  @since            从类的那一个版本，此方法被添加进来
 * @return java.lang.String
 */


 //注：认为来源必须为订单，不管上层来源是什么。
private String getPoStoWhereSQL(
	NormalCondVO[]	normalVOs,
	ConditionVO[] definedVOs
	) {

	//1删除的不做查询
	String condStr =
						//入库单
						" (ic_general_h.fbillflag = 3 OR ic_general_h.fbillflag = 4) "
					+	" AND ic_general_h.dr=0"
					+	" AND ic_general_b.dr=0"
						//赠品不能入库
					+   " AND ISNULL(ic_general_b.flargess,'N')='N'"
					+	" AND ic_general_bb3.dr=0"
					    //采购入库单已全完结算不能参照
					+   " AND ic_general_b.isok = 'N'"
						//其他条件
					+	" AND ic_general_b.ninnum IS NOT NULL ";
//					+	" AND ("
//					+	"		ic_general_bb3.nsignnum IS NULL "
//					+	"		OR ABS(ic_general_b.ninnum)>ABS(ic_general_bb3.nsignnum) "
//					+	"	   )" ;

	for (int i = 0; i < normalVOs.length; i++){
		if(normalVOs[i].getKey().equals("公司")){
			condStr += " AND " + normalVOs[i].getWhereStr("ic_general_b.pk_invoicecorp","=",NormalCondVO.STRING) ;
		}else	if(normalVOs[i].getKey().equals("业务类型")){
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_h.cbiztype","=",NormalCondVO.STRING) ;
		}else	if(normalVOs[i].getKey().equals("单据类型")){
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_h.cbilltypecode","=",NormalCondVO.STRING) ;
		}
	}

	/*入库日期	入库单号	订单号	供应商	存货		采购部门		业务员*/
	//相关表VO
	RelatedTableVO	tableVO = new	RelatedTableVO("入库单") ;
	//自定义
	String	strDefined = "", sTemp = "", s = "";
	int k1 = 0, k2 = 0;
	//////////////////对自定义条件进行处理
	if(definedVOs!=null && definedVOs.length!=0){
		//常用条件与自定义条件为AND关系
		for (int i = 0; i < definedVOs.length; i++){
      //把打印条件加上表名：以免后查询时报列名不明确的SQL异常
      if("iprintcount".equals(definedVOs[i].getFieldCode())){
        definedVOs[i].setFieldCode("ic_general_h.iprintcount");
      }else if("ISNULL(iprintcount,0)".equals(definedVOs[i].getFieldCode())){
        definedVOs[i].setFieldCode("ISNULL(ic_general_h.iprintcount,0)");
      }
			//得到该VO的对应语句
			sTemp = definedVOs[i].getSQLStr();
			if(sTemp != null && sTemp.indexOf("AND bd_cumandoc.pk_corp") >= 0){
				k1 = sTemp.indexOf("AND bd_cumandoc.pk_corp");
				k2 = sTemp.indexOf(")", k1);
				s = sTemp.substring(k1, k2);
				if(s != null) sTemp = sTemp.replaceAll(s, "");
			}
			if(sTemp != null && sTemp.indexOf("AND bd_invmandoc.pk_corp") >= 0){
				k1 = sTemp.indexOf("AND bd_invmandoc.pk_corp");
				k2 = sTemp.indexOf(")", k1);
				s = sTemp.substring(k1, k2);
				if(s != null) sTemp = sTemp.replaceAll(s, "");
			}

      if(sTemp != null && sTemp.indexOf("ic_general_h.cproviderid in ( (select areaclcode from bd_areacl where 0=0  and pk_areacl") >= 0){
        sTemp = sTemp.replace("ic_general_h.cproviderid in ( (select areaclcode from bd_areacl where 0=0  and pk_areacl", "ic_general_h.cproviderid in ( (select pk_cumandoc from bd_cubasdoc,bd_cumandoc,bd_areacl where bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc and bd_cubasdoc.pk_areacl = bd_areacl.pk_areacl and bd_areacl.pk_areacl");
      }
			strDefined += sTemp;
			if(i==0){
				strDefined = strDefined.substring(strDefined.indexOf("(", 1));
			}
			///得到表名
			int	index = definedVOs[i].getFieldCode().indexOf(".",1) ;
      
      //修正字符串索引异常
      if(index >= 0){
        String  table = definedVOs[i].getFieldCode().substring(0,index) ;
        if( !table.equals("ic_general_h") && !table.equals("po_order") ){
          String  describtion = RelatedTableVO.getJoinTableDescription(table) ;
          if(describtion!=null)
            tableVO.addElement(describtion) ;
        }
      }
		}
		strDefined = " AND (" + strDefined + ")" ;
	}
	condStr += strDefined ;

	//滤掉封存的存货
	tableVO.addElement("存货管理档案") ;
	condStr += " AND bd_invmandoc.sealflag='N'" ;

	String	strBaseTableJoin = 		tableVO.getBaseTableJoin()
			//add by danxionghui NO 20
			  //到货单
			+"LEFT OUTER JOIN po_arriveorder "
			+"ON  po_arriveorder.carriveorderid = ic_general_b.csourcebillhid"
			////end  by danxionghui NO20
								//订单
								+ " LEFT OUTER JOIN po_order_b "
								+ "      ON     ic_general_b.cfirstbillhid = po_order_b.corderid"
								+ "         AND ic_general_b.cfirstbillbid = po_order_b.corder_bid"
								+ " LEFT OUTER JOIN po_order "
								+ "      ON     po_order.corderid=po_order_b.corderid " ;
	if( !tableVO.containTable("供应商基本档案") ){
		strBaseTableJoin += " LEFT OUTER JOIN bd_cumandoc ON ic_general_h.cproviderid=bd_cumandoc.pk_cumandoc " ;
	}

	tableVO.setBaseTableJoin(strBaseTableJoin) ;
	strBaseTableJoin = tableVO.getFromTable() ;

	condStr += 	  " AND ("
				+ "         (ic_general_b.cfirsttype IS NULL) OR"
				+ "         ( "
				+ "                 po_order.dr=0 "
				+ "             AND po_order_b.dr=0 "
				+ "             AND ic_general_b.cfirsttype = '"+ nc.vo.scm.pu.BillTypeConst.PO_ORDER + "'"
				+ "          )"
				+ "     )"
				+ " AND (bd_cumandoc.frozenflag IS NULL OR bd_cumandoc.frozenflag='N')" ;

	strBaseTableJoin = "FROM " + strBaseTableJoin + " WHERE " + condStr ;
  
	return strBaseTableJoin ;
}
/**
 * 得到根据入库单生成发票的查询条件
 *  @param          参数说明
 *  @return          返回值
 *  @exception    异常描述
 *  @see              需要参见的其它内容
 *  @since            从类的那一个版本，此方法被添加进来
 * @return java.lang.String
 */


 //注：认为来源必须为订单，不管上层来源是什么。
private String getScStoWhereSQL(
	NormalCondVO[]	normalVOs,
	ConditionVO[] definedVOs
	) {

	//1删除的不做查询
	String condStr = 	//入库单
						" (ic_general_h.fbillflag = 3 OR ic_general_h.fbillflag = 4) "
					+	" AND ic_general_h.dr=0"
					+	" AND ic_general_b.dr=0"
						//赠品不能入库
					+   " AND ISNULL(ic_general_b.flargess,'N')='N'"
					+	" AND ic_general_bb3.dr=0"
						//其他条件
					+	" AND ic_general_b.ninnum IS NOT NULL ";
//					+	" AND (	"
//					+	"		ic_general_bb3.nsignnum IS NULL "
//					+	"		OR ABS(ic_general_b.ninnum)>ABS(ic_general_bb3.nsignnum)"
//					+	"	   )"
//					+	" AND ic_general_b.bzgflag='Y' " ;//edit by shikun 2015-01-22
	if (!("1078".equals(m_pkCorp)||"1108".equals(m_pkCorp))) {//非制盖公司edit by shikun 2015-01-22
		condStr = condStr + " AND ic_general_b.bzgflag='Y' " ;
	}else{//制盖公司--非库存委外加工入库单edit by shikun 2015-01-22
		if(!getBillType().equals(nc.vo.scm.pu.BillTypeConst.STORE_SC)){
			condStr = condStr + " AND ic_general_b.bzgflag='Y' " ;
		}
	}

	for (int i = 0; i < normalVOs.length; i++){
		if(normalVOs[i].getKey().equals("公司")){
			condStr += " AND " + normalVOs[i].getWhereStr("ic_general_h.pk_corp","=",NormalCondVO.STRING) ;
		}else	if(normalVOs[i].getKey().equals("业务类型")){
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_h.cbiztype","=",NormalCondVO.STRING) ;
		}else	if(normalVOs[i].getKey().equals("单据类型")){
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_h.cbilltypecode","=",NormalCondVO.STRING) ;
		}
	}

	/*入库日期	入库单号	订单号	供应商	存货		采购部门		业务员*/
	//相关表VO
	RelatedTableVO	tableVO = new	RelatedTableVO("入库单") ;
	//自定义
	String	strDefined = "" ;
	//////////////////对自定义条件进行处理
	if(definedVOs!=null && definedVOs.length!=0){
		//常用条件与自定义条件为AND关系
		for (int i = 0; i < definedVOs.length; i++){
			//得到该VO的对应语句
			strDefined += definedVOs[i].getSQLStr() ;
			if(i==0){
				strDefined = strDefined.substring(strDefined.indexOf("(", 1));
			}
			///得到表名
			int	index = definedVOs[i].getFieldCode().indexOf(".",1) ;
			String	table = definedVOs[i].getFieldCode().substring(0,index) ;
			if( !table.equals("ic_general_h") && !table.equals("po_order") ){
				String	describtion = RelatedTableVO.getJoinTableDescription(table) ;
				if(describtion!=null)
					tableVO.addElement(describtion) ;
			}
		}
		strDefined = " AND (" + strDefined + ")" ;
	}
	condStr += strDefined ;

	//滤掉封存的存货
	tableVO.addElement("存货管理档案") ;
	condStr += " AND bd_invmandoc.sealflag='N'" ;

	String	strBaseTableJoin = 		tableVO.getBaseTableJoin()
								//订单
								+ " LEFT OUTER JOIN sc_order_b "
								+ "      ON     ic_general_b.cfirstbillhid = sc_order_b.corderid"
								+ "         AND ic_general_b.cfirstbillbid = sc_order_b.corder_bid"
								+ " LEFT OUTER JOIN sc_order "
								+ "      ON     sc_order.corderid=sc_order_b.corderid " ;
	if( !tableVO.containTable("供应商基本档案") ){
		strBaseTableJoin += " LEFT OUTER JOIN bd_cumandoc ON ic_general_h.cproviderid=bd_cumandoc.pk_cumandoc " ;
	}
	tableVO.setBaseTableJoin(strBaseTableJoin) ;
	strBaseTableJoin = tableVO.getFromTable() ;

	condStr += 	  " AND ("
				+ "         (ic_general_b.cfirsttype IS NULL) OR"
				+ "         ( "
				+ "                 sc_order.dr=0 "
				+ "             AND sc_order_b.dr=0 "
				+ "             AND ic_general_b.cfirsttype = '"+ nc.vo.scm.pu.BillTypeConst.SC_ORDER + "'"
				+ "          )"
				+ "     )"
				+ " AND (bd_cumandoc.frozenflag IS NULL OR bd_cumandoc.frozenflag='N')" ;

	strBaseTableJoin = "FROM " + strBaseTableJoin + " WHERE " + condStr ;

	//所有有关采购订单的查询条件均替换为委外订单的查询条件
	strBaseTableJoin = StringUtil.replaceAllString(strBaseTableJoin,"po_order","sc_order") ;
	//返回
	return strBaseTableJoin ;
}
/**
 * 获得由ConditionVO[]组合成的where子句。
 * 创建日期：(2001-7-12 16:09:26)
 * @return java.lang.String
 * 2002-11-14	wyf		加入对LIKE处理
 */
public String getWhereSQL() {

	//查询条件VO
	NormalCondVO[]	normalVOs = getNormalCondVOs() ;
	ConditionVO[]	definedVOs = getConditionVO() ;
	definedVOs = dealAreaForVendor(definedVOs,this.getPk_corp());

	if(definedVOs != null && definedVOs.length >0)
		definedVOs = nc.ui.scm.pub.query.ConvertQueryCondition.getConvertedVO(definedVOs,getPk_corp());

	//wyf	2002-11-14	add	begin
	PiPqPublicUIClass.processLIKEInCondVOs(definedVOs) ;
	//wyf	2002-11-14	add	end

	if(getBillType().equals(nc.vo.scm.pu.BillTypeConst.STORE_SC)){
		return	getScStoWhereSQL(normalVOs,definedVOs) ;
	}else{
		return	getPoStoWhereSQL(normalVOs,definedVOs) ;
	}
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-12-1 12:19:04)
 */
public void initData(
	String pkCorp,
	String operator,
	String funNode,
	String businessType,
	String currentBillType,
	String sourceBilltype,
	Object userObj)
	throws Exception {

	//设置用到的变量
	setBillType(sourceBilltype) ;
	setBizType(businessType) ;
	m_pkCorp = pkCorp;
	m_operator = operator;
	m_sourceBillType = sourceBilltype;


	initiDefine() ;
	initiNormal() ;
	initiTitle() ;
	initiMultiUnit() ;
	//查询模板增加打印次数
	setShowPrintStatusPanel(true);

	nc.ui.scm.pub.def.DefSetTool.updateQueryConditionClientUserDef(
		this,
		getPk_corp(),
    "icbill",
		"ic_general.vuserdef",
		"ic_general_b.vuserdef");
	nc.ui.scm.pub.def.DefSetTool.updateQueryConditionForCubasdoc(
		this,
		getPk_corp(),
		"bd_cubasdoc.def"
		);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initiDefine() {
	//隐藏常用条件
	hideNormal() ;


}
/**
 * 作者：王印芬
 * 功能：初始化自定义模板
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2001-10-13 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 * 2002-12-02	wyf		修改存货及供应商的非树表状参数
 */
private void initiNormal() {

	//装裁模板
	//setTempletID("40040401030000000000") ;
	String s = null;
	if(m_sourceBillType.equals("45")){//库存采购入库单
		m_funNode = "40089910";
		s = "40080602";
	}else if(m_sourceBillType.equals("47")){//库存委外加工入库单
		m_funNode = "40089911";
		s = "40080606";
	}else if(m_sourceBillType.equals("4T")){//库存期初采购
		m_funNode = "40089912";
		s = "40080402";
	}
	setTempletID(m_pkCorp, s, m_operator, m_strBizType,m_funNode);


	//设置自定义的“订单号”
	//UIRefPane	pane1 = new	UIRefPane() ;
	//pane1.setIsCustomDefined(true) ;
	//AbstractRefModel	model1 = null ;
	//if(getBillType().equals(nc.vo.pu.pub.BillTypeConst.STORE_SC)){
		//model1 = new	VOrderCodeRefModel(getPk_corp(),getBizType(),nc.vo.pu.pub.BillTypeConst.SC_ORDER) ;
		//model1.addWherePart(" AND sc_order.ibillstatus=3") ;
	//}else{
		//model1 = new	VOrderCodeRefModel(getPk_corp(),getBizType(),nc.vo.pu.pub.BillTypeConst.PO_ORDER) ;
		//model1.addWherePart(" AND po_order.forderstatus IN (3,5,7)") ;
	//}
	//pane1.setRefModel(model1) ;
	//setValueRef("po_order.vordercode", pane1);

	//“入库单号”参照
	//AbstractRefModel	model2 = new	VGeneralCodeRefModel(getPk_corp(),getBizType(),getBillType()) ;
	//model2.addWherePart(" AND ic_general_h.cregister IS NOT NULL") ;
	//UIRefPane	pane2 = new	UIRefPane() ;
	//pane2.setIsCustomDefined(true) ;
	//pane2.setRefModel(model2) ;
	//setValueRef("ic_general_h.vbillcode", pane2);
	//滤掉封存的存货，不可采购存货
	//AbstractRefModel	model3 = new	nc.ui.bd.ref.DefaultRefModel("存货档案") ;
	DefaultRefGridTreeModel	model3 = new	DefaultRefGridTreeModel("存货档案") ;
	model3.addWherePart(" AND bd_invmandoc.sealflag='N' AND UPPER(ISNULL(bd_invmandoc.iscanpurchased,'Y')) = 'Y'") ;
	UIRefPane	pane3 = new	UIRefPane() ;
	//pane3.setIsCustomDefined(true) ;
	pane3.setRefModel(model3) ;
	setValueRef("bd_invbasdoc.invcode", pane3);

	//滤掉冻结的供应商
	//AbstractRefModel	model4 = new	nc.ui.bd.ref.DefaultRefModel("供应商档案") ;
	DefaultRefGridTreeModel	model4 = new	DefaultRefGridTreeModel("供应商档案") ;
	model4.addWherePart(" AND bd_cumandoc.frozenflag='N'") ;
	UIRefPane	pane4 = new	UIRefPane() ;
	//pane4.setIsCustomDefined(true) ;
	pane4.setRefModel(model4) ;
	setValueRef("bd_cubasdoc.custcode", pane4);

	setValueRef("ic_general_h.dbilldate","日历");
	setDefaultValue("ic_general_h.dbilldate","ic_general_h.dbilldate",nc.ui.pub.ClientEnvironment.getInstance().getDate().toString());

}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initiTitle() {

	setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("40040401","UPP40040401-000126")/*@res "根据入库单生成发票"*/);
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-9-28 9:43:30)
 * @param newBizType java.lang.String
 */
private void setBillType(java.lang.String newBillType) {
	m_strBillType = newBillType;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-9-28 9:43:30)
 * @param newBizType java.lang.String
 */
private void setBizType(java.lang.String newBizType) {
	m_strBizType = newBizType;
}

	//节点编码
	String m_funNode = null;
	//操作员
	String m_operator = null;
	//公司主键
	String m_pkCorp = null;
	//当前单据类型
	String m_sourceBillType = null;

/**
 * 此处插入方法说明。
 * 功能描述:处理查询条件的包含关系
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:
 * @return nc.vo.pub.query.ConditionVO[]
 */
private ConditionVO[] dealAreaForVendor(ConditionVO[] cons, String pk_corp) {
	/*************************************************************/
	//构造形如:( (bd_areacl.areaclcode = '01') or (bd_areacl.areaclcode = '0301') or
	//(bd_areacl.areaclcode = '030101') or (bd_areacl.areaclcode = '030101') )
	//的条件
	/**************************************************************/
	try {
		Vector v = new Vector();
		for (int i = 0; cons != null && i < cons.length; i++) {

			//处理供应商所在地区查询条件
			// modified by wwm on 2003-09-19
			if(cons[i].getTableCodeForMultiTable() == null
				|| !cons[i].getTableCodeForMultiTable().trim().equalsIgnoreCase("areaclcode")){
				v.add(cons[i]);
				continue;
			}
			//if (cons[i].getTableCodeForMultiTable() != null
				//&& !cons[i].getTableCodeForMultiTable().trim().equalsIgnoreCase("areaclcode")) {
				//v.add(cons[i]);

			// end
			else {

				String[] areaCodes =
					nc.ui.pu.pub.PubHelper.getSubAreaCodes(
						pk_corp,
						cons[i].getValue(),
						cons[i].getOperaCode());
				for (int j = 0; j < areaCodes.length; j++) {

					ConditionVO con = (ConditionVO) cons[i].clone();
					if (j == 0) {
						con.setNoLeft(false);
					} else {
						con.setNoLeft(true);
					}
					con.setOperaCode("=");
					con.setValue(areaCodes[j]);

					if (j == areaCodes.length - 1) {
						con.setNoRight(false);
					} else {
						con.setNoRight(true);
					}

					if (j > 0) {
						con.setLogic(false);
					}
					v.add(con);

				}
			}

		}

		ConditionVO[] vos = null;
		if (v.size() > 0) {
			vos = new ConditionVO[v.size()];
			v.copyInto(vos);
		}
		return vos;
	} catch (Exception e) {
		SCMEnv.out(e);
		return cons;
	}
}

/**
 * 隐藏"多单位选择"按钮
 * 创建日期：(2001-12-1 12:19:04)
 */
private void initiMultiUnit()
{
	hideUnitButton() ;
}

/**
 * 查询模板中没有公司时，要设置虚拟公司。
 * <p>
 * <b>examples:</b>
 * <p>
 * 使用示例
 * <p>
 * <b>参数说明</b>
 * <p>
 * @author lxd
 * @time 2007-3-13 上午11:10:56
 */
public void initCorpRefs(){

  ArrayList<String> alcorp=new ArrayList<String>();
  alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
  initCorpRef(CORPKEY,ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),alcorp);   
  //
  setDefaultCorps(new String[] {ClientEnvironment.getInstance().getCorporation().getPrimaryKey()});
  //
  setCorpRefs(CORPKEY, REFKEYS);
  //    
}
}