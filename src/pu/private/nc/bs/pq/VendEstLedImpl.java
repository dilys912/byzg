package nc.bs.pq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import nc.itf.pq.IVendEstLed;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pi.NormalCondVO;
import nc.vo.pi.RelatedTableVO;
import nc.vo.pq.VendEstVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * 供应商估明细BO
 * 作者：王印芬
 * @version   最后修改日期
 * @see         需要参见的其它类
 * @since		从产品的那一个版本，此类被添加进来。（可选）
 */
public class VendEstLedImpl implements IVendEstLed {
/**
 * SortAnalyseBO 构造子注解。
 */
public VendEstLedImpl() {
	super();
}

/**
 * 支持数据权限处理，拆分查询条件是用户录入自定义查询条件，还是UI端系统拼接的数据权限查询条件
 * @param voaCond
 * @return{0、非权限条件VO[]；1、权限条件查询串}
 */
private ArrayList dealCondVosForPower(ConditionVO[] voaCond){
	
	//拆分用户录入VO、数据权限VO
	ArrayList listUserInputVos = new ArrayList();
	ArrayList listPowerVos = new ArrayList();
	
	int iLen = voaCond.length;
	
	for(int i=0; i<iLen; i++){
		if(voaCond[i].getOperaCode().trim().equalsIgnoreCase("IS") && voaCond[i].getValue().trim().equalsIgnoreCase("NULL")){
			listPowerVos.add(voaCond[i]);
      i++;
      listPowerVos.add(voaCond[i]);
		}else{
			listUserInputVos.add(voaCond[i]);
		}
	}
	
	//组织返回VO
	ArrayList listRet = new ArrayList();
	
	//用户录入
	ConditionVO[] voaCondUserInput = null;
	if(listUserInputVos.size() > 0){
		voaCondUserInput = new ConditionVO[listUserInputVos.size()];
		listUserInputVos.toArray(voaCondUserInput);
	}
	listRet.add(voaCondUserInput);
	
	//数据权限VO==>查询条件串
	ConditionVO[] voaCondPower = null;
	if(listPowerVos.size() > 0){
		voaCondPower = new ConditionVO[listPowerVos.size()];
		listPowerVos.toArray(voaCondPower);
		String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);
		//将非标准的字段替换掉
//		strPowerWherePart = StringUtil.replace(strPowerWherePart, "po_order.cdeptid_incsub", "po_order.cdeptid");
//		strPowerWherePart = StringUtil.replace(strPowerWherePart, "po_praybill.cdeptid_incsub", "po_praybill.cdeptid");
		//
		listRet.add(strPowerWherePart);
	}else{
		listRet.add(null);
	}
	
	return listRet;
	
}

/**
 * 对期初数据、入库数据、结算数据进行排序。
 *  @param          
 *  @return         
 *  @exception    异常描述
 *  @see              需要参见的其它内容
 *  @since            从类的那一个版本，此方法被添加进来
 * @return java.lang.String
 */
