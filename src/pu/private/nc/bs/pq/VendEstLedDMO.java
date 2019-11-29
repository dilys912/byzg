package nc.bs.pq;

import java.sql.*;
import java.util.*;

import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.lang.*;
import nc.vo.pq.*;
import nc.vo.scm.pub.SCMEnv;

/**
 * 供应商估明细
 DMO
 * 作者：王印芬
 * @version   最后修改日期
 * @see         需要参见的其它类
 * @since		从产品的那一个版本，此类被添加进来。（可选）
 */
public class VendEstLedDMO extends nc.bs.pub.DataManageObject {
/**
 * SortAnalyseDMO 构造子注解。
 * @exception javax.naming.NamingException 异常说明。
 * @exception nc.bs.pub.SystemException 异常说明。
 */
public VendEstLedDMO() throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super();
}
/**
 * SortAnalyseDMO 构造子注解。
 * @param dbName java.lang.String
 * @exception javax.naming.NamingException 异常说明。
 * @exception nc.bs.pub.SystemException 异常说明。
 */
public VendEstLedDMO(String dbName) throws javax.naming.NamingException, nc.bs.pub.SystemException {
	super(dbName);
}






/**
 * 此处插入方法说明。
 * 创建日期：(2004-3-4 11:46:24)
 * 2007-09-29  CZP   支持分收集结后，要按供应商基本ID分组，否则界面展现时会出现重复的供应商
 */
