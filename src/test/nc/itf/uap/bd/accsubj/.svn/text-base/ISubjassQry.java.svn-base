package nc.itf.uap.bd.accsubj;

import nc.vo.bd.b02.AccsubjVO;
import nc.vo.bd.b02.GlSubjAssVO;
import nc.vo.bd.b02.SubjassVO;
import nc.vo.pub.BusinessException;

public abstract interface ISubjassQry
{
  public abstract SubjassVO[] querySubjassVosByVO(SubjassVO paramSubjassVO, Boolean paramBoolean)
    throws BusinessException;

  public abstract SubjassVO[] querySubjassVos(String[] paramArrayOfString, String paramString1, String paramString2)
    throws BusinessException;

  /** @deprecated */
  public abstract SubjassVO[] querySubjassVos(String[] paramArrayOfString, String paramString)
    throws BusinessException;

  public abstract SubjassVO[] querySubjassVosDetial(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract AccsubjVO[] queryAllByAssPksAndCorpPKs(String[] paramArrayOfString1, String[] paramArrayOfString2, String paramString)
    throws BusinessException;

  public abstract GlSubjAssVO[] queryAllBySubjPKs(String[] paramArrayOfString)
    throws BusinessException;

  public abstract SubjassVO[] queryBDInfo(String paramString)
    throws BusinessException;

  public abstract SubjassVO[] queryLeafAccsubjWithBdinfo(String paramString1, String paramString2)
    throws BusinessException;

  public abstract SubjassVO[] querySubjassVos(String[] paramArrayOfString, String paramString1, String paramString2, String paramString3, String paramString4)
    throws BusinessException;
}