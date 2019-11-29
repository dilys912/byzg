package nc.bs.ic.pub.freeze;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.ic2a1.PickBillDMO;
import nc.bs.ic.mo.mo2010.PickmDMO;
import nc.bs.ic.pub.ModuleEnable;
import nc.bs.ic.pub.QueryInfoDMO;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.ic.pub.bill.ICLockBO;
import nc.bs.ic.pub.check.CheckDMO;
import nc.bs.ic.pub.vmi.ICSmartToolsDmo;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.scm.inter.MMHelper;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.itf.mm.scm.IMmToIc;
import nc.itf.so.service.ISOToIC_DRP;
import nc.ui.sp.pub.ShowMsgDlg;
import nc.vo.ic.ic2a1.OutBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.freeze.FreezeVO;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.ctrl.GenMsgCtrl;

public class FreezeDMO extends DataManageObject
{
  public FreezeDMO()
    throws NamingException, SystemException
  {
  }

  public FreezeDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public void checkOnHandNum(FreezeVO vo)
    throws BusinessException
  {
    StringBuffer sSql = new StringBuffer();
    StringBuffer sWhere = new StringBuffer(" where 0=0 ");
    StringBuffer sErr = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001439") + vo.getInvname() + " ");

    if (vo.getPk_corp() != null)
      sWhere.append(" and pk_corp='" + vo.getPk_corp() + "'");
    else {
      sWhere.append(" and pk_corp is null ");
    }

    if (vo.getCwarehouseid() != null)
      sWhere.append(" and cwarehouseid='" + vo.getCwarehouseid() + "'");
    else
      sWhere.append(" and cwarehouseid is null ");
    if (vo.getCinventoryid() != null)
      sWhere.append(" and cinventoryid='" + vo.getCinventoryid() + "'");
    else
      sWhere.append(" and cinventoryid is null ");
    if (vo.getCastunitid() != null) {
      sWhere.append(" and castunitid='" + vo.getCastunitid() + "'");
      sErr.append(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0003974") + vo.getCastunitname());
    }
    else
    {
      sWhere.append(" and castunitid is null ");
    }
    if (vo.getVbatchcode() != null) {
      sWhere.append(" and vbatchcode='" + vo.getVbatchcode() + "'");
      sErr.append(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0002060") + vo.getVbatchcode());
    }
    else
    {
      sWhere.append(" and vbatchcode is null ");
    }
    FreeVO voFreeItem = vo.getFreeItemVO();
    if ((voFreeItem != null) && (voFreeItem.getVfree0() != null)) {
      sErr.append(vo.getAttributeValue("vfree0"));

      if (voFreeItem.getVfree1() != null) {
        sWhere.append(" and vfree1='" + voFreeItem.getVfree1() + "'");
      }
      else
      {
        sWhere.append(" and vfree1 is null ");
      }
      if (voFreeItem.getVfree2() != null) {
        sWhere.append(" and vfree2='" + voFreeItem.getVfree2() + "'");
      }
      else
      {
        sWhere.append(" and vfree2 is null ");
      }
      if (voFreeItem.getVfree3() != null) {
        sWhere.append(" and vfree3='" + voFreeItem.getVfree3() + "'");
      }
      else
      {
        sWhere.append(" and vfree3 is null ");
      }
      if (voFreeItem.getVfree4() != null) {
        sWhere.append(" and vfree4='" + voFreeItem.getVfree4() + "'");
      }
      else
      {
        sWhere.append(" and vfree4 is null ");
      }
      if (voFreeItem.getVfree5() != null) {
        sWhere.append(" and vfree5='" + voFreeItem.getVfree5() + "'");
      }
      else
      {
        sWhere.append(" and vfree5 is null ");
      }
      if (voFreeItem.getVfree6() != null) {
        sWhere.append(" and vfree6='" + voFreeItem.getVfree6() + "'");
      }
      else
      {
        sWhere.append(" and vfree6 is null ");
      }
      if (voFreeItem.getVfree7() != null) {
        sWhere.append(" and vfree7='" + voFreeItem.getVfree7() + "'");
      }
      else
      {
        sWhere.append(" and vfree7 is null ");
      }
      if (voFreeItem.getVfree8() != null) {
        sWhere.append(" and vfree8='" + voFreeItem.getVfree8() + "'");
      }
      else
      {
        sWhere.append(" and vfree8 is null ");
      }
      if (voFreeItem.getVfree9() != null) {
        sWhere.append(" and vfree9='" + voFreeItem.getVfree9() + "'");
      }
      else
      {
        sWhere.append(" and vfree9 is null ");
      }
      if (voFreeItem.getVfree10() != null) {
        sWhere.append(" and vfree10='" + voFreeItem.getVfree10() + "'");
      }
      else
      {
        sWhere.append(" and vfree10 is null ");
      }
    }
    if (vo.getCspaceid() != null) {
      sWhere.append(" and cspaceid='" + vo.getCspaceid() + "'");
      sSql.append(" select sum(isnull(ninspacenum,0)-isnull(noutspacenum,0)) from v_ic_onhandnum2");

      sErr.append(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0003830") + vo.getCspacename());
    }
    else
    {
      sSql.append(" select sum(isnull(ninnum,0)-isnull(noutnum,0)) from v_ic_onhandnum1");
    }

    sSql.append(sWhere);

    UFDouble num = new UFDouble(0);
    UFDouble locknum = vo.getNfreezenum();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql.toString());

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        BigDecimal num1 = rs.getBigDecimal(1);
        num = num1 == null ? num : new UFDouble(num1);
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
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
    if (num.compareTo(locknum) < 0)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000022") + sErr.toString() + NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000023") + num.toString() + NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000024") + locknum.toString());
  }

  public String freeze(FreezeVO voFreeze)
    throws Exception
  {
    if ((voFreeze == null) || (voFreeze.getCwarehouseid() == null)) {
      SCMEnv.out("freeze param null");
      return null;
    }

    String sPK = null;
    try {
      if (busiCheck(voFreeze))
        sPK = insertFreeze(voFreeze);
      else {
        throw new BusinessException(voFreeze.getInvname() + NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000025"));
      }

    }
    catch (Exception e)
    {
      throw nc.bs.ic.pub.GenMethod.handleException(null, e);
    }

    return sPK;
  }

  public String insertFreeze(FreezeVO freeze)
    throws SQLException, BusinessException
  {
    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    SmartDMO dmo = null;

    key = getOID();
    freeze.setCfreezeid(key);
    try
    {
      dealFreezeVO(freeze);

      freeze.setCthawpersonid(null);
      freeze.setDthawdate(null);
      if (freeze.getBfrzhandflag() == null) {
        freeze.setBfrzhandflag("Y");
      }

      dmo = new SmartDMO();
      dmo.maintain(freeze);

      String[] snids = freeze.getSerials();
      if ((snids != null) && (key != null)) {
        String sqlsn = "select cserialid,vserialcode from ic_general_bb2 where dr=0 " + GeneralSqlString.formInSQL("cserialid", snids);

        con = getConnection();
        stmt = con.prepareStatement(sqlsn);
        ResultSet rs = stmt.executeQuery();
        HashMap ht = new HashMap();
        String id = null;
        String code = null;
        while (rs.next()) {
          id = rs.getString(1);
          code = rs.getString(2);
          if (id != null)
            ht.put(id, code);
        }
        stmt.close();
        String snsql = null;
        ArrayList aloutsn = new ArrayList();
        for (int i = 0; i < snids.length; i++) {
          snsql = "update ic_general_bb2 set cfreezeid='" + key + "' where  cserialid=? and coutbillbodyid is null ";

          stmt = con.prepareStatement(snsql);
          stmt.setString(1, snids[i]);
          if (stmt.executeUpdate() == 0) {
            aloutsn.add(ht.get(snids[i]));
          }
        }
        if (aloutsn.size() > 0) {
          StringBuffer msg = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000026"));

          for (int k = 0; k < aloutsn.size(); k++)
            msg.append(aloutsn.get(k) + ",");
          throw new SQLException(msg.toString());
        }
      }
    }
    catch (Exception e) {
      throw nc.bs.ic.pub.GenMethod.handleException(null, e);
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
    return key;
  }

  public FreezeVO[] qryFreezedRecordByVO(FreezeVO condFreezeVO)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "queryByVO", new Object[] { condFreezeVO });

    String strSql = "select cfreezeid,nfreezenum from ic_freeze ";
    StringBuffer sbConditionNames = new StringBuffer();
    if (condFreezeVO.getPk_corp() != null) {
      sbConditionNames.append("  AND      pk_corp='" + condFreezeVO.getPk_corp() + "' ");
    }

    if (condFreezeVO.getCwarehouseid() != null) {
      sbConditionNames.append("  AND      cwarehouseid='" + condFreezeVO.getCwarehouseid() + "' ");
    }

    if (condFreezeVO.getCspaceid() != null) {
      sbConditionNames.append("  AND      cspaceid='" + condFreezeVO.getCspaceid() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      cspaceid IS NULL ");
    }
    if (condFreezeVO.getCinventoryid() != null) {
      sbConditionNames.append("  AND      cinventoryid='" + condFreezeVO.getCinventoryid() + "' ");
    }

    if (condFreezeVO.getVbatchcode() != null) {
      sbConditionNames.append("  AND      vbatchcode='" + condFreezeVO.getVbatchcode() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vbatchcode IS NULL ");
    }if (condFreezeVO.getCastunitid() != null) {
      sbConditionNames.append("  AND      castunitid='" + condFreezeVO.getCastunitid() + "' ");
    }

    FreeVO voFreeItem = condFreezeVO.getFreeItemVO();
    if (voFreeItem == null) {
      voFreeItem = new FreeVO();
    }
    if (voFreeItem.getVfree1() != null) {
      sbConditionNames.append("  AND      vfree1='" + voFreeItem.getVfree1() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree1 IS NULL");
    }if (voFreeItem.getVfree2() != null) {
      sbConditionNames.append("  AND      vfree2='" + voFreeItem.getVfree2() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree2 IS NULL");
    }if (voFreeItem.getVfree3() != null) {
      sbConditionNames.append("  AND      vfree3='" + voFreeItem.getVfree3() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree3 IS NULL");
    }if (voFreeItem.getVfree4() != null) {
      sbConditionNames.append("  AND      vfree4='" + voFreeItem.getVfree4() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree4 IS NULL");
    }if (voFreeItem.getVfree5() != null) {
      sbConditionNames.append("  AND      vfree5='" + voFreeItem.getVfree5() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree5 IS NULL");
    }if (voFreeItem.getVfree10() != null) {
      sbConditionNames.append("  AND      vfree10='" + voFreeItem.getVfree10() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree10 IS NULL");
    }if (voFreeItem.getVfree9() != null) {
      sbConditionNames.append("  AND      vfree9='" + voFreeItem.getVfree9() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree9 IS NULL");
    }if (voFreeItem.getVfree8() != null) {
      sbConditionNames.append("  AND      vfree8='" + voFreeItem.getVfree8() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree8 IS NULL");
    }if (voFreeItem.getVfree7() != null) {
      sbConditionNames.append("  AND      vfree7='" + voFreeItem.getVfree7() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree7 IS NULL");
    }if (voFreeItem.getVfree6() != null) {
      sbConditionNames.append("  AND      vfree6='" + voFreeItem.getVfree6() + "' ");
    }
    else
    {
      sbConditionNames.append("  AND      vfree6 IS NULL");
    }
    sbConditionNames.append("  AND      cfreezerid IS NOT NULL  AND cthawpersonid IS NULL ");

    String strConditionNames = sbConditionNames.toString().trim();

    if (strConditionNames.length() > 0) {
      strConditionNames = strConditionNames.trim().substring(3, strConditionNames.length());
    }

    strSql = strSql + " where " + strConditionNames;

    FreezeVO[] freezes = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        FreezeVO freeze = new FreezeVO();

        String cfreezeid = rs.getString(1);
        freeze.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        BigDecimal nfreezenum = rs.getBigDecimal(2);
        freeze.setNfreezenum(nfreezenum == null ? null : new UFDouble(nfreezenum));

        v.addElement(freeze);
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
    freezes = new FreezeVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(freezes);
    }

    afterCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "queryByVO", new Object[] { condFreezeVO });

    return freezes;
  }

  public ArrayList qryUnlockable(String sCurDate)
    throws SQLException
  {
    ArrayList al = new ArrayList();
    if (sCurDate == null) {
      return al;
    }
    String strSql = "select cfreezeid from ic_freeze where  dunlockdate<? and cfreezerid IS NOT NULL  AND cthawpersonid IS NULL and dr=0 ";

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);
      stmt.setString(1, sCurDate);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String cfreezeid = rs.getString(1);
        al.add(cfreezeid);
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    return al;
  }

  public FreezeVO[] qryRecordByVO(FreezeVO condFreezeVO)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "queryByVO", new Object[] { condFreezeVO });

    String strSql = "select cfreezeid,nfreezenum from ic_freeze ";
    StringBuffer sbConditionNames = new StringBuffer();
    if (condFreezeVO.getPk_corp() != null) {
      sbConditionNames.append("AND  pk_corp='" + condFreezeVO.getPk_corp() + "' ");
    }

    if (condFreezeVO.getCwarehouseid() != null) {
      sbConditionNames.append("AND  cwarehouseid='" + condFreezeVO.getCwarehouseid() + "' ");
    }

    if (condFreezeVO.getCspaceid() != null) {
      sbConditionNames.append("AND  cspaceid='" + condFreezeVO.getCspaceid() + "' ");
    }

    if (condFreezeVO.getCinventoryid() != null) {
      sbConditionNames.append("AND  cinventoryid='" + condFreezeVO.getCinventoryid() + "' ");
    }

    if (condFreezeVO.getCastunitid() != null) {
      sbConditionNames.append("AND  castunitid='" + condFreezeVO.getCastunitid() + "' ");
    }

    if (condFreezeVO.getVbatchcode() != null) {
      sbConditionNames.append("AND  vbatchcode='" + condFreezeVO.getVbatchcode() + "' ");
    }

    FreeVO voFreeItem = condFreezeVO.getFreeItemVO();
    if (voFreeItem == null) {
      voFreeItem = new FreeVO();
    }
    if (voFreeItem.getVfree1() != null) {
      sbConditionNames.append("AND  vfree1='" + voFreeItem.getVfree1() + "' ");
    }
    if (voFreeItem.getVfree2() != null) {
      sbConditionNames.append("AND  vfree2='" + voFreeItem.getVfree2() + "' ");
    }
    if (voFreeItem.getVfree3() != null) {
      sbConditionNames.append("AND  vfree3='" + voFreeItem.getVfree3() + "' ");
    }
    if (voFreeItem.getVfree4() != null) {
      sbConditionNames.append("AND  vfree4='" + voFreeItem.getVfree4() + "' ");
    }
    if (voFreeItem.getVfree5() != null) {
      sbConditionNames.append("AND  vfree5='" + voFreeItem.getVfree5() + "' ");
    }
    if (voFreeItem.getVfree10() != null) {
      sbConditionNames.append("AND  vfree10='" + voFreeItem.getVfree10() + "' ");
    }

    if (voFreeItem.getVfree9() != null) {
      sbConditionNames.append("AND  vfree9='" + voFreeItem.getVfree9() + "' ");
    }
    if (voFreeItem.getVfree8() != null) {
      sbConditionNames.append("AND  vfree8='" + voFreeItem.getVfree8() + "' ");
    }
    if (voFreeItem.getVfree7() != null) {
      sbConditionNames.append("AND  vfree7='" + voFreeItem.getVfree7() + "' ");
    }
    if (voFreeItem.getVfree6() != null) {
      sbConditionNames.append("AND  vfree6='" + voFreeItem.getVfree6() + "' ");
    }
    if (condFreezeVO.getCcorrespondbid() != null) {
      sbConditionNames.append("AND  ccorrespondbid='" + condFreezeVO.getCcorrespondbid() + "' ");
    }

    if (condFreezeVO.getCcorrespondhid() != null) {
      sbConditionNames.append("AND  ccorrespondhid='" + condFreezeVO.getCcorrespondhid() + "' ");
    }

    if (condFreezeVO.getCcorrespondtype() != null) {
      sbConditionNames.append("AND  ccorrespondtype='" + condFreezeVO.getCcorrespondtype() + "' ");
    }

    if (condFreezeVO.getCcorrespondcode() != null) {
      sbConditionNames.append("AND  ccorrespondcode='" + condFreezeVO.getCcorrespondcode() + "' ");
    }

    if (condFreezeVO.getCfreezerid() != null) {
      sbConditionNames.append("AND  cfreezerid='" + condFreezeVO.getCfreezerid() + "' ");
    }

    if (condFreezeVO.getDtfreezetime() != null) {
      sbConditionNames.append("AND  dtfreezetime='" + condFreezeVO.getDtfreezetime() + "' ");
    }

    if (condFreezeVO.getCthawpersonid() != null) {
      sbConditionNames.append("AND  cthawpersonid='" + condFreezeVO.getCthawpersonid() + "' ");
    }

    if (condFreezeVO.getDthawdate() != null) {
      sbConditionNames.append("AND  dthawdate='" + condFreezeVO.getDthawdate() + "' ");
    }

    String strConditionNames = sbConditionNames.toString().trim();

    if (strConditionNames.length() > 0) {
      strConditionNames = strConditionNames.substring(3, strConditionNames.length() - 1);
    }

    strSql = strSql + " where " + strConditionNames;

    FreezeVO[] freezes = null;
    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(strSql);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        FreezeVO freeze = new FreezeVO();

        String cfreezeid = rs.getString(1);
        freeze.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());

        BigDecimal nfreezenum = rs.getBigDecimal(2);
        freeze.setNfreezenum(nfreezenum == null ? null : new UFDouble(nfreezenum));

        v.addElement(freeze);
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
    freezes = new FreezeVO[v.size()];
    if (v.size() > 0) {
      v.copyInto(freezes);
    }

    afterCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "queryByVO", new Object[] { condFreezeVO });

    return freezes;
  }

  public ArrayList query(QryConditionVO voCond)
    throws Exception
  {
    if ((voCond == null) || (voCond.getParam(1) == null) || (voCond.getParam(1).toString().trim().length() == 0))
    {
      SCMEnv.out("cond para null.");
      return null;
    }

    String sCorpID = voCond.getStrParam(0);

    String sWhID = null;
    if (voCond.getParam(0) != null) {
      sWhID = voCond.getParam(0).toString();
    }
    String sWhName = null;

    String sIsLocMgt = null;

    String sQryType = "FREEZE";
    if (voCond.getParam(1) != null) {
      sQryType = voCond.getParam(1).toString().trim().toUpperCase();
    }
    FreezeVO[] voaFreeze = null;
    Vector vAllData = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;

    String sPk_calbody = null;
    try {
      con = getConnection();
      ResultSet rs = null;

      if ("FREEZE".equals(sQryType))
      {
        stmt = con.prepareStatement("SELECT storname,csflag,pk_calbody FROM bd_stordoc WHERE pk_stordoc='" + sWhID + "'");

        rs = stmt.executeQuery();
        if (rs.next()) {
          sWhName = rs.getString(1);
          sIsLocMgt = rs.getString(2);
          sPk_calbody = rs.getString(3);
        }

        if (stmt != null) {
          stmt.close();
        }
      }
      StringBuffer sbSql = new StringBuffer();
      if ("FREEZE".equals(sQryType)) {
        sbSql.append("SELECT * FROM (");
        sbSql.append("SELECT wh.pk_stordoc as cwarehouseid,wh.storcode,bq.pk_corp,bq.cinventoryid,bq.cinvbasid,inv.invcode, inv.invname,inv.invspec,inv.invtype, meas1.measname AS measdocname,    conv102.fixedflag AS issolidconvrate, meas2.measname AS castunitname, bq.castunitid, bq.vbatchcode,bq.dvalidate,bq.cvendorid, bq.vfree1, bq.vfree2,       bq.vfree3, bq.vfree4, bq.vfree5, bq.vfree6, bq.vfree7, bq.vfree8, bq.vfree9, bq.vfree10,           bq.nonhandqty AS nonhandnum , bq.nonhandastqty AS nonhandastnum ,bq.ngross, inv.isstorebyconvert, case when inv.isstorebyconvert='N' and conv102.fixedflag='Y' then conv102.mainmeasrate else hsl end as hsl ");

        if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y"))) {
          sbSql.append(", bq.cspaceid,loc.cscode AS cspacecode, loc.csname AS cspacename ");
        }
        sbSql.append("  FROM (SELECT pk_corp,cwarehouseid, cinventoryid,cinvbasid, vbatchcode,dvalidate,cvendorid, castunitid,");

        if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y")))
          sbSql.append(" cspaceid, ");
        sbSql.append("vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10,   ");

        if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y"))) {
          sbSql.append(" SUM(COALESCE (ninspacenum, 0.0) - COALESCE (noutspacenum, 0.0))   AS nonhandqty ,     SUM(COALESCE (ninspaceassistnum, 0.0) - COALESCE (noutspaceassistnum, 0.0))   AS nonhandastqty  ,SUM(COALESCE (ninspacegrossnum, 0.0)-COALESCE (noutspacegrossnum, 0.0)) as ngross, hsl ");
        }
        else {
          sbSql.append(" SUM(COALESCE (ninnum, 0.0) - COALESCE (noutnum, 0.0)) AS nonhandqty  ,\n SUM(COALESCE (ninassistnum, 0.0) - COALESCE (noutassistnum, 0.0)) AS nonhandastqty,SUM(COALESCE (ningrossnum, 0.0)-COALESCE (noutgrossnum, 0.0)) as ngross, hsl  ");
        }

        sbSql.append(" FROM ");

        if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y")))
          sbSql.append("v_ic_onhandnum2      ");
        else {
          sbSql.append("v_ic_onhandnum1      ");
        }
        sbSql.append("  GROUP BY pk_corp,cwarehouseid, cinventoryid,cinvbasid, vbatchcode,dvalidate,cvendorid, castunitid, ");

        if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y")))
          sbSql.append(" cspaceid, ");
        sbSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10, hsl )  bq  LEFT OUTER  JOIN      bd_invmandoc invman ON  bq.cinventoryid = invman.pk_invmandoc  LEFT OUTER JOIN      bd_invbasdoc inv  ON invman.pk_invbasdoc=inv.pk_invbasdoc    LEFT OUTER JOIN      bd_measdoc meas1 ON inv.pk_measdoc = meas1.pk_measdoc ");

        if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y"))) {
          sbSql.append(" LEFT OUTER JOIN      bd_cargdoc loc ON  bq.cspaceid=loc.pk_cargdoc   ");
        }
        sbSql.append(" LEFT OUTER JOIN     bd_measdoc meas2 ON       bq.castunitid = meas2.pk_measdoc     LEFT OUTER JOIN     " + GeneralSqlString.sBd_ConvertMainUom + " conv ON inv.pk_invbasdoc = conv.pk_invbasdoc LEFT OUTER JOIN     " + GeneralSqlString.sBd_Convert + " conv102 ON bq.castunitid=conv102.pk_measdoc  LEFT OUTER JOIN     bd_stordoc wh ON bq.cwarehouseid=wh.pk_stordoc ");

        sbSql.append("      WHERE  " + GeneralSqlString.leftOuterJoinConvert("conv", "conv102") + "  ");

        if ((sCorpID != null) && (sCorpID.trim().length() > 0)) {
          sbSql.append(" AND bq.pk_corp='");
          sbSql.append(sCorpID);
          sbSql.append("'");
        }

        if ((sWhID != null) && (sWhID.trim().length() > 0)) {
          sbSql.append(" AND bq.cwarehouseid='");
          sbSql.append(sWhID);
          sbSql.append("'");
        }
        sbSql.append("  ) AS freeze");

        if ((voCond.getQryCond() != null) && (voCond.getQryCond().trim().length() > 0))
        {
          sbSql.append("     WHERE ");
          sbSql.append(voCond.getQryCond());
        }

      }
      else
      {
        sbSql.append("SELECT cfreezeid, cwarehouseid, wh.storname AS cwarehousename,   conv102.fixedflag AS issolidconvrate, meas2.measname AS castunitname, f.castunitid,  case when inv.isstorebyconvert='N' and conv102.fixedflag='Y' then conv102.mainmeasrate else f.hsl end as hsl ,    cspaceid,   loc.cscode AS cspacecode, loc.csname AS cspacename, cinventoryid, inv.invcode,       inv.invname,inv.invspec,inv.invtype, meas1.measname AS measdocname, vbatchcode,dvalidate, cvendorid, vfree1,       vfree2, vfree3, vfree4, vfree5, vfree10, vfree9, vfree8, vfree7, vfree6, nfreezenum,           nfreezeastnum,ngrossnum , ccorrespondbid, ccorrespondhid, ccorrespondtype,bt.billtypename AS vcorrespondname,  ccorrespondcode,       cfreezerid, p1.user_name AS cfreezername, dtfreezetime, cthawpersonid,   p2.user_name AS cthawpersonname, dthawdate,dunlockdate,ndefrznum,ndefrzgrsnum,ndefrzastnum,bfrzhandflag, conv102.fixedflag AS issolidconvrate, inv.isstorebyconvert, f.ts  FROM ic_freeze f   LEFT OUTER JOIN bd_billtype bt ON f.ccorrespondtype = bt.pk_billtypecode  LEFT OUTER JOIN      sm_user p1 ON f.cfreezerid = p1.cUserID LEFT OUTER JOIN      sm_user p2 ON f.cthawpersonid = p2.cUserID  LEFT OUTER  JOIN      bd_invmandoc invman ON  f.cinventoryid = invman.pk_invmandoc  LEFT OUTER JOIN      bd_invbasdoc inv  ON invman.pk_invbasdoc=inv.pk_invbasdoc      LEFT OUTER JOIN      bd_measdoc meas1 ON inv.pk_measdoc = meas1.pk_measdoc     LEFT OUTER JOIN      bd_cargdoc loc ON f.cspaceid=loc.pk_cargdoc  LEFT OUTER JOIN      bd_stordoc wh ON  f.cwarehouseid=wh.pk_stordoc      LEFT OUTER JOIN      bd_measdoc meas2 ON f.castunitid = meas2.pk_measdoc LEFT OUTER JOIN      " + GeneralSqlString.sBd_ConvertMainUom + " conv ON inv.pk_invbasdoc = conv.pk_invbasdoc  LEFT OUTER JOIN     " + GeneralSqlString.sBd_Convert + " conv102 ON f.castunitid=conv102.pk_measdoc   WHERE " + GeneralSqlString.leftOuterJoinConvert("conv", "conv102") + " AND f.cfreezerid IS NOT NULL AND ");

        if ("UNFREEZE".equals(sQryType))
        {
          sbSql.append(" f.cthawpersonid IS NULL ");
        }
        else {
          sbSql.append(" f.cthawpersonid IS NOT NULL ");
        }

        if ((sCorpID != null) && (sCorpID.trim().length() > 0)) {
          sbSql.append(" AND f.pk_corp='");
          sbSql.append(sCorpID);
          sbSql.append("'");
        }

        if ((sWhID != null) && (sWhID.trim().length() > 0)) {
          sbSql.append(" AND f.cwarehouseid='");
          sbSql.append(sWhID);
          sbSql.append("' and ");
        }

        if ((voCond.getQryCond() != null) && (voCond.getQryCond().trim().length() > 0))
        {
          sbSql.append(voCond.getQryCond());
        }
      }
      SCMEnv.out("-------->" + sbSql.toString());
      GenMsgCtrl.printHint("\n" + sbSql.toString());

      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      while (rs.next()) {
        FreezeVO voFreeze = new FreezeVO();

        Object oValue = null;
        ResultSetMetaData meta = rs.getMetaData();

        String sColumnName = null;
        for (int i = 1; i <= meta.getColumnCount(); i++) {
          oValue = null;
          sColumnName = meta.getColumnName(i).trim().toLowerCase();

          oValue = rs.getObject(i);

          voFreeze.setFselected(new UFBoolean(false));

          if ((sColumnName.equals("nonhandastnum")) && (oValue != null) && (Double.valueOf(oValue.toString()).doubleValue() == 0.0D))
          {
            oValue = null;
          }voFreeze.setAttributeValue(sColumnName, oValue);

          if ("UNFREEZE".equals(sQryType)) {
            voFreeze.setAttributeValue("ndefrznum", rs.getObject("nfreezenum"));
            voFreeze.setAttributeValue("ndefrzastnum", rs.getObject("nfreezeastnum"));

            voFreeze.setAttributeValue("ndefrzgrsnum", rs.getObject("ngrossnum"));
          }

        }

        voFreeze.setCcalbodyid(sPk_calbody);

        if ("FREEZE".equals(sQryType));
        vAllData.addElement(voFreeze);
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    if (vAllData.size() > 0) {
      voaFreeze = new FreezeVO[vAllData.size()];
      vAllData.copyInto(voaFreeze);
      nc.bs.ic.pub.GenMethod method = new nc.bs.ic.pub.GenMethod();

      method.setFreeItemVO(voaFreeze);

      method.filterAstUnit(voaFreeze);
    }

    ArrayList alRet = new ArrayList();
    alRet.add(sWhName);
    alRet.add(voaFreeze);
    return alRet;
  }

  private OutBillItemVO getOutBillItemVoFromFreezeVo(FreezeVO vofreeze)
    throws Exception
  {
    OutBillItemVO voNewItem = new OutBillItemVO();
    voNewItem.setAttributeValue("crowno", "1");
    voNewItem.setAttributeValue("pk_corp", vofreeze.getPk_corp());
    voNewItem.setAttributeValue("ccalbodyid", vofreeze.getCcalbodyid());
    voNewItem.setAttributeValue("cwarehouseid", vofreeze.getCwarehouseid());
    voNewItem.setAttributeValue("cinventoryid", vofreeze.getCinventoryid());
    voNewItem.setAttributeValue("castunitid", vofreeze.getCastunitid());
    voNewItem.setAttributeValue("vbatchcode", vofreeze.getVbatchcode());
    voNewItem.setAttributeValue("vfree1", vofreeze.getVfree1());
    voNewItem.setAttributeValue("vfree2", vofreeze.getVfree2());
    voNewItem.setAttributeValue("vfree3", vofreeze.getVfree3());
    voNewItem.setAttributeValue("vfree4", vofreeze.getVfree4());
    voNewItem.setAttributeValue("vfree5", vofreeze.getVfree5());
    voNewItem.setAttributeValue("vfree6", vofreeze.getVfree6());
    voNewItem.setAttributeValue("vfree7", vofreeze.getVfree7());
    voNewItem.setAttributeValue("vfree8", vofreeze.getVfree8());
    voNewItem.setAttributeValue("vfree9", vofreeze.getVfree9());
    voNewItem.setAttributeValue("vfree10", vofreeze.getVfree10());
    voNewItem.setAttributeValue("cgeneralhid", "header");
    voNewItem.setAttributeValue("cgeneralbid", "item");
    voNewItem.setAttributeValue("cbilltypecode", "4I");
    voNewItem.setAttributeValue("vbillcode", "temp");

    voNewItem.setNshouldoutnum((UFDouble)vofreeze.getAttributeValue("nshouldnum"));

    voNewItem.setNshouldoutassistnum((UFDouble)vofreeze.getAttributeValue("nshouldastnum"));

    voNewItem.setCfirstbillbid(vofreeze.getCcorrespondbid());
    ArrayList alid = new ArrayList();
    alid.add(vofreeze.getCinventoryid());
    InvVO[] voInvs = new QueryInfoDMO().getInvInfo(alid);
    if ((voInvs != null) && (voInvs.length > 0))
      voNewItem.setAttributeValue("invvo", voInvs[0]);
    voNewItem.setCvendorid(vofreeze.getCvendorid());
    voNewItem.setHsl(vofreeze.getHsl());
    return voNewItem;
  }

  private FreezeVO[] getFreezeVoFromOutBillItemVo(OutBillItemVO[] vofreeze)
    throws Exception
  {
    if ((vofreeze == null) || (vofreeze.length == 0))
      return null;
    FreezeVO[] voNewItems = new FreezeVO[vofreeze.length];
    for (int i = 0; i < vofreeze.length; i++) {
      voNewItems[i] = new FreezeVO();

      voNewItems[i].setAttributeValue("pk_corp", vofreeze[i].getPk_corp());
      voNewItems[i].setAttributeValue("ccalbodyid", vofreeze[i].getCcalbodyid());

      voNewItems[i].setAttributeValue("cwarehouseid", vofreeze[i].getCwarehouseid());

      voNewItems[i].setAttributeValue("cinventoryid", vofreeze[i].getCinventoryid());

      voNewItems[i].setAttributeValue("castunitid", vofreeze[i].getCastunitid());

      voNewItems[i].setAttributeValue("vbatchcode", vofreeze[i].getVbatchcode());

      voNewItems[i].setAttributeValue("cspaceid", vofreeze[i].getAttributeValue("cspaceid"));

      voNewItems[i].setAttributeValue("vfree1", vofreeze[i].getAttributeValue("vfree1"));

      voNewItems[i].setAttributeValue("vfree2", vofreeze[i].getAttributeValue("vfree2"));

      voNewItems[i].setAttributeValue("vfree3", vofreeze[i].getAttributeValue("vfree3"));

      voNewItems[i].setAttributeValue("vfree4", vofreeze[i].getAttributeValue("vfree4"));

      voNewItems[i].setAttributeValue("vfree5", vofreeze[i].getAttributeValue("vfree5"));

      voNewItems[i].setAttributeValue("vfree6", vofreeze[i].getAttributeValue("vfree6"));

      voNewItems[i].setAttributeValue("vfree7", vofreeze[i].getAttributeValue("vfree7"));

      voNewItems[i].setAttributeValue("vfree8", vofreeze[i].getAttributeValue("vfree8"));

      voNewItems[i].setAttributeValue("vfree9", vofreeze[i].getAttributeValue("vfree9"));

      voNewItems[i].setAttributeValue("vfree10", vofreeze[i].getAttributeValue("vfree10"));

      voNewItems[i].setNonhandnum((UFDouble)vofreeze[i].getAttributeValue("nonhandnum"));

      voNewItems[i].setNonhandastnum((UFDouble)vofreeze[i].getAttributeValue("nonhandastnum"));

      voNewItems[i].setAttributeValue("nonhandgrossnum", vofreeze[i].getAttributeValue("ngrossnum"));

      voNewItems[i].setAttributeValue("dvalidate", vofreeze[i].getAttributeValue("dvalidate"));

      voNewItems[i].setCvendorid(vofreeze[i].getCvendorid());
      voNewItems[i].setHsl(vofreeze[i].getHsl());
    }
    return voNewItems;
  }

  public FreezeVO[] queryFreezableVOs(FreezeVO vofreeze)
    throws Exception
  {
    beforeCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "queryFreezable", new Object[] { vofreeze });

    if (vofreeze == null) {
      return null;
    }
    OutBillItemVO vobill = getOutBillItemVoFromFreezeVo(vofreeze);

    PickBillDMO dmo = new PickBillDMO();
    OutBillItemVO[] vos = dmo.queryOutableVOs(vobill);
    FreezeVO[] voFreezes = getFreezeVoFromOutBillItemVo(vos);

    nc.bs.ic.pub.GenMethod method = new nc.bs.ic.pub.GenMethod();

    method.setFreeItemVO(voFreezes);

    method.filterAstUnit(voFreezes);

    return voFreezes;
  }

  public FreezeVO[] queryFreezableVOs_old(FreezeVO vofreeze)
    throws Exception
  {
    beforeCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "queryFreezable", new Object[] { vofreeze });

    if (vofreeze == null) {
      return null;
    }
    String[] sField = { "pk_corp", "cwarehouseid", "cinventoryid", "castunitid", "vbatchcode", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9", "vfree10", "cvendorid" };

    StringBuffer sWhere = new StringBuffer(" 0=0 ");
    String sWhID = null;
    String sInvID = null;
    for (int i = 0; i < sField.length; i++) {
      Object oValue = vofreeze.getAttributeValue(sField[i]);
      if ((oValue != null) && (oValue.toString().trim().length() > 0)) {
        sWhere.append(" and " + sField[i] + "='" + oValue.toString() + "' ");
        if (sField[i].equals("cwarehouseid"))
          sWhID = oValue.toString();
        if (sField[i].equals("cinventoryid")) {
          sInvID = oValue.toString();
        }
      }

    }

    if (sWhID == null) {
      throw new BusinessException(" whid is null ");
    }
    String sIsLocMgt = "N";
    String sIsLotMgt = "N";
    String issupplierstock = "N";

    FreezeVO[] voaFreeze = null;
    Vector vAllData = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    int iOrder = 0;
    try {
      con = getConnection();
      ResultSet rs = null;

      stmt = con.prepareStatement("SELECT storname,csflag,serialmanaflag,wholemanaflag,outpriority,issupplierstock FROM bd_stordoc,bd_invmandoc WHERE  pk_stordoc='" + sWhID + "' and pk_invmandoc='" + sInvID + "'");

      rs = stmt.executeQuery();
      if (rs.next())
      {
        sIsLocMgt = rs.getString(2);

        sIsLotMgt = rs.getString(4);
        try {
          iOrder = rs.getInt(5);
        }
        catch (Exception e)
        {
        }
        issupplierstock = rs.getString(6);
        issupplierstock = issupplierstock == null ? "N" : issupplierstock;
      }

      if (stmt != null) {
        stmt.close();
      }

      StringBuffer sbSql = new StringBuffer();
      sbSql.append("SELECT wh.storcode,wh.storname as cwarehousename,bq.pk_corp,bq.cinventoryid,");

      if (issupplierstock.equals("Y")) {
        sbSql.append("bq.cvendorid,");
      }
      if ((sIsLotMgt != null) && (sIsLotMgt.toUpperCase().trim().equals("Y")) && 
        (iOrder != 2))
        sbSql.append("bq.dbizdate,");
      sbSql.append("\tinv.invcode, inv.invname,inv.invspec,inv.invtype, meas1.measname AS measdocname,    conv102.fixedflag AS issolidconvrate, meas2.measname AS castunitname, bq.castunitid, hsl,         bq.vbatchcode,bq.dvalidate, bq.vfree1, bq.vfree2,       bq.vfree3, bq.vfree4, bq.vfree5, bq.vfree6, bq.vfree7, bq.vfree8, bq.vfree9, bq.vfree10,           bq.nonhandqty AS nonhandnum , bq.nonhandastqty AS nonhandastnum,bq.nonhandgrossqty as nonhandgrossnum ");

      if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y"))) {
        sbSql.append(", bq.cspaceid,loc.cscode AS cspacecode, loc.csname AS cspacename ");
      }

      sbSql.append("  FROM (SELECT pk_corp,ccalbodyid,cwarehouseid, cinventoryid, ");

      sbSql.append(" castunitid, vbatchcode,dvalidate,");

      if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y")))
        sbSql.append(" cspaceid, ");
      sbSql.append("vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10,   ");

      if (issupplierstock.equals("Y")) {
        sbSql.append(" cvendorid, ");
      }
      if ((sIsLotMgt != null) && (sIsLotMgt.toUpperCase().trim().equals("Y")) && 
        (iOrder != 2)) {
        sbSql.append(" min(dbizdate) as dbizdate,");
      }

      if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y"))) {
        sbSql.append(" SUM(COALESCE (ninspacenum, 0.0) - COALESCE (noutspacenum, 0.0))   AS nonhandqty ,     SUM(COALESCE (ninspaceassistnum, 0.0) - COALESCE (noutspaceassistnum, 0.0))   AS nonhandastqty ,SUM(COALESCE (ninspacegrossnum, 0.0) - COALESCE (noutspacegrossnum, 0.0))   AS nonhandgrossqty ");
      }
      else {
        sbSql.append(" SUM(COALESCE (ninnum, 0.0) - COALESCE (noutnum, 0.0)) AS nonhandqty  ,\n SUM(COALESCE (ninassistnum, 0.0) - COALESCE (noutassistnum, 0.0)) AS nonhandastqty ,SUM(COALESCE (ningrossnum, 0.0) - COALESCE (noutgrossnum, 0.0)) AS nonhandgrossqty");
      }

      sbSql.append(" FROM ");

      if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y"))) {
        if (iOrder != 2)
          sbSql.append(" ic_keep_detail2      ");
        else {
          sbSql.append(" v_ic_onhandnum2      ");
        }
      }
      else if (iOrder != 2)
        sbSql.append(" ic_keep_detail1      ");
      else {
        sbSql.append(" v_ic_onhandnum1      ");
      }

      sbSql.append(" where " + sWhere.toString());
      sbSql.append("  GROUP BY pk_corp,ccalbodyid,cwarehouseid, cinventoryid, ");

      sbSql.append("\tcastunitid, vbatchcode,dvalidate,");

      if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y"))) {
        sbSql.append(" cspaceid, ");
      }
      if (issupplierstock.equals("Y")) {
        sbSql.append(" cvendorid, ");
      }
      sbSql.append(" vfree1, vfree2, vfree3, vfree4, vfree5, vfree6, vfree7, vfree8, vfree9, vfree10)  bq LEFT OUTER JOIN      ic_freeze f ON  bq.cwarehouseid=f.cwarehouseid  AND  bq.cinventoryid=f.cinventoryid  AND  bq.vbatchcode=f.vbatchcode  AND   bq.vfree1=f.vfree1 AND bq.vfree2=f.vfree2 AND bq.vfree3=f.vfree3 AND  bq.vfree4=f.vfree4 AND bq.vfree5=f.vfree5 AND bq.vfree6=f.vfree6 AND     bq.vfree7=f.vfree7 AND bq.vfree8=f.vfree8  AND bq.vfree9=f.vfree9  AND   bq.vfree10 = f.vfree10  LEFT OUTER JOIN      sm_user p1 ON f.cfreezerid = p1.cUserID     LEFT OUTER JOIN      sm_user p2 ON f.cthawpersonid = p2.cUserID  LEFT OUTER  JOIN      bd_invmandoc invman ON  bq.cinventoryid = invman.pk_invmandoc  LEFT OUTER JOIN      bd_invbasdoc inv  ON invman.pk_invbasdoc=inv.pk_invbasdoc    LEFT OUTER JOIN      bd_measdoc meas1 ON inv.pk_measdoc = meas1.pk_measdoc ");