public HashMap findVOsByCondsForVMI(
	String sCondition,
	UFDate	dBeginDate,
	UFDate	dEndDate)
	throws SQLException {
			
	String sql = "select cvendorid, custname, cinventoryid, invcode, invname, cvmihid, A.noutnum-A.noutinnum, A.nmoney, A.dgaugedate, ";
	sql += "B.dsettledate, B.csettlebillid, C.csettlebill_bid, C.nsettlenum, C.ngaugemny, C.nmoney, invclassname, ";
	sql += "vinvoicecode, dinvoicedate, D.pk_cubasdoc ";
	sql += "from ic_vmi_sum A "; 
	sql += "left outer join po_settlebill_b C on A.cvmihid = C.cvmiid and C.dr = 0 ";
	sql += "left outer join po_settlebill B on B.csettlebillid = C.csettlebillid and A.cinventoryid = C.cmangid and B.dr = 0 ";
	sql += "left outer join po_invoice I on I.cinvoiceid = C.cinvoiceid and I.dr = 0 ";
	sql += "inner join bd_cumandoc D on cvendorid = D.pk_cumandoc ";
	sql += "inner join bd_cubasdoc E on D.pk_cubasdoc = E.pk_cubasdoc ";
	sql += "inner join bd_invmandoc F on cinventoryid = F.pk_invmandoc ";
	sql += "inner join bd_invbasdoc G on F.pk_invbasdoc = G.pk_invbasdoc ";
	sql += "inner join bd_invcl H on G.pk_invcl = H.pk_invcl ";
	sql += "where bgaugeflag = 'Y' and A.dr = 0 and custflag in ('1','3') ";
	
	if(sCondition != null && sCondition.trim().length() > 0) sql += sCondition;
	
	sql += " order by cvendorid, cinventoryid, cvmihid, dgaugedate, dsettledate";
	
	CrossDBConnection con = null;
	Statement stmt = null;
	HashMap	map = new	HashMap() ;
	try {
		con = (CrossDBConnection)getConnection();
		con.enableSQLTranslator(false);
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		String	strOldGeneralBid = "" ;
    //
    String strPkCuBasDoc = null;
		while (rs.next()) {
			VendEstVO 	rsVO = new VendEstVO();

			//供应商ID
			String	str = rs.getString(1) ;
			rsVO.setCvendormangid( str==null? "":str ) ;
			//供应商NAME
			str = rs.getString(2) ;
			rsVO.setCvendorname( str==null? "":str ) ;
			//存货ID
			str = rs.getString(3) ; 
			rsVO.setCmangid( str==null? "":str ) ;
			//存货CODE
			str = rs.getString(4) ;
			rsVO.setCinvcode( str==null? "":str ) ;
			//存货NAME
			str = rs.getString(5) ;
			rsVO.setCinvname( str==null? "":str ) ;
			//入库单行ID
			str = (String) rs.getString(6);
			rsVO.setCgeneralbid((str==null||str.trim().equals("")) ? null:str.trim());
			//入库数量
			double dOb = rs.getDouble(7) ;
			rsVO.setNinnum( dOb==0.0? null : new	UFDouble(dOb) );
			//入库金额
			dOb = rs.getDouble(8) ;
			rsVO.setNinmny( dOb==0.0? null : new	UFDouble(dOb) );
			//入库日期
			String	dgeneraldate = (String) rs.getString(9);
			//结算日期
			String	dsettledate = (String) rs.getString(10);
			rsVO.setDsettledate(dsettledate==null ? null : new	UFDate(dsettledate)) ;
			//结算单ID
			String	csettlebillid = (String) rs.getString(11);
			rsVO.setCsettlebillid(csettlebillid) ;
			//结算单行ID
			String	csettlebill_bid = (String) rs.getString(12);
			rsVO.setCsettlebill_bid(csettlebill_bid) ;
			//结转数量
			dOb = rs.getDouble(13) ;
			rsVO.setNsumsettlenum( dOb==0.0? null : new	UFDouble(dOb) );
			//结转金额
			dOb = rs.getDouble(14) ;
			rsVO.setNsumgaugemny( dOb==0.0? null : new	UFDouble(dOb) );
			//结算金额
			dOb = rs.getDouble(15) ;
			rsVO.setNsumsettlemny( dOb==0.0? null : new	UFDouble(dOb) );
			//存货分类
			str = (String) rs.getString(16);
			rsVO.setCinvclassname((str==null||str.trim().equals("")) ? null:str.trim());
			//发票号	
			String	vinvoicecode = (String) rs.getString(17);
			//发票日期
			String	dinvoicedate = (String) rs.getString(18);
      //供应商基本ID
      strPkCuBasDoc = rs.getString(19);
			
			//一个入库单行对应几个结算单行，则拆分为1+n个行，第一行为入库单行，以后为结算单行
			Vector		apartVEC =	new	Vector() ;	
			//入库单行1--->结算单行1
			//入库单行1--->结算单行2
			if(!rsVO.getCgeneralbid().equals(strOldGeneralBid)){
				//入库
				VendEstVO	generalVO = (VendEstVO)rsVO.clone() ;
				generalVO.setCabstract("入库") ;
				generalVO.setNsumsettlenum(null) ;
				generalVO.setNsumgaugemny(null) ;
				generalVO.setNsumsettlemny(null) ;
				generalVO.setBilldate(dgeneraldate==null ? null : new	UFDate(dgeneraldate)) ;
				apartVEC.addElement(generalVO) ;
			}
			//结算
			if(rsVO.getCsettlebillid()!=null && !rsVO.getCsettlebillid().trim().equals("")){
				VendEstVO	settleVO = rsVO ;
				settleVO.setCabstract("结算") ;
				settleVO.setNinnum(null) ;
				settleVO.setNinmny(null) ;
				settleVO.setBillcode(vinvoicecode==null?null:vinvoicecode.trim()) ;
				settleVO.setBilldate(dinvoicedate==null ? null : new	UFDate(dinvoicedate)) ;
				apartVEC.addElement(settleVO) ;
			}
			strOldGeneralBid = rsVO.getCgeneralbid() ;

			if (apartVEC.size()>0) {
				if(!map.containsKey(strPkCuBasDoc)){
					//新加入一个成员
					Vector	voVEC = new	Vector() ;
					voVEC.addElement((VendEstVO)apartVEC.elementAt(0)) ;
					if(apartVEC.size()==2){
						voVEC.addElement((VendEstVO)apartVEC.elementAt(1)) ;
					}
					map.put(strPkCuBasDoc,voVEC) ;
				}else{
					//在原先的KEY中加一个成员
					Vector	existedVEC = (Vector)map.get(strPkCuBasDoc) ;
					existedVEC.addElement((VendEstVO)apartVEC.elementAt(0)) ;
					if(apartVEC.size()==2){
						existedVEC.addElement((VendEstVO)apartVEC.elementAt(1)) ;
					}
				}
			}
		}

		//关闭结果集,即时释放资源
		rs.close() ;

	} finally {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
		}
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
		}
	}
  
	return map;
}