private void addAInvVOsToAVendorVEC(
	Vector aVendorVEC,
	VendEstVO[] VOs,
	int beginPos,
	int endPos,
	UFDate dBeginDate,
	UFDate dEndDate)
	throws BusinessException {

	//没有期初，可直接返回
	if (VOs == null) {
		return;
	}

	//期初统计数据
	VendEstVO beforeSumVO = null;
	//期初明细数据
	Vector ledVEC = new Vector();
	//得到期初合计数据，及位于日期范围内的结算明细数据
	double dBeforeSumNum = 0.0 ;
	double dBeforeSumMny = 0.0 ;
	double dBeforeSumSettledNum = 0.0 ;
	double dBeforeSumGaugeMny = 0.0 ;
	double dBeforeSumSettledMny = 0.0 ;
	//该存货的小计数据
	double dSumNum = 0.0 ;
	double dSumMny = 0.0 ;
	double dSumSettledNum = 0.0 ;
	double dSumGaugeMny = 0.0 ;
	double dSumSettledMny = 0.0 ;

	for (int i = beginPos; i <= endPos; i++) {
		if (VOs[i].getCabstract().equals("入库")) {
			//入库日期在期初范围内
			if (VOs[i].getBilldate().before(dBeginDate)) {
				if (beforeSumVO == null)
					beforeSumVO = new VendEstVO();
				//总计入库数量及入库金额
				if (VOs[i].getNinnum() != null) {
					dBeforeSumNum = dBeforeSumNum + VOs[i].getNinnum().doubleValue() ;
				}
				if (VOs[i].getNinmny() != null) {
					dBeforeSumMny = dBeforeSumMny + VOs[i].getNinmny().doubleValue() ;
				}
			}else	if (VOs[i].getBilldate().compareTo(dEndDate)<=0
					&& VOs[i].getBilldate().compareTo(dBeginDate)>=0) {//入库日期在查询日期范围内
				ledVEC.addElement(VOs[i]);
			}else continue;
		}else	if (VOs[i].getCabstract().equals("结算")) {
			//结算日期在期初范围内，则相加，否则取出该VO为查询日期范围内的明细一员
			if (VOs[i].getDsettledate().before(dBeginDate)) {
				if (beforeSumVO == null)
					beforeSumVO = new VendEstVO();

				//总计结算数量、结转金额、结算金额
				if (VOs[i].getNsumsettlenum() != null) {
					dBeforeSumSettledNum += VOs[i].getNsumsettlenum().doubleValue() ;
				}
				if (VOs[i].getNsumgaugemny() != null) {
					dBeforeSumGaugeMny += VOs[i].getNsumgaugemny().doubleValue() ;
				}
				if (VOs[i].getNsumsettlemny() != null) {
					dBeforeSumSettledMny += VOs[i].getNsumsettlemny().doubleValue() ;
				}
			} else	if (VOs[i].getDsettledate().compareTo(dEndDate)<=0 && 
						VOs[i].getDsettledate().compareTo(dBeginDate)>=0) {////结算日期在查询日期范围内
				ledVEC.addElement(VOs[i]);
			}else continue;
		}
		//小计数据
		if(VOs[i].getNinnum()!=null)
			dSumNum += VOs[i].getNinnum().doubleValue() ;
		if(VOs[i].getNinmny()!=null)
			dSumMny += VOs[i].getNinmny().doubleValue() ;
		if(VOs[i].getNsumsettlenum()!=null)
			dSumSettledNum += VOs[i].getNsumsettlenum().doubleValue() ;
		if(VOs[i].getNsumgaugemny()!=null)
			dSumGaugeMny += VOs[i].getNsumgaugemny().doubleValue() ;
		if(VOs[i].getNsumsettlemny()!=null)
			dSumSettledMny += VOs[i].getNsumsettlemny().doubleValue() ;
	}

	//对应期初行的VO
	if (beforeSumVO != null) {
		beforeSumVO.setCabstract("期初");
		beforeSumVO.setCvendormangid(VOs[beginPos].getCvendormangid());
		beforeSumVO.setCvendorname(VOs[beginPos].getCvendorname());
		beforeSumVO.setCmangid(VOs[beginPos].getCmangid());
		beforeSumVO.setCinvcode(VOs[beginPos].getCinvcode());
		beforeSumVO.setCinvname(VOs[beginPos].getCinvname());
		beforeSumVO.setCinvclassname(VOs[beginPos].getCinvclassname());		
		beforeSumVO.setNinnum(dBeforeSumNum==0.0?null:new	UFDouble(dBeforeSumNum) );
		beforeSumVO.setNinmny(dBeforeSumMny==0.0?null:new	UFDouble(dBeforeSumMny) );
		beforeSumVO.setNsumsettlenum(dBeforeSumSettledNum==0.0?null:new	UFDouble(dBeforeSumSettledNum) );
		beforeSumVO.setNsumgaugemny(dBeforeSumGaugeMny==0.0?null:new	UFDouble(dBeforeSumGaugeMny) );
		beforeSumVO.setNsumsettlemny(dBeforeSumSettledMny==0.0?null:new	UFDouble(dBeforeSumSettledMny) );
		//期初余数：入库数量－结算数量
		//期初余额：入库金额－暂估金额
		double	dTemp = dBeforeSumNum - dBeforeSumSettledNum ;
		beforeSumVO.setNresidualnum(dTemp==0.0?null:new	UFDouble(dTemp) );
		dTemp = dBeforeSumMny - dBeforeSumGaugeMny ;
		beforeSumVO.setNbalance(dTemp==0.0?null:new	UFDouble(dTemp) );

		//加入到aVendorVEC中
		aVendorVEC.addElement(beforeSumVO);
	}

	if (ledVEC.size() > 0) {
		//余数、余额：上一行余数、余额加本行入库数量、入库金额再减本行结转数量、结转金额。
		double	dResiduleNum = dBeforeSumNum-dBeforeSumSettledNum;
		double	dBal = dBeforeSumMny-dBeforeSumGaugeMny;
		for (int i = 0; i < ledVEC.size(); i++) {
			VendEstVO	curRowVO = (VendEstVO)ledVEC.elementAt(i) ;

			double	dTemp = 	dResiduleNum 
							+ (curRowVO.getNinnum()==null?0.0:curRowVO.getNinnum().doubleValue()) 
							- (curRowVO.getNsumsettlenum()==null?0.0:curRowVO.getNsumsettlenum().doubleValue()) ;
			dResiduleNum = dTemp ;
			curRowVO.setNresidualnum(dResiduleNum==0.0?null:new	UFDouble(dResiduleNum) );
			
			dTemp = 	dBal 
					+ (curRowVO.getNinmny()==null?0.0:curRowVO.getNinmny().doubleValue())
					- (curRowVO.getNsumgaugemny()==null?0.0:curRowVO.getNsumgaugemny().doubleValue()) ;
			dBal = dTemp ;
			curRowVO.setNbalance(dBal==0.0?null:new	UFDouble(dBal) );

			if(beforeSumVO!=null || i!=0)
				curRowVO.setCmangid(null) ;//不显示存货
				
			aVendorVEC.addElement(curRowVO);
		}

	}
	/*为支持排序功能，利用前台有小计功能
	//小计:[入库数量][入库金额][结算数量][结转金额][结算金额]
	VendEstVO	subTotalVO = new	VendEstVO() ;
	subTotalVO.setCabstract(Iinvoice.SYMBOL_SUBTOTAL);
	subTotalVO.setCvendormangid(VOs[beginPos].getCvendormangid());
	subTotalVO.setCmangid(null);//不再显示存货
	subTotalVO.setNinnum(dSumNum==0.0?null:new	UFDouble(dSumNum) );
	subTotalVO.setNinmny(dSumMny==0.0?null:new	UFDouble(dSumMny) );
	subTotalVO.setNsumsettlenum(dSumSettledNum==0.0?null:new	UFDouble(dSumSettledNum) );
	subTotalVO.setNsumgaugemny(dSumGaugeMny==0.0?null:new	UFDouble(dSumGaugeMny) );
	subTotalVO.setNsumsettlemny(dSumSettledMny==0.0?null:new	UFDouble(dSumSettledMny) );

	//加入到aVendorVEC中
	aVendorVEC.addElement(subTotalVO);
	*/

	return;

}

