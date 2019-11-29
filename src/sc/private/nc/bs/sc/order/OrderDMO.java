package nc.bs.sc.order;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.para.SysInitBO;
import nc.bs.pub.pf.IBackCheckState;
import nc.bs.pub.pf.ICheckState;
import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.sc.ct.ScFromCtImpl;
import nc.bs.sc.pub.BatchCodeDMO;
import nc.bs.sc.pub.GetSysBillCode;
import nc.bs.sc.pub.PublicDMO;
import nc.bs.sc.pub.SCATP;
import nc.bs.scm.pub.ScmPubDMO;
import nc.bs.scm.pub.TempTableDMO;
import nc.bs.sm.createcorp.CreatecorpDMO;
import nc.itf.sc.inter.IScToPu_OrderDMO;
import nc.itf.uap.bd.prayvsbusitype.IPrayvsBusiQry;
import nc.vo.bd.b999.PrayvsbusiVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.sc.order.NewPraybillItemVO;
import nc.vo.sc.order.OrderBbVO;
import nc.vo.sc.order.OrderHeaderVO;
import nc.vo.sc.order.OrderItemVO;
import nc.vo.sc.order.OrderVO;
import nc.vo.sc.pub.BD_ConvertVO;
import nc.vo.sc.pub.RetScVrmAndParaPriceVO;
import nc.vo.sc.pub.SCPubVO;
import nc.vo.sc.pub.ScConstants;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

