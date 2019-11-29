package nc.itf.ct.service;

import java.util.ArrayList;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.ctpo.CtAllotImprestPoVO;
import nc.vo.scm.ctpo.CtStatusToPoVO;
import nc.vo.scm.ctpo.ParaPoToCtRewriteVO;

public abstract interface ICtToPo_BackToCt
{
  public abstract void writeBackAccuOrdData(ParaPoToCtRewriteVO[] paramArrayOfParaPoToCtRewriteVO)
    throws BusinessException;

  public abstract CtStatusToPoVO isCtActive(CtStatusToPoVO paramCtStatusToPoVO)
    throws BusinessException;

  public abstract DMDataVO[] qryExecImprest(ArrayList paramArrayList)
    throws BusinessException;

  public abstract CtAllotImprestPoVO[] qryNeedAllotImprestBills(ArrayList paramArrayList)
    throws BusinessException;

  public abstract void writeBackImprest(CtAllotImprestPoVO[] paramArrayOfCtAllotImprestPoVO)
    throws BusinessException;
}