/**
 * 对期初数据、入库数据、结算数据进行排序。
 *  @param          
 *  @return         
 *  @exception    异常描述
 *  @see              需要参见的其它内容
 *  @since            从类的那一个版本，此方法被添加进来
 * @return java.lang.String
 */
private int findInvEndIndex(
	VendEstVO[]	curVO,
	int			beginPos)
	throws BusinessException {
		
	if ( curVO == null || beginPos<0 || beginPos>curVO.length)
		return -1;

	String	strInvCode = curVO[beginPos].getCinvcode() ;
	for (int i = beginPos; i < curVO.length; i++){
		if(!curVO[i].getCinvcode().equals(strInvCode)){
			return i-1 ;
		}
	}

	if(curVO[curVO.length-1].getCinvcode().equals(strInvCode)){
		return curVO.length-1 ;
	}

	return -1 ;
	
}

/**
 * 对期初数据、入库数据、结算数据进行排序。
 *  @param          
 *  @return         
 *  @exception    异常描述
 *  @see              需要参见的其它内容
 *  @since            从类的那一个版本，此方法被添加进来
 * @return java.lang.String
 */
private VendEstVO[][] findSortedVOsByRelations(
	VendEstVO[][] vos,
	UFDate		dBeginDate,
	UFDate		dEndDate)
	throws BusinessException {
	if ( vos == null )
		return null;

	//所有供应商的所有存货
	Vector	allVEC = new	Vector() ; 
	//开始整合
	for (int i = 0; i <vos.length; i++){
		//存入当前供应商的所有VO
		Vector	curVendorVEC = new	Vector() ;
		//当前供应商对应的VO
		VendEstVO[]		curVendorVO = vos[i] ;
		//对一个供应商的所有存货进行整理
		int		beginPos = 0 ;
		while(beginPos<curVendorVO.length){
			//String	curInvMang =  curVendorVO[beginPos].getCmangid() ;
			//得到该存货的起止范围 
			int	endPos = findInvEndIndex(curVendorVO,beginPos) ;
			if(endPos==-1){
				break ;
			}

			//加入该存货对应的VO到VEC中
			addAInvVOsToAVendorVEC(curVendorVEC,curVendorVO,beginPos,endPos,dBeginDate,dEndDate) ;

			//下一个存货
			beginPos = endPos+1 ;
		}

		//加入到全部VEC中
		allVEC.addElement(curVendorVEC) ;
	}

	VendEstVO[][]	allVOs = new	VendEstVO[allVEC.size()][] ;
	for (int i = 0; i < allVEC.size(); i++){
		allVOs[i] = new	VendEstVO[((Vector)allVEC.elementAt(i)).size()] ;
		((Vector)allVEC.elementAt(i)).copyInto(allVOs[i]) ;
	}
	
	return allVOs; 
}