      if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y"))) {
        sbSql.append(" LEFT OUTER JOIN      bd_cargdoc loc ON  bq.cspaceid=loc.pk_cargdoc   ");
      }
      sbSql.append(" LEFT OUTER JOIN     bd_measdoc meas2 ON       bq.castunitid = meas2.pk_measdoc     LEFT OUTER JOIN     " + GeneralSqlString.sBd_ConvertMainUom + " conv ON inv.pk_invbasdoc = conv.pk_invbasdoc LEFT OUTER JOIN     " + GeneralSqlString.sBd_Convert + " conv102 ON bq.castunitid=conv102.pk_measdoc  LEFT OUTER JOIN     bd_stordoc wh ON bq.cwarehouseid=wh.pk_stordoc ");

      sbSql.append("      WHERE  " + GeneralSqlString.leftOuterJoinConvert("conv", "conv102") + "  ");

      if ((sIsLotMgt != null) && (sIsLotMgt.toUpperCase().trim().equals("Y"))) {
        if (iOrder == 0)
          sbSql.append(" order by bq.dbizdate");
        else if (iOrder == 1)
          sbSql.append(" order by bq.dbizdate DESC");
        else {
          sbSql.append(" order by bq.dvalidate ");
        }

      }

      SCMEnv.out("-------->" + sbSql.toString());
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      while (rs.next()) {
        FreezeVO voFreeze = new FreezeVO();

        Object oValue = null;
        ResultSetMetaData meta = rs.getMetaData();

        String sColumnName = null;
        for (int i = 1; i <= meta.getColumnCount(); i++) {
          oValue = null;
          sColumnName = meta.getColumnName(i).trim().toLowerCase();

          oValue = rs.getObject(sColumnName);

          voFreeze.setFselected(new UFBoolean(false));

          if ((sColumnName.equals("nonhandastnum")) && (oValue != null) && (Double.valueOf(oValue.toString()).doubleValue() == 0.0D))
          {
            oValue = null;
          }voFreeze.setAttributeValue(sColumnName, oValue);
        }

        if (((UFDouble)voFreeze.getAttributeValue("nonhandnum")).doubleValue() > 0.0D)
          vAllData.addElement(voFreeze);
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    if (vAllData.size() > 0) {
      voaFreeze = new FreezeVO[vAllData.size()];
      vAllData.copyInto(voaFreeze);
      nc.bs.ic.pub.GenMethod method = new nc.bs.ic.pub.GenMethod();

      method.setFreeItemVO(voaFreeze);

      method.filterAstUnit(voaFreeze);
    }

    afterCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "queryFreezableVOs", new Object[] { vofreeze });

    return voaFreeze;
  }

  public FreezeVO[] queryFreezeInv(QryConditionVO voCond)
    throws Exception
  {
    beforeCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "query", new Object[] { voCond });

    FreezeVO[] voaFreeze = null;

    Vector vAllData = new Vector();
    StringBuffer sbSql = new StringBuffer("SELECT cfreezeid, f.pk_corp,ccalbodyid, cwarehouseid, wh.storname AS cwarehousename,   conv102.fixedflag AS issolidconvrate, meas2.measname AS castunitname, f.castunitid,  hsl,    cspaceid,   loc.cscode AS cspacecode, loc.csname AS cspacename, cinventoryid, inv.invcode,       inv.invname,inv.invspec,inv.invtype, meas1.measname AS measdocname, vbatchcode, dvalidate, vfree1,       vfree2, vfree3, vfree4, vfree5, vfree10, vfree9, vfree8, vfree7, vfree6, nfreezenum,           nfreezeastnum, ccorrespondbid, ccorrespondhid, ccorrespondtype, ccorrespondcode,       cfreezerid, p1.user_name AS cfreezername, dtfreezetime, cthawpersonid,   p2.user_name AS cthawpersonname, dthawdate,dunlockdate,ngrossnum,cvendorid FROM ic_freeze f LEFT OUTER JOIN      sm_user p1 ON f.cfreezerid = p1.cUserID LEFT OUTER JOIN      sm_user p2 ON f.cthawpersonid = p2.cUserID  LEFT OUTER  JOIN      bd_invmandoc invman ON  f.cinventoryid = invman.pk_invmandoc  LEFT OUTER JOIN      bd_invbasdoc inv  ON invman.pk_invbasdoc=inv.pk_invbasdoc      LEFT OUTER JOIN      bd_measdoc meas1 ON inv.pk_measdoc = meas1.pk_measdoc     LEFT OUTER JOIN      bd_cargdoc loc ON f.cspaceid=loc.pk_cargdoc  LEFT OUTER JOIN      bd_stordoc wh ON  f.cwarehouseid=wh.pk_stordoc      LEFT OUTER JOIN      bd_measdoc meas2 ON f.castunitid = meas2.pk_measdoc LEFT OUTER JOIN      " + GeneralSqlString.sBd_ConvertMainUom + " conv ON inv.pk_invbasdoc = conv.pk_invbasdoc  LEFT OUTER JOIN     " + GeneralSqlString.sBd_Convert + " conv102 ON f.castunitid=conv102.pk_measdoc   WHERE " + GeneralSqlString.leftOuterJoinConvert("conv", "conv102") + " AND f.cfreezerid IS NOT NULL AND ");

    sbSql.append(" f.cthawpersonid IS NULL ");

    if ((voCond != null) && (voCond.getQryCond() != null) && (voCond.getQryCond().trim().length() > 0))
    {
      sbSql.append("   and  " + voCond.getQryCond());
    }SCMEnv.out("-------->" + sbSql.toString());

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      ResultSet rs = null;
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      while (rs.next()) {
        FreezeVO voFreeze = new FreezeVO();

        Object oValue = null;
        ResultSetMetaData meta = rs.getMetaData();

        String sColumnName = null;
        for (int i = 1; i <= meta.getColumnCount(); i++) {
          oValue = null;
          sColumnName = meta.getColumnName(i).trim().toLowerCase();
          oValue = rs.getObject(sColumnName);

          voFreeze.setFselected(new UFBoolean(false));

          if ((sColumnName.equals("nonhandastnum")) && (oValue != null) && (Double.valueOf(oValue.toString()).doubleValue() == 0.0D))
          {
            oValue = null;
          }voFreeze.setAttributeValue(sColumnName, oValue);
        }

        vAllData.addElement(voFreeze);
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    if (vAllData.size() > 0) {
      voaFreeze = new FreezeVO[vAllData.size()];
      vAllData.copyInto(voaFreeze);
      nc.bs.ic.pub.GenMethod method = new nc.bs.ic.pub.GenMethod();

      method.setFreeItemVO(voaFreeze);

      method.filterAstUnit(voaFreeze);
    }

    afterCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "query", new Object[] { voCond });

    return voaFreeze;
  }

  public boolean busiCheck(FreezeVO voFreeze)
    throws Exception
  {
    boolean bIsPass = false;

    StringBuffer sSql = new StringBuffer();
    StringBuffer sWhere = new StringBuffer(" where 0=0 ");
    StringBuffer sErr = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001439") + voFreeze.getInvname() + " ");

    if (voFreeze.getPk_corp() != null)
      sWhere.append(" and pk_corp='" + voFreeze.getPk_corp() + "'");
    if (voFreeze.getCwarehouseid() != null)
      sWhere.append(" and cwarehouseid='" + voFreeze.getCwarehouseid() + "'");
    if (voFreeze.getCinventoryid() != null)
      sWhere.append(" and cinventoryid='" + voFreeze.getCinventoryid() + "'");
    if (voFreeze.getCastunitid() != null) {
      sWhere.append(" and castunitid='" + voFreeze.getCastunitid() + "'");
      sErr.append(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0003974") + voFreeze.getCastunitname());
    }

    if ((voFreeze.getHsl() != null) && (voFreeze.getIsstorebyconvert() != null) && (voFreeze.getIsstorebyconvert().booleanValue()))
    {
      sWhere.append(" and hsl= " + voFreeze.getHsl() + " ");
    }

    if (voFreeze.getVbatchcode() != null) {
      sWhere.append(" and vbatchcode='" + voFreeze.getVbatchcode() + "'");
      sErr.append(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0002060") + voFreeze.getVbatchcode());
    }

    if (voFreeze.getCvendorid() != null) {
      sWhere.append(" and cvendorid='" + voFreeze.getCvendorid() + "'");
      sErr.append(ResBase.getVendor() + voFreeze.getCvendorid());
    }

    FreeVO voFreeItem = voFreeze.getFreeItemVO();

    if ((voFreeItem != null) && (voFreeItem.getVfree0() != null)) {
      sErr.append(voFreeze.getAttributeValue("vfree0"));
      for (int i = 1; i <= 10; i++) {
        if (voFreeItem.getAttributeValue("vfree" + i) != null) {
          sWhere.append(" and vfree" + i + "='" + voFreeItem.getAttributeValue(new StringBuilder().append("vfree").append(i).toString()).toString().trim() + "'");
        }

      }

    }

    if (voFreeze.getCspaceid() != null) {
      sWhere.append(" and cspaceid='" + voFreeze.getCspaceid() + "'");
      sSql.append(" select sum(isnull(ninspacenum,0)-isnull(noutspacenum,0)) from v_ic_onhandnum2");

      sErr.append(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0003830") + voFreeze.getCspacename());
    }
    else
    {
      sSql.append(" select sum(isnull(ninnum,0)-isnull(noutnum,0)) from v_ic_onhandnum1");
    }

    sSql.append(sWhere);

    UFDouble num = null;
    UFDouble locknum = voFreeze.getNfreezenum();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql.toString());

      rs = stmt.executeQuery();

      if (rs.next()) {
        BigDecimal num1 = rs.getBigDecimal(1);
        num = num1 == null ? num : new UFDouble(num1);
      }
    }
    finally
    {
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

    if ((num == null) || (num.doubleValue() < locknum.doubleValue())) {
      bIsPass = false;
    }
    else
    {
      bIsPass = true;
    }

    return bIsPass;
  }

  public String[] queryFreezedSerial(String cfreezeid)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "queryFreezedSerial", new Object[] { cfreezeid });

    if (cfreezeid == null) {
      return null;
    }
    Connection con = null;
    PreparedStatement stmt = null;
    Vector v = new Vector();
    try
    {
      con = getConnection();
      ResultSet rs = null;
      stmt = con.prepareStatement("select vserialcode from ic_general_bb2 where cfreezeid=?");

      stmt.setString(1, cfreezeid);
      rs = stmt.executeQuery();
      if (rs.next()) {
        String sncode = rs.getString(1);
        if (sncode != null)
          v.add(sncode.trim());
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
    }
    String[] sncodes = null;
    if (v.size() > 0) {
      sncodes = new String[v.size()];
      v.copyInto(sncodes);
    }

    afterCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "queryFreezedSerial", new Object[] { cfreezeid });

    return sncodes;
  }

  public void setLockedFlag(String billtype, ArrayList albid, ArrayList alfid)
    throws BusinessException
  {
    if ((billtype == null) || (albid == null) || (albid.size() == 0))
      return;
    try
    {
      if ((alfid == null) || (alfid.size() == 0)) {
        alfid = new ArrayList(albid.size());
        for (int i = 0; i < albid.size(); i++) {
          alfid.add(null);
        }

      }

      if ("30".equals(billtype))
      {
        ISOToIC_DRP bo = (ISOToIC_DRP)NCLocator.getInstance().lookup(ISOToIC_DRP.class.getName());

        bo.setLockedFlag(albid, alfid);
      }

      if ("A3".equals(billtype)) {
        String[] sbids = new String[albid.size()];
        String[] sfids = new String[alfid.size()];

        for (int i = 0; i < albid.size(); i++) {
          sbids[i] = ((String)albid.get(i));
          sfids[i] = ((String)alfid.get(i));
        }

        if (ModuleEnable.isModuleEnabled_MM("MM")) {
          MMHelper.getIMmToIc().setLockedFlagIC(albid, alfid);
        }
        else {
          PickmDMO dmo = new PickmDMO();
          dmo.setLockedFlag(sbids, sfids);
        }
      }
    }
    catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000027") + e.getMessage());
    }
  }

  public void unfreeze(FreezeVO[] voaFreeze)
    throws SQLException, SystemException, BusinessException
  {
    if ((voaFreeze == null) || (voaFreeze.length == 0)) {
      return;
    }
    String sUserID = null;
    UFDate dtLogDate = null;
    Hashtable htLock = new Hashtable();
    ArrayList vLockid = null;
    String cortype = null;
    String corbid = null;

    String[] saFreezeid = null;

    Hashtable htOtherFields = new Hashtable();
    String sUnFreNum = null;
    String sUnFreastNum = null;
    String sUnFregroNum = null;

    ICLockBO lock = new ICLockBO();
    ArrayList al_PK = new ArrayList();
    ICSmartToolsDmo icSTDmo = null;

    for (int i = 0; i < voaFreeze.length; i++)
    {
      saFreezeid = new String[1];

      if (i == 0)
      {
        sUserID = voaFreeze[i].getCthawpersonid();
        dtLogDate = voaFreeze[i].getDthawdate();
      }

      saFreezeid[0] = voaFreeze[i].getPrimaryKey();
      al_PK.add(saFreezeid[0]);
      try
      {
        lock.lockPKs(sUserID, al_PK);

        icSTDmo = new ICSmartToolsDmo();
        icSTDmo.checkTs(new FreezeVO[] { voaFreeze[i] }, "ic_freeze", "ts", "cfreezeid", "ts");
      }
      catch (Exception ex)
      {
        throw nc.bs.ic.pub.GenMethod.handleException(ex.getMessage(), ex);
      }

      cortype = voaFreeze[i].getCcorrespondtype();
      corbid = voaFreeze[i].getCcorrespondbid();

      sUnFreNum = voaFreeze[i].getNdefrznum() != null ? String.valueOf(voaFreeze[i].getNdefrznum().doubleValue()) : null;

      sUnFreastNum = voaFreeze[i].getNdefrzastnum() != null ? String.valueOf(voaFreeze[i].getNdefrzastnum().doubleValue()) : null;

      sUnFregroNum = voaFreeze[i].getNdefrzgrsnum() != null ? String.valueOf(voaFreeze[i].getNdefrzgrsnum().doubleValue()) : null;

      if ((cortype != null) && (corbid != null)) {
        if (htLock.containsKey(cortype)) {
          vLockid = (ArrayList)htLock.get(cortype);
          vLockid.add(corbid);
        }
        else {
          vLockid = new ArrayList();
          vLockid.add(corbid);
          htLock.put(cortype, vLockid);
        }
      }

      if (sUnFreNum != null) {
        htOtherFields.put("ndefrznum", sUnFreNum);
      }

      if (sUnFreastNum != null) {
        htOtherFields.put("ndefrzastnum", sUnFreastNum);
      }
      if (sUnFregroNum != null) {
        htOtherFields.put("ndefrzgrsnum", sUnFregroNum);
      }

      htOtherFields.put("currentVO", voaFreeze[i]);
      unfreeze(sUserID, dtLogDate.toString(), saFreezeid, htOtherFields);

      autoFreeze(new FreezeVO[] { voaFreeze[i] }, dtLogDate);
    }

    if (htLock.containsKey("30")) {
      ArrayList albid = (ArrayList)htLock.get("30");
      setLockedFlag("30", albid, null);
    }
    if (htLock.containsKey("A3")) {
      ArrayList albid = (ArrayList)htLock.get("A3");
      setLockedFlag("A3", albid, null);
    }
  }

  private String autoFreeze(FreezeVO[] voaFreeze, UFDate dtLogDate)
    throws BusinessException
  {
    SmartDMO dmo = null;
    String sPK = null;

    UFDouble ufdFreezeNum = null;

    UFDouble ufdFreezeastNum = null;

    UFDouble ufdFreezeGrossNum = null;

    boolean needAutoFre = false;
    try
    {
      dmo = new SmartDMO();
      FreezeVO fvo = null;
      for (int i = 0; i < voaFreeze.length; i++)
      {
        fvo = (FreezeVO)dmo.selectByKey(FreezeVO.class, voaFreeze[i].getPrimaryKey());

        ufdFreezeNum = (fvo.getNfreezenum() != null) && (fvo.getNdefrznum() != null) ? fvo.getNfreezenum().sub(fvo.getNdefrznum()) : null;

        ufdFreezeastNum = (fvo.getNfreezeastnum() != null) && (fvo.getNfreezeastnum() != null) ? fvo.getNfreezeastnum().sub(fvo.getNdefrzastnum()) : null;

        ufdFreezeGrossNum = (fvo.getNgrossnum() != null) && (fvo.getNdefrzgrsnum() != null) ? fvo.getNgrossnum().sub(fvo.getNdefrzgrsnum()) : null;

        if ((!nc.vo.ic.pub.GenMethod.isEQZeroOrNull(ufdFreezeNum)) && (!needAutoFre))
        {
          needAutoFre = true;
        }if ((!nc.vo.ic.pub.GenMethod.isEQZeroOrNull(ufdFreezeastNum)) && (!needAutoFre))
        {
          needAutoFre = true;
        }if ((!nc.vo.ic.pub.GenMethod.isEQZeroOrNull(ufdFreezeGrossNum)) && (!needAutoFre))
        {
          needAutoFre = true;
        }
        if (!needAutoFre) {
          continue;
        }
        fvo.setPrimaryKey(null);

        fvo.setDtfreezetime(dtLogDate);
        fvo.setNfreezenum(ufdFreezeNum);
        fvo.setNfreezeastnum(ufdFreezeastNum);
        fvo.setNgrossnum(ufdFreezeGrossNum);
        fvo.setNdefrznum(null);
        fvo.setNdefrzastnum(null);
        fvo.setNdefrzgrsnum(null);
        fvo.setCthawpersonid(null);
        fvo.setDthawdate(null);
        fvo.setBfrzhandflag("N");

        fvo.setStatus(2);

        sPK = freeze(fvo);
      }
    }
    catch (Exception ex)
    {
      nc.bs.ic.pub.GenMethod.handleException(ResBase.getUnfreError(), ex);
    }

    return sPK;
  }

  public void unfreeze(String sOperatorid, String sDate, String[] saFreezeid)
    throws SQLException, SystemException, BusinessException
  {
    if ((sOperatorid == null) || (sDate == null) || (saFreezeid == null) || (saFreezeid.length == 0))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000028"));
    }

    SmartDMO smartDMO = null;
    FreezeVO tempfvo = null;
    String sNdefreezeNum = null;
    String sNdefreezeastNun = null;
    String sNdefreezegroNum = null;
    Hashtable htOtherFields = new Hashtable();
    try
    {
      smartDMO = new SmartDMO();

      int i = 0; for (int j = saFreezeid.length; i < j; i++) {
        tempfvo = (FreezeVO)smartDMO.selectByKey(FreezeVO.class, saFreezeid[i]);

        if (tempfvo != null)
          continue;
        Object[] oret = smartDMO.selectBy(FreezeVO.class, null, " ccorrespondbid = '" + saFreezeid[i] + "' and cthawpersonid is null ");

        if ((oret != null) && (oret.length > 0)) {
          int m = 0; for (int n = oret.length; m < n; m++) {
            tempfvo = (FreezeVO)oret[m];

            if (null == tempfvo) {
              break;
            }
            sNdefreezeNum = tempfvo.getNfreezenum() != null ? String.valueOf(tempfvo.getNfreezenum()) : null;

            sNdefreezeastNun = tempfvo.getNfreezeastnum() != null ? String.valueOf(tempfvo.getNfreezeastnum()) : null;

            sNdefreezegroNum = tempfvo.getNgrossnum() != null ? String.valueOf(tempfvo.getNgrossnum()) : null;

            if (sNdefreezeNum != null) {
              htOtherFields.put("ndefrznum", sNdefreezeNum);
            }

            if (sNdefreezeastNun != null) {
              htOtherFields.put("ndefrzastnum", sNdefreezeastNun);
            }
            if (sNdefreezegroNum != null) {
              htOtherFields.put("ndefrzgrsnum", sNdefreezegroNum);
            }

            unfreeze(sOperatorid, sDate, new String[] { tempfvo.getCfreezeid() }, htOtherFields);

            tempfvo = null;
          }
        }

      }

    }
    catch (NamingException e)
    {
      nc.bs.ic.pub.GenMethod.handleException(null, e);
    }
  }

  public void unfreeze(String sOperatorid, String sDate, String[] saFreezeid, Hashtable htOtherFields)
    throws SQLException, SystemException, BusinessException
  {
    if ((sOperatorid == null) || (sDate == null) || (saFreezeid == null) || (saFreezeid.length == 0))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000028"));
    }

    String sbSql1 = GeneralSqlString.formInSQL("cfreezeid", saFreezeid);

    String sbSql = " and cfreezeid in (select cfreezeid from ic_freeze where (0=0 " + sbSql1 + ")" + " or (0=0 " + sbSql1.replaceAll("cfreezeid", "ccorrespondbid") + ")) ";

    String sUnFreNum = null;
    String sUnFreastNum = null;
    String sUnFregroNum = null;
    StringBuffer sbufferSql = new StringBuffer("UPDATE ic_freeze SET  cthawpersonid = ?,  dthawdate = ? ");

    if ((htOtherFields != null) && (htOtherFields.size() > 0))
    {
      sUnFreNum = htOtherFields.containsKey("ndefrznum") ? htOtherFields.get("ndefrznum").toString() : null;

      sUnFreastNum = htOtherFields.containsKey("ndefrzastnum") ? htOtherFields.get("ndefrzastnum").toString() : null;

      sUnFregroNum = htOtherFields.containsKey("ndefrzgrsnum") ? htOtherFields.get("ndefrzgrsnum").toString() : null;
    }

    if (sUnFreNum != null)
      sbufferSql.append(", ndefrznum = ? ");
    if (sUnFreastNum != null)
      sbufferSql.append(", ndefrzastnum = ? ");
    if (sUnFregroNum != null) {
      sbufferSql.append(", ndefrzgrsnum = ? ");
    }
    sbufferSql.append(" WHERE 0=0 ");
