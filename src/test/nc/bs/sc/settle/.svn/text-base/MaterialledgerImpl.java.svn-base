package nc.bs.sc.settle;

import java.util.Hashtable;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.para.SysInitBO;
import nc.bs.sc.pub.PublicDMO;
import nc.bs.uap.lock.PKLock;
import nc.itf.ic.service.IICPub_GeneralBillDMO;
import nc.itf.pu.inter.IPuToSc_EstimateBO;
import nc.itf.sc.IMateriallledger;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.sc.pub.ScConstants;
import nc.vo.sc.settle.BalanceVO;
import nc.vo.sc.settle.MaterialledgerVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;

public class MaterialledgerImpl
  implements IMateriallledger
{
  public MaterialledgerVO findByPrimaryKey(String key)
    throws BusinessException
  {
    MaterialledgerVO materialledger = null;
    try {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      materialledger = dmo.findByPrimaryKey(key);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return materialledger;
  }

  public String insert(MaterialledgerVO materialledger)
    throws BusinessException
  {
    try
    {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      String key = dmo.insert(materialledger);
      return key;
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return null;
  }

  public void delete(MaterialledgerVO vo)
    throws BusinessException
  {
    try
    {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      dmo.delete(vo);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
  }

  public void update(MaterialledgerVO materialledger)
    throws BusinessException
  {
    try
    {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      dmo.update(materialledger);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
  }

  public MaterialledgerVO[] queryAll(String unitCode)
    throws BusinessException
  {
    MaterialledgerVO[] materialledgers = null;
    try {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      materialledgers = dmo.queryAll(unitCode);
    }
    catch (Exception e) {
      PublicDMO.throwBusinessException(e);
    }
    return materialledgers;
  }

  public MaterialledgerVO[] queryByVO(MaterialledgerVO condMaterialledgerVO, Boolean isAnd)
    throws BusinessException
  {
    MaterialledgerVO[] materialledgers = null;
    try {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      materialledgers = dmo.queryByVO(condMaterialledgerVO, isAnd);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return materialledgers;
  }

  public GeneralBillItemVO[] findGeneralBody(String key)
    throws BusinessException
  {
    GeneralBillItemVO[] generalItemVOs = null;
    try
    {
      IICPub_GeneralBillDMO dmo = (IICPub_GeneralBillDMO)NCLocator.getInstance().lookup(IICPub_GeneralBillDMO.class.getName());
      generalItemVOs = (GeneralBillItemVO[])(GeneralBillItemVO[])dmo.queryItemDataByBillPk(key);

      if (generalItemVOs != null) {
        MaterialledgerDMO dmo1 = new MaterialledgerDMO();

        String[] ids = new String[generalItemVOs.length];
        for (int i = 0; i < generalItemVOs.length; i++) {
          ids[i] = generalItemVOs[i].getCgeneralbid();
        }
        Hashtable table = dmo1.queryGeneral_bb3s(ids);

        for (int i = 0; i < generalItemVOs.length; i++) {
          String cgeneralbid = generalItemVOs[i].getCgeneralbid();

          if (table.containsKey(cgeneralbid)) {
            Object[] returnObj = (Object[])(Object[])table.get(cgeneralbid);
            if (returnObj[0] == null)
              generalItemVOs[i].setNplannedprice(null);
            else
              generalItemVOs[i].setNplannedprice(new UFDouble("" + returnObj[0]));
            if (returnObj[1] == null)
              generalItemVOs[i].setNplannedmny(null);
            else
              generalItemVOs[i].setNplannedmny(new UFDouble("" + returnObj[1]));
          }
        }
      }
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return generalItemVOs;
  }

  public GeneralBillHeaderVO[] findGeneralHead(String whereStr)
    throws BusinessException
  {
    GeneralBillHeaderVO[] generalHeadVOs = null;
    try
    {
      IICPub_GeneralBillDMO dmo = (IICPub_GeneralBillDMO)NCLocator.getInstance().lookup(IICPub_GeneralBillDMO.class.getName());
      generalHeadVOs = (GeneralBillHeaderVO[])(GeneralBillHeaderVO[])dmo.queryAllHeadData(whereStr);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return generalHeadVOs;
  }

  public String[] getIc_bb3info(String[] cgeneralbid)
    throws BusinessException
  {
    String[] obj = null;
    try {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      obj = dmo.getIc_bb3info(cgeneralbid);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return obj;
  }

  public String getPk_measdoc(String cbaseid)
    throws BusinessException
  {
    String pk_measdoc = null;
    try {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      pk_measdoc = dmo.getPk_measdoc(cbaseid);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return pk_measdoc;
  }

  public UFDouble getProcessMny(String corder_bid)
    throws BusinessException
  {
    UFDouble nprice = null;
    try {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      nprice = dmo.getProcessMny(corder_bid);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return nprice;
  }

  public UFDouble[] getProcessMnys(String[] corder_bids)
    throws BusinessException
  {
    UFDouble[] nprice = null;
    try {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      nprice = dmo.getProcessMnys(corder_bids);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return nprice;
  }

  public String[] insertArray(MaterialledgerVO[] materialledgers, EstimateVO[] estimateVOs, String coperatorid, UFDate curDate)
    throws BusinessException
  {
    PKLock myService = null;
    boolean bLock = false;
    String[] sKey = null;
    String as[];
    try
    {
      Vector vTemp = new Vector();
      vTemp.addElement(materialledgers[0].getCbillid());
      vTemp.addElement(materialledgers[0].getCbill_bid());
      for (int i = 1; i < materialledgers.length; i++) {
        if (!vTemp.contains(materialledgers[i].getCbillid()))
          vTemp.addElement(materialledgers[i].getCbillid());
        if (!vTemp.contains(materialledgers[i].getCbill_bid()))
          vTemp.addElement(materialledgers[i].getCbill_bid());
      }
      sKey = new String[vTemp.size()];
      vTemp.copyInto(sKey);

      String[] keys = null;
      MaterialledgerDMO dmo = new MaterialledgerDMO();

      vTemp = dmo.queryGeneralBillStatus(materialledgers[0].getCbillid());
      int dr = ((Integer)vTemp.elementAt(0)).intValue();
      String cregister = (String)vTemp.elementAt(1);
      boolean bSettled = ((UFBoolean)vTemp.elementAt(2)).booleanValue();
      if (dr == 1)
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000072"));
      if ((cregister == null) || (cregister.trim().length() == 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000073"));
      if (bSettled) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000074"));
      }

      String newTs = dmo.queryGeneralBillhTs(materialledgers[0].getCbillid());
      if (!newTs.equals(materialledgers[0].getTs())) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000316"));
      }

      keys = dmo.insertArray(materialledgers);

      IPuToSc_EstimateBO esti = (IPuToSc_EstimateBO)NCLocator.getInstance().lookup(IPuToSc_EstimateBO.class.getName());
      esti.estimateForSc(estimateVOs, coperatorid, curDate);

      BalanceVO[] balanceVOs = trimToBalanceVO(materialledgers, true);

      BalanceDMO dmo2 = new BalanceDMO();
      dmo2.updateBalanceDatas(balanceVOs, new UFBoolean("N"));

      SysInitBO myService1 = new SysInitBO();
      String sCross = "";
      SysInitVO[] initVO = myService1.querySysInit(estimateVOs[0].getPk_corp(), "SC06");
      if ((initVO == null) || (initVO.length == 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000075"));
      sCross = initVO[0].getValue();
      as = keys;
      if ((sCross != null) && (sCross.trim().length() > 0) && (sCross.equals("·ñ"))) {
        for (int i = 0; i < balanceVOs.length; i++) {
          BalanceVO[] tempVO = dmo2.queryByVONormally(balanceVOs[i], new Boolean(true));
          if ((tempVO != null) && (tempVO.length > 0)) {
            for (int j = 0; j < tempVO.length; j++) {
              UFDouble nBalanceNum = tempVO[j].getNbalancenum();
              if ((nBalanceNum == null) || (nBalanceNum.doubleValue() >= 0.0D))
                continue;
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000076"));
            }
          }
        }

      }
      return as;
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      PublicDMO.throwBusinessException(e);
    }
    finally
    {
    }

    return null;
  }

  public Vector queryByVOs(MaterialledgerVO[] condMaterialledgerVOs, Boolean[] isAnds)
    throws BusinessException
  {
    Vector v = new Vector();
    try {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      v = dmo.queryByVOs(condMaterialledgerVOs, isAnds);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return v;
  }

  public BalanceVO queryPrice(BalanceVO condBalanceVO)
    throws BusinessException
  {
    BalanceVO balanceVO = null;
    try {
      BalanceDMO dmo = new BalanceDMO();
      balanceVO = dmo.queryPrice(condBalanceVO);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    return balanceVO;
  }

  public BalanceVO[] queryPriceBatch(BalanceVO[] condBalanceVO)
    throws BusinessException
  {
    Vector vTemp = new Vector();
    try {
      BalanceDMO dmo = new BalanceDMO();
      for (int i = 0; i < condBalanceVO.length; i++) {
        BalanceVO balanceVO = dmo.queryPrice(condBalanceVO[i]);
        vTemp.addElement(balanceVO);
      }
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
    if (vTemp.size() > 0) {
      BalanceVO[] balanceVO = new BalanceVO[vTemp.size()];
      vTemp.copyInto(balanceVO);
      return balanceVO;
    }
    return null;
  }

  public BalanceVO[] trimToBalanceVO(MaterialledgerVO[] itemVOs, boolean isModify)
    throws BusinessException
  {
    if (itemVOs == null) {
      return null;
    }
    int count = itemVOs.length;

    BalanceVO[] newItemVOs = new BalanceVO[count];

    for (int i = 0; i < count; i++)
    {
      BalanceVO oneVO = new BalanceVO();

      oneVO.setPk_corp(itemVOs[i].getPk_corp());
      oneVO.setCvendorid(itemVOs[i].getCvendorid());
      oneVO.setCvendormangid(itemVOs[i].getCvendormangid());
      oneVO.setCprocessbaseid(itemVOs[i].getCprocessbaseid());
      oneVO.setCprocessmangid(itemVOs[i].getCprocessmangid());
      oneVO.setCmaterialbaseid(itemVOs[i].getCmaterialbaseid());
      oneVO.setCmaterialmangid(itemVOs[i].getCmaterialmangid());

      double num = 0.0D;
      double money = 0.0D;

      if (itemVOs[i].getNnum() != null) {
        num = itemVOs[i].getNnum().doubleValue();
      }

      if (itemVOs[i].getNmny() != null) {
        money = itemVOs[i].getNmny().doubleValue();
      }

      if (isModify) {
        switch (itemVOs[i].getStatus()) {
        case 2:
          break;
        case 1:
          if (itemVOs[i].getNoldnum() != null)
            num -= itemVOs[i].getNoldnum().doubleValue();
          if (itemVOs[i].getNoldmoney() == null) break;
          money -= itemVOs[i].getNoldmoney().doubleValue(); break;
        case 3:
          if (itemVOs[i].getNoldnum() != null)
            num = 0.0D - itemVOs[i].getNoldnum().doubleValue();
          if (itemVOs[i].getNoldmoney() == null) break;
          money = 0.0D - itemVOs[i].getNoldmoney().doubleValue();
        }
      }

      oneVO.setNbalancenum(new UFDouble(num));
      oneVO.setNbalancemny(new UFDouble(money));

      oneVO.setVfree1(itemVOs[i].getVfree1());
      oneVO.setVfree2(itemVOs[i].getVfree2());
      oneVO.setVfree3(itemVOs[i].getVfree3());
      oneVO.setVfree4(itemVOs[i].getVfree4());
      oneVO.setVfree5(itemVOs[i].getVfree5());
      oneVO.setVbatch(itemVOs[i].getVbatch());

      newItemVOs[i] = oneVO;
    }

    return newItemVOs;
  }

  public void updateArray(MaterialledgerVO[] materialledgers, EstimateVO[] estimateVOs, int status, String coperatorid, UFDate curDate)
    throws BusinessException
  {
    PKLock myService = null;
    boolean bLock = false;
    String[] sKey = null;
    try
    {
      Vector vTemp = new Vector();
      vTemp.addElement(materialledgers[0].getCbillid());
      vTemp.addElement(materialledgers[0].getCbill_bid());
      for (int i = 1; i < materialledgers.length; i++) {
        if (!vTemp.contains(materialledgers[i].getCbillid()))
          vTemp.addElement(materialledgers[i].getCbillid());
        if (!vTemp.contains(materialledgers[i].getCbill_bid()))
          vTemp.addElement(materialledgers[i].getCbill_bid());
      }
      sKey = new String[vTemp.size()];
      vTemp.copyInto(sKey);

      MaterialledgerDMO dmo = new MaterialledgerDMO();

      vTemp = dmo.queryGeneralBillStatus(materialledgers[0].getCbillid());
      int dr = ((Integer)vTemp.elementAt(0)).intValue();
      String cregister = (String)vTemp.elementAt(1);
      boolean bSettled = ((UFBoolean)vTemp.elementAt(2)).booleanValue();
      if (dr == 1)
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000072"));
      if ((cregister == null) || (cregister.trim().length() == 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000073"));
      if (bSettled) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40120001", "UPP40120001-000074"));
      }
      dmo.updateArray(materialledgers);

      IPuToSc_EstimateBO esti = (IPuToSc_EstimateBO)NCLocator.getInstance().lookup(IPuToSc_EstimateBO.class.getName());
      esti.antiEstimateForSc(estimateVOs, coperatorid);

      if ((status != ScConstants.STATE_UNVERIFY) || 
        (status == ScConstants.STATE_MODIFY))
      {
        esti.estimateForSc(estimateVOs, coperatorid, curDate);
      }

      BalanceVO[] balanceVOs = trimToBalanceVO(materialledgers, true);

      BalanceDMO dmo2 = new BalanceDMO();
      dmo2.updateBalanceDatas(balanceVOs, new UFBoolean("N"));
    }
    catch (Exception e) {
      SCMEnv.out(e);

      PublicDMO.throwBusinessException(e);
    }
    finally
    {
    }
  }

  public void updateArrayByAdjust(MaterialledgerVO[] materialledgers)
    throws BusinessException
  {
    try
    {
      MaterialledgerDMO dmo = new MaterialledgerDMO();
      dmo.updateArrayByAdjust(materialledgers);
    }
    catch (Exception e)
    {
      PublicDMO.throwBusinessException(e);
    }
  }

  public MaterialledgerVO[] queryAll(String strWhereSql, ClientLink cl)
    throws BusinessException
  {
    return null;
  }

  public void updateBalanceAfterModify(MaterialledgerVO[] itemVOs, boolean isModify)
    throws BusinessException
  {
  }

  public void updateBalanceBeforeModify(MaterialledgerVO[] itemVOs, boolean isModify)
    throws BusinessException
  {
  }

  public void updateItems(MaterialledgerVO[] materialledgers, String coperatorid)
    throws BusinessException
  {
  }
}