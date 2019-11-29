package nc.ui.ia.analyze;

import java.util.ArrayList;
import nc.bs.framework.common.NCLocator;
import nc.itf.ia.analyze.IIaAnalyse;
import nc.vo.ia.analyze.InvInOutSumVO;
import nc.vo.ia.analyze.QueryVO;
import nc.vo.ia.analyze.StatisticsVO;
import nc.vo.ia.analyze.VelocityVO;

public class IaAnalyseBO_Client
{
  private static String beanName = IIaAnalyse.class.getName();

  public static Object[] getInCost(QueryVO query)
    throws Exception
  {
    IIaAnalyse iIaAnalyse = (IIaAnalyse)NCLocator.getInstance().lookup(beanName);
    Object[] o = null;
    o = iIaAnalyse.getInCost(query);
    return o;
  }

  public static Object[] getAbc(String arg0, String arg1, String arg2, String[] arg3, String arg4)
    throws Exception
  {
    IIaAnalyse iIaAnalyse = (IIaAnalyse)NCLocator.getInstance().lookup(beanName);
    Object[] o = null;
    o = iIaAnalyse.getAbc(arg0, arg1, arg2, arg3, arg4);
    return o;
  }

  public static InvInOutSumVO[] getDiffAlloc(QueryVO arg0)
    throws Exception
  {
    IIaAnalyse iIaAnalyse = (IIaAnalyse)NCLocator.getInstance().lookup(beanName);
    InvInOutSumVO[] o = null;
    o = iIaAnalyse.getDiffAlloc(arg0);
    return o;
  }

  public static String checkCondition(QueryVO arg0) throws Exception
  {
    IIaAnalyse iIaAnalyse = (IIaAnalyse)NCLocator.getInstance().lookup(beanName);
    String o = null;
    o = iIaAnalyse.checkCondition(arg0);
    return o;
  }

  public static InvInOutSumVO[] getInBill(QueryVO arg0)
    throws Exception
  {
    IIaAnalyse iIaAnalyse = (IIaAnalyse)NCLocator.getInstance().lookup(beanName);
    InvInOutSumVO[] o = null;
    o = iIaAnalyse.getInBill(arg0);
    return o;
  }

  public static ArrayList getInOutSum(QueryVO arg0)
    throws Exception
  {
    IIaAnalyse iIaAnalyse = (IIaAnalyse)NCLocator.getInstance().lookup(beanName);
    ArrayList o = null;
    o = iIaAnalyse.getInOutSum(arg0);
    return o;
  }

  public static InvInOutSumVO[] getOutBill(QueryVO arg0)
    throws Exception
  {
    IIaAnalyse iIaAnalyse = (IIaAnalyse)NCLocator.getInstance().lookup(beanName);
    InvInOutSumVO[] o = null;
    o = iIaAnalyse.getOutBill(arg0);
    return o;
  }

  public static Object[] getStockCapital(String arg0, Integer arg1, String arg2)
    throws Exception
  {
    IIaAnalyse iIaAnalyse = (IIaAnalyse)NCLocator.getInstance().lookup(beanName);
    Object[] o = null;
    o = iIaAnalyse.getStockCapital(arg0, arg1, arg2);
    return o;
  }

  public static VelocityVO[] getVelocity(String arg0, String[] arg1, String arg2, StatisticsVO[] arg3)
    throws Exception
  {
    IIaAnalyse iIaAnalyse = (IIaAnalyse)NCLocator.getInstance().lookup(beanName);
    VelocityVO[] o = null;
    o = iIaAnalyse.getVelocity(arg0, arg1, arg2, arg3);
    return o;
  }
}