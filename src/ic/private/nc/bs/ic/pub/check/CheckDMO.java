package nc.bs.ic.pub.check;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javax.naming.NamingException;

import org.apache.commons.lang.ArrayUtils;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.QueryInfoDMO;
import nc.bs.ic.pub.bill.GeneralBillDMO;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.ic.pub.bill.MiscDMO;
import nc.bs.ic.pub.bill.OnhandnumDMO;
import nc.bs.ic.pub.bill.SpecialBillDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.bill.BillTempletDMO;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.bs.pub.para.SysInitBO;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.scm.pub.TempTableDMO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.sm.createcorp.CreatecorpDMO;
import nc.itf.pu.inter.IPuToIc_ToIC;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.uap.billtemplate.IBillTemplateQry;
import nc.itf.uap.sf.IOperateLogService;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.ui.dbcache.gui.MessageBox;
import nc.vo.ic.ic291.AllocationHHeaderVO;
import nc.vo.ic.ic291.AllocationHVO;
import nc.vo.ic.ic700.WastageBillBVO;
import nc.vo.ic.ic700.WastageBillVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.I18N;
import nc.vo.ic.pub.bill.IBillCodeExt;
import nc.vo.ic.pub.bill.SpecialBillHeaderVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.check.GeneralMethodGetErrorMsg;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.exp.BillCodeNotUnique;
import nc.vo.ic.pub.lang.Check;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.funcs.Businesslog;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.pub.IBillCode;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.sourcebill.SourceBillParaV3;
import nc.vo.sm.log.OperatelogVO;

public class CheckDMO extends DataManageObject
{
  ArrayList m_alTableA = new ArrayList();
  ArrayList m_alTableC = new ArrayList();
  Hashtable m_htTableB = new Hashtable();
  Hashtable m_htTableD = new Hashtable();
  Hashtable m_htTableE = new Hashtable();

  GeneralBillVO m_checkVO = null;
  GeneralBillItemVO m_gbivo = null;
  GeneralBillHeaderVO m_gbhvo = null;

  String m_pkcorp = null;
  String m_corpname = null;
  String m_warehouseid = null;
  String m_warehousename = null;

  String m_IsIC031 = null;
  String m_IsIC001 = null;

  String m_GeneralBID = "' '";

  String m_sTsHint = NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000047");

  Hashtable m_htTsSql = null;

  Hashtable m_htAloneFlag = new Hashtable();

  Hashtable m_htFixSpace = new Hashtable();

  String sNULL = null;

  Timer m_timer = new Timer();
  private final UFDouble ZERO = new UFDouble(0.0D);

  public CheckDMO()
    throws NamingException, SystemException
  {
  }

  public CheckDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public GeneralBillVO[] filtVOsBy4K(GeneralBillVO[] voaBill)
  {
    if ((voaBill == null) || (voaBill.length <= 0)) return null;
    int len = voaBill.length;

    ArrayList alRet = new ArrayList();
    for (int i = 0; i < len; i++) {
      if ((voaBill[i] == null) || 
        (voaBill[i].isFromSource("4K"))) continue;
      alRet.add(voaBill[i]);
    }

    if (alRet.size() <= 0) return null;

    GeneralBillVO[] voaRet = new GeneralBillVO[alRet.size()];
    alRet.toArray(voaRet);

    return voaRet;
  }

