package nc.bs.pub.pf;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

public abstract interface IQueryData
{
  public abstract CircularlyAccessibleValueObject[] queryAllBodyData(String paramString)
    throws BusinessException;

  public abstract CircularlyAccessibleValueObject[] queryAllHeadData(String paramString)
    throws BusinessException;
}