//    String sql = sbSql;
    String sql = sbufferSql.toString();//zwx测试 测试环境改位这个才可以返工处理，用于测试，未更新补丁上去

    String sqlsn = "UPDATE ic_general_bb2 SET cfreezeid=? WHERE 0=0 " + sbSql;

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      stmt.setString(1, sOperatorid);
      stmt.setString(2, sDate);

      int iParam = 3;
      if (sUnFreNum != null) {
        stmt.setString(iParam, sUnFreNum);
        iParam++;
      }
      if (sUnFreastNum != null) {
        stmt.setString(iParam, sUnFreastNum);
        iParam++;
      }
      if (sUnFregroNum != null) {
        stmt.setString(iParam, sUnFregroNum);
      }

      stmt.executeUpdate();

      if (stmt != null) {
        stmt.close();
      }

      stmt = con.prepareStatement(sqlsn);
      stmt.setNull(1, 1);
      stmt.executeUpdate();
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
    }
  }

  public void unLockInv(String billtype, String[] cbodyids, String userID, UFDate dlogindate)
    throws BusinessException
  {
    if ((billtype == null) || (cbodyids == null) || (cbodyids.length <= 0) || (userID == null) || (dlogindate == null))
    {
      return;
    }
    try
    {
      unfreeze(userID, dlogindate.toString(), cbodyids);

      ArrayList idlist = new ArrayList(cbodyids.length);
      idlist.addAll(Arrays.asList(cbodyids));

      if (billtype.equals("30")) {
        setLockedFlag("30", idlist, null);
      }
      if (billtype.equals("A3")) {
        setLockedFlag("A3", idlist, null);
      }
    }
    catch (Exception e)
    {
      throw nc.bs.ic.pub.GenMethod.handleException(null, e);
    }
  }

  public void unLockInv(GeneralBillVO vo, String userID, String sDate)
    throws SQLException, SystemException, BusinessException
  {
    if ((vo == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
    {
      return;
    }GeneralBillItemVO[] voItems = (GeneralBillItemVO[])(GeneralBillItemVO[])vo.getChildrenVO();

    Vector v = new Vector();
    String cortype = null;
    String corbid = null;
    Hashtable htLock = new Hashtable();
    ArrayList vLockid = null;
    String cfreezeid = null;
    String billtype = vo.getHeaderVO().getCbilltypecode();

    for (int i = 0; i < voItems.length; i++) {
      cfreezeid = (String)voItems[i].getAttributeValue("cfreezeid");
      if (cfreezeid == null) {
        if (("4Y".equals(billtype)) || ("4C".equals(billtype)))
          cfreezeid = voItems[i].getCfirstbillbid();
        else if (("4D".equals(billtype)) || ("4F".equals(billtype))) {
          cfreezeid = voItems[i].getCsourcebillbid();
        }
      }
      if ("4F".equals(billtype)) {
        cfreezeid = voItems[i].getCsourcebillbid();
      }
      if ((cfreezeid == null) || (voItems[i].getNoutnum() == null) || (voItems[i].getNoutnum().doubleValue() == 0.0D))
        continue;
      v.add(cfreezeid);
      cortype = voItems[i].getCfirsttype();
      corbid = voItems[i].getCfirstbillbid();

      if ((cortype != null) && (corbid != null)) {
        if (htLock.containsKey(cortype)) {
          vLockid = (ArrayList)htLock.get(cortype);
          vLockid.add(corbid);
        }
        else {
          vLockid = new ArrayList();
          vLockid.add(corbid);
          htLock.put(cortype, vLockid);
        }
      }
      cortype = voItems[i].getCsourcetype();
      corbid = voItems[i].getCsourcebillbid();
      if ((voItems[i].getCsourcetype().equals(voItems[i].getCfirsttype())) || 
        (cortype == null) || (corbid == null)) continue;
      if (htLock.containsKey(cortype)) {
        vLockid = (ArrayList)htLock.get(cortype);
        vLockid.add(corbid);
      }
      else {
        vLockid = new ArrayList();
        vLockid.add(corbid);
        htLock.put(cortype, vLockid);
      }

    }

    if (v.size() > 0) {
      String[] freezeids = new String[v.size()];
      v.copyInto(freezeids);

      unfreeze(userID, sDate, freezeids);
    }

    if (htLock.containsKey("30")) {
      ArrayList albid = (ArrayList)htLock.get("30");
      setLockedFlag("30", albid, null);
    }
    if (htLock.containsKey("A3")) {
      ArrayList albid = (ArrayList)htLock.get("A3");
      setLockedFlag("A3", albid, null);
    }
  }

  public String insertFreezeold(FreezeVO freeze)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "freeze", new Object[] { freeze });

    if ((freeze == null) || (freeze.getCfreezerid() == null) || (freeze.getDtfreezetime() == null))
    {
      SCMEnv.out("data is null,id null.");
      return null;
    }
    String sql = "insert into ic_freeze(cfreezeid, pk_corp, cwarehouseid, cspaceid, cinventoryid, castunitid, vbatchcode, dvalidate,  vfree1, vfree2, vfree3, vfree4, vfree5, vfree10, vfree9, vfree8, vfree7, vfree6, nfreezenum,  nfreezeastnum,  ccorrespondbid, ccorrespondhid, ccorrespondtype, ccorrespondcode, cfreezerid,  dtfreezetime, cthawpersonid,  dthawdate,ccalbodyid,dunlockdate) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

    String key = null;
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      key = getOID();
      con = getConnection();
      SCMEnv.out("freeze--->" + sql);
      stmt = con.prepareStatement(sql);

      stmt.setString(1, key);

      if (freeze.getPk_corp() == null) {
        stmt.setNull(2, 1);
      }
      else {
        stmt.setString(2, freeze.getPk_corp());
      }
      if (freeze.getCwarehouseid() == null) {
        stmt.setNull(3, 1);
      }
      else {
        stmt.setString(3, freeze.getCwarehouseid());
      }
      if (freeze.getCspaceid() == null) {
        stmt.setNull(4, 1);
      }
      else {
        stmt.setString(4, freeze.getCspaceid());
      }
      if (freeze.getCinventoryid() == null) {
        stmt.setNull(5, 1);
      }
      else {
        stmt.setString(5, freeze.getCinventoryid());
      }
      if (freeze.getCastunitid() == null) {
        stmt.setNull(6, 1);
      }
      else {
        stmt.setString(6, freeze.getCastunitid());
      }
      if (freeze.getVbatchcode() == null) {
        stmt.setNull(7, 1);
      }
      else {
        stmt.setString(7, freeze.getVbatchcode());
      }
      if (freeze.getDvalidate() == null) {
        stmt.setNull(8, 1);
      }
      else {
        stmt.setString(8, freeze.getDvalidate().toString());
      }
      FreeVO voFreeItem = freeze.getFreeItemVO();
      if (voFreeItem == null) {
        voFreeItem = new FreeVO();
      }
      if (voFreeItem.getVfree1() == null) {
        stmt.setNull(9, 1);
      }
      else {
        stmt.setString(9, voFreeItem.getVfree1());
      }
      if (voFreeItem.getVfree2() == null) {
        stmt.setNull(10, 1);
      }
      else {
        stmt.setString(10, voFreeItem.getVfree2());
      }
      if (voFreeItem.getVfree3() == null) {
        stmt.setNull(11, 1);
      }
      else {
        stmt.setString(11, voFreeItem.getVfree3());
      }
      if (voFreeItem.getVfree4() == null) {
        stmt.setNull(12, 1);
      }
      else {
        stmt.setString(12, voFreeItem.getVfree4());
      }
      if (voFreeItem.getVfree5() == null) {
        stmt.setNull(13, 1);
      }
      else {
        stmt.setString(13, voFreeItem.getVfree5());
      }
      if (voFreeItem.getVfree10() == null) {
        stmt.setNull(14, 1);
      }
      else {
        stmt.setString(14, voFreeItem.getVfree10());
      }
      if (voFreeItem.getVfree9() == null) {
        stmt.setNull(15, 1);
      }
      else {
        stmt.setString(15, voFreeItem.getVfree9());
      }
      if (voFreeItem.getVfree8() == null) {
        stmt.setNull(16, 1);
      }
      else {
        stmt.setString(16, voFreeItem.getVfree8());
      }
      if (voFreeItem.getVfree7() == null) {
        stmt.setNull(17, 1);
      }
      else {
        stmt.setString(17, voFreeItem.getVfree7());
      }
      if (voFreeItem.getVfree6() == null) {
        stmt.setNull(18, 1);
      }
      else {
        stmt.setString(18, voFreeItem.getVfree6());
      }
      if (freeze.getNfreezenum() == null) {
        stmt.setNull(19, 4);
      }
      else {
        stmt.setBigDecimal(19, freeze.getNfreezenum().toBigDecimal());
      }
      if (freeze.getNfreezeastnum() == null) {
        stmt.setNull(20, 4);
      }
      else {
        stmt.setBigDecimal(20, freeze.getNfreezeastnum().toBigDecimal());
      }
      if (freeze.getCcorrespondbid() == null) {
        stmt.setNull(21, 1);
      }
      else {
        stmt.setString(21, freeze.getCcorrespondbid());
      }
      if (freeze.getCcorrespondhid() == null) {
        stmt.setNull(22, 1);
      }
      else {
        stmt.setString(22, freeze.getCcorrespondhid());
      }
      if (freeze.getCcorrespondtype() == null) {
        stmt.setNull(23, 1);
      }
      else {
        stmt.setString(23, freeze.getCcorrespondtype());
      }
      if (freeze.getCcorrespondcode() == null) {
        stmt.setNull(24, 1);
      }
      else {
        stmt.setString(24, freeze.getCcorrespondcode());
      }
      if (freeze.getCfreezerid() == null) {
        stmt.setNull(25, 1);
      }
      else {
        stmt.setString(25, freeze.getCfreezerid());
      }
      if (freeze.getDtfreezetime() == null) {
        stmt.setNull(26, 1);
      }
      else {
        stmt.setString(26, freeze.getDtfreezetime().toString());
      }
      if (freeze.getCthawpersonid() == null) {
        stmt.setNull(27, 1);
      }
      else {
        stmt.setString(27, freeze.getCthawpersonid());
      }
      if (freeze.getDthawdate() == null) {
        stmt.setNull(28, 1);
      }
      else {
        stmt.setString(28, freeze.getDthawdate().toString());
      }
      if (freeze.getCcalbodyid() == null) {
        stmt.setNull(29, 1);
      }
      else {
        stmt.setString(29, freeze.getCcalbodyid().toString());
      }
      if (freeze.getAttributeValue("dunlockdate") == null) {
        stmt.setNull(30, 1);
      }
      else {
        stmt.setString(30, freeze.getAttributeValue("dunlockdate").toString());
      }

      stmt.executeUpdate();

      if (stmt != null) {
        stmt.close();
      }

      String[] snids = freeze.getSerials();

      if ((snids != null) && (key != null))
      {
        String sqlsn = "select cserialid,vserialcode from ic_general_bb2 where dr=0 " + GeneralSqlString.formInSQL("cserialid", snids);

        stmt = con.prepareStatement(sqlsn);
        ResultSet rs = stmt.executeQuery();
        HashMap ht = new HashMap();
        String id = null;
        String code = null;
        while (rs.next()) {
          id = rs.getString(1);
          code = rs.getString(2);
          if (id != null) {
            ht.put(id, code);
          }
        }

        stmt.close();

        String snsql = null;
        ArrayList aloutsn = new ArrayList();
        for (int i = 0; i < snids.length; i++)
        {
          snsql = "update ic_general_bb2 set cfreezeid='" + key + "' where  cserialid=? and coutbillbodyid is null ";

          stmt = con.prepareStatement(snsql);
          stmt.setString(1, snids[i]);
          if (stmt.executeUpdate() == 0) {
            aloutsn.add(ht.get(snids[i]));
          }
        }

        if (aloutsn.size() > 0) {
          StringBuffer msg = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000026"));

          for (int k = 0; k < aloutsn.size(); k++)
            msg.append(aloutsn.get(k) + ",");
          throw new SQLException(msg.toString());
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
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }

    }

    afterCallMethod("nc.bs.ic.pub.freeze.FreezeDMO", "freeze", new Object[] { freeze });

    return key;
  }

  public ArrayList query_old1(QryConditionVO voCond)
    throws Exception
  {
    if ((voCond == null) || (voCond.getParam(1) == null) || (voCond.getParam(1).toString().trim().length() == 0))
    {
      SCMEnv.out("cond para null.");
      return null;
    }

    String sCorpID = voCond.getStrParam(0);

    String sWhID = null;
    if (voCond.getParam(0) != null) {
      sWhID = voCond.getParam(0).toString();
    }
    String sWhName = null;

    String sIsLocMgt = null;

    String sQryType = "FREEZE";
    if (voCond.getParam(1) != null) {
      sQryType = voCond.getParam(1).toString().trim().toUpperCase();
    }
    FreezeVO[] voaFreeze = null;
    Vector vAllData = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;

    String sPk_calbody = null;
    try {
      con = getConnection();
      ResultSet rs = null;

      if ("FREEZE".equals(sQryType))
      {
        stmt = con.prepareStatement("SELECT storname,csflag,pk_calbody FROM bd_stordoc WHERE pk_stordoc='" + sWhID + "'");

        rs = stmt.executeQuery();
        if (rs.next()) {
          sWhName = rs.getString(1);
          sIsLocMgt = rs.getString(2);
          sPk_calbody = rs.getString(3);
        }

        if (stmt != null) {
          stmt.close();
        }
      }

      StringBuffer sbSql = new StringBuffer();
      if ("FREEZE".equals(sQryType)) {
        String sSelectFields = " v.cwarehouseid,v.pk_corp,v.cinventoryid,v.castunitid,v.hsl,v.vbatchcode,v.cvendorid, v.vfree1,v.vfree2,v.vfree3,v.vfree4,v.vfree5,v.vfree6,v.vfree7,v.vfree8,v.vfree9,v.vfree10,";

        if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y")))
          sSelectFields = sSelectFields + " SUM(COALESCE (v.ninspacenum, 0.0) - COALESCE (v.noutspacenum, 0.0))   AS nonhandnum ,     SUM(COALESCE (v.ninspaceassistnum, 0.0) - COALESCE (v.noutspaceassistnum, 0.0))   AS nonhandastnum,SUM(COALESCE (v.ningrossnum, 0.0)-COALESCE (v.noutgrossnum, 0.0)) as ngross  ";
        else {
          sSelectFields = sSelectFields + " SUM(COALESCE (v.ninnum, 0.0) - COALESCE (v.noutnum, 0.0)) AS nonhandnum  , SUM(COALESCE (v.ninassistnum, 0.0) - COALESCE (v.noutassistnum, 0.0)) AS nonhandastnum,SUM(COALESCE (v.ningrossnum, 0.0)-COALESCE (v.noutgrossnum, 0.0)) as ngross   ";
        }
        String sGroupBy = " GROUP BY v.pk_corp,v.cwarehouseid, v.cinventoryid, v.vbatchcode,v.castunitid,v.cvendorid,v.hsl,v.vfree1,v.vfree2,v.vfree3,v.vfree4,v.vfree5,v.vfree6,v.vfree7,v.vfree8,v.vfree9,v.vfree10";
        String sFrom = " v_ic_onhandnum1 ";
        String sWhere = " where 1=1 ";

        if ((sCorpID != null) && (sCorpID.trim().length() > 0)) {
          sWhere = sWhere + " AND v.pk_corp='" + sCorpID + "'";
        }

        if ((sWhID != null) && (sWhID.trim().length() > 0)) {
          sWhere = sWhere + " AND v.cwarehouseid='" + sWhID + "'";
        }

        if ((voCond.getQryCond() != null) && (voCond.getQryCond().trim().length() > 0))
        {
          sWhere = sWhere + " and " + voCond.getQryCond();
        }

        if ((sIsLocMgt != null) && (sIsLocMgt.toUpperCase().trim().equals("Y"))) {
          sSelectFields = sSelectFields + ",v.cspaceid ";
          sFrom = " v_ic_onhandnum2 ";
          sGroupBy = sGroupBy + ",v.cspaceid ";
        }
        String sBQ = "select " + sSelectFields + " from " + sFrom + " v " + sWhere + sGroupBy;

        sbSql.append("select bq.cwarehouseid,bq.pk_corp,bq.cinventoryid,bq.castunitid,bq.hsl,bq.vbatchcode,bq.cvendorid ");

        sbSql.append(",bq.vfree1,bq.vfree2,bq.vfree3,bq.vfree4,bq.vfree5,bq.vfree6,bq.vfree7,bq.vfree8,bq.vfree9,bq.vfree10");

        sbSql.append(",bq.nonhandnum,bq.nonhandastnum,bq.ngross");
        sbSql.append(",f.nfreezenum,f.cfreezerid,f.dtfreezetime,f.cthawpersonid,f.dthawdate ");

        sbSql.append(" from (");
        sbSql.append(sBQ);
        sbSql.append(") bq ");
        sbSql.append(" LEFT OUTER JOIN ic_freeze f ");
        sbSql.append(" ON  bq.cwarehouseid=f.cwarehouseid  ");
        sbSql.append(" AND bq.cinventoryid=f.cinventoryid AND bq.vbatchcode=f.vbatchcode  ");

        sbSql.append(" AND isnull(bq.cvendorid,' ')=isnull(f.cvendorid,' ') ");
        sbSql.append(" AND isnull(bq.vfree1,' ')=isnull(f.vfree1,' ') AND isnull(bq.vfree2,' ')=isnull(f.vfree2,' ') AND isnull(bq.vfree3,' ')=isnull(f.vfree3,' ') AND isnull(bq.vfree4,' ')=isnull(f.vfree4,' ') AND isnull(bq.vfree5,' ')=isnull(f.vfree5,' ') AND isnull(bq.vfree6,' ')=isnull(f.vfree6,' ') AND isnull(bq.vfree7,' ')=isnull(f.vfree7,' ') AND isnull(bq.vfree8,' ')=isnull(f.vfree8,' ')  AND isnull(bq.vfree9,' ')=isnull(f.vfree9,' ') AND isnull(bq.vfree10,' ') = isnull(f.vfree10,' ') ");

        sbSql.append(" where 1=1 and  bq.nonhandnum > 0 ");
      }
      else
      {
        sbSql.append("SELECT cfreezeid, f.pk_corp, cwarehouseid, wh.storname AS cwarehousename,   conv102.fixedflag AS issolidconvrate, meas2.measname AS castunitname, f.castunitid,   hsl,    cspaceid,   loc.cscode AS cspacecode, loc.csname AS cspacename, cinventoryid, inv.invcode,       inv.invname,inv.invspec,inv.invtype, meas1.measname AS measdocname, vbatchcode,  vfree1,       vfree2, vfree3, vfree4, vfree5, vfree10, vfree9, vfree8, vfree7, vfree6, nfreezenum,nfreezeastnum,ngrossnum, ccorrespondbid, ccorrespondhid, ccorrespondtype,bt.billtypename AS vcorrespondname,  ccorrespondcode,       cfreezerid, p1.user_name AS cfreezername, dtfreezetime, cthawpersonid,   p2.user_name AS cthawpersonname, dthawdate,dunlockdate,cvendorid FROM ic_freeze f   LEFT OUTER JOIN bd_billtype bt ON f.ccorrespondtype = bt.pk_billtypecode  LEFT OUTER JOIN      sm_user p1 ON f.cfreezerid = p1.cUserID LEFT OUTER JOIN      sm_user p2 ON f.cthawpersonid = p2.cUserID  LEFT OUTER  JOIN      bd_invmandoc invman ON  f.cinventoryid = invman.pk_invmandoc  LEFT OUTER JOIN      bd_invbasdoc inv  ON invman.pk_invbasdoc=inv.pk_invbasdoc      LEFT OUTER JOIN      bd_measdoc meas1 ON inv.pk_measdoc = meas1.pk_measdoc     LEFT OUTER JOIN      bd_cargdoc loc ON f.cspaceid=loc.pk_cargdoc  LEFT OUTER JOIN      bd_stordoc wh ON  f.cwarehouseid=wh.pk_stordoc      LEFT OUTER JOIN      bd_measdoc meas2 ON f.castunitid = meas2.pk_measdoc LEFT OUTER JOIN      " + GeneralSqlString.sBd_ConvertMainUom + " conv ON inv.pk_invbasdoc = conv.pk_invbasdoc  LEFT OUTER JOIN     " + GeneralSqlString.sBd_Convert + " conv102 ON f.castunitid=conv102.pk_measdoc   WHERE " + GeneralSqlString.leftOuterJoinConvert("conv", "conv102") + " AND f.cfreezerid IS NOT NULL AND ");

        if ("UNFREEZE".equals(sQryType))
        {
          sbSql.append(" f.cthawpersonid IS NULL ");
        }
        else {
          sbSql.append(" f.cthawpersonid IS NOT NULL ");
        }

        if ((sCorpID != null) && (sCorpID.trim().length() > 0)) {
          sbSql.append(" AND f.pk_corp='");
          sbSql.append(sCorpID);
          sbSql.append("'");
        }

        if ((sWhID != null) && (sWhID.trim().length() > 0)) {
          sbSql.append(" AND cwarehouseid='");
          sbSql.append(sWhID);
          sbSql.append("'");
        }

        if ((voCond.getQryCond() != null) && (voCond.getQryCond().trim().length() > 0))
        {
          sbSql.append("      AND " + voCond.getQryCond());
        }
      }

      SCMEnv.out("-------->" + sbSql.toString());
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      while (rs.next()) {
        FreezeVO voFreeze = new FreezeVO();

        Object oValue = null;
        ResultSetMetaData meta = rs.getMetaData();

        String sColumnName = null;
        for (int i = 1; i <= meta.getColumnCount(); i++) {
          oValue = null;
          sColumnName = meta.getColumnName(i).trim().toLowerCase();

          oValue = rs.getObject(i);

          voFreeze.setFselected(new UFBoolean(false));

          if ((sColumnName.equals("nonhandastnum")) && (oValue != null) && (Double.valueOf(oValue.toString()).doubleValue() == 0.0D))
          {
            oValue = null;
          }voFreeze.setAttributeValue(sColumnName, oValue);
        }

        voFreeze.setCcalbodyid(sPk_calbody);

        if ("FREEZE".equals(sQryType));
        vAllData.addElement(voFreeze);
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
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    if (vAllData.size() > 0) {
      voaFreeze = new FreezeVO[vAllData.size()];
      vAllData.copyInto(voaFreeze);
      nc.bs.ic.pub.GenMethod method = new nc.bs.ic.pub.GenMethod();

      method.setFreeItemVO(voaFreeze);

      method.filterAstUnit(voaFreeze);
    }

    ArrayList alRet = new ArrayList();
    alRet.add(sWhName);
    alRet.add(voaFreeze);
    return alRet;
  }

  private void dealFreezeVO(FreezeVO vo) throws Exception {
    if (vo == null)
      return;
    InvVO voInv = new InvVO();
    voInv.setCinventoryid(vo.getCinventoryid());
    voInv.setCastunitid(vo.getCastunitid());

    voInv = CheckDMO.appendInvInfo(voInv);

    WhVO voWh = new QueryInfoDMO().getWhInfo(vo.getCwarehouseid());
    if ((voInv.getIsStoreByConvert() == null) || (voInv.getIsStoreByConvert().intValue() != 1))
    {
      vo.setHsl(null);
    }
    if (((voWh.getIsgathersettle() == null) || (!voWh.getIsgathersettle().booleanValue())) && ((voInv.getIssupplierstock() == null) || (voInv.getIssupplierstock().intValue() != 1)))
    {
      vo.setCvendorid(null);
    }
  }
}