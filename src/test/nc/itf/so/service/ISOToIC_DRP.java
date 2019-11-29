package nc.itf.so.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.naming.NamingException;
import nc.bs.pub.SystemException;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;

public abstract interface ISOToIC_DRP
{
  public abstract boolean isSaleOut(String paramString1, String paramString2)
    throws BusinessException;

  public abstract UFDouble getOutNum(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract Hashtable getSaleOrderPrice(String[] paramArrayOfString)
    throws BusinessException;

  public abstract Hashtable getSaleOrderCust(String[] paramArrayOfString)
    throws BusinessException;

  public abstract void setOutNum(AggregatedValueObject paramAggregatedValueObject)
    throws BusinessException;

  public abstract void setOutNum(String paramString1, String paramString2, String paramString3, String paramString4, UFDouble paramUFDouble)
    throws BusinessException;

  public abstract void isOverSaleOrder(AggregatedValueObject paramAggregatedValueObject)
    throws BusinessException;

  public abstract void setSignNum(String paramString1, String paramString2, String paramString3, UFDouble paramUFDouble)
    throws BusinessException;

  public abstract Hashtable getOosNumber(ArrayList paramArrayList)
    throws BusinessException;

  public abstract void setReturnNum(CircularlyAccessibleValueObject[] paramArrayOfCircularlyAccessibleValueObject)
    throws BusinessException;

  /** @deprecated */
  public abstract void setLockedFlag(ArrayList paramArrayList1, ArrayList paramArrayList2)
    throws SQLException, BusinessException, NamingException, SystemException;

  public abstract void setTransLossNum(String[] paramArrayOfString, UFDouble[] paramArrayOfUFDouble)
    throws BusinessException;

  /** @deprecated */
  public abstract void setOOSNum(GeneralBillVO paramGeneralBillVO)
    throws Exception;

  public abstract void setOOSNum(GeneralBillVO[] paramArrayOfGeneralBillVO)
    throws Exception;

  /** @deprecated */
  public abstract void setOOSNumUnDo(GeneralBillVO paramGeneralBillVO)
    throws Exception;

  public abstract void setOOSNumUnDo(GeneralBillVO[] paramArrayOfGeneralBillVO)
    throws Exception;
}