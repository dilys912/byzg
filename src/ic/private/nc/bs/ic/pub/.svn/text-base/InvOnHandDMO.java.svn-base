package nc.bs.ic.pub;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.framework.server.util.NewObjectService;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.para.SysInitBO;
import nc.bs.scm.ic.freeitem.DefdefDMO;
import nc.itf.ic.service.IICPub_InvOnHandDMO;
import nc.vo.ic.pub.InvOnHandVO;
import nc.vo.ic.pub.bill.OnhandnumItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.ATPVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pu.ATPForOneInvBillVO;
import nc.vo.scm.pu.ATPForOneInvHeaderVO;
import nc.vo.scm.pu.ATPForOneInvItemVO;
import nc.vo.scm.pub.SCMEnv;

public class InvOnHandDMO extends DataManageObject
  implements IICPub_InvOnHandDMO
{
  public InvOnHandDMO()
    throws NamingException, SystemException
  {
  }

  public CircularlyAccessibleValueObject[] getNDetailOnHandNums(String sWhere)
    throws Exception
  {
    if (sWhere == null) {
      return null;
    }
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT ccalbodyid,cwarehouseid,cinventoryid,vbatchcode,castunitid,vfree1,vfree2,vfree3,vfree4,vfree5 ,SUM(COALESCE(v1.ninspacenum,0.0)-COALESCE(v1.noutspacenum,0.0)) as nonhandnum,SUM(COALESCE(v1.ninspaceassistnum,0.0)-COALESCE(v1.noutspaceassistnum,0.0)) as nonhandassistnum from v_ic_onhandnum3 v1 left outer join bd_cargdoc c on v1.cspaceid=c.pk_cargdoc where  ( c.isrmplace='N' or c.isrmplace is null) and " + sWhere);

    sql.append(" group by ccalbodyid,cwarehouseid,cinventoryid,vbatchcode,castunitid,vfree1,vfree2,vfree3,vfree4,vfree5");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList alret = new ArrayList();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      rs = stmt.executeQuery();
      ResultSetMetaData meta = rs.getMetaData();
      GenMethod gm = new GenMethod();

      while (rs.next()) {
        OnhandnumItemVO vo = new OnhandnumItemVO();
        String tmp = rs.getString("ccalbodyid");
        vo.setCcalbodyid(tmp);
        tmp = rs.getString("cwarehouseid");
        vo.setCwarehouseid(tmp);
        tmp = rs.getString("cinventoryid");
        vo.setCinventoryid(tmp);
        tmp = rs.getString("vbatchcode");
        vo.setVlot(tmp);
        tmp = rs.getString("castunitid");
        vo.setCastunitid(tmp);
        for (int i = 1; i <= 5; i++) {
          String vfree = "vfree" + String.valueOf(i);
          tmp = rs.getString(vfree);
          vo.setAttributeValue(vfree, tmp);
        }
        BigDecimal num = rs.getBigDecimal("nonhandnum");
        if (num != null) {
          vo.setNnum(new UFDouble(num));
        }
        BigDecimal assistnum = rs.getBigDecimal("nonhandassistnum");
        if (assistnum != null) {
          vo.setNastnum(new UFDouble(assistnum));
        }

        alret.add(vo);
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
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    OnhandnumItemVO[] vos = null;
    if (alret.size() > 0) {
      vos = new OnhandnumItemVO[alret.size()];
      alret.toArray(vos);
    }

    return vos;
  }

  public CircularlyAccessibleValueObject[] getNDetailOnRoadNums(String sWhere)
    throws Exception
  {
    if (sWhere == null) {
      return null;
    }
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT ccalbodyid,cwarehouseid,cinventoryid,vbatchcode,castunitid,vfree1,vfree2,vfree3,vfree4,vfree5 ,SUM(COALESCE(v1.ninspacenum,0.0)-COALESCE(v1.noutspacenum,0.0)) as nonhandnum,SUM(COALESCE(v1.ninspaceassistnum,0.0)-COALESCE(v1.noutspaceassistnum,0.0)) as nonhandassistnum from v_ic_onhandnum3 v1,bd_cargdoc c  where v1.cspaceid=c.pk_cargdoc and c.isrmplace='Y' and " + sWhere);

    sql.append(" group by ccalbodyid,cwarehouseid,cinventoryid,vbatchcode,castunitid,vfree1,vfree2,vfree3,vfree4,vfree5");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList alret = new ArrayList();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      rs = stmt.executeQuery();
      ResultSetMetaData meta = rs.getMetaData();
      GenMethod gm = new GenMethod();

      while (rs.next()) {
        OnhandnumItemVO vo = new OnhandnumItemVO();
        String tmp = rs.getString("ccalbodyid");
        vo.setCcalbodyid(tmp);
        tmp = rs.getString("cwarehouseid");
        vo.setCwarehouseid(tmp);
        tmp = rs.getString("cinventoryid");
        vo.setCinventoryid(tmp);
        tmp = rs.getString("vbatchcode");
        vo.setVlot(tmp);
        tmp = rs.getString("castunitid");
        vo.setCastunitid(tmp);
        for (int i = 1; i <= 5; i++) {
          String vfree = "vfree" + String.valueOf(i);
          tmp = rs.getString(vfree);
          vo.setAttributeValue(vfree, tmp);
        }
        BigDecimal num = rs.getBigDecimal("nonhandnum");
        if (num != null) {
          vo.setNnum(new UFDouble(num));
        }
        BigDecimal assistnum = rs.getBigDecimal("nonhandassistnum");
        if (assistnum != null) {
          vo.setNastnum(new UFDouble(assistnum));
        }

        alret.add(vo);
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
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    OnhandnumItemVO[] vos = null;
    if (alret.size() > 0) {
      vos = new OnhandnumItemVO[alret.size()];
      alret.toArray(vos);
    }

    return vos;
  }

  public UFBoolean isAffectAtp(String cwhid)
  {
    StringBuffer sql = new StringBuffer("select isatpaffected from bd_stordoc where pk_stordoc=?");

    boolean isAffected = true;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, cwhid);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String isatp = rs.getString(1);

        if ((isatp != null) && (isatp.equals("N")))
          isAffected = false;
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    return new UFBoolean(isAffected);
  }

  public InvOnHandDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public UFDouble[] calculateStockNum(String pk_corp, String pk_calbody, String[] invBasDocPks, UFDate dstart, UFDate dend, UFDouble n, UFDouble m)
    throws SQLException
  {
    Hashtable htOutNum = null;
    try {
      htOutNum = getOutNumByInvDays(pk_corp, pk_calbody, invBasDocPks, dstart.toString(), dend.toString());
    } catch (SQLException e) {
      throw e;
    }

    UFDouble[] stocknums = new UFDouble[invBasDocPks.length];
    UFDouble outnum = null;
    int days = UFDate.getDaysBetween(dstart, dend);
    UFDouble ufdays = new UFDouble(days);
    for (int i = 0; i < invBasDocPks.length; i++) {
      if (htOutNum.containsKey(invBasDocPks[i])) {
        outnum = (UFDouble)htOutNum.get(invBasDocPks[i]);
        stocknums[i] = execFomula(outnum, ufdays, n, m);
      }
      else {
        stocknums[i] = null;
      }
    }
    return stocknums;
  }

  private UFDouble execFomula(UFDouble outNum, UFDouble days, UFDouble n, UFDouble m)
  {
    UFDouble StockNum = null;

    if ((outNum != null) && (days != null) && (n != null) && (m != null)) {
      double tmp = outNum.doubleValue() / days.doubleValue() * n.doubleValue() * m.doubleValue();

      StockNum = new UFDouble(tmp);
    }

    return StockNum;
  }

  public ATPForOneInvBillVO getATP(String[] pk_corps, String pk_invbasdoc, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5, String vfree6, String vfree7, String vfree8, String vfree9, String vfree10, String end_date)
    throws BusinessException
  {
    if ((pk_corps == null) || (pk_corps.length == 0) || (pk_invbasdoc == null)) {
      throw new BusinessException("Caused by:", new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000119")));
    }

    ATPForOneInvBillVO voatp = new ATPForOneInvBillVO();
    ATPForOneInvHeaderVO voatpHeader = new ATPForOneInvHeaderVO();
    ATPForOneInvItemVO[] voatpItems = null;
    try
    {
      InvATPDMO dmo = new InvATPDMO();
      InvOnHandDMO dmoOnHand = new InvOnHandDMO();

      String[] invinfos = dmo.queryInvInfo(pk_invbasdoc);
      String[] invids = dmo.queryPk_invmandoc(pk_corps, pk_invbasdoc);

      if ((invinfos != null) && (invinfos.length > 0)) {
        voatpHeader.setAttributeValue("invcode", invinfos[0]);
        voatpHeader.setAttributeValue("invname", invinfos[1]);
      }
      if ((invids != null) && (invids.length > 0))
      {
        String pk_invmandoc = null;
        for (int i = 0; i < invids.length; i++) {
          if ((invids[i] != null) && (invids[i].length() > 0)) {
            pk_invmandoc = invids[i];
            break;
          }
        }
        StringBuffer freeitem = new StringBuffer();

        if ((vfree1 != null) || (vfree2 != null) || (vfree3 != null) || (vfree4 != null) || (vfree5 != null) || (vfree6 != null) || (vfree7 != null) || (vfree8 != null) || (vfree9 != null) || (vfree10 != null))
        {
          FreeVO freevo = dmoOnHand.queryFreeVO(pk_invmandoc, new String[] { vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10 });

          if (freevo != null)
            voatpHeader.setAttributeValue("freeitem", freevo.getVfree0());
        }
      }
      voatp.setParentVO(voatpHeader);

      Vector v = new Vector();
      for (int k = 0; k < pk_corps.length; k++) {
        if ((pk_corps[k] == null) || (invids[k] == null))
          continue;
        String[][] pk_calbodies = dmo.queryCalbody(pk_corps[k], invids[k]);

        ATPForOneInvItemVO voatpItem = null;
        UFDouble nonhandnum = null;
        UFDouble natpnum = null;
        UFDouble natpdatenum = null;
        if ((pk_calbodies != null) && (pk_calbodies.length > 0)) {
          for (int i = 0; i < pk_calbodies.length; i++) {
            if (pk_calbodies[i][0] == null)
              continue;
            voatpItem = new ATPForOneInvItemVO();
            natpnum = dmo.getATPNum(pk_corps[k], pk_calbodies[i][0], invids[k], vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10, null, null, null);

            natpdatenum = dmo.getATPNum(pk_corps[k], pk_calbodies[i][0], invids[k], vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10, end_date, null, null);

            nonhandnum = dmoOnHand.getOnHandNum(pk_corps[k], pk_calbodies[i][0], invids[k], vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10, null);

            voatpItem.setAttributeValue("handnum", nonhandnum);
            voatpItem.setAttributeValue("usablenum", natpnum);
            voatpItem.setAttributeValue("usablenumbydate", natpdatenum);
            voatpItem.setAttributeValue("calbodyname", pk_calbodies[i][1]);
            voatpItem.setAttributeValue("unitname", pk_calbodies[i][2]);
            v.add(voatpItem);
          }
        }

        if (v.size() > 0) {
          voatpItems = new ATPForOneInvItemVO[v.size()];
          v.copyInto(voatpItems);
        }

        voatp.setChildrenVO(voatpItems);
      }
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }

    return voatp;
  }

  public UFDouble getCustOutNum(UFDate StartDay, UFDate EndDay, String ccustmanid, String cinvmanid)
    throws SQLException
  {
    if ((StartDay == null) || (EndDay == null) || (ccustmanid == null) || (ccustmanid == null)) {
      return null;
    }
    String sql = null;
    UFDouble ufd = null;

    sql = "select  sum(coalesce(noutnum,0.0))  from ic_general_h h inner join ic_general_b b on h.cgeneralhid=b.cgeneralhid where  b.dbizdate >=? and b.dbizdate<=?  and h.ccustomerid=? and b.cinventoryid=? ";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, StartDay.toString());
      stmt.setString(2, EndDay.toString());
      stmt.setString(3, ccustmanid);
      stmt.setString(4, cinvmanid);

      rs = stmt.executeQuery();

      if (rs.next())
      {
        BigDecimal num = rs.getBigDecimal(1);
        ufd = num == null ? null : new UFDouble(num);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    return ufd;
  }

  public int getForeDayCount(String pk_corp)
  {
    int iDay = 30;

    SysInitBO initBO = null;
    try {
      initBO = new SysInitBO();
      String sDays = initBO.getParaString(pk_corp, "IC022");
      if (sDays != null)
        iDay = Integer.parseInt(sDays.trim());
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
    return iDay;
  }

  public UFDouble getInNum(UFDate StartDay, UFDate EndDay, String PKCalbody, String InvID)
    throws SQLException
  {
    if ((StartDay == null) || (EndDay == null) || (PKCalbody == null) || (InvID == null)) {
      return null;
    }
    String sql = null;
    UFDouble ufd = null;

    sql = "select  sum(coalesce(ninnum,0.0)-coalesce(noutnum,0.0))  from ic_keep_detail1 b LEFT OUTER JOIN bd_invmandoc m on b.cinventoryid = m.pk_invmandoc LEFT OUTER JOIN bd_invbasdoc d ON m.pk_invbasdoc=d.pk_invbasdoc    where  b.dbizdate >=? and b.dbizdate<=? and (b.ninnum>0 or b.noutnum<0) and b.ccalbodyid=? and b.cinventoryid=? group by b.cinventoryid";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, StartDay.toString());
      stmt.setString(2, EndDay.toString());
      stmt.setString(3, PKCalbody);
      stmt.setString(4, InvID);

      rs = stmt.executeQuery();

      if (rs.next())
      {
        BigDecimal num = rs.getBigDecimal(1);
        ufd = num == null ? null : new UFDouble(num);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    return ufd;
  }

  public UFDouble[] getInvClassCodePreNum(String pk_corp, String pk_calbody, String invclasscode, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (invclasscode == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000131"));
    }

    UFDouble[] ufRets = new UFDouble[2];
    UFDouble preinnum = new UFDouble(0);
    UFDouble preoutnum = new UFDouble(0);
    try
    {
      String key = null;
      Hashtable ht = new Hashtable();
      InvATPDMO dmo = new InvATPDMO();

      ATPVO[] vos = dmo.queryAtpNum(pk_corp, pk_calbody, startdate, enddate, new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), " invclasscode like '" + invclasscode + "%'");
      if (vos != null)
      {
        UFDouble uf = null;
        for (int i = 0; i < vos.length; i++)
        {
          if (vos[i] == null) {
            continue;
          }
          preinnum = preinnum.add(vos[i].calculatePreIn());
          preoutnum = preoutnum.add(vos[i].calculatePreOut());
        }

      }

    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }

    ufRets[0] = preinnum;
    ufRets[1] = preoutnum;
    return ufRets;
  }

  public UFDouble[] getInvClassPreNum(String pk_corp, String pk_calbody, String pk_invcl, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (pk_invcl == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000131"));
    }

    String sqlInvOfClass = "select pk_invmandoc from bd_invmandoc LEFT OUTER join bd_invbasdoc on bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc left outer join bd_invcl on bd_invbasdoc.pk_invcl=bd_invcl.pk_invcl ,(select invclasscode,len(invclasscode) as ilevellength from bd_invcl where pk_invcl='" + pk_invcl + "' ) aa where substring(bd_invcl.invclasscode,1,aa.ilevellength)=aa.invclasscode and bd_invmandoc.pk_corp='" + pk_corp + "'";

    UFDouble[] ufRets = new UFDouble[2];
    UFDouble preinnum = new UFDouble(0);
    UFDouble preoutnum = new UFDouble(0);
    try
    {
      String key = null;
      Hashtable ht = new Hashtable();
      InvATPDMO dmo = new InvATPDMO();
      ATPVO[] vos = dmo.queryAtpNum(pk_corp, pk_calbody, startdate, enddate, new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), " cinventoryid in (" + sqlInvOfClass + ")");
      if (vos != null)
      {
        UFDouble uf = null;
        for (int i = 0; i < vos.length; i++)
        {
          uf = vos[i].calculatePreIn();
          if (uf != null)
            preinnum = preinnum.add(uf);
          uf = vos[i].calculatePreOut();
          if (uf != null) {
            preoutnum = preoutnum.add(uf);
          }
        }

      }

    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }

    ufRets[0] = preinnum;
    ufRets[1] = preoutnum;
    return ufRets;
  }

  public UFDouble getOnHandNum(String pk_invmandoc)
    throws Exception
  {
    if (pk_invmandoc == null) {
      return new UFDouble(0.0D);
    }
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT SUM(COALESCE(v1.nonhandnum,0.0)) from  " + GeneralSqlString.V_IC_ONHANDNUM + "  v1  where  v1.cinventoryid=?");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_invmandoc);

      rs = stmt.executeQuery();

      if (rs.next()) {
        Object o = rs.getObject(1);
        onhandnum = new UFDouble(o == null ? "0.0" : o.toString());
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
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    return onhandnum;
  }

  public InvOnHandVO[] getOnHandNum(String pk_corp, String pk_warehouse, String pk_invmandoc, int isFreeExpand, int isBatchExpand, int isSpaceExpand, int isAssistExpand)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_warehouse, pk_invmandoc });

    StringBuffer sql = new StringBuffer();
    sql.append("select ");
    if (isFreeExpand == 1) {
      sql.append(" vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10, ");
    }

    if (isBatchExpand == 1) {
      sql.append(" vbatchcode,dvalidate,");
    }
    if (isSpaceExpand == 1) {
      sql.append("cspaceid,");
    }
    if (isAssistExpand == 1) {
      sql.append("castunitid,");
    }
    sql.append(" SUM(COALESCE(v1.ninspacenum,0.0)-COALESCE(v1.noutspacenum,0.0)) ");
    if (isAssistExpand == 1) {
      sql.append(",SUM(COALESCE(v1.ninspaceassistnum,0.0)-COALESCE(v1.noutspaceassistnum,0.0)) ");
    }

    sql.append(" from v_ic_onhandnum6 v1 where pk_corp=? and cwarehouseid=? and cinventoryid=? ");

    if (isFreeExpand + isBatchExpand + isSpaceExpand + isAssistExpand > 0) {
      sql.append(" group by ");
    }
    if (isFreeExpand == 1) {
      sql.append(" vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10 ");
    }

    if (isBatchExpand == 1) {
      if (isFreeExpand == 1)
        sql.append(",");
      sql.append(" vbatchcode,dvalidate ");
    }
    if (isSpaceExpand == 1) {
      if ((isFreeExpand == 1) || (isBatchExpand == 1))
        sql.append(",");
      sql.append(" cspaceid ");
    }
    if (isAssistExpand == 1) {
      if ((isFreeExpand == 1) || (isBatchExpand == 1) || (isSpaceExpand == 1))
        sql.append(",");
      sql.append("castunitid ");
    }

    Vector retv = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    UFDouble ufd0 = new UFDouble(0);
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);
      stmt.setString(2, pk_warehouse);
      stmt.setString(3, pk_invmandoc);

      rs = stmt.executeQuery();

      while (rs.next()) {
        InvOnHandVO me = new InvOnHandVO();

        int index = 1;
        if (isFreeExpand == 1) {
          String tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree1(tmp);
          tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree2(tmp);
          tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree3(tmp);
          tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree4(tmp);
          tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree5(tmp);
          tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree6(tmp);
          tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree7(tmp);
          tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree8(tmp);
          tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree9(tmp);
          tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVfree10(tmp);
        }

        if (isBatchExpand == 1) {
          String tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setVbatchcode(tmp);
          String sx = rs.getString(index++);
          me.setDvalidate(sx == null ? null : new UFDate(sx));
        }
        if (isSpaceExpand == 1) {
          String tmp = rs.getString(index++);
          if (tmp != null)
            tmp = tmp.trim();
          me.setCspaceid(tmp);
        }

        if (isAssistExpand == 1) {
          me.setCastunitid(rs.getString(index++));
        }
        Object o = rs.getObject(index++);
        onhandnum = new UFDouble(o != null ? o.toString() : "0.0");
        me.setNmaster(onhandnum);
        if (isAssistExpand == 1) {
          Object b = rs.getObject(index++);
          me.setNassist(new UFDouble(b != null ? b.toString() : "0.0"));
        }

        StringBuffer t = new StringBuffer();
        t.append(me.getVfree1() == null ? "" : me.getVfree1());
        t.append(" ");
        t.append(me.getVfree2() == null ? "" : me.getVfree2());
        t.append(" ");
        t.append(me.getVfree3() == null ? "" : me.getVfree3());
        t.append(" ");
        t.append(me.getVfree4() == null ? "" : me.getVfree4());
        t.append(" ");
        t.append(me.getVfree5() == null ? "" : me.getVfree5());
        t.append(" ");
        t.append(me.getVfree6() == null ? "" : me.getVfree6());
        t.append(" ");
        t.append(me.getVfree7() == null ? "" : me.getVfree7());
        t.append(" ");
        t.append(me.getVfree8() == null ? "" : me.getVfree8());
        t.append(" ");
        t.append(me.getVfree9() == null ? "" : me.getVfree9());
        t.append(" ");
        t.append(me.getVfree10() == null ? "" : me.getVfree10());
        me.m_free = t.toString().trim();
        if (onhandnum.compareTo(ufd0) != 0)
          retv.addElement(me);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_warehouse, pk_invmandoc });

    InvOnHandVO[] retvo = new InvOnHandVO[retv.size()];
    for (int i = 0; i < retvo.length; i++) {
      retvo[i] = ((InvOnHandVO)retv.elementAt(i));
    }
    return retvo;
  }

  public UFDouble getOnHandNum(String pk_corp, String pk_calbody, String pk_warehouse, String pk_invmandoc, String free1, String free2, String free3, String free4, String free5, String batchNO)
    throws BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (pk_invmandoc == null)) {
      return null;
    }
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT SUM(COALESCE(v1.nonhandnum,0.0)) from  " + GeneralSqlString.V_IC_ONHANDNUM + "  v1  where v1.pk_corp=? and v1.ccalbodyid=? and v1.cinventoryid=?");

    if ((pk_warehouse != null) && (pk_warehouse.length() != 0)) {
      sql.append(" and v1.cwarehouseid =? ");
    }

    if ((free1 != null) && (free1.length() != 0)) {
      sql.append(" and vfree1 = ?");
    }
    if ((free2 != null) && (free2.length() != 0)) {
      sql.append(" and vfree2 = ?");
    }
    if ((free3 != null) && (free3.length() != 0)) {
      sql.append(" and vfree3 = ?");
    }
    if ((free4 != null) && (free4.length() != 0)) {
      sql.append(" and vfree4 = ?");
    }
    if ((free5 != null) && (free5.length() != 0)) {
      sql.append(" and vfree5 = ?");
    }

    if ((batchNO != null) && (batchNO.length() != 0)) {
      sql.append(" and vlot = ?");
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);
      stmt.setString(2, pk_calbody);
      stmt.setString(3, pk_invmandoc);
      int index = 4;
      if ((pk_warehouse != null) && (pk_warehouse.length() != 0)) {
        stmt.setString(index++, pk_warehouse);
      }
      if ((free1 != null) && (free1.length() != 0)) {
        stmt.setString(index++, free1);
      }
      if ((free2 != null) && (free2.length() != 0)) {
        stmt.setString(index++, free2);
      }
      if ((free3 != null) && (free3.length() != 0)) {
        stmt.setString(index++, free3);
      }
      if ((free4 != null) && (free4.length() != 0)) {
        stmt.setString(index++, free4);
      }
      if ((free5 != null) && (free5.length() != 0)) {
        stmt.setString(index++, free5);
      }

      if ((batchNO != null) && (batchNO.length() != 0)) {
        stmt.setString(index++, batchNO);
      }
      rs = stmt.executeQuery();

      if (rs.next()) {
        Object o = rs.getObject(1);
        onhandnum = new UFDouble(o == null ? "0.0" : o.toString());
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    return onhandnum;
  }

  public UFDouble getOnHandNum(String pk_corp, String pk_invmandoc, String free1, String free2, String free3, String free4, String free5, String free6, String free7, String free8, String free9, String free10, String batchNO)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_invmandoc });

    StringBuffer sql = new StringBuffer();

    sql.append("SELECT SUM(COALESCE(v1.nonhandnum,0.0)) from  " + GeneralSqlString.V_IC_ONHANDNUM + "  v1 where v1.pk_corp=? and v1.cinventoryid=? ");

    if ((free1 != null) && (free1.length() != 0)) {
      sql.append(" and vfree1 = ?");
    }
    if ((free2 != null) && (free2.length() != 0)) {
      sql.append(" and vfree2 = ?");
    }
    if ((free3 != null) && (free3.length() != 0)) {
      sql.append(" and vfree3 = ?");
    }
    if ((free4 != null) && (free4.length() != 0)) {
      sql.append(" and vfree4 = ?");
    }
    if ((free5 != null) && (free5.length() != 0)) {
      sql.append(" and vfree5 = ?");
    }
    if ((free6 != null) && (free6.length() != 0)) {
      sql.append(" and vfree6 = ?");
    }
    if ((free7 != null) && (free7.length() != 0)) {
      sql.append(" and vfree7 = ?");
    }
    if ((free8 != null) && (free8.length() != 0)) {
      sql.append(" and vfree8 = ?");
    }
    if ((free9 != null) && (free9.length() != 0)) {
      sql.append(" and vfree9 = ?");
    }
    if ((free10 != null) && (free10.length() != 0)) {
      sql.append(" and vfree10 = ?");
    }
    if ((batchNO != null) && (batchNO.length() != 0)) {
      sql.append(" and vlot = ?");
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);
      stmt.setString(2, pk_invmandoc);
      int index = 3;
      if ((free1 != null) && (free1.length() != 0)) {
        stmt.setString(index++, free1);
      }
      if ((free2 != null) && (free2.length() != 0)) {
        stmt.setString(index++, free2);
      }
      if ((free3 != null) && (free3.length() != 0)) {
        stmt.setString(index++, free3);
      }
      if ((free4 != null) && (free4.length() != 0)) {
        stmt.setString(index++, free4);
      }
      if ((free5 != null) && (free5.length() != 0)) {
        stmt.setString(index++, free5);
      }
      if ((free6 != null) && (free6.length() != 0)) {
        stmt.setString(index++, free6);
      }
      if ((free7 != null) && (free7.length() != 0)) {
        stmt.setString(index++, free7);
      }
      if ((free8 != null) && (free8.length() != 0)) {
        stmt.setString(index++, free8);
      }
      if ((free9 != null) && (free9.length() != 0)) {
        stmt.setString(index++, free9);
      }
      if ((free10 != null) && (free10.length() != 0)) {
        stmt.setString(index++, free10);
      }
      if ((batchNO != null) && (batchNO.length() != 0)) {
        stmt.setString(index++, batchNO);
      }
      rs = stmt.executeQuery();

      if (rs.next()) {
        Object o = rs.getObject(1);
        onhandnum = new UFDouble(o == null ? "0.0" : o.toString());
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_invmandoc });

    return onhandnum;
  }

  public UFDouble getOnHandNum(String pk_corp, String pk_calbody, String pk_invmandoc, String free1, String free2, String free3, String free4, String free5, String free6, String free7, String free8, String free9, String free10, String batchNO)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_calbody, pk_invmandoc });

    StringBuffer sql = new StringBuffer();

    sql.append("SELECT SUM(COALESCE(v1.nonhandnum,0.0)) from  " + GeneralSqlString.V_IC_ONHANDNUM + "  v1 where v1.pk_corp=? and v1.cwarehouseid in (SELECT pk_stordoc from bd_stordoc where pk_corp=? and pk_calbody=?) and v1.cinventoryid=?");

    if ((free1 != null) && (free1.length() != 0)) {
      sql.append(" and vfree1 = ?");
    }
    if ((free2 != null) && (free2.length() != 0)) {
      sql.append(" and vfree2 = ?");
    }
    if ((free3 != null) && (free3.length() != 0)) {
      sql.append(" and vfree3 = ?");
    }
    if ((free4 != null) && (free4.length() != 0)) {
      sql.append(" and vfree4 = ?");
    }
    if ((free5 != null) && (free5.length() != 0)) {
      sql.append(" and vfree5 = ?");
    }
    if ((free6 != null) && (free6.length() != 0)) {
      sql.append(" and vfree6 = ?");
    }
    if ((free7 != null) && (free7.length() != 0)) {
      sql.append(" and vfree7 = ?");
    }
    if ((free8 != null) && (free8.length() != 0)) {
      sql.append(" and vfree8 = ?");
    }
    if ((free9 != null) && (free9.length() != 0)) {
      sql.append(" and vfree9 = ?");
    }
    if ((free10 != null) && (free10.length() != 0)) {
      sql.append(" and vfree10 = ?");
    }
    if ((batchNO != null) && (batchNO.length() != 0)) {
      sql.append(" and vlot = ?");
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);
      stmt.setString(2, pk_corp);
      stmt.setString(3, pk_calbody);
      stmt.setString(4, pk_invmandoc);
      int index = 5;
      if ((free1 != null) && (free1.length() != 0)) {
        stmt.setString(index++, free1);
      }
      if ((free2 != null) && (free2.length() != 0)) {
        stmt.setString(index++, free2);
      }
      if ((free3 != null) && (free3.length() != 0)) {
        stmt.setString(index++, free3);
      }
      if ((free4 != null) && (free4.length() != 0)) {
        stmt.setString(index++, free4);
      }
      if ((free5 != null) && (free5.length() != 0)) {
        stmt.setString(index++, free5);
      }
      if ((free6 != null) && (free6.length() != 0)) {
        stmt.setString(index++, free6);
      }
      if ((free7 != null) && (free7.length() != 0)) {
        stmt.setString(index++, free7);
      }
      if ((free8 != null) && (free8.length() != 0)) {
        stmt.setString(index++, free8);
      }
      if ((free9 != null) && (free9.length() != 0)) {
        stmt.setString(index++, free9);
      }
      if ((free10 != null) && (free10.length() != 0)) {
        stmt.setString(index++, free10);
      }
      if ((batchNO != null) && (batchNO.length() != 0)) {
        stmt.setString(index++, batchNO);
      }
      rs = stmt.executeQuery();

      if (rs.next()) {
        Object o = rs.getObject(1);
        onhandnum = new UFDouble(o == null ? "0.0" : o.toString());
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_calbody, pk_invmandoc });

    return onhandnum;
  }

  public UFDouble getOnHandNum(String pk_corp, String pk_calbody, String pk_warehouse, String pk_invmandoc, String free1, String free2, String free3, String free4, String free5, String free6, String free7, String free8, String free9, String free10, String batchNO)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_calbody, pk_warehouse, pk_invmandoc });

    StringBuffer sql = new StringBuffer();

    sql.append("SELECT SUM(COALESCE(v1.nonhandnum,0.0)) from  " + GeneralSqlString.V_IC_ONHANDNUM + "  v1 where  v1.cinventoryid=?");

    if (pk_warehouse != null)
      sql.append(" and v1.cwarehouseid =? ");
    if (pk_calbody != null)
      sql.append(" and v1.ccalbodyid =? ");
    if (pk_corp != null)
      sql.append(" and v1.pk_corp=? ");
    if ((free1 != null) && (free1.length() != 0)) {
      sql.append(" and vfree1 = ?");
    }
    if ((free2 != null) && (free2.length() != 0)) {
      sql.append(" and vfree2 = ?");
    }
    if ((free3 != null) && (free3.length() != 0)) {
      sql.append(" and vfree3 = ?");
    }
    if ((free4 != null) && (free4.length() != 0)) {
      sql.append(" and vfree4 = ?");
    }
    if ((free5 != null) && (free5.length() != 0)) {
      sql.append(" and vfree5 = ?");
    }
    if ((free6 != null) && (free6.length() != 0)) {
      sql.append(" and vfree6 = ?");
    }
    if ((free7 != null) && (free7.length() != 0)) {
      sql.append(" and vfree7 = ?");
    }
    if ((free8 != null) && (free8.length() != 0)) {
      sql.append(" and vfree8 = ?");
    }
    if ((free9 != null) && (free9.length() != 0)) {
      sql.append(" and vfree9 = ?");
    }
    if ((free10 != null) && (free10.length() != 0)) {
      sql.append(" and vfree10 = ?");
    }
    if ((batchNO != null) && (batchNO.length() != 0)) {
      sql.append(" and vlot = ?");
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      stmt.setString(1, pk_invmandoc);
      int index = 2;
      if (pk_warehouse != null)
        stmt.setString(index++, pk_warehouse);
      if (pk_calbody != null)
        stmt.setString(index++, pk_calbody);
      if (pk_corp != null)
        stmt.setString(index++, pk_corp);
      if ((free1 != null) && (free1.length() != 0)) {
        stmt.setString(index++, free1);
      }
      if ((free2 != null) && (free2.length() != 0)) {
        stmt.setString(index++, free2);
      }
      if ((free3 != null) && (free3.length() != 0)) {
        stmt.setString(index++, free3);
      }
      if ((free4 != null) && (free4.length() != 0)) {
        stmt.setString(index++, free4);
      }
      if ((free5 != null) && (free5.length() != 0)) {
        stmt.setString(index++, free5);
      }
      if ((free6 != null) && (free6.length() != 0)) {
        stmt.setString(index++, free6);
      }
      if ((free7 != null) && (free7.length() != 0)) {
        stmt.setString(index++, free7);
      }
      if ((free8 != null) && (free8.length() != 0)) {
        stmt.setString(index++, free8);
      }
      if ((free9 != null) && (free9.length() != 0)) {
        stmt.setString(index++, free9);
      }
      if ((free10 != null) && (free10.length() != 0)) {
        stmt.setString(index++, free10);
      }
      if ((batchNO != null) && (batchNO.length() != 0)) {
        stmt.setString(index++, batchNO);
      }
      rs = stmt.executeQuery();

      if (rs.next()) {
        Object o = rs.getObject(1);
        onhandnum = new UFDouble(o == null ? "0.0" : o.toString());
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_calbody, pk_warehouse, pk_invmandoc });

    return onhandnum;
  }

  public UFDouble getOnHandNum(String pk_corp, String pk_calbody, String pk_warehouse, String pk_invmandoc, String free1, String free2, String free3, String free4, String free5, String free6, String free7, String free8, String free9, String free10, String batchNO, String castunitid)
    throws BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (pk_invmandoc == null)) {
      return null;
    }
    StringBuffer sql = new StringBuffer();

    //edit by zwx 2015-9-9 发运单出库中的现存量改为可发货数量（现存量-冻结数量）
