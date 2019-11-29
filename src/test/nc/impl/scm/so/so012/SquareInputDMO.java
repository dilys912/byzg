package nc.impl.scm.so.so012;

import java.math.BigDecimal;
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
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.impl.scm.so.pub.FetchValueDMO;
import nc.impl.scm.so.pub.GeneralSqlString;
import nc.impl.scm.so.pub.VOCalculate;
import nc.itf.scm.so.so012.ISquare;
import nc.itf.scm.so.so012.ISquareImputDMO;
import nc.itf.uap.pf.IPFBusiAction;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so012.SquareHeaderVO;
import nc.vo.so.so012.SquareItemVO;
import nc.vo.so.so012.SquareTotalVO;
import nc.vo.so.so012.SquareVO;

public class SquareInputDMO extends DataManageObject
  implements ISquareImputDMO
{
  String SO_05 = null;

  public SquareInputDMO()
    throws NamingException, SystemException
  {
  }

  public SquareInputDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  private void autoSquareOut(SquareVO voData)
    throws SQLException, BusinessException, SystemException, NamingException, Exception
  {
    SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])voData.getChildrenVO();
    for (int i = 0; i < items.length; i++) {
      String sBillTyep = ((SquareHeaderVO)voData.getParentVO()).getCreceipttype();

      if ((sBillTyep == null ? "" : sBillTyep).equals("32")) {
        items[i] = loadInvoiceWarehouseID(items[i]);
      }

      items[i].setNnewbalancenum(items[i].getNoutnum());
    }
    IPFBusiAction pfbp = (IPFBusiAction)NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
    pfbp.processAction("SoSquare", "33", getClientEnvironment().getDate().toString(), null, voData, null, null);
  }

  public SquareItemVO calculateOutMoney(SquareItemVO voItem)
    throws Exception
  {
    return voItem;
  }

  public boolean checkCancelData(SquareVO[] voData)
    throws SQLException, BusinessException
  {
    Hashtable ht = new Hashtable();

    for (int i = 0; i < voData.length; i++) {
      String scsaleid = voData[i].getParentVO().getAttributeValue("csaleid").toString();

      if (ht.containsKey(scsaleid)) {
        int j = Integer.parseInt(ht.get(scsaleid).toString()) + voData[i].getChildrenVO().length;

        ht.put(scsaleid, Integer.toString(j));
      } else {
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
          stmt.setNull(1, 1);
        else {
          stmt.setString(1, ((SquareHeaderVO)voData[i].getParentVO()).getCsaleid());
        }

        ResultSet rs = stmt.executeQuery();
        int m = 0;
        if (rs.next()) {
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
      try {
        if (stmt != null) {
          stmt.close();
        }

        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    return true;
  }

  public String getCreceiptcorpidByID(String csaleid) throws Exception
  {
    String sSql = " select creceiptcorpid from so_sale where csaleid='" + csaleid + "' ";
    SmartDMO sdmo = new SmartDMO();
    Object[] o = sdmo.selectBy2(sSql);
    String creceiptcorpid = null;
    if ((o != null) && (o.length > 0)) {
      Object[] o1 = (Object[])(Object[])o[0];
      if ((o1 != null) && (o1.length > 0)) {
        creceiptcorpid = (String)o1[0];
      }
    }
    return creceiptcorpid;
  }

  public SquareVO checkSquareVO(SquareVO voData)
    throws SQLException, BusinessException, SystemException, NamingException, Exception
  {
    SquareHeaderVO header = (SquareHeaderVO)voData.getParentVO();
    String sCorp = header.getPk_corp();

    UFBoolean bIncome = header.getBincomeflag();

    UFBoolean bCost = header.getBcostflag();

    UFBoolean bthisEs = header.getBEstimation() == null ? new UFBoolean(false) : header.getBEstimation();

    String sBillType = header.getCreceipttype();

    String sVerifyrule = header.getVerifyrule();

    getSystemPara(sCorp);

    SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])voData.getChildrenVO();

    Vector vBodyid = new Vector();

    Connection con = getConnection();
    PreparedStatement stmt = null;

    for (int i = 0; i < items.length; i++)
    {
      if (items[i].getCsourcebillbodyid() != null) {
        vBodyid.addElement(items[i].getCsourcebillbodyid());
      }
      else {
        vBodyid.addElement(getAnySourceBodyid(items[i].getCsaleid()));
      }
      if ((sBillType == null ? "" : sBillType).equals("4C")) {
        if ((this.SO_05 == null) || (!this.SO_05.equals("签收量")))
          continue;
        UFDouble dSignNumber = getSignNumber(items[i].getPrimaryKey());

        if ((dSignNumber == null) || (dSignNumber.doubleValue() == 0.0D)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000026"));
        }

      }
      else
      {
        boolean isLaborDiscount = items[i].getLaborflag() != null;

        if (isLaborDiscount)
          continue;
        UFDouble dOut = items[i].getNoutnum();
        if ((dOut == null) || (dOut.doubleValue() == 0.0D)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000027"));
        }

      }

    }

    String sSql = getSquareSql(vBodyid);

    Vector vReturn = new Vector();

    if (sSql != null)
    {
      stmt = prepareStatement(con, sSql);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Vector vTemp = new Vector();
        vTemp.addElement(rs.getString(1));
        vTemp.addElement(rs.getString(2));
        vTemp.addElement(rs.getString(3));
        vTemp.addElement(rs.getString(4));
        String bestimation = rs.getString(5);
        vTemp.addElement(bestimation == null ? new UFBoolean(false) : new UFBoolean(bestimation.toString().trim()));
        vReturn.addElement(vTemp);
      }

      stmt.close();
    }

    for (int i = 0; i < vReturn.size(); i++) {
      Vector vTemp = (Vector)vReturn.elementAt(i);

      String sRetBillType = vTemp.elementAt(0).toString();

      UFBoolean bRetIncome = vTemp.elementAt(2) == null ? null : new UFBoolean(vTemp.elementAt(2).toString());

      UFBoolean bRetCost = vTemp.elementAt(3) == null ? null : new UFBoolean(vTemp.elementAt(3).toString());

      UFBoolean bestimation = (UFBoolean)vTemp.elementAt(4);
      if ((bIncome.booleanValue()) && (!sVerifyrule.equals("W")) && (!sVerifyrule.equals("F")))
      {
        if ((!sBillType.equals(sRetBillType)) && (bRetIncome.booleanValue()) && (!bestimation.booleanValue()) && (!bthisEs.booleanValue()) && ((!"4C".equals(sBillType)) || (!"4453".equals(sRetBillType))) && ((!"4453".equals(sBillType)) || (!"4C".equals(sRetBillType))) && ((!"32".equals(sBillType)) || (!"4453".equals(sRetBillType))))
        {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000030"));
        }

      }

      if ((!bCost.booleanValue()) || (sVerifyrule.equals("W")) || (sVerifyrule.equals("F")))
        continue;
      if ((sBillType.equals(sRetBillType)) || (!bRetCost.booleanValue()) || (("4C".equals(sBillType)) && ("4453".equals(sRetBillType))) || (("4453".equals(sBillType)) && ("4C".equals(sRetBillType))))
        continue;
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000031"));
    }

    voData = getSquareOutNumber(voData);
    return voData;
  }

  private String getSquareSql(Vector vSourcebodyid)
  {
    String sSql = "select distinct so_square.creceipttype, cbiztype, bincomeflag, bcostflag,bestimation from so_square join so_square_b on so_square_b.csaleid=so_square.csaleid where  isnull(so_square_b.nbalancenum, 0) != 0 and so_square_b.dr=0 and so_square.dr=0 and so_square.csaleid in (select csaleid from so_square_b where 1=1 " + GeneralSqlString.formInSQL("csourcebillbodyid", (String[])(String[])vSourcebodyid.toArray(new String[vSourcebodyid.size()])) + " ) ";

    return sSql;
  }

  private String getAnySourceBodyid(String cSaleid) throws SQLException {
    String sSql = " select TOP 1 csourcebillbodyid from so_square_b where csaleid=? and csourcebillbodyid is not null and dr=0";

    PreparedStatement stmt = null;
    Connection con = null;
    String sCsourcebillbodyid = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql);

      stmt.setString(1, cSaleid);

      ResultSet rs = stmt.executeQuery();
      if (rs.next())
        sCsourcebillbodyid = rs.getString(1);
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
    return sCsourcebillbodyid;
  }

  private void delete(String key)
    throws SQLException
  {
    Connection m_con = null;
    PreparedStatement m_stmt = null;
    try {
      deleteRow(key);
      deleteHead(key);
    } finally {
      try {
        if (m_stmt != null)
          m_stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (m_con != null)
          m_con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  private void deleteHead(String key)
    throws SQLException
  {
    String sql = "UPDATE so_square SET dr=1 where csaleid = ?";
    Connection m_con = null;
    PreparedStatement m_stmt = null;
    try {
      m_con = getConnection();
      m_stmt = m_con.prepareStatement(sql);
      m_stmt.setString(1, key);
      m_stmt.executeUpdate();
    } finally {
      try {
        if (m_stmt != null)
          m_stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (m_con != null)
          m_con.close();
      }
      catch (Exception e)
      {
      }
    }
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

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "deleteItem", new Object[] { vo });
  }

  private void deleteRow(String key)
    throws SQLException
  {
    String sql = "UPDATE so_square_b SET dr=1 where csaleid = ?";
    Connection m_con = null;
    PreparedStatement m_stmt = null;
    try {
      m_con = getConnection();
      m_stmt = m_con.prepareStatement(sql);
      m_stmt.setString(1, key);
      m_stmt.executeUpdate();
    } finally {
      try {
        if (m_stmt != null)
          m_stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (m_con != null)
          m_con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  public SquareVO getAutoSquareOut(SquareVO voData)
    throws SQLException, BusinessException, SystemException, NamingException, Exception
  {
    SquareItemVO[] itemVOs = (SquareItemVO[])(SquareItemVO[])voData.getChildrenVO();
    SquareHeaderVO headerVO = (SquareHeaderVO)voData.getParentVO();

    String sBillType = headerVO.getCreceipttype();

    if ((sBillType != null) && (sBillType.equals("4C"))) {
      FetchValueDMO dmo = new FetchValueDMO();
      String sInnerctlDays = dmo.getColValue("bd_cumandoc", "innerctldays", " pk_cumandoc='" + headerVO.getCcustomerid() + "'");

      Integer dInnerctldays = sInnerctlDays == null ? new Integer(0) : new Integer(sInnerctlDays);

      headerVO.setDinnerctlday(headerVO.getDmakedate().getDateAfter(dInnerctldays.intValue()));
    }

    for (int i = 0; i < itemVOs.length; i++)
    {
      if ((sBillType == null ? "" : sBillType).equals("32"))
      {
        itemVOs[i] = loadInvoiceWarehouseID(itemVOs[i]);
      }

      itemVOs[i].setNnewbalancenum(itemVOs[i].getNoutnum());

      itemVOs[i] = loadDiscountLaborflag(itemVOs[i]);

      if ((itemVOs[i].getNpacknumber() == null) || (itemVOs[i].getNpacknumber().compareTo(new UFDouble(0)) == 0))
        continue;
      itemVOs[i].setScalefactor((itemVOs[i].getNoutnum() == null ? new UFDouble(0) : itemVOs[i].getNoutnum()).div(itemVOs[i].getNpacknumber()));
    }

    return voData;
  }

  protected ClientEnvironment getClientEnvironment()
  {
    ClientEnvironment m_ceSingleton = null;
    if (m_ceSingleton == null) {
      m_ceSingleton = ClientEnvironment.getInstance();
    }
    return m_ceSingleton;
  }

  private Hashtable getExistIDs(SquareVO vo, Connection con)
    throws SQLException, BusinessException, SystemException
  {
    PreparedStatement stmtCheck = null;
    Hashtable htRes = new Hashtable();
    try
    {
      String sCheckSql = "SELECT corder_bid FROM so_square_b WHERE csaleid ='" + vo.getParentVO().getPrimaryKey() + "' ";

      stmtCheck = con.prepareStatement(sCheckSql);
      ResultSet rsCheck = stmtCheck.executeQuery();
      while (rsCheck.next()) {
        String sChildID = rsCheck.getString(1);
        if (!htRes.containsKey(sChildID)) {
          htRes.put(sChildID, sChildID);
        }
      }
      rsCheck.close();
    } finally {
      try {
        if (stmtCheck != null)
          stmtCheck.close();
      }
      catch (Exception e) {
      }
    }
    return htRes;
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

  public UFDouble getSignNumber(String strID)
    throws SQLException
  {
    Connection con = null;
    PreparedStatement stmt = null;
    String sql = "SELECT ndmsignnum FROM ic_general_bb3 WHERE cgeneralbid='" + strID + "' AND dr=0";

    UFDouble dNum = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        BigDecimal nsignnum = (BigDecimal)rs.getObject(1);
        dNum = nsignnum == null ? null : new UFDouble(nsignnum);
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
    return dNum;
  }

  public SquareVO getSquareOutNumber(SquareVO voData)
    throws SQLException, BusinessException, SystemException, NamingException, Exception
  {
    UFBoolean bautoIncome = ((SquareHeaderVO)voData.getParentVO()).getBautoincomeflag();
    if ((bautoIncome == null) || (!bautoIncome.booleanValue())) {
      return voData;
    }
    String sBillType = ((SquareHeaderVO)voData.getParentVO()).getCreceipttype();

    SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])voData.getChildrenVO();
    if (!(sBillType == null ? "" : sBillType).equals("4C")) { if (!(sBillType == null ? "" : sBillType).equals("4453")); } else { for (int i = 0; i < items.length; i++) {
        if ((this.SO_05 != null) && (this.SO_05.equals("应发量"))) {
          ((SquareItemVO[])(SquareItemVO[])voData.getChildrenVO())[i].setNoutnum(items[i].getNshouldoutnum());
        }

        if ((this.SO_05 != null) && (this.SO_05.equals("签收量"))) {
          UFDouble dSignNumber = getSignNumber(items[i].getPrimaryKey());

          ((SquareItemVO[])(SquareItemVO[])voData.getChildrenVO())[i].setNoutnum(dSignNumber);
        }
      }

      getAutoSquareOut(voData);
    }

    VOCalculate voCalc = new VOCalculate();
    voData = (SquareVO)voCalc.retChangeBusiVO(voData, voData);
    return voData;
  }

  public void getSystemPara(String pk_corp)
    throws Exception
  {
    try
    {
      if (this.SO_05 == null) {
        SysInitDMO paraDMO = new SysInitDMO();
        this.SO_05 = paraDMO.getParaString(pk_corp, "SO05");
      }
    }
    catch (Exception e) {
      SCMEnv.out("系统参数获取失败!" + e.getMessage());
      throw e;
    }
  }

  public String insertHeader(SquareHeaderVO squareHeader, Connection con)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "insertHeader", new Object[] { squareHeader });

    String sql = "insert into so_square(csaleid, pk_corp, vreceiptcode, creceipttype, dbilldate, ccustomerid, cbiztype, coperatorid, ccalbodyid, cwarehouseid, dmakedate, capproveid, dapprovedate, fstatus, cdeptid, cemployeeid, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, ctermprotocolid, verifyrule, cdispatcherid,bincomeflag,bcostflag,vdef11,vdef12,vdef13,vdef14,vdef15,vdef16,vdef17,vdef18,vdef19,vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,pk_defdoc5,pk_defdoc6,pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11,pk_defdoc12,pk_defdoc13,pk_defdoc14,pk_defdoc15,pk_defdoc16,pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20,cfreecustid,bautoincomeflag,bestimation)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    String key = null;
    PreparedStatement m_stmt = null;
    try {
      m_stmt = con.prepareStatement(sql);

      key = squareHeader.getCsaleid();
      m_stmt.setString(1, key);

      if (squareHeader.getPk_corp() == null)
        m_stmt.setNull(2, 1);
      else {
        m_stmt.setString(2, squareHeader.getPk_corp());
      }
      if (squareHeader.getVreceiptcode() == null)
        m_stmt.setNull(3, 1);
      else {
        m_stmt.setString(3, squareHeader.getVreceiptcode());
      }
      if (squareHeader.getCreceipttype() == null)
        m_stmt.setNull(4, 1);
      else {
        m_stmt.setString(4, squareHeader.getCreceipttype());
      }
      if (squareHeader.getDbilldate() == null)
        m_stmt.setNull(5, 1);
      else {
        m_stmt.setString(5, squareHeader.getDbilldate().toString());
      }
      if (squareHeader.getCcustomerid() == null)
        m_stmt.setNull(6, 1);
      else {
        m_stmt.setString(6, squareHeader.getCcustomerid());
      }
      if (squareHeader.getCbiztype() == null)
        m_stmt.setNull(7, 1);
      else {
        m_stmt.setString(7, squareHeader.getCbiztype());
      }
      if (squareHeader.getCoperatorid() == null)
        m_stmt.setNull(8, 1);
      else {
        m_stmt.setString(8, squareHeader.getCoperatorid());
      }
      if (squareHeader.getCcalbodyid() == null)
        m_stmt.setNull(9, 1);
      else {
        m_stmt.setString(9, squareHeader.getCcalbodyid());
      }
      if (squareHeader.getCwarehouseid() == null)
        m_stmt.setNull(10, 1);
      else {
        m_stmt.setString(10, squareHeader.getCwarehouseid());
      }
      if (squareHeader.getDmakedate() == null)
        m_stmt.setNull(11, 1);
      else {
        m_stmt.setString(11, squareHeader.getDmakedate().toString());
      }
      if (squareHeader.getCapproveid() == null)
        m_stmt.setNull(12, 1);
      else {
        m_stmt.setString(12, squareHeader.getCapproveid());
      }
      if (squareHeader.getDapprovedate() == null)
        m_stmt.setNull(13, 1);
      else {
        m_stmt.setString(13, squareHeader.getDapprovedate().toString());
      }
      if (squareHeader.getFstatus() == null)
        m_stmt.setNull(14, 4);
      else {
        m_stmt.setInt(14, squareHeader.getFstatus().intValue());
      }
      if (squareHeader.getCdeptid() == null)
        m_stmt.setNull(15, 1);
      else {
        m_stmt.setString(15, squareHeader.getCdeptid());
      }
      if (squareHeader.getCemployeeid() == null)
        m_stmt.setNull(16, 1);
      else {
        m_stmt.setString(16, squareHeader.getCemployeeid());
      }
      if (squareHeader.getVdef1() == null)
        m_stmt.setNull(17, 1);
      else {
        m_stmt.setString(17, squareHeader.getVdef1());
      }
      if (squareHeader.getVdef2() == null)
        m_stmt.setNull(18, 1);
      else {
        m_stmt.setString(18, squareHeader.getVdef2());
      }
      if (squareHeader.getVdef3() == null)
        m_stmt.setNull(19, 1);
      else {
        m_stmt.setString(19, squareHeader.getVdef3());
      }
      if (squareHeader.getVdef4() == null)
        m_stmt.setNull(20, 1);
      else {
        m_stmt.setString(20, squareHeader.getVdef4());
      }
      if (squareHeader.getVdef5() == null)
        m_stmt.setNull(21, 1);
      else {
        m_stmt.setString(21, squareHeader.getVdef5());
      }
      if (squareHeader.getVdef6() == null)
        m_stmt.setNull(22, 1);
      else {
        m_stmt.setString(22, squareHeader.getVdef6());
      }
      if (squareHeader.getVdef7() == null)
        m_stmt.setNull(23, 1);
      else {
        m_stmt.setString(23, squareHeader.getVdef7());
      }
      if (squareHeader.getVdef8() == null)
        m_stmt.setNull(24, 1);
      else {
        m_stmt.setString(24, squareHeader.getVdef8());
      }
      if (squareHeader.getVdef9() == null)
        m_stmt.setNull(25, 1);
      else {
        m_stmt.setString(25, squareHeader.getVdef9());
      }
      if (squareHeader.getVdef10() == null)
        m_stmt.setNull(26, 1);
      else {
        m_stmt.setString(26, squareHeader.getVdef10());
      }

      if (squareHeader.getCtermprotocolid() == null)
        m_stmt.setNull(27, 1);
      else {
        m_stmt.setString(27, squareHeader.getCtermprotocolid());
      }
      if (squareHeader.getVerifyrule() == null)
        m_stmt.setNull(28, 1);
      else {
        m_stmt.setString(28, squareHeader.getVerifyrule());
      }

      if (squareHeader.getDispatcherid() == null)
        m_stmt.setNull(29, 1);
      else {
        m_stmt.setString(29, squareHeader.getDispatcherid());
      }

      if (squareHeader.getBincomeflag() == null)
        m_stmt.setNull(30, 1);
      else {
        m_stmt.setString(30, squareHeader.getBincomeflag().toString());
      }

      if (squareHeader.getBcostflag() == null)
        m_stmt.setNull(31, 1);
      else {
        m_stmt.setString(31, squareHeader.getBcostflag().toString());
      }

      if (squareHeader.getVdef11() == null)
        m_stmt.setNull(32, 1);
      else {
        m_stmt.setString(32, squareHeader.getVdef11());
      }

      if (squareHeader.getVdef12() == null)
        m_stmt.setNull(33, 1);
      else {
        m_stmt.setString(33, squareHeader.getVdef12());
      }

      if (squareHeader.getVdef13() == null)
        m_stmt.setNull(34, 1);
      else {
        m_stmt.setString(34, squareHeader.getVdef13());
      }

      if (squareHeader.getVdef14() == null)
        m_stmt.setNull(35, 1);
      else {
        m_stmt.setString(35, squareHeader.getVdef14());
      }

      if (squareHeader.getVdef15() == null)
        m_stmt.setNull(36, 1);
      else {
        m_stmt.setString(36, squareHeader.getVdef15());
      }

      if (squareHeader.getVdef16() == null)
        m_stmt.setNull(37, 1);
      else {
        m_stmt.setString(37, squareHeader.getVdef16());
      }

      if (squareHeader.getVdef17() == null)
        m_stmt.setNull(38, 1);
      else {
        m_stmt.setString(38, squareHeader.getVdef17());
      }

      if (squareHeader.getVdef18() == null)
        m_stmt.setNull(39, 1);
      else {
        m_stmt.setString(39, squareHeader.getVdef18());
      }

      if (squareHeader.getVdef19() == null)
        m_stmt.setNull(40, 1);
      else {
        m_stmt.setString(40, squareHeader.getVdef19());
      }

      if (squareHeader.getVdef20() == null)
        m_stmt.setNull(41, 1);
      else {
        m_stmt.setString(41, squareHeader.getVdef20());
      }

      if (squareHeader.getPk_defdoc1() == null)
        m_stmt.setNull(42, 1);
      else {
        m_stmt.setString(42, squareHeader.getPk_defdoc1());
      }

      if (squareHeader.getPk_defdoc2() == null)
        m_stmt.setNull(43, 1);
      else {
        m_stmt.setString(43, squareHeader.getPk_defdoc2());
      }

      if (squareHeader.getPk_defdoc3() == null)
        m_stmt.setNull(44, 1);
      else {
        m_stmt.setString(44, squareHeader.getPk_defdoc3());
      }

      if (squareHeader.getPk_defdoc4() == null)
        m_stmt.setNull(45, 1);
      else {
        m_stmt.setString(45, squareHeader.getPk_defdoc4());
      }

      if (squareHeader.getPk_defdoc5() == null)
        m_stmt.setNull(46, 1);
      else {
        m_stmt.setString(46, squareHeader.getPk_defdoc5());
      }

      if (squareHeader.getPk_defdoc6() == null)
        m_stmt.setNull(47, 1);
      else {
        m_stmt.setString(47, squareHeader.getPk_defdoc6());
      }

      if (squareHeader.getPk_defdoc7() == null)
        m_stmt.setNull(48, 1);
      else {
        m_stmt.setString(48, squareHeader.getPk_defdoc7());
      }

      if (squareHeader.getPk_defdoc8() == null)
        m_stmt.setNull(49, 1);
      else {
        m_stmt.setString(49, squareHeader.getPk_defdoc8());
      }

      if (squareHeader.getPk_defdoc9() == null)
        m_stmt.setNull(50, 1);
      else {
        m_stmt.setString(50, squareHeader.getPk_defdoc9());
      }

      if (squareHeader.getPk_defdoc10() == null)
        m_stmt.setNull(51, 1);
      else {
        m_stmt.setString(51, squareHeader.getPk_defdoc10());
      }

      if (squareHeader.getPk_defdoc11() == null)
        m_stmt.setNull(52, 1);
      else {
        m_stmt.setString(52, squareHeader.getPk_defdoc11());
      }

      if (squareHeader.getPk_defdoc12() == null)
        m_stmt.setNull(53, 1);
      else {
        m_stmt.setString(53, squareHeader.getPk_defdoc12());
      }

      if (squareHeader.getPk_defdoc13() == null)
        m_stmt.setNull(54, 1);
      else {
        m_stmt.setString(54, squareHeader.getPk_defdoc13());
      }

      if (squareHeader.getPk_defdoc14() == null)
        m_stmt.setNull(55, 1);
      else {
        m_stmt.setString(55, squareHeader.getPk_defdoc14());
      }

      if (squareHeader.getPk_defdoc15() == null)
        m_stmt.setNull(56, 1);
      else {
        m_stmt.setString(56, squareHeader.getPk_defdoc15());
      }

      if (squareHeader.getPk_defdoc16() == null)
        m_stmt.setNull(57, 1);
      else {
        m_stmt.setString(57, squareHeader.getPk_defdoc16());
      }

      if (squareHeader.getPk_defdoc17() == null)
        m_stmt.setNull(58, 1);
      else {
        m_stmt.setString(58, squareHeader.getPk_defdoc17());
      }

      if (squareHeader.getPk_defdoc18() == null)
        m_stmt.setNull(59, 1);
      else {
        m_stmt.setString(59, squareHeader.getPk_defdoc18());
      }

      if (squareHeader.getPk_defdoc19() == null)
        m_stmt.setNull(60, 1);
      else {
        m_stmt.setString(60, squareHeader.getPk_defdoc19());
      }

      if (squareHeader.getPk_defdoc20() == null)
        m_stmt.setNull(61, 1);
      else {
        m_stmt.setString(61, squareHeader.getPk_defdoc20());
      }

      if (squareHeader.getCfreecustid1() == null)
        m_stmt.setNull(62, 1);
      else {
        m_stmt.setString(62, squareHeader.getCfreecustid1());
      }
      if (squareHeader.getBautoincomeflag() == null)
        m_stmt.setString(63, "N");
      else {
        m_stmt.setString(63, squareHeader.getBautoincomeflag().toString());
      }

      if (squareHeader.getBEstimation() == null)
        m_stmt.setString(64, "N");
      else {
        m_stmt.setString(64, squareHeader.getBautoincomeflag().toString());
      }

      m_stmt.executeUpdate();
    } finally {
      try {
        if (m_stmt != null) {
          m_stmt.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "insertHeader", new Object[] { squareHeader });

    return key;
  }

  public String insertHeaderData(SquareHeaderVO squareHeader, Connection con)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "insertHeader", new Object[] { squareHeader });

    String sql = "insert into so_square(csaleid, pk_corp, vreceiptcode, creceipttype, dbilldate, ccustomerid, cbiztype, coperatorid, ccalbodyid, cwarehouseid, dmakedate, capproveid, dapprovedate, fstatus, cdeptid, cemployeeid, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, ctermprotocolid, verifyrule, cdispatcherid, cfreecustid,bautoincomeflag,bestimation) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

    String key = null;
    PreparedStatement m_stmt = null;
    try {
      m_stmt = con.prepareStatement(sql);

      key = getOID();

      m_stmt.setString(1, key);

      if (squareHeader.getPk_corp() == null)
        m_stmt.setNull(2, 1);
      else {
        m_stmt.setString(2, squareHeader.getPk_corp());
      }
      if (squareHeader.getVreceiptcode() == null)
        m_stmt.setNull(3, 1);
      else {
        m_stmt.setString(3, squareHeader.getVreceiptcode());
      }
      if (squareHeader.getCreceipttype() == null)
        m_stmt.setNull(4, 1);
      else {
        m_stmt.setString(4, squareHeader.getCreceipttype());
      }
      if (squareHeader.getDbilldate() == null)
        m_stmt.setNull(5, 1);
      else {
        m_stmt.setString(5, squareHeader.getDbilldate().toString());
      }
      if (squareHeader.getCcustomerid() == null)
        m_stmt.setNull(6, 1);
      else {
        m_stmt.setString(6, squareHeader.getCcustomerid());
      }
      if (squareHeader.getCbiztype() == null)
        m_stmt.setNull(7, 1);
      else {
        m_stmt.setString(7, squareHeader.getCbiztype());
      }
      if (squareHeader.getCoperatorid() == null)
        m_stmt.setNull(8, 1);
      else {
        m_stmt.setString(8, squareHeader.getCoperatorid());
      }
      if (squareHeader.getCcalbodyid() == null)
        m_stmt.setNull(9, 1);
      else {
        m_stmt.setString(9, squareHeader.getCcalbodyid());
      }
      if (squareHeader.getCwarehouseid() == null)
        m_stmt.setNull(10, 1);
      else {
        m_stmt.setString(10, squareHeader.getCwarehouseid());
      }
      if (squareHeader.getDmakedate() == null)
        m_stmt.setNull(11, 1);
      else {
        m_stmt.setString(11, squareHeader.getDmakedate().toString());
      }
      if (squareHeader.getCapproveid() == null)
        m_stmt.setNull(12, 1);
      else {
        m_stmt.setString(12, squareHeader.getCapproveid());
      }
      if (squareHeader.getDapprovedate() == null)
        m_stmt.setNull(13, 1);
      else {
        m_stmt.setString(13, squareHeader.getDapprovedate().toString());
      }
      if (squareHeader.getFstatus() == null)
        m_stmt.setNull(14, 4);
      else {
        m_stmt.setInt(14, squareHeader.getFstatus().intValue());
      }
      if (squareHeader.getCdeptid() == null)
        m_stmt.setNull(15, 1);
      else {
        m_stmt.setString(15, squareHeader.getCdeptid());
      }
      if (squareHeader.getCemployeeid() == null)
        m_stmt.setNull(16, 1);
      else {
        m_stmt.setString(16, squareHeader.getCemployeeid());
      }
      if (squareHeader.getVdef1() == null)
        m_stmt.setNull(17, 1);
      else {
        m_stmt.setString(17, squareHeader.getVdef1());
      }
      if (squareHeader.getVdef2() == null)
        m_stmt.setNull(18, 1);
      else {
        m_stmt.setString(18, squareHeader.getVdef2());
      }
      if (squareHeader.getVdef3() == null)
        m_stmt.setNull(19, 1);
      else {
        m_stmt.setString(19, squareHeader.getVdef3());
      }
      if (squareHeader.getVdef4() == null)
        m_stmt.setNull(20, 1);
      else {
        m_stmt.setString(20, squareHeader.getVdef4());
      }
      if (squareHeader.getVdef5() == null)
        m_stmt.setNull(21, 1);
      else {
        m_stmt.setString(21, squareHeader.getVdef5());
      }
      if (squareHeader.getVdef6() == null)
        m_stmt.setNull(22, 1);
      else {
        m_stmt.setString(22, squareHeader.getVdef6());
      }
      if (squareHeader.getVdef7() == null)
        m_stmt.setNull(23, 1);
      else {
        m_stmt.setString(23, squareHeader.getVdef7());
      }
      if (squareHeader.getVdef8() == null)
        m_stmt.setNull(24, 1);
      else {
        m_stmt.setString(24, squareHeader.getVdef8());
      }
      if (squareHeader.getVdef9() == null)
        m_stmt.setNull(25, 1);
      else {
        m_stmt.setString(25, squareHeader.getVdef9());
      }
      if (squareHeader.getVdef10() == null)
        m_stmt.setNull(26, 1);
      else {
        m_stmt.setString(26, squareHeader.getVdef10());
      }

      if (squareHeader.getCtermprotocolid() == null)
        m_stmt.setNull(27, 1);
      else {
        m_stmt.setString(27, squareHeader.getCtermprotocolid());
      }
      if (squareHeader.getVerifyrule() == null)
        m_stmt.setNull(28, 1);
      else {
        m_stmt.setString(28, squareHeader.getVerifyrule());
      }

      if (squareHeader.getDispatcherid() == null)
        m_stmt.setNull(29, 1);
      else {
        m_stmt.setString(29, squareHeader.getDispatcherid());
      }

      if (squareHeader.getCfreecustid1() == null)
        m_stmt.setNull(30, 1);
      else {
        m_stmt.setString(30, squareHeader.getCfreecustid1());
      }
      if (squareHeader.getBautoincomeflag() == null)
        m_stmt.setString(31, "N");
      else {
        m_stmt.setString(31, squareHeader.getBautoincomeflag().toString());
      }

      if (squareHeader.getBEstimation() == null)
        m_stmt.setString(32, "N");
      else {
        m_stmt.setString(32, squareHeader.getBautoincomeflag().toString());
      }

      m_stmt.executeUpdate();
    } finally {
      try {
        if (m_stmt != null) {
          m_stmt.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "insertHeader", new Object[] { squareHeader });

    return key;
  }

  public String insertItem(SquareItemVO squareItem, String foreignKey, Connection con)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "insertItem", new Object[] { squareItem, foreignKey });

    squareItem.setCsaleid(foreignKey);
    String key = insertItem(squareItem, con);

    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "insertItem", new Object[] { squareItem, foreignKey });

    return key;
  }

  public String insertItem(SquareItemVO squareItem, Connection con)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "insertItem", new Object[] { squareItem });

    String sql = "insert into so_square_b(corder_bid, csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, creceipttype, cupreceipttype, cupbillid, cupbillbodyid, cbodywarehouseid, cpackunitid, scalefactor, nbalancemny, ncostmny, blargessflag, discountflag, laborflag, ndiscountmny, noriginalcurdiscountmny, nnetprice, ntaxnetprice, dbizdate  ,cbodycalbodyid ,npacknumber,cprolineid,nreturntaxrate, vdef7,vdef8,vdef9,vdef10,vdef11,vdef12,vdef13,vdef14,vdef15,vdef16,vdef17,vdef18,vdef19,vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,pk_defdoc5,pk_defdoc6,pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11,pk_defdoc12,pk_defdoc13,pk_defdoc14,pk_defdoc15,pk_defdoc16,pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20,cquoteunitid,nquoteunitnum,nquoteunitrate,nqttaxnetprc,nqtnetprc,nqttaxprc,nqtprc,norgqttaxnetprc,norgqtnetprc,norgqttaxprc,norgqtprc,pk_corp)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    String key = null;
    PreparedStatement m_stmt = null;
    try {
      m_stmt = con.prepareStatement(sql);

      key = squareItem.getCorder_bid();
      m_stmt.setString(1, key);

      if (squareItem.getCsaleid() == null)
        m_stmt.setNull(2, 1);
      else {
        m_stmt.setString(2, squareItem.getCsaleid());
      }
      if (squareItem.getCsourcebillid() == null)
        m_stmt.setNull(3, 1);
      else {
        m_stmt.setString(3, squareItem.getCsourcebillid());
      }
      if (squareItem.getCsourcebillbodyid() == null)
        m_stmt.setNull(4, 1);
      else {
        m_stmt.setString(4, squareItem.getCsourcebillbodyid());
      }
      if (squareItem.getCinvbasdocid() == null)
        m_stmt.setNull(5, 1);
      else {
        m_stmt.setString(5, squareItem.getCinvbasdocid());
      }
      if (squareItem.getCinventoryid() == null)
        m_stmt.setNull(6, 1);
      else {
        m_stmt.setString(6, squareItem.getCinventoryid());
      }
      if (squareItem.getNoutnum() == null)
        m_stmt.setNull(7, 4);
      else {
        m_stmt.setBigDecimal(7, squareItem.getNoutnum().toBigDecimal());
      }
      if (squareItem.getNshouldoutnum() == null)
        m_stmt.setNull(8, 4);
      else {
        m_stmt.setBigDecimal(8, squareItem.getNshouldoutnum().toBigDecimal());
      }

      if (squareItem.getNbalancenum() == null)
        m_stmt.setNull(9, 4);
      else {
        m_stmt.setBigDecimal(9, squareItem.getNbalancenum().toBigDecimal());
      }

      if (squareItem.getNsignnum() == null)
        m_stmt.setNull(10, 4);
      else {
        m_stmt.setBigDecimal(10, squareItem.getNsignnum().toBigDecimal());
      }

      if (squareItem.getCcurrencytypeid() == null)
        m_stmt.setNull(11, 1);
      else {
        m_stmt.setString(11, squareItem.getCcurrencytypeid());
      }
      if (squareItem.getNoriginalcurtaxnetprice() == null)
        m_stmt.setNull(12, 4);
      else {
        m_stmt.setBigDecimal(12, squareItem.getNoriginalcurtaxnetprice().toBigDecimal());
      }

      if (squareItem.getNoriginalcurmny() == null)
        m_stmt.setNull(13, 4);
      else {
        m_stmt.setBigDecimal(13, squareItem.getNoriginalcurmny().toBigDecimal());
      }

      if (squareItem.getNoriginalcursummny() == null)
        m_stmt.setNull(14, 4);
      else {
        m_stmt.setBigDecimal(14, squareItem.getNoriginalcursummny().toBigDecimal());
      }

      if (squareItem.getBifpaybalance() == null)
        m_stmt.setNull(15, 1);
      else {
        m_stmt.setString(15, squareItem.getBifpaybalance().toString());
      }
      if (squareItem.getCbatchid() == null)
        m_stmt.setNull(16, 1);
      else {
        m_stmt.setString(16, squareItem.getCbatchid());
      }
      if (squareItem.getNexchangeotobrate() == null)
        m_stmt.setNull(17, 4);
      else {
        m_stmt.setBigDecimal(17, squareItem.getNexchangeotobrate().toBigDecimal());
      }

      if (squareItem.getNexchangeotoarate() == null)
        m_stmt.setNull(18, 4);
      else {
        m_stmt.setBigDecimal(18, squareItem.getNexchangeotoarate().toBigDecimal());
      }

      if (squareItem.getNtaxrate() == null)
        m_stmt.setNull(19, 4);
      else {
        m_stmt.setBigDecimal(19, squareItem.getNtaxrate().toBigDecimal());
      }

      if (squareItem.getNoriginalcurnetprice() == null)
        m_stmt.setNull(20, 4);
      else {
        m_stmt.setBigDecimal(20, squareItem.getNoriginalcurnetprice().toBigDecimal());
      }

      if (squareItem.getNoriginalcurtaxmny() == null)
        m_stmt.setNull(21, 4);
      else {
        m_stmt.setBigDecimal(21, squareItem.getNoriginalcurtaxmny().toBigDecimal());
      }

      if (squareItem.getNtaxmny() == null)
        m_stmt.setNull(22, 4);
      else {
        m_stmt.setBigDecimal(22, squareItem.getNtaxmny().toBigDecimal());
      }

      if (squareItem.getNmny() == null)
        m_stmt.setNull(23, 4);
      else {
        m_stmt.setBigDecimal(23, squareItem.getNmny().toBigDecimal());
      }
      if (squareItem.getNsummny() == null)
        m_stmt.setNull(24, 4);
      else {
        m_stmt.setBigDecimal(24, squareItem.getNsummny().toBigDecimal());
      }

      if (squareItem.getNassistcursummny() == null)
        m_stmt.setNull(25, 4);
      else {
        m_stmt.setBigDecimal(25, squareItem.getNassistcursummny().toBigDecimal());
      }

      if (squareItem.getNassistcurmny() == null)
        m_stmt.setNull(26, 4);
      else {
        m_stmt.setBigDecimal(26, squareItem.getNassistcurmny().toBigDecimal());
      }

      if (squareItem.getNassistcurtaxmny() == null)
        m_stmt.setNull(27, 4);
      else {
        m_stmt.setBigDecimal(27, squareItem.getNassistcurtaxmny().toBigDecimal());
      }

      if (squareItem.getCprojectid() == null)
        m_stmt.setNull(28, 1);
      else {
        m_stmt.setString(28, squareItem.getCprojectid());
      }
      if (squareItem.getCprojectphaseid() == null)
        m_stmt.setNull(29, 1);
      else {
        m_stmt.setString(29, squareItem.getCprojectphaseid());
      }
      if (squareItem.getVfree1() == null)
        m_stmt.setNull(30, 1);
      else {
        m_stmt.setString(30, squareItem.getVfree1());
      }
      if (squareItem.getVfree2() == null)
        m_stmt.setNull(31, 1);
      else {
        m_stmt.setString(31, squareItem.getVfree2());
      }
      if (squareItem.getVfree3() == null)
        m_stmt.setNull(32, 1);
      else {
        m_stmt.setString(32, squareItem.getVfree3());
      }
      if (squareItem.getVfree4() == null)
        m_stmt.setNull(33, 1);
      else {
        m_stmt.setString(33, squareItem.getVfree4());
      }
      if (squareItem.getVfree5() == null)
        m_stmt.setNull(34, 1);
      else {
        m_stmt.setString(34, squareItem.getVfree5());
      }
      if (squareItem.getVdef1() == null)
        m_stmt.setNull(35, 1);
      else {
        m_stmt.setString(35, squareItem.getVdef1());
      }
      if (squareItem.getVdef2() == null)
        m_stmt.setNull(36, 1);
      else {
        m_stmt.setString(36, squareItem.getVdef2());
      }
      if (squareItem.getVdef3() == null)
        m_stmt.setNull(37, 1);
      else {
        m_stmt.setString(37, squareItem.getVdef3());
      }
      if (squareItem.getVdef4() == null)
        m_stmt.setNull(38, 1);
      else {
        m_stmt.setString(38, squareItem.getVdef4());
      }
      if (squareItem.getVdef5() == null)
        m_stmt.setNull(39, 1);
      else {
        m_stmt.setString(39, squareItem.getVdef5());
      }
      if (squareItem.getVdef6() == null)
        m_stmt.setNull(40, 1);
      else {
        m_stmt.setString(40, squareItem.getVdef6());
      }

      if (squareItem.getCreceipttype() == null)
        m_stmt.setNull(41, 1);
      else {
        m_stmt.setString(41, squareItem.getCreceipttype());
      }

      if (squareItem.getCupreceipttype() == null)
        m_stmt.setNull(42, 1);
      else {
        m_stmt.setString(42, squareItem.getCupreceipttype());
      }

      if (squareItem.getCupbillid() == null)
        m_stmt.setNull(43, 1);
      else {
        m_stmt.setString(43, squareItem.getCupbillid());
      }

      if (squareItem.getCupbillbodyid() == null)
        m_stmt.setNull(44, 1);
      else {
        m_stmt.setString(44, squareItem.getCupbillbodyid());
      }

      if (squareItem.getCbodywarehouseid() == null)
        m_stmt.setNull(45, 1);
      else {
        m_stmt.setString(45, squareItem.getCbodywarehouseid());
      }

      if (squareItem.getCpackunitid() == null)
        m_stmt.setNull(46, 1);
      else {
        m_stmt.setString(46, squareItem.getCpackunitid());
      }

      if (squareItem.getScalefactor() == null)
        m_stmt.setNull(47, 4);
      else {
        m_stmt.setBigDecimal(47, squareItem.getScalefactor().toBigDecimal());
      }

      if (squareItem.getNbalancemny() == null)
        m_stmt.setNull(48, 4);
      else {
        m_stmt.setBigDecimal(48, squareItem.getNbalancemny().toBigDecimal());
      }

      if (squareItem.getNcostmny() == null)
        m_stmt.setNull(49, 4);
      else {
        m_stmt.setBigDecimal(49, squareItem.getNcostmny().toBigDecimal());
      }

      if (squareItem.getBlargessflag() == null)
        m_stmt.setNull(50, 1);
      else {
        m_stmt.setString(50, squareItem.getBlargessflag().toString());
      }

      if (squareItem.getDiscountflag() == null)
        m_stmt.setNull(51, 1);
      else {
        m_stmt.setString(51, squareItem.getDiscountflag().toString());
      }

      if (squareItem.getLaborflag() == null)
        m_stmt.setNull(52, 1);
      else {
        m_stmt.setString(52, squareItem.getLaborflag().toString());
      }

      if (squareItem.getNdiscountmny() == null)
        m_stmt.setNull(53, 4);
      else {
        m_stmt.setBigDecimal(53, squareItem.getNdiscountmny().toBigDecimal());
      }

      if (squareItem.getNoriginalcurdiscountmny() == null)
        m_stmt.setNull(54, 4);
      else {
        m_stmt.setBigDecimal(54, squareItem.getNoriginalcurdiscountmny().toBigDecimal());
      }

      if (squareItem.getNnetprice() == null)
        m_stmt.setNull(55, 4);
      else {
        m_stmt.setBigDecimal(55, squareItem.getNnetprice().toBigDecimal());
      }

      if (squareItem.getNtaxnetprice() == null)
        m_stmt.setNull(56, 4);
      else {
        m_stmt.setBigDecimal(56, squareItem.getNtaxnetprice().toBigDecimal());
      }

      if (squareItem.getDbizdate() == null)
        m_stmt.setNull(57, 1);
      else {
        m_stmt.setString(57, squareItem.getDbizdate().toString());
      }

      if (squareItem.getCbodycalbodyid() == null)
        m_stmt.setNull(58, 1);
      else {
        m_stmt.setString(58, squareItem.getCbodycalbodyid());
      }

      if (squareItem.getAttributeValue("npacknumber") == null)
        m_stmt.setNull(59, 3);
      else {
        m_stmt.setBigDecimal(59, ((UFDouble)squareItem.getAttributeValue("npacknumber")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("cprolineid") == null)
        m_stmt.setNull(60, 1);
      else {
        m_stmt.setString(60, squareItem.getAttributeValue("cprolineid").toString());
      }

      if (squareItem.getAttributeValue("nreturntaxrate") == null)
        m_stmt.setNull(61, 3);
      else {
        m_stmt.setBigDecimal(61, squareItem.getNreturntaxrate().toBigDecimal());
      }

      if (squareItem.getVdef7() == null)
        m_stmt.setNull(62, 1);
      else {
        m_stmt.setString(62, squareItem.getVdef7());
      }

      if (squareItem.getVdef8() == null)
        m_stmt.setNull(63, 1);
      else {
        m_stmt.setString(63, squareItem.getVdef8());
      }

      if (squareItem.getVdef9() == null)
        m_stmt.setNull(64, 1);
      else {
        m_stmt.setString(64, squareItem.getVdef9());
      }

      if (squareItem.getVdef10() == null)
        m_stmt.setNull(65, 1);
      else {
        m_stmt.setString(65, squareItem.getVdef10());
      }

      if (squareItem.getVdef11() == null)
        m_stmt.setNull(66, 1);
      else {
        m_stmt.setString(66, squareItem.getVdef11());
      }

      if (squareItem.getVdef12() == null)
        m_stmt.setNull(67, 1);
      else {
        m_stmt.setString(67, squareItem.getVdef12());
      }

      if (squareItem.getVdef13() == null)
        m_stmt.setNull(68, 1);
      else {
        m_stmt.setString(68, squareItem.getVdef13());
      }

      if (squareItem.getVdef14() == null)
        m_stmt.setNull(69, 1);
      else {
        m_stmt.setString(69, squareItem.getVdef14());
      }

      if (squareItem.getVdef15() == null)
        m_stmt.setNull(70, 1);
      else {
        m_stmt.setString(70, squareItem.getVdef15());
      }

      if (squareItem.getVdef16() == null)
        m_stmt.setNull(71, 1);
      else {
        m_stmt.setString(71, squareItem.getVdef16());
      }

      if (squareItem.getVdef17() == null)
        m_stmt.setNull(72, 1);
      else {
        m_stmt.setString(72, squareItem.getVdef17());
      }

      if (squareItem.getVdef18() == null)
        m_stmt.setNull(73, 1);
      else {
        m_stmt.setString(73, squareItem.getVdef18());
      }

      if (squareItem.getVdef19() == null)
        m_stmt.setNull(74, 1);
      else {
        m_stmt.setString(74, squareItem.getVdef19());
      }

      if (squareItem.getVdef20() == null)
        m_stmt.setNull(75, 1);
      else {
        m_stmt.setString(75, squareItem.getVdef20());
      }

      if (squareItem.getPk_defdoc1() == null)
        m_stmt.setNull(76, 1);
      else {
        m_stmt.setString(76, squareItem.getPk_defdoc1());
      }

      if (squareItem.getPk_defdoc2() == null)
        m_stmt.setNull(77, 1);
      else {
        m_stmt.setString(77, squareItem.getPk_defdoc2());
      }

      if (squareItem.getPk_defdoc3() == null)
        m_stmt.setNull(78, 1);
      else {
        m_stmt.setString(78, squareItem.getPk_defdoc3());
      }

      if (squareItem.getPk_defdoc4() == null)
        m_stmt.setNull(79, 1);
      else {
        m_stmt.setString(79, squareItem.getPk_defdoc4());
      }

      if (squareItem.getPk_defdoc5() == null)
        m_stmt.setNull(80, 1);
      else {
        m_stmt.setString(80, squareItem.getPk_defdoc5());
      }

      if (squareItem.getPk_defdoc6() == null)
        m_stmt.setNull(81, 1);
      else {
        m_stmt.setString(81, squareItem.getPk_defdoc6());
      }

      if (squareItem.getPk_defdoc7() == null)
        m_stmt.setNull(82, 1);
      else {
        m_stmt.setString(82, squareItem.getPk_defdoc7());
      }

      if (squareItem.getPk_defdoc8() == null)
        m_stmt.setNull(83, 1);
      else {
        m_stmt.setString(83, squareItem.getPk_defdoc8());
      }

      if (squareItem.getPk_defdoc9() == null)
        m_stmt.setNull(84, 1);
      else {
        m_stmt.setString(84, squareItem.getPk_defdoc9());
      }

      if (squareItem.getPk_defdoc10() == null)
        m_stmt.setNull(85, 1);
      else {
        m_stmt.setString(85, squareItem.getPk_defdoc10());
      }

      if (squareItem.getPk_defdoc11() == null)
        m_stmt.setNull(86, 1);
      else {
        m_stmt.setString(86, squareItem.getPk_defdoc11());
      }

      if (squareItem.getPk_defdoc12() == null)
        m_stmt.setNull(87, 1);
      else {
        m_stmt.setString(87, squareItem.getPk_defdoc12());
      }

      if (squareItem.getPk_defdoc13() == null)
        m_stmt.setNull(88, 1);
      else {
        m_stmt.setString(88, squareItem.getPk_defdoc13());
      }

      if (squareItem.getPk_defdoc14() == null)
        m_stmt.setNull(89, 1);
      else {
        m_stmt.setString(89, squareItem.getPk_defdoc14());
      }

      if (squareItem.getPk_defdoc15() == null)
        m_stmt.setNull(90, 1);
      else {
        m_stmt.setString(90, squareItem.getPk_defdoc15());
      }

      if (squareItem.getPk_defdoc16() == null)
        m_stmt.setNull(91, 1);
      else {
        m_stmt.setString(91, squareItem.getPk_defdoc16());
      }

      if (squareItem.getPk_defdoc17() == null)
        m_stmt.setNull(92, 1);
      else {
        m_stmt.setString(92, squareItem.getPk_defdoc17());
      }

      if (squareItem.getPk_defdoc18() == null)
        m_stmt.setNull(93, 1);
      else {
        m_stmt.setString(93, squareItem.getPk_defdoc18());
      }

      if (squareItem.getPk_defdoc19() == null)
        m_stmt.setNull(94, 1);
      else {
        m_stmt.setString(94, squareItem.getPk_defdoc19());
      }

      if (squareItem.getPk_defdoc20() == null)
        m_stmt.setNull(95, 1);
      else {
        m_stmt.setString(95, squareItem.getPk_defdoc20());
      }

      if (squareItem.getCquoteunitid() == null)
        m_stmt.setNull(96, 1);
      else {
        m_stmt.setString(96, squareItem.getCquoteunitid());
      }

      if (squareItem.getAttributeValue("nquoteunitnum") == null)
        m_stmt.setNull(97, 3);
      else {
        m_stmt.setBigDecimal(97, ((UFDouble)squareItem.getAttributeValue("nquoteunitnum")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nquoteunitrate") == null)
        m_stmt.setNull(98, 3);
      else {
        m_stmt.setBigDecimal(98, ((UFDouble)squareItem.getAttributeValue("nquoteunitrate")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nqttaxnetprc") == null)
        m_stmt.setNull(99, 3);
      else {
        m_stmt.setBigDecimal(99, ((UFDouble)squareItem.getAttributeValue("nqttaxnetprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nqtnetprc") == null)
        m_stmt.setNull(100, 3);
      else {
        m_stmt.setBigDecimal(100, ((UFDouble)squareItem.getAttributeValue("nqtnetprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nqttaxprc") == null)
        m_stmt.setNull(101, 3);
      else {
        m_stmt.setBigDecimal(101, ((UFDouble)squareItem.getAttributeValue("nqttaxprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nqtprc") == null)
        m_stmt.setNull(102, 3);
      else {
        m_stmt.setBigDecimal(102, ((UFDouble)squareItem.getAttributeValue("nqtprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("norgqttaxnetprc") == null)
        m_stmt.setNull(103, 3);
      else {
        m_stmt.setBigDecimal(103, ((UFDouble)squareItem.getAttributeValue("norgqttaxnetprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("norgqtnetprc") == null)
        m_stmt.setNull(104, 3);
      else {
        m_stmt.setBigDecimal(104, ((UFDouble)squareItem.getAttributeValue("norgqtnetprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("norgqttaxprc") == null)
        m_stmt.setNull(105, 3);
      else {
        m_stmt.setBigDecimal(105, ((UFDouble)squareItem.getAttributeValue("norgqttaxprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("norgqtprc") == null)
        m_stmt.setNull(106, 3);
      else {
        m_stmt.setBigDecimal(106, ((UFDouble)squareItem.getAttributeValue("norgqtprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("pk_corp") == null)
        m_stmt.setNull(107, 1);
      else {
        m_stmt.setString(107, squareItem.getPk_corp());
      }

      m_stmt.executeUpdate();
    } finally {
      try {
        if (m_stmt != null) {
          m_stmt.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "insertItem", new Object[] { squareItem });

    return key;
  }

  public String insertItems(SquareItemVO[] squareItem, String sKey, Connection con)
    throws SQLException, SystemException
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "insertItem", new Object[] { squareItem });

    String sql = "insert into so_square_b(corder_bid, csaleid, csourcebillid, csourcebillbodyid, cinvbasdocid, cinventoryid, noutnum, nshouldoutnum, nbalancenum, nsignnum, ccurrencytypeid, noriginalcurtaxnetprice, noriginalcurmny, noriginalcursummny, bifpaybalance, cbatchid, nexchangeotobrate, nexchangeotoarate, ntaxrate, noriginalcurnetprice, noriginalcurtaxmny, ntaxmny, nmny, nsummny, nassistcursummny, nassistcurmny, nassistcurtaxmny, cprojectid, cprojectphaseid, vfree1, vfree2, vfree3, vfree4, vfree5, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, creceipttype, cupreceipttype, cupbillid, cupbillbodyid, cbodywarehouseid, cpackunitid, scalefactor, nbalancemny, ncostmny, blargessflag, discountflag, laborflag, ndiscountmny, noriginalcurdiscountmny, nnetprice, ntaxnetprice, dbizdate  ,cbodycalbodyid ,npacknumber,cprolineid,nreturntaxrate, vdef7,vdef8,vdef9,vdef10,vdef11,vdef12,vdef13,vdef14,vdef15,vdef16,vdef17,vdef18,vdef19,vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,pk_defdoc5,pk_defdoc6,pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11,pk_defdoc12,pk_defdoc13,pk_defdoc14,pk_defdoc15,pk_defdoc16,pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20,cquoteunitid,nquoteunitnum,nquoteunitrate,nqttaxnetprc,nqtnetprc,nqttaxprc,nqtprc,norgqttaxnetprc,norgqtnetprc,norgqttaxprc,norgqtprc,pk_corp)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    String key = null;
    PreparedStatement m_stmt = null;
    try {
      if ((squareItem != null) && (squareItem.length != 0)) {
        for (int i = 0; i < squareItem.length; i++) {
          if (sKey != null) {
            squareItem[i].setCsaleid(sKey);
          }
          if (m_stmt == null) {
            m_stmt = prepareStatement(con, sql);
          }

          key = squareItem[i].getCorder_bid();
          m_stmt.setString(1, key);

          if (squareItem[i].getCsaleid() == null)
            m_stmt.setNull(2, 1);
          else {
            m_stmt.setString(2, squareItem[i].getCsaleid());
          }
          if (squareItem[i].getCsourcebillid() == null)
            m_stmt.setNull(3, 1);
          else {
            m_stmt.setString(3, squareItem[i].getCsourcebillid());
          }
          if (squareItem[i].getCsourcebillbodyid() == null)
            m_stmt.setNull(4, 1);
          else {
            m_stmt.setString(4, squareItem[i].getCsourcebillbodyid());
          }

          if (squareItem[i].getCinvbasdocid() == null)
            m_stmt.setNull(5, 1);
          else {
            m_stmt.setString(5, squareItem[i].getCinvbasdocid());
          }
          if (squareItem[i].getCinventoryid() == null)
            m_stmt.setNull(6, 1);
          else {
            m_stmt.setString(6, squareItem[i].getCinventoryid());
          }
          if (squareItem[i].getNoutnum() == null)
            m_stmt.setNull(7, 4);
          else {
            m_stmt.setBigDecimal(7, squareItem[i].getNoutnum().toBigDecimal());
          }

          if (squareItem[i].getNshouldoutnum() == null)
            m_stmt.setNull(8, 4);
          else {
            m_stmt.setBigDecimal(8, squareItem[i].getNshouldoutnum().toBigDecimal());
          }

          if (squareItem[i].getNbalancenum() == null)
            m_stmt.setNull(9, 4);
          else {
            m_stmt.setBigDecimal(9, squareItem[i].getNbalancenum().toBigDecimal());
          }

          if (squareItem[i].getNsignnum() == null)
            m_stmt.setNull(10, 4);
          else {
            m_stmt.setBigDecimal(10, squareItem[i].getNsignnum().toBigDecimal());
          }

          if (squareItem[i].getCcurrencytypeid() == null)
            m_stmt.setNull(11, 1);
          else {
            m_stmt.setString(11, squareItem[i].getCcurrencytypeid());
          }

          if (squareItem[i].getNoriginalcurtaxnetprice() == null)
            m_stmt.setNull(12, 4);
          else {
            m_stmt.setBigDecimal(12, squareItem[i].getNoriginalcurtaxnetprice().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcurmny() == null)
            m_stmt.setNull(13, 4);
          else {
            m_stmt.setBigDecimal(13, squareItem[i].getNoriginalcurmny().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcursummny() == null)
            m_stmt.setNull(14, 4);
          else {
            m_stmt.setBigDecimal(14, squareItem[i].getNoriginalcursummny().toBigDecimal());
          }

          if (squareItem[i].getBifpaybalance() == null)
            m_stmt.setNull(15, 1);
          else {
            m_stmt.setString(15, squareItem[i].getBifpaybalance().toString());
          }

          if (squareItem[i].getCbatchid() == null)
            m_stmt.setNull(16, 1);
          else {
            m_stmt.setString(16, squareItem[i].getCbatchid());
          }
          if (squareItem[i].getNexchangeotobrate() == null)
            m_stmt.setNull(17, 4);
          else {
            m_stmt.setBigDecimal(17, squareItem[i].getNexchangeotobrate().toBigDecimal());
          }

          if (squareItem[i].getNexchangeotoarate() == null)
            m_stmt.setNull(18, 4);
          else {
            m_stmt.setBigDecimal(18, squareItem[i].getNexchangeotoarate().toBigDecimal());
          }

          if (squareItem[i].getNtaxrate() == null)
            m_stmt.setNull(19, 4);
          else {
            m_stmt.setBigDecimal(19, squareItem[i].getNtaxrate().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcurnetprice() == null)
            m_stmt.setNull(20, 4);
          else {
            m_stmt.setBigDecimal(20, squareItem[i].getNoriginalcurnetprice().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcurtaxmny() == null)
            m_stmt.setNull(21, 4);
          else {
            m_stmt.setBigDecimal(21, squareItem[i].getNoriginalcurtaxmny().toBigDecimal());
          }

          if (squareItem[i].getNtaxmny() == null)
            m_stmt.setNull(22, 4);
          else {
            m_stmt.setBigDecimal(22, squareItem[i].getNtaxmny().toBigDecimal());
          }

          if (squareItem[i].getNmny() == null)
            m_stmt.setNull(23, 4);
          else {
            m_stmt.setBigDecimal(23, squareItem[i].getNmny().toBigDecimal());
          }

          if (squareItem[i].getNsummny() == null)
            m_stmt.setNull(24, 4);
          else {
            m_stmt.setBigDecimal(24, squareItem[i].getNsummny().toBigDecimal());
          }

          if (squareItem[i].getNassistcursummny() == null)
            m_stmt.setNull(25, 4);
          else {
            m_stmt.setBigDecimal(25, squareItem[i].getNassistcursummny().toBigDecimal());
          }

          if (squareItem[i].getNassistcurmny() == null)
            m_stmt.setNull(26, 4);
          else {
            m_stmt.setBigDecimal(26, squareItem[i].getNassistcurmny().toBigDecimal());
          }

          if (squareItem[i].getNassistcurtaxmny() == null)
            m_stmt.setNull(27, 4);
          else {
            m_stmt.setBigDecimal(27, squareItem[i].getNassistcurtaxmny().toBigDecimal());
          }

          if (squareItem[i].getCprojectid() == null)
            m_stmt.setNull(28, 1);
          else {
            m_stmt.setString(28, squareItem[i].getCprojectid());
          }
          if (squareItem[i].getCprojectphaseid() == null)
            m_stmt.setNull(29, 1);
          else {
            m_stmt.setString(29, squareItem[i].getCprojectphaseid());
          }

          if (squareItem[i].getVfree1() == null)
            m_stmt.setNull(30, 1);
          else {
            m_stmt.setString(30, squareItem[i].getVfree1());
          }
          if (squareItem[i].getVfree2() == null)
            m_stmt.setNull(31, 1);
          else {
            m_stmt.setString(31, squareItem[i].getVfree2());
          }
          if (squareItem[i].getVfree3() == null)
            m_stmt.setNull(32, 1);
          else {
            m_stmt.setString(32, squareItem[i].getVfree3());
          }
          if (squareItem[i].getVfree4() == null)
            m_stmt.setNull(33, 1);
          else {
            m_stmt.setString(33, squareItem[i].getVfree4());
          }
          if (squareItem[i].getVfree5() == null)
            m_stmt.setNull(34, 1);
          else {
            m_stmt.setString(34, squareItem[i].getVfree5());
          }
          if (squareItem[i].getVdef1() == null)
            m_stmt.setNull(35, 1);
          else {
            m_stmt.setString(35, squareItem[i].getVdef1());
          }
          if (squareItem[i].getVdef2() == null)
            m_stmt.setNull(36, 1);
          else {
            m_stmt.setString(36, squareItem[i].getVdef2());
          }
          if (squareItem[i].getVdef3() == null)
            m_stmt.setNull(37, 1);
          else {
            m_stmt.setString(37, squareItem[i].getVdef3());
          }
          if (squareItem[i].getVdef4() == null)
            m_stmt.setNull(38, 1);
          else {
            m_stmt.setString(38, squareItem[i].getVdef4());
          }
          if (squareItem[i].getVdef5() == null)
            m_stmt.setNull(39, 1);
          else {
            m_stmt.setString(39, squareItem[i].getVdef5());
          }
          if (squareItem[i].getVdef6() == null)
            m_stmt.setNull(40, 1);
          else {
            m_stmt.setString(40, squareItem[i].getVdef6());
          }

          if (squareItem[i].getCreceipttype() == null)
            m_stmt.setNull(41, 1);
          else {
            m_stmt.setString(41, squareItem[i].getCreceipttype());
          }

          if (squareItem[i].getCupreceipttype() == null)
            m_stmt.setNull(42, 1);
          else {
            m_stmt.setString(42, squareItem[i].getCupreceipttype());
          }

          if (squareItem[i].getCupbillid() == null)
            m_stmt.setNull(43, 1);
          else {
            m_stmt.setString(43, squareItem[i].getCupbillid());
          }

          if (squareItem[i].getCupbillbodyid() == null)
            m_stmt.setNull(44, 1);
          else {
            m_stmt.setString(44, squareItem[i].getCupbillbodyid());
          }

          if (squareItem[i].getCbodywarehouseid() == null)
            m_stmt.setNull(45, 1);
          else {
            m_stmt.setString(45, squareItem[i].getCbodywarehouseid());
          }

          if (squareItem[i].getCpackunitid() == null)
            m_stmt.setNull(46, 1);
          else {
            m_stmt.setString(46, squareItem[i].getCpackunitid());
          }

          if (squareItem[i].getScalefactor() == null)
            m_stmt.setNull(47, 4);
          else {
            m_stmt.setBigDecimal(47, squareItem[i].getScalefactor().toBigDecimal());
          }

          if (squareItem[i].getNbalancemny() == null)
            m_stmt.setNull(48, 4);
          else {
            m_stmt.setBigDecimal(48, squareItem[i].getNbalancemny().toBigDecimal());
          }

          if (squareItem[i].getNcostmny() == null)
            m_stmt.setNull(49, 4);
          else {
            m_stmt.setBigDecimal(49, squareItem[i].getNcostmny().toBigDecimal());
          }

          if (squareItem[i].getBlargessflag() == null)
            m_stmt.setNull(50, 1);
          else {
            m_stmt.setString(50, squareItem[i].getBlargessflag().toString());
          }

          if (squareItem[i].getDiscountflag() == null)
            m_stmt.setNull(51, 1);
          else {
            m_stmt.setString(51, squareItem[i].getDiscountflag().toString());
          }

          if (squareItem[i].getLaborflag() == null)
            m_stmt.setNull(52, 1);
          else {
            m_stmt.setString(52, squareItem[i].getLaborflag().toString());
          }

          if (squareItem[i].getNdiscountmny() == null)
            m_stmt.setNull(53, 4);
          else {
            m_stmt.setBigDecimal(53, squareItem[i].getNdiscountmny().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcurdiscountmny() == null)
            m_stmt.setNull(54, 4);
          else {
            m_stmt.setBigDecimal(54, squareItem[i].getNoriginalcurdiscountmny().toBigDecimal());
          }

          if (squareItem[i].getNnetprice() == null)
            m_stmt.setNull(55, 4);
          else {
            m_stmt.setBigDecimal(55, squareItem[i].getNnetprice().toBigDecimal());
          }

          if (squareItem[i].getNtaxnetprice() == null)
            m_stmt.setNull(56, 4);
          else {
            m_stmt.setBigDecimal(56, squareItem[i].getNtaxnetprice().toBigDecimal());
          }

          if (squareItem[i].getDbizdate() == null)
            m_stmt.setNull(57, 1);
          else {
            m_stmt.setString(57, squareItem[i].getDbizdate().toString());
          }

          if (squareItem[i].getCbodycalbodyid() == null)
            m_stmt.setNull(58, 1);
          else {
            m_stmt.setString(58, squareItem[i].getCbodycalbodyid());
          }

          if (squareItem[i].getAttributeValue("npacknumber") == null)
            m_stmt.setNull(59, 3);
          else {
            m_stmt.setBigDecimal(59, ((UFDouble)squareItem[i].getAttributeValue("npacknumber")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("cprolineid") == null)
            m_stmt.setNull(60, 1);
          else {
            m_stmt.setString(60, squareItem[i].getAttributeValue("cprolineid").toString());
          }

          if (squareItem[i].getAttributeValue("nreturntaxrate") == null)
            m_stmt.setNull(61, 3);
          else {
            m_stmt.setBigDecimal(61, squareItem[i].getNreturntaxrate().toBigDecimal());
          }

          if (squareItem[i].getVdef7() == null)
            m_stmt.setNull(62, 1);
          else {
            m_stmt.setString(62, squareItem[i].getVdef7());
          }

          if (squareItem[i].getVdef8() == null)
            m_stmt.setNull(63, 1);
          else {
            m_stmt.setString(63, squareItem[i].getVdef8());
          }

          if (squareItem[i].getVdef9() == null)
            m_stmt.setNull(64, 1);
          else {
            m_stmt.setString(64, squareItem[i].getVdef9());
          }

          if (squareItem[i].getVdef10() == null)
            m_stmt.setNull(65, 1);
          else {
            m_stmt.setString(65, squareItem[i].getVdef10());
          }

          if (squareItem[i].getVdef11() == null)
            m_stmt.setNull(66, 1);
          else {
            m_stmt.setString(66, squareItem[i].getVdef11());
          }

          if (squareItem[i].getVdef12() == null)
            m_stmt.setNull(67, 1);
          else {
            m_stmt.setString(67, squareItem[i].getVdef12());
          }

          if (squareItem[i].getVdef13() == null)
            m_stmt.setNull(68, 1);
          else {
            m_stmt.setString(68, squareItem[i].getVdef13());
          }

          if (squareItem[i].getVdef14() == null)
            m_stmt.setNull(69, 1);
          else {
            m_stmt.setString(69, squareItem[i].getVdef14());
          }

          if (squareItem[i].getVdef15() == null)
            m_stmt.setNull(70, 1);
          else {
            m_stmt.setString(70, squareItem[i].getVdef15());
          }

          if (squareItem[i].getVdef16() == null)
            m_stmt.setNull(71, 1);
          else {
            m_stmt.setString(71, squareItem[i].getVdef16());
          }

          if (squareItem[i].getVdef17() == null)
            m_stmt.setNull(72, 1);
          else {
            m_stmt.setString(72, squareItem[i].getVdef17());
          }

          if (squareItem[i].getVdef18() == null)
            m_stmt.setNull(73, 1);
          else {
            m_stmt.setString(73, squareItem[i].getVdef18());
          }

          if (squareItem[i].getVdef19() == null)
            m_stmt.setNull(74, 1);
          else {
            m_stmt.setString(74, squareItem[i].getVdef19());
          }

          if (squareItem[i].getVdef20() == null)
            m_stmt.setNull(75, 1);
          else {
            m_stmt.setString(75, squareItem[i].getVdef20());
          }

          if (squareItem[i].getPk_defdoc1() == null)
            m_stmt.setNull(76, 1);
          else {
            m_stmt.setString(76, squareItem[i].getPk_defdoc1());
          }

          if (squareItem[i].getPk_defdoc2() == null)
            m_stmt.setNull(77, 1);
          else {
            m_stmt.setString(77, squareItem[i].getPk_defdoc2());
          }

          if (squareItem[i].getPk_defdoc3() == null)
            m_stmt.setNull(78, 1);
          else {
            m_stmt.setString(78, squareItem[i].getPk_defdoc3());
          }

          if (squareItem[i].getPk_defdoc4() == null)
            m_stmt.setNull(79, 1);
          else {
            m_stmt.setString(79, squareItem[i].getPk_defdoc4());
          }

          if (squareItem[i].getPk_defdoc5() == null)
            m_stmt.setNull(80, 1);
          else {
            m_stmt.setString(80, squareItem[i].getPk_defdoc5());
          }

          if (squareItem[i].getPk_defdoc6() == null)
            m_stmt.setNull(81, 1);
          else {
            m_stmt.setString(81, squareItem[i].getPk_defdoc6());
          }

          if (squareItem[i].getPk_defdoc7() == null)
            m_stmt.setNull(82, 1);
          else {
            m_stmt.setString(82, squareItem[i].getPk_defdoc7());
          }

          if (squareItem[i].getPk_defdoc8() == null)
            m_stmt.setNull(83, 1);
          else {
            m_stmt.setString(83, squareItem[i].getPk_defdoc8());
          }

          if (squareItem[i].getPk_defdoc9() == null)
            m_stmt.setNull(84, 1);
          else {
            m_stmt.setString(84, squareItem[i].getPk_defdoc9());
          }

          if (squareItem[i].getPk_defdoc10() == null)
            m_stmt.setNull(85, 1);
          else {
            m_stmt.setString(85, squareItem[i].getPk_defdoc10());
          }

          if (squareItem[i].getPk_defdoc11() == null)
            m_stmt.setNull(86, 1);
          else {
            m_stmt.setString(86, squareItem[i].getPk_defdoc11());
          }

          if (squareItem[i].getPk_defdoc12() == null)
            m_stmt.setNull(87, 1);
          else {
            m_stmt.setString(87, squareItem[i].getPk_defdoc12());
          }

          if (squareItem[i].getPk_defdoc13() == null)
            m_stmt.setNull(88, 1);
          else {
            m_stmt.setString(88, squareItem[i].getPk_defdoc13());
          }

          if (squareItem[i].getPk_defdoc14() == null)
            m_stmt.setNull(89, 1);
          else {
            m_stmt.setString(89, squareItem[i].getPk_defdoc14());
          }

          if (squareItem[i].getPk_defdoc15() == null)
            m_stmt.setNull(90, 1);
          else {
            m_stmt.setString(90, squareItem[i].getPk_defdoc15());
          }

          if (squareItem[i].getPk_defdoc16() == null)
            m_stmt.setNull(91, 1);
          else {
            m_stmt.setString(91, squareItem[i].getPk_defdoc16());
          }

          if (squareItem[i].getPk_defdoc17() == null)
            m_stmt.setNull(92, 1);
          else {
            m_stmt.setString(92, squareItem[i].getPk_defdoc17());
          }

          if (squareItem[i].getPk_defdoc18() == null)
            m_stmt.setNull(93, 1);
          else {
            m_stmt.setString(93, squareItem[i].getPk_defdoc18());
          }

          if (squareItem[i].getPk_defdoc19() == null)
            m_stmt.setNull(94, 1);
          else {
            m_stmt.setString(94, squareItem[i].getPk_defdoc19());
          }

          if (squareItem[i].getPk_defdoc20() == null)
            m_stmt.setNull(95, 1);
          else {
            m_stmt.setString(95, squareItem[i].getPk_defdoc20());
          }

          if (squareItem[i].getCquoteunitid() == null)
            m_stmt.setNull(96, 1);
          else {
            m_stmt.setString(96, squareItem[i].getCquoteunitid());
          }

          if (squareItem[i].getAttributeValue("nquoteunitnum") == null)
            m_stmt.setNull(97, 3);
          else {
            m_stmt.setBigDecimal(97, ((UFDouble)squareItem[i].getAttributeValue("nquoteunitnum")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nquoteunitrate") == null)
            m_stmt.setNull(98, 3);
          else {
            m_stmt.setBigDecimal(98, ((UFDouble)squareItem[i].getAttributeValue("nquoteunitrate")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nqttaxnetprc") == null)
            m_stmt.setNull(99, 3);
          else {
            m_stmt.setBigDecimal(99, ((UFDouble)squareItem[i].getAttributeValue("nqttaxnetprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nqtnetprc") == null)
            m_stmt.setNull(100, 3);
          else {
            m_stmt.setBigDecimal(100, ((UFDouble)squareItem[i].getAttributeValue("nqtnetprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nqttaxprc") == null)
            m_stmt.setNull(101, 3);
          else {
            m_stmt.setBigDecimal(101, ((UFDouble)squareItem[i].getAttributeValue("nqttaxprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nqtprc") == null)
            m_stmt.setNull(102, 3);
          else {
            m_stmt.setBigDecimal(102, ((UFDouble)squareItem[i].getAttributeValue("nqtprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("norgqttaxnetprc") == null)
            m_stmt.setNull(103, 3);
          else {
            m_stmt.setBigDecimal(103, ((UFDouble)squareItem[i].getAttributeValue("norgqttaxnetprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("norgqtnetprc") == null)
            m_stmt.setNull(104, 3);
          else {
            m_stmt.setBigDecimal(104, ((UFDouble)squareItem[i].getAttributeValue("norgqtnetprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("norgqttaxprc") == null)
            m_stmt.setNull(105, 3);
          else {
            m_stmt.setBigDecimal(105, ((UFDouble)squareItem[i].getAttributeValue("norgqttaxprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("norgqtprc") == null)
            m_stmt.setNull(106, 3);
          else {
            m_stmt.setBigDecimal(106, ((UFDouble)squareItem[i].getAttributeValue("norgqtprc")).toBigDecimal());
          }

          if (squareItem[i].getPk_corp() == null)
            m_stmt.setNull(107, 1);
          else {
            m_stmt.setString(107, squareItem[i].getPk_corp());
          }

          executeUpdate(m_stmt);
        }

      }

      if (m_stmt != null)
        executeBatch(m_stmt);
    } finally {
      try {
        if (m_stmt != null) {
          m_stmt.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "insertItem", new Object[] { squareItem });

    return key;
  }

  public String insertSquare(Hashtable hcsaleid, SquareVO vo)
    throws SQLException, BusinessException, SystemException, Exception
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceCheck", new Object[] { vo });

    String key = null;
    Connection m_con = null;
    PreparedStatement stmtCheck = null;
    try {
      String sPkCsaleid = ((SquareHeaderVO)vo.getParentVO()).getCsaleid();

      VOCalculate voCalc = new VOCalculate();
      vo = (SquareVO)voCalc.retChangeBusiVO(vo, vo);

      m_con = getConnection();

      if (!hcsaleid.containsKey(sPkCsaleid))
      {
        key = insertHeaderData((SquareHeaderVO)vo.getParentVO(), m_con);

        SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
        for (int i = 0; i < items.length; i++)
          insertItem(items[i], key, m_con);
      }
      else
      {
        update(vo, m_con);
      }
    } finally {
      try {
        if (stmtCheck != null)
          stmtCheck.close();
      }
      catch (Exception e) {
      }
      try {
        if (m_con != null) {
          m_con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceCheck", new Object[] { vo });

    return key;
  }

  public String insertSquare(SquareVO vo)
    throws SQLException, BusinessException, SystemException, Exception
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceCheck", new Object[] { vo });

    String key = null;
    Connection m_con = null;
    PreparedStatement stmtCheck = null;
    try
    {
      VOCalculate voCalc = new VOCalculate();
      vo = (SquareVO)voCalc.retChangeBusiVO(vo, vo);

      m_con = getConnection();

      key = insertHeader((SquareHeaderVO)vo.getParentVO(), m_con);

      SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
      for (int i = 0; i < items.length; i++)
        insertItem(items[i], key, m_con);
    }
    finally
    {
      try {
        if (stmtCheck != null)
          stmtCheck.close();
      }
      catch (Exception e) {
      }
      try {
        if (m_con != null) {
          m_con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceCheck", new Object[] { vo });

    return key;
  }

  private boolean isValueValidity(String tablename, String fieldname, String filedvalue)
    throws SQLException, BusinessException, SystemException
  {
    PreparedStatement stmtCheck = null;
    Connection m_con = null;

    boolean isValue = false;
    try
    {
      String sCheckSql = "select " + fieldname + " from " + tablename + " where " + fieldname + "='" + filedvalue + "' ";

      m_con = getConnection();
      stmtCheck = m_con.prepareStatement(sCheckSql);
      ResultSet rsCheck = stmtCheck.executeQuery();
      if (rsCheck.next())
        isValue = true;
    }
    finally {
      try {
        if (stmtCheck != null)
          stmtCheck.close();
      }
      catch (Exception e) {
      }
      try {
        if (m_con != null)
          m_con.close();
      }
      catch (Exception e) {
      }
    }
    return isValue;
  }

  public SquareItemVO loadDiscountLaborflag(SquareItemVO voItem)
    throws SQLException, BusinessException, SystemException
  {
    String cinvbasdocid = voItem.getCinvbasdocid();
    PreparedStatement stmt = null;
    Connection con = null;
    try
    {
      String sql = "select discountflag, laborflag from bd_invbasdoc where pk_invbasdoc='" + cinvbasdocid + "' ";

      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        String discountflag = rs.getString("discountflag");
        voItem.setDiscountflag(discountflag == null ? null : new UFBoolean(discountflag.trim()));

        String laborflag = rs.getString("laborflag");
        voItem.setLaborflag(laborflag == null ? null : new UFBoolean(laborflag.trim()));
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
    return voItem;
  }

  public SquareItemVO loadInvoiceWarehouseID(SquareItemVO voItem)
    throws SQLException, BusinessException, SystemException
  {
    String idItem = voItem.getPrimaryKey();
    PreparedStatement stmt = null;
    Connection con = null;
    try
    {
      String sql = "select cbodywarehouseid, ncostmny from so_saleinvoice_b where cinvoice_bid='" + idItem + "' ";

      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        String cbodywarehouseid = rs.getString("cbodywarehouseid");
        voItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());

        BigDecimal ncostmny = (BigDecimal)rs.getObject("ncostmny");
        voItem.setNcostmny(ncostmny == null ? null : new UFDouble(ncostmny));
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
    return voItem;
  }

  public SquareItemVO loadOutUpID(SquareItemVO voItem)
    throws SQLException, BusinessException, SystemException
  {
    String cinvbasdocid = voItem.getCinvbasdocid();
    PreparedStatement stmt = null;
    Connection con = null;
    try
    {
      String sql = "SELECT csourcetype, csourcebillhid, csourcebillbid FROM ic_general_b WHERE cgeneralbid = ?";
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, voItem.getPrimaryKey());
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        String csourcetype = rs.getString("csourcetype");
        voItem.setCupreceipttype(csourcetype == null ? null : csourcetype.trim());

        String csourcebillhid = rs.getString("csourcebillhid");
        voItem.setCupbillid(csourcebillhid == null ? null : csourcebillhid.trim());

        String csourcebillbid = rs.getString("csourcebillbid");
        voItem.setCupbillbodyid(csourcebillbid == null ? null : csourcebillbid.trim());
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
    return voItem;
  }

  public Hashtable queryOldSquare()
    throws SQLException
  {
    String sql = "select csaleid from so_square where dr=0";

    Hashtable hCaleid = new Hashtable();

    SquareItemVO squareItem = null;

    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next())
        hCaleid.put(rs.getObject(1), "");
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
    return hCaleid;
  }

  public void setAfterInvoiceAbandonCheck(String sPKBill)
    throws SQLException, BusinessException, SystemException
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceAbandonCheck", new Object[] { sPKBill });

    setAfterOutAbandonCheck(sPKBill);
  }

  public String setAfterInvoiceCheck(SquareVO vo)
    throws SQLException, BusinessException, SystemException, Exception
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceCheck", new Object[] { vo });

    String key = null;
    PreparedStatement stmtCheck = null;
    Connection m_con = null;
    try {
      String sPkCsaleid = ((SquareHeaderVO)vo.getParentVO()).getCsaleid();

      String sCheckSql = "select csaleid from so_square where csaleid='" + sPkCsaleid + "' ";

      m_con = getConnection();
      stmtCheck = m_con.prepareStatement(sCheckSql);
      ResultSet rsCheck = stmtCheck.executeQuery();
      if (!rsCheck.next()) {
        stmtCheck.close();

        key = insertHeader((SquareHeaderVO)vo.getParentVO(), m_con);

        SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
        for (int i = 0; i < items.length; i++)
          insertItem(items[i], key, m_con);
      }
      else {
        stmtCheck.close();
        update(vo, m_con);
      }
    } finally {
      try {
        if (stmtCheck != null)
          stmtCheck.close();
      }
      catch (Exception e) {
      }
      try {
        if (m_con != null) {
          m_con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceCheck", new Object[] { vo });

    return key;
  }

  public void setAfterOutAbandonCheck(String sPKBill)
    throws SQLException, BusinessException, SystemException
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceAbandonCheck", new Object[] { sPKBill });

    PreparedStatement stmtCheck = null;
    boolean bHaveBalance = false;
    Connection m_con = null;
    try {
      ISquare bo = (ISquare)NCLocator.getInstance().lookup(ISquare.class.getName());

      SquareTotalVO[] VOs = null;
      SquareVO[] squareVOs = null;
      SquareVO tmpVO = null;
      try {
        SquareDMO dmo = new SquareDMO();
        VOs = (SquareTotalVO[])(SquareTotalVO[])dmo.queryTatalDataUnBal(" sd.csaleid= '" + sPKBill + "' and sd.dr=0 ");
        squareVOs = bo.changeTotalToSquareVO(VOs);
        boolean bautoincomeflag = false;
        if (squareVOs != null) {
          tmpVO = squareVOs[0];
          if ((tmpVO.getParentVO().getAttributeValue("bautoincomeflag") != null) && (
            (tmpVO.getParentVO().getAttributeValue("bautoincomeflag").toString().equals("Y")) || (new UFBoolean(tmpVO.getParentVO().getAttributeValue("bautoincomeflag").toString()).booleanValue()))) {
            PfUtilBO pfbo = null;
            bautoincomeflag = true;

            if (squareVOs.length > 1)
            {
              ArrayList list = new ArrayList();
              HashMap map = new HashMap();
              for (int i = 0; i < squareVOs.length; i++) {
                SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])squareVOs[i].getChildrenVO();
                for (int j = 0; j < items.length; j++) {
                  if (map.containsKey(items[j].getPrimaryKey()))
                    continue;
                  list.add(items[j]);
                  map.put(items[j].getPrimaryKey(), items[j].getPrimaryKey());
                }

              }

              SquareItemVO[] bodys = new SquareItemVO[list.size()];
              list.toArray(bodys);
              tmpVO.setChildrenVO(bodys);
            }

            pfbo = new PfUtilBO();
            pfbo.processAction("UnSoSquare", "33", tmpVO.getParentVO().getAttributeValue("dmakedate") == null ? new UFDate(System.currentTimeMillis()).toString() : tmpVO.getParentVO().getAttributeValue("dmakedate").toString(), null, tmpVO, null);
          }

          if (!bautoincomeflag) {
            for (int i = 0; i < tmpVO.getChildrenVO().length; i++) {
              UFDouble dTemp = (UFDouble)(UFDouble)tmpVO.getChildrenVO()[i].getAttributeValue("nbalancenum");
              if ((dTemp == null) || (dTemp.doubleValue() == 0.0D))
                continue;
              bHaveBalance = true;
              break;
            }

          }

        }

        if (!bHaveBalance)
          delete(sPKBill);
      }
      catch (Exception e) {
        reportException(e);
        throw new BusinessException(e.getMessage());
      }

    }
    finally
    {
      try
      {
        if (stmtCheck != null)
          stmtCheck.close();
      }
      catch (Exception e) {
      }
      try {
        if (m_con != null) {
          m_con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceAbandonCheck", new Object[] { sPKBill });

    if (bHaveBalance) {
      BusinessException ex = new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000034"));

      throw ex;
    }
  }

  public String setAfterOutCheck(SquareVO vo)
    throws SQLException, BusinessException, SystemException, Exception
  {
    beforeCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceCheck", new Object[] { vo });

    String key = null;
    Connection m_con = null;
    PreparedStatement stmtCheck = null;
    try {
      SquareHeaderVO hvo = (SquareHeaderVO)vo.getParentVO();
      String sPkCsaleid = hvo.getCsaleid();
      String sCorp = hvo.getPk_corp();

      getSystemPara(sCorp);

      vo = getSquareOutNumber(vo);

      vo = setReTaxRate(vo);

      String sCheckSql = "select csaleid from so_square where csaleid='" + sPkCsaleid + "' ";

      m_con = getConnection();
      stmtCheck = m_con.prepareStatement(sCheckSql);
      ResultSet rsCheck = stmtCheck.executeQuery();
      if (!rsCheck.next()) {
        stmtCheck.close();
        VOCalculate voCalc = new VOCalculate();
        vo = (SquareVO)voCalc.retChangeBusiVO(vo, vo);

        key = insertHeader((SquareHeaderVO)vo.getParentVO(), m_con);

        SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
        insertItems(items, key, m_con);
      }
      else {
        stmtCheck.close();
        update(vo, m_con);
      }
    } finally {
      try {
        if (stmtCheck != null)
          stmtCheck.close();
      }
      catch (Exception e) {
      }
      try {
        if (m_con != null) {
          m_con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.so012.SquareInputDMO", "setAfterInvoiceCheck", new Object[] { vo });

    return key;
  }

  public SquareVO setReTaxRate(SquareVO vo)
  {
    SquareItemVO[] itemVO = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();

    String[] sSorbodyid = new String[itemVO.length];

    Vector vTemp = new Vector();
    for (int i = 0; i < itemVO.length; i++) {
      if (itemVO[i].getCsourcebillbodyid() != null) {
        vTemp.addElement(itemVO[i].getCsourcebillbodyid());
      }
    }

    vTemp.copyInto(sSorbodyid);

    Object[] oRate = null;
    try
    {
      String sql = " select corder_bid,nreturntaxrate from so_saleorder_b where 1=1 " + GeneralSqlString.formInSQL("corder_bid", sSorbodyid);

      SmartDMO sdmo = new SmartDMO();
      Object[] o = sdmo.selectBy2(sql);
      Hashtable ht = new Hashtable();
      if ((o != null) && (o.length > 0)) {
        Object[] o1 = null;
        for (int i = 0; i < o.length; i++) {
          o1 = (Object[])(Object[])o[i];
          if ((o1 != null) && (o1.length > 0) && (o1[1] != null))
            ht.put(o1[0], o1[1]);
        }
      }
      oRate = new Object[sSorbodyid.length];
      for (int i = 0; i < sSorbodyid.length; i++)
        if ((sSorbodyid[i] != null) && (ht.containsKey(sSorbodyid[i])))
          oRate[i] = ht.get(sSorbodyid[i]);
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
    for (int i = 0; i < itemVO.length; i++) {
      itemVO[i].setAttributeValue("nreturntaxrate", oRate[i] == null ? null : new UFDouble(oRate[i].toString()));
    }

    vo.setChildrenVO(itemVO);

    return vo;
  }

  public void update(SquareVO vo, Connection con)
    throws SQLException, BusinessException, SystemException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "update", new Object[] { vo });

    VOCalculate voCalc = new VOCalculate();
    vo = (SquareVO)voCalc.retChangeBusiVO(vo, vo);

    SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])vo.getChildrenVO();
    Hashtable htExistID = getExistIDs(vo, con);
    Vector vUpdateItems = new Vector();
    Vector vInsertItems = new Vector();

    for (int i = 0; i < items.length; i++) {
      if (htExistID.containsKey(items[i].getPrimaryKey())) {
        vUpdateItems.addElement(items[i]);
      }
      else
      {
        vInsertItems.addElement(items[i]);
      }
    }

    if ((vUpdateItems != null) && (vUpdateItems.size() != 0)) {
      SquareItemVO[] updateitems = new SquareItemVO[vUpdateItems.size()];
      vUpdateItems.copyInto(updateitems);
      updateItems(updateitems, con);
    }
    if ((vInsertItems != null) && (vInsertItems.size() != 0)) {
      SquareItemVO[] insertitems = new SquareItemVO[vInsertItems.size()];
      vInsertItems.copyInto(insertitems);
      insertItems(insertitems, null, con);
    }
    updateHeader((SquareHeaderVO)vo.getParentVO(), con);

    afterCallMethod("nc.bs.so.altertable.SquareDMO", "update", new Object[] { vo });
  }

  public void updateHeader(SquareHeaderVO squareHeader, Connection con)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "updateHeader", new Object[] { squareHeader });

    String sql = "update so_square set dr = 0, pk_corp = ?, vreceiptcode = ?, creceipttype = ?, dbilldate = ?, ccustomerid = ?, cbiztype = ?, coperatorid = ?, ccalbodyid = ?, cwarehouseid = ?, dmakedate = ?, capproveid = ?, dapprovedate = ?, fstatus = ?, cdeptid = ?, cemployeeid = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, vdef7 = ?, vdef8 = ?, vdef9 = ?, vdef10 = ?, ctermprotocolid = ?, verifyrule = ?, cdispatcherid = ? ,bincomeflag = ? ,bcostflag = ?,  vdef11 = ?,vdef12 = ?,vdef13 = ?,vdef14 = ?,vdef15 = ?,vdef16 = ?,vdef17 = ?,vdef18 = ?,vdef19 = ?,vdef20 = ?,pk_defdoc1=?,pk_defdoc2=?,pk_defdoc3=?,pk_defdoc4=?,pk_defdoc5=?,pk_defdoc6=?,pk_defdoc7=?,pk_defdoc8=?,pk_defdoc9=?,pk_defdoc10=?,pk_defdoc11=?,pk_defdoc12=?,pk_defdoc13=?,pk_defdoc14=?,pk_defdoc15=?,pk_defdoc16=?,pk_defdoc17=?,pk_defdoc18=?,pk_defdoc19=?,pk_defdoc20=?,cfreecustid = ?,bautoincomeflag = ?,bestimation=?  where csaleid = ?";

    PreparedStatement stmt = null;
    try {
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

      if (squareHeader.getCtermprotocolid() == null)
        stmt.setNull(26, 1);
      else {
        stmt.setString(26, squareHeader.getCtermprotocolid());
      }
      if (squareHeader.getVerifyrule() == null)
        stmt.setNull(27, 1);
      else {
        stmt.setString(27, squareHeader.getVerifyrule());
      }
      if (squareHeader.getDispatcherid() == null)
        stmt.setNull(28, 1);
      else {
        stmt.setString(28, squareHeader.getDispatcherid());
      }
      if (squareHeader.getBincomeflag() == null)
        stmt.setNull(29, 1);
      else {
        stmt.setString(29, squareHeader.getBincomeflag().toString());
      }
      if (squareHeader.getBcostflag() == null)
        stmt.setNull(30, 1);
      else {
        stmt.setString(30, squareHeader.getBcostflag().toString());
      }

      if (squareHeader.getVdef11() == null)
        stmt.setNull(31, 1);
      else {
        stmt.setString(31, squareHeader.getVdef11());
      }

      if (squareHeader.getVdef12() == null)
        stmt.setNull(32, 1);
      else {
        stmt.setString(32, squareHeader.getVdef12());
      }

      if (squareHeader.getVdef13() == null)
        stmt.setNull(33, 1);
      else {
        stmt.setString(33, squareHeader.getVdef13());
      }

      if (squareHeader.getVdef14() == null)
        stmt.setNull(34, 1);
      else {
        stmt.setString(34, squareHeader.getVdef14());
      }

      if (squareHeader.getVdef15() == null)
        stmt.setNull(35, 1);
      else {
        stmt.setString(35, squareHeader.getVdef15());
      }

      if (squareHeader.getVdef16() == null)
        stmt.setNull(36, 1);
      else {
        stmt.setString(36, squareHeader.getVdef16());
      }

      if (squareHeader.getVdef17() == null)
        stmt.setNull(37, 1);
      else {
        stmt.setString(37, squareHeader.getVdef17());
      }

      if (squareHeader.getVdef18() == null)
        stmt.setNull(38, 1);
      else {
        stmt.setString(38, squareHeader.getVdef18());
      }

      if (squareHeader.getVdef19() == null)
        stmt.setNull(39, 1);
      else {
        stmt.setString(39, squareHeader.getVdef19());
      }

      if (squareHeader.getVdef20() == null)
        stmt.setNull(40, 1);
      else {
        stmt.setString(40, squareHeader.getVdef20());
      }

      if (squareHeader.getPk_defdoc1() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, squareHeader.getPk_defdoc1());
      }

      if (squareHeader.getPk_defdoc2() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, squareHeader.getPk_defdoc2());
      }

      if (squareHeader.getPk_defdoc3() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, squareHeader.getPk_defdoc3());
      }

      if (squareHeader.getPk_defdoc4() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, squareHeader.getPk_defdoc4());
      }

      if (squareHeader.getPk_defdoc5() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, squareHeader.getPk_defdoc5());
      }

      if (squareHeader.getPk_defdoc6() == null)
        stmt.setNull(46, 1);
      else {
        stmt.setString(46, squareHeader.getPk_defdoc6());
      }

      if (squareHeader.getPk_defdoc7() == null)
        stmt.setNull(47, 1);
      else {
        stmt.setString(47, squareHeader.getPk_defdoc7());
      }

      if (squareHeader.getPk_defdoc8() == null)
        stmt.setNull(48, 1);
      else {
        stmt.setString(48, squareHeader.getPk_defdoc8());
      }

      if (squareHeader.getPk_defdoc9() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, squareHeader.getPk_defdoc9());
      }

      if (squareHeader.getPk_defdoc10() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, squareHeader.getPk_defdoc10());
      }

      if (squareHeader.getPk_defdoc11() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, squareHeader.getPk_defdoc11());
      }

      if (squareHeader.getPk_defdoc12() == null)
        stmt.setNull(52, 1);
      else {
        stmt.setString(52, squareHeader.getPk_defdoc12());
      }

      if (squareHeader.getPk_defdoc13() == null)
        stmt.setNull(53, 1);
      else {
        stmt.setString(53, squareHeader.getPk_defdoc13());
      }

      if (squareHeader.getPk_defdoc14() == null)
        stmt.setNull(54, 1);
      else {
        stmt.setString(54, squareHeader.getPk_defdoc14());
      }

      if (squareHeader.getPk_defdoc15() == null)
        stmt.setNull(55, 1);
      else {
        stmt.setString(55, squareHeader.getPk_defdoc15());
      }

      if (squareHeader.getPk_defdoc16() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, squareHeader.getPk_defdoc16());
      }

      if (squareHeader.getPk_defdoc17() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, squareHeader.getPk_defdoc17());
      }

      if (squareHeader.getPk_defdoc18() == null)
        stmt.setNull(58, 1);
      else {
        stmt.setString(58, squareHeader.getPk_defdoc18());
      }

      if (squareHeader.getPk_defdoc19() == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, squareHeader.getPk_defdoc19());
      }

      if (squareHeader.getPk_defdoc20() == null)
        stmt.setNull(60, 1);
      else {
        stmt.setString(60, squareHeader.getPk_defdoc20());
      }
      if (squareHeader.getCfreecustid1() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, squareHeader.getCfreecustid1());
      }
      if (squareHeader.getBautoincomeflag() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, squareHeader.getBautoincomeflag().toString());
      }

      if (squareHeader.getBEstimation() == null)
        stmt.setString(63, "N");
      else {
        stmt.setString(63, squareHeader.getBautoincomeflag().toString());
      }

      stmt.setString(64, squareHeader.getPrimaryKey());

      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.altertable.SquareDMO", "updateHeader", new Object[] { squareHeader });
  }

  public void updateItem(SquareItemVO squareItem, Connection con)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "updateItem", new Object[] { squareItem });

    String sql = "update so_square_b set dr = ?, csourcebillid = ?, csourcebillbodyid = ?, cinvbasdocid = ?, cinventoryid = ?, noutnum = ?, nshouldoutnum = ?, nbalancenum = ?, nsignnum = ?, ccurrencytypeid = ?, noriginalcurtaxnetprice = ?, noriginalcurmny = ?, noriginalcursummny = ?, bifpaybalance = ?, cbatchid = ?, nexchangeotobrate = ?, nexchangeotoarate = ?, ntaxrate = ?, noriginalcurnetprice = ?, noriginalcurtaxmny = ?, ntaxmny = ?, nmny = ?, nsummny = ?, nassistcursummny = ?, nassistcurmny = ?, nassistcurtaxmny = ?, cprojectid = ?, cprojectphaseid = ?, vfree1 = ?, vfree2 = ?, vfree3 = ?, vfree4 = ?, vfree5 = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, creceipttype = ?, cupreceipttype = ?, cupbillid = ?, cupbillbodyid = ?, cbodywarehouseid =?, cpackunitid = ?, scalefactor = ?, nbalancemny = ?, ncostmny = ?, blargessflag = ?, discountflag = ?, laborflag = ?, ndiscountmny = ? , noriginalcurdiscountmny = ?, nnetprice = ?, ntaxnetprice = ?, dbizdate = ?, cbodycalbodyid=? , npacknumber=? ,cprolineid=?, nreturntaxrate=?, vdef7 =?,vdef8 =?,vdef9 =?,vdef10 =?,vdef11 =?,vdef12 =?,vdef13 =?,vdef14 =?,vdef15 =?,vdef16 =?, vdef17 =?,vdef18 =?,vdef19 =?,vdef20 =?, pk_defdoc1=?,pk_defdoc2=?,pk_defdoc3=?,pk_defdoc4=?,pk_defdoc5=?,pk_defdoc6=?,pk_defdoc7=?,pk_defdoc8=?,pk_defdoc9=?,pk_defdoc10=?,pk_defdoc11=?,pk_defdoc12=?,pk_defdoc13=?,pk_defdoc14=?,pk_defdoc15=?,pk_defdoc16=?,pk_defdoc17=?,pk_defdoc18=?,pk_defdoc19=?,pk_defdoc20=?,cquoteunitid=?,nquoteunitnum=?,nquoteunitrate=?,nqttaxnetprc=?,nqtnetprc=?,nqttaxprc=?,nqtprc=?,norgqttaxnetprc=?,norgqtnetprc=?,norgqttaxprc=?,norgqtprc=?,pk_corp=? where corder_bid = ?";

    PreparedStatement stmt = null;
    try {
      stmt = con.prepareStatement(sql);

      stmt.setInt(1, 0);
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

      if (squareItem.getCupreceipttype() == null)
        stmt.setNull(41, 1);
      else {
        stmt.setString(41, squareItem.getCupreceipttype());
      }

      if (squareItem.getCupbillid() == null)
        stmt.setNull(42, 1);
      else {
        stmt.setString(42, squareItem.getCupbillid());
      }

      if (squareItem.getCupbillbodyid() == null)
        stmt.setNull(43, 1);
      else {
        stmt.setString(43, squareItem.getCupbillbodyid());
      }

      if (squareItem.getCbodywarehouseid() == null)
        stmt.setNull(44, 1);
      else {
        stmt.setString(44, squareItem.getCbodywarehouseid());
      }

      if (squareItem.getCpackunitid() == null)
        stmt.setNull(45, 1);
      else {
        stmt.setString(45, squareItem.getCpackunitid());
      }

      if (squareItem.getScalefactor() == null)
        stmt.setNull(46, 4);
      else {
        stmt.setBigDecimal(46, squareItem.getScalefactor().toBigDecimal());
      }

      if (squareItem.getNbalancemny() == null)
        stmt.setNull(47, 4);
      else {
        stmt.setBigDecimal(47, squareItem.getNbalancemny().toBigDecimal());
      }

      if (squareItem.getNcostmny() == null)
        stmt.setNull(48, 4);
      else {
        stmt.setBigDecimal(48, squareItem.getNcostmny().toBigDecimal());
      }

      if (squareItem.getBlargessflag() == null)
        stmt.setNull(49, 1);
      else {
        stmt.setString(49, squareItem.getBlargessflag().toString());
      }

      if (squareItem.getDiscountflag() == null)
        stmt.setNull(50, 1);
      else {
        stmt.setString(50, squareItem.getDiscountflag().toString());
      }

      if (squareItem.getLaborflag() == null)
        stmt.setNull(51, 1);
      else {
        stmt.setString(51, squareItem.getLaborflag().toString());
      }

      if (squareItem.getNdiscountmny() == null)
        stmt.setNull(52, 4);
      else {
        stmt.setBigDecimal(52, squareItem.getNdiscountmny().toBigDecimal());
      }

      if (squareItem.getNoriginalcurdiscountmny() == null)
        stmt.setNull(53, 4);
      else {
        stmt.setBigDecimal(53, squareItem.getNoriginalcurdiscountmny().toBigDecimal());
      }

      if (squareItem.getNnetprice() == null)
        stmt.setNull(54, 4);
      else {
        stmt.setBigDecimal(54, squareItem.getNnetprice().toBigDecimal());
      }

      if (squareItem.getNtaxnetprice() == null)
        stmt.setNull(55, 4);
      else {
        stmt.setBigDecimal(55, squareItem.getNtaxnetprice().toBigDecimal());
      }

      if (squareItem.getDbizdate() == null)
        stmt.setNull(56, 1);
      else {
        stmt.setString(56, squareItem.getDbizdate().toString());
      }

      if (squareItem.getCbodycalbodyid() == null)
        stmt.setNull(57, 1);
      else {
        stmt.setString(57, squareItem.getCbodycalbodyid());
      }

      if (squareItem.getAttributeValue("npacknumber") == null)
        stmt.setNull(58, 3);
      else {
        stmt.setBigDecimal(58, ((UFDouble)squareItem.getAttributeValue("npacknumber")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("cprolineid") == null)
        stmt.setNull(59, 1);
      else {
        stmt.setString(59, squareItem.getAttributeValue("cprolineid").toString());
      }

      if (squareItem.getNreturntaxrate() == null)
        stmt.setNull(60, 3);
      else {
        stmt.setBigDecimal(60, squareItem.getNreturntaxrate().toBigDecimal());
      }

      if (squareItem.getVdef7() == null)
        stmt.setNull(61, 1);
      else {
        stmt.setString(61, squareItem.getVdef7());
      }

      if (squareItem.getVdef8() == null)
        stmt.setNull(62, 1);
      else {
        stmt.setString(62, squareItem.getVdef8());
      }

      if (squareItem.getVdef9() == null)
        stmt.setNull(63, 1);
      else {
        stmt.setString(63, squareItem.getVdef9());
      }

      if (squareItem.getVdef10() == null)
        stmt.setNull(64, 1);
      else {
        stmt.setString(64, squareItem.getVdef10());
      }

      if (squareItem.getVdef11() == null)
        stmt.setNull(65, 1);
      else {
        stmt.setString(65, squareItem.getVdef11());
      }

      if (squareItem.getVdef12() == null)
        stmt.setNull(66, 1);
      else {
        stmt.setString(66, squareItem.getVdef12());
      }

      if (squareItem.getVdef13() == null)
        stmt.setNull(67, 1);
      else {
        stmt.setString(67, squareItem.getVdef13());
      }

      if (squareItem.getVdef14() == null)
        stmt.setNull(68, 1);
      else {
        stmt.setString(68, squareItem.getVdef14());
      }

      if (squareItem.getVdef15() == null)
        stmt.setNull(69, 1);
      else {
        stmt.setString(69, squareItem.getVdef15());
      }

      if (squareItem.getVdef16() == null)
        stmt.setNull(70, 1);
      else {
        stmt.setString(70, squareItem.getVdef16());
      }

      if (squareItem.getVdef17() == null)
        stmt.setNull(71, 1);
      else {
        stmt.setString(71, squareItem.getVdef17());
      }

      if (squareItem.getVdef18() == null)
        stmt.setNull(72, 1);
      else {
        stmt.setString(72, squareItem.getVdef18());
      }

      if (squareItem.getVdef19() == null)
        stmt.setNull(73, 1);
      else {
        stmt.setString(73, squareItem.getVdef19());
      }

      if (squareItem.getVdef20() == null)
        stmt.setNull(74, 1);
      else {
        stmt.setString(74, squareItem.getVdef20());
      }

      if (squareItem.getPk_defdoc1() == null)
        stmt.setNull(75, 1);
      else {
        stmt.setString(75, squareItem.getPk_defdoc1());
      }

      if (squareItem.getPk_defdoc2() == null)
        stmt.setNull(76, 1);
      else {
        stmt.setString(76, squareItem.getPk_defdoc2());
      }

      if (squareItem.getPk_defdoc3() == null)
        stmt.setNull(77, 1);
      else {
        stmt.setString(77, squareItem.getPk_defdoc3());
      }

      if (squareItem.getPk_defdoc4() == null)
        stmt.setNull(78, 1);
      else {
        stmt.setString(78, squareItem.getPk_defdoc4());
      }

      if (squareItem.getPk_defdoc5() == null)
        stmt.setNull(79, 1);
      else {
        stmt.setString(79, squareItem.getPk_defdoc5());
      }

      if (squareItem.getPk_defdoc6() == null)
        stmt.setNull(80, 1);
      else {
        stmt.setString(80, squareItem.getPk_defdoc6());
      }

      if (squareItem.getPk_defdoc7() == null)
        stmt.setNull(81, 1);
      else {
        stmt.setString(81, squareItem.getPk_defdoc7());
      }

      if (squareItem.getPk_defdoc8() == null)
        stmt.setNull(82, 1);
      else {
        stmt.setString(82, squareItem.getPk_defdoc8());
      }

      if (squareItem.getPk_defdoc9() == null)
        stmt.setNull(83, 1);
      else {
        stmt.setString(83, squareItem.getPk_defdoc9());
      }

      if (squareItem.getPk_defdoc10() == null)
        stmt.setNull(84, 1);
      else {
        stmt.setString(84, squareItem.getPk_defdoc10());
      }

      if (squareItem.getPk_defdoc11() == null)
        stmt.setNull(85, 1);
      else {
        stmt.setString(85, squareItem.getPk_defdoc11());
      }

      if (squareItem.getPk_defdoc12() == null)
        stmt.setNull(86, 1);
      else {
        stmt.setString(86, squareItem.getPk_defdoc12());
      }

      if (squareItem.getPk_defdoc13() == null)
        stmt.setNull(87, 1);
      else {
        stmt.setString(87, squareItem.getPk_defdoc13());
      }

      if (squareItem.getPk_defdoc14() == null)
        stmt.setNull(88, 1);
      else {
        stmt.setString(88, squareItem.getPk_defdoc14());
      }

      if (squareItem.getPk_defdoc15() == null)
        stmt.setNull(89, 1);
      else {
        stmt.setString(89, squareItem.getPk_defdoc15());
      }

      if (squareItem.getPk_defdoc16() == null)
        stmt.setNull(90, 1);
      else {
        stmt.setString(90, squareItem.getPk_defdoc16());
      }

      if (squareItem.getPk_defdoc17() == null)
        stmt.setNull(91, 1);
      else {
        stmt.setString(91, squareItem.getPk_defdoc17());
      }

      if (squareItem.getPk_defdoc18() == null)
        stmt.setNull(92, 1);
      else {
        stmt.setString(92, squareItem.getPk_defdoc18());
      }

      if (squareItem.getPk_defdoc19() == null)
        stmt.setNull(93, 1);
      else {
        stmt.setString(93, squareItem.getPk_defdoc19());
      }

      if (squareItem.getPk_defdoc20() == null)
        stmt.setNull(94, 1);
      else {
        stmt.setString(94, squareItem.getPk_defdoc20());
      }

      if (squareItem.getCquoteunitid() == null)
        stmt.setNull(95, 1);
      else {
        stmt.setString(95, squareItem.getCquoteunitid());
      }

      if (squareItem.getAttributeValue("nquoteunitnum") == null)
        stmt.setNull(96, 3);
      else {
        stmt.setBigDecimal(96, ((UFDouble)squareItem.getAttributeValue("nquoteunitnum")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nquoteunitrate") == null)
        stmt.setNull(97, 3);
      else {
        stmt.setBigDecimal(97, ((UFDouble)squareItem.getAttributeValue("nquoteunitrate")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nqttaxnetprc") == null)
        stmt.setNull(98, 3);
      else {
        stmt.setBigDecimal(98, ((UFDouble)squareItem.getAttributeValue("nqttaxnetprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nqtnetprc") == null)
        stmt.setNull(99, 3);
      else {
        stmt.setBigDecimal(99, ((UFDouble)squareItem.getAttributeValue("nqtnetprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nqttaxprc") == null)
        stmt.setNull(100, 3);
      else {
        stmt.setBigDecimal(100, ((UFDouble)squareItem.getAttributeValue("nqttaxprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("nqtprc") == null)
        stmt.setNull(101, 3);
      else {
        stmt.setBigDecimal(101, ((UFDouble)squareItem.getAttributeValue("nqtprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("norgqttaxnetprc") == null)
        stmt.setNull(102, 3);
      else {
        stmt.setBigDecimal(102, ((UFDouble)squareItem.getAttributeValue("norgqttaxnetprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("norgqtnetprc") == null)
        stmt.setNull(103, 3);
      else {
        stmt.setBigDecimal(103, ((UFDouble)squareItem.getAttributeValue("norgqtnetprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("norgqttaxprc") == null)
        stmt.setNull(104, 3);
      else {
        stmt.setBigDecimal(104, ((UFDouble)squareItem.getAttributeValue("norgqttaxprc")).toBigDecimal());
      }

      if (squareItem.getAttributeValue("norgqtprc") == null)
        stmt.setNull(105, 3);
      else {
        stmt.setBigDecimal(105, ((UFDouble)squareItem.getAttributeValue("norgqtprc")).toBigDecimal());
      }

      if (squareItem.getPk_corp() == null)
        stmt.setNull(106, 1);
      else {
        stmt.setString(106, squareItem.getPk_corp());
      }

      stmt.setString(107, squareItem.getPrimaryKey());

      stmt.executeUpdate();
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.altertable.SquareDMO", "updateItem", new Object[] { squareItem });
  }

  public void updateItems(SquareItemVO[] squareItem, Connection con)
    throws SQLException
  {
    beforeCallMethod("nc.bs.so.altertable.SquareDMO", "updateItem", new Object[] { squareItem });

    String sql = "update so_square_b set dr = ?, csourcebillid = ?, csourcebillbodyid = ?, cinvbasdocid = ?, cinventoryid = ?, noutnum = ?, nshouldoutnum = ?, nbalancenum = ?, nsignnum = ?, ccurrencytypeid = ?, noriginalcurtaxnetprice = ?, noriginalcurmny = ?, noriginalcursummny = ?, bifpaybalance = ?, cbatchid = ?, nexchangeotobrate = ?, nexchangeotoarate = ?, ntaxrate = ?, noriginalcurnetprice = ?, noriginalcurtaxmny = ?, ntaxmny = ?, nmny = ?, nsummny = ?, nassistcursummny = ?, nassistcurmny = ?, nassistcurtaxmny = ?, cprojectid = ?, cprojectphaseid = ?, vfree1 = ?, vfree2 = ?, vfree3 = ?, vfree4 = ?, vfree5 = ?, vdef1 = ?, vdef2 = ?, vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, creceipttype = ?, cupreceipttype = ?, cupbillid = ?, cupbillbodyid = ?, cbodywarehouseid =?, cpackunitid = ?, scalefactor = ?, nbalancemny = ?, ncostmny = ?, blargessflag = ?, discountflag = ?, laborflag = ?, ndiscountmny = ? , noriginalcurdiscountmny = ?, nnetprice = ?, ntaxnetprice = ?, dbizdate = ?, cbodycalbodyid=? , npacknumber=? ,cprolineid=?, nreturntaxrate=?, vdef7 =?,vdef8 =?,vdef9 =?,vdef10 =?,vdef11 =?,vdef12 =?,vdef13 =?,vdef14 =?,vdef15 =?,vdef16 =?, vdef17 =?,vdef18 =?,vdef19 =?,vdef20 =?, pk_defdoc1=?,pk_defdoc2=?,pk_defdoc3=?,pk_defdoc4=?,pk_defdoc5=?,pk_defdoc6=?,pk_defdoc7=?,pk_defdoc8=?,pk_defdoc9=?,pk_defdoc10=?,pk_defdoc11=?,pk_defdoc12=?,pk_defdoc13=?,pk_defdoc14=?,pk_defdoc15=?,pk_defdoc16=?,pk_defdoc17=?,pk_defdoc18=?,pk_defdoc19=?,pk_defdoc20=?,cquoteunitid=?,nquoteunitnum=?,nquoteunitrate=?,nqttaxnetprc=?,nqtnetprc=?,nqttaxprc=?,nqtprc=?,norgqttaxnetprc=?,norgqtnetprc=?,norgqttaxprc=?,norgqtprc=?,pk_corp=? where corder_bid = ?";

    PreparedStatement stmt = null;
    try {
      if ((squareItem != null) && (squareItem.length != 0)) {
        for (int i = 0; i < squareItem.length; i++) {
          if (stmt == null) {
            stmt = prepareStatement(con, sql);
          }

          stmt.setInt(1, 0);
          if (squareItem[i].getCsourcebillid() == null)
            stmt.setNull(2, 1);
          else {
            stmt.setString(2, squareItem[i].getCsourcebillid());
          }
          if (squareItem[i].getCsourcebillbodyid() == null)
            stmt.setNull(3, 1);
          else {
            stmt.setString(3, squareItem[i].getCsourcebillbodyid());
          }
          if (squareItem[i].getCinvbasdocid() == null)
            stmt.setNull(4, 1);
          else {
            stmt.setString(4, squareItem[i].getCinvbasdocid());
          }
          if (squareItem[i].getCinventoryid() == null)
            stmt.setNull(5, 1);
          else {
            stmt.setString(5, squareItem[i].getCinventoryid());
          }
          if (squareItem[i].getNoutnum() == null)
            stmt.setNull(6, 4);
          else {
            stmt.setBigDecimal(6, squareItem[i].getNoutnum().toBigDecimal());
          }

          if (squareItem[i].getNshouldoutnum() == null)
            stmt.setNull(7, 4);
          else {
            stmt.setBigDecimal(7, squareItem[i].getNshouldoutnum().toBigDecimal());
          }

          if (squareItem[i].getNbalancenum() == null)
            stmt.setNull(8, 4);
          else {
            stmt.setBigDecimal(8, squareItem[i].getNbalancenum().toBigDecimal());
          }

          if (squareItem[i].getNsignnum() == null)
            stmt.setNull(9, 4);
          else {
            stmt.setBigDecimal(9, squareItem[i].getNsignnum().toBigDecimal());
          }

          if (squareItem[i].getCcurrencytypeid() == null)
            stmt.setNull(10, 1);
          else {
            stmt.setString(10, squareItem[i].getCcurrencytypeid());
          }
          if (squareItem[i].getNoriginalcurtaxnetprice() == null)
            stmt.setNull(11, 4);
          else {
            stmt.setBigDecimal(11, squareItem[i].getNoriginalcurtaxnetprice().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcurmny() == null)
            stmt.setNull(12, 4);
          else {
            stmt.setBigDecimal(12, squareItem[i].getNoriginalcurmny().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcursummny() == null)
            stmt.setNull(13, 4);
          else {
            stmt.setBigDecimal(13, squareItem[i].getNoriginalcursummny().toBigDecimal());
          }

          if (squareItem[i].getBifpaybalance() == null)
            stmt.setNull(14, 1);
          else {
            stmt.setString(14, squareItem[i].getBifpaybalance().toString());
          }

          if (squareItem[i].getCbatchid() == null)
            stmt.setNull(15, 1);
          else {
            stmt.setString(15, squareItem[i].getCbatchid());
          }
          if (squareItem[i].getNexchangeotobrate() == null)
            stmt.setNull(16, 4);
          else {
            stmt.setBigDecimal(16, squareItem[i].getNexchangeotobrate().toBigDecimal());
          }

          if (squareItem[i].getNexchangeotoarate() == null)
            stmt.setNull(17, 4);
          else {
            stmt.setBigDecimal(17, squareItem[i].getNexchangeotoarate().toBigDecimal());
          }

          if (squareItem[i].getNtaxrate() == null)
            stmt.setNull(18, 4);
          else {
            stmt.setBigDecimal(18, squareItem[i].getNtaxrate().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcurnetprice() == null)
            stmt.setNull(19, 4);
          else {
            stmt.setBigDecimal(19, squareItem[i].getNoriginalcurnetprice().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcurtaxmny() == null)
            stmt.setNull(20, 4);
          else {
            stmt.setBigDecimal(20, squareItem[i].getNoriginalcurtaxmny().toBigDecimal());
          }

          if (squareItem[i].getNtaxmny() == null)
            stmt.setNull(21, 4);
          else {
            stmt.setBigDecimal(21, squareItem[i].getNtaxmny().toBigDecimal());
          }

          if (squareItem[i].getNmny() == null)
            stmt.setNull(22, 4);
          else {
            stmt.setBigDecimal(22, squareItem[i].getNmny().toBigDecimal());
          }

          if (squareItem[i].getNsummny() == null)
            stmt.setNull(23, 4);
          else {
            stmt.setBigDecimal(23, squareItem[i].getNsummny().toBigDecimal());
          }

          if (squareItem[i].getNassistcursummny() == null)
            stmt.setNull(24, 4);
          else {
            stmt.setBigDecimal(24, squareItem[i].getNassistcursummny().toBigDecimal());
          }

          if (squareItem[i].getNassistcurmny() == null)
            stmt.setNull(25, 4);
          else {
            stmt.setBigDecimal(25, squareItem[i].getNassistcurmny().toBigDecimal());
          }

          if (squareItem[i].getNassistcurtaxmny() == null)
            stmt.setNull(26, 4);
          else {
            stmt.setBigDecimal(26, squareItem[i].getNassistcurtaxmny().toBigDecimal());
          }

          if (squareItem[i].getCprojectid() == null)
            stmt.setNull(27, 1);
          else {
            stmt.setString(27, squareItem[i].getCprojectid());
          }
          if (squareItem[i].getCprojectphaseid() == null)
            stmt.setNull(28, 1);
          else {
            stmt.setString(28, squareItem[i].getCprojectphaseid());
          }
          if (squareItem[i].getVfree1() == null)
            stmt.setNull(29, 1);
          else {
            stmt.setString(29, squareItem[i].getVfree1());
          }
          if (squareItem[i].getVfree2() == null)
            stmt.setNull(30, 1);
          else {
            stmt.setString(30, squareItem[i].getVfree2());
          }
          if (squareItem[i].getVfree3() == null)
            stmt.setNull(31, 1);
          else {
            stmt.setString(31, squareItem[i].getVfree3());
          }
          if (squareItem[i].getVfree4() == null)
            stmt.setNull(32, 1);
          else {
            stmt.setString(32, squareItem[i].getVfree4());
          }
          if (squareItem[i].getVfree5() == null)
            stmt.setNull(33, 1);
          else {
            stmt.setString(33, squareItem[i].getVfree5());
          }
          if (squareItem[i].getVdef1() == null)
            stmt.setNull(34, 1);
          else {
            stmt.setString(34, squareItem[i].getVdef1());
          }
          if (squareItem[i].getVdef2() == null)
            stmt.setNull(35, 1);
          else {
            stmt.setString(35, squareItem[i].getVdef2());
          }
          if (squareItem[i].getVdef3() == null)
            stmt.setNull(36, 1);
          else {
            stmt.setString(36, squareItem[i].getVdef3());
          }
          if (squareItem[i].getVdef4() == null)
            stmt.setNull(37, 1);
          else {
            stmt.setString(37, squareItem[i].getVdef4());
          }
          if (squareItem[i].getVdef5() == null)
            stmt.setNull(38, 1);
          else {
            stmt.setString(38, squareItem[i].getVdef5());
          }
          if (squareItem[i].getVdef6() == null)
            stmt.setNull(39, 1);
          else {
            stmt.setString(39, squareItem[i].getVdef6());
          }

          if (squareItem[i].getCreceipttype() == null)
            stmt.setNull(40, 1);
          else {
            stmt.setString(40, squareItem[i].getCreceipttype().toString());
          }

          if (squareItem[i].getCupreceipttype() == null)
            stmt.setNull(41, 1);
          else {
            stmt.setString(41, squareItem[i].getCupreceipttype());
          }

          if (squareItem[i].getCupbillid() == null)
            stmt.setNull(42, 1);
          else {
            stmt.setString(42, squareItem[i].getCupbillid());
          }

          if (squareItem[i].getCupbillbodyid() == null)
            stmt.setNull(43, 1);
          else {
            stmt.setString(43, squareItem[i].getCupbillbodyid());
          }

          if (squareItem[i].getCbodywarehouseid() == null)
            stmt.setNull(44, 1);
          else {
            stmt.setString(44, squareItem[i].getCbodywarehouseid());
          }

          if (squareItem[i].getCpackunitid() == null)
            stmt.setNull(45, 1);
          else {
            stmt.setString(45, squareItem[i].getCpackunitid());
          }

          if (squareItem[i].getScalefactor() == null)
            stmt.setNull(46, 4);
          else {
            stmt.setBigDecimal(46, squareItem[i].getScalefactor().toBigDecimal());
          }

          if (squareItem[i].getNbalancemny() == null)
            stmt.setNull(47, 4);
          else {
            stmt.setBigDecimal(47, squareItem[i].getNbalancemny().toBigDecimal());
          }

          if (squareItem[i].getNcostmny() == null)
            stmt.setNull(48, 4);
          else {
            stmt.setBigDecimal(48, squareItem[i].getNcostmny().toBigDecimal());
          }

          if (squareItem[i].getBlargessflag() == null)
            stmt.setNull(49, 1);
          else {
            stmt.setString(49, squareItem[i].getBlargessflag().toString());
          }

          if (squareItem[i].getDiscountflag() == null)
            stmt.setNull(50, 1);
          else {
            stmt.setString(50, squareItem[i].getDiscountflag().toString());
          }

          if (squareItem[i].getLaborflag() == null)
            stmt.setNull(51, 1);
          else {
            stmt.setString(51, squareItem[i].getLaborflag().toString());
          }

          if (squareItem[i].getNdiscountmny() == null)
            stmt.setNull(52, 4);
          else {
            stmt.setBigDecimal(52, squareItem[i].getNdiscountmny().toBigDecimal());
          }

          if (squareItem[i].getNoriginalcurdiscountmny() == null)
            stmt.setNull(53, 4);
          else {
            stmt.setBigDecimal(53, squareItem[i].getNoriginalcurdiscountmny().toBigDecimal());
          }

          if (squareItem[i].getNnetprice() == null)
            stmt.setNull(54, 4);
          else {
            stmt.setBigDecimal(54, squareItem[i].getNnetprice().toBigDecimal());
          }

          if (squareItem[i].getNtaxnetprice() == null)
            stmt.setNull(55, 4);
          else {
            stmt.setBigDecimal(55, squareItem[i].getNtaxnetprice().toBigDecimal());
          }

          if (squareItem[i].getDbizdate() == null)
            stmt.setNull(56, 1);
          else {
            stmt.setString(56, squareItem[i].getDbizdate().toString());
          }

          if (squareItem[i].getCbodycalbodyid() == null)
            stmt.setNull(57, 1);
          else {
            stmt.setString(57, squareItem[i].getCbodycalbodyid());
          }

          if (squareItem[i].getAttributeValue("npacknumber") == null)
            stmt.setNull(58, 3);
          else {
            stmt.setBigDecimal(58, ((UFDouble)squareItem[i].getAttributeValue("npacknumber")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("cprolineid") == null)
            stmt.setNull(59, 1);
          else {
            stmt.setString(59, squareItem[i].getAttributeValue("cprolineid").toString());
          }

          if (squareItem[i].getNreturntaxrate() == null)
            stmt.setNull(60, 3);
          else {
            stmt.setBigDecimal(60, squareItem[i].getNreturntaxrate().toBigDecimal());
          }

          if (squareItem[i].getVdef7() == null)
            stmt.setNull(61, 1);
          else {
            stmt.setString(61, squareItem[i].getVdef7());
          }

          if (squareItem[i].getVdef8() == null)
            stmt.setNull(62, 1);
          else {
            stmt.setString(62, squareItem[i].getVdef8());
          }

          if (squareItem[i].getVdef9() == null)
            stmt.setNull(63, 1);
          else {
            stmt.setString(63, squareItem[i].getVdef9());
          }

          if (squareItem[i].getVdef10() == null)
            stmt.setNull(64, 1);
          else {
            stmt.setString(64, squareItem[i].getVdef10());
          }

          if (squareItem[i].getVdef11() == null)
            stmt.setNull(65, 1);
          else {
            stmt.setString(65, squareItem[i].getVdef11());
          }

          if (squareItem[i].getVdef12() == null)
            stmt.setNull(66, 1);
          else {
            stmt.setString(66, squareItem[i].getVdef12());
          }

          if (squareItem[i].getVdef13() == null)
            stmt.setNull(67, 1);
          else {
            stmt.setString(67, squareItem[i].getVdef13());
          }

          if (squareItem[i].getVdef14() == null)
            stmt.setNull(68, 1);
          else {
            stmt.setString(68, squareItem[i].getVdef14());
          }

          if (squareItem[i].getVdef15() == null)
            stmt.setNull(69, 1);
          else {
            stmt.setString(69, squareItem[i].getVdef15());
          }

          if (squareItem[i].getVdef16() == null)
            stmt.setNull(70, 1);
          else {
            stmt.setString(70, squareItem[i].getVdef16());
          }

          if (squareItem[i].getVdef17() == null)
            stmt.setNull(71, 1);
          else {
            stmt.setString(71, squareItem[i].getVdef17());
          }

          if (squareItem[i].getVdef18() == null)
            stmt.setNull(72, 1);
          else {
            stmt.setString(72, squareItem[i].getVdef18());
          }

          if (squareItem[i].getVdef19() == null)
            stmt.setNull(73, 1);
          else {
            stmt.setString(73, squareItem[i].getVdef19());
          }

          if (squareItem[i].getVdef20() == null)
            stmt.setNull(74, 1);
          else {
            stmt.setString(74, squareItem[i].getVdef20());
          }

          if (squareItem[i].getPk_defdoc1() == null)
            stmt.setNull(75, 1);
          else {
            stmt.setString(75, squareItem[i].getPk_defdoc1());
          }

          if (squareItem[i].getPk_defdoc2() == null)
            stmt.setNull(76, 1);
          else {
            stmt.setString(76, squareItem[i].getPk_defdoc2());
          }

          if (squareItem[i].getPk_defdoc3() == null)
            stmt.setNull(77, 1);
          else {
            stmt.setString(77, squareItem[i].getPk_defdoc3());
          }

          if (squareItem[i].getPk_defdoc4() == null)
            stmt.setNull(78, 1);
          else {
            stmt.setString(78, squareItem[i].getPk_defdoc4());
          }

          if (squareItem[i].getPk_defdoc5() == null)
            stmt.setNull(79, 1);
          else {
            stmt.setString(79, squareItem[i].getPk_defdoc5());
          }

          if (squareItem[i].getPk_defdoc6() == null)
            stmt.setNull(80, 1);
          else {
            stmt.setString(80, squareItem[i].getPk_defdoc6());
          }

          if (squareItem[i].getPk_defdoc7() == null)
            stmt.setNull(81, 1);
          else {
            stmt.setString(81, squareItem[i].getPk_defdoc7());
          }

          if (squareItem[i].getPk_defdoc8() == null)
            stmt.setNull(82, 1);
          else {
            stmt.setString(82, squareItem[i].getPk_defdoc8());
          }

          if (squareItem[i].getPk_defdoc9() == null)
            stmt.setNull(83, 1);
          else {
            stmt.setString(83, squareItem[i].getPk_defdoc9());
          }

          if (squareItem[i].getPk_defdoc10() == null)
            stmt.setNull(84, 1);
          else {
            stmt.setString(84, squareItem[i].getPk_defdoc10());
          }

          if (squareItem[i].getPk_defdoc11() == null)
            stmt.setNull(85, 1);
          else {
            stmt.setString(85, squareItem[i].getPk_defdoc11());
          }

          if (squareItem[i].getPk_defdoc12() == null)
            stmt.setNull(86, 1);
          else {
            stmt.setString(86, squareItem[i].getPk_defdoc12());
          }

          if (squareItem[i].getPk_defdoc13() == null)
            stmt.setNull(87, 1);
          else {
            stmt.setString(87, squareItem[i].getPk_defdoc13());
          }

          if (squareItem[i].getPk_defdoc14() == null)
            stmt.setNull(88, 1);
          else {
            stmt.setString(88, squareItem[i].getPk_defdoc14());
          }

          if (squareItem[i].getPk_defdoc15() == null)
            stmt.setNull(89, 1);
          else {
            stmt.setString(89, squareItem[i].getPk_defdoc15());
          }

          if (squareItem[i].getPk_defdoc16() == null)
            stmt.setNull(90, 1);
          else {
            stmt.setString(90, squareItem[i].getPk_defdoc16());
          }

          if (squareItem[i].getPk_defdoc17() == null)
            stmt.setNull(91, 1);
          else {
            stmt.setString(91, squareItem[i].getPk_defdoc17());
          }

          if (squareItem[i].getPk_defdoc18() == null)
            stmt.setNull(92, 1);
          else {
            stmt.setString(92, squareItem[i].getPk_defdoc18());
          }

          if (squareItem[i].getPk_defdoc19() == null)
            stmt.setNull(93, 1);
          else {
            stmt.setString(93, squareItem[i].getPk_defdoc19());
          }

          if (squareItem[i].getPk_defdoc20() == null)
            stmt.setNull(94, 1);
          else {
            stmt.setString(94, squareItem[i].getPk_defdoc20());
          }

          if (squareItem[i].getCquoteunitid() == null)
            stmt.setNull(95, 1);
          else {
            stmt.setString(95, squareItem[i].getCquoteunitid());
          }

          if (squareItem[i].getAttributeValue("nquoteunitnum") == null)
            stmt.setNull(96, 3);
          else {
            stmt.setBigDecimal(96, ((UFDouble)squareItem[i].getAttributeValue("nquoteunitnum")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nquoteunitrate") == null)
            stmt.setNull(97, 3);
          else {
            stmt.setBigDecimal(97, ((UFDouble)squareItem[i].getAttributeValue("nquoteunitrate")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nqttaxnetprc") == null)
            stmt.setNull(98, 3);
          else {
            stmt.setBigDecimal(98, ((UFDouble)squareItem[i].getAttributeValue("nqttaxnetprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nqtnetprc") == null)
            stmt.setNull(99, 3);
          else {
            stmt.setBigDecimal(99, ((UFDouble)squareItem[i].getAttributeValue("nqtnetprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nqttaxprc") == null)
            stmt.setNull(100, 3);
          else {
            stmt.setBigDecimal(100, ((UFDouble)squareItem[i].getAttributeValue("nqttaxprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("nqtprc") == null)
            stmt.setNull(101, 3);
          else {
            stmt.setBigDecimal(101, ((UFDouble)squareItem[i].getAttributeValue("nqtprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("norgqttaxnetprc") == null)
            stmt.setNull(102, 3);
          else {
            stmt.setBigDecimal(102, ((UFDouble)squareItem[i].getAttributeValue("norgqttaxnetprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("norgqtnetprc") == null)
            stmt.setNull(103, 3);
          else {
            stmt.setBigDecimal(103, ((UFDouble)squareItem[i].getAttributeValue("norgqtnetprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("norgqttaxprc") == null)
            stmt.setNull(104, 3);
          else {
            stmt.setBigDecimal(104, ((UFDouble)squareItem[i].getAttributeValue("norgqttaxprc")).toBigDecimal());
          }

          if (squareItem[i].getAttributeValue("norgqtprc") == null)
            stmt.setNull(105, 3);
          else {
            stmt.setBigDecimal(105, ((UFDouble)squareItem[i].getAttributeValue("norgqtprc")).toBigDecimal());
          }

          if (squareItem[i].getPk_corp() == null)
            stmt.setNull(106, 1);
          else {
            stmt.setString(106, squareItem[i].getPk_corp());
          }

          stmt.setString(107, squareItem[i].getPrimaryKey());

          executeUpdate(stmt);
        }

        if (stmt != null)
          executeBatch(stmt);
      }
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.so.altertable.SquareDMO", "updateItem", new Object[] { squareItem });
  }
}