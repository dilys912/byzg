package nc.ui.pi;

import nc.bs.framework.common.NCLocator;
import nc.itf.ic.pub.IIcbItf;
import nc.vo.pub.query.ConditionVO;

public class InvoiceHelper1
{
  private static String beanName = IIcbItf.class.getName();


  public static Object[] queryGenenelVOsByFromWhere(String p0, String p1, ConditionVO[] p2) throws Exception {
    IIcbItf bo = (IIcbItf)NCLocator.getInstance().lookup(beanName);
    Object[] o = null;
    o = bo.queryGenenelVOsByFromWhere(p0, p1, p2);
    return o;
  }
  
}