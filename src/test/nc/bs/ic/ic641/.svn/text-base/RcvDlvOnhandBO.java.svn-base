package nc.bs.ic.ic641;

import java.util.ArrayList;
import nc.bs.ic.pub.GenMethod;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.smart.ObjectUtils;

public class RcvDlvOnhandBO
{
  public ArrayList queryRcvDlvOnhand(QryConditionVO voCond)
    throws BusinessException
  {
    try
    {
      RevDel641Imp dmo = new RevDel641Imp();
      ArrayList vo = dmo.query(voCond);
      ObjectUtils.objectReference(vo);
      return vo;
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  public ArrayList queryRcvDlvOnhand_Old(QryConditionVO voCond)
  throws BusinessException
{
  try
  {
      RcvDlvOHandByFormulaDMO dmo = new RcvDlvOHandByFormulaDMO();
      ArrayList vo = dmo.query(voCond);
      ObjectUtils.objectReference(vo);
      return vo;
  }
  catch(Exception e)
  {
      if(e instanceof BusinessException)
          throw (BusinessException)e;
      else
          throw new BusinessException("Caused by:", e);
  }
}
}