public HashMap findVOsByCondsForVMICorp(
		String sCondition,
		UFDate	dBeginDate,
		UFDate	dEndDate,
	 	String corp)//edit by zwx 2015-3-9 
		throws SQLException {
				
		String sql = "select cvendorid, custname, cinventoryid, invcode, invname, cvmihid, A.noutnum-A.noutinnum, A.nmoney, A.dgaugedate, ";
		sql += "B.dsettledate, B.csettlebillid, C.csettlebill_bid, C.nsettlenum, C.ngaugemny, C.nmoney, invclassname, ";
		sql += "vinvoicecode, dinvoicedate, D.pk_cubasdoc ";
		sql += "from ic_vmi_sum A "; 
		sql += "left outer join po_settlebill_b C on A.cvmihid = C.cvmiid and C.dr = 0 ";
		sql += "left outer join po_settlebill B on B.csettlebillid = C.csettlebillid and A.cinventoryid = C.cmangid and B.dr = 0 ";
		sql += "left outer join po_invoice I on I.cinvoiceid = C.cinvoiceid and I.dr = 0 ";
		sql += "inner join bd_cumandoc D on cvendorid = D.pk_cumandoc ";
		sql += "inner join bd_cubasdoc E on D.pk_cubasdoc = E.pk_cubasdoc ";
		sql += "inner join bd_invmandoc F on cinventoryid = F.pk_invmandoc ";
		sql += "inner join bd_invbasdoc G on F.pk_invbasdoc = G.pk_invbasdoc ";
		sql += "inner join bd_invcl H on G.pk_invcl = H.pk_invcl ";
		sql += "where bgaugeflag = 'Y' and A.dr = 0 and custflag in ('1','3') ";
		//add by zwx 2015-3-9
		if(corp!=null&&corp.length()>0){
			sql += "and A.pk_corp='"+corp+"'";
		}
	    
		
		if(sCondition != null && sCondition.trim().length() > 0) sql += sCondition;
		
		sql += " order by cvendorid, cinventoryid, cvmihid, dgaugedate, dsettledate";
		
		CrossDBConnection con = null;
		Statement stmt = null;
		HashMap	map = new	HashMap() ;
		try {
			con = (CrossDBConnection)getConnection();
			con.enableSQLTranslator(false);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			String	strOldGeneralBid = "" ;
	     
	    String strPkCuBasDoc = null;
			while (rs.next()) {
				VendEstVO 	rsVO = new VendEstVO();

				//供应商ID
				String	str = rs.getString(1) ;
				rsVO.setCvendormangid( str==null? "":str ) ;
				//供应商NAME
				str = rs.getString(2) ;
				rsVO.setCvendorname( str==null? "":str ) ;
				//存货ID
				str = rs.getString(3) ; 
				rsVO.setCmangid( str==null? "":str ) ;
				//存货CODE
				str = rs.getString(4) ;
				rsVO.setCinvcode( str==null? "":str ) ;
				//存货NAME
				str = rs.getString(5) ;
				rsVO.setCinvname( str==null? "":str ) ;
				//入库单行ID
				str = (String) rs.getString(6);
				rsVO.setCgeneralbid((str==null||str.trim().equals("")) ? null:str.trim());
				//入库数量
				double dOb = rs.getDouble(7) ;
				rsVO.setNinnum( dOb==0.0? null : new	UFDouble(dOb) );
				//入库金额
				dOb = rs.getDouble(8) ;
				rsVO.setNinmny( dOb==0.0? null : new	UFDouble(dOb) );
				//入库日期
				String	dgeneraldate = (String) rs.getString(9);
				//结算日期
				String	dsettledate = (String) rs.getString(10);
				rsVO.setDsettledate(dsettledate==null ? null : new	UFDate(dsettledate)) ;
				//结算单ID
				String	csettlebillid = (String) rs.getString(11);
				rsVO.setCsettlebillid(csettlebillid) ;
				//结算单行ID
				String	csettlebill_bid = (String) rs.getString(12);
				rsVO.setCsettlebill_bid(csettlebill_bid) ;
				//结转数量
				dOb = rs.getDouble(13) ;
				rsVO.setNsumsettlenum( dOb==0.0? null : new	UFDouble(dOb) );
				//结转金额
				dOb = rs.getDouble(14) ;
				rsVO.setNsumgaugemny( dOb==0.0? null : new	UFDouble(dOb) );
				//结算金额
				dOb = rs.getDouble(15) ;
				rsVO.setNsumsettlemny( dOb==0.0? null : new	UFDouble(dOb) );
				//存货分类
				str = (String) rs.getString(16);
				rsVO.setCinvclassname((str==null||str.trim().equals("")) ? null:str.trim());
				//发票号	
				String	vinvoicecode = (String) rs.getString(17);
				//发票日期
				String	dinvoicedate = (String) rs.getString(18);
	      //供应商基本ID
	      strPkCuBasDoc = rs.getString(19);
				
				//一个入库单行对应几个结算单行，则拆分为1+n个行，第一行为入库单行，以后为结算单行
				Vector		apartVEC =	new	Vector() ;	
				//入库单行1--->结算单行1
				//入库单行1--->结算单行2
				if(!rsVO.getCgeneralbid().equals(strOldGeneralBid)){
					//入库
					VendEstVO	generalVO = (VendEstVO)rsVO.clone() ;
					generalVO.setCabstract("入库") ;
					generalVO.setNsumsettlenum(null) ;
					generalVO.setNsumgaugemny(null) ;
					generalVO.setNsumsettlemny(null) ;
					generalVO.setBilldate(dgeneraldate==null ? null : new	UFDate(dgeneraldate)) ;
					apartVEC.addElement(generalVO) ;
				}
				//结算
				if(rsVO.getCsettlebillid()!=null && !rsVO.getCsettlebillid().trim().equals("")){
					VendEstVO	settleVO = rsVO ;
					settleVO.setCabstract("结算") ;
					settleVO.setNinnum(null) ;
					settleVO.setNinmny(null) ;
					settleVO.setBillcode(vinvoicecode==null?null:vinvoicecode.trim()) ;
					settleVO.setBilldate(dinvoicedate==null ? null : new	UFDate(dinvoicedate)) ;
					apartVEC.addElement(settleVO) ;
				}
				strOldGeneralBid = rsVO.getCgeneralbid() ;

				if (apartVEC.size()>0) {
					if(!map.containsKey(strPkCuBasDoc)){
						//新加入一个成员
						Vector	voVEC = new	Vector() ;
						voVEC.addElement((VendEstVO)apartVEC.elementAt(0)) ;
						if(apartVEC.size()==2){
							voVEC.addElement((VendEstVO)apartVEC.elementAt(1)) ;
						}
						map.put(strPkCuBasDoc,voVEC) ;
					}else{
						//在原先的KEY中加一个成员
						Vector	existedVEC = (Vector)map.get(strPkCuBasDoc) ;
						existedVEC.addElement((VendEstVO)apartVEC.elementAt(0)) ;
						if(apartVEC.size()==2){
							existedVEC.addElement((VendEstVO)apartVEC.elementAt(1)) ;
						}
					}
				}
			}

			//关闭结果集,即时释放资源
			rs.close() ;

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	  
		return map;
	}

/**
* 作者：王印芬
* 功能：按条件进行查询得到供应商的入库数据等数据
* 参数：String strFromWhere		FROM、WHERE后的语句
*		UFDate	dBeginDate		查询条件的开始日期
*		UFDate	dEndDate		查询条件的结束日期
* 返回：无
* 例外：无
* 日期：(2002-3-13 11:39:21)
* 修改日期，修改人，修改原因，注释标志：
* 2002-06-24	WYF		如果对结算单作日期限制，则有一部分符合条件的入库单也会过滤掉
*					因此去掉结算单的条件限制，但在DMO中按日期过滤
*					会查询出符合条件的入库单，但结算单不一定合适，需要过滤
* 2002-07-08	WYF		修改一个存在暂估明细结果但未查出的问题
* 2002-08-02	WYF		修改一个数组越界错误，不存在发票的结算单亦列出
* 2007-09-29  CZP   支持分收集结后，要按供应商基本ID分组，按存货基本档案ID排序，否则界面展现时会出现重复的供应商及未组织到一块的存货
*/

public HashMap findVOsByCondsMy(
	String strFromWhere,
	UFDate	dBeginDate,
	UFDate	dEndDate)
	throws SQLException {
  
	//入库单
	//[供应商ID][存货ID][入库单行ID][入库数量][入库金额][入库单号][入库日期]	
	//按[供应商ID][存货ID][入库单行ID]排
		
	//SELECT
	String selectStr =
			"SELECT ic_general_h.cproviderid,bd_cubasdoc.custname,"
		+	"ic_general_b.cinventoryid,bd_invbasdoc.invcode,bd_invbasdoc.invname,"
		+	"ic_general_b.cgeneralhid,ic_general_b.cgeneralbid,"
		+	"ic_general_b.ninnum,ic_general_bb3.npmoney,"
		+	"ic_general_h.vbillcode,ic_general_h.dbilldate,"
		+	"po_settlebill.dsettledate,po_settlebill_b.csettlebillid,po_settlebill_b.csettlebill_bid,"
		+	"po_settlebill_b.nsettlenum,po_settlebill_b.ngaugemny,po_settlebill_b.nmoney,"
		+	"po_invoice.vinvoicecode,po_invoice.dinvoicedate,"
		+	"po_invoice_b.cinvoiceid,po_invoice_b.cinvoice_bid,bd_invcl.invclassname,bd_cubasdoc.pk_cubasdoc ";
	//ORDER BY
	String orderByStr = "ORDER BY bd_cubasdoc.pk_cubasdoc, bd_invbasdoc.invcode, ic_general_b.cgeneralbid,"
			+"ic_general_h.dbilldate,po_settlebill.dsettledate";
	//SQL
	String sql = selectStr + " " + strFromWhere + " " + orderByStr;
	SCMEnv.out(sql);
	Connection con = null;
	Statement stmt = null;
	HashMap	map = new	HashMap() ;
  String strPkCuBaseDoc = null;
	try {
		con = getConnection();
		stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		String	strOldGeneralBid = "" ;
		while (rs.next()) {
			VendEstVO 	rsVO = new VendEstVO();

			//供应商ID
			String	str = rs.getString(1) ;
			rsVO.setCvendormangid( str==null? "":str ) ;
			//供应商NAME
			str = rs.getString(2) ;
			rsVO.setCvendorname( str==null? "":str ) ;
			//存货ID
			str = rs.getString(3) ; 
			rsVO.setCmangid( str==null? "":str ) ;
			//存货CODE
			str = rs.getString(4) ;
			rsVO.setCinvcode( str==null? "":str ) ;
			//存货NAME
			str = rs.getString(5) ;
			rsVO.setCinvname( str==null? "":str ) ;
			//入库单ID
			str = (String) rs.getString("cgeneralhid");
			rsVO.setCgeneralhid((str==null||str.trim().equals("")) ? null:str.trim());
			//入库单行ID
			str = (String) rs.getString("cgeneralbid");
			rsVO.setCgeneralbid((str==null||str.trim().equals("")) ? null:str.trim());
			//入库数量
			double dOb = rs.getDouble("ninnum") ;
			rsVO.setNinnum( dOb==0.0? null : new	UFDouble(dOb) );
			//入库金额
			dOb = rs.getDouble("npmoney") ;
			rsVO.setNinmny( dOb==0.0? null : new	UFDouble(dOb) );
			//入库单号
			String	vgeneralcode = (String) rs.getString("vbillcode");
			//入库日期
			String	dgeneraldate = (String) rs.getString("dbilldate");
			//结算日期
			String	dsettledate = (String) rs.getString("dsettledate");
			rsVO.setDsettledate(dsettledate==null ? null : new	UFDate(dsettledate)) ;
			//结算单ID
			String	csettlebillid = (String) rs.getString("csettlebillid");
			rsVO.setCsettlebillid(csettlebillid) ;
			//结算单行ID
			String	csettlebill_bid = (String) rs.getString("csettlebill_bid");
			rsVO.setCsettlebill_bid(csettlebill_bid) ;
			//结转数量
			dOb = rs.getDouble("nsettlenum") ;
			rsVO.setNsumsettlenum( dOb==0.0? null : new	UFDouble(dOb) );
			//结转金额
			dOb = rs.getDouble("ngaugemny") ;
			rsVO.setNsumgaugemny( dOb==0.0? null : new	UFDouble(dOb) );
			//结算金额
			dOb = rs.getDouble("nmoney") ;
			rsVO.setNsumsettlemny( dOb==0.0? null : new	UFDouble(dOb) );
			//发票号	
			String	vinvoicecode = (String) rs.getString("vinvoicecode");
			//发票日期
			String	dinvoicedate = (String) rs.getString("dinvoicedate");
			//发票ID
			str = (String) rs.getString("cinvoiceid");
			rsVO.setCinvoiceid((str==null||str.trim().equals("")) ? null:str.trim());
			//发票行ID
			str = (String) rs.getString("cinvoice_bid");
			rsVO.setCinvoice_bid((str==null||str.trim().equals("")) ? null:str.trim());
			//存货分类
			str = (String) rs.getString("invclassname");
			rsVO.setCinvclassname((str==null||str.trim().equals("")) ? null:str.trim());
      //供应商基本ID
      strPkCuBaseDoc = rs.getString("pk_cubasdoc");
			
			//一个入库单行对应几个结算单行，则拆分为1+n个行，第一行为入库单行，以后为结算单行
			Vector		apartVEC =	new	Vector() ;	
			//入库单行1--->结算单行1
			//入库单行1--->结算单行2
			if(!rsVO.getCgeneralbid().equals(strOldGeneralBid)){
				//入库
				VendEstVO	generalVO = (VendEstVO)rsVO.clone() ;
				generalVO.setCabstract("入库") ;
				generalVO.setNsumsettlenum(null) ;
				generalVO.setNsumgaugemny(null) ;
				generalVO.setNsumsettlemny(null) ;
				generalVO.setBillcode(vgeneralcode==null ?null :vgeneralcode.trim()) ;
				generalVO.setBilldate(dgeneraldate==null ? null : new	UFDate(dgeneraldate)) ;
				apartVEC.addElement(generalVO) ;
			}
			//结算
			if(rsVO.getCsettlebillid()!=null && !rsVO.getCsettlebillid().trim().equals("")){
				//wyf	2002-08-02	modify		begin
				/*
				if (dinvoicedate!=null ) {
					UFDate	dDate = new	UFDate(dinvoicedate) ;
					if ( dEndDate!=null && dDate.after(dEndDate) ) {
						//滤掉日期在结束日期之后的行
					}else{
					*/
					VendEstVO	settleVO = rsVO ;
					settleVO.setCabstract("结算") ;
					settleVO.setNinnum(null) ;
					settleVO.setNinmny(null) ;
					settleVO.setBillcode(vinvoicecode==null?null:vinvoicecode.trim()) ;
					settleVO.setBilldate(dinvoicedate==null ? null : new	UFDate(dinvoicedate)) ;
					
					apartVEC.addElement(settleVO) ;
					//}
				//}
				//wyf	2002-08-02	modify		begin
			}
			strOldGeneralBid = rsVO.getCgeneralbid() ;

			//wyf	2002-08-02	add		begin
			if (apartVEC.size()>0) {
			//wyf	2002-08-02	add		end
				if(!map.containsKey(strPkCuBaseDoc)){
					//新加入一个成员
					Vector	voVEC = new	Vector() ;
					voVEC.addElement((VendEstVO)apartVEC.elementAt(0)) ;
					if(apartVEC.size()==2){
						voVEC.addElement((VendEstVO)apartVEC.elementAt(1)) ;
					}
					map.put(strPkCuBaseDoc,voVEC) ;
				}else{
					//在原先的KEY中加一个成员
					Vector	existedVEC = (Vector)map.get(strPkCuBaseDoc) ;
					existedVEC.addElement((VendEstVO)apartVEC.elementAt(0)) ;
					if(apartVEC.size()==2){
						existedVEC.addElement((VendEstVO)apartVEC.elementAt(1)) ;
					}
				}
			}
		}

		//关闭结果集,即时释放资源
		rs.close() ;

	} finally {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
		}
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
		}
	}
		
	return map;
}
}