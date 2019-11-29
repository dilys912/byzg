package nc.bs.ic.pub;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import javax.naming.NamingException;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.mm.pub.pub1030.PickmDMO;
import nc.bs.ic.pub.bill.GeneralBillDMO;
import nc.bs.ic.pub.bill.GeneralSqlString;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.scm.inter.MMHelper;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.sm.createcorp.CreatecorpDMO;
import nc.itf.dm.service.IDMToIC;
import nc.itf.mm.scm.IMmToIc;
import nc.itf.mr.service.IMRWriteDownBillInfo;
import nc.itf.pd.scm.IPdToIc;
import nc.itf.pu.inter.IPuToIc_EstimateImpl;
import nc.itf.pu.inter.IPuToIc_ToIC;
import nc.itf.scm.so.back.ISOApply;
import nc.itf.scm.so.back.ISOTake;
import nc.itf.scm.to.service.IOuter;
import nc.itf.so.service.ISOToIC_DRP;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.ic.pub.DesassemblyVO;
import nc.vo.ic.pub.ICGenVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.check.GeneralMethodGetErrorMsg;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.merge.DefaultVOMerger;
import nc.vo.scm.pu.ToICParaVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.rewrite.ParaRewriteVO;
import nc.vo.so.credit.BillCreditOriginVO;

public class RewriteDMO extends DataManageObject
{
  Timer m_timer = new Timer();

  public RewriteDMO()
    throws NamingException, SystemException
  {
  }

  public RewriteDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public void check4149CancelForPur(GeneralBillVO voBill)
    throws BusinessException, SQLException
  {
    if ((voBill == null) || (voBill.getParentVO() == null)) {
      SCMEnv.out("no bill para when rewrite.");
      return;
    }

    this.m_timer.start();

    GeneralBillHeaderVO voHeader = (GeneralBillHeaderVO)voBill.getParentVO();

    String sBillPK = null; String sBillTypeCode = null; String sCorpID = null;

    sBillPK = voHeader.getPrimaryKey();
    sBillTypeCode = voHeader.getCbilltypecode();
    sCorpID = voHeader.getPk_corp();

    if ((sBillPK == null) || (sBillPK.trim().length() == 0) || (sBillTypeCode == null) || (sBillTypeCode.trim().length() == 0) || (sCorpID == null) || (sCorpID.trim().length() == 0))
    {
      return;
    }

    if (!isModuleStarted(sCorpID, "PO"))
      return;
    try
    {
      Object obj = null;
      try
      {
        obj = NCLocator.getInstance().lookup(IPuToIc_ToIC.class.getName()); } catch (Exception e) {
        throw new RemoteException(e.getMessage());
      }

      if (obj != null)
        ((IPuToIc_ToIC)obj).isExistBill(sBillTypeCode, sBillPK);
    }
    catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }

    this.m_timer.stopAndShow("chk4149");
  }

  public void check424HCancelForSale(GeneralBillVO voBill)
    throws BusinessException, SQLException
  {
    if ((voBill == null) || (voBill.getParentVO() == null)) {
      SCMEnv.out("no bill para when rewrite.");
      return;
    }

    this.m_timer.start();

    GeneralBillHeaderVO voHeader = (GeneralBillHeaderVO)voBill.getParentVO();

    String sBillPK = null; String sBillTypeCode = null; String sCorpID = null;

    sBillPK = voHeader.getPrimaryKey();
    sBillTypeCode = voHeader.getCbilltypecode();
    sCorpID = voHeader.getPk_corp();

    if ((sBillPK == null) || (sBillPK.trim().length() == 0) || (sBillTypeCode == null) || (sBillTypeCode.trim().length() == 0) || (sCorpID == null) || (sCorpID.trim().length() == 0))
    {
      return;
    }

    if (!isModuleStarted(sCorpID, "SO")) {
      return;
    }
    String sql = "select csourcebillbodyid from so_saleorder_b where frowstatus != 5 AND csourcebillbodyid IS NOT NULL AND csourcebillbodyid in (select cgeneralbid from ic_general_b where cgeneralhid= ? )";

    boolean bHaveRelateBill = isHaveRecord(sql, sBillPK);
    if (bHaveRelateBill) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000138"));
    }
    this.m_timer.stopAndShow("chk424H");
  }

  public void check45CancelForPur(GeneralBillVO bill)
    throws BusinessException, SQLException
  {
    if ((bill == null) || (bill.getItemVOs() == null) || (bill.getItemVOs().length == 0))
    {
      SCMEnv.out("no bill para when rewrite.");
      return;
    }

    this.m_timer.start();

    GeneralBillItemVO[] body = (GeneralBillItemVO[])bill.getItemVOs();
    GeneralBillHeaderVO head = (GeneralBillHeaderVO)bill.getParentVO();

    if (!isModuleStarted(head.getPk_corp(), "PO")) {
      return;
    }
    try
    {
      Object obj = null;
      try
      {
        obj = NCLocator.getInstance().lookup(IPuToIc_ToIC.class.getName());
      } catch (Exception e) {
        throw new RemoteException(e.getMessage());
      }
      UFBoolean[] bvalue = null;
      if (obj != null) {
        String[] cgeneralhid = { head.getCgeneralhid() };
        bvalue = ((IPuToIc_ToIC)obj).isPurInBatch(cgeneralhid);
      }

      if ((bvalue != null) && (bvalue.length > 0)) {
        int size = bvalue.length;
        for (int i = 0; i < size; i++)
          if (bvalue[i].booleanValue())
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000424"));
      }
    }
    catch (BusinessException ef)
    {
      throw ef;
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }

    this.m_timer.stopAndShow("chk45");
  }

  public void check4CCancelForSale(GeneralBillVO bill)
    throws BusinessException, SQLException
  {
    if ((bill == null) || (bill.getItemVOs() == null) || (bill.getItemVOs().length == 0))
    {
      SCMEnv.out("no bill para when rewrite.");
      return;
    }

    this.m_timer.start();

    GeneralBillItemVO[] body = (GeneralBillItemVO[])bill.getItemVOs();
    GeneralBillHeaderVO head = (GeneralBillHeaderVO)bill.getParentVO();

    if (!isModuleStarted(head.getPk_corp(), "SO")) {
      return;
    }
    try
    {
      String sHint = NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000139");

      if (isSaleBillProcessed(bill.getPrimaryKey())) {
        throw new BusinessException(sHint);
      }

      if (!isModuleStarted(head.getPk_corp(), "SO5")) {
        return;
      }
      Object obj = null;
      try
      {
        obj = NCLocator.getInstance().lookup(ISOApply.class.getName());
      } catch (Exception e) {
        throw new RemoteException(e.getMessage());
      }
      boolean bReturn = false;
      if (obj != null) {
        bReturn = ((ISOApply)obj).isSaleUsedInApply(bill.getPrimaryKey(), "4C");
      }

      if (bReturn) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000140"));
      }

    }
    catch (BusinessException ef)
    {
      throw ef;
    } catch (Exception e) {
      SCMEnv.error(e);
      throw new BusinessException(e.getMessage());
    }

    this.m_timer.stopAndShow("chk4C");
  }

  public void check4CCancelForSaleOrder(GeneralBillVO bill)
    throws BusinessException, SQLException
  {
    if ((bill == null) || (bill.getItemVOs() == null) || (bill.getItemVOs().length == 0))
    {
      SCMEnv.out("no bill para when rewrite.");
      return;
    }

    this.m_timer.start();

    GeneralBillItemVO[] body = (GeneralBillItemVO[])bill.getItemVOs();
    GeneralBillHeaderVO head = (GeneralBillHeaderVO)bill.getParentVO();

    if (!isModuleStarted(head.getPk_corp(), "SO")) {
      return;
    }
    try
    {
      String sHint = NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000141");

      Object obj = null;
      try
      {
        obj = NCLocator.getInstance().lookup(ISOToIC_DRP.class.getName());
      } catch (Exception e) {
        throw new RemoteException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000142"));
      }

      for (int i = 0; i < body.length; i++) {
        if ((obj == null) || 
          (!((ISOToIC_DRP)obj).isSaleOut(body[i].getCfirstbillhid(), body[i].getCfirstbillbid())))
          continue;
        throw new BusinessException(sHint);
      }
    }
    catch (BusinessException ef)
    {
      throw ef;
    } catch (Exception e) {
      SCMEnv.error(e);
      throw new BusinessException(e.getMessage());
    }

    this.m_timer.stopAndShow("chk4Cforsaleorder");
  }

  public void estimateBill(GeneralBillVO voBill)
    throws BusinessException
  {
    try
    {
      if ((voBill == null) || (voBill.getHeaderVO() == null)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000143"));
      }
      String sCorpID = null;
      if (voBill != null)
        sCorpID = voBill.getHeaderVO().getPk_corp();
      if (sCorpID == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000144"));
      }
      if (!isModuleStarted(sCorpID, "PO")) {
        return;
      }
      Object obj = null;
      try
      {
        obj = NCLocator.getInstance().lookup(IPuToIc_EstimateImpl.class.getName());
      } catch (Exception e) {
        throw new RemoteException(e.getMessage());
      }
      GeneralBillVO[] vos = { voBill };

      if (obj != null)
        ((IPuToIc_EstimateImpl)obj).estimateBatch(vos);
    }
    catch (Exception e)
    {
      if (((e instanceof RemoteException)) && (((RemoteException)e).detail != null))
      {
        throw new BusinessException(((RemoteException)e).detail.getMessage());
      }

      throw new BusinessException(e.getMessage());
    }
  }

  private GeneralBillItemVO[] getCombinedItems(GeneralBillVO newVO, GeneralBillVO oldVO)
  {
    GeneralBillItemVO[] voItems = null;

    if (newVO != null)
      newVO.setBisIUK_SourceBodyidOnly(true);
    if (oldVO != null)
      oldVO.setBisIUK_SourceBodyidOnly(true);
    GeneralBillVO voBill = GeneralBillVO.combine(newVO, oldVO);
    if (voBill != null)
      voItems = voBill.getItemVOs();
    return voItems;
  }

  public Hashtable getRewriteDMInfo(GeneralBillItemVO[] voItems)
    throws BusinessException
  {
    if ((voItems == null) || (voItems.length < 1))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
    int iItemCount = voItems.length;

    for (int i = 0; i < iItemCount; i++)
    {
      if (voItems[i].getCsourcebillhid() == null) {
        voItems[i].setCsourcebillhid(voItems[i].getCsourcebillbid());
      }
    }
    return getRewriteSrcInfo(voItems, "noutnum");
  }

  public Hashtable getRewriteSaleInfo(GeneralBillItemVO[] voItems)
    throws BusinessException
  {
    return getRewriteSrcInfo(voItems, "noutnum");
  }

  public Hashtable getRewriteSrcInfo(GeneralBillItemVO[] voItems, String sFieldNum)
    throws BusinessException
  {
    if ((voItems == null) || (voItems.length < 1))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
    int iItemCount = voItems.length;

    UFDouble dNum = null;

    String sSourtype = null;

    String sSourbillhid = null;

    String sSourbillbid = null;

    String sQualityLevel = null;

    Hashtable htNum = new Hashtable();
    String sKey = null;
    UFDouble dTempNum = null;
    UFDouble ufd0 = new UFDouble(0.0D);
    for (int i = 0; i < iItemCount; i++) {
      sSourtype = voItems[i].getCsourcetype();
      sSourbillhid = voItems[i].getCsourcebillhid();
      sSourbillbid = voItems[i].getCsourcebillbid();

      dNum = ufd0;
      if (voItems[i].getAttributeValue(sFieldNum) != null) {
        dNum = new UFDouble(voItems[i].getAttributeValue(sFieldNum).toString());
      }

      if ((sSourtype == null) || (sSourbillhid == null) || (sSourbillbid == null))
      {
        SCMEnv.out("---- rewrite -----> no source info");
      }
      else {
        sKey = sSourtype + "-" + sSourbillhid + "+" + sSourbillbid;
        if (htNum.containsKey(sKey)) {
          dTempNum = new UFDouble(htNum.get(sKey).toString());
          dTempNum = dTempNum.add(dNum);
          htNum.put(sKey, dTempNum);
        } else {
          htNum.put(sKey, dNum);
        }
      }
    }
    return htNum;
  }

  private UFDouble getStorHsl(GeneralBillItemVO voItem)
  {
    if (voItem == null)
      return null;
    UFDouble ufdBillStorHsl = null;

    String cStorUnitid = voItem.getStorUnitID(voItem.getCastunitid());

    boolean isHaveStoreUnit = false;
    String cMainUnit = voItem.getDesassemVO().getMainUnitID();

    boolean isNotChange = false;
    if ((cStorUnitid != null) && (cStorUnitid.length() > 0)) {
      ufdBillStorHsl = voItem.getBillSotreHsl();

      if (cStorUnitid.equals(cMainUnit))
        ufdBillStorHsl = new UFDouble(1.0D);
    }
    return ufdBillStorHsl;
  }

  public void reWriteCorNum(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    String[] sNumFields = { "ninnum", "ninassistnum", "ningrossnum" };
    GeneralBillVO vot = null;
    GeneralBillVO votNew = null;
    GeneralBillVO votOld = null;
    boolean isQtyFilled = false;
    if (newVO != null) {
      vot = newVO;
    }
    else {
      vot = oldVO;
    }
    if ((newVO == null) && (oldVO == null))
    {
      return;
    }

    if (vot.getBillInOutFlag() == -1) {
      sNumFields[0] = "noutnum";
      sNumFields[1] = "noutassistnum";
      sNumFields[2] = "noutgrossnum";
    }

    try
    {
      String CJ = new QueryInfoDMO().queryBusiTypeVerify(vot.getHeaderVO().getCbiztypeid());
      if ((CJ != null) && ((CJ.equalsIgnoreCase("C")) || (CJ.equalsIgnoreCase("J")))) {
        SCMEnv.out("借转类型，不回写累计出库数量");
        return;
      }

      GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO, new String[] { "ccorrespondbid" }, sNumFields);

      this.m_timer.stopAndShow("汇总VO");

      String billcode = null;

      if ((voItems == null) || (voItems.length < 1)) {
        SCMEnv.out("不回写累计出库数量");
        return;
      }

      GeneralBillDMO dmo = new GeneralBillDMO();
      String[] ids = new String[voItems.length];
      UFDouble[] nnums = new UFDouble[voItems.length];
      UFDouble[] nastnums = new UFDouble[voItems.length];
      UFDouble[] ngrsnums = new UFDouble[voItems.length];

      UFDouble neg = new UFDouble(-1);
      for (int i = 0; i < voItems.length; i++) {
        ids[i] = voItems[i].getCcorrespondbid();
        nnums[i] = ((UFDouble)voItems[i].getAttributeValue(sNumFields[0]));
        nastnums[i] = ((UFDouble)voItems[i].getAttributeValue(sNumFields[1]));
        ngrsnums[i] = ((UFDouble)voItems[i].getAttributeValue(sNumFields[2]));
        if (nnums[i] == null)
          nnums[i] = nc.vo.ic.pub.GenMethod.ZERO;
        if (nastnums[i] == null)
          nastnums[i] = nc.vo.ic.pub.GenMethod.ZERO;
        if (ngrsnums[i] == null) {
          ngrsnums[i] = nc.vo.ic.pub.GenMethod.ZERO;
        }
        if (vot.getBillInOutFlag() == 1) {
          if (nnums[i] != null)
            nnums[i] = nnums[i].multiply(neg);
          if (nastnums[i] != null)
            nastnums[i] = nastnums[i].multiply(neg);
          if (ngrsnums[i] != null) {
            ngrsnums[i] = ngrsnums[i].multiply(neg);
          }
        }
      }

      dmo.updateCorNumBatch(ids, nnums, nastnums, ngrsnums);

      this.m_timer.stopAndShow("回写累计出库数量");
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
  }

  public boolean isHaveRecord(String sSql, String sBillPK)
    throws BusinessException, SQLException
  {
    if ((sSql == null) || (sBillPK == null))
      return false;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String sTemp = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sSql);
      stmt.setString(1, sBillPK);
      rs = stmt.executeQuery();
      if (rs.next())
        sTemp = rs.getString(1);
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
      catch (Exception e)
      {
      }
    }
    return (sTemp != null) && (sTemp.trim().length() > 0);
  }

  public boolean isModuleStarted(String sCorpID, String sModuleCode)
    throws BusinessException
  {
    return new CreatecorpDMO().isEnabled(sCorpID, sModuleCode);
  }

  public boolean isSaleBillProcessed(String sBillPk)
    throws SQLException
  {
    beforeCallMethod("nc.bs.ic.pub.RewriteDMO", "isSaleBillProcessed", new Object[] { sBillPk });

    String sql = " SELECT cinvoice_bid ";
    sql = sql + " FROM so_saleinvoice_b ";
    sql = sql + " WHERE dr = 0 AND cupreceipttype='4C' AND cupsourcebillid=? ";

    boolean bProcessed = false;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, sBillPk);

      rs = stmt.executeQuery();

      if (rs.next())
        bProcessed = true;
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
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }

    afterCallMethod("nc.bs.ic.pub.RewriteDMO", "isSaleBillProcessed", new Object[] { sBillPk });

    return bProcessed;
  }

  public void reWriteDMNew(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    if ((newVO == null) && (oldVO == null)) {
      return;
    }

    String[] sNumFields = { "noutnum" };

    GeneralBillVO vot = null;
    if (newVO != null) {
      vot = newVO;
    }
    else {
      vot = oldVO;
    }

    String sCorpID = vot.getHeaderVO().getPk_corp();

    if (!isModuleStarted(sCorpID, "DM")) {
      return;
    }
    if ((vot.getItemVOs() == null) || (vot.getItemVOs().length == 0)) {
      return;
    }

    String pksendtype = vot.getHeaderVO().getCdilivertypeid();

    String csourcetype = vot.getItemVOs()[0].getCsourcetype();

    if ((csourcetype == null) || (!csourcetype.startsWith("7")))
      return;
    try
    {
      this.m_timer.start();
      GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO, new String[] { "csourcetype", "csourcebillhid", "csourcebillbid" }, sNumFields);

      this.m_timer.stopAndShow("汇总VO");

      if ((voItems == null) || (voItems.length < 1)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
      }

      DMDataVO[] aDMVOs = new DMDataVO[voItems.length];
      for (int i = 0; i < voItems.length; i++)
      {
        aDMVOs[i] = new DMDataVO();

        aDMVOs[i].setAttributeValue("pksourceh", voItems[i].getCsourcebillhid());
        aDMVOs[i].setAttributeValue("pksourceb", voItems[i].getCsourcebillbid());
        aDMVOs[i].setAttributeValue("sourcebilltype", voItems[i].getCsourcetype());
        aDMVOs[i].setAttributeValue("noutnum", voItems[i].getNoutnum());
        aDMVOs[i].setAttributeValue("pk_corp", sCorpID);
        aDMVOs[i].setAttributeValue("pksendtype", pksendtype);
      }

      this.m_timer.start();
      Object obj = null;

      obj = NCLocator.getInstance().lookup(IDMToIC.class.getName());

      ClientLink clientLink = new ClientLink(sCorpID, vot.getHeaderVO().getCoperatoridnow(), null, null, null, null, null, null, null, false, null, null, null);

      ((IDMToIC)obj).setOutnum(aDMVOs, clientLink);

      this.m_timer.stopAndShow("回写发运");
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
  }

  public void reWriteMMNew(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO);

    if ((voItems == null) || (voItems.length < 1))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
    try
    {
      int iItemCount = voItems.length;

      String sCorpID = null;
      if (newVO != null)
        sCorpID = newVO.getHeaderVO().getPk_corp();
      else
        sCorpID = oldVO.getHeaderVO().getPk_corp();
      if (sCorpID == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000146"));
      }
      if (!isModuleStarted(sCorpID, "MM")) {
        return;
      }

      UFDouble dNum = null;

      String pk_calbody = null;

      String sSourbillhid = null;

      String sSourbillbid = null;
      String sFirstbillhid = null;
      String sFirstbillbid = null;
      Hashtable htNum = new Hashtable();
      String sKey = null;
      UFDouble dTempNum = null;

      for (int i = 0; i < iItemCount; i++) {
        if (oldVO == null)
          pk_calbody = (String)newVO.getHeaderValue("pk_calbody");
        else {
          pk_calbody = (String)oldVO.getHeaderValue("pk_calbody");
        }
        sSourbillhid = voItems[i].getCsourcebillhid();
        sSourbillbid = voItems[i].getCsourcebillbid();
        sFirstbillhid = voItems[i].getCfirstbillhid();
        sFirstbillbid = voItems[i].getCfirstbillbid();
        dNum = null;
        if (voItems[i].getNoutnum() != null)
          dNum = new UFDouble(voItems[i].getNoutnum().toString());
        if (dNum == null) {
          dNum = new UFDouble(0.0D);
        }
        if ((pk_calbody == null) || (sSourbillhid == null) || (sSourbillbid == null))
        {
          SCMEnv.out("---- rewrite -----> no source info");
        }
        else {
          sKey = pk_calbody + "-" + sSourbillhid + "+" + sSourbillbid;
          if (htNum.containsKey(sKey)) {
            dTempNum = new UFDouble(htNum.get(sKey).toString());
            dTempNum = dTempNum.add(dNum);
            htNum.put(sKey, dTempNum);
          } else {
            htNum.put(sKey, dNum);
          }
        }
      }
      int istart1 = 0;
      int istart2 = 0;

      Object objTemp = null;
      if ((htNum != null) && (htNum.size() > 0)) {
        Enumeration enumKey = htNum.keys();
        while (enumKey.hasMoreElements()) {
          objTemp = enumKey.nextElement();
          dNum = (UFDouble)htNum.get(objTemp);
          sKey = objTemp.toString();
          istart1 = sKey.indexOf("-");
          istart2 = sKey.indexOf("+");

          pk_calbody = sKey.substring(0, istart1);
          sSourbillhid = sKey.substring(istart1 + 1, istart2);
          sSourbillbid = sKey.substring(istart2 + 1);
        }
      }

      this.m_timer.stopAndShow("回写备料计划old");
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
  }

  public void reWriteMMNewBatch(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    GeneralBillVO vot = null;
    if (newVO != null) {
      vot = newVO;
    }
    else {
      vot = oldVO;
    }

    GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO);

    if ((voItems == null) || (voItems.length < 1))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
    try
    {
      int iItemCount = voItems.length;

      String sCorpID = null;
      if (newVO != null)
        sCorpID = newVO.getHeaderVO().getPk_corp();
      else
        sCorpID = oldVO.getHeaderVO().getPk_corp();
      if (sCorpID == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000146"));
      }

      String csourcetype = vot.getItemVOs()[0].getCsourcetype();
      if ((csourcetype == null) || (!csourcetype.equals("A3"))) {
        return;
      }

      UFDouble[] dNums = null;
      UFDouble[] dAstNums = null;
      UFDouble dNum = null;
      UFDouble dAstNum = null;

      String pk_calbody = null;

      String sSourbillhid = null;

      String sSourbillbid = null;

      Hashtable htNum = new Hashtable();
      String sKey = null;
      UFDouble[] dTmp = null;
      UFDouble ZERO = new UFDouble(0);
      String castunitid = null;
      String cbatchcode = null;
      ArrayList alhid = new ArrayList();
      ArrayList altmp = null;
      ArrayList alrow = null;
      if (oldVO == null)
        pk_calbody = (String)newVO.getHeaderValue("pk_calbody");
      else {
        pk_calbody = (String)oldVO.getHeaderValue("pk_calbody");
      }
      for (int i = 0; i < iItemCount; i++)
      {
        sSourbillhid = voItems[i].getCsourcebillhid();
        sSourbillbid = voItems[i].getCsourcebillbid();

        if ((pk_calbody == null) || (sSourbillhid == null) || (sSourbillbid == null))
        {
          SCMEnv.out("---- rewrite -----> no source info");
        }
        else {
          if (!htNum.containsKey(sSourbillhid)) {
            alhid.add(sSourbillhid);
          }
          if (voItems[i].getCastunitid() != null)
            castunitid = voItems[i].getCastunitid();
          else {
            castunitid = "null";
          }

          cbatchcode = voItems[i].getVbatchcode();

          dNum = ZERO;
          dAstNum = ZERO;
          if (voItems[i].getNoutnum() != null)
            dNum = voItems[i].getNoutnum();
          if (voItems[i].getNoutassistnum() != null) {
            dAstNum = voItems[i].getNoutassistnum();
          }
          if (htNum.containsKey(sSourbillhid)) {
            altmp = (ArrayList)htNum.get(sSourbillhid);
          } else {
            altmp = new ArrayList();
            htNum.put(sSourbillhid, altmp);
          }
          alrow = new ArrayList();
          alrow.add(sSourbillbid);
          alrow.add(castunitid);
          alrow.add(dNum);
          alrow.add(dAstNum);
          alrow.add(cbatchcode);
          altmp.add(alrow);
        }
      }
      ArrayList albid = new ArrayList();
      ArrayList alcastid = new ArrayList();
      ArrayList alnum = new ArrayList();
      ArrayList alastnum = new ArrayList();
      ArrayList albatchcode = new ArrayList();
      for (int i = 0; i < alhid.size(); i++) {
        sSourbillhid = (String)alhid.get(i);
        altmp = (ArrayList)htNum.get(sSourbillhid);
        SCMEnv.out("@@@@回写备料计划开始" + i);
        if (altmp != null) {
          for (int j = 0; j < altmp.size(); j++) {
            alrow = (ArrayList)altmp.get(j);
            albid.add(alrow.get(0));
            SCMEnv.out("@@@@子表ID" + alrow.get(0));
            alcastid.add(alrow.get(1));
            SCMEnv.out("@@@@辅计量" + alrow.get(1));
            alnum.add(alrow.get(2));
            SCMEnv.out("@@@@数量" + alrow.get(2));
            alastnum.add(alrow.get(3));
            SCMEnv.out("@@@@辅数量" + alrow.get(3));
            albatchcode.add(alrow.get(4));
            SCMEnv.out("@@@@批次号" + alrow.get(4));
          }
          int size = albid.size();
          String[] bids = new String[size];
          String[] castids = new String[size];
          UFDouble[] nums = new UFDouble[size];
          UFDouble[] astnums = new UFDouble[size];
          String[] saBatchcode = new String[size];

          bids = (String[])(String[])albid.toArray(bids);
          castids = (String[])(String[])alcastid.toArray(castids);
          nums = (UFDouble[])(UFDouble[])alnum.toArray(nums);
          astnums = (UFDouble[])(UFDouble[])alastnum.toArray(astnums);
          saBatchcode = (String[])(String[])albatchcode.toArray(saBatchcode);

          int result = 0;
          try {
            if (ModuleEnable.isModuleEnabled_MM("MM")) {
              result = MMHelper.getIPdToIc().setInfoIc(sCorpID, pk_calbody, sSourbillhid, bids, nums, astnums, castids, saBatchcode);
            }
            else
            {
              PickmDMO dmo = new PickmDMO();
              result = dmo.setInfo(sCorpID, pk_calbody, sSourbillhid, bids, nums, astnums, castids, saBatchcode);
            }
          }
          catch (Exception e)
          {
            if ((e instanceof InvocationTargetException)) {
              String errmsg = ((InvocationTargetException)e).getTargetException().getMessage();
              throw new BusinessException(errmsg);
            }
            throw new BusinessException(e.getMessage());
          }

          albid.clear();
          alcastid.clear();
          alnum.clear();
          alastnum.clear();
          albatchcode.clear();

          if (result == 2) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000425"));
          }

        }

      }

      this.m_timer.stopAndShow("回写备料计划batch");
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
  }

  public void reWriteMMWRPI(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO);
    if ((voItems == null) || (voItems.length < 1))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
    try
    {
      int iItemCount = voItems.length;

      String sCorpID = null;
      if (newVO != null)
        sCorpID = newVO.getHeaderVO().getPk_corp();
      else
        sCorpID = oldVO.getHeaderVO().getPk_corp();
      if (sCorpID == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000146"));
      }
      if (!isModuleStarted(sCorpID, "MM")) {
        return;
      }

      UFDouble dNum = null;
      UFDouble dAstNum = null;

      String sSourbillbid = null;
      String castunitid = null;
      Hashtable htNum = new Hashtable();
      String sKey = null;
      UFDouble dTempNum = null;
      UFDouble dTempAstNum = null;
      UFDouble[] dTmp = null;
      UFDouble ZERO = new UFDouble(0);

      for (int i = 0; i < iItemCount; i++)
      {
        sSourbillbid = voItems[i].getCsourcebillbid();

        if (sSourbillbid == null) {
          SCMEnv.out("---- rewrite -----> no source info");
        }
        else
        {
          castunitid = voItems[i].getCastunitid();

          if (castunitid == null) {
            castunitid = "null";
          }
          dNum = ZERO;
          dTempAstNum = ZERO;

          if (voItems[i].getNinnum() != null)
            dNum = voItems[i].getNinnum();
          if (voItems[i].getNinassistnum() != null) {
            dAstNum = voItems[i].getNinassistnum();
          }

          sKey = sSourbillbid + "+" + castunitid;

          if (htNum.containsKey(sKey)) {
            dTmp = (UFDouble[])(UFDouble[])htNum.get(sKey);
            dTmp[0] = dTmp[0].add(dNum);
            dTmp[1] = dTmp[1].add(dAstNum);
            htNum.put(sKey, dTmp);
          } else {
            dTmp = new UFDouble[2];
            dTmp[0] = dNum;
            dTmp[1] = dAstNum;
            htNum.put(sKey, dTmp);
          }
        }
      }

      Object objTemp = null;

      if ((htNum != null) && (htNum.size() > 0)) {
        Enumeration enumKey = htNum.keys();
        while (enumKey.hasMoreElements()) {
          objTemp = enumKey.nextElement();
          dTmp = (UFDouble[])(UFDouble[])htNum.get(objTemp);
          sKey = objTemp.toString();

          sSourbillbid = sKey.substring(0, sKey.indexOf("+"));
          castunitid = sKey.substring(sKey.indexOf("+") + 1, sKey.length());

          if ("null".equals(castunitid)) {
            try
            {
              MMHelper.getIMmToIc().rewriteIncomNumIC(sSourbillbid, null, dTmp[0], null);
            }
            catch (Exception e)
            {
              if ((e instanceof InvocationTargetException)) {
                String errmsg = ((InvocationTargetException)e).getTargetException().getMessage();
                throw new BusinessException(errmsg);
              }
              throw new BusinessException(e.getMessage());
            }

          }

          try
          {
            MMHelper.getIMmToIc().rewriteIncomNumIC(sSourbillbid, castunitid, dTmp[0], dTmp[1]);
          } catch (Exception e) {
            throw new RemoteException(e.getMessage());
          }
        }
      }

      this.m_timer.stopAndShow("回写完工报告");
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
  }

  public void reWritePurNew(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    if ((newVO != null) && (newVO.getItemCount() > 0) && (oldVO != null) && (oldVO.getItemCount() > 0))
    {
      oldVO.setStatus(0);
    } else if (((newVO == null) || (newVO.getItemCount() == 0)) && (oldVO != null) && (oldVO.getItemCount() > 0))
    {
      oldVO.setStatus(3);
    } else if ((newVO != null) && (newVO.getItemCount() > 0) && ((oldVO == null) || (oldVO.getItemCount() == 0)))
    {
      newVO.setStatus(2);
    }
    else throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000147"));

    Hashtable htOldNum = new Hashtable();

    if (oldVO != null) {
      GeneralBillItemVO[] voItems = oldVO.getItemVOs();
      for (int i = 0; i < voItems.length; i++) {
        htOldNum.put(voItems[i].getCgeneralbid(), voItems[i]);
      }

    }

    try
    {
      String sCorpID = null; String sPK_PurCorp = null;
      String sBiztype = null;
      UFBoolean isReplenish = new UFBoolean(false);

      if (newVO != null) {
        sCorpID = newVO.getHeaderVO().getPk_corp();
        sPK_PurCorp = (String)newVO.getHeaderVO().getAttributeValue("pk_purcorp");
        sBiztype = newVO.getHeaderVO().getCbiztypeid();
        isReplenish = newVO.getHeaderVO().getFreplenishflag();
      }
      else {
        sCorpID = oldVO.getHeaderVO().getPk_corp();
        sPK_PurCorp = (String)oldVO.getHeaderVO().getAttributeValue("pk_purcorp");
        sBiztype = oldVO.getHeaderVO().getCbiztypeid();
        isReplenish = oldVO.getHeaderVO().getFreplenishflag();
      }
      if (sCorpID == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000146"));
      }
      if (!isModuleStarted(sCorpID, "PO")) {
        return;
      }

      ArrayList vData = new ArrayList();
      UFDouble ZERO = new UFDouble(0.0D);
      GeneralBillItemVO voOldItem = null;

      boolean weatherbreak = true;

      if (newVO != null) {
        GeneralBillItemVO[] voNewItems = null;
        voNewItems = newVO.getItemVOs();

        for (int i = 0; i < voNewItems.length; i++)
        {
          if (voNewItems[i].getStatus() == 0) {
            htOldNum.remove(voNewItems[i].getCgeneralbid());
          }
          else {
            ToICParaVO voPara = new ToICParaVO();
            voPara.setPk_corp(sCorpID);
            voPara.setBizType(sBiztype);
            voPara.setBillType(voNewItems[i].getCsourcetype());
            voPara.setRowID(voNewItems[i].getCsourcebillbid());
            voPara.setCmangid(voNewItems[i].getCinventoryid());

            voPara.setRowRowID(voNewItems[i].getCorder_bb1id());
            voPara.setCheckStateID(voNewItems[i].getCcheckstateid());
            voPara.setIsback(isReplenish);

            voPara.setNewNum(ZERO);
            voPara.setOldNum(ZERO);

            voPara.setIsPresent(voNewItems[i].getFlargess());
            voPara.setIsUpPresent(voNewItems[i].getBsourcelargess());
            voPara.setIsPresentOld(voNewItems[i].getFlargess());

            voPara.setPk_corp(sPK_PurCorp);
            voPara.setPk_arrvcorp(sCorpID);

            if (voNewItems[i].getNinnum() != null) {
              weatherbreak = false;
              voPara.setNewNum(voNewItems[i].getNinnum());
            }

            if (voNewItems[i].getStatus() == 3) {
              weatherbreak = false;
              voPara.setNewNum(ZERO);
            }

            if (htOldNum.containsKey(voNewItems[i].getCgeneralbid()))
            {
              voOldItem = (GeneralBillItemVO)htOldNum.get(voNewItems[i].getCgeneralbid());
              voPara.setIsPresentOld(voOldItem.getFlargess());
              voPara.setOldNum(voOldItem.getNinnum());
              htOldNum.remove(voNewItems[i].getCgeneralbid());
            }
            vData.add(voPara);
          }

        }

      }

      if (htOldNum.size() > 0)
      {
        weatherbreak = false;
        Enumeration enumKey = htOldNum.keys();
        while (enumKey.hasMoreElements()) {
          Object objTemp = enumKey.nextElement();
          voOldItem = (GeneralBillItemVO)htOldNum.get(objTemp);
          ToICParaVO voPara = new ToICParaVO();
          voPara.setPk_corp(sCorpID);
          voPara.setBizType(sBiztype);
          voPara.setBillType(voOldItem.getCsourcetype());
          voPara.setRowID(voOldItem.getCsourcebillbid());

          voPara.setRowRowID(voOldItem.getCorder_bb1id());
          voPara.setCheckStateID(voOldItem.getCcheckstateid());
          voPara.setIsback(isReplenish);
          voPara.setNewNum(ZERO);
          voPara.setOldNum(voOldItem.getNinnum());
          voPara.setIsPresent(voOldItem.getFlargess());
          voPara.setIsPresentOld(voOldItem.getFlargess());
          voPara.setIsUpPresent(voOldItem.getBsourcelargess());
          voPara.setCmangid(voOldItem.getCinventoryid());

          voPara.setPk_corp(sPK_PurCorp);
          voPara.setPk_arrvcorp(sCorpID);

          vData.add(voPara);
        }
      }
      if (weatherbreak) {
        return;
      }

      Object obj = NCLocator.getInstance().lookup(IPuToIc_ToIC.class.getName());
      if (vData.size() > 0)
      {
        boolean bUserConfirmFlag = false;
        if (((newVO != null) && (newVO.isRwtPuUserConfirmFlag())) || ((oldVO != null) && (oldVO.isRwtPuUserConfirmFlag()))) {
          bUserConfirmFlag = true;
        }
        ToICParaVO[] voaPara = new ToICParaVO[vData.size()];
        int i = 0; for (int loop = voaPara.length; i < loop; i++) {
          voaPara[i] = ((ToICParaVO)vData.get(i));
          voaPara[i].setUserConfirm(bUserConfirmFlag);
        }

        SCMEnv.out("rewrite line num=" + vData.size());

        ((IPuToIc_ToIC)obj).setInNum(voaPara);
      }

      this.m_timer.stopAndShow("回写采购");
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
  }

  public void reWriteSaleNew(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO);

    String sbiderr = null;

    String billcode = null;

    if ((voItems == null) || (voItems.length < 1))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
    try
    {
      String sCorpID = null;
      if (newVO != null)
        sCorpID = newVO.getHeaderVO().getPk_corp();
      else
        sCorpID = oldVO.getHeaderVO().getPk_corp();
      if (sCorpID == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000146"));
      }
      if (!isModuleStarted(sCorpID, "SO")) {
        return;
      }
      int iItemCount = voItems.length;

      Object obj = null;
      try
      {
        obj = NCLocator.getInstance().lookup(ISOToIC_DRP.class.getName());
      } catch (Exception e) {
        throw new RemoteException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000142"));
      }

      UFDouble dNum = null;

      String sSourtype = null;

      String sSourbillhid = null;

      String sSourbillbid = null;

      String sQualityLevel = null;
      Hashtable htNum = getRewriteSaleInfo(voItems);
      String sKey = null;
      UFDouble dTempNum = null;

      int istart1 = 0;
      int istart2 = 0;
      int istart3 = 0;

      Object objTemp = null;
      if ((htNum != null) && (htNum.size() > 0)) {
        Enumeration enumKey = htNum.keys();
        while (enumKey.hasMoreElements()) {
          objTemp = enumKey.nextElement();
          dNum = (UFDouble)htNum.get(objTemp);
          sKey = objTemp.toString();
          istart1 = sKey.indexOf("-");
          istart2 = sKey.indexOf("+");

          sSourtype = sKey.substring(0, istart1);
          sSourbillhid = sKey.substring(istart1 + 1, istart2);
          sSourbillbid = sKey.substring(istart2 + 1);
          sbiderr = sSourbillbid;
          SCMEnv.out("\n\n\n---------------------------" + sSourbillbid + ", num=" + dNum + "\n\n");

          if (obj != null) {
            ((ISOToIC_DRP)obj).setOutNum(sCorpID, sSourtype, sSourbillhid, sSourbillbid, dNum);
          }
        }

      }

      this.m_timer.stopAndShow("回写销售");
    }
    catch (Exception e) {
      if ((e instanceof BusinessException))
      {
        String sErrMsg = GeneralMethodGetErrorMsg.spellString(new String[] { billcode, sbiderr }, "#U#", "$$AAA$$", "$$ZZZ$$");

        throw new BusinessException(sErrMsg + e.getMessage());
      }

      throw new BusinessException(e.getMessage());
    }
  }

  public void reWriteSaleNewBatch(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    if ((newVO != null) && ((newVO.getItemVOs() == null) || (newVO.getItemVOs().length == 0))) {
      return;
    }
    if ((oldVO != null) && ((oldVO.getItemVOs() == null) || (oldVO.getItemVOs().length == 0))) {
      return;
    }

    String[] sNumFields = { "noutnum", "noutassistnum", "nshouldoutnum", "nshouldoutassistnum" };
    GeneralBillVO vot = null;

    String sCorpID = null;
    String pksendtype = null;
    String csourcetype = null;
    String cfirsttype = null;
    if (newVO != null) {
      vot = new GeneralBillVO();
      vot.setParentVO((GeneralBillHeaderVO)newVO.getHeaderVO().clone());
      csourcetype = newVO.getItemVOs()[0].getCsourcetype();
      cfirsttype = newVO.getItemVOs()[0].getCfirsttype();
    }
    else {
      vot = new GeneralBillVO();
      vot.setParentVO((GeneralBillHeaderVO)oldVO.getHeaderVO().clone());
      csourcetype = oldVO.getItemVOs()[0].getCsourcetype();
      cfirsttype = oldVO.getItemVOs()[0].getCfirsttype();
    }
    sCorpID = vot.getHeaderVO().getPk_corp();

    if (!isModuleStarted(sCorpID, "SO")) {
      return;
    }

    pksendtype = vot.getHeaderVO().getCdilivertypeid();

    if ((cfirsttype == null) || (!cfirsttype.startsWith("3"))) {
      return;
    }

    try
    {
      String[] fieldkeys = { "cfirsttype", "cfirstbillhid", "cfirstbillbid", "csourcetype", "csourcebillhid", "csourcebillbid" };

      GeneralBillVO[] genvos = processNshouldoutnum(newVO, oldVO, fieldkeys, sNumFields);
      if (genvos == null)
        return;
      GeneralBillItemVO[] voItems = getCombinedItems(genvos[0], genvos[1], fieldkeys, sNumFields);

      vot.setChildrenVO(voItems);
      this.m_timer.stopAndShow("汇总VO");

      String billcode = null;

      if ((voItems == null) || (voItems.length < 1)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
      }

      boolean isBack = false;

      if ((vot.getHeaderVO().getFreplenishflag() != null) && (vot.getHeaderVO().getFreplenishflag().booleanValue()) && ((csourcetype.equals("3U")) || (csourcetype.equals("3V")) || (cfirsttype.equals("3U")) || (cfirsttype.equals("3V"))))
      {
        isBack = true;
      }
      if (isBack)
      {
        Object obj = null;
        try
        {
          obj = NCLocator.getInstance().lookup(ISOTake.class.getName());
        }
        catch (Exception e) {
          throw new RemoteException(e.getMessage());
        }

        if (obj != null) {
          ((ISOTake)obj).setOutNum(vot);
        }

      }
      else
      {
        Object obj = null;
        try
        {
          obj = NCLocator.getInstance().lookup(ISOToIC_DRP.class.getName());
        } catch (Exception e) {
          throw new RemoteException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000142"));
        }

        if (obj != null) {
          ((ISOToIC_DRP)obj).isOverSaleOrder(vot);
          ((ISOToIC_DRP)obj).setOutNum(vot);
        }

      }

      this.m_timer.stopAndShow("回写销售batch");
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
  }

  public void reWriteTranOrder(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException, Exception
  {
    this.m_timer.start();
    if ((newVO != null) && ((newVO.getItemVOs() == null) || (newVO.getItemVOs().length == 0))) {
      return;
    }
    if ((oldVO != null) && ((oldVO.getItemVOs() == null) || (oldVO.getItemVOs().length == 0))) {
      return;
    }
    if ((newVO == null) && (oldVO == null)) {
      return;
    }

    boolean isOut = false;
    try
    {
      String sCorpID = (String)(newVO == null ? oldVO : newVO).getHeaderValue("pk_corp");

      if (!isModuleStarted(sCorpID, "TO")) {
        return;
      }
      String cfirsttype = null;
      String cbilltypecode = null;

      if (newVO != null) {
        cfirsttype = newVO.getItemVOs()[0].getCfirsttype();
        cbilltypecode = newVO.getHeaderVO().getCbilltypecode();
        if (newVO.getBillInOutFlag() == -1)
          isOut = true;
      }
      else {
        cfirsttype = oldVO.getItemVOs()[0].getCfirsttype();
        cbilltypecode = oldVO.getHeaderVO().getCbilltypecode();
        if (oldVO.getBillInOutFlag() == -1) {
          isOut = true;
        }

      }

      if ((cfirsttype == null) || (!cfirsttype.startsWith("5"))) {
        return;
      }

      String[] fieldkeys = { "cfirstbillhid", "cfirstbillbid" };
      String[] sNumFields = { "ninnum", "ninassistnum", "noutnum", "noutassistnum", "nshouldoutnum", "nshouldinnum", "nshouldoutassistnum" };

      GeneralBillVO[] genvos = processNshouldoutnum(newVO, oldVO, fieldkeys, sNumFields);
      if (genvos == null)
        return;
      GeneralBillItemVO[] voItems = getCombinedItems(genvos[0], genvos[1], fieldkeys, sNumFields);

      if ((voItems == null) || (voItems.length == 0)) {
        return;
      }

      String[] shids = new String[voItems.length];
      String[] sbids = new String[voItems.length];
      UFDouble[] dNums = new UFDouble[voItems.length];

      UFDouble[] dAstNums = new UFDouble[voItems.length];
      UFDouble[] dShouldNums = new UFDouble[voItems.length];
      UFDouble[] dShouldAstNums = new UFDouble[voItems.length];

      for (int i = 0; i < voItems.length; i++) {
        shids[i] = voItems[i].getCfirstbillhid();
        sbids[i] = voItems[i].getCfirstbillbid();
        if (!isOut) {
          dNums[i] = voItems[i].getNinnum();
          dAstNums[i] = voItems[i].getNinassistnum();
          if (dNums[i] == null) {
            dNums[i] = nc.vo.ic.pub.GenMethod.ZERO;
          }
          if (dAstNums[i] == null)
            dAstNums[i] = nc.vo.ic.pub.GenMethod.ZERO;
        }
        else
        {
          dNums[i] = voItems[i].getNoutnum();
          dAstNums[i] = voItems[i].getNoutassistnum();
          if (dNums[i] == null) {
            dNums[i] = nc.vo.ic.pub.GenMethod.ZERO;
          }
          if (dAstNums[i] == null) {
            dAstNums[i] = nc.vo.ic.pub.GenMethod.ZERO;
          }

          dShouldNums[i] = voItems[i].getNshouldoutnum();
          dShouldAstNums[i] = voItems[i].getNshouldoutassistnum();
        }
      }

      ParaRewriteVO voWrite = new ParaRewriteVO();
      voWrite.setPk_corp(sCorpID);
      voWrite.setCHeadIdArray(shids);
      voWrite.setCBodyIdArray(sbids);
      voWrite.setDNumArray(dNums);
      voWrite.setDAssNumArray(dAstNums);

      if (isOut) {
        voWrite.setDShouldNumArray(dShouldNums);
        voWrite.setDShouldAssNumArray(dShouldAstNums);
      }

      Object obj = null;
      try
      {
        obj = NCLocator.getInstance().lookup(IOuter.class.getName());
      } catch (Exception e) {
        throw new RemoteException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000149"));
      }

      if (obj != null) {
        ((IOuter)obj).backToOrder(voWrite, cbilltypecode);
      }
      this.m_timer.stopAndShow("回写调拨订单batch");
    }
    catch (Exception e)
    {
      throw e;
    }
  }

  public void reWriteTranOutNum(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    if ((newVO != null) && ((newVO.getItemVOs() == null) || (newVO.getItemVOs().length == 0))) {
      return;
    }
    if ((oldVO != null) && ((oldVO.getItemVOs() == null) || (oldVO.getItemVOs().length == 0))) {
      return;
    }
    if ((newVO == null) && (oldVO == null))
    {
      return;
    }

    try
    {
      String csourcetype = null;
      String cbilltypecode = null;

      if (newVO != null) {
        csourcetype = newVO.getItemVOs()[0].getCsourcetype();
      }
      else
      {
        csourcetype = oldVO.getItemVOs()[0].getCsourcetype();
      }

      if ((csourcetype == null) || (!csourcetype.equals("4Y"))) {
        return;
      }

      GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO, new String[] { "csourcebillbid" }, new String[] { "ninnum", "ninassistnum" });

      if ((voItems == null) || (voItems.length < 1)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
      }

      int iItemCount = voItems.length;
      String[] ids = new String[iItemCount];
      UFDouble[] nnums = new UFDouble[iItemCount];

      for (int i = 0; i < iItemCount; i++)
      {
        ids[i] = voItems[i].getCsourcebillbid();
        nnums[i] = voItems[i].getNinnum();
        if (nnums[i] == null) {
          nnums[i] = nc.vo.ic.pub.GenMethod.ZERO;
        }

      }

      nc.bs.ic.ic218.GeneralHDMO dmo = new nc.bs.ic.ic218.GeneralHDMO();
      dmo.updateTransNumBatch(ids, nnums);

      this.m_timer.stopAndShow("回写调拨出库单");
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }

      throw new BusinessException(e.getMessage());
    }
  }

  public void reWriteBorrowRetNum(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    if ((newVO != null) && ((newVO.getItemVOs() == null) || (newVO.getItemVOs().length == 0))) {
      return;
    }
    if ((oldVO != null) && ((oldVO.getItemVOs() == null) || (oldVO.getItemVOs().length == 0))) {
      return;
    }
    if ((newVO == null) && (oldVO == null))
    {
      return;
    }

    try
    {
      String csourcetype = null;
      String cbilltypecode = null;

      if (newVO != null) {
        csourcetype = newVO.getItemVOs()[0].getCsourcetype();
      }
      else
      {
        csourcetype = oldVO.getItemVOs()[0].getCsourcetype();
      }

      if ((csourcetype == null) || (!csourcetype.startsWith("4"))) {
        return;
      }

      GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO, new String[] { "csourcebillhid", "csourcebillbid" }, new String[] { "noutnum", "noutgrossnum" });

      if ((voItems == null) || (voItems.length < 1)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
      }

      int iItemCount = voItems.length;
      String[] hids = new String[iItemCount];
      String[] bids = new String[iItemCount];
      UFDouble[] nnums = new UFDouble[iItemCount];
      UFDouble[] ngrsnums = new UFDouble[iItemCount];

      for (int i = 0; i < iItemCount; i++) {
        hids[i] = voItems[i].getCsourcebillhid();
        bids[i] = voItems[i].getCsourcebillbid();
        nnums[i] = voItems[i].getNoutnum();
        ngrsnums[i] = voItems[i].getNoutgrossnum();
      }

      nc.bs.ic.ic205.GeneralHDMO dmo = new nc.bs.ic.ic205.GeneralHDMO();

      dmo.updateItemRetNumBatch(hids, bids, nnums, ngrsnums);

      this.m_timer.stopAndShow("回写借入还回数量");
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }

      throw new BusinessException(e.getMessage());
    }
  }

  public void reWriteLendRetNum(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    if ((newVO != null) && ((newVO.getItemVOs() == null) || (newVO.getItemVOs().length == 0))) {
      return;
    }
    if ((oldVO != null) && ((oldVO.getItemVOs() == null) || (oldVO.getItemVOs().length == 0))) {
      return;
    }
    if ((newVO == null) && (oldVO == null)) {
      return;
    }

    try
    {
      String csourcetype = null;
      String cbilltypecode = null;

      if (newVO != null) {
        csourcetype = newVO.getItemVOs()[0].getCsourcetype();
      }
      else
      {
        csourcetype = oldVO.getItemVOs()[0].getCsourcetype();
      }

      if ((csourcetype == null) || (!csourcetype.startsWith("4"))) {
        return;
      }

      GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO, new String[] { "csourcebillhid", "csourcebillbid" }, new String[] { "ninnum", "ningrossnum" });

      if ((voItems == null) || (voItems.length < 1)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
      }

      int iItemCount = voItems.length;
      String[] hids = new String[iItemCount];
      String[] bids = new String[iItemCount];
      UFDouble[] nnums = new UFDouble[iItemCount];
      UFDouble[] ngrossnums = new UFDouble[iItemCount];
      nc.bs.ic.ic215.GeneralHDMO dmo = new nc.bs.ic.ic215.GeneralHDMO();

      for (int i = 0; i < iItemCount; i++) {
        hids[i] = voItems[i].getCsourcebillhid();
        bids[i] = voItems[i].getCsourcebillbid();
        nnums[i] = voItems[i].getNinnum();
        ngrossnums[i] = voItems[i].getNingrossnum();
        dmo.updateItemRetNum(bids[i], nnums[i], ngrossnums[i]);
      }

      this.m_timer.stopAndShow("回写借出还回数量");
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }

      throw new BusinessException(e.getMessage());
    }
  }

  private GeneralBillItemVO[] dealStorUnit(GeneralBillItemVO[] voItems, int inoutflag)
  {
    if ((voItems == null) || (voItems.length == 0))
      return voItems;
    UFDouble ufdBillStorHsl = null;
    boolean isOut = false;
    if (inoutflag == -1) {
      isOut = true;
    }
    for (int i = 0; i < voItems.length; i++) {
      ufdBillStorHsl = getStorHsl(voItems[i]);
      if (ufdBillStorHsl != null) {
        if (isOut) {
          if (voItems[i].getNoutnum() != null)
            voItems[i].setNoutassistnum(voItems[i].getNoutnum().div(ufdBillStorHsl));
        }
        else if (voItems[i].getNinnum() != null) {
          voItems[i].setNinassistnum(voItems[i].getNinnum().div(ufdBillStorHsl));
        }

      }

    }

    return voItems;
  }

  private GeneralBillItemVO[] getCombinedItems(GeneralBillVO newVO, GeneralBillVO oldVO, String[] sGroupFields, String[] sNumFields)
    throws Exception
  {
    this.m_timer.start();
    GeneralBillItemVO[] voItems = null;
    ArrayList alvo = new ArrayList();
    if (newVO != null) {
      voItems = newVO.getItemVOs();
      for (int i = 0; i < voItems.length; i++) {
        if (voItems[i].getStatus() == 3) {
          continue;
        }
        for (int j = 0; j < sGroupFields.length; j++) {
          if (voItems[i].getAttributeValue(sGroupFields[j]) != null) {
            alvo.add(voItems[i]);
            break;
          }
        }
      }
    }

    if (oldVO != null) {
      voItems = oldVO.getItemVOs();
      for (int i = 0; i < voItems.length; i++) {
        GeneralBillItemVO voItem = new GeneralBillItemVO();
        UFDouble neg = new UFDouble(-1.0D);
        boolean isHasvalue = false;
        for (int j = 0; j < sGroupFields.length; j++) {
          if (voItems[i].getAttributeValue(sGroupFields[j]) != null) {
            voItem.setAttributeValue(sGroupFields[j], voItems[i].getAttributeValue(sGroupFields[j]));
            isHasvalue = true;
          }
        }

        if (isHasvalue) {
          alvo.add(voItem);

          for (int j = 0; j < sNumFields.length; j++) {
            if (voItems[i].getAttributeValue(sNumFields[j]) != null)
              voItem.setAttributeValue(sNumFields[j], ((UFDouble)voItems[i].getAttributeValue(sNumFields[j])).multiply(neg));
            else {
              voItem.setAttributeValue(sNumFields[j], nc.vo.ic.pub.GenMethod.ZERO);
            }
          }
        }

      }

    }

    this.m_timer.stopAndShow("getCombinedItems");
    GeneralBillItemVO[] voRes = null;

    if (alvo.size() > 0)
    {
      GeneralBillItemVO[] voItemstmp = new GeneralBillItemVO[alvo.size()];
      alvo.toArray(voItemstmp);
      this.m_timer.stopAndShow("getCombinedItems");
      DefaultVOMerger m = new DefaultVOMerger();
      m.setMergeAttrs(sGroupFields, sNumFields, null, null, null);

      voRes = (GeneralBillItemVO[])(GeneralBillItemVO[])m.mergeByGroupOnly(voItemstmp);

      this.m_timer.stopAndShow("getCombinedItems_merge");
    }

    return voRes;
  }

  private GeneralBillVO[] processNshouldoutnum(GeneralBillVO newVO, GeneralBillVO oldVO, String[] fields, String[] snumfields)
  {
    GeneralBillVO[] gvos = new GeneralBillVO[2];
    UFDouble nnum = null; UFDouble d0 = new UFDouble(0);
    if (newVO != null) {
      GeneralBillItemVO[] newitemvos = newVO.getItemVOs();
      gvos[0] = new GeneralBillVO();
      gvos[0].setParentVO(newVO.getParentVO());
      if ((newitemvos != null) && (newitemvos.length > 0)) {
        GeneralBillItemVO[] newitemvos_c = new GeneralBillItemVO[newitemvos.length];
        int i = 0; for (int loop = newitemvos_c.length; i < loop; i++) {
          newitemvos_c[i] = new GeneralBillItemVO();
          newitemvos_c[i].setStatus(newitemvos[i].getStatus());
          int k = 0; for (int loopk = fields.length; k < loopk; k++)
            newitemvos_c[i].setAttributeValue(fields[k], newitemvos[i].getAttributeValue(fields[k]));
           k = 0; for (int loopk = snumfields.length; k < loopk; k++) {
            newitemvos_c[i].setAttributeValue(snumfields[k], newitemvos[i].getAttributeValue(snumfields[k]));
          }
          nnum = newitemvos_c[i].getNoutnum();
          if (nnum != null) {
            newitemvos_c[i].setNshouldoutnum(d0);
            newitemvos_c[i].setNshouldoutassistnum(d0);
          }
        }
        gvos[0].setChildrenVO(newitemvos_c);
      }
    }

    if (oldVO != null) {
      GeneralBillItemVO[] olditemvos = oldVO.getItemVOs();
      gvos[1] = new GeneralBillVO();
      gvos[1].setParentVO(oldVO.getParentVO());
      if ((olditemvos != null) && (olditemvos.length > 0)) {
        GeneralBillItemVO[] olditemvos_c = new GeneralBillItemVO[olditemvos.length];
        int i = 0; for (int loop = olditemvos_c.length; i < loop; i++) {
          olditemvos_c[i] = new GeneralBillItemVO();
          olditemvos_c[i].setStatus(olditemvos[i].getStatus());
          int k = 0; for (int loopk = fields.length; k < loopk; k++)
            olditemvos_c[i].setAttributeValue(fields[k], olditemvos[i].getAttributeValue(fields[k]));
           k = 0; for (int loopk = snumfields.length; k < loopk; k++) {
            olditemvos_c[i].setAttributeValue(snumfields[k], olditemvos[i].getAttributeValue(snumfields[k]));
          }
          nnum = olditemvos_c[i].getNoutnum();
          if (nnum != null) {
            olditemvos_c[i].setNshouldoutnum(d0);
            olditemvos_c[i].setNshouldoutassistnum(d0);
          }
        }
        gvos[1].setChildrenVO(olditemvos_c);
      }

    }

    return gvos;
  }

  public void reWriteSaleFor33(DMDataVO[] paramvos)
    throws BusinessException
  {
    this.m_timer.start();

    if ((paramvos == null) || (paramvos.length <= 0)) {
      return;
    }
    GeneralBillItemVO[] updatevos = null;
    try
    {
      SmartDMO sdmo = new SmartDMO();

      ArrayList idlist = new ArrayList();
      String id = null;
      HashMap hsparam = new HashMap();
      int i = 0; for (int loop = paramvos.length; i < loop; i++) {
        id = (String)paramvos[i].getAttributeValue("cgeneralbid");
        if (id != null) {
          if (!idlist.contains(id))
            idlist.add(id);
          hsparam.put(id, paramvos[i]);
        }
      }

      if (idlist.size() <= 0) {
        return;
      }
      String swhere = " 1=1 " + GeneralSqlString.formInSQL("cgeneralbid", idlist);

      updatevos = (GeneralBillItemVO[])(GeneralBillItemVO[])sdmo.selectBy(GeneralBillItemVO.class, null, swhere);

      if ((updatevos == null) || (updatevos.length <= 0)) {
        return;
      }

      DMDataVO paramvo = null;
      UFDouble nvalue0 = null; UFDouble nvalue1 = null;

      GeneralBillItemVO[] updatevos_bak = (GeneralBillItemVO[])(GeneralBillItemVO[])ObjectUtils.serializableClone(updatevos);

      ArrayList arvolist = new ArrayList();
      ArrayList aroldlist = new ArrayList();
       i = 0; for (int loop = updatevos.length; i < loop; i++) {
        paramvo = (DMDataVO)hsparam.get(updatevos[i].getAttributeValue("cgeneralbid"));

        if (paramvo == null)
        {
          continue;
        }
        updatevos[i].setAttributeValue("cquoteunitid", paramvo.getAttributeValue("cquoteunitid"));

        updatevos[i].setAttributeValue("nquoteunitrate", paramvo.getAttributeValue("nquoteunitrate"));

        nvalue0 = (UFDouble)updatevos_bak[i].getAttributeValue("nquoteunitrate");

        nvalue0 = nvalue0 == null ? ICGenVO.duf0 : nvalue0;
        nvalue1 = (UFDouble)updatevos[i].getAttributeValue("nquoteunitrate");

        nvalue1 = nvalue1 == null ? ICGenVO.duf0 : nvalue1;

        if (nvalue0.compareTo(nvalue1) != 0) {
          updatevos[i].setAttributeValue("nquoteunitnum", ICGenVO.div((UFDouble)updatevos[i].getAttributeValue("noutnum"), (UFDouble)updatevos[i].getAttributeValue("nquoteunitrate")));
        }

        updatevos[i].setAttributeValue("cquotecurrency", paramvo.getAttributeValue("cquotecurrency"));

        updatevos[i].setAttributeValue("nquoteprice", paramvo.getAttributeValue("nquoteprice"));

        nvalue0 = (UFDouble)updatevos_bak[i].getAttributeValue("nquoteunitnum");

        nvalue0 = nvalue0 == null ? ICGenVO.duf0 : nvalue0;
        nvalue1 = (UFDouble)updatevos[i].getAttributeValue("nquoteunitnum");

        nvalue1 = nvalue1 == null ? ICGenVO.duf0 : nvalue1;

        boolean bnqtnume = nvalue0.compareTo(nvalue1) == 0;

        nvalue0 = (UFDouble)updatevos_bak[i].getAttributeValue("nquoteprice");

        nvalue0 = nvalue0 == null ? ICGenVO.duf0 : nvalue0;
        nvalue1 = (UFDouble)updatevos[i].getAttributeValue("nquoteprice");

        nvalue1 = nvalue1 == null ? ICGenVO.duf0 : nvalue1;

        boolean bnqtpricee = nvalue0.compareTo(nvalue1) == 0;

        if ((!bnqtnume) || (!bnqtpricee)) {
          updatevos[i].setAttributeValue("nquotemny", ICGenVO.mult((UFDouble)updatevos[i].getAttributeValue("nquoteunitnum"), (UFDouble)updatevos[i].getAttributeValue("nquoteprice")));
        }

        nvalue0 = (UFDouble)updatevos[i].getAttributeValue("ntaxprice");

        nvalue1 = (UFDouble)paramvo.getAttributeValue("ntaxprice");
        if (nvalue0 == null)
          nvalue0 = ICGenVO.duf0;
        if (nvalue1 == null)
          nvalue1 = ICGenVO.duf0;
        if (nvalue0.compareTo(nvalue1) != 0) {
          updatevos[i].setAttributeValue("ntaxprice", paramvo.getAttributeValue("ntaxprice"));

          updatevos[i].setAttributeValue("ntaxmny", ICGenVO.mult((UFDouble)updatevos[i].getAttributeValue("noutnum"), (UFDouble)updatevos[i].getAttributeValue("ntaxprice")));

          if (!arvolist.contains(updatevos[i])) {
            arvolist.add(updatevos[i]);
            aroldlist.add(updatevos_bak[i]);
          }
        }

        nvalue0 = (UFDouble)updatevos[i].getAttributeValue("nsaleprice");

        nvalue1 = (UFDouble)paramvo.getAttributeValue("nsaleprice");
        if (nvalue0 == null)
          nvalue0 = ICGenVO.duf0;
        if (nvalue1 == null)
          nvalue1 = ICGenVO.duf0;
        if (nvalue0.compareTo(nvalue1) != 0) {
          updatevos[i].setAttributeValue("nsaleprice", paramvo.getAttributeValue("nsaleprice"));

          updatevos[i].setAttributeValue("nsalemny", ICGenVO.mult((UFDouble)updatevos[i].getAttributeValue("noutnum"), (UFDouble)updatevos[i].getAttributeValue("nsaleprice")));

          if (!arvolist.contains(updatevos[i])) {
            arvolist.add(updatevos[i]);
            aroldlist.add(updatevos_bak[i]);
          }

        }

      }

      String[] updatefields = { "cquoteunitid", "nquoteunitrate", "nquoteunitnum", "cquotecurrency", "ntaxprice", "nsaleprice", "nquoteprice", "ntaxmny", "nsalemny", "nquotemny" };

      sdmo.executeUpdateBatch(updatevos, updatefields, new String[] { "cgeneralbid" });

      if (arvolist.size() > 0) {
        updateBusiAr((GeneralBillItemVO[])(GeneralBillItemVO[])aroldlist.toArray(new GeneralBillItemVO[aroldlist.size()]), (GeneralBillItemVO[])(GeneralBillItemVO[])arvolist.toArray(new GeneralBillItemVO[arvolist.size()]));
      }

    }
    catch (Exception e)
    {
      throw GenMethod.handleException(null, e);
    }

    this.m_timer.stopAndShow("回写销售结算");
  }

  public void updateBusiAr(GeneralBillItemVO[] olditemvos, GeneralBillItemVO[] curitemvos)
    throws BusinessException
  {
    if ((curitemvos == null) || (curitemvos.length <= 0) || (olditemvos == null) || (olditemvos.length <= 0))
    {
      return;
    }

    try
    {
      SmartDMO sdmo = new SmartDMO();
      GeneralBillHeaderVO[] headvos = (GeneralBillHeaderVO[])(GeneralBillHeaderVO[])sdmo.selectBy(GeneralBillHeaderVO.class, null, " 1=1 " + GeneralSqlString.formInSQL("cgeneralhid", ICGenVO.getVOsOnlyValues(curitemvos, "cgeneralhid")));

      if ((headvos == null) || (headvos.length <= 0)) {
        return;
      }

      GeneralBillVO[] curbillvos = (GeneralBillVO[])(GeneralBillVO[])ICGenVO.mergToAggVO(GeneralBillVO.class, headvos, curitemvos, "cgeneralhid");

      GeneralBillVO[] oldbillvos = (GeneralBillVO[])(GeneralBillVO[])ICGenVO.mergToAggVO(GeneralBillVO.class, headvos, olditemvos, "cgeneralhid");

      IBillInvokeCreditManager invoker = (IBillInvokeCreditManager)NCLocator.getInstance().lookup(IBillInvokeCreditManager.class.getName());

      BillCreditOriginVO creditvo = null;
      int i = 0; for (int loop = curbillvos.length; i < loop; i++) {
        creditvo = new BillCreditOriginVO();
        creditvo.m_iBillType = 1;
        creditvo.m_iBillAct = 15;
        creditvo.m_voBill = curbillvos[i].getParentVO();
        creditvo.m_voBill_b = curbillvos[i].getChildrenVO();
        creditvo.m_voBill_init = oldbillvos[i].getParentVO();
        creditvo.m_voBill_init_b = oldbillvos[i].getChildrenVO();

        invoker.renovateAR(creditvo);
      }
    }
    catch (Exception e) {
      throw GenMethod.handleException(null, e);
    }
  }

  private void getSrcBillInfo(ArrayList ids, String srcbilltype, HashMap hssrcinfo)
    throws BusinessException
  {
    if ((ids == null) || (ids.size() <= 0))
      return;
    if (srcbilltype == null)
      return;
    if (hssrcinfo == null)
      return;
    String sql_src = null;
    String sql_ic = null;
    try
    {
      if ("30".equals(srcbilltype)) {
        sql_src = "select corder_bid,nnumber,nsummny,nmny,noriginalcursummny from so_saleorder_b where dr = 0 " + GeneralSqlString.formInSQL("corder_bid", ids);
        sql_ic = "select cfirstbillbid,sum(noutnum),sum(ntaxmny),sum(nsalemny),sum(nquotemny) from ic_general_b where dr = 0 and csourcetype<>'32' and cfirsttype='30' " + GeneralSqlString.formInSQL("cfirstbillbid", ids) + " group by cfirstbillbid ";
      } else if ("32".equals(srcbilltype)) {
        sql_src = "select cinvoice_bid,nnumber,nsummny,nmny,noriginalcursummny from so_saleinvoice_b where dr = 0 " + GeneralSqlString.formInSQL("cinvoice_bid", ids);
        sql_ic = "select csourcebillbid,sum(noutnum),sum(ntaxmny),sum(nsalemny),sum(nquotemny) from ic_general_b where dr = 0 and csourcetype='32' " + GeneralSqlString.formInSQL("csourcebillbid", ids) + " group by csourcebillbid ";
      } else if ("3U".equals(srcbilltype)) {
        sql_src = "select pk_apply_b,nnumber,nsummny,nmny,noriginalcursummny from so_apply_b where dr = 0 " + GeneralSqlString.formInSQL("pk_apply_b", ids);
        sql_ic = "select cfirstbillbid,sum(noutnum),sum(ntaxmny),sum(nsalemny),sum(nquotemny) from ic_general_b where dr = 0 and csourcetype<>'32' and cfirsttype='3U' " + GeneralSqlString.formInSQL("cfirstbillbid", ids) + " group by cfirstbillbid ";
      }

      if (sql_src == null) {
        return;
      }
      SmartDMO sdmo = new SmartDMO();
      Object[] srcinfo = null;
      UFDouble[] drowinfo = null;
      Object[] orowinfo = null;

      srcinfo = sdmo.selectBy2(sql_src);
      if ((srcinfo != null) && (srcinfo.length > 0)) {
        int i = 0; for (int loop = srcinfo.length; i < loop; i++) {
          orowinfo = (Object[])(Object[])srcinfo[i];
          if (orowinfo[0] == null)
            continue;
          drowinfo = new UFDouble[8];
          if (orowinfo[0] == null)
            continue;
          if (orowinfo[1] != null) {
            if (orowinfo[1].getClass() == UFDouble.class)
              drowinfo[0] = ((UFDouble)orowinfo[1]);
            else
              drowinfo[0] = new UFDouble(orowinfo[1].toString());
          }
          if (orowinfo[2] != null) {
            if (orowinfo[2].getClass() == UFDouble.class)
              drowinfo[2] = ((UFDouble)orowinfo[2]);
            else
              drowinfo[2] = new UFDouble(orowinfo[2].toString());
          }
          if (orowinfo[3] != null) {
            if (orowinfo[3].getClass() == UFDouble.class)
              drowinfo[3] = ((UFDouble)orowinfo[3]);
            else
              drowinfo[3] = new UFDouble(orowinfo[3].toString());
          }
          if (orowinfo[4] != null) {
            if (orowinfo[4].getClass() == UFDouble.class)
              drowinfo[4] = ((UFDouble)orowinfo[4]);
            else
              drowinfo[4] = new UFDouble(orowinfo[4].toString());
          }
          hssrcinfo.put(orowinfo[0].toString(), drowinfo);
        }
      }

      srcinfo = sdmo.selectBy2(sql_ic);

      if ((srcinfo != null) && (srcinfo.length > 0)) {
        int i = 0; for (int loop = srcinfo.length; i < loop; i++) {
          orowinfo = (Object[])(Object[])srcinfo[i];
          if (orowinfo[0] == null) {
            continue;
          }
          drowinfo = (UFDouble[])(UFDouble[])hssrcinfo.get(orowinfo[0].toString());
          if (drowinfo == null) {
            continue;
          }
          if (orowinfo[1] != null) {
            if (orowinfo[1].getClass() == UFDouble.class)
              drowinfo[1] = ((UFDouble)orowinfo[1]);
            else
              drowinfo[1] = new UFDouble(orowinfo[1].toString());
          }
          if (orowinfo[2] != null) {
            if (orowinfo[2].getClass() == UFDouble.class)
              drowinfo[5] = ((UFDouble)orowinfo[2]);
            else
              drowinfo[5] = new UFDouble(orowinfo[2].toString());
          }
          if (orowinfo[3] != null) {
            if (orowinfo[3].getClass() == UFDouble.class)
              drowinfo[6] = ((UFDouble)orowinfo[3]);
            else
              drowinfo[6] = new UFDouble(orowinfo[3].toString());
          }
          if (orowinfo[4] != null)
            if (orowinfo[4].getClass() == UFDouble.class)
              drowinfo[7] = ((UFDouble)orowinfo[3]);
            else
              drowinfo[7] = new UFDouble(orowinfo[3].toString());
        }
      }
    }
    catch (Exception e) {
      throw GenMethod.handleException(null, e);
    }
  }

  private HashMap getSrcBillInfo(GeneralBillItemVO[] itemvos, boolean issubcur)
    throws BusinessException
  {
    if ((itemvos == null) || (itemvos.length <= 0))
      return null;
    HashMap retmap = null;
    ArrayList ordlist = new ArrayList();
    ArrayList invlist = new ArrayList();
    ArrayList retlist = new ArrayList();
    int i = 0; for (int loop = itemvos.length; i < loop; i++)
    {
      if ("32".equals(itemvos[i].getCsourcetype())) {
        if (!invlist.contains(itemvos[i].getCsourcebillbid()))
          invlist.add(itemvos[i].getCsourcebillbid());
      }
      else if ("30".equals(itemvos[i].getCfirsttype())) {
        if (!ordlist.contains(itemvos[i].getCfirstbillbid()))
          ordlist.add(itemvos[i].getCfirstbillbid());
      } else {
        if ((!"3U".equals(itemvos[i].getCfirsttype())) || 
          (retlist.contains(itemvos[i].getCfirstbillbid()))) continue;
        retlist.add(itemvos[i].getCfirstbillbid());
      }
    }

    retmap = new HashMap();
    try
    {
      if (invlist.size() > 0) {
        getSrcBillInfo(invlist, "32", retmap);
      }

      if (ordlist.size() > 0) {
        getSrcBillInfo(ordlist, "30", retmap);
      }

      if (retlist.size() > 0) {
        getSrcBillInfo(retlist, "3U", retmap);
      }

      if (!issubcur) {
        return retmap;
      }
      String csourcetype = null;
      String srcbillrowid = null;
      UFDouble[] arrrowinfo = null;

      UFDouble d0 = new UFDouble(0);

      UFDouble noutnum = null; UFDouble ntaxmny = null; UFDouble nmny = null; UFDouble nquotemny = null;

       i = 0; for (int loop = itemvos.length; i < loop; i++)
      {
        if ("32".equals(itemvos[i].getCsourcebillbid()))
          srcbillrowid = itemvos[i].getCsourcebillbid();
        else {
          srcbillrowid = itemvos[i].getCfirstbillbid();
        }

        arrrowinfo = (UFDouble[])(UFDouble[])retmap.get(srcbillrowid);
        if (arrrowinfo == null)
        {
          continue;
        }
        if (arrrowinfo[1] == null) {
          arrrowinfo[1] = d0;
        }
        noutnum = itemvos[i].getNoutnum();
        if (noutnum != null) {
          arrrowinfo[1] = arrrowinfo[1].sub(noutnum);
        }

        if (arrrowinfo[5] == null) {
          arrrowinfo[5] = d0;
        }
        ntaxmny = itemvos[i].getNtaxmny();
        if (ntaxmny != null) {
          arrrowinfo[5] = arrrowinfo[5].sub(ntaxmny);
        }
        if (arrrowinfo[6] == null) {
          arrrowinfo[6] = d0;
        }
        nmny = itemvos[i].getNsalemny();
        if (nmny != null) {
          arrrowinfo[6] = arrrowinfo[6].sub(nmny);
        }
        if (arrrowinfo[7] == null) {
          arrrowinfo[7] = d0;
        }
        nquotemny = (UFDouble)itemvos[i].getAttributeValue("nquotemny");
        if (nquotemny != null)
          arrrowinfo[7] = arrrowinfo[7].sub(nquotemny);
      }
    }
    catch (Exception e) {
      throw GenMethod.handleException(null, e);
    }

    return retmap;
  }

  public void processOutBillMny(GeneralBillItemVO[] itemvos, String[] fieldnames, boolean issubcur)
    throws BusinessException
  {
    if ((fieldnames == null) || (fieldnames.length <= 0) || (itemvos == null) || (itemvos.length <= 0)) {
      return;
    }
    try
    {
      HashSet hsfield = new HashSet(10);
      if (fieldnames == null)
        fieldnames = new String[] { "ntaxmny", "nsalemny", "nquotemny" };
      int i = 0; for (int loop = fieldnames.length; i < loop; i++) {
        hsfield.add(fieldnames[i]);
      }

      HashMap hssrcinfo = getSrcBillInfo(itemvos, issubcur);
      if ((hssrcinfo == null) || (hssrcinfo.size() <= 0)) {
        return;
      }
      UFDouble ntotaloutnum = null;
      UFDouble noutnum = null;
      String csourcetype = null;
      String srcbillrowid = null;
      UFDouble[] arrrowinfo = null;

      UFDouble d0 = new UFDouble(0);
      UFDouble dtemp = null; UFDouble dtemp1 = null;
      ArrayList updatevolist = new ArrayList();

      int itemp = 0;

       i = 0; for (int loop = itemvos.length; i < loop; i++)
      {
        noutnum = itemvos[i].getNoutnum();
        if (noutnum == null) {
          noutnum = d0;
        }
        if ("32".equals(itemvos[i].getCsourcebillbid()))
          srcbillrowid = itemvos[i].getCsourcebillbid();
        else {
          srcbillrowid = itemvos[i].getCfirstbillbid();
        }

        arrrowinfo = (UFDouble[])(UFDouble[])hssrcinfo.get(srcbillrowid);
        if (arrrowinfo == null) {
          continue;
        }
        if (arrrowinfo[0] == null) {
          continue;
        }
        ntotaloutnum = arrrowinfo[1];
        if (ntotaloutnum == null) {
          ntotaloutnum = d0;
        }
        itemp = ntotaloutnum.compareTo(arrrowinfo[0]);

        if (itemp == 0)
        {
          if (hsfield.contains("ntaxmny")) {
            arrrowinfo[2] = (arrrowinfo[2] == null ? d0 : arrrowinfo[2]);
            arrrowinfo[5] = (arrrowinfo[5] == null ? d0 : arrrowinfo[5]);

            dtemp = arrrowinfo[2].abs().sub(arrrowinfo[5].abs());
            if (dtemp.compareTo(d0) > 0) {
              itemvos[i].setNtaxmny(arrrowinfo[2].sub(arrrowinfo[5]));
            }
            arrrowinfo[5] = arrrowinfo[2];

            if (!updatevolist.contains(itemvos[i])) {
              updatevolist.add(itemvos[i]);
            }
          }
          if (hsfield.contains("nsalemny")) {
            arrrowinfo[3] = (arrrowinfo[3] == null ? d0 : arrrowinfo[3]);
            arrrowinfo[6] = (arrrowinfo[6] == null ? d0 : arrrowinfo[6]);

            dtemp = arrrowinfo[3].abs().sub(arrrowinfo[6].abs());
            if (dtemp.compareTo(d0) > 0) {
              itemvos[i].setNsalemny(arrrowinfo[3].sub(arrrowinfo[6]));
            }
            arrrowinfo[6] = arrrowinfo[3];

            if (!updatevolist.contains(itemvos[i])) {
              updatevolist.add(itemvos[i]);
            }
          }

          if (hsfield.contains("nquotemny")) {
            arrrowinfo[4] = (arrrowinfo[4] == null ? d0 : arrrowinfo[4]);
            arrrowinfo[7] = (arrrowinfo[7] == null ? d0 : arrrowinfo[7]);

            dtemp = arrrowinfo[4].abs().sub(arrrowinfo[7].abs());
            if (dtemp.compareTo(d0) > 0) {
              itemvos[i].setAttributeValue("nquotemny", arrrowinfo[4].sub(arrrowinfo[7]));
            }
            arrrowinfo[7] = arrrowinfo[4];

            if (!updatevolist.contains(itemvos[i]))
              updatevolist.add(itemvos[i]);
          }
        }
        else {
          dtemp = itemvos[i].getNtaxmny();
          if (dtemp == null)
            dtemp = d0;
          arrrowinfo[5] = (arrrowinfo[5] == null ? d0 : arrrowinfo[5]);
          arrrowinfo[5] = arrrowinfo[5].add(dtemp);

          dtemp = itemvos[i].getNsalemny();
          if (dtemp == null)
            dtemp = d0;
          arrrowinfo[6] = (arrrowinfo[6] == null ? d0 : arrrowinfo[6]);
          arrrowinfo[6] = arrrowinfo[6].add(dtemp);

          dtemp = (UFDouble)itemvos[i].getAttributeValue("nquotemny");
          if (dtemp == null)
            dtemp = d0;
          arrrowinfo[7] = (arrrowinfo[7] == null ? d0 : arrrowinfo[7]);
          arrrowinfo[7] = arrrowinfo[7].add(dtemp);
        }

        if (updatevolist.size() > 0) {
          itemvos = (GeneralBillItemVO[])(GeneralBillItemVO[])updatevolist.toArray(new GeneralBillItemVO[updatevolist.size()]);

          SmartDMO sdmo = new SmartDMO();

          sdmo.executeUpdateBatch(itemvos, new String[] { "ntaxmny", "nsalemny", "nquotemny" }, new String[] { "cgeneralbid" });
        }
      }
    }
    catch (Exception e)
    {
      throw GenMethod.handleException(null, e);
    }
  }

  public void reWriteMROutNum(GeneralBillVO newVO, GeneralBillVO oldVO)
    throws BusinessException
  {
    this.m_timer.start();

    GeneralBillVO vot = null;
    if (newVO != null) {
      vot = newVO;
    }
    else {
      vot = oldVO;
    }

    GeneralBillItemVO[] voItems = getCombinedItems(newVO, oldVO);
    if ((voItems == null) || (voItems.length < 1))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000145"));
    try
    {
      int iItemCount = voItems.length;

      String sCorpID = null;
      if (newVO != null)
        sCorpID = newVO.getHeaderVO().getPk_corp();
      else
        sCorpID = oldVO.getHeaderVO().getPk_corp();
      if (sCorpID == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008other", "UPP4008other-000146"));
      }
      if (!isModuleStarted(sCorpID, "PO")) {
        return;
      }

      String csourcetype = vot.getItemVOs()[0].getCsourcetype();
      if ((csourcetype == null) || (!csourcetype.equals("422X"))) {
        return;
      }

      UFDouble dNum = null;

      String sSourbillbid = null;
      Hashtable htNum = new Hashtable();
      String sKey = null;
      UFDouble dTmp = null;
      UFDouble ZERO = new UFDouble(0);

      for (int i = 0; i < iItemCount; i++) {
        sSourbillbid = voItems[i].getCsourcebillbid();
        if (sSourbillbid == null) {
          SCMEnv.out("---- rewrite -----> no source info");
        }
        else {
          dNum = ZERO;
          if (voItems[i].getNoutnum() != null) {
            dNum = voItems[i].getNoutnum();
          }
          sKey = sSourbillbid;
          if (htNum.containsKey(sKey)) {
            dTmp = (UFDouble)htNum.get(sKey);
            dTmp = dTmp.add(dNum);
            htNum.put(sKey, dTmp);
          } else {
            dTmp = new UFDouble();
            dTmp = dNum;
            htNum.put(sKey, dTmp);
          }
        }
      }
      if ((htNum == null) || (htNum.size() == 0)) {
        return;
      }
      Object[] oKeys = htNum.keySet().toArray();
      String[] sKeys = new String[htNum.size()];
      UFDouble[] dNums = new UFDouble[htNum.size()];
      for (int i = 0; i < oKeys.length; i++) {
        sKeys[i] = oKeys[i].toString();
        dNums[i] = ((UFDouble)htNum.get(oKeys[i]));
      }

      IMRWriteDownBillInfo inter = (IMRWriteDownBillInfo)NCLocator.getInstance().lookup(IMRWriteDownBillInfo.class.getName());
      inter.rewriteOutNum(sCorpID, sKeys, dNums);

      this.m_timer.stopAndShow("回写物资需求申请");
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
  }
}