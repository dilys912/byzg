package nc.bs.so.so120;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.itf.uap.pf.IPFMetaModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.PfUtilActionConstrictVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.bill.CreditConst;
import nc.vo.scm.pub.bill.IExamAVO;
import nc.vo.so.credit.AccountMnyVO;
import nc.vo.so.credit.BillCreditOriginVO;
import nc.vo.so.credit.CreditUtil;
import nc.vo.so.credit.CuCreditVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so120.BillCreditVO;
import nc.vo.so.so120.CreditNotEnoughException;
import nc.vo.so.so120.CreditVO;
import nc.vo.so.so120.ExamResultVO;
import nc.vo.so.so120.LimitTypeCVO;
import nc.vo.so.so120.PeriodNotEnoughException;

public class BillInvokeCreditManager
  implements CreditConst, IBillInvokeCreditManager
{
  private BillCreditOriginVO m_voBillCreditOrigin = null;

  private BillCreditVO[][] m_voBillCredit = (BillCreditVO[][])null;

  private AccountMnyVO[] m_voAccountMny = null;

  private CreditVO m_voCredit = null;

  private UFBoolean m_bFreeofcremnycheck = null;

  private UFBoolean m_bFreeofacclmtcheck = null;

  private UFDouble exam(IExamAVO vo, boolean bCredit, String sMethod, String sBillName)
    throws Exception
  {
    String sAction = vo.getActionCode();
    SCMEnv.out("@@@@@@@@@@@@@@@@@@: " + sAction);

    if (bCredit) {
      if (!vo.isCheckCredit())
        return new UFDouble(-10000000);
    }
    else if (!vo.isCheckPeriod()) {
      return new UFDouble(-10000000);
    }

    BillCreditOriginVO voOrigin = new BillCreditOriginVO();
    voOrigin.m_iBillType = vo.getBillTypeInt();
    voOrigin.m_iBillAct = vo.getActionInt();
    if (voOrigin.m_iBillAct == 1) {
      AggregatedValueObject voModified = vo.getModifiedVO();
      voOrigin.m_voBill = voModified.getParentVO();
      voOrigin.m_voBill_b = voModified.getChildrenVO();
      AggregatedValueObject voOld = vo.getOldVO();
      voOrigin.m_voBill_init = voOld.getParentVO();
      voOrigin.m_voBill_init_b = voOld.getChildrenVO();
    } else {
      voOrigin.m_voBill = vo.getParentVO();
      voOrigin.m_voBill_b = vo.getChildrenVO();
    }

    ExamResultVO voExamResult = null;
    if (bCredit) {
      if (vo.getOperatorid() == null) {
        vo.setLockOperatorid(new UFDouble(System.currentTimeMillis()).toString());
      }

      voExamResult = examOverCredit(voOrigin, vo.getLoginDate(), sAction);
    }
    else {
      voExamResult = examOverPeriod(voOrigin, vo.getLoginDate());
      if ((vo != null) && (vo.getLockKey() != null) && (voExamResult != null)) {
        voExamResult.m_sLockKey = vo.getLockKey();
      }

    }

    if ((voExamResult == null) || (voExamResult.m_dOverMny == null) || (voExamResult.m_dOverMny.doubleValue() <= 0.0D))
    {
      if (voExamResult != null) {
        vo.setLockKey(voExamResult.m_sLockKey);
      }
      return new UFDouble(-10000000);
    }

    String beanName = IPFMetaModel.class.getName();
    IPFMetaModel bo = (IPFMetaModel)NCLocator.getInstance().lookup(beanName);

    PfUtilActionConstrictVO[] voPfAll = bo.queryActionConstricts(vo.getBillTypeCode(), vo.getBizTypeid(), vo.getActionCode(), vo.getPk_corp(), vo.getOperatorid());

    ArrayList al = new ArrayList();
    if (voPfAll != null) {
      for (int i = 0; i < voPfAll.length; i++) {
        if (("nc.impl.scm.so.pub.CreditControlDMO".equals(voPfAll[i].getFunClassName())) || (("nc.bs.ic.ic211.GeneralHDMO".equals(voPfAll[i].getFunClassName())) && (sMethod.equals(voPfAll[i].getMethod()))))
        {
          al.add(voPfAll[i]);
        }
      }
    }
    PfUtilActionConstrictVO voPf = null;
    for (int i = 0; i < al.size(); i++) {
      voPf = (PfUtilActionConstrictVO)al.get(i);
      String ysf = voPf.getYsf();
      if ((ysf != null) && ((ysf.equals("<=")) || (ysf.equals("<")))) {
        double dValue = 0.0D;
        try {
          dValue = Double.parseDouble(voPf.getValue());
        } catch (Exception err) {
          if (bCredit) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000086"));
          }

          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000087"));
        }

        if (((ysf.equals("<=")) && (voExamResult.m_dOverMny.doubleValue() > dValue)) || ((ysf.equals("<")) && (voExamResult.m_dOverMny.doubleValue() >= dValue)))
        {
          int iBillType = 0;

          if (voExamResult.m_iFlag == 1) {
            if (bCredit)
            {
              if ((iBillType != 0) && (iBillType == 1));
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000088", null, new String[] { sBillName, String.valueOf(voExamResult.m_dOverMny) }));
            }

            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000089", null, new String[] { sBillName, String.valueOf(voExamResult.m_dOverMny) }));
          }

          if (bCredit) {
            throw new CreditNotEnoughException(sBillName + NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000104", null, new String[] { String.valueOf(voExamResult.m_dOverMny) }));
          }

          throw new PeriodNotEnoughException(sBillName + NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000105", null, new String[] { String.valueOf(voExamResult.m_dOverMny) }));
        }

      }

    }

    return voExamResult.m_dOverMny;
  }

  public UFDouble examOverCreditGeneral(AggregatedValueObject voGeneral)
    throws Exception
  {
    if ((!(voGeneral instanceof IExamAVO)) || (voGeneral == null)) {
      throw new Exception(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000090"));
    }

    return exam((IExamAVO)voGeneral, true, "examOverCreditGeneral", NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000091"));
  }

  public UFDouble examOverCreditOrder(AggregatedValueObject voOrder)
    throws Exception
  {
    if ((!(voOrder instanceof IExamAVO)) || (voOrder == null)) {
      throw new Exception(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000090"));
    }

    return exam((IExamAVO)voOrder, true, "examOverCreditOrder", NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000092"));
  }

  public ExamResultVO examOverPeriod(BillCreditOriginVO voOrigin, UFDate date)
    throws Exception
  {
    try
    {
      SCMEnv.out("调用客户信用（so120）账期检查开始！");
      printInitString(voOrigin);
      long l1 = System.currentTimeMillis();

      resetOriginVO(voOrigin);

      if (this.m_bFreeofacclmtcheck == null) {
        putCumanCheckFlag();
      }
      if (this.m_bFreeofacclmtcheck.booleanValue())
      {
        return null;
      }

      if (this.m_voBillCredit == null) {
        BillCreditConvertor billConvertor = new BillCreditConvertor();
        this.m_voBillCredit = billConvertor.billConvert(voOrigin);
      }

      boolean bHasCredit = true;
      if ((this.m_voBillCredit == null) || (this.m_voBillCredit.length == 0) || (this.m_voBillCredit[0] == null) || (this.m_voBillCredit[0].length == 0))
      {
        bHasCredit = false;
      }

      MatchCredit match = new MatchCredit();
      DataManager dmo = new DataManager();
      LimitTypeCVO voLimitType = null;
      String pk_corp = null;
      String pk_cumandoc = null;

      String productid = null;
      String biztypeid = null;
      if (bHasCredit) {
        pk_corp = this.m_voBillCredit[0][0].m_pk_corp;
        pk_cumandoc = this.m_voBillCredit[0][0].m_pk_cumandoc;

        if (CreditUtil.isProductline(pk_corp))
          productid = this.m_voBillCredit[0][0].m_cproductid;
        else
          productid = "#123456789*123456789";
        biztypeid = this.m_voBillCredit[0][0].m_cbiztypeid;
      }
      else if ((voOrigin.m_voBill instanceof SaleorderHVO)) {
        pk_corp = ((SaleorderHVO)voOrigin.m_voBill).m_pk_corp;
        pk_cumandoc = ((SaleorderHVO)voOrigin.m_voBill).m_ccustomerid;
        biztypeid = ((SaleorderHVO)voOrigin.m_voBill).m_cbiztype;
        if (CreditUtil.isProductline(pk_corp))
          productid = ((SaleorderBVO[])(SaleorderBVO[])voOrigin.m_voBill_b)[0].cprolineid;
        else
          productid = "#123456789*123456789";
      }
      else {
        pk_corp = (String)voOrigin.m_voBill.getAttributeValue("pk_corp");

        pk_cumandoc = (String)voOrigin.m_voBill.getAttributeValue("ccustomerid");

        biztypeid = (String)voOrigin.m_voBill.getAttributeValue("cbiztype");

        if (CreditUtil.isProductline(pk_corp)) {
          productid = (String)voOrigin.m_voBill_b[0].getAttributeValue("cinventoryid");

          productid = dmo.getProductidByInvman(new String[] { productid })[0];
        }
        else {
          productid = "#123456789*123456789";
        }
      }

      if (productid == null)
        productid = "#123456789*123456789";
      voLimitType = match.getMatchLimitType(pk_corp, productid, biztypeid);

      ExamResultVO voRE = new ExamResultVO();
      if (voLimitType == null)
      {
        UFBoolean bExamNoPatch = dmo.isExamNoPatch();
        if (bExamNoPatch.booleanValue())
        {
          UFDouble[] dPmny = dmo.getPeriodFromArap(pk_corp, pk_cumandoc, productid, biztypeid, date, voLimitType);

          if ((dPmny != null) && (dPmny.length > 2) && (dPmny[2] != null))
            voRE.m_dOverMny = dPmny[2];
          voRE.m_dOverMny = voRE.m_dOverMny.sub(dmo.getPeriodNoType(pk_cumandoc, productid, biztypeid));

          voRE.m_iFlag = 1;
        } else {
          return null;
        }
      } else {
        if ((voLimitType.m_faccountflag != null) && (voLimitType.m_faccountflag.intValue() == 2))
        {
          return null;
        }
        UFDouble[] dPmny = dmo.getPeriodFromArap(pk_corp, pk_cumandoc, productid, biztypeid, date, voLimitType);

        if ((dPmny != null) && (dPmny.length > 2) && (dPmny[2] != null))
          voRE.m_dOverMny = dPmny[2];
        voRE.m_dOverMny = voRE.m_dOverMny.sub(dmo.getPeriodHasType(pk_cumandoc, voLimitType.m_climittypecid));

        voRE.m_iFlag = voLimitType.m_faccountflag.intValue();
      }

      voRE.m_dOverMny = dmo.changeDataByCurScale(pk_corp, new UFDouble[] { voRE.m_dOverMny })[0];

      long l2 = System.currentTimeMillis();
      long l12 = l2 - l1;
      SCMEnv.out("调用客户信用（so120）账期检查结束，调用时间消耗（毫秒）：" + l12);

      return voRE;
    }
    catch (Exception err) {
      SCMEnv.out(err.getMessage());
      throw err;
    }
  }

  public UFDouble examOverPeriodGeneral(AggregatedValueObject voOrder)
    throws Exception
  {
    if ((!(voOrder instanceof IExamAVO)) || (voOrder == null)) {
      throw new Exception(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000090"));
    }

    return exam((IExamAVO)voOrder, false, "examOverPeriodGeneral", NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000091"));
  }

  public UFDouble examOverPeriodOrder(AggregatedValueObject voOrder)
    throws Exception
  {
    if ((!(voOrder instanceof IExamAVO)) || (voOrder == null)) {
      throw new Exception(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000090"));
    }

    return exam((IExamAVO)voOrder, false, "examOverPeriodOrder", NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000092"));
  }

  private void printInitString(BillCreditOriginVO voOrigin)
  {
    System.out.print("调用单据：");
    if (voOrigin.m_iBillType == 0)
      SCMEnv.out("销售订单");
    else if (voOrigin.m_iBillType == 1)
      SCMEnv.out("销售出库单");
    else if (voOrigin.m_iBillType == 5)
      SCMEnv.out("冲应收单");
    else {
      SCMEnv.out("应收应付单据：" + voOrigin.m_iBillType);
    }
    System.out.print("调用动作：");
    if (voOrigin.m_iBillAct == 0)
      SCMEnv.out("增加");
    else if (voOrigin.m_iBillAct == 2)
      SCMEnv.out("删除");
    else if (voOrigin.m_iBillAct == 1)
      SCMEnv.out("修改");
    else
      SCMEnv.out(voOrigin.m_iBillAct);
  }

  private void putCumanCheckFlag()
    throws Exception
  {
    String sCumandoc = null;
    Object objTemp = null;

    objTemp = this.m_voBillCreditOrigin.m_voBill.getAttributeValue("ccustomerid");

    sCumandoc = objTemp == null ? null : objTemp.toString().trim();

    if (sCumandoc == null) {
      this.m_bFreeofcremnycheck = new UFBoolean(false);
      this.m_bFreeofacclmtcheck = new UFBoolean(false);
    }
    else {
      DataManager dmo = new DataManager();
      UFBoolean[] bFlag = dmo.getCumanCheckFlag(sCumandoc);
      if (bFlag == null) {
        this.m_bFreeofcremnycheck = new UFBoolean(false);
        this.m_bFreeofacclmtcheck = new UFBoolean(false);
      } else {
        this.m_bFreeofcremnycheck = bFlag[0];
        this.m_bFreeofacclmtcheck = bFlag[1];
      }
    }
  }

  public int renovateAR(BillCreditOriginVO[] voOrigin)
    throws BusinessException
  {
    if ((voOrigin == null) || (voOrigin.length == 0))
      return 0;
    try
    {
      SCMEnv.out("批量调用客户信用（so120）更新应收开始！");
      printInitString(voOrigin[0]);
      long l1 = System.currentTimeMillis();

      BillCreditConvertor billConvertor = new BillCreditConvertor();
      RenovateAR renovateAR = new RenovateAR();
      BillCreditVO[][] voBillCredit = (BillCreditVO[][])null;
      AccountMnyVO[] voAccountMny = null;
      Vector v = new Vector();
      for (int i = 0; i < voOrigin.length; i++)
      {
        voBillCredit = billConvertor.billConvert(voOrigin[i]);

        voAccountMny = RenovateAR.getAccountMny(voBillCredit);
        if (voAccountMny != null) {
          for (int j = 0; j < voAccountMny.length; j++) {
            v.add(voAccountMny[j]);
          }
        }
      }

      AccountMnyVO[] voAccountMnyAll = new AccountMnyVO[v.size()];
      v.copyInto(voAccountMnyAll);

      voAccountMnyAll = RenovateAR.unitAccountMny(voAccountMnyAll);

      if ((voAccountMnyAll != null) && (voAccountMnyAll.length > 0))
        CreditUtil.acquireDynamicLock("SOCREDIT" + voAccountMnyAll[0].m_pk_corp + "$@$SHARED_LOCK$@$");
      renovateAR.saveAccountMny(voAccountMnyAll);

      long l2 = System.currentTimeMillis();
      long l12 = l2 - l1;
      SCMEnv.out("批量调用客户信用（so120）更新应收结束，调用时间消耗（毫秒）：" + l12);
    } catch (Exception err) {
      SCMEnv.out(err.getMessage());
      throw new BusinessException(err.getMessage());
    }
    return 0;
  }

  public AccountMnyVO[] getAccountMnyVO(BillCreditOriginVO[] voOrigin)
    throws BusinessException
  {
    if ((voOrigin == null) || (voOrigin.length == 0))
      return null;
    try
    {
      SCMEnv.out("批量调用客户信用（so120）更新应收开始！");
      printInitString(voOrigin[0]);
      BillCreditConvertor billConvertor = new BillCreditConvertor();

      BillCreditVO[][] voBillCredit = (BillCreditVO[][])null;
      AccountMnyVO[] voAccountMny = null;
      Vector v = new Vector();
      for (int i = 0; i < voOrigin.length; i++)
      {
        voBillCredit = billConvertor.billConvert(voOrigin[i]);

        voAccountMny = RenovateAR.getAccountMny(voBillCredit);
        if (voAccountMny != null) {
          for (int j = 0; j < voAccountMny.length; j++) {
            v.add(voAccountMny[j]);
          }
        }
      }

      AccountMnyVO[] voAccountMnyAll = new AccountMnyVO[v.size()];
      v.copyInto(voAccountMnyAll);

      return RenovateAR.unitAccountMny(voAccountMnyAll);
    }
    catch (Exception err)
    {
      SCMEnv.out(err.getMessage());
      throw new BusinessException(err.getMessage());
    }
  }

  public int saveAccountMnyVO(AccountMnyVO[] vos) throws BusinessException {
    if (vos == null) {
      return 0;
    }
    try
    {
      RenovateAR dmo = new RenovateAR();
      dmo.saveAccountMny(vos);
    }
    catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessException(e.getMessage());
    }

    return -1;
  }

  public int renovateAR(BillCreditOriginVO voOrigin)
    throws Exception
  {
    try
    {
      SCMEnv.out("调用客户信用（so120）更新应收开始！");
      printInitString(voOrigin);

      long l1 = System.currentTimeMillis();
      resetOriginVO(voOrigin);

      BillCreditConvertor billConvertor = new BillCreditConvertor();
      this.m_voBillCredit = billConvertor.billConvert(voOrigin);

      RenovateAR renovateAR = new RenovateAR();
      //协商去掉这段校验 by 沈瑞春 2018年3月20日16:00:09
      //this.m_voAccountMny = RenovateAR.getAccountMny(this.m_voBillCredit);

      if ((this.m_voAccountMny != null) && (this.m_voAccountMny.length > 0))
      {
        CreditUtil.acquireDynamicLock("SOCREDIT" + this.m_voAccountMny[0].m_pk_corp + "$@$SHARED_LOCK$@$");
        renovateAR.saveAccountMny(this.m_voAccountMny);
      }

      long l2 = System.currentTimeMillis();
      long l12 = l2 - l1;
      SCMEnv.out("调用客户信用（so120）更新应收结束，调用时间消耗（毫秒）：" + l12);
    } catch (Exception err) {
      SCMEnv.out(err.getMessage());
      throw err;
    }
    return 0;
  }

  public AccountMnyVO[] getAccountMnyVO(BillCreditOriginVO voOrigin)
    throws Exception
  {
    try
    {
      SCMEnv.out("调用客户信用（so120）更新应收开始！");
      printInitString(voOrigin);
      resetOriginVO(voOrigin);

      if (this.m_voBillCredit == null) {
        BillCreditConvertor billConvertor = new BillCreditConvertor();
        this.m_voBillCredit = billConvertor.billConvert(voOrigin);
      }

      return RenovateAR.getAccountMny(this.m_voBillCredit);
    }
    catch (Exception err) {
      SCMEnv.out(err.getMessage());
      throw err;
    }
  }

  public void unlockExamAR(String sLockKey, String optid)
  {
  }

  private void resetOriginVO(BillCreditOriginVO voOrigin)
  {
    if ((this.m_voBillCreditOrigin == null) || (this.m_voBillCreditOrigin != voOrigin))
    {
      this.m_voBillCreditOrigin = voOrigin;
      this.m_voBillCredit = ((BillCreditVO[][])null);
      this.m_voAccountMny = null;
      this.m_voCredit = null;
      this.m_bFreeofcremnycheck = null;
      this.m_bFreeofacclmtcheck = null;
    }
  }

  public UFDouble examOverCreditForAudit(BillCreditOriginVO voOrigin, String sMethod, UFDate logindate) throws Exception
  {
    ExamResultVO voExamResult = examOverCredit(voOrigin, logindate, sMethod);
    if ((voExamResult == null) || (voExamResult.m_dOverMny == null)) {
      return new UFDouble(-10000000);
    }
    return voExamResult.m_dOverMny;
  }

  public UFDouble examOverCreditOrderForAudit(AggregatedValueObject voOrder)
    throws Exception
  {
    if ((!(voOrder instanceof IExamAVO)) || (voOrder == null)) {
      throw new Exception(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000090"));
    }

    IExamAVO vo = (IExamAVO)voOrder;

    BillCreditOriginVO voOrigin = new BillCreditOriginVO();
    voOrigin.m_iBillType = vo.getBillTypeInt();
    voOrigin.m_iBillAct = vo.getActionInt();
    if (voOrigin.m_iBillAct == 1) {
      AggregatedValueObject voModified = vo.getModifiedVO();
      voOrigin.m_voBill = voModified.getParentVO();
      voOrigin.m_voBill_b = voModified.getChildrenVO();
      AggregatedValueObject voOld = vo.getOldVO();
      voOrigin.m_voBill_init = voOld.getParentVO();
      voOrigin.m_voBill_init_b = voOld.getChildrenVO();
    } else {
      voOrigin.m_voBill = vo.getParentVO();
      voOrigin.m_voBill_b = vo.getChildrenVO();
    }

    return examOverCreditForAudit(voOrigin, vo.getActionCode(), vo.getLoginDate());
  }

  public UFDouble examOverPeriodOrderForAudit(AggregatedValueObject voOrder)
    throws Exception
  {
    if ((!(voOrder instanceof IExamAVO)) || (voOrder == null)) {
      throw new Exception(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000090"));
    }

    IExamAVO vo = (IExamAVO)voOrder;

    BillCreditOriginVO voOrigin = new BillCreditOriginVO();
    voOrigin.m_iBillType = vo.getBillTypeInt();
    voOrigin.m_iBillAct = vo.getActionInt();
    if (voOrigin.m_iBillAct == 1) {
      AggregatedValueObject voModified = vo.getModifiedVO();
      voOrigin.m_voBill = voModified.getParentVO();
      voOrigin.m_voBill_b = voModified.getChildrenVO();
      AggregatedValueObject voOld = vo.getOldVO();
      voOrigin.m_voBill_init = voOld.getParentVO();
      voOrigin.m_voBill_init_b = voOld.getChildrenVO();
    } else {
      voOrigin.m_voBill = vo.getParentVO();
      voOrigin.m_voBill_b = vo.getChildrenVO();
    }

    ExamResultVO voExamResult = examOverPeriod(voOrigin, vo.getLoginDate());
    if ((voExamResult == null) || (voExamResult.m_dOverMny == null)) {
      return new UFDouble(-10000000);
    }
    return voExamResult.m_dOverMny;
  }

  public UFDouble examOverCreditGeneralForAudit(AggregatedValueObject voOrder)
    throws Exception
  {
    if ((!(voOrder instanceof IExamAVO)) || (voOrder == null)) {
      throw new Exception(NCLangResOnserver.getInstance().getStrByID("SCMSOCredit", "UPPSCMSOCredit-000090"));
    }

    IExamAVO vo = (IExamAVO)voOrder;

    BillCreditOriginVO voOrigin = new BillCreditOriginVO();
    voOrigin.m_iBillType = vo.getBillTypeInt();
    voOrigin.m_iBillAct = vo.getActionInt();
    if (voOrigin.m_iBillAct == 1) {
      AggregatedValueObject voModified = vo.getModifiedVO();
      voOrigin.m_voBill = voModified.getParentVO();
      voOrigin.m_voBill_b = voModified.getChildrenVO();
      AggregatedValueObject voOld = vo.getOldVO();
      voOrigin.m_voBill_init = voOld.getParentVO();
      voOrigin.m_voBill_init_b = voOld.getChildrenVO();
    } else {
      voOrigin.m_voBill = vo.getParentVO();
      voOrigin.m_voBill_b = vo.getChildrenVO();
    }

    return examOverCreditForAudit(voOrigin, vo.getActionCode(), vo.getLoginDate());
  }

  private UFDouble getGroupCreditOcc(String pk_corp, String pk_cubasdoc, LimitTypeCVO voLimitType, CreditVO m_voGroupCredit, DataManager dmo)
    throws Exception
  {
    CreditUtil.acquireDynamicLock(pk_cubasdoc + voLimitType.m_climittypeid);
    UFDouble[] dGroup = null;
    UFDouble dGroupOcc = CreditUtil.ZERO;

    CuCreditVO[] vos = null;

    if ((vos == null) || (vos.length < 1))
    {
      dGroupOcc = dmo.getThreeYSInfoHasType(m_voGroupCredit.m_pk_cubasdoc, m_voGroupCredit.m_climittypeid, null);
    }
    else
    {
      String temp = dmo.getThreeInfoWithOcc(vos);
      String sWhere = " acc.pk_corp not in ( select distinct pk_corp from " + temp + " )";

      dGroupOcc = dmo.getThreeYSInfoHasType(m_voGroupCredit.m_pk_cubasdoc, m_voGroupCredit.m_climittypeid, sWhere);

      dGroupOcc = CreditUtil.convertObjToUFDouble(dGroupOcc).add(CreditUtil.convertObjToUFDouble(dmo.getThreeInfoWithOcc(temp, m_voGroupCredit.m_pk_cubasdoc, m_voGroupCredit.m_climittypeid)));
    }

    dGroup = dmo.getThreeYSInfoNOType(m_voGroupCredit.m_pk_cubasdoc, m_voGroupCredit.m_climittypeid);

    dGroupOcc = dGroupOcc.add(CreditUtil.getCreditMny("0", dGroup));

    dGroupOcc = dGroupOcc.sub(dmo.getSKMnyHasType(pk_corp, m_voGroupCredit.m_pk_cubasdoc, m_voGroupCredit.m_climittypeid));

    dGroupOcc = dGroupOcc.sub(dmo.getSKMnyNOType(pk_corp, m_voGroupCredit.m_pk_cubasdoc, m_voGroupCredit.m_climittypeid));

    return dGroupOcc;
  }

  public ExamResultVO examOverCredit(BillCreditOriginVO voOrigin, UFDate logindate, String sAction)
    throws Exception
  {
    try
    {
      SCMEnv.out("调用客户信用（so120）信用检查开始！");
      printInitString(voOrigin);
      long l1 = System.currentTimeMillis();

      resetOriginVO(voOrigin);

      if (this.m_bFreeofcremnycheck == null) {
        putCumanCheckFlag();
      }

      if (this.m_bFreeofcremnycheck.booleanValue()) {
        return null;
      }

      if (this.m_voBillCredit == null) {
        BillCreditConvertor billConvertor = new BillCreditConvertor();
        this.m_voBillCredit = billConvertor.billConvert(voOrigin);
      }
      boolean bHasCredit = true;
      if ((this.m_voBillCredit == null) || (this.m_voBillCredit.length == 0) || (this.m_voBillCredit[0] == null) || (this.m_voBillCredit[0].length == 0))
      {
        bHasCredit = false;
      }

      if (logindate == null) {
        logindate = new UFDate(new Date());
      }
      MatchCredit match = new MatchCredit();
      LimitTypeCVO voLimitType = null;
      String sLockKey = null;

      UFBoolean bExamNoPatch = null;
      DataManager dmo = new DataManager();

      boolean bCorpControl = true;
      boolean bGroupControl = true;
      CreditVO m_voGroupCredit = null;

      String pk_corp = null;
      String pk_cumandoc = null;
      String pk_cubasdoc = null;

      String productid = null;
      String biztypeid = null;
      dmo = new DataManager();
      if (bHasCredit) {
        pk_corp = this.m_voBillCredit[0][0].m_pk_corp;
        pk_cumandoc = this.m_voBillCredit[0][0].m_pk_cumandoc;
        pk_cubasdoc = this.m_voBillCredit[0][0].m_pk_cubasdoc;
        if (CreditUtil.isProductline(pk_corp))
          productid = this.m_voBillCredit[0][0].m_cproductid;
        else
          productid = "#123456789*123456789";
        biztypeid = this.m_voBillCredit[0][0].m_cbiztypeid;
      }
      else if ((voOrigin.m_voBill instanceof SaleorderHVO)) {
        pk_corp = ((SaleorderHVO)voOrigin.m_voBill).m_pk_corp;
        pk_cumandoc = ((SaleorderHVO)voOrigin.m_voBill).m_ccustomerid;
        biztypeid = ((SaleorderHVO)voOrigin.m_voBill).m_cbiztype;
        if (CreditUtil.isProductline(pk_corp))
          productid = ((SaleorderBVO[])(SaleorderBVO[])voOrigin.m_voBill_b)[0].cprolineid;
        else
          productid = "#123456789*123456789";
        pk_cubasdoc = dmo.getCubasdocIDFromMan(pk_cumandoc);
      } else {
        pk_corp = (String)voOrigin.m_voBill.getAttributeValue("pk_corp");

        pk_cumandoc = (String)voOrigin.m_voBill.getAttributeValue("ccustomerid");

        biztypeid = (String)voOrigin.m_voBill.getAttributeValue("cbiztype");

        if (CreditUtil.isProductline(pk_corp)) {
          productid = (String)voOrigin.m_voBill_b[0].getAttributeValue("cinventoryid");

          productid = dmo.getProductidByInvman(new String[] { productid })[0];
        }
        else {
          productid = "#123456789*123456789";
        }pk_cubasdoc = dmo.getCubasdocIDFromMan(pk_cumandoc);
      }

      voLimitType = match.getMatchLimitType(pk_corp, productid, biztypeid);

      if (voLimitType == null)
      {
        return null;
      }

      if ((voLimitType.m_fcreditflag != null) && (voLimitType.m_fcreditflag.intValue() == 2))
      {
        return null;
      }
      this.m_voCredit = match.getMatchCredit(voLimitType, pk_cubasdoc, pk_cumandoc, logindate);

      m_voGroupCredit = match.getMatchGroupCredit(voLimitType, pk_cubasdoc, logindate);

      if (this.m_voCredit == null)
      {
        if (bExamNoPatch == null) {
          bExamNoPatch = dmo.isExamNoPatch();
          if (bExamNoPatch == null) {
            bExamNoPatch = new UFBoolean(false);
          }
        }

        if (bExamNoPatch.booleanValue()) {
          bCorpControl = true;
          if (m_voGroupCredit == null)
            bGroupControl = false;
          else
            bGroupControl = true;
        } else {
          if (m_voGroupCredit == null) {
            return null;
          }
          bCorpControl = false;
          bGroupControl = true;
        }
      }
      else {
        bCorpControl = true;
        if (m_voGroupCredit == null)
          bGroupControl = false;
        else {
          bGroupControl = true;
        }
      }

      long li = System.currentTimeMillis();
      long l = li - l1;
      SCMEnv.out("调用客户信用（so120）信用信息转换和信用匹配，调用时间消耗（毫秒）：" + l);

      if (bCorpControl) {
        if (voLimitType == null) {
          this.m_voCredit = match.getNoMatchCreditWhenNoType(pk_corp, pk_cubasdoc, pk_cumandoc);

          CreditUtil.acquireDynamicLock(pk_cumandoc);
        } else {
          CreditUtil.acquireDynamicLock(pk_cumandoc + voLimitType.m_climittypecid);
          if (this.m_voCredit == null) {
            this.m_voCredit = match.getNoMatchCredit(voLimitType, pk_cubasdoc, pk_cumandoc);
          }
        }

      }

      UFDouble dGroupOcc = CreditUtil.ZERO;
      if (bGroupControl) {
        dGroupOcc = getGroupCreditOcc(pk_corp, pk_cubasdoc, voLimitType, m_voGroupCredit, dmo);
      }

      boolean bAddCurrentBill = false;
      boolean bOnlyAddBillmny = false;
      if (bHasCredit) {
        if ((this.m_voBillCredit[0][0].m_fbilltypeflag.intValue() == 0) && ((this.m_voCredit == null) || (this.m_voCredit.m_voccupiedfun.equals("0")) || (this.m_voCredit.m_voccupiedfun.equals("3"))) && (("PreKeep".equals(sAction)) || ("PreModify".equals(sAction)) || ("SpecialSave".equals(sAction))))
        {
          bAddCurrentBill = true;
        } else if ((this.m_voBillCredit[0][0].m_fbilltypeflag.intValue() == 1) && 
          ((this.m_voCredit == null) || (this.m_voCredit.m_voccupiedfun.equals("1")) || (this.m_voCredit.m_voccupiedfun.equals("0"))) && (
          ("SAVEBASE".equals(sAction)) || (!"DELETE".equals(sAction))))
        {
          bAddCurrentBill = true;
        }
      }
      else {
        bAddCurrentBill = false;
      }

      if ((this.m_voCredit == null) || ((this.m_voCredit.m_voccupiedfun.equals("2")) && ("SIGN".equals(sAction)) && (CreditUtil.is4CAutoIncomeBalance(pk_corp, biztypeid))))
      {
        bOnlyAddBillmny = true;
      }

      if ((bAddCurrentBill) && 
        (this.m_voAccountMny == null)) {
        this.m_voAccountMny = RenovateAR.getAccountMny(this.m_voBillCredit);
      }

      long lj = System.currentTimeMillis();
      l = lj - li;
      SCMEnv.out("调用客户信用（so120）信用更新应收计算，调用时间消耗（毫秒）：" + l);

      CreditMny creditmny = new CreditMny();
      CuCreditVO[] corpvos = null;

      UFDouble[] dCreditOccupAll = null;
      if ((corpvos == null) || (corpvos.length < 1)) {
        dCreditOccupAll = creditmny.getCreditMny(this.m_voCredit, this.m_voAccountMny);
      }
      else {
        String temp = dmo.getThreeInfoWithOcc(corpvos);
        dCreditOccupAll = new UFDouble[2];
        dCreditOccupAll[0] = ((UFDouble)dmo.getThreeInfoWithOcc(temp, this.m_voCredit.m_pk_cubasdoc, this.m_voCredit.m_climittypeid, this.m_voCredit.m_pk_corp, this.m_voCredit.m_climittypecid));

        if (this.m_voAccountMny == null)
          dCreditOccupAll[1] = creditmny.getCreditMnyTemp(this.m_voCredit, this.m_voAccountMny);
        else
          dCreditOccupAll[1] = CreditUtil.ZERO;
      }
      if ((dCreditOccupAll == null) || (dCreditOccupAll.length < 2))
      {
        dCreditOccupAll = new UFDouble[2];
        dCreditOccupAll[0] = CreditUtil.ZERO;
        dCreditOccupAll[1] = CreditUtil.ZERO;
      }
      else
      {
        dCreditOccupAll[0] = CreditUtil.convertObjToUFDouble(dCreditOccupAll[0]);
        dCreditOccupAll[1] = CreditUtil.convertObjToUFDouble(dCreditOccupAll[1]);
      }
      UFDouble dCreditOccup = null;
      UFDouble dCreditLimit = null;

      if (bCorpControl)
      {
        dCreditLimit = this.m_voCredit.m_nlimitmny;
        dCreditOccup = dCreditOccupAll[0].add(dCreditOccupAll[1]);
        if (this.m_voCredit != null) {
          dCreditOccup = dCreditOccup.sub(CreditUtil.convertObjToUFDouble(dmo.getSKMnyForCorp(this.m_voCredit.m_pk_corp, this.m_voCredit.m_pk_cubasdoc, this.m_voCredit.m_climittypecid)));
        }

      }
      else
      {
        dCreditLimit = CreditUtil.ZERO;
        dCreditOccup = new UFDouble(-100000.0D);
      }
      UFDouble dGroupCreditLimit = CreditUtil.ZERO;
      if (m_voGroupCredit != null) {
        dGroupCreditLimit = CreditUtil.convertObjToUFDouble(m_voGroupCredit.m_nlimitmny);
      }

      dGroupOcc = dGroupOcc.add(dCreditOccupAll[1]);

      ExamResultVO voRE = new ExamResultVO();
      voRE.m_sLockKey = sLockKey;
      if (!bGroupControl)
        dGroupOcc = new UFDouble(-100000.0D);
      voRE.m_dOverMny = CreditUtil.maxUFDoble(dCreditOccup.sub(dCreditLimit), dGroupOcc.sub(dGroupCreditLimit));

      if (bOnlyAddBillmny) {
        voRE.m_dOverMny = voRE.m_dOverMny.add(CreditUtil.getBillBusiMny(voOrigin.m_voBill_b, 0));
      }

      voRE.m_dOverMny = dmo.changeDataByCurScale(pk_corp, new UFDouble[] { voRE.m_dOverMny })[0];

      voRE.m_iFlag = 0;
      if (this.m_voCredit != null)
        voRE.m_iFlag = this.m_voCredit.m_fcreditflag.intValue();
      else if (m_voGroupCredit != null) {
        voRE.m_iFlag = m_voGroupCredit.m_fcreditflag.intValue();
      }
      long l2 = System.currentTimeMillis();
      long l12 = l2 - l1;
      SCMEnv.out("调用客户信用（so120）信用检查结束，调用时间消耗（毫秒）：" + l12);

      return voRE;
    }
    catch (Exception err) {
      SCMEnv.out(err.getMessage());
      throw err;
    }
  }
}