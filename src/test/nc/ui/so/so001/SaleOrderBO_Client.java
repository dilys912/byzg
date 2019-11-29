package nc.ui.so.so001;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.framework.common.NCLocator;
import nc.itf.scm.so.so001.ISaleOrder;
import nc.itf.scm.so.so001.ISaleOrderQuery;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ct.TypeVO;
import nc.vo.so.pub.CustCreditVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

public class SaleOrderBO_Client
{
  private static String beanName = ISaleOrder.class.getName();
  private static String beanName1 = ISaleOrderQuery.class.getName();

  public static void delete(String arg0) throws Exception {
    ISaleOrder bo = (ISaleOrder)NCLocator.getInstance().lookup(beanName);

    bo.orderDelete(arg0);
  }

  public static ArrayList insert(SaleOrderVO arg0) throws Exception
  {
    ISaleOrder bo = (ISaleOrder)NCLocator.getInstance().lookup(beanName);
    ArrayList o = null;
    o = bo.insert(arg0);
    return o;
  }

  public static ArrayList update(SaleOrderVO arg0) throws Exception
  {
    ISaleOrder bo = (ISaleOrder)NCLocator.getInstance().lookup(beanName);
    ArrayList o = null;
    o = bo.update(arg0);
    return o;
  }

  public static ArrayList queryInfo(Integer arg0, Object[] arg1) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    ArrayList o = null;
    o = bo.queryInfo(arg0, arg1);
    return o;
  }

  public static AggregatedValueObject queryData(String arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    AggregatedValueObject o = null;
    o = bo.queryData(arg0);
    return o;
  }

  public static SaleorderHVO[] queryHeadAllData(String arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    SaleorderHVO[] o = null;
    o = bo.queryHeadAllData(arg0);
    return o;
  }

  public static SaleorderBVO[] queryBodyAllData(String arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    SaleorderBVO[] o = null;
    o = bo.queryBodyAllData(arg0);
    return o;
  }

  public static void updateLock(String arg0, String arg1) throws Exception
  {
    ISaleOrder bo = (ISaleOrder)NCLocator.getInstance().lookup(beanName);
    bo.orderUpdateLock(arg0, arg1);
  }

  public static Hashtable getAllContractType(String[] arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    Hashtable o = null;
    o = bo.getAllContractType(arg0);
    return o;
  }

  public static String[][] getBomChildInfo(String arg0, String arg1) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    String[][] o = (String[][])null;
    o = bo.getBomChildInfo(arg0, arg1);
    return o;
  }

  public static String[][] getBomInfo(String arg0, String arg1, String arg2) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    String[][] o = (String[][])null;
    o = bo.getBomInfo(arg0, arg1, arg2);
    return o;
  }

  public static TypeVO getContractType(String arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    TypeVO o = null;
    o = bo.getContractType(arg0);
    return o;
  }

  public static UFDouble getCreditMoney(String arg0, String arg1) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    UFDouble o = null;
    o = bo.getCreditMoney(arg0, arg1);
    return o;
  }

  public static String[][] getCtCode(String arg0, String arg1, String arg2) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    String[][] o = (String[][])null;
    o = bo.getCtCode(arg0, arg1, arg2);
    return o;
  }

  public static String[] getCustAddress(String arg0, String arg1) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    String[] o = null;
    o = bo.getCustAddress(arg0, arg1);
    return o;
  }

  public static CustCreditVO getCustCreditData(CustCreditVO arg0, String arg1) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    CustCreditVO o = null;
    o = bo.getCustCreditData(arg0, arg1);
    return o;
  }

  public static UFDouble[] getCustManInfoAr(String arg0, String arg1) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    UFDouble[] o = null;
    o = bo.getCustManInfoAr(arg0, arg1);
    return o;
  }

  public static UFDate getDateByAheadPeriod(String arg0, String arg1, String arg2, UFDate arg3, UFDate arg4) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    UFDate o = null;
    o = bo.getDateByAheadPeriod(arg0, arg1, arg2, arg3, arg4);
    return o;
  }

  public static UFDouble getFuncExceptionValue(String arg0, String arg1, String arg2, String arg3, String arg4, String[] arg5, SaleOrderVO arg6) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    UFDouble o = null;
    o = bo.getFuncExceptionValue(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    return o;
  }

  public static double getInvInNumByBatch(String arg0, String arg1, String arg2) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    double o = 0.0D;
    o = bo.getInvInNumByBatch(arg0, arg1, arg2);
    return o;
  }

  public static boolean isBomApproved(String arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    boolean o = false;
    o = bo.isBomApproved(arg0);
    return o;
  }

  public static boolean isCtExist(String arg0, String arg1) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    boolean o = false;
    o = bo.isCtExist(arg0, arg1);
    return o;
  }

  public static Hashtable queryForCntAll(String sPk_corp, String[] sman, String[] sven, UFDate date)
    throws BusinessException
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    Hashtable o = null;
    o = bo.queryForCntAll(sPk_corp, sman, sven, date);
    return o;
  }

  public static boolean isInvBatched(String arg0)
    throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    boolean o = false;
    o = bo.isInvBatched(arg0);
    return o;
  }

  public static boolean isInvBom(String arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    boolean o = false;
    o = bo.isInvBom(arg0);
    return o;
  }

  public static boolean isInvExist(String arg0, String arg1) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    boolean o = false;
    o = bo.isInvExist(arg0, arg1);
    return o;
  }

  public static boolean isInvLocked(String arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    boolean o = false;
    o = bo.isInvLocked(arg0);
    return o;
  }

  public static SaleorderBVO[] queryBodyAllDataByIDs(String[] arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    SaleorderBVO[] o = null;
    o = bo.queryBodyAllDataByIDs(arg0);
    return o;
  }

  public static UFDouble queryCachPayByOrdId(String arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    UFDouble o = null;
    o = bo.queryCachPayByOrdId(arg0);
    return o;
  }

  public static SaleorderBVO[] queryOrderEnd(String arg0) throws Exception
  {
    ISaleOrderQuery bo = (ISaleOrderQuery)NCLocator.getInstance().lookup(beanName1);
    SaleorderBVO[] o = null;
    o = bo.queryOrderEnd(arg0);
    return o;
  }

  public static void returnCode(SaleOrderVO arg0) throws Exception
  {
    ISaleOrder bo = (ISaleOrder)NCLocator.getInstance().lookup(beanName);
    bo.returnCode(arg0);
  }

  public static void saveSaleBillForImport(SaleOrderVO arg0) throws Exception
  {
    ISaleOrder bo = (ISaleOrder)NCLocator.getInstance().lookup(beanName);
    bo.saveSaleBillForImport(arg0);
  }

  public static void updateOrderEnd(SaleorderBVO[] arg0) throws Exception
  {
    ISaleOrder bo = (ISaleOrder)NCLocator.getInstance().lookup(beanName);
    bo.updateOrderEnd(arg0);
  }

  public static void updateRetinvFlag(String arg0) throws Exception
  {
    ISaleOrder bo = (ISaleOrder)NCLocator.getInstance().lookup(beanName);
    bo.updateRetinvFlag(arg0);
  }

  public static boolean isModuleEnabled(String pkCorp, String funcCode)
    throws Exception
  {
    ICreateCorpQueryService srv = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());

    return srv.isEnabled(pkCorp, funcCode);
  }
}