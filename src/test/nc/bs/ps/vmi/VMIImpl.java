package nc.bs.ps.vmi;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.ps.settle.SettleDMO;
import nc.bs.ps.settle.SettleImpl;
import nc.bs.pu.pub.GetSysBillCode;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.bs.scm.pub.TempTableDMO;
import nc.itf.ia.service.IIAToPUBill;
import nc.itf.ic.service.IICToPU_VmiSumDMO;
import nc.itf.ps.vmi.IVMI;
import nc.itf.pu.pub.fw.LockTool;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.ia.bill.BillVO;
import nc.vo.ic.pub.vmi.VmiSumHeaderVO;
import nc.vo.ic.pub.vmi.VmiSumVO;
import nc.vo.ic.service.ParaVOEstiToVMI;
import nc.vo.ic.service.ParaVOSettleToVMI;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pi.InvoiceVO;
import nc.vo.ps.settle.FeeinvoiceVO;
import nc.vo.ps.settle.IBillHeaderVO;
import nc.vo.ps.settle.IBillItemVO;
import nc.vo.ps.settle.IinvoiceVO;
import nc.vo.ps.settle.SettlebillHeaderVO;
import nc.vo.ps.settle.SettlebillItemVO;
import nc.vo.ps.settle.SettlebillVO;
import nc.vo.ps.vmi.EstimateVO;
import nc.vo.ps.vmi.SettleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.formulaset.VarryVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.pub.SCMEnv;

