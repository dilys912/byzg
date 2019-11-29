package nc.impl.so.sointerface;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.sm.createcorp.CreatecorpDMO;
import nc.impl.scm.so.pub.FetchValueDMO;
import nc.impl.scm.so.so001.SaleOrderDMO;
import nc.impl.scm.so.so003.SendMsgImpl;
import nc.impl.scm.so.so008.OosinfoDMO;
import nc.impl.scm.so.so016.SOToolsDMO;
import nc.itf.ic.service.IICToPU_StoreadminDMO;
import nc.itf.so.service.ISOToIC_DRP;
import nc.itf.uap.pf.IPFConfig;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.so005.SaleItemVO;
import nc.vo.so.so008.OosinfoItemVO;

public class SaleToICDRPDMO extends DataManageObject
  implements ISOToIC_DRP
{
  HashMap hssendtype = null;

  public SaleToICDRPDMO()
    throws NamingException, SystemException
  {
  }

  public SaleToICDRPDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  private UFDouble assignOOSNum(OosinfoItemVO[] itemVO, UFDouble nnum, UFDate date, String operid)
    throws Exception
  {
    if (itemVO == null) {
      return null;
    }
    OosinfoDMO dmo = new OosinfoDMO();

    for (int i = 0; i < itemVO.length; i++) {
      UFDouble oldnum = itemVO[i].getNnumber();
      UFDouble fillnumber = itemVO[i].getNfillnumber();
      if (fillnumber == null)
        fillnumber = new UFDouble(0);
      if (nnum.doubleValue() >= oldnum.sub(fillnumber).doubleValue())
      {
        itemVO[i].setNfillnumber(oldnum);
        itemVO[i].setBfillflag(new UFBoolean(true));
        itemVO[i].setDfilldate(date);
        nnum = nnum.sub(oldnum.sub(fillnumber));
        try
        {
          SendMsgImpl mess = new SendMsgImpl();

          String id = itemVO[i].getCsaleid();

          String custid = itemVO[i].getCcustomerid();

          String invid = itemVO[i].getCinventoryid();

          String sid = operid;

          String rid = itemVO[i].getCoperatorid();
          mess.send(id, custid, invid, sid, rid);
        } catch (Exception e) {
          SCMEnv.out("消息触发失败！");
          SCMEnv.out(e.getMessage());
          throw e;
        }
      } else {
        itemVO[i].setNfillnumber(nnum.add(fillnumber));
        itemVO[i].setBfillflag(new UFBoolean(false));
        itemVO[i].setDfilldate(null);
        nnum = new UFDouble(0);
      }

      dmo.updateOosinItem(itemVO[i]);

      if (nnum.doubleValue() <= 0.0D) {
        return null;
      }
    }

    return nnum;
  }

  /** @deprecated */
  private String getInvoiceSourceBillTypeID(String strDetailID)
    throws SQLException, BusinessException
  {
    String strSourceID = null;

    String strSQL = "Select cupreceipttype from so_saleinvoice_b Where cinvoice_bid ";

    strSQL = strSQL + " =  ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, strDetailID);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        strSourceID = rs.getString(1);
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
    return strSourceID;
  }

  /** @deprecated */
  private String[] getInvoiceSourceID(String strBillType, String strDetailID)
    throws SQLException, BusinessException
  {
    String[] strSourceID = null;
    String strField = null;

    String strSQL = "Select cupsourcebillid,cupsourcebillbodyid from ";

    if ((strBillType.equals("30")) || (strBillType.equals("3A")))
    {
      strSQL = strSQL + " so_saleorder_b ";
      strField = "corder_bid";
    }

    if ((strBillType.equals("32")) || (strBillType.equals("3C")))
    {
      strSQL = strSQL + " so_saleinvoice_b ";
      strField = "cinvoice_bid";
    }

    if (strBillType.equals("31")) {
      strSQL = strSQL + " so_salereceipt_b ";
      strField = "creceipt_bid";
    }

    strSQL = strSQL + " Where " + strField + " =  ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, strDetailID);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        strSourceID = new String[2];
        strSourceID[0] = rs.getString(1);
        strSourceID[1] = rs.getString(2);
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
    return strSourceID;
  }

  /** @deprecated */
  public UFDouble[] getONReceiptNum(String pk_corp, String pk_calbody, String pk_invmandoc, UFBoolean bisFree, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5, String vfree6, String vfree7, String vfree8, String vfree9, String vfree10, String startDate, String endDate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_corp.trim().equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000034"));
    }
    if ((pk_calbody == null) || (pk_calbody.trim().equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000035"));
    }
    if ((pk_invmandoc == null) || (pk_invmandoc.trim().equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000036"));
    }
    if ((bisFree == null) || (bisFree.toString().trim().equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000037"));
    }
    if ((startDate == null) || (startDate.equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000038"));
    }
    if ((endDate == null) || (endDate.equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000039"));
    }

    UFDouble[] retNums = null;
    UFDouble retNUm = null;
    UFDate start = null;
    UFDate end = null;
    try {
      start = new UFDate(startDate);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000040"));
    }
    try {
      end = new UFDate(endDate);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000041"));
    }
    String selectpart = "select ";
    String selectpart1 = " sum(so_salereceipt_b.nnumber - (case when so_saleexecute.ntotalinventorynumber is null then isnull(so_saleexecute.ntotalreceiptnumber,0) else isnull(so_saleexecute.ntotalinventorynumber,0) end))";
    String frompart = "  From so_salereceipt inner JOIN  so_salereceipt_b ON so_salereceipt.csaleid = so_salereceipt_b.csaleid inner JOIN  so_saleexecute ON so_salereceipt.csaleid = so_saleexecute.csaleid AND ";
    frompart = frompart + " so_salereceipt_b.creceipt_bid = so_saleexecute.csale_bid";

    String where = " and so_salereceipt.fstatus = 2";

    where = where + " and (so_salereceipt_b.nnumber>so_saleexecute.ntotalinventorynumber or so_saleexecute.ntotalinventorynumber is null ) ";

    String sql = selectpart + selectpart1 + frompart + where;

    String wherepart = " and so_salereceipt.pk_corp ='" + pk_corp + "' ";
    wherepart = wherepart + " and so_salereceipt.ccalbodyid  ='" + pk_calbody + "' ";
    wherepart = wherepart + " and so_salereceipt_b.cinventoryid  = '" + pk_invmandoc + "' ";

    wherepart = wherepart + " and  so_salereceipt.creceipttype  = '31'  ";

    if (bisFree.booleanValue()) {
      if (vfree1 == null)
        wherepart = wherepart + " and (so_saleexecute.vfree1 is null or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree1 = '" + vfree1 + "' ";
      }
      if (vfree2 == null)
        wherepart = wherepart + " and (so_saleexecute.vfree2 is null  or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree2 = '" + vfree2 + "' ";
      }
      if (vfree3 == null)
        wherepart = wherepart + " and (so_saleexecute.vfree3 is null or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree3 = '" + vfree3 + "' ";
      }
      if (vfree4 == null)
        wherepart = wherepart + " and (so_saleexecute.vfree4 is null or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree4 = '" + vfree4 + "' ";
      }
      if (vfree5 == null)
        wherepart = wherepart + " and (so_saleexecute.vfree5 is null or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree5 = '" + vfree5 + "' ";
      }
    }

    Connection con = null;
    ResultSet rs = null;
    PreparedStatement stmt = null;
    Vector vrslt = new Vector();
    String sql0 = sql;
    String sql1 = "";
    String wherepart0 = wherepart;
    String wherepart1 = wherepart;
    try {
      con = getConnection();

      wherepart0 = wherepart0 + " and so_salereceipt.dbilldate < ? ";
      sql0 = sql0 + wherepart0;

      stmt = con.prepareStatement(sql0);
      stmt.setString(1, start.toString());
      rs = stmt.executeQuery();
      retNUm = null;
      while (rs.next()) {
        BigDecimal bretNum = rs.getBigDecimal(1);
        retNUm = bretNum == null ? new UFDouble(0) : new UFDouble(bretNum);
      }

      vrslt.addElement(retNUm);

      sql1 = selectpart + " so_salereceipt.dbilldate, " + selectpart1 + frompart + where + wherepart1 + " and so_salereceipt.dbilldate >= '" + start + "' and so_salereceipt.dbilldate <= '" + endDate + "'";

      sql1 = sql1 + "group by so_salereceipt.dbilldate";
      stmt = con.prepareStatement(sql1);
      rs = stmt.executeQuery();
      Vector tempDate = new Vector();
      Vector tempV = new Vector();
      while (rs.next()) {
        String d = rs.getString(1);
        UFDate date = d == null ? null : new UFDate(d.trim());
        tempDate.addElement(date);
        BigDecimal n = rs.getBigDecimal(2);
        UFDouble num = n == null ? new UFDouble(0) : new UFDouble(n);
        tempV.addElement(num);
      }
      rs.close();
      UFDate currDate = null;
      for (int i = 0; i <= end.getDaysAfter(start); i++) {
        retNUm = null;
        currDate = start.getDateAfter(i);
        int ind = tempDate.indexOf(currDate);
        if (ind >= 0) {
          retNUm = (UFDouble)tempV.get(ind);
        }
        vrslt.addElement(retNUm);
      }
      if (vrslt.size() > 0) {
        retNums = new UFDouble[vrslt.size()];
        vrslt.copyInto(retNums);
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      SCMEnv.out(e.getMessage());
    }
    finally {
      if (stmt != null) {
        stmt.close();
      }
      if (con != null) {
        con.close();
      }

    }

    afterCallMethod("nc.bs.pu.ic.ToICDMO", "getOnPONum", new Object[] { pk_corp, pk_calbody, pk_invmandoc, bisFree, vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10, startDate, endDate });

    return retNums;
  }

  /** @deprecated */
  public UFDouble[] getOnSONum(String pk_corp, String pk_calbody, String pk_invmandoc, UFBoolean bisFree, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5, String vfree6, String vfree7, String vfree8, String vfree9, String vfree10, String startDate, String endDate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_corp.trim().equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000034"));
    }
    if ((pk_calbody == null) || (pk_calbody.trim().equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000035"));
    }
    if ((pk_invmandoc == null) || (pk_invmandoc.trim().equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000036"));
    }
    if ((bisFree == null) || (bisFree.toString().trim().equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000037"));
    }
    if ((startDate == null) || (startDate.equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000038"));
    }
    if ((endDate == null) || (endDate.equals(""))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000039"));
    }

    UFDouble[] retNums = null;
    UFDouble retNUm = null;
    UFDate start = null;
    UFDate end = null;
    try {
      start = new UFDate(startDate);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000040"));
    }
    try {
      end = new UFDate(endDate);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000041"));
    }
    String selectpart = "select ";
    String selectpart1 = " sum(so_saleorder_b.nnumber - (case when so_saleexecute.ntotalreceiptnumber is null  then isnull(so_saleexecute.ntotalinventorynumber,0) else isnull(so_saleexecute.ntotalreceiptnumber,0) end)) From ";

    String frompart = "  so_sale ,so_saleorder_b,so_saleexecute where so_sale.csaleid = so_saleorder_b.csaleid  and so_sale.csaleid = so_saleexecute.csaleid ";

    frompart = frompart + " AND  so_saleorder_b.corder_bid = so_saleexecute.csale_bid and ((so_saleorder_b.creceipttype != '4H' and so_saleorder_b.creceipttype != '42') or so_saleorder_b.creceipttype is null) ";

    String where = " AND (so_sale.fstatus != 5 and  so_sale.fstatus !=6)  and so_saleorder_b.beditflag = 'N' ";

    where = where + " and ((so_saleorder_b.nnumber>so_saleexecute.ntotalinventorynumber or so_saleexecute.ntotalinventorynumber is null ) or (so_saleorder_b.nnumber>so_saleexecute.ntotalreceiptnumber or so_saleexecute.ntotalreceiptnumber is null ))";

    String sql = selectpart + selectpart1 + frompart + where;

    String wherepart = " and so_sale.pk_corp ='" + pk_corp + "' ";
    wherepart = wherepart + " and so_sale.ccalbodyid  ='" + pk_calbody + "' ";
    wherepart = wherepart + " and so_saleorder_b.cinventoryid  = '" + pk_invmandoc + "' ";

    wherepart = wherepart + " and  (so_sale.creceipttype  = '30' or so_sale.creceipttype  = '3A')  ";

    if (bisFree.booleanValue()) {
      if (vfree1 == null)
        wherepart = wherepart + " and so_saleexecute.vfree1 is null ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree1 = '" + vfree1 + "' ";
      }
      if (vfree2 == null)
        wherepart = wherepart + " and so_saleexecute.vfree2 is null ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree2 = '" + vfree2 + "' ";
      }
      if (vfree3 == null)
        wherepart = wherepart + " and so_saleexecute.vfree3 is null ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree3 = '" + vfree3 + "' ";
      }
      if (vfree4 == null)
        wherepart = wherepart + " and so_saleexecute.vfree4 is null ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree4 = '" + vfree4 + "' ";
      }
      if (vfree5 == null)
        wherepart = wherepart + " and so_saleexecute.vfree5 is null ";
      else {
        wherepart = wherepart + " and so_saleexecute.vfree5 = '" + vfree5 + "' ";
      }
    }

    Connection con = null;
    ResultSet rs = null;
    PreparedStatement stmt = null;
    Vector vrslt = new Vector();
    String sql0 = sql;
    String sql1 = "";
    String wherepart0 = wherepart;
    String wherepart1 = wherepart;
    try {
      con = getConnection();

      wherepart0 = wherepart0 + " and so_saleorder_b.dconsigndate < ? ";
      sql0 = sql0 + wherepart0;
      stmt = con.prepareStatement(sql0);
      stmt.setString(1, start.toString());
      rs = stmt.executeQuery();
      retNUm = null;
      while (rs.next()) {
        BigDecimal bretNum = rs.getBigDecimal(1);
        retNUm = bretNum == null ? new UFDouble(0) : new UFDouble(bretNum);
      }

      vrslt.addElement(retNUm);

      sql1 = selectpart + " so_saleorder_b.dconsigndate, " + selectpart1 + frompart + where + wherepart1 + " and so_saleorder_b.dconsigndate >= '" + start + "' and so_saleorder_b.dconsigndate <= '" + endDate + "'";

      sql1 = sql1 + " group by so_saleorder_b.dconsigndate";
      stmt = con.prepareStatement(sql1);
      rs = stmt.executeQuery();
      Vector tempDate = new Vector();
      Vector tempV = new Vector();
      while (rs.next()) {
        String d = rs.getString(1);
        UFDate date = d == null ? null : new UFDate(d.trim());
        tempDate.addElement(date);
        BigDecimal n = rs.getBigDecimal(2);
        UFDouble num = n == null ? new UFDouble(0) : new UFDouble(n);
        System.out.println("**********************" + num + "***********************************************************");

        tempV.addElement(num);
      }
      rs.close();
      UFDate currDate = null;
      for (int i = 0; i <= end.getDaysAfter(start); i++) {
        retNUm = null;
        currDate = start.getDateAfter(i);
        int ind = tempDate.indexOf(currDate);
        if (ind >= 0) {
          retNUm = (UFDouble)tempV.get(ind);
        }
        vrslt.addElement(retNUm);
      }
      if (vrslt.size() > 0) {
        retNums = new UFDouble[vrslt.size()];
        vrslt.copyInto(retNums);
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      SCMEnv.out(e.getMessage());
    }
    finally {
      if (stmt != null) {
        stmt.close();
      }
      if (con != null) {
        con.close();
      }

    }

    afterCallMethod("nc.bs.pu.ic.ToICDMO", "getOnPONum", new Object[] { pk_corp, pk_calbody, pk_invmandoc, bisFree, vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10, startDate, endDate });

    return retNums;
  }

  /** @deprecated */
  public UFDouble getOutNum(String salebillType, String SaleID, String SaleDetailID)
    throws BusinessException
  {
    UFDouble ntotalinventorynumber = null;
    String strSQL = "Select ntotalinventorynumber From so_saleexecute where creceipttype = ? and csaleid = ? and corder_bid = ? ";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, salebillType);

      stmt.setString(2, SaleID);

      stmt.setString(3, SaleDetailID);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        BigDecimal n = rs.getBigDecimal(1);
        ntotalinventorynumber = n == null ? new UFDouble(0) : new UFDouble(n);
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
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
      catch (Exception e)
      {
      }
    }
    return ntotalinventorynumber;
  }

  /** @deprecated */
  public UFDouble getOutSourceNum(Object[] obj)
    throws SQLException, BusinessException, Exception
  {
    String SQLBussiness = null;
    String[] arrBussiness = null;

    IPFConfig bsPfConfig = (IPFConfig)NCLocator.getInstance().lookup(IPFConfig.class.getName());
    arrBussiness = bsPfConfig.getBusitypeByCorpAndStyle(obj[0].toString(), "W");

    if (arrBussiness != null) {
      for (int i = 0; i < arrBussiness.length; i++) {
        if (SQLBussiness == null) {
          SQLBussiness = " so_sale.cbiztype = '" + arrBussiness[i].toString() + "'";
        }
        else {
          SQLBussiness = SQLBussiness + " or so_sale.cbiztype = '" + arrBussiness[i].toString() + "'";
        }

      }

      if (SQLBussiness != null)
        SQLBussiness = " AND (" + SQLBussiness + " ) ";
      else {
        return new UFDouble(0);
      }

      SCMEnv.out("委托代销业务类型:" + SQLBussiness);
    }
    else {
      return new UFDouble(0);
    }

    if (obj == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000042"));
    }
    UFDouble dbltrust = null;

    String SQLTrust = "";

    String SQLSubWhere = "";

    String SQLGroupBy = "";

    SQLTrust = "SELECT sum(ISNULL(so_saleexecute.ntotalinventorynumber, 0) - ISNULL(so_saleexecute.ntotalbalancenumber, 0)) AS dblTrustQuantity ";
    SQLTrust = SQLTrust + " FROM so_sale inner JOIN  so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid inner JOIN ";
    SQLTrust = SQLTrust + "so_saleexecute ON so_sale.csaleid = so_saleexecute.csaleid AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

    SQLTrust = SQLTrust + " WHERE so_sale.creceipttype = '30' AND (so_saleexecute.bifpaybalance = 'N' or so_saleexecute.bifpaybalance is NULL) and \tso_sale.fstatus = 2";

    if (obj[0] != null) {
      SQLSubWhere = SQLSubWhere + " and so_sale.pk_corp ='" + obj[0].toString() + "' ";
    }

    if (obj[1] != null) {
      SQLSubWhere = SQLSubWhere + "and so_sale.ccalbodyid ='" + obj[1].toString() + "' ";
    }

    if (obj[2] != null) {
      SQLSubWhere = SQLSubWhere + "and so_saleorder_b.cinventoryid ='" + obj[2].toString() + "' ";
    }

    if (obj[14] != null) {
      SQLSubWhere = SQLSubWhere + "and so_saleorder_b.cbatchid ='" + obj[14].toString() + "' ";
    }

    if (((UFBoolean)obj[3]).booleanValue()) {
      if (obj[4] == null)
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree1 is null ";
      else {
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree1 = '" + obj[4].toString() + "' ";
      }

      if (obj[5] == null)
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree2 is null ";
      else {
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree2 = '" + obj[5].toString() + "' ";
      }

      if (obj[6] == null)
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree3 is null ";
      else {
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree3 = '" + obj[6].toString() + "' ";
      }

      if (obj[7] == null)
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree4 is null ";
      else {
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree4 = '" + obj[7].toString() + "' ";
      }

      if (obj[8] == null)
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree5 is null ";
      else {
        SQLSubWhere = SQLSubWhere + "and so_saleexecute.vfree5 = '" + obj[8].toString() + "' ";
      }

    }

    SQLTrust = SQLTrust + SQLSubWhere + SQLBussiness + SQLGroupBy;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(SQLTrust);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        BigDecimal n = rs.getBigDecimal("dblTrustQuantity");
        dbltrust = n == null ? new UFDouble(0) : new UFDouble(n);
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
    return dbltrust;
  }

  /** @deprecated */
  public UFDouble getPreSaleNum(UFDate StartDay, UFDate EndDay, String PKCalbody, String InvID)
    throws SQLException, BusinessException
  {
    UFDouble nnumber = null;
    String strSQL = "Select sum(so_saleorder_b.nnumber - isnull(so_saleexecute.ntotalinventorynumber,0)) FROM so_saleexecute RIGHT OUTER JOIN so_saleorder_b ON so_saleexecute.csale_bid = so_saleorder_b.corder_bid AND so_saleexecute.csaleid = so_saleorder_b.csaleid RIGHT OUTER JOIN so_sale ON so_saleorder_b.csaleid = so_sale.csaleid where so_sale.dbilldate >= ? and so_sale.dbilldate <= ? and so_sale.ccalbodyid = ? and so_saleorder_b.cinvbasdocid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      if (StartDay != null)
        stmt.setString(1, StartDay.toString());
      else {
        stmt.setNull(1, 1);
      }
      if (EndDay != null)
        stmt.setString(2, EndDay.toString());
      else {
        stmt.setNull(2, 1);
      }
      stmt.setString(3, PKCalbody);

      stmt.setString(4, InvID);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        BigDecimal n = rs.getBigDecimal(1);
        nnumber = n == null ? new UFDouble(0) : new UFDouble(n);
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
    return nnumber;
  }

  /** @deprecated */
  public UFDouble getSaleNum(UFDate StartDay, UFDate EndDay, String PKCalbody, String InvID)
    throws SQLException, BusinessException
  {
    UFDouble nnumber = null;
    String strSQL = "Select sum(so_saleexecute.ntotalinventorynumber) FROM so_saleexecute RIGHT OUTER JOIN so_saleorder_b ON so_saleexecute.csaleid = so_saleorder_b.csaleid AND so_saleexecute.csale_bid = so_saleorder_b.corder_bid RIGHT OUTER JOIN so_sale ON so_saleorder_b.csaleid = so_sale.csaleid where so_sale.dbilldate >= ? and so_sale.dbilldate <= ? and so_sale.ccalbodyid = ? and so_saleorder_b.cinventoryid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      if (StartDay != null)
        stmt.setString(1, StartDay.toString());
      else {
        stmt.setNull(1, 1);
      }
      if (EndDay != null)
        stmt.setString(2, EndDay.toString());
      else {
        stmt.setNull(2, 1);
      }
      stmt.setString(3, PKCalbody);

      stmt.setString(4, InvID);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        BigDecimal n = rs.getBigDecimal(1);
        nnumber = n == null ? new UFDouble(0) : new UFDouble(n);
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
    return nnumber;
  }

  public SaleOrderVO[] getSaleOrderFromDrp(Object bids)
    throws BusinessException, SQLException
  {
    if ((bids == null) && ((bids instanceof ArrayList))) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000043"));
    }

    ArrayList alBids = (ArrayList)bids;

    StringBuffer sbfWhereStr = new StringBuffer();
    for (int i = 0; i < alBids.size(); i++)
    {
      if (alBids.get(i) != null) {
        if (sbfWhereStr.toString().trim().equals("")) {
          sbfWhereStr.append(" where (csourcebillbodyid='");
          sbfWhereStr.append((String)alBids.get(i));
          sbfWhereStr.append("' ");
        } else {
          sbfWhereStr.append(" or csourcebillbodyid='");
          sbfWhereStr.append((String)alBids.get(i));
          sbfWhereStr.append("' ");
        }
      }
    }
    if (!sbfWhereStr.toString().trim().equals(""))
    {
      sbfWhereStr.append(")");
      return getSaleOrders(sbfWhereStr.toString());
    }
    throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000043"));
  }

  private SaleOrderVO[] getSaleOrders(String WhereStr)
    throws SQLException
  {
    if (WhereStr != null) {
      WhereStr = WhereStr + " and so_saleorder_b.frowstatus != " + 5;
    }

    StringBuffer sbfSQL = new StringBuffer();
    sbfSQL.append("SELECT so_sale.csaleid,so_sale.pk_corp, so_sale.vreceiptcode,");

    sbfSQL.append("so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass,");

    sbfSQL.append("so_sale.finvoicetype, so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate,");

    sbfSQL.append("so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, ");

    sbfSQL.append("so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid,");

    sbfSQL.append("so_sale.vreceiveaddress, so_sale.creceiptcorpid, so_sale.ctransmodeid, ");

    sbfSQL.append("so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag,");

    sbfSQL.append("so_sale.cfreecustid, so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, ");

    sbfSQL.append("so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, so_sale.dapprovedate,");

    sbfSQL.append("so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6,");

    sbfSQL.append("so_sale.vdef7,so_sale.vdef8, so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,");

    sbfSQL.append("so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,so_sale.breceiptendflag, ");

    sbfSQL.append("so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, ");

    sbfSQL.append("so_saleorder_b.creceipttype, so_saleorder_b.csourcebillid, so_saleorder_b.csourcebillbodyid,");

    sbfSQL.append("so_saleorder_b.cinventoryid, so_saleorder_b.cunitid, so_saleorder_b.cpackunitid,");

    sbfSQL.append("so_saleorder_b.nnumber, so_saleorder_b.npacknumber, so_saleorder_b.cbodywarehouseid,");

    sbfSQL.append("so_saleorder_b.dconsigndate, so_saleorder_b.ddeliverdate, so_saleorder_b.blargessflag,");

    sbfSQL.append("so_saleorder_b.ceditsaleid, so_saleorder_b.beditflag, so_saleorder_b.veditreason,");

    sbfSQL.append("so_saleorder_b.ccurrencytypeid, so_saleorder_b.nitemdiscountrate, so_saleorder_b.ndiscountrate,");

    sbfSQL.append("so_saleorder_b.nexchangeotobrate, so_saleorder_b.nexchangeotoarate, so_saleorder_b.ntaxrate,");

    sbfSQL.append("so_saleorder_b.noriginalcurprice, so_saleorder_b.noriginalcurtaxprice, so_saleorder_b.noriginalcurnetprice,");

    sbfSQL.append("so_saleorder_b.noriginalcurtaxnetprice, so_saleorder_b.noriginalcurtaxmny, so_saleorder_b.noriginalcurmny,");

    sbfSQL.append("so_saleorder_b.noriginalcursummny, so_saleorder_b.noriginalcurdiscountmny, so_saleorder_b.nprice,");

    sbfSQL.append("so_saleorder_b.ntaxprice, so_saleorder_b.nnetprice, so_saleorder_b.ntaxnetprice,");

    sbfSQL.append("so_saleorder_b.ntaxmny, so_saleorder_b.nmny, so_saleorder_b.nsummny,");

    sbfSQL.append("so_saleorder_b.ndiscountmny, so_saleorder_b.coperatorid, so_saleorder_b.frowstatus,");

    sbfSQL.append("so_saleorder_b.frownote, so_saleorder_b.fbatchstatus,so_saleorder_b.cbomorderid ");

    sbfSQL.append("FROM so_saleorder_b LEFT OUTER JOIN ");
    sbfSQL.append("so_sale ON so_saleorder_b.csaleid = so_sale.csaleid ");
    sbfSQL.append(WhereStr);
    sbfSQL.append(" order by so_sale.csaleid ");

    SaleorderHVO saleHeader = null;
    SaleOrderVO[] hvos = null;
    SaleOrderVO hvo = null;

    SaleorderBVO[] items = null;
    SaleorderBVO saleItem = null;
    ArrayList alitems = null;
    ArrayList alHvos = new ArrayList();
    SCMEnv.out(sbfSQL.toString());

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sbfSQL.toString());

      ResultSet rs = stmt.executeQuery();

      String headerid = "";
      while (rs.next()) {
        saleItem = new SaleorderBVO();

        String Cdemandbillhid = rs.getString(1);
        if (!Cdemandbillhid.equals(headerid)) {
          headerid = Cdemandbillhid;
          if ((saleHeader != null) && (alitems != null) && (alitems.size() > 0))
          {
            items = new SaleorderBVO[alitems.size()];
            for (int i = 0; i < alitems.size(); i++) {
              if (alitems.get(i) != null) {
                items[i] = ((SaleorderBVO)alitems.get(i));
              }
            }
            hvo = new SaleOrderVO();
            hvo.setParentVO(saleHeader);
            if ((items != null) && (items.length > 0)) {
              hvo.setChildrenVO(items);
              alHvos.add(hvo);
            }
          }
          alitems = new ArrayList();
          saleHeader = new SaleorderHVO();
          saleHeader.setCsaleid(Cdemandbillhid == null ? null : Cdemandbillhid.trim());

          String pk_corp = rs.getString(2);
          saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

          String vreceiptcode = rs.getString(3);
          saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

          String creceipttype = rs.getString(4);
          saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

          String cbiztype = rs.getString(5);
          saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

          Integer finvoiceclass = (Integer)rs.getObject(6);
          saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);

          Integer finvoicetype = (Integer)rs.getObject(7);
          saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);

          String vaccountyear = rs.getString(8);
          saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());

          String binitflag = rs.getString(9);
          saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));

          String dbilldate = rs.getString(10);
          saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));

          String ccustomerid = rs.getString(11);
          saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

          String cdeptid = rs.getString(12);
          saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

          String cemployeeid = rs.getString(13);
          saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

          String coperatorid = rs.getString(14);
          saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

          String ctermprotocolid = rs.getString(15);
          saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());

          String csalecorpid = rs.getString(16);
          saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());

          String creceiptcustomerid = rs.getString(17);
          saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());

          String vreceiveaddress = rs.getString(18);
          saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

          String creceiptcorpid = rs.getString(19);
          saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

          String ctransmodeid = rs.getString(20);
          saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

          BigDecimal ndiscountrate = (BigDecimal)rs.getObject(21);
          saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

          String cwarehouseid = rs.getString(22);
          saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

          String veditreason = rs.getString(23);
          saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());

          String bfreecustflag = rs.getString(24);
          saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));

          String cfreecustid = rs.getString(25);
          saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

          Integer ibalanceflag = (Integer)rs.getObject(26);
          saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);

          BigDecimal nsubscription = (BigDecimal)rs.getObject(27);
          saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription));

          String ccreditnum = rs.getString(28);
          saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());

          BigDecimal nevaluatecarriage = (BigDecimal)rs.getObject(29);

          saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));

          String dmakedate = rs.getString(30);
          saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));

          String capproveid = rs.getString(31);
          saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

          String dapprovedate = rs.getString(32);
          saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));

          Integer fstatus = (Integer)rs.getObject(33);
          saleHeader.setFstatus(fstatus == null ? null : fstatus);

          String vnote = rs.getString(34);
          saleHeader.setVnote(vnote == null ? null : vnote.trim());

          String vdef1 = rs.getString(35);
          saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

          String vdef2 = rs.getString(36);
          saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

          String vdef3 = rs.getString(37);
          saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

          String vdef4 = rs.getString(38);
          saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

          String vdef5 = rs.getString(39);
          saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

          String vdef6 = rs.getString(40);
          saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

          String vdef7 = rs.getString(41);
          saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

          String vdef8 = rs.getString(42);
          saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

          String vdef9 = rs.getString(43);
          saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

          String vdef10 = rs.getString(44);
          saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

          String ccalbodyid = rs.getString(45);
          saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

          String bretinvflag = rs.getString(46);
          saleHeader.setBretinvflag(bretinvflag == null ? null : new UFBoolean(bretinvflag.trim()));

          String boutendflag = rs.getString(47);
          saleHeader.setBoutendflag(boutendflag == null ? null : new UFBoolean(boutendflag.trim()));

          String binvoicendflag = rs.getString(48);
          saleHeader.setBinvoicendflag(binvoicendflag == null ? null : new UFBoolean(binvoicendflag.trim()));

          String breceiptendflag = rs.getString(49);
          saleHeader.setBreceiptendflag(breceiptendflag == null ? null : new UFBoolean(breceiptendflag.trim()));
        }

        String corder_bid = rs.getString(50);
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString(51);
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString(52);
        saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());

        String creceipttype = rs.getString(53);
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString(54);
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString(55);
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinventoryid = rs.getString(56);
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString(57);
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString(58);
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject(59);
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject(60);
        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        String cbodywarehouseid = rs.getString(61);
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String dconsigndate = rs.getString(62);
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString(63);
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString(64);
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String ceditsaleid = rs.getString(65);
        saleItem.setCeditsaleid(ceditsaleid == null ? null : ceditsaleid.trim());

        String beditflag = rs.getString(66);
        saleItem.setBeditflag(beditflag == null ? null : new UFBoolean(beditflag.trim()));

        String veditreason = rs.getString(67);
        saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());

        String ccurrencytypeid = rs.getString(68);
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject(69);
        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject(70);
        saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject(71);
        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject(72);
        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject(73);
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject(74);
        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject(75);
        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject(76);
        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject(77);

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject(78);
        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject(79);
        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject(80);
        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject(81);

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject(82);
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject(83);
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject(84);
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject(85);
        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject(86);
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject(87);
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject(88);
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject(89);
        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        String coperatorid = rs.getString(90);
        saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        Integer frowstatus = (Integer)rs.getObject(91);
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString(92);
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        Integer fbatchstatus = (Integer)rs.getObject(93);
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String cbomorderid = rs.getString(94);
        saleItem.setCbomorderid(cbomorderid == null ? null : cbomorderid.trim());

        alitems.add(saleItem);
      }

      if ((saleHeader != null) && (alitems != null) && (alitems.size() > 0)) {
        items = new SaleorderBVO[alitems.size()];
        for (int i = 0; i < alitems.size(); i++) {
          if (alitems.get(i) != null) {
            items[i] = ((SaleorderBVO)alitems.get(i));
          }
        }
        hvo = new SaleOrderVO();
        hvo.setParentVO(saleHeader);
        if ((items != null) && (items.length > 0)) {
          hvo.setChildrenVO(items);
          alHvos.add(hvo);
        }
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
    SaleOrderVO[] vos = null;
    if ((alHvos != null) && (alHvos.size() > 0)) {
      vos = new SaleOrderVO[alHvos.size()];
      for (int i = 0; i < alHvos.size(); i++) {
        if (alHvos.get(i) != null) {
          vos[i] = ((SaleOrderVO)alHvos.get(i));
        }
      }
      return vos;
    }
    return null;
  }

  /** @deprecated */
  private String getSourceBillTypeID(String strDetailID)
    throws SQLException, BusinessException
  {
    String strSourceID = null;

    String strSQL = "Select creceipttype from so_saleinvoice_b Where cinvoice_bid ";

    strSQL = strSQL + " =  ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, strDetailID);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        strSourceID = rs.getString(1);
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
    return strSourceID;
  }

  /** @deprecated */
  private String[] getSourceID(String strBillType, String strDetailID)
    throws SQLException, BusinessException
  {
    String[] strSourceID = null;
    String strField = null;
    String strSQL = "Select csourcebillid,csourcebillbodyid from ";

    if ((strBillType.equals("30")) || (strBillType.equals("3A")))
    {
      strSQL = strSQL + " so_saleorder_b ";
      strField = "corder_bid";
    }

    if ((strBillType.equals("32")) || (strBillType.equals("3C")))
    {
      strSQL = strSQL + " so_saleinvoice_b ";
      strField = "cinvoice_bid";
    }

    if (strBillType.equals("31")) {
      strSQL = strSQL + " so_salereceipt_b ";
      strField = "creceipt_bid";
    }

    if (strBillType.equals("7F")) {
      strSQL = " Select pkorder, pkorderrow from dm_delivbill_b ";
      strField = "pk_delivbill_b";
    }

    if (strBillType.equals("7D")) {
      strSQL = " Select pkbillh, pkbillb from dm_delivdaypl ";
      strField = "pk_delivdaypl";
    }
    strSQL = strSQL + " Where " + strField + " =  ?";
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      stmt.setString(1, strDetailID);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        strSourceID = new String[2];
        strSourceID[0] = rs.getString(1);
        strSourceID[1] = rs.getString(2);
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
    return strSourceID;
  }

  /** @deprecated */
  public boolean isSaleOut(String SaleID, String SaleDetailID)
    throws BusinessException
  {
    String SQLRelation = "SELECT ntotalbalancenumber FROM so_saleexecute WHERE ";
    SQLRelation = SQLRelation + " csaleid = '" + SaleID + "'";
    SQLRelation = SQLRelation + "  and csale_bid = '" + SaleDetailID + "'";

    BigDecimal dblNumber = new BigDecimal(0);

    Connection con = null;
    PreparedStatement stmt = null;

    UFBoolean bResult = new UFBoolean(false);
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQLRelation);

      ResultSet rstNumber = stmt.executeQuery();

      if (rstNumber.next())
      {
        Object o = rstNumber.getObject("ntotalbalancenumber");

        if (o != null)
        {
          dblNumber = new BigDecimal(o.toString());

          bResult = dblNumber.doubleValue() == 0.0D ? new UFBoolean(false) : new UFBoolean(true);
        }
        else
        {
          bResult = new UFBoolean(false);
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
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
      catch (Exception e)
      {
      }
    }
    return bResult.booleanValue();
  }

  /** @deprecated */
  public boolean isSaleOutFor4Hand42(String SaleID, String SaleDetailID)
    throws SQLException, BusinessException
  {
    String SQLRelation = "SELECT csaleid FROM so_saleorder_b WHERE ";
    SQLRelation = SQLRelation + " csaleid = '" + SaleID + "'";
    SQLRelation = SQLRelation + "  and corder_bid = '" + SaleDetailID + "'";

    SQLRelation = SQLRelation + "  and  (creceipttype = " + "3A" + "or creceipttype = " + "30" + ")";

    SQLRelation = SQLRelation + "  and frowstatus = " + 5;

    Connection con = null;
    PreparedStatement stmt = null;

    UFBoolean bResult = new UFBoolean(false);
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQLRelation);

      ResultSet rstNumber = stmt.executeQuery();

      if (rstNumber.next())
      {
        bResult = rstNumber.getString("csaleid") == null ? new UFBoolean(false) : new UFBoolean(true);
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return bResult.booleanValue();
  }

  /** @deprecated */
  private boolean isSaleOverOut(String IC003, UFDouble IC004, String SaleDetailID, UFDouble SaleOutNum)
    throws SQLException, BusinessException
  {
    String SQLRelation = "SELECT so_saleorder_b.nnumber, (CASE WHEN so_saleexecute.ntotalinventorynumber IS NULL  THEN 0 ELSE so_saleexecute.ntotalinventorynumber END) AS inventorynumber  ";
    SQLRelation = SQLRelation + " ,nouttoplimit ";
    SQLRelation = SQLRelation + " FROM so_saleorder_b ,so_saleexecute  Where  so_saleorder_b.csaleid = so_saleexecute.csaleid AND  so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

    SQLRelation = SQLRelation + "  and csale_bid = '" + SaleDetailID + "'";

    BigDecimal dblNumber = new BigDecimal(0);
    BigDecimal dblinventorynumber = new BigDecimal(0);

    UFDouble nouttoplimit = null;

    Connection con = null;
    PreparedStatement stmt = null;

    UFBoolean bResult = new UFBoolean(false);
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQLRelation);

      ResultSet rstNumber = stmt.executeQuery();

      if (rstNumber.next())
      {
        Object oNnumber = rstNumber.getObject("nnumber");
        Object oInventorynumber = rstNumber.getObject("inventorynumber");

        nouttoplimit = rstNumber.getObject("nouttoplimit") == null ? IC004 : new UFDouble(rstNumber.getObject("nouttoplimit").toString());

        if ((oNnumber != null) && (oInventorynumber != null))
        {
          dblNumber = new BigDecimal(oNnumber.toString());
          dblinventorynumber = new BigDecimal(oInventorynumber.toString());

          SCMEnv.out("nouttoplimit:" + nouttoplimit.doubleValue());
          SCMEnv.out("dblNumber:" + dblNumber.doubleValue());
          SCMEnv.out("dblinventorynumber:" + dblinventorynumber.doubleValue());

          System.out.println("SaleOutNum:" + SaleOutNum.doubleValue());

          if (dblNumber.floatValue() >= 0.0F)
          {
            UFDouble nResult;
          //  UFDouble nResult;
            if (IC003.equals("Y")) {
              nResult = nouttoplimit.multiply(0.01D).add(1.0D).multiply(dblNumber.doubleValue()).sub(dblinventorynumber.doubleValue()).sub(SaleOutNum.doubleValue());
            }
            else
            {
              nResult = new UFDouble(1).multiply(dblNumber.doubleValue()).sub(dblinventorynumber.doubleValue()).sub(SaleOutNum.doubleValue());
            }

            bResult = nResult.doubleValue() >= 0.0D ? new UFBoolean(false) : new UFBoolean(true);
          }
          else
          {
            UFDouble nResult;
            //UFDouble nResult;
            if (IC003.equals("Y")) {
              nResult = nouttoplimit.multiply(0.01D).add(1.0D).multiply(dblNumber.doubleValue()).sub(dblinventorynumber.doubleValue()).sub(SaleOutNum.doubleValue());
            }
            else
            {
              nResult = new UFDouble(1).multiply(dblNumber.doubleValue()).sub(dblinventorynumber.doubleValue()).sub(SaleOutNum.doubleValue());
            }

            bResult = nResult.doubleValue() <= 0.0D ? new UFBoolean(false) : new UFBoolean(true);
          }

        }
        else
        {
          bResult = new UFBoolean(false);
        }
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
    return bResult.booleanValue();
  }

  /** @deprecated */
  public void setDesOutNum(AggregatedValueObject outBillVO)
    throws SQLException, BusinessException, NamingException, SystemException
  {
    String pk_corp = outBillVO.getParentVO().getAttributeValue("pk_corp").toString();

    SysInitDMO initReferDMO = new SysInitDMO();

    String IC003 = initReferDMO.getParaString(pk_corp, "SO23");

    String IC004 = initReferDMO.getParaString(pk_corp, "SO24");

    UFDouble nPercent = IC004 == null ? new UFDouble("0") : new UFDouble(IC004);

    String csourcebillhid = null;

    String csourcebiibid = null;

    String SQLUpdate = "update so_saleexecute set ntotalinventorynumber=isnull(ntotalinventorynumber,0) + ? where csale_bid = ? and csaleid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      for (int i = 0; i < outBillVO.getChildrenVO().length; i++)
      {
        CircularlyAccessibleValueObject bodyVO = outBillVO.getChildrenVO()[i];

        if (bodyVO.getAttributeValue("csourcetype") == null)
          continue;
        csourcebillhid = bodyVO.getAttributeValue("csourcebillhid").toString();

        csourcebiibid = bodyVO.getAttributeValue("csourcebillbid").toString();

        UFDouble noutnum = bodyVO.getAttributeValue("noutnum") == null ? new UFDouble("0") : new UFDouble(bodyVO.getAttributeValue("noutnum").toString());

        if (noutnum.doubleValue() == 0.0D) {
          continue;
        }
        if ((bodyVO.getAttributeValue("csourcetype").equals("30")) || (bodyVO.getAttributeValue("csourcetype").equals("3A")))
        {
          if (isSaleOverOut(IC003, nPercent, csourcebiibid, noutnum))
          {
            BusinessException e = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044"));

            throw e;
          }

          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, csourcebiibid);

          stmt.setString(3, csourcebillhid);
          stmt.executeUpdate();

          stmt.close();
          con.close();
        }
        else if (bodyVO.getAttributeValue("csourcetype").equals("31"))
        {
          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, csourcebiibid);

          stmt.setString(3, csourcebillhid);
          stmt.executeUpdate();

          stmt.close();
          con.close();

          String[] sourceBillID = getSourceID("31", csourcebiibid);

          if (isSaleOverOut(IC003, nPercent, sourceBillID[1], noutnum))
          {
            BusinessException e = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044"));

            throw e;
          }

          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, sourceBillID[1]);

          stmt.setString(3, sourceBillID[0]);
          stmt.executeUpdate();

          stmt.close();
          con.close();
        }
        else
        {
          if ((!bodyVO.getAttributeValue("csourcetype").equals("32")) && (!bodyVO.getAttributeValue("csourcetype").equals("3C")))
          {
            continue;
          }

          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, csourcebiibid);

          stmt.setString(3, csourcebillhid);
          stmt.executeUpdate();

          stmt.close();
          con.close();

          String SourceBillTypeID = getSourceBillTypeID(csourcebiibid);

          if ((!SourceBillTypeID.equals("30")) && (!SourceBillTypeID.equals("3A")))
          {
            continue;
          }

          String[] sourceBillID = getSourceID("32", csourcebiibid);

          if (isSaleOverOut(IC003, nPercent, sourceBillID[1], noutnum))
          {
            BusinessException e = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044"));

            throw e;
          }

          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, sourceBillID[1]);

          stmt.setString(3, sourceBillID[0]);
          stmt.executeUpdate();

          stmt.close();
          con.close();
        }

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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  /** @deprecated */
  public void setLockedFlag(ArrayList sorder_bid, ArrayList cfreezeids)
    throws SQLException, BusinessException, NamingException, SystemException
  {
    if ((sorder_bid == null) || (sorder_bid.size() == 0)) {
      return;
    }
    String SQLUpdate = "update so_saleorder_b set cfreezeid = ? where corder_bid = ?";

    Connection con = getConnection();
    PreparedStatement stmt = con.prepareStatement(SQLUpdate);

    for (int i = 0; i < sorder_bid.size(); i++) {
      if ((String)cfreezeids.get(i) == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, (String)cfreezeids.get(i));
      }
      stmt.setString(2, (String)sorder_bid.get(i));

      stmt.executeUpdate();
    }

    stmt.close();
    con.close();
  }

  /** @deprecated */
  private void setOOSNum(String pk_corp, String ccalbodyid, String cinvid, UFDouble ninnum, UFDate date, String csaleorderno, String operid, String[] freeitem)
    throws Exception
  {
    if (ninnum == null) {
      return;
    }
    if (ninnum.doubleValue() <= 0.0D) {
      return;
    }
    OosinfoDMO dmo = new OosinfoDMO();
    OosinfoItemVO[] itemVO = null;

    String freewhere = "";
    if (freeitem != null) {
      for (int i = 0; i < freeitem.length; i++) {
        if (freeitem[i] == null) {
          freewhere = freewhere + " and (so_oosinfo_b.vfree" + (i + 1) + " is null or so_oosinfo_b.vfree" + (i + 1) + " = '') ";
        }
        else
        {
          freewhere = freewhere + " and so_oosinfo_b.vfree" + (i + 1) + "='" + freeitem[i] + "' ";
        }
      }
    }

    UFDouble nnum = ninnum;

    if (csaleorderno != null)
    {
      String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and so_oosinfo.pk_corp = '" + pk_corp + "' and so_oosinfo_b.bfillflag = 'N' and so_oosinfo.vreceiptcode = '" + csaleorderno + "' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

      itemVO = dmo.queryAllByCondition(where);
      if ((itemVO == null) || (itemVO.length == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000045", null, new String[] { csaleorderno }));
      }

      nnum = assignOOSNum(itemVO, nnum, date, operid);
    }

    if (nnum != null)
    {
      String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '" + pk_corp + "' and  so_oosinfo_b.bfillflag = 'N' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

      itemVO = dmo.queryAllByCondition(where);
      nnum = assignOOSNum(itemVO, nnum, date, operid);
      if ((nnum != null) && (nnum.doubleValue() > 0.0D))
      {
        where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '" + pk_corp + "' and  so_oosinfo_b.bfillflag = 'N' and so_oosinfo_b.boosflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

        itemVO = dmo.queryAllByCondition(where);
        nnum = assignOOSNum(itemVO, nnum, date, operid);
      }
    }
  }

  /** @deprecated */
  public void setOOSNum(GeneralBillVO outBillVO)
    throws Exception
  {
    GeneralBillHeaderVO headVO = outBillVO.getHeaderVO();
    GeneralBillItemVO[] bodyVOs = outBillVO.getItemVOs();

    String pk_corp = headVO.getPk_corp();
    UFDate date = headVO.getDbilldate();
    String operid = headVO.getCregister();
    String calbody = headVO.getPk_calbody();

    for (int i = 0; i < bodyVOs.length; i++) {
      String cinvid = bodyVOs[i].getCinventoryid();
      UFDouble ninnum = bodyVOs[i].getNinnum();

      String csaleorderno = bodyVOs[i].getVproductbatch();

      String[] freeitem = new String[5];

      freeitem[0] = bodyVOs[i].getVfree1();
      freeitem[1] = bodyVOs[i].getVfree2();
      freeitem[2] = bodyVOs[i].getVfree3();
      freeitem[3] = bodyVOs[i].getVfree4();
      freeitem[4] = bodyVOs[i].getVfree5();

      setOOSNum(pk_corp, calbody, cinvid, ninnum, date, csaleorderno, operid, freeitem);
    }
  }

  /** @deprecated */
  public void setOutNum_bak(String pk_corp, String SourceBillTypeID, String csourcebillhid, String csourcebiibid, UFDouble noutnum)
    throws SQLException, BusinessException, NamingException, SystemException
  {
    SysInitDMO initReferDMO = new SysInitDMO();

    String IC003 = initReferDMO.getParaString(pk_corp, "SO23");

    String IC004 = initReferDMO.getParaString(pk_corp, "SO24");
    UFDouble nPercent = IC004 == null ? new UFDouble("0") : new UFDouble(IC004);

    String SQLUpdate = "update so_saleexecute set ntotalinventorynumber=isnull(ntotalinventorynumber,0) + ? where csale_bid = ? and csaleid = ? and creceipttype = '30' ";
    String[] sourceBillID = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      if (SourceBillTypeID != null)
      {
        if ((SourceBillTypeID.equals("30")) || (SourceBillTypeID.equals("3A")))
        {
          if (isSaleOverOut(IC003, nPercent, csourcebiibid, noutnum)) {
            BusinessException e = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044"));

            throw e;
          }
          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, csourcebiibid);

          stmt.setString(3, csourcebillhid);
          stmt.executeUpdate();

          stmt.close();
          con.close();
        }
        else if ((SourceBillTypeID.equals("7F")) || (SourceBillTypeID.equals("7D")))
        {
          if (SourceBillTypeID.equals("7F")) {
            sourceBillID = getSourceID("7F", csourcebiibid);
          }

          if (SourceBillTypeID.equals("7D")) {
            sourceBillID = getSourceID("7D", csourcebiibid);
          }

          if (isSaleOverOut(IC003, nPercent, sourceBillID[1], noutnum))
          {
            BusinessException e = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044"));

            throw e;
          }
          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, sourceBillID[1]);

          stmt.setString(3, sourceBillID[0]);
          stmt.executeUpdate();

          stmt.close();
          con.close();
        }
        else if (SourceBillTypeID.equals("31"))
        {
          SCMEnv.out("*****************" + SourceBillTypeID);

          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, csourcebiibid);

          stmt.setString(3, csourcebillhid);
          stmt.executeUpdate();

          stmt.close();
          con.close();

          sourceBillID = getSourceID("31", csourcebiibid);

          if (isSaleOverOut(IC003, nPercent, sourceBillID[1], noutnum))
          {
            BusinessException e = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044"));

            throw e;
          }
          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, sourceBillID[1]);

          stmt.setString(3, sourceBillID[0]);
          stmt.executeUpdate();

          stmt.close();
          con.close();
        }
        else if ((SourceBillTypeID.equals("32")) || (SourceBillTypeID.equals("3C")))
        {
          con = getConnection();
          stmt = con.prepareStatement(SQLUpdate);
          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setString(2, csourcebiibid);

          stmt.setString(3, csourcebillhid);
          stmt.executeUpdate();

          stmt.close();
          con.close();

          String sSourceBillTypeID = getInvoiceSourceBillTypeID(csourcebiibid);
          SCMEnv.out("_______________________" + SourceBillTypeID);

          if (((sSourceBillTypeID != null) && (sSourceBillTypeID.equals("30"))) || (sSourceBillTypeID.equals("3A")))
          {
            sourceBillID = getInvoiceSourceID("32", csourcebiibid);

            if (isSaleOverOut(IC003, nPercent, sourceBillID[1], noutnum))
            {
              BusinessException e = new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044"));

              throw e;
            }
            con = getConnection();
            stmt = con.prepareStatement(SQLUpdate);
            stmt.setBigDecimal(1, noutnum.toBigDecimal());

            stmt.setString(2, sourceBillID[1]);

            stmt.setString(3, sourceBillID[0]);
            stmt.executeUpdate();

            stmt.close();
            con.close();
          }
        }
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  public void setOutNum(String pk_corp, String SourceBillTypeID, String csourcebillhid, String csourcebiibid, UFDouble noutnum)
    throws BusinessException
  {
    throw new BusinessException("This Interface has been closed,please contact SMA ");
  }

  /** @deprecated */
  public void setOutNum(AggregatedValueObject outBillVO)
    throws BusinessException
  {
    if ((outBillVO == null) || (outBillVO.getParentVO() == null)) {
      return;
    }
    String csourcebiibid = null;

    UFDouble nshouldoutnum = null;

    CircularlyAccessibleValueObject[] billbody = outBillVO.getChildrenVO();
    if ((billbody == null) || (billbody.length <= 0)) {
      return;
    }
    ArrayList alistids = new ArrayList();
    String[] corder_bids = null;

    int i = 0; for (int loop = billbody.length; i < loop; i++) {
      Object cfirsttype = billbody[i].getAttributeValue("cfirsttype");
      if (!"30".equals(cfirsttype))
        continue;
      Object id = billbody[i].getAttributeValue("cfirstbillbid");
      if ((id != null) && (!alistids.contains(id))) {
        alistids.add(id);
      }

    }

    HashMap hsntotalinventorynumber = null;
    if (alistids.size() > 0) {
      corder_bids = (String[])(String[])alistids.toArray(new String[alistids.size()]);
      try {
        hsntotalinventorynumber = SOToolsDMO.getAnyValueUFDouble("so_saleexecute", "ntotalinventorynumber", "csale_bid", corder_bids, " creceipttype = '30' ");
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
        throw new BusinessException(e.getMessage());
      }

    }

    String SQLUpdate = "update so_saleexecute set ntotalinventorynumber=isnull(ntotalinventorynumber,0) + ?,ntotalshouldoutnum= isnull(ntotalshouldoutnum,0) + ? where csale_bid = ? and creceipttype = ?  ";

    String sqlCheck = "select so_saleexecute.csale_bid from so_saleexecute,so_saleinvoice_b where so_saleexecute.csale_bid=so_saleinvoice_b.cinvoice_bid and so_saleexecute.ntotalinventorynumber>so_saleinvoice_b.nnumber and csale_bid = ? and creceipttype = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    PreparedStatement stmtCheck = null;
    try
    {
      con = getConnection();
      stmt = prepareStatement(con, SQLUpdate);
      stmtCheck = prepareStatement(con, sqlCheck);
      for ( i = 0; i < outBillVO.getChildrenVO().length; i++)
      {
        CircularlyAccessibleValueObject bodyVO = outBillVO.getChildrenVO()[i];

        if (bodyVO.getAttributeValue("csourcetype") == null)
        {
          continue;
        }
        UFDouble noutnum = bodyVO.getAttributeValue("noutnum") == null ? new UFDouble("0") : new UFDouble(bodyVO.getAttributeValue("noutnum").toString());

        nshouldoutnum = bodyVO.getAttributeValue("nshouldoutnum") == null ? new UFDouble(0) : new UFDouble(bodyVO.getAttributeValue("nshouldoutnum").toString());

        if ((bodyVO.getAttributeValue("csourcetype").equals("32")) || (bodyVO.getAttributeValue("csourcetype").equals("3C")))
        {
          csourcebiibid = bodyVO.getAttributeValue("csourcebillbid").toString();

          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setBigDecimal(2, nshouldoutnum.toBigDecimal());

          stmt.setString(3, csourcebiibid);

          stmt.setString(4, "32");

          executeUpdate(stmt);

          csourcebiibid = bodyVO.getAttributeValue("cfirstbillbid").toString();

          stmt.setBigDecimal(1, noutnum.toBigDecimal());

          stmt.setBigDecimal(2, nshouldoutnum.toBigDecimal());

          stmt.setString(3, csourcebiibid);

          stmt.setString(4, "30");

          executeUpdate(stmt);
        }
        else
        {
          csourcebiibid = bodyVO.getAttributeValue("cfirstbillbid").toString();

          String csourcetype = bodyVO.getAttributeValue("cfirsttype").toString();

          noutnum = bodyVO.getAttributeValue("noutnum") == null ? new UFDouble("0") : new UFDouble(bodyVO.getAttributeValue("noutnum").toString());

          nshouldoutnum = bodyVO.getAttributeValue("nshouldoutnum") == null ? new UFDouble(0) : new UFDouble(bodyVO.getAttributeValue("nshouldoutnum").toString());

          stmt.setBigDecimal(1, noutnum.toBigDecimal());
          stmt.setBigDecimal(2, nshouldoutnum.toBigDecimal());

          stmt.setString(3, csourcebiibid);

          stmt.setString(4, csourcetype);

          executeUpdate(stmt);
        }

      }

      executeBatch(stmt);

      if ((corder_bids != null) && (corder_bids.length > 0))
      {
        SaleOrderDMO saledmo = new SaleOrderDMO();
        SaleorderBVO[] oldordbvos = (SaleorderBVO[])(SaleorderBVO[])saledmo.queryBodyDataForUpdateStatus(corder_bids);

        if ((oldordbvos == null) || (oldordbvos.length <= 0))
          return;
        if (hsntotalinventorynumber != null)
        {
           i = 0; for (int loop = oldordbvos.length; i < loop; i++) {
            oldordbvos[i].setNtotalinventorynumber_old((UFDouble)hsntotalinventorynumber.get(oldordbvos[i].getCorder_bid()));
          }

        }

        saledmo.processOutState(oldordbvos);

        saledmo.setOrdLastDate("dlastoutdate", oldordbvos, (UFDate)outBillVO.getParentVO().getAttributeValue("dbilldate"));
      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
    finally
    {
      try
      {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  public SaleinvoiceVO[] splitInvoiceVOForIC(SaleinvoiceVO vos)
    throws SQLException, BusinessException, NamingException, SystemException
  {
    SaleinvoiceVO[] resultVOs = null;
    FetchValueDMO soDMO = new FetchValueDMO();
    try
    {
      int i = 0; for (int loop = vos.getChildrenVO().length; i < loop; i++) {
        UFBoolean btranmode = getSendType((String)vos.getChildrenVO()[i].getAttributeValue("csoucebillid"));

        if ((btranmode != null) && (btranmode.booleanValue())) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000047"));
        }
      }

      String splitMode = "仓库";

      String pk_corp = ((nc.vo.so.so002.SaleVO)vos.getParentVO()).getPk_corp();
      try
      {
        SysInitDMO initReferDMO = new SysInitDMO();

        String IC035 = initReferDMO.getParaString(pk_corp, "IC035");
        if (IC035 != null)
          splitMode = IC035;
      } catch (Exception e) {
        throw new BusinessException(e.getMessage());
      }

      Hashtable table = null;
      Vector vCbaseids = new Vector();
      SaleinvoiceBVO[] items = null;
      String[] cBaseids = null;
      items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])vos.getChildrenVO();

      for (int j = 0; j < items.length; j++) {
        if ((items[j].getCbodywarehouseid() != null) && (!items[j].getCbodywarehouseid().trim().equals("")))
          continue;
        if (!vCbaseids.contains(items[j].getCinvbasdocid())) {
          vCbaseids.addElement(items[j].getCinvbasdocid());
        }

      }

      if ((vCbaseids != null) && (vCbaseids.size() > 0)) {
        cBaseids = new String[vCbaseids.size()];
        vCbaseids.copyInto(cBaseids);
        table = soDMO.fetchArrayValue("bd_produce", "pk_stordoc", "pk_invbasdoc", cBaseids);

        if ((table == null) || (table.size() <= 0))
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000048"));
      }
      if ((table != null) && (table.size() > 0)) {
        for (int j = 0; j < items.length; j++) {
          if ((items[j].getCbodywarehouseid() != null) && (items[j].getCbodywarehouseid().trim() != ""))
            continue;
          if (table.containsKey(items[j].getCinvbasdocid())) {
            items[j].setCbodywarehouseid((String)table.get(items[j].getCinvbasdocid()));
          }
          else {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000049"));
          }
        }

      }

      if (splitMode.equalsIgnoreCase("仓库")) {
        resultVOs = (SaleinvoiceVO[])(SaleinvoiceVO[])SplitBillVOs.getSplitVO("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO", "nc.vo.so.so002.SaleinvoiceBVO", vos, null, new String[] { "cbodywarehouseid" });
      }
      else if (splitMode.equalsIgnoreCase("仓库+保管员"))
      {
        String[] cInventoryids = null;
        String[] cWarehouseids = null;
        String cinvid = null;
        String cwarehouseid = null;
        Vector vInvids = new Vector();
        Vector vWarehouseids = new Vector();
        Vector vInvAndStores = new Vector();
        for (int j = 0; j < items.length; j++) {
          cinvid = items[j].getCinventoryid();
          cwarehouseid = items[j].getCbodywarehouseid();
          if ((items[j].getCbodywarehouseid() == null) || (items[j].getCbodywarehouseid().trim() == "") || (vInvAndStores.contains(cinvid + cWarehouseids))) {
            continue;
          }
          vInvids.addElement(cinvid == null ? "" : cinvid);
          vWarehouseids.addElement(cWarehouseids);
        }

        if (vInvAndStores.size() > 0) {
          cInventoryids = new String[vInvids.size()];
          vInvids.copyInto(cInventoryids);
          cWarehouseids = new String[vWarehouseids.size()];
          vWarehouseids.copyInto(cWarehouseids);
        }

        IICToPU_StoreadminDMO storedmo = (IICToPU_StoreadminDMO)NCLocator.getInstance().lookup(IICToPU_StoreadminDMO.class.getName());

        for (int j = 0; j < items.length; j++) {
          cinvid = items[j].getCinventoryid();
          cwarehouseid = items[j].getCbodywarehouseid();
          if ((items[j].getCbodywarehouseid() == null) || (items[j].getCbodywarehouseid().trim() == ""))
            continue;
          items[j].setStoreAdmin(storedmo.getWHManager(pk_corp, null, cwarehouseid, cinvid));
        }

        resultVOs = (SaleinvoiceVO[])(SaleinvoiceVO[])SplitBillVOs.getSplitVO("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO", "nc.vo.so.so002.SaleinvoiceBVO", vos, null, new String[] { "cbodywarehouseid", "cstoreadmin" });
      }
      else if ((splitMode.equalsIgnoreCase("仓库+存货大类")) || (splitMode.equalsIgnoreCase("仓库+存货分类末级")))
      {
        Vector vBaseids = new Vector();
        String cinvid = null;
        String[] cinvids = null;

        for (int j = 0; j < items.length; j++) {
          cinvid = items[j].getCinvbasdocid();
          if (!vBaseids.contains(cinvid))
            vBaseids.addElement(cinvid);
        }
        if (vBaseids.size() > 0) {
          cinvids = new String[vBaseids.size()];
          vBaseids.copyInto(cinvids);
          table = soDMO.fetchArrayValue("bd_invbasdoc", "pk_invcl", "pk_invbasdoc", cinvids);

          for (int j = 0; j < items.length; j++) {
            cinvid = items[j].getCinvbasdocid();
            if (vBaseids.contains(cinvid)) {
              items[j].setInvSort((String)table.get(cinvid));
            }
          }
        }
        resultVOs = (SaleinvoiceVO[])(SaleinvoiceVO[])SplitBillVOs.getSplitVO("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO", "nc.vo.so.so002.SaleinvoiceBVO", vos, null, new String[] { "cbodywarehouseid", "cinvsort" });
      }
      else if (splitMode.equalsIgnoreCase("仓库+按单品"))
      {
        resultVOs = (SaleinvoiceVO[])(SaleinvoiceVO[])SplitBillVOs.getSplitVO("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO", "nc.vo.so.so002.SaleinvoiceBVO", vos, null, new String[] { "cbodywarehouseid", "cinventoryid" });
      }

      if (resultVOs != null) {
        for ( i = 0; i < resultVOs.length; i++) {
          if (resultVOs[i].getChildrenVO() != null) {
            for (int j = 0; j < resultVOs[i].getChildrenVO().length; j++)
              resultVOs[i].getChildrenVO()[j].setStatus(2);
          }
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000054"));
    }
    return resultVOs;
  }

  public nc.vo.so.so005.SaleVO[] splitReceiptVOForIC(nc.vo.so.so005.SaleVO vos)
    throws SQLException, BusinessException, NamingException, SystemException
  {
    nc.vo.so.so005.SaleVO[] resultVOs = null;
    try
    {
      String splitMode = "仓库";

      Hashtable table = null;
      Vector vCbaseids = new Vector();
      SaleItemVO[] items = null;
      String[] cBaseids = null;

      items = (SaleItemVO[])(SaleItemVO[])vos.getChildrenVO();
      for (int j = 0; j < items.length; j++) {
        if ((items[j].getCbodywarehouseid() != null) && (items[j].getCbodywarehouseid().trim() != ""))
          continue;
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000055"));
      }

      if (splitMode.equalsIgnoreCase("仓库")) {
        resultVOs = (nc.vo.so.so005.SaleVO[])(nc.vo.so.so005.SaleVO[])SplitBillVOs.getSplitVO("nc.vo.so.so005.SaleVO", "nc.vo.so.so005.SaleHeaderVO", "nc.vo.so.so005.SaleItemVO", vos, null, new String[] { "cbodywarehouseid" });

        if (resultVOs != null) {
          for (int i = 0; i < resultVOs.length; i++) {
            if (resultVOs[i].getChildrenVO() != null) {
              for (int j = 0; j < resultVOs[i].getChildrenVO().length; j++)
                resultVOs[i].getChildrenVO()[j].setStatus(2);
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000056"));
    }
    return resultVOs;
  }

  public SaleOrderVO[] splitVOForIC(SaleOrderVO vos)
    throws SQLException, BusinessException, NamingException, SystemException
  {
    FetchValueDMO soDMO = new FetchValueDMO();
    SaleOrderVO[] resultVOs = null;

    if ((vos == null) || (vos.getChildrenVO() == null) || (vos.getChildrenVO().length <= 0))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000057"));
    }

    try
    {
      if ((vos.getHeadVO().getBdeliver() != null) && (vos.getHeadVO().getBdeliver().booleanValue()))
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000058"));
      }

      ArrayList volist = new ArrayList();

      int i = 0; for (int loop = vos.getBodyVOs().length; i < loop; i++)
      {
        if ((vos.getBodyVOs()[i].getLaborflag() != null) && (vos.getBodyVOs()[i].getLaborflag().booleanValue())) {
          continue;
        }
        if ((vos.getBodyVOs()[i].getDiscountflag() != null) && (vos.getBodyVOs()[i].getDiscountflag().booleanValue()))
        {
          continue;
        }
        if ((vos.getBodyVOs()[i].getBdericttrans() != null) && (vos.getBodyVOs()[i].getBdericttrans().booleanValue()))
        {
          continue;
        }
        volist.add(vos.getBodyVOs()[i]);
      }

      if (volist.size() <= 0) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000059"));
      }
      vos.setChildrenVO((SaleorderBVO[])(SaleorderBVO[])volist.toArray(new SaleorderBVO[volist.size()]));

      String splitMode = "仓库";
      String pk_corp = ((SaleorderHVO)vos.getParentVO()).getPk_corp();
      try
      {
        SysInitDMO initReferDMO = new SysInitDMO();

        String IC035 = initReferDMO.getParaString(pk_corp, "IC035");
        if (IC035 != null)
          splitMode = IC035;
      } catch (Exception e) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000060"));
      }

      Hashtable table = null;
      Vector vCbaseids = new Vector();
      SaleorderBVO[] items = null;
      String[] cBaseids = null;

      items = (SaleorderBVO[])(SaleorderBVO[])vos.getChildrenVO();

      for (int j = 0; j < items.length; j++) {
        if ((items[j].getCbodywarehouseid() != null) && (!items[j].getCbodywarehouseid().trim().equals("")))
          continue;
        if (!vCbaseids.contains(items[j].getCinvbasdocid())) {
          vCbaseids.addElement(items[j].getCinvbasdocid());
        }

      }

      if ((vCbaseids != null) && (vCbaseids.size() > 0)) {
        cBaseids = new String[vCbaseids.size()];
        vCbaseids.copyInto(cBaseids);
        table = soDMO.fetchArrayValue("bd_produce", "pk_stordoc", "pk_invbasdoc", cBaseids);

        if ((table == null) || (table.size() <= 0)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000048"));
        }
      }
      if ((table != null) && (table.size() > 0)) {
        for (int j = 0; j < items.length; j++) {
          if ((items[j].getCbodywarehouseid() != null) && (items[j].getCbodywarehouseid().trim() != ""))
            continue;
          if (table.containsKey(items[j].getCinvbasdocid())) {
            items[j].setCbodywarehouseid((String)table.get(items[j].getCinvbasdocid()));
          }
          else {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000048"));
          }

        }

      }

      if (splitMode.equalsIgnoreCase("仓库")) {
        resultVOs = (SaleOrderVO[])(SaleOrderVO[])SplitBillVOs.getSplitVO("nc.vo.so.so001.SaleOrderVO", "nc.vo.so.so001.SaleorderHVO", "nc.vo.so.so001.SaleorderBVO", vos, null, new String[] { "cbodywarehouseid" });
      }
      else if (splitMode.equalsIgnoreCase("仓库+保管员"))
      {
        String[] cInventoryids = null;
        String[] cWarehouseids = null;
        String cinvid = null;
        String cwarehouseid = null;

        Vector vInvids = new Vector();
        Vector vWarehouseids = new Vector();
        Vector vInvAndStores = new Vector();

        for (int j = 0; j < items.length; j++) {
          cinvid = items[j].getCinventoryid();
          cwarehouseid = items[j].getCbodywarehouseid();
          if ((items[j].getCbodywarehouseid() == null) || (items[j].getCbodywarehouseid().trim() == "") || (vInvAndStores.contains(cinvid + cWarehouseids))) {
            continue;
          }
          vInvids.addElement(cinvid == null ? "" : cinvid);
          vWarehouseids.addElement(cWarehouseids);
        }

        if (vInvAndStores.size() > 0) {
          cInventoryids = new String[vInvids.size()];
          vInvids.copyInto(cInventoryids);
          cWarehouseids = new String[vWarehouseids.size()];
          vWarehouseids.copyInto(cWarehouseids);
        }

        IICToPU_StoreadminDMO storedmo = (IICToPU_StoreadminDMO)NCLocator.getInstance().lookup(IICToPU_StoreadminDMO.class.getName());

        for (int j = 0; j < items.length; j++) {
          cinvid = items[j].getCinventoryid();
          cwarehouseid = items[j].getCbodywarehouseid();
          if ((items[j].getCbodywarehouseid() == null) || (items[j].getCbodywarehouseid().trim() == ""))
            continue;
          items[j].setStoreAdmin(storedmo.getWHManager(pk_corp, null, cwarehouseid, cinvid));
        }

        resultVOs = (SaleOrderVO[])(SaleOrderVO[])SplitBillVOs.getSplitVO("nc.vo.so.so001.SaleOrderVO", "nc.vo.so.so001.SaleorderHVO", "nc.vo.so.so001.SaleorderBVO", vos, null, new String[] { "cbodywarehouseid", "cstoreadmin" });
      }
      else if ((splitMode.equalsIgnoreCase("仓库+存货大类")) || (splitMode.equalsIgnoreCase("仓库+存货分类末级")))
      {
        Vector vBaseids = new Vector();
        String cinvid = null;
        String[] cinvids = null;

        for (int j = 0; j < items.length; j++) {
          cinvid = items[j].getCinvbasdocid();
          if (!vBaseids.contains(cinvid))
            vBaseids.addElement(cinvid);
        }
        if (vBaseids.size() > 0) {
          cinvids = new String[vBaseids.size()];
          vBaseids.copyInto(cinvids);

          table = soDMO.fetchArrayValue("bd_invbasdoc", "pk_invcl", "pk_invbasdoc", cinvids);

          for (int j = 0; j < items.length; j++) {
            cinvid = items[j].getCinvbasdocid();
            if (vBaseids.contains(cinvid)) {
              items[j].setInvSort((String)table.get(cinvid));
            }
          }
        }
        resultVOs = (SaleOrderVO[])(SaleOrderVO[])SplitBillVOs.getSplitVO("nc.vo.so.so001.SaleOrderVO", "nc.vo.so.so001.SaleorderHVO", "nc.vo.so.so001.SaleorderBVO", vos, null, new String[] { "cbodywarehouseid", "cinvsort" });
      }
      else if (splitMode.equalsIgnoreCase("仓库+按单品"))
      {
        resultVOs = (SaleOrderVO[])(SaleOrderVO[])SplitBillVOs.getSplitVO("nc.vo.so.so001.SaleOrderVO", "nc.vo.so.so001.SaleorderHVO", "nc.vo.so.so001.SaleorderBVO", vos, null, new String[] { "cbodywarehouseid", "cinventoryid" });
      }

      if (resultVOs != null) {
        for ( i = 0; i < resultVOs.length; i++) {
          if (resultVOs[i].getChildrenVO() != null) {
            for (int j = 0; j < resultVOs[i].getChildrenVO().length; j++)
              resultVOs[i].getChildrenVO()[j].setStatus(2);
          }
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000061"));
    }
    return resultVOs;
  }

  private UFDouble assignOOSNum2(OosinfoItemVO[] itemVO, UFDouble nnum, UFDate date, String operid)
    throws Exception
  {
    if (itemVO == null) {
      return null;
    }
    OosinfoDMO dmo = new OosinfoDMO();

    CrossDBConnection con = null;
    PreparedStatement stmt = null;

    String sql = "update so_oosinfo_b set  bfillflag = ?, nfillnumber = ?, dfilldate = ?,noriginalcurtaxnetprice=?,noriginalcursummny=?,cnote=? where coosinfo_bid = ?";
    try
    {
      con = (CrossDBConnection)getConnection();
      con.enableSQLTranslator(false);
      stmt = prepareStatement(con, sql);

      for (int i = 0; i < itemVO.length; i++) {
        UFDouble oldnum = itemVO[i].getNnumber();
        UFDouble fillnumber = itemVO[i].getNfillnumber();
        if (fillnumber == null)
          fillnumber = new UFDouble(0);
        if (nnum.doubleValue() >= oldnum.sub(fillnumber).doubleValue())
        {
          itemVO[i].setNfillnumber(oldnum);
          itemVO[i].setBfillflag(new UFBoolean(true));
          itemVO[i].setDfilldate(date);

          nnum = nnum.sub(oldnum.sub(fillnumber));
          try
          {
            SendMsgImpl mess = new SendMsgImpl();

            String id = itemVO[i].getCsaleid();

            String custid = itemVO[i].getCcustomerid();

            String invid = itemVO[i].getCinventoryid();

            String sid = operid;

            String rid = itemVO[i].getCoperatorid();
            mess.send(id, custid, invid, sid, rid);
          } catch (Exception e) {
            SCMEnv.out("消息触发失败！");
            SCMEnv.out(e.getMessage());
            throw e;
          }
        } else {
          itemVO[i].setNfillnumber(nnum.add(fillnumber));
          itemVO[i].setBfillflag(new UFBoolean(false));
          itemVO[i].setDfilldate(null);

          nnum = new UFDouble(0);
        }

        dmo.updateOosinItem(itemVO[i], stmt);

        if (nnum.doubleValue() <= 0.0D)
        {
          break;
        }
      }
      executeBatch(stmt);
    }
    catch (SQLException e) {
      throw e;
    }

    if (nnum.doubleValue() <= 0.0D) {
      return null;
    }
    return nnum;
  }

  private UFDouble assignOOSNumUnDo(OosinfoItemVO[] itemVO, UFDouble nnum, UFDate date, String operid)
    throws Exception
  {
    if (itemVO == null) {
      return null;
    }
    OosinfoDMO dmo = new OosinfoDMO();

    for (int i = 0; i < itemVO.length; i++) {
      UFDouble fillnumber = itemVO[i].getNfillnumber();
      if (fillnumber == null) {
        fillnumber = new UFDouble(0.0D);
      }
      itemVO[i].setNfillnumber(fillnumber.sub(nnum).doubleValue() < 0.0D ? new UFDouble(0) : fillnumber.sub(nnum));

      itemVO[i].setBfillflag(new UFBoolean(false));
      itemVO[i].setDfilldate(null);

      nnum = nnum.sub(fillnumber);

      dmo.updateOosinItem(itemVO[i]);

      if (nnum.doubleValue() <= 0.0D) {
        return null;
      }
    }

    return nnum;
  }

  private UFDouble assignOOSNumUnDo2(OosinfoItemVO[] itemVO, UFDouble nnum, UFDate date, String operid)
    throws Exception
  {
    if (itemVO == null) {
      return null;
    }
    OosinfoDMO dmo = new OosinfoDMO();

    CrossDBConnection con = null;
    PreparedStatement stmt = null;

    String sql = "update so_oosinfo_b set  bfillflag = ?, nfillnumber = ?, dfilldate = ?,noriginalcurtaxnetprice=?,noriginalcursummny=?,cnote=? where coosinfo_bid = ?";
    try
    {
      con = (CrossDBConnection)getConnection();
      con.enableSQLTranslator(false);
      stmt = prepareStatement(con, sql);

      for (int i = 0; i < itemVO.length; i++) {
        UFDouble fillnumber = itemVO[i].getNfillnumber();
        if (fillnumber == null) {
          fillnumber = new UFDouble(0);
        }
        itemVO[i].setNfillnumber(fillnumber.sub(nnum).doubleValue() < 0.0D ? new UFDouble(0) : fillnumber.sub(nnum));

        itemVO[i].setBfillflag(new UFBoolean(false));
        itemVO[i].setDfilldate(null);

        nnum = nnum.sub(fillnumber);

        dmo.updateOosinItem(itemVO[i], stmt);

        if (nnum.doubleValue() <= 0.0D)
        {
          break;
        }
      }
      executeBatch(stmt);
    }
    catch (SQLException e) {
      throw e;
    }

    if (nnum.doubleValue() <= 0.0D) {
      return null;
    }
    return nnum;
  }

  public Hashtable getOosNumber(ArrayList sID)
    throws BusinessException
  {
    if ((sID == null) || (sID.size() == 0)) {
      return null;
    }

    Hashtable hasnumber = null;

    Object objTemp = null;
    StringBuffer sbfWhereStr = new StringBuffer();
    sbfWhereStr.append(" where so_saleexecute.creceipttype='30' and so_oosinfo_b.bfillflag = 'N' and (1<0 ");

    for (int i = 0; i < sID.size(); i++) {
      sbfWhereStr.append(" or so_oosinfo_b.corder_bid = '");
      sbfWhereStr.append((String)sID.get(i));
      sbfWhereStr.append("' ");
    }
    sbfWhereStr.append(") ");

    StringBuffer sbfSql = new StringBuffer();
    sbfSql.append("SELECT so_oosinfo_b.corder_bid,isnull(so_oosinfo_b.nfillnumber,0)");

    sbfSql.append("FROM so_oosinfo_b ");
    sbfSql.append(sbfWhereStr.toString());
    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sbfSql.toString());
      ResultSet rs = stmt.executeQuery();

      String sbid = "";
      String sCode = "";
      UFDouble dbNum = null;

      while (rs.next()) {
        if (hasnumber == null) {
          hasnumber = new Hashtable();
        }
        sbid = rs.getString(1);

        BigDecimal obj = rs.getBigDecimal(2);
        dbNum = obj == null ? new UFDouble(0) : new UFDouble(obj);

        hasnumber.put(sbid, dbNum);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
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
    return hasnumber;
  }

  public String getOutSourceNumSQL(String corp, String ccalbodyid)
    throws SQLException, BusinessException, Exception
  {
    String SQLBussiness = null;
    String[] arrBussiness = null;
    IPFConfig bsPfConfig = (IPFConfig)NCLocator.getInstance().lookup(IPFConfig.class.getName());
    arrBussiness = bsPfConfig.getBusitypeByCorpAndStyle(corp, "W");
    if (arrBussiness != null) {
      for (int i = 0; i < arrBussiness.length; i++) {
        if (SQLBussiness == null) {
          SQLBussiness = " so_sale.cbiztype = '" + arrBussiness[i].toString() + "'";
        }
        else {
          SQLBussiness = SQLBussiness + " or so_sale.cbiztype = '" + arrBussiness[i].toString() + "'";
        }

      }

      if (SQLBussiness != null)
        SQLBussiness = " AND (" + SQLBussiness + " ) ";
      else {
        return null;
      }

      SCMEnv.out("委托代销业务类型:" + SQLBussiness);
    }
    else {
      return null;
    }

    UFDouble dbltrust = null;

    String SQLTrust = "";

    String SQLSubWhere = "";

    String SQLGroupBy = "";

    SQLTrust = "SELECT so_sale.pk_corp,so_saleorder_b.cadvisecalbodyid as pk_calbody,so_saleorder_b.cinventoryid,so_saleexecute.vfree1,so_saleexecute.vfree2,so_saleexecute.vfree3,so_saleexecute.vfree4,so_saleexecute.vfree5,so_saleorder_b.cbatchid as vbatchcode,sum(ISNULL(so_saleexecute.ntotalinventorynumber, 0) - ISNULL(so_saleexecute.ntotalbalancenumber, 0)) AS outsourcenum ";
    SQLTrust = SQLTrust + " FROM so_sale inner JOIN  so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid inner JOIN ";
    SQLTrust = SQLTrust + "so_saleexecute ON so_sale.csaleid = so_saleexecute.csaleid AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

    SQLTrust = SQLTrust + " WHERE so_sale.creceipttype = '30' AND (so_saleexecute.bifpaybalance = 'N' or so_saleexecute.bifpaybalance is NULL) and \tso_sale.fstatus = 2";

    if (corp != null) {
      SQLSubWhere = SQLSubWhere + " and so_sale.pk_corp ='" + corp + "' ";
    }
    if (ccalbodyid != null) {
      SQLSubWhere = SQLSubWhere + "and so_saleorder_b.cadvisecalbodyid ='" + ccalbodyid + "' ";
    }

    SQLGroupBy = " group by so_sale.pk_corp,so_saleorder_b.cadvisecalbodyid,so_saleorder_b.cinventoryid,so_saleexecute.vfree1,so_saleexecute.vfree2,so_saleexecute.vfree3,so_saleexecute.vfree4,so_saleexecute.vfree5,so_saleorder_b.cbatchid ";

    SQLTrust = SQLTrust + SQLSubWhere + SQLBussiness + SQLGroupBy;

    return SQLTrust;
  }

  /** @deprecated */
  public Hashtable getSaleOrderCust(String[] strID)
    throws BusinessException
  {
    String where = " IN(";

    Hashtable hcustomer = new Hashtable();

    for (int i = 0; i < strID.length; i++) {
      if (i == 0)
        where = where + "'" + strID[i] + "'";
      else {
        where = where + ",'" + strID[i] + "'";
      }
    }
    where = where + ")";

    UFDouble nprice = new UFDouble(0);
    String strSQL = "select csaleid,ccustomerid FROM so_sale where csaleid " + where;

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        String id = rs.getString(1);

        String customerid = rs.getString(2);

        hcustomer.put(id, customerid);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
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
      catch (Exception e)
      {
      }
    }
    return hcustomer;
  }

  /** @deprecated */
  public Hashtable getSaleOrderPrice(String[] strBodyID)
    throws BusinessException
  {
    String where = " IN(";

    Hashtable hprice = new Hashtable();

    for (int i = 0; i < strBodyID.length; i++) {
      if (i == 0)
        where = where + "'" + strBodyID[i] + "'";
      else {
        where = where + ",'" + strBodyID[i] + "'";
      }
    }
    where = where + ")";

    UFDouble nprice = new UFDouble(0);
    String strSQL = "select corder_bid,abs(nsummny/nnumber) FROM so_saleorder_b where corder_bid " + where;

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(strSQL);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        String id = rs.getString(1);

        BigDecimal n = rs.getBigDecimal(2);
        nprice = n == null ? new UFDouble(0) : new UFDouble(n);

        hprice.put(id, nprice);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
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
      catch (Exception e)
      {
      }
    }
    return hprice;
  }

  public void isEnough(Hashtable htNum)
    throws BusinessException, SQLException
  {
    if (htNum == null) {
      return;
    }

    Object objTemp = null;
    StringBuffer sbfWhereStr = new StringBuffer();
    sbfWhereStr.append(" where so_saleexecute.creceipttype='30' and so_oosinfo_b.bfillflag = 'N' and (1<0 ");

    Enumeration enumer = htNum.keys();
    while (enumer.hasMoreElements()) {
      objTemp = enumer.nextElement();
      sbfWhereStr.append(" or so_oosinfo_b.corder_bid = '");
      sbfWhereStr.append(objTemp.toString());
      sbfWhereStr.append("' ");
    }
    sbfWhereStr.append(") ");

    StringBuffer sbfSql = new StringBuffer();
    sbfSql.append("SELECT so_oosinfo_b.corder_bid,isnull(so_oosinfo_b.nfillnumber,0)-isnull(so_saleexecute.ntotalinventorynumber,0), bd_invbasdoc.invcode ");

    sbfSql.append("FROM so_oosinfo_b LEFT OUTER JOIN ");
    sbfSql.append("bd_invbasdoc ON so_oosinfo_b.cinvbasdocid = bd_invbasdoc.pk_invbasdoc ");

    sbfSql.append("LEFT OUTER JOIN so_saleexecute ");
    sbfSql.append("on so_oosinfo_b.corder_bid=so_saleexecute.csale_bid ");
    sbfSql.append(sbfWhereStr.toString());
    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sbfSql.toString());
      ResultSet rs = stmt.executeQuery();

      Object obj = null;
      String sbid = "";
      String sCode = "";
      UFDouble dbNum = null;
      UFDouble dbFill = null;
      UFDouble dbZero = new UFDouble(0);
      StringBuffer sbfExp = new StringBuffer();
      while (rs.next()) {
        sbid = rs.getString(1);
        if ((sbid != null) && (sbid.length() > 0)) {
          obj = rs.getObject(2);
          dbNum = obj == null ? dbZero : new UFDouble(obj.toString());

          sCode = rs.getString(3);
          sCode = sCode == null ? "" : sCode;
          obj = htNum.get(sbid);
          if (obj != null) {
            dbFill = new UFDouble(obj.toString()).sub(dbNum);
            if (dbFill.compareTo(dbZero) > 0) {
              sbfExp.append(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000062", null, new String[] { sCode }));

              sbfExp.append("\n");
            }
          }
        }
      }
      if (sbfExp.toString().length() > 0)
        throw new BusinessException(sbfExp.toString());
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

  private boolean isModuleStarted(String sCorpID, String sModuleCode) {
    boolean isUsed = false;
    try {
      CreatecorpDMO dmo = new CreatecorpDMO();
      isUsed = dmo.isEnabled(sCorpID, sModuleCode);
    }
    catch (Exception e) {
      isUsed = false;
      SCMEnv.out(e.getMessage());
    }

    SCMEnv.out("----------$$$$$$$$$$$--------- module status is " + sModuleCode + isUsed);

    return isUsed;
  }

  /** @deprecated */
  public void isOverSaleOrder(AggregatedValueObject outBillVO)
    throws BusinessException
  {
    String pk_corp = outBillVO.getParentVO().getAttributeValue("pk_corp").toString();

    SysInitDMO initReferDMO = new SysInitDMO();

    String IC003 = initReferDMO.getParaString(pk_corp, "SO23");

    String IC004 = initReferDMO.getParaString(pk_corp, "SO24");

    UFDouble nPercent = IC004 == null ? new UFDouble("0") : new UFDouble(IC004);

    String id = outBillVO.getParentVO().getPrimaryKey();

    String corderhid = null;

    String corderbid = null;

    UFDouble noutnum = null;
    UFDouble nshouldoutnum = null;
    Hashtable houtnum = new Hashtable();

    String SourceBillTypeID = null;

    if ((outBillVO.getChildrenVO() == null) || (outBillVO.getChildrenVO().length == 0))
    {
      return;
    }
    String subSQL = null;

    CircularlyAccessibleValueObject bodyVO = outBillVO.getChildrenVO()[0];
    if (bodyVO.getAttributeValue("csourcetype") != null) {
      SourceBillTypeID = bodyVO.getAttributeValue("csourcetype").toString();

      if ((SourceBillTypeID.equals("30")) || (bodyVO.getAttributeValue("csourcetype").equals("3A")))
      {
        subSQL = "select csourcebillbid from ic_general_b where cgeneralhid = '" + id + "'";
      }
      else {
        subSQL = "select cfirstbillbid from ic_general_b where cgeneralhid = '" + id + "'";
      }

    }

    for (int i = 0; i < outBillVO.getChildrenVO().length; i++)
    {
      bodyVO = outBillVO.getChildrenVO()[i];

      if ((SourceBillTypeID.equals("30")) || (bodyVO.getAttributeValue("csourcetype").equals("3A")))
      {
        corderbid = bodyVO.getAttributeValue("csourcebillbid").toString();
      }
      else
      {
        corderbid = bodyVO.getAttributeValue("cfirstbillbid").toString();
      }

      noutnum = bodyVO.getAttributeValue("noutnum") == null ? new UFDouble("0") : new UFDouble(bodyVO.getAttributeValue("noutnum").toString());

      nshouldoutnum = bodyVO.getAttributeValue("nshouldoutnum") == null ? new UFDouble(0) : new UFDouble(bodyVO.getAttributeValue("nshouldoutnum").toString());

      noutnum = noutnum.add(nshouldoutnum);
      houtnum.put(corderbid, noutnum);
    }

    String error = null;
    try {
      error = isSaleOverOut2(IC003, nPercent, subSQL, houtnum);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }

    if (error != null) {
      BusinessException e = new BusinessException(error);

      throw e;
    }
  }

  /** @deprecated */
  private void isSaleBalance(String SaleHeadID, String SaleBodyID)
    throws SQLException, BusinessException
  {
    String SQLRelation = "SELECT nbalancenum from so_square_b where corder_bid = ? and csaleid = ? and (nbalancenum <> 0 or bifpaybalance = 'Y') and dr = 0";

    Connection con = null;
    PreparedStatement stmt = null;

    boolean isBalance = false;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQLRelation);

      stmt.setString(1, SaleBodyID);

      stmt.setString(2, SaleHeadID);

      ResultSet rstNumber = stmt.executeQuery();

      if (rstNumber.next())
        isBalance = true;
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
    if (isBalance)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000064"));
  }

  /** @deprecated */
  private String isSaleOverOut2(String IC003, UFDouble IC004, String subSQL, Hashtable hSaleOutNum)
    throws SQLException, BusinessException
  {
    String SQLRelation = "SELECT so_saleorder_b.corder_bid,so_saleorder_b.nnumber, isnull(so_saleexecute.ntotalinventorynumber,0) + isnull(so_saleexecute.ntotalshouldoutnum,0)-isnull(so_saleexecute.ntranslossnum,0) AS inventorynumber, so_saleexecute.bifinventoryfinish,so_saleorder_b.nouttoplimit  ";
    SQLRelation = SQLRelation + " FROM so_saleorder_b ,so_saleexecute  Where  so_saleorder_b.csaleid = so_saleexecute.csaleid AND  so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

    SQLRelation = SQLRelation + "  and csale_bid IN (" + subSQL + ")";
    BigDecimal dblNumber = new BigDecimal(0);
    BigDecimal dblinventorynumber = new BigDecimal(0);

    Connection con = null;
    PreparedStatement stmt = null;
    UFDouble nResult = null;
    String sResult = null;
    UFBoolean bifinventoryfinish = null;

    IC004 = IC004.multiply(0.01D).add(1.0D);

    UFDouble nouttoplimit = null;
    boolean isOverOut = false;

    UFDouble uf1 = new UFDouble(1);

    if ("Y".equals(IC003))
      isOverOut = true;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQLRelation);
      ResultSet rstNumber = stmt.executeQuery();

      while (rstNumber.next()) {
        String corder_bid = rstNumber.getString("corder_bid");
        Object oNnumber = rstNumber.getObject("nnumber");
        Object oInventorynumber = rstNumber.getObject("inventorynumber");

        String stemp = rstNumber.getString("bifinventoryfinish");
        if ((stemp == null) || (stemp.trim().length() <= 0))
          bifinventoryfinish = new UFBoolean(false);
        else
          bifinventoryfinish = new UFBoolean(stemp.trim());
        Object o = rstNumber.getObject("nouttoplimit");
        nouttoplimit = o == null ? IC004 : new UFDouble(o.toString()).multiply(0.01D).add(1.0D);

        if (!hSaleOutNum.containsKey(corder_bid))
          continue;
        UFDouble SaleOutNum = (UFDouble)hSaleOutNum.get(corder_bid);
        if ((oNnumber != null) && (oInventorynumber != null)) {
          dblNumber = new BigDecimal(oNnumber.toString());
          dblinventorynumber = new BigDecimal(oInventorynumber.toString());

          SCMEnv.out("nouttoplimit:" + nouttoplimit.doubleValue());

          SCMEnv.out("dblNumber:" + dblNumber.doubleValue());
          SCMEnv.out("dblinventorynumber:" + dblinventorynumber.doubleValue());

          System.out.println("SaleOutNum:" + SaleOutNum.doubleValue());

          if (bifinventoryfinish.booleanValue()) {
            if ((dblNumber.doubleValue() > 0.0D) && (SaleOutNum.doubleValue() > 0.0D))
            {
              sResult = NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000065");
              break;
            }

            if ((dblNumber.doubleValue() < 0.0D) && (SaleOutNum.doubleValue() < 0.0D))
            {
              sResult = NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000065");
              break;
            }

          }

          if (dblNumber.floatValue() >= 0.0F) {
            if (isOverOut) {
              nResult = nouttoplimit.multiply(dblNumber.doubleValue()).sub(dblinventorynumber.doubleValue()).sub(SaleOutNum.doubleValue());
            }
            else
            {
              nResult = uf1.multiply(dblNumber.doubleValue()).sub(dblinventorynumber.doubleValue()).sub(SaleOutNum.doubleValue());
            }

            SCMEnv.out("nResult:" + nResult.doubleValue());
            if (nResult.doubleValue() < 0.0D) {
              sResult = NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044");
              break;
            }
          }
          else {
            if (isOverOut) {
              nResult = nouttoplimit.multiply(dblNumber.doubleValue()).sub(dblinventorynumber.doubleValue()).sub(SaleOutNum.doubleValue());
            }
            else
            {
              nResult = uf1.multiply(dblNumber.doubleValue()).sub(dblinventorynumber.doubleValue()).sub(SaleOutNum.doubleValue());
            }

            if (nResult.doubleValue() > 0.0D) {
              sResult = NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044");
              break;
            }
          }
        } else {
          sResult = NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000044");
          break;
        }
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
    return sResult;
  }

  public void setOOSNum(GeneralBillVO[] outBillVOs)
    throws Exception
  {
    if (outBillVOs == null) {
      return;
    }
    for (int billnum = 0; billnum < outBillVOs.length; billnum++) {
      GeneralBillHeaderVO headVO = outBillVOs[billnum].getHeaderVO();

      GeneralBillItemVO[] bodyVOs = outBillVOs[billnum].getItemVOs();

      String pk_corp = headVO.getPk_corp();
      UFDate date = headVO.getDbilldate();
      String operid = headVO.getCregister();
      String calbody = headVO.getPk_calbody();

      for (int i = 0; i < bodyVOs.length; i++) {
        String cinvid = bodyVOs[i].getCinventoryid();
        UFDouble ninnum = bodyVOs[i].getNinnum();

        String csaleorderno = bodyVOs[i].getVproductbatch();
        if (csaleorderno == null) {
          continue;
        }
        String[] freeitem = new String[5];

        freeitem[0] = bodyVOs[i].getVfree1();
        freeitem[1] = bodyVOs[i].getVfree2();
        freeitem[2] = bodyVOs[i].getVfree3();
        freeitem[3] = bodyVOs[i].getVfree4();
        freeitem[4] = bodyVOs[i].getVfree5();

        setOOSNum2(pk_corp, calbody, cinvid, ninnum, date, csaleorderno, operid, freeitem);
      }
    }
  }

  private void setOOSNum2(String pk_corp, String ccalbodyid, String cinvid, UFDouble ninnum, UFDate date, String csaleorderno, String operid, String[] freeitem)
    throws Exception
  {
    if (ninnum == null) {
      return;
    }
    if (ninnum.doubleValue() <= 0.0D) {
      return;
    }
    OosinfoDMO dmo = new OosinfoDMO();
    OosinfoItemVO[] itemVO = null;

    String freewhere = "";
    if (freeitem != null) {
      for (int i = 0; i < freeitem.length; i++) {
        if (freeitem[i] == null) {
          freewhere = freewhere + " and (so_oosinfo_b.vfree" + (i + 1) + " is null or so_oosinfo_b.vfree" + (i + 1) + " = '') ";
        }
        else
        {
          freewhere = freewhere + " and so_oosinfo_b.vfree" + (i + 1) + "='" + freeitem[i] + "' ";
        }
      }
    }

    UFDouble nnum = ninnum;

    if (csaleorderno != null)
    {
      String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and so_oosinfo.pk_corp = '" + pk_corp + "' and so_oosinfo_b.bfillflag = 'N' and so_oosinfo.vreceiptcode = '" + csaleorderno + "' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

      itemVO = dmo.queryAllByCondition(where);
      if ((itemVO == null) || (itemVO.length == 0))
      {
        return;
      }

      nnum = assignOOSNum2(itemVO, nnum, date, operid);
    }

    if (nnum != null)
    {
      String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '" + pk_corp + "' and  so_oosinfo_b.bfillflag = 'N' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

      itemVO = dmo.queryAllByCondition(where);
      nnum = assignOOSNum2(itemVO, nnum, date, operid);
      if ((nnum != null) && (nnum.doubleValue() > 0.0D))
      {
        where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '" + pk_corp + "' and  so_oosinfo_b.bfillflag = 'N' and so_oosinfo_b.boosflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

        itemVO = dmo.queryAllByCondition(where);
        nnum = assignOOSNum2(itemVO, nnum, date, operid);
      }
    }
  }

  public void setOOSNumUnDo(GeneralBillVO[] outBillVOs)
    throws Exception
  {
    if (outBillVOs == null) {
      return;
    }
    for (int billnum = 0; billnum < outBillVOs.length; billnum++) {
      GeneralBillHeaderVO headVO = outBillVOs[billnum].getHeaderVO();

      GeneralBillItemVO[] bodyVOs = outBillVOs[billnum].getItemVOs();

      String pk_corp = headVO.getPk_corp();
      UFDate date = headVO.getDbilldate();
      String operid = headVO.getCregister();
      String calbody = headVO.getPk_calbody();

      for (int i = 0; i < bodyVOs.length; i++) {
        String cinvid = bodyVOs[i].getCinventoryid();
        UFDouble ninnum = bodyVOs[i].getNinnum();

        String csaleorderno = bodyVOs[i].getVproductbatch();
        if (csaleorderno == null) {
          continue;
        }
        String[] freeitem = new String[5];

        freeitem[0] = bodyVOs[i].getVfree1();
        freeitem[1] = bodyVOs[i].getVfree2();
        freeitem[2] = bodyVOs[i].getVfree3();
        freeitem[3] = bodyVOs[i].getVfree4();
        freeitem[4] = bodyVOs[i].getVfree5();

        setOOSNumUnDo2(pk_corp, calbody, cinvid, ninnum, date, csaleorderno, operid, freeitem);
      }
    }
  }

  /** @deprecated */
  private void setOOSNumUnDo(String pk_corp, String ccalbodyid, String cinvid, UFDouble ninnum, UFDate date, String csaleorderno, String operid, String[] freeitem)
    throws Exception
  {
    if (ninnum == null) {
      return;
    }
    if (ninnum.doubleValue() <= 0.0D) {
      return;
    }
    OosinfoDMO dmo = new OosinfoDMO();
    OosinfoItemVO[] itemVO = null;

    String freewhere = "";
    if (freeitem != null) {
      for (int i = 0; i < freeitem.length; i++) {
        if (freeitem[i] == null) {
          freewhere = freewhere + " and (so_oosinfo_b.vfree" + (i + 1) + " is null or so_oosinfo_b.vfree" + (i + 1) + " = '') ";
        }
        else
        {
          freewhere = freewhere + " and so_oosinfo_b.vfree" + (i + 1) + "='" + freeitem[i] + "' ";
        }
      }
    }

    UFDouble nnum = ninnum;

    if (csaleorderno != null)
    {
      String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and so_oosinfo.pk_corp = '" + pk_corp + "' and so_oosinfo.vreceiptcode = '" + csaleorderno + "' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

      itemVO = dmo.queryAllByCondition(where);
      if ((itemVO == null) || (itemVO.length == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000045", null, new String[] { csaleorderno }));
      }

      nnum = assignOOSNum(itemVO, nnum, date, operid);
    }

    if (nnum != null)
    {
      String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '" + pk_corp + "' and  so_oosinfo_b.nfillnumber > 0 and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

      itemVO = dmo.queryAllByCondition(where);
      nnum = assignOOSNum(itemVO, nnum, date, operid);
      if ((nnum != null) && (nnum.doubleValue() > 0.0D))
      {
        where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '" + pk_corp + "' and  so_oosinfo_b.nfillnumber > 0 and so_oosinfo_b.boosflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

        itemVO = dmo.queryAllByCondition(where);
        nnum = assignOOSNum(itemVO, nnum, date, operid);
      }
    }
  }

  /** @deprecated */
  public void setOOSNumUnDo(GeneralBillVO outBillVO)
    throws Exception
  {
    GeneralBillHeaderVO headVO = outBillVO.getHeaderVO();
    GeneralBillItemVO[] bodyVOs = outBillVO.getItemVOs();

    String pk_corp = headVO.getPk_corp();
    UFDate date = headVO.getDbilldate();
    String operid = headVO.getCregister();
    String calbody = headVO.getPk_calbody();

    for (int i = 0; i < bodyVOs.length; i++) {
      String cinvid = bodyVOs[i].getCinventoryid();
      UFDouble ninnum = bodyVOs[i].getNinnum();

      String csaleorderno = bodyVOs[i].getVproductbatch();

      String[] freeitem = new String[5];

      freeitem[0] = bodyVOs[i].getVfree1();
      freeitem[1] = bodyVOs[i].getVfree2();
      freeitem[2] = bodyVOs[i].getVfree3();
      freeitem[3] = bodyVOs[i].getVfree4();
      freeitem[4] = bodyVOs[i].getVfree5();

      setOOSNumUnDo(pk_corp, calbody, cinvid, ninnum, date, csaleorderno, operid, freeitem);
    }
  }

  private void setOOSNumUnDo2(String pk_corp, String ccalbodyid, String cinvid, UFDouble ninnum, UFDate date, String csaleorderno, String operid, String[] freeitem)
    throws Exception
  {
    if (ninnum == null) {
      return;
    }
    if (ninnum.doubleValue() <= 0.0D) {
      return;
    }
    OosinfoDMO dmo = new OosinfoDMO();
    OosinfoItemVO[] itemVO = null;

    String freewhere = "";
    if (freeitem != null) {
      for (int i = 0; i < freeitem.length; i++) {
        if (freeitem[i] == null) {
          freewhere = freewhere + " and (so_oosinfo_b.vfree" + (i + 1) + " is null or so_oosinfo_b.vfree" + (i + 1) + " = '') ";
        }
        else
        {
          freewhere = freewhere + " and so_oosinfo_b.vfree" + (i + 1) + "='" + freeitem[i] + "' ";
        }
      }
    }

    UFDouble nnum = ninnum;

    if (csaleorderno != null)
    {
      String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and so_oosinfo.pk_corp = '" + pk_corp + "' and so_oosinfo.vreceiptcode = '" + csaleorderno + "' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

      itemVO = dmo.queryAllByCondition(where);
      if ((itemVO == null) || (itemVO.length == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface", "UPPsointerface-000045", null, new String[] { csaleorderno }));
      }

      nnum = assignOOSNum2(itemVO, nnum, date, operid);
    }

    if (nnum != null)
    {
      String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '" + pk_corp + "' and  so_oosinfo_b.nfillnumber > 0 and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

      itemVO = dmo.queryAllByCondition(where);
      nnum = assignOOSNum2(itemVO, nnum, date, operid);
      if ((nnum != null) && (nnum.doubleValue() > 0.0D))
      {
        where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '" + pk_corp + "' and  so_oosinfo_b.nfillnumber > 0 and so_oosinfo_b.boosflag = 'Y' and so_oosinfo_b.cinventoryid = '" + cinvid + "'" + " and so_oosinfo.ccalbodyid = '" + ccalbodyid + "' " + freewhere + " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

        itemVO = dmo.queryAllByCondition(where);
        nnum = assignOOSNum2(itemVO, nnum, date, operid);
      }
    }
  }

  /** @deprecated */
  public void setReturnNum(CircularlyAccessibleValueObject[] toretvos)
    throws BusinessException
  {
    if ((toretvos == null) || (toretvos.length <= 0))
      return;
    try {
      SOToolsDMO.updateNoAllBatch(toretvos, new String[] { "ntotalreturnnumber" }, "so_saleexecute", new String[] { "csale_bid" });

      String[] sIds = new String[toretvos.length];
      for (int i = 0; i < toretvos.length; i++) {
        sIds[i] = ((String)toretvos[i].getAttributeValue("csale_bid"));
      }
      SaleOrderDMO.checkIsExecuteNumRigth(sIds);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
  }

  /** @deprecated */
  public void setSignNum(String pk_corp, String cbillhid, String cbillbid, UFDouble noutnum)
    throws BusinessException
  {
    SysInitDMO initReferDMO = new SysInitDMO();

    String SO05 = initReferDMO.getParaString(pk_corp, "SO05");

    String SQLUpdate = "update so_square_b set nsignnum = ? ";

    if (SO05.equals("签收量")) {
      try {
        isSaleBalance(cbillhid, cbillbid);
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
        throw new BusinessException(e.getMessage());
      }

      SQLUpdate = SQLUpdate + ",noutnum = ? ";
    }

    SQLUpdate = SQLUpdate + " where corder_bid = ? and csaleid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(SQLUpdate);
      if (noutnum == null)
        stmt.setNull(1, 4);
      else {
        stmt.setBigDecimal(1, noutnum.toBigDecimal());
      }
      int n = 2;

      if (SO05.equals("签收量")) {
        if (noutnum == null)
          stmt.setNull(1, 4);
        else
          stmt.setBigDecimal(2, noutnum.toBigDecimal());
        n = 3;
      }

      stmt.setString(n, cbillbid);

      stmt.setString(n + 1, cbillhid);
      stmt.executeUpdate();

      stmt.close();
      con.close();
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
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
      catch (Exception e)
      {
      }
    }
  }

  private UFBoolean getSendType(String csaleid)
  {
    if (csaleid == null)
      return null;
    if (this.hssendtype == null)
      this.hssendtype = new HashMap();
    UFBoolean retb = null;
    retb = (UFBoolean)this.hssendtype.get(csaleid);
    if (retb == null) {
      String stemp = null;
      try {
        FetchValueDMO fetchdmo = new FetchValueDMO();
        stemp = fetchdmo.fetchValue(" so_sale ", "bdeliver", " so_sale.csaleid='" + csaleid + "' ");

        if ((stemp != null) && (stemp.trim().length() > 0)) {
          retb = new UFBoolean(stemp.trim());
        }
        else {
          retb = SoVoConst.buffalse;
        }
        this.hssendtype.put(csaleid, retb);
      }
      catch (Exception e) {
        retb = SoVoConst.buffalse;
        SCMEnv.out(e.getMessage());
      }
    }
    return retb;
  }

  public void setTransLossNum(String[] ids, UFDouble[] divnums)
    throws BusinessException
  {
    if ((ids == null) || (divnums == null) || (ids.length == 0) || (ids.length != divnums.length)) return; try
    {
      String sSql = "update so_saleexecute set ntranslossnum=isnull(ntranslossnum,0) + ? where csale_bid = ? and creceipttype = '30' ";
      SmartDMO sdmo = new SmartDMO();

      ArrayList alValue = new ArrayList();
      ArrayList alitem = null;

      for (int i = 0; i < ids.length; i++) {
        alitem = new ArrayList();
        alitem.add(divnums[i]);
        alitem.add(ids[i]);
        alValue.add(alitem);
      }
      ArrayList alType = new ArrayList();

      alType.add(new Integer(1));
      alType.add(new Integer(3));

      sdmo.executeUpdateBatch(sSql, alValue, alType);
      SaleOrderDMO ddmo = new SaleOrderDMO();
      SaleorderBVO[] bvos = (SaleorderBVO[])(SaleorderBVO[])ddmo.queryAllBodyDataByBIDs(ids);
      ddmo.processOutState(bvos);
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
  }
}