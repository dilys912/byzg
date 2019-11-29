package nc.ui.ct.pub;

import java.util.ArrayList;
import nc.bs.framework.common.NCLocator;
import nc.itf.ct.pub.IContractBaseQuery;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.ct.pub.ManageVO;
import nc.vo.pub.lang.UFBoolean;

public class ContractQueryHelper
{
  private static String beanName = IContractBaseQuery.class.getName();

  public static UFBoolean canBSCModify(String arg0, String arg1, String arg2) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    UFBoolean o = null;
    o = bo.canBSCModify(arg0, arg1, arg2);
    return o;
  }

  public static String findCurrByCustid(String arg0) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    String o = null;
    o = bo.findCurrByCustid(arg0);
    return o;
  }

  public static Integer qryCtStatus(String arg0) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    Integer o = null;
    o = bo.qryCtStatus(arg0);
    return o;
  }

  public static String qryStatusTs(String arg0) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    String o = null;
    o = bo.qryStatusTs(arg0);
    return o;
  }

  public static CurrinfoVO[] queryAllRateDigit(String arg0) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    CurrinfoVO[] o = null;
    o = bo.queryAllRateDigit(arg0);
    return o;
  }

  public static ManageVO[] queryChangedBills(ArrayList arg0) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    ManageVO[] o = null;
    o = bo.queryChangedBills(arg0);
    return o;
  }

  public static String[] queryCustInfo(String arg0) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    String[] o = null;
    o = bo.queryCustInfo(arg0);
    return o;
  }

  public static String[] getSYSParaString(String arg0, String[] arg1) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    String[] o = null;
    o = bo.getCtSysParaString(arg0, arg1);
    return o;
  }

  public static ManageVO[] queryBill(String arg0, String arg1) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    ManageVO[] o = null;
    o = bo.queryBill(arg0, arg1);
    return o;
  }

  public static String[] queryCtCtrlScope(String arg0) throws Exception
  {
    IContractBaseQuery bo = (IContractBaseQuery)NCLocator.getInstance().lookup(beanName);
    String[] o = null;
    o = bo.queryCtCtrlScope(arg0);
    return o;
  }
}