//    sql.append("SELECT SUM(COALESCE(v1.nonhandnum,0.0)) from  " + GeneralSqlString.V_IC_ONHANDNUM + "  v1  where v1.pk_corp=? and v1.ccalbodyid=? and v1.cinventoryid=?");

    sql.append(" select SUM(COALESCE(a.num,0.0)) -SUM(COALESCE(b.freezenum,0.0)) ") 
    .append("   from (select kp.pk_corp, ") 
    .append("                kp.ccalbodyid, ") 
    .append("                kp.cwarehouseid, ") 
    .append("                kp.cinventoryid, ") 
    .append("                kp.cspaceid, ") 
    .append("                kp.vbatchcode, ") 
    .append("                kp.vfree1, ") 
    .append("                kp.vfree2, ") 
    .append("                kp.vfree3, ") 
    .append("                kp.vfree4, ") 
    .append("                kp.vfree5, ") 
    .append("                kp.vfree6, ") 
    .append("                kp.vfree7, ") 
    .append("                kp.vfree8, ") 
    .append("                kp.vfree9, ") 
    .append("                kp.vfree10, ") 
    .append("                kp.castunitid, ") 
    .append("                SUM(nvl(ninspacenum, 0.0)) - SUM(nvl(noutspacenum, 0.0)) num, ") 
    .append("                SUM(nvl(ningrossnum, 0.0) - nvl(noutgrossnum, 0.0)) ngrossnum ") 
    .append("           from v_ic_onhandnum6 kp, ") 
    .append("                bd_stordoc      wh1, ") 
    .append("                bd_invmandoc    man, ") 
    .append("                bd_invbasdoc    inv ") 
    .append("          where kp.cwarehouseid = wh1.pk_stordoc(+) ") 
    .append("            and kp.cinventoryid = man.pk_invmandoc ") 
    .append("            and man.pk_invbasdoc = inv.pk_invbasdoc ") 
    .append("          group by kp.pk_corp, ") 
    .append("                   kp.ccalbodyid, ") 
    .append("                   kp.cwarehouseid, ") 
    .append("                   kp.cinventoryid, ") 
    .append("                   kp.cspaceid, ") 
    .append("                   kp.vbatchcode, ") 
    .append("                   kp.vfree1, ") 
    .append("                   kp.vfree2, ") 
    .append("                   kp.vfree3, ") 
    .append("                   kp.vfree4, ") 
    .append("                   kp.vfree5, ") 
    .append("                   kp.vfree6, ") 
    .append("                   kp.vfree7, ") 
    .append("                   kp.vfree8, ") 
    .append("                   kp.vfree9, ") 
    .append("                   kp.vfree10, ") 
    .append("                   kp.castunitid) a, ") 
    .append("        (select kp.pk_corp, ") 
    .append("                kp.ccalbodyid, ") 
    .append("                kp.cwarehouseid, ") 
    .append("                kp.cinventoryid, ") 
    .append("                kp.cspaceid, ") 
    .append("                kp.vbatchcode, ") 
    .append("                kp.vfree1, ") 
    .append("                kp.vfree2, ") 
    .append("                kp.vfree3, ") 
    .append("                kp.vfree4, ") 
    .append("                kp.vfree5, ") 
    .append("                kp.vfree6, ") 
    .append("                kp.vfree7, ") 
    .append("                kp.vfree8, ") 
    .append("                kp.vfree9, ") 
    .append("                kp.vfree10, ") 
    .append("                kp.castunitid, ") 
    .append("                sum(nvl(nfreezenum, 0)) freezenum, ") 
    .append("                sum(nvl(ngrossnum, 0)) ngrossnum ") 
    .append("           from ic_freeze    kp, ") 
    .append("                bd_stordoc   wh1, ") 
    .append("                bd_invmandoc man, ") 
    .append("                bd_invbasdoc inv ") 
    .append("          where kp.cwarehouseid = wh1.pk_stordoc(+) ") 
    .append("            and kp.cinventoryid = man.pk_invmandoc ") 
    .append("            and man.pk_invbasdoc = inv.pk_invbasdoc ") 
    .append("            and (cthawpersonid is null) ") 
    .append("          group by kp.pk_corp, ") 
    .append("                   kp.ccalbodyid, ") 
    .append("                   kp.cwarehouseid, ") 
    .append("                   kp.cinventoryid, ") 
    .append("                   kp.cspaceid, ") 
    .append("                   kp.vbatchcode, ") 
    .append("                   kp.vfree1, ") 
    .append("                   kp.vfree2, ") 
    .append("                   kp.vfree3, ") 
    .append("                   kp.vfree4, ") 
    .append("                   kp.vfree5, ") 
    .append("                   kp.vfree6, ") 
    .append("                   kp.vfree7, ") 
    .append("                   kp.vfree8, ") 
    .append("                   kp.vfree9, ") 
    .append("                   kp.vfree10, ") 
    .append("                   kp.castunitid) b ") 
    .append("  where a.pk_corp = b.pk_corp(+) ") 
    .append("    and a.ccalbodyid = b.ccalbodyid(+) ") 
    .append("    and a.cwarehouseid = b.cwarehouseid(+) ") 
    .append("    and a.cinventoryid = b.cinventoryid(+) ") 
    .append("    and nvl(a.cspaceid, ' ') = nvl(b.cspaceid(+), ' ') ") 
    .append("    and nvl(a.vbatchcode, ' ') = nvl(b.vbatchcode(+), ' ') ") 
    .append("    and nvl(a.vfree1, ' ') = nvl(b.vfree1(+), ' ') ") 
    .append("    and nvl(a.vfree2, ' ') = nvl(b.vfree2(+), ' ') ") 
    .append("    and nvl(a.vfree3, ' ') = nvl(b.vfree3(+), ' ') ") 
    .append("    and nvl(a.vfree4, ' ') = nvl(b.vfree4(+), ' ') ") 
    .append("    and nvl(a.vfree5, ' ') = nvl(b.vfree5(+), ' ') ") 
    .append("    and nvl(a.vfree6, ' ') = nvl(b.vfree6(+), ' ') ") 
    .append("    and nvl(a.vfree7, ' ') = nvl(b.vfree7(+), ' ') ") 
    .append("    and nvl(a.vfree8, ' ') = nvl(b.vfree8(+), ' ') ") 
    .append("    and nvl(a.vfree9, ' ') = nvl(b.vfree9(+), ' ') ") 
    .append("    and nvl(a.vfree10, ' ') = nvl(b.vfree10(+), ' ') ") 
    .append("    and a.pk_corp = ? ") 
    .append("    and a.ccalbodyid = ? ") 
    .append("    and a.cinventoryid = ? ");


    
    if ((pk_warehouse != null) && (pk_warehouse.length() != 0)) {
      sql.append(" and a.cwarehouseid =? ");
    }

    if ((free1 != null) && (free1.length() != 0)) {
//      sql.append(" and vfree1 = ?");
    	sql.append(" and a.vfree1 = ?");
    }
    if ((free2 != null) && (free2.length() != 0)) {
//      sql.append(" and vfree2 = ?");
    	sql.append(" and a.vfree2 = ?");
    }
    if ((free3 != null) && (free3.length() != 0)) {
//      sql.append(" and vfree3 = ?");
      sql.append(" and a.vfree3 = ?");
    }
    if ((free4 != null) && (free4.length() != 0)) {
//      sql.append(" and vfree4 = ?");
    	sql.append(" and a.vfree4 = ?");
    }
    if ((free5 != null) && (free5.length() != 0)) {
//      sql.append(" and vfree5 = ?");
      sql.append(" and a.vfree5 = ?");
    }
    if ((free6 != null) && (free6.length() != 0)) {
//      sql.append(" and vfree6 = ?");
    	sql.append(" and a.vfree6 = ?");
    }
    if ((free7 != null) && (free7.length() != 0)) {
//      sql.append(" and vfree7 = ?");
    	sql.append(" and a.vfree7 = ?");
    }
    if ((free8 != null) && (free8.length() != 0)) {
//      sql.append(" and vfree8 = ?");
    	sql.append(" and a.vfree8 = ?");
    }
    if ((free9 != null) && (free9.length() != 0)) {
//      sql.append(" and vfree9 = ?");
    	sql.append(" and a.vfree9 = ?");
    }
    if ((free10 != null) && (free10.length() != 0)) {
//      sql.append(" and vfree10 = ?");
    	sql.append(" and a.vfree10 = ?");
    }

    if ((batchNO != null) && (batchNO.length() != 0)) {
//      sql.append(" and vlot = ?");
      sql.append(" and a.vbatchcode = ?");
      
    }
    if ((castunitid != null) && (castunitid.length() != 0)) {
//      sql.append(" and castunitid = ?");
    	sql.append(" and a.castunitid = ?");
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);
      stmt.setString(2, pk_calbody);
      stmt.setString(3, pk_invmandoc);
      int index = 4;
      if ((pk_warehouse != null) && (pk_warehouse.length() != 0)) {
        stmt.setString(index++, pk_warehouse);
      }
      if ((free1 != null) && (free1.length() != 0)) {
        stmt.setString(index++, free1);
      }
      if ((free2 != null) && (free2.length() != 0)) {
        stmt.setString(index++, free2);
      }
      if ((free3 != null) && (free3.length() != 0)) {
        stmt.setString(index++, free3);
      }
      if ((free4 != null) && (free4.length() != 0)) {
        stmt.setString(index++, free4);
      }
      if ((free5 != null) && (free5.length() != 0)) {
        stmt.setString(index++, free5);
      }
      if ((free6 != null) && (free6.length() != 0)) {
        stmt.setString(index++, free6);
      }
      if ((free7 != null) && (free7.length() != 0)) {
        stmt.setString(index++, free7);
      }
      if ((free8 != null) && (free8.length() != 0)) {
        stmt.setString(index++, free8);
      }
      if ((free9 != null) && (free9.length() != 0)) {
        stmt.setString(index++, free9);
      }
      if ((free10 != null) && (free10.length() != 0)) {
        stmt.setString(index++, free10);
      }
      if ((batchNO != null) && (batchNO.length() != 0)) {
        stmt.setString(index++, batchNO);
      }
      if ((castunitid != null) && (castunitid.length() != 0)) {
        stmt.setString(index++, castunitid);
      }
      rs = stmt.executeQuery();

      if (rs.next()) {
        Object o = rs.getObject(1);
        onhandnum = new UFDouble(o == null ? "0.0" : o.toString());
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    return onhandnum;
  }

  public UFDouble getOnHandNumForMM(String pk_corp, String pk_calbody, String pk_warehouse, String pk_invmandoc, String free1, String free2, String free3, String free4, String free5, String vbatchcode)
    throws BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (pk_invmandoc == null)) {
      return null;
    }
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT SUM(COALESCE(v1.nonhandnum,0.0)) from  " + GeneralSqlString.V_IC_ONHANDNUM + "  v1  left outer join bd_stordoc on v1.cwarehouseid=bd_stordoc.pk_stordoc left outer join bd_calbody on bd_stordoc.pk_calbody=bd_calbody.pk_calbody   where bd_stordoc.mrpflag='Y' and v1.pk_corp=? and bd_calbody.pk_calbody=? and v1.cinventoryid=?");

    if ((pk_warehouse != null) && (pk_warehouse.length() != 0)) {
      sql.append(" and v1.cwarehouseid =? ");
    }

    if ((free1 != null) && (free1.length() != 0)) {
      sql.append(" and vfree1 = ?");
    }
    if ((free2 != null) && (free2.length() != 0)) {
      sql.append(" and vfree2 = ?");
    }
    if ((free3 != null) && (free3.length() != 0)) {
      sql.append(" and vfree3 = ?");
    }
    if ((free4 != null) && (free4.length() != 0)) {
      sql.append(" and vfree4 = ?");
    }
    if ((free5 != null) && (free5.length() != 0)) {
      sql.append(" and vfree5 = ?");
    }

    if ((vbatchcode != null) && (vbatchcode.length() != 0)) {
      sql.append(" and vlot = ?");
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);
      stmt.setString(2, pk_calbody);
      stmt.setString(3, pk_invmandoc);
      int index = 4;
      if ((pk_warehouse != null) && (pk_warehouse.length() != 0)) {
        stmt.setString(index++, pk_warehouse);
      }
      if ((free1 != null) && (free1.length() != 0)) {
        stmt.setString(index++, free1);
      }
      if ((free2 != null) && (free2.length() != 0)) {
        stmt.setString(index++, free2);
      }
      if ((free3 != null) && (free3.length() != 0)) {
        stmt.setString(index++, free3);
      }
      if ((free4 != null) && (free4.length() != 0)) {
        stmt.setString(index++, free4);
      }
      if ((free5 != null) && (free5.length() != 0)) {
        stmt.setString(index++, free5);
      }

      if ((vbatchcode != null) && (vbatchcode.length() != 0)) {
        stmt.setString(index++, vbatchcode);
      }
      rs = stmt.executeQuery();

      if (rs.next()) {
        Object o = rs.getObject(1);
        onhandnum = new UFDouble(o == null ? "0.0" : o.toString());
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    return onhandnum;
  }

  public UFDouble[] getOnhandNums(String pk_corp, String pk_calbody, String cwarehouseid, String[] invmanids)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_calbody, cwarehouseid, invmanids });

    if ((pk_corp == null) || (invmanids == null))
      return null;
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT cinventoryid ,SUM(COALESCE(ninnum,0.0)-COALESCE(noutnum,0.0))  from  v_ic_onhandnum4 ic_onhandnum  where ic_onhandnum.pk_corp=?  ");

    if (pk_calbody != null)
      sql.append(" and ccalbodyid='").append(pk_calbody).append("'");
    if (cwarehouseid != null)
      sql.append(" and cwarehouseid='").append(cwarehouseid).append("'");
    if (invmanids.length >= 1) {
      sql.append(GeneralSqlString.formInSQL("cinventoryid", invmanids));
    }

    sql.append(" group by cinventoryid ");
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    UFDouble ufd0 = new UFDouble(0);
    Hashtable htNum = new Hashtable();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);

      rs = stmt.executeQuery();

      while (rs.next()) {
        String invid = rs.getString(1);
        BigDecimal o = rs.getBigDecimal(2);
        onhandnum = o == null ? ufd0 : new UFDouble(o);
        if ((invid != null) && (onhandnum != null))
          htNum.put(invid, onhandnum);
      }
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    UFDouble[] nums = new UFDouble[invmanids.length];
    for (int i = 0; i < invmanids.length; i++) {
      if (htNum.containsKey(invmanids[i]))
        nums[i] = ((UFDouble)htNum.get(invmanids[i]));
      else {
        nums[i] = ufd0;
      }

    }

    afterCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_calbody, cwarehouseid, invmanids });

    return nums;
  }

  public UFDouble[] getOnhandNums(String pk_corp, String pk_calbody, String[] invmanids)
    throws BusinessException
  {
    return getOnhandNums(pk_corp, pk_calbody, null, invmanids);
  }

  public HashMap getOnHandNumsByInvbasID(String cinvbasid)
    throws SQLException
  {
    if (cinvbasid == null) {
      return new HashMap();
    }
    HashMap ht = new HashMap();
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT cwarehouseid,SUM(COALESCE(v1.nonhandnum,0.0)) from " + GeneralSqlString.V_IC_ONHANDNUM + " v1 ,bd_invmandoc invman where v1.cinventoryid=invman.pk_invmandoc and invman.pk_invbasdoc=? group by cwarehouseid ");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, cinvbasid);
      rs = stmt.executeQuery();
      String id = null;
      BigDecimal num = null;

      while (rs.next()) {
        id = rs.getString(1);
        num = rs.getBigDecimal(2);
        ht.put(id, num == null ? null : new UFDouble(num));
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    return ht;
  }

  public UFDouble[] getOrderOnRouteNum(String pk_corp, String pk_calbody, String[] invclid, String[] invmanid, Integer iMode)
    throws BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || ((invclid == null) && (invmanid == null)) || (iMode == null))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000132"));
    StringBuffer sbSQL = null;
    StringBuffer sbWhere = null;
    boolean bInvCl = false;
    StringBuffer sbInvcl = null;

    sbWhere = new StringBuffer();
    sbWhere.append(" Where pk_corp = '" + pk_corp + "' ").append(" And pk_calbody = '" + pk_calbody + "' ");
    if (invmanid != null)
    {
      String sInvWhere = "";

      for (int i = 0; i < invmanid.length; i++)
      {
        if (0 == i)
        {
          sInvWhere = "And cinventoryid in ('" + invmanid[i] + "'";
        }
        else
        {
          sInvWhere = sInvWhere + ", '" + invmanid[i] + "'";
          continue;
        }

        if (invmanid.length - 1 != i)
          continue;
        sInvWhere = sInvWhere + " )";
        break;
      }

      sbWhere.append(sInvWhere);
    }

    if (invmanid == null)
    {
      bInvCl = true;
    }

    if (iMode.intValue() == 1)
    {
      sbSQL = new StringBuffer();
      sbSQL.append(" (select ISNULL(ntotalinventorynumber, 0) - ISNULL(naccumstorenum, 0) AS nordernum,purchasecorp as pk_corp,cstoreorganization  AS pk_calbody ,cinventoryid ").append(" FROM  v_so_po_ic_sotoic h )").append(" UNION ALL ").append(" (select ISNULL(ntotalinventorynumber, 0) - ISNULL(naccumstorenum, 0) AS nordernum,purchasecorp as pk_corp, cstoreorganization  AS pk_calbody ,cinventoryid").append("  FROM  v_po_so_ic_potoic h)").append(" UNION ALL ").append(" (select ISNULL(noutnum, 0) - ISNULL(ninnum, 0) AS nordernum ,h.pk_corp,h.cincalbodyid as pk_calbody, b.cinventoryid ").append("  FROM  ic_allocation_h h inner join ic_allocation_b b on(h.callocationhid = b.callocationhid ))");
    }
    else
    {
      sbSQL = new StringBuffer();
      sbSQL.append(" (select ISNULL(ntotalinventorynumber, 0) - ISNULL(naccumstorenum, 0) AS nordernum,salecorp as pk_corp,ccalbodyid   AS pk_calbody ,cinventoryid ").append(" FROM  v_so_po_ic_sotoic h )").append(" UNION ALL ").append(" (select ISNULL(ntotalinventorynumber, 0) - ISNULL(naccumstorenum, 0) AS nordernum,salecorp as pk_corp, ccalbodyid   AS pk_calbody ,cinventoryid").append("  FROM  v_po_so_ic_potoic h)").append(" UNION ALL ").append(" (select ISNULL(noutnum, 0) - ISNULL(ninnum, 0) AS nordernum ,h.pk_corp,h.coutcalbodyid as pk_calbody , b.cinventoryid ").append("  FROM  ic_allocation_h h inner join ic_allocation_b b on(h.callocationhid = b.callocationhid ))");
    }

    Connection con = null;
    PreparedStatement stmt = null;

    UFDouble[] ufOnRoutenum = null;
    StringBuffer sql = new StringBuffer();
    UFDouble ufNum = null;
    ArrayList alReturn = null;
    try
    {
      con = getConnection();
      if (!bInvCl)
      {
        sql.append(" Select sum(nordernum) from (").append(sbSQL).append(" ) aa ").append(sbWhere);
        stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();

        alReturn = new ArrayList();
        while (rs.next())
        {
          Object restnum = rs.getObject(1);
          ufNum = restnum == null ? new UFDouble(0) : new UFDouble(restnum.toString().trim());
          alReturn.add(ufNum);
        }

      }
      else if (bInvCl)
      {
        alReturn = new ArrayList();
        for (int i = 0; i < invclid.length; i++)
        {
          sbInvcl = new StringBuffer(" And cinventoryid in(select pk_invmandoc from bd_invmandoc ").append(" where pk_invbasdoc in (select pk_invbasdoc ").append(" from bd_invbasdoc\twhere pk_invcl ='" + invclid[i] + "' ))");

          sql.append(" Select * from (").append(sbSQL).append(" ) aa ").append(sbWhere).append(sbInvcl);

          stmt = con.prepareStatement(sbSQL.toString());
          ResultSet rs = stmt.executeQuery();

          while (rs.next())
          {
            Object restnum = rs.getObject(1);
            ufNum = restnum == null ? new UFDouble(0) : new UFDouble(restnum.toString().trim());
          }

          alReturn.add(ufNum);
        }
      }

    }
    catch (Exception e)
    {
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

    afterCallMethod("nc.bs.ic.ic604.OnRouteDMO", "getOrderOnRouteNum", new Object[] { ufOnRoutenum });

    if (alReturn != null)
    {
      ufOnRoutenum = new UFDouble[alReturn.size()];
      alReturn.toArray(ufOnRoutenum);
    }

    return ufOnRoutenum;
  }

  public UFDouble getOutNum(UFDate StartDay, UFDate EndDay, String PKCalbody, String InvID)
    throws SQLException
  {
    if ((StartDay == null) || (EndDay == null) || (PKCalbody == null) || (InvID == null)) {
      return null;
    }
    String sql = null;
    UFDouble ufd = null;

    sql = "select  sum(coalesce(noutnum,0.0)-coalesce(ninnum,0.0))  from ic_keep_detail1 b left outer join   bd_invmandoc m on b.cinventoryid = m.pk_invmandoc LEFT OUTER JOIN bd_invbasdoc d ON m.pk_invbasdoc=d.pk_invbasdoc    where  b.dbizdate >=? and b.dbizdate<=?  and (b.ninnum<0 or b.noutnum>0) and b.ccalbodyid=? and b.cinventoryid=? group by b.cinventoryid";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, StartDay.toString());
      stmt.setString(2, EndDay.toString());
      stmt.setString(3, PKCalbody);
      stmt.setString(4, InvID);

      rs = stmt.executeQuery();

      if (rs.next())
      {
        BigDecimal num = rs.getBigDecimal(1);
        ufd = num == null ? null : new UFDouble(num);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    return ufd;
  }

  private Hashtable getOutNumByInvDays(String pk_corp, String pk_calbody, String[] sInvBasIds, String sDateStart, String sDateEnd)
    throws SQLException
  {
    Hashtable ht = new Hashtable();
    StringBuffer sWhere = new StringBuffer(" and (0=0  ");
    for (int i = 0; i < sInvBasIds.length; i++) {
      sWhere.append(" or d.pk_invbasdoc='" + sInvBasIds[i] + "'");
    }
    sWhere.append(")");
    String sql = null;

    sql = "select d.pk_invbasdoc, (-sum(coalesce(ninnum,0.0))+sum(coalesce(noutnum,0.0))) from ic_keep_detail1 b   left outer JOIN bd_invmandoc m  on b.cinventoryid = m.pk_invmandoc left outer JOIN bd_invbasdoc d ON m.pk_invbasdoc=d.pk_invbasdoc where b.dbizdate >=? and b.dbizdate<=?  and (b.ninnum<0 or b.noutnum>0) and b.ccalbodyid=? and b.pk_corp=?  " + sWhere.toString() + " group by d.pk_invbasdoc";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, sDateStart);
      stmt.setString(2, sDateEnd);
      stmt.setString(3, pk_calbody);
      stmt.setString(4, pk_corp);

      rs = stmt.executeQuery();

      ht = new Hashtable();
      while (rs.next()) {
        String key = rs.getString(1);
        BigDecimal num = rs.getBigDecimal(2);
        if (key != null)
          ht.put(key.trim(), new UFDouble(num));
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    return ht;
  }

  public UFDouble getPreInNum(String pk_corp, String pk_calbody, String pk_invmandoc, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    UFDouble[] nums = getPreInNum(pk_corp, pk_calbody, new String[] { pk_invmandoc }, startdate, enddate);

    if (nums != null) {
      return nums[0];
    }
    return new UFDouble(0);
  }

  public ArrayList getPreNumByWH(String pk_corp, String pk_calbody, String cwarehouseid, String[] invmanids, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (cwarehouseid == null) || (invmanids == null) || (invmanids.length == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000133"));
    }

    StringBuffer sWhere = new StringBuffer();
    sWhere.append("  cwarehouseid='" + cwarehouseid + "' ");
    sWhere.append(GeneralSqlString.formInSQL("cinventoryid", invmanids));

    UFDouble[] ufdRet = { new UFDouble(0), new UFDouble(0) };
    HashMap hm = new HashMap();
    try
    {
      String key = null;

      InvATPDMO dmo = new InvATPDMO();
      ATPVO[] vos = dmo.queryAtpNum(pk_corp, pk_calbody, startdate, enddate, new UFBoolean(false), new UFBoolean(false), new UFBoolean(true), new UFBoolean(false), sWhere.toString());
      if ((vos != null) && (vos.length > 0))
      {
        for (int i = 0; i < vos.length; i++) {
          hm.put(vos[i].getCinventoryid(), new UFDouble[] { vos[i].calculatePreIn(), vos[i].calculatePreOut() });
        }
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }

    ArrayList alRet = new ArrayList();
    for (int i = 0; i < invmanids.length; i++) {
      if (hm.containsKey(invmanids[i]))
        alRet.add(hm.get(invmanids[i]));
      else {
        alRet.add(ufdRet);
      }
    }

    return alRet;
  }

  public UFDouble getPreInNum(String pk_corp, String pk_calbody, String invmanid, String[] vfrees, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (invmanid == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000133"));
    }

    StringBuffer sWhere = new StringBuffer();
    sWhere.append(" cinventoryid ='" + invmanid + "'");
    if (vfrees != null) {
      for (int i = 0; i < vfrees.length; i++) {
        if (vfrees[i] != null)
          sWhere.append(" and vfree" + String.valueOf(i + 1) + "='" + vfrees[i] + "'");
        else {
          sWhere.append(" and vfree" + String.valueOf(i + 1) + " is null ");
        }
      }
    }
    else {
      for (int i = 0; i < vfrees.length; i++) {
        sWhere.append(" and vfree" + String.valueOf(i + 1) + " is null ");
      }
    }

    UFDouble ufdRet = new UFDouble(0);
    try
    {
      String key = null;
      Hashtable ht = new Hashtable();
      InvATPDMO dmo = new InvATPDMO();
      ATPVO[] vos = dmo.queryAtpNum(pk_corp, pk_calbody, startdate, enddate, new UFBoolean(true), new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), sWhere.toString());
      if ((vos != null) && (vos.length > 0) && (vos[0] != null))
        ufdRet = vos[0].calculatePreIn();
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }

    return ufdRet;
  }

  public UFDouble getPreInNum(String pk_corp, String pk_calbody, String pk_invmandoc, UFBoolean bisFree, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5, String vfree6, String vfree7, String vfree8, String vfree9, String vfree10, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (pk_invmandoc == null))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000133"));
    }
    if (bisFree.booleanValue()) {
      return getPreInNum(pk_corp, pk_calbody, pk_invmandoc, new String[] { vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10 }, startdate, enddate);
    }
    return getPreInNum(pk_corp, pk_calbody, pk_invmandoc, startdate, enddate);
  }

  public UFDouble[] getPreInNum(String pk_corp, String pk_calbody, String[] invmanids, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (invmanids == null) || (invmanids.length == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000133"));
    }

    StringBuffer sWhere = new StringBuffer("  0=0 ");
    String inWhere = GeneralSqlString.formInSQL("cinventoryid", invmanids);
    sWhere.append(inWhere);

    UFDouble[] ufRets = new UFDouble[invmanids.length];
    try
    {
      String key = null;
      Hashtable ht = new Hashtable();
      InvATPDMO dmo = new InvATPDMO();

      ATPVO[] vos = dmo.queryAtpNum(pk_corp, pk_calbody, startdate, enddate, new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), sWhere.toString());
      if (vos != null)
      {
        for (int i = 0; i < vos.length; i++)
        {
          key = vos[i].getPk_corp() + vos[i].getCcalbodyid() + vos[i].getCinventoryid();
          ht.put(key, vos[i]);
        }
      }

      UFDouble ufd0 = new UFDouble(0);
      for (int i = 0; i < invmanids.length; i++)
      {
        key = pk_corp + pk_calbody + invmanids[i];
        if (ht.containsKey(key))
        {
          ufRets[i] = ((ATPVO)ht.get(key)).calculatePreIn();
        }
        else
          ufRets[i] = ufd0;
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }

    return ufRets;
  }

  public UFDouble getPreOutNum(String pk_corp, String pk_calbody, String pk_invmandoc, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (pk_invmandoc == null))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000134"));
    }
    UFDouble[] nums = getPreOutNum(pk_corp, pk_calbody, new String[] { pk_invmandoc }, startdate, enddate);

    if (nums != null) {
      return nums[0];
    }
    return new UFDouble(0);
  }

  public UFDouble getPreOutNum(String pk_corp, String pk_calbody, String invmanid, String[] vfrees, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (invmanid == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000134"));
    }

    StringBuffer sWhere = new StringBuffer();
    sWhere.append(" cinventoryid ='" + invmanid + "'");
    if (vfrees != null) {
      for (int i = 0; i < vfrees.length; i++) {
        if (vfrees[i] != null)
          sWhere.append(" and vfree" + String.valueOf(i + 1) + "='" + vfrees[i] + "'");
        else {
          sWhere.append(" and vfree" + String.valueOf(i + 1) + " is null ");
        }
      }
    }
    else {
      for (int i = 0; i < vfrees.length; i++) {
        sWhere.append(" and vfree" + String.valueOf(i + 1) + " is null ");
      }
    }

    UFDouble ufdRet = new UFDouble(0);
    try
    {
      String key = null;
      Hashtable ht = new Hashtable();
      InvATPDMO dmo = new InvATPDMO();
      ATPVO[] vos = dmo.queryAtpNum(pk_corp, pk_calbody, startdate, enddate, new UFBoolean(true), new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), sWhere.toString());
      if ((vos != null) && (vos.length > 0) && (vos[0] != null))
        ufdRet = vos[0].calculatePreOut();
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }

    return ufdRet;
  }

  public UFDouble getPreOutNum(String pk_corp, String pk_calbody, String pk_invmandoc, UFBoolean bisFree, String vfree1, String vfree2, String vfree3, String vfree4, String vfree5, String vfree6, String vfree7, String vfree8, String vfree9, String vfree10, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (pk_invmandoc == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000134"));
    }
    if (bisFree.booleanValue()) {
      return getPreOutNum(pk_corp, pk_calbody, pk_invmandoc, new String[] { vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10 }, startdate, enddate);
    }
    return getPreOutNum(pk_corp, pk_calbody, pk_invmandoc, startdate, enddate);
  }

  public UFDouble[] getPreOutNum(String pk_corp, String pk_calbody, String[] invmanids, String startdate, String enddate)
    throws SQLException, BusinessException
  {
    if ((pk_corp == null) || (pk_calbody == null) || (invmanids == null) || (invmanids.length == 0))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000134"));
    }
    StringBuffer sWhere = new StringBuffer(" 0=0 ");
    String inWhere = GeneralSqlString.formInSQL("cinventoryid", invmanids);
    sWhere.append(inWhere);

    UFDouble[] ufRets = new UFDouble[invmanids.length];
    try
    {
      String key = null;
      Hashtable ht = new Hashtable();
      InvATPDMO dmo = new InvATPDMO();

      ATPVO[] vos = dmo.queryAtpNum(pk_corp, pk_calbody, startdate, enddate, new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), sWhere.toString());
      if (vos != null)
      {
        for (int i = 0; i < vos.length; i++)
        {
          key = vos[i].getPk_corp() + vos[i].getCcalbodyid() + vos[i].getCinventoryid();
          ht.put(key, vos[i]);
        }
      }

      UFDouble ufd0 = new UFDouble(0);
      for (int i = 0; i < invmanids.length; i++)
      {
        key = pk_corp + pk_calbody + invmanids[i];
        if (ht.containsKey(key))
        {
          ufRets[i] = ((ATPVO)ht.get(key)).calculatePreOut();
        }
        else
          ufRets[i] = ufd0;
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }

    return ufRets;
  }

  public UFDouble[] getStorageCurNum(String pk_corp, String pk_calbody, String[] invbasids)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_calbody, invbasids });

    if ((pk_corp == null) || (pk_calbody == null) || (invbasids == null))
      return null;
    StringBuffer sql = new StringBuffer();

    sql.append("SELECT m.pk_invbasdoc ,SUM(COALESCE(nonhandnum,0.0)) from  " + GeneralSqlString.V_IC_ONHANDNUM + "  v1 left outer join bd_invmandoc m on v1.cinventoryid=m.pk_invmandoc where v1.pk_corp=? and v1.ccalbodyid=? and (1<0  ");

    for (int i = 0; i < invbasids.length; i++) {
      sql.append(" or m.pk_invbasdoc='" + invbasids[i] + "'");
    }

    sql.append(") group by m.pk_invbasdoc ");
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    UFDouble onhandnum = null;
    UFDouble ufd0 = new UFDouble(0);
    Hashtable htNum = new Hashtable();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);

      stmt.setString(2, pk_calbody);

      rs = stmt.executeQuery();

      while (rs.next()) {
        String invid = rs.getString(1);
        BigDecimal o = rs.getBigDecimal(2);
        onhandnum = o == null ? ufd0 : new UFDouble(o);
        if ((invid != null) && (onhandnum != null))
          htNum.put(invid, onhandnum);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    UFDouble[] nums = new UFDouble[invbasids.length];
    for (int i = 0; i < invbasids.length; i++) {
      if (htNum.containsKey(invbasids[i]))
        nums[i] = ((UFDouble)htNum.get(invbasids[i]));
      else {
        nums[i] = ufd0;
      }

    }

    afterCallMethod("nc.bs.ic.pub.InvOnHandDMO", "getOnHandNum", new Object[] { pk_corp, pk_calbody, invbasids });

    return nums;
  }

  public FreeVO queryFreeVO(String pk_invmandoc, String[] vfrees)
    throws Exception
  {
    if ((pk_invmandoc == null) || (vfrees == null) || (vfrees.length < 1))
      return null;
    FreeVO voFree = null;

    DefdefDMO defdmo = new DefdefDMO();
    voFree = defdmo.queryFreeVOByInvID(pk_invmandoc);

    if (voFree != null) {
      for (int f = 1; f <= vfrees.length; f++)
      {
        voFree.setAttributeValue("vfree" + f, vfrees[(f - 1)]);
      }

    }

    return voFree;
  }

  public String[] queryInvbasIds(String[] invmanids)
    throws SQLException
  {
    StringBuffer sql = new StringBuffer("select pk_invmandoc,pk_invbasdoc from bd_invmandoc where 0=0 ");

    sql.append(GeneralSqlString.formInSQL("pk_invmandoc", invmanids));

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Hashtable htid = new Hashtable();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      rs = stmt.executeQuery();

      while (rs.next())
      {
        String pk_invmandoc = rs.getString(1);
        String pk_invbasdoc = rs.getString(2);
        if ((pk_invmandoc != null) && (pk_invbasdoc != null))
          htid.put(pk_invmandoc.trim(), pk_invbasdoc.trim());
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    String[] invbasids = new String[invmanids.length];
    for (int i = 0; i < invmanids.length; i++) {
      if (htid.containsKey(invmanids[i]))
        invbasids[i] = ((String)htid.get(invmanids[i]));
      else {
        invbasids[i] = null;
      }
    }
    return invbasids;
  }

  public String[] queryInvClassCodes(String invclasscode, int iLevel)
    throws SQLException
  {
    StringBuffer sql = new StringBuffer("select invclasscode from bd_invcl where dr=0  and invclasslev=" + String.valueOf(iLevel));

    if ((invclasscode != null) && (invclasscode.trim().length() > 0)) {
      sql.append(" and invclasscode like '" + invclasscode + "%'");
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Vector v = new Vector();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      rs = stmt.executeQuery();
      String code = null;
      while (rs.next()) {
        code = rs.getString(1);

        if (code != null)
          v.addElement(code.trim());
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    String[] codes = null;
    if (v.size() > 0) {
      codes = new String[v.size()];
      v.copyInto(codes);
    }

    return codes;
  }

  private String[] queryInvIds(String pk_corp, String pk_invcl)
    throws SQLException
  {
    StringBuffer sql = new StringBuffer("select pk_invmandoc from bd_invmandoc LEFT OUTER join bd_invbasdoc on bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc left outer join bd_invcl on bd_invbasdoc.pk_invcl=bd_invcl.pk_invcl ,(select invclasscode,len(invclasscode) as ilevellength from bd_invcl where pk_invcl='");

    sql.append(pk_invcl);
    sql.append("' ) aa where substring(bd_invcl.invclasscode,1,aa.ilevellength)=aa.invclasscode and bd_invmandoc.pk_corp='");

    sql.append(pk_corp);
    sql.append("'");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Vector v = new Vector();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      rs = stmt.executeQuery();

      while (rs.next()) {
        String pk_invmandoc = rs.getString(1);

        if (pk_invmandoc != null)
          v.addElement(pk_invmandoc.trim());
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
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
    }
    String[] pks = null;
    if (v.size() > 0) {
      pks = new String[v.size()];
      v.copyInto(pks);
    }

    return pks;
  }

  public String[] queryInvmanIds(String pk_corp, String[] invbasids)
    throws SQLException
  {
    StringBuffer sql = new StringBuffer("select pk_invbasdoc,pk_invmandoc from bd_invmandoc where pk_corp=? and (1<0");

    for (int i = 0; i < invbasids.length; i++) {
      sql.append(" or pk_invbasdoc='" + invbasids[i] + "'");
    }
    sql.append(")");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Hashtable htid = new Hashtable();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);

      rs = stmt.executeQuery();

      while (rs.next()) {
        String pk_invbasdoc = rs.getString(1);
        String pk_invmandoc = rs.getString(2);
        if ((pk_invmandoc != null) && (pk_invbasdoc != null))
          htid.put(pk_invbasdoc.trim(), pk_invmandoc.trim());
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    String[] invmanids = new String[invbasids.length];
    for (int i = 0; i < invbasids.length; i++) {
      if (htid.containsKey(invbasids[i]))
        invmanids[i] = ((String)htid.get(invbasids[i]));
      else {
        invmanids[i] = null;
      }
    }
    return invmanids;
  }

  public int queryLengthOfInvClassLevel(int iLevel)
    throws SQLException
  {
    StringBuffer sql = new StringBuffer("select distinct len(invclasscode) from bd_invcl where  dr=0  and invclasslev=" + String.valueOf(iLevel));

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int length = 0;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      rs = stmt.executeQuery();
      String code = null;
      while (rs.next()) {
        length = rs.getInt(1);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    return length;
  }

  private void setItemData(ResultSet rs, CircularlyAccessibleValueObject voBillItem)
    throws Exception
  {
    Object oValue = null;
    ResultSetMetaData meta = rs.getMetaData();
    int totalcolnum = meta.getColumnCount();
    String sColumnName = null;

    for (int i = 1; i <= totalcolnum; i++)
    {
      sColumnName = meta.getColumnName(i).trim().toLowerCase();

      oValue = rs.getObject(sColumnName);

      voBillItem.setAttributeValue(sColumnName, oValue);
    }
  }

  public OnhandnumItemVO[] queryOnhandnumView6(GroupDefine gd, String swhere)
    throws BusinessException
  {
    String sqlNum = " sum(isnull(ninspacenum,0.0)-isnull(noutspacenum,0.0)) as nnum,sum(isnull(ninspaceassistnum,0.0)-isnull(noutspaceassistnum,0.0)) as nastnum ";

    ArrayList alSelect = new ArrayList();
    ArrayList alGroup = new ArrayList();

    if (gd.isGroupByCorp()) {
      alSelect.add(" pk_corp ");
      alGroup.add(" pk_corp ");
    }
    if (gd.isGroupByCalbody()) {
      alSelect.add(" ccalbodyid ");
      alGroup.add(" ccalbodyid ");
    }
    if (gd.isGroupByInv()) {
      alSelect.add(" cinventoryid ");
      alGroup.add(" cinventoryid ");
    }
    if (gd.isGroupByCastunit()) {
      alSelect.add(" castunitid ");
      alGroup.add(" castunitid ");
    }
    if (gd.isGroupByHsl()) {
      alSelect.add(" hsl ");
      alGroup.add(" hsl ");
    }
    if (gd.isGroupByBatchcode()) {
      alSelect.add(" vbatchcode ");
      alGroup.add(" vbatchcode ");
    }
    if (gd.isGroupByFreeItem()) {
      alSelect.add(" vfree1,vfree2,vfree3,vfree4,vfree5 ");
      alGroup.add(" vfree1,vfree2,vfree3,vfree4,vfree5 ");
    }
    if (gd.isGroupByVendor()) {
      alSelect.add(" cvendorid ");
      alGroup.add(" cvendorid ");
    }
    if (gd.isGroupBySpace()) {
      alSelect.add(" cspaceid ");
      alGroup.add(" cspaceid ");
    }

    StringBuffer select = new StringBuffer("");
    StringBuffer group = new StringBuffer("");

    for (int i = 0; i < alSelect.size(); i++) {
      if (i > 0) {
        select.append(",");
        group.append(",");
      }
      select.append(alSelect.get(i));
      group.append(alGroup.get(i));
    }
    StringBuffer sql = new StringBuffer("select ");
    sql.append(select);
    sql.append(sqlNum);
    sql.append(" from v_ic_onhandnum6 where 0=0 ");
    if (swhere != null)
      sql.append(" and ").append(swhere);
    sql.append(group);

    OnhandnumItemVO[] vos = null;
    try {
      ArrayList alRet = queryBySql(sql.toString(), OnhandnumItemVO.class.getClass());

      if ((alRet != null) && (alRet.size() > 0)) {
        vos = new OnhandnumItemVO[alRet.size()];
        alRet.toArray(vos);
      }
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }

    return vos;
  }

  private ArrayList queryBySql(String sql, Class aclass)
    throws Exception
  {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    ArrayList alRet = new ArrayList();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      rs = stmt.executeQuery();
      GenMethod gm = new GenMethod();
      ResultSetMetaData meta = rs.getMetaData();

      while (rs.next())
      {
        CircularlyAccessibleValueObject vo = (CircularlyAccessibleValueObject)NewObjectService.newInstance(aclass.getName());

        gm.setData(rs, vo, meta);
        alRet.add(vo);
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
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
    }
    return alRet;
  }
}