/**
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 *  @param          参数说明
 *  @return          返回值
 *  @exception    异常描述
 *  @see              需要参见的其它内容
 *  @since            从类的那一个版本，此方法被添加进来
 * @param condVOs nc.vo.pub.query.ConditionVO[]
 */
public VendEstVO[][] findVendEstLedVOsByCondMy(
	NormalCondVO[]	normalVOs,
	ConditionVO[] definedVOs)
	throws BusinessException {
	
	//数据权限控制
    ArrayList listRet = dealCondVosForPower(definedVOs);
    definedVOs = (ConditionVO[]) listRet.get(0);
    String strDataPowerSql = (String) listRet.get(1);

	//得到查询条件	
	String[]	strFW = getFromWhere(normalVOs,definedVOs,strDataPowerSql) ;
	UFDate		dBeginDate = new	UFDate(strFW[1]) ;
	UFDate		dEndDate = new	UFDate(strFW[2]) ; 
	
	VendEstVO[][]	vos = null ;
	try {
		
		VendEstLedDMO dmo = new VendEstLedDMO();
		//小于查询结束日期范围的所有数据
		HashMap map1 = null;

		//增加入库单类型选择
		boolean b1 = false;
		boolean b2 = false;
		boolean b3 = false;
		//add by zwx 2015-3-9 
		String pk_corp="";
		for(int i = 0; i < normalVOs.length; i++){
			if(normalVOs[i].getKey().equals("采购入库暂估") && normalVOs[i].getValue().equals("是")) b1 = true;
			if(normalVOs[i].getKey().equals("委外加工入库暂估") && normalVOs[i].getValue().equals("是")) b2 = true;
			if(normalVOs[i].getKey().equals("VMI暂估") && normalVOs[i].getValue().equals("是")) b3 = true;
			//add by zwx 2015-3-9 
			if(normalVOs[i].getKey().equals("公司"))  pk_corp=(String) normalVOs[i].getValue();
		}
		if(b1 || b2){
			if(strDataPowerSql != null) map1 = dmo.findVOsByCondsMy(strFW[0] + " and " + strDataPowerSql + " ",dBeginDate,dEndDate);
			else map1 = dmo.findVOsByCondsMy(strFW[0],dBeginDate,dEndDate);
		}
				
		//增加对VMI暂估的处理
		if(b3){
			String sql = getFromWhereForVMI(normalVOs,definedVOs);
			 
//			HashMap map2 = dmo.findVOsByCondsForVMI(sql,dBeginDate,dEndDate);
			//edit by zwx 2015-3-9
			HashMap map2 = dmo.findVOsByCondsForVMICorp(sql,dBeginDate,dEndDate,pk_corp);

			if(map2 != null && map2.size() > 0){
				vos = combineForVMI(map1,map2);
			}else{
				if(map1 != null && map1.size() > 0) vos = combineForVMI(null, map1);
			}
			
		}else{
			if(map1 != null && map1.size() > 0) vos = combineForVMI(null, map1);
		}	
	} catch (Exception e) {
		nc.bs.pu.pub.PubDMO.throwBusinessException(e);
	}

	if(vos==null )
		return null ;

	return findSortedVOsByRelations(vos,dBeginDate,dEndDate) ;

	
}

 /**
* 作者：王印芬
* 功能：得到非期初的查询条件
* 参数：NormalCondVO[]	normalVOs	常用条件VO数组
		ConditionVO[] definedVOs	自定义条件VO数组
* 返回：无
* 例外：无
* 日期：(2002-3-13 11:39:21)
* 修改日期，修改人，修改原因，注释标志：
* 2002-06-24	WYF		如果对结算单作日期限制，则有一部分符合条件的入库单也会过滤掉
*					因此去掉结算单的条件限制，但在DMO中按日期过滤
*/

 //已全部结算入库单不统计：
	//选择此项时，不包含入库数量等于累计结算数量的入库单及与此入库单关联的结算单。
	//相当于往来两清功能。
	//在FROM中作相应改变,需包括表"ic_general_bb3"
	
