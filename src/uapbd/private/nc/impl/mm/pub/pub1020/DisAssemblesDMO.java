package nc.impl.mm.pub.pub1020;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.mm.pub.pub1020.DisConditionVO;
import nc.vo.mm.pub.pub1020.DisassembleVO;
import nc.vo.mm.pub.pub1020.ProduceCoreVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class DisAssemblesDMO extends DataManageObject
{
  public DisAssemblesDMO()
    throws NamingException, SystemException
  {
  }

  public DisAssemblesDMO(String arg1)
    throws NamingException, SystemException
  {
    super(arg1);
  }

  public DisassembleVO[] getChildVOs(DisConditionVO voc)
    throws SQLException, BusinessException
  {
    Vector resv = new Vector();
    
    //add by shikun 
    String[] tmpres = new String[4];
    String pk_corp = voc.getPk_corp()==null?"":voc.getPk_corp();
    String bomver = voc.getId()==null?"":voc.getId();//°æ±¾ºÅ
    if ("1078".equals(pk_corp)&&bomver.length()<19) {
        tmpres = getAvaVersion2(voc.getPk_corp(), voc.getGcbm(), voc.getWlbmid(), voc.getZzfsid(), "A",bomver);
	}else{
    tmpres = getAvaVersion(voc.getPk_corp(), voc.getGcbm(), voc.getWlbmid(), voc.getZzfsid(), "A");
	}
    //end 

    String pk_bomid = tmpres[0];
    if (pk_bomid == null) {
      return new DisassembleVO[0];
    }
    Integer cm = new Integer(voc.getCm().intValue() + 1);
    DisassembleVO dvo = null;
    Connection con = null;
    PreparedStatement stmt = null;
    Object tempob = voc.getYxrq();
    if (tempob == null)
      tempob = voc.getLogdate();
    String yxrq = tempob.toString();
    try {
      con = getConnection();
      String bomsql = "select bomb.pk_bom_bid, bomb.zxbmid,bomb.sl,bomb.jldwid,bomb.shxs,bomb.zzgx, bomb.sftdl, bomb.zxlx, " +
      		"bomb.sdate, bomb.gyfs, bomb.flckid, bomb.edate, bomb.kzbz,bomb.pztqq,bomb.sfwwfl,bom.sl,bomb.pk_produce  " +
      		"from bd_bom bom INNER JOIN bd_bom_b bomb ON bom.pk_bomid = bomb.pk_bomid  " +
      		"where bom.pk_bomid = ? and bomb.sdate<= ? and  bomb.edate>= ? ";

      stmt = con.prepareStatement(bomsql);
      stmt.setString(1, pk_bomid);
      stmt.setString(2, yxrq);
      stmt.setString(3, yxrq);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        dvo = new DisassembleVO();
        dvo.setPk_corp(voc.getPk_corp());
        dvo.setGcbm(voc.getGcbm());

        String pk_bom_bid = rs.getString(1);
        dvo.setPk(pk_bom_bid);

        String zxbmid = rs.getString(2);
        dvo.setWlbmid(zxbmid);

        UFDouble zxsl = new UFDouble(rs.getBigDecimal(3));

        String zxjldwid = rs.getString(4);
        dvo.setJldwid(zxjldwid);

        UFDouble shxs = new UFDouble((BigDecimal)rs.getObject(5));
        dvo.setShxs(shxs);

        String zzgx = rs.getString(6);
        dvo.setZzgx(zzgx == null ? null : zzgx.trim());

        String sftdl = rs.getString(7);
        dvo.setSfth(new UFBoolean(sftdl));

        Integer zxlx = (Integer)rs.getObject(8);
        dvo.setZxlx(zxlx);

        String sdate = rs.getString(9);
        dvo.setSdate(sdate);

        Integer gyfs = (Integer)rs.getObject(10);
        dvo.setGyfs(gyfs);

        String flckid = rs.getString(11);
        dvo.setFlckid(flckid == null ? null : flckid.trim());

        String edate = rs.getString(12);
        dvo.setEdate(edate);

        Integer kzbz = (Integer)rs.getObject(13);
        dvo.setKzbz(kzbz);

        Object tempo = rs.getObject(14);
        UFDouble pztqq;
        if (tempo != null)
          pztqq = new UFDouble((BigDecimal)tempo);
        else
          pztqq = null;
        dvo.setPztqq(pztqq);

        tempo = rs.getObject(15);
        if (tempo == null)
          tempo = new String("N");
        dvo.setSfwwfl(new UFBoolean(tempo.toString()));
        UFDouble sl = new UFDouble(rs.getBigDecimal(16));
        if (gyfs.intValue() == 1) {
          dvo.setSl(zxsl);
        }
        else {
          if (voc.getSfsh().booleanValue()) {
            zxsl = voc.getSl().multiply(zxsl).multiply(new UFDouble(1.0D + shxs.doubleValue()));
          }

          dvo.setSl(zxsl.div(sl));
        }
        String pk_produce = rs.getString(17);
        dvo.setPk_produce(pk_produce);
        dvo.setCm(cm);
        resv.addElement(dvo);
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
      catch (Exception e) {
      }
    }
    DisassembleVO[] vos = new DisassembleVO[resv.size()];
    if (resv.size() > 0) {
      resv.copyInto(vos);
    }
    return vos;
  }

  public String[] getAvaVersion(String pk_corp, String gcbm, String wlbmid, String zzfsid, String dx)
    throws SQLException, BusinessException
  {
    String[] res = new String[4];
    if (res[0] == null) {
      String[] resbom = new String[2];
      String[] resrt = new String[2];
      if (dx.equals("A"))
        resbom = getVerFromBom(pk_corp, gcbm, wlbmid);
      else {
        throw new RuntimeException("Unsupported object id:" + dx);
      }
      res[0] = resbom[0];
      res[1] = resbom[1];
      res[2] = resrt[0];
      res[3] = resrt[1];
    }
    return res;
  }

  private String[] getVerFromBom(String pk_corp, String gcbm, String wlbmid) throws SQLException
  {
    String[] res = new String[2];
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      String bomversql = "select pk_bomid,version from bd_bom where wlbmid=? and pk_corp=? and gcbm=? and bblx=0  and sfmr='Y'";
      stmt = con.prepareStatement(bomversql);
      stmt.setString(1, wlbmid);
      stmt.setString(2, pk_corp);
      stmt.setString(3, gcbm);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        res[0] = rs.getString(1);
        res[1] = rs.getString(2);
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
    return res;
  }

  public ProduceCoreVO[] getBatchFjldwInfos(String[] wlbmids)
    throws SQLException
  {
    ProduceCoreVO[] pVOs = getBatchFjldwInfosFromDB(wlbmids);
    if ((pVOs == null) || (pVOs.length == 0)) {
      return null;
    }

    Hashtable h = new Hashtable();
    for (int i = 0; i < pVOs.length; i++) {
      if (!h.containsKey(pVOs[i].getPk_invbasdoc())) {
        h.put(pVOs[i].getPk_invbasdoc(), pVOs[i]);
      }
    }
    ProduceCoreVO[] retvos = new ProduceCoreVO[wlbmids.length];

    for (int i = 0; i < wlbmids.length; i++) {
      if (h.containsKey(wlbmids[i]))
        retvos[i] = ((ProduceCoreVO)h.get(wlbmids[i]));
    }
    return retvos;
  }

  private ProduceCoreVO[] getBatchFjldwInfosFromDB(String[] wlbmids)
    throws SQLException
  {
    String sql = " select b.pk_invbasdoc, b.invcode, b.invname, b.invspec, b.invtype, b.graphid, c.pk_measdoc fjldwid, c.mainmeasrate, c.fixedflag, b.pk_measdoc zjldwid, m.measname zjldwmc, m1.measname fjldwmc from bd_invbasdoc b inner join bd_measdoc m on b.pk_measdoc = m.pk_measdoc left outer join (bd_convert c inner join bd_measdoc m1 on c.pk_measdoc = m1.pk_measdoc) on b.pk_invbasdoc = c.pk_invbasdoc and b.pk_measdoc5 = c.pk_measdoc where b.pk_invbasdoc in ";

    ProduceCoreVO convert = null;
    ProduceCoreVO[] converts = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();

      ArrayList list = cutArray(wlbmids);

      String[] strWlbms = null;
      String strWlbm = null;
      for (int i = 0; i < list.size(); i++) {
        strWlbms = (String[])(String[])list.get(i);
        strWlbm = orgWhereIn(strWlbms);

        stmt = con.prepareStatement(sql + strWlbm);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
          convert = new ProduceCoreVO();

          String pk_invbasdoc = rs.getString(1);
          convert.setPk_invbasdoc(pk_invbasdoc == null ? null : pk_invbasdoc.trim());

          String invcode = rs.getString(2);
          convert.setInvcode(invcode == null ? null : invcode.trim());

          String invname = rs.getString(3);
          convert.setInvname(invname == null ? null : invname.trim());

          String invspec = rs.getString(4);
          convert.setInvspec(invspec == null ? null : invspec.trim());

          String invtype = rs.getString(5);
          convert.setInvtype(invtype == null ? null : invtype.trim());

          String graphid = rs.getString(6);
          convert.setGraphid(graphid == null ? null : graphid.trim());

          String pk_measdoc = rs.getString(7);
          convert.setFjldwid(pk_measdoc == null ? null : pk_measdoc.trim());

          BigDecimal mainmeasrate = (BigDecimal)rs.getObject(8);
          convert.setMainmeasrate(mainmeasrate == null ? null : new UFDouble(mainmeasrate));

          String fixedflag = rs.getString(9);
          convert.setFixedflag(fixedflag == null ? null : new UFBoolean(fixedflag.trim()));

          String main_pk_measdoc = rs.getString(10);
          convert.setPk_measdoc(main_pk_measdoc == null ? null : main_pk_measdoc.trim());

          String mainMeasname = rs.getString(11);
          convert.setMeasname(mainMeasname == null ? null : mainMeasname.trim());

          String fmeasname = rs.getString(12);
          convert.setFjldwmc(fmeasname == null ? null : fmeasname.trim());

          v.addElement(convert);
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
    converts = new ProduceCoreVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(converts);
    }
    return converts;
  }

  private ArrayList cutArray(String[] wlbmids)
  {
    int max_len = 200;
    ArrayList al = new ArrayList();
    if (max_len > 0) {
      if (wlbmids.length > max_len) {
        int current_pos = 0;
        while (current_pos < wlbmids.length) {
          int last_len = wlbmids.length - current_pos;
          int len = max_len;
          if (last_len < max_len) {
            len = last_len;
          }
          String[] strArray = new String[len];
          System.arraycopy(wlbmids, current_pos, strArray, 0, len);
          al.add(strArray);
          current_pos += len;
        }
      }
      else {
        al.add(wlbmids);
      }
    }
    return al;
  }

  private String orgWhereIn(String[] wlbmids)
  {
    StringBuffer where = new StringBuffer();
    where.append(" ");
    for (int i = 0; i < wlbmids.length - 1; i++) {
      where.append("'");
      where.append(wlbmids[i]);
      where.append("',");
    }
    where.append("'" + wlbmids[(wlbmids.length - 1)] + "'");
    where.insert(0, "(");

    where.append(")");
    return where.toString();
  }

  /**
   * @author shikun
   * */
  public String[] getAvaVersion2(String pk_corp, String gcbm, String wlbmid, String zzfsid, String dx,String bomer)
    throws SQLException, BusinessException
  {
    String[] res = new String[4];
    if (res[0] == null) {
      String[] resbom = new String[2];
      String[] resrt = new String[2];
      if (dx.equals("A"))
        resbom = getVerFromBom2(pk_corp, gcbm, wlbmid,bomer);
      else {
        throw new RuntimeException("Unsupported object id:" + dx);
      }
      res[0] = resbom[0];
      res[1] = resbom[1];
      res[2] = resrt[0];
      res[3] = resrt[1];
    }
    return res;
  }

  /**
   * @author shikun
   * */
  private String[] getVerFromBom2(String pk_corp, String gcbm, String wlbmid,String bomver) throws SQLException
  {
    String[] res = new String[2];
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      String bomversql = "select pk_bomid,version from bd_bom where wlbmid=? and pk_corp=? and gcbm=? and bblx=0  and version='"+bomver+"'";
      stmt = con.prepareStatement(bomversql);
      stmt.setString(1, wlbmid);
      stmt.setString(2, pk_corp);
      stmt.setString(3, gcbm);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        res[0] = rs.getString(1);
        res[1] = rs.getString(2);
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
    return res;
  }
}