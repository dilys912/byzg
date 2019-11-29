package nc.itf.ct.pub;

import java.util.ArrayList;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.ct.pub.ManageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public abstract interface IContractBaseQuery
{
  public abstract UFBoolean canBSCModify(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract String findCurrByCustid(String paramString)
    throws BusinessException;

  public abstract String[] getCtSysParaString(String paramString, String[] paramArrayOfString)
    throws BusinessException;

  public abstract Integer qryCtStatus(String paramString)
    throws BusinessException;

  public abstract String qryStatusTs(String paramString)
    throws BusinessException;

  public abstract CurrinfoVO[] queryAllRateDigit(String paramString)
    throws BusinessException;

  public abstract ManageVO[] queryBill(String paramString1, String paramString2)
    throws BusinessException;

  public abstract ManageVO[] queryChangedBills(ArrayList paramArrayList)
    throws BusinessException;

  public abstract String[] queryCustInfo(String paramString)
    throws BusinessException;

  public abstract String[] queryCtCtrlScope(String paramString)
    throws BusinessException;
}