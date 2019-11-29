package nc.ui.ic.ic606;

import nc.ui.ic.pub.tools.GenMethod;
import nc.vo.ic.ic605.CargcardVO;
import nc.vo.pub.query.ConditionVO;

public class CargDisHelper
{
  private static String beanName = "nc.bs.ic.ic606.CargDisBO";

  public static CargcardVO queryCargDis(ConditionVO[] arg0) throws Exception {
    CargcardVO o = (CargcardVO)GenMethod.callICService(beanName, "queryCargDis", new Class[] { nc.vo.pub.query.ConditionVO.class }, new Object[] { arg0 });

    return o;
  }
}