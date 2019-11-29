package nc.impl.scm.so.pub;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.TempTableDMO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.impl.scm.so.so001.SaleOrderDMO;
import nc.impl.scm.so.so016.SOToolsDMO;
import nc.impl.so.sointerface.SaleToMRPDMO;
import nc.itf.arap.pub.IArapQuery4SailOrderPublic;
import nc.itf.ct.service.ICtToPo_BackToCt;
import nc.itf.ia.service.IIAToSO;
import nc.itf.ic.service.IIC215GeneralH;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ctpo.ParaPoToCtRewriteVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.pub.ProdNotInstallException;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.so022.SaleGrossprofitVO;
import nc.vo.so.so023.ParamVO;
import nc.vo.so.so023.ReturnVO;
import nc.vo.so.so041.SaleCostDetailVO;
import nc.vo.so.so054.CustDeliveryDetailMoneyBVO;

public class OtherInterfaceDMO extends DataManageObject
{
  private HashMap hsCorpAtpNum = null;
  private HashMap hsSo32inventory = null;

  public OtherInterfaceDMO()
    throws NamingException, SystemException
  {
  }

  public OtherInterfaceDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public void checkATP(SaleOrderVO ordvo)
    throws Exception
  {
    checkATP(ordvo, false);
  }

  public boolean checkCTisExist(String pk_corp)
  {
    try
    {
      String beanname = ICreateCorpQueryService.class.getName();
      ICreateCorpQueryService bs = (ICreateCorpQueryService)NCLocator.getInstance().lookup(beanname);
      return bs.isEnabled(pk_corp, "CT");
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
    }return false;
  }

  public CustDeliveryDetailMoneyBVO[] getArapDetailForSale(ParamVO vo)
    throws SQLException
  {
    Vector vResult = new Vector();

    String strSQL = "select zb.djrq,zb.djbh,balanname,fb.dfybje,zb.pj_num,bd_psndoc.psnname from arap_djzb zb left outer join arap_djfb fb on zb.vouchid=fb.vouchid  left outer  join bd_balatype on zb.pj_jsfs = bd_balatype.pk_balatype  left outer  join bd_psndoc on fb.ywybm = bd_psndoc.pk_psndoc where (zb.djdl = 'sk') AND (zb.dr = 0) AND (fb.dr = 0)  and ksbm_cl = ? and bzbm = ? and djrq <= ? and djrq >= ? and zb.dwbm = ?";

    if ((String)vo.getAttributeValue("cemployeeid") != null) {
      strSQL = strSQL + " and ywybm = '" + vo.getAttributeValue("cemployeeid") + "'";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, (String)vo.getAttributeValue("custid"));

      stmt.setString(2, (String)vo.getAttributeValue("currencyid"));

      stmt.setString(3, (String)vo.getAttributeValue("endDate"));

      stmt.setString(4, (String)vo.getAttributeValue("startDate"));

      stmt.setString(5, (String)vo.getAttributeValue("pk_corp"));

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        CustDeliveryDetailMoneyBVO bvo = new CustDeliveryDetailMoneyBVO();

        bvo.setAttributeValue("billdate", rs.getString(1));

        bvo.setAttributeValue("arapcode", rs.getString(2));

        bvo.setAttributeValue("squaremode", rs.getString(3));

        BigDecimal dfybje = rs.getBigDecimal(4);
        bvo.setAttributeValue("paymny", dfybje == null ? new UFDouble(0) : new UFDouble(dfybje));

        bvo.setAttributeValue("billcode", rs.getString(5));

        bvo.setAttributeValue("hemployeename", rs.getString(6));

        vResult.addElement(bvo);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    CustDeliveryDetailMoneyBVO[] s = null;

    if (vResult.size() != 0) {
      s = new CustDeliveryDetailMoneyBVO[vResult.size()];
      vResult.copyInto(s);
    }

    return s;
  }

  public UFDouble getArapForSale(String corpid, String ccustomerid, String cemployeeid, String ccurrencytypeid, String startdate, String enddate)
    throws SQLException
  {
    UFDouble nnumber = new UFDouble(0.0D);

    String strSQL = "select sum(fb.dfybje) from arap_djzb zb left outer join arap_djfb fb on zb.vouchid=fb.vouchid where (zb.djdl = 'sk') AND (zb.dr = 0) AND (fb.dr = 0)  and ksbm_cl = ? and bzbm = ? and djrq <= ? and zb.dwbm = ?";

    if (startdate != null) {
      strSQL = strSQL + " and djrq >= ?";
    }

    if (cemployeeid == null) {
      strSQL = strSQL + " and ywybm is null ";
    }
    else {
      strSQL = strSQL + " and ywybm = '" + cemployeeid + "'";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, ccustomerid);

      stmt.setString(2, ccurrencytypeid);

      stmt.setString(3, enddate);

      stmt.setString(4, corpid);

      if (startdate != null) {
        stmt.setString(5, startdate);
      }

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        BigDecimal n = rs.getBigDecimal(1);
        nnumber = n == null ? new UFDouble(0) : new UFDouble(n);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return nnumber;
  }

  public UFDouble getArapForSale2(String corpid, String cdeptid, String startdate, String enddate)
    throws SQLException
  {
    UFDouble nnumber = new UFDouble(0.0D);

    String strSQL = "select sum(fb.dfbbje) from arap_djzb zb left outer join arap_djfb fb on zb.vouchid=fb.vouchid where (zb.djdl = 'sk') AND (zb.dr = 0) AND (fb.dr = 0)  and zb.dwbm = ?";

    if (cdeptid != null) {
      strSQL = strSQL + " and fb.deptid = '" + cdeptid + "' ";
    }
    else {
      strSQL = strSQL + " and fb.deptid is null ";
    }

    if (enddate == null) {
      strSQL = strSQL + " and djrq = '" + startdate + "'";
    }
    else {
      strSQL = strSQL + " and djrq >= '" + startdate + "' and djrq <= '" + enddate + "'";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, corpid);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        BigDecimal n = rs.getBigDecimal(1);
        nnumber = n == null ? new UFDouble(0) : new UFDouble(n);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return nnumber;
  }

