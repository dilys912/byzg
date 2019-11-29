package nc.itf.scm.so.so001;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ct.TypeVO;
import nc.vo.so.pub.CustCreditVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

public abstract interface ISaleOrderQuery
{
  public abstract Hashtable getAllContractType(String[] paramArrayOfString)
    throws BusinessException;

  public abstract String[][] getBomChildInfo(String paramString1, String paramString2)
    throws BusinessException;

  public abstract String[][] getBomInfo(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract TypeVO getContractType(String paramString)
    throws BusinessException;

  public abstract UFDouble getCreditMoney(String paramString1, String paramString2)
    throws BusinessException;

  public abstract String[][] getCtCode(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract String[] getCustAddress(String paramString1, String paramString2)
    throws BusinessException;

  public abstract CustCreditVO getCustCreditData(CustCreditVO paramCustCreditVO, String paramString)
    throws BusinessException;

  public abstract UFDouble[] getCustManInfoAr(String paramString1, String paramString2)
    throws BusinessException;

  public abstract UFDate getDateByAheadPeriod(String paramString1, String paramString2, String paramString3, UFDate paramUFDate1, UFDate paramUFDate2)
    throws BusinessException;

  public abstract UFDouble getFuncExceptionValue(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String[] paramArrayOfString, SaleOrderVO paramSaleOrderVO)
    throws BusinessException;

  public abstract double getInvInNumByBatch(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract boolean isBomApproved(String paramString)
    throws BusinessException;

  public abstract boolean isCtExist(String paramString1, String paramString2)
    throws BusinessException;

  public abstract boolean isInvBatched(String paramString)
    throws BusinessException;

  public abstract boolean isInvBom(String paramString)
    throws BusinessException;

  public abstract boolean isInvExist(String paramString1, String paramString2)
    throws BusinessException;

  public abstract boolean isInvLocked(String paramString)
    throws BusinessException;

  public abstract SaleorderBVO[] queryBodyAllData(String paramString)
    throws BusinessException;

  public abstract SaleorderBVO[] queryBodyAllDataByIDs(String[] paramArrayOfString)
    throws BusinessException;

  public abstract UFDouble queryCachPayByOrdId(String paramString)
    throws BusinessException;

  public abstract AggregatedValueObject queryData(String paramString)
    throws BusinessException;

  public abstract SaleorderHVO[] queryHeadAllData(String paramString)
    throws BusinessException;

  public abstract ArrayList queryInfo(Integer paramInteger, Object[] paramArrayOfObject)
    throws BusinessException;

  public abstract SaleorderBVO[] queryOrderEnd(String paramString)
    throws BusinessException;

  public abstract Hashtable queryForCntAll(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, UFDate paramUFDate)
    throws BusinessException;

  public abstract SaleOrderVO querySourceBillVOForLinkAdd(String paramString1, String paramString2, String paramString3, Object paramObject)
    throws BusinessException;
}