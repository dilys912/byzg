package nc.itf.uap.bd.accsubj;

import nc.vo.bd.b02.BdinfoVO;
import nc.vo.pub.BusinessException;

public abstract interface IBdinfoQry
{
  public abstract BdinfoVO findSubjBdinfoVOByPrimaryKey(String paramString)
    throws BusinessException;

  public abstract BdinfoVO[] querySubjBdinfoVos(String paramString)
    throws BusinessException;

  public abstract BdinfoVO[] queryBySubjBdinfoVOs(BdinfoVO paramBdinfoVO, Boolean paramBoolean)
    throws BusinessException;

  public abstract BdinfoVO[] querySubjBdinfoVos(String paramString1, String paramString2)
    throws BusinessException;

  public abstract BdinfoVO[] queryBySubjBdinfoVOs(BdinfoVO paramBdinfoVO, Boolean paramBoolean, String paramString)
    throws BusinessException;

  public abstract BdinfoVO[] querySubjBdinfoDetail(String[] paramArrayOfString)
    throws BusinessException;

  public abstract BdinfoVO[] querySubjBdinfoVosByGlorgbook(String paramString1, String paramString2)
    throws BusinessException;
}