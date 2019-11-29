package nc.ui.pr.pray;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.pr.pray.IPraybill;
import nc.itf.pu.inter.IPuToIc_PraybillImpl;
import nc.itf.pu.inter.IPuToSc_PraybillDMO;
import nc.itf.pu.inter.IPuToTO_PuToTO;
import nc.vo.pi.NormalCondVO;
import nc.vo.pr.pray.BudgetItemVO;
import nc.vo.pr.pray.ImplementItemVO;
import nc.vo.pr.pray.ImplementVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pr.pray.PriceInfosVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pu.ParaVO21WriteNumTo20;
import nc.vo.scm.puplbillprocess.BillInvokeVO;

public class PraybillHelper
{
  private static String beanName = IPraybill.class.getName();

  public static UFBoolean checkBeforeSave(PraybillVO p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    UFBoolean o = bo.checkBeforeSave(p0);
    return o;
  }

  public static PraybillItemVO[] queryBodysForAsk(ArrayList p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillItemVO[] o = bo.queryBodysForAsk(p0);
    return o;
  }

  public static PraybillItemVO[] queryBodysForPriceAudit(ArrayList p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillItemVO[] o = bo.queryBodysForPriceAudit(p0);
    return o;
  }

  public static PraybillHeaderVO[] queryHeadsForAsk(ArrayList p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillHeaderVO[] o = bo.queryHeadsForAsk(p0);
    return o;
  }

  public static PraybillHeaderVO[] queryHeadsForPriceAudit(ArrayList p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillHeaderVO[] o = bo.queryHeadsForPriceAudit(p0);
    return o;
  }

  public static UFBoolean checkGenBill(String[] p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    UFBoolean o = bo.checkGenBill(p0);
    return o;
  }

  public static ArrayList closeBill(PraybillVO p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ArrayList o = bo.closeBill(p0);
    return o;
  }

  public static String[] getPurOrgForInv(String[] p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    String[] o = bo.getPurOrgForInv(p0);
    return o;
  }

