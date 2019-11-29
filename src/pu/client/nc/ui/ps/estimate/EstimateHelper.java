package nc.ui.ps.estimate;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.ps.estimate.IEstimate;
import nc.itf.ps.estimate.IEstimate_saleyf;
import nc.itf.ps.estimate.IEstimate_ww;
import nc.itf.ps.estimate.IEstimate_wwyf;
import nc.itf.ps.estimate.IEstimate_yf;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.estimate.saleEstimateVO;
import nc.vo.ps.estimate.GeneralHVO;
import nc.vo.ps.estimate.wwEstimateVO;
import nc.vo.ps.settle.OorderVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.session.ClientLink;

public class EstimateHelper
{
  private static String beanName = IEstimate.class.getName();
  private static String beanName_yf = IEstimate_yf.class.getName();
  private static String beanName_saleyf = IEstimate_saleyf.class.getName();
  private static String beanName_wwyf = IEstimate_wwyf.class.getName();
  private static String beanName_ww = IEstimate_ww.class.getName();

  public static void antiEstimate(EstimateVO[] p0, String p1) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    bo.antiEstimate(p0, p1);
  }

  public static void antiEstimateBatch(GeneralBillVO[] p0) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    bo.antiEstimateBatch(p0);
  }

  public static void discard(ArrayList p0) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    bo.discard(p0);
  }

  public static ArrayList doSave(GeneralHVO p0, boolean p1, ArrayList p2, String p3, OorderVO[] p4) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.doSave(p0, p1, p2, p3, p4);
    return (ArrayList)result;
  }

  public static void estimate(EstimateVO[] p0, ArrayList p1) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    bo.estimate(p0, p1);
  }

  public static String getStoreOrg(String p0) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.getStoreOrg(p0);
    return (String)result;
  }

  public static String[][] getFreeItem0(GeneralHVO[] p0) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.getFreeItem0(p0);
    if (result == null) {
      return null;
    }
    return (String[][])result;
  }

  public static String getRefDeptKey(String p0) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.getRefDeptKey(p0);
    return (String)result;
  }

  public static String getRefDeptName(String p0) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.getRefDeptName(p0);
    return (String)result;
  }

  public static String getRefOperatorKey(String p0) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.getRefOperatorKey(p0);
    return (String)result;
  }

  public static String[] getRSModeBatch(String[] p0) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.getRSModeBatch(p0);
    if (result == null) {
      return null;
    }
    return (String[])result;
  }

  public static boolean isCodeDuplicate(String p0, String p1, String p2) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    boolean result = bo.isCodeDuplicate(p0, p1, p2);
    return result;
  }

  public static EstimateVO[] queryEstimate(String p0, ConditionVO[] p1, String p2, String p3) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.queryEstimate(p0, p1, p2, p3);
    if (result == null) {
      return null;
    }
    return (EstimateVO[]) result;
  }

  public static ArrayList queryInitialBody(String p0, String p1) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.queryInitialBody(p0, p1);
    return (ArrayList)result;
  }

  public static ArrayList queryInitialBodyBatch(String[] p0, String[] p1) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.queryInitialBodyBatch(p0, p1);
    return (ArrayList)result;
  }

  public static GeneralHVO[] queryStockForEstimate(String p0, ConditionVO[] p1) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.queryStockForEstimate(p0, p1);
    if (result == null) {
      return null;
    }
    return (GeneralHVO[])result;
  }

  public static OorderVO[] queryOrder(String p0, ConditionVO[] p1, String[] p2, String p3) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.queryOrder(p0, p1, p2, p3);
    if (result == null) {
      return null;
    }
    return (OorderVO[])result;
  }

  public static String[] querySpeBiztypeID(String p0) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.querySpeBiztypeID(p0);
    if (result == null) {
      return null;
    }
    return (String[])result;
  }

  public static GeneralHVO queryStockByHeadKey(String p0, String p1) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    Object result = bo.queryStockByHeadKey(p0, p1);
    return (GeneralHVO)result;
  }

  public static void saveBillForARAP(EstimateVO[] p0, ArrayList p1) throws Exception {
    IEstimate bo = (IEstimate)NCLocator.getInstance().lookup(beanName);
    bo.saveBillForARAP(p0, p1);
  }

  public static void estimate_yf(EstimateVO[] vOs, ArrayList p1, ClientLink cl) throws Exception
  {
    IEstimate_yf bo = (IEstimate_yf)NCLocator.getInstance().lookup(beanName_yf);
    bo.estimate_yfn(vOs, p1, cl);
  }

  public static EstimateVO[] queryEstimate_yf(String p0, ConditionVO[] p1, String p2, String p3) throws Exception
  {
    IEstimate_yf bo = (IEstimate_yf)NCLocator.getInstance().lookup(beanName_yf);
    Object result = bo.queryEstimate_yfn(p0, p1, p2, p3);
    if (result == null) {
      return null;
    }
    return (EstimateVO[])result;
  }

  public static void antiEstimate_yf(EstimateVO[] vOs, String p1) throws Exception
  {
    IEstimate_yf bo = (IEstimate_yf)NCLocator.getInstance().lookup(beanName_yf);
    bo.antiEstimate_yfn(vOs, p1);
  }
  
  //销售运费暂估——查询 wangkaifei -2014-10-17
  public static saleEstimateVO[] queryEstimate_saleyf(String p0, ConditionVO[] p1, String p2, String p3) throws Exception
  {
    IEstimate_saleyf bo = (IEstimate_saleyf)NCLocator.getInstance().lookup(beanName_saleyf);
    Object result = bo.queryEstimate_saleyf(p0, p1, p2, p3);
    if (result == null) {
      return null;
    }
    return (saleEstimateVO[])result;
  }
  //销售运费暂估-暂估 wangkaifei2014-10-22
  public static void estimate_saleyf(saleEstimateVO[] vOs, ArrayList p1, ClientLink cl) throws Exception
  {
    IEstimate_saleyf bo = (IEstimate_saleyf)NCLocator.getInstance().lookup(beanName_saleyf);
    bo.estimate_saleyf(vOs, p1, cl);
  }
  
  public static void antiEstimate_saleyf(saleEstimateVO[] vOs, String p1) throws Exception
  {
    IEstimate_saleyf bo = (IEstimate_saleyf)NCLocator.getInstance().lookup(beanName_saleyf);
    bo.antiEstimate_saleyf(vOs, p1);
  }
  
  //委外运费暂估——查询 wangkaifei -2014-11-03
  public static wwEstimateVO[] queryEstimate_wwyf(String p0, ConditionVO[] p1, String p2, String p3) throws Exception
  {
    IEstimate_wwyf bo = (IEstimate_wwyf)NCLocator.getInstance().lookup(beanName_wwyf);
    Object result = bo.queryEstimate_wwyf(p0, p1, p2, p3);
    if (result == null) {
      return null;
    }
    return (wwEstimateVO[])result;
  }
  
  //委外运费暂估-暂估 wangkaifei2014-11-03
  public static void estimate_wwyf(wwEstimateVO[] vOs, ArrayList p1, ClientLink cl) throws Exception
  {
    IEstimate_wwyf bo = (IEstimate_wwyf)NCLocator.getInstance().lookup(beanName_wwyf);
    bo.estimate_wwyf(vOs, p1, cl);
  }
  
  public static void antiEstimate_wwyf(wwEstimateVO[] vOs, String p1) throws Exception
  {
    IEstimate_wwyf bo = (IEstimate_wwyf)NCLocator.getInstance().lookup(beanName_wwyf);
    bo.antiEstimate_wwyf(vOs, p1);
  }

//委外加工费暂估——查询 wangkaifei -2014-11-03
  public static wwEstimateVO[] queryEstimate_ww(String p0, ConditionVO[] p1, String p2, String p3) throws Exception
  {
    IEstimate_ww bo = (IEstimate_ww)NCLocator.getInstance().lookup(beanName_ww);
    Object result = bo.queryEstimate_ww(p0, p1, p2, p3);
    if (result == null) {
      return null;
    }
    return (wwEstimateVO[])result;
  }
  
//委外加工费暂估-暂估 wangkaifei2014-11-03
  public static void estimate_ww(wwEstimateVO[] vOs, ArrayList p1, ClientLink cl) throws Exception
  {
    IEstimate_ww bo = (IEstimate_ww)NCLocator.getInstance().lookup(beanName_ww);
    bo.estimate_ww(vOs, p1, cl);
  }
  //委外加工费暂估——反暂估wangkaifei -2014-11-03
  public static void antiEstimate_ww(wwEstimateVO[] vOs, String p1) throws Exception
  {
    IEstimate_ww bo = (IEstimate_ww)NCLocator.getInstance().lookup(beanName_ww);
    bo.antiEstimate_ww(vOs, p1);
  }
}