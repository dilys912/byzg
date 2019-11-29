package nc.itf.ct.service;

import java.util.Hashtable;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;

public abstract interface ICtToSo_QueryCt
{
  public abstract RetCtToPoQueryVO[][] queryForCntAll(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, UFDate paramUFDate)
    throws BusinessException;

  public abstract Hashtable queryCtCodeAndNameByIDSql(String paramString)
    throws BusinessException;
}