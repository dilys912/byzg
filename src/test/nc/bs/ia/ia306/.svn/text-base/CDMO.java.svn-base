package nc.bs.ia.ia306;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.impl.ia.pub.CommonDataImpl;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.vo.ia.ia306.GLVO;
import nc.vo.ia.ia306.ParamVO308;
import nc.vo.ia.pub.Log;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class CDMO extends DataManageObject
{
  public CDMO()
    throws NamingException, SystemException
  {
  }

  public CDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public ParamVO308 getSumIOL(ParamVO308 p)
    throws SQLException, BusinessException
  {
    beforeCallMethod("nc.bs.ia.ia306.CDMO", "getSumIOL", new Object[] { p });

    CommonDataImpl cbo = new CommonDataImpl();
    String period = p.getPeriod();
    String perPeriod = cbo.getPerviousPeriod(p.getPkCorp(), p.getPeriod());
    String year = period.substring(0, period.indexOf(45));
    String month = period.substring(period.indexOf(45) + 1);

    StringBuffer sb = new StringBuffer();

    sb.append(" select t.rd, t.inv, t.vbatch, ");
    sb.append(" sum(t.ninnum) ninnum, sum(t.ninmny) ninmny, sum(t.noutnum) noutnum, sum(t.noutmny) noutmny, sum(t.nabnum) nabnum, sum(t.nabmny) nabmny,");

    sb.append(" sum(t.ninvarymny) ninvarymny, sum(t.noutvarymny) noutvarymny, sum(t.nabvarymny) nabvarymny ");

    sb.append(" from(");
    if (perPeriod.substring(5).equalsIgnoreCase("00"))
    {
      sb.append(" (select crdcenterid rd , cinventoryid inv , ");
      sb.append(" case when vbatch is null then 'NULL' else vbatch end vbatch, ");

      sb.append(" 0 as ninnum, 0 as ninmny, 0 as noutnum, 0 as noutmny, nabnum, nabmny, 0 as ninvarymny, 0 as noutvarymny, nabvarymny ");

      sb.append(" from ia_generalledger where pk_corp='" + p.getPkCorp() + "' ");

      sb.append(" and caccountyear='" + perPeriod.substring(0, 4) + "' and caccountmonth='00' and frecordtypeflag=1) ");
    }
    else
    {
      sb.append(" (select crdcenterid rd , cinventoryid inv , ");
      sb.append(" case when vbatch is null then 'NULL' else vbatch end vbatch, ");

      sb.append(" 0 as ninnum, 0 as ninmny, 0 as noutnum, 0 as noutmny, nabnum, nabmny, 0 as ninvarymny, 0 as noutvarymny, nabvarymny ");

      sb.append(" from ia_monthledger where pk_corp='" + p.getPkCorp() + "' ");
      sb.append(" and caccountyear='" + perPeriod.substring(0, 4) + "' and caccountmonth='" + perPeriod.substring(5) + "' and frecordtypeflag=3) ");
    }

    sb.append(" union all (select crdcenterid rd , cinventoryid inv , ");
    sb.append(" case when vbatch is null then 'NULL' else vbatch end vbatch, ");
    sb.append(" sum(ninnum) as ninnum, sum(ninmny) as ninmny, sum(noutnum) as noutnum, sum(noutmny) as noutmny, ");

    sb.append(" 0 as nabnum, 0 as nabmny, ");
    sb.append(" sum(ninvarymny) as ninvarymny, sum(noutvarymny) as noutvarymny, 0 as nabvarymny ");

    sb.append(" from ia_monthledger where pk_corp='" + p.getPkCorp() + "' ");
    sb.append(" and caccountyear||'-'||caccountmonth<='" + perPeriod + "' ");
    sb.append(" and frecordtypeflag=2 ");
    sb.append(" group by crdcenterid, cinventoryid, case when vbatch is null then 'NULL' else vbatch end)");

    sb.append(" union all (select crdcenterid rd , cinventoryid inv , ");
    sb.append(" case when bauditbatchflag = 'Y' then vbatch else 'NULL' end vbatch,");

    sb.append("sum(case when fdispatchflag = 0 and cbilltypecode >= 'I2' then coalesce(nnumber, 0) else 0 end) ninnum,");

    sb.append("sum(case when fdispatchflag = 0 and cbilltypecode >= 'I2' then coalesce(nmoney, 0) else 0 end) ninmny, ");

    sb.append("sum(case when fdispatchflag = 1 then coalesce(nnumber, 0) else 0 end) noutnum,");

    sb.append("sum(case when fdispatchflag = 1 then coalesce(nmoney, 0) else 0 end) noutmny, ");

    sb.append("sum(case when fdispatchflag = 0 then coalesce(nnumber, 0) else - coalesce(nnumber, 0) end) nabnum,");

    sb.append("sum(case when fdispatchflag = 0 then coalesce(nmoney, 0) else - coalesce(nmoney, 0) end) nabmny, ");

    sb.append("sum(case when fdispatchflag = 0 and cbilltypecode >= 'I2' then coalesce(ninvarymny, 0) else 0 end) ninvarymny, ");

    sb.append("sum(case when fdispatchflag = 1 then coalesce(noutvarymny, 0) else 0 end) noutvarymny,");

    sb.append("sum(case when fdispatchflag = 0 then coalesce(ninvarymny, 0) else - coalesce(noutvarymny, 0) end) nabvarymny ");

    sb.append(" from v_ia_inoutledger ");
    sb.append(" where ");
    sb.append(" pk_corp = " + toS(p.getPkCorp()));
    sb.append(" and ( caccountyear='" + year + "' and caccountmonth='" + month + "' )  ");

    sb.append(" and cauditorid is not null and cbilltypecode != 'I1' and cbilltypecode != 'IE' and cbilltypecode != 'IH' and cbilltypecode != 'IG' and cbilltypecode != 'IF' ");

    sb.append(" and(cbiztypeid is null or csourcebilltypecode is null  or csourcebilltypecode != '32' ");

    sb.append(" or cbiztypeid not in (select pk_busitype from bd_busitype where verifyrule in('W', 'F')");

    sb.append(" and (pk_corp = " + toS(p.getPkCorp()) + " ))) ");
    sb.append(" group by crdcenterid, cinventoryid, case when bauditbatchflag = 'Y' then vbatch else 'NULL' end)) t ");

    sb.append(" group by t.rd, t.inv, t.vbatch ");
    ParamVO308[] voa = null;
    Vector v = new Vector();
    CrossDBConnection con = null;
    PreparedStatement stmt = null;
    try {
      con = (CrossDBConnection)getConnection();
      Log.info(sb.toString());
      stmt = con.prepareStatement(sb.toString());
      ResultSet rs = stmt.executeQuery();

      String sPkCalbody = null;
      String sPkInv = null;
      String sVBatch = null;
      Object dInNum = null;
      Object dInMny = null;
      Object dOutNum = null;
      Object dOutMny = null;
      Object dNabNum = null;
      Object dNabMny = null;
      Object dInvarymny = null;
      Object dOutvarymny = null;
      Object dAbvarymny = null;
      ParamVO308 vo = null;
      int i = 0;
      while (rs.next()) {
        vo = new ParamVO308();
        i = 1;

        sPkCalbody = rs.getString(i++);
        vo.setPkCalbody((sPkCalbody == null) ? null : sPkCalbody.trim());

        sPkInv = rs.getString(i++);
        vo.setPkInv((sPkInv == null) ? null : sPkInv.trim());

        sVBatch = rs.getString(i++);
        vo.setVBatch((sVBatch == null) ? null : sVBatch.trim());

        dInNum = rs.getObject(i++);
        vo.setInNum(new UFDouble(dInNum.toString()));

        dInMny = rs.getObject(i++);
        vo.setInMny(new UFDouble(dInMny.toString()));

        dOutNum = rs.getObject(i++);
        vo.setOutNum(new UFDouble(dOutNum.toString()));

        dOutMny = rs.getObject(i++);
        vo.setOutMny(new UFDouble(dOutMny.toString()));

        dNabNum = rs.getObject(i++);
        vo.setNabNum(new UFDouble(dNabNum.toString()));

        dNabMny = rs.getObject(i++);
        vo.setNabMny(new UFDouble(dNabMny.toString()));

        dInvarymny = rs.getObject(i++);
        vo.setInvarymny(new UFDouble(dInvarymny.toString()));

        dOutvarymny = rs.getObject(i++);
        vo.setOutvarymny(new UFDouble(dOutvarymny.toString()));

        dAbvarymny = rs.getObject(i++);
        vo.setAbvarymny(new UFDouble(dAbvarymny.toString()));

        v.addElement(vo);
      }
      rs.close();
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
    voa = new ParamVO308[v.size()];
    if (v.size() > 0) {
      v.copyInto(voa);
    }

    afterCallMethod("nc.bs.ia.ia306.CDMO", "getSumIOL", new Object[] { p });

    p.setVOA(voa);
    return p;
  }

  public String insert(String[] sa)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.ia.ia401.FormulaDMO", "insert", new Object[] { sa });

    String sql = "insert into ia_modifygllog(cmodifygllogid,pk_corp,crdcenterid,cinventoryid,vbatch,csql,colddata) values(?, ?, ?, ?, ?, ?, ?)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      key = getOID();
      stmt.setString(1, key);

      if (sa[0] == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, sa[0]);
      }
      if (sa[1] == null) {
        stmt.setNull(3, 1);
      }
      else {
        stmt.setString(3, sa[1]);
      }
      if (sa[2] == null) {
        stmt.setNull(4, 1);
      }
      else {
        stmt.setString(4, sa[2]);
      }
      if (sa[3] == null) {
        stmt.setNull(5, 1);
      }
      else {
        stmt.setString(5, sa[3]);
      }
      if (sa[4] == null) {
        stmt.setNull(6, 1);
      }
      else {
        stmt.setString(6, sa[4]);
      }
      if (sa[5] == null) {
        stmt.setNull(7, 1);
      }
      else {
        stmt.setString(7, sa[5]);
      }

      stmt.executeUpdate();
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

    afterCallMethod("nc.bs.ia.ia401.FormulaDMO", "insert", new Object[] { sa });

    return key;
  }

  private String toS(String param)
  {
    if (param == null) {
      return "''";
    }
    return "'" + param + "' ";
  }

  public GLVO[] findByCalbodyAndInv(String sCalb, String sInv, String sBatch)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.ia504.GeneralledgerDMO", "findHeaderByPrimaryKey", new Object[] { sCalb, sInv, sBatch });

    String sql = "select pk_corp, frecordtypeflag, crdcenterid, cinventoryid, fpricemodeflag, caccountyear, caccountmonth, nmonthprice, ninnum, ninmny, noutnum, noutmny, nabnum, nabprice, nabmny, nvariancerate, ninvarymny, noutvarymny, nabvarymny, nemitprice, nemitdebitnum, nemitcreditnum, nemitabnum, nemitdebitmny, nemitcreditmny, nemitabmny, nentrustprice, nentrustdebitnum, nentrustcreditnum, nentrustabnum, nentrustdebitmny, nentrustcreditmny, nentrustabmny, btryflag, m_nemitinvarymny, m_nemitoutvarymny, m_nemitabvarymny, m_nentrustinvarymny, m_nentrustoutvarymny, m_nentrustabvarymny, m_nplanedprice,vbatch from ia_generalledger where  crdcenterid = ? AND cinventoryid = ? ";
    if ((sBatch != null) && (!(sBatch.equals("NULL")))) {
      sql = sql + " AND vbatch = '" + sBatch + "'";
    }
    sql = "select cgeneralledgerid, pk_corp, frecordtypeflag, crdcenterid, cinventoryid, fpricemodeflag, caccountyear, caccountmonth, nmonthprice, ninnum, ninmny, noutnum, noutmny, nabnum, nabprice, nabmny, nvariancerate, ninvarymny, noutvarymny, nabvarymny, nemitprice, nemitdebitnum, nemitcreditnum, nemitabnum, nemitdebitmny, nemitcreditmny, nemitabmny, nentrustprice, nentrustdebitnum, nentrustcreditnum, nentrustabnum, nentrustdebitmny, nentrustcreditmny, nentrustabmny, btryflag, nemitinvarymny, nemitoutvarymny, nemitabvarymny, nentrustinvarymny, nentrustoutvarymny, nentrustabvarymny, nplanedprice,vbatch from ia_generalledger where crdcenterid = ? AND cinventoryid = ? ";

    if ((sBatch != null) && (!(sBatch.equals("NULL")))) {
      sql = sql + " AND vbatch = " + toS(sBatch);
    }

    GLVO[] generalledgers = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, sCalb);
      stmt.setString(2, sInv);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        GLVO generalledger = new GLVO();

        String cgeneralledgerid = rs.getString(1);
        generalledger.setCgeneralledgerid((cgeneralledgerid == null) ? null : cgeneralledgerid.trim());

        String pk_corp = rs.getString(2);
        generalledger.setPk_corp((pk_corp == null) ? null : pk_corp.trim());

        Integer frecordtypeflag = (Integer)rs.getObject(3);
        generalledger.setFrecordtypeflag((frecordtypeflag == null) ? null : frecordtypeflag);

        String crdcenterid = rs.getString(4);
        generalledger.setCrdcenterid((crdcenterid == null) ? null : crdcenterid.trim());

        String cinventoryid = rs.getString(5);
        generalledger.setCinventoryid((cinventoryid == null) ? null : cinventoryid.trim());

        Integer fpricemodeflag = (Integer)rs.getObject(6);
        generalledger.setFpricemodeflag((fpricemodeflag == null) ? null : fpricemodeflag);

        String caccountyear = rs.getString(7);
        generalledger.setCaccountyear((caccountyear == null) ? null : caccountyear.trim());

        String caccountmonth = rs.getString(8);
        generalledger.setCaccountmonth((caccountmonth == null) ? null : caccountmonth.trim());

        BigDecimal nmonthprice = (BigDecimal)rs.getObject(9);
        generalledger.setNmonthprice(new UFDouble(nmonthprice));

        BigDecimal ninnum = (BigDecimal)rs.getObject(10);
        generalledger.setNinnum(new UFDouble(ninnum));

        BigDecimal ninmny = (BigDecimal)rs.getObject(11);
        generalledger.setNinmny(new UFDouble(ninmny));

        BigDecimal noutnum = (BigDecimal)rs.getObject(12);
        generalledger.setNoutnum(new UFDouble(noutnum));

        BigDecimal noutmny = (BigDecimal)rs.getObject(13);
        generalledger.setNoutmny(new UFDouble(noutmny));

        BigDecimal nabnum = (BigDecimal)rs.getObject(14);
        generalledger.setNabnum(new UFDouble(nabnum));

        BigDecimal nabprice = (BigDecimal)rs.getObject(15);
        generalledger.setNabprice(new UFDouble(nabprice));

        BigDecimal nabmny = (BigDecimal)rs.getObject(16);
        generalledger.setNabmny(new UFDouble(nabmny));

        BigDecimal nvariancerate = (BigDecimal)rs.getObject(17);
        generalledger.setNvariancerate((nvariancerate == null) ? null : nvariancerate);

        BigDecimal ninvarymny = (BigDecimal)rs.getObject(18);
        generalledger.setNinvarymny(new UFDouble(ninvarymny));

        BigDecimal noutvarymny = (BigDecimal)rs.getObject(19);
        generalledger.setNoutvarymny(new UFDouble(noutvarymny));

        BigDecimal nabvarymny = (BigDecimal)rs.getObject(20);
        generalledger.setNabvarymny(new UFDouble(nabvarymny));

        BigDecimal nemitprice = (BigDecimal)rs.getObject(21);
        generalledger.setNemitprice(new UFDouble(nemitprice));

        BigDecimal nemitdebitnum = (BigDecimal)rs.getObject(22);
        generalledger.setNemitdebitnum(new UFDouble(nemitdebitnum));

        BigDecimal nemitcreditnum = (BigDecimal)rs.getObject(23);
        generalledger.setNemitcreditnum(new UFDouble(nemitcreditnum));

        BigDecimal nemitabnum = (BigDecimal)rs.getObject(24);
        generalledger.setNemitabnum(new UFDouble(nemitabnum));

        BigDecimal nemitdebitmny = (BigDecimal)rs.getObject(25);
        generalledger.setNemitdebitmny(new UFDouble(nemitdebitmny));

        BigDecimal nemitcreditmny = (BigDecimal)rs.getObject(26);
        generalledger.setNemitcreditmny(new UFDouble(nemitcreditmny));

        BigDecimal nemitabmny = (BigDecimal)rs.getObject(27);
        generalledger.setNemitabmny(new UFDouble(nemitabmny));

        BigDecimal nentrustprice = (BigDecimal)rs.getObject(28);
        generalledger.setNentrustprice(new UFDouble(nentrustprice));

        BigDecimal nentrustdebitnum = (BigDecimal)rs.getObject(29);
        generalledger.setNentrustdebitnum(new UFDouble(nentrustdebitnum));

        BigDecimal nentrustcreditnum = (BigDecimal)rs.getObject(30);
        generalledger.setNentrustcreditnum(new UFDouble(nentrustcreditnum));

        BigDecimal nentrustabnum = (BigDecimal)rs.getObject(31);
        generalledger.setNentrustabnum(new UFDouble(nentrustabnum));

        BigDecimal nentrustdebitmny = (BigDecimal)rs.getObject(32);
        generalledger.setNentrustdebitmny(new UFDouble(nentrustdebitmny));

        BigDecimal nentrustcreditmny = (BigDecimal)rs.getObject(33);
        generalledger.setNentrustcreditmny(new UFDouble(nentrustcreditmny));

        BigDecimal nentrustabmny = (BigDecimal)rs.getObject(34);
        generalledger.setNentrustabmny(new UFDouble(nentrustabmny));

        UFBoolean btryflag = new UFBoolean(rs.getString(35));
        generalledger.setBtryflag((btryflag == null) ? null : btryflag);

        BigDecimal nemitinvarymny = (BigDecimal)rs.getObject(36);
        generalledger.setNemitinvarymny(new UFDouble(nemitinvarymny));

        BigDecimal nemitoutvarymny = (BigDecimal)rs.getObject(37);
        generalledger.setNemitoutvarymny(new UFDouble(nemitoutvarymny));

        BigDecimal nemitabvarymny = (BigDecimal)rs.getObject(38);
        generalledger.setNemitabvarymny(new UFDouble(nemitabvarymny));

        BigDecimal nentrustinvarymny = (BigDecimal)rs.getObject(39);
        generalledger.setNentrustinvarymny(new UFDouble(nentrustinvarymny));

        BigDecimal nentrustoutvarymny = (BigDecimal)rs.getObject(40);
        generalledger.setNentrustoutvarymny(new UFDouble(nentrustoutvarymny));

        BigDecimal nentrustabvarymny = (BigDecimal)rs.getObject(41);
        generalledger.setNentrustabvarymny(new UFDouble(nentrustabvarymny));

        BigDecimal nplanedprice = (BigDecimal)rs.getObject(42);
        generalledger.setNplanedprice(new UFDouble(nplanedprice));

        String vbatch = rs.getString(43);
        generalledger.setVbatch((vbatch == null) ? null : vbatch.trim());
        v.addElement(generalledger);
      }
      rs.close();
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
    generalledgers = new GLVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(generalledgers);
    }

    afterCallMethod("nc.bs.ia.ia504.GeneralledgerDMO", "findHeaderByPrimaryKey", new Object[] { sCalb, sInv, sBatch });

    return generalledgers;
  }

  public String insertHeader(GLVO generalledgerHeader)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.ia.ia504.GeneralledgerDMO", "insertHeader", new Object[] { generalledgerHeader });

    String sql = "insert into ia_generalledger(cgeneralledgerid, pk_corp, frecordtypeflag, crdcenterid, cinventoryid, fpricemodeflag, caccountyear, caccountmonth, nmonthprice, ninnum, ninmny, noutnum, noutmny, nabnum, nabprice, nabmny, nvariancerate, ninvarymny, noutvarymny, nabvarymny, nemitprice, nemitdebitnum, nemitcreditnum, nemitabnum, nemitdebitmny, nemitcreditmny, nemitabmny, nentrustprice, nentrustdebitnum, nentrustcreditnum, nentrustabnum, nentrustdebitmny, nentrustcreditmny, nentrustabmny, btryflag, nemitinvarymny, nemitoutvarymny, nemitabvarymny, nentrustinvarymny, nentrustoutvarymny, nentrustabvarymny, nplanedprice,vbatch) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      key = getOID();
      stmt.setString(1, key);

      if (generalledgerHeader.getPk_corp() == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, generalledgerHeader.getPk_corp());
      }
      if (generalledgerHeader.getFrecordtypeflag() == null) {
        stmt.setNull(3, 4);
      }
      else {
        stmt.setInt(3, generalledgerHeader.getFrecordtypeflag().intValue());
      }
      if (generalledgerHeader.getCrdcenterid() == null) {
        stmt.setNull(4, 1);
      }
      else {
        stmt.setString(4, generalledgerHeader.getCrdcenterid());
      }
      if (generalledgerHeader.getCinventoryid() == null) {
        stmt.setNull(5, 1);
      }
      else {
        stmt.setString(5, generalledgerHeader.getCinventoryid());
      }
      if (generalledgerHeader.getFpricemodeflag() == null) {
        stmt.setNull(6, 4);
      }
      else {
        stmt.setInt(6, generalledgerHeader.getFpricemodeflag().intValue());
      }
      if (generalledgerHeader.getCaccountyear() == null) {
        stmt.setNull(7, 1);
      }
      else {
        stmt.setString(7, generalledgerHeader.getCaccountyear());
      }
      if (generalledgerHeader.getCaccountmonth() == null) {
        stmt.setNull(8, 1);
      }
      else {
        stmt.setString(8, generalledgerHeader.getCaccountmonth());
      }
      if (generalledgerHeader.getNmonthprice() == null) {
        stmt.setNull(9, 4);
      }
      else {
        stmt.setBigDecimal(9, generalledgerHeader.getNmonthprice().toBigDecimal());
      }

      if (generalledgerHeader.getNinnum() == null) {
        stmt.setNull(10, 4);
      }
      else {
        stmt.setBigDecimal(10, generalledgerHeader.getNinnum().toBigDecimal());
      }
      if (generalledgerHeader.getNinmny() == null) {
        stmt.setNull(11, 4);
      }
      else {
        stmt.setBigDecimal(11, generalledgerHeader.getNinmny().toBigDecimal());
      }
      if (generalledgerHeader.getNoutnum() == null) {
        stmt.setNull(12, 4);
      }
      else {
        stmt.setBigDecimal(12, generalledgerHeader.getNoutnum().toBigDecimal());
      }
      if (generalledgerHeader.getNoutmny() == null) {
        stmt.setNull(13, 4);
      }
      else {
        stmt.setBigDecimal(13, generalledgerHeader.getNoutmny().toBigDecimal());
      }
      if (generalledgerHeader.getNabnum() == null) {
        stmt.setNull(14, 4);
      }
      else {
        stmt.setBigDecimal(14, generalledgerHeader.getNabnum().toBigDecimal());
      }
      if (generalledgerHeader.getNabprice() == null) {
        stmt.setNull(15, 4);
      }
      else {
        stmt.setBigDecimal(15, generalledgerHeader.getNabprice().toBigDecimal());
      }

      if (generalledgerHeader.getNabmny() == null) {
        stmt.setNull(16, 4);
      }
      else {
        stmt.setBigDecimal(16, generalledgerHeader.getNabmny().toBigDecimal());
      }
      if (generalledgerHeader.getNvariancerate() == null) {
        stmt.setNull(17, 4);
      }
      else {
        stmt.setBigDecimal(17, generalledgerHeader.getNvariancerate());
      }

      if (generalledgerHeader.getNinvarymny() == null) {
        stmt.setNull(18, 4);
      }
      else {
        stmt.setBigDecimal(18, generalledgerHeader.getNinvarymny().toBigDecimal());
      }

      if (generalledgerHeader.getNoutvarymny() == null) {
        stmt.setNull(19, 4);
      }
      else {
        stmt.setBigDecimal(19, generalledgerHeader.getNoutvarymny().toBigDecimal());
      }

      if (generalledgerHeader.getNabvarymny() == null) {
        stmt.setNull(20, 4);
      }
      else {
        stmt.setBigDecimal(20, generalledgerHeader.getNabvarymny().toBigDecimal());
      }

      if (generalledgerHeader.getNemitprice() == null) {
        stmt.setNull(21, 4);
      }
      else {
        stmt.setBigDecimal(21, generalledgerHeader.getNemitprice().toBigDecimal());
      }

      if (generalledgerHeader.getNemitdebitnum() == null) {
        stmt.setNull(22, 4);
      }
      else {
        stmt.setBigDecimal(22, generalledgerHeader.getNemitdebitnum().toBigDecimal());
      }

      if (generalledgerHeader.getNemitcreditnum() == null) {
        stmt.setNull(23, 4);
      }
      else {
        stmt.setBigDecimal(23, generalledgerHeader.getNemitcreditnum().toBigDecimal());
      }

      if (generalledgerHeader.getNemitabnum() == null) {
        stmt.setNull(24, 4);
      }
      else {
        stmt.setBigDecimal(24, generalledgerHeader.getNemitabnum().toBigDecimal());
      }

      if (generalledgerHeader.getNemitdebitmny() == null) {
        stmt.setNull(25, 4);
      }
      else {
        stmt.setBigDecimal(25, generalledgerHeader.getNemitdebitmny().toBigDecimal());
      }

      if (generalledgerHeader.getNemitcreditmny() == null) {
        stmt.setNull(26, 4);
      }
      else {
        stmt.setBigDecimal(26, generalledgerHeader.getNemitcreditmny().toBigDecimal());
      }

      if (generalledgerHeader.getNemitabmny() == null) {
        stmt.setNull(27, 4);
      }
      else {
        stmt.setBigDecimal(27, generalledgerHeader.getNemitabmny().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustprice() == null) {
        stmt.setNull(28, 4);
      }
      else {
        stmt.setBigDecimal(28, generalledgerHeader.getNentrustprice().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustdebitnum() == null) {
        stmt.setNull(29, 4);
      }
      else {
        stmt.setBigDecimal(29, generalledgerHeader.getNentrustdebitnum().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustcreditnum() == null) {
        stmt.setNull(30, 4);
      }
      else {
        stmt.setBigDecimal(30, generalledgerHeader.getNentrustcreditnum().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustabnum() == null) {
        stmt.setNull(31, 4);
      }
      else {
        stmt.setBigDecimal(31, generalledgerHeader.getNentrustabnum().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustdebitmny() == null) {
        stmt.setNull(32, 4);
      }
      else {
        stmt.setBigDecimal(32, generalledgerHeader.getNentrustdebitmny().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustcreditmny() == null) {
        stmt.setNull(33, 4);
      }
      else {
        stmt.setBigDecimal(33, generalledgerHeader.getNentrustcreditmny().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustabmny() == null) {
        stmt.setNull(34, 4);
      }
      else {
        stmt.setBigDecimal(34, generalledgerHeader.getNentrustabmny().toBigDecimal());
      }

      if (generalledgerHeader.getBtryflag() == null) {
        stmt.setNull(35, 1);
      }
      else {
        stmt.setString(35, generalledgerHeader.getBtryflag().toString());
      }
      if (generalledgerHeader.getNemitinvarymny() == null) {
        stmt.setNull(36, 4);
      }
      else {
        stmt.setBigDecimal(36, generalledgerHeader.getNemitinvarymny().toBigDecimal());
      }

      if (generalledgerHeader.getNemitoutvarymny() == null) {
        stmt.setNull(37, 4);
      }
      else {
        stmt.setBigDecimal(37, generalledgerHeader.getNemitoutvarymny().toBigDecimal());
      }

      if (generalledgerHeader.getNemitabvarymny() == null) {
        stmt.setNull(38, 4);
      }
      else {
        stmt.setBigDecimal(38, generalledgerHeader.getNemitabvarymny().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustinvarymny() == null) {
        stmt.setNull(39, 4);
      }
      else {
        stmt.setBigDecimal(39, generalledgerHeader.getNentrustinvarymny().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustoutvarymny() == null) {
        stmt.setNull(40, 4);
      }
      else {
        stmt.setBigDecimal(40, generalledgerHeader.getNentrustoutvarymny().toBigDecimal());
      }

      if (generalledgerHeader.getNentrustabvarymny() == null) {
        stmt.setNull(41, 4);
      }
      else {
        stmt.setBigDecimal(41, generalledgerHeader.getNentrustabvarymny().toBigDecimal());
      }

      if (generalledgerHeader.getNplanedprice() == null) {
        stmt.setNull(42, 4);
      }
      else {
        stmt.setBigDecimal(42, generalledgerHeader.getNplanedprice().toBigDecimal());
      }

      if (generalledgerHeader.getVbatch() == null) {
        stmt.setNull(43, 1);
      }
      else {
        stmt.setString(43, generalledgerHeader.getVbatch());
      }

      stmt.executeUpdate();
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

    afterCallMethod("nc.bs.ia.ia504.GeneralledgerDMO", "insertHeader", new Object[] { generalledgerHeader });

    return key;
  }

  public GLVO[] queryAll(String unitCode)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ia.ia306.CDMO", "queryAll", new Object[] { unitCode });

    String sql = "";
    if (unitCode != null) {
      sql = "select cgeneralledgerid, pk_corp, frecordtypeflag, crdcenterid, cinventoryid, fpricemodeflag, caccountyear, caccountmonth, nmonthprice, ninnum, ninmny, noutnum, noutmny, nabnum, nabprice, nabmny, nvariancerate, ninvarymny, noutvarymny, nabvarymny, nemitprice, nemitdebitnum, nemitcreditnum, nemitabnum, nemitdebitmny, nemitcreditmny, nemitabmny, nentrustprice, nentrustdebitnum, nentrustcreditnum, nentrustabnum, nentrustdebitmny, nentrustcreditmny, nentrustabmny, btryflag, nemitinvarymny, nemitoutvarymny, nemitabvarymny, nentrustinvarymny, nentrustoutvarymny, nentrustabvarymny, nplanedprice,vbatch from ia_generalledger where pk_corp = ? AND frecordtypeflag = 4 ";
    }

    GLVO[] generalledgers = null;
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
        GLVO generalledger = new GLVO();

        String cgeneralledgerid = rs.getString(1);
        generalledger.setCgeneralledgerid(cgeneralledgerid != null ? cgeneralledgerid : null);
        String pk_corp = rs.getString(2);
        generalledger.setPk_corp(pk_corp != null ? pk_corp.trim() : null);
        Integer frecordtypeflag = (Integer)rs.getObject(3);
        generalledger.setFrecordtypeflag(frecordtypeflag != null ? frecordtypeflag : null);
        String crdcenterid = rs.getString(4);
        generalledger.setCrdcenterid(crdcenterid != null ? crdcenterid.trim() : null);
        String cinventoryid = rs.getString(5);
        generalledger.setCinventoryid(cinventoryid != null ? cinventoryid.trim() : null);
        Integer fpricemodeflag = (Integer)rs.getObject(6);
        generalledger.setFpricemodeflag(fpricemodeflag != null ? fpricemodeflag : null);
        String caccountyear = rs.getString(7);
        generalledger.setCaccountyear(caccountyear != null ? caccountyear.trim() : null);
        String caccountmonth = rs.getString(8);
        generalledger.setCaccountmonth(caccountmonth != null ? caccountmonth.trim() : null);
        BigDecimal nmonthprice = (BigDecimal)rs.getObject(9);
        generalledger.setNmonthprice(nmonthprice != null ? new UFDouble(nmonthprice) : null);
        BigDecimal ninnum = (BigDecimal)rs.getObject(10);
        generalledger.setNinnum(ninnum != null ? new UFDouble(ninnum) : null);
        BigDecimal ninmny = (BigDecimal)rs.getObject(11);
        generalledger.setNinmny(ninmny != null ? new UFDouble(ninmny) : null);
        BigDecimal noutnum = (BigDecimal)rs.getObject(12);
        generalledger.setNoutnum(noutnum != null ? new UFDouble(noutnum) : null);
        BigDecimal noutmny = (BigDecimal)rs.getObject(13);
        generalledger.setNoutmny(noutmny != null ? new UFDouble(noutmny) : null);
        BigDecimal nabnum = (BigDecimal)rs.getObject(14);
        generalledger.setNabnum(nabnum != null ? new UFDouble(nabnum) : null);
        BigDecimal nabprice = (BigDecimal)rs.getObject(15);
        generalledger.setNabprice(nabprice != null ? new UFDouble(nabprice) : null);
        BigDecimal nabmny = (BigDecimal)rs.getObject(16);
        generalledger.setNabmny(nabmny != null ? new UFDouble(nabmny) : null);
        BigDecimal nvariancerate = (BigDecimal)rs.getObject(17);
        generalledger.setNvariancerate(nvariancerate != null ? nvariancerate : null);
        BigDecimal ninvarymny = (BigDecimal)rs.getObject(18);
        generalledger.setNinvarymny(ninvarymny != null ? new UFDouble(ninvarymny) : null);
        BigDecimal noutvarymny = (BigDecimal)rs.getObject(19);
        generalledger.setNoutvarymny(noutvarymny != null ? new UFDouble(noutvarymny) : null);
        BigDecimal nabvarymny = (BigDecimal)rs.getObject(20);
        generalledger.setNabvarymny(nabvarymny != null ? new UFDouble(nabvarymny) : null);
        BigDecimal nemitprice = (BigDecimal)rs.getObject(21);
        generalledger.setNemitprice(nemitprice != null ? new UFDouble(nemitprice) : null);
        BigDecimal nemitdebitnum = (BigDecimal)rs.getObject(22);
        generalledger.setNemitdebitnum(nemitdebitnum != null ? new UFDouble(nemitdebitnum) : null);
        BigDecimal nemitcreditnum = (BigDecimal)rs.getObject(23);
        generalledger.setNemitcreditnum(nemitcreditnum != null ? new UFDouble(nemitcreditnum) : null);
        BigDecimal nemitabnum = (BigDecimal)rs.getObject(24);
        generalledger.setNemitabnum(nemitabnum != null ? new UFDouble(nemitabnum) : null);
        BigDecimal nemitdebitmny = (BigDecimal)rs.getObject(25);
        generalledger.setNemitdebitmny(nemitdebitmny != null ? new UFDouble(nemitdebitmny) : null);
        BigDecimal nemitcreditmny = (BigDecimal)rs.getObject(26);
        generalledger.setNemitcreditmny(nemitcreditmny != null ? new UFDouble(nemitcreditmny) : null);
        BigDecimal nemitabmny = (BigDecimal)rs.getObject(27);
        generalledger.setNemitabmny(nemitabmny != null ? new UFDouble(nemitabmny) : null);
        BigDecimal nentrustprice = (BigDecimal)rs.getObject(28);
        generalledger.setNentrustprice(nentrustprice != null ? new UFDouble(nentrustprice) : null);
        BigDecimal nentrustdebitnum = (BigDecimal)rs.getObject(29);
        generalledger.setNentrustdebitnum(nentrustdebitnum != null ? new UFDouble(nentrustdebitnum) : null);
        BigDecimal nentrustcreditnum = (BigDecimal)rs.getObject(30);
        generalledger.setNentrustcreditnum(nentrustcreditnum != null ? new UFDouble(nentrustcreditnum) : null);
        BigDecimal nentrustabnum = (BigDecimal)rs.getObject(31);
        generalledger.setNentrustabnum(nentrustabnum != null ? new UFDouble(nentrustabnum) : null);
        BigDecimal nentrustdebitmny = (BigDecimal)rs.getObject(32);
        generalledger.setNentrustdebitmny(nentrustdebitmny != null ? new UFDouble(nentrustdebitmny) : null);
        BigDecimal nentrustcreditmny = (BigDecimal)rs.getObject(33);
        generalledger.setNentrustcreditmny(nentrustcreditmny != null ? new UFDouble(nentrustcreditmny) : null);
        BigDecimal nentrustabmny = (BigDecimal)rs.getObject(34);
        generalledger.setNentrustabmny(nentrustabmny != null ? new UFDouble(nentrustabmny) : null);
        UFBoolean btryflag = new UFBoolean(rs.getString(35));
        generalledger.setBtryflag(btryflag != null ? btryflag : null);
        BigDecimal nemitinvarymny = (BigDecimal)rs.getObject(36);
        generalledger.setNemitinvarymny(nemitinvarymny != null ? new UFDouble(nemitinvarymny) : null);
        BigDecimal nemitoutvarymny = (BigDecimal)rs.getObject(37);
        generalledger.setNemitoutvarymny(nemitoutvarymny != null ? new UFDouble(nemitoutvarymny) : null);
        BigDecimal nemitabvarymny = (BigDecimal)rs.getObject(38);
        generalledger.setNemitabvarymny(nemitabvarymny != null ? new UFDouble(nemitabvarymny) : null);
        BigDecimal nentrustinvarymny = (BigDecimal)rs.getObject(39);
        generalledger.setNentrustinvarymny(nentrustinvarymny != null ? new UFDouble(nentrustinvarymny) : null);
        BigDecimal nentrustoutvarymny = (BigDecimal)rs.getObject(40);
        generalledger.setNentrustoutvarymny(nentrustoutvarymny != null ? new UFDouble(nentrustoutvarymny) : null);
        BigDecimal nentrustabvarymny = (BigDecimal)rs.getObject(41);
        generalledger.setNentrustabvarymny(nentrustabvarymny != null ? new UFDouble(nentrustabvarymny) : null);
        BigDecimal nplanedprice = (BigDecimal)rs.getObject(42);
        generalledger.setNplanedprice(nplanedprice != null ? new UFDouble(nplanedprice) : null);
        String vbatch = rs.getString(43);
        generalledger.setVbatch(vbatch != null ? vbatch.trim() : null);
        v.addElement(generalledger);
      }
      rs.close();
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
    generalledgers = new GLVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(generalledgers);
    }

    afterCallMethod("nc.bs.ia.ia306.CDMO", "queryAll", new Object[] { unitCode });

    return generalledgers;
  }
}