private String[] getFromWhere(
	NormalCondVO[]	normalVOs,
	ConditionVO[] definedVOs,
	String strDataPowerSql)  throws BusinessException{

	//暂估过的表体行才查
	String condStr =" ic_general_h.dr=0"
				+	" AND ic_general_b.dr=0 AND ic_general_b.bzgflag='Y'"
				+	" AND ic_general_bb3.dr=0" ;

	//增加入库单类型选择
	boolean b1 = false;
	boolean b2 = false;
	for(int i = 0; i < normalVOs.length; i++){
		if(normalVOs[i].getKey().equals("采购入库暂估") && normalVOs[i].getValue().equals("是")) b1 = true;
		if(normalVOs[i].getKey().equals("委外加工入库暂估") && normalVOs[i].getValue().equals("是")) b2 = true;		
	}
	if(b1 && b2) condStr += " AND ic_general_h.cbilltypecode IN ('45','47','4T')";
	else if(b1 && (!b2)) condStr += " AND ic_general_h.cbilltypecode IN ('45','4T')";
	else if((!b1) && b2) condStr += " AND ic_general_h.cbilltypecode = '47'";
	//
				
	//相关表VO
	RelatedTableVO	tableVO = new	RelatedTableVO("入库单") ;
	
	//结算单的日期范围
	//String	strSettlePeriod = "" ;
	//查询起始日期
	String	beginDate = null ;
	//查询结束日期
	String	endDate = null ;
	String pk_corp = null;
	//////////////////对常用条件进行处理
	for (int i = 0; i < normalVOs.length; i++){
		//公司
		if(normalVOs[i].getKey().equals("公司")){
			pk_corp = (String)normalVOs[i].getValue();
			condStr += " AND " + normalVOs[i].getWhereStr("ic_general_b.pk_invoicecorp","=",NormalCondVO.STRING) ;
		}
		//起
		if(normalVOs[i].getKey().equals("日期起")){
			//condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_b.dbizdate",">=",NormalCondVO.STRING) ;
			//zhounl , since v55, 单据日期条件匹配入库单的暂估日期
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_b.dzgdate",">=",NormalCondVO.STRING) ;
			beginDate = normalVOs[i].getValue().toString() ;
		}
		//止
		else	if(normalVOs[i].getKey().equals("日期止")){
			//condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_b.dbizdate","<=",NormalCondVO.STRING) ;
			//zhounl , since v55, 单据日期条件匹配入库单的暂估日期
			condStr += " AND " +  normalVOs[i].getWhereStr("ic_general_b.dzgdate","<=",NormalCondVO.STRING) ;
			//strSettlePeriod += normalVOs[i].getWhereStr("po_settlebill.dsettledate","<=",NormalCondVO.STRING) ;
			endDate = normalVOs[i].getValue().toString() ;
		}
		//结算是否统计
		else	if(normalVOs[i].getKey().equals("已全部结算入库单不统计")){
			if(normalVOs[i].getValue().equals("是")){
				condStr += " AND ic_general_b.ninnum <> ic_general_bb3.naccountnum1"  ;
			}
		}
		//供应商基本ID:专为供应商暂估余额设计
		else	if(normalVOs[i].getKey().equals("供应商基本ID")){
			condStr += " AND ic_general_h.pk_cubasdoc='" + normalVOs[i].getValue() + "'" ;
		}
	}

	//处理供应商所在地区的查询条件(包含处理)
	definedVOs = dealAreaForVendor(definedVOs,pk_corp);
	definedVOs = dealContainRelation(definedVOs);
	
	if(strDataPowerSql != null && strDataPowerSql.indexOf("bd_psndoc") >= 0 && !tableVO.containTable("业务员")) tableVO.addElement("业务员");
	if(strDataPowerSql != null && strDataPowerSql.indexOf("bd_deptdoc") >= 0 && !tableVO.containTable("部门")) tableVO.addElement("部门");

	//自定义
	String	strDefined = "" ;
	boolean bProjectIncluded = false;//是否包含项目
	//////////////////对自定义条件进行处理
	if(definedVOs!=null && definedVOs.length!=0){
		//常用条件与自定义条件为AND关系
		for (int i = 0; i < definedVOs.length; i++){
			strDefined += definedVOs[i].getSQLStr() ;
			if(i==0){
				strDefined = strDefined.substring(strDefined.indexOf("(", 1));
			}
			//得到表名
			int	index = definedVOs[i].getFieldCode().indexOf(".",1) ;
			String	tableName = definedVOs[i].getFieldCode().substring(0,index) ;
			//加入到表的VO中
			String	describtion = RelatedTableVO.getJoinTableDescription(tableName) ;
			if(!bProjectIncluded){
				if(describtion != null && describtion.equals("项目")) bProjectIncluded = true;
			}
			if(describtion != null && !describtion.equals("项目"))
				tableVO.addElement(describtion) ;
		}
		if(strDefined != null && strDefined.trim().length() > 0) strDefined = " AND (" + strDefined + ")" ;
	}

	tableVO.addElement("供应商基本档案") ;
	tableVO.addElement("供应商管理档案") ;
	tableVO.addElement("存货基本档案") ;
	tableVO.addElement("存货管理档案") ;
	
	condStr += strDefined ;
	
	///////////////以入库单为中心
	String	strBaseTableJoin = 	tableVO.getBaseTableJoin()
							//结算单
							+ 	" LEFT OUTER JOIN po_settlebill_b "
							+	" ON ic_general_b.cgeneralhid=po_settlebill_b.cstockid"
							+	" AND ic_general_b.cgeneralbid=po_settlebill_b.cstockrow"
							+	" AND ic_general_b.dr=po_settlebill_b.dr"
							+	" LEFT OUTER JOIN po_settlebill"
							+ 	" ON  po_settlebill_b.csettlebillid=po_settlebill.csettlebillid"
							+ 	" AND  po_settlebill_b.dr=po_settlebill.dr"
							//发票
							+ 	" LEFT OUTER JOIN po_invoice_b "
							+	" ON po_settlebill_b.cinvoiceid=po_invoice_b.cinvoiceid"
							+	"    AND po_settlebill_b.cinvoice_bid=po_invoice_b.cinvoice_bid"
							+	"    AND po_settlebill_b.dr=po_invoice_b.dr"
							+	" LEFT OUTER JOIN po_invoice"
							+	"   ON po_invoice_b.cinvoiceid=po_invoice.cinvoiceid "
							+	"   AND po_invoice_b.dr=po_invoice.dr ";
	tableVO.setBaseTableJoin(strBaseTableJoin) ;

	//得到FROM WHERE
	String	strJoinTable = tableVO.getFromTable() ;
	if(bProjectIncluded) strJoinTable += " INNER JOIN bd_jobmngfil ON ic_general_b.cprojectid=bd_jobmngfil.pk_jobmngfil INNER JOIN bd_jobbasfil ON bd_jobmngfil.pk_jobbasfil=bd_jobbasfil.pk_jobbasfil ";

	//处理存货类
	if(strJoinTable.indexOf("bd_invcl") < 0){
		strJoinTable += " INNER JOIN bd_invcl on bd_invcl.pk_invcl = bd_invbasdoc.pk_invcl ";
	}
	//

	condStr = "FROM " + strJoinTable + " WHERE " + condStr + " AND bd_cumandoc.custflag in ('1','3') ";


	return new	String[]{condStr,beginDate,endDate};
}

