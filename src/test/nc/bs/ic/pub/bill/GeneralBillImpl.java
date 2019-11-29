package nc.bs.ic.pub.bill;

import java.util.ArrayList;
import nc.bs.ic.pub.GenMethod;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.bill.BillTempletDMO;
import nc.itf.ic.pub.IGeneralBill;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.pub.BusinessException;

public class GeneralBillImpl
  implements IGeneralBill
{
  public ArrayList queryBills(String sBillType, QryConditionVO voQC)
    throws BusinessException
  {
    ArrayList al = null;
    try {
      if (sBillType.equals(BillTypeConst.m_initIn)) {
        nc.bs.ic.ic101.GeneralHBO bo = new nc.bs.ic.ic101.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_initBorrow)) {
        nc.bs.ic.ic102.GeneralHBO bo = new nc.bs.ic.ic102.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_initLend)) {
        nc.bs.ic.ic103.GeneralHBO bo = new nc.bs.ic.ic103.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_initWaster)) {
        nc.bs.ic.ic104.GeneralHBO bo = new nc.bs.ic.ic104.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_initEntrustMachining)) {
        nc.bs.ic.ic106.GeneralHBO bo = new nc.bs.ic.ic106.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_purchaseIn)) {
        nc.bs.ic.ic201.GeneralHBO bo = new nc.bs.ic.ic201.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_productIn)) {
        nc.bs.ic.ic202.GeneralHBO bo = new nc.bs.ic.ic202.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_consignMachiningIn)) {
        nc.bs.ic.ic203.GeneralHBO bo = new nc.bs.ic.ic203.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_entrustMachiningIn)) {
        nc.bs.ic.ic204.GeneralHBO bo = new nc.bs.ic.ic204.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_borrowIn)) {
        nc.bs.ic.ic205.GeneralHBO bo = new nc.bs.ic.ic205.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_lendIn)) {
        nc.bs.ic.ic206.GeneralHBO bo = new nc.bs.ic.ic206.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_otherIn)) {
        nc.bs.ic.ic207.GeneralHBO bo = new nc.bs.ic.ic207.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_allocationIn)) {
        nc.bs.ic.ic209.GeneralHBO bo = new nc.bs.ic.ic209.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_wasterIn)) {
        nc.bs.ic.ic104.GeneralHBO bo = new nc.bs.ic.ic104.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_saleOut)) {
        nc.bs.ic.ic211.GeneralHBO bo = new nc.bs.ic.ic211.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_materialOut)) {
        nc.bs.ic.ic212.GeneralHBO bo = new nc.bs.ic.ic212.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_consignMachiningOut)) {
        nc.bs.ic.ic213.GeneralHBO bo = new nc.bs.ic.ic213.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_entrustMachiningOut)) {
        nc.bs.ic.ic214.GeneralHBO bo = new nc.bs.ic.ic214.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_lendOut)) {
        nc.bs.ic.ic215.GeneralHBO bo = new nc.bs.ic.ic215.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_borrowOut)) {
        nc.bs.ic.ic216.GeneralHBO bo = new nc.bs.ic.ic216.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_otherOut)) {
        nc.bs.ic.ic217.GeneralHBO bo = new nc.bs.ic.ic217.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_allocationOut)) {
        nc.bs.ic.ic218.GeneralHBO bo = new nc.bs.ic.ic218.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_discardOut)) {
        nc.bs.ic.ic241.GeneralHBO bo = new nc.bs.ic.ic241.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_wasterProcess)) {
        nc.bs.ic.ic242.GeneralHBO bo = new nc.bs.ic.ic242.GeneralHBO();
        al = bo.queryBills(voQC);
      } else if (sBillType.equals(BillTypeConst.m_spaceAdjust)) {
        nc.bs.ic.ic251.GeneralHBO bo = new nc.bs.ic.ic251.GeneralHBO();
        al = bo.queryBills(voQC);
      }

    }
    catch (Exception e)
    {
      throw GenMethod.handleException(null, e);
    }
    return al;
  }

  public Object queryInfo(Integer iSel, Object alQryCond)
  throws BusinessException
{
  try
  {
      GeneralBillBO bo = new GeneralBillBO();
      return bo.queryInfo(iSel, alQryCond);
  }
  catch(Exception e)
  {
      if(e instanceof BusinessException)
          throw (BusinessException)e;
      else
          throw new BusinessException("Caused by:", e);
  }
}

  public ArrayList queryJointCheckBills(QryConditionVO voQC)
    throws BusinessException
  {
    try
    {
      if ((voQC == null) || (voQC.getParam(0) == null))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000237"));
      ArrayList alParam = (ArrayList)voQC.getParam(0);
      if ((alParam == null) || (alParam.size() < 4)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000237"));
      }
      String sCorpID = (String)alParam.get(0);
      String sUserID = (String)alParam.get(1);
      String sBillTypeCode = (String)alParam.get(2);
      String sBillPK = (String)alParam.get(3);

      String sQryBillBO = "";

      ArrayList alBill = queryBills(sBillTypeCode, new QryConditionVO("head.cgeneralhid='" + sBillPK + "'"));

      if ((alBill == null) || (alBill.size() == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000238"));
      }
      BillTempletDMO btdmo = new BillTempletDMO();
      ArrayList alRet = new ArrayList();

      alRet.add(btdmo.findDefaultCardTempletData(sBillTypeCode, sCorpID, null, sUserID));

      if ((alBill != null) && (alBill.size() > 0))
        alRet.add(alBill.get(0));
      else {
        alRet.add(null);
      }

      String[] saScale = { "2", "2", "2", "2" };

      String[] saParam = { "BD501", "BD502", "BD503", "BD504" };
      saScale = new MiscDMO().getSysParam(sCorpID, saParam);

      alRet.add(saScale);
      return alRet;
    }
    catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }

  public ArrayList queryPartbySetInfo(String sSetInvID)
    throws BusinessException
  {
    try
    {
      return new MiscDMO().queryPartbySetInfo(sSetInvID);
    } catch (Exception e) {
      GenMethod.throwBusiException(e);
    }
    return null;
  }
}