public class VMIImpl
  implements IVMI
{
  public void antiEstimate(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 6)) {
      SCMEnv.out("程序BUG：VMI汇总查询参数传递不正确!直接返回 NULL");
      return;
    }
    EstimateVO[] estiVOs = (EstimateVO[])listPara.get(0);
    if ((estiVOs == null) || (estiVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[暂估数据],直接返回!");
      return;
    }
    String strOperator = (String)listPara.get(2);
    boolean bLock = false;
    int iLen = estiVOs.length;

    String[] strKeys = new String[iLen];
    for (int i = 0; i < iLen; i++) {
      strKeys[i] = estiVOs[i].getCvmihid();
    }
    try
    {
      bLock = LockTool.setLockForPks(strKeys, strOperator);
      if (!bLock) {
        throw new RemoteException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000009"));
      }
      checkTSChangedEsti(estiVOs);

      rewriteVMIAntiEsti(estiVOs);

      generalBillVOAntiEsti(estiVOs, strOperator);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    } finally {
      try {
        if (bLock)
          LockTool.releaseLockForPks(strKeys, strOperator);
      }
      catch (Exception localException1)
      {
      }
    }
  }

  private void checkTSChangedEsti(EstimateVO[] estiVOs)
    throws BusinessException
  {
    if ((estiVOs == null) || (estiVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[暂估数据],直接返回!");
      return;
    }
    int iLen = estiVOs.length;
    String[] saHid = new String[iLen];
    String[] saOldTs = new String[iLen];
    for (int i = 0; i < iLen; i++) {
      if (estiVOs[i] == null)
        continue;
      saHid[i] = estiVOs[i].getCvmihid();
      saOldTs[i] = estiVOs[i].getTs();
    }
    checkTSChangedVMIHead(saHid, saOldTs);
  }

  private String checkTSChangedSettle(SettleVO[] settleVOs, IinvoiceVO[] iinvoiceVOs, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs)
    throws BusinessException
  {
    String sMessage = "";
    try
    {
      PubDMO pubDMO = new PubDMO();

      String[] sHeadKey = (String[])null;
      String[] sBodyKey = (String[])null;

      if ((settleVOs != null) && (settleVOs.length > 0)) {
        int iLen = settleVOs.length;
        String[] saTsOld = new String[iLen];
        sHeadKey = new String[iLen];
        for (int i = 0; i < settleVOs.length; i++) {
          if (settleVOs[i] == null)
            continue;
          sHeadKey[i] = settleVOs[i].getCvmihid();
          saTsOld[i] = settleVOs[i].getTs();
        }
        checkTSChangedVMIHead(sHeadKey, saTsOld);
      }

      if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
        sHeadKey = new String[iinvoiceVOs.length];
        sBodyKey = new String[iinvoiceVOs.length];
        for (int i = 0; i < iinvoiceVOs.length; i++) {
          sHeadKey[i] = iinvoiceVOs[i].getCinvoiceid();
          sBodyKey[i] = iinvoiceVOs[i].getCinvoice_bid();
        }

        Object[] ts = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
        if ((ts != null) && (ts.length > 0)) {
          String[] ts1 = (String[])ts[0];
          String[] ts2 = (String[])ts[1];

          if ((ts1 != null) && (ts1.length > 0))
            for (int i = 0; i < iinvoiceVOs.length; i++) {
              String temp1 = ts1[i];
              String temp2 = iinvoiceVOs[i].getTs1();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000181", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000017");
          }

          if ((ts2 != null) && (ts2.length > 0))
            for (int i = 0; i < iinvoiceVOs.length; i++) {
              String temp1 = ts2[i];
              String temp2 = iinvoiceVOs[i].getTs2();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000182", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000018");
          }
        }

      }

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        sHeadKey = new String[feeVOs.length];
        sBodyKey = new String[feeVOs.length];
        for (int i = 0; i < feeVOs.length; i++) {
          sHeadKey[i] = feeVOs[i].getCinvoiceid();
          sBodyKey[i] = feeVOs[i].getCinvoice_bid();
        }

        Object[] ts = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
        if ((ts != null) && (ts.length > 0)) {
          String[] ts1 = (String[])ts[0];
          String[] ts2 = (String[])ts[1];

          if ((ts1 != null) && (ts1.length > 0))
            for (int i = 0; i < feeVOs.length; i++) {
              String temp1 = ts1[i];
              String temp2 = feeVOs[i].getTs1();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000183", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000017");
          }

          if ((ts2 != null) && (ts2.length > 0))
            for (int i = 0; i < feeVOs.length; i++) {
              String temp1 = ts2[i];
              String temp2 = feeVOs[i].getTs2();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000184", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000018");
          }
        }

      }

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        sHeadKey = new String[discountVOs.length];
        sBodyKey = new String[discountVOs.length];
        for (int i = 0; i < discountVOs.length; i++) {
          sHeadKey[i] = discountVOs[i].getCinvoiceid();
          sBodyKey[i] = discountVOs[i].getCinvoice_bid();
        }

        Object[] ts = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
        if ((ts != null) && (ts.length > 0)) {
          String[] ts1 = (String[])ts[0];
          String[] ts2 = (String[])ts[1];

          if ((ts1 != null) && (ts1.length > 0))
            for (int i = 0; i < discountVOs.length; i++) {
              String temp1 = ts1[i];
              String temp2 = discountVOs[i].getTs1();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000185", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000017");
          }

          if ((ts2 != null) && (ts2.length > 0))
            for (int i = 0; i < discountVOs.length; i++) {
              String temp1 = ts2[i];
              String temp2 = discountVOs[i].getTs2();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000186", null, value);
              }
            }
          else
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000018");
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }

    return sMessage;
  }

  private String checkTSChangedSettleFee(SettleVO[] settleVOs, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs, FeeinvoiceVO[] specimalVOs)
    throws BusinessException
  {
    String sMessage = "";
    try
    {
      PubDMO pubDMO = new PubDMO();

      String[] sHeadKey = (String[])null;
      String[] sBodyKey = (String[])null;

      if ((settleVOs != null) && (settleVOs.length > 0)) {
        int iLen = settleVOs.length;
        String[] saTsOld = new String[iLen];
        sHeadKey = new String[iLen];
        for (int i = 0; i < settleVOs.length; i++) {
          if (settleVOs[i] == null)
            continue;
          sHeadKey[i] = settleVOs[i].getCvmihid();
          saTsOld[i] = settleVOs[i].getTs();
        }
        checkTSChangedVMIHead(sHeadKey, saTsOld);
      }

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        sHeadKey = new String[feeVOs.length];
        sBodyKey = new String[feeVOs.length];
        for (int i = 0; i < feeVOs.length; i++) {
          sHeadKey[i] = feeVOs[i].getCinvoiceid();
          sBodyKey[i] = feeVOs[i].getCinvoice_bid();
        }

        Object[] ts = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
        if ((ts != null) && (ts.length > 0)) {
          String[] ts1 = (String[])ts[0];
          String[] ts2 = (String[])ts[1];

          if ((ts1 != null) && (ts1.length > 0))
            for (int i = 0; i < feeVOs.length; i++) {
              String temp1 = ts1[i];
              String temp2 = feeVOs[i].getTs1();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000183", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000017");
          }

          if ((ts2 != null) && (ts2.length > 0))
            for (int i = 0; i < feeVOs.length; i++) {
              String temp1 = ts2[i];
              String temp2 = feeVOs[i].getTs2();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000184", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000018");
          }
        }

      }

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        sHeadKey = new String[discountVOs.length];
        sBodyKey = new String[discountVOs.length];
        for (int i = 0; i < discountVOs.length; i++) {
          sHeadKey[i] = discountVOs[i].getCinvoiceid();
          sBodyKey[i] = discountVOs[i].getCinvoice_bid();
        }

        Object[] ts = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
        if ((ts != null) && (ts.length > 0)) {
          String[] ts1 = (String[])ts[0];
          String[] ts2 = (String[])ts[1];

          if ((ts1 != null) && (ts1.length > 0))
            for (int i = 0; i < discountVOs.length; i++) {
              String temp1 = ts1[i];
              String temp2 = discountVOs[i].getTs1();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000185", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000017");
          }

          if ((ts2 != null) && (ts2.length > 0))
            for (int i = 0; i < discountVOs.length; i++) {
              String temp1 = ts2[i];
              String temp2 = discountVOs[i].getTs2();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000186", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000018");
          }
        }

      }

      if ((specimalVOs != null) && (specimalVOs.length > 0)) {
        sHeadKey = new String[specimalVOs.length];
        sBodyKey = new String[specimalVOs.length];
        for (int i = 0; i < specimalVOs.length; i++) {
          sHeadKey[i] = specimalVOs[i].getCinvoiceid();
          sBodyKey[i] = specimalVOs[i].getCinvoice_bid();
        }

        Object[] ts = pubDMO.queryHBTsArrayByHBIDArray("25", sHeadKey, sBodyKey, null);
        if ((ts != null) && (ts.length > 0)) {
          String[] ts1 = (String[])ts[0];
          String[] ts2 = (String[])ts[1];

          if ((ts1 != null) && (ts1.length > 0))
            for (int i = 0; i < specimalVOs.length; i++) {
              String temp1 = ts1[i];
              String temp2 = specimalVOs[i].getTs1();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000181", null, value);
              }
            }
          else {
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000017");
          }

          if ((ts2 != null) && (ts2.length > 0))
            for (int i = 0; i < specimalVOs.length; i++) {
              String temp1 = ts2[i];
              String temp2 = specimalVOs[i].getTs2();
              if ((temp1 == null) || (!temp1.equals(temp2))) {
                String[] value = { String.valueOf(i + 1) };
                sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000182", null, value);
              }
            }
          else
            sMessage = sMessage + NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000018");
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }

    return sMessage;
  }

  private void checkTSChangedVMIHead(String[] saHid, String[] saOldTs)
    throws BusinessException
  {
    if ((saHid == null) || (saHid.length <= 0)) {
      SCMEnv.out("传入参数为空[VMI头ID数据],直接返回!");
      return;
    }
    if ((saOldTs == null) || (saOldTs.length <= 0)) {
      SCMEnv.out("传入参数为空[VMI头TS数据],直接返回!");
      return;
    }
    int iLenId = saHid.length;
    int iLenTs = saOldTs.length;
    if (iLenId != iLenTs) {
      SCMEnv.out("传入参数不正确[VMI头ID长度与TS长度不同],直接返回!");
      return;
    }
    StringBuffer sbSql = new StringBuffer("select cvmihid,ts from ic_vmi_sum where ");
    for (int i = 0; i < iLenId; i++) {
      if (i > 0)
        sbSql.append("or ");
      sbSql.append("cvmihid='" + saHid[i] + "' ");
    }
    try {
      VMIDMO dmo = new VMIDMO();
      Hashtable hashTs = dmo.queryHashStringBySQL(sbSql.toString());
      if ((hashTs == null) || (hashTs.size() != iLenTs)) {
        throw new RemoteException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
      }
      String strTsNew = null;
      for (int i = 0; i < iLenTs; i++) {
        if (saHid[i] == null)
          continue;
        strTsNew = (String)hashTs.get(saHid[i]);
        if ((strTsNew == null) || (strTsNew.trim().length() <= 0) || (!strTsNew.equals(saOldTs[i])))
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000058"));
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  public ArrayList doBalanceFee(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 9)) {
      SCMEnv.out("程序BUG：VMI汇总结算参数传递不正确!直接返回 NULL");
      return null;
    }
    SettlebillVO settlebillVO = (SettlebillVO)listPara.get(0);
    SettlebillItemVO[] settleItemVOs = settlebillVO.getBodyVO();
    SettleVO[] settleVOs = (SettleVO[])listPara.get(1);
    FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])listPara.get(2);
    FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])listPara.get(3);
    FeeinvoiceVO[] specialVOs = (FeeinvoiceVO[])listPara.get(4);
    String sDifferenceMode = (String)listPara.get(5);
    String sOperator = (String)listPara.get(6);
    String sUnitCode = (String)listPara.get(7);
    UFDate dateLogin = (UFDate)listPara.get(8);

    SettleDMO dmo = null;
    ICreateCorpQueryService srvCrtCorp = null;
    boolean bLock = false;
    String key = null;

    String[] sKeys = getAllLockKeysFee(settleVOs, feeVOs, discountVOs, specialVOs);
    try
    {
      bLock = LockTool.setLockForPks(sKeys, sOperator);
      if (!bLock) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000009"));
      }

      String strErr = checkTSChangedSettleFee(settleVOs, feeVOs, discountVOs, specialVOs);
      if ((strErr != null) && (strErr.trim().length() > 0)) {
        throw new RemoteException(strErr);
      }

      GetSysBillCode getBillCode = new GetSysBillCode();
      String vSettleCode = getBillCode.getSysBillNO(settlebillVO);

      SettlebillHeaderVO head = settlebillVO.getHeadVO();
      head.setAttributeValue("vsettlebillcode", vSettleCode);
      head.setTmaketime(new UFDateTime(new Date()).toString());

      dmo = new SettleDMO();
      key = dmo.insertHead(head);

      SettlebillItemVO[] body = settlebillVO.getBodyVO();
      String[] s = dmo.insertBody(body, key);
      for (int i = 0; i < body.length; i++) {
        body[i].setCsettlebill_bid(s[i]);
        body[i].setCsettlebillid(key);
      }

      if ((settleVOs != null) && (settleVOs.length > 0)) {
        rewriteVMISettleFee(settleItemVOs);
      }

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        dmo.updateFee(feeVOs);
      }

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        dmo.updateDiscount(discountVOs);
      }

      if ((specialVOs != null) && (specialVOs.length > 0)) {
        dmo.updateFee(specialVOs);
      }

      if ((settleVOs != null) && (settleVOs.length > 0)) {
        srvCrtCorp = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
        boolean bIAStartUp = srvCrtCorp.isEnabled(sUnitCode, "IA");
        if (bIAStartUp)
          generalBillVOSettleFee(settleItemVOs, settleVOs, sOperator, sUnitCode, sDifferenceMode, dateLogin, vSettleCode);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      try
      {
        GetSysBillCode billCode = new GetSysBillCode();
        billCode.returnBillNo(settlebillVO);
      }
      catch (Exception ex) {
        SCMEnv.out(e);
        PubDMO.throwBusinessException(e);
      }

      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    finally {
      try {
        if (bLock)
          LockTool.releaseLockForPks(sKeys, sOperator);
      }
      catch (Exception e)
      {
        SCMEnv.out("解锁或除去业务BO对象时异常：");
        SCMEnv.out(e);
      }
    }
    ArrayList listRet = new ArrayList();
    listRet.add(key);
    return listRet;
  }

  public ArrayList doVMI2InvoiceSettle(InvoiceVO invoiceVO, String accountYear, UFDate dCurrDate, String pk_corp, String cOperator, String sZGMode, String sDifferMode, UFBoolean bNeedLock)
    throws BusinessException
  {
    String vSettlebillCode = null;
    UFBoolean bSucceed = new UFBoolean(false);
    ArrayList listLockId = null;
    boolean bLockSucc = false;
    Vector v = null;
    try
    {
      GetSysBillCode billCode = new GetSysBillCode();
      SettlebillHeaderVO head = new SettlebillHeaderVO();
      head.setPk_corp(pk_corp);
      SettlebillVO tempVO = new SettlebillVO(1);
      tempVO.setParentVO(head);
      tempVO.setChildrenVO(new SettlebillItemVO[] { new SettlebillItemVO() });
      do
        vSettlebillCode = billCode.getSysBillNO(tempVO);
      while (
        new SettleImpl().isSettlebillCodeDuplicate(pk_corp, vSettlebillCode));

      VMI2InvoiceSettle settle = new VMI2InvoiceSettle(accountYear, dCurrDate, pk_corp, cOperator, vSettlebillCode);
      v = settle.doBalance(invoiceVO);
      if ((v == null) || (v.size() == 0)) {
        GetSysBillCode billCode1 = new GetSysBillCode();
        billCode1.returnBillNo(tempVO);

        ArrayList list = new ArrayList();
        list.add(listLockId);
        list.add(bSucceed);
        return list;
      }
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    VmiSumHeaderVO[] vmiVOs = (VmiSumHeaderVO[])v.elementAt(1);
    IinvoiceVO[] invoiceVOs = (IinvoiceVO[])v.elementAt(0);
    SettlebillVO settlebillVO = (SettlebillVO)v.elementAt(2);
    try
    {
      SettleDMO dmo = new SettleDMO();
      String key = dmo.insertHead(settlebillVO.getHeadVO());

      SettlebillItemVO[] body = settlebillVO.getBodyVO();
      String[] s = dmo.insertBody(body, key);
      for (int i = 0; i < body.length; i++) {
        body[i].setCsettlebill_bid(s[i]);
        body[i].setCsettlebillid(key);
      }

      ParaVOSettleToVMI[] paraVOs = new ParaVOSettleToVMI[body.length];
      for (int i = 0; i < body.length; i++) {
        paraVOs[i] = new ParaVOSettleToVMI();
        paraVOs[i].setSVMIHId(body[i].getCvmiid());
        paraVOs[i].setNMoney(body[i].getNmoney());
        paraVOs[i].setNNum(body[i].getNsettlenum());
      }
      IICToPU_VmiSumDMO sumdmo = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());
      sumdmo.writeBackToVMISettle(paraVOs);

      if ((invoiceVOs != null) && (invoiceVOs.length > 0)) {
        dmo.updateInvoice(invoiceVOs);
      }

      bSucceed = new UFBoolean(true);

      ICreateCorpQueryService srvCrtCorp = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      boolean bIAStartUp = srvCrtCorp.isEnabled(pk_corp, "IA");
      if (bIAStartUp)
      {
        if (bNeedLock.booleanValue()) {
          listLockId = new ArrayList();
          int iLen = body.length;
          for (int i = 0; i < iLen; i++) {
            if (body[i].getCvmiid() != null) {
              listLockId.add(body[i].getCvmiid());
            }
          }
          String[] saLockId = (String[])null;
          if ((listLockId != null) && (listLockId.size() > 0)) {
            saLockId = new String[listLockId.size()];
            saLockId = (String[])listLockId.toArray(saLockId);
            bLockSucc = LockTool.setLockForPks(saLockId, cOperator);
            if (!bLockSucc) throw new BusinessException("参与结算的VMI正在被占用!");
          }
        }

        savebillFromOutterVMI(
          body, 
          vmiVOs, 
          cOperator, 
          pk_corp, 
          sZGMode, 
          sDifferMode, 
          dCurrDate, 
          vSettlebillCode);
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      try
      {
        GetSysBillCode billCode = new GetSysBillCode();
        billCode.returnBillNo(settlebillVO);
      } catch (Exception ex) {
        SCMEnv.out(e);
        PubDMO.throwBusinessException(e);
      }

      if ((bLockSucc) && (listLockId != null) && (listLockId.size() > 0)) {
        String[] saLockId = new String[listLockId.size()];
        saLockId = (String[])listLockId.toArray(saLockId);
        try {
          LockTool.releaseLockForPks(saLockId, cOperator);
        } catch (Exception ee) {
          PubDMO.throwBusinessException(ee);
        }
      }
      PubDMO.throwBusinessException(e);
    }

    ArrayList list = new ArrayList();
    list.add(listLockId);
    list.add(bSucceed);
    return list;
  }

  public ArrayList doBalanceManual(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 10)) {
      SCMEnv.out("程序BUG：VMI汇总结算参数传递不正确!直接返回 NULL");
      return null;
    }
    SettlebillVO settlebillVO = (SettlebillVO)listPara.get(0);
    SettlebillItemVO[] settleItemVOs = settlebillVO.getBodyVO();
    SettleVO[] settleVOs = (SettleVO[])listPara.get(1);
    IinvoiceVO[] iinvoiceVOs = (IinvoiceVO[])listPara.get(2);
    FeeinvoiceVO[] feeVOs = (FeeinvoiceVO[])listPara.get(3);
    FeeinvoiceVO[] discountVOs = (FeeinvoiceVO[])listPara.get(4);
    String sEstimateMode = (String)listPara.get(5);
    String sDifferenceMode = (String)listPara.get(6);
    String sOperator = (String)listPara.get(7);
    String sUnitCode = (String)listPara.get(8);
    UFDate dateLogin = (UFDate)listPara.get(9);

    SettleDMO dmo = null;
    ICreateCorpQueryService srvCrtCorp = null;
    boolean bLock = false;
    String key = null;

    String[] sKeys = getAllLockKeysManual(settleVOs, iinvoiceVOs, feeVOs, discountVOs);
    try
    {
      bLock = LockTool.setLockForPks(sKeys, sOperator);
      if (!bLock) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000009"));
      }

      String strErr = checkTSChangedSettle(settleVOs, iinvoiceVOs, feeVOs, discountVOs);
      if ((strErr != null) && (strErr.trim().length() > 0)) {
        throw new RemoteException(strErr);
      }

      GetSysBillCode getBillCode = new GetSysBillCode();
      String vSettleCode = getBillCode.getSysBillNO(settlebillVO);

      SettlebillHeaderVO head = settlebillVO.getHeadVO();
      head.setAttributeValue("vsettlebillcode", vSettleCode);
      head.setTmaketime(new UFDateTime(new Date()).toString());

      dmo = new SettleDMO();
      key = dmo.insertHead(head);

      SettlebillItemVO[] body = settlebillVO.getBodyVO();
      String[] s = dmo.insertBody(body, key);
      for (int i = 0; i < body.length; i++) {
        body[i].setCsettlebill_bid(s[i]);
        body[i].setCsettlebillid(key);
      }

      if ((settleVOs != null) && (settleVOs.length > 0)) {
        rewriteVMISettle(settleVOs);
      }

      if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
        dmo.updateInvoice(iinvoiceVOs);
      }

      if ((feeVOs != null) && (feeVOs.length > 0)) {
        dmo.updateFee(feeVOs);
      }

      if ((discountVOs != null) && (discountVOs.length > 0)) {
        dmo.updateDiscount(discountVOs);
      }

      if ((settleVOs != null) && (settleVOs.length > 0)) {
        srvCrtCorp = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
        boolean bIAStartUp = srvCrtCorp.isEnabled(sUnitCode, "IA");
        if (bIAStartUp)
          generalBillVOSettle(
            settleItemVOs, 
            settleVOs, 
            sOperator, 
            sUnitCode, 
            sEstimateMode, 
            sDifferenceMode, 
            dateLogin, 
            vSettleCode);
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      try
      {
        GetSysBillCode billCode = new GetSysBillCode();
        billCode.returnBillNo(settlebillVO);
      }
      catch (Exception ex) {
        SCMEnv.out(e);
        PubDMO.throwBusinessException(e);
      }

      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    finally {
      try {
        if (bLock)
          LockTool.releaseLockForPks(sKeys, sOperator);
      }
      catch (Exception e)
      {
        SCMEnv.out("解锁或除去业务BO对象时异常：");
        SCMEnv.out(e);
      }
    }
    ArrayList listRet = new ArrayList();
    listRet.add(key);
    return listRet;
  }

  public void estimate(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 6)) {
      SCMEnv.out("程序BUG：VMI汇总查询参数传递不正确!直接返回 NULL");
      return;
    }
    EstimateVO[] estiVOs = (EstimateVO[])listPara.get(0);
    if ((estiVOs == null) || (estiVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[暂估数据],直接返回!");
      return;
    }
    String strUnitCode = (String)listPara.get(1);
    String strOperator = (String)listPara.get(2);
    UFDate dateLogin = (UFDate)listPara.get(3);
    boolean bLock = false;
    int iLen = estiVOs.length;

    String[] strKeys = new String[iLen];
    for (int i = 0; i < iLen; i++) {
      strKeys[i] = estiVOs[i].getCvmihid();
    }
    try
    {
      bLock = LockTool.setLockForPks(strKeys, strOperator);
      if (!bLock) {
        throw new RemoteException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000009"));
      }
      checkTSChangedEsti(estiVOs);

      rewriteVMIEsti(estiVOs, dateLogin);

      generalBillVOEsti(estiVOs, strOperator, strUnitCode, dateLogin);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    } finally {
      try {
        if (bLock)
          LockTool.releaseLockForPks(strKeys, strOperator);
      }
      catch (Exception localException1)
      {
      }
    }
  }

  private void generalBillVOAntiEsti(EstimateVO[] estiVOs, String strOperator)
    throws BusinessException
  {
    if ((estiVOs == null) || (estiVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[暂估数据],直接返回!");
      return;
    }
    int iLen = estiVOs.length;
    String[] strHIds = new String[iLen];
    for (int i = 0; i < iLen; i++) {
      strHIds[i] = estiVOs[i].getCvmihid();
    }
    IIAToPUBill myService = null;
    try
    {
      myService = (IIAToPUBill)NCLocator.getInstance().lookup(IIAToPUBill.class.getName());
      myService.deleteBillItemForPUs(strHIds, strOperator);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  private void generalBillVOEsti(EstimateVO[] estiVOs, String strOperator, String strPkCorp, UFDate dateLogin)
    throws BusinessException
  {
    if ((estiVOs == null) || (estiVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[暂估数据],直接返回!");
      return;
    }
    IIAToPUBill myService = null;
    try
    {
      VMIDMO dmo = new VMIDMO();
      ArrayList listData = dmo.queryInfosForIAEsti(estiVOs);
      SettleDMO settleDmo = new SettleDMO();
      String strVMIBusitypeId = settleDmo.queryVMIBusitye(strPkCorp);
      IBillItemVO[] bodyVOs = new IBillItemVO[listData.size()];
      IBillHeaderVO[] headVOs = new IBillHeaderVO[listData.size()];
      BillVO[] billVOs = new BillVO[listData.size()];
      VmiSumHeaderVO vmiSumHVO = null;
      ArrayList listTemp = null;
      Integer iPriceMethod = null;
      UFDouble dPlanPrice = null; UFDouble dAssNum = null;
      double d1 = 0.0D; double d2 = 0.0D;
      for (int i = 0; i < bodyVOs.length; i++)
      {
        bodyVOs[i] = new IBillItemVO();
        listTemp = (ArrayList)(ArrayList)listData.get(i);
        vmiSumHVO = (VmiSumHeaderVO)listTemp.get(0);
        iPriceMethod = (Integer)listTemp.get(1);
        dPlanPrice = (UFDouble)listTemp.get(2);
        dAssNum = (UFDouble)listTemp.get(3);

        bodyVOs[i].setPk_corp(vmiSumHVO.getPk_corp());
        bodyVOs[i].setCbilltypecode("I2");
        bodyVOs[i].setCbill_bid(null);
        bodyVOs[i].setCbillid(null);

        bodyVOs[i].setCsourcebilltypecode("50");
        bodyVOs[i].setCsourcebillid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setCsourcebillitemid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setVsourcebillcode(vmiSumHVO.getVbillcode());

        bodyVOs[i].setCfirstbilltypecode("50");
        bodyVOs[i].setCfirstbillid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setCfirstbillitemid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setVfirstbillcode(vmiSumHVO.getVbillcode());

        bodyVOs[i].setFpricemodeflag(iPriceMethod);
        bodyVOs[i].setCinventoryid(vmiSumHVO.getCinventoryid());
        bodyVOs[i].setVbatch(vmiSumHVO.getVlot());

        d1 = 0.0D;
        if (vmiSumHVO.getNoutnum() != null) d1 = vmiSumHVO.getNoutnum().doubleValue();
        d2 = 0.0D;
        if (vmiSumHVO.getNoutinnum() != null) d2 = vmiSumHVO.getNoutinnum().doubleValue();
        bodyVOs[i].setNnumber(new UFDouble(d1 - d2));

        if (bodyVOs[i].getNnumber() == null)
          bodyVOs[i].setNnumber(new UFDouble(0.0D));
        bodyVOs[i].setNprice(vmiSumHVO.getNprice());
        if (bodyVOs[i].getNprice() == null)
          bodyVOs[i].setNprice(new UFDouble(0.0D));
        bodyVOs[i].setNmoney(bodyVOs[i].getNnumber().multiply(bodyVOs[i].getNprice()));
        bodyVOs[i].setNplanedprice(dPlanPrice);
        if (bodyVOs[i].getNplanedprice() == null)
          bodyVOs[i].setNplanedprice(new UFDouble(0.0D));
        bodyVOs[i].setNplanedmny(bodyVOs[i].getNnumber().multiply(bodyVOs[i].getNplanedprice()));
        bodyVOs[i].setCastunitid(vmiSumHVO.getCastunitid());
        bodyVOs[i].setNassistnum((bodyVOs[i].getCastunitid() == null) || (bodyVOs[i].getCastunitid().trim().equals("")) ? null : dAssNum);

        bodyVOs[i].setIrownumber(new Integer(i));

        bodyVOs[i].setVfree1(vmiSumHVO.getVfree1());
        bodyVOs[i].setVfree2(vmiSumHVO.getVfree2());
        bodyVOs[i].setVfree3(vmiSumHVO.getVfree3());
        bodyVOs[i].setVfree4(vmiSumHVO.getVfree4());
        bodyVOs[i].setVfree5(vmiSumHVO.getVfree5());

        bodyVOs[i].setCprojectid(null);
        bodyVOs[i].setCprojectphase(null);

        bodyVOs[i].setDbizdate(vmiSumHVO.getDgaugedate());

        headVOs[i] = new IBillHeaderVO();
        headVOs[i].setPk_corp(vmiSumHVO.getPk_corp());
        headVOs[i].setCbilltypecode("I2");
        headVOs[i].setCbillid(vmiSumHVO.getCvmihid());
        headVOs[i].setDbilldate(dateLogin);
        headVOs[i].setCsourcemodulename("PO");
        headVOs[i].setFdispatchflag(new Integer(0));
        headVOs[i].setCdispatchid(null);

        headVOs[i].setCbiztypeid(strVMIBusitypeId);
        headVOs[i].setCrdcenterid(vmiSumHVO.getCcalbodyid());
        headVOs[i].setCwarehouseid(vmiSumHVO.getCwarehouseid());

        headVOs[i].setCdeptid(null);
        headVOs[i].setCoperatorid(strOperator);
        headVOs[i].setCcustomvendorid(vmiSumHVO.getCvendorid());

        headVOs[i].setVnote(null);
        headVOs[i].setBestimateflag(new UFBoolean(true));
        headVOs[i].setCwarehousemanagerid(null);
        headVOs[i].setCemployeeid(null);

        headVOs[i].setVbillcode(null);
        headVOs[i].setBwithdrawalflag(new UFBoolean(false));
        headVOs[i].setBdisableflag(new UFBoolean(false));
        headVOs[i].setBauditedflag(new UFBoolean(false));

        billVOs[i] = new BillVO();
        billVOs[i].setParentVO(headVOs[i]);
        billVOs[i].setChildrenVO(new IBillItemVO[] { bodyVOs[i] });
      }

      myService = (IIAToPUBill)NCLocator.getInstance().lookup(IIAToPUBill.class.getName());
      myService.saveBillFromOutterArray(billVOs, "PO", "50");
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  private void savebillFromOutterVMI(SettlebillItemVO[] settleItemVOs, VmiSumHeaderVO[] vmiVOs, String cOperator, String pk_corp, String sZGMode, String sDifferMode, UFDate dCurrDate, String vSettlebillCode)
    throws BusinessException
  {
    IIAToPUBill myService = null;

    BillVO gBillVO = null;
    IBillHeaderVO gHeaderVO = null;
    IBillItemVO[] gBodyVOs = (IBillItemVO[])null;
    Vector vAllBillVO = new Vector();
    boolean bGenBillVO = true;
    try
    {
      UFDouble dSettleNum = null; UFDouble dSettleMny = null; UFDouble dPrice = null; UFDouble dGaugeMny = null;
      Hashtable hashNum = new Hashtable();
      Hashtable hashMny = new Hashtable();
      Hashtable hashPrice = new Hashtable();
      Hashtable hashGuageMny = new Hashtable();
      int iLen = settleItemVOs.length;
      String strKey = null;
      UFDouble dTmp = null;
      for (int i = 0; i < iLen; i++) {
        strKey = settleItemVOs[i].getCvmiid();
        if ((strKey == null) || 
          (settleItemVOs[i].getVbillcode() == null)) continue;
        dSettleNum = settleItemVOs[i].getNsettlenum();
        dSettleMny = settleItemVOs[i].getNmoney();
        dPrice = settleItemVOs[i].getNprice();
        dGaugeMny = settleItemVOs[i].getNgaugemny();
        if (dSettleNum != null) {
          if (hashNum.containsKey(strKey)) {
            dTmp = (UFDouble)hashNum.get(strKey);
            hashNum.put(strKey, dSettleNum.add(dTmp));
          } else {
            hashNum.put(strKey, dSettleNum);
          }
        }
        if (dSettleMny != null) {
          if (hashMny.containsKey(strKey)) {
            dTmp = (UFDouble)hashMny.get(strKey);
            hashMny.put(strKey, dSettleMny.add(dTmp));
          } else {
            hashMny.put(strKey, dSettleMny);
          }
        }
        if (dGaugeMny != null) {
          if (hashGuageMny.containsKey(strKey)) {
            dTmp = (UFDouble)hashGuageMny.get(strKey);
            hashGuageMny.put(strKey, dGaugeMny.add(dTmp));
          } else {
            hashGuageMny.put(strKey, dGaugeMny);
          }
        }
        if (dPrice != null) {
          hashPrice.put(strKey, dPrice);
        }

      }

      Hashtable hashHid = new Hashtable();
      Hashtable hashBid = new Hashtable();
      for (int i = 0; i < iLen; i++) {
        if ((settleItemVOs[i].getCvmiid() == null) || 
          (settleItemVOs[i].getVbillcode() == null)) continue;
        hashHid.put(settleItemVOs[i].getCvmiid(), settleItemVOs[i].getCsettlebillid());
        hashBid.put(settleItemVOs[i].getCvmiid(), settleItemVOs[i].getCsettlebill_bid());
      }

      VMIDMO dmo = new VMIDMO();
      ArrayList listData = dmo.queryInfosForIASettleVMI(vmiVOs, settleItemVOs);

      SettleDMO settleDmo = new SettleDMO();
      String strVMIBusitypeId = settleDmo.queryVMIBusitye(pk_corp);
      IBillItemVO[] bodyVOs = new IBillItemVO[listData.size()];
      IBillHeaderVO[] headVOs = new IBillHeaderVO[listData.size()];
      BillVO[] billVOs = new BillVO[listData.size()];
      VmiSumHeaderVO vmiSumHVO = null;
      ArrayList listTemp = null;
      Integer iPriceMethod = null;
      UFDouble dPlanPrice = null; UFDouble dAssitNum = null;
      UFBoolean bZG = null;

      Hashtable tZG = new Hashtable();

      for (int i = 0; i < bodyVOs.length; i++)
      {
        bodyVOs[i] = new IBillItemVO();
        listTemp = (ArrayList)(ArrayList)listData.get(i);
        vmiSumHVO = (VmiSumHeaderVO)listTemp.get(0);
        iPriceMethod = (Integer)listTemp.get(1);
        dPlanPrice = (UFDouble)listTemp.get(2);

        bodyVOs[i].setPk_corp(vmiSumHVO.getPk_corp());

        if ((vmiSumHVO.getBgaugeflag() != null) && (vmiSumHVO.getBgaugeflag().booleanValue())) {
          tZG.put(vmiSumHVO.getCvmihid(), new UFDouble[] { vmiSumHVO.getNoutnum(), vmiSumHVO.getNmoney() });
        }

        bodyVOs[i].setCbill_bid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setCbillid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setVbillcode(null);

        bodyVOs[i].setCsourcebilltypecode("27");
        bodyVOs[i].setVsourcebillcode(vSettlebillCode);
        bodyVOs[i].setCsourcebillid((String)hashHid.get(vmiSumHVO.getCvmihid()));
        bodyVOs[i].setCsourcebillitemid((String)hashBid.get(vmiSumHVO.getCvmihid()));

        bodyVOs[i].setCfirstbilltypecode("50");
        bodyVOs[i].setVfirstbillcode(vmiSumHVO.getVbillcode());
        bodyVOs[i].setCfirstbillid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setCfirstbillitemid(vmiSumHVO.getCvmihid());

        bodyVOs[i].setCinventoryid(vmiSumHVO.getCinventoryid());

        bodyVOs[i].setFpricemodeflag(iPriceMethod);
        bodyVOs[i].setNplanedprice(dPlanPrice);

        bodyVOs[i].setVbatch(vmiSumHVO.getVlot());
        bodyVOs[i].setIrownumber(new Integer(1));

        bodyVOs[i].setVfree1(vmiSumHVO.getVfree1());
        bodyVOs[i].setVfree2(vmiSumHVO.getVfree2());
        bodyVOs[i].setVfree3(vmiSumHVO.getVfree3());
        bodyVOs[i].setVfree4(vmiSumHVO.getVfree4());
        bodyVOs[i].setVfree5(vmiSumHVO.getVfree5());

        bodyVOs[i].setBlargessflag(new UFBoolean(false));
        bodyVOs[i].setDbizdate(vmiSumHVO.getDsumdate());

        bodyVOs[i].setCprojectid(null);
        bodyVOs[i].setCprojectphase(null);

        headVOs[i] = new IBillHeaderVO();
        headVOs[i].setPk_corp(vmiSumHVO.getPk_corp());
        headVOs[i].setCbillid(vmiSumHVO.getCvmihid());
        headVOs[i].setDbilldate(dCurrDate);
        headVOs[i].setCsourcemodulename("PO");
        headVOs[i].setFdispatchflag(new Integer(0));
        headVOs[i].setCdispatchid(null);

        headVOs[i].setCbiztypeid(strVMIBusitypeId);
        headVOs[i].setCrdcenterid(vmiSumHVO.getCcalbodyid());
        headVOs[i].setCwarehouseid(vmiSumHVO.getCwarehouseid());

        headVOs[i].setCdeptid(null);
        headVOs[i].setCoperatorid(cOperator);
        headVOs[i].setCcustomvendorid(vmiSumHVO.getCvendorid());

        headVOs[i].setVnote(null);
        headVOs[i].setBestimateflag(vmiSumHVO.getBgaugeflag());
        headVOs[i].setCwarehousemanagerid(null);
        headVOs[i].setCemployeeid(null);

        headVOs[i].setVbillcode(null);
        headVOs[i].setBwithdrawalflag(new UFBoolean(false));
        headVOs[i].setBdisableflag(new UFBoolean(false));
        headVOs[i].setBauditedflag(new UFBoolean(false));

        gBillVO = null;
        bGenBillVO = true;

        bZG = vmiSumHVO.getBgaugeflag();
        if (bZG == null) bZG = new UFBoolean(false);
        dSettleNum = (UFDouble)hashNum.get(vmiSumHVO.getCvmihid());
        dSettleMny = (UFDouble)hashMny.get(vmiSumHVO.getCvmihid());
        dPrice = (UFDouble)hashPrice.get(vmiSumHVO.getCvmihid());
        dGaugeMny = (UFDouble)hashGuageMny.get(vmiSumHVO.getCvmihid());
        dAssitNum = (UFDouble)listTemp.get(3);

        if (!bZG.booleanValue()) {
          headVOs[i].setBestimateflag(new UFBoolean(false));

          headVOs[i].setCbilltypecode("I2");
          bodyVOs[i].setCbilltypecode("I2");

          bodyVOs[i].setNnumber(dSettleNum);
          bodyVOs[i].setNmoney(dSettleMny);
          if (dPrice != null) {
            bodyVOs[i].setNprice(dPrice);
          } else if ((dSettleMny != null) && (dSettleNum != null) && (Math.abs(dSettleNum.doubleValue()) >= 0.0D)) {
            double d = dSettleMny.doubleValue() / dSettleNum.doubleValue();
            bodyVOs[i].setNprice(new UFDouble(d));
          }

          bodyVOs[i].setNassistnum((bodyVOs[i].getCastunitid() == null) || (bodyVOs[i].getCastunitid().trim().equals("")) ? null : dAssitNum);
        }
        else {
          headVOs[i].setBestimateflag(new UFBoolean(true));
          if (sZGMode.trim().equals("单到回冲"))
          {
            headVOs[i].setCbilltypecode("I2");
            bodyVOs[i].setCbilltypecode("I2");
            bodyVOs[i].setNnumber(dSettleNum);
            bodyVOs[i].setNmoney(dSettleMny);
            if (dPrice != null) {
              bodyVOs[i].setNprice(dPrice);
            } else if ((dSettleMny != null) && (dSettleNum != null) && (Math.abs(dSettleNum.doubleValue()) >= 0.0D)) {
              double d = dSettleMny.doubleValue() / dSettleNum.doubleValue();
              bodyVOs[i].setNprice(new UFDouble(d));
            }
            bodyVOs[i].setNassistnum((bodyVOs[i].getCastunitid() == null) || (bodyVOs[i].getCastunitid().trim().equals("")) ? null : dAssitNum);
            headVOs[i].setBestimateflag(new UFBoolean(false));
            headVOs[i].setBRedBill(new UFBoolean(false));

            gBillVO = new BillVO();
            gHeaderVO = (IBillHeaderVO)headVOs[i].clone();
            gBodyVOs = new IBillItemVO[] { (IBillItemVO)bodyVOs[i].clone() };
            if (dSettleNum != null)
              gBodyVOs[0].setNnumber(new UFDouble(-dSettleNum.doubleValue()));
            if (dGaugeMny != null)
              gBodyVOs[0].setNmoney(new UFDouble(-dGaugeMny.doubleValue()));
            if ((dGaugeMny != null) && (dSettleNum != null) && (Math.abs(dSettleNum.doubleValue()) >= 0.0D)) {
              double d = dGaugeMny.doubleValue() / dSettleNum.doubleValue();
              gBodyVOs[0].setNprice(new UFDouble(d));
            }
            gBodyVOs[0].setNassistnum((gBodyVOs[0].getCastunitid() == null) || (gBodyVOs[0].getCastunitid().trim().equals("")) ? null : dAssitNum);
            gHeaderVO.setBestimateflag(new UFBoolean(true));
            gHeaderVO.setBRedBill(new UFBoolean(true));
            gBillVO.setParentVO(gHeaderVO);
            gBillVO.setChildrenVO(gBodyVOs);
          }
          else
          {
            bodyVOs[i].setCastunitid(null);
            bodyVOs[i].setNassistnum(null);
            bodyVOs[i].setNSettleNum(dSettleNum);
            bodyVOs[i].setNSettleMny(dSettleMny);

            bodyVOs[i].setCadjustbillid(bodyVOs[i].getCbillid());
            bodyVOs[i].setCadjustbillitemid(bodyVOs[i].getCbill_bid());

            if (sDifferMode.trim().equals("成本"))
            {
              headVOs[i].setCbilltypecode("I9");
              headVOs[i].setBestimateflag(new UFBoolean(false));

              bodyVOs[i].setCbilltypecode("I9");
              bodyVOs[i].setBretractflag(new UFBoolean(false));

              bodyVOs[i].setNnumber(null);
              bodyVOs[i].setNadjustnum(dSettleNum);
              if ((dSettleMny != null) && (dGaugeMny != null)) {
                double d = dSettleMny.doubleValue() - dGaugeMny.doubleValue();
                bodyVOs[i].setNmoney(new UFDouble(d));
              }

              if ((bodyVOs[i].getNmoney() == null) || 
                (Math.abs(bodyVOs[i].getNmoney().doubleValue()) < VMIDMO.getDigitRMB(pk_corp).doubleValue())) {
                bGenBillVO = false;
              }
            }
            else
            {
              headVOs[i].setCbilltypecode("IG");
              headVOs[i].setBestimateflag(new UFBoolean(false));
              bodyVOs[i].setCbilltypecode("IG");

              bodyVOs[i].setNnumber(null);
              bodyVOs[i].setBretractflag(new UFBoolean(false));
              if ((dSettleMny != null) && (dGaugeMny != null)) {
                double d = dSettleMny.doubleValue() - dGaugeMny.doubleValue();
                bodyVOs[i].setNmoney(new UFDouble(d));
              }

              if ((bodyVOs[i].getNmoney() == null) || 
                (Math.abs(bodyVOs[i].getNmoney().doubleValue()) < VMIDMO.getDigitRMB(pk_corp).doubleValue())) {
                bGenBillVO = false;
              }
            }
          }
        }

        billVOs[i] = new BillVO();
        billVOs[i].setParentVO(headVOs[i]);
        billVOs[i].setChildrenVO(new IBillItemVO[] { bodyVOs[i] });

        if (gBillVO != null) {
          vAllBillVO.addElement(gBillVO);
        }
        if (bGenBillVO) {
          vAllBillVO.addElement(billVOs[i]);
        }
      }
      if (vAllBillVO.size() == 0) {
        SCMEnv.out("没有可向存货核算传入的单据，直接返回");
        return;
      }
      billVOs = new BillVO[vAllBillVO.size()];
      vAllBillVO.copyInto(billVOs);
      SCMEnv.out("向存货核算传入的单据数：" + vAllBillVO.size());

      if (tZG.size() > 0) {
        if (sZGMode.trim().equals("单到回冲")) billVOs = crackBillDDHCForVMI(tZG, billVOs); else
          billVOs = crackBillDDBCForVMI(tZG, billVOs);
      }
      if ((billVOs == null) || (billVOs.length == 0)) return;

      myService = (IIAToPUBill)NCLocator.getInstance().lookup(IIAToPUBill.class.getName());
      myService.saveBillFromOutterArray(billVOs, "PO", "27");
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  private void generalBillVOSettle(SettlebillItemVO[] settleItemVOs, SettleVO[] settleVOs, String strOperator, String strPkCorp, String strEstMode, String strDiffMode, UFDate dateLogin, String strBillCode)
    throws BusinessException
  {
    if ((settleVOs == null) || (settleVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[VMI结算数据],直接返回!");
      return;
    }
    IIAToPUBill myService = null;

    BillVO gBillVO = null;
    IBillHeaderVO gHeaderVO = null;
    IBillItemVO[] gBodyVOs = (IBillItemVO[])null;
    Vector vAllBillVO = new Vector();
    boolean bGenBillVO = true;
    try
    {
      UFDouble dSettleNum = null; UFDouble dSettleMny = null; UFDouble dPrice = null; UFDouble dGaugeMny = null;
      Hashtable hashNum = new Hashtable();
      Hashtable hashMny = new Hashtable();
      Hashtable hashPrice = new Hashtable();
      Hashtable hashGuageMny = new Hashtable();
      int iLen = settleItemVOs.length;
      String strKey = null;
      UFDouble dTmp = null;
      for (int i = 0; i < iLen; i++) {
        strKey = settleItemVOs[i].getCvmiid();
        if (strKey == null)
          continue;
        if (settleItemVOs[i].getVbillcode() == null)
          continue;
        dSettleNum = settleItemVOs[i].getNsettlenum();
        dSettleMny = settleItemVOs[i].getNmoney();
        dPrice = settleItemVOs[i].getNprice();
        dGaugeMny = settleItemVOs[i].getNgaugemny();
        if (dSettleNum != null) {
          if (hashNum.containsKey(strKey)) {
            dTmp = (UFDouble)hashNum.get(strKey);
            hashNum.put(strKey, dSettleNum.add(dTmp));
          } else {
            hashNum.put(strKey, dSettleNum);
          }
        }
        if (dSettleMny != null) {
          if (hashMny.containsKey(strKey)) {
            dTmp = (UFDouble)hashMny.get(strKey);
            hashMny.put(strKey, dSettleMny.add(dTmp));
          } else {
            hashMny.put(strKey, dSettleMny);
          }
        }
        if (dGaugeMny != null) {
          if (hashGuageMny.containsKey(strKey)) {
            dTmp = (UFDouble)hashGuageMny.get(strKey);
            hashGuageMny.put(strKey, dGaugeMny.add(dTmp));
          } else {
            hashGuageMny.put(strKey, dGaugeMny);
          }
        }
        if (dPrice != null) {
          hashPrice.put(strKey, dPrice);
        }

      }

      Hashtable hashHid = new Hashtable();
      Hashtable hashBid = new Hashtable();
      for (int i = 0; i < iLen; i++) {
        if (settleItemVOs[i].getCvmiid() == null)
          continue;
        if (settleItemVOs[i].getVbillcode() == null)
          continue;
        hashHid.put(settleItemVOs[i].getCvmiid(), settleItemVOs[i].getCsettlebillid());
        hashBid.put(settleItemVOs[i].getCvmiid(), settleItemVOs[i].getCsettlebill_bid());
      }

      VMIDMO dmo = new VMIDMO();
      ArrayList listData = dmo.queryInfosForIASettle(settleVOs);

      SettleDMO settleDmo = new SettleDMO();
      String strVMIBusitypeId = settleDmo.queryVMIBusitye(strPkCorp);
      IBillItemVO[] bodyVOs = new IBillItemVO[listData.size()];
      IBillHeaderVO[] headVOs = new IBillHeaderVO[listData.size()];
      BillVO[] billVOs = new BillVO[listData.size()];
      VmiSumHeaderVO vmiSumHVO = null;
      ArrayList listTemp = null;
      Integer iPriceMethod = null;
      UFDouble dPlanPrice = null; UFDouble dAssitNum = null;
      UFBoolean bZG = null;

      Hashtable tZG = new Hashtable();

      for (int i = 0; i < bodyVOs.length; i++)
      {
        bodyVOs[i] = new IBillItemVO();
        listTemp = (ArrayList)(ArrayList)listData.get(i);
        vmiSumHVO = (VmiSumHeaderVO)listTemp.get(0);
        iPriceMethod = (Integer)listTemp.get(1);
        dPlanPrice = (UFDouble)listTemp.get(2);

        bodyVOs[i].setPk_corp(vmiSumHVO.getPk_corp());

        if ((vmiSumHVO.getBgaugeflag() != null) && (vmiSumHVO.getBgaugeflag().booleanValue())) {
          tZG.put(vmiSumHVO.getCvmihid(), new UFDouble[] { vmiSumHVO.getNoutnum(), vmiSumHVO.getNmoney() });
        }

        bodyVOs[i].setCbill_bid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setCbillid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setVbillcode(null);

        bodyVOs[i].setCsourcebilltypecode("27");
        bodyVOs[i].setVsourcebillcode(strBillCode);
        bodyVOs[i].setCsourcebillid((String)hashHid.get(vmiSumHVO.getCvmihid()));
        bodyVOs[i].setCsourcebillitemid((String)hashBid.get(vmiSumHVO.getCvmihid()));

        bodyVOs[i].setCfirstbilltypecode("50");
        bodyVOs[i].setVfirstbillcode(vmiSumHVO.getVbillcode());
        bodyVOs[i].setCfirstbillid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setCfirstbillitemid(vmiSumHVO.getCvmihid());

        bodyVOs[i].setCinventoryid(vmiSumHVO.getCinventoryid());

        bodyVOs[i].setFpricemodeflag(iPriceMethod);
        bodyVOs[i].setNplanedprice(dPlanPrice);

        bodyVOs[i].setVbatch(vmiSumHVO.getVlot());
        bodyVOs[i].setIrownumber(new Integer(1));

        bodyVOs[i].setVfree1(vmiSumHVO.getVfree1());
        bodyVOs[i].setVfree2(vmiSumHVO.getVfree2());
        bodyVOs[i].setVfree3(vmiSumHVO.getVfree3());
        bodyVOs[i].setVfree4(vmiSumHVO.getVfree4());
        bodyVOs[i].setVfree5(vmiSumHVO.getVfree5());

        bodyVOs[i].setBlargessflag(new UFBoolean(false));
        bodyVOs[i].setDbizdate(vmiSumHVO.getDsumdate());

        bodyVOs[i].setCprojectid(null);
        bodyVOs[i].setCprojectphase(null);

        headVOs[i] = new IBillHeaderVO();
        headVOs[i].setPk_corp(vmiSumHVO.getPk_corp());
        headVOs[i].setCbillid(vmiSumHVO.getCvmihid());
        headVOs[i].setDbilldate(dateLogin);
        headVOs[i].setCsourcemodulename("PO");
        headVOs[i].setFdispatchflag(new Integer(0));
        headVOs[i].setCdispatchid(null);

        headVOs[i].setCbiztypeid(strVMIBusitypeId);
        headVOs[i].setCrdcenterid(vmiSumHVO.getCcalbodyid());
        headVOs[i].setCwarehouseid(vmiSumHVO.getCwarehouseid());

        headVOs[i].setCdeptid(null);
        headVOs[i].setCoperatorid(strOperator);
        headVOs[i].setCcustomvendorid(vmiSumHVO.getCvendorid());

        headVOs[i].setVnote(null);
        headVOs[i].setBestimateflag(vmiSumHVO.getBgaugeflag());
        headVOs[i].setCwarehousemanagerid(null);
        headVOs[i].setCemployeeid(null);

        headVOs[i].setVbillcode(null);
        headVOs[i].setBwithdrawalflag(new UFBoolean(false));
        headVOs[i].setBdisableflag(new UFBoolean(false));
        headVOs[i].setBauditedflag(new UFBoolean(false));

        gBillVO = null;
        bGenBillVO = true;

        bZG = vmiSumHVO.getBgaugeflag();
        if (bZG == null)
          bZG = new UFBoolean(false);
        dSettleNum = (UFDouble)hashNum.get(vmiSumHVO.getCvmihid());
        dSettleMny = (UFDouble)hashMny.get(vmiSumHVO.getCvmihid());
        dPrice = (UFDouble)hashPrice.get(vmiSumHVO.getCvmihid());
        dGaugeMny = (UFDouble)hashGuageMny.get(vmiSumHVO.getCvmihid());
        dAssitNum = (UFDouble)listTemp.get(3);

        if (!bZG.booleanValue()) {
          headVOs[i].setBestimateflag(new UFBoolean(false));

          headVOs[i].setCbilltypecode("I2");
          bodyVOs[i].setCbilltypecode("I2");

          bodyVOs[i].setNnumber(dSettleNum);
          bodyVOs[i].setNmoney(dSettleMny);
          if (dPrice != null) {
            bodyVOs[i].setNprice(dPrice);
          } else if ((dSettleMny != null) && (dSettleNum != null) && (Math.abs(dSettleNum.doubleValue()) >= 0.0D)) {
            double d = dSettleMny.doubleValue() / dSettleNum.doubleValue();
            bodyVOs[i].setNprice(new UFDouble(d));
          }

          bodyVOs[i].setNassistnum((bodyVOs[i].getCastunitid() == null) || (bodyVOs[i].getCastunitid().trim().equals("")) ? null : dAssitNum);
        }
        else {
          headVOs[i].setBestimateflag(new UFBoolean(true));
          if (strEstMode.trim().equals("单到回冲"))
          {
            headVOs[i].setCbilltypecode("I2");
            bodyVOs[i].setCbilltypecode("I2");
            bodyVOs[i].setNnumber(dSettleNum);
            bodyVOs[i].setNmoney(dSettleMny);
            if (dPrice != null) {
              bodyVOs[i].setNprice(dPrice);
            } else if ((dSettleMny != null) && (dSettleNum != null) && (Math.abs(dSettleNum.doubleValue()) >= 0.0D)) {
              double d = dSettleMny.doubleValue() / dSettleNum.doubleValue();
              bodyVOs[i].setNprice(new UFDouble(d));
            }
            bodyVOs[i].setNassistnum((bodyVOs[i].getCastunitid() == null) || (bodyVOs[i].getCastunitid().trim().equals("")) ? null : dAssitNum);
            headVOs[i].setBestimateflag(new UFBoolean(false));
            headVOs[i].setBRedBill(new UFBoolean(false));

            gBillVO = new BillVO();
            gHeaderVO = (IBillHeaderVO)headVOs[i].clone();
            gBodyVOs = new IBillItemVO[] { (IBillItemVO)bodyVOs[i].clone() };
            if (dSettleNum != null)
              gBodyVOs[0].setNnumber(new UFDouble(-dSettleNum.doubleValue()));
            if (dGaugeMny != null)
              gBodyVOs[0].setNmoney(new UFDouble(-dGaugeMny.doubleValue()));
            if ((dGaugeMny != null) && (dSettleNum != null) && (Math.abs(dSettleNum.doubleValue()) >= 0.0D)) {
              double d = dGaugeMny.doubleValue() / dSettleNum.doubleValue();
              gBodyVOs[0].setNprice(new UFDouble(d));
            }
            gBodyVOs[0].setNassistnum((gBodyVOs[0].getCastunitid() == null) || (gBodyVOs[0].getCastunitid().trim().equals("")) ? null : dAssitNum);
            gHeaderVO.setBestimateflag(new UFBoolean(true));
            gHeaderVO.setBRedBill(new UFBoolean(true));
            gBillVO.setParentVO(gHeaderVO);
            gBillVO.setChildrenVO(gBodyVOs);
          }
          else
          {
            bodyVOs[i].setCastunitid(null);
            bodyVOs[i].setNassistnum(null);
            bodyVOs[i].setNSettleNum(dSettleNum);
            bodyVOs[i].setNSettleMny(dSettleMny);

            bodyVOs[i].setCadjustbillid(bodyVOs[i].getCbillid());
            bodyVOs[i].setCadjustbillitemid(bodyVOs[i].getCbill_bid());

            if (strDiffMode.trim().equals("成本"))
            {
              headVOs[i].setCbilltypecode("I9");
              headVOs[i].setBestimateflag(new UFBoolean(false));

              bodyVOs[i].setCbilltypecode("I9");
              bodyVOs[i].setBretractflag(new UFBoolean(false));

              bodyVOs[i].setNnumber(null);
              bodyVOs[i].setNadjustnum(dSettleNum);
              if ((dSettleMny != null) && (dGaugeMny != null)) {
                double d = dSettleMny.doubleValue() - dGaugeMny.doubleValue();
                bodyVOs[i].setNmoney(new UFDouble(d));
              }

              if ((bodyVOs[i].getNmoney() == null) || 
                (Math.abs(bodyVOs[i].getNmoney().doubleValue()) < VMIDMO.getDigitRMB(strPkCorp).doubleValue())) {
                bGenBillVO = false;
              }
            }
            else
            {
              headVOs[i].setCbilltypecode("IG");
              headVOs[i].setBestimateflag(new UFBoolean(false));
              bodyVOs[i].setCbilltypecode("IG");

              bodyVOs[i].setNnumber(null);
              bodyVOs[i].setBretractflag(new UFBoolean(false));
              if ((dSettleMny != null) && (dGaugeMny != null)) {
                double d = dSettleMny.doubleValue() - dGaugeMny.doubleValue();
                bodyVOs[i].setNmoney(new UFDouble(d));
              }

              if ((bodyVOs[i].getNmoney() == null) || 
                (Math.abs(bodyVOs[i].getNmoney().doubleValue()) < VMIDMO.getDigitRMB(strPkCorp).doubleValue())) {
                bGenBillVO = false;
              }
            }
          }
        }

        billVOs[i] = new BillVO();
        billVOs[i].setParentVO(headVOs[i]);
        billVOs[i].setChildrenVO(new IBillItemVO[] { bodyVOs[i] });

        if (gBillVO != null) {
          vAllBillVO.addElement(gBillVO);
        }
        if (bGenBillVO) {
          vAllBillVO.addElement(billVOs[i]);
        }
      }
      if (vAllBillVO.size() == 0) {
        SCMEnv.out("没有可向存货核算传入的单据，直接返回");
        return;
      }
      billVOs = new BillVO[vAllBillVO.size()];
      vAllBillVO.copyInto(billVOs);
      SCMEnv.out("向存货核算传入的单据数：" + vAllBillVO.size());

      if (tZG.size() > 0) {
        if (strEstMode.trim().equals("单到回冲")) billVOs = crackBillDDHCForVMI(tZG, billVOs); else
          billVOs = crackBillDDBCForVMI(tZG, billVOs);
      }
      if ((billVOs == null) || (billVOs.length == 0)) return;

      myService = (IIAToPUBill)NCLocator.getInstance().lookup(IIAToPUBill.class.getName());
      myService.saveBillFromOutterArray(billVOs, "PO", "27");
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  private void generalBillVOSettleFee(SettlebillItemVO[] settleItemVOs, SettleVO[] settleVOs, String strOperator, String strPkCorp, String strDiffMode, UFDate dateLogin, String strBillCode)
    throws BusinessException
  {
    if ((settleVOs == null) || (settleVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[VMI费用结算数据],直接返回!");
      return;
    }

    IIAToPUBill myService = null;
    Vector vAllBillVO = new Vector();
    try
    {
      UFDouble dSettleMny = null;
      Hashtable hashMny = new Hashtable();
      int iLen = settleItemVOs.length;
      String strKey = null;
      UFDouble dTmp = null;
      for (int i = 0; i < iLen; i++) {
        strKey = settleItemVOs[i].getCvmiid();
        if (strKey == null) {
          continue;
        }
        if ((settleItemVOs[i].getVbillcode() == null) || (settleItemVOs[i].getVbillcode().trim().length() == 0))
          continue;
        dSettleMny = settleItemVOs[i].getNmoney();
        if (dSettleMny != null) {
          if (hashMny.containsKey(strKey)) {
            dTmp = (UFDouble)hashMny.get(strKey);
            hashMny.put(strKey, dSettleMny.add(dTmp));
          } else {
            hashMny.put(strKey, dSettleMny);
          }

        }

      }

      Hashtable hashHid = new Hashtable();
      Hashtable hashBid = new Hashtable();
      for (int i = 0; i < iLen; i++) {
        if (settleItemVOs[i].getCvmiid() == null)
          continue;
        hashHid.put(settleItemVOs[i].getCvmiid(), settleItemVOs[i].getCsettlebillid());
        hashBid.put(settleItemVOs[i].getCvmiid(), settleItemVOs[i].getCsettlebill_bid());
      }

      VMIDMO dmo = new VMIDMO();
      ArrayList listData = dmo.queryInfosForIASettle(settleVOs);

      SettleDMO settleDmo = new SettleDMO();
      String strVMIBusitypeId = settleDmo.queryVMIBusitye(strPkCorp);
      IBillItemVO[] bodyVOs = new IBillItemVO[listData.size()];
      IBillHeaderVO[] headVOs = new IBillHeaderVO[listData.size()];
      BillVO[] billVOs = new BillVO[listData.size()];
      VmiSumHeaderVO vmiSumHVO = null;
      ArrayList listTemp = null;
      Integer iPriceMethod = null;
      UFDouble dPlanPrice = null;

      for (int i = 0; i < bodyVOs.length; i++)
      {
        bodyVOs[i] = new IBillItemVO();
        listTemp = (ArrayList)(ArrayList)listData.get(i);
        vmiSumHVO = (VmiSumHeaderVO)listTemp.get(0);
        iPriceMethod = (Integer)listTemp.get(1);
        dPlanPrice = (UFDouble)listTemp.get(2);

        bodyVOs[i].setPk_corp(vmiSumHVO.getPk_corp());

        bodyVOs[i].setCbill_bid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setCbillid(vmiSumHVO.getCvmihid());

        bodyVOs[i].setCsourcebilltypecode("27");
        bodyVOs[i].setVsourcebillcode(strBillCode);
        bodyVOs[i].setCsourcebillid((String)hashHid.get(vmiSumHVO.getCvmihid()));
        bodyVOs[i].setCsourcebillitemid((String)hashBid.get(vmiSumHVO.getCvmihid()));

        bodyVOs[i].setCfirstbilltypecode("50");
        bodyVOs[i].setVfirstbillcode(vmiSumHVO.getVbillcode());
        bodyVOs[i].setCfirstbillid(vmiSumHVO.getCvmihid());
        bodyVOs[i].setCfirstbillitemid(vmiSumHVO.getCvmihid());

        bodyVOs[i].setCinventoryid(vmiSumHVO.getCinventoryid());

        bodyVOs[i].setFpricemodeflag(iPriceMethod);
        bodyVOs[i].setNplanedprice(dPlanPrice);

        bodyVOs[i].setVbatch(vmiSumHVO.getVlot());

        bodyVOs[i].setIrownumber(new Integer(1));

        bodyVOs[i].setVfree1(vmiSumHVO.getVfree1());
        bodyVOs[i].setVfree2(vmiSumHVO.getVfree2());
        bodyVOs[i].setVfree3(vmiSumHVO.getVfree3());
        bodyVOs[i].setVfree4(vmiSumHVO.getVfree4());
        bodyVOs[i].setVfree5(vmiSumHVO.getVfree5());

        bodyVOs[i].setBlargessflag(new UFBoolean(false));
        bodyVOs[i].setDbizdate(vmiSumHVO.getDsumdate());
        bodyVOs[i].setCprojectid(null);
        bodyVOs[i].setCprojectphase(null);

        bodyVOs[i].setCadjustbillid(bodyVOs[i].getCbillid());
        bodyVOs[i].setCadjustbillitemid(bodyVOs[i].getCbill_bid());

        headVOs[i] = new IBillHeaderVO();
        headVOs[i].setPk_corp(vmiSumHVO.getPk_corp());
        headVOs[i].setCbillid(vmiSumHVO.getCvmihid());
        headVOs[i].setDbilldate(dateLogin);
        headVOs[i].setCsourcemodulename("PO");
        headVOs[i].setFdispatchflag(new Integer(0));
        headVOs[i].setCdispatchid(null);

        headVOs[i].setCbiztypeid(strVMIBusitypeId);
        headVOs[i].setCrdcenterid(vmiSumHVO.getCcalbodyid());
        headVOs[i].setCwarehouseid(vmiSumHVO.getCwarehouseid());

        headVOs[i].setCdeptid(null);
        headVOs[i].setCoperatorid(strOperator);
        headVOs[i].setCcustomvendorid(vmiSumHVO.getCvendorid());

        headVOs[i].setVnote(null);
        headVOs[i].setBestimateflag(vmiSumHVO.getBgaugeflag());
        headVOs[i].setCwarehousemanagerid(null);
        headVOs[i].setCemployeeid(null);

        headVOs[i].setVbillcode(null);
        headVOs[i].setBwithdrawalflag(new UFBoolean(false));
        headVOs[i].setBdisableflag(new UFBoolean(false));
        headVOs[i].setBauditedflag(new UFBoolean(false));

        dSettleMny = (UFDouble)hashMny.get(vmiSumHVO.getCvmihid());

        bodyVOs[i].setCastunitid(null);
        bodyVOs[i].setNassistnum(null);
        bodyVOs[i].setNnumber(null);
        bodyVOs[i].setNmoney(dSettleMny);
        if (strDiffMode.trim().equals("成本"))
        {
          headVOs[i].setCbilltypecode("I9");
          headVOs[i].setBestimateflag(new UFBoolean(false));

          IBillItemVO[] itemVO = new SettleImpl().rearrangeBillItemVOForFee(bodyVOs[i], settleItemVOs[i], true, vmiSumHVO.getBgaugeflag());
          if ((itemVO != null) && (itemVO.length > 0)) {
            for (int j = 0; j < itemVO.length; j++) {
              itemVO[j].setCbilltypecode("I9");
              itemVO[j].setNnumber(null);
              itemVO[j].setBretractflag(new UFBoolean(true));
            }

            billVOs[i] = new BillVO();
            billVOs[i].setParentVO(headVOs[i]);
            billVOs[i].setChildrenVO(itemVO);

            boolean bGenBillVO = true;
            for (int j = 0; j < itemVO.length; j++) {
              if ((itemVO[j].getNmoney() == null) || (Math.abs(itemVO[j].getNmoney().doubleValue()) < VMIDMO.getDigitRMB(strPkCorp).doubleValue())) {
                bGenBillVO = false;
                break;
              }
            }
            if (bGenBillVO) {
              vAllBillVO.addElement(billVOs[i]);
            }
          }
        }
        else
        {
          headVOs[i].setCbilltypecode("IG");
          headVOs[i].setBestimateflag(new UFBoolean(false));
          bodyVOs[i].setCbilltypecode("IG");
          bodyVOs[i].setBretractflag(new UFBoolean(true));

          if ((bodyVOs[i].getNmoney() == null) || (Math.abs(bodyVOs[i].getNmoney().doubleValue()) < VMIDMO.getDigitRMB(strPkCorp).doubleValue()))
            continue;
          billVOs[i] = new BillVO();
          billVOs[i].setParentVO(headVOs[i]);
          billVOs[i].setChildrenVO(new IBillItemVO[] { bodyVOs[i] });
          vAllBillVO.addElement(billVOs[i]);
        }
      }

      if (vAllBillVO.size() == 0) {
        SCMEnv.out("没有可向存货核算传入的单据，直接返回");
        return;
      }
      billVOs = new BillVO[vAllBillVO.size()];
      vAllBillVO.copyInto(billVOs);
      SCMEnv.out("向存货核算传入的单据数：" + vAllBillVO.size());

      for (int i = 0; i < billVOs.length; i++) {
        IBillItemVO[] bodyVO = (IBillItemVO[])billVOs[i].getChildrenVO();
        for (int j = 0; j < bodyVO.length; j++) {
          String s = bodyVO[j].getCadjustbillid();
          String str1 = bodyVO[j].getCadjustbillitemid();
        }

      }

      myService = (IIAToPUBill)NCLocator.getInstance().lookup(IIAToPUBill.class.getName());
      myService.saveBillFromOutterArray(billVOs, "PO", "27");
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  private String[] getAllLockKeysFee(SettleVO[] settleVOs, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs, FeeinvoiceVO[] specialVOs)
    throws BusinessException
  {
    String[] saKey = (String[])null;
    Vector vTmp = new Vector();
    int iLen = 0;
    if ((settleVOs != null) && (settleVOs.length > 0)) {
      iLen = settleVOs.length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(settleVOs[i].getCvmihid());
      }
    }
    if ((feeVOs != null) && (feeVOs.length > 0)) {
      iLen = feeVOs.length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(feeVOs[i].getCinvoice_bid());
        if (!vTmp.contains(feeVOs[i].getCinvoiceid()))
          vTmp.addElement(feeVOs[i].getCinvoiceid());
      }
    }
    if ((discountVOs != null) && (discountVOs.length > 0)) {
      iLen = discountVOs.length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(discountVOs[i].getCinvoice_bid());
        if (!vTmp.contains(discountVOs[i].getCinvoiceid()))
          vTmp.addElement(discountVOs[i].getCinvoiceid());
      }
    }
    if ((specialVOs != null) && (specialVOs.length > 0)) {
      iLen = specialVOs.length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(specialVOs[i].getCinvoice_bid());
        if (!vTmp.contains(specialVOs[i].getCinvoiceid())) {
          vTmp.addElement(specialVOs[i].getCinvoiceid());
        }
      }
    }
    if (vTmp.size() > 0) {
      saKey = new String[vTmp.size()];
      vTmp.copyInto(saKey);
    }
    return saKey;
  }

  private String[] getAllLockKeysManual(SettleVO[] settleVOs, IinvoiceVO[] iinvoiceVOs, FeeinvoiceVO[] feeVOs, FeeinvoiceVO[] discountVOs)
    throws BusinessException
  {
    String[] saKey = (String[])null;
    Vector vTmp = new Vector();
    int iLen = 0;
    if ((settleVOs != null) && (settleVOs.length > 0)) {
      iLen = settleVOs.length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(settleVOs[i].getCvmihid());
      }
    }
    if ((iinvoiceVOs != null) && (iinvoiceVOs.length > 0)) {
      iLen = iinvoiceVOs.length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(iinvoiceVOs[i].getCinvoice_bid());
        if (!vTmp.contains(iinvoiceVOs[i].getCinvoiceid()))
          vTmp.addElement(iinvoiceVOs[i].getCinvoiceid());
      }
    }
    if ((feeVOs != null) && (feeVOs.length > 0)) {
      iLen = feeVOs.length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(feeVOs[i].getCinvoice_bid());
        if (!vTmp.contains(feeVOs[i].getCinvoiceid()))
          vTmp.addElement(feeVOs[i].getCinvoiceid());
      }
    }
    if ((discountVOs != null) && (discountVOs.length > 0)) {
      iLen = discountVOs.length;
      for (int i = 0; i < iLen; i++) {
        vTmp.addElement(discountVOs[i].getCinvoice_bid());
        if (!vTmp.contains(discountVOs[i].getCinvoiceid())) {
          vTmp.addElement(discountVOs[i].getCinvoiceid());
        }
      }
    }
    if (vTmp.size() > 0) {
      saKey = new String[vTmp.size()];
      vTmp.copyInto(saKey);
    }
    return saKey;
  }

  private String getReplacedSQL(String sSource, String sOld, String sReplace)
  {
    if ((sReplace == null) || (sReplace.trim().length() == 0))
      return sSource;
    int nStart = sSource.indexOf(sOld);
    if (nStart < 0)
      return sSource;
    int nMiddle = sSource.indexOf("'", nStart + 1);
    if (nMiddle < 0)
      return sSource;
    int nEnd = sSource.indexOf("'", nMiddle + 1);

    String s1 = sSource.substring(0, nStart);
    String s2 = sSource.substring(nEnd + 1);

    String s = s1 + sReplace + s2;

    return s;
  }

  public ArrayList getSUnitWeightAndVolume(String[] saBaseId)
    throws BusinessException
  {
    if ((saBaseId == null) || (saBaseId.length <= 0)) {
      SCMEnv.out("传入参数为空，直接返回 NULL !");
      return null;
    }
    FormulaParse f = new FormulaParse();

    String sExpress1 = "getColValue(bd_invbasdoc,unitweight,pk_invbasdoc,cBaseid)";

    String sExpress2 = "getColValue(bd_invbasdoc,unitvolume,pk_invbasdoc,cBaseid)";

    f.setExpressArray(new String[] { sExpress1, sExpress2 });

    VarryVO[] varry = f.getVarryArray();

    f.addVariable(varry[0].getVarry()[0], saBaseId);
    f.addVariable(varry[1].getVarry()[0], saBaseId);

    String[] sUnitWeight = f.getValueSArray()[0];
    String[] sUnitVolume = f.getValueSArray()[1];

    ArrayList listRet = new ArrayList();
    for (int i = 0; i < saBaseId.length; i++) {
      ArrayList listTmp = new ArrayList();
      listTmp.add(sUnitWeight[i]);
      listTmp.add(sUnitVolume[i]);
      listRet.add(listTmp);
    }

    return listRet;
  }

  private ArrayList dealCondVosForPower(ConditionVO[] voaCond)
  {
    ArrayList listUserInputVos = new ArrayList();
    ArrayList listPowerVos = new ArrayList();

    int iLen = voaCond.length;

    for (int i = 0; i < iLen; i++) {
      if ((voaCond[i].getOperaCode().trim().equalsIgnoreCase("IS")) && (voaCond[i].getValue().trim().equalsIgnoreCase("NULL"))) {
        listPowerVos.add(voaCond[i]);
        i++;
        listPowerVos.add(voaCond[i]);
      } else {
        listUserInputVos.add(voaCond[i]);
      }

    }

    ArrayList listRet = new ArrayList();

    ConditionVO[] voaCondUserInput = (ConditionVO[])null;
    if (listUserInputVos.size() > 0) {
      voaCondUserInput = new ConditionVO[listUserInputVos.size()];
      listUserInputVos.toArray(voaCondUserInput);
    }
    listRet.add(voaCondUserInput);

    ConditionVO[] voaCondPower = (ConditionVO[])null;
    if (listPowerVos.size() > 0) {
      voaCondPower = new ConditionVO[listPowerVos.size()];
      listPowerVos.toArray(voaCondPower);
      String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cvendorbaseid", "cvendorid");

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cbaseid", "cinventoryid");

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cinvclassid is", "cinventoryid is");
      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cinvclassid in (", "cinventoryid in (select pk_invmandoc from bd_invbasdoc A, bd_invmandoc B, bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and C.pk_invcl in");

      listRet.add(strPowerWherePart);
    } else {
      listRet.add(null);
    }

    return listRet;
  }

  private ArrayList dealCondVosForPowerForManual(ConditionVO[] voaCond)
  {
    ArrayList listUserInputVos = new ArrayList();
    ArrayList listPowerVos = new ArrayList();

    int iLen = voaCond.length;

    for (int i = 0; i < iLen; i++) {
      if ((voaCond[i].getOperaCode().trim().equalsIgnoreCase("IS")) && (voaCond[i].getValue().trim().equalsIgnoreCase("NULL"))) {
        listPowerVos.add(voaCond[i]);
        i++;
        listPowerVos.add(voaCond[i]);
      } else {
        listUserInputVos.add(voaCond[i]);
      }

    }

    ArrayList listRet = new ArrayList();

    ConditionVO[] voaCondUserInput = (ConditionVO[])null;
    if (listUserInputVos.size() > 0) {
      voaCondUserInput = new ConditionVO[listUserInputVos.size()];
      listUserInputVos.toArray(voaCondUserInput);
    }
    listRet.add(voaCondUserInput);

    ConditionVO[] voaCondPower = (ConditionVO[])null;
    if (listPowerVos.size() > 0) {
      voaCondPower = new ConditionVO[listPowerVos.size()];
      listPowerVos.toArray(voaCondPower);
      String strPowerWherePart = voaCondPower[0].getWhereSQL(voaCondPower);

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "custcode", "pk_cumandoc");
      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cvendorbaseid", "cvendorid");

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "storcode", "pk_stordoc");

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "invcode", "pk_invmandoc");
      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cbaseid", "cinventoryid");

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "cinvclassid", "cinventoryid");
      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "select invclasscode from bd_invcl where 0=0  and pk_invcl", "select pk_invmandoc from bd_invbasdoc A, bd_invmandoc B, bd_invcl C where A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and C.pk_invcl");

      strPowerWherePart = StringUtil.replaceAllString(strPowerWherePart, "bodycode", "pk_calbody");
      listRet.add(strPowerWherePart);
    } else {
      listRet.add(null);
    }

    return listRet;
  }

  public EstimateVO[] queryVMIEsti(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() <= 2)) {
      SCMEnv.out("程序BUG：VMI汇总查询参数传递不正确!直接返回 NULL");
      return null;
    }
    String sUnitCode = (String)listPara.get(0);
    ConditionVO[] conditionVO = (ConditionVO[])listPara.get(1);
    String sZG = (String)listPara.get(2);

    VmiSumVO[] vmiVOs = (VmiSumVO[])null;
    EstimateVO[] estiVOs = (EstimateVO[])null;
    try
    {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";

      ArrayList listRet = dealCondVosForPower(conditionVO);
      conditionVO = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      int iCnt = conditionVO == null ? 0 : conditionVO.length;
      for (int i = 0; i < iCnt; i++) {
        String sName = conditionVO[i].getFieldCode().trim();
        String sOpera = conditionVO[i].getOperaCode().trim();
        String sValue = conditionVO[i].getRefResult() == null ? "ErrorPkMarkedByCzp" : conditionVO[i].getRefResult().getRefPK();
        String sSQL = conditionVO[i].getSQLStr();
        String sReplace = null;

        if ((sName.equals("cbaseid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "cinventoryid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("dsumdate")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "dsumdate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && (sValue.length() > 0)) {
          String[] sClassID = dmo.getSubInvClassID(sValue);

          if ((sClassID != null) && (sClassID.length > 0)) {
            sValue = "(";
            for (int j = 0; j < sClassID.length; j++) {
              if (j < sClassID.length - 1)
                sValue = sValue + "C.pk_invcl " + sOpera + " '" + sClassID[j] + "' or ";
              else
                sValue = sValue + "C.pk_invcl " + sOpera + " '" + sClassID[j] + "')";
            }
            sReplace = 
              "cinventoryid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + 
              sUnitCode + 
              "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + 
              sValue + 
              ")";
          } else {
            sReplace = 
              "cinventoryid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + 
              sUnitCode + 
              "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and C.pk_invcl " + 
              sOpera + 
              " '" + 
              sValue + 
              "')";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "cvendorid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("ccalbodyid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "ccalbodyid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cwarehouseid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "cwarehouseid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }
      sCondition = PubDMO.processFirst(sCondition);
      if ((sCondition == null) || (sCondition.trim().length() == 0)) {
        sCondition = "";
      }
      sCondition = sCondition + " and coalesce(bgaugeflag,'N') = '" + sZG + "' ";

      sCondition = sCondition + "and coalesce(naccountnum,0) = 0 ";

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      IICToPU_VmiSumDMO vmiDMO = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());
      vmiVOs = (VmiSumVO[])vmiDMO.getOutDetail(sUnitCode, sCondition);

      VmiSumHeaderVO head = null;
      String strZGPriceMode = "";
      PubDMO getPriceDmo = null;
      if ((vmiVOs != null) && (vmiVOs.length > 0)) {
        ArrayList listTmp = new ArrayList();
        int iLen = vmiVOs.length;
        EstimateVO estiVOTmp = null;
        UFDouble d1 = null; UFDouble d2 = null;
        for (int i = 0; i < iLen; i++) {
          if (vmiVOs[i] == null)
            continue;
          if (vmiVOs[i].getParentVO() == null)
            continue;
          head = (VmiSumHeaderVO)vmiVOs[i].getParentVO();
          estiVOTmp = new EstimateVO();

          estiVOTmp.setCvmihid(head.getCvmihid());
          estiVOTmp.setCbillcode(head.getVbillcode());
          estiVOTmp.setCcalbodyid(head.getCcalbodyid());
          estiVOTmp.setCvendormangid(head.getCvendorid());
          estiVOTmp.setCwarehouseid(head.getCwarehouseid());
          estiVOTmp.setCmangid(head.getCinventoryid());
          estiVOTmp.setTs(head.getTs());
          estiVOTmp.setPk_corp(sUnitCode);

          d1 = new UFDouble(0);
          if (head.getNoutnum() != null) d1 = head.getNoutnum();
          d2 = new UFDouble(0);
          if (head.getNoutinnum() != null) d2 = head.getNoutinnum();
          d1 = d1.sub(d2);
          d2 = new UFDouble(0);
          if (head.getNprice() != null) d2 = head.getNprice();

          estiVOTmp.setNestinum(d1);
          estiVOTmp.setNestiprice(d2);
          estiVOTmp.setNestimoney(d1.multiply(d2));

          listTmp.add(estiVOTmp);
        }
        if (listTmp.size() > 0) {
          estiVOs = new EstimateVO[listTmp.size()];
          listTmp.toArray(estiVOs);
        }

        if ("N".equalsIgnoreCase(sZG)) {
          getPriceDmo = new PubDMO();
          strZGPriceMode = getPriceDmo.fetchSysPara(sUnitCode, "PO27");
          if (strZGPriceMode == null) {
            strZGPriceMode = "订单价";
          }
          VMIDMO vmiDmo = new VMIDMO();
          estiVOs = vmiDmo.replacePriceForEstimate(estiVOs, strZGPriceMode, sUnitCode);
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    return estiVOs;
  }

  public SettleVO[] queryVMISettleFee(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 2)) {
      SCMEnv.out("程序BUG：查询已经结算过的VMI汇总方法参数传递不正确!直接返回 NULL");
      return null;
    }
    String sUnitCode = (String)listPara.get(0);
    ConditionVO[] conditionVO = (ConditionVO[])listPara.get(1);

    VmiSumVO[] vmiVOs = (VmiSumVO[])null;
    SettleVO[] settleVOs = (SettleVO[])null;
    try
    {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";
      String sName = null; String sOpera = null; String sValue = null; String sSQL = null; String sReplace = null;

      ArrayList listRet = dealCondVosForPowerForManual(conditionVO);
      conditionVO = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      for (int i = 0; i < conditionVO.length; i++) {
        sName = conditionVO[i].getFieldCode().trim();
        sOpera = conditionVO[i].getOperaCode().trim();

        if (conditionVO[i].getRefResult() == null)
          sValue = "ErrorPK";
        else {
          sValue = conditionVO[i].getRefResult().getRefPK();
        }
        sSQL = conditionVO[i].getSQLStr();
        sReplace = null;

        if ((sName.equals("cbaseid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "cinventoryid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("dsumdate")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "dsumdate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && (sValue.length() > 0)) {
          String[] sClassID = dmo.getSubInvClassID(sValue);

          if ((sClassID != null) && (sClassID.length > 0)) {
            sValue = "(";
            for (int j = 0; j < sClassID.length; j++) {
              if (j < sClassID.length - 1)
                sValue = sValue + "C.pk_invcl " + sOpera + " '" + sClassID[j] + "' or ";
              else
                sValue = sValue + "C.pk_invcl " + sOpera + " '" + sClassID[j] + "')";
            }
            sReplace = 
              "cinventoryid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + 
              sUnitCode + 
              "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + 
              sValue + 
              ")";
          } else {
            sReplace = 
              "cinventoryid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + 
              sUnitCode + 
              "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and C.pk_invcl " + 
              sOpera + 
              " '" + 
              sValue + 
              "')";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "cvendorid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("ccalbodyid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "ccalbodyid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cwarehouseid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "cwarehouseid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }
      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and coalesce(naccountnum,0) > 0 ";

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      IICToPU_VmiSumDMO vmiDMO = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());

      vmiVOs = (VmiSumVO[])vmiDMO.getOutDetail(sUnitCode, sCondition);

      UFBoolean bGuageFlag = null;
      if ((vmiVOs != null) && (vmiVOs.length > 0)) {
        ArrayList listTmp = new ArrayList();
        int iLen = vmiVOs.length;
        SettleVO settleVOTmp = null;
        VmiSumHeaderVO head = null;
        UFDouble d1 = null; UFDouble d2 = null;
        for (int i = 0; i < iLen; i++) {
          if (vmiVOs[i] == null)
            continue;
          if (vmiVOs[i].getParentVO() == null)
            continue;
          head = (VmiSumHeaderVO)vmiVOs[i].getParentVO();
          settleVOTmp = new SettleVO();

          settleVOTmp.setCvmihid(head.getCvmihid());
          settleVOTmp.setCbillcode(head.getVbillcode());
          settleVOTmp.setCcalbodyid(head.getCcalbodyid());
          settleVOTmp.setCvendormangid(head.getCvendorid());
          settleVOTmp.setCwarehouseid(head.getCwarehouseid());
          settleVOTmp.setCmangid(head.getCinventoryid());
          settleVOTmp.setVfree1(head.getVfree1());
          settleVOTmp.setVfree2(head.getVfree2());
          settleVOTmp.setVfree3(head.getVfree3());
          settleVOTmp.setVfree4(head.getVfree4());
          settleVOTmp.setVfree5(head.getVfree5());
          settleVOTmp.setTs(head.getTs());
          settleVOTmp.setCbilltype("50");

          d1 = head.getNoutnum();
          if (d1 == null) d1 = new UFDouble(0);
          d2 = head.getNoutinnum();
          if (d2 == null) d2 = new UFDouble(0);
          d1 = d1.sub(d2);

          settleVOTmp.setNvminum(d1);
          if (settleVOTmp.getNvminum() == null) settleVOTmp.setNvminum(new UFDouble(0.0D));

          bGuageFlag = head.getBgaugeflag();
          if (new UFBoolean(true).equals(bGuageFlag))
            settleVOTmp.setNestinum(d1);
          else {
            settleVOTmp.setNestinum(new UFDouble(0));
          }

          settleVOTmp.setNprice(head.getNprice());
          if (settleVOTmp.getNprice() == null) settleVOTmp.setNprice(new UFDouble(0));

          settleVOTmp.setNestimny(head.getNmoney());
          if (settleVOTmp.getNestimny() == null) settleVOTmp.setNestimny(new UFDouble(0));

          settleVOTmp.setNsettlenum(head.getNaccountnum());
          if (settleVOTmp.getNsettlenum() == null) settleVOTmp.setNsettlenum(new UFDouble(0));

          settleVOTmp.setNsettlemny(head.getNaccountmny());
          if (settleVOTmp.getNsettlemny() == null) settleVOTmp.setNsettlemny(new UFDouble(0));

          settleVOTmp.setNnosettlenum(settleVOTmp.getNvminum().sub(settleVOTmp.getNsettlenum()));

          settleVOTmp.setNnosettlemny(settleVOTmp.getNnosettlenum().multiply(settleVOTmp.getNprice()));

          listTmp.add(settleVOTmp);
        }
        if (listTmp.size() > 0) {
          settleVOs = new SettleVO[listTmp.size()];
          listTmp.toArray(settleVOs);
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    return settleVOs;
  }

  public SettleVO[] queryVMISettleManual(ArrayList listPara)
    throws BusinessException
  {
    if ((listPara == null) || (listPara.size() < 2)) {
      SCMEnv.out("程序BUG：VMI汇总查询参数传递不正确!直接返回 NULL");
      return null;
    }
    String sUnitCode = (String)listPara.get(0);
    ConditionVO[] conditionVO = (ConditionVO[])listPara.get(1);

    VmiSumVO[] vmiVOs = (VmiSumVO[])null;
    SettleVO[] settleVOs = (SettleVO[])null;
    try
    {
      SettleDMO dmo = new SettleDMO();

      String sCondition = "";
      String sName = null; String sOpera = null; String sValue = null; String sSQL = null; String sReplace = null;

      ArrayList listRet = dealCondVosForPowerForManual(conditionVO);
      conditionVO = (ConditionVO[])listRet.get(0);
      String strDataPowerSql = (String)listRet.get(1);

      for (int i = 0; i < conditionVO.length; i++) {
        sName = conditionVO[i].getFieldCode().trim();
        sOpera = conditionVO[i].getOperaCode().trim();

        if (conditionVO[i].getRefResult() == null)
          sValue = "ErrorPK";
        else {
          sValue = conditionVO[i].getRefResult().getRefPK();
        }
        sSQL = conditionVO[i].getSQLStr();
        sReplace = null;

        if ((sName.equals("cbaseid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "cinventoryid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("dsumdate")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "dsumdate " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cinvclassid")) && (sValue != null) && (sValue.length() > 0)) {
          String[] sClassID = dmo.getSubInvClassID(sValue);

          if ((sClassID != null) && (sClassID.length > 0)) {
            sValue = "(";
            for (int j = 0; j < sClassID.length; j++) {
              if (j < sClassID.length - 1)
                sValue = sValue + "C.pk_invcl " + sOpera + " '" + sClassID[j] + "' or ";
              else
                sValue = sValue + "C.pk_invcl " + sOpera + " '" + sClassID[j] + "')";
            }
            sReplace = 
              "cinventoryid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + 
              sUnitCode + 
              "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and " + 
              sValue + 
              ")";
          } else {
            sReplace = 
              "cinventoryid in (select pk_invmandoc from bd_invbasdoc A,bd_invmandoc B,bd_invcl C where B.pk_corp = '" + 
              sUnitCode + 
              "' and A.pk_invbasdoc = B.pk_invbasdoc and A.pk_invcl = C.pk_invcl and C.pk_invcl " + 
              sOpera + 
              " '" + 
              sValue + 
              "')";
          }
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cvendorbaseid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "cvendorid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("ccalbodyid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "ccalbodyid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        } else if ((sName.equals("cwarehouseid")) && (sValue != null) && (sValue.length() > 0)) {
          sReplace = "cwarehouseid " + sOpera + " '" + sValue + "'";
          String s = getReplacedSQL(sSQL, sName, sReplace);
          sCondition = sCondition + s;
        }
      }
      sCondition = PubDMO.processFirst(sCondition);

      sCondition = sCondition + " and coalesce(bsettleendflag,'N') = 'N' ";

      if (strDataPowerSql != null) sCondition = sCondition + " and " + strDataPowerSql;

      IICToPU_VmiSumDMO vmiDMO = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());
      vmiVOs = (VmiSumVO[])vmiDMO.getOutDetail(sUnitCode, sCondition);

      if ((vmiVOs != null) && (vmiVOs.length > 0)) {
        ArrayList listTmp = new ArrayList();
        int iLen = vmiVOs.length;
        SettleVO settleVOTmp = null;
        VmiSumHeaderVO head = null;
        UFDouble d1 = null; UFDouble d2 = null;
        for (int i = 0; i < iLen; i++) {
          if (vmiVOs[i] == null)
            continue;
          if (vmiVOs[i].getParentVO() == null)
            continue;
          head = (VmiSumHeaderVO)vmiVOs[i].getParentVO();
          settleVOTmp = new SettleVO();

          settleVOTmp.setCvmihid(head.getCvmihid());
          settleVOTmp.setCbillcode(head.getVbillcode());
          settleVOTmp.setCcalbodyid(head.getCcalbodyid());
          settleVOTmp.setCvendormangid(head.getCvendorid());
          settleVOTmp.setCwarehouseid(head.getCwarehouseid());
          settleVOTmp.setCmangid(head.getCinventoryid());
          settleVOTmp.setVfree1(head.getVfree1());
          settleVOTmp.setVfree2(head.getVfree2());
          settleVOTmp.setVfree3(head.getVfree3());
          settleVOTmp.setVfree4(head.getVfree4());
          settleVOTmp.setVfree5(head.getVfree5());
          settleVOTmp.setTs(head.getTs());
          settleVOTmp.setCbilltype("50");

          d1 = head.getNoutnum();
          if (d1 == null) d1 = new UFDouble(0);
          d2 = head.getNoutinnum();
          if (d2 == null) d2 = new UFDouble(0);
          d1 = d1.sub(d2);
          settleVOTmp.setNvminum(d1);

          if ((head.getBgaugeflag() != null) && (head.getBgaugeflag().booleanValue()))
            settleVOTmp.setNestinum(d1);
          else {
            settleVOTmp.setNestinum(new UFDouble(0.0D));
          }

          settleVOTmp.setNprice(head.getNprice());
          if (settleVOTmp.getNprice() == null) settleVOTmp.setNprice(new UFDouble(0.0D));

          settleVOTmp.setNestimny(head.getNmoney());
          if (settleVOTmp.getNestimny() == null) settleVOTmp.setNestimny(new UFDouble(0.0D));

          settleVOTmp.setNsettlenum(head.getNaccountnum());
          if (settleVOTmp.getNsettlenum() == null) settleVOTmp.setNsettlenum(new UFDouble(0.0D));

          settleVOTmp.setNsettlemny(head.getNaccountmny());
          if (settleVOTmp.getNsettlemny() == null) settleVOTmp.setNsettlemny(new UFDouble(0.0D));

          settleVOTmp.setNnosettlenum(settleVOTmp.getNvminum().sub(settleVOTmp.getNsettlenum()));

          settleVOTmp.setNnosettlemny(settleVOTmp.getNnosettlenum().multiply(settleVOTmp.getNprice()));

          listTmp.add(settleVOTmp);
        }
        if (listTmp.size() > 0) {
          settleVOs = new SettleVO[listTmp.size()];
          listTmp.toArray(settleVOs);
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
    return settleVOs;
  }

  private void rewriteVMIAntiEsti(EstimateVO[] estiVOs)
    throws BusinessException
  {
    if ((estiVOs == null) || (estiVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[暂估数据],直接返回!");
      return;
    }
    int iLen = estiVOs.length;
    String[] saVMIHid = new String[iLen];
    for (int i = 0; i < iLen; i++) {
      saVMIHid[i] = estiVOs[i].getCvmihid();
    }
    try
    {
      IICToPU_VmiSumDMO dmo = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());
      dmo.writeBackToVMIAntiEsti(saVMIHid);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  public void rewriteVMIAntiSettle(SettlebillItemVO[] bodyVOs)
    throws BusinessException
  {
    if ((bodyVOs == null) || (bodyVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[VMI结算数据],直接返回!");
      return;
    }
    int iLen = bodyVOs.length;
    Vector vRealVo = new Vector();
    for (int i = 0; i < iLen; i++)
    {
      if (bodyVOs[i] == null)
        continue;
      if ((bodyVOs[i].getCvmiid() == null) || (bodyVOs[i].getCvmiid().trim().length() == 0))
        continue;
      vRealVo.addElement(bodyVOs[i]);
    }
    if (vRealVo.size() == 0) {
      SCMEnv.out("所有结算单不存在对应的VMI汇总记录，直接返回!");
      return;
    }
    bodyVOs = new SettlebillItemVO[vRealVo.size()];
    vRealVo.copyInto(bodyVOs);
    iLen = bodyVOs.length;
    ParaVOSettleToVMI[] paraVOs = new ParaVOSettleToVMI[iLen];
    for (int i = 0; i < iLen; i++) {
      paraVOs[i] = new ParaVOSettleToVMI();
      paraVOs[i].setSVMIHId(bodyVOs[i].getCvmiid());
      if (bodyVOs[i].getNmoney() != null)
        bodyVOs[i].setNmoney(bodyVOs[i].getNmoney().multiply(-1.0D));
      paraVOs[i].setNMoney(bodyVOs[i].getNmoney());
      if (bodyVOs[i].getNsettlenum() != null)
        bodyVOs[i].setNsettlenum(bodyVOs[i].getNsettlenum().multiply(-1.0D));
      paraVOs[i].setNNum(bodyVOs[i].getNsettlenum());
    }
    try
    {
      IICToPU_VmiSumDMO dmo = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());
      dmo.writeBackToVMIAntiSettle(paraVOs);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  private void rewriteVMIEsti(EstimateVO[] estiVOs, UFDate dateLogin)
    throws BusinessException
  {
    if ((estiVOs == null) || (estiVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[暂估数据],直接返回!");
      return;
    }
    int iLen = estiVOs.length;
    ParaVOEstiToVMI[] paraVOs = new ParaVOEstiToVMI[iLen];
    for (int i = 0; i < iLen; i++) {
      paraVOs[i] = new ParaVOEstiToVMI();
      paraVOs[i].setSVmiHid(estiVOs[i].getCvmihid());
      paraVOs[i].setDEsti(dateLogin);
      paraVOs[i].setNMoney(estiVOs[i].getNestimoney());
      paraVOs[i].setNPrice(estiVOs[i].getNestiprice());
    }
    try
    {
      IICToPU_VmiSumDMO dmo = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());
      dmo.writeBackToVMIEsti(paraVOs);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  private void rewriteVMISettle(SettleVO[] settleVOs)
    throws BusinessException
  {
    if ((settleVOs == null) || (settleVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[VMI结算数据],直接返回!");
      return;
    }
    int iLen = settleVOs.length;
    ParaVOSettleToVMI[] paraVOs = new ParaVOSettleToVMI[iLen];
    for (int i = 0; i < iLen; i++) {
      paraVOs[i] = new ParaVOSettleToVMI();
      paraVOs[i].setSVMIHId(settleVOs[i].getCvmihid());
      paraVOs[i].setNMoney(settleVOs[i].getNsettlemny());
      paraVOs[i].setNNum(settleVOs[i].getNsettlenum());
    }
    try
    {
      IICToPU_VmiSumDMO dmo = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());
      dmo.writeBackToVMISettle(paraVOs);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  private void rewriteVMISettleFee(SettlebillItemVO[] settleVOs)
    throws BusinessException
  {
    if ((settleVOs == null) || (settleVOs.length <= 0)) {
      SCMEnv.out("传入参数为空[VMI结算数据],直接返回!");
      return;
    }
    int iLen = settleVOs.length;
    Vector vTmp = new Vector();
    ParaVOSettleToVMI voPara = null;
    for (int i = 0; i < iLen; i++)
    {
      if (settleVOs[i].getVbillcode() != null)
        continue;
      voPara = new ParaVOSettleToVMI();
      voPara.setSVMIHId(settleVOs[i].getCvmiid());
      voPara.setNMoney(settleVOs[i].getNmoney());
      voPara.setNNum(new UFDouble(0.0D));
      vTmp.addElement(voPara);
    }
    if (vTmp.size() == 0) {
      SCMEnv.out("本次结算没有费用行,没有回写VMI汇总，直接返回!");
    }
    ParaVOSettleToVMI[] paraVOs = new ParaVOSettleToVMI[vTmp.size()];
    vTmp.copyInto(paraVOs);
    try
    {
      IICToPU_VmiSumDMO dmo = (IICToPU_VmiSumDMO)NCLocator.getInstance().lookup(IICToPU_VmiSumDMO.class.getName());
      dmo.writeBackToVMISettle(paraVOs);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      PubDMO.throwBusinessException(e);
    }
  }

  private BillVO[] crackBillDDHCForVMI(Hashtable tStock, BillVO[] VOs)
    throws Exception
  {
    if ((VOs == null) || (VOs.length == 0)) return VOs;

    Hashtable t = new Hashtable();
    Object oTemp = null; Object[] data = (Object[])null;
    double d1 = 0.0D; double d2 = 0.0D; double d3 = 0.0D; double d4 = 0.0D;

    Vector v = new Vector();
    for (int i = 0; i < VOs.length; i++) {
      IBillHeaderVO headVO = (IBillHeaderVO)VOs[i].getParentVO();
      if ((headVO.getBRedBill() == null) || (!headVO.getBRedBill().booleanValue()))
        continue;
      IBillItemVO[] bodyVO = (IBillItemVO[])VOs[i].getChildrenVO();
      for (int j = 0; j < bodyVO.length; j++) {
        if (v.contains(bodyVO[j].getCbill_bid())) continue; v.addElement(bodyVO[j].getCbill_bid());
      }
    }

    String[] sTemp = new String[v.size()];
    v.copyInto(sTemp);
    String ss = null;
    try {
      TempTableDMO dmoTmpTbl = new TempTableDMO();
      ss = dmoTmpTbl.insertTempTable(sTemp, "t_pu_settle_100", "pk_pu");
      if ((ss == null) || (ss.trim().equals("()")))
        ss = " ('ErrorPk') ";
    }
    catch (Exception e) {
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000046") + e.getMessage());
    }
    ss = " dr = 0 and cvmiid in " + ss;
    Hashtable hSettle = new PubDMO().queryHtResultFromAnyTable("po_settlebill_b", "cvmiid", new String[] { "nsettlenum", "ngaugemny" }, ss);

    for (int i = 0; i < VOs.length; i++) {
      IBillHeaderVO headVO = (IBillHeaderVO)VOs[i].getParentVO();
      if ((headVO.getBRedBill() != null) && (headVO.getBRedBill().booleanValue())) {
        IBillItemVO[] bodyVO = (IBillItemVO[])VOs[i].getChildrenVO();

        for (int j = 0; j < bodyVO.length; j++) {
          oTemp = hSettle.get(bodyVO[j].getCbill_bid());
          if (oTemp != null) {
            v = (Vector)oTemp;
            if ((v != null) && (v.size() > 0)) {
              for (int k = 0; k < v.size(); k++) {
                data = (Object[])v.elementAt(k);
                if ((data != null) && (data.length > 0)) {
                  if (data[0] != null) d1 = new UFDouble(data[0].toString()).doubleValue();
                  if (data[1] != null) d2 = new UFDouble(data[1].toString()).doubleValue();
                }

                oTemp = t.get(bodyVO[j].getCbill_bid());
                if (oTemp != null) {
                  data = (Object[])oTemp;
                  if ((data != null) && (data.length > 0)) {
                    if (data[0] != null) d1 += new UFDouble(data[0].toString()).doubleValue();
                    if (data[1] != null) d2 += new UFDouble(data[1].toString()).doubleValue();
                  }
                }
                t.put(bodyVO[j].getCbill_bid(), new UFDouble[] { new UFDouble(d1), new UFDouble(d2) });
              }
            }
          }

          oTemp = t.get(bodyVO[j].getCbill_bid());
          data = (Object[])oTemp;
          d1 = new UFDouble(data[0].toString()).doubleValue();
          d2 = new UFDouble(data[1].toString()).doubleValue();

          oTemp = tStock.get(bodyVO[j].getCbill_bid());
          data = (Object[])oTemp;
          d3 = new UFDouble(data[0].toString()).doubleValue();
          d4 = new UFDouble(data[1].toString()).doubleValue();

          if (Math.abs(d1 - d3) >= VMIDMO.getDigitRMB(headVO.getPk_corp()).doubleValue())
            continue;
          d4 -= d2 + bodyVO[j].getNmoney().doubleValue();
          bodyVO[j].setNmoney(new UFDouble(d4 * -1.0D));
        }
      }
    }

    return VOs;
  }

  private BillVO[] crackBillDDBCForVMI(Hashtable tStock, BillVO[] VOs)
    throws Exception
  {
    if ((VOs == null) || (VOs.length == 0)) return VOs;

    Hashtable t = new Hashtable();
    Object oTemp = null; Object[] data = (Object[])null;
    double d1 = 0.0D; double d2 = 0.0D; double d22 = 0.0D; double d3 = 0.0D; double d4 = 0.0D; double d44 = 0.0D;

    Vector v = new Vector();
    for (int i = 0; i < VOs.length; i++) {
      IBillItemVO[] bodyVO = (IBillItemVO[])VOs[i].getChildrenVO();
      for (int j = 0; j < bodyVO.length; j++) {
        if (v.contains(bodyVO[j].getCbill_bid())) continue; v.addElement(bodyVO[j].getCbill_bid());
      }
    }

    String[] sTemp = new String[v.size()];
    v.copyInto(sTemp);
    String ss = null;
    try {
      TempTableDMO dmoTmpTbl = new TempTableDMO();
      ss = dmoTmpTbl.insertTempTable(sTemp, "t_pu_settle_100", "pk_pu");
      if ((ss == null) || (ss.trim().equals("()")))
        ss = " ('ErrorPk') ";
    }
    catch (Exception e) {
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("40040502", "UPP40040502-000046") + e.getMessage());
    }
    ss = " dr = 0 and cvmiid in " + ss;
    Hashtable hSettle = new PubDMO().queryHtResultFromAnyTable("po_settlebill_b", "cvmiid", new String[] { "nsettlenum", "nmoney", "ngaugemny" }, ss);

    for (int i = 0; i < VOs.length; i++) {
      IBillHeaderVO headVO = (IBillHeaderVO)VOs[i].getParentVO();
      IBillItemVO[] bodyVO = (IBillItemVO[])VOs[i].getChildrenVO();

      for (int j = 0; j < bodyVO.length; j++) {
        oTemp = hSettle.get(bodyVO[j].getCbill_bid());
        if (oTemp != null) {
          v = (Vector)oTemp;
          if ((v != null) && (v.size() > 0)) {
            for (int k = 0; k < v.size(); k++) {
              data = (Object[])v.elementAt(k);
              if ((data != null) && (data.length > 0)) {
                if (data[0] != null) d1 = new UFDouble(data[0].toString()).doubleValue();
                if (data[1] != null) d2 = new UFDouble(data[1].toString()).doubleValue();
              }
              if (data[2] != null) {
                d22 = new UFDouble(data[2].toString()).doubleValue();
              }

              oTemp = t.get(bodyVO[j].getCbill_bid());
              if (oTemp != null) {
                data = (Object[])oTemp;
                if ((data != null) && (data.length > 0)) {
                  if (data[0] != null) d1 += new UFDouble(data[0].toString()).doubleValue();
                  if (data[1] != null) d2 += new UFDouble(data[1].toString()).doubleValue();
                }
                if (data[2] != null) d22 += new UFDouble(data[2].toString()).doubleValue();
              }
              t.put(bodyVO[j].getCbill_bid(), new UFDouble[] { new UFDouble(d1), new UFDouble(d2), new UFDouble(d22) });
            }
          }
        }

        oTemp = t.get(bodyVO[j].getCbill_bid());
        data = (Object[])oTemp;
        d1 = new UFDouble(data[0].toString()).doubleValue();
        d2 = new UFDouble(data[1].toString()).doubleValue();
        d22 = new UFDouble(data[2].toString()).doubleValue();

        oTemp = tStock.get(bodyVO[j].getCbill_bid());
        data = (Object[])oTemp;
        d3 = new UFDouble(data[0].toString()).doubleValue();
        d4 = new UFDouble(data[1].toString()).doubleValue();

        if (Math.abs(d1 - d3) >= VMIDMO.getDigitRMB(headVO.getPk_corp()).doubleValue())
          continue;
        d44 = d2 - d4 - bodyVO[j].getNmoney().doubleValue();
        d4 = d2 - d4 - d44;
        bodyVO[j].setNmoney(new UFDouble(d4));
      }

    }

    return VOs;
  }
}