package nc.impl.scm.so.todm;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.SystemException;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.scm.pub.bill.ScmDMO;
import nc.impl.scm.so.so001.SOToolsImpl;
import nc.impl.scm.so.so001.SaleOrderDMO;
import nc.impl.scm.so.so016.SOToolsDMO;
import nc.itf.so.service.ISOToDM_SOTODMDMO;
import nc.itf.uap.sf.IServiceProviderSerivce;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.pub.ProdNotInstallException;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so016.SoVoTools;

public class SOTODMDMO extends ScmDMO
  implements ISOToDM_SOTODMDMO
{
  public SOTODMDMO()
    throws NamingException, SystemException
  {
  }

  public SOTODMDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  private UFDouble getReceiptNumber(String SaleID, String SaleDetailID)
    throws SQLException, BusinessException
  {
    String SQLRowStatus = "select nnumber-isnull(ntotalreceiptnumber,0) nReceiptNum ";
    SQLRowStatus = SQLRowStatus + " from  so_saleorder_b left outer  join so_saleexecute on so_saleorder_b.csaleid = so_saleexecute.csaleid ";

    SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";

    SQLRowStatus = SQLRowStatus + " where so_saleorder_b.csaleid ='" + SaleID + "' ";

    SQLRowStatus = SQLRowStatus + " and so_saleorder_b.corder_bid ='" + SaleDetailID + "' ";

    Connection con = null;
    PreparedStatement stmt = null;

    String bResultMsg = null;

    BigDecimal dblNumber = new BigDecimal(0);
    try
    {
      con = getConnection("getReceiptNumber1");
      stmt = con.prepareStatement(SQLRowStatus);

      ResultSet rstNumber = stmt.executeQuery();

      if (rstNumber.next())
      {
        Object o = rstNumber.getObject("nReceiptNum");

        if (o != null)
          dblNumber = new BigDecimal(o.toString());
      }
    }
    finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (Exception e)
      {
      }
      try
      {
        removeConnection("getReceiptNumber1");
      }
      catch (Exception e) {
      }
    }
    UFDouble number = new UFDouble(dblNumber);

    return number;
  }

  private UFBoolean getSupplyflag(String sBid)
    throws SQLException, Exception
  {
    String sSQL = "select bfillflag from so_oosinfo_b where dr=0 and corder_bid = ?";

    Connection con = null;
    PreparedStatement stmt = null;

    UFBoolean result = new UFBoolean(false);
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSQL);

      stmt.setString(1, sBid);

      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        String bfillflag = rs.getString(1);
        if (bfillflag != null)
          result = new UFBoolean(bfillflag.trim());
      }
      else {
        result = new UFBoolean(true);
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

  private void queryFollowBody(SaleorderBVO[] bodys)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so001.SaleOrderDMO", "queryFollowBody", new Object[] { bodys });

    String sql = "select csale_bid, creceipttype, ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, bifpaybalance, bifpaysign, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6,ntotalplanreceiptnumber,ntotalcarrynumber,ntotalreturnnumber from so_saleexecute where csaleid = ? order by csale_bid";

    Connection con = null;
    PreparedStatement stmt = null;

    con = getConnection();
    stmt = con.prepareStatement(sql);
    try
    {
      SaleorderBVO saleexecute = bodys[0];

      String key = saleexecute.getCsaleid();
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      int i = 0;

      while (rs.next())
      {
        if (i >= bodys.length) break;
        saleexecute = bodys[i];

        String csale_bid = rs.getString(1);

        if (csale_bid.equals(saleexecute.getPrimaryKey()))
        {
          String creceipttype = rs.getString(2);

          BigDecimal ntotalpaymny = (BigDecimal)rs.getObject(3);
          saleexecute.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));

          BigDecimal ntotalreceiptnumber = (BigDecimal)rs.getObject(4);

          saleexecute.setNtotalreceiptnumber(ntotalreceiptnumber == null ? null : new UFDouble(ntotalreceiptnumber));

          BigDecimal ntotalinvoicenumber = (BigDecimal)rs.getObject(5);

          saleexecute.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));

          BigDecimal ntotalinvoicemny = (BigDecimal)rs.getObject(6);

          saleexecute.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));

          BigDecimal ntotalinventorynumber = (BigDecimal)rs.getObject(7);

          saleexecute.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(ntotalinventorynumber));

          BigDecimal ntotalbalancenumber = (BigDecimal)rs.getObject(8);

          saleexecute.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));

          BigDecimal ntotalsignnumber = (BigDecimal)rs.getObject(9);

          saleexecute.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));

          BigDecimal ntotalcostmny = (BigDecimal)rs.getObject(10);

          saleexecute.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));

          String bifinvoicefinish = rs.getString(11);
          saleexecute.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));

          String bifreceiptfinish = rs.getString(12);
          saleexecute.setBifreceiptfinish(bifreceiptfinish == null ? null : new UFBoolean(bifreceiptfinish.trim()));

          String bifinventoryfinish = rs.getString(13);
          saleexecute.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish.trim()));

          String bifpayfinish = rs.getString(14);
          saleexecute.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

          String bifpaybalance = rs.getString(15);
          saleexecute.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

          String bifpaysign = rs.getString(16);
          saleexecute.setBifpaysign(bifpaysign == null ? null : new UFBoolean(bifpaysign.trim()));

          BigDecimal nassistcurdiscountmny = (BigDecimal)rs.getObject(17);

          saleexecute.setNassistcurdiscountmny(nassistcurdiscountmny == null ? null : new UFDouble(nassistcurdiscountmny));

          BigDecimal nassistcursummny = (BigDecimal)rs.getObject(18);

          saleexecute.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

          BigDecimal nassistcurmny = (BigDecimal)rs.getObject(19);

          saleexecute.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

          BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject(20);

          saleexecute.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

          BigDecimal nassistcurtaxnetprice = (BigDecimal)rs.getObject(21);

          saleexecute.setNassistcurtaxnetprice(nassistcurtaxnetprice == null ? null : new UFDouble(nassistcurtaxnetprice));

          BigDecimal nassistcurnetprice = (BigDecimal)rs.getObject(22);

          saleexecute.setNassistcurnetprice(nassistcurnetprice == null ? null : new UFDouble(nassistcurnetprice));

          BigDecimal nassistcurtaxprice = (BigDecimal)rs.getObject(23);

          saleexecute.setNassistcurtaxprice(nassistcurtaxprice == null ? null : new UFDouble(nassistcurtaxprice));

          BigDecimal nassistcurprice = (BigDecimal)rs.getObject(24);

          saleexecute.setNassistcurprice(nassistcurprice == null ? null : new UFDouble(nassistcurprice));

          String cprojectid = rs.getString(25);
          saleexecute.setCprojectid(cprojectid == null ? null : cprojectid.trim());

          String cprojectphaseid = rs.getString(26);
          saleexecute.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

          String cprojectid3 = rs.getString(27);
          saleexecute.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());

          String vfree1 = rs.getString(28);
          saleexecute.setVfree1(vfree1 == null ? null : vfree1.trim());

          String vfree2 = rs.getString(29);
          saleexecute.setVfree2(vfree2 == null ? null : vfree2.trim());

          String vfree3 = rs.getString(30);
          saleexecute.setVfree3(vfree3 == null ? null : vfree3.trim());

          String vfree4 = rs.getString(31);
          saleexecute.setVfree4(vfree4 == null ? null : vfree4.trim());

          String vfree5 = rs.getString(32);
          saleexecute.setVfree5(vfree5 == null ? null : vfree5.trim());

          String vdef1 = rs.getString(33);
          saleexecute.setVdef1(vdef1 == null ? null : vdef1.trim());

          String vdef2 = rs.getString(34);
          saleexecute.setVdef2(vdef2 == null ? null : vdef2.trim());

          String vdef3 = rs.getString(35);
          saleexecute.setVdef3(vdef3 == null ? null : vdef3.trim());

          String vdef4 = rs.getString(36);
          saleexecute.setVdef4(vdef4 == null ? null : vdef4.trim());

          String vdef5 = rs.getString(37);
          saleexecute.setVdef5(vdef5 == null ? null : vdef5.trim());

          String vdef6 = rs.getString(38);
          saleexecute.setVdef6(vdef6 == null ? null : vdef6.trim());

          BigDecimal ntotalplanreceiptnumber = (BigDecimal)rs.getObject(39);

          saleexecute.setNtotalplanreceiptnumber(ntotalplanreceiptnumber == null ? null : new UFDouble(ntotalplanreceiptnumber));

          BigDecimal ntotalcarrynumber = (BigDecimal)rs.getObject(40);

          saleexecute.setNtotalcarrynumber(ntotalcarrynumber == null ? null : new UFDouble(ntotalcarrynumber));

          BigDecimal ntotalreturnnumber = (BigDecimal)rs.getObject(41);

          saleexecute.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(ntotalreturnnumber));

          i += 1;
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "queryFollowBody", new Object[] { bodys });
  }

  private CircularlyAccessibleValueObject[] querySaleOrderBodyData(String key)
    throws SQLException
  {
    String sql = "select so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, so_saleorder_b.creceipttype, csourcebillid, csourcebillbodyid, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, cbodywarehouseid, dconsigndate, ddeliverdate, blargessflag, ceditsaleid, beditflag, veditreason, ccurrencytypeid, nitemdiscountrate, ndiscountrate, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, coperatorid, frowstatus, frownote,cinvbasdocid,cbatchid,fbatchstatus,cbomorderid,cfreezeid,ct_manageid , so_saleorder_b.ts,so_saleorder_b.creceiptareaid ,so_saleorder_b.vreceiveaddress,so_saleorder_b.creceiptcorpid,so_saleorder_b.cadvisecalbodyid, ,so_saleexecute.ntotalsignnumber,so_saleexecute.ntotalreturnnumber  from so_saleorder_b,so_saleexecute,bd_invbasdoc,bd_invcl   where so_saleorder_b.csaleid = so_saleexecute.csaleid  AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid  AND  so_saleorder_b.csaleid = ?  AND bifreceiptfinish = 'N' AND beditflag = 'N'and  bd_invbasdoc.discountflag = 'N' and bd_invbasdoc.laborflag = 'N' order by corder_bid";

    SaleorderBVO[] saleItems = null;
    Vector v = new Vector();

    Connection con = null;
    PreparedStatement stmt = null;

    con = getConnection();
    try {
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SaleorderBVO saleItem = new SaleorderBVO();

        String corder_bid = rs.getString("corder_bid");
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString("pk_corp");
        saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());

        String creceipttype = rs.getString("creceipttype");
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString("csourcebillid");
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString("cunitid");
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String dconsigndate = rs.getString("dconsigndate");
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString("ddeliverdate");
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString("blargessflag");
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String ceditsaleid = rs.getString("ceditsaleid");
        saleItem.setCeditsaleid(ceditsaleid == null ? null : ceditsaleid.trim());

        String beditflag = rs.getString("beditflag");
        saleItem.setBeditflag(beditflag == null ? null : new UFBoolean(beditflag.trim()));

        String veditreason = rs.getString("veditreason");
        saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject("nitemdiscountrate");

        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject("ndiscountrate");

        saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");

        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject("noriginalcurtaxprice");

        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");

        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");

        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");

        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject("noriginalcurdiscountmny");

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject("nprice");
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");

        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject("ndiscountmny");

        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        String coperatorid = rs.getString("coperatorid");
        saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        Integer frowstatus = (Integer)rs.getObject("frowstatus");
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString("frownote");
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cbatchid = rs.getString("cbatchid");
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        Integer fbatchstatus = (Integer)rs.getObject("fbatchstatus");
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String cbomorderid = rs.getString("cbomorderid");
        saleItem.setCbomorderid(cbomorderid == null ? null : cbomorderid.trim());

        String cfreezeid = rs.getString("cfreezeid");
        saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        String ct_manageid = rs.getString("ct_manageid");
        saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());

        String ts = rs.getString("ts");
        saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        String creceiptareaid = rs.getString("creceiptareaid");
        saleItem.setCreceiptareaid(creceiptareaid == null ? null : creceiptareaid.trim());

        String vreceiveaddress = rs.getString("vreceiveaddress");
        saleItem.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString("creceiptcorpid");
        saleItem.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String cadvisecalbodyid = rs.getString("cadvisecalbodyid");
        saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());

        Object ntotalsignnumber = rs.getObject("ntotalsignnumber");
        if ((ntotalsignnumber != null) && (ntotalsignnumber.toString().trim().length() > 0))
        {
          saleItem.setNtotalsignnumber(new UFDouble(ntotalsignnumber.toString()));
        }

        Object ntotalreturnnumber = rs.getObject("ntotalreturnnumber");
        if ((ntotalreturnnumber != null) && (ntotalreturnnumber.toString().trim().length() > 0))
        {
          saleItem.setNtotalreturnnumber(new UFDouble(ntotalreturnnumber.toString()));
        }

        v.addElement(saleItem);
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
    saleItems = new SaleorderBVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(saleItems);
    }

    queryFollowBody(saleItems);

    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", new Object[] { key });

    return saleItems;
  }

  private CircularlyAccessibleValueObject[] querySaleOrderBodyData(String key, String where) throws SQLException
  {
    String sql = "select so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, so_saleorder_b.creceipttype, csourcebillid, csourcebillbodyid, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, cbodywarehouseid, dconsigndate, ddeliverdate, blargessflag, ceditsaleid, beditflag, veditreason, ccurrencytypeid, nitemdiscountrate, ndiscountrate, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, coperatorid, frowstatus, frownote,cinvbasdocid,cbatchid,fbatchstatus,cbomorderid,cfreezeid,ct_manageid, so_saleorder_b.ts,so_saleorder_b.creceiptareaid ,so_saleorder_b.vreceiveaddress,so_saleorder_b.creceiptcorpid,so_saleorder_b.cadvisecalbodyid ,so_saleexecute.ntotalsignnumber,so_saleexecute.ntotalreturnnumber  from so_saleorder_b,so_saleexecute,bd_invbasdoc,bd_invcl   where so_saleorder_b.csaleid = so_saleexecute.csaleid  AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid  and so_saleorder_b.cinvbasdocid = bd_invbasdoc.pk_invbasdoc and bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl  AND  so_saleorder_b.csaleid = ?  AND beditflag = 'N'  AND bifreceiptfinish = 'N'";

    sql = sql + " and  bd_invbasdoc.discountflag = 'N' and bd_invbasdoc.laborflag = 'N' ";

    if (where != null) {
      sql = sql + where;
    }
    sql = sql + " order by corder_bid";

    SaleorderBVO[] saleItems = null;
    Vector v = new Vector();

    Connection con = null;
    PreparedStatement stmt = null;

    con = getConnection();
    try {
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SaleorderBVO saleItem = new SaleorderBVO();

        String corder_bid = rs.getString("corder_bid");
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ccorpid = rs.getString("pk_corp");
        saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());

        String creceipttype = rs.getString("creceipttype");
        saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String csourcebillid = rs.getString("csourcebillid");
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString("cunitid");
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String dconsigndate = rs.getString("dconsigndate");
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString("ddeliverdate");
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString("blargessflag");
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String ceditsaleid = rs.getString("ceditsaleid");
        saleItem.setCeditsaleid(ceditsaleid == null ? null : ceditsaleid.trim());

        String beditflag = rs.getString("beditflag");
        saleItem.setBeditflag(beditflag == null ? null : new UFBoolean(beditflag.trim()));

        String veditreason = rs.getString("veditreason");
        saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject("nitemdiscountrate");

        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject("ndiscountrate");

        saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");

        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject("noriginalcurtaxprice");

        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");

        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");

        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");

        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject("noriginalcurdiscountmny");

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject("nprice");
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");

        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject("ndiscountmny");

        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        String coperatorid = rs.getString("coperatorid");
        saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        Integer frowstatus = (Integer)rs.getObject("frowstatus");
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString("frownote");
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cbatchid = rs.getString("cbatchid");
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        Integer fbatchstatus = (Integer)rs.getObject("fbatchstatus");
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String cbomorderid = rs.getString("cbomorderid");
        saleItem.setCbomorderid(cbomorderid == null ? null : cbomorderid.trim());

        String cfreezeid = rs.getString("cfreezeid");
        saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        String ct_manageid = rs.getString("ct_manageid");
        saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());

        String ts = rs.getString("ts");
        saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        String creceiptareaid = rs.getString("creceiptareaid");
        saleItem.setCreceiptareaid(creceiptareaid == null ? null : creceiptareaid.trim());

        String vreceiveaddress = rs.getString("vreceiveaddress");
        saleItem.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString("creceiptcorpid");
        saleItem.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String cadvisecalbodyid = rs.getString("cadvisecalbodyid");
        saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());

        Object ntotalsignnumber = rs.getObject("ntotalsignnumber");
        if ((ntotalsignnumber != null) && (ntotalsignnumber.toString().trim().length() > 0))
        {
          saleItem.setNtotalsignnumber(new UFDouble(ntotalsignnumber.toString()));
        }

        Object ntotalreturnnumber = rs.getObject("ntotalreturnnumber");
        if ((ntotalreturnnumber != null) && (ntotalreturnnumber.toString().trim().length() > 0))
        {
          saleItem.setNtotalreturnnumber(new UFDouble(ntotalreturnnumber.toString()));
        }

        v.addElement(saleItem);
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
    saleItems = new SaleorderBVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(saleItems);
    }

    queryFollowBody(saleItems);

    afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", new Object[] { key });

    return saleItems;
  }

  public AggregatedValueObject[] querySaleOrderData(ConditionVO[] condvo)
    throws BusinessException
  {
    String headwhere = "";
    String bodywhere = "";
    ArrayList arrayli = new ArrayList();

    if (condvo != null) {
      String bid = "";
      for (int i = 0; i < condvo.length; i++) {
        String temp = null;

        if (condvo[i].getFieldCode().equals("pkdelivorg"))
        {
          ArrayList corps = null;
          try {
            corps = (ArrayList)new SOToolsImpl().runComMethod("DM", "nc.bs.dm.pub.DmDMO", "queryCorpIDsByDelivOrgID", new Class[] { String.class }, new Object[] { condvo[i].getValue() });
          }
          catch (ProdNotInstallException e)
          {
            SCMEnv.out(e.getMessage());
            corps = null;
          }
          catch (Exception e) {
            reportException(e);
            throw new BusinessException(e.getMessage());
          }

          if ((corps != null) && (corps.size() > 0)) {
            temp = "so_sale.pk_corp IN (";
            for (int j = 0; j < corps.size(); j++) {
              if (j == 0)
                temp = temp + "'" + corps.get(j) + "'";
              else
                temp = temp + ",'" + corps.get(j) + "'";
            }
            temp = temp + ")";
          }

        }
        else if (condvo[i].getFieldCode().equals("datefrom")) {
          condvo[i].setFieldCode("so_sale.dbilldate ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("dateto")) {
          condvo[i].setFieldCode(" so_sale.dbilldate ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("pkcust")) {
          condvo[i].setFieldCode(" so_sale.ccustomerid ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("pksendstockorg")) {
          condvo[i].setFieldCode(" so_saleorder_b.cadvisecalbodyid ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("pkinv")) {
          condvo[i].setFieldCode("so_saleorder_b.cinventoryid");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("invcl")) {
          condvo[i].setFieldCode("so_saleorder_b.cinventoryid");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("vbillcode")) {
          condvo[i].setFieldCode(" so_sale.vreceiptcode ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("billoutdate")) {
          condvo[i].setFieldCode(" so_saleorder_b.dconsigndate ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("pkreceiptcorpid")) {
          condvo[i].setFieldCode(" so_saleorder_b.creceiptcorpid ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("pkarrivearea")) {
          condvo[i].setFieldCode(" so_saleorder_b.creceiptareaid ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("pkoperator")) {
          condvo[i].setFieldCode(" so_sale.cemployeeid ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("userid")) {
          condvo[i].setFieldCode(" so_sale.coperatorid ");
          arrayli.add(condvo[i]);
        }
        else if (condvo[i].getFieldCode().equals("cbiztype")) {
          condvo[i].setFieldCode(" so_sale.cbiztype ");
          arrayli.add(condvo[i]);
        }
        else {
          if (condvo[i].getFieldCode().equals("pkbillb")) {
            if (bid.equals("")) {
              bid = bid + " (so_saleorder_b.corder_bid = '" + condvo[i].getValue() + "'";
            }
            else {
              bid = bid + " or so_saleorder_b.corder_bid = '" + condvo[i].getValue() + "'";
            }
//            if (i != condvo.length - 1) continue;
//            bid = bid + ")";
//            headwhere = headwhere + " and " + bid;
//            continue;
          }

          if ((condvo[i].getFieldCode().equals("pkdeststoreorg")) && ((condvo[i].getValue() != null) || (condvo[i].getValue().trim().length() != 0)))
          {
            temp = " 0>1 ";
          }
          else if ((condvo[i].getFieldCode().equals("发货库存组织")) && 
            (condvo[i].getValue() != null) && (condvo[i].getValue().length() > 0)) {
            temp = " COALESCE(creccalbodyid,cadvisecalbodyid) IN (" + condvo[i].getValue() + " ) ";
          }

        }
        
        if (temp != null)
          headwhere = headwhere + " AND " + temp;
      }
      //edit by shikun 
      if (!bid.equals("")) {
          bid = bid + ")";
          headwhere = headwhere + " and " + bid;
      }
      //end 
      if (arrayli.size() > 0)
      {
        ConditionVO[] condvonew = (ConditionVO[])(ConditionVO[])arrayli.toArray(new ConditionVO[0]);

        ConditionVO contempvo = new ConditionVO();
        headwhere = headwhere + " AND " + contempvo.getWhereSQL(condvonew);
      }
    }

    headwhere = headwhere + "  and so_sale.creceipttype = '30' and so_sale.fstatus = " + 2 + " and so_sale.breceiptendflag = 'N' and so_saleexecute.bifinventoryfinish = 'N' ";

    SaleOrderVO[] saleorders = null;
    try {
      saleorders = querySaleData2(headwhere);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }

    return saleorders;
  }

  public SaleOrderVO[] querySaleOrderData(String where)
    throws SQLException, Exception
  {
    where = where + "  and fstatus = " + 2 + " and breceiptendflag = 'N'";

    SaleOrderVO[] saleorders = querySaleData(where);

    return saleorders;
  }

  private CircularlyAccessibleValueObject[] querySaleOrderHeadData(String where) throws SQLException
  {
    String strSql = "select DISTINCT so_sale.pk_corp, so_sale.vreceiptcode, so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass, so_sale.finvoicetype,so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate, so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid, so_sale.vreceiveaddress, so_sale.creceiptcorpid, so_sale.ctransmodeid, so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag, so_sale.cfreecustid, so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, so_sale.dapprovedate, so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6, so_sale.vdef7, so_sale.vdef8,so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,so_sale.csaleid,so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,so_sale.breceiptendflag, so_sale.ts from so_sale,so_saleorder_b,bd_sendtype,bd_invbasdoc,bd_invcl,bd_cumandoc,bd_cubasdoc where so_sale.csaleid = so_saleorder_b.csaleid and so_sale.ctransmodeid = bd_sendtype.pk_sendtype and issendarranged = 'Y'  and so_saleorder_b.cinvbasdocid = bd_invbasdoc.pk_invbasdoc and bd_invbasdoc.pk_invcl = bd_invcl.pk_invcl and so_sale.ccustomerid = bd_cumandoc.pk_cumandoc and bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ";

    strSql = strSql + " and  bd_invbasdoc.discountflag = 'N' and bd_invbasdoc.laborflag = 'N' ";

    if ((where != null) && (!where.equals(""))) {
      strSql = strSql + where;
    }
    strSql = strSql + " order by so_sale.csaleid ";

    SaleorderHVO[] saleorderHs = null;
    Vector v = new Vector();

    Connection con = null;
    PreparedStatement stmt = null;

    con = getConnection();
    try
    {
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        SaleorderHVO saleHeader = new SaleorderHVO();

        String pk_corp = rs.getString(1);
        saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String vreceiptcode = rs.getString(2);
        saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString(3);
        saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String cbiztype = rs.getString(4);
        saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        Integer finvoiceclass = (Integer)rs.getObject(5);
        saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);

        Integer finvoicetype = (Integer)rs.getObject(6);
        saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);

        String vaccountyear = rs.getString(7);
        saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());

        String binitflag = rs.getString(8);
        saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));

        String dbilldate = rs.getString(9);
        saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));

        String ccustomerid = rs.getString(10);
        saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        String cdeptid = rs.getString(11);
        saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(12);
        saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String coperatorid = rs.getString(13);
        saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String ctermprotocolid = rs.getString(14);
        saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());

        String csalecorpid = rs.getString(15);
        saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());

        String creceiptcustomerid = rs.getString(16);
        saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());

        String vreceiveaddress = rs.getString(17);
        saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString(18);
        saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String ctransmodeid = rs.getString(19);
        saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject(20);
        saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        String cwarehouseid = rs.getString(21);
        saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String veditreason = rs.getString(22);
        saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());

        String bfreecustflag = rs.getString(23);
        saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));

        String cfreecustid = rs.getString(24);
        saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        Integer ibalanceflag = (Integer)rs.getObject(25);
        saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);

        BigDecimal nsubscription = (BigDecimal)rs.getObject(26);
        saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription));

        String ccreditnum = rs.getString(27);
        saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());

        BigDecimal nevaluatecarriage = (BigDecimal)rs.getObject(28);
        saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));

        String dmakedate = rs.getString(29);
        saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));

        String capproveid = rs.getString(30);
        saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

        String dapprovedate = rs.getString(31);
        saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));

        Integer fstatus = (Integer)rs.getObject(32);
        saleHeader.setFstatus(fstatus == null ? null : fstatus);

        String vnote = rs.getString(33);
        saleHeader.setVnote(vnote == null ? null : vnote.trim());

        String vdef1 = rs.getString(34);
        saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(35);
        saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(36);
        saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(37);
        saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(38);
        saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(39);
        saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(40);
        saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(41);
        saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(42);
        saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(43);
        saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String ccalbodyid = rs.getString(44);
        saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String csaleid = rs.getString(45);
        saleHeader.setCsaleid(csaleid == null ? null : csaleid.trim());

        String bretinvflag = rs.getString(46);
        saleHeader.setBretinvflag(bretinvflag == null ? null : new UFBoolean(bretinvflag.trim()));

        String boutendflag = rs.getString(47);
        saleHeader.setBoutendflag(boutendflag == null ? null : new UFBoolean(boutendflag.trim()));

        String binvoicendflag = rs.getString(48);
        saleHeader.setBinvoicendflag(binvoicendflag == null ? null : new UFBoolean(binvoicendflag.trim()));

        String breceiptendflag = rs.getString(49);
        saleHeader.setBreceiptendflag(breceiptendflag == null ? null : new UFBoolean(breceiptendflag.trim()));

        String ts = rs.getString(50);
        saleHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        v.addElement(saleHeader);
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
      saleorderHs = new SaleorderHVO[v.size()];
      v.copyInto(saleorderHs);
    }
    return saleorderHs;
  }

  public void autoSetReceiptFinish(DMDataVO[] dmVO)
    throws NamingException, RemoteException, SQLException, SystemException, BusinessException
  {
    beforeCallMethod("nc.bs.so.pub.DataControlDMO", "autoSetReceiptFinish", new Object[] { dmVO });

    if ((dmVO == null) || (dmVO.length <= 0)) {
      return;
    }

    SaleOrderDMO saledmo = new SaleOrderDMO();
    SaleorderBVO[] oldordbvos = (SaleorderBVO[])(SaleorderBVO[])saledmo.queryBodyDataForUpdateStatus(SoVoTools.getVOsOnlyValues(dmVO, "pkbillb"));

    autoSetReceiptFinish(dmVO, oldordbvos);

    afterCallMethod("nc.bs.so.pub.DataControlDMO", "autoSetReceiptFinish", new Object[] { dmVO });
  }

  public void autoSetReceiptFinish(DMDataVO[] dmVO, SaleorderBVO[] oldordbvos)
    throws NamingException, RemoteException, SQLException, SystemException, BusinessException
  {
    beforeCallMethod("nc.bs.so.pub.DataControlDMO", "autoSetReceiptFinish", new Object[] { dmVO });

    if ((dmVO == null) || (dmVO.length <= 0)) {
      return;
    }

    SaleOrderDMO saledmo = new SaleOrderDMO();
    if ((oldordbvos == null) || (oldordbvos.length <= 0)) {
      return;
    }
    HashMap hsdmvo = new HashMap();
    int i = 0; for (int loop = dmVO.length; i < loop; i++) {
      hsdmvo.put(dmVO[i].getAttributeValue("pkbillb"), dmVO[i]);
    }

    ArrayList autobvoslist = new ArrayList();
    ArrayList selfbvoslist = new ArrayList();
    DMDataVO dmvo = null;

    UFBoolean bifreceiptfinish = null;
    UFBoolean uffalse = new UFBoolean(false);
    UFBoolean uftrue = new UFBoolean(true);
    int i1 = 0; for (int loop = oldordbvos.length; i1 < loop; i1++)
    {
      dmvo = (DMDataVO)hsdmvo.get(oldordbvos[i1].getCorder_bid());
      if (dmvo == null) {
        continue;
      }
      if (dmvo.getAttributeValue("orderstatus") != null) {
        bifreceiptfinish = new UFBoolean(dmvo.getAttributeValue("orderstatus").toString().trim());
      }
      else {
        bifreceiptfinish = uffalse;
      }
      if (bifreceiptfinish.booleanValue()) {
        selfbvoslist.add(oldordbvos[i1]);
      }
      else {
        autobvoslist.add(oldordbvos[i1]);
      }

    }

    if (autobvoslist.size() > 0)
    {
      saledmo.processReceiptfinishState((SaleorderBVO[])(SaleorderBVO[])autobvoslist.toArray(new SaleorderBVO[autobvoslist.size()]));
    }

    if (selfbvoslist.size() > 0)
    {
      SaleorderBVO[] selfbvos = (SaleorderBVO[])(SaleorderBVO[])selfbvoslist.toArray(new SaleorderBVO[selfbvoslist.size()]);

      SaleorderBVO[] bakbvos = (SaleorderBVO[])(SaleorderBVO[])SoVoTools.getVOsByVOs("nc.vo.so.so001.SaleorderBVO", selfbvos, selfbvos[0].getAttributeNames(), selfbvos[0].getAttributeNames());

      SaleorderHVO[] hvos = (SaleorderHVO[])(SaleorderHVO[])saledmo.queryHeadDataForUpdateStatus(SoVoTools.getVOsOnlyValues(selfbvos, "csaleid"));

      int i11 = 0; for (int loop = selfbvos.length; i11 < loop; i11++)
      {
        dmvo = (DMDataVO)hsdmvo.get(selfbvos[i11].getCorder_bid());
        if ((dmvo == null) || (dmvo.getAttributeValue("orderstatus") == null))
        {
          continue;
        }
        selfbvos[i11].setCreceipttype("30");

        bifreceiptfinish = new UFBoolean(dmvo.getAttributeValue("orderstatus").toString().trim());

        if (bifreceiptfinish.booleanValue()) {
          selfbvos[i11].setBifreceiptfinish(uftrue);
        } else {
          selfbvos[i11].setBifreceiptfinish(uffalse);
          selfbvos[i11].setBifinventoryfinish(uffalse);
        }

        if ((selfbvos[i11].getBifinventoryfinish() != null) && (selfbvos[i11].getBifinventoryfinish().booleanValue()) && (selfbvos[i11].getBifreceiptfinish() != null) && (selfbvos[i11].getBifreceiptfinish().booleanValue()) && (selfbvos[i11].getBifinvoicefinish() != null) && (selfbvos[i11].getBifinvoicefinish().booleanValue()))
        {
          if ((selfbvos[i11].getFrowstatus() == null) || (selfbvos[i11].getFrowstatus().intValue() != 2))
            continue;
          selfbvos[i11].setFrowstatus(new Integer(6));
        }
        else
        {
          if ((selfbvos[i11].getFrowstatus() == null) || (selfbvos[i11].getFrowstatus().intValue() != 6))
            continue;
          selfbvos[i11].setFrowstatus(new Integer(2));
        }

      }

      SOToolsDMO.updateBatch(selfbvos, new String[] { "bifreceiptfinish", "bifinventoryfinish" }, new String[] { "bifreceiptfinish", "bifinventoryfinish" }, "so_saleexecute", new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

      SOToolsDMO.updateBatch(selfbvos, new String[] { "frowstatus" }, new String[] { "frowstatus" }, "so_saleorder_b", new String[] { "corder_bid" }, new String[] { "corder_bid" });

      saledmo.setBillStatus(hvos, "breceiptendflag");

      saledmo.updateAtpByOrdRows(hvos, bakbvos, selfbvos);

      saledmo.updateArByOrdRows(hvos, bakbvos, selfbvos);

      saledmo.updateOrdBalanceByOrdRows(hvos, bakbvos, selfbvos);
    }

    saledmo.setOrdLastDate("dlastconsigdate", oldordbvos, ((IServiceProviderSerivce)NCLocator.getInstance().lookup(IServiceProviderSerivce.class.getName())).getServerTime() == null ? null : ((IServiceProviderSerivce)NCLocator.getInstance().lookup(IServiceProviderSerivce.class.getName())).getServerTime().getDate());

    afterCallMethod("nc.bs.so.pub.DataControlDMO", "autoSetReceiptFinish", new Object[] { dmVO });
  }

  public UFBoolean[] getCloseStatus(String[] sBids)
    throws SQLException, Exception
  {
    if ((sBids == null) || (sBids.length <= 0))
      return null;
    UFBoolean[] aryRslt = null;

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sSql = null;
    try {
      con = getConnection();

      Hashtable hInvMan = new Hashtable();
      String sTmp = null;
      UFBoolean bTmp = null;

      sSql = "select CSALE_BID,bifreceiptfinish from so_saleexecute where 1 < 0 ";
      for (int i = 0; i < sBids.length; i++) {
        sSql = sSql + "or CSALE_BID ='";
        sSql = sSql + sBids[i];
        sSql = sSql + "' ";
      }
      stmt = con.prepareStatement(sSql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        String sbid = rs.getString(1);
        sTmp = rs.getString(2);
        if ((sTmp != null) && (new UFBoolean(sTmp).booleanValue()))
          bTmp = new UFBoolean(true);
        else {
          bTmp = new UFBoolean(false);
        }
        hInvMan.put(sbid, bTmp);
      }

      aryRslt = new UFBoolean[sBids.length];
      for (int i = 0; i < sBids.length; i++)
        if (hInvMan.containsKey(sBids[i]))
          aryRslt[i] = ((UFBoolean)hInvMan.get(sBids[i]));
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
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
    return aryRslt;
  }

  public UFDouble[] getReceiptNumber(DMDataVO[] dmVO)
    throws BusinessException
  {
    Connection con = null;
    try
    {
      con = getConnection("getReceiptNumber");
      UFDouble[] number = new UFDouble[dmVO.length];
      for (int i = 0; i < dmVO.length; i++) {
        if (!dmVO[i].getAttributeValue("vbilltype").equals("30"))
          continue;
        number[i] = getReceiptNumber(dmVO[i].getAttributeValue("pkbillh").toString(), dmVO[i].getAttributeValue("pkbillb").toString());
      }

      return number;
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
    finally
    {
      try {
        removeConnection("getReceiptNumber"); 
        } catch (Exception e) {
        	throw new BusinessException(e.getMessage());
      }
    }
  }

  public UFBoolean[] getSupplyflag(DMDataVO[] dmVO)
    throws BusinessException
  {
    if (dmVO == null)
      return null;
    try {
      UFBoolean[] flags = new UFBoolean[dmVO.length];

      for (int i = 0; i < dmVO.length; i++) {
        String sBid = (String)dmVO[i].getAttributeValue("pkbillb");
        flags[i] = getSupplyflag(sBid);
      }

      return flags;
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
  }

  /** @deprecated */
  private void isSaleOverOut(String IC003, UFDouble IC004, DMDataVO[] dmVO)
    throws SQLException, BusinessException
  {
    if ((dmVO == null) || (dmVO.length <= 0)) {
      return;
    }
    HashMap hsrow = SOToolsDMO.getAnyValueSORow("so_saleorder_b,so_saleexecute", new String[] { "so_saleorder_b.nnumber", "isnull(ntotalreceiptnumber,0)", "bifreceiptfinish", "nouttoplimit" }, "so_saleorder_b.corder_bid", SoVoTools.getVOsOnlyValues(dmVO, "pkbillb"), " so_saleorder_b.csaleid = so_saleexecute.csaleid and so_saleorder_b.corder_bid = so_saleexecute.csale_bid ");

    if (hsrow == null) {
      return;
    }
    UFDouble dblNumber = null;
    UFDouble dbltotalreceiptnumber = null;
    UFBoolean bifreceiptfinish = null;
    SORowData row = null;
    String corder_bid = null;
    UFDouble uf0 = SoVoConst.duf0;
    UFBoolean uffalse = new UFBoolean(false);
    UFDouble SaleOutNum = null;
    UFDouble nResult = null;

    boolean isOverSend = false;

    UFBoolean orderstatus = null;

    if ("Y".equals(IC003)) {
      isOverSend = true;
    }
    UFDouble uf1 = new UFDouble(1);

    IC004 = IC004.multiply(0.01D).add(1.0D);

    HashMap hstotalreceiptnumber = new HashMap();

    UFDouble dtemp = null;
    UFDouble nouttoplimit = null;

    int i = 0; for (int loop = dmVO.length; i < loop; i++) {
      corder_bid = (String)dmVO[i].getAttributeValue("pkbillb");
      if (corder_bid == null)
        continue;
      SaleOutNum = (UFDouble)dmVO[i].getAttributeValue("ndelivernum");
      if (SaleOutNum == null) {
        continue;
      }
      row = (SORowData)hsrow.get(corder_bid);
      if (row == null) {
        continue;
      }
      dblNumber = row.getUFDouble(0);
      dblNumber = dblNumber == null ? uf0 : dblNumber;
      dbltotalreceiptnumber = row.getUFDouble(1);
      dbltotalreceiptnumber = dbltotalreceiptnumber == null ? uf0 : dbltotalreceiptnumber;

      bifreceiptfinish = row.getUFBoolean(2);
      bifreceiptfinish = bifreceiptfinish == null ? uffalse : bifreceiptfinish;

      nouttoplimit = row.getUFDouble(3);
      if (nouttoplimit == null)
        nouttoplimit = IC004;
      else {
        nouttoplimit = nouttoplimit.multiply(0.01D).add(1.0D);
      }
      dtemp = (UFDouble)hstotalreceiptnumber.get(corder_bid);
      if (dtemp == null) {
        hstotalreceiptnumber.put(corder_bid, dbltotalreceiptnumber);
        dtemp = dbltotalreceiptnumber;
      }
      dtemp = SoVoTools.add(dtemp, SaleOutNum);
      hstotalreceiptnumber.put(corder_bid, dtemp);

      if (dblNumber.doubleValue() >= 0.0D)
      {
        if (isOverSend) {
          nResult = nouttoplimit.multiply(dblNumber).sub(dtemp);
        }
        else {
          nResult = uf1.multiply(dblNumber).sub(dtemp);
        }

        if (nResult.doubleValue() < 0.0D) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("todm", "UPPtodm-000000"));
        }
      }
      else
      {
        if (isOverSend)
        {
          nResult = nouttoplimit.multiply(dblNumber).sub(dtemp);
        }
        else {
          nResult = uf1.multiply(dblNumber).sub(dtemp);
        }

        if (nResult.doubleValue() > 0.0D) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("todm", "UPPtodm-000000"));
        }

      }

      if (bifreceiptfinish.booleanValue()) {
        if ((dblNumber.doubleValue() > 0.0D) && (SaleOutNum.doubleValue() > 0.0D)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("todm", "UPPtodm-000001"));
        }

        if ((dblNumber.doubleValue() < 0.0D) && (SaleOutNum.doubleValue() < 0.0D))
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("todm", "UPPtodm-000001"));
      }
    }
  }

  private SaleOrderVO[] querySaleData(String where)
    throws SQLException
  {
    String strSql = "select so_sale.csaleid, so_sale.vreceiptcode, so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass, so_sale.finvoicetype,so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate, so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid, so_sale.vreceiveaddress, so_sale.creceiptcorpid, so_sale.ctransmodeid, so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag, so_sale.cfreecustid, so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, so_sale.dapprovedate, so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6, so_sale.vdef7, so_sale.vdef8,so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,so_sale.pk_corp,so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,so_sale.breceiptendflag, so_sale.ts ,so_saleorder_b.corder_bid, so_saleorder_b.csaleid cbodysaleid, so_saleorder_b.pk_corp cbodypk_corp, so_saleorder_b.creceipttype cbodyreceipttype, csourcebillid, csourcebillbodyid, cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, cbodywarehouseid, dconsigndate, ddeliverdate, blargessflag, ceditsaleid, beditflag, so_saleorder_b.veditreason vbodyeditreason, ccurrencytypeid, nitemdiscountrate, so_saleorder_b.ndiscountrate nbodydiscountrate, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, so_saleorder_b.coperatorid cbodyoperatorid, frowstatus, frownote,cinvbasdocid,cbatchid,fbatchstatus,cbomorderid,cfreezeid,ct_manageid , so_saleorder_b.ts bodyts,so_saleorder_b.creceiptareaid ,so_saleorder_b.vreceiveaddress vbodyreceiveaddress,so_saleorder_b.creceiptcorpid cbodyreceiptcorpid,so_saleorder_b.cadvisecalbodyid ,ntotalpaymny, ntotalreceiptnumber, ntotalinvoicenumber, ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, bifinvoicefinish, bifreceiptfinish, bifinventoryfinish, bifpayfinish, bifpaybalance, bifpaysign, nassistcurdiscountmny, nassistcursummny, nassistcurmny, nassistcurtaxmny, nassistcurtaxnetprice, nassistcurnetprice, nassistcurtaxprice, nassistcurprice, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, vfree4, vfree5, so_saleexecute.vdef1 vbodydef1, so_saleexecute.vdef2 vbodydef2, so_saleexecute.vdef3 vbodydef3, so_saleexecute.vdef4 vbodydef4, so_saleexecute.vdef5 vbodydef5, so_saleexecute.vdef6 vbodydef6, ntotalplanreceiptnumber,ntotalcarrynumber,ntotalreturnnumber from so_sale LEFT JOIN so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid LEFT JOIN so_saleexecute ON so_saleorder_b.csaleid = so_saleexecute.csaleid AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid  LEFT JOIN bd_sendtype ON so_sale.ctransmodeid = bd_sendtype.pk_sendtype LEFT JOIN bd_invbasdoc ON so_saleorder_b.cinvbasdocid = bd_invbasdoc.pk_invbasdoc   where issendarranged = 'Y'  and bifreceiptfinish = 'N'";

    strSql = strSql + " and beditflag = 'N' and  bd_invbasdoc.discountflag = 'N' and bd_invbasdoc.laborflag = 'N' ";

    if ((where != null) && (!where.equals(""))) {
      strSql = strSql + where;
    }
    strSql = strSql + " order by so_sale.csaleid ";

    SaleOrderVO[] saleorders = null;

    SaleorderHVO saleHeader = null;

    Vector v = new Vector();
    Vector vbody = new Vector();

    Connection con = null;
    PreparedStatement stmt = null;

    con = getConnection();

    String sID = "";
    try
    {
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        String csaleid = rs.getString(1);

        if (!csaleid.equals(sID)) {
          if (!sID.equals("")) {
            SaleOrderVO saleorder = new SaleOrderVO();
            saleorder.setParentVO(saleHeader);
            if (vbody.size() > 0) {
              SaleorderBVO[] saleorderbody = new SaleorderBVO[vbody.size()];

              vbody.copyInto(saleorderbody);
              saleorder.setChildrenVO(saleorderbody);
            }
            v.addElement(saleorder);
          }
          saleHeader = new SaleorderHVO();
          vbody = new Vector();
          sID = csaleid;
        }
        saleHeader.setCsaleid(csaleid == null ? null : csaleid.trim());

        String vreceiptcode = rs.getString(2);
        saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString(3);
        saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String cbiztype = rs.getString(4);
        saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        Integer finvoiceclass = (Integer)rs.getObject(5);
        saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);

        Integer finvoicetype = (Integer)rs.getObject(6);
        saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);

        String vaccountyear = rs.getString(7);
        saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());

        String binitflag = rs.getString(8);
        saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));

        String dbilldate = rs.getString(9);
        saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));

        String ccustomerid = rs.getString(10);
        saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        String cdeptid = rs.getString(11);
        saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(12);
        saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String coperatorid = rs.getString(13);
        saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String ctermprotocolid = rs.getString(14);
        saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());

        String csalecorpid = rs.getString(15);
        saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());

        String creceiptcustomerid = rs.getString(16);
        saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());

        String vreceiveaddress = rs.getString(17);
        saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString(18);
        saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String ctransmodeid = rs.getString(19);
        saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        BigDecimal ndiscountrate = (BigDecimal)rs.getObject(20);
        saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));

        String cwarehouseid = rs.getString(21);
        saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String veditreason = rs.getString(22);
        saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());

        String bfreecustflag = rs.getString(23);
        saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));

        String cfreecustid = rs.getString(24);
        saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        Integer ibalanceflag = (Integer)rs.getObject(25);
        saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);

        BigDecimal nsubscription = (BigDecimal)rs.getObject(26);
        saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription));

        String ccreditnum = rs.getString(27);
        saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());

        BigDecimal nevaluatecarriage = (BigDecimal)rs.getObject(28);
        saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));

        String dmakedate = rs.getString(29);
        saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));

        String capproveid = rs.getString(30);
        saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

        String dapprovedate = rs.getString(31);
        saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));

        Integer fstatus = (Integer)rs.getObject(32);
        saleHeader.setFstatus(fstatus == null ? null : fstatus);

        String vnote = rs.getString(33);
        saleHeader.setVnote(vnote == null ? null : vnote.trim());

        String vdef1 = rs.getString(34);
        saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(35);
        saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(36);
        saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(37);
        saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(38);
        saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(39);
        saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(40);
        saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(41);
        saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(42);
        saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(43);
        saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String ccalbodyid = rs.getString(44);
        saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String pk_corp = rs.getString(45);
        saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String bretinvflag = rs.getString(46);
        saleHeader.setBretinvflag(bretinvflag == null ? null : new UFBoolean(bretinvflag.trim()));

        String boutendflag = rs.getString(47);
        saleHeader.setBoutendflag(boutendflag == null ? null : new UFBoolean(boutendflag.trim()));

        String binvoicendflag = rs.getString(48);
        saleHeader.setBinvoicendflag(binvoicendflag == null ? null : new UFBoolean(binvoicendflag.trim()));

        String breceiptendflag = rs.getString(49);
        saleHeader.setBreceiptendflag(breceiptendflag == null ? null : new UFBoolean(breceiptendflag.trim()));

        String ts = rs.getString(50);
        saleHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        SaleorderBVO saleItem = new SaleorderBVO();

        String corder_bid = rs.getString("corder_bid");
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String cbodysaleid = rs.getString("cbodysaleid");
        saleItem.setCsaleid(cbodysaleid == null ? null : cbodysaleid.trim());

        String ccorpid = rs.getString("cbodypk_corp");
        saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());

        String cbodyreceipttype = rs.getString("cbodyreceipttype");
        saleItem.setCreceipttype(cbodyreceipttype == null ? null : cbodyreceipttype.trim());

        String csourcebillid = rs.getString("csourcebillid");
        saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString("cunitid");
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String dconsigndate = rs.getString("dconsigndate");
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString("ddeliverdate");
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString("blargessflag");
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String ceditsaleid = rs.getString("ceditsaleid");
        saleItem.setCeditsaleid(ceditsaleid == null ? null : ceditsaleid.trim());

        String beditflag = rs.getString("beditflag");
        saleItem.setBeditflag(beditflag == null ? null : new UFBoolean(beditflag.trim()));

        String vbodyeditreason = rs.getString("vbodyeditreason");
        saleItem.setVeditreason(vbodyeditreason == null ? null : vbodyeditreason.trim());

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal nitemdiscountrate = (BigDecimal)rs.getObject("nitemdiscountrate");

        saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));

        BigDecimal nbodydiscountrate = (BigDecimal)rs.getObject("nbodydiscountrate");

        saleItem.setNdiscountrate(nbodydiscountrate == null ? null : new UFDouble(nbodydiscountrate));

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");

        saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");

        saleItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurprice = (BigDecimal)rs.getObject("noriginalcurprice");

        saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginalcurtaxprice = (BigDecimal)rs.getObject("noriginalcurtaxprice");

        saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(noriginalcurtaxprice));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");

        saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");

        saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");

        saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject("noriginalcurdiscountmny");

        saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nprice = (BigDecimal)rs.getObject("nprice");
        saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");

        saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject("ndiscountmny");

        saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        String cbodyoperatorid = rs.getString("cbodyoperatorid");
        saleItem.setCoperatorid(cbodyoperatorid == null ? null : cbodyoperatorid.trim());

        Integer frowstatus = (Integer)rs.getObject("frowstatus");
        saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);

        String frownote = rs.getString("frownote");
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cbatchid = rs.getString("cbatchid");
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        Integer fbatchstatus = (Integer)rs.getObject("fbatchstatus");
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String cbomorderid = rs.getString("cbomorderid");
        saleItem.setCbomorderid(cbomorderid == null ? null : cbomorderid.trim());

        String cfreezeid = rs.getString("cfreezeid");
        saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        String ct_manageid = rs.getString("ct_manageid");
        saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());

        String bodyts = rs.getString("bodyts");
        saleItem.setTs(bodyts == null ? null : new UFDateTime(bodyts.trim()));

        String creceiptareaid = rs.getString("creceiptareaid");
        saleItem.setCreceiptareaid(creceiptareaid == null ? null : creceiptareaid.trim());

        String vbodyreceiveaddress = rs.getString("vbodyreceiveaddress");

        saleItem.setVreceiveaddress(vbodyreceiveaddress == null ? null : vbodyreceiveaddress.trim());

        String cbodyreceiptcorpid = rs.getString("cbodyreceiptcorpid");
        saleItem.setCreceiptcorpid(cbodyreceiptcorpid == null ? null : cbodyreceiptcorpid.trim());

        String cadvisecalbodyid = rs.getString("cadvisecalbodyid");
        saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());

        BigDecimal ntotalpaymny = (BigDecimal)rs.getObject("ntotalpaymny");

        saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));

        BigDecimal ntotalreceiptnumber = (BigDecimal)rs.getObject("ntotalreceiptnumber");

        saleItem.setNtotalreceiptnumber(ntotalreceiptnumber == null ? null : new UFDouble(ntotalreceiptnumber));

        BigDecimal ntotalinvoicenumber = (BigDecimal)rs.getObject("ntotalinvoicenumber");

        saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));

        BigDecimal ntotalinvoicemny = (BigDecimal)rs.getObject("ntotalinvoicemny");

        saleItem.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));

        BigDecimal ntotalinventorynumber = (BigDecimal)rs.getObject("ntotalinventorynumber");

        saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(ntotalinventorynumber));

        BigDecimal ntotalbalancenumber = (BigDecimal)rs.getObject("ntotalbalancenumber");

        saleItem.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));

        BigDecimal ntotalsignnumber = (BigDecimal)rs.getObject("ntotalsignnumber");

        saleItem.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));

        BigDecimal ntotalcostmny = (BigDecimal)rs.getObject("ntotalcostmny");

        saleItem.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));

        String bifinvoicefinish = rs.getString("bifinvoicefinish");
        saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));

        String bifreceiptfinish = rs.getString("bifreceiptfinish");
        saleItem.setBifreceiptfinish(bifreceiptfinish == null ? null : new UFBoolean(bifreceiptfinish.trim()));

        String bifinventoryfinish = rs.getString("bifinventoryfinish");
        saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish.trim()));

        String bifpayfinish = rs.getString("bifpayfinish");
        saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

        String bifpaybalance = rs.getString("bifpaybalance");
        saleItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String bifpaysign = rs.getString("bifpaysign");
        saleItem.setBifpaysign(bifpaysign == null ? null : new UFBoolean(bifpaysign.trim()));

        BigDecimal nassistcurdiscountmny = (BigDecimal)rs.getObject("nassistcurdiscountmny");

        saleItem.setNassistcurdiscountmny(nassistcurdiscountmny == null ? null : new UFDouble(nassistcurdiscountmny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject("nassistcursummny");

        saleItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject("nassistcurmny");

        saleItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject("nassistcurtaxmny");

        saleItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        BigDecimal nassistcurtaxnetprice = (BigDecimal)rs.getObject("nassistcurtaxnetprice");

        saleItem.setNassistcurtaxnetprice(nassistcurtaxnetprice == null ? null : new UFDouble(nassistcurtaxnetprice));

        BigDecimal nassistcurnetprice = (BigDecimal)rs.getObject("nassistcurnetprice");

        saleItem.setNassistcurnetprice(nassistcurnetprice == null ? null : new UFDouble(nassistcurnetprice));

        BigDecimal nassistcurtaxprice = (BigDecimal)rs.getObject("nassistcurtaxprice");

        saleItem.setNassistcurtaxprice(nassistcurtaxprice == null ? null : new UFDouble(nassistcurtaxprice));

        BigDecimal nassistcurprice = (BigDecimal)rs.getObject("nassistcurprice");

        saleItem.setNassistcurprice(nassistcurprice == null ? null : new UFDouble(nassistcurprice));

        String cprojectid = rs.getString("cprojectid");
        saleItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        saleItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String cprojectid3 = rs.getString("cprojectid3");
        saleItem.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());

        String vfree1 = rs.getString("vfree1");
        saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vbodydef1 = rs.getString("vbodydef1");
        saleItem.setVdef1(vbodydef1 == null ? null : vbodydef1.trim());

        String vbodydef2 = rs.getString("vbodydef2");
        saleItem.setVdef2(vbodydef2 == null ? null : vbodydef2.trim());

        String vbodydef3 = rs.getString("vbodydef3");
        saleItem.setVdef3(vbodydef3 == null ? null : vbodydef3.trim());

        String vbodydef4 = rs.getString("vbodydef4");
        saleItem.setVdef4(vbodydef4 == null ? null : vbodydef4.trim());

        String vbodydef5 = rs.getString("vbodydef5");
        saleItem.setVdef5(vbodydef5 == null ? null : vbodydef5.trim());

        String vbodydef6 = rs.getString("vbodydef6");
        saleItem.setVdef6(vbodydef6 == null ? null : vbodydef6.trim());

        BigDecimal ntotalplanreceiptnumber = (BigDecimal)rs.getObject("ntotalplanreceiptnumber");

        saleItem.setNtotalplanreceiptnumber(ntotalplanreceiptnumber == null ? null : new UFDouble(ntotalplanreceiptnumber));

        BigDecimal ntotalcarrynumber = (BigDecimal)rs.getObject("ntotalcarrynumber");

        saleItem.setNtotalcarrynumber(ntotalcarrynumber == null ? null : new UFDouble(ntotalcarrynumber));

        BigDecimal ntotalreturnnumber = (BigDecimal)rs.getObject("ntotalreturnnumber");

        saleItem.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(ntotalreturnnumber));

        vbody.addElement(saleItem);
      }

      if (vbody.size() > 0) {
        SaleOrderVO saleorder = new SaleOrderVO();
        saleorder.setParentVO(saleHeader);
        SaleorderBVO[] saleorderbody = new SaleorderBVO[vbody.size()];
        vbody.copyInto(saleorderbody);
        saleorder.setChildrenVO(saleorderbody);
        v.addElement(saleorder);
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
    if (v != null) {
      saleorders = new SaleOrderVO[v.size()];
      v.copyInto(saleorders);
    }
    return saleorders;
  }

  private SaleOrderVO[] querySaleData2(String where)
    throws SQLException
  {
    String strSql = null;
    StringBuffer stbSql = new StringBuffer();
    stbSql.append("SELECT so_sale.csaleid, so_sale.vreceiptcode, so_sale.creceipttype, so_sale.cbiztype, ");

    stbSql.append("so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.csalecorpid, ");

    stbSql.append("so_sale.vreceiveaddress, so_sale.creceiptcorpid, so_sale.ctransmodeid, so_sale.vnote vheadnote, ");

    stbSql.append("so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, ");

    stbSql.append("so_sale.vdef6, so_sale.vdef7, so_sale.vdef8, so_sale.vdef9, so_sale.vdef10, ");

    stbSql.append("so_sale.pk_corp, so_sale.ts, so_saleorder_b.corder_bid, ");

    stbSql.append("so_saleorder_b.cinventoryid, so_saleorder_b.cunitid, so_saleorder_b.cpackunitid, ");

    stbSql.append("so_saleorder_b.nnumber, so_saleorder_b.npacknumber, ");
    stbSql.append("so_saleorder_b.cbodywarehouseid, so_saleorder_b.dconsigndate, ");

    stbSql.append("so_saleorder_b.ddeliverdate, so_saleorder_b.blargessflag, ");

    stbSql.append("so_saleorder_b.ntaxprice, ");
    stbSql.append("so_saleorder_b.frownote, so_saleorder_b.cbatchid, ");
    stbSql.append("so_saleorder_b.fbatchstatus, so_saleorder_b.cfreezeid, ");

    stbSql.append("so_saleorder_b.ts AS bodyts, so_saleorder_b.creceiptareaid,so_saleorder_b.vreceiveaddress vbodyreceiveaddress, so_saleorder_b.creceiptcorpid cbodyreceiptcorpid, ");

    stbSql.append("so_saleorder_b.cadvisecalbodyid, so_saleexecute.ntotalreceiptnumber, ");

    stbSql.append("so_saleexecute.ntotalinvoicenumber, so_saleexecute.ntotalinventorynumber, ");

    stbSql.append("so_saleexecute.ntotalsignnumber, so_saleexecute.cprojectid, ");

    stbSql.append("so_saleexecute.cprojectphaseid, so_saleexecute.vfree1, so_saleexecute.vfree2, ");

    stbSql.append("so_saleexecute.vfree3, so_saleexecute.vfree4, so_saleexecute.vfree5, ");

    stbSql.append("so_saleexecute.vdef1 AS vbodydef1, so_saleexecute.vdef2 AS vbodydef2, ");

    stbSql.append("so_saleexecute.vdef3 AS vbodydef3, so_saleexecute.vdef4 AS vbodydef4, ");

    stbSql.append("so_saleexecute.vdef5 AS vbodydef5, so_saleexecute.vdef6 AS vbodydef6, ");

    stbSql.append("so_saleexecute.ntotalplanreceiptnumber, so_saleexecute.ntotalcarrynumber, ");

    stbSql.append("so_saleexecute.ntotalreturnnumber,so_saleorder_b.cconsigncorpid, ");

    stbSql.append("so_saleorder_b.creccalbodyid,so_saleorder_b.crecwareid,so_saleorder_b.tconsigntime,so_saleorder_b.tdelivertime,so_saleorder_b.crecaddrnode,bd_cubasdoc.pk_corp1, ");

    stbSql.append("so_sale.bdeliver,  ");

    stbSql.append("so_sale.vdef11, so_sale.vdef12, so_sale.vdef13, so_sale.vdef14, so_sale.vdef15, ");
    stbSql.append("so_sale.vdef16, so_sale.vdef17, so_sale.vdef18, so_sale.vdef19, so_sale.vdef20, ");

    stbSql.append("so_sale.pk_defdoc1, so_sale.pk_defdoc2, so_sale.pk_defdoc3, so_sale.pk_defdoc4, so_sale.pk_defdoc5, ");
    stbSql.append("so_sale.pk_defdoc6, so_sale.pk_defdoc7, so_sale.pk_defdoc8, so_sale.pk_defdoc9, so_sale.pk_defdoc10, ");

    stbSql.append("so_sale.pk_defdoc11, so_sale.pk_defdoc12, so_sale.pk_defdoc13, so_sale.pk_defdoc14, so_sale.pk_defdoc15, ");
    stbSql.append("so_sale.pk_defdoc16, so_sale.pk_defdoc17, so_sale.pk_defdoc18, so_sale.pk_defdoc19, so_sale.pk_defdoc20, ");

    stbSql.append("so_saleexecute.vdef7 AS vbodydef7, so_saleexecute.vdef8 AS vbodydef8, ");
    stbSql.append("so_saleexecute.vdef9 AS vbodydef9, so_saleexecute.vdef10 AS vbodydef10, ");
    stbSql.append("so_saleexecute.vdef11 AS vbodydef11, so_saleexecute.vdef12 AS vbodydef12, ");
    stbSql.append("so_saleexecute.vdef13 AS vbodydef13, so_saleexecute.vdef14 AS vbodydef14, ");
    stbSql.append("so_saleexecute.vdef15 AS vbodydef15, so_saleexecute.vdef16 AS vbodydef16, ");
    stbSql.append("so_saleexecute.vdef17 AS vbodydef17, so_saleexecute.vdef18 AS vbodydef18, ");
    stbSql.append("so_saleexecute.vdef19 AS vbodydef19, so_saleexecute.vdef20 AS vbodydef20, ");

    stbSql.append("so_saleexecute.pk_defdoc1 AS pkbodydefdoc1, so_saleexecute.pk_defdoc2 AS pkbodydefdoc2, ");
    stbSql.append("so_saleexecute.pk_defdoc3 AS pkbodydefdoc3, so_saleexecute.pk_defdoc4 AS pkbodydefdoc4, ");
    stbSql.append("so_saleexecute.pk_defdoc5 AS pkbodydefdoc5, so_saleexecute.pk_defdoc6 AS pkbodydefdoc6, ");
    stbSql.append("so_saleexecute.pk_defdoc7 AS pkbodydefdoc7, so_saleexecute.pk_defdoc8 AS pkbodydefdoc8, ");
    stbSql.append("so_saleexecute.pk_defdoc9 AS pkbodydefdoc9, so_saleexecute.pk_defdoc10 AS pkbodydefdoc10, ");

    stbSql.append("so_saleexecute.pk_defdoc11 AS pkbodydefdoc11, so_saleexecute.pk_defdoc12 AS pkbodydefdoc12, ");
    stbSql.append("so_saleexecute.pk_defdoc13 AS pkbodydefdoc13, so_saleexecute.pk_defdoc14 AS pkbodydefdoc14, ");
    stbSql.append("so_saleexecute.pk_defdoc15 AS pkbodydefdoc15, so_saleexecute.pk_defdoc16 AS pkbodydefdoc16, ");
    stbSql.append("so_saleexecute.pk_defdoc17 AS pkbodydefdoc17, so_saleexecute.pk_defdoc18 AS pkbodydefdoc18, ");
    stbSql.append("so_saleexecute.pk_defdoc19 AS pkbodydefdoc19, so_saleexecute.pk_defdoc10 AS pkbodydefdoc20, ");
    stbSql.append("so_saleorder_b.cquoteunitid AS cquoteunitid, so_saleorder_b.nquoteunitnum AS nquoteunitnum, ");
    stbSql.append(" so_saleexecute.ntranslossnum as ntranslossnum ");

    stbSql.append("FROM so_sale LEFT OUTER JOIN ");
    stbSql.append("so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid LEFT OUTER JOIN ");

    stbSql.append("so_saleexecute ON so_saleorder_b.csaleid = so_saleexecute.csaleid AND ");

    stbSql.append("so_saleorder_b.corder_bid = so_saleexecute.csale_bid LEFT OUTER JOIN ");

    stbSql.append("bd_sendtype ON ");
    stbSql.append("so_sale.ctransmodeid = bd_sendtype.pk_sendtype LEFT OUTER JOIN ");

    stbSql.append("bd_invbasdoc ON ");
    stbSql.append("so_saleorder_b.cinvbasdocid = bd_invbasdoc.pk_invbasdoc LEFT OUTER JOIN ");

    stbSql.append("bd_cumandoc ON ");
    stbSql.append("so_saleorder_b.creceiptcorpid = bd_cumandoc.pk_cumandoc LEFT OUTER JOIN ");

    stbSql.append("bd_cubasdoc ON ");
    stbSql.append("bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc ");

    stbSql.append(" where ");
    stbSql.append(" bifreceiptfinish = 'N' and COALESCE(so_saleorder_b.bdericttrans,'N')='N' and COALESCE(so_sale.bdeliver,'N') = 'Y' ");

    strSql = stbSql.toString();

    strSql = strSql + " and beditflag = 'N' and  bd_invbasdoc.discountflag = 'N' and bd_invbasdoc.laborflag = 'N' ";

    if ((where != null) && (!where.equals(""))) {
      strSql = strSql + where;
    }
    strSql = strSql + " order by so_sale.csaleid ";

    SaleOrderVO[] saleorders = null;

    SaleorderHVO saleHeader = null;

    Vector v = new Vector();
    Vector vbody = new Vector();

    Connection con = null;
    PreparedStatement stmt = null;

    con = getConnection();

    String sID = "";
    try
    {
      stmt = con.prepareStatement(strSql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        String csaleid = rs.getString("csaleid");

        if (!csaleid.equals(sID)) {
          if (!sID.equals("")) {
            SaleOrderVO saleorder = new SaleOrderVO();
            saleorder.setParentVO(saleHeader);
            if (vbody.size() > 0) {
              SaleorderBVO[] saleorderbody = new SaleorderBVO[vbody.size()];

              vbody.copyInto(saleorderbody);
              saleorder.setChildrenVO(saleorderbody);
            }
            v.addElement(saleorder);
          }
          saleHeader = new SaleorderHVO();
          vbody = new Vector();
          sID = csaleid;
        }
        saleHeader.setCsaleid(csaleid == null ? null : csaleid.trim());

        String vreceiptcode = rs.getString("vreceiptcode");
        saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString("creceipttype");
        saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String cbiztype = rs.getString("cbiztype");
        saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        String ccustomerid = rs.getString("ccustomerid");
        saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        String cdeptid = rs.getString("cdeptid");
        saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString("cemployeeid");
        saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String csalecorpid = rs.getString("csalecorpid");
        saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());

        String vreceiveaddress = rs.getString("vreceiveaddress");
        saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());

        String creceiptcorpid = rs.getString("creceiptcorpid");
        saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());

        String ctransmodeid = rs.getString("ctransmodeid");
        saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());

        String vnote = rs.getString("vheadnote");
        saleHeader.setVnote(vnote == null ? null : vnote.trim());

        String vdef1 = rs.getString("vdef1");
        saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString("vdef7");
        saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString("vdef8");
        saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString("vdef9");
        saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString("vdef10");
        saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String pk_corp = rs.getString("pk_corp");
        saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String ts = rs.getString("ts");
        saleHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

        SaleorderBVO saleItem = new SaleorderBVO();

        String corder_bid = rs.getString("corder_bid");
        saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        String cunitid = rs.getString("cunitid");
        saleItem.setCunitid(cunitid == null ? null : cunitid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal nnumber = (BigDecimal)rs.getObject("nnumber");
        saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));

        BigDecimal npacknumber = (BigDecimal)rs.getObject("npacknumber");

        saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String dconsigndate = rs.getString("dconsigndate");
        saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));

        String ddeliverdate = rs.getString("ddeliverdate");
        saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));

        String blargessflag = rs.getString("blargessflag");
        saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        BigDecimal ntaxprice = (BigDecimal)rs.getObject("ntaxprice");
        saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));

        String frownote = rs.getString("frownote");
        saleItem.setFrownote(frownote == null ? null : frownote.trim());

        String cbatchid = rs.getString("cbatchid");
        saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        Integer fbatchstatus = (Integer)rs.getObject("fbatchstatus");
        saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);

        String cfreezeid = rs.getString("cfreezeid");
        saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        String bodyts = rs.getString("bodyts");
        saleItem.setTs(bodyts == null ? null : new UFDateTime(bodyts.trim()));

        String creceiptareaid = rs.getString("creceiptareaid");
        saleItem.setCreceiptareaid(creceiptareaid == null ? null : creceiptareaid.trim());

        String vbodyreceiveaddress = rs.getString("vbodyreceiveaddress");

        saleItem.setVreceiveaddress(vbodyreceiveaddress == null ? null : vbodyreceiveaddress.trim());

        String cbodyreceiptcorpid = rs.getString("cbodyreceiptcorpid");
        saleItem.setCreceiptcorpid(cbodyreceiptcorpid == null ? null : cbodyreceiptcorpid.trim());

        String cadvisecalbodyid = rs.getString("cadvisecalbodyid");
        saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());

        BigDecimal ntotalreceiptnumber = (BigDecimal)rs.getObject("ntotalreceiptnumber");

        saleItem.setNtotalreceiptnumber(ntotalreceiptnumber == null ? null : new UFDouble(ntotalreceiptnumber));

        BigDecimal ntotalinvoicenumber = (BigDecimal)rs.getObject("ntotalinvoicenumber");

        saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));

        BigDecimal ntotalinventorynumber = (BigDecimal)rs.getObject("ntotalinventorynumber");

        saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(ntotalinventorynumber));

        BigDecimal ntotalsignnumber = (BigDecimal)rs.getObject("ntotalsignnumber");

        saleItem.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));

        String cprojectid = rs.getString("cprojectid");
        saleItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        saleItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vfree1 = rs.getString("vfree1");
        saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vbodydef1 = rs.getString("vbodydef1");
        saleItem.setVdef1(vbodydef1 == null ? null : vbodydef1.trim());

        String vbodydef2 = rs.getString("vbodydef2");
        saleItem.setVdef2(vbodydef2 == null ? null : vbodydef2.trim());

        String vbodydef3 = rs.getString("vbodydef3");
        saleItem.setVdef3(vbodydef3 == null ? null : vbodydef3.trim());

        String vbodydef4 = rs.getString("vbodydef4");
        saleItem.setVdef4(vbodydef4 == null ? null : vbodydef4.trim());

        String vbodydef5 = rs.getString("vbodydef5");
        saleItem.setVdef5(vbodydef5 == null ? null : vbodydef5.trim());

        String vbodydef6 = rs.getString("vbodydef6");
        saleItem.setVdef6(vbodydef6 == null ? null : vbodydef6.trim());

        BigDecimal ntotalplanreceiptnumber = (BigDecimal)rs.getObject("ntotalplanreceiptnumber");

        saleItem.setNtotalplanreceiptnumber(ntotalplanreceiptnumber == null ? null : new UFDouble(ntotalplanreceiptnumber));

        BigDecimal ntotalcarrynumber = (BigDecimal)rs.getObject("ntotalcarrynumber");

        saleItem.setNtotalcarrynumber(ntotalcarrynumber == null ? null : new UFDouble(ntotalcarrynumber));

        BigDecimal ntotalreturnnumber = (BigDecimal)rs.getObject("ntotalreturnnumber");

        saleItem.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(ntotalreturnnumber));

        String cconsigncorpid = rs.getString("cconsigncorpid");
        saleItem.setCconsigncorpid(cconsigncorpid == null ? null : cconsigncorpid.trim());

        String creccalbodyid = rs.getString("creccalbodyid");
        saleItem.setCreccalbodyid(creccalbodyid == null ? null : creccalbodyid.trim());

        String crecwareid = rs.getString("crecwareid");
        saleItem.setCrecwareid(crecwareid == null ? null : crecwareid.trim());

        String tconsigntime = rs.getString("tconsigntime");
        saleItem.setTconsigntime(tconsigntime == null ? null : tconsigntime.trim());

        String tdelivertime = rs.getString("tdelivertime");
        saleItem.setTdelivertime(tdelivertime == null ? null : tdelivertime.trim());

        String crecaddrnode = rs.getString("crecaddrnode");
        saleItem.setCrecaddrnode(crecaddrnode == null ? null : crecaddrnode.trim());

        String pk_corp1 = rs.getString("pk_corp1");
        saleItem.setPkarrivecorp(pk_corp1 == null ? null : pk_corp1.trim());

        String sbdeliver = rs.getString("bdeliver");
        saleHeader.setBdeliver(sbdeliver == null ? null : new UFBoolean(sbdeliver.trim()));

        String vdef11 = rs.getString("vdef11");
        saleHeader.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString("vdef12");
        saleHeader.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString("vdef13");
        saleHeader.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString("vdef14");
        saleHeader.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString("vdef15");
        saleHeader.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString("vdef16");
        saleHeader.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString("vdef17");
        saleHeader.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString("vdef18");
        saleHeader.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString("vdef19");
        saleHeader.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString("vdef20");
        saleHeader.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString("pk_defdoc1");
        saleHeader.setPk_defdoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString("pk_defdoc2");
        saleHeader.setPk_defdoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString("pk_defdoc3");
        saleHeader.setPk_defdoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString("pk_defdoc4");
        saleHeader.setPk_defdoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString("pk_defdoc5");
        saleHeader.setPk_defdoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString("pk_defdoc6");
        saleHeader.setPk_defdoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString("pk_defdoc7");
        saleHeader.setPk_defdoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString("pk_defdoc8");
        saleHeader.setPk_defdoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString("pk_defdoc9");
        saleHeader.setPk_defdoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString("pk_defdoc10");
        saleHeader.setPk_defdoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString("pk_defdoc11");
        saleHeader.setPk_defdoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString("pk_defdoc12");
        saleHeader.setPk_defdoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString("pk_defdoc13");
        saleHeader.setPk_defdoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString("pk_defdoc14");
        saleHeader.setPk_defdoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString("pk_defdoc15");
        saleHeader.setPk_defdoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString("pk_defdoc16");
        saleHeader.setPk_defdoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString("pk_defdoc17");
        saleHeader.setPk_defdoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString("pk_defdoc18");
        saleHeader.setPk_defdoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString("pk_defdoc19");
        saleHeader.setPk_defdoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString("pk_defdoc20");
        saleHeader.setPk_defdoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        String vbodydef7 = rs.getString("vbodydef7");
        saleItem.setVdef7(vbodydef7 == null ? null : vbodydef7.trim());

        String vbodydef8 = rs.getString("vbodydef8");
        saleItem.setVdef8(vbodydef8 == null ? null : vbodydef8.trim());

        String vbodydef9 = rs.getString("vbodydef9");
        saleItem.setVdef9(vbodydef9 == null ? null : vbodydef9.trim());

        String vbodydef10 = rs.getString("vbodydef10");
        saleItem.setVdef10(vbodydef10 == null ? null : vbodydef10.trim());

        String vbodydef11 = rs.getString("vbodydef11");
        saleItem.setVdef11(vbodydef11 == null ? null : vbodydef11.trim());

        String vbodydef12 = rs.getString("vbodydef12");
        saleItem.setVdef12(vbodydef12 == null ? null : vbodydef12.trim());

        String vbodydef13 = rs.getString("vbodydef13");
        saleItem.setVdef13(vbodydef13 == null ? null : vbodydef13.trim());

        String vbodydef14 = rs.getString("vbodydef14");
        saleItem.setVdef14(vbodydef14 == null ? null : vbodydef14.trim());

        String vbodydef15 = rs.getString("vbodydef15");
        saleItem.setVdef15(vbodydef15 == null ? null : vbodydef15.trim());

        String vbodydef16 = rs.getString("vbodydef16");
        saleItem.setVdef16(vbodydef16 == null ? null : vbodydef16.trim());

        String vbodydef17 = rs.getString("vbodydef17");
        saleItem.setVdef17(vbodydef17 == null ? null : vbodydef17.trim());

        String vbodydef18 = rs.getString("vbodydef18");
        saleItem.setVdef18(vbodydef18 == null ? null : vbodydef18.trim());

        String vbodydef19 = rs.getString("vbodydef19");
        saleItem.setVdef19(vbodydef19 == null ? null : vbodydef19.trim());

        String vbodydef20 = rs.getString("vbodydef20");
        saleItem.setVdef20(vbodydef20 == null ? null : vbodydef20.trim());

        String pkbodydefdoc1 = rs.getString("pkbodydefdoc1");
        saleItem.setPk_defdoc1(pkbodydefdoc1 == null ? null : pkbodydefdoc1.trim());

        String pkbodydefdoc2 = rs.getString("pkbodydefdoc2");
        saleItem.setPk_defdoc2(pkbodydefdoc2 == null ? null : pkbodydefdoc2.trim());

        String pkbodydefdoc3 = rs.getString("pkbodydefdoc3");
        saleItem.setPk_defdoc3(pkbodydefdoc3 == null ? null : pkbodydefdoc3.trim());

        String pkbodydefdoc4 = rs.getString("pkbodydefdoc4");
        saleItem.setPk_defdoc4(pkbodydefdoc4 == null ? null : pkbodydefdoc4.trim());

        String pkbodydefdoc5 = rs.getString("pkbodydefdoc5");
        saleItem.setPk_defdoc5(pkbodydefdoc5 == null ? null : pkbodydefdoc5.trim());

        String pkbodydefdoc6 = rs.getString("pkbodydefdoc6");
        saleItem.setPk_defdoc6(pkbodydefdoc6 == null ? null : pkbodydefdoc6.trim());

        String pkbodydefdoc7 = rs.getString("pkbodydefdoc7");
        saleItem.setPk_defdoc7(pkbodydefdoc7 == null ? null : pkbodydefdoc7.trim());

        String pkbodydefdoc8 = rs.getString("pkbodydefdoc8");
        saleItem.setPk_defdoc8(pkbodydefdoc8 == null ? null : pkbodydefdoc8.trim());

        String pkbodydefdoc9 = rs.getString("pkbodydefdoc9");
        saleItem.setPk_defdoc9(pkbodydefdoc9 == null ? null : pkbodydefdoc9.trim());

        String pkbodydefdoc10 = rs.getString("pkbodydefdoc10");
        saleItem.setPk_defdoc10(pkbodydefdoc10 == null ? null : pkbodydefdoc10.trim());

        String pkbodydefdoc11 = rs.getString("pkbodydefdoc11");
        saleItem.setPk_defdoc11(pkbodydefdoc11 == null ? null : pkbodydefdoc11.trim());

        String pkbodydefdoc12 = rs.getString("pkbodydefdoc12");
        saleItem.setPk_defdoc12(pkbodydefdoc12 == null ? null : pkbodydefdoc12.trim());

        String pkbodydefdoc13 = rs.getString("pkbodydefdoc13");
        saleItem.setPk_defdoc13(pkbodydefdoc13 == null ? null : pkbodydefdoc13.trim());

        String pkbodydefdoc14 = rs.getString("pkbodydefdoc14");
        saleItem.setPk_defdoc14(pkbodydefdoc14 == null ? null : pkbodydefdoc14.trim());

        String pkbodydefdoc15 = rs.getString("pkbodydefdoc15");
        saleItem.setPk_defdoc15(pkbodydefdoc15 == null ? null : pkbodydefdoc15.trim());

        String pkbodydefdoc16 = rs.getString("pkbodydefdoc16");
        saleItem.setPk_defdoc16(pkbodydefdoc16 == null ? null : pkbodydefdoc16.trim());

        String pkbodydefdoc17 = rs.getString("pkbodydefdoc17");
        saleItem.setPk_defdoc17(pkbodydefdoc17 == null ? null : pkbodydefdoc17.trim());

        String pkbodydefdoc18 = rs.getString("pkbodydefdoc18");
        saleItem.setPk_defdoc18(pkbodydefdoc18 == null ? null : pkbodydefdoc18.trim());

        String pkbodydefdoc19 = rs.getString("pkbodydefdoc19");
        saleItem.setPk_defdoc19(pkbodydefdoc19 == null ? null : pkbodydefdoc19.trim());

        String pkbodydefdoc20 = rs.getString("pkbodydefdoc20");
        saleItem.setPk_defdoc20(pkbodydefdoc20 == null ? null : pkbodydefdoc20.trim());

        String cquoteunitid = rs.getString("cquoteunitid");
        saleItem.setCquoteunitid(cquoteunitid == null ? null : cquoteunitid.trim());

        BigDecimal nquoteunitnum = (BigDecimal)rs.getObject("nquoteunitnum");
        saleItem.setNquoteunitnum(nquoteunitnum == null ? null : new UFDouble(nquoteunitnum));

        saleItem.setNqtscalefactor(SoVoTools.div(saleItem.getNnumber(), saleItem.getNquoteunitnum()));

        BigDecimal ntranslossnum = (BigDecimal)rs.getObject("ntranslossnum");
        saleItem.setNtranslossnum(ntranslossnum == null ? null : new UFDouble(ntranslossnum));

        vbody.addElement(saleItem);
      }

      if (vbody.size() > 0) {
        SaleOrderVO saleorder = new SaleOrderVO();
        saleorder.setParentVO(saleHeader);
        SaleorderBVO[] saleorderbody = new SaleorderBVO[vbody.size()];
        vbody.copyInto(saleorderbody);
        saleorder.setChildrenVO(saleorderbody);
        v.addElement(saleorder);
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
    if (v != null) {
      saleorders = new SaleOrderVO[v.size()];
      v.copyInto(saleorders);
    }
    return saleorders;
  }

  public AggregatedValueObject[] querySaleOrderData(DMDataVO[] dmVO)
    throws SQLException, Exception
  {
    SaleOrderVO[] saleorders = null;
    Vector vSaleOrder = new Vector();
    Hashtable hBodyID = new Hashtable();

    for (int i = 0; i < dmVO.length; i++) {
      if (!dmVO[i].getAttributeValue("vbilltype").equals("30"))
        continue;
      if (!hBodyID.containsKey(dmVO[i].getAttributeValue("pkbillb"))) {
        String strWhere = "csaleid = '" + dmVO[i].getAttributeValue("pkbillh") + "'";

        CircularlyAccessibleValueObject[] headVO = querySaleOrderHeadData(strWhere);

        if ((headVO != null) && (headVO.length > 0)) {
          CircularlyAccessibleValueObject[] bodyVOs = querySaleOrderBodyData(headVO[0].getPrimaryKey());

          Vector vbody = new Vector();
          for (int j = 0; j < bodyVOs.length; j++) {
            SaleorderBVO bodyVO = (SaleorderBVO)bodyVOs[j];
            if (!bodyVO.getPrimaryKey().equals(dmVO[i].getAttributeValue("pkbillb")))
              continue;
            vbody.addElement(bodyVO);
          }
          if (vbody.size() > 0) {
            SaleorderBVO[] saleorderbodys = new SaleorderBVO[vbody.size()];

            vbody.copyInto(saleorderbodys);
            SaleOrderVO saleorder = new SaleOrderVO();
            saleorder.setParentVO(headVO[0]);
            saleorder.setChildrenVO(saleorderbodys);
            vSaleOrder.add(saleorder);
          }
        }
        hBodyID.put(dmVO[i].getAttributeValue("pkbillb"), dmVO[i].getAttributeValue("pkbillb"));
      }

    }

    if (vSaleOrder.size() > 0) {
      saleorders = new SaleOrderVO[vSaleOrder.size()];
      vSaleOrder.copyInto(saleorders);
    }

    return saleorders;
  }

  public void setTotalInventoryNumber(DMDataVO[] dmVO)
    throws SQLException, BusinessException
  {
    String sql = "update so_saleexecute set ntotalinventorynumber=isnull(ntotalinventorynumber,0) + ? where csale_bid = ? and csaleid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = prepareStatement(con, sql);

      for (int i = 0; i < dmVO.length; i++) {
        if (!"30".equals(dmVO[i].getAttributeValue("vbilltype")))
          continue;
        UFDouble nnumber = new UFDouble(0.0D);

        nnumber = (UFDouble)dmVO[i].getAttributeValue("noutnum");
        if (nnumber == null) {
          nnumber = new UFDouble(0.0D);
        }

        stmt.setBigDecimal(1, nnumber.toBigDecimal());

        stmt.setString(2, dmVO[i].getAttributeValue("pkbillb").toString());

        stmt.setString(3, dmVO[i].getAttributeValue("pkbillh").toString());

        executeUpdate(stmt);
      }

      executeBatch(stmt);
    }
    finally {
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  public void setTotalPlanReceiptNumberForPush(DMDataVO[] dmVO)
    throws SQLException, BusinessException, NamingException, SystemException, RemoteException
  {
    if ((dmVO == null) || (dmVO.length <= 0))
      return;
    ArrayList alistids = new ArrayList();

    int i = 0; for (int loop = dmVO.length; i < loop; i++) {
      if (!dmVO[i].getAttributeValue("vbilltype").equals("30"))
        continue;
      Object id = dmVO[i].getAttributeValue("pkbillb");
      if ((id != null) && (!alistids.contains(id))) {
        alistids.add(id);
      }

    }

    HashMap hsbifreceiptfinish = null;
    String[] corder_bids = null;

    if (alistids.size() > 0) {
      corder_bids = (String[])(String[])alistids.toArray(new String[alistids.size()]);
      try
      {
        hsbifreceiptfinish = SOToolsDMO.getAnyValueUFBoolean("so_saleexecute", "bifreceiptfinish", "csale_bid", corder_bids, " creceipttype = '30' ");
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
        throw new BusinessException(e.getMessage());
      }

    }

    Connection con = null;
    PreparedStatement stmt = null;
    PreparedStatement stmt1 = null;
    try
    {
      String sql = "update so_saleexecute set ntotalreceiptnumber=isnull(ntotalreceiptnumber,0) + ? , bifreceiptfinish = 'Y',dlastconsigdate = ? where csale_bid = ? and csaleid = ? and creceipttype = '30' ";

      con = getConnection();
      stmt = prepareStatement(con, sql);
      UFDouble uf0 = new UFDouble(0);

      UFDouble nnumber = null;
      UFBoolean bifreceiptfinish = null;
      Object oo = null;
      UFDate curdate = ((IServiceProviderSerivce)NCLocator.getInstance().lookup(IServiceProviderSerivce.class.getName())).getServerTime() == null ? null : ((IServiceProviderSerivce)NCLocator.getInstance().lookup(IServiceProviderSerivce.class.getName())).getServerTime().getDate();
      for (int i1 = 0; i1 < dmVO.length; i1++) {
        if (!dmVO[i1].getAttributeValue("vbilltype").equals("30"))
          continue;
        nnumber = (UFDouble)dmVO[i1].getAttributeValue("ndelivernum");

        if (nnumber == null)
          nnumber = uf0;
        if (nnumber.doubleValue() > 0.0D) {
          oo = hsbifreceiptfinish.get(dmVO[i1].getAttributeValue("pkbillb"));
          bifreceiptfinish = new UFBoolean(oo == null ? "false" : oo.toString());
          if (bifreceiptfinish.booleanValue())
          {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000517"));
          }

        }

        stmt.setBigDecimal(1, nnumber.toBigDecimal());

        if (curdate == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, curdate.toString());
        }

        stmt.setString(3, dmVO[i1].getAttributeValue("pkbillb").toString());

        stmt.setString(4, dmVO[i1].getAttributeValue("pkbillh").toString());

        executeUpdate(stmt);
      }

      executeBatch(stmt);

      sql = " update so_sale set breceiptendflag = 'Y' where csaleid = ?  ";
      stmt1 = con.prepareStatement(sql);

      stmt1.setString(1, dmVO[0].getAttributeValue("pkbillh").toString());
      stmt1.executeUpdate();
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
        if (stmt1 != null)
          stmt1.close();
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

  public void setTotalPlanReceiptNumber(DMDataVO[] dmVO)
    throws BusinessException
  {
    if ((dmVO == null) || (dmVO.length <= 0)) {
      return;
    }
    boolean ispushsave = !SoVoTools.isEmptyString((String)dmVO[0].getAttributeValue("active"));
    if (ispushsave) {
      try {
        setTotalPlanReceiptNumberForPush(dmVO);
      }
      catch (Exception e) {
        SCMEnv.out(e.getMessage());
        throw new BusinessException(e.getMessage());
      }

      return;
    }

    Connection con = null;
    PreparedStatement stmt = null;
    PreparedStatement stmt1 = null;
    try {
      ArrayList alistids = new ArrayList();

      int i = 0; for (int loop = dmVO.length; i < loop; i++) {
        if (!dmVO[i].getAttributeValue("vbilltype").equals("30"))
          continue;
        Object id = dmVO[i].getAttributeValue("pkbillb");
        if ((id != null) && (!alistids.contains(id))) {
          alistids.add(id);
        }

      }

      HashMap hsbifreceiptfinish = null;
      String[] corder_bids = null;

      if (alistids.size() > 0) {
        corder_bids = (String[])(String[])alistids.toArray(new String[alistids.size()]);
        try
        {
          hsbifreceiptfinish = SOToolsDMO.getAnyValueUFBoolean("so_saleexecute", "bifreceiptfinish", "csale_bid", corder_bids, " creceipttype = '30' ");
        }
        catch (Exception e)
        {
          SCMEnv.out(e.getMessage());
          throw new BusinessException(e.getMessage());
        }
      }

      String SO23 = null;
      String SO24 = null;
      String pk_corp = (String)dmVO[0].getAttributeValue("pksalecorp");

      SysInitDMO initReferDMO = new SysInitDMO();

      SO23 = initReferDMO.getParaString(pk_corp, "SO23");

      SO24 = initReferDMO.getParaString(pk_corp, "SO24");
      if ((SO23 == null) || (SO23.trim().length() <= 0)) {
        SO23 = "N";
      }

      UFDouble nPercent = SO24 == null ? new UFDouble("0") : new UFDouble(SO24);

      isSaleOverOut(SO23, nPercent, dmVO);

      String sql = "update so_saleexecute set ntotalreceiptnumber=isnull(ntotalreceiptnumber,0) + ?  where csale_bid = ? and csaleid = ? and creceipttype = '30' ";

      con = getConnection();
      stmt = prepareStatement(con, sql);
      UFDouble uf0 = new UFDouble(0);
      UFBoolean bifreceiptfinish = null;
      Object oo = null;

      UFDouble nnumber = null;
      for (int i1 = 0; i1 < dmVO.length; i1++) {
        if (!dmVO[i1].getAttributeValue("vbilltype").equals("30")) {
          continue;
        }
        nnumber = (UFDouble)dmVO[i1].getAttributeValue("ndelivernum");

        if (nnumber == null)
          nnumber = uf0;
        if (nnumber.doubleValue() > 0.0D) {
          oo = hsbifreceiptfinish.get(dmVO[i1].getAttributeValue("pkbillb"));
          bifreceiptfinish = new UFBoolean(oo == null ? "false" : oo.toString());
          if (bifreceiptfinish.booleanValue())
          {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000517"));
          }

        }

        stmt.setBigDecimal(1, nnumber.toBigDecimal());

        stmt.setString(2, dmVO[i1].getAttributeValue("pkbillb").toString());

        stmt.setString(3, dmVO[i1].getAttributeValue("pkbillh").toString());

        executeUpdate(stmt);
      }

      executeBatch(stmt);

      autoSetReceiptFinish(dmVO);
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
    finally
    {
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  public void setTotalReceiptNumber(DMDataVO[] dmVO)
    throws BusinessException
  {
    String sql = "update so_saleexecute set ntotalcarrynumber=isnull(ntotalcarrynumber,0) + ? where csale_bid = ? and csaleid = ? and creceipttype = '30' ";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = prepareStatement(con, sql);

      for (int i = 0; i < dmVO.length; i++) {
        if (!"30".equals(dmVO[i].getAttributeValue("vbilltype")))
          continue;
        UFDouble nnumber = new UFDouble(0.0D);

        nnumber = (UFDouble)dmVO[i].getAttributeValue("ndelivernum");

        if (nnumber == null) {
          nnumber = new UFDouble(0.0D);
        }

        stmt.setBigDecimal(1, nnumber.toBigDecimal());

        stmt.setString(2, dmVO[i].getAttributeValue("pkbillb").toString());

        stmt.setString(3, dmVO[i].getAttributeValue("pkbillh").toString());

        executeUpdate(stmt);
      }

      executeBatch(stmt);
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }
    finally
    {
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  public void setTotalSignNumber(DMDataVO[] dmVO)
    throws BusinessException
  {
    String sql = "update so_saleexecute set ntotalsignnumber=isnull(ntotalsignnumber,0) + ?,ntaltransretnum=isnull(ntaltransretnum,0) + ? where csale_bid = ? and csaleid = ? and creceipttype = '30' ";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = prepareStatement(con, sql);

      for (int i = 0; i < dmVO.length; i++) {
        if (!"30".equals(dmVO[i].getAttributeValue("vbilltype")))
          continue;
        UFDouble nnumber = new UFDouble(0.0D);

        nnumber = (UFDouble)dmVO[i].getAttributeValue("nsignnum");
        if (nnumber == null) {
          nnumber = new UFDouble(0.0D);
        }

        UFDouble nbacknum = new UFDouble(0.0D);

        nbacknum = (UFDouble)dmVO[i].getAttributeValue("nbacknum");
        if (nbacknum == null) {
          nbacknum = new UFDouble(0.0D);
        }

        stmt.setBigDecimal(1, nnumber.toBigDecimal());

        stmt.setBigDecimal(2, nbacknum.toBigDecimal());

        stmt.setString(3, dmVO[i].getAttributeValue("pkbillb").toString());

        stmt.setString(4, dmVO[i].getAttributeValue("pkbillh").toString());

        executeUpdate(stmt);
      }

      executeBatch(stmt);
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

  public void setTranslossNum(DMDataVO[] dmVO)
    throws BusinessException
  {
  }
}