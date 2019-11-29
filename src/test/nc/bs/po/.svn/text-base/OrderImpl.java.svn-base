package nc.bs.po;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.po.base.OrderstatusDMO;
import nc.bs.po.close.PoCloseImpl;
import nc.bs.po.rp.PoReceivePlanDMO;
import nc.bs.po.rp.PoReceivePlanImpl;
import nc.bs.po.status.OrderBbDMO;
import nc.bs.pr.pray.PraybillImpl;
import nc.bs.pu.ic.POATP;
import nc.bs.pu.mpp.ReWriteDMOPo;
import nc.bs.pu.pub.BatchCodeDMO;
import nc.bs.pu.pub.GetSysBillCode;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pub.SystemException;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.rc.receive.ArriveorderDMO;
import nc.bs.rc.receive.ArriveorderImpl;
import nc.bs.scm.datapower.ScmDps;
import nc.bs.scm.ic.freeitem.DefdefDMO;
import nc.bs.scm.pub.TempTableDMO;
import nc.itf.ct.service.ICtToPo_BackToCt;
import nc.itf.ic.service.IICPub_GeneralBillDMO;
import nc.itf.ic.service.IICPub_InvATPDMO;
import nc.itf.ic.service.IICPub_InvOnHandDMO;
import nc.itf.ic.service.IICToPU_201GeneralHDMO;
import nc.itf.ic.service.IICToPU_Ic2puBO;
import nc.itf.ic.service.IICToPU_StoreadminDMO;
import nc.itf.po.IOrder;
import nc.itf.pp.ask.IAsk;
import nc.itf.scm.cenpur.service.CentrPurchaseUtil;
import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.so.service.ISOToPU_SaleToPurchaseDMO;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.itf.vrm.inter.IVrmToPu_Vendorstock_D;
import nc.vo.pi.NormalCondVO;
import nc.vo.po.OrderCloseItemVO;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.po.OrderstatusVO;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.po.pub.PoVendorVO;
import nc.vo.po.pub.RetPoVrmAndParaPriceVO;
import nc.vo.po.rewrite.ParaBackIcToPoRewriteVO;
import nc.vo.po.rewrite.ParaBackRcToPoRewriteVO;
import nc.vo.po.rewrite.ParaIcToPoRewriteVO;
import nc.vo.po.rewrite.ParaPiToPoRewriteVO;
import nc.vo.po.rewrite.ParaRcToPoRewriteVO;
import nc.vo.po.rewrite.ParaRcWasteToPoRewriteVO;
import nc.vo.po.rp.OrderReceivePlanVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pu.exception.MaxPriceException;
import nc.vo.pu.exception.MaxStockException;
import nc.vo.pu.exception.PoReviseException;
import nc.vo.pu.exception.RwtPiToPoException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.pub.pf.PfPOArriveVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.util.StringUtil;
import nc.vo.scm.cenpur.service.ChgDocPkVO;
import nc.vo.scm.ctpo.ParaPoToCtRewriteVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.ParaVO21WriteNumTo20;
import nc.vo.scm.pu.ParaVO21WriteNumTo23;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.StockPresentException;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.scm.rewrite.ParaBackPOToICRewriteVO;
import nc.vo.scm.rewrite.ParaDayplToPoRewriteVO;
import nc.vo.scm.rewrite.ParaDelivToPoRewriteVO;

