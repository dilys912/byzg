package nc.ui.bd.basedata;

import nc.ui.bd.pub.AbstractDecorator;
import nc.ui.trade.businessaction.IBusinessController;
import nc.vo.bd.basedata.CurrtypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class EuroPatchDecorator extends AbstractDecorator
{
  public EuroPatchDecorator(IBusinessController businessAction)
  {
    super(businessAction);
  }

  private void patchEuro(AggregatedValueObject billVO)
  {
    CircularlyAccessibleValueObject[] vos = billVO.getChildrenVO();
    if (vos != null)
    {
      for (CircularlyAccessibleValueObject o : vos) {
        CurrtypeVO vo = (CurrtypeVO)o;
        vo.patchECUSymbol();
      }
    }
  }

  public AggregatedValueObject save(AggregatedValueObject billVO, String billType, String billDate, Object userObj, AggregatedValueObject checkVo) throws Exception
  {
    patchEuro(billVO);
    return super.save(billVO, billType, billDate, userObj, checkVo);
  }
}