  public static Vector computeBudget(PraybillHeaderVO[] p0, PraybillItemVO[] p1, Vector p2, String p3)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    Vector o = bo.computeBudget(p0, p1, p2, p3);
    return o;
  }

  public static void discardPraybillArr(String[] p0, String[] p1, String p2, String p3, String p4)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    bo.discardPraybillArr(p0, p1, p2, p3, p4);
  }

  public static ArrayList doSave(PraybillVO p0) throws Exception
  {
    IPuToIc_PraybillImpl bo = (IPuToIc_PraybillImpl)NCLocator.getInstance().lookup(IPuToIc_PraybillImpl.class.getName());

    ArrayList o = bo.doSave(p0);
    return o;
  }

  public static PraybillItemVO[] queryBody(String p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillItemVO[] o = bo.queryBody(p0);
    return o;
  }

  public static ArrayList queryPrayForSaveAudit(String p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ArrayList o = bo.queryPrayForSaveAudit(p0);
    return o;
  }

  public static PraybillVO queryPrayVoByHid(String p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillVO o = bo.queryPrayVoByHid(p0);
    return o;
  }

  public static String[] getAssistUnitFlag(PraybillItemVO[] p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    String[] o = bo.getAssistUnitFlag(p0);
    return o;
  }

  public static UFDouble getATPNum(String p0, String p1, String p2, String p3, String p4, String p5, String p6, String p7, String p8, String p9, String p10, String p11, String p12, String p13)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    UFDouble o = bo.getATPNum(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13);

    return o;
  }

  public static String[] queryBackMultiple(String p0, String p1, String p2, String p3)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    String[] o = bo.queryBackMultiple(p0, p1, p2, p3);
    return o;
  }

  public static String[] queryForwardMultiple(String p0, String p1, String p2, String p3)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    String[] o = bo.queryForwardMultiple(p0, p1, p2, p3);
    return o;
  }

  public static ArrayList getBudgetPrice(ArrayList p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ArrayList o = bo.getBudgetPrice(p0);
    return o;
  }

  public static UFDouble[] queryNewPriceArray(String p0, String[] p1) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    UFDouble[] o = bo.queryNewPriceArray(p0, p1);
    return o;
  }

  public static UFDouble[] queryNewPriceArray(String[] p0, String[] p1) throws Exception {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    UFDouble[] o = bo.queryNewPriceArray(p0, p1);
    return o;
  }

  public static BudgetItemVO[] getBudgetRefResult(BudgetItemVO[] p0) throws Exception {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    BudgetItemVO[] o = bo.getBudgetRefResult(p0);
    return o;
  }

  public static String[] getOutWarehouseid(String p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    String[] o = bo.getOutWarehouseid(p0);
    return o;
  }

  public static ImplementItemVO[] getImplementDRefResult(ImplementItemVO[] p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ImplementItemVO[] o = bo.getImplementDRefResult(p0);
    return o;
  }

  public static ImplementItemVO[] getImplementTRefResult(ImplementItemVO[] p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ImplementItemVO[] o = bo.getImplementTRefResult(p0);
    return o;
  }

  public static UFDouble getOnHandNum(String p0, String p1, String p2, String p3, String p4, String p5, String p6, String p7, String p8, String p9)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    UFDouble o = bo.getOnHandNum(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);

    return o;
  }

  public static String[] getRefDeptIDName(String p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    String[] o = bo.getRefDeptIDName(p0);
    return o;
  }

  public static String[] getRefOperatorAuditorName(String p0, String p1) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    String[] o = bo.getRefOperatorAuditorName(p0, p1);
    return o;
  }

  public static String[] getRefValues(String p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    String[] o = bo.getRefValues(p0);
    return o;
  }

  public static UFDouble[] getRulePrice(String[] p0, String[] p1, String p2, String p3)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    UFDouble[] o = bo.getRulePrice(p0, p1, p2, p3);
    return o;
  }

  public static PraybillVO getSpecifiedPraybill(String p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillVO o = bo.getSpecifiedPraybill(p0);
    return o;
  }

  public static String getUnit(String p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    String o = bo.getUnit(p0);
    return o;
  }

  public static PraybillItemVO[] getVOForUse(PraybillItemVO[] p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillItemVO[] o = bo.getVOForUse(p0);
    return o;
  }

  public static UFBoolean isInvBelongStoreOrg(PraybillVO p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    UFBoolean o = bo.isInvBelongStoreOrg(p0);
    return o;
  }

  public static UFBoolean isPsnBelongDept(PraybillVO p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    UFBoolean o = bo.isPsnBelongDept(p0);
    return o;
  }

  public static void onRearOrderDelete(String[] p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    bo.onRearOrderDelete(p0);
  }

  public static ArrayList openBill(PraybillVO p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ArrayList o = bo.openBill(p0);
    return o;
  }

  public static Vector queryAdvanceDays(String p0, String p1, String p2) throws Exception
  {
    IPuToSc_PraybillDMO bo = (IPuToSc_PraybillDMO)NCLocator.getInstance().lookup(IPuToSc_PraybillDMO.class.getName());

    Vector o = bo.queryAdvanceDays(p0, p1, p2);
    return o;
  }

  public static PraybillVO[] queryAll(String unitCode, ConditionVO[] conditionVO, String sAudit, String sOperPsn, String strSubSql) throws Exception
  {
    IPuToTO_PuToTO bo = (IPuToTO_PuToTO)NCLocator.getInstance().lookup(IPuToTO_PuToTO.class.getName());

    PraybillVO[] o = bo.queryAll(unitCode, conditionVO, sAudit, sOperPsn, strSubSql);

    return o;
  }

  public static String[] queryCalbodyForPs(String[] p1) throws Exception
  {
    IPuToTO_PuToTO bo = (IPuToTO_PuToTO)NCLocator.getInstance().lookup(IPuToTO_PuToTO.class.getName());

    String[] o = bo.queryCalbodyForPs(p1);
    return o;
  }

  public static ArrayList queryAllPrayBodys(ArrayList p0)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ArrayList o = bo.queryAllPrayBodys(p0);
    return o;
  }

  public static Vector queryAssistUnitData(String p0, String p1) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    Vector o = bo.queryAssistUnitData(p0, p1);
    return o;
  }

  public static PraybillVO[] queryBills(ArrayList p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillVO[] o = bo.queryBills(p0);
    return o;
  }

  public static PraybillVO[] queryBudget(String p0, ConditionVO[] p1) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillVO[] o = bo.queryBudget(p0, p1);
    return o;
  }

  public static PraybillVO[] queryForOrderBill(NormalCondVO[] p0, ConditionVO[] p1, UFDate p2)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PraybillVO[] o = bo.queryForOrderBill(p0, p1, p2);
    return o;
  }

  public static ImplementVO queryImplementDetail(String p0, ConditionVO[] p1, Boolean p2, Integer p3, String p4)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ImplementVO o = bo.queryImplementDetail(p0, p1, p2, p3, p4);

    return o;
  }

  public static ImplementVO queryImplementTotal(String[] p0, ConditionVO[] p1, int p2, Boolean p3, Integer p4, String p5)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ImplementVO o = bo.queryImplementTotal(p0, p1, p2, p3, p4, p5);

    return o;
  }

  public static Vector queryPraybillStatus(PraybillVO[] p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    Vector o = bo.queryPraybillStatus(p0);
    return o;
  }

  public static PriceInfosVO[] queryPriceInfos(String[] saInvBaseId, UFDate date, String[] saPurCorpId, String wherePart)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    PriceInfosVO[] o = bo.queryPriceInfos(saInvBaseId, date, saPurCorpId, wherePart);

    return o;
  }

  public static BillInvokeVO[] queryPrayDiff(String[] p0, UFDouble[] p1)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    BillInvokeVO[] o = bo.queryPrayDiff(p0, p1);
    return o;
  }

  public static ArrayList queryPurOrgAndVendor(String[] p0, String[] p1, String p2, String p3)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    ArrayList o = bo.queryPurOrgAndVendor(p0, p1, p2, p3);
    return o;
  }

  public static FreeVO queryVOForFreeItem(String p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    FreeVO o = bo.queryVOForFreeItem(p0);
    return o;
  }

  public static void updateAccumulateNum(String p0, UFDouble p1, UFDouble p2, String p3)
    throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    bo.updateAccumulateNum(p0, p1, p2, p3);
  }

  public static void updateAccumulateNum(ParaVO21WriteNumTo20[] p0) throws Exception
  {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);

    bo.updateAccumulateNum(p0);
  }

  public static Hashtable queryPsnByPurOrgCorps(String[] saCorpId, String[] saInvBasId) throws Exception {
    IPraybill bo = (IPraybill)NCLocator.getInstance().lookup(beanName);
    return bo.queryPsnByPurOrgCorps(saCorpId, saInvBasId);
  }
}