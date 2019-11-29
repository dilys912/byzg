package nc.itf.scm.service;

import nc.vo.pub.BusinessException;
import nc.vo.scm.service.ServcallVO;

public abstract interface IScmEJBService
{
  public abstract Object[] callEJBService(String paramString, ServcallVO[] paramArrayOfServcallVO)
    throws BusinessException;

  public abstract Object[] callEJBService_RequiresNew(String paramString, ServcallVO[] paramArrayOfServcallVO)
    throws BusinessException;
}