  public UFDouble getARByCondVO(ParamVO cvo)
    throws SQLException
  {
    boolean bUseInv = false;
    int iInvclLength = -1;

    Object oCorpID = cvo.getAttributeValue("pk_corp");
    if ((oCorpID == null) || (oCorpID.toString().trim().length() == 0)) {
      SQLException re = new SQLException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000088"));

      throw re;
    }

    Object oBeginDate = cvo.getAttributeValue("startDate");

    Object oEndDate = cvo.getAttributeValue("endDate");

    Object oInvID = cvo.getAttributeValue("pk_invmandoc");

    if ((oInvID != null) && (oInvID.toString().trim().length() != 0))
    {
      bUseInv = true;
    }

    Object oInvCLCode = cvo.getAttributeValue("invclcode");

    if ((!bUseInv) && (oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
    {
      iInvclLength = oInvCLCode.toString().trim().length();
    }

    Object oBatch = cvo.getAttributeValue("batchid");

    Object oDeptID = cvo.getAttributeValue("deptid");

    Object oEmployeeID = cvo.getAttributeValue("cemployeeid");

    Object oStorID = cvo.getAttributeValue("warehouseid");

    Object oCalbodyID = cvo.getAttributeValue("calbodyid");

    Object oBiztypeID = cvo.getAttributeValue("cbiztypeid");

    Object oCustomerID = cvo.getAttributeValue("custid");

    Object oVfree1 = cvo.getAttributeValue("vfree1");
    Object oVfree2 = cvo.getAttributeValue("vfree2");
    Object oVfree3 = cvo.getAttributeValue("vfree3");
    Object oVfree4 = cvo.getAttributeValue("vfree4");
    Object oVfree5 = cvo.getAttributeValue("vfree5");

    String sSQL = "select sum(bbye) ";
    sSQL = sSQL + " from ";

    sSQL = sSQL + " arap_djzb az ";
    sSQL = sSQL + " left join arap_djfb af ";
    sSQL = sSQL + " on ";
    sSQL = sSQL + " az.vouchid=af.vouchid ";
    sSQL = sSQL + " left join so_square_b so_b ";
    sSQL = sSQL + " on ";
    sSQL = sSQL + " so_b.corder_bid = af.ddhh ";
    sSQL = sSQL + " left join so_square b ";
    sSQL = sSQL + " on ";
    sSQL = sSQL + " b.csaleid = so_b.csaleid ";

    sSQL = sSQL + " where ";

    sSQL = sSQL + " (djdl='ys' or djdl='yf') and az.dr=0 and af.dr=0 ";
    sSQL = sSQL + " and az.dwbm = '" + oCorpID + "'";

    if ((oBeginDate != null) && (oEndDate != null)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " az.djrq >= '" + oBeginDate + "'";
      sSQL = sSQL + " and ";
      sSQL = sSQL + " az.djrq <= '" + oEndDate + "'";
    }

    if (bUseInv) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " af.cinventoryid = '" + oInvID + "'";
    }
    else if (iInvclLength != -1)
    {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " e.pk_invcl = m.pk_invcl ";
      sSQL = sSQL + " and ";
      sSQL = sSQL + " left(m.invclasscode," + iInvclLength + ") = '" + oInvCLCode + "'";
    }

    if ((oDeptID != null) && (oDeptID.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " af.deptid = '" + oDeptID + "'";
    }

    if ((oEmployeeID != null) && (oEmployeeID.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " af.ywybm = '" + oEmployeeID + "'";
    }

    if ((oStorID != null) && (oStorID.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " b.cwarehouseid = '" + oStorID + "'";
    }

    if ((oCalbodyID != null) && (oCalbodyID.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " b.ccalbodyid = '" + oCalbodyID + "'";
    }

    if ((oBatch != null) && (oBatch.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " so_b.cbatchid = '" + oBatch + "'";
    }

    if ((oBiztypeID != null) && (oBiztypeID.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " az.xslxbm = '" + oBiztypeID + "'";
    }

    if ((oCustomerID != null) && (oCustomerID.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " af.ksbm_cl = '" + oCustomerID + "'";
    }

    if ((oVfree1 != null) && (oVfree1.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " so_b.vfree1 = '" + oVfree1 + "'";
    }
    if ((oVfree2 != null) && (oVfree2.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " so_b.vfree2 = '" + oVfree2 + "'";
    }
    if ((oVfree3 != null) && (oVfree3.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " so_b.vfree3 = '" + oVfree3 + "'";
    }
    if ((oVfree4 != null) && (oVfree4.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " so_b.vfree4 = '" + oVfree4 + "'";
    }
    if ((oVfree5 != null) && (oVfree5.toString().trim().length() != 0)) {
      sSQL = sSQL + " and ";
      sSQL = sSQL + " so_b.vfree5 = '" + oVfree5 + "'";
    }

    Connection con = null;
    PreparedStatement stmt = null;

    UFDouble result = new UFDouble(0);
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSQL);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        BigDecimal n = rs.getBigDecimal(1);
        result = n == null ? new UFDouble(0) : new UFDouble(n);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return result;
  }

  public UFDouble getARByCustAndDept(String strCorpID, String strCustomerID, String strDeptID, String enddate)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.pub.CreditControlDMO", "getCustFinanceAR", new Object[] { strCustomerID });

    String sql = "select sum(jfbbje) from arap_djzb left outer join arap_djfb on arap_djfb.vouchid = arap_djzb.vouchid where (djdl='ys' ) and arap_djzb.dr=0 and arap_djfb.dr=0 and arap_djfb.wldx = 0  and arap_djzb.qcbz = 'Y' and pzglh = 0 and  arap_djzb.dwbm =? and arap_djfb.deptid = ? ";

    if (strCustomerID != null) {
      sql = sql + " and arap_djfb.ksbm_cl = ? ";
    }

    Connection con = null;
    PreparedStatement stmt = null;

    UFDouble result = new UFDouble(0);
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, strCorpID);

      stmt.setString(2, strDeptID);

      if (strCustomerID != null) {
        stmt.setString(3, strCustomerID);
      }

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        BigDecimal n = rs.getBigDecimal(1);
        result = n == null ? new UFDouble(0) : new UFDouble(n);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.so.pub.CreditControlDMO", "getCustFinanceAR", new Object[] { strCustomerID });

    return result;
  }

  private String getBizTypeID(String sCorpID, String sBizType)
    throws Exception
  {
    String sBizTypeID = "";
    String sBizName = "";
    if (sBizType.equalsIgnoreCase("FQSK")) {
      sBizName = "F";
    }
    else if (sBizType.equalsIgnoreCase("WTDX")) {
      sBizName = "W";
    }
    else if (sBizType.equalsIgnoreCase("ZY")) {
      sBizName = "Z";
    }
    else {
      return null;
    }

    String sSQL = " select ";
    sSQL = sSQL + " pk_busitype ";
    sSQL = sSQL + " from ";
    sSQL = sSQL + " bd_busitype ";
    sSQL = sSQL + " where ";
    sSQL = sSQL + " (pk_corp = '" + sCorpID + "' or pk_corp = '" + "0001" + "')";

    sSQL = sSQL + " and ";
    sSQL = sSQL + " verifyrule = '" + sBizName + "'";

    String[][] sResult = queryInData(sSQL);
    if (sResult.length != 0) {
      for (int i = 0; i < sResult.length; i++) {
        if (i != 0) {
          sBizTypeID = sBizTypeID + ",";
        }
        String[] sTemp = sResult[i];

        if (sTemp.length != 0) {
          sBizTypeID = sBizTypeID + "'" + sTemp[0] + "'";
        }
      }
    }

    if (sBizTypeID.trim().length() == 0) {
      sBizTypeID = "''";
    }

    return sBizTypeID;
  }

  private Hashtable getBusiReceive(String[] ccustomerids)
    throws SQLException
  {
    String where = getCustCond("ccustomerid", ccustomerids);
    Hashtable hNmny = null;

    String strSQL = "SELECT ccustomerid,sum(mny) FROM (SELECT ccustomerid,ISNULL(so_saleexecute.ntotalinventorynumber, 0) * ISNULL(so_saleorder_b.ntaxnetprice, 0) - ISNULL(so_saleexecute.ntotalpaymny, 0) - ISNULL((SELECT SUM(arap_djfb.jfshl * so_b.ntaxnetprice - arap_djfb.jfbbje)  FROM arap_djzb LEFT OUTER JOIN arap_djfb ON arap_djfb.vouchid = arap_djzb.vouchid LEFT OUTER JOIN \tso_saleorder_b so_b ON arap_djfb.cksqsh = so_b.corder_bid  WHERE (arap_djzb.dr = 0) AND (arap_djfb.dr = 0) AND (arap_djzb.djzt = 2) and arap_djzb.lybz = 3 and frowstatus = 2 and  arap_djfb.cksqsh  =  so_saleorder_b.CORDER_BID),0) as mny FROM so_saleorder_b INNER JOIN so_sale ON so_saleorder_b.csaleid = so_sale.csaleid LEFT OUTER JOIN so_saleexecute ON so_saleorder_b.corder_bid = so_saleexecute.csale_bid WHERE so_saleorder_b.beditflag = 'N' and so_saleexecute.ntotalinventorynumber <> 0 and frowstatus != 5 and frowstatus != 6 and frowstatus != 3 and " + where + ") so" + " group by ccustomerid";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        if (hNmny == null) {
          hNmny = new Hashtable();
        }

        String ccustomerid = rs.getString(1);

        BigDecimal n = rs.getBigDecimal(2);
        UFDouble nmny = n == null ? new UFDouble(0) : new UFDouble(n);

        hNmny.put(ccustomerid, nmny);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return hNmny;
  }

  private String getCustCond(String fieldname, String[] sCust)
  {
    if ((sCust == null) || (sCust.length == 0)) {
      SCMEnv.out("no data");
      return null;
    }
    StringBuffer sbufKey = new StringBuffer("(");
    for (int i = 0; i < sCust.length; i++) {
      sbufKey.append(fieldname + " = '" + sCust[i] + "'");
      if (i != sCust.length - 1) {
        sbufKey.append("or ");
      }
      else {
        sbufKey.append(") ");
      }
    }

    return sbufKey.toString();
  }

  private Hashtable getFactoryid(String strID)
    throws SQLException
  {
    String cfactoryid = null;
    String corder_bid = null;

    String strSQL = "Select corder_bid,cfactoryid From so_saleorder_b where csaleid =  ?";

    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable hfactoryid = new Hashtable();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, strID);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        corder_bid = rs.getString(1);

        cfactoryid = rs.getString(2);

        if ((cfactoryid != null) && (!cfactoryid.equals("")))
          hfactoryid.put(corder_bid, cfactoryid);
      }
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return hfactoryid;
  }

  private Hashtable getFinanceBackMoney(String[] ccustomerids, boolean bIsPre)
    throws SQLException
  {
    String where = getCustCond("ksbm_cl", ccustomerids);
    Hashtable hNmny = null;

    String strSQL = "select ksbm_cl,sum(bbye) from arap_djzb left outer join arap_djfb on arap_djfb.vouchid = arap_djzb.vouchid where djdl='sk' and arap_djzb.dr=0 and arap_djfb.dr=0 and arap_djzb.djzt = 2";

    if (bIsPre) {
      strSQL = strSQL + " and prepay = 'Y'";
    }
    else {
      strSQL = strSQL + " and prepay = 'N'";
    }

    strSQL = strSQL + " and " + where + " group by ksbm_cl";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        if (hNmny == null) {
          hNmny = new Hashtable();
        }

        String ccustomerid = rs.getString(1);

        BigDecimal n = rs.getBigDecimal(2);
        UFDouble nmny = n == null ? new UFDouble(0) : new UFDouble(n);

        hNmny.put(ccustomerid, nmny);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return hNmny;
  }

  public SaleCostDetailVO[] getIACostDetailForSale(ParamVO cvo)
    throws RemoteException, BusinessException
  {
    SaleCostDetailVO[] scVOs = null;
    Vector vscVOs = new Vector();
    try
    {
      boolean bUseInv = false;
      int iInvclLength = -1;

      Object oCorpID = cvo.getAttributeValue("pk_corp");
      if ((oCorpID == null) || (oCorpID.toString().trim().length() == 0)) {
        BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000088"));

        throw re;
      }

      Object oBeginDate = cvo.getAttributeValue("startDate");

      Object oEndDate = cvo.getAttributeValue("endDate");

      Object oAuditBeginDate = cvo.getAttributeValue("accountStartDate");

      Object oAuditEndDate = cvo.getAttributeValue("accountEndDate");

      Object oInvID = cvo.getAttributeValue("pk_invmandoc");
      if ((oInvID != null) && (oInvID.toString().trim().length() != 0))
      {
        bUseInv = true;
      }
      Object oInvCLCode = cvo.getAttributeValue("invclcode");
      if ((!bUseInv) && (oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
      {
        iInvclLength = oInvCLCode.toString().trim().length();
      }

      Object oBatch = cvo.getAttributeValue("batchid");

      Object oDeptID = cvo.getAttributeValue("deptid");

      Object oEmployeeID = cvo.getAttributeValue("cemployeeid");

      Object oStorID = cvo.getAttributeValue("warehouseid");

      Object oCalbodyID = cvo.getAttributeValue("calbodyid");

      Object oBiztypeID = cvo.getAttributeValue("cbiztypeid");

      Object oCustomerID = cvo.getAttributeValue("custid");

      Object oVfree1 = cvo.getAttributeValue("vfree1");
      Object oVfree2 = cvo.getAttributeValue("vfree2");
      Object oVfree3 = cvo.getAttributeValue("vfree3");
      Object oVfree4 = cvo.getAttributeValue("vfree4");
      Object oVfree5 = cvo.getAttributeValue("vfree5");

      StringBuffer sSQL = new StringBuffer();

      sSQL.append(" select ");

      sSQL.append("  a.dbilldate,b.dbilldate,n.billtypename,b.vreceiptcode,c.storname,d.bodyname,");

      sSQL.append(" a.cinventoryid,e.invcode,e.invname,e.invspec,e.invtype,g.measname,h.deptname,i.businame,j.custname,l.psnname,");

      sSQL.append(" a.vfree1,a.vfree2,a.vfree3,a.vfree4,a.vfree5,a.nnumber,");
      sSQL.append("coalesce( ");
      sSQL.append(" case when a.nprice is null then ");

      sSQL.append(" case when a.fpricemodeflag = 5 AND a.cauditorid IS NOT NULL then a.nplanedprice ");
      sSQL.append(" end ");
      sSQL.append(" else ");
      sSQL.append(" a.nprice ");
      sSQL.append("  end,0),");
      sSQL.append("coalesce( ");
      sSQL.append(" case when a.nmoney is null then ");
      sSQL.append(" case when a.fpricemodeflag = 5 AND a.cauditorid IS NOT NULL then a.nplanedmny else ");
      sSQL.append(" case when a.fpricemodeflag = 4 AND a.cauditorid IS NOT NULL then a.nsimulatemny ");
      sSQL.append(" end ");
      sSQL.append(" end ");
      sSQL.append(" else ");
      sSQL.append(" a.nmoney ");
      sSQL.append("  end,0)");
      if (cvo.getRelateToArap().booleanValue()) {
        sSQL.append(" ,af.wbfbbje ");
      }

      sSQL.append(" ,a.vbatch ");
      sSQL.append(" from v_ia_inoutledger a  ");
      sSQL.append(" inner join  bd_calbody d  on a.crdcenterid = d.pk_calbody ");

      sSQL.append(" inner join  bd_invmandoc f on a.cinventoryid = f.pk_invmandoc  ");

      sSQL.append(" left outer join  bd_invbasdoc e on  f.pk_invbasdoc = e.pk_invbasdoc  ");

      sSQL.append(" left outer join bd_invcl on bd_invcl.pk_invcl = e.pk_invcl  ");

      sSQL.append(" left outer join bd_measdoc g  on  e.pk_measdoc = g.pk_measdoc ");

      sSQL.append(" inner join bd_deptdoc h on a.cdeptid = h.pk_deptdoc  ");
      sSQL.append(" inner join  bd_busitype i  on a.cbiztypeid = i.pk_busitype  ");

      sSQL.append(" inner join bd_cumandoc k on a.ccustomvendorid = k.pk_cumandoc ");

      sSQL.append(" left outer join bd_cubasdoc j on  k.pk_cubasdoc = j.pk_cubasdoc ");

      sSQL.append(" left outer join bd_areacl on bd_areacl.pk_areacl = j.pk_areacl  ");

      if (!cvo.getRelateToArap().booleanValue())
      {
        sSQL.append(" inner join so_square_b so_b on so_b.corder_bid = a.csourcebillitemid ");

        sSQL.append(" inner join so_square b on b.csaleid = so_b.csaleid ");
      }
      else
      {
        sSQL.append(" inner join so_square_b so_b on so_b.corder_bid = a.csourcebillitemid ");

        sSQL.append(" inner join so_square b on b.csaleid = so_b.csaleid ");
        sSQL.append(" left join arap_djfb af on so_b.corder_bid = af.ddhh ");
        sSQL.append(" left join arap_djzb az on az.vouchid=af.vouchid ");
      }
      sSQL.append(" left outer join bd_billtype n on   b.creceipttype = n.pk_billtypecode ");

      sSQL.append(" left outer join bd_psndoc l on a.cemployeeid = l.pk_psndoc left outer join bd_stordoc c on a.cwarehouseid = c.pk_stordoc ");

      sSQL.append(" where ");

      sSQL.append(" a.crdcenterid = d.pk_calbody ");
      sSQL.append(" and ");
      sSQL.append(" a.cinventoryid = f.pk_invmandoc ");
      sSQL.append(" and ");
      sSQL.append(" f.pk_invbasdoc = e.pk_invbasdoc ");
      sSQL.append(" and ");
      sSQL.append(" e.pk_measdoc = g.pk_measdoc ");
      sSQL.append(" and ");
      sSQL.append(" a.cdeptid = h.pk_deptdoc ");
      sSQL.append(" and ");
      sSQL.append(" a.cbiztypeid = i.pk_busitype ");
      sSQL.append(" and ");
      sSQL.append(" a.ccustomvendorid = k.pk_cumandoc ");
      sSQL.append(" and ");
      sSQL.append(" k.pk_cubasdoc = j.pk_cubasdoc ");
      sSQL.append(" and ");
      sSQL.append(" b.creceipttype = n.pk_billtypecode ");

      sSQL.append(" and ");
      sSQL.append(" a.cbilltypecode = 'I5'");

      if ((cvo.getAttributeValue("whereSQL") == null) || (cvo.getAttributeValue("whereSQL").toString().equals("")))
      {
        if ((oDeptID != null) && (oDeptID.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.cdeptid " + cvo.getOP("deptid") + "'" + oDeptID + "'");
        }

        if ((oEmployeeID != null) && (oEmployeeID.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.cemployeeid " + cvo.getOP("cemployeeid") + "'" + oEmployeeID + "'");
        }

        if ((oStorID != null) && (oStorID.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.cwarehouseid " + cvo.getOP("warehouseid") + "'" + oStorID + "'");
        }

        if ((oCalbodyID != null) && (oCalbodyID.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.crdcenterid " + cvo.getOP("batchid") + "'" + oCalbodyID + "'");
        }

        if ((oBatch != null) && (oBatch.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.vbatch " + cvo.getOP("batchid") + "'" + oBatch + "'");
        }

        if ((oBiztypeID != null) && (oBiztypeID.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.cbiztypeid " + cvo.getOP("cbiztypeid") + "'" + oBiztypeID + "'");
        }

        if ((oCustomerID != null) && (oCustomerID.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.ccustomvendorid " + cvo.getOP("custid") + "'" + oCustomerID + "'");
        }

        if ((oVfree1 != null) && (oVfree1.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.vfree1 " + cvo.getOP("vfree1") + "'" + oVfree1 + "'");
        }

        if ((oVfree2 != null) && (oVfree2.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.vfree2 " + cvo.getOP("vfree2") + "'" + oVfree2 + "'");
        }

        if ((oVfree3 != null) && (oVfree3.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.vfree3 " + cvo.getOP("vfree3") + "'" + oVfree3 + "'");
        }

        if ((oVfree4 != null) && (oVfree4.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.vfree4 " + cvo.getOP("vfree4") + "'" + oVfree4 + "'");
        }

        if ((oVfree5 != null) && (oVfree5.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.vfree5 " + cvo.getOP("vfree5") + "'" + oVfree5 + "'");
        }

        Object oCustomerCode = cvo.getAttributeValue("custcode");
        if ((oCustomerCode != null) && (oCustomerCode.toString().trim().length() != 0))
        {
          sSQL.append(" and ");
          sSQL.append(" a.ccustomvendorid " + cvo.getOP("custcode") + oCustomerCode);
        }

        Object oCustomerName = cvo.getAttributeValue("custname");
        if ((oCustomerName != null) && (oCustomerName.toString().trim().length() != 0))
        {
          sSQL.append(" and ");
          sSQL.append(" a.ccustomvendorid " + cvo.getOP("custname") + oCustomerName);
        }

        Object oCustomerArea = cvo.getAttributeValue("areaclname");
        if ((oCustomerArea != null) && (oCustomerArea.toString().trim().length() != 0))
        {
          sSQL.append(" and ");
          sSQL.append(" a.ccustomvendorid " + cvo.getOP("areaclname") + oCustomerArea);
        }

        Object oCustomerDef1 = cvo.getAttributeValue("def1");
        if ((oCustomerDef1 != null) && (oCustomerDef1.toString().trim().length() != 0))
        {
          sSQL.append(" and ");
          sSQL.append(" a.ccustomvendorid " + cvo.getOP("def1") + oCustomerDef1);
        }

        Object oCustomerDef2 = cvo.getAttributeValue("def2");
        if ((oCustomerDef2 != null) && (oCustomerDef2.toString().trim().length() != 0))
        {
          sSQL.append(" and ");
          sSQL.append(" a.ccustomvendorid " + cvo.getOP("def2") + oCustomerDef2);
        }

        Object oCustomerDef3 = cvo.getAttributeValue("def3");
        if ((oCustomerDef3 != null) && (oCustomerDef3.toString().trim().length() != 0))
        {
          sSQL.append(" and ");
          sSQL.append(" a.ccustomvendorid " + cvo.getOP("def3") + oCustomerDef3);
        }

        Object oCustomerDef4 = cvo.getAttributeValue("def4");
        if ((oCustomerDef4 != null) && (oCustomerDef4.toString().trim().length() != 0))
        {
          sSQL.append(" and ");
          sSQL.append(" a.ccustomvendorid " + cvo.getOP("def4") + oCustomerDef4);
        }

        Object oCustomerDef5 = cvo.getAttributeValue("def5");
        if ((oCustomerDef5 != null) && (oCustomerDef5.toString().trim().length() != 0))
        {
          sSQL.append(" and ");
          sSQL.append(" a.ccustomvendorid " + cvo.getOP("def5") + oCustomerDef5);
        }

        Object oInvmancode = cvo.getAttributeValue("invmancode");
        if ((oInvmancode != null) && (oInvmancode.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.cinventoryid " + cvo.getOP("invmancode") + oInvmancode);
        }

        Object oInvname = cvo.getAttributeValue("invname");
        if ((oInvname != null) && (oInvname.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.cinventoryid " + cvo.getOP("invname") + oInvname);
        }

        Object oInvclcode = cvo.getAttributeValue("invclcode");
        if ((oInvclcode != null) && (oInvclcode.toString().trim().length() != 0)) {
          sSQL.append(" and ");
          sSQL.append(" a.cinventoryid " + cvo.getOP("invclcode") + oInvclcode);
        }
      }
      else
      {
        sSQL.append(cvo.getAttributeValue("whereSQL").toString());
      }

      if (cvo.getAttributeValue("csourcebilltypecode").equals("32")) {
        sSQL.append(" and a.csourcebilltypecode ='32' ");
      }
      else if (cvo.getAttributeValue("csourcebilltypecode").equals("4C")) {
        String sFQSK = getBizTypeID(oCorpID.toString(), "FQSK");
        String sWTDX = getBizTypeID(oCorpID.toString(), "WTDX");
        sSQL.append(" and a.csourcebilltypecode='4C' and a.cbiztypeid not in(" + sFQSK + ") and a.cbiztypeid not in (" + sWTDX + ")");
      }
      else if (cvo.getAttributeValue("csourcebilltypecode").equals("IJ")) {
        sSQL.append(" and  a.cbilltypecode ='IJ' and a.cfirstbilltypecode in('5D','5C') ");
      }
      else
      {
        String sFQSK = getBizTypeID(oCorpID.toString(), "FQSK");
        String sWTDX = getBizTypeID(oCorpID.toString(), "WTDX");

        sSQL.append(" and ");
        sSQL.append(" ( ");
        sSQL.append(" not (");
        sSQL.append(" (");
        sSQL.append(" a.cbiztypeid in (" + sFQSK + ") ");
        sSQL.append(" or ");
        sSQL.append(" a.cbiztypeid in (" + sWTDX + ")");
        sSQL.append(")");
        sSQL.append(" and ");
        sSQL.append(" a.csourcebilltypecode ='4C'");
        sSQL.append(")");
        sSQL.append(" or ");
        sSQL.append(" a.cbiztypeid is null ");
        sSQL.append(" or ");
        sSQL.append(" a.csourcebilltypecode is null) ");
      }

      sSQL.append(" order by ");
      sSQL.append(" a.dauditdate,a.dbilldate,vreceiptcode ");
      String[][] sResult = queryInData(sSQL.toString());

      for (int i = 0; i < sResult.length; i++) {
        String[] sTemp = sResult[i];
        SaleCostDetailVO scVO = new SaleCostDetailVO();

        scVO.setAttributeValue("accountdate", sTemp[0]);
        scVO.setAttributeValue("billdate", sTemp[1]);
        scVO.setAttributeValue("billtype", sTemp[2]);
        scVO.setAttributeValue("billcode", sTemp[3]);
        scVO.setAttributeValue("warehousename", sTemp[4]);
        scVO.setAttributeValue("calbodyname", sTemp[5]);
        scVO.setAttributeValue("cinventoryid", sTemp[6]);
        scVO.setAttributeValue("invcode", sTemp[7]);
        scVO.setAttributeValue("invname", sTemp[8]);
        scVO.setAttributeValue("invspec", sTemp[9]);
        scVO.setAttributeValue("invtype", sTemp[10]);
        scVO.setAttributeValue("unitname", sTemp[11]);
        scVO.setAttributeValue("deptname", sTemp[12]);
        scVO.setAttributeValue("cbiztype", sTemp[13]);
        scVO.setAttributeValue("custname", sTemp[14]);
        scVO.setAttributeValue("employee", sTemp[15]);
        int iFreeBegin = 16;
        scVO.setAttributeValue("vfree1", sTemp[iFreeBegin]);
        scVO.setAttributeValue("vfree2", sTemp[(iFreeBegin + 1)]);
        scVO.setAttributeValue("vfree3", sTemp[(iFreeBegin + 2)]);
        scVO.setAttributeValue("vfree4", sTemp[(iFreeBegin + 3)]);
        scVO.setAttributeValue("vfree5", sTemp[(iFreeBegin + 4)]);

        int iNumberBegin = iFreeBegin + 5;
        if ((sTemp[iNumberBegin] != null) && (sTemp[iNumberBegin].trim().length() != 0))
        {
          scVO.setAttributeValue("number", new UFDouble(sTemp[iNumberBegin]));
        }
        if ((sTemp[(iNumberBegin + 1)] != null) && (sTemp[(iNumberBegin + 1)].trim().length() != 0))
        {
          scVO.setAttributeValue("costprice", new UFDouble(sTemp[(iNumberBegin + 1)]));
        }

        if ((sTemp[(iNumberBegin + 2)] != null) && (sTemp[(iNumberBegin + 2)].trim().length() != 0))
        {
          scVO.setAttributeValue("costmny", new UFDouble(sTemp[(iNumberBegin + 2)]));
        }

        if ((cvo.getRelateToArap().booleanValue()) && 
          (sTemp[(iNumberBegin + 3)] != null) && (sTemp[(iNumberBegin + 3)].trim().length() != 0))
        {
          scVO.setAttributeValue("cursomoney", new UFDouble(sTemp[(iNumberBegin + 3)]));
        }

        if ((sTemp[24] != null) && (sTemp[24].trim().length() != 0)) {
          scVO.setAttributeValue("vbatch", sTemp[24]);
        }
        vscVOs.addElement(scVO);
      }
      if (vscVOs.size() != 0) {
        scVOs = new SaleCostDetailVO[vscVOs.size()];
        vscVOs.copyInto(scVOs);
      }

    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
    return scVOs;
  }

  public ReturnVO[] getIACostForSale(ParamVO cvo)
    throws RemoteException, BusinessException, Exception, SQLException
  {
    ReturnVO[] sgVOs = null;
    Vector vsgVOs = new Vector();

    boolean bUseInv = false;
    boolean bUseInvClass = false;
    int iInvclLength = -1;

    Object oCorpID = cvo.getAttributeValue("pk_corp");
    if ((oCorpID == null) || (oCorpID.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000088"));

      throw re;
    }

    Object oBeginDate = cvo.getAttributeValue("startDate");
    if ((oBeginDate == null) || (oBeginDate.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000090"));

      throw re;
    }

    Object oEndDate = cvo.getAttributeValue("endDate");
    if ((oEndDate == null) || (oEndDate.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000091"));

      throw re;
    }

    Object oInvID = cvo.getAttributeValue("pk_invmandoc");
    Object oInvCode = cvo.getAttributeValue("invmancode");
    Object oInvName = cvo.getAttributeValue("invname");
    Object oMeasName = cvo.getAttributeValue("unitname");

    Object oInvLife = cvo.getAttributeValue("invlifeperiod");
    if ((oInvID != null) && (oInvID.toString().trim().length() != 0))
    {
      bUseInv = true;
    }
    Object oInvCLCode = cvo.getAttributeValue("invclcode");
    Object oInvCLID = cvo.getAttributeValue("invclname");

    Object oBatch = cvo.getAttributeValue("batchid");

    Object oDeptID = cvo.getAttributeValue("deptid");
    Object oDeptName = cvo.getAttributeValue("deptname");

    Object oEmployeeID = cvo.getAttributeValue("cemployeeid");
    Object oEmployeeName = cvo.getAttributeValue("cemployeename");

    Object oStorID = cvo.getAttributeValue("warehouseid");
    Object oStorName = cvo.getAttributeValue("warehousename");

    Object oCalbodyID = cvo.getAttributeValue("calbodyid");
    Object oCalbodyName = cvo.getAttributeValue("calbodyname");

    Object oCurID = cvo.getAttributeValue("custid");
    Object oCurName = cvo.getAttributeValue("custname");

    Object oBusiID = cvo.getAttributeValue("cbiztypeid");
    Object oBusiName = cvo.getAttributeValue("cbiztypename");

    Object oVfree1 = cvo.getAttributeValue("vfree1");
    Object oVfree2 = cvo.getAttributeValue("vfree2");
    Object oVfree3 = cvo.getAttributeValue("vfree3");
    Object oVfree4 = cvo.getAttributeValue("vfree4");
    Object oVfree5 = cvo.getAttributeValue("vfree5");

    Object oCurrencyID = cvo.getAttributeValue("currencyid");
    Object oCurrencyName = cvo.getAttributeValue("currencyname");

    BusinessCurrencyRateUtil dmo = new BusinessCurrencyRateUtil(oCorpID.toString());

    String sCurID = dmo.getLocalCurrPK();
    String sCurName = "";
    if (sCurID != null) {
      String sSQL = " select ";
      sSQL = sSQL + " currtypename ";
      sSQL = sSQL + " from ";
      sSQL = sSQL + " bd_currtype ";
      sSQL = sSQL + " where ";
      sSQL = sSQL + " pk_currtype = '" + sCurID + "'";
      String[][] sResult = queryInData(sSQL);
      if ((sResult != null) && (sResult.length != 0) && 
        (sResult[0] != null) && (sResult[0].length != 0)) {
        sCurName = sResult[0][0];
      }
    }

    if ((oCurrencyID != null) && (!oCurrencyID.toString().trim().equals(sCurID)))
    {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000092"));

      throw re;
    }

    Object oSalestruID = cvo.getAttributeValue("salestruid");
    Object oSalestruName = cvo.getAttributeValue("salestruname");

    Object oGroup = cvo.getAttributeValue("groupConditons");
    if (oGroup == null) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000093"));

      throw re;
    }
    String[] sGroups = (String[])(String[])oGroup;
    if (sGroups.length == 0) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000093"));

      throw re;
    }
    String sFieldString = "";
    String sOuterFieldString = "";
    String sOuterFieldString2 = "";
    String sGruopByFieldString = "";
    Vector vAttrs = new Vector();
    Vector vOuterFields = new Vector();
    String[] sAttributes = null;
    String sTableString = "";
    String sConnectionString = " ";

    boolean isCust = false;
    boolean isArea = false;
    for (int i = 0; i < sGroups.length; i++) {
      if (sGroups[i].equals("存货")) {
        vOuterFields.addElement("a.invcode");
        vOuterFields.addElement("a.invname");
        vOuterFields.addElement("a.invspec || a.invtype inv");
        vOuterFields.addElement("d.measname");
        vOuterFields.addElement("v.cinventoryid");
        vAttrs.addElement("invcode");
        vAttrs.addElement("invname");
        vAttrs.addElement("invmodel");
        vAttrs.addElement("unitname");
        vAttrs.addElement("pk_invmandoc");
        sTableString = sTableString + " inner join bd_invmandoc b ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cinventoryid = b.pk_invmandoc ";
        sTableString = sTableString + " inner join bd_invbasdoc a ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " b.pk_invbasdoc = a.pk_invbasdoc ";
        sTableString = sTableString + " inner join bd_measdoc d ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " a.pk_measdoc = d.pk_measdoc ";
        if ((cvo.getFreeItemGroupParam() != null) && (cvo.getFreeItemGroupParam().booleanValue()))
        {
          vOuterFields.addElement("v.vfree1");
          vOuterFields.addElement("v.vfree2");
          vOuterFields.addElement("v.vfree3");
          vOuterFields.addElement("v.vfree4");
          vOuterFields.addElement("v.vfree5");
          vAttrs.addElement("vfree1");
          vAttrs.addElement("vfree2");
          vAttrs.addElement("vfree3");
          vAttrs.addElement("vfree4");
          vAttrs.addElement("vfree5");
        }
        bUseInvClass = true;
      }
      else if (sGroups[i].equals("部门")) {
        vOuterFields.addElement("e.deptname");
        vOuterFields.addElement("v.cdeptid");
        vAttrs.addElement("deptname");
        vAttrs.addElement("deptid");
        sTableString = sTableString + " left outer join bd_deptdoc e";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cdeptid = e.pk_deptdoc ";
      }
      else if (sGroups[i].equals("库存组织")) {
        vOuterFields.addElement("v.crdcenterid");
        vAttrs.addElement("calbodyid");
        sTableString = sTableString + " left outer join bd_calbody m";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.crdcenterid = m.pk_calbody ";
      }
      else if (sGroups[i].equals("仓库")) {
        vOuterFields.addElement("f.storname");
        vOuterFields.addElement("v.cwarehouseid");
        vAttrs.addElement("warehousename");
        vAttrs.addElement("warehouseid");
        sTableString = sTableString + " left outer join bd_stordoc f";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cwarehouseid = f.pk_stordoc ";
      }
      else if (sGroups[i].equals("批次")) {
        vOuterFields.addElement("v.batch");
        vAttrs.addElement("batchname");
      }
      else if (sGroups[i].equals("业务员"))
      {
        vOuterFields.addElement("bd_psndoc.psnname");

        vAttrs.addElement("employee");
        sTableString = sTableString + " LEFT OUTER JOIN bd_psndoc  ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cemployeeid = bd_psndoc.pk_psndoc ";
      }
      else if (sGroups[i].equals("客户")) {
        vOuterFields.addElement("g.custcode");
        vOuterFields.addElement("g.custname");
        vAttrs.addElement("custcode");
        vAttrs.addElement("custname");
        if (!isArea) {
          sTableString = sTableString + " left join bd_cumandoc h ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " v.ccustomvendorid = h.pk_cumandoc ";
          sTableString = sTableString + " left join bd_cubasdoc g ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " h.pk_cubasdoc = g.pk_cubasdoc ";
        }
        isCust = true;
      }
      else if (sGroups[i].equals("地区")) {
        vOuterFields.addElement("i.areaclname");
        vAttrs.addElement("areaname");
        if (!isCust) {
          sTableString = sTableString + " left join bd_cumandoc h ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " v.ccustomvendorid = h.pk_cumandoc ";
          sTableString = sTableString + " left join bd_cubasdoc g ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " h.pk_cubasdoc = g.pk_cubasdoc ";
        }
        sTableString = sTableString + " left join bd_areacl i ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " g.pk_areacl=i.pk_areacl ";
        isArea = true;
      }
    }
    for (int i = 0; i < vOuterFields.size(); i++) {
      sOuterFieldString = sOuterFieldString + vOuterFields.elementAt(i) + ",";
      if (((String)vOuterFields.elementAt(i)).startsWith("a.invspec || a.invtype"))
      {
        sOuterFieldString2 = sOuterFieldString2 + "inv" + ",";
        sGruopByFieldString = sGruopByFieldString + " a.invspec || a.invtype " + ",";
      }
      else
      {
        sGruopByFieldString = sGruopByFieldString + vOuterFields.elementAt(i) + ",";

        sOuterFieldString2 = sOuterFieldString2 + ((String)vOuterFields.elementAt(i)).substring(2) + ",";
      }

    }

    sAttributes = new String[vAttrs.size()];
    vAttrs.copyInto(sAttributes);
    if (sConnectionString.trim().length() != 0)
    {
      sConnectionString = " where " + sConnectionString.substring(5);
    }

    String sSQL = " select ";
    String sSQL1 = "";
    if (cvo.getRelateToArap().booleanValue()) {
      sSQL1 = sSQL + sOuterFieldString + " 0 outnum,0 outmny,0 salemny ";
      sSQL = sSQL + sOuterFieldString2 + " sum(outnum) outnum,sum(outmny) outmny,sum(salemny) salemny, sum(nsummny) nsummny from (select ";
    }

    sSQL = sSQL + sOuterFieldString + " sum(coalesce(case when fdispatchflag=1 then nnumber end,0)) outnum,";

    sSQL = sSQL + " sum(coalesce(case when fdispatchflag=1 then ";
    sSQL = sSQL + " case when nmoney is null then ";

    sSQL = sSQL + " isnull(nsimulatemny,isnull(nplanedmny,0)) ";

    sSQL = sSQL + " else ";
    sSQL = sSQL + " nmoney ";
    sSQL = sSQL + " end ";
    sSQL = sSQL + " end,0)) outmny, ";

    sSQL = sSQL + " sum(coalesce(case when fdispatchflag=1 then nnumber*isnull(so_b.ntaxnetprice,0) end,0)) salemny";

    if (cvo.getRelateToArap().booleanValue()) {
      sSQL = sSQL + ",0.0000 nsummny ";
      sSQL1 = sSQL1 + ",isnull(af.wbfbbje,0) nsummny ";
    }

    String sFrom = " from ";

    sFrom = sFrom + " v_ia_inoutledger v ";
    sFrom = sFrom + " left join so_square_b so_b ";
    sFrom = sFrom + " on ";
    sFrom = sFrom + " so_b.corder_bid = v.csourcebillitemid ";
    sFrom = sFrom + " left join so_square so ";
    sFrom = sFrom + " on ";
    sFrom = sFrom + " so.csaleid = so_b.csaleid ";

    StringBuffer sWhere = new StringBuffer();
    sWhere.append(" where ");
    sWhere.append(" v.pk_corp = '" + oCorpID + "'");
    sWhere.append(" and ");
    sWhere.append(" v.dbilldate >= '" + oBeginDate + "'");
    sWhere.append(" and ");
    sWhere.append(" v.dbilldate <= '" + oEndDate + "'");

    sWhere.append(" and ");
    sWhere.append(" v.cbilltypecode in ('I2','I9','I5','IA')");

    if ((cvo.getAttributeValue("whereSQL") == null) || (cvo.getAttributeValue("whereSQL").toString().equals("")))
    {
      if (bUseInv) {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid = '" + oInvID + "'");
      }
      else if ((oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid " + cvo.getOP("invclcode") + oInvCLCode);
      }
      else if ((oInvLife != null) && (oInvLife.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid = b.pk_invmandoc ");
        sWhere.append(" and ");
        sWhere.append(" a.pk_invbasdoc = b.pk_invbasdoc ");
        sWhere.append(" and ");
        sWhere.append(" b.invlifeperiod = '" + oInvLife + "'");
      }

      if ((oCurID != null) && (oCurID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid = '" + oCurID + "'");
      }

      if ((oDeptID != null) && (oDeptID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cdeptid = '" + oDeptID + "'");
      }

      if ((oEmployeeID != null) && (oEmployeeID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cemployeeid = '" + oEmployeeID + "'");
      }

      if ((oStorID != null) && (oStorID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cwarehouseid = '" + oStorID + "'");
      }

      if ((oBusiID != null) && (oBusiID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cbiztypeid = '" + oBusiID + "'");
      }

      if ((oCalbodyID != null) && (oCalbodyID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.crdcenterid = '" + oCalbodyID + "'");
      }

      if ((oBatch != null) && (oBatch.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vbatch = '" + oBatch + "'");
      }

      if ((oVfree1 != null) && (oVfree1.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree1 = '" + oVfree1 + "'");
      }
      if ((oVfree2 != null) && (oVfree2.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree2 = '" + oVfree2 + "'");
      }
      if ((oVfree3 != null) && (oVfree3.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree3 = '" + oVfree3 + "'");
      }
      if ((oVfree4 != null) && (oVfree4.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree4 = '" + oVfree4 + "'");
      }
      if ((oVfree5 != null) && (oVfree5.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree5 = '" + oVfree5 + "'");
      }

      Object oCustomerCode = cvo.getAttributeValue("custcode");
      if ((oCustomerCode != null) && (oCustomerCode.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("custcode") + oCustomerCode);
      }

      Object oCustomerName = cvo.getAttributeValue("custname");
      if ((oCustomerName != null) && (oCustomerName.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("custname") + oCustomerName);
      }

      Object oCustomerArea = cvo.getAttributeValue("areaclname");
      if ((oCustomerArea != null) && (oCustomerArea.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("areaclname") + oCustomerArea);
      }

      Object oCustomerDef1 = cvo.getAttributeValue("def1");
      if ((oCustomerDef1 != null) && (oCustomerDef1.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def1") + oCustomerDef1);
      }

      Object oCustomerDef2 = cvo.getAttributeValue("def2");
      if ((oCustomerDef2 != null) && (oCustomerDef2.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def2") + oCustomerDef2);
      }

      Object oCustomerDef3 = cvo.getAttributeValue("def3");
      if ((oCustomerDef3 != null) && (oCustomerDef3.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def3") + oCustomerDef3);
      }

      Object oCustomerDef4 = cvo.getAttributeValue("def4");
      if ((oCustomerDef4 != null) && (oCustomerDef4.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def4") + oCustomerDef4);
      }

      Object oCustomerDef5 = cvo.getAttributeValue("def5");
      if ((oCustomerDef5 != null) && (oCustomerDef5.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def5") + oCustomerDef5);
      }

      Object oInvmancode = cvo.getAttributeValue("invmancode");
      if ((oInvmancode != null) && (oInvmancode.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid " + cvo.getOP("invmancode") + oInvmancode);
      }

      Object oInvname = cvo.getAttributeValue("invname");
      if ((oInvname != null) && (oInvname.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid " + cvo.getOP("invname") + oInvname);
      }
    }
    else {
      sWhere.append(cvo.getAttributeValue("whereSQL").toString());
    }

    String sFQSK = getBizTypeID(oCorpID.toString(), "FQSK");
    String sWTDX = getBizTypeID(oCorpID.toString(), "WTDX");

    sWhere.append(" and ");
    sWhere.append(" ( ");
    sWhere.append(" not (");
    sWhere.append(" (");
    sWhere.append(" v.cbiztypeid in (" + sFQSK + ") ");
    sWhere.append(" or ");
    sWhere.append(" v.cbiztypeid in (" + sWTDX + ")");
    sWhere.append(")");
    sWhere.append(" and ");
    sWhere.append(" v.csourcebilltypecode ='4C'");
    sWhere.append(")");
    sWhere.append(" or ");
    sWhere.append(" v.cbiztypeid is null ");
    sWhere.append(" or ");
    sWhere.append(" v.csourcebilltypecode is null ");
    sWhere.append(" ) ");
    sFrom = sFrom + sTableString;
    if ((!bUseInv) && (oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
    {
      if (!bUseInvClass)
      {
        sFrom = sFrom + " inner join bd_invmandoc b ";
        sFrom = sFrom + " on ";
        sFrom = sFrom + " v.cinventoryid = b.pk_invmandoc ";
        sFrom = sFrom + " inner join bd_invbasdoc a ";
        sFrom = sFrom + " on ";
        sFrom = sFrom + " b.pk_invbasdoc = a.pk_invbasdoc ";
        sFrom = sFrom + " left join bd_invcl c ";
        sFrom = sFrom + " on ";
        sFrom = sFrom + " a.pk_invcl = c.pk_invcl ";
      }
    }
    sSQL = sSQL + sFrom + sConnectionString;
    sSQL = sSQL + sWhere.toString();

    if (cvo.getRelateToArap().booleanValue()) {
      sFrom = sFrom + " left join arap_djfb af ";
      sFrom = sFrom + " on ";
      sFrom = sFrom + " v.csourcebillitemid = af.ddhh ";
      sSQL1 = sSQL1 + sFrom;
      sSQL1 = sSQL1 + sWhere.toString() + " and af.dr = 0 ";
    }

    sOuterFieldString = sOuterFieldString.substring(0, sOuterFieldString.length() - 1);

    sOuterFieldString2 = sOuterFieldString2.substring(0, sOuterFieldString2.length() - 1);

    sGruopByFieldString = sGruopByFieldString.substring(0, sGruopByFieldString.length() - 1);

    if (cvo.getRelateToArap().booleanValue()) {
      sSQL = sSQL + " group by v.csourcebillitemid," + sGruopByFieldString;
      sSQL = sSQL + " union all (" + sSQL1 + ")) t";
      sSQL = sSQL + " group by " + sOuterFieldString2;
    }
    else {
      sSQL = sSQL + " group by " + sGruopByFieldString;
    }
    Vector vOrderByField = new Vector();
    if ((cvo.getOrderName() != null) && (cvo.getOrderName().length > 0)) {
      String sOrderByField = " outnum desc ";
      String[] sOrder = cvo.getOrderName();
      String desc = " ";
      if ((cvo.getOrderFlag() != null) && (!cvo.getOrderFlag().booleanValue())) {
        desc = " desc ";
      }
      for (int i = 0; i < sOrder.length; i++) {
        String sOrderBy = null;
        if (sOrder[i].equals("销售数量"))
        {
          sOrderBy = " outnum " + desc;
        }
        else if (sOrder[i].equals("销售金额"))
        {
          sOrderBy = " salemny " + desc;
        }
        if (i == 0) {
          sOrderByField = sOrderBy;
        }
        else {
          sOrderByField = sOrderByField + "," + sOrderBy;
        }
      }
      sSQL = sSQL + " order by " + sOrderByField;
    }
    String[][] sResult = queryInData(sSQL);

    int iFieldLength = sAttributes.length;
    for (int i = 0; i < sResult.length; i++) {
      String[] sTemp = sResult[i];
      ReturnVO sgVO = new ReturnVO();

      sgVO.setAttributeValue("deptid", oDeptID);
      sgVO.setAttributeValue("deptname", oDeptName);
      sgVO.setAttributeValue("warehouseid", oStorID);
      sgVO.setAttributeValue("warehousename", oStorName);
      sgVO.setAttributeValue("pk_invmandoc", oInvID);
      sgVO.setAttributeValue("invcode", oInvCode);
      sgVO.setAttributeValue("invname", oInvName);
      sgVO.setAttributeValue("batchname", oBatch);
      sgVO.setAttributeValue("unitname", oMeasName);
      sgVO.setAttributeValue("employee", oEmployeeName);
      sgVO.setAttributeValue("custname", oCurName);

      for (int j = 0; j < iFieldLength; j++) {
        sgVO.setAttributeValue(sAttributes[j], sTemp[j]);
      }

      sgVO.setAttributeValue("cursonumber", new UFDouble(sTemp[iFieldLength]));

      sgVO.setAttributeValue("cursocost", new UFDouble(sTemp[(iFieldLength + 1)]));

      sgVO.setAttributeValue("cursalemoney", new UFDouble(sTemp[(iFieldLength + 2)]));

      if (cvo.getRelateToArap().booleanValue()) {
        sgVO.setAttributeValue("cursomoney", new UFDouble(sTemp[(iFieldLength + 3)]));
      }

      vsgVOs.addElement(sgVO);
    }
    if (vsgVOs.size() != 0) {
      sgVOs = new ReturnVO[vsgVOs.size()];
      vsgVOs.copyInto(sgVOs);
    }
    return sgVOs;
  }

  public ReturnVO[] getIACostForSale2(ParamVO cvo)
    throws RemoteException, BusinessException, Exception, SQLException
  {
    ReturnVO[] sgVOs = null;
    Vector vsgVOs = new Vector();

    boolean bUseInv = false;
    boolean bUseInvClass = false;
    int iInvclLength = -1;

    Object oCorpID = cvo.getAttributeValue("pk_corp");
    if ((oCorpID == null) || (oCorpID.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000088"));

      throw re;
    }

    Object oBeginDate = cvo.getAttributeValue("startDate");
    if ((oBeginDate == null) || (oBeginDate.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000090"));

      throw re;
    }

    Object oEndDate = cvo.getAttributeValue("endDate");
    if ((oEndDate == null) || (oEndDate.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000091"));

      throw re;
    }

    Object oInvID = cvo.getAttributeValue("pk_invmandoc");
    Object oInvCode = cvo.getAttributeValue("invmancode");
    Object oInvName = cvo.getAttributeValue("invname");
    Object oMeasName = cvo.getAttributeValue("unitname");

    Object oInvLife = cvo.getAttributeValue("invlifeperiod");
    if ((oInvID != null) && (oInvID.toString().trim().length() != 0))
    {
      bUseInv = true;
    }
    Object oInvCLCode = cvo.getAttributeValue("invclcode");
    Object oInvCLID = cvo.getAttributeValue("invclname");

    Object oBatch = cvo.getAttributeValue("batchid");

    Object oDeptID = cvo.getAttributeValue("deptid");
    Object oDeptName = cvo.getAttributeValue("deptname");

    Object oEmployeeID = cvo.getAttributeValue("cemployeeid");
    Object oEmployeeName = cvo.getAttributeValue("cemployeename");

    Object oStorID = cvo.getAttributeValue("warehouseid");
    Object oStorName = cvo.getAttributeValue("warehousename");

    Object oCalbodyID = cvo.getAttributeValue("calbodyid");
    Object oCalbodyName = cvo.getAttributeValue("calbodyname");

    Object oCurID = cvo.getAttributeValue("custid");
    Object oCurName = cvo.getAttributeValue("custname");

    Object oBusiID = cvo.getAttributeValue("cbiztypeid");
    Object oBusiName = cvo.getAttributeValue("cbiztypename");

    Object oVfree1 = cvo.getAttributeValue("vfree1");
    Object oVfree2 = cvo.getAttributeValue("vfree2");
    Object oVfree3 = cvo.getAttributeValue("vfree3");
    Object oVfree4 = cvo.getAttributeValue("vfree4");
    Object oVfree5 = cvo.getAttributeValue("vfree5");

    Object oCurrencyID = cvo.getAttributeValue("currencyid");
    Object oCurrencyName = cvo.getAttributeValue("currencyname");

    BusinessCurrencyRateUtil dmo = new BusinessCurrencyRateUtil(oCorpID.toString());

    String sCurID = dmo.getLocalCurrPK();
    String sCurName = "";
    if (sCurID != null) {
      String sSQL = " select ";
      sSQL = sSQL + " currtypename ";
      sSQL = sSQL + " from ";
      sSQL = sSQL + " bd_currtype ";
      sSQL = sSQL + " where ";
      sSQL = sSQL + " pk_currtype = '" + sCurID + "'";
      String[][] sResult = queryInData(sSQL);
      if ((sResult != null) && (sResult.length != 0) && 
        (sResult[0] != null) && (sResult[0].length != 0)) {
        sCurName = sResult[0][0];
      }
    }

    if ((oCurrencyID != null) && (!oCurrencyID.toString().trim().equals(sCurID)))
    {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000092"));

      throw re;
    }

    Object oSalestruID = cvo.getAttributeValue("salestruid");
    Object oSalestruName = cvo.getAttributeValue("salestruname");

    Object oGroup = cvo.getAttributeValue("groupConditons");
    if (oGroup == null) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000093"));

      throw re;
    }
    String[] sGroups = (String[])(String[])oGroup;
    if (sGroups.length == 0) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000093"));

      throw re;
    }
    String sFieldString = "";
    String sOuterFieldString = "";
    String sOuterFieldString2 = "";
    String sGruopByFieldString = "";
    Vector vAttrs = new Vector();
    Vector vOuterFields = new Vector();
    String[] sAttributes = null;
    String sTableString = "";
    String sConnectionString = " ";

    boolean isCust = false;
    boolean isArea = false;
    for (int i = 0; i < sGroups.length; i++) {
      if (sGroups[i].equals("存货")) {
        vOuterFields.addElement("a.invcode");
        vOuterFields.addElement("a.invname");
        vOuterFields.addElement("a.invspec || a.invtype inv");
        vOuterFields.addElement("d.measname");
        vOuterFields.addElement("v.cinventoryid");
        vAttrs.addElement("invcode");
        vAttrs.addElement("invname");
        vAttrs.addElement("invmodel");
        vAttrs.addElement("unitname");
        vAttrs.addElement("pk_invmandoc");
        sTableString = sTableString + " inner join bd_invmandoc b ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cinventoryid = b.pk_invmandoc ";
        sTableString = sTableString + " inner join bd_invbasdoc a ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " b.pk_invbasdoc = a.pk_invbasdoc ";
        sTableString = sTableString + " inner join bd_measdoc d ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " a.pk_measdoc = d.pk_measdoc ";
        if ((cvo.getFreeItemGroupParam() != null) && (cvo.getFreeItemGroupParam().booleanValue()))
        {
          vOuterFields.addElement("v.vfree1");
          vOuterFields.addElement("v.vfree2");
          vOuterFields.addElement("v.vfree3");
          vOuterFields.addElement("v.vfree4");
          vOuterFields.addElement("v.vfree5");
          vAttrs.addElement("vfree1");
          vAttrs.addElement("vfree2");
          vAttrs.addElement("vfree3");
          vAttrs.addElement("vfree4");
          vAttrs.addElement("vfree5");
        }
        bUseInvClass = true;
      }
      else if (sGroups[i].equals("部门")) {
        vOuterFields.addElement("e.deptname");
        vOuterFields.addElement("v.cdeptid");
        vAttrs.addElement("deptname");
        vAttrs.addElement("deptid");
        sTableString = sTableString + " left outer join bd_deptdoc e";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cdeptid = e.pk_deptdoc ";
      }
      else if (sGroups[i].equals("库存组织")) {
        vOuterFields.addElement("v.crdcenterid");
        vAttrs.addElement("calbodyid");
        sTableString = sTableString + " left outer join bd_calbody m";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.crdcenterid = m.pk_calbody ";
      }
      else if (sGroups[i].equals("仓库")) {
        vOuterFields.addElement("f.storname");
        vOuterFields.addElement("v.cwarehouseid");
        vAttrs.addElement("warehousename");
        vAttrs.addElement("warehouseid");
        sTableString = sTableString + " left outer join bd_stordoc f";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cwarehouseid = f.pk_stordoc ";
      }
      else if (sGroups[i].equals("批次")) {
        vOuterFields.addElement("v.batch");
        vAttrs.addElement("batchname");
      }
      else if (sGroups[i].equals("业务员"))
      {
        vOuterFields.addElement("bd_psndoc.psnname");

        vAttrs.addElement("employee");
        sTableString = sTableString + " LEFT OUTER JOIN bd_psndoc  ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cemployeeid = bd_psndoc.pk_psndoc ";
      }
      else if (sGroups[i].equals("客户")) {
        vOuterFields.addElement("g.custcode");
        vOuterFields.addElement("g.custname");
        vAttrs.addElement("custcode");
        vAttrs.addElement("custname");
        if (!isArea) {
          sTableString = sTableString + " left join bd_cumandoc h ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " v.ccustomvendorid = h.pk_cumandoc ";
          sTableString = sTableString + " left join bd_cubasdoc g ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " h.pk_cubasdoc = g.pk_cubasdoc ";
        }
        isCust = true;
      }
      else if (sGroups[i].equals("地区")) {
        vOuterFields.addElement("i.areaclname");
        vAttrs.addElement("areaname");
        if (!isCust) {
          sTableString = sTableString + " left join bd_cumandoc h ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " v.ccustomvendorid = h.pk_cumandoc ";
          sTableString = sTableString + " left join bd_cubasdoc g ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " h.pk_cubasdoc = g.pk_cubasdoc ";
        }
        sTableString = sTableString + " left join bd_areacl i ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " g.pk_areacl=i.pk_areacl ";
        isArea = true;
      }
    }
    for (int i = 0; i < vOuterFields.size(); i++) {
      sOuterFieldString = sOuterFieldString + vOuterFields.elementAt(i) + ",";
      if (((String)vOuterFields.elementAt(i)).startsWith("a.invspec || a.invtype"))
      {
        sOuterFieldString2 = sOuterFieldString2 + "inv" + ",";
        sGruopByFieldString = sGruopByFieldString + " a.invspec || a.invtype " + ",";
      }
      else
      {
        sGruopByFieldString = sGruopByFieldString + vOuterFields.elementAt(i) + ",";

        sOuterFieldString2 = sOuterFieldString2 + ((String)vOuterFields.elementAt(i)).substring(2) + ",";
      }

    }

    sAttributes = new String[vAttrs.size()];
    vAttrs.copyInto(sAttributes);
    if (sConnectionString.trim().length() != 0)
    {
      sConnectionString = " where " + sConnectionString.substring(5);
    }

    String sSQL = " select ";

    sSQL = sSQL + sOuterFieldString + " sum(coalesce(case when fdispatchflag=1 then nnumber end,0)) outnum,";

    sSQL = sSQL + " sum(coalesce(case when fdispatchflag=1 then ";
    sSQL = sSQL + " case when nmoney is null then ";
    sSQL = sSQL + " case when fpricemodeflag =5 then nplanedmny else ";
    sSQL = sSQL + " case when fpricemodeflag =4 then nsimulatemny ";
    sSQL = sSQL + " end ";
    sSQL = sSQL + " end ";
    sSQL = sSQL + " else ";
    sSQL = sSQL + " nmoney ";
    sSQL = sSQL + " end ";
    sSQL = sSQL + " end,0)) outmny, ";

    sSQL = sSQL + " sum(coalesce(case when fdispatchflag=1 then nnumber*isnull(so_b.ntaxnetprice,0) end,0)) salemny";

    if (cvo.getRelateToArap().booleanValue()) {
      sSQL = sSQL + ", sum(isnull(af.wbfbbje,0)) nsummny ";
    }

    String sFrom = " from ";

    sFrom = sFrom + " v_ia_inoutledger v ";
    sFrom = sFrom + " left join so_square_b so_b ";
    sFrom = sFrom + " on ";
    sFrom = sFrom + " so_b.corder_bid = v.csourcebillitemid ";
    sFrom = sFrom + " left join so_square so ";
    sFrom = sFrom + " on ";
    sFrom = sFrom + " so.csaleid = so_b.csaleid ";

    StringBuffer sWhere = new StringBuffer();
    sWhere.append(" where ");
    sWhere.append(" v.pk_corp = '" + oCorpID + "'");
    sWhere.append(" and ");
    sWhere.append(" v.dbilldate >= '" + oBeginDate + "'");
    sWhere.append(" and ");
    sWhere.append(" v.dbilldate <= '" + oEndDate + "'");
    sWhere.append(" and ");
    sWhere.append(" v.cauditorid is not null ");

    sWhere.append(" and so_b.blargessflag = 'N'");

    sWhere.append(" and ");
    sWhere.append(" v.cbilltypecode in ('I2','I9','I5','IA')");

    if ((cvo.getAttributeValue("whereSQL") != null) && (!cvo.getAttributeValue("whereSQL").toString().equals("")))
    {
      sWhere.append(cvo.getAttributeValue("whereSQL").toString());
    }
    else
    {
      if (bUseInv) {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid = '" + oInvID + "'");
      }
      else if ((oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid " + cvo.getOP("invclcode") + "'" + oInvCLCode + "'");
      }
      else if ((oInvLife != null) && (oInvLife.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid = b.pk_invmandoc ");
        sWhere.append(" and ");
        sWhere.append(" a.pk_invbasdoc = b.pk_invbasdoc ");
        sWhere.append(" and ");
        sWhere.append(" b.invlifeperiod = '" + oInvLife + "'");
      }

      if ((oCurID != null) && (oCurID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid = '" + oCurID + "'");
      }

      if ((oDeptID != null) && (oDeptID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cdeptid = '" + oDeptID + "'");
      }

      if ((oEmployeeID != null) && (oEmployeeID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cemployeeid = '" + oEmployeeID + "'");
      }

      if ((oStorID != null) && (oStorID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cwarehouseid = '" + oStorID + "'");
      }

      if ((oBusiID != null) && (oBusiID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cbiztypeid = '" + oBusiID + "'");
      }

      if ((oCalbodyID != null) && (oCalbodyID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.crdcenterid = '" + oCalbodyID + "'");
      }

      if ((oBatch != null) && (oBatch.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vbatch = '" + oBatch + "'");
      }

      if ((oVfree1 != null) && (oVfree1.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree1 = '" + oVfree1 + "'");
      }
      if ((oVfree2 != null) && (oVfree2.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree2 = '" + oVfree2 + "'");
      }
      if ((oVfree3 != null) && (oVfree3.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree3 = '" + oVfree3 + "'");
      }
      if ((oVfree4 != null) && (oVfree4.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree4 = '" + oVfree4 + "'");
      }
      if ((oVfree5 != null) && (oVfree5.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree5 = '" + oVfree5 + "'");
      }

      Object oCustomerCode = cvo.getAttributeValue("custcode");
      if ((oCustomerCode != null) && (oCustomerCode.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("custcode") + oCustomerCode);
      }

      Object oCustomerName = cvo.getAttributeValue("custname");
      if ((oCustomerName != null) && (oCustomerName.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("custname") + oCustomerName);
      }

      Object oCustomerArea = cvo.getAttributeValue("areaclname");
      if ((oCustomerArea != null) && (oCustomerArea.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("areaclname") + oCustomerArea);
      }

      Object oCustomerDef1 = cvo.getAttributeValue("def1");
      if ((oCustomerDef1 != null) && (oCustomerDef1.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def1") + oCustomerDef1);
      }

      Object oCustomerDef2 = cvo.getAttributeValue("def2");
      if ((oCustomerDef2 != null) && (oCustomerDef2.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def2") + oCustomerDef2);
      }

      Object oCustomerDef3 = cvo.getAttributeValue("def3");
      if ((oCustomerDef3 != null) && (oCustomerDef3.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def3") + oCustomerDef3);
      }

      Object oCustomerDef4 = cvo.getAttributeValue("def4");
      if ((oCustomerDef4 != null) && (oCustomerDef4.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def4") + oCustomerDef4);
      }

      Object oCustomerDef5 = cvo.getAttributeValue("def5");
      if ((oCustomerDef5 != null) && (oCustomerDef5.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def5") + oCustomerDef5);
      }

      Object oInvmancode = cvo.getAttributeValue("invmancode");
      if ((oInvmancode != null) && (oInvmancode.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid " + cvo.getOP("invmancode") + oInvmancode);
      }

      Object oInvname = cvo.getAttributeValue("invname");
      if ((oInvname != null) && (oInvname.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid " + cvo.getOP("invname") + oInvname);
      }

    }

    String sFQSK = getBizTypeID(oCorpID.toString(), "FQSK");
    String sWTDX = getBizTypeID(oCorpID.toString(), "WTDX");

    sWhere.append(" and ");
    sWhere.append(" ( ");
    sWhere.append(" not (");
    sWhere.append(" (");
    sWhere.append(" v.cbiztypeid in (" + sFQSK + ") ");
    sWhere.append(" or ");
    sWhere.append(" v.cbiztypeid in (" + sWTDX + ")");
    sWhere.append(")");
    sWhere.append(" and ");
    sWhere.append(" v.csourcebilltypecode ='4C'");
    sWhere.append(")");
    sWhere.append(" or ");
    sWhere.append(" v.cbiztypeid is null ");
    sWhere.append(" or ");
    sWhere.append(" v.csourcebilltypecode is null ");
    sWhere.append(" ) ");
    sFrom = sFrom + sTableString;
    if ((!bUseInv) && (oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
    {
      if (!bUseInvClass)
      {
        sFrom = sFrom + " inner join bd_invmandoc b ";
        sFrom = sFrom + " on ";
        sFrom = sFrom + " v.cinventoryid = b.pk_invmandoc ";
        sFrom = sFrom + " inner join bd_invbasdoc a ";
        sFrom = sFrom + " on ";
        sFrom = sFrom + " b.pk_invbasdoc = a.pk_invbasdoc ";
        sFrom = sFrom + " left join bd_invcl c ";
        sFrom = sFrom + " on ";
        sFrom = sFrom + " a.pk_invcl = c.pk_invcl ";
      }

    }

    if (cvo.getRelateToArap().booleanValue()) {
      sFrom = sFrom + " left join arap_djfb af ";
      sFrom = sFrom + " on ";
      sFrom = sFrom + " v.csourcebillitemid = af.ddhh ";
      sWhere.append(" and af.dr = 0 ");
    }

    sSQL = sSQL + sFrom + sConnectionString;
    sSQL = sSQL + sWhere.toString();

    sOuterFieldString = sOuterFieldString.substring(0, sOuterFieldString.length() - 1);

    sOuterFieldString2 = sOuterFieldString2.substring(0, sOuterFieldString2.length() - 1);

    sGruopByFieldString = sGruopByFieldString.substring(0, sGruopByFieldString.length() - 1);

    if (cvo.getRelateToArap().booleanValue())
    {
      sSQL = sSQL + " group by " + sGruopByFieldString;
    }
    else
    {
      sSQL = sSQL + " group by " + sGruopByFieldString;
    }

    sSQL = sSQL + " ORDER BY outnum DESC";
    String[][] sResult = queryInData(sSQL);

    int iFieldLength = sAttributes.length;
    for (int i = 0; i < sResult.length; i++) {
      String[] sTemp = sResult[i];
      ReturnVO sgVO = new ReturnVO();

      sgVO.setAttributeValue("deptid", oDeptID);
      sgVO.setAttributeValue("deptname", oDeptName);
      sgVO.setAttributeValue("warehouseid", oStorID);
      sgVO.setAttributeValue("warehousename", oStorName);
      sgVO.setAttributeValue("pk_invmandoc", oInvID);
      sgVO.setAttributeValue("invcode", oInvCode);
      sgVO.setAttributeValue("invname", oInvName);
      sgVO.setAttributeValue("batchname", oBatch);
      sgVO.setAttributeValue("unitname", oMeasName);
      sgVO.setAttributeValue("employee", oEmployeeName);
      sgVO.setAttributeValue("custname", oCurName);

      for (int j = 0; j < iFieldLength; j++) {
        sgVO.setAttributeValue(sAttributes[j], sTemp[j]);
      }

      sgVO.setAttributeValue("cursonumber", new UFDouble(sTemp[iFieldLength]));

      sgVO.setAttributeValue("cursocost", new UFDouble(sTemp[(iFieldLength + 1)]));

      sgVO.setAttributeValue("cursalemoney", new UFDouble(sTemp[(iFieldLength + 2)]));

      if (cvo.getRelateToArap().booleanValue()) {
        sgVO.setAttributeValue("cursomoney", new UFDouble(sTemp[(iFieldLength + 3)]));
      }

      vsgVOs.addElement(sgVO);
    }
    if (vsgVOs.size() != 0) {
      sgVOs = new ReturnVO[vsgVOs.size()];
      vsgVOs.copyInto(sgVOs);
    }
    return sgVOs;
  }

  public ReturnVO[] getIACostForSale3(ParamVO cvo)
    throws RemoteException, BusinessException, Exception, SQLException
  {
    ReturnVO[] sgVOs = null;
    Vector vsgVOs = new Vector();

    boolean bUseInv = false;
    boolean bUseInvClass = false;
    int iInvclLength = -1;

    Object oCorpID = cvo.getAttributeValue("pk_corp");
    if ((oCorpID == null) || (oCorpID.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000088"));

      throw re;
    }

    Object oBeginDate = cvo.getAttributeValue("startDate");
    if ((oBeginDate == null) || (oBeginDate.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000090"));

      throw re;
    }

    Object oEndDate = cvo.getAttributeValue("endDate");
    if ((oEndDate == null) || (oEndDate.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000091"));

      throw re;
    }

    Object oInvID = cvo.getAttributeValue("pk_invmandoc");
    Object oInvCode = cvo.getAttributeValue("invmancode");
    Object oInvName = cvo.getAttributeValue("invname");
    Object oMeasName = cvo.getAttributeValue("unitname");

    Object oInvLife = cvo.getAttributeValue("invlifeperiod");
    if ((oInvID != null) && (oInvID.toString().trim().length() != 0))
    {
      bUseInv = true;
    }
    Object oInvCLCode = cvo.getAttributeValue("invclcode");
    Object oInvCLID = cvo.getAttributeValue("invclname");

    Object oBatch = cvo.getAttributeValue("batchid");

    Object oDeptID = cvo.getAttributeValue("deptid");
    Object oDeptName = cvo.getAttributeValue("deptname");

    Object oEmployeeID = cvo.getAttributeValue("cemployeeid");
    Object oEmployeeName = cvo.getAttributeValue("cemployeename");

    Object oStorID = cvo.getAttributeValue("warehouseid");
    Object oStorName = cvo.getAttributeValue("warehousename");

    Object oCalbodyID = cvo.getAttributeValue("calbodyid");
    Object oCalbodyName = cvo.getAttributeValue("calbodyname");

    Object oCurID = cvo.getAttributeValue("custid");
    Object oCurName = cvo.getAttributeValue("custname");

    Object oBusiID = cvo.getAttributeValue("cbiztypeid");
    Object oBusiName = cvo.getAttributeValue("cbiztypename");

    Object oVfree1 = cvo.getAttributeValue("vfree1");
    Object oVfree2 = cvo.getAttributeValue("vfree2");
    Object oVfree3 = cvo.getAttributeValue("vfree3");
    Object oVfree4 = cvo.getAttributeValue("vfree4");
    Object oVfree5 = cvo.getAttributeValue("vfree5");

    Object oCurrencyID = cvo.getAttributeValue("currencyid");
    Object oCurrencyName = cvo.getAttributeValue("currencyname");

    BusinessCurrencyRateUtil dmo = new BusinessCurrencyRateUtil(oCorpID.toString());

    String sCurID = dmo.getLocalCurrPK();
    String sCurName = "";
    if (sCurID != null) {
      String sSQL = " select ";
      sSQL = sSQL + " currtypename ";
      sSQL = sSQL + " from ";
      sSQL = sSQL + " bd_currtype ";
      sSQL = sSQL + " where ";
      sSQL = sSQL + " pk_currtype = '" + sCurID + "'";
      String[][] sResult = queryInData(sSQL);
      if ((sResult != null) && (sResult.length != 0) && 
        (sResult[0] != null) && (sResult[0].length != 0)) {
        sCurName = sResult[0][0];
      }
    }

    if ((oCurrencyID != null) && (!oCurrencyID.toString().trim().equals(sCurID)))
    {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000092"));

      throw re;
    }

    Object oSalestruID = cvo.getAttributeValue("salestruid");
    Object oSalestruName = cvo.getAttributeValue("salestruname");

    Object oGroup = cvo.getAttributeValue("groupConditons");
    if (oGroup == null) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000093"));

      throw re;
    }
    String[] sGroups = (String[])(String[])oGroup;
    if (sGroups.length == 0) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000093"));

      throw re;
    }
    String sFieldString = "";
    String sOuterFieldString = "";
    String sOuterFieldString2 = "";
    String sGruopByFieldString = "";
    Vector vAttrs = new Vector();
    Vector vOuterFields = new Vector();
    String[] sAttributes = null;
    String sTableString = "";
    String sConnectionString = " ";

    boolean isCust = false;
    boolean isArea = false;
    for (int i = 0; i < sGroups.length; i++) {
      if (sGroups[i].equals("存货")) {
        vOuterFields.addElement("a.invcode");
        vOuterFields.addElement("a.invname");
        vOuterFields.addElement("a.invspec || a.invtype inv");
        vOuterFields.addElement("d.measname");
        vOuterFields.addElement("v.cinventoryid");
        vAttrs.addElement("invcode");
        vAttrs.addElement("invname");
        vAttrs.addElement("invmodel");
        vAttrs.addElement("unitname");
        vAttrs.addElement("pk_invmandoc");
        sTableString = sTableString + " inner join bd_invmandoc b ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cinventoryid = b.pk_invmandoc ";
        sTableString = sTableString + " inner join bd_invbasdoc a ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " b.pk_invbasdoc = a.pk_invbasdoc ";
        sTableString = sTableString + " inner join bd_measdoc d ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " a.pk_measdoc = d.pk_measdoc ";
        if ((cvo.getFreeItemGroupParam() != null) && (cvo.getFreeItemGroupParam().booleanValue()))
        {
          vOuterFields.addElement("v.vfree1");
          vOuterFields.addElement("v.vfree2");
          vOuterFields.addElement("v.vfree3");
          vOuterFields.addElement("v.vfree4");
          vOuterFields.addElement("v.vfree5");
          vAttrs.addElement("vfree1");
          vAttrs.addElement("vfree2");
          vAttrs.addElement("vfree3");
          vAttrs.addElement("vfree4");
          vAttrs.addElement("vfree5");
        }
        bUseInvClass = true;
      }
      else if (sGroups[i].equals("部门")) {
        vOuterFields.addElement("e.deptname");
        vOuterFields.addElement("v.cdeptid");
        vAttrs.addElement("deptname");
        vAttrs.addElement("deptid");
        sTableString = sTableString + " left outer join bd_deptdoc e";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cdeptid = e.pk_deptdoc ";
      }
      else if (sGroups[i].equals("库存组织")) {
        vOuterFields.addElement("v.crdcenterid");
        vAttrs.addElement("calbodyid");
        sTableString = sTableString + " left outer join bd_calbody m";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.crdcenterid = m.pk_calbody ";
      }
      else if (sGroups[i].equals("仓库")) {
        vOuterFields.addElement("f.storname");
        vOuterFields.addElement("v.cwarehouseid");
        vAttrs.addElement("warehousename");
        vAttrs.addElement("warehouseid");
        sTableString = sTableString + " left outer join bd_stordoc f";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cwarehouseid = f.pk_stordoc ";
      }
      else if (sGroups[i].equals("批次")) {
        vOuterFields.addElement("v.batch");
        vAttrs.addElement("batchname");
      }
      else if (sGroups[i].equals("业务员"))
      {
        vOuterFields.addElement("bd_psndoc.psnname");

        vAttrs.addElement("employee");
        sTableString = sTableString + " LEFT OUTER JOIN bd_psndoc  ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " v.cemployeeid = bd_psndoc.pk_psndoc ";
      }
      else if (sGroups[i].equals("客户")) {
        vOuterFields.addElement("g.custcode");
        vOuterFields.addElement("g.custname");
        vAttrs.addElement("custcode");
        vAttrs.addElement("custname");
        if (!isArea) {
          sTableString = sTableString + " left join bd_cumandoc h ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " v.ccustomvendorid = h.pk_cumandoc ";
          sTableString = sTableString + " left join bd_cubasdoc g ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " h.pk_cubasdoc = g.pk_cubasdoc ";
        }
        isCust = true;
      }
      else if (sGroups[i].equals("地区")) {
        vOuterFields.addElement("i.areaclname");
        vAttrs.addElement("areaname");
        if (!isCust) {
          sTableString = sTableString + " left join bd_cumandoc h ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " v.ccustomvendorid = h.pk_cumandoc ";
          sTableString = sTableString + " left join bd_cubasdoc g ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " h.pk_cubasdoc = g.pk_cubasdoc ";
        }
        sTableString = sTableString + " left join bd_areacl i ";
        sTableString = sTableString + " on ";
        sTableString = sTableString + " g.pk_areacl=i.pk_areacl ";
        isArea = true;
      }
    }
    for (int i = 0; i < vOuterFields.size(); i++) {
      sOuterFieldString = sOuterFieldString + vOuterFields.elementAt(i) + ",";
      if (((String)vOuterFields.elementAt(i)).startsWith("a.invspec || a.invtype"))
      {
        sOuterFieldString2 = sOuterFieldString2 + "inv" + ",";
        sGruopByFieldString = sGruopByFieldString + " a.invspec || a.invtype " + ",";
      }
      else
      {
        sGruopByFieldString = sGruopByFieldString + vOuterFields.elementAt(i) + ",";

        sOuterFieldString2 = sOuterFieldString2 + ((String)vOuterFields.elementAt(i)).substring(2) + ",";
      }

    }

    sAttributes = new String[vAttrs.size()];
    vAttrs.copyInto(sAttributes);
    if (sConnectionString.trim().length() != 0)
    {
      sConnectionString = " where " + sConnectionString.substring(5);
    }

    String sSQL = " select ";
    String sSQL1 = "";
    if (cvo.getRelateToArap().booleanValue()) {
      sSQL1 = sSQL + sOuterFieldString + " 0 outnum,0 outmny,0 salemny ";
      sSQL = sSQL + sOuterFieldString2 + " sum(outnum) outnum,sum(outmny) outmny,sum(salemny) salemny, sum(nsummny) nsummny from (select ";
    }

    sSQL = sSQL + sOuterFieldString + " sum(coalesce(case when fdispatchflag=1 then nnumber end,0)) outnum,";

    sSQL = sSQL + " sum(coalesce(case when fdispatchflag=1 then ";
    sSQL = sSQL + " case when nmoney is null then ";
    sSQL = sSQL + " case when fpricemodeflag =5 then nplanedmny else ";
    sSQL = sSQL + " case when fpricemodeflag =4 then nsimulatemny ";
    sSQL = sSQL + " end ";
    sSQL = sSQL + " end ";
    sSQL = sSQL + " else ";
    sSQL = sSQL + " nmoney ";
    sSQL = sSQL + " end ";
    sSQL = sSQL + " end,0)) outmny, ";

    sSQL = sSQL + " sum(coalesce(case when fdispatchflag=1 then nnumber*isnull(so_b.ntaxnetprice,0) end,0)) salemny";

    if (cvo.getRelateToArap().booleanValue()) {
      sSQL = sSQL + ",0.0000 nsummny ";
      sSQL1 = sSQL1 + ",isnull(af.wbfbbje,0) nsummny ";
    }

    String sFrom = " from ";

    sFrom = sFrom + " v_ia_inoutledger v ";
    sFrom = sFrom + " left join so_square_b so_b ";
    sFrom = sFrom + " on ";
    sFrom = sFrom + " so_b.corder_bid = v.csourcebillitemid ";
    sFrom = sFrom + " left join so_square so ";
    sFrom = sFrom + " on ";
    sFrom = sFrom + " so.csaleid = so_b.csaleid ";

    StringBuffer sWhere = new StringBuffer();
    sWhere.append(" where ");
    sWhere.append(" v.pk_corp = '" + oCorpID + "'");
    sWhere.append(" and ");
    sWhere.append(" v.dbilldate >= '" + oBeginDate + "'");
    sWhere.append(" and ");
    sWhere.append(" v.dbilldate <= '" + oEndDate + "'");

    sWhere.append(" and so_b.blargessflag = 'N'");

    sWhere.append(" and ");
    sWhere.append(" v.cbilltypecode in ('I2','I9','I5','IA')");

    if ((cvo.getAttributeValue("whereSQL") != null) && (!cvo.getAttributeValue("whereSQL").toString().equals("")))
    {
      sWhere.append(cvo.getAttributeValue("whereSQL").toString());
    }
    else {
      if (bUseInv) {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid = '" + oInvID + "'");
      }
      else if ((oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid " + cvo.getOP("invclcode") + oInvCLCode);
      }
      else if ((oInvLife != null) && (oInvLife.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid = b.pk_invmandoc ");
        sWhere.append(" and ");
        sWhere.append(" a.pk_invbasdoc = b.pk_invbasdoc ");
        sWhere.append(" and ");
        sWhere.append(" b.invlifeperiod = '" + oInvLife + "'");
      }

      if ((oCurID != null) && (oCurID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid = '" + oCurID + "'");
      }

      if ((oDeptID != null) && (oDeptID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cdeptid = '" + oDeptID + "'");
      }

      if ((oEmployeeID != null) && (oEmployeeID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cemployeeid = '" + oEmployeeID + "'");
      }

      if ((oStorID != null) && (oStorID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cwarehouseid = '" + oStorID + "'");
      }

      if ((oBusiID != null) && (oBusiID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cbiztypeid = '" + oBusiID + "'");
      }

      if ((oCalbodyID != null) && (oCalbodyID.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.crdcenterid = '" + oCalbodyID + "'");
      }

      if ((oBatch != null) && (oBatch.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vbatch = '" + oBatch + "'");
      }

      if ((oVfree1 != null) && (oVfree1.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree1 = '" + oVfree1 + "'");
      }
      if ((oVfree2 != null) && (oVfree2.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree2 = '" + oVfree2 + "'");
      }
      if ((oVfree3 != null) && (oVfree3.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree3 = '" + oVfree3 + "'");
      }
      if ((oVfree4 != null) && (oVfree4.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree4 = '" + oVfree4 + "'");
      }
      if ((oVfree5 != null) && (oVfree5.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.vfree5 = '" + oVfree5 + "'");
      }

      Object oCustomerCode = cvo.getAttributeValue("custcode");
      if ((oCustomerCode != null) && (oCustomerCode.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("custcode") + oCustomerCode);
      }

      Object oCustomerName = cvo.getAttributeValue("custname");
      if ((oCustomerName != null) && (oCustomerName.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("custname") + oCustomerName);
      }

      Object oCustomerArea = cvo.getAttributeValue("areaclname");
      if ((oCustomerArea != null) && (oCustomerArea.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("areaclname") + oCustomerArea);
      }

      Object oCustomerDef1 = cvo.getAttributeValue("def1");
      if ((oCustomerDef1 != null) && (oCustomerDef1.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def1") + oCustomerDef1);
      }

      Object oCustomerDef2 = cvo.getAttributeValue("def2");
      if ((oCustomerDef2 != null) && (oCustomerDef2.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def2") + oCustomerDef2);
      }

      Object oCustomerDef3 = cvo.getAttributeValue("def3");
      if ((oCustomerDef3 != null) && (oCustomerDef3.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def3") + oCustomerDef3);
      }

      Object oCustomerDef4 = cvo.getAttributeValue("def4");
      if ((oCustomerDef4 != null) && (oCustomerDef4.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def4") + oCustomerDef4);
      }

      Object oCustomerDef5 = cvo.getAttributeValue("def5");
      if ((oCustomerDef5 != null) && (oCustomerDef5.toString().trim().length() != 0))
      {
        sWhere.append(" and ");
        sWhere.append(" v.ccustomvendorid " + cvo.getOP("def5") + oCustomerDef5);
      }

      Object oInvmancode = cvo.getAttributeValue("invmancode");
      if ((oInvmancode != null) && (oInvmancode.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid " + cvo.getOP("invmancode") + oInvmancode);
      }

      Object oInvname = cvo.getAttributeValue("invname");
      if ((oInvname != null) && (oInvname.toString().trim().length() != 0)) {
        sWhere.append(" and ");
        sWhere.append(" v.cinventoryid " + cvo.getOP("invname") + oInvname);
      }

    }

    String sFQSK = getBizTypeID(oCorpID.toString(), "FQSK");
    String sWTDX = getBizTypeID(oCorpID.toString(), "WTDX");

    sWhere.append(" and ");
    sWhere.append(" ( ");
    sWhere.append(" not (");
    sWhere.append(" (");
    sWhere.append(" v.cbiztypeid in (" + sFQSK + ") ");
    sWhere.append(" or ");
    sWhere.append(" v.cbiztypeid in (" + sWTDX + ")");
    sWhere.append(")");
    sWhere.append(" and ");
    sWhere.append(" v.csourcebilltypecode ='4C'");
    sWhere.append(")");
    sWhere.append(" or ");
    sWhere.append(" v.cbiztypeid is null ");
    sWhere.append(" or ");
    sWhere.append(" v.csourcebilltypecode is null ");
    sWhere.append(" ) ");
    sFrom = sFrom + sTableString;
    if ((!bUseInv) && (oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
    {
      if (!bUseInvClass)
      {
        sFrom = sFrom + " inner join bd_invmandoc b ";
        sFrom = sFrom + " on ";
        sFrom = sFrom + " v.cinventoryid = b.pk_invmandoc ";
        sFrom = sFrom + " inner join bd_invbasdoc a ";
        sFrom = sFrom + " on ";
        sFrom = sFrom + " b.pk_invbasdoc = a.pk_invbasdoc ";
        sFrom = sFrom + " left join bd_invcl c ";
        sFrom = sFrom + " on ";
        sFrom = sFrom + " a.pk_invcl = c.pk_invcl ";
      }
    }
    sSQL = sSQL + sFrom + sConnectionString;
    sSQL = sSQL + sWhere.toString();

    if (cvo.getRelateToArap().booleanValue()) {
      sFrom = sFrom + " left join arap_djfb af ";
      sFrom = sFrom + " on ";
      sFrom = sFrom + " v.csourcebillitemid = af.ddhh ";
      sSQL1 = sSQL1 + sFrom;
      sSQL1 = sSQL1 + sWhere.toString() + " and af.dr = 0 ";
    }

    sOuterFieldString = sOuterFieldString.substring(0, sOuterFieldString.length() - 1);

    sOuterFieldString2 = sOuterFieldString2.substring(0, sOuterFieldString2.length() - 1);

    sGruopByFieldString = sGruopByFieldString.substring(0, sGruopByFieldString.length() - 1);

    if (cvo.getRelateToArap().booleanValue()) {
      sSQL = sSQL + " group by v.csourcebillitemid," + sGruopByFieldString;
      sSQL = sSQL + " union all (" + sSQL1 + ")) t";
      sSQL = sSQL + " group by " + sOuterFieldString2;
    }
    else {
      sSQL = sSQL + " group by " + sGruopByFieldString;
    }
    Vector vOrderByField = new Vector();
    if ((cvo.getOrderName() != null) && (cvo.getOrderName().length > 0)) {
      String sOrderByField = " outnum desc ";
      String[] sOrder = cvo.getOrderName();
      String desc = " ";
      if ((cvo.getOrderFlag() != null) && (!cvo.getOrderFlag().booleanValue())) {
        desc = " desc ";
      }
      for (int i = 0; i < sOrder.length; i++) {
        String sOrderBy = null;
        if (sOrder[i].equals("销售数量"))
        {
          sOrderBy = " outnum " + desc;
        }
        else if (sOrder[i].equals("销售金额"))
        {
          sOrderBy = " salemny " + desc;
        }
        if (i == 0) {
          sOrderByField = sOrderBy;
        }
        else {
          sOrderByField = sOrderByField + "," + sOrderBy;
        }
      }
      sSQL = sSQL + " order by " + sOrderByField;
    }
    String[][] sResult = queryInData(sSQL);

    int iFieldLength = sAttributes.length;
    for (int i = 0; i < sResult.length; i++) {
      String[] sTemp = sResult[i];
      ReturnVO sgVO = new ReturnVO();

      sgVO.setAttributeValue("deptid", oDeptID);
      sgVO.setAttributeValue("deptname", oDeptName);
      sgVO.setAttributeValue("warehouseid", oStorID);
      sgVO.setAttributeValue("warehousename", oStorName);
      sgVO.setAttributeValue("pk_invmandoc", oInvID);
      sgVO.setAttributeValue("invcode", oInvCode);
      sgVO.setAttributeValue("invname", oInvName);
      sgVO.setAttributeValue("batchname", oBatch);
      sgVO.setAttributeValue("unitname", oMeasName);
      sgVO.setAttributeValue("employee", oEmployeeName);
      sgVO.setAttributeValue("custname", oCurName);

      for (int j = 0; j < iFieldLength; j++) {
        sgVO.setAttributeValue(sAttributes[j], sTemp[j]);
      }

      sgVO.setAttributeValue("cursonumber", new UFDouble(sTemp[iFieldLength]));

      sgVO.setAttributeValue("cursocost", new UFDouble(sTemp[(iFieldLength + 1)]));

      sgVO.setAttributeValue("cursalemoney", new UFDouble(sTemp[(iFieldLength + 2)]));

      if (cvo.getRelateToArap().booleanValue()) {
        sgVO.setAttributeValue("cursomoney", new UFDouble(sTemp[(iFieldLength + 3)]));
      }

      vsgVOs.addElement(sgVO);
    }
    if (vsgVOs.size() != 0) {
      sgVOs = new ReturnVO[vsgVOs.size()];
      vsgVOs.copyInto(sgVOs);
    }
    return sgVOs;
  }

  public ReturnVO[] getIADataForSale(ParamVO cvo)
    throws RemoteException, BusinessException
  {
    ReturnVO[] sgVOs = null;
    Vector vsgVOs = new Vector();
    try
    {
      boolean bUseInv = false;
      int iInvclLength = -1;

      Object oCorpID = cvo.getAttributeValue("pk_corp");
      if ((oCorpID == null) || (oCorpID.toString().trim().length() == 0)) {
        BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000088"));

        throw re;
      }

      Object oBeginDate = cvo.getAttributeValue("startDate");
      if ((oBeginDate == null) || (oBeginDate.toString().trim().length() == 0)) {
        BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000090"));

        throw re;
      }

      Object oEndDate = cvo.getAttributeValue("endDate");
      if ((oEndDate == null) || (oEndDate.toString().trim().length() == 0)) {
        BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000091"));

        throw re;
      }

      Object oInvID = cvo.getAttributeValue("pk_invmandoc");
      Object oInvCode = cvo.getAttributeValue("invmancode");
      Object oInvName = cvo.getAttributeValue("invname");
      Object oMeasName = cvo.getAttributeValue("unitname");

      if ((oInvID != null) && (oInvID.toString().trim().length() != 0))
      {
        bUseInv = true;
      }

      Object oInvCLCode = cvo.getAttributeValue("invclcode");
      Object oInvCLID = cvo.getAttributeValue("invclname");

      Object oBatch = cvo.getAttributeValue("batchname");
      String sBatchOper = cvo.getOP("batchname");

      Object oDeptID = cvo.getAttributeValue("deptid");
      Object oDeptName = cvo.getAttributeValue("deptname");

      Object oEmployeeID = cvo.getAttributeValue("cemployeeid");
      Object oEmployeeName = cvo.getAttributeValue("cemployeename");

      Object oStorID = cvo.getAttributeValue("warehouseid");
      Object oStorName = cvo.getAttributeValue("warehousename");

      Object oCalbodyID = cvo.getAttributeValue("calbodyid");
      Object oCalbodyName = cvo.getAttributeValue("calbodyname");

      Object oCurID = cvo.getAttributeValue("custid");
      Object oCurName = cvo.getAttributeValue("custname");

      Object oBusiID = cvo.getAttributeValue("cbiztypeid");
      Object oBusiName = cvo.getAttributeValue("cbiztypename");

      Object oCurrencyID = cvo.getAttributeValue("currencyid");
      Object oCurrencyName = cvo.getAttributeValue("currencyname");

      BusinessCurrencyRateUtil dmo = new BusinessCurrencyRateUtil(oCorpID.toString());

      String sCurID = dmo.getLocalCurrPK();
      String sCurName = "";

      if (sCurID != null) {
        String sSQL = " select ";
        sSQL = sSQL + " currtypename ";
        sSQL = sSQL + " from ";
        sSQL = sSQL + " bd_currtype ";
        sSQL = sSQL + " where ";
        sSQL = sSQL + " pk_currtype = '" + sCurID + "'";

        String[][] sResult = queryInData(sSQL);

        if ((sResult != null) && (sResult.length != 0) && 
          (sResult[0] != null) && (sResult[0].length != 0)) {
          sCurName = sResult[0][0];
        }

      }

      if ((oCurrencyID != null) && (!oCurrencyID.toString().trim().equals(sCurID))) {
        BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000092"));

        throw re;
      }

      Object oSalestruID = cvo.getAttributeValue("salestruid");
      Object oSalestruName = cvo.getAttributeValue("salestruname");

      Object oGroup = cvo.getAttributeValue("groupConditons");
      if (oGroup == null) {
        BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000093"));

        throw re;
      }

      String[] sGroups = (String[])(String[])oGroup;
      if (sGroups.length == 0) {
        BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000093"));

        throw re;
      }

      String sFieldString = "";
      String sOuterFieldString = "";
      Vector vAttrs = new Vector();
      Vector vOuterFields = new Vector();
      String[] sAttributes = null;

      String sTableString = "";
      String sConnectionString = " ";

      boolean isCust = false;
      boolean isArea = false;

      for (int i = 0; i < sGroups.length; i++) {
        String sField = "";
        if (sGroups[i].equals("存货"))
        {
          sField = "cinventoryid inv";
          vOuterFields.addElement("a.invcode");
          vOuterFields.addElement("a.invname");
          vOuterFields.addElement("a.invspec || a.invtype");
          vOuterFields.addElement("d.measname");
          vOuterFields.addElement("x.inv");

          vAttrs.addElement("invcode");
          vAttrs.addElement("invname");
          vAttrs.addElement("invmodel");
          vAttrs.addElement("unitname");
          vAttrs.addElement("pk_invmandoc");

          sTableString = sTableString + " inner join bd_invmandoc b ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " x.inv = b.pk_invmandoc ";
          sTableString = sTableString + " inner join bd_invbasdoc a ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " b.pk_invbasdoc = a.pk_invbasdoc ";
          sTableString = sTableString + " inner join bd_measdoc d ";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " a.pk_measdoc = d.pk_measdoc ";

          if ((cvo.getFreeItemGroupParam() != null) && (cvo.getFreeItemGroupParam().booleanValue()))
          {
            sField = sField + ",v.vfree1,v.vfree2,v.vfree3,v.vfree4,v.vfree5";

            vOuterFields.addElement("x.vfree1");
            vOuterFields.addElement("x.vfree2");
            vOuterFields.addElement("x.vfree3");
            vOuterFields.addElement("x.vfree4");
            vOuterFields.addElement("x.vfree5");

            vAttrs.addElement("vfree1");
            vAttrs.addElement("vfree2");
            vAttrs.addElement("vfree3");
            vAttrs.addElement("vfree4");
            vAttrs.addElement("vfree5");
          }
        }
        else if (sGroups[i].equals("部门"))
        {
          sField = "cdeptid dept";
          vOuterFields.addElement("e.deptname");
          vOuterFields.addElement("x.dept");

          vAttrs.addElement("deptname");
          vAttrs.addElement("deptid");

          sTableString = sTableString + " left outer join bd_deptdoc e";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " x.dept = e.pk_deptdoc ";
        }
        else if (sGroups[i].equals("仓库"))
        {
          sField = "cwarehouseid warehouse";
          vOuterFields.addElement("f.storname");
          vOuterFields.addElement("x.warehouse");

          vAttrs.addElement("warehousename");
          vAttrs.addElement("warehouseid");

          sTableString = sTableString + " left outer join bd_stordoc f";
          sTableString = sTableString + " on ";
          sTableString = sTableString + " x.warehouse = f.pk_stordoc ";
        }
        else if (!sGroups[i].equals("批次"))
        {
          if (sGroups[i].equals("业务员"))
          {
            sField = "cemployeeid employ";
            vOuterFields.addElement("x.employ");
            vOuterFields.addElement("bd_psndoc.psnname");

            vAttrs.addElement("employee");
            vAttrs.addElement("employee");
            sTableString = sTableString + " LEFT OUTER JOIN bd_psndoc  ";
            sTableString = sTableString + " on ";
            sTableString = sTableString + " x.employ = bd_psndoc.pk_psndoc ";
          }
          else if (sGroups[i].equals("客户"))
          {
            sField = "ccustomvendorid cust";
            vOuterFields.addElement("g.custcode");
            vOuterFields.addElement("g.custname");

            vAttrs.addElement("custcode");
            vAttrs.addElement("custname");

            if (!isArea) {
              sTableString = sTableString + " inner join bd_cumandoc h ";
              sTableString = sTableString + " on ";
              sTableString = sTableString + " x.cust = h.pk_cumandoc ";
              sTableString = sTableString + " inner join bd_cubasdoc g ";
              sTableString = sTableString + " on ";
              sTableString = sTableString + " h.pk_cubasdoc = g.pk_cubasdoc ";
            }

            isCust = true;
          }
          else if (sGroups[i].equals("地区"))
          {
            sField = "i.areaclname area";
            vOuterFields.addElement("i.areaclname");

            vAttrs.addElement("areaname");

            if (!isCust) {
              sTableString = sTableString + " inner join bd_cumandoc h ";
              sTableString = sTableString + " on ";
              sTableString = sTableString + " x.cust = h.pk_cumandoc ";
              sTableString = sTableString + " inner join bd_cubasdoc g ";
              sTableString = sTableString + " on ";
              sTableString = sTableString + " h.pk_cubasdoc = g.pk_cubasdoc ";
            }
            sTableString = sTableString + " inner join bd_areacl i ";
            sTableString = sTableString + " on ";
            sTableString = sTableString + " g.pk_areacl=i.pk_areacl ";
            isArea = true;
          }
        }
        if (!sField.equals("")) {
          sFieldString = sFieldString + sField + ",";
        }

      }

      sFieldString = sFieldString + "vbatch batch,";

      for (int i = 0; i < vOuterFields.size(); i++) {
        sOuterFieldString = sOuterFieldString + vOuterFields.elementAt(i) + ",";
      }

      sOuterFieldString = sOuterFieldString + " x.batch,";

      vAttrs.addElement("batchname");
      sAttributes = new String[vAttrs.size()];
      vAttrs.copyInto(sAttributes);

      if (sConnectionString.trim().length() != 0)
      {
        sConnectionString = " where " + sConnectionString.substring(5);
      }

      String sSQL = " select ";
      sSQL = sSQL + sOuterFieldString + "  sum(begininnum - beginoutnum),sum(begininmny - beginoutmny),sum(x.innum),sum(x.inmny),sum(x.outnum),sum(x.outmny),sum(begininnum - beginoutnum) + sum(x.innum) - sum(x.outnum),sum(begininmny - beginoutmny) + sum(x.inmny) - sum(x.outmny) ";
      sSQL = sSQL + " from ";

      sSQL = sSQL + " (";
      sSQL = sSQL + " select ";
      sSQL = sSQL + sFieldString + " coalesce(case when fdispatchflag=0 then nnumber end,0) begininnum,coalesce(case when fdispatchflag=0 then nmoney end,0) begininmny,coalesce(case when fdispatchflag=1 then nnumber end,0) beginoutnum, coalesce(case when fdispatchflag=1 then";

      sSQL = sSQL + " case when nmoney is null then ";

      sSQL = sSQL + " case when nsimulatemny is null then isnull(nplanedmny,0) else  nsimulatemny ";

      sSQL = sSQL + " end ";
      sSQL = sSQL + " else ";
      sSQL = sSQL + " nmoney ";
      sSQL = sSQL + " end ";

      sSQL = sSQL + " end,0) beginoutmny,0 innum,0 inmny,0 outnum,0 outmny ";
      sSQL = sSQL + " from ";
      sSQL = sSQL + " v_ia_inoutledger v ";

      if ((!bUseInv) && (oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
      {
        sSQL = sSQL + ",bd_invbasdoc a,bd_invmandoc b,bd_invcl c ";

        iInvclLength = oInvCLCode.toString().trim().length();
      }

      sSQL = sSQL + " where ";
      sSQL = sSQL + " v.pk_corp = '" + oCorpID + "'";
      sSQL = sSQL + " and ";
      sSQL = sSQL + " v.dbilldate < '" + oBeginDate + "'";

      if (bUseInv) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cinventoryid = '" + oInvID + "'";
      }
      else if ((oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
      {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cinventoryid = b.pk_invmandoc ";
        sSQL = sSQL + " and ";
        sSQL = sSQL + " a.pk_invbasdoc = b.pk_invbasdoc ";
        sSQL = sSQL + " and ";
        sSQL = sSQL + " a.pk_invcl = c.pk_invcl ";
        sSQL = sSQL + " and ";
        sSQL = sSQL + " left(c.invclasscode," + iInvclLength + ") = '" + oInvCLCode + "'";
      }

      if ((oDeptID != null) && (oDeptID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cdeptid = '" + oDeptID + "'";
      }

      if ((oEmployeeID != null) && (oEmployeeID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cemployeeid = '" + oEmployeeID + "'";
      }

      if ((oStorID != null) && (oStorID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cwarehouseid = '" + oStorID + "'";
      }

      if ((oCalbodyID != null) && (oCalbodyID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.crdcenterid = '" + oCalbodyID + "'";
      }

      if ((oBatch != null) && (oBatch.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        if (sBatchOper.equals("=")) {
          sSQL = sSQL + " v.vbatch = '" + oBatch + "'";
        }
        if (sBatchOper.equals("like")) {
          sSQL = sSQL + " v.vbatch  like'" + oBatch + "%'";
        }

      }

      if ((oCurID != null) && (oCurID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.ccustomvendorid = '" + oCurID + "'";
      }

      if ((oBusiID != null) && (oBusiID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cbiztypeid = '" + oBusiID + "'";
      }

      String sFQSK = getBizTypeID(oCorpID.toString(), "FQSK");
      String sWTDX = getBizTypeID(oCorpID.toString(), "WTDX");

      sSQL = sSQL + " and ";
      sSQL = sSQL + " ( ";
      sSQL = sSQL + " not (";
      sSQL = sSQL + " (";
      sSQL = sSQL + " v.cbiztypeid in (" + sFQSK + ") ";
      sSQL = sSQL + " or ";
      sSQL = sSQL + " v.cbiztypeid in (" + sWTDX + ")";
      sSQL = sSQL + ")";
      sSQL = sSQL + " and ";
      sSQL = sSQL + " v.csourcebilltypecode ='" + "4C" + "'";
      sSQL = sSQL + ")";
      sSQL = sSQL + " or ";
      sSQL = sSQL + " v.cbiztypeid is null ";
      sSQL = sSQL + " or ";
      sSQL = sSQL + " v.csourcebilltypecode is null ";
      sSQL = sSQL + " ) ";

      sSQL = sSQL + " union all ";

      sSQL = sSQL + " ( ";
      sSQL = sSQL + " select ";
      sSQL = sSQL + sFieldString + " 0 begininnum,0 begininmny,0 beginoutnum,0 beginoutmny, coalesce(case when fdispatchflag=0 then nnumber end,0) innum,coalesce(case when fdispatchflag=0 then nmoney end,0) inmny,coalesce(case when fdispatchflag=1 then nnumber end,0) outnum,coalesce(case when fdispatchflag=1 then ";

      sSQL = sSQL + " case when nmoney is null then ";

      sSQL = sSQL + " case when nsimulatemny is null then isnull(nplanedmny,0) else  nsimulatemny ";

      sSQL = sSQL + " end ";
      sSQL = sSQL + " else ";
      sSQL = sSQL + " nmoney ";
      sSQL = sSQL + " end ";

      sSQL = sSQL + " end,0) outmny ";

      sSQL = sSQL + " from ";
      sSQL = sSQL + " v_ia_inoutledger v ";

      if ((!bUseInv) && (oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
      {
        sSQL = sSQL + ",bd_invbasdoc a,bd_invmandoc b,bd_invcl c ";

        iInvclLength = oInvCLCode.toString().trim().length();
      }

      sSQL = sSQL + " where ";
      sSQL = sSQL + " v.pk_corp = '" + oCorpID + "'";
      sSQL = sSQL + " and ";
      sSQL = sSQL + " v.dbilldate >= '" + oBeginDate + "'";
      sSQL = sSQL + " and ";
      sSQL = sSQL + " v.dbilldate <= '" + oEndDate + "'";

      sSQL = sSQL + " and ";
      sSQL = sSQL + " ( v.cbilltypecode in ('" + "I2" + "','" + "I9" + "','" + "I5" + "','" + "IA" + "','" + "IH" + "')";

      sSQL = sSQL + " or ( v.cbilltypecode in('II','IJ') and v.cfirstbilltypecode in('5C','5D')) )";

      if (bUseInv) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cinventoryid = '" + oInvID + "'";
      }
      else if ((oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0))
      {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cinventoryid = b.pk_invmandoc ";
        sSQL = sSQL + " and ";
        sSQL = sSQL + " a.pk_invbasdoc = b.pk_invbasdoc ";
        sSQL = sSQL + " and ";
        sSQL = sSQL + " a.pk_invcl = c.pk_invcl ";
        sSQL = sSQL + " and ";
        sSQL = sSQL + " left(c.invclasscode," + iInvclLength + ") = '" + oInvCLCode + "'";
      }

      if ((oDeptID != null) && (oDeptID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cdeptid = '" + oDeptID + "'";
      }

      if ((oEmployeeID != null) && (oEmployeeID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cemployeeid = '" + oEmployeeID + "'";
      }

      if ((oStorID != null) && (oStorID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.cwarehouseid = '" + oStorID + "'";
      }

      if ((oCalbodyID != null) && (oCalbodyID.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        sSQL = sSQL + " v.crdcenterid = '" + oCalbodyID + "'";
      }

      if ((oBatch != null) && (oBatch.toString().trim().length() != 0)) {
        sSQL = sSQL + " and ";
        if (sBatchOper.equals("=")) {
          sSQL = sSQL + " v.vbatch = '" + oBatch + "'";
        }
        if (sBatchOper.equals("like")) {
          sSQL = sSQL + " v.vbatch  like'" + oBatch + "%'";
        }

      }

      sSQL = sSQL + " and ";
      sSQL = sSQL + " ( ";
      sSQL = sSQL + " not (";
      sSQL = sSQL + " (";
      sSQL = sSQL + " v.cbiztypeid in (" + sFQSK + ") ";
      sSQL = sSQL + " or ";
      sSQL = sSQL + " v.cbiztypeid in (" + sWTDX + ")";
      sSQL = sSQL + ")";
      sSQL = sSQL + " and ";
      sSQL = sSQL + " v.csourcebilltypecode ='" + "4C" + "'";
      sSQL = sSQL + ")";
      sSQL = sSQL + " or ";
      sSQL = sSQL + " v.cbiztypeid is null ";
      sSQL = sSQL + " or ";
      sSQL = sSQL + " v.csourcebilltypecode is null ";
      sSQL = sSQL + " ) ";

      sSQL = sSQL + " )";
      sSQL = sSQL + " )x" + sTableString;
      sSQL = sSQL + sConnectionString;

      sOuterFieldString = sOuterFieldString.substring(0, sOuterFieldString.length() - 1);

      sSQL = sSQL + " group by " + sOuterFieldString;

      String[][] sResult = queryInData(sSQL);

      SCMEnv.out(sSQL);

      int iFieldLength = sAttributes.length;
      for (int i = 0; i < sResult.length; i++) {
        String[] sTemp = sResult[i];
        ReturnVO sgVO = new ReturnVO();

        sgVO.setAttributeValue("deptid", oDeptID);
        sgVO.setAttributeValue("deptname", oDeptName);
        sgVO.setAttributeValue("warehouseid", oStorID);
        sgVO.setAttributeValue("warehousename", oStorName);
        sgVO.setAttributeValue("pk_invmandoc", oInvID);
        sgVO.setAttributeValue("invcode", oInvCode);
        sgVO.setAttributeValue("invname", oInvName);
        sgVO.setAttributeValue("batchname", oBatch);
        sgVO.setAttributeValue("unitname", oMeasName);
        sgVO.setAttributeValue("employee", oEmployeeName);
        sgVO.setAttributeValue("custname", oCurName);

        for (int j = 0; j < iFieldLength; j++) {
          sgVO.setAttributeValue(sAttributes[j], sTemp[j]);
        }

        sgVO.setAttributeValue("initnumber", new UFDouble(sTemp[iFieldLength]));
        sgVO.setAttributeValue("initmoney", new UFDouble(sTemp[(iFieldLength + 1)]));

        sgVO.setAttributeValue("curpurnumber", new UFDouble(sTemp[(iFieldLength + 2)]));

        sgVO.setAttributeValue("curpurmoney", new UFDouble(sTemp[(iFieldLength + 3)]));

        sgVO.setAttributeValue("cursonumber", new UFDouble(sTemp[(iFieldLength + 4)]));

        sgVO.setAttributeValue("cursocost", new UFDouble(sTemp[(iFieldLength + 5)]));

        sgVO.setAttributeValue("lastnumber", new UFDouble(sTemp[(iFieldLength + 6)]));

        sgVO.setAttributeValue("lastmoney", new UFDouble(sTemp[(iFieldLength + 7)]));

        vsgVOs.addElement(sgVO);
      }

      if (vsgVOs.size() != 0) {
        sgVOs = new ReturnVO[vsgVOs.size()];
        vsgVOs.copyInto(sgVOs);
      }
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }

    return sgVOs;
  }

  public Hashtable getNewBusiReceive(String[] ccustomerids)
    throws SQLException, BusinessException
  {
    Hashtable hNmny = null;

    Hashtable hOrdermny = getBusiReceive(ccustomerids);

    IArapQuery4SailOrderPublic bo = (IArapQuery4SailOrderPublic)NCLocator.getInstance().lookup(IArapQuery4SailOrderPublic.class.getName());
    Hashtable hFmny = bo.getNewCoustomBlcExPh(ccustomerids);

    for (int i = 0; i < ccustomerids.length; i++) {
      String sCustID = ccustomerids[i];

      UFDouble ordermny = new UFDouble(0.0D);
      if ((hOrdermny != null) && (hOrdermny.containsKey(sCustID))) {
        ordermny = (UFDouble)hOrdermny.get(sCustID);
      }

      UFDouble fmny = new UFDouble(0.0D);

      if ((hFmny != null) && (hFmny.containsKey(sCustID))) {
        fmny = (UFDouble)hFmny.get(sCustID);
      }

      UFDouble mny = ordermny.add(fmny);

      if (hNmny == null) {
        hNmny = new Hashtable();
      }
      hNmny.put(sCustID, mny);
    }
    return hNmny;
  }

  public Hashtable getNewFinanceBackMoney(String[] ccustomerids)
    throws SQLException
  {
    return getFinanceBackMoney(ccustomerids, false);
  }

  public Hashtable getNewFinancePreBackMoney(String[] ccustomerids)
    throws SQLException
  {
    return getFinanceBackMoney(ccustomerids, true);
  }

  public Hashtable getNewFinanceReceive(String[] ccustomerids)
    throws BusinessException
  {
    IArapQuery4SailOrderPublic bo = (IArapQuery4SailOrderPublic)NCLocator.getInstance().lookup(IArapQuery4SailOrderPublic.class.getName());

    return bo.getNewCoustomBanlance(ccustomerids);
  }

  public Hashtable getNewOrderReceive(String[] ccustomerids)
    throws BusinessException, SQLException
  {
    Hashtable hNmny = null;

    Hashtable hOrdermny = getOrderReceive(ccustomerids);

    IArapQuery4SailOrderPublic bo = (IArapQuery4SailOrderPublic)NCLocator.getInstance().lookup(IArapQuery4SailOrderPublic.class.getName());
    Hashtable hFmny = bo.getNewCoustomBlcExPh(ccustomerids);

    for (int i = 0; i < ccustomerids.length; i++)
    {
      String sCustID = ccustomerids[i];

      UFDouble ordermny = new UFDouble(0.0D);
      if ((hOrdermny != null) && (hOrdermny.containsKey(sCustID))) {
        ordermny = (UFDouble)hOrdermny.get(sCustID);
      }

      UFDouble fmny = new UFDouble(0.0D);

      if ((hFmny != null) && (hFmny.containsKey(sCustID))) {
        fmny = (UFDouble)hFmny.get(sCustID);
      }

      SCMEnv.out("ordermny======" + ordermny);
      SCMEnv.out("fmny======" + fmny);
      UFDouble mny = ordermny.add(fmny);
      SCMEnv.out("mny======" + mny);

      if (hNmny == null) {
        hNmny = new Hashtable();
      }
      hNmny.put(sCustID, mny);
    }

    return hNmny;
  }

  private Hashtable getOrderReceive(String[] ccustomerids)
    throws SQLException
  {
    String where = getCustCond("ccustomerid", ccustomerids);
    Hashtable hNmny = null;

    String strSQL = "SELECT ccustomerid,sum(mny) FROM (SELECT ccustomerid,ISNULL(so_saleorder_b.nsummny, 0) - ISNULL(so_saleexecute.ntotalpaymny, 0) - ISNULL((SELECT SUM(arap_djfb.jfshl * so_b.ntaxnetprice - arap_djfb.jfbbje)  FROM arap_djzb LEFT OUTER JOIN arap_djfb ON arap_djfb.vouchid = arap_djzb.vouchid LEFT OUTER JOIN \tso_saleorder_b so_b ON arap_djfb.cksqsh = so_b.corder_bid  WHERE (arap_djzb.dr = 0) AND (arap_djfb.dr = 0) AND (arap_djzb.djzt = 2) and arap_djzb.lybz = 3 and frowstatus = 2 and  arap_djfb.cksqsh  =  so_saleorder_b.CORDER_BID),0) as mny FROM so_saleorder_b INNER JOIN so_sale ON so_saleorder_b.csaleid = so_sale.csaleid LEFT OUTER JOIN so_saleexecute ON so_saleorder_b.corder_bid = so_saleexecute.csale_bid WHERE so_saleorder_b.beditflag = 'N'  and frowstatus != 5 and frowstatus != 6 and frowstatus != 3 and " + where + ") so" + " group by ccustomerid";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        if (hNmny == null) {
          hNmny = new Hashtable();
        }

        String ccustomerid = rs.getString(1);

        BigDecimal n = rs.getBigDecimal(2);
        UFDouble nmny = n == null ? new UFDouble(0) : new UFDouble(n);

        hNmny.put(ccustomerid, nmny);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return hNmny;
  }

  public UFDouble getSKByCustAndDept(String corpid, String custid, String cdeptid, String startdate, String enddate)
    throws SQLException
  {
    StringBuffer sSQL1 = new StringBuffer();
    sSQL1.append("select sum(fb.dfbbje) ");
    sSQL1.append("from arap_djfb fb inner  join arap_djzb zb on zb.vouchid=fb.vouchid ");

    sSQL1.append("where (zb.djdl = 'sk') and (zb.dr = 0) and (fb.dr = 0) and fb.wldx=0 ");

    sSQL1.append(" and fb.deptid = ? and zb.dwbm = ? ");
    if (custid != null) {
      sSQL1.append(" and ksbm_cl = ? ");
    }

    if (enddate == null) {
      sSQL1.append(" and zb.djrq = '" + startdate + "'");
    }
    else if (startdate == null) {
      sSQL1.append(" and zb.djrq < '" + enddate + "' ");
    }
    else
    {
      sSQL1.append(" and zb.djrq >= '" + startdate + "' and zb.djrq <= '" + enddate + "' ");
    }

    StringBuffer sSQL2 = new StringBuffer();
    sSQL2.append("select sum(arap_djclb.dfclbbje) ");
    sSQL2.append("from arap_djfb fb inner  join arap_djzb zb on zb.vouchid=fb.vouchid ");

    sSQL2.append("left outer join arap_djclb on fb.fb_oid=arap_djclb.fb_oid ");
    sSQL2.append("where (zb.djdl = 'sk') and (zb.dr = 0) and (fb.dr = 0) and fb.wldx=0 ");

    sSQL2.append("and arap_djclb.clbz=1 and arap_djclb.dr=0 ");
    sSQL2.append("and fb.deptid = ? and zb.dwbm = ? ");
    if (custid != null) {
      sSQL2.append(" and ksbm_cl = ? ");
    }
    if (enddate == null) {
      sSQL2.append(" and zb.djrq = '" + startdate + "'");
    }
    else if (startdate == null) {
      sSQL1.append(" and zb.djrq <= '" + enddate + "' ");
    }
    else
    {
      sSQL2.append(" and zb.djrq >= '" + startdate + "' and zb.djrq <= '" + enddate + "' ");
    }

    UFDouble dfbbje = new UFDouble();
    UFDouble dfclbbje = new UFDouble();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();

      stmt = con.prepareStatement(sSQL1.toString());
      stmt.setString(1, cdeptid);
      stmt.setString(2, corpid);
      if (custid != null) {
        stmt.setString(3, custid);
      }
      ResultSet rs1 = stmt.executeQuery();
      if (rs1.next())
      {
        BigDecimal n = rs1.getBigDecimal(1);
        dfbbje = n == null ? new UFDouble(0) : new UFDouble(n);
      }
      stmt.close();

      stmt = con.prepareStatement(sSQL2.toString());
      stmt.setString(1, cdeptid);
      stmt.setString(2, corpid);
      if (custid != null) {
        stmt.setString(3, custid);
      }
      ResultSet rs2 = stmt.executeQuery();
      if (rs2.next())
      {
        BigDecimal n = rs2.getBigDecimal(1);
        dfclbbje = n == null ? new UFDouble(0) : new UFDouble(n);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return dfbbje.sub(dfclbbje);
  }

  public boolean isAutoatpcheck(SaleorderBVO saleOrderBVO)
    throws SQLException, BusinessException
  {
    String sql = "SELECT isautoatpcheck  FROM bd_invmandoc  WHERE pk_invmandoc = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, saleOrderBVO.getCinventoryid());
      ResultSet rs = stmt.executeQuery();
      boolean i = (rs.next()) && ("Y".equals(rs.getString("isautoatpcheck"))) ? true : false;
      return i;
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception e) {
//    	    throw e;
      }
    }
  }

  public void orderChangeSendInfo(SaleOrderVO vo, Integer type)
    throws Exception
  {
    if (vo == null) {
      return;
    }

    SaleorderHVO headVO = (SaleorderHVO)vo.getParentVO();

    SaleorderBVO[] bodyVO = (SaleorderBVO[])(SaleorderBVO[])vo.getChildrenVO();

    SaleToMRPDMO dmo = new SaleToMRPDMO();

    if (bodyVO == null) {
      return;
    }

    String corp = headVO.getPk_corp();
    String operid = headVO.getCoperatorid();
    String billcode = headVO.getVreceiptcode();

    Hashtable hfactoryid = getFactoryid(headVO.getPrimaryKey());
    Hashtable htemp = new Hashtable();

    for (int i = 0; i < bodyVO.length; i++)
      if (bodyVO[i].getPrimaryKey() != null) {
        String cfactoryid = (String)hfactoryid.get(bodyVO[i].getPrimaryKey());
        if ((cfactoryid == null) || (htemp.containsKey(cfactoryid)) || 
          (!hfactoryid.containsKey(bodyVO[i].getPrimaryKey()))) continue;
        dmo.sendInfoToMRP(corp, billcode, operid, type.intValue());
        htemp.put(cfactoryid, cfactoryid);
      }
  }

  private String[][] queryInData(String sql)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    String[][] s = new String[0][0];
    Vector vResult = new Vector();
    ResultSetMetaData rsmd = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      rsmd = rs.getMetaData();
      while (rs.next()) {
        String[] sone = null;
        Vector record = new Vector();
        for (int i = 1; i <= rsmd.getColumnCount(); i++)
        {
          Object str = rs.getObject(i);
          if (rs.wasNull()) {
            record.addElement(null);
          }
          else {
            record.addElement(str.toString());
          }
        }
        sone = new String[record.size()];
        record.copyInto(sone);
        vResult.addElement(sone);
      }
      rs.close();
      stmt.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    if (vResult.size() != 0) {
      s = new String[vResult.size()][rsmd.getColumnCount()];
      vResult.copyInto(s);
    }

    return s;
  }

  public UFDouble querySOIcgenMny(ParamVO voCond)
    throws SQLException
  {
    beforeCallMethod("nc.bs.arap.inter.Arap2SoDMO", "querySOInvoiceMny", new Object[] { voCond });

    String[][] sFlds = { { "pk_corp", "deptid", "cemployeeid", "warehouseid", "calbodyid", "pk_invmandoc", "batchname", "startDate", "endDate", "pk_invcl" }, { "arap_djzb zb", "arap_djfb fb", "arap_djfb fb", "ic_general_h iczb", "ic_general_h iczb", "arap_djfb fb", "ic_general_b icfb", "arap_djzb zb", "arap_djzb zb", "bd_invbasdoc chda" }, { "zb.dwbm=", "fb.deptid=", "fb.ywybm=", "iczb.cwarehouseid=", "iczb.pk_calbody=", "fb.chbm_cl=", "icfb.vbatchcode", "zb.djrq>=", "zb.djrq<=", "chda.pk_invcl=" }, { "", "", "", " on fb.ddlx= iczb.cgeneralhid", " on fb.ddlx = iczb.cgeneralhid", " on fb.Ddhh= icfb.cgeneralbid ", " on fb.Ddhh= icfb.cgeneralbid ", "", "", " on fb.cinventoryid=chda.pk_invbasdoc " } };

    String strSql = "select sum(fb.Wbfbbje) from arap_djzb zb left outer join arap_djfb fb on zb.vouchid = fb.vouchid ";

    if (voCond.getFreeItemGroupParam().booleanValue()) {
      strSql = strSql + " left outer join so_square_b so_b on so_b.corder_bid = fb.ddhh ";
    }

    String strWhere = " zb.djdl='ys' and zb.lybz=3 and fb.jsfsbm<>'34' and zb.dr=0 and fb.dr=0 and fb.Jsfsbm='4C'";
    Hashtable hash = new Hashtable();
    hash.put("arap_djzb zb", "arap_djzb zb");
    hash.put("arap_djfb fb", "arap_djfb fb");
    for (int i = 0; i < sFlds[0].length; i++) {
      if ((voCond.getAttributeValue(sFlds[0][i]) == null) || (((String)voCond.getAttributeValue(sFlds[0][i])).length() <= 0)) {
        continue;
      }
      Object sTab = hash.get(sFlds[1][i]);
      if ((sTab == null) || (((String)sTab).length() <= 0)) {
        hash.put(sFlds[1][i], sFlds[1][i]);
        strSql = strSql + " left outer join " + sFlds[1][i] + sFlds[3][i];
      }
      if (strWhere.length() > 0) {
        strWhere = strWhere + " and ";
      }
      if (((String)voCond.getAttributeValue(sFlds[0][i])).equalsIgnoreCase("null"))
      {
        String sTemp = "";
        if ((sFlds[2][i].indexOf("<=") != -1) || (sFlds[2][i].indexOf(">=") != -1))
        {
          sTemp = sFlds[2][i].substring(0, sFlds[2][i].length() - 2);
        }
        else {
          sTemp = sFlds[2][i].substring(0, sFlds[2][i].length() - 1);
        }
        strWhere = strWhere + sTemp + " is null ";
      }
      else if (sFlds[0][i].equals("batchname")) {
        if (voCond.getOP(sFlds[0][i]).equals("like")) {
          strWhere = strWhere + sFlds[2][i] + " like'" + (String)voCond.getAttributeValue(sFlds[0][i]) + "%'";
        }
        else
        {
          strWhere = strWhere + sFlds[2][i] + "='" + (String)voCond.getAttributeValue(sFlds[0][i]) + "'";
        }

      }
      else
      {
        strWhere = strWhere + sFlds[2][i] + "'" + (String)voCond.getAttributeValue(sFlds[0][i]) + "'";
      }

    }

    if (voCond.getFreeItemGroupParam().booleanValue())
    {
      if (voCond.getAttributeValue("vfree1") == null) {
        strWhere = strWhere + " and so_b.vfree1 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree1 = '" + voCond.getAttributeValue("vfree1") + "'";
      }

      if (voCond.getAttributeValue("vfree2") == null) {
        strWhere = strWhere + " and so_b.vfree2 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree2 = '" + voCond.getAttributeValue("vfree2") + "'";
      }

      if (voCond.getAttributeValue("vfree3") == null) {
        strWhere = strWhere + " and so_b.vfree3 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree3 = '" + voCond.getAttributeValue("vfree3") + "'";
      }

      if (voCond.getAttributeValue("vfree4") == null) {
        strWhere = strWhere + " and so_b.vfree4 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree4 = '" + voCond.getAttributeValue("vfree4") + "'";
      }

      if (voCond.getAttributeValue("vfree5") == null) {
        strWhere = strWhere + " and so_b.vfree5 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree5 = '" + voCond.getAttributeValue("vfree5") + "'";
      }
    }

    if (strWhere.length() > 0) {
      strSql = strSql + " where " + strWhere;
    }
    Connection con = null;
    PreparedStatement stmt = null;
    Vector vRe = new Vector();
    UFDouble ufResult = new UFDouble();
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Object obj = rs.getObject(1);
        if (obj == null) {
          ufResult = new UFDouble("0.0");
        }
        else
          ufResult = new UFDouble(obj.toString());
      }
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.arap.inter.Arap2SoDMO", "querySOInvoiceMny", new Object[] { voCond });

    return ufResult;
  }

  public UFDouble querySOInvoiceMny(ParamVO voCond)
    throws SQLException
  {
    beforeCallMethod("nc.bs.arap.inter.Arap2SoDMO", "querySOInvoiceMny", new Object[] { voCond });

    String[][] sFlds = { { "pk_corp", "deptid", "cemployeeid", "warehouseid", "calbodyid", "pk_invmandoc", "batchname", "startDate", "endDate", "pk_invcl" }, { "arap_djzb zb", "arap_djfb fb", "arap_djfb fb", "so_saleinvoice_b sofb", "so_saleinvoice sozb", "arap_djfb fb", "so_saleinvoice_b sofb", "arap_djzb zb", "arap_djzb zb", "bd_invbasdoc chda" }, { "zb.dwbm=", "fb.deptid=", "fb.ywybm=", "sofb.cbodywarehouseid=", "sozb.ccalbodyid=", "fb.chbm_cl=", "sofb.cbatchid", "zb.djrq>=", "zb.djrq<=", "chda.pk_invcl=" }, { "", "", "", " on fb.Ddhh= sofb.cinvoice_bid ", " on fb.ddlx = sozb.csaleid", " on fb.Ddhh= sofb.cinvoice_bid ", " on fb.Ddhh= sofb.cinvoice_bid ", "", "", " on fb.cinventoryid=chda.pk_invbasdoc " } };

    String strSql = "select sum(fb.Wbfbbje) from arap_djzb zb left outer join arap_djfb fb on zb.vouchid = fb.vouchid ";

    if (voCond.getFreeItemGroupParam().booleanValue()) {
      strSql = strSql + " left outer join so_square_b so_b on so_b.corder_bid = fb.ddhh ";
    }

    String strWhere = " zb.djdl='ys' and zb.lybz=3 and fb.jsfsbm<>'34' and zb.dr=0 and fb.dr=0 ";

    Hashtable hash = new Hashtable();
    hash.put("arap_djzb zb", "arap_djzb zb");
    hash.put("arap_djfb fb", "arap_djfb fb");
    for (int i = 0; i < sFlds[0].length; i++) {
      if ((voCond.getAttributeValue(sFlds[0][i]) == null) || (((String)voCond.getAttributeValue(sFlds[0][i])).length() <= 0)) {
        continue;
      }
      Object sTab = hash.get(sFlds[1][i]);
      if ((sTab == null) || (((String)sTab).length() <= 0)) {
        hash.put(sFlds[1][i], sFlds[1][i]);
        strSql = strSql + " left outer join " + sFlds[1][i] + sFlds[3][i];
      }
      if (strWhere.length() > 0) {
        strWhere = strWhere + " and ";
      }
      if (((String)voCond.getAttributeValue(sFlds[0][i])).equalsIgnoreCase("null"))
      {
        String sTemp = "";
        if ((sFlds[2][i].indexOf("<=") != -1) || (sFlds[2][i].indexOf(">=") != -1))
        {
          sTemp = sFlds[2][i].substring(0, sFlds[2][i].length() - 2);
        }
        else {
          sTemp = sFlds[2][i].substring(0, sFlds[2][i].length() - 1);
        }
        strWhere = strWhere + sTemp + " is null ";
      }
      else if (sFlds[0][i].equals("batchname")) {
        if (voCond.getOP(sFlds[0][i]).equals("like")) {
          strWhere = strWhere + sFlds[2][i] + " like'" + (String)voCond.getAttributeValue(sFlds[0][i]) + "%'";
        }
        else
        {
          strWhere = strWhere + sFlds[2][i] + "='" + (String)voCond.getAttributeValue(sFlds[0][i]) + "'";
        }

      }
      else
      {
        strWhere = strWhere + sFlds[2][i] + "'" + (String)voCond.getAttributeValue(sFlds[0][i]) + "'";
      }

    }

    if (voCond.getFreeItemGroupParam().booleanValue())
    {
      if (voCond.getAttributeValue("vfree1") == null) {
        strWhere = strWhere + " and so_b.vfree1 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree1 = '" + voCond.getAttributeValue("vfree1") + "'";
      }

      if (voCond.getAttributeValue("vfree2") == null) {
        strWhere = strWhere + " and so_b.vfree2 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree2 = '" + voCond.getAttributeValue("vfree2") + "'";
      }

      if (voCond.getAttributeValue("vfree3") == null) {
        strWhere = strWhere + " and so_b.vfree3 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree3 = '" + voCond.getAttributeValue("vfree3") + "'";
      }

      if (voCond.getAttributeValue("vfree4") == null) {
        strWhere = strWhere + " and so_b.vfree4 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree4 = '" + voCond.getAttributeValue("vfree4") + "'";
      }

      if (voCond.getAttributeValue("vfree5") == null) {
        strWhere = strWhere + " and so_b.vfree5 is null ";
      }
      else {
        strWhere = strWhere + " and so_b.vfree5 = '" + voCond.getAttributeValue("vfree5") + "'";
      }
    }

    if (strWhere.length() > 0) {
      strSql = strSql + " where " + strWhere;
    }
    Connection con = null;
    PreparedStatement stmt = null;
    Vector vRe = new Vector();
    UFDouble ufResult = new UFDouble();
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Object obj = rs.getObject(1);
        if (obj == null) {
          ufResult = new UFDouble("0.0");
        }
        else
          ufResult = new UFDouble(obj.toString());
      }
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.arap.inter.Arap2SoDMO", "querySOInvoiceMny", new Object[] { voCond });

    return ufResult;
  }

  public UFDouble querySoOutMny(ParamVO voCond)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.pub.OtherInterfaceDMO", "querySoOutMny()", new Object[] { voCond });

    String[][] sFlds = { { "pk_corp", "deptid", "cemployeeid", "warehouseid", "calbodyid", "pk_invmandoc", "batchname", "startDate", "endDate", "pk_invcl" }, { "zb.dwbm=", "fb.deptid=", "fb.ywybm=", "iczb.cwarehouseid=", "iczb.pk_calbody=", "fb.chbm_cl=", "icfb.vbatchcode", "zb.djrq>=", "zb.djrq<=", "chda.pk_invcl=" } };

    StringBuffer sSql = new StringBuffer("select sum(fb.Wbfbbje) from arap_djzb zb left outer join arap_djfb fb on zb.vouchid = fb.vouchid ");

    sSql.append(" left outer join to_settlelist_b set_b on ( set_b.csettlelistid=fb.ddlx and  fb.Ddhh = set_b.csettlelist_bid ) left outer join ic_general_h iczb on set_b.csourceid=iczb.cgeneralhid ");

    sSql.append(" left outer join ic_general_b icfb on set_b.csourcebid=icfb.cgeneralbid ");

    sSql.append(" left outer join bd_invbasdoc chda on fb.cinventoryid=chda.pk_invbasdoc ");

    String sWhere = " zb.djdl='ys' and zb.lybz=3 and fb.jsfsbm<>'34' and zb.dr=0 and fb.dr=0 and fb.Jsfsbm='5F'";

    for (int i = 0; i < sFlds[0].length; i++) {
      if ((voCond.getAttributeValue(sFlds[0][i]) == null) || (((String)voCond.getAttributeValue(sFlds[0][i])).length() <= 0)) {
        continue;
      }
      sWhere = sWhere + " and ";

      if (((String)voCond.getAttributeValue(sFlds[0][i])).equalsIgnoreCase("null"))
      {
        String sTemp = "";
        if ((sFlds[1][i].indexOf("<=") != -1) || (sFlds[1][i].indexOf(">=") != -1))
        {
          sTemp = sFlds[1][i].substring(0, sFlds[1][i].length() - 2);
        }
        else {
          sTemp = sFlds[1][i].substring(0, sFlds[1][i].length() - 1);
        }
        sWhere = sWhere + sTemp + " is null ";
      }
      else if (sFlds[0][i].equals("batchname")) {
        if (voCond.getOP(sFlds[0][i]).equals("like")) {
          sWhere = sWhere + sFlds[1][i] + " like'" + (String)voCond.getAttributeValue(sFlds[0][i]) + "%'";
        }
        else
        {
          sWhere = sWhere + sFlds[1][i] + "='" + (String)voCond.getAttributeValue(sFlds[0][i]) + "'";
        }

      }
      else
      {
        sWhere = sWhere + sFlds[1][i] + "'" + (String)voCond.getAttributeValue(sFlds[0][i]) + "'";
      }

    }

    if (sWhere.length() > 0) {
      sSql.append(" where " + sWhere);
    }

    Connection con = null;
    PreparedStatement stmt = null;
    Vector vRe = new Vector();
    UFDouble ufResult = new UFDouble();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql.toString());
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Object obj = rs.getObject(1);
        if (obj == null) {
          ufResult = new UFDouble("0.0");
        }
        else
          ufResult = new UFDouble(obj.toString());
      }
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }

    }

    afterCallMethod("nc.bs.so.pub.OtherInterfaceDMO", "querySoOutMny", new Object[] { voCond });

    return ufResult;
  }

  public void setDecreaseSaleOut(AggregatedValueObject billVO)
    throws SQLException, BusinessException, RemoteException
  {
    IIC215GeneralH clsSaleOut = (IIC215GeneralH)NCLocator.getInstance().lookup(IIC215GeneralH.class.getName());

    SCMEnv.out("回写借出转销售的定购数量");

    String strMsg = null;
    for (int i = 0; i < billVO.getChildrenVO().length; i++) {
      CircularlyAccessibleValueObject bodyVO = billVO.getChildrenVO()[i];
      if (bodyVO.getAttributeValue("creceipttype") == null) {
        continue;
      }
      if ((!bodyVO.getAttributeValue("creceipttype").equals("40")) && (!bodyVO.getAttributeValue("creceipttype").equals("4H")) && (!bodyVO.getAttributeValue("creceipttype").equals("42")))
      {
        continue;
      }
      clsSaleOut.setOutNum(bodyVO.getAttributeValue("csourcebillid").toString(), bodyVO.getAttributeValue("csourcebillbodyid").toString(), new UFDouble(0).sub((UFDouble)bodyVO.getAttributeValue("nnumber")));
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void setSaleCT(AggregatedValueObject billVO)
    throws SQLException, BusinessException, NamingException, SystemException, RemoteException
  {
    String spkcorp = null;
    if ((billVO != null) && (billVO.getParentVO() != null))
      spkcorp = billVO.getParentVO().getAttributeValue("pk_corp").toString();
    else {
      return;
    }
    if (!checkCTisExist(spkcorp)) {
      return;
    }

    SCMEnv.out("回写销售合同的定购数量");

    if (billVO == null) {
      return;
    }

    if (billVO.getChildrenVO() == null) {
      return;
    }

    if (billVO.getChildrenVO().length == 0) {
      return;
    }

    SaleOrderDMO dmo = new SaleOrderDMO();
    ParaPoToCtRewriteVO[] paravo = dmo.getChangeFromSaleVO((SaleOrderVO)billVO);

    if (paravo != null)
      try
      {
        ICtToPo_BackToCt clsSaleCT = (ICtToPo_BackToCt)NCLocator.getInstance().lookup(ICtToPo_BackToCt.class.getName());

        if (clsSaleCT == null) {
          return;
        }

        clsSaleCT.writeBackAccuOrdData(paravo);
      }
      catch (ProdNotInstallException e)
      {
        SCMEnv.out(e.getMessage());
      }
      catch (Exception e) {
        reportException(e);
        throw new BusinessException(e.getMessage());
      }
  }

  public void setSaleOut(AggregatedValueObject billVO)
    throws SQLException, BusinessException, RemoteException
  {
    IIC215GeneralH clsSaleOut = (IIC215GeneralH)NCLocator.getInstance().lookup(IIC215GeneralH.class.getName());

    SCMEnv.out("回写借出转销售的定购数量");

    String strMsg = null;
    for (int i = 0; i < billVO.getChildrenVO().length; i++) {
      CircularlyAccessibleValueObject bodyVO = billVO.getChildrenVO()[i];

      if (bodyVO.getAttributeValue("creceipttype") == null)
        continue;
      if ((!bodyVO.getAttributeValue("creceipttype").equals("40")) && (!bodyVO.getAttributeValue("creceipttype").equals("4H")) && (!bodyVO.getAttributeValue("creceipttype").equals("42")))
      {
        continue;
      }
      clsSaleOut.setOutNum(bodyVO.getAttributeValue("csourcebillid").toString(), bodyVO.getAttributeValue("csourcebillbodyid").toString(), (UFDouble)bodyVO.getAttributeValue("nnumber"));
    }

    if (strMsg != null) {
      BusinessException e = new BusinessException(strMsg);
      throw e;
    }
  }

  public void checkATP(SaleOrderVO ordvo, boolean bCheckOther)
    throws Exception
  {
  }

  private UFDouble getCorpATPNum(SaleorderBVO bvo, String cinventoryid1)
  {
    if ((bvo == null) || (this.hsCorpAtpNum == null) || (cinventoryid1 == null) || (cinventoryid1.trim().length() == 0))
    {
      return null;
    }

    String key = "";

    key = key + cinventoryid1;

    if ((bvo.getVfree1() != null) && (bvo.getVfree1().trim().length() <= 0)) {
      key = key + null;
    }
    else {
      key = key + bvo.getVfree1();
    }

    if ((bvo.getVfree2() != null) && (bvo.getVfree2().trim().length() <= 0)) {
      key = key + null;
    }
    else {
      key = key + bvo.getVfree2();
    }

    if ((bvo.getVfree3() != null) && (bvo.getVfree3().trim().length() <= 0)) {
      key = key + null;
    }
    else {
      key = key + bvo.getVfree3();
    }

    if ((bvo.getVfree4() != null) && (bvo.getVfree4().trim().length() <= 0)) {
      key = key + null;
    }
    else {
      key = key + bvo.getVfree4();
    }

    if ((bvo.getVfree5() != null) && (bvo.getVfree5().trim().length() <= 0)) {
      key = key + null;
    }
    else {
      key = key + bvo.getVfree5();
    }

    key = key + bvo.getDconsigndate();

    return (UFDouble)this.hsCorpAtpNum.get(key);
  }

  private HashMap getCorpATPNums(SaleorderBVO[] bvos, String SO32)
    throws Exception
  {
    if ((SO32 == null) || (SO32.trim().length() <= 0) || (bvos == null) || (bvos.length <= 0))
    {
      return this.hsCorpAtpNum;
    }

    if (this.hsCorpAtpNum == null)
    {
      this.hsCorpAtpNum = new HashMap();

      ArrayList atpparams = new ArrayList();
      ArrayList alist = null;

      String key = null;

      HashMap so32inventory = getHsSo32inventory(bvos, SO32);

      String[] vfrees = null;
      String invid = null;

      HashMap hslisttorow = new HashMap();

      int i = 0; for (int loop = bvos.length; i < loop; i++) {
        if ((bvos[i] == null) || (bvos[i].getStatus() == 3) || ((bvos[i].getBoosflag() != null) && (bvos[i].getBoosflag().booleanValue())) || ((bvos[i].getBsupplyflag() != null) && (bvos[i].getBsupplyflag().booleanValue())) || ((bvos[i].getLaborflag() != null) && (bvos[i].getLaborflag().booleanValue())) || ((bvos[i].getDiscountflag() != null) && (bvos[i].getDiscountflag().booleanValue())))
        {
          continue;
        }

        if (bvos[i].getCinvbasdocid() == null)
        {
          continue;
        }
        invid = (String)so32inventory.get(bvos[i].getCinvbasdocid());

        if ((invid == null) || (invid.trim().length() == 0))
        {
          continue;
        }
        key = "";

        key = key + invid;

        vfrees = new String[5];

        if ((bvos[i].getVfree1() != null) && (bvos[i].getVfree1().trim().length() == 0))
        {
          vfrees[0] = null;
        }
        else {
          vfrees[0] = bvos[i].getVfree1();
        }

        key = key + vfrees[0];

        if ((bvos[i].getVfree2() != null) && (bvos[i].getVfree2().trim().length() == 0))
        {
          vfrees[1] = null;
        }
        else {
          vfrees[1] = bvos[i].getVfree2();
        }

        key = key + vfrees[1];

        if ((bvos[i].getVfree3() != null) && (bvos[i].getVfree3().trim().length() == 0))
        {
          vfrees[2] = null;
        }
        else {
          vfrees[2] = bvos[i].getVfree3();
        }

        key = key + vfrees[2];

        if ((bvos[i].getVfree4() != null) && (bvos[i].getVfree4().trim().length() == 0))
        {
          vfrees[3] = null;
        }
        else {
          vfrees[3] = bvos[i].getVfree4();
        }

        key = key + vfrees[3];

        if ((bvos[i].getVfree5() != null) && (bvos[i].getVfree5().trim().length() == 0))
        {
          vfrees[4] = null;
        }
        else {
          vfrees[4] = bvos[i].getVfree5();
        }

        key = key + vfrees[4];

        key = key + bvos[i].getDconsigndate();

        if ((this.hsCorpAtpNum.size() > 0) && (this.hsCorpAtpNum.keySet().contains(key)))
        {
          continue;
        }
        alist = new ArrayList();

        alist.add(SO32);
        alist.add(null);
        alist.add(invid);
        alist.add(vfrees);
        alist.add(bvos[i].getDconsigndate() == null ? null : bvos[i].getDconsigndate().toString());

        atpparams.add(alist);

        hslisttorow.put(alist, key);
      }

      if (atpparams.size() > 0)
      {
        UFDouble[] aptnums = null;

        if (aptnums != null) {
          for (int i1=0;i1< aptnums.length; i1++) {
            key = (String)hslisttorow.get(atpparams.get(i1));
            if ((key != null) && (aptnums[i1] != null)) {
              this.hsCorpAtpNum.put(key, aptnums[i1]);
            }
          }
        }
      }

    }

    return this.hsCorpAtpNum;
  }

  private HashMap getHsSo32inventory(SaleorderBVO[] bvos, String SO32)
    throws Exception
  {
    if (this.hsSo32inventory == null)
    {
      if ((bvos == null) || (bvos.length < 0) || (SO32 == null) || (SO32.length() <= 0))
      {
        return null;
      }

      try
      {
        this.hsSo32inventory = SOToolsDMO.getAnyValue("bd_invmandoc", "pk_invmandoc", "pk_invbasdoc", SoVoTools.getVOsOnlyValues(bvos, "cinvbasdocid"), " pk_corp = '" + SO32.trim() + "' ");
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
        this.hsSo32inventory = null;
      }
    }
    return this.hsSo32inventory;
  }

  public Object[] getSqlPiece(ParamVO condition)
    throws Exception
  {
    Object[] sReturn = new Object[11];
    String sSqlVia = "";
    String sSqlSo = "";
    String sSqlAp = "";
    boolean bwhere = false;

    Object oInvID = condition.getAttributeValue("pk_invmandoc");
    Object oInvCLCode = condition.getAttributeValue("invclcode");
    sSqlVia = sSqlVia + " join bd_invmandoc invm on invm.pk_invmandoc= v.cinventoryid " + " join bd_invbasdoc invb on invb.pk_invbasdoc=invm.pk_invbasdoc " + " join bd_invcl invc on invc.pk_invcl=invb.pk_invcl ";

    sSqlSo = sSqlSo + " join bd_invmandoc invm on invm.pk_invmandoc= sob.cinventoryid " + " join bd_invbasdoc invb on invb.pk_invbasdoc=invm.pk_invbasdoc " + " join bd_invcl invc on invc.pk_invcl=invb.pk_invcl ";

    sSqlAp = sSqlAp + " join bd_invmandoc invm on invm.pk_invmandoc= apb.cinventoryid " + " join bd_invbasdoc invb on invb.pk_invbasdoc=invm.pk_invbasdoc " + " join bd_invcl invc on invc.pk_invcl=invb.pk_invcl ";

    if ((oInvCLCode != null) && (oInvCLCode.toString().trim().length() != 0)) {
      sSqlVia = sSqlVia + " where invc.invclasscode like '" + oInvCLCode + "%' and ";

      sSqlSo = sSqlSo + " where invc.invclasscode like '" + oInvCLCode + "%' and ";

      sSqlAp = sSqlAp + " where invc.invclasscode like '" + oInvCLCode + "%' and ";

      bwhere = true;
    }

    if ((oInvID != null) && (oInvID.toString().trim().length() != 0)) {
      if (!bwhere) {
        sSqlVia = sSqlVia + " where ";
        sSqlSo = sSqlSo + " where ";
        sSqlAp = sSqlAp + " where ";
        bwhere = true;
      }

      sSqlVia = sSqlVia + "  v.cinventoryid='" + oInvID + "' and ";
      sSqlSo = sSqlSo + "  sob.cinventoryid='" + oInvID + "' and ";
      sSqlAp = sSqlAp + "  apb.cinventoryid='" + oInvID + "' and ";
    }

    Object oBatch = condition.getAttributeValue("batchname");
    String sBatchOper = condition.getOP("batchname");

    if ((oBatch != null) && (oBatch.toString().trim().length() != 0))
    {
      if (!bwhere) {
        sSqlVia = sSqlVia + " where ";
        sSqlSo = sSqlSo + " where ";
        sSqlAp = sSqlAp + " where ";
        bwhere = true;
      }

      if (sBatchOper.equals("=")) {
        sSqlVia = sSqlVia + "  v.vbatch = '" + oBatch + "' and ";
        sSqlSo = sSqlSo + "  sob.cbatchid = '" + oBatch + "' and ";
        sSqlAp = sSqlAp + "  apb.cbatchid = '" + oBatch + "' and ";
      }

      if (sBatchOper.equals("like")) {
        sSqlVia = sSqlVia + "  v.vbatch  like'" + oBatch + "%' and ";
        sSqlSo = sSqlSo + "  sob.cbatchid  like'" + oBatch + "%' and ";
        sSqlAp = sSqlAp + "  apb.cbatchid  like'" + oBatch + "%' and ";
      }

    }

    Object oDeptID = condition.getAttributeValue("deptid");
    Object oDeptName = condition.getAttributeValue("deptname");

    if ((oDeptID != null) && (oDeptID.toString().trim().length() != 0))
    {
      if (!bwhere) {
        sSqlVia = sSqlVia + " where ";
        sSqlSo = sSqlSo + " where ";
        sSqlAp = sSqlAp + " where ";
        bwhere = true;
      }

      sSqlVia = sSqlVia + "   v.cdeptid='" + oDeptID + "' and ";
      sSqlSo = sSqlSo + "   so.cdeptid='" + oDeptID + "' and ";
      sSqlAp = sSqlAp + "   ap.cdeptid='" + oDeptID + "' and ";
    }

    Object oEmployeeID = condition.getAttributeValue("cemployeeid");
    Object oEmployeeName = condition.getAttributeValue("cemployeename");

    if ((oEmployeeID != null) && (oEmployeeID.toString().trim().length() != 0)) {
      if (!bwhere) {
        sSqlVia = sSqlVia + " where ";
        sSqlSo = sSqlSo + " where ";
        sSqlAp = sSqlAp + " where ";

        bwhere = true;
      }

      sSqlVia = sSqlVia + "  v.cemployeeid='" + oEmployeeID + "' and ";
      sSqlSo = sSqlSo + "  so.cemployeeid='" + oEmployeeID + "' and ";
      sSqlAp = sSqlAp + "  ap.cemployeeid='" + oEmployeeID + "' and ";
    }

    Object oStorID = condition.getAttributeValue("warehouseid");
    Object oStorName = condition.getAttributeValue("warehousename");

    if ((oStorID != null) && (oStorID.toString().trim().length() != 0)) {
      if (!bwhere) {
        sSqlVia = sSqlVia + " where ";
        sSqlSo = sSqlSo + " where ";
        sSqlAp = sSqlAp + " where ";
        bwhere = true;
      }

      sSqlVia = sSqlVia + " v.cwarehouseid='" + oStorID + "' and ";
      sSqlSo = sSqlSo + " sob.cbodywarehouseid='" + oStorID + "' and ";
      sSqlAp = sSqlAp + " apb.cbodywarehouseid='" + oStorID + "' and ";
    }

    Object oCalbodyID = condition.getAttributeValue("calbodyid");
    Object oCalbodyName = condition.getAttributeValue("calbodyname");

    if ((oCalbodyID != null) && (oCalbodyID.toString().trim().length() != 0)) {
      if (!bwhere) {
        sSqlVia = sSqlVia + " where ";
        sSqlSo = sSqlSo + " where ";
        sSqlAp = sSqlAp + " where ";

        bwhere = true;
      }

      sSqlVia = sSqlVia + "  v.crdcenterid='" + oCalbodyID + "' and ";
      sSqlSo = sSqlSo + "  isnull( sob.creccalbodyid,sob.cadvisecalbodyid )='" + oCalbodyID + "' and ";
      sSqlAp = sSqlAp + "  apb.ccalbodyid='" + oCalbodyID + "' and ";
    }

    Object oCurID = condition.getAttributeValue("custid");
    Object oCurName = condition.getAttributeValue("custname");

    if ((oCurID != null) && (oCurID.toString().trim().length() != 0)) {
      if (!bwhere) {
        sSqlVia = sSqlVia + " where ";
        sSqlSo = sSqlSo + " where ";
        sSqlAp = sSqlAp + " where ";

        bwhere = true;
      }

      sSqlVia = sSqlVia + " v.ccustomvendorid='" + oCurID + "' and ";
      sSqlSo = sSqlSo + " so.ccustomerid='" + oCurID + "' and ";
      sSqlAp = sSqlAp + " ap.ccustomerid='" + oCurID + "' and ";
    }

    Object oBusiID = condition.getAttributeValue("cbiztypeid");
    Object oBusiName = condition.getAttributeValue("cbiztypename");

    if ((oBusiID != null) && (oBusiID.toString().trim().length() != 0))
    {
      if (!bwhere) {
        sSqlVia = sSqlVia + " where ";
        sSqlSo = sSqlSo + " where ";
        sSqlAp = sSqlAp + " where ";

        bwhere = true;
      }

      sSqlVia = sSqlVia + " v.cbiztypeid='" + oBusiID + "' and ";
      sSqlSo = sSqlSo + " so.cbiztype='" + oBusiID + "' and ";
      sSqlAp = sSqlAp + " ap.cbiztype='" + oBusiID + "' and ";
    }

    String spk_corp = condition.getAttributeValue("pk_corp").toString();

    if (!bwhere) {
      sSqlVia = sSqlVia + " where v.pk_corp='" + spk_corp + "'";
      sSqlSo = sSqlSo + " where so.pk_corp='" + spk_corp + "'";
      sSqlAp = sSqlAp + " where ap.pk_corp='" + spk_corp + "'";
    }
    else
    {
      sSqlVia = sSqlVia + "  v.pk_corp='" + spk_corp + "'";
      sSqlSo = sSqlSo + "  so.pk_corp='" + spk_corp + "'";
      sSqlAp = sSqlAp + "  ap.pk_corp='" + spk_corp + "'";
    }

    String[] sGroups = (String[])(String[])condition.getAttributeValue("groupConditons");

    Vector vNewColCreate = new Vector();

    String sNewColInsert = "";
    String sSelVia = "";
    String sSelSo = "";
    String sSelAp = "";

    String sOutField = "";
    String sOutTable = "";

    Vector vAttrs = new Vector();

    for (int i = 0; i < sGroups.length; i++) {
      if (sGroups[i].equals("存货"))
      {
        sNewColInsert = sNewColInsert + "cinventoryid,";

        sSelVia = sSelVia + "v.cinventoryid,";
        sSelSo = sSelSo + "sob.cinventoryid,";
        sSelAp = sSelAp + "apb.cinventoryid,";

        sOutField = sOutField + "invb.invcode,invb.invname,invb.invspec || invb.invtype,mea.measname,cinventoryid,";

        sOutTable = sOutTable + " inner join bd_invmandoc invm ";
        sOutTable = sOutTable + " on ";
        sOutTable = sOutTable + " cinventoryid = invm.pk_invmandoc ";
        sOutTable = sOutTable + " inner join bd_invbasdoc invb ";
        sOutTable = sOutTable + " on ";
        sOutTable = sOutTable + " invm.pk_invbasdoc = invb.pk_invbasdoc ";
        sOutTable = sOutTable + " inner join bd_measdoc mea ";
        sOutTable = sOutTable + " on ";
        sOutTable = sOutTable + " invb.pk_measdoc = mea.pk_measdoc ";

        vAttrs.addElement("invcode");
        vAttrs.addElement("invname");
        vAttrs.addElement("invmodel");
        vAttrs.addElement("unitname");
        vAttrs.addElement("pk_invmandoc");

        if ((condition.getFreeItemGroupParam() == null) || (!condition.getFreeItemGroupParam().booleanValue()))
          continue;
        vNewColCreate.addElement("vfree1 char(20)");
        vNewColCreate.addElement("vfree2 char(20)");
        vNewColCreate.addElement("vfree3 char(20)");
        vNewColCreate.addElement("vfree4 char(20)");
        vNewColCreate.addElement("vfree5 char(20)");

        sNewColInsert = sNewColInsert + "vfree1,vfree2,vfree3,vfree4,vfree5,";

        sSelVia = sSelVia + "v.vfree1,v.vfree2,v.vfree3,v.vfree4,v.vfree5,";

        sSelSo = sSelSo + "soexe.vfree1,soexe.vfree2,soexe.vfree3,soexe.vfree4,soexe.vfree5,";

        sSelAp = sSelAp + "apb.vfree1,apb.vfree2,apb.vfree3,apb.vfree4,apb.vfree5,";

        sOutField = sOutField + "vfree1,vfree2,vfree3,vfree4,vfree5,";

        vAttrs.addElement("vfree1");
        vAttrs.addElement("vfree2");
        vAttrs.addElement("vfree3");
        vAttrs.addElement("vfree4");
        vAttrs.addElement("vfree5");
      }
      else if (sGroups[i].equals("存货类"))
      {
        vNewColCreate.addElement("pk_invcl char(20)");
        sNewColInsert = sNewColInsert + "cinvclid,";

        sSelVia = sSelVia + "invc.pk_invcl,";
        sSelSo = sSelSo + "invc.pk_invcl,";
        sSelAp = sSelAp + "invc.pk_invcl,";

        sOutField = sOutField + "invc.invclasscode,invc.invclassname,cinvclid,";
        sOutTable = sOutTable + " join bd_invcl invc on invc.pk_invcl=cinvclid ";

        vAttrs.addElement("invclasscode");
        vAttrs.addElement("invclassname");
        vAttrs.addElement("cinvclid");
      }
      else if (sGroups[i].equals("部门"))
      {
        vNewColCreate.addElement("cdeptid char(20)");

        sNewColInsert = sNewColInsert + "cdeptid,";

        sSelVia = sSelVia + "v.cdeptid,";
        sSelSo = sSelSo + "so.cdeptid,";
        sSelAp = sSelAp + "ap.cdeptid,";

        sOutField = sOutField + "dep.deptname,cdeptid,";

        sOutTable = sOutTable + " left outer join bd_deptdoc dep on cdeptid = dep.pk_deptdoc";

        vAttrs.addElement("deptname");
        vAttrs.addElement("cdeptid");
      }
      else if (sGroups[i].equals("仓库"))
      {
        vNewColCreate.addElement("cwarehouseid char(20)");

        sNewColInsert = sNewColInsert + "cwarehouseid,";

        sSelVia = sSelVia + "v.cwarehouseid,";
        sSelSo = sSelSo + "sob.cbodywarehouseid,";
        sSelAp = sSelAp + "apb.cbodywarehouseid,";

        sOutField = sOutField + "stor.storname,cwarehouseid,";

        sOutTable = sOutTable + " left outer join bd_stordoc stor on cwarehouseid = stor.pk_stordoc";

        vAttrs.addElement("warehousename");
        vAttrs.addElement("cwarehouseid");
      }
      else {
        if (!sGroups[i].equals("批次")) {
          continue;
        }
        vNewColCreate.addElement("cbatchid char(20)");

        sNewColInsert = sNewColInsert + "cbatchid,";

        sSelVia = sSelVia + "v.vbatch,";
        sSelSo = sSelSo + "sob.cbatchid,";
        sSelAp = sSelAp + "apb.cbatchid,";

        sOutField = sOutField + "cbatchid,";

        vAttrs.addElement("batchname");
      }

    }

    String sNewColCreate = "";
    for (int i = 0; i < vNewColCreate.size(); i++) {
      sNewColCreate = sNewColCreate + vNewColCreate.elementAt(i) + ",";
    }

    sReturn[0] = sNewColCreate;
    sReturn[1] = sNewColInsert;
    sReturn[2] = sSelVia;
    sReturn[3] = sSelSo;
    sReturn[4] = sSelAp;
    sReturn[5] = sSqlVia;
    sReturn[6] = sSqlSo;
    sReturn[7] = sSqlAp;
    sReturn[8] = sOutField;
    sReturn[9] = sOutTable;
    sReturn[10] = vAttrs;
    return sReturn;
  }

  public String createTempTable()
    throws Exception
  {
    String[] columns = { "begininnum ", "beginoutnum", "begininmny", "beginoutmny", "innum", "inmny", "outnum", "outmny", "retnum", "retmny", "incomemny", "retcostmny", "calbodyid", "cinventoryid", "vbatch", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "cdeptid", "cwarehouseid", "cbatchid", "cinvclid", "costnum", "retcostnum" };

    String[] types = { "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "decimal(20,8)", "char(20)", "char(20)", "varchar(30)", "varchar(100)", "varchar(100)", "varchar(100)", "varchar(100)", "varchar(100)", "char(20)", "char(20)", "varchar(30)", "char(20)", "decimal(20,8)", "decimal(20,8)" };

    String index = null;
    String[][] datas = new String[0][0];

    TempTableDMO dmo = new TempTableDMO();
    String tableName = dmo.getTempStringTable("t_SoGroPro_v5", columns, types, index, datas);

    return tableName;
  }

  public void insertPurBefore(String sNewColInsert, String sSelVia, String sSqlPieceVia, String sBeginDate, String sTable)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into ");
    sql.append(sTable);
    sql.append(" ( ");
    sql.append(sNewColInsert);
    sql.append(" begininnum,begininmny) ");
    sql.append(" select ");
    sql.append(sSelVia);
    sql.append(" sum( coalesce(nnumber,0) ) begininnum, ");
    sql.append(" sum( coalesce(nmoney,0) )  begininmny ");
    sql.append(" from v_ia_inoutledger v  ");
    sql.append(sSqlPieceVia);
    sql.append(" and fdispatchflag = 0 ");
    sql.append(" and v.dbilldate <'" + sBeginDate + "'");
    sql.append(" and  ( coalesce(nnumber,0) != 0 ");
    sql.append(" or coalesce(nmoney,0) != 0 ) ");
    sql.append(" group by ");
    sql.append(sSelVia.substring(0, sSelVia.length() - 1));

    update(sql.toString());
  }

  public void insertPurNow(String sNewColInsert, String sSelVia, String sSqlPieceVia, String sBeginDate, String sEndDate, String sTable)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into ");
    sql.append(sTable);
    sql.append(" ( ");
    sql.append(sNewColInsert);
    sql.append(" innum,inmny) ");
    sql.append(" select ");
    sql.append(sSelVia);
    sql.append(" sum( coalesce(nnumber,0) ) innum, ");
    sql.append(" sum( coalesce(nmoney,0) )  inmny ");
    sql.append(" from v_ia_inoutledger v  ");
    sql.append(sSqlPieceVia);
    sql.append(" and fdispatchflag = 0 ");
    sql.append(" and v.dbilldate>='" + sBeginDate + "'");
    sql.append(" and v.dbilldate<='" + sEndDate + "'");
    sql.append(" and ( v.cbilltypecode in ('I2','");
    sql.append("I9','I5','");
    sql.append("IA','IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) ) ");
    sql.append(" and  ( coalesce(nnumber,0) != 0 ");
    sql.append(" or coalesce(nmoney,0) != 0 ) ");
    sql.append(" group by ");
    sql.append(sSelVia.substring(0, sSelVia.length() - 1));

    update(sql.toString());
  }

  public void insertSoNumBefore(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sBeginDate, String sTable)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" beginoutnum)  select " + sSelSo);
    sql.append(" sum( coalesce(ntotalbalancenumber,0)) beginoutnum  ");
    sql.append(" from so_saleexecute soexe, so_sale so, so_saleorder_b sob,arap_djfb b,arap_djzb a ");
    sql.append(sSqlPieceSo);
    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");
    sql.append(" and a.vouchid= b.vouchid ");

    sql.append(" and ( so.bretinvflag='N' or so.bretinvflag is null) ");
    sql.append(" and a.djrq<'" + sBeginDate + "'");
    sql.append(" and so.dr=0 and sob.dr=0 and soexe.dr=0");
    sql.append(" and coalesce(ntotalbalancenumber,0) != 0 ");
    sql.append(" and a.djdl='ys' and b.ph= '30' and b.xyzh=so.csaleid ");
    sql.append(" and b.cksqsh = sob.corder_bid ");
    sql.append(" and a.dr = 0 and b.dr = 0  and xgbh = -1 ");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));
    update(sql.toString());
  }

  public void insertTONumBefore(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sBeginDate, String sTable, boolean bsanfang)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" beginoutnum)  select " + sSelSo);
    sql.append(" sum( coalesce(norderinoutnum,0)) beginoutnum  ");
    sql.append(" from  to_bill toh, to_bill_b tob,arap_djfb b,arap_djzb a ");
    sql.append(sSqlPieceSo);
    sql.append(" and toh.cbillid=tob.cbillid ");
    sql.append(" and a.vouchid= b.vouchid ");

    sql.append(" and a.djdl='ys' and b.xyzh=toh.cbillid ");
    sql.append(" and b.cksqsh = tob.cbill_bid ");
    sql.append(" and a.dr = 0 and b.dr = 0  and xgbh = -1 and toh.dr=0 and tob.dr=0 ");
    sql.append(" and (toh.bretractflag is null or toh.bretractflag = 'N') ");
    if (bsanfang)
      sql.append(" and tob.coutcorpid <> tob.ctakeoutcorpid ");
    else {
      sql.append(" and tob.coutcorpid = tob.ctakeoutcorpid ");
    }

    sql.append(" and a.djrq<'" + sBeginDate + "'");
    sql.append(" and toh.dr=0 and tob.dr=0 ");
    sql.append(" and coalesce(norderinoutnum,0) != 0 ");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertToNumAndMnyNow(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sBeginDate, String sEndDate, String sTable, boolean bsanfang)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" outnum,incomemny ) select " + sSelSo);
    sql.append(" sum( b.jfshl) outnum, sum( b.wbfbbje) incomemny ");
    sql.append(" from ");

    sql.append(" arap_djfb b,arap_djzb a,to_bill toh, to_bill_b tob ");
    sql.append(sSqlPieceSo);
    sql.append(" and a.vouchid= b.vouchid ");
    sql.append(" and toh.cbillid=tob.cbillid ");

    sql.append(" and a.djdl='ys' and b.xyzh=toh.cbillid ");
    sql.append(" and b.cksqsh = tob.cbill_bid ");
    sql.append(" and a.dr = 0 and b.dr = 0  and xgbh = -1 and toh.dr=0 and tob.dr=0 ");
    sql.append(" and (toh.bretractflag is null or toh.bretractflag = 'N') ");
    if (bsanfang)
      sql.append(" and tob.coutcorpid <> tob.ctakeoutcorpid ");
    else {
      sql.append(" and tob.coutcorpid = tob.ctakeoutcorpid ");
    }
    sql.append(" and a.djrq >='" + sBeginDate + "'");
    sql.append(" and a.djrq <='" + sEndDate + "'");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertSoNumAndMnyNow(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sBeginDate, String sEndDate, String sTable)
    throws Exception
  {
    boolean hasFree = sNewColInsert.indexOf("vfree1") > 0;

    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" outnum,incomemny ) select " + sSelSo);
    sql.append(" sum( b.jfshl) outnum, sum( b.wbfbbje) incomemny ");
    sql.append(" from so_sale so,");
    if (hasFree) {
      sql.append(" so_saleexecute soexe, ");
    }
    sql.append(" arap_djfb b,arap_djzb a,so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append(" and a.vouchid= b.vouchid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    if (hasFree) {
      sql.append(" and soexe.csaleid = sob.csaleid ");
      sql.append(" and soexe.csale_bid = sob.corder_bid ");
      sql.append(" and soexe.csaleid = so.csaleid ");
    }

    sql.append(" and ( so.bretinvflag = 'N' or so.bretinvflag is null )");
    sql.append(" and a.djrq >='" + sBeginDate + "'");
    sql.append(" and a.djrq  <='" + sEndDate + "'");
    sql.append(" and a.djdl='ys' and b.ph= '30' and b.xyzh=so.csaleid ");
    sql.append(" and b.cksqsh = sob.corder_bid ");
    sql.append(" and so.dr = 0 and sob.dr = 0  ");
    sql.append(" and a.dr = 0 and b.dr = 0  and xgbh = -1 ");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertTONumAndMnyNow(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sBeginDate, String sEndDate, String sTable)
    throws Exception
  {
    boolean hasFree = sNewColInsert.indexOf("vfree1") > 0;

    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" outnum,incomemny ) select " + sSelSo);
    sql.append(" sum( b.jfshl) outnum, sum( b.wbfbbje) incomemny ");

    sql.append(" from arap_djfb b,arap_djzb a,to_bill_b sob,to_bill so ");
    sql.append(sSqlPieceSo);
    sql.append(" and a.vouchid= b.vouchid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    if (hasFree) {
      sql.append(" and soexe.csaleid = sob.csaleid ");
      sql.append(" and soexe.csale_bid = sob.corder_bid ");
      sql.append(" and soexe.csaleid = so.csaleid ");
    }

    sql.append(" and ( so.bretinvflag = 'N' or so.bretinvflag is null )");
    sql.append(" and so.dbilldate >='" + sBeginDate + "'");
    sql.append(" and so.dbilldate <='" + sEndDate + "'");
    sql.append(" and a.djdl='ys' and b.ph= '30' and b.xyzh=so.csaleid ");
    sql.append(" and b.cksqsh = sob.corder_bid ");
    sql.append(" and so.dr = 0 and sob.dr = 0  ");
    sql.append(" and a.dr = 0 and b.dr = 0  and xgbh = -1 ");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertCostMnyBeforeM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sTable)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" beginoutmny ) select " + sSelSo);

    sql.append(" isnull(v.nmoney, v.nsimulatemny) beginoutmny ");
    sql.append(" from ia_bill_b v, so_sale so, ");
    sql.append("  so_saleexecute soexe,so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append("  and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid = soexe.csale_bid");

    sql.append(" and sob.csaleid=so.csaleid ");
    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" >= coalesce(soexe.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(soexe.ntotlbalcostnum,0) != 0 ");

    sql.append(" and (nmoney is not null or nsimulatemny is not null) ");

    sql.append(" and so.dbilldate <'" + sBeginDate + "'");

    sql.append(" and ( csaleadviceid  is  null or  ");
    sql.append(" csourcebilltypecode  != '32' )");

    sql.append(" and ( so.bretinvflag='N' or so.bretinvflag is null ) ");
    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr = 0 and v.dr=0 ");

    update(sql.toString());
  }

  public void insertCostMnyNowM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" outmny ) select " + sSelSo);

    sql.append(" sum( isnull(v.nmoney, v.nsimulatemny)) beginoutmny ");
    sql.append(" from ia_bill_b v, so_sale so,ia_bill va, ");
    sql.append("  so_saleexecute soexe,so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append("  and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid = soexe.csale_bid");
    sql.append(" and v.cbillid=va.cbillid ");

    sql.append(" and sob.csaleid=so.csaleid ");
    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" >= coalesce(soexe.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(soexe.ntotlbalcostnum,0) != 0 ");

    sql.append(" and (nmoney is not null or nsimulatemny is not null) ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( csaleadviceid  is  null or  ");
    sql.append(" csourcebilltypecode  != '32' )");

    sql.append(" and ( so.bretinvflag='N' or so.bretinvflag is null ) ");

    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr = 0 and v.dr=0 and va.dr=0 ");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");
    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertCostNumNowM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" costnum ) select " + sSelSo);

    sql.append(" sum( isnull(v.nnumber, 0)) beginoutmny ");
    sql.append(" from ia_bill_b v, so_sale so,ia_bill va, ");
    sql.append("  so_saleexecute soexe,so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append("  and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid = soexe.csale_bid");
    sql.append(" and v.cbillid=va.cbillid ");

    sql.append(" and sob.csaleid=so.csaleid ");
    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( csaleadviceid  is  null or  ");
    sql.append(" csourcebilltypecode  != '32' )");

    sql.append(" and ( so.bretinvflag='N' or so.bretinvflag is null ) ");

    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr = 0 and v.dr=0 and va.dr=0 ");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");
    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertTOCostMnyNowM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable, boolean bsanfang)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" outmny ) select " + sSelSo);

    sql.append(" sum( isnull(v.nmoney, v.nsimulatemny)) beginoutmny ");
    sql.append(" from ia_bill_b v, ia_bill va,");
    sql.append("  to_bill_b tob,to_bill toh ");
    sql.append(sSqlPieceSo);
    sql.append("  and v.cfirstbilltypecode in ('5C','5D') ");
    sql.append(" and v.cfirstbillid=tob.cbillid ");
    sql.append(" and v.cfirstbillitemid = tob.cbill_bid");
    sql.append(" and toh.cbillid = tob.cbillid");
    sql.append(" and v.cbillid = va.cbillid");

    sql.append(" and (nmoney is not null or nsimulatemny is not null) ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( csaleadviceid  is  null or  ");
    sql.append("  csourcebilltypecode  != '32' )");

    sql.append(" and ( toh.bretractflag is null or toh.bretractflag = 'N') ");
    if (bsanfang)
      sql.append(" and tob.coutcorpid <> tob.ctakeoutcorpid ");
    else {
      sql.append(" and  tob.coutcorpid = tob.ctakeoutcorpid ");
    }

    sql.append(" and tob.dr=0  and v.dr=0 and toh.dr = 0 ");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");
    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertTOCostNumNowM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable, boolean bsanfang)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" costnum ) select " + sSelSo);

    sql.append(" sum( isnull(v.nnumber, 0)) beginoutmny ");
    sql.append(" from ia_bill_b v, ia_bill va,");
    sql.append("  to_bill_b tob,to_bill toh ");
    sql.append(sSqlPieceSo);
    sql.append("  and v.cfirstbilltypecode in ('5C','5D') ");
    sql.append(" and v.cfirstbillid=tob.cbillid ");
    sql.append(" and v.cfirstbillitemid = tob.cbill_bid");
    sql.append(" and toh.cbillid = tob.cbillid");
    sql.append(" and v.cbillid = va.cbillid");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( csaleadviceid  is  null or  ");
    sql.append("  csourcebilltypecode  != '32' )");

    sql.append(" and ( toh.bretractflag is null or toh.bretractflag = 'N') ");
    if (bsanfang)
      sql.append(" and tob.coutcorpid <> tob.ctakeoutcorpid ");
    else {
      sql.append(" and  tob.coutcorpid = tob.ctakeoutcorpid ");
    }

    sql.append(" and tob.dr=0  and v.dr=0 and toh.dr = 0 ");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");
    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertCostNumMnyBeforeM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sTable, Map costIndex)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select v.cstockrdcenterid, v.cinventoryid, v.vbatch, ");
    sql.append(" sum( coalesce(v.nnumber,0) ), " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  v_ia_inoutledger v ,so_sale so, ");
    sql.append(" so_saleexecute soexe, so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append(" and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid=soexe.csale_bid ");

    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and (v.nmoney is null and v.nsimulatemny is null )");

    sql.append(" and ( csaleadviceid  is  null ");
    sql.append(" or csourcebilltypecode  != '32' )");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" >= coalesce(soexe.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(soexe.ntotlbalcostnum,0) != 0 ");

    sql.append(" and (so.bretinvflag='N'or so.bretinvflag is null) ");

    sql.append(" and so.dbilldate <'" + sBeginDate + "'");
    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr = 0 ");
    sql.append(" group by v.cstockrdcenterid, v.cinventoryid, v.vbatch, ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    Object[] data = query(sql.toString());
    ArrayList list = calculateVirtualCost(data, costIndex, sCorp);

    persistent(sTable, sNewColInsert, "beginoutmny", list);
  }

  public void insertCostNumMnyNowM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable, Map costIndex)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select v.cstockrdcenterid , v.cinventoryid ,v.vbatch, ");
    sql.append(" sum ( coalesce(v.nnumber,0) ) , " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  v_ia_inoutledger v ,so_sale so, ");
    sql.append(" so_saleexecute soexe, so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append(" and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid=soexe.csale_bid ");

    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and (v.nmoney is null and v.nsimulatemny is null )");

    sql.append(" and ( csaleadviceid  is  null ");
    sql.append(" or csourcebilltypecode  != '32' )");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" >= coalesce(soexe.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(soexe.ntotlbalcostnum,0) != 0 ");

    sql.append(" and (so.bretinvflag='N'or so.bretinvflag is null) ");

    sql.append(" and v.dbilldate >='" + sBeginDate + "'");
    sql.append(" and v.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");
    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");

    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr = 0 ");
    sql.append(" group by v.cstockrdcenterid , v.cinventoryid ,v.vbatch, ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    Object[] data = query(sql.toString());
    ArrayList list = calculateVirtualCost(data, costIndex, sCorp);
    persistent(sTable, sNewColInsert, "outmny", list);
  }

  public void insertTOCostNumMnyNowM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable, Map costIndex, boolean bsanfang)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select v.cstockrdcenterid , v.cinventoryid ,v.vbatch, ");
    sql.append(" sum ( coalesce(v.nnumber,0) ) , " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  v_ia_inoutledger v , ");
    sql.append("  to_bill_b tob,to_bill toh ");
    sql.append(sSqlPieceSo);
    sql.append(" and v.cfirstbilltypecode in ('5C','5D') ");
    sql.append(" and v.cfirstbillid=tob.cbillid ");
    sql.append(" and v.cfirstbillitemid = tob.cbill_bid");
    sql.append(" and toh.cbillid = tob.cbillid");

    sql.append(" and (v.nmoney is null and v.nsimulatemny is null )");

    sql.append(" and ( csaleadviceid  is  null ");
    sql.append(" or csourcebilltypecode  != '32' )");

    sql.append(" and (toh.bretractflag is null or toh.bretractflag = 'N') ");
    if (bsanfang)
      sql.append(" and  tob.coutcorpid <> tob.ctakeoutcorpid ");
    else {
      sql.append(" and tob.coutcorpid = tob.ctakeoutcorpid ");
    }

    sql.append(" and tob.dr=0  and toh.dr=0 ");

    sql.append(" and v.dbilldate >='" + sBeginDate + "'");
    sql.append(" and v.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");
    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");

    sql.append(" group by v.cstockrdcenterid , v.cinventoryid ,v.vbatch, ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    Object[] data = query(sql.toString());
    ArrayList list = calculateVirtualCost(data, costIndex, sCorp);
    persistent(sTable, sNewColInsert, "outmny", list);
  }

  public void insertSubIncomeToCostBefore(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sTable, Map costIndex)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select isnull( sob.creccalbodyid,sob.cadvisecalbodyid), ");
    sql.append(" sob.cinventoryid, sob.cbatchid, ");

    sql.append(" sum( COALESCE(soexe.ntotalbalancenumber,0)- ");
    sql.append(" COALESCE(soexe.ntotlbalcostnum,0)) ");
    sql.append(" nnumber, " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  so_sale so, ");
    sql.append(" so_saleexecute soexe, so_saleorder_b sob ");
    sql.append(sSqlPieceSo);

    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" > coalesce(soexe.ntotlbalcostnum,0) ");

    sql.append(" and (so.bretinvflag='N'or so.bretinvflag is null) ");

    sql.append(" and so.dbilldate <'" + sBeginDate + "'");
    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr=0 ");
    sql.append(" group by ");
    sql.append(" isnull( sob.creccalbodyid,sob.cadvisecalbodyid), ");
    sql.append(" sob.cinventoryid, sob.cbatchid, ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    Object[] data = query(sql.toString());
    ArrayList list = calculateVirtualCost(data, costIndex, sCorp);
    persistent(sTable, sNewColInsert, "beginoutmny", list);
  }

  public void insertSubIncomeToCostNow(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable, Map costIndex)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select isnull( sob.creccalbodyid, sob.cadvisecalbodyid),  ");
    sql.append(" sob.cinventoryid , sob.cbatchid , ");

    sql.append(" sum ( COALESCE(soexe.ntotalbalancenumber,0)- ");
    sql.append(" COALESCE(soexe.ntotlbalcostnum,0) )");
    sql.append(" nnumber, " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  so_sale so, ");
    sql.append(" so_saleexecute soexe, so_saleorder_b sob,ia_bill_b v ,ia_bill va ");
    sql.append(sSqlPieceSo);

    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append("  and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid = soexe.csale_bid");
    sql.append(" and v.cbillid=va.cbillid ");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" > coalesce(soexe.ntotlbalcostnum,0) ");

    sql.append(" and (so.bretinvflag='N'or so.bretinvflag is null) ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");
    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr=0 and va.dr = 0 and v.dr = 0 ");

    sql.append(" group by isnull( sob.creccalbodyid, sob.cadvisecalbodyid),");
    sql.append(" sob.cinventoryid , sob.cbatchid  , ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    Object[] data = query(sql.toString());
    ArrayList list = calculateVirtualCost(data, costIndex, sCorp);
    persistent(sTable, sNewColInsert, "outmny", list);
  }

  public void insertIncomeNumBefore(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sTable)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select sob.corder_bid,  ");

    sql.append(" COALESCE(ntotalbalancenumber,0) nnumber, " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  so_sale so, ");
    sql.append(" so_saleexecute soexe, so_saleorder_b sob,ia_bill_b v ,ia_bill va  ");
    sql.append(sSqlPieceSo);

    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append("  and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid = soexe.csale_bid");
    sql.append(" and v.cbillid=va.cbillid ");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" < coalesce(soexe.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) != 0 ");

    sql.append(" and (so.bretinvflag='N'or so.bretinvflag is null) ");

    sql.append(" and va.dbilldate <'" + sBeginDate + "'");
    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr=0 ");

    Object[] data = query(sql.toString());
    ArrayList list = calculateActualCost(data, sCorp);
    persistent(sTable, sNewColInsert, "beginoutmny", list);
  }

  public void insertIncomeNumNow(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select sob.corder_bid,  ");

    sql.append(" COALESCE(ntotalbalancenumber,0) nnumber, " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  so_sale so, ");
    sql.append(" so_saleexecute soexe, so_saleorder_b sob,ia_bill_b v ,ia_bill va  ");
    sql.append(sSqlPieceSo);

    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" < coalesce(soexe.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) != 0 ");
    sql.append("  and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid = soexe.csale_bid");
    sql.append(" and v.cbillid=va.cbillid ");

    sql.append(" and (so.bretinvflag='N'or so.bretinvflag is null) ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");
    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr=0 and v.dr=0 and va.dr = 0 ");

    Object[] data = query(sql.toString());
    ArrayList list = calculateActualCost(data, sCorp);
    persistent(sTable, sNewColInsert, "outmny", list);
  }

  public void insertRetNumMny(String sNewColInsert, String sSelSo, String sSelAp, String sSqlPieceSo, String sSqlPieceAp, String sBeginDate, String sEndDate, String sTable, boolean retStarted)
    throws Exception
  {
    boolean hasFree = sNewColInsert.indexOf("vfree1") > 0;

    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" retnum,retmny ) ");
    sql.append(" select " + sSelSo + " sum ( - coalesce(b.jfshl,0) ) retnum, ");

    sql.append(" sum( -coalesce(b.wbfbbje,0) ) retmny ");
    sql.append(" from so_sale so, ");
    if (hasFree) {
      sql.append(" so_saleexecute soexe, ");
    }
    sql.append(" arap_djfb b,arap_djzb a,so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append(" and so.csaleid = sob.csaleid ");
    sql.append(" and a.vouchid= b.vouchid ");
    if (hasFree) {
      sql.append(" and soexe.csaleid = sob.csaleid ");
      sql.append(" and soexe.csale_bid = sob.corder_bid ");
      sql.append(" and soexe.csaleid = so.csaleid ");
    }

    sql.append(" and a.djrq >='" + sBeginDate + "'");
    sql.append(" and a.djrq <='" + sEndDate + "'");
    sql.append(" and so.bretinvflag='Y' ");
    sql.append(" and a.djdl='ys' and b.ph= '30' and b.xyzh=so.csaleid ");
    sql.append(" and b.cksqsh = sob.corder_bid ");
    sql.append(" and so.dr = 0 and sob.dr = 0  ");
    sql.append(" and a.dr = 0 and b.dr = 0  and xgbh = -1 ");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());

    if (!retStarted) {
      return;
    }

    sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" retnum,retmny ) ");
    sql.append(" select " + sSelAp);
    sql.append(" sum( -coalesce(b.jfshl,0) ) retnum, ");
    sql.append(" sum( -coalesce(b.wbfbbje,0) ) retmny ");
    sql.append(" from so_apply ap, ");
    sql.append(" arap_djfb b,arap_djzb a , so_apply_b apb");
    sql.append(sSqlPieceAp);
    sql.append(" and ap.pk_apply=apb.pk_apply ");
    sql.append(" and a.vouchid= b.vouchid ");
    sql.append(" and ap.dbilldate >='" + sBeginDate + "'");
    sql.append(" and ap.dbilldate <='" + sEndDate + "'");
    sql.append(" and a.djdl='ys' and b.ph= '3U' and b.xyzh=apb.pk_apply ");
    sql.append(" and b.cksqsh = apb.pk_apply_b ");
    sql.append(" and ap.dr=0 and apb.dr=0 ");
    sql.append(" and a.dr = 0 and b.dr = 0  and xgbh = -1 ");
    sql.append(" group by ");
    sql.append(sSelAp.substring(0, sSelAp.length() - 1));

    update(sql.toString());
  }

  public void insertToRetNumAndMnyNow(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sBeginDate, String sEndDate, String sTable, boolean bsanfang)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" retnum,retmny  ) select " + sSelSo);
    sql.append(" sum( -coalesce(b.jfshl,0) ) retnum, ");
    sql.append(" sum( -coalesce(b.wbfbbje,0) ) retmny ");

    sql.append(" from ");

    sql.append(" arap_djfb b,arap_djzb a,to_bill toh, to_bill_b tob ");
    sql.append(sSqlPieceSo);
    sql.append(" and a.vouchid= b.vouchid ");
    sql.append(" and toh.cbillid=tob.cbillid ");

    sql.append(" and a.djdl='ys' and b.xyzh=toh.cbillid ");
    sql.append(" and b.cksqsh = tob.cbill_bid ");
    sql.append(" and a.dr = 0 and b.dr = 0  and xgbh = -1 and toh.dr=0 and tob.dr=0 ");

    sql.append(" and ( toh.bretractflag = 'Y') ");
    if (bsanfang)
      sql.append(" and tob.coutcorpid <> tob.ctakeoutcorpid ");
    else {
      sql.append(" and tob.coutcorpid = tob.ctakeoutcorpid ");
    }
    sql.append(" and a.djrq >='" + sBeginDate + "'");
    sql.append(" and a.djrq <='" + sEndDate + "'");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertTORetCostMnyNowM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable, boolean bsanfang)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" retcostmny ) select " + sSelSo);

    sql.append(" sum( -isnull(v.nmoney, v.nsimulatemny) ) retcostmny ");
    sql.append(" from ia_bill_b v, ia_bill va,");
    sql.append("  to_bill_b tob,to_bill toh ");
    sql.append(sSqlPieceSo);
    sql.append("  and v.cfirstbilltypecode in ('5C','5D') ");
    sql.append(" and v.cfirstbillid=tob.cbillid ");
    sql.append(" and v.cfirstbillitemid = tob.cbill_bid");
    sql.append(" and toh.cbillid = tob.cbillid");
    sql.append(" and va.cbillid = v.cbillid");

    sql.append(" and (nmoney is not null or nsimulatemny is not null) ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( csaleadviceid  is  null or  ");
    sql.append(" csourcebilltypecode  != '32' )");

    sql.append(" and (toh.bretractflag = 'Y') ");
    if (bsanfang)
      sql.append(" and tob.coutcorpid <> tob.ctakeoutcorpid ");
    else {
      sql.append(" and tob.coutcorpid = tob.ctakeoutcorpid ");
    }

    sql.append(" and tob.dr=0  and v.dr=0 and toh.dr = 0 ");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");
    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertTORetCostNumNowM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable, boolean bsanfang)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert);
    sql.append(" retcostnum ) select " + sSelSo);

    sql.append(" sum( -isnull(v.nmoney, v.nsimulatemny) ) retcostmny ");
    sql.append(" from ia_bill_b v, ia_bill va,");
    sql.append("  to_bill_b tob,to_bill toh ");
    sql.append(sSqlPieceSo);
    sql.append("  and v.cfirstbilltypecode in ('5C','5D') ");
    sql.append(" and v.cfirstbillid=tob.cbillid ");
    sql.append(" and v.cfirstbillitemid = tob.cbill_bid");
    sql.append(" and toh.cbillid = tob.cbillid");
    sql.append(" and va.cbillid = v.cbillid");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( csaleadviceid  is  null or  ");
    sql.append(" csourcebilltypecode  != '32' )");

    sql.append(" and (toh.bretractflag = 'Y') ");
    if (bsanfang)
      sql.append(" and tob.coutcorpid <> tob.ctakeoutcorpid ");
    else {
      sql.append(" and tob.coutcorpid = tob.ctakeoutcorpid ");
    }

    sql.append(" and tob.dr=0  and v.dr=0 and toh.dr = 0 ");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");
    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());
  }

  public void insertRetCostNum(String sNewColInsert, String sSelSo, String sSelAp, String sSqlPieceSo, String sSqlPieceAp, String sCorp, String sBeginDate, String sEndDate, String sTable, boolean retStarted)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert + "retcostnum)");
    sql.append(" select " + sSelSo);

    sql.append(" sum( -isnull(v.nnumber, 0) ) retcostmny");
    sql.append(" from ia_bill_b v,ia_bill va, so_sale so, ");
    sql.append(" so_saleexecute soexe , so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append(" and v.cfirstbilltypecode='30'");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid=soexe.csale_bid");

    sql.append(" and v.cbillid=va.cbillid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and so.bretinvflag='Y' ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( csaleadviceid  is  null ");
    sql.append(" or csourcebilltypecode  != '32' )");

    sql.append(" and so.dr=0 and soexe.dr=0 and v.dr=0 and sob.dr=0 ");

    sql.append(" and ( v.cbilltypecode in ('I2','");
    sql.append("I9','I5','");
    sql.append("IA','IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) ) ");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());

    if (!retStarted) {
      return;
    }
    sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert + "retcostnum)");
    sql.append(" select " + sSelAp);

    sql.append("sum( -isnull(v.nnumber, 0) ) retcostmny");
    sql.append(" from ia_bill_b v,ia_bill va, so_apply ap,so_apply_b apb ");
    sql.append(sSqlPieceAp);
    sql.append(" and v.cfirstbilltypecode='3U'");
    sql.append(" and v.cfirstbillitemid=apb.pk_apply_b");
    sql.append(" and v.cfirstbillid=ap.pk_apply ");
    sql.append(" and ap.pk_apply=apb.pk_apply ");

    sql.append(" and v.cbillid=va.cbillid ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( csaleadviceid  is  null ");

    sql.append(" or csourcebilltypecode  != '32' )");
    sql.append(" and ( v.cbilltypecode in ('I2','");
    sql.append("I9','I5','");
    sql.append("IA','IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) ) ");
    sql.append(" and ap.dr=0 and apb.dr=0 and v.dr=0 ");
    sql.append(" group by ");
    sql.append(sSelAp.substring(0, sSelAp.length() - 1));

    update(sql.toString());
  }

  public void insertRetCostMny(String sNewColInsert, String sSelSo, String sSelAp, String sSqlPieceSo, String sSqlPieceAp, String sCorp, String sBeginDate, String sEndDate, String sTable, boolean retStarted)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert + "retcostmny)");
    sql.append(" select " + sSelSo);

    sql.append(" sum( -isnull(v.nmoney, v.nsimulatemny) ) retcostmny");
    sql.append(" from ia_bill_b v,ia_bill va, so_sale so, ");
    sql.append(" so_saleexecute soexe , so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append(" and v.cfirstbilltypecode='30'");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid=soexe.csale_bid");

    sql.append(" and v.cbillid=va.cbillid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and (nmoney is not null or nsimulatemny is not null) ");

    sql.append(" and so.bretinvflag='Y' ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" <= coalesce(soexe.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(soexe.ntotlbalcostnum,0) != 0 ");

    sql.append(" and ( csaleadviceid  is  null ");
    sql.append(" or csourcebilltypecode  != '32' )");

    sql.append(" and so.dr=0 and soexe.dr=0 and v.dr=0 and sob.dr=0 ");

    sql.append(" and ( v.cbilltypecode in ('I2','");
    sql.append("I9','I5','");
    sql.append("IA','IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) ) ");
    sql.append(" group by ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    update(sql.toString());

    if (!retStarted) {
      return;
    }
    sql = new StringBuffer();
    sql.append(" insert into " + sTable + "(" + sNewColInsert + "retcostmny)");
    sql.append(" select " + sSelAp);

    sql.append("sum( -isnull(v.nmoney, v.nsimulatemny) ) retcostmny");
    sql.append(" from ia_bill_b v,ia_bill va, so_apply ap,so_apply_b apb ");
    sql.append(sSqlPieceAp);
    sql.append(" and v.cfirstbilltypecode='3U'");
    sql.append(" and v.cfirstbillitemid=apb.pk_apply_b");
    sql.append(" and v.cfirstbillid=ap.pk_apply ");
    sql.append(" and ap.pk_apply=apb.pk_apply ");

    sql.append(" and v.cbillid=va.cbillid ");

    sql.append(" and (nmoney is not null or nsimulatemny is not null) ");

    sql.append(" and va.dbilldate >='" + sBeginDate + "'");
    sql.append(" and va.dbilldate <='" + sEndDate + "'");

    sql.append(" and coalesce(apb.ntotalbalancenumber,0) ");
    sql.append(" >= coalesce(apb.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(apb.ntotlbalcostnum,0) != 0 ");

    sql.append(" and ( csaleadviceid  is  null ");

    sql.append(" or csourcebilltypecode  != '32' )");
    sql.append(" and ( v.cbilltypecode in ('I2','");
    sql.append("I9','I5','");
    sql.append("IA','IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) ) ");
    sql.append(" and ap.dr=0 and apb.dr=0 and v.dr=0 ");
    sql.append(" group by ");
    sql.append(sSelAp.substring(0, sSelAp.length() - 1));

    update(sql.toString());
  }

  public void insertRetCostNumMny(String sNewColInsert, String sSelSo, String sSelAp, String sSqlPieceSo, String sSqlPieceAp, String sCorp, String sBeginDate, String sEndDate, String sTable, boolean retStarted, Map costIndex)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select v.cstockrdcenterid, v.cinventoryid,v.vbatch , ");

    sql.append(" sum( -coalesce(v.nnumber,0) ), " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  v_ia_inoutledger v ,so_sale so, ");
    sql.append(" so_saleexecute soexe, so_saleorder_b sob ");
    sql.append(sSqlPieceSo);
    sql.append(" and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid=soexe.csale_bid ");

    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and (v.nmoney is null and v.nsimulatemny is null )");

    sql.append(" and ( csaleadviceid  is  null ");
    sql.append(" or csourcebilltypecode  != '32' )");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" <= coalesce(soexe.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(soexe.ntotlbalcostnum,0) != 0 ");

    sql.append(" and so.bretinvflag='Y' ");

    sql.append(" and v.dbilldate >='" + sBeginDate + "'");
    sql.append(" and v.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");
    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");
    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr=0 ");
    sql.append(" group by v.cstockrdcenterid, v.cinventoryid,v.vbatch, ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    Object[] data = query(sql.toString());

    if (retStarted) {
      sql = new StringBuffer();
      sql.append(" select v.cstockrdcenterid, v.cinventoryid,v.vbatch , ");

      sql.append(" sum( -coalesce(v.nnumber,0)) , " + sSelAp);
      sql.deleteCharAt(sql.length() - 1);
      sql.append(" from v_ia_inoutledger v, so_apply ap,so_apply_b apb ");
      sql.append(sSqlPieceAp);
      sql.append(" and v.cfirstbilltypecode='3U'");
      sql.append(" and v.cfirstbillitemid=apb.pk_apply_b");
      sql.append(" and v.cfirstbillid=ap.pk_apply ");
      sql.append(" and ap.pk_apply=apb.pk_apply ");

      sql.append(" and (v.nmoney is null and v.nsimulatemny is null )");

      sql.append(" and v.dbilldate >='" + sBeginDate + "'");
      sql.append(" and v.dbilldate <='" + sEndDate + "'");

      sql.append(" and coalesce(apb.ntotalbalancenumber,0) ");
      sql.append(" >= coalesce(apb.ntotlbalcostnum,0) ");
      sql.append(" and coalesce(apb.ntotlbalcostnum,0) != 0 ");

      sql.append(" and ( csaleadviceid  is  null ");
      sql.append(" or csourcebilltypecode  != '32' )");

      sql.append(" and ( v.cbilltypecode in ('I2','");
      sql.append("I9','I5','");
      sql.append("IA','IH')");
      sql.append(" or ( v.cbilltypecode in('II','IJ') ");
      sql.append(" and v.cfirstbilltypecode in('5C','5D')) ) ");
      sql.append(" and ap.dr=0 and apb.dr=0 ");
      sql.append(" group by v.cstockrdcenterid, v.cinventoryid,v.vbatch, ");
      sql.append(sSelAp.substring(0, sSelAp.length() - 1));

      Object[] data2 = query(sql.toString());

      if (data2.length > 0) {
        int size = data.length + data2.length;
        Object[] array = new Object[size];
        System.arraycopy(data, 0, array, 0, data.length);
        System.arraycopy(data2, 0, array, data.length, data2.length);
        data = array;
      }
    }
    ArrayList list = calculateVirtualCost(data, costIndex, sCorp);
    persistent(sTable, sNewColInsert, "retcostmny", list);
  }

  public void insertTORetCostNumMnyNowM(String sNewColInsert, String sSelSo, String sSqlPieceSo, String sCorp, String sBeginDate, String sEndDate, String sTable, Map costIndex, boolean bsanfang)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select v.cstockrdcenterid , v.cinventoryid ,v.vbatch, ");
    sql.append(" sum ( -coalesce(v.nnumber,0) ) , " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  v_ia_inoutledger v , ");
    sql.append("  to_bill_b tob,to_bill toh ");
    sql.append(sSqlPieceSo);
    sql.append(" and v.cfirstbilltypecode in('5C','5D') ");
    sql.append(" and v.cfirstbillid=tob.cbillid ");
    sql.append(" and v.cfirstbillitemid = tob.cbill_bid");
    sql.append(" and toh.cbillid = tob.cbillid");

    sql.append(" and (v.nmoney is null and v.nsimulatemny is null )");

    sql.append(" and ( csaleadviceid  is  null ");
    sql.append(" or csourcebilltypecode  != '32' )");

    sql.append(" and ( toh.bretractflag = 'Y') ");
    if (bsanfang)
      sql.append(" and tob.coutcorpid <> tob.ctakeoutcorpid ");
    else {
      sql.append(" and tob.coutcorpid = tob.ctakeoutcorpid ");
    }

    sql.append(" and tob.dr=0   and toh.dr=0 ");

    sql.append(" and v.dbilldate >='" + sBeginDate + "'");
    sql.append(" and v.dbilldate <='" + sEndDate + "'");

    sql.append(" and ( v.cbilltypecode in ('");
    sql.append("I2,I9','");
    sql.append("I5','IA','");

    sql.append("IH')");
    sql.append(" or ( v.cbilltypecode in('II','IJ') ");
    sql.append(" and v.cfirstbilltypecode in('5C','5D')) )");

    sql.append(" group by v.cstockrdcenterid , v.cinventoryid ,v.vbatch, ");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    Object[] data = query(sql.toString());
    ArrayList list = calculateVirtualCost(data, costIndex, sCorp);
    persistent(sTable, sNewColInsert, "retcostmny", list);
  }

  public void insertRetSubINcomeToCost(String sNewColInsert, String sSelSo, String sSelAp, String sSqlPieceSo, String sSqlPieceAp, String sCorp, String sBeginDate, String sEndDate, String sTable, boolean retStarted, Map costIndex)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select isnull( sob.creccalbodyid,sob.cadvisecalbodyid ), ");
    sql.append(" sob.cinventoryid, sob.cbatchid, ");

    sql.append(" sum( COALESCE(soexe.ntotlbalcostnum,0)- ");
    sql.append(" COALESCE(soexe.ntotalbalancenumber,0) )");
    sql.append(" nnumber, " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from  so_sale so, ");
    sql.append(" so_saleexecute soexe, so_saleorder_b sob, v_ia_inoutledger v ");
    sql.append(sSqlPieceSo);

    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");

    sql.append(" and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid=soexe.csale_bid ");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" < coalesce(soexe.ntotlbalcostnum,0) ");

    sql.append(" and so.bretinvflag='Y' ");

    sql.append(" and v.dbilldate >='" + sBeginDate + "'");
    sql.append(" and v.dbilldate <='" + sEndDate + "'");

    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr=0 ");
    sql.append(" group by isnull( sob.creccalbodyid,sob.cadvisecalbodyid ),");
    sql.append(" sob.cinventoryid, sob.cbatchid ,");
    sql.append(sSelSo.substring(0, sSelSo.length() - 1));

    Object[] data = query(sql.toString());

    if (retStarted) {
      sql = new StringBuffer();
      sql.append(" select apb.ccalbodyid, apb.cinventoryid, apb.cbatchid, ");

      sql.append(" sum ( coalesce(apb.ntotalbalancenumber,0) - ");
      sql.append(" coalesce(apb.ntotlbalcostnum,0) ) nnumber," + sSelAp);
      sql.deleteCharAt(sql.length() - 1);
      sql.append(" from so_apply ap,so_apply_b apb, v_ia_inoutledger v  ");
      sql.append(sSqlPieceAp);
      sql.append(" and ap.pk_apply=apb.pk_apply ");

      sql.append(" and v.cfirstbilltypecode='3U'");
      sql.append(" and v.cfirstbillitemid=apb.pk_apply_b");
      sql.append(" and v.cfirstbillid=ap.pk_apply ");
      sql.append(" and ap.pk_apply=apb.pk_apply ");

      sql.append(" and v.dbilldate >='" + sBeginDate + "'");
      sql.append(" and v.dbilldate <='" + sEndDate + "'");

      sql.append(" and coalesce(apb.ntotalbalancenumber,0) ");
      sql.append(" > coalesce(apb.ntotlbalcostnum,0) ");

      sql.append(" and ap.dr=0 and apb.dr=0 ");
      sql.append(" group by apb.ccalbodyid, apb.cinventoryid, apb.cbatchid,");
      sql.append(sSelAp.substring(0, sSelAp.length() - 1));

      Object[] data2 = query(sql.toString());

      if (data2.length > 0) {
        int size = data.length + data2.length;
        Object[] array = new Object[size];
        System.arraycopy(data, 0, array, 0, data.length);
        System.arraycopy(data2, 0, array, data.length, data2.length);
        data = array;
      }

    }

    ArrayList list = calculateVirtualCost(data, costIndex, sCorp);
    persistent(sTable, sNewColInsert, "retcostmny", list);
  }

  public void insertRetIncomeNum(String sNewColInsert, String sSelSo, String sSelAp, String sSqlPieceSo, String sSqlPieceAp, String sCorp, String sBeginDate, String sEndDate, String sTable, boolean retStarted)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select sob.corder_bid,  ");

    sql.append(" -COALESCE(ntotalbalancenumber,0) nnumber, " + sSelSo);
    sql.deleteCharAt(sql.length() - 1);
    sql.append(" from so_sale so, ");
    sql.append(" so_saleexecute soexe, so_saleorder_b sob, v_ia_inoutledger v ");
    sql.append(sSqlPieceSo);

    sql.append(" and soexe.csaleid = sob.csaleid ");
    sql.append(" and so.csaleid=sob.csaleid ");
    sql.append(" and soexe.csale_bid = sob.corder_bid ");
    sql.append(" and soexe.csaleid = so.csaleid ");
    sql.append(" and v.cfirstbilltypecode='30' ");
    sql.append(" and v.cfirstbillid=so.csaleid ");
    sql.append(" and v.cfirstbillitemid=soexe.csale_bid ");

    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) ");
    sql.append(" > coalesce(soexe.ntotlbalcostnum,0) ");
    sql.append(" and coalesce(soexe.ntotalbalancenumber,0) != 0 ");

    sql.append(" and so.bretinvflag='Y' ");

    sql.append(" and v.dbilldate >='" + sBeginDate + "'");
    sql.append(" and v.dbilldate <='" + sEndDate + "'");

    sql.append(" and so.dr=0 and soexe.dr=0 and sob.dr=0 ");

    Object[] data = query(sql.toString());

    if (retStarted) {
      sql = new StringBuffer();
      sql.append(" select apb.pk_apply_b ,  ");

      sql.append(" COALESCE(ntotalbalancenumber,0) nnumber, " + sSelAp);
      sql.deleteCharAt(sql.length() - 1);
      sql.append(" from  so_apply ap  ,so_apply_b apb , v_ia_inoutledger v ");
      sql.append(sSqlPieceAp);

      sql.append(" and ap.pk_apply=apb.pk_apply ");

      sql.append(" and v.cfirstbilltypecode='3U'");
      sql.append(" and v.cfirstbillitemid=apb.pk_apply_b");
      sql.append(" and v.cfirstbillid=ap.pk_apply ");

      sql.append(" and coalesce(apb.ntotalbalancenumber,0) ");
      sql.append(" < coalesce(apb.ntotlbalcostnum,0) ");
      sql.append(" and coalesce(apb.ntotalbalancenumber,0) != 0 ");

      sql.append(" and v.dbilldate >='" + sBeginDate + "'");
      sql.append(" and v.dbilldate <='" + sEndDate + "'");

      sql.append(" and ap.dr=0 and apb.dr=0 ");

      Object[] data2 = query(sql.toString());
      if (data2.length > 0) {
        int size = data.length + data2.length;
        Object[] array = new Object[size];
        System.arraycopy(data, 0, array, 0, data.length);
        System.arraycopy(data2, 0, array, data.length, data2.length);
        data = array;
      }
    }

    ArrayList list = calculateActualCost(data, sCorp);
    persistent(sTable, sNewColInsert, "retcostmny", list);
  }

  public String[] checkQueryData(ParamVO condition)
    throws Exception
  {
    Object oGroup = condition.getAttributeValue("groupConditons");
    String[] sReturn = new String[3];

    if (oGroup == null) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000093"));

      throw re;
    }

    Object oCorpID = condition.getAttributeValue("pk_corp");

    if ((oCorpID == null) || (oCorpID.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000088"));

      throw re;
    }

    sReturn[0] = oCorpID.toString();

    Object oBeginDate = condition.getAttributeValue("startDate");
    if ((oBeginDate == null) || (oBeginDate.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000090"));

      throw re;
    }

    sReturn[1] = oBeginDate.toString();

    Object oEndDate = condition.getAttributeValue("endDate");
    if ((oEndDate == null) || (oEndDate.toString().trim().length() == 0)) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000091"));

      throw re;
    }
    sReturn[2] = oEndDate.toString();

    Object oCurrencyID = condition.getAttributeValue("currencyid");

    BusinessCurrencyRateUtil dmo = new BusinessCurrencyRateUtil(oCorpID.toString());

    String sCurID = dmo.getLocalCurrPK();

    if ((oCurrencyID != null) && (!oCurrencyID.toString().trim().equals(sCurID))) {
      BusinessException re = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000092"));

      throw re;
    }

    return sReturn;
  }

  public SaleGrossprofitVO[] queryGrossData(String sField, String sTempTable, String JoinTable, Vector vAttrs, String newField)
    throws Exception
  {
    StringBuffer sql = new StringBuffer();
    sql.append(" select ");
    sql.append(sField);
    sql.append(" initnumber,initmoney,  curpurnumber,curpurmoney,");
    sql.append(" cursonumber, cursomoney, cursocost, lastnumber,lastmoney, ");
    sql.append(" retnum,retmny, retcostmny,costnum,retcostnum ");
    sql.append("  from ( ");

    sql.append(" select ");
    sql.append(newField);
    sql.append(" sum (coalesce(begininnum,0)");
    sql.append(" - coalesce(beginoutnum,0)) initnumber, ");
    sql.append(" sum (coalesce(begininmny,0)  ");
    sql.append(" - coalesce(beginoutmny,0)) initmoney, ");
    sql.append(" sum (coalesce(innum,0)) curpurnumber, ");
    sql.append(" sum (coalesce(inmny,0)) curpurmoney, ");
    sql.append(" sum (coalesce(outnum,0)) cursonumber, ");
    sql.append(" sum(coalesce(incomemny,0)) cursomoney,");
    sql.append(" sum (coalesce(outmny,0)) cursocost, ");
    sql.append(" sum (coalesce(begininnum,0) - coalesce(beginoutnum,0)) ");
    sql.append(" + sum (coalesce(innum,0)) - sum (coalesce(costnum,0))+ sum (coalesce(retcostnum,0))");
    sql.append(" lastnumber, ");
    sql.append(" sum (coalesce(begininmny,0) - coalesce(beginoutmny,0)) ");
    sql.append(" + sum (coalesce(inmny,0)) - sum (coalesce(outmny,0))+ sum (coalesce(retcostmny,0))");
    sql.append(" lastmoney,  ");
    sql.append(" sum(coalesce(retnum,0)) retnum, ");
    sql.append(" sum(coalesce(retmny,0)) retmny,");
    sql.append(" sum(coalesce(retcostmny,0)) retcostmny, ");
    sql.append(" sum(coalesce(costnum,0)) costnum, ");
    sql.append(" sum(coalesce(retcostnum,0)) retcostnum ");

    sql.append(" from ");
    sql.append(sTempTable);
    sql.append(" group by ");
    sql.append(newField.substring(0, newField.length() - 1));
    sql.append(" ) a ");
    sql.append(JoinTable);

    SaleGrossprofitVO[] retVOs = null;

    String[][] sResult = queryInData(sql.toString());

    int iFieldLength = vAttrs.size();
    int length = sResult.length;
    List list = new ArrayList(length);

    for (int i = 0; i < length; i++) {
      String[] sTemp = sResult[i];

      boolean bIsZero = true;
      UFDouble initnumber = new UFDouble(sTemp[iFieldLength]);
      UFDouble initmoney = new UFDouble(sTemp[(iFieldLength + 1)]);
      UFDouble curpurnumber = new UFDouble(sTemp[(iFieldLength + 2)]);
      UFDouble curpurmoney = new UFDouble(sTemp[(iFieldLength + 3)]);
      UFDouble cursonumber = new UFDouble(sTemp[(iFieldLength + 4)]);
      UFDouble cursomoney = new UFDouble(sTemp[(iFieldLength + 5)]);
      UFDouble cursocost = new UFDouble(sTemp[(iFieldLength + 6)]);
      UFDouble lastnumber = new UFDouble(sTemp[(iFieldLength + 7)]);
      UFDouble lastmoney = new UFDouble(sTemp[(iFieldLength + 8)]);
      UFDouble retnum = new UFDouble(sTemp[(iFieldLength + 9)]);
      UFDouble retmny = new UFDouble(sTemp[(iFieldLength + 10)]);
      UFDouble retcostmny = new UFDouble(sTemp[(iFieldLength + 11)]);

      UFDouble costnum = new UFDouble(sTemp[(iFieldLength + 12)]);
      UFDouble retcostnum = new UFDouble(sTemp[(iFieldLength + 13)]);
      if ((bIsZero) && (initnumber.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (initmoney.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (curpurnumber.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (curpurmoney.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (cursonumber.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (cursomoney.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (cursocost.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (lastnumber.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (lastmoney.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (retnum.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (retmny.doubleValue() != 0.0D)) {
        bIsZero = false;
      }
      if ((bIsZero) && (retcostmny.doubleValue() != 0.0D)) {
        bIsZero = false;
      }

      if (bIsZero) {
        continue;
      }
      ReturnVO sgVO = new ReturnVO();

      for (int j = 0; j < iFieldLength; j++) {
        sgVO.setAttributeValue(vAttrs.elementAt(j).toString(), sTemp[j]);
      }

      sgVO.setAttributeValue("initnumber", initnumber);
      sgVO.setAttributeValue("initmoney", initmoney);
      sgVO.setAttributeValue("curpurnumber", curpurnumber);
      sgVO.setAttributeValue("curpurmoney", curpurmoney);
      sgVO.setAttributeValue("cursonumber", cursonumber);
      sgVO.setAttributeValue("cursomoney", cursomoney);
      sgVO.setAttributeValue("cursocost", cursocost);
      sgVO.setAttributeValue("lastnumber", lastnumber);
      sgVO.setAttributeValue("lastmoney", lastmoney);
      sgVO.setAttributeValue("retnum", retnum);
      sgVO.setAttributeValue("retmny", retmny);
      sgVO.setAttributeValue("retcostmny", retcostmny);

      sgVO.setAttributeValue("cursocostnum", costnum);
      sgVO.setAttributeValue("cursocostretnum", retcostnum);

      list.add(sgVO);
    }

    retVOs = new ReturnVO[list.size()];
    retVOs = (SaleGrossprofitVO[])(SaleGrossprofitVO[])list.toArray(retVOs);

    return retVOs;
  }

  private String getAuditKey(Object crdcenterid, Object cinventoryid, Object vbatch)
  {
    StringBuffer key = new StringBuffer();
    key.append(cinventoryid);
    key.append(",");
    key.append(crdcenterid);
    if (vbatch != null) {
      key.append(",");
      key.append(vbatch);
    }
    return key.toString();
  }

  private UFDouble getMoney(Map index, String cfirstbillitemid, UFDouble number)
  {
    UFDouble money = new UFDouble(0);
    ArrayList list = (ArrayList)index.get(cfirstbillitemid);
    if (list == null) {
      return money;
    }

    UFDouble dTempIncomeNum = number;

    int size = list.size();
    for (int i = 0; i < size; i++) {
      List detail = (List)list.get(i);
      UFDouble dCostNum = (UFDouble)detail.get(1);
      UFDouble dCostMny = (UFDouble)detail.get(2);
      if (dCostNum == null) {
        dCostNum = new UFDouble(0);
      }
      if (dCostMny == null) {
        dCostMny = new UFDouble(0);
      }

      if (dCostNum.doubleValue() < 0.0D) {
        dCostNum = dCostNum.multiply(-1.0D);
        dCostMny = dCostMny.multiply(-1.0D);
      }

      if (dTempIncomeNum.compareTo(dCostNum) <= 0) {
        money = money.add(dTempIncomeNum.div(dCostNum).multiply(dCostMny));

        break;
      }

      if (dTempIncomeNum.compareTo(dCostNum) > 0) {
        money = money.add(dCostMny);
        dTempIncomeNum = dTempIncomeNum.sub(dCostNum);
      }
    }

    return money;
  }

  private void update(String sql) throws Exception {
    SmartDMO dmo = new SmartDMO();
    dmo.executeUpdate(sql, new ArrayList(), new ArrayList());
  }

  private Object[] query(String sql) throws Exception {
    SmartDMO dmo = new SmartDMO();
    Object[] data = dmo.selectBy2(sql);
    return data;
  }

  private ArrayList calculateVirtualCost(Object[] data, Map costIndex, String pk_corp)
    throws Exception
  {
    ArrayList list = new ArrayList();
    Map index = new HashMap();
    int length = data.length;
    if (length == 0) {
      return list;
    }
    for (int i = 0; i < length; i++) {
      Object[] row = (Object[])(Object[])data[i];

      String key = getAuditKey(row[0], row[1], row[2]);
      if ((!index.containsKey(key)) && (!costIndex.containsKey(key))) {
        String[] condition = new String[3];
        condition[0] = ((String)row[1]);
        condition[1] = ((String)row[0]);
        condition[2] = ((String)row[2]);
        index.put(key, condition);
      }
    }
    String[][] condition = new String[index.size()][3];
    condition = (String[][])(String[][])index.values().toArray(condition);

    Object obj = NCLocator.getInstance().lookup(IIAToSO.class.getName());

    if ((obj != null) && (condition.length > 0)) {
      Map map = ((IIAToSO)obj).getCostPrice(pk_corp, condition);

      costIndex.putAll(map);
    }

    int rowLength = ((Object[])(Object[])data[0]).length;

    for (int i = 0; i < length; i++) {
      Object[] row = (Object[])(Object[])data[i];

      String key = getAuditKey(row[0], row[1], row[2]);
      UFDouble price = (UFDouble)costIndex.get(key);
      ArrayList rowList = new ArrayList();

      for (int j = 4; j < rowLength; j++) {
        rowList.add(row[j]);
      }
      UFDouble number = row[3] == null ? null : new UFDouble(row[3].toString());
      UFDouble money = null;
      if (number != null) {
        money = price.multiply(number);
      }

      if (money.doubleValue() == 0.0D) {
        continue;
      }
      rowList.add(money);
      list.add(rowList);
    }
    return list;
  }

  private void persistent(String tabelName, String fieldNames, String costFieldName, ArrayList data)
    throws Exception
  {
    if (data.size() == 0) {
      return;
    }
    List row = (List)data.get(0);
    int rowLength = row.size();
    if (rowLength == 0) {
      return;
    }

    StringBuffer sql = new StringBuffer();
    sql.append(" insert into ");
    sql.append(tabelName);
    sql.append("(");
    sql.append(fieldNames);
    sql.append(costFieldName);
    sql.append(" ) values ( ? ");
    for (int i = 1; i < rowLength; i++) {
      sql.append(", ? ");
    }
    sql.append(" ) ");
    ArrayList metaData = new ArrayList();
    for (int i = 0; i < rowLength - 1; i++) {
      metaData.add(new Integer(3));
    }
    metaData.add(new Integer(1));

    SmartDMO dmo = new SmartDMO();
    dmo.executeUpdateBatch(sql.toString(), data, metaData);
  }

  private ArrayList calculateActualCost(Object[] data, String pk_corp)
    throws Exception
  {
    ArrayList list = new ArrayList();
    Map index = new HashMap();
    int length = data.length;
    if (length == 0) {
      return list;
    }
    for (int i = 0; i < length; i++) {
      Object[] row = (Object[])(Object[])data[i];

      String key = (String)row[0];
      if (!index.containsKey(key)) {
        index.put(key, key);
      }
    }
    String[] condition = new String[index.size()];
    condition = (String[])(String[])index.values().toArray(condition);

    Map map = new HashMap();

    Object obj = NCLocator.getInstance().lookup(IIAToSO.class.getName());
    if ((obj != null) && (condition.length > 0)) {
      map = ((IIAToSO)obj).getSaleCostMnyDetail("30", condition, pk_corp);
    }

    int rowLength = ((Object[])(Object[])data[0]).length;
    for (int i = 0; i < length; i++) {
      Object[] row = (Object[])(Object[])data[i];

      String key = (String)row[0];
      ArrayList rowList = new ArrayList();

      for (int j = 2; j < rowLength; j++) {
        rowList.add(row[j]);
      }
      UFDouble money = getMoney(map, key, new UFDouble(row[1].toString()));
      if (money.doubleValue() == 0.0D) {
        continue;
      }
      rowList.add(money);
      list.add(rowList);
    }
    return list;
  }

  private String getCalInfo(String strID)
    throws SQLException
  {
    String info = "";

    String strSQL = "Select bodyname From ";
    String strField = null;

    strSQL = strSQL + " bd_calbody ";
    strField = "pk_calbody";

    strSQL = strSQL + " Where " + strField + " =  ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, strID);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String code = rs.getString(1);

        info = code;
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return info;
  }

  private String getInvInfo(String strID)
    throws SQLException
  {
    String info = "";

    String strSQL = "Select invcode,invname From ";
    String strField = null;

    strSQL = strSQL + " bd_invbasdoc ";
    strField = "pk_invbasdoc";

    strSQL = strSQL + " Where " + strField + " =  ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, strID);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String code = rs.getString(1);
        String name = rs.getString(2);

        info = code;
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return info;
  }
}