  public void checkBb3SignNum(AggregatedValueObject voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getParentVO() == null) || (voNowBill.getParentVO().getPrimaryKey() == null))
    {
      return;
    }this.m_timer.start();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Object oValue = null;
    try {
      String sql = "SELECT h.cgeneralhid FROM ic_general_h h INNER JOIN ic_general_bb3 bb3 ON h.cgeneralhid=bb3.cgeneralhid WHERE h.cgeneralhid=? AND h.dr=0 AND COALESCE(bb3.nsignnum,0.0) <> 0.0 ";

      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, voNowBill.getParentVO().getPrimaryKey());
      rs = stmt.executeQuery();

      if (rs.next())
        oValue = rs.getString(1);
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
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
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000024"));

    if ((oValue != null) && (oValue.toString().trim().length() > 0))
    {
      throw new BusinessException(ResBase.getStopAction() + Check.getBillInvoiced() + "<" + ((GeneralBillVO)voNowBill).getVBillCode() + ">");
    }
  }

  private Hashtable hashUnion(Hashtable ht1, Hashtable ht2)
  {
    if ((ht1 == null) || (ht2 == null))
    {
      return null;
    }

    Hashtable ht = new Hashtable();
    if ((ht1 != null) && (ht2 != null))
    {
      Set set1 = ht1.entrySet();
      Set set2 = ht2.entrySet();
      Iterator iter1 = set1.iterator();
      Iterator iter2 = set2.iterator();

      while (iter1.hasNext())
      {
        Map.Entry entry = (Map.Entry)iter1.next();

        while (ht2.containsKey(entry.getKey())) {
          ht.put(entry.getKey(), entry.getValue());
          ht2.remove(entry.getKey());
        }
      }
      return ht;
    }
    return null;
  }

  private String qryCalbodyByWh(String whid) throws BusinessException {
    if (whid == null)
      return null;
    String sql1 = "select pk_calbody from bd_stordoc where pk_stordoc=?";

    Connection con = null;
    PreparedStatement stmt = null;

    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql1);
      stmt.setString(1, whid);
      rs = stmt.executeQuery();

      if (rs.next()) {
        String pk_calbody = rs.getString(1);
        if (pk_calbody != null)
        {
          return pk_calbody;
        }
      }
    } catch (Exception e1) {
      SCMEnv.error(e1);
      throw new BusinessException(e1.getMessage());
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
    return null;
  }

  public void checkBillCode(AggregatedValueObject vo)
    throws BusinessException
  {
    if (!(vo instanceof GeneralBillVO))
      return;
    GeneralBillVO voBill = (GeneralBillVO)vo;

    if (voBill.getHeaderVO() == null)
      return;
    String vbillcode = (String)vo.getParentVO().getAttributeValue("vbillcode");
    String pk_corp = (String)vo.getParentVO().getAttributeValue("pk_corp");
    String hid = (String)vo.getParentVO().getAttributeValue("cgeneralhid");
    if ((vbillcode == null) || (vbillcode.trim().length() == 0))
      throw new BusinessException(ResBase.getIsnull() + ResBase.getBillcode());
    this.m_timer.start();
    String sSql = "SELECT vbillcode FROM ic_general_h WHERE  vbillcode=? and pk_corp=? and dr=0 ";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean isFound = false;
    int count = 0;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, vbillcode);
      stmt.setString(2, pk_corp);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String code = rs.getString(1);
        count++;
      }
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
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
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000025"));
    if (count > 1)
      throw new BusinessException(Check.getBillcodeExisted() + ResBase.getBillcode() + voBill.getHeaderVO().getVbillcode());
  }

  public UFBoolean checkBillSumed(AggregatedValueObject voParam)
    throws BusinessException
  {
    UFBoolean ufbSumed = new UFBoolean(false);
    if (!(voParam instanceof GeneralBillVO))
      return ufbSumed;
    GeneralBillVO voBill = (GeneralBillVO)voParam;

    if ((voBill.getHeaderVO() == null) || (voBill.getHeaderVO().getPrimaryKey() == null) || (voBill.getHeaderVO().getPrimaryKey().trim().length() == 0))
    {
      return ufbSumed;
    }
    GeneralBillItemVO[] voaItem = voBill.getItemVOs();

    String sSql = "SELECT cgeneralbid FROM ic_general_b WHERE dr=0 AND csumid IS NOT NULL AND cgeneralhid =?";

    this.m_timer.start();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, voBill.getHeaderVO().getPrimaryKey());
      rs = stmt.executeQuery();

      if (rs.next())
        ufbSumed = new UFBoolean(true);
    }
    catch (SQLException e) {
      throw new BusinessException(e.getMessage());
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
    if (ufbSumed.booleanValue())
      throw new BusinessException(ResBase.getStopAction() + Check.getBillVmiSumed() + "<" + voBill.getVBillCode() + ">");
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000026"));
    return ufbSumed;
  }

  public boolean isBillSign(GeneralBillVO vo)
    throws Exception
  {
    if ((vo == null) || (vo.getParentVO() == null)) return false;
    String pk = vo.getPrimaryKey();
    if (pk == null) return false;

    String sql = "select cregister from ic_general_h where dr=0 and cgeneralhid='" + pk + "'";
    SmartDMO dmo = new SmartDMO();
    Object[] oa = dmo.selectBy2(sql);
    return (oa != null) && (oa.length != 0) && (oa[0] != null) && (((Object[])(Object[])oa[0])[0] != null);
  }

  private void checkBillVO(AggregatedValueObject sbvo, BillTempletVO btvo, String m_sNumItemKey, String m_sAstItemKey, String sGrossNumKey)
    throws BusinessException
  {
    StringBuffer sbAllErrorMessage = new StringBuffer();
    StringBuffer sbNullErrorMessage = new StringBuffer();
    String sErrorMessage = null;
    Object oNum = null;
    boolean bISShouldBill = true;
    for (int i = 0; i < sbvo.getChildrenVO().length; i++) {
      if (sbvo.getChildrenVO()[i].getAttributeValue(m_sNumItemKey) == null)
      {
        continue;
      }

      bISShouldBill = false;
      break;
    }

    VOCheck.checkNullVO(sbvo);
    if (sbvo.getParentVO().getAttributeValue("vbillcode") == null)
      sbvo.getParentVO().setAttributeValue("vbillcode", sbvo.getParentVO().getAttributeValue("pk_corp"));
    if (!bISShouldBill)
    {
      try
      {
        VOCheck.validate(sbvo, GeneralMethodGetErrorMsg.getHeaderCanotNullString(btvo), GeneralMethodGetErrorMsg.getBodyCanotNullString(btvo));
      }
      catch (ICException e)
      {
        sErrorMessage = ArrayUtils.toString(e.getErrorRowNums().toArray())+"::"+e.getHint();
        sbAllErrorMessage.append(sErrorMessage + "\n");
      }

      try
      {
        VOCheck.checkFreeItemInput(sbvo, m_sNumItemKey);
      }
      catch (ICException e) {
        String sErrorMessage1 = VOCheck.getBodyErrorMessage(sbvo, e.getErrorRowNums(), e.getHint());
        sbNullErrorMessage.append(sErrorMessage1 + "\n");
      }

      if ((sbvo.getChildrenVO()[0].getAttributeValue("csourcetype") != null) && (!sbvo.getChildrenVO()[0].getAttributeValue("csourcetype").equals("A3"))) {
        try {
          VOCheck.checkAssistUnitInputByID(sbvo, m_sNumItemKey, m_sAstItemKey);
        }
        catch (ICException e) {
          String sErrorMessage1 = VOCheck.getBodyErrorMessage(sbvo, e.getErrorRowNums(), e.getHint());
          sbNullErrorMessage.append(sErrorMessage1 + "\n");
        }

      }

      try
      {
        VOCheck.checkdbizdate(sbvo, m_sNumItemKey);
      }
      catch (ICException e) {
        String sErrorMessage1 = VOCheck.getBodyErrorMessage(sbvo, e.getErrorRowNums(), e.getHint());
        sbNullErrorMessage.append(sErrorMessage1 + "\n");
      }
      try
      {
        VOCheck.checkSNInput(sbvo.getChildrenVO(), m_sNumItemKey);
      }
      catch (ICException e) {
        String sErrorMessage1 = VOCheck.getBodyErrorMessage(sbvo, e.getErrorRowNums(), e.getHint());
        sbNullErrorMessage.append(sErrorMessage1 + "\n");
      }
      try
      {
        VOCheck.checkGreaterThanZeroInput(sbvo.getChildrenVO(), "nprice", ResBase.getPrice());
      }
      catch (ICException e)
      {
        String sErrorMessage1 = VOCheck.getBodyErrorMessage(sbvo, e.getErrorRowNums(), e.getHint());
        sbNullErrorMessage.append(sErrorMessage1 + "\n");
      }

      try
      {
        VOCheck.checkGrossNumInput(sbvo.getChildrenVO(), sGrossNumKey, m_sNumItemKey);
      }
      catch (ICException e) {
        String sErrorMessage1 = VOCheck.getBodyErrorMessage(sbvo, e.getErrorRowNums(), e.getHint());

        sbNullErrorMessage.append(sErrorMessage1 + "\n");
      }

    }

    if (sbNullErrorMessage.toString().trim().length() > 0) {
      sbAllErrorMessage.append(ResBase.getIsnull() + "\n");
      sbAllErrorMessage.append(sbNullErrorMessage);
    }

    if (sbAllErrorMessage.toString().trim().length() > 0)
    {
      throw new BusinessException(sbAllErrorMessage.toString());
    }
  }

  public void checkHaveEstimatedItems(AggregatedValueObject vo)
    throws BusinessException
  {
    String sBillPK = null;
    if ((vo != null) && (vo.getParentVO() != null) && (vo.getParentVO().getPrimaryKey() != null)) {
      sBillPK = vo.getParentVO().getPrimaryKey().trim();
    }
    beforeCallMethod("nc.bs.ic.pub.check.CheckDMO", "checkHaveEstimatedItems", new Object[] { vo });

    if (sBillPK == null)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000027"));
    this.m_timer.start();

    Boolean bHave = new Boolean(false);

    String sSql = "SELECT cgeneralbid,bzgflag,isok FROM ic_general_b WHERE dr=0 AND cgeneralhid= ? AND (csourcebillbid IS NOT NULL OR csourcetype IS NOT NULL) AND (rtrim(ltrim(COALESCE(bzgflag,'N')))='Y' OR rtrim(ltrim(COALESCE(bzgflag,'N')))='y'  OR rtrim(ltrim(COALESCE(isok,'N')))='Y' OR rtrim(ltrim(COALESCE(isok,'N')))='y' )";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, sBillPK);
      rs = stmt.executeQuery();
      if (rs.next())
      {
        bHave = new Boolean(true);
      }
    } catch (Exception e) {
      throw new BusinessException("checkHaveEstimatedItems:" + e.getMessage());
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
      catch (Exception e) {
      }
    }
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000028"));

    afterCallMethod("nc.bs.ic.pub.check.CheckDMO", "checkHaveEstimatedItems", new Object[] { vo });

    if (bHave.booleanValue())
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000029"));
  }

  public void checkHaveEstimatedItemsAnyway(AggregatedValueObject vo)
    throws BusinessException
  {
    String sBillPK = null;
    if ((vo != null) && (vo.getParentVO() != null) && (vo.getParentVO().getPrimaryKey() != null)) {
      sBillPK = vo.getParentVO().getPrimaryKey().trim();
    }

    beforeCallMethod("nc.bs.ic.pub.check.CheckDMO", "checkHaveEstimatedItems", new Object[] { sBillPK });

    if (sBillPK == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000027"));
    }
    Boolean bHave = new Boolean(false);
    this.m_timer.start();
    String sSql = "SELECT cgeneralbid,bzgflag,isok FROM ic_general_b WHERE dr=0 AND cgeneralhid= ?  AND (rtrim(ltrim(COALESCE(bzgflag,'N')))='Y' OR rtrim(ltrim(COALESCE(bzgflag,'N')))='y'  OR rtrim(ltrim(COALESCE(isok,'N')))='Y' OR rtrim(ltrim(COALESCE(isok,'N')))='y' )";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, sBillPK);
      rs = stmt.executeQuery();
      ArrayList alTemp = null;
      if (rs.next())
      {
        bHave = new Boolean(true);
      }
    } catch (Exception e) {
      throw new BusinessException("checkHaveEstimatedItemsAnyway:" + e.getMessage());
    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
        SCMEnv.error(e);
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
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000030"));

    afterCallMethod("nc.bs.ic.pub.check.CheckDMO", "checkHaveEstimatedItems", new Object[] { sBillPK });

    if (bHave.booleanValue())
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000029"));
  }

  public void checkInOutTrace(AggregatedValueObject avo)
    throws BusinessException, SystemException
  {
  }

  public String checkInvalidateDate(AggregatedValueObject vo)
    throws BusinessException
  {
    String unitCode = (String)vo.getParentVO().getAttributeValue("pk_corp");
    UFDate dBillDate = (UFDate)vo.getParentVO().getAttributeValue("dbilldate");
    this.m_timer.start();
    String sFirstCheck = "ninnum";
    if (((GeneralBillVO)vo).getBillInOutFlag() == -1) {
      sFirstCheck = "noutnum";
    }
    try
    {
      VOCheck.checkLotInput(vo, sFirstCheck);
    } catch (ICNullFieldException e) {
      ArrayList alerr = e.getErrorRowNums();
      StringBuffer sberr = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000123"));
      if ((alerr != null) && (alerr.size() > 0)) {
        int iLen = alerr.size();
        sberr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000124"));
        for (int i = 0; i < iLen; i++) {
          if (i != 0)
            sberr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000100"));
          sberr.append(((ArrayList)alerr.get(i)).get(0).toString());
        }
      }
      throw new BusinessException(sberr.toString());
    }
    ArrayList alData = new ArrayList();
    Hashtable htDate = new Hashtable();
    Hashtable htErr = new Hashtable();
    int length = vo.getChildrenVO().length;
    ArrayList alrow = null;
    for (int i = 0; i < length; i++) {
      if ((((GeneralBillItemVO)vo.getChildrenVO()[i]).getIsValidateMgt() == null) || (((GeneralBillItemVO)vo.getChildrenVO()[i]).getIsValidateMgt().intValue() == 0))
        continue;
      if ((vo.getChildrenVO()[i].getStatus() == 3) || (((GeneralBillItemVO)vo.getChildrenVO()[i]).getIsValidateMgt().intValue() == 0) || (vo.getChildrenVO()[i].getAttributeValue("vbatchcode") == null))
      {
        continue;
      }

      alrow = new ArrayList();

      String sInv = (String)vo.getChildrenVO()[i].getAttributeValue("cinvbasid");
      String sBatch = (String)vo.getChildrenVO()[i].getAttributeValue("vbatchcode");
      alrow.add(sInv);
      alrow.add(sBatch);
      alData.add(alrow);
    }

    if (alData.size() < 1) {
      return null;
    }
    try
    {
      Object cgeneralhid = vo.getParentVO().getAttributeValue("cgeneralhid");
      String chid = cgeneralhid == null ? null : (String)cgeneralhid;
      htDate = queryValidate(alData, unitCode, chid);
    }
    catch (Exception sqle) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000031"));
    }

    GeneralBillItemVO voItem = null;
    for (int i = 0; i < length; i++) {
      voItem = (GeneralBillItemVO)vo.getChildrenVO()[i];
      if (voItem.getStatus() == 3) {
        continue;
      }
      if ((((GeneralBillItemVO)vo.getChildrenVO()[i]).getIsValidateMgt() == null) || (((GeneralBillItemVO)vo.getChildrenVO()[i]).getIsValidateMgt().intValue() == 0)) {
        continue;
      }
      String[] tmpStr = new String[2];
      String sValidate = null;

      if (voItem.getAttributeValue("dvalidate") != null) {
        sValidate = voItem.getDvalidate().toString();
      }
      String sInv = (String)voItem.getAttributeValue("cinvbasid");
      String sInvCode = (String)voItem.getAttributeValue("cinventorycode");
      String sInvname = (String)voItem.getAttributeValue("invname");
      String sBatch = voItem.getVbatchcode();
      String key = sInv + "+" + sBatch;

      if (htDate.containsKey(key)) {
        String tmpDate = (String)htDate.get(key);
        if (!tmpDate.equals(sValidate))
        {
          if (tmpDate != null)
          {
            voItem.setAttributeValue("dvalidate", new UFDate(tmpDate));
            if (voItem.getStatus() == 0) {
              voItem.setStatus(1);
            }

            voItem.setAttributeValue("scrq", null);
            voItem.calPrdDate();
          }
        }
      }

      if (voItem.getDvalidate() == null) {
        voItem.setAttributeValue("scrq", dBillDate);
        if (voItem.getQualityDay() != null) {
          voItem.setDvalidate(voItem.getScrq().getDateAfter(voItem.getQualityDay().intValue()));
        }
      }

    }

    try
    {
      VOCheck.checkInvalidateDateInput(vo, sFirstCheck);
    } catch (ICNullFieldException e) {
      ArrayList alerr = e.getErrorRowNums();
      StringBuffer sberr = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000123"));
      if ((alerr != null) && (alerr.size() > 0)) {
        int iLen = alerr.size();
        sberr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000124"));
        for (int i = 0; i < iLen; i++) {
          if (i != 0)
            sberr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000100"));
          sberr.append(((ArrayList)alerr.get(i)).get(0).toString());
        }
      }
      throw new BusinessException(sberr.toString());
    }

    return null;
  }

  public UFBoolean checkIsSpecifyBusiType(AggregatedValueObject voParam)
    throws BusinessException
  {
    if ((voParam == null) || (voParam.getParentVO() == null) || (voParam.getChildrenVO() == null) || (voParam.getChildrenVO().length == 0) || (!(voParam instanceof GeneralBillVO)))
    {
      return new UFBoolean(false);
    }
    GeneralBillVO gvo = (GeneralBillVO)voParam;

    String sBillBusiType = null; String sCorpID = null;
    String sBillType = null;

    if ((gvo != null) && (gvo.getHeaderVO() != null)) {
      sCorpID = gvo.getHeaderVO().getPk_corp();
      sBillBusiType = gvo.getHeaderVO().getCbiztypeid();
      sBillType = gvo.getHeaderVO().getCbilltypecode();
      if ((!BillTypeConst.m_saleOut.equals(sBillType)) && (!BillTypeConst.m_purchaseIn.equals(sBillType)))
      {
        return new UFBoolean(false);
      }
    } else {
      SCMEnv.out("check busitype is specify busitype!!");
      return new UFBoolean(false);
    }
    this.m_timer.start();
    try
    {
      OnhandnumDMO dmoOnhand = new OnhandnumDMO();

      if ((sBillBusiType != null) && (sBillBusiType.length() > 0)) {
        Hashtable htBusitype = dmoOnhand.queryBusitype(sCorpID);

        this.m_timer.stopAndShow(" spec biz type ");

        if ((htBusitype != null) && (htBusitype.containsKey(sBillBusiType)))
          return new UFBoolean(true);
      }
    } catch (Exception e) {
      SCMEnv.error(e);
      throw new BusinessException(e.getMessage());
    }

    return new UFBoolean(false);
  }

  public void checkOnlyHeadTimeStamp(GeneralBillVO voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getHeaderVO() == null) || (voNowBill.getItemVOs() == null) || (voNowBill.getItemVOs().length == 0))
    {
      return;
    }this.m_timer.start();
    try
    {
      String sNowHeadPK = voNowBill.getHeaderVO().getPrimaryKey();
      String sNowHeadTs = voNowBill.getHeaderVO().getTs();
      String sOrgTs = null; String sNowTs = null;

      if (sNowHeadTs != null)
      {
        GeneralBillDMO dmoBill = new GeneralBillDMO();
        String sHeadTs = dmoBill.queryBillHeadTs(sNowHeadPK);

        dmoBill = null;
        if (!sNowHeadTs.equals(sHeadTs))
          throw new BusinessException(this.m_sTsHint);
      }
      this.m_timer.stopAndShow(" check head ts ");
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
  }

  public void checkOnlyHeadTimeStamp(SpecialBillVO voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getHeaderVO() == null) || (voNowBill.getItemVOs() == null) || (voNowBill.getItemVOs().length == 0))
    {
      return;
    }this.m_timer.start();
    try
    {
      String sNowHeadPK = voNowBill.getHeaderVO().getPrimaryKey();
      String sNowHeadTs = voNowBill.getHeaderVO().getTs();
      String sOrgTs = null; String sNowTs = null;

      if (sNowHeadTs != null)
      {
        SpecialBillDMO dmoBill = new SpecialBillDMO();
        String sHeadTs = dmoBill.queryBillHeadTs(sNowHeadPK);

        dmoBill = null;
        if (!sNowHeadTs.equals(sHeadTs))
          throw new BusinessException(this.m_sTsHint);
      }
      this.m_timer.stopAndShow(" check head ts ");
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }

  public void checkOutVO(AggregatedValueObject avo)
    throws BusinessException
  {
    StringBuffer sbNotNullErrorMsg = new StringBuffer();
    this.m_timer.start();
    if (avo == null)
      throw new BusinessException("vo is null ");
    try
    {
      GeneralBillVO gvo = (GeneralBillVO)avo;

      nc.vo.ic.pub.GenMethod.convertICAssistNumAtBs(new GeneralBillVO[] { gvo });

      appendInvWhInfo(gvo);

      GeneralBillItemVO[] newItemVO = gvo.filterItem();

      if (newItemVO == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000035"));
      }

      gvo.setChildrenVO(newItemVO);

      VOCheck.checkVMIWh(new QueryInfoDMO(), gvo);

      checkDef(gvo);
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }

    if (sbNotNullErrorMsg.toString().trim().length() > 0) {
      sbNotNullErrorMsg.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000036"));
      throw new BusinessException(sbNotNullErrorMsg.toString());
    }

    BillTempletVO btvo = findDefaultCardTempletData(avo);
    int iWH = 1;
    if ((avo instanceof GeneralBillVO)) {
      iWH = ((GeneralBillVO)avo).getBillInOutFlag();
    }
    checkBillVO(avo, btvo, iWH == 1 ? "ninnum" : "noutnum", iWH == 1 ? "ninassistnum" : "noutassistnum", iWH == 1 ? "ningrossnum" : "noutgrossnum");

    this.m_timer.stopAndShow(" push save check ");
  }

  public static InvVO appendInvInfo(InvVO voInv)
    throws BusinessException
  {
    if (voInv == null) {
      return voInv;
    }
    try
    {
      String InvID2 = null;
      MiscDMO dmoMisc = new MiscDMO();

      if (voInv.getCinventoryid() == null) {
        throw new BusinessException(ResBase.getIsnull() + ":\n" + ResBase.getInv());
      }
      InvVO[] alInvVO = null;

      alInvVO = dmoMisc.getInvInfo(new String[] { voInv.getCinventoryid() });

      if ((alInvVO != null) && (alInvVO.length > 0)) {
        return alInvVO[0];
      }
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return voInv;
  }

  public static void appendInvInfo(GeneralBillVO gvo)
    throws BusinessException
  {
    try
    {
      String WHID = gvo.getHeaderVO().getCwarehouseid();
      String WasteWHID = gvo.getHeaderVO().getCwastewarehouseid();

      if (WHID == null) {
        WHID = WasteWHID;
      }

      String InvID2 = null;

      int RowNum = gvo.getItemCount();

      MiscDMO dmoMisc = new MiscDMO();

      HashMap hmInvID = new HashMap();
      for (int j = 0; j < RowNum; j++) {
        InvID2 = gvo.getItemVOs()[j].getCinventoryid();

        if (InvID2 == null)
          throw new BusinessException(ResBase.getIsnull() + ":\n" + ResBase.getInv());
        if (!hmInvID.containsKey(InvID2)) {
          hmInvID.put(InvID2, InvID2);
        }

      }

      String[] invids = new String[hmInvID.size()];
      hmInvID.keySet().toArray(invids);
      InvVO[] alInvVO = dmoMisc.getInvInfo(invids);

      if (alInvVO != null) {
        InvVO invvotemp = null;
        for (int i = 0; i < alInvVO.length; i++) {
          hmInvID.put(alInvVO[i].getCinventoryid(), alInvVO[i]);
        }

        for (int i = 0; i < RowNum; i++) {
          InvID2 = gvo.getItemVOs()[i].getCinventoryid();
          if (hmInvID.containsKey(InvID2)) {
            invvotemp = (InvVO)hmInvID.get(InvID2);
            gvo.getItemVOs()[i].setInvPartly(invvotemp);
          }
        }
      }

    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }

  public static void appendWhInfo(GeneralBillVO gvo)
    throws BusinessException
  {
    try
    {
      MiscDMO dmoMisc = new MiscDMO();

      String WHID = gvo.getHeaderVO().getCwarehouseid();
      String WasteWHID = gvo.getHeaderVO().getCwastewarehouseid();

      if ((WHID == null) && (WasteWHID == null)) {
        throw new BusinessException(ResBase.getIsnull() + ":\n" + ResBase.getWareHouse());
      }
      if ((WHID != null) && (WHID.trim().length() > 0)) {
        WhVO whvo = dmoMisc.getWhInfo(WHID);
        gvo.setWh(whvo);
      }

      if ((WasteWHID != null) && (WasteWHID.trim().length() > 0)) {
        WhVO whvo = dmoMisc.getWhInfo(WasteWHID);
        if ((whvo.getIsWasteWh() != null) && (whvo.getIsWasteWh().intValue() == 1)) {
          gvo.setWasteWh(whvo);
        }
      }
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }

  public static void appendInvWhInfo(GeneralBillVO gvo)
    throws BusinessException
  {
    appendWhInfo(gvo);
    appendInvInfo(gvo);
  }

  public UFBoolean checkSomeItemsSumed(AggregatedValueObject voParam)
    throws BusinessException
  {
    UFBoolean ufbSumed = new UFBoolean(false);
    if (!(voParam instanceof GeneralBillVO))
      return ufbSumed;
    GeneralBillVO voBill = (GeneralBillVO)voParam;

    if ((voBill.getHeaderVO() != null) && ((voBill.getHeaderVO().getStatus() == 2) || (voBill.getHeaderVO().getPrimaryKey() == null) || (voBill.getHeaderVO().getPrimaryKey().trim().length() == 0)))
    {
      return ufbSumed;
    }
    this.m_timer.start();

    GeneralBillItemVO[] voaItem = voBill.getItemVOs();

    StringBuffer sbSql = new StringBuffer("SELECT cgeneralbid FROM ic_general_b WHERE dr=0 AND csumid IS NOT NULL ");

    ArrayList alKeys = new ArrayList();
    for (int row = 0; row < voaItem.length; row++) {
      if ((voaItem[row] == null) || (voaItem[row].getPrimaryKey() == null) || ((voaItem[row].getStatus() != 3) && (voaItem[row].getStatus() != 1)))
      {
        continue;
      }
      alKeys.add(voaItem[row].getPrimaryKey());
    }

    sbSql.append(GeneralSqlString.formInSQL("cgeneralbid", alKeys));

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();

      if (rs.next())
        ufbSumed = new UFBoolean(true);
    } catch (SQLException e) {
      throw new BusinessException("checkSomeItemsSumed:" + e.getMessage());
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
    this.m_timer.stopAndShow(" item sumed ");

    if (ufbSumed.booleanValue())
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000037"));
    return ufbSumed;
  }

  public void checkTimeStamp(GeneralBillVO voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getHeaderVO() == null) || (voNowBill.getItemVOs() == null) || (voNowBill.getItemVOs().length == 0))
    {
      return;
    }
    this.m_timer.start();
    try
    {
      String sNowHeadPK = voNowBill.getHeaderVO().getPrimaryKey();
      String sNowHeadTs = voNowBill.getHeaderVO().getTs();
      String sOrgTs = null; String sNowTs = null;

      if ((sNowHeadTs != null) && (sNowHeadPK != null))
      {
        GeneralBillDMO dmoBill = new GeneralBillDMO();
        String sHeadTs = dmoBill.queryBillHeadTs(sNowHeadPK);

        if (!sNowHeadTs.equals(sHeadTs)) {
          SCMEnv.out("1====" + sNowHeadTs + "2====" + sHeadTs);
          throw new BusinessException("1:" + this.m_sTsHint);
        }

        Hashtable htNowItemTs = new Hashtable();
        GeneralBillItemVO[] voaNowItem = voNowBill.getItemVOs();

        int iNowOriginalRowNum = 0;
        for (int i = 0; i < voaNowItem.length; i++) {
          if ((voaNowItem[i].getPrimaryKey() != null) && (voaNowItem[i].getTs() != null)) {
            htNowItemTs.put(voaNowItem[i].getPrimaryKey(), voaNowItem[i].getTs());
          }

          if (voaNowItem[i].getStatus() != 2) {
            iNowOriginalRowNum++;
          }
        }

        if (iNowOriginalRowNum == 0) {
          return;
        }
        GeneralBillItemVO[] voaOrgItem = dmoBill.queryBillItemTs(sNowHeadPK);

        if ((voaOrgItem != null) && (voaOrgItem.length != iNowOriginalRowNum)) {
          SCMEnv.out("1====" + iNowOriginalRowNum + "2====" + voaOrgItem.length);
          throw new BusinessException("2:" + this.m_sTsHint);
        }

        if ((voaOrgItem != null) && (voaOrgItem.length > 0)) {
          for (int j = 0; j < voaOrgItem.length; j++)
          {
            sNowTs = null;

            sOrgTs = voaOrgItem[j].getTs();

            if ((voaOrgItem[j].getPrimaryKey() != null) && (htNowItemTs.containsKey(voaOrgItem[j].getPrimaryKey())))
            {
              sNowTs = (String)htNowItemTs.get(voaOrgItem[j].getPrimaryKey());
            }
            if ((sOrgTs != null) && (sNowTs != null) && (!sOrgTs.trim().equals(sNowTs.trim()))) {
              SCMEnv.out("i=" + j + "1====" + sNowTs + "2====" + sOrgTs);
              throw new BusinessException("3:" + this.m_sTsHint);
            }
          }

        }

        dmoBill = null;
      }
      this.m_timer.stopAndShow(" ts ");
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }

  public void checkTimeStamp(SpecialBillVO voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getHeaderVO() == null) || (voNowBill.getItemVOs() == null) || (voNowBill.getItemVOs().length == 0))
    {
      return;
    }
    this.m_timer.start();
    try
    {
      String sNowHeadPK = voNowBill.getHeaderVO().getPrimaryKey();
      String sNowHeadTs = voNowBill.getHeaderVO().getTs();
      String sOrgTs = null; String sNowTs = null;

      if ((sNowHeadTs != null) && (sNowHeadPK != null))
      {
        SpecialBillDMO dmoBill = new SpecialBillDMO();
        String sHeadTs = dmoBill.queryBillHeadTs(sNowHeadPK);

        if (!sNowHeadTs.equals(sHeadTs)) {
          throw new BusinessException(this.m_sTsHint);
        }

        Hashtable htNowItemTs = new Hashtable();
        SpecialBillItemVO[] voaNowItem = voNowBill.getItemVOs();

        int iNowOriginalRowNum = 0;
        for (int i = 0; i < voaNowItem.length; i++) {
          if ((voaNowItem[i].getPrimaryKey() != null) && (voaNowItem[i].getTs() != null)) {
            htNowItemTs.put(voaNowItem[i].getPrimaryKey(), voaNowItem[i].getTs());
          }
          if (voaNowItem[i].getStatus() != 2) {
            iNowOriginalRowNum++;
          }
        }

        if (iNowOriginalRowNum == 0) {
          return;
        }

        SpecialBillItemVO[] voaOrgItem = dmoBill.queryBillItemTs(sNowHeadPK);

        if ((voaOrgItem != null) && (voaOrgItem.length != iNowOriginalRowNum)) {
          throw new BusinessException(this.m_sTsHint);
        }

        if ((voaOrgItem != null) && (voaOrgItem.length > 0)) {
          for (int j = 0; j < voaOrgItem.length; j++)
          {
            sNowTs = null;

            sOrgTs = voaOrgItem[j].getTs();

            if ((voaOrgItem[j].getPrimaryKey() != null) && (htNowItemTs.containsKey(voaOrgItem[j].getPrimaryKey())))
            {
              sNowTs = (String)htNowItemTs.get(voaOrgItem[j].getPrimaryKey());
            }
            if ((sOrgTs != null) && (!sOrgTs.equals(sNowTs))) {
              throw new BusinessException(this.m_sTsHint);
            }
          }
        }

        dmoBill = null;
      }
      this.m_timer.stopAndShow(" ts ");
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }

  public void checkTimeStamp(AggregatedValueObject voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getParentVO() == null) || (voNowBill.getChildrenVO() == null) || (voNowBill.getChildrenVO().length == 0))
    {
      return;
    }
    this.m_timer.start();

    if ((voNowBill instanceof GeneralBillVO))
      checkTimeStamp((GeneralBillVO)voNowBill);
    else if ((voNowBill instanceof SpecialBillVO))
      checkTimeStamp((SpecialBillVO)voNowBill);
    else if ((voNowBill instanceof AllocationHVO))
      checkTimeStamp((AllocationHVO)voNowBill);
  }

  private String execFormular(String formula, String value)
  {
    FormulaParse f = new FormulaParse();

    boolean isValidity = true;

    if ((formula != null) && (!formula.equals("")))
    {
      f.setExpress(formula);

      VarryVO varry = f.getVarry();

      Hashtable h = new Hashtable();
      for (int j = 0; j < varry.getVarry().length; j++) {
        String key = varry.getVarry()[j];

        String[] vs = new String[1];
        vs[0] = value;
        h.put(key, vs);
      }

      f.setDataS(h);

      if ((varry.getFormulaName() != null) && (!varry.getFormulaName().trim().equals("")))
      {
        return f.getValueS()[0];
      }
      return f.getValueS()[0];
    }

    return null;
  }

  private BillTempletVO findDefaultCardTempletData(AggregatedValueObject vo)
    throws BusinessException
  {
    BillTempletVO btvo = null;
    try
    {
      BillTempletDMO dmo = new BillTempletDMO();
      String[] ids = dmo.findTempletIDWithTS((String)vo.getParentVO().getAttributeValue("cbilltypecode"), (String)vo.getParentVO().getAttributeValue("pk_corp"), (String)vo.getParentVO().getAttributeValue("cbiztypeid"), (String)vo.getParentVO().getAttributeValue("coperatoridnow"), null);

      if ((ids != null) && (ids.length > 0)) {
        IBillTemplateQry bo = (IBillTemplateQry)NCLocator.getInstance().lookup(IBillTemplateQry.class.getName());
        btvo = bo.findCardTempletData(ids[0], (String)vo.getParentVO().getAttributeValue("pk_corp"));
      }

    }
    catch (Exception e)
    {
      throw new BusinessException("Cannot get User Defined Params from MiddleWare Server!");
    }

    return btvo;
  }

  public UFBoolean isAllowedModifyByOther(AggregatedValueObject vo)
    throws BusinessException, SystemException
  {
    if ((vo == null) || (vo.getParentVO() == null)) {
      return new UFBoolean(true);
    }

    String sPreOperator = (String)vo.getParentVO().getAttributeValue("coperatorid");
    String sCurOperator = (String)vo.getParentVO().getAttributeValue("coperatoridnow");

    if (sPreOperator != null)
    {
      if (!sPreOperator.equals(sCurOperator)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000038"));
      }
    }
    return new UFBoolean(true);
  }

  public UFBoolean isInvControlledByOperator(AggregatedValueObject vo)
    throws BusinessException, SQLException
  {
    this.m_timer.start();

    UFBoolean ret = new UFBoolean(true);

    String coperatorid = (String)vo.getParentVO().getAttributeValue("coperatoridnow");
    String pk_psndoc = null;

    pk_psndoc = execFormular("getColValue(sm_userandclerk,pk_psndoc,userid,userid)", coperatorid);

    String cwarehouseid = (String)vo.getParentVO().getAttributeValue("cwarehouseid");
    if (cwarehouseid == null)
      cwarehouseid = (String)vo.getParentVO().getAttributeValue("cwastewarehouseid");
    String unitCode = (String)vo.getParentVO().getAttributeValue("pk_corp");
    String sql = "";

    if ((coperatorid == null) || (coperatorid.trim().length() < 1) || (cwarehouseid == null) || (pk_psndoc == null) || (pk_psndoc.trim().length() < 1))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000039"));
    }

    ArrayList alErr = queryWH1(pk_psndoc, cwarehouseid, unitCode);
    if (alErr.size() > 0) {
      boolean flag = false;
      for (int i = 0; i < alErr.size(); i++) {
        String[] sInvs = (String[])(String[])alErr.get(i);
        if ((sInvs[0] != null) || (sInvs[1] != null)) {
          flag = true;
          break;
        }
      }
      if (!flag)
        return ret;
    } else {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000040"));
    }

    Hashtable htInv = queryWH2(pk_psndoc, cwarehouseid, unitCode);
    alErr.clear();
    int length = vo.getChildrenVO().length;
    for (int i = 0; i < length; i++) {
      String sInv = (String)vo.getChildrenVO()[i].getAttributeValue("cinventoryid");
      if ((sInv == null) || (sInv.trim().length() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000125", null, new String[] { I18N.appendRemark(new Integer(i + 1).toString()) }));
      }
      if (!htInv.containsKey(sInv)) {
        String sCode = (String)vo.getChildrenVO()[i].getAttributeValue("cinventorycode");
        if (sCode == null)
          sCode = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000126", null, new String[] { I18N.appendRemark(new Integer(i + 1).toString()) });
        alErr.add(sCode);
      }
    }

    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000041"));
    if (alErr.size() > 0)
    {
      String msg = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000127");
      for (int i = 0; i < alErr.size(); i++) {
        msg = msg + I18N.appendRemark(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000099")) + (String)alErr.get(i);
      }
      throw new BusinessException(msg);
    }

    return ret;
  }

  public UFBoolean isInvControlledByWHManager(AggregatedValueObject vo)
    throws BusinessException, SQLException
  {
    UFBoolean ret = new UFBoolean(true);
    String cwhsmanagerid = (String)vo.getParentVO().getAttributeValue("cwhsmanagerid");
    String cwarehouseid = (String)vo.getParentVO().getAttributeValue("cwarehouseid");
    if (cwarehouseid == null)
      cwarehouseid = (String)vo.getParentVO().getAttributeValue("cwastewarehouseid");
    String unitCode = (String)vo.getParentVO().getAttributeValue("pk_corp");
    String sql = "";

    this.m_timer.start();

    if ((cwhsmanagerid == null) || (cwhsmanagerid.trim().length() < 1) || (cwarehouseid == null)) {
      return ret;
    }
    ArrayList alErr = queryWH1(cwhsmanagerid, cwarehouseid, unitCode);
    if (alErr.size() > 0) {
      boolean flag = false;
      for (int i = 0; i < alErr.size(); i++) {
        String[] sInvs = (String[])(String[])alErr.get(i);
        if ((sInvs[0] != null) || (sInvs[1] != null)) {
          flag = true;
          break;
        }
      }
      if (!flag)
        return ret;
    } else {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000040"));
    }

    Hashtable htInv = queryWH2(cwhsmanagerid, cwarehouseid, unitCode);
    alErr.clear();
    int length = vo.getChildrenVO().length;
    for (int i = 0; i < length; i++) {
      String sInv = (String)vo.getChildrenVO()[i].getAttributeValue("cinventoryid");
      if ((sInv == null) || (sInv.trim().length() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000125") + I18N.appendRemark(new Integer(i + 1).toString()) + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000130"));
      }

      if (!htInv.containsKey(sInv)) {
        String sCode = (String)vo.getChildrenVO()[i].getAttributeValue("cinventorycode");
        if (sCode == null)
          sCode = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000126") + I18N.appendRemark(new Integer(i + 1).toString()) + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000128");
        alErr.add(sCode);
      }
    }

    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000042"));
    if (alErr.size() > 0)
    {
      String msg = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000129");
      for (int i = 0; i < alErr.size(); i++) {
        msg = msg + I18N.appendRemark(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000099")) + (String)alErr.get(i);
      }
      throw new BusinessException(msg);
    }

    return ret;
  }

  public UFBoolean isModifiedByOther(AggregatedValueObject vo)
    throws BusinessException
  {
    return new UFBoolean(true);
  }

  public void isPicked(AggregatedValueObject vo)
    throws SQLException, BusinessException
  {
    if ((vo == null) || (vo.getParentVO() == null))
      return;
    String cgeneralhid = vo.getParentVO().getPrimaryKey();
    if (cgeneralhid == null)
      return;
    isPicked(cgeneralhid);
  }

  private ArrayList queryControlValue(String cwarehouseid, String[] sinvs, String unitCode)
    throws BusinessException
  {
    String sIC049 = "库存组织";
    SysInitBO initBO = null;

    initBO = new SysInitBO();
    String param = initBO.getParaString(unitCode, "IC049");
    if (param != null) {
      sIC049 = param.trim();
    }

    StringBuffer sql = null;

    Hashtable htMax = new Hashtable();
    Hashtable htMin = new Hashtable();
    Hashtable htSafe = new Hashtable();
    Hashtable htOrderPoint = new Hashtable();
    if (unitCode != null) {
      if (sIC049.equals("仓库")) {
        sql = new StringBuffer("select cinventoryid,nmaxstocknum,nminstocknum,nsafestocknum,norderpointnum from ic_numctl where  cwarehouseid='");

        sql.append(cwarehouseid);

        sql.append("' And pk_corp='");
        sql.append(unitCode);
        sql.append("' And (cinventoryid= '");
        sql.append(sinvs[0]);
        sql.append("'");
        for (int i = 1; i < sinvs.length; i++) {
          sql.append(" or cinventoryid= '");
          sql.append(sinvs[i]);
          sql.append("'");
        }
        sql.append(")");
      }
      else {
        sql = new StringBuffer("select bd_produce.pk_invmandoc,maxstornum,lowstocknum,safetystocknum ,zdhd from bd_produce inner join bd_stordoc on bd_produce.pk_calbody=bd_stordoc.pk_calbody where bd_stordoc.pk_stordoc='");

        sql.append(cwarehouseid);

        sql.append("' And bd_produce.pk_corp='");
        sql.append(unitCode);
        sql.append("' And (bd_produce.pk_invmandoc= '");
        sql.append(sinvs[0]);
        sql.append("'");
        for (int i = 1; i < sinvs.length; i++) {
          sql.append(" or bd_produce.pk_invmandoc= '");
          sql.append(sinvs[i]);
          sql.append("'");
        }
        sql.append(")");
      }

    }

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      rs = stmt.executeQuery();

      while (rs.next()) {
        String sInv = rs.getString(1).trim();
        BigDecimal nmaxstocknum = rs.getBigDecimal(2);
        if (nmaxstocknum != null) {
          htMax.put(sInv, new UFDouble(nmaxstocknum));
        }
        BigDecimal nminstocknum = rs.getBigDecimal(3);
        if (nminstocknum != null) {
          htMin.put(sInv, new UFDouble(nminstocknum));
        }
        BigDecimal nsafestocknum = rs.getBigDecimal(4);
        if (nsafestocknum != null)
          htSafe.put(sInv, new UFDouble(nsafestocknum));
        BigDecimal norderpointnum = rs.getBigDecimal(5);
        if (norderpointnum != null)
          htOrderPoint.put(sInv, new UFDouble(norderpointnum));
      }
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    finally {
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
    ArrayList alRes = new ArrayList();
    alRes.add(0, htMax);
    alRes.add(1, htMin);
    alRes.add(2, htSafe);
    alRes.add(3, htOrderPoint);
    return alRes;
  }

  private Hashtable queryNumByInv(String[] sPKs)
    throws SQLException
  {
    if ((sPKs == null) || (sPKs.length < 1))
      return null;
    StringBuffer sql = null;
    Hashtable htXcl = new Hashtable();

    sql = new StringBuffer("select cinventoryid, sum(ISNULL(ninnum,0.0))-sum(ISNULL(noutnum,0.0)) from ic_general_b  where 0=0 ").append(GeneralSqlString.formInSQL("cgeneralbid", sPKs));

    sql.append(" group by cinventoryid");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      rs = stmt.executeQuery();

      while (rs.next())
      {
        String sInv = rs.getString(1).trim();
        BigDecimal xcl = rs.getBigDecimal(2);
        if (xcl != null)
          htXcl.put(sInv, new UFDouble(xcl));
      }
    }
    finally {
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
    return htXcl;
  }

  private Hashtable queryValidate(ArrayList alData, String pk_corp, String cgeneralhid)
    throws BusinessException
  {
    if ((pk_corp == null) || (alData == null) || (alData.size() == 0)) {
      return new Hashtable();
    }
    ArrayList alValue = new ArrayList();
    for (int i = 0; i < alData.size(); i++)
    {
      ArrayList tmpStr = (ArrayList)alData.get(i);
      alValue.add((String)tmpStr.get(0) + (String)tmpStr.get(1));
    }

    try
    {
      Connection con = null;
      PreparedStatement stmt = null;

      con = getConnection();

      StringBuffer sql = null;
      Hashtable htRes = new Hashtable();

      sql = new StringBuffer("SELECT  pk_invbasdoc,vbatchcode,dvalidate FROM scm_batchcode where 0=0 ").append(GeneralSqlString.formInSQL("pk_invbasdoc||vbatchcode", alValue));

      ResultSet rs = null;
      try
      {
        stmt = con.prepareStatement(sql.toString());

        rs = stmt.executeQuery();

        while (rs.next()) {
          String sInv = rs.getString(1).trim();
          String sBatch = rs.getString(2).trim();
          String sDate = rs.getString(3);
          if ((sDate != null) && (!htRes.containsKey(sInv + "+" + sBatch)))
            htRes.put(sInv + "+" + sBatch, sDate);
        }
      }
      finally {
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
      return htRes;
    }
    catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return new Hashtable();
  }

  private ArrayList queryWH1(String cwhsmanagerid, String cwarehouseid, String unitCode)
    throws SQLException
  {
    String sql = "";
    if (unitCode != null) {
      sql = "select cinventoryclassid, cinventoryid,cwarehouseid from ic_storeadmin where cwhsmanagerid=? AND cwarehouseid=? AND pk_corp=?";
    }
    else {
      sql = "select cinventoryclassid, cinventoryid,cwarehouseid from ic_storeadmin where cwhsmanagerid=? AND cwarehouseid=? ";
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ArrayList alErr = new ArrayList();
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (unitCode != null) {
        stmt.setString(1, cwhsmanagerid);
        stmt.setString(2, cwarehouseid);
        stmt.setString(3, unitCode);
      } else {
        stmt.setString(1, cwhsmanagerid);
        stmt.setString(2, cwarehouseid);
      }
      rs = stmt.executeQuery();

      while (rs.next()) {
        String[] sInvs = new String[2];
        String sInvClass = rs.getString(1);
        sInvs[0] = (sInvClass == null ? null : sInvClass.trim());
        String sInv = rs.getString(2);
        sInvs[1] = (sInv == null ? null : sInv.trim());
        alErr.add(sInvs);
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
    return alErr;
  }

  private Hashtable queryWH2(String cwhsmanagerid, String cwarehouseid, String unitCode)
    throws SQLException
  {
    String sql = "";
    if (unitCode != null) {
      sql = " select cinventoryid,bd_invbasdoc.invcode from ic_storeadmin inner join bd_invmandoc on cinventoryid=bd_invmandoc.pk_invmandoc LEFT OUTER join bd_invbasdoc on bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc  WHERE ic_storeadmin.cwhsmanagerid =? AND  ic_storeadmin.cwarehouseid = ? and ic_storeadmin.pk_corp=? and cinventoryid is not null union (SELECT bd_invmandoc.pk_invmandoc as cinventoryid,bd_invbasdoc.invcode FROM bd_invcl bd_invcl1 INNER JOIN bd_invbasdoc ON bd_invcl1.pk_invcl = bd_invbasdoc.pk_invcl INNER JOIN  bd_invmandoc ON  bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc INNER JOIN  bd_invcl ON substring(bd_invcl1.invclasscode, 1, len(bd_invcl.invclasscode)) = bd_invcl.invclasscode INNER JOIN ic_storeadmin ON bd_invcl.pk_invcl = ic_storeadmin.cinventoryclassid WHERE ic_storeadmin.cwhsmanagerid = ? AND    ic_storeadmin.cwarehouseid =? and bd_invmandoc.pk_corp=? AND cinventoryid is null)";
    }

    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable htInv = new Hashtable();
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (unitCode != null) {
        stmt.setString(1, cwhsmanagerid);
        stmt.setString(2, cwarehouseid);
        stmt.setString(3, unitCode);
        stmt.setString(4, cwhsmanagerid);
        stmt.setString(5, cwarehouseid);
        stmt.setString(6, unitCode);
      }
      else {
        stmt.setString(1, cwhsmanagerid);
        stmt.setString(2, cwarehouseid);
        stmt.setString(3, cwhsmanagerid);
        stmt.setString(4, cwarehouseid);
      }

      rs = stmt.executeQuery();

      while (rs.next()) {
        String sInv = rs.getString(1);
        sInv = sInv == null ? null : sInv.trim();
        String sCode = rs.getString(2);
        htInv.put(sInv, sCode);
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
    return htInv;
  }

  public void setBillCode(AggregatedValueObject voBill)
    throws BusinessException
  {
    if ((voBill == null) || (!(voBill instanceof IBillCode)))
      return;
    IBillCode vo = (IBillCode)voBill;
    setBillCode(vo);
  }

  public void checkAllocationBillCodeFore(AggregatedValueObject vo)
    throws BusinessException
  {
    if (!(vo instanceof AllocationHVO))
      return;
    AllocationHVO voBill = (AllocationHVO)vo;

    if (voBill.getHeaderVO() == null)
      return;
    String vbillcode = (String)vo.getParentVO().getAttributeValue("vbillcode");
    String pk_corp = (String)vo.getParentVO().getAttributeValue("pk_corp");
    String hid = (String)vo.getParentVO().getAttributeValue("callocationhid");
    if (hid != null)
      return;
    if ((vbillcode == null) || (vbillcode.trim().length() == 0))
    {
      throw new BusinessException(ResBase.getIsnull() + ResBase.getBillcode());
    }this.m_timer.start();
    String sSql = "SELECT vbillcode FROM ic_allocation_h WHERE  vbillcode=? and pk_corp=? and dr=0 ";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean isFound = false;
    int count = 0;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, vbillcode);
      stmt.setString(2, pk_corp);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String code = rs.getString(1);
        count++;
      }

    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
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
    if (count > 0)
    {
      throw new BusinessException(Check.getBillcodeExisted() + ResBase.getBillcode() + voBill.getHeaderVO().getVbillcode());
    }this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000043"));
  }

  public void checkBillCodeFore(AggregatedValueObject vo)
    throws BusinessException
  {
    if (!(vo instanceof GeneralBillVO))
      return;
    GeneralBillVO voBill = (GeneralBillVO)vo;

    if (voBill.getHeaderVO() == null)
      return;
    String vbillcode = (String)vo.getParentVO().getAttributeValue("vbillcode");
    String pk_corp = (String)vo.getParentVO().getAttributeValue("pk_corp");
    String hid = (String)vo.getParentVO().getAttributeValue("cgeneralhid");
    if (hid != null)
      return;
    if ((vbillcode == null) || (vbillcode.trim().length() == 0))
      throw new BusinessException(ResBase.getIsnull() + ResBase.getBillcode());
    this.m_timer.start();
    String sSql = "SELECT vbillcode FROM ic_general_h WHERE  vbillcode=? and pk_corp=? and dr=0 ";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean isFound = false;
    int count = 0;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, vbillcode);
      stmt.setString(2, pk_corp);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String code = rs.getString(1);
        count++;
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
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
    if (count > 0)
      throw new BillCodeNotUnique(Check.getBillcodeExisted() + ResBase.getBillcode() + voBill.getHeaderVO().getVbillcode());
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000043"));
  }

  public void checkBillFlargessAvailable(AggregatedValueObject vo)
    throws BusinessException
  {
    if (!(vo instanceof GeneralBillVO)) {
      return;
    }
    GeneralBillVO voBill = (GeneralBillVO)vo;
    if (voBill.getHeaderVO() == null) {
      return;
    }

    if (vo.getParentVO().getAttributeValue("freplenishflag") == null) {
      return;
    }
    UFBoolean bReturn = (UFBoolean)vo.getParentVO().getAttributeValue("freplenishflag");

    if (!bReturn.booleanValue()) {
      return;
    }
    GeneralBillItemVO[] voBillItem = (GeneralBillItemVO[])(GeneralBillItemVO[])voBill.getChildrenVO();
    String sCinventoryid = null; String sCfirstbillbid = null;
    UFBoolean bFlargess = null;
    StringBuffer sbFistBillBid = new StringBuffer("");
    int n = 0;
    for (int i = 0; i < voBillItem.length; i++) {
      bFlargess = voBillItem[i].getFlargess();
      if ((bFlargess != null) && (bFlargess.booleanValue())) {
        sCinventoryid = voBillItem[i].getCinventoryid();
        sCfirstbillbid = voBillItem[i].getCfirstbillbid();
        if (sCfirstbillbid != null) {
          if (n > 0)
            sbFistBillBid.append(",");
          sbFistBillBid.append("'" + sCfirstbillbid + "'");
          n++;
        }
      }

    }

    if (sbFistBillBid.length() == 0) {
      return;
    }
    this.m_timer.start();
    StringBuffer sbSql = new StringBuffer("");
    sbSql.append("SELECT  vfirstbillcode, cinventoryid FROM ic_general_b WHERE  csourcetype='23' and (dr = 0) and flargess='Y'  and cfirstbillbid in(" + sbFistBillBid.toString() + ")  GROUP BY vfirstbillcode, cinventoryid HAVING (SUM(ninnum) < 0)");

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    boolean isFound = false;
    int count = 0;
    ArrayList alErr = new ArrayList();
    try
    {
      con = getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sbSql.toString());
      String cfirstbillbid = null;
      while (rs.next()) {
        cfirstbillbid = rs.getString(1);

        alErr.add(cfirstbillbid);
      }

    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
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
    StringBuffer sbError = new StringBuffer("");
    if (alErr.size() > 0) {
      sbError.append(Check.getFlargessNum() + ResBase.getFirstBillCode() + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000099") + "\n");
      for (int i = 0; i < alErr.size(); i++) {
        if (i > 0)
          sbError.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000100"));
        sbError.append(alErr.get(i));
      }

      throw new BusinessException(sbError.toString());
    }

    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000044"));
  }

  public void checkBillHeadTimeStamp(GeneralBillVO voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getHeaderVO() == null) || (voNowBill.getItemVOs() == null) || (voNowBill.getItemVOs().length == 0))
    {
      return;
    }
    this.m_timer.start();
    try
    {
      String sNowHeadPK = voNowBill.getHeaderVO().getPrimaryKey();
      String sNowHeadTs = voNowBill.getHeaderVO().getTs();
      String sOrgTs = null; String sNowTs = null;

      if ((sNowHeadTs != null) && (sNowHeadPK != null))
      {
        GeneralBillDMO dmoBill = new GeneralBillDMO();
        String sHeadTs = dmoBill.queryBillHeadTs(sNowHeadPK);

        if (!sNowHeadTs.equals(sHeadTs)) {
          SCMEnv.out("1====" + sNowHeadTs + "2====" + sHeadTs);
          throw new BusinessException("1:" + this.m_sTsHint);
        }
        dmoBill = null;
      }
      this.m_timer.stopAndShow(" ts ");
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }

  public void checkBillisReferedOrder(AggregatedValueObject vo)
    throws BusinessException
  {
    if (!(vo instanceof GeneralBillVO)) {
      return;
    }
    GeneralBillVO voBill = (GeneralBillVO)vo;
    if ((voBill.getHeaderVO() == null) || (voBill.getChildrenVO() == null) || (voBill.getChildrenVO().length == 0))
    {
      return;
    }
    String sCorpID = voBill.getHeaderVO().getPk_corp();
    if (sCorpID == null) {
      return;
    }

    if (vo.getParentVO().getAttributeValue("freplenishflag") == null) {
      return;
    }
    UFBoolean bReturn = (UFBoolean)vo.getParentVO().getAttributeValue("freplenishflag");

    if (!bReturn.booleanValue()) {
      return;
    }
    GeneralBillItemVO[] voBillItem = (GeneralBillItemVO[])(GeneralBillItemVO[])voBill.getChildrenVO();
    String[] aryBID = null;
    int n = 0;
    ArrayList alBID = new ArrayList();
    String sBid = null;

    for (int i = 0; i < voBillItem.length; i++) {
      sBid = voBillItem[i].getCgeneralbid();
      if ((sBid != null) && (sBid.length() > 0)) {
        alBID.add(sBid);
      }

    }

    if ((alBID != null) && (alBID.size() > 0)) {
      aryBID = new String[alBID.size()];
      alBID.toArray(aryBID);
      try
      {
        CreatecorpDMO dmo = new CreatecorpDMO();
        boolean isPurUsed = dmo.isEnabled(sCorpID, "PO");
        if (isPurUsed)
        {
          IPuToIc_ToIC dmo1 = (IPuToIc_ToIC)NCLocator.getInstance().lookup(IPuToIc_ToIC.class.getName());

          dmo1.isRowReferedInOrder(aryBID);
        }
      } catch (Exception e) {
        nc.bs.ic.pub.GenMethod.throwBusiException(e);
      }
    }
  }

  public void checkCalBodyInv_New(AggregatedValueObject vo)
    throws BusinessException
  {
    if ((vo == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0)) {
      return;
    }

    String cwarehouseid = (String)vo.getParentVO().getAttributeValue("cwarehouseid");

    String ccalbodyid = (String)vo.getParentVO().getAttributeValue("pk_calbody");
    String cwarehousename = (String)vo.getParentVO().getAttributeValue("cwarehousename");

    String pk_corp = (String)vo.getParentVO().getAttributeValue("pk_corp");
    String subQuery = null;
    String hid = null;
    if ((vo instanceof GeneralBillVO)) {
      hid = (String)vo.getParentVO().getAttributeValue("cgeneralhid");
      subQuery = "select distinct cinventoryid from ic_general_b where cgeneralhid='" + hid + "'";
    }
    else if ((vo instanceof SpecialBillVO)) {
      hid = (String)vo.getParentVO().getAttributeValue("cspecialhid");
      subQuery = "select distinct cinventoryid from ic_special_b where cspecialhid='" + hid + "'";
    }

    String wastewhid = (String)vo.getParentVO().getAttributeValue("cwastewarehouseid");
    boolean bCheckWasteWh = false;
    String pk_calwaste = null;
    if ((wastewhid != null) && (cwarehouseid != null))
    {
      bCheckWasteWh = true;
      pk_calwaste = qryCalbodyByWh(wastewhid);
    }

    if (cwarehouseid == null) {
      cwarehouseid = (String)vo.getParentVO().getAttributeValue("cwastewarehouseid");
    }
    if ((cwarehouseid == null) || (pk_corp == null) || (hid == null)) {
      return;
    }

    this.m_timer.start();
    Hashtable htAccessibleInv = null;
    Hashtable htWaste = null;
    Hashtable htAll = null;
    try
    {
      htAccessibleInv = queryCalbodyInv(ccalbodyid, cwarehouseid, pk_corp, subQuery);

      if ((bCheckWasteWh) && (pk_calwaste != null) && (wastewhid != null))
      {
        htWaste = queryCalbodyInv(pk_calwaste, wastewhid, pk_corp, subQuery);
        htAll = hashUnion(htAccessibleInv, htWaste);
        htAccessibleInv = htAll;
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }

    GeneralBillItemVO[] voItems = ((GeneralBillVO)vo).getItemVOs();
    Hashtable htInv = new Hashtable();
    String inv = null;
    String invcode = null;
    ArrayList alInv = new ArrayList();

    for (int i = 0; i < voItems.length; i++) {
      if (voItems[i].getStatus() == 3)
        continue;
      inv = voItems[i].getCinventoryid();
      if ((htAccessibleInv.containsKey(inv)) || (htInv.containsKey(inv)))
        continue;
      invcode = voItems[i].getInvname() + "[" + voItems[i].getCinventorycode() + "]";
      if (invcode != null) {
        htInv.put(inv, invcode);
        alInv.add(invcode);
      }

    }

    if (alInv.size() > 0)
    {
      String msg = Check.getInvProperty();
      for (int i = 0; i < alInv.size(); i++) {
        msg = msg + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000099") + (String)alInv.get(i);
      }
//      throw new BusinessException(msg);
    }

    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000045"));
  }

  public Hashtable checkInvInCal(String cwarehouseid, String pk_corp, ArrayList alInvs)
    throws SQLException
  {
    return queryCalbodyInv(cwarehouseid, pk_corp, alInvs);
  }

  public void checkCargoVolumeOut(GeneralBillVO voCur)
    throws BusinessException
  {
    GeneralBillVO voExt = voCur;
    if (voExt == null) {
      return;
    }

    this.m_timer.start();
    StringBuffer sMsg = new StringBuffer();
    try
    {
      ArrayList alErr = queryCspaceLeftVolume(voExt);

      ArrayList alrow = null;
      if ((alErr != null) && (alErr.size() > 0))
        for (int i = 0; i < alErr.size(); i++) {
          alrow = (ArrayList)alErr.get(i);
          sMsg.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000046") + alrow.get(0).toString() + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000131") + alrow.get(1).toString() + "\n");
        }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      throw new BusinessException(e.getMessage());
    }

    if (sMsg.length() > 0)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000132") + "\n" + sMsg.toString());
  }

  public void checkCargoVolumeOut(GeneralBillVO voCur, GeneralBillVO voPre)
    throws BusinessException
  {
    GeneralBillVO voExt = getExtendVO(voCur, voPre);

    if (voExt == null) {
      return;
    }

    this.m_timer.start();
    StringBuffer sMsg = new StringBuffer();
    try
    {
      ArrayList alErr = queryCspaceLeftVolume(voExt);

      ArrayList alrow = null;
      if ((alErr != null) && (alErr.size() > 0))
        for (int i = 0; i < alErr.size(); i++) {
          alrow = (ArrayList)alErr.get(i);
          sMsg.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000046") + alrow.get(0).toString() + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000047") + alrow.get(1).toString() + "\n");
        }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      throw new BusinessException(e.getMessage());
    }

    if (sMsg.length() > 0)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000132") + "\n" + sMsg.toString());
  }

  private String checkConsistent(GeneralBillVO voBill)
  {
    GeneralBillItemVO[] voItems = voBill.getItemVOs();
    ArrayList alBarcodeErr = new ArrayList();
    BarCodeVO[] aryBarcodevos = null;
    BarCodeVO[] arySourceBarcodes = null;
    String sRowNum = null;
    int length = voBill.getChildrenVO().length;
    for (int i = 0; i < length; i++)
    {
      if ((!voItems[i].getBarcodeManagerflag().booleanValue()) && (voItems[i].getBarCodeVOs() == null))
        continue;
      aryBarcodevos = voItems[i].getBarCodeVOs();
      arySourceBarcodes = voItems[i].getSourceBarcode();
      sRowNum = voItems[i].getCrowno();
      Hashtable ht = new Hashtable();

      for (int k = 0; k < arySourceBarcodes.length; k++) {
        StringBuffer sbSBarCode = new StringBuffer();
        sbSBarCode.append(arySourceBarcodes[k].getVpackcode());
        sbSBarCode.append(arySourceBarcodes[k].getVbarcode());
        sbSBarCode.append(arySourceBarcodes[k].getVbarcodesub());
        ht.put(sbSBarCode.toString(), null);
      }

      for (int k = 0; k < aryBarcodevos.length; k++) {
        StringBuffer sbBarCode = new StringBuffer();
        sbBarCode.append(aryBarcodevos[k].getVpackcode());
        sbBarCode.append(aryBarcodevos[k].getVbarcode());
        sbBarCode.append(aryBarcodevos[k].getVbarcodesub());
        if (!ht.containsKey(sbBarCode.toString())) {
          alBarcodeErr.add(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000095") + sRowNum + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000133") + aryBarcodevos[k].getVpackcode() + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000100") + aryBarcodevos[k].getVbarcode() + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000100") + aryBarcodevos[k].getVbarcodesub() + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000101"));

          break;
        }
      }
    }

    StringBuffer sbMsg = new StringBuffer();
    String sMsg = null;
    if ((alBarcodeErr != null) && (alBarcodeErr.size() > 0)) {
      sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000134") + "\n");
      for (int i = 0; i < alBarcodeErr.size(); i++) {
        if (i > 0)
          sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000101") + "\n");
        sbMsg.append((String)alBarcodeErr.get(i));
      }
      sMsg = sbMsg.toString();
    }

    return sMsg;
  }

  public void checkDBL_New(AggregatedValueObject voCur)
    throws BusinessException
  {
    this.m_timer.start();

    if ((voCur == null) || (voCur.getChildrenVO() == null) || (voCur.getChildrenVO().length == 0))
    {
      return;
    }

    GeneralBillVO checkVO = null;
    if (!(voCur instanceof GeneralBillVO)) {
      return;
    }
    checkVO = (GeneralBillVO)voCur;

    GeneralBillHeaderVO voCurHead = checkVO.getHeaderVO();
    if ((voCurHead.getPk_corp() == null) || (voCurHead.getCwarehouseid() == null))
    {
      return;
    }

    if (((checkVO.getItemValue(0, "ninnum") == null) || (checkVO.getItemValue(0, "ninnum").toString().trim().length() == 0)) && ((checkVO.getItemValue(0, "noutnum") == null) || (checkVO.getItemValue(0, "noutnum").toString().trim().length() == 0)) && ((checkVO.getItemValue(0, "ningrossnum") == null) || (checkVO.getItemValue(0, "ningrossnum").toString().trim().length() == 0)) && ((checkVO.getItemValue(0, "noutgrossnum") == null) || (checkVO.getItemValue(0, "noutgrossnum").toString().trim().length() == 0)) && (checkVO.getItemVOs()[0].getLocator() == null))
    {
      return;
    }

    ArrayList alErr = null;

    if ((voCurHead.getIsLocatorMgt() != null) && (voCurHead.getIsLocatorMgt().intValue() == 1)) {
      alErr = getAlErr_CS(checkVO);

      if ((alErr == null) || (alErr.size() == 0)) {
        alErr = new ArrayList();
        ArrayList alErr1 = getAlErr_WH(checkVO);
        if (alErr1 != null)
          alErr.addAll(alErr1);
      }
    }
    else
    {
      alErr = getAlErr_WH(checkVO);
    }

    if ((alErr != null) && (alErr.size() > 0)) {
      StringBuffer sMsg = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000048"));
      for (int i = 0; i < alErr.size(); i++) {
        sMsg.append("\n" + (String)alErr.get(i));
      }
      throw new BusinessException(sMsg.toString());
    }
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000049"));
  }

  public void checkDBL_New(AggregatedValueObject voCur, AggregatedValueObject voOld)
    throws BusinessException
  {
    this.m_timer.start();
    if (((voCur == null) || (voCur.getChildrenVO() == null)) && ((voOld == null) || (voOld.getChildrenVO() == null)))
    {
      return;
    }

    GeneralBillVO vo = getExtendVO((GeneralBillVO)voCur, (GeneralBillVO)voOld);
    checkDBL_New(vo);

    this.m_timer.stopAndShow("checkDBL_New");

//    checkInOutTrace_NEW(vo);
    this.m_timer.stopAndShow("checkInOutTrace_NEW");
  }

  public void checkFixSpace(AggregatedValueObject voCur)
    throws BusinessException
  {
    if ((voCur == null) || (voCur.getParentVO() == null) || (voCur.getChildrenVO() == null) || (voCur.getChildrenVO().length == 0))
    {
      return;
    }
    GeneralBillItemVO[] voItems = (GeneralBillItemVO[])(GeneralBillItemVO[])voCur.getChildrenVO();

    boolean isNeedcheck = false;
    for (int i = 0; i < voItems.length; i++) {
      if ((voItems[i] != null) && (voItems[i].getLocator() != null) && (voItems[i].getLocator().length > 0)) {
        isNeedcheck = true;
        break;
      }
    }

    if (!isNeedcheck) {
      return;
    }

    this.m_timer.start();
    StringBuffer sqlbusi = new StringBuffer("\tselect distinct cinventoryid,invclasscode,cspaceid,invcode from ic_keep_detail2 ").append("\tleft outer join bd_invmandoc inner join bd_invbasdoc left outer join bd_invcl ").append(" \ton bd_invbasdoc.pk_invcl=bd_invcl.pk_invcl ").append("\ton bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc ").append("\ton ic_keep_detail2.cinventoryid=bd_invmandoc.pk_invmandoc ").append(" where cgeneralhid=? ");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer sErr = null;
    String sInv = null;
    String sInvClass = null;
    String cspace = null;
    String invcode = null;

    String sCodeSet = null;
    Hashtable htErr = new Hashtable();
    Hashtable htSpace = null;
    try {
      GeneralBillHeaderVO voHead = (GeneralBillHeaderVO)voCur.getParentVO();
      String pk_corp = voHead.getPk_corp();
      String cwarehouseid = voHead.getCwarehouseid();
      String cgenerahid = voHead.getCgeneralhid();
      Hashtable htInv = queryFixSpace(cwarehouseid);

      if ((htInv == null) || (htInv.size() == 0)) return;
      con = getConnection();
      stmt = con.prepareStatement(sqlbusi.toString());

      stmt.setString(1, cgenerahid);
      rs = stmt.executeQuery();

      while (rs.next()) {
        if (sErr == null) {
          sErr = new StringBuffer();
        }
        sInv = rs.getString(1).trim();
        sInvClass = rs.getString(2).trim();
        cspace = rs.getString(3).trim();
        invcode = rs.getString(4).trim();
        if (htErr.containsKey(sInv))
          continue;
        htSpace = null;

        if (htInv.containsKey(sInv)) {
          htSpace = (Hashtable)htInv.get(sInv);
        }
        else
        {
          sCodeSet = findSubCode(sInvClass, htInv);
          if (sCodeSet != null) {
            htSpace = (Hashtable)htInv.get(sCodeSet);
          }
        }
        if ((htSpace != null) && (!htSpace.containsKey(cspace))) {
          htErr.put(sInv, invcode);
          sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000104")).append(invcode).append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000135"));
        }
      }
    }
    catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000050") + e.getMessage());
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
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000051"));

    if ((htErr != null) && (htErr.size() > 0))
      throw new BusinessException(sErr.toString());
  }

  public void checkInOutTrace_NEW(AggregatedValueObject avo)
    throws BusinessException
  {
    if ((avo == null) || (avo.getParentVO() == null) || (avo.getChildrenVO() == null))
      return;
    this.m_timer.start();

    GeneralBillVO gvo = (GeneralBillVO)avo;
    GeneralBillItemVO[] itemvos = gvo.getItemVOs();
    Hashtable htInv = new Hashtable();
    Hashtable htbid = new Hashtable();
    String key = null;
    ArrayList alinv = new ArrayList();
    ArrayList albid = null;

    for (int i = 0; i < itemvos.length; i++)
    {
      if ((itemvos[i].getNinnum() == null) && (itemvos[i].getNoutnum() == null)) {
        continue;
      }
      key = itemvos[i].getCgeneralbid();

      htbid.put(itemvos[i].getCgeneralbid(), itemvos[i]);

      if (itemvos[i].getCcorrespondbid() != null) {
        htbid.put(itemvos[i].getCcorrespondbid(), itemvos[i]);
      }
      if (htInv.containsKey(itemvos[i].getCinventoryid())) {
        albid = (ArrayList)htInv.get(itemvos[i].getCinventoryid());
      } else {
        albid = new ArrayList();
        htInv.put(itemvos[i].getCinventoryid(), albid);
      }

      if (itemvos[i].getCcorrespondbid() != null)
        albid.add(itemvos[i].getCcorrespondbid());
      else {
        albid.add(key);
      }
      alinv.add(itemvos[i].getCinventoryid());
    }

    if ((htInv == null) || (htInv.size() == 0)) {
      return;
    }

    String sqlTest = " select  pk_invmandoc from bd_invmandoc where outtrackin='Y' " + GeneralSqlString.formInSQL("pk_invmandoc", alinv);

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer sErr = new StringBuffer();
    ArrayList alErr = new ArrayList();
    alinv.clear();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sqlTest);
      rs = stmt.executeQuery();

      while (rs.next()) {
        alinv.add(rs.getString(1));
      }

      stmt.close();

      ArrayList aldd = null;
      if (alinv.size() > 0) {
        ArrayList altmp = null;
        aldd = new ArrayList();
        for (int i = 0; i < alinv.size(); i++) {
          altmp = (ArrayList)htInv.get(alinv.get(i));
          if ((altmp != null) && (altmp.size() > 0)) {
            aldd.addAll(altmp);
          }

        }

        String cgeneralhid = gvo.getHeaderVO().getCgeneralhid();
        StringBuffer sql = new StringBuffer();

        sql.append(" select cgeneralbid,num,assistnum from (select cgeneralbid,sum(isnull(ninnum,0)-isnull(noutnum,0)-isnull(ncorrespondnum,0)) as num ,sum(isnull(ninassistnum,0)-isnull(noutassistnum,0)-isnull(ncorrespondastnum,0)) as assistnum ").append(" from ic_general_b where dr=0 ").append(GeneralSqlString.formInSQL("cgeneralbid", aldd)).append(" group by cgeneralbid) a ").append(" where num<0 or assistnum<0");

        stmt = con.prepareStatement(sql.toString());

        rs = stmt.executeQuery();

        String invid = null;
        BigDecimal num = null;
        BigDecimal assistnum = null;
        GeneralBillItemVO voItem = null;

        while (rs.next()) {
          invid = rs.getString(1);
          num = rs.getBigDecimal("num");
          assistnum = rs.getBigDecimal("assistnum");
          if (htbid.containsKey(invid)) {
            voItem = (GeneralBillItemVO)htbid.get(invid);
            sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000104")).append(voItem.getInvname() + "[" + voItem.getCinventorycode() + "]");
            if (voItem.getVfree0() != null) {
              sErr.append(voItem.getVfree0());
            }
            if (voItem.getVbatchcode() != null) {
              sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000116")).append(voItem.getVbatchcode());
            }
            if (voItem.getCastunitid() != null) {
              sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000117")).append(voItem.getCastunitname());
            }
            if (num != null) {
              sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000136")).append(num.toString());
            }
            if (voItem.getCastunitid() != null) {
              sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000137")).append(assistnum.toString());
            }
            alErr.add(sErr.toString());
            sErr = new StringBuffer();
          }
        }
      }
    }
    catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000052") + e.getMessage());
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
      catch (Exception e) {
      }
    }
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000053"));

    if ((alErr != null) && (alErr.size() > 0)) {
      sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000054") + "\n");
      for (int i = 0; i < alErr.size(); i++) {
        sErr.append((String)alErr.get(i) + "\n");
      }
      throw new BusinessException(sErr.toString());
    }
  }

  public void checkOutVOForSo(AggregatedValueObject avo)
    throws BusinessException
  {
    checkOutVO(avo);
  }

  public String checkParam_new(AggregatedValueObject vo)
    throws BusinessException, SystemException
  {
    if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
    {
      return null;
    }GeneralBillVO voBill = (GeneralBillVO)vo;

    if (voBill.getHeaderVO().getCgeneralhid() == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000055"));
    }

    boolean isNeedcheck = false;
    GeneralBillItemVO[] voItems = voBill.getItemVOs();
    for (int i = 0; i < voItems.length; i++) {
      if ((voItems[i] != null) && ((voItems[i].getNinnum() != null) || (voItems[i].getNoutnum() != null))) {
        isNeedcheck = true;
        break;
      }
    }

    if (!isNeedcheck) {
      return null;
    }

    String pVIc014 = "Y";

    String pVIc016 = "Y";

    String pVIc018 = "Y";

    String pVIc049 = "库存组织";
    this.m_timer.start();

    Hashtable htParam = new Hashtable();
    try
    {
      String[] sParaCode = { "IC014", "IC016", "IC018", "IC049" };

      SysInitBO initBO = new SysInitBO();

      htParam = initBO.queryBatchParaValues(voBill.getHeaderVO().getPk_corp(), sParaCode);
    }
    catch (Exception e) {
      throw new SystemException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000056"));
    }

    if ((htParam != null) && (htParam.containsKey("IC014")))
      pVIc014 = (String)htParam.get("IC014");
    if ((htParam != null) && (htParam.containsKey("IC016")))
      pVIc016 = (String)htParam.get("IC016");
    if ((htParam != null) && (htParam.containsKey("IC018")))
      pVIc018 = (String)htParam.get("IC018");
    if ((htParam != null) && (htParam.containsKey("IC049")))
      pVIc049 = (String)htParam.get("IC049");
    boolean isCalbody = true;
    if (!"库存组织".equals(pVIc049)) {
      isCalbody = false;
    }
    int length = vo.getChildrenVO().length;

    Hashtable htXcl = null;
    Hashtable htCtrl = null;
    ArrayList alCtrlValue = null;
    try
    {
      htCtrl = queryCtrlValue(voBill, isCalbody);
      if ((htCtrl == null) || (htCtrl.size() == 0)) {
        return null;
      }

      htXcl = queryOnhandNum(voBill, isCalbody);
      if ((htXcl == null) || (htXcl.size() == 0))
        return null;
    } catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000057") + e.getMessage());
    }

    ArrayList alMaxErr = new ArrayList();
    ArrayList alMinErr = new ArrayList();
    ArrayList alSafeErr = new ArrayList();
    ArrayList alOrderPointErr = new ArrayList();
    String sInvCode = null;
    UFDouble xcl = null;
    String sInv = null;
    UFDouble maxNum = null;
    UFDouble minNum = null;
    UFDouble orderPointNum = null;
    UFDouble safeNum = null;

    Hashtable htInv = new Hashtable();
    ArrayList alset = null;
    for (int i = 0; i < length; i++)
    {
      sInv = voItems[i].getCinventoryid();
      sInvCode = voItems[i].getInvname() + "[" + voItems[i].getCinventorycode() + "]";

      if (htInv.containsKey(sInv)) {
        continue;
      }
      htInv.put(sInv, sInv);

      if (!htCtrl.containsKey(sInv)) {
        continue;
      }
      alset = (ArrayList)htCtrl.get(sInv);
      maxNum = (UFDouble)alset.get(0);
      minNum = (UFDouble)alset.get(1);
      safeNum = (UFDouble)alset.get(2);
      orderPointNum = (UFDouble)alset.get(3);

      if (htXcl.containsKey(sInv))
        xcl = (UFDouble)htXcl.get(sInv);
      else {
        xcl = this.ZERO;
      }
      if ((maxNum != null) && (xcl.compareTo(maxNum) > 0)) {
        alMaxErr.add(I18N.appendRemark(new StringBuilder().append(sInvCode).append("=").append(xcl.toString()).toString()) + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000150") + I18N.appendRemark(maxNum.toString()) + "\n");
      }

      if ((minNum != null) && (xcl.compareTo(minNum) < 0)) {
        alMinErr.add(I18N.appendRemark(new StringBuilder().append(sInvCode).append("=").append(xcl.toString()).toString()) + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000151") + I18N.appendRemark(minNum.toString()) + "\n");
      }

      if ((safeNum != null) && (xcl.compareTo(safeNum) < 0)) {
        alSafeErr.add(I18N.appendRemark(new StringBuilder().append(sInvCode).append("=").append(xcl.toString()).toString()) + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000152") + I18N.appendRemark(safeNum.toString()) + "\n");
      }

      if ((orderPointNum != null) && (xcl.compareTo(orderPointNum) < 0)) {
        alOrderPointErr.add(I18N.appendRemark(new StringBuilder().append(sInvCode).append("=").append(xcl.toString()).toString()) + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000153") + I18N.appendRemark(orderPointNum) + "\n");
      }

    }

    StringBuffer msg = new StringBuffer();

    if (alMaxErr.size() > 0) {
      msg.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000138"));
      for (int i = 0; i < alMaxErr.size(); i++) {
        msg.append(" " + (String)alMaxErr.get(i));
      }
      if (pVIc014.equals(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000139")))
      {
        throw new BusinessException(msg.toString());
      }

    }

    if (alMinErr.size() > 0) {
      msg.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000140"));
      for (int i = 0; i < alMinErr.size(); i++) {
        msg.append(" " + (String)alMinErr.get(i));
      }
      if (pVIc016.equals(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000139")))
      {
        throw new BusinessException(msg.toString());
      }

    }

    if (alSafeErr.size() > 0) {
      msg.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000141"));
      for (int i = 0; i < alSafeErr.size(); i++) {
        msg.append(" " + (String)alSafeErr.get(i));
      }
      if (pVIc018.equals(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000139")))
      {
        throw new BusinessException(msg.toString());
      }

    }

    if (alOrderPointErr.size() > 0) {
      msg.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000142"));
      for (int i = 0; i < alOrderPointErr.size(); i++)
        msg.append(" " + (String)alOrderPointErr.get(i));
    }
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000058"));

    if (msg.toString().trim().length() > 0) {
      return msg.toString();
    }
    return null;
  }

  public void checkPlaceAlone(AggregatedValueObject voCur)
    throws BusinessException
  {
    if ((voCur == null) || (voCur.getParentVO() == null) || (voCur.getChildrenVO() == null) || (voCur.getChildrenVO().length == 0))
    {
      return;
    }
    GeneralBillItemVO[] voItems = (GeneralBillItemVO[])(GeneralBillItemVO[])voCur.getChildrenVO();

    boolean isNeedcheck = false;

    for (int i = 0; i < voItems.length; i++) {
      if ((voItems[i] != null) && ((voItems[i].getNinnum() != null) || (voItems[i].getNoutnum() != null))) {
        isNeedcheck = true;
        break;
      }
    }

    if (!isNeedcheck) {
      return;
    }

    if ((voItems[0] == null) || (voItems[0].getLocator() == null) || (voItems[0].getLocator().length == 0)) {
      return;
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Hashtable htErr = new Hashtable();
    StringBuffer sErr = null;
    this.m_timer.start();
    try {
      GeneralBillHeaderVO voHead = (GeneralBillHeaderVO)voCur.getParentVO();
      String pk_corp = voHead.getPk_corp();
      String cwarehouseid = voHead.getCwarehouseid();
      String cgenerahid = voHead.getCgeneralhid();
      Hashtable htSpace = new Hashtable();
      Hashtable htSpaceClass = new Hashtable();
      Hashtable htInv = queryPlaceAlone(cwarehouseid);

      if ((htInv == null) || (htInv.size() == 0)) return;
      StringBuffer sqlbusi = new StringBuffer();
      sqlbusi.append(" select cspaceid,cinventoryid,invclasscode,invcode ,cscode from ")
      .append(" (select cspaceid,cinventoryid,sum(isnull(ninspacenum,0)-isnull(noutspacenum,0)) as num from v_ic_onhandnum2 ")
      .append(" where cwarehouseid=? and cspaceid in (select cspaceid from ic_general_bb1 where cgeneralbid in (select cgeneralbid from ic_general_b where cgeneralhid=? ) )")
      .append(" group by cspaceid,cinventoryid) icnum ")
      .append(" \tleft outer join bd_invmandoc inner join bd_invbasdoc left outer join bd_invcl ")
      .append("\ton bd_invbasdoc.pk_invcl=bd_invcl.pk_invcl")
      .append("\ton bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc")
      .append(" \ton icnum.cinventoryid=bd_invmandoc.pk_invmandoc")
      .append("  left outer join bd_cargdoc on icnum.cspaceid=bd_cargdoc.pk_cargdoc ")
      .append(" where icnum.num>0 and cspaceid <>'_________N/A________' ");//edit by shikun 2014-05-29 货位ID为_________N/A________时，货位编码为NULL报错

      String sInv = null;
      String sInvClass = null;
      String cspace = null;
      String invcode = null;
      String cscode = null;

      String sCodeSet = null;

      ArrayList alInv = null;
      ArrayList alInvClass = null;
      String inv1 = null;
      String code1 = null;
      String ss = null;

      con = getConnection();
      stmt = con.prepareStatement(sqlbusi.toString());
      stmt.setString(1, cwarehouseid);
      stmt.setString(2, cgenerahid);
      rs = stmt.executeQuery();

      while (rs.next()) {
        if (sErr == null)
          sErr = new StringBuffer();
        cspace = rs.getString(1).trim();
        sInv = rs.getString(2).trim();
        sInvClass = rs.getString(3).trim();
        invcode = rs.getString(4).trim();
        cscode = rs.getString(5)==null?"":rs.getString(5).trim();//edit by shikun 2014-05-29 货位ID为_________N/A________时，货位编码为NULL报错
        if (htErr.containsKey(sInv))
          continue;
        if (htSpace.containsKey(cscode)) {
          alInv = (ArrayList)htSpace.get(cscode);
        } else {
          alInv = new ArrayList();
          htSpace.put(cscode, alInv);
        }
        if (htSpaceClass.containsKey(cscode)) {
          alInvClass = (ArrayList)htSpaceClass.get(cscode);
        } else {
          alInvClass = new ArrayList();
          htSpaceClass.put(cscode, alInvClass);
        }

        if (alInv.size() < 1) {
          alInv.add(sInv);
          alInv.add(invcode);
        } else {
          inv1 = (String)alInv.get(0);
          if ((htInv.containsKey(inv1)) || (htInv.containsKey(sInv))) {
            if (htInv.containsKey(inv1))
              invcode = (String)alInv.get(1);
            htErr.put(cscode, invcode);
            sErr.append("\n" + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000143") + cscode + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000144") + invcode + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000145"));
          }

        }

        sCodeSet = findSubCode(sInvClass, htInv);

        if (alInvClass.size() < 1) {
          if (sCodeSet == null) {
            alInvClass.add(sInvClass);
            alInvClass.add(invcode);
            continue;
          }
          if (sInvClass.startsWith(sCodeSet)) {
            alInvClass.add(sCodeSet);
            alInvClass.add(invcode);
            continue;
          }
        }

        inv1 = (String)alInvClass.get(0);
        if ((sCodeSet != null) || (htInv.containsKey(inv1))) {
          if (htInv.containsKey(inv1)) {
            invcode = (String)alInvClass.get(1);
          }
          htErr.put(cscode, invcode);
          sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000143") + cscode + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000144") + invcode + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000145"));
        }
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000059") + e.getMessage());
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
      catch (Exception e) {
      }
    }
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000060"));

    if ((htErr != null) && (htErr.size() > 0))
      throw new BusinessException(sErr.toString());
  }

  public void checkSourceBillTimeStamp(AggregatedValueObject vo, String sNameHID, String sNameBID, String sNameSrcType, String sNameSrcHid, String sNameSrcBid, String sNameSrcHTs, String sNameSrcBTs)
    throws BusinessException
  {
    if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
    {
      return;
    }
    this.m_timer.start();

    if ((vo.getParentVO().getAttributeValue(sNameHID) != null) || (vo.getParentVO().getStatus() != 2)) {
      return;
    }

    String hts = null;

    String hid = null;
    Hashtable hthid = null;
    Hashtable htbid = null;
    Hashtable htTypeCode = new Hashtable();
    ArrayList alTypeCode = new ArrayList();
    ArrayList alh = null;

    CircularlyAccessibleValueObject[] voItems = vo.getChildrenVO();
    String billtype = null;
    String bid = null;
    String bts = null;

    for (int i = 0; i < voItems.length; i++)
    {
      if ((voItems[i].getAttributeValue(sNameSrcType) == null) || (voItems[i].getAttributeValue(sNameBID) != null))
      {
        continue;
      }

      billtype = (String)voItems[i].getAttributeValue(sNameSrcType);
      hid = (String)voItems[i].getAttributeValue(sNameSrcHid);
      hts = (String)voItems[i].getAttributeValue(sNameSrcHTs);
      if ((hid == null) || (hts == null))
        continue;
      bid = (String)voItems[i].getAttributeValue(sNameSrcBid);
      bts = (String)voItems[i].getAttributeValue(sNameSrcBTs);
      if ((bid != null) && (bts != null)) {
        if (htTypeCode.containsKey(billtype)) {
          hthid = (Hashtable)htTypeCode.get(billtype);
        } else {
          hthid = new Hashtable();
          htTypeCode.put(billtype, hthid);
          alTypeCode.add(billtype);
        }

        if (hthid.containsKey(hid)) {
          alh = (ArrayList)hthid.get(hid);
        } else {
          alh = new ArrayList();
          alh.add(hts);
          htbid = new Hashtable();
          alh.add(htbid);
          hthid.put(hid, alh);
        }
        htbid = (Hashtable)alh.get(1);
        htbid.put(bid, bts);
      }

    }

    if (alTypeCode.size() > 0) {
      for (int i = 0; i < alTypeCode.size(); i++) {
        billtype = (String)alTypeCode.get(i);
        hthid = (Hashtable)htTypeCode.get(billtype);
        Enumeration keys = hthid.keys();
        while (keys.hasMoreElements()) {
          hid = (String)keys.nextElement();
          alh = (ArrayList)hthid.get(hid);
          if (isSourceModified(billtype, hid, alh)) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000061"));
          }
        }
      }
    }
    this.m_timer.stopAndShow(" src ts ");
  }

  public void checkSourceBillTimeStamp_new(AggregatedValueObject vo)
    throws BusinessException
  {
    try
    {
      checkSourceBillTimeStamp(vo, "cgeneralhid", "cgeneralbid", "csourcetype", "csourcebillhid", "csourcebillbid", "csourceheadts", "csourcebodyts");
    } catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0002745") + e.getMessage());
    }
  }

  public void checkSpecialBillCodeFore(AggregatedValueObject vo)
    throws BusinessException
  {
    if (!(vo instanceof SpecialBillVO))
      return;
    SpecialBillVO voBill = (SpecialBillVO)vo;

    if (voBill.getHeaderVO() == null)
      return;
    String vbillcode = (String)vo.getParentVO().getAttributeValue("vbillcode");
    String pk_corp = (String)vo.getParentVO().getAttributeValue("pk_corp");
    String hid = (String)vo.getParentVO().getAttributeValue("cspecialhid");
    if (hid != null)
      return;
    if ((vbillcode == null) || (vbillcode.trim().length() == 0))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000062"));
    this.m_timer.start();
    String sSql = "SELECT vbillcode FROM ic_special_h WHERE  vbillcode=? and pk_corp=? and dr=0 ";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean isFound = false;
    int count = 0;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, vbillcode);
      stmt.setString(2, pk_corp);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String code = rs.getString(1);
        count++;
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000063") + e.getMessage());
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
    if (count > 0)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000064"));
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000043"));
  }

  public ArrayList checkTimeStamp(GeneralBillItemVO[] voaNowItem)
    throws BusinessException
  {
    if ((voaNowItem == null) || (voaNowItem.length == 0)) {
      return null;
    }
    this.m_timer.start();
    try
    {
      String sNowHeadPK = voaNowItem[0].getCgeneralhid();
      GeneralBillDMO dmoBill = new GeneralBillDMO();
      GeneralBillItemVO[] voaOrgItem = dmoBill.queryBillItemTs(sNowHeadPK);

      Hashtable htbOrgNowItemTs = new Hashtable();
      if ((voaOrgItem != null) && (voaOrgItem.length > 0)) {
        for (int j = 0; j < voaOrgItem.length; j++) {
          if ((voaOrgItem[j].getPrimaryKey() != null) && (voaOrgItem[j].getTs() != null)) {
            htbOrgNowItemTs.put(voaOrgItem[j].getPrimaryKey(), voaOrgItem[j].getTs());
          }
        }
      }

      int iNowOriginalRowNum = 0;

      String sOrgTs = null; String sNowTs = null;

      ArrayList alDirtyItem = new ArrayList();

      ArrayList alForUpdateItem = new ArrayList();

      for (int j = 0; j < voaNowItem.length; j++)
      {
        sOrgTs = null;

        sNowTs = voaNowItem[j].getTs();
        if ((voaOrgItem[j].getPrimaryKey() != null) && (htbOrgNowItemTs.containsKey(voaNowItem[j].getPrimaryKey())))
        {
          sOrgTs = (String)htbOrgNowItemTs.get(voaNowItem[j].getPrimaryKey());
        }if ((sOrgTs != null) && (!sOrgTs.equals(sNowTs)))
        {
          alDirtyItem.add(voaNowItem[j]);
        }
        else {
          alForUpdateItem.add(voaNowItem[j]);
        }
      }

      ArrayList alresult = new ArrayList();
      alresult.add(alDirtyItem);
      alresult.add(alForUpdateItem);
      this.m_timer.stopAndShow(" ts ");
      return alresult;
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  public void checkTimeStamp(AllocationHVO voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getHeaderVO() == null) || (voNowBill.getItemVOs() == null) || (voNowBill.getItemVOs().length == 0))
    {
      return;
    }
  }

  public void checkTimeStamps(GeneralBillVO[] voBills)
    throws BusinessException
  {
    if ((voBills == null) || (voBills.length == 0))
      return;
    ArrayList alErr = new ArrayList();
    this.m_timer.start();
    try
    {
      int ilen = voBills.length;
      String sHeadTs = null;
      String hid = null;
      Hashtable htNowTs = new Hashtable();
      Hashtable htDbTs = new Hashtable();
      ArrayList alhid = new ArrayList();
      for (int i = 0; i < voBills.length; i++) {
        hid = voBills[i].getHeaderVO().getCgeneralhid();
        sHeadTs = voBills[i].getHeaderVO().getTs();
        alhid.add(hid);
        if ((hid != null) && (sHeadTs != null)) {
          htNowTs.put(hid, sHeadTs);
        }
        else {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000065"));
        }

      }

      if (alhid.size() > 0) {
        String sDbHeadTs = null;

        GeneralBillDMO dmoBill = new GeneralBillDMO();
        htDbTs = dmoBill.queryHashHeadTs(alhid);
        if (htDbTs.size() == 0) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000066"));
        }
        for (int i = 0; i < voBills.length; i++) {
          hid = voBills[i].getHeaderVO().getCgeneralhid();
          sHeadTs = voBills[i].getHeaderVO().getTs();
          if (htDbTs.containsKey(hid)) {
            sDbHeadTs = (String)htDbTs.get(hid);
            if (!sDbHeadTs.equals(sHeadTs))
              alErr.add(voBills[i].getHeaderVO().getVbillcode());
          }
          else
          {
            alErr.add(voBills[i].getHeaderVO().getVbillcode());
          }

        }

      }

    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
    if (alErr.size() > 0) {
      StringBuffer sMsg = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000067"));
      for (int i = 0; i < alErr.size(); i++) {
        sMsg.append((String)alErr.get(i)).append("\n");
      }

      throw new BusinessException(sMsg.toString());
    }
  }

  public void checkValidInvs(AggregatedValueObject voNowBill)
    throws BusinessException
  {
    if ((voNowBill == null) || (voNowBill.getParentVO() == null) || (voNowBill.getChildrenVO() == null) || (voNowBill.getChildrenVO().length == 0))
    {
      return;
    }this.m_timer.start();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Object oValue = null;
    ArrayList alpkinvman = new ArrayList();

    for (int i = 0; i < voNowBill.getChildrenVO().length; i++) {
      alpkinvman.add((String)voNowBill.getChildrenVO()[i].getAttributeValue("cinventoryid"));
    }

    if (alpkinvman.size() == 0) {
      return;
    }

    String sWhere = GeneralSqlString.formInSQL("pk_invmandoc", alpkinvman);
    Hashtable htInvPK = null;
    try
    {
      String pk_invmandoc = null;
      con = getConnection();
      htInvPK = new Hashtable();

      String sql = "SELECT pk_invmandoc FROM bd_invmandoc WHERE dr=0   " + sWhere;
      stmt = con.prepareStatement(sql);

      rs = stmt.executeQuery();

      while (rs.next()) {
        pk_invmandoc = rs.getString(1);
        if (pk_invmandoc != null)
          htInvPK.put(pk_invmandoc, pk_invmandoc);
      }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000068") + e.getMessage());
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
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000024"));

    String sErrorMsg = "";
    for (int i = 0; i < voNowBill.getChildrenVO().length; i++) {
      if (!htInvPK.containsKey((String)voNowBill.getChildrenVO()[i].getAttributeValue("cinventoryid")))
        sErrorMsg = sErrorMsg + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000146") + (String)voNowBill.getChildrenVO()[i].getAttributeValue("cinventoryid") + NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000147");
    }
    if (sErrorMsg.trim().length() > 0)
      throw new BusinessException(sErrorMsg);
  }

  private String findSubCode(String code, Hashtable htCode)
  {
    if ((code == null) || (htCode == null))
      return null;
    String subCode = null;
    Enumeration enumKey = htCode.keys();

    while (enumKey.hasMoreElements()) {
      Object obj = enumKey.nextElement().toString().trim();
      if (code.startsWith((String)obj)) {
        subCode = (String)obj;
      }
    }

    return subCode;
  }

  private ArrayList getAlErr_CS(AggregatedValueObject voCur)
    throws BusinessException
  {
    ArrayList alErr = new ArrayList();

    GeneralBillVO checkVO = (GeneralBillVO)voCur;
    GeneralBillHeaderVO voCurHead = checkVO.getHeaderVO();
    UFDouble[] nums = null;
    Hashtable ht = queryOnhandNum_CS(checkVO);

    if ((ht != null) && (ht.size() > 0)) {
      GeneralBillItemVO[] voItems = checkVO.getItemVOs();
      StringBuffer key = null;
      String key1 = null;
      StringBuffer sErr = null;
      LocatorVO[] voLocs = null;
      LocatorVO voLoc = null;
      for (int i = 0; i < voItems.length; i++) {
        InvVO invvo = voItems[i].getInv();
        key = new StringBuffer();
        key.append(voItems[i].getCinventoryid()).append(voItems[i].getVfree1()).append(voItems[i].getVfree2()).append(voItems[i].getVfree3()).append(voItems[i].getVfree4()).append(voItems[i].getVfree5()).append(voItems[i].getVfree6()).append(voItems[i].getVfree7()).append(voItems[i].getVfree8()).append(voItems[i].getVfree9()).append(voItems[i].getVfree10()).append(voItems[i].getVbatchcode()).append(voItems[i].getCastunitid());

        voLocs = voItems[i].getLocator();
        if (voLocs != null) {
          for (int j = 0; j < voLocs.length; j++) {
            if (voLocs[j] == null)
              continue;
            if (nc.vo.ic.pub.GenMethod.isNull(voLocs[j].getCspaceid())) {
              continue;
            }
            key1 = key.toString() + voLocs[j].getCspaceid();
            if ((invvo.getIssupplierstock() != null) && ((invvo.getIssupplierstock().toString().equals("1")) || (invvo.getIssupplierstock().equals("Y"))))
              key1 = key1 + voItems[i].getCvendorid();
            else
              key1 = key1 + this.sNULL;
            if ((invvo.getIsStoreByConvert() != null) && ((invvo.getIsStoreByConvert().toString().equals("1")) || (invvo.getIsStoreByConvert().equals("Y"))) && (voItems[i].getHsl() != null)) {
              UFDouble hsl = voItems[i].getHsl();
              hsl.setTrimZero(true);
              key1 = key1 + hsl.toString();
            } else {
              key1 = key1 + this.sNULL;
            }

            if (ht.containsKey(key1)) {
              sErr = new StringBuffer("");
              nums = (UFDouble[])(UFDouble[])ht.get(key1.toString());
              sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000104") + voItems[i].getInvname() + "<" + voItems[i].getCinventorycode() + ">");
              if (voItems[i].getVfree0() != null)
                sErr.append(" -" + voItems[i].getVfree0());
              if (voItems[i].getVbatchcode() != null)
                sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000116") + voItems[i].getVbatchcode());
              if (voItems[i].getCastunitname() != null)
                sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000117") + voItems[i].getCastunitname());
              if (voItems[i].getAttributeValue("vvendorname") != null) {
                sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000118") + voItems[i].getAttributeValue("vvendorname"));
                sErr.append(" ");
              }
              if (voItems[i].getHsl() != null) {
                sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000219") + voItems[i].getHsl());
                sErr.append(" ");
              }
              if (voLocs[j].getVspacename() != null) {
                sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000148") + voLocs[j].getVspacename());
              }

              sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000119") + nums[0].toString());
              sErr.append(" ");
              if (voItems[i].getCastunitname() != null) {
                sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000120") + nums[1].toString());
                sErr.append(" ");
              }
              if ((invvo.getIsmngstockbygrswt() != null) && ((invvo.getIsmngstockbygrswt().toString().equals("1")) || (invvo.getIsmngstockbygrswt().equals("Y"))))
              {
                sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000218") + nums[2].toString());
                sErr.append(" ");
              }

              alErr.add(sErr.toString());
              ht.remove(key1);
            }
          }

        }

      }

    }

    return alErr;
  }

  private ArrayList getAlErr_WH(AggregatedValueObject voCur)
    throws BusinessException
  {
    ArrayList alErr = new ArrayList();

    GeneralBillVO checkVO = (GeneralBillVO)voCur;
    GeneralBillHeaderVO voCurHead = checkVO.getHeaderVO();

    UFDouble[] nums = null;
    Hashtable ht = queryOnhandNum_WH(checkVO);

    boolean bVendor = checkVO.getWh().getIsgathersettle().booleanValue();

    if ((ht != null) && (ht.size() > 0)) {
      GeneralBillItemVO[] voItems = checkVO.getItemVOs();
      StringBuffer key = null;
      StringBuffer sErr = null;
      for (int i = 0; i < voItems.length; i++) {
        InvVO invvo = voItems[i].getInv();
        key = new StringBuffer();
        key.append(voItems[i].getCinventoryid()).append(voItems[i].getVfree1()).append(voItems[i].getVfree2()).append(voItems[i].getVfree3()).append(voItems[i].getVfree4()).append(voItems[i].getVfree5()).append(voItems[i].getVfree6()).append(voItems[i].getVfree7()).append(voItems[i].getVfree8()).append(voItems[i].getVfree9()).append(voItems[i].getVfree10()).append(voItems[i].getVbatchcode()).append(voItems[i].getCastunitid());

        if ((bVendor) || ((invvo.getIssupplierstock() != null) && ((invvo.getIssupplierstock().toString().equals("1")) || (invvo.getIssupplierstock().equals("Y")))))
          key.append(voItems[i].getCvendorid());
        else
          key.append(this.sNULL);
        if ((invvo.getIsStoreByConvert() != null) && ((invvo.getIsStoreByConvert().toString().equals("1")) || (invvo.getIsStoreByConvert().equals("Y"))) && (voItems[i].getHsl() != null)) {
          UFDouble hsl = voItems[i].getHsl();
          hsl.setTrimZero(true);
          key.append(hsl.toString());
        } else {
          key.append(this.sNULL);
        }
        if (ht.containsKey(key.toString())) {
          sErr = new StringBuffer("");
          nums = (UFDouble[])(UFDouble[])ht.get(key.toString());
          sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000104"));
          if (voItems[i].getInvname() != null)
            sErr.append(voItems[i].getInvname() + "<" + voItems[i].getCinventorycode() + ">");
          else sErr.append("<" + voItems[i].getCinventorycode() + ">");
          if (voItems[i].getVfree0() != null) {
            sErr.append("-" + voItems[i].getVfree0());
          }

          if (voItems[i].getVbatchcode() != null) {
            sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000116") + voItems[i].getVbatchcode());
          }

          if (voItems[i].getCastunitname() != null) {
            sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000117") + voItems[i].getCastunitname());
          }

          if (voItems[i].getVvendorname() != null)
          {
            sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000118") + voItems[i].getVvendorname());
            sErr.append(" ");
          }
          if (voItems[i].getHsl() != null) {
            sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000219") + voItems[i].getHsl());
            sErr.append(" ");
          }
          sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000119") + nums[0].toString());
          sErr.append(" ");
          if (voItems[i].getCastunitname() != null) {
            sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000120") + nums[1].toString());
            sErr.append(" ");
          }
          if ((invvo.getIsmngstockbygrswt() != null) && ((invvo.getIsmngstockbygrswt().toString().equals("1")) || (invvo.getIsmngstockbygrswt().equals("Y"))))
          {
            sErr.append(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000218") + nums[2].toString());
            sErr.append(" ");
          }

          alErr.add(sErr.toString());
          ht.remove(key.toString());
        }
      }

    }

    return alErr;
  }

  public String getBillCode(String billtype, String pkcorp, String custbillcode, BillCodeObjValueVO billVO)
    throws BusinessException
  {
    try
    {
      BillcodeGenerater bcrbo = new BillcodeGenerater();
      String billcode = bcrbo.getBillCode(billtype, pkcorp, custbillcode, billVO);
      return billcode;
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  private String getBodyColName(String billtype)
  {
    if (billtype == null)
      return null;
    String colname = null;
    if (billtype.equalsIgnoreCase("A3")) {
      colname = " pk_pickm_bid";
    } else if (billtype.equalsIgnoreCase("A4")) {
      colname = " pk_wr_bid";
    } else if (billtype.equalsIgnoreCase("3W")) {
      colname = " pk_retaildaily";
    } else if (billtype.equalsIgnoreCase("422X")) {
      colname = " crequireapp_bid";
    }
    else {
      colname = SourceBillParaV3.getBodyPkField(billtype);
      if ((colname != null) && (colname.trim().length() > 0)) {
        colname = " " + colname;
      } else {
        colname = null;
        SCMEnv.out("ERR:no info bpk-" + billtype);
      }
    }
    return colname;
  }

  private String getBodyColName_Old(String billtype)
  {
    if (billtype == null)
      return null;
    String colname = null;

    if ((billtype.equalsIgnoreCase("30")) || (billtype.equalsIgnoreCase("3A")))
      colname = " corder_bid";
    else if (billtype.equalsIgnoreCase("31"))
      colname = " creceipt_bid ";
    else if ((billtype.equalsIgnoreCase("32")) || (billtype.equalsIgnoreCase("3C")))
      colname = " cinvoice_bid ";
    else if ((billtype.equalsIgnoreCase("3U")) || (billtype.equalsIgnoreCase("3V")))
      colname = " pk_apply_b ";
    else if (billtype.equalsIgnoreCase("21"))
      colname = " corder_bid";
    else if (billtype.equalsIgnoreCase("23"))
      colname = " carriveorder_bid";
    else if (billtype.equalsIgnoreCase("61"))
      colname = " corder_bid";
    else if (billtype.equalsIgnoreCase("A3"))
      colname = " pk_pickm_bid";
    else if (billtype.equalsIgnoreCase("A4"))
      colname = " pk_wr_bid";
    else if ((billtype.equalsIgnoreCase("41")) || (billtype.equalsIgnoreCase("42")) || (billtype.equalsIgnoreCase("49")) || (billtype.equalsIgnoreCase("4H")) || (billtype.equalsIgnoreCase("4Q")) || (billtype.equalsIgnoreCase("4C")) || (billtype.equalsIgnoreCase("45")) || (billtype.equalsIgnoreCase("4D")) || (billtype.equalsIgnoreCase("4E")) || (billtype.equalsIgnoreCase("4F")) || (billtype.equalsIgnoreCase("4G")) || (billtype.equalsIgnoreCase("4I")) || (billtype.equalsIgnoreCase("4Y")) || (billtype.equalsIgnoreCase("4A")))
    {
      colname = " cgeneralbid";
    } else if ((billtype.equalsIgnoreCase("4K")) || (billtype.equalsIgnoreCase("4L")) || (billtype.equalsIgnoreCase("4M")) || (billtype.equalsIgnoreCase("4N")) || (billtype.equalsIgnoreCase("4R")))
    {
      colname = " cspecialbid";
    } else if (billtype.equalsIgnoreCase("4U"))
      colname = " callocationbid";
    else if (billtype.equalsIgnoreCase("7F"))
      colname = " pk_delivbill_b";
    else if (billtype.startsWith("5"))
      colname = " cbill_bid";
    return colname;
  }

  private String getBodyTableName(String billtype)
  {
    if (billtype == null)
      return null;
    String tablename = null;
    if (billtype.equalsIgnoreCase("A3")) {
      tablename = " mm_pickm_b";
    } else if (billtype.equalsIgnoreCase("A4")) {
      tablename = " mm_wr_b";
    } else if (billtype.equalsIgnoreCase("3W")) {
      tablename = " so_retaildaily";
    } else if (billtype.equalsIgnoreCase("422X")) {
      tablename = " po_requireapp_b";
    } else {
      tablename = SourceBillParaV3.getBodyTable(billtype);
      if ((tablename != null) && (tablename.trim().length() > 0)) {
        tablename = " " + tablename;
      } else {
        tablename = null;
        SCMEnv.out("ERR:no info b-" + billtype);
      }
    }
    return tablename;
  }

  private String getBodyTableName_Old(String billtype)
  {
    if (billtype == null)
      return null;
    String tablename = null;

    if ((billtype.equalsIgnoreCase("30")) || (billtype.equalsIgnoreCase("3A")))
      tablename = " so_saleorder_b";
    else if (billtype.equalsIgnoreCase("31"))
      tablename = " so_salereceipt_b ";
    else if ((billtype.equalsIgnoreCase("32")) || (billtype.equalsIgnoreCase("3C")))
      tablename = " so_saleinvoice_b ";
    else if ((billtype.equalsIgnoreCase("3U")) || (billtype.equalsIgnoreCase("3V")))
      tablename = " so_apply_b ";
    else if (billtype.equalsIgnoreCase("21"))
      tablename = " po_order_b";
    else if (billtype.equalsIgnoreCase("23"))
      tablename = " po_arriveorder_b";
    else if (billtype.equalsIgnoreCase("61"))
      tablename = " sc_order_b";
    else if (billtype.equalsIgnoreCase("A3"))
      tablename = " mm_pickm_b";
    else if (billtype.equalsIgnoreCase("A4"))
      tablename = " mm_wr_b";
    else if ((billtype.equalsIgnoreCase("41")) || (billtype.equalsIgnoreCase("42")) || (billtype.equalsIgnoreCase("49")) || (billtype.equalsIgnoreCase("4H")) || (billtype.equalsIgnoreCase("4Q")) || (billtype.equalsIgnoreCase("4C")) || (billtype.equalsIgnoreCase("45")) || (billtype.equalsIgnoreCase("4D")) || (billtype.equalsIgnoreCase("4E")) || (billtype.equalsIgnoreCase("4F")) || (billtype.equalsIgnoreCase("4G")) || (billtype.equalsIgnoreCase("4I")) || (billtype.equalsIgnoreCase("4Y")) || (billtype.equalsIgnoreCase("4A")))
    {
      tablename = " ic_general_b";
    } else if ((billtype.equalsIgnoreCase("4K")) || (billtype.equalsIgnoreCase("4L")) || (billtype.equalsIgnoreCase("4M")) || (billtype.equalsIgnoreCase("4N")) || (billtype.equalsIgnoreCase("4R")))
    {
      tablename = " ic_special_b";
    } else if (billtype.equalsIgnoreCase("4U"))
      tablename = " ic_allocation_b";
    else if (billtype.equalsIgnoreCase("7F"))
      tablename = " dm_delivbill_b";
    else if (billtype.startsWith("5"))
      tablename = " to_bill_b";
    return tablename;
  }

  private String getHeadColName(String billtype)
  {
    return nc.vo.ic.pub.GenMethod.getHeadColName(billtype);
  }

  private String getHeadColName_Old(String billtype)
  {
    if (billtype == null)
      return null;
    String colname = null;

    if ((billtype.equalsIgnoreCase("30")) || (billtype.equalsIgnoreCase("3A")))
      colname = " csaleid";
    else if (billtype.equalsIgnoreCase("31"))
      colname = " csaleid ";
    else if ((billtype.equalsIgnoreCase("32")) || (billtype.equalsIgnoreCase("3C")))
      colname = " csaleid ";
    else if ((billtype.equalsIgnoreCase("3U")) || (billtype.equalsIgnoreCase("3V")))
      colname = " pk_apply ";
    else if (billtype.equalsIgnoreCase("21"))
      colname = " corderid";
    else if (billtype.equalsIgnoreCase("23"))
      colname = " carriveorderid";
    else if (billtype.equalsIgnoreCase("61"))
      colname = " corderid";
    else if (billtype.equalsIgnoreCase("A3"))
      colname = " pk_pickmid";
    else if (billtype.equalsIgnoreCase("A4"))
      colname = " pk_wrid";
    else if ((billtype.equalsIgnoreCase("41")) || (billtype.equalsIgnoreCase("42")) || (billtype.equalsIgnoreCase("49")) || (billtype.equalsIgnoreCase("4H")) || (billtype.equalsIgnoreCase("4Q")) || (billtype.equalsIgnoreCase("4C")) || (billtype.equalsIgnoreCase("45")) || (billtype.equalsIgnoreCase("4D")) || (billtype.equalsIgnoreCase("4E")) || (billtype.equalsIgnoreCase("4F")) || (billtype.equalsIgnoreCase("4G")) || (billtype.equalsIgnoreCase("4I")) || (billtype.equalsIgnoreCase("4Y")) || (billtype.equalsIgnoreCase("4A")))
    {
      colname = " cgeneralhid";
    } else if ((billtype.equalsIgnoreCase("4K")) || (billtype.equalsIgnoreCase("4L")) || (billtype.equalsIgnoreCase("4M")) || (billtype.equalsIgnoreCase("4N")) || (billtype.equalsIgnoreCase("4R")))
    {
      colname = " cspecialhid";
    } else if (billtype.equalsIgnoreCase("4U"))
      colname = " callocationhid";
    else if (billtype.equalsIgnoreCase("7F"))
      colname = " pk_delivbill_h";
    else if (billtype.startsWith("5"))
      colname = " cbillid";
    return colname;
  }

  private String getHeadTableName(String billtype)
  {
    return nc.vo.ic.pub.GenMethod.getHeadTableName(billtype);
  }

  private String getHeadTableName_Old(String billtype)
  {
    if (billtype == null)
      return null;
    String tablename = null;

    if ((billtype.equalsIgnoreCase("30")) || (billtype.equalsIgnoreCase("3A")))
      tablename = " so_sale";
    else if (billtype.equalsIgnoreCase("31"))
      tablename = " so_salereceipt ";
    else if ((billtype.equalsIgnoreCase("32")) || (billtype.equalsIgnoreCase("3C")))
      tablename = " so_saleinvoice ";
    else if ((billtype.equalsIgnoreCase("3U")) || (billtype.equalsIgnoreCase("3V")))
      tablename = " so_apply ";
    else if (billtype.equalsIgnoreCase("21"))
      tablename = " po_order";
    else if (billtype.equalsIgnoreCase("23"))
      tablename = " po_arriveorder";
    else if (billtype.equalsIgnoreCase("61"))
      tablename = " sc_order";
    else if (billtype.equalsIgnoreCase("A3"))
      tablename = " mm_pickm";
    else if (billtype.equalsIgnoreCase("A4"))
      tablename = " mm_wr";
    else if ((billtype.equalsIgnoreCase("41")) || (billtype.equalsIgnoreCase("42")) || (billtype.equalsIgnoreCase("49")) || (billtype.equalsIgnoreCase("4H")) || (billtype.equalsIgnoreCase("4Q")) || (billtype.equalsIgnoreCase("4C")) || (billtype.equalsIgnoreCase("45")) || (billtype.equalsIgnoreCase("4D")) || (billtype.equalsIgnoreCase("4E")) || (billtype.equalsIgnoreCase("4F")) || (billtype.equalsIgnoreCase("4G")) || (billtype.equalsIgnoreCase("4I")) || (billtype.equalsIgnoreCase("4Y")) || (billtype.equalsIgnoreCase("4A")))
    {
      tablename = " ic_general_h";
    } else if ((billtype.equalsIgnoreCase("4K")) || (billtype.equalsIgnoreCase("4L")) || (billtype.equalsIgnoreCase("4M")) || (billtype.equalsIgnoreCase("4N")) || (billtype.equalsIgnoreCase("4R")))
    {
      tablename = " ic_special_h";
    } else if (billtype.equalsIgnoreCase("4U"))
      tablename = " ic_allocation_h";
    else if (billtype.equalsIgnoreCase("7F"))
      tablename = " dm_delivbill_h";
    else if (billtype.startsWith("5")) {
      tablename = " to_bill";
    }
    return tablename;
  }

  private GeneralBillVO getExtendVO(GeneralBillVO newVO, GeneralBillVO oldVO)
  {
    if ((newVO == null) && (oldVO == null)) {
      return newVO;
    }
    GeneralBillVO returnVO = null;

    GeneralBillItemVO[] gbivos = null;
    HashMap ht = new HashMap();
    boolean isDelete = false;
    if (newVO != null) {
      gbivos = newVO.getItemVOs();

      if ((oldVO != null) && (oldVO.getChildrenVO() != null)) {
        GeneralBillItemVO[] voItems = (GeneralBillItemVO[])(GeneralBillItemVO[])oldVO.getChildrenVO();
        for (int i = 0; i < voItems.length; i++) {
          if (voItems[i].getCgeneralbid() != null)
            ht.put(voItems[i].getCgeneralbid(), voItems[i]);
        }
      }
      else
      {
        isDelete = true;
      }
    }
    else {
      gbivos = oldVO.getItemVOs();
      isDelete = true;
    }

    if ((gbivos == null) || (gbivos.length == 0)) {
      return null;
    }
    String BID = null;
    ArrayList alRet = new ArrayList();
    GeneralBillItemVO voBody = null;
    for (int i = 0; i < gbivos.length; i++)
    {
      if (isDelete) {
        alRet.add(gbivos[i]);
      } else {
        if ((gbivos[i].getNinnum() == null) && (gbivos[i].getNoutnum() == null))
        {
          continue;
        }

        alRet.add(gbivos[i]);

        if ((gbivos[i].getStatus() != 3) && (gbivos[i].getStatus() != 1) && (gbivos[i].getLocStatus() != 1))
        {
          continue;
        }

        BID = gbivos[i].getCgeneralbid();
        if (ht.containsKey(BID)) {
          voBody = (GeneralBillItemVO)ht.get(BID);
          if ((voBody.getNinnum() == null) && (voBody.getNoutnum() == null))
          {
            continue;
          }

          alRet.add(voBody);
        }

      }

    }

    if (alRet.size() > 0) {
      returnVO = new GeneralBillVO();
      returnVO.setParentVO(newVO == null ? oldVO.getHeaderVO() : newVO.getHeaderVO());
      GeneralBillItemVO[] vobodies = new GeneralBillItemVO[alRet.size()];
      alRet.toArray(vobodies);
      returnVO.setChildrenVO(vobodies);
    }

    return returnVO;
  }

  public void insertBusinesslog(GeneralBillVO[] vo, String sMsg)
    throws BusinessException
  {
    if ((vo == null) || (vo.length == 0)) {
      return;
    }
    insertBusinesslog(vo[0], sMsg);
  }

  public void insertBusinesslog(GeneralBillVO[] vo, String sMsg, String sEnterbutton)
    throws BusinessException
  {
    if ((vo == null) || (vo.length == 0))
      return;
    for (int i = 0; i < vo.length; i++)
      insertBusinesslog(vo[i], sMsg, sEnterbutton);
  }

  public void insertBusinesslog(AggregatedValueObject[] vo, String sMsg)
    throws BusinessException
  {
    if ((vo == null) || (vo.length == 0)) {
      return;
    }
    insertBusinesslog(vo[0], sMsg);
  }

  public void insertBusinesslog(AggregatedValueObject[] vo, String sMsg, String sEnterbutton)
    throws BusinessException
  {
    if ((vo == null) || (vo.length == 0))
      return;
    for (int i = 0; i < vo.length; i++)
      insertBusinesslog(vo[i], sMsg, sEnterbutton);
  }

  public void insertBusinesslog(AggregatedValueObject vo, String sMsg)
    throws BusinessException
  {
    insertBusinesslog(vo, sMsg, "保存");
  }

  public void insertBusinesslog(AggregatedValueObject vo, String sMsg, String sEnterbutton)
    throws BusinessException
  {
    if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
    {
      return;
    }
    insertBusinesslog(vo, sMsg, sEnterbutton, Businesslog.MSGWARNING);
  }

  private void insertBusinesslog(AggregatedValueObject vo, String sMsg, String sEnterbutton, String sMessgageType)
    throws BusinessException
  {
    try
    {
      if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0))
      {
        return;
      }
      OperatelogVO operatelogvo = Operlog.getOperLogVO(sMsg, sEnterbutton, sMessgageType, vo);

      IOperateLogService log = (IOperateLogService)NCLocator.getInstance().lookup(IOperateLogService.class.getName());
      log.insert(operatelogvo);
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }

  public UFBoolean isInvInWareHouse(AggregatedValueObject vo)
    throws BusinessException, SQLException
  {
    UFBoolean ufRet = new UFBoolean(true);
    if ((vo == null) || (vo.getChildrenVO() == null)) {
      return ufRet;
    }
    this.m_timer.start();

    String cwarehouseid = (String)vo.getParentVO().getAttributeValue("cwarehouseid");

    String cwarehousename = (String)vo.getParentVO().getAttributeValue("cwarehousename");
    String pk_corp = (String)vo.getParentVO().getAttributeValue("pk_corp");
    String[] sInvs = null;
    Hashtable htInv = null;

    if ((cwarehouseid == null) || (pk_corp == null)) {
      return ufRet;
    }

    int len = vo.getChildrenVO().length;
    Vector v = new Vector();
    htInv = new Hashtable();
    for (int i = 0; i < len; i++) {
      String inv = (String)vo.getChildrenVO()[i].getAttributeValue("cinventoryid");
      if ((inv == null) || (inv.trim().length() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000149", null, new String[] { I18N.appendRemark(new Integer(i + 1).toString()) }));
      }
      String invcode = (String)vo.getChildrenVO()[i].getAttributeValue("cinventorycode");
      if (invcode == null)
        invcode = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000126", null, new String[] { I18N.appendRemark(new Integer(i + 1).toString()) });
      if ((inv != null) && (!htInv.containsKey(inv))) {
        v.addElement(inv);

        htInv.put(inv, invcode);
      }

    }

    if (v.size() > 0) {
      sInvs = new String[v.size()];
      v.copyInto(sInvs);
    }

    if (sInvs == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000070"));
    }
    Hashtable htAccessibleInv = null;
    if (htAccessibleInv == null) {
      htAccessibleInv = new Hashtable();
    }
    htAccessibleInv = queryWarehouseInv(cwarehouseid, pk_corp, sInvs);
    ArrayList alErr = new ArrayList();

    for (int i = 0; i < sInvs.length; i++) {
      if (!htAccessibleInv.containsKey(sInvs[i])) {
        alErr.add((String)htInv.get(sInvs[i]));
      }
    }
    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000071"));
    if (alErr.size() > 0)
    {
      String msg = NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000072") + I18N.appendRemark(cwarehousename) + "\n";
      for (int i = 0; i < alErr.size(); i++) {
        msg = msg + I18N.appendRemark(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000099")) + (String)alErr.get(i);
      }
      throw new BusinessException(msg);
    }

    return ufRet;
  }

  public boolean isPicked(String cgeneralhid)
    throws SQLException, BusinessException
  {
    beforeCallMethod("nc.bs.ic.ic2a1.PickbillDMO", "isPicked", new Object[] { cgeneralhid });

    if (cgeneralhid == null) {
      return false;
    }
    this.m_timer.start();

    String sql1 = " select sum(isnull(npackagednum,0.0)) from ic_pickbill where coutcorrespondhid=? ";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean isPicked = false;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql1);
      stmt.setString(1, cgeneralhid);
      rs = stmt.executeQuery();
      UFDouble ufd = null;
      if (rs.next()) {
        BigDecimal num = rs.getBigDecimal(1);
        if (num != null) {
          ufd = new UFDouble(num);
          if (ufd.compareTo(new UFDouble(0.0D)) != 0) {
            isPicked = true;
          }
        }
      }

      if (isPicked) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000073"));
      }
      String sql = "SELECT pk_pickbill from ic_pickbill WHERE  coutcorrespondhid=? and cexecutorid is null ";
      stmt = con.prepareStatement(sql);
      stmt.setString(1, cgeneralhid);
      rs = stmt.executeQuery();
      if (rs.next())
        isPicked = true;
    }
    catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000074") + e.getMessage());
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
    afterCallMethod("nc.bs.ic.ic2a1.PickbillDMO", "isPicked", new Object[] { cgeneralhid });

    this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000075"));
    if (isPicked)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000076"));
    return isPicked;
  }

  public UFBoolean isPlanPrice(AggregatedValueObject vo)
    throws BusinessException
  {
    UFBoolean b = new UFBoolean(true);
    if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null)) {
      return b;
    }
    int len = vo.getChildrenVO().length;
    if (len == 0)
      return b;
    for (int i = 0; i < len; i++) {
      if ((vo.getChildrenVO()[i].getAttributeValue("nplannedprice") != null) && (vo.getChildrenVO()[i].getAttributeValue("nplannedprice").toString().trim().length() != 0))
        continue;
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000077"));
    }

    return b;
  }

  private boolean isSourceModified(String billtype, String hid, ArrayList alh)
    throws BusinessException
  {
    if ((billtype == null) || (hid == null) || (alh == null))
      return false;
    boolean ret = false;
    String hts = (String)alh.get(0);
    Hashtable htbts = (Hashtable)alh.get(1);
    String bid = null;
    String bts = null;

    StringBuffer sqlh = new StringBuffer();
    sqlh.append(" select ts from ").append(getHeadTableName(billtype)).append(" where dr=0 and ").append(getHeadColName(billtype)).append(" = '").append(hid).append("'");

    StringBuffer sqlb = new StringBuffer();
    sqlb.append(" select ").append(getBodyColName(billtype)).append(" ,ts from ").append(getBodyTableName(billtype)).append(" where dr=0 and ").append(getHeadColName(billtype)).append(" = '").append(hid).append("'");

    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable htInv = new Hashtable();
    ResultSet rs = null;
    String ts = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sqlh.toString());
      rs = stmt.executeQuery();

      if (rs.next()) {
        ts = rs.getString(1);
        if (!hts.equals(ts))
          ret = true;
      }
      else
      {
        ret = true;
      }
      if (!ret) {
        stmt.close();
        stmt = con.prepareStatement(sqlb.toString());
        rs = stmt.executeQuery();

        while (rs.next()) {
          bid = rs.getString(1);
          ts = rs.getString(2);
          if ((bid == null) || (!htbts.containsKey(bid)))
            continue;
          bts = (String)htbts.get(bid);
          if (!bts.equals(ts)) {
            ret = true;
            break;
          }

          htbts.remove(bid);
        }

      }

    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
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
      catch (Exception e)
      {
      }
    }
    if ((!ret) && (htbts.size() > 0)) {
      ret = true;
    }
    return ret;
  }

  public final boolean isUniKeyEqual(GeneralBillItemVO vo1, GeneralBillItemVO vo2)
  {
    if ((vo1 == null) && (vo2 != null))
      return false;
    if ((vo1 != null) && (vo2 == null))
      return false;
    if ((vo1 == null) && (vo2 == null)) {
      return true;
    }
    boolean isEqual = true;
    StringBuffer key1 = new StringBuffer();
    key1.append(vo1.getCinventoryid());
    for (int i = 1; i <= 10; i++) {
      key1.append(vo1.getAttributeValue("vfree" + i));
    }
    key1.append(vo1.getCastunitid());
    key1.append(vo1.getVbatchcode());

    StringBuffer key2 = new StringBuffer();
    key2.append(vo2.getCinventoryid());
    for (int i = 1; i <= 10; i++) {
      key2.append(vo2.getAttributeValue("vfree" + i));
    }
    key2.append(vo2.getCastunitid());
    key2.append(vo2.getVbatchcode());
    if (!key2.toString().equals(key1.toString()))
      isEqual = false;
    return isEqual;
  }

  private Hashtable queryCalbodyInv(String ccalbodyid, String cwarehouseid, String pk_corp, String subQuery)
    throws BusinessException
  {
    if ((pk_corp == null) || (cwarehouseid == null) || (subQuery == null)) {
      return new Hashtable();
    }

    String sql1 = "select pk_calbody from bd_stordoc where pk_stordoc=?";
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT pk_invmandoc FROM bd_produce  ");

    sql.append(" where pk_calbody=? and isused='Y' ");

    sql.append(" and pk_invmandoc in (" + subQuery + ")");

    boolean isWhInv = false;

    SysInitDMO initdmo = new SysInitDMO();
    String value = initdmo.getParaString(pk_corp, "IC050");
    if ((value != null) && ("仓库".equalsIgnoreCase(value.trim()))) {
      isWhInv = true;
    }
    StringBuffer sql2 = null;
    if (isWhInv) {
      sql2 = new StringBuffer();
      sql2.append("select cinventoryid from ic_numctl where cwarehouseid=? and cinventoryid in (");
      sql2.append(subQuery);
      sql2.append(")");
    }
    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable htInv = new Hashtable();
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql1);
      stmt.setString(1, cwarehouseid);
      rs = stmt.executeQuery();

      if (rs.next()) {
        String pk_calbody = rs.getString(1);
        if ((ccalbodyid != null) && (!ccalbodyid.equals(pk_calbody)))
          throw new SQLException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000078"));
        ccalbodyid = pk_calbody;
      }

      if (stmt != null) {
        stmt.close();
      }

      if (!isWhInv) {
        stmt = con.prepareStatement(sql.toString());
        stmt.setString(1, ccalbodyid);
      } else {
        stmt = con.prepareStatement(sql2.toString());
        stmt.setString(1, cwarehouseid);
      }

      rs = stmt.executeQuery();

      String sInv = null;
      while (rs.next()) {
        sInv = rs.getString(1);
        sInv = sInv == null ? null : sInv.trim();

        if (sInv != null)
          htInv.put(sInv, sInv);
      }
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
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
    return htInv;
  }

  private Hashtable queryCalbodyInv(String cwarehouseid, String pk_corp, ArrayList alInvs)
    throws SQLException
  {
    if ((pk_corp == null) || (cwarehouseid == null) || (alInvs == null) || (alInvs.size() == 0)) {
      return new Hashtable();
    }
    String sql1 = "select pk_calbody from bd_stordoc where pk_stordoc=?";
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT pk_invmandoc FROM bd_produce  ");
    sql.append(" where pk_calbody=? and isused='Y' ");
    sql.append(GeneralSqlString.formInSQL("pk_invmandoc", alInvs));

    StringBuffer sql2 = null;
    String pk_calbody = null;

    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable htInv = new Hashtable();
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql1);
      stmt.setString(1, cwarehouseid);
      rs = stmt.executeQuery();

      if (rs.next()) {
        pk_calbody = rs.getString(1);
      }

      if (stmt != null) {
        stmt.close();
      }
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_calbody);

      rs = stmt.executeQuery();

      String sInv = null;
      while (rs.next()) {
        sInv = rs.getString(1);
        sInv = sInv == null ? null : sInv.trim();
        if (sInv != null)
          htInv.put(sInv, sInv);
      }
    }
    finally {
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
    return htInv;
  }

  private ArrayList queryCspaceLeftVolume(GeneralBillVO vo)
    throws SQLException
  {
    ArrayList alRet = new ArrayList();

    if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null)) {
      return alRet;
    }

    GeneralBillItemVO[] voItems = vo.getItemVOs();

    ArrayList alid = new ArrayList();
    LocatorVO[] voLocs = null;
    for (int i = 0; i < voItems.length; i++) {
      voLocs = voItems[i].getLocator();
      if (voLocs != null) {
        for (int j = 0; j < voLocs.length; j++) {
          if (!alid.contains(voLocs[j].getCspaceid())) {
            alid.add(voLocs[j].getCspaceid());
          }
        }
      }
    }

    if (alid.size() == 0) {
      return alRet;
    }

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    String subQry = null;

    con = getConnection();

    StringBuffer sql = new StringBuffer();

    sql = new StringBuffer("select cscode ,leftvolume from (").append(" select cspaceid,cscode,isnull(num,0)*isnull(storeunitnum, 0.0) -isnull(volume,0) as leftvolume ").append(" from (select cspaceid,cinventoryid,sum(isnull(ninspacenum,0)-isnull(noutspacenum,0)) as num from v_ic_onhandnum6 ").append(" where  cspaceid in ").append(" (").append("select cspaceid from ic_general_bb1 where cgeneralbid in (select cgeneralbid from ic_general_b where cgeneralhid=? )").append(") ").append(" group by cspaceid,cinventoryid ").append(" ) xcl inner join  bd_invmandoc on xcl.cinventoryid=bd_invmandoc.pk_invmandoc ").append(" inner join bd_invbasdoc ON  bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc ").append(" inner join bd_cargdoc on xcl.cspaceid=bd_cargdoc.pk_cargdoc ").append(" ) tmp where leftvolume >0");

    String cscode = null;

    BigDecimal num = null;
    try
    {
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, vo.getHeaderVO().getCgeneralhid());
      rs = stmt.executeQuery();

      ArrayList alrow = null;
      while (rs.next()) {
        alrow = new ArrayList();
        cscode = rs.getString(1);
        num = rs.getBigDecimal(2);
        if ((cscode != null) && (num != null)) {
          alrow.add(cscode);
          alrow.add(new UFDouble(num));
          alRet.add(alrow);
        }
      }
    }
    finally
    {
      try
      {
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
    return alRet;
  }

  private Hashtable queryCtrlValue(GeneralBillVO voBill, boolean isCalbody)
    throws SQLException
  {
    Hashtable ht = new Hashtable();

    if ((voBill == null) || (voBill.getParentVO() == null) || (voBill.getHeaderVO().getCgeneralhid() == null))
    {
      return ht;
    }String cgeneralhid = voBill.getHeaderVO().getCgeneralhid();
    String ccalbodyid = voBill.getHeaderVO().getPk_calbody();
    String cwarehouseid = voBill.getHeaderVO().getCwarehouseid();

    StringBuffer sql = new StringBuffer();

    if (!isCalbody) {
      sql = new StringBuffer("select cinventoryid,nmaxstocknum,nminstocknum,nsafestocknum,norderpointnum from ic_numctl where  cwarehouseid='").append(cwarehouseid).append("' and cinventoryid in (select cinventoryid from ic_general_b where cgeneralhid=? )");
    }
    else
    {
      sql = new StringBuffer("select bd_produce.pk_invmandoc,maxstornum,lowstocknum,safetystocknum ,zdhd from bd_produce  where pk_calbody='");

      sql.append(ccalbodyid).append("' and pk_invmandoc in (select cinventoryid from ic_general_b where cgeneralhid=? )");
    }

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, cgeneralhid);
      rs = stmt.executeQuery();

      String sInv = null;
      BigDecimal nmaxstocknum = null;
      BigDecimal nminstocknum = null;
      BigDecimal nsafestocknum = null;
      BigDecimal norderpointnum = null;
      ArrayList alset = null;
      while (rs.next()) {
        alset = new ArrayList();
        sInv = rs.getString(1).trim();

        nmaxstocknum = rs.getBigDecimal(2);
        if (nmaxstocknum != null)
          alset.add(new UFDouble(nmaxstocknum));
        else {
          alset.add(null);
        }
        nminstocknum = rs.getBigDecimal(3);
        if (nminstocknum != null)
          alset.add(new UFDouble(nminstocknum));
        else
          alset.add(null);
        nsafestocknum = rs.getBigDecimal(4);
        if (nsafestocknum != null)
          alset.add(new UFDouble(nsafestocknum));
        else
          alset.add(null);
        norderpointnum = rs.getBigDecimal(5);
        if (norderpointnum != null)
          alset.add(new UFDouble(norderpointnum));
        else
          alset.add(null);
        if (sInv != null)
          ht.put(sInv, alset);
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    return ht;
  }

  private Hashtable queryFixSpace(String cwarehouseid)
    throws SQLException
  {
    String sInv = null;
    String sInvClass = null;
    String cspace = null;
    StringBuffer sqldefine = new StringBuffer();
    sqldefine.append(" select cinventoryid,invclasscode,cspaceid from ic_storectl ").append(" left outer join ic_defaultspace on ic_storectl.cstorectlid=ic_defaultspace.cstorectlid ").append(" left outer join bd_invcl on cinventoryclassid=bd_invcl.pk_invcl ").append(" where ffixedspace='Y' and cwarehouseid=? ").append(" order by cinventoryid desc,len(invclasscode) desc ");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer sErr = null;

    Hashtable htSpace = null;
    Hashtable htInv = null;
    String precode = "";
    String cspaceid = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sqldefine.toString());
      stmt.setString(1, cwarehouseid);

      rs = stmt.executeQuery();

      while (rs.next()) {
        if (htInv == null) {
          htInv = new Hashtable();
        }

        sInv = rs.getString(1);
        sInvClass = rs.getString(2);
        cspaceid = rs.getString(3);

        if ((sInv != null) && (sInv.trim().length() > 0)) {
          if (htInv.containsKey(sInv)) {
            htSpace = (Hashtable)htInv.get(sInv);
          }
          else {
            htSpace = new Hashtable();
            htInv.put(sInv, htSpace);
          }
          if (!htSpace.containsKey(cspaceid)) {
            htSpace.put(cspaceid, cspaceid); continue;
          }

        }

        if (sInvClass == null)
          continue;
        if (!precode.startsWith(sInvClass)) {
          htSpace = new Hashtable();
          htInv.put(sInvClass, htSpace);
          precode = sInvClass;
        }
        if (htInv.containsKey(sInvClass)) {
          htSpace = (Hashtable)htInv.get(sInvClass);
          htSpace.put(cspaceid, cspaceid);
        }

      }

    }
    finally
    {
      try
      {
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
    return htInv;
  }

  private Hashtable queryOnhandNum(GeneralBillVO voBill, boolean isGroupbyCalBody)
    throws SQLException
  {
    Hashtable ht = new Hashtable();

    if ((voBill == null) || (voBill.getParentVO() == null) || (voBill.getHeaderVO().getCgeneralhid() == null))
    {
      return ht;
    }String cgeneralhid = voBill.getHeaderVO().getCgeneralhid();
    String ccalbodyid = voBill.getHeaderVO().getPk_calbody();
    String cwarehouseid = voBill.getHeaderVO().getCwarehouseid();

    StringBuffer sql = new StringBuffer();

    sql = new StringBuffer("select cinventoryid, sum(ISNULL(ninnum,0.0)-ISNULL(noutnum,0.0)) from v_ic_onhandnum4 ").append(" where ");

    if (isGroupbyCalBody)
      sql.append(" ccalbodyid=? ");
    else {
      sql.append(" cwarehouseid=? ");
    }
    sql.append(" and cinventoryid in ").append(" (select cinventoryid from ic_general_b where cgeneralhid=?  ) ").append(" Group by cinventoryid");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      if (isGroupbyCalBody)
        stmt.setString(1, ccalbodyid);
      else
        stmt.setString(1, cwarehouseid);
      stmt.setString(2, cgeneralhid);

      rs = stmt.executeQuery();
      String sInv = null;
      BigDecimal xcl = null;
      while (rs.next())
      {
        sInv = rs.getString(1).trim();
        xcl = rs.getBigDecimal(2);
        if (xcl != null)
          ht.put(sInv, new UFDouble(xcl));
      }
    }
    finally {
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
    return ht;
  }

  private Hashtable queryOnhandNum_CS(GeneralBillVO vo)
    throws BusinessException
  {
    Hashtable ht = new Hashtable();

    if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null)) {
      return ht;
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sTableName = null;
    try
    {
      GeneralBillItemVO[] voItems = vo.getItemVOs();

      ArrayList alValue = new ArrayList();
      ArrayList alrow = null;
      HashMap hmkey = new HashMap();
      String key1 = null;
      for (int i = 0; i < voItems.length; i++)
      {
        if (voItems[i].getInv().getNegallowed() == null) {
          throw new BusinessException("inv negallowed==null");
        }
        if (voItems[i].getInv().getNegallowed().booleanValue())
        {
          continue;
        }

        LocatorVO[] voLocs = voItems[i].getLocator();
        if (voLocs == null) {
          continue;
        }
        for (int j = 0; j < voLocs.length; j++) {
          if (nc.vo.ic.pub.GenMethod.isNull(voLocs[j].getCspaceid()))
            continue;
          key1 = voItems[i].getCinventoryid() + voItems[i].getCastunitid() + voItems[i].getVbatchcode() + voLocs[j].getCspaceid();

          if (hmkey.containsKey(key1))
            continue;
          hmkey.put(key1, key1);

          alrow = new ArrayList();
          alValue.add(alrow);
          alrow.add(voItems[i].getCinventoryid());
          alrow.add(voItems[i].getCastunitid());
          alrow.add(voItems[i].getVbatchcode());
          alrow.add(voLocs[j].getCspaceid());
        }

      }

      if (alValue.size() == 0) {
        Hashtable i = ht;
        return i;
      }
      con = getConnection();

      TempTableDMO tmpTable = new TempTableDMO();
      sTableName = tmpTable.getTempStringTable(con, "temp_ic_check_dbl_2", new String[] { "cinvid", "castid", "vbatch", "clocid" }, new String[] { "char(20)", "char(20)", "varchar(30)", "char(20)" }, null, alValue);

      if (sTableName == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000080"));
      }
      StringBuffer sql = new StringBuffer();

      String cwarehouseid = vo.getHeaderVO().getCwarehouseid();

      sql = new StringBuffer(" select cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cspaceid,cvendorid,hsl, num, assistnum,grossnum from (").append(" SELECT cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cspaceid,cvendorid,hsl,SUM(ISNULL(ninspacenum,0.0)-ISNULL(noutspacenum,0.0)) as num,SUM(ISNULL(ninspaceassistnum,0.0)-ISNULL(noutspaceassistnum,0.0)) as assistnum,sum(isnull(ninspacegrossnum,0.0)-isnull(noutspacegrossnum,0.0)) as grossnum ").append(" from v_ic_onhandnum2 onhand, ").append(sTableName).append(" tmp where  cwarehouseid=? and onhand.cinventoryid=tmp.cinvid and  onhand.cspaceid=tmp.clocid and isnull(onhand.castunitid,'1')=isnull(tmp.castid,'1') and isnull(vbatchcode,'1')=isnull(tmp.vbatch,'1') ").append(" group by  cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cspaceid,cvendorid,hsl ").append(" ) a where (num<0 or grossnum<0)");

      StringBuffer sql1 = new StringBuffer(" select cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cspaceid,cvendorid,hsl , num, assistnum,grossnum from (").append(" SELECT cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cspaceid,cvendorid,hsl,SUM(ISNULL(ninspacenum,0.0)-ISNULL(noutspacenum,0.0)) as num,SUM(ISNULL(ninspaceassistnum,0.0)-ISNULL(noutspaceassistnum,0.0)) as assistnum,sum(isnull(ninspacegrossnum,0.0)-isnull(noutspacegrossnum,0.0)) as grossnum ").append(" from v_ic_onhandnum2 onhand, ").append(sTableName).append(" tmp where  cwarehouseid=? and onhand.cinventoryid=tmp.cinvid and  onhand.cspaceid=tmp.clocid and isnull(onhand.castunitid,'1')=isnull(tmp.castid,'1') and isnull(vbatchcode,'1')=isnull(tmp.vbatch,'1') and castunitid is not null ").append(" group by  cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cspaceid,cvendorid,hsl  ").append(" ) a where ( num*assistnum<0 or (num=0 and assistnum<0) or num*grossnum<0 ) ");

      StringBuffer key = null;
      String invid = null;
      String vfree1 = null;
      String vfree2 = null;
      String vfree3 = null;
      String vfree4 = null;
      String vfree5 = null;
      String vfree6 = null;
      String vfree7 = null;
      String vfree8 = null;
      String vfree9 = null;
      String vfree10 = null;
      String vbatchcode = null;
      String castunitid = null;
      String cspaceid = null;
      String cvendorid = null;
      BigDecimal hsl = null;
      BigDecimal num = null;
      BigDecimal assistnum = null;
      BigDecimal grossnum = null;
      UFDouble ufnum = null;
      UFDouble ufassistnum = null;
      UFDouble ufgrossnum = null;
      GeneralBillItemVO voItem = null;
      UFDouble ZERO = new UFDouble(0.0D);

      String issupplierstock = null;
      String isstorebyconvert = null;

      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, cwarehouseid);

      rs = stmt.executeQuery();

      while (rs.next()) {
        invid = rs.getString("cinventoryid");
        vfree1 = rs.getString("vfree1");
        vfree2 = rs.getString("vfree2");
        vfree3 = rs.getString("vfree3");
        vfree4 = rs.getString("vfree4");
        vfree5 = rs.getString("vfree5");
        vfree6 = rs.getString("vfree6");
        vfree7 = rs.getString("vfree7");
        vfree8 = rs.getString("vfree8");
        vfree9 = rs.getString("vfree9");
        vfree10 = rs.getString("vfree10");
        vbatchcode = rs.getString("vbatchcode");
        castunitid = rs.getString("castunitid");
        cspaceid = rs.getString("cspaceid");
        cvendorid = rs.getString("cvendorid");
        hsl = rs.getBigDecimal("hsl");

        num = rs.getBigDecimal("num");
        assistnum = rs.getBigDecimal("assistnum");
        grossnum = rs.getBigDecimal("grossnum");
        ufnum = num == null ? ZERO : new UFDouble(num);
        ufassistnum = assistnum == null ? ZERO : new UFDouble(assistnum);
        ufgrossnum = grossnum == null ? ZERO : new UFDouble(grossnum);

        if ((castunitid != null) && (ufassistnum.compareTo(ZERO) == 0) && (ufnum.compareTo(ZERO) >= 0) && (ufgrossnum.compareTo(ZERO) >= 0))
        {
          continue;
        }
        key = new StringBuffer();
        key.append(invid);
        key.append(vfree1);
        key.append(vfree2);
        key.append(vfree3);
        key.append(vfree4);
        key.append(vfree5);
        key.append(vfree6);
        key.append(vfree7);
        key.append(vfree8);
        key.append(vfree9);
        key.append(vfree10);
        key.append(vbatchcode);
        key.append(castunitid);
        key.append(cspaceid);

        key.append(cvendorid);

        if (hsl != null) {
          UFDouble hsl1 = new UFDouble(hsl);
          hsl1.setTrimZero(true);
          key.append(hsl1.toString());
        } else {
          key.append(hsl);
        }
        ht.put(key.toString(), new UFDouble[] { ufnum, ufassistnum, ufgrossnum });
      }
      if (stmt != null)
        stmt.close();
      stmt = con.prepareStatement(sql1.toString());
      stmt.setString(1, cwarehouseid);

      rs = stmt.executeQuery();

      while (rs.next()) {
        invid = rs.getString("cinventoryid");
        vfree1 = rs.getString("vfree1");
        vfree2 = rs.getString("vfree2");
        vfree3 = rs.getString("vfree3");
        vfree4 = rs.getString("vfree4");
        vfree5 = rs.getString("vfree5");
        vfree6 = rs.getString("vfree6");
        vfree7 = rs.getString("vfree7");
        vfree8 = rs.getString("vfree8");
        vfree9 = rs.getString("vfree9");
        vfree10 = rs.getString("vfree10");
        vbatchcode = rs.getString("vbatchcode");
        castunitid = rs.getString("castunitid");
        cspaceid = rs.getString("cspaceid");
        cvendorid = rs.getString("cvendorid");
        hsl = rs.getBigDecimal("hsl");

        num = rs.getBigDecimal("num");
        assistnum = rs.getBigDecimal("assistnum");
        grossnum = rs.getBigDecimal("grossnum");
        ufnum = num == null ? ZERO : new UFDouble(num);
        ufassistnum = assistnum == null ? ZERO : new UFDouble(assistnum);
        ufgrossnum = grossnum == null ? ZERO : new UFDouble(grossnum);

        key = new StringBuffer();
        key.append(invid);
        key.append(vfree1);
        key.append(vfree2);
        key.append(vfree3);
        key.append(vfree4);
        key.append(vfree5);
        key.append(vfree6);
        key.append(vfree7);
        key.append(vfree8);
        key.append(vfree9);
        key.append(vfree10);
        key.append(vbatchcode);
        key.append(castunitid);
        key.append(cspaceid);

        key.append(cvendorid);

        if (hsl != null) {
          UFDouble hsl1 = new UFDouble(hsl);
          hsl1.setTrimZero(true);
          key.append(hsl1.toString());
        } else {
          key.append(hsl);
        }
        ht.put(key.toString(), new UFDouble[] { ufnum, ufassistnum, ufgrossnum });
      }
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    finally
    {
      try
      {
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
    return ht;
  }

  private Hashtable queryOnhandNum_WH(GeneralBillVO vo)
    throws BusinessException
  {
    Hashtable ht = new Hashtable();

    if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null)) {
      return ht;
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sTableName = null;
    try
    {
      GeneralBillItemVO[] voItems = vo.getItemVOs();

      ArrayList alValue = new ArrayList();
      ArrayList alrow = null;
      HashMap hmkey = new HashMap();
      String key1 = null;
      for (int i = 0; i < voItems.length; i++)
      {
        if (voItems[i].getInv().getNegallowed() == null) {
          throw new BusinessException("inv negallowed==null");
        }
        if (voItems[i].getInv().getNegallowed().booleanValue())
          continue;
        key1 = voItems[i].getCinventoryid() + voItems[i].getCastunitid() + voItems[i].getVbatchcode();

        if (hmkey.containsKey(key1))
          continue;
        hmkey.put(key1, key1);

        alrow = new ArrayList();
        alValue.add(alrow);
        alrow.add(voItems[i].getCinventoryid());
        alrow.add(voItems[i].getCastunitid());
        alrow.add(voItems[i].getVbatchcode());
      }

      if (alValue.size() == 0) {
        Hashtable i = ht;
        return i;
      }
      con = getConnection();

      TempTableDMO tmpTable = new TempTableDMO();
      sTableName = tmpTable.getTempStringTable(con, "temp_ic_check_dbl_1", new String[] { "cinvid", "castid", "vbatch" }, new String[] { "char(20)", "char(20)", "varchar(30)" }, null, alValue);

      if (sTableName == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000080"));
      }
      StringBuffer sql = new StringBuffer();

      String cwarehouseid = vo.getHeaderVO().getCwarehouseid();

      sql = new StringBuffer(" select cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cvendorid,hsl, num, assistnum,grossnum from (").append(" SELECT cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cvendorid,hsl,SUM(ISNULL(ninnum,0.0)-ISNULL(noutnum,0.0)) as num,SUM(ISNULL(ninassistnum,0.0)-ISNULL(noutassistnum,0.0)) as assistnum,sum(isnull(ningrossnum,0.0)-isnull(noutgrossnum,0.0)) as grossnum ").append(" from v_ic_onhandnum1 onhand, ").append(sTableName).append(" tmp where  cwarehouseid=? and onhand.cinventoryid=tmp.cinvid and isnull(onhand.castunitid,'1')=isnull(tmp.castid,'1') and isnull(vbatchcode,'1')=isnull(tmp.vbatch,'1') ").append(" group by  cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cvendorid,hsl ").append(" ) a where (num<0 or grossnum<0)");

      StringBuffer sql1 = new StringBuffer(" select cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cvendorid,hsl, num, assistnum,grossnum from (").append(" SELECT cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cvendorid,hsl,SUM(ISNULL(ninnum,0.0)-ISNULL(noutnum,0.0)) as num,SUM(ISNULL(ninassistnum,0.0)-ISNULL(noutassistnum,0.0)) as assistnum ,sum(isnull(ningrossnum,0.0)-isnull(noutgrossnum,0.0)) as grossnum").append(" from v_ic_onhandnum1 onhand, ").append(sTableName).append(" tmp where  cwarehouseid=? and onhand.cinventoryid=tmp.cinvid and isnull(onhand.castunitid,'1')=isnull(tmp.castid,'1') and isnull(vbatchcode,'1')=isnull(tmp.vbatch,'1') ").append("  and castunitid is not null ").append(" group by  cinventoryid,vfree1,vfree2,vfree3,vfree4,vfree5,vfree6,vfree7,vfree8,vfree9,vfree10,vbatchcode,castunitid,cvendorid,hsl ").append(" ) a where ( num*assistnum<0  or (num=0 and assistnum<0) or num*grossnum<0  ) ");

      StringBuffer key = null;
      String invid = null;
      String vfree1 = null;
      String vfree2 = null;
      String vfree3 = null;
      String vfree4 = null;
      String vfree5 = null;
      String vfree6 = null;
      String vfree7 = null;
      String vfree8 = null;
      String vfree9 = null;
      String vfree10 = null;
      String vbatchcode = null;
      String castunitid = null;
      String cvendorid = null;
      BigDecimal hsl = null;
      BigDecimal num = null;
      BigDecimal assistnum = null;
      BigDecimal grossnum = null;
      UFDouble ufnum = null;
      UFDouble ufassistnum = null;
      UFDouble ufgrossnum = null;
      GeneralBillItemVO voItem = null;
      UFDouble ZERO = new UFDouble(0.0D);

      String issupplierstock = null;
      String isstorebyconvert = null;

      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, cwarehouseid);

      rs = stmt.executeQuery();

      while (rs.next()) {
        invid = rs.getString("cinventoryid");
        vfree1 = rs.getString("vfree1");
        vfree2 = rs.getString("vfree2");
        vfree3 = rs.getString("vfree3");
        vfree4 = rs.getString("vfree4");
        vfree5 = rs.getString("vfree5");
        vfree6 = rs.getString("vfree6");
        vfree7 = rs.getString("vfree7");
        vfree8 = rs.getString("vfree8");
        vfree9 = rs.getString("vfree9");
        vfree10 = rs.getString("vfree10");
        vbatchcode = rs.getString("vbatchcode");
        castunitid = rs.getString("castunitid");
        cvendorid = rs.getString("cvendorid");
        hsl = rs.getBigDecimal("hsl");

        num = rs.getBigDecimal("num");
        assistnum = rs.getBigDecimal("assistnum");
        grossnum = rs.getBigDecimal("grossnum");
        ufnum = num == null ? ZERO : new UFDouble(num);
        ufassistnum = assistnum == null ? ZERO : new UFDouble(assistnum);
        ufgrossnum = grossnum == null ? ZERO : new UFDouble(grossnum);

        if ((castunitid != null) && (ufassistnum.compareTo(ZERO) == 0) && (ufnum.compareTo(ZERO) >= 0) && (ufgrossnum.compareTo(ZERO) >= 0)) {
          continue;
        }
        key = new StringBuffer();
        key.append(invid);
        key.append(vfree1);
        key.append(vfree2);
        key.append(vfree3);
        key.append(vfree4);
        key.append(vfree5);
        key.append(vfree6);
        key.append(vfree7);
        key.append(vfree8);
        key.append(vfree9);
        key.append(vfree10);
        key.append(vbatchcode);
        key.append(castunitid);

        key.append(cvendorid);

        if (hsl != null) {
          UFDouble hsl1 = new UFDouble(hsl);
          hsl1.setTrimZero(true);
          key.append(hsl1.toString());
        } else {
          key.append(hsl);
        }

        ht.put(key.toString(), new UFDouble[] { ufnum, ufassistnum, ufgrossnum });
      }
      if (stmt != null) {
        stmt.close();
      }
      stmt = con.prepareStatement(sql1.toString());
      stmt.setString(1, cwarehouseid);

      rs = stmt.executeQuery();

      while (rs.next()) {
        invid = rs.getString("cinventoryid");
        vfree1 = rs.getString("vfree1");
        vfree2 = rs.getString("vfree2");
        vfree3 = rs.getString("vfree3");
        vfree4 = rs.getString("vfree4");
        vfree5 = rs.getString("vfree5");
        vfree6 = rs.getString("vfree6");
        vfree7 = rs.getString("vfree7");
        vfree8 = rs.getString("vfree8");
        vfree9 = rs.getString("vfree9");
        vfree10 = rs.getString("vfree10");
        vbatchcode = rs.getString("vbatchcode");
        castunitid = rs.getString("castunitid");
        cvendorid = rs.getString("cvendorid");
        hsl = rs.getBigDecimal("hsl");

        num = rs.getBigDecimal("num");
        assistnum = rs.getBigDecimal("assistnum");
        grossnum = rs.getBigDecimal("grossnum");
        ufnum = num == null ? ZERO : new UFDouble(num);
        ufassistnum = assistnum == null ? ZERO : new UFDouble(assistnum);
        ufgrossnum = grossnum == null ? ZERO : new UFDouble(grossnum);

        key = new StringBuffer();
        key.append(invid);
        key.append(vfree1);
        key.append(vfree2);
        key.append(vfree3);
        key.append(vfree4);
        key.append(vfree5);
        key.append(vfree6);
        key.append(vfree7);
        key.append(vfree8);
        key.append(vfree9);
        key.append(vfree10);
        key.append(vbatchcode);
        key.append(castunitid);

        key.append(cvendorid);

        if (hsl != null) {
          UFDouble hsl1 = new UFDouble(hsl);
          hsl1.setTrimZero(true);
          key.append(hsl1.toString());
        } else {
          key.append(hsl);
        }
        ht.put(key.toString(), new UFDouble[] { ufnum, ufassistnum, ufgrossnum });
      }
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
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
    SCMEnv.out("checkDBL HashTemp keys=" + ht.size() + " ; and vobill rowcount=" + vo.getItemVOs().length);
    return ht;
  }

  private Hashtable queryPlaceAlone(String cwarehouseid)
    throws SQLException
  {
    String sInv = null;
    String sInvClass = null;
    String cspace = null;
    StringBuffer sqldefine = new StringBuffer();
    sqldefine.append(" select cinventoryid,invclasscode from ic_storectl ").append("  left outer join ic_defaultspace on ic_storectl.cstorectlid=ic_defaultspace.cstorectlid ").append("  left outer join bd_invcl on cinventoryclassid=bd_invcl.pk_invcl ").append(" where fseparatespace='Y' and cwarehouseid=? order by cinventoryid desc,len(invclasscode) desc ");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer sErr = null;

    Hashtable htInv = null;
    String precode = "";
    String cspaceid = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sqldefine.toString());
      stmt.setString(1, cwarehouseid);

      rs = stmt.executeQuery();

      while (rs.next()) {
        if (htInv == null) {
          htInv = new Hashtable();
        }

        sInv = rs.getString(1);
        sInvClass = rs.getString(2);

        if ((sInv != null) && (sInv.trim().length() > 0)) {
          if (!htInv.containsKey(sInv)) {
            htInv.put(sInv, sInv);
            continue;
          }

        }

        if ((sInvClass == null) || 
          (precode.startsWith(sInvClass))) continue;
        htInv.put(sInvClass, sInvClass);
        precode = sInvClass;
      }

    }
    finally
    {
      try
      {
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
    return htInv;
  }

  private Hashtable queryWarehouseInv(String cwarehouseid, String pk_corp, String[] sInvs)
    throws SQLException
  {
    if ((pk_corp == null) || (cwarehouseid == null) || (sInvs == null)) {
      return new Hashtable();
    }

    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT cinventoryid,bd_invbasdoc.invcode FROM ic_numctl LEFT OUTER JOIN  bd_invmandoc ON  ic_numctl.cinventoryid = bd_invmandoc.pk_invmandoc LEFT OUTER JOIN  bd_invbasdoc ON  bd_invmandoc.pk_invbasdoc =bd_invbasdoc.pk_invbasdoc ");

    sql.append(" where ic_numctl.pk_corp=? ");
    sql.append(" and ic_numctl.cwarehouseid=? ");

    sql.append(GeneralSqlString.formInSQL("ic_numctl.cinventoryid", sInvs));

    Connection con = null;
    PreparedStatement stmt = null;
    Hashtable htInv = new Hashtable();
    ResultSet rs = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());
      stmt.setString(1, pk_corp);
      stmt.setString(2, cwarehouseid);
      rs = stmt.executeQuery();

      String sInv = null;
      while (rs.next()) {
        sInv = rs.getString(1);
        sInv = sInv == null ? null : sInv.trim();
        String sCode = rs.getString(2);
        sCode = sCode == null ? null : sCode.trim();
        if ((sInv != null) && (sCode != null))
          htInv.put(sInv, sCode);
      }
    }
    finally {
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
    return htInv;
  }

  public void returnEditBillCode(IBillCodeExt voBill)
    throws BusinessException
  {
    if (voBill == null)
      return;
  }

  public void returnBillCode(IBillCode voBill)
    throws BusinessException
  {
    if (voBill == null)
      return;
  }

  public void returnBillCodeWhenDelete(IBillCode voBill)
    throws BusinessException
  {
    if (voBill == null) {
      return;
    }
    this.m_timer.start();
    try
    {
      IBillcodeRuleService s = (IBillcodeRuleService)NCLocator.getInstance().lookup(IBillcodeRuleService.class.getName());

      s.returnBillCodeOnDelete(voBill.getPk_corp(), voBill.getBillTypeCode(), voBill.getVBillCode(), voBill.getBillCodeObjVO());
      this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000081"));
      return;
    }
    catch (Exception e) {
      SCMEnv.error(e);
    throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000082") + e.getMessage());
    }
  }

  public String setBillCodeSuppEditCode(IBillCodeExt voBill)
    throws BusinessException
  {
    if (voBill == null) {
      return null;
    }
    this.m_timer.start();
    String sBillCode = voBill.getVBillCode();

    String soldBillCode = voBill.getVoldBillCode();
    try
    {
      if ((sBillCode == null) || (sBillCode.trim().length() == 0) || (sBillCode.equals(voBill.getPk_corp()))) {
        sBillCode = null;
      }

      if (sBillCode != null) {
        sBillCode = sBillCode.trim();
      }

      if ((sBillCode != null) && (sBillCode.equals(soldBillCode))) {
        return null;
      }

      sBillCode = getBillCode(voBill.getBillTypeCode(), voBill.getPk_corp(), sBillCode, voBill.getBillCodeObjVO());

      voBill.setVBillCode(sBillCode);

      this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000083"));

      return sBillCode;
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
    
    throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000084") + e.getMessage());
    }
  }

  public String setBillCode(IBillCode voBill)
    throws BusinessException
  {
    if (voBill == null) {
      return null;
    }
    this.m_timer.start();
    String sBillCode = voBill.getVBillCode();
    try
    {
      if ((sBillCode == null) || (sBillCode.trim().length() == 0) || (sBillCode.equals(voBill.getPk_corp()))) {
        sBillCode = null;
      }

      if (voBill.getStatus() == 2) {
        sBillCode = getBillCode(voBill.getBillTypeCode(), voBill.getPk_corp(), sBillCode, voBill.getBillCodeObjVO());

        voBill.setVBillCode(sBillCode);

        this.m_timer.stopAndShow(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000083"));
        return sBillCode;
      }
      return null;
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
    
    throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008check", "UPP4008check-000084") + e.getMessage());}
  }

  public void checkDef(AggregatedValueObject billvo)
    throws BusinessException
  {
    if (billvo == null) {
      return;
    }
    if (billvo.getParentVO().getStatus() == 3) {
      return;
    }
    Vector v = new Vector();
    for (int i = 0; i < billvo.getChildrenVO().length; i++) {
      if (billvo.getChildrenVO()[i].getStatus() == 3)
        continue;
      v.add(billvo.getChildrenVO()[i]);
    }
    try
    {
      IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
      if ((billvo instanceof GeneralBillVO)) {
        GeneralBillVO vo = new GeneralBillVO();
        vo.setParentVO(billvo.getParentVO());
        if ((v != null) && (v.size() > 0)) {
          GeneralBillItemVO[] items = new GeneralBillItemVO[v.size()];
          v.copyInto(items);
          vo.setChildrenVO(items);
        }
        srv.checkDefDataType(vo);
      } else if ((billvo instanceof SpecialBillVO)) {
        SpecialBillVO vo = new SpecialBillVO();
        vo.setParentVO(billvo.getParentVO());
        if ((v != null) && (v.size() > 0)) {
          SpecialBillItemVO[] items = new SpecialBillItemVO[v.size()];
          v.copyInto(items);
          vo.setChildrenVO(items);
        }
        srv.checkDefDataType(vo);
      } else if ((billvo instanceof WastageBillVO)) {
        WastageBillVO vo = new WastageBillVO();
        vo.setParentVO(billvo.getParentVO());
        if ((v != null) && (v.size() > 0)) {
          WastageBillBVO[] items = new WastageBillBVO[v.size()];
          v.copyInto(items);
          vo.setChildrenVO(items);
        }
        srv.checkDefDataType(vo);
      }
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }
}