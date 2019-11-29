package nc.bs.ic.pub.bill;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.bd.b48.StorVSCostBO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ic.pub.PriceDMO;
import nc.bs.ic.pub.QueryInfoDMO;
import nc.bs.ic.pub.bc.BarCodeDMO;
import nc.bs.ic.pub.check.CheckBarcodeDMO;
import nc.bs.ic.pub.check.CheckBusiDMO;
import nc.bs.ic.pub.check.CheckDMO;
import nc.bs.ic.pub.check.CheckInvVendorDMO;
import nc.bs.ic.pub.locator.LocatorDMO;
import nc.bs.ic.pub.settlement.SettlementDMO;
import nc.bs.ic.pub.sn.SNDMO;
import nc.bs.ic.pub.vmi.ICSmartToolsDmo;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.sm.createcorp.CreatecorpBO;
import nc.impl.ic.ic001.BatchcodeImpl;
import nc.impl.ic.ic003.StorectlDMO;
import nc.itf.ic.service.IICPub_GeneralBillBO;
import nc.itf.uap.busibean.ISysInitQry;
import nc.jdbc.framework.generator.SystemTsGenerator;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.sn.SerialVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.pub.SCMEnv;

/** @deprecated */
public class GeneralBillBO
  implements IICPub_GeneralBillBO
{
  private final int SN_STA_ORG = 0; private final int SN_STA_NOW = 1;

  protected ArrayList cancelSignBill(GeneralBillVO voAuditBill)
    throws BusinessException
  {
    try
    {
      if (voAuditBill == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000162"));
      }

      GeneralBillHeaderVO voHead = (GeneralBillHeaderVO)voAuditBill.getParentVO();
      if (voHead == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000162"));
      }

      ArrayList alRes = new ArrayList();

      if (voHead.getDaccountdate() == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000163"));
      }

      GeneralBillDMO dmoBill = getBillDMO();

      alRes.add(dmoBill.cancelSign(voAuditBill));

      freshTs(voAuditBill, voAuditBill.getPrimaryKey());

      return alRes;
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  protected ArrayList cancelSignBills(ArrayList alParam)
    throws BusinessException
  {
    try
    {
      if ((alParam != null) && (alParam.size() > 0)) {
        ArrayList alRet = new ArrayList();
        GeneralBillDMO dmo = getBillDMO();
        for (int i = 0; i < alParam.size(); ++i)
          if (alParam.get(i) != null)
            alRet.add(dmo.cancelSign((ArrayList)alParam.get(i)));
          else
            alRet.add(null);
        return alRet;
      }
      return null;
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  protected void deleteBarCode(GeneralBillItemVO[] voaItem, boolean bSaveBarcodeFinal)
    throws Exception
  {
    if ((voaItem == null) || (voaItem.length == 0)) {
      return;
    }
    if (!bSaveBarcodeFinal)
      return;
    ArrayList alBarCode = new ArrayList();
    for (int i = 0; i < voaItem.length; ++i) {
      GeneralBillItemVO itemvo = voaItem[i];
      if ((itemvo.getBarCodeVOs() != null) && (itemvo.getBarCodeVOs().length > 0)) {
        for (int j = 0; j < itemvo.getBarCodeVOs().length; ++j) {
          if ((itemvo.getBarCodeVOs()[j] == null) || ((itemvo.getBarCodeVOs()[j].getStatus() != 3) && (itemvo.getBarCodeVOs()[j].getStatus() != 1))) {
            continue;
          }
          alBarCode.add(itemvo.getBarCodeVOs()[j]);
        }
      }

    }

    if (alBarCode.size() > 0) {
      BarCodeDMO dmoBill = new BarCodeDMO();
      dmoBill.deleteArray(alBarCode);
    }
  }

  protected void deleteBarCode(GeneralBillVO voNewBill)
    throws Exception
  {
    if (voNewBill == null)
      return;
    GeneralBillItemVO[] voaItem = (GeneralBillItemVO[])(GeneralBillItemVO[])voNewBill.getChildrenVO();
    if ((voaItem == null) || (voaItem.length == 0)) {
      return;
    }
    if (!voNewBill.bSaveBarcodeFinal())
      return;
    ArrayList alBarCode = new ArrayList();
    for (int i = 0; i < voaItem.length; ++i) {
      GeneralBillItemVO itemvo = voaItem[i];
      if ((itemvo.getBarCodeVOs() != null) && (itemvo.getBarCodeVOs().length > 0)) {
        for (int j = 0; j < itemvo.getBarCodeVOs().length; ++j) {
          if ((itemvo.getBarCodeVOs()[j] == null) || ((itemvo.getBarCodeVOs()[j].getStatus() != 3) && (itemvo.getBarCodeVOs()[j].getStatus() != 1))) {
            continue;
          }
          alBarCode.add(itemvo.getBarCodeVOs()[j]);
        }
      }

    }

    if (alBarCode.size() > 0) {
      BarCodeDMO dmoBill = new BarCodeDMO();
      dmoBill.deleteArray(alBarCode);
    }
  }

  protected void deleteBarCodeAllBill(GeneralBillVO voNewBill)
    throws Exception
  {
    if (voNewBill == null)
      return;
    GeneralBillItemVO[] voaItem = (GeneralBillItemVO[])(GeneralBillItemVO[])voNewBill.getChildrenVO();
    if ((voaItem == null) || (voaItem.length == 0))
      return;
    deleteBarcodeBillItems(voaItem);
  }

  protected void deleteBarcodeBillItems(GeneralBillItemVO[] voaItem)
    throws Exception
  {
    if ((voaItem == null) || (voaItem.length == 0))
      return;
    ArrayList alBarCodeBillItem = new ArrayList();
    GeneralBillItemVO itemvo = null;
    for (int i = 0; i < voaItem.length; ++i) {
      itemvo = voaItem[i];
      if ((itemvo.getBarCodeVOs() != null) && (itemvo.getBarCodeVOs().length > 0)) {
        alBarCodeBillItem.add(itemvo.getCgeneralbid());
      }
    }
    if (alBarCodeBillItem.size() > 0) {
      BarCodeDMO dmoBill = new BarCodeDMO();
      dmoBill.deleteBarcodeByBillItemIDs(alBarCodeBillItem);
    }
  }

  protected void deleteBill(GeneralBillVO voDeleteBill)
    throws BusinessException
  {
    try
    {
      if (voDeleteBill == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000162"));
      }
      GeneralBillHeaderVO voHead = (GeneralBillHeaderVO)voDeleteBill.getParentVO();
      if (voHead == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000162"));
      }
      try
      {
        CheckDMO dmo = new CheckDMO();
        if (dmo.isBillSign(voDeleteBill)) {
          String billcode = (String)voDeleteBill.getHeaderValue("vbillcode");
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000505") + ":" + billcode);
        }
      } catch (Exception e1) {
        SCMEnv.error(e1);
        throw new BusinessException(e1.getMessage());
      }

      voHead.setStatus(3);
      GeneralBillItemVO[] voaBillItemTemp = voDeleteBill.getItemVOs();

      for (int item = 0; item < voaBillItemTemp.length; ++item) {
        voaBillItemTemp[item].setStatus(3);
      }

      deleteThisBill(voDeleteBill);

      new CheckBarcodeDMO().checkBCOnhandAndRepeat(voDeleteBill);
    }
    catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }

  public void deleteThisBill(GeneralBillVO voDeletedBill)
    throws BusinessException
  {
    try
    {
      if ((voDeletedBill == null) || (voDeletedBill.getHeaderVO() == null) || (voDeletedBill.getItemVOs() == null) || (voDeletedBill.getItemVOs().length == 0))
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000164"));
      }

      GeneralBillHeaderVO voHeader = voDeletedBill.getHeaderVO();
      voDeletedBill.setStatus(3);
      String sBillPK = voHeader.getPrimaryKey();
      GeneralBillItemVO[] voaItem = voDeletedBill.getItemVOs();

      deleteBarCodeAllBill(voDeletedBill);

      String sCorpID = voHeader.getPk_corp();

      GeneralBillDMO dmoBill = getBillDMO();

      Timer t = new Timer();
      t.start();

      t.stopAndShow("@@--qry rel");

      LocatorDMO dmoLoc = new LocatorDMO();

      ArrayList alLocsn = getBillDMO().getLocSNInfo(sBillPK);
      if ((alLocsn != null) && (alLocsn.size() > 0)) {
        voDeletedBill.setLocators((ArrayList)alLocsn.get(0));
      }
      if ((alLocsn != null) && (alLocsn.size() > 1)) {
        voDeletedBill.setSNs((ArrayList)alLocsn.get(1));
      }

      SettlementDMO dmoStl = new SettlementDMO();

      SerialVO[] voasn = null;

      String sItemPK = null;
      t.start();
      for (int row = 0; row < voaItem.length; ++row) {
        if (voaItem[row] == null)
          continue;
        voaItem[row].setStatus(3);
      }

      String sMsg = snDelete(voDeletedBill);
      if ((sMsg != null) && (sMsg.length() > 0)) {
        throw new BusinessException(sMsg);
      }
      t.stopAndShow("@@--sn -- del ");

      t.start();

      dmoLoc.deleteLocatorsForCopyByBillPK(sBillPK);

      dmoStl.deleteBB3ForCopyByBillPK(sBillPK);

      dmoBill.deleteItemsForHeader(sBillPK);

      dmoBill.deleteHeader(sBillPK);
      t.stopAndShow("@@--del bill");

      t.start();
      synchronized (this) {
        modifyOnhandNum(null, voDeletedBill);
      }
      t.stopAndShow("@@--del num");
    }
    catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
  }

  public void freshTs(GeneralBillVO voBill, String sBillPK)
    throws BusinessException
  {
    String sBillStatus = null;
    if ((voBill == null) || (voBill.getParentVO() == null) || (voBill.getChildrenVO() == null))
    {
      return;
    }
    ArrayList alTs = null;
    try {
      GeneralBillDMO dmo = getBillDMO();
      alTs = dmo.getTs(sBillPK);
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }

    if ((alTs == null) || (alTs.size() == 0) || (alTs.get(0) == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000165"));
    }

    GeneralBillHeaderVO voHead = (GeneralBillHeaderVO)voBill.getParentVO();
    GeneralBillItemVO[] voItems = (GeneralBillItemVO[])(GeneralBillItemVO[])voBill.getChildrenVO();
    voHead.setTs((String)alTs.get(0));
    ArrayList alItemTs = null;
    Hashtable ht = new Hashtable();
    for (int i = 1; i < alTs.size(); ++i) {
      alItemTs = (ArrayList)alTs.get(i);
      if ((alItemTs == null) || (alItemTs.size() <= 1) || 
        (alItemTs.get(0) == null) || (alItemTs.get(1) == null)) continue;
      ht.put((String)alItemTs.get(0), alItemTs);
    }

    String bid = null;
    UFBoolean ufBFalse = new UFBoolean(false);
    int xxx;
    for (int i = 0; i < voItems.length; ++i) {
      bid = voItems[i].getCgeneralbid();

      if (ht.containsKey(bid)) {
        alItemTs = (ArrayList)ht.get(bid);
        if ((alItemTs != null) && (alItemTs.size() > 7)) {
          voItems[i].setTs((String)alItemTs.get(1));
          voItems[i].setVfirstbillcode((String)alItemTs.get(4));

          if (alItemTs.get(6) != null) {
            voItems[i].setListnbarcodenum((UFDouble)alItemTs.get(6));
          }
          if ((alItemTs.get(7) == null) || (alItemTs.get(7).toString().trim().length() == 0))
          {
            voItems[i].setBarcodeClose(ufBFalse);
          }
          else voItems[i].setBarcodeClose((UFBoolean)alItemTs.get(7));

        }
        else
        {
          xxx = 0;
        }
      }
    }
  }

  private void freshTsBodyItem(ArrayList alParam)
    throws BusinessException
  {
    String sHeadPK = (String)alParam.get(0);
    ArrayList alBids = (ArrayList)alParam.get(1);
    ArrayList alTss = (ArrayList)alParam.get(2);

    ArrayList alTs = null;
    try {
      GeneralBillDMO dmo = getBillDMO();
      alTs = dmo.getTs(sHeadPK);
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }

    if ((alTs == null) || (alTs.get(0) == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000165"));
    }

    ArrayList alItemTs = null;
    Hashtable ht = new Hashtable();
    for (int i = 1; i < alTs.size(); ++i) {
      alItemTs = (ArrayList)alTs.get(i);
      if ((alItemTs == null) || (alItemTs.size() <= 1) || 
        (alItemTs.get(0) == null) || (alItemTs.get(1) == null)) continue;
      ht.put((String)alItemTs.get(0), alItemTs);
    }

    String bid = null;
    for (int i = 0; i < alBids.size(); ++i) {
      bid = (String)alBids.get(i);
      if (ht.containsKey(bid)) {
        alItemTs = (ArrayList)ht.get(bid);
        if ((alItemTs != null) && (alItemTs.size() > 4))
          alTss.set(i, (String)alItemTs.get(1));
      }
    }
  }

  protected String[] getAllPK(GeneralBillVO voBill)
    throws Exception
  {
    if ((voBill == null) || (voBill.getHeaderVO() == null))
    {
      return null;
    }
    Vector vAllPK = new Vector();

    String sMyPK = voBill.getHeaderVO().getPrimaryKey();
    if ((sMyPK != null) && (sMyPK.trim().length() > 0))
      vAllPK.addElement(sMyPK);
    GeneralBillItemVO[] voaItem = voBill.getItemVOs();
    int iLineNum = 0;
    if (voaItem != null) {
      iLineNum = voaItem.length;
    }

    String sSourcePK = null;
    String sCorrPK = null;
    for (int row = 0; row < iLineNum; ++row)
    {
      sSourcePK = voaItem[row].getCsourcebillhid();
      if ((sSourcePK != null) && (sSourcePK.trim().length() > 0) && (!vAllPK.contains(sSourcePK)))
      {
        vAllPK.addElement(sSourcePK);
      }

      sCorrPK = voaItem[row].getCcorrespondhid();
      if ((sCorrPK == null) || (sCorrPK.trim().length() <= 0) || (vAllPK.contains(sCorrPK))) {
        continue;
      }
      vAllPK.addElement(sCorrPK);
    }

    if ((sMyPK != null) && (sMyPK.trim().length() > 0)) {
      try {
        GeneralBillDMO dmo = getBillDMO();
        String[] saChildrenPK = null;

        if (saChildrenPK != null)
          for (int u = 0; u < saChildrenPK.length; ++u) {
            if ((saChildrenPK[u] == null) || (saChildrenPK[u].trim().length() <= 0) || (vAllPK.contains(saChildrenPK[u]))) {
              continue;
            }
            vAllPK.addElement(saChildrenPK[u]);
          }
      }
      catch (Exception e) {
        throw e;
      }

    }

    String[] saRet = null;
    if (vAllPK.size() > 0) {
      saRet = new String[vAllPK.size()];
      vAllPK.copyInto(saRet);
    }

    return saRet;
  }

  public ArrayList getBarcodeUPdateItem(GeneralBillItemVO[] voaItem, boolean onlyUnChange)
  {
    ArrayList alUpdateItem = new ArrayList();
    GeneralBillItemVO itemvo = null;
    for (int i = 0; i < voaItem.length; ++i) {
      itemvo = voaItem[i];
      if (itemvo == null)
        continue;
      if (((itemvo.getStatus() != 0) && (((onlyUnChange) || (itemvo.getStatus() != 1)))) || 
        (itemvo.getBarCodeVOs() == null) || (itemvo.getBarCodeVOs().length <= 0)) continue;
      for (int j = 0; j < itemvo.getBarCodeVOs().length; ++j) {
        if ((itemvo.getBarCodeVOs()[j] == null) || ((itemvo.getBarCodeVOs()[j].getStatus() != 3) && (itemvo.getBarCodeVOs()[j].getStatus() != 1) && (itemvo.getBarCodeVOs()[j].getStatus() != 2))) {
          continue;
        }

        alUpdateItem.add(itemvo);
        break;
      }

    }

    return alUpdateItem;
  }

  protected GeneralBillDMO getBillDMO()
    throws BusinessException
  {
    try
    {
      return new GeneralBillDMO();
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  protected ArrayList getBillItemAryID(ArrayList aryBillItem)
  {
    ArrayList aryBillItemID = null;
    if (aryBillItem != null)
    {
      aryBillItemID = new ArrayList();
      String sID = null;
      Object objTemp = null;
      for (int i = 0; i < aryBillItem.size(); ++i)
      {
        objTemp = aryBillItem.get(i);
        if (objTemp == null)
          continue;
        sID = ((GeneralBillItemVO)objTemp).getCgeneralbid();
        if (sID != null) {
          aryBillItemID.add(sID);
        }
      }
    }

    return aryBillItemID;
  }

  protected String getSnHintMsg(ArrayList arrylistEx, ArrayList aryListNotEx, ArrayList aryListReturn, ArrayList aryListOutOrFreeze)
  {
    StringBuffer sbMsg = new StringBuffer();

    String sSn = null;
    int iCount = 0;

    if (arrylistEx != null)
    {
      iCount = arrylistEx.size();
      if (iCount > 0) {
        sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000167"));
        for (int sn = 0; sn < iCount; ++sn) {
          sSn = (String)arrylistEx.get(sn);
          if (sSn != null)
          {
            sbMsg.append(sSn);
          }if (sn != iCount - 1)
            sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPPSCMCommon-000000"));
          else {
            sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000168"));
          }
        }
      }
    }

    if (aryListNotEx != null)
    {
      iCount = aryListNotEx.size();
      if (iCount > 0) {
        sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000345"));
        for (int sn = 0; sn < iCount; ++sn) {
          sSn = (String)aryListNotEx.get(sn);
          if (sSn != null)
            sbMsg.append(sSn);
          if (sn != iCount - 1)
            sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPPSCMCommon-000000"));
          else {
            sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000168"));
          }
        }
      }
    }
    if (aryListReturn != null)
    {
      iCount = aryListReturn.size();
      if (iCount > 0) {
        sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000169"));
        for (int sn = 0; sn < iCount; ++sn) {
          sSn = (String)aryListReturn.get(sn);
          if (sSn != null)
          {
            sbMsg.append(sSn);
          }if (sn != iCount - 1)
            sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPPSCMCommon-000000"));
          else {
            sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000168"));
          }
        }
      }
    }
    if (aryListOutOrFreeze != null)
    {
      iCount = aryListOutOrFreeze.size();
      if (iCount > 0) {
        sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000170"));
        for (int sn = 0; sn < iCount; ++sn) {
          sSn = (String)aryListOutOrFreeze.get(sn);
          if (sSn != null)
          {
            sbMsg.append(sSn);
          }if (sn != iCount - 1)
            sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPPSCMCommon-000000"));
          else
            sbMsg.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000168"));
        }
      }
    }
    return sbMsg.toString();
  }

  private ArrayList getSnStatues(GeneralBillVO voUpdate, GeneralBillVO voOld, Hashtable htOldItems)
    throws BusinessException
  {
    ArrayList arylistResult = new ArrayList();

    boolean isOneOpeter = false;
    boolean isInTrue = false;
    boolean isSnStatuese = false;
    ArrayList aryBillItemAddIn = new ArrayList();
    ArrayList aryBillItemAddOut = new ArrayList();
    ArrayList aryBillItemDelIn = new ArrayList();
    ArrayList aryBillItemDelOut = new ArrayList();
    ArrayList aryBillItemUpdateIn = new ArrayList();
    ArrayList aryBillItemUpdateOut = new ArrayList();

    long lTime = System.currentTimeMillis();

    if ((voUpdate != null) && (htOldItems == null))
    {
      GeneralBillItemVO[] billItemUpdate = (GeneralBillItemVO[])(GeneralBillItemVO[])voUpdate.getChildrenVO();

      if ((billItemUpdate != null) && (billItemUpdate.length > 0))
      {
        int[] iNewInOut = voUpdate.getBillItemInOutPty();
        int iLen = billItemUpdate.length;
        for (int i = 0; i < iLen; ++i) {
          if (billItemUpdate[i].getSerial() != null) {
            if (iNewInOut[i] == 1) {
              aryBillItemAddIn.add(billItemUpdate[i]);
            }
            else if (iNewInOut[i] == -1) {
              aryBillItemAddOut.add(billItemUpdate[i]);
            }
          }
        }

        if ((aryBillItemAddIn.size() > 0) && (aryBillItemAddOut.size() == 0)) {
          isOneOpeter = true;
          isInTrue = true;
        }
        if ((aryBillItemAddOut.size() > 0) && (aryBillItemAddIn.size() == 0)) {
          isOneOpeter = true;
          isInTrue = false;
        }
      }

    }
    else if ((voUpdate == null) && (voOld != null))
    {
      GeneralBillItemVO[] billItemOld = (GeneralBillItemVO[])(GeneralBillItemVO[])voOld.getChildrenVO();
      if ((billItemOld != null) && (billItemOld.length > 0))
      {
        int[] iNewInOut = voOld.getBillItemInOutPty();
        int iLen = billItemOld.length;
        Integer isSerialMgt = null;
        for (int i = 0; i < iLen; ++i)
        {
          isSerialMgt = billItemOld[i].getInv().getIsSerialMgt();
          if ((((isSerialMgt == null) || (isSerialMgt.intValue() != 1))) && (((billItemOld[i].getSerial() == null) || (billItemOld[i].getSerial().length <= 0)))) {
            continue;
          }

          if (iNewInOut[i] == 1) {
            aryBillItemDelIn.add(billItemOld[i]);
          }
          else if (iNewInOut[i] == -1) {
            aryBillItemDelOut.add(billItemOld[i]);
          }

        }

        if ((aryBillItemDelIn.size() > 0) && (aryBillItemDelOut.size() == 0)) {
          isOneOpeter = true;
          isInTrue = true;
        }
        if ((aryBillItemDelOut.size() > 0) && (aryBillItemDelIn.size() == 0)) {
          isOneOpeter = true;
          isInTrue = false;
        }
      }
    }
    else {
      if ((voUpdate == null) && (htOldItems == null))
      {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000055"));
      }
      if ((voUpdate != null) && (htOldItems != null))
      {
        GeneralBillItemVO[] billItemUpdate = (GeneralBillItemVO[])(GeneralBillItemVO[])voUpdate.getChildrenVO();

        int[] iNewInOut = voUpdate.getBillItemInOutPty();
        int[] iNewInOutOld = voOld.getBillItemInOutPty();
        int iLen = billItemUpdate.length;
        Integer isSerialMgt = null;
        Integer isSerialMgtOld = null;

        String sInvIDNew = null; String sInvIDOld = null;
        boolean isHasUnchange = false;
        boolean isSameInv = false;
        boolean bOldSerialMgt = false;
        boolean bUpdateSerialMgt = false;
        String sCgeneralbid = null;

        for (int i = 0; i < iLen; ++i) {
          int iNewStatus = billItemUpdate[i].getStatus();
          int iTempSnStatus = billItemUpdate[i].getSnStatus();

          sCgeneralbid = billItemUpdate[i].getCgeneralbid();
          GeneralBillItemVO billItemVoOld = null;

          if (htOldItems.containsKey(sCgeneralbid)) {
            billItemVoOld = (GeneralBillItemVO)htOldItems.get(sCgeneralbid);

            if ((!nc.vo.ic.pub.GenMethod.isStringEqual(billItemUpdate[i].getCvendorid(), billItemVoOld.getCvendorid())) && (billItemUpdate[i].getSnStatus() == 0)) {
              billItemUpdate[i].setSnStatus(1);
            }
            iTempSnStatus = billItemUpdate[i].getSnStatus();

            if (billItemVoOld != null) {
              sInvIDOld = billItemVoOld.getInv().getCinventoryid();
              isSerialMgtOld = billItemVoOld.getInv().getIsSerialMgt();
              sInvIDNew = billItemUpdate[i].getInv().getCinventoryid();
              if ((sInvIDOld != null) && (!sInvIDOld.equalsIgnoreCase(sInvIDNew))) {
                isSameInv = false;
              }
              else {
                isSameInv = true;
              }
              if ((isSerialMgtOld != null) && (isSerialMgtOld.intValue() == 1))
                bOldSerialMgt = true;
              else
                bOldSerialMgt = false;
            }
          } else {
            bOldSerialMgt = false;
            isSameInv = false;
          }

          isSerialMgt = billItemUpdate[i].getInv().getIsSerialMgt();
          if ((isSerialMgt != null) && (isSerialMgt.intValue() == 1))
            bUpdateSerialMgt = true;
          else {
            bUpdateSerialMgt = false;
          }
          switch (iNewStatus)
          {
          case 2:
            if (bUpdateSerialMgt)
              if (iNewInOut[i] == 1) {
                aryBillItemAddIn.add(billItemUpdate[i]);
              }
              else if (iNewInOut[i] == -1)
                aryBillItemAddOut.add(billItemUpdate[i]); 
            break;
          case 3:
            if (bUpdateSerialMgt)
              if (iNewInOut[i] == 1) {
                aryBillItemDelIn.add(billItemUpdate[i]);
              }
              else if (iNewInOut[i] == -1)
                aryBillItemDelOut.add(billItemUpdate[i]); 
            break;
          case 0:
            if (iTempSnStatus == 0)
              isHasUnchange = true;
            break;
          case 1:
            if (!isSameInv)
            {
              if (bOldSerialMgt)
              {
                if (iNewInOutOld[i] == 1)
                {
                  if (billItemVoOld != null) {
                    aryBillItemDelIn.add(billItemVoOld);
                  }
                }
                else if ((iNewInOutOld[i] == -1) && 
                  (billItemVoOld != null)) {
                  aryBillItemDelOut.add(billItemVoOld);
                }
              }

              if (bUpdateSerialMgt) {
                if (iNewInOut[i] == 1) {
                  aryBillItemAddIn.add(billItemUpdate[i]);
                }
                else if (iNewInOut[i] == -1)
                  aryBillItemAddOut.add(billItemUpdate[i]);
              }
            }
            else
            {
              if (!bUpdateSerialMgt) {
                continue;
              }

              int iOldInOut = billItemVoOld.getInOutFlag();
              if (iTempSnStatus != 0)
              {
                if (iNewInOut[i] == 1)
                  if (iTempSnStatus == 1) {
                    if (iOldInOut == -1)
                    {
                      aryBillItemDelOut.add(billItemVoOld);
                      aryBillItemAddIn.add(billItemUpdate[i]);
                    }
                    else
                    {
                      aryBillItemUpdateIn.add(billItemUpdate[i]);
                    }

                  }
                  else if (iTempSnStatus == 3) {
                    aryBillItemDelIn.add(billItemUpdate[i]);
                  }
                  else if (iTempSnStatus == 2)
                    aryBillItemAddIn.add(billItemUpdate[i]);
                else if (iNewInOut[i] == -1) {
                  if (iTempSnStatus == 1) {
                    if (iOldInOut == -1)
                    {
                      aryBillItemUpdateOut.add(billItemUpdate[i]);
                    }
                    else
                    {
                      aryBillItemDelIn.add(billItemVoOld);

                      aryBillItemAddOut.add(billItemUpdate[i]);
                    }

                  }
                  else if (iTempSnStatus == 3) {
                    aryBillItemDelOut.add(billItemUpdate[i]);
                  }
                  else if (iTempSnStatus == 2) {
                    aryBillItemAddOut.add(billItemUpdate[i]);
                  }

                }
                else if (iOldInOut == -1)
                {
                  aryBillItemDelOut.add(billItemVoOld);
                }
                else
                {
                  aryBillItemDelIn.add(billItemVoOld);
                }

              }
              else {
                isHasUnchange = true;
              }

            }

          }

        }

        if ((!isHasUnchange) && (aryBillItemDelIn.size() > 0) && (aryBillItemDelOut.size() == 0) && (aryBillItemUpdateIn.size() == 0) && (aryBillItemUpdateOut.size() == 0) && (aryBillItemAddIn.size() == 0) && (aryBillItemAddOut.size() == 0))
        {
          isOneOpeter = true;
          isInTrue = true;
        }

        if ((!isHasUnchange) && (aryBillItemDelOut.size() > 0) && (aryBillItemDelIn.size() == 0) && (aryBillItemUpdateIn.size() == 0) && (aryBillItemUpdateOut.size() == 0) && (aryBillItemAddIn.size() == 0) && (aryBillItemAddOut.size() == 0))
        {
          isOneOpeter = true;
          isInTrue = false;
        }

        if ((!isHasUnchange) && (aryBillItemUpdateIn.size() > 0) && (aryBillItemDelIn.size() == 0) && (aryBillItemDelOut.size() == 0) && (aryBillItemUpdateOut.size() == 0) && (aryBillItemAddIn.size() == 0) && (aryBillItemAddOut.size() == 0))
        {
          isOneOpeter = true;
          isInTrue = true;
        }

        if ((!isHasUnchange) && (aryBillItemUpdateOut.size() > 0) && (aryBillItemDelIn.size() == 0) && (aryBillItemDelOut.size() == 0) && (aryBillItemUpdateIn.size() == 0) && (aryBillItemAddIn.size() == 0) && (aryBillItemAddOut.size() == 0))
        {
          isOneOpeter = true;
          isInTrue = false;
        }

        if ((!isHasUnchange) && (aryBillItemUpdateOut.size() == 0) && (aryBillItemDelIn.size() == 0) && (aryBillItemDelOut.size() == 0) && (aryBillItemUpdateIn.size() == 0) && (aryBillItemAddIn.size() > 0) && (aryBillItemAddOut.size() == 0))
        {
          isOneOpeter = true;
          isInTrue = true;
        }

        if ((!isHasUnchange) && (aryBillItemUpdateOut.size() == 0) && (aryBillItemDelIn.size() == 0) && (aryBillItemDelOut.size() == 0) && (aryBillItemUpdateIn.size() == 0) && (aryBillItemAddIn.size() == 0) && (aryBillItemAddOut.size() > 0))
        {
          isOneOpeter = true;
          isInTrue = false;
        }
      }
    }

    if (SCMEnv.DEBUG) {
      if (isOneOpeter)
        SCMEnv.out("sndmo:整单操作");
      else {
        SCMEnv.out("sndmo:非整单操作");
      }
      if (isInTrue)
        SCMEnv.out("sndmo:入库操作");
      else
        SCMEnv.out("sndmo:出库操作");
    }
    arylistResult.add(new Boolean(isOneOpeter));
    arylistResult.add(new Boolean(isInTrue));
    arylistResult.add(aryBillItemAddIn);
    arylistResult.add(aryBillItemAddOut);
    arylistResult.add(aryBillItemDelIn);
    arylistResult.add(aryBillItemDelOut);
    arylistResult.add(aryBillItemUpdateIn);
    arylistResult.add(aryBillItemUpdateOut);

    if ((aryBillItemAddIn.size() > 0) || (aryBillItemAddOut.size() > 0) || (aryBillItemDelIn.size() > 0) || (aryBillItemDelOut.size() > 0) || (aryBillItemUpdateIn.size() > 0) || (aryBillItemUpdateOut.size() > 0))
    {
      isSnStatuese = true;
    }
    else isSnStatuese = false;

    arylistResult.add(new Boolean(isSnStatuese));
    SCMEnv.showTime(lTime, "getSnStatues:");

    return arylistResult;
  }

  protected String[] getSysParam(String sCorpID, String[] saParam)
    throws BusinessException
  {
    try
    {
      MiscDMO dmo = new MiscDMO();
      return dmo.getSysParam(sCorpID, saParam);
    } catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000346") + e.getMessage());
    }
  }

  protected void insertBarCode(GeneralBillItemVO[] voaItem, boolean bSaveBarcodeFinal)
    throws Exception
  {
    if ((voaItem == null) || (voaItem.length == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000171"));
    }

    if (bSaveBarcodeFinal) {
      ArrayList alBarCode = new ArrayList();
      for (int i = 0; i < voaItem.length; ++i) {
        GeneralBillItemVO itemvo = voaItem[i];
        if ((itemvo.getBarCodeVOs() != null) && (itemvo.getBarCodeVOs().length > 0)) {
          for (int j = 0; j < itemvo.getBarCodeVOs().length; ++j) {
            if ((itemvo.getBarCodeVOs()[j] == null) || ((itemvo.getBarCodeVOs()[j].getStatus() != 2) && (itemvo.getBarCodeVOs()[j].getStatus() != 1))) {
              continue;
            }
            alBarCode.add(itemvo.getBarCodeVOs()[j]);
          }

        }

      }

      if (alBarCode.size() > 0) {
        BarCodeDMO dmoBill = new BarCodeDMO();
        dmoBill.insertArray(alBarCode);
      }
    }
  }

  protected void insertBarCode(GeneralBillVO voNewBill)
    throws Exception
  {
    insertBarCode(voNewBill, false);
  }

  protected void insertBarCode(GeneralBillVO voNewBill, boolean bAllInsert)
    throws Exception
  {
    if (voNewBill == null)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000171"));
    GeneralBillItemVO[] voaItem = (GeneralBillItemVO[])(GeneralBillItemVO[])voNewBill.getChildrenVO();
    if ((voaItem == null) || (voaItem.length == 0)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000171"));
    }

    if (voNewBill.bSaveBarcodeFinal()) {
      ArrayList alBarCode = new ArrayList();
      for (int i = 0; i < voaItem.length; ++i) {
        GeneralBillItemVO itemvo = voaItem[i];
        if ((itemvo.getStatus() == 3) || (itemvo.getBarCodeVOs() == null) || (itemvo.getBarCodeVOs().length <= 0)) {
          continue;
        }
        for (int j = 0; j < itemvo.getBarCodeVOs().length; ++j) {
          if ((itemvo.getBarCodeVOs()[j] == null) || (itemvo.getBarCodeVOs()[j].getStatus() == 3) || ((!bAllInsert) && (itemvo.getStatus() != 2) && (((bAllInsert) || ((itemvo.getBarCodeVOs()[j].getStatus() != 2) && (itemvo.getBarCodeVOs()[j].getStatus() != 1))))))
          {
            continue;
          }

          alBarCode.add(itemvo.getBarCodeVOs()[j]);
        }

      }

      if (alBarCode.size() > 0) {
        BarCodeDMO dmoBill = new BarCodeDMO();
        dmoBill.insertArray(alBarCode);
      }
    }
  }

  protected ArrayList insertBillsWithNoLock(GeneralBillVO[] vos)
    throws BusinessException
  {
    ArrayList alKey = null;
    if (vos != null) {
      alKey = new ArrayList();
      for (int i = 0; i < vos.length; ++i)
        alKey.add(insertThisBill(vos[i]));
    }
    return alKey;
  }

  public ArrayList insertThisBill(GeneralBillVO voNewBill)
    throws BusinessException
  {
    try
    {
      return insertThisBill_Batch(voNewBill);
    } catch (Exception e) {
    	e.getCause().printStackTrace();
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  protected ArrayList insertThisBill_Batch(GeneralBillVO voNewBill)
    throws Exception
  {
    if (voNewBill == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000171"));
    }
    GeneralBillHeaderVO voHead = voNewBill.getHeaderVO();
    GeneralBillItemVO[] voaItem = voNewBill.getItemVOs();

    if ((voHead == null) || (voaItem == null) || (voaItem.length == 0) || (voHead.getCbilltypecode() == null) || (voHead.getPk_corp() == null) || (voHead.getCbilltypecode().trim().length() == 0) || (voHead.getPk_corp().trim().length() == 0))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000172"));
    }
    VOCheck.checkVMIWh(new QueryInfoDMO(), voNewBill);

    voHead.setFbillflag(new Integer("2"));
    Timer t = new Timer();
    t.start();

    GeneralBillDMO dmoBill = getBillDMO();

    ArrayList alKey = dmoBill.setBillPrimaryKeys(voNewBill);

    t.showExecuteTime("new** batchgetpk--@@");

    Object tmaketime = null;
    if (voHead != null) {
      tmaketime = voHead.getAttributeValue("tmaketime");
      if ((tmaketime == null) || (tmaketime.toString().trim().length() <= 0)) {
        voHead.setAttributeValue("tmaketime", new SystemTsGenerator().generateTS());
      }
    }
    String sBillPK = dmoBill.insertHeader(voHead);

    t.showExecuteTime("new** insert head--@@");

    SettlementDMO dmoStl = new SettlementDMO();
    LocatorDMO dmoLoc = new LocatorDMO();

    GeneralBillItemVO[] voItems = voNewBill.getItemVOs();

    for (int i = 0; i < voItems.length; ++i) {
      voItems[i].calBarcodeNum();
    }
    t.showExecuteTime("new** calBarcodeNum--@@");
    dmoBill.insertItemBatch(voItems);
    t.showExecuteTime("new** insertItemBatch--@@");

    insertBarCode(voNewBill);
    t.showExecuteTime("new** insertBarCode--@@");
    dmoLoc.insertLocatorBatch(voNewBill.getInsertedLocatorVOs());
    t.showExecuteTime("new** insertLocatorBatch--@@");

    if (dmoStl.hasBB3(voHead.getCbilltypecode()))
      dmoStl.insertBB3Batch(voItems);
    t.showExecuteTime("new** insertBB3Batch--@@");

    String sMsg = snInsert(voNewBill);

    if ((sMsg != null) && (sMsg.length() > 0)) {
      throw new BusinessException(sMsg);
    }

    if (voNewBill.isHaveSourceBill()) {
      dmoBill.setFirstBillCode(voItems, sBillPK);
      String csourcetype = (String)voNewBill.getItemValue(0, "csourcetype");
      if ((csourcetype != null) && (voNewBill.getItemValue(0, "vsourcerowno") == null))
      {
        dmoBill.setSourceRowNos(csourcetype, voHead.getCgeneralhid());
      }
      if (voNewBill.getItemValue(0, "cinvmanid") == null)
      {
        dmoBill.updateBasid(voHead.getCgeneralhid());
      }
    }
    t.showExecuteTime("new** update源头单据号--@@");

    freshTs(voNewBill, sBillPK);
    t.showExecuteTime("new** freshts--@@");

    t.start();

    synchronized (this) {
      modifyOnhandNum(voNewBill, null);
    }
    t.stop();
    t.showTime("new** modify num--@@");

    return alKey;
  }

  private void isSignLateBusiDate(GeneralBillVO gvo)
    throws Exception
  {
    if ((gvo == null) || (gvo.getItemVOs() == null) || (gvo.getItemCount() < 1)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000173"));
    }

    GeneralBillHeaderVO voHead = gvo.getHeaderVO();
    if (voHead == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000162"));
    }
    UFDate udCurDate = null; UFDate udBusiDate = null;

    if (voHead.getDaccountdate() != null)
      udCurDate = voHead.getDaccountdate();
    else
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000174"));
    int iLen = gvo.getItemCount();
    for (int i = 0; i < iLen; ++i) {
      udBusiDate = gvo.getItemVOs()[i].getDbizdate();

      if ((udBusiDate != null) && (udBusiDate.after(udCurDate)))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000175"));
    }
  }

  public String makeBothToZeroOnly(AggregatedValueObject vo)
    throws BusinessException
  {
    return makeBothToZeroOnly1(vo);
  }

  public String makeBothToZeroOnly1(AggregatedValueObject vo)
    throws BusinessException
  {
    if ((vo == null) || (vo.getChildrenVO() == null))
      return null;
    try {
      GeneralBillVO voCur = (GeneralBillVO)vo;
      GeneralBillHeaderVO voCurHead = voCur.getHeaderVO();
      GeneralBillItemVO[] voCurItems = (GeneralBillItemVO[])voCur.getItemVOs();
      if ((voCurHead.getPk_corp() == null) || (voCurHead.getCwarehouseid() == null)) {
        return null;
      }

      GeneralBillItemVO[] voItems = null;
      StringBuffer sErr = null;

      GeneralBillDMO dmo = getBillDMO();
      LocatorDMO dmoLoc = new LocatorDMO();

      GeneralBillVO[] vos = dmo.getVOSForUpdate2(voCur);
      if ((vos == null) || (vos.length != 2) || (vos[0] == null) || (vos[1] == null))
        return null;
      GeneralBillVO voUpdate = vos[0];
      GeneralBillVO voOld = vos[1];
      if ((voUpdate == null) || (voUpdate.getHeaderVO() == null) || (voUpdate.getItemVOs() == null))
      {
        return null;
      }voItems = voUpdate.getItemVOs();
      updateThisBill_Batch(voUpdate, voOld);

      GeneralBillItemVO[] voOldItems = voOld.getItemVOs();
      for (int i = 0; i < voItems.length; ++i) {
        if ((i < voOldItems.length) && (voOldItems[i] != null)) {
          voItems[i].setStatus(voOldItems[i].getStatus());
          voItems[i].setLocStatus(voOldItems[i].getLocStatus());
        }

      }

      if (voItems != null) {
        sErr = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000347"));
        for (int i = 0; i < voItems.length; ++i) {
          sErr.append("\n").append(voItems[i].getCinventorycode());
          if (voItems[i].getVfree1() != null)
            sErr.append("-" + NCLangResOnserver.getInstance().getStrByID("common", "UC000-0003327") + ":" + voItems[i].getVfree0());
          if (voItems[i].getVbatchcode() != null)
            sErr.append("-" + NCLangResOnserver.getInstance().getStrByID("4008bill", "UPPSCMCommon-000182") + NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000330") + voItems[i].getVbatchcode());
          if (voItems[i].getCastunitid() != null) {
            sErr.append("-" + NCLangResOnserver.getInstance().getStrByID("common", "UC000-0003938") + ":" + voItems[i].getCastunitname());
          }
        }
      }
      if (sErr != null) {
        return sErr.toString();
      }
      return null;
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  protected void modifyOnhandNum(GeneralBillVO voNewBill, GeneralBillVO voDbBill)
    throws Exception
  {
    OnhandnumDMO dmoOnhand = new OnhandnumDMO();
    dmoOnhand.modifyOnhandNum(voNewBill, voDbBill);
    dmoOnhand = null;
  }

  public ArrayList queryBills(QryConditionVO voQC)
    throws BusinessException
  {
    try
    {
      GeneralBillDMO dmo = getBillDMO();
      return dmo.queryBills(voQC);
    }
    catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  public Object queryInfo(Integer iSel, Object oQryCond)
    throws BusinessException
  {
    try
    {
      if ((iSel == null) || (oQryCond == null)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000178"));
      }

      Object objRet = null;

      ArrayList alIDs = null;
      ArrayList alRet = null; ArrayList alInvID = null;

      String sWhID = null; String sInvID = null; String sUserID = null; String sCorpID = null; String sAstUOMID = null;
      String pk_calbody = null;
      MiscDMO dmoMisc = null;
      GeneralBillDMO dmoBill = null;

      if ((iSel != null) && (iSel.intValue() == 17) && (oQryCond != null) && (oQryCond instanceof ArrayList) && (((ArrayList)oQryCond).size() > 0) && (((ArrayList)oQryCond).get(0) != null))
      {
        ArrayList alBusitype = new ArrayList();

        OnhandnumDMO dmoOnhand = new OnhandnumDMO();
        Hashtable htBusitype = dmoOnhand.queryBusitype(((ArrayList)oQryCond).get(0).toString());
        if (htBusitype != null) {
          Enumeration enumKey = htBusitype.keys();
          String sBusitypeid = null;
          while (enumKey.hasMoreElements())
          {
            sBusitypeid = (String)enumKey.nextElement();
            alBusitype.add(sBusitypeid);
          }
        }

        return alBusitype;
      }

      if ((iSel.intValue() == 12) || (iSel.intValue() == 16) || (iSel.intValue() == 3) || (iSel.intValue() == 4) || (iSel.intValue() == 23) || (iSel.intValue() == 18))
      {
        dmoBill = getBillDMO();
      }
      else dmoMisc = new MiscDMO();
      String CalBody;
      switch (iSel.intValue())
      {
      case 0:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null)
          sWhID = alIDs.get(0).toString();
        if (alIDs.get(1) != null)
          sInvID = alIDs.get(1).toString();
        if (alIDs.size() > 2) {
          if (alIDs.get(2) != null)
            sUserID = alIDs.get(2).toString();
          if (alIDs.get(3) != null)
            sCorpID = alIDs.get(3).toString();
        }
        objRet = dmoMisc.getInvQty(sWhID, sInvID, sUserID, sCorpID);
        break;
      case 1:
        if (dmoMisc != null)
          objRet = dmoMisc.getWhInfo((String)oQryCond); break;
      case 2:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null)
          sWhID = alIDs.get(0).toString();
        if (alIDs.get(1) != null)
          sInvID = alIDs.get(1).toString();
        objRet = dmoMisc.getQty(sWhID, sInvID);
        break;
      case 200:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null)
          sWhID = alIDs.get(0).toString();
        if (alIDs.get(1) != null)
          sInvID = alIDs.get(1).toString();
        if (alIDs.size() > 2) {
          if (alIDs.get(2) != null)
            pk_calbody = alIDs.get(2).toString();
          if (alIDs.get(3) != null) {
            sCorpID = alIDs.get(3).toString();
          }
        }
        objRet = dmoMisc.getInvQtyCal(sWhID, sInvID, pk_calbody, sCorpID);

        InvVO voTmp = dmoMisc.getOnHandNum(sCorpID, pk_calbody, sWhID, sInvID);
        ((InvVO)objRet).setBkxcl(voTmp.getBkxcl());
        ((InvVO)objRet).setXczl(voTmp.getXczl());

        break;
      case 202:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null)
          sWhID = alIDs.get(0).toString();
        if (alIDs.get(1) != null)
          sInvID = alIDs.get(1).toString();
        if (alIDs.size() > 2) {
          if (alIDs.get(2) != null)
            pk_calbody = alIDs.get(2).toString();
          if (alIDs.get(3) != null) {
            sCorpID = alIDs.get(3).toString();
          }
        }
        objRet = dmoMisc.getInvQtyCal(sWhID, sInvID, pk_calbody, sCorpID);

        break;
      case 201:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null)
          sWhID = alIDs.get(0).toString();
        if (alIDs.get(1) != null)
          sInvID = alIDs.get(1).toString();
        if (alIDs.size() > 2) {
          if (alIDs.get(2) != null)
            pk_calbody = alIDs.get(2).toString();
          if (alIDs.get(3) != null) {
            sCorpID = alIDs.get(3).toString();
          }
        }
        objRet = dmoMisc.getOnHandNum(sCorpID, pk_calbody, sWhID, sInvID);

        break;
      case 3:
        objRet = dmoBill.getLocSNInfo((String)oQryCond);
        break;
      case 4:
        objRet = dmoBill.getSNInfo((String)oQryCond);
        break;
      case 23:
        objRet = dmoBill.getLocInfo((String)oQryCond);
        break;
      case 5:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null)
          sInvID = alIDs.get(0).toString();
        if (alIDs.get(1) != null)
          sAstUOMID = alIDs.get(1).toString();
        objRet = dmoMisc.getUOMinfo(sInvID, sAstUOMID);
        break;
      case 6:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null) {
          alInvID = (ArrayList)alIDs.get(0);
        }
        if ((alInvID != null) && (alInvID.size() > 0)) {
          alRet = dmoMisc.getInvInfo(alInvID);
        }
        objRet = alRet;
        break;
      case 7:
        String[] saParam = null;
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null) {
          sCorpID = alIDs.get(0).toString();
        }
        if (alIDs.get(1) != null) {
          saParam = (String[])(String[])alIDs.get(1);
        }
        objRet = dmoMisc.getSysParam(sCorpID, saParam);
        break;
      case 8:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null) {
          alInvID = (ArrayList)alIDs.get(0);
        }
        if ((alInvID != null) && (alInvID.size() > 0)) {
          alRet = new ArrayList();

          ArrayList alTemp = null;

          InvVO voTempInv = null;
          for (int i = 0; i < alInvID.size(); ++i) {
            alTemp = (ArrayList)alInvID.get(i);
            if (alTemp.size() >= 2) {
              sInvID = (String)alTemp.get(0);
              sAstUOMID = (String)alTemp.get(1);
              voTempInv = dmoMisc.getInvInfo(sInvID, sAstUOMID);
              if (voTempInv != null) {
                alRet.add(voTempInv);
              } else {
                SCMEnv.out("@@@cannot qry with astuom.....");
                alRet.add(dmoMisc.getInvInfo(sInvID));
              }
            } else {
              alRet.add(null);
            }
          }
        }
        objRet = alRet;
        break;
      case 9:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null)
          sWhID = alIDs.get(0).toString();
        if (alIDs.get(1) != null)
          sUserID = alIDs.get(1).toString();
        if (alIDs.get(2) != null)
          sCorpID = alIDs.get(2).toString();
        objRet = dmoMisc.getCalBody(sWhID, sUserID, sCorpID);
        break;
      case 10:
        objRet = dmoMisc.getUserCorpIDs(oQryCond.toString());
        break;
      case 11:
        ArrayList alInitParam = (ArrayList)oQryCond;

        if (alInitParam.size() >= 2) {
          ArrayList alInitRet = new ArrayList();

          String[] saParamCode = null;
          alIDs = (ArrayList)alInitParam.get(0);
          if (alIDs.get(0) != null) {
            sCorpID = alIDs.get(0).toString();
          }
          if (alIDs.get(1) != null) {
            saParamCode = (String[])(String[])alIDs.get(1);
          }
          alInitRet.add(dmoMisc.getSysParam(sCorpID, saParamCode));

          alInitRet.add(dmoMisc.getUserCorpIDs((String)alInitParam.get(1)));

          CreatecorpBO bo1 = new CreatecorpBO();
          String[] sdate = bo1.queryEnabledPeriod(sCorpID, "IC");

          if ((sdate != null) && (sdate.length > 2))
          {
            AccountCalendar ac = AccountCalendar.getInstance();
            ac.set(sdate[0], sdate[1]);
            AccperiodmonthVO voAccperiodmonth = ac.getMonthVO();

            if (voAccperiodmonth != null) {
              alInitRet.add(voAccperiodmonth.getBegindate().toString());
            }
          }

          objRet = alInitRet;
        }break;
      case 12:
        objRet = dmoBill.isSigned((String)oQryCond);

        break;
      case 16:
        ArrayList alTsRet = new ArrayList();
        String sSigned = null;
        String billcode = null;

        sSigned = dmoBill.getBillStatus((String)oQryCond);
        billcode = dmoBill.queryBillCode((String)oQryCond);

        alTsRet.add(sSigned);

        alTsRet.add(dmoBill.getTs((String)oQryCond));

        alTsRet.add(billcode);
        objRet = alTsRet;
        break;
      case 14:
        alIDs = (ArrayList)oQryCond;
        if (alIDs.get(0) != null) {
          alInvID = (ArrayList)alIDs.get(0);
        }
        if ((alInvID != null) && (alInvID.size() > 0)) {
          alRet = new ArrayList();

          ArrayList alTemp = null;

          InvVO voTempInv = null;

          String sJob = null; String sJobphase = null;
          String[] saJobName = null;
          for (int i = 0; i < alInvID.size(); ++i) {
            alTemp = (ArrayList)alInvID.get(i);
            if (alTemp.size() >= 2) {
              sInvID = (String)alTemp.get(0);
              sAstUOMID = (String)alTemp.get(1);
              voTempInv = dmoMisc.getInvInfo(sInvID, sAstUOMID);
              if (voTempInv == null) {
                SCMEnv.out("@@@cannot qry with astuom.....");
                voTempInv = dmoMisc.getInvInfo(sInvID);
              }

              sJob = (String)alTemp.get(2);
              sJobphase = (String)alTemp.get(3);
              saJobName = dmoMisc.getJobInfo(sJob, sJobphase);
              if ((saJobName != null) && (saJobName.length >= 4))
              {
                voTempInv.setCprojectid(sJob);
                voTempInv.setCprojectcode(saJobName[0]);
                voTempInv.setCprojectname(saJobName[1]);

                voTempInv.setCprojectphaseid(sJobphase);
                voTempInv.setCprojectphasecode(saJobName[2]);
                voTempInv.setCprojectphasename(saJobName[3]);
              }
              alRet.add(voTempInv);
            }
            else {
              alRet.add(null);
            }
          }
        }
        objRet = alRet;
        break;
      case 15:
        alIDs = (ArrayList)oQryCond;

        if (alIDs.get(0) != null) {
          sWhID = (String)alIDs.get(0);
        }
        String sLastCalBody = (String)alIDs.get(1);

        WhVO voWh = dmoMisc.getWhInfo(sWhID);

        if (voWh.getPk_calbody() == null) {
          objRet = voWh;
        }
        else {
          sCorpID = (String)alIDs.get(2);
          alInvID = (ArrayList)alIDs.get(3);
          alRet = new ArrayList();
          alRet.add(voWh);
          alRet.add(dmoMisc.getPlanPrice(sWhID, alInvID, null, sCorpID));
          objRet = alRet;
        }
        break;
      case 24:
        alIDs = (ArrayList)oQryCond;
        CalBody = null;
        if (alIDs.get(0) != null)
          sWhID = (String)alIDs.get(0);
        else {
          sWhID = "null";
        }

        if (alIDs.get(1) != null) {
          CalBody = (String)alIDs.get(1);
        }
        if (alIDs.get(2) != null) {
          sCorpID = (String)alIDs.get(2);
        }
        if (alIDs.get(3) != null) {
          sInvID = (String)alIDs.get(3);
        }
        alRet = new ArrayList();
        objRet = dmoMisc.getPlanPrice(sWhID, sInvID, CalBody, sCorpID);
        break;
      case 26:
        alIDs = (ArrayList)oQryCond;
        CalBody = null;

        if (alIDs.get(0) != null) {
          CalBody = (String)alIDs.get(0);
        }
        if (alIDs.get(1) != null) {
          sCorpID = (String)alIDs.get(1);
        }
        ArrayList alInvIDs = null;
        if (alIDs.get(2) != null) {
          alInvIDs = (ArrayList)alIDs.get(2);
        }
        alRet = new ArrayList();
        objRet = dmoMisc.getPlanPrices(CalBody, alInvIDs, sCorpID);
        break;
      case 22:
        alIDs = (ArrayList)oQryCond;

        if (alIDs.get(1) != null) {
          sWhID = (String)alIDs.get(1);
        }
        sCorpID = (String)alIDs.get(3);
        sInvID = (String)alIDs.get(0);
        UFDouble ufdPlanPrice = dmoMisc.getPlanPrice(sWhID, sInvID, null, sCorpID);
        objRet = ufdPlanPrice;
        break;
      case 18:
        objRet = null;
        GeneralBillItemVO voRetItem = dmoBill.queryBillItem(oQryCond.toString());
        if (voRetItem != null) {
          ArrayList alItem = new ArrayList();
          alItem.add(voRetItem);
          objRet = alItem;
        }

      }

      return objRet;
    } catch (Exception e) {
      if (e instanceof BusinessException) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
  }

  private int[][] readSnStatus(GeneralBillVO voUpdatedBill, GeneralBillVO voOld)
    throws BusinessException, Exception
  {
    GeneralBillHeaderVO voUpdatedHeader = voUpdatedBill.getHeaderVO();

    GeneralBillItemVO[] voaUpdatedItem = voUpdatedBill.getItemVOs();

    GeneralBillItemVO[] voaOldItem = voOld.getItemVOs();

    Hashtable htOldItems = new Hashtable();

    for (int i = 0; i < voaOldItem.length; ++i) {
      if ((voaOldItem[i] != null) && (voaOldItem[i].getPrimaryKey() != null))
        htOldItems.put(voaOldItem[i].getPrimaryKey(), voaOldItem[i]);
      else {
        SCMEnv.out("fatal:old bill item null");
      }
    }
    int[] iaInOut = voUpdatedBill.getBillItemInOutPty();
    int rowcount = voaUpdatedItem.length;
    int[][] ia2SnStatus = new int[rowcount][2];

    String sBodyPK = null;

    int iInOutPty = 0;

    int iTempSnStatus = 0;

    GeneralBillItemVO voTempItem = null;

    for (int row = 0; row < rowcount; ++row) {
      ia2SnStatus[row][0] = 0;
      ia2SnStatus[row][1] = 0;

      sBodyPK = voaUpdatedItem[row].getPrimaryKey();
      switch (voaUpdatedItem[row].getStatus())
      {
      case 2:
        ia2SnStatus[row][1] = iaInOut[row];
        break;
      case 3:
        ia2SnStatus[row][0] = iaInOut[row];
        break;
      case 1:
        iTempSnStatus = voaUpdatedItem[row].getSnStatus();

        voTempItem = (GeneralBillItemVO)htOldItems.get(sBodyPK);
        iInOutPty = GeneralBillVO.getBillItemInOutPty(voUpdatedHeader, voTempItem);
        if ((iTempSnStatus == 0) || (
          (iTempSnStatus != 1) && (iTempSnStatus != 3))) continue;
        ia2SnStatus[row][0] = iaInOut[row];
        ia2SnStatus[row][1] = iInOutPty;
        break;
      case 0:
        iTempSnStatus = voaUpdatedItem[row].getSnStatus();
        if ((iTempSnStatus == 0) || (
          (iTempSnStatus != 1) && (iTempSnStatus != 3))) continue;
        ia2SnStatus[row][0] = iaInOut[row];
        ia2SnStatus[row][1] = iaInOut[row];
      }

    }

    return ia2SnStatus;
  }

  public ArrayList revBBarcodeClose(ArrayList alParam, UFBoolean bUFflag)
    throws BusinessException
  {
    try
    {
      if ((alParam == null) || (alParam.size() != 3)) return null;
      ArrayList alBIDs = (ArrayList)alParam.get(1);
      ArrayList alTss = (ArrayList)alParam.get(2);
      if ((alBIDs.size() <= 0) || (alTss.size() <= 0) || (alBIDs.size() != alTss.size())) {
        return null;
      }

      String sHeadPk = (String)alParam.get(0);

      GeneralBillDMO dmo = new GeneralBillDMO();
      GeneralBillItemVO[] voaItemDB = dmo.queryBillItemTs(sHeadPk);

      if (voaItemDB == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000179"));
      }
      int iHashLen = voaItemDB.length;
      Hashtable htDB = new Hashtable();
      for (int i = 0; i < iHashLen; ++i) {
        if (voaItemDB[i].getPrimaryKey() != null) {
          htDB.put(voaItemDB[i].getPrimaryKey(), voaItemDB[i].getTs());
        }
      }

      int iSize = alBIDs.size();

      String sPKBody = null;
      String tsTemp = null;

      for (int i = 0; i < iSize; ++i) {
        sPKBody = (String)alBIDs.get(i);
        tsTemp = (String)alTss.get(i);
        if ((!htDB.containsKey(sPKBody)) || 
          (htDB.get(sPKBody).equals(tsTemp))) continue;
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000180"));
      }

      dmo.revBBarcodeClose(alParam, bUFflag);

      freshTsBodyItem(alParam);
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }

    return alParam;
  }

  protected ArrayList saveBill(GeneralBillVO voNewBill)
    throws BusinessException
  {
    if ((voNewBill == null) || (voNewBill.getHeaderVO() == null)) {
      SCMEnv.out("param null ");
      return null;
    }
    try
    {
      ArrayList alRes = null;
      if (voNewBill != null)
      {
        String sBillPK = voNewBill.getHeaderVO().getPrimaryKey();
        String sBillCode = voNewBill.getHeaderVO().getVbillcode();
        voNewBill.synProvider();

        if (sBillPK == null)
        {
          alRes = insertThisBill(voNewBill);
        }
        else alRes = updateThisBill(voNewBill);

        saveBatchCode(voNewBill);
      }
      return alRes;
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  protected ArrayList saveBill(GeneralBillVO voCur, Object voPre)
    throws BusinessException
  {
    Timer t = new Timer();
    t.start();
    if ((voCur == null) || (voCur.getHeaderVO() == null)) {
      SCMEnv.out("param null ");
      return null;
    }
  
    try
    {
      ArrayList alRes = null;
      if (voCur != null)
      {
        try
        {
          VOCheck.checkNumInput(voCur.getChildrenVO(), "csourcebillbid");
        }
        catch (ICNumException e1) {
          throw new BusinessException(e1.getHint());
        }
        String sBillPK = voCur.getHeaderVO().getPrimaryKey();
        String sBillCode = voCur.getHeaderVO().getVbillcode();
        int iBillStatus = voCur.getHeaderVO().getStatus();
        SCMEnv.out("单据保存：单据状态，" + iBillStatus);
        

        
        GeneralBillBOHelper.reCheckBarCodeParam(voCur);


        checkLocator(voCur);
        t.showExecuteTime("new** 处理货位及检查--@@");

        if (voCur != null) {
          CheckInvVendorDMO dmo = new CheckInvVendorDMO();
  
          dmo.checkVmiVendorInput(voCur);
          voCur.synProvider();
        }
        t.showExecuteTime("new** 供应商必输检查及入库供应商同步--@@");
   
        CheckDMO dmo = new CheckDMO();

        dmo.checkDef(voCur);
        t.showExecuteTime("new** 单据自定义项检查--@@");

        fillPlanPrice(voCur);

        GeneralBillBOHelper.dealPkCorp(voCur);
 
        if ((iBillStatus == 2) || (voCur.getHeaderVO().isImportData()))
        {
        	
          alRes = insertThisBill(voCur);
          SCMEnv.out("单据保存：单据状态新增加");
        } else {
          alRes = updateThisBill(voCur, (GeneralBillVO)voPre);
          SCMEnv.out("单据保存：单据状态修改");
        }
        saveBatchCode(voCur);

        new CheckBusiDMO().checkCorNum(voCur.getHeaderVO().getCgeneralhid());

        new CheckBarcodeDMO().checkBCOnhandAndRepeat(voCur);

        t.showExecuteTime("new** 单据保存总时间--@@");
      }
      return alRes;
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  public void fillPlanPrice(GeneralBillVO vo)
    throws Exception
  {
    if ((vo.getHeaderVO() != null) && (vo.getHeaderVO().getCcustomerid() != null) && (nc.vo.ic.pub.GenMethod.isSEmptyOrNull((String)vo.getHeaderValue("pk_cubasdocC"))))
    {
      new ICSmartToolsDmo().fillVOValueBy(new GeneralBillHeaderVO[] { vo.getHeaderVO() }, "pk_cubasdocC", "ccustomerid", "bd_cumandoc", "pk_cubasdoc", "pk_cumandoc", null);
    }

    if ((!vo.isGetPlanPriceAtBs()) || (nc.vo.ic.pub.GenMethod.isSEmptyOrNull(vo.getHeaderVO().getPk_calbody())))
      return;
    GeneralBillItemVO[] items = vo.getItemVOs();
    ArrayList invids = new ArrayList(items.length);
    for (GeneralBillItemVO item : items) {
      if (nc.vo.ic.pub.GenMethod.isEQZeroOrNull(item.getNplannedprice()))
        invids.add(item.getCinventoryid());
    }
    if (invids.size() == 0) {
      return;
    }

    String pkCostCalbody = storeToCost(vo.getHeaderVO().getPk_corp(), vo.getHeaderVO().getPk_calbody(), vo.getHeaderVO().getCwarehouseid());

    PriceDMO dmoPrice = new PriceDMO();
    Hashtable htPrice = dmoPrice.getPrdPlanPrice(vo.getHeaderVO().getPk_corp(), pkCostCalbody, null, (String[])invids.toArray(new String[invids.size()]));

    for (GeneralBillItemVO item : items)
      if (htPrice.containsKey(item.getCinventoryid())) {
        item.setNplannedprice((UFDouble)htPrice.get(item.getCinventoryid()));
        item.getInv().setNplannedprice(item.getNplannedprice());
      }
  }

  private String storeToCost(String pk_corp, String pk_storcalbody, String pk_stordoc)
    throws BusinessException
  {
    String pkCostCalbody = null;

    StorVSCostBO bo = new StorVSCostBO();
    try {
      pkCostCalbody = bo.getCostCalBody(pk_corp, pk_storcalbody, pk_stordoc);
    }
    catch (BusinessException e) {
      throw nc.bs.ic.pub.GenMethod.handleException(null, e);
    }

    if ((pkCostCalbody == null) || (pkCostCalbody.length() == 0)) {
      pkCostCalbody = pk_storcalbody;
    }
    return pkCostCalbody;
  }

  public String saveBillBarcode(GeneralBillVO voNewBill)
    throws BusinessException
  {
    if ((voNewBill == null) || (voNewBill.getHeaderVO() == null)) {
      SCMEnv.out("param null ");
      return null;
    }

    ArrayList aDirtyList = null;
    ArrayList aUpdateList = null;
    StringBuffer sbErro = null;
    try
    {
      ArrayList alRes = null;
      int xx;
      if (voNewBill != null)
      {
        String sPkCorp = voNewBill.getPk_corp();
        String sBillPK = voNewBill.getHeaderVO().getPrimaryKey();
        String sBillCode = voNewBill.getHeaderVO().getVbillcode();

        if (sBillPK == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000181"));
        }

        if (voNewBill.bSaveBarcodeFinal())
        {
          ArrayList alUpdateItem = new ArrayList();
          GeneralBillItemVO[] voaItem = voNewBill.getItemVOs();
          GeneralBillItemVO itemvo = null;

          alUpdateItem = getBarcodeUPdateItem(voaItem, false);

          if ((alUpdateItem != null) && (alUpdateItem.size() > 0))
          {
            GeneralBillItemVO[] voaUpateItem = new GeneralBillItemVO[alUpdateItem.size()];
            alUpdateItem.toArray(voaUpateItem);

            CheckDMO checkdmo = new CheckDMO();
            ArrayList alresult = checkdmo.checkTimeStamp(voaUpateItem);

            if ((alresult != null) && (alresult.size() >= 2)) {
              aDirtyList = (ArrayList)alresult.get(0);
              aUpdateList = (ArrayList)alresult.get(1);
              GeneralBillItemVO itemvoDirty = null;

              if ((aDirtyList != null) && (aDirtyList.size() > 0))
              {
                sbErro = new StringBuffer("\n" + NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000348") + NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000330") + "\n");
                for (int i = 0; i < aDirtyList.size(); ++i) {
                  itemvoDirty = (GeneralBillItemVO)aDirtyList.get(i);
                  sbErro.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000349") + ":" + itemvoDirty.getCrowno() + "," + NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001439") + ":" + itemvoDirty.getInvname() + "\n");
                }

                sbErro.append(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000350"));
              }

              GeneralBillDMO dmoBill = getBillDMO();

              if ((aUpdateList != null) && (aUpdateList.size() > 0)) {
                GeneralBillItemVO[] voaUpateItemNew = new GeneralBillItemVO[aUpdateList.size()];

                aUpdateList.toArray(voaUpateItemNew);

                alRes = dmoBill.setBillBarcodePrimaryKeys(voaUpateItemNew, sPkCorp);

                deleteBarCode(voaUpateItemNew, true);

                insertBarCode(voaUpateItemNew, true);

                dmoBill.updateItemBarcodeNumBatch(aUpdateList);
              }

              new CheckBarcodeDMO().checkBCOnhandAndRepeat(voNewBill);

              freshTs(voNewBill, sBillPK);

              dmoBill.queryBillItemBarCode(voNewBill.getItemVOs());
              xx = 0;
            }

          }

        }

      }

      if (sbErro == null) {
        return null;
      }
      return sbErro.toString();
    }
    catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  protected ArrayList signBill(GeneralBillVO voAuditBill)
    throws BusinessException
  {
    try
    {
      if (voAuditBill == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000162"));
      }
      GeneralBillHeaderVO voHead = (GeneralBillHeaderVO)voAuditBill.getParentVO();
      if (voHead == null)
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000162"));
      return signBills(new GeneralBillVO[] { voAuditBill });
    }
    catch (Exception e)
    {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  protected ArrayList signBills(GeneralBillVO[] voAuditBills)
    throws BusinessException
  {
    try
    {
      GeneralBillDMO dmoBill = getBillDMO();
      for (int i = 0; i < voAuditBills.length; ++i) {
        CheckDMO.appendInvWhInfo(voAuditBills[i]);
      }
      dmoBill.sign(voAuditBills);

      GeneralBillBOHelper.modifyBillsSign(voAuditBills, true);

      freshTs(voAuditBills[0], voAuditBills[0].getPrimaryKey());
    }
    catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }
    return null;
  }

  private String snDelete(GeneralBillVO voCur)
    throws Exception
  {
    GeneralBillItemVO[] voaItem = voCur.getItemVOs();

    ArrayList aryEx = null;
    ArrayList aryOutorFreeze = null;
    ArrayList aryReturn = null;

    boolean isOneOperate = false;
    boolean isOperIn = false;

    ArrayList aryBillItemDelIn = null;
    ArrayList aryBillItemDelOut = null;

    ArrayList aryResult = getSnStatues(null, voCur, null);
    if (aryResult == null) {
      return null;
    }
    boolean isSnStatuese = ((Boolean)aryResult.get(8)).booleanValue();
    if (!isSnStatuese) {
      return null;
    }

    isOneOperate = ((Boolean)aryResult.get(0)).booleanValue();
    isOperIn = ((Boolean)aryResult.get(1)).booleanValue();
    SNDMO sndmo = new SNDMO();
    aryBillItemDelIn = (ArrayList)aryResult.get(4);
    aryBillItemDelOut = (ArrayList)aryResult.get(5);
    GeneralBillHeaderVO headerV0 = voCur.getHeaderVO();
    if (isOneOperate)
    {
      String sHeaderHid = headerV0.getPrimaryKey();
      if (isOperIn)
      {
        SCMEnv.out("整单删除入库");
        aryOutorFreeze = sndmo.billAllInCancelCheck(sHeaderHid);
        if ((aryOutorFreeze == null) || (aryOutorFreeze.size() == 0))
        {
          SCMEnv.out("整单入库取消校验合格");
          sndmo.billAllInCancel(sHeaderHid);
          SCMEnv.out("整单入库取消完成");
        }

      }
      else
      {
        SCMEnv.out("整单删除出库");
        aryReturn = sndmo.billAllOutCancelCheck(sHeaderHid);
        if ((aryReturn == null) || (aryReturn.size() == 0))
        {
          sndmo.billAllOutCancel(sHeaderHid);
          SCMEnv.out("整单出库取消完成");
        }

      }

    }
    else
    {
      if ((aryBillItemDelIn != null) && (aryBillItemDelIn.size() > 0))
      {
        SCMEnv.out("表体行入库行删除");
        aryOutorFreeze = sndmo.billItemInCancelCheck(getBillItemAryID(aryBillItemDelIn));
        if ((aryOutorFreeze == null) || (aryOutorFreeze.size() == 0)) {
          sndmo.billItemInCancel(aryBillItemDelIn);
        }
      }
      if ((aryBillItemDelOut != null) && (aryBillItemDelOut.size() > 0))
      {
        SCMEnv.out("表体行出库行删除");
        aryReturn = sndmo.billItemOutCancelCheck(getBillItemAryID(aryBillItemDelOut));
        if ((aryReturn == null) || (aryReturn.size() == 0)) {
          sndmo.billItemOutCancel(getBillItemAryID(aryBillItemDelIn));
        }
      }
    }
    String sMsg = getSnHintMsg(aryEx, null, aryReturn, aryOutorFreeze);
    return sMsg;
  }

  protected String snInsert(GeneralBillVO voCur)
    throws Exception
  {
    GeneralBillItemVO[] voaItem = voCur.getItemVOs();

    ArrayList aryEx = null;
    ArrayList aryOutorFreeze = null;

    boolean isOneOperate = false;
    boolean isOperIn = false;
    ArrayList aryBillItemIn = null;
    ArrayList aryBillItemOut = null;

    ArrayList aryResult = getSnStatues(voCur, null, null);
    if (aryResult == null) {
      return null;
    }

    boolean isSnStatuese = ((Boolean)aryResult.get(8)).booleanValue();
    if (!isSnStatuese) {
      return null;
    }

    isOneOperate = ((Boolean)aryResult.get(0)).booleanValue();
    isOperIn = ((Boolean)aryResult.get(1)).booleanValue();

    SNDMO sndmo = new SNDMO();
    aryBillItemIn = (ArrayList)aryResult.get(2);
    aryBillItemOut = (ArrayList)aryResult.get(3);
    GeneralBillHeaderVO headerV0 = voCur.getHeaderVO();
    long lTime = System.currentTimeMillis();
    if ((isOneOperate) && (isOperIn))
    {
      SCMEnv.out("snInsert:执行整单录入");

      lTime = System.currentTimeMillis();
      sndmo.billItemIn(aryBillItemIn, headerV0);
      SCMEnv.showTime(lTime, "sndmo.billItemIn:");
      String sHeaderHid = headerV0.getPrimaryKey();

      lTime = System.currentTimeMillis();
      aryEx = sndmo.billInCheck(sHeaderHid);
      SCMEnv.out("snInsert:执行整单录入结束");
      SCMEnv.showTime(lTime, "sndmo.billInCheck:");
    }
    else
    {
      if ((aryBillItemIn != null) && (aryBillItemIn.size() > 0))
      {
        SCMEnv.out("snInsert:执行单据行入库录入");

        lTime = System.currentTimeMillis();
        sndmo.billItemIn(aryBillItemIn, headerV0);
        SCMEnv.showTime(lTime, "sndmo.billItemIn:");

        lTime = System.currentTimeMillis();
        aryEx = sndmo.billItemInCheck(getBillItemAryID(aryBillItemIn));
        SCMEnv.showTime(lTime, "sndmo.billItemInCheck:");

        SCMEnv.out("snInsert:执行单据行入库录入完毕");
      }

      if ((aryBillItemOut != null) && (aryBillItemOut.size() > 0))
      {
        SCMEnv.out("snInsert:执行单据行出库录入");
        lTime = System.currentTimeMillis();
        aryOutorFreeze = sndmo.billItemOutandCheck(aryBillItemOut, headerV0);
        SCMEnv.showTime(lTime, "sndmo.billItemOutandCheck:");
        SCMEnv.out("snInsert:执行单据行出库录入完毕");
      }
    }
    String sMsg = getSnHintMsg(aryEx, null, null, aryOutorFreeze);
    return sMsg;
  }

  protected String snUpdate(GeneralBillVO voCur, Hashtable htOldItems, GeneralBillVO voOld)
    throws Exception
  {
    GeneralBillItemVO[] voaItem = voCur.getItemVOs();

    ArrayList aryEx = null;
    ArrayList aryOutorFreeze = null;
    ArrayList aryReturn = null;

    boolean isOneOperate = false;
    boolean isOperIn = false;

    ArrayList aryResult = getSnStatues(voCur, voOld, htOldItems);
    if (aryResult == null) {
      return null;
    }

    boolean isSnStatuese = ((Boolean)aryResult.get(8)).booleanValue();
    if (!isSnStatuese) {
      return null;
    }

    isOneOperate = ((Boolean)aryResult.get(0)).booleanValue();
    isOperIn = ((Boolean)aryResult.get(1)).booleanValue();
    SNDMO sndmo = new SNDMO();
    ArrayList aryBillItemIn = (ArrayList)aryResult.get(2);
    ArrayList aryBillItemOut = (ArrayList)aryResult.get(3);
    ArrayList aryBillItemDelIn = (ArrayList)aryResult.get(4);
    ArrayList aryBillItemDelOut = (ArrayList)aryResult.get(5);
    ArrayList aryBillItemUpdateIn = (ArrayList)aryResult.get(6);
    ArrayList aryBillItemUpdateOut = (ArrayList)aryResult.get(7);

    GeneralBillHeaderVO headerV0 = voCur.getHeaderVO();
    String sHeaderHid = headerV0.getPrimaryKey();
    long lTime = System.currentTimeMillis();
    if (isOneOperate)
    {
      if (isOperIn)
      {
        if ((aryBillItemUpdateIn != null) && (aryBillItemUpdateIn.size() > 0))
        {
          lTime = System.currentTimeMillis();
          aryOutorFreeze = sndmo.billAllInCancelCheck(sHeaderHid);
          SCMEnv.showTime(lTime, "sndmo.billAllInCancelCheck:");
          if ((aryOutorFreeze == null) || (aryOutorFreeze.size() == 0))
          {
            lTime = System.currentTimeMillis();
            sndmo.billAllInCancel(sHeaderHid);
            SCMEnv.showTime(lTime, "sndmo.billAllInCancel:");

            lTime = System.currentTimeMillis();
            sndmo.billItemIn(aryBillItemUpdateIn, headerV0);
            SCMEnv.showTime(lTime, "sndmo.billItemIn:");

            lTime = System.currentTimeMillis();
            aryEx = sndmo.billInCheck(sHeaderHid);
            SCMEnv.showTime(lTime, "sndmo.billInCheck:");

            SCMEnv.out("整单入库修改");
          }

        }

        if ((aryBillItemDelIn != null) && (aryBillItemDelIn.size() > 0))
        {
          SCMEnv.out("修改状态：入库取消校验");
          lTime = System.currentTimeMillis();
          aryOutorFreeze = sndmo.billAllInCancelCheck(sHeaderHid);
          SCMEnv.showTime(lTime, "sndmo.billAllInCancelCheck:");

          if ((aryOutorFreeze == null) || (aryOutorFreeze.size() == 0))
          {
            SCMEnv.out("整单入库取消校验合格");
            lTime = System.currentTimeMillis();

            sndmo.billAllInCancel(sHeaderHid);

            SCMEnv.showTime(lTime, "sndmo.billAllInCancel:");
            SCMEnv.out("整单入库取消完成");
          }

        }

        if ((aryBillItemIn != null) && (aryBillItemIn.size() > 0))
        {
          lTime = System.currentTimeMillis();
          sndmo.billItemIn(aryBillItemIn, headerV0);
          SCMEnv.showTime(lTime, "sndmo.billItemIn:");
          sHeaderHid = headerV0.getPrimaryKey();

          lTime = System.currentTimeMillis();
          aryEx = sndmo.billInCheck(sHeaderHid);
          SCMEnv.out("snInsert:执行整单录入结束");
          SCMEnv.showTime(lTime, "sndmo.billInCheck:");
        }

      }
      else
      {
        if ((aryBillItemUpdateOut != null) && (aryBillItemUpdateOut.size() > 0))
        {
          lTime = System.currentTimeMillis();
          aryReturn = sndmo.billAllOutCancelCheck(sHeaderHid);
          SCMEnv.showTime(lTime, "sndmo.billAllOutCancelCheck:");

          if ((aryReturn == null) || (aryReturn.size() == 0))
          {
            lTime = System.currentTimeMillis();
            sndmo.billAllOutCancel(sHeaderHid);
            SCMEnv.showTime(lTime, "sndmo.billAllOutCancel:");

            lTime = System.currentTimeMillis();
            aryOutorFreeze = sndmo.billItemOutandCheck(aryBillItemUpdateOut, headerV0);
            SCMEnv.showTime(lTime, "sndmo.billItemOutandCheck:");

            SCMEnv.out("修改状态：整单出库修改");
          }
        }
        if ((aryBillItemDelOut != null) && (aryBillItemDelOut.size() > 0))
        {
          SCMEnv.out("修改状态：整单出库取消");
          lTime = System.currentTimeMillis();

          aryReturn = sndmo.billAllOutCancelCheck(sHeaderHid);
          SCMEnv.showTime(lTime, "sndmo.billAllOutCancelCheck:");

          if ((aryReturn == null) || (aryReturn.size() == 0))
          {
            lTime = System.currentTimeMillis();
            sndmo.billAllOutCancel(sHeaderHid);
            SCMEnv.showTime(lTime, "sndmo.billAllOutCancel:");
            SCMEnv.out("修改状态：整单出库取消完成");
          }

        }

        if ((aryBillItemOut != null) && (aryBillItemOut.size() > 0))
        {
          lTime = System.currentTimeMillis();
          aryOutorFreeze = sndmo.billItemOutandCheck(aryBillItemOut, headerV0);
          SCMEnv.showTime(lTime, "sndmo1.billItemOutandCheck:");
          SCMEnv.out("修改状态：新增出库");
        }
      }

    }
    else
    {
      if ((aryBillItemDelIn != null) && (aryBillItemDelIn.size() > 0))
      {
        lTime = System.currentTimeMillis();
        aryOutorFreeze = sndmo.billItemInCancelCheck(getBillItemAryID(aryBillItemDelIn));
        SCMEnv.showTime(lTime, "sndmo.billItemInCancelCheck:");

        lTime = System.currentTimeMillis();
        if ((aryOutorFreeze == null) || (aryOutorFreeze.size() == 0))
          sndmo.billItemInCancel(aryBillItemDelIn);
        SCMEnv.showTime(lTime, "sndmo.billItemInCancel:");
        SCMEnv.out("修改状态：删除入库");
      }
      if ((aryBillItemDelOut != null) && (aryBillItemDelOut.size() > 0))
      {
        lTime = System.currentTimeMillis();
        ArrayList aryBillItemDelID = getBillItemAryID(aryBillItemDelOut);
        aryReturn = sndmo.billItemOutCancelCheck(aryBillItemDelID);
        SCMEnv.showTime(lTime, "sndmo.billItemOutCancelCheck");

        lTime = System.currentTimeMillis();
        if ((aryReturn == null) || (aryReturn.size() == 0)) {
          sndmo.billItemOutCancel(aryBillItemDelID);
        }
        SCMEnv.showTime(lTime, "sndmo.billItemOutCancel:");

        SCMEnv.out("修改状态：删除出库");
      }

      if ((aryBillItemUpdateIn != null) && (aryBillItemUpdateIn.size() > 0))
      {
        lTime = System.currentTimeMillis();
        aryOutorFreeze = sndmo.billItemInCancelCheck(getBillItemAryID(aryBillItemUpdateIn));
        SCMEnv.showTime(lTime, "sndmo.billItemInCancelCheck:");
        if ((aryOutorFreeze == null) || (aryOutorFreeze.size() == 0))
        {
          lTime = System.currentTimeMillis();
          sndmo.billItemInCancel(aryBillItemUpdateIn);
          SCMEnv.showTime(lTime, "billItemInCancel");

          lTime = System.currentTimeMillis();
          sndmo.billItemIn(aryBillItemUpdateIn, headerV0);
          SCMEnv.showTime(lTime, "billItemIn");

          lTime = System.currentTimeMillis();
          aryEx = sndmo.billItemInCheck(getBillItemAryID(aryBillItemUpdateIn));
          SCMEnv.showTime(lTime, "sndmo.billItemInCheck:");

          SCMEnv.out("修改状态：入库->入库修改");
        }
      }
      if ((aryBillItemUpdateOut != null) && (aryBillItemUpdateOut.size() > 0))
      {
        ArrayList alUpdateItemID = getBillItemAryID(aryBillItemUpdateOut);
        lTime = System.currentTimeMillis();
        aryReturn = sndmo.billItemOutCancelCheck(alUpdateItemID);
        SCMEnv.showTime(lTime, "sndmo.billItemOutCancelCheck:");

        if ((aryReturn == null) || (aryReturn.size() == 0))
        {
          sndmo.billItemOutCancel(alUpdateItemID);
          lTime = System.currentTimeMillis();
          aryOutorFreeze = sndmo.billItemOutandCheck(aryBillItemUpdateOut, headerV0);
          SCMEnv.showTime(lTime, "sndmo.billItemOutandCheck:");
          SCMEnv.out("修改状态：出库->出库修改");
        }

      }

      if ((aryBillItemIn != null) && (aryBillItemIn.size() > 0))
      {
        lTime = System.currentTimeMillis();
        sndmo.billItemIn(aryBillItemIn, headerV0);
        SCMEnv.showTime(lTime, "sndmo.billItemIn:");

        lTime = System.currentTimeMillis();
        aryEx = sndmo.billItemInCheck(getBillItemAryID(aryBillItemIn));
        SCMEnv.showTime(lTime, "sndmo.billItemInCheck:");

        SCMEnv.out("修改状态：新增入库");
      }
      if ((aryBillItemOut != null) && (aryBillItemOut.size() > 0))
      {
        lTime = System.currentTimeMillis();
        aryOutorFreeze = sndmo.billItemOutandCheck(aryBillItemOut, headerV0);
        SCMEnv.showTime(lTime, "sndmo2.billItemOutandCheck:");
        SCMEnv.out("修改状态：新增出库");
      }
    }

    String sMsg = getSnHintMsg(aryEx, null, aryReturn, aryOutorFreeze);
    return sMsg;
  }

  /** @deprecated */
  protected ArrayList updateThisBill(GeneralBillVO voUpdatedBill)
    throws Exception
  {
    if (voUpdatedBill == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000182"));
    }
    GeneralBillHeaderVO voUpdatedHeader = voUpdatedBill.getHeaderVO();

    GeneralBillItemVO[] voaUpdatedItem = voUpdatedBill.getItemVOs();

    if ((voUpdatedHeader == null) || (voaUpdatedItem == null) || (voaUpdatedItem.length == 0) || (voUpdatedHeader.getCbilltypecode() == null) || (voUpdatedHeader.getPk_corp() == null) || (voUpdatedHeader.getCbilltypecode().trim().length() == 0) || (voUpdatedHeader.getPk_corp().trim().length() == 0) || (voUpdatedHeader.getPrimaryKey() == null))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000172"));
    }

    String sBillPK = voUpdatedHeader.getPrimaryKey().trim();

    GeneralBillDMO dmoBill = getBillDMO();
    GeneralBillHeaderVO voDbBillHeader = dmoBill.queryBillHead(sBillPK);

    GeneralBillItemVO[] voaDbBillItem = dmoBill.queryBillItemByBillPk(sBillPK, true, true);

    if ((voDbBillHeader == null) || ((voDbBillHeader.getPk_corp() == null) && (voDbBillHeader.getPrimaryKey() == null)))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000183"));
    }
    GeneralBillVO voOldBill = new GeneralBillVO();
    voOldBill.setParentVO(voDbBillHeader);
    voOldBill.setChildrenVO(voaDbBillItem);

    return updateThisBill(voUpdatedBill, voOldBill);
  }

  protected ArrayList updateThisBill(GeneralBillVO voUpdatedBill, GeneralBillVO voOld)
    throws Exception
  {
    return updateThisBill_Batch(voUpdatedBill, voOld);
  }

  protected ArrayList updateThisBill_Batch(GeneralBillVO voUpdatedBill, GeneralBillVO voOld)
    throws Exception
  {
    if ((voUpdatedBill == null) || (voOld == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000182"));
    }
    GeneralBillHeaderVO voUpdatedHeader = voUpdatedBill.getHeaderVO();

    GeneralBillItemVO[] voaUpdatedItem = voUpdatedBill.getItemVOs();

    GeneralBillItemVO[] voaOldItem = voOld.getItemVOs();

    if ((voUpdatedHeader == null) || (voaUpdatedItem == null) || (voaUpdatedItem.length == 0) || (voaOldItem == null) || (voaOldItem.length == 0) || (voUpdatedHeader.getCbilltypecode() == null) || (voUpdatedHeader.getPk_corp() == null) || (voUpdatedHeader.getCbilltypecode().trim().length() == 0) || (voUpdatedHeader.getPk_corp().trim().length() == 0) || (voUpdatedHeader.getPrimaryKey() == null))
    {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000172"));
    }
    Hashtable htOldItems = new Hashtable();

    for (int i = 0; i < voaOldItem.length; ++i)
      if ((voaOldItem[i] != null) && (voaOldItem[i].getPrimaryKey() != null))
        htOldItems.put(voaOldItem[i].getPrimaryKey(), voaOldItem[i]);
      else
        SCMEnv.out("fatal:old bill item null");
    Timer t = new Timer();

    String sBodyPK = null;

    String sBillPK = voUpdatedHeader.getPrimaryKey().trim();

    String sCorpID = voUpdatedHeader.getPk_corp().trim();

    voUpdatedHeader.setFbillflag(new Integer("2"));

    GeneralBillDMO dmoBill = getBillDMO();

    LocatorDMO dmoLoc = new LocatorDMO();

    SettlementDMO dmoStl = new SettlementDMO();

    ArrayList alNewPK = dmoBill.setBillPrimaryKeys(voUpdatedBill);

    ISysInitQry sysdmo = (ISysInitQry)NCLocator.getInstance().lookup(ISysInitQry.class.getName());

    String IC010 = null;

    IC010 = sysdmo.getParaString(sCorpID, "IC010");

    if ((IC010 != null) && (IC010.equals("Y"))) {
      t.start();
      dmoBill.copyBill((GeneralBillVO)voOld.clone());
      t.stopAndShow("copybill@@--");
    }

    String cbilltypecode = voUpdatedHeader.getCbilltypecode();

    String sSnHintMsg = null;

    t.start();
    Vector vNew = new Vector();
    Vector vUpdate = new Vector();
    Vector vDelete = new Vector();
    Vector vNewLoc = new Vector();
    Vector vItemForDel = new Vector();
    Vector vBarCodeDel = new Vector();
    GeneralBillItemVO[] voItems = voUpdatedBill.getItemVOs();

    for (int i = 0; i < voItems.length; ++i)
    {
      voItems[i].setCgeneralhid(sBillPK);

      if (voItems[i].getStatus() == 2) {
        vNew.add(voItems[i]);
      }
      else if (voItems[i].getStatus() == 1) {
        vUpdate.add(voItems[i]);
        if ((voItems[i].getLocator() != null) || ((htOldItems != null) && (htOldItems.containsKey(voItems[i].getPrimaryKey())) && (((GeneralBillItemVO)htOldItems.get(voItems[i].getPrimaryKey())).getLocator() != null)))
        {
          vItemForDel.add(voItems[i]);
        }
      }
      else if (voItems[i].getStatus() == 3) {
        vDelete.add(voItems[i]);
        vBarCodeDel.add(voItems[i]);
        if (voItems[i].getLocator() != null)
          vItemForDel.add(voItems[i]);
      }
      else if ((voItems[i].getStatus() == 0) && 
        (voItems[i].getLocator() != null) && (voItems[i].getLocStatus() != 0))
      {
        vItemForDel.add(voItems[i]);
      }

      if ((voItems[i].getStatus() != 2) && (voItems[i].getStatus() != 1) && (((voItems[i].getLocator() == null) || ((voItems[i].getLocStatus() != 2) && (voItems[i].getLocStatus() != 1))))) {
        continue;
      }
      LocatorVO[] voLocs = voItems[i].getLocator();
      if (voLocs != null) {
        for (int j = 0; j < voLocs.length; ++j) {
          vNewLoc.add(voLocs[j]);
        }

      }

    }

    if (vNew.size() > 0) {
      GeneralBillItemVO[] voInsertItems = new GeneralBillItemVO[vNew.size()];
      vNew.copyInto(voInsertItems);

      for (int i = 0; i < voInsertItems.length; ++i) {
        voInsertItems[i].calBarcodeNum();
      }
      dmoBill.insertItemBatch(voInsertItems);
      if (dmoStl.hasBB3(cbilltypecode)) {
        dmoStl.insertBB3Batch(voInsertItems);
      }
    }

    if (vUpdate.size() > 0) {
      GeneralBillItemVO[] voUpdateItems = new GeneralBillItemVO[vUpdate.size()];
      vUpdate.copyInto(voUpdateItems);

      for (int i = 0; i < voUpdateItems.length; ++i) {
        voUpdateItems[i].calBarcodeNum();
      }
      dmoBill.updateItemBatch(voUpdateItems);
    }

    deleteBarCodeAllBill(voUpdatedBill);
    insertBarCode(voUpdatedBill, true);

    ArrayList alUpdateByBarcodeItems = getBarcodeUPdateItem(voUpdatedBill.getItemVOs(), true);

    dmoBill.updateItemBarcodeNumBatch(alUpdateByBarcodeItems);

    if (vItemForDel.size() > 0) {
      GeneralBillItemVO[] voDelItems = new GeneralBillItemVO[vItemForDel.size()];
      vItemForDel.copyInto(voDelItems);

      dmoLoc.deleteLocatorBatch(voDelItems);
    }

    if (vNewLoc.size() > 0) {
      LocatorVO[] voLocs = new LocatorVO[vNewLoc.size()];
      vNewLoc.copyInto(voLocs);
      dmoLoc.insertLocatorBatch(voLocs);
    }

    t.stopAndShow("modify item,loc,sn,...@@--");

    String sMsg = snUpdate(voUpdatedBill, htOldItems, voOld);
    StringBuffer sbAllErrMsg = new StringBuffer();

    if ((sMsg != null) && (sMsg.length() > 0)) {
      sbAllErrMsg.append(sMsg);
    }
    if (sbAllErrMsg.toString().length() > 0) {
      throw new BusinessException(sbAllErrMsg.toString());
    }

    if (vDelete.size() > 0) {
      GeneralBillItemVO[] voDeleteItems = new GeneralBillItemVO[vDelete.size()];
      vDelete.copyInto(voDeleteItems);
      if (dmoStl.hasBB3(cbilltypecode)) {
        dmoStl.deleteBB3Batch(voDeleteItems);
      }
      dmoBill.deleteItemBatch(voDeleteItems);

      deleteBarcodeBillItems(voDeleteItems);
    }

    dmoBill.updateHeader(voUpdatedHeader);

    t.start();
    freshTs(voUpdatedBill, sBillPK);
    t.stopAndShow("fresh ts@@--");

    t.start();
    synchronized (this) {
      modifyOnhandNum(voUpdatedBill, voOld);
    }
    t.stopAndShow("m num@@--");

    return alNewPK;
  }

  public void checkLocator(GeneralBillVO voBill)
    throws BusinessException
  {
    if ((voBill == null) || (voBill.getChildrenVO() == null) || (voBill.getItemVOs().length == 0)) {
      return;
    }
    String sBillTypeCode = voBill.getHeaderVO().getCbilltypecode();
    if ((sBillTypeCode == null) || (sBillTypeCode.equals("42")) || (sBillTypeCode.equals("43")) || (sBillTypeCode.equals("4P")) || (sBillTypeCode.equals("4X"))) {
      return;
    }

    String sBillBusiType = voBill.getHeaderVO().getCbiztypeid();
    if ((sBillBusiType != null) && (sBillBusiType.length() > 0)) {
      String sCorpID = voBill.getHeaderVO().getPk_corp();
      Hashtable htBusitype = null;
      try {
        OnhandnumDMO dmoOnhand = new OnhandnumDMO();
        htBusitype = dmoOnhand.queryBusitype(sCorpID);
      }
      catch (Exception e)
      {
      }
      if ((htBusitype != null) && (htBusitype.containsKey(sBillBusiType))) {
        return;
      }
    }

    boolean bIsLocatorMgt = false;
    try
    {
      if (voBill.getHeaderVO() != null) {
        String whid = voBill.getHeaderVO().getCwarehouseid();

        QueryInfoDMO dmo = new QueryInfoDMO();
        WhVO vowh = dmo.getWhInfo(whid);
        voBill.setWh(vowh);
        boolean isLoc = false;
        if ((vowh != null) && (vowh.getIsLocatorMgt() != null) && (vowh.getIsLocatorMgt().intValue() == 1)) {
          isLoc = true;
        }
        ArrayList alroaderr = new ArrayList();

        ArrayList alcor = new ArrayList();

        ArrayList alcorin = new ArrayList();

        ArrayList alerr = new ArrayList();
        ArrayList alnotloc = new ArrayList();

        GeneralBillItemVO[] voItems = voBill.getItemVOs();

        String sRmspace = null;
        if (isLoc) {
          sRmspace = dmo.getRmCarg(whid);
        }
        UFBoolean isonroad = null;

        for (int i = 0; i < voItems.length; ++i) {
          if ((voItems[i].getNinnum() == null) && (voItems[i].getNoutnum() == null)) {
            continue;
          }
          isonroad = (UFBoolean)voItems[i].getAttributeValue("bonroadflag");

          if ((isonroad != null) && (isonroad.booleanValue()))
          {
            if (!isLoc) {
              alnotloc.add(voItems[i].getCrowno());
            }

            if (sRmspace != null) {
              LocatorVO[] voLocs = voItems[i].getLocator();
              if ((voLocs != null) && (voLocs.length > 0)) {
                for (int j = 0; j < voLocs.length; ++j) {
                  if ((voLocs[j].getCspaceid() != null) && (!sRmspace.equals(voLocs[j].getCspaceid()))) {
                    alerr.add(voItems[i].getCrowno());
                    break;
                  }
                }

              }
              else
              {
                voItems[i].getCspaceid();
                voItems[i].setCspaceid(sRmspace);
                voItems[i].synLocator();
                if ((voItems[i].getLocator() != null) && (voItems[i].getLocator().length > 0)) {
                  voItems[i].getLocator()[0].setCwarehouseid(whid);

                  SerialVO[] serVO = voItems[i].getSerial();

                  if (serVO != null) {
                    for (int p = 0; p < serVO.length; ++p) {
                      serVO[p].setCspaceid(sRmspace);
                    }
                    voItems[i].setSerial(serVO);
                  }

                  if (voItems[i].getStatus() == 0)
                    voItems[i].setStatus(1);
                }
              }
            }
            else {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000483"));
            }

            if ((voItems[i].getInOutFlag() == -1) && 
              (voItems[i].getCcorrespondbid() == null)) {
              alcor.add(voItems[i].getCrowno());
            }

          }
          else if (sRmspace != null) {
            LocatorVO[] voLocs = voItems[i].getLocator();
            if ((voLocs != null) && (voLocs.length > 0)) {
              for (int j = 0; j < voLocs.length; ++j) {
                if (sRmspace.equals(voLocs[j].getCspaceid())) {
                  alroaderr.add(voItems[i].getCrowno());
                  break;
                }

              }

            }

          }

          if ((voItems[i].getInOutFlag() != 1) || 
            (voItems[i].getCcorrespondbid() == null)) continue;
          alcorin.add(voItems[i].getCrowno());
        }

        if (alroaderr.size() > 0)
        {
          StringBuffer smsg = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000484") + voBill.getHeaderVO().getVbillcode() + ">\n");
          for (int i = 0; i < alroaderr.size(); ++i) {
            if (i > 0)
              smsg.append(",");
            smsg.append(alroaderr.get(i).toString());
          }
          throw new BusinessException(smsg.toString());
        }
        if (alerr.size() > 0) {
          StringBuffer smsg = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000485") + voBill.getHeaderVO().getVbillcode() + ">\n");
          for (int i = 0; i < alerr.size(); ++i) {
            if (i > 0)
              smsg.append(",");
            smsg.append(alerr.get(i).toString());
          }
          throw new BusinessException(smsg.toString());
        }
        if (alcor.size() > 0) {
          StringBuffer smsg = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000486") + voBill.getHeaderVO().getVbillcode() + ">\n");
          for (int i = 0; i < alcor.size(); ++i) {
            if (i > 0)
              smsg.append(",");
            smsg.append(alcor.get(i).toString());
          }
          throw new BusinessException(smsg.toString());
        }

        if (alcorin.size() > 0) {
          StringBuffer smsg = new StringBuffer(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000487") + voBill.getHeaderVO().getVbillcode() + ">\n");
          for (int i = 0; i < alcorin.size(); ++i) {
            if (i > 0)
              smsg.append(",");
            smsg.append(alcorin.get(i).toString());
          }
          throw new BusinessException(smsg.toString());
        }
        if (alnotloc.size() > 0) {
          StringBuffer smsg = new StringBuffer("不是货位管理的仓库，不能打在途标记！请清除<" + voBill.getHeaderVO().getVbillcode() + ">\n");
          for (int i = 0; i < alnotloc.size(); ++i) {
            if (i > 0)
              smsg.append(",");
            smsg.append(alnotloc.get(i).toString());
          }
          throw new BusinessException(smsg.toString());
        }

        appendInDefaultSpace(voBill);

        VOCheck.checkSpaceInput(voBill, new Integer(voBill.getBillInOutFlag()));
      }
    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
  }

  private void saveBatchCode(GeneralBillVO vo) throws BusinessException
  {
    BatchcodeImpl impl = new BatchcodeImpl();
    impl.checkandSaveBatchcode(vo);
  }

  private void appendInDefaultSpace(GeneralBillVO vo)
    throws BusinessException
  {
    GeneralBillHeaderVO hvo = (GeneralBillHeaderVO)vo.getParentVO();

    if ((hvo.getIsLocatorMgt() == null) || (hvo.getIsLocatorMgt().intValue() != 1)) {
      return;
    }

    String sBillTypeCode = hvo.getCbilltypecode();
    if ((sBillTypeCode == null) || (sBillTypeCode.equals("42")) || (sBillTypeCode.equals("43")) || (sBillTypeCode.equals("4P")) || (sBillTypeCode.equals("4X"))) {
      return;
    }

    GeneralBillItemVO[] bvos = (GeneralBillItemVO[])(GeneralBillItemVO[])vo.getChildrenVO();
    LocatorVO[] voLocs = null;
    if ((hvo == null) || (bvos == null)) {
      return;
    }
    ArrayList alinv = new ArrayList();
    for (int i = 0; i < bvos.length; ++i)
    {
      if ((bvos[i].getInOutFlag() == 1) && (bvos[i].getLocator() == null)) {
        alinv.add(bvos[i].getCinventoryid());
      }
    }

    if (alinv.size() <= 0) {
      return;
    }
    HashMap hmSpace = null;
    try
    {
      String[] invids = new String[alinv.size()];
      alinv.toArray(invids);
      hmSpace = new StorectlDMO().getDefaultSpace(invids, vo.getHeaderVO().getCwarehouseid());
    } catch (Exception e) {
      nc.bs.ic.pub.GenMethod.throwBusiException(e);
    }

    if ((hmSpace == null) || (hmSpace.size() == 0)) {
      return;
    }
    for (int row = 0; row < bvos.length; ++row) {
      voLocs = bvos[row].getLocator();
      if ((voLocs != null) && (voLocs.length != 0))
        continue;
      bvos[row].setCspaceid((String)hmSpace.get(bvos[row].getCinventoryid()));
      bvos[row].synLocator();

      if ((bvos[row].getLocator() != null) && 
        (bvos[row].getLocator()[0] != null)) {
        bvos[row].getLocator()[0].setCwarehouseid(hvo.getCwarehouseid());
      }
      if (bvos[row].getLocStatus() == 0)
        bvos[row].setLocStatus(1);
    }
  }
}