public class OrderDMO extends DataManageObject
  implements IQueryData, ICheckState, IQueryData2, IScToPu_OrderDMO, IBackCheckState
{
  public OrderDMO()
    throws NamingException, SystemException
  {
  }

  public OrderDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public void backNoState(String billId, String approveId, String approveDate, String backNote)
    throws Exception
  {
    String sql = "update sc_order set ibillstatus = 0, cauditpsn = null, dauditdate = null where corderid = ? and dr = 0";
    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();

      stmt = prepareStatement(con, sql);

      stmt.setString(1, billId);

      executeUpdate(stmt);

      executeBatch(stmt);
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
  }

  public void backGoing(String billId, String approveId, String approveDate, String backNote)
    throws Exception
  {
  }

  public UFDouble[] getNewPrices(String pk_corp, String[] saMangId, String[] saCurrId, int iPriorPrice)
    throws Exception
  {
    if ((saMangId == null) || (saCurrId == null)) {
      return null;
    }

    String sPriceField = iPriorPrice == 6 ? "noriginalnetprice" : "norgnettaxprice";

    int iLen = saMangId.length;
    ArrayList listTempTableValue = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      if ((SCPubVO.getString_TrimZeroLenAsNull(saMangId[i]) == null) || (SCPubVO.getString_TrimZeroLenAsNull(saCurrId[i]) == null)) {
        continue;
      }
      ArrayList listElement = new ArrayList();
      listElement.add(saMangId[i]);
      listElement.add(saCurrId[i]);

      listTempTableValue.add(listElement);
    }
    String sTempTableName = new TempTableDMO().getTempStringTable("t_pu_po_clms_01", new String[] { "cmangid", "ccurrencytypeid" }, new String[] { "char(20) not null ", "char(20) not null " }, null, listTempTableValue);

    String sSql = "(SELECT sc_order_b.cmangid,sc_order_b.ccurrencytypeid,MAX(sc_order.dorderdate) AS dorderdate";
    sSql = sSql + " FROM " + sTempTableName + ",sc_order,sc_order_b";
    sSql = sSql + " WHERE " + sTempTableName + ".cmangid=sc_order_b.cmangid ";
    sSql = sSql + " AND " + sTempTableName + ".ccurrencytypeid=sc_order_b.ccurrencytypeid ";
    sSql = sSql + " AND sc_order.corderid = sc_order_b.corderid";
    sSql = sSql + " AND (sc_order.dr=0)";
    sSql = sSql + " AND (sc_order_b.dr=0)";
    sSql = sSql + " AND  sc_order_b." + sPriceField + " IS NOT NULL";
    sSql = sSql + " AND  sc_order_b.pk_corp = '" + pk_corp + "'";
    sSql = sSql + " GROUP BY sc_order_b.cmangid,sc_order_b.ccurrencytypeid) A";

    String sSqlMain = "SELECT sc_order_b.cmangid,sc_order_b.ccurrencytypeid,sc_order_b." + sPriceField;
    sSqlMain = sSqlMain + " FROM " + sSql + " JOIN sc_order_b ON A.cmangid=sc_order_b.cmangid";
    sSqlMain = sSqlMain + " AND A.ccurrencytypeid=sc_order_b.ccurrencytypeid ";
    sSqlMain = sSqlMain + " ORDER BY sc_order_b.ts DESC ";

    String sKey = null;

    HashMap hmapMangId = new HashMap();
    Connection con = null;
    Statement stmt = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sSqlMain);

      while (rs.next()) {
        sKey = rs.getString(1) + rs.getString(2);

        UFDouble dValue = SCPubVO.getUFDouble_ValueAsValue(rs.getDouble(3));
        if (dValue == null)
          hmapMangId.put(sKey, "");
        else {
          hmapMangId.put(sKey, dValue);
        }

      }

      rs.close();
    } finally {
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
    if (hmapMangId == null) {
      return null;
    }

    UFDouble[] daPrice = new UFDouble[iLen];
    for (int i = 0; i < iLen; i++) {
      sKey = saMangId[i] + saCurrId[i];
      daPrice[i] = ((UFDouble)hmapMangId.get(sKey));
    }

    return daPrice;
  }

  public UFDouble[] getLowPrices(String pk_corp, String[] saMangId, String[] saCurrId, int iPriorPrice)
    throws Exception
  {
    String sql = "";
    if ((saMangId == null) || (saCurrId == null)) {
      return null;
    }

    String sPriceField = iPriorPrice == 6 ? "noriginalnetprice" : "norgnettaxprice";

    int iLen = saMangId.length;
    ArrayList listTempTableValue = new ArrayList();
    for (int i = 0; i < iLen; i++) {
      if ((SCPubVO.getString_TrimZeroLenAsNull(saMangId[i]) == null) || (SCPubVO.getString_TrimZeroLenAsNull(saCurrId[i]) == null)) {
        continue;
      }
      ArrayList listElement = new ArrayList();
      listElement.add(saMangId[i]);
      listElement.add(saCurrId[i]);

      listTempTableValue.add(listElement);
    }
    String sTempTableName = new TempTableDMO().getTempStringTable("t_pu_po_clms_02", new String[] { "cmangid", "ccurrencytypeid" }, new String[] { "char(20) not null ", "char(20) not null " }, null, listTempTableValue);

    sql = "SELECT sc_order_b.cmangid,sc_order_b.ccurrencytypeid,MIN(sc_order_b." + sPriceField + ")";
    sql = sql + " FROM " + SCPubVO.getJoinTableBy2Table(sTempTableName, "cmangid", "sc_order_b", "cmangid");
    sql = sql + " AND " + sTempTableName + ".ccurrencytypeid=sc_order_b.ccurrencytypeid";
    sql = sql + " WHERE ";
    sql = sql + "(sc_order_b.dr=0)";
    sql = sql + " AND pk_corp = '" + pk_corp + "' AND " + sPriceField + " IS NOT NULL";
    sql = sql + " GROUP BY sc_order_b.cmangid,sc_order_b.ccurrencytypeid";

    String sKey = null;

    HashMap hmapMangId = new HashMap();
    Connection con = null;
    Statement stmt = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        sKey = rs.getString(1) + rs.getString(2);

        UFDouble dValue = SCPubVO.getUFDouble_ValueAsValue(rs.getDouble(3));
        if (dValue == null)
          hmapMangId.put(sKey, "");
        else {
          hmapMangId.put(sKey, dValue);
        }

      }

      rs.close();
    } finally {
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
    if (hmapMangId == null) {
      return null;
    }

    UFDouble[] daPrice = new UFDouble[iLen];
    for (int i = 0; i < iLen; i++) {
      sKey = saMangId[i] + saCurrId[i];
      daPrice[i] = ((UFDouble)hmapMangId.get(sKey));
    }

    return daPrice;
  }

  public UFDouble[] getCostPrices(String pk_corp, String sStoOrgId, String[] saMangId)
    throws Exception
  {
    if ((SCPubVO.getString_TrimZeroLenAsNull(pk_corp) == null) || (SCPubVO.getString_TrimZeroLenAsNull(sStoOrgId) == null) || (saMangId == null)) {
      return null;
    }

    String sTempTable = null;
    try {
      sTempTable = new TempTableDMO().getTempTable(saMangId, "t_sc_general", "pk_sc");
    } catch (Exception e) {
      reportException(e);
      throw e;
    }

    String sql = "SELECT ckcb,pk_invmandoc FROM " + SCPubVO.getJoinTableBy2Table(sTempTable, "pk_sc", "bd_produce", "pk_invmandoc") + " WHERE pk_corp = ? AND pk_calbody= ? " + " AND ckcb IS NOT NULL";

    HashMap mapRet = new HashMap();

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, pk_corp);
      stmt.setString(2, sStoOrgId);
      rs = stmt.executeQuery();

      UFDouble dValue = null;
      while (rs.next()) {
        dValue = SCPubVO.getUFDouble_ValueAsValue(rs.getBigDecimal(1));
        if (dValue != null)
          mapRet.put(rs.getString(2), dValue);
      }
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception e) {
      }
    }
    int iLen = saMangId.length;
    UFDouble[] daPrice = new UFDouble[iLen];
    if (mapRet.size() > 0) {
      for (int i = 0; i < iLen; i++) {
        daPrice[i] = ((UFDouble)mapRet.get(saMangId[i]));
      }
    }
    return daPrice;
  }

  public HashMap queryCostPricesFrmInvman(String pk_corp, String[] sMangIds)
    throws RemoteException, SQLException
  {
    if ((sMangIds == null) || (sMangIds.length == 0)) {
      return null;
    }

    StringBuffer sbSql = new StringBuffer();
    sbSql.append("select pk_invbasdoc,pk_invmandoc,costprice from bd_invmandoc where pk_invmandoc in ");

    String strIdsSet = "";
    try {
      TempTableDMO tmpTblDmo = new TempTableDMO();
      strIdsSet = tmpTblDmo.insertTempTable(sMangIds, "t_sc_general", "pk_sc");
      if ((strIdsSet == null) || (strIdsSet.trim().length() == 0))
        strIdsSet = "('TempTableDMOError')";
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }
    sbSql.append(strIdsSet + " ");

    sbSql.append(" and pk_corp='");
    sbSql.append(pk_corp);
    sbSql.append("'");

    HashMap hResult = new HashMap();

    String sPk_invMangdoc = null;

    UFDouble uCostPrice = null;

    Object oTemp = null;

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();
      while (rs.next()) {
        oTemp = rs.getObject("pk_invmandoc");
        if (oTemp != null) {
          sPk_invMangdoc = oTemp.toString();

          oTemp = rs.getObject("costprice");
          if ((oTemp == null) || (oTemp.toString().trim().equals("")))
            uCostPrice = new UFDouble(0);
          else
            uCostPrice = new UFDouble(oTemp.toString());
          hResult.put(sPk_invMangdoc, uCostPrice);
        }
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
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
    return hResult;
  }

  public UFDouble[] getPlanPrices(String pk_corp, String sStoOrgId, String[] saMangId)
    throws Exception
  {
    if ((SCPubVO.getString_TrimZeroLenAsNull(pk_corp) == null) || (SCPubVO.getString_TrimZeroLenAsNull(sStoOrgId) == null) || (saMangId == null)) {
      return null;
    }

    String sTempTable = null;
    try {
      sTempTable = new TempTableDMO().getTempTable(saMangId, "t_sc_general", "pk_sc");
    } catch (Exception e) {
      reportException(e);
      throw e;
    }
    String sql = "SELECT jhj,pk_invmandoc FROM " + SCPubVO.getJoinTableBy2Table(sTempTable, "pk_sc", "bd_produce", "pk_invmandoc") + " WHERE pk_corp = ? AND pk_calbody= ? " + " AND jhj IS NOT NULL";

    HashMap mapRet = new HashMap();

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, pk_corp);
      stmt.setString(2, sStoOrgId);
      rs = stmt.executeQuery();

      UFDouble dValue = null;
      while (rs.next()) {
        dValue = SCPubVO.getUFDouble_ValueAsValue(rs.getBigDecimal(1));
        if (dValue != null)
          mapRet.put(rs.getString(2), dValue);
      }
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception e) {
      }
    }
    int iLen = saMangId.length;
    UFDouble[] daPrice = new UFDouble[iLen];
    if (mapRet.size() > 0) {
      for (int i = 0; i < iLen; i++) {
        daPrice[i] = ((UFDouble)mapRet.get(saMangId[i]));
      }
    }
    return daPrice;
  }

  public HashMap queryPlanPricesFrmInvMan(String[] sMangIds, String sCorpId)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pr.pray.PraybillDMO", "getPlanPriceForPr", new Object[] { sMangIds, sCorpId });

    if ((sMangIds == null) || (sMangIds.length == 0)) {
      return null;
    }
    StringBuffer sbSql = new StringBuffer();
    sbSql.append("select pk_invbasdoc,pk_invmandoc,planprice from bd_invmandoc where pk_invmandoc in ");

    String strIdsSet = "";
    try {
      TempTableDMO tmpTblDmo = new TempTableDMO();
      strIdsSet = tmpTblDmo.insertTempTable(sMangIds, "t_sc_general", "pk_sc");
      if ((strIdsSet == null) || (strIdsSet.trim().length() == 0))
        strIdsSet = "('TempTableDMOError')";
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }
    sbSql.append(strIdsSet + " ");

    sbSql.append(" and pk_corp='");
    sbSql.append(sCorpId);
    sbSql.append("'");

    HashMap hResult = new HashMap();

    String sPk_invMangdoc = null;

    UFDouble uPlanPrice = null;

    Object oTemp = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();
      while (rs.next()) {
        oTemp = rs.getObject("pk_invmandoc");
        if (oTemp != null) {
          sPk_invMangdoc = oTemp.toString();

          oTemp = rs.getObject("planprice");
          if (oTemp != null)
            uPlanPrice = new UFDouble(oTemp.toString());
          else
            uPlanPrice = null;
          hResult.put(sPk_invMangdoc, uPlanPrice);
        }
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
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

    afterCallMethod("nc.bs.pr.pray.PraybillDMO", "getPlanPriceForPr", new Object[] { sMangIds, sCorpId });

    return hResult;
  }

  public OrderVO findByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findByPrimaryKey", new Object[] { key });

    OrderVO vo = new OrderVO();

    OrderHeaderVO header = findHeaderByPrimaryKey(key);
    OrderItemVO[] items = null;
    if (header != null)
    {
      items = findItemsAllForHeader(header.getPrimaryKey());
    }

    vo.setParentVO(header);
    vo.setChildrenVO(getUnionItem(items));

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findByPrimaryKey", new Object[] { key });

    return vo;
  }

  public OrderHeaderVO findHeaderByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findHeaderByPrimaryKey", new Object[] { key });

    StringBuffer strbuf = new StringBuffer();
    strbuf.append(" select vordercode, pk_corp, cpurorganization, cwareid, dorderdate,\n ");
    strbuf.append(" cvendorid, caccountbankid, cdeptid, cemployeeid, cbiztype, creciever,\n ");
    strbuf.append(" cgiveinvoicevendor, ctransmodeid, ctermProtocolid, ibillstatus,\n ");
    strbuf.append(" vmemo, caccountyear, coperator,\n ");
    strbuf.append(" vdef1, vdef2, vdef3, vdef4, vdef5,\n ");
    strbuf.append(" vdef6, vdef7, vdef8, vdef9, vdef10 ,\n ");
    strbuf.append(" vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20 ,\n ");
    strbuf.append(" cvendormangid, cauditpsn, dauditdate,\n ");
    strbuf.append(" ts, iprintcount, tmaketime, taudittime, tlastmaketime \n ");
    strbuf.append(" from sc_order where corderid = ?\n ");

    String sql = strbuf.toString();

    OrderHeaderVO orderHeader = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        orderHeader = new OrderHeaderVO(key);

        String vordercode = rs.getString(1);
        orderHeader.setVordercode(vordercode == null ? null : vordercode.trim());

        String pk_corp = rs.getString(2);
        orderHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cpurorganization = rs.getString(3);
        orderHeader.setCpurorganization(cpurorganization == null ? null : cpurorganization.trim());

        String cwareid = rs.getString(4);
        orderHeader.setCwareid(cwareid == null ? null : cwareid.trim());

        String dorderdate = rs.getString(5);
        orderHeader.setDorderdate(dorderdate == null ? null : new UFDate(dorderdate.trim(), false));

        String cvendorid = rs.getString(6);
        orderHeader.setCvendorid(cvendorid == null ? null : cvendorid.trim());

        String caccountbankid = rs.getString(7);
        orderHeader.setCaccountbankid(caccountbankid == null ? null : caccountbankid.trim());

        String cdeptid = rs.getString(8);
        orderHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(9);
        orderHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String cbiztype = rs.getString(10);
        orderHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        String creciever = rs.getString(11);
        orderHeader.setCreciever(creciever == null ? null : creciever.trim());

        String cgiveinvoicevendor = rs.getString(12);
        orderHeader.setCgiveinvoicevendor(cgiveinvoicevendor == null ? null : cgiveinvoicevendor.trim());

        String ctransmodeid = rs.getString(13);
        orderHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        String ctermProtocolid = rs.getString(14);
        orderHeader.setCtermProtocolid(ctermProtocolid == null ? null : ctermProtocolid.trim());

        Integer ibillstatus = (Integer)rs.getObject(15);
        orderHeader.setIbillstatus(ibillstatus == null ? null : ibillstatus);

        String vmemo = rs.getString(16);
        orderHeader.setVmemo(vmemo == null ? null : vmemo.trim());

        String caccountyear = rs.getString(17);
        orderHeader.setCaccountyear(caccountyear == null ? null : caccountyear.trim());

        String coperator = rs.getString(18);
        orderHeader.setCoperator(coperator == null ? null : coperator.trim());

        String vdef1 = rs.getString(19);
        orderHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(20);
        orderHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(21);
        orderHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(22);
        orderHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(23);
        orderHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(24);
        orderHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(25);
        orderHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(26);
        orderHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(27);
        orderHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(28);
        orderHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString(29);
        orderHeader.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString(30);
        orderHeader.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString(31);
        orderHeader.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString(32);
        orderHeader.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString(33);
        orderHeader.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString(34);
        orderHeader.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString(35);
        orderHeader.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString(36);
        orderHeader.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString(37);
        orderHeader.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString(38);
        orderHeader.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString(39);
        orderHeader.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(40);
        orderHeader.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(41);
        orderHeader.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(42);
        orderHeader.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(43);
        orderHeader.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(44);
        orderHeader.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(45);
        orderHeader.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(46);
        orderHeader.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(47);
        orderHeader.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(48);
        orderHeader.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(49);
        orderHeader.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(50);
        orderHeader.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(51);
        orderHeader.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(52);
        orderHeader.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(53);
        orderHeader.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(54);
        orderHeader.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(55);
        orderHeader.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(56);
        orderHeader.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(57);
        orderHeader.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(58);
        orderHeader.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String cvendormangid = rs.getString(59);
        orderHeader.setCvendormangid(cvendormangid == null ? null : cvendormangid.trim());

        String cauditpsn = rs.getString(60);
        orderHeader.setCauditpsn(cauditpsn == null ? null : cauditpsn.trim());

        String dauditdate = rs.getString(61);
        orderHeader.setDauditdate(dauditdate == null ? null : new UFDate(dauditdate.trim(), false));

        String ts = rs.getString(62);
        orderHeader.setTs(ts == null ? null : ts.trim());

        Integer iprintcount = (Integer)rs.getObject(63);
        orderHeader.setIprintcount(iprintcount == null ? null : iprintcount);

        String tmaketime = rs.getString(64);
        orderHeader.setTmaketime(tmaketime == null ? null : tmaketime.trim());
        String taudittime = rs.getString(65);
        orderHeader.setTaudittime(taudittime == null ? null : taudittime.trim());
        String tlastmaketime = rs.getString(66);
        orderHeader.setTlastmaketime(tlastmaketime == null ? null : tlastmaketime.trim());
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findHeaderByPrimaryKey", new Object[] { key });

    return orderHeader;
  }

  public OrderItemVO findItemByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findItemByPrimaryKey", new Object[] { key });

    String sql = "select corderid, pk_corp, cmangid, cbaseid, nordernum, cassistunit, nassistnum, ndiscountrate, idiscounttaxtype," +
    		" ntaxrate, ccurrencytypeid, noriginalnetprice, noriginalcurprice, noriginalcurmny, noriginaltaxmny, noriginalsummny," +
    		" nexchangeotobrate, ntaxmny, nmoney, nsummny, nexchangeotoarate, nassistcurmny, nassisttaxmny, nassistsummny, naccumarrvnum," +
    		" naccumstorenum, naccuminvoicenum, naccumwastnum, dplanarrvdate, cwarehouseid, creceiveaddress, cprojectid, cprojectphaseid," +
    		" coperator, forderrowstatus, bisactive, cordersource, csourcebillid, csourcebillrow, cupsourcebilltype, cupsourcebillid," +
    		" cupsourcebillrowid, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8," +
    		" vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2," +
    		" pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12," +
    		" pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20, ts, crowno, norgtaxprice," +
    		" norgnettaxprice,vproducenum,ccontractid,ccontractrowid,ccontractrcode,vpriceauditcode,cpriceauditid,cpriceaudit_bid," +
    		"cpriceaudit_bb1id,bomvers " +//shikun bomvers
    		"from sc_order_b where corder_bid = ?";

    OrderItemVO orderItem = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        orderItem = new OrderItemVO(key);

        String corderid = rs.getString(1);
        orderItem.setCorderid(corderid == null ? null : corderid.trim());

        String pk_corp = rs.getString(2);
        orderItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cmangid = rs.getString(3);
        orderItem.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString(4);
        orderItem.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal nordernum = rs.getBigDecimal(5);
        orderItem.setNordernum(nordernum == null ? null : new UFDouble(nordernum));

        String cassistunit = rs.getString(6);
        orderItem.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = rs.getBigDecimal(7);
        orderItem.setNassistnum(nassistnum == null ? null : new UFDouble(nassistnum));

        BigDecimal ndiscountrate = rs.getBigDecimal(8);
        orderItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        Integer idiscounttaxtype = (Integer)rs.getObject(9);
        orderItem.setIdiscounttaxtype(idiscounttaxtype == null ? null : idiscounttaxtype);

        BigDecimal ntaxrate = rs.getBigDecimal(10);
        orderItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        String ccurrencytypeid = rs.getString(11);
        orderItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalnetprice = rs.getBigDecimal(12);
        orderItem.setNoriginalnetprice(noriginalnetprice == null ? null : new UFDouble(noriginalnetprice));

        BigDecimal noriginalcurprice = rs.getBigDecimal(13);
        orderItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurmny = rs.getBigDecimal(14);
        orderItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginaltaxmny = rs.getBigDecimal(15);
        orderItem.setNoriginaltaxmny(noriginaltaxmny == null ? null : new UFDouble(noriginaltaxmny));

        BigDecimal noriginalsummny = rs.getBigDecimal(16);
        orderItem.setNoriginalsummny(noriginalsummny == null ? null : new UFDouble(noriginalsummny));

        BigDecimal nexchangeotobrate = rs.getBigDecimal(17);
        orderItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal ntaxmny = rs.getBigDecimal(18);
        orderItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmoney = rs.getBigDecimal(19);
        orderItem.setNmoney(nmoney == null ? null : new UFDouble(nmoney));

        BigDecimal nsummny = rs.getBigDecimal(20);
        orderItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nexchangeotoarate = rs.getBigDecimal(21);
        orderItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nassistcurmny = rs.getBigDecimal(22);
        orderItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassisttaxmny = rs.getBigDecimal(23);
        orderItem.setNassisttaxmny(nassisttaxmny == null ? null : new UFDouble(nassisttaxmny));

        BigDecimal nassistsummny = rs.getBigDecimal(24);
        orderItem.setNassistsummny(nassistsummny == null ? null : new UFDouble(nassistsummny));

        BigDecimal naccumarrvnum = rs.getBigDecimal(25);
        orderItem.setNaccumarrvnum(naccumarrvnum == null ? null : new UFDouble(naccumarrvnum));

        BigDecimal naccumstorenum = rs.getBigDecimal(26);
        orderItem.setNaccumstorenum(naccumstorenum == null ? null : new UFDouble(naccumstorenum));

        BigDecimal naccuminvoicenum = rs.getBigDecimal(27);
        orderItem.setNaccuminvoicenum(naccuminvoicenum == null ? null : new UFDouble(naccuminvoicenum));

        BigDecimal naccumwastnum = rs.getBigDecimal(28);
        orderItem.setNaccumwastnum(naccumwastnum == null ? null : new UFDouble(naccumwastnum));

        String dplanarrvdate = rs.getString(29);
        orderItem.setDplanarrvdate(dplanarrvdate == null ? null : new UFDate(dplanarrvdate.trim(), false));

        String cwarehouseid = rs.getString(30);
        orderItem.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String creceiveaddress = rs.getString(31);
        orderItem.setCreceiveaddress(creceiveaddress == null ? null : creceiveaddress.trim());

        String cprojectid = rs.getString(32);
        orderItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString(33);
        orderItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String coperator = rs.getString(34);
        orderItem.setCoperator(coperator == null ? null : coperator.trim());

        Integer forderrowstatus = (Integer)rs.getObject(35);
        orderItem.setForderrowstatus(forderrowstatus == null ? null : forderrowstatus);

        String bisactive = rs.getString(36);
        if (bisactive == null)
          orderItem.setBisactive(null);
        else if (bisactive.trim().equals("1"))
          orderItem.setBisactive(new UFBoolean(false));
        else {
          orderItem.setBisactive(new UFBoolean(true));
        }
        String cordersource = rs.getString(37);
        orderItem.setCordersource(cordersource == null ? null : cordersource.trim());

        String csourcebillid = rs.getString(38);
        orderItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillrow = rs.getString(39);
        orderItem.setCsourcebillrow(csourcebillrow == null ? null : csourcebillrow.trim());

        String cupsourcebilltype = rs.getString(40);
        orderItem.setCupsourcebilltype(cupsourcebilltype == null ? null : cupsourcebilltype.trim());

        String cupsourcebillid = rs.getString(41);
        orderItem.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillrowid = rs.getString(42);
        orderItem.setCupsourcebillrowid(cupsourcebillrowid == null ? null : cupsourcebillrowid.trim());

        String vmemo = rs.getString(43);
        orderItem.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString(44);
        orderItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(45);
        orderItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(46);
        orderItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(47);
        orderItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(48);
        orderItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString(49);
        orderItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(50);
        orderItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(51);
        orderItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(52);
        orderItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(53);
        orderItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(54);
        orderItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(55);
        orderItem.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(56);
        orderItem.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(57);
        orderItem.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(58);
        orderItem.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString(59);
        orderItem.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString(60);
        orderItem.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString(61);
        orderItem.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString(62);
        orderItem.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString(63);
        orderItem.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString(64);
        orderItem.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString(65);
        orderItem.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString(66);
        orderItem.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString(67);
        orderItem.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString(68);
        orderItem.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString(69);
        orderItem.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(70);
        orderItem.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(71);
        orderItem.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(72);
        orderItem.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(73);
        orderItem.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(74);
        orderItem.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(75);
        orderItem.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(76);
        orderItem.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(77);
        orderItem.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(78);
        orderItem.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(79);
        orderItem.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(80);
        orderItem.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(81);
        orderItem.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(82);
        orderItem.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(83);
        orderItem.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(84);
        orderItem.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(85);
        orderItem.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(86);
        orderItem.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(87);
        orderItem.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(88);
        orderItem.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String ts = rs.getString(89);
        orderItem.setTs(ts == null ? null : ts.trim());

        String crowno = rs.getString(90);
        orderItem.setCrowno(crowno == null ? null : crowno.trim());

        BigDecimal norgtaxprice = rs.getBigDecimal(91);
        orderItem.setNorgtaxprice(norgtaxprice == null ? null : new UFDouble(norgtaxprice));

        BigDecimal norgnettaxprice = rs.getBigDecimal(92);
        orderItem.setNorgnettaxprice(norgnettaxprice == null ? null : new UFDouble(norgnettaxprice));

        String vproducenum = rs.getString(93);
        orderItem.setVproducenum(vproducenum == null ? null : vproducenum.trim());

        String ccontractid = rs.getString(94);
        orderItem.setCcontractid(ccontractid == null ? null : ccontractid.trim());

        String ccontractrowid = rs.getString(95);
        orderItem.setCcontractrowid(ccontractrowid == null ? null : ccontractrowid.trim());

        String ccontractrcode = rs.getString(96);
        orderItem.setCcontractrcode(ccontractrcode == null ? null : ccontractrcode.trim());

        String vpriceauditcode = rs.getString(97);
        orderItem.setVpriceauditcode(vpriceauditcode == null ? null : vpriceauditcode.trim());

        String cpriceauditid = rs.getString(98);
        orderItem.setCpriceauditid(cpriceauditid == null ? null : cpriceauditid.trim());

        String cpriceaudit_bid = rs.getString(99);
        orderItem.setCpriceaudit_bid(cpriceaudit_bid == null ? null : cpriceaudit_bid.trim());

        String cpriceaudit_bb1id = rs.getString(100);
        orderItem.setCpriceaudit_bb1id(cpriceaudit_bb1id == null ? null : cpriceaudit_bb1id.trim());

        String bomvers = rs.getString(101);
        orderItem.setBomvers(bomvers == null ? null : bomvers.trim());
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findItemByPrimaryKey", new Object[] { key });

    return orderItem;
  }

  public OrderItemVO[] findItemsForHeader(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findItemsForHeader", new Object[] { key });

    String sql = "select corder_bid, corderid, pk_corp, cmangid, cbaseid, nordernum,";
    sql = sql + " cassistunit, nassistnum, ndiscountrate, idiscounttaxtype, ntaxrate,";
    sql = sql + " ccurrencytypeid, noriginalnetprice, noriginalcurprice, noriginalcurmny,";
    sql = sql + "  noriginaltaxmny, noriginalsummny, nexchangeotobrate, ntaxmny, nmoney,";
    sql = sql + " nsummny, nexchangeotoarate, nassistcurmny, nassisttaxmny, nassistsummny,";
    sql = sql + " naccumarrvnum, naccumstorenum, naccuminvoicenum, naccumwastnum,";
    sql = sql + " dplanarrvdate, cwarehouseid, creceiveaddress, cprojectid, ";
    sql = sql + " cprojectphaseid, coperator, forderrowstatus, bisactive, cordersource,";
    sql = sql + " csourcebillid, csourcebillrow, cupsourcebilltype, cupsourcebillid,";
    sql = sql + " cupsourcebillrowid, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5,";
    sql = sql + " vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13," +
    		" vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3," +
    		" pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11," +
    		" pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, " +
    		"pk_defdoc19, pk_defdoc20, ts, crowno, norgtaxprice, norgnettaxprice, vproducenum,ccontractid," +
    		"ccontractrowid,ccontractrcode,vpriceauditcode,cpriceauditid,cpriceaudit_bid,cpriceaudit_bb1id,bomvers ";//shikun bomvers
    sql = sql + " from sc_order_b where dr=0 and corderid = ? ";

    OrderItemVO[] orderItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        OrderItemVO orderItem = new OrderItemVO();

        String corder_bid = rs.getString("corder_bid");
        orderItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String corderid = rs.getString("corderid");
        orderItem.setCorderid(corderid == null ? null : corderid.trim());

        String pk_corp = rs.getString("pk_corp");
        orderItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cmangid = rs.getString("cmangid");
        orderItem.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString("cbaseid");
        orderItem.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal nordernum = rs.getBigDecimal("nordernum");
        orderItem.setNordernum(nordernum == null ? null : new UFDouble(nordernum));
        orderItem.setNoldnum(nordernum == null ? null : new UFDouble(nordernum));

        String cassistunit = rs.getString("cassistunit");
        orderItem.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = rs.getBigDecimal("nassistnum");
        orderItem.setNassistnum(nassistnum == null ? null : new UFDouble(nassistnum));

        BigDecimal ndiscountrate = rs.getBigDecimal("ndiscountrate");
        orderItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        Integer idiscounttaxtype = (Integer)rs.getObject("idiscounttaxtype");
        orderItem.setIdiscounttaxtype(idiscounttaxtype == null ? null : idiscounttaxtype);

        BigDecimal ntaxrate = rs.getBigDecimal("ntaxrate");
        orderItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        orderItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalnetprice = rs.getBigDecimal("noriginalnetprice");
        orderItem.setNoriginalnetprice(noriginalnetprice == null ? null : new UFDouble(noriginalnetprice));

        BigDecimal noriginalcurprice = rs.getBigDecimal("noriginalcurprice");
        orderItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurmny = rs.getBigDecimal("noriginalcurmny");
        orderItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginaltaxmny = rs.getBigDecimal("noriginaltaxmny");
        orderItem.setNoriginaltaxmny(noriginaltaxmny == null ? null : new UFDouble(noriginaltaxmny));

        BigDecimal noriginalsummny = rs.getBigDecimal("noriginalsummny");
        orderItem.setNoriginalsummny(noriginalsummny == null ? null : new UFDouble(noriginalsummny));

        BigDecimal nexchangeotobrate = rs.getBigDecimal("nexchangeotobrate");
        orderItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal ntaxmny = rs.getBigDecimal("ntaxmny");
        orderItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmoney = rs.getBigDecimal("nmoney");
        orderItem.setNmoney(nmoney == null ? null : new UFDouble(nmoney));

        BigDecimal nsummny = rs.getBigDecimal("nsummny");
        orderItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nexchangeotoarate = rs.getBigDecimal("nexchangeotoarate");
        orderItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nassistcurmny = rs.getBigDecimal("nassistcurmny");
        orderItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassisttaxmny = rs.getBigDecimal("nassisttaxmny");
        orderItem.setNassisttaxmny(nassisttaxmny == null ? null : new UFDouble(nassisttaxmny));

        BigDecimal nassistsummny = rs.getBigDecimal("nassistsummny");
        orderItem.setNassistsummny(nassistsummny == null ? null : new UFDouble(nassistsummny));

        BigDecimal naccumarrvnum = rs.getBigDecimal("naccumarrvnum");
        orderItem.setNaccumarrvnum(naccumarrvnum == null ? null : new UFDouble(naccumarrvnum));

        BigDecimal naccumstorenum = rs.getBigDecimal("naccumstorenum");
        orderItem.setNaccumstorenum(naccumstorenum == null ? null : new UFDouble(naccumstorenum));

        BigDecimal naccuminvoicenum = rs.getBigDecimal("naccuminvoicenum");
        orderItem.setNaccuminvoicenum(naccuminvoicenum == null ? null : new UFDouble(naccuminvoicenum));

        BigDecimal naccumwastnum = rs.getBigDecimal("naccumwastnum");
        orderItem.setNaccumwastnum(naccumwastnum == null ? null : new UFDouble(naccumwastnum));

        String dplanarrvdate = rs.getString("dplanarrvdate");
        orderItem.setDplanarrvdate(dplanarrvdate == null ? null : new UFDate(dplanarrvdate.trim(), false));

        String cwarehouseid = rs.getString("cwarehouseid");
        orderItem.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String creceiveaddress = rs.getString("creceiveaddress");
        orderItem.setCreceiveaddress(creceiveaddress == null ? null : creceiveaddress.trim());

        String cprojectid = rs.getString("cprojectid");
        orderItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        orderItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String coperator = rs.getString("coperator");
        orderItem.setCoperator(coperator == null ? null : coperator.trim());

        Integer forderrowstatus = (Integer)rs.getObject("forderrowstatus");
        orderItem.setForderrowstatus(forderrowstatus == null ? null : forderrowstatus);

        String bisactive = rs.getString("bisactive");
        if (bisactive == null)
          orderItem.setBisactive(null);
        else if (bisactive.trim().equals("1"))
          orderItem.setBisactive(new UFBoolean(false));
        else {
          orderItem.setBisactive(new UFBoolean(true));
        }

        String cordersource = rs.getString("cordersource");
        orderItem.setCordersource(cordersource == null ? null : cordersource.trim());

        String csourcebillid = rs.getString("csourcebillid");
        orderItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillrow = rs.getString("csourcebillrow");
        orderItem.setCsourcebillrow(csourcebillrow == null ? null : csourcebillrow.trim());

        String cupsourcebilltype = rs.getString("cupsourcebilltype");
        orderItem.setCupsourcebilltype(cupsourcebilltype == null ? null : cupsourcebilltype.trim());

        String cupsourcebillid = rs.getString("cupsourcebillid");
        orderItem.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillrowid = rs.getString("cupsourcebillrowid");
        orderItem.setCupsourcebillrowid(cupsourcebillrowid == null ? null : cupsourcebillrowid.trim());

        String vmemo = rs.getString("vmemo");
        orderItem.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString("vfree1");
        orderItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        orderItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        orderItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        orderItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        orderItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        orderItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        orderItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        orderItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        orderItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        orderItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        orderItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString("vdef7");
        orderItem.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString("vdef8");
        orderItem.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString("vdef9");
        orderItem.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString("vdef10");
        orderItem.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString("vdef11");
        orderItem.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString("vdef12");
        orderItem.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString("vdef13");
        orderItem.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString("vdef14");
        orderItem.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString("vdef15");
        orderItem.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString("vdef16");
        orderItem.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString("vdef17");
        orderItem.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString("vdef18");
        orderItem.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString("vdef19");
        orderItem.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString("vdef20");
        orderItem.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString("pk_defdoc1");
        orderItem.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString("pk_defdoc2");
        orderItem.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString("pk_defdoc3");
        orderItem.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString("pk_defdoc4");
        orderItem.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString("pk_defdoc5");
        orderItem.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString("pk_defdoc6");
        orderItem.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString("pk_defdoc7");
        orderItem.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString("pk_defdoc8");
        orderItem.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString("pk_defdoc9");
        orderItem.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString("pk_defdoc10");
        orderItem.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString("pk_defdoc11");
        orderItem.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString("pk_defdoc12");
        orderItem.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString("pk_defdoc13");
        orderItem.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString("pk_defdoc14");
        orderItem.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString("pk_defdoc15");
        orderItem.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString("pk_defdoc16");
        orderItem.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString("pk_defdoc17");
        orderItem.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString("pk_defdoc18");
        orderItem.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString("pk_defdoc19");
        orderItem.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString("pk_defdoc20");
        orderItem.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String ts = rs.getString("ts");
        orderItem.setTs(ts == null ? null : ts.trim());

        String crowno = rs.getString("crowno");
        orderItem.setCrowno(crowno == null ? null : crowno.trim());

        BigDecimal norgtaxprice = rs.getBigDecimal("norgtaxprice");
        orderItem.setNorgtaxprice(norgtaxprice == null ? null : new UFDouble(norgtaxprice));

        BigDecimal norgnettaxprice = rs.getBigDecimal("norgnettaxprice");
        orderItem.setNorgnettaxprice(norgnettaxprice == null ? null : new UFDouble(norgnettaxprice));

        String vproducenum = rs.getString("vproducenum");
        orderItem.setVproducenum(vproducenum == null ? null : vproducenum.trim());

        String ccontractid = rs.getString("ccontractid");
        orderItem.setCcontractid(ccontractid == null ? null : ccontractid.trim());

        String ccontractrowid = rs.getString("ccontractrowid");
        orderItem.setCcontractrowid(ccontractrowid == null ? null : ccontractrowid.trim());

        String ccontractrcode = rs.getString("ccontractrcode");
        orderItem.setCcontractrcode(ccontractrcode == null ? null : ccontractrcode.trim());

        String vpriceauditcode = rs.getString("vpriceauditcode");
        orderItem.setVpriceauditcode(vpriceauditcode == null ? null : vpriceauditcode.trim());

        String cpriceauditid = rs.getString("cpriceauditid");
        orderItem.setCpriceauditid(cpriceauditid == null ? null : cpriceauditid.trim());

        String cpriceaudit_bid = rs.getString("cpriceaudit_bid");
        orderItem.setCpriceaudit_bid(cpriceaudit_bid == null ? null : cpriceaudit_bid.trim());

        String cpriceaudit_bb1id = rs.getString("cpriceaudit_bb1id");
        orderItem.setCpriceaudit_bb1id(cpriceaudit_bb1id == null ? null : cpriceaudit_bb1id.trim());

        String bomvers = rs.getString("bomvers");
        orderItem.setBomvers(bomvers == null ? null : bomvers.trim());

        v.addElement(orderItem);
      }
    } finally {
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
    orderItems = new OrderItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(orderItems);
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findItemsForHeader", new Object[] { key });

    return orderItems;
  }

  public String insertHeader(OrderHeaderVO orderHeader)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "insertHeader", new Object[] { orderHeader });

    String sql = "insert into sc_order(corderid, vordercode, pk_corp, cpurorganization, cwareid, dorderdate, cvendorid, caccountbankid, cdeptid, cemployeeid, cbiztype, creciever, cgiveinvoicevendor, ctransmodeid, ctermProtocolid, ibillstatus, vmemo, caccountyear, coperator, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20,cvendormangid,tmaketime,taudittime,tlastmaketime ) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    String pk_corp = orderHeader.getPk_corp();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      key = getOID(pk_corp);
      stmt.setString(1, key);

      if (orderHeader.getVordercode() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, orderHeader.getVordercode());
      }
      if (orderHeader.getPk_corp() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, orderHeader.getPk_corp());
      }
      if (orderHeader.getCpurorganization() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, orderHeader.getCpurorganization());
      }
      if (orderHeader.getCwareid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, orderHeader.getCwareid());
      }
      if (orderHeader.getDorderdate() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, orderHeader.getDorderdate().toString());
      }
      if (orderHeader.getCvendorid() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, orderHeader.getCvendorid());
      }
      if (orderHeader.getCaccountbankid() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, orderHeader.getCaccountbankid());
      }
      if (orderHeader.getCdeptid() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, orderHeader.getCdeptid());
      }
      if (orderHeader.getCemployeeid() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, orderHeader.getCemployeeid());
      }
      if (orderHeader.getCbiztype() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, orderHeader.getCbiztype());
      }
      if (orderHeader.getCreciever() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, orderHeader.getCreciever());
      }
      if (orderHeader.getCgiveinvoicevendor() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, orderHeader.getCgiveinvoicevendor());
      }
      if (orderHeader.getCtransmodeid() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, orderHeader.getCtransmodeid());
      }
      if (orderHeader.getCtermProtocolid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, orderHeader.getCtermProtocolid());
      }
      if (orderHeader.getIbillstatus() == null)
        stmt.setNull(16, 4);
      else {
        stmt.setInt(16, orderHeader.getIbillstatus().intValue());
      }
      if (orderHeader.getVmemo() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, orderHeader.getVmemo());
      }
      if (orderHeader.getCaccountyear() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, orderHeader.getCaccountyear());
      }
      if (orderHeader.getCoperator() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, orderHeader.getCoperator());
      }
      if (orderHeader.getVdef1() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, orderHeader.getVdef1());
      }
      if (orderHeader.getVdef2() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, orderHeader.getVdef2());
      }
      if (orderHeader.getVdef3() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, orderHeader.getVdef3());
      }
      if (orderHeader.getVdef4() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, orderHeader.getVdef4());
      }
      if (orderHeader.getVdef5() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, orderHeader.getVdef5());
      }
      if (orderHeader.getVdef6() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, orderHeader.getVdef6());
      }
      if (orderHeader.getVdef7() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, orderHeader.getVdef7());
      }
      if (orderHeader.getVdef8() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, orderHeader.getVdef8());
      }
      if (orderHeader.getVdef9() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, orderHeader.getVdef9());
      }
      if (orderHeader.getVdef10() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, orderHeader.getVdef10());
      }

      if (orderHeader.getVdef11() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, orderHeader.getVdef11());
      }
      if (orderHeader.getVdef12() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, orderHeader.getVdef12());
      }
      if (orderHeader.getVdef13() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, orderHeader.getVdef13());
      }
      if (orderHeader.getVdef14() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, orderHeader.getVdef14());
      }
      if (orderHeader.getVdef15() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, orderHeader.getVdef15());
      }
      if (orderHeader.getVdef16() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, orderHeader.getVdef16());
      }
      if (orderHeader.getVdef17() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, orderHeader.getVdef17());
      }
      if (orderHeader.getVdef18() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, orderHeader.getVdef18());
      }
      if (orderHeader.getVdef19() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, orderHeader.getVdef19());
      }
      if (orderHeader.getVdef20() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, orderHeader.getVdef20());
      }
      if (orderHeader.getPKDefDoc1() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, orderHeader.getPKDefDoc1());
      }
      if (orderHeader.getPKDefDoc2() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, orderHeader.getPKDefDoc2());
      }
      if (orderHeader.getPKDefDoc3() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, orderHeader.getPKDefDoc3());
      }
      if (orderHeader.getPKDefDoc4() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, orderHeader.getPKDefDoc4());
      }
      if (orderHeader.getPKDefDoc5() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, orderHeader.getPKDefDoc5());
      }
      if (orderHeader.getPKDefDoc6() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, orderHeader.getPKDefDoc6());
      }
      if (orderHeader.getPKDefDoc7() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, orderHeader.getPKDefDoc7());
      }
      if (orderHeader.getPKDefDoc8() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, orderHeader.getPKDefDoc8());
      }
      if (orderHeader.getPKDefDoc9() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, orderHeader.getPKDefDoc9());
      }
      if (orderHeader.getPKDefDoc10() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, orderHeader.getPKDefDoc10());
      }
      if (orderHeader.getPKDefDoc11() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, orderHeader.getPKDefDoc11());
      }
      if (orderHeader.getPKDefDoc12() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, orderHeader.getPKDefDoc12());
      }
      if (orderHeader.getPKDefDoc13() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, orderHeader.getPKDefDoc13());
      }
      if (orderHeader.getPKDefDoc14() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, orderHeader.getPKDefDoc14());
      }
      if (orderHeader.getPKDefDoc15() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, orderHeader.getPKDefDoc15());
      }
      if (orderHeader.getPKDefDoc16() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, orderHeader.getPKDefDoc16());
      }
      if (orderHeader.getPKDefDoc17() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, orderHeader.getPKDefDoc17());
      }
      if (orderHeader.getPKDefDoc18() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, orderHeader.getPKDefDoc18());
      }
      if (orderHeader.getPKDefDoc19() == null)
        stmt.setNull(58, 1);
      else {
        stmt.setString(58, orderHeader.getPKDefDoc19());
      }
      if (orderHeader.getPKDefDoc20() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, orderHeader.getPKDefDoc20());
      }

      if (orderHeader.getCvendormangid() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, orderHeader.getCvendormangid());
      }
      if (orderHeader.getTmaketime() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, orderHeader.getTmaketime());
      }
      if (orderHeader.getTaudittime() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, orderHeader.getTaudittime());
      }
      if (orderHeader.getTlastmaketime() == null)
        stmt.setNull(63, 1);
      else {
        stmt.setString(63, orderHeader.getTlastmaketime());
      }

      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "insertHeader", new Object[] { orderHeader });

    return key;
  }

  public String insertItem(OrderItemVO orderItem)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "insertItem", new Object[] { orderItem });

    String sql = "insert into sc_order_b(corder_bid, corderid, pk_corp, cmangid, cbaseid, nordernum, cassistunit, nassistnum," +
    		" ndiscountrate, idiscounttaxtype, ntaxrate, ccurrencytypeid, noriginalnetprice, noriginalcurprice, noriginalcurmny," +
    		" noriginaltaxmny, noriginalsummny, nexchangeotobrate, ntaxmny, nmoney, nsummny, nexchangeotoarate, nassistcurmny," +
    		" nassisttaxmny, nassistsummny, naccumarrvnum, naccumstorenum, naccuminvoicenum, naccumwastnum, dplanarrvdate, cwarehouseid," +
    		" creceiveaddress, cprojectid, cprojectphaseid, coperator, forderrowstatus, bisactive, cordersource, csourcebillid," +
    		" csourcebillrow, cupsourcebilltype, cupsourcebillid, cupsourcebillrowid, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5," +
    		" vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16," +
    		" vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7," +
    		" pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16," +
    		" pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20, crowno, norgtaxprice, norgnettaxprice, vproducenum,ccontractid," +
    		"ccontractrowid,ccontractrcode,vpriceauditcode,cpriceauditid,cpriceaudit_bid,cpriceaudit_bb1id,bomvers ) " +//shikun bomvers
    		"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
    		" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
    		" ?, ?, ?, ?, ?, ?, ? , ?, ?, ? , ?, ?, ?, ? , ?, ?,? )";//shikun bomvers

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    String pk_corp = orderItem.getPk_corp();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      key = getOID(pk_corp);
      stmt.setString(1, key);

      if (orderItem.getCorderid() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, orderItem.getCorderid());
      }
      if (orderItem.getPk_corp() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, orderItem.getPk_corp());
      }
      if (orderItem.getCmangid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, orderItem.getCmangid());
      }
      if (orderItem.getCbaseid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, orderItem.getCbaseid());
      }
      if (orderItem.getNordernum() == null)
      {
        stmt.setBigDecimal(6, new UFDouble(0).toBigDecimal());
      }
      else stmt.setBigDecimal(6, orderItem.getNordernum().toBigDecimal());

      if (orderItem.getCassistunit() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, orderItem.getCassistunit());
      }
      if (orderItem.getNassistnum() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, orderItem.getNassistnum().toBigDecimal());
      }
      if (orderItem.getNdiscountrate() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setBigDecimal(9, orderItem.getNdiscountrate().toBigDecimal());
      }
      if (orderItem.getIdiscounttaxtype() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setInt(10, orderItem.getIdiscounttaxtype().intValue());
      }
      if (orderItem.getNtaxrate() == null)
        stmt.setNull(11, 4);
      else {
        stmt.setBigDecimal(11, orderItem.getNtaxrate().toBigDecimal());
      }
      if (orderItem.getCcurrencytypeid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, orderItem.getCcurrencytypeid());
      }
      if (orderItem.getNoriginalnetprice() == null)
        stmt.setNull(13, 4);
      else {
        stmt.setBigDecimal(13, orderItem.getNoriginalnetprice().toBigDecimal());
      }
      if (orderItem.getNoriginalcurprice() == null)
        stmt.setNull(14, 4);
      else {
        stmt.setBigDecimal(14, orderItem.getNoriginalcurprice().toBigDecimal());
      }
      if (orderItem.getNoriginalcurmny() == null)
        stmt.setNull(15, 4);
      else {
        stmt.setBigDecimal(15, orderItem.getNoriginalcurmny().toBigDecimal());
      }
      if (orderItem.getNoriginaltaxmny() == null)
        stmt.setNull(16, 4);
      else {
        stmt.setBigDecimal(16, orderItem.getNoriginaltaxmny().toBigDecimal());
      }
      if (orderItem.getNoriginalsummny() == null)
        stmt.setNull(17, 4);
      else {
        stmt.setBigDecimal(17, orderItem.getNoriginalsummny().toBigDecimal());
      }
      if (orderItem.getNexchangeotobrate() == null)
        stmt.setNull(18, 4);
      else {
        stmt.setBigDecimal(18, orderItem.getNexchangeotobrate().toBigDecimal());
      }
      if (orderItem.getNtaxmny() == null)
        stmt.setNull(19, 4);
      else {
        stmt.setBigDecimal(19, orderItem.getNtaxmny().toBigDecimal());
      }
      if (orderItem.getNmoney() == null)
      {
        stmt.setBigDecimal(20, new UFDouble(0).toBigDecimal());
      }
      else stmt.setBigDecimal(20, orderItem.getNmoney().toBigDecimal());

      if (orderItem.getNsummny() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, orderItem.getNsummny().toBigDecimal());
      }
      if (orderItem.getNexchangeotoarate() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, orderItem.getNexchangeotoarate().toBigDecimal());
      }
      if (orderItem.getNassistcurmny() == null)
        stmt.setNull(23, 4);
      else {
        stmt.setBigDecimal(23, orderItem.getNassistcurmny().toBigDecimal());
      }
      if (orderItem.getNassisttaxmny() == null)
        stmt.setNull(24, 4);
      else {
        stmt.setBigDecimal(24, orderItem.getNassisttaxmny().toBigDecimal());
      }
      if (orderItem.getNassistsummny() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setBigDecimal(25, orderItem.getNassistsummny().toBigDecimal());
      }
      if (orderItem.getNaccumarrvnum() == null)
      {
        stmt.setBigDecimal(26, new UFDouble(0).toBigDecimal());
      }
      else stmt.setBigDecimal(26, orderItem.getNaccumarrvnum().toBigDecimal());

      if (orderItem.getNaccumstorenum() == null)
      {
        stmt.setBigDecimal(27, new UFDouble(0).toBigDecimal());
      }
      else stmt.setBigDecimal(27, orderItem.getNaccumstorenum().toBigDecimal());

      if (orderItem.getNaccuminvoicenum() == null)
        stmt.setNull(28, 4);
      else {
        stmt.setBigDecimal(28, orderItem.getNaccuminvoicenum().toBigDecimal());
      }
      if (orderItem.getNaccumwastnum() == null)
        stmt.setNull(29, 4);
      else {
        stmt.setBigDecimal(29, orderItem.getNaccumwastnum().toBigDecimal());
      }
      if (orderItem.getDplanarrvdate() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, orderItem.getDplanarrvdate().toString());
      }
      if (orderItem.getCwarehouseid() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, orderItem.getCwarehouseid());
      }
      if (orderItem.getCreceiveaddress() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, orderItem.getCreceiveaddress());
      }
      if (orderItem.getCprojectid() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, orderItem.getCprojectid());
      }
      if (orderItem.getCprojectphaseid() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, orderItem.getCprojectphaseid());
      }
      if (orderItem.getCoperator() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, orderItem.getCoperator());
      }
      if (orderItem.getForderrowstatus() == null)
      {
        stmt.setInt(36, 0);
      }
      else stmt.setInt(36, orderItem.getForderrowstatus().intValue());

      if (orderItem.getBisactive() == null)
        stmt.setString(37, "0");
      else {
        stmt.setString(37, orderItem.getBisactive().booleanValue() ? "0" : "1");
      }

      if (orderItem.getCordersource() == null) {
        stmt.setNull(38, 1);
      }
      else {
        stmt.setString(38, orderItem.getCordersource());
      }
      if (orderItem.getCsourcebillid() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, orderItem.getCsourcebillid());
      }
      if (orderItem.getCsourcebillrow() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, orderItem.getCsourcebillrow());
      }

      if (orderItem.getCupsourcebilltype() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, orderItem.getCupsourcebilltype());
      }
      if (orderItem.getCupsourcebillid() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, orderItem.getCupsourcebillid());
      }
      if (orderItem.getCupsourcebillrowid() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, orderItem.getCupsourcebillrowid());
      }

      if (orderItem.getVmemo() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, orderItem.getVmemo());
      }
      if (orderItem.getVfree1() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, orderItem.getVfree1());
      }
      if (orderItem.getVfree2() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, orderItem.getVfree2());
      }
      if (orderItem.getVfree3() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, orderItem.getVfree3());
      }
      if (orderItem.getVfree4() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, orderItem.getVfree4());
      }
      if (orderItem.getVfree5() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, orderItem.getVfree5());
      }
      if (orderItem.getVdef1() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, orderItem.getVdef1());
      }
      if (orderItem.getVdef2() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, orderItem.getVdef2());
      }
      if (orderItem.getVdef3() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, orderItem.getVdef3());
      }
      if (orderItem.getVdef4() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, orderItem.getVdef4());
      }
      if (orderItem.getVdef5() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, orderItem.getVdef5());
      }
      if (orderItem.getVdef6() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, orderItem.getVdef6());
      }
      if (orderItem.getVdef7() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, orderItem.getVdef7());
      }
      if (orderItem.getVdef8() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, orderItem.getVdef8());
      }
      if (orderItem.getVdef9() == null)
        stmt.setNull(58, 1);
      else {
        stmt.setString(58, orderItem.getVdef9());
      }
      if (orderItem.getVdef10() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, orderItem.getVdef10());
      }

      if (orderItem.getVdef11() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, orderItem.getVdef11());
      }
      if (orderItem.getVdef12() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, orderItem.getVdef12());
      }
      if (orderItem.getVdef13() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, orderItem.getVdef13());
      }
      if (orderItem.getVdef14() == null)
        stmt.setNull(63, 1);
      else {
        stmt.setString(63, orderItem.getVdef14());
      }
      if (orderItem.getVdef15() == null)
        stmt.setNull(64, 1);
      else {
        stmt.setString(64, orderItem.getVdef15());
      }
      if (orderItem.getVdef16() == null)
        stmt.setNull(65, 1);
      else {
        stmt.setString(65, orderItem.getVdef16());
      }
      if (orderItem.getVdef17() == null)
        stmt.setNull(66, 1);
      else {
        stmt.setString(66, orderItem.getVdef17());
      }
      if (orderItem.getVdef18() == null)
        stmt.setNull(67, 1);
      else {
        stmt.setString(67, orderItem.getVdef18());
      }
      if (orderItem.getVdef19() == null)
        stmt.setNull(68, 1);
      else {
        stmt.setString(68, orderItem.getVdef19());
      }
      if (orderItem.getVdef20() == null)
        stmt.setNull(69, 1);
      else {
        stmt.setString(69, orderItem.getVdef20());
      }
      if (orderItem.getPKDefDoc1() == null)
        stmt.setNull(70, 1);
      else {
        stmt.setString(70, orderItem.getPKDefDoc1());
      }
      if (orderItem.getPKDefDoc2() == null)
        stmt.setNull(71, 1);
      else {
        stmt.setString(71, orderItem.getPKDefDoc2());
      }
      if (orderItem.getPKDefDoc3() == null)
        stmt.setNull(72, 1);
      else {
        stmt.setString(72, orderItem.getPKDefDoc3());
      }
      if (orderItem.getPKDefDoc4() == null)
        stmt.setNull(73, 1);
      else {
        stmt.setString(73, orderItem.getPKDefDoc4());
      }
      if (orderItem.getPKDefDoc5() == null)
        stmt.setNull(74, 1);
      else {
        stmt.setString(74, orderItem.getPKDefDoc5());
      }
      if (orderItem.getPKDefDoc6() == null)
        stmt.setNull(75, 1);
      else {
        stmt.setString(75, orderItem.getPKDefDoc6());
      }
      if (orderItem.getPKDefDoc7() == null)
        stmt.setNull(76, 1);
      else {
        stmt.setString(76, orderItem.getPKDefDoc7());
      }
      if (orderItem.getPKDefDoc8() == null)
        stmt.setNull(77, 1);
      else {
        stmt.setString(77, orderItem.getPKDefDoc8());
      }
      if (orderItem.getPKDefDoc9() == null)
        stmt.setNull(78, 1);
      else {
        stmt.setString(78, orderItem.getPKDefDoc9());
      }
      if (orderItem.getPKDefDoc10() == null)
        stmt.setNull(79, 1);
      else {
        stmt.setString(79, orderItem.getPKDefDoc10());
      }
      if (orderItem.getPKDefDoc11() == null)
        stmt.setNull(80, 1);
      else {
        stmt.setString(80, orderItem.getPKDefDoc11());
      }
      if (orderItem.getPKDefDoc12() == null)
        stmt.setNull(81, 1);
      else {
        stmt.setString(81, orderItem.getPKDefDoc12());
      }
      if (orderItem.getPKDefDoc13() == null)
        stmt.setNull(82, 1);
      else {
        stmt.setString(82, orderItem.getPKDefDoc13());
      }
      if (orderItem.getPKDefDoc14() == null)
        stmt.setNull(83, 1);
      else {
        stmt.setString(83, orderItem.getPKDefDoc14());
      }
      if (orderItem.getPKDefDoc15() == null)
        stmt.setNull(84, 1);
      else {
        stmt.setString(84, orderItem.getPKDefDoc15());
      }
      if (orderItem.getPKDefDoc16() == null)
        stmt.setNull(85, 1);
      else {
        stmt.setString(85, orderItem.getPKDefDoc16());
      }
      if (orderItem.getPKDefDoc17() == null)
        stmt.setNull(86, 1);
      else {
        stmt.setString(86, orderItem.getPKDefDoc17());
      }
      if (orderItem.getPKDefDoc18() == null)
        stmt.setNull(87, 1);
      else {
        stmt.setString(87, orderItem.getPKDefDoc18());
      }
      if (orderItem.getPKDefDoc19() == null)
        stmt.setNull(88, 1);
      else {
        stmt.setString(88, orderItem.getPKDefDoc19());
      }
      if (orderItem.getPKDefDoc20() == null)
        stmt.setNull(89, 1);
      else {
        stmt.setString(89, orderItem.getPKDefDoc20());
      }
      if (orderItem.getCrowno() == null)
        stmt.setNull(90, 1);
      else {
        stmt.setString(90, orderItem.getCrowno());
      }
      if (orderItem.getNorgtaxprice() == null)
        stmt.setNull(91, 4);
      else {
        stmt.setBigDecimal(91, orderItem.getNorgtaxprice().toBigDecimal());
      }
      if (orderItem.getNorgnettaxprice() == null)
        stmt.setNull(92, 4);
      else {
        stmt.setBigDecimal(92, orderItem.getNorgnettaxprice().toBigDecimal());
      }

      if (orderItem.getVproducenum() == null)
        stmt.setNull(93, 1);
      else {
        stmt.setString(93, orderItem.getVproducenum());
      }

      if (orderItem.getCcontractid() == null)
        stmt.setNull(94, 1);
      else {
        stmt.setString(94, orderItem.getCcontractid());
      }

      if (orderItem.getCcontractrowid() == null)
        stmt.setNull(95, 1);
      else {
        stmt.setString(95, orderItem.getCcontractrowid());
      }

      if (orderItem.getCcontractrcode() == null)
        stmt.setNull(96, 1);
      else {
        stmt.setString(96, orderItem.getCcontractrcode());
      }

      if (orderItem.getVpriceauditcode() == null)
        stmt.setNull(97, 1);
      else {
        stmt.setString(97, orderItem.getVpriceauditcode());
      }

      if (orderItem.getCpriceauditid() == null)
        stmt.setNull(98, 1);
      else {
        stmt.setString(98, orderItem.getCpriceauditid());
      }

      if (orderItem.getCpriceaudit_bid() == null)
        stmt.setNull(99, 1);
      else {
        stmt.setString(99, orderItem.getCpriceaudit_bid());
      }

      if (orderItem.getCpriceaudit_bb1id() == null)
        stmt.setNull(100, 1);
      else {
        stmt.setString(100, orderItem.getCpriceaudit_bb1id());
      }

      if (orderItem.getBomvers() == null)//shikun bomvers
        stmt.setNull(101, 1);
      else {
        stmt.setString(101, orderItem.getBomvers());
      }

      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "insertItem", new Object[] { orderItem });

    return key;
  }

  public String insertItem(OrderItemVO orderItem, String foreignKey)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "insertItem", new Object[] { orderItem, foreignKey });

    orderItem.setCorderid(foreignKey);
    String key = insertItem(orderItem);

    afterCallMethod("nc.bs.sc.order.OrderDMO", "insertItem", new Object[] { orderItem, foreignKey });

    return key;
  }

  public void delete(OrderVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "delete", new Object[] { vo });

    deleteItemsForHeader(((OrderHeaderVO)vo.getParentVO()).getPrimaryKey());
    deleteHeader((OrderHeaderVO)vo.getParentVO());

    afterCallMethod("nc.bs.sc.order.OrderDMO", "delete", new Object[] { vo });
  }

  public void saveOutterBill(OrderVO[] VOs, ClientLink clientlink)
    throws BusinessException
  {
    try
    {
      SCATP atp = new SCATP();
      String key = null;
      String sTime = new UFDateTime(new Date()).toString();

      for (int i = 0; i < VOs.length; i++)
      {
        PublicDMO dmo = new PublicDMO();
        dmo.getBillCode(VOs[i]);

        OrderHeaderVO headVO = (OrderHeaderVO)VOs[i].getParentVO();
        headVO.setStatus(2);
        headVO.setDorderdate(clientlink.getLogonDate());
        headVO.setCoperator(clientlink.getUser());
        headVO.setTmaketime(sTime);
        headVO.setTlastmaketime(sTime);
        OrderItemVO[] bodyVO = (OrderItemVO[])(OrderItemVO[])VOs[i].getChildrenVO();
        if ((bodyVO != null) && (bodyVO.length > 0))
        {
          String upbill_type = bodyVO[0].getCupsourcebilltype();
          int lint_Praysource = 3;
          if ((upbill_type != null) && (upbill_type.trim().length() > 0))
          {
            IPrayvsBusiQry bo = (IPrayvsBusiQry)NCLocator.getInstance().lookup(IPrayvsBusiQry.class.getName());

            PrayvsbusiVO l_condPrayvsbusiVO = new PrayvsbusiVO();
            l_condPrayvsbusiVO.setPk_corp(headVO.getPk_corp());
            if ((upbill_type.trim().equals("5C")) || (upbill_type.trim().equals("5D")) || (upbill_type.trim().equals("5E")) || (upbill_type.trim().equals("5I")))
            {
              l_condPrayvsbusiVO.setPraysource(new Integer(7));
              l_condPrayvsbusiVO.setPraytype(new Integer(0));
              lint_Praysource = 7;
            }

            if (upbill_type.trim().equals("30"))
            {
              l_condPrayvsbusiVO.setPraysource(new Integer(3));
              l_condPrayvsbusiVO.setPraytype(new Integer(0));
              lint_Praysource = 3;
            }

            PrayvsbusiVO[] l_resultPrayvsbusiVO = bo.queryByPrayvsBusitypeVO(l_condPrayvsbusiVO, new Boolean(true));
            if ((l_resultPrayvsbusiVO != null) && (l_resultPrayvsbusiVO.length > 0)) {
              Vector v = new Vector();
              for (int j = 0; j < l_resultPrayvsbusiVO.length; j++) {
                if ((l_resultPrayvsbusiVO[j].getPk_corp() != null) && (l_resultPrayvsbusiVO[j].getPk_corp().equals(headVO.getPk_corp())) && (l_resultPrayvsbusiVO[j].getPraysource() != null) && (l_resultPrayvsbusiVO[j].getPraysource().intValue() == lint_Praysource) && (l_resultPrayvsbusiVO[j].getPraytype() != null) && (l_resultPrayvsbusiVO[j].getPraytype().intValue() == 0))
                  v.addElement(l_resultPrayvsbusiVO[j]);
              }
              if (v.size() > 0) {
                l_resultPrayvsbusiVO = new PrayvsbusiVO[v.size()];
                v.copyInto(l_resultPrayvsbusiVO);
                headVO.setCbiztype(l_resultPrayvsbusiVO[0].getPk_busitype());
              } else {
                throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000077"));
              }
            }
            else {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000077"));
            }

            if ((headVO.getCbiztype() == null) || (headVO.getCbiztype().length() == 0)) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000077"));
            }
          }

        }

        headVO.setDauditdate(clientlink.getLogonDate());
        key = insertHeader(headVO);

        headVO.setPrimaryKey(key);
        if ((bodyVO != null) && (bodyVO.length > 0))
        {
          for (int j = 0; j < bodyVO.length; j++) {
            bodyVO[j].setCorderid(key);
            bodyVO[j].setStatus(2);
            bodyVO[j].setBisactive(new UFBoolean(true));
            bodyVO[j].setCrowno(new Integer(j + 1).toString());
            bodyVO[j].setIdiscounttaxtype(new Integer(2));
            bodyVO[j].setNdiscountrate(new UFDouble(100));

            bodyVO[j].setNoriginalcurmny(null);
            bodyVO[j].setNoriginalsummny(null);
            bodyVO[j].setNoriginaltaxmny(null);
            bodyVO[j].setNmoney(null);
            bodyVO[j].setNsummny(null);
            bodyVO[j].setNtaxmny(null);
            bodyVO[j].setNassistcurmny(null);
            bodyVO[j].setNassistsummny(null);
            bodyVO[j].setNassisttaxmny(null);
            bodyVO[j].setNoriginalnetprice(null);
            bodyVO[j].setNorgnettaxprice(null);
            bodyVO[j].setNoriginalcurprice(null);
            bodyVO[j].setNorgtaxprice(null);
          }

          VOs[i].setClientLink(clientlink);
          setRelateCntAndDefaultPrice(VOs[i]);

          insertItems(bodyVO);
        }

        VOs[i].setBillAction("推式保存");
        atp.modifyATP(VOs[i]);

        OrderImpl l_OrderBO = new OrderImpl();
        l_OrderBO.updateXSDDNum(VOs[i]);
        l_OrderBO.updateDBDDNum(VOs[i]);
        l_OrderBO.updateCTNum(VOs[i], null);

        atp.checkAtpInstantly(VOs[i], null);
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }
  }

  private OrderVO setRelateCntAndDefaultPrice(OrderVO a_OrderVO)
    throws Exception
  {
    if (a_OrderVO == null) {
      return null;
    }
    OrderHeaderVO headVO = (OrderHeaderVO)a_OrderVO.getParentVO();
    OrderItemVO[] bodyVO = (OrderItemVO[])(OrderItemVO[])a_OrderVO.getChildrenVO();

    Vector lVec_rows = new Vector();
    for (int i = 0; i < bodyVO.length; i++) {
      if ((bodyVO[i].getCmangid() != null) && (bodyVO[i].getCmangid().toString().length() > 0)) {
        lVec_rows.addElement(new Integer(i));
      }
    }

    int[] lintary_rows = null;
    if (lVec_rows.size() > 0) {
      lintary_rows = new int[lVec_rows.size()];
      for (int i = 0; i < lVec_rows.size(); i++)
        lintary_rows[i] = ((Integer)lVec_rows.elementAt(i)).intValue();
    }
    else {
      return a_OrderVO;
    }

    int li_rowcount = lintary_rows.length;
    if (li_rowcount == 0) {
      return a_OrderVO;
    }

    String lStr_vendorbasid = headVO.getCvendorid();
    lStr_vendorbasid = SCPubVO.getString_TrimZeroLenAsNull(lStr_vendorbasid);

    String lStr_vendormangid = headVO.getCvendormangid();
    lStr_vendormangid = SCPubVO.getString_TrimZeroLenAsNull(lStr_vendormangid);

    String lStr_orderdate = headVO.getDorderdate().toString();
    lStr_orderdate = SCPubVO.getString_TrimZeroLenAsNull(lStr_orderdate);

    String lStr_wareid = headVO.getCwareid();
    lStr_wareid = SCPubVO.getString_TrimZeroLenAsNull(lStr_wareid);

    String[] lStrary_baseid = new String[li_rowcount];
    for (int i = 0; i < li_rowcount; i++) {
      int lint_rowindex = lintary_rows[i];
      lStrary_baseid[i] = SCPubVO.getString_TrimZeroLenAsNull(bodyVO[lint_rowindex].getCbaseid());
    }

    String[] lStrary_vendorbaseid = null;

    if ((lStr_vendorbasid != null) && (lStr_vendorbasid.trim().length() > 0)) {
      lStrary_vendorbaseid = (String[])(String[])SCPubVO.getSameValueArray(lStr_vendorbasid, li_rowcount);
    }

    String lStr_PKCorp = headVO.getPk_corp();

    int lint_PricePolicy = 6;
    SysInitBO l_SysInitBO = new SysInitBO();
    SysInitVO[] lAry_initVO = l_SysInitBO.querySysInit(lStr_PKCorp, "SC04");

    if ((lAry_initVO != null) && (lAry_initVO.length > 0)) {
      String s = lAry_initVO[0].getValue();
      if ((s != null) && (s.trim().length() > 0)) {
        if (s.endsWith("无税价格优先")) {
          lint_PricePolicy = 6;
        }
        else {
          lint_PricePolicy = 5;
        }
      }
    }

    String lStr_ChangedKey = OrderItemVO.getPriceFieldByPricePolicy(lint_PricePolicy);

    boolean[] lbolary_findCtPrice = new boolean[li_rowcount];
    for (int i = 0; i < li_rowcount; i++) {
      lbolary_findCtPrice[i] = false;
    }

    boolean lbol_CTStartUp = new CreatecorpDMO().isEnabled(lStr_PKCorp, "CT");

    if ((lbol_CTStartUp) && (lStrary_baseid != null) && (lStrary_baseid.length > 0) && (lStrary_vendorbaseid != null) && (lStrary_vendorbaseid.length > 0) && (lStr_orderdate != null) && (lStr_orderdate.trim().length() > 0))
    {
      RetCtToPoQueryVO[] lary_CtRetVO = new ScFromCtImpl().queryForCnt(lStr_PKCorp, lStrary_baseid, lStrary_vendorbaseid, new UFDate(lStr_orderdate));

      if ((lary_CtRetVO != null) && (lary_CtRetVO.length > 0) && (lary_CtRetVO.length == li_rowcount))
      {
        for (int i = 0; i < li_rowcount; i++)
        {
          int lint_rowindex = lintary_rows[i];

          bodyVO[lint_rowindex].setCcontractid(null);
          bodyVO[lint_rowindex].setCcontractrowid(null);
          bodyVO[lint_rowindex].setCcontractrcode(null);

          if (lary_CtRetVO[i] != null)
          {
            UFDouble lUFD_Price = null;
            lUFD_Price = SCPubVO.getPriceValueByPricePolicy(lary_CtRetVO[i], lint_PricePolicy);

            if (lUFD_Price != null)
            {
              UFDouble lUFD_OldPrice = SCPubVO.getUFDouble_ValueAsValue(bodyVO[lint_rowindex].getAttributeValue(lStr_ChangedKey));

              lbolary_findCtPrice[i] = true;

              bodyVO[lint_rowindex].setCcontractid(lary_CtRetVO[i].getCContractID());
              bodyVO[lint_rowindex].setCcontractrowid(lary_CtRetVO[i].getCContractRowId());
              bodyVO[lint_rowindex].setCcontractrcode(lary_CtRetVO[i].getCContractCode());

              if ((lUFD_OldPrice == null) || (lUFD_Price.compareTo(lUFD_OldPrice) != 0)) {
                bodyVO[lint_rowindex].setAttributeValue(lStr_ChangedKey, lUFD_Price);

                SCPubVO.calRelation(bodyVO[lint_rowindex], lint_PricePolicy);
              }
            }
            else {
              lbolary_findCtPrice[i] = false;
            }
          } else {
            lbolary_findCtPrice[i] = false;
          }
        }
      }

    }

    Vector lVec_needDefault = new Vector();

    for (int i = 0; i < lbolary_findCtPrice.length; i++) {
      if (lbolary_findCtPrice[i]) {
        lVec_needDefault.addElement(new Integer(lintary_rows[i]));
      }
    }

    if (lVec_needDefault.size() > 0) {
      int li_rowcount2 = lVec_needDefault.size();
      int[] lintary_needDefault = new int[li_rowcount2];
      for (int i = 0; i < li_rowcount2; i++) {
        lintary_needDefault[i] = ((Integer)lVec_needDefault.elementAt(i)).intValue();
      }

      String[] lStrary_mangid2 = new String[li_rowcount2];
      String[] lStrary_currencytypeid2 = new String[li_rowcount2];
      UFDouble[] lUFDary_BRate2 = new UFDouble[li_rowcount2];
      UFDouble[] lUFDary_ARate2 = new UFDouble[li_rowcount2];

      for (int i = 0; i < li_rowcount2; i++) {
        int lint_rowindex = lintary_needDefault[i];
        lStrary_mangid2[i] = SCPubVO.getString_TrimZeroLenAsNull(bodyVO[lint_rowindex].getCmangid());
        lStrary_currencytypeid2[i] = SCPubVO.getString_TrimZeroLenAsNull(bodyVO[lint_rowindex].getCcurrencytypeid());
        lUFDary_BRate2[i] = SCPubVO.getUFDouble_ValueAsValue(bodyVO[lint_rowindex].getNexchangeotobrate());
        lUFDary_ARate2[i] = SCPubVO.getUFDouble_ValueAsValue(bodyVO[lint_rowindex].getNexchangeotoarate());
      }

      RetScVrmAndParaPriceVO l_voPara = new RetScVrmAndParaPriceVO(1);
      l_voPara.setPk_corp(lStr_PKCorp);
      l_voPara.setStoOrgId(lStr_wareid);
      l_voPara.setVendMangId(lStr_vendormangid);
      l_voPara.setSaInvMangId(lStrary_mangid2);
      l_voPara.setSaCurrId(lStrary_currencytypeid2);
      l_voPara.setDaBRate(lUFDary_BRate2);
      l_voPara.setDaARate(lUFDary_ARate2);
      l_voPara.setDOrderDate(lStr_orderdate == null ? null : new UFDate(lStr_orderdate));
      l_voPara.setClientLink(a_OrderVO.getClientLink());

      RetScVrmAndParaPriceVO l_voRetPrice = new OrderImpl().queryVrmAndParaPrices(l_voPara);

      for (int i = 0; i < li_rowcount2; i++) {
        int lint_rowindex = lintary_needDefault[i];
        UFDouble lUFD_Price = l_voRetPrice.getPriceAt(i);
        if (lUFD_Price == null)
        {
          continue;
        }
        if (l_voRetPrice.isSetPriceNoTaxAt(i)) {
          lStr_ChangedKey = "noriginalcurprice";
        }

        UFDouble lUFD_OldPrice = SCPubVO.getUFDouble_ValueAsValue(bodyVO[lint_rowindex].getAttributeValue(lStr_ChangedKey));

        if ((lUFD_OldPrice == null) || (lUFD_Price.compareTo(lUFD_OldPrice) != 0)) {
          bodyVO[lint_rowindex].setAttributeValue(lStr_ChangedKey, lUFD_Price);

          SCPubVO.calRelation(bodyVO[lint_rowindex], lint_PricePolicy);
        }
      }

    }

    a_OrderVO.setChildrenVO(bodyVO);
    return a_OrderVO;
  }

  public boolean isReferedOtherBills(String[] astrary_billids)
    throws RemoteException
  {
    beforeCallMethod("nc.bs.sc.query.OrderDMO", "isReferedOtherBills", new Object[] { astrary_billids });

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    String strSql = "select cupsourcebillid from sc_order_b where dr = '0' ";
    try
    {
      TempTableDMO tmpTblDmo = new TempTableDMO();

      int iLen = astrary_billids.length;
      ArrayList listIds = new ArrayList();
      for (int i = 0; i < iLen; i++) {
        if (!listIds.contains(astrary_billids[i])) {
          listIds.add(astrary_billids[i]);
        }
      }
      String strIdSet = tmpTblDmo.insertTempTable(listIds, "t_sc_close_1", "pk_pu");
      if ((strIdSet == null) || (strIdSet.trim().length() == 0)) {
        throw new SQLException("创建临时表时未返回主键串集合或子查询串，直接返加NULL!");
      }
      strSql = strSql + "and cupsourcebillid in " + strIdSet;

      con = getConnection();
      stmt = con.prepareStatement(strSql);

      rs = stmt.executeQuery();
      if (rs.next())
      {
        return true;
      }
      return false;
    }
    catch (Exception e)
    {
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.sc.query.OrderDMO", "isReferedOtherBills", new Object[] { astrary_billids });

    return true;
  }

  public UFBoolean[] queryIfExecPray(String[] aStrAry_PrayBillRowID)
    throws RemoteException
  {
    beforeCallMethod("nc.bs.sc.query.OrderDMO", "queryIfExecPray", new Object[] { aStrAry_PrayBillRowID });

    if (aStrAry_PrayBillRowID == null) {
      return null;
    }

    if (aStrAry_PrayBillRowID.length == 0) {
      return null;
    }

    UFBoolean[] lUFBAry_Return = new UFBoolean[aStrAry_PrayBillRowID.length];
    Vector lVec_Temp = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    String strSql = "select B.cupsourcebillrowid from sc_order A,sc_order_b B where A.corderid = B.corderid and A.dr = 0 ";
    try
    {
      TempTableDMO tmpTblDmo = new TempTableDMO();

      int iLen = aStrAry_PrayBillRowID.length;
      ArrayList listIds = new ArrayList();
      for (int i = 0; i < iLen; i++) {
        if (!listIds.contains(aStrAry_PrayBillRowID[i])) {
          listIds.add(aStrAry_PrayBillRowID[i]);
        }
      }
      String strIdSet = tmpTblDmo.insertTempTable(listIds, "t_sc_close_1", "pk_pu");
      if ((strIdSet == null) || (strIdSet.trim().length() == 0)) {
        throw new SQLException("创建临时表时未返回主键串集合或子查询串，直接返加NULL!");
      }
      strSql = strSql + "and B.cupsourcebillrowid in " + strIdSet;

      con = getConnection();
      stmt = con.prepareStatement(strSql);

      rs = stmt.executeQuery();

      while (rs.next()) {
        String s = rs.getString(1);
        if ((s != null) && (s.trim().length() > 0)) {
          lVec_Temp.addElement(s);
        }
      }

      if (rs != null) {
        rs.close();
      }
      for (int i = 0; i < aStrAry_PrayBillRowID.length; i++)
        if (lVec_Temp.contains(aStrAry_PrayBillRowID[i]))
          lUFBAry_Return[i] = new UFBoolean(true);
        else
          lUFBAry_Return[i] = new UFBoolean(false);
    }
    catch (Exception e)
    {
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
    afterCallMethod("nc.bs.sc.query.OrderDMO", "queryIfExecPray", new Object[] { aStrAry_PrayBillRowID });

    return lUFBAry_Return;
  }

  public void deleteHeader(OrderHeaderVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "deleteHeader", new Object[] { vo });

    String sql = "update  sc_order set dr=1 where corderid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, vo.getPrimaryKey());
      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "deleteHeader", new Object[] { vo });
  }

  public void deleteItem(OrderItemVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "deleteItem", new Object[] { vo });

    String sql = "update sc_order_b set dr=1 where corder_bid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, vo.getPrimaryKey());
      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "deleteItem", new Object[] { vo });
  }

  public void deleteItemsForHeader(String headerKey)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "deleteItemsForHeader", new Object[] { headerKey });

    String sql = "update sc_order_b set dr=1 where corderid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, headerKey);
      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "deleteItemsForHeader", new Object[] { headerKey });
  }

  public void updateHeader(OrderHeaderVO orderHeader)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "updateHeader", new Object[] { orderHeader });

    String sql = "update sc_order set vordercode = ?, pk_corp = ?, cpurorganization = ?, cwareid = ?, dorderdate = ?, cvendorid = ?, caccountbankid = ?, cdeptid = ?, cemployeeid = ?, cbiztype = ?, creciever = ?, cgiveinvoicevendor = ?, ctransmodeid = ?, ctermProtocolid = ?, ibillstatus = ?, vmemo = ?, caccountyear = ?, coperator = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, vdef7 = ?, vdef8 = ?, vdef9 = ?, vdef10 = ?, vdef11 = ?, vdef12 = ?, vdef13 = ?, vdef14 = ?, vdef15 = ?, vdef16 = ?, vdef17 = ?, vdef18 = ?, vdef19 = ?, vdef20 = ?, pk_defdoc1 = ?, pk_defdoc2 = ?, pk_defdoc3 = ?, pk_defdoc4 = ?, pk_defdoc5 = ?, pk_defdoc6 = ?, pk_defdoc7 = ?, pk_defdoc8 = ?, pk_defdoc9 = ?, pk_defdoc10 = ?, pk_defdoc11 = ?, pk_defdoc12 = ?, pk_defdoc13 = ?, pk_defdoc14 = ?, pk_defdoc15 = ?, pk_defdoc16 = ?, pk_defdoc17 = ?, pk_defdoc18 = ?, pk_defdoc19 = ?, pk_defdoc20 = ?, cvendormangid = ?, cauditpsn = ?, dauditdate = ?, tlastmaketime = ?  where corderid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (orderHeader.getVordercode() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, orderHeader.getVordercode());
      }
      if (orderHeader.getPk_corp() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, orderHeader.getPk_corp());
      }
      if (orderHeader.getCpurorganization() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, orderHeader.getCpurorganization());
      }
      if (orderHeader.getCwareid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, orderHeader.getCwareid());
      }
      if (orderHeader.getDorderdate() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, orderHeader.getDorderdate().toString());
      }
      if (orderHeader.getCvendorid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, orderHeader.getCvendorid());
      }
      if (orderHeader.getCaccountbankid() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, orderHeader.getCaccountbankid());
      }
      if (orderHeader.getCdeptid() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, orderHeader.getCdeptid());
      }
      if (orderHeader.getCemployeeid() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, orderHeader.getCemployeeid());
      }
      if (orderHeader.getCbiztype() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, orderHeader.getCbiztype());
      }
      if (orderHeader.getCreciever() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, orderHeader.getCreciever());
      }
      if (orderHeader.getCgiveinvoicevendor() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, orderHeader.getCgiveinvoicevendor());
      }
      if (orderHeader.getCtransmodeid() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, orderHeader.getCtransmodeid());
      }
      if (orderHeader.getCtermProtocolid() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, orderHeader.getCtermProtocolid());
      }
      if (orderHeader.getIbillstatus() == null)
        stmt.setNull(15, 4);
      else {
        stmt.setInt(15, orderHeader.getIbillstatus().intValue());
      }
      if (orderHeader.getVmemo() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, orderHeader.getVmemo());
      }
      if (orderHeader.getCaccountyear() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, orderHeader.getCaccountyear());
      }
      if (orderHeader.getCoperator() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, orderHeader.getCoperator());
      }
      if (orderHeader.getVdef1() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, orderHeader.getVdef1());
      }
      if (orderHeader.getVdef2() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, orderHeader.getVdef2());
      }
      if (orderHeader.getVdef3() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, orderHeader.getVdef3());
      }
      if (orderHeader.getVdef4() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, orderHeader.getVdef4());
      }
      if (orderHeader.getVdef5() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, orderHeader.getVdef5());
      }
      if (orderHeader.getVdef6() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, orderHeader.getVdef6());
      }
      if (orderHeader.getVdef7() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, orderHeader.getVdef7());
      }
      if (orderHeader.getVdef8() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, orderHeader.getVdef8());
      }
      if (orderHeader.getVdef9() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, orderHeader.getVdef9());
      }
      if (orderHeader.getVdef10() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, orderHeader.getVdef10());
      }

      if (orderHeader.getVdef11() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, orderHeader.getVdef11());
      }
      if (orderHeader.getVdef12() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, orderHeader.getVdef12());
      }
      if (orderHeader.getVdef13() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, orderHeader.getVdef13());
      }
      if (orderHeader.getVdef14() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, orderHeader.getVdef14());
      }
      if (orderHeader.getVdef15() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, orderHeader.getVdef15());
      }
      if (orderHeader.getVdef16() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, orderHeader.getVdef16());
      }
      if (orderHeader.getVdef17() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, orderHeader.getVdef17());
      }
      if (orderHeader.getVdef18() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, orderHeader.getVdef18());
      }
      if (orderHeader.getVdef19() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, orderHeader.getVdef19());
      }
      if (orderHeader.getVdef20() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, orderHeader.getVdef20());
      }
      if (orderHeader.getPKDefDoc1() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, orderHeader.getPKDefDoc1());
      }
      if (orderHeader.getPKDefDoc2() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, orderHeader.getPKDefDoc2());
      }
      if (orderHeader.getPKDefDoc3() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, orderHeader.getPKDefDoc3());
      }
      if (orderHeader.getPKDefDoc4() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, orderHeader.getPKDefDoc4());
      }
      if (orderHeader.getPKDefDoc5() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, orderHeader.getPKDefDoc5());
      }
      if (orderHeader.getPKDefDoc6() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, orderHeader.getPKDefDoc6());
      }
      if (orderHeader.getPKDefDoc7() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, orderHeader.getPKDefDoc7());
      }
      if (orderHeader.getPKDefDoc8() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, orderHeader.getPKDefDoc8());
      }
      if (orderHeader.getPKDefDoc9() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, orderHeader.getPKDefDoc9());
      }
      if (orderHeader.getPKDefDoc10() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, orderHeader.getPKDefDoc10());
      }
      if (orderHeader.getPKDefDoc11() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, orderHeader.getPKDefDoc11());
      }
      if (orderHeader.getPKDefDoc12() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, orderHeader.getPKDefDoc12());
      }
      if (orderHeader.getPKDefDoc13() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, orderHeader.getPKDefDoc13());
      }
      if (orderHeader.getPKDefDoc14() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, orderHeader.getPKDefDoc14());
      }
      if (orderHeader.getPKDefDoc15() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, orderHeader.getPKDefDoc15());
      }
      if (orderHeader.getPKDefDoc16() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, orderHeader.getPKDefDoc16());
      }
      if (orderHeader.getPKDefDoc17() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, orderHeader.getPKDefDoc17());
      }
      if (orderHeader.getPKDefDoc18() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, orderHeader.getPKDefDoc18());
      }
      if (orderHeader.getPKDefDoc19() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, orderHeader.getPKDefDoc19());
      }
      if (orderHeader.getPKDefDoc20() == null)
        stmt.setNull(58, 1);
      else {
        stmt.setString(58, orderHeader.getPKDefDoc20());
      }

      if (orderHeader.getCvendormangid() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, orderHeader.getCvendormangid());
      }

      if (orderHeader.getCauditpsn() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, orderHeader.getCauditpsn());
      }

      if (orderHeader.getDauditdate() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, orderHeader.getDauditdate().toString());
      }
      if (orderHeader.getTlastmaketime() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, orderHeader.getTlastmaketime().toString());
      }

      stmt.setString(63, orderHeader.getPrimaryKey());

      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "updateHeader", new Object[] { orderHeader });
  }

  public void updateItem(OrderItemVO orderItem)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "updateItem", new Object[] { orderItem });

    String sql = "update sc_order_b set corderid = ?, pk_corp = ?, cmangid = ?, cbaseid = ?, nordernum = ?, cassistunit = ?," +
    		" nassistnum = ?, ndiscountrate = ?, idiscounttaxtype = ?, ntaxrate = ?, ccurrencytypeid = ?, noriginalnetprice = ?," +
    		" noriginalcurprice = ?, noriginalcurmny = ?, noriginaltaxmny = ?, noriginalsummny = ?, nexchangeotobrate = ?, ntaxmny = ?," +
    		" nmoney = ?, nsummny = ?, nexchangeotoarate = ?, nassistcurmny = ?, nassisttaxmny = ?, nassistsummny = ?, naccumarrvnum = ?," +
    		" naccumstorenum = ?, naccuminvoicenum = ?, naccumwastnum = ?, dplanarrvdate = ?, cwarehouseid = ?, creceiveaddress = ?, cprojectid = ?," +
    		" cprojectphaseid = ?, coperator = ?, forderrowstatus = ?, bisactive = ?, cordersource = ?, csourcebillid = ?, csourcebillrow = ?," +
    		" cupsourcebilltype = ?, cupsourcebillid = ?, cupsourcebillrowid = ?, vmemo = ?, vfree1 = ?, vfree2 = ?, vfree3 = ?, vfree4 = ?," +
    		" vfree5 = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, vdef7 = ?, vdef8 = ?, vdef9 = ?, vdef10 = ?, vdef11 = ?," +
    		" vdef12 = ?, vdef13 = ?, vdef14 = ?, vdef15 = ?, vdef16 = ?, vdef17 = ?, vdef18 = ?, vdef19 = ?, vdef20 = ?, pk_defdoc1 = ?, pk_defdoc2 = ?," +
    		" pk_defdoc3 = ?, pk_defdoc4 = ?, pk_defdoc5 = ?, pk_defdoc6 = ?, pk_defdoc7 = ?, pk_defdoc8 = ?, pk_defdoc9 = ?, pk_defdoc10 = ?," +
    		" pk_defdoc11 = ?, pk_defdoc12 = ?, pk_defdoc13 = ?, pk_defdoc14 = ?, pk_defdoc15 = ?, pk_defdoc16 = ?, pk_defdoc17 = ?, pk_defdoc18 = ?," +
    		" pk_defdoc19 = ?, pk_defdoc20 = ?, crowno = ?, norgtaxprice = ?, norgnettaxprice = ?, vproducenum = ?, ccontractid = ?, ccontractrowid = ?," +
    		" ccontractrcode = ?, vpriceauditcode = ?, cpriceauditid = ?, cpriceaudit_bid = ?, cpriceaudit_bb1id = ? , bomvers = ? " +//shikun bomvers
    		" where corder_bid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (orderItem.getCorderid() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, orderItem.getCorderid());
      }
      if (orderItem.getPk_corp() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, orderItem.getPk_corp());
      }
      if (orderItem.getCmangid() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, orderItem.getCmangid());
      }
      if (orderItem.getCbaseid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, orderItem.getCbaseid());
      }
      if (orderItem.getNordernum() == null)
        stmt.setNull(5, 4);
      else {
        stmt.setBigDecimal(5, orderItem.getNordernum().toBigDecimal());
      }
      if (orderItem.getCassistunit() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, orderItem.getCassistunit());
      }
      if (orderItem.getNassistnum() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, orderItem.getNassistnum().toBigDecimal());
      }
      if (orderItem.getNdiscountrate() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, orderItem.getNdiscountrate().toBigDecimal());
      }
      if (orderItem.getIdiscounttaxtype() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setInt(9, orderItem.getIdiscounttaxtype().intValue());
      }
      if (orderItem.getNtaxrate() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setBigDecimal(10, orderItem.getNtaxrate().toBigDecimal());
      }
      if (orderItem.getCcurrencytypeid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, orderItem.getCcurrencytypeid());
      }
      if (orderItem.getNoriginalnetprice() == null)
        stmt.setNull(12, 4);
      else {
        stmt.setBigDecimal(12, orderItem.getNoriginalnetprice().toBigDecimal());
      }
      if (orderItem.getNoriginalcurprice() == null)
        stmt.setNull(13, 4);
      else {
        stmt.setBigDecimal(13, orderItem.getNoriginalcurprice().toBigDecimal());
      }
      if (orderItem.getNoriginalcurmny() == null)
        stmt.setNull(14, 4);
      else {
        stmt.setBigDecimal(14, orderItem.getNoriginalcurmny().toBigDecimal());
      }
      if (orderItem.getNoriginaltaxmny() == null)
        stmt.setNull(15, 4);
      else {
        stmt.setBigDecimal(15, orderItem.getNoriginaltaxmny().toBigDecimal());
      }
      if (orderItem.getNoriginalsummny() == null)
        stmt.setNull(16, 4);
      else {
        stmt.setBigDecimal(16, orderItem.getNoriginalsummny().toBigDecimal());
      }
      if (orderItem.getNexchangeotobrate() == null)
        stmt.setNull(17, 4);
      else {
        stmt.setBigDecimal(17, orderItem.getNexchangeotobrate().toBigDecimal());
      }
      if (orderItem.getNtaxmny() == null)
        stmt.setNull(18, 4);
      else {
        stmt.setBigDecimal(18, orderItem.getNtaxmny().toBigDecimal());
      }
      if (orderItem.getNmoney() == null)
        stmt.setNull(19, 4);
      else {
        stmt.setBigDecimal(19, orderItem.getNmoney().toBigDecimal());
      }
      if (orderItem.getNsummny() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, orderItem.getNsummny().toBigDecimal());
      }
      if (orderItem.getNexchangeotoarate() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, orderItem.getNexchangeotoarate().toBigDecimal());
      }
      if (orderItem.getNassistcurmny() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, orderItem.getNassistcurmny().toBigDecimal());
      }
      if (orderItem.getNassisttaxmny() == null)
        stmt.setNull(23, 4);
      else {
        stmt.setBigDecimal(23, orderItem.getNassisttaxmny().toBigDecimal());
      }
      if (orderItem.getNassistsummny() == null)
        stmt.setNull(24, 4);
      else {
        stmt.setBigDecimal(24, orderItem.getNassistsummny().toBigDecimal());
      }
      if (orderItem.getNaccumarrvnum() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setBigDecimal(25, orderItem.getNaccumarrvnum().toBigDecimal());
      }
      if (orderItem.getNaccumstorenum() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setBigDecimal(26, orderItem.getNaccumstorenum().toBigDecimal());
      }
      if (orderItem.getNaccuminvoicenum() == null)
        stmt.setNull(27, 4);
      else {
        stmt.setBigDecimal(27, orderItem.getNaccuminvoicenum().toBigDecimal());
      }
      if (orderItem.getNaccumwastnum() == null)
        stmt.setNull(28, 4);
      else {
        stmt.setBigDecimal(28, orderItem.getNaccumwastnum().toBigDecimal());
      }
      if (orderItem.getDplanarrvdate() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, orderItem.getDplanarrvdate().toString());
      }
      if (orderItem.getCwarehouseid() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, orderItem.getCwarehouseid());
      }
      if (orderItem.getCreceiveaddress() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, orderItem.getCreceiveaddress());
      }
      if (orderItem.getCprojectid() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, orderItem.getCprojectid());
      }
      if (orderItem.getCprojectphaseid() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, orderItem.getCprojectphaseid());
      }
      if (orderItem.getCoperator() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, orderItem.getCoperator());
      }
      if (orderItem.getForderrowstatus() == null)
        stmt.setNull(35, 4);
      else {
        stmt.setInt(35, orderItem.getForderrowstatus().intValue());
      }
      if (orderItem.getBisactive() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, orderItem.getBisactive().booleanValue() ? "0" : "1");
      }
      if (orderItem.getCordersource() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, orderItem.getCordersource());
      }
      if (orderItem.getCsourcebillid() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, orderItem.getCsourcebillid());
      }
      if (orderItem.getCsourcebillrow() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, orderItem.getCsourcebillrow());
      }
      if (orderItem.getCupsourcebilltype() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, orderItem.getCupsourcebilltype());
      }
      if (orderItem.getCupsourcebillid() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, orderItem.getCupsourcebillid());
      }
      if (orderItem.getCupsourcebillrowid() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, orderItem.getCupsourcebillrowid());
      }
      if (orderItem.getVmemo() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, orderItem.getVmemo());
      }
      if (orderItem.getVfree1() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, orderItem.getVfree1());
      }
      if (orderItem.getVfree2() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, orderItem.getVfree2());
      }
      if (orderItem.getVfree3() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, orderItem.getVfree3());
      }
      if (orderItem.getVfree4() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, orderItem.getVfree4());
      }
      if (orderItem.getVfree5() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, orderItem.getVfree5());
      }
      if (orderItem.getVdef1() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, orderItem.getVdef1());
      }
      if (orderItem.getVdef2() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, orderItem.getVdef2());
      }
      if (orderItem.getVdef3() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, orderItem.getVdef3());
      }
      if (orderItem.getVdef4() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, orderItem.getVdef4());
      }
      if (orderItem.getVdef5() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, orderItem.getVdef5());
      }
      if (orderItem.getVdef6() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, orderItem.getVdef6());
      }
      if (orderItem.getVdef7() == null) {
        stmt.setNull(55, 1);
      }
      else {
        stmt.setString(55, orderItem.getVdef7());
      }
      if (orderItem.getVdef8() == null) {
        stmt.setNull(56, 1);
      }
      else {
        stmt.setString(56, orderItem.getVdef8());
      }
      if (orderItem.getVdef9() == null) {
        stmt.setNull(57, 1);
      }
      else {
        stmt.setString(57, orderItem.getVdef9());
      }
      if (orderItem.getVdef10() == null) {
        stmt.setNull(58, 1);
      }
      else {
        stmt.setString(58, orderItem.getVdef10());
      }
      if (orderItem.getVdef11() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, orderItem.getVdef11());
      }
      if (orderItem.getVdef12() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, orderItem.getVdef12());
      }
      if (orderItem.getVdef13() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, orderItem.getVdef13());
      }
      if (orderItem.getVdef14() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, orderItem.getVdef14());
      }
      if (orderItem.getVdef15() == null)
        stmt.setNull(63, 1);
      else {
        stmt.setString(63, orderItem.getVdef15());
      }
      if (orderItem.getVdef16() == null)
        stmt.setNull(64, 1);
      else {
        stmt.setString(64, orderItem.getVdef16());
      }
      if (orderItem.getVdef17() == null)
        stmt.setNull(65, 1);
      else {
        stmt.setString(65, orderItem.getVdef17());
      }
      if (orderItem.getVdef18() == null)
        stmt.setNull(66, 1);
      else {
        stmt.setString(66, orderItem.getVdef18());
      }
      if (orderItem.getVdef19() == null)
        stmt.setNull(67, 1);
      else {
        stmt.setString(67, orderItem.getVdef19());
      }
      if (orderItem.getVdef20() == null)
        stmt.setNull(68, 1);
      else {
        stmt.setString(68, orderItem.getVdef20());
      }
      if (orderItem.getPKDefDoc1() == null)
        stmt.setNull(69, 1);
      else {
        stmt.setString(69, orderItem.getPKDefDoc1());
      }
      if (orderItem.getPKDefDoc2() == null)
        stmt.setNull(70, 1);
      else {
        stmt.setString(70, orderItem.getPKDefDoc2());
      }
      if (orderItem.getPKDefDoc3() == null)
        stmt.setNull(71, 1);
      else {
        stmt.setString(71, orderItem.getPKDefDoc3());
      }
      if (orderItem.getPKDefDoc4() == null)
        stmt.setNull(72, 1);
      else {
        stmt.setString(72, orderItem.getPKDefDoc4());
      }
      if (orderItem.getPKDefDoc5() == null)
        stmt.setNull(73, 1);
      else {
        stmt.setString(73, orderItem.getPKDefDoc5());
      }
      if (orderItem.getPKDefDoc6() == null)
        stmt.setNull(74, 1);
      else {
        stmt.setString(74, orderItem.getPKDefDoc6());
      }
      if (orderItem.getPKDefDoc7() == null)
        stmt.setNull(75, 1);
      else {
        stmt.setString(75, orderItem.getPKDefDoc7());
      }
      if (orderItem.getPKDefDoc8() == null)
        stmt.setNull(76, 1);
      else {
        stmt.setString(76, orderItem.getPKDefDoc8());
      }
      if (orderItem.getPKDefDoc9() == null)
        stmt.setNull(77, 1);
      else {
        stmt.setString(77, orderItem.getPKDefDoc9());
      }
      if (orderItem.getPKDefDoc10() == null)
        stmt.setNull(78, 1);
      else {
        stmt.setString(78, orderItem.getPKDefDoc10());
      }
      if (orderItem.getPKDefDoc11() == null)
        stmt.setNull(79, 1);
      else {
        stmt.setString(79, orderItem.getPKDefDoc11());
      }
      if (orderItem.getPKDefDoc12() == null)
        stmt.setNull(80, 1);
      else {
        stmt.setString(80, orderItem.getPKDefDoc12());
      }
      if (orderItem.getPKDefDoc13() == null)
        stmt.setNull(81, 1);
      else {
        stmt.setString(81, orderItem.getPKDefDoc13());
      }
      if (orderItem.getPKDefDoc14() == null)
        stmt.setNull(82, 1);
      else {
        stmt.setString(82, orderItem.getPKDefDoc14());
      }
      if (orderItem.getPKDefDoc15() == null)
        stmt.setNull(83, 1);
      else {
        stmt.setString(83, orderItem.getPKDefDoc15());
      }
      if (orderItem.getPKDefDoc16() == null)
        stmt.setNull(84, 1);
      else {
        stmt.setString(84, orderItem.getPKDefDoc16());
      }
      if (orderItem.getPKDefDoc17() == null)
        stmt.setNull(85, 1);
      else {
        stmt.setString(85, orderItem.getPKDefDoc17());
      }
      if (orderItem.getPKDefDoc18() == null)
        stmt.setNull(86, 1);
      else {
        stmt.setString(86, orderItem.getPKDefDoc18());
      }
      if (orderItem.getPKDefDoc19() == null)
        stmt.setNull(87, 1);
      else {
        stmt.setString(87, orderItem.getPKDefDoc19());
      }
      if (orderItem.getPKDefDoc20() == null)
        stmt.setNull(88, 1);
      else {
        stmt.setString(88, orderItem.getPKDefDoc20());
      }

      if (orderItem.getCrowno() == null)
        stmt.setNull(89, 1);
      else {
        stmt.setString(89, orderItem.getCrowno());
      }
      if (orderItem.getNorgtaxprice() == null)
        stmt.setNull(90, 4);
      else {
        stmt.setBigDecimal(90, orderItem.getNorgtaxprice().toBigDecimal());
      }
      if (orderItem.getNorgnettaxprice() == null)
        stmt.setNull(91, 4);
      else {
        stmt.setBigDecimal(91, orderItem.getNorgnettaxprice().toBigDecimal());
      }

      if (orderItem.getVproducenum() == null)
        stmt.setNull(92, 1);
      else {
        stmt.setString(92, orderItem.getVproducenum());
      }

      if (orderItem.getCcontractid() == null)
        stmt.setNull(93, 1);
      else {
        stmt.setString(93, orderItem.getCcontractid());
      }

      if (orderItem.getCcontractrowid() == null)
        stmt.setNull(94, 1);
      else {
        stmt.setString(94, orderItem.getCcontractrowid());
      }

      if (orderItem.getCcontractrcode() == null)
        stmt.setNull(95, 1);
      else {
        stmt.setString(95, orderItem.getCcontractrcode());
      }

      if (orderItem.getVpriceauditcode() == null)
        stmt.setNull(96, 1);
      else {
        stmt.setString(96, orderItem.getVpriceauditcode());
      }

      if (orderItem.getCpriceauditid() == null)
        stmt.setNull(97, 1);
      else {
        stmt.setString(97, orderItem.getCpriceauditid());
      }

      if (orderItem.getCpriceaudit_bid() == null)
        stmt.setNull(98, 1);
      else {
        stmt.setString(98, orderItem.getCpriceaudit_bid());
      }

      if (orderItem.getCpriceaudit_bb1id() == null)
        stmt.setNull(99, 1);
      else {
        stmt.setString(99, orderItem.getCpriceaudit_bb1id());
      }

      if (orderItem.getBomvers() == null)//shikun bomvers
        stmt.setNull(100, 1);
      else {
        stmt.setString(100, orderItem.getBomvers());
      }

      stmt.setString(101, orderItem.getPrimaryKey());

      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "updateItem", new Object[] { orderItem });
  }

  public OrderItemVO bbToBVO(OrderBbVO bbVO)
  {
    OrderItemVO itemVO = new OrderItemVO();

    itemVO.setCorderid(bbVO.getCorderid());
    itemVO.setPk_corp(bbVO.getPk_corp());

    itemVO.setCmangid(bbVO.getCmangid());
    itemVO.setCbaseid(bbVO.getCbaseid());

    itemVO.setNordernum(bbVO.getNmaterialnum());
    itemVO.setCassistunit(bbVO.getCassistunit());
    itemVO.setNassistnum(bbVO.getNassistnum());
    itemVO.setNoriginalcurprice(bbVO.getNprice());
    itemVO.setNoriginalcurmny(bbVO.getNmoney());
    itemVO.setDplanarrvdate(bbVO.getDdeliverydate());
    itemVO.setCwarehouseid(bbVO.getCdeliverywarehouse());
    itemVO.setCreceiveaddress(bbVO.getVdeliveryaddress());

    itemVO.setCbbid(bbVO.getCorder_bbid());

    itemVO.setVmemo(bbVO.getVmemo());
    itemVO.setStatus(bbVO.getStatus());
    itemVO.setVdef1(bbVO.getVdef1());
    itemVO.setVdef2(bbVO.getVdef2());
    itemVO.setVdef3(bbVO.getVdef3());
    itemVO.setVdef4(bbVO.getVdef4());
    itemVO.setVdef5(bbVO.getVdef5());
    itemVO.setVdef6(bbVO.getVdef6());
    itemVO.setVfree1(bbVO.getVfree1());
    itemVO.setVfree2(bbVO.getVfree2());
    itemVO.setVfree3(bbVO.getVfree3());
    itemVO.setVfree4(bbVO.getVfree4());
    itemVO.setVfree5(bbVO.getVfree5());

    itemVO.setCinvshow("");
    return itemVO;
  }

  public OrderBbVO bToBbVO(OrderItemVO itemVO)
  {
    OrderBbVO bbVO = new OrderBbVO();

    bbVO.setCorderid(itemVO.getCorderid());
    bbVO.setCorder_bid(itemVO.getCorder_bid());
    bbVO.setPk_corp(itemVO.getPk_corp());

    bbVO.setCbaseid(itemVO.getCbaseid());
    bbVO.setCmangid(itemVO.getCmangid());

    bbVO.setNmaterialnum(itemVO.getNordernum());
    bbVO.setCassistunit(itemVO.getCassistunit());
    bbVO.setNassistnum(itemVO.getNassistnum());
    bbVO.setNprice(itemVO.getNoriginalcurprice());
    bbVO.setNmoney(itemVO.getNoriginalcurmny());
    bbVO.setDdeliverydate(itemVO.getDplanarrvdate());

    bbVO.setCdeliverywarehouse(itemVO.getCwarehouseid());
    bbVO.setVdeliveryaddress(itemVO.getCreceiveaddress());

    bbVO.setVmemo(itemVO.getVmemo());
    bbVO.setStatus(itemVO.getStatus());
    bbVO.setVdef1(itemVO.getVdef1());
    bbVO.setVdef2(itemVO.getVdef2());
    bbVO.setVdef3(itemVO.getVdef3());
    bbVO.setVdef4(itemVO.getVdef4());
    bbVO.setVdef5(itemVO.getVdef5());
    bbVO.setVdef6(itemVO.getVdef6());
    bbVO.setVfree1(itemVO.getVfree1());
    bbVO.setVfree2(itemVO.getVfree2());
    bbVO.setVfree3(itemVO.getVfree3());
    bbVO.setVfree4(itemVO.getVfree4());
    bbVO.setVfree5(itemVO.getVfree5());

    bbVO.setPrimaryKey(itemVO.getCbbid());

    return bbVO;
  }

  public boolean checkGoing(String billId, String ApproveId, String ApproveDate, String checkNote)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "checkGoing", new Object[] { billId, ApproveId, ApproveDate, checkNote });

    String sql = "update sc_order set  cauditpsn = ? ,dauditdate = ? , ibillstatus = 2, taudittime = ? where corderid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (ApproveId == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, ApproveId);
      }
      if (ApproveDate == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, ApproveDate);
      }
      stmt.setString(3, new UFDateTime(new Date()).toString());

      if (billId == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, billId);
      }

      stmt.executeUpdate();
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "checkGoing", new Object[] { billId, ApproveId, ApproveDate, checkNote });

    return true;
  }

  public boolean checkNoPass(String billId, String ApproveId, String ApproveDate, String checkNote)
    throws SQLException, BusinessException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "checkNoPass", new Object[] { billId, ApproveId, ApproveDate, checkNote });

    SCATP atp = new SCATP();
    OrderVO vo = findByPrimaryKey(billId);
    vo.setBillAction("审批未通过");
    atp.modifyATPWhenCloseBill(vo);

    String sql = "update sc_order set  cauditpsn = null ,dauditdate = null , ibillstatus = 4, taudittime = null where corderid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (billId == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, billId);
      }

      stmt.executeUpdate();

      atp.checkAtpInstantly(vo, null);
    }
    catch (Exception e) {
      throw new SQLException(e.getMessage());
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "checkNoPass", new Object[] { billId, ApproveId, ApproveDate, checkNote });

    return true;
  }

  public boolean checkPass(String billId, String ApproveId, String ApproveDate, String checkNote)
    throws BusinessException, SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "checkPass", new Object[] { billId, ApproveId, ApproveDate, checkNote });
    try
    {
      String status = getOrderState(null, billId);
      if ((status == null) || (status.equals("1")))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000061"));
      if (status.equals("3"))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000062"));
    }
    catch (Exception r)
    {
      if ((r instanceof SQLException)) {
        throw ((SQLException)r);
      }
      throw new BusinessException(r.getMessage());
    }
    String sql = "update sc_order set  cauditpsn = ? ,dauditdate = ? , ibillstatus = 3, taudittime = ? where corderid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (ApproveId == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, ApproveId);
      }

      if (ApproveDate == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, ApproveDate);
      }

      stmt.setString(3, new UFDateTime(new Date()).toString());
      if (billId == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, billId);
      }

      stmt.executeUpdate();
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "checkPass", new Object[] { billId, ApproveId, ApproveDate, checkNote });

    return true;
  }

  public String checkWarehouse(OrderVO VO)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "checkWarehouse", new Object[] { VO });

    String sql = "select pk_calbody from bd_stordoc where dr = 0 and pk_stordoc in ";
    OrderItemVO[] bodyVO = (OrderItemVO[])(OrderItemVO[])VO.getChildrenVO();
    if ((bodyVO == null) || (bodyVO.length == 0)) {
      return null;
    }
    Vector vTemp = new Vector();
    vTemp.addElement(bodyVO[0].getCwarehouseid());
    for (int i = 1; i < bodyVO.length; i++) {
      if (!vTemp.contains(bodyVO[i].getCwarehouseid())) {
        vTemp.addElement(bodyVO[i].getCwarehouseid());
      }

    }

    String[] sTemp = new String[vTemp.size()];
    vTemp.copyInto(sTemp);

    String strSetId = null;
    try {
      TempTableDMO dmoTempTbl = new TempTableDMO();
      strSetId = dmoTempTbl.insertTempTable(sTemp, "t_sc_general", "pk_sc");
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }

    sql = sql + strSetId;

    vTemp = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String s = rs.getString(1);
        if ((s != null) && (!vTemp.contains(s)))
          vTemp.addElement(s);
      }
      if (rs != null)
        rs.close();
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "checkWarehouse", new Object[] { VO });

    String pk_calbody = ((OrderHeaderVO)VO.getParentVO()).getCwareid();
    if (vTemp.size() == 0)
      return null;
    if ((vTemp.contains(pk_calbody)) && (vTemp.size() == 1)) {
      return null;
    }
    return NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000063");
  }

  public void deleteMaterialItem(OrderBbVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderBbDMO", "deleteMaterialItem", new Object[] { vo });

    String sql = "update sc_order_bb set dr=1 where corder_bbid = ? ";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, vo.getPrimaryKey());
      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderBbDMO", "deleteMaterialItem", new Object[] { vo });
  }

  public void discardBill(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "discardBill", new Object[] { key });

    String sql = "update sc_order set  dr = 1,ibillstatus = 1   where corderid = ? ";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, key);

      stmt.executeUpdate();
    } finally {
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
    String sql1 = " update sc_order_b set  dr = 1   where corderid = '" + key + "' ";
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql1);

      stmt.executeUpdate();
    } finally {
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
    afterCallMethod("nc.bs.sc.order.OrderDMO", "discardBill", new Object[] { key });
  }

  public OrderItemVO[] findAllItemsAllForHeader(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findItemsAllForHeader", new Object[] { key });

    String patch = "";

    patch = patch + " left join bd_invbasdoc on sc_order_b.cbaseid=bd_invbasdoc.pk_invbasdoc \n";

    patch = patch + " left join bd_measdoc measdo1 on bd_invbasdoc.pk_measdoc=measdo1.pk_measdoc   \n";

    patch = patch + " left join bd_measdoc measdo2 on sc_order_b.cassistunit=measdo2.pk_measdoc  \n";

    patch = patch + " left join bd_stordoc on sc_order_b.cwarehouseid=bd_stordoc.pk_stordoc  \n";

    patch = patch + " left join bd_jobmngfil on sc_order_b.cprojectid=bd_jobmngfil.pk_jobmngfil  \n";
    patch = patch + " left join bd_jobbasfil on bd_jobbasfil.pk_jobbasfil=bd_jobmngfil.pk_jobbasfil \n ";

    patch = patch + " left join bd_jobobjpha on sc_order_b.cprojectphaseid=bd_jobobjpha.pk_jobobjpha  \n";
    patch = patch + " left join bd_jobphase on bd_jobobjpha.pk_jobphase = bd_jobphase.pk_jobphase  \n";

    patch = patch + " left join bd_currtype on sc_order_b.ccurrencytypeid = bd_currtype.pk_currtype \n";

    String select = " \n select corder_bid, corderid, sc_order_b.pk_corp, cmangid, cbaseid, nordernum, \n";
    select = select + " cassistunit, nassistnum, ndiscountrate, idiscounttaxtype, ntaxrate, \n";
    select = select + " ccurrencytypeid, noriginalnetprice, noriginalcurprice, noriginalcurmny, \n";
    select = select + "  noriginaltaxmny, noriginalsummny, nexchangeotobrate, ntaxmny, nmoney, \n";
    select = select + " nsummny, nexchangeotoarate, nassistcurmny, nassisttaxmny, nassistsummny, \n";
    select = select + " naccumarrvnum, naccumstorenum, naccuminvoicenum, naccumwastnum, \n";
    select = select + " dplanarrvdate, cwarehouseid, creceiveaddress, cprojectid,  \n";
    select = select + " cprojectphaseid, coperator, forderrowstatus, bisactive, cordersource, \n";
    select = select + " csourcebillid, csourcebillrow, cupsourcebilltype, cupsourcebillid, \n";
    select = select + " cupsourcebillrowid, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5, \n";
    select = select + " vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15," +
    		" vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7," +
    		" pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17," +
    		" pk_defdoc18, pk_defdoc19, pk_defdoc20, crowno, norgtaxprice, norgnettaxprice, nbackarrvnum,nbackstorenum,ccontractid," +
    		"ccontractrowid,ccontractrcode,vpriceauditcode,cpriceauditid,cpriceaudit_bid,cpriceaudit_bb1id,bomvers  \n";//shikun bomvers

    StringBuffer strbuf = new StringBuffer();

    strbuf.append(" ,invcode,invname,invspec,invtype\n ");

    strbuf.append(" ,measdo1.measname\n ");

    strbuf.append(" ,measdo2.measname\n ");

    strbuf.append(" ,bd_stordoc.storname\n ");

    strbuf.append(" ,bd_jobbasfil.jobname\n ");

    strbuf.append(" ,bd_jobphase.jobphasename\n ");

    strbuf.append(" , bd_currtype.currtypename \n ");

    String sql = select + strbuf.toString() + " from sc_order_b " + patch + " where sc_order_b.dr=0 and corderid = ? ";

    OrderItemVO[] orderItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        OrderItemVO orderItem = new OrderItemVO();

        String corder_bid = rs.getString("corder_bid");
        orderItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String corderid = rs.getString("corderid");
        orderItem.setCorderid(corderid == null ? null : corderid.trim());

        String pk_corp = rs.getString("pk_corp");
        orderItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cmangid = rs.getString("cmangid");
        orderItem.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString("cbaseid");
        orderItem.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal nordernum = rs.getBigDecimal("nordernum");
        orderItem.setNordernum(nordernum == null ? null : new UFDouble(nordernum));
        orderItem.setNoldnum(nordernum == null ? null : new UFDouble(nordernum));

        String cassistunit = rs.getString("cassistunit");
        orderItem.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = rs.getBigDecimal("nassistnum");
        orderItem.setNassistnum(nassistnum == null ? null : new UFDouble(nassistnum));

        BigDecimal ndiscountrate = rs.getBigDecimal("ndiscountrate");
        orderItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        Integer idiscounttaxtype = (Integer)rs.getObject("idiscounttaxtype");
        orderItem.setIdiscounttaxtype(idiscounttaxtype == null ? null : idiscounttaxtype);

        BigDecimal ntaxrate = rs.getBigDecimal("ntaxrate");
        orderItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        orderItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalnetprice = rs.getBigDecimal("noriginalnetprice");
        orderItem.setNoriginalnetprice(noriginalnetprice == null ? null : new UFDouble(noriginalnetprice));

        BigDecimal noriginalcurprice = rs.getBigDecimal("noriginalcurprice");
        orderItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurmny = rs.getBigDecimal("noriginalcurmny");
        orderItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginaltaxmny = rs.getBigDecimal("noriginaltaxmny");
        orderItem.setNoriginaltaxmny(noriginaltaxmny == null ? null : new UFDouble(noriginaltaxmny));

        BigDecimal noriginalsummny = rs.getBigDecimal("noriginalsummny");
        orderItem.setNoriginalsummny(noriginalsummny == null ? null : new UFDouble(noriginalsummny));

        BigDecimal nexchangeotobrate = rs.getBigDecimal("nexchangeotobrate");
        orderItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal ntaxmny = rs.getBigDecimal("ntaxmny");
        orderItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmoney = rs.getBigDecimal("nmoney");
        orderItem.setNmoney(nmoney == null ? null : new UFDouble(nmoney));

        BigDecimal nsummny = rs.getBigDecimal("nsummny");
        orderItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nexchangeotoarate = rs.getBigDecimal("nexchangeotoarate");
        orderItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nassistcurmny = rs.getBigDecimal("nassistcurmny");
        orderItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassisttaxmny = rs.getBigDecimal("nassisttaxmny");
        orderItem.setNassisttaxmny(nassisttaxmny == null ? null : new UFDouble(nassisttaxmny));

        BigDecimal nassistsummny = rs.getBigDecimal("nassistsummny");
        orderItem.setNassistsummny(nassistsummny == null ? null : new UFDouble(nassistsummny));

        BigDecimal naccumarrvnum = rs.getBigDecimal("naccumarrvnum");
        orderItem.setNaccumarrvnum(naccumarrvnum == null ? null : new UFDouble(naccumarrvnum));

        BigDecimal naccumstorenum = rs.getBigDecimal("naccumstorenum");
        orderItem.setNaccumstorenum(naccumstorenum == null ? null : new UFDouble(naccumstorenum));

        BigDecimal naccuminvoicenum = rs.getBigDecimal("naccuminvoicenum");
        orderItem.setNaccuminvoicenum(naccuminvoicenum == null ? null : new UFDouble(naccuminvoicenum));

        BigDecimal naccumwastnum = rs.getBigDecimal("naccumwastnum");
        orderItem.setNaccumwastnum(naccumwastnum == null ? null : new UFDouble(naccumwastnum));

        String dplanarrvdate = rs.getString("dplanarrvdate");
        orderItem.setDplanarrvdate(dplanarrvdate == null ? null : new UFDate(dplanarrvdate.trim(), false));

        String cwarehouseid = rs.getString("cwarehouseid");
        orderItem.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String creceiveaddress = rs.getString("creceiveaddress");
        orderItem.setCreceiveaddress(creceiveaddress == null ? null : creceiveaddress.trim());

        String cprojectid = rs.getString("cprojectid");
        orderItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        orderItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String coperator = rs.getString("coperator");
        orderItem.setCoperator(coperator == null ? null : coperator.trim());

        Integer forderrowstatus = (Integer)rs.getObject("forderrowstatus");
        orderItem.setForderrowstatus(forderrowstatus == null ? null : forderrowstatus);

        String bisactive = rs.getString("bisactive");
        if (bisactive == null)
          orderItem.setBisactive(null);
        else if (bisactive.trim().equals("1"))
          orderItem.setBisactive(new UFBoolean(false));
        else {
          orderItem.setBisactive(new UFBoolean(true));
        }

        String cordersource = rs.getString("cordersource");
        orderItem.setCordersource(cordersource == null ? null : cordersource.trim());

        String csourcebillid = rs.getString("csourcebillid");
        orderItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillrow = rs.getString("csourcebillrow");
        orderItem.setCsourcebillrow(csourcebillrow == null ? null : csourcebillrow.trim());

        String cupsourcebilltype = rs.getString("cupsourcebilltype");
        orderItem.setCupsourcebilltype(cupsourcebilltype == null ? null : cupsourcebilltype.trim());

        String cupsourcebillid = rs.getString("cupsourcebillid");
        orderItem.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillrowid = rs.getString("cupsourcebillrowid");
        orderItem.setCupsourcebillrowid(cupsourcebillrowid == null ? null : cupsourcebillrowid.trim());

        String vmemo = rs.getString("vmemo");
        orderItem.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString("vfree1");
        orderItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        orderItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        orderItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        orderItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        orderItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        orderItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        orderItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        orderItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        orderItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        orderItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        orderItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString("vdef7");
        orderItem.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString("vdef8");
        orderItem.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString("vdef9");
        orderItem.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString("vdef10");
        orderItem.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString("vdef11");
        orderItem.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString("vdef12");
        orderItem.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString("vdef13");
        orderItem.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString("vdef14");
        orderItem.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString("vdef15");
        orderItem.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString("vdef16");
        orderItem.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString("vdef17");
        orderItem.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString("vdef18");
        orderItem.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString("vdef19");
        orderItem.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString("vdef20");
        orderItem.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString("pk_defdoc1");
        orderItem.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString("pk_defdoc2");
        orderItem.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString("pk_defdoc3");
        orderItem.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString("pk_defdoc4");
        orderItem.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString("pk_defdoc5");
        orderItem.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString("pk_defdoc6");
        orderItem.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString("pk_defdoc7");
        orderItem.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString("pk_defdoc8");
        orderItem.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString("pk_defdoc9");
        orderItem.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString("pk_defdoc10");
        orderItem.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString("pk_defdoc11");
        orderItem.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString("pk_defdoc12");
        orderItem.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString("pk_defdoc13");
        orderItem.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString("pk_defdoc14");
        orderItem.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString("pk_defdoc15");
        orderItem.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString("pk_defdoc16");
        orderItem.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString("pk_defdoc17");
        orderItem.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString("pk_defdoc18");
        orderItem.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString("pk_defdoc19");
        orderItem.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString("pk_defdoc20");
        orderItem.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String invcode = rs.getString("invcode");
        orderItem.setVinvcode(invcode == null ? null : invcode.trim());

        String invname = rs.getString("invname");
        orderItem.setVinvname(invname == null ? null : invname.trim());

        String invspec = rs.getString("invspec");
        orderItem.setVinvspec(invspec == null ? null : invspec.trim());

        String invtype = rs.getString("invtype");
        orderItem.setVinvtype(invtype == null ? null : invtype.trim());

        String crowno = rs.getString("crowno");
        orderItem.setCrowno(crowno == null ? null : crowno.trim());

        BigDecimal norgtaxprice = rs.getBigDecimal("norgtaxprice");
        orderItem.setNorgtaxprice(norgtaxprice == null ? null : new UFDouble(norgtaxprice));

        BigDecimal norgnettaxprice = rs.getBigDecimal("norgnettaxprice");
        orderItem.setNorgnettaxprice(norgnettaxprice == null ? null : new UFDouble(norgnettaxprice));

        BigDecimal nbackarrvnum = rs.getBigDecimal("nbackarrvnum");
        orderItem.setNbackarrvnum(nbackarrvnum == null ? null : new UFDouble(nbackarrvnum));

        BigDecimal nbackstorenum = rs.getBigDecimal("nbackstorenum");
        orderItem.setNbackstorenum(nbackstorenum == null ? null : new UFDouble(nbackstorenum));

        String ccontractid = rs.getString("ccontractid");
        orderItem.setCcontractid(ccontractid == null ? null : ccontractid.trim());

        String ccontractrowid = rs.getString("ccontractrowid");
        orderItem.setCcontractrowid(ccontractrowid == null ? null : ccontractrowid.trim());

        String ccontractrcode = rs.getString("ccontractrcode");
        orderItem.setCcontractrcode(ccontractrcode == null ? null : ccontractrcode.trim());

        String vpriceauditcode = rs.getString("vpriceauditcode");
        orderItem.setVpriceauditcode(vpriceauditcode == null ? null : vpriceauditcode.trim());

        String cpriceauditid = rs.getString("cpriceauditid");
        orderItem.setCpriceauditid(cpriceauditid == null ? null : cpriceauditid.trim());

        String cpriceaudit_bid = rs.getString("cpriceaudit_bid");
        orderItem.setCpriceaudit_bid(cpriceaudit_bid == null ? null : cpriceaudit_bid.trim());

        String cpriceaudit_bb1id = rs.getString("cpriceaudit_bb1id");
        orderItem.setCpriceaudit_bb1id(cpriceaudit_bb1id == null ? null : cpriceaudit_bb1id.trim());

        String bomvers = rs.getString("bomvers");
        orderItem.setBomvers(bomvers == null ? null : bomvers.trim());

        int index = 107;//shikun add bomvers 106
        String measdocname = rs.getString(index++);
        orderItem.setVmeasdocname(measdocname == null ? null : measdocname.trim());

        String assunitname = rs.getString(index++);
        orderItem.setVassunitname(assunitname == null ? null : assunitname.trim());

        String storename = rs.getString(index++);
        orderItem.setVwarehousename(storename == null ? null : storename.trim());

        String projectname = rs.getString(index++);
        orderItem.setVprojectname(projectname == null ? null : projectname.trim());

        String projectphasename = rs.getString(index++);
        orderItem.setVprojectphasename(projectphasename == null ? null : projectphasename.trim());

        String currencyname = rs.getString(index++);
        orderItem.setVcurrencyname(currencyname == null ? null : currencyname.trim());

        v.addElement(orderItem);
      }
    } finally {
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
    orderItems = new OrderItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(orderItems);
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findItemsAllForHeader", new Object[] { key });

    return orderItems;
  }

  public BD_ConvertVO[] findBd_Converts(String[] pk_invbasdoc, String[] pk_measdoc)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findBd_Converts", new Object[] { pk_invbasdoc, pk_measdoc });

    if ((pk_invbasdoc == null) || (pk_invbasdoc.length == 0))
      return null;
    if ((pk_measdoc == null) || (pk_measdoc.length == 0)) {
      return null;
    }
    String sTemp1 = null;
    String sTemp2 = null;
    ArrayList list1 = new ArrayList();
    ArrayList list2 = new ArrayList();
    ArrayList listTmp = null;
    for (int i = 0; i < pk_invbasdoc.length; i++) {
      listTmp = new ArrayList();
      listTmp.add(new Integer(i));
      listTmp.add(pk_invbasdoc[i]);
      list1.add(listTmp);
      listTmp = new ArrayList();
      listTmp.add(new Integer(i));
      listTmp.add(pk_measdoc[i]);
      list2.add(listTmp);
    }
    try {
      TempTableDMO dmo = new TempTableDMO();
      sTemp1 = dmo.getTempStringTable("t_sc_body", new String[] { "pk_sc", "id1" }, new String[] { "int", "char(20)" }, "pk_sc", list1);
      sTemp2 = dmo.getTempStringTable("t_sc_bbody", new String[] { "pk_sc", "id2" }, new String[] { "int", "char(20)" }, "pk_sc", list2);
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }

    String sql = "select a.pk_invbasdoc, a.pk_measdoc, a.fixedflag, a.mainmeasrate ";
    sql = sql + "from bd_convert a, " + sTemp1 + " as b, " + sTemp2 + " as c ";
    sql = sql + "where a.dr = 0 and a.pk_invbasdoc = b.id1 and a.pk_measdoc = c.id2 and b.pk_sc = c.pk_sc";

    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable t = new Hashtable();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String s1 = rs.getString(1);
        String s2 = rs.getString(2);

        String s3 = rs.getString(3);

        Object o = rs.getObject(4);

        BD_ConvertVO VO = new BD_ConvertVO();
        if (s3 != null)
          VO.setBfixedflag(new UFBoolean(s3));
        else
          VO.setBfixedflag(null);
        if (o != null)
          VO.setNmainmeasrate(new UFDouble(o.toString()));
        else {
          VO.setNmainmeasrate(null);
        }
        t.put(s1 + s2, VO);
      }
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findBd_Converts", new Object[] { pk_invbasdoc, pk_measdoc });

    BD_ConvertVO[] VOs = new BD_ConvertVO[pk_invbasdoc.length];
    for (int i = 0; i < VOs.length; i++) {
      VOs[i] = new BD_ConvertVO();
      Object o = t.get(pk_invbasdoc[i] + pk_measdoc[i]);
      if (o != null) {
        VOs[i] = ((BD_ConvertVO)o);
      }
    }
    return VOs;
  }

  public boolean findBillCode(String billcode)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findBillCode", new Object[] { billcode });

    String sql = "select vordercode  from sc_order where vordercode = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, billcode);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        return true;
      }
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findBillCode", new Object[] { billcode });

    return false;
  }

  public OrderItemVO[] findItemsAllForHeader(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findItemsAllForHeader", new Object[] { key });

    String patch = "";

    patch = patch + " left join bd_invbasdoc on sc_order_b.cbaseid=bd_invbasdoc.pk_invbasdoc  ";

    patch = patch + " left join bd_measdoc measdo1 on bd_invbasdoc.pk_measdoc=measdo1.pk_measdoc    ";

    patch = patch + " left join bd_measdoc measdo2 on sc_order_b.cassistunit=measdo2.pk_measdoc   ";

    patch = patch + " left join bd_stordoc on sc_order_b.cwarehouseid=bd_stordoc.pk_stordoc   ";

    patch = patch + " left join bd_jobmngfil on sc_order_b.cprojectid=bd_jobmngfil.pk_jobmngfil   ";
    patch = patch + " left join bd_jobbasfil on bd_jobbasfil.pk_jobbasfil=bd_jobmngfil.pk_jobbasfil   ";

    patch = patch + " left join bd_jobobjpha on sc_order_b.cprojectphaseid=bd_jobobjpha.pk_jobobjpha   ";
    patch = patch + " left join bd_jobphase on bd_jobobjpha.pk_jobphase = bd_jobphase.pk_jobphase   ";

    patch = patch + " left join bd_currtype on sc_order_b.ccurrencytypeid = bd_currtype.pk_currtype  ";

    String select = "   select corder_bid, corderid, sc_order_b.pk_corp, cmangid, cbaseid, nordernum,  ";
    select = select + " cassistunit, nassistnum, ndiscountrate, idiscounttaxtype, ntaxrate,  ";
    select = select + " ccurrencytypeid, noriginalnetprice, noriginalcurprice, noriginalcurmny,  ";
    select = select + "  noriginaltaxmny, noriginalsummny, nexchangeotobrate, ntaxmny, nmoney,  ";
    select = select + " nsummny, nexchangeotoarate, nassistcurmny, nassisttaxmny, nassistsummny,  ";
    select = select + " naccumarrvnum, naccumstorenum, naccuminvoicenum, naccumwastnum,  ";
    select = select + " dplanarrvdate, cwarehouseid, creceiveaddress, cprojectid,   ";
    select = select + " cprojectphaseid, coperator, forderrowstatus, bisactive, cordersource,  ";
    select = select + " csourcebillid, csourcebillrow, cupsourcebilltype, cupsourcebillid,  ";
    select = select + " cupsourcebillrowid, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5,  ";
    select = select + " vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, vdef11," +
    		" vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1," +
    		" pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8," +
    		" pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15," +
    		" pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20, crowno, norgtaxprice," +
    		" norgnettaxprice, vproducenum,ccontractid,ccontractrowid,ccontractrcode,vpriceauditcode," +
    		"cpriceauditid,cpriceaudit_bid,cpriceaudit_bb1id ,bomvers  ";//shikun bomvers

    StringBuffer strbuf = new StringBuffer();

    strbuf.append(" ,invcode,invname,invspec,invtype  ");

    strbuf.append(" ,measdo1.measname  ");

    strbuf.append(" ,measdo2.measname  ");

    strbuf.append(" ,bd_stordoc.storname  ");

    strbuf.append(" ,bd_jobbasfil.jobname  ");

    strbuf.append(" ,bd_jobphase.jobphasename  ");

    strbuf.append(" , bd_currtype.currtypename   ");

    strbuf.append(" , sc_order_b.ts");

    strbuf.append(" , sc_order_b.nbackarrvnum");

    strbuf.append(" , sc_order_b.nbackstorenum");

    String sql = select + strbuf.toString() + " from sc_order_b " + patch + " where sc_order_b.dr=0 and corderid = ? ";

    OrderItemVO[] orderItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        OrderItemVO orderItem = new OrderItemVO();

        String corder_bid = rs.getString("corder_bid");
        orderItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String corderid = rs.getString("corderid");
        orderItem.setCorderid(corderid == null ? null : corderid.trim());

        String pk_corp = rs.getString("pk_corp");
        orderItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cmangid = rs.getString("cmangid");
        orderItem.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString("cbaseid");
        orderItem.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal nordernum = rs.getBigDecimal("nordernum");
        orderItem.setNordernum(nordernum == null ? null : new UFDouble(nordernum));
        orderItem.setNoldnum(nordernum == null ? null : new UFDouble(nordernum));

        String cassistunit = rs.getString("cassistunit");
        orderItem.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = (BigDecimal)rs.getObject("nassistnum");
        orderItem.setNassistnum(nassistnum == null ? null : new UFDouble(nassistnum));

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject("ndiscountrate");
        orderItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        Integer idiscounttaxtype = (Integer)rs.getObject("idiscounttaxtype");
        orderItem.setIdiscounttaxtype(idiscounttaxtype == null ? null : idiscounttaxtype);

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        orderItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        orderItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalnetprice = (BigDecimal)rs.getObject("noriginalnetprice");
        orderItem.setNoriginalnetprice(noriginalnetprice == null ? null : new UFDouble(noriginalnetprice));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");
        orderItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");
        orderItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginaltaxmny = (BigDecimal)rs.getObject("noriginaltaxmny");
        orderItem.setNoriginaltaxmny(noriginaltaxmny == null ? null : new UFDouble(noriginaltaxmny));

        BigDecimal noriginalsummny = (BigDecimal)rs.getObject("noriginalsummny");
        orderItem.setNoriginalsummny(noriginalsummny == null ? null : new UFDouble(noriginalsummny));

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");
        orderItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        orderItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmoney = (BigDecimal)rs.getObject("nmoney");
        orderItem.setNmoney(nmoney == null ? null : new UFDouble(nmoney));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        orderItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");
        orderItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject("nassistcurmny");
        orderItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassisttaxmny = (BigDecimal)rs.getObject("nassisttaxmny");
        orderItem.setNassisttaxmny(nassisttaxmny == null ? null : new UFDouble(nassisttaxmny));

        BigDecimal nassistsummny = (BigDecimal)rs.getObject("nassistsummny");
        orderItem.setNassistsummny(nassistsummny == null ? null : new UFDouble(nassistsummny));

        BigDecimal naccumarrvnum = (BigDecimal)rs.getObject("naccumarrvnum");
        orderItem.setNaccumarrvnum(naccumarrvnum == null ? null : new UFDouble(naccumarrvnum));

        BigDecimal naccumstorenum = (BigDecimal)rs.getObject("naccumstorenum");
        orderItem.setNaccumstorenum(naccumstorenum == null ? null : new UFDouble(naccumstorenum));

        BigDecimal naccuminvoicenum = (BigDecimal)rs.getObject("naccuminvoicenum");
        orderItem.setNaccuminvoicenum(naccuminvoicenum == null ? null : new UFDouble(naccuminvoicenum));

        BigDecimal naccumwastnum = (BigDecimal)rs.getObject("naccumwastnum");
        orderItem.setNaccumwastnum(naccumwastnum == null ? null : new UFDouble(naccumwastnum));

        String dplanarrvdate = rs.getString("dplanarrvdate");
        orderItem.setDplanarrvdate(dplanarrvdate == null ? null : new UFDate(dplanarrvdate.trim(), false));

        String cwarehouseid = rs.getString("cwarehouseid");
        orderItem.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String creceiveaddress = rs.getString("creceiveaddress");
        orderItem.setCreceiveaddress(creceiveaddress == null ? null : creceiveaddress.trim());

        String cprojectid = rs.getString("cprojectid");
        orderItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        orderItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String coperator = rs.getString("coperator");
        orderItem.setCoperator(coperator == null ? null : coperator.trim());

        Integer forderrowstatus = (Integer)rs.getObject("forderrowstatus");
        orderItem.setForderrowstatus(forderrowstatus == null ? null : forderrowstatus);

        String bisactive = rs.getString("bisactive");
        if (bisactive == null)
          orderItem.setBisactive(null);
        else if (bisactive.trim().equals("1"))
          orderItem.setBisactive(new UFBoolean(false));
        else {
          orderItem.setBisactive(new UFBoolean(true));
        }

        String cordersource = rs.getString("cordersource");
        orderItem.setCordersource(cordersource == null ? null : cordersource.trim());

        String csourcebillid = rs.getString("csourcebillid");
        orderItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillrow = rs.getString("csourcebillrow");
        orderItem.setCsourcebillrow(csourcebillrow == null ? null : csourcebillrow.trim());

        String cupsourcebilltype = rs.getString("cupsourcebilltype");
        orderItem.setCupsourcebilltype(cupsourcebilltype == null ? null : cupsourcebilltype.trim());

        String cupsourcebillid = rs.getString("cupsourcebillid");
        orderItem.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillrowid = rs.getString("cupsourcebillrowid");
        orderItem.setCupsourcebillrowid(cupsourcebillrowid == null ? null : cupsourcebillrowid.trim());

        String vmemo = rs.getString("vmemo");
        orderItem.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString("vfree1");
        orderItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        orderItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        orderItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        orderItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        orderItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        orderItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        orderItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        orderItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        orderItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        orderItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        orderItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString("vdef7");
        orderItem.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString("vdef8");
        orderItem.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString("vdef9");
        orderItem.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString("vdef10");
        orderItem.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString("vdef11");
        orderItem.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString("vdef12");
        orderItem.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString("vdef13");
        orderItem.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString("vdef14");
        orderItem.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString("vdef15");
        orderItem.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString("vdef16");
        orderItem.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString("vdef17");
        orderItem.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString("vdef18");
        orderItem.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString("vdef19");
        orderItem.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString("vdef20");
        orderItem.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString("pk_defdoc1");
        orderItem.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString("pk_defdoc2");
        orderItem.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString("pk_defdoc3");
        orderItem.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString("pk_defdoc4");
        orderItem.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString("pk_defdoc5");
        orderItem.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString("pk_defdoc6");
        orderItem.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString("pk_defdoc7");
        orderItem.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString("pk_defdoc8");
        orderItem.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString("pk_defdoc9");
        orderItem.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString("pk_defdoc10");
        orderItem.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString("pk_defdoc11");
        orderItem.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString("pk_defdoc12");
        orderItem.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString("pk_defdoc13");
        orderItem.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString("pk_defdoc14");
        orderItem.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString("pk_defdoc15");
        orderItem.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString("pk_defdoc16");
        orderItem.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString("pk_defdoc17");
        orderItem.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString("pk_defdoc18");
        orderItem.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString("pk_defdoc19");
        orderItem.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString("pk_defdoc20");
        orderItem.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String crowno = rs.getString("crowno");
        orderItem.setCrowno(crowno == null ? null : crowno.trim());

        BigDecimal norgtaxprice = (BigDecimal)rs.getObject("norgtaxprice");
        orderItem.setNorgtaxprice(norgtaxprice == null ? null : new UFDouble(norgtaxprice));

        BigDecimal norgnettaxprice = (BigDecimal)rs.getObject("norgnettaxprice");
        orderItem.setNorgnettaxprice(norgnettaxprice == null ? null : new UFDouble(norgnettaxprice));

        String vproducenum = rs.getString("vproducenum");
        orderItem.setVproducenum(vproducenum == null ? null : vproducenum.trim());

        String ccontractid = rs.getString("ccontractid");
        orderItem.setCcontractid(ccontractid == null ? null : ccontractid.trim());

        String ccontractrowid = rs.getString("ccontractrowid");
        orderItem.setCcontractrowid(ccontractrowid == null ? null : ccontractrowid.trim());

        String ccontractrcode = rs.getString("ccontractrcode");
        orderItem.setCcontractrcode(ccontractrcode == null ? null : ccontractrcode.trim());

        String vpriceauditcode = rs.getString("vpriceauditcode");
        orderItem.setVpriceauditcode(vpriceauditcode == null ? null : vpriceauditcode.trim());

        String cpriceauditid = rs.getString("cpriceauditid");
        orderItem.setCpriceauditid(cpriceauditid == null ? null : cpriceauditid.trim());

        String cpriceaudit_bid = rs.getString("cpriceaudit_bid");
        orderItem.setCpriceaudit_bid(cpriceaudit_bid == null ? null : cpriceaudit_bid.trim());

        String cpriceaudit_bb1id = rs.getString("cpriceaudit_bb1id");
        orderItem.setCpriceaudit_bb1id(cpriceaudit_bb1id == null ? null : cpriceaudit_bb1id.trim());

        String bomvers = rs.getString("bomvers");//shikun bomvers
        orderItem.setBomvers(bomvers == null ? null : bomvers.toString().trim());

        String invcode = rs.getString("invcode");
        orderItem.setVinvcode(invcode == null ? null : invcode.trim());

        String invname = rs.getString("invname");
        orderItem.setVinvname(invname == null ? null : invname.trim());

        String invspec = rs.getString("invspec");
        orderItem.setVinvspec(invspec == null ? null : invspec.trim());

        String invtype = rs.getString("invtype");
        orderItem.setVinvtype(invtype == null ? null : invtype.trim());

        int index = 106;//shikun add bomvers 105
        String measdocname = rs.getString(index++);
        orderItem.setVmeasdocname(measdocname == null ? null : measdocname.trim());

        String assunitname = rs.getString(index++);
        orderItem.setVassunitname(assunitname == null ? null : assunitname.trim());

        String storename = rs.getString(index++);
        orderItem.setVwarehousename(storename == null ? null : storename.trim());

        String projectname = rs.getString(index++);
        orderItem.setVprojectname(projectname == null ? null : projectname.trim());

        String projectphasename = rs.getString(index++);
        orderItem.setVprojectphasename(projectphasename == null ? null : projectphasename.trim());

        String currencyname = rs.getString(index++);
        orderItem.setVcurrencyname(currencyname == null ? null : currencyname.trim());

        String ts = rs.getString(index++);
        orderItem.setTs(ts == null ? null : ts.trim());

        BigDecimal nbackarrvnum = (BigDecimal)rs.getObject(index++);
        orderItem.setNbackarrvnum(nbackarrvnum == null ? null : new UFDouble(nbackarrvnum));

        BigDecimal nbackstorenum = (BigDecimal)rs.getObject(index++);
        orderItem.setNbackstorenum(nbackstorenum == null ? null : new UFDouble(nbackstorenum));

        v.addElement(orderItem);
      }
    } finally {
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
    orderItems = new OrderItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(orderItems);
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findItemsAllForHeader", new Object[] { key });

    return orderItems;
  }

  public OrderItemVO[] findItemsForHeader(String key, String whereString)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findItemsForHeader", new Object[] { key });

    StringBuffer strbuf = new StringBuffer();
    strbuf.append(" select sc_order_b.corder_bid, sc_order_b.corderid, sc_order_b.pk_corp, sc_order_b.cmangid, sc_order_b.cbaseid, sc_order_b.nordernum,\n ");
    strbuf.append(" sc_order_b.cassistunit, sc_order_b.nassistnum, sc_order_b.ndiscountrate, sc_order_b.idiscounttaxtype, sc_order_b.ntaxrate, sc_order_b.\n ");
    strbuf.append(" ccurrencytypeid, sc_order_b.noriginalnetprice, sc_order_b.noriginalcurprice, sc_order_b.noriginalcurmny, sc_order_b.\n ");
    strbuf.append(" noriginaltaxmny, sc_order_b.noriginalsummny, sc_order_b.nexchangeotobrate, sc_order_b.ntaxmny, sc_order_b.nmoney, sc_order_b.\n ");
    strbuf.append(" nsummny, sc_order_b.nexchangeotoarate, sc_order_b.nassistcurmny, sc_order_b.nassisttaxmny, sc_order_b.nassistsummny, sc_order_b.\n ");
    strbuf.append(" naccumarrvnum, sc_order_b.naccumstorenum, sc_order_b.naccuminvoicenum, sc_order_b.naccumwastnum, sc_order_b.\n ");
    strbuf.append(" dplanarrvdate, sc_order_b.cwarehouseid, sc_order_b.creceiveaddress, sc_order_b.cprojectid, sc_order_b.\n ");
    strbuf.append(" cprojectphaseid, sc_order_b.coperator, sc_order_b.forderrowstatus, sc_order_b.bisactive, sc_order_b.cordersource, sc_order_b.\n ");
    strbuf.append(" csourcebillid, sc_order_b.csourcebillrow, sc_order_b.cupsourcebilltype, sc_order_b.cupsourcebillid, sc_order_b.\n ");
    strbuf.append(" cupsourcebillrowid, sc_order_b.vmemo, sc_order_b.\n ");
    strbuf.append(" vfree1, sc_order_b.vfree2, sc_order_b.vfree3, sc_order_b.vfree4, sc_order_b.vfree5, sc_order_b.\n ");
    strbuf.append(" vdef1, sc_order_b.vdef2, sc_order_b.vdef3, sc_order_b.vdef4, sc_order_b.vdef5, sc_order_b.vdef6," +
    		" sc_order_b.vdef7, sc_order_b.vdef8, sc_order_b.vdef9, sc_order_b.vdef10, sc_order_b.vdef11," +
    		" sc_order_b.vdef12, sc_order_b.vdef13, sc_order_b.vdef14, sc_order_b.vdef15, sc_order_b.vdef16, sc_order_b.vdef17," +
    		" sc_order_b.vdef18, sc_order_b.vdef19, sc_order_b.vdef20, sc_order_b.pk_defdoc1, sc_order_b.pk_defdoc2, sc_order_b.pk_defdoc3," +
    		" sc_order_b.pk_defdoc4, sc_order_b.pk_defdoc5, sc_order_b.pk_defdoc6, sc_order_b.pk_defdoc7, sc_order_b.pk_defdoc8, " +
    		"sc_order_b.pk_defdoc9, sc_order_b.pk_defdoc10, sc_order_b.pk_defdoc11, sc_order_b.pk_defdoc12, sc_order_b.pk_defdoc13," +
    		" sc_order_b.pk_defdoc14, sc_order_b.pk_defdoc15, sc_order_b.pk_defdoc16, sc_order_b.pk_defdoc17, sc_order_b.pk_defdoc18," +
    		" sc_order_b.pk_defdoc19, sc_order_b.pk_defdoc20 , sc_order_b.ts, sc_order_b.crowno, sc_order_b.norgtaxprice, sc_order_b.norgnettaxprice," +
    		" sc_order_b.nbackarrvnum,sc_order_b.nbackstorenum, sc_order_b.vproducenum,sc_order_b.ccontractid,sc_order_b.ccontractrowid," +
    		"sc_order_b.ccontractrcode,sc_order_b.vpriceauditcode,sc_order_b.cpriceauditid,sc_order_b.cpriceaudit_bid," +
    		"sc_order_b.cpriceaudit_bb1id,sc_order_b.bomvers \n ");//shikun bomvers
    strbuf.append(" from sc_order_b  \n ");

    String patch = " inner join sc_order on sc_order.corderid = sc_order_b.corderid  \n";
    patch = patch + " left join bd_cubasdoc on sc_order.cvendorid=bd_cubasdoc.pk_cubasdoc \n";
    patch = patch + " left join bd_psndoc on sc_order.cemployeeid=bd_psndoc.pk_psndoc \n";
    patch = patch + " left join bd_deptdoc on sc_order.cdeptid=bd_deptdoc.pk_deptdoc \n";
    patch = patch + " left join bd_invbasdoc on bd_invbasdoc.pk_invbasdoc = sc_order_b.cbaseid \n";

    strbuf.append(patch);
    strbuf.append(" where sc_order_b.bisactive = '0' and sc_order_b.dr=0 \n ");
    strbuf.append("  and sc_order_b.corderid = ?  \n ");

    if ((whereString != null) && (!whereString.trim().equals("")))
      strbuf.append(" and " + whereString + " \n ");
    String sql = strbuf.toString();

    OrderItemVO[] orderItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        OrderItemVO orderItem = new OrderItemVO();

        String corder_bid = rs.getString("corder_bid");
        orderItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String corderid = rs.getString("corderid");
        orderItem.setCorderid(corderid == null ? null : corderid.trim());

        String pk_corp = rs.getString("pk_corp");
        orderItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cmangid = rs.getString("cmangid");
        orderItem.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString("cbaseid");
        orderItem.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal nordernum = rs.getBigDecimal("nordernum");
        orderItem.setNordernum(nordernum == null ? null : new UFDouble(nordernum));
        orderItem.setNoldnum(nordernum == null ? null : new UFDouble(nordernum));

        String cassistunit = rs.getString("cassistunit");
        orderItem.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = rs.getBigDecimal("nassistnum");
        orderItem.setNassistnum(nassistnum == null ? null : new UFDouble(nassistnum));

        BigDecimal ndiscountrate = rs.getBigDecimal("ndiscountrate");
        orderItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        Integer idiscounttaxtype = (Integer)rs.getObject("idiscounttaxtype");
        orderItem.setIdiscounttaxtype(idiscounttaxtype == null ? null : idiscounttaxtype);

        BigDecimal ntaxrate = rs.getBigDecimal("ntaxrate");
        orderItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        orderItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalnetprice = rs.getBigDecimal("noriginalnetprice");
        orderItem.setNoriginalnetprice(noriginalnetprice == null ? null : new UFDouble(noriginalnetprice));

        BigDecimal noriginalcurprice = rs.getBigDecimal("noriginalcurprice");
        orderItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurmny = rs.getBigDecimal("noriginalcurmny");
        orderItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginaltaxmny = rs.getBigDecimal("noriginaltaxmny");
        orderItem.setNoriginaltaxmny(noriginaltaxmny == null ? null : new UFDouble(noriginaltaxmny));

        BigDecimal noriginalsummny = rs.getBigDecimal("noriginalsummny");
        orderItem.setNoriginalsummny(noriginalsummny == null ? null : new UFDouble(noriginalsummny));

        BigDecimal nexchangeotobrate = rs.getBigDecimal("nexchangeotobrate");
        orderItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal ntaxmny = rs.getBigDecimal("ntaxmny");
        orderItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmoney = rs.getBigDecimal("nmoney");
        orderItem.setNmoney(nmoney == null ? null : new UFDouble(nmoney));

        BigDecimal nsummny = rs.getBigDecimal("nsummny");
        orderItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nexchangeotoarate = rs.getBigDecimal("nexchangeotoarate");
        orderItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nassistcurmny = rs.getBigDecimal("nassistcurmny");
        orderItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassisttaxmny = rs.getBigDecimal("nassisttaxmny");
        orderItem.setNassisttaxmny(nassisttaxmny == null ? null : new UFDouble(nassisttaxmny));

        BigDecimal nassistsummny = rs.getBigDecimal("nassistsummny");
        orderItem.setNassistsummny(nassistsummny == null ? null : new UFDouble(nassistsummny));

        BigDecimal naccumarrvnum = rs.getBigDecimal("naccumarrvnum");
        orderItem.setNaccumarrvnum(naccumarrvnum == null ? null : new UFDouble(naccumarrvnum));

        BigDecimal naccumstorenum = rs.getBigDecimal("naccumstorenum");
        orderItem.setNaccumstorenum(naccumstorenum == null ? null : new UFDouble(naccumstorenum));

        BigDecimal naccuminvoicenum = rs.getBigDecimal("naccuminvoicenum");
        orderItem.setNaccuminvoicenum(naccuminvoicenum == null ? null : new UFDouble(naccuminvoicenum));

        BigDecimal naccumwastnum = rs.getBigDecimal("naccumwastnum");
        orderItem.setNaccumwastnum(naccumwastnum == null ? null : new UFDouble(naccumwastnum));

        String dplanarrvdate = rs.getString("dplanarrvdate");
        orderItem.setDplanarrvdate(dplanarrvdate == null ? null : new UFDate(dplanarrvdate.trim(), false));

        String cwarehouseid = rs.getString("cwarehouseid");
        orderItem.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String creceiveaddress = rs.getString("creceiveaddress");
        orderItem.setCreceiveaddress(creceiveaddress == null ? null : creceiveaddress.trim());

        String cprojectid = rs.getString("cprojectid");
        orderItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        orderItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String coperator = rs.getString("coperator");
        orderItem.setCoperator(coperator == null ? null : coperator.trim());

        Integer forderrowstatus = (Integer)rs.getObject("forderrowstatus");
        orderItem.setForderrowstatus(forderrowstatus == null ? null : forderrowstatus);

        String bisactive = rs.getString("bisactive");
        if (bisactive == null)
          orderItem.setBisactive(null);
        else if (bisactive.trim().equals("1"))
          orderItem.setBisactive(new UFBoolean(false));
        else {
          orderItem.setBisactive(new UFBoolean(true));
        }

        String cordersource = rs.getString("cordersource");
        orderItem.setCordersource(cordersource == null ? null : cordersource.trim());

        String csourcebillid = rs.getString("csourcebillid");
        orderItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillrow = rs.getString("csourcebillrow");
        orderItem.setCsourcebillrow(csourcebillrow == null ? null : csourcebillrow.trim());

        String cupsourcebilltype = rs.getString("cupsourcebilltype");
        orderItem.setCupsourcebilltype(cupsourcebilltype == null ? null : cupsourcebilltype.trim());

        String cupsourcebillid = rs.getString("cupsourcebillid");
        orderItem.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillrowid = rs.getString("cupsourcebillrowid");
        orderItem.setCupsourcebillrowid(cupsourcebillrowid == null ? null : cupsourcebillrowid.trim());

        String vmemo = rs.getString("vmemo");
        orderItem.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString("vfree1");
        orderItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        orderItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        orderItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        orderItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        orderItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        orderItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        orderItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        orderItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        orderItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        orderItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        orderItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString("vdef7");
        orderItem.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString("vdef8");
        orderItem.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString("vdef9");
        orderItem.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString("vdef10");
        orderItem.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString("vdef11");
        orderItem.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString("vdef12");
        orderItem.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString("vdef13");
        orderItem.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString("vdef14");
        orderItem.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString("vdef15");
        orderItem.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString("vdef16");
        orderItem.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString("vdef17");
        orderItem.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString("vdef18");
        orderItem.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString("vdef19");
        orderItem.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString("vdef20");
        orderItem.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString("pk_defdoc1");
        orderItem.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString("pk_defdoc2");
        orderItem.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString("pk_defdoc3");
        orderItem.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString("pk_defdoc4");
        orderItem.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString("pk_defdoc5");
        orderItem.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString("pk_defdoc6");
        orderItem.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString("pk_defdoc7");
        orderItem.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString("pk_defdoc8");
        orderItem.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString("pk_defdoc9");
        orderItem.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString("pk_defdoc10");
        orderItem.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString("pk_defdoc11");
        orderItem.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString("pk_defdoc12");
        orderItem.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString("pk_defdoc13");
        orderItem.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString("pk_defdoc14");
        orderItem.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString("pk_defdoc15");
        orderItem.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString("pk_defdoc16");
        orderItem.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString("pk_defdoc17");
        orderItem.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString("pk_defdoc18");
        orderItem.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString("pk_defdoc19");
        orderItem.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString("pk_defdoc20");
        orderItem.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String ts = rs.getString("ts");
        orderItem.setTs(ts == null ? null : ts.trim());
        String crowno = rs.getString("crowno");
        orderItem.setCrowno(crowno == null ? null : crowno.trim());

        BigDecimal norgtaxprice = rs.getBigDecimal("norgtaxprice");
        orderItem.setNorgtaxprice(norgtaxprice == null ? null : new UFDouble(norgtaxprice));

        BigDecimal norgnettaxprice = rs.getBigDecimal("norgnettaxprice");
        orderItem.setNorgnettaxprice(norgnettaxprice == null ? null : new UFDouble(norgnettaxprice));

        BigDecimal nbackarrvnum = rs.getBigDecimal("nbackarrvnum");
        orderItem.setNbackarrvnum(nbackarrvnum == null ? null : new UFDouble(nbackarrvnum));

        BigDecimal nbackstorenum = rs.getBigDecimal("nbackstorenum");
        orderItem.setNbackstorenum(nbackstorenum == null ? null : new UFDouble(nbackstorenum));

        String vproducenum = rs.getString("vproducenum");
        orderItem.setVproducenum(vproducenum == null ? null : vproducenum.trim());

        String ccontractid = rs.getString("ccontractid");
        orderItem.setCcontractid(ccontractid == null ? null : ccontractid.trim());

        String ccontractrowid = rs.getString("ccontractrowid");
        orderItem.setCcontractrowid(ccontractrowid == null ? null : ccontractrowid.trim());

        String ccontractrcode = rs.getString("ccontractrcode");
        orderItem.setCcontractrcode(ccontractrcode == null ? null : ccontractrcode.trim());

        String vpriceauditcode = rs.getString("vpriceauditcode");
        orderItem.setVpriceauditcode(vpriceauditcode == null ? null : vpriceauditcode.trim());

        String cpriceauditid = rs.getString("cpriceauditid");
        orderItem.setCpriceauditid(cpriceauditid == null ? null : cpriceauditid.trim());

        String cpriceaudit_bid = rs.getString("cpriceaudit_bid");
        orderItem.setCpriceaudit_bid(cpriceaudit_bid == null ? null : cpriceaudit_bid.trim());

        String cpriceaudit_bb1id = rs.getString("cpriceaudit_bb1id");
        orderItem.setCpriceaudit_bb1id(cpriceaudit_bb1id == null ? null : cpriceaudit_bb1id.trim());

        String bomvers = rs.getString("bomvers");
        orderItem.setBomvers(bomvers == null ? null : bomvers.trim());

        v.addElement(orderItem);
      }
    } finally {
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
    orderItems = new OrderItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(orderItems);
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findItemsForHeader", new Object[] { key });

    return orderItems;
  }

  public OrderBbVO findMateItemByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderBbDMO", "findMateItemByPrimaryKey", new Object[] { key });

    String sql = "select corder_bbid, corderid, corder_bid, pk_corp, cmangid, cbaseid, nmaterialnum, cassistunit, nassistnum, nprice, nmoney, ddeliverydate, ntotalnum, cdeliverywarehouse, vdeliveryaddress, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6  from sc_order_bb where  corder_bbid = ?";

    OrderBbVO orderBb = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        orderBb = new OrderBbVO(key);

        String corder_bbid = rs.getString(1);
        orderBb.setCorder_bbid(corder_bbid == null ? null : corder_bbid.trim());

        String corderid = rs.getString(2);
        orderBb.setCorderid(corderid == null ? null : corderid.trim());

        String corder_bid = rs.getString(3);
        orderBb.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String pk_corp = rs.getString(4);
        orderBb.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cmangid = rs.getString(5);
        orderBb.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString(6);
        orderBb.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal nmaterialnum = rs.getBigDecimal(7);
        orderBb.setNmaterialnum(nmaterialnum == null ? null : new UFDouble(nmaterialnum));

        String cassistunit = rs.getString(8);
        orderBb.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = rs.getBigDecimal(9);
        orderBb.setNassistnum(nassistnum == null ? null : new UFDouble(nassistnum));

        BigDecimal nprice = rs.getBigDecimal(10);
        orderBb.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal nmoney = rs.getBigDecimal(11);
        orderBb.setNmoney(nmoney == null ? null : new UFDouble(nmoney));

        String ddeliverydate = rs.getString(12);
        orderBb.setDdeliverydate(ddeliverydate == null ? null : new UFDate(ddeliverydate.trim(), false));

        BigDecimal ntotalnum = rs.getBigDecimal(13);
        orderBb.setNtotalnum(ntotalnum == null ? null : new UFDouble(ntotalnum));

        String cdeliverywarehouse = rs.getString(14);
        orderBb.setCdeliverywarehouse(cdeliverywarehouse == null ? null : cdeliverywarehouse.trim());

        String vdeliveryaddress = rs.getString(15);
        orderBb.setVdeliveryaddress(vdeliveryaddress == null ? null : vdeliveryaddress.trim());

        String vmemo = rs.getString(16);
        orderBb.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString(17);
        orderBb.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(18);
        orderBb.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(19);
        orderBb.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(20);
        orderBb.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(21);
        orderBb.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString(22);
        orderBb.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(23);
        orderBb.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(24);
        orderBb.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(25);
        orderBb.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(26);
        orderBb.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(27);
        orderBb.setVdef6(vdef6 == null ? null : vdef6.trim());
      }
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderBbDMO", "findMateItemByPrimaryKey", new Object[] { key });

    return orderBb;
  }

  public OrderBbVO[] findMateItemForPreItem(String headKey, String itemKey)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderBbDMO", "findMateItemByPrimaryKey", new Object[] { headKey, itemKey });

    String sql = "select corder_bbid, corderid, corder_bid, pk_corp, cmangid, cbaseid, nmaterialnum, cassistunit, nassistnum, nprice, nmoney, ddeliverydate, ntotalnum, cdeliverywarehouse, vdeliveryaddress, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6   from sc_order_bb  ";
    sql = sql + " where dr=0 ";
    if (headKey != null) {
      sql = sql + "and corderid=? ";
    }

    if (itemKey != null) {
      sql = sql + " and corder_bid = ?";
    }
    OrderBbVO[] orderBbs = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (headKey != null) {
        stmt.setString(1, headKey);
        if (itemKey != null)
          stmt.setString(2, itemKey);
      } else if (itemKey != null) {
        stmt.setString(1, itemKey);
      }
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        OrderBbVO orderBb = new OrderBbVO();

        String corder_bbid = rs.getString(1);
        orderBb.setCorder_bbid(corder_bbid == null ? null : corder_bbid.trim());

        String corderid = rs.getString(2);
        orderBb.setCorderid(corderid == null ? null : corderid.trim());

        String corder_bid = rs.getString(3);
        orderBb.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String pk_corp = rs.getString(4);
        orderBb.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cmangid = rs.getString(5);
        orderBb.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString(6);
        orderBb.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal nmaterialnum = rs.getBigDecimal(7);
        orderBb.setNmaterialnum(nmaterialnum == null ? null : new UFDouble(nmaterialnum));

        String cassistunit = rs.getString(8);
        orderBb.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = rs.getBigDecimal(9);
        orderBb.setNassistnum(nassistnum == null ? null : new UFDouble(nassistnum));

        BigDecimal nprice = rs.getBigDecimal(10);
        orderBb.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal nmoney = rs.getBigDecimal(11);
        orderBb.setNmoney(nmoney == null ? null : new UFDouble(nmoney));

        String ddeliverydate = rs.getString(12);
        orderBb.setDdeliverydate(ddeliverydate == null ? null : new UFDate(ddeliverydate.trim(), false));

        BigDecimal ntotalnum = rs.getBigDecimal(13);
        orderBb.setNtotalnum(ntotalnum == null ? null : new UFDouble(ntotalnum));

        String cdeliverywarehouse = rs.getString(14);
        orderBb.setCdeliverywarehouse(cdeliverywarehouse == null ? null : cdeliverywarehouse.trim());

        String vdeliveryaddress = rs.getString(15);
        orderBb.setVdeliveryaddress(vdeliveryaddress == null ? null : vdeliveryaddress.trim());

        String vmemo = rs.getString(16);
        orderBb.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString(17);
        orderBb.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(18);
        orderBb.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(19);
        orderBb.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(20);
        orderBb.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(21);
        orderBb.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString(22);
        orderBb.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(23);
        orderBb.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(24);
        orderBb.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(25);
        orderBb.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(26);
        orderBb.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(27);
        orderBb.setVdef6(vdef6 == null ? null : vdef6.trim());

        v.addElement(orderBb);
      }
    } finally {
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
    orderBbs = new OrderBbVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(orderBbs);
    }

    afterCallMethod("nc.bs.sc.order.OrderBbDMO", "findMateItemByPrimaryKey", new Object[] { headKey, itemKey });

    return orderBbs;
  }

  public String findNextArrBill(String corderid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findNextArrBill", new Object[] { corderid });

    String sql = "select carriveorder_bid from po_arriveorder_b where dr=0 and csourcebilltype ='61' and csourcebillid = ? ";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, corderid);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String str1 = rs.getString(1);
        return str1;
      }
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findNextArrBill", new Object[] { corderid });

    return null;
  }

  public String findNextBill(String corderid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findNextBill", new Object[] { corderid });

    String sql = "select vordercode  from sc_order where vordercode = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, corderid);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        rs.getString(1);
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findNextBill", new Object[] { corderid });

    return null;
  }

  public String findNextInvoiceBill(String corderid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findNextInvoiceBill", new Object[] { corderid });

    String sql = "select cinvoice_bid from po_invoice_b where dr=0 and csourcebilltype ='61' and csourcebillid = ? ";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, corderid);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String str1 = rs.getString(1);
        return str1;
      }
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findNextInvoiceBill", new Object[] { corderid });

    return null;
  }

  public String findNextWareBill(String corderid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findNextBill", new Object[] { corderid });

    String sql = "select cgeneralbid from ic_general_b where dr=0 and csourcetype ='61' and csourcebillhid = ? ";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, corderid);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String str1 = rs.getString(1);
        return str1;
      }
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findNextBill", new Object[] { corderid });

    return null;
  }

  public OrderVO[] findVOsBack(OrderHeaderVO[] headers)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "findItemsForHeader", new Object[] { headers });

    StringBuffer sb = new StringBuffer("select corder_bid, corderid, pk_corp, cmangid, cbaseid, nordernum,");
    sb.append("cassistunit, nassistnum, ndiscountrate, idiscounttaxtype, ntaxrate,");
    sb.append("ccurrencytypeid, noriginalnetprice, noriginalcurprice, noriginalcurmny,");
    sb.append("noriginaltaxmny, noriginalsummny, nexchangeotobrate, ntaxmny, nmoney,");
    sb.append("nsummny, nexchangeotoarate, nassistcurmny, nassisttaxmny, nassistsummny,");
    sb.append("naccumarrvnum, naccumstorenum, naccuminvoicenum, naccumwastnum,");
    sb.append("dplanarrvdate, cwarehouseid, creceiveaddress, cprojectid, ");
    sb.append("cprojectphaseid, coperator, forderrowstatus, bisactive, cordersource,");
    sb.append("csourcebillid, csourcebillrow, cupsourcebilltype, cupsourcebillid,");
    sb.append("cupsourcebillrowid, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5,");
    sb.append("vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13," +
    		" vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4," +
    		" pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12," +
    		" pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20," +
    		" ts, crowno, norgtaxprice, norgnettaxprice, vproducenum,ccontractid,ccontractrowid,ccontractrcode,vpriceauditcode," +
    		"cpriceauditid,cpriceaudit_bid,cpriceaudit_bb1id,bomvers,");//shikun bomvers
    sb.append("nbackarrvnum,nbackstorenum ");
    sb.append("from sc_order_b where dr=0 ");
    sb.append("and (1<0 ");
    for (int i = 0; i < headers.length; i++) {
      if (headers[i].getPrimaryKey() == null)
        continue;
      sb.append("or corderid ='");
      sb.append(headers[i].getPrimaryKey());
      sb.append("' ");
    }
    sb.append(") ");
    OrderVO[] rsltVos = null;
    OrderItemVO orderItem = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sb.toString());
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        orderItem = new OrderItemVO();

        String corder_bid = rs.getString("corder_bid");
        orderItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String corderid = rs.getString("corderid");
        orderItem.setCorderid(corderid == null ? null : corderid.trim());

        String pk_corp = rs.getString("pk_corp");
        orderItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cmangid = rs.getString("cmangid");
        orderItem.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString("cbaseid");
        orderItem.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal nordernum = rs.getBigDecimal("nordernum");
        orderItem.setNordernum(nordernum == null ? null : new UFDouble(nordernum));
        orderItem.setNoldnum(nordernum == null ? null : new UFDouble(nordernum));

        String cassistunit = rs.getString("cassistunit");
        orderItem.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = rs.getBigDecimal("nassistnum");
        orderItem.setNassistnum(nassistnum == null ? null : new UFDouble(nassistnum));

        BigDecimal ndiscountrate = rs.getBigDecimal("ndiscountrate");
        orderItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        Integer idiscounttaxtype = (Integer)rs.getObject("idiscounttaxtype");
        orderItem.setIdiscounttaxtype(idiscounttaxtype == null ? null : idiscounttaxtype);

        BigDecimal ntaxrate = rs.getBigDecimal("ntaxrate");
        orderItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        orderItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalnetprice = rs.getBigDecimal("noriginalnetprice");
        orderItem.setNoriginalnetprice(noriginalnetprice == null ? null : new UFDouble(noriginalnetprice));

        BigDecimal noriginalcurprice = rs.getBigDecimal("noriginalcurprice");
        orderItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurmny = rs.getBigDecimal("noriginalcurmny");
        orderItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginaltaxmny = rs.getBigDecimal("noriginaltaxmny");
        orderItem.setNoriginaltaxmny(noriginaltaxmny == null ? null : new UFDouble(noriginaltaxmny));

        BigDecimal noriginalsummny = rs.getBigDecimal("noriginalsummny");
        orderItem.setNoriginalsummny(noriginalsummny == null ? null : new UFDouble(noriginalsummny));

        BigDecimal nexchangeotobrate = rs.getBigDecimal("nexchangeotobrate");
        orderItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal ntaxmny = rs.getBigDecimal("ntaxmny");
        orderItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmoney = rs.getBigDecimal("nmoney");
        orderItem.setNmoney(nmoney == null ? null : new UFDouble(nmoney));

        BigDecimal nsummny = rs.getBigDecimal("nsummny");
        orderItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nexchangeotoarate = rs.getBigDecimal("nexchangeotoarate");
        orderItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nassistcurmny = rs.getBigDecimal("nassistcurmny");
        orderItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassisttaxmny = rs.getBigDecimal("nassisttaxmny");
        orderItem.setNassisttaxmny(nassisttaxmny == null ? null : new UFDouble(nassisttaxmny));

        BigDecimal nassistsummny = rs.getBigDecimal("nassistsummny");
        orderItem.setNassistsummny(nassistsummny == null ? null : new UFDouble(nassistsummny));

        BigDecimal naccumarrvnum = rs.getBigDecimal("naccumarrvnum");
        orderItem.setNaccumarrvnum(naccumarrvnum == null ? null : new UFDouble(naccumarrvnum));

        BigDecimal naccumstorenum = rs.getBigDecimal("naccumstorenum");
        orderItem.setNaccumstorenum(naccumstorenum == null ? null : new UFDouble(naccumstorenum));

        BigDecimal naccuminvoicenum = rs.getBigDecimal("naccuminvoicenum");
        orderItem.setNaccuminvoicenum(naccuminvoicenum == null ? null : new UFDouble(naccuminvoicenum));

        BigDecimal naccumwastnum = rs.getBigDecimal("naccumwastnum");
        orderItem.setNaccumwastnum(naccumwastnum == null ? null : new UFDouble(naccumwastnum));

        String dplanarrvdate = rs.getString("dplanarrvdate");
        orderItem.setDplanarrvdate(dplanarrvdate == null ? null : new UFDate(dplanarrvdate.trim(), false));

        String cwarehouseid = rs.getString("cwarehouseid");
        orderItem.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String creceiveaddress = rs.getString("creceiveaddress");
        orderItem.setCreceiveaddress(creceiveaddress == null ? null : creceiveaddress.trim());

        String cprojectid = rs.getString("cprojectid");
        orderItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        orderItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String coperator = rs.getString("coperator");
        orderItem.setCoperator(coperator == null ? null : coperator.trim());

        Integer forderrowstatus = (Integer)rs.getObject("forderrowstatus");
        orderItem.setForderrowstatus(forderrowstatus == null ? null : forderrowstatus);

        String bisactive = rs.getString("bisactive");
        if (bisactive == null)
          orderItem.setBisactive(null);
        else if (bisactive.trim().equals("1"))
          orderItem.setBisactive(new UFBoolean(false));
        else {
          orderItem.setBisactive(new UFBoolean(true));
        }

        String cordersource = rs.getString("cordersource");
        orderItem.setCordersource(cordersource == null ? null : cordersource.trim());

        String csourcebillid = rs.getString("csourcebillid");
        orderItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillrow = rs.getString("csourcebillrow");
        orderItem.setCsourcebillrow(csourcebillrow == null ? null : csourcebillrow.trim());

        String cupsourcebilltype = rs.getString("cupsourcebilltype");
        orderItem.setCupsourcebilltype(cupsourcebilltype == null ? null : cupsourcebilltype.trim());

        String cupsourcebillid = rs.getString("cupsourcebillid");
        orderItem.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillrowid = rs.getString("cupsourcebillrowid");
        orderItem.setCupsourcebillrowid(cupsourcebillrowid == null ? null : cupsourcebillrowid.trim());

        String vmemo = rs.getString("vmemo");
        orderItem.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString("vfree1");
        orderItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        orderItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        orderItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        orderItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        orderItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        orderItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        orderItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        orderItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        orderItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        orderItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        orderItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString("vdef7");
        orderItem.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString("vdef8");
        orderItem.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString("vdef9");
        orderItem.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString("vdef10");
        orderItem.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString("vdef11");
        orderItem.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString("vdef12");
        orderItem.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString("vdef13");
        orderItem.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString("vdef14");
        orderItem.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString("vdef15");
        orderItem.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString("vdef16");
        orderItem.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString("vdef17");
        orderItem.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString("vdef18");
        orderItem.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString("vdef19");
        orderItem.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString("vdef20");
        orderItem.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString("pk_defdoc1");
        orderItem.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString("pk_defdoc2");
        orderItem.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString("pk_defdoc3");
        orderItem.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString("pk_defdoc4");
        orderItem.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString("pk_defdoc5");
        orderItem.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString("pk_defdoc6");
        orderItem.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString("pk_defdoc7");
        orderItem.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString("pk_defdoc8");
        orderItem.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString("pk_defdoc9");
        orderItem.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString("pk_defdoc10");
        orderItem.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString("pk_defdoc11");
        orderItem.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString("pk_defdoc12");
        orderItem.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString("pk_defdoc13");
        orderItem.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString("pk_defdoc14");
        orderItem.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString("pk_defdoc15");
        orderItem.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString("pk_defdoc16");
        orderItem.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString("pk_defdoc17");
        orderItem.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString("pk_defdoc18");
        orderItem.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString("pk_defdoc19");
        orderItem.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString("pk_defdoc20");
        orderItem.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String ts = rs.getString("ts");
        orderItem.setTs(ts == null ? null : ts.trim());

        String crowno = rs.getString("crowno");
        orderItem.setCrowno(crowno == null ? null : crowno.trim());

        BigDecimal norgtaxprice = rs.getBigDecimal("norgtaxprice");
        orderItem.setNorgtaxprice(norgtaxprice == null ? null : new UFDouble(norgtaxprice));

        BigDecimal norgnettaxprice = rs.getBigDecimal("norgnettaxprice");
        orderItem.setNorgnettaxprice(norgnettaxprice == null ? null : new UFDouble(norgnettaxprice));

        String vproducenum = rs.getString("vproducenum");
        orderItem.setVproducenum(vproducenum == null ? null : vproducenum.trim());

        String ccontractid = rs.getString("ccontractid");
        orderItem.setCcontractid(ccontractid == null ? null : ccontractid.trim());

        String ccontractrowid = rs.getString("ccontractrowid");
        orderItem.setCcontractrowid(ccontractrowid == null ? null : ccontractrowid.trim());

        String ccontractrcode = rs.getString("ccontractrcode");
        orderItem.setCcontractrcode(ccontractrcode == null ? null : ccontractrcode.trim());

        String vpriceauditcode = rs.getString("vpriceauditcode");
        orderItem.setVpriceauditcode(vpriceauditcode == null ? null : vpriceauditcode.trim());

        String cpriceauditid = rs.getString("cpriceauditid");
        orderItem.setCpriceauditid(cpriceauditid == null ? null : cpriceauditid.trim());

        String cpriceaudit_bid = rs.getString("cpriceaudit_bid");
        orderItem.setCpriceaudit_bid(cpriceaudit_bid == null ? null : cpriceaudit_bid.trim());

        String cpriceaudit_bb1id = rs.getString("cpriceaudit_bb1id");
        orderItem.setCpriceaudit_bb1id(cpriceaudit_bb1id == null ? null : cpriceaudit_bb1id.trim());

        String bomvers = rs.getString("bomvers");//shikun bomvers
        orderItem.setBomvers(bomvers == null ? null : bomvers.trim());

        BigDecimal nbackarrvnum = rs.getBigDecimal("nbackarrvnum");
        orderItem.setNbackarrvnum(nbackarrvnum == null ? null : new UFDouble(nbackarrvnum));

        BigDecimal nbackstorenum = rs.getBigDecimal("nbackstorenum");
        orderItem.setNbackstorenum(nbackstorenum == null ? null : new UFDouble(nbackstorenum));

        v.addElement(orderItem);
      }
    } finally {
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
    if (v.size() > 0) {
      OrderItemVO[] orderItems = new OrderItemVO[v.size()];
      v.copyInto(orderItems);
      Hashtable hTmp = PublicDMO.getHashBodyByHeadKey(orderItems, "corderid");
      rsltVos = new OrderVO[headers.length];
      for (int i = 0; i < headers.length; i++) {
        rsltVos[i] = new OrderVO();
        rsltVos[i].setParentVO(headers[i]);
        if (headers[i].getPrimaryKey() == null)
          continue;
        rsltVos[i].setChildrenVO((OrderItemVO[])(OrderItemVO[])hTmp.get(headers[i].getPrimaryKey()));
      }

    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "findItemsForHeader", new Object[] { headers });

    return rsltVos;
  }

  public String getOrderState(String pos, String id)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "isAuditState", new Object[] { pos, id });

    if (id == null) {
      return null;
    }
    String sql = "";
    if ((pos == null) || (pos.trim().equals("")))
      sql = "select ibillstatus from sc_order where  corderid = ? and dr=0  ";
    else {
      sql = " select ibillstatus from sc_order left join sc_order_b on sc_order.corderid = sc_order_b.corderid where sc_order_b.corder_bid = ?  ";
    }
    Integer ibillstatus = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        ibillstatus = (Integer)rs.getObject(1);
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "isAuditState", new Object[] { pos, id });

    if (ibillstatus == null) {
      return null;
    }
    return ibillstatus.toString();
  }

  public String getPk_measdoc(String cbaseid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "getPk_measdoc", new Object[] { cbaseid });

    if (cbaseid == null) {
      return "";
    }
    String sql = "select pk_measdoc from bd_invbasdoc where  pk_invbasdoc = ? ";

    String pk_measdoc = "";
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, cbaseid);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
        pk_measdoc = rs.getString(1);
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "getPk_measdoc", new Object[] { cbaseid });

    return pk_measdoc;
  }

  public Object[] getPk_produce(String pk_corp, String cbaseid, String pk_calbody)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "getPk_produce", new Object[] { pk_corp, cbaseid });

    if ((pk_corp == null) || (cbaseid == null)) {
      return new String[2];
    }
    String sql = "select pk_produce ,prevahead from bd_produce where pk_corp = ?  and pk_invbasdoc = ? and pk_calbody =? ";

    Object[] produceInfo = new Object[2];
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, pk_corp);
      stmt.setString(2, cbaseid);
      stmt.setString(3, pk_calbody);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        produceInfo[0] = rs.getString(1);
        produceInfo[1] = rs.getObject(2);
      }
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "getPk_produce", new Object[] { pk_corp, cbaseid });

    return produceInfo;
  }

  public String getPrayState(String pos, String id)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "getPrayState", new Object[] { pos, id });

    if (id == null) {
      return null;
    }
    String sql = "";

    sql = "select ibillstatus from po_praybill where  cpraybillid = ?  and dr = 0 ";

    Integer ibillstatus = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        ibillstatus = (Integer)rs.getObject(1);
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "getPrayState", new Object[] { pos, id });

    if (ibillstatus == null) {
      return null;
    }
    return ibillstatus.toString();
  }

  public String[] getSubInvClassCode(String cInvClassCode, String sOpera)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ps.cost.CostanalyseDMO", "getSubInvClassCode", new Object[] { cInvClassCode, sOpera });

    String sql = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String[] fatherCode = null;
    try {
      con = getConnection();

      if (sOpera.toLowerCase().trim().equals("like")) {
        sql = "select invclasscode from bd_invcl where invclasscode like '%" + cInvClassCode + "%'";
        stmt = con.prepareStatement(sql);
        rs = stmt.executeQuery();

        while (rs.next()) {
          String s = rs.getString(1);
          if ((s != null) && (s.trim().length() > 0))
            v.addElement(s);
        }
        if (stmt != null)
          stmt.close();
        if (v.size() > 0) {
          fatherCode = new String[v.size()];
          v.copyInto(fatherCode);
        }
      } else {
        fatherCode = new String[1];
        fatherCode[0] = cInvClassCode;
      }

      v = new Vector();
      sql = "select invclasscode from bd_invcl where invclasslev >= (select invclasslev from bd_invcl where invclasscode = ? )";
      stmt = con.prepareStatement(sql);

      for (int i = 0; i < fatherCode.length; i++) {
        stmt.setString(1, fatherCode[i]);
        rs = stmt.executeQuery();

        while (rs.next()) {
          String s = rs.getString(1);
          if ((s != null) && (s.trim().length() > 0)) {
            int index = s.indexOf(fatherCode[i]);
            if (index >= 0)
              v.addElement(s);
          }
        }
        if (rs != null)
          rs.close();
      }
    } finally {
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

    afterCallMethod("nc.bs.ps.cost.CostanalyseDMO", "getSubInvClassCode", new Object[] { cInvClassCode, sOpera });

    if (v.size() > 0)
    {
      Vector vTemp = new Vector();
      vTemp.addElement(v.elementAt(0));
      for (int i = 1; i < v.size(); i++) {
        String s = (String)v.elementAt(i);
        if (!vTemp.contains(s))
          vTemp.addElement(s);
      }
      String[] s = new String[vTemp.size()];
      vTemp.copyInto(s);
      return s;
    }

    return null;
  }

  public OrderItemVO[] getUnionItem(OrderItemVO[] item)
    throws SQLException
  {
    Vector v = new Vector();
    for (int i = 0; i < item.length; i++) {
      item[i].setCinvshow(ScConstants.PROCESSFLAG);

      v.addElement(item[i]);
    }

    OrderItemVO[] unionItem = new OrderItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(unionItem);
    }
    return unionItem;
  }

  public String getUserName(String pk_corp, String coperatorid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "getUserName", new Object[] { coperatorid });

    String sql = "select user_name from  sm_user where   cuserid =?  ";

    String userName = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, coperatorid);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        userName = rs.getString(1);
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "getUserName", new Object[] { coperatorid });

    return userName;
  }

  public String getVerifyRule(String cbiztype)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "getVerifyRule", new Object[] { cbiztype });

    String sql = "select verifyrule from bd_busitype where pk_busitype = ?  ";

    String verifyRule = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, cbiztype);
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        verifyRule = rs.getString(1);
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "getVerifyRule", new Object[] { cbiztype });

    return verifyRule;
  }

  public OrderItemVO[] getVOsWithRate(OrderItemVO[] items)
    throws SQLException
  {
    if ((items == null) || (items.length <= 0)) {
      SCMEnv.out("传入参数为空，直接返回");
      return items;
    }

    String sassistunit = null; String baseid = null; String fixed = null;
    Object[][] obj = (Object[][])null;
    UFDouble ufdMainNum = null; UFDouble ufdAssNum = null; UFDouble ufdRate = null;
    try
    {
      ScmPubDMO pubDmo = new ScmPubDMO();
      int iLen = items.length;
      for (int i = 0; i < iLen; i++) {
        if (items[i] == null)
          continue;
        sassistunit = (String)items[i].getAttributeValue("cassistunit");
        baseid = (String)items[i].getAttributeValue("cbaseid");
        if ((sassistunit == null) || (sassistunit.trim().equals("")) || (baseid == null)) {
          items[i].setAttributeValue("measrate", null);
        }
        else {
          obj = pubDmo.queryResultsFromAnyTable("bd_convert", new String[] { "fixedflag", "mainmeasrate", "pk_measdoc" }, " pk_measdoc = '" + sassistunit + "' and pk_invbasdoc = '" + baseid + "'");
          if ((obj != null) && (obj[0] != null) && (obj[0].length == 3)) {
            fixed = obj[0][0].toString();
            if ("Y".equalsIgnoreCase(fixed)) {
              ufdRate = new UFDouble(obj[0][1].toString());
            } else {
              if ((items[i].getAttributeValue("nassistnum") != null) && (items[i].getAttributeValue("nassistnum").toString().trim().length() > 0)) {
                ufdAssNum = new UFDouble(items[i].getAttributeValue("nassistnum").toString());
              }
              if (ufdAssNum.equals(new UFDouble(0.0D))) {
                SCMEnv.out("非固定换算率时 辅数量 出现零情况,越过");
                continue;
              }
              if ((items[i].getAttributeValue("nordernum") != null) && (items[i].getAttributeValue("nordernum").toString().trim().length() > 0)) {
                ufdMainNum = new UFDouble(items[i].getAttributeValue("nordernum").toString());
                ufdRate = ufdMainNum.div(ufdAssNum);
              } else {
                SCMEnv.out("主数量出现零情况,换算率处理成零");
                ufdRate = new UFDouble(0.0D);
              }
            }
            items[i].setAttributeValue("measrate", ufdRate);
          }
        }
      }
    } catch (Exception e) {
      SCMEnv.out("设置委外订单表体换算率出错，明细如下：");
      SCMEnv.out(e);
      throw new SQLException(e.getMessage());
    }
    return items;
  }

  public AggregatedValueObject[] getWarehouseFromProduce(AggregatedValueObject[] superVO)
    throws BusinessException
  {
    OrderVO[] orderVO = (OrderVO[])(OrderVO[])superVO;
    if ((orderVO == null) || (orderVO.length == 0))
      return orderVO;
    String pk_corp = ((OrderHeaderVO)orderVO[0].getParentVO()).getPk_corp();

    Vector vTemp1 = new Vector();
    Vector vTemp2 = new Vector();
    Vector vTemp = new Vector();
    for (int i = 0; i < orderVO.length; i++) {
      OrderItemVO[] bodyVO = (OrderItemVO[])(OrderItemVO[])orderVO[i].getChildrenVO();
      if ((bodyVO == null) || (bodyVO.length == 0))
        continue;
      String cstoreorganization = ((OrderHeaderVO)orderVO[0].getParentVO()).getCwareid();
      for (int j = 0; j < bodyVO.length; j++) {
        String cwarehouseid = bodyVO[j].getCwarehouseid();
        if ((cwarehouseid != null) && (cwarehouseid.trim().length() > 0))
          continue;
        String cbaseid = bodyVO[j].getCbaseid();
        if (!vTemp.contains(cstoreorganization + cbaseid)) {
          vTemp.addElement(cstoreorganization + cbaseid);
          vTemp1.addElement(cstoreorganization);
          vTemp2.addElement(cbaseid);
        }
      }
    }
    if (vTemp.size() == 0) {
      return orderVO;
    }

    String sql = "select pk_calbody, pk_invbasdoc, pk_stordoc from bd_produce where dr = 0 and pk_corp = '" + pk_corp + "' and (";
    for (int i = 0; i < vTemp.size() - 1; i++)
      sql = sql + "(pk_calbody = '" + vTemp1.elementAt(i) + "' and pk_invbasdoc = '" + vTemp2.elementAt(i) + "') or ";
    sql = sql + "(pk_calbody = '" + vTemp1.elementAt(vTemp.size() - 1) + "' and pk_invbasdoc = '" + vTemp2.elementAt(vTemp.size() - 1) + "'))";

    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable t = new Hashtable();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String s1 = rs.getString(1);
        String s2 = rs.getString(2);
        String s3 = rs.getString(3);
        if ((s1 != null) && (s2 != null) && (s3 != null)) {
          t.put(s1 + s2, s3);
        }
      }
      if (rs != null)
        rs.close();
    }
    catch (SQLException e) {
      SCMEnv.out(e);
    } finally {
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
    for (int i = 0; i < orderVO.length; i++) {
      OrderItemVO[] bodyVO = (OrderItemVO[])(OrderItemVO[])orderVO[i].getChildrenVO();
      if ((bodyVO == null) || (bodyVO.length == 0))
        continue;
      String cstoreorganization = ((OrderHeaderVO)orderVO[0].getParentVO()).getCwareid();
      for (int j = 0; j < bodyVO.length; j++) {
        String cwarehouseid = bodyVO[j].getCwarehouseid();
        if ((cwarehouseid != null) && (cwarehouseid.trim().length() > 0))
          continue;
        String cbaseid = bodyVO[j].getCbaseid();
        Object o = t.get(cstoreorganization + cbaseid);
        if (o != null) {
          bodyVO[j].setCwarehouseid(o.toString());
        }
      }
    }
    return orderVO;
  }

  public OrderVO insert(OrderVO vo)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "insert", new Object[] { vo });

    OrderVO returnVO = null;
    try
    {
      BatchCodeDMO batchCodeDmo = new BatchCodeDMO();
      HashMap hBatchCodes = batchCodeDmo.getBatchCode(vo);
      OrderItemVO[] batchItems = (OrderItemVO[])(OrderItemVO[])vo.getChildrenVO();
      if ((hBatchCodes != null) && (hBatchCodes.size() > 0)) {
        for (int i = 0; i < batchItems.length; i++) {
          if ((batchItems[i].getVproducenum() == null) || (batchItems[i].getVproducenum().trim().length() == 0)) {
            batchItems[i].setVproducenum((String)hBatchCodes.get(batchItems[i].getCmangid()));
          }
        }
      }
      String key = null;

      new GetSysBillCode().setBillNoWhenModify(vo, vo.getM_voOld(), "vordercode");

      key = insertHeader((OrderHeaderVO)vo.getParentVO());

      vo.getParentVO().setPrimaryKey(key);

      OrderItemVO[] items = (OrderItemVO[])(OrderItemVO[])vo.getChildrenVO();

      for (int i = 0; i < items.length; i++) {
        items[i].setCorderid(key);
      }
      insertItems(items);

      if ((key != null) && (key.trim().length() > 0))
        returnVO = findByPrimaryKey(key);
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "insert", new Object[] { vo });

    return returnVO;
  }

  public String insertMaterialItem(OrderBbVO orderBb)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.order.OrderBbDMO", "insert", new Object[] { orderBb });

    String sql = "insert into sc_order_bb(corder_bbid, corderid, corder_bid, pk_corp, cmangid, cbaseid, nmaterialnum, cassistunit, nassistnum, nprice, nmoney, ddeliverydate, ntotalnum, cdeliverywarehouse, vdeliveryaddress, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    String pk_corp = orderBb.getPk_corp();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      key = getOID(pk_corp);
      stmt.setString(1, key);

      if (orderBb.getCorderid() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, orderBb.getCorderid());
      }
      if (orderBb.getCorder_bid() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, orderBb.getCorder_bid());
      }
      if (orderBb.getPk_corp() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, orderBb.getPk_corp());
      }
      if (orderBb.getCmangid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, orderBb.getCmangid());
      }
      if (orderBb.getCbaseid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, orderBb.getCbaseid());
      }
      if (orderBb.getNmaterialnum() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, orderBb.getNmaterialnum().toBigDecimal());
      }
      if (orderBb.getCassistunit() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, orderBb.getCassistunit());
      }
      if (orderBb.getNassistnum() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setBigDecimal(9, orderBb.getNassistnum().toBigDecimal());
      }
      if (orderBb.getNprice() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setBigDecimal(10, orderBb.getNprice().toBigDecimal());
      }
      if (orderBb.getNmoney() == null)
        stmt.setNull(11, 4);
      else {
        stmt.setBigDecimal(11, orderBb.getNmoney().toBigDecimal());
      }
      if (orderBb.getDdeliverydate() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, orderBb.getDdeliverydate().toString());
      }
      if (orderBb.getNtotalnum() == null)
        stmt.setNull(13, 4);
      else {
        stmt.setBigDecimal(13, orderBb.getNtotalnum().toBigDecimal());
      }
      if (orderBb.getCdeliverywarehouse() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, orderBb.getCdeliverywarehouse());
      }
      if (orderBb.getVdeliveryaddress() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, orderBb.getVdeliveryaddress());
      }
      if (orderBb.getVmemo() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, orderBb.getVmemo());
      }
      if (orderBb.getVfree1() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, orderBb.getVfree1());
      }
      if (orderBb.getVfree2() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, orderBb.getVfree2());
      }
      if (orderBb.getVfree3() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, orderBb.getVfree3());
      }
      if (orderBb.getVfree4() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, orderBb.getVfree4());
      }
      if (orderBb.getVfree5() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, orderBb.getVfree5());
      }
      if (orderBb.getVdef1() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, orderBb.getVdef1());
      }
      if (orderBb.getVdef2() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, orderBb.getVdef2());
      }
      if (orderBb.getVdef3() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, orderBb.getVdef3());
      }
      if (orderBb.getVdef4() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, orderBb.getVdef4());
      }
      if (orderBb.getVdef5() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, orderBb.getVdef5());
      }
      if (orderBb.getVdef6() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, orderBb.getVdef6());
      }

      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderBbDMO", "insert", new Object[] { orderBb });

    return key;
  }

  public String insertMaterialItem(OrderBbVO orderBb, String corderid, String corder_bid)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "insertMaterialItem", new Object[] { orderBb, corderid, corder_bid });

    orderBb.setCorderid(corderid);
    orderBb.setCorder_bid(corder_bid);
    String key = insertMaterialItem(orderBb);

    afterCallMethod("nc.bs.sc.order.OrderDMO", "insertMaterialItem", new Object[] { orderBb, corderid, corder_bid });

    return key;
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
    throws BusinessException
  {
    OrderItemVO[] items = null;
    try
    {
      items = findItemsForHeader(key.substring(0, 20), key.substring(20, key.length()));

      items = getVOsWithRate(items);
    } catch (SQLException ex) {
      SCMEnv.out("委外实现平台 IQueryData2 接口方法出错");
      ex.printStackTrace();
      throw new BusinessException(ex.getMessage());
    }
    return items;
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key, String whereString)
    throws BusinessException
  {
    OrderItemVO[] items = null;
    try
    {
      items = findItemsForHeader(key, whereString);

      items = getVOsWithRate(items);
    } catch (SQLException ex) {
      SCMEnv.out("委外实现平台 IQueryData2 接口方法出错");
      ex.printStackTrace();
      throw new BusinessException(ex.getMessage());
    }
    return items;
  }

  public OrderHeaderVO[] queryAllHead(String unitCode)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "queryAllHead", new Object[] { unitCode });

    String sql = "";
    if (unitCode != null)
      sql = "select corderid, vordercode, pk_corp, cpurorganization, cwareid, dorderdate, cvendorid, caccountbankid, cdeptid, cemployeeid, cbiztype, creciever, cgiveinvoicevendor, ctransmodeid, ctermProtocolid, ibillstatus, vmemo, caccountyear, coperator, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20, ts, iprintcount, tmaketime, taudittime, tlastmaketime from sc_order where pk_corp = ? and dr = 0 ";
    else {
      sql = "select corderid, vordercode, pk_corp, cpurorganization, cwareid, dorderdate, cvendorid, caccountbankid, cdeptid, cemployeeid, cbiztype, creciever, cgiveinvoicevendor, ctransmodeid, ctermProtocolid, ibillstatus, vmemo, caccountyear, coperator, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20, ts, iprintcount, tmaketime, taudittime, tlastmaketime from sc_order where dr=0 ";
    }

    OrderHeaderVO[] orders = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (unitCode != null) {
        stmt.setString(1, unitCode);
      }
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        OrderHeaderVO order = new OrderHeaderVO();

        String corderid = rs.getString(1);
        order.setCorderid(corderid == null ? null : corderid.trim());

        String vordercode = rs.getString(2);
        order.setVordercode(vordercode == null ? null : vordercode.trim());

        String pk_corp = rs.getString(3);
        order.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cpurorganization = rs.getString(4);
        order.setCpurorganization(cpurorganization == null ? null : cpurorganization.trim());

        String cwareid = rs.getString(5);
        order.setCwareid(cwareid == null ? null : cwareid.trim());

        String dorderdate = rs.getString(6);
        order.setDorderdate(dorderdate == null ? null : new UFDate(dorderdate.trim(), false));

        String cvendorid = rs.getString(7);
        order.setCvendorid(cvendorid == null ? null : cvendorid.trim());

        String caccountbankid = rs.getString(8);
        order.setCaccountbankid(caccountbankid == null ? null : caccountbankid.trim());

        String cdeptid = rs.getString(9);
        order.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(10);
        order.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String cbiztypeid = rs.getString(11);
        order.setCbiztype(cbiztypeid == null ? null : cbiztypeid.trim());

        String creciever = rs.getString(12);
        order.setCreciever(creciever == null ? null : creciever.trim());

        String cgiveinvoicevendor = rs.getString(13);
        order.setCgiveinvoicevendor(cgiveinvoicevendor == null ? null : cgiveinvoicevendor.trim());

        String ctransmodeid = rs.getString(14);
        order.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        String ctermProtocolid = rs.getString(15);
        order.setCtermProtocolid(ctermProtocolid == null ? null : ctermProtocolid.trim());

        Integer ibillstatus = (Integer)rs.getObject(16);
        order.setIbillstatus(ibillstatus == null ? null : ibillstatus);

        String vmemo = rs.getString(17);
        order.setVmemo(vmemo == null ? null : vmemo.trim());

        String caccountyear = rs.getString(18);
        order.setCaccountyear(caccountyear == null ? null : caccountyear.trim());

        String coperator = rs.getString(19);
        order.setCoperator(coperator == null ? null : coperator.trim());

        String vdef1 = rs.getString(20);
        order.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(21);
        order.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(22);
        order.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(23);
        order.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(24);
        order.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(25);
        order.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(26);
        order.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(27);
        order.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(28);
        order.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(29);
        order.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString(30);
        order.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString(31);
        order.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString(32);
        order.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString(33);
        order.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString(34);
        order.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString(35);
        order.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString(36);
        order.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString(37);
        order.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString(38);
        order.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString(39);
        order.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString(40);
        order.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(41);
        order.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(42);
        order.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(43);
        order.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(44);
        order.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(45);
        order.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(46);
        order.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(47);
        order.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(48);
        order.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(49);
        order.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(50);
        order.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(51);
        order.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(52);
        order.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(53);
        order.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(54);
        order.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(55);
        order.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(56);
        order.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(57);
        order.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(58);
        order.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(59);
        order.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String ts = rs.getString(60);
        order.setTs(ts == null ? null : ts.trim());

        Integer iprintcount = (Integer)rs.getObject(61);
        order.setIprintcount(iprintcount == null ? null : iprintcount);

        String tmaketime = rs.getString(62);
        order.setTmaketime(tmaketime == null ? null : tmaketime.trim());
        String taudittime = rs.getString(63);
        order.setTaudittime(taudittime == null ? null : taudittime.trim());
        String tlastmaketime = rs.getString(64);
        order.setTlastmaketime(tlastmaketime == null ? null : tlastmaketime.trim());

        v.addElement(order);
      }
    } finally {
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
    orders = new OrderHeaderVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(orders);
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "queryAllHead", new Object[] { unitCode });

    return orders;
  }

  public OrderHeaderVO[] queryAllHead(String auditCondition, String condition)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "queryAllHead", new Object[] { auditCondition, condition });

    String patch = "";

    patch = patch + " left join bd_busitype on sc_order.cbiztype= bd_busitype.pk_busitype   ";

    patch = patch + " left join bd_calbody on sc_order.cwareid = bd_calbody.pk_calbody   ";

    patch = patch + " left join bd_purorg on sc_order.cpurorganization=bd_purorg.pk_purorg   ";

    patch = patch + " left join bd_cubasdoc on sc_order.cvendorid=bd_cubasdoc.pk_cubasdoc   ";

    patch = patch + " left join bd_deptdoc on sc_order.cdeptid=bd_deptdoc.pk_deptdoc   ";

    patch = patch + " left join bd_psndoc on sc_order.cemployeeid=bd_psndoc.pk_psndoc   ";

    patch = patch + " left join bd_cumandoc cumandoc1 on sc_order.creciever=cumandoc1.pk_cumandoc  ";
    patch = patch + " left join bd_cubasdoc cubasdoc1 on cumandoc1.pk_cubasdoc=cubasdoc1.pk_cubasdoc  ";

    patch = patch + " left join bd_cumandoc cumandoc2 on sc_order.cgiveinvoicevendor=cumandoc2.pk_cumandoc  ";
    patch = patch + " left join bd_cubasdoc cubasdoc2 on cumandoc2.pk_cubasdoc=cubasdoc2.pk_cubasdoc  ";

    patch = patch + " left join bd_custbank on sc_order.caccountbankid = bd_custbank.pk_custbank  ";

    patch = patch + " left join bd_sendtype on sc_order.ctransmodeid =bd_sendtype.pk_sendtype  ";

    patch = patch + " left join bd_payterm on sc_order.ctermProtocolid =bd_payterm.pk_payterm  ";

    patch = patch + " left join sm_user sm_user1 on sc_order.coperator = sm_user1.cuserid ";

    patch = patch + " left join sm_user sm_user2 on sc_order.cauditpsn = sm_user2.cuserid ";

    patch = patch + " left join bd_corp on sc_order.pk_corp = bd_corp.pk_corp ";

    patch = patch + " left join sc_order_b on sc_order.corderid = sc_order_b.corderid  ";

    patch = patch + " left join bd_invbasdoc on bd_invbasdoc.pk_invbasdoc = sc_order_b.cbaseid ";

    patch = patch + " left join bd_invcl on bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl ";

    StringBuffer strbuf = new StringBuffer();
    strbuf.append(" \n select distinct sc_order.corderid, vordercode, sc_order.pk_corp, cpurorganization, ");
    strbuf.append(" cwareid, dorderdate, cvendorid, caccountbankid, cdeptid, cemployeeid, ");
    strbuf.append(" cbiztype, creciever, cgiveinvoicevendor, ctransmodeid, ctermProtocolid,");
    strbuf.append(" ibillstatus, sc_order.vmemo, caccountyear, sc_order.coperator, ");
    strbuf.append(" sc_order.vdef1, sc_order.vdef2, sc_order.vdef3, ");
    strbuf.append(" sc_order.vdef4, sc_order.vdef5, sc_order.vdef6, ");
    strbuf.append(" sc_order.vdef7, sc_order.vdef8, sc_order.vdef9, sc_order.vdef10, sc_order.vdef11, sc_order.vdef12, sc_order.vdef13, sc_order.vdef14, sc_order.vdef15, sc_order.vdef16, sc_order.vdef17, sc_order.vdef18, sc_order.vdef19, sc_order.vdef20, sc_order.pk_defdoc1, sc_order.pk_defdoc2, sc_order.pk_defdoc3, sc_order.pk_defdoc4, sc_order.pk_defdoc5, sc_order.pk_defdoc6, sc_order.pk_defdoc7, sc_order.pk_defdoc8, sc_order.pk_defdoc9, sc_order.pk_defdoc10, sc_order.pk_defdoc11, sc_order.pk_defdoc12, sc_order.pk_defdoc13, sc_order.pk_defdoc14, sc_order.pk_defdoc15, sc_order.pk_defdoc16, sc_order.pk_defdoc17, sc_order.pk_defdoc18, sc_order.pk_defdoc19, sc_order.pk_defdoc20 ,");
    strbuf.append(" cvendormangid ,cauditpsn, dauditdate, sc_order.ts, sc_order.iprintcount,sc_order.tmaketime,sc_order.taudittime,sc_order.tlastmaketime   ");

    strbuf.append(" ,bd_busitype.businame  ");

    strbuf.append(" ,bd_calbody.bodyname  ");

    strbuf.append(" ,bd_purorg.name  ");

    strbuf.append(" ,bd_cubasdoc.custname ");

    strbuf.append(" ,bd_deptdoc.deptname   ");

    strbuf.append(" ,bd_psndoc.psnname  ");

    strbuf.append(" ,cubasdoc2.custname  ");

    strbuf.append(" ,cubasdoc1.custname ");

    strbuf.append(" ,bd_custbank.accname  ");

    strbuf.append(" ,bd_sendtype.sendname ");

    strbuf.append(" ,bd_payterm.termname  ");

    strbuf.append(" ,sm_user1.user_name, sm_user2.user_name  ");

    strbuf.append(" ,bd_corp.unitname ");

    strbuf.append(" from  ");
    strbuf.append(" sc_order ");

    strbuf.append(patch);

    String sql = strbuf.toString();

    sql = sql + " where sc_order.dr=0 and sc_order_b.dr=0 ";

    if (auditCondition.equals("1"))
      sql = sql + " and ibillstatus = 3 ";
    else if (auditCondition.equals("2"))
      sql = sql + " and (ibillstatus = 0 or ibillstatus=2) ";
    else {
      auditCondition = null;
    }
    if (condition != null) {
      sql = sql + " and " + condition;
    }
    OrderHeaderVO[] orders = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        OrderHeaderVO order = new OrderHeaderVO();

        String corderid = rs.getString(1);
        order.setCorderid(corderid == null ? null : corderid.trim());

        String vordercode = rs.getString(2);
        order.setVordercode(vordercode == null ? null : vordercode.trim());

        String pk_corp = rs.getString(3);
        order.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cpurorganization = rs.getString(4);
        order.setCpurorganization(cpurorganization == null ? null : cpurorganization.trim());

        String cwareid = rs.getString(5);
        order.setCwareid(cwareid == null ? null : cwareid.trim());

        String dorderdate = rs.getString(6);
        order.setDorderdate(dorderdate == null ? null : new UFDate(dorderdate.trim(), false));

        String cvendorid = rs.getString(7);
        order.setCvendorid(cvendorid == null ? null : cvendorid.trim());

        String caccountbankid = rs.getString(8);
        order.setCaccountbankid(caccountbankid == null ? null : caccountbankid.trim());

        String cdeptid = rs.getString(9);
        order.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(10);
        order.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String cbiztypeid = rs.getString(11);
        order.setCbiztype(cbiztypeid == null ? null : cbiztypeid.trim());

        String creciever = rs.getString(12);
        order.setCreciever(creciever == null ? null : creciever.trim());

        String cgiveinvoicevendor = rs.getString(13);
        order.setCgiveinvoicevendor(cgiveinvoicevendor == null ? null : cgiveinvoicevendor.trim());

        String ctransmodeid = rs.getString(14);
        order.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        String ctermProtocolid = rs.getString(15);
        order.setCtermProtocolid(ctermProtocolid == null ? null : ctermProtocolid.trim());

        Integer ibillstatus = (Integer)rs.getObject(16);
        order.setIbillstatus(ibillstatus == null ? null : ibillstatus);

        String vmemo = rs.getString(17);
        order.setVmemo(vmemo == null ? null : vmemo.trim());

        String caccountyear = rs.getString(18);
        order.setCaccountyear(caccountyear == null ? null : caccountyear.trim());

        String coperator = rs.getString(19);
        order.setCoperator(coperator == null ? null : coperator.trim());

        String vdef1 = rs.getString(20);
        order.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(21);
        order.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(22);
        order.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(23);
        order.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(24);
        order.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(25);
        order.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(26);
        order.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(27);
        order.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(28);
        order.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(29);
        order.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString(30);
        order.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString(31);
        order.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString(32);
        order.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString(33);
        order.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString(34);
        order.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString(35);
        order.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString(36);
        order.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString(37);
        order.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString(38);
        order.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString(39);
        order.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString(40);
        order.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(41);
        order.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(42);
        order.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(43);
        order.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(44);
        order.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(45);
        order.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(46);
        order.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(47);
        order.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(48);
        order.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(49);
        order.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(50);
        order.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(51);
        order.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(52);
        order.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(53);
        order.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(54);
        order.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(55);
        order.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(56);
        order.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(57);
        order.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(58);
        order.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(59);
        order.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String cvendormangid = rs.getString(60);
        order.setCvendormangid(cvendormangid == null ? null : cvendormangid.trim());

        String cauditpsn = rs.getString(61);
        order.setCauditpsn(cauditpsn == null ? null : cauditpsn.trim());

        String dauditdate = rs.getString(62);
        order.setDauditdate(dauditdate == null ? null : new UFDate(dauditdate.trim(), false));

        String ts = rs.getString(63);
        order.setTs(ts == null ? null : ts.trim());

        Integer iprintcount = (Integer)rs.getObject(64);
        order.setIprintcount(iprintcount == null ? null : iprintcount);

        String tmaketime = rs.getString(65);
        order.setTmaketime(tmaketime == null ? null : tmaketime.trim());
        String taudittime = rs.getString(66);
        order.setTaudittime(taudittime == null ? null : taudittime.trim());
        String tlastmaketime = rs.getString(67);
        order.setTlastmaketime(tlastmaketime == null ? null : tlastmaketime.trim());

        int index = 68;

        String biztypename = rs.getString(index++);
        order.setVbiztypename(biztypename == null ? null : biztypename.trim());

        String calbodyname = rs.getString(index++);
        order.setVcalbodyname(calbodyname == null ? null : calbodyname.trim());

        String purname = rs.getString(index++);
        order.setVpurname(purname == null ? null : purname.trim());

        String custname = rs.getString(index++);
        order.setVcustname(custname == null ? null : custname.trim());

        String deptname = rs.getString(index++);
        order.setVdeptname(deptname == null ? null : deptname.trim());

        String employeename = rs.getString(index++);
        order.setVemployeename(employeename == null ? null : employeename.trim());

        String giveinvoicename = rs.getString(index++);
        order.setVgiveinvoicename(giveinvoicename == null ? null : giveinvoicename.trim());

        String receivename = rs.getString(index++);
        order.setVreceivename(receivename == null ? null : receivename.trim());

        String bankname = rs.getString(index++);
        order.setVbankname(bankname == null ? null : bankname.trim());

        String transmodename = rs.getString(index++);
        order.setVtransmodename(transmodename == null ? null : transmodename.trim());

        String protocolname = rs.getString(index++);
        order.setVprotocolname(protocolname == null ? null : protocolname.trim());

        String operatorname = rs.getString(index++);
        order.setVoperatorname(operatorname == null ? null : operatorname.trim());

        String auditpsnname = rs.getString(index++);
        order.setVauditpsnname(auditpsnname == null ? null : auditpsnname.trim());

        v.addElement(order);
      }
    } finally {
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
    orders = new OrderHeaderVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(orders);
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "queryAllHead", new Object[] { auditCondition, condition });

    return orders;
  }

  public OrderHeaderVO[] queryAllHead(String auditCondition, String condition, String usedByRef)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "queryAllHead", new Object[] { auditCondition, condition });

    String key = "corderid";
    if ((condition != null) && (!condition.trim().equals("")) && (condition.indexOf(key) >= 0)) {
      String mid = condition;
      condition = "";
      int iIndexCarriveorderid = mid.indexOf(key);
      condition = mid.substring(0, iIndexCarriveorderid) + " ";
      condition = condition + " sc_order." + key + " ";
      condition = condition + mid.substring(iIndexCarriveorderid + key.length(), mid.length()) + " ";
    }

    String patch = " left join bd_cubasdoc on sc_order.cvendorid=bd_cubasdoc.pk_cubasdoc \n";
    patch = patch + " left join bd_psndoc on sc_order.cemployeeid=bd_psndoc.pk_psndoc \n";
    patch = patch + " left join bd_deptdoc on sc_order.cdeptid=bd_deptdoc.pk_deptdoc \n";
    patch = patch + " left join sc_order_b on sc_order.corderid = sc_order_b.corderid  \n";
    patch = patch + " left join bd_invbasdoc on bd_invbasdoc.pk_invbasdoc = sc_order_b.cbaseid \n";

    StringBuffer strbuf = new StringBuffer();
    strbuf.append(" \n select distinct sc_order.corderid, vordercode, sc_order.pk_corp, cpurorganization, \n");
    strbuf.append(" cwareid, dorderdate, cvendorid, caccountbankid, cdeptid, cemployeeid, \n");
    strbuf.append(" cbiztype, creciever, cgiveinvoicevendor, ctransmodeid, ctermProtocolid,\n");
    strbuf.append(" ibillstatus, sc_order.vmemo, caccountyear, sc_order.coperator, \n");
    strbuf.append(" sc_order.vdef1, sc_order.vdef2, sc_order.vdef3, \n");
    strbuf.append(" sc_order.vdef4, sc_order.vdef5, sc_order.vdef6, \n");
    strbuf.append(" sc_order.vdef7, sc_order.vdef8, sc_order.vdef9, sc_order.vdef10 ,\n");
    strbuf.append(" sc_order.vdef11, sc_order.vdef12, sc_order.vdef13, sc_order.vdef14, sc_order.vdef15, sc_order.vdef16, sc_order.vdef17, sc_order.vdef18, sc_order.vdef19, sc_order.vdef20, sc_order.pk_defdoc1, sc_order.pk_defdoc2, sc_order.pk_defdoc3, sc_order.pk_defdoc4, sc_order.pk_defdoc5, sc_order.pk_defdoc6, sc_order.pk_defdoc7, sc_order.pk_defdoc8, sc_order.pk_defdoc9, sc_order.pk_defdoc10, sc_order.pk_defdoc11, sc_order.pk_defdoc12, sc_order.pk_defdoc13, sc_order.pk_defdoc14, sc_order.pk_defdoc15, sc_order.pk_defdoc16, sc_order.pk_defdoc17, sc_order.pk_defdoc18, sc_order.pk_defdoc19, sc_order.pk_defdoc20 ,\n");

    strbuf.append(" cvendormangid ,cauditpsn, dauditdate, sc_order.ts, sc_order.iprintcount,sc_order.tmaketime,sc_order.taudittime,sc_order.tlastmaketime  \n ");

    strbuf.append(" from  \n");
    strbuf.append(" sc_order \n");

    strbuf.append(patch);

    String sql = strbuf.toString();

    sql = sql + " where sc_order_b.bisactive='0'  and sc_order.dr=0 ";

    if (auditCondition.equals("1"))
      sql = sql + " and ibillstatus = 3 ";
    else if (auditCondition.equals("2"))
      sql = sql + " and (ibillstatus = 0 or ibillstatus=4) ";
    else {
      auditCondition = null;
    }
    if (condition != null) {
      sql = sql + " and " + condition;
    }
    OrderHeaderVO[] orders = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      SCMEnv.out(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        OrderHeaderVO order = new OrderHeaderVO();

        String corderid = rs.getString(1);
        order.setCorderid(corderid == null ? null : corderid.trim());

        String vordercode = rs.getString(2);
        order.setVordercode(vordercode == null ? null : vordercode.trim());

        String pk_corp = rs.getString(3);
        order.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cpurorganization = rs.getString(4);
        order.setCpurorganization(cpurorganization == null ? null : cpurorganization.trim());

        String cwareid = rs.getString(5);
        order.setCwareid(cwareid == null ? null : cwareid.trim());

        String dorderdate = rs.getString(6);
        order.setDorderdate(dorderdate == null ? null : new UFDate(dorderdate.trim(), false));

        String cvendorid = rs.getString(7);
        order.setCvendorid(cvendorid == null ? null : cvendorid.trim());

        String caccountbankid = rs.getString(8);
        order.setCaccountbankid(caccountbankid == null ? null : caccountbankid.trim());

        String cdeptid = rs.getString(9);
        order.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(10);
        order.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String cbiztypeid = rs.getString(11);
        order.setCbiztype(cbiztypeid == null ? null : cbiztypeid.trim());

        String creciever = rs.getString(12);
        order.setCreciever(creciever == null ? null : creciever.trim());

        String cgiveinvoicevendor = rs.getString(13);
        order.setCgiveinvoicevendor(cgiveinvoicevendor == null ? null : cgiveinvoicevendor.trim());

        String ctransmodeid = rs.getString(14);
        order.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        String ctermProtocolid = rs.getString(15);
        order.setCtermProtocolid(ctermProtocolid == null ? null : ctermProtocolid.trim());

        Integer ibillstatus = (Integer)rs.getObject(16);
        order.setIbillstatus(ibillstatus == null ? null : ibillstatus);

        String vmemo = rs.getString(17);
        order.setVmemo(vmemo == null ? null : vmemo.trim());

        String caccountyear = rs.getString(18);
        order.setCaccountyear(caccountyear == null ? null : caccountyear.trim());

        String coperator = rs.getString(19);
        order.setCoperator(coperator == null ? null : coperator.trim());

        String vdef1 = rs.getString(20);
        order.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(21);
        order.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(22);
        order.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(23);
        order.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(24);
        order.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(25);
        order.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(26);
        order.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(27);
        order.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(28);
        order.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(29);
        order.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString(30);
        order.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString(31);
        order.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString(32);
        order.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString(33);
        order.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString(34);
        order.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString(35);
        order.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString(36);
        order.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString(37);
        order.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString(38);
        order.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString(39);
        order.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString(40);
        order.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(41);
        order.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(42);
        order.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(43);
        order.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(44);
        order.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(45);
        order.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(46);
        order.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(47);
        order.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(48);
        order.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(49);
        order.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(50);
        order.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(51);
        order.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(52);
        order.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(53);
        order.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(54);
        order.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(55);
        order.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(56);
        order.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(57);
        order.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(58);
        order.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(59);
        order.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String cvendormangid = rs.getString(60);
        order.setCvendormangid(cvendormangid == null ? null : cvendormangid.trim());

        String cauditpsn = rs.getString(61);
        order.setCauditpsn(cauditpsn == null ? null : cauditpsn.trim());

        String dauditdate = rs.getString(62);
        order.setDauditdate(dauditdate == null ? null : new UFDate(dauditdate.trim(), false));

        String ts = rs.getString(63);
        order.setTs(ts == null ? null : ts.trim());

        Integer iprintcount = (Integer)rs.getObject(64);
        order.setIprintcount(iprintcount == null ? null : iprintcount);

        String tmaketime = rs.getString(65);
        order.setTmaketime(tmaketime == null ? null : tmaketime.trim());
        String taudittime = rs.getString(66);
        order.setTaudittime(taudittime == null ? null : taudittime.trim());
        String tlastmaketime = rs.getString(67);
        order.setTlastmaketime(tlastmaketime == null ? null : tlastmaketime.trim());

        v.addElement(order);
      }
    } finally {
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
    orders = new OrderHeaderVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(orders);
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "queryAllHead", new Object[] { auditCondition, condition });

    return orders;
  }

  public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString) throws BusinessException
  {
    try {
      return queryAllHead("1", whereString, "");
    }
    catch (SQLException e) {
      SCMEnv.out(e);
    }
    return null;
  }

  public NewPraybillItemVO[] queryAllPrayBody(String condition)
    throws SQLException
  {
    StringBuffer patch = new StringBuffer();

    patch.append(" left join bd_deptdoc on po_praybill.cdeptid = bd_deptdoc.pk_deptdoc   ");

    patch.append(" left join bd_psndoc on po_praybill.cpraypsn = bd_psndoc.pk_psndoc  ");

    patch.append(" left join bd_busitype on po_praybill.cbiztype = bd_busitype.pk_busitype    ");

    patch.append(" left join bd_calbody on po_praybill_b.pk_reqstoorg = bd_calbody.pk_calbody    ");

    patch.append(" left join sm_user on po_praybill.coperator = sm_user.cuserid  ");
    patch.append("");

    StringBuffer sql = new StringBuffer();
    sql.append("    ");
    sql.append(" select cpraybill_bid, po_praybill_b.cpraybillid, po_praybill_b.pk_corp,   ");
    sql.append(" cpurorganization, cmangid, cbaseid, isnull(npraynum,0),   ");
    sql.append(" cassistunit, nassistnum, nsuggestprice, cvendormangid, cvendorbaseid,  ");
    sql.append(" ddemanddate, dsuggestdate, cwarehouseid, isnull(naccumulatenum,0) ,  ");

    sql.append(" po_praybill.cdeptid,po_praybill.vpraycode,po_praybill_b.pk_reqstoorg, ");
    sql.append(" po_praybill.vdef1, po_praybill.vdef2, po_praybill.vdef3, po_praybill.vdef4, po_praybill.vdef5,");
    sql.append(" po_praybill.vdef6, po_praybill.vdef7, po_praybill.vdef8, po_praybill.vdef9, po_praybill.vdef10,");
    sql.append(" po_praybill.vdef11, po_praybill.vdef12, po_praybill.vdef13, po_praybill.vdef14, po_praybill.vdef15,");
    sql.append(" po_praybill.vdef16, po_praybill.vdef17, po_praybill.vdef18, po_praybill.vdef19, po_praybill.vdef20,");
    sql.append(" po_praybill.vmemo,");

    sql.append(" po_praybill.ts, po_praybill_b.ts,");

    sql.append(" po_praybill_b.vdef1, po_praybill_b.vdef2, po_praybill_b.vdef3, po_praybill_b.vdef4, po_praybill_b.vdef5, po_praybill_b.vdef6, ");

    sql.append(" po_praybill_b.vdef7, po_praybill_b.vdef8, po_praybill_b.vdef9, po_praybill_b.vdef10, ");

    sql.append(" po_praybill_b.vdef11, po_praybill_b.vdef12, po_praybill_b.vdef13, po_praybill_b.vdef14, po_praybill_b.vdef15, po_praybill_b.vdef16, ");

    sql.append(" po_praybill_b.vdef17, po_praybill_b.vdef18, po_praybill_b.vdef19, po_praybill_b.vdef20, ");

    sql.append(" po_praybill_b.vmemo, ");

    sql.append(" po_praybill_b.vfree1, po_praybill_b.vfree2, po_praybill_b.vfree3, po_praybill_b.vfree4, po_praybill_b.vfree5, ");
    sql.append(" po_praybill_b.cprojectid, po_praybill_b.cprojectphaseid, ");
    sql.append(" po_praybill_b.vproducenum, ");
    sql.append(" po_praybill_b.csourcebilltype,po_praybill_b.csourcebillid,po_praybill_b.csourcebillrowid,po_praybill_b.cupsourcebilltype,po_praybill_b.cupsourcebillid,po_praybill_b.cupsourcebillrowid ");

    sql.append(" from po_praybill   ");
    sql.append(" left join po_praybill_b   ");
    sql.append("      on po_praybill.pk_corp=po_praybill_b.pk_corp and po_praybill.cpraybillid = po_praybill_b.cpraybillid   ");
    sql.append(patch.toString());
    sql.append(" where po_praybill.dr=0 and po_praybill_b.dr=0 and po_praybill.ibillstatus=3   ");
    sql.append(" and isnull(npraynum,0)>isnull(naccumulatenum,0)   ");

    if ((condition != null) && (!condition.trim().equals(""))) {
      sql.append(" and " + condition + "   ");
    }
    NewPraybillItemVO[] praybillBs = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      rs = stmt.executeQuery();

      while (rs.next()) {
        NewPraybillItemVO praybillB = new NewPraybillItemVO();

        String cpraybill_bid = rs.getString(1);
        praybillB.setCpraybill_bid(cpraybill_bid == null ? null : cpraybill_bid.trim());

        String cpraybillid = rs.getString(2);
        praybillB.setCpraybillid(cpraybillid == null ? null : cpraybillid.trim());

        String cunitid = rs.getString(3);
        praybillB.setPk_corp(cunitid == null ? null : cunitid.trim());

        String cpurorganization = rs.getString(4);
        praybillB.setCpurorganization(cpurorganization == null ? null : cpurorganization.trim());

        String cmangid = rs.getString(5);
        praybillB.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString(6);
        praybillB.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal npraynum = rs.getBigDecimal(7);
        praybillB.setNpraynum(npraynum == null ? null : new UFDouble(npraynum));

        String cassistunit = rs.getString(8);
        praybillB.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = rs.getBigDecimal(9);
        praybillB.setNassistnum(nassistnum == null ? null : new UFDouble(nassistnum));

        BigDecimal nsuggestprice = rs.getBigDecimal(10);
        praybillB.setNsuggestprice(nsuggestprice == null ? null : new UFDouble(nsuggestprice));

        String cvendormangid = rs.getString(11);
        praybillB.setCvendormangid(cvendormangid == null ? null : cvendormangid.trim());

        String cvendorbaseid = rs.getString(12);
        praybillB.setCvendorbaseid(cvendorbaseid == null ? null : cvendorbaseid.trim());

        String ddemanddate = rs.getString(13);
        praybillB.setDdemanddate(ddemanddate == null ? null : new UFDate(ddemanddate.trim(), false));

        String dsuggestdate = rs.getString(14);
        praybillB.setDsuggestdate(dsuggestdate == null ? null : new UFDate(dsuggestdate.trim(), false));

        String cwarehouseid = rs.getString(15);
        praybillB.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        BigDecimal naccumulatenum = rs.getBigDecimal(16);
        praybillB.setNaccumulatenum(naccumulatenum == null ? null : new UFDouble(naccumulatenum));

        String cdeptid = rs.getString(17);
        praybillB.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String vpraycode = rs.getString(18);
        praybillB.setVpraycode(vpraycode == null ? null : vpraycode.trim());

        String cstoreorganization = rs.getString(19);
        praybillB.setCstoreorganization(cstoreorganization == null ? null : cstoreorganization.trim());

        String[] vdefs = new String[20];
        String vdef1 = rs.getString(20);
        vdefs[0] = (vdef1 == null ? null : vdef1.trim());
        String vdef2 = rs.getString(21);
        vdefs[1] = (vdef2 == null ? null : vdef2.trim());
        String vdef3 = rs.getString(22);
        vdefs[2] = (vdef3 == null ? null : vdef3.trim());
        String vdef4 = rs.getString(23);
        vdefs[3] = (vdef4 == null ? null : vdef4.trim());
        String vdef5 = rs.getString(24);
        vdefs[4] = (vdef5 == null ? null : vdef5.trim());
        String vdef6 = rs.getString(25);
        vdefs[5] = (vdef6 == null ? null : vdef6.trim());
        String vdef7 = rs.getString(26);
        vdefs[6] = (vdef7 == null ? null : vdef7.trim());
        String vdef8 = rs.getString(27);
        vdefs[7] = (vdef8 == null ? null : vdef8.trim());
        String vdef9 = rs.getString(28);
        vdefs[8] = (vdef9 == null ? null : vdef9.trim());
        String vdef10 = rs.getString(29);
        vdefs[9] = (vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString(30);
        vdefs[10] = (vdef11 == null ? null : vdef11.trim());
        String vdef12 = rs.getString(31);
        vdefs[11] = (vdef12 == null ? null : vdef12.trim());
        String vdef13 = rs.getString(32);
        vdefs[12] = (vdef13 == null ? null : vdef13.trim());
        String vdef14 = rs.getString(33);
        vdefs[13] = (vdef14 == null ? null : vdef14.trim());
        String vdef15 = rs.getString(34);
        vdefs[14] = (vdef15 == null ? null : vdef15.trim());
        String vdef16 = rs.getString(35);
        vdefs[15] = (vdef16 == null ? null : vdef16.trim());
        String vdef17 = rs.getString(36);
        vdefs[16] = (vdef17 == null ? null : vdef17.trim());
        String vdef18 = rs.getString(37);
        vdefs[17] = (vdef18 == null ? null : vdef18.trim());
        String vdef19 = rs.getString(38);
        vdefs[18] = (vdef19 == null ? null : vdef19.trim());
        String vdef20 = rs.getString(39);
        vdefs[19] = (vdef20 == null ? null : vdef20.trim());

        praybillB.setHvdefs(vdefs);

        String memo = rs.getString(40);
        praybillB.setHmemo(memo == null ? null : memo.trim());

        String hts = rs.getString(41);
        praybillB.setHts(hts == null ? null : hts.trim());

        String bts = rs.getString(42);
        praybillB.setTs(bts == null ? null : bts.trim());

        String def1 = rs.getString(43);
        praybillB.setAttributeValue("vdef1", def1 == null ? null : def1.trim());
        String def2 = rs.getString(44);
        praybillB.setAttributeValue("vdef2", def2 == null ? null : def2.trim());
        String def3 = rs.getString(45);
        praybillB.setAttributeValue("vdef3", def3 == null ? null : def3.trim());
        String def4 = rs.getString(46);
        praybillB.setAttributeValue("vdef4", def4 == null ? null : def4.trim());
        String def5 = rs.getString(47);
        praybillB.setAttributeValue("vdef5", def5 == null ? null : def5.trim());
        String def6 = rs.getString(48);
        praybillB.setAttributeValue("vdef6", def6 == null ? null : def6.trim());

        String def7 = rs.getString(49);
        praybillB.setAttributeValue("vdef7", def7 == null ? null : def7.trim());
        String def8 = rs.getString(50);
        praybillB.setAttributeValue("vdef8", def8 == null ? null : def8.trim());
        String def9 = rs.getString(51);
        praybillB.setAttributeValue("vdef9", def9 == null ? null : def9.trim());
        String def10 = rs.getString(52);
        praybillB.setAttributeValue("vdef10", def10 == null ? null : def10.trim());

        String def11 = rs.getString(53);
        praybillB.setAttributeValue("vdef11", def11 == null ? null : def11.trim());
        String def12 = rs.getString(54);
        praybillB.setAttributeValue("vdef12", def12 == null ? null : def12.trim());
        String def13 = rs.getString(55);
        praybillB.setAttributeValue("vdef13", def13 == null ? null : def13.trim());
        String def14 = rs.getString(56);
        praybillB.setAttributeValue("vdef14", def14 == null ? null : def14.trim());
        String def15 = rs.getString(57);
        praybillB.setAttributeValue("vdef15", def15 == null ? null : def15.trim());
        String def16 = rs.getString(58);
        praybillB.setAttributeValue("vdef16", def16 == null ? null : def16.trim());

        String def17 = rs.getString(59);
        praybillB.setAttributeValue("vdef17", def17 == null ? null : def17.trim());
        String def18 = rs.getString(60);
        praybillB.setAttributeValue("vdef18", def18 == null ? null : def18.trim());
        String def19 = rs.getString(61);
        praybillB.setAttributeValue("vdef19", def19 == null ? null : def19.trim());
        String def20 = rs.getString(62);
        praybillB.setAttributeValue("vdef20", def20 == null ? null : def20.trim());

        String bmemo = rs.getString(63);
        praybillB.setVmemo(bmemo == null ? null : bmemo.trim());

        String vfree1 = rs.getString(64);
        praybillB.setVfree1(vfree1 == null ? null : vfree1.trim());
        String vfree2 = rs.getString(65);
        praybillB.setVfree2(vfree2 == null ? null : vfree2.trim());
        String vfree3 = rs.getString(66);
        praybillB.setVfree3(vfree3 == null ? null : vfree3.trim());
        String vfree4 = rs.getString(67);
        praybillB.setVfree4(vfree4 == null ? null : vfree4.trim());
        String vfree5 = rs.getString(68);
        praybillB.setVfree5(vfree5 == null ? null : vfree5.trim());

        String cprojectid = rs.getString(69);
        praybillB.setCprojectid(cprojectid == null ? null : cprojectid.trim());
        String cprojectphaseid = rs.getString(70);
        praybillB.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vproducenum = rs.getString(71);
        praybillB.setVproducenum(vproducenum == null ? null : vproducenum.trim());

        String csourcebilltype = rs.getString(72);
        praybillB.setCsourcebilltype(csourcebilltype == null ? null : csourcebilltype.trim());

        String csourcebillid = rs.getString(73);
        praybillB.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillrowid = rs.getString(74);
        praybillB.setCsourcebillrowid(csourcebillrowid == null ? null : csourcebillrowid.trim());

        String cupsourcebilltype = rs.getString(75);
        praybillB.setCupsourcebilltype(cupsourcebilltype == null ? null : cupsourcebilltype.trim());

        String cupsourcebillid = rs.getString(76);
        praybillB.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillrowid = rs.getString(77);
        praybillB.setCupsourcebillrowid(cupsourcebillrowid == null ? null : cupsourcebillrowid.trim());

        v.addElement(praybillB);
      }
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
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
    praybillBs = new NewPraybillItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(praybillBs);
    }

    return praybillBs;
  }

  public OrderHeaderVO[] queryHeadByVO(OrderHeaderVO condOrderVO, Boolean isAnd)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "queryHeadByVO", new Object[] { condOrderVO, isAnd });

    String strSql = "select corderid, vordercode, pk_corp, cpurorganization, cwareid, dorderdate, cvendorid, caccountbankid, cdeptid, cemployeeid, cbiztype, creciever, cgiveinvoicevendor, ctransmodeid, ctermProtocolid, ibillstatus, vmemo, caccountyear, coperator, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14, pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20, iprintcount from sc_order";
    String strConditionNames = "";
    String strAndOr = "and ";
    if (!isAnd.booleanValue()) {
      strAndOr = "or  ";
    }
    if (condOrderVO.getVordercode() != null) {
      strConditionNames = strConditionNames + strAndOr + "vordercode=? ";
    }
    if (condOrderVO.getPk_corp() != null) {
      strConditionNames = strConditionNames + strAndOr + "pk_corp=? ";
    }
    if (condOrderVO.getCpurorganization() != null) {
      strConditionNames = strConditionNames + strAndOr + "cpurorganization=? ";
    }
    if (condOrderVO.getCwareid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cwareid=? ";
    }
    if (condOrderVO.getDorderdate() != null) {
      strConditionNames = strConditionNames + strAndOr + "dorderdate=? ";
    }
    if (condOrderVO.getCvendorid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cvendorid=? ";
    }
    if (condOrderVO.getCaccountbankid() != null) {
      strConditionNames = strConditionNames + strAndOr + "caccountbankid=? ";
    }
    if (condOrderVO.getCdeptid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cdeptid=? ";
    }
    if (condOrderVO.getCemployeeid() != null) {
      strConditionNames = strConditionNames + strAndOr + "cemployeeid=? ";
    }
    if (condOrderVO.getCbiztype() != null) {
      strConditionNames = strConditionNames + strAndOr + "cbiztype=? ";
    }
    if (condOrderVO.getCreciever() != null) {
      strConditionNames = strConditionNames + strAndOr + "creciever=? ";
    }
    if (condOrderVO.getCgiveinvoicevendor() != null) {
      strConditionNames = strConditionNames + strAndOr + "cgiveinvoicevendor=? ";
    }
    if (condOrderVO.getCtransmodeid() != null) {
      strConditionNames = strConditionNames + strAndOr + "ctransmodeid=? ";
    }
    if (condOrderVO.getCtermProtocolid() != null) {
      strConditionNames = strConditionNames + strAndOr + "ctermProtocolid=? ";
    }
    if (condOrderVO.getIbillstatus() != null) {
      strConditionNames = strConditionNames + strAndOr + "ibillstatus=? ";
    }
    if (condOrderVO.getVmemo() != null) {
      strConditionNames = strConditionNames + strAndOr + "vmemo=? ";
    }
    if (condOrderVO.getCaccountyear() != null) {
      strConditionNames = strConditionNames + strAndOr + "caccountyear=? ";
    }
    if (condOrderVO.getCoperator() != null) {
      strConditionNames = strConditionNames + strAndOr + "coperator=? ";
    }
    if (condOrderVO.getVdef1() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef1=? ";
    }
    if (condOrderVO.getVdef2() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef2=? ";
    }
    if (condOrderVO.getVdef3() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef3=? ";
    }
    if (condOrderVO.getVdef4() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef4=? ";
    }
    if (condOrderVO.getVdef5() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef5=? ";
    }
    if (condOrderVO.getVdef6() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef6=? ";
    }
    if (condOrderVO.getVdef7() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef7=? ";
    }
    if (condOrderVO.getVdef8() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef8=? ";
    }
    if (condOrderVO.getVdef9() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef9=? ";
    }
    if (condOrderVO.getVdef10() != null) {
      strConditionNames = strConditionNames + strAndOr + "vdef10=? ";
    }
    if (strConditionNames.length() > 0)
      strConditionNames = strConditionNames.substring(3, strConditionNames.length() - 1);
    else {
      return queryAllHead(null);
    }

    strSql = strSql + " where dr=0 ";
    strSql = strSql + " and " + strConditionNames;

    int index = 0;
    OrderHeaderVO[] orders = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);

      if (condOrderVO.getVordercode() != null) {
        index++; stmt.setString(index, condOrderVO.getVordercode());
      }
      if (condOrderVO.getPk_corp() != null) {
        index++; stmt.setString(index, condOrderVO.getPk_corp());
      }
      if (condOrderVO.getCpurorganization() != null) {
        index++; stmt.setString(index, condOrderVO.getCpurorganization());
      }
      if (condOrderVO.getCwareid() != null) {
        index++; stmt.setString(index, condOrderVO.getCwareid());
      }
      if (condOrderVO.getDorderdate() != null) {
        index++; stmt.setString(index, condOrderVO.getDorderdate().toString());
      }
      if (condOrderVO.getCvendorid() != null) {
        index++; stmt.setString(index, condOrderVO.getCvendorid());
      }
      if (condOrderVO.getCaccountbankid() != null) {
        index++; stmt.setString(index, condOrderVO.getCaccountbankid());
      }
      if (condOrderVO.getCdeptid() != null) {
        index++; stmt.setString(index, condOrderVO.getCdeptid());
      }
      if (condOrderVO.getCemployeeid() != null) {
        index++; stmt.setString(index, condOrderVO.getCemployeeid());
      }
      if (condOrderVO.getCbiztype() != null) {
        index++; stmt.setString(index, condOrderVO.getCbiztype());
      }
      if (condOrderVO.getCreciever() != null) {
        index++; stmt.setString(index, condOrderVO.getCreciever());
      }
      if (condOrderVO.getCgiveinvoicevendor() != null) {
        index++; stmt.setString(index, condOrderVO.getCgiveinvoicevendor());
      }
      if (condOrderVO.getCtransmodeid() != null) {
        index++; stmt.setString(index, condOrderVO.getCtransmodeid());
      }
      if (condOrderVO.getCtermProtocolid() != null) {
        index++; stmt.setString(index, condOrderVO.getCtermProtocolid());
      }
      if (condOrderVO.getIbillstatus() != null) {
        index++; stmt.setInt(index, condOrderVO.getIbillstatus().intValue());
      }
      if (condOrderVO.getVmemo() != null) {
        index++; stmt.setString(index, condOrderVO.getVmemo());
      }
      if (condOrderVO.getCaccountyear() != null) {
        index++; stmt.setString(index, condOrderVO.getCaccountyear());
      }
      if (condOrderVO.getCoperator() != null) {
        index++; stmt.setString(index, condOrderVO.getCoperator());
      }
      if (condOrderVO.getVdef1() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef1());
      }
      if (condOrderVO.getVdef2() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef2());
      }
      if (condOrderVO.getVdef3() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef3());
      }
      if (condOrderVO.getVdef4() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef4());
      }
      if (condOrderVO.getVdef5() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef5());
      }
      if (condOrderVO.getVdef6() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef6());
      }
      if (condOrderVO.getVdef7() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef7());
      }
      if (condOrderVO.getVdef8() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef8());
      }
      if (condOrderVO.getVdef9() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef9());
      }
      if (condOrderVO.getVdef10() != null) {
        index++; stmt.setString(index, condOrderVO.getVdef10());
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        OrderHeaderVO order = new OrderHeaderVO();

        String corderid = rs.getString(1);
        order.setCorderid(corderid == null ? null : corderid.trim());

        String vordercode = rs.getString(2);
        order.setVordercode(vordercode == null ? null : vordercode.trim());

        String pk_corp = rs.getString(3);
        order.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cpurorganization = rs.getString(4);
        order.setCpurorganization(cpurorganization == null ? null : cpurorganization.trim());

        String cwareid = rs.getString(5);
        order.setCwareid(cwareid == null ? null : cwareid.trim());

        String dorderdate = rs.getString(6);
        order.setDorderdate(dorderdate == null ? null : new UFDate(dorderdate.trim(), false));

        String cvendorid = rs.getString(7);
        order.setCvendorid(cvendorid == null ? null : cvendorid.trim());

        String caccountbankid = rs.getString(8);
        order.setCaccountbankid(caccountbankid == null ? null : caccountbankid.trim());

        String cdeptid = rs.getString(9);
        order.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(10);
        order.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String cbiztypeid = rs.getString(11);
        order.setCbiztype(cbiztypeid == null ? null : cbiztypeid.trim());

        String creciever = rs.getString(12);
        order.setCreciever(creciever == null ? null : creciever.trim());

        String cgiveinvoicevendor = rs.getString(13);
        order.setCgiveinvoicevendor(cgiveinvoicevendor == null ? null : cgiveinvoicevendor.trim());

        String ctransmodeid = rs.getString(14);
        order.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        String ctermProtocolid = rs.getString(15);
        order.setCtermProtocolid(ctermProtocolid == null ? null : ctermProtocolid.trim());

        Integer ibillstatus = (Integer)rs.getObject(16);
        order.setIbillstatus(ibillstatus == null ? null : ibillstatus);

        String vmemo = rs.getString(17);
        order.setVmemo(vmemo == null ? null : vmemo.trim());

        String caccountyear = rs.getString(18);
        order.setCaccountyear(caccountyear == null ? null : caccountyear.trim());

        String coperator = rs.getString(19);
        order.setCoperator(coperator == null ? null : coperator.trim());

        String vdef1 = rs.getString(20);
        order.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(21);
        order.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(22);
        order.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(23);
        order.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(24);
        order.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(25);
        order.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(26);
        order.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(27);
        order.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(28);
        order.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(29);
        order.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString(30);
        order.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString(31);
        order.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString(32);
        order.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString(33);
        order.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString(34);
        order.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString(35);
        order.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString(36);
        order.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString(37);
        order.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString(38);
        order.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString(39);
        order.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString(40);
        order.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(41);
        order.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(42);
        order.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(43);
        order.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(44);
        order.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(45);
        order.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(46);
        order.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(47);
        order.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(48);
        order.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(49);
        order.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(50);
        order.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(51);
        order.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(52);
        order.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(53);
        order.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(54);
        order.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(55);
        order.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(56);
        order.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(57);
        order.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(58);
        order.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(59);
        order.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        Integer iprintcount = (Integer)rs.getObject(60);
        order.setIprintcount(iprintcount == null ? null : iprintcount);

        v.addElement(order);
      }
    } finally {
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
    orders = new OrderHeaderVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(orders);
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "queryHeadByVO", new Object[] { condOrderVO, isAnd });

    return orders;
  }

  public NewPraybillItemVO queryOnePrayBody(String bill_bid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pr.pray.PraybillDMO", "queryAllBody", new Object[] { bill_bid });

    String sql = "select cpraybill_bid, cpraybillid, pk_corp, cpurorganization, cmangid, cbaseid, npraynum, cassistunit, nassistnum, nsuggestprice, cvendormangid, cvendorbaseid, ddemanddate, dsuggestdate, cwarehouseid, naccumulatenum, cprojectid, cprojectphaseid, csourcebilltype, csourcebillid, csourcebillrowid, cupsourcebilltype, cupsourcebillid, cupsourcebillrowid, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6 from po_praybill_b where dr = 0 and cpraybill_bid = ? ";

    NewPraybillItemVO praybillB = new NewPraybillItemVO();
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (bill_bid != null) {
        stmt.setString(1, bill_bid);
      }
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        String cpraybill_bid = rs.getString(1);
        praybillB.setCpraybill_bid(cpraybill_bid == null ? null : cpraybill_bid.trim());

        String cpraybillid = rs.getString(2);
        praybillB.setCpraybillid(cpraybillid == null ? null : cpraybillid.trim());

        String cunitid = rs.getString(3);
        praybillB.setPk_corp(cunitid == null ? null : cunitid.trim());

        String cpurorganization = rs.getString(4);
        praybillB.setCpurorganization(cpurorganization == null ? null : cpurorganization.trim());

        String cmangid = rs.getString(5);
        praybillB.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString(6);
        praybillB.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal npraynum = rs.getBigDecimal(7);
        praybillB.setNpraynum(npraynum == null ? new UFDouble(0.0D) : new UFDouble(npraynum));

        String cassistunit = rs.getString(8);
        praybillB.setCassistunit(cassistunit == null ? null : cassistunit.trim());

        BigDecimal nassistnum = rs.getBigDecimal(9);
        praybillB.setNassistnum(nassistnum == null ? new UFDouble(0.0D) : new UFDouble(nassistnum));

        BigDecimal nsuggestprice = rs.getBigDecimal(10);
        praybillB.setNsuggestprice(nsuggestprice == null ? new UFDouble(0.0D) : new UFDouble(nsuggestprice));

        String cvendormangid = rs.getString(11);
        praybillB.setCvendormangid(cvendormangid == null ? null : cvendormangid.trim());

        String cvendorbaseid = rs.getString(12);
        praybillB.setCvendorbaseid(cvendorbaseid == null ? null : cvendorbaseid.trim());

        String ddemanddate = rs.getString(13);
        praybillB.setDdemanddate(ddemanddate == null ? null : new UFDate(ddemanddate.trim(), false));

        String dsuggestdate = rs.getString(14);
        praybillB.setDsuggestdate(dsuggestdate == null ? null : new UFDate(dsuggestdate.trim(), false));

        String cwarehouseid = rs.getString(15);
        praybillB.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        BigDecimal naccumulatenum = rs.getBigDecimal(16);
        praybillB.setNaccumulatenum(naccumulatenum == null ? new UFDouble(0.0D) : new UFDouble(naccumulatenum));

        String cprojectid = rs.getString(17);
        praybillB.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString(18);
        praybillB.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String csourcebilltype = rs.getString(19);
        praybillB.setCsourcebilltype(csourcebilltype == null ? null : csourcebilltype.trim());

        String csourcebillid = rs.getString(20);
        praybillB.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillrowid = rs.getString(21);
        praybillB.setCsourcebillrowid(csourcebillrowid == null ? null : csourcebillrowid.trim());

        String cupsourcebilltype = rs.getString(22);
        praybillB.setCupsourcebilltype(cupsourcebilltype == null ? null : cupsourcebilltype.trim());

        String cupsourcebillid = rs.getString(23);
        praybillB.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillrowid = rs.getString(24);
        praybillB.setCupsourcebillrowid(cupsourcebillrowid == null ? null : cupsourcebillrowid.trim());

        String vmemo = rs.getString(25);
        praybillB.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString(26);
        praybillB.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(27);
        praybillB.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(28);
        praybillB.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(29);
        praybillB.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(30);
        praybillB.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString(31);
        praybillB.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(32);
        praybillB.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(33);
        praybillB.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(34);
        praybillB.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(35);
        praybillB.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(36);
        praybillB.setVdef6(vdef6 == null ? null : vdef6.trim());

        v.addElement(praybillB);
      }
    } finally {
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

    afterCallMethod("nc.bs.pr.pray.PraybillDMO", "queryAllBody", new Object[] { bill_bid });

    return praybillB;
  }

  public ArrayList splitForA3(OrderVO[] vos)
    throws BusinessException
  {
    ArrayList array = new ArrayList();
    Vector orderVec = new Vector();
    try {
      for (int i = 0; i < vos.length; i++)
        if (vos[i].getChildrenVO().length == 1)
          orderVec.addElement(vos[i]);
        else
          for (int j = 0; j < vos[i].getChildrenVO().length; j++) {
            OrderVO tempVO = new OrderVO();
            tempVO.setParentVO((OrderHeaderVO)vos[i].getParentVO().clone());
            OrderItemVO[] items = new OrderItemVO[1];
            items[0] = ((OrderItemVO)vos[i].getChildrenVO()[j]);
            tempVO.setChildrenVO(items);
            orderVec.addElement(tempVO);
          }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      throw new BusinessException(e.getMessage());
    }
    for (int i = 0; i < orderVec.size(); i++) {
      array.add(orderVec.get(i));
    }
    return array;
  }

  public void unAudit(OrderVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "unAudit", new Object[] { vo });

    OrderHeaderVO headVO = (OrderHeaderVO)vo.getParentVO();
    String billId = headVO.getCorderid();

    String sql = "update sc_order set  cauditpsn = null ,dauditdate = null , ibillstatus = 0, taudittime = null where corderid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (billId == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, billId);
      }

      stmt.executeUpdate();
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "unAudit", new Object[] { vo });
  }

  public OrderVO update(OrderVO vo)
    throws SQLException, BusinessException, SystemException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "update", new Object[] { vo });

    String[] key_b = null;

    OrderItemVO[] items = (OrderItemVO[])(OrderItemVO[])vo.getChildrenVO();

    Vector lvec_ItemsNew = new Vector();
    Vector lvec_ItemsUpdated = new Vector();
    Vector lvec_ItemsDeleted = new Vector();

    for (int i = 0; i < items.length; i++) {
      switch (items[i].getStatus()) {
      case 2:
        lvec_ItemsNew.addElement(items[i]);
        break;
      case 1:
        lvec_ItemsUpdated.addElement(items[i]);
        break;
      case 3:
        lvec_ItemsDeleted.addElement(items[i]);
      }
    }

    OrderItemVO[] l_ItemsNew = new OrderItemVO[lvec_ItemsNew.size()];
    OrderItemVO[] l_ItemsUpdated = new OrderItemVO[lvec_ItemsUpdated.size()];
    OrderItemVO[] l_ItemsDeleted = new OrderItemVO[lvec_ItemsDeleted.size()];

    lvec_ItemsNew.copyInto(l_ItemsNew);
    lvec_ItemsUpdated.copyInto(l_ItemsUpdated);
    lvec_ItemsDeleted.copyInto(l_ItemsDeleted);

    if (l_ItemsNew.length > 0) {
      key_b = insertItems(l_ItemsNew);
    }

    if (l_ItemsUpdated.length > 0) {
      updateItems(l_ItemsUpdated);
    }

    if (l_ItemsDeleted.length > 0) {
      deleteItems(l_ItemsDeleted);
    }

    updateHeader((OrderHeaderVO)vo.getParentVO());

    String key = ((OrderHeaderVO)vo.getParentVO()).getCorderid();
    if ((key != null) && (key.trim().length() > 0)) {
      OrderVO returnVO = findByPrimaryKey(key);
      return returnVO;
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "update", new Object[] { vo });

    return null;
  }

  public void updateMaterialItem(OrderBbVO orderBb)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderBbDMO", "updateMaterialItems", new Object[] { orderBb });

    String sql = "update sc_order_bb set corderid = ?, corder_bid = ?, pk_corp = ?, cmangid = ?, cbaseid = ?, nmaterialnum = ?, cassistunit = ?, nassistnum = ?, nprice = ?, nmoney = ?, ddeliverydate = ?, ntotalnum = ?, cdeliverywarehouse = ?, vdeliveryaddress = ?, vmemo = ?, vfree1 = ?, vfree2 = ?, vfree3 = ?, vfree4 = ?, vfree5 = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ? where corder_bbid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (orderBb.getCorderid() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, orderBb.getCorderid());
      }
      if (orderBb.getCorder_bid() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, orderBb.getCorder_bid());
      }
      if (orderBb.getPk_corp() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, orderBb.getPk_corp());
      }
      if (orderBb.getCmangid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, orderBb.getCmangid());
      }
      if (orderBb.getCbaseid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, orderBb.getCbaseid());
      }
      if (orderBb.getNmaterialnum() == null)
        stmt.setNull(6, 4);
      else {
        stmt.setBigDecimal(6, orderBb.getNmaterialnum().toBigDecimal());
      }
      if (orderBb.getCassistunit() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, orderBb.getCassistunit());
      }
      if (orderBb.getNassistnum() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, orderBb.getNassistnum().toBigDecimal());
      }
      if (orderBb.getNprice() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setBigDecimal(9, orderBb.getNprice().toBigDecimal());
      }
      if (orderBb.getNmoney() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setBigDecimal(10, orderBb.getNmoney().toBigDecimal());
      }
      if (orderBb.getDdeliverydate() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, orderBb.getDdeliverydate().toString());
      }
      if (orderBb.getNtotalnum() == null)
        stmt.setNull(12, 4);
      else {
        stmt.setBigDecimal(12, orderBb.getNtotalnum().toBigDecimal());
      }
      if (orderBb.getCdeliverywarehouse() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, orderBb.getCdeliverywarehouse());
      }
      if (orderBb.getVdeliveryaddress() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, orderBb.getVdeliveryaddress());
      }
      if (orderBb.getVmemo() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, orderBb.getVmemo());
      }
      if (orderBb.getVfree1() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, orderBb.getVfree1());
      }
      if (orderBb.getVfree2() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, orderBb.getVfree2());
      }
      if (orderBb.getVfree3() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, orderBb.getVfree3());
      }
      if (orderBb.getVfree4() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, orderBb.getVfree4());
      }
      if (orderBb.getVfree5() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, orderBb.getVfree5());
      }
      if (orderBb.getVdef1() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, orderBb.getVdef1());
      }
      if (orderBb.getVdef2() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, orderBb.getVdef2());
      }
      if (orderBb.getVdef3() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, orderBb.getVdef3());
      }
      if (orderBb.getVdef4() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, orderBb.getVdef4());
      }
      if (orderBb.getVdef5() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, orderBb.getVdef5());
      }
      if (orderBb.getVdef6() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, orderBb.getVdef6());
      }

      stmt.setString(27, orderBb.getPrimaryKey());

      stmt.executeUpdate();
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderBbDMO", "updateMaterialItems", new Object[] { orderBb });
  }

  public void deleteItems(OrderItemVO[] vos)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "deleteItem", new Object[] { vos });

    String sql = "update sc_order_b set dr=1 where corder_bid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      for (int i = 0; i < vos.length; i++) {
        stmt.setString(1, vos[i].getPrimaryKey());
        stmt.executeUpdate();
      }
      executeBatch(stmt);
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "deleteItem", new Object[] { vos });
  }

  public UFDouble getScOrderSumMny(OrderVO order)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "getScOrderSumMny", new Object[] { order });

    String corderid = ((OrderHeaderVO)order.getParentVO()).getCorderid();
    if ((corderid == null) || (corderid.trim().equals(""))) {
      return null;
    }
    String sql = "select nsummny from sc_order_b where corderid = ?  ";

    Vector vec = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, corderid);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        BigDecimal mainmeasrate = rs.getBigDecimal(1);
        vec.addElement(mainmeasrate);
      }
    } finally {
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
    if ((vec == null) || (vec.size() == 0))
      return null;
    UFDouble returnSummny = new UFDouble();
    for (int i = 0; i < vec.size(); i++) {
      UFDouble curMny = vec.elementAt(i) == null ? new UFDouble(0) : new UFDouble(vec.elementAt(i).toString());
      returnSummny = returnSummny.add(curMny);
    }

    afterCallMethod("nc.bs.sc.order.OrderDMO", "getScOrderSumMny", new Object[] { order });

    return new UFDouble(returnSummny.toDouble());
  }

  public String[] insertItems(OrderItemVO[] orderItems)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "insertItems", new Object[] { orderItems });

    String sql = "insert into sc_order_b(corder_bid, corderid, pk_corp, cmangid, cbaseid, nordernum," +
    		" cassistunit, nassistnum, ndiscountrate, idiscounttaxtype, ntaxrate, ccurrencytypeid," +
    		" noriginalnetprice, noriginalcurprice, noriginalcurmny, noriginaltaxmny, noriginalsummny," +
    		" nexchangeotobrate, ntaxmny, nmoney, nsummny, nexchangeotoarate, nassistcurmny, nassisttaxmny," +
    		" nassistsummny, naccumarrvnum, naccumstorenum, naccuminvoicenum, naccumwastnum, dplanarrvdate," +
    		" cwarehouseid, creceiveaddress, cprojectid, cprojectphaseid, coperator, forderrowstatus, bisactive," +
    		" cordersource, csourcebillid, csourcebillrow, cupsourcebilltype, cupsourcebillid, cupsourcebillrowid," +
    		" vmemo, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7," +
    		" vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19," +
    		" vdef20, pk_defdoc1, pk_defdoc2, pk_defdoc3, pk_defdoc4, pk_defdoc5, pk_defdoc6, pk_defdoc7, " +
    		"pk_defdoc8, pk_defdoc9, pk_defdoc10, pk_defdoc11, pk_defdoc12, pk_defdoc13, pk_defdoc14," +
    		" pk_defdoc15, pk_defdoc16, pk_defdoc17, pk_defdoc18, pk_defdoc19, pk_defdoc20, crowno," +
    		" norgtaxprice, norgnettaxprice, vproducenum,ccontractid,ccontractrowid,ccontractrcode," +
    		"vpriceauditcode,cpriceauditid,cpriceaudit_bid,cpriceaudit_bb1id,bomvers ) " +//shikun bomvers
    		"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
    		" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
    		"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ? , ?, ?, ?, ? , ?, ?,? )";//shikun ?

    String[] key = new String[orderItems.length];
    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      for (int i = 0; i < orderItems.length; i++)
      {
        key[i] = getOID(orderItems[i].getPk_corp());

        orderItems[i].setPrimaryKey(key[i]);

        stmt.setString(1, key[i]);

        if (orderItems[i].getCorderid() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, orderItems[i].getCorderid());
        }
        if (orderItems[i].getPk_corp() == null)
          stmt.setNull(3, 1);
        else {
          stmt.setString(3, orderItems[i].getPk_corp());
        }
        if (orderItems[i].getCmangid() == null)
          stmt.setNull(4, 1);
        else {
          stmt.setString(4, orderItems[i].getCmangid());
        }
        if (orderItems[i].getCbaseid() == null)
          stmt.setNull(5, 1);
        else {
          stmt.setString(5, orderItems[i].getCbaseid());
        }
        if (orderItems[i].getNordernum() == null)
        {
          stmt.setBigDecimal(6, new UFDouble(0).toBigDecimal());
        }
        else stmt.setBigDecimal(6, orderItems[i].getNordernum().toBigDecimal());

        if (orderItems[i].getCassistunit() == null)
          stmt.setNull(7, 1);
        else {
          stmt.setString(7, orderItems[i].getCassistunit());
        }
        if (orderItems[i].getNassistnum() == null)
          stmt.setNull(8, 4);
        else {
          stmt.setBigDecimal(8, orderItems[i].getNassistnum().toBigDecimal());
        }
        if (orderItems[i].getNdiscountrate() == null)
          stmt.setNull(9, 4);
        else {
          stmt.setBigDecimal(9, orderItems[i].getNdiscountrate().toBigDecimal());
        }
        if (orderItems[i].getIdiscounttaxtype() == null)
          stmt.setNull(10, 4);
        else {
          stmt.setInt(10, orderItems[i].getIdiscounttaxtype().intValue());
        }
        if (orderItems[i].getNtaxrate() == null)
          stmt.setNull(11, 4);
        else {
          stmt.setBigDecimal(11, orderItems[i].getNtaxrate().toBigDecimal());
        }
        if (orderItems[i].getCcurrencytypeid() == null)
          stmt.setNull(12, 1);
        else {
          stmt.setString(12, orderItems[i].getCcurrencytypeid());
        }
        if (orderItems[i].getNoriginalnetprice() == null)
          stmt.setNull(13, 4);
        else {
          stmt.setBigDecimal(13, orderItems[i].getNoriginalnetprice().toBigDecimal());
        }
        if (orderItems[i].getNoriginalcurprice() == null)
          stmt.setNull(14, 4);
        else {
          stmt.setBigDecimal(14, orderItems[i].getNoriginalcurprice().toBigDecimal());
        }
        if (orderItems[i].getNoriginalcurmny() == null)
          stmt.setNull(15, 4);
        else {
          stmt.setBigDecimal(15, orderItems[i].getNoriginalcurmny().toBigDecimal());
        }
        if (orderItems[i].getNoriginaltaxmny() == null)
          stmt.setNull(16, 4);
        else {
          stmt.setBigDecimal(16, orderItems[i].getNoriginaltaxmny().toBigDecimal());
        }
        if (orderItems[i].getNoriginalsummny() == null)
          stmt.setNull(17, 4);
        else {
          stmt.setBigDecimal(17, orderItems[i].getNoriginalsummny().toBigDecimal());
        }
        if (orderItems[i].getNexchangeotobrate() == null)
          stmt.setNull(18, 4);
        else {
          stmt.setBigDecimal(18, orderItems[i].getNexchangeotobrate().toBigDecimal());
        }
        if (orderItems[i].getNtaxmny() == null)
          stmt.setNull(19, 4);
        else {
          stmt.setBigDecimal(19, orderItems[i].getNtaxmny().toBigDecimal());
        }
        if (orderItems[i].getNmoney() == null)
        {
          stmt.setBigDecimal(20, new UFDouble(0).toBigDecimal());
        }
        else stmt.setBigDecimal(20, orderItems[i].getNmoney().toBigDecimal());

        if (orderItems[i].getNsummny() == null)
          stmt.setNull(21, 4);
        else {
          stmt.setBigDecimal(21, orderItems[i].getNsummny().toBigDecimal());
        }
        if (orderItems[i].getNexchangeotoarate() == null)
          stmt.setNull(22, 4);
        else {
          stmt.setBigDecimal(22, orderItems[i].getNexchangeotoarate().toBigDecimal());
        }
        if (orderItems[i].getNassistcurmny() == null)
          stmt.setNull(23, 4);
        else {
          stmt.setBigDecimal(23, orderItems[i].getNassistcurmny().toBigDecimal());
        }
        if (orderItems[i].getNassisttaxmny() == null)
          stmt.setNull(24, 4);
        else {
          stmt.setBigDecimal(24, orderItems[i].getNassisttaxmny().toBigDecimal());
        }
        if (orderItems[i].getNassistsummny() == null)
          stmt.setNull(25, 4);
        else {
          stmt.setBigDecimal(25, orderItems[i].getNassistsummny().toBigDecimal());
        }
        if (orderItems[i].getNaccumarrvnum() == null) {
          stmt.setNull(26, 4);
        }
        else {
          stmt.setBigDecimal(26, orderItems[i].getNaccumarrvnum().toBigDecimal());
        }
        if (orderItems[i].getNaccumstorenum() == null)
        {
          System.out.println("llllllllllllll");
          stmt.setBigDecimal(27, new UFDouble(0).toBigDecimal());
        } else {
          stmt.setBigDecimal(27, orderItems[i].getNaccumstorenum().toBigDecimal());
        }
        if (orderItems[i].getNaccuminvoicenum() == null)
          stmt.setNull(28, 4);
        else {
          stmt.setBigDecimal(28, orderItems[i].getNaccuminvoicenum().toBigDecimal());
        }
        if (orderItems[i].getNaccumwastnum() == null)
          stmt.setNull(29, 4);
        else {
          stmt.setBigDecimal(29, orderItems[i].getNaccumwastnum().toBigDecimal());
        }
        if (orderItems[i].getDplanarrvdate() == null)
          stmt.setNull(30, 1);
        else {
          stmt.setString(30, orderItems[i].getDplanarrvdate().toString());
        }
        if (orderItems[i].getCwarehouseid() == null)
          stmt.setNull(31, 1);
        else {
          stmt.setString(31, orderItems[i].getCwarehouseid());
        }
        if (orderItems[i].getCreceiveaddress() == null)
          stmt.setNull(32, 1);
        else {
          stmt.setString(32, orderItems[i].getCreceiveaddress());
        }
        if (orderItems[i].getCprojectid() == null)
          stmt.setNull(33, 1);
        else {
          stmt.setString(33, orderItems[i].getCprojectid());
        }
        if (orderItems[i].getCprojectphaseid() == null)
          stmt.setNull(34, 1);
        else {
          stmt.setString(34, orderItems[i].getCprojectphaseid());
        }
        if (orderItems[i].getCoperator() == null)
          stmt.setNull(35, 1);
        else {
          stmt.setString(35, orderItems[i].getCoperator());
        }
        if (orderItems[i].getForderrowstatus() == null)
        {
          stmt.setInt(36, 0);
        }
        else stmt.setInt(36, orderItems[i].getForderrowstatus().intValue());

        if (orderItems[i].getBisactive() == null)
          stmt.setString(37, "0");
        else {
          stmt.setString(37, orderItems[i].getBisactive().booleanValue() ? "0" : "1");
        }

        if (orderItems[i].getCordersource() == null) {
          stmt.setNull(38, 1);
        }
        else {
          stmt.setString(38, orderItems[i].getCordersource());
        }
        if (orderItems[i].getCsourcebillid() == null)
          stmt.setNull(39, 1);
        else {
          stmt.setString(39, orderItems[i].getCsourcebillid());
        }
        if (orderItems[i].getCsourcebillrow() == null)
          stmt.setNull(40, 1);
        else {
          stmt.setString(40, orderItems[i].getCsourcebillrow());
        }

        if (orderItems[i].getCupsourcebilltype() == null)
          stmt.setNull(41, 1);
        else {
          stmt.setString(41, orderItems[i].getCupsourcebilltype());
        }
        if (orderItems[i].getCupsourcebillid() == null)
          stmt.setNull(42, 1);
        else {
          stmt.setString(42, orderItems[i].getCupsourcebillid());
        }
        if (orderItems[i].getCupsourcebillrowid() == null)
          stmt.setNull(43, 1);
        else {
          stmt.setString(43, orderItems[i].getCupsourcebillrowid());
        }

        if (orderItems[i].getVmemo() == null)
          stmt.setNull(44, 1);
        else {
          stmt.setString(44, orderItems[i].getVmemo());
        }
        if (orderItems[i].getVfree1() == null)
          stmt.setNull(45, 1);
        else {
          stmt.setString(45, orderItems[i].getVfree1());
        }
        if (orderItems[i].getVfree2() == null)
          stmt.setNull(46, 1);
        else {
          stmt.setString(46, orderItems[i].getVfree2());
        }
        if (orderItems[i].getVfree3() == null)
          stmt.setNull(47, 1);
        else {
          stmt.setString(47, orderItems[i].getVfree3());
        }
        if (orderItems[i].getVfree4() == null)
          stmt.setNull(48, 1);
        else {
          stmt.setString(48, orderItems[i].getVfree4());
        }
        if (orderItems[i].getVfree5() == null)
          stmt.setNull(49, 1);
        else {
          stmt.setString(49, orderItems[i].getVfree5());
        }
        if (orderItems[i].getVdef1() == null)
          stmt.setNull(50, 1);
        else {
          stmt.setString(50, orderItems[i].getVdef1());
        }
        if (orderItems[i].getVdef2() == null)
          stmt.setNull(51, 1);
        else {
          stmt.setString(51, orderItems[i].getVdef2());
        }
        if (orderItems[i].getVdef3() == null)
          stmt.setNull(52, 1);
        else {
          stmt.setString(52, orderItems[i].getVdef3());
        }
        if (orderItems[i].getVdef4() == null)
          stmt.setNull(53, 1);
        else {
          stmt.setString(53, orderItems[i].getVdef4());
        }
        if (orderItems[i].getVdef5() == null)
          stmt.setNull(54, 1);
        else {
          stmt.setString(54, orderItems[i].getVdef5());
        }
        if (orderItems[i].getVdef6() == null)
          stmt.setNull(55, 1);
        else {
          stmt.setString(55, orderItems[i].getVdef6());
        }
        if (orderItems[i].getVdef7() == null)
          stmt.setNull(56, 1);
        else {
          stmt.setString(56, orderItems[i].getVdef7());
        }
        if (orderItems[i].getVdef8() == null)
          stmt.setNull(57, 1);
        else {
          stmt.setString(57, orderItems[i].getVdef8());
        }
        if (orderItems[i].getVdef9() == null)
          stmt.setNull(58, 1);
        else {
          stmt.setString(58, orderItems[i].getVdef9());
        }
        if (orderItems[i].getVdef10() == null)
          stmt.setNull(59, 1);
        else {
          stmt.setString(59, orderItems[i].getVdef10());
        }

        if (orderItems[i].getVdef11() == null)
          stmt.setNull(60, 1);
        else {
          stmt.setString(60, orderItems[i].getVdef11());
        }
        if (orderItems[i].getVdef12() == null)
          stmt.setNull(61, 1);
        else {
          stmt.setString(61, orderItems[i].getVdef12());
        }
        if (orderItems[i].getVdef13() == null)
          stmt.setNull(62, 1);
        else {
          stmt.setString(62, orderItems[i].getVdef13());
        }
        if (orderItems[i].getVdef14() == null)
          stmt.setNull(63, 1);
        else {
          stmt.setString(63, orderItems[i].getVdef14());
        }
        if (orderItems[i].getVdef15() == null)
          stmt.setNull(64, 1);
        else {
          stmt.setString(64, orderItems[i].getVdef15());
        }
        if (orderItems[i].getVdef16() == null)
          stmt.setNull(65, 1);
        else {
          stmt.setString(65, orderItems[i].getVdef16());
        }
        if (orderItems[i].getVdef17() == null)
          stmt.setNull(66, 1);
        else {
          stmt.setString(66, orderItems[i].getVdef17());
        }
        if (orderItems[i].getVdef18() == null)
          stmt.setNull(67, 1);
        else {
          stmt.setString(67, orderItems[i].getVdef18());
        }
        if (orderItems[i].getVdef19() == null)
          stmt.setNull(68, 1);
        else {
          stmt.setString(68, orderItems[i].getVdef19());
        }
        if (orderItems[i].getVdef20() == null)
          stmt.setNull(69, 1);
        else {
          stmt.setString(69, orderItems[i].getVdef20());
        }
        if (orderItems[i].getPKDefDoc1() == null)
          stmt.setNull(70, 1);
        else {
          stmt.setString(70, orderItems[i].getPKDefDoc1());
        }
        if (orderItems[i].getPKDefDoc2() == null)
          stmt.setNull(71, 1);
        else {
          stmt.setString(71, orderItems[i].getPKDefDoc2());
        }
        if (orderItems[i].getPKDefDoc3() == null)
          stmt.setNull(72, 1);
        else {
          stmt.setString(72, orderItems[i].getPKDefDoc3());
        }
        if (orderItems[i].getPKDefDoc4() == null)
          stmt.setNull(73, 1);
        else {
          stmt.setString(73, orderItems[i].getPKDefDoc4());
        }
        if (orderItems[i].getPKDefDoc5() == null)
          stmt.setNull(74, 1);
        else {
          stmt.setString(74, orderItems[i].getPKDefDoc5());
        }
        if (orderItems[i].getPKDefDoc6() == null)
          stmt.setNull(75, 1);
        else {
          stmt.setString(75, orderItems[i].getPKDefDoc6());
        }
        if (orderItems[i].getPKDefDoc7() == null)
          stmt.setNull(76, 1);
        else {
          stmt.setString(76, orderItems[i].getPKDefDoc7());
        }
        if (orderItems[i].getPKDefDoc8() == null)
          stmt.setNull(77, 1);
        else {
          stmt.setString(77, orderItems[i].getPKDefDoc8());
        }
        if (orderItems[i].getPKDefDoc9() == null)
          stmt.setNull(78, 1);
        else {
          stmt.setString(78, orderItems[i].getPKDefDoc9());
        }
        if (orderItems[i].getPKDefDoc10() == null)
          stmt.setNull(79, 1);
        else {
          stmt.setString(79, orderItems[i].getPKDefDoc10());
        }
        if (orderItems[i].getPKDefDoc11() == null)
          stmt.setNull(80, 1);
        else {
          stmt.setString(80, orderItems[i].getPKDefDoc11());
        }
        if (orderItems[i].getPKDefDoc12() == null)
          stmt.setNull(81, 1);
        else {
          stmt.setString(81, orderItems[i].getPKDefDoc12());
        }
        if (orderItems[i].getPKDefDoc13() == null)
          stmt.setNull(82, 1);
        else {
          stmt.setString(82, orderItems[i].getPKDefDoc13());
        }
        if (orderItems[i].getPKDefDoc14() == null)
          stmt.setNull(83, 1);
        else {
          stmt.setString(83, orderItems[i].getPKDefDoc14());
        }
        if (orderItems[i].getPKDefDoc15() == null)
          stmt.setNull(84, 1);
        else {
          stmt.setString(84, orderItems[i].getPKDefDoc15());
        }
        if (orderItems[i].getPKDefDoc16() == null)
          stmt.setNull(85, 1);
        else {
          stmt.setString(85, orderItems[i].getPKDefDoc16());
        }
        if (orderItems[i].getPKDefDoc17() == null)
          stmt.setNull(86, 1);
        else {
          stmt.setString(86, orderItems[i].getPKDefDoc17());
        }
        if (orderItems[i].getPKDefDoc18() == null)
          stmt.setNull(87, 1);
        else {
          stmt.setString(87, orderItems[i].getPKDefDoc18());
        }
        if (orderItems[i].getPKDefDoc19() == null)
          stmt.setNull(88, 1);
        else {
          stmt.setString(88, orderItems[i].getPKDefDoc19());
        }
        if (orderItems[i].getPKDefDoc20() == null)
          stmt.setNull(89, 1);
        else {
          stmt.setString(89, orderItems[i].getPKDefDoc20());
        }
        if (orderItems[i].getCrowno() == null)
          stmt.setNull(90, 1);
        else {
          stmt.setString(90, orderItems[i].getCrowno());
        }
        if (orderItems[i].getNorgtaxprice() == null)
          stmt.setNull(91, 4);
        else {
          stmt.setBigDecimal(91, orderItems[i].getNorgtaxprice().toBigDecimal());
        }
        if (orderItems[i].getNorgnettaxprice() == null)
          stmt.setNull(92, 4);
        else {
          stmt.setBigDecimal(92, orderItems[i].getNorgnettaxprice().toBigDecimal());
        }

        if (orderItems[i].getVproducenum() == null)
          stmt.setNull(93, 1);
        else {
          stmt.setString(93, orderItems[i].getVproducenum());
        }

        if (orderItems[i].getCcontractid() == null)
          stmt.setNull(94, 1);
        else {
          stmt.setString(94, orderItems[i].getCcontractid());
        }

        if (orderItems[i].getCcontractrowid() == null)
          stmt.setNull(95, 1);
        else {
          stmt.setString(95, orderItems[i].getCcontractrowid());
        }

        if (orderItems[i].getCcontractrcode() == null)
          stmt.setNull(96, 1);
        else {
          stmt.setString(96, orderItems[i].getCcontractrcode());
        }

        if (orderItems[i].getVpriceauditcode() == null)
          stmt.setNull(97, 1);
        else {
          stmt.setString(97, orderItems[i].getVpriceauditcode());
        }

        if (orderItems[i].getCpriceauditid() == null)
          stmt.setNull(98, 1);
        else {
          stmt.setString(98, orderItems[i].getCpriceauditid());
        }

        if (orderItems[i].getCpriceaudit_bid() == null)
          stmt.setNull(99, 1);
        else {
          stmt.setString(99, orderItems[i].getCpriceaudit_bid());
        }

        if (orderItems[i].getCpriceaudit_bb1id() == null)
          stmt.setNull(100, 1);
        else {
          stmt.setString(100, orderItems[i].getCpriceaudit_bb1id());
        }

        if (orderItems[i].getBomvers() == null)//shikun bomvers
          stmt.setNull(101, 1);
        else {
          stmt.setString(101, orderItems[i].getBomvers());
        }

        stmt.executeUpdate();
      }
      executeBatch(stmt);
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "insertItem", new Object[] { orderItems });

    return key;
  }

  public ArrayList splitForA3AuditFlow(OrderVO[] vos)
    throws SQLException
  {
    ArrayList array = new ArrayList();
    Vector orderVec = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      if ((vos == null) || (vos.length == 0)) {
        return null;
      }
      Vector vTemp = new Vector();
      for (int i = 0; i < vos.length; i++) {
        String ls_tempid = ((OrderHeaderVO)vos[i].getParentVO()).getCorderid();
        vTemp.addElement(ls_tempid);
      }

      String[] sTemp = new String[vTemp.size()];
      vTemp.copyInto(sTemp);

      TempTableDMO dmoTempTbl = new TempTableDMO();
      String strSetId = dmoTempTbl.insertTempTable(sTemp, "t_sc_general", "pk_sc");

      Hashtable lh_status = new Hashtable();
      String sql = "select corderid, ibillstatus  from sc_order where corderid  in ";
      sql = sql + strSetId;

      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        String corderid = rs.getString("corderid");
        corderid = corderid == null ? null : corderid.trim();

        Integer ibillstatus = (Integer)rs.getObject("ibillstatus");
        ibillstatus = ibillstatus == null ? null : ibillstatus;

        lh_status.put(corderid, ibillstatus);
      }

      for (int i = 0; i < vos.length; i++)
      {
        Object lobj_temp = lh_status.get(((OrderHeaderVO)vos[i].getParentVO()).getCorderid());

        if (((Integer)lobj_temp).intValue() == 3)
          if (vos[i].getChildrenVO().length == 1)
            orderVec.addElement(vos[i]);
          else
            for (int j = 0; j < vos[i].getChildrenVO().length; j++) {
              OrderVO tempVO = new OrderVO();
              tempVO.setParentVO((OrderHeaderVO)vos[i].getParentVO().clone());
              OrderItemVO[] items = new OrderItemVO[1];
              items[0] = ((OrderItemVO)vos[i].getChildrenVO()[j]);
              tempVO.setChildrenVO(items);
              orderVec.addElement(tempVO);
            }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      throw new SQLException(e.getMessage());
    } finally {
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
    for (int i = 0; i < orderVec.size(); i++) {
      array.add(orderVec.get(i));
    }
    return array;
  }

  public void updateItems(OrderItemVO[] orderItems)
    throws SQLException
  {
    beforeCallMethod("nc.bs.sc.order.OrderDMO", "updateItems", new Object[] { orderItems });

    String sql = "update sc_order_b set corderid = ?, pk_corp = ?, cmangid = ?, cbaseid = ?, nordernum = ?," +
    		" cassistunit = ?, nassistnum = ?, ndiscountrate = ?, idiscounttaxtype = ?, ntaxrate = ?," +
    		" ccurrencytypeid = ?, noriginalnetprice = ?, noriginalcurprice = ?, noriginalcurmny = ?," +
    		" noriginaltaxmny = ?, noriginalsummny = ?, nexchangeotobrate = ?, ntaxmny = ?, nmoney = ?," +
    		" nsummny = ?, nexchangeotoarate = ?, nassistcurmny = ?, nassisttaxmny = ?, nassistsummny = ?," +
    		" naccumarrvnum = ?, naccumstorenum = ?, naccuminvoicenum = ?, naccumwastnum = ?, dplanarrvdate = ?," +
    		" cwarehouseid = ?, creceiveaddress = ?, cprojectid = ?, cprojectphaseid = ?, coperator = ?," +
    		" forderrowstatus = ?, bisactive = ?, cordersource = ?, csourcebillid = ?, csourcebillrow = ?," +
    		" cupsourcebilltype = ?, cupsourcebillid = ?, cupsourcebillrowid = ?, vmemo = ?, vfree1 = ?, vfree2 = ?," +
    		" vfree3 = ?, vfree4 = ?, vfree5 = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?," +
    		" vdef7 = ?, vdef8 = ?, vdef9 = ?, vdef10 = ?, vdef11 = ?, vdef12 = ?, vdef13 = ?, vdef14 = ?, vdef15 = ?," +
    		" vdef16 = ?, vdef17 = ?, vdef18 = ?, vdef19 = ?, vdef20 = ?, pk_defdoc1 = ?, pk_defdoc2 = ?, pk_defdoc3 = ?," +
    		" pk_defdoc4 = ?, pk_defdoc5 = ?, pk_defdoc6 = ?, pk_defdoc7 = ?, pk_defdoc8 = ?, pk_defdoc9 = ?," +
    		" pk_defdoc10 = ?, pk_defdoc11 = ?, pk_defdoc12 = ?, pk_defdoc13 = ?, pk_defdoc14 = ?, pk_defdoc15 = ?," +
    		" pk_defdoc16 = ?, pk_defdoc17 = ?, pk_defdoc18 = ?, pk_defdoc19 = ?, pk_defdoc20 = ?, crowno = ?," +
    		" norgtaxprice = ?, norgnettaxprice = ?, vproducenum = ?, ccontractid = ?, ccontractrowid = ?, ccontractrcode = ?," +
    		" vpriceauditcode = ?, cpriceauditid = ?, cpriceaudit_bid = ?, cpriceaudit_bb1id = ?, bomvers = ?" +
    		" where corder_bid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      for (int i = 0; i < orderItems.length; i++)
      {
        if (orderItems[i].getCorderid() == null)
          stmt.setNull(1, 1);
        else {
          stmt.setString(1, orderItems[i].getCorderid());
        }
        if (orderItems[i].getPk_corp() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, orderItems[i].getPk_corp());
        }
        if (orderItems[i].getCmangid() == null)
          stmt.setNull(3, 1);
        else {
          stmt.setString(3, orderItems[i].getCmangid());
        }
        if (orderItems[i].getCbaseid() == null)
          stmt.setNull(4, 1);
        else {
          stmt.setString(4, orderItems[i].getCbaseid());
        }
        if (orderItems[i].getNordernum() == null)
          stmt.setNull(5, 4);
        else {
          stmt.setBigDecimal(5, orderItems[i].getNordernum().toBigDecimal());
        }
        if (orderItems[i].getCassistunit() == null)
          stmt.setNull(6, 1);
        else {
          stmt.setString(6, orderItems[i].getCassistunit());
        }
        if (orderItems[i].getNassistnum() == null)
          stmt.setNull(7, 4);
        else {
          stmt.setBigDecimal(7, orderItems[i].getNassistnum().toBigDecimal());
        }
        if (orderItems[i].getNdiscountrate() == null)
          stmt.setNull(8, 4);
        else {
          stmt.setBigDecimal(8, orderItems[i].getNdiscountrate().toBigDecimal());
        }
        if (orderItems[i].getIdiscounttaxtype() == null)
          stmt.setNull(9, 4);
        else {
          stmt.setInt(9, orderItems[i].getIdiscounttaxtype().intValue());
        }
        if (orderItems[i].getNtaxrate() == null)
          stmt.setNull(10, 4);
        else {
          stmt.setBigDecimal(10, orderItems[i].getNtaxrate().toBigDecimal());
        }
        if (orderItems[i].getCcurrencytypeid() == null)
          stmt.setNull(11, 1);
        else {
          stmt.setString(11, orderItems[i].getCcurrencytypeid());
        }
        if (orderItems[i].getNoriginalnetprice() == null)
          stmt.setNull(12, 4);
        else {
          stmt.setBigDecimal(12, orderItems[i].getNoriginalnetprice().toBigDecimal());
        }
        if (orderItems[i].getNoriginalcurprice() == null)
          stmt.setNull(13, 4);
        else {
          stmt.setBigDecimal(13, orderItems[i].getNoriginalcurprice().toBigDecimal());
        }
        if (orderItems[i].getNoriginalcurmny() == null)
          stmt.setNull(14, 4);
        else {
          stmt.setBigDecimal(14, orderItems[i].getNoriginalcurmny().toBigDecimal());
        }
        if (orderItems[i].getNoriginaltaxmny() == null)
          stmt.setNull(15, 4);
        else {
          stmt.setBigDecimal(15, orderItems[i].getNoriginaltaxmny().toBigDecimal());
        }
        if (orderItems[i].getNoriginalsummny() == null)
          stmt.setNull(16, 4);
        else {
          stmt.setBigDecimal(16, orderItems[i].getNoriginalsummny().toBigDecimal());
        }
        if (orderItems[i].getNexchangeotobrate() == null)
          stmt.setNull(17, 4);
        else {
          stmt.setBigDecimal(17, orderItems[i].getNexchangeotobrate().toBigDecimal());
        }
        if (orderItems[i].getNtaxmny() == null)
          stmt.setNull(18, 4);
        else {
          stmt.setBigDecimal(18, orderItems[i].getNtaxmny().toBigDecimal());
        }
        if (orderItems[i].getNmoney() == null)
          stmt.setNull(19, 4);
        else {
          stmt.setBigDecimal(19, orderItems[i].getNmoney().toBigDecimal());
        }
        if (orderItems[i].getNsummny() == null)
          stmt.setNull(20, 4);
        else {
          stmt.setBigDecimal(20, orderItems[i].getNsummny().toBigDecimal());
        }
        if (orderItems[i].getNexchangeotoarate() == null)
          stmt.setNull(21, 4);
        else {
          stmt.setBigDecimal(21, orderItems[i].getNexchangeotoarate().toBigDecimal());
        }
        if (orderItems[i].getNassistcurmny() == null)
          stmt.setNull(22, 4);
        else {
          stmt.setBigDecimal(22, orderItems[i].getNassistcurmny().toBigDecimal());
        }
        if (orderItems[i].getNassisttaxmny() == null)
          stmt.setNull(23, 4);
        else {
          stmt.setBigDecimal(23, orderItems[i].getNassisttaxmny().toBigDecimal());
        }
        if (orderItems[i].getNassistsummny() == null)
          stmt.setNull(24, 4);
        else {
          stmt.setBigDecimal(24, orderItems[i].getNassistsummny().toBigDecimal());
        }
        if (orderItems[i].getNaccumarrvnum() == null)
          stmt.setNull(25, 4);
        else {
          stmt.setBigDecimal(25, orderItems[i].getNaccumarrvnum().toBigDecimal());
        }
        if (orderItems[i].getNaccumstorenum() == null)
          stmt.setNull(26, 4);
        else {
          stmt.setBigDecimal(26, orderItems[i].getNaccumstorenum().toBigDecimal());
        }
        if (orderItems[i].getNaccuminvoicenum() == null)
          stmt.setNull(27, 4);
        else {
          stmt.setBigDecimal(27, orderItems[i].getNaccuminvoicenum().toBigDecimal());
        }
        if (orderItems[i].getNaccumwastnum() == null)
          stmt.setNull(28, 4);
        else {
          stmt.setBigDecimal(28, orderItems[i].getNaccumwastnum().toBigDecimal());
        }
        if (orderItems[i].getDplanarrvdate() == null)
          stmt.setNull(29, 1);
        else {
          stmt.setString(29, orderItems[i].getDplanarrvdate().toString());
        }
        if (orderItems[i].getCwarehouseid() == null)
          stmt.setNull(30, 1);
        else {
          stmt.setString(30, orderItems[i].getCwarehouseid());
        }
        if (orderItems[i].getCreceiveaddress() == null)
          stmt.setNull(31, 1);
        else {
          stmt.setString(31, orderItems[i].getCreceiveaddress());
        }
        if (orderItems[i].getCprojectid() == null)
          stmt.setNull(32, 1);
        else {
          stmt.setString(32, orderItems[i].getCprojectid());
        }
        if (orderItems[i].getCprojectphaseid() == null)
          stmt.setNull(33, 1);
        else {
          stmt.setString(33, orderItems[i].getCprojectphaseid());
        }
        if (orderItems[i].getCoperator() == null)
          stmt.setNull(34, 1);
        else {
          stmt.setString(34, orderItems[i].getCoperator());
        }
        if (orderItems[i].getForderrowstatus() == null)
          stmt.setNull(35, 4);
        else {
          stmt.setInt(35, orderItems[i].getForderrowstatus().intValue());
        }
        if (orderItems[i].getBisactive() == null)
          stmt.setNull(36, 1);
        else {
          stmt.setString(36, orderItems[i].getBisactive().booleanValue() ? "0" : "1");
        }
        if (orderItems[i].getCordersource() == null)
          stmt.setNull(37, 1);
        else {
          stmt.setString(37, orderItems[i].getCordersource());
        }
        if (orderItems[i].getCsourcebillid() == null)
          stmt.setNull(38, 1);
        else {
          stmt.setString(38, orderItems[i].getCsourcebillid());
        }
        if (orderItems[i].getCsourcebillrow() == null)
          stmt.setNull(39, 1);
        else {
          stmt.setString(39, orderItems[i].getCsourcebillrow());
        }
        if (orderItems[i].getCupsourcebilltype() == null)
          stmt.setNull(40, 1);
        else {
          stmt.setString(40, orderItems[i].getCupsourcebilltype());
        }
        if (orderItems[i].getCupsourcebillid() == null)
          stmt.setNull(41, 1);
        else {
          stmt.setString(41, orderItems[i].getCupsourcebillid());
        }
        if (orderItems[i].getCupsourcebillrowid() == null)
          stmt.setNull(42, 1);
        else {
          stmt.setString(42, orderItems[i].getCupsourcebillrowid());
        }
        if (orderItems[i].getVmemo() == null)
          stmt.setNull(43, 1);
        else {
          stmt.setString(43, orderItems[i].getVmemo());
        }
        if (orderItems[i].getVfree1() == null)
          stmt.setNull(44, 1);
        else {
          stmt.setString(44, orderItems[i].getVfree1());
        }
        if (orderItems[i].getVfree2() == null)
          stmt.setNull(45, 1);
        else {
          stmt.setString(45, orderItems[i].getVfree2());
        }
        if (orderItems[i].getVfree3() == null)
          stmt.setNull(46, 1);
        else {
          stmt.setString(46, orderItems[i].getVfree3());
        }
        if (orderItems[i].getVfree4() == null)
          stmt.setNull(47, 1);
        else {
          stmt.setString(47, orderItems[i].getVfree4());
        }
        if (orderItems[i].getVfree5() == null)
          stmt.setNull(48, 1);
        else {
          stmt.setString(48, orderItems[i].getVfree5());
        }
        if (orderItems[i].getVdef1() == null)
          stmt.setNull(49, 1);
        else {
          stmt.setString(49, orderItems[i].getVdef1());
        }
        if (orderItems[i].getVdef2() == null)
          stmt.setNull(50, 1);
        else {
          stmt.setString(50, orderItems[i].getVdef2());
        }
        if (orderItems[i].getVdef3() == null)
          stmt.setNull(51, 1);
        else {
          stmt.setString(51, orderItems[i].getVdef3());
        }
        if (orderItems[i].getVdef4() == null)
          stmt.setNull(52, 1);
        else {
          stmt.setString(52, orderItems[i].getVdef4());
        }
        if (orderItems[i].getVdef5() == null)
          stmt.setNull(53, 1);
        else {
          stmt.setString(53, orderItems[i].getVdef5());
        }
        if (orderItems[i].getVdef6() == null)
          stmt.setNull(54, 1);
        else {
          stmt.setString(54, orderItems[i].getVdef6());
        }
        if (orderItems[i].getVdef7() == null) {
          stmt.setNull(55, 1);
        }
        else {
          stmt.setString(55, orderItems[i].getVdef7());
        }
        if (orderItems[i].getVdef8() == null) {
          stmt.setNull(56, 1);
        }
        else {
          stmt.setString(56, orderItems[i].getVdef8());
        }
        if (orderItems[i].getVdef9() == null) {
          stmt.setNull(57, 1);
        }
        else {
          stmt.setString(57, orderItems[i].getVdef9());
        }
        if (orderItems[i].getVdef10() == null) {
          stmt.setNull(58, 1);
        }
        else {
          stmt.setString(58, orderItems[i].getVdef10());
        }
        if (orderItems[i].getVdef11() == null)
          stmt.setNull(59, 1);
        else {
          stmt.setString(59, orderItems[i].getVdef11());
        }
        if (orderItems[i].getVdef12() == null)
          stmt.setNull(60, 1);
        else {
          stmt.setString(60, orderItems[i].getVdef12());
        }
        if (orderItems[i].getVdef13() == null)
          stmt.setNull(61, 1);
        else {
          stmt.setString(61, orderItems[i].getVdef13());
        }
        if (orderItems[i].getVdef14() == null)
          stmt.setNull(62, 1);
        else {
          stmt.setString(62, orderItems[i].getVdef14());
        }
        if (orderItems[i].getVdef15() == null)
          stmt.setNull(63, 1);
        else {
          stmt.setString(63, orderItems[i].getVdef15());
        }
        if (orderItems[i].getVdef16() == null)
          stmt.setNull(64, 1);
        else {
          stmt.setString(64, orderItems[i].getVdef16());
        }
        if (orderItems[i].getVdef17() == null)
          stmt.setNull(65, 1);
        else {
          stmt.setString(65, orderItems[i].getVdef17());
        }
        if (orderItems[i].getVdef18() == null)
          stmt.setNull(66, 1);
        else {
          stmt.setString(66, orderItems[i].getVdef18());
        }
        if (orderItems[i].getVdef19() == null)
          stmt.setNull(67, 1);
        else {
          stmt.setString(67, orderItems[i].getVdef19());
        }
        if (orderItems[i].getVdef20() == null)
          stmt.setNull(68, 1);
        else {
          stmt.setString(68, orderItems[i].getVdef20());
        }
        if (orderItems[i].getPKDefDoc1() == null)
          stmt.setNull(69, 1);
        else {
          stmt.setString(69, orderItems[i].getPKDefDoc1());
        }
        if (orderItems[i].getPKDefDoc2() == null)
          stmt.setNull(70, 1);
        else {
          stmt.setString(70, orderItems[i].getPKDefDoc2());
        }
        if (orderItems[i].getPKDefDoc3() == null)
          stmt.setNull(71, 1);
        else {
          stmt.setString(71, orderItems[i].getPKDefDoc3());
        }
        if (orderItems[i].getPKDefDoc4() == null)
          stmt.setNull(72, 1);
        else {
          stmt.setString(72, orderItems[i].getPKDefDoc4());
        }
        if (orderItems[i].getPKDefDoc5() == null)
          stmt.setNull(73, 1);
        else {
          stmt.setString(73, orderItems[i].getPKDefDoc5());
        }
        if (orderItems[i].getPKDefDoc6() == null)
          stmt.setNull(74, 1);
        else {
          stmt.setString(74, orderItems[i].getPKDefDoc6());
        }
        if (orderItems[i].getPKDefDoc7() == null)
          stmt.setNull(75, 1);
        else {
          stmt.setString(75, orderItems[i].getPKDefDoc7());
        }
        if (orderItems[i].getPKDefDoc8() == null)
          stmt.setNull(76, 1);
        else {
          stmt.setString(76, orderItems[i].getPKDefDoc8());
        }
        if (orderItems[i].getPKDefDoc9() == null)
          stmt.setNull(77, 1);
        else {
          stmt.setString(77, orderItems[i].getPKDefDoc9());
        }
        if (orderItems[i].getPKDefDoc10() == null)
          stmt.setNull(78, 1);
        else {
          stmt.setString(78, orderItems[i].getPKDefDoc10());
        }
        if (orderItems[i].getPKDefDoc11() == null)
          stmt.setNull(79, 1);
        else {
          stmt.setString(79, orderItems[i].getPKDefDoc11());
        }
        if (orderItems[i].getPKDefDoc12() == null)
          stmt.setNull(80, 1);
        else {
          stmt.setString(80, orderItems[i].getPKDefDoc12());
        }
        if (orderItems[i].getPKDefDoc13() == null)
          stmt.setNull(81, 1);
        else {
          stmt.setString(81, orderItems[i].getPKDefDoc13());
        }
        if (orderItems[i].getPKDefDoc14() == null)
          stmt.setNull(82, 1);
        else {
          stmt.setString(82, orderItems[i].getPKDefDoc14());
        }
        if (orderItems[i].getPKDefDoc15() == null)
          stmt.setNull(83, 1);
        else {
          stmt.setString(83, orderItems[i].getPKDefDoc15());
        }
        if (orderItems[i].getPKDefDoc16() == null)
          stmt.setNull(84, 1);
        else {
          stmt.setString(84, orderItems[i].getPKDefDoc16());
        }
        if (orderItems[i].getPKDefDoc17() == null)
          stmt.setNull(85, 1);
        else {
          stmt.setString(85, orderItems[i].getPKDefDoc17());
        }
        if (orderItems[i].getPKDefDoc18() == null)
          stmt.setNull(86, 1);
        else {
          stmt.setString(86, orderItems[i].getPKDefDoc18());
        }
        if (orderItems[i].getPKDefDoc19() == null)
          stmt.setNull(87, 1);
        else {
          stmt.setString(87, orderItems[i].getPKDefDoc19());
        }
        if (orderItems[i].getPKDefDoc20() == null)
          stmt.setNull(88, 1);
        else {
          stmt.setString(88, orderItems[i].getPKDefDoc20());
        }

        if (orderItems[i].getCrowno() == null)
          stmt.setNull(89, 1);
        else {
          stmt.setString(89, orderItems[i].getCrowno());
        }
        if (orderItems[i].getNorgtaxprice() == null)
          stmt.setNull(90, 4);
        else {
          stmt.setBigDecimal(90, orderItems[i].getNorgtaxprice().toBigDecimal());
        }
        if (orderItems[i].getNorgnettaxprice() == null)
          stmt.setNull(91, 4);
        else {
          stmt.setBigDecimal(91, orderItems[i].getNorgnettaxprice().toBigDecimal());
        }

        if (orderItems[i].getVproducenum() == null)
          stmt.setNull(92, 1);
        else {
          stmt.setString(92, orderItems[i].getVproducenum());
        }

        if (orderItems[i].getCcontractid() == null)
          stmt.setNull(93, 1);
        else {
          stmt.setString(93, orderItems[i].getCcontractid());
        }

        if (orderItems[i].getCcontractrowid() == null)
          stmt.setNull(94, 1);
        else {
          stmt.setString(94, orderItems[i].getCcontractrowid());
        }

        if (orderItems[i].getCcontractrcode() == null)
          stmt.setNull(95, 1);
        else {
          stmt.setString(95, orderItems[i].getCcontractrcode());
        }

        if (orderItems[i].getVpriceauditcode() == null)
          stmt.setNull(96, 1);
        else {
          stmt.setString(96, orderItems[i].getVpriceauditcode());
        }

        if (orderItems[i].getCpriceauditid() == null)
          stmt.setNull(97, 1);
        else {
          stmt.setString(97, orderItems[i].getCpriceauditid());
        }

        if (orderItems[i].getCpriceaudit_bid() == null)
          stmt.setNull(98, 1);
        else {
          stmt.setString(98, orderItems[i].getCpriceaudit_bid());
        }

        if (orderItems[i].getCpriceaudit_bb1id() == null)
          stmt.setNull(99, 1);
        else {
          stmt.setString(99, orderItems[i].getCpriceaudit_bb1id());
        }

        if (orderItems[i].getBomvers() == null)//shikun bomvers
          stmt.setNull(100, 1);
        else {
          stmt.setString(100, orderItems[i].getBomvers());
        }

        stmt.setString(101, orderItems[i].getPrimaryKey());

        stmt.executeUpdate();
      }
      executeBatch(stmt);
    } finally {
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

    afterCallMethod("nc.bs.sc.order.OrderDMO", "updateItems", new Object[] { orderItems });
  }

  public AggregatedValueObject[] findVOsBack(CircularlyAccessibleValueObject[] headers)
    throws BusinessException
  {
    try
    {
      return (OrderVO[])findVOsBack((OrderHeaderVO[])(OrderHeaderVO[])headers);
    }
    catch (SQLException e) {
      SCMEnv.out(e);
    }
    return null;
  }

  public String getCTState(String id)
    throws SQLException
  {
    if (id == null) {
      return null;
    }
    String sql = "";

    sql = "select ts from ct_manage where dr=0 and activeflag=0 and ctflag=2 and bsc='Y' and pk_ct_manage = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    String ts = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (id != null) {
        stmt.setString(1, id);
      }

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
        ts = rs.getString(1);
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
      catch (Exception e) {
      }
    }
    return ts;
  }
}