public class OrderImpl
  implements IOrder
{
  private void freezeBill(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.freezeBill(OrderVO)";

    if (voOrder == null) {
      PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000197")));
    }

    try
    {
      ICreateCorpQueryService corpDmo = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      if (!corpDmo.isEnabled(voOrder.getHeadVO().getPk_corp(), "IC")) {
        return;
      }

      voOrder.setBillAction("冻结");
      POATP atp = new POATP();
      atp.modifyATPWhenCloseBill(voOrder);

      new OrderDMO().freezeBill(voOrder.getCheckVO());

      atp.checkAtpInstantly(voOrder, null);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  public String[] getFreecustBank(String cfreecustid)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getFreecustBank(String)";

    String[] aryBank = null;
    try {
      OrderDMO dmo = new OrderDMO();
      aryBank = dmo.getFreecustBank(cfreecustid);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return aryBank;
  }

  public ArrayList getFreeVOList(ArrayList allIdList)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getFreeVOList(ArrayList)";

    ArrayList freeVOList = null;
    try {
      DefdefDMO freeDMO = new DefdefDMO();

      freeVOList = freeDMO.queryFreeVOByInvIDsGroupByBills(allIdList);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return freeVOList;
  }

  public String[][] getSaleCustomerID(PraybillItemVO[] bvos)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getSaleCustomerID(PraybillItemVO [])";

    if (bvos == null) return (String[][])null;
    String[][] arycustomerid = (String[][])null;
    Vector vecprayid = new Vector();
    Vector veccustomer = new Vector();
    try
    {
      ISOToPU_SaleToPurchaseDMO dmo = (ISOToPU_SaleToPurchaseDMO)NCLocator.getInstance().lookup(ISOToPU_SaleToPurchaseDMO.class.getName());
      for (int i = 0; i < bvos.length; i++)
        if ((bvos[i].getCupsourcebilltype() != null) && (bvos[i].getCupsourcebilltype().trim().equals("30"))) {
          String id = bvos[i].getCupsourcebillid();
          if (id != null) {
            String customerid = dmo.getCustomerID(id);
            if (customerid != null) {
              vecprayid.addElement(bvos[i].getCpraybill_bid());
              veccustomer.addElement(customerid);
            }
          }
        }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    if (vecprayid.size() > 0) {
      arycustomerid = new String[2][];
      arycustomerid[0] = new String[vecprayid.size()];
      arycustomerid[1] = new String[vecprayid.size()];
      for (int i = 0; i < vecprayid.size(); i++) {
        arycustomerid[0][i] = vecprayid.get(i).toString().trim();
        arycustomerid[1][i] = veccustomer.get(i).toString().trim();
      }
    }
    return arycustomerid;
  }

  public Object[][] getSingleQuotas(String cmangid, UFDouble npraynum, UFDate date)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getSingleQuotas(String, UFDouble, UFDate)";

    Object[][] objs = (Object[][])null;
    try
    {
      IVrmToPu_Vendorstock_D dmo = (IVrmToPu_Vendorstock_D)NCLocator.getInstance().lookup(IVrmToPu_Vendorstock_D.class.getName());
      objs = dmo.getVendorQuotas(cmangid, npraynum, date);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return objs;
  }

  public ArrayList onSaveMy(OrderVO voOrder)
    throws BusinessException
  {
    String[] strKey = new String[0];
    try
    {
      IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
      srv.checkDefDataType(voOrder);

      int iStatus = voOrder.getStatus();
      UFDateTime ut = new UFDateTime(System.currentTimeMillis());
      if (iStatus == 2)
      {
        if ((voOrder.getHeadVO().getBislatest().booleanValue()) && (voOrder.isNeedCreateNewBillCode())) {
          voOrder.getHeadVO().setVordercode(new GetSysBillCode().getSysBillNO(voOrder));
        }

        voOrder.getHeadVO().setTmaketime(ut);
        voOrder.getHeadVO().setTlastmaketime(ut);
      }
      else {
        voOrder.getHeadVO().setTlastmaketime(ut);
      }

      String sBthCodeCfgHead = "NULL";
      OrderVO voCheck = voOrder.getCheckVO();
      OrderVO voBatchOrder = null;
      OrderItemVO[] voaCheckItem = null;
      OrderItemVO[] voaBatchItem = null;
      if ((voCheck != null) && (!voCheck.isReturn())) {
        voaCheckItem = voCheck.getBodyVO();
        int iCheckItemLen = voaCheckItem == null ? 0 : voaCheckItem.length;
        Vector vTemp = new Vector();
        for (int i = 0; i < iCheckItemLen; i++) {
          if (PuPubVO.getString_TrimZeroLenAsNull(voaCheckItem[i].getVproducenum()) != null) {
            continue;
          }
          if (PuPubVO.getString_TrimZeroLenAsNull(voaCheckItem[i].getCorder_bid()) == null) {
            voaCheckItem[i].setCorder_bid(sBthCodeCfgHead + i);

            voaCheckItem[i].setDirty(true);
          }
          vTemp.addElement(voaCheckItem[i]);
        }
        if (vTemp.size() > 0) {
          voBatchOrder = new OrderVO();
          voBatchOrder.setParentVO(voOrder.getHeadVO());
          voaBatchItem = new OrderItemVO[vTemp.size()];
          vTemp.copyInto(voaBatchItem);
          voBatchOrder.setChildrenVO(voaBatchItem);
        }
      }
      if (voBatchOrder != null) {
        HashMap hmapCode = new BatchCodeDMO().getBatchCode(voBatchOrder);
        if (hmapCode != null) {
          for (int i = 0; i < voaCheckItem.length; i++) {
            if (PuPubVO.getString_TrimZeroLenAsNull(voaCheckItem[i].getVproducenum()) != null) {
              continue;
            }
            voaCheckItem[i].setVproducenum((String)hmapCode.get(voaCheckItem[i].getCorder_bid() + voaCheckItem[i].getCmangid()));

            if (voaCheckItem[i].isDirty()) {
              voaCheckItem[i].setCorder_bid(null);
            }

            if (voaCheckItem[i].getStatus() == 0) {
              voaCheckItem[i].setStatus(1);
            }
          }
        }

      }

      if (iStatus == 2)
      {
        strKey = insertOrder(voOrder);
      } else if (iStatus == 1)
      {
        strKey = updateOrder(voOrder);
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e.getMessage(), e);
    }

    ArrayList arraylist = new ArrayList();
    if ((strKey != null) && (strKey.length > 0)) {
      arraylist.add(strKey[0]);
      arraylist.add(voOrder);
    }

    return arraylist;
  }

  public void rewriteArrayMy(OrderVO[] voaOrder)
    throws BusinessException
  {
    if (voaOrder == null) {
      return;
    }
    int iLen = voaOrder.length;
    for (int j = 0; j < iLen; j++)
      rewriteMy(voaOrder[j]);
  }

  public void rewriteMy(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.rewriteMy(OrderVO)";

    if (voOrder == null) {
      return;
    }
    OrderItemVO[] voaItem = voOrder.getBodyVO();
    if ((voaItem == null) || (voaItem.length < 1)) {
      return;
    }

    int iVOStatus = voOrder.getStatus();

    String sPk_corp = voOrder.getHeadVO().getPk_corp();

    String sOperatorId = voOrder.getHeadVO().getCoperator();

    boolean bReplenish = voOrder.getHeadVO().getBisreplenish().booleanValue();

    Vector vecBackToCt = new Vector();

    Vector vecBackToPr = new Vector();

    Vector vecBackToRcRowId = new Vector();
    Vector vecBackToRcSubValue = new Vector();

    Vector vecBackToIcHId = new Vector();
    Vector vecBackToIcBId = new Vector();
    Vector vecBackToIcOldValue = new Vector();
    Vector vecBackToIcNewValue = new Vector();

    Vector vecBackToLendHId = new Vector();
    Vector vecBackToLendBId = new Vector();
    Vector vecBackToLendSubValue = new Vector();

    Vector vecBackToPriceNew = new Vector();

    Vector vecBackToPriceDel = new Vector();

    int iLen = voaItem.length;

    OrderItemVO voOldItem = null;

    boolean isLargessOld = false;

    String sPk_reqcorp = null;

    for (int i = 0; i < iLen; i++)
    {
      if (((iVOStatus == 2) || (iVOStatus == 3)) && (voaItem[i].isLargess()))
      {
        continue;
      }
      if (voaItem[i].getStatus() == 0)
      {
        continue;
      }
      if (voaItem[i].getIisactive().compareTo(OrderItemVO.IISACTIVE_REVISION) == 0)
      {
        continue;
      }

      String sUpBillType = PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getCupsourcebilltype());

      String sCntRowId = PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getccontractrowid());

      if (voOrder.getOldVO() != null) {
        voOldItem = voOrder.getOldVO().getBodyVOByBId(voaItem[i].getCorder_bid());
      }

      String sOldCntRowId = voOldItem == null ? null : voOldItem.getccontractrowid();

      if ((sUpBillType == null) && (sCntRowId == null) && (sOldCntRowId == null))
      {
        continue;
      }
      String sUpBillHeadId = voaItem[i].getCupsourcebillid();
      String sUpBillBodyId = voaItem[i].getCupsourcebillrowid();

      UFDouble dOrderNum = PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNordernum());
      UFDouble dLocalSummny = PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNtaxpricemny());
      UFDouble dOldOrderNum = voOldItem == null ? VariableConst.ZERO : PuPubVO.getUFDouble_NullAsZero(voOldItem.getNordernum());

      UFDouble dOldLocalSummny = voOldItem == null ? VariableConst.ZERO : PuPubVO.getUFDouble_NullAsZero(voOldItem.getNtaxpricemny());

      UFDouble dMarginNum = VariableConst.ZERO;
      UFDouble dMarginSummny = VariableConst.ZERO;

      isLargessOld = (voOldItem != null) && (voOldItem.isLargess());

      if (iVOStatus == 2)
      {
        dMarginNum = dOrderNum;
        dMarginSummny = dLocalSummny;
        dOldOrderNum = VariableConst.ZERO;
        dOldLocalSummny = VariableConst.ZERO;

        if (sCntRowId != null) {
          ParaPoToCtRewriteVO voParaToCt = new ParaPoToCtRewriteVO();
          voParaToCt.setFirstTime(voOrder.isFirstTime());
          voParaToCt.setCContractRowID(sCntRowId);
          voParaToCt.setDNum(dMarginNum);
          voParaToCt.setDSummny(dMarginSummny);
          voParaToCt.setCorpId(sPk_corp);
          voParaToCt.setBillDate(voOrder.getHeadVO() == null ? null : voOrder.getHeadVO().getDorderdate());
          voParaToCt.setOperatorID(sOperatorId);
          vecBackToCt.addElement(voParaToCt);
        }

        if (PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getCpriceaudit_bb1id()) != null)
          vecBackToPriceNew.add(voaItem[i].getCpriceaudit_bb1id());
      }
      else if (iVOStatus == 1)
      {
        if ((voaItem[i].getStatus() == 2) && (voaItem[i].isLargess()))
        {
          continue;
        }
        if ((voaItem[i].getStatus() == 1) && (voaItem[i].isLargess()) && (isLargessOld))
        {
          continue;
        }
        if ((voaItem[i].getStatus() == 3) && (isLargessOld))
        {
          continue;
        }
        if (voaItem[i].getStatus() == 1)
        {
          if ((voaItem[i].isLargess()) && (!isLargessOld)) {
            dOrderNum = VariableConst.ZERO;
            dLocalSummny = VariableConst.ZERO;
            vecBackToPriceDel.add(voaItem[i].getCpriceaudit_bb1id());
          }

          if ((!voaItem[i].isLargess()) && (isLargessOld)) {
            dOldOrderNum = VariableConst.ZERO;
            dOldLocalSummny = VariableConst.ZERO;
            vecBackToPriceNew.add(voaItem[i].getCpriceaudit_bb1id());
          }

        }
        else if (voaItem[i].getStatus() == 3)
        {
          if (!isLargessOld) {
            dOrderNum = VariableConst.ZERO;
            dLocalSummny = VariableConst.ZERO;
            vecBackToPriceDel.add(voaItem[i].getCpriceaudit_bb1id());
          }
        }

        dMarginNum = dOrderNum.sub(dOldOrderNum);
        dMarginSummny = dLocalSummny.sub(dOldLocalSummny);

        if ((sOldCntRowId != null) && (!sOldCntRowId.equals(sCntRowId))) {
          ParaPoToCtRewriteVO voParaToCt = new ParaPoToCtRewriteVO();
          voParaToCt.setFirstTime(voOrder.isFirstTime());
          voParaToCt.setCContractRowID(sOldCntRowId);
          voParaToCt.setDNum(dOldOrderNum.multiply(-1.0D));
          voParaToCt.setDSummny(dOldLocalSummny.multiply(-1.0D));
          voParaToCt.setCorpId(sPk_corp);
          voParaToCt.setBillDate(voOrder.getHeadVO() == null ? null : voOrder.getHeadVO().getDorderdate());
          voParaToCt.setOperatorID(sOperatorId);
          vecBackToCt.addElement(voParaToCt);
        }

        if (sCntRowId != null) {
          UFDouble dTempCtMarginNum = VariableConst.ZERO;
          UFDouble dTempCtMarginSummny = VariableConst.ZERO;

          if (sCntRowId.equals(sOldCntRowId))
          {
            dTempCtMarginNum = dMarginNum;
            dTempCtMarginSummny = dMarginSummny;
          }
          else {
            dTempCtMarginNum = dOrderNum;
            dTempCtMarginSummny = dLocalSummny;
          }
          ParaPoToCtRewriteVO voParaToCt = new ParaPoToCtRewriteVO();
          voParaToCt.setFirstTime(voOrder.isFirstTime());
          voParaToCt.setCContractRowID(sCntRowId);
          voParaToCt.setDNum(dTempCtMarginNum);
          voParaToCt.setDSummny(dTempCtMarginSummny);
          voParaToCt.setCorpId(sPk_corp);
          voParaToCt.setBillDate(voOrder.getHeadVO() == null ? null : voOrder.getHeadVO().getDorderdate());
          voParaToCt.setOperatorID(sOperatorId);
          vecBackToCt.addElement(voParaToCt);
        }
      } else if (iVOStatus == 3)
      {
        dMarginNum = dOrderNum.multiply(-1.0D);
        dMarginSummny = dLocalSummny.multiply(-1.0D);

        dOldOrderNum = dOrderNum;
        dOldLocalSummny = dLocalSummny;
        dOrderNum = VariableConst.ZERO;
        dLocalSummny = VariableConst.ZERO;

        if (sCntRowId != null) {
          ParaPoToCtRewriteVO voParaToCt = new ParaPoToCtRewriteVO();
          voParaToCt.setFirstTime(voOrder.isFirstTime());
          voParaToCt.setCContractRowID(sCntRowId);
          voParaToCt.setDNum(dMarginNum);
          voParaToCt.setDSummny(dMarginSummny);
          voParaToCt.setCorpId(sPk_corp);
          voParaToCt.setBillDate(voOrder.getHeadVO() == null ? null : voOrder.getHeadVO().getDorderdate());
          voParaToCt.setOperatorID(sOperatorId);
          vecBackToCt.addElement(voParaToCt);
        }

        if (PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getCpriceaudit_bb1id()) != null) {
          vecBackToPriceDel.add(voaItem[i].getCpriceaudit_bb1id());
        }

      }

      sPk_reqcorp = voaItem[i].getPk_reqcorp();
      if (PuPubVO.getString_TrimZeroLenAsNull(sUpBillType) != null) {
        if (sUpBillType.equals("20"))
        {
          ParaVO21WriteNumTo20 voParaToPr = new ParaVO21WriteNumTo20();
          voParaToPr.setPkCorp(sPk_reqcorp);
          voParaToPr.setRowbid(sUpBillBodyId);
          voParaToPr.setNumOld(dOldOrderNum);
          voParaToPr.setNumNew(dOrderNum);
          voParaToPr.setUserConfirm(!voOrder.isFirstTime());
          vecBackToPr.addElement(voParaToPr);
        } else if (("41".equals(sUpBillType)) || ("49".equals(sUpBillType)))
        {
          if (dMarginNum.compareTo(VariableConst.ZERO) != 0) {
            vecBackToLendHId.addElement(sUpBillHeadId);
            vecBackToLendBId.addElement(sUpBillBodyId);
            vecBackToLendSubValue.addElement(dMarginNum);
          }
        } else if ((bReplenish) && ("23".equals(sUpBillType)))
        {
          vecBackToRcRowId.addElement(sUpBillBodyId);
          vecBackToRcSubValue.addElement(dMarginNum); } else {
          if ((!bReplenish) || (!"45".equals(sUpBillType)))
          {
            continue;
          }
          vecBackToIcHId.addElement(sUpBillHeadId);
          vecBackToIcBId.addElement(sUpBillBodyId);
          vecBackToIcOldValue.addElement(dOldOrderNum);
          vecBackToIcNewValue.addElement(dOrderNum);
        }
      }

    }

    try
    {
      int iBackLen = vecBackToPr.size();
      if (iBackLen > 0) {
        new PraybillImpl().updateAccumulateNum((ParaVO21WriteNumTo20[])(ParaVO21WriteNumTo20[])vecBackToPr.toArray(new ParaVO21WriteNumTo20[iBackLen]));
      }

      ICtToPo_BackToCt rewriteBackToCt = null;

      vecBackToCt = getContractBackToCt(vecBackToCt, voOrder);
      iBackLen = vecBackToCt == null ? 0 : vecBackToCt.size();
      if (iBackLen > 0) {
        rewriteBackToCt = (ICtToPo_BackToCt)NCLocator.getInstance().lookup(ICtToPo_BackToCt.class.getName());
        if (rewriteBackToCt != null) {
          rewriteBackToCt.writeBackAccuOrdData((ParaPoToCtRewriteVO[])(ParaPoToCtRewriteVO[])vecBackToCt.toArray(new ParaPoToCtRewriteVO[iBackLen]));
        }
        else
        {
          SCMEnv.out("调用合同接口：查询实例时返回NULL对象实例，未真正回写合同");
        }
      }

      iBackLen = vecBackToRcRowId.size();
      if (iBackLen > 0) {
        ParaVO21WriteNumTo23 voBackToRc = new ParaVO21WriteNumTo23((String[])(String[])vecBackToRcRowId.toArray(new String[iBackLen]), (UFDouble[])(UFDouble[])vecBackToRcSubValue.toArray(new UFDouble[iBackLen]));

        new ArriveorderImpl().rewriteNaccReplenishNum(voBackToRc);
      }

      iBackLen = vecBackToIcHId.size();
      if (vecBackToIcHId.size() > 0) {
        ParaBackPOToICRewriteVO voBackToIc = new ParaBackPOToICRewriteVO();

        voBackToIc.setPk_corp(sPk_corp);
        voBackToIc.setCHIdArray((String[])(String[])vecBackToIcHId.toArray(new String[iBackLen]));
        voBackToIc.setCRowIdArray((String[])(String[])vecBackToIcBId.toArray(new String[iBackLen]));
        voBackToIc.setDOldNumArray((UFDouble[])(UFDouble[])vecBackToIcOldValue.toArray(new UFDouble[iBackLen]));
        voBackToIc.setDNumArray((UFDouble[])(UFDouble[])vecBackToIcNewValue.toArray(new UFDouble[iBackLen]));

        IICToPU_Ic2puBO bo = (IICToPU_Ic2puBO)NCLocator.getInstance().lookup(IICToPU_Ic2puBO.class.getName());
        bo.rewriteReplenishedNum(voBackToIc);
      }

      iBackLen = vecBackToLendHId.size();
      if (vecBackToLendHId.size() > 0)
      {
        IICPub_GeneralBillDMO dmo = (IICPub_GeneralBillDMO)NCLocator.getInstance().lookup(IICPub_GeneralBillDMO.class.getName());

        SCMEnv.out("");
        SCMEnv.out("");
        ArrayList listRet = getAccForJR((String[])(String[])vecBackToLendHId.toArray(new String[iBackLen]), (String[])(String[])vecBackToLendBId.toArray(new String[iBackLen]), (UFDouble[])(UFDouble[])vecBackToLendSubValue.toArray(new UFDouble[iBackLen]));

        if ((listRet != null) && (listRet.size() == 3)) {
          dmo.updateItemTransNumBatch((String[])(String[])listRet.get(0), (String[])(String[])listRet.get(1), (UFDouble[])(UFDouble[])listRet.get(2));
        }

      }

      String[] saNewBB1Id = null;
      iBackLen = vecBackToPriceNew.size();
      if (vecBackToPriceNew.size() > 0) {
        saNewBB1Id = (String[])(String[])vecBackToPriceNew.toArray(new String[iBackLen]);
      }

      String[] saDelBB1Id = null;
      iBackLen = vecBackToPriceDel.size();
      if (vecBackToPriceDel.size() > 0) {
        saDelBB1Id = (String[])(String[])vecBackToPriceDel.toArray(new String[iBackLen]);
      }
      IAsk writeTo28 = (IAsk)NCLocator.getInstance().lookup(IAsk.class.getName());
      writeTo28.reWriteGenOrderNums(saNewBB1Id, saDelBB1Id);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  private Vector getContractBackToCt(Vector vecBackToCt, OrderVO voOrder)
    throws BusinessException
  {
    if ((vecBackToCt == null) || (vecBackToCt.size() == 0)) {
      return null;
    }
    if ((voOrder == null) || (voOrder.getHeadVO() == null)) {
      return null;
    }

    if (!voOrder.isReplenish()) {
      return vecBackToCt;
    }
    OrderItemVO[] voaItem = voOrder.getBodyVO();
    if ((voaItem == null) || (voaItem.length < 1)) {
      return null;
    }

    ArrayList listUpRowId = new ArrayList();
    int iLen = voaItem.length;
    String strUpBillType = null;
    for (int i = 0; i < iLen; i++) {
      if ((PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getCupsourcebilltype()) == null) || (PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getCupsourcebillrowid()) == null))
        continue;
      strUpBillType = PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getCupsourcebilltype());
      listUpRowId.add(voaItem[i].getCupsourcebillrowid());
    }

    if ((strUpBillType == null) || (listUpRowId.size() == 0)) {
      return vecBackToCt;
    }

    String[] saUpRowId = (String[])(String[])listUpRowId.toArray(new String[listUpRowId.size()]);
    Object[][] oa2Ret = (Object[][])null;
    PubDMO dmo = null;
    try {
      dmo = new PubDMO();
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
    String strTable = null;
    String strPkField = null;
    String strQueryField = null;
    if (strUpBillType.equalsIgnoreCase("23")) {
      strTable = "po_arriveorder_b";
      strPkField = "carriveorder_bid";
      strQueryField = "corderid";
    } else {
      strTable = "ic_general_b";
      strPkField = "cgeneralbid";
      strQueryField = "cfirstbillhid";
    }
    Object[] oaReplenish = new Object[saUpRowId.length];
    try
    {
      oa2Ret = dmo.queryArrayValue(strTable, strPkField, new String[] { strQueryField }, saUpRowId);

      if ((oa2Ret == null) || (oa2Ret.length != saUpRowId.length)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000262"));
      }

      String[] saHId = new String[saUpRowId.length];
      for (int i = 0; i < oa2Ret.length; i++) {
        if ((oa2Ret[i] == null) || (oa2Ret[i][0] == null)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000262"));
        }
        saHId[i] = (oa2Ret[i][0] + "");
      }
      oa2Ret = dmo.queryArrayValue("po_order", "corderid", new String[] { "breturn" }, saHId);

      if ((oa2Ret == null) || (oa2Ret.length == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000262"));
      }
      for (int i = 0; i < oa2Ret.length; i++) {
        if ((oa2Ret[i] == null) || (oa2Ret[i][0] == null)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000262"));
        }
        oaReplenish[i] = (oa2Ret[i][0] + "");
      }
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }

    HashMap mapFilterOrder = new HashMap();
    for (int i = 0; i < saUpRowId.length; i++) {
      if (!PuPubVO.getUFBoolean_NullAs(oaReplenish[i], UFBoolean.FALSE).booleanValue()) {
        mapFilterOrder.put(saUpRowId[i], "");
      }
    }

    HashMap mapFilterCon = new HashMap();
    for (int i = 0; i < iLen; i++) {
      if ((PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getCupsourcebilltype()) == null) || (PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getCupsourcebillrowid()) == null) || (PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getccontractrowid()) == null) || (!mapFilterOrder.containsKey(PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getCupsourcebillrowid()))))
      {
        continue;
      }
      mapFilterCon.put(PuPubVO.getString_TrimZeroLenAsNull(voaItem[i].getccontractrowid()), "");
    }

    Vector vRet = new Vector();
    int iSize = vecBackToCt.size();
    for (int i = 0; i < iSize; i++) {
      if ((PuPubVO.getString_TrimZeroLenAsNull(((ParaPoToCtRewriteVO)vecBackToCt.get(i)).getCContractRowId()) == null) || (mapFilterCon.containsKey(PuPubVO.getString_TrimZeroLenAsNull(((ParaPoToCtRewriteVO)vecBackToCt.get(i)).getCContractRowId()))))
        continue;
      vRet.add(PuPubVO.getString_TrimZeroLenAsNull((ParaPoToCtRewriteVO)vecBackToCt.get(i)));
    }

    return vRet;
  }

  private ArrayList getAccForJR(String[] saHid, String[] saBid, UFDouble[] uaNum)
  {
    ArrayList listRet = null;
    if ((saHid == null) || (saBid == null) || (uaNum == null)) {
      return null;
    }
    if ((saHid.length != saBid.length) || (saHid.length != uaNum.length)) {
      return null;
    }
    int iLen = saHid.length;
    ArrayList listHid = new ArrayList();
    ArrayList listBid = new ArrayList();
    ArrayList listNum = new ArrayList();
    UFDouble num = null;
    int iPos = 0;
    for (int i = 0; i < iLen; i++) {
      if ((saHid[i] == null) || (saBid[i] == null) || (uaNum[i] == null)) {
        continue;
      }
      iPos = listBid.indexOf(saBid[i]);
      if (iPos >= 0) {
        num = uaNum[i].add(PuPubVO.getUFDouble_NullAsZero(listNum.get(iPos)));
        listNum.remove(iPos);
        listNum.add(iPos, num);
      }
      else {
        listHid.add(saHid[i]);
        listBid.add(saBid[i]);
        listNum.add(uaNum[i]);
      }
    }
    if (listBid.size() > 0) {
      int iSize = listBid.size();
      listRet = new ArrayList();
      listRet.add((String[])(String[])listHid.toArray(new String[iSize]));
      listRet.add((String[])(String[])listBid.toArray(new String[iSize]));
      listRet.add((UFDouble[])(UFDouble[])listNum.toArray(new UFDouble[iSize]));
    }
    return listRet;
  }

  public void unfreezeBillArray(OrderVO[] voaOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.unfreezeBillArray(OrderVO [])";

    if (voaOrder == null) {
      PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000199")));
    }

    try
    {
      ICreateCorpQueryService corpDmo = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      if (!corpDmo.isEnabled(voaOrder[0].getHeadVO().getPk_corp(), "IC")) {
        return;
      }
      int iLen = voaOrder.length;
      POATP poatp = new POATP();
      for (int i = 0; i < iLen; i++)
      {
        voaOrder[i].setBillAction("解冻");
        poatp.modifyATPWhenOpenBill(voaOrder[i]);
      }

      new OrderDMO().unfreezeBillArray(voaOrder);

      for (int i = 0; i < iLen; i++)
        poatp.checkAtpInstantly(voaOrder[i], null);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  public void clearPrePayMnyIfOneCnt(OrderVO[] voaOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.clearPrePayMnyIfOneCnt(OrderVO [])";

    if (voaOrder == null) {
      return;
    }

    Vector vecId = new Vector();
    int iLen = voaOrder.length;
    for (int i = 0; i < iLen; i++) {
      if (voaOrder[i].isFrmOneContract()) {
        vecId.addElement(voaOrder[i].getHeadVO().getCorderid());
      }
      else {
        if (PuPubVO.getUFDouble_NullAsZero(voaOrder[i].getHeadVO().getNprepaymny()).compareTo(VariableConst.ZERO) == 0)
          continue;
        PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000195")));
      }

    }

    int iClearLen = vecId.size();
    if (iClearLen == 0) {
      return;
    }

    try
    {
      new OrderDMO().clearPrePayMny((String[])(String[])vecId.toArray(new String[iClearLen]));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  public OrderVO filtOrderForPushStor(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.filtOrderForPushStor(OrderVO)";

    if (BillStatus.AUDITING == voOrder.getHeadVO().getForderstatus()) {
      return null;
    }

    String sOrderId = voOrder.getHeadVO().getCorderid();
    String sBizType = voOrder.getHeadVO().getCbiztype();

    NormalCondVO[] voaNormal = new NormalCondVO[5];

    voaNormal[0] = new NormalCondVO("待查询公司", voOrder.getHeadVO().getPk_corp());

    voaNormal[1] = new NormalCondVO("业务类型", sBizType);

    voaNormal[2] = new NormalCondVO("业务类型", sOrderId);

    voaNormal[3] = new NormalCondVO("订单审批生成入库单", new UFBoolean(true));

    voaNormal[4] = new NormalCondVO("订单VO", voOrder);

    OrderVO[] voaRet = null;
    try
    {
      voaRet = queryOrderArrayForStore(voaNormal, null);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    return voaRet == null ? null : voaRet[0];
  }

  public PfPOArriveVO[] getBizeTypes(String pk_corp)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getPOArrvFlag(String, String, String)";

    PfPOArriveVO[] vos = null;
    try {
      OrderDMO dmo = new OrderDMO();
      vos = dmo.queryBizeTypes(pk_corp);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return vos;
  }

  public ArrayList getInfosCanModify(ArrayList listPara)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getOrderNumByBiztype(String, String)";
    if ((listPara == null) || (listPara.size() != 4)) {
      SCMEnv.out(sMethodName + "参数有问题，请联系相关程序员!");
      return null;
    }
    String strPkCorp = (String)listPara.get(0);
    String strCbiztype = (String)listPara.get(1);
    UFBoolean bStChged = (UFBoolean)listPara.get(2);
    UFBoolean bRpChged = (UFBoolean)listPara.get(3);

    ArrayList listRet = new ArrayList();
    UFDouble ncount = null;
    try {
      OrderDMO dmo = new OrderDMO();

      if ((bStChged != null) && (bStChged.booleanValue())) {
        ncount = dmo.getOrderNumByBiztype(strPkCorp, strCbiztype);
        listRet.add(new UFBoolean((ncount == null) || (ncount.doubleValue() <= 0.0D)));
      } else {
        listRet.add(null);
      }

      if ((bRpChged != null) && (bRpChged.booleanValue())) {
        UFBoolean ufbExistActive = dmo.isExistActive(strPkCorp, strCbiztype);
        listRet.add(new UFBoolean(!ufbExistActive.booleanValue()));
      } else {
        listRet.add(null);
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    return listRet;
  }

  private String[] getOrderUIFromAndWhere(NormalCondVO[] voaNormalCond, ConditionVO[] voaDefinedCond)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getOrderUIFromAndWhere(NormalCondVO [], ConditionVO [])";

    int iDefinedLen = voaNormalCond.length;
    String sPk_corp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormalCond);

    String sNormalWhere = null;
    boolean bReplenish = false;
    boolean bConstrictClosedLines = false;
    boolean bOnlyClosedLines = false;
    for (int i = 0; i < iDefinedLen; i++) {
      if (voaNormalCond[i].getKey().equals("常用条件")) {
        sNormalWhere = (String)voaNormalCond[i].getValue();
      } else if (voaNormalCond[i].getKey().equals("补货")) {
        if (((String)voaNormalCond[i].getValue()).equals("是"))
          bReplenish = true;
      }
      else if (voaNormalCond[i].getKey().equals("ConstrictClosedLines")) {
        if (((String)voaNormalCond[i].getValue()).equals("Y"))
          bConstrictClosedLines = true;
      } else {
        if ((!voaNormalCond[i].getKey().equals("OnlyClosedLines")) || 
          (!((String)voaNormalCond[i].getValue()).equals("Y"))) continue;
        bOnlyClosedLines = true;
      }

    }

    StringBuffer sbufUIWhere = new StringBuffer();
    sbufUIWhere.append(" po_order.pk_corp='");
    sbufUIWhere.append(sPk_corp);
    sbufUIWhere.append("'");
    if ((sNormalWhere != null) && (!sNormalWhere.trim().equals(""))) {
      sbufUIWhere.append(" AND ");
      sbufUIWhere.append(sNormalWhere);
    }
    if (bReplenish) {
      sbufUIWhere.append(" AND ");
      sbufUIWhere.append("ISNULL(po_order.bisreplenish,'N')='Y'");
    }

    if (bConstrictClosedLines) {
      sbufUIWhere.append(" AND ");
      sbufUIWhere.append(OrderPubVO.SQL_PO_BODY_ACTIVE);
    }
    if (bOnlyClosedLines) {
      sbufUIWhere.append(" AND ");
      sbufUIWhere.append(OrderPubVO.SQL_PO_BODY_CLOSE);
    }
    if (voaDefinedCond != null)
    {
      String sDefinedWhere = null;
      try {
        sDefinedWhere = OrderPubDMO.getWhereSQL(new String[] { sPk_corp }, voaDefinedCond);
      }
      catch (Exception e) {
        PubDMO.throwBusinessException(sMethodName, e);
      }

      if (sDefinedWhere != null) {
        sbufUIWhere.append(" AND ");
        sbufUIWhere.append(sDefinedWhere);
      }

    }

    sbufUIWhere.append(" AND ");
    sbufUIWhere.append("(po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')");
    sbufUIWhere.append(" AND ");
    sbufUIWhere.append(OrderPubVO.SQL_PO_BODY);

    String[] saSql = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufUIWhere.toString());
    if ((saSql == null) || (saSql.length < 2)) {
      SCMEnv.out("程序BUG!以下信息供程序员参考：\n");
      SCMEnv.out(new Exception());
      throw new BusinessException("不能正确得到表头查询条件!");
    }

    return saSql;
  }

  private String getParaMaxPrice(SysInitDMO dmoSysInit, String pk_corp)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getParaMaxPrice(SysInitDMO, String)";
    try
    {
      return dmoSysInit.getParaString(pk_corp, "PO01");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000200")));
    }

    return null;
  }

  private String getParaMaxStock(SysInitDMO dmoSysInit, String pk_corp)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getParaMaxStock(SysInitDMO, String)";
    try
    {
      return dmoSysInit.getParaString(pk_corp, "PO00");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000201")));
    }

    return null;
  }

  private String getReplacedSqlByBB1Info(String sSql)
  {
    if (PuPubVO.getString_TrimZeroLenAsNull(sSql) == null) {
      return sSql;
    }

    String sNewSql = sSql;

    sNewSql = StringUtil.replaceAllString(sNewSql, "po_order_b.pk_arrvstoorg", "po_order_bb1.cstoreorganization");

    sNewSql = StringUtil.replaceAllString(sNewSql, "po_order_b.vfree", "po_order_bb1.vfree");

    sNewSql = StringUtil.replaceAllString(sNewSql, "po_order_b.dplanarrvdate", "po_order_bb1.dplanarrvdate");

    sNewSql = StringUtil.replaceAllString(sNewSql, "po_order_b.vproducenum", "po_order_bb1.vproducenum");

    return sNewSql;
  }

  public PoVendorVO[] getVendoresInfo(String[] saVendMangId)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.getVendoresInfo(String [])";

    if (saVendMangId == null) {
      return null;
    }

    int iLen = saVendMangId.length;
    PoVendorVO[] voaRet = new PoVendorVO[iLen];
    String[] saDistinctId = PuPubVO.getDistinctArray(saVendMangId);
    if (saDistinctId == null) {
      return voaRet;
    }

    Hashtable htVO = null;
    try {
      htVO = new OrderDMO().getVendorInfos(saVendMangId);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    for (int i = 0; i < iLen; i++) {
      if (saVendMangId[i] != null) {
        voaRet[i] = ((PoVendorVO)htVO.get(saVendMangId[i]));
      }
    }

    return voaRet;
  }

  public void initSatus(String pk_corp)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.initSatus(String)";

    PfPOArriveVO[] vos = null;
    try {
      OrderDMO dmo = new OrderDMO();

      vos = dmo.queryBizeTypes(pk_corp);
      if ((vos == null) || (vos.length == 0)) {
        return;
      }
      OrderstatusDMO statusDmo = new OrderstatusDMO();
      String[] cbiztypes = new String[vos.length];
      for (int i = 0; i < vos.length; i++) {
        cbiztypes[i] = vos[i].getPk_busiType();
      }

      cbiztypes = statusDmo.isExistByBiztypes(pk_corp, cbiztypes);
      int iLen = cbiztypes == null ? 0 : cbiztypes.length;
      SCMEnv.out("初始化订单状态了表信息：已有定义数目：" + iLen);

      HashMap mapIdVosExist = new HashMap();
      for (int i = 0; i < iLen; i++) {
        if ((cbiztypes[i] == null) || (mapIdVosExist.containsKey(cbiztypes[i]))) {
          continue;
        }
        mapIdVosExist.put(cbiztypes[i], "");
      }

      HashMap mapIdVosNew = new HashMap();
      iLen = vos.length;
      for (int i = 0; i < iLen; i++) {
        if ((vos[i] == null) || (mapIdVosNew.containsKey(vos[i].getPk_busiType())) || (mapIdVosExist.containsKey(vos[i].getPk_busiType())))
        {
          continue;
        }

        mapIdVosNew.put(vos[i].getPk_busiType(), new Vector());
      }
      if (mapIdVosNew.size() == 0) {
        SCMEnv.out("初始化订单状态了表信息：新定义数目：0!");
        return;
      }
      Vector vTmp = null;
      for (int i = 0; i < iLen; i++) {
        if ((vos[i] == null) || (vos[i].getPk_busiType() == null) || (!mapIdVosNew.containsKey(vos[i].getPk_busiType())))
        {
          continue;
        }

        vTmp = (Vector)mapIdVosNew.get(vos[i].getPk_busiType());
        vTmp.add(vos[i]);
      }

      boolean bArriveFlag = false;
      boolean bStoreFlag = false;
      Iterator keys = mapIdVosNew.keySet().iterator();
      String strKey = null;

      PfPOArriveVO[] voaOrder = new PfPOArriveVO[mapIdVosNew.keySet().size()];
      PfPOArriveVO voTmp = null;
      int iPos = 0;
      while (keys.hasNext()) {
        strKey = (String)keys.next();
        vTmp = (Vector)mapIdVosNew.get(strKey);
        bArriveFlag = false;
        bStoreFlag = false;
        for (int j = 0; j < vTmp.size(); j++) {
          voTmp = (PfPOArriveVO)vTmp.elementAt(j);
          if (!bArriveFlag) {
            bArriveFlag = voTmp.getArriveFlag().booleanValue();
          }
          if (!bStoreFlag) {
            bStoreFlag = voTmp.getStoreFlag().booleanValue();
          }
        }
        voaOrder[iPos] = voTmp;
        voaOrder[iPos].setArriveFlag(new UFBoolean(bArriveFlag));
        voaOrder[iPos].setStoreFlag(new UFBoolean(bStoreFlag));
        iPos++;
      }
      statusDmo.autoInsert(pk_corp, voaOrder);
      SCMEnv.out("初始化订单状态了表信息：新定义数目：" + mapIdVosNew.keySet().size());
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  private String[] insertOrder(OrderVO vo)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.insertOrder(OrderVO)";

    if ((vo == null) || (vo.getHeadVO() == null)) {
      return null;
    }
    OrderHeaderVO voHead = vo.getHeadVO();
    OrderItemVO[] voaItem = vo.getBodyVO();

    Vector vecBody = new Vector();
    int iBodyLen = voaItem.length;
    for (int i = 0; i < iBodyLen; i++) {
      if (voaItem[i].getStatus() == 3) {
        continue;
      }
      vecBody.addElement(voaItem[i]);
    }

    int iInsertBodyLen = vecBody.size();
    if (iInsertBodyLen == 0) {
      return null;
    }

    OrderItemVO[] voaInsertItem = new OrderItemVO[iInsertBodyLen];
    vecBody.copyInto(voaInsertItem);

    String sHeadKey = null;

    String[] saRetBodyKey = null;
    try {
      OrderDMO dmo = new OrderDMO();

      sHeadKey = dmo.insertHead(voHead);

      voHead.setPrimaryKey(sHeadKey);

      for (int i = 0; i < iInsertBodyLen; i++) {
        voaInsertItem[i].setCorderid(sHeadKey);
      }
      saRetBodyKey = dmo.insertBodyArray(voaInsertItem);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    String[] saRetKey = new String[iInsertBodyLen + 1];
    saRetKey[0] = sHeadKey;
    for (int i = 0; i < iInsertBodyLen; i++) {
      saRetKey[(i + 1)] = saRetBodyKey[i];

      voaInsertItem[i].setCorder_bid(saRetBodyKey[i]);
    }
    return saRetKey;
  }

  public UFBoolean isBusiRp(String strBusiType, String strCorpId)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.isBusiRp(String strBusiType)";
    try
    {
      OrderstatusDMO statusDmo = new OrderstatusDMO();
      OrderstatusVO status = statusDmo.queryStatusVOByBizTypeID(strBusiType, strCorpId);
      if ((status != null) && (status.getBisneedrp() != null) && (status.getBisneedrp().intValue() == 1))
        return new UFBoolean(true);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return new UFBoolean(false);
  }

  public OrderCloseItemVO[] queryForClose(NormalCondVO[] voaNormalCond, ConditionVO[] voaDefinedCond)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryForClose(NormalCondVO [], ConditionVO [])";

    StringBuffer sbufSql = new StringBuffer("");
    sbufSql.append(OrderPubVO.SQL_PO_AUDIT_OR_OUTPUT + " AND " + OrderPubVO.SQL_PO_BODY);

    int iLen = voaNormalCond.length;
    for (int i = 0; i < iLen; i++) {
      if (voaNormalCond[i].getKey().equals("查询已关闭")) {
        if (voaNormalCond[i].getValue().equals("是"))
          sbufSql.append(" AND " + OrderPubVO.SQL_PO_BODY_CLOSE);
        else {
          sbufSql.append(" AND " + OrderPubVO.SQL_PO_BODY_ACTIVE);
        }
      }
    }
    String sQueryPkCorp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormalCond);
    String sLoginPkCorp = OrderPubDMO.getPkCorpLoginFromNormalVOs(voaNormalCond);
    String sLoginUserId = OrderPubDMO.getOperatorFromNormalVOs(voaNormalCond);
    String sDefinedPart = null;
    try {
      sDefinedPart = OrderPubDMO.getWhereSQL(new String[] { sQueryPkCorp }, voaDefinedCond);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    if (sDefinedPart != null) {
      sbufSql.append(" AND ");
      sbufSql.append(sDefinedPart);
    }

    OrderCloseItemVO[] voaRet = null;
    try {
      OrderDMO dmo = new OrderDMO();
      String[] arySql = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufSql.toString());
      if (sQueryPkCorp != null) {
        arySql[1] = (arySql[1] + " AND (po_order_b.pk_corp = '" + sQueryPkCorp + "')");
      }

      String strHeadSql = null;
      String strBodySql = null;
      if ((sLoginUserId == null) || (sLoginPkCorp == null)) {
        SCMEnv.out("未传入操作员或公司主键：本次查询不启用权限\n");
      } else {
        strHeadSql = ScmDps.getSubSql("po_order", null, sLoginUserId, new String[] { sLoginPkCorp });
        if ((strHeadSql != null) && (strHeadSql.trim().length() > 0))
        {
          int tmp369_368 = 1;
          String[] tmp369_366 = arySql; tmp369_366[tmp369_368] = (tmp369_366[tmp369_368] + " and " + strHeadSql + " ");
        }
        strBodySql = ScmDps.getSubSql("po_order_b", null, sLoginUserId, new String[] { sLoginPkCorp });
        if ((strBodySql != null) && (strBodySql.trim().length() > 0))
        {
          int tmp441_440 = 1;
          String[] tmp441_438 = arySql; tmp441_438[tmp441_440] = (tmp441_438[tmp441_440] + " and " + strBodySql + " ");
        }
      }
      voaRet = dmo.queryCloseBodyArrayByConds("FROM " + arySql[0] + " WHERE " + arySql[1]);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return voaRet;
  }

  public OrderVO[] queryForRevision(NormalCondVO[] voaNormalCond, ConditionVO[] voaDefinedCond)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryForRevision(NormalCondVO [], ConditionVO [])";

    String sPk_Corp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormalCond);

    String sLoginPk_Corp = OrderPubDMO.getPkCorpLoginFromNormalVOs(voaNormalCond);

    String sNormalCond = OrderPubDMO.getNormalFromNormalVOs(voaNormalCond);

    String sDefinedPart = null;
    try {
      sDefinedPart = OrderPubDMO.getWhereSQL(new String[] { sPk_Corp }, voaDefinedCond);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    StringBuffer sbufSql = new StringBuffer("");
    sbufSql.append(OrderPubVO.SQL_PO_AUDIT_OR_OUTPUT);
    sbufSql.append(" AND (po_order.pk_corp = '" + sPk_Corp + "')");
    sbufSql.append(" AND " + OrderPubVO.SQL_PO_BODY_ACTIVE);
    sbufSql.append(" AND (po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')");
    sbufSql.append(" AND " + OrderPubVO.SQL_PO_BODY);
    if (sDefinedPart != null) {
      sbufSql.append(" AND ");
      sbufSql.append(sDefinedPart);
    }
    String[] saSql = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufSql.toString());
    String sHeadFrmWhere = " FROM " + saSql[0] + " WHERE " + saSql[1];
    String sLoginUserId = OrderPubDMO.getOperatorFromNormalVOs(voaNormalCond);
    String strHeadSql = null;
    String strBodySql = null;
    if ((sLoginUserId == null) || (sLoginPk_Corp == null)) {
      SCMEnv.out("未传入操作员或公司主键：本次查询不启用权限\n");
    } else {
      strHeadSql = ScmDps.getSubSql("po_order", null, sLoginUserId, new String[] { sLoginPk_Corp });
      if ((strHeadSql != null) && (strHeadSql.trim().length() > 0)) {
        sHeadFrmWhere = sHeadFrmWhere + " and " + strHeadSql + " ";
      }
      strBodySql = ScmDps.getSubSql("po_order_b", null, sLoginUserId, new String[] { sLoginPk_Corp });
      if ((strBodySql != null) && (strBodySql.trim().length() > 0)) {
        sHeadFrmWhere = sHeadFrmWhere + " and " + strBodySql + " ";
      }
    }

    if ((sNormalCond != null) && (sNormalCond.trim().length() > 0)) {
      sHeadFrmWhere = sHeadFrmWhere + " AND " + sNormalCond;
    }

    OrderVO[] voaRet = null;
    try
    {
      OrderDMO dmoOrder = new OrderDMO();
      HashMap hmapOrder = dmoOrder.queryHeadHashMapByConds(sHeadFrmWhere);

      if (hmapOrder == null) {
        return null;
      }

      voaRet = OrderPubDMO.getOrderVOsFromHashMap(hmapOrder);

      voaRet[0].setChildrenVO(dmoOrder.queryBodyByHeadId(voaRet[0].getHeadVO().getCorderid()));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    return voaRet;
  }

  public OrderVO[] queryOrderArrayByConds(String sWhere, String sLoginCorpId, String sLoginUserId)
    throws BusinessException
  {
    return queryOrderArrayByConds(sWhere, null, sLoginCorpId, sLoginUserId, null);
  }

  public OrderVO[] queryOrderArrayByConds(String sWholeWhere, String sBodyWhere, String sLoginCorpId, String sLoginUserId, String strDataPowerSql)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryOrderArrayByConds(String, String)";

    String sSql = sWholeWhere + " AND " + "(po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')" + " AND " + OrderPubVO.SQL_PO_BODY;

    String[] saTableAndWhere = OrderPubVO.getSourceSql("po_order", "po_order_b", sSql);

    saTableAndWhere = OrderPubDMO.dealFromWhere(strDataPowerSql, saTableAndWhere);

    String sHeadFrmWhere = " FROM " + saTableAndWhere[0] + " WHERE " + saTableAndWhere[1];

    HashMap hmapHead = null;
    try {
      hmapHead = new OrderDMO().queryHeadHashMapByConds(sHeadFrmWhere);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    if (hmapHead == null) {
      return null;
    }

    StringBuffer sbufFromWhere = new StringBuffer();
    sbufFromWhere.append(" FROM po_order_b WHERE po_order_b.corderid IN(");
    sbufFromWhere.append(" SELECT po_order.corderid ");
    sbufFromWhere.append(sHeadFrmWhere);
    sbufFromWhere.append(") AND");
    sbufFromWhere.append(OrderPubVO.SQL_PO_BODY);
    if (sBodyWhere != null) {
      sbufFromWhere.append(" AND ");
      sbufFromWhere.append(sBodyWhere);
    }

    HashMap hmapBody = null;
    try {
      hmapBody = new OrderDMO().queryBodyHashmapHIDAsKeyByConds(sbufFromWhere.toString());
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    if (hmapBody == null) {
      return null;
    }

    ArrayList listVO = new ArrayList();
    int iLen = hmapBody.size();
    String[] saHId = (String[])(String[])hmapBody.keySet().toArray(new String[iLen]);
    OrderHeaderVO voHead = null;
    Vector vecBody = null;
    for (int i = 0; i < iLen; i++)
    {
      voHead = (OrderHeaderVO)hmapHead.get(saHId[i]);
      if (voHead == null)
      {
        continue;
      }
      vecBody = (Vector)hmapBody.get(saHId[i]);

      listVO.add(new OrderVO(voHead, (OrderItemVO[])(OrderItemVO[])vecBody.toArray(new OrderItemVO[vecBody.size()])));
    }

    iLen = listVO.size();
    return iLen == 0 ? null : (OrderVO[])(OrderVO[])listVO.toArray(new OrderVO[iLen]);
  }

  private OrderVO[] queryOrderArrayForAfter_B(String sPk_corp, int iAfterBillStatus, String sFromWhereB, String sFromWhereBB1)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryOrderArrayForAfter_B(String, String, String)";

    if (sFromWhereBB1 != null) {
      sFromWhereBB1 = StringUtil.replaceAllString(sFromWhereBB1, " po_order_b.pk_arrvcorp", " po_order_bb1.pk_arrvcorp");
    }

    String sHSql = "(";
    if (sFromWhereB != null) {
      sHSql = sHSql + " SELECT po_order_b.corderid " + sFromWhereB + " UNION ALL ";
    }

    sHSql = sHSql + " SELECT po_order_bb1.corderid " + sFromWhereBB1 + ")";

    String sBSql = "(";
    if (sFromWhereB != null) {
      sBSql = sBSql + " SELECT po_order_b.corder_bid " + sFromWhereB + " UNION ALL ";
    }

    sBSql = sBSql + " SELECT po_order_bb1.corder_bid " + sFromWhereBB1 + ")";

    String sBB1Sql = " (SELECT po_order_bb1.corder_bb1id " + sFromWhereBB1 + ")";

    HashMap hmapHead = null;
    HashMap hmapBody = null;
    HashMap hmapRP = null;
    try {
      OrderDMO dmoOrder = new OrderDMO();

      hmapHead = dmoOrder.queryHeadHashMapByConds(" FROM po_order WHERE corderid IN " + sHSql);

      if (hmapHead == null) {
        return null;
      }

      hmapBody = dmoOrder.queryBodyHashmapHIDAsKeyByConds(" FROM po_order_b WHERE corder_bid IN " + sBSql);

      if (hmapBody == null) {
        return null;
      }

      hmapRP = new PoReceivePlanDMO().queryHMapBIDAsKeyByConds(" FROM po_order_bb1 WHERE corder_bb1id IN " + sBB1Sql);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    String sLocalCurrId = null;
    int iPriceDigit = 0;
    try {
      sLocalCurrId = PubDMO.getLocalCurrId(sPk_corp);
      iPriceDigit = new nc.bs.pu.pub.PubImpl().getDigitBatch(sPk_corp, new String[] { "BD505" })[0];
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    String[] saHId = (String[])(String[])hmapHead.keySet().toArray(new String[hmapHead.size()]);
    int iLen = saHId.length;
    for (int i = 0; i < iLen; i++)
    {
      OrderHeaderVO voHead = (OrderHeaderVO)hmapHead.get(saHId[i]);
      Vector vecBody = (Vector)hmapBody.get(saHId[i]);
      if (vecBody == null) {
        hmapHead.remove(saHId[i]);
      }
      else
      {
        OrderVO voOrder = new OrderVO();
        voOrder.setParentVO(voHead);
        voOrder.setChildrenVO((OrderItemVO[])(OrderItemVO[])vecBody.toArray(new OrderItemVO[vecBody.size()]));

        OrderItemVO[] voaItem = voOrder.getBodyVO();
        int iBodyLen = voaItem.length;
        for (int j = 0; j < iBodyLen; j++) {
          OrderItemVO voItem = voaItem[j];

          if (hmapRP != null) {
            ArrayList listRP = (ArrayList)hmapRP.get(voItem.getCorder_bid());
            if (listRP != null) {
              voItem.setRPVOs((OrderReceivePlanVO[])(OrderReceivePlanVO[])listRP.toArray(new OrderReceivePlanVO[listRP.size()]));
            }
          }

          voItem.calLocalPrice(sLocalCurrId, iPriceDigit);
          voItem.calLocalTaxPrice(sLocalCurrId, iPriceDigit);

          if (voItem.getNordernum() != null) {
            if (iAfterBillStatus == 7)
            {
              voItem.setNnotarrvnum(voItem.getNordernum().sub(PuPubVO.getUFDouble_NullAsZero(voItem.getNaccumarrvnum())));
            }
            else if (iAfterBillStatus == 8)
            {
              voItem.setNnotstorenum(voItem.getNordernum().sub(PuPubVO.getUFDouble_NullAsZero(voItem.getNaccumstorenum())));
            }
            else if (iAfterBillStatus == 10)
            {
              UFDouble dNotArrvNum = null;
              if (voItem.getNordernum().compareTo(VariableConst.ZERO) < 0)
              {
                dNotArrvNum = voItem.getNordernum().add(PuPubVO.getUFDouble_NullAsZero(voItem.getNbackarrvnum()));
                dNotArrvNum = dNotArrvNum.add(PuPubVO.getUFDouble_NullAsZero(voItem.getNbackstorenum()));
                voItem.setNnotarrvnum(dNotArrvNum);
              }
              else {
                dNotArrvNum = PuPubVO.getUFDouble_NullAsZero(voItem.getNaccumarrvnum());
                dNotArrvNum = dNotArrvNum.sub(PuPubVO.getUFDouble_NullAsZero(voItem.getNbackarrvnum()));
                dNotArrvNum = dNotArrvNum.sub(PuPubVO.getUFDouble_NullAsZero(voItem.getNaccumstorenum()));
                voItem.setNnotarrvnum(dNotArrvNum.multiply(-1.0D));
              }
            } else {
              if (iAfterBillStatus != 11)
                continue;
              UFDouble dNotStoreNum = null;
              if (voItem.getNordernum().compareTo(VariableConst.ZERO) < 0)
              {
                dNotStoreNum = voItem.getNordernum().add(PuPubVO.getUFDouble_NullAsZero(voItem.getNbackarrvnum()));
                dNotStoreNum = dNotStoreNum.add(PuPubVO.getUFDouble_NullAsZero(voItem.getNbackstorenum()));
                voItem.setNnotstorenum(dNotStoreNum);
              }
              else {
                dNotStoreNum = PuPubVO.getUFDouble_NullAsZero(voItem.getNaccumstorenum());
                dNotStoreNum = dNotStoreNum.sub(PuPubVO.getUFDouble_NullAsZero(voItem.getNbackstorenum()));
                voItem.setNnotstorenum(dNotStoreNum.multiply(-1.0D));
              }
            }
          }
        }

        hmapHead.remove(saHId[i]);
        hmapHead.put(voHead.getVordercode() + saHId[i], voOrder);
      }
    }
    TreeMap trmapRet = new TreeMap(hmapHead);
    return (OrderVO[])(OrderVO[])trmapRet.values().toArray(new OrderVO[trmapRet.size()]);
  }

  public OrderVO[] queryOrderArrayForArrive(NormalCondVO[] voaNormal, ConditionVO[] voaDefined)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryOrderArrayForArrive(NormalCondVO [], ConditionVO [])";

    String sLoginCorp = OrderPubDMO.getPkCorpLoginFromNormalVOs(voaNormal);
    String sQueryCorp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormal);
    String sBizType = OrderPubDMO.getBizTypeIdFromNormalVOs(voaNormal);

    StringBuffer sbufSql = new StringBuffer("po_order.cbiztype = '" + sBizType + "'");

    sbufSql.append(" AND po_order_b.pk_arrvcorp = '" + sLoginCorp + "'");

    String sOrderCode = OrderPubDMO.getVordercodeFromNormalVOs(voaNormal);
    if (sOrderCode != null) {
      sbufSql.append(" AND po_order.vordercode = '" + sOrderCode + "'");
    }
    String sVplancodeSql = OrderPubDMO.getVplancodeSqlFromNormalVOs(voaNormal);
    if (sVplancodeSql != null) {
      sbufSql.append(" AND ");
      sbufSql.append(sVplancodeSql);
    }

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(voaDefined);
    voaDefined = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);

    String sDefinedPart = null;
    try {
      sDefinedPart = OrderPubDMO.getWhereSQL(new String[] { sQueryCorp }, voaDefined);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    if (sDefinedPart != null) {
      sbufSql.append(" AND ");
      sbufSql.append(sDefinedPart);
    }

    OrderVO[] voaOrder = null;
    try {
      OrderstatusVO voBizStatus = new OrderstatusDMO().queryStatusVOByBizTypeID(sBizType, sQueryCorp);
      if (voBizStatus == null) {
        SCMEnv.out("业务类型[主键为：" + sBizType + "]在采购公司[主键为：" + sQueryCorp + "]未初始化，本次查询返回NULL");
        return null;
      }

      if (!voBizStatus.isArrive()) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000203"));
      }

      int iUpStatus = voBizStatus.getUpStatus(7, true);

      sbufSql.append(" AND (" + OrderPubVO.SQL_PO_AUDIT + " OR " + OrderPubVO.SQL_PO_OUTPUT + ") ");

      sbufSql.append(" AND ");
      sbufSql.append("(po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')");

      sbufSql.append(" AND ");
      sbufSql.append(OrderPubVO.SQL_PO_BODY);
      sbufSql.append(" AND ");
      sbufSql.append(OrderPubVO.SQL_PO_BODY_ACTIVE);

      String sFilterWhere = " po_order.cvendormangid IN (SELECT pk_cumandoc FROM bd_cumandoc WHERE frozenflag='N') AND po_order_b.cbaseid IN (SELECT pk_invbasdoc FROM bd_invbasdoc WHERE laborflag='N' AND discountflag='N')";

      String sBFromWhere = null;
      if (iUpStatus < 3)
      {
        String[] saTableAndWhere = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufSql.toString());

        saTableAndWhere = OrderPubDMO.dealFromWhere(strDataPowerSql, saTableAndWhere);

        if ((sVplancodeSql == null) && (!voBizStatus.isNeedrp()))
        {
          sBFromWhere = " FROM " + saTableAndWhere[0] + " WHERE " + saTableAndWhere[1] + " AND " + sFilterWhere + " AND " + OrderPubVO.SQL_PO_BODY_RP_NO + " AND " + "(po_order_b.nordernum>ISNULL(po_order_b.naccumarrvnum,0.0))";
        }

        String sBB1FromWhere = " FROM " + saTableAndWhere[0] + ",po_order_bb1" + " WHERE " + getReplacedSqlByBB1Info(saTableAndWhere[1]) + " AND po_order_bb1.corder_bid = po_order_b.corder_bid" + " AND " + sFilterWhere + " AND " + "(po_order_bb1.dr=0)" + " AND " + OrderPubVO.SQL_PO_BODY_RP_YES + " AND " + "(po_order_bb1.nordernum>ISNULL(po_order_bb1.naccumarrvnum,0.0))";

        voaOrder = queryOrderArrayForAfter_B(sQueryCorp, 7, sBFromWhere, sBB1FromWhere);
      }
      else
      {
        sbufSql.append(" AND po_order_b.nordernum>0");
        sbufSql.append(" AND ( po_order_b.corder_bid IN");
        sbufSql.append(" ( SELECT po_order_b.corder_bid");
        sbufSql.append(" \tFROM po_order_b JOIN po_order_bb ON po_order_bb.corder_bid = po_order_b.corder_bid");
        sbufSql.append(" \tWHERE po_order_bb.fonwaystatus=" + (iUpStatus - 3));
        sbufSql.append(" \tAND (po_order_bb.dr=0)");
        sbufSql.append(" \tGROUP BY po_order_b.corder_bid, ISNULL(naccumarrvnum, 0)");
        sbufSql.append(" \tHAVING  SUM(ISNULL(nonwaynum, 0)) > ISNULL(naccumarrvnum, 0) )");
        sbufSql.append(" )");
        String[] saRet = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufSql.toString());

        saRet = OrderPubDMO.dealFromWhere(strDataPowerSql, saRet);

        if ((sVplancodeSql == null) && (!voBizStatus.isNeedrp()))
        {
          sBFromWhere = " FROM " + saRet[0] + " WHERE " + saRet[1] + " AND " + sFilterWhere + " AND " + OrderPubVO.SQL_PO_BODY_RP_NO;
        }

        String sBB1FromWhere = " FROM " + saRet[0] + ",po_order_bb1" + " WHERE " + getReplacedSqlByBB1Info(saRet[1]) + " AND " + sFilterWhere + " AND " + "(po_order_bb1.dr=0)" + " AND " + OrderPubVO.SQL_PO_BODY_RP_YES + " AND po_order_bb1.corder_bid = po_order_b.corder_bid" + " AND " + "(po_order_bb1.nordernum>ISNULL(po_order_bb1.naccumarrvnum,0.0))";

        voaOrder = queryOrderArrayForAfter_BB(sQueryCorp, 7, iUpStatus, sBFromWhere, sBB1FromWhere);
      }

      if ((sVplancodeSql == null) && (!voBizStatus.isNeedrp())) {
        new OrderDMO().getWarehouseFromProduce("( SELECT po_order_b.corder_bid " + sBFromWhere + ")", voaOrder);
      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    return voaOrder;
  }

  public OrderVO[] queryOrderArrayForBackArrive(NormalCondVO[] voaNormal, ConditionVO[] voaDefined)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryOrderArrayForBackArrive(NormalCondVO [], ConditionVO [])";

    StringBuffer sbufSql = new StringBuffer();
    String sLoginCorp = OrderPubDMO.getPkCorpLoginFromNormalVOs(voaNormal);
    String sPurCorp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormal);

    sbufSql.append(" po_order_b.pk_arrvcorp = '" + sLoginCorp + "'");
    sbufSql.append(" AND po_order.forderstatus IN( " + BillStatus.AUDITED + "," + BillStatus.OUTPUT + ")");

    sbufSql.append(" AND ");
    sbufSql.append("(po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')");
    sbufSql.append(" AND ");
    sbufSql.append(OrderPubVO.SQL_PO_BODY);

    sbufSql.append(" AND ");
    sbufSql.append(OrderPubVO.SQL_PO_BODY_ACTIVE);
    String sVplancodeSql = OrderPubDMO.getVplancodeSqlFromNormalVOs(voaNormal);
    if (sVplancodeSql != null) {
      sbufSql.append(" AND ");
      sbufSql.append(sVplancodeSql);
    }

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(voaDefined);
    voaDefined = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);

    String sDefinedPart = null;
    try {
      sDefinedPart = OrderPubDMO.getWhereSQL(new String[] { sPurCorp }, voaDefined);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    if (sDefinedPart != null) {
      sbufSql.append(" AND ");
      sbufSql.append(sDefinedPart);
    }
    String[] saTableAndWhere = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufSql.toString());

    saTableAndWhere = OrderPubDMO.dealFromWhere(strDataPowerSql, saTableAndWhere);
    int tmp301_300 = 1;
    String[] tmp301_298 = saTableAndWhere; tmp301_298[tmp301_300] = (tmp301_298[tmp301_300] + " AND po_order.cvendormangid IN (SELECT pk_cumandoc FROM bd_cumandoc WHERE frozenflag='N')");
    int tmp326_325 = 1;
    String[] tmp326_323 = saTableAndWhere; tmp326_323[tmp326_325] = (tmp326_323[tmp326_325] + " AND po_order_b.cbaseid IN (SELECT pk_invbasdoc FROM bd_invbasdoc WHERE laborflag='N' AND discountflag='N')");
    int tmp351_350 = 1;
    String[] tmp351_348 = saTableAndWhere; tmp351_348[tmp351_350] = (tmp351_348[tmp351_350] + " AND po_order.cbiztype IN");
    int tmp376_375 = 1;
    String[] tmp376_373 = saTableAndWhere; tmp376_373[tmp376_375] = (tmp376_373[tmp376_375] + " (SELECT corderstatusid FROM po_orderstatus");
    int tmp401_400 = 1;
    String[] tmp401_398 = saTableAndWhere; tmp401_398[tmp401_400] = (tmp401_398[tmp401_400] + " WHERE pk_corp='" + sPurCorp + "'");
    int tmp437_436 = 1;
    String[] tmp437_434 = saTableAndWhere; tmp437_434[tmp437_436] = (tmp437_434[tmp437_436] + " AND " + OrderPubVO.SQL_PO_STATUS_ARRV_YES + ")");

    String sBFromWhere = null;
    if (sVplancodeSql == null) {
      sBFromWhere = " FROM " + saTableAndWhere[0] + ",po_orderstatus" + " WHERE " + saTableAndWhere[1] + " AND po_order.cbiztype=po_orderstatus.corderstatusid" + " AND po_orderstatus.pk_corp='" + sPurCorp + "' " + " AND " + OrderPubVO.SQL_PO_BODY_RP_NO + " AND (" + " \t\t(po_order_b.nordernum<0 AND ISNULL(po_order_b.nordernum,0)+ISNULL(po_order_b.nbackarrvnum,0)+ISNULL(po_order_b.nbackstorenum,0)<0 )" + " \tOR\t(po_order_b.nordernum>0 AND ISNULL(po_order_b.naccumarrvnum,0)-ISNULL(po_order_b.nbackarrvnum,0)-ISNULL(po_order_b.naccumstorenum,0)>0 " + "  \t\t\tAND ISNULL(po_orderstatus.bisneedrp," + OrderstatusVO.BISNEEDRP_NO + ")=" + OrderstatusVO.BISNEEDRP_NO + ")" + " )";
    }

    String sBB1FromWhere = " FROM " + saTableAndWhere[0] + ",po_order_bb1" + " WHERE " + getReplacedSqlByBB1Info(saTableAndWhere[1]) + " AND " + OrderPubVO.SQL_PO_BODY_RP_YES + " AND " + "(po_order_bb1.dr=0)" + " AND po_order_bb1.corder_bid = po_order_b.corder_bid" + " AND ( ISNULL(po_order_bb1.naccumarrvnum,0)-ISNULL(po_order_bb1.nbackarrvnum,0)-ISNULL(po_order_bb1.naccumstorenum,0)>0 )";

    OrderVO[] voaOrder = null;
    try
    {
      voaOrder = queryOrderArrayForAfter_B(sPurCorp, 10, sBFromWhere, sBB1FromWhere);

      if (voaOrder == null) {
        return null;
      }

      String sSubBIdSql = " SELECT  corder_bid FROM (";
      if (sBFromWhere != null) {
        sSubBIdSql = sSubBIdSql + "SELECT po_order_b.corder_bid " + sBFromWhere + " UNION ALL ";
      }
      sSubBIdSql = sSubBIdSql + "SELECT po_order_b.corder_bid " + sBB1FromWhere + ") AS A";
      setNNotEligNumForOrderVOs(voaOrder, sSubBIdSql);

      if (sVplancodeSql == null) {
        new OrderDMO().getWarehouseFromProduce("( SELECT po_order_b.corder_bid " + sBFromWhere + ")", voaOrder);
      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    if (voaOrder == null) {
      return null;
    }

    return voaOrder;
  }

  public OrderVO[] queryOrderArrayForBackStore(NormalCondVO[] voaNormal, ConditionVO[] voaDefined)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryOrderArrayForBackStore(NormalCondVO [], ConditionVO [])";

    StringBuffer sbufSql = new StringBuffer();
    String sLoginCorp = OrderPubDMO.getPkCorpLoginFromNormalVOs(voaNormal);
    String sPurCorp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormal);
    String sOprId = OrderPubDMO.getOperatorFromNormalVOs(voaNormal);

    sbufSql.append(" po_order_b.pk_arrvcorp = '" + sLoginCorp + "'");
    sbufSql.append(" AND po_order.forderstatus IN( " + BillStatus.AUDITED + "," + BillStatus.OUTPUT + ")");

    sbufSql.append(" AND ");
    sbufSql.append("(po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')");
    sbufSql.append(" AND ");
    sbufSql.append(OrderPubVO.SQL_PO_BODY);

    sbufSql.append(" AND ");
    sbufSql.append(OrderPubVO.SQL_PO_BODY_ACTIVE);

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(voaDefined);
    voaDefined = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);

    String sDefinedPart = null;
    try {
      sDefinedPart = OrderPubDMO.getWhereSQL(new String[] { sPurCorp }, voaDefined);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    if (sDefinedPart != null) {
      sbufSql.append(" AND ");
      sbufSql.append(sDefinedPart);
    }
    String[] saTableAndWhere = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufSql.toString());

    saTableAndWhere = OrderPubDMO.dealFromWhere(strDataPowerSql, saTableAndWhere);
    int tmp279_278 = 1;
    String[] tmp279_276 = saTableAndWhere; tmp279_276[tmp279_278] = (tmp279_276[tmp279_278] + " AND po_order.cvendormangid IN (SELECT pk_cumandoc FROM bd_cumandoc WHERE frozenflag='N')");
    int tmp304_303 = 1;
    String[] tmp304_301 = saTableAndWhere; tmp304_301[tmp304_303] = (tmp304_301[tmp304_303] + " AND po_order_b.cbaseid IN (SELECT pk_invbasdoc FROM bd_invbasdoc WHERE laborflag='N' AND discountflag='N')");
    int tmp329_328 = 1;
    String[] tmp329_326 = saTableAndWhere; tmp329_326[tmp329_328] = (tmp329_326[tmp329_328] + " AND (EXISTS (SELECT pk_calbody FROM bd_produce WHERE bd_produce.pk_calbody=po_order_b.pk_arrvstoorg AND bd_produce.pk_invbasdoc=po_order_b.cbaseid AND ISNULL(bd_produce.isused,'Y')='Y')  OR po_order_b.pk_arrvstoorg IS NULL) ");
    String sVplancodeSql = OrderPubDMO.getVplancodeSqlFromNormalVOs(voaNormal);
    if (sVplancodeSql != null)
    {
      int tmp365_364 = 1;
      String[] tmp365_362 = saTableAndWhere; tmp365_362[tmp365_364] = (tmp365_362[tmp365_364] + " AND " + sVplancodeSql);
    }

    String sBFromWhere = null;
    if (sVplancodeSql == null) {
      sBFromWhere = " FROM " + saTableAndWhere[0] + ",po_orderstatus" + " WHERE " + saTableAndWhere[1] + " AND po_order.cbiztype=po_orderstatus.corderstatusid" + " AND po_orderstatus.pk_corp= '" + sPurCorp + "' " + " AND " + OrderPubVO.SQL_PO_BODY_RP_NO + " AND (" + " \t\t(po_order_b.nordernum<0 AND ISNULL(po_order_b.nordernum,0)+ISNULL(po_order_b.nbackarrvnum,0)+ISNULL(po_order_b.nbackstorenum,0)<0 )" + " \tOR\t(po_order_b.nordernum>0 AND ISNULL(po_order_b.naccumstorenum,0)-ISNULL(po_order_b.nbackstorenum,0)>0 " + "\t\t\t AND po_order.cbiztype=po_orderstatus.corderstatusid AND po_orderstatus.pk_corp= '" + sPurCorp + "' AND ISNULL(po_orderstatus.bisneedrp," + OrderstatusVO.BISNEEDRP_NO + ")=" + OrderstatusVO.BISNEEDRP_NO + ")" + " )";
    }

    String sBB1FromWhere = " FROM " + saTableAndWhere[0] + ",po_order_bb1" + " WHERE " + getReplacedSqlByBB1Info(saTableAndWhere[1]) + " AND " + OrderPubVO.SQL_PO_BODY_RP_YES + " AND " + "(po_order_bb1.dr=0)" + " AND po_order_bb1.corder_bid = po_order_b.corder_bid" + " AND (ISNULL(po_order_bb1.naccumstorenum,0)-ISNULL(po_order_bb1.nbackstorenum,0)>0 )";

    OrderVO[] voaOrder = null;
    try
    {
      voaOrder = queryOrderArrayForAfter_B(sPurCorp, 11, sBFromWhere, sBB1FromWhere);
      try
      {
        voaOrder = (OrderVO[])(OrderVO[])new OrderDMO().filterVosForIc(voaOrder, sLoginCorp, sOprId, "21");
      } catch (NamingException e) {
        SCMEnv.out(e);
        throw new BusinessException(e.getMessage());
      } catch (SystemException e) {
        SCMEnv.out(e);
        throw new BusinessException(e.getMessage());
      }
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return voaOrder;
  }

  public OrderVO[] queryOrderArrayForInvoice(NormalCondVO[] voaNormal, ConditionVO[] voaDefined)
    throws BusinessException
  {
    String sQueryCorp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormal);
    String sBizType = OrderPubDMO.getBizTypeIdFromNormalVOs(voaNormal);

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(voaDefined);
    voaDefined = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);

    String sDefinedWhere = null;
    try {
      sDefinedWhere = OrderPubDMO.getWhereSQL(new String[] { sQueryCorp }, voaDefined);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    StringBuffer sbufUIWhere = new StringBuffer();

    sbufUIWhere.append(" po_order_b.pk_invoicecorp='");
    sbufUIWhere.append(sQueryCorp);
    sbufUIWhere.append("'");

    sbufUIWhere.append(" AND po_order.cbiztype='" + sBizType + "'");

    sbufUIWhere.append(" AND ");
    sbufUIWhere.append("(po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')");
    sbufUIWhere.append(" AND ");
    sbufUIWhere.append(OrderPubVO.SQL_PO_BODY);

    sbufUIWhere.append(" AND ");
    sbufUIWhere.append(OrderPubVO.SQL_PO_AUDIT_OR_OUTPUT);

    sbufUIWhere.append(" AND (");
    sbufUIWhere.append("\t\t( ISNULL(po_order_b.nordernum,0.0)>=0 AND ISNULL(po_order_b.nordernum,0.0)>ISNULL(po_order_b.naccuminvoicenum,0.0) )");
    sbufUIWhere.append("\tOR\t( po_order_b.nordernum<0 AND ISNULL(po_order_b.nordernum,0.0)<ISNULL(po_order_b.naccuminvoicenum,0.0) )");
    sbufUIWhere.append("\t)");

    if (sDefinedWhere != null) {
      sbufUIWhere.append(" AND (");
      sbufUIWhere.append(sDefinedWhere);
      sbufUIWhere.append(" )");
    }

    String[] saTableAndWhere = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufUIWhere.toString());

    saTableAndWhere = OrderPubDMO.dealFromWhere(strDataPowerSql, saTableAndWhere);

    String sFilterWhere = " AND po_order.cvendormangid IN (SELECT pk_cumandoc FROM bd_cumandoc WHERE frozenflag='N') AND po_order_b.cbaseid IN (SELECT pk_invbasdoc FROM bd_invbasdoc)";

    sFilterWhere = sFilterWhere + " AND COALESCE(po_order_b.blargess,'N') = 'N'";
    int tmp318_317 = 1;
    String[] tmp318_315 = saTableAndWhere; tmp318_315[tmp318_317] = (tmp318_315[tmp318_317] + sFilterWhere);
    try
    {
      return new OrderDMO().queryOrderArrayByConds(" FROM " + saTableAndWhere[0] + " WHERE " + saTableAndWhere[1]);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    return null;
  }

  public OrderVO[] queryOrderArrayForSo(NormalCondVO[] voaNormal, ConditionVO[] voaDefined)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryOrderArrayForSo(NormalCondVO [], ConditionVO [])";

    String sQueryCorp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormal);

    String sDefinedWhere = null;

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(voaDefined);
    voaDefined = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);
    try
    {
      sDefinedWhere = OrderPubDMO.getWhereSQL(new String[] { sQueryCorp }, voaDefined);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    String sVendBaseId = null;
    int iNormalLen = voaNormal.length;
    for (int i = 0; i < iNormalLen; i++) {
      if (voaNormal[i].getKey().equals("供应商基本ID")) {
        sVendBaseId = (String)voaNormal[i].getValue();
      }
    }
    StringBuffer sbufUIWhere = new StringBuffer();

    sbufUIWhere.append(" po_order.pk_corp='");
    sbufUIWhere.append(sQueryCorp);
    sbufUIWhere.append("'");

    if ((sVendBaseId != null) && (sVendBaseId.trim().length() > 0)) {
      sbufUIWhere.append(" AND po_order.cvendorbaseid='");
      sbufUIWhere.append(sVendBaseId);
      sbufUIWhere.append("'");
    }
    sbufUIWhere.append(" AND ");
    sbufUIWhere.append("(po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')");
    sbufUIWhere.append(" AND ");
    sbufUIWhere.append(OrderPubVO.SQL_PO_AUDIT_OR_OUTPUT);
    sbufUIWhere.append(" AND ");
    sbufUIWhere.append(OrderPubVO.SQL_PO_BODY_ACTIVE);

    String sForbidSaleWhere = " po_order_b.cmangid IN (SELECT pk_invmandoc FROM bd_invmandoc WHERE pk_corp='" + sQueryCorp + "' AND iscansold='Y')";
    sbufUIWhere.append(" AND ");
    sbufUIWhere.append(sForbidSaleWhere);
    if (sDefinedWhere != null) {
      sbufUIWhere.append(" AND (");
      sbufUIWhere.append(sDefinedWhere);
      sbufUIWhere.append(" )");
    }

    String sLoginCorpId = OrderPubDMO.getPkCorpLoginFromNormalVOs(voaNormal);
    String sLoginUserId = OrderPubDMO.getOperatorFromNormalVOs(voaNormal);
    return queryOrderArrayByConds(sbufUIWhere.toString(), OrderPubVO.SQL_PO_BODY_ACTIVE + " AND " + sForbidSaleWhere, sLoginCorpId, sLoginUserId, strDataPowerSql);
  }

  public OrderVO[] queryOrderArrayForStore(NormalCondVO[] voaNormal, ConditionVO[] voaDefined)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryOrderArrayForStore(NormalCondVO [], ConditionVO [])";
    String sLoginCorp = OrderPubDMO.getPkCorpLoginFromNormalVOs(voaNormal);
    String sQueryCorp = OrderPubDMO.getPkCorpQueryFromNormalVOs(voaNormal);
    String sBizType = OrderPubDMO.getBizTypeIdFromNormalVOs(voaNormal);
    String sOprId = OrderPubDMO.getOperatorFromNormalVOs(voaNormal);

    if ((sQueryCorp == null) || (sQueryCorp.trim().length() < 1) || (sBizType == null) || (sBizType.trim().length() < 1))
    {
      SCMEnv.out(sMethodName + "传入参数不正确");
      return null;
    }

    StringBuffer sbufSql = new StringBuffer("po_order.cbiztype = '" + sBizType + "'");

    if (sLoginCorp != null) {
      sbufSql.append(" AND po_order_b.pk_arrvcorp = '" + sLoginCorp + "'");
    }

    sbufSql.append(" AND ");
    sbufSql.append("(po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')");

    sbufSql.append(" AND ");
    sbufSql.append(OrderPubVO.SQL_PO_BODY);
    sbufSql.append(" AND po_order_b.nordernum>0");

    sbufSql.append(" AND ");
    sbufSql.append(OrderPubVO.SQL_PO_BODY_ACTIVE);

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(voaDefined);
    voaDefined = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);

    String sDefinedPart = null;
    try {
      sDefinedPart = OrderPubDMO.getWhereSQL(new String[] { sQueryCorp }, voaDefined);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    if ((sDefinedPart != null) && (sDefinedPart.trim().length() > 0)) {
      sbufSql.append(" AND (");
      sbufSql.append(sDefinedPart);
      sbufSql.append(")");
    }

    String sFilterWhere = " po_order.cvendormangid IN (SELECT pk_cumandoc FROM bd_cumandoc WHERE frozenflag='N') AND po_order_b.cbaseid IN (SELECT pk_invbasdoc FROM bd_invbasdoc WHERE laborflag='N' AND discountflag='N') AND ( EXISTS (SELECT pk_calbody FROM bd_produce WHERE (bd_produce.pk_calbody=po_order_b.pk_arrvstoorg AND bd_produce.pk_invbasdoc=po_order_b.cbaseid  AND ISNULL(bd_produce.isused,'Y')='Y')) OR po_order_b.pk_arrvstoorg IS NULL  ) ";

    OrderstatusVO voBizStatus = null;
    try {
      voBizStatus = new OrderstatusDMO().queryStatusVOByBizTypeID(sBizType, sQueryCorp);
      if (voBizStatus == null) {
        SCMEnv.out("业务类型[主键为：" + sBizType + "]在采购公司[主键为：" + sQueryCorp + "]未初始化，本次查询返回NULL");
        return null;
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    int iUpStatus = voBizStatus.getUpStatus(8, true);

    if (OrderPubDMO.isPoAuditToStoreFromNormalVOs(voaNormal)) {
      OrderVO voOrder = OrderPubDMO.getOrderVOFromNormalVOs(voaNormal);

      String sOrderId = voOrder.getHeadVO().getCorderid();
      sbufSql.append(" AND po_order.corderid = '" + sOrderId + "'");

      OrderVO[] voaRet = null;
      try {
        String[] saRet = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufSql.toString());

        saRet = OrderPubDMO.dealFromWhere(strDataPowerSql, saRet);
        int tmp511_510 = 1;
        String[] tmp511_508 = saRet; tmp511_508[tmp511_510] = (tmp511_508[tmp511_510] + " AND " + sFilterWhere);
        int tmp541_540 = 1;
        String[] tmp541_538 = saRet; tmp541_538[tmp541_540] = (tmp541_538[tmp541_540] + " AND " + OrderPubVO.SQL_PO_AUDIT);
        voaRet = queryOrderArrayForAfter_B(sQueryCorp, 8, " FROM " + saRet[0] + " WHERE " + saRet[1], " FROM po_order_bb1 WHERE po_order_bb1.corderid ='" + sOrderId + "' AND " + "(po_order_bb1.dr=0)");

        if (voaRet == null) {
          return null;
        }
        if (voBizStatus.isArrive()) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000205"));
        }

        if (voBizStatus.isNeedrp()) {
          int iBodyLen = voaRet[0].getBodyVO().length;
          for (int i = 0; i < iBodyLen; i++) {
            if (!voaRet[0].getBodyVO()[i].isHaveRP())
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000206"));
          }
        }
      }
      catch (Exception e)
      {
        PubDMO.throwBusinessException(sMethodName, e);
      }

      OrderVO.splitOrderVOByRPVOs(8, voaRet);
      return voaRet;
    }

    if (voBizStatus.isArrive()) {
      return null;
    }

    sbufSql.append(" AND (" + OrderPubVO.SQL_PO_AUDIT + " OR " + OrderPubVO.SQL_PO_OUTPUT + ") ");

    String sVplancodeSql = OrderPubDMO.getVplancodeSqlFromNormalVOs(voaNormal);

    if (iUpStatus < 3)
    {
      String[] saRet = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufSql.toString());

      saRet = OrderPubDMO.dealFromWhere(strDataPowerSql, saRet);
      int tmp867_866 = 1;
      String[] tmp867_864 = saRet; tmp867_864[tmp867_866] = (tmp867_864[tmp867_866] + " AND " + sFilterWhere);
      if (sVplancodeSql != null)
      {
        int tmp902_901 = 1;
        String[] tmp902_899 = saRet; tmp902_899[tmp902_901] = (tmp902_899[tmp902_901] + " AND " + sVplancodeSql);
      }

      String sBFromWhere = null;

      if ((sVplancodeSql == null) && (!voBizStatus.isNeedrp()))
      {
        sBFromWhere = " FROM " + saRet[0] + " WHERE " + saRet[1] + " AND " + OrderPubVO.SQL_PO_BODY_RP_NO + " AND " + "(po_order_b.nordernum>ISNULL(po_order_b.naccumstorenum,0.0))";
      }

      String sBB1FromWhere = " FROM " + saRet[0] + ",po_order_bb1" + " WHERE " + getReplacedSqlByBB1Info(saRet[1]) + " AND po_order_bb1.corder_bid = po_order_b.corder_bid" + " AND " + "(po_order_bb1.dr=0)" + " AND " + OrderPubVO.SQL_PO_BODY_RP_YES + " AND " + "(po_order_bb1.nordernum>ISNULL(po_order_bb1.naccumstorenum,0.0))";

      OrderVO[] voaOrder = queryOrderArrayForAfter_B(sQueryCorp, 8, sBFromWhere, sBB1FromWhere);
      try
      {
        voaOrder = (OrderVO[])(OrderVO[])new OrderDMO().filterVosForIc(voaOrder, sLoginCorp, sOprId, "21");
      } catch (NamingException e) {
        SCMEnv.out(e);
        throw new BusinessException(e.getMessage());
      } catch (SystemException e) {
        SCMEnv.out(e);
        throw new BusinessException(e.getMessage());
      }

      return voaOrder;
    }

    sbufSql.append(" AND (po_order_b.corder_bid IN (SELECT po_order_b.corder_bid");
    sbufSql.append(" FROM po_order_b JOIN po_order_bb ON po_order_bb.corder_bid = po_order_b.corder_bid");
    sbufSql.append(" WHERE po_order_bb.fonwaystatus=" + (iUpStatus - 3));
    sbufSql.append(" AND po_order_bb.dr=0");
    sbufSql.append(" GROUP BY po_order_b.corder_bid, ISNULL(naccumstorenum, 0)");
    sbufSql.append(" HAVING SUM(ISNULL(nonwaynum, 0)) > ISNULL(naccumstorenum, 0) )");
    sbufSql.append(" )");
    String[] saRet = OrderPubVO.getSourceSql("po_order", "po_order_b", sbufSql.toString());

    saRet = OrderPubDMO.dealFromWhere(strDataPowerSql, saRet);
    int tmp1294_1293 = 1;
    String[] tmp1294_1291 = saRet; tmp1294_1291[tmp1294_1293] = (tmp1294_1291[tmp1294_1293] + " AND " + sFilterWhere);
    if (sVplancodeSql != null)
    {
      int tmp1329_1328 = 1;
      String[] tmp1329_1326 = saRet; tmp1329_1326[tmp1329_1328] = (tmp1329_1326[tmp1329_1328] + " AND " + sVplancodeSql);
    }

    String sBFromWhere = null;

    if ((sVplancodeSql == null) && (!voBizStatus.isNeedrp())) {
      sBFromWhere = " FROM " + saRet[0] + " WHERE " + saRet[1] + " AND " + OrderPubVO.SQL_PO_BODY_RP_NO;
    }

    String sBB1FromWhere = " FROM " + saRet[0] + ",po_order_bb1" + " WHERE " + getReplacedSqlByBB1Info(saRet[1]) + " AND po_order_bb1.corder_bid = po_order_b.corder_bid" + " AND " + sFilterWhere + " AND " + "(po_order_bb1.dr=0)" + " AND " + OrderPubVO.SQL_PO_BODY_RP_YES + " AND " + "(po_order_bb1.nordernum>ISNULL(po_order_bb1.naccumstorenum,0.0))";

    OrderVO[] voaOrder = queryOrderArrayForAfter_BB(sQueryCorp, 8, iUpStatus, sBFromWhere, sBB1FromWhere);
    try
    {
      voaOrder = (OrderVO[])(OrderVO[])new OrderDMO().filterVosForIc(voaOrder, sLoginCorp, sOprId, "21");
    } catch (NamingException e) {
      SCMEnv.out(e);
      throw new BusinessException(e.getMessage());
    } catch (SystemException e) {
      SCMEnv.out(e);
      throw new BusinessException(e.getMessage());
    }

    return voaOrder;
  }

  public OrderVO[] queryOrderArrayOnlyBodyByHIdTsTY(String[] saHId, String[] saHTs, String strWherePartBody)
    throws BusinessException
  {
    if ((saHId == null) || (saHTs == null)) {
      return null;
    }

    int iVOLen = saHId.length;
    ArrayList listTempTableValue = new ArrayList();
    for (int i = 0; i < iVOLen; i++) {
      ArrayList listElement = new ArrayList();
      listElement.add(saHId[i]);
      listElement.add(saHTs[i]);

      listTempTableValue.add(listElement);
    }

    HashMap hmapBody = new HashMap();
    try
    {
      String sTempTableName = null;
      if (saHId.length > 1) {
        sTempTableName = new TempTableDMO().getTempStringTable("t_pu_po_018", new String[] { "corderid", "hts" }, new String[] { "char(20) not null ", "char(19) not null " }, null, listTempTableValue);
      }

      StringBuffer sbufFromWhere = new StringBuffer();
      sbufFromWhere.append(" FROM po_order JOIN po_order_b ON po_order.corderid=po_order_b.corderid");
      if (sTempTableName == null) {
        sbufFromWhere.append(" and po_order.corderid='" + saHId[0] + "' AND po_order.ts='" + saHTs[0] + "' ");
      }
      else {
        sbufFromWhere.append(" JOIN " + sTempTableName + " ON po_order.corderid=" + sTempTableName + ".corderid AND po_order.ts=" + sTempTableName + ".hts");
      }

      sbufFromWhere.append(" WHERE ");
      sbufFromWhere.append(OrderPubVO.SQL_PO_BODY);

      if (strWherePartBody != null) {
        sbufFromWhere.append("" + strWherePartBody);
      }

      hmapBody = new OrderDMO().queryBodyHashmapHIDAsKeyByConds(sbufFromWhere.toString());
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    if ((hmapBody == null) || (hmapBody.size() != iVOLen)) {
      PubDMO.throwBusinessException(new BusinessException(PuPubVO.MESSAGE_CONCURRENT));
    }

    OrderVO[] voaOrder = new OrderVO[iVOLen];
    for (int i = 0; i < iVOLen; i++)
    {
      Vector vecBody = (Vector)hmapBody.get(saHId[i]);
      int iVecBodylen = vecBody.size();

      voaOrder[i] = new OrderVO();

      OrderItemVO[] itemVOs = new OrderItemVO[iVecBodylen];
      vecBody.copyInto(itemVOs);

      voaOrder[i].setChildrenVO(itemVOs);
      voaOrder[i].setParentVO(new OrderHeaderVO(itemVOs[0].getCorderid()));
    }

    return voaOrder;
  }

  public OrderVO[] queryOrderArrayOnlyHead(NormalCondVO[] voaNormalCond, ConditionVO[] voaDefinedCond)
    throws BusinessException
  {
    Timer timer = new Timer();
    timer.start(" 订单维护查询　");

    String sMethodName = "nc.bs.po.OrderImpl.queryOrderArrayOnlyHead(NormalCondVO [], ConditionVO [])";

    ArrayList listRet = OrderPubDMO.dealCondVosForPower(voaDefinedCond);
    voaDefinedCond = (ConditionVO[])(ConditionVO[])listRet.get(0);
    String strDataPowerSql = (String)listRet.get(1);

    String[] saTableAndWhere = getOrderUIFromAndWhere(voaNormalCond, voaDefinedCond);

    saTableAndWhere = OrderPubDMO.dealFromWhere(strDataPowerSql, saTableAndWhere);
    int tmp77_76 = 1;
    String[] tmp77_74 = saTableAndWhere; tmp77_74[tmp77_76] = (tmp77_74[tmp77_76] + " AND (po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')");
    int tmp102_101 = 1;
    String[] tmp102_99 = saTableAndWhere; tmp102_99[tmp102_101] = (tmp102_99[tmp102_101] + " AND " + OrderPubVO.SQL_PO_BODY);

    OrderVO[] voaRet = null;
    OrderDMO dmoOrder = null;
    try {
      dmoOrder = new OrderDMO();

      HashMap mapRet = dmoOrder.queryHeadHashMapByConds(" FROM " + saTableAndWhere[0] + " WHERE " + saTableAndWhere[1]);
      if (mapRet == null) {
        return null;
      }

      voaRet = OrderPubDMO.getOrderVOsFromHashMap(mapRet);

      boolean bConstrictClosedLines = false;
      boolean bOnlyClosedLines = false;

      int iLen = voaNormalCond == null ? 0 : voaNormalCond.length;

      for (int i = 0; i < iLen; i++) {
        if ((voaNormalCond[i].getKey().equals("ConstrictClosedLines")) && 
          (((String)voaNormalCond[i].getValue()).equals("Y"))) {
          bConstrictClosedLines = true;
        }

        if ((!voaNormalCond[i].getKey().equals("OnlyClosedLines")) || 
          (!((String)voaNormalCond[i].getValue()).equals("Y"))) continue;
        bOnlyClosedLines = true;
      }

      String strWherePart = null;
      if (bConstrictClosedLines) {
        strWherePart = OrderPubVO.SQL_PO_BODY_ACTIVE;
      }
      if (bOnlyClosedLines) {
        strWherePart = OrderPubVO.SQL_PO_BODY_CLOSE;
      }
      voaRet[0].setChildrenVO(dmoOrder.queryBodyByHeadId(voaRet[0].getHeadVO().getCorderid(), strWherePart));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    timer.showTime(" 组合数据　");

    dmoOrder.fillVosIfExistClosedRows(voaRet);
    return voaRet;
  }

  public OrderVO queryOrderVOByKey(String sId)
    throws BusinessException
  {
    OrderVO[] voaOrder = queryOrderVOsByIds(new String[] { sId }, null, null);
    return voaOrder == null ? null : voaOrder[0];
  }

  public OrderVO queryOrderVOLight(String strId, String strOprType)
    throws BusinessException
  {
    if ((strId == null) || (strOprType == null)) {
      SCMEnv.out("传入参数为空，直接返回NULL");
      return null;
    }
    OrderVO voOrder = null;
    try {
      OrderDMO dmo = new OrderDMO();
      if ("AUDIT".equalsIgnoreCase(strOprType))
        voOrder = dmo.queryOrderVOLightAudit(strId);
      else if ("UNAUDIT".equalsIgnoreCase(strOprType))
        voOrder = dmo.queryOrderVOLightUnAudit(strId);
      else if ("SAVE".equalsIgnoreCase(strOprType))
        voOrder = dmo.queryOrderVOLightSave(strId);
      else
        SCMEnv.out("不支持的操作类型");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return voOrder;
  }

  public OrderVO queryOrderVOForQuickArrive(String strArrvCorp, String sOrderCode)
    throws BusinessException
  {
    ArrayList listHids = new ArrayList();
    ArrayList listPurCorps = new ArrayList();
    try {
      PubDMO dmo = new PubDMO();
      Object[][] oa2Ret = dmo.queryArrayValue("po_order", "vordercode", new String[] { "corderid", "pk_corp" }, new String[] { sOrderCode }, "dr=0");
      if ((oa2Ret != null) && (oa2Ret.length > 0)) {
        for (int i = 0; i < oa2Ret.length; i++) {
          if ((oa2Ret[i] == null) || (oa2Ret[i].length == 0) || (oa2Ret[i][0] == null)) {
            continue;
          }
          listHids.add(oa2Ret[i][0].toString());
          listPurCorps.add(oa2Ret[i][1].toString());
        }
      }
      if (listHids.size() == 0) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("common", "4004COMMON000000077", null, new String[] { sOrderCode }));
      }
      if (listHids.size() > 1)
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("common", "4004COMMON000000078"));
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw new BusinessException(e.getMessage());
    }
    return queryOrderVOForQuickArrive(strArrvCorp, sOrderCode, (String)listHids.get(0), (String)listPurCorps.get(0));
  }

  private OrderVO queryOrderVOForQuickArrive(String strArrvCorp, String sOrderCode, String strOrderHid, String strPurCorp)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryOrderVOByCode(String, String)";

    if ((PuPubVO.getString_TrimZeroLenAsNull(strArrvCorp) == null) || (PuPubVO.getString_TrimZeroLenAsNull(strOrderHid) == null) || (PuPubVO.getString_TrimZeroLenAsNull(strPurCorp) == null))
    {
      return null;
    }

    String sBizType = null;
    try {
      String strHeadSql = " FROM po_order WHERE corderid='" + strOrderHid + "' AND " + "(po_order.dr=0 AND ISNULL(po_order.bislatest,'Y')='Y')";
      OrderHeaderVO[] voaHead = new OrderDMO().queryHeadArrayByConds(strHeadSql);
      if (voaHead == null) {
        return null;
      }
      sBizType = voaHead[0].getCbiztype();
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    NormalCondVO[] voaNormal = new NormalCondVO[4];
    voaNormal[0] = new NormalCondVO("订单号", sOrderCode);
    voaNormal[1] = new NormalCondVO("待查询公司", strPurCorp);
    voaNormal[2] = new NormalCondVO("业务类型", sBizType);
    voaNormal[3] = new NormalCondVO("登陆公司", strArrvCorp);

    OrderVO[] voaOrder = queryOrderArrayForArrive(voaNormal, null);

    if (voaOrder != null)
    {
      OrderVO.splitOrderVOByRPVOs(7, voaOrder);
    }

    return voaOrder == null ? null : voaOrder[0];
  }

  public OrderVO queryForOrderBillLinkAdd(ClientLink cl, String strHid)
    throws BusinessException
  {
    OrderVO voRet = null;

    if ((cl == null) || (strHid == null)) {
      SCMEnv.out("传入参数为空，直接返回空");
      SCMEnv.out(new Exception());
      return null;
    }
    String strArrvCorp = cl.getCorp();
    String strBillCode = null;
    String strPurCorp = null;
    try {
      PubDMO dmo = new PubDMO();
      Object[][] oa2Ret = dmo.queryArrayValue("po_order", "corderid", new String[] { "vordercode", "pk_corp" }, new String[] { strHid }, "dr=0");
      if ((oa2Ret != null) && (oa2Ret.length > 0) && (oa2Ret[0] != null) && (oa2Ret[0].length >= 2)) {
        strBillCode = (String)oa2Ret[0][0];
        strPurCorp = (String)oa2Ret[0][1];
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      throw new BusinessException(e.getMessage());
    }
    voRet = queryOrderVOForQuickArrive(strArrvCorp, strBillCode, strHid, strPurCorp);

    return voRet;
  }

  public OrderVO[] queryOrderVOsByIds(String[] saId, String sLoginCorpId, String sLoginUserId)
    throws BusinessException
  {
    if (saId == null) {
      return null;
    }

    String sIdSubSql = null;
    try {
      sIdSubSql = new TempTableDMO().insertTempTable(saId, "t_pu_general", "pk_pu");
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    return queryOrderArrayByConds("po_order.corderid IN " + sIdSubSql, null, sLoginCorpId, sLoginUserId, null);
  }

  public ArrayList queryPrayHBTSInAOrder(String sOrderId)
    throws BusinessException
  {
    String sMethodName = "";

    if (sOrderId == null) {
      SCMEnv.out("nc.bs.po.OrderImpl.setVOUpSourcePrayTS(OrderVO)传入参数不正确！");
      return null;
    }

    Hashtable htHTs = null;
    Hashtable htBTs = null;
    try {
      PubDMO dmoPub = new PubDMO();
      htHTs = dmoPub.queryHtResultFromAnyTable("po_praybill", "cpraybillid", new String[] { "ts" }, "cpraybillid IN (SELECT cupsourcebillid FROM po_order_b WHERE corderid='" + sOrderId + "' AND dr=0 AND iisactive<>" + OrderItemVO.IISACTIVE_REVISION + " AND cupsourcebilltype='" + "20" + "')");

      if (htHTs == null) {
        return null;
      }
      htBTs = dmoPub.queryHtResultFromAnyTable("po_praybill_b", "cpraybill_bid", new String[] { "ts" }, "cpraybill_bid IN (SELECT cupsourcebillrowid FROM po_order_b WHERE corderid='" + sOrderId + "' AND dr=0 AND iisactive<>" + OrderItemVO.IISACTIVE_REVISION + " AND cupsourcebilltype='" + "20" + "')");

      if (htBTs == null)
        return null;
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    int iHLen = htHTs.size();
    String[] saHKey = new String[iHLen];
    htHTs.keySet().toArray(saHKey);
    String[][] saHTs = new String[iHLen][2];
    for (int i = 0; i < iHLen; i++) {
      saHTs[i][0] = saHKey[i];
      Vector vecTs = (Vector)htHTs.get(saHKey[i]);
      String sHTs = (String)((Object[])(Object[])vecTs.get(0))[0];
      saHTs[i][1] = sHTs;
    }

    int iBLen = htBTs.size();
    String[] saBKey = new String[iBLen];
    htBTs.keySet().toArray(saBKey);
    String[][] saBTs = new String[iBLen][2];
    for (int i = 0; i < iBLen; i++) {
      saBTs[i][0] = saBKey[i];
      Vector vecTs = (Vector)htBTs.get(saBKey[i]);
      String sBTs = (String)((Object[])(Object[])vecTs.get(0))[0];
      saBTs[i][1] = sBTs;
    }

    ArrayList listRet = new ArrayList(2);
    listRet.add(saHTs);
    listRet.add(saBTs);
    return listRet;
  }

  private UFDouble[] queryPricesFromPara(RetPoVrmAndParaPriceVO voPara, String sPara, int iPricePolicy)
    throws BusinessException
  {
    if ((voPara == null) || (voPara.getSaInvMangId() == null)) {
      return null;
    }
    String[] saStoOrgId = voPara.getStoOrgIds();
    int iLen = saStoOrgId.length;
    String sBizTypeId = voPara.getBizTypeId();

    Timer timeDebug = new Timer();
    timeDebug.start();

    UFDouble[] daPrice = new UFDouble[iLen];

    ArrayList listPos = new ArrayList();

    ArrayList listMangId = new ArrayList();
    String[] saMangId = voPara.getSaInvMangId();
    for (int i = 0; i < iLen; i++) {
      if (saStoOrgId[i] != null)
        listPos.add(new Integer(i));
      else {
        listMangId.add(saMangId[i]);
      }
    }

    saMangId = (String[])listMangId.toArray(new String[listMangId.size()]);
    int iLenReal = listPos.size();

    String[] saStoOrgIdReal = new String[iLenReal];

    String[] saBaseIdReal = new String[iLenReal];

    String[] saBaseId = voPara.getSaInvBaseId();
    int iPosNotNull = 0;
    for (int i = 0; i < iLenReal; i++) {
      iPosNotNull = ((Integer)listPos.get(i)).intValue();
      saStoOrgIdReal[i] = saStoOrgId[iPosNotNull];
      saBaseIdReal[i] = saBaseId[iPosNotNull];
    }
    try {
      OrderDMO dmo = new OrderDMO();

      if (sPara.equals("最新进价")) {
        daPrice = dmo.getNewPrices(voPara.getPk_corp(), voPara.getSaInvMangId(), voPara.getSaCurrId(), iPricePolicy);
        return daPrice;
      }if (sPara.equals("最低进价")) {
        daPrice = dmo.getLowPrices(voPara.getPk_corp(), voPara.getSaInvMangId(), voPara.getSaCurrId(), iPricePolicy);
        return daPrice;
      }if (sPara.equals("参考成本"))
      {
        if ((CentrPurchaseUtil.isGroupBusiType(sBizTypeId)) || (iLenReal == 0)) {
          HashMap hCostPrice = dmo.queryCostPricesFrmInvman(voPara.getPk_corp(), voPara.getSaInvMangId());
          for (int i = 0; i < voPara.getSaInvMangId().length; i++) {
            daPrice[i] = ((UFDouble)hCostPrice.get(voPara.getSaInvMangId()[i]));
          }

        }
        else
        {
          UFDouble[] uaPriceProd = dmo.getCostPrices(saStoOrgIdReal, saBaseIdReal);

          HashMap hCostPrice = new HashMap();
          if ((saMangId != null) && (saMangId.length > 0)) {
            hCostPrice = dmo.queryCostPricesFrmInvman(voPara.getPk_corp(), saMangId);
          }

          int iPos = 0;
          for (int i = 0; i < iLen; i++) {
            if (saStoOrgId[i] != null)
              daPrice[i] = uaPriceProd[(iPos++)];
            else
              daPrice[i] = ((UFDouble)hCostPrice.get(saMangId[i]));
          }
        }
      }
      else if (sPara.equals("计划价"))
      {
        if ((CentrPurchaseUtil.isGroupBusiType(sBizTypeId)) || (iLenReal == 0)) {
          HashMap hPlanPrice = dmo.queryPlanPricesFrmInvMan(voPara.getSaInvMangId(), voPara.getPk_corp());
          for (int i = 0; i < voPara.getSaInvMangId().length; i++) {
            daPrice[i] = ((UFDouble)hPlanPrice.get(voPara.getSaInvMangId()[i]));
          }

        }
        else
        {
          UFDouble[] uaPriceProd = dmo.getPlanPrices(saStoOrgIdReal, saBaseIdReal);

          HashMap hPlanPrice = new HashMap();
          if ((saMangId != null) && (saMangId.length > 0)) {
            hPlanPrice = dmo.queryPlanPricesFrmInvMan(saMangId, voPara.getPk_corp());
          }

          int iPos = 0;
          for (int i = 0; i < iLen; i++) {
            if (saStoOrgId[i] != null)
              daPrice[i] = uaPriceProd[(iPos++)];
            else
              daPrice[i] = ((UFDouble)hPlanPrice.get(saMangId[i]));
          }
        }
      }
      else if (sPara.equals("无默认")) {
        return daPrice;
      }
      timeDebug.addExecutePhase("查询价格");

      if (daPrice != null) {
        BusinessCurrencyRateUtil currArith = new BusinessCurrencyRateUtil(voPara.getPk_corp());
        boolean bNeedARate = currArith.isBlnLocalFrac();
        for (int i = 0; i < daPrice.length; i++) {
          if (daPrice[i] == null) continue; if ((PuPubVO.getString_TrimZeroLenAsNull(voPara.getSaCurrId() == null ? null : voPara.getSaCurrId()[i]) == null) || (voPara.getDaBRate()[i] == null) || ((bNeedARate) && (voPara.getDaARate()[i] == null)))
          {
            continue;
          }

          daPrice[i] = currArith.getOriginAmountByOpp(voPara.getSaCurrId()[i], voPara.getSaCurrId()[i], daPrice[i], voPara.getDaBRate()[i] == null ? null : voPara.getDaBRate()[i], voPara.getDOrderDate().toString());
        }

      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }
    timeDebug.addExecutePhase("价格换算");
    timeDebug.showAllExecutePhase("订单BS采购价格");

    return daPrice;
  }

  public RetPoVrmAndParaPriceVO queryVrmAndParaPrices(RetPoVrmAndParaPriceVO voPara)
    throws BusinessException
  {
    String pk_corp = voPara.getPk_corp();

    String[] saStoOrgId = voPara.getStoOrgIds();

    String sVendMangId = voPara.getVendMangId();
    String[] saMangId = voPara.getSaInvMangId();
    String[] saBaseId = voPara.getSaInvBaseId();
    String[] saCurrId = voPara.getSaCurrId();
    UFDouble[] daBRate = voPara.getDaBRate();
    UFDouble[] daARate = voPara.getDaARate();
    UFDate dOrderDate = voPara.getDOrderDate();

    Timer timeDebug = new Timer();
    timeDebug.start();

    int iTotalLen = saBaseId.length;
    UFDouble[] daPrice = null;

    RetPoVrmAndParaPriceVO voRet = new RetPoVrmAndParaPriceVO(iTotalLen);
    try {
      int iPriorPrice = PubDMO.getPricePriorPolicy(pk_corp);

      IAsk queryPrice = (IAsk)NCLocator.getInstance().lookup(IAsk.class.getName());

      String strDate = null;
      if (voPara.getClientLink() != null)
        strDate = voPara.getClientLink().getLogonDate().toString();
      else if (voPara.getDOrderDate() != null)
        strDate = voPara.getDOrderDate().toString();
      else {
        strDate = new UFDate(System.currentTimeMillis()).toString();
      }

      daPrice = queryPrice.queryPriceForPO(saMangId, new String[] { sVendMangId }, saCurrId, iPriorPrice == 6 ? "无税价格优先" : "含税价格优先", strDate);

      timeDebug.addExecutePhase("调用询报价接口取得价格");

      Vector vParaIndex = new Vector();
      if (daPrice == null) {
        daPrice = new UFDouble[iTotalLen];
      }
      for (int i = 0; i < daPrice.length; i++) {
        if (daPrice[i] == null) {
          vParaIndex.addElement(new Integer(i));
        }
      }

      int iParaLen = vParaIndex.size();
      if (iParaLen > 0)
      {
        String sPara = new SysInitDMO().getParaString(pk_corp, "PO06");
        if (sPara != null) {
          UFDouble[] daBasePrice = queryPricesFromPara(voPara, sPara, iPriorPrice);

          if ((daBasePrice != null) && (daBasePrice.length > 0)) {
            for (int i = 0; i < iParaLen; i++) {
              if (daBasePrice[i] == null) {
                continue;
              }
              int iIndex = ((Integer)vParaIndex.get(i)).intValue();
              daPrice[iIndex] = daBasePrice[i];
              if ((sPara.equals("参考成本")) || (sPara.equals("计划价"))) {
                voRet.setPriceNoTaxAt(iIndex, true);
              }
            }
          }
        }
      }

      timeDebug.addExecutePhase("根据系统参数到取采购价格");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    voRet.setDaQueryValue(daPrice);
    voRet.setPk_corp(pk_corp);

    voRet.setStoOrgIds(saStoOrgId);

    voRet.setSaCurrId(saCurrId);
    voRet.setVendMangId(sVendMangId);
    voRet.setSaInvMangId(saMangId);
    voRet.setSaInvBaseId(saBaseId);
    voRet.setDOrderDate(dOrderDate);
    voRet.setDaBRate(daBRate);
    voRet.setDaARate(daARate);
    timeDebug.showAllExecutePhase("订单BS取VRM及采购默认价格时间");

    return voRet;
  }

  public void rewriteBackIcNum(ParaBackIcToPoRewriteVO voPara)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.rewriteBackIcNum(ParaBackIcToPoRewriteVO)";

    if (voPara == null) {
      SCMEnv.out(sMethodName + "传入参数不正确！");
      return;
    }

    try
    {
      new PoReceivePlanImpl().rewriteBackIcNum(voPara);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    int iLen = voPara.getCBodyIdArray().length;

    UFDouble[] daRightOldNum = new UFDouble[iLen];
    UFDouble[] daRightNum = new UFDouble[iLen];
    for (int i = 0; i < iLen; i++) {
      daRightOldNum[i] = (voPara.getDOldNumArray()[i] == null ? new UFDouble(0.0D) : voPara.getDOldNumArray()[i].abs());
      daRightNum[i] = (voPara.getDNumArray()[i] == null ? null : voPara.getDNumArray()[i].abs());
    }

    try
    {
      new OrderDMO().rewriteValue_Body(voPara.getPk_corp(), "nbackstorenum", voPara.getCBodyIdArray(), daRightOldNum, daRightNum);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    new OrderPubImpl().validateRewriteNum(voPara.getPk_corp(), null, "po_order_b", "nbackstorenum", voPara.getCBodyIdArray(), voPara.isUserConfirm());

    adjustMppOnIcRwt(voPara.getCBodyIdArray(), voPara.getDOldNumArray(), voPara.getDNumArray());
  }

  public void rewriteBackRcNum(ParaBackRcToPoRewriteVO voPara)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.writeBackBackRcNum(ParaBackRcToPoRewriteVO)";

    if (voPara == null) {
      SCMEnv.out(sMethodName + "传入参数不正确！");
      return;
    }

    try
    {
      new PoReceivePlanImpl().rewriteBackRcNum(voPara);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    int iLen = voPara.getCBodyIdArray().length;

    UFDouble[] daRightOldNum = new UFDouble[iLen];
    UFDouble[] daRightNum = new UFDouble[iLen];
    for (int i = 0; i < iLen; i++) {
      daRightOldNum[i] = (voPara.getDOldNumArray()[i] == null ? new UFDouble(0.0D) : voPara.getDOldNumArray()[i].abs());
      daRightNum[i] = (voPara.getDNumArray()[i] == null ? new UFDouble(0.0D) : voPara.getDNumArray()[i].abs());
    }

    try
    {
      new OrderDMO().rewriteValue_Body(voPara.getPk_corp(), "nbackarrvnum", voPara.getCBodyIdArray(), daRightOldNum, daRightNum);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    new OrderPubImpl().validateRewriteNum(voPara.getPk_corp(), null, "po_order_b", "nbackarrvnum", voPara.getCBodyIdArray(), voPara.isUserConfirm());
  }

  public void rewriteIcNum(ParaIcToPoRewriteVO voPara)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.rewriteIcNum(ParaIcToPoRewriteVO)";
    try
    {
      new PoReceivePlanImpl().rewriteIcNum(voPara);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    String sPk_corp = voPara.getPk_corp();
    String sBizType = voPara.getCbiztype();
    String[] saBId = voPara.getCBodyIdArray();
    UFDouble[] daOldGenedNum = voPara.getDOldNumArray();
    UFDouble[] daCurNum = voPara.getDNumArray();
    try
    {
      new OrderDMO().rewriteValue_Body(sPk_corp, "naccumstorenum", saBId, daOldGenedNum, daCurNum);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    new OrderPubImpl().validateRewriteNum(sPk_corp, sBizType, "po_order_b", "naccumstorenum", saBId, voPara.isUserConfirm());

    new PoCloseImpl().closeRowsAtOnce(sPk_corp, "naccumstorenum", saBId, null);

    adjustMppOnIcRwt(saBId, daOldGenedNum, daCurNum);
  }

  private void adjustMppOnIcRwt(String[] saBId, UFDouble[] daOldGenedNum, UFDouble[] daCurNum)
    throws BusinessException
  {
    if ((saBId == null) || (daOldGenedNum == null) || (daCurNum == null) || (saBId.length == 0) || (saBId.length != daOldGenedNum.length) || (saBId.length != daCurNum.length))
    {
      return;
    }

    HashMap mapBidDiffNum = new HashMap();
    try {
      int iLen = saBId.length;
      for (int i = 0; i < iLen; i++) {
        if (mapBidDiffNum.containsKey(saBId[i])) {
          continue;
        }
        mapBidDiffNum.put(saBId[i], PuPubVO.getUFDouble_NullAsZero(daCurNum[i]).sub(PuPubVO.getUFDouble_NullAsZero(daOldGenedNum[i])));
      }

      OrderVO[] voaAdjusting = queryOrderArrayByConds(" po_order_b.corder_bid in " + new TempTableUtil().getSubSql(saBId), OrderPubVO.SQL_PO_BODY_CLOSE, null, null, null);

      if ((voaAdjusting == null) || (voaAdjusting.length == 0)) {
        SCMEnv.out("本次订单行均未关闭，直接返回");
        return;
      }

      ArrayList listCloseItems = null;
      ArrayList listOpenItems = null;
      ArrayList listCloseVos = new ArrayList();
      ArrayList listOpenVos = new ArrayList();
      int iHeadLen = voaAdjusting.length;
      int iBodyLen = 0;
      boolean bClose = false;
      boolean bOpen = false;
      UFDouble ufdDiffVal = null;
      String strBid = null;
      OrderVO voTmp = null;
      UFDouble ufdZero = new UFDouble(0.0D);
      for (int i = 0; i < iHeadLen; i++) {
        iBodyLen = voaAdjusting[i].getBodyLen();
        listOpenItems = new ArrayList();
        listCloseItems = new ArrayList();
        for (int k = 0; k < iBodyLen; k++) {
          strBid = voaAdjusting[i].getBodyVO()[k].getCorder_bid();
          if ((strBid == null) || (mapBidDiffNum.get(strBid) == null)) {
            continue;
          }
          ufdDiffVal = (UFDouble)mapBidDiffNum.get(strBid);

          if (ufdDiffVal.doubleValue() <= 0.0D) {
            listCloseItems.add(voaAdjusting[i].getBodyVO()[k]);
          }
          else
          {
            listOpenItems.add(voaAdjusting[i].getBodyVO()[k]);
          }

          if (voaAdjusting[i].isReturn()) {
            voaAdjusting[i].getHeadVO().setBreturn(UFBoolean.FALSE);
            voaAdjusting[i].getBodyVO()[k].setNordernum(voaAdjusting[i].getBodyVO()[k].getNordernum().multiply(-1.0D));
            voaAdjusting[i].getBodyVO()[k].setNtaxpricemny(voaAdjusting[i].getBodyVO()[k].getNtaxpricemny().multiply(-1.0D));
          }
          voaAdjusting[i].getBodyVO()[k].setNaccumstorenum(ufdZero.sub(ufdDiffVal.abs()).add(voaAdjusting[i].getBodyVO()[k].getNordernum()));
        }

        if (listCloseItems.size() == 0) {
          listOpenVos.add(voaAdjusting[i]);
        } else if (listCloseItems.size() == iBodyLen) {
          listCloseVos.add(voaAdjusting[i]);
        } else {
          voTmp = new OrderVO();
          voTmp.setParentVO(voaAdjusting[i].getHeadVO());
          voTmp.setChildrenVO((OrderItemVO[])(OrderItemVO[])listCloseItems.toArray(new OrderItemVO[listCloseItems.size()]));
          listCloseVos.add(voTmp);
          voTmp = new OrderVO();
          voTmp.setParentVO(voaAdjusting[i].getHeadVO());
          voTmp.setChildrenVO((OrderItemVO[])(OrderItemVO[])listOpenItems.toArray(new OrderItemVO[listOpenItems.size()]));
          listOpenVos.add(voTmp);
        }
      }

      ReWriteDMOPo wrtDmo = new ReWriteDMOPo();
      if (listCloseVos.size() > 0) {
        wrtDmo.rw_N_21_CLOSE_LINES((OrderVO[])(OrderVO[])listCloseVos.toArray(new OrderVO[listCloseVos.size()]));
      }
      if (listOpenVos.size() > 0)
        wrtDmo.rw_N_21_OPEN_LINES((OrderVO[])(OrderVO[])listOpenVos.toArray(new OrderVO[listOpenVos.size()]));
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw new BusinessException("adjustMppOnIcRwt()调用异常：" + e.getMessage());
    }
  }

  public void rewritePiNum(ParaPiToPoRewriteVO voPara)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.rewritePiNum(ParaPiToPoRewriteVO)";

    if (voPara == null) {
      return;
    }

    try
    {
      new OrderDMO().rewriteValue_Body(voPara.getPk_corp(), "naccuminvoicenum", voPara.getCBodyIdArray(), voPara.getDOldNumArray(), voPara.getDNumArray());

      String sPk_corp = voPara.getPk_corp();

      SysInitDMO dmoInit = new SysInitDMO();
      String sNumPresKind = null;
      String sPricePresKind = null;
      int iPriceDigit = 2;
      Hashtable tPara = dmoInit.queryBatchParaValues(sPk_corp, new String[] { "PO02", "PO04", "BD505" });
      if ((tPara != null) && (tPara.size() > 0)) {
        Object oTemp = tPara.get("PO02");
        if (oTemp != null) sNumPresKind = oTemp.toString();
        oTemp = tPara.get("PO04");
        if (oTemp != null) sPricePresKind = oTemp.toString();
        oTemp = tPara.get("BD505");
        if (oTemp != null) iPriceDigit = Integer.parseInt(oTemp.toString());

      }

      boolean bNumCheck = sNumPresKind.equals("不保存");
      boolean bPriceCheck = sPricePresKind.equals("不保存");
      boolean bNumHint = sNumPresKind.equals("提示");
      boolean bPriceHint = sPricePresKind.equals("提示");
      if ((bNumCheck) || (bPriceCheck) || (bNumHint) || (bPriceHint))
      {
        UFDouble dNumPresValue = null;
        UFDouble dPricePresValue = null;
        if ((bNumCheck) || (bNumHint)) {
          dNumPresValue = dmoInit.getParaDbl(sPk_corp, "PO03");
          dNumPresValue = PuPubVO.getUFDouble_NullAsZero(dNumPresValue);
        }
        if ((bPriceCheck) || (bPriceHint)) {
          dPricePresValue = dmoInit.getParaDbl(sPk_corp, "PO05");
          dPricePresValue = PuPubVO.getUFDouble_NullAsZero(dPricePresValue);
        }

        String[] saBId = voPara.getCBodyIdArray();
        UFDouble[] daLocalNetPrice = voPara.getDLocalNetPriceArray();

        String sPoBIdSubSql = new TempTableDMO().insertTempTable(saBId, "t_pu_po_017", "corder_bid");

        PubDMO dmoPuPub = new PubDMO();
        Hashtable htPoData = dmoPuPub.queryHtResultFromAnyTable("po_order_b", "corder_bid", new String[] { "nordernum", "nmoney", "naccuminvoicenum" }, "corder_bid IN " + sPoBIdSubSql);

        if (htPoData == null) {
          htPoData = new Hashtable();
        }
        Object[] oaData = null;
        UFDouble dOrderNum = null;
        UFDouble dMoney = null;
        UFDouble dAccumInvoiceNum = null;
        UFDouble dPrice = null;

        int iLen = saBId.length;
        for (int i = 0; i < iLen; i++) {
          Vector vecData = (Vector)htPoData.get(saBId[i]);
          if (vecData == null)
          {
            continue;
          }
          oaData = (Object[])(Object[])vecData.get(0);
          dOrderNum = PuPubVO.getUFDouble_NullAsZero(oaData[0]);
          dMoney = PuPubVO.getUFDouble_NullAsZero(oaData[1]);
          dAccumInvoiceNum = PuPubVO.getUFDouble_NullAsZero(oaData[2]);

          if (dOrderNum.multiply(dAccumInvoiceNum).compareTo(VariableConst.ZERO) < 0) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000259"));
          }

          if (bNumCheck) {
            if (((dAccumInvoiceNum.compareTo(VariableConst.ZERO) > 0) && (dAccumInvoiceNum.compareTo(dOrderNum.multiply(dNumPresValue.div(100.0D).add(1.0D))) > 0)) || ((dAccumInvoiceNum.compareTo(VariableConst.ZERO) < 0) && (dAccumInvoiceNum.compareTo(dOrderNum.multiply(dNumPresValue.div(100.0D).add(1.0D))) < 0)))
            {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000260"));
            }
          } else if ((bNumHint) && (!voPara.isUserConfirm()) && (
            ((dAccumInvoiceNum.compareTo(VariableConst.ZERO) > 0) && (dAccumInvoiceNum.compareTo(dOrderNum.multiply(dNumPresValue.div(100.0D).add(1.0D))) > 0)) || ((dAccumInvoiceNum.compareTo(VariableConst.ZERO) < 0) && (dAccumInvoiceNum.compareTo(dOrderNum.multiply(dNumPresValue.div(100.0D).add(1.0D))) < 0))))
          {
            throw new RwtPiToPoException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000266"));
          }

          if (bPriceCheck) {
            if (dOrderNum.compareTo(VariableConst.ZERO) == 0)
            {
              continue;
            }
            dPrice = dMoney.div(dOrderNum).setScale(iPriceDigit, 4);
            dPrice = dPrice.multiply(dPricePresValue.div(100.0D).add(1.0D)).setScale(iPriceDigit, 4);
            if (daLocalNetPrice[i] != null) daLocalNetPrice[i] = daLocalNetPrice[i].setScale(iPriceDigit, 4);
            if ((daLocalNetPrice[i] != null) && (daLocalNetPrice[i].compareTo(dPrice) > 0))
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000261"));
          }
          else {
            if ((!bPriceHint) || (voPara.isUserConfirm()) || 
              (dOrderNum.compareTo(VariableConst.ZERO) == 0))
            {
              continue;
            }
            dPrice = dMoney.div(dOrderNum).setScale(iPriceDigit, 4);
            dPrice = dPrice.multiply(dPricePresValue.div(100.0D).add(1.0D)).setScale(iPriceDigit, 4);
            if (daLocalNetPrice[i] != null) daLocalNetPrice[i] = daLocalNetPrice[i].setScale(iPriceDigit, 4);
            if ((daLocalNetPrice[i] != null) && (daLocalNetPrice[i].compareTo(dPrice) > 0)) {
              throw new RwtPiToPoException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000267"));
            }

          }

        }

      }

      new PoCloseImpl().closeRowsAtOnce(voPara.getPk_corp(), "naccuminvoicenum", voPara.getCBodyIdArray(), null);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  public void rewriteRcNum(ParaRcToPoRewriteVO voPara)
    throws BusinessException
  {
    String sPk_corp = voPara.getPk_corp();
    String[] saBId = voPara.getCBodyIdArray();
    try
    {
      new PoReceivePlanImpl().rewriteRcNum(voPara);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    try
    {
      new OrderDMO().rewriteValue_Body(sPk_corp, "naccumarrvnum", saBId, voPara.getDOldNumArray(), voPara.getDNumArray());
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    new OrderPubImpl().validateRewriteNum(sPk_corp, voPara.getCbiztype(), "po_order_b", "naccumarrvnum", saBId, voPara.isUserConfirm());

    new PoCloseImpl().closeRowsAtOnce(sPk_corp, "naccumarrvnum", saBId, null);
  }

  public void rewriteWasteNum(ParaRcWasteToPoRewriteVO voPara)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.rewriteWasteNum(ParaRewriteVO)";
    try
    {
      new PoReceivePlanImpl().rewriteWasteNum(voPara);
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    try
    {
      new OrderDMO().rewriteValue_Body(voPara.getPk_corp(), "naccumwastnum", voPara.getCBodyIdArray(), voPara.getDOldNumArray(), voPara.getDNumArray());
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  private void setNNotEligNumForOrderVOs(OrderVO[] voaOrder, String sSubBIdSql)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.setNNotEligNumForOrderVOs(OrderVO [], String)";

    if (voaOrder == null) {
      return;
    }
    if (PuPubVO.getString_TrimZeroLenAsNull(sSubBIdSql) == null) {
      SCMEnv.out(sMethodName + "传入参数不正确！");
      return;
    }

    String sSql = " po_arriveorder_b.corder_bid IN (" + sSubBIdSql + ") AND dr=0 GROUP BY po_arriveorder_b.corder_bid";

    Hashtable htRet = null;
    try {
      PubDMO dmoPuPub = new PubDMO();
      htRet = dmoPuPub.queryHtResultFromAnyTable("po_arriveorder_b", "corder_bid", new String[] { "SUM(nnotelignum)" }, sSql);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    if (htRet == null) {
      return;
    }

    int iLen = voaOrder.length;

    int iBodyLen = 0;
    OrderItemVO[] voaItem = null;
    Vector vecData = null;
    Object[] oaNum = null;
    UFDouble dNum = null;

    for (int i = 0; i < iLen; i++) {
      voaItem = voaOrder[i].getBodyVO();
      iBodyLen = voaItem.length;
      for (int j = 0; j < iBodyLen; j++) {
        vecData = (Vector)htRet.get(voaItem[j].getCorder_bid());
        if (vecData == null) {
          continue;
        }
        oaNum = (Object[])(Object[])vecData.get(0);
        dNum = PuPubVO.getUFDouble_ZeroAsNull(oaNum[0]);
        if (dNum != null)
          voaItem[j].setNnotelignum(dNum);
      }
    }
  }

  public void setVODefaultStorWhenToIC(OrderVO[] voaOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.setVODefaultStorWhenToIC(OrderVO [])";
    if (voaOrder == null) {
      return;
    }

    ArrayList listMangId = new ArrayList();
    ArrayList listCalBodyId = new ArrayList();

    int iLen = voaOrder.length;
    for (int i = 0; i < iLen; i++) {
      OrderItemVO[] voaItem = voaOrder[i].getBodyVO();
      for (int j = 0; j < voaItem.length; j++) {
        if (PuPubVO.getString_TrimZeroLenAsNull(voaItem[j].getCwarehouseid()) != null)
          continue;
        listMangId.add(voaItem[j].getCmangid());
        listCalBodyId.add(voaItem[j].getPk_arrvstoorg());
      }
    }

    iLen = listMangId.size();
    if (iLen == 0) {
      return;
    }

    HashMap hmapRet = null;
    try {
      hmapRet = new OrderDMO().queryDefaultStor((String[])(String[])listMangId.toArray(new String[iLen]), (String[])(String[])listCalBodyId.toArray(new String[iLen]));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    if (hmapRet == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000207"));
    }
    String sKey = null;

    iLen = voaOrder.length;
    for (int i = 0; i < iLen; i++) {
      OrderItemVO[] voaItem = voaOrder[i].getBodyVO();
      for (int j = 0; j < voaItem.length; j++) {
        if (PuPubVO.getString_TrimZeroLenAsNull(voaItem[j].getCwarehouseid()) != null)
          continue;
        sKey = voaItem[j].getCmangid() + voaItem[j].getPk_arrvstoorg();
        if (hmapRet.containsKey(sKey))
          voaItem[j].setCwarehouseid((String)hmapRet.get(sKey));
        else
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000207"));
      }
    }
  }

  public OrderVO[] splitVOForIC(OrderVO[] voaOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderDMO.splitVOForIC(OrderVO [])";

    setVODefaultStorWhenToIC(voaOrder);

    OrderVO[] resultVOs = null;
    String pk_corp = voaOrder[0].getHeadVO().getPk_corp();
    OrderItemVO[] voaItem = null;
    try
    {
      SysInitDMO sysDmo = new SysInitDMO();
      String splitMode = "仓库";

      SysInitVO initVO = sysDmo.queryByParaCode(pk_corp, "IC035");
      if (initVO != null) {
        splitMode = initVO.getValue().trim();
      }

      if (splitMode.equalsIgnoreCase("仓库")) {
        resultVOs = (OrderVO[])(OrderVO[])SplitBillVOs.getSplitVOs("nc.vo.po.OrderVO", "nc.vo.po.OrderHeaderVO", "nc.vo.po.OrderItemVO", voaOrder, null, new String[] { "cwarehouseid" });
      }
      else if (splitMode.equalsIgnoreCase("仓库+保管员")) {
        String cinvid = null;
        String cwarehouseid = null;

        IICToPU_StoreadminDMO storedmo = (IICToPU_StoreadminDMO)NCLocator.getInstance().lookup(IICToPU_StoreadminDMO.class.getName());

        for (int i = 0; i < voaOrder.length; i++) {
          voaItem = (OrderItemVO[])(OrderItemVO[])voaOrder[i].getChildrenVO();
          for (int j = 0; j < voaItem.length; j++) {
            cinvid = voaItem[j].getCmangid();
            cwarehouseid = voaItem[j].getCwarehouseid();
            if ((voaItem[j].getCwarehouseid() == null) || (voaItem[j].getCwarehouseid().trim() == ""))
              continue;
            voaItem[j].setcStoreAdmin(storedmo.getWHManager(pk_corp, null, cwarehouseid, cinvid));
          }

        }

        resultVOs = (OrderVO[])(OrderVO[])SplitBillVOs.getSplitVOs("nc.vo.po.OrderVO", "nc.vo.po.OrderHeaderVO", "nc.vo.po.OrderItemVO", voaOrder, null, new String[] { "cwarehouseid", "cstoreadmin" });
      }
      else if (splitMode.equalsIgnoreCase("仓库+存货分类末级"))
      {
        Vector vBaseids = new Vector();
        String cinvid = null;
        String[] cinvids = null;

        for (int i = 0; i < voaOrder.length; i++) {
          voaItem = (OrderItemVO[])(OrderItemVO[])voaOrder[i].getChildrenVO();
          for (int j = 0; j < voaItem.length; j++) {
            cinvid = voaItem[j].getCbaseid();
            if (!vBaseids.contains(cinvid))
              vBaseids.addElement(cinvid);
          }
        }
        if (vBaseids.size() > 0) {
          cinvids = new String[vBaseids.size()];
          vBaseids.copyInto(cinvids);

          Hashtable htInvAndClass = new PubDMO().fetchArrayValue("bd_invbasdoc", "pk_invcl", "pk_invbasdoc", cinvids);

          for (int i = 0; i < voaOrder.length; i++) {
            voaItem = (OrderItemVO[])(OrderItemVO[])voaOrder[i].getChildrenVO();
            for (int j = 0; j < voaItem.length; j++) {
              cinvid = voaItem[j].getCbaseid();
              if (vBaseids.contains(cinvid))
                voaItem[j].setcInvSort((String)htInvAndClass.get(cinvid));
            }
          }
        }
        resultVOs = (OrderVO[])(OrderVO[])SplitBillVOs.getSplitVOs("nc.vo.po.OrderVO", "nc.vo.po.OrderHeaderVO", "nc.vo.po.OrderItemVO", voaOrder, null, new String[] { "cwarehouseid", "cinvsort" });
      }
      else if (splitMode.equalsIgnoreCase("仓库+按单品")) {
        resultVOs = (OrderVO[])(OrderVO[])SplitBillVOs.getSplitVOs("nc.vo.po.OrderVO", "nc.vo.po.OrderHeaderVO", "nc.vo.po.OrderItemVO", voaOrder, null, new String[] { "cwarehouseid", "cmangid" });
      }

    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
    return resultVOs;
  }

  private String[] updateOrder(OrderVO voUpdated)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.updateOrder(OrderVO)";

    if ((voUpdated == null) || (voUpdated.getHeadVO() == null)) {
      return null;
    }

    OrderHeaderVO voHead = voUpdated.getHeadVO();

    OrderItemVO[] voaItem = voUpdated.getBodyVO();
    int iBodyLen = voaItem.length;
    Vector vecInsertBody = new Vector();
    Vector vecUpdateBody = new Vector();
    Vector vecDeleteBody = new Vector();
    for (int i = 0; i < iBodyLen; i++) {
      int iStatus = voaItem[i].getStatus();

      voaItem[i].setCorderid(voHead.getCorderid());
      if (iStatus == 2)
        vecInsertBody.addElement(voaItem[i]);
      else if (iStatus == 1)
        vecUpdateBody.addElement(voaItem[i]);
      else if (iStatus == 3) {
        vecDeleteBody.addElement(voaItem[i]);
      }
    }
    OrderItemVO[] voaInsertItem = null;
    if (vecInsertBody.size() > 0) {
      voaInsertItem = (OrderItemVO[])(OrderItemVO[])vecInsertBody.toArray(new OrderItemVO[vecInsertBody.size()]);
    }
    OrderItemVO[] voaUpdateItem = null;
    if (vecUpdateBody.size() > 0) {
      voaUpdateItem = (OrderItemVO[])(OrderItemVO[])vecUpdateBody.toArray(new OrderItemVO[vecUpdateBody.size()]);
    }
    OrderItemVO[] voaDeleteItem = null;
    if (vecDeleteBody.size() > 0) {
      voaDeleteItem = (OrderItemVO[])(OrderItemVO[])vecDeleteBody.toArray(new OrderItemVO[vecDeleteBody.size()]);
    }
    try
    {
      OrderDMO dmoOrder = new OrderDMO();

      new GetSysBillCode().setBillNoWhenModify(voUpdated, voUpdated.getOldVO(), "vordercode");

      dmoOrder.updateHead(voHead);

      if (voaInsertItem != null) {
        dmoOrder.insertBodyArray(voaInsertItem);
      }
      if (voaUpdateItem != null) {
        dmoOrder.updateBodyArray(voaUpdateItem);
      }
      if (voaDeleteItem != null)
        dmoOrder.deleteBodyArray(voaDeleteItem);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    String[] strKey = new String[1];

    strKey[0] = voHead.getCorderid();

    return strKey;
  }

  public void validateAssistUnit(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.validateAssistUnit(OrderVO)";

    if (voOrder == null) {
      return;
    }

    OrderVO voChecked = voOrder.getCheckVO();

    OrderItemVO[] voaCheckedItem = voChecked.getBodyVO();

    String sOrderId = voChecked.getHeadVO().getCorderid();

    Hashtable htAssistUnit = null;
    try
    {
      htAssistUnit = new PubDMO().queryHtResultFromAnyTable("bd_invbasdoc", "pk_invbasdoc", new String[] { "assistunit" }, "pk_invbasdoc IN (SELECT cbaseid FROM po_order_b WHERE corderid='" + sOrderId + "' AND dr=0 AND iisactive<>" + OrderItemVO.IISACTIVE_REVISION + ")");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    if (htAssistUnit == null) {
      return;
    }

    String sBaseId = null;
    int iBodyLen = voaCheckedItem.length;
    for (int i = 0; i < iBodyLen; i++) {
      sBaseId = voaCheckedItem[i].getCbaseid();

      if (!htAssistUnit.containsKey(sBaseId))
      {
        continue;
      }
      Vector vecAssistUnit = (Vector)htAssistUnit.get(sBaseId);
      String sAssistManaged = ((Object[])(Object[])vecAssistUnit.get(0))[0].toString();

      if ((!sAssistManaged.equalsIgnoreCase("Y")) || (
        (voaCheckedItem[i].getCassistunit() != null) && (!voaCheckedItem[i].getCassistunit().equals("")) && (voaCheckedItem[i].getNassistnum() != null)))
        continue;
      PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000208")));
    }
  }

  public void validateCalBodyAndInv(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.validateCalBodyAndInv(OrderVO)";

    if (voOrder == null) {
      return;
    }

    if (voOrder.getArrStoOrgIds() == null)
      return;
    try {
      ArrayList listIds = voOrder.getArrCorpArrStoOrgIds();
      if (listIds == null) {
        SCMEnv.out("表体未录入收货库存组织，直接返回!");
        return;
      }
      String[] saOrgId = (String[])(String[])listIds.get(0);
      String[] saCorpId = (String[])(String[])listIds.get(1);
      String[] saBaseId = (String[])(String[])listIds.get(2);
      ArrayList listComKey = new OrderDMO().isOrderInvInCalBody(voOrder.getHeadVO().getCorderid(), saCorpId, saOrgId, saBaseId);

      String strErrRowNos = "";
      String strErrRowComKey = null;
      if (listComKey == null) {
        listComKey = new ArrayList();
      }
      OrderItemVO[] items = voOrder.getBodyVO();
      int iLen = items.length;
      for (int i = 0; i < iLen; i++) {
        if (items[i].getStatus() == 3) {
          continue;
        }
        if (PuPubVO.getString_TrimZeroLenAsNull(items[i].getPk_arrvstoorg()) == null) {
          continue;
        }
        strErrRowComKey = items[i].getPk_arrvcorp();
        strErrRowComKey = strErrRowComKey + items[i].getPk_arrvstoorg();
        strErrRowComKey = strErrRowComKey + items[i].getCbaseid();
        if (!listComKey.contains(strErrRowComKey)) {
          if (strErrRowNos.length() > 0) {
            strErrRowNos = strErrRowNos + NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000");
          }
          strErrRowNos = strErrRowNos + items[i].getCrowno();
        }
      }
      if (strErrRowNos.length() > 0)
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000209", null, new String[] { strErrRowNos }));
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  public void validateContractInfo(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.validateContractInfo(OrderVO)";

    if (voOrder == null) {
      return;
    }

    OrderVO voChecked = voOrder.getCheckVO();

    OrderItemVO[] voaCheckedItem = voChecked.getBodyVO();

    String sOrderId = voChecked.getHeadVO().getCorderid();

    Hashtable htHaveCnt = null;
    try
    {
      htHaveCnt = new PubDMO().queryHtResultFromAnyTable("bd_invmandoc", "pk_invmandoc", new String[] { "isnoconallowed" }, "pk_invmandoc IN (SELECT cmangid FROM po_order_b WHERE corderid='" + sOrderId + "' AND dr=0 AND iisactive<>" + OrderItemVO.IISACTIVE_REVISION + ")");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    if (htHaveCnt == null) {
      return;
    }

    String sMangId = null;
    int iBodyLen = voaCheckedItem.length;
    for (int i = 0; i < iBodyLen; i++) {
      sMangId = voaCheckedItem[i].getCmangid();

      if (!htHaveCnt.containsKey(sMangId))
      {
        continue;
      }
      Vector vecHaveCnt = (Vector)htHaveCnt.get(sMangId);
      Object oHaveCnt = ((Object[])(Object[])vecHaveCnt.get(0))[0];
      if (oHaveCnt == null) {
        continue;
      }
      String sHaveCnt = oHaveCnt.toString();

      if ((!sHaveCnt.equalsIgnoreCase("N")) || (
        (voaCheckedItem[i].getccontractrowid() != null) && (!voaCheckedItem[i].getccontractrowid().trim().equals(""))))
        continue;
      PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000210")));
    }
  }

  public void validateMaxCtrlPrice(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.validateMaxCtrlPrice(OrderVO)";

    if (voOrder == null) {
      return;
    }

    OrderVO voChecked = voOrder.getCheckVO();

    OrderItemVO[] voaCheckedItem = voChecked.getBodyVO();

    String sPk_corp = voChecked.getHeadVO().getPk_corp();

    String sOrderId = voChecked.getHeadVO().getCorderid();

    Timer timeDubeg = new Timer();
    timeDubeg.start();

    String sPara = null;

    int iPriceDigit = 2;

    Hashtable htInvMaxPrice = null;

    PubDMO dmoPub = null;
    try
    {
      SysInitDMO dmoSysInit = new SysInitDMO();
      sPara = getParaMaxPrice(dmoSysInit, sPk_corp);
      if (sPara.equals("不控制")) {
        return;
      }
      if ((sPara.equals("提示")) && (!voChecked.isFirstTimePrice()))
      {
        return;
      }
      iPriceDigit = dmoSysInit.getParaInt(sPk_corp, "BD505").intValue();

      dmoPub = new PubDMO();

      Object[][] ba2IsFreeze = dmoPub.queryResultsFromAnyTable("po_order", new String[] { "forderstatus" }, "corderid='" + sOrderId + "'");

      if ((ba2IsFreeze == null) || (ba2IsFreeze[0] == null) || (ba2IsFreeze[0][0] == null)) {
        SCMEnv.out("程序或数据库数据异常：未查询到订单状态值，无法确定是否冻结！");
        return;
      }
      if (new Integer(ba2IsFreeze[0][0].toString()).intValue() == BillStatus.FREEZE.intValue())
      {
        return;
      }
      timeDubeg.addExecutePhase("已冻结则不再冻结：与最高限价协调");

      htInvMaxPrice = dmoPub.queryHtResultFromAnyTable("bd_invmandoc", "pk_invmandoc", new String[] { "maxprice" }, "pk_invmandoc IN (SELECT cmangid FROM po_order_b WHERE corderid='" + sOrderId + "' AND dr=0 AND iisactive<>" + OrderItemVO.IISACTIVE_REVISION + ")");

      timeDubeg.addExecutePhase("得到各存货的最高限价Hashtable");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    if (htInvMaxPrice == null) {
      return;
    }

    boolean bNeedFreeze = false;

    String sMangId = null;
    int iBodyLen = voaCheckedItem.length;
    for (int i = 0; i < iBodyLen; i++)
    {
      if (voaCheckedItem[i].isLargess()) {
        continue;
      }
      sMangId = voaCheckedItem[i].getCmangid();

      if (!htInvMaxPrice.containsKey(sMangId))
      {
        continue;
      }
      Vector vecMaxPrice = (Vector)htInvMaxPrice.get(sMangId);
      UFDouble dMaxPrice = PuPubVO.getUFDouble_NullAsZero(((Object[])(Object[])vecMaxPrice.get(0))[0]);

      dMaxPrice = dMaxPrice.setScale(iPriceDigit, 4);
      if (dMaxPrice.compareTo(VariableConst.ZERO) == 0)
      {
        continue;
      }

      UFDouble dItemPrice = VariableConst.ZERO;
      if ((voaCheckedItem[i].getNtaxpricemny() == null) || (voaCheckedItem[i].getNordernum() == null) || (voaCheckedItem[i].getNordernum().compareTo(VariableConst.ZERO) == 0)) {
        continue;
      }
      dItemPrice = voaCheckedItem[i].getNtaxpricemny().div(voaCheckedItem[i].getNordernum());
      dItemPrice = dItemPrice.setScale(iPriceDigit, 4);

      if (dItemPrice.compareTo(dMaxPrice) > 0) {
        if (sPara.equals("不保存")) {
          PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000211")));
        }
        else if (sPara.equals("提示"))
        {
          if (voChecked.isFirstTimePrice()) {
            PubDMO.throwBusinessException(sMethodName, new MaxPriceException("存在超出最高限价的行，是否继续？"));
          }
        }
        else if (sPara.equals("冻结")) {
          bNeedFreeze = true;
        }
      }
    }

    timeDubeg.addExecutePhase("检查");

    if (bNeedFreeze) {
      try
      {
        freezeBill(voOrder);
      }
      catch (Exception e) {
        PubDMO.throwBusinessException(sMethodName, e);
      }
    }

    timeDubeg.addExecutePhase("冻结");
    timeDubeg.showAllExecutePhase("订单BS最高限价检查");
  }

  public UFBoolean isOverMaxStock(OrderVO voOrder)
    throws BusinessException
  {
    voOrder.setUsedByFunc(true);

    UFBoolean ufbRet = validateMaxCtrlStock(voOrder);

    return ufbRet;
  }

  public UFBoolean validateMaxCtrlStock(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.validateMaxCtrlStock(OrderVO)";

    if (voOrder == null) {
      return UFBoolean.FALSE;
    }

    OrderVO voAtpChecked = voOrder.getMaxStockCheckVO();
    if (voAtpChecked == null) {
      return UFBoolean.FALSE;
    }

    String pk_corp = voAtpChecked.getHeadVO().getPk_corp();
    try
    {
      ICreateCorpQueryService corpDmo = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      if (!corpDmo.isEnabled(pk_corp, "IC"))
        return UFBoolean.FALSE;
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    String sOrderDate = voAtpChecked.getHeadVO().getDorderdate().toString();

    String sOrderId = voAtpChecked.getHeadVO().getCorderid();

    OrderItemVO[] voaCheckedItem = voAtpChecked.getBodyVO();

    String sPara = null;

    Hashtable htInvMaxStock = null;

    PubDMO dmoPub = null;

    Timer timeDubeg = new Timer();
    timeDubeg.start();

    HashMap mapMangId = new HashMap();
    try
    {
      sPara = getParaMaxStock(new SysInitDMO(), pk_corp);
      if (sPara.equals("不控制")) {
        if (!voOrder.isUsedByFunc()) {
          return UFBoolean.FALSE;
        }
      }
      else if ((sPara.equals("提示")) && (!voAtpChecked.isFirstTimeStock()))
      {
        if (!voOrder.isUsedByFunc()) {
          return UFBoolean.FALSE;
        }
      }

      dmoPub = new PubDMO();

      Object[][] ba2IsFreeze = dmoPub.queryResultsFromAnyTable("po_order", new String[] { "forderstatus" }, "corderid='" + sOrderId + "'");

      if ((ba2IsFreeze == null) || (ba2IsFreeze[0] == null) || (ba2IsFreeze[0][0] == null)) {
        SCMEnv.out("程序或数据库数据异常：未查询到订单状态值，无法确定是否冻结");
        if (!voOrder.isUsedByFunc()) {
          return UFBoolean.FALSE;
        }
      }
      else if (new Integer(ba2IsFreeze[0][0].toString()).intValue() == BillStatus.FREEZE.intValue())
      {
        if (!voOrder.isUsedByFunc()) {
          return UFBoolean.FALSE;
        }
      }
      timeDubeg.addExecutePhase("已冻结则不再冻结：与最高库存协调");

      String strWhereSql = " po_order_b.cbaseid = bd_invbasdoc.pk_invbasdoc and po_order_b.corderid='" + sOrderId + "' and " + OrderPubVO.SQL_PO_BODY + " " + "and bd_invbasdoc.laborflag='N' and bd_invbasdoc.discountflag='N' ";

      Object[][] oa2Ret = dmoPub.queryResultsFromAnyTable("po_order_b,bd_invbasdoc", new String[] { "po_order_b.cmangid", "po_order_b.pk_arrvstoorg", "pk_arrvcorp" }, strWhereSql);
      if ((oa2Ret == null) || (oa2Ret.length == 0)) {
        SCMEnv.out("所有表体行均未填写收货库存组织，最高库存检查未进行，直接返回");
        return UFBoolean.FALSE;
      }
      ArrayList listPurMangId = new ArrayList();
      ArrayList listStorId = new ArrayList();
      ArrayList listStorCorpId = new ArrayList();
      for (int i = 0; i < oa2Ret.length; i++) {
        if ((oa2Ret[i] == null) || (oa2Ret[i].length < 3) || (oa2Ret[i][0] == null) || (oa2Ret[i][1] == null) || (oa2Ret[i][2] == null))
        {
          continue;
        }

        listPurMangId.add((String)oa2Ret[i][0]);
        listStorId.add((String)oa2Ret[i][1]);
        listStorCorpId.add((String)oa2Ret[i][2]);
      }

      if (listStorId.size() == 0) {
        SCMEnv.out("所有表体行均未填写收货库存组织，最高库存检查未进行，直接返回");
        return UFBoolean.FALSE;
      }
      int iLen = listPurMangId.size();
      String[] saMangIdPur = (String[])(String[])listPurMangId.toArray(new String[iLen]);
      String[] saStorId = (String[])(String[])listStorId.toArray(new String[iLen]);
      String[] saStorCorpId = (String[])(String[])listStorCorpId.toArray(new String[iLen]);
      ChgDocPkVO[] voaDocChg = new ChgDocPkVO[iLen];
      for (int i = 0; i < iLen; i++) {
        voaDocChg[i] = new ChgDocPkVO();
        voaDocChg[i].setDstCorpId(saStorCorpId[i]);
        voaDocChg[i].setSrcCorpId(pk_corp);
        voaDocChg[i].setSrcManId(saMangIdPur[i]);
      }
      voaDocChg = ChgDataUtil.chgPkInvByCorp(voaDocChg);

      String[] saMangIdStor = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        saMangIdStor[i] = voaDocChg[i].getDstManId();
        mapMangId.put(voaDocChg[i].getSrcManId(), voaDocChg[i].getDstManId());
      }

      String strTmpTblName = new TempTableUtil().createTempTable(saMangIdStor, saStorId);

      htInvMaxStock = dmoPub.queryHtResultFromAnyTable2("bd_produce," + strTmpTblName, "pk_invmandoc", "pk_calbody", new String[] { "maxstornum" }, "pk_invmandoc = pk_pu1 and pk_calbody = pk_pu2");

      timeDubeg.addExecutePhase("得到各存货的最高库存Hashtable：滤掉劳务及折扣属性的存货");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    if (htInvMaxStock == null) {
      return UFBoolean.FALSE;
    }

    ArrayList aryPara = new ArrayList();
    int iBodyLen = voaCheckedItem.length;

    String sPlanDate = null;
    for (int i = 0; i < iBodyLen; i++)
    {
      if (htInvMaxStock.containsKey((String)mapMangId.get(voaCheckedItem[i].getCmangid()) + voaCheckedItem[i].getPk_arrvstoorg())) {
        if ((voaCheckedItem[i].getDplanarrvdate() == null) || (voaCheckedItem[i].getDplanarrvdate().toString().trim().equals("")))
          sPlanDate = sOrderDate;
        else {
          sPlanDate = voaCheckedItem[i].getDplanarrvdate().toString();
        }
        ArrayList aryCur = new ArrayList();
        aryCur.add(voaCheckedItem[i].getPk_arrvcorp());
        aryCur.add(voaCheckedItem[i].getPk_arrvstoorg());
        aryCur.add(sPlanDate);
        aryCur.add((String)mapMangId.get(voaCheckedItem[i].getCmangid()));

        aryPara.add(aryCur);
      }
    }
    timeDubeg.addExecutePhase("组织数据进行可用量查询");

    if (aryPara == null) {
      return UFBoolean.FALSE;
    }

    Hashtable htAtp = getAtpHash(aryPara);

    if (htAtp == null) {
      return UFBoolean.FALSE;
    }
    timeDubeg.addExecutePhase("可用量查询");

    boolean bNeedFreeze = false;

    String sMangId = null;
    String sStorId = null;
    String sAtpKey = null;
    ArrayList listErrRowNo = new ArrayList();
    for (int i = 0; i < iBodyLen; i++)
    {
      if ((voaCheckedItem[i].getDplanarrvdate() == null) || (voaCheckedItem[i].getDplanarrvdate().toString().trim().equals("")))
      {
        sPlanDate = sOrderDate;
      }
      else sPlanDate = voaCheckedItem[i].getDplanarrvdate().toString();

      sMangId = (String)mapMangId.get(voaCheckedItem[i].getCmangid());

      sStorId = voaCheckedItem[i].getPk_arrvstoorg();

      if (!htInvMaxStock.containsKey(sMangId + sStorId))
      {
        continue;
      }

      sAtpKey = voaCheckedItem[i].getPk_arrvcorp() + sStorId + sPlanDate + sMangId;
      if (!htAtp.containsKey(sAtpKey))
      {
        continue;
      }
      Vector vecMaxStock = (Vector)htInvMaxStock.get(sMangId + sStorId);
      UFDouble dMaxStock = PuPubVO.getUFDouble_NullAsZero(((Object[])(Object[])vecMaxStock.get(0))[0]);
      if (dMaxStock.compareTo(VariableConst.ZERO) == 0) {
        continue;
      }
      UFDouble dAtpNum = PuPubVO.getUFDouble_NullAsZero(htAtp.get(sAtpKey));

      if (dAtpNum.compareTo(dMaxStock) <= 0)
        continue;
      if (voOrder.isUsedByFunc()) {
        return UFBoolean.TRUE;
      }
      if ((sPara.equals("不保存")) || (sPara.equals("提示")))
        listErrRowNo.add(voaCheckedItem[i].getCrowno());
      else if (sPara.equals("冻结")) {
        bNeedFreeze = true;
      }
    }

    if (listErrRowNo.size() > 0) {
      String strRowNo = (String)listErrRowNo.get(0);
      for (int j = 1; j < listErrRowNo.size(); j++) {
        strRowNo = strRowNo + NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000");
        strRowNo = strRowNo + (String)listErrRowNo.get(j);
      }
      if (sPara.equals("不保存")) {
        PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000212", null, new String[] { strRowNo })));
      }
      else if (sPara.equals("提示"))
      {
        if (voAtpChecked.isFirstTimeStock()) {
          PubDMO.throwBusinessException(sMethodName, new MaxStockException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000273", null, new String[] { strRowNo })));
        }
      }
    }
    timeDubeg.addExecutePhase("检查");

    if (bNeedFreeze) {
      try
      {
        freezeBill(voOrder);
      }
      catch (Exception e) {
        PubDMO.throwBusinessException(sMethodName, e);
      }
    }

    timeDubeg.addExecutePhase("冻结");
    timeDubeg.showAllExecutePhase("订单BS最高库存检查");

    return UFBoolean.FALSE;
  }

  private Hashtable getAtpHash(ArrayList aryPara)
    throws BusinessException
  {
    Hashtable htAtp = null;

    int iSize = aryPara.size();
    HashMap mapComKeyInvMangIds = new HashMap();
    ArrayList listInvMangId = new ArrayList();
    String strComKey = null;
    ArrayList aryValues = null;
    for (int i = 0; i < iSize; i++) {
      aryValues = (ArrayList)aryPara.get(i);
      if ((PuPubVO.getString_TrimZeroLenAsNull(aryValues.get(0)) == null) || (PuPubVO.getString_TrimZeroLenAsNull(aryValues.get(1)) == null) || (PuPubVO.getString_TrimZeroLenAsNull(aryValues.get(2)) == null))
      {
        continue;
      }

      strComKey = (String)aryValues.get(0) + (String)aryValues.get(1) + (String)aryValues.get(2);
      if (mapComKeyInvMangIds.containsKey(strComKey))
        listInvMangId = (ArrayList)mapComKeyInvMangIds.get(strComKey);
      else {
        listInvMangId = new ArrayList();
      }
      listInvMangId.add(aryValues.get(3));
      mapComKeyInvMangIds.put(strComKey, listInvMangId);
    }
    try {
      IICPub_InvATPDMO dmo = (IICPub_InvATPDMO)NCLocator.getInstance().lookup(IICPub_InvATPDMO.class.getName());
      Iterator it = mapComKeyInvMangIds.keySet().iterator();
      UFDouble[] uaRet = null;
      htAtp = new Hashtable();
      String strAtpKey = null;
      String[] saMangId = null;
      while (it.hasNext()) {
        strComKey = (String)it.next();
        listInvMangId = (ArrayList)mapComKeyInvMangIds.get(strComKey);
        saMangId = (String[])(String[])listInvMangId.toArray(new String[listInvMangId.size()]);
        uaRet = dmo.getATPNum(strComKey.substring(0, 4), strComKey.substring(4, 24), saMangId, strComKey.substring(24, 34));

        if (uaRet != null)
          for (int i = 0; i < saMangId.length; i++)
            htAtp.put(strComKey + saMangId[i], uaRet[i]);
      }
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(e);
    }

    return htAtp;
  }

  public void validateReviseNum(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.validateReviseNum(OrderVO)";
    String sMessage = NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000213");
    String sMessageHint = NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000263");

    if ((voOrder == null) || (voOrder.getHeadVO() == null)) {
      return;
    }
    String sPk_corp = voOrder.getHeadVO().getPk_corp();

    SysInitDMO dmoInit = new SysInitDMO();
    String sPoPresKind = dmoInit.getParaString(sPk_corp, "PO02");
    UFDouble dPoPresValue = null;

    boolean bNoUserConfirm = voOrder.isFirstTimeSP();

    if ((sPoPresKind.equals("不保存")) || (sPoPresKind.equals("提示")))
    {
      dPoPresValue = dmoInit.getParaDbl(sPk_corp, "PO03");
      dPoPresValue = PuPubVO.getUFDouble_NullAsZero(dPoPresValue);
    } else {
      SCMEnv.out("采购参数：PO02为“不控制”，直接返回");
      return;
    }

    OrderItemVO[] voaItem = voOrder.getBodyVO();
    int iBodyLen = voaItem.length;

    UFDouble dOrderNum = null;

    UFDouble dOldOrderNum = null;

    UFDouble dBackNum = null;

    OrderItemVO voOldItem = null;
    for (int i = 0; i < iBodyLen; i++) {
      if (voaItem[i].getStatus() != 1)
      {
        continue;
      }
      dOrderNum = PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNordernum());

      voOldItem = voOrder.getOldVO().getBodyVOByBId(voaItem[i].getCorder_bid());

      dOldOrderNum = voOldItem == null ? VariableConst.ZERO : PuPubVO.getUFDouble_NullAsZero(voOldItem.getNordernum());

      if (dOrderNum.multiply(dOldOrderNum).compareTo(VariableConst.ZERO) < 0) {
        PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000214")));
      }

      UFDouble dCheckNum = dOrderNum.multiply(PuPubVO.getUFDouble_NullAsZero(dPoPresValue).div(100.0D).add(1.0D));

      dBackNum = PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNbackarrvnum());
      dBackNum = dBackNum.add(PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNbackstorenum()));
      if (dOldOrderNum.compareTo(VariableConst.ZERO) == 0) {
        if ((PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccumarrvnum()).compareTo(VariableConst.ZERO) != 0) || (PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccumstorenum()).compareTo(VariableConst.ZERO) != 0) || (PuPubVO.getUFDouble_NullAsZero(dBackNum).compareTo(VariableConst.ZERO) != 0))
        {
          if (sPoPresKind.equals("不保存"))
            PubDMO.throwBusinessException(sMethodName, new BusinessException(sMessage));
          else if ((sPoPresKind.equals("提示")) && (bNoUserConfirm))
            PubDMO.throwBusinessException(sMethodName, new PoReviseException(sMessageHint));
        }
        else if (PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccuminvoicenum()).compareTo(VariableConst.ZERO) != 0)
        {
          if (sPoPresKind.equals("不保存"))
            PubDMO.throwBusinessException(sMethodName, new BusinessException(sMessage));
          else if ((sPoPresKind.equals("提示")) && (bNoUserConfirm))
            PubDMO.throwBusinessException(sMethodName, new PoReviseException(sMessageHint));
        } else {
          if (PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccumdayplnum()).compareTo(VariableConst.ZERO) == 0)
            continue;
          if (sPoPresKind.equals("不保存"))
            PubDMO.throwBusinessException(sMethodName, new BusinessException(sMessage));
          else if ((sPoPresKind.equals("提示")) && (bNoUserConfirm))
            PubDMO.throwBusinessException(sMethodName, new PoReviseException(sMessageHint));
        }
      }
      else if (dOldOrderNum.compareTo(VariableConst.ZERO) > 0)
      {
        if ((dCheckNum.compareTo(PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccumarrvnum())) < 0) || (dCheckNum.compareTo(PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccumstorenum())) < 0) || (dCheckNum.compareTo(PuPubVO.getUFDouble_NullAsZero(dBackNum)) < 0))
        {
          if (sPoPresKind.equals("不保存"))
            PubDMO.throwBusinessException(sMethodName, new BusinessException(sMessage));
          else if ((sPoPresKind.equals("提示")) && (bNoUserConfirm))
            PubDMO.throwBusinessException(sMethodName, new PoReviseException(sMessageHint));
        }
        else if (PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccuminvoicenum()).compareTo(dCheckNum) > 0)
        {
          if (sPoPresKind.equals("不保存"))
            PubDMO.throwBusinessException(sMethodName, new BusinessException(sMessage));
          else if ((sPoPresKind.equals("提示")) && (bNoUserConfirm))
            PubDMO.throwBusinessException(sMethodName, new PoReviseException(sMessageHint));
        } else {
          if (dCheckNum.compareTo(PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccumdayplnum())) >= 0)
            continue;
          if (sPoPresKind.equals("不保存"))
            PubDMO.throwBusinessException(sMethodName, new BusinessException(sMessage));
          else if ((sPoPresKind.equals("提示")) && (bNoUserConfirm)) {
            PubDMO.throwBusinessException(sMethodName, new PoReviseException(sMessageHint));
          }
        }

      }
      else if (dCheckNum.multiply(-1.0D).compareTo(dBackNum) < 0) {
        if (sPoPresKind.equals("不保存"))
          PubDMO.throwBusinessException(sMethodName, new BusinessException(sMessage));
        else if ((sPoPresKind.equals("提示")) && (bNoUserConfirm))
          PubDMO.throwBusinessException(sMethodName, new PoReviseException(sMessageHint));
      }
      else if (PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccuminvoicenum()).compareTo(dCheckNum) < 0)
      {
        if (sPoPresKind.equals("不保存"))
          PubDMO.throwBusinessException(sMethodName, new BusinessException(sMessage));
        else if ((sPoPresKind.equals("提示")) && (bNoUserConfirm))
          PubDMO.throwBusinessException(sMethodName, new PoReviseException(sMessageHint));
      } else {
        if (dCheckNum.multiply(-1.0D).compareTo(PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNaccumdayplnum())) >= 0)
          continue;
        if (sPoPresKind.equals("不保存"))
          PubDMO.throwBusinessException(sMethodName, new BusinessException(sMessage));
        else if ((sPoPresKind.equals("提示")) && (bNoUserConfirm))
          PubDMO.throwBusinessException(sMethodName, new PoReviseException(sMessageHint));
      }
    }
  }

  public void validateStockPresent(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.validateStockPresent(OrderVO)";

    if (voOrder == null) {
      return;
    }

    if (!voOrder.isFirstTimeSP()) {
      return;
    }
    OrderVO voChecked = voOrder.getCheckVO();

    OrderItemVO[] voaCheckedItem = voChecked.getBodyVO();

    int iBodyLen = voaCheckedItem.length;

    String[] sCbaseids = new String[iBodyLen];
    for (int i = 0; i < iBodyLen; i++) {
      sCbaseids[i] = voaCheckedItem[i].getCbaseid();
    }

    ArrayList arrVOs = new ArrayList();
    Object[] oFlag = null;
    UFDouble dOrderNum = null;
    try {
      PubDMO puPubDmo = new PubDMO();

      HashMap hCbaseIds = puPubDmo.queryArrayValues("bd_invbasdoc", "pk_invbasdoc", new String[] { "discountflag", "laborflag" }, sCbaseids, "bd_invbasdoc.dr=0");

      for (int i = 0; i < iBodyLen; i++) {
        dOrderNum = voaCheckedItem[i].getNordernum();
        if (dOrderNum.doubleValue() >= 0.0D)
          continue;
        if (hCbaseIds != null)
          oFlag = (Object[])(Object[])hCbaseIds.get(sCbaseids[i]);
        if (((oFlag != null) && (oFlag[0].toString().equals("Y"))) || (oFlag[1].toString().equals("Y")))
          continue;
        arrVOs.add(voaCheckedItem[i]);
      }

      OrderItemVO[] items = null;
      if (arrVOs.size() > 0) {
        items = new OrderItemVO[arrVOs.size()];
        arrVOs.toArray(items);
      } else {
        return;
      }int iLen = items.length;
      Hashtable htStoOrg = new Hashtable();
      Hashtable htStoCorp = new Hashtable();
      ArrayList listMangId = null;
      HashMap hCombine = new HashMap();
      HashMap hBaseId = new HashMap();
      String sStorid = null;
      String sMangid = null;
      String sBaseid = null;
      UFDouble temp = null;
      String sKey = null;

      for (int i = 0; i < iLen; i++) {
        sStorid = items[i].getPk_arrvstoorg();
        if (PuPubVO.getString_TrimZeroLenAsNull(sStorid) == null) {
          continue;
        }
        if (PuPubVO.getString_TrimZeroLenAsNull(items[i].getPk_arrvcorp()) == null) {
          continue;
        }
        sMangid = items[i].getCmangid();
        sBaseid = items[i].getCbaseid();
        sKey = sStorid + sMangid;
        dOrderNum = items[i].getNordernum();
        if (hCombine.containsKey(sKey)) {
          temp = (UFDouble)hCombine.get(sKey);
          temp = temp.add(dOrderNum);
          hCombine.put(sKey, temp);
        } else {
          hCombine.put(sKey, dOrderNum);
          hBaseId.put(sMangid, sBaseid);
        }

        listMangId = (ArrayList)htStoOrg.get(sStorid);
        if (listMangId == null) {
          listMangId = new ArrayList();
        }
        if (!listMangId.contains(sMangid)) {
          listMangId.add(sMangid);
          htStoOrg.put(sStorid, listMangId);
          htStoCorp.put(sStorid, items[i].getPk_arrvcorp());
        }
      }

      ArrayList arrBaseId = new ArrayList();
      IICPub_InvOnHandDMO invOnHandDMO = (IICPub_InvOnHandDMO)NCLocator.getInstance().lookup(IICPub_InvOnHandDMO.class.getName());
      UFDouble[] dStockPresentNum = null;
      String sStoOrgId = null;
      Enumeration keys = htStoOrg.keys();
      String[] saMangidPur = null;
      String[] saMangidArr = null;

      String sCorpId = null;

      ChgDocPkVO[] vosChgDoc = null;
      while (keys.hasMoreElements()) {
        sKey = (String)keys.nextElement();
        listMangId = (ArrayList)htStoOrg.get(sKey);
        sCorpId = (String)htStoCorp.get(sKey);
        vosChgDoc = new ChgDocPkVO[listMangId.size()];
        for (int j = 0; j < listMangId.size(); j++) {
          vosChgDoc[j] = new ChgDocPkVO();
          vosChgDoc[j].setDstCorpId(sCorpId);
          vosChgDoc[j].setSrcCorpId(voChecked.getHeadVO().getPk_corp());
          vosChgDoc[j].setSrcManId((String)listMangId.get(j));
        }
        vosChgDoc = ChgDataUtil.chgPkInvByCorp(vosChgDoc);
        saMangidArr = new String[listMangId.size()];
        for (int j = 0; j < listMangId.size(); j++) {
          saMangidArr[j] = vosChgDoc[j].getDstManId();
        }
        sStoOrgId = sKey.substring(0, 20);
        iLen = saMangidArr.length;
        dStockPresentNum = invOnHandDMO.getOnhandNums(sCorpId, sStoOrgId, saMangidArr);
        if (dStockPresentNum == null) {
          dStockPresentNum = new UFDouble[iLen];
        }
        saMangidPur = (String[])(String[])listMangId.toArray(new String[listMangId.size()]);
        for (int i = 0; i < iLen; i++) {
          temp = (UFDouble)hCombine.get(sStoOrgId + saMangidPur[i]);
          if (temp.doubleValue() + (dStockPresentNum[i] == null ? 0.0D : dStockPresentNum[i].doubleValue()) < 0.0D)
            arrBaseId.add((String)hBaseId.get(saMangidPur[i]));
        }
      }
      String[] sBaseId = null;
      if (arrBaseId.size() > 0) {
        sBaseId = new String[arrBaseId.size()];
        arrBaseId.toArray(sBaseId);
      } else {
        return;
      }
      HashMap hInvName = puPubDmo.queryArrayValues("bd_invbasdoc", "pk_invbasdoc", new String[] { "invname" }, sBaseId, "bd_invbasdoc.dr=0");

      String[] saKey = new String[sBaseId.length];
      hInvName.keySet().toArray(saKey);
      String strInvNames = "";
      int jLen = sKey == null ? 0 : saKey.length;
      for (int i = 0; i < jLen; i++) {
        Object[] o = (Object[])(Object[])hInvName.get(saKey[i]);
        if ((o != null) && (o[0] != null)) {
          strInvNames = strInvNames + o[0];
        }
      }
      String strErrMsg = NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000215", null, new String[] { strInvNames });
      if (strInvNames.length() > 0)
        throw new StockPresentException(strErrMsg);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }
  }

  public void verifyOrderCouldBeUnAudited(OrderVO voOrder)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.verifyOrderCouldBeUnAudited(OrderVO)";

    if (voOrder == null) {
      PubDMO.throwBusinessException(sMethodName, new Exception(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000216")));
    }

    Object[][] oa2Ret = (Object[][])null;
    try {
      oa2Ret = new PubDMO().queryResultsFromAnyTable("po_order_bb", new String[] { "corderid" }, "corderid='" + voOrder.getHeadVO().getCorderid() + "' AND dr=0");
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    if (oa2Ret != null)
      PubDMO.throwBusinessException(sMethodName, new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000217")));
  }

  public void rewriteDayplNum(ParaDayplToPoRewriteVO voPara)
    throws BusinessException
  {
    if (voPara == null) {
      SCMEnv.out("回写订单已生成日计划数量失败:发运管理传入参数为空，直接返回");
      return;
    }
    String[] saHid = voPara.getCHeadIdArray();
    String[] saBid = voPara.getCBodyIdArray();
    String[] saBB1id = voPara.getCOrderPlanIdArray();
    String[] saCorpid = voPara.getCOrderCorpIdArray();
    UFDouble[] uaNum = voPara.getDNumArray();
    UFDouble[] uaNumOld = voPara.getDOldNumArray();

    if ((saHid == null) || (saHid.length == 0) || (saBid == null) || (saBid.length == 0) || (saCorpid == null) || (saCorpid.length == 0) || (uaNum == null) || (uaNum.length == 0) || (uaNumOld == null) || (uaNumOld.length == 0))
    {
      SCMEnv.out("回写订单已生成日计划数量失败:发运管理传入参数检查未通过--数组参数存在空或零长{参数包括：订单ID 数组、行ID数组、订单所属公司ID数组、新日计划数量数组、旧日计划数量数组}");
      return;
    }
    if ((saHid.length != saBid.length) || (saHid.length != saBB1id.length) || (saHid.length != uaNum.length) || (saHid.length != uaNumOld.length))
    {
      SCMEnv.out("回写订单已生成日计划数量失败:发运管理传入参数检查未通过--数组参数存在长度不一致{参数包括：订单ID 数组、行ID数组、订单所属公司ID数组、新日计划数量数组、旧日计划数量数组}");
      return;
    }
    OrderDMO dmo = null;
    try {
      dmo = new OrderDMO();
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    Hashtable hash = new Hashtable();
    String strCorpId = null;
    Enumeration enumeration = null;

    if ((saBB1id != null) && (saBB1id.length > 0) && (saBB1id[0] != null)) {
      dmo.rewriteValueAnyTable("po_order_bb1", "naccumdayplnum", uaNum, uaNumOld, "corder_bb1id", saBB1id);

      hash = PubDMO.getGroupForFirst(saCorpid, saBB1id);
      enumeration = hash.keys();
      try {
        while (enumeration.hasMoreElements()) {
          strCorpId = (String)enumeration.nextElement();
          dmo.checkNumOut(strCorpId, "po_order_bb1", "nordernum", "naccumdayplnum", "corder_bb1id", saBB1id);
        }
      } catch (Exception e) {
        if ((e instanceof BusinessException))
          PubDMO.throwBusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000218")));
        else {
          PubDMO.throwBusinessException(e);
        }
      }
    }

    dmo.rewriteValueAnyTable("po_order_b", "naccumdayplnum", uaNum, uaNumOld, "corder_bid", saBid);

    hash = PubDMO.getGroupForFirst(saCorpid, saBid);
    enumeration = hash.keys();
    try {
      while (enumeration.hasMoreElements()) {
        strCorpId = (String)enumeration.nextElement();
        dmo.checkNumOut(strCorpId, "po_order_b", "nordernum", "naccumdayplnum", "corder_bid", saBid);
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        PubDMO.throwBusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000219")));
      else
        PubDMO.throwBusinessException(e);
    }
  }

  public void rewriteDelivNum(ParaDelivToPoRewriteVO voPara)
    throws BusinessException
  {
    if (voPara == null) {
      SCMEnv.out("回写订单已发运数量失败:发运管理传入参数为空，直接返回!");
      return;
    }
    String[] saHid = voPara.getCHeadIdArray();
    String[] saBid = voPara.getCBodyIdArray();
    String[] saBB1id = voPara.getCOrderPlanIdArray();
    String[] saCorpid = voPara.getCOrderCorpIdArray();
    UFDouble[] uaNum = voPara.getDNumArray();
    UFDouble[] uaNumOld = voPara.getDOldNumArray();

    if ((saHid == null) || (saHid.length == 0) || (saBid == null) || (saBid.length == 0) || (saCorpid == null) || (saCorpid.length == 0) || (uaNum == null) || (uaNum.length == 0) || (uaNumOld == null) || (uaNumOld.length == 0))
    {
      SCMEnv.out("回写订单已发运数量失败:发运管理传入参数检查未通过--数组参数存在空或零长{参数包括：订单ID 数组、行ID数组、订单所属公司ID数组、新日计划数量数组、旧日计划数量数组}");
      return;
    }
    if ((saHid.length != saBid.length) || (saHid.length != saBB1id.length) || (saHid.length != uaNum.length) || (saHid.length != uaNumOld.length))
    {
      SCMEnv.out("回写订单已发运数量失败:发运管理传入参数检查未通过--数组参数存在长度不一致{参数包括：订单ID 数组、行ID数组、订单所属公司ID数组、新日计划数量数组、旧日计划数量数组}");
      return;
    }
    OrderDMO dmo = null;
    try {
      dmo = new OrderDMO();
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    Hashtable hash = new Hashtable();
    String strCorpId = null;
    Enumeration enumeration = null;

    if ((saBB1id != null) && (saBB1id.length > 0) && (saBB1id[0] != null)) {
      dmo.rewriteValueAnyTable("po_order_bb1", "naccumdevnum", uaNum, uaNumOld, "corder_bb1id", saBB1id);

      hash = PubDMO.getGroupForFirst(saCorpid, saBB1id);
      enumeration = hash.keys();
      try {
        while (enumeration.hasMoreElements()) {
          strCorpId = (String)enumeration.nextElement();
          dmo.checkNumOut(strCorpId, "po_order_bb1", "nordernum", "naccumdevnum", "corder_bb1id", saBB1id);
        }
      } catch (Exception e) {
        if ((e instanceof BusinessException))
          PubDMO.throwBusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000220")));
        else {
          PubDMO.throwBusinessException(e);
        }
      }
    }

    dmo.rewriteValueAnyTable("po_order_b", "naccumdevnum", uaNum, uaNumOld, "corder_bid", saBid);

    hash = PubDMO.getGroupForFirst(saCorpid, saBB1id);
    enumeration = hash.keys();
    try {
      while (enumeration.hasMoreElements()) {
        strCorpId = (String)enumeration.nextElement();
        dmo.checkNumOut(strCorpId, "po_order_b", "nordernum", "naccumdevnum", "corder_bid", saBid);
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        PubDMO.throwBusinessException(new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000221")));
      else
        PubDMO.throwBusinessException(e);
    }
  }

  private OrderVO[] queryOrderArrayForAfter_BB(String sPk_corp, int iAfterBillStatus, int iOnWayStatus, String sFromWhereB, String sFromWhereBB1)
    throws BusinessException
  {
    String sMethodName = "nc.bs.po.OrderImpl.queryOrderArrayForAfter_BB(String, String, String)";

    String sHSql = "(";
    if (sFromWhereB != null) {
      sHSql = sHSql + " SELECT po_order_b.corderid " + sFromWhereB + " UNION ALL ";
    }

    sHSql = sHSql + " SELECT po_order_bb1.corderid " + sFromWhereBB1 + ")";

    String sBSql = "(";
    if (sFromWhereB != null) {
      sBSql = sBSql + " SELECT po_order_b.corder_bid " + sFromWhereB + " UNION ALL ";
    }

    sBSql = sBSql + " SELECT po_order_bb1.corder_bid " + sFromWhereBB1 + ")";

    String sBB1Sql = " (SELECT po_order_bb1.corder_bb1id " + sFromWhereBB1 + ")";

    HashMap hmapHead = null;
    HashMap hmapBody = null;
    HashMap hmapOnWay = null;
    HashMap hmapRP = null;
    try {
      OrderDMO dmoOrder = new OrderDMO();

      hmapHead = dmoOrder.queryHeadHashMapByConds(" FROM po_order WHERE corderid IN " + sHSql);

      if (hmapHead == null) {
        return null;
      }

      hmapBody = dmoOrder.queryBodyHashmapHIDAsKeyByConds(" FROM po_order_b WHERE corder_bid IN " + sBSql);

      if (hmapBody == null) {
        return null;
      }

      hmapOnWay = new OrderBbDMO().queryHashMapBId_OnWayNum("po_order_bb.corder_bid IN " + sBSql, iOnWayStatus - 3);

      if (hmapOnWay == null) {
        return null;
      }

      hmapRP = new PoReceivePlanDMO().queryHMapBIDAsKeyByConds(" FROM po_order_bb1 WHERE corder_bb1id IN " + sBB1Sql);
    }
    catch (Exception e)
    {
      PubDMO.throwBusinessException(sMethodName, e);
    }

    String sLocalCurrId = null;
    int iPriceDigit = 0;
    try {
      sLocalCurrId = PubDMO.getLocalCurrId(sPk_corp);
      iPriceDigit = new nc.bs.pu.pub.PubImpl().getDigitBatch(sPk_corp, new String[] { "BD505" })[0];
    }
    catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }

    String[] saHId = (String[])(String[])hmapHead.keySet().toArray(new String[hmapHead.size()]);
    int iLen = saHId.length;
    for (int i = 0; i < iLen; i++)
    {
      OrderHeaderVO voHead = (OrderHeaderVO)hmapHead.get(saHId[i]);
      Vector vecBody = (Vector)hmapBody.get(saHId[i]);
      if (vecBody == null) {
        hmapHead.remove(saHId[i]);
      }
      else
      {
        OrderVO voOrder = new OrderVO();
        voOrder.setParentVO(voHead);
        voOrder.setChildrenVO((OrderItemVO[])(OrderItemVO[])vecBody.toArray(new OrderItemVO[vecBody.size()]));

        OrderItemVO[] voaItem = voOrder.getBodyVO();
        int iBodyLen = voaItem.length;
        for (int j = 0; j < iBodyLen; j++) {
          OrderItemVO voItem = voaItem[j];

          voItem.calLocalPrice(sLocalCurrId, iPriceDigit);
          voItem.calLocalTaxPrice(sLocalCurrId, iPriceDigit);

          if (hmapRP != null) {
            ArrayList listRP = (ArrayList)hmapRP.get(voItem.getCorder_bid());
            if (listRP != null) {
              voItem.setRPVOs((OrderReceivePlanVO[])(OrderReceivePlanVO[])listRP.toArray(new OrderReceivePlanVO[listRP.size()]));
            }

          }

          if (voItem.getNordernum() == null)
            continue;
          UFDouble dOnWayNum = PuPubVO.getUFDouble_NullAsZero(hmapOnWay.get(voItem.getCorder_bid()));

          if (iAfterBillStatus == 7)
          {
            UFDouble dAccuNum = PuPubVO.getUFDouble_NullAsZero(voItem.getNaccumarrvnum());
            voItem.setNnotrevonwaynum(dOnWayNum.sub(dAccuNum));
            voItem.setNnotarrvnum(voItem.getNnotrevonwaynum()); } else {
            if (iAfterBillStatus != 8)
              continue;
            UFDouble dAccuNum = PuPubVO.getUFDouble_NullAsZero(voItem.getNaccumstorenum());
            voItem.setNnotrevonwaynum(dOnWayNum.sub(dAccuNum));
            voItem.setNnotstorenum(voItem.getNnotrevonwaynum());
          }

        }

        hmapHead.remove(saHId[i]);
        hmapHead.put(voHead.getVordercode() + saHId[i], voOrder);
      }
    }
    TreeMap trmapRet = new TreeMap(hmapHead);
    return (OrderVO[])(OrderVO[])trmapRet.values().toArray(new OrderVO[trmapRet.size()]);
  }

  public OrderVO[] filterVosForDayPl(OrderVO[] voaSrc, PfParameterVO voPf, Hashtable hashPfRet)
    throws BusinessException
  {
    if ((voaSrc == null) || (voaSrc.length == 0)) {
      return null;
    }
    int iLen = voaSrc.length;
    OrderVO[] voaRet = null;
    try
    {
      String strPkcorp = null;
      int iPos = 0;
      while (strPkcorp == null) {
        if ((voaSrc[iPos] == null) || (voaSrc[iPos].getHeadVO() == null)) {
          continue;
        }
        strPkcorp = voaSrc[iPos].getHeadVO().getPk_corp();
        iPos++;
      }
      if (strPkcorp == null) {
        SCMEnv.out("程序BUG：不能正确获取公司主键，直接返回!");
        return null;
      }

      ICreateCorpQueryService corpDmo = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      if (!corpDmo.isEnabled(strPkcorp, "DM")) {
        return null;
      }
      voaRet = new OrderVO[iLen];
      for (int i = 0; i < iLen; i++) {
        voaRet[i] = ((OrderVO)voaSrc[i].clone());
      }
      ArrayList listValidVo = new ArrayList();

      if (hashPfRet != null) {
        for (int i = 0; i < iLen; i++) {
          if ((voaRet[i] == null) || (voaRet[i].getHeadVO() == null)) {
            continue;
          }
          if (hashPfRet.containsKey(String.valueOf(i))) {
            continue;
          }
          listValidVo.add(voaRet[i]);
        }
        if (listValidVo.size() == 0) {
          SCMEnv.out("本次所有订单均处于审批进行中或审批未通过，直接返回!");
          return null;
        }
        voaRet = (OrderVO[])(OrderVO[])listValidVo.toArray(new OrderVO[listValidVo.size()]);

        iLen = voaRet.length;
      }

      listValidVo = new ArrayList();
      for (int i = 0; i < iLen; i++) {
        if ((voaRet[i] == null) || (voaRet[i].getHeadVO() == null)) {
          continue;
        }
        if (!voaRet[i].getHeadVO().isDeliver()) {
          continue;
        }
        listValidVo.add(voaRet[i]);
      }
      if (listValidVo.size() == 0) {
        SCMEnv.out("本次所有订单均不走发运，直接返回!");
        return null;
      }
      voaRet = (OrderVO[])(OrderVO[])listValidVo.toArray(new OrderVO[listValidVo.size()]);

      iLen = voaRet.length;

      Hashtable hashIdStatusVO = new OrderstatusDMO().queryStatusVOsByPkCorp(strPkcorp);
      listValidVo = new ArrayList();
      OrderstatusVO status = null;
      for (int i = 0; i < iLen; i++) {
        if (voaRet[i].getHeadVO().getCbiztype() == null) {
          continue;
        }
        status = (OrderstatusVO)hashIdStatusVO.get(voaRet[i].getHeadVO().getCbiztype());
        if ((status == null) || ((status.getBisneedrp() != null) && (status.getBisneedrp().intValue() == 1) && (!voaRet[i].getHeadVO().isReturn()))) {
          continue;
        }
        listValidVo.add(voaRet[i]);
      }
      if (listValidVo.size() == 0) {
        SCMEnv.out("过滤：业务类型不安排到货计划的订单后没有可生成日计划数据，直接返回!");
        return null;
      }
      voaRet = (OrderVO[])(OrderVO[])listValidVo.toArray(new OrderVO[listValidVo.size()]);
      iLen = voaRet.length;

      ArrayList listErrorCode = new ArrayList();
      ArrayList listBody = null;
      OrderItemVO[] items = null;
      listValidVo = new ArrayList();
      int jLen = 0;
      for (int i = 0; i < iLen; i++) {
        if ((voaRet[i] == null) || (voaRet[i].getHeadVO() == null)) {
          continue;
        }
        items = voaRet[i].getBodyVO();
        if ((items == null) || (items.length == 0)) {
          continue;
        }
        jLen = items.length;
        listBody = new ArrayList();
        for (int j = 0; j < jLen; j++)
        {
          if (PuPubVO.getString_TrimZeroLenAsNull(items[j].getPk_arrvstoorg()) == null) {
            continue;
          }
          listBody.add(items[j]);
        }
        if (listBody.size() == 0) {
          listErrorCode.add(voaRet[i].getHeadVO().getVordercode());
        } else {
          items = (OrderItemVO[])(OrderItemVO[])listBody.toArray(new OrderItemVO[listBody.size()]);
          voaRet[i].setChildrenVO(items);
          listValidVo.add(voaRet[i]);
        }
      }
      if (listErrorCode.size() > 0) {
        int iSize = listErrorCode.size();
        String strErrMsg = listErrorCode.get(0) + "";
        for (int i = 1; i < iSize; i++) {
          strErrMsg = strErrMsg + NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000000") + listErrorCode;
        }
        strErrMsg = NCLangResOnserver.getInstance().getStrByID("4004020201", "UPP4004020201-000222", null, new String[] { strErrMsg });
        throw new BusinessException(strErrMsg);
      }
      voaRet = (OrderVO[])(OrderVO[])listValidVo.toArray(new OrderVO[listValidVo.size()]);
      iLen = voaRet.length;

      items = null;
      jLen = 0;
      ArrayList listCmangid = new ArrayList();
      for (int i = 0; i < iLen; i++) {
        if ((voaRet[i].getBodyVO() == null) || (voaRet[i].getBodyVO().length == 0)) {
          continue;
        }
        items = voaRet[i].getBodyVO();
        jLen = items.length;
        for (int j = 0; j < jLen; j++)
        {
          if ((items[j] == null) || (items[j].getCmangid() == null))
          {
            continue;
          }
          if (!listCmangid.contains(items[j].getCmangid())) {
            listCmangid.add(items[j].getCmangid());
          }
        }
      }
      HashMap mapLabDis = new OrderDMO().queryLabDisMangIdMap(listCmangid);
      if (mapLabDis == null) {
        mapLabDis = new HashMap();
      }
      listValidVo = new ArrayList();
      ArrayList listItemsValid = null;
      for (int i = 0; i < iLen; i++) {
        if ((voaRet[i].getBodyVO() == null) || (voaRet[i].getBodyVO().length == 0)) {
          continue;
        }
        items = voaRet[i].getBodyVO();
        jLen = items.length;
        listItemsValid = new ArrayList();
        for (int j = 0; j < jLen; j++)
        {
          if ((items[j] == null) || (items[j].getCmangid() == null))
          {
            continue;
          }
          if (!items[j].isActive())
          {
            continue;
          }
          if (mapLabDis.containsKey(items[j].getCmangid()))
          {
            continue;
          }
          if ((items[j].getNnotdayplnum() == null) || (items[j].getNnotdayplnum().doubleValue() == 0.0D)) {
            continue;
          }
          listItemsValid.add(items[j]);
        }
        if (listItemsValid.size() == 0) {
          continue;
        }
        if (listItemsValid.size() < jLen) {
          voaRet[i].setChildrenVO((OrderItemVO[])(OrderItemVO[])listItemsValid.toArray(new OrderItemVO[listItemsValid.size()]));
        }
        listValidVo.add(voaRet[i]);
      }
      if (listValidVo.size() == 0) {
        SCMEnv.out("过滤：行关闭、存货属性为劳务、折扣、ABS（订单数量）<= 已生成日计划数量后没有可生成日计划数据，直接返回!");
        return null;
      }
      voaRet = (OrderVO[])(OrderVO[])listValidVo.toArray(new OrderVO[listValidVo.size()]);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return voaRet;
  }

  public OrderVO[] filterNotCrtDayPlayBills(OrderVO[] vos)
    throws BusinessException
  {
    OrderVO[] voaRet = vos;

    int iLen = voaRet == null ? 0 : voaRet.length;

    String strPkcorp = null;
    int iPos = 0;
    while (strPkcorp == null) {
      if ((voaRet[iPos] == null) || (voaRet[iPos].getHeadVO() == null)) {
        continue;
      }
      strPkcorp = voaRet[iPos].getHeadVO().getPk_corp();
      iPos++;
    }
    if (strPkcorp == null) {
      SCMEnv.out("程序BUG：不能正确获取公司主键，直接返回!");
      return null;
    }
    ICreateCorpQueryService corpDmo = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
    if (!corpDmo.isEnabled(strPkcorp, "DM")) {
      return null;
    }

    Hashtable hashIdStatusVO = null;
    try {
      hashIdStatusVO = new OrderstatusDMO().queryStatusVOsByPkCorp(strPkcorp);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    if (hashIdStatusVO == null) {
      hashIdStatusVO = new Hashtable();
    }
    ArrayList listValidVo = new ArrayList();
    OrderstatusVO status = null;
    for (int i = 0; i < iLen; i++) {
      if (voaRet[i].getHeadVO().getCbiztype() == null) {
        continue;
      }
      status = (OrderstatusVO)hashIdStatusVO.get(voaRet[i].getHeadVO().getCbiztype());
      if ((status == null) || ((status.getBisneedrp() != null) && (status.getBisneedrp().intValue() == 1) && (!voaRet[i].getHeadVO().isReturn()))) {
        continue;
      }
      if (!voaRet[i].isDeliver()) {
        continue;
      }
      listValidVo.add(voaRet[i]);
    }
    if (listValidVo.size() == 0) {
      SCMEnv.out("剔除走到货计划的订单VO后无数据，直接返回!");
      return null;
    }
    voaRet = (OrderVO[])(OrderVO[])listValidVo.toArray(new OrderVO[listValidVo.size()]);

    return voaRet;
  }

  public void setCheckGoingTY(OrderVO voOrder, Object objUser)
    throws BusinessException
  {
    if ((voOrder == null) || (voOrder.getHeadVO() == null) || (voOrder.getHeadVO().getPrimaryKey() == null)) {
      SCMEnv.out("传入第一个参数(订单VO)不正确：为空或表头主键为空，直接返回!");
      return;
    }

    try
    {
      OrderDMO dmo = new OrderDMO();
      String strBillId = voOrder.getHeadVO().getPrimaryKey();

      dmo.checkGoing(strBillId, "", "", "");
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    finally
    {
    }
  }

  public UFBoolean isExistClosedUpSrcBill(ArrayList list)
    throws BusinessException
  {
    if ((list == null) || (list.size() == 0)) {
      SCMEnv.out("传入参数ArrayList不正确：为空或长度为0，直接返回NULL");
      return null;
    }
    UFBoolean bIsExist = null;
    try {
      OrderDMO dmo = new OrderDMO();
      bIsExist = dmo.isExistClosedPraybill((String[])(String[])list.toArray(new String[list.size()]));
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return bIsExist;
  }

  public UFBoolean[] queryIfExecPray(String[] saRowId)
    throws BusinessException
  {
    UFBoolean[] ufbRet = null;
    if ((saRowId == null) || (saRowId.length == 0)) {
      SCMEnv.out("传入参数为空！");
      return null;
    }
    try {
      OrderDMO dmo = new OrderDMO();
      ufbRet = dmo.queryIfExecPray(saRowId);
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    return ufbRet;
  }

  public void rewriteInvoiceCorp(OrderVO voChg, OrderVO voOld)
    throws BusinessException
  {
    if ((voChg == null) || (voOld == null) || (voChg.getBodyLen() == 0) || (voOld.getBodyLen() == 0))
    {
      return;
    }

    HashMap mapOldCorp = new HashMap();
    int iLen = voOld.getBodyLen();
    OrderItemVO[] items = voOld.getBodyVO();
    for (int i = 0; i < iLen; i++) {
      if ((items[i] == null) || (items[i].getCorder_bid() == null) || (items[i].getPk_invoicecorp() == null))
      {
        continue;
      }

      mapOldCorp.put(items[i].getCorder_bid(), items[i].getPk_invoicecorp());
    }

    ArrayList listArrRowId = new ArrayList();
    ArrayList listArrCorpId = new ArrayList();
    ArrayList listStoRowId = new ArrayList();
    ArrayList listStoCorpId = new ArrayList();
    iLen = voChg.getBodyLen();
    items = voChg.getBodyVO();
    String strOldCorp = null;
    boolean bArr = false;
    boolean bSto = false;
    for (int i = 0; i < iLen; i++) {
      if ((items[i] == null) || (items[i].getCorder_bid() == null))
      {
        continue;
      }

      bArr = PuPubVO.getUFDouble_ZeroAsNull(items[i].getNaccumarrvnum()) != null;

      bSto = PuPubVO.getUFDouble_ZeroAsNull(items[i].getNaccumstorenum()) != null;

      if ((!bArr) && (!bSto))
      {
        continue;
      }
      strOldCorp = (String)mapOldCorp.get(items[i].getCorder_bid());
      if (((strOldCorp != null) || (items[i].getPk_invoicecorp() == null)) && (strOldCorp.equals(items[i].getPk_invoicecorp())))
        continue;
      if (bArr) {
        listArrRowId.add(items[i].getCorder_bid());
        listArrCorpId.add(items[i].getPk_invoicecorp());
      }
      if (bSto) {
        listStoRowId.add(items[i].getCorder_bid());
        listStoCorpId.add(items[i].getPk_invoicecorp());
      }
    }

    String[] saRowId = null;
    String[] saCorpId = null;
    if (listArrRowId.size() > 0) {
      saRowId = (String[])(String[])listArrRowId.toArray(new String[listArrRowId.size()]);
      saCorpId = (String[])(String[])listArrCorpId.toArray(new String[listArrCorpId.size()]);
      try {
        new ArriveorderDMO().updateInvoiceCorpId(saRowId, saCorpId);
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }
    }
    if (listStoRowId.size() > 0) {
      saRowId = (String[])(String[])listStoRowId.toArray(new String[listStoRowId.size()]);
      saCorpId = (String[])(String[])listStoCorpId.toArray(new String[listStoCorpId.size()]);
      try {
        IICToPU_201GeneralHDMO dmo = (IICToPU_201GeneralHDMO)NCLocator.getInstance().lookup(IICToPU_201GeneralHDMO.class.getName());
        dmo.updateInvoiceCorpId(saRowId, saCorpId);
      } catch (Exception e) {
        PubDMO.throwBusinessException(e);
      }
    }
  }

  public void dealNtaxrateNullToZero(OrderVO[] voaOrder)
  {
    if ((voaOrder == null) || (voaOrder.length == 0)) {
      return;
    }
    int iLen = voaOrder.length;
    int jLen = 0;
    UFDouble ufdZero = new UFDouble(0.0D);
    for (int i = 0; i < iLen; i++) {
      if ((voaOrder[i] == null) || (voaOrder[i].getBodyLen() == 0)) {
        continue;
      }
      jLen = voaOrder[i].getBodyLen();
      for (int j = 0; j < jLen; j++)
        if (voaOrder[i].getBodyVO()[j].getNtaxrate() == null) {
          voaOrder[i].getBodyVO()[j].setNtaxrate(ufdZero);
          voaOrder[i].getBodyVO()[j].setNtaxmny(ufdZero);
        }
    }
  }
}