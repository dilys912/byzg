package nc.itf.scm.so.so001;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.BomorderHeaderVO;
import nc.vo.so.so001.BomorderItemVO;
import nc.vo.so.so001.BomorderVO;

public abstract interface IBomorderQuery
{
  public abstract BomorderVO findByPrimaryKey(String paramString)
    throws BusinessException;

  public abstract BomorderItemVO[] findItemsForHeader(String paramString)
    throws BusinessException;

  public abstract BomorderItemVO[] findItemsForWlbm(String paramString1, String paramString2)
    throws BusinessException;

  public abstract BomorderItemVO[] findItemsForWlbm(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    throws BusinessException;

  public abstract BomorderVO[] getBomOrdersByDate(String paramString1, String paramString2)
    throws BusinessException;

  public abstract BomorderVO getBomOrderVO(String paramString)
    throws BusinessException;

  public abstract UFDouble getBomPrice(String paramString)
    throws BusinessException;

  public abstract UFDouble getBomPriceUnify(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract String getBusiTage(String paramString)
    throws BusinessException;

  public abstract BomorderVO queryData(String paramString1, String paramString2)
    throws BusinessException;

  public abstract BomorderHeaderVO[] queryHeadAllData(String paramString)
    throws BusinessException;
}