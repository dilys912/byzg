package nc.itf.uap.pf;

import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.msg.SysMessageParam;
import nc.vo.pub.pf.PfUtilBillActionVO;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.uap.rbac.RoleVO;
import nc.vo.uap.rbac.power.PowerResultVO;

public abstract interface IPFConfig
{
  public abstract PfUtilBillActionVO[] querybillActionStyle(String paramString1, String paramString2)
    throws BusinessException;

  public abstract String[] getBusitypeByCorpAndStyle(String paramString1, String paramString2)
    throws BusinessException;

  public abstract BusitypeVO[] querybillBusinessType(String paramString1, String paramString2)
    throws BusinessException;

  public abstract BillbusinessVO[] querybillSource(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract BillbusinessVO[] queryBillDest(String paramString1, String paramString2)
    throws BusinessException;

  public abstract RoleVO[] queryRolesHasBillbusi(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    throws BusinessException;

  public abstract PfUtilBillActionVO[] querybillStateActionStyle(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
    throws BusinessException;

  public abstract PfUtilBillActionVO[] querybillStateActionStyleNoBusi(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract CircularlyAccessibleValueObject[] queryHeadAllData(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract CircularlyAccessibleValueObject[] queryBodyAllData(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract AggregatedValueObject queryBillDataVO(String paramString1, String paramString2)
    throws BusinessException;

  public abstract SysMessageParam getSysMsgParam()
    throws BusinessException;

  public abstract void saveSysMsgParam(SysMessageParam paramSysMessageParam)
    throws BusinessException;

  public abstract PowerResultVO queryPowerBusiness(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract void generateBillItemByDDC(String paramString1, String paramString2, String paramString3)
    throws BusinessException;

  public abstract void completeWorkitem(String paramString1, String[] paramArrayOfString, String paramString2, String paramString3, String paramString4)
    throws BusinessException;
}