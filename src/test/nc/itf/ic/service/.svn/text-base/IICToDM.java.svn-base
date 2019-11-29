package nc.itf.ic.service;

import nc.vo.dm.pub.DMDataVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public abstract interface IICToDM
{
  public abstract UFBoolean saveOutDM(AggregatedValueObject[] paramArrayOfAggregatedValueObject)
    throws BusinessException;

  public abstract UFBoolean reverseOutSignNum(DMDataVO[] paramArrayOfDMDataVO)
    throws BusinessException;

  public abstract UFBoolean setOutSignNum(DMDataVO[] paramArrayOfDMDataVO)
    throws BusinessException;

  public abstract UFBoolean setOutSignAndBackNum(DMDataVO[] paramArrayOfDMDataVO, boolean paramBoolean)
    throws BusinessException;

  public abstract UFDouble[] getRestNum(DMDataVO[] paramArrayOfDMDataVO)
    throws BusinessException;

  public abstract UFBoolean[] isBillsClosed(String[] paramArrayOfString)
    throws BusinessException;

  public abstract DMDataVO[] queryDeliv2OutSumShouldOutNum(String[] paramArrayOfString)
    throws BusinessException;

  public abstract DMDataVO[] queryDeliv2OutSumShouldOutNum2(String paramString)
    throws BusinessException;

  public abstract UFBoolean setPlanIssueNum(DMDataVO[] paramArrayOfDMDataVO)
    throws BusinessException;

  public abstract UFBoolean setIssueNum(DMDataVO[] paramArrayOfDMDataVO)
    throws BusinessException;

  public abstract UFBoolean setSignAndBackNum(DMDataVO[] paramArrayOfDMDataVO)
    throws BusinessException;

  public abstract UFBoolean setOutNum(DMDataVO[] paramArrayOfDMDataVO)
    throws BusinessException;
}