package nc.impl.scm.so.so012;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.lock.LockBO;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.sm.createcorp.CreatecorpBO;
import nc.bs.uap.lock.PKLock;
import nc.impl.scm.so.pub.DataControlDMO;
import nc.impl.scm.so.pub.FetchValueDMO;
import nc.impl.scm.so.pub.GeneralSqlString;
import nc.impl.scm.so.so016.BalanceDMO;
import nc.impl.so.sointerface.SaleToIADMO;
import nc.itf.arap.pub.IArapBillPublic;
import nc.itf.arap.pub.IArapVerifyLogPublic;
import nc.itf.ia.bill.IBill;
import nc.itf.scm.so.back.ISOApply;
import nc.itf.so.service.ISOToSoReturn;
import nc.itf.uap.bd.currtype.ICurrtype;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.so.credit.CreditUtil;
import nc.vo.so.pub.BusiUtil;
import nc.vo.so.so012.SquareHeaderVO;
import nc.vo.so.so012.SquareItemVO;
import nc.vo.so.so012.SquareTotalVO;
import nc.vo.so.so012.SquareVO;
import nc.vo.so.so012.SquaredetailVO;
import nc.vo.so.so016.SoBalanceToArVO;

public class SquareDMO extends DataManageObject
  implements IQueryData, ISOToSoReturn
{
  SquareFactory factory = null;

  String SO_42 = null;

  public SquareDMO()
    throws NamingException, SystemException
  {
  }

  public SquareDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  private SquareFactory getSquareFactory()
  {
    if (this.factory == null) {
      return new SquareFactory();
    }
    return this.factory;
  }

  public SquareVO findByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "findByPrimaryKey", new Object[] { key });

    SquareVO vo = new SquareVO();

    SquareHeaderVO header = findHeaderByPrimaryKey(key);
    SquareItemVO[] items = null;
    if (header != null) {
      items = findItemsForHeader(header.getPrimaryKey());
    }

    vo.setParentVO(header);
    vo.setChildrenVO(items);

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "findByPrimaryKey", new Object[] { key });

    return vo;
  }

  public SquareHeaderVO findHeaderByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "findHeaderByPrimaryKey", new Object[] { key });

    String sql = "select pk_corp, vreceiptcode, creceipttype, dbilldate, ccustomerid, cbiztype, coperatorid, ccalbodyid, cwarehouseid, dmakedate, capproveid, dapprovedate, fstatus, cdeptid, cemployeeid, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, ts, dr, cfreecustid from so_square where csaleid = ?";

    SquareHeaderVO squareHeader = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        squareHeader = new SquareHeaderVO(key);

        String pk_corp = rs.getString(1);
        squareHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String vreceiptcode = rs.getString(2);
        squareHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString(3);
        squareHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String dbilldate = rs.getString(4);
        squareHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim(), false));

        String ccustomerid = rs.getString(5);
        squareHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        String cbiztype = rs.getString(6);
        squareHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        String coperatorid = rs.getString(7);
        squareHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String ccalbodyid = rs.getString(8);
        squareHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String cwarehouseid = rs.getString(9);
        squareHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String dmakedate = rs.getString(10);
        squareHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim(), false));

        String capproveid = rs.getString(11);
        squareHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

        String dapprovedate = rs.getString(12);
        squareHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim(), false));

        Integer fstatus = (Integer)rs.getObject(13);
        squareHeader.setFstatus(fstatus == null ? null : fstatus);

        String cdeptid = rs.getString(14);
        squareHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(15);
        squareHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String vdef1 = rs.getString(16);
        squareHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(17);
        squareHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(18);
        squareHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(19);
        squareHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(20);
        squareHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(21);
        squareHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(22);
        squareHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(23);
        squareHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(24);
        squareHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(25);
        squareHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String ts = rs.getString(26);
        squareHeader.setTs(ts == null ? null : new UFDateTime(ts.trim(), false));

        Integer dr = (Integer)rs.getObject(27);
        squareHeader.setDr(dr == null ? null : dr);

        String cfreecustid = rs.getString(28);
        squareHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());
      }
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
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
    afterCallMethod("nc.bs.so.altertable.SquareDMO", "findHeaderByPrimaryKey", new Object[] { key });

    return squareHeader;
  }

  public SquareItemVO findItemByPrimaryKey(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "findItemByPrimaryKey", new Object[] { key });

    String sql = "select pk_corp,csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, ts, dr, creceipttype from so_square_b where corder_bid = ?";

    SquareItemVO squareItem = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        squareItem = new SquareItemVO(key);

        String csaleid = rs.getString(1);
        squareItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String csourcebillid = rs.getString(2);
        squareItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString(3);
        squareItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinvbasdocid = rs.getString(4);
        squareItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cinventoryid = rs.getString(5);
        squareItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        BigDecimal noutnum = (BigDecimal)rs.getObject(6);
        squareItem.setNoutnum(noutnum == null ? null : new UFDouble(noutnum));

        BigDecimal nshouldoutnum = (BigDecimal)rs.getObject(7);
        squareItem.setNshouldoutnum(nshouldoutnum == null ? null : new UFDouble(nshouldoutnum));

        BigDecimal nbalancenum = (BigDecimal)rs.getObject(8);
        squareItem.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nsignnum = (BigDecimal)rs.getObject(9);
        squareItem.setNsignnum(nsignnum == null ? null : new UFDouble(nsignnum));

        String ccurrencytypeid = rs.getString(10);
        squareItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject(11);
        squareItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject(12);
        squareItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject(13);
        squareItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        String bifpaybalance = rs.getString(14);
        squareItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String cbatchid = rs.getString(15);
        squareItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject(16);
        squareItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject(17);
        squareItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject(18);
        squareItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject(19);
        squareItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject(20);
        squareItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject(21);
        squareItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject(22);
        squareItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject(23);
        squareItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject(24);
        squareItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject(25);
        squareItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject(26);
        squareItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        String cprojectid = rs.getString(27);
        squareItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString(28);
        squareItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vfree1 = rs.getString(29);
        squareItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString(30);
        squareItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString(31);
        squareItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString(32);
        squareItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString(33);
        squareItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString(34);
        squareItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(35);
        squareItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(36);
        squareItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(37);
        squareItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(38);
        squareItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(39);
        squareItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String ts = rs.getString(40);
        squareItem.setTs(ts == null ? null : new UFDateTime(ts.trim(), false));

        Integer dr = (Integer)rs.getObject(41);
        squareItem.setDr(dr == null ? null : dr);

        String creceipttype = rs.getString(41);
        squareItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());
      }
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
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
    afterCallMethod("nc.bs.so.altertable.SquareDMO", "findItemByPrimaryKey", new Object[] { key });

    return squareItem;
  }

  public CircularlyAccessibleValueObject[] queryAllBodyDataByBIDs(String[] bodykeys) throws BusinessException
  {
    CircularlyAccessibleValueObject[] vo = null;
    return vo;
  }

  public SquareItemVO[] findItemsForHeader(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeader", new Object[] { key });

    String sql = "select pk_corp,corder_bid, csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, ts, dr, creceipttype,cupreceipttype, cupbillid, cupbillbodyid, cbodywarehouseid, cpackunitid, scalefactor, nbalancemny, ncostmny, blargessflag, discountflag, laborflag, ndiscountmny, noriginalcurdiscountmny, nnetprice, ntaxnetprice, cbodycalbodyid from so_square_b where csaleid = ? and dr=0 and (bifpaybalance='N' or bifpaybalance IS NULL) ";

    SquareItemVO[] squareItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SquareItemVO squareItem = new SquareItemVO();

        String corder_bid = rs.getString("corder_bid");
        squareItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        squareItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String csourcebillid = rs.getString("csourcebillid");
        squareItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        squareItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        squareItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        squareItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        BigDecimal noutnum = (BigDecimal)rs.getObject("noutnum");
        squareItem.setNoutnum(noutnum == null ? null : new UFDouble(noutnum));

        BigDecimal nshouldoutnum = (BigDecimal)rs.getObject("nshouldoutnum");
        squareItem.setNshouldoutnum(nshouldoutnum == null ? null : new UFDouble(nshouldoutnum));

        BigDecimal nbalancenum = (BigDecimal)rs.getObject("nbalancenum");
        squareItem.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nsignnum = (BigDecimal)rs.getObject("nsignnum");
        squareItem.setNsignnum(nsignnum == null ? null : new UFDouble(nsignnum));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        squareItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        squareItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");
        squareItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");
        squareItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        String bifpaybalance = rs.getString("bifpaybalance");
        squareItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String cbatchid = rs.getString("cbatchid");
        squareItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");
        squareItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");
        squareItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        squareItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        squareItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");
        squareItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        squareItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        squareItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        squareItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject("nassistcursummny");
        squareItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject("nassistcurmny");
        squareItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject("nassistcurtaxmny");
        squareItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        String cprojectid = rs.getString("cprojectid");
        squareItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        squareItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vfree1 = rs.getString("vfree1");
        squareItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        squareItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        squareItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        squareItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        squareItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        squareItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        squareItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        squareItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        squareItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        squareItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        squareItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String ts = rs.getString("ts");
        squareItem.setTs(ts == null ? null : new UFDateTime(ts.trim(), false));

        Integer dr = (Integer)rs.getObject("dr");
        squareItem.setDr(dr == null ? null : dr);

        String creceipttype = rs.getString("creceipttype");
        squareItem.setCreceipttype(creceipttype == null ? null : creceipttype);

        String cupreceipttype = rs.getString("cupreceipttype");
        squareItem.setCupreceipttype(cupreceipttype == null ? null : cupreceipttype.trim());

        String cupbillid = rs.getString("cupbillid");
        squareItem.setCupbillid(cupbillid == null ? null : cupbillid.trim());

        String cupbillbodyid = rs.getString("cupbillbodyid");
        squareItem.setCupbillbodyid(cupbillbodyid == null ? null : cupbillbodyid.trim());

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        squareItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        String cpackunitid = rs.getString("cpackunitid");
        squareItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());

        BigDecimal scalefactor = (BigDecimal)rs.getObject("scalefactor");
        squareItem.setScalefactor(scalefactor == null ? null : new UFDouble(scalefactor));

        BigDecimal nbalancemny = (BigDecimal)rs.getObject("nbalancemny");
        squareItem.setNbalancemny(nbalancemny == null ? null : new UFDouble(nbalancemny));

        BigDecimal ncostmny = (BigDecimal)rs.getObject("ncostmny");
        squareItem.setNcostmny(ncostmny == null ? null : new UFDouble(ncostmny));

        String blargessflag = rs.getString("blargessflag");
        squareItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

        String discountflag = rs.getString("discountflag");
        squareItem.setDiscountflag(discountflag == null ? null : new UFBoolean(discountflag.trim()));

        String laborflag = rs.getString("laborflag");
        squareItem.setLaborflag(laborflag == null ? null : new UFBoolean(laborflag.trim()));

        BigDecimal ndiscountmny = (BigDecimal)rs.getObject("ndiscountmny");
        squareItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));

        BigDecimal noriginalcurdiscountmny = (BigDecimal)rs.getObject("noriginalcurdiscountmny");

        squareItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(noriginalcurdiscountmny));

        BigDecimal nnetprice = (BigDecimal)rs.getObject("nnetprice");
        squareItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));

        BigDecimal ntaxnetprice = (BigDecimal)rs.getObject("ntaxnetprice");
        squareItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));

        String cbodycalbodyid = rs.getString("cbodycalbodyid");
        squareItem.setCbodycalbodyid(cbodycalbodyid == null ? null : cbodycalbodyid);

        v.addElement(squareItem);
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
      catch (Exception e) {
      }
    }
    squareItems = new SquareItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(squareItems);
    }

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeader", new Object[] { key });

    squareItems = getScalefactor(squareItems, key);
    return squareItems;
  }

  public String insert(SquareVO vo)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "insert", new Object[] { vo });

    String key = insertHeader((SquareHeaderVO)vo.getParentVO());

    SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
    for (int i = 0; i < items.length; i++) {
      insertItem(items[i], key);
    }

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "insert", new Object[] { vo });

    return key;
  }

  public String insertHeader(SquareHeaderVO squareHeader)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "insertHeader", new Object[] { squareHeader });

    String sql = "insert into so_square(csaleid, pk_corp, vreceiptcode, creceipttype, dbilldate, ccustomerid, cbiztype, coperatorid, ccalbodyid, cwarehouseid, dmakedate, capproveid, dapprovedate, fstatus, cdeptid, cemployeeid, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, cfreecustid,bautoincomeflag) values(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      key = getOID();
      stmt.setString(1, key);

      if (squareHeader.getPk_corp() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, squareHeader.getPk_corp());
      }
      if (squareHeader.getVreceiptcode() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, squareHeader.getVreceiptcode());
      }
      if (squareHeader.getCreceipttype() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, squareHeader.getCreceipttype());
      }
      if (squareHeader.getDbilldate() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, squareHeader.getDbilldate().toString());
      }
      if (squareHeader.getCcustomerid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, squareHeader.getCcustomerid());
      }
      if (squareHeader.getCbiztype() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, squareHeader.getCbiztype());
      }
      if (squareHeader.getCoperatorid() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, squareHeader.getCoperatorid());
      }
      if (squareHeader.getCcalbodyid() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, squareHeader.getCcalbodyid());
      }
      if (squareHeader.getCwarehouseid() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, squareHeader.getCwarehouseid());
      }
      if (squareHeader.getDmakedate() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, squareHeader.getDmakedate().toString());
      }
      if (squareHeader.getCapproveid() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, squareHeader.getCapproveid());
      }
      if (squareHeader.getDapprovedate() == null)
        stmt.setNull(13, 1);
      else {
        stmt.setString(13, squareHeader.getDapprovedate().toString());
      }
      if (squareHeader.getFstatus() == null)
        stmt.setNull(14, 4);
      else {
        stmt.setInt(14, squareHeader.getFstatus().intValue());
      }
      if (squareHeader.getCdeptid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, squareHeader.getCdeptid());
      }
      if (squareHeader.getCemployeeid() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, squareHeader.getCemployeeid());
      }
      if (squareHeader.getVdef1() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, squareHeader.getVdef1());
      }
      if (squareHeader.getVdef2() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, squareHeader.getVdef2());
      }
      if (squareHeader.getVdef3() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, squareHeader.getVdef3());
      }
      if (squareHeader.getVdef4() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, squareHeader.getVdef4());
      }
      if (squareHeader.getVdef5() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, squareHeader.getVdef5());
      }
      if (squareHeader.getVdef6() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, squareHeader.getVdef6());
      }
      if (squareHeader.getVdef7() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, squareHeader.getVdef7());
      }
      if (squareHeader.getVdef8() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, squareHeader.getVdef8());
      }
      if (squareHeader.getVdef9() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, squareHeader.getVdef9());
      }
      if (squareHeader.getVdef10() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, squareHeader.getVdef10());
      }
      if (squareHeader.getCfreecustid1() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, squareHeader.getCfreecustid1());
      }
      if (squareHeader.getBautoincomeflag() == null)
        stmt.setString(28, "N");
      else {
        stmt.setString(28, squareHeader.getBautoincomeflag().toString());
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

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "insertHeader", new Object[] { squareHeader });

    return key;
  }

  public String insertItem(SquareItemVO squareItem)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "insertItem", new Object[] { squareItem });

    String sql = "insert into so_square_b(corder_bid, csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, creceipttype,cbodycalbodyid,cbodywarehouseid,pk_corp) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      key = getOID();
      stmt.setString(1, key);

      if (squareItem.getCsaleid() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, squareItem.getCsaleid());
      }
      if (squareItem.getCsourcebillid() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, squareItem.getCsourcebillid());
      }
      if (squareItem.getCsourcebillbodyid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, squareItem.getCsourcebillbodyid());
      }
      if (squareItem.getCinvbasdocid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, squareItem.getCinvbasdocid());
      }
      if (squareItem.getCinventoryid() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, squareItem.getCinventoryid());
      }
      if (squareItem.getNoutnum() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, squareItem.getNoutnum().toBigDecimal());
      }
      if (squareItem.getNshouldoutnum() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, squareItem.getNshouldoutnum().toBigDecimal());
      }
      if (squareItem.getNbalancenum() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setBigDecimal(9, squareItem.getNbalancenum().toBigDecimal());
      }
      if (squareItem.getNsignnum() == null)
        stmt.setNull(10, 4);
      else {
        stmt.setBigDecimal(10, squareItem.getNsignnum().toBigDecimal());
      }
      if (squareItem.getCcurrencytypeid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, squareItem.getCcurrencytypeid());
      }
      if (squareItem.getNoriginalcurtaxnetprice() == null)
        stmt.setNull(12, 4);
      else {
        stmt.setBigDecimal(12, squareItem.getNoriginalcurtaxnetprice().toBigDecimal());
      }
      if (squareItem.getNoriginalcurmny() == null)
        stmt.setNull(13, 4);
      else {
        stmt.setBigDecimal(13, squareItem.getNoriginalcurmny().toBigDecimal());
      }
      if (squareItem.getNoriginalcursummny() == null)
        stmt.setNull(14, 4);
      else {
        stmt.setBigDecimal(14, squareItem.getNoriginalcursummny().toBigDecimal());
      }
      if (squareItem.getBifpaybalance() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, squareItem.getBifpaybalance().toString());
      }
      if (squareItem.getCbatchid() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, squareItem.getCbatchid());
      }
      if (squareItem.getNexchangeotobrate() == null)
        stmt.setNull(17, 4);
      else {
        stmt.setBigDecimal(17, squareItem.getNexchangeotobrate().toBigDecimal());
      }
      if (squareItem.getNexchangeotoarate() == null)
        stmt.setNull(18, 4);
      else {
        stmt.setBigDecimal(18, squareItem.getNexchangeotoarate().toBigDecimal());
      }
      if (squareItem.getNtaxrate() == null)
        stmt.setNull(19, 4);
      else {
        stmt.setBigDecimal(19, squareItem.getNtaxrate().toBigDecimal());
      }
      if (squareItem.getNoriginalcurnetprice() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, squareItem.getNoriginalcurnetprice().toBigDecimal());
      }
      if (squareItem.getNoriginalcurtaxmny() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, squareItem.getNoriginalcurtaxmny().toBigDecimal());
      }
      if (squareItem.getNtaxmny() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, squareItem.getNtaxmny().toBigDecimal());
      }
      if (squareItem.getNmny() == null)
        stmt.setNull(23, 4);
      else {
        stmt.setBigDecimal(23, squareItem.getNmny().toBigDecimal());
      }
      if (squareItem.getNsummny() == null)
        stmt.setNull(24, 4);
      else {
        stmt.setBigDecimal(24, squareItem.getNsummny().toBigDecimal());
      }
      if (squareItem.getNassistcursummny() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setBigDecimal(25, squareItem.getNassistcursummny().toBigDecimal());
      }
      if (squareItem.getNassistcurmny() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setBigDecimal(26, squareItem.getNassistcurmny().toBigDecimal());
      }
      if (squareItem.getNassistcurtaxmny() == null)
        stmt.setNull(27, 4);
      else {
        stmt.setBigDecimal(27, squareItem.getNassistcurtaxmny().toBigDecimal());
      }
      if (squareItem.getCprojectid() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, squareItem.getCprojectid());
      }
      if (squareItem.getCprojectphaseid() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, squareItem.getCprojectphaseid());
      }
      if (squareItem.getVfree1() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, squareItem.getVfree1());
      }
      if (squareItem.getVfree2() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, squareItem.getVfree2());
      }
      if (squareItem.getVfree3() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, squareItem.getVfree3());
      }
      if (squareItem.getVfree4() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, squareItem.getVfree4());
      }
      if (squareItem.getVfree5() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, squareItem.getVfree5());
      }
      if (squareItem.getVdef1() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, squareItem.getVdef1());
      }
      if (squareItem.getVdef2() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, squareItem.getVdef2());
      }
      if (squareItem.getVdef3() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, squareItem.getVdef3());
      }
      if (squareItem.getVdef4() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, squareItem.getVdef4());
      }
      if (squareItem.getVdef5() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, squareItem.getVdef5());
      }
      if (squareItem.getVdef6() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, squareItem.getVdef6());
      }

      if (squareItem.getCreceipttype() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, squareItem.getCreceipttype().toString());
      }

      if (squareItem.getCbodycalbodyid() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, squareItem.getCbodycalbodyid());
      }
      if (squareItem.getCbodywarehouseid() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, squareItem.getCbodywarehouseid());
      }
      if (squareItem.getPk_corp() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, squareItem.getPk_corp());
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

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "insertItem", new Object[] { squareItem });

    return key;
  }

  public String insertItem(SquareItemVO squareItem, String foreignKey)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "insertItem", new Object[] { squareItem, foreignKey });

    squareItem.setCsaleid(foreignKey);
    String key = insertItem(squareItem);

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "insertItem", new Object[] { squareItem, foreignKey });

    return key;
  }

  public void delete(SquareVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "delete", new Object[] { vo });

    deleteItemsForHeader(((SquareHeaderVO)vo.getParentVO()).getPrimaryKey());
    deleteHeader((SquareHeaderVO)vo.getParentVO());

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "delete", new Object[] { vo });
  }

  public void deleteHeader(SquareHeaderVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "deleteHeader", new Object[] { vo });

    String sql = "delete from so_square where csaleid = ?";

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
      } catch (Exception e) {
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
    afterCallMethod("nc.bs.so.altertable.SquareDMO", "deleteHeader", new Object[] { vo });
  }

  public void deleteItem(SquareItemVO vo)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "deleteItem", new Object[] { vo });

    String sql = "delete from so_square_b where corder_bid = ?";

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
      } catch (Exception e) {
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
    afterCallMethod("nc.bs.so.altertable.SquareDMO", "deleteItem", new Object[] { vo });
  }

  public void deleteItemsForHeader(String headerKey)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "deleteItemsForHeader", new Object[] { headerKey });

    String sql = "delete from so_square_b where csaleid = ?";

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
      } catch (Exception e) {
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
    afterCallMethod("nc.bs.so.altertable.SquareDMO", "deleteItemsForHeader", new Object[] { headerKey });
  }

  public void update(SquareVO vo)
    throws SQLException, BusinessException, SystemException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "update", new Object[] { vo });

    SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
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
    updateHeader((SquareHeaderVO)vo.getParentVO());

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "update", new Object[] { vo });
  }

  public void updateHeader(SquareHeaderVO squareHeader)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "updateHeader", new Object[] { squareHeader });

    String sql = "update so_square set pk_corp = ?, vreceiptcode = ?, creceipttype = ?, dbilldate = ?, ccustomerid = ?, cbiztype = ?, coperatorid = ?, ccalbodyid = ?, cwarehouseid = ?, dmakedate = ?, capproveid = ?, dapprovedate = ?, fstatus = ?, cdeptid = ?, cemployeeid = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, vdef7 = ?, vdef8 = ?, vdef9 = ?, vdef10 = ?, cfreecustid = ? ,bautoincomeflag = ?where csaleid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (squareHeader.getPk_corp() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, squareHeader.getPk_corp());
      }
      if (squareHeader.getVreceiptcode() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, squareHeader.getVreceiptcode());
      }
      if (squareHeader.getCreceipttype() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, squareHeader.getCreceipttype());
      }
      if (squareHeader.getDbilldate() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, squareHeader.getDbilldate().toString());
      }
      if (squareHeader.getCcustomerid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, squareHeader.getCcustomerid());
      }
      if (squareHeader.getCbiztype() == null)
        stmt.setNull(6, 1);
      else {
        stmt.setString(6, squareHeader.getCbiztype());
      }
      if (squareHeader.getCoperatorid() == null)
        stmt.setNull(7, 1);
      else {
        stmt.setString(7, squareHeader.getCoperatorid());
      }
      if (squareHeader.getCcalbodyid() == null)
        stmt.setNull(8, 1);
      else {
        stmt.setString(8, squareHeader.getCcalbodyid());
      }
      if (squareHeader.getCwarehouseid() == null)
        stmt.setNull(9, 1);
      else {
        stmt.setString(9, squareHeader.getCwarehouseid());
      }
      if (squareHeader.getDmakedate() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, squareHeader.getDmakedate().toString());
      }
      if (squareHeader.getCapproveid() == null)
        stmt.setNull(11, 1);
      else {
        stmt.setString(11, squareHeader.getCapproveid());
      }
      if (squareHeader.getDapprovedate() == null)
        stmt.setNull(12, 1);
      else {
        stmt.setString(12, squareHeader.getDapprovedate().toString());
      }
      if (squareHeader.getFstatus() == null)
        stmt.setNull(13, 4);
      else {
        stmt.setInt(13, squareHeader.getFstatus().intValue());
      }
      if (squareHeader.getCdeptid() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, squareHeader.getCdeptid());
      }
      if (squareHeader.getCemployeeid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, squareHeader.getCemployeeid());
      }
      if (squareHeader.getVdef1() == null)
        stmt.setNull(16, 1);
      else {
        stmt.setString(16, squareHeader.getVdef1());
      }
      if (squareHeader.getVdef2() == null)
        stmt.setNull(17, 1);
      else {
        stmt.setString(17, squareHeader.getVdef2());
      }
      if (squareHeader.getVdef3() == null)
        stmt.setNull(18, 1);
      else {
        stmt.setString(18, squareHeader.getVdef3());
      }
      if (squareHeader.getVdef4() == null)
        stmt.setNull(19, 1);
      else {
        stmt.setString(19, squareHeader.getVdef4());
      }
      if (squareHeader.getVdef5() == null)
        stmt.setNull(20, 1);
      else {
        stmt.setString(20, squareHeader.getVdef5());
      }
      if (squareHeader.getVdef6() == null)
        stmt.setNull(21, 1);
      else {
        stmt.setString(21, squareHeader.getVdef6());
      }
      if (squareHeader.getVdef7() == null)
        stmt.setNull(22, 1);
      else {
        stmt.setString(22, squareHeader.getVdef7());
      }
      if (squareHeader.getVdef8() == null)
        stmt.setNull(23, 1);
      else {
        stmt.setString(23, squareHeader.getVdef8());
      }
      if (squareHeader.getVdef9() == null)
        stmt.setNull(24, 1);
      else {
        stmt.setString(24, squareHeader.getVdef9());
      }
      if (squareHeader.getVdef10() == null)
        stmt.setNull(25, 1);
      else {
        stmt.setString(25, squareHeader.getVdef10());
      }
      if (squareHeader.getCfreecustid1() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, squareHeader.getCfreecustid1());
      }
      if (squareHeader.getBautoincomeflag() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, squareHeader.getBautoincomeflag().toString());
      }

      stmt.setString(28, squareHeader.getPrimaryKey());

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

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "updateHeader", new Object[] { squareHeader });
  }

  public void updateItem(SquareItemVO squareItem)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "updateItem", new Object[] { squareItem });

    String sql = "update so_square_b set csaleid = ?, csourcebillid = ?, csourcebillbodyid = ?, cinvbasdocid = ?, cinventoryid = ?, noutnum = ?, nshouldoutnum = ?, nbalancenum = ?, nsignnum = ?, ccurrencytypeid = ?, noriginalcurtaxnetprice = ?, noriginalcurmny = ?, noriginalcursummny = ?, bifpaybalance = ?, cbatchid = ?, nexchangeotobrate = ?, nexchangeotoarate = ?, ntaxrate = ?, noriginalcurnetprice = ?, noriginalcurtaxmny = ?, ntaxmny = ?, nmny = ?, nsummny = ?, nassistcursummny = ?, nassistcurmny = ?, nassistcurtaxmny = ?, cprojectid = ?, cprojectphaseid = ?, vfree1 = ?, vfree2 = ?, vfree3 = ?, vfree4 = ?, vfree5 = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, creceipttype= ?, cbodycalbodyid=? , cbodywarehouseid=?,pk_corp=? where corder_bid = ?";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      if (squareItem.getCsaleid() == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, squareItem.getCsaleid());
      }
      if (squareItem.getCsourcebillid() == null)
        stmt.setNull(2, 1);
      else {
        stmt.setString(2, squareItem.getCsourcebillid());
      }
      if (squareItem.getCsourcebillbodyid() == null)
        stmt.setNull(3, 1);
      else {
        stmt.setString(3, squareItem.getCsourcebillbodyid());
      }
      if (squareItem.getCinvbasdocid() == null)
        stmt.setNull(4, 1);
      else {
        stmt.setString(4, squareItem.getCinvbasdocid());
      }
      if (squareItem.getCinventoryid() == null)
        stmt.setNull(5, 1);
      else {
        stmt.setString(5, squareItem.getCinventoryid());
      }
      if (squareItem.getNoutnum() == null)
        stmt.setNull(6, 4);
      else {
        stmt.setBigDecimal(6, squareItem.getNoutnum().toBigDecimal());
      }
      if (squareItem.getNshouldoutnum() == null)
        stmt.setNull(7, 4);
      else {
        stmt.setBigDecimal(7, squareItem.getNshouldoutnum().toBigDecimal());
      }
      if (squareItem.getNbalancenum() == null)
        stmt.setNull(8, 4);
      else {
        stmt.setBigDecimal(8, squareItem.getNbalancenum().toBigDecimal());
      }
      if (squareItem.getNsignnum() == null)
        stmt.setNull(9, 4);
      else {
        stmt.setBigDecimal(9, squareItem.getNsignnum().toBigDecimal());
      }
      if (squareItem.getCcurrencytypeid() == null)
        stmt.setNull(10, 1);
      else {
        stmt.setString(10, squareItem.getCcurrencytypeid());
      }
      if (squareItem.getNoriginalcurtaxnetprice() == null)
        stmt.setNull(11, 4);
      else {
        stmt.setBigDecimal(11, squareItem.getNoriginalcurtaxnetprice().toBigDecimal());
      }
      if (squareItem.getNoriginalcurmny() == null)
        stmt.setNull(12, 4);
      else {
        stmt.setBigDecimal(12, squareItem.getNoriginalcurmny().toBigDecimal());
      }
      if (squareItem.getNoriginalcursummny() == null)
        stmt.setNull(13, 4);
      else {
        stmt.setBigDecimal(13, squareItem.getNoriginalcursummny().toBigDecimal());
      }
      if (squareItem.getBifpaybalance() == null)
        stmt.setNull(14, 1);
      else {
        stmt.setString(14, squareItem.getBifpaybalance().toString());
      }
      if (squareItem.getCbatchid() == null)
        stmt.setNull(15, 1);
      else {
        stmt.setString(15, squareItem.getCbatchid());
      }
      if (squareItem.getNexchangeotobrate() == null)
        stmt.setNull(16, 4);
      else {
        stmt.setBigDecimal(16, squareItem.getNexchangeotobrate().toBigDecimal());
      }
      if (squareItem.getNexchangeotoarate() == null)
        stmt.setNull(17, 4);
      else {
        stmt.setBigDecimal(17, squareItem.getNexchangeotoarate().toBigDecimal());
      }
      if (squareItem.getNtaxrate() == null)
        stmt.setNull(18, 4);
      else {
        stmt.setBigDecimal(18, squareItem.getNtaxrate().toBigDecimal());
      }
      if (squareItem.getNoriginalcurnetprice() == null)
        stmt.setNull(19, 4);
      else {
        stmt.setBigDecimal(19, squareItem.getNoriginalcurnetprice().toBigDecimal());
      }
      if (squareItem.getNoriginalcurtaxmny() == null)
        stmt.setNull(20, 4);
      else {
        stmt.setBigDecimal(20, squareItem.getNoriginalcurtaxmny().toBigDecimal());
      }
      if (squareItem.getNtaxmny() == null)
        stmt.setNull(21, 4);
      else {
        stmt.setBigDecimal(21, squareItem.getNtaxmny().toBigDecimal());
      }
      if (squareItem.getNmny() == null)
        stmt.setNull(22, 4);
      else {
        stmt.setBigDecimal(22, squareItem.getNmny().toBigDecimal());
      }
      if (squareItem.getNsummny() == null)
        stmt.setNull(23, 4);
      else {
        stmt.setBigDecimal(23, squareItem.getNsummny().toBigDecimal());
      }
      if (squareItem.getNassistcursummny() == null)
        stmt.setNull(24, 4);
      else {
        stmt.setBigDecimal(24, squareItem.getNassistcursummny().toBigDecimal());
      }
      if (squareItem.getNassistcurmny() == null)
        stmt.setNull(25, 4);
      else {
        stmt.setBigDecimal(25, squareItem.getNassistcurmny().toBigDecimal());
      }
      if (squareItem.getNassistcurtaxmny() == null)
        stmt.setNull(26, 4);
      else {
        stmt.setBigDecimal(26, squareItem.getNassistcurtaxmny().toBigDecimal());
      }
      if (squareItem.getCprojectid() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, squareItem.getCprojectid());
      }
      if (squareItem.getCprojectphaseid() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, squareItem.getCprojectphaseid());
      }
      if (squareItem.getVfree1() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, squareItem.getVfree1());
      }
      if (squareItem.getVfree2() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, squareItem.getVfree2());
      }
      if (squareItem.getVfree3() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, squareItem.getVfree3());
      }
      if (squareItem.getVfree4() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, squareItem.getVfree4());
      }
      if (squareItem.getVfree5() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, squareItem.getVfree5());
      }
      if (squareItem.getVdef1() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, squareItem.getVdef1());
      }
      if (squareItem.getVdef2() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, squareItem.getVdef2());
      }
      if (squareItem.getVdef3() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, squareItem.getVdef3());
      }
      if (squareItem.getVdef4() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, squareItem.getVdef4());
      }
      if (squareItem.getVdef5() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, squareItem.getVdef5());
      }
      if (squareItem.getVdef6() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, squareItem.getVdef6());
      }

      if (squareItem.getCreceipttype() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, squareItem.getCreceipttype().toString());
      }

      if (squareItem.getCbodycalbodyid() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, squareItem.getCbodycalbodyid());
      }

      if (squareItem.getCbodywarehouseid() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, squareItem.getCbodywarehouseid());
      }

      if (squareItem.getPk_corp() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, squareItem.getPk_corp());
      }

      stmt.setString(44, squareItem.getPrimaryKey());

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

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "updateItem", new Object[] { squareItem });
  }

  public SquaredetailVO[] changeToDetailVOs(SquareVO voSource)
  {
    int iChildSize = voSource.getChildrenVO().length;
    SquaredetailVO[] voTarget = new SquaredetailVO[iChildSize];

    for (int i = 0; i < iChildSize; i++)
    {
      for (int j = 0; j < voSource.getParentVO().getAttributeNames().length; j++) {
        voTarget[i].setAttributeValue(voSource.getParentVO().getAttributeNames()[j], voSource.getParentVO().getAttributeValue(voSource.getParentVO().getAttributeNames()[j]));
      }

    }

    return null;
  }

  private SquareItemVO[] changeVectorToItemAry(Vector vecData)
  {
    SquareItemVO[] items = new SquareItemVO[vecData.size()];
    if (vecData.size() > 0) {
      vecData.copyInto(items);
    }
    return items;
  }

  public void getSystemPara(String pk_corp)
  {
    try
    {
      if (this.SO_42 == null)
        this.SO_42 = getSquareFactory().getParaString(pk_corp, "SO42");
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      SCMEnv.out("系统参数获取失败!" + e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
  }

  public boolean checkCancelData(SquareVO[] voData)
    throws SQLException, BusinessException
  {
    Hashtable ht = new Hashtable();

    for (int i = 0; i < voData.length; i++)
    {
      String scsaleid = voData[i].getParentVO().getAttributeValue("csaleid").toString();

      if (ht.containsKey(scsaleid))
      {
        int j = Integer.parseInt(ht.get(scsaleid).toString()) + voData[i].getChildrenVO().length;
        ht.put(scsaleid, Integer.toString(j));
      }
      else {
        ht.put(scsaleid, Integer.toString(voData[i].getChildrenVO().length));
      }
    }
    SquareItemVO[] items = null;
    PreparedStatement stmt = null;
    Connection con = null;
    try
    {
      con = getConnection();
      String sSql = "select count(*) from so_squaredetail where csaleid=? and dr=0";
      stmt = con.prepareStatement(sSql);

      for (int i = 0; i < voData.length; i++)
      {
        if (((SquareHeaderVO)voData[i].getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)voData[i].getParentVO()).getCsaleid());
        }

        ResultSet rs = stmt.executeQuery();
        int m = 0;
        if (rs.next())
        {
          m = rs.getInt(1);
        }

        int n = Integer.parseInt(ht.get(((SquareHeaderVO)voData[i].getParentVO()).getCsaleid()).toString());
        if (m != n) {
          throw new BusinessException("结算单需整单取消结算，请重新查询！");
        }
      }

      stmt.close();
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }

        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
      }

    }

    return true;
  }

  public boolean checkParallel(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException
  {
    SCMEnv.out("######################   检查结算并发   ####################");
    String sql = "SELECT ts from so_square WHERE csaleid= ? ";
    Connection con = getConnection();
    PreparedStatement stmt = null;
    boolean isChanged = false;
    try
    {
      SquareHeaderVO voHead = (SquareHeaderVO)(SquareHeaderVO)vo.getParentVO();
      stmt = con.prepareStatement(sql);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, voHead.getCsaleid());
      }
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        String ts = rs.getString("ts");
        SCMEnv.out("######################   检查结算单    ts：" + ts + "  ####################");
        if (!ts.equals(voHead.getTs().toString()))
          isChanged = true;
      }
      else {
        isChanged = true;
      }
    }
    finally
    {
      try {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (Exception e)
      {
        return false;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return false;
      }
    }
    if (isChanged)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000020"));
    if (vo.getChildrenVO() != null)
    {
      checkParallelBody((SquareItemVO[])(SquareItemVO[])vo.getChildrenVO(), vo.getParentVO().getPrimaryKey());
    }
    return isChanged;
  }

  public boolean checkParallelBody(SquareItemVO[] voItems, String sBillID)
    throws SQLException, SystemException, NamingException, BusinessException
  {
    String sql = "SELECT corder_bid,ts from so_square_b WHERE csaleid= ? ";
    Connection con = getConnection();
    PreparedStatement stmt = null;
    Hashtable htBodyTS = new Hashtable();
    try {
      stmt = con.prepareStatement(sql);
      if (sBillID == null)
        stmt.setNull(1, 1);
      else {
        stmt.setString(1, sBillID);
      }
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String corder_bid = rs.getString(1);
        String ts = rs.getString(2);
        if ((ts != null) && 
          (!htBodyTS.containsKey(corder_bid)))
          htBodyTS.put(corder_bid, ts);
      }
    }
    finally
    {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
        return false;
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
        return false;
      }
    }
    boolean isChanged = false;
    if (htBodyTS.size() > 0) {
      for (int i = 0; i < voItems.length; i++) {
        String itemID = voItems[i].getCorder_bid();
        String itemTS = voItems[i].getTs() == null ? "" : voItems[i].getTs().toString();
        if ((!htBodyTS.containsKey(itemID)) || 
          (itemTS.equals((String)htBodyTS.get(itemID)))) continue;
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000021"));
      }

    }

    return isChanged;
  }

  public SquareVO correctMny(SquareVO vo)
    throws SQLException
  {
    String sCorp = ((SquareHeaderVO)vo.getParentVO()).getPk_corp();
    getSystemPara(sCorp);

    if ((this.SO_42 != null) && (new UFBoolean(this.SO_42).booleanValue())) {
      return vo;
    }
    Connection con = null;
    PreparedStatement stmt = null;
    UFDouble ufZero = new UFDouble(0);

    String sSql_square = " select noriginalcurmny,noriginalcurtaxmny,noriginalcursummny,nmny,ntaxmny,nsummny,nassistcurmny,nassistcurtaxmny,nassistcursummny from so_square_b where dr=0 and  corder_bid = ? ";

    String sSql_detail = " select sum(noriginalcurmny),sum(noriginalcurtaxmny),sum(noriginalcursummny),sum(nmny),sum(ntaxmny),sum(nsummny),sum(nassistcurmny),sum(nassistcurtaxmny),sum(nassistcursummny) from so_squaredetail where dr=0 and corder_bid = ? ";

    SquareItemVO[] itemVOs = null;
    try
    {
      con = getConnection();
      if (vo != null)
      {
        itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();

        if (itemVOs != null)
        {
          for (int i = 0; i < itemVOs.length; i++)
          {
            UFDouble ufOutNum = itemVOs[i].getNoutnum() == null ? ufZero : itemVOs[i].getNoutnum();

            UFDouble ufBalanceNum = itemVOs[i].getNbalancenum() == null ? ufZero : itemVOs[i].getNbalancenum();

            UFDouble ufNewBalanceNum = itemVOs[i].getNnewbalancenum() == null ? ufZero : itemVOs[i].getNnewbalancenum();

            if (ufOutNum.compareTo(ufBalanceNum.add(ufNewBalanceNum)) != 0) {
              continue;
            }
            UFDouble ufOrMny = null;
            UFDouble ufOrTaxmny = null;
            UFDouble ufOrSummny = null;
            UFDouble ufNaMny = null;
            UFDouble ufNaTaxmny = null;
            UFDouble ufNaSummny = null;
            UFDouble ufAsMny = null;
            UFDouble ufAsTaxmny = null;
            UFDouble ufAsSummny = null;

            UFDouble ufOrMny_detail = null;
            UFDouble ufOrTaxmny_detail = null;
            UFDouble ufOrSummny_detail = null;
            UFDouble ufNaMny_detail = null;
            UFDouble ufNaTaxmny_detail = null;
            UFDouble ufNaSummny_detail = null;
            UFDouble ufAsMny_detail = null;
            UFDouble ufAsTaxmny_detail = null;
            UFDouble ufAsSummny_detail = null;

            stmt = con.prepareStatement(sSql_square);
            stmt.setString(1, itemVOs[i].getCorder_bid());
            ResultSet rs1 = stmt.executeQuery();
            if (rs1.next())
            {
              BigDecimal bOrMny = rs1.getBigDecimal(1);
              ufOrMny = bOrMny == null ? ufZero : new UFDouble(bOrMny);

              BigDecimal bOrTaxMny = rs1.getBigDecimal(2);
              ufOrTaxmny = bOrTaxMny == null ? ufZero : new UFDouble(bOrTaxMny);

              BigDecimal bOrSummny = rs1.getBigDecimal(3);
              ufOrSummny = bOrSummny == null ? ufZero : new UFDouble(bOrSummny);

              BigDecimal bNaMny = rs1.getBigDecimal(4);
              ufNaMny = bNaMny == null ? ufZero : new UFDouble(bNaMny);

              BigDecimal bNaTaxmny = rs1.getBigDecimal(5);
              ufNaTaxmny = bNaTaxmny == null ? ufZero : new UFDouble(bNaTaxmny);

              BigDecimal bNaSummny = rs1.getBigDecimal(6);
              ufNaSummny = bNaSummny == null ? ufZero : new UFDouble(bNaSummny);

              BigDecimal bAsMny = rs1.getBigDecimal(7);
              ufAsMny = bAsMny == null ? ufZero : new UFDouble(bAsMny);

              BigDecimal bAsTaxmny = rs1.getBigDecimal(8);
              ufAsTaxmny = bAsTaxmny == null ? ufZero : new UFDouble(bAsTaxmny);

              BigDecimal bAsSummny = rs1.getBigDecimal(9);
              ufAsSummny = bAsSummny == null ? ufZero : new UFDouble(bAsSummny);
            }

            stmt.close();

            stmt = con.prepareStatement(sSql_detail);
            stmt.setString(1, itemVOs[i].getCorder_bid());
            ResultSet rs2 = stmt.executeQuery();
            if (rs2.next())
            {
              BigDecimal bOrMny_detail = rs2.getBigDecimal(1);
              ufOrMny_detail = bOrMny_detail == null ? ufZero : new UFDouble(bOrMny_detail);

              BigDecimal bOrTaxmny_detail = rs2.getBigDecimal(2);
              ufOrTaxmny_detail = bOrTaxmny_detail == null ? ufZero : new UFDouble(bOrTaxmny_detail);

              BigDecimal bOrSummny_detail = rs2.getBigDecimal(3);
              ufOrSummny_detail = bOrSummny_detail == null ? ufZero : new UFDouble(bOrSummny_detail);

              BigDecimal bNaMny_detail = rs2.getBigDecimal(4);
              ufNaMny_detail = bNaMny_detail == null ? ufZero : new UFDouble(bNaMny_detail);

              BigDecimal bNaTaxmny_detail = rs2.getBigDecimal(5);
              ufNaTaxmny_detail = bNaTaxmny_detail == null ? ufZero : new UFDouble(bNaTaxmny_detail);

              BigDecimal bNaSummny_detail = rs2.getBigDecimal(6);
              ufNaSummny_detail = bNaSummny_detail == null ? ufZero : new UFDouble(bNaSummny_detail);

              BigDecimal bAsMny_detail = rs2.getBigDecimal(7);
              ufAsMny_detail = bAsMny_detail == null ? ufZero : new UFDouble(bAsMny_detail);

              BigDecimal bAsTaxmny_detail = rs2.getBigDecimal(8);
              ufAsTaxmny_detail = bAsTaxmny_detail == null ? ufZero : new UFDouble(bAsTaxmny_detail);

              BigDecimal bAsSummny_detail = rs2.getBigDecimal(9);
              ufAsSummny_detail = bAsSummny_detail == null ? ufZero : new UFDouble(bAsSummny_detail);
            }
            stmt.close();
            itemVOs[i].setNoriginalcurmny(ufOrMny.sub(ufOrMny_detail));
            itemVOs[i].setNoriginalcurtaxmny(ufOrTaxmny.sub(ufOrTaxmny_detail));
            itemVOs[i].setNoriginalcursummny(ufOrSummny.sub(ufOrSummny_detail));
            itemVOs[i].setNmny(ufNaMny.sub(ufNaMny_detail));
            itemVOs[i].setNtaxmny(ufNaTaxmny.sub(ufNaTaxmny_detail));
            itemVOs[i].setNsummny(ufNaSummny.sub(ufNaSummny_detail));
            itemVOs[i].setNassistcurmny(ufAsMny.sub(ufAsMny_detail));
            itemVOs[i].setNassistcurtaxmny(ufAsTaxmny.sub(ufAsTaxmny_detail));
            itemVOs[i].setNassistcursummny(ufAsSummny.sub(ufAsSummny_detail));
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
      catch (Exception e)
      {
      }
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    return vo;
  }

  private SquareVO findByVO(SquareVO voSource)
    throws SQLException, BusinessException
  {
    String headID = voSource.getParentVO().getPrimaryKey();
    SquareItemVO[] items = null;
    if (headID != null)
      items = findItems(headID);
    voSource.setChildrenVO(items);
    SCMEnv.out("+++++++++++++++++++++++++\t\t断点：voSource:" + voSource + "++++++++++++++++++");

    return voSource;
  }

  private SquareVO findByVOOut(SquareVO voSource)
    throws SQLException, BusinessException
  {
    String headID = voSource.getParentVO().getPrimaryKey();
    SquareItemVO[] items = null;
    if (headID != null)
      items = findItemsOut(headID);
    voSource.setChildrenVO(items);
    SCMEnv.out("+++++++++++++++++++++++++\t\t断点：voSource:" + voSource + "++++++++++++++++++");

    return voSource;
  }

  private SquareItemVO[] findItems(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeader", new Object[] { key });

    String sql = "select pk_corp,corder_bid, csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, ts, dr, creceipttype, cbodycalbodyid, cbodywarehouseid from so_square_b where csaleid = ? and bifpaybalance = 'Y' ";

    SquareItemVO[] squareItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SquareItemVO squareItem = new SquareItemVO();

        String pk_corp = rs.getString("pk_crop");
        squareItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String corder_bid = rs.getString("corder_bid");
        squareItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        squareItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String csourcebillid = rs.getString("csourcebillid");
        squareItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        squareItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        squareItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        squareItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        BigDecimal noutnum = (BigDecimal)rs.getObject("noutnum");
        squareItem.setNoutnum(noutnum == null ? null : new UFDouble(noutnum));

        BigDecimal nshouldoutnum = (BigDecimal)rs.getObject("nshouldoutnum");
        squareItem.setNshouldoutnum(nshouldoutnum == null ? null : new UFDouble(nshouldoutnum));

        BigDecimal nbalancenum = (BigDecimal)rs.getObject("nbalancenum");
        squareItem.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nsignnum = (BigDecimal)rs.getObject("nsignnum");
        squareItem.setNsignnum(nsignnum == null ? null : new UFDouble(nsignnum));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        squareItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        squareItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");
        squareItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");
        squareItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        String bifpaybalance = rs.getString("bifpaybalance");
        squareItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String cbatchid = rs.getString("cbatchid");
        squareItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");
        squareItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");
        squareItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        squareItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        squareItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");
        squareItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        squareItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        squareItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        squareItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject("nassistcursummny");
        squareItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject("nassistcurmny");
        squareItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject("nassistcurtaxmny");
        squareItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        String cprojectid = rs.getString("cprojectid");
        squareItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        squareItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vfree1 = rs.getString("vfree1");
        squareItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        squareItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        squareItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        squareItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        squareItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        squareItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        squareItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        squareItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        squareItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        squareItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        squareItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String ts = rs.getString("ts");
        squareItem.setTs(ts == null ? null : new UFDateTime(ts.trim(), false));

        Integer dr = (Integer)rs.getObject("dr");
        squareItem.setDr(dr == null ? null : dr);

        String creceipttype = rs.getString("creceipttype");
        squareItem.setCreceipttype(creceipttype == null ? null : creceipttype);

        String cbodycalbodyid = rs.getString("cbodycalbodyid");
        squareItem.setCbodycalbodyid(cbodycalbodyid == null ? null : cbodycalbodyid);

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        squareItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid);

        v.addElement(squareItem);
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
    squareItems = new SquareItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(squareItems);
    }

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeader", new Object[] { key });

    squareItems = queryInvoiceBodyData(squareItems);

    return getScalefactor(squareItems, key);
  }

  public SquareItemVO[] findItemsForHeaderByOut(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeaderByOut", new Object[] { key });

    String sql = "select pk_corp,corder_bid, csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, ts, dr, creceipttype, cbodycalbodyid, cbodywarehouseid from so_square_b where csaleid = ? and dr=0";

    SquareItemVO[] squareItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SquareItemVO squareItem = new SquareItemVO();

        String pk_corp = rs.getString("pk_corp");
        squareItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String corder_bid = rs.getString("corder_bid");
        squareItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        squareItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String csourcebillid = rs.getString("csourcebillid");
        squareItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        squareItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        squareItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        squareItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        BigDecimal noutnum = (BigDecimal)rs.getObject("noutnum");
        squareItem.setNoutnum(noutnum == null ? null : new UFDouble(noutnum));

        BigDecimal nshouldoutnum = (BigDecimal)rs.getObject("nshouldoutnum");
        squareItem.setNshouldoutnum(nshouldoutnum == null ? null : new UFDouble(nshouldoutnum));

        BigDecimal nbalancenum = (BigDecimal)rs.getObject("nbalancenum");
        squareItem.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nsignnum = (BigDecimal)rs.getObject("nsignnum");
        squareItem.setNsignnum(nsignnum == null ? null : new UFDouble(nsignnum));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        squareItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        squareItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");
        squareItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");
        squareItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        String bifpaybalance = rs.getString("bifpaybalance");
        squareItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String cbatchid = rs.getString("cbatchid");
        squareItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");
        squareItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");
        squareItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        squareItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        squareItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");
        squareItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        squareItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        squareItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        squareItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject("nassistcursummny");
        squareItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject("nassistcurmny");
        squareItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject("nassistcurtaxmny");
        squareItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        String cprojectid = rs.getString("cprojectid");
        squareItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        squareItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vfree1 = rs.getString("vfree1");
        squareItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        squareItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        squareItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        squareItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        squareItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        squareItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        squareItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        squareItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        squareItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        squareItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        squareItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String ts = rs.getString("ts");
        squareItem.setTs(ts == null ? null : new UFDateTime(ts.trim(), false));

        Integer dr = (Integer)rs.getObject("dr");
        squareItem.setDr(dr == null ? null : dr);

        String creceipttype = rs.getString("creceipttype");
        squareItem.setCreceipttype(creceipttype == null ? null : creceipttype);

        String cbodycalbodyid = rs.getString("cbodycalbodyid");
        squareItem.setCbodycalbodyid(cbodycalbodyid == null ? null : cbodycalbodyid);

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        squareItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid);

        v.addElement(squareItem);
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
    squareItems = new SquareItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(squareItems);
    }

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeaderByOut", new Object[] { key });

    return queryOutBodyData(squareItems);
  }

  private SquareItemVO[] findItemsOut(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeaderByOut", new Object[] { key });

    String sql = "select pk_corp,corder_bid, csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, ts, dr, creceipttype,  cbodycalbodyid, cbodywarehouseid from so_square_b where csaleid = ? and nbalancenum <> 0";

    SquareItemVO[] squareItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SquareItemVO squareItem = new SquareItemVO();

        String pk_corp = rs.getString("pk_corp");
        squareItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String corder_bid = rs.getString("corder_bid");
        squareItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        squareItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String csourcebillid = rs.getString("csourcebillid");
        squareItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        squareItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        squareItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        squareItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        BigDecimal noutnum = (BigDecimal)rs.getObject("noutnum");
        squareItem.setNoutnum(noutnum == null ? null : new UFDouble(noutnum));

        BigDecimal nshouldoutnum = (BigDecimal)rs.getObject("nshouldoutnum");
        squareItem.setNshouldoutnum(nshouldoutnum == null ? null : new UFDouble(nshouldoutnum));

        BigDecimal nbalancenum = (BigDecimal)rs.getObject("nbalancenum");
        squareItem.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nsignnum = (BigDecimal)rs.getObject("nsignnum");
        squareItem.setNsignnum(nsignnum == null ? null : new UFDouble(nsignnum));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        squareItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        squareItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");
        squareItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");
        squareItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        String bifpaybalance = rs.getString("bifpaybalance");
        squareItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String cbatchid = rs.getString("cbatchid");
        squareItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");
        squareItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");
        squareItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        squareItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        squareItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");
        squareItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        squareItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        squareItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        squareItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject("nassistcursummny");
        squareItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject("nassistcurmny");
        squareItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject("nassistcurtaxmny");
        squareItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        String cprojectid = rs.getString("cprojectid");
        squareItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        squareItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vfree1 = rs.getString("vfree1");
        squareItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        squareItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        squareItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        squareItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        squareItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        squareItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        squareItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        squareItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        squareItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        squareItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        squareItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String ts = rs.getString("ts");
        squareItem.setTs(ts == null ? null : new UFDateTime(ts.trim(), false));

        Integer dr = (Integer)rs.getObject("dr");
        squareItem.setDr(dr == null ? null : dr);

        String creceipttype = rs.getString("creceipttype");
        squareItem.setCreceipttype(creceipttype == null ? null : creceipttype);

        String cbodycalbodyid = rs.getString("cbodycalbodyid");
        squareItem.setCbodycalbodyid(cbodycalbodyid == null ? null : cbodycalbodyid);

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        squareItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid);

        v.addElement(squareItem);
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
    squareItems = new SquareItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(squareItems);
    }

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeaderByOut", new Object[] { key });

    return queryOutBodyData(squareItems);
  }

  public SquareItemVO[] getScalefactor(SquareItemVO[] itemsSource, String sBillID)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    String sql = "SELECT pk_invbasdoc || pk_measdoc AS Expr1, mainmeasrate FROM bd_convert WHERE (pk_invbasdoc IN (SELECT cinvbasdocid FROM so_square_b WHERE csaleid = '" + sBillID + "' and (NOT (cpackunitid IS NULL)) AND (NOT (cpackunitid = ''))))" + " AND (pk_measdoc IN " + "(SELECT cpackunitid FROM so_square_b " + "WHERE csaleid = '" + sBillID + "' and (NOT (cpackunitid IS NULL)) AND (NOT (cpackunitid = ''))))";

    Hashtable htScale = new Hashtable();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        String sHashKey = rs.getString(1);

        BigDecimal mainmeasrate = (BigDecimal)rs.getObject(2);
        if ((mainmeasrate != null) && 
          (!htScale.containsKey(sHashKey))) {
          htScale.put(sHashKey, new UFDouble(mainmeasrate));
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
      catch (Exception e) {
      }
    }
    if (htScale.size() > 0) {
      for (int i = 0; i < itemsSource.length; i++) {
        String sIndex = itemsSource[i].getCinvbasdocid() + itemsSource[i].getCpackunitid();

        if (htScale.containsKey(sIndex)) {
          itemsSource[i].setScalefactor((UFDouble)htScale.get(sIndex));
        }
      }
    }
    return itemsSource;
  }

  public SquareTotalVO[] getScalefactor(SquareTotalVO[] totalSource)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;

    if ((totalSource == null) || (totalSource.length == 0)) {
      return totalSource;
    }
    Hashtable htCsaleid = new Hashtable();
    for (int i = 0; i < totalSource.length; i++) {
      int j = 0;

      if (!htCsaleid.containsValue(totalSource[i].getAttributeValue("csaleid_h").toString()))
      {
        htCsaleid.put(new Integer(j), totalSource[i].getAttributeValue("csaleid_h"));
      }j += 1;
    }

    String sql = "SELECT pk_invbasdoc || pk_measdoc AS Expr1, mainmeasrate FROM bd_convert WHERE (pk_invbasdoc IN (SELECT cinvbasdocid FROM so_square_b WHERE csaleid =?   and (NOT (cpackunitid IS NULL)) AND (NOT (cpackunitid = '')))) AND (pk_measdoc IN (SELECT cpackunitid FROM so_square_b WHERE csaleid =?   and (NOT (cpackunitid IS NULL)) AND (NOT (cpackunitid = ''))))";

    Hashtable htScale = new Hashtable();
    try
    {
      con = getConnection();
      for (int i = 0; i < htCsaleid.size(); i++)
      {
        stmt = con.prepareStatement(sql);

        stmt.setString(1, htCsaleid.get(new Integer(i)).toString());

        stmt.setString(2, htCsaleid.get(new Integer(i)).toString());

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
          continue;
        }
        String sHashKey = rs.getString(1);

        BigDecimal mainmeasrate = (BigDecimal)rs.getObject(2);
        if (mainmeasrate == null)
          continue;
        if (htScale.containsKey(sHashKey))
          continue;
        htScale.put(sHashKey, new UFDouble(mainmeasrate));
      }

    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (Exception e)
      {
      }
      try {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e) {
      }
    }
    if (htScale.size() > 0)
    {
      for (int i = 0; i < totalSource.length; i++)
      {
        String sIndex = (totalSource[i].getAttributeValue("cinvbasdocid") == null ? null : totalSource[i].getAttributeValue("cinvbasdocid").toString()) + (totalSource[i].getAttributeValue("cpackunitid") == null ? null : totalSource[i].getAttributeValue("cpackunitid").toString());

        if (!htScale.containsKey(sIndex))
          continue;
        totalSource[i].setAttributeValue("scalefactor", (UFDouble)htScale.get(sIndex));
      }
    }

    return totalSource;
  }

  public SquareItemVO getScalefactor(SquareItemVO itemData)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    String sInvbasdoc = itemData.getCinvbasdocid();
    String sPackunitid = itemData.getCpackunitid();
    String sql = "SELECT mainmeasrate FROM bd_convert WHERE pk_invbasdoc='" + sInvbasdoc + "' AND pk_measdoc='" + sPackunitid + "' ";

    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        BigDecimal mainmeasrate = (BigDecimal)rs.getObject("mainmeasrate");
        itemData.setScalefactor(mainmeasrate == null ? null : new UFDouble(mainmeasrate));
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
    return itemData;
  }

  public String[] insertSquareVOArray(SquareVO voSource)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so012.SquaredetailDMO", "insertSquareVOArray", new Object[] { voSource });

    String sql = "insert into so_squaredetail(pk_squaredetail, corder_bid, csaleid, pk_corp, vreceiptcode, creceipttype, ctermprotocolid, verifyrule, dbilldate, ccustomerid, cbiztype, coperatorid, ccalbodyid, cwarehouseid, dmakedate, capproveid, dapprovedate, fstatus, cdeptid, cemployeeid, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, csourcereceipttype, csourcebillid, csourcebillbodyid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, nnewbalancenum, currencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vbodydef1, vbodydef2, vbodydef3, vbodydef4, vbodydef5, vbodydef6, cupreceipttype, cupbillid, cupbillbodyid, cbodywarehouseid, cpackunitid, scalefactor, nbalancemny, ncostmny, blargessflag, discountflag, laborflag, bifinventoryfinish, noriginalcurdiscountmny, nnetprice, ntaxnetprice, ndiscountmny,vdef11,vdef12,vdef13,vdef14,vdef15,vdef16,vdef17,vdef18,vdef19,vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,pk_defdoc5,pk_defdoc6,pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11, pk_defdoc12,pk_defdoc13,pk_defdoc14,pk_defdoc15,pk_defdoc16,pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20,vbodydef7,vbodydef8,vbodydef9,vbodydef10,vbodydef11,vbodydef12,vbodydef13,vbodydef14,vbodydef15,vbodydef16,vbodydef17,vbodydef18,vbodydef19,vbodydef20, pk_bodydefdoc1,pk_bodydefdoc2,pk_bodydefdoc3,pk_bodydefdoc4,pk_bodydefdoc5,pk_bodydefdoc6,pk_bodydefdoc7,pk_bodydefdoc8,pk_bodydefdoc9,pk_bodydefdoc10,pk_bodydefdoc11,pk_bodydefdoc12,pk_bodydefdoc13,pk_bodydefdoc14,pk_bodydefdoc15,pk_bodydefdoc16,pk_bodydefdoc17,pk_bodydefdoc18,pk_bodydefdoc19,pk_bodydefdoc20,carhid,carbid,ciahid,ciabid,cquoteunitid,nquoteunitnum,nquoteunitrate,nqttaxnetprc,nqtnetprc,nqttaxprc,nqtprc,norgqttaxnetprc,norgqtnetprc,norgqttaxprc,norgqtprc,clbh) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    Connection con = null;
    PreparedStatement stmt = null;
    SquareHeaderVO voHead = (SquareHeaderVO)voSource.getParentVO();
    SquareItemVO[] voBodys = (SquareItemVO[])(SquareItemVO[])voSource.getChildrenVO();
    try
    {
      con = getConnection();

      stmt = prepareStatement(con, sql);
      for (int i = 0; i < voBodys.length; i++)
      {
        stmt.setString(1, voBodys[i].getCsquareid());

        if (voBodys[i].getCorder_bid() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, voBodys[i].getCorder_bid());
        }
        if (voHead.getCsaleid() == null)
          stmt.setNull(3, 1);
        else {
          stmt.setString(3, voHead.getCsaleid());
        }
        if (voHead.getPk_corp() == null)
          stmt.setNull(4, 1);
        else {
          stmt.setString(4, voHead.getPk_corp());
        }
        if (voHead.getVreceiptcode() == null)
          stmt.setNull(5, 1);
        else {
          stmt.setString(5, voHead.getVreceiptcode());
        }
        if (voHead.getCreceipttype() == null)
          stmt.setNull(6, 1);
        else {
          stmt.setString(6, voHead.getCreceipttype());
        }
        if (voHead.getCtermprotocolid() == null)
          stmt.setNull(7, 1);
        else {
          stmt.setString(7, voHead.getCtermprotocolid());
        }
        if (voHead.getVerifyrule() == null)
          stmt.setNull(8, 1);
        else {
          stmt.setString(8, voHead.getVerifyrule());
        }
        if (voHead.getDbilldate() == null)
          stmt.setNull(9, 1);
        else {
          stmt.setString(9, voHead.getDbilldate().toString());
        }
        if (voHead.getCcustomerid() == null)
          stmt.setNull(10, 1);
        else {
          stmt.setString(10, voHead.getCcustomerid());
        }
        if (voHead.getCbiztype() == null)
          stmt.setNull(11, 1);
        else {
          stmt.setString(11, voHead.getCbiztype());
        }
        if (voHead.getCoperatorid() == null)
          stmt.setNull(12, 1);
        else {
          stmt.setString(12, voHead.getCoperatorid());
        }
        if (voHead.getCcalbodyid() == null)
          stmt.setNull(13, 1);
        else {
          stmt.setString(13, voHead.getCcalbodyid());
        }
        if (voHead.getCwarehouseid() == null)
          stmt.setNull(14, 1);
        else {
          stmt.setString(14, voHead.getCwarehouseid());
        }
        if (voHead.getDmakedate() == null)
          stmt.setNull(15, 1);
        else {
          stmt.setString(15, voHead.getDmakedate().toString());
        }
        if (voHead.getCapproveid() == null)
          stmt.setNull(16, 1);
        else {
          stmt.setString(16, voHead.getCapproveid());
        }
        if (voHead.getDapprovedate() == null)
          stmt.setNull(17, 1);
        else {
          stmt.setString(17, voHead.getDapprovedate().toString());
        }
        if (voHead.getFstatus() == null)
          stmt.setNull(18, 4);
        else {
          stmt.setInt(18, voHead.getFstatus().intValue());
        }
        if (voHead.getCdeptid() == null)
          stmt.setNull(19, 1);
        else {
          stmt.setString(19, voHead.getCdeptid());
        }
        if (voHead.getCemployeeid() == null)
          stmt.setNull(20, 1);
        else {
          stmt.setString(20, voHead.getCemployeeid());
        }
        if (voHead.getVdef1() == null)
          stmt.setNull(21, 1);
        else {
          stmt.setString(21, voHead.getVdef1());
        }
        if (voHead.getVdef2() == null)
          stmt.setNull(22, 1);
        else {
          stmt.setString(22, voHead.getVdef2());
        }
        if (voHead.getVdef3() == null)
          stmt.setNull(23, 1);
        else {
          stmt.setString(23, voHead.getVdef3());
        }
        if (voHead.getVdef4() == null)
          stmt.setNull(24, 1);
        else {
          stmt.setString(24, voHead.getVdef4());
        }
        if (voHead.getVdef5() == null)
          stmt.setNull(25, 1);
        else {
          stmt.setString(25, voHead.getVdef5());
        }
        if (voHead.getVdef6() == null)
          stmt.setNull(26, 1);
        else {
          stmt.setString(26, voHead.getVdef6());
        }
        if (voHead.getVdef7() == null)
          stmt.setNull(27, 1);
        else {
          stmt.setString(27, voHead.getVdef7());
        }
        if (voHead.getVdef8() == null)
          stmt.setNull(28, 1);
        else {
          stmt.setString(28, voHead.getVdef8());
        }
        if (voHead.getVdef9() == null)
          stmt.setNull(29, 1);
        else {
          stmt.setString(29, voHead.getVdef9());
        }
        if (voHead.getVdef10() == null)
          stmt.setNull(30, 1);
        else {
          stmt.setString(30, voHead.getVdef10());
        }

        if (voBodys[i].getCreceipttype() == null)
          stmt.setNull(31, 1);
        else {
          stmt.setString(31, voBodys[i].getCreceipttype());
        }
        if (voBodys[i].getCsourcebillid() == null)
          stmt.setNull(32, 1);
        else {
          stmt.setString(32, voBodys[i].getCsourcebillid());
        }
        if (voBodys[i].getCsourcebillbodyid() == null)
          stmt.setNull(33, 1);
        else {
          stmt.setString(33, voBodys[i].getCsourcebillbodyid());
        }
        if (voBodys[i].getCinventoryid() == null)
          stmt.setNull(34, 1);
        else {
          stmt.setString(34, voBodys[i].getCinventoryid());
        }
        if (voBodys[i].getNoutnum() == null)
          stmt.setNull(35, 4);
        else {
          stmt.setBigDecimal(35, voBodys[i].getNoutnum().toBigDecimal());
        }
        if (voBodys[i].getNshouldoutnum() == null)
          stmt.setNull(36, 4);
        else {
          stmt.setBigDecimal(36, voBodys[i].getNshouldoutnum().toBigDecimal());
        }
        if (voBodys[i].getNbalancenum() == null)
          stmt.setNull(37, 4);
        else {
          stmt.setBigDecimal(37, voBodys[i].getNbalancenum().toBigDecimal());
        }
        if (voBodys[i].getNsignnum() == null)
          stmt.setNull(38, 4);
        else {
          stmt.setBigDecimal(38, voBodys[i].getNsignnum().toBigDecimal());
        }
        if (voBodys[i].getNnewbalancenum() == null)
          stmt.setNull(39, 4);
        else {
          stmt.setBigDecimal(39, voBodys[i].getNnewbalancenum().toBigDecimal());
        }
        if (voBodys[i].getCcurrencytypeid() == null)
          stmt.setNull(40, 1);
        else {
          stmt.setString(40, voBodys[i].getCcurrencytypeid());
        }
        if (voBodys[i].getNoriginalcurtaxnetprice() == null)
          stmt.setNull(41, 4);
        else {
          stmt.setBigDecimal(41, voBodys[i].getNoriginalcurtaxnetprice().toBigDecimal());
        }
        if (voBodys[i].getNoriginalcurmny() == null)
          stmt.setNull(42, 4);
        else {
          stmt.setBigDecimal(42, voBodys[i].getNoriginalcurmny().toBigDecimal());
        }
        if (voBodys[i].getNoriginalcursummny() == null)
          stmt.setNull(43, 4);
        else {
          stmt.setBigDecimal(43, voBodys[i].getNoriginalcursummny().toBigDecimal());
        }
        if (voBodys[i].getBifpaybalance() == null)
          stmt.setNull(44, 1);
        else {
          stmt.setString(44, voBodys[i].getBifpaybalance().toString());
        }
        if (voBodys[i].getCbatchid() == null)
          stmt.setNull(45, 1);
        else {
          stmt.setString(45, voBodys[i].getCbatchid());
        }
        if (voBodys[i].getNexchangeotobrate() == null)
          stmt.setNull(46, 4);
        else {
          stmt.setBigDecimal(46, voBodys[i].getNexchangeotobrate().toBigDecimal());
        }
        if (voBodys[i].getNexchangeotoarate() == null)
          stmt.setNull(47, 4);
        else {
          stmt.setBigDecimal(47, voBodys[i].getNexchangeotoarate().toBigDecimal());
        }
        if (voBodys[i].getNtaxrate() == null)
          stmt.setNull(48, 4);
        else {
          stmt.setBigDecimal(48, voBodys[i].getNtaxrate().toBigDecimal());
        }
        if (voBodys[i].getNoriginalcurnetprice() == null)
          stmt.setNull(49, 4);
        else {
          stmt.setBigDecimal(49, voBodys[i].getNoriginalcurnetprice().toBigDecimal());
        }
        if (voBodys[i].getNoriginalcurtaxmny() == null)
          stmt.setNull(50, 4);
        else {
          stmt.setBigDecimal(50, voBodys[i].getNoriginalcurtaxmny().toBigDecimal());
        }
        if (voBodys[i].getNtaxmny() == null)
          stmt.setNull(51, 4);
        else {
          stmt.setBigDecimal(51, voBodys[i].getNtaxmny().toBigDecimal());
        }
        if (voBodys[i].getNmny() == null)
          stmt.setNull(52, 4);
        else {
          stmt.setBigDecimal(52, voBodys[i].getNmny().toBigDecimal());
        }
        if (voBodys[i].getNsummny() == null)
          stmt.setNull(53, 4);
        else {
          stmt.setBigDecimal(53, voBodys[i].getNsummny().toBigDecimal());
        }
        if (voBodys[i].getNassistcursummny() == null)
          stmt.setNull(54, 4);
        else {
          stmt.setBigDecimal(54, voBodys[i].getNassistcursummny().toBigDecimal());
        }
        if (voBodys[i].getNassistcurmny() == null)
          stmt.setNull(55, 4);
        else {
          stmt.setBigDecimal(55, voBodys[i].getNassistcurmny().toBigDecimal());
        }
        if (voBodys[i].getNassistcurtaxmny() == null)
          stmt.setNull(56, 4);
        else {
          stmt.setBigDecimal(56, voBodys[i].getNassistcurtaxmny().toBigDecimal());
        }
        if (voBodys[i].getCprojectid() == null)
          stmt.setNull(57, 1);
        else {
          stmt.setString(57, voBodys[i].getCprojectid());
        }
        if (voBodys[i].getCprojectphaseid() == null)
          stmt.setNull(58, 1);
        else {
          stmt.setString(58, voBodys[i].getCprojectphaseid());
        }
        if (voBodys[i].getVfree1() == null)
          stmt.setNull(59, 1);
        else {
          stmt.setString(59, voBodys[i].getVfree1());
        }
        if (voBodys[i].getVfree2() == null)
          stmt.setNull(60, 1);
        else {
          stmt.setString(60, voBodys[i].getVfree2());
        }
        if (voBodys[i].getVfree3() == null)
          stmt.setNull(61, 1);
        else {
          stmt.setString(61, voBodys[i].getVfree3());
        }
        if (voBodys[i].getVfree4() == null)
          stmt.setNull(62, 1);
        else {
          stmt.setString(62, voBodys[i].getVfree4());
        }
        if (voBodys[i].getVfree5() == null)
          stmt.setNull(63, 1);
        else {
          stmt.setString(63, voBodys[i].getVfree5());
        }
        if (voBodys[i].getVdef1() == null)
          stmt.setNull(64, 1);
        else {
          stmt.setString(64, voBodys[i].getVdef1());
        }
        if (voBodys[i].getVdef2() == null)
          stmt.setNull(65, 1);
        else {
          stmt.setString(65, voBodys[i].getVdef2());
        }
        if (voBodys[i].getVdef3() == null)
          stmt.setNull(66, 1);
        else {
          stmt.setString(66, voBodys[i].getVdef3());
        }
        if (voBodys[i].getVdef4() == null)
          stmt.setNull(67, 1);
        else {
          stmt.setString(67, voBodys[i].getVdef4());
        }
        if (voBodys[i].getVdef5() == null)
          stmt.setNull(68, 1);
        else {
          stmt.setString(68, voBodys[i].getVdef5());
        }
        if (voBodys[i].getVdef6() == null)
          stmt.setNull(69, 1);
        else {
          stmt.setString(69, voBodys[i].getVdef6());
        }
        if (voBodys[i].getCupreceipttype() == null)
          stmt.setNull(70, 1);
        else {
          stmt.setString(70, voBodys[i].getCupreceipttype());
        }
        if (voBodys[i].getCupbillid() == null)
          stmt.setNull(71, 1);
        else {
          stmt.setString(71, voBodys[i].getCupbillid());
        }
        if (voBodys[i].getCupbillbodyid() == null)
          stmt.setNull(72, 1);
        else {
          stmt.setString(72, voBodys[i].getCupbillbodyid());
        }
        if (voBodys[i].getCbodywarehouseid() == null)
          stmt.setNull(73, 1);
        else {
          stmt.setString(73, voBodys[i].getCbodywarehouseid());
        }
        if (voBodys[i].getCpackunitid() == null)
          stmt.setNull(74, 1);
        else {
          stmt.setString(74, voBodys[i].getCpackunitid());
        }
        if (voBodys[i].getScalefactor() == null)
          stmt.setNull(75, 4);
        else {
          stmt.setBigDecimal(75, voBodys[i].getScalefactor().toBigDecimal());
        }
        if (voBodys[i].getNbalancemny() == null)
          stmt.setNull(76, 4);
        else {
          stmt.setBigDecimal(76, voBodys[i].getNbalancemny().toBigDecimal());
        }
        if (voBodys[i].getNcostmny() == null)
          stmt.setNull(77, 4);
        else {
          stmt.setBigDecimal(77, voBodys[i].getNcostmny().toBigDecimal());
        }
        if (voBodys[i].getBlargessflag() == null)
          stmt.setNull(78, 1);
        else {
          stmt.setString(78, voBodys[i].getBlargessflag().toString());
        }
        if (voBodys[i].getDiscountflag() == null)
          stmt.setNull(79, 1);
        else {
          stmt.setString(79, voBodys[i].getDiscountflag().toString());
        }
        if (voBodys[i].getLaborflag() == null)
          stmt.setNull(80, 1);
        else {
          stmt.setString(80, voBodys[i].getLaborflag().toString());
        }
        if (voBodys[i].getBifinventoryfinish() == null)
          stmt.setNull(81, 1);
        else {
          stmt.setString(81, voBodys[i].getBifinventoryfinish().toString());
        }
        if (voBodys[i].getNoriginalcurdiscountmny() == null)
          stmt.setNull(82, 4);
        else {
          stmt.setBigDecimal(82, voBodys[i].getNoriginalcurdiscountmny().toBigDecimal());
        }
        if (voBodys[i].getNnetprice() == null)
          stmt.setNull(83, 4);
        else {
          stmt.setBigDecimal(83, voBodys[i].getNnetprice().toBigDecimal());
        }
        if (voBodys[i].getNtaxnetprice() == null)
          stmt.setNull(84, 4);
        else {
          stmt.setBigDecimal(84, voBodys[i].getNtaxnetprice().toBigDecimal());
        }
        if (voBodys[i].getNdiscountmny() == null)
          stmt.setNull(85, 4);
        else {
          stmt.setBigDecimal(85, voBodys[i].getNdiscountmny().toBigDecimal());
        }
        if (voHead.getVdef11() == null)
          stmt.setNull(86, 1);
        else {
          stmt.setString(86, voHead.getVdef11());
        }

        if (voHead.getVdef12() == null)
          stmt.setNull(87, 1);
        else {
          stmt.setString(87, voHead.getVdef12());
        }

        if (voHead.getVdef13() == null)
          stmt.setNull(88, 1);
        else {
          stmt.setString(88, voHead.getVdef13());
        }

        if (voHead.getVdef14() == null)
          stmt.setNull(89, 1);
        else {
          stmt.setString(89, voHead.getVdef14());
        }

        if (voHead.getVdef15() == null)
          stmt.setNull(90, 1);
        else {
          stmt.setString(90, voHead.getVdef15());
        }

        if (voHead.getVdef16() == null)
          stmt.setNull(91, 1);
        else {
          stmt.setString(91, voHead.getVdef16());
        }

        if (voHead.getVdef17() == null)
          stmt.setNull(92, 1);
        else {
          stmt.setString(92, voHead.getVdef17());
        }

        if (voHead.getVdef18() == null)
          stmt.setNull(93, 1);
        else {
          stmt.setString(93, voHead.getVdef18());
        }

        if (voHead.getVdef19() == null)
          stmt.setNull(94, 1);
        else {
          stmt.setString(94, voHead.getVdef19());
        }

        if (voHead.getVdef20() == null)
          stmt.setNull(95, 1);
        else {
          stmt.setString(95, voHead.getVdef20());
        }

        if (voHead.getPk_defdoc1() == null)
          stmt.setNull(96, 1);
        else {
          stmt.setString(96, voHead.getPk_defdoc1());
        }

        if (voHead.getPk_defdoc2() == null)
          stmt.setNull(97, 1);
        else {
          stmt.setString(97, voHead.getPk_defdoc2());
        }

        if (voHead.getPk_defdoc3() == null)
          stmt.setNull(98, 1);
        else {
          stmt.setString(98, voHead.getPk_defdoc3());
        }

        if (voHead.getPk_defdoc4() == null)
          stmt.setNull(99, 1);
        else {
          stmt.setString(99, voHead.getPk_defdoc4());
        }

        if (voHead.getPk_defdoc5() == null)
          stmt.setNull(100, 1);
        else {
          stmt.setString(100, voHead.getPk_defdoc5());
        }

        if (voHead.getPk_defdoc6() == null)
          stmt.setNull(101, 1);
        else {
          stmt.setString(101, voHead.getPk_defdoc6());
        }

        if (voHead.getPk_defdoc7() == null)
          stmt.setNull(102, 1);
        else {
          stmt.setString(102, voHead.getPk_defdoc7());
        }

        if (voHead.getPk_defdoc8() == null)
          stmt.setNull(103, 1);
        else {
          stmt.setString(103, voHead.getPk_defdoc8());
        }

        if (voHead.getPk_defdoc9() == null)
          stmt.setNull(104, 1);
        else {
          stmt.setString(104, voHead.getPk_defdoc9());
        }

        if (voHead.getPk_defdoc10() == null)
          stmt.setNull(105, 1);
        else {
          stmt.setString(105, voHead.getPk_defdoc10());
        }

        if (voHead.getPk_defdoc11() == null)
          stmt.setNull(106, 1);
        else {
          stmt.setString(106, voHead.getPk_defdoc11());
        }

        if (voHead.getPk_defdoc12() == null)
          stmt.setNull(107, 1);
        else {
          stmt.setString(107, voHead.getPk_defdoc12());
        }

        if (voHead.getPk_defdoc13() == null)
          stmt.setNull(108, 1);
        else {
          stmt.setString(108, voHead.getPk_defdoc13());
        }

        if (voHead.getPk_defdoc14() == null)
          stmt.setNull(109, 1);
        else {
          stmt.setString(109, voHead.getPk_defdoc14());
        }

        if (voHead.getPk_defdoc15() == null)
          stmt.setNull(110, 1);
        else {
          stmt.setString(110, voHead.getPk_defdoc15());
        }

        if (voHead.getPk_defdoc16() == null)
          stmt.setNull(111, 1);
        else {
          stmt.setString(111, voHead.getPk_defdoc16());
        }

        if (voHead.getPk_defdoc17() == null)
          stmt.setNull(112, 1);
        else {
          stmt.setString(112, voHead.getPk_defdoc17());
        }

        if (voHead.getPk_defdoc18() == null)
          stmt.setNull(113, 1);
        else {
          stmt.setString(113, voHead.getPk_defdoc18());
        }

        if (voHead.getPk_defdoc19() == null)
          stmt.setNull(114, 1);
        else {
          stmt.setString(114, voHead.getPk_defdoc19());
        }

        if (voHead.getPk_defdoc20() == null)
          stmt.setNull(115, 1);
        else {
          stmt.setString(115, voHead.getPk_defdoc20());
        }

        if (voBodys[i].getVdef7() == null)
          stmt.setNull(116, 1);
        else {
          stmt.setString(116, voBodys[i].getVdef7());
        }

        if (voBodys[i].getVdef8() == null)
          stmt.setNull(117, 1);
        else {
          stmt.setString(117, voBodys[i].getVdef8());
        }

        if (voBodys[i].getVdef9() == null)
          stmt.setNull(118, 1);
        else {
          stmt.setString(118, voBodys[i].getVdef9());
        }

        if (voBodys[i].getVdef10() == null)
          stmt.setNull(119, 1);
        else {
          stmt.setString(119, voBodys[i].getVdef10());
        }

        if (voBodys[i].getVdef11() == null)
          stmt.setNull(120, 1);
        else {
          stmt.setString(120, voBodys[i].getVdef11());
        }

        if (voBodys[i].getVdef12() == null)
          stmt.setNull(121, 1);
        else {
          stmt.setString(121, voBodys[i].getVdef12());
        }

        if (voBodys[i].getVdef13() == null)
          stmt.setNull(122, 1);
        else {
          stmt.setString(122, voBodys[i].getVdef13());
        }

        if (voBodys[i].getVdef14() == null)
          stmt.setNull(123, 1);
        else {
          stmt.setString(123, voBodys[i].getVdef14());
        }

        if (voBodys[i].getVdef15() == null)
          stmt.setNull(124, 1);
        else {
          stmt.setString(124, voBodys[i].getVdef15());
        }

        if (voBodys[i].getVdef16() == null)
          stmt.setNull(125, 1);
        else {
          stmt.setString(125, voBodys[i].getVdef16());
        }

        if (voBodys[i].getVdef17() == null)
          stmt.setNull(126, 1);
        else {
          stmt.setString(126, voBodys[i].getVdef17());
        }

        if (voBodys[i].getVdef18() == null)
          stmt.setNull(127, 1);
        else {
          stmt.setString(127, voBodys[i].getVdef18());
        }

        if (voBodys[i].getVdef19() == null)
          stmt.setNull(128, 1);
        else {
          stmt.setString(128, voBodys[i].getVdef19());
        }

        if (voBodys[i].getVdef20() == null)
          stmt.setNull(129, 1);
        else {
          stmt.setString(129, voBodys[i].getVdef20());
        }

        if (voBodys[i].getPk_defdoc1() == null)
          stmt.setNull(130, 1);
        else {
          stmt.setString(130, voBodys[i].getPk_defdoc1());
        }

        if (voBodys[i].getPk_defdoc2() == null)
          stmt.setNull(131, 1);
        else {
          stmt.setString(131, voBodys[i].getPk_defdoc2());
        }

        if (voBodys[i].getPk_defdoc3() == null)
          stmt.setNull(132, 1);
        else {
          stmt.setString(132, voBodys[i].getPk_defdoc3());
        }

        if (voBodys[i].getPk_defdoc4() == null)
          stmt.setNull(133, 1);
        else {
          stmt.setString(133, voBodys[i].getPk_defdoc4());
        }

        if (voBodys[i].getPk_defdoc5() == null)
          stmt.setNull(134, 1);
        else {
          stmt.setString(134, voBodys[i].getPk_defdoc5());
        }

        if (voBodys[i].getPk_defdoc6() == null)
          stmt.setNull(135, 1);
        else {
          stmt.setString(135, voBodys[i].getPk_defdoc6());
        }

        if (voBodys[i].getPk_defdoc7() == null)
          stmt.setNull(136, 1);
        else {
          stmt.setString(136, voBodys[i].getPk_defdoc7());
        }

        if (voBodys[i].getPk_defdoc8() == null)
          stmt.setNull(137, 1);
        else {
          stmt.setString(137, voBodys[i].getPk_defdoc8());
        }

        if (voBodys[i].getPk_defdoc9() == null)
          stmt.setNull(138, 1);
        else {
          stmt.setString(138, voBodys[i].getPk_defdoc9());
        }

        if (voBodys[i].getPk_defdoc10() == null)
          stmt.setNull(139, 1);
        else {
          stmt.setString(139, voBodys[i].getPk_defdoc10());
        }

        if (voBodys[i].getPk_defdoc11() == null)
          stmt.setNull(140, 1);
        else {
          stmt.setString(140, voBodys[i].getPk_defdoc11());
        }

        if (voBodys[i].getPk_defdoc12() == null)
          stmt.setNull(141, 1);
        else {
          stmt.setString(141, voBodys[i].getPk_defdoc12());
        }

        if (voBodys[i].getPk_defdoc13() == null)
          stmt.setNull(142, 1);
        else {
          stmt.setString(142, voBodys[i].getPk_defdoc13());
        }

        if (voBodys[i].getPk_defdoc14() == null)
          stmt.setNull(143, 1);
        else {
          stmt.setString(143, voBodys[i].getPk_defdoc14());
        }

        if (voBodys[i].getPk_defdoc15() == null)
          stmt.setNull(144, 1);
        else {
          stmt.setString(144, voBodys[i].getPk_defdoc15());
        }

        if (voBodys[i].getPk_defdoc16() == null)
          stmt.setNull(145, 1);
        else {
          stmt.setString(145, voBodys[i].getPk_defdoc16());
        }

        if (voBodys[i].getPk_defdoc17() == null)
          stmt.setNull(146, 1);
        else {
          stmt.setString(146, voBodys[i].getPk_defdoc17());
        }

        if (voBodys[i].getPk_defdoc18() == null)
          stmt.setNull(147, 1);
        else {
          stmt.setString(147, voBodys[i].getPk_defdoc18());
        }

        if (voBodys[i].getPk_defdoc19() == null)
          stmt.setNull(148, 1);
        else {
          stmt.setString(148, voBodys[i].getPk_defdoc19());
        }

        if (voBodys[i].getPk_defdoc20() == null)
          stmt.setNull(149, 1);
        else {
          stmt.setString(149, voBodys[i].getPk_defdoc20());
        }

        if (voHead.getCarhid() == null)
          stmt.setNull(150, 1);
        else {
          stmt.setString(150, voHead.getCarhid());
        }

        if (voBodys[i].getCarbid() == null)
          stmt.setNull(151, 1);
        else {
          stmt.setString(151, voBodys[i].getCarbid());
        }

        if (voBodys[i].getCiahid() == null)
          stmt.setNull(152, 1);
        else {
          stmt.setString(152, voBodys[i].getCiahid());
        }

        if (voBodys[i].getCiabid() == null)
          stmt.setNull(153, 1);
        else {
          stmt.setString(153, voBodys[i].getCiabid());
        }

        if (voBodys[i].getCquoteunitid() == null)
          stmt.setNull(154, 1);
        else {
          stmt.setString(154, voBodys[i].getCquoteunitid());
        }

        if (voBodys[i].getNquoteunitnum() == null)
          stmt.setNull(155, 4);
        else {
          stmt.setBigDecimal(155, voBodys[i].getNquoteunitnum().toBigDecimal());
        }

        if (voBodys[i].getNquoteunitrate() == null)
          stmt.setNull(156, 4);
        else {
          stmt.setBigDecimal(156, voBodys[i].getNquoteunitrate().toBigDecimal());
        }

        if (voBodys[i].getNqttaxnetprc() == null)
          stmt.setNull(157, 4);
        else {
          stmt.setBigDecimal(157, voBodys[i].getNqttaxnetprc().toBigDecimal());
        }

        if (voBodys[i].getNqtnetprc() == null)
          stmt.setNull(158, 4);
        else {
          stmt.setBigDecimal(158, voBodys[i].getNqtnetprc().toBigDecimal());
        }

        if (voBodys[i].getNqttaxprc() == null)
          stmt.setNull(159, 4);
        else {
          stmt.setBigDecimal(159, voBodys[i].getNqttaxprc().toBigDecimal());
        }

        if (voBodys[i].getNqtprc() == null)
          stmt.setNull(160, 4);
        else {
          stmt.setBigDecimal(160, voBodys[i].getNqtprc().toBigDecimal());
        }

        if (voBodys[i].getNorgqttaxnetprc() == null)
          stmt.setNull(161, 4);
        else {
          stmt.setBigDecimal(161, voBodys[i].getNorgqttaxnetprc().toBigDecimal());
        }

        if (voBodys[i].getNorgqtnetprc() == null)
          stmt.setNull(162, 4);
        else {
          stmt.setBigDecimal(162, voBodys[i].getNorgqtnetprc().toBigDecimal());
        }

        if (voBodys[i].getNorgqttaxprc() == null)
          stmt.setNull(163, 4);
        else {
          stmt.setBigDecimal(163, voBodys[i].getNorgqttaxprc().toBigDecimal());
        }
        if (voBodys[i].getNorgqtprc() == null)
          stmt.setNull(164, 4);
        else {
          stmt.setBigDecimal(164, voBodys[i].getNorgqtprc().toBigDecimal());
        }

        if (voBodys[i].getClbh() == null)
          stmt.setNull(165, 1);
        else {
          stmt.setString(165, voBodys[i].getClbh());
        }

        executeUpdate(stmt);
      }
      if (stmt != null)
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

    afterCallMethod("nc.bs.so.so012.SquaredetailDMO", "insertSquareVOArray", new Object[] { voSource });

    return null;
  }

  private SquareVO makeBodyWarehouseID(SquareVO voSource)
    throws Exception
  {
    if (voSource == null)
      return null;
    String sBillType = ((SquareHeaderVO)voSource.getParentVO()).getCreceipttype() == null ? "" : ((SquareHeaderVO)voSource.getParentVO()).getCreceipttype();

    SquareHeaderVO headSource = (SquareHeaderVO)voSource.getParentVO();
    SquareItemVO[] itemsSource = (SquareItemVO[])(SquareItemVO[])voSource.getChildrenVO();

    if (sBillType.equals("4C"))
    {
      if ((headSource.getCwarehouseid() != null) && (headSource.getCwarehouseid().length() != 0))
      {
        for (int i = 0; i < itemsSource.length; i++)
        {
          itemsSource[i].setCbodywarehouseid(headSource.getCwarehouseid());
        }
      }
    }

    return voSource;
  }

  public ArrayList manageSquareVO(SquareVO voSource, UFDate date)
    throws Exception
  {
    SquareItemVO[] voBodys = (SquareItemVO[])(SquareItemVO[])voSource.getChildrenVO();
    String[] keys = getOIDs(voBodys.length);
    for (int i = 0; i < keys.length; i++)
    {
      voBodys[i].setCsquareid(keys[i]);
    }

    voSource = updateSquare(voSource, date);

    voSource = makeBodyWarehouseID(voSource);

    voSource = setIsCalInvCost(voSource);

    SquareHeaderVO ArHVO = (SquareHeaderVO)voSource.getParentVO().clone();
    SquareHeaderVO IaHVO = (SquareHeaderVO)ArHVO.clone();

    SquareVO voOnlyAR = new SquareVO();
    SquareVO voOnlyIA = new SquareVO();

    voOnlyAR.setParentVO(ArHVO);
    voOnlyIA.setParentVO(IaHVO);

    if (voSource == null)
      return null;
    String sBillType = ((SquareHeaderVO)voSource.getParentVO()).getCreceipttype() == null ? "" : ((SquareHeaderVO)voSource.getParentVO()).getCreceipttype();

    SquareItemVO[] itemsSource = (SquareItemVO[])(SquareItemVO[])voSource.getChildrenVO();

    UFDouble dZero = new UFDouble(0);

    if ((sBillType.equals("4C")) || (sBillType.equals("4453")))
    {
      Object oTemp = ((SquareHeaderVO)voSource.getParentVO()).getVerifyrule();

      if (!(oTemp == null ? "" : oTemp).equals("F")) { if (!(oTemp == null ? "" : oTemp).equals("W")); } else {
        Vector vecTemp = new Vector();
        for (int i = 0; i < itemsSource.length; i++)
        {
          boolean discountFlag = itemsSource[i].getDiscountflag() == null ? false : itemsSource[i].getDiscountflag().booleanValue();

          boolean laborFlag = itemsSource[i].getLaborflag() == null ? false : itemsSource[i].getLaborflag().booleanValue();

          boolean bIsCalinvCost = itemsSource[i].getIscalculatedinvcost() == null ? false : itemsSource[i].getIscalculatedinvcost().booleanValue();

          if ((discountFlag == true) || (laborFlag == true) || (!bIsCalinvCost))
            continue;
          vecTemp.addElement(itemsSource[i]);
        }
        voOnlyIA.setChildrenVO(changeVectorToItemAry(vecTemp));
        ;
      }

      Vector vecOnlyAR = new Vector();
      Vector vecOnlyIA = new Vector();
      for (int i = 0; i < itemsSource.length; i++)
      {
        boolean largessFlag = itemsSource[i].getBlargessflag() == null ? false : itemsSource[i].getBlargessflag().booleanValue();
        boolean discountFlag = itemsSource[i].getDiscountflag() == null ? false : itemsSource[i].getDiscountflag().booleanValue();

        boolean laborFlag = itemsSource[i].getLaborflag() == null ? false : itemsSource[i].getLaborflag().booleanValue();

        boolean bIsCalinvCost = itemsSource[i].getIscalculatedinvcost() == null ? false : itemsSource[i].getIscalculatedinvcost().booleanValue();

        UFDouble dOut = itemsSource[i].getNoutnum();

        UFDouble dMny = itemsSource[i].getNmny() == null ? dZero : itemsSource[i].getNmny();

        if ((dOut == null) || (dOut.compareTo(new UFDouble(0)) == 0)) {
          continue;
        }
        if (((discountFlag) || (laborFlag) || (!bIsCalinvCost)) && ((largessFlag) || (dMny.compareTo(dZero) == 0)))
        {
          continue;
        }

        if ((largessFlag) || (dMny.compareTo(dZero) == 0))
        {
          vecOnlyIA.addElement(itemsSource[i]);
        }
        else if ((discountFlag) || (laborFlag) || (!bIsCalinvCost))
        {
          vecOnlyAR.addElement(itemsSource[i]);
        }
        else
        {
          vecOnlyAR.addElement(itemsSource[i]);
          vecOnlyIA.addElement(itemsSource[i]);
        }

      }

      ((SquareHeaderVO)voOnlyAR.getParentVO()).setVreceiptcode("");

      voOnlyAR.setChildrenVO(changeVectorToItemAry(vecOnlyAR));
      voOnlyIA.setChildrenVO(changeVectorToItemAry(vecOnlyIA));
    }
    else
    {
     Vector vecOnlyAR = new Vector();
      Vector vecOnlyIA = new Vector();
      for (int i = 0; i < itemsSource.length; i++)
      {
        boolean largessFlag = itemsSource[i].getBlargessflag() == null ? false : itemsSource[i].getBlargessflag().booleanValue();
        boolean discountFlag = itemsSource[i].getDiscountflag() == null ? false : itemsSource[i].getDiscountflag().booleanValue();

        boolean laborFlag = itemsSource[i].getLaborflag() == null ? false : itemsSource[i].getLaborflag().booleanValue();

        boolean bIsCalinvCost = itemsSource[i].getIscalculatedinvcost() == null ? false : itemsSource[i].getIscalculatedinvcost().booleanValue();

        UFDouble dMny = itemsSource[i].getNmny() == null ? dZero : itemsSource[i].getNmny();

        if (((discountFlag) || (laborFlag) || (!bIsCalinvCost)) && ((largessFlag) || (dMny.compareTo(dZero) == 0)))
        {
          continue;
        }

        if ((largessFlag) || (dMny.compareTo(dZero) == 0))
        {
          vecOnlyIA.addElement(itemsSource[i]);
        }
        else if ((discountFlag) || (laborFlag) || (!bIsCalinvCost))
        {
          vecOnlyAR.addElement(itemsSource[i]);
        }
        else
        {
          vecOnlyAR.addElement(itemsSource[i]);
          vecOnlyIA.addElement(itemsSource[i]);
        }
      }
      voOnlyAR.setChildrenVO(changeVectorToItemAry(vecOnlyAR));
      voOnlyIA.setChildrenVO(changeVectorToItemAry(vecOnlyIA));
    }
    ArrayList ret = new ArrayList(2);
    ret.add(0, voOnlyAR);
    ret.add(1, voOnlyIA);
    return ret;
  }

  public ArrayList manageSquareCancelVO(SquareVO voSource)
    throws Exception
  {
    voSource = setIsCalInvCost(voSource);

    SquareHeaderVO ArHVO = (SquareHeaderVO)voSource.getParentVO().clone();
    SquareHeaderVO IaHVO = (SquareHeaderVO)ArHVO.clone();

    SquareVO voOnlyAR = new SquareVO();
    SquareVO voOnlyIA = new SquareVO();

    voOnlyAR.setParentVO(ArHVO);
    voOnlyIA.setParentVO(IaHVO);

    if (voSource == null)
      return null;
    String sBillType = ((SquareHeaderVO)voSource.getParentVO()).getCreceipttype() == null ? "" : ((SquareHeaderVO)voSource.getParentVO()).getCreceipttype();

    SquareItemVO[] itemsSource = (SquareItemVO[])(SquareItemVO[])voSource.getChildrenVO();

    UFDouble dZero = new UFDouble(0);

    if ((sBillType.equals("4C")) || (sBillType.equals("4453")))
    {
      Object oTemp = ((SquareHeaderVO)voSource.getParentVO()).getVerifyrule();

      if (!(oTemp == null ? "" : oTemp).equals("F")) { if (!(oTemp == null ? "" : oTemp).equals("W")); } else {
        Vector vecTemp = new Vector();
        for (int i = 0; i < itemsSource.length; i++)
        {
          boolean discountFlag = itemsSource[i].getDiscountflag() == null ? false : itemsSource[i].getDiscountflag().booleanValue();

          boolean laborFlag = itemsSource[i].getLaborflag() == null ? false : itemsSource[i].getLaborflag().booleanValue();

          boolean bIsCalinvCost = itemsSource[i].getIscalculatedinvcost() == null ? false : itemsSource[i].getIscalculatedinvcost().booleanValue();

          if ((discountFlag == true) || (laborFlag == true) || (!bIsCalinvCost))
            continue;
          vecTemp.addElement(itemsSource[i]);
        }
        voOnlyIA.setChildrenVO(changeVectorToItemAry(vecTemp));
      //  break ;
      }

      Vector vecOnlyAR = new Vector();
      Vector vecOnlyIA = new Vector();
      for (int i = 0; i < itemsSource.length; i++)
      {
        boolean largessFlag = itemsSource[i].getBlargessflag() == null ? false : itemsSource[i].getBlargessflag().booleanValue();
        boolean discountFlag = itemsSource[i].getDiscountflag() == null ? false : itemsSource[i].getDiscountflag().booleanValue();

        boolean laborFlag = itemsSource[i].getLaborflag() == null ? false : itemsSource[i].getLaborflag().booleanValue();

        boolean bIsCalinvCost = itemsSource[i].getIscalculatedinvcost() == null ? false : itemsSource[i].getIscalculatedinvcost().booleanValue();

        UFDouble dOut = itemsSource[i].getNoutnum();

        UFDouble dMny = itemsSource[i].getNmny() == null ? dZero : itemsSource[i].getNmny();

        if ((dOut == null) || (dOut.compareTo(new UFDouble(0)) == 0)) {
          continue;
        }
        if (((discountFlag) || (laborFlag) || (!bIsCalinvCost)) && ((largessFlag) || (dMny.compareTo(dZero) == 0)))
        {
          continue;
        }

        if ((largessFlag) || (dMny.compareTo(dZero) == 0))
        {
          vecOnlyIA.addElement(itemsSource[i]);
        }
        else if ((discountFlag) || (laborFlag) || (!bIsCalinvCost))
        {
          vecOnlyAR.addElement(itemsSource[i]);
        }
        else
        {
          vecOnlyAR.addElement(itemsSource[i]);
          vecOnlyIA.addElement(itemsSource[i]);
        }
      }

      voOnlyAR.setChildrenVO(changeVectorToItemAry(vecOnlyAR));
      voOnlyIA.setChildrenVO(changeVectorToItemAry(vecOnlyIA));
    }
    else
    {
       Vector vecOnlyAR = new Vector();
      Vector vecOnlyIA = new Vector();
      for (int i = 0; i < itemsSource.length; i++)
      {
        boolean largessFlag = itemsSource[i].getBlargessflag() == null ? false : itemsSource[i].getBlargessflag().booleanValue();
        boolean discountFlag = itemsSource[i].getDiscountflag() == null ? false : itemsSource[i].getDiscountflag().booleanValue();

        boolean laborFlag = itemsSource[i].getLaborflag() == null ? false : itemsSource[i].getLaborflag().booleanValue();

        boolean bIsCalinvCost = itemsSource[i].getIscalculatedinvcost() == null ? false : itemsSource[i].getIscalculatedinvcost().booleanValue();

        UFDouble dMny = itemsSource[i].getNmny() == null ? dZero : itemsSource[i].getNmny();

        if (((discountFlag) || (laborFlag) || (!bIsCalinvCost)) && ((largessFlag) || (dMny.compareTo(dZero) == 0)))
        {
          continue;
        }

        if ((largessFlag) || (dMny.compareTo(dZero) == 0))
        {
          vecOnlyIA.addElement(itemsSource[i]);
        }
        else if ((discountFlag) || (laborFlag) || (!bIsCalinvCost))
        {
          vecOnlyAR.addElement(itemsSource[i]);
        }
        else
        {
          vecOnlyAR.addElement(itemsSource[i]);
          vecOnlyIA.addElement(itemsSource[i]);
        }
      }
      voOnlyAR.setChildrenVO(changeVectorToItemAry(vecOnlyAR));
      voOnlyIA.setChildrenVO(changeVectorToItemAry(vecOnlyIA));
    }
    ArrayList ret = new ArrayList(2);
    ret.add(0, voOnlyAR);
    ret.add(1, voOnlyIA);
    return ret;
  }

  void newMethod()
  {
  }

  public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeader", new Object[] { key });

    String sql = "select pk_corp,corder_bid, csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, ts, dr, creceipttype,cbodycalbodyid,cbodywarehouseid from so_square_b where csaleid = ? and dr=0";

    SquareItemVO[] squareItems = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SquareItemVO squareItem = new SquareItemVO();

        String pk_corp = rs.getString("pk_corp");
        squareItem.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String corder_bid = rs.getString("corder_bid");
        squareItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());

        String csaleid = rs.getString("csaleid");
        squareItem.setCsaleid(csaleid == null ? null : csaleid.trim());

        String csourcebillid = rs.getString("csourcebillid");
        squareItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());

        String csourcebillbodyid = rs.getString("csourcebillbodyid");
        squareItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());

        String cinvbasdocid = rs.getString("cinvbasdocid");
        squareItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());

        String cinventoryid = rs.getString("cinventoryid");
        squareItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());

        BigDecimal noutnum = (BigDecimal)rs.getObject("noutnum");
        squareItem.setNoutnum(noutnum == null ? null : new UFDouble(noutnum));

        BigDecimal nshouldoutnum = (BigDecimal)rs.getObject("nshouldoutnum");
        squareItem.setNshouldoutnum(nshouldoutnum == null ? null : new UFDouble(nshouldoutnum));

        BigDecimal nbalancenum = (BigDecimal)rs.getObject("nbalancenum");
        squareItem.setNbalancenum(nbalancenum == null ? null : new UFDouble(nbalancenum));

        BigDecimal nsignnum = (BigDecimal)rs.getObject("nsignnum");
        squareItem.setNsignnum(nsignnum == null ? null : new UFDouble(nsignnum));

        String ccurrencytypeid = rs.getString("ccurrencytypeid");
        squareItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

        BigDecimal noriginalcurtaxnetprice = (BigDecimal)rs.getObject("noriginalcurtaxnetprice");

        squareItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(noriginalcurtaxnetprice));

        BigDecimal noriginalcurmny = (BigDecimal)rs.getObject("noriginalcurmny");
        squareItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

        BigDecimal noriginalcursummny = (BigDecimal)rs.getObject("noriginalcursummny");
        squareItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

        String bifpaybalance = rs.getString("bifpaybalance");
        squareItem.setBifpaybalance(bifpaybalance == null ? null : new UFBoolean(bifpaybalance.trim()));

        String cbatchid = rs.getString("cbatchid");
        squareItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

        BigDecimal nexchangeotobrate = (BigDecimal)rs.getObject("nexchangeotobrate");
        squareItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

        BigDecimal nexchangeotoarate = (BigDecimal)rs.getObject("nexchangeotoarate");
        squareItem.setNexchangeotoarate(nexchangeotoarate == null ? null : new UFDouble(nexchangeotoarate));

        BigDecimal ntaxrate = (BigDecimal)rs.getObject("ntaxrate");
        squareItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));

        BigDecimal noriginalcurnetprice = (BigDecimal)rs.getObject("noriginalcurnetprice");

        squareItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(noriginalcurnetprice));

        BigDecimal noriginalcurtaxmny = (BigDecimal)rs.getObject("noriginalcurtaxmny");
        squareItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));

        BigDecimal ntaxmny = (BigDecimal)rs.getObject("ntaxmny");
        squareItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));

        BigDecimal nmny = (BigDecimal)rs.getObject("nmny");
        squareItem.setNmny(nmny == null ? null : new UFDouble(nmny));

        BigDecimal nsummny = (BigDecimal)rs.getObject("nsummny");
        squareItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));

        BigDecimal nassistcursummny = (BigDecimal)rs.getObject("nassistcursummny");
        squareItem.setNassistcursummny(nassistcursummny == null ? null : new UFDouble(nassistcursummny));

        BigDecimal nassistcurmny = (BigDecimal)rs.getObject("nassistcurmny");
        squareItem.setNassistcurmny(nassistcurmny == null ? null : new UFDouble(nassistcurmny));

        BigDecimal nassistcurtaxmny = (BigDecimal)rs.getObject("nassistcurtaxmny");
        squareItem.setNassistcurtaxmny(nassistcurtaxmny == null ? null : new UFDouble(nassistcurtaxmny));

        String cprojectid = rs.getString("cprojectid");
        squareItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());

        String cprojectphaseid = rs.getString("cprojectphaseid");
        squareItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());

        String vfree1 = rs.getString("vfree1");
        squareItem.setVfree1(vfree1 == null ? null : vfree1.trim());

        String vfree2 = rs.getString("vfree2");
        squareItem.setVfree2(vfree2 == null ? null : vfree2.trim());

        String vfree3 = rs.getString("vfree3");
        squareItem.setVfree3(vfree3 == null ? null : vfree3.trim());

        String vfree4 = rs.getString("vfree4");
        squareItem.setVfree4(vfree4 == null ? null : vfree4.trim());

        String vfree5 = rs.getString("vfree5");
        squareItem.setVfree5(vfree5 == null ? null : vfree5.trim());

        String vdef1 = rs.getString("vdef1");
        squareItem.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString("vdef2");
        squareItem.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString("vdef3");
        squareItem.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString("vdef4");
        squareItem.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString("vdef5");
        squareItem.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString("vdef6");
        squareItem.setVdef6(vdef6 == null ? null : vdef6.trim());

        String ts = rs.getString("ts");
        squareItem.setTs(ts == null ? null : new UFDateTime(ts.trim(), false));

        Integer dr = (Integer)rs.getObject("dr");
        squareItem.setDr(dr == null ? null : dr);

        String creceipttype = rs.getString("creceipttype");
        squareItem.setCreceipttype(creceipttype == null ? null : creceipttype);

        String cbodycalbodyid = rs.getString("cbodycalbodyid");
        squareItem.setCbodycalbodyid(cbodycalbodyid == null ? null : cbodycalbodyid);

        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        squareItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid);

        v.addElement(squareItem);
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
      catch (Exception e) {
      }
    }
    squareItems = new SquareItemVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(squareItems);
    }

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "findItemsForHeader", new Object[] { key });

    return squareItems;
  }

  public CircularlyAccessibleValueObject[] queryAllHeadData(String key)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "queryAllHeadData", new Object[] { key });

    String sql = "select pk_corp, vreceiptcode, creceipttype, dbilldate, ccustomerid, cbiztype, coperatorid, ccalbodyid, cwarehouseid, dmakedate, capproveid, dapprovedate, fstatus, cdeptid, cemployeeid, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, ts, dr, csaleid, ctermprotocolid, verifyrule, cfreecustid  from so_square where " + key;

    SquareHeaderVO[] allHeader = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SquareHeaderVO squareHeader = null;
        squareHeader = new SquareHeaderVO(key);

        String pk_corp = rs.getString(1);
        squareHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String vreceiptcode = rs.getString(2);
        squareHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString(3);
        squareHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String dbilldate = rs.getString(4);
        squareHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim(), false));

        String ccustomerid = rs.getString(5);
        squareHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        String cbiztype = rs.getString(6);
        squareHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        String coperatorid = rs.getString(7);
        squareHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String ccalbodyid = rs.getString(8);
        squareHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String cwarehouseid = rs.getString(9);
        squareHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String dmakedate = rs.getString(10);
        squareHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim(), false));

        String capproveid = rs.getString(11);
        squareHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

        String dapprovedate = rs.getString(12);
        squareHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim(), false));

        Integer fstatus = (Integer)rs.getObject(13);
        squareHeader.setFstatus(fstatus == null ? null : fstatus);

        String cdeptid = rs.getString(14);
        squareHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(15);
        squareHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String vdef1 = rs.getString(16);
        squareHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(17);
        squareHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(18);
        squareHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(19);
        squareHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(20);
        squareHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(21);
        squareHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(22);
        squareHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(23);
        squareHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(24);
        squareHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(25);
        squareHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String ts = rs.getString(26);
        squareHeader.setTs(ts == null ? null : new UFDateTime(ts.trim(), false));

        Integer dr = (Integer)rs.getObject(27);
        squareHeader.setDr(dr == null ? null : dr);

        String csaleid = rs.getString(28);
        squareHeader.setCsaleid(csaleid == null ? null : csaleid.trim());

        String ctermprotocolid = rs.getString(29);
        squareHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());

        String verifyrule = rs.getString(30);
        squareHeader.setVerifyrule(verifyrule == null ? null : verifyrule.trim());

        String cfreecustid = rs.getString(31);
        squareHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        v.addElement(squareHeader);
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
      catch (Exception e) {
      }
    }
    allHeader = new SquareHeaderVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(allHeader);
    }

    afterCallMethod("nc.bs.so.so012.SquareDMO", "queryAllHeadData", new Object[] { key });

    return allHeader;
  }

  public CircularlyAccessibleValueObject[] queryHeadData(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "queryAllHeadData", new Object[] { key });

    String sql = "select pk_corp, vreceiptcode, creceipttype, dbilldate, ccustomerid, cbiztype, coperatorid, ccalbodyid, cwarehouseid, dmakedate, capproveid, dapprovedate, fstatus, cdeptid, cemployeeid, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, ts, dr, csaleid, cfreecustid from so_square where pk_corp = ?";

    SquareHeaderVO[] allHeader = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, key);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        SquareHeaderVO squareHeader = null;
        squareHeader = new SquareHeaderVO(key);

        String pk_corp = rs.getString(1);
        squareHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());

        String vreceiptcode = rs.getString(2);
        squareHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());

        String creceipttype = rs.getString(3);
        squareHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());

        String dbilldate = rs.getString(4);
        squareHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim(), false));

        String ccustomerid = rs.getString(5);
        squareHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

        String cbiztype = rs.getString(6);
        squareHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());

        String coperatorid = rs.getString(7);
        squareHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());

        String ccalbodyid = rs.getString(8);
        squareHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());

        String cwarehouseid = rs.getString(9);
        squareHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());

        String dmakedate = rs.getString(10);
        squareHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim(), false));

        String capproveid = rs.getString(11);
        squareHeader.setCapproveid(capproveid == null ? null : capproveid.trim());

        String dapprovedate = rs.getString(12);
        squareHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim(), false));

        Integer fstatus = (Integer)rs.getObject(13);
        squareHeader.setFstatus(fstatus == null ? null : fstatus);

        String cdeptid = rs.getString(14);
        squareHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());

        String cemployeeid = rs.getString(15);
        squareHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());

        String vdef1 = rs.getString(16);
        squareHeader.setVdef1(vdef1 == null ? null : vdef1.trim());

        String vdef2 = rs.getString(17);
        squareHeader.setVdef2(vdef2 == null ? null : vdef2.trim());

        String vdef3 = rs.getString(18);
        squareHeader.setVdef3(vdef3 == null ? null : vdef3.trim());

        String vdef4 = rs.getString(19);
        squareHeader.setVdef4(vdef4 == null ? null : vdef4.trim());

        String vdef5 = rs.getString(20);
        squareHeader.setVdef5(vdef5 == null ? null : vdef5.trim());

        String vdef6 = rs.getString(21);
        squareHeader.setVdef6(vdef6 == null ? null : vdef6.trim());

        String vdef7 = rs.getString(22);
        squareHeader.setVdef7(vdef7 == null ? null : vdef7.trim());

        String vdef8 = rs.getString(23);
        squareHeader.setVdef8(vdef8 == null ? null : vdef8.trim());

        String vdef9 = rs.getString(24);
        squareHeader.setVdef9(vdef9 == null ? null : vdef9.trim());

        String vdef10 = rs.getString(25);
        squareHeader.setVdef10(vdef10 == null ? null : vdef10.trim());

        String ts = rs.getString(26);
        squareHeader.setTs(ts == null ? null : new UFDateTime(ts.trim(), false));

        Integer dr = (Integer)rs.getObject(27);
        squareHeader.setDr(dr == null ? null : dr);

        String csaleid = rs.getString(28);
        squareHeader.setCsaleid(csaleid == null ? null : csaleid);

        String cfreecustid = rs.getString(29);
        squareHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

        v.addElement(squareHeader);
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
    allHeader = new SquareHeaderVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(allHeader);
    }

    afterCallMethod("nc.bs.so.so012.SquareDMO", "queryAllHeadData", new Object[] { key });

    return allHeader;
  }

  private SquareItemVO[] queryInvoiceBodyData(SquareItemVO[] itemData)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    for (int i = 0; i < itemData.length; i++) {
      String sql = "SELECT cupreceipttype, cupsourcebillid, cupsourcebillbodyid, blargessflag, cpackunitid FROM so_saleinvoice_b WHERE cinvoice_bid = ?";

      SquareItemVO[] squareItems = null;
      Vector v = new Vector();
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);
        stmt.setString(1, itemData[i].getPrimaryKey());
        ResultSet rs = stmt.executeQuery();
        if (rs.next())
        {
          String csourcetype = rs.getString("cupreceipttype");
          itemData[i].setCupreceipttype(csourcetype == null ? null : csourcetype.trim());

          String csourcebillhid = rs.getString("cupsourcebillid");
          itemData[i].setCupbillid(csourcebillhid == null ? null : csourcebillhid.trim());

          String csourcebillbid = rs.getString("cupsourcebillbodyid");
          itemData[i].setCupbillbodyid(csourcebillbid == null ? null : csourcebillbid.trim());

          String blargessflag = rs.getString("blargessflag");
          itemData[i].setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

          String cpackunitid = rs.getString("cpackunitid");
          itemData[i].setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());
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
        catch (Exception e) {
        }
      }
    }
    return itemData;
  }

  private SquareItemVO[] queryOutBodyData(SquareItemVO[] itemData)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    for (int i = 0; i < itemData.length; i++) {
      String sql = "SELECT csourcetype, csourcebillhid, csourcebillbid, flargess, castunitid FROM ic_general_b WHERE cgeneralbid = ?";

      SquareItemVO[] squareItems = null;
      Vector v = new Vector();
      try {
        con = getConnection();
        stmt = con.prepareStatement(sql);
        stmt.setString(1, itemData[i].getPrimaryKey());
        ResultSet rs = stmt.executeQuery();

        if (rs.next())
        {
          String csourcetype = rs.getString("csourcetype");
          itemData[i].setCupreceipttype(csourcetype == null ? null : csourcetype.trim());

          String csourcebillhid = rs.getString("csourcebillhid");
          itemData[i].setCupbillid(csourcebillhid == null ? null : csourcebillhid.trim());

          String csourcebillbid = rs.getString("csourcebillbid");
          itemData[i].setCupbillbodyid(csourcebillbid == null ? null : csourcebillbid.trim());

          String blargessflag = rs.getString("flargess");
          itemData[i].setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

          String castunitid = rs.getString("castunitid");
          itemData[i].setCpackunitid(castunitid == null ? null : castunitid.trim());

          itemData[i] = getScalefactor(itemData[i]);
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
    }
    return itemData;
  }

  public boolean querySquareItem(String[] sCsaleid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "querySquareItem", new Object[] { sCsaleid });

    String sql = "select corder_bid from so_square_b where corder_bid = ? and dr=0 and bifpaybalance='Y'";

    boolean bIsBal = false;

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      for (int i = 0; i < sCsaleid.length; i++)
      {
        stmt.setString(1, sCsaleid[i]);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
          continue;
        bIsBal = true;
        break;
      }

    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (Exception e)
      {
      }
      try
      {
        if (con != null)
        {
          con.close();
        }

      }
      catch (Exception e)
      {
      }

    }

    afterCallMethod("nc.bs.so.so012.SquareDMO", "querySquareItem", new Object[] { sCsaleid });

    return bIsBal;
  }

  public CircularlyAccessibleValueObject[] queryTotalData(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "queryTotalData", new Object[] { key });

    String sql = "SELECT so_square.csaleid AS m_csaleid_h, so_square.pk_corp AS m_pk_corp,so_square.vreceiptcode AS m_vreceiptcode,so_square.creceipttype AS m_creceipttype_h, so_square.dbilldate AS m_dbilldate,so_square.ccustomerid AS m_ccustomerid, so_square.cbiztype AS m_cbiztype,so_square.coperatorid AS m_coperatorid, so_square.ccalbodyid AS m_ccalbodyid,so_square.cwarehouseid AS m_cwarehouseid, so_square.dmakedate AS m_dmakedate,so_square.capproveid AS m_capproveid, so_square.dapprovedate AS m_dapprovedate,so_square.fstatus AS m_fstatus, so_square.cdeptid AS m_cdeptid,so_square.cemployeeid AS m_cemployeeid, so_square.vdef1 AS vdef1_h,so_square.vdef2 AS vdef2_h, so_square.vdef3 AS vdef3_h,so_square.vdef4 AS vdef4_h, so_square.vdef5 AS vdef5_h,so_square.vdef6 AS vdef6_h, so_square.vdef7 AS vdef7_h,so_square.vdef8 AS vdef8_h, so_square.vdef9 AS vdef9_h,so_square.vdef10 AS vdef10_h,so_square.ts AS m_ts_h, so_square.dr AS m_dr_b,so_square.verifyrule AS m_verifyrule, so_square.ctermprotocolid AS m_ctermprotocolid,so_square_b.dbizdate AS m_dbizdate, so_square_b.corder_bid AS m_corder_bid,so_square_b.csaleid AS m_csaleid_b, so_square_b.csourcebillid AS m_csourcebillid,so_square_b.csourcebillbodyid AS m_csourcebillbodyid,so_square_b.cinvbasdocid AS m_cinvbasdocid,so_square_b.cinventoryid AS m_cinventoryid, so_square_b.noutnum AS m_noutnum,so_square_b.nshouldoutnum AS m_nshouldoutnum,so_square_b.nbalancenum AS m_nbalancenum, so_square_b.nsignnum AS m_nsignnum,so_square_b.ccurrencytypeid AS m_ccurrencytypeid,so_square_b.noriginalcurtaxnetprice AS m_noriginalcurtaxnetprice,so_square_b.noriginalcurmny AS m_noriginalcurmny,so_square_b.noriginalcursummny AS m_noriginalcursummny,so_square_b.bifpaybalance AS m_bifpaybalance, so_square_b.cbatchid AS m_cbatchid,so_square_b.nexchangeotobrate AS m_nexchangeotobrate,so_square_b.nexchangeotoarate AS m_nexchangeotoarate,so_square_b.ntaxrate AS m_ntaxrate,so_square_b.noriginalcurnetprice AS m_noriginalcurnetprice,so_square_b.noriginalcurtaxmny AS m_noriginalcurtaxmny,so_square_b.ntaxmny AS m_ntaxmny, so_square_b.nmny AS m_nmny,so_square_b.nsummny AS m_nsummny,so_square_b.nassistcursummny AS m_nassistcursummny,so_square_b.nassistcurmny AS m_nassistcurmny,so_square_b.nassistcurtaxmny AS m_nassistcurtaxmny,so_square_b.cprojectid AS m_cprojectid,so_square_b.cprojectphaseid AS m_cprojectphaseid, so_square_b.vfree1 AS m_vfree1,so_square_b.vfree2 AS m_vfree2, so_square_b.vfree3 AS m_vfree3,so_square_b.vfree4 AS m_vfree4, so_square_b.vfree5 AS m_vfree5,so_square_b.vdef1 AS vdef1_b, so_square_b.vdef2 AS vdef2_b,so_square_b.vdef3 AS vdef3_b, so_square_b.vdef4 AS vdef4_b,so_square_b.vdef5 AS vdef5_b, so_square_b.vdef6 AS vdef6_b,so_square_b.ts AS m_ts_b, so_square_b.dr AS m_dr_b,so_square_b.creceipttype AS m_creceipttype_b,so_square_b.cupreceipttype AS m_cupreceipttype,so_square_b.cupbillid AS m_cupbillid, so_square_b.cupbillbodyid AS m_cupbillbodyid,so_square_b.blargessflag AS m_blargessflag,so_square_b.discountflag AS m_discountflag, so_square_b.laborflag AS m_laborflag,so_square_b.cbodywarehouseid AS m_cbodywarehouseid,so_square_b.ncostmny AS m_ncostmny, so_square_b.cpackunitid AS m_cpackunitid,so_square_b.scalefactor AS m_scalefactor,so_square_b.nbalancemny AS m_nbalancemny,so_square_b.noriginalcurdiscountmny AS m_noriginalcurdiscountmny,so_square_b.nnetprice AS m_nnetprice, so_square_b.ntaxnetprice AS m_ntaxnetprice,so_square_b.ndiscountmny AS m_ndiscountmny, so_square.cdispatcherid, so_square_b.cbodycalbodyid ,so_square_b.npacknumber,so_square_b.cprolineid ,so_square_b.nreturntaxrate ,so_square.bincomeflag,so_square.bcostflag, so_square.vdef11 AS vdef11_h,so_square.vdef12 AS vdef12_h, so_square.vdef13 AS vdef13_h,so_square.vdef14 AS vdef14_h, so_square.vdef15 AS vdef15_h,so_square.vdef16 AS vdef16_h, so_square.vdef17 AS vdef17_h,so_square.vdef18 AS vdef18_h, so_square.vdef19 AS vdef19_h,so_square.vdef20 AS vdef20_h,so_square.pk_defdoc1 AS pk_defdoc1_h,so_square.pk_defdoc2 AS pk_defdoc2_h, so_square.pk_defdoc3 AS pk_defdoc3_h,so_square.pk_defdoc4 AS pk_defdoc4_h, so_square.pk_defdoc5 AS pk_defdoc5_h,so_square.pk_defdoc6 AS pk_defdoc6_h, so_square.pk_defdoc7 AS pk_defdoc7_h,so_square.pk_defdoc8 AS pk_defdoc8_h, so_square.pk_defdoc9 AS pk_defdoc9_h,so_square.pk_defdoc10 AS pk_defdoc10_h,so_square.pk_defdoc11 AS pk_defdoc11_h,so_square.pk_defdoc12 AS pk_defdoc12_h, so_square.pk_defdoc13 AS pk_defdoc13_h,so_square.pk_defdoc14 AS pk_defdoc14_h, so_square.pk_defdoc15 AS pk_defdoc15_h,so_square.pk_defdoc16 AS pk_defdoc16_h, so_square.pk_defdoc17 AS pk_defdoc17_h,so_square.pk_defdoc18 AS pk_defdoc18_h, so_square.pk_defdoc19 AS pk_defdoc19_h,so_square.pk_defdoc20 AS pk_defdoc20_h,so_square_b.vdef7 AS vdef7_b, so_square_b.vdef8 AS vdef8_b,so_square_b.vdef9 AS vdef9_b, so_square_b.vdef10 AS vdef10_b,so_square_b.vdef11 AS vdef11_b, so_square_b.vdef12 AS vdef12_b,so_square_b.vdef13 AS vdef13_b, so_square_b.vdef14 AS vdef14_b,so_square_b.vdef15 AS vdef15_b, so_square_b.vdef16 AS vdef16_b,so_square_b.vdef17 AS vdef17_b, so_square_b.vdef18 AS vdef18_b,so_square_b.vdef19 AS vdef19_b, so_square_b.vdef20 AS vdef20_b,so_square_b.pk_defdoc1 AS pk_defdoc1_b, so_square_b.pk_defdoc2 AS pk_defdoc2_b,so_square_b.pk_defdoc3 AS pk_defdoc3_b, so_square_b.pk_defdoc4 AS pk_defdoc4_b,so_square_b.pk_defdoc5 AS pk_defdoc5_b, so_square_b.pk_defdoc6 AS pk_defdoc6_b,so_square_b.pk_defdoc7 AS pk_defdoc7_b, so_square_b.pk_defdoc8 AS pk_defdoc8_b,so_square_b.pk_defdoc9 AS pk_defdoc9_b, so_square_b.pk_defdoc10 AS pk_defdoc10_b,so_square_b.pk_defdoc11 AS pk_defdoc11_b, so_square_b.pk_defdoc12 AS pk_defdoc12_b,so_square_b.pk_defdoc13 AS pk_defdoc13_b, so_square_b.pk_defdoc14 AS pk_defdoc14_b,so_square_b.pk_defdoc15 AS pk_defdoc15_b, so_square_b.pk_defdoc16 AS pk_defdoc16_b,so_square_b.pk_defdoc17 AS pk_defdoc17_b, so_square_b.pk_defdoc18 AS pk_defdoc18_b,so_square_b.pk_defdoc19 AS pk_defdoc19_b, so_square_b.pk_defdoc20 AS pk_defdoc20_b, so_square_b.cquoteunitid,so_square_b.nquoteunitnum,so_square_b.nquoteunitrate,so_square_b.nqttaxnetprc,so_square_b.nqtnetprc,so_square_b.nqttaxprc,so_square_b.nqtprc, so_square_b.norgqttaxnetprc,so_square_b.norgqtnetprc,so_square_b.norgqttaxprc,so_square_b.norgqtprc, so_square.cfreecustid AS m_cfreecustid, so_square.bautoincomeflag  FROM so_square INNER JOIN so_square_b ON so_square.csaleid = so_square_b.csaleid where " + key;

    SCMEnv.out(sql);
    SquareTotalVO[] allTotal = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        SquareTotalVO squareTotal = null;
        squareTotal = new SquareTotalVO();

        String m_csaleid_h = rs.getString(1);
        squareTotal.setAttributeValue("csaleid_h", m_csaleid_h == null ? null : m_csaleid_h.trim());

        String m_pk_corp = rs.getString(2);
        squareTotal.setAttributeValue("pk_corp", m_pk_corp == null ? null : m_pk_corp.trim());

        String m_vreceiptcode = rs.getString(3);
        squareTotal.setAttributeValue("vreceiptcode", m_vreceiptcode == null ? null : m_vreceiptcode.trim());

        String m_creceipttype_h = rs.getString(4);
        squareTotal.setAttributeValue("creceipttype_h", m_creceipttype_h == null ? null : m_creceipttype_h.trim());

        String m_dbilldate = rs.getString(5);
        squareTotal.setAttributeValue("dbilldate", m_dbilldate == null ? null : new UFDate(m_dbilldate.trim(), false));

        String m_ccustomerid = rs.getString(6);
        squareTotal.setAttributeValue("ccustomerid", m_ccustomerid == null ? null : m_ccustomerid.trim());

        String m_cbiztype = rs.getString(7);
        squareTotal.setAttributeValue("cbiztype", m_cbiztype == null ? null : m_cbiztype.trim());

        String m_coperatorid = rs.getString(8);
        squareTotal.setAttributeValue("coperatorid", m_coperatorid == null ? null : m_coperatorid.trim());

        String m_ccalbodyid = rs.getString(9);
        squareTotal.setAttributeValue("ccalbodyid", m_ccalbodyid == null ? null : m_ccalbodyid.trim());

        String m_cwarehouseid = rs.getString(10);
        squareTotal.setAttributeValue("cwarehouseid", m_cwarehouseid == null ? null : m_cwarehouseid.trim());

        String m_dmakedate = rs.getString(11);
        squareTotal.setAttributeValue("dmakedate", m_dmakedate == null ? null : new UFDate(m_dmakedate.trim(), false));

        String m_capproveid = rs.getString(12);
        squareTotal.setAttributeValue("capproveid", m_capproveid == null ? null : m_capproveid.trim());

        String m_dapprovedate = rs.getString(13);
        squareTotal.setAttributeValue("dapprovedate", m_dapprovedate == null ? null : new UFDate(m_dapprovedate.trim(), false));

        Integer m_fstatus = (Integer)rs.getObject(14);
        squareTotal.setAttributeValue("fstatus", m_fstatus == null ? null : m_fstatus);

        String m_cdeptid = rs.getString(15);
        squareTotal.setAttributeValue("cdeptid", m_cdeptid == null ? null : m_cdeptid.trim());

        String m_cemployeeid = rs.getString(16);
        squareTotal.setAttributeValue("cemployeeid", m_cemployeeid == null ? null : m_cemployeeid.trim());

        String m_vdef1_h = rs.getString(17);
        squareTotal.setAttributeValue("vdef1_h", m_vdef1_h == null ? null : m_vdef1_h.trim());

        String m_vdef2_h = rs.getString(18);
        squareTotal.setAttributeValue("vdef2_h", m_vdef2_h == null ? null : m_vdef2_h.trim());

        String m_vdef3_h = rs.getString(19);
        squareTotal.setAttributeValue("vdef3_h", m_vdef3_h == null ? null : m_vdef3_h.trim());

        String m_vdef4_h = rs.getString(20);
        squareTotal.setAttributeValue("vdef4_h", m_vdef4_h == null ? null : m_vdef4_h.trim());

        String m_vdef5_h = rs.getString(21);
        squareTotal.setAttributeValue("vdef5_h", m_vdef5_h == null ? null : m_vdef5_h.trim());

        String m_vdef6_h = rs.getString(22);
        squareTotal.setAttributeValue("vdef6_h", m_vdef6_h == null ? null : m_vdef6_h.trim());

        String m_vdef7_h = rs.getString(23);
        squareTotal.setAttributeValue("vdef7_h", m_vdef7_h == null ? null : m_vdef7_h.trim());

        String m_vdef8_h = rs.getString(24);
        squareTotal.setAttributeValue("vdef8_h", m_vdef8_h == null ? null : m_vdef8_h.trim());

        String m_vdef9_h = rs.getString(25);
        squareTotal.setAttributeValue("vdef9_h", m_vdef9_h == null ? null : m_vdef9_h.trim());

        String m_vdef10_h = rs.getString(26);
        squareTotal.setAttributeValue("vdef10_h", m_vdef10_h == null ? null : m_vdef10_h.trim());

        String m_ts_h = rs.getString(27);
        squareTotal.setAttributeValue("ts_h", m_ts_h == null ? null : new UFDateTime(m_ts_h.trim(), false));

        Integer m_dr_h = (Integer)rs.getObject(28);
        squareTotal.setAttributeValue("dr_h", m_dr_h == null ? null : m_dr_h);

        String m_verifyrule = rs.getString(29);
        squareTotal.setAttributeValue("verifyrule", m_verifyrule == null ? null : m_verifyrule.trim());

        String m_ctermprotocolid = rs.getString(30);
        squareTotal.setAttributeValue("ctermprotocolid", m_ctermprotocolid == null ? null : m_ctermprotocolid.trim());

        String m_dbizdate = rs.getString(31);
        squareTotal.setAttributeValue("dbizdate", m_dbizdate == null ? null : new UFDate(m_dbizdate.trim(), false));

        String m_corder_bid = rs.getString(32);
        squareTotal.setAttributeValue("corder_bid", m_corder_bid == null ? null : m_corder_bid.trim());

        String m_csaleid_b = rs.getString(33);
        squareTotal.setAttributeValue("csaleid_b", m_csaleid_b == null ? null : m_csaleid_b.trim());

        String m_csourcebillid = rs.getString(34);
        squareTotal.setAttributeValue("csourcebillid", m_csourcebillid == null ? null : m_csourcebillid.trim());

        String m_csourcebillbodyid = rs.getString(35);
        squareTotal.setAttributeValue("csourcebillbodyid", m_csourcebillbodyid == null ? null : m_csourcebillbodyid.trim());

        String m_cinvbasdocid = rs.getString(36);
        squareTotal.setAttributeValue("cinvbasdocid", m_cinvbasdocid == null ? null : m_cinvbasdocid.trim());

        String m_cinventoryid = rs.getString(37);
        squareTotal.setAttributeValue("cinventoryid", m_cinventoryid == null ? null : m_cinventoryid.trim());

        BigDecimal m_noutnum = (BigDecimal)rs.getObject(38);
        squareTotal.setAttributeValue("noutnum", m_noutnum == null ? null : new UFDouble(m_noutnum));

        BigDecimal m_nshouldoutnum = (BigDecimal)rs.getObject(39);
        squareTotal.setAttributeValue("nshouldoutnum", m_nshouldoutnum == null ? null : new UFDouble(m_nshouldoutnum));

        BigDecimal m_nbalancenum = (BigDecimal)rs.getObject(40);
        squareTotal.setAttributeValue("nbalancenum", m_nbalancenum == null ? null : new UFDouble(m_nbalancenum));

        BigDecimal m_nsignnum = (BigDecimal)rs.getObject(41);
        squareTotal.setAttributeValue("nsignnum", m_nsignnum == null ? null : new UFDouble(m_nsignnum));

        String m_ccurrencytypeid = rs.getString(42);
        squareTotal.setAttributeValue("ccurrencytypeid", m_ccurrencytypeid == null ? null : m_ccurrencytypeid.trim());

        BigDecimal m_noriginalcurtaxnetprice = (BigDecimal)rs.getObject(43);
        squareTotal.setAttributeValue("noriginalcurtaxnetprice", m_noriginalcurtaxnetprice == null ? null : new UFDouble(m_noriginalcurtaxnetprice));

        BigDecimal m_noriginalcurmny = (BigDecimal)rs.getObject(44);
        squareTotal.setAttributeValue("noriginalcurmny", m_noriginalcurmny == null ? null : new UFDouble(m_noriginalcurmny));

        BigDecimal m_noriginalcursummny = (BigDecimal)rs.getObject(45);
        squareTotal.setAttributeValue("noriginalcursummny", m_noriginalcursummny == null ? null : new UFDouble(m_noriginalcursummny));

        String m_bifpaybalance = rs.getString(46);
        squareTotal.setAttributeValue("bifpaybalance", m_bifpaybalance == null ? null : new UFBoolean(m_bifpaybalance.trim()));

        String m_cbatchid = rs.getString(47);
        squareTotal.setAttributeValue("cbatchid", m_cbatchid == null ? null : m_cbatchid.trim());

        BigDecimal m_nexchangeotobrate = (BigDecimal)rs.getObject(48);
        squareTotal.setAttributeValue("nexchangeotobrate", m_nexchangeotobrate == null ? null : new UFDouble(m_nexchangeotobrate));

        BigDecimal m_nexchangeotoarate = (BigDecimal)rs.getObject(49);
        squareTotal.setAttributeValue("nexchangeotoarate", m_nexchangeotoarate == null ? null : new UFDouble(m_nexchangeotoarate));

        BigDecimal m_ntaxrate = (BigDecimal)rs.getObject(50);
        squareTotal.setAttributeValue("ntaxrate", m_ntaxrate == null ? null : new UFDouble(m_ntaxrate));

        BigDecimal m_noriginalcurnetprice = (BigDecimal)rs.getObject(51);
        squareTotal.setAttributeValue("noriginalcurnetprice", m_noriginalcurnetprice == null ? null : new UFDouble(m_noriginalcurnetprice));

        BigDecimal m_noriginalcurtaxmny = (BigDecimal)rs.getObject(52);
        squareTotal.setAttributeValue("noriginalcurtaxmny", m_noriginalcurtaxmny == null ? null : new UFDouble(m_noriginalcurtaxmny));

        BigDecimal m_ntaxmny = (BigDecimal)rs.getObject(53);
        squareTotal.setAttributeValue("ntaxmny", m_ntaxmny == null ? null : new UFDouble(m_ntaxmny));

        BigDecimal m_nmny = (BigDecimal)rs.getObject(54);
        squareTotal.setAttributeValue("nmny", m_nmny == null ? null : new UFDouble(m_nmny));

        BigDecimal m_nsummny = (BigDecimal)rs.getObject(55);
        squareTotal.setAttributeValue("nsummny", m_nsummny == null ? null : new UFDouble(m_nsummny));

        BigDecimal m_nassistcursummny = (BigDecimal)rs.getObject(56);
        squareTotal.setAttributeValue("nassistcursummny", m_nassistcursummny == null ? null : new UFDouble(m_nassistcursummny));

        BigDecimal m_nassistcurmny = (BigDecimal)rs.getObject(57);
        squareTotal.setAttributeValue("nassistcurmny", m_nassistcurmny == null ? null : new UFDouble(m_nassistcurmny));

        BigDecimal m_nassistcurtaxmny = (BigDecimal)rs.getObject(58);
        squareTotal.setAttributeValue("nassistcurtaxmny", m_nassistcurtaxmny == null ? null : new UFDouble(m_nassistcurtaxmny));

        String m_cprojectid = rs.getString(59);
        squareTotal.setAttributeValue("cprojectid", m_cprojectid == null ? null : m_cprojectid.trim());

        String m_cprojectphaseid = rs.getString(60);
        squareTotal.setAttributeValue("cprojectphaseid", m_cprojectphaseid == null ? null : m_cprojectphaseid.trim());

        String m_vfree1 = rs.getString(61);
        squareTotal.setAttributeValue("vfree1", m_vfree1 == null ? null : m_vfree1.trim());

        String m_vfree2 = rs.getString(62);
        squareTotal.setAttributeValue("vfree2", m_vfree2 == null ? null : m_vfree2.trim());

        String m_vfree3 = rs.getString(63);
        squareTotal.setAttributeValue("vfree3", m_vfree3 == null ? null : m_vfree3.trim());

        String m_vfree4 = rs.getString(64);
        squareTotal.setAttributeValue("vfree4", m_vfree4 == null ? null : m_vfree4.trim());

        String m_vfree5 = rs.getString(65);
        squareTotal.setAttributeValue("vfree5", m_vfree5 == null ? null : m_vfree5.trim());

        String m_vdef1_b = rs.getString(66);
        squareTotal.setAttributeValue("vdef1_b", m_vdef1_b == null ? null : m_vdef1_b.trim());

        String m_vdef2_b = rs.getString(67);
        squareTotal.setAttributeValue("vdef2_b", m_vdef2_b == null ? null : m_vdef2_b.trim());

        String m_vdef3_b = rs.getString(68);
        squareTotal.setAttributeValue("vdef3_b", m_vdef3_b == null ? null : m_vdef3_b.trim());

        String m_vdef4_b = rs.getString(69);
        squareTotal.setAttributeValue("vdef4_b", m_vdef4_b == null ? null : m_vdef4_b.trim());

        String m_vdef5_b = rs.getString(70);
        squareTotal.setAttributeValue("vdef5_b", m_vdef5_b == null ? null : m_vdef5_b.trim());

        String m_vdef6_b = rs.getString(71);
        squareTotal.setAttributeValue("vdef6_b", m_vdef6_b == null ? null : m_vdef6_b.trim());

        String m_ts_b = rs.getString(72);
        squareTotal.setAttributeValue("ts_b", m_ts_b == null ? null : new UFDateTime(m_ts_b.trim(), false));

        Integer m_dr_b = (Integer)rs.getObject(73);
        squareTotal.setAttributeValue("dr_b", m_dr_b == null ? null : m_dr_b);

        String m_creceipttype_b = rs.getString(74);
        squareTotal.setAttributeValue("creceipttype_b", m_creceipttype_b == null ? null : m_creceipttype_b.trim());

        String m_cupreceipttype = rs.getString(75);
        squareTotal.setAttributeValue("cupreceipttype", m_cupreceipttype == null ? null : m_cupreceipttype.trim());

        String m_cupbillid = rs.getString(76);
        squareTotal.setAttributeValue("cupbillid", m_cupbillid == null ? null : m_cupbillid.trim());

        String m_cupbillbodyid = rs.getString(77);
        squareTotal.setAttributeValue("cupbillbodyid", m_cupbillbodyid == null ? null : m_cupbillbodyid.trim());

        String m_blargessflag = rs.getString(78);
        squareTotal.setAttributeValue("blargessflag", m_blargessflag == null ? null : new UFBoolean(m_blargessflag.trim()));

        String m_discountflag = rs.getString(79);
        squareTotal.setAttributeValue("discountflag", m_discountflag == null ? null : new UFBoolean(m_discountflag.trim()));

        String m_laborflag = rs.getString(80);
        squareTotal.setAttributeValue("laborflag", m_laborflag == null ? null : new UFBoolean(m_laborflag.trim()));

        String m_cbodywarehouseid = rs.getString(81);
        squareTotal.setAttributeValue("cbodywarehouseid", m_cbodywarehouseid == null ? null : m_cbodywarehouseid.trim());

        BigDecimal m_ncostmny = (BigDecimal)rs.getObject(82);
        squareTotal.setAttributeValue("ncostmny", m_ncostmny == null ? null : new UFDouble(m_ncostmny));

        String m_cpackunitid = rs.getString(83);
        squareTotal.setAttributeValue("cpackunitid", m_cpackunitid == null ? null : m_cpackunitid.trim());

        BigDecimal m_scalefactor = (BigDecimal)rs.getObject(84);
        squareTotal.setAttributeValue("scalefactor", m_scalefactor == null ? null : new UFDouble(m_scalefactor));

        BigDecimal m_nbalancemny = (BigDecimal)rs.getObject(85);
        squareTotal.setAttributeValue("nbalancemny", m_nbalancemny == null ? null : new UFDouble(m_nbalancemny));

        BigDecimal m_noriginalcurdiscountmny = (BigDecimal)rs.getObject(86);
        squareTotal.setAttributeValue("noriginalcurdiscountmny", m_noriginalcurdiscountmny == null ? null : new UFDouble(m_noriginalcurdiscountmny));

        BigDecimal m_nnetprice = (BigDecimal)rs.getObject(87);
        squareTotal.setAttributeValue("nnetprice", m_nnetprice == null ? null : new UFDouble(m_nnetprice));

        BigDecimal m_ntaxnetprice = (BigDecimal)rs.getObject(88);
        squareTotal.setAttributeValue("ntaxnetprice", m_ntaxnetprice == null ? null : new UFDouble(m_ntaxnetprice));

        BigDecimal m_ndiscountmny = (BigDecimal)rs.getObject(89);
        squareTotal.setAttributeValue("ndiscountmny", m_ndiscountmny == null ? null : new UFDouble(m_ndiscountmny));

        String sCdispatcherid = rs.getString(90);
        squareTotal.setAttributeValue("dispatcherid", sCdispatcherid == null ? null : sCdispatcherid.trim());

        String cbodycalbodyid = rs.getString(91);
        squareTotal.setAttributeValue("cbodycalbodyid", cbodycalbodyid == null ? null : cbodycalbodyid.trim());

        BigDecimal npacknumber = (BigDecimal)rs.getObject(92);
        squareTotal.setAttributeValue("npacknumber", npacknumber == null ? null : new UFDouble(npacknumber));

        String cprolineid = rs.getString(93);
        squareTotal.setAttributeValue("cprolineid", cprolineid == null ? null : cprolineid.trim());

        BigDecimal nreturntaxrate = (BigDecimal)rs.getObject(94);
        squareTotal.setAttributeValue("nreturntaxrate", nreturntaxrate == null ? null : new UFDouble(nreturntaxrate));

        String bincomeflag = rs.getString(95);
        squareTotal.setAttributeValue("bincomeflag", bincomeflag == null ? null : new UFBoolean(bincomeflag.trim()));

        String bcostflag = rs.getString(96);
        squareTotal.setAttributeValue("bcostflag", bcostflag == null ? null : new UFBoolean(bcostflag.trim()));

        String sDef11_h = rs.getString(97);
        squareTotal.setAttributeValue("vdef11_h", sDef11_h == null ? null : sDef11_h.trim());

        String sDef12_h = rs.getString(98);
        squareTotal.setAttributeValue("vdef12_h", sDef12_h == null ? null : sDef12_h.trim());

        String sDef13_h = rs.getString(99);
        squareTotal.setAttributeValue("vdef13_h", sDef13_h == null ? null : sDef13_h.trim());

        String sDef14_h = rs.getString(100);
        squareTotal.setAttributeValue("vdef14_h", sDef14_h == null ? null : sDef14_h.trim());

        String sDef15_h = rs.getString(101);
        squareTotal.setAttributeValue("vdef15_h", sDef15_h == null ? null : sDef15_h.trim());

        String sDef16_h = rs.getString(102);
        squareTotal.setAttributeValue("vdef16_h", sDef16_h == null ? null : sDef16_h.trim());

        String sDef17_h = rs.getString(103);
        squareTotal.setAttributeValue("vdef17_h", sDef17_h == null ? null : sDef17_h.trim());

        String sDef18_h = rs.getString(104);
        squareTotal.setAttributeValue("vdef18_h", sDef18_h == null ? null : sDef18_h.trim());

        String sDef19_h = rs.getString(105);
        squareTotal.setAttributeValue("vdef19_h", sDef19_h == null ? null : sDef19_h.trim());

        String sDef20_h = rs.getString(106);
        squareTotal.setAttributeValue("vdef20_h", sDef20_h == null ? null : sDef20_h.trim());

        String sPk_defdoc1_h = rs.getString(107);
        squareTotal.setAttributeValue("pk_defdoc1_h", sPk_defdoc1_h == null ? null : sPk_defdoc1_h.trim());

        String sPk_defdoc2_h = rs.getString(108);
        squareTotal.setAttributeValue("pk_defdoc2_h", sPk_defdoc2_h == null ? null : sPk_defdoc2_h.trim());

        String sPk_defdoc3_h = rs.getString(109);
        squareTotal.setAttributeValue("pk_defdoc3_h", sPk_defdoc3_h == null ? null : sPk_defdoc3_h.trim());

        String sPk_defdoc4_h = rs.getString(110);
        squareTotal.setAttributeValue("pk_defdoc4_h", sPk_defdoc4_h == null ? null : sPk_defdoc4_h.trim());

        String sPk_defdoc5_h = rs.getString(111);
        squareTotal.setAttributeValue("pk_defdoc5_h", sPk_defdoc5_h == null ? null : sPk_defdoc5_h.trim());

        String sPk_defdoc6_h = rs.getString(112);
        squareTotal.setAttributeValue("pk_defdoc6_h", sPk_defdoc6_h == null ? null : sPk_defdoc6_h.trim());

        String sPk_defdoc7_h = rs.getString(113);
        squareTotal.setAttributeValue("pk_defdoc7_h", sPk_defdoc7_h == null ? null : sPk_defdoc7_h.trim());

        String sPk_defdoc8_h = rs.getString(114);
        squareTotal.setAttributeValue("pk_defdoc8_h", sPk_defdoc8_h == null ? null : sPk_defdoc8_h.trim());

        String sPk_defdoc9_h = rs.getString(115);
        squareTotal.setAttributeValue("pk_defdoc9_h", sPk_defdoc9_h == null ? null : sPk_defdoc9_h.trim());

        String sPk_defdoc10_h = rs.getString(116);
        squareTotal.setAttributeValue("pk_defdoc10_h", sPk_defdoc10_h == null ? null : sPk_defdoc10_h.trim());

        String sPk_defdoc11_h = rs.getString(117);
        squareTotal.setAttributeValue("pk_defdoc11_h", sPk_defdoc11_h == null ? null : sPk_defdoc11_h.trim());

        String sPk_defdoc12_h = rs.getString(118);
        squareTotal.setAttributeValue("pk_defdoc12_h", sPk_defdoc12_h == null ? null : sPk_defdoc12_h.trim());

        String sPk_defdoc13_h = rs.getString(119);
        squareTotal.setAttributeValue("pk_defdoc13_h", sPk_defdoc13_h == null ? null : sPk_defdoc13_h.trim());

        String sPk_defdoc14_h = rs.getString(120);
        squareTotal.setAttributeValue("pk_defdoc14_h", sPk_defdoc14_h == null ? null : sPk_defdoc14_h.trim());

        String sPk_defdoc15_h = rs.getString(121);
        squareTotal.setAttributeValue("pk_defdoc15_h", sPk_defdoc15_h == null ? null : sPk_defdoc15_h.trim());

        String sPk_defdoc16_h = rs.getString(122);
        squareTotal.setAttributeValue("pk_defdoc16_h", sPk_defdoc16_h == null ? null : sPk_defdoc16_h.trim());

        String sPk_defdoc17_h = rs.getString(123);
        squareTotal.setAttributeValue("pk_defdoc17_h", sPk_defdoc17_h == null ? null : sPk_defdoc17_h.trim());

        String sPk_defdoc18_h = rs.getString(124);
        squareTotal.setAttributeValue("pk_defdoc18_h", sPk_defdoc18_h == null ? null : sPk_defdoc18_h.trim());

        String sPk_defdoc19_h = rs.getString(125);
        squareTotal.setAttributeValue("pk_defdoc19_h", sPk_defdoc19_h == null ? null : sPk_defdoc19_h.trim());

        String sPk_defdoc20_h = rs.getString(126);
        squareTotal.setAttributeValue("pk_defdoc20_h", sPk_defdoc20_h == null ? null : sPk_defdoc20_h.trim());

        String sDef7_b = rs.getString(127);
        squareTotal.setAttributeValue("vdef7_b", sDef7_b == null ? null : sDef7_b.trim());

        String sDef8_b = rs.getString(128);
        squareTotal.setAttributeValue("vdef8_b", sDef8_b == null ? null : sDef8_b.trim());

        String sDef9_b = rs.getString(129);
        squareTotal.setAttributeValue("vdef9_b", sDef9_b == null ? null : sDef9_b.trim());

        String sDef10_b = rs.getString(130);
        squareTotal.setAttributeValue("vdef10_b", sDef10_b == null ? null : sDef10_b.trim());

        String sDef11_b = rs.getString(131);
        squareTotal.setAttributeValue("vdef11_b", sDef11_b == null ? null : sDef11_b.trim());

        String sDef12_b = rs.getString(132);
        squareTotal.setAttributeValue("vdef12_b", sDef12_b == null ? null : sDef12_b.trim());

        String sDef13_b = rs.getString(133);
        squareTotal.setAttributeValue("vdef13_b", sDef13_b == null ? null : sDef13_b.trim());

        String sDef14_b = rs.getString(134);
        squareTotal.setAttributeValue("vdef14_b", sDef14_b == null ? null : sDef14_b.trim());

        String sDef15_b = rs.getString(135);
        squareTotal.setAttributeValue("vdef15_b", sDef15_b == null ? null : sDef15_b.trim());

        String sDef16_b = rs.getString(136);
        squareTotal.setAttributeValue("vdef16_b", sDef16_b == null ? null : sDef16_b.trim());

        String sDef17_b = rs.getString(137);
        squareTotal.setAttributeValue("vdef17_b", sDef17_b == null ? null : sDef17_b.trim());

        String sDef18_b = rs.getString(138);
        squareTotal.setAttributeValue("vdef18_b", sDef18_b == null ? null : sDef18_b.trim());

        String sDef19_b = rs.getString(139);
        squareTotal.setAttributeValue("vdef19_b", sDef19_b == null ? null : sDef19_b.trim());

        String sDef20_b = rs.getString(140);
        squareTotal.setAttributeValue("vdef20_b", sDef20_b == null ? null : sDef20_b.trim());

        String sPk_defdoc1_b = rs.getString(141);
        squareTotal.setAttributeValue("pk_defdoc1_b", sPk_defdoc1_b == null ? null : sPk_defdoc1_b.trim());

        String sPk_defdoc2_b = rs.getString(142);
        squareTotal.setAttributeValue("pk_defdoc2_b", sPk_defdoc2_b == null ? null : sPk_defdoc2_b.trim());

        String sPk_defdoc3_b = rs.getString(143);
        squareTotal.setAttributeValue("pk_defdoc3_b", sPk_defdoc3_b == null ? null : sPk_defdoc3_b.trim());

        String sPk_defdoc4_b = rs.getString(144);
        squareTotal.setAttributeValue("pk_defdoc4_b", sPk_defdoc4_b == null ? null : sPk_defdoc4_b.trim());

        String sPk_defdoc5_b = rs.getString(145);
        squareTotal.setAttributeValue("pk_defdoc5_b", sPk_defdoc5_b == null ? null : sPk_defdoc5_b.trim());

        String sPk_defdoc6_b = rs.getString(146);
        squareTotal.setAttributeValue("pk_defdoc6_b", sPk_defdoc6_b == null ? null : sPk_defdoc6_b.trim());

        String sPk_defdoc7_b = rs.getString(147);
        squareTotal.setAttributeValue("pk_defdoc7_b", sPk_defdoc7_b == null ? null : sPk_defdoc7_b.trim());

        String sPk_defdoc8_b = rs.getString(148);
        squareTotal.setAttributeValue("pk_defdoc8_b", sPk_defdoc8_b == null ? null : sPk_defdoc8_b.trim());

        String sPk_defdoc9_b = rs.getString(149);
        squareTotal.setAttributeValue("pk_defdoc9_b", sPk_defdoc9_b == null ? null : sPk_defdoc9_b.trim());

        String sPk_defdoc10_b = rs.getString(150);
        squareTotal.setAttributeValue("pk_defdoc10_b", sPk_defdoc10_b == null ? null : sPk_defdoc10_b.trim());

        String sPk_defdoc11_b = rs.getString(151);
        squareTotal.setAttributeValue("pk_defdoc11_b", sPk_defdoc11_b == null ? null : sPk_defdoc11_b.trim());

        String sPk_defdoc12_b = rs.getString(152);
        squareTotal.setAttributeValue("pk_defdoc12_b", sPk_defdoc12_b == null ? null : sPk_defdoc12_b.trim());

        String sPk_defdoc13_b = rs.getString(153);
        squareTotal.setAttributeValue("pk_defdoc13_b", sPk_defdoc13_b == null ? null : sPk_defdoc13_b.trim());

        String sPk_defdoc14_b = rs.getString(154);
        squareTotal.setAttributeValue("pk_defdoc14_b", sPk_defdoc14_b == null ? null : sPk_defdoc14_b.trim());

        String sPk_defdoc15_b = rs.getString(155);
        squareTotal.setAttributeValue("pk_defdoc15_b", sPk_defdoc15_b == null ? null : sPk_defdoc15_b.trim());

        String sPk_defdoc16_b = rs.getString(156);
        squareTotal.setAttributeValue("pk_defdoc16_b", sPk_defdoc16_b == null ? null : sPk_defdoc16_b.trim());

        String sPk_defdoc17_b = rs.getString(157);
        squareTotal.setAttributeValue("pk_defdoc17_b", sPk_defdoc17_b == null ? null : sPk_defdoc17_b.trim());

        String sPk_defdoc18_b = rs.getString(158);
        squareTotal.setAttributeValue("pk_defdoc18_b", sPk_defdoc18_b == null ? null : sPk_defdoc18_b.trim());

        String sPk_defdoc19_b = rs.getString(159);
        squareTotal.setAttributeValue("pk_defdoc19_b", sPk_defdoc19_b == null ? null : sPk_defdoc19_b.trim());

        String sPk_defdoc20_b = rs.getString(160);
        squareTotal.setAttributeValue("pk_defdoc20_b", sPk_defdoc20_b == null ? null : sPk_defdoc20_b.trim());

        String cquoteunitid = rs.getString(161);
        squareTotal.setAttributeValue("cquoteunitid", cquoteunitid == null ? null : cquoteunitid.trim());

        BigDecimal nquoteunitnum = (BigDecimal)rs.getObject(162);
        squareTotal.setAttributeValue("nquoteunitnum", nquoteunitnum == null ? null : new UFDouble(nquoteunitnum));

        BigDecimal nquoteunitrate = (BigDecimal)rs.getObject(163);
        squareTotal.setAttributeValue("nquoteunitrate", nquoteunitrate == null ? null : new UFDouble(nquoteunitrate));

        BigDecimal nqttaxnetprc = (BigDecimal)rs.getObject(164);
        squareTotal.setAttributeValue("nqttaxnetprc", nqttaxnetprc == null ? null : new UFDouble(nqttaxnetprc));

        BigDecimal nqtnetprc = (BigDecimal)rs.getObject(165);
        squareTotal.setAttributeValue("nqtnetprc", nqtnetprc == null ? null : new UFDouble(nqtnetprc));

        BigDecimal nqttaxprc = (BigDecimal)rs.getObject(166);
        squareTotal.setAttributeValue("nqttaxprc", nqttaxprc == null ? null : new UFDouble(nqttaxprc));

        BigDecimal nqtprc = (BigDecimal)rs.getObject(167);
        squareTotal.setAttributeValue("nqtprc", nqtprc == null ? null : new UFDouble(nqtprc));

        BigDecimal norgqttaxnetprc = (BigDecimal)rs.getObject(168);
        squareTotal.setAttributeValue("norgqttaxnetprc", norgqttaxnetprc == null ? null : new UFDouble(norgqttaxnetprc));

        BigDecimal norgqtnetprc = (BigDecimal)rs.getObject(169);
        squareTotal.setAttributeValue("norgqtnetprc", norgqtnetprc == null ? null : new UFDouble(norgqtnetprc));

        BigDecimal norgqttaxprc = (BigDecimal)rs.getObject(170);
        squareTotal.setAttributeValue("norgqttaxprc", norgqttaxprc == null ? null : new UFDouble(norgqttaxprc));

        BigDecimal norgqtprc = (BigDecimal)rs.getObject(171);
        squareTotal.setAttributeValue("norgqtprc", norgqtprc == null ? null : new UFDouble(norgqtprc));

        String cfreecustid = rs.getString(172);
        squareTotal.setAttributeValue("cfreecustid", cfreecustid == null ? null : cfreecustid.trim());

        String bautoincomeflag = rs.getString(173);
        squareTotal.setAttributeValue("bautoincomeflag", bautoincomeflag == null ? null : new UFBoolean(bautoincomeflag.trim()));

        v.addElement(squareTotal);
      }
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
      }
    }
    allTotal = new SquareTotalVO[v.size()];
    if (v.size() > 0)
    {
      v.copyInto(allTotal);
    }

    allTotal = getScalefactor(allTotal);

    afterCallMethod("nc.bs.so.so012.SquareDMO", "queryTotalData", new Object[] { key });

    return allTotal;
  }

  public CircularlyAccessibleValueObject[] queryTatalDataUnBal(String key)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "queryTotalData", new Object[] { key });

    String sql = "SELECT sd.csaleid  m_csaleid_h ,sd.pk_corp  m_pk_corp , sd.vreceiptcode  m_vreceiptcode ,sd.creceipttype  m_creceipttype_h , sd.dbilldate  m_dbilldate , sd.ccustomerid  m_ccustomerid , sd.cbiztype  m_cbiztype , sd.coperatorid  m_coperatorid , sd.ccalbodyid  m_ccalbodyid , sd.cwarehouseid  m_cwarehouseid , sd.dmakedate  m_dmakedate , sd.capproveid  m_capproveid ,  sd.dapprovedate  m_dapprovedate ,  sd.fstatus  m_fstatus , sd.cdeptid  m_cdeptid ,sd.cemployeeid  m_cemployeeid ,sd.vdef1  m_vdef1_h ,sd.vdef2  m_vdef2_h ,sd.vdef3  m_vdef3_h ,sd.vdef4  m_vdef4_h ,sd.vdef5  m_vdef5_h ,sd.vdef6  m_vdef6_h ,sd.vdef7  m_vdef7_h ,sd.vdef8  m_vdef8_h ,sd.vdef9  m_vdef9_h ,sd.vdef10  m_vdef10_h ,so_square.ts  m_ts_h ,so_square.dr  m_dr_h ,sd.verifyrule  m_verifyrule ,sd.ctermprotocolid  m_ctermprotocolid , sb.dbizdate  m_dbizdate ,sd.corder_bid  m_corder_bid ,sd.csaleid  m_csaleid_b ,sd.csourcebillid  m_csourcebillid ,sd.csourcebillbodyid  m_csourcebillbodyid ,sb.cinvbasdocid  m_cinvbasdocid ,sd.cinventoryid  m_cinventoryid ,sd.noutnum  m_noutnum ,sd.nshouldoutnum  m_nshouldoutnum ,sd.nnewbalancenum  m_nnewbalancenum ,sd.nsignnum  m_nsignnum ,sd.currencytypeid  m_ccurrencytypeid ,sd.noriginalcurtaxnetprice  m_noriginalcurtaxnetprice ,sd.noriginalcurmny  m_noriginalcurmny ,sd.noriginalcursummny  m_noriginalcursummny ,sd.bifpaybalance  m_bifpaybalance ,sd.cbatchid  m_cbatchid ,sd.nexchangeotobrate  m_nexchangeotobrate ,sd.nexchangeotoarate  m_nexchangeotoarate ,sd.ntaxrate  m_ntaxrate ,sd.noriginalcurnetprice  m_noriginalcurnetprice ,sd.noriginalcurtaxmny  m_noriginalcurtaxmny ,sd.ntaxmny  m_ntaxmny ,sd.nmny  m_nmny ,sd.nsummny  m_nsummny ,sd.nassistcursummny  m_nassistcursummny ,sd.nassistcurmny  m_nassistcurmny ,sd.nassistcurtaxmny  m_nassistcurtaxmny ,sd.cprojectid  m_cprojectid ,sd.cprojectphaseid  m_cprojectphaseid ,sd.vfree1  m_vfree1 ,sd.vfree2  m_vfree2 ,sd.vfree3  m_vfree3 ,sd.vfree4  m_vfree4 ,sd.vfree5  m_vfree5 ,sd.vbodydef1  m_vdef1_b ,sd.vbodydef2  m_vdef2_b ,sd.vbodydef3  m_vdef3_b ,sd.vbodydef4  m_vdef4_b ,sd.vbodydef5  m_vdef5_b ,sd.vbodydef6  m_vdef6_b ,sb.ts  m_ts_b ,sb.dr  m_dr_b ,sd.csourcereceipttype  m_creceipttype_b ,sd.cupreceipttype  m_cupreceipttype ,sd.cupbillid  m_cupbillid ,sd.cupbillbodyid  m_cupbillbodyid ,sd.blargessflag  m_blargessflag ,sd.discountflag  m_discountflag ,sd.laborflag  m_laborflag ,sd.cbodywarehouseid  m_cbodywarehouseid ,sd.ncostmny  m_ncostmny ,sd.cpackunitid  m_cpackunitid ,sd.scalefactor  m_scalefactor ,sd.nbalancemny  m_nbalancemny ,sd.noriginalcurdiscountmny  m_noriginalcurdiscountmny ,sd.nnetprice  m_nnetprice ,sd.ntaxnetprice  m_ntaxnetprice ,sd.ndiscountmny  m_ndiscountmny ,so_square.cdispatcherid ,sb.cbodycalbodyid , sb.npacknumber ,sb.cprolineid , sb.nreturntaxrate, sd.pk_squaredetail,so_square.bincomeflag,so_square.bcostflag, sd.cquoteunitid,sd.nquoteunitnum,sd.nquoteunitrate,sd.nqttaxnetprc,sd.nqtnetprc,sd.nqttaxprc,sd.nqtprc, sd.norgqttaxnetprc,sd.norgqtnetprc,sd.norgqttaxprc,sd.norgqtprc,sd.carhid,sd.carbid,sd.ciahid,sd.ciabid,so_square.bautoincomeflag,sb.nbalancenum,sd.clbh,so_square.bestimation  FROM so_squaredetail sd  join so_square_b sb on sd.corder_bid=sb.corder_bid  join so_square  on so_square.csaleid=sb.csaleid where " + key;

    SCMEnv.out(sql);
    SquareTotalVO[] allTotal = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      ResultSet rs = stmt.executeQuery();

      while (rs.next())
      {
        SquareTotalVO squareTotal = null;
        squareTotal = new SquareTotalVO();

        String m_csaleid_h = rs.getString(1);
        squareTotal.setAttributeValue("csaleid_h", m_csaleid_h == null ? null : m_csaleid_h.trim());

        String m_pk_corp = rs.getString(2);
        squareTotal.setAttributeValue("pk_corp", m_pk_corp == null ? null : m_pk_corp.trim());

        String m_vreceiptcode = rs.getString(3);
        squareTotal.setAttributeValue("vreceiptcode", m_vreceiptcode == null ? null : m_vreceiptcode.trim());

        String m_creceipttype_h = rs.getString(4);
        squareTotal.setAttributeValue("creceipttype_h", m_creceipttype_h == null ? null : m_creceipttype_h.trim());

        String m_dbilldate = rs.getString(5);
        squareTotal.setAttributeValue("dbilldate", m_dbilldate == null ? null : new UFDate(m_dbilldate.trim(), false));

        String m_ccustomerid = rs.getString(6);
        squareTotal.setAttributeValue("ccustomerid", m_ccustomerid == null ? null : m_ccustomerid.trim());

        String m_cbiztype = rs.getString(7);
        squareTotal.setAttributeValue("cbiztype", m_cbiztype == null ? null : m_cbiztype.trim());

        String m_coperatorid = rs.getString(8);
        squareTotal.setAttributeValue("coperatorid", m_coperatorid == null ? null : m_coperatorid.trim());

        String m_ccalbodyid = rs.getString(9);
        squareTotal.setAttributeValue("ccalbodyid", m_ccalbodyid == null ? null : m_ccalbodyid.trim());

        String m_cwarehouseid = rs.getString(10);
        squareTotal.setAttributeValue("cwarehouseid", m_cwarehouseid == null ? null : m_cwarehouseid.trim());

        String m_dmakedate = rs.getString(11);
        squareTotal.setAttributeValue("dmakedate", m_dmakedate == null ? null : new UFDate(m_dmakedate.trim(), false));

        String m_capproveid = rs.getString(12);
        squareTotal.setAttributeValue("capproveid", m_capproveid == null ? null : m_capproveid.trim());

        String m_dapprovedate = rs.getString(13);
        squareTotal.setAttributeValue("dapprovedate", m_dapprovedate == null ? null : new UFDate(m_dapprovedate.trim(), false));

        Integer m_fstatus = (Integer)rs.getObject(14);
        squareTotal.setAttributeValue("fstatus", m_fstatus == null ? null : m_fstatus);

        String m_cdeptid = rs.getString(15);
        squareTotal.setAttributeValue("cdeptid", m_cdeptid == null ? null : m_cdeptid.trim());

        String m_cemployeeid = rs.getString(16);
        squareTotal.setAttributeValue("cemployeeid", m_cemployeeid == null ? null : m_cemployeeid.trim());

        String m_vdef1_h = rs.getString(17);
        squareTotal.setAttributeValue("vdef1_h", m_vdef1_h == null ? null : m_vdef1_h.trim());

        String m_vdef2_h = rs.getString(18);
        squareTotal.setAttributeValue("vdef2_h", m_vdef2_h == null ? null : m_vdef2_h.trim());

        String m_vdef3_h = rs.getString(19);
        squareTotal.setAttributeValue("vdef3_h", m_vdef3_h == null ? null : m_vdef3_h.trim());

        String m_vdef4_h = rs.getString(20);
        squareTotal.setAttributeValue("vdef4_h", m_vdef4_h == null ? null : m_vdef4_h.trim());

        String m_vdef5_h = rs.getString(21);
        squareTotal.setAttributeValue("vdef5_h", m_vdef5_h == null ? null : m_vdef5_h.trim());

        String m_vdef6_h = rs.getString(22);
        squareTotal.setAttributeValue("vdef6_h", m_vdef6_h == null ? null : m_vdef6_h.trim());

        String m_vdef7_h = rs.getString(23);
        squareTotal.setAttributeValue("vdef7_h", m_vdef7_h == null ? null : m_vdef7_h.trim());

        String m_vdef8_h = rs.getString(24);
        squareTotal.setAttributeValue("vdef8_h", m_vdef8_h == null ? null : m_vdef8_h.trim());

        String m_vdef9_h = rs.getString(25);
        squareTotal.setAttributeValue("vdef9_h", m_vdef9_h == null ? null : m_vdef9_h.trim());

        String m_vdef10_h = rs.getString(26);
        squareTotal.setAttributeValue("vdef10_h", m_vdef10_h == null ? null : m_vdef10_h.trim());

        String m_ts_h = rs.getString(27);
        squareTotal.setAttributeValue("ts_h", m_ts_h == null ? null : new UFDateTime(m_ts_h.trim(), false));

        Integer m_dr_h = (Integer)rs.getObject(28);
        squareTotal.setAttributeValue("dr_h", m_dr_h == null ? null : m_dr_h);

        String m_verifyrule = rs.getString(29);
        squareTotal.setAttributeValue("verifyrule", m_verifyrule == null ? null : m_verifyrule.trim());

        String m_ctermprotocolid = rs.getString(30);
        squareTotal.setAttributeValue("ctermprotocolid", m_ctermprotocolid == null ? null : m_ctermprotocolid.trim());

        String m_dbizdate = rs.getString(31);
        squareTotal.setAttributeValue("dbizdate", m_dbizdate == null ? null : new UFDate(m_dbizdate.trim(), false));

        String m_corder_bid = rs.getString(32);
        squareTotal.setAttributeValue("corder_bid", m_corder_bid == null ? null : m_corder_bid.trim());

        String m_csaleid_b = rs.getString(33);
        squareTotal.setAttributeValue("csaleid_b", m_csaleid_b == null ? null : m_csaleid_b.trim());

        String m_csourcebillid = rs.getString(34);
        squareTotal.setAttributeValue("csourcebillid", m_csourcebillid == null ? null : m_csourcebillid.trim());

        String m_csourcebillbodyid = rs.getString(35);
        squareTotal.setAttributeValue("csourcebillbodyid", m_csourcebillbodyid == null ? null : m_csourcebillbodyid.trim());

        String m_cinvbasdocid = rs.getString(36);
        squareTotal.setAttributeValue("cinvbasdocid", m_cinvbasdocid == null ? null : m_cinvbasdocid.trim());

        String m_cinventoryid = rs.getString(37);
        squareTotal.setAttributeValue("cinventoryid", m_cinventoryid == null ? null : m_cinventoryid.trim());

        BigDecimal m_noutnum = (BigDecimal)rs.getObject(38);
        squareTotal.setAttributeValue("noutnum", m_noutnum == null ? null : new UFDouble(m_noutnum));

        BigDecimal m_nshouldoutnum = (BigDecimal)rs.getObject(39);
        squareTotal.setAttributeValue("nshouldoutnum", m_nshouldoutnum == null ? null : new UFDouble(m_nshouldoutnum));

        BigDecimal m_nbalancenum = (BigDecimal)rs.getObject(40);
        squareTotal.setAttributeValue("nnewbalancenum", m_nbalancenum == null ? null : new UFDouble(m_nbalancenum));

        BigDecimal m_nsignnum = (BigDecimal)rs.getObject(41);
        squareTotal.setAttributeValue("nsignnum", m_nsignnum == null ? null : new UFDouble(m_nsignnum));

        String m_ccurrencytypeid = rs.getString(42);
        squareTotal.setAttributeValue("ccurrencytypeid", m_ccurrencytypeid == null ? null : m_ccurrencytypeid.trim());

        BigDecimal m_noriginalcurtaxnetprice = (BigDecimal)rs.getObject(43);
        squareTotal.setAttributeValue("noriginalcurtaxnetprice", m_noriginalcurtaxnetprice == null ? null : new UFDouble(m_noriginalcurtaxnetprice));

        BigDecimal m_noriginalcurmny = (BigDecimal)rs.getObject(44);
        squareTotal.setAttributeValue("noriginalcurmny", m_noriginalcurmny == null ? null : new UFDouble(m_noriginalcurmny));

        BigDecimal m_noriginalcursummny = (BigDecimal)rs.getObject(45);
        squareTotal.setAttributeValue("noriginalcursummny", m_noriginalcursummny == null ? null : new UFDouble(m_noriginalcursummny));

        String m_bifpaybalance = rs.getString(46);
        squareTotal.setAttributeValue("bifpaybalance", m_bifpaybalance == null ? null : new UFBoolean(m_bifpaybalance.trim()));

        String m_cbatchid = rs.getString(47);
        squareTotal.setAttributeValue("cbatchid", m_cbatchid == null ? null : m_cbatchid.trim());

        BigDecimal m_nexchangeotobrate = (BigDecimal)rs.getObject(48);
        squareTotal.setAttributeValue("nexchangeotobrate", m_nexchangeotobrate == null ? null : new UFDouble(m_nexchangeotobrate));

        BigDecimal m_nexchangeotoarate = (BigDecimal)rs.getObject(49);
        squareTotal.setAttributeValue("nexchangeotoarate", m_nexchangeotoarate == null ? null : new UFDouble(m_nexchangeotoarate));

        BigDecimal m_ntaxrate = (BigDecimal)rs.getObject(50);
        squareTotal.setAttributeValue("ntaxrate", m_ntaxrate == null ? null : new UFDouble(m_ntaxrate));

        BigDecimal m_noriginalcurnetprice = (BigDecimal)rs.getObject(51);
        squareTotal.setAttributeValue("noriginalcurnetprice", m_noriginalcurnetprice == null ? null : new UFDouble(m_noriginalcurnetprice));

        BigDecimal m_noriginalcurtaxmny = (BigDecimal)rs.getObject(52);
        squareTotal.setAttributeValue("noriginalcurtaxmny", m_noriginalcurtaxmny == null ? null : new UFDouble(m_noriginalcurtaxmny));

        BigDecimal m_ntaxmny = (BigDecimal)rs.getObject(53);
        squareTotal.setAttributeValue("ntaxmny", m_ntaxmny == null ? null : new UFDouble(m_ntaxmny));

        BigDecimal m_nmny = (BigDecimal)rs.getObject(54);
        squareTotal.setAttributeValue("nmny", m_nmny == null ? null : new UFDouble(m_nmny));

        BigDecimal m_nsummny = (BigDecimal)rs.getObject(55);
        squareTotal.setAttributeValue("nsummny", m_nsummny == null ? null : new UFDouble(m_nsummny));

        BigDecimal m_nassistcursummny = (BigDecimal)rs.getObject(56);
        squareTotal.setAttributeValue("nassistcursummny", m_nassistcursummny == null ? null : new UFDouble(m_nassistcursummny));

        BigDecimal m_nassistcurmny = (BigDecimal)rs.getObject(57);
        squareTotal.setAttributeValue("nassistcurmny", m_nassistcurmny == null ? null : new UFDouble(m_nassistcurmny));

        BigDecimal m_nassistcurtaxmny = (BigDecimal)rs.getObject(58);
        squareTotal.setAttributeValue("nassistcurtaxmny", m_nassistcurtaxmny == null ? null : new UFDouble(m_nassistcurtaxmny));

        String m_cprojectid = rs.getString(59);
        squareTotal.setAttributeValue("cprojectid", m_cprojectid == null ? null : m_cprojectid.trim());

        String m_cprojectphaseid = rs.getString(60);
        squareTotal.setAttributeValue("cprojectphaseid", m_cprojectphaseid == null ? null : m_cprojectphaseid.trim());

        String m_vfree1 = rs.getString(61);
        squareTotal.setAttributeValue("vfree1", m_vfree1 == null ? null : m_vfree1.trim());

        String m_vfree2 = rs.getString(62);
        squareTotal.setAttributeValue("vfree2", m_vfree2 == null ? null : m_vfree2.trim());

        String m_vfree3 = rs.getString(63);
        squareTotal.setAttributeValue("vfree3", m_vfree3 == null ? null : m_vfree3.trim());

        String m_vfree4 = rs.getString(64);
        squareTotal.setAttributeValue("vfree4", m_vfree4 == null ? null : m_vfree4.trim());

        String m_vfree5 = rs.getString(65);
        squareTotal.setAttributeValue("vfree5", m_vfree5 == null ? null : m_vfree5.trim());

        String m_vdef1_b = rs.getString(66);
        squareTotal.setAttributeValue("vdef1_b", m_vdef1_b == null ? null : m_vdef1_b.trim());

        String m_vdef2_b = rs.getString(67);
        squareTotal.setAttributeValue("vdef2_b", m_vdef2_b == null ? null : m_vdef2_b.trim());

        String m_vdef3_b = rs.getString(68);
        squareTotal.setAttributeValue("vdef3_b", m_vdef3_b == null ? null : m_vdef3_b.trim());

        String m_vdef4_b = rs.getString(69);
        squareTotal.setAttributeValue("vdef4_b", m_vdef4_b == null ? null : m_vdef4_b.trim());

        String m_vdef5_b = rs.getString(70);
        squareTotal.setAttributeValue("vdef5_b", m_vdef5_b == null ? null : m_vdef5_b.trim());

        String m_vdef6_b = rs.getString(71);
        squareTotal.setAttributeValue("vdef6_b", m_vdef6_b == null ? null : m_vdef6_b.trim());

        String m_ts_b = rs.getString(72);
        squareTotal.setAttributeValue("ts_b", m_ts_b == null ? null : new UFDateTime(m_ts_b.trim(), false));

        Integer m_dr_b = (Integer)rs.getObject(73);
        squareTotal.setAttributeValue("dr_b", m_dr_b == null ? null : m_dr_b);

        String m_creceipttype_b = rs.getString(74);
        squareTotal.setAttributeValue("creceipttype_b", m_creceipttype_b == null ? null : m_creceipttype_b.trim());

        String m_cupreceipttype = rs.getString(75);
        squareTotal.setAttributeValue("cupreceipttype", m_cupreceipttype == null ? null : m_cupreceipttype.trim());

        String m_cupbillid = rs.getString(76);
        squareTotal.setAttributeValue("cupbillid", m_cupbillid == null ? null : m_cupbillid.trim());

        String m_cupbillbodyid = rs.getString(77);
        squareTotal.setAttributeValue("cupbillbodyid", m_cupbillbodyid == null ? null : m_cupbillbodyid.trim());

        String m_blargessflag = rs.getString(78);
        squareTotal.setAttributeValue("blargessflag", m_blargessflag == null ? null : new UFBoolean(m_blargessflag.trim()));

        String m_discountflag = rs.getString(79);
        squareTotal.setAttributeValue("discountflag", m_discountflag == null ? null : new UFBoolean(m_discountflag.trim()));

        String m_laborflag = rs.getString(80);
        squareTotal.setAttributeValue("laborflag", m_laborflag == null ? null : new UFBoolean(m_laborflag.trim()));

        String m_cbodywarehouseid = rs.getString(81);
        squareTotal.setAttributeValue("cbodywarehouseid", m_cbodywarehouseid == null ? null : m_cbodywarehouseid.trim());

        BigDecimal m_ncostmny = (BigDecimal)rs.getObject(82);
        squareTotal.setAttributeValue("ncostmny", m_ncostmny == null ? null : new UFDouble(m_ncostmny));

        String m_cpackunitid = rs.getString(83);
        squareTotal.setAttributeValue("cpackunitid", m_cpackunitid == null ? null : m_cpackunitid.trim());

        BigDecimal m_scalefactor = (BigDecimal)rs.getObject(84);
        squareTotal.setAttributeValue("scalefactor", m_scalefactor == null ? null : new UFDouble(m_scalefactor));

        BigDecimal m_nbalancemny = (BigDecimal)rs.getObject(85);
        squareTotal.setAttributeValue("nbalancemny", m_nbalancemny == null ? null : new UFDouble(m_nbalancemny));

        BigDecimal m_noriginalcurdiscountmny = (BigDecimal)rs.getObject(86);
        squareTotal.setAttributeValue("noriginalcurdiscountmny", m_noriginalcurdiscountmny == null ? null : new UFDouble(m_noriginalcurdiscountmny));

        BigDecimal m_nnetprice = (BigDecimal)rs.getObject(87);
        squareTotal.setAttributeValue("nnetprice", m_nnetprice == null ? null : new UFDouble(m_nnetprice));

        BigDecimal m_ntaxnetprice = (BigDecimal)rs.getObject(88);
        squareTotal.setAttributeValue("ntaxnetprice", m_ntaxnetprice == null ? null : new UFDouble(m_ntaxnetprice));

        BigDecimal m_ndiscountmny = (BigDecimal)rs.getObject(89);
        squareTotal.setAttributeValue("ndiscountmny", m_ndiscountmny == null ? null : new UFDouble(m_ndiscountmny));

        String sCdispatcherid = rs.getString(90);
        squareTotal.setAttributeValue("dispatcherid", sCdispatcherid == null ? null : sCdispatcherid.trim());

        String cbodycalbodyid = rs.getString(91);
        squareTotal.setAttributeValue("cbodycalbodyid", cbodycalbodyid == null ? null : cbodycalbodyid.trim());

        BigDecimal npacknumber = (BigDecimal)rs.getObject(92);
        squareTotal.setAttributeValue("npacknumber", npacknumber == null ? null : new UFDouble(npacknumber));

        String cprolineid = rs.getString(93);
        squareTotal.setAttributeValue("cprolineid", cprolineid == null ? null : cprolineid.trim());

        BigDecimal nreturntaxrate = (BigDecimal)rs.getObject(94);
        squareTotal.setAttributeValue("nreturntaxrate", nreturntaxrate == null ? null : new UFDouble(nreturntaxrate));

        String pk_squaredetail = rs.getString(95);
        squareTotal.setAttributeValue("csquareid", pk_squaredetail == null ? null : pk_squaredetail.trim());

        String bincomeflag = rs.getString(96);
        squareTotal.setAttributeValue("bincomeflag", bincomeflag == null ? null : new UFBoolean(bincomeflag.trim()));

        String bcostflag = rs.getString(97);
        squareTotal.setAttributeValue("bcostflag", bcostflag == null ? null : new UFBoolean(bcostflag.trim()));

        String cquoteunitid = rs.getString(98);
        squareTotal.setAttributeValue("cquoteunitid", cquoteunitid == null ? null : cquoteunitid.trim());

        BigDecimal nquoteunitnum = (BigDecimal)rs.getObject(99);
        squareTotal.setAttributeValue("nquoteunitnum", nquoteunitnum == null ? null : new UFDouble(nquoteunitnum));

        BigDecimal nquoteunitrate = (BigDecimal)rs.getObject(100);
        squareTotal.setAttributeValue("nquoteunitrate", nquoteunitrate == null ? null : new UFDouble(nquoteunitrate));

        BigDecimal nqttaxnetprc = (BigDecimal)rs.getObject(101);
        squareTotal.setAttributeValue("nqttaxnetprc", nqttaxnetprc == null ? null : new UFDouble(nqttaxnetprc));

        BigDecimal nqtnetprc = (BigDecimal)rs.getObject(102);
        squareTotal.setAttributeValue("nqtnetprc", nqtnetprc == null ? null : new UFDouble(nqtnetprc));

        BigDecimal nqttaxprc = (BigDecimal)rs.getObject(103);
        squareTotal.setAttributeValue("nqttaxprc", nqttaxprc == null ? null : new UFDouble(nqttaxprc));

        BigDecimal nqtprc = (BigDecimal)rs.getObject(104);
        squareTotal.setAttributeValue("nqtprc", nqtprc == null ? null : new UFDouble(nqtprc));

        BigDecimal norgqttaxnetprc = (BigDecimal)rs.getObject(105);
        squareTotal.setAttributeValue("norgqttaxnetprc", norgqttaxnetprc == null ? null : new UFDouble(norgqttaxnetprc));

        BigDecimal norgqtnetprc = (BigDecimal)rs.getObject(106);
        squareTotal.setAttributeValue("norgqtnetprc", norgqtnetprc == null ? null : new UFDouble(norgqtnetprc));

        BigDecimal norgqttaxprc = (BigDecimal)rs.getObject(107);
        squareTotal.setAttributeValue("norgqttaxprc", norgqttaxprc == null ? null : new UFDouble(norgqttaxprc));

        BigDecimal norgqtprc = (BigDecimal)rs.getObject(108);
        squareTotal.setAttributeValue("norgqtprc", norgqtprc == null ? null : new UFDouble(norgqtprc));
        v.addElement(squareTotal);

        String carhid = rs.getString(109);
        squareTotal.setAttributeValue("carhid", carhid == null ? null : carhid.trim());

        String carbid = rs.getString(110);
        squareTotal.setAttributeValue("carbid", carbid == null ? null : carbid.trim());

        String ciahid = rs.getString(111);
        squareTotal.setAttributeValue("ciahid", ciahid == null ? null : ciahid.trim());

        String ciabid = rs.getString(112);
        squareTotal.setAttributeValue("ciabid", ciabid == null ? null : ciabid.trim());

        String bautoincomeflag = rs.getString(113);
        squareTotal.setAttributeValue("bautoincomeflag", bautoincomeflag == null ? null : new UFBoolean(bautoincomeflag.trim()));

        BigDecimal m_nbalancenumA = (BigDecimal)rs.getObject(114);
        squareTotal.setAttributeValue("nbalancenum", m_nbalancenumA == null ? null : new UFDouble(m_nbalancenumA));

        String clbh = rs.getString(115);
        squareTotal.setAttributeValue("clbh", clbh == null ? null : clbh.trim());

        String bEstimation = rs.getString(116);
        squareTotal.setAttributeValue("bEstimation", bEstimation == null ? new UFBoolean(false) : new UFBoolean(bEstimation.trim()));
      }

    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
      }
    }
    allTotal = new SquareTotalVO[v.size()];
    if (v.size() > 0)
    {
      v.copyInto(allTotal);
    }

    allTotal = getScalefactor(allTotal);

    afterCallMethod("nc.bs.so.so012.SquareDMO", "queryTotalData", new Object[] { key });

    return allTotal;
  }

  public boolean setAbnormal(SquareVO vo)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "squareByInvoice", new Object[] { vo });

    Connection con = getConnection();
    PreparedStatement stmt = null;
    PreparedStatement stmtSquare = null;
    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
    for (int i = 0; i < itemVOs.length; i++) {
      String sRenewOrderNum = new String("update so_saleexecute set ntotalbalancenumber = ISNULL(ntotalbalancenumber,0) + ? where csale_bid = ? ");

      String sRenewSquareNum = new String("update so_square_b set nbalancenum = ISNULL(nbalancenum,0) + ? where corder_bid = ? ");
      try
      {
        stmt = con.prepareStatement(sRenewOrderNum);
        if (itemVOs[i].getNnewbalancenum() == null)
          stmt.setNull(1, 4);
        else {
          stmt.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
        }
        if (itemVOs[i].getCsaleid() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, itemVOs[i].getCsourcebillbodyid());
        }
        stmt.executeUpdate();
        stmt = con.prepareStatement(sRenewSquareNum);
        if (itemVOs[i].getNnewbalancenum() == null)
          stmt.setNull(1, 4);
        else {
          stmt.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
        }
        if (itemVOs[i].getCsaleid() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, itemVOs[i].getCorder_bid());
        }
        stmt.executeUpdate();
      } finally {
        try {
          if (stmt != null)
            stmt.close();
        }
        catch (Exception e) {
          return false;
        }
        try {
          if (con != null)
            con.close();
        }
        catch (Exception e) {
          return false;
        }
      }

    }

    afterCallMethod("nc.bs.so.so012.SquareDMO", "squareByInvoice", new Object[] { vo });

    return true;
  }

  public SquareVO setIsCalInvCost(SquareVO vo)
  {
    String sBillType = ((SquareHeaderVO)vo.getParentVO()).getCreceipttype();
    SquareItemVO[] itemVOs = null;

    if ((sBillType != null) && ((sBillType.equals("4C")) || (sBillType.equals("4453"))))
    {
      String sWareHouseid = ((SquareHeaderVO)vo.getParentVO()).getCwarehouseid();

      String[] sIsCalculatedInvCost = null;
      try {
        FetchValueDMO fetchValueDMO = new FetchValueDMO();
        sIsCalculatedInvCost = fetchValueDMO.getColValues("bd_stordoc", "iscalculatedinvcost", "pk_stordoc='" + sWareHouseid + "'");
      } catch (Exception e) {
        SCMEnv.out(e.getMessage());
        throw new BusinessRuntimeException(e.getMessage());
      }
      itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();

      if ((sIsCalculatedInvCost != null) && (itemVOs != null))
      {
        for (int i = 0; i < itemVOs.length; i++)
        {
          itemVOs[i].setIscalculatedinvcost(new UFBoolean(sIsCalculatedInvCost[0]));
        }

      }

    }
    else if ((sBillType != null) && (sBillType.equals("32")))
    {
      itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();

      if (itemVOs != null)
      {
        String[] sWareHouseid = new String[itemVOs.length];

        for (int i = 0; i < itemVOs.length; i++)
        {
          sWareHouseid[i] = itemVOs[i].getCbodywarehouseid();
        }

        Hashtable sIsCalculatedInvCost = null;
        try {
          FetchValueDMO fetchValueDMO = new FetchValueDMO();
          sIsCalculatedInvCost = fetchValueDMO.fetchArrayValue("bd_stordoc", "iscalculatedinvcost", "pk_stordoc", sWareHouseid);
        }
        catch (Exception e) {
          SCMEnv.out(e.getMessage());
          throw new BusinessRuntimeException(e.getMessage());
        }

        for (int i = 0; i < itemVOs.length; i++)
        {
          if (sWareHouseid[i] == null)
          {
            itemVOs[i].setIscalculatedinvcost(new UFBoolean(true));
          }
          else if ((sIsCalculatedInvCost != null) && (sIsCalculatedInvCost.containsKey(sWareHouseid[i])))
          {
            itemVOs[i].setIscalculatedinvcost(new UFBoolean(sIsCalculatedInvCost.get(sWareHouseid[i]).toString()));
          }
          else
          {
            itemVOs[i].setIscalculatedinvcost(new UFBoolean(true));
          }
        }

      }

    }

    return vo;
  }

  public boolean squareByInvoice(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "squareByInvoice", new Object[] { vo });

    long t1 = System.currentTimeMillis();

    String receipttype = ((SquareHeaderVO)vo.getParentVO()).getCreceipttype() == null ? "" : ((SquareHeaderVO)vo.getParentVO()).getCreceipttype().trim();

    if (receipttype.equals("4C"))
    {
      return squareByOut(vo);
    }
    Connection con = getConnection();
    PreparedStatement stmt = null;
    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
    String sUpdateExecute = "update so_saleexecute set ntotalbalancenumber = ISNULL(ntotalbalancenumber,0) + ? where csale_bid = ? and creceipttype= ? ";

    PreparedStatement stmtUpdateExecute = null;
    String sDelItemSql = "UPDATE so_square_b SET bifpaybalance='Y',nbalancenum = ISNULL(nbalancenum,0) + ? WHERE corder_bid = ? ";

    PreparedStatement stmtDelItem = null;
    String sUpdateItemSql = "UPDATE so_square_b SET nbalancenum = ISNULL(nbalancenum,0) + ? WHERE corder_bid = ? ";
    PreparedStatement stmtUpdateItem = null;
    try
    {
      for (int i = 0; i < itemVOs.length; i++)
      {
        if (stmtUpdateExecute == null)
          stmtUpdateExecute = prepareStatement(con, sUpdateExecute);
        if (itemVOs[i].getNnewbalancenum() == null)
        {
          stmtUpdateExecute.setNull(1, 4);
        }
        else
        {
          stmtUpdateExecute.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
        }
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateExecute.setNull(2, 1);
        }
        else
        {
          stmtUpdateExecute.setString(2, itemVOs[i].getCorder_bid());
        }

        stmtUpdateExecute.setString(3, "32");

        executeUpdate(stmtUpdateExecute);

        UFDouble dOut = itemVOs[i].getNoutnum() == null ? new UFDouble(0) : itemVOs[i].getNoutnum();
        UFDouble dBal = itemVOs[i].getNbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNbalancenum();
        UFDouble dNewbal = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();

        if (dOut.compareTo(dBal.add(dNewbal)) == 0)
        {
          if (stmtDelItem == null)
            stmtDelItem = prepareStatement(con, sDelItemSql);
          if (itemVOs[i].getNnewbalancenum() == null)
          {
            stmtDelItem.setNull(1, 4);
          }
          else
          {
            stmtDelItem.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
          }
          if (itemVOs[i].getCorder_bid() == null)
          {
            stmtDelItem.setNull(2, 1);
          }
          else
          {
            stmtDelItem.setString(2, itemVOs[i].getCorder_bid());
          }
          executeUpdate(stmtDelItem);
        }
        else
        {
          if (stmtUpdateItem == null)
            stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
          if (itemVOs[i].getNnewbalancenum() == null)
          {
            stmtUpdateItem.setNull(1, 4);
          }
          else
          {
            stmtUpdateItem.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
          }
          if (itemVOs[i].getCorder_bid() == null)
          {
            stmtUpdateItem.setNull(2, 1);
          }
          else
          {
            stmtUpdateItem.setString(2, itemVOs[i].getCorder_bid());
          }
          executeUpdate(stmtUpdateItem);
        }

        String creceipttype = itemVOs[i].getCreceipttype();

        if (creceipttype == null)
          continue;
        if (!creceipttype.equals("30"))
          continue;
        if (stmtUpdateExecute == null)
          stmtUpdateExecute = prepareStatement(con, sUpdateExecute);
        if (itemVOs[i].getNnewbalancenum() == null)
        {
          stmtUpdateExecute.setNull(1, 4);
        }
        else
        {
          stmtUpdateExecute.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
        }
        if (itemVOs[i].getCsourcebillbodyid() == null)
        {
          stmtUpdateExecute.setNull(2, 1);
        }
        else
        {
          stmtUpdateExecute.setString(2, itemVOs[i].getCsourcebillbodyid());
        }

        stmtUpdateExecute.setString(3, "30");

        executeUpdate(stmtUpdateExecute);
      }

      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtDelItem != null)
        executeBatch(stmtDelItem);
      if (stmtUpdateItem != null) {
        executeBatch(stmtUpdateItem);
      }
      String sqlCheck = "SELECT csaleid FROM so_square_b WHERE csaleid = ? AND (bifpaybalance='N' OR bifpaybalance IS NULL) AND dr=0";

      stmt = con.prepareStatement(sqlCheck);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      ResultSet rs = stmt.executeQuery();

      if (!rs.next()) {
        stmt.close();
        String sUpdateStatusSql = "UPDATE so_square SET fstatus=9 WHERE csaleid=? ";
        stmt = con.prepareStatement(sUpdateStatusSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }
      else
      {
        stmt.close();

        String sUpdateTsSql = new String("UPDATE so_square SET pk_corp = pk_corp where csaleid=?");
        stmt = con.prepareStatement(sUpdateTsSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }
    }
    finally
    {
      long t2 = System.currentTimeMillis();
      SCMEnv.out("结算耗时：" + (t2 - t1));
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtDelItem != null)
          stmtDelItem.close();
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
      }
      catch (Exception e)
      {
        return false;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return false;
      }
    }

    insertSquareVOArray(vo);

    afterCallMethod("nc.bs.so.so012.SquareDMO", "squareByInvoice", new Object[] { vo });

    return true;
  }

  public Hashtable[] getOutBalNumOrNum(SquareVO vo)
    throws SystemException, NamingException, SQLException
  {
    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
    ArrayList pks = new ArrayList(itemVOs.length);
    for (int i = 0; i < itemVOs.length; i++)
    {
      if ((itemVOs[i].getCupbillbodyid() != null) && (itemVOs[i].getCupreceipttype() != null) && (itemVOs[i].getCupreceipttype().equals("4C")))
        pks.add(itemVOs[i].getCupbillbodyid());
    }
    StringBuffer sbfTemp = new StringBuffer();

    sbfTemp.append(" SELECT ");
    sbfTemp.append("   cupbillbodyid,");
    sbfTemp.append("   sum(nbalancenum)");
    sbfTemp.append(" FROM ");
    sbfTemp.append("   so_square_b,");
    sbfTemp.append("   so_square");
    sbfTemp.append(" WHERE ");
    sbfTemp.append("   so_square.dr = 0 and so_square_b.dr = 0 and ");
    sbfTemp.append("   so_square.csaleid = so_square_b.csaleid and ");
    sbfTemp.append("   ( so_square.bestimation is null or so_square.bestimation = 'N') ");
    sbfTemp.append(GeneralSqlString.formInSQL("cupbillbodyid", pks));
    sbfTemp.append("   group by cupbillbodyid ");

    SmartDMO dmo = new SmartDMO();
    Object[] o = dmo.selectBy2(sbfTemp.toString());
    Object[] oneRow = null;
    Hashtable icBalMap = new Hashtable();
    Hashtable icNumMap = new Hashtable();
    if ((o != null) && (o.length > 0))
    {
      for (int i = 0; i < o.length; i++) {
        oneRow = (Object[])(Object[])o[i];
        if ((oneRow == null) || (oneRow.length <= 0))
          continue;
        icBalMap.put(oneRow[0], CreditUtil.convertObjToUFDouble(oneRow[1]));
      }
    }

    sbfTemp.setLength(0);

    sbfTemp.append("   select cgeneralbid,noutnum from  ic_general_b where dr = 0 ");
    sbfTemp.append(GeneralSqlString.formInSQL("cgeneralbid", pks));
    o = dmo.selectBy2(sbfTemp.toString());
    if ((o != null) && (o.length > 0))
    {
      for (int i = 0; i < o.length; i++) {
        oneRow = (Object[])(Object[])o[i];
        if ((oneRow == null) || (oneRow.length <= 0))
          continue;
        icNumMap.put(oneRow[0], CreditUtil.convertObjToUFDouble(oneRow[1]));
      }
    }

    return new Hashtable[] { icBalMap, icNumMap };
  }

  private static UFDouble dealBalNum(SquareItemVO item, Hashtable icNum, Hashtable icbalNum, UFDouble dtalBalIncomeNum)
  {
    if ((item.getCupbillbodyid() != null) && (icNum.containsKey(item.getCupbillbodyid())))
    {
      UFDouble balnum = CreditUtil.convertObjToUFDouble(icbalNum.get(item.getCupbillbodyid()));

      UFDouble num = balnum.add(dtalBalIncomeNum);
      UFDouble icnum = CreditUtil.convertObjToUFDouble(icNum.get(item.getCupbillbodyid()));

      if (balnum.abs().compareTo(icnum.abs()) <= 0) {
        if (num.abs().compareTo(icnum.abs()) > 0)
          dtalBalIncomeNum = num.sub(icnum);
        else
          dtalBalIncomeNum = CreditUtil.ZERO;
      }
      icbalNum.put(item.getCupbillbodyid(), num);
    }

    return dtalBalIncomeNum;
  }

  private static UFDouble dealunBalNum(SquareItemVO item, Hashtable icNum, Hashtable icbalNum, UFDouble dtalBalIncomeNum, boolean bapp)
  {
    if ((item.getCupbillbodyid() != null) && (icNum.containsKey(item.getCupbillbodyid())))
    {
      UFDouble balnum = CreditUtil.convertObjToUFDouble(icbalNum.get(item.getCupbillbodyid()));

      UFDouble num = balnum.sub(dtalBalIncomeNum);
      UFDouble icnum = CreditUtil.convertObjToUFDouble(icNum.get(item.getCupbillbodyid()));

      if (balnum.abs().compareTo(icnum.abs()) > 0) {
        if (num.abs().compareTo(icnum.abs()) <= 0) {
          if (bapp)
            dtalBalIncomeNum = icnum.sub(balnum);
          else
            dtalBalIncomeNum = balnum.sub(icnum);
        } else if (bapp) {
          dtalBalIncomeNum = dtalBalIncomeNum.multiply(-1.0D);
        }
      }
      else
        dtalBalIncomeNum = CreditUtil.ZERO;
      icbalNum.put(item.getCupbillbodyid(), num);
    }

    return dtalBalIncomeNum;
  }

  private Hashtable queryTotalBalanceMny(SquareItemVO[] vos) throws SQLException, SystemException, NamingException
  {
    ArrayList orderbids = new ArrayList(vos.length);
    for (int i = 0; i < vos.length; i++) {
      orderbids.add(vos[i].getCsourcebillbodyid());
    }

    StringBuffer sql = new StringBuffer();
    sql.append(" select fb.cksqsh , sum ( fb.jfbbje ) AS ntotalbalancemny ");
    sql.append(" from arap_djzb zb ,  ");
    sql.append("  arap_djfb fb  ");
    sql.append(" WHERE zb.djdl = 'ys'  and fb.jsfsbm <> '34' and ph = '30' ");
    sql.append(" and zb.lybz = 3 ");
    sql.append(" and fb.dr = 0 and zb.dr = 0 and  zb.vouchid = fb.vouchid ");
    sql.append(" and  fb.cksqsh in ");
    sql.append(GeneralSqlString.formSubSql("fb.cksqsh", orderbids));
    sql.append(" GROUP BY fb.cksqsh ");
    SmartDMO sdmo = new SmartDMO();

    Object[] o = sdmo.selectBy2(sql.toString());
    Hashtable table = new Hashtable();
    if ((o != null) && (o.length > 0))
    {
      for (int i = 0; i < o.length; i++) {
        Object[] onerow = (Object[])(Object[])o[i];
        if ((onerow == null) || (onerow.length <= 0))
          continue;
        table.put(onerow[0], onerow[1]);
      }
    }

    return table;
  }

  public UFDateTime squareByInvoiceNew(SquareVO vo)
    throws SQLException, SystemException, BusinessException, RemoteException, Exception
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "squareByInvoice", new Object[] { vo });

    LockBO lock = null;
    boolean bIsLock = false;
    String sCoperatorid = ((SquareHeaderVO)vo.getParentVO()).getCoperatorid();

    long t1 = System.currentTimeMillis();

    UFDateTime UFTs = null;
    boolean bFromApp = false;
    Connection con = getConnection();
    PreparedStatement stmt = null;
    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();

    UFBoolean bIncomeFlag = ((SquareHeaderVO)vo.getParentVO()).getBincomeflag();
    UFBoolean bCostFlag = ((SquareHeaderVO)vo.getParentVO()).getBcostflag();

    String[] sApplyBodyid = null;

    UFDouble[] bToAppBalIncomeNum = null;

    UFDouble[] bToAppBalCostNum = null;

    UFDouble[] bToAppBalIncomeMny = null;

    int adjust = BusiUtil.isAdjustBiztype(((SquareHeaderVO)vo.getParentVO()).getPk_corp(), ((SquareHeaderVO)vo.getParentVO()).getCbiztype());

    Hashtable icbalNum = null;
    Hashtable icNum = null;
    boolean bAdjust = false;
    Hashtable BalMny = new Hashtable(1);
    if ((adjust == 3) || ((adjust == 4) && (bIncomeFlag != null) && (bIncomeFlag.booleanValue())))
    {
      bAdjust = true;
      Hashtable[] tables = getOutBalNumOrNum(vo);
      icbalNum = tables[0];
      icNum = tables[1];
      BalMny = queryTotalBalanceMny(itemVOs);
    }
    if (icbalNum == null)
      icbalNum = new Hashtable();
    if (icNum == null)
      icNum = new Hashtable();
    Hashtable upbidMap = new Hashtable();

    if ((itemVOs != null) && (itemVOs.length != 0) && (itemVOs[0].getCreceipttype() != null) && (itemVOs[0].getCreceipttype().equals("3U")))
    {
      bFromApp = true;

      sApplyBodyid = new String[itemVOs.length];

      bToAppBalIncomeNum = new UFDouble[itemVOs.length];

      bToAppBalCostNum = new UFDouble[itemVOs.length];

      bToAppBalIncomeMny = new UFDouble[itemVOs.length];
    }

    String sUpdateExecute = "update so_saleexecute set ntotalbalancenumber = ISNULL(ntotalbalancenumber,0) + ?,ntotlbalcostnum=ISNULL(ntotlbalcostnum,0) + ?,ntalbalancemny=ISNULL(ntalbalancemny,0) + ? where csale_bid = ? and creceipttype= ? ";

    String sUpdateExecuteAdjust = "update so_saleexecute set ntotalbalancenumber = ISNULL(ntotalbalancenumber,0) + ?,ntotlbalcostnum=ISNULL(ntotlbalcostnum,0) + ?,ntalbalancemny= ? where csale_bid = ? and creceipttype= ? ";

    PreparedStatement stmtUpdateExecute = null;

    PreparedStatement stmtUpdateExecuteAdjust = null;

    String sDelItemSql = "UPDATE so_square_b SET bifpaybalance='Y',nbalancenum = ISNULL(nbalancenum,0) + ? WHERE corder_bid = ? ";

    PreparedStatement stmtDelItem = null;
    String sUpdateItemSql = "UPDATE so_square_b SET nbalancenum = ISNULL(nbalancenum,0) + ? WHERE corder_bid = ? ";
    PreparedStatement stmtUpdateItem = null;
    try
    {
      UFDouble dZero = new UFDouble(0);
      UFDouble dtalBalIncomeNum = null;
      UFDouble dtalBalIncomeMny = null;
      UFDouble dtalBalCostNum = null;

      for (int i = 0; i < itemVOs.length; i++)
      {
        if ((bIncomeFlag.booleanValue()) && (bCostFlag.booleanValue()))
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();
          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }
        else if (bIncomeFlag.booleanValue()) {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();
          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = dZero;
        }
        else if (bCostFlag.booleanValue()) {
          dtalBalIncomeNum = dZero;
          dtalBalIncomeMny = dZero;
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }

        if (bFromApp)
        {
          sApplyBodyid[i] = itemVOs[i].getCsourcebillbodyid();

          dtalBalIncomeNum = dealBalNum(itemVOs[i], icNum, icbalNum, dtalBalIncomeNum);

          bToAppBalIncomeNum[i] = dtalBalIncomeNum;

          bToAppBalCostNum[i] = dtalBalCostNum;

          bToAppBalIncomeMny[i] = dtalBalIncomeMny;
        }

        if (stmtUpdateExecute == null) {
          stmtUpdateExecute = prepareStatement(con, sUpdateExecute);
        }
        if (dtalBalIncomeNum == null)
        {
          stmtUpdateExecute.setNull(1, 4);
        }
        else
        {
          stmtUpdateExecute.setBigDecimal(1, dtalBalIncomeNum.toBigDecimal());
        }

        if (dtalBalCostNum == null)
        {
          stmtUpdateExecute.setNull(2, 4);
        }
        else
        {
          stmtUpdateExecute.setBigDecimal(2, dtalBalCostNum.toBigDecimal());
        }

        if (dtalBalIncomeMny == null)
        {
          stmtUpdateExecute.setNull(3, 4);
        }
        else
        {
          stmtUpdateExecute.setBigDecimal(3, dtalBalIncomeMny.toBigDecimal());
        }
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateExecute.setNull(4, 1);
        }
        else
        {
          stmtUpdateExecute.setString(4, itemVOs[i].getCorder_bid());
        }
        stmtUpdateExecute.setString(5, "32");

        executeUpdate(stmtUpdateExecute);

        String creceipttype = itemVOs[i].getCreceipttype();
        if (creceipttype != null)
        {
          if (creceipttype.equals("30"))
          {
            PreparedStatement stmttemp = null;
            if (!bAdjust) {
              if (stmtUpdateExecute == null) {
                stmtUpdateExecute = prepareStatement(con, sUpdateExecute);
              }
              stmttemp = stmtUpdateExecute;
            }
            else {
              if (stmtUpdateExecuteAdjust == null) {
                stmtUpdateExecuteAdjust = prepareStatement(con, sUpdateExecuteAdjust);
              }
              stmttemp = stmtUpdateExecuteAdjust;
            }

            if (dtalBalIncomeNum == null)
            {
              stmttemp.setNull(1, 4);
            }
            else
            {
              if (!bFromApp) {
                dtalBalIncomeNum = dealBalNum(itemVOs[i], icNum, icbalNum, dtalBalIncomeNum);
              }
              stmttemp.setBigDecimal(1, dtalBalIncomeNum.toBigDecimal());
            }

            if (dtalBalCostNum == null)
            {
              stmttemp.setNull(2, 4);
            }
            else
            {
              stmttemp.setBigDecimal(2, dtalBalCostNum.toBigDecimal());
            }

            if (dtalBalIncomeMny == null)
            {
              stmttemp.setNull(3, 4);
            }
            else if (!bAdjust)
              stmttemp.setBigDecimal(3, dtalBalIncomeMny.toBigDecimal());
            else {
              stmttemp.setBigDecimal(3, CreditUtil.convertObjToUFDouble(BalMny.get(itemVOs[i].getCsourcebillbodyid())).toBigDecimal());
            }

            if (itemVOs[i].getCsourcebillbodyid() == null)
            {
              stmttemp.setNull(4, 1);
            }
            else
            {
              stmttemp.setString(4, itemVOs[i].getCsourcebillbodyid());
            }

            stmttemp.setString(5, "30");

            executeUpdate(stmttemp);
          }

        }

        UFDouble dOut = itemVOs[i].getNoutnum() == null ? new UFDouble(0) : itemVOs[i].getNoutnum();
        UFDouble dBal = itemVOs[i].getNbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNbalancenum();
        UFDouble dNewbal = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();

        if (dOut.compareTo(dBal.add(dNewbal)) == 0)
        {
          if (stmtDelItem == null)
            stmtDelItem = prepareStatement(con, sDelItemSql);
          if (itemVOs[i].getNnewbalancenum() == null)
          {
            stmtDelItem.setNull(1, 4);
          }
          else
          {
            stmtDelItem.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
          }
          if (itemVOs[i].getCorder_bid() == null)
          {
            stmtDelItem.setNull(2, 1);
          }
          else
          {
            stmtDelItem.setString(2, itemVOs[i].getCorder_bid());
          }
          executeUpdate(stmtDelItem);
        }
        else
        {
          if (stmtUpdateItem == null)
            stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
          if (itemVOs[i].getNnewbalancenum() == null)
          {
            stmtUpdateItem.setNull(1, 4);
          }
          else
          {
            stmtUpdateItem.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
          }
          if (itemVOs[i].getCorder_bid() == null)
          {
            stmtUpdateItem.setNull(2, 1);
          }
          else
          {
            stmtUpdateItem.setString(2, itemVOs[i].getCorder_bid());
          }
          executeUpdate(stmtUpdateItem);
        }
      }

      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtUpdateExecuteAdjust != null)
        executeBatch(stmtUpdateExecuteAdjust);
      if (stmtDelItem != null)
        executeBatch(stmtDelItem);
      if (stmtUpdateItem != null) {
        executeBatch(stmtUpdateItem);
      }

      String sqlCheck = "SELECT csaleid FROM so_square_b WHERE csaleid = ? AND (bifpaybalance='N' OR bifpaybalance IS NULL) AND dr=0";

      stmt = con.prepareStatement(sqlCheck);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      ResultSet rs = stmt.executeQuery();

      if (!rs.next())
      {
        stmt.close();
        String sUpdateStatusSql = "UPDATE so_square SET fstatus=9 WHERE csaleid=? ";
        stmt = con.prepareStatement(sUpdateStatusSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }
      else
      {
        stmt.close();

        String sUpdateTsSql = new String("UPDATE so_square SET pk_corp = pk_corp where csaleid=?");
        stmt = con.prepareStatement(sUpdateTsSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }

      String sSqlTs = "select ts from so_square where csaleid=? ";

      stmt = con.prepareStatement(sSqlTs);

      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }

      ResultSet rst = stmt.executeQuery();
      if (rst.next()) {
        String sts = rst.getString(1);
        UFTs = sts == null ? null : new UFDateTime(sts);
      }
      stmt.close();

      if (bFromApp)
      {
        lock = new LockBO();
        bIsLock = lock.lockPKArray(sApplyBodyid, sCoperatorid, "so_apply");

        if (!bIsLock)
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000022"));
        }

        Object obj = NCLocator.getInstance().lookup(ISOApply.class.getName());
        if (obj != null) {
          ((ISOApply)obj).reWriteTotalBal(sApplyBodyid, bToAppBalIncomeNum, bToAppBalCostNum, bToAppBalIncomeMny);
        }
      }

      String sVerifyrule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();
      if ((sVerifyrule != null) && (itemVOs != null) && (itemVOs.length > 0) && (
        (sVerifyrule.equals("W")) || (sVerifyrule.equals("F"))))
      {
        IncomeAdjust checkdmo = new IncomeAdjust();
        HashMap map = new HashMap(itemVOs.length);
        for (int i = 0; i < itemVOs.length; i++) {
          map.put(itemVOs[i].getCupbillbodyid(), itemVOs[i].getCupbillbodyid());
        }
        String[] ids = new String[map.size()];
        map.keySet().toArray(ids);
        checkdmo.checkWFCostNum(ids);
      }

    }
    finally
    {
      long t2 = System.currentTimeMillis();
      SCMEnv.out("结算耗时：" + (t2 - t1));
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtDelItem != null)
          stmtDelItem.close();
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
        if (stmtUpdateExecuteAdjust != null)
          stmtUpdateExecuteAdjust.close();
      }
      catch (Exception e)
      {
        return UFTs;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return UFTs;
      }

      if ((bIsLock == true) && (lock != null) && (sCoperatorid != null)) {
        lock.freePKArray(sApplyBodyid, sCoperatorid, "so_apply");
      }
    }

    insertSquareVOArray(vo);

    afterCallMethod("nc.bs.so.so012.SquareDMO", "squareByInvoice", new Object[] { vo });

    return UFTs;
  }

  public boolean squareByOut(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "squareByOut", new Object[] { vo });

    long t1 = System.currentTimeMillis();
    Connection con = getConnection();
    PreparedStatement stmt = null;

    String sUpdateIcSql = "UPDATE ic_general_bb3 SET naccountnum1 = ISNULL(naccountnum1,0) + ? WHERE cgeneralbid = ? ";
    PreparedStatement stmtUpdateIc = null;

    String sDelItemSql = "UPDATE so_square_b SET bifpaybalance='Y' WHERE corder_bid = ? ";
    PreparedStatement stmtDelItem = null;

    String sUpdateItemSql = "UPDATE so_square_b SET nbalancenum = ISNULL(nbalancenum,0) + ? WHERE corder_bid = ? ";
    PreparedStatement stmtUpdateItem = null;

    String sUpdateExecuteSql = "UPDATE so_saleexecute SET ntotalbalancenumber = ISNULL(ntotalbalancenumber,0) + ? WHERE csale_bid = ? and creceipttype= ? ";
    PreparedStatement stmtUpdateExecute = null;
    try
    {
      SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
      for (int i = 0; i < itemVOs.length; i++)
      {
        if (stmtUpdateIc == null)
          stmtUpdateIc = prepareStatement(con, sUpdateIcSql);
        if (itemVOs[i].getNnewbalancenum() == null)
        {
          stmtUpdateIc.setNull(1, 4);
        }
        else
        {
          stmtUpdateIc.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
        }
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateIc.setNull(2, 1);
        }
        else
        {
          stmtUpdateIc.setString(2, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateIc);

        UFDouble dOut = itemVOs[i].getNoutnum() == null ? new UFDouble(0) : itemVOs[i].getNoutnum();
        UFDouble dBal = itemVOs[i].getNbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNbalancenum();
        UFDouble dNewbal = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();
        if (dOut.compareTo(dBal.add(dNewbal)) == 0)
        {
          if (stmtDelItem == null)
            stmtDelItem = prepareStatement(con, sDelItemSql);
          if (itemVOs[i].getCorder_bid() == null)
          {
            stmtDelItem.setNull(1, 1);
          }
          else
          {
            stmtDelItem.setString(1, itemVOs[i].getCorder_bid());
          }
          executeUpdate(stmtDelItem);
        }

        if (stmtUpdateItem == null)
          stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
        if (itemVOs[i].getNnewbalancenum() == null)
        {
          stmtUpdateItem.setNull(1, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
        }
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateItem.setNull(2, 1);
        }
        else
        {
          stmtUpdateItem.setString(2, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateItem);

        String creceipttype = itemVOs[i].getCreceipttype();
        if (creceipttype != null)
        {
          if (creceipttype.equals("31"))
          {
            if (stmtUpdateExecute == null)
              stmtUpdateExecute = prepareStatement(con, sUpdateExecuteSql);
            if (itemVOs[i].getNnewbalancenum() == null)
            {
              stmtUpdateExecute.setNull(1, 4);
            }
            else
            {
              stmtUpdateExecute.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
            }
            if (itemVOs[i].getCsourcebillbodyid() == null)
            {
              stmtUpdateExecute.setNull(2, 1);
            }
            else
            {
              stmtUpdateExecute.setString(2, itemVOs[i].getCsourcebillbodyid());
            }

            stmtUpdateExecute.setString(3, "30");

            executeUpdate(stmtUpdateExecute);
          }
        }

        String sVerifyRule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();

        if ((sVerifyRule == null ? "" : sVerifyRule).equals("W")) continue; if ((sVerifyRule == null ? "" : sVerifyRule).equals("F"))
        {
          continue;
        }

        String sUpreceipttype = itemVOs[i].getCupreceipttype();
        UFDouble ufdNewbalancenum = itemVOs[i].getNnewbalancenum();
        if ((ufdNewbalancenum == null) || (sUpreceipttype == null))
          continue;
        DataControlDMO aDMO = new DataControlDMO();

        if ((sUpreceipttype.equals("7D")) || (sUpreceipttype.equals("7F")))
        {
          aDMO.setTotalBalanceNumReceipt(itemVOs[i].getCsourcebillbodyid(), ufdNewbalancenum);
        }
        else
        {
          aDMO.setTotalBalanceNum(sUpreceipttype, itemVOs[i].getCupbillbodyid(), ufdNewbalancenum);
        }

      }

      if (stmtUpdateIc != null)
        executeBatch(stmtUpdateIc);
      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtDelItem != null)
        executeBatch(stmtDelItem);
      if (stmtUpdateItem != null) {
        executeBatch(stmtUpdateItem);
      }

      String sqlCheck = "SELECT csaleid FROM so_square_b WHERE csaleid = ? AND (bifpaybalance='N' OR bifpaybalance IS NULL) AND dr=0";

      stmt = con.prepareStatement(sqlCheck);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      ResultSet rs = stmt.executeQuery();

      if (!rs.next())
      {
        stmt.close();
        String sUpdateStatusSql = "UPDATE so_square SET fstatus=9 WHERE csaleid=? ";
        stmt = con.prepareStatement(sUpdateStatusSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }
      else
      {
        stmt.close();
        String sUpdateTsSql = new String("UPDATE so_square SET pk_corp = pk_corp where csaleid=?");
        stmt = con.prepareStatement(sUpdateTsSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }

      SquaredetailDMO detailDMO = new SquaredetailDMO();
      detailDMO.insertSquareVOArray(vo);
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtDelItem != null)
          stmtDelItem.close();
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateIc != null)
          stmtUpdateIc.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
      }
      catch (Exception e)
      {
        return false;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return false;
      }
    }

    afterCallMethod("nc.bs.so.so012.SquareDMO", "squareByOut", new Object[] { vo });

    return true;
  }

  public boolean squareCancel(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException
  {
    String receipttype = ((SquareHeaderVO)vo.getParentVO()).getCreceipttype() == null ? "" : ((SquareHeaderVO)vo.getParentVO()).getCreceipttype().trim();

    if (receipttype.equals("4C"))
    {
      return squareCancelOut(vo);
    }

    vo = findByVO(vo);
    if ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000023"));
    Connection con = getConnection();
    PreparedStatement stmt = null;
    String sUpdateExecuteSql = "update so_saleexecute set ntotalbalancenumber = 0 where csale_bid = ? and creceipttype= ? ";
    PreparedStatement stmtUpdateExecute = null;
    String sUpdateItemSql = "UPDATE so_square_b SET bifpaybalance='N', nbalancenum = 0 WHERE corder_bid = ? ";
    PreparedStatement stmtUpdateItem = null;
    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
    try
    {
      for (int i = 0; i < itemVOs.length; i++)
      {
        if (stmtUpdateExecute == null)
          stmtUpdateExecute = prepareStatement(con, sUpdateExecuteSql);
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateExecute.setNull(1, 1);
        }
        else
        {
          stmtUpdateExecute.setString(1, itemVOs[i].getCorder_bid());
        }

        stmtUpdateExecute.setString(2, "32");

        executeUpdate(stmtUpdateExecute);

        UFDouble dBal = itemVOs[i].getNbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNbalancenum();

        if (stmtUpdateItem == null)
          stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateItem.setNull(1, 1);
        }
        else
        {
          stmtUpdateItem.setString(1, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateItem);

        String creceipttype = itemVOs[i].getCreceipttype();

        if (creceipttype != null)
        {
          if (creceipttype.equals("32"))
          {
            if (stmtUpdateExecute == null)
              stmtUpdateExecute = prepareStatement(con, sUpdateExecuteSql);
            if (itemVOs[i].getCsourcebillbodyid() == null)
            {
              stmtUpdateExecute.setNull(1, 1);
            }
            else
            {
              stmtUpdateExecute.setString(1, itemVOs[i].getCsourcebillbodyid());
            }

            stmtUpdateExecute.setString(2, "30");

            executeUpdate(stmtUpdateExecute);
          }
        }

        DataControlDMO aDMO = new DataControlDMO();
        UFDouble dZero = new UFDouble(0);
        aDMO.setTotalBalanceNum(receipttype, itemVOs[i].getPrimaryKey(), dZero.sub(dBal));
      }

      String sqlCheck = "UPDATE so_square SET fstatus=2 WHERE csaleid=? ";
      stmt = con.prepareStatement(sqlCheck);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      stmt.executeUpdate();

      stmt.close();
      sqlCheck = "UPDATE so_squaredetail SET dr=1 WHERE csaleid=? ";
      stmt = con.prepareStatement(sqlCheck);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      stmt.executeUpdate();
      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtUpdateItem != null)
        executeBatch(stmtUpdateItem);
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
      }
      catch (Exception e)
      {
        return false;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return false;
      }
    }
    return true;
  }

  public boolean squareCancelOut(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException
  {
    vo = findByVOOut(vo);
    if ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000023"));
    Connection con = getConnection();
    PreparedStatement stmt = null;
    String sUpdateIcSql = "UPDATE ic_general_bb3 SET naccountnum1 = 0 WHERE cgeneralbid = ? ";
    PreparedStatement stmtUpdateIc = null;
    String sUpdateItemSql = "UPDATE so_square_b SET bifpaybalance='N', nbalancenum=0 WHERE corder_bid = ? ";
    PreparedStatement stmtUpdateItem = null;
    String sUpdateExecuteSql = "UPDATE so_saleexecute SET ntotalbalancenumber=0 WHERE csale_bid = ? and creceipttype= ? ";
    PreparedStatement stmtUpdateExecute = null;
    try
    {
      SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
      for (int i = 0; i < itemVOs.length; i++)
      {
        if (stmtUpdateIc == null)
          stmtUpdateIc = prepareStatement(con, sUpdateIcSql);
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateIc.setNull(1, 1);
        }
        else
        {
          stmtUpdateIc.setString(1, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateIc);

        UFDouble dBal = itemVOs[i].getNbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNbalancenum();

        if (stmtUpdateItem == null)
          stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateItem.setNull(1, 1);
        }
        else
        {
          stmtUpdateItem.setString(1, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateItem);

        String creceipttype = itemVOs[i].getCreceipttype();
        if (creceipttype != null)
        {
          if (creceipttype.equals("31"))
          {
            if (stmtUpdateExecute == null)
              stmtUpdateExecute = prepareStatement(con, sUpdateExecuteSql);
            if (itemVOs[i].getCsourcebillbodyid() == null)
            {
              stmtUpdateExecute.setNull(1, 1);
            }
            else
            {
              stmtUpdateExecute.setString(1, itemVOs[i].getCsourcebillbodyid());
            }

            stmtUpdateExecute.setString(2, "30");

            executeUpdate(stmtUpdateExecute);
          }
        }

        String sVerifyRule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();
        if ((sVerifyRule == null ? "" : sVerifyRule).equals("W")) continue; if ((sVerifyRule == null ? "" : sVerifyRule).equals("F"))
        {
          continue;
        }

        String sUpreceipttype = itemVOs[i].getCupreceipttype();
        if (sUpreceipttype == null)
          continue;
        DataControlDMO aDMO = new DataControlDMO();
        UFDouble dZero = new UFDouble(0);

        if ((sUpreceipttype.equals("7D")) || (sUpreceipttype.equals("7F")))
        {
          aDMO.setTotalBalanceNumReceipt(itemVOs[i].getCsourcebillbodyid(), dZero.sub(dBal));
        }
        else
        {
          aDMO.setTotalBalanceNum(itemVOs[i].getCupreceipttype(), itemVOs[i].getCupbillbodyid(), dZero.sub(dBal));
        }

      }

      String sqlCheck = "UPDATE so_square SET fstatus=2 WHERE csaleid=? ";
      stmt = con.prepareStatement(sqlCheck);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      stmt.executeUpdate();

      stmt.close();
      sqlCheck = "UPDATE so_squaredetail SET dr=1 WHERE csaleid=? ";
      stmt = con.prepareStatement(sqlCheck);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      stmt.executeUpdate();
      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtUpdateIc != null)
        executeBatch(stmtUpdateIc);
      if (stmtUpdateItem != null)
        executeBatch(stmtUpdateItem);
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateIc != null)
          stmtUpdateIc.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
      }
      catch (Exception e)
      {
        return false;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return false;
      }
    }
    return true;
  }

  public SquareVO updateSquare(SquareVO invo, UFDate date) throws Exception
  {
    String sPk_corp = ((SquareHeaderVO)invo.getParentVO()).getPk_corp();

    String sLocalCurrId = null;

    String sFraCurrId = null;

    UFBoolean bLocalCovMode = null;

    UFBoolean bFraCovMode = null;

    Integer iLocalDigit = null;

    Integer iFraDigit = null;

    SysInitDMO sysDMO = new SysInitDMO();

    UFBoolean bBD302 = sysDMO.getParaBoolean(sPk_corp, "BD302");

    if (bBD302 == null) {
      bBD302 = new UFBoolean(false);
    }
    BusinessCurrencyRateUtil currtype = new BusinessCurrencyRateUtil(sPk_corp);

    sLocalCurrId = currtype.getLocalCurrPK();
    sFraCurrId = currtype.getFracCurrPK();

    SquareItemVO[] itemVO = (SquareItemVO[])(SquareItemVO[])invo.getChildrenVO();
    if ((itemVO != null) && (itemVO.length > 0))
    {
      for (int i = 0; i < itemVO.length; i++)
      {
        CurrinfoVO OriVO = currtype.getCurrinfoVO(itemVO[i].getCcurrencytypeid(), null);

        if (bBD302.booleanValue())
        {
          UFBoolean zfCovMode = OriVO.getConvmode();

          UFBoolean zbCovMode = currtype.getCurrinfoVO(sFraCurrId, null).getConvmode();
          itemVO[i].setAttributeValue("bZfConvMode", zfCovMode);
          itemVO[i].setAttributeValue("bZbConvMode", zbCovMode);
        }
        else
        {
          itemVO[i].setAttributeValue("bZfConvMode", null);

          UFBoolean zbCovMode = OriVO.getConvmode();
          itemVO[i].setAttributeValue("bZbConvMode", zbCovMode);
        }

      }

    }

    ICurrtype type = (ICurrtype)NCLocator.getInstance().lookup(ICurrtype.class.getName());

    CurrtypeVO LocalVO = type.findCurrtypeVOByPK(sLocalCurrId);
    iLocalDigit = LocalVO.getCurrdigit();
    if (sFraCurrId != null)
    {
      CurrtypeVO fracVO = type.findCurrtypeVOByPK(sFraCurrId);
      iFraDigit = fracVO.getCurrdigit();
    }

    SquareHeaderVO hVO = (SquareHeaderVO)invo.getParentVO();

    AccountCalendar accPerDMO = AccountCalendar.getInstance();
    AccperiodVO accPerVO = accPerDMO.getYearVO();
    String sPeriodyear = accPerVO.getPeriodyear();

    hVO.setAttributeValue("iLocalCurDigit", iLocalDigit);

    hVO.setAttributeValue("iFraCurDigit", iFraDigit);

    hVO.setAttributeValue("cCurrid", ((SquareItemVO)invo.getChildrenVO()[0]).getCcurrencytypeid());

    hVO.setAttributeValue("cAccountYear", sPeriodyear);
    return invo;
  }

  public Object ToAr(DJZBVO d0VO, String userDate)
    throws ArapBusinessException, Exception
  {
    SCMEnv.out("-------------------向应收传递-----------------------");

    Object reObj = null;
    if (((DJZBHeaderVO)d0VO.getParentVO()).getDjrq() != null)
      SCMEnv.out(((DJZBHeaderVO)d0VO.getParentVO()).getDjrq());
    else
      SCMEnv.out("应收的单据日期为空");
    try {
      PfUtilBO pfbo = new PfUtilBO();
      reObj = pfbo.processAction("SAVE", "D0", userDate, null, d0VO, null);
    }
    catch (Exception e) {
      throw e;
    }

    return reObj;
  }

  public ArrayList ToIa(SquareVO vo, String sDate, PfParameterVO parVO)
    throws NamingException, SystemException, BusinessException, SQLException, RemoteException
  {
    SCMEnv.out("-----------------------向存货传递-------------------------------");

    long l = System.currentTimeMillis();
    HashMap hm = null;

    String sBillType = ((SquareHeaderVO)vo.getParentVO()).getCreceipttype();

    BillVO[] IaVO = new BillVO[1];

    IBill billBO = (IBill)NCLocator.getInstance().lookup(IBill.class.getName());

    SaleToIADMO dmo = new SaleToIADMO();

    SquareVO[] SqSplitVOs = dmo.splitVOForIA(vo);

    AggregatedValueObject[] vSplitAVO = SplitBillVOs.getSplitVOs(SquareVO.class.getName(), SquareHeaderVO.class.getName(), SquareItemVO.class.getName(), SqSplitVOs, new String[] { "csaleid", "cwarehouseid" }, new String[] { "cbodycalbodyid" });

    ArrayList alTotal = new ArrayList();

    for (int i = 0; i < vSplitAVO.length; i++)
    {
      SquareVO SqSplitVO = (SquareVO)vSplitAVO[i];

      IaVO[0] = ((BillVO)PfUtilTools.runChangeData("33", "I5", SqSplitVO, parVO));

      if (IaVO[0] != null) {
        IaVO[0].getParentVO().setAttributeValue("dbilldate", new UFDate(sDate));

        SCMEnv.out("-----------------------vo 对照 :" + (System.currentTimeMillis() - l) / 1000L);
        l = System.currentTimeMillis();

        hm = billBO.saveBillFromOutterArray_bill(IaVO, "SO", sBillType);

        SCMEnv.out("-----------------------存货传递单据保存 :" + (System.currentTimeMillis() - l) / 1000L);
        alTotal.add(hm);
      }
    }

    SCMEnv.out("-----------------------向存货传递完成-------------------------------");
    return alTotal;
  }

  public void SoBalance(SquareVO SquareVO, DJZBVO ArVO)
    throws Exception
  {
    boolean bFlag = false;
    IArapBillPublic Djzbbo = (IArapBillPublic)NCLocator.getInstance().lookup(IArapBillPublic.class.getName());

    IArapVerifyLogPublic Arapbo = (IArapVerifyLogPublic)NCLocator.getInstance().lookup(IArapVerifyLogPublic.class.getName());

    Object soBalToArObj = null;
    try
    {
      if (ArVO != null)
      {
        DJZBItemVO[] itemYSVOs = (DJZBItemVO[])(DJZBItemVO[])ArVO.getChildrenVO();
        SquareItemVO[] itemSquVOs = (SquareItemVO[])(SquareItemVO[])SquareVO.getChildrenVO();

        if ((itemYSVOs != null) && (itemYSVOs.length != 0))
        {
          HashMap hm = new HashMap();
          String sddhh = null;
          for (int i = 0; i < itemYSVOs.length; i++)
          {
            sddhh = itemYSVOs[i].getDdhh();
            if (sddhh != null) {
              hm.put(sddhh, itemYSVOs[i]);
            }
          }
          DJZBItemVO ysItemVO = null;

          if ((itemSquVOs != null) && (itemSquVOs.length != 0))
          {
            for (int i = 0; i < itemSquVOs.length; i++)
            {
              ysItemVO = (DJZBItemVO)hm.get(itemSquVOs[i].getCorder_bid());
              if (ysItemVO == null) {
                continue;
              }
              itemSquVOs[i].setAttributeValue("csaleorderid", itemSquVOs[i].getCsourcebillid());

              itemSquVOs[i].setAttributeValue("YSDID", ysItemVO.getAttributeValue("vouchid"));

              itemSquVOs[i].setAttributeValue("YSDROWID", ysItemVO.getAttributeValue("fb_oid"));
            }

          }

        }

        BalanceDMO dmo = new BalanceDMO();

        soBalToArObj = dmo.updateSoBalance(SquareVO);

        if (soBalToArObj != null)
        {
          DJZBHeaderVO djzbHVO = (DJZBHeaderVO)ArVO.getParentVO();

          djzbHVO.setShr(djzbHVO.getLrr());

          djzbHVO.setShrq(djzbHVO.getDjrq());

          djzbHVO.setShkjnd(djzbHVO.getDjkjnd());

          djzbHVO.setShkjqj(djzbHVO.getDjkjqj());

          CreatecorpBO createbo = new CreatecorpBO();
          String[] ss = createbo.querySettledPeriod(djzbHVO.getDwbm(), "AR");
          if ((ss != null) && (ss[0] != null) && (ss[1] != null) && (ss[1].compareTo("00") != 0)) {
            int isameYear = ss[0].compareTo(djzbHVO.getDjkjnd());
            if ((isameYear > 0) || ((isameYear == 0) && (ss[1].compareTo(djzbHVO.getDjkjqj()) >= 0))) {
              throw new RemoteException("remote call", new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000286")));
            }

          }

          bFlag = true;

          SoBalanceToArVO[] soBalTOArVOs = (SoBalanceToArVO[])(SoBalanceToArVO[])soBalToArObj;

          int i = 0; for (int loop = soBalTOArVOs.length; i < loop; i++) {
            soBalTOArVOs[i].setKJQJ(djzbHVO.getDjkjqj());
          }

          Arapbo.onSoSelectData(soBalTOArVOs, "SoPush");
        }
      }

    }
    catch (Exception e)
    {
      if ((Djzbbo != null) && (soBalToArObj != null) && (bFlag))
      {
        IArapBillPublic appbo = (IArapBillPublic)NCLocator.getInstance().lookup(IArapBillPublic.class.getName());
        try
        {
          Djzbbo.returnArapBillCode(ArVO);
        }
        catch (Exception exx) {
        }
      }
      throw e;
    }
  }

  public Object squareCancelInvoiceNew(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException, Exception, RemoteException
  {
    String receipttype = ((SquareHeaderVO)vo.getParentVO()).getCreceipttype() == null ? "" : ((SquareHeaderVO)vo.getParentVO()).getCreceipttype().trim();

    LockBO lock = null;
    boolean bIsLock = false;

    String sCoperatorid = ((SquareHeaderVO)vo.getParentVO()).getCoperatorid();

    UFBoolean bIncomeFlag = ((SquareHeaderVO)vo.getParentVO()).getBincomeflag();
    UFBoolean bCostFlag = ((SquareHeaderVO)vo.getParentVO()).getBcostflag();
    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();

    UFDouble dZero = new UFDouble(0);
    UFDouble dtalBalIncomeNum = null;
    UFDouble dtalBalIncomeMny = null;
    UFDouble dtalBalCostNum = null;

    boolean bFromApp = false;
    String[] sApplyBodyid = null;

    UFDouble[] bToAppBalIncomeNum = null;

    UFDouble[] bToAppBalCostNum = null;

    UFDouble[] bToAppBalIncomeMny = null;

    int adjust = BusiUtil.isAdjustBiztype(((SquareHeaderVO)vo.getParentVO()).getPk_corp(), ((SquareHeaderVO)vo.getParentVO()).getCbiztype());

    Hashtable icbalNum = null;
    Hashtable icNum = null;
    Hashtable BalMny = new Hashtable(1);
    boolean bAdjust = false;
    if ((adjust == 3) || ((adjust == 4) && (bIncomeFlag != null) && (bIncomeFlag.booleanValue())))
    {
      Hashtable[] tables = getOutBalNumOrNum(vo);
      icbalNum = tables[0];
      icNum = tables[1];
      BalMny = queryTotalBalanceMny(itemVOs);
      bAdjust = true;
    }
    if (icbalNum == null)
      icbalNum = new Hashtable();
    if (icNum == null) {
      icNum = new Hashtable();
    }

    if ((itemVOs != null) && (itemVOs.length != 0) && (itemVOs[0].getCreceipttype() != null) && (itemVOs[0].getCreceipttype().equals("3U")))
    {
      bFromApp = true;

      sApplyBodyid = new String[itemVOs.length];

      bToAppBalIncomeNum = new UFDouble[itemVOs.length];

      bToAppBalCostNum = new UFDouble[itemVOs.length];

      bToAppBalIncomeMny = new UFDouble[itemVOs.length];
    }

    if ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000023"));
    Connection con = getConnection();
    PreparedStatement stmt = null;
    String sUpdateExecuteSql = "update so_saleexecute set ntotalbalancenumber = ISNULL(ntotalbalancenumber,0) + ?,ntotlbalcostnum=ISNULL(ntotlbalcostnum,0) + ?,ntalbalancemny=ISNULL(ntalbalancemny,0) + ? where csale_bid = ? and creceipttype= ? ";

    String sUpdateExecuteSqlAdjust = "update so_saleexecute set ntotalbalancenumber = ISNULL(ntotalbalancenumber,0) + ?,ntotlbalcostnum=ISNULL(ntotlbalcostnum,0) + ?,ntalbalancemny= ? where csale_bid = ? and creceipttype= ? ";

    PreparedStatement stmtUpdateExecute = null;
    PreparedStatement stmtUpdateExecuteAdjust = null;
    String sUpdateItemSql = "UPDATE so_square_b SET bifpaybalance='N', nbalancenum=ISNULL(nbalancenum,0) + ? WHERE corder_bid = ? ";
    PreparedStatement stmtUpdateItem = null;

    String sqlDetail = "UPDATE so_squaredetail SET dr=1 WHERE pk_squaredetail=? ";
    PreparedStatement stmtUpdateDetail = null;

    Vector vTs = new Vector();
    try
    {
      for (int i = 0; i < itemVOs.length; i++)
      {
        if ((bIncomeFlag.booleanValue()) && (bCostFlag.booleanValue()))
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();

          dtalBalIncomeMny = itemVOs[i].getNsummny() == null ? new UFDouble(0) : itemVOs[i].getNsummny();
          dtalBalCostNum = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();
        }
        else if (bIncomeFlag.booleanValue())
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();
          dtalBalIncomeMny = itemVOs[i].getNsummny() == null ? new UFDouble(0) : itemVOs[i].getNsummny();
          dtalBalCostNum = dZero;
        }
        else if (bCostFlag.booleanValue())
        {
          dtalBalIncomeNum = dZero;
          dtalBalIncomeMny = dZero;
          dtalBalCostNum = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();
        }

        if (bFromApp)
        {
          sApplyBodyid[i] = itemVOs[i].getCsourcebillbodyid();
          dtalBalIncomeNum = dealunBalNum(itemVOs[i], icNum, icbalNum, dtalBalIncomeNum, bFromApp);

          bToAppBalIncomeNum[i] = dtalBalIncomeNum;

          bToAppBalCostNum[i] = dZero.sub(dtalBalCostNum);

          bToAppBalIncomeMny[i] = dZero.sub(dtalBalIncomeMny);
        }

        if (stmtUpdateExecute == null) {
          stmtUpdateExecute = prepareStatement(con, sUpdateExecuteSql);
        }

        stmtUpdateExecute.setBigDecimal(1, dZero.sub(dtalBalIncomeNum).toBigDecimal());

        stmtUpdateExecute.setBigDecimal(2, dZero.sub(dtalBalCostNum).toBigDecimal());

        stmtUpdateExecute.setBigDecimal(3, dZero.sub(dtalBalIncomeMny).toBigDecimal());

        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateExecute.setNull(4, 1);
        }
        else
        {
          stmtUpdateExecute.setString(4, itemVOs[i].getCorder_bid());
        }

        stmtUpdateExecute.setString(5, "32");

        executeUpdate(stmtUpdateExecute);

        UFDouble dnewBal = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();

        if (stmtUpdateItem == null) {
          stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
        }
        stmtUpdateItem.setBigDecimal(1, dZero.sub(dnewBal).toBigDecimal());

        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateItem.setNull(2, 1);
        }
        else
        {
          stmtUpdateItem.setString(2, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateItem);

        String creceipttype = itemVOs[i].getCreceipttype();
        if (creceipttype != null)
        {
          if (creceipttype.equals("30"))
          {
            PreparedStatement temp = null;
            if (!bAdjust) {
              if (stmtUpdateExecute == null)
                stmtUpdateExecute = prepareStatement(con, sUpdateExecuteSql);
              temp = stmtUpdateExecute;
            }
            else
            {
              if (stmtUpdateExecuteAdjust == null)
                stmtUpdateExecuteAdjust = prepareStatement(con, sUpdateExecuteSqlAdjust);
              temp = stmtUpdateExecuteAdjust;
            }

            if (!bFromApp) {
              dtalBalIncomeNum = dealunBalNum(itemVOs[i], icNum, icbalNum, dtalBalIncomeNum, bFromApp);
            }
            temp.setBigDecimal(1, dZero.sub(dtalBalIncomeNum).toBigDecimal());

            temp.setBigDecimal(2, dZero.sub(dtalBalCostNum).toBigDecimal());

            if (!bAdjust)
              temp.setBigDecimal(3, dZero.sub(dtalBalIncomeMny).toBigDecimal());
            else {
              temp.setBigDecimal(3, CreditUtil.convertObjToUFDouble(BalMny.get(itemVOs[i].getCsourcebillbodyid())).toBigDecimal());
            }
            if (itemVOs[i].getCsourcebillbodyid() == null)
            {
              temp.setNull(4, 1);
            }
            else
            {
              temp.setString(4, itemVOs[i].getCsourcebillbodyid());
            }

            temp.setString(5, "30");

            executeUpdate(temp);
          }

        }

        if (stmtUpdateDetail == null) {
          stmtUpdateDetail = prepareStatement(con, sqlDetail);
        }
        if (itemVOs[i].getCsquareid() == null)
        {
          stmtUpdateDetail.setNull(1, 1);
        }
        else
        {
          stmtUpdateDetail.setString(1, itemVOs[i].getCsquareid());
        }
        executeUpdate(stmtUpdateDetail);

        PreparedStatement stmtdw = null;
        String sUpdateSQL = " update so_saleexecute set ntalbalancemny=0 where ntotalbalancenumber=0 and creceipttype = '32'  and csale_bid = ?   and not exists (select cinvoice_bid from so_saleinvoice_b,bd_invbasdoc  where so_saleinvoice_b.cinvoice_bid = so_saleexecute.csale_bid  and so_saleinvoice_b.cinvbasdocid=bd_invbasdoc.pk_invbasdoc  and (bd_invbasdoc.laborflag='Y' or bd_invbasdoc.discountflag='Y')) ";

        stmtdw = con.prepareStatement(sUpdateSQL);
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtdw.setNull(1, 1);
        }
        else
        {
          stmtdw.setString(1, itemVOs[i].getCorder_bid());
        }

        stmtdw.executeUpdate();
        stmtdw.close();
      }

      String sSqlState = new String();

      sSqlState = "UPDATE so_square SET fstatus=2 WHERE csaleid =? and not EXISTS (select csaleid from so_square_b where so_square_b.csaleid=? and isnull(nbalancenum,0)!=0 or (bifpaybalance='N' and isnull(noutnum,0)=0)) ";
      stmt = con.prepareStatement(sSqlState);

      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());

        stmt.setString(2, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      stmt.executeUpdate();
      stmt.close();

      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtUpdateExecuteAdjust != null)
        executeBatch(stmtUpdateExecuteAdjust);
      if (stmtUpdateItem != null)
        executeBatch(stmtUpdateItem);
      if (stmtUpdateDetail != null) {
        executeBatch(stmtUpdateDetail);
      }

      String sSqlTs = "select ts from so_square_b where corder_bid=? ";
      stmt = con.prepareStatement(sSqlTs);

      for (int i = 0; i < itemVOs.length; i++)
      {
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, itemVOs[i].getCorder_bid());
        }

        ResultSet rst = stmt.executeQuery();
        if (rst.next()) {
          String sts = rst.getString(1);
          vTs.addElement(sts);
        }
      }
      stmt.close();

      if (bFromApp)
      {
        lock = new LockBO();
        bIsLock = lock.lockPKArray(sApplyBodyid, sCoperatorid, "so_apply");

        if (!bIsLock)
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000022"));
        }

        Object obj = NCLocator.getInstance().lookup(ISOApply.class.getName());
        if (obj != null) {
          ((ISOApply)obj).reWriteTotalBal(sApplyBodyid, bToAppBalIncomeNum, bToAppBalCostNum, bToAppBalIncomeMny);
        }
      }

      String sVerifyrule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();

      if ((sVerifyrule != null) && (itemVOs != null) && (itemVOs.length > 0) && (
        (sVerifyrule.equals("W")) || (sVerifyrule.equals("F"))))
      {
        IncomeAdjust checkdmo = new IncomeAdjust();
        HashMap map = new HashMap(itemVOs.length);
        for (int i = 0; i < itemVOs.length; i++) {
          map.put(itemVOs[i].getCupbillbodyid(), itemVOs[i].getCupbillbodyid());
        }
        String[] ids = new String[map.size()];
        map.keySet().toArray(ids);
        checkdmo.checkWFCostNum(ids);
      }

    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
        if (stmtUpdateDetail != null)
          stmtUpdateDetail.close();
        if (stmtUpdateExecuteAdjust != null) {
          stmtUpdateExecuteAdjust.close();
        }
      }
      catch (Exception e)
      {
        return vTs;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return vTs;
      }

      if ((bIsLock == true) && (lock != null) && (sCoperatorid != null)) {
        lock.freePKArray(sApplyBodyid, sCoperatorid, "so_apply");
      }
    }
    return vTs;
  }

  public Object squareCancelOutNew(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException, RemoteException, Exception
  {
    if ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000023"));
    }

    LockBO lock = null;
    boolean bIsLock = false;

    String sCoperatorid = ((SquareHeaderVO)vo.getParentVO()).getCoperatorid();

    String sVerifyRule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();

    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();

    UFBoolean bIncomeFlag = ((SquareHeaderVO)vo.getParentVO()).getBincomeflag();
    UFBoolean bCostFlag = ((SquareHeaderVO)vo.getParentVO()).getBcostflag();

    UFDouble dZero = new UFDouble(0);
    UFDouble dtalBalIncomeNum = null;
    UFDouble dtalBalIncomeMny = null;
    UFDouble dtalBalCostNum = null;

    boolean bFromApp = false;

    String[] sApplyBodyid = null;

    UFDouble[] bToAppBalIncomeNum = null;

    UFDouble[] bToAppBalCostNum = null;

    UFDouble[] bToAppBalIncomeMny = null;

    if ((itemVOs != null) && (itemVOs.length != 0) && (itemVOs[0].getCreceipttype() != null) && (itemVOs[0].getCreceipttype().equals("3U")))
    {
      bFromApp = true;

      sApplyBodyid = new String[itemVOs.length];

      bToAppBalIncomeNum = new UFDouble[itemVOs.length];

      bToAppBalCostNum = new UFDouble[itemVOs.length];

      bToAppBalIncomeMny = new UFDouble[itemVOs.length];
    }

    Connection con = getConnection();
    PreparedStatement stmt = null;
    String sUpdateIcSql = "UPDATE ic_general_bb3 SET naccountnum1 =ISNULL(naccountnum1,0) + ?  WHERE cgeneralbid = ? ";
    PreparedStatement stmtUpdateIc = null;
    String sUpdateItemSql = "UPDATE so_square_b SET bifpaybalance='N', nbalancenum=ISNULL(nbalancenum,0) + ? WHERE corder_bid = ? ";

    PreparedStatement stmtUpdateItem = null;
    String sUpdateExecuteSql = "UPDATE so_saleexecute SET ntotalbalancenumber=ISNULL(ntotalbalancenumber,0) + ? ,ntotlbalcostnum=ISNULL(ntotlbalcostnum,0) + ?,ntalbalancemny=ISNULL(ntalbalancemny,0) + ? WHERE csale_bid = ? and creceipttype= ? ";

    PreparedStatement stmtUpdateExecute = null;

    String sqlDetail = "UPDATE so_squaredetail SET dr=1 WHERE pk_squaredetail=? ";
    PreparedStatement stmtUpdateDetail = null;

    Vector vTs = new Vector();
    try
    {
      for (int i = 0; i < itemVOs.length; i++)
      {
        if ((bIncomeFlag.booleanValue()) && (bCostFlag.booleanValue()))
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();

          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }
        else if (bIncomeFlag.booleanValue())
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();
          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = dZero;
        }
        else if (bCostFlag.booleanValue())
        {
          dtalBalIncomeNum = dZero;
          dtalBalIncomeMny = dZero;
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }

        if (bFromApp)
        {
          sApplyBodyid[i] = itemVOs[i].getCsourcebillbodyid();

          bToAppBalIncomeNum[i] = dZero.sub(dtalBalIncomeNum);

          bToAppBalCostNum[i] = dZero.sub(dtalBalCostNum);

          bToAppBalIncomeMny[i] = dZero.sub(dtalBalIncomeMny);
        }

        UFDouble dnewBal = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();

        if (stmtUpdateIc == null) {
          stmtUpdateIc = prepareStatement(con, sUpdateIcSql);
        }
        stmtUpdateIc.setBigDecimal(1, dZero.sub(dnewBal).toBigDecimal());

        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateIc.setNull(2, 1);
        }
        else
        {
          stmtUpdateIc.setString(2, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateIc);

        if (stmtUpdateItem == null) {
          stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
        }
        stmtUpdateItem.setBigDecimal(1, dZero.sub(dnewBal).toBigDecimal());

        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateItem.setNull(2, 1);
        }
        else
        {
          stmtUpdateItem.setString(2, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateItem);

        if (stmtUpdateDetail == null) {
          stmtUpdateDetail = prepareStatement(con, sqlDetail);
        }
        if (itemVOs[i].getCsquareid() == null)
        {
          stmtUpdateDetail.setNull(1, 1);
        }
        else
        {
          stmtUpdateDetail.setString(1, itemVOs[i].getCsquareid());
        }
        executeUpdate(stmtUpdateDetail);

        if ((sVerifyRule == null ? "" : sVerifyRule).equals("W")) continue; if ((sVerifyRule == null ? "" : sVerifyRule).equals("F"))
        {
          continue;
        }

        String sUpreceipttype = itemVOs[i].getCupreceipttype();
        if (sUpreceipttype == null)
          continue;
        DataControlDMO aDMO = new DataControlDMO();

        if ((sUpreceipttype.equals("7D")) || (sUpreceipttype.equals("7F")) || (sUpreceipttype.equals("4Y")))
        {
          aDMO.setTotalBalanceNumReceipt(itemVOs[i].getCsourcebillbodyid(), dZero.sub(dtalBalIncomeNum), dZero.sub(dtalBalCostNum), dZero.sub(dtalBalIncomeMny));
        }
        else
        {
          aDMO.setTotalBalanceNum(itemVOs[i].getCupreceipttype(), itemVOs[i].getCupbillbodyid(), dZero.sub(dtalBalIncomeNum), dZero.sub(dtalBalCostNum), dZero.sub(dtalBalIncomeMny));
        }

      }

      String sSqlState = new String();

      sSqlState = "UPDATE so_square SET fstatus=2 WHERE csaleid =? and not EXISTS (select csaleid from so_square_b where so_square_b.csaleid=? and (nbalancenum!=0 and nbalancenum is not null)) ";
      stmt = con.prepareStatement(sSqlState);

      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());

        stmt.setString(2, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      stmt.executeUpdate();
      stmt.close();

      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtUpdateIc != null)
        executeBatch(stmtUpdateIc);
      if (stmtUpdateItem != null)
        executeBatch(stmtUpdateItem);
      if (stmtUpdateDetail != null) {
        executeBatch(stmtUpdateDetail);
      }

      String sSqlTs = "select ts from so_square_b where corder_bid=? ";
      stmt = con.prepareStatement(sSqlTs);

      for (int i = 0; i < itemVOs.length; i++)
      {
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, itemVOs[i].getCorder_bid());
        }

        ResultSet rst = stmt.executeQuery();
        if (rst.next()) {
          String sts = rst.getString(1);
          vTs.addElement(sts);
        }
      }
      stmt.close();

      if ((bFromApp) && (!sVerifyRule.equals("W")) && (!sVerifyRule.equals("F")))
      {
        lock = new LockBO();
        bIsLock = lock.lockPKArray(sApplyBodyid, sCoperatorid, "so_apply");

        if (!bIsLock)
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000022"));
        }

        Object obj = NCLocator.getInstance().lookup(ISOApply.class.getName());
        if (obj != null) {
          ((ISOApply)obj).reWriteTotalBal(sApplyBodyid, bToAppBalIncomeNum, bToAppBalCostNum, bToAppBalIncomeMny);
        }

      }

      String sVerifyrule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();
      if ((sVerifyrule != null) && (itemVOs != null) && (itemVOs.length > 0) && (
        (sVerifyrule.equals("W")) || (sVerifyrule.equals("F"))))
      {
        IncomeAdjust checkdmo = new IncomeAdjust();
        HashMap map = new HashMap(itemVOs.length);
        for (int i = 0; i < itemVOs.length; i++) {
          map.put(itemVOs[i].getPrimaryKey(), itemVOs[i].getPrimaryKey());
        }
        String[] ids = new String[map.size()];
        map.keySet().toArray(ids);
        checkdmo.checkWFCostNum(ids);
      }

    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateIc != null)
          stmtUpdateIc.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
        if (stmtUpdateDetail != null)
          stmtUpdateDetail.close();
      }
      catch (Exception e)
      {
        return vTs;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return vTs;
      }

      if ((bIsLock == true) && (lock != null) && (sCoperatorid != null))
        lock.freePKArray(sApplyBodyid, sCoperatorid, "so_apply");
    }
    return vTs;
  }

  HashMap<String, UFDouble[]> getPriceAndMnyFor4C(String csaleid) throws SystemException, NamingException, SQLException
  {
    String sql = "select corder_bid, ntaxnetprice,nsummny from so_square_b where dr = 0 and csaleid = '" + csaleid + "'";
    SmartDMO dmo = new SmartDMO();
    Object[] o = dmo.selectBy2(sql);
    HashMap map = null;
    if ((o != null) && (o.length > 0))
    {
      map = new HashMap(o.length);
      for (int i = 0; i < o.length; i++) {
        Object[] onerow = (Object[])(Object[])o[i];
        if ((onerow != null) && (onerow.length > 0)) {
          map.put(CreditUtil.convertObjToString(onerow[0]), new UFDouble[] { CreditUtil.convertObjToUFDouble(onerow[1]), CreditUtil.convertObjToUFDouble(onerow[2]) });
        }
      }
    }
    return map;
  }

  public UFDateTime squareByOutNew(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException, RemoteException, Exception
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "squareByOut", new Object[] { vo });

    long t1 = System.currentTimeMillis();
    UFDateTime UFTs = null;
    Connection con = getConnection();
    PreparedStatement stmt = null;

    String sUpdateIcSql = "UPDATE ic_general_bb3 SET naccountnum1 = ISNULL(naccountnum1,0) + ? WHERE cgeneralbid = ? ";
    PreparedStatement stmtUpdateIc = null;

    String sDelItemSql = "UPDATE so_square_b SET bifpaybalance='Y' WHERE corder_bid = ? ";
    PreparedStatement stmtDelItem = null;

    String sUpdateItemSql = "UPDATE so_square_b SET nbalancenum = ISNULL(nbalancenum,0) + ?, ccurrencytypeid = ?, cquoteunitid = ?,nexchangeotobrate = ?, nsummny =  ?,nquoteunitrate = ?, noriginalcurnetprice= ? ,noriginalcurtaxnetprice =? ,norgqttaxnetprc=?,norgqtnetprc=?,norgqttaxprc=?,norgqtprc=? WHERE corder_bid = ? ";

    PreparedStatement stmtUpdateItem = null;

    String sUpdateExecuteSql = "UPDATE so_saleexecute SET ntotalbalancenumber = ISNULL(ntotalbalancenumber,0) + ? WHERE csale_bid = ? and creceipttype= ? ";

    PreparedStatement stmtUpdateExecute = null;

    LockBO lock = null;
    boolean bIsLock = false;
    String sCoperatorid = ((SquareHeaderVO)vo.getParentVO()).getCoperatorid();

    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
    String sVerifyRule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();

    UFBoolean bIncomeFlag = ((SquareHeaderVO)vo.getParentVO()).getBincomeflag();
    UFBoolean bCostFlag = ((SquareHeaderVO)vo.getParentVO()).getBcostflag();

    UFDouble dZero = new UFDouble(0);
    UFDouble dtalBalIncomeNum = null;
    UFDouble dtalBalIncomeMny = null;
    UFDouble dtalBalCostNum = null;

    boolean bFromApp = false;

    String[] sApplyBodyid = null;

    UFDouble[] bToAppBalIncomeNum = null;

    UFDouble[] bToAppBalCostNum = null;

    UFDouble[] bToAppBalIncomeMny = null;

    if ((itemVOs != null) && (itemVOs.length != 0) && (itemVOs[0].getCreceipttype() != null) && (itemVOs[0].getCreceipttype().equals("3U")))
    {
      bFromApp = true;

      sApplyBodyid = new String[itemVOs.length];

      bToAppBalIncomeNum = new UFDouble[itemVOs.length];

      bToAppBalCostNum = new UFDouble[itemVOs.length];

      bToAppBalIncomeMny = new UFDouble[itemVOs.length];
    }

    try
    {
      HashMap map = getPriceAndMnyFor4C(vo.getParentVO().getPrimaryKey());
      DataControlDMO aDMO = new DataControlDMO();
      String sUpreceipttype = null;
      ArrayList uperbill = new ArrayList(itemVOs.length);
      ArrayList nbalnum = new ArrayList(itemVOs.length);
      ArrayList nbalcostnum = new ArrayList(itemVOs.length);
      ArrayList nbalmny = new ArrayList(itemVOs.length);

      for (int i = 0; i < itemVOs.length; i++)
      {
        if (stmtUpdateIc == null)
          stmtUpdateIc = prepareStatement(con, sUpdateIcSql);
        if (itemVOs[i].getNnewbalancenum() == null)
        {
          stmtUpdateIc.setNull(1, 4);
        }
        else
        {
          stmtUpdateIc.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
        }
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateIc.setNull(2, 1);
        }
        else
        {
          stmtUpdateIc.setString(2, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateIc);

        UFDouble dOut = itemVOs[i].getNoutnum() == null ? new UFDouble(0) : itemVOs[i].getNoutnum();
        UFDouble dBal = itemVOs[i].getNbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNbalancenum();
        UFDouble dNewbal = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();
        if (dOut.compareTo(dBal.add(dNewbal)) <= 0)
        {
          if (stmtDelItem == null)
            stmtDelItem = prepareStatement(con, sDelItemSql);
          if (itemVOs[i].getCorder_bid() == null)
          {
            stmtDelItem.setNull(1, 1);
          }
          else
          {
            stmtDelItem.setString(1, itemVOs[i].getCorder_bid());
          }
          executeUpdate(stmtDelItem);
        }

        if (stmtUpdateItem == null)
          stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
        if (itemVOs[i].getNnewbalancenum() == null)
        {
          stmtUpdateItem.setNull(1, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
        }
        if (itemVOs[i].getCcurrencytypeid() == null)
        {
          stmtUpdateItem.setNull(2, 1);
        }
        else
        {
          stmtUpdateItem.setString(2, itemVOs[i].getCcurrencytypeid());
        }

        if (itemVOs[i].getCquoteunitid() == null)
        {
          stmtUpdateItem.setNull(3, 1);
        }
        else
        {
          stmtUpdateItem.setString(3, itemVOs[i].getCquoteunitid());
        }
        if (itemVOs[i].getNexchangeotobrate() == null)
        {
          stmtUpdateItem.setNull(4, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(4, itemVOs[i].getNexchangeotobrate().toBigDecimal());
        }

        if (itemVOs[i].getNsummny() == null)
        {
          stmtUpdateItem.setNull(5, 4);
        }
        else
        {
          UFDouble[] price = (UFDouble[])map.get(itemVOs[i].getPrimaryKey());
          if ((price == null) || (price.length < 1) || (price[0] == null) || (!price[0].equals(itemVOs[i].getNtaxnetprice())))
            stmtUpdateItem.setBigDecimal(5, itemVOs[i].getNtaxnetprice().multiply(itemVOs[i].getNoutnum()).toBigDecimal());
          else {
            stmtUpdateItem.setBigDecimal(5, price[1].toBigDecimal());
          }
        }
        if (itemVOs[i].getNquoteunitrate() == null)
        {
          stmtUpdateItem.setNull(6, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(6, itemVOs[i].getNquoteunitrate().toBigDecimal());
        }

        if (itemVOs[i].getNoriginalcurnetprice() == null)
        {
          stmtUpdateItem.setNull(7, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(7, itemVOs[i].getNoriginalcurnetprice().toBigDecimal());
        }
        if (itemVOs[i].getNoriginalcurtaxnetprice() == null)
        {
          stmtUpdateItem.setNull(8, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(8, itemVOs[i].getNoriginalcurtaxnetprice().toBigDecimal());
        }
        if (itemVOs[i].getNorgqttaxnetprc() == null)
        {
          stmtUpdateItem.setNull(9, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(9, itemVOs[i].getNorgqttaxnetprc().toBigDecimal());
        }
        if (itemVOs[i].getNorgqtnetprc() == null)
        {
          stmtUpdateItem.setNull(10, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(10, itemVOs[i].getNorgqtnetprc().toBigDecimal());
        }

        if (itemVOs[i].getNorgqttaxprc() == null)
        {
          stmtUpdateItem.setNull(11, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(11, itemVOs[i].getNorgqttaxprc().toBigDecimal());
        }

        if (itemVOs[i].getNorgqtprc() == null)
        {
          stmtUpdateItem.setNull(12, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(12, itemVOs[i].getNorgqtprc().toBigDecimal());
        }

        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateItem.setNull(13, 1);
        }
        else
        {
          stmtUpdateItem.setString(13, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateItem);

        String creceipttype = itemVOs[i].getCreceipttype();

        if ((sVerifyRule == null ? "" : sVerifyRule).equals("W")) continue; if ((sVerifyRule == null ? "" : sVerifyRule).equals("F"))
        {
          continue;
        }

        sUpreceipttype = itemVOs[i].getCupreceipttype();

        if ((bIncomeFlag.booleanValue()) && (bCostFlag.booleanValue()))
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();
          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }
        else if (bIncomeFlag.booleanValue())
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();
          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = dZero;
        }
        else if (bCostFlag.booleanValue())
        {
          dtalBalIncomeNum = dZero;
          dtalBalIncomeMny = dZero;
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }

        if (bFromApp)
        {
          sApplyBodyid[i] = itemVOs[i].getCsourcebillbodyid();

          bToAppBalIncomeNum[i] = dtalBalIncomeNum;

          bToAppBalCostNum[i] = dtalBalCostNum;

          bToAppBalIncomeMny[i] = dtalBalIncomeMny;
        }

        if (sUpreceipttype == null)
        {
          continue;
        }
        nbalnum.add(dtalBalIncomeNum);
        nbalcostnum.add(dtalBalCostNum);
        nbalmny.add(dtalBalIncomeMny);

        if ((sUpreceipttype.equals("7D")) || (sUpreceipttype.equals("7F")) || (sUpreceipttype.equals("4Y")))
        {
          uperbill.add(itemVOs[i].getCsourcebillbodyid());
        }
        else
        {
          uperbill.add(itemVOs[i].getCupbillbodyid());
        }

      }

      int size = 0;
      size = uperbill.size();
      if (size > 0)
      {
        String[] aruperbill = new String[size];
        uperbill.toArray(aruperbill);
        UFDouble[] arnbalnum = new UFDouble[size];
        nbalnum.toArray(arnbalnum);
        UFDouble[] arnbalcostnum = new UFDouble[size];
        nbalcostnum.toArray(arnbalcostnum);
        UFDouble[] arnbalmny = new UFDouble[size];
        nbalmny.toArray(arnbalmny);

        if ((sUpreceipttype.equals("7D")) || (sUpreceipttype.equals("7F")) || (sUpreceipttype.equals("4Y")))
        {
          aDMO.setTotalBalanceNumReceipt(aruperbill, arnbalnum, arnbalcostnum, arnbalmny);
        }
        else
        {
          aDMO.setTotalBalanceNum(sUpreceipttype, aruperbill, arnbalnum, arnbalcostnum, arnbalmny);
        }
      }

      if (stmtUpdateIc != null)
        executeBatch(stmtUpdateIc);
      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtDelItem != null)
        executeBatch(stmtDelItem);
      if (stmtUpdateItem != null) {
        executeBatch(stmtUpdateItem);
      }

      String sqlCheck = "SELECT csaleid FROM so_square_b WHERE csaleid = ? AND (bifpaybalance='N' OR bifpaybalance IS NULL) AND dr=0";

      stmt = con.prepareStatement(sqlCheck);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      ResultSet rs = stmt.executeQuery();

      if (!rs.next())
      {
        stmt.close();
        String sUpdateStatusSql = "UPDATE so_square SET fstatus=9 WHERE csaleid=? ";
        stmt = con.prepareStatement(sUpdateStatusSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }
      else
      {
        stmt.close();
        String sUpdateTsSql = new String("UPDATE so_square SET pk_corp = pk_corp where csaleid=?");
        stmt = con.prepareStatement(sUpdateTsSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }

      String sSqlTs = "select ts from so_square where csaleid=? ";
      stmt = con.prepareStatement(sSqlTs);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }

      ResultSet rst = stmt.executeQuery();
      if (rst.next())
      {
        String sts = rst.getString(1);
        UFTs = sts == null ? null : new UFDateTime(sts);
      }
      stmt.close();

      insertSquareVOArray(vo);

      if ((bFromApp) && (!sVerifyRule.equals("W")) && (!sVerifyRule.equals("F")))
      {
        lock = new LockBO();
        bIsLock = lock.lockPKArray(sApplyBodyid, sCoperatorid, "so_apply");

        if (!bIsLock)
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000022"));
        }

        Object obj = NCLocator.getInstance().lookup(ISOApply.class.getName());
        if (obj != null) {
          ((ISOApply)obj).reWriteTotalBal(sApplyBodyid, bToAppBalIncomeNum, bToAppBalCostNum, bToAppBalIncomeMny);
        }
      }

    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtDelItem != null)
          stmtDelItem.close();
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateIc != null)
          stmtUpdateIc.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
      }
      catch (Exception e)
      {
        return UFTs;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return UFTs;
      }

      if ((bIsLock == true) && (lock != null) && (sCoperatorid != null)) {
        lock.freePKArray(sApplyBodyid, sCoperatorid, "so_apply");
      }
    }

    afterCallMethod("nc.bs.so.so012.SquareDMO", "squareByOut", new Object[] { vo });

    return UFTs;
  }

  public void priceChangeToSquare(ArrayList pList, String sOperId)
    throws BusinessException
  {
    Connection con = null;
    PreparedStatement stmt = null;

    LockBO lock = null;

    boolean bIsLock = false;
    String[] sBodyid = null;
    String[] sSourceBodyid = null;
    try
    {
      if (pList != null)
      {
        sSourceBodyid = new String[pList.size()];

        for (int i = 0; i < sSourceBodyid.length; i++)
        {
          sSourceBodyid[i] = ((Object[])(Object[])pList.get(i))[0].toString();
        }

        String sSql = "select corder_bid from so_square_b join so_square on so_square_b.csaleid=so_square.csaleid where  so_square.dr=0 and so_square_b.bifpaybalance='N'and so_square.creceipttype='4C' " + GeneralSqlString.formInSQL("csourcebillbodyid", sSourceBodyid);

        con = getConnection();
        stmt = con.prepareStatement(sSql);

        ResultSet rs = stmt.executeQuery();

        Vector vBodyid = new Vector();
        while (rs.next())
        {
          vBodyid.addElement(rs.getString(1));
        }

        stmt.close();

        if (vBodyid.size() > 0)
        {
          sBodyid = new String[vBodyid.size()];
          vBodyid.copyInto(sBodyid);

          lock = new LockBO();
          bIsLock = lock.lockPKArray(sBodyid, sOperId, "so_square_b");
        }

        if (bIsLock)
        {
          sSql = "update so_square_b set noriginalcurnetprice=? ,noriginalcurtaxnetprice=? ,noriginalcurtaxmny=?*noutnum,noriginalcurmny=?*noutnum,noriginalcursummny=?*noutnum, nnetprice=?,ntaxnetprice=?,ntaxmny=?*noutnum,nmny=?*noutnum,nsummny=?*noutnum, nassistcurtaxmny=?*noutnum,nassistcurmny=?*noutnum,nassistcursummny=?*noutnum,  nqttaxnetprc=?,nqtnetprc=?,nqttaxprc=?,nqtprc=?,norgqttaxnetprc=?,norgqtnetprc=?,norgqttaxprc=?,norgqtprc=?  where so_square_b.bifpaybalance='N' and csourcebillbodyid=?  and so_square_b.csaleid in (select so_square.csaleid from so_square where so_square.creceipttype='4C' and so_square.dr=0 and  so_square.csaleid=so_square_b.csaleid)";

          stmt = con.prepareStatement(sSql);
          int iSize = pList.size();

          for (int i = 0; i < iSize; i++)
          {
            Object[] oreturn = (Object[])(Object[])pList.get(i);
            if (oreturn == null) {
              continue;
            }
            if (oreturn[1] == null)
              stmt.setNull(1, 4);
            else {
              stmt.setBigDecimal(1, new UFDouble(oreturn[1].toString()).toBigDecimal());
            }

            if (oreturn[2] == null)
              stmt.setNull(2, 4);
            else {
              stmt.setBigDecimal(2, new UFDouble(oreturn[2].toString()).toBigDecimal());
            }

            if ((oreturn[2] == null) || (oreturn[1] == null)) {
              stmt.setNull(3, 4);
            }
            else {
              stmt.setBigDecimal(3, new UFDouble(oreturn[2].toString()).sub(new UFDouble(oreturn[1].toString())).toBigDecimal());
            }

            if (oreturn[1] == null) {
              stmt.setNull(4, 4);
            }
            else {
              stmt.setBigDecimal(4, new UFDouble(oreturn[1].toString()).toBigDecimal());
            }

            if (oreturn[2] == null) {
              stmt.setNull(5, 4);
            }
            else {
              stmt.setBigDecimal(5, new UFDouble(oreturn[2].toString()).toBigDecimal());
            }

            if (oreturn[3] == null)
              stmt.setNull(6, 4);
            else {
              stmt.setBigDecimal(6, new UFDouble(oreturn[3].toString()).toBigDecimal());
            }

            if (oreturn[4] == null)
              stmt.setNull(7, 4);
            else {
              stmt.setBigDecimal(7, new UFDouble(oreturn[4].toString()).toBigDecimal());
            }

            if ((oreturn[3] == null) || (oreturn[4] == null)) {
              stmt.setNull(8, 4);
            }
            else {
              stmt.setBigDecimal(8, new UFDouble(oreturn[4].toString()).sub(new UFDouble(oreturn[3].toString())).toBigDecimal());
            }

            if (oreturn[3] == null) {
              stmt.setNull(9, 4);
            }
            else {
              stmt.setBigDecimal(9, new UFDouble(oreturn[3].toString()).toBigDecimal());
            }

            if (oreturn[4] == null) {
              stmt.setNull(10, 4);
            }
            else {
              stmt.setBigDecimal(10, new UFDouble(oreturn[4].toString()).toBigDecimal());
            }

            if ((oreturn[5] == null) || (oreturn[6] == null)) {
              stmt.setNull(11, 4);
            }
            else {
              stmt.setBigDecimal(11, new UFDouble(oreturn[6].toString()).sub(new UFDouble(oreturn[5].toString())).toBigDecimal());
            }

            if (oreturn[5] == null) {
              stmt.setNull(12, 4);
            }
            else {
              stmt.setBigDecimal(12, new UFDouble(oreturn[5].toString()).toBigDecimal());
            }

            if (oreturn[6] == null) {
              stmt.setNull(13, 4);
            }
            else {
              stmt.setBigDecimal(13, new UFDouble(oreturn[6].toString()).toBigDecimal());
            }

            if (oreturn[7] == null)
              stmt.setNull(14, 4);
            else {
              stmt.setBigDecimal(14, new UFDouble(oreturn[7].toString()).toBigDecimal());
            }

            if (oreturn[8] == null)
              stmt.setNull(15, 4);
            else {
              stmt.setBigDecimal(15, new UFDouble(oreturn[8].toString()).toBigDecimal());
            }

            if (oreturn[9] == null)
              stmt.setNull(16, 4);
            else {
              stmt.setBigDecimal(16, new UFDouble(oreturn[9].toString()).toBigDecimal());
            }

            if (oreturn[10] == null)
              stmt.setNull(17, 4);
            else {
              stmt.setBigDecimal(17, new UFDouble(oreturn[10].toString()).toBigDecimal());
            }

            if (oreturn[11] == null)
              stmt.setNull(18, 4);
            else {
              stmt.setBigDecimal(18, new UFDouble(oreturn[11].toString()).toBigDecimal());
            }

            if (oreturn[12] == null)
              stmt.setNull(19, 4);
            else {
              stmt.setBigDecimal(19, new UFDouble(oreturn[12].toString()).toBigDecimal());
            }

            if (oreturn[13] == null)
              stmt.setNull(20, 4);
            else {
              stmt.setBigDecimal(20, new UFDouble(oreturn[13].toString()).toBigDecimal());
            }

            if (oreturn[14] == null)
              stmt.setNull(21, 4);
            else {
              stmt.setBigDecimal(21, new UFDouble(oreturn[14].toString()).toBigDecimal());
            }
            stmt.setString(22, oreturn[0].toString());

            executeUpdate(stmt);
          }

          if (stmt != null) {
            executeBatch(stmt);
          }
        }
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
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
        throw new BusinessException(e.getMessage());
      }

      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
        throw new BusinessException(e.getMessage());
      }

      if ((bIsLock == true) && (lock != null) && (sOperId != null))
        lock.freePKArray(sBodyid, sOperId, "so_square_b");
    }
  }

  public void orderChangeToSquare(ArrayList pList, String sPk_corp, String sOperId)
    throws Exception
  {
    Connection con = null;
    PreparedStatement stmt = null;

    PKLock lock = null;
    boolean bIsLock = false;
    String[] sSourceBodyid = null;
    String[] sBodyid = null;
    try
    {
      if (pList != null)
      {
        sSourceBodyid = new String[pList.size()];

        for (int i = 0; i < sSourceBodyid.length; i++)
        {
          sSourceBodyid[i] = ((Object[])(Object[])pList.get(i))[0].toString();
        }

        String sSql = "select corder_bid,nbalancenum,csourcebillbodyid from so_square_b join so_square on so_square_b.csaleid=so_square.csaleid  where  so_square.dr=0 and so_square_b.bifpaybalance='N'and so_square.creceipttype='4C' " + GeneralSqlString.formInSQL("csourcebillbodyid", sSourceBodyid);

        con = getConnection();
        stmt = con.prepareStatement(sSql);

        ResultSet rs = stmt.executeQuery();
        UFDouble nbalancenum = null;

        Object obalancenum = null;
        Vector vBodyid = new Vector();
        Vector vsource = new Vector();
        while (rs.next())
        {
          obalancenum = rs.getObject(2);
          nbalancenum = obalancenum == null ? new UFDouble(0) : new UFDouble(obalancenum.toString());

          if (nbalancenum.doubleValue() == 0.0D) {
            vBodyid.addElement(rs.getString(1));
            vsource.add(rs.getString(3));
          }
        }

        stmt.close();
        Vector vLostid = new Vector();

        if (vBodyid.size() > 0)
        {
          sBodyid = new String[vBodyid.size()];
          vBodyid.copyInto(sBodyid);

          lock = PKLock.getInstance();
          bIsLock = lock.acquireBatchLock(sBodyid, sOperId, "so_square_b");

          String sSql1 = " select ic_wastagebill_b.cfirstbillbid from ic_wastagebill_b,ic_wastagebill  where ic_wastagebill_b.cwastagebillid=ic_wastagebill.cwastagebillid  and ic_wastagebill.fstatusflag=1 " + GeneralSqlString.formInSQL("ic_wastagebill_b.csourcebillbid", sBodyid);

          SmartDMO sdmo = new SmartDMO();
          Object[] o = sdmo.selectBy2(sSql1);
          if ((o != null) && (o.length > 0)) {
            Object[] o1 = null;
            for (int i = 0; i < o.length; i++) {
              o1 = (Object[])(Object[])o[i];
              if ((o1 != null) && (o1.length > 0) && (o1[0] != null)) {
                vLostid.add(o1[0]);
              }
            }
          }

        }

        if (bIsLock)
        {
          sSql = "update so_square_b set noriginalcurnetprice=? ,noriginalcurtaxnetprice=? ,noriginalcurtaxmny=? *noutnum,noriginalcurmny=?*noutnum,noriginalcursummny=?*noutnum, nnetprice=?,ntaxnetprice=?,ntaxmny=?*noutnum ,nmny=?*noutnum,nsummny=?*noutnum, nassistcurtaxmny=?*noutnum,nassistcurmny=?*noutnum,nassistcursummny=?*noutnum,ntaxrate=?,nexchangeotobrate=?,nexchangeotoarate=?,ccurrencytypeid=? , cquoteunitid=?,nquoteunitrate=?,nquoteunitnum=?*noutnum,nqttaxnetprc=?,nqtnetprc=?,nqttaxprc=?,nqtprc=?,norgqttaxnetprc=?,norgqtnetprc=?,norgqttaxprc=?,norgqtprc=?  where so_square_b.bifpaybalance='N'  and csourcebillbodyid=? and so_square_b.csaleid in (select csaleid from so_square where so_square.creceipttype='4C' and so_square.dr=0  and so_square.csaleid=so_square_b.csaleid) " + GeneralSqlString.formInSQL("so_square_b.corder_bid", sBodyid);

          BusinessCurrencyRateUtil arith = new BusinessCurrencyRateUtil(sPk_corp);
          UFDouble dTemp = null;

          SysInitDMO sysDMO = new SysInitDMO();
          UFBoolean bBD302 = sysDMO.getParaBoolean(sPk_corp, "BD302");

          stmt = con.prepareStatement(sSql);
          int iSize = pList.size();
          for (int i = 0; i < iSize; i++)
          {
            Object[] oreturn = (Object[])(Object[])pList.get(i);

            if ((!vsource.contains(oreturn[0])) || 
              (vLostid.contains(oreturn[0])) || 
              (oreturn == null)) {
              continue;
            }
            if (oreturn[1] == null)
              stmt.setNull(1, 4);
            else {
              stmt.setBigDecimal(1, new UFDouble(oreturn[1].toString()).toBigDecimal());
            }

            if (oreturn[2] == null)
              stmt.setNull(2, 4);
            else {
              stmt.setBigDecimal(2, new UFDouble(oreturn[2].toString()).toBigDecimal());
            }

            if ((oreturn[2] == null) || (oreturn[1] == null)) {
              stmt.setNull(3, 4);
            }
            else {
              stmt.setBigDecimal(3, new UFDouble(oreturn[2].toString()).sub(new UFDouble(oreturn[1].toString())).toBigDecimal());
            }

            if (oreturn[1] == null) {
              stmt.setNull(4, 4);
            }
            else {
              stmt.setBigDecimal(4, new UFDouble(oreturn[1].toString()).toBigDecimal());
            }

            if (oreturn[2] == null) {
              stmt.setNull(5, 4);
            }
            else {
              stmt.setBigDecimal(5, new UFDouble(oreturn[2].toString()).toBigDecimal());
            }

            if (oreturn[3] == null)
              stmt.setNull(6, 4);
            else {
              stmt.setBigDecimal(6, new UFDouble(oreturn[3].toString()).toBigDecimal());
            }

            if (oreturn[4] == null)
              stmt.setNull(7, 4);
            else {
              stmt.setBigDecimal(7, new UFDouble(oreturn[4].toString()).toBigDecimal());
            }

            if ((oreturn[3] == null) || (oreturn[4] == null)) {
              stmt.setNull(8, 4);
            }
            else {
              stmt.setBigDecimal(8, new UFDouble(oreturn[4].toString()).sub(new UFDouble(oreturn[3].toString())).toBigDecimal());
            }

            if (oreturn[3] == null) {
              stmt.setNull(9, 4);
            }
            else {
              stmt.setBigDecimal(9, new UFDouble(oreturn[3].toString()).toBigDecimal());
            }

            if (oreturn[4] == null) {
              stmt.setNull(10, 4);
            }
            else {
              stmt.setBigDecimal(10, new UFDouble(oreturn[4].toString()).toBigDecimal());
            }

            if ((bBD302 != null) && (bBD302.booleanValue()))
            {
              UFDouble bRate = oreturn[7] == null ? null : new UFDouble(oreturn[7].toString());
              dTemp = arith.getAmountByOpp(arith.getFracCurrPK(), oreturn[8].toString(), new UFDouble(oreturn[2].toString()).sub(new UFDouble(oreturn[1].toString())), new UFDouble(oreturn[6].toString()), oreturn[9].toString());

              stmt.setBigDecimal(11, dTemp.toBigDecimal());

              dTemp = arith.getAmountByOpp(arith.getFracCurrPK(), oreturn[8].toString(), new UFDouble(oreturn[1].toString()), new UFDouble(oreturn[6].toString()), oreturn[9].toString());

              stmt.setBigDecimal(12, dTemp.toBigDecimal());

              dTemp = arith.getAmountByOpp(arith.getFracCurrPK(), oreturn[8].toString(), new UFDouble(oreturn[2].toString()), new UFDouble(oreturn[6].toString()), oreturn[9].toString());

              stmt.setBigDecimal(13, dTemp.toBigDecimal());
            }
            else
            {
              stmt.setNull(11, 4);

              stmt.setNull(12, 4);

              stmt.setNull(13, 4);
            }

            if (oreturn[5] == null)
              stmt.setNull(14, 4);
            else {
              stmt.setBigDecimal(14, new UFDouble(oreturn[5].toString()).toBigDecimal());
            }

            if (oreturn[6] == null)
              stmt.setNull(15, 4);
            else {
              stmt.setBigDecimal(15, new UFDouble(oreturn[6].toString()).toBigDecimal());
            }

            if (oreturn[7] == null)
              stmt.setNull(16, 4);
            else {
              stmt.setBigDecimal(16, new UFDouble(oreturn[7].toString()).toBigDecimal());
            }

            if (oreturn[8] == null)
              stmt.setNull(17, 1);
            else {
              stmt.setString(17, oreturn[8].toString());
            }

            if (oreturn[10] == null)
              stmt.setNull(18, 1);
            else {
              stmt.setString(18, oreturn[10].toString());
            }

            if (oreturn[11] == null)
              stmt.setNull(19, 4);
            else {
              stmt.setBigDecimal(19, new UFDouble(oreturn[11].toString()).toBigDecimal());
            }

            if (oreturn[11] == null)
              stmt.setNull(20, 4);
            else {
              stmt.setBigDecimal(20, new UFDouble(oreturn[11].toString()).toBigDecimal());
            }

            if (oreturn[12] == null)
              stmt.setNull(21, 4);
            else {
              stmt.setBigDecimal(21, new UFDouble(oreturn[12].toString()).toBigDecimal());
            }

            if (oreturn[13] == null)
              stmt.setNull(22, 4);
            else {
              stmt.setBigDecimal(22, new UFDouble(oreturn[13].toString()).toBigDecimal());
            }

            if (oreturn[14] == null)
              stmt.setNull(23, 4);
            else {
              stmt.setBigDecimal(23, new UFDouble(oreturn[14].toString()).toBigDecimal());
            }

            if (oreturn[15] == null)
              stmt.setNull(24, 4);
            else {
              stmt.setBigDecimal(24, new UFDouble(oreturn[15].toString()).toBigDecimal());
            }

            if (oreturn[16] == null)
              stmt.setNull(25, 4);
            else {
              stmt.setBigDecimal(25, new UFDouble(oreturn[16].toString()).toBigDecimal());
            }

            if (oreturn[17] == null)
              stmt.setNull(26, 4);
            else {
              stmt.setBigDecimal(26, new UFDouble(oreturn[17].toString()).toBigDecimal());
            }

            if (oreturn[18] == null)
              stmt.setNull(27, 4);
            else {
              stmt.setBigDecimal(27, new UFDouble(oreturn[18].toString()).toBigDecimal());
            }

            if (oreturn[19] == null)
              stmt.setNull(28, 4);
            else {
              stmt.setBigDecimal(28, new UFDouble(oreturn[19].toString()).toBigDecimal());
            }
            stmt.setString(29, oreturn[0].toString());

            executeUpdate(stmt);
          }

          if (stmt != null)
            executeBatch(stmt);
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw e;
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
        throw e;
      }

      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        SCMEnv.out(e.getMessage());
        throw e;
      }

      if ((bIsLock == true) && (lock != null) && (sOperId != null))
        lock.releaseBatchLock(sBodyid, sOperId, "so_square_b");
    }
  }

  protected Object getBeanHome(Class homeClass, String beanName)
    throws NamingException
  {
    Object objref = getInitialContext().lookup("java:comp/env/ejb/" + beanName);
    return PortableRemoteObject.narrow(objref, homeClass);
  }

  protected InitialContext getInitialContext()
    throws NamingException
  {
    return new InitialContext();
  }

  public UFDateTime squareByWastageNew(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException, RemoteException, Exception
  {
    beforeCallMethod("nc.bs.so.so012.SquareDMO", "squareByWastageNew", new Object[] { vo });

    long t1 = System.currentTimeMillis();
    UFDateTime UFTs = null;
    Connection con = getConnection();
    PreparedStatement stmt = null;

    String sDelItemSql = "UPDATE so_square_b SET bifpaybalance='Y' WHERE corder_bid = ? ";
    PreparedStatement stmtDelItem = null;

    String sUpdateItemSql = "UPDATE so_square_b SET nbalancenum = ISNULL(nbalancenum,0) + ? WHERE corder_bid = ? ";
    PreparedStatement stmtUpdateItem = null;

    String sUpdateExecuteSql = "UPDATE so_saleexecute SET ntotalbalancenumber = ISNULL(ntotalbalancenumber,0) + ? WHERE csale_bid = ? and creceipttype= ? ";

    PreparedStatement stmtUpdateExecute = null;

    LockBO lock = null;
    boolean bIsLock = false;
    String sCoperatorid = ((SquareHeaderVO)vo.getParentVO()).getCoperatorid();

    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
    String sVerifyRule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();

    UFBoolean bIncomeFlag = ((SquareHeaderVO)vo.getParentVO()).getBincomeflag();
    UFBoolean bCostFlag = ((SquareHeaderVO)vo.getParentVO()).getBcostflag();

    UFDouble dZero = new UFDouble(0);
    UFDouble dtalBalIncomeNum = null;
    UFDouble dtalBalIncomeMny = null;
    UFDouble dtalBalCostNum = null;

    boolean bFromApp = false;
    try
    {
      for (int i = 0; i < itemVOs.length; i++)
      {
        UFDouble dOut = itemVOs[i].getNoutnum() == null ? new UFDouble(0) : itemVOs[i].getNoutnum();
        UFDouble dBal = itemVOs[i].getNbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNbalancenum();
        UFDouble dNewbal = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();
        if (dOut.compareTo(dBal.add(dNewbal)) == 0)
        {
          if (stmtDelItem == null)
            stmtDelItem = prepareStatement(con, sDelItemSql);
          if (itemVOs[i].getCorder_bid() == null)
          {
            stmtDelItem.setNull(1, 1);
          }
          else
          {
            stmtDelItem.setString(1, itemVOs[i].getCorder_bid());
          }
          executeUpdate(stmtDelItem);
        }

        if (stmtUpdateItem == null)
          stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
        if (itemVOs[i].getNnewbalancenum() == null)
        {
          stmtUpdateItem.setNull(1, 4);
        }
        else
        {
          stmtUpdateItem.setBigDecimal(1, itemVOs[i].getNnewbalancenum().toBigDecimal());
        }
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateItem.setNull(2, 1);
        }
        else
        {
          stmtUpdateItem.setString(2, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateItem);

        String creceipttype = itemVOs[i].getCreceipttype();

        if ((sVerifyRule == null ? "" : sVerifyRule).equals("W")) continue; if ((sVerifyRule == null ? "" : sVerifyRule).equals("F"))
        {
          continue;
        }

        String sUpreceipttype = itemVOs[i].getCupreceipttype();

        if ((bIncomeFlag.booleanValue()) && (bCostFlag.booleanValue()))
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();
          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }
        else if (bIncomeFlag.booleanValue())
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();
          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = dZero;
        }
        else if (bCostFlag.booleanValue())
        {
          dtalBalIncomeNum = dZero;
          dtalBalIncomeMny = dZero;
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }

        if (sUpreceipttype == null)
          continue;
        DataControlDMO aDMO = new DataControlDMO();

        if ((sUpreceipttype.equals("7D")) || (sUpreceipttype.equals("7F")) || (sUpreceipttype.equals("4Y")))
        {
          aDMO.setTotalBalanceNumReceipt(itemVOs[i].getCsourcebillbodyid(), dtalBalIncomeNum, dtalBalCostNum, dtalBalIncomeMny);
        }
        else
        {
          aDMO.setTotalBalanceNum(sUpreceipttype, itemVOs[i].getCupbillbodyid(), dtalBalIncomeNum, dtalBalCostNum, dtalBalIncomeMny);
        }

      }

      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtDelItem != null)
        executeBatch(stmtDelItem);
      if (stmtUpdateItem != null) {
        executeBatch(stmtUpdateItem);
      }

      String sqlCheck = "SELECT csaleid FROM so_square_b WHERE csaleid = ? AND (bifpaybalance='N' OR bifpaybalance IS NULL) AND dr=0";

      stmt = con.prepareStatement(sqlCheck);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      ResultSet rs = stmt.executeQuery();

      if (!rs.next())
      {
        stmt.close();
        String sUpdateStatusSql = "UPDATE so_square SET fstatus=9 WHERE csaleid=? ";
        stmt = con.prepareStatement(sUpdateStatusSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }
      else
      {
        stmt.close();
        String sUpdateTsSql = new String("UPDATE so_square SET pk_corp = pk_corp where csaleid=?");
        stmt = con.prepareStatement(sUpdateTsSql);
        if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
        }
        stmt.executeUpdate();
        stmt.close();
      }

      String sSqlTs = "select ts from so_square where csaleid=? ";
      stmt = con.prepareStatement(sSqlTs);
      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }

      ResultSet rst = stmt.executeQuery();
      if (rst.next())
      {
        String sts = rst.getString(1);
        UFTs = sts == null ? null : new UFDateTime(sts);
      }
      stmt.close();

      insertSquareVOArray(vo);

      String sVerifyrule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();
      if ((sVerifyrule != null) && (itemVOs != null) && (itemVOs.length > 0) && (
        (sVerifyrule.equals("W")) || (sVerifyrule.equals("F"))))
      {
        IncomeAdjust checkdmo = new IncomeAdjust();
        HashMap map = new HashMap(itemVOs.length);
        for (int i = 0; i < itemVOs.length; i++) {
          map.put(itemVOs[i].getCupbillbodyid(), itemVOs[i].getCupbillbodyid());
        }
        String[] ids = new String[map.size()];
        map.keySet().toArray(ids);
        checkdmo.checkWFCostNum(ids);
      }
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtDelItem != null)
          stmtDelItem.close();
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
      }
      catch (Exception e)
      {
        return UFTs;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return UFTs;
      }

    }

    afterCallMethod("nc.bs.so.so012.SquareDMO", "squareByWastageNew", new Object[] { vo });

    return UFTs;
  }

  public Object squareCancelWastageNew(SquareVO vo)
    throws SQLException, SystemException, NamingException, BusinessException, RemoteException, Exception
  {
    if ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000023"));
    }

    LockBO lock = null;
    boolean bIsLock = false;

    String sCoperatorid = ((SquareHeaderVO)vo.getParentVO()).getCoperatorid();

    String sVerifyRule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();

    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();

    UFBoolean bIncomeFlag = ((SquareHeaderVO)vo.getParentVO()).getBincomeflag();
    UFBoolean bCostFlag = ((SquareHeaderVO)vo.getParentVO()).getBcostflag();

    UFDouble dZero = new UFDouble(0);
    UFDouble dtalBalIncomeNum = null;
    UFDouble dtalBalIncomeMny = null;
    UFDouble dtalBalCostNum = null;

    Connection con = getConnection();
    PreparedStatement stmt = null;

    String sUpdateItemSql = "UPDATE so_square_b SET bifpaybalance='N', nbalancenum=ISNULL(nbalancenum,0) + ?  WHERE corder_bid = ? ";

    PreparedStatement stmtUpdateItem = null;
    String sUpdateExecuteSql = "UPDATE so_saleexecute SET ntotalbalancenumber=ISNULL(ntotalbalancenumber,0) + ? ,ntotlbalcostnum=ISNULL(ntotlbalcostnum,0) + ?,ntalbalancemny=ISNULL(ntalbalancemny,0) + ? WHERE csale_bid = ? and creceipttype= ? ";

    PreparedStatement stmtUpdateExecute = null;

    String sqlDetail = "UPDATE so_squaredetail SET dr=1 WHERE pk_squaredetail=? ";
    PreparedStatement stmtUpdateDetail = null;

    Vector vTs = new Vector();
    try
    {
      for (int i = 0; i < itemVOs.length; i++)
      {
        if ((bIncomeFlag.booleanValue()) && (bCostFlag.booleanValue()))
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();

          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }
        else if (bIncomeFlag.booleanValue())
        {
          dtalBalIncomeNum = itemVOs[i].getNnewbalancenum();
          dtalBalIncomeMny = itemVOs[i].getNsummny();
          dtalBalCostNum = dZero;
        }
        else if (bCostFlag.booleanValue())
        {
          dtalBalIncomeNum = dZero;
          dtalBalIncomeMny = dZero;
          dtalBalCostNum = itemVOs[i].getNnewbalancenum();
        }

        UFDouble dnewBal = itemVOs[i].getNnewbalancenum() == null ? new UFDouble(0) : itemVOs[i].getNnewbalancenum();

        if (stmtUpdateItem == null) {
          stmtUpdateItem = prepareStatement(con, sUpdateItemSql);
        }
        stmtUpdateItem.setBigDecimal(1, dZero.sub(dnewBal).toBigDecimal());

        if (itemVOs[i].getCorder_bid() == null)
        {
          stmtUpdateItem.setNull(2, 1);
        }
        else
        {
          stmtUpdateItem.setString(2, itemVOs[i].getCorder_bid());
        }
        executeUpdate(stmtUpdateItem);

        if (stmtUpdateDetail == null) {
          stmtUpdateDetail = prepareStatement(con, sqlDetail);
        }
        if (itemVOs[i].getCsquareid() == null)
        {
          stmtUpdateDetail.setNull(1, 1);
        }
        else
        {
          stmtUpdateDetail.setString(1, itemVOs[i].getCsquareid());
        }
        executeUpdate(stmtUpdateDetail);
      }

      String sSqlState = new String();

      sSqlState = "UPDATE so_square SET fstatus=2 WHERE csaleid =? and not EXISTS (select csaleid from so_square_b where so_square_b.csaleid=? and (nbalancenum!=0 and nbalancenum is not null)) ";
      stmt = con.prepareStatement(sSqlState);

      if (((SquareHeaderVO)vo.getParentVO()).getCsaleid() == null)
      {
        stmt.setNull(1, 1);
      }
      else
      {
        stmt.setString(1, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());

        stmt.setString(2, ((SquareHeaderVO)vo.getParentVO()).getCsaleid());
      }
      stmt.executeUpdate();
      stmt.close();

      if (stmtUpdateExecute != null)
        executeBatch(stmtUpdateExecute);
      if (stmtUpdateItem != null)
        executeBatch(stmtUpdateItem);
      if (stmtUpdateDetail != null) {
        executeBatch(stmtUpdateDetail);
      }

      String sSqlTs = "select ts from so_square_b where corder_bid=? ";
      stmt = con.prepareStatement(sSqlTs);

      for (int i = 0; i < itemVOs.length; i++)
      {
        if (itemVOs[i].getCorder_bid() == null)
        {
          stmt.setNull(1, 1);
        }
        else
        {
          stmt.setString(1, itemVOs[i].getCorder_bid());
        }

        ResultSet rst = stmt.executeQuery();
        if (rst.next()) {
          String sts = rst.getString(1);
          vTs.addElement(sts);
        }
      }
      stmt.close();
      String sVerifyrule = ((SquareHeaderVO)vo.getParentVO()).getVerifyrule();
      if ((sVerifyrule != null) && (itemVOs != null) && (itemVOs.length > 0) && (
        (sVerifyrule.equals("W")) || (sVerifyrule.equals("F"))))
      {
        IncomeAdjust checkdmo = new IncomeAdjust();
        HashMap map = new HashMap(itemVOs.length);
        for (int i = 0; i < itemVOs.length; i++) {
          map.put(itemVOs[i].getCupbillbodyid(), itemVOs[i].getCupbillbodyid());
        }
        String[] ids = new String[map.size()];
        map.keySet().toArray(ids);
        checkdmo.checkWFCostNum(ids);
      }
    }
    finally
    {
      try
      {
        if (stmt != null)
        {
          stmt.close();
        }
        if (stmtUpdateExecute != null)
          stmtUpdateExecute.close();
        if (stmtUpdateItem != null)
          stmtUpdateItem.close();
        if (stmtUpdateDetail != null)
          stmtUpdateDetail.close();
      }
      catch (Exception e)
      {
        return vTs;
      }
      try
      {
        if (con != null)
        {
          con.close();
        }
      }
      catch (Exception e)
      {
        return vTs;
      }
    }
    return vTs;
  }

  public void UpdateSquareYSCB(SquareVO vo) throws SQLException, NamingException, SystemException {
    Connection con = null;
    PreparedStatement stmt = null;

    if (vo.getParentVO().getAttributeValue("carhid") != null) {
      String temp = vo.getParentVO().getAttributeValue("carhid").toString();

      con = getConnection();
      String Sql = "Update so_squaredetail set carhid =?,carbid=?,ciahid=?,ciabid=? Where corder_bid=? ";
      stmt = con.prepareStatement(Sql);

      SquareItemVO[] sQuarebVO = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
      for (int i = 0; i < sQuarebVO.length; i++) {
        SquareItemVO itemVO = sQuarebVO[i];
        if (temp == null)
          stmt.setNull(1, 1);
        else {
          stmt.setString(1, temp);
        }
        if (itemVO.getCarbid() == null)
          stmt.setNull(2, 1);
        else {
          stmt.setString(2, itemVO.getCarbid().toString());
        }
        if (itemVO.getCiahid() == null)
          stmt.setNull(3, 1);
        else {
          stmt.setString(3, itemVO.getCiahid().toString());
        }
        if (itemVO.getCiabid() == null)
          stmt.setNull(4, 1);
        else {
          stmt.setString(4, itemVO.getCiabid().toString());
        }
        stmt.setString(5, itemVO.getCorder_bid());

        executeUpdate(stmt);
      }
    }
  }
}