/**
 * 此处插入方法说明。
 * 创建日期：(2004-3-4 14:00:47)
 * @return java.util.HashMap
 * @param map1 java.util.HashMap
 * @param map2 java.util.HashMap
 */
private VendEstVO[][] combineForVMI(HashMap map1, HashMap map2) {
	VendEstVO[][] ledVOs = null;
	
	if(map1 == null || map1.size() == 0){
		//转换后,直接返回
		ledVOs = new VendEstVO[map2.size()][];
		Object[] voArray = map2.values().toArray();
		for (int i = 0; i < voArray.length; i++){
			ledVOs[i] = new	VendEstVO[((Vector)voArray[i]).size()];
			((Vector)voArray[i]).copyInto(ledVOs[i]);
		}
		
	}else{
		//若MAP2的主键与MAP1的主键重复,则将MAP2的该主键下的内容加到MAP1的该主键下
		Set set = map1.keySet();
		Object key[] = set.toArray();

		for(int i = 0; i < key.length; i++){
			Vector vTemp0 = (Vector) map1.get(key[i]);
			if(map2.containsKey(key[i])){
				Vector vTemp1 = (Vector) map2.get(key[i]);
				for(int j = 0; j < vTemp1.size(); j++) vTemp0.addElement(vTemp1.elementAt(j));
				map1.put(key[i],vTemp0);
			}
		}

		//若MAP2的主键不与MAP1的主键重复,则将MAP2的该主键下的内容加到MAP1中
		set = map2.keySet();
		key = set.toArray();
		for(int i = 0; i < key.length; i++){
			Vector vTemp0 = (Vector) map2.get(key[i]);
			if(!map1.containsKey(key[i])) map1.put(key[i],vTemp0);
		}
		
		//转换后,返回
		ledVOs = new VendEstVO[map1.size()][];
		Object[] voArray = map1.values().toArray();
		for (int i = 0; i < voArray.length; i++){
			ledVOs[i] = new	VendEstVO[((Vector)voArray[i]).size()];
			((Vector)voArray[i]).copyInto(ledVOs[i]);
		}		
	}
	
	return ledVOs;
}

