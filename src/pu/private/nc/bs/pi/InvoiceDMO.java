package nc.bs.pi;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.arap.outer.ArapPubUnVerifyInterface;
import nc.bs.arap.outer.ArapPubVerifyInterface;
import nc.bs.bd.b21.CurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.po.close.PoCloseImpl;
import nc.bs.ps.vmi.VMIDMO;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.pf.IBackCheckState;
import nc.bs.pub.pf.ICheckState;
import nc.bs.pub.pf.IQueryData;
import nc.bs.scm.pub.TempTableDMO;
import nc.itf.pi.IInvoiceD;
import nc.itf.uap.busibean.ISysInitQry;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.vo.arap.accountpub.DjclDapItemVO;
import nc.vo.arap.accountpub.DjclDapVO;
import nc.vo.arap.verify.DJCLBVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pi.RelatedTableVO;
import nc.vo.ps.estimate.GeneralBb3VO;
import nc.vo.ps.settle.IAdjuestVO;
import nc.vo.pu.exception.RwtPiToScException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sc.order.OrderHeaderVO;
import nc.vo.sc.order.OrderVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.puic.ParaPoToIcLendRewriteVO;

public class InvoiceDMO extends DataManageObject
  implements IQueryData, ICheckState, ArapPubVerifyInterface, ArapPubUnVerifyInterface, IBackCheckState, IInvoiceD
{
  public InvoiceDMO()
    throws NamingException, SystemException
  {
  }

  public InvoiceDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public void backGoing(String billId, String approveId, String approveDate, String backNote)
    throws Exception
  {
  }

  public void backNoState(String billId, String approveId, String approveDate, String backNote)
    throws Exception
  {
    String sql = 
      "update po_invoice set ibillstatus = 0, cauditpsn = null, dauditdate = null where cinvoiceid = ? and dr = 0";
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
      } catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
  }

  public InvoiceVO findByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findByPrimaryKey", new Object[] { key });

    InvoiceVO vo = new InvoiceVO();

    InvoiceHeaderVO header = findHeaderByPrimaryKey(key);
    InvoiceItemVO[] items = (InvoiceItemVO[])null;
    if (header != null) {
      items = findItemsForHeader(header.getPrimaryKey());
    }

    vo.setParentVO(header);
    vo.setChildrenVO(items);

    afterCallMethod("nc.bs.pi.InvoiceDMO", "findByPrimaryKey", new Object[] { key });

    return vo;
  }

  public InvoiceHeaderVO findHeaderByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findHeaderByPrimaryKey", new Object[] { key });

    StringBuffer sbufHeadSQL = new StringBuffer("SELECT distinct ");
    String[] saHField = InvoiceHeaderVO.getDbFields();
    int iHLen = saHField.length;
    for (int i = 0; i < iHLen; i++) {
      sbufHeadSQL.append("po_invoice.");
      sbufHeadSQL.append(saHField[i]);
      sbufHeadSQL.append(",");
    }
    sbufHeadSQL.append("po_invoice_b.nexchangeotobrate,");
    sbufHeadSQL.append("po_invoice_b.nexchangeotoarate,");

    sbufHeadSQL.append("po_invoice_b.ccurrencytypeid ");
    sbufHeadSQL.append("from po_invoice,po_invoice_b where po_invoice.cinvoiceid = po_invoice_b.cinvoiceid and po_invoice.cinvoiceid = ?");

    InvoiceHeaderVO headVO = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbufHeadSQL.toString());
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        headVO = new InvoiceHeaderVO();

        Object ob = rs.getObject(1);
        headVO.setPrimaryKey(ob.toString());

        for (int i = 1; i < saHField.length; i++) {
          ob = rs.getObject(i + 1);
          if ((ob == null) || (ob.toString().trim().equals("")))
            headVO.setAttributeValue(saHField[i], null);
          else if (ob.getClass().equals(String.class))
            headVO.setAttributeValue(saHField[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            headVO.setAttributeValue(saHField[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            if (new UFDouble(ob.toString()).doubleValue() == 0.0D)
              headVO.setAttributeValue(saHField[i], null);
            else {
              headVO.setAttributeValue(saHField[i], new UFDouble(ob.toString()));
            }
          }

        }

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject(saHField.length + 1);
        if (nexchangeotobrate != null)
          headVO.setNexchangeotobrate(new UFDouble(nexchangeotobrate));
        else {
          headVO.setNexchangeotobrate(null);
        }

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject(saHField.length + 2);
        if (nexchangeotoarate != null)
          headVO.setNexchangeotoarate(new UFDouble(nexchangeotoarate));
        else {
          headVO.setNexchangeotoarate(null);
        }

        String ccurrencytypeid = rs.getObject(saHField.length + 3).toString();
        headVO.setCcurrencytypeid(ccurrencytypeid);
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findHeaderByPrimaryKey", new Object[] { key });

    return headVO;
  }

  public InvoiceItemVO findItemByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findItemByPrimaryKey", new Object[] { key });

    String sql = "select cinvoiceid, pk_corp, cusedeptid, corder_bid, corderid, csourcebilltype, csourcebillid, csourcebillrowid, cupsourcebilltype, cupsourcebillid, cupsourcebillrowid, cmangid, cbaseid, ninvoicenum, naccumsettnum, idiscounttaxtype, ntaxrate, ccurrencytypeid, noriginalcurprice, noriginaltaxmny, noriginalcurmny, noriginalsummny, noriginalpaymentmny, nexchangeotobrate, nmoney, ntaxmny, nsummny, npaymentmny, naccumsettmny, nexchangeotoarate, nassistcurmny, nassisttaxmny, nassistsummny, nassistpaymny, nassistsettmny, cprojectid, cprojectphaseid, vmemo, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6,";
    sql = sql + "vdef7, vdef8, vdef9, vdef10, vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,pk_defdoc5,pk_defdoc6,pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11,pk_defdoc12,pk_defdoc13,pk_defdoc14,pk_defdoc15,pk_defdoc16,pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20, dr, ts, crowno, nreasonwastenum, pk_upsrccorp,b_cjje1,b_cjje2,b_cjje3 from po_invoice_b where cinvoice_bid = ?";

    InvoiceItemVO invoiceItem = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        invoiceItem = new InvoiceItemVO(key);

        String cinvoiceid = rs.getString(1);
        invoiceItem.setCinvoiceid(cinvoiceid == null ? null : cinvoiceid.trim());

        String pk_corp = rs.getString(2);
        invoiceItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String cusedeptid = rs.getString(3);
        invoiceItem.setCusedeptid(cusedeptid == null ? null : cusedeptid.trim());

        String corder_bid = rs.getString(4);
        invoiceItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String corderid = rs.getString(5);
        invoiceItem.setCorderid(corderid == null ? null : corderid.trim());

        String csourcebilltype = rs.getString(6);
        invoiceItem.setCsourcebilltype(csourcebilltype == null ? null : csourcebilltype.trim());

        String csourcebillid = rs.getString(7);
        invoiceItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillrowid = rs.getString(8);
        invoiceItem.setCsourcebillrowid(csourcebillrowid == null ? null : csourcebillrowid.trim());

        String cupsourcebilltype = rs.getString(9);
        invoiceItem.setCupsourcebilltype(cupsourcebilltype == null ? null : cupsourcebilltype.trim());

        String cupsourcebillid = rs.getString(10);
        invoiceItem.setCupsourcebillid(cupsourcebillid == null ? null : cupsourcebillid.trim());

        String cupsourcebillrowid = rs.getString(11);
        invoiceItem.setCupsourcebillrowid(cupsourcebillrowid == null ? null : cupsourcebillrowid.trim());

        String cmangid = rs.getString(12);
        invoiceItem.setCmangid(cmangid == null ? null : cmangid.trim());

        String cbaseid = rs.getString(13);
        invoiceItem.setCbaseid(cbaseid == null ? null : cbaseid.trim());

        BigDecimal ninvoicenum = rs.getBigDecimal(14);
        invoiceItem.setNinvoicenum(ninvoicenum == null ? null : new UFDouble(ninvoicenum));

        BigDecimal naccumsettnum = rs.getBigDecimal(15);
        invoiceItem.setNaccumsettnum(naccumsettnum == null ? null : new UFDouble(naccumsettnum));

        Integer idiscounttaxtype = (Integer)rs.getObject(16);
        invoiceItem.setIdiscounttaxtype(idiscounttaxtype == null ? null : idiscounttaxtype);

        BigDecimal ntaxrate = rs.getBigDecimal(17);
        invoiceItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        String ccurrencytypeid = rs.getString(18);
        invoiceItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalcurprice = rs.getBigDecimal(19);
        invoiceItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));

        BigDecimal noriginaltaxmny = rs.getBigDecimal(20);
        invoiceItem.setNoriginaltaxmny(noriginaltaxmny == null ? null : new UFDouble(noriginaltaxmny));

        BigDecimal noriginalcurmny = rs.getBigDecimal(21);
        invoiceItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalsummny = rs.getBigDecimal(22);
        invoiceItem.setNoriginalsummny(noriginalsummny == null ? null : new UFDouble(noriginalsummny));

        BigDecimal noriginalpaymentmny = rs.getBigDecimal(23);
        invoiceItem.setNoriginalpaymentmny(noriginalpaymentmny == null ? null : new UFDouble(noriginalpaymentmny));

        BigDecimal nexchangeotobrate = rs.getBigDecimal(24);
        invoiceItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nmoney = rs.getBigDecimal(25);
        invoiceItem.setNmoney(nmoney == null ? null : new UFDouble(nmoney));

        BigDecimal ntaxmny = rs.getBigDecimal(26);
        invoiceItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nsummny = rs.getBigDecimal(27);
        invoiceItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal npaymentmny = rs.getBigDecimal(28);
        invoiceItem.setNpaymentmny(npaymentmny == null ? null : new UFDouble(npaymentmny));

        BigDecimal naccumsettmny = rs.getBigDecimal(29);
        invoiceItem.setNaccumsettmny(naccumsettmny == null ? null : new UFDouble(naccumsettmny));

        BigDecimal nexchangeotoarate = rs.getBigDecimal(30);
        invoiceItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal nassistcurmny = rs.getBigDecimal(31);
        invoiceItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassisttaxmny = rs.getBigDecimal(32);
        invoiceItem.setNassisttaxmny(nassisttaxmny == null ? null : new UFDouble(nassisttaxmny));

        BigDecimal nassistsummny = rs.getBigDecimal(33);
        invoiceItem.setNassistsummny(nassistsummny == null ? null : new UFDouble(nassistsummny));

        BigDecimal nassistpaymny = rs.getBigDecimal(34);
        invoiceItem.setNassistpaymny(nassistpaymny == null ? null : new UFDouble(nassistpaymny));

        BigDecimal nassistsettmny = rs.getBigDecimal(35);
        invoiceItem.setNassistsettmny(nassistsettmny == null ? null : new UFDouble(nassistsettmny));

        String cprojectid = rs.getString(36);
        invoiceItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString(37);
        invoiceItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vmemo = rs.getString(38);
        invoiceItem.setVmemo(vmemo == null ? null : vmemo.trim());

        String vfree1 = rs.getString(39);
        invoiceItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(40);
        invoiceItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(41);
        invoiceItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(42);
        invoiceItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(43);
        invoiceItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString(44);
        invoiceItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(45);
        invoiceItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(46);
        invoiceItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(47);
        invoiceItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(48);
        invoiceItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(49);
        invoiceItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(50);
        invoiceItem.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(51);
        invoiceItem.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(52);
        invoiceItem.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(53);
        invoiceItem.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString(54);
        invoiceItem.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString(55);
        invoiceItem.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString(56);
        invoiceItem.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString(57);
        invoiceItem.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString(58);
        invoiceItem.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString(59);
        invoiceItem.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString(60);
        invoiceItem.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString(61);
        invoiceItem.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString(62);
        invoiceItem.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString(63);
        invoiceItem.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString(64);
        invoiceItem.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(65);
        invoiceItem.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(66);
        invoiceItem.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(67);
        invoiceItem.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(68);
        invoiceItem.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(69);
        invoiceItem.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(70);
        invoiceItem.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(71);
        invoiceItem.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(72);
        invoiceItem.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(73);
        invoiceItem.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(74);
        invoiceItem.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(75);
        invoiceItem.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(76);
        invoiceItem.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(77);
        invoiceItem.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(78);
        invoiceItem.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(79);
        invoiceItem.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(80);
        invoiceItem.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(81);
        invoiceItem.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(82);
        invoiceItem.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(83);
        invoiceItem.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        Integer dr = (Integer)rs.getObject(84);
        invoiceItem.setDr(dr == null ? null : dr);

        String ts = (String)rs.getObject(85);
        invoiceItem.setTs(ts == null ? null : ts.trim());

        String crowno = (String)rs.getObject(86);
        invoiceItem.setCrowno(crowno == null ? null : crowno.trim());

        BigDecimal nreasonwastenum = rs.getBigDecimal(87);
        invoiceItem.setNreasonwastenum(nreasonwastenum == null ? null : new UFDouble(nreasonwastenum));

        String pk_upsrccorp = (String)rs.getObject(88);
        invoiceItem.setPk_upsrccorp(pk_upsrccorp == null ? null : pk_upsrccorp.trim());
        
        String b_cjje1 = rs.getString(89);
        invoiceItem.setB_cjje1(b_cjje1 == null ? null : b_cjje1.trim());
        
        String b_cjje2 = rs.getString(90);
        invoiceItem.setB_cjje2(b_cjje2 == null ? null : b_cjje2.trim());
        
        String b_cjje3 = rs.getString(91);
        invoiceItem.setB_cjje3(b_cjje3 == null ? null : b_cjje3.trim());
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findItemByPrimaryKey", new Object[] { key });

    return invoiceItem;
  }

  public InvoiceItemVO[] findItemsForHeader(String key)
    throws SQLException
  {
    return findItemsForHeaders(new String[] { key });
  }

  public String insert(InvoiceVO vo)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "insert", new Object[] { vo });

    String key = insertHeader((InvoiceHeaderVO)vo.getParentVO());

    InvoiceItemVO[] items = (InvoiceItemVO[])vo.getChildrenVO();

    insertItems(items, key);

    afterCallMethod("nc.bs.pi.InvoiceDMO", "insert", new Object[] { vo });

    return key;
  }

  public String insertHeader(InvoiceHeaderVO invoiceHeader)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "insertHeader", new Object[] { invoiceHeader });

    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO po_invoice(cinvoiceid,pk_corp,vinvoicecode,iinvoicetype,cdeptid,cfreecustid,cvendormangid,cvendorbaseid,cemployeeid,dinvoicedate,");
    sql.append("darrivedate,cbiztype,caccountbankid,cpayunit,finitflag,cvoucherid,ctermprotocolid,coperator,caccountyear,cbilltype,");
    sql.append("ibillstatus,dauditdate,cauditpsn,vmemo,vdef1,vdef2,vdef3,vdef4,vdef5,vdef6,vdef7,vdef8,vdef9,vdef10,vdef11, ");
    sql.append("vdef12,vdef13,vdef14,vdef15,vdef16,vdef17,vdef18,vdef19,vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,pk_defdoc5,pk_defdoc6,");
    sql.append("pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11,pk_defdoc12,pk_defdoc13,pk_defdoc14,pk_defdoc15,pk_defdoc16,pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20,dr,cstoreorganization,pk_purcorp,tmaketime,taudittime,tlastmaketime)");
    sql.append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ? , ?, ?, ?, ?,");
    sql.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?,?,?,?,?)");

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      key = getOID(invoiceHeader.getPk_corp());
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      stmt.setString(1, key);

      if (invoiceHeader.getPk_corp() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, invoiceHeader.getPk_corp());
      }
      if (invoiceHeader.getVinvoicecode() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, invoiceHeader.getVinvoicecode());
      }
      if (invoiceHeader.getIinvoicetype() == null)
        stmt.setNull(4, 4);
      else {
        stmt.setInt(4, invoiceHeader.getIinvoicetype().intValue());
      }
      if (invoiceHeader.getCdeptid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, invoiceHeader.getCdeptid());
      }
      if (invoiceHeader.getCfreecustid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, invoiceHeader.getCfreecustid());
      }
      if (invoiceHeader.getCvendormangid() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, invoiceHeader.getCvendormangid());
      }
      if (invoiceHeader.getCvendorbaseid() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, invoiceHeader.getCvendorbaseid());
      }
      if (invoiceHeader.getCemployeeid() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, invoiceHeader.getCemployeeid());
      }
      if (invoiceHeader.getDinvoicedate() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, invoiceHeader.getDinvoicedate().toString());
      }
      if (invoiceHeader.getDarrivedate() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, invoiceHeader.getDarrivedate().toString());
      }
      if (invoiceHeader.getCbiztype() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, invoiceHeader.getCbiztype());
      }
      if (invoiceHeader.getCaccountbankid() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, invoiceHeader.getCaccountbankid());
      }
      if (invoiceHeader.getCpayunit() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, invoiceHeader.getCpayunit());
      }
      if (invoiceHeader.getFinitflag() == null)
        stmt.setNull(15, 4);
      else {
        stmt.setInt(15, invoiceHeader.getFinitflag().intValue());
      }
      if (invoiceHeader.getCvoucherid() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, invoiceHeader.getCvoucherid());
      }
      if (invoiceHeader.getCtermprotocolid() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, invoiceHeader.getCtermprotocolid());
      }
      if (invoiceHeader.getCoperator() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, invoiceHeader.getCoperator());
      }
      if (invoiceHeader.getCaccountyear() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, invoiceHeader.getCaccountyear());
      }
      if (invoiceHeader.getCbilltype() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, invoiceHeader.getCbilltype());
      }
      if (invoiceHeader.getIbillstatus() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setInt(21, invoiceHeader.getIbillstatus().intValue());
      }
      if (invoiceHeader.getDauditdate() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, invoiceHeader.getDauditdate().toString());
      }
      if (invoiceHeader.getCauditpsn() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, invoiceHeader.getCauditpsn());
      }
      if (invoiceHeader.getVmemo() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, invoiceHeader.getVmemo());
      }
      if (invoiceHeader.getVdef1() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, invoiceHeader.getVdef1());
      }
      if (invoiceHeader.getVdef2() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, invoiceHeader.getVdef2());
      }
      if (invoiceHeader.getVdef3() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, invoiceHeader.getVdef3());
      }
      if (invoiceHeader.getVdef4() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, invoiceHeader.getVdef4());
      }
      if (invoiceHeader.getVdef5() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, invoiceHeader.getVdef5());
      }
      if (invoiceHeader.getVdef6() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, invoiceHeader.getVdef6());
      }
      if (invoiceHeader.getVdef7() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, invoiceHeader.getVdef7());
      }
      if (invoiceHeader.getVdef8() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, invoiceHeader.getVdef8());
      }
      if (invoiceHeader.getVdef9() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, invoiceHeader.getVdef9());
      }
      if (invoiceHeader.getVdef10() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, invoiceHeader.getVdef10());
      }
      if (invoiceHeader.getVdef11() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, invoiceHeader.getVdef11());
      }
      if (invoiceHeader.getVdef12() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, invoiceHeader.getVdef12());
      }
      if (invoiceHeader.getVdef13() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, invoiceHeader.getVdef13());
      }
      if (invoiceHeader.getVdef14() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, invoiceHeader.getVdef14());
      }
      if (invoiceHeader.getVdef15() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, invoiceHeader.getVdef15());
      }
      if (invoiceHeader.getVdef16() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, invoiceHeader.getVdef16());
      }
      if (invoiceHeader.getVdef17() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, invoiceHeader.getVdef17());
      }
      if (invoiceHeader.getVdef18() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, invoiceHeader.getVdef18());
      }
      if (invoiceHeader.getVdef19() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, invoiceHeader.getVdef19());
      }
      if (invoiceHeader.getVdef20() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, invoiceHeader.getVdef20());
      }
      if (invoiceHeader.getPKDefDoc1() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, invoiceHeader.getPKDefDoc1());
      }
      if (invoiceHeader.getPKDefDoc2() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, invoiceHeader.getPKDefDoc2());
      }
      if (invoiceHeader.getPKDefDoc3() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, invoiceHeader.getPKDefDoc3());
      }
      if (invoiceHeader.getPKDefDoc4() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, invoiceHeader.getPKDefDoc4());
      }
      if (invoiceHeader.getPKDefDoc5() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, invoiceHeader.getPKDefDoc5());
      }
      if (invoiceHeader.getPKDefDoc6() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, invoiceHeader.getPKDefDoc6());
      }
      if (invoiceHeader.getPKDefDoc7() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, invoiceHeader.getPKDefDoc7());
      }
      if (invoiceHeader.getPKDefDoc8() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, invoiceHeader.getPKDefDoc8());
      }
      if (invoiceHeader.getPKDefDoc9() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, invoiceHeader.getPKDefDoc9());
      }
      if (invoiceHeader.getPKDefDoc10() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, invoiceHeader.getPKDefDoc10());
      }
      if (invoiceHeader.getPKDefDoc11() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, invoiceHeader.getPKDefDoc11());
      }
      if (invoiceHeader.getPKDefDoc12() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, invoiceHeader.getPKDefDoc12());
      }
      if (invoiceHeader.getPKDefDoc13() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, invoiceHeader.getPKDefDoc13());
      }
      if (invoiceHeader.getPKDefDoc14() == null)
        stmt.setNull(58, 1);
      else {
        stmt.setString(58, invoiceHeader.getPKDefDoc14());
      }
      if (invoiceHeader.getPKDefDoc15() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, invoiceHeader.getPKDefDoc15());
      }
      if (invoiceHeader.getPKDefDoc16() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, invoiceHeader.getPKDefDoc16());
      }
      if (invoiceHeader.getPKDefDoc17() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, invoiceHeader.getPKDefDoc17());
      }
      if (invoiceHeader.getPKDefDoc18() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, invoiceHeader.getPKDefDoc18());
      }
      if (invoiceHeader.getPKDefDoc19() == null)
        stmt.setNull(63, 1);
      else {
        stmt.setString(63, invoiceHeader.getPKDefDoc19());
      }
      if (invoiceHeader.getPKDefDoc20() == null)
        stmt.setNull(64, 1);
      else {
        stmt.setString(64, invoiceHeader.getPKDefDoc20());
      }
      if (invoiceHeader.getDr() == null)
        stmt.setNull(65, 4);
      else {
        stmt.setInt(65, invoiceHeader.getDr().intValue());
      }
      if (invoiceHeader.getCstoreorganization() == null)
        stmt.setNull(66, 4);
      else {
        stmt.setString(66, invoiceHeader.getCstoreorganization());
      }

      if (invoiceHeader.getPk_purcorp() == null)
        stmt.setNull(67, 1);
      else {
        stmt.setString(67, invoiceHeader.getPk_purcorp());
      }

      if (invoiceHeader.getTmaketime() == null)
        stmt.setNull(68, 1);
      else {
        stmt.setString(68, invoiceHeader.getTmaketime().toString());
      }

      if (invoiceHeader.getTaudittime() == null)
        stmt.setNull(69, 1);
      else {
        stmt.setString(69, invoiceHeader.getTaudittime().toString());
      }

      if (invoiceHeader.getTlastmaketime() == null)
        stmt.setNull(70, 1);
      else {
        stmt.setString(70, invoiceHeader.getTlastmaketime().toString());
      }

      stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "insertHeader", new Object[] { invoiceHeader });

    return key;
  }

  public String insertItem(InvoiceItemVO invoiceItem)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "insertItem", new Object[] { invoiceItem });

    String key = null;

    if (invoiceItem != null) {
      InvoiceItemVO[] vos = { invoiceItem };
      String[] keys = insertItems(vos);

      if ((keys != null) && (keys.length > 0)) {
        key = keys[0];
      }

    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "insertItem", new Object[] { invoiceItem });

    return key;
  }

  public String insertItem(InvoiceItemVO invoiceItem, String foreignKey)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "insertItem", new Object[] { invoiceItem, foreignKey });

    invoiceItem.setCinvoiceid(foreignKey);
    String key = insertItem(invoiceItem);

    afterCallMethod("nc.bs.pi.InvoiceDMO", "insertItem", new Object[] { invoiceItem, foreignKey });

    return key;
  }

  public void delete(InvoiceVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "delete", new Object[] { vo });

    deleteItemsForHeader(((InvoiceHeaderVO)vo.getParentVO()).getPrimaryKey());
    deleteHeader((InvoiceHeaderVO)vo.getParentVO());

    afterCallMethod("nc.bs.pi.InvoiceDMO", "delete", new Object[] { vo });
  }

  public void deleteHeader(InvoiceHeaderVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "deleteHeader", new Object[] { vo });

    String sql = "delete from po_invoice where cinvoiceid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, vo.getPrimaryKey());
      stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "deleteHeader", new Object[] { vo });
  }

  public void deleteItem(InvoiceItemVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "deleteItem", new Object[] { vo });

    String sql = "delete from po_invoice_b where cinvoice_bid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, vo.getPrimaryKey());
      stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "deleteItem", new Object[] { vo });
  }

  public void deleteItemsForHeader(String headerKey)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "deleteItemsForHeader", new Object[] { headerKey });

    String sql = "delete from po_invoice_b where cinvoiceid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, headerKey);
      stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "deleteItemsForHeader", new Object[] { headerKey });
  }

  public void update(InvoiceVO vo)
    throws SQLException, BusinessException, SystemException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "update", new Object[] { vo });

    InvoiceItemVO[] items = (InvoiceItemVO[])vo.getChildrenVO();
    for (int i = 0; i < items.length; i++) {
      switch (items[i].getStatus()) {
      case 2:
        insertItem(items[i]);
        break;
      case 1:
        updateItem(items[i]);
        break;
      case 3:
        deleteItem(items[i]);
      }
    }
    updateHeader((InvoiceHeaderVO)vo.getParentVO());

    afterCallMethod("nc.bs.pi.InvoiceDMO", "update", new Object[] { vo });
  }

  public void updateHeader(InvoiceHeaderVO invoiceHeader)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateHeader", new Object[] { invoiceHeader });

    StringBuffer sql = new StringBuffer("UPDATE po_invoice SET ");
    sql.append("pk_corp = ?,vinvoicecode = ?,iinvoicetype = ?,cdeptid = ?,");
    sql.append("cfreecustid = ?,cvendormangid = ?,cvendorbaseid = ?,cemployeeid = ?,dinvoicedate = ?,");
    sql.append("darrivedate = ?,cbiztype = ?,caccountbankid = ?,cpayunit = ?,finitflag = ?,");
    sql.append("cvoucherid = ?,ctermprotocolid = ?,coperator = ?,caccountyear = ?,cbilltype = ?,");
    sql.append("ibillstatus = ?,dauditdate = ?,cauditpsn = ?,vmemo = ?, vdef1 = ?,");
    sql.append("vdef2 = ?,vdef3 = ?,vdef4 = ?,vdef5 = ?,vdef6 = ?,");
    sql.append("vdef7 = ?,vdef8 = ?,vdef9 = ?,vdef10 = ?,");
    sql.append("vdef11 = ?,vdef12 = ?,vdef13 = ?,vdef14 = ?,vdef15 = ?,");
    sql.append("vdef16 = ?,vdef17 = ?,vdef18 = ?,vdef19 = ?,vdef20 = ?,");
    sql.append("pk_defdoc1 = ?,pk_defdoc2 = ?,pk_defdoc3 = ?,pk_defdoc4 = ?,pk_defdoc5 = ?,");
    sql.append("pk_defdoc6 = ?,pk_defdoc7 = ?,pk_defdoc8 = ?,pk_defdoc9 = ?,pk_defdoc10 = ?,");
    sql.append("pk_defdoc11 = ?,pk_defdoc12 = ?,pk_defdoc13 = ?,pk_defdoc14 = ?,pk_defdoc15 = ?,");
    sql.append("pk_defdoc16 = ?,pk_defdoc17 = ?,pk_defdoc18 = ?,pk_defdoc19 = ?,pk_defdoc20 = ?,");
    sql.append("dr = ?,cstoreorganization = ?,pk_purcorp = ?,tmaketime = ?,taudittime = ?,tlastmaketime = ?");
    sql.append(" WHERE cinvoiceid = ?");

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      if (invoiceHeader.getPk_corp() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, invoiceHeader.getPk_corp());
      }
      if (invoiceHeader.getVinvoicecode() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, invoiceHeader.getVinvoicecode());
      }
      if (invoiceHeader.getIinvoicetype() == null)
        stmt.setNull(3, 4);
      else {
        stmt.setInt(3, invoiceHeader.getIinvoicetype().intValue());
      }
      if (invoiceHeader.getCdeptid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, invoiceHeader.getCdeptid());
      }
      if (invoiceHeader.getCfreecustid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, invoiceHeader.getCfreecustid());
      }
      if (invoiceHeader.getCvendormangid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, invoiceHeader.getCvendormangid());
      }
      if (invoiceHeader.getCvendorbaseid() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, invoiceHeader.getCvendorbaseid());
      }
      if (invoiceHeader.getCemployeeid() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, invoiceHeader.getCemployeeid());
      }
      if (invoiceHeader.getDinvoicedate() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, invoiceHeader.getDinvoicedate().toString());
      }
      if (invoiceHeader.getDarrivedate() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, invoiceHeader.getDarrivedate().toString());
      }
      if (invoiceHeader.getCbiztype() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, invoiceHeader.getCbiztype());
      }
      if (invoiceHeader.getCaccountbankid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, invoiceHeader.getCaccountbankid());
      }
      if (invoiceHeader.getCpayunit() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, invoiceHeader.getCpayunit());
      }
      if (invoiceHeader.getFinitflag() == null)
        stmt.setNull(14, 4);
      else {
        stmt.setInt(14, invoiceHeader.getFinitflag().intValue());
      }
      if (invoiceHeader.getCvoucherid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, invoiceHeader.getCvoucherid());
      }
      if (invoiceHeader.getCtermprotocolid() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, invoiceHeader.getCtermprotocolid());
      }
      if (invoiceHeader.getCoperator() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, invoiceHeader.getCoperator());
      }
      if (invoiceHeader.getCaccountyear() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, invoiceHeader.getCaccountyear());
      }
      if (invoiceHeader.getCbilltype() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, invoiceHeader.getCbilltype());
      }
      if (invoiceHeader.getIbillstatus() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setInt(20, invoiceHeader.getIbillstatus().intValue());
      }
      if (invoiceHeader.getDauditdate() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, invoiceHeader.getDauditdate().toString());
      }
      if (invoiceHeader.getCauditpsn() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, invoiceHeader.getCauditpsn());
      }
      if (invoiceHeader.getVmemo() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, invoiceHeader.getVmemo());
      }
      if (invoiceHeader.getVdef1() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, invoiceHeader.getVdef1());
      }
      if (invoiceHeader.getVdef2() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, invoiceHeader.getVdef2());
      }
      if (invoiceHeader.getVdef3() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, invoiceHeader.getVdef3());
      }
      if (invoiceHeader.getVdef4() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, invoiceHeader.getVdef4());
      }
      if (invoiceHeader.getVdef5() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, invoiceHeader.getVdef5());
      }
      if (invoiceHeader.getVdef6() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, invoiceHeader.getVdef6());
      }
      if (invoiceHeader.getVdef7() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, invoiceHeader.getVdef7());
      }
      if (invoiceHeader.getVdef8() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, invoiceHeader.getVdef8());
      }
      if (invoiceHeader.getVdef9() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, invoiceHeader.getVdef9());
      }
      if (invoiceHeader.getVdef10() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, invoiceHeader.getVdef10());
      }
      if (invoiceHeader.getVdef11() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, invoiceHeader.getVdef11());
      }
      if (invoiceHeader.getVdef12() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, invoiceHeader.getVdef12());
      }
      if (invoiceHeader.getVdef13() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, invoiceHeader.getVdef13());
      }
      if (invoiceHeader.getVdef14() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, invoiceHeader.getVdef14());
      }
      if (invoiceHeader.getVdef15() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, invoiceHeader.getVdef15());
      }
      if (invoiceHeader.getVdef16() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, invoiceHeader.getVdef16());
      }
      if (invoiceHeader.getVdef17() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, invoiceHeader.getVdef17());
      }
      if (invoiceHeader.getVdef18() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, invoiceHeader.getVdef18());
      }
      if (invoiceHeader.getVdef19() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, invoiceHeader.getVdef19());
      }
      if (invoiceHeader.getVdef20() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, invoiceHeader.getVdef20());
      }
      if (invoiceHeader.getPKDefDoc1() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, invoiceHeader.getPKDefDoc1());
      }
      if (invoiceHeader.getPKDefDoc2() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, invoiceHeader.getPKDefDoc2());
      }
      if (invoiceHeader.getPKDefDoc3() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, invoiceHeader.getPKDefDoc3());
      }
      if (invoiceHeader.getPKDefDoc4() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, invoiceHeader.getPKDefDoc4());
      }
      if (invoiceHeader.getPKDefDoc5() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, invoiceHeader.getPKDefDoc5());
      }
      if (invoiceHeader.getPKDefDoc6() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, invoiceHeader.getPKDefDoc6());
      }
      if (invoiceHeader.getPKDefDoc7() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, invoiceHeader.getPKDefDoc7());
      }
      if (invoiceHeader.getPKDefDoc8() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, invoiceHeader.getPKDefDoc8());
      }
      if (invoiceHeader.getPKDefDoc9() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, invoiceHeader.getPKDefDoc9());
      }
      if (invoiceHeader.getPKDefDoc10() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, invoiceHeader.getPKDefDoc10());
      }
      if (invoiceHeader.getPKDefDoc11() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, invoiceHeader.getPKDefDoc11());
      }
      if (invoiceHeader.getPKDefDoc12() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, invoiceHeader.getPKDefDoc12());
      }
      if (invoiceHeader.getPKDefDoc13() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, invoiceHeader.getPKDefDoc13());
      }
      if (invoiceHeader.getPKDefDoc14() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, invoiceHeader.getPKDefDoc14());
      }
      if (invoiceHeader.getPKDefDoc15() == null)
        stmt.setNull(58, 1);
      else {
        stmt.setString(58, invoiceHeader.getPKDefDoc15());
      }
      if (invoiceHeader.getPKDefDoc16() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, invoiceHeader.getPKDefDoc16());
      }
      if (invoiceHeader.getPKDefDoc17() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, invoiceHeader.getPKDefDoc17());
      }
      if (invoiceHeader.getPKDefDoc18() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, invoiceHeader.getPKDefDoc18());
      }
      if (invoiceHeader.getPKDefDoc19() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, invoiceHeader.getPKDefDoc19());
      }
      if (invoiceHeader.getPKDefDoc20() == null)
        stmt.setNull(63, 1);
      else {
        stmt.setString(63, invoiceHeader.getPKDefDoc20());
      }
      if (invoiceHeader.getDr() == null)
        stmt.setNull(64, 4);
      else {
        stmt.setInt(64, invoiceHeader.getDr().intValue());
      }
      if (invoiceHeader.getCstoreorganization() == null)
        stmt.setNull(65, 4);
      else {
        stmt.setString(65, invoiceHeader.getCstoreorganization());
      }

      if (invoiceHeader.getPk_purcorp() == null)
        stmt.setNull(66, 1);
      else {
        stmt.setString(66, invoiceHeader.getPk_purcorp());
      }

      if (invoiceHeader.getTmaketime() == null)
        stmt.setNull(67, 1);
      else {
        stmt.setString(67, invoiceHeader.getTmaketime().toString());
      }

      if (invoiceHeader.getTaudittime() == null)
        stmt.setNull(68, 1);
      else {
        stmt.setString(68, invoiceHeader.getTaudittime().toString());
      }

      if (invoiceHeader.getTlastmaketime() == null)
        stmt.setNull(69, 1);
      else {
        stmt.setString(69, invoiceHeader.getTlastmaketime().toString());
      }

      stmt.setString(70, invoiceHeader.getPrimaryKey());

      stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateHeader", new Object[] { invoiceHeader });
  }

  public void updateItem(InvoiceItemVO invoiceItem)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateItem", new Object[] { invoiceItem });

    if (invoiceItem != null) {
      InvoiceItemVO[] invoiceItems = { invoiceItem };
      updateItems(invoiceItems);
    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateItem", new Object[] { invoiceItem });
  }

  public double findInvoiceNumByInvoiceBPKMy(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceNumByInvoiceBPK", new Object[] { key });

    String sql = "SELECT ninvoicenum FROM po_invoice_b WHERE cinvoice_bid='" + key + "'";

    double value = 0.0D;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        BigDecimal ob = rs.getBigDecimal("ninvoicenum");
        if (ob != null) {
          value = ob.doubleValue();
        }
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceNumByInvoiceBPK", new Object[] { key });

    return value;
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "queryAllBodyData", new Object[] { key });

    InvoiceItemVO[] items = (InvoiceItemVO[])null;
    try
    {
      findItemsForHeader(key);
    } catch (SQLException e) {
      throw new BusinessException(e.getMessage());
    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "queryAllBodyData", new Object[] { key });

    return items;
  }

  public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "queryAllHeadData", new Object[] { whereString });

    String sql = "select pk_corp, vinvoicecode, iinvoicetype, cdeptid, cfreecustid, cvendormangid, cvendorbaseid, cemployeeid, dinvoicedate, darrivedate, cbiztype, caccountbankid, cpayunit, finitflag, cvoucherid, ctermprotocolid, coperator, caccountyear, cbilltype, ibillstatus, dauditdate, cauditpsn, vmemo, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10,";
    sql = sql + "vdef11, vdef12, vdef13, vdef14, vdef15, vdef16, vdef17, vdef18, vdef19, vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,pk_defdoc5,pk_defdoc6,pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11,pk_defdoc12,pk_defdoc13,pk_defdoc14,pk_defdoc15,pk_defdoc16,pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20,dr, cinvoiceid,pk_purcorp,tmaketime,taudittime,tlastmaketime from po_invoice ";
    sql = sql + " WHERE " + whereString;

    Vector vec = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        InvoiceHeaderVO invoiceHeader = new InvoiceHeaderVO();

        String pk_corp = rs.getString(1);
        invoiceHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String vinvoicecode = rs.getString(2);
        invoiceHeader.setVinvoicecode(vinvoicecode == null ? null : vinvoicecode.trim());

        Integer iinvoicetype = (Integer)rs.getObject(3);
        invoiceHeader.setIinvoicetype(iinvoicetype == null ? null : iinvoicetype);

        String cdeptid = rs.getString(4);
        invoiceHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cfreecustid = rs.getString(5);
        invoiceHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        String cvendormangid = rs.getString(6);
        invoiceHeader.setCvendormangid(cvendormangid == null ? null : cvendormangid.trim());

        String cvendorbaseid = rs.getString(7);
        invoiceHeader.setCvendorbaseid(cvendorbaseid == null ? null : cvendorbaseid.trim());

        String cemployeeid = rs.getString(8);
        invoiceHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String dinvoicedate = rs.getString(9);
        invoiceHeader.setDinvoicedate(dinvoicedate == null ? null : new UFDate(dinvoicedate.trim(), false));

        String darrivedate = rs.getString(10);
        invoiceHeader.setDarrivedate(darrivedate == null ? null : new UFDate(darrivedate.trim(), false));

        String cbiztype = rs.getString(11);
        invoiceHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        String caccountbankid = rs.getString(12);
        invoiceHeader.setCaccountbankid(caccountbankid == null ? null : caccountbankid.trim());

        String cpayunit = rs.getString(13);
        invoiceHeader.setCpayunit(cpayunit == null ? null : cpayunit.trim());

        Integer finitflag = (Integer)rs.getObject(14);
        invoiceHeader.setFinitflag(finitflag == null ? null : finitflag);

        String cvoucherid = rs.getString(15);
        invoiceHeader.setCvoucherid(cvoucherid == null ? null : cvoucherid.trim());

        String ctermProtocolid = rs.getString(16);
        invoiceHeader.setCtermprotocolid(ctermProtocolid == null ? null : ctermProtocolid.trim());

        String coperator = rs.getString(17);
        invoiceHeader.setCoperator(coperator == null ? null : coperator.trim());

        String caccountyear = rs.getString(18);
        invoiceHeader.setCaccountyear(caccountyear == null ? null : caccountyear.trim());

        String cbilltype = rs.getString(19);
        invoiceHeader.setCbilltype(cbilltype == null ? null : cbilltype.trim());

        Integer ibillstatus = (Integer)rs.getObject(20);
        invoiceHeader.setIbillstatus(ibillstatus == null ? null : ibillstatus);

        String dauditdate = rs.getString(21);
        invoiceHeader.setDauditdate(dauditdate == null ? null : new UFDate(dauditdate.trim(), false));

        String cauditpsn = rs.getString(22);
        invoiceHeader.setCauditpsn(cauditpsn == null ? null : cauditpsn.trim());

        String vmemo = rs.getString(23);
        invoiceHeader.setVmemo(vmemo == null ? null : vmemo.trim());

        String vdef1 = rs.getString(24);
        invoiceHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(25);
        invoiceHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(26);
        invoiceHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(27);
        invoiceHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(28);
        invoiceHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(29);
        invoiceHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(30);
        invoiceHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(31);
        invoiceHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(32);
        invoiceHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(33);
        invoiceHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String vdef11 = rs.getString(34);
        invoiceHeader.setVdef11(vdef11 == null ? null : vdef11.trim());

        String vdef12 = rs.getString(35);
        invoiceHeader.setVdef12(vdef12 == null ? null : vdef12.trim());

        String vdef13 = rs.getString(36);
        invoiceHeader.setVdef13(vdef13 == null ? null : vdef13.trim());

        String vdef14 = rs.getString(37);
        invoiceHeader.setVdef14(vdef14 == null ? null : vdef14.trim());

        String vdef15 = rs.getString(38);
        invoiceHeader.setVdef15(vdef15 == null ? null : vdef15.trim());

        String vdef16 = rs.getString(39);
        invoiceHeader.setVdef16(vdef16 == null ? null : vdef16.trim());

        String vdef17 = rs.getString(40);
        invoiceHeader.setVdef17(vdef17 == null ? null : vdef17.trim());

        String vdef18 = rs.getString(41);
        invoiceHeader.setVdef18(vdef18 == null ? null : vdef18.trim());

        String vdef19 = rs.getString(42);
        invoiceHeader.setVdef19(vdef19 == null ? null : vdef19.trim());

        String vdef20 = rs.getString(43);
        invoiceHeader.setVdef20(vdef20 == null ? null : vdef20.trim());

        String pk_defdoc1 = rs.getString(44);
        invoiceHeader.setPKDefDoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

        String pk_defdoc2 = rs.getString(45);
        invoiceHeader.setPKDefDoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

        String pk_defdoc3 = rs.getString(46);
        invoiceHeader.setPKDefDoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

        String pk_defdoc4 = rs.getString(47);
        invoiceHeader.setPKDefDoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

        String pk_defdoc5 = rs.getString(48);
        invoiceHeader.setPKDefDoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

        String pk_defdoc6 = rs.getString(49);
        invoiceHeader.setPKDefDoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

        String pk_defdoc7 = rs.getString(50);
        invoiceHeader.setPKDefDoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

        String pk_defdoc8 = rs.getString(51);
        invoiceHeader.setPKDefDoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

        String pk_defdoc9 = rs.getString(52);
        invoiceHeader.setPKDefDoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

        String pk_defdoc10 = rs.getString(53);
        invoiceHeader.setPKDefDoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

        String pk_defdoc11 = rs.getString(54);
        invoiceHeader.setPKDefDoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

        String pk_defdoc12 = rs.getString(55);
        invoiceHeader.setPKDefDoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

        String pk_defdoc13 = rs.getString(56);
        invoiceHeader.setPKDefDoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());

        String pk_defdoc14 = rs.getString(57);
        invoiceHeader.setPKDefDoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

        String pk_defdoc15 = rs.getString(58);
        invoiceHeader.setPKDefDoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

        String pk_defdoc16 = rs.getString(59);
        invoiceHeader.setPKDefDoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

        String pk_defdoc17 = rs.getString(60);
        invoiceHeader.setPKDefDoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

        String pk_defdoc18 = rs.getString(61);
        invoiceHeader.setPKDefDoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

        String pk_defdoc19 = rs.getString(62);
        invoiceHeader.setPKDefDoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

        String pk_defdoc20 = rs.getString(63);
        invoiceHeader.setPKDefDoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

        Integer dr = (Integer)rs.getObject(64);
        invoiceHeader.setDr(dr == null ? new Integer(0) : dr);

        String cinvoiceid = rs.getString(65);
        invoiceHeader.setPrimaryKey(cinvoiceid == null ? null : cinvoiceid.trim());

        String pk_purcorp = rs.getString(66);
        invoiceHeader.setPk_purcorp(pk_purcorp == null ? null : pk_purcorp.trim());

        String tmaketime = rs.getString(67);
        invoiceHeader.setTmaketime(tmaketime == null ? null : tmaketime.trim());

        String taudittime = rs.getString(68);
        invoiceHeader.setTaudittime(taudittime == null ? null : taudittime.trim());

        String tlastmaketime = rs.getString(69);
        invoiceHeader.setTlastmaketime(tlastmaketime == null ? null : tlastmaketime.trim());

        vec.addElement(invoiceHeader);
      }

      rs.close();
    } catch (SQLException e) {
      PubDMO.throwBusinessException(e);
      try
      {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException2) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException3)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "queryAllHeadData", new Object[] { whereString });

    InvoiceHeaderVO[] retVOs = (InvoiceHeaderVO[])null;
    if (vec.size() != 0) {
      retVOs = new InvoiceHeaderVO[vec.size()];
      vec.copyInto(retVOs);
    }

    return retVOs;
  }

  public Object findNaccumsettnumForScOrder(String bllid_b)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "queryAllHeadData", new Object[] { bllid_b });

    String sql = "select sum(naccumsettnum) from po_invoice_b ";
    sql = sql + " WHERE corder_bid ='" + bllid_b + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    String naccumsettnum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        naccumsettnum = rs.getString(1);
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "queryAllHeadData", new Object[] { bllid_b });

    return naccumsettnum;
  }

  public boolean checkGoing(String billId, String ApproveId, String ApproveDate, String checkNote)
    throws SQLException, BusinessException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "checkGoing", new Object[] { billId });

    InvoiceHeaderVO vo = findHeaderByPrimaryKey(billId);

    if ((vo.getIbillstatus().intValue() != BillStatus.AUDITING.intValue()) && (vo.getIbillstatus().intValue() != BillStatus.FREE.intValue())) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000096"));
    }

    String sql = "update po_invoice set cauditpsn=?,ibillstatus = ?,dauditdate=?,taudittime=? where cinvoiceid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, ApproveId);
      stmt.setInt(2, BillStatus.AUDITING.intValue());
      stmt.setString(3, ApproveDate.toString());
      stmt.setString(4, new UFDateTime(new Date()).toString());
      stmt.setString(5, billId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "checkGoing", new Object[] { billId });

    return true;
  }

  public boolean checkNoPass(String billId, String ApproveId, String ApproveDate, String checkNote)
    throws SQLException, BusinessException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "checkNoPass", new Object[] { billId });

    String sql = "update po_invoice set cauditpsn=null,ibillstatus = ?,dauditdate=null,taudittime=null where cinvoiceid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setInt(1, BillStatus.AUDITFAIL.intValue());
      stmt.setString(2, billId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "checkNoPass", new Object[] { billId });

    return true;
  }

  public boolean checkPass(String billId, String ApproveId, String ApproveDate, String checkNote)
    throws SQLException, BusinessException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "checkNoPass", new Object[] { billId });

    InvoiceHeaderVO vo = findHeaderByPrimaryKey(billId);

    if (vo.getIbillstatus().intValue() == BillStatus.AUDITED.intValue()) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000097"));
    }
    if (vo.getIbillstatus().intValue() == BillStatus.DELETED.intValue()) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000098"));
    }
    if (vo.getIbillstatus().intValue() == BillStatus.AUDITFAIL.intValue()) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000099"));
    }

    String sql = "update po_invoice set cauditpsn=?,ibillstatus = ?,dauditdate=?,taudittime=? where cinvoiceid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, ApproveId);
      stmt.setInt(2, BillStatus.AUDITED.intValue());
      stmt.setString(3, ApproveDate.toString());
      stmt.setString(4, new UFDateTime(new Date()).toString());
      stmt.setString(5, billId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }

      }
      catch (Exception localException1)
      {
      }

    }

    return true;
  }

  public double findAccumInvoiceNumByPoOrderBPKMy(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findAccumInvoiceNumByOrderBPK", new Object[] { key });

    String sql = "SELECT naccuminvoicenum FROM po_order_b WHERE corder_bid='" + key + "'";

    double value = 0.0D;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        BigDecimal ob = rs.getBigDecimal("naccuminvoicenum");
        if (ob != null) {
          value = ob.doubleValue();
        }
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findAccumInvoiceNumByOrderBPK", new Object[] { key });

    return value;
  }

  public double findAccumInvoiceNumByScOrderBPKMy(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findAccumInvoiceNumByScOrderBPKMy", new Object[] { key });

    String sql = "SELECT naccuminvoicenum FROM sc_order_b WHERE corder_bid='" + key + "'";

    double value = 0.0D;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        BigDecimal ob = rs.getBigDecimal("naccuminvoicenum");
        if (ob != null) {
          value = ob.doubleValue();
        }
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findAccumInvoiceNumByScOrderBPKMy", new Object[] { key });

    return value;
  }

  public Object[] findGenenelVOsFromPoOrderByCondsMy(String strFromWhere)
    throws SQLException, BusinessException, SystemException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findGenenelVOsByCondsMy", new Object[] { strFromWhere });

    String[] strHeadItems = { 
      "cauditorid", "cbilltypecode", "cbizid", "cbiztype", 
      "ccustomerid", "cdilivertypeid", "cdispatcherid", "cdptid", 
      "cinventoryid", "coperatorid", "cproviderid", 
      "cregister", "cwarehouseid", "cwastewarehouseid", "cwhsmanagerid", 
      "daccountdate", "dauditdate", "dbilldate", "dr", 
      "fbillflag", "fspecialflag", "pk_corp", "vbillcode", 
      "vdiliveraddress", "vnote", "vuserdef1", "vuserdef10", 
      "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", 
      "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", 
      "vuserdef11", "vuserdef12", 
      "vuserdef13", "vuserdef14", "vuserdef15", "vuserdef16", 
      "vuserdef17", "vuserdef18", "vuserdef19", "vuserdef20", 
      "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", "pk_defdoc8", 
      "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13", "pk_defdoc14", "pk_defdoc15", 
      "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20", 
      "ts", 
      "pk_calbody", "pk_purcorp" };

    String[] strBodyItems = { 
      "bzgflag", "castunitid", "ccorrespondbid", "ccorrespondcode", 
      "ccorrespondhid", "ccorrespondtype", "ccostobject", "cfirstbillbid", 
      "cfirstbillhid", "cfirsttype", "cgeneralbid", "cgeneralhid", 
      "cinventoryid", "cprojectid", "cprojectphaseid", "csourcebillbid", 
      "csourcebillhid", "csourcetype", "cwp", "dbizdate", 
      "dr", "dstandbydate", "dvalidate", "fchecked", 
      "flargess", "isok", "ninassistnum", "ninnum", 
      "nmny", "nneedinassistnum", "noutassistnum", "noutnum", 
      "nplannedmny", "nplannedprice", "nprice", "nshouldinnum", 
      "nshouldoutassistnum", "nshouldoutnum", "ntranoutnum", "vbatchcode", 
      "vfree1", "vfree10", "vfree2", "vfree3", 
      "vfree4", "vfree5", "vfree6", "vfree7", 
      "vfree8", "vfree9", "vproductbatch", "vsourcebillcode", 
      "vuserdef1", "vuserdef10", "vuserdef2", "vuserdef3", 
      "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", 
      "vuserdef8", "vuserdef9", 
      "vuserdef11", "vuserdef12", "vuserdef13", "vuserdef14", 
      "vuserdef15", "vuserdef16", "vuserdef17", "vuserdef18", 
      "vuserdef19", "vuserdef20", 
      "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", "pk_defdoc8", 
      "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13", "pk_defdoc14", "pk_defdoc15", 
      "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20", 
      "ts", 
      "vnotebody", 
      "cinvbasid", "pk_invoicecorp" };

    String[] strBb3Items = { 
      "caccountunitid", "cgeneralbb3", "cgeneralbid", "cgeneralhid", 
      "dr", "naccountmny", "naccountnum1", "naccountnum2", 
      "nmaterialmoney", "npmoney", "npprice", "nsignnum", 
      "ts" };

    String[] strPoOrderItems = { 
      "corderid", "corder_bid", "pk_corp", "cusedeptid", 
      "cmangid", "cbaseid", "nordernum", "cassistunit", 
      "nassistnum", "ndiscountrate", "idiscounttaxtype", "ntaxrate", 
      "ccurrencytypeid", "noriginalnetprice", "noriginalcurprice", "noriginalcurmny", 
      "noriginaltaxmny", "noriginaltaxpricemny", "nexchangeotobrate", "ntaxmny", 
      "nmoney", "ntaxpricemny", "nexchangeotoarate", "nassistcurmny", 
      "nassisttaxmny", "nassisttaxpricemny", "naccumarrvnum", "naccumstorenum", 
      "naccuminvoicenum", "naccumwastnum", "dplanarrvdate", "vvendorordercode", 
      "vvendororderrow", "cwarehouseid", "vreceiveaddress", "nconfirmnum", 
      "dconfirmdate", "ccorrectrowid", "dcorrectdate", "cprojectid", 
      "cprojectphaseid", "coperator", "iisactive", "iisreplenish", 
      "forderrowstatus", "csourcebilltype", "csourcebillid", "csourcerowid", 
      "cupsourcebilltype", "cupsourcebillid", "cupsourcebillrowid", "vmemo", 
      "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", 
      "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "dr", 
      "ts", 
      "norgnettaxprice" };

    String selectStr = "SELECT ic_general_h.cgeneralhid,";

    for (int i = 0; i < strHeadItems.length; i++) {
      selectStr = selectStr + "ic_general_h." + strHeadItems[i] + ",";
    }

    for (int i = 0; i < strBodyItems.length; i++) {
      selectStr = selectStr + "ic_general_b." + strBodyItems[i] + ",";
    }
    for (int i = 0; i < strBb3Items.length; i++) {
      selectStr = selectStr + "ic_general_bb3." + strBb3Items[i] + ",";
    }
    selectStr = selectStr + "po_order.vordercode,po_order.ctermprotocolid,";

    for (int i = 0; i < strPoOrderItems.length - 1; i++)
      selectStr = selectStr + "po_order_b." + strPoOrderItems[i] + ",";
    selectStr = selectStr + "po_order_b." + strPoOrderItems[(strPoOrderItems.length - 1)];

    String orderByStr = "ORDER BY ic_general_h.cgeneralhid,ic_general_b.cinventoryid";
    String sql = selectStr + " " + strFromWhere + " " + orderByStr;

    Connection con = null;
    Statement stmt = null;
    Hashtable voTable = new Hashtable();
    try {
      con = getConnection();

      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      String curKey = null;

      while (rs.next())
      {
        Object ob = rs.getString(1);
        if (!voTable.containsKey(ob)) {
          GeneralBillHeaderVO headVO = new GeneralBillHeaderVO();
          headVO.setCgeneralhid((String)ob);
          for (int i = 0; i < strHeadItems.length; i++) {
            ob = rs.getObject(i + 2);

            if ((ob == null) || (ob.toString().trim().equals(""))) {
              headVO.setAttributeValue(strHeadItems[i], null);
            }
            else if ((strHeadItems[i].equals("dbilldate")) || (strHeadItems[i].equals("dauditdate")) || 
              (strHeadItems[i].equals("daccountdate")))
            {
              headVO.setAttributeValue(strHeadItems[i], new UFDate(ob.toString().trim(), false));
            } else if (ob.getClass().equals(String.class))
              headVO.setAttributeValue(strHeadItems[i], ob.toString().trim());
            else if (ob.getClass().equals(Integer.class))
              headVO.setAttributeValue(strHeadItems[i], ob);
            else if (ob.getClass().equals(BigDecimal.class)) {
              headVO.setAttributeValue(strHeadItems[i], new UFDouble(ob.toString()));
            }
          }

          Vector voVEC = new Vector();
          voVEC.addElement(headVO);

          Vector[] allVEC = new Vector[5];
          allVEC[0] = voVEC;
          for (int i = 1; i < 5; i++) {
            allVEC[i] = new Vector();
          }

          curKey = headVO.getPrimaryKey();
          voTable.put(curKey, allVEC);
        }

        int recordPos = strHeadItems.length + 2;
        GeneralBillItemVO bodyVO = new GeneralBillItemVO();
        for (int i = 0; i < strBodyItems.length; i++) {
          ob = rs.getObject(recordPos + i);

          if ((ob == null) || (ob.toString().trim().equals(""))) {
            bodyVO.setAttributeValue(strBodyItems[i], null);
          }
          else if ((strBodyItems[i].equals("dvalidate")) || (strBodyItems[i].equals("dbizdate")) || 
            (strBodyItems[i].equals("dstandbydate")))
          {
            bodyVO.setAttributeValue(strBodyItems[i], new UFDate(ob.toString().trim(), false));
          } else if (ob.getClass().equals(String.class))
            bodyVO.setAttributeValue(strBodyItems[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            bodyVO.setAttributeValue(strBodyItems[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            bodyVO.setAttributeValue(strBodyItems[i], new UFDouble(ob.toString()));
          }
        }

        ((Vector[])voTable.get(curKey))[0].addElement(bodyVO);

        GeneralBb3VO bb3VO = new GeneralBb3VO();
        recordPos += strBodyItems.length;
        for (int i = 0; i < strBb3Items.length; i++) {
          ob = rs.getObject(recordPos + i);

          if ((ob == null) || (ob.toString().trim().equals("")))
            bb3VO.setAttributeValue(strBb3Items[i], null);
          else if (ob.getClass().equals(String.class))
            bb3VO.setAttributeValue(strBb3Items[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            bb3VO.setAttributeValue(strBb3Items[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            bb3VO.setAttributeValue(strBb3Items[i], new UFDouble(ob.toString()));
          }
        }

        ((Vector[])voTable.get(curKey))[1].addElement(bb3VO);

        recordPos += strBb3Items.length;
        ob = rs.getObject(recordPos);

        ((Vector[])voTable.get(curKey))[2].addElement((String)ob);

        if (ob != null)
        {
          recordPos++;
          ob = rs.getObject(recordPos);

          ((Vector[])voTable.get(curKey))[3].addElement((String)ob);

          nc.vo.po.OrderItemVO orderItemVO = new nc.vo.po.OrderItemVO();
          recordPos++;
          for (int i = 0; i < strPoOrderItems.length; i++) {
            ob = rs.getObject(recordPos + i);

            if ((ob == null) || (ob.toString().trim().equals("")))
              orderItemVO.setAttributeValue(strPoOrderItems[i], null);
            else if ((strPoOrderItems[i].equals("dplanarrvdate")) || 
              (strPoOrderItems[i].equals("dconfirmdate")) || 
              (strPoOrderItems[i].equals("dcorrectdate")))
            {
              orderItemVO.setAttributeValue(strPoOrderItems[i], new UFDate(ob.toString().trim(), false));
            } else if (ob.getClass().equals(String.class))
              orderItemVO.setAttributeValue(strPoOrderItems[i], ob.toString().trim());
            else if (ob.getClass().equals(Integer.class))
              orderItemVO.setAttributeValue(strPoOrderItems[i], ob);
            else if (ob.getClass().equals(BigDecimal.class)) {
              orderItemVO.setAttributeValue(strPoOrderItems[i], new UFDouble(ob.toString()));
            }
          }

          ((Vector[])voTable.get(curKey))[4].addElement(orderItemVO);
        } else {
          ((Vector[])voTable.get(curKey))[3].addElement(null);
          ((Vector[])voTable.get(curKey))[4].addElement(null);
        }
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findGenenelVOsByCondsMy", new Object[] { strFromWhere });

    GeneralBillVO[] allVOs = (GeneralBillVO[])null;
    Vector bb3VEC = new Vector();
    Vector orderCodeVEC = new Vector();
    Vector termProtocolVEC = new Vector();
    Vector orderDataVEC = new Vector();
    if (voTable.size() > 0)
    {
      allVOs = new GeneralBillVO[voTable.size()];
      Enumeration elems = voTable.elements();
      int i = 0;
      while (elems.hasMoreElements()) {
        Vector[] vecs = (Vector[])elems.nextElement();
        int len = vecs[0].size() - 1;

        GeneralBillHeaderVO headVO = (GeneralBillHeaderVO)vecs[0].elementAt(0);
        GeneralBillItemVO[] itemVOs = new GeneralBillItemVO[len];
        vecs[0].removeElementAt(0);
        vecs[0].copyInto(itemVOs);

        allVOs[i] = new GeneralBillVO(len);
        allVOs[i].setParentVO(headVO);
        allVOs[i].setChildrenVO(itemVOs);

        for (int j = 0; j < itemVOs.length; j++) {
          bb3VEC.addElement(vecs[1].elementAt(j));
          orderCodeVEC.addElement(vecs[2].elementAt(j));
          termProtocolVEC.addElement(vecs[3].elementAt(j));
          orderDataVEC.addElement(vecs[4].elementAt(j));
        }

        i++;
      }

      GeneralBb3VO[] bb3VOs = new GeneralBb3VO[bb3VEC.size()];
      bb3VEC.copyInto(bb3VOs);

      String[] strOrderCodes = new String[orderCodeVEC.size()];
      orderCodeVEC.copyInto(strOrderCodes);

      String[] strTermProtocolIds = new String[orderCodeVEC.size()];
      termProtocolVEC.copyInto(strTermProtocolIds);

      nc.vo.po.OrderItemVO[] orderItemVOs = new nc.vo.po.OrderItemVO[orderDataVEC.size()];
      orderDataVEC.copyInto(orderItemVOs);

      return filterStockData(allVOs, bb3VOs, strOrderCodes, strTermProtocolIds, orderItemVOs, true);
    }

    return null;
  }

  private Object[] filterStockData(GeneralBillVO[] allVOs, GeneralBb3VO[] bb3VOs, String[] strOrderCodes, String[] strTermProtocolIds, CircularlyAccessibleValueObject[] orderItemVOs, boolean bPO)
    throws SQLException
  {
    Connection con = null;
    Statement stmt = null;
    Hashtable t = new Hashtable();
    try {
      String sql = "select A.cgeneralbid, sum(B.nreasonwastenum) from ic_general_b A, po_invoice_b B ";
      sql = sql + "where A.cgeneralbid = B.cupsourcebillrowid and A.dr = 0 and B.dr = 0 ";
      sql = sql + " and A.cgeneralbid in ";

      String[] sID = new String[bb3VOs.length];
      for (int i = 0; i < bb3VOs.length; i++) sID[i] = bb3VOs[i].getCgeneralbid();

      String sIdSubSql = null;
      try {
        sIdSubSql = new TempTableDMO().insertTempTable(sID, "t_pu_general", "pk_pu");
      } catch (Exception e) {
        reportException(e);
        throw e;
      }

      sql = sql + sIdSubSql + " group by A.cgeneralbid";
      con = getConnection();

      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String sid = rs.getString(1);
        Object oTemp = rs.getObject(2);
        if (oTemp == null) continue; t.put(sid, new UFDouble(oTemp.toString()));
      }

      if (t.size() > 0)
        for (int i = 0; i < bb3VOs.length; i++) {
          Object oTemp = t.get(bb3VOs[i].getCgeneralbid());
          if (oTemp == null) continue; bb3VOs[i].setNaccountwastenum((UFDouble)oTemp);
        }
    }
    catch (Exception e) {
      throw new SQLException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    Vector vTemp = new Vector();
    Hashtable t1 = new Hashtable(); Hashtable t2 = new Hashtable();
    for (int i = 0; i < allVOs.length; i++) {
      GeneralBillItemVO[] bodyVO = allVOs[i].getItemVOs();
      if ((bodyVO != null) && (bodyVO.length != 0))
        for (int j = 0; j < bodyVO.length; j++) {
          if ((t1.containsKey(bodyVO[j].getCgeneralbid())) || (bodyVO[j].getNinnum() == null)) continue; t1.put(bodyVO[j].getCgeneralbid(), bodyVO[j].getNinnum());
        }
    }
    for (int i = 0; i < bb3VOs.length; i++) {
      if (t2.contains(bb3VOs[i].getCgeneralbid())) continue; t2.put(bb3VOs[i].getCgeneralbid(), new Object[] { bb3VOs[i].getNaccountnum1(), bb3VOs[i].getNsignnum() });
    }

    for (int i = 0; i < bb3VOs.length; i++) {
      Object o1 = t1.get(bb3VOs[i].getCgeneralbid());
      Object o2 = t2.get(bb3VOs[i].getCgeneralbid());
      Object o3 = t.get(bb3VOs[i].getCgeneralbid());
      if ((o1 != null) && (o2 != null)) {
        UFDouble nInNum = (UFDouble)o1;
        UFDouble nAccountNum1 = new UFDouble(0);
        UFDouble nSignNum = new UFDouble(0);
        UFDouble nReasonWasteNum = new UFDouble(0);
        Object[] o = (Object[])o2;
        if ((o[0] != null) && (o[0].toString().trim().length() > 0)) nAccountNum1 = (UFDouble)o[0];
        if ((o[1] != null) && (o[1].toString().trim().length() > 0)) nSignNum = (UFDouble)o[1];
        if (o3 != null) nReasonWasteNum = (UFDouble)o3;
        if (Math.abs(nAccountNum1.doubleValue()) < Math.abs(nSignNum.doubleValue() - nReasonWasteNum.doubleValue())) nAccountNum1 = new UFDouble(nSignNum.doubleValue() - nReasonWasteNum.doubleValue());
        if (Math.abs(nInNum.doubleValue() - nAccountNum1.doubleValue()) <= 0.0D) continue; vTemp.addElement(bb3VOs[i].getCgeneralbid());
      }
    }

    Vector vTemp1 = new Vector(); Vector vTemp2 = new Vector();
    for (int i = 0; i < bb3VOs.length; i++) {
      if (vTemp.contains(bb3VOs[i].getCgeneralbid())) {
        vTemp1.addElement(bb3VOs[i]);
        vTemp2.addElement(new Integer(i));
      }
    }
    if (vTemp1.size() == 0) return null;
    bb3VOs = new GeneralBb3VO[vTemp1.size()];
    vTemp1.copyInto(bb3VOs);

    vTemp1 = new Vector();
    for (int i = 0; i < strOrderCodes.length; i++) {
      if (!vTemp2.contains(new Integer(i))) continue; vTemp1.addElement(strOrderCodes[i]);
    }
    strOrderCodes = new String[vTemp1.size()];
    vTemp1.copyInto(strOrderCodes);

    vTemp1 = new Vector();
    for (int i = 0; i < strTermProtocolIds.length; i++) {
      if (!vTemp2.contains(new Integer(i))) continue; vTemp1.addElement(strTermProtocolIds[i]);
    }
    strTermProtocolIds = new String[vTemp1.size()];
    vTemp1.copyInto(strTermProtocolIds);

    vTemp1 = new Vector();
    for (int i = 0; i < orderItemVOs.length; i++) {
      if (!vTemp2.contains(new Integer(i))) continue; vTemp1.addElement(orderItemVOs[i]);
    }
    if (bPO) orderItemVOs = new nc.vo.po.OrderItemVO[vTemp1.size()]; else
      orderItemVOs = new nc.vo.sc.order.OrderItemVO[vTemp1.size()];
    vTemp1.copyInto(orderItemVOs);

    vTemp1 = new Vector();
    for (int i = 0; i < allVOs.length; i++) {
      vTemp2 = new Vector();
      GeneralBillItemVO[] bodyVO = allVOs[i].getItemVOs();
      if ((bodyVO != null) && (bodyVO.length != 0)) {
        for (int j = 0; j < bodyVO.length; j++) {
          if (!vTemp.contains(bodyVO[j].getCgeneralbid())) continue; vTemp2.addElement(bodyVO[j]);
        }
        if (vTemp2.size() == 0)
          continue;
        bodyVO = new GeneralBillItemVO[vTemp2.size()];
        vTemp2.copyInto(bodyVO);
        allVOs[i].setChildrenVO(bodyVO);
        vTemp1.addElement(allVOs[i]);
      }
    }
    if (vTemp1.size() == 0) return null;
    allVOs = new GeneralBillVO[vTemp1.size()];
    vTemp1.copyInto(allVOs);

    Object[] ob = new Object[5];
    ob[0] = allVOs;
    ob[1] = bb3VOs;
    ob[2] = strOrderCodes;
    ob[3] = strTermProtocolIds;
    ob[4] = orderItemVOs;
    return ob;
  }

  public Object queryRelatedData(String cSourceBillType, String[] cSourceBillRowID)
    throws SQLException
  {
    Object[][] oTemp = (Object[][])null;

    String sql1 = "select corder_bid, naccuminvoicenum, nordernum from po_order_b where corder_bid in ('";
    String sql2 = "select corder_bid, naccuminvoicenum, nordernum from sc_order_b where corder_bid in ('";
    String sql3 = "select A.cgeneralbid, B.nsignnum, A.ninnum from ic_general_b A, ic_general_bb3 B where A.cgeneralbid = B.cgeneralbid and A.cgeneralbid in ('";

    for (int i = 0; i < cSourceBillRowID.length - 1; i++) {
      sql1 = sql1 + cSourceBillRowID[i] + "','";
      sql2 = sql2 + cSourceBillRowID[i] + "','";
      sql3 = sql3 + cSourceBillRowID[i] + "','";
    }
    sql1 = sql1 + cSourceBillRowID[(cSourceBillRowID.length - 1)] + "')";
    sql2 = sql2 + cSourceBillRowID[(cSourceBillRowID.length - 1)] + "')";
    sql3 = sql3 + cSourceBillRowID[(cSourceBillRowID.length - 1)] + "')";

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Hashtable t = new Hashtable();
    String id = null;
    Object o1 = null; Object o2 = null;
    try {
      con = getConnection();
      stmt = con.createStatement();

      if (cSourceBillType.equals("21")) rs = stmt.executeQuery(sql1);
      else if (cSourceBillType.equals("61")) rs = stmt.executeQuery(sql2);
      else if ((cSourceBillType.equals("45")) || (cSourceBillType.equals("47"))) rs = stmt.executeQuery(sql3);
    	   else
        return null;
      while(rs.next()){
    	  id = rs.getString(1);
          o1 = rs.getObject(2);
          o2 = rs.getObject(3);
          t.put(id, new Object[] { o1, o2 });
      }
      if (rs != null) rs.close();

      if (t.size() > 0) {
        oTemp = new Object[cSourceBillRowID.length][2];
        Object[] oo = (Object[])null;
        for (int i = 0; i < oTemp.length; i++) {
          o1 = t.get(cSourceBillRowID[i]);
          if (o1 != null) {
            oo = (Object[])o1;
            if (oo[0] != null)
              oTemp[i][0] = oo[0];
            else {
              oTemp[i][0] = new BigDecimal(0);
            }
            if (oo[1] != null)
              oTemp[i][1] = oo[1];
            else
              oTemp[i][1] = new BigDecimal(0);
          }
          else {
            oTemp[i][0] = new UFDouble(0);
            oTemp[i][1] = new UFDouble(0);
          }
        }
      }
    }
    catch (Exception e) {
      throw new SQLException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException3) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException4)
      {
      }
    }
    try
    {
      if (stmt != null)
        stmt.close();
    } catch (Exception localException5) {
    }
    try {
      if (con != null)
        con.close();
    }
    catch (Exception localException6)
    {
    }
    return oTemp;
  }

  public void isSignNumExceedInNum(ParaPoToIcLendRewriteVO[] VOs)
    throws SQLException, BusinessException
  {
    String sql1 = "select A.cgeneralbid, A.ninnum, B.nsignnum from ic_general_b A, ic_general_bb3 B ";
    sql1 = sql1 + "where A.cgeneralbid = B.cgeneralbid and A.dr = 0 and B.dr = 0 and A.cgeneralbid in ('";

    String sql2 = "select A.cgeneralbid, sum(B.nreasonwastenum) from ic_general_b A, po_invoice_b B ";
    sql2 = sql2 + "where A.cgeneralbid = B.cupsourcebillrowid and A.dr = 0 and B.dr = 0 and A.cgeneralbid in ('";

    for (int i = 0; i < VOs.length - 1; i++) {
      sql1 = sql1 + VOs[i].getCRowId() + "','";
      sql2 = sql2 + VOs[i].getCRowId() + "','";
    }
    sql1 = sql1 + VOs[(VOs.length - 1)].getCRowId() + "') ";
    sql2 = sql2 + VOs[(VOs.length - 1)].getCRowId() + "') ";

    sql2 = sql2 + "group by A.cgeneralbid ";

    Connection con = null;
    Statement stmt = null;
    Hashtable hashBid2Num = new Hashtable(); Hashtable hashBidWastNum = new Hashtable();
    try {
      con = getConnection();
      stmt = con.createStatement();

      ResultSet rs = stmt.executeQuery(sql1);
      while (rs.next()) {
        String sid = rs.getString(1);

        Object oTemp = rs.getString(2);
        UFDouble d1 = new UFDouble(0);
        if (oTemp != null) d1 = new UFDouble(oTemp.toString());

        oTemp = rs.getString(3);
        UFDouble d2 = new UFDouble(0);
        if (oTemp != null) d2 = new UFDouble(oTemp.toString());

        hashBid2Num.put(sid, new UFDouble[] { d1, d2 });
      }
      if (rs != null) rs.close();

      rs = stmt.executeQuery(sql2);
      while (rs.next()) {
        String sid = rs.getString(1);
        Object oTemp = rs.getObject(2);
        if (oTemp == null) continue; hashBidWastNum.put(sid, new UFDouble(oTemp.toString()));
      }

      if (hashBid2Num.size() == 0) throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000242"));

      for (int i = 0; i < VOs.length; i++) {
        String s = VOs[i].getCRowId();
        if ((s != null) && (s.trim().length() != 0)) {
          Object oTemp1 = hashBid2Num.get(s);
          Object oTemp2 = hashBidWastNum.get(s);
          if (oTemp2 == null) oTemp2 = new UFDouble(0);

          UFDouble[] ufdUsuableNums = (UFDouble[])oTemp1;
          UFDouble ufdWastNum = (UFDouble)oTemp2;
          UFDouble ufdRslt = new UFDouble(0.0D);
          if (ufdUsuableNums[1] != null) {
            ufdRslt = ufdRslt.add(ufdUsuableNums[1]);
          }
          if (VOs[i].getDSubNum() != null) {
            ufdRslt = ufdRslt.add(VOs[i].getDSubNum());
          }
          if (ufdWastNum != null) {
            ufdRslt = ufdRslt.sub(ufdWastNum);
          }
          if (VOs[i].getDWasteSubNum() != null) {
            ufdRslt = ufdRslt.sub(VOs[i].getDWasteSubNum());
          }

          ufdUsuableNums[1] = ufdRslt;
          hashBid2Num.put(s, ufdUsuableNums);
        }
      }
      for (int i = 0; i < VOs.length; i++) {
        String s = VOs[i].getCRowId();
        if ((s != null) && (s.trim().length() != 0)) {
          Object oTemp1 = hashBid2Num.get(s);
          UFDouble[] ufdUsuableNums = (UFDouble[])oTemp1;
          UFDouble ufdRslt = new UFDouble(0.0D);

          if (ufdUsuableNums[1] != null) {
            ufdRslt = ufdUsuableNums[1];

            if ((ufdUsuableNums[0].doubleValue() >= 0.0D) && (ufdRslt.compareTo(ufdUsuableNums[0]) > 0)) throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000241"));
            if ((ufdUsuableNums[0].doubleValue() >= 0.0D) || (ufdRslt.compareTo(ufdUsuableNums[0]) >= 0)) continue; throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000241"));
          }
        }
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) throw new BusinessException(e.getMessage());
      throw new SQLException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2)
      {
      }
    }
  }

  public Object[] findGenenelVOsFromScOrderByCondsMy(String strFromWhere)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findGenenelVOsByCondsMy", new Object[] { strFromWhere });

    String[] strHeadItems = { 
      "cauditorid", "cbilltypecode", "cbizid", "cbiztype", 
      "ccustomerid", "cdilivertypeid", "cdispatcherid", "cdptid", 
      "cinventoryid", "coperatorid", "cproviderid", 
      "cregister", "cwarehouseid", "cwastewarehouseid", "cwhsmanagerid", 
      "daccountdate", "dauditdate", "dbilldate", "dr", 
      "fbillflag", "fspecialflag", "pk_corp", "vbillcode", 
      "vdiliveraddress", "vnote", "vuserdef1", "vuserdef10", 
      "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", 
      "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", 
      "ts", "pk_calbody" };

    String[] strBodyItems = { 
      "bzgflag", "castunitid", "ccorrespondbid", "ccorrespondcode", 
      "ccorrespondhid", "ccorrespondtype", "ccostobject", "cfirstbillbid", 
      "cfirstbillhid", "cfirsttype", "cgeneralbid", "cgeneralhid", 
      "cinventoryid", "cprojectid", "cprojectphaseid", "csourcebillbid", 
      "csourcebillhid", "csourcetype", "cwp", "dbizdate", 
      "dr", "dstandbydate", "dvalidate", "fchecked", 
      "flargess", "isok", "ninassistnum", "ninnum", 
      "nmny", "nneedinassistnum", "noutassistnum", "noutnum", 
      "nplannedmny", "nplannedprice", "nprice", "nshouldinnum", 
      "nshouldoutassistnum", "nshouldoutnum", "ntranoutnum", "vbatchcode", 
      "vfree1", "vfree10", "vfree2", "vfree3", 
      "vfree4", "vfree5", "vfree6", "vfree7", 
      "vfree8", "vfree9", "vproductbatch", "vsourcebillcode", 
      "vuserdef1", "vuserdef10", "vuserdef2", "vuserdef3", 
      "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", 
      "vuserdef8", "vuserdef9", "ts", 
      "cinvbasid" };

    String[] strBb3Items = { 
      "caccountunitid", "cgeneralbb3", "cgeneralbid", "cgeneralhid", 
      "dr", "naccountmny", "naccountnum1", "naccountnum2", 
      "nmaterialmoney", "npmoney", "npprice", "nsignnum", 
      "ts" };

    String[] strScOrderItems = { 
      "corderid", "corder_bid", "pk_corp", "cmangid", 
      "cbaseid", "nordernum", "cassistunit", "nassistnum", 
      "ndiscountrate", "idiscounttaxtype", "ntaxrate", "ccurrencytypeid", 
      "noriginalnetprice", "noriginalcurprice", "noriginalcurmny", "noriginaltaxmny", 
      "noriginalsummny", "nexchangeotobrate", "ntaxmny", "nmoney", 
      "nsummny", "nexchangeotoarate", "nassistcurmny", "nassisttaxmny", 
      "nassistsummny", "naccumarrvnum", "naccumstorenum", "naccuminvoicenum", 
      "naccumwastnum", "dplanarrvdate", "cwarehouseid", "creceiveaddress", 
      "cprojectid", "cprojectphaseid", "coperator", "forderrowstatus", 
      "cordersource", "csourcebillid", "csourcebillrow", "cupsourcebilltype", 
      "cupsourcebillid", "cupsourcebillrowid", "vmemo", 
      "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", 
      "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", 
      "dr", 
      "ts", 
      "norgnettaxprice" };

    String selectStr = "SELECT ic_general_h.cgeneralhid,";

    for (int i = 0; i < strHeadItems.length; i++) {
      selectStr = selectStr + "ic_general_h." + strHeadItems[i] + ",";
    }

    for (int i = 0; i < strBodyItems.length; i++) {
      selectStr = selectStr + "ic_general_b." + strBodyItems[i] + ",";
    }
    for (int i = 0; i < strBb3Items.length; i++) {
      selectStr = selectStr + "ic_general_bb3." + strBb3Items[i] + ",";
    }
    selectStr = selectStr + "sc_order.vordercode,sc_order.ctermProtocolid,";

    for (int i = 0; i < strScOrderItems.length - 1; i++)
      selectStr = selectStr + "sc_order_b." + strScOrderItems[i] + ",";
    selectStr = selectStr + "sc_order_b." + strScOrderItems[(strScOrderItems.length - 1)];

    String orderByStr = "ORDER BY ic_general_h.cgeneralhid,ic_general_b.cinventoryid";
    String sql = selectStr + " " + strFromWhere + " " + orderByStr;

    Connection con = null;
    Statement stmt = null;
    Hashtable voTable = new Hashtable();
    try {
      con = getConnection();

      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      String curKey = null;

      while (rs.next())
      {
        Object ob = rs.getString(1);
        if (!voTable.containsKey(ob)) {
          GeneralBillHeaderVO headVO = new GeneralBillHeaderVO();
          headVO.setCgeneralhid((String)ob);
          for (int i = 0; i < strHeadItems.length; i++) {
            ob = rs.getObject(i + 2);

            if ((ob == null) || (ob.toString().trim().equals(""))) {
              headVO.setAttributeValue(strHeadItems[i], null);
            }
            else if ((strHeadItems[i].equals("dbilldate")) || (strHeadItems[i].equals("dauditdate")) || 
              (strHeadItems[i].equals("daccountdate")))
            {
              headVO.setAttributeValue(strHeadItems[i], new UFDate(ob.toString().trim(), false));
            } else if (ob.getClass().equals(String.class))
              headVO.setAttributeValue(strHeadItems[i], ob.toString().trim());
            else if (ob.getClass().equals(Integer.class))
              headVO.setAttributeValue(strHeadItems[i], ob);
            else if (ob.getClass().equals(BigDecimal.class)) {
              headVO.setAttributeValue(strHeadItems[i], new UFDouble(ob.toString()));
            }
          }

          Vector voVEC = new Vector();
          voVEC.addElement(headVO);

          Vector[] allVEC = new Vector[5];
          allVEC[0] = voVEC;
          for (int i = 1; i < 5; i++) {
            allVEC[i] = new Vector();
          }

          curKey = headVO.getPrimaryKey();
          voTable.put(curKey, allVEC);
        }

        int recordPos = strHeadItems.length + 2;
        GeneralBillItemVO bodyVO = new GeneralBillItemVO();
        for (int i = 0; i < strBodyItems.length; i++) {
          ob = rs.getObject(recordPos + i);

          if ((ob == null) || (ob.toString().trim().equals(""))) {
            bodyVO.setAttributeValue(strBodyItems[i], null);
          }
          else if ((strBodyItems[i].equals("dvalidate")) || (strBodyItems[i].equals("dbizdate")) || 
            (strBodyItems[i].equals("dstandbydate")))
          {
            bodyVO.setAttributeValue(strBodyItems[i], new UFDate(ob.toString().trim(), false));
          } else if (ob.getClass().equals(String.class))
            bodyVO.setAttributeValue(strBodyItems[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            bodyVO.setAttributeValue(strBodyItems[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            bodyVO.setAttributeValue(strBodyItems[i], new UFDouble(ob.toString()));
          }
        }

        ((Vector[])voTable.get(curKey))[0].addElement(bodyVO);

        GeneralBb3VO bb3VO = new GeneralBb3VO();
        recordPos += strBodyItems.length;
        for (int i = 0; i < strBb3Items.length; i++) {
          ob = rs.getObject(recordPos + i);

          if ((ob == null) || (ob.toString().trim().equals("")))
            bb3VO.setAttributeValue(strBb3Items[i], null);
          else if (ob.getClass().equals(String.class))
            bb3VO.setAttributeValue(strBb3Items[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            bb3VO.setAttributeValue(strBb3Items[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            bb3VO.setAttributeValue(strBb3Items[i], new UFDouble(ob.toString()));
          }
        }

        ((Vector[])voTable.get(curKey))[1].addElement(bb3VO);

        recordPos += strBb3Items.length;
        ob = rs.getObject(recordPos);

        ((Vector[])voTable.get(curKey))[2].addElement((String)ob);

        if (ob != null)
        {
          recordPos++;
          ob = rs.getObject(recordPos);

          ((Vector[])voTable.get(curKey))[3].addElement((String)ob);

          nc.vo.po.OrderItemVO orderItemVO = new nc.vo.po.OrderItemVO();
          recordPos++;
          for (int i = 0; i < strScOrderItems.length; i++) {
            ob = rs.getObject(recordPos + i);

            if ((ob == null) || (ob.toString().trim().equals("")))
              orderItemVO.setAttributeValue(strScOrderItems[i], null);
            else if (strScOrderItems[i].equals("dplanarrvdate"))
              orderItemVO.setAttributeValue(strScOrderItems[i], new UFDate(ob.toString().trim(), false));
            else if (ob.getClass().equals(String.class)) {
              if (strScOrderItems[i].equals("cordersource"))
                orderItemVO.setCsourcebilltype(ob.toString());
              else if (strScOrderItems[i].equals("csourcebillrow"))
                orderItemVO.setCsourcerowid(ob.toString());
              else
                orderItemVO.setAttributeValue(strScOrderItems[i], ob.toString().trim());
            }
            else if (ob.getClass().equals(Integer.class)) {
              if (strScOrderItems[i].equals("idiscounttaxtype"))
              {
                if ((ob == null) || (ob.toString().length() < 1)) {
                  orderItemVO.setIdiscounttaxtype(new Integer(1));
                } else {
                  int iDis = new Integer(ob.toString().trim()).intValue();
                  if (iDis == 0)
                    orderItemVO.setIdiscounttaxtype(new Integer(0));
                  else if (iDis == 1)
                    orderItemVO.setIdiscounttaxtype(new Integer(1));
                  else if (iDis == 2)
                    orderItemVO.setIdiscounttaxtype(new Integer(2));
                  else
                    orderItemVO.setIdiscounttaxtype(new Integer(1));
                }
              }
              else
                orderItemVO.setAttributeValue(strScOrderItems[i], ob);
            }
            else if (ob.getClass().equals(BigDecimal.class)) {
              if (strScOrderItems[i].equals("noriginalsummny"))
                orderItemVO.setNoriginaltaxpricemny(new UFDouble(ob.toString()));
              else if (strScOrderItems[i].equals("nsummny"))
                orderItemVO.setNtaxpricemny(new UFDouble(ob.toString()));
              else if (strScOrderItems[i].equals("nassistsummny"))
                orderItemVO.setNassisttaxmny(new UFDouble(ob.toString()));
              else {
                orderItemVO.setAttributeValue(strScOrderItems[i], new UFDouble(ob.toString()));
              }
            }
          }

          ((Vector[])voTable.get(curKey))[4].addElement(orderItemVO);
        } else {
          ((Vector[])voTable.get(curKey))[3].addElement(null);
          ((Vector[])voTable.get(curKey))[4].addElement(null);
        }

      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findGenenelVOsByCondsMy", new Object[] { strFromWhere });

    GeneralBillVO[] allVOs = (GeneralBillVO[])null;
    Vector bb3VEC = new Vector();
    Vector orderCodeVEC = new Vector();
    Vector termProtocolVEC = new Vector();
    Vector orderDataVEC = new Vector();
    if (voTable.size() > 0)
    {
      allVOs = new GeneralBillVO[voTable.size()];
      Enumeration elems = voTable.elements();
      int i = 0;
      while (elems.hasMoreElements()) {
        Vector[] vecs = (Vector[])elems.nextElement();
        int len = vecs[0].size() - 1;

        GeneralBillHeaderVO headVO = (GeneralBillHeaderVO)vecs[0].elementAt(0);
        GeneralBillItemVO[] itemVOs = new GeneralBillItemVO[len];
        vecs[0].removeElementAt(0);
        vecs[0].copyInto(itemVOs);

        allVOs[i] = new GeneralBillVO(len);
        allVOs[i].setParentVO(headVO);
        allVOs[i].setChildrenVO(itemVOs);

        for (int j = 0; j < itemVOs.length; j++) {
          bb3VEC.addElement(vecs[1].elementAt(j));
          orderCodeVEC.addElement(vecs[2].elementAt(j));
          termProtocolVEC.addElement(vecs[3].elementAt(j));
          orderDataVEC.addElement(vecs[4].elementAt(j));
        }

        i++;
      }

      GeneralBb3VO[] bb3VOs = new GeneralBb3VO[bb3VEC.size()];
      bb3VEC.copyInto(bb3VOs);

      String[] strOrderCodes = new String[orderCodeVEC.size()];
      orderCodeVEC.copyInto(strOrderCodes);

      String[] strTermProtocolIds = new String[orderCodeVEC.size()];
      termProtocolVEC.copyInto(strTermProtocolIds);

      nc.vo.po.OrderItemVO[] orderItemVOs = new nc.vo.po.OrderItemVO[orderDataVEC.size()];
      orderDataVEC.copyInto(orderItemVOs);

      return filterStockData(allVOs, bb3VOs, strOrderCodes, strTermProtocolIds, orderItemVOs, true);
    }

    return null;
  }

  public OrderVO[] findScOrderVOsByCondsMy(String strFromWhere)
    throws SQLException, ClassNotFoundException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findScOrderVOsByCondsMy", new Object[] { strFromWhere });

    String[] strHeadItems = { 
      "corderid", "vordercode", "pk_corp", "cpurorganization", 
      "cwareid", "dorderdate", "cvendorid", "caccountbankid", 
      "cdeptid", "cemployeeid", "cbiztype", "creciever", 
      "cgiveinvoicevendor", "ctransmodeid", "ctermProtocolid", "caccountyear", 
      "coperator", "ibillstatus", "dauditdate", "cauditpsn", 
      "cvendormangid", "vmemo", 
      "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10", 
      "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", 
      "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", 
      "pk_defdoc8", "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13", "pk_defdoc14", 
      "pk_defdoc15", "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20", 
      "dr", 
      "ts" };

    String[] strBodyItems = { 
      "corderid", "corder_bid", "pk_corp", "cmangid", 
      "cbaseid", "nordernum", "cassistunit", "nassistnum", 
      "ndiscountrate", "idiscounttaxtype", "ntaxrate", "ccurrencytypeid", 
      "noriginalnetprice", "noriginalcurprice", "noriginalcurmny", "noriginaltaxmny", 
      "noriginalsummny", "nexchangeotobrate", "ntaxmny", "nmoney", 
      "nsummny", "nexchangeotoarate", "nassistcurmny", "nassisttaxmny", 
      "nassistsummny", "naccumarrvnum", "naccumstorenum", "naccuminvoicenum", 
      "naccumwastnum", "dplanarrvdate", "cwarehouseid", "creceiveaddress", 
      "cprojectid", "cprojectphaseid", "coperator", "forderrowstatus", 
      "cordersource", "csourcebillid", "csourcebillrow", 
      "cupsourcebilltype", "cupsourcebillid", "cupsourcebillrowid", "vmemo", 
      "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", 
      "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", 
      "vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", 
      "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", 
      "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", 
      "pk_defdoc8", "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13", "pk_defdoc14", 
      "pk_defdoc15", "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20", 
      "dr", 
      "ts", 
      "norgnettaxprice" };

    StringBuffer selectStr = new StringBuffer("sc_order." + strHeadItems[0]);
    for (int i = 1; i < strHeadItems.length; i++) {
      selectStr.append(",sc_order." + strHeadItems[i]);
    }
    for (int i = 0; i < strBodyItems.length; i++) {
      selectStr.append(",sc_order_b." + strBodyItems[i]);
    }

    String orderByStr = "ORDER BY sc_order.vordercode,sc_order.corderid";

    String sql = "SELECT " + selectStr.toString() + 
      " " + strFromWhere + 
      " " + orderByStr;

    Connection con = null;
    Statement stmt = null;
    Hashtable voTable = new Hashtable();
    try {
      con = getConnection();
      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      String curKey = null;

      Object ob = null;

      while (rs.next())
      {
        ob = rs.getString(1);
        if (!voTable.containsKey(ob)) {
          OrderHeaderVO headVO = new OrderHeaderVO();
          headVO.setPrimaryKey(ob.toString());
          for (int i = 1; i < strHeadItems.length; i++) {
            ob = rs.getObject(i + 1);

            if ((ob == null) || (ob.toString().trim().equals("")))
              headVO.setAttributeValue(strHeadItems[i], null);
            else if ((strHeadItems[i].equals("dorderdate")) || (strHeadItems[i].equals("dauditdate")))
            {
              headVO.setAttributeValue(strHeadItems[i], new UFDate(ob.toString().trim(), false));
            } else if (ob.getClass().equals(String.class))
              headVO.setAttributeValue(strHeadItems[i], ob.toString().trim());
            else if (ob.getClass().equals(Integer.class))
              headVO.setAttributeValue(strHeadItems[i], ob);
            else if (ob.getClass().equals(BigDecimal.class)) {
              headVO.setAttributeValue(strHeadItems[i], new UFDouble(ob.toString()));
            }
          }

          Vector voVEC = new Vector();
          voVEC.addElement(headVO);

          curKey = headVO.getPrimaryKey();
          voTable.put(curKey, voVEC);
        }

        int recordPos = strHeadItems.length + 1;
        nc.vo.sc.order.OrderItemVO bodyVO = new nc.vo.sc.order.OrderItemVO();
        for (int i = 0; i < strBodyItems.length; i++) {
          ob = rs.getObject(recordPos + i);

          if ((ob == null) || (ob.toString().trim().equals("")))
            bodyVO.setAttributeValue(strBodyItems[i], null);
          else if (strBodyItems[i].equals("dplanarrvdate"))
            bodyVO.setAttributeValue(strBodyItems[i], new UFDate(ob.toString().trim(), false));
          else if (ob.getClass().equals(String.class))
            bodyVO.setAttributeValue(strBodyItems[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class)) {
            if (strBodyItems[i].equals("idiscounttaxtype"))
            {
              if ((ob == null) || (ob.toString().length() < 1)) {
                bodyVO.setIdiscounttaxtype(new Integer(1));
              } else {
                int iDis = new Integer(ob.toString().trim()).intValue();
                if (iDis == 0)
                  bodyVO.setIdiscounttaxtype(new Integer(0));
                else if (iDis == 1)
                  bodyVO.setIdiscounttaxtype(new Integer(1));
                else if (iDis == 2)
                  bodyVO.setIdiscounttaxtype(new Integer(2));
                else
                  bodyVO.setIdiscounttaxtype(new Integer(1));
              }
            }
            else
              bodyVO.setAttributeValue(strBodyItems[i], ob);
          }
          else if (ob.getClass().equals(BigDecimal.class)) {
            bodyVO.setAttributeValue(strBodyItems[i], new UFDouble(ob.toString()));
          }
        }

        ((Vector)voTable.get(curKey)).addElement(bodyVO);
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findScOrderVOsByCondsMy", new Object[] { strFromWhere });

    OrderVO[] allVOs = (OrderVO[])null;
    if (voTable.size() > 0) {
      allVOs = new OrderVO[voTable.size()];
      Enumeration elems = voTable.elements();
      int i = 0;
      while (elems.hasMoreElements()) {
        Vector vec = (Vector)elems.nextElement();
        int len = vec.size() - 1;

        OrderHeaderVO headVO = (OrderHeaderVO)vec.elementAt(0);
        nc.vo.sc.order.OrderItemVO[] itemVOs = new nc.vo.sc.order.OrderItemVO[len];
        vec.removeElementAt(0);
        vec.copyInto(itemVOs);

        allVOs[i] = new OrderVO();
        allVOs[i].setParentVO(headVO);
        allVOs[i].setChildrenVO(itemVOs);

        i++;
      }
    }

    return allVOs;
  }

  public void updateAccumInvoiceNumByScOrderBPKMy(String key, double value)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateAccumInvoiceNumByScOrderBPKMy", new Object[] { key });

    String[] keys = { key };
    double[] values = { value };
    updateAccumInvoiceNumByScOrderBPKMy(keys, values);

    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateAccumInvoiceNumByScOrderBPKMy", new Object[] { key });
  }

  public Object[] findGeneralVOsFromIDs(String strIdWhere)
    throws Exception
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "getGeneralVOsFromInvVO", new Object[] { strIdWhere });

    String[] strHeadItems = { 
      "cauditorid", "cbilltypecode", "cbizid", "cbiztype", 
      "ccustomerid", "cdilivertypeid", "cdispatcherid", "cdptid", 
      "cgeneralhid", "pk_calbody", 
      "cinventoryid", "coperatorid", "cproviderid", 
      "cregister", "cwarehouseid", "cwastewarehouseid", "cwhsmanagerid", 
      "daccountdate", "dauditdate", "dbilldate", "dr", 
      "fbillflag", "fspecialflag", "pk_corp", "vbillcode", 
      "vdiliveraddress", "vnote", "vuserdef1", "vuserdef10", 
      "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", 
      "vuserdef6", "vuserdef7", "vuserdef8", "vuserdef9", 
      "pk_purcorp" };

    String[] strBodyItems = { 
      "bzgflag", "castunitid", "ccorrespondbid", "ccorrespondcode", 
      "ccorrespondhid", "ccorrespondtype", "ccostobject", "cfirstbillbid", 
      "cfirstbillhid", "cfirsttype", "cgeneralbid", "cgeneralhid", 
      "cinventoryid", "cprojectid", "cprojectphaseid", "csourcebillbid", 
      "csourcebillhid", "csourcetype", "cwp", "dbizdate", 
      "dr", "dstandbydate", "dvalidate", "fchecked", 
      "flargess", "isok", "ninassistnum", "ninnum", 
      "nmny", "nneedinassistnum", "noutassistnum", "noutnum", 
      "nplannedmny", "nplannedprice", "nprice", "nshouldinnum", 
      "nshouldoutassistnum", "nshouldoutnum", "ntranoutnum", "vbatchcode", 
      "vfree1", "vfree10", "vfree2", "vfree3", 
      "vfree4", "vfree5", "vfree6", "vfree7", 
      "vfree8", "vfree9", "vproductbatch", "vsourcebillcode", 
      "vuserdef1", "vuserdef10", "vuserdef2", "vuserdef3", 
      "vuserdef4", "vuserdef5", "vuserdef6", "vuserdef7", 
      "vuserdef8", "vuserdef9", "pk_invoicecorp", "vfirstbillcode", 
      "hsl", "cinvbasid", "cvendorid" };

    String[] strBb3Items = { 
      "caccountunitid", "cgeneralbb3", "cgeneralbid", "cgeneralhid", 
      "dr", "naccountmny", "naccountnum1", "naccountnum2", 
      "nmaterialmoney", "npmoney", "npprice", "nsignnum" };

    String selectStr = "SELECT ";

    for (int i = 0; i < strHeadItems.length; i++) {
      selectStr = selectStr + "ic_general_h." + strHeadItems[i] + ",";
    }

    for (int i = 0; i < strBodyItems.length; i++) {
      selectStr = selectStr + "ic_general_b." + strBodyItems[i] + ",";
    }
    for (int i = 0; i < strBb3Items.length - 1; i++)
      selectStr = selectStr + "ic_general_bb3." + strBb3Items[i] + ",";
    selectStr = selectStr + "ic_general_bb3." + strBb3Items[(strBb3Items.length - 1)];

    String sql = selectStr + " FROM " + new RelatedTableVO("入库单").getFromTable() + 
      " WHERE " + strIdWhere + 
      " AND ic_general_h.dr=0 AND ic_general_b.dr=0 AND ic_general_bb3.dr=0";

    SCMEnv.out(sql);

    Vector hVector = new Vector();
    Vector bVector = new Vector();
    Vector bb3Vector = new Vector();

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      Object ob = null;
      while (rs.next())
      {
        GeneralBillHeaderVO headVO = new GeneralBillHeaderVO();
        for (int i = 0; i < strHeadItems.length; i++) {
          ob = rs.getObject(i + 1);

          if ((ob == null) || (ob.toString().trim().equals(""))) {
            headVO.setAttributeValue(strHeadItems[i], null);
          }
          else if ((strHeadItems[i].equals("dbilldate")) || (strHeadItems[i].equals("dauditdate")) || 
            (strHeadItems[i].equals("daccountdate")))
          {
            headVO.setAttributeValue(strHeadItems[i], new UFDate(ob.toString().trim(), false));
          } else if (ob.getClass().equals(String.class))
            headVO.setAttributeValue(strHeadItems[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            headVO.setAttributeValue(strHeadItems[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            headVO.setAttributeValue(strHeadItems[i], new UFDouble(ob.toString()));
          }
        }
        hVector.addElement(headVO);

        int recordPos = strHeadItems.length + 1;
        GeneralBillItemVO bodyVO = new GeneralBillItemVO();
        for (int i = 0; i < strBodyItems.length; i++) {
          ob = rs.getObject(recordPos + i);

          if ((ob == null) || (ob.toString().trim().equals(""))) {
            bodyVO.setAttributeValue(strBodyItems[i], null);
          }
          else if ((strBodyItems[i].equals("dvalidate")) || (strBodyItems[i].equals("dbizdate")) || 
            (strBodyItems[i].equals("dstandbydate")))
          {
            bodyVO.setAttributeValue(strBodyItems[i], new UFDate(ob.toString().trim(), false));
          } else if (ob.getClass().equals(String.class))
            bodyVO.setAttributeValue(strBodyItems[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            bodyVO.setAttributeValue(strBodyItems[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            bodyVO.setAttributeValue(strBodyItems[i], new UFDouble(ob.toString()));
          }
        }
        bVector.addElement(bodyVO);

        GeneralBb3VO bb3VO = new GeneralBb3VO();
        recordPos += strBodyItems.length;
        for (int i = 0; i < strBb3Items.length; i++) {
          ob = rs.getObject(recordPos + i);

          if ((ob == null) || (ob.toString().trim().equals("")))
            bb3VO.setAttributeValue(strBb3Items[i], null);
          else if (ob.getClass().equals(String.class))
            bb3VO.setAttributeValue(strBb3Items[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            bb3VO.setAttributeValue(strBb3Items[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            bb3VO.setAttributeValue(strBb3Items[i], new UFDouble(ob.toString()));
          }
        }
        bb3Vector.addElement(bb3VO);
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findGenenelVOsByCondsMy", new Object[] { strIdWhere });

    if (hVector.size() == 0) {
      return null;
    }

    return new Object[] { hVector, bVector, bb3Vector };
  }

  public UFDouble getTotalNativeSummny(InvoiceVO invVO)
    throws SQLException
  {
    if (invVO == null) {
      return VariableConst.ZERO;
    }

    UFDouble dSummny = VariableConst.ZERO;
    InvoiceItemVO[] voaItem = invVO.getBodyVO();
    int iLen = voaItem.length;
    for (int i = 0; i < iLen; i++)
    {
      if (voaItem[i].getStatus() == 3)
      {
        continue;
      }
      UFDouble dCurSummny = voaItem[i].getNsummny();
      dSummny = dSummny.add(dCurSummny == null ? VariableConst.ZERO : dCurSummny);
    }
    return dSummny;
  }

  public UFBoolean isSettleFinished(InvoiceVO invVO)
    throws Exception
  {
    if ((invVO == null) || (invVO.getHeadVO() == null)) {
      return new UFBoolean(false);
    }

    InvoiceItemVO[] voaItem = invVO.getBodyVO();
    int iLen = voaItem.length;
    for (int i = 0; i < iLen; i++)
    {
      if (voaItem[i].getStatus() == 3)
      {
        continue;
      }
      UFDouble dAccumSetMny = voaItem[i].getNaccumsettmny() == null ? VariableConst.ZERO : voaItem[i].getNaccumsettmny();
      UFDouble dMny = voaItem[i].getNmoney() == null ? VariableConst.ZERO : voaItem[i].getNmoney();
      if (dAccumSetMny.compareTo(dMny) != 0) {
        return new UFBoolean(false);
      }
    }

    return new UFBoolean(true);
  }

  public void updateIbillStatusAndApproveForHID(String hID, int ibillstatus, String approveID, String approveDate)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateIBillStatusForHID", new Object[] { hID });

    String sql = "update po_invoice set ibillstatus = " + ibillstatus + ",";
    if (approveID == null)
      sql = sql + "cauditpsn=null,";
    else {
      sql = sql + "cauditpsn='" + approveID + "',";
    }
    if (approveDate == null)
      sql = sql + "dauditdate=null,taudittime=null";
    else {
      sql = sql + "dauditdate='" + approveDate + "',taudittime='" + new UFDateTime(new Date()).toString() + "'";
    }
    sql = sql + " WHERE cinvoiceid = '" + hID + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateIBillStatusForHID", new Object[] { hID });
  }

  public UFBoolean checkCalbAndInv(InvoiceVO vo)
    throws SQLException, BusinessException
  {
    InvoiceHeaderVO head = (InvoiceHeaderVO)vo.getParentVO();
    InvoiceItemVO[] items = (InvoiceItemVO[])vo.getChildrenVO();

    String sstoreOrg = head.getCstoreorganization().trim();

    Vector vBaseids = new Vector();
    boolean bflag = true;

    String sql = "select pk_invbasdoc, pk_calbody from bd_produce where dr = 0 and (1< 0";

    for (int i = 0; i < items.length; i++)
    {
      if (items[i].getStatus() == 3) {
        continue;
      }
      if (vBaseids.indexOf(items[i].getCbaseid()) < 0) {
        vBaseids.addElement(items[i].getCbaseid());
        sql = sql + " or pk_invbasdoc = '" + items[i].getCbaseid() + "'";
      }
    }
    sql = sql + ")";

    boolean[] flag = new boolean[vBaseids.size()];
    for (int i = 0; i < flag.length; i++) {
      flag[i] = false;
    }

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();
      String sorg = null;
      String cbaseid = null;
      int i = 0;
      while (rs.next()) {
        i++;
        cbaseid = rs.getString(1);
        sorg = rs.getString(2);
        if ((sorg == null) || (sorg.trim().length() == 0))
          continue;
        if (sorg.trim().equals(sstoreOrg)) {
          int index = vBaseids.indexOf(cbaseid);

          flag[index] = true;
        }

      }

      rs.close();
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException1) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException2)
      {
      }
    }
    for (int j = 0; j < flag.length; j++)
    {
      if (!flag[j])
        bflag = false;
    }
    return new UFBoolean(bflag);
  }

  public void deleteBIdArrayForHIdArray(String[] saInvoiceid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateDrForBIDArray(InvoiceItemVO [], int)", new Object[] { saInvoiceid });

    StringBuffer sbufSQL = 
      new StringBuffer("UPDATE po_invoice_b SET dr = 1 WHERE cinvoiceid IN (");
    int iLen = saInvoiceid.length;
    for (int i = 0; i < iLen; i++) {
      sbufSQL.append("'");
      sbufSQL.append(saInvoiceid[i]);
      sbufSQL.append("'");
      if (i < iLen - 1)
        sbufSQL.append(",");
      else {
        sbufSQL.append(")");
      }
    }

    Connection con = null;
    Statement stmt = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      stmt.executeUpdate(sbufSQL.toString());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateDrForBIDArray(InvoiceItemVO [], int)", new Object[] { saInvoiceid });
  }

  public void deleteHIdArray(String[] saInvoiceId)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateDrForHIDArray(String [], int)", new Object[] { saInvoiceId });

    StringBuffer sbufSQL = 
      new StringBuffer("UPDATE po_invoice SET dr = 1 WHERE cinvoiceid IN (");
    int iLen = saInvoiceId.length;
    for (int i = 0; i < iLen; i++) {
      sbufSQL.append("'");
      sbufSQL.append(saInvoiceId[i]);
      sbufSQL.append("'");
      if (i < iLen - 1)
        sbufSQL.append(",");
      else {
        sbufSQL.append(")");
      }
    }

    Connection con = null;
    Statement stmt = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      stmt.executeUpdate(sbufSQL.toString());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateDrForHIDArray(String [], int)", new Object[] { saInvoiceId });
  }

  public HashMap findAccumInvoiceNumByScOrderBPKMy(String[] sKeys)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findAccumInvoiceNumByScOrderBPKMy", new Object[] { sKeys });

    HashMap hRet = new HashMap();
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT corder_bid,naccuminvoicenum FROM sc_order_b WHERE dr = 0 AND ");

    sql.append(" corder_bid in ");
    String strIdsSet = "";
    try {
      TempTableDMO tmpTblDmo = new TempTableDMO();
      strIdsSet = tmpTblDmo.insertTempTable(sKeys, "t_pu_pi_001", "pk_pu");
      if ((strIdsSet == null) || (strIdsSet.trim().length() == 0))
        strIdsSet = "('TempTableDMOError')";
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }
    sql.append(strIdsSet + " ");

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String sCorder_bId = rs.getString("corder_bid");
        BigDecimal ob = rs.getBigDecimal("naccuminvoicenum");
        if (ob != null) {
          hRet.put(sCorder_bId, new UFDouble(ob.doubleValue()));
        }
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException2)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findAccumInvoiceNumByScOrderBPKMy", new Object[] { sKeys });

    return hRet;
  }

  public HashMap findInvoiceNum(String[] sKeys)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceNum", new Object[] { sKeys });

    HashMap hRet = new HashMap();
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT cinvoice_bid,ninvoicenum FROM po_invoice_b WHERE dr = 0 AND ");

    sql.append(" cinvoice_bid in ");
    String strIdsSet = "";
    try {
      TempTableDMO tmpTblDmo = new TempTableDMO();
      strIdsSet = tmpTblDmo.insertTempTable(sKeys, "t_pu_pi_003", "pk_pu");
      if ((strIdsSet == null) || (strIdsSet.trim().length() == 0))
        strIdsSet = "('TempTableDMOError')";
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }
    sql.append(strIdsSet + " ");

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String sCinvoice_bId = rs.getString("cinvoice_bid");
        BigDecimal ob = rs.getBigDecimal("ninvoicenum");
        if (ob != null) {
          hRet.put(sCinvoice_bId, new UFDouble(ob.doubleValue()));
        }
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException2)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceNum", new Object[] { sKeys });

    return hRet;
  }

  public HashMap findInvoiceNumByInvoiceBPKMy(String[] sKeys)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceNumByInvoiceBPK", new Object[] { sKeys });

    HashMap hRet = new HashMap();
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT cinvoice_bid,ninvoicenum FROM po_invoice_b WHERE dr = 0 AND ");

    sql.append(" cinvoice_bid in ");
    String strIdsSet = "";
    try {
      TempTableDMO tmpTblDmo = new TempTableDMO();
      strIdsSet = tmpTblDmo.insertTempTable(sKeys, "t_pu_pi_002", "pk_pu");
      if ((strIdsSet == null) || (strIdsSet.trim().length() == 0))
        strIdsSet = "('TempTableDMOError')";
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }
    sql.append(strIdsSet + " ");

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String sCinvoice_bId = rs.getString("cinvoice_bid");
        BigDecimal ob = rs.getBigDecimal("ninvoicenum");
        if (ob != null) {
          hRet.put(sCinvoice_bId, new UFDouble(ob.doubleValue()));
        }
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException2)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceNumByInvoiceBPK", new Object[] { sKeys });

    return hRet;
  }

  public HashMap findInvoiceNumByInvoiceBPKMyForIc2In(String[] sKeys)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceNumByInvoiceBPK", new Object[] { sKeys });

    HashMap hRet = new HashMap();
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT cinvoice_bid,ninvoicenum,nreasonwastenum FROM po_invoice_b WHERE dr = 0 AND ");

    sql.append(" cinvoice_bid in ");
    String strIdsSet = "";
    try {
      TempTableDMO tmpTblDmo = new TempTableDMO();
      strIdsSet = tmpTblDmo.insertTempTable(sKeys, "t_pu_pi_002", "pk_pu");
      if ((strIdsSet == null) || (strIdsSet.trim().length() == 0))
        strIdsSet = "('TempTableDMOError')";
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }
    sql.append(strIdsSet + " ");

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String sCinvoice_bId = rs.getString("cinvoice_bid");
        UFDouble d1 = new UFDouble(0); UFDouble d2 = new UFDouble(0);
        Object ob = rs.getObject("ninvoicenum");
        if (ob != null) d1 = new UFDouble(ob.toString());
        ob = rs.getObject("nreasonwastenum");
        if (ob != null) d2 = new UFDouble(ob.toString());
        hRet.put(sCinvoice_bId, new UFDouble[] { d1, d2 });
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException2)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceNumByInvoiceBPK", new Object[] { sKeys });

    return hRet;
  }

  public InvoiceVO[] findInvoiceVOsByAllItems(String strFromWhere)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceVOsByCondsMy", new Object[] { strFromWhere });

    StringBuffer sbufHeadSQL = new StringBuffer("SELECT distinct ");
    String[] saHField = InvoiceHeaderVO.getDbFields();
    int iHLen = saHField.length;
    for (int i = 0; i < iHLen - 1; i++) {
      sbufHeadSQL.append("po_invoice.");
      sbufHeadSQL.append(saHField[i]);
      sbufHeadSQL.append(",");
    }
    sbufHeadSQL.append("po_invoice." + saHField[(iHLen - 1)] + " " + strFromWhere);

    StringBuffer sbufBodySQL = new StringBuffer("SELECT ");
    String[] saBField = InvoiceItemVO.getDbFields();
    int iBLen = saBField.length;
    for (int i = 0; i < iBLen - 1; i++) {
      sbufBodySQL.append("po_invoice_b.");
      sbufBodySQL.append(saBField[i]);
      sbufBodySQL.append(",");
    }
    sbufBodySQL.append("po_invoice_b." + saBField[(iBLen - 1)]);

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Hashtable voTable1 = new Hashtable();
    try {
      con = getConnection();
      stmt = con.createStatement();
      Vector headIDsV = new Vector();

      rs = stmt.executeQuery(sbufHeadSQL.toString());

      while (rs.next()) {
        Object ob = rs.getObject(1);
        InvoiceHeaderVO headVO = new InvoiceHeaderVO();
        headVO.setPrimaryKey(ob.toString());
        for (int i = 1; i < saHField.length; i++) {
          ob = rs.getObject(i + 1);
          if ((ob == null) || (ob.toString().trim().equals("")))
            headVO.setAttributeValue(saHField[i], null);
          else if (ob.getClass().equals(String.class))
            headVO.setAttributeValue(saHField[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            headVO.setAttributeValue(saHField[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            if (new UFDouble(ob.toString()).doubleValue() == 0.0D)
              headVO.setAttributeValue(saHField[i], null);
            else {
              headVO.setAttributeValue(saHField[i], new UFDouble(ob.toString()));
            }
          }
        }
        Vector voVEC = new Vector();
        voVEC.addElement(headVO);
        voTable1.put(headVO.getCinvoiceid(), voVEC);

        headIDsV.addElement(headVO.getCinvoiceid());
      }

      if (voTable1.size() > 0)
      {
        String[] invoiceids = new String[headIDsV.size()];
        headIDsV.copyInto(invoiceids);

        sbufBodySQL.append(" FROM po_invoice_b where po_invoice_b.dr=0");
        for (int k = 0; k < headIDsV.size(); k++) {
          rs = stmt.executeQuery(sbufBodySQL.toString() + " and (po_invoice_b.cinvoiceid in('" + headIDsV.get(k) + "'))");

          while (rs.next())
          {
            InvoiceItemVO bodyVO = new InvoiceItemVO();
            for (int i = 0; i < saBField.length; i++) {
              Object ob = rs.getObject(i + 1);
              if ((ob == null) || (ob.toString().trim().equals("")))
                bodyVO.setAttributeValue(saBField[i], null);
              else if (ob.getClass().equals(String.class))
                bodyVO.setAttributeValue(saBField[i], ob.toString().trim());
              else if (ob.getClass().equals(Integer.class))
                bodyVO.setAttributeValue(saBField[i], ob);
              else if (ob.getClass().equals(BigDecimal.class)) {
                if (new UFDouble(ob.toString()).doubleValue() == 0.0D)
                  bodyVO.setAttributeValue(saBField[i], null);
                else {
                  bodyVO.setAttributeValue(saBField[i], new UFDouble(ob.toString()));
                }

              }

            }

            ((Vector)voTable1.get(bodyVO.getCinvoiceid())).addElement(bodyVO);
          }
        }

      }

      rs.close();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }

    InvoiceVO[] allVOs1 = (InvoiceVO[])null;
    if (voTable1.size() > 0) {
      allVOs1 = new InvoiceVO[voTable1.size()];
      Enumeration elems = voTable1.elements();
      int i = 0;
      while (elems.hasMoreElements()) {
        Vector vec = (Vector)elems.nextElement();
        int len = vec.size() - 1;

        InvoiceHeaderVO headVO = (InvoiceHeaderVO)vec.elementAt(0);
        InvoiceItemVO[] itemVOs = new InvoiceItemVO[len];
        vec.removeElementAt(0);
        vec.copyInto(itemVOs);

        headVO.setCcurrencytypeid(itemVOs[0].getCcurrencytypeid());
        headVO.setNexchangeotobrate(itemVOs[0].getNexchangeotobrate());
        headVO.setNexchangeotoarate(itemVOs[0].getNexchangeotoarate());

        allVOs1[i] = new InvoiceVO(len);
        allVOs1[i].setParentVO(headVO);
        allVOs1[i].setChildrenVO(itemVOs);

        i++;
      }

    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceVOsByCondsMy", new Object[] { strFromWhere });

    return allVOs1;
  }

  public InvoiceVO[] findInvoiceVOsByNoneItems(String strFromWhere)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceVOsByCondsMy", new Object[] { strFromWhere });

    StringBuffer sbufHeadSQL = new StringBuffer("SELECT distinct ");
    String[] saHField = InvoiceHeaderVO.getDbFields();
    int iHLen = saHField.length;
    for (int i = 0; i < iHLen; i++)
    {
      sbufHeadSQL.append("po_invoice.");
      sbufHeadSQL.append(saHField[i]);
      if (i < iHLen - 1)
        sbufHeadSQL.append(",");
      else {
        sbufHeadSQL.append(" ");
      }

    }

    sbufHeadSQL.append(strFromWhere);

    StringBuffer sbufBodySQL = new StringBuffer("SELECT ");
    String[] saBField = InvoiceItemVO.getDbFields();
    int iBLen = saBField.length;
    for (int i = 0; i < iBLen - 1; i++) {
      sbufBodySQL.append("po_invoice_b.");
      sbufBodySQL.append(saBField[i]);
      sbufBodySQL.append(",");
    }
    sbufBodySQL.append("po_invoice_b." + saBField[(iBLen - 1)]);

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sbufHeadSQL.toString());

      while (rs.next()) {
        Object ob = rs.getObject(1);
        InvoiceHeaderVO headVO = new InvoiceHeaderVO();
        headVO.setPrimaryKey(ob.toString());
        for (int i = 1; i < saHField.length; i++) {         
          ob = rs.getObject(i + 1);
          if ((ob == null) || (ob.toString().trim().equals("")))
            headVO.setAttributeValue(saHField[i], null);
          else if (ob.getClass().equals(String.class))
            headVO.setAttributeValue(saHField[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            headVO.setAttributeValue(saHField[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            if (new UFDouble(ob.toString()).doubleValue() == 0.0D)
              headVO.setAttributeValue(saHField[i], null);
            else {
              headVO.setAttributeValue(saHField[i], new UFDouble(ob.toString()));
            }
          }
        }

        InvoiceVO vo = new InvoiceVO();
        vo.setParentVO(headVO);
        v.addElement(vo);
      }

      rs.close();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }

      }
      catch (Exception localException1)
      {
      }

    }

    InvoiceVO[] vos = (InvoiceVO[])null;
    if (v.size() > 0) {
      vos = new InvoiceVO[v.size()];
      v.copyInto(vos);
    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "findInvoiceVOsByCondsMy", new Object[] { strFromWhere });

    return vos;
  }

  public HashMap findItemsByPrimaryKeys(String[] keys)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findItemsByPrimaryKeys", new Object[] { keys });

    if ((keys == null) || (keys.length == 0)) {
      return null;
    }
    HashMap hRet = new HashMap();

    String wherePart = " and (cinvoiceid = '" + keys[0] + "'";
    for (int i = 1; i < keys.length; i++) {
      wherePart = wherePart + " or cinvoiceid = '" + keys[i] + "'";
    }
    wherePart = wherePart + ")";

    StringBuffer sbufBodySQL = new StringBuffer("SELECT ");
    String[] saBField = InvoiceItemVO.getDbFields();
    int iBLen = saBField.length;
    for (int i = 0; i < iBLen - 1; i++) {
      sbufBodySQL.append("po_invoice_b.");
      sbufBodySQL.append(saBField[i]);
      sbufBodySQL.append(",");
    }
    sbufBodySQL.append("po_invoice_b." + saBField[(iBLen - 1)] + " ");
    sbufBodySQL.append("from po_invoice_b where dr = 0 " + wherePart);

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbufBodySQL.toString());
      ResultSet rs = stmt.executeQuery();
      String sCinvoiceId = "";

      while (rs.next())
      {
        InvoiceItemVO bodyVO = new InvoiceItemVO();
        for (int i = 0; i < saBField.length; i++) {
          Object ob = rs.getObject(i + 1);
          if ((ob == null) || (ob.toString().trim().equals("")))
            bodyVO.setAttributeValue(saBField[i], null);
          else if (ob.getClass().equals(String.class))
            bodyVO.setAttributeValue(saBField[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            bodyVO.setAttributeValue(saBField[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            if (new UFDouble(ob.toString()).doubleValue() == 0.0D)
              bodyVO.setAttributeValue(saBField[i], null);
            else
              bodyVO.setAttributeValue(saBField[i], new UFDouble(ob.toString()));
          }
        }
        if (sCinvoiceId.trim().length() <= 0) {
          ArrayList arr = new ArrayList();
          arr.add(bodyVO);
          sCinvoiceId = bodyVO.getCinvoiceid();
          hRet.put(sCinvoiceId, arr);
        } else if (hRet.containsKey(bodyVO.getCinvoiceid())) {
          ArrayList arr = (ArrayList)hRet.get(bodyVO.getCinvoiceid());
          arr.add(bodyVO);
        } else {
          ArrayList arr = new ArrayList();
          arr.add(bodyVO);
          sCinvoiceId = bodyVO.getCinvoiceid();
          hRet.put(sCinvoiceId, arr);
        }
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    HashMap hResult = new HashMap();
    InvoiceItemVO[] invoiceItems = (InvoiceItemVO[])null;
    for (int i = 0; i < keys.length; i++) {
      ArrayList arr = (ArrayList)hRet.get(keys[i]);
      if ((arr == null) || (arr.size() <= 0))
        continue;
      invoiceItems = new InvoiceItemVO[arr.size()];
      arr.toArray(invoiceItems);
      hResult.put(keys[i], invoiceItems);
    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "findItemsByPrimaryKeys", new Object[] { keys });

    return hResult;
  }

  public InvoiceItemVO[] findItemsForHeaders(String[] keys)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "findItemsForHeaders", new Object[] { keys });

    if ((keys == null) || (keys.length == 0)) {
      return null;
    }

    String wherePart = " and (cinvoiceid = '" + keys[0] + "'";
    for (int i = 1; i < keys.length; i++) {
      wherePart = wherePart + " or cinvoiceid = '" + keys[i] + "'";
    }
    wherePart = wherePart + ")";

    StringBuffer sbufBodySQL = new StringBuffer("SELECT ");
    String[] saBField = InvoiceItemVO.getDbFields();
    int iBLen = saBField.length;
    for (int i = 0; i < iBLen - 1; i++) {
      sbufBodySQL.append("po_invoice_b.");
      sbufBodySQL.append(saBField[i]);
      sbufBodySQL.append(",");
    }
    sbufBodySQL.append("po_invoice_b." + saBField[(iBLen - 1)] + " ");
    sbufBodySQL.append("from po_invoice_b where dr = 0 " + wherePart);

    InvoiceItemVO[] invoiceItems = (InvoiceItemVO[])null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbufBodySQL.toString());
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        InvoiceItemVO bodyVO = new InvoiceItemVO();
        for (int i = 0; i < saBField.length; i++) {
          Object ob = rs.getObject(i + 1);
          if ((ob == null) || (ob.toString().trim().equals("")))
            bodyVO.setAttributeValue(saBField[i], null);
          else if (ob.getClass().equals(String.class))
            bodyVO.setAttributeValue(saBField[i], ob.toString().trim());
          else if (ob.getClass().equals(Integer.class))
            bodyVO.setAttributeValue(saBField[i], ob);
          else if (ob.getClass().equals(BigDecimal.class)) {
            if (new UFDouble(ob.toString()).doubleValue() == 0.0D)
              bodyVO.setAttributeValue(saBField[i], null);
            else {
              bodyVO.setAttributeValue(saBField[i], new UFDouble(ob.toString()));
            }

          }

        }

        v.add(bodyVO);
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception localException1) {
      }
    }
    invoiceItems = new InvoiceItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(invoiceItems);
    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "findItemsForHeaders", new Object[] { keys });

    return invoiceItems;
  }

  private PoCloseImpl getBean_PoClose()
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getBean_PoClose()";

    PoCloseImpl bean = null;
    try {
      bean = new PoCloseImpl();
    } catch (Exception e1) {
      reportException(e1);
      PubDMO.throwBusinessException(sMethodName, e1);
    }
    return bean;
  }

  public HashMap getPlanPricesForPr(String[] sBaseIds, String sStoreOrgId, String sCorpId)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "getPlanPriceForPr", new Object[] { sBaseIds, sStoreOrgId, sCorpId });

    if ((sBaseIds == null) || (sBaseIds.length == 0) || (sStoreOrgId == null)) {
      return null;
    }
    StringBuffer sbSql = new StringBuffer();
    sbSql.append("select pk_invbasdoc,jhj from bd_produce where pk_invbasdoc in ");

    String strIdsSet = "";
    try {
      TempTableDMO tmpTblDmo = new TempTableDMO();
      strIdsSet = tmpTblDmo.insertTempTable(sBaseIds, "t_pu_pi_010", "pk_pu");
      if ((strIdsSet == null) || (strIdsSet.trim().length() == 0))
        strIdsSet = "('TempTableDMOError')";
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }
    sbSql.append(strIdsSet + " ");

    sbSql.append(" and pk_calbody='");
    sbSql.append(sStoreOrgId);
    sbSql.append("'");
    sbSql.append(" and pk_corp='");
    sbSql.append(sCorpId);
    sbSql.append("'");

    HashMap hResult = new HashMap();

    String sPk_invbasedoc = null;

    UFDouble uPlanPrice = null;

    Object oTemp = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();
      while (rs.next())
      {
        oTemp = rs.getObject("pk_invbasdoc");
        if (oTemp != null) {
          sPk_invbasedoc = oTemp.toString();

          oTemp = rs.getObject("jhj");
          if (oTemp != null)
            uPlanPrice = new UFDouble(oTemp.toString());
          else
            uPlanPrice = null;
          hResult.put(sPk_invbasedoc, uPlanPrice);
        }
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception localException1) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException2) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException3)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "getPlanPriceForPr", new Object[] { sBaseIds, sStoreOrgId, sCorpId });

    return hResult;
  }

  public HashMap getPlanPricesFrmInvMan(String[] sBaseIds, String sCorpId)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "getPlanPriceForPr", new Object[] { sBaseIds, sCorpId });

    if ((sBaseIds == null) || (sBaseIds.length == 0)) {
      return null;
    }
    StringBuffer sbSql = new StringBuffer();
    sbSql.append("select pk_invbasdoc,pk_invmandoc,planprice from bd_invmandoc where pk_invbasdoc in ");

    String strIdsSet = "";
    try {
      TempTableDMO tmpTblDmo = new TempTableDMO();
      strIdsSet = tmpTblDmo.insertTempTable(sBaseIds, "t_pu_pi_009", "pk_pu");
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

    String sPk_invbasedoc = null;

    UFDouble uPlanPrice = null;

    Object oTemp = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();
      while (rs.next())
      {
        oTemp = rs.getObject("pk_invbasdoc");
        if (oTemp != null) {
          sPk_invbasedoc = oTemp.toString();

          oTemp = rs.getObject("planprice");
          if (oTemp != null)
            uPlanPrice = new UFDouble(oTemp.toString());
          else
            uPlanPrice = null;
          hResult.put(sPk_invbasedoc, uPlanPrice);
        }
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception localException1) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException2) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException3)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "getPlanPriceForPr", new Object[] { sBaseIds, sCorpId });

    return hResult;
  }

  public ParaPoToIcLendRewriteVO[] getPuSignNums()
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "getPuSignNums", new Object[0]);

    ParaPoToIcLendRewriteVO[] vos = (ParaPoToIcLendRewriteVO[])null;
    ParaPoToIcLendRewriteVO vo = null;
    ArrayList arrResult = new ArrayList();
    StringBuffer sbSql = new StringBuffer();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      sbSql.append("select sum(ninvoicenum),cupsourcebillrowid from po_invoice_b where cupsourcebillrowid in");
      sbSql.append("(select cgeneralbid from ic_general_b where dr = 0) and dr = 0 group by cupsourcebillrowid");
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();
      while (rs.next()) {
        vo = new ParaPoToIcLendRewriteVO();
        Object o = rs.getObject(1);
        if (o != null)
          vo.setDSubNum(new UFDouble(o.toString()));
        vo.setCRowID(rs.getString(2));
        arrResult.add(vo);
      }
      int size = arrResult.size();
      if (size > 0) {
        vos = new ParaPoToIcLendRewriteVO[size];
        arrResult.toArray(vos);
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception localException) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException2)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "getPuSignNums", new Object[0]);

    return vos;
  }

  public String[] insertItems(InvoiceItemVO[] invoiceItems)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "insertItems", new Object[] { invoiceItems });

    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO po_invoice_b(");
    sql.append("cinvoice_bid,cinvoiceid,pk_corp,cusedeptid,corder_bid,corderid,csourcebilltype,csourcebillid,csourcebillrowid,cupsourcebilltype,");
    sql.append("cupsourcebillid,cupsourcebillrowid,cmangid,cbaseid,ninvoicenum,naccumsettnum,idiscounttaxtype,ntaxrate,ccurrencytypeid,noriginalcurprice,");
    sql.append("noriginaltaxmny,noriginalcurmny,noriginalsummny,noriginalpaymentmny,nexchangeotobrate,nmoney,ntaxmny,nsummny,npaymentmny,naccumsettmny,");
    sql.append("nexchangeotoarate,nassistcurmny,nassisttaxmny,nassistsummny,nassistpaymny,nassistsettmny,cprojectid,cprojectphaseid,vmemo,vfree1,");
    sql.append("vfree2,vfree3,vfree4,vfree5,vdef1,vdef2,vdef3,vdef4,vdef5,vdef6,");
    sql.append("vdef7,vdef8,vdef9,vdef10,vdef11, vdef12, vdef13, vdef14, vdef15, vdef16,");
    sql.append("vdef17, vdef18, vdef19,vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,pk_defdoc5,pk_defdoc6,");
    sql.append("pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11,pk_defdoc12,pk_defdoc13,pk_defdoc14,pk_defdoc15,pk_defdoc16,");
    sql.append("pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20,dr,cwarehouseid,vproducenum,crowno,norgnettaxprice,nreasonwastenum,pk_upsrccorp,b_cjje1,b_cjje2,b_cjje3)");
    sql.append(" values(");
    sql.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \t\t?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
    sql.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \t\t?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
    sql.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \t\t?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
    sql.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \t\t?, ?, ?, ?, ?, ?, ?, ?, ?, ?,");
    sql.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)");

    String[] keys = (String[])null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = prepareStatement(con, sql.toString());
      InvoiceItemVO invoiceItem = null;

      if (invoiceItems != null) {
        keys = new String[invoiceItems.length];
      }

      for (int i = 0; (invoiceItems != null) && (i < invoiceItems.length); i++)
      {
        invoiceItem = invoiceItems[i];

        String key = getOID(invoiceItem.getPk_corp());

        invoiceItem.setPrimaryKey(key);

        keys[i] = key;

        stmt.setString(1, key);

        if (invoiceItem.getCinvoiceid() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, invoiceItem.getCinvoiceid());
        }
        if (invoiceItem.getPk_corp() == null)
          stmt.setNull(3, 1);
        else {
          stmt.setString(3, invoiceItem.getPk_corp());
        }
        if (invoiceItem.getCusedeptid() == null)
          stmt.setNull(4, 1);
        else {
          stmt.setString(4, invoiceItem.getCusedeptid());
        }
        if (invoiceItem.getCorder_bid() == null)
          stmt.setNull(5, 1);
        else {
          stmt.setString(5, invoiceItem.getCorder_bid());
        }
        if (invoiceItem.getCorderid() == null)
          stmt.setNull(6, 1);
        else {
          stmt.setString(6, invoiceItem.getCorderid());
        }
        if (invoiceItem.getCsourcebilltype() == null)
          stmt.setNull(7, 1);
        else {
          stmt.setString(7, invoiceItem.getCsourcebilltype());
        }
        if (invoiceItem.getCsourcebillid() == null)
          stmt.setNull(8, 1);
        else {
          stmt.setString(8, invoiceItem.getCsourcebillid());
        }
        if (invoiceItem.getCsourcebillrowid() == null)
          stmt.setNull(9, 1);
        else {
          stmt.setString(9, invoiceItem.getCsourcebillrowid());
        }
        if (invoiceItem.getCupsourcebilltype() == null)
          stmt.setNull(10, 1);
        else {
          stmt.setString(10, invoiceItem.getCupsourcebilltype());
        }
        if (invoiceItem.getCupsourcebillid() == null)
          stmt.setNull(11, 1);
        else {
          stmt.setString(11, invoiceItem.getCupsourcebillid());
        }
        if (invoiceItem.getCupsourcebillrowid() == null)
          stmt.setNull(12, 1);
        else {
          stmt.setString(12, invoiceItem.getCupsourcebillrowid());
        }
        if (invoiceItem.getCmangid() == null)
          stmt.setNull(13, 1);
        else {
          stmt.setString(13, invoiceItem.getCmangid());
        }
        if (invoiceItem.getCbaseid() == null)
          stmt.setNull(14, 1);
        else {
          stmt.setString(14, invoiceItem.getCbaseid());
        }
        if (invoiceItem.getNinvoicenum() == null)
          stmt.setNull(15, 4);
        else {
          stmt.setBigDecimal(15, 
            invoiceItem.getNinvoicenum().toBigDecimal());
        }
        if (invoiceItem.getNaccumsettnum() == null)
          stmt.setNull(16, 4);
        else {
          stmt.setBigDecimal(16, 
            invoiceItem.getNaccumsettnum().toBigDecimal());
        }
        if (invoiceItem.getIdiscounttaxtype() == null)
          stmt.setNull(17, 4);
        else {
          stmt.setInt(17, 
            invoiceItem.getIdiscounttaxtype().intValue());
        }
        if (invoiceItem.getNtaxrate() == null)
          stmt.setNull(18, 4);
        else {
          stmt.setBigDecimal(18, 
            invoiceItem.getNtaxrate().toBigDecimal());
        }
        if (invoiceItem.getCcurrencytypeid() == null)
          stmt.setNull(19, 1);
        else {
          stmt.setString(19, invoiceItem.getCcurrencytypeid());
        }
        if (invoiceItem.getNoriginalcurprice() == null)
          stmt.setNull(20, 4);
        else {
          stmt.setBigDecimal(20, 
            invoiceItem.getNoriginalcurprice().toBigDecimal());
        }
        if (invoiceItem.getNoriginaltaxmny() == null)
          stmt.setNull(21, 4);
        else {
          stmt.setBigDecimal(21, 
            invoiceItem.getNoriginaltaxmny().toBigDecimal());
        }
        if (invoiceItem.getNoriginalcurmny() == null)
          stmt.setNull(22, 4);
        else {
          stmt.setBigDecimal(22, 
            invoiceItem.getNoriginalcurmny().toBigDecimal());
        }
        if (invoiceItem.getNoriginalsummny() == null)
          stmt.setNull(23, 4);
        else {
          stmt.setBigDecimal(23, 
            invoiceItem.getNoriginalsummny().toBigDecimal());
        }
        if (invoiceItem.getNoriginalpaymentmny() == null)
          stmt.setNull(24, 4);
        else {
          stmt.setBigDecimal(24, 
            invoiceItem.getNoriginalpaymentmny().toBigDecimal());
        }
        if (invoiceItem.getNexchangeotobrate() == null)
          stmt.setNull(25, 4);
        else {
          stmt.setBigDecimal(25, 
            invoiceItem.getNexchangeotobrate().toBigDecimal());
        }
        if (invoiceItem.getNmoney() == null)
          stmt.setNull(26, 4);
        else {
          stmt.setBigDecimal(26, 
            invoiceItem.getNmoney().toBigDecimal());
        }
        if (invoiceItem.getNtaxmny() == null)
          stmt.setNull(27, 4);
        else {
          stmt.setBigDecimal(27, 
            invoiceItem.getNtaxmny().toBigDecimal());
        }
        if (invoiceItem.getNsummny() == null)
          stmt.setNull(28, 4);
        else {
          stmt.setBigDecimal(28, 
            invoiceItem.getNsummny().toBigDecimal());
        }
        if (invoiceItem.getNpaymentmny() == null)
          stmt.setNull(29, 4);
        else {
          stmt.setBigDecimal(29, 
            invoiceItem.getNpaymentmny().toBigDecimal());
        }
        if (invoiceItem.getNaccumsettmny() == null)
          stmt.setNull(30, 4);
        else {
          stmt.setBigDecimal(30, 
            invoiceItem.getNaccumsettmny().toBigDecimal());
        }
        if (invoiceItem.getNexchangeotoarate() == null)
          stmt.setNull(31, 4);
        else {
          stmt.setBigDecimal(31, 
            invoiceItem.getNexchangeotoarate().toBigDecimal());
        }
        if (invoiceItem.getNassistcurmny() == null)
          stmt.setNull(32, 4);
        else {
          stmt.setBigDecimal(32, 
            invoiceItem.getNassistcurmny().toBigDecimal());
        }
        if (invoiceItem.getNassisttaxmny() == null)
          stmt.setNull(33, 4);
        else {
          stmt.setBigDecimal(33, 
            invoiceItem.getNassisttaxmny().toBigDecimal());
        }
        if (invoiceItem.getNassistsummny() == null)
          stmt.setNull(34, 4);
        else {
          stmt.setBigDecimal(34, 
            invoiceItem.getNassistsummny().toBigDecimal());
        }
        if (invoiceItem.getNassistpaymny() == null)
          stmt.setNull(35, 4);
        else {
          stmt.setBigDecimal(35, 
            invoiceItem.getNassistpaymny().toBigDecimal());
        }
        if (invoiceItem.getNassistsettmny() == null)
          stmt.setNull(36, 4);
        else {
          stmt.setBigDecimal(36, 
            invoiceItem.getNassistsettmny().toBigDecimal());
        }
        if (invoiceItem.getCprojectid() == null)
          stmt.setNull(37, 1);
        else {
          stmt.setString(37, invoiceItem.getCprojectid());
        }
        if (invoiceItem.getCprojectphaseid() == null)
          stmt.setNull(38, 1);
        else {
          stmt.setString(38, invoiceItem.getCprojectphaseid());
        }
        if (invoiceItem.getVmemo() == null)
          stmt.setNull(39, 1);
        else {
          stmt.setString(39, invoiceItem.getVmemo());
        }
        if (invoiceItem.getVfree1() == null)
          stmt.setNull(40, 1);
        else {
          stmt.setString(40, invoiceItem.getVfree1());
        }
        if (invoiceItem.getVfree2() == null)
          stmt.setNull(41, 1);
        else {
          stmt.setString(41, invoiceItem.getVfree2());
        }
        if (invoiceItem.getVfree3() == null)
          stmt.setNull(42, 1);
        else {
          stmt.setString(42, invoiceItem.getVfree3());
        }
        if (invoiceItem.getVfree4() == null)
          stmt.setNull(43, 1);
        else {
          stmt.setString(43, invoiceItem.getVfree4());
        }
        if (invoiceItem.getVfree5() == null)
          stmt.setNull(44, 1);
        else {
          stmt.setString(44, invoiceItem.getVfree5());
        }
        if (invoiceItem.getVdef1() == null)
          stmt.setNull(45, 1);
        else {
          stmt.setString(45, invoiceItem.getVdef1());
        }
        if (invoiceItem.getVdef2() == null)
          stmt.setNull(46, 1);
        else {
          stmt.setString(46, invoiceItem.getVdef2());
        }
        if (invoiceItem.getVdef3() == null)
          stmt.setNull(47, 1);
        else {
          stmt.setString(47, invoiceItem.getVdef3());
        }
        if (invoiceItem.getVdef4() == null)
          stmt.setNull(48, 1);
        else {
          stmt.setString(48, invoiceItem.getVdef4());
        }
        if (invoiceItem.getVdef5() == null)
          stmt.setNull(49, 1);
        else {
          stmt.setString(49, invoiceItem.getVdef5());
        }
        if (invoiceItem.getVdef6() == null)
          stmt.setNull(50, 1);
        else {
          stmt.setString(50, invoiceItem.getVdef6());
        }
        if (invoiceItem.getVdef7() == null)
          stmt.setNull(51, 1);
        else {
          stmt.setString(51, invoiceItem.getVdef7());
        }
        if (invoiceItem.getVdef8() == null)
          stmt.setNull(52, 1);
        else {
          stmt.setString(52, invoiceItem.getVdef8());
        }
        if (invoiceItem.getVdef9() == null)
          stmt.setNull(53, 1);
        else {
          stmt.setString(53, invoiceItem.getVdef9());
        }
        if (invoiceItem.getVdef10() == null)
          stmt.setNull(54, 1);
        else {
          stmt.setString(54, invoiceItem.getVdef10());
        }
        if (invoiceItem.getVdef11() == null)
          stmt.setNull(55, 1);
        else {
          stmt.setString(55, invoiceItem.getVdef11());
        }
        if (invoiceItem.getVdef12() == null)
          stmt.setNull(56, 1);
        else {
          stmt.setString(56, invoiceItem.getVdef12());
        }
        if (invoiceItem.getVdef13() == null)
          stmt.setNull(57, 1);
        else {
          stmt.setString(57, invoiceItem.getVdef13());
        }
        if (invoiceItem.getVdef14() == null)
          stmt.setNull(58, 1);
        else {
          stmt.setString(58, invoiceItem.getVdef14());
        }
        if (invoiceItem.getVdef15() == null)
          stmt.setNull(59, 1);
        else {
          stmt.setString(59, invoiceItem.getVdef15());
        }
        if (invoiceItem.getVdef16() == null)
          stmt.setNull(60, 1);
        else {
          stmt.setString(60, invoiceItem.getVdef16());
        }
        if (invoiceItem.getVdef17() == null)
          stmt.setNull(61, 1);
        else {
          stmt.setString(61, invoiceItem.getVdef17());
        }
        if (invoiceItem.getVdef18() == null)
          stmt.setNull(62, 1);
        else {
          stmt.setString(62, invoiceItem.getVdef18());
        }
        if (invoiceItem.getVdef19() == null)
          stmt.setNull(63, 1);
        else {
          stmt.setString(63, invoiceItem.getVdef19());
        }
        if (invoiceItem.getVdef20() == null)
          stmt.setNull(64, 1);
        else {
          stmt.setString(64, invoiceItem.getVdef20());
        }
        if (invoiceItem.getPKDefDoc1() == null)
          stmt.setNull(65, 1);
        else {
          stmt.setString(65, invoiceItem.getPKDefDoc1());
        }
        if (invoiceItem.getPKDefDoc2() == null)
          stmt.setNull(66, 1);
        else {
          stmt.setString(66, invoiceItem.getPKDefDoc2());
        }
        if (invoiceItem.getPKDefDoc3() == null)
          stmt.setNull(67, 1);
        else {
          stmt.setString(67, invoiceItem.getPKDefDoc3());
        }
        if (invoiceItem.getPKDefDoc4() == null)
          stmt.setNull(68, 1);
        else {
          stmt.setString(68, invoiceItem.getPKDefDoc4());
        }
        if (invoiceItem.getPKDefDoc5() == null)
          stmt.setNull(69, 1);
        else {
          stmt.setString(69, invoiceItem.getPKDefDoc5());
        }
        if (invoiceItem.getPKDefDoc6() == null)
          stmt.setNull(70, 1);
        else {
          stmt.setString(70, invoiceItem.getPKDefDoc6());
        }
        if (invoiceItem.getPKDefDoc7() == null)
          stmt.setNull(71, 1);
        else {
          stmt.setString(71, invoiceItem.getPKDefDoc7());
        }
        if (invoiceItem.getPKDefDoc8() == null)
          stmt.setNull(72, 1);
        else {
          stmt.setString(72, invoiceItem.getPKDefDoc8());
        }
        if (invoiceItem.getPKDefDoc9() == null)
          stmt.setNull(73, 1);
        else {
          stmt.setString(73, invoiceItem.getPKDefDoc9());
        }
        if (invoiceItem.getPKDefDoc10() == null)
          stmt.setNull(74, 1);
        else {
          stmt.setString(74, invoiceItem.getPKDefDoc10());
        }
        if (invoiceItem.getPKDefDoc11() == null)
          stmt.setNull(75, 1);
        else {
          stmt.setString(75, invoiceItem.getPKDefDoc11());
        }
        if (invoiceItem.getPKDefDoc12() == null)
          stmt.setNull(76, 1);
        else {
          stmt.setString(76, invoiceItem.getPKDefDoc12());
        }
        if (invoiceItem.getPKDefDoc13() == null)
          stmt.setNull(77, 1);
        else {
          stmt.setString(77, invoiceItem.getPKDefDoc13());
        }
        if (invoiceItem.getPKDefDoc14() == null)
          stmt.setNull(78, 1);
        else {
          stmt.setString(78, invoiceItem.getPKDefDoc14());
        }
        if (invoiceItem.getPKDefDoc15() == null)
          stmt.setNull(79, 1);
        else {
          stmt.setString(79, invoiceItem.getPKDefDoc15());
        }
        if (invoiceItem.getPKDefDoc16() == null)
          stmt.setNull(80, 1);
        else {
          stmt.setString(80, invoiceItem.getPKDefDoc16());
        }
        if (invoiceItem.getPKDefDoc17() == null)
          stmt.setNull(81, 1);
        else {
          stmt.setString(81, invoiceItem.getPKDefDoc17());
        }
        if (invoiceItem.getPKDefDoc18() == null)
          stmt.setNull(82, 1);
        else {
          stmt.setString(82, invoiceItem.getPKDefDoc18());
        }
        if (invoiceItem.getPKDefDoc19() == null)
          stmt.setNull(83, 1);
        else {
          stmt.setString(83, invoiceItem.getPKDefDoc19());
        }
        if (invoiceItem.getPKDefDoc20() == null)
          stmt.setNull(84, 1);
        else {
          stmt.setString(84, invoiceItem.getPKDefDoc20());
        }
        if (invoiceItem.getDr() == null)
          stmt.setNull(85, 4);
        else {
          stmt.setInt(85, invoiceItem.getDr().intValue());
        }

        if (invoiceItem.getCwarehouseid() == null)
          stmt.setString(86, "");
        else {
          stmt.setString(86, invoiceItem.getCwarehouseid());
        }
        if (invoiceItem.getVproducenum() == null)
          stmt.setNull(87, 1);
        else {
          stmt.setString(87, invoiceItem.getVproducenum());
        }
        if (invoiceItem.getCrowno() == null)
          stmt.setNull(88, 1);
        else {
          stmt.setString(88, invoiceItem.getCrowno());
        }
        if (invoiceItem.getNorgnettaxprice() == null)
          stmt.setNull(89, 4);
        else {
          stmt.setBigDecimal(89, 
            invoiceItem.getNorgnettaxprice().toBigDecimal());
        }
        if (invoiceItem.getNreasonwastenum() == null)
          stmt.setNull(90, 4);
        else {
          stmt.setBigDecimal(90, 
            invoiceItem.getNreasonwastenum().toBigDecimal());
        }

        if (invoiceItem.getPk_upsrccorp() == null)
          stmt.setNull(91, 1);
        else {
          stmt.setString(91, invoiceItem.getPk_upsrccorp());
        }
        
        if (invoiceItem.getB_cjje1() == null)
            stmt.setNull(92, 1);
          else {
            stmt.setString(92, invoiceItem.getB_cjje1());
          }
        
        if (invoiceItem.getB_cjje2() == null)
            stmt.setNull(93, 1);
          else {
            stmt.setString(93, invoiceItem.getB_cjje2());
          }
        
        if (invoiceItem.getB_cjje3() == null)
            stmt.setNull(94, 1);
          else {
            stmt.setString(94, invoiceItem.getB_cjje3());
          }

        executeUpdate(stmt);
      }
      executeBatch(stmt);
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "insertItems", new Object[] { invoiceItems });

    return keys;
  }

  public String[] insertItems(InvoiceItemVO[] invoiceItems, String foreignKey)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "insertItems", new Object[] { invoiceItems, foreignKey });

    for (int i = 0; (invoiceItems != null) && (i < invoiceItems.length); i++) {
      invoiceItems[i].setCinvoiceid(foreignKey);
    }
    String[] keys = insertItems(invoiceItems);

    afterCallMethod("nc.bs.pi.InvoiceDMO", "insertItems", new Object[] { invoiceItems, foreignKey });

    return keys;
  }

  public ArrayList queryForSaveAudit(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "queryForSaveAudit", new Object[0]);

    ArrayList arr = new ArrayList();
    String sql = "SELECT dauditdate,cauditpsn,ibillstatus,ts FROM po_invoice WHERE cinvoiceid='";
    sql = sql + key + "'";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String sDate = rs.getString("dauditdate");
        UFDate dDate = sDate == null ? null : new UFDate(sDate);
        String sAuditPsn = rs.getString("cauditpsn");
        Integer iBillStatus = new Integer(rs.getInt("ibillstatus"));
        String ts = rs.getString("ts");

        arr.add(dDate);
        arr.add(sAuditPsn);
        arr.add(iBillStatus);
        arr.add(ts);
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "queryForSaveAudit", new Object[0]);

    return arr;
  }

  public Hashtable queryForSaveAuditBatch(String[] key) throws SQLException
  {
    String sql = "SELECT cinvoiceid,dauditdate,cauditpsn,ibillstatus,ts FROM po_invoice WHERE dr = 0 and cinvoiceid in ('";
    for (int i = 0; i < key.length - 1; i++) sql = sql + key[i] + "','";
    sql = sql + key[(key.length - 1)] + "')";

    Hashtable t = new Hashtable();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String s = rs.getString("cinvoiceid");
        String sDate = rs.getString("dauditdate");
        UFDate dDate = sDate == null ? null : new UFDate(sDate);
        String sAuditPsn = rs.getString("cauditpsn");
        Integer iBillStatus = new Integer(rs.getInt("ibillstatus"));
        String ts = rs.getString("ts");

        ArrayList arr = new ArrayList();
        arr.add(dDate);
        arr.add(sAuditPsn);
        arr.add(iBillStatus);
        arr.add(ts);

        if (s == null) continue; t.put(s, arr);
      }

      if (rs != null) rs.close(); 
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1) {
      }
    }
    return t;
  }

  public boolean queryIfExecVMI(String conHids)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "queryIfExecVMI", new Object[0]);

    String sql = "SELECT cinvoice_bid,csourcebillid FROM po_invoice_b WHERE dr = 0 and csourcebilltype='50' and csourcebillid in " + conHids;

    Connection con = null;
    PreparedStatement stmt = null;
    boolean isAlreadyGen = false;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        isAlreadyGen = true;
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "queryForSaveAudit", new Object[0]);

    return isAlreadyGen;
  }

  public void updateAccumInvoiceNumByScOrderBPKMy(String[] keys, double[] values)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateAccumInvoiceNumByScOrderBPKMy", new Object[] { keys, values });

    if ((keys == null) || 
      (keys.length == 0) || 
      (values == null) || 
      (values.length == 0) || 
      (keys.length != values.length)) {
      return;
    }

    HashMap mapCom = new HashMap();
    UFDouble ufdNum = null;
    for (int i = 0; i < keys.length; i++) {
      if (keys[i] == null) {
        continue;
      }
      if (mapCom.containsKey(keys[i])) {
        ufdNum = (UFDouble)mapCom.get(keys[i]);
        ufdNum = ufdNum.add(values[i]);
        mapCom.put(keys[i], ufdNum);
      } else {
        mapCom.put(keys[i], new UFDouble(values[i]));
      }
    }
    if (mapCom.size() == 0) {
      return;
    }
    Object[] oaKeys = mapCom.keySet().toArray();

    String sql = "UPDATE sc_order_b set naccuminvoicenum=?  WHERE corder_bid=?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = prepareStatement(con, sql);
      for (int i = 0; i < oaKeys.length; i++) {
        stmt.setDouble(1, ((UFDouble)mapCom.get(oaKeys[i])).doubleValue());
        stmt.setString(2, (String) oaKeys[i]);

        executeUpdate(stmt);
      }
      executeBatch(stmt);
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateAccumInvoiceNumByScOrderBPKMy", new Object[] { keys, values });
  }

  public void updateAccumInvoiceNumByScOrderBPKMy(String[] keys, double[] values, double[] prices, InvoiceVO invVO)
    throws SQLException, BusinessException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateAccumInvoiceNumByScOrderBPKMy", new Object[] { keys, values });
    try
    {
      String sPk_corp = invVO.getHeadVO().getPk_corp();

      ISysInitQry dmoInit = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
      String sNumPresKind = dmoInit.getParaString(sPk_corp, "PO02");
      String sPricePresKind = dmoInit.getParaString(sPk_corp, "PO04");

     int  iPriceDigit = dmoInit.getParaInt(sPk_corp, "BD505").intValue();

      boolean bNumCheck = false;
      if ((sNumPresKind.equals("不保存")) || (sNumPresKind.equals("提示"))) bNumCheck = true;
      boolean bPriceCheck = false;
      if ((sPricePresKind.equals("不保存")) || (sPricePresKind.equals("提示"))) bPriceCheck = true;
      if ((bNumCheck) || (bPriceCheck))
      {
        UFDouble dNumPresValue = null;
        UFDouble dPricePresValue = null;
        if (bNumCheck) {
          dNumPresValue = dmoInit.getParaDbl(sPk_corp, "PO03");
          dNumPresValue = PuPubVO.getUFDouble_NullAsZero(dNumPresValue);
        }
        if (bPriceCheck) {
          dPricePresValue = dmoInit.getParaDbl(sPk_corp, "PO05");
          dPricePresValue = PuPubVO.getUFDouble_NullAsZero(dPricePresValue);
        }

        String[] saBId = keys;

        UFDouble[] daLocalNetPrice = (UFDouble[])null;
        if (prices != null) {
          daLocalNetPrice = new UFDouble[prices.length];
          for (int i = 0; i < prices.length; i++) {
            daLocalNetPrice[i] = new UFDouble(prices[i]);
          }

        }

        String sPoBIdSubSql = new TempTableDMO().insertTempTable(
          saBId, 
          "t_pu_po_017", 
          "corder_bid");

        PubDMO dmoPuPub = new PubDMO();
        Hashtable htScData = dmoPuPub.queryHtResultFromAnyTable(
          "sc_order_b", 
          "corder_bid", 
          new String[] { "nordernum", "nmoney", "naccuminvoicenum" }, 
          "corder_bid IN " + sPoBIdSubSql);
        if (htScData == null) {
          htScData = new Hashtable();
        }
        Object[] oaData = (Object[])null;
        UFDouble dOrderNum = null;
        UFDouble dMoney = null;
        UFDouble dAccumInvoiceNum = null;
        UFDouble dPrice = null;

        int iLen = saBId.length;
        for (int i = 0; i < iLen; i++) {
          Vector vecData = (Vector)htScData.get(saBId[i]);
          if (vecData == null)
          {
            continue;
          }
          oaData = (Object[])vecData.get(0);
          dOrderNum = PuPubVO.getUFDouble_NullAsZero(oaData[0]);
          dMoney = PuPubVO.getUFDouble_NullAsZero(oaData[1]);
          dAccumInvoiceNum = PuPubVO.getUFDouble_NullAsZero(null);
          dAccumInvoiceNum = dAccumInvoiceNum.add(values[i]);

          if (dOrderNum.multiply(dAccumInvoiceNum).compareTo(VariableConst.ZERO) < 0) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000259"));
          }

          if ((bNumCheck) && (
            ((dAccumInvoiceNum.compareTo(VariableConst.ZERO) > 0) && (dAccumInvoiceNum.compareTo(dOrderNum.multiply(dNumPresValue.div(100.0D).add(1.0D))) > 0)) || (
            (dAccumInvoiceNum.compareTo(VariableConst.ZERO) < 0) && (dAccumInvoiceNum.compareTo(dOrderNum.multiply(dNumPresValue.div(100.0D).add(1.0D))) < 0)))) {
            if (sNumPresKind.equals("提示")) {
              if ((invVO.getUserConfirmFlag() == null) || (!invVO.getUserConfirmFlag().booleanValue()))
                throw new RwtPiToScException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000260"));
            }
            else {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000260"));
            }

          }

          if ((!bPriceCheck) || 
            (dOrderNum.compareTo(VariableConst.ZERO) == 0))
          {
            continue;
          }
          dPrice = dMoney.div(dOrderNum).setScale(iPriceDigit, 4);
          dPrice = dPrice.multiply(dPricePresValue.div(100.0D).add(1.0D)).setScale(iPriceDigit, 4);
          daLocalNetPrice[i] = daLocalNetPrice[i].setScale(iPriceDigit, 4);
          if (daLocalNetPrice[i].compareTo(dPrice) > 0) {
            if (sPricePresKind.equals("提示")) {
              if ((invVO.getUserConfirmFlag() == null) || (!invVO.getUserConfirmFlag().booleanValue()))
                throw new RwtPiToScException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000261"));
            }
            else {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000261"));
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException("updateAccumInvoiceNumByScOrderBPKMy", e);
    }

    String sql = "UPDATE sc_order_b set naccuminvoicenum=?  WHERE corder_bid=?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = prepareStatement(con, sql);
      for (int i = 0; (keys != null) && (values != null) && (keys.length == values.length) && (i < keys.length); i++) {
        stmt.setDouble(1, values[i]);
        stmt.setString(2, keys[i]);

        executeUpdate(stmt);
      }
      executeBatch(stmt);
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException2)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateAccumInvoiceNumByScOrderBPKMy", new Object[] { keys, values });
  }

  public void updateAccumWashNumForIC(String[] keys, double[] values, boolean addFlag)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateAccumWashNumForIC", new Object[] { keys, values });

    String sql = "UPDATE ic_general_bb3 set naccumwashnum = isnull(naccumwashnum,0) + ? WHERE dr = 0 and cgeneralbid = ?";
    if (!addFlag) {
      sql = "UPDATE ic_general_bb3 set naccumwashnum = isnull(naccumwashnum,0) - ? WHERE dr = 0 and cgeneralbid = ?";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = prepareStatement(con, sql);
      for (int i = 0; (keys != null) && (values != null) && (keys.length == values.length) && (i < keys.length); i++) {
        stmt.setDouble(1, values[i]);
        stmt.setString(2, keys[i]);

        executeUpdate(stmt);
      }
      executeBatch(stmt);
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateAccumWashNumForIC", new Object[] { keys, values });
  }

  public void updateCostPriceForInv(InvoiceItemVO[] vos, String sStoreOrg, String sCorpId)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateInvoicePaymentMny", new Object[] { vos, sStoreOrg, sCorpId });

    String sql = "update bd_produce set ckcb = ? where pk_invbasdoc = ? and pk_calbody = ? and pk_corp = ?";
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = prepareStatement(con, sql);

      for (int i = 0; (vos != null) && (i < vos.length); i++) {
        double value = vos[i].getNmoney().doubleValue() / vos[i].getNinvoicenum().doubleValue();
        stmt.setDouble(1, value);
        stmt.setString(2, vos[i].getCbaseid());
        stmt.setString(3, sStoreOrg);
        stmt.setString(4, sCorpId);
        executeUpdate(stmt);
      }
      executeBatch(stmt);
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateRefSalePriceForInv", new Object[] { vos, sStoreOrg, sCorpId });
  }

  public void updateInvoicePaymentMny(String[] sPk_corp, String[] strInvoiceBId, UFDouble[] dOriginalPayMny, UFDouble[] dNativePayMny, UFDouble[] dAssistPayMny)
    throws Exception
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateInvoicePaymentMny", new Object[] { strInvoiceBId });

    String sql2 = "UPDATE po_invoice_b SET noriginalpaymentmny=ISNULL(noriginalpaymentmny,0.0)+(?),npaymentmny=ISNULL(npaymentmny,0.0)+(?),nassistpaymny=ISNULL(nassistpaymny,0.0)+(?)  WHERE cinvoice_bid=?";
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = prepareStatement(con, sql2);

      for (int i = 0; i < sPk_corp.length; i++) {
        stmt.setBigDecimal(1, dOriginalPayMny[i] == null ? VariableConst.ZERO.toBigDecimal() : dOriginalPayMny[i].toBigDecimal());
        stmt.setBigDecimal(2, dNativePayMny[i] == null ? VariableConst.ZERO.toBigDecimal() : dNativePayMny[i].toBigDecimal());
        stmt.setBigDecimal(3, dAssistPayMny[i] == null ? VariableConst.ZERO.toBigDecimal() : dAssistPayMny[i].toBigDecimal());
        stmt.setString(4, strInvoiceBId[i]);
        executeUpdate(stmt);
      }
      executeBatch(stmt);
    } catch (Exception e) {
      SCMEnv.out(e);
      try
      {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2)
      {
      }
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException3) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException4)
      {
      }
    }
    PoCloseImpl beanPoClose = null;
    try
    {
      ArrayList listValid = new ArrayList();
      int iLen = strInvoiceBId.length;
      for (int i = 0; i < iLen; i++) {
        if (PuPubVO.getString_TrimZeroLenAsNull(strInvoiceBId[i]) != null) {
          listValid.add(strInvoiceBId[i]);
        }
      }
      if (listValid.size() == 0) {
        SCMEnv.out("发票行均不是源自采购订单，未执行自动关闭功能!");
        return;
      }
      String[] saRowid = (String[])listValid.toArray(new String[listValid.size()]);
      Object[][] oa2Val = new PubDMO().queryArrayValue("po_invoice_b", "cinvoice_bid", new String[] { "corder_bid" }, saRowid);
      if (oa2Val == null) {
        SCMEnv.out("发票行均不是源自采购订单，未执行自动关闭功能!");
        return;
      }
      iLen = oa2Val.length;
      saRowid = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        saRowid[i] = (oa2Val[i] == null ? null : (String)oa2Val[i][0]);
      }
      beanPoClose = getBean_PoClose();

      beanPoClose.closeRowsAtOnce_ForPay(
        sPk_corp[0], 
        saRowid, 
        null);
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateInvoicePaymentMny", new Object[] { strInvoiceBId });
  }

  public void updateItems(InvoiceItemVO[] invoiceItems)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pi.InvoiceDMO", "updateItems", new Object[] { invoiceItems });

    StringBuffer sql = new StringBuffer("UPDATE po_invoice_b SET ");
    sql.append("cinvoiceid = ?,pk_corp = ?,cusedeptid = ?,corder_bid = ?, ");
    sql.append("corderid = ?,csourcebilltype = ?,csourcebillid = ?,csourcebillrowid = ?,cupsourcebilltype = ?, ");
    sql.append("cupsourcebillid = ?,cupsourcebillrowid = ?,cmangid = ?,cbaseid = ?,ninvoicenum = ?,");
    sql.append("naccumsettnum = ?,idiscounttaxtype = ?,ntaxrate = ?,ccurrencytypeid = ?,noriginalcurprice = ?,");
    sql.append("noriginaltaxmny = ?,noriginalcurmny = ?,noriginalsummny = ?,noriginalpaymentmny = ?,nexchangeotobrate = ?,");
    sql.append("nmoney = ?,ntaxmny = ?,nsummny = ?,npaymentmny = ?,naccumsettmny = ?,");
    sql.append("nexchangeotoarate = ?,nassistcurmny = ?,nassisttaxmny = ?,nassistsummny = ?,nassistpaymny = ?,");
    sql.append("nassistsettmny = ?,cprojectid = ?,cprojectphaseid = ?,vmemo = ?,vfree1 = ?,");
    sql.append("vfree2 = ?,vfree3 = ?,vfree4 = ?,vfree5 = ?,vdef1 = ?,");
    sql.append("vdef2 = ?,vdef3 = ?,vdef4 = ?,vdef5 = ?,vdef6 = ?,");
    sql.append("vdef7 = ?,vdef8 = ?,vdef9 = ?,vdef10 = ?,");
    sql.append("vdef11 = ?,vdef12 = ?,vdef13 = ?,vdef14 = ?,vdef15 = ?,");
    sql.append("vdef16 = ?,vdef17 = ?,vdef18 = ?,vdef19 = ?,vdef20 = ?,");
    sql.append("pk_defdoc1 = ?,pk_defdoc2 = ?,pk_defdoc3 = ?,pk_defdoc4 = ?,pk_defdoc5 = ?,");
    sql.append("pk_defdoc6 = ?,pk_defdoc7 = ?,pk_defdoc8 = ?,pk_defdoc9 = ?,pk_defdoc10 = ?,");
    sql.append("pk_defdoc11 = ?,pk_defdoc12 = ?,pk_defdoc13 = ?,pk_defdoc14 = ?,pk_defdoc15 = ?,");
    sql.append("pk_defdoc16 = ?,pk_defdoc17 = ?,pk_defdoc18 = ?,pk_defdoc19 = ?,pk_defdoc20 = ?,");
    sql.append("dr = ?,cwarehouseid = ?,vproducenum = ?,crowno = ?,norgnettaxprice = ?,nreasonwastenum = ?,pk_upsrccorp = ?,b_cjje1 = ?,b_cjje2 = ?,b_cjje3 = ? ");
    sql.append(" WHERE cinvoice_bid = ?");

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = prepareStatement(con, sql.toString());
      InvoiceItemVO invoiceItem = null;

      for (int i = 0; (invoiceItems != null) && (i < invoiceItems.length); i++) {
        invoiceItem = invoiceItems[i];

        if (invoiceItem.getCinvoiceid() == null)
          stmt.setNull(1, 1);
        else {
          stmt.setString(1, invoiceItem.getCinvoiceid());
        }
        if (invoiceItem.getPk_corp() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, invoiceItem.getPk_corp());
        }
        if (invoiceItem.getCusedeptid() == null)
          stmt.setNull(3, 1);
        else {
          stmt.setString(3, invoiceItem.getCusedeptid());
        }
        if (invoiceItem.getCorder_bid() == null)
          stmt.setNull(4, 1);
        else {
          stmt.setString(4, invoiceItem.getCorder_bid());
        }
        if (invoiceItem.getCorderid() == null)
          stmt.setNull(5, 1);
        else {
          stmt.setString(5, invoiceItem.getCorderid());
        }
        if (invoiceItem.getCsourcebilltype() == null)
          stmt.setNull(6, 1);
        else {
          stmt.setString(6, invoiceItem.getCsourcebilltype());
        }
        if (invoiceItem.getCsourcebillid() == null)
          stmt.setNull(7, 1);
        else {
          stmt.setString(7, invoiceItem.getCsourcebillid());
        }
        if (invoiceItem.getCsourcebillrowid() == null)
          stmt.setNull(8, 1);
        else {
          stmt.setString(8, invoiceItem.getCsourcebillrowid());
        }
        if (invoiceItem.getCupsourcebilltype() == null)
          stmt.setNull(9, 1);
        else {
          stmt.setString(9, invoiceItem.getCupsourcebilltype());
        }
        if (invoiceItem.getCupsourcebillid() == null)
          stmt.setNull(10, 1);
        else {
          stmt.setString(10, invoiceItem.getCupsourcebillid());
        }
        if (invoiceItem.getCupsourcebillrowid() == null)
          stmt.setNull(11, 1);
        else {
          stmt.setString(11, invoiceItem.getCupsourcebillrowid());
        }
        if (invoiceItem.getCmangid() == null)
          stmt.setNull(12, 1);
        else {
          stmt.setString(12, invoiceItem.getCmangid());
        }
        if (invoiceItem.getCbaseid() == null)
          stmt.setNull(13, 1);
        else {
          stmt.setString(13, invoiceItem.getCbaseid());
        }
        if (invoiceItem.getNinvoicenum() == null)
          stmt.setNull(14, 4);
        else {
          stmt.setBigDecimal(14, 
            invoiceItem.getNinvoicenum().toBigDecimal());
        }
        if (invoiceItem.getNaccumsettnum() == null)
          stmt.setNull(15, 4);
        else {
          stmt.setBigDecimal(15, 
            invoiceItem.getNaccumsettnum().toBigDecimal());
        }
        if (invoiceItem.getIdiscounttaxtype() == null)
          stmt.setNull(16, 4);
        else {
          stmt.setInt(16, 
            invoiceItem.getIdiscounttaxtype().intValue());
        }
        if (invoiceItem.getNtaxrate() == null)
          stmt.setNull(17, 4);
        else {
          stmt.setBigDecimal(17, 
            invoiceItem.getNtaxrate().toBigDecimal());
        }
        if (invoiceItem.getCcurrencytypeid() == null)
          stmt.setNull(18, 1);
        else {
          stmt.setString(18, invoiceItem.getCcurrencytypeid());
        }
        if (invoiceItem.getNoriginalcurprice() == null)
          stmt.setNull(19, 4);
        else {
          stmt.setBigDecimal(19, 
            invoiceItem.getNoriginalcurprice().toBigDecimal());
        }
        if (invoiceItem.getNoriginaltaxmny() == null)
          stmt.setNull(20, 4);
        else {
          stmt.setBigDecimal(20, 
            invoiceItem.getNoriginaltaxmny().toBigDecimal());
        }
        if (invoiceItem.getNoriginalcurmny() == null)
          stmt.setNull(21, 4);
        else {
          stmt.setBigDecimal(21, 
            invoiceItem.getNoriginalcurmny().toBigDecimal());
        }
        if (invoiceItem.getNoriginalsummny() == null)
          stmt.setNull(22, 4);
        else {
          stmt.setBigDecimal(22, 
            invoiceItem.getNoriginalsummny().toBigDecimal());
        }
        if (invoiceItem.getNoriginalpaymentmny() == null)
          stmt.setNull(23, 4);
        else {
          stmt.setBigDecimal(23, 
            invoiceItem.getNoriginalpaymentmny().toBigDecimal());
        }
        if (invoiceItem.getNexchangeotobrate() == null)
          stmt.setNull(24, 4);
        else {
          stmt.setBigDecimal(24, 
            invoiceItem.getNexchangeotobrate().toBigDecimal());
        }
        if (invoiceItem.getNmoney() == null)
          stmt.setNull(25, 4);
        else {
          stmt.setBigDecimal(25, 
            invoiceItem.getNmoney().toBigDecimal());
        }
        if (invoiceItem.getNtaxmny() == null)
          stmt.setNull(26, 4);
        else {
          stmt.setBigDecimal(26, 
            invoiceItem.getNtaxmny().toBigDecimal());
        }
        if (invoiceItem.getNsummny() == null)
          stmt.setNull(27, 4);
        else {
          stmt.setBigDecimal(27, 
            invoiceItem.getNsummny().toBigDecimal());
        }
        if (invoiceItem.getNpaymentmny() == null)
          stmt.setNull(28, 4);
        else {
          stmt.setBigDecimal(28, 
            invoiceItem.getNpaymentmny().toBigDecimal());
        }
        if (invoiceItem.getNaccumsettmny() == null)
          stmt.setNull(29, 4);
        else {
          stmt.setBigDecimal(29, 
            invoiceItem.getNaccumsettmny().toBigDecimal());
        }
        if (invoiceItem.getNexchangeotoarate() == null)
          stmt.setNull(30, 4);
        else {
          stmt.setBigDecimal(30, 
            invoiceItem.getNexchangeotoarate().toBigDecimal());
        }
        if (invoiceItem.getNassistcurmny() == null)
          stmt.setNull(31, 4);
        else {
          stmt.setBigDecimal(31, 
            invoiceItem.getNassistcurmny().toBigDecimal());
        }
        if (invoiceItem.getNassisttaxmny() == null)
          stmt.setNull(32, 4);
        else {
          stmt.setBigDecimal(32, 
            invoiceItem.getNassisttaxmny().toBigDecimal());
        }
        if (invoiceItem.getNassistsummny() == null)
          stmt.setNull(33, 4);
        else {
          stmt.setBigDecimal(33, 
            invoiceItem.getNassistsummny().toBigDecimal());
        }
        if (invoiceItem.getNassistpaymny() == null)
          stmt.setNull(34, 4);
        else {
          stmt.setBigDecimal(34, 
            invoiceItem.getNassistpaymny().toBigDecimal());
        }
        if (invoiceItem.getNassistsettmny() == null)
          stmt.setNull(35, 4);
        else {
          stmt.setBigDecimal(35, 
            invoiceItem.getNassistsettmny().toBigDecimal());
        }
        if (invoiceItem.getCprojectid() == null)
          stmt.setNull(36, 1);
        else {
          stmt.setString(36, invoiceItem.getCprojectid());
        }
        if (invoiceItem.getCprojectphaseid() == null)
          stmt.setNull(37, 1);
        else {
          stmt.setString(37, invoiceItem.getCprojectphaseid());
        }
        if (invoiceItem.getVmemo() == null)
          stmt.setNull(38, 1);
        else {
          stmt.setString(38, invoiceItem.getVmemo());
        }
        if (invoiceItem.getVfree1() == null)
          stmt.setNull(39, 1);
        else {
          stmt.setString(39, invoiceItem.getVfree1());
        }
        if (invoiceItem.getVfree2() == null)
          stmt.setNull(40, 1);
        else {
          stmt.setString(40, invoiceItem.getVfree2());
        }
        if (invoiceItem.getVfree3() == null)
          stmt.setNull(41, 1);
        else {
          stmt.setString(41, invoiceItem.getVfree3());
        }
        if (invoiceItem.getVfree4() == null)
          stmt.setNull(42, 1);
        else {
          stmt.setString(42, invoiceItem.getVfree4());
        }
        if (invoiceItem.getVfree5() == null)
          stmt.setNull(43, 1);
        else {
          stmt.setString(43, invoiceItem.getVfree5());
        }
        if (invoiceItem.getVdef1() == null)
          stmt.setNull(44, 1);
        else {
          stmt.setString(44, invoiceItem.getVdef1());
        }
        if (invoiceItem.getVdef2() == null)
          stmt.setNull(45, 1);
        else {
          stmt.setString(45, invoiceItem.getVdef2());
        }
        if (invoiceItem.getVdef3() == null)
          stmt.setNull(46, 1);
        else {
          stmt.setString(46, invoiceItem.getVdef3());
        }
        if (invoiceItem.getVdef4() == null)
          stmt.setNull(47, 1);
        else {
          stmt.setString(47, invoiceItem.getVdef4());
        }
        if (invoiceItem.getVdef5() == null)
          stmt.setNull(48, 1);
        else {
          stmt.setString(48, invoiceItem.getVdef5());
        }
        if (invoiceItem.getVdef6() == null)
          stmt.setNull(49, 1);
        else {
          stmt.setString(49, invoiceItem.getVdef6());
        }

        if (invoiceItem.getVdef7() == null)
          stmt.setNull(50, 1);
        else {
          stmt.setString(50, invoiceItem.getVdef7());
        }
        if (invoiceItem.getVdef8() == null)
          stmt.setNull(51, 1);
        else {
          stmt.setString(51, invoiceItem.getVdef8());
        }
        if (invoiceItem.getVdef9() == null)
          stmt.setNull(52, 1);
        else {
          stmt.setString(52, invoiceItem.getVdef9());
        }
        if (invoiceItem.getVdef10() == null)
          stmt.setNull(53, 1);
        else {
          stmt.setString(53, invoiceItem.getVdef10());
        }
        if (invoiceItem.getVdef11() == null)
          stmt.setNull(54, 1);
        else {
          stmt.setString(54, invoiceItem.getVdef11());
        }
        if (invoiceItem.getVdef12() == null)
          stmt.setNull(55, 1);
        else {
          stmt.setString(55, invoiceItem.getVdef12());
        }
        if (invoiceItem.getVdef13() == null)
          stmt.setNull(56, 1);
        else {
          stmt.setString(56, invoiceItem.getVdef13());
        }
        if (invoiceItem.getVdef14() == null)
          stmt.setNull(57, 1);
        else {
          stmt.setString(57, invoiceItem.getVdef14());
        }
        if (invoiceItem.getVdef15() == null)
          stmt.setNull(58, 1);
        else {
          stmt.setString(58, invoiceItem.getVdef15());
        }
        if (invoiceItem.getVdef16() == null)
          stmt.setNull(59, 1);
        else {
          stmt.setString(59, invoiceItem.getVdef16());
        }
        if (invoiceItem.getVdef17() == null)
          stmt.setNull(60, 1);
        else {
          stmt.setString(60, invoiceItem.getVdef17());
        }
        if (invoiceItem.getVdef18() == null)
          stmt.setNull(61, 1);
        else {
          stmt.setString(61, invoiceItem.getVdef18());
        }
        if (invoiceItem.getVdef19() == null)
          stmt.setNull(62, 1);
        else {
          stmt.setString(62, invoiceItem.getVdef19());
        }
        if (invoiceItem.getVdef20() == null)
          stmt.setNull(63, 1);
        else {
          stmt.setString(63, invoiceItem.getVdef20());
        }
        if (invoiceItem.getPKDefDoc1() == null)
          stmt.setNull(64, 1);
        else {
          stmt.setString(64, invoiceItem.getPKDefDoc1());
        }
        if (invoiceItem.getPKDefDoc2() == null)
          stmt.setNull(65, 1);
        else {
          stmt.setString(65, invoiceItem.getPKDefDoc2());
        }
        if (invoiceItem.getPKDefDoc3() == null)
          stmt.setNull(66, 1);
        else {
          stmt.setString(66, invoiceItem.getPKDefDoc3());
        }
        if (invoiceItem.getPKDefDoc4() == null)
          stmt.setNull(67, 1);
        else {
          stmt.setString(67, invoiceItem.getPKDefDoc4());
        }
        if (invoiceItem.getPKDefDoc5() == null)
          stmt.setNull(68, 1);
        else {
          stmt.setString(68, invoiceItem.getPKDefDoc5());
        }
        if (invoiceItem.getPKDefDoc6() == null)
          stmt.setNull(69, 1);
        else {
          stmt.setString(69, invoiceItem.getPKDefDoc6());
        }
        if (invoiceItem.getPKDefDoc7() == null)
          stmt.setNull(70, 1);
        else {
          stmt.setString(70, invoiceItem.getPKDefDoc7());
        }
        if (invoiceItem.getPKDefDoc8() == null)
          stmt.setNull(71, 1);
        else {
          stmt.setString(71, invoiceItem.getPKDefDoc8());
        }
        if (invoiceItem.getPKDefDoc9() == null)
          stmt.setNull(72, 1);
        else {
          stmt.setString(72, invoiceItem.getPKDefDoc9());
        }
        if (invoiceItem.getPKDefDoc10() == null)
          stmt.setNull(73, 1);
        else {
          stmt.setString(73, invoiceItem.getPKDefDoc10());
        }
        if (invoiceItem.getPKDefDoc11() == null)
          stmt.setNull(74, 1);
        else {
          stmt.setString(74, invoiceItem.getPKDefDoc11());
        }
        if (invoiceItem.getPKDefDoc12() == null)
          stmt.setNull(75, 1);
        else {
          stmt.setString(75, invoiceItem.getPKDefDoc12());
        }
        if (invoiceItem.getPKDefDoc13() == null)
          stmt.setNull(76, 1);
        else {
          stmt.setString(76, invoiceItem.getPKDefDoc13());
        }
        if (invoiceItem.getPKDefDoc14() == null)
          stmt.setNull(77, 1);
        else {
          stmt.setString(77, invoiceItem.getPKDefDoc14());
        }
        if (invoiceItem.getPKDefDoc15() == null)
          stmt.setNull(78, 1);
        else {
          stmt.setString(78, invoiceItem.getPKDefDoc15());
        }
        if (invoiceItem.getPKDefDoc16() == null)
          stmt.setNull(79, 1);
        else {
          stmt.setString(79, invoiceItem.getPKDefDoc16());
        }
        if (invoiceItem.getPKDefDoc17() == null)
          stmt.setNull(80, 1);
        else {
          stmt.setString(80, invoiceItem.getPKDefDoc17());
        }
        if (invoiceItem.getPKDefDoc18() == null)
          stmt.setNull(81, 1);
        else {
          stmt.setString(81, invoiceItem.getPKDefDoc18());
        }
        if (invoiceItem.getPKDefDoc19() == null)
          stmt.setNull(82, 1);
        else {
          stmt.setString(82, invoiceItem.getPKDefDoc19());
        }
        if (invoiceItem.getPKDefDoc20() == null)
          stmt.setNull(83, 1);
        else {
          stmt.setString(83, invoiceItem.getPKDefDoc20());
        }
        if (invoiceItem.getDr() == null)
          stmt.setNull(84, 4);
        else {
          stmt.setInt(84, invoiceItem.getDr().intValue());
        }
        if (invoiceItem.getCwarehouseid() == null)
          stmt.setNull(85, 1);
        else {
          stmt.setString(85, invoiceItem.getCwarehouseid());
        }
        if (invoiceItem.getVproducenum() == null)
          stmt.setNull(86, 4);
        else {
          stmt.setString(86, invoiceItem.getVproducenum());
        }
        if (invoiceItem.getCrowno() == null)
          stmt.setNull(87, 4);
        else {
          stmt.setString(87, invoiceItem.getCrowno());
        }
        if (invoiceItem.getNorgnettaxprice() == null)
          stmt.setNull(88, 4);
        else {
          stmt.setBigDecimal(88, 
            invoiceItem.getNorgnettaxprice().toBigDecimal());
        }
        if (invoiceItem.getNreasonwastenum() == null)
          stmt.setNull(89, 4);
        else {
          stmt.setBigDecimal(89, 
            invoiceItem.getNreasonwastenum().toBigDecimal());
        }

        if (invoiceItem.getPk_upsrccorp() == null)
          stmt.setNull(90, 1);
        else {
          stmt.setString(90, invoiceItem.getPk_upsrccorp());
        }
        
        if (invoiceItem.getB_cjje1() == null)
            stmt.setNull(91, 1);
          else {
            stmt.setString(91, invoiceItem.getB_cjje1());
          }
        
        if (invoiceItem.getB_cjje2() == null)
            stmt.setNull(92, 1);
          else {
            stmt.setString(92, invoiceItem.getB_cjje2());
          }
        
        if (invoiceItem.getB_cjje3() == null)
            stmt.setNull(93, 1);
          else {
            stmt.setString(93, invoiceItem.getB_cjje3());
          }

        stmt.setString(94, invoiceItem.getPrimaryKey());

        executeUpdate(stmt);
      }
      executeBatch(stmt);
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1)
      {
      }
    }
    afterCallMethod("nc.bs.pi.InvoiceDMO", "updateItems", new Object[] { invoiceItems });
  }

  public DjclDapVO afterVerifyAct(DjclDapVO vo) throws Exception
  {
    SCMEnv.out("执行收付接口方法开始afterVerifyAct(DjclDapVO)...");
    DjclDapItemVO[] voItems = (DjclDapItemVO[])vo.getChildrenVO();
    if ((voItems == null) || (voItems.length == 0)) return vo;

    Vector vTemp = new Vector();
    for (int i = 0; i < voItems.length; i++) {
      if ((voItems[i].getDdhh() == null) || (voItems[i].getDdhh().trim().length() <= 0)) continue; vTemp.addElement(voItems[i]);
    }
    if (vTemp.size() == 0) return vo;
    DjclDapItemVO[] voItem = new DjclDapItemVO[vTemp.size()];
    vTemp.copyInto(voItem);

    int nLen = voItem.length;
    String[] pk_corp = new String[nLen]; String[] rowid = new String[nLen];
    UFDouble[] clybje = new UFDouble[nLen]; UFDouble[] clbbje = new UFDouble[nLen]; UFDouble[] clfbje = new UFDouble[nLen];
    for (int i = 0; i < nLen; i++) {
      int lybz = voItem[i].getLybz().intValue();
      int clbz = voItem[i].getClbz().intValue();
      pk_corp[i] = voItem[i].getDwbm();

      if ((lybz == 4) && ((clbz == -1) || (clbz == 0) || (clbz == 3) || (clbz == 4) || (clbz == -2) || (clbz == 1) || (clbz == 2) || (clbz == 5) || (clbz == 6))) {
        rowid[i] = voItem[i].getDdhh();
        CurrencyRateUtil sCurrdmo = new CurrencyRateUtil(pk_corp[i]);

        String ybbm = voItem[i].getBzbm();
        String fbbm = sCurrdmo.getFracCurrPK();
        String bbbm = sCurrdmo.getLocalCurrPK();
        int ybDig = CurrtypeQuery.getInstance().getCurrtypeVO(ybbm).getCurrdigit().intValue();
        int fbDig = 
          (fbbm != null) && (fbbm.trim().length() != 0) ? 
          CurrtypeQuery.getInstance().getCurrtypeVO(fbbm).getCurrdigit().intValue() : 0;
        int bbDig = CurrtypeQuery.getInstance().getCurrtypeVO(bbbm).getCurrdigit().intValue();

        clybje[i] = voItem[i].getJfclybje().add(voItem[i].getDfclybje());
        clfbje[i] = voItem[i].getJfclfbje().add(voItem[i].getDfclfbje());
        clbbje[i] = voItem[i].getJfclbbje().add(voItem[i].getDfclbbje());
        clybje[i] = new UFDouble(clybje[i].toString(), ybDig);
        clfbje[i] = new UFDouble(clfbje[i].toString(), fbDig);
        clbbje[i] = new UFDouble(clbbje[i].toString(), bbDig);
      }
    }

    updateInvoicePaymentMny(pk_corp, rowid, clybje, clbbje, clfbje);
    SCMEnv.out("执行收付接口方法结束afterVerifyAct(DjclDapVO)");

    return vo;
  }

  public DJCLBVO[] beforeVerifyAct(DJCLBVO[] vos) throws Exception {
    SCMEnv.out("无操作返回DJZBVO!执行收付接口方法在afterVerifyAct(DJCLBVO)");

    return vos;
  }

  public DjclDapVO afterUnVerifyAct(DjclDapVO vo) throws Exception {
    SCMEnv.out("执行收付接口方法开始 afterUnVerifyAct(DjclDapVO)...");
    DjclDapItemVO[] voItems = (DjclDapItemVO[])vo.getChildrenVO();
    if ((voItems == null) || (voItems.length == 0)) return vo;

    Vector vTemp = new Vector();
    for (int i = 0; i < voItems.length; i++) {
      if ((voItems[i].getDdhh() == null) || (voItems[i].getDdhh().trim().length() <= 0)) continue; vTemp.addElement(voItems[i]);
    }
    if (vTemp.size() == 0) return vo;
    DjclDapItemVO[] voItem = new DjclDapItemVO[vTemp.size()];
    vTemp.copyInto(voItem);

    int nLen = voItem.length;
    String[] pk_corp = new String[nLen]; String[] rowid = new String[nLen];
    UFDouble[] clybje = new UFDouble[nLen]; UFDouble[] clbbje = new UFDouble[nLen]; UFDouble[] clfbje = new UFDouble[nLen];

    for (int i = 0; i < nLen; i++) {
      int lybz = voItem[i].getLybz().intValue();
      int clbz = voItem[i].getClbz().intValue();
      pk_corp[i] = voItem[i].getDwbm();

      if ((lybz == 4) && ((clbz == -1) || (clbz == 0) || (clbz == 3) || (clbz == 4) || (clbz == -2) || (clbz == 1) || (clbz == 2) || (clbz == 5) || (clbz == 6))) {
        rowid[i] = voItem[i].getDdhh();
        CurrencyRateUtil sCurrdmo = new CurrencyRateUtil(pk_corp[i]);

        String ybbm = voItem[i].getBzbm();
        String fbbm = sCurrdmo.getFracCurrPK();
        String bbbm = sCurrdmo.getLocalCurrPK();
        int ybDig = CurrtypeQuery.getInstance().getCurrtypeVO(ybbm).getCurrdigit().intValue();
        int fbDig = 
          (fbbm != null) && (fbbm.trim().length() != 0) ? 
          CurrtypeQuery.getInstance().getCurrtypeVO(fbbm).getCurrdigit().intValue() : 0;
        int bbDig = CurrtypeQuery.getInstance().getCurrtypeVO(bbbm).getCurrdigit().intValue();

        clybje[i] = voItem[i].getJfclybje().add(voItem[i].getDfclybje());
        clfbje[i] = voItem[i].getJfclfbje().add(voItem[i].getDfclfbje());
        clbbje[i] = voItem[i].getJfclbbje().add(voItem[i].getDfclbbje());

        clybje[i] = clybje[i].multiply(-1.0D);
        clfbje[i] = clfbje[i].multiply(-1.0D);
        clbbje[i] = clbbje[i].multiply(-1.0D);

        clybje[i] = new UFDouble(clybje[i].toString(), ybDig);
        clfbje[i] = new UFDouble(clfbje[i].toString(), fbDig);
        clbbje[i] = new UFDouble(clbbje[i].toString(), bbDig);
      }
    }

    updateInvoicePaymentMny(pk_corp, rowid, clybje, clbbje, clfbje);
    SCMEnv.out("执行收付接口方法结束 afterUnVerifyAct(DjclDapVO)");

    return vo;
  }

  public DJCLBVO[] beforeUnVerifyAct(DJCLBVO[] vos) throws Exception
  {
    SCMEnv.out("无操作返回DJZBVO!执行收付接口方法在 afterUnVerifyAct(DjclDapVO)");

    return vos;
  }

  public IAdjuestVO[] washDataForZGYF(InvoiceVO[] invoiceVOs)
    throws Exception
  {
    Vector v1 = new Vector(); Vector v2 = new Vector();
    String sUnitCode = null;

    for (int i = 0; i < invoiceVOs.length; i++) {
      InvoiceHeaderVO headVO = invoiceVOs[i].getHeadVO();
      sUnitCode = headVO.getPk_corp();
      InvoiceItemVO[] bodyVO = invoiceVOs[i].getBodyVO();
      for (int j = 0; j < bodyVO.length; j++) {
        if (v1.contains(bodyVO[j].getCbaseid())) continue; v1.addElement(bodyVO[j].getCbaseid());
      }
    }
    String[] sTemp = new String[v1.size()];
    v1.copyInto(sTemp);

    ISysInitQry myService = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());
    String sZGYF = myService.getParaString(sUnitCode, "PO52");
    if ((sZGYF.equals("N")) || (sZGYF.equals("否"))) return null;

    HashMap hInv = new PubDMO().queryArrayValues("bd_invbasdoc", "pk_invbasdoc", new String[] { "laborflag", "discountflag" }, sTemp, " dr = 0 ");
    v1 = new Vector();
    Object oTemp = null; Object[] invFlag = (Object[])null;
    for (int i = 0; i < invoiceVOs.length; i++) {
      InvoiceItemVO[] bodyVO = invoiceVOs[i].getBodyVO();
      for (int j = 0; j < bodyVO.length; j++) {
        oTemp = hInv.get(bodyVO[j].getCbaseid());
        if (oTemp != null) {
          invFlag = (Object[])oTemp;
          if ((invFlag[0].equals("Y")) || (invFlag[1].equals("Y"))) v1.addElement(bodyVO[j]); else
            v2.addElement(bodyVO[j]);
        }
      }
    }
    if (v2.size() == 0)
    {
      return null;
    }

    Vector v22 = new Vector();
    for (int i = 0; i < v2.size(); i++) {
      InvoiceItemVO bodyVO = (InvoiceItemVO)v2.elementAt(i);
      if (v22.contains(bodyVO.getCinvoiceid())) continue; v22.addElement(bodyVO.getCinvoiceid());
    }
    String[] cinvoiceid = new String[v22.size()];
    v22.copyInto(cinvoiceid);
    Hashtable hVerifyRule = queryVerifyRuleAndBillStatus(cinvoiceid);
    if ((hVerifyRule == null) || (hVerifyRule.size() == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000072"));
    }

    v22 = new Vector();
    Vector v23 = new Vector();
    Vector v24 = new Vector();
    for (int i = 0; i < v2.size(); i++) {
      InvoiceItemVO bodyVO = (InvoiceItemVO)v2.elementAt(i);

      oTemp = hVerifyRule.get(bodyVO.getCinvoiceid());
      if (oTemp != null) {
        Object[] data = (Object[])oTemp;
        String verifyrule = (String)data[1];
        if ((verifyrule.equals("N")) || (verifyrule.equals("Z")) || (verifyrule.equals("V")))
          continue;
        if (Math.abs(bodyVO.getNaccumsettmny().doubleValue() - bodyVO.getNmoney().doubleValue()) < VMIDMO.getDigitRMB(sUnitCode).doubleValue()) {
          v22.addElement(bodyVO);
        }
        else if (Math.abs(bodyVO.getNaccumsettmny().doubleValue()) < VMIDMO.getDigitRMB(sUnitCode).doubleValue())
          v24.addElement(bodyVO);
        else {
          v23.addElement(bodyVO);
        }
      }
    }
//
//    if (v23.size() > 0)
//    {
//      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000243"));
//    }
//    if (v24.size() > 0)
//    {
//      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040401", "UPP40040401-000243"));
//    }
//
//    if (v22.size() > 0)
//    {
//      HashMap hSettle = getSettleInfoByInvoice(v22);
//      if ((hSettle == null) || (hSettle.size() == 0))
//      {
//        return null;
//      }
//
//      HashMap tZGYF = isStockZGYF(hSettle);
//      if ((tZGYF == null) || (tZGYF.size() == 0))
//      {
//        return null;
//      }
//
//      v1 = getInvoicesToWashZGYF(hSettle, tZGYF, v22);
//      if (v1.size() == 0) return null;
//
//      IAdjuestVO[] VOs = getReturnVOsToWashZGYF(v1, hSettle, v22, tZGYF);
//      return VOs;
//    }

    return null;
  }

  private HashMap getSettleInfoByInvoice(Vector v22) throws Exception
  {
    HashMap hSettle = new HashMap();
    Vector v1 = new Vector();
    for (int i = 0; i < v22.size(); i++) {
      InvoiceItemVO bodyVO = (InvoiceItemVO)v22.elementAt(i);
      v1.addElement(bodyVO.getCinvoice_bid());
    }
    String[] sTemp = new String[v1.size()];
    v1.copyInto(sTemp);
    String strIdSet = null;
    try {
      TempTableDMO dmoTmpTbl = new TempTableDMO();
      strIdSet = dmoTmpTbl.insertTempTable(sTemp, "t_pu_ps_35", "pk_pu");
      if ((strIdSet == null) || (strIdSet.trim().length() == 0))
        strIdSet = "('TempTableDMOError')";
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }

    String sql = "select cinvoice_bid,cstockrow,nsettlenum,nreasonalwastnum from po_settlebill_b where dr = 0 and cinvoice_bid in ";
    sql = sql + strIdSet;

    Connection con = null;
    PreparedStatement stmt = null;
    String cinvoice_bid = null;
    String cstockrow = null;
    Object nsettlenum = null;
    Object nreasonalwastnum = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        cinvoice_bid = rs.getString(1);
        cstockrow = rs.getString(2);
        nsettlenum = rs.getObject(3);
        nreasonalwastnum = rs.getObject(4);
        if ((cinvoice_bid == null) || (cstockrow == null))
          continue;
        ArrayList list = new ArrayList();
        if (hSettle.get(cinvoice_bid) == null) {
          list.add(new Object[] { cstockrow, nsettlenum, nsettlenum });
        } else {
          list = (ArrayList)hSettle.get(cinvoice_bid);
          list.add(new Object[] { cstockrow, nsettlenum, nsettlenum });
        }
        hSettle.put(cinvoice_bid, list);
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    return hSettle;
  }

  private HashMap isStockZGYF(HashMap hSettle) throws Exception
  {
    Vector v1 = new Vector();
    Object key = null; Object data = null; Object[] d = (Object[])null;
    ArrayList list = null;
    Set keys = hSettle.keySet();
    Object[] kKeys = new Object[keys.size()];
    keys.toArray(kKeys);
    for (int j = 0; j < kKeys.length; j++) {
      key = kKeys[j];
      data = hSettle.get(key);
      if (data != null) {
        list = (ArrayList)data;
        for (int i = 0; i < list.size(); i++) {
          d = (Object[])list.get(i);
          if ((d[0] == null) || (v1.contains(d[0]))) continue; v1.addElement(d[0]);
        }
      }
    }
    if (v1.size() == 0) return null;
    String[] sTemp = new String[v1.size()];
    v1.copyInto(sTemp);
    HashMap tZGYF = new PubDMO().queryArrayValues("arap_djfb", "ddhh", new String[] { "vouchid" }, sTemp, "dr=0");
    if ((tZGYF == null) || (tZGYF.size() == 0))
    {
      return null;
    }

    return tZGYF;
  }

  private Vector getInvoicesToWashZGYF(HashMap hSettle, HashMap tZGYF, Vector v22)
    throws Exception
  {
    Vector v1 = new Vector();
    Object data = null; Object[] d = (Object[])null; Object[] dd = (Object[])null; Object oTemp = null;
    ArrayList list = null;
    for (int i = 0; i < v22.size(); i++) {
      InvoiceItemVO bodyVO = (InvoiceItemVO)v22.elementAt(i);
      data = hSettle.get(bodyVO.getCinvoice_bid());
      if (data != null) {
        list = (ArrayList)data;
        for (int j = 0; j < list.size(); j++) {
          d = (Object[])list.get(j);
          if (d[0] != null) {
            oTemp = tZGYF.get(d[0]);
            if (oTemp != null) {
              dd = (Object[])oTemp;
              if ((dd[0] == null) || (v1.contains(bodyVO.getCinvoice_bid()))) continue; v1.addElement(bodyVO.getCinvoice_bid());
            }
          }
        }
      }
    }
    if (v1.size() == 0) return null;

    return v1;
  }

  private IAdjuestVO[] getReturnVOsToWashZGYF(Vector v1, HashMap hSettle, Vector v22, HashMap tZGYF) throws Exception {
    Vector v2 = new Vector();
    UFDouble d1 = null; UFDouble d2 = null;
    Object data = null; Object[] d = (Object[])null;
    ArrayList list = null;

    for (int i = 0; i < v1.size(); i++) {
      data = hSettle.get(v1.elementAt(i));
      if (data != null) {
        list = (ArrayList)data;
        for (int j = 0; j < list.size(); j++) {
          d = (Object[])list.get(j);
          if ((d[0] == null) || (tZGYF.get(d[0]) == null))
            continue;
          IAdjuestVO tempVO = new IAdjuestVO();
          tempVO.setCinvoice_bid((String)v1.elementAt(i));
          tempVO.setDdhh((String)d[0]);

          d1 = new UFDouble(0);
          d2 = new UFDouble(0);
          if (d[1] != null) d1 = new UFDouble(d[1].toString());
          if (d[2] != null) d2 = new UFDouble(d[2].toString());
          tempVO.setShl(d1);

          v2.addElement(tempVO);
        }
      }
    }
    if (v2.size() == 0) return null;

    IAdjuestVO[] VOs = new IAdjuestVO[v2.size()];
    v2.copyInto(VOs);
    return VOs;
  }

  public IAdjuestVO[] antiWashDataForZGYF(InvoiceVO[] invoiceVOs)
    throws Exception
  {
    Vector v1 = new Vector(); Vector v2 = new Vector(); Vector v22 = new Vector();
    String sUnitCode = null;

    for (int i = 0; i < invoiceVOs.length; i++) {
      InvoiceHeaderVO headVO = invoiceVOs[i].getHeadVO();
      sUnitCode = headVO.getPk_corp();
      InvoiceItemVO[] bodyVO = invoiceVOs[i].getBodyVO();
      for (int j = 0; j < bodyVO.length; j++) {
        if (v1.contains(bodyVO[j].getCbaseid())) continue; v1.addElement(bodyVO[j].getCbaseid());
      }
    }
    String[] sTemp = new String[v1.size()];
    v1.copyInto(sTemp);
    HashMap hInv = new PubDMO().queryArrayValues("bd_invbasdoc", "pk_invbasdoc", new String[] { "laborflag", "discountflag" }, sTemp, "dr=0");
    v1 = new Vector();
    Object oTemp = null; Object[] invFlag = (Object[])null;
    for (int i = 0; i < invoiceVOs.length; i++) {
      InvoiceItemVO[] bodyVO = invoiceVOs[i].getBodyVO();
      InvoiceHeaderVO headerVO = (InvoiceHeaderVO)invoiceVOs[i].getParentVO();
      for (int j = 0; j < bodyVO.length; j++) {
        oTemp = hInv.get(bodyVO[j].getCbaseid());
        if (oTemp != null) {
          invFlag = (Object[])oTemp;
          if ((invFlag[0].equals("Y")) || (invFlag[1].equals("Y")) || (headerVO.getIinvoicetype().intValue() == 3)) v1.addElement(bodyVO[j]); else
            v2.addElement(bodyVO[j]);
        }
        if (Math.abs(bodyVO[j].getNaccumsettmny().doubleValue() - bodyVO[j].getNmoney().doubleValue()) < VMIDMO.getDigitRMB(sUnitCode).doubleValue()) {
          v22.addElement(bodyVO[j]);
        }
      }
    }
    if ((v2.size() == 0) || (v22.size() == 0)) return null;

    HashMap hSettle = getSettleInfoByInvoice(v22);
    if ((hSettle == null) || (hSettle.size() == 0)) return null;

    v1 = new Vector();
    Object key = null; Object data = null; Object[] d = (Object[])null; Object[] dd = (Object[])null;
    Set keys = hSettle.keySet();
    Object[] kKeys = new Object[keys.size()];
    keys.toArray(kKeys);
    for (int j = 0; j < kKeys.length; j++) {
      key = kKeys[j];
      data = hSettle.get(key);
      if (data != null) {
        ArrayList list = (ArrayList)data;
        if ((list != null) && (list.size() != 0))
          for (int k = 0; k < list.size(); k++) {
            d = (Object[])list.get(k);
            if ((d[0] == null) || (v1.contains(d[0]))) continue; v1.addElement(d[0]);
          }
      }
    }
    if (v1.size() == 0) return null;
    sTemp = new String[v1.size()];
    v1.copyInto(sTemp);
    HashMap tZGYF = new PubDMO().queryArrayValues("arap_djfb", "ddhh", new String[] { "vouchid" }, sTemp, "dr=0");
    if ((tZGYF == null) || (tZGYF.size() == 0)) return null;

    v1 = new Vector();
    Vector vv = null;
    for (int i = 0; i < v22.size(); i++) {
      InvoiceItemVO bodyVO = (InvoiceItemVO)v22.elementAt(i);
      data = hSettle.get(bodyVO.getCinvoice_bid());
      if (data != null) {
        ArrayList list = (ArrayList)data;
        if ((list != null) && (list.size() != 0))
          for (int k = 0; k < list.size(); k++) {
            d = (Object[])list.get(k);
            if (d[0] != null) {
              oTemp = tZGYF.get(d[0]);
              if (oTemp != null) {
                dd = (Object[])oTemp;
                if ((dd[0] == null) || (v1.contains(bodyVO.getCinvoice_bid()))) continue; v1.addElement(bodyVO.getCinvoice_bid());
              }
            }
          }
      }
    }
    if (v1.size() == 0) return null;

    v2 = new Vector();
    UFDouble d1 = null; UFDouble d2 = null;
    for (int i = 0; i < v1.size(); i++) {
      data = hSettle.get(v1.elementAt(i));
      if (data != null) {
        ArrayList list = (ArrayList)data;
        if ((list == null) || (list.size() == 0))
          continue;
        for (int k = 0; k < list.size(); k++) {
          d = (Object[])list.get(k);
          if ((d[0] == null) || (tZGYF.get(d[0]) == null))
            continue;
          IAdjuestVO tempVO = new IAdjuestVO();
          tempVO.setCinvoice_bid((String)v1.elementAt(i));
          tempVO.setDdhh((String)d[0]);

          d1 = new UFDouble(0);
          d2 = new UFDouble(0);
          if (d[1] != null) d1 = new UFDouble(d[1].toString());
          if (d[2] != null) d2 = new UFDouble(d[2].toString());
          tempVO.setShl(d1);

          v2.addElement(tempVO);
        }
      }
    }
    IAdjuestVO[] VOs = new IAdjuestVO[v2.size()];
    v2.copyInto(VOs);
    return VOs;
  }

  public Hashtable queryVerifyRuleAndBillStatus(String[] cinvoiceid)
    throws SQLException
  {
    String sql = "select A.cinvoiceid, A.ibillstatus, B.verifyrule from po_invoice A, bd_busitype B ";
    sql = sql + "where A.cbiztype = B.pk_busitype and A.dr = 0 and A.cinvoiceid in";
    String strSetId = null;
    try {
      TempTableDMO dmoTmpTbl = new TempTableDMO();
      strSetId = 
        dmoTmpTbl.insertTempTable(cinvoiceid, "t_pu_general", "pk_pu");
      if ((strSetId == null) || (strSetId.trim().equals("()")))
        strSetId = " ('ErrorPk') ";
    }
    catch (Exception e) {
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000413") + e.getMessage());
    }
    sql = sql + strSetId;

    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable h = new Hashtable();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String s1 = rs.getString(1);
        Object o = rs.getObject(2);
        String s2 = rs.getString(3);
        if ((s1 == null) || (o == null) || (s2 == null)) continue; h.put(s1, new Object[] { o, s2 });
      }

      rs.close();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception localException1) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception localException2) {
      }
    }
    return h;
  }
}