/**
 * 此处插入方法说明。
 * 功能描述:处理查询条件的包含关系
 * 输入参数: ConditionVO[],需要处理的VO,String[]多公司参数
 * 返回值:
 * 异常处理:
 * 日期:
 * @return nc.vo.pub.query.ConditionVO[]
 */
private ConditionVO[] dealAreaForVendor(ConditionVO[] cons,String pk_corp) throws BusinessException {
	/*************************************************************/
	//构造形如:( (bd_areacl.areaclcode = '01') or (bd_areacl.areaclcode = '0301') or
	//(bd_areacl.areaclcode = '030101') or (bd_areacl.areaclcode = '030101') )
	//的条件
	/**************************************************************/
	Vector v = new Vector();	

	try {
		for(int i=0; cons!=null&&i<cons.length;i++) {
						
			//处理供应商所在地区查询条件
			if(!cons[i].getFieldCode().equals("bd_areacl.areaclcode")) {
				v.add(cons[i]);
								
			}else {
				nc.bs.pu.pub.PubDMO dmo = new nc.bs.pu.pub.PubDMO();
				String[] areaCodes = dmo.getSubAreaCodes(pk_corp,cons[i].getValue(),cons[i].getOperaCode());
				for(int j=0; j<areaCodes.length; j++ ) {
					
					ConditionVO con = (ConditionVO)cons[i].clone();
					if(j==0) {
						con.setNoLeft(false);
					}else {
						con.setNoLeft(true);
					}
					con.setOperaCode("=");
					con.setValue(areaCodes[j]);
					
					if(j==areaCodes.length -1) {
						con.setNoRight(false);
					}else {
						con.setNoRight(true);
					}

					if(j >0) {
						con.setLogic(false);
					}
					v.add(con);		
				}
			}
		}
				
	}catch(Exception e) {
		nc.bs.pu.pub.PubDMO.throwBusinessException(e);
	}
	
	ConditionVO[] vos = null;
	if(v.size() >0) {
		vos = new ConditionVO[v.size()];
		v.copyInto(vos);
	}
	return vos;	
}

/**
 * 此处插入方法说明。
 * 功能描述:处理查询条件的包含关系
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:
 * @return nc.vo.pub.query.ConditionVO[]
 */
private ConditionVO[] dealContainRelation(ConditionVO[] cons) {
	for(int i=0; cons!=null&&i<cons.length;i++) {
		//处理包含关系
		if(cons[i].getOperaCode().equalsIgnoreCase("like")){
			if(!cons[i].getValue().startsWith("%"))
				cons[i].setValue("%"+cons[i].getValue());
			//后面的"%"模板加
		}
	}
	return cons;
}

/**
 * 此处插入方法说明。
 * 功能描述:获得查询VMI数据的SQL
 * 输入参数:
 * 返回:
 * 异常处理:2004/03/03 xhq
 */
private String getFromWhereForVMI(NormalCondVO[] normalVOs,	ConditionVO[] definedVOs)
	throws BusinessException
{
	//分解查询条件
	String sCondition = "";

	try{

		for(int i = 0; i < definedVOs.length; i++){
			String sName = definedVOs[i].getFieldCode().trim();
			String sOpera = definedVOs[i].getOperaCode().trim();
			String sValue = definedVOs[i].getValue();
			String sSQL = definedVOs[i].getSQLStr();
			String sReplace = null;

			//存货编码
			if(sName.equals("bd_invbasdoc.invcode") && sValue!=null && sValue.length()>0){
				sReplace = "cinventoryid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B where A.pk_invbasdoc = B.pk_invbasdoc and invcode " + sOpera + " '" + sValue + "')";						
				String s = getReplacedSQL(sSQL,sName,sReplace);
				sCondition += s;	
			}

			//存货分类编码
			if(sName.equals("bd_invcl.invclasscode") && sValue!=null && sValue.length()>0){
				try{
					nc.bs.ps.cost.CostanalyseDMO ddmo = new nc.bs.ps.cost.CostanalyseDMO();
					String sClassCode[] = ddmo.getSubInvClassCode(sValue,sOpera);
					if(sOpera.toLowerCase().trim().equals("like")) sOpera = "=";

					if(sClassCode != null && sClassCode.length > 0){
						sValue = "(";													
						for(int j = 0; j < sClassCode.length; j++){
							if(j < sClassCode.length - 1) sValue += "invclasscode " + sOpera + " '" + sClassCode[j] + "' or ";
							else sValue += "invclasscode " + sOpera + " '" + sClassCode[j] + "')";
						}							
						sReplace = "cinventoryid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + sValue + ")";						
					}else{
						sReplace = "cinventoryid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and invclasscode " + sOpera + " '" + sValue + "')";							
					}
					String s = getReplacedSQL(sSQL,sName,sReplace);
					sCondition += s;
				}catch(Exception e){
					SCMEnv.out(e);
					throw e;
				}
			}

			//供应商名称
			if(sName.equals("bd_cubasdoc.custname") && sValue!=null && sValue.length()>0){
				if(sOpera.toLowerCase().equals("like")){					
					sReplace = "cvendorid in (select pk_cumandoc from bd_cumandoc A,bd_cubasdoc B where A.pk_cubasdoc = B.pk_cubasdoc and custname " + sOpera + " '%" + sValue + "%' and (custflag = '1' or custflag = '3'))";
				}else{
					sReplace = "cvendorid in (select pk_cumandoc from bd_cumandoc A,bd_cubasdoc B where A.pk_cubasdoc = B.pk_cubasdoc and custname " + sOpera + " '" + sValue + "' and (custflag = '1' or custflag = '3'))";						
				}
				String s = getReplacedSQL(sSQL,sName,sReplace);
				sCondition += s;
			}

			//供应商编码
			if(sName.equals("bd_cubasdoc.custcode") && sValue!=null && sValue.length()>0){
				sReplace = "cvendorid in (select pk_cumandoc from bd_cumandoc A,bd_cubasdoc B where A.pk_cubasdoc = B.pk_cubasdoc and custcode " + sOpera + " '" + sValue + "' and (custflag = '1' or custflag = '3'))";					
				String s = getReplacedSQL(sSQL,sName,sReplace);
				sCondition += s;
			}			

			//地区编码
			if(sName.equals("bd_areacl.areaclcode") && sValue!=null && sValue.length()>0){
				nc.bs.pu.pub.PubDMO pubDMO = new nc.bs.pu.pub.PubDMO();
				String vendorCode[] = pubDMO.getVendorCodeByAreaClassCode(normalVOs[0].getValue().toString(),sValue,sOpera);
				if(vendorCode == null || vendorCode.length == 0){
					vendorCode = new String[]{"null"};
				}
				if(sOpera.toLowerCase().trim().equals("like")) sOpera = "=";
				sValue = "(";														
				for(int j = 0; j < vendorCode.length; j++){
					if(j < vendorCode.length - 1) sValue += "custcode " + sOpera + " '" + vendorCode[j] + "' or ";
					else sValue += "custcode " + sOpera + " '" + vendorCode[j] + "')";
				}										
				sReplace = "cvendorid in (select pk_cumandoc from bd_cumandoc A,bd_cubasdoc B where A.pk_cubasdoc = B.pk_cubasdoc and " + sValue + " and (custflag = '1' or custflag = '3'))";
				String s = getReplacedSQL(sSQL,sName,sReplace);
				sCondition += s;
			}				
		}

		sCondition = processFirst(sCondition);
		
	}catch(Exception e){
		nc.bs.pu.pub.PubDMO.throwBusinessException(e);
	}
	
	return sCondition;
}

/**
 * 此处插入方法说明。 创建日期：(2004-3-3 14:39:17)
 */
private String processFirst(String s) {
  s = s.trim();

  int nNull = s.indexOf(" ");
  if (nNull > 0) {
    String s2 = s.substring(nNull);
    s = " and (" + s2 + ")";
    return " " + s + " ";
  }

  return s;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2004-3-4 11:46:35)
 */
private String getReplacedSQL(String sSource, String sOld,String sReplace) {
	if(sReplace == null || sReplace.trim().length() == 0) return sSource;

	int nStart = sSource.indexOf(sOld);
	if(nStart < 0) return sSource;
	int nMiddle = sSource.indexOf("'",nStart + 1);
	if(nMiddle < 0) return sSource;
	int nEnd = sSource.indexOf("'",nMiddle + 1);

	String s1 = sSource.substring(0,nStart);
	String s2 = sSource.substring(nEnd + 1);

	String s = s1 + sReplace + s2;
	
	return s;
}

}