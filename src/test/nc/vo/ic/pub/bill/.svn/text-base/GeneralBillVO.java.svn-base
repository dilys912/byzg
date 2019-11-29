package nc.vo.ic.pub.bill;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.function.IFuncPower;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.ic.ic004.StoreadminBodyVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.DesassemblyVO;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.ILockIC;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillHeaderVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillItemVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.ic.pub.sn.SerialVO;
import nc.vo.ic.service.IICStoreAdminData;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pub.IBillCode;
import nc.vo.scm.pub.IscmDefCheckVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.bill.IExamAVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.sm.log.OperatelogVO;

public class GeneralBillVO extends AggregatedValueObject
  implements IBillCode, ILockIC, IExamAVO, IICStoreAdminData, IscmDefCheckVO
{
  private static String[] m_saAllBillTypeCode = { BillTypeConst.m_purchaseIn, BillTypeConst.m_productIn, BillTypeConst.m_consignMachiningIn, BillTypeConst.m_otherIn, BillTypeConst.m_saleOut, BillTypeConst.m_materialOut, BillTypeConst.m_consignMachiningOut, BillTypeConst.m_otherOut, BillTypeConst.m_discardOut, BillTypeConst.m_entrustMachiningIn, BillTypeConst.m_borrowIn, BillTypeConst.m_lendOut, BillTypeConst.m_borrowOut, BillTypeConst.m_lendIn, BillTypeConst.m_wasterProcess, BillTypeConst.m_entrustMachiningOut, BillTypeConst.m_allocationIn, BillTypeConst.m_allocationOut, BillTypeConst.m_initIn, BillTypeConst.m_initBorrow, BillTypeConst.m_initLend, BillTypeConst.m_initWaster, BillTypeConst.m_initEntrustMachining, BillTypeConst.m_spaceAdjust };

  private static String[] m_saAllBillTypeName = { "采购入库单", "产成品入库单", "委托加工收货单", "其他入库单", "销售出库单", "材料出库单", "委托加工发料单", "其他出库单", "报废单", "来料加工入库单", "借入单", "借出单", "借入还回单", "借出还回单", "废品处理单", "来料加工出库单", "调拨入库单", "调拨出库单", "期初余额", "期初借入单", "期初借出单", "期初废品单", "期初来料加工单", "货位调整单" };
  public static final int QRY_FIRST_ITEM_NUM = 20;
  public static final int QRY_FULL_BILL = 0;
  public static final int QRY_FULL_BILL_PURE = 500;
  public static final int QRY_HEAD_ONLY = 100;
  public static final int QRY_HEAD_ONLY_PURE = 300;
  public static final int QRY_ITEM_ONLY = 200;
  public static final int QRY_ITEM_ONLY_PURE = 400;
  long currentTimestamp;
  long initialTimestamp;
  ArrayList m_alLockedPK = null;

  private UFBoolean m_bBodyParsed = new UFBoolean(false);

  private UFBoolean m_bHeadParsed = new UFBoolean(false);

  ClientLink m_clientlink = null;

  ControlInfo m_controlinfo = null;

  private boolean isCheckCredit = true;
  private boolean isCheckPeriod = true;
  private boolean isRwtPuUserConfirmFlag = false;
  private boolean isGetPlanPriceAtBs = true;

  public boolean m_bAutoSendBarcodes = false;

  private boolean m_bBarcodeNumVerify = true;

  public boolean m_bHaveCalConvRate = false;

  private boolean m_bSaveBadBarcode = true;

  private boolean m_bSaveBarcode = true;

  public boolean m_bSaveBarcodeDelFirst = false;
  public int m_iActionInt;
  private OperatelogVO m_OperatelogVO;
  public String m_sAccreditUserID;
  public String m_sActionCode = null;
  public String m_sErr = null;
  public SMGeneralBillVO m_SmGenBillVO;
  private GeneralBillItemVO[] m_voaItem = null;
  private GeneralBillHeaderVO m_voHeader = null;
  public GeneralBillVO m_voOld = null;

  final UFDouble ZERO = new UFDouble(0.0D);

  private String sLockKey = null;

  public ArrayList getInvs()
  {
    ArrayList alInv = null;
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      alInv = new ArrayList();
      for (int i = 0; i < this.m_voaItem.length; i++) {
        if (this.m_voaItem[i] != null)
          alInv.add(this.m_voaItem[i].getInv());
      }
    }
    return alInv;
  }

  public void setHeadParsed(UFBoolean newHeadParsed) {
    this.m_bHeadParsed = newHeadParsed;
  }

  public void setBodyParsed(UFBoolean newBodyParsed) {
    this.m_bBodyParsed = newBodyParsed;
  }

  public void setControlinfo(ControlInfo newM_controlinfo)
  {
    this.m_controlinfo = newM_controlinfo;
  }

  public void setClientlink(ClientLink newM_clientlink) {
    this.m_clientlink = newM_clientlink;
    if ((this.m_voHeader != null) && (newM_clientlink != null)) {
      String userid = this.m_clientlink.getUser();
      this.m_voHeader.setCoperatorid(userid);
      this.m_voHeader.setCoperatoridnow(userid);
      this.m_voHeader.setClogdatenow(this.m_clientlink.getLogonDate().toString());
    }
  }

  public UFBoolean getBodyParsed() {
    return this.m_bBodyParsed;
  }

  public UFBoolean getHeadParsed()
  {
    return this.m_bHeadParsed;
  }

  public static GeneralBillVO combine(GeneralBillVO billvonew, GeneralBillVO billvoold)
  {
    GeneralBillVO voNewBill = null;
    GeneralBillVO voOldBill = null;

    if (billvonew != null)
    {
      voNewBill = (GeneralBillVO)billvonew.clone();
      voNewBill.sumInvQtyNotUseDelRow();
    }
    if (billvoold != null)
    {
      voOldBill = (GeneralBillVO)billvoold.clone();

      voOldBill.setStatus(0);
      voOldBill.sumInvQtyNotUseDelRow();
    }

    if (voNewBill != null)
    {
      if (voOldBill != null)
        voNewBill.sub(voOldBill.getItemVOs());
      return voNewBill;
    }
    if (voOldBill != null)
    {
      voOldBill.neg();
      return voOldBill;
    }

    SCMEnv.out("no any bills.");
    return null;
  }

  public void setIsCheckAtp(boolean y)
  {
    UFBoolean isy = new UFBoolean(y);
    if (this.m_voHeader != null) {
      this.m_voHeader.setAttributeValue("ischeckatp", isy);
    }
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
    {
      for (int i = 0; i < this.m_voaItem.length; i++)
        this.m_voaItem[i].setAttributeValue("ischeckatp", isy);
    }
  }

  public static int getBillInOutFlag(String sBillTypeCode)
  {
    int iBillInOut = 1;
    if (sBillTypeCode == null)
      return 0;
    sBillTypeCode = sBillTypeCode.trim();

    if ((sBillTypeCode.equals(BillTypeConst.m_initIn)) || (sBillTypeCode.equals(BillTypeConst.m_initBorrow)) || (sBillTypeCode.equals(BillTypeConst.m_initWaster)) || (sBillTypeCode.equals(BillTypeConst.m_initEntrustMachining)) || (sBillTypeCode.equals(BillTypeConst.m_purchaseIn)) || (sBillTypeCode.equals(BillTypeConst.m_productIn)) || (sBillTypeCode.equals(BillTypeConst.m_consignMachiningIn)) || (sBillTypeCode.equals(BillTypeConst.m_entrustMachiningIn)) || (sBillTypeCode.equals(BillTypeConst.m_borrowIn)) || (sBillTypeCode.equals(BillTypeConst.m_otherIn)) || (sBillTypeCode.equals(BillTypeConst.m_lendIn)) || (sBillTypeCode.equals(BillTypeConst.m_purchaseInit)) || (sBillTypeCode.equals(BillTypeConst.m_allocationIn)))
    {
      iBillInOut = 1;
    }
    else if ((sBillTypeCode.equals(BillTypeConst.m_saleOut)) || (sBillTypeCode.equals(BillTypeConst.m_materialOut)) || (sBillTypeCode.equals(BillTypeConst.m_consignMachiningOut)) || (sBillTypeCode.equals(BillTypeConst.m_entrustMachiningOut)) || (sBillTypeCode.equals(BillTypeConst.m_lendOut)) || (sBillTypeCode.equals(BillTypeConst.m_otherOut)) || (sBillTypeCode.equals(BillTypeConst.m_borrowOut)) || (sBillTypeCode.equals(BillTypeConst.m_discardOut)) || (sBillTypeCode.equals(BillTypeConst.m_wasterProcess)) || (sBillTypeCode.equals(BillTypeConst.m_initLend)) || (sBillTypeCode.equals(BillTypeConst.m_allocationOut)))
    {
      iBillInOut = -1;
    }
    else iBillInOut = 0;
    return iBillInOut;
  }

  public static int getBillItemInOutPty(GeneralBillHeaderVO voHeader, GeneralBillItemVO voItem)
  {
    if ((voHeader == null) || (voItem == null))
      return 0;
    int iInoutPry = 1;

    int iBillInOut = getBillInOutFlag(voHeader.getCbilltypecode());

    if (voItem != null)
    {
      if ((1 == iBillInOut) && (voItem.getNinnum() != null)) {
        if (voItem.getNinnum().doubleValue() >= 0.0D)
          iInoutPry = 1;
        else {
          iInoutPry = -1;
        }

      }
      else if ((-1 == iBillInOut) && (voItem.getNoutnum() != null)) {
        if (voItem.getNoutnum().doubleValue() >= 0.0D)
          iInoutPry = -1;
        else
          iInoutPry = 1;
      }
    }
    return iInoutPry;
  }

  public static String getBillTypeCode(String sBillName)
  {
    if (sBillName != null)
    {
      for (int j = 0; j < m_saAllBillTypeName.length; j++)
        if (m_saAllBillTypeName[j].equals(sBillName.trim()))
        {
          return m_saAllBillTypeCode[j];
        }
    }
    return null;
  }

  public static String getBillTypeName(String sBillCode)
  {
    if (sBillCode != null)
    {
      for (int j = 0; j < m_saAllBillTypeCode.length; j++)
        if (m_saAllBillTypeCode[j].equals(sBillCode.trim()))
        {
          return m_saAllBillTypeName[j];
        }
    }
    return null;
  }

  public GeneralBillVO() {
    this.m_voHeader = new GeneralBillHeaderVO();
    this.m_voaItem = new GeneralBillItemVO[0];
  }
  public GeneralBillVO(int iRowCount) {
    this.m_voHeader = new GeneralBillHeaderVO();
    this.m_voaItem = new GeneralBillItemVO[iRowCount];
    for (int i = 0; i < iRowCount; i++)
      this.m_voaItem[i] = new GeneralBillItemVO();
  }

  public boolean bSaveBarcodeFinal()
  {
    String pkCorp = getPk_corp();

    IFuncPower bo = (IFuncPower)NCLocator.getInstance().lookup(IFuncPower.class.getName());
    try {
      boolean bRightCheck = bo.isPowerByUserFunc(getAccreditUserID(), pkCorp, "40080201");

      if ((isSaveBarcode()) && ((isBarcodeNumVerify()) || ((isSaveBadBarcode()) && (!isBarcodeNumVerify())) || ((bRightCheck) && (!isBarcodeNumVerify()))))
      {
        return true;
      }
    }
    catch (BusinessException e)
    {
      e.printStackTrace();
    }
    return false;
  }

  public void calConvRate()
  {
    if (!this.m_bHaveCalConvRate) {
      this.m_bHaveCalConvRate = true;
      if (this.m_voaItem != null)
        for (int i = 0; i < this.m_voaItem.length; i++)
          if (this.m_voaItem[i] != null)
            this.m_voaItem[i].calConvRate();
    }
  }

  public void calPrdDate()
  {
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++)
        if (this.m_voaItem[i] != null)
          this.m_voaItem[i].calPrdDate();
  }

  public void clearInvQtyInfo()
  {
    if (this.m_voaItem != null)
      for (int line = 0; line < this.m_voaItem.length; line++)
        clearInvQtyInfo(line);
  }

  public void clearInvQtyInfo(int line)
  {
    if ((this.m_voaItem != null) && (line >= 0) && (this.m_voaItem.length > line)) {
      this.m_voaItem[line].setBkxcl(null);
      this.m_voaItem[line].setXczl(null);
      this.m_voaItem[line].setNmaxstocknum(null);
      this.m_voaItem[line].setNminstocknum(null);
      this.m_voaItem[line].setNsafestocknum(null);
      this.m_voaItem[line].setNorderpointnum(null);

      this.m_voaItem[line].getInv().setChzl(null);
    }
  }

  public void clearItem(int line)
  {
    if ((this.m_voaItem != null) && (line >= 0) && (this.m_voaItem.length > line)) {
      GeneralBillItemVO voNew = new GeneralBillItemVO();

      if (this.m_voHeader != null)
        voNew.setCgeneralhid(this.m_voHeader.getPrimaryKey());
      voNew.setPrimaryKey(this.m_voaItem[line].getPrimaryKey());

      voNew.setCrowno(this.m_voaItem[line].getCrowno());

      this.m_voaItem[line] = voNew;
    }
  }

  public void clearRedundantQty(int iInOut)
  {
    if (this.m_voaItem != null)
    {
      for (int line = 0; line < this.m_voaItem.length; line++) {
        if (this.m_voaItem[line] == null)
          continue;
        this.m_voaItem[line].clearRedundantQty(iInOut);
      }
    }
  }

  public Object clone()
  {
    try
    {
      if ((this.m_voHeader == null) || (this.m_voaItem == null)) {
        return null;
      }

      return ObjectUtils.serializableClone(this);
    } catch (Exception e) {
    }
    return null;
  }

  public GeneralBillItemVO[] compare(GeneralBillItemVO[] voaBase, GeneralBillItemVO[] voaCompItem)
  {
    if ((voaBase == null) || (voaBase.length == 0)) {
      if ((voaCompItem == null) || (voaCompItem.length == 0)) {
        for (int i = 0; i < voaCompItem.length; i++)
          voaCompItem[i].setStatus(2);
        return voaCompItem;
      }
      return null;
    }

    if ((voaCompItem == null) || (voaCompItem.length == 0)) {
      if ((voaBase == null) || (voaBase.length == 0)) {
        for (int i = 0; i < voaBase.length; i++)
          voaBase[i].setStatus(3);
        return voaBase;
      }
      return null;
    }

    voaBase = sumInvQty(voaBase);
    voaCompItem = sumInvQty(voaCompItem);

    int iBillInOutFlag = getBillInOutFlag();

    Hashtable htBodyKey = new Hashtable();
    for (int i = 0; i < voaCompItem.length; i++) {
      if ((voaCompItem[i] == null) || (htBodyKey.containsKey(voaCompItem[i].getPrimaryKey())))
        continue;
      htBodyKey.put(voaCompItem[i].getPrimaryKey(), "" + i);
    }
    int iLineNum2 = 0;
    Object oValueTemp = null;
    Vector vResult = new Vector();
    for (int i = 0; i < voaBase.length; i++) {
      if (voaBase[i] == null)
      {
        continue;
      }
      oValueTemp = null;
      oValueTemp = htBodyKey.get(voaBase[i].getPrimaryKey());

      if (oValueTemp == null) {
        voaBase[i].setStatus(3);
        vResult.addElement(voaBase[i]);
      }
      else {
        iLineNum2 = Integer.valueOf(oValueTemp.toString()).intValue();

        htBodyKey.remove(voaBase[i].getPrimaryKey());

        if (iBillInOutFlag == 1) {
          if (voaBase[i].getNinnum() != null) {
            if (voaCompItem[iLineNum2].getNinnum() != null) {
              voaBase[i].setNinnum(voaBase[i].getNinnum().sub(voaCompItem[iLineNum2].getNinnum()));
            }
          }
          else if (voaCompItem[iLineNum2].getNinnum() != null) {
            voaBase[i].setNinnum(this.ZERO.sub(voaCompItem[iLineNum2].getNinnum()));
          }

          if (voaBase[i].getNinassistnum() != null) {
            if (voaCompItem[iLineNum2].getNinassistnum() != null) {
              voaBase[i].setNinassistnum(voaBase[i].getNinassistnum().sub(voaCompItem[iLineNum2].getNinassistnum()));
            }

          }
          else if (voaCompItem[iLineNum2].getNinassistnum() != null) {
            voaBase[i].setNinassistnum(this.ZERO.sub(voaCompItem[iLineNum2].getNinassistnum()));
          }

          if ((this.ZERO.equals(voaBase[i].getNinnum())) && (this.ZERO.equals(voaBase[i].getNinassistnum())))
          {
            continue;
          }
        } else {
          if (voaBase[i].getNoutnum() != null) {
            if (voaCompItem[iLineNum2].getNoutnum() != null) {
              voaBase[i].setNoutnum(voaBase[i].getNoutnum().sub(voaCompItem[iLineNum2].getNoutnum()));
            }

          }
          else if (voaCompItem[iLineNum2].getNoutnum() != null) {
            voaBase[i].setNoutnum(this.ZERO.sub(voaCompItem[iLineNum2].getNoutnum()));
          }

          if (voaBase[i].getNoutassistnum() != null) {
            if (voaCompItem[iLineNum2].getNoutassistnum() != null) {
              voaBase[i].setNoutassistnum(voaBase[i].getNoutassistnum().sub(voaCompItem[iLineNum2].getNoutassistnum()));
            }

          }
          else if (voaCompItem[iLineNum2].getNoutassistnum() != null) {
            voaBase[i].setNoutassistnum(this.ZERO.sub(voaCompItem[iLineNum2].getNoutassistnum()));
          }

          if ((this.ZERO.equals(voaBase[i].getNoutnum())) && (this.ZERO.equals(voaBase[i].getNoutassistnum())))
          {
            continue;
          }
        }

        voaBase[i].subLocator(voaCompItem[iLineNum2].getLocator());
        voaBase[i].setStatus(1);
        vResult.addElement(voaBase[i]);
      }
    }
    Enumeration enumKey = htBodyKey.keys();
    Object oKey = null;
    while (enumKey.hasMoreElements()) {
      oKey = enumKey.nextElement();
      oValueTemp = htBodyKey.get(oKey);
      iLineNum2 = Integer.valueOf(oValueTemp.toString()).intValue();

      if (voaCompItem[iLineNum2].getCinventoryid() != null) {
        voaCompItem[iLineNum2].setStatus(2);
        vResult.addElement(voaCompItem[iLineNum2]);
      }
    }
    GeneralBillItemVO[] voaItem = null;
    if (vResult.size() > 0) {
      voaItem = new GeneralBillItemVO[vResult.size()];
      vResult.copyInto(voaItem);
    }
    return voaItem;
  }

  public void copyAllPK(GeneralBillVO voBill, boolean bResetData)
  {
    if ((voBill == null) || (voBill.getHeaderVO() == null) || (voBill.getItemCount() <= 0))
    {
      SCMEnv.out("copy param null.");
      return;
    }

    int iItemCount = voBill.getItemCount();

    if (bResetData) {
      this.m_voHeader = new GeneralBillHeaderVO();
      this.m_voaItem = new GeneralBillItemVO[iItemCount];
      for (int i = 0; i < iItemCount; i++) {
        this.m_voaItem[i] = new GeneralBillItemVO();
      }
    }

    if (this.m_voHeader != null)
      this.m_voHeader.setPrimaryKey(voBill.getHeaderVO().getPrimaryKey());
    else
      SCMEnv.out("----> vo head is not initialzed.");
    if (this.m_voaItem != null)
      for (int row = 0; row < iItemCount; row++)
        if (this.m_voaItem[row] != null)
          this.m_voaItem[row].setPrimaryKey((String)voBill.getItemValue(row, "cgeneralbid"));
        else
          SCMEnv.out("---->a vo item is not initialzed.");
    else
      SCMEnv.out("----> vo item is not initialzed.");
  }

  public void filter0NumAnd0LocNumItem()
  {
    if ((this.m_voaItem == null) || (this.m_voaItem.length == 0)) {
      return;
    }
    ArrayList alResult = null;
    int len = this.m_voaItem.length;

    for (int i = 0; i < len; i++)
    {
      if ((this.m_voaItem[i] == null) || ((this.m_voaItem[i].is0Num()) && (this.m_voaItem[i].isLocator0Num())))
        continue;
      if (alResult == null)
        alResult = new ArrayList();
      alResult.add(this.m_voaItem[i]);
    }

    GeneralBillItemVO[] voaNewItem = this.m_voaItem;
    if ((alResult == null) || (alResult.size() == 0))
    {
      voaNewItem = null;
    } else if (alResult.size() != len) {
      voaNewItem = new GeneralBillItemVO[alResult.size()];
      alResult.toArray(voaNewItem);
    }

    this.m_voaItem = voaNewItem;
  }
  public void setLockOperatorid(String optid) {
    if (this.m_voHeader != null)
      this.m_voHeader.setCoperatoridnow(optid);
  }

  public void filter0NumItem()
  {
    if ((this.m_voaItem == null) || (this.m_voaItem.length == 0)) {
      return;
    }
    ArrayList alResult = null;
    int len = this.m_voaItem.length;

    for (int i = 0; i < len; i++)
    {
      if ((this.m_voaItem[i] != null) && (!this.m_voaItem[i].is0Num())) {
        if (alResult == null)
          alResult = new ArrayList();
        alResult.add(this.m_voaItem[i]);
      }
    }

    GeneralBillItemVO[] voaNewItem = this.m_voaItem;
    if ((alResult == null) || (alResult.size() == 0))
    {
      voaNewItem = null;
    } else if (alResult.size() != len) {
      voaNewItem = new GeneralBillItemVO[alResult.size()];
      alResult.toArray(voaNewItem);
    }

    this.m_voaItem = voaNewItem;
  }

  public GeneralBillItemVO[] filterItem()
  {
    if ((this.m_voaItem == null) || (this.m_voaItem.length == 0)) {
      return this.m_voaItem;
    }
    GeneralBillItemVO[] voaNewItem = this.m_voaItem;

    int i = 0;
    for (i = 0; i < this.m_voaItem.length; i++)
    {
      if ((this.m_voaItem[i] != null) && (((this.m_voaItem[i].getDiscountflag() != null) && (this.m_voaItem[i].getDiscountflag().booleanValue())) || ((this.m_voaItem[i].getLaborflag() != null) && (this.m_voaItem[i].getLaborflag().booleanValue()))))
      {
        break;
      }

      if ((this.m_voaItem[i].getInv().getIsmngstockbygrswt() == null) || (this.m_voaItem[i].getInv().getIsmngstockbygrswt().intValue() != 1))
        continue;
      if (this.m_voaItem[i].getNoutgrossnum() == null)
        this.m_voaItem[i].setNoutgrossnum(this.m_voaItem[i].getNoutnum());
      if (this.m_voaItem[i].getNingrossnum() == null)
        this.m_voaItem[i].setNingrossnum(this.m_voaItem[i].getNinnum());
      LocatorVO[] voLocs = this.m_voaItem[i].getLocator();
      if (voLocs != null) {
        for (int j = 0; j < voLocs.length; j++) {
          if (voLocs[j].getNingrossnum() == null)
            voLocs[j].setNingrossnum(voLocs[j].getNinspacenum());
          if (voLocs[j].getNoutgrossnum() == null) {
            voLocs[j].setNoutgrossnum(voLocs[j].getNoutspacenum());
          }

        }

      }

    }

    if (i < this.m_voaItem.length) {
      Vector vResult = new Vector();
      for (i = 0; i < this.m_voaItem.length; i++)
      {
        if ((this.m_voaItem[i] == null) || ((this.m_voaItem[i].getDiscountflag() != null) && (this.m_voaItem[i].getDiscountflag().booleanValue())) || ((this.m_voaItem[i].getLaborflag() != null) && (this.m_voaItem[i].getLaborflag().booleanValue())))
        {
          continue;
        }

        vResult.addElement(this.m_voaItem[i]);
      }

      if (vResult.size() == 0) {
        SCMEnv.out("============> no valid item !");
        voaNewItem = null;
      } else {
        voaNewItem = new GeneralBillItemVO[vResult.size()];
        vResult.copyInto(voaNewItem);
      }
    }
    return voaNewItem;
  }

  public GeneralBillItemVO[] filterWaitCheckedItem()
  {
    if ((this.m_voaItem == null) || (this.m_voaItem.length == 0)) {
      return this.m_voaItem;
    }
    GeneralBillItemVO[] voaNewItem = this.m_voaItem;

    int i = 0;
    for (i = 0; i < this.m_voaItem.length; i++)
    {
      if ((this.m_voaItem[i] != null) && (this.m_voaItem[i].getFchecked() != null) && (this.m_voaItem[i].getFchecked().intValue() != 0))
      {
        break;
      }

    }

    if (i < this.m_voaItem.length) {
      Vector vResult = new Vector();
      for (i = 0; i < this.m_voaItem.length; i++)
      {
        if ((this.m_voaItem[i] == null) || (this.m_voaItem[i].getFchecked() == null) || (this.m_voaItem[i].getFchecked().intValue() != 0)) {
          continue;
        }
        vResult.addElement(this.m_voaItem[i]);
      }

      if (vResult.size() == 0) {
        SCMEnv.out("============> no valid item !");
        voaNewItem = null;
      } else {
        voaNewItem = new GeneralBillItemVO[vResult.size()];
        vResult.copyInto(voaNewItem);
      }
    }
    return voaNewItem;
  }

  public String[] findItemByKey(String sKey, String sValue)
  {
    String[] saValue = null;
    if ((sKey != null) && (sValue != null) && (this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      ArrayList alResult = new ArrayList();
      for (int i = 0; i < this.m_voaItem.length; i++) {
        String sItemValue = (String)this.m_voaItem[i].getAttributeValue(sKey);
        if (sValue.equals(sItemValue)) {
          alResult.add((String)this.m_voaItem[i].getAttributeValue("cgeneralbid"));
        }
      }
      if (alResult.size() > 0) {
        saValue = new String[alResult.size()];
        alResult.toArray(saValue);
      }

    }

    return saValue;
  }

  public String getAccreditUserID()
  {
    return this.m_sAccreditUserID;
  }

  public String getActionCode()
  {
    return this.m_sActionCode;
  }

  public int getActionInt()
  {
    return this.m_iActionInt;
  }

  public boolean getAutoSendBarcodes()
  {
    return this.m_bAutoSendBarcodes;
  }

  public BillCodeObjValueVO getBillCodeObjVO()
  {
    BillCodeObjValueVO bcovo = new BillCodeObjValueVO();

    if (this.m_voHeader != null) {
      bcovo.setAttributeValue("公司", this.m_voHeader.getPk_corp() == null ? "" : this.m_voHeader.getPk_corp());
      bcovo.setAttributeValue("操作员", this.m_voHeader.getCoperatorid() == null ? "" : this.m_voHeader.getCoperatorid());
      bcovo.setAttributeValue("库存组织", this.m_voHeader.getPk_calbody() == null ? "" : this.m_voHeader.getPk_calbody());
      bcovo.setAttributeValue("仓库", this.m_voHeader.getCwarehouseid() == null ? "" : this.m_voHeader.getCwarehouseid());
      bcovo.setAttributeValue("收发类别", this.m_voHeader.getCdispatcherid() == null ? "" : this.m_voHeader.getCdispatcherid());
    }
    return bcovo;
  }

  public int getBillInOutFlag()
  {
    if (this.m_voHeader == null)
      return 0;
    int iBillInOut = 1;
    String sBillTypeCode = this.m_voHeader.getCbilltypecode();
    if (sBillTypeCode == null)
      return 0;
    sBillTypeCode = sBillTypeCode.trim();

    if ((sBillTypeCode.equals(BillTypeConst.m_initIn)) || (sBillTypeCode.equals(BillTypeConst.m_initBorrow)) || (sBillTypeCode.equals(BillTypeConst.m_initWaster)) || (sBillTypeCode.equals(BillTypeConst.m_initEntrustMachining)) || (sBillTypeCode.equals(BillTypeConst.m_purchaseIn)) || (sBillTypeCode.equals(BillTypeConst.m_productIn)) || (sBillTypeCode.equals(BillTypeConst.m_consignMachiningIn)) || (sBillTypeCode.equals(BillTypeConst.m_entrustMachiningIn)) || (sBillTypeCode.equals(BillTypeConst.m_borrowIn)) || (sBillTypeCode.equals(BillTypeConst.m_otherIn)) || (sBillTypeCode.equals(BillTypeConst.m_lendIn)) || (sBillTypeCode.equals(BillTypeConst.m_purchaseInit)) || (sBillTypeCode.equals(BillTypeConst.m_allocationIn)))
    {
      iBillInOut = 1;
    }
    else if ((sBillTypeCode.equals(BillTypeConst.m_saleOut)) || (sBillTypeCode.equals(BillTypeConst.m_initLend)) || (sBillTypeCode.equals(BillTypeConst.m_materialOut)) || (sBillTypeCode.equals(BillTypeConst.m_consignMachiningOut)) || (sBillTypeCode.equals(BillTypeConst.m_entrustMachiningOut)) || (sBillTypeCode.equals(BillTypeConst.m_lendOut)) || (sBillTypeCode.equals(BillTypeConst.m_otherOut)) || (sBillTypeCode.equals(BillTypeConst.m_borrowOut)) || (sBillTypeCode.equals(BillTypeConst.m_discardOut)) || (sBillTypeCode.equals(BillTypeConst.m_saleInit)) || (sBillTypeCode.equals(BillTypeConst.m_allocationOut)) || (sBillTypeCode.equals("3Q")))
    {
      iBillInOut = -1;
    }
    else iBillInOut = 0;
    return iBillInOut;
  }

  public int[] getBillItemInOutPty()
  {
    if ((this.m_voHeader == null) || (this.m_voaItem == null) || (this.m_voaItem.length == 0))
      return null;
    int[] iaInoutPry = new int[getItemCount()];
    int iBillInOut = getBillInOutFlag();
    for (int row = 0; row < getItemCount(); row++)
    {
      iaInoutPry[row] = 0;
      if (this.m_voaItem[row] == null)
        continue;
      if ((1 == iBillInOut) && (this.m_voaItem[row].getNinnum() != null)) {
        if (this.m_voaItem[row].getNinnum().doubleValue() >= 0.0D)
          iaInoutPry[row] = 1;
        else {
          iaInoutPry[row] = -1;
        }

      }
      else if ((-1 == iBillInOut) && (this.m_voaItem[row].getNoutnum() != null)) {
        if (this.m_voaItem[row].getNoutnum().doubleValue() >= 0.0D)
          iaInoutPry[row] = -1;
        else {
          iaInoutPry[row] = 1;
        }
      }
    }
    return iaInoutPry;
  }

  public int getBillItemInOutPtybyRow(int iCurRow)
  {
    int iInoutPry = 0;

    if ((this.m_voHeader == null) || (this.m_voaItem == null) || (this.m_voaItem.length == 0)) {
      return iInoutPry;
    }
    int iBillInOut = getBillInOutFlag();
    if (iCurRow >= iBillInOut) {
      return iInoutPry;
    }
    if (this.m_voaItem[iCurRow] != null)
    {
      if ((1 == iBillInOut) && (this.m_voaItem[iCurRow].getNinnum() != null)) {
        if (this.m_voaItem[iCurRow].getNinnum().doubleValue() >= 0.0D)
          iInoutPry = 1;
        else {
          iInoutPry = -1;
        }

      }
      else if ((-1 == iBillInOut) && (this.m_voaItem[iCurRow].getNoutnum() != null)) {
        if (this.m_voaItem[iCurRow].getNoutnum().doubleValue() >= 0.0D)
          iInoutPry = -1;
        else {
          iInoutPry = 1;
        }
      }
    }
    return iInoutPry;
  }

  public String getBillTypeCode()
  {
    if (this.m_voHeader != null)
      return this.m_voHeader.getCbilltypecode();
    return null;
  }

  public int getBillTypeInt()
  {
    return 1;
  }

  public boolean getBisIUK_SourceBodyidOnly()
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0) && (this.m_voaItem[0] != null)) {
      return this.m_voaItem[0].getBisIUK_SourceBodyidOnly();
    }
    return false;
  }

  public String getBizTypeid()
  {
    if (this.m_voHeader != null)
      return this.m_voHeader.getCbiztypeid();
    return null;
  }

  public CircularlyAccessibleValueObject[] getChildrenVO()
  {
    return this.m_voaItem;
  }

  public String getCurUserID()
  {
    if (this.m_voHeader != null)
      return this.m_voHeader.getCoperatoridnow();
    return null;
  }

  public GeneralBillItemVO[] getDeletedItemVOs()
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      Vector v = new Vector();

      for (int row = 0; row < this.m_voaItem.length; row++) {
        if (this.m_voaItem[row].getStatus() == 3) {
          v.add(this.m_voaItem[row]);
        }
      }
      GeneralBillItemVO[] voNews = null;
      if (v.size() > 0) {
        voNews = new GeneralBillItemVO[v.size()];
        v.copyInto(voNews);
      }
      return voNews;
    }
    return null;
  }

  public Object getHeaderValue(String sKey)
  {
    if (this.m_voHeader != null)
      return this.m_voHeader.getAttributeValue(sKey);
    return null;
  }

  public GeneralBillHeaderVO getHeaderVO()
  {
    return this.m_voHeader;
  }

  public GeneralBillVO getIDItems()
  {
    GeneralBillVO voBill = new GeneralBillVO(getItemCount());

    if (this.m_voHeader != null) {
      voBill.setParentVO(this.m_voHeader.getIDItems());
    }
    GeneralBillItemVO[] voaBi = new GeneralBillItemVO[getItemCount()];
    if (this.m_voaItem != null) {
      for (int i = 0; i < this.m_voaItem.length; i++)
        if (this.m_voaItem[i] != null)
          voaBi[i] = this.m_voaItem[i].getIDItems();
    }
    voBill.setChildrenVO(voaBi);
    return voBill;
  }

  public GeneralBillItemVO[] getInsertedItemVOs()
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      Vector v = new Vector();

      for (int row = 0; row < this.m_voaItem.length; row++) {
        if (this.m_voaItem[row].getStatus() == 2) {
          v.add(this.m_voaItem[row]);
        }
      }
      GeneralBillItemVO[] voNews = null;
      if (v.size() > 0) {
        voNews = new GeneralBillItemVO[v.size()];
        v.copyInto(voNews);
      }
      return voNews;
    }
    return null;
  }

  public LocatorVO[] getInsertedLocatorVOs()
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      LocatorVO[] voNews = null;

      Vector v = new Vector();
      LocatorVO[] voLocs = null;
      for (int row = 0; row < this.m_voaItem.length; row++)
      {
        if ((this.m_voaItem[row].getStatus() != 2) && (this.m_voaItem[row].getLocStatus() != 1))
          continue;
        voLocs = this.m_voaItem[row].getLocator();
        if ((voLocs != null) && (voLocs.length > 0)) {
          for (int i = 0; i < voLocs.length; i++) {
            v.add(voLocs[i]);
          }
        }
      }

      if (v.size() > 0) {
        voNews = new LocatorVO[v.size()];
        v.copyInto(voNews);
      }
      return voNews;
    }

    return null;
  }

  public Hashtable getInvInfo()
  {
    Hashtable htInvs = null;
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      htInvs = new Hashtable();
      for (int i = 0; i < this.m_voaItem.length; i++) {
        if ((this.m_voaItem[i] == null) || (this.m_voaItem[i].getCinventoryid() == null) || (htInvs.containsKey(this.m_voaItem[i].getCinventoryid()))) {
          continue;
        }
        htInvs.put(this.m_voaItem[i].getCinventoryid(), this.m_voaItem[i].getInv());
      }
    }

    return htInvs;
  }

  public BarCodeVO[] getItemBarcodeVO(int row)
  {
    if ((this.m_voaItem != null) && (row >= 0) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null)) {
      return this.m_voaItem[row].getBarCodeVOs();
    }
    return null;
  }

  public int getItemCount()
  {
    if (this.m_voaItem != null) {
      return this.m_voaItem.length;
    }
    return 0;
  }

  public InvVO getItemInv(int row)
  {
    if ((this.m_voaItem != null) && (row >= 0) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null)) {
      return this.m_voaItem[row].getInv();
    }
    return null;
  }

  public Object getItemValue(int row, String sKey)
  {
    if ((this.m_voaItem != null) && (row >= 0) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null))
      return this.m_voaItem[row].getAttributeValue(sKey);
    return null;
  }

  public GeneralBillItemVO[] getItemVOs()
  {
    return this.m_voaItem;
  }

  public Hashtable getLocatorInfo()
  {
    Hashtable htInvs = null;
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      htInvs = new Hashtable();
      int size = this.m_voaItem.length; int len = 0;
      LocatorVO[] voaLoc = null;
      for (int i = 0; i < size; i++) {
        if (this.m_voaItem[i] != null) {
          voaLoc = this.m_voaItem[i].getLocator();
          if (voaLoc != null) {
            len = voaLoc.length;
            for (int j = 0; j < len; j++) {
              if ((voaLoc[j] == null) || (voaLoc[j].getCspaceid() == null) || (htInvs.containsKey(voaLoc[j].getCspaceid()))) {
                continue;
              }
              htInvs.put(voaLoc[j].getCspaceid(), voaLoc[j]);
            }
          }
        }
      }
    }
    return htInvs;
  }

  public ArrayList getLocators()
  {
    ArrayList alData = new ArrayList();
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++)
        alData.add(this.m_voaItem[i].getLocator());
    return alData;
  }

  public ArrayList getLocatorsClone()
  {
    ArrayList alData = new ArrayList();
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++)
        alData.add(this.m_voaItem[i].getLocatorClone());
    return alData;
  }

  public ArrayList getLockablePKArray()
  {
    if (this.m_alLockedPK == null) {
      ArrayList althis = getThisBillPKArray();
      ArrayList alsrc = getSourceBillPKArray();
      int count = 0;
      if ((althis != null) && (althis.size() > 0))
        count += althis.size();
      if ((alsrc != null) && (alsrc.size() > 0)) {
        count += alsrc.size();
      }
      if (count > 0) {
        this.m_alLockedPK = new ArrayList(count);
        for (int i = 0; i < althis.size(); i++)
          this.m_alLockedPK.add(althis.get(i));
        for (int i = 0; i < alsrc.size(); i++) {
          this.m_alLockedPK.add(alsrc.get(i));
        }
      }

    }

    return this.m_alLockedPK;
  }

  public AggregatedValueObject getModifiedVO()
  {
    return this;
  }

  public String[] getNumInfo(int[] iOperator)
  {
    String[] sRet = null;
    GeneralBillItemVO[] voItems = null;
    StringBuffer[] sbMsg = null;
    if ((iOperator != null) && (iOperator.length > 0)) {
      sbMsg = new StringBuffer[iOperator.length];
      sRet = new String[iOperator.length];
    }
    else {
      return null;
    }
    voItems = getItemVOs();
    if ((voItems != null) && (voItems.length > 0)) {
      for (int i = 0; i < voItems.length; i++) {
        int iResult = voItems[i].compareNum();
        for (int j = 0; j < iOperator.length; j++) {
          if (iResult == iOperator[j]) {
            if (sbMsg[j] == null)
              sbMsg[j] = new StringBuffer();
            sbMsg[j].append(i);
            sbMsg[j].append(" ");
            sbMsg[j].append(voItems[i].getCinventorycode());
            sbMsg[j].append(voItems[i].getInvname());
            sbMsg[j].append("\n");
          }
        }
      }
    }
    for (int i = 0; i < iOperator.length; i++) {
      if ((sbMsg[i] != null) && (sbMsg.length > 0))
        sRet[i] = sbMsg[i].toString();
    }
    return sRet;
  }

  public AggregatedValueObject getOldVO()
  {
    return this.m_voOld;
  }

  public OperatelogVO getOperatelogVO()
  {
    return this.m_OperatelogVO;
  }

  public String getOperatorid()
  {
    return getCurUserID();
  }

  public CircularlyAccessibleValueObject getParentVO()
  {
    return this.m_voHeader;
  }

  public String getPk_corp()
  {
    if ((this.m_voHeader != null) && (this.m_voHeader.getPk_corp() != null)) {
      return this.m_voHeader.getPk_corp();
    }
    return null;
  }

  public String getPrimaryKey()
  {
    if (this.m_voHeader != null) {
      return this.m_voHeader.getPrimaryKey();
    }
    return null;
  }

  public String getRptSnInfo()
  {
    Hashtable htInvSn = null;
    StringBuffer sbMsg = null;
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      htInvSn = new Hashtable();
      int size = this.m_voaItem.length; int len = 0;
      SerialVO[] voaSn = null;
      String sValue = "";
      String sKey = null;
      sbMsg = new StringBuffer();
      for (int i = 0; i < size; i++) {
        if ((this.m_voaItem[i] != null) && (this.m_voaItem[i].getCinventoryid() != null)) {
          voaSn = this.m_voaItem[i].getSerial();
          if (voaSn != null) {
            len = voaSn.length;
            for (int j = 0; j < len; j++) {
              sKey = this.m_voaItem[i].getCinventoryid() + voaSn[j].getVserialcode();
              if ((voaSn[j] != null) && (voaSn[j].getVserialcode() != null)) {
                if (!htInvSn.containsKey(sKey)) {
                  htInvSn.put(sKey, "");
                } else {
                  if (sbMsg.length() > 0)
                    sbMsg.append(",");
                  sbMsg.append(this.m_voaItem[i].getCinventorycode());
                  sbMsg.append("/");
                  sbMsg.append(voaSn[j].getVserialcode());
                }
              }
            }
          }
        }
      }
    }
    if ((sbMsg != null) && (sbMsg.toString().trim().length() > 0)) {
      return sbMsg.toString();
    }
    return null;
  }

  public SMGeneralBillVO getSmallBillVO()
  {
    if (this.m_SmGenBillVO == null) {
      this.m_SmGenBillVO = new SMGeneralBillVO();
    }
    this.m_SmGenBillVO.setErrMsg(this.m_sErr);

    SMGeneralBillHeaderVO smbillHeadvo = new SMGeneralBillHeaderVO();
    SMGeneralBillItemVO[] smbillItemvo = null;

    GeneralBillHeaderVO billHeadvo = getHeaderVO();
    if (billHeadvo != null) {
      smbillHeadvo.setCgeneralhid(billHeadvo.getCgeneralhid());
      smbillHeadvo.setFbillflag(billHeadvo.getFbillflag());
      smbillHeadvo.setTs(billHeadvo.getTs());
      smbillHeadvo.setVbillcode(billHeadvo.getVbillcode());

      smbillHeadvo.setAttributeValue("coutcorpid", billHeadvo.getAttributeValue("coutcorpid"));
      smbillHeadvo.setAttributeValue("coutunitname", billHeadvo.getAttributeValue("coutunitname"));
      smbillHeadvo.setAttributeValue("coutcalbodyid", billHeadvo.getAttributeValue("coutcalbodyid"));
      smbillHeadvo.setAttributeValue("coutbodyname", billHeadvo.getAttributeValue("coutbodyname"));
    }

    GeneralBillItemVO[] billItemvos = (GeneralBillItemVO[])(GeneralBillItemVO[])getChildrenVO();

    if ((billItemvos != null) && (billItemvos.length > 0)) {
      int iLen = billItemvos.length;
      smbillItemvo = new SMGeneralBillItemVO[iLen];

      BarCodeVO[] barCodeVOs = null;

      for (int i = 0; i < billItemvos.length; i++) {
        if (billItemvos[i] != null) {
          smbillItemvo[i] = new SMGeneralBillItemVO();

          barCodeVOs = billItemvos[i].getBarCodeVOs();
          if ((barCodeVOs != null) && (barCodeVOs.length > 0)) {
            smbillItemvo[i].setBarCodeVOs(barCodeVOs);
          }
          smbillItemvo[i].setVbatchcode(billItemvos[i].getVbatchcode());
          smbillItemvo[i].setCgeneralbid(billItemvos[i].getCgeneralbid());
          smbillItemvo[i].setCgeneralhid(billItemvos[i].getCgeneralhid());
          smbillItemvo[i].setNinnum(billItemvos[i].getNinnum());
          smbillItemvo[i].setNinassistnum(billItemvos[i].getNinassistnum());
          smbillItemvo[i].setNoutassistnum(billItemvos[i].getNoutassistnum());
          smbillItemvo[i].setNoutnum(billItemvos[i].getNoutnum());
          smbillItemvo[i].setVfirstbillcode(billItemvos[i].getVfirstbillcode());
          smbillItemvo[i].setTs(billItemvos[i].getTs());
          smbillItemvo[i].setCrowno(billItemvos[i].getCrowno());

          smbillItemvo[i].setBarcodeClose(billItemvos[i].getBarcodeClose());
          smbillItemvo[i].setListnbarcodenum(billItemvos[i].getListbarcodenums());
          smbillItemvo[i].setAttributeValue("dvalidate", billItemvos[i].getDvalidate());
          smbillItemvo[i].setAttributeValue("scrq", billItemvos[i].getAttributeValue("scrq"));
          smbillItemvo[i].setAttributeValue("cspaceid", billItemvos[i].getCspaceid());
          smbillItemvo[i].setAttributeValue("vspacename", billItemvos[i].getVspacename());
          if (billItemvos[i].getLocator() != null) {
            smbillItemvo[i].setAttributeValue("locator", billItemvos[i].getLocator());
          }
          smbillItemvo[i].setCcorrespondbid(billItemvos[i].getCcorrespondbid());
          smbillItemvo[i].setCcorrespondcode(billItemvos[i].getCcorrespondcode());
          smbillItemvo[i].setCcorrespondhid(billItemvos[i].getCcorrespondhid());
          smbillItemvo[i].setCcorrespondtype(billItemvos[i].getCcorrespondtype());
          smbillItemvo[i].setCcorrespondtypename(billItemvos[i].getCcorrespondtypename());
          smbillItemvo[i].setAttributeValue("cparentid", billItemvos[i].getAttributeValue("cparentid"));

          smbillItemvo[i].setNsalemny(billItemvos[i].getNsalemny());
          smbillItemvo[i].setNtaxmny(billItemvos[i].getNtaxmny());

          smbillItemvo[i].setNmny(billItemvos[i].getNmny());
          smbillItemvo[i].setAttributeValue("nprice", billItemvos[i].getNprice());
          smbillItemvo[i].setAttributeValue("ntaxprice", billItemvos[i].getNtaxprice());
          smbillItemvo[i].setAttributeValue("ntaxmny", billItemvos[i].getNtaxmny());
          smbillItemvo[i].setAttributeValue("nsaleprice", billItemvos[i].getNsaleprice());
          smbillItemvo[i].setAttributeValue("nsalemny", billItemvos[i].getNsalemny());
          smbillItemvo[i].setAttributeValue("nquotemny", billItemvos[i].getAttributeValue("nquotemny"));
        }

      }

    }

    this.m_SmGenBillVO.setChildrenVO(smbillItemvo);
    this.m_SmGenBillVO.setParentVO(smbillHeadvo);

    return this.m_SmGenBillVO;
  }

  public SMGeneralBillVO getSmGenBillVO()
  {
    return this.m_SmGenBillVO;
  }

  public ArrayList getSNs()
  {
    ArrayList alData = new ArrayList();
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++)
        alData.add(this.m_voaItem[i].getSerial());
    return alData;
  }

  public ArrayList getSNsClone()
  {
    ArrayList alData = new ArrayList();
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++)
        alData.add(this.m_voaItem[i].getSerialClone());
    return alData;
  }

  public ArrayList getSourceBillPKArray()
  {
    if ((this.m_voHeader != null) && 
      (this.m_voHeader.getBdirecttranflag() != null) && (this.m_voHeader.getBdirecttranflag().booleanValue())) {
      return null;
    }

    ArrayList alLockedPK = new ArrayList();
    HashMap ht = new HashMap();
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      String id = null;
      for (int i = 0; i < this.m_voaItem.length; i++)
      {
        if (this.m_voaItem[i] == null)
          continue;
        id = this.m_voaItem[i].getCsourcebillhid();
        Integer idesatypeObj = this.m_voaItem[i].getDesaType();
        int idesatype = idesatypeObj == null ? 0 : idesatypeObj.intValue();

        if ((id == null) || ("4Y".equals(this.m_voaItem[i].getCsourcetype())) || (idesatype == DesassemblyVO.TYPE_SYS)) {
          continue;
        }
        if ((id != null) && (!ht.containsKey(id)))
        {
          ht.put(id, id);
          alLockedPK.add(id);
        }

        id = this.m_voaItem[i].getCsourcebillbid();

        if ((id != null) && (!ht.containsKey(id))) {
          ht.put(id, id);
          alLockedPK.add(id);
        }

      }

    }

    return alLockedPK;
  }

  public int getStatus()
  {
    if (this.m_voHeader != null) {
      if ((this.m_voHeader.getStatus() == 2) || (this.m_voHeader.getCgeneralhid() == null))
        return 2;
      return this.m_voHeader.getStatus();
    }
    return -1;
  }

  public ArrayList getThisBillPKArray()
  {
    ArrayList alLockedPK = new ArrayList();
    if ((this.m_voHeader != null) && (this.m_voHeader.getCgeneralhid() != null)) {
      alLockedPK.add(this.m_voHeader.getCgeneralhid());
    }
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
    {
      for (int i = 0; i < this.m_voaItem.length; i++) {
        if ((this.m_voaItem[i] != null) && (this.m_voaItem[i].getCgeneralbid() != null)) {
          alLockedPK.add(this.m_voaItem[i].getCgeneralbid());
        }
      }

    }

    return alLockedPK;
  }

  public GeneralBillItemVO[] getUpdatedItemVOs()
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      Vector v = new Vector();

      for (int row = 0; row < this.m_voaItem.length; row++) {
        if (this.m_voaItem[row].getStatus() == 1) {
          v.add(this.m_voaItem[row]);
        }
      }
      GeneralBillItemVO[] voNews = null;
      if (v.size() > 0) {
        voNews = new GeneralBillItemVO[v.size()];
        v.copyInto(voNews);
      }
      return voNews;
    }
    return null;
  }

  public String getVBillCode()
  {
    if ((this.m_voHeader != null) && (this.m_voHeader.getVbillcode() != null)) {
      return this.m_voHeader.getVbillcode();
    }
    return null;
  }

  public WhVO getWasteWh()
  {
    if ((this.m_voHeader != null) && (this.m_voHeader.getCwastewarehouseid() != null)) {
      WhVO voWh = new WhVO();
      voWh.setCwarehouseid(this.m_voHeader.getCwastewarehouseid());
      voWh.setCwarehousename(this.m_voHeader.getCwastewarehousename());
      voWh.setIsWasteWh(this.m_voHeader.getIsWasteWh2());

      voWh.setPk_corp(this.m_voHeader.getPk_corp());
      voWh.setPk_calbody(this.m_voHeader.getPk_calbody());
      voWh.setVcalbodyname(this.m_voHeader.getVcalbodyname());

      return voWh;
    }
    return null;
  }

  public WhVO getWh()
  {
    if ((this.m_voHeader != null) && (this.m_voHeader.getCwarehouseid() != null)) {
      WhVO voWh = new WhVO();
      voWh.setCwarehouseid(this.m_voHeader.getCwarehouseid());
      voWh.setCwarehousename(this.m_voHeader.getCwarehousename());
      voWh.setIsWasteWh(this.m_voHeader.getIsWasteWh());
      voWh.setIsLocatorMgt(this.m_voHeader.getIsLocatorMgt());
      voWh.setWhReservedPty(this.m_voHeader.getWhReservedPty());
      voWh.setPk_corp(this.m_voHeader.getPk_corp());
      voWh.setPk_calbody(this.m_voHeader.getPk_calbody());
      voWh.setVcalbodyname(this.m_voHeader.getVcalbodyname());

      voWh.setIsgathersettle(this.m_voHeader.getIsgathersettle());

      voWh.setIscalculatedinvcost(this.m_voHeader.getIscalculatedinvcost());

      voWh.setIsdirectstore(this.m_voHeader.getIsdirectstore());

      return voWh;
    }
    return null;
  }

  public void insertItem(int line)
  {
    Vector vData = new Vector();

    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
      for (int i = 0; i < this.m_voaItem.length; i++)
        vData.addElement(this.m_voaItem[i]);
    } else {
      this.m_voaItem = new GeneralBillItemVO[1];
      this.m_voaItem[0] = new GeneralBillItemVO();
      return;
    }

    if ((line >= 0) && (line < this.m_voaItem.length)) {
      vData.insertElementAt(new GeneralBillItemVO(), line);
    }
    else
      vData.addElement(new GeneralBillItemVO());
    this.m_voaItem = new GeneralBillItemVO[vData.size()];
    vData.copyInto(this.m_voaItem);
  }

  public boolean isBarcodeNumVerify()
  {
    return this.m_bBarcodeNumVerify;
  }

  public boolean isBillQtyIntegrity()
  {
    if ((this.m_voHeader == null) || (this.m_voaItem == null) || (this.m_voaItem.length == 0))
      return false;
    int iInNumCount = 0; int iOutNumCount = 0;

    int iRowCount = getItemCount();

    for (int row = 0; row < iRowCount; row++) {
      if (this.m_voaItem[row] == null)
        continue;
      if (this.m_voaItem[row].getNinnum() != null) {
        iInNumCount++;
      }
      else if (this.m_voaItem[row].getNoutnum() != null) {
        iOutNumCount++;
      }
    }

    return (iInNumCount == iRowCount) || (iOutNumCount == iRowCount);
  }

  public boolean isFromFirst(String cBillType)
  {
    if ((cBillType == null) || (this.m_voHeader == null) || (this.m_voaItem == null) || (this.m_voaItem.length == 0))
      return false;
    int iCount = 0;

    int iRowCount = getItemCount();

    for (int row = 0; row < iRowCount; row++) {
      if (this.m_voaItem[row] == null)
        continue;
      if (cBillType.equals(this.m_voaItem[row].getCfirsttype())) {
        iCount++;
      }
    }

    return iCount > 0;
  }

  public boolean isFromSource(String cBillType)
  {
    if ((cBillType == null) || (this.m_voHeader == null) || (this.m_voaItem == null) || (this.m_voaItem.length == 0))
      return false;
    int iCount = 0;

    int iRowCount = getItemCount();

    for (int row = 0; row < iRowCount; row++) {
      if (this.m_voaItem[row] == null)
        continue;
      if (cBillType.equals(this.m_voaItem[row].getCsourcetype())) {
        iCount++;
      }
    }

    return iCount > 0;
  }

  public boolean isHaveCalConvRate()
  {
    return this.m_bHaveCalConvRate;
  }

  public boolean isHaveSourceBill()
  {
    boolean isHaveSourceBill = false;
    if (this.m_voaItem != null) {
      String sourcebid = null;
      for (int i = 0; i < this.m_voaItem.length; i++) {
        sourcebid = (String)this.m_voaItem[i].getAttributeValue("csourcebillbid");
        if ((sourcebid != null) && (sourcebid.trim().length() > 0)) {
          isHaveSourceBill = true;
          break;
        }
      }
    }
    return isHaveSourceBill;
  }

  public boolean isHaveSourceBill(String[] sourcetypes)
  {
    boolean isHaveSourceBill = false;
    if (this.m_voaItem != null) {
      String sourcetype = null;
      Hashtable ht = new Hashtable();
      for (int i = 0; i < sourcetypes.length; i++) {
        if (sourcetypes[i] != null)
          ht.put(sourcetypes[i], sourcetypes[i]);
      }
      for (int i = 0; i < this.m_voaItem.length; i++) {
        sourcetype = (String)this.m_voaItem[i].getAttributeValue("csourcetype");
        if ((sourcetype != null) && (ht.containsKey(sourcetype))) {
          isHaveSourceBill = true;
          break;
        }
      }
    }
    return isHaveSourceBill;
  }

  public boolean isQtyFilled()
  {
    if ((this.m_voHeader == null) || (this.m_voaItem == null) || (this.m_voaItem.length == 0) || (this.m_voaItem[0] == null))
      return false;
    int iBillInOut = getBillInOutFlag();

    if ((1 == iBillInOut) && (this.m_voaItem[0].getNinnum() != null)) {
      return true;
    }

    return (-1 == iBillInOut) && (this.m_voaItem[0].getNoutnum() != null);
  }

  public boolean isSaveBadBarcode()
  {
    return this.m_bSaveBadBarcode;
  }

  public boolean isSaveBarcode()
  {
    return this.m_bSaveBarcode;
  }

  public boolean isSaveBarcodeDelFirst()
  {
    return this.m_bSaveBarcodeDelFirst;
  }

  public GeneralBillItemVO[] neg()
  {
    return neg(this.m_voaItem);
  }

  public GeneralBillItemVO[] neg(GeneralBillItemVO[] voaItem)
  {
    if ((voaItem == null) || (voaItem.length == 0)) {
      return voaItem;
    }
    int iBillInOutFlag = getBillInOutFlag();

    for (int i = 0; i < voaItem.length; i++) {
      if (voaItem[i] == null)
        continue;
      if (iBillInOutFlag == 1)
      {
        if (voaItem[i].getNinnum() != null) {
          voaItem[i].setNinnum(this.ZERO.sub(voaItem[i].getNinnum()));
        }
        if (voaItem[i].getNinassistnum() != null)
          voaItem[i].setNinassistnum(this.ZERO.sub(voaItem[i].getNinassistnum()));
      }
      else {
        if (voaItem[i].getNoutnum() != null) {
          voaItem[i].setNoutnum(this.ZERO.sub(voaItem[i].getNoutnum()));
        }

        if (voaItem[i].getNoutassistnum() != null) {
          voaItem[i].setNoutassistnum(this.ZERO.sub(voaItem[i].getNoutassistnum()));
        }
      }

      voaItem[i].negLocator();
    }

    return voaItem;
  }

  public void removeItem(int line)
  {
    Vector vData = new Vector();

    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int i = 0; i < this.m_voaItem.length; i++)
        vData.addElement(this.m_voaItem[i]);
    else
      return;
    if ((line >= 0) && (line < this.m_voaItem.length))
      vData.removeElementAt(line);
    if (vData.size() > 0) {
      this.m_voaItem = new GeneralBillItemVO[vData.size()];
      vData.copyInto(this.m_voaItem);
    } else {
      this.m_voaItem = new GeneralBillItemVO[0];
    }
  }

  public GeneralBillItemVO[] removeUnchangeItemVOs(GeneralBillItemVO[] avoOldItem, GeneralBillItemVO[] avoNewItem)
  {
    GeneralBillItemVO[] avoResult = null;
    if ((avoNewItem != null) && (avoOldItem != null)) {
      UFDouble dZero = new UFDouble(0);

      Hashtable htSearch = new Hashtable();
      for (int i = 0; i < avoOldItem.length; i++) {
        htSearch.put(avoOldItem[i].getCgeneralbid(), avoOldItem[i]);
      }

      ArrayList alnewvo = new ArrayList();
      for (int m = 0; m < avoNewItem.length; m++) {
        alnewvo.add(avoNewItem[m]);
      }

      for (int j = 0; j < avoNewItem.length; j++) {
        String sbid = avoNewItem[j].getCgeneralbid();
        if ((avoNewItem[j].getStatus() == 0) && (htSearch.containsKey(sbid))) {
          if ((avoNewItem[j].getLocator() == null) && (((GeneralBillItemVO)htSearch.get(sbid)).getLocator() == null)) {
            alnewvo.set(j, null);
          }
          else if ((avoNewItem[j].getLocator() != null) && (((GeneralBillItemVO)htSearch.get(sbid)).getLocator() != null)) {
            LocatorVO[] lvonew = avoNewItem[j].getLocator();
            LocatorVO[] lvoold = ((GeneralBillItemVO)htSearch.get(sbid)).getLocator();
            boolean bEqual = false;
            for (int k = 0; k < lvonew.length; k++) {
              String locatorid = lvonew[k].getCspaceid() == null ? "" : lvonew[k].getCspaceid();
              UFDouble nlocatornum = (lvonew[k].getNinspacenum() == null ? dZero : lvonew[k].getNinspacenum()).sub(lvonew[k].getNoutspacenum() == null ? dZero : lvonew[k].getNoutspacenum());
              UFDouble nlocatorastnum = (lvonew[k].getNinspaceassistnum() == null ? dZero : lvonew[k].getNinspaceassistnum()).sub(lvonew[k].getNoutspaceassistnum() == null ? dZero : lvonew[k].getNoutspaceassistnum());
              for (int l = 0; l < lvoold.length; l++) {
                String locatoridold = lvoold[k].getCspaceid() == null ? "" : lvoold[k].getCspaceid();
                UFDouble nlocatornumold = (lvoold[k].getNinspacenum() == null ? dZero : lvoold[k].getNinspacenum()).sub(lvoold[k].getNoutspacenum() == null ? dZero : lvoold[k].getNoutspacenum());
                UFDouble nlocatorastnumold = (lvoold[k].getNinspaceassistnum() == null ? dZero : lvoold[k].getNinspaceassistnum()).sub(lvoold[k].getNoutspaceassistnum() == null ? dZero : lvoold[k].getNoutspaceassistnum());
                if ((locatorid.equals(locatoridold)) && (nlocatornum.equals(nlocatornumold)) && (nlocatorastnum.equals(nlocatorastnumold))) {
                  bEqual = true;
                }
                else
                {
                  bEqual = false;
                  break;
                }
              }
            }

            if (bEqual) {
              alnewvo.set(j, null);
            }
          }
        }

      }

      if (alnewvo.size() > 0) {
        ArrayList alnew = new ArrayList();
        for (int i = 0; i < alnewvo.size(); i++) {
          if (alnewvo.get(i) != null) {
            alnew.add(alnewvo.get(i));
          }
        }
        if (alnew.size() > 0) {
          avoResult = new GeneralBillItemVO[alnew.size()];
          alnew.toArray(avoResult);
        }
      }

    }

    if ((avoNewItem != null) && (avoOldItem == null)) {
      avoResult = avoNewItem;
    }
    return avoResult;
  }

  public void setAccreditUserID(String newAccreditUserID)
  {
    this.m_sAccreditUserID = newAccreditUserID;
  }

  public void setAutoSendBarcodes(boolean newM_bAutoSendBarcodes)
  {
    this.m_bAutoSendBarcodes = newM_bAutoSendBarcodes;
  }

  public void setBarcodeNumVerify(boolean newBarcodeNumVerify)
  {
    this.m_bBarcodeNumVerify = newBarcodeNumVerify;
  }

  public void setBisIUK_SourceBodyidOnly(boolean newIUK_SourceBodyidOnly)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        this.m_voaItem[l].setBisIUK_SourceBodyidOnly(newIUK_SourceBodyidOnly);
  }

  public void setBorrowLendLocators(ArrayList alData)
  {
    Hashtable htData = null;

    if ((this.m_voaItem != null) && (alData != null)) {
      htData = new Hashtable();
      LocatorVO[] voaLoc = null;

      for (int j = 0; j < alData.size(); j++) {
        Object oLocVOs = alData.get(j);
        if (oLocVOs != null) {
          voaLoc = (LocatorVO[])(LocatorVO[])oLocVOs;
          if ((voaLoc == null) || (voaLoc.length <= 0) || (voaLoc[0] == null) || (voaLoc[0].getCgeneralbid() == null) || (htData.containsKey(voaLoc[0].getCgeneralbid())))
          {
            continue;
          }

          htData.put(voaLoc[0].getCgeneralbid(), voaLoc);
        }
      }
      setLocators(htData);
    }
  }

  public void setBorrowLendLocators(Hashtable htData)
  {
    if ((this.m_voaItem != null) && (htData != null)) {
      LocatorVO[] voaLoc = null;
      for (int i = 0; i < this.m_voaItem.length; i++)
        if ((this.m_voaItem[i] != null) && (this.m_voaItem[i].getCfirstbillbid() != null)) {
          voaLoc = (LocatorVO[])(LocatorVO[])htData.get(this.m_voaItem[i].getCfirstbillbid());

          this.m_voaItem[i].setLocator(voaLoc);
        }
    }
  }

  public void setBorrowLendSNs(ArrayList alData)
  {
    Hashtable htData = null;

    if ((this.m_voaItem != null) && (alData != null))
    {
      htData = new Hashtable();
      SerialVO[] voaSN = null;
      for (int j = 0; j < alData.size(); j++) {
        Object oSNVOs = alData.get(j);
        if (oSNVOs != null) {
          voaSN = (SerialVO[])(SerialVO[])oSNVOs;
          if ((voaSN == null) || (voaSN.length <= 0) || (voaSN[0] == null) || (voaSN[0].getCgeneralbid() == null) || (htData.containsKey(voaSN[0].getCgeneralbid())))
          {
            continue;
          }

          htData.put(voaSN[0].getCgeneralbid(), voaSN);
        }
      }
      setSNs(htData);
    }
  }

  public void setBorrowLendSNs(Hashtable htData)
  {
    if ((this.m_voaItem != null) && (htData != null)) {
      SerialVO[] voaSn = null;
      for (int i = 0; i < this.m_voaItem.length; i++)
        if ((this.m_voaItem[i] != null) && (this.m_voaItem[i].getCfirstbillbid() != null)) {
          voaSn = (SerialVO[])(SerialVO[])htData.get(this.m_voaItem[i].getCfirstbillbid());

          this.m_voaItem[i].setSerial(voaSn);
        }
    }
  }

  public void setChildrenVO(CircularlyAccessibleValueObject[] children)
  {
    this.m_voaItem = ((GeneralBillItemVO[])(GeneralBillItemVO[])children);
  }

  public void setDispItemNull()
  {
    if (this.m_voHeader != null)
      this.m_voHeader.setDispItemNull();
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++)
        this.m_voaItem[i].setDispItemNull();
  }

  public void setErrMsg(String s)
  {
    if (this.m_sErr != null)
      this.m_sErr = (this.m_sErr + "\n" + s);
    else
      this.m_sErr = ("\n" + s);
  }

  public void setHaveCalConvRate(boolean newHaveCalConvRate)
  {
    this.m_bHaveCalConvRate = newHaveCalConvRate;
  }

  public void setHeaderValue(String sKey, Object oValue)
  {
    if (this.m_voHeader != null)
      this.m_voHeader.setAttributeValue(sKey, oValue);
  }

  public void setIDClearBarcodeItems(GeneralBillVO voBill)
  {
    if (voBill != null)
    {
      GeneralBillItemVO[] voBi = voBill.getItemVOs();
      if ((this.m_voaItem != null) && (voBi != null))
        for (int i = 0; i < this.m_voaItem.length; i++)
          if ((i < voBi.length) && (voBi[i] != null))
            this.m_voaItem[i].setIDClearBarcodeItems(voBi[i]);
    }
  }

  public void setIDItems(GeneralBillVO voBill)
  {
    if (voBill != null)
    {
      if (this.m_voHeader != null) {
        this.m_voHeader.setIDItems(voBill.getHeaderVO());
      }
      GeneralBillItemVO[] voBi = voBill.getItemVOs();
      if ((this.m_voaItem != null) && (voBi != null))
        for (int i = 0; i < this.m_voaItem.length; i++)
          if ((i < voBi.length) && (voBi[i] != null))
            this.m_voaItem[i].setIDItems(voBi[i]);
    }
  }

  public void setItem(int row, GeneralBillItemVO vo)
  {
    if ((this.m_voaItem != null) && (row >= 0) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null))
      this.m_voaItem[row] = vo;
  }

  public void setItemBarCodeVOs(int row, BarCodeVO[] barCodeVOs)
  {
    if ((this.m_voaItem != null) && (row >= 0) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null))
      this.m_voaItem[row].setBarCodeVOs(barCodeVOs);
  }

  public void setItemFreeVO(int row, FreeVO voFree)
  {
    if ((this.m_voaItem != null) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null))
    {
      for (int i = 0; i < 10; i++)
      {
        if (null != voFree) {
          setItemValue(row, "vfree" + Integer.toString(i + 1), voFree.getAttributeValue("vfree" + Integer.toString(i + 1)));
        }
        else
        {
          setItemValue(row, "vfree" + Integer.toString(i + 1).trim(), null);
        }
      }
    }
  }

  public void setItemInv(int row, InvVO voInv)
  {
    if ((this.m_voaItem != null) && (row >= 0) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null))
      this.m_voaItem[row].setInv(voInv);
  }

  public void setItemValue(int row, String sKey, Object oValue)
  {
    if ((this.m_voaItem != null) && (row >= 0) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null))
      this.m_voaItem[row].setAttributeValue(sKey, oValue);
  }

  public void setIUK_IncludeSourceBodyid(boolean b)
  {
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++)
        this.m_voaItem[i].setBisIUK_SourceBodyidOnly(b);
  }

  public void setLocators(ArrayList alData)
  {
    Hashtable htData = null;

    if (this.m_voaItem != null)
      if (alData != null) {
        htData = new Hashtable();
        LocatorVO[] voaLoc = null;

        for (int j = 0; j < alData.size(); j++) {
          Object oLocVOs = alData.get(j);
          if (oLocVOs != null) {
            voaLoc = (LocatorVO[])(LocatorVO[])oLocVOs;
            if ((voaLoc == null) || (voaLoc.length <= 0) || (voaLoc[0] == null) || (voaLoc[0].getCgeneralbid() == null) || (htData.containsKey(voaLoc[0].getCgeneralbid())))
            {
              continue;
            }

            htData.put(voaLoc[0].getCgeneralbid(), voaLoc);
          }
        }
        setLocators(htData);
      }
      else
      {
        for (int i = 0; i < this.m_voaItem.length; i++)
          if (this.m_voaItem[i] != null)
            this.m_voaItem[i].setLocator(null);
      }
  }

  public void setLocators(Hashtable htData)
  {
    if ((this.m_voaItem != null) && (htData != null)) {
      LocatorVO[] voaLoc = null;
      for (int i = 0; i < this.m_voaItem.length; i++)
        if ((this.m_voaItem[i] != null) && (this.m_voaItem[i].getPrimaryKey() != null)) {
          voaLoc = (LocatorVO[])(LocatorVO[])htData.get(this.m_voaItem[i].getPrimaryKey());

          this.m_voaItem[i].setLocator(voaLoc);
        }
    }
  }

  public void setLocStatus(GeneralBillVO voOld)
  {
    if ((voOld != null) && (voOld.getItemCount() > 0) && (this.m_voaItem != null)) {
      GeneralBillItemVO[] voaOldItem = voOld.getItemVOs();
      GeneralBillItemVO voTempItem = null;
      Hashtable htItems = new Hashtable();

      for (int i = 0; i < voaOldItem.length; i++) {
        if ((voaOldItem[i] != null) && (voaOldItem[i].getPrimaryKey() != null)) {
          htItems.put(voaOldItem[i].getPrimaryKey(), voaOldItem[i]);
        }
      }

      boolean bSnFullUpdated = false;
      if (this.m_voHeader != null) {
        bSnFullUpdated = (this.m_voHeader.getStatus() == 1) && (this.m_voHeader.isRelDataUpdated(voOld.getHeaderVO()));
      }
      else
      {
        SCMEnv.out("nvl head...");
      }
      if (bSnFullUpdated) {
        for (int i = 0; i < this.m_voaItem.length; i++)
          if (this.m_voaItem[i] != null)
            this.m_voaItem[i].setSnStatus(1);
      }
      int iLineStatus = 0;
      for (int line = 0; line < this.m_voaItem.length; line++)
        if (this.m_voaItem[line] != null) {
          iLineStatus = this.m_voaItem[line].getStatus();

          this.m_voaItem[line].setLocStatus(iLineStatus);

          if (this.m_voaItem[line].getPrimaryKey() != null) {
            voTempItem = (GeneralBillItemVO)htItems.get(this.m_voaItem[line].getPrimaryKey());

            if ((iLineStatus != 0) && (iLineStatus != 1)) {
              continue;
            }
            this.m_voaItem[line].setLocStatus(voTempItem);

            if (!bSnFullUpdated)
              this.m_voaItem[line].setSnStatus(voTempItem);
          }
        }
    }
  }

  public void setNewPK(ArrayList alPK)
  {
    if ((alPK != null) && (alPK.size() >= 2))
    {
      if ((this.m_voHeader != null) && (alPK.get(0) != null) && (alPK.get(0).toString().trim().length() > 0))
      {
        this.m_voHeader.setPrimaryKey(alPK.get(0).toString().trim());
      }
      if (this.m_voaItem != null) {
        int iCount = 1;
        for (int i = 0; i < this.m_voaItem.length; i++)
        {
          if ((this.m_voaItem[i].getStatus() != 2) || (iCount >= alPK.size()) || (alPK.get(iCount) == null) || (alPK.get(iCount).toString().trim().length() <= 0))
          {
            continue;
          }
          this.m_voaItem[i].setPrimaryKey(alPK.get(iCount).toString().trim());
          iCount++;
        }
      }
    }
  }

  public void setNumNull()
  {
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++) {
        this.m_voaItem[i].setNoutnum(null);
        this.m_voaItem[i].setNoutassistnum(null);
        this.m_voaItem[i].setNinnum(null);
        this.m_voaItem[i].setNinassistnum(null);
      }
  }

  public void setOperatelogVO(OperatelogVO newOperatelogVO)
  {
    this.m_OperatelogVO = newOperatelogVO;
  }

  public void setParentVO(CircularlyAccessibleValueObject parent)
  {
    this.m_voHeader = ((GeneralBillHeaderVO)parent);
  }

  public void setSaveBadBarcode(boolean newSaveBadBarcode)
  {
    this.m_bSaveBadBarcode = newSaveBadBarcode;
  }

  public void setSaveBarcode(boolean newSaveBarcode)
  {
    this.m_bSaveBarcode = newSaveBarcode;
  }

  public void setSaveBarcodeDelFirst(boolean newSaveBarcodeDelFirst)
  {
    this.m_bSaveBarcodeDelFirst = newSaveBarcodeDelFirst;
  }

  public void setShouldAstNum()
  {
    if (this.m_voaItem != null)
    {
      UFDouble ufdhsl = null;

      UFDouble ufdAstInNum = null;
      UFDouble ufdAstOutNum = null;

      UFDouble ufdInNum = null;
      UFDouble ufdOutNum = null;
      for (int i = 0; i < this.m_voaItem.length; i++) {
        ufdhsl = this.m_voaItem[i].getHsl();
        ufdInNum = this.m_voaItem[i].getNshouldinnum();
        ufdOutNum = this.m_voaItem[i].getNshouldoutnum();
        if (ufdhsl != null)
          if (ufdInNum != null) {
            ufdAstInNum = ufdInNum.div(ufdhsl);
            this.m_voaItem[i].setNshouldoutassistnum(ufdAstInNum);
          } else if (ufdOutNum != null) {
            ufdAstOutNum = ufdOutNum.div(ufdhsl);
            this.m_voaItem[i].setNshouldoutassistnum(ufdAstOutNum);
          }
      }
    }
  }

  public void setShouldInfromBackNum()
  {
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++)
        this.m_voaItem[i].setShouldInfromBackNum();
  }

  public void setSmallBillVO(SMGeneralBillVO smbillvo)
  {
    this.m_SmGenBillVO = smbillvo;

    SMGeneralBillHeaderVO smbillHeadvo = smbillvo.getHeaderVO();
    SMGeneralBillItemVO[] smbillItemvo = (SMGeneralBillItemVO[])(SMGeneralBillItemVO[])smbillvo.getChildrenVO();

    GeneralBillHeaderVO billHeadvo = getHeaderVO();
    if (billHeadvo != null) {
      billHeadvo.setCgeneralhid(smbillHeadvo.getCgeneralhid());
      billHeadvo.setFbillflag(smbillHeadvo.getFbillflag());
      billHeadvo.setTs(smbillHeadvo.getTs());
      billHeadvo.setVbillcode(smbillHeadvo.getVbillcode());
    }

    GeneralBillItemVO[] billItemvos = (GeneralBillItemVO[])(GeneralBillItemVO[])getChildrenVO();

    if ((billItemvos != null) && (billItemvos.length > 0))
    {
      BarCodeVO[] barcodeVOs = null;

      for (int i = 0; i < billItemvos.length; i++)
        if (billItemvos[i] != null) {
          billItemvos[i].setCgeneralbid(smbillItemvo[i].getCgeneralbid());
          billItemvos[i].setCgeneralhid(smbillItemvo[i].getCgeneralhid());
          billItemvos[i].setNinnum(smbillItemvo[i].getNinnum());
          billItemvos[i].setNinassistnum(smbillItemvo[i].getNinassistnum());
          billItemvos[i].setNoutassistnum(smbillItemvo[i].getNoutassistnum());
          billItemvos[i].setNoutnum(smbillItemvo[i].getNoutnum());
          billItemvos[i].setVfirstbillcode(smbillItemvo[i].getVfirstbillcode());
          billItemvos[i].setTs(smbillItemvo[i].getTs());
          billItemvos[i].setVbatchcode(smbillItemvo[i].getVbatchcode());

          billItemvos[i].setBarcodeClose(smbillItemvo[i].getBarcodeClose());
          billItemvos[i].setListnbarcodenum(smbillItemvo[i].getListbarcodenums());
          billItemvos[i].setDvalidate((UFDate)smbillItemvo[i].getAttributeValue("dvalidate"));
          billItemvos[i].setAttributeValue("scrq", smbillItemvo[i].getAttributeValue("scrq"));

          barcodeVOs = smbillItemvo[i].getBarCodeVOs();
          billItemvos[i].setBarCodeVOs(barcodeVOs);

          billItemvos[i].setCspaceid((String)smbillItemvo[i].getAttributeValue("cspaceid"));
          billItemvos[i].setVspacename((String)smbillItemvo[i].getAttributeValue("vspacename"));
          billItemvos[i].setLocator((LocatorVO[])(LocatorVO[])smbillItemvo[i].getAttributeValue("locator"));
          billItemvos[i].setCcorrespondbid(smbillItemvo[i].getCcorrespondbid());
          billItemvos[i].setCcorrespondcode(smbillItemvo[i].getCcorrespondcode());
          billItemvos[i].setCcorrespondhid(smbillItemvo[i].getCcorrespondhid());
          billItemvos[i].setCcorrespondtype(smbillItemvo[i].getCcorrespondtype());
          billItemvos[i].setCcorrespondtypename(smbillItemvo[i].getCcorrespondtypename());
          billItemvos[i].setAttributeValue("cparentid", smbillItemvo[i].getAttributeValue("cparentid"));
          billItemvos[i].setAttributeValue("nmny", smbillItemvo[i].getAttributeValue("nmny"));
          billItemvos[i].setAttributeValue("nprice", smbillItemvo[i].getAttributeValue("nprice"));
          billItemvos[i].setAttributeValue("ntaxmny", smbillItemvo[i].getAttributeValue("ntaxmny"));
          billItemvos[i].setAttributeValue("ntaxprice", smbillItemvo[i].getAttributeValue("ntaxprice"));
          billItemvos[i].setAttributeValue("nsalemny", smbillItemvo[i].getAttributeValue("nsalemny"));
          billItemvos[i].setAttributeValue("nsaleprice", smbillItemvo[i].getAttributeValue("nsaleprice"));
        }
    }
  }

  public void setSNs(ArrayList alData)
  {
    Hashtable htData = null;

    if (this.m_voaItem != null)
      if (alData != null)
      {
        htData = new Hashtable();
        SerialVO[] voaSN = null;
        for (int j = 0; j < alData.size(); j++) {
          Object oSNVOs = alData.get(j);
          if (oSNVOs != null) {
            voaSN = (SerialVO[])(SerialVO[])oSNVOs;
            if ((voaSN == null) || (voaSN.length <= 0) || (voaSN[0] == null) || (voaSN[0].getCgeneralbid() == null) || (htData.containsKey(voaSN[0].getCgeneralbid())))
            {
              continue;
            }

            htData.put(voaSN[0].getCgeneralbid(), voaSN);
          }
        }
        setSNs(htData);
      }
      else
      {
        for (int i = 0; i < this.m_voaItem.length; i++)
          if (this.m_voaItem[i] != null)
            this.m_voaItem[i].setSerial(null);
      }
  }

  public void setSNs(Hashtable htData)
  {
    if ((this.m_voaItem != null) && (htData != null)) {
      SerialVO[] voaSn = null;
      for (int i = 0; i < this.m_voaItem.length; i++)
        if ((this.m_voaItem[i] != null) && (this.m_voaItem[i].getPrimaryKey() != null)) {
          voaSn = (SerialVO[])(SerialVO[])htData.get(this.m_voaItem[i].getPrimaryKey());

          this.m_voaItem[i].setSerial(voaSn);
        }
    }
  }

  public void setStatus(int status)
  {
    if (this.m_voHeader != null) {
      this.m_voHeader.setStatus(status);
    }
    if (this.m_voaItem != null)
      for (int i = 0; i < this.m_voaItem.length; i++)
        this.m_voaItem[i].setStatus(status);
  }

  public void setTs(GeneralBillVO voBill)
  {
    if (voBill != null)
    {
      if (this.m_voHeader != null) {
        this.m_voHeader.setTs(voBill.getHeaderVO().getTs());
      }
      GeneralBillItemVO[] voBi = voBill.getItemVOs();
      if ((this.m_voaItem != null) && (voBi != null))
        for (int i = 0; i < this.m_voaItem.length; i++)
          if ((i < voBi.length) && (voBi[i] != null))
            this.m_voaItem[i].setTs(voBi[i].getTs());
    }
  }

  public void setUkUseAstUOM(boolean newUkUseAstUOM)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseAstUOM(newUkUseAstUOM);
  }

  public void setUkUseCorrbid(boolean newUkUseCorrbid)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseCorrbid(newUkUseCorrbid);
  }

  public void setUkUseCorrhid(boolean newUkUseCorrhid)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseCorrhid(newUkUseCorrhid);
  }

  public void setUkUseFirstbid(boolean newUkUseFirstbid)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseFirstbid(newUkUseFirstbid);
  }

  public void setUkUseFirsthid(boolean newUkUseFirsthid)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseFirsthid(newUkUseFirsthid);
  }

  public void setUkUseFreeItem(boolean newUkUseFreeItem)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseFreeItem(newUkUseFreeItem);
  }

  public void setUkUseInvID(boolean newUkUseInvID)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseInvID(newUkUseInvID);
  }

  public void setUkUseLot(boolean newUkUseLot)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseLot(newUkUseLot);
  }

  public void setUkUseSourcebid(boolean newUkUseSourcebid)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseSourcehid(newUkUseSourcebid);
  }

  public void setUkUseSourcehid(boolean newUkUseSourcehid)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseSourcehid(newUkUseSourcehid);
  }

  public void setUkUseVendorid(boolean newUkUseCorrbid)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUkUseVendorid(newUkUseCorrbid);
  }

  public void setUseUkSetup(boolean newUseUkSetup)
  {
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
      for (int l = 0; l < this.m_voaItem.length; l++)
        if (this.m_voaItem[l] != null)
          this.m_voaItem[l].setUseUkSetup(newUseUkSetup);
  }

  public void setVBillCode(String sBillCode)
  {
    if (this.m_voHeader != null)
      this.m_voHeader.setVbillcode(sBillCode);
  }

  public void setWasteWh(WhVO voWh)
  {
    if (this.m_voHeader != null) {
      this.m_voHeader.setCwastewarehouseid(voWh == null ? null : voWh.getCwarehouseid());
      this.m_voHeader.setCwastewarehousename(voWh == null ? null : voWh.getCwarehousename());

      this.m_voHeader.setIsWasteWh2(voWh == null ? null : voWh.getIsWasteWh());

      this.m_voHeader.setPk_corp(voWh == null ? null : voWh.getPk_corp());
      this.m_voHeader.setPk_calbody(voWh == null ? null : voWh.getPk_calbody());
      this.m_voHeader.setVcalbodyname(voWh == null ? null : voWh.getVcalbodyname());
    }
  }

  public void setWh(WhVO voWh)
  {
    if (this.m_voHeader != null) {
      this.m_voHeader.setCwarehouseid(voWh == null ? null : voWh.getCwarehouseid());
      this.m_voHeader.setCwarehousename(voWh == null ? null : voWh.getCwarehousename());
      this.m_voHeader.setIsWasteWh(voWh == null ? null : voWh.getIsWasteWh());
      this.m_voHeader.setIsLocatorMgt(voWh == null ? null : voWh.getIsLocatorMgt());
      this.m_voHeader.setWhReservedPty(voWh == null ? null : voWh.getWhReservedPty());
      this.m_voHeader.setPk_corp(voWh == null ? null : voWh.getPk_corp());
      this.m_voHeader.setPk_calbody(voWh == null ? null : voWh.getPk_calbody());
      this.m_voHeader.setVcalbodyname(voWh == null ? null : voWh.getVcalbodyname());

      this.m_voHeader.setIsforeignstor(voWh == null ? null : voWh.getIsforeignstor());
      this.m_voHeader.setIsgathersettle(voWh == null ? null : voWh.getIsgathersettle());
      this.m_voHeader.setIscalculatedinvcost(voWh == null ? null : voWh.getIscalculatedinvcost());

      this.m_voHeader.setIsdirectstore(voWh == null ? null : voWh.getIsdirectstore());
    }
  }

  public void synProvider()
  {
    if ((getBillInOutFlag() == 1) && 
      (this.m_voHeader != null)) {
      String cproviderid = this.m_voHeader.getCproviderid();
      if ((this.m_voaItem != null) && (this.m_voaItem.length > 0)) {
        int size = this.m_voaItem.length;
        for (int i = 0; i < size; i++) {
          if (this.m_voaItem[i] == null)
            continue;
          if (GenMethod.isStringEqual(this.m_voaItem[i].getCvendorid(), cproviderid))
          {
            continue;
          }

          if (this.m_voaItem[i].getStatus() == 0)
            this.m_voaItem[i].setStatus(1);
        }
      }
    }
  }

  public void sub(GeneralBillItemVO[] voaSubedItem)
  {
    this.m_voaItem = sub(this.m_voaItem, voaSubedItem);
  }

  public GeneralBillItemVO[] sub(GeneralBillItemVO[] voaItem, GeneralBillItemVO[] voaSubedItem)
  {
    if ((voaItem == null) || (voaItem.length == 0)) {
      return neg(voaSubedItem);
    }

    if ((voaSubedItem == null) || (voaSubedItem.length == 0)) {
      return voaItem;
    }
    int iBillInOutFlag = getBillInOutFlag();

    Hashtable htNewBillBodyKey = new Hashtable();
    for (int i = 0; i < voaItem.length; i++) {
      if ((voaItem[i] == null) || (htNewBillBodyKey.containsKey(voaItem[i].getPrimaryKey())))
        continue;
      htNewBillBodyKey.put(voaItem[i].getPrimaryKey(), "" + i);
    }

    Hashtable htBodyKey = new Hashtable();

    ArrayList alDeletedBodyKey = new ArrayList();
    for (int i = 0; i < voaSubedItem.length; i++) {
      if ((voaSubedItem[i] == null) || (htBodyKey.containsKey(voaSubedItem[i].getPrimaryKey())))
        continue;
      htBodyKey.put(voaSubedItem[i].getPrimaryKey(), "" + i);

      if (!htNewBillBodyKey.containsKey(voaSubedItem[i].getPrimaryKey())) {
        alDeletedBodyKey.add("" + i);
      }
    }
    int iLineNum2 = 0;
    Object oValueTemp = null;
    UFDate ufdNewValidate = null;
    UFDate ufdOldValidate = null;
    for (int i = 0; i < voaItem.length; i++) {
      if (voaItem[i] == null)
        continue;
      oValueTemp = htBodyKey.get(voaItem[i].getPrimaryKey());

      if (oValueTemp == null) {
        continue;
      }
      iLineNum2 = Integer.valueOf(oValueTemp.toString()).intValue();

      ufdNewValidate = voaItem[i].getDvalidate();
      ufdOldValidate = voaSubedItem[iLineNum2].getDvalidate();
      if (((ufdNewValidate == null) && (ufdOldValidate != null)) || ((ufdNewValidate != null) && (ufdOldValidate == null)) || ((ufdNewValidate != null) && (ufdOldValidate != null) && (ufdNewValidate.compareTo(ufdOldValidate) != 0)))
      {
        voaItem[i].setValidateUpdateFlg(true);
      }

      if (iBillInOutFlag == 1) {
        if (voaItem[i].getNinnum() != null) {
          if (voaSubedItem[iLineNum2].getNinnum() != null) {
            voaItem[i].setNinnum(voaItem[i].getNinnum().sub(voaSubedItem[iLineNum2].getNinnum()));
          }
        }
        else if (voaSubedItem[iLineNum2].getNinnum() != null) {
          voaItem[i].setNinnum(this.ZERO.sub(voaSubedItem[iLineNum2].getNinnum()));
        }

        if (voaItem[i].getNinassistnum() != null) {
          if (voaSubedItem[iLineNum2].getNinassistnum() != null) {
            voaItem[i].setNinassistnum(voaItem[i].getNinassistnum().sub(voaSubedItem[iLineNum2].getNinassistnum()));
          }

        }
        else if (voaSubedItem[iLineNum2].getNinassistnum() != null)
          voaItem[i].setNinassistnum(this.ZERO.sub(voaSubedItem[iLineNum2].getNinassistnum()));
      }
      else {
        if (voaItem[i].getNoutnum() != null) {
          if (voaSubedItem[iLineNum2].getNoutnum() != null) {
            voaItem[i].setNoutnum(voaItem[i].getNoutnum().sub(voaSubedItem[iLineNum2].getNoutnum()));
          }

        }
        else if (voaSubedItem[iLineNum2].getNoutnum() != null) {
          voaItem[i].setNoutnum(this.ZERO.sub(voaSubedItem[iLineNum2].getNoutnum()));
        }

        if (voaItem[i].getNoutassistnum() != null) {
          if (voaSubedItem[iLineNum2].getNoutassistnum() != null) {
            voaItem[i].setNoutassistnum(voaItem[i].getNoutassistnum().sub(voaSubedItem[iLineNum2].getNoutassistnum()));
          }

        }
        else if (voaSubedItem[iLineNum2].getNoutassistnum() != null) {
          voaItem[i].setNoutassistnum(this.ZERO.sub(voaSubedItem[iLineNum2].getNoutassistnum()));
        }

      }

      voaItem[i].subLocator(voaSubedItem[iLineNum2].getLocator());
    }

    if (alDeletedBodyKey.size() > 0) {
      GeneralBillItemVO[] voaDeletedItem = new GeneralBillItemVO[alDeletedBodyKey.size()];

      int delete_index = 0;
      for (int del = 0; del < alDeletedBodyKey.size(); del++) {
        delete_index = Integer.valueOf(alDeletedBodyKey.get(del).toString()).intValue();
        voaDeletedItem[del] = voaSubedItem[delete_index];
      }
      neg(voaDeletedItem);

      GeneralBillItemVO[] voaWholeItem = new GeneralBillItemVO[voaItem.length + alDeletedBodyKey.size()];

      for (int i = 0; i < voaItem.length; i++)
        voaWholeItem[i] = voaItem[i];
      for (int i = 0; i < voaDeletedItem.length; i++)
        voaWholeItem[(i + voaItem.length)] = voaDeletedItem[i];
      voaItem = voaWholeItem;
    }
    return voaItem;
  }

  public void subShouldNum(GeneralBillItemVO[] voaSubedItem)
  {
    this.m_voaItem = subShouldNum(this.m_voaItem, voaSubedItem);
  }

  public GeneralBillItemVO[] subShouldNum(GeneralBillItemVO[] voaItem, GeneralBillItemVO[] voaSubedItem)
  {
    if ((voaItem == null) || (voaItem.length == 0)) {
      return neg(voaSubedItem);
    }

    if ((voaSubedItem == null) || (voaSubedItem.length == 0)) {
      return voaItem;
    }
    int iBillInOutFlag = getBillInOutFlag();

    Hashtable htNewBillBodyKey = new Hashtable();
    for (int i = 0; i < voaItem.length; i++) {
      if ((voaItem[i] != null) && (!htNewBillBodyKey.containsKey(voaItem[i].getPrimaryKey()))) {
        htNewBillBodyKey.put(voaItem[i].getPrimaryKey(), "" + i);
      }
    }
    Hashtable htBodyKey = new Hashtable();

    ArrayList alDeletedBodyKey = new ArrayList();
    for (int i = 0; i < voaSubedItem.length; i++) {
      if ((voaSubedItem[i] != null) && (!htBodyKey.containsKey(voaSubedItem[i].getPrimaryKey()))) {
        htBodyKey.put(voaSubedItem[i].getPrimaryKey(), "" + i);

        if (!htNewBillBodyKey.containsKey(voaSubedItem[i].getPrimaryKey()))
          alDeletedBodyKey.add("" + i);
      }
    }
    int iLineNum2 = 0;
    Object oValueTemp = null;
    for (int i = 0; i < voaItem.length; i++) {
      if (voaItem[i] == null)
        continue;
      oValueTemp = htBodyKey.get(voaItem[i].getPrimaryKey());

      if (oValueTemp == null) {
        continue;
      }
      iLineNum2 = Integer.valueOf(oValueTemp.toString()).intValue();

      if (iBillInOutFlag == 1) {
        if (voaItem[i].getNshouldinnum() != null) {
          if (voaSubedItem[iLineNum2].getNshouldinnum() != null) {
            voaItem[i].setNshouldinnum(voaItem[i].getNshouldinnum().sub(voaSubedItem[iLineNum2].getNshouldinnum()));
          }

        }
        else if (voaSubedItem[iLineNum2].getNshouldinnum() != null) {
          voaItem[i].setNshouldinnum(this.ZERO.sub(voaSubedItem[iLineNum2].getNshouldinnum()));
        }

        if (voaItem[i].getNneedinassistnum() != null) {
          if (voaSubedItem[iLineNum2].getNneedinassistnum() != null) {
            voaItem[i].setNneedinassistnum(voaItem[i].getNneedinassistnum().sub(voaSubedItem[iLineNum2].getNneedinassistnum()));
          }

        }
        else if (voaSubedItem[iLineNum2].getNneedinassistnum() != null)
          voaItem[i].setNneedinassistnum(this.ZERO.sub(voaSubedItem[iLineNum2].getNneedinassistnum()));
      }
      else
      {
        if (voaItem[i].getNshouldoutnum() != null) {
          if (voaSubedItem[iLineNum2].getNshouldoutnum() != null) {
            voaItem[i].setNshouldoutnum(voaItem[i].getNshouldoutnum().sub(voaSubedItem[iLineNum2].getNshouldoutnum()));
          }

        }
        else if (voaSubedItem[iLineNum2].getNshouldoutnum() != null) {
          voaItem[i].setNshouldoutnum(this.ZERO.sub(voaSubedItem[iLineNum2].getNshouldoutnum()));
        }

        if (voaItem[i].getNshouldoutassistnum() != null) {
          if (voaSubedItem[iLineNum2].getNshouldoutassistnum() != null) {
            voaItem[i].setNshouldoutassistnum(voaItem[i].getNshouldoutassistnum().sub(voaSubedItem[iLineNum2].getNshouldoutassistnum()));
          }

        }
        else if (voaSubedItem[iLineNum2].getNshouldoutassistnum() != null) {
          voaItem[i].setNshouldoutassistnum(this.ZERO.sub(voaSubedItem[iLineNum2].getNshouldoutassistnum()));
        }
      }

    }

    if (alDeletedBodyKey.size() > 0) {
      GeneralBillItemVO[] voaDeletedItem = new GeneralBillItemVO[alDeletedBodyKey.size()];
      int delete_index = 0;
      for (int del = 0; del < alDeletedBodyKey.size(); del++) {
        delete_index = Integer.valueOf(alDeletedBodyKey.get(del).toString()).intValue();
        voaDeletedItem[del] = voaSubedItem[delete_index];
      }
      neg(voaDeletedItem);

      GeneralBillItemVO[] voaWholeItem = new GeneralBillItemVO[voaItem.length + alDeletedBodyKey.size()];
      for (int i = 0; i < voaItem.length; i++)
        voaWholeItem[i] = voaItem[i];
      for (int i = 0; i < voaDeletedItem.length; i++)
        voaWholeItem[(i + voaItem.length)] = voaDeletedItem[i];
      voaItem = voaWholeItem;
    }
    return voaItem;
  }

  public void sumInvQty()
  {
    this.m_voaItem = sumInvQty(this.m_voaItem);
  }

  public GeneralBillItemVO[] sumInvQty(GeneralBillItemVO[] voaParamItem)
  {
    return sumInvQty(voaParamItem, true);
  }

  protected GeneralBillItemVO[] sumInvQty(GeneralBillItemVO[] voaParamItem, boolean bUseDelRow)
  {
    if ((voaParamItem == null) || (voaParamItem.length == 0)) {
      return voaParamItem;
    }

    GeneralBillItemVO[] voaItem = (GeneralBillItemVO[])(GeneralBillItemVO[])voaParamItem.clone();

    int iBillInOutFlag = getBillInOutFlag();

    int iVoStatus1 = 3;
    int iVoStatus2 = 3;

    UFDouble dInvSumNum = null;
    UFDouble dInvSumAstNum = null;
    Vector vSumLoc = null;
    UFDouble dItemNum = null;
    UFDouble dItemAstNum = null;
    Hashtable htBodyKey = new Hashtable();

    String sKey = null;
    String sKey2 = null;
    Vector vResultItem = new Vector();
    GeneralBillItemVO voTempItem = null;
    LocatorVO[] voaLoc = null;

    for (int i = 0; i < voaItem.length; i++)
    {
      iVoStatus1 = voaItem[i].getStatus();
      if ((iVoStatus1 == 3) && (!bUseDelRow))
        continue;
      dInvSumNum = this.ZERO;
      dInvSumAstNum = this.ZERO;
      vSumLoc = null;
      sKey = voaItem[i].getInvUniqueKey();
      if (htBodyKey.containsKey(sKey)) {
        continue;
      }
      htBodyKey.put(sKey, "");

      for (int j = i; j < voaItem.length; j++) {
        iVoStatus2 = voaItem[j].getStatus();

        if ((iVoStatus2 == 3) && (!bUseDelRow)) {
          continue;
        }
        sKey2 = voaItem[j].getInvUniqueKey();

        if (!sKey.equals(sKey2))
          continue;
        if ((iBillInOutFlag == 1) && (voaItem[j].getNinnum() != null))
          dItemNum = voaItem[j].getNinnum();
        else if ((iBillInOutFlag == -1) && (voaItem[j].getNoutnum() != null))
          dItemNum = voaItem[j].getNoutnum();
        else {
          dItemNum = this.ZERO;
        }
        if ((iBillInOutFlag == 1) && (voaItem[j].getNinassistnum() != null))
          dItemAstNum = voaItem[j].getNinassistnum();
        else if ((iBillInOutFlag == -1) && (voaItem[j].getNoutassistnum() != null))
          dItemAstNum = voaItem[j].getNoutassistnum();
        else {
          dItemAstNum = this.ZERO;
        }
        if (iVoStatus2 == 3) {
          dInvSumNum = dInvSumNum.sub(dItemNum);
          dInvSumAstNum = dInvSumAstNum.sub(dItemAstNum);
        }
        else {
          dInvSumNum = dInvSumNum.add(dItemNum);
          dInvSumAstNum = dInvSumAstNum.add(dItemAstNum);
        }
        voaLoc = voaItem[j].getLocator();
        if (voaLoc != null) {
          if (vSumLoc == null)
            vSumLoc = new Vector();
          for (int loc = 0; loc < voaLoc.length; loc++) {
            vSumLoc.add(voaLoc[loc]);
          }
        }
      }

      voTempItem = (GeneralBillItemVO)voaItem[i].clone();

      if (iBillInOutFlag == 1) {
        voTempItem.setNinnum(dInvSumNum);
        voTempItem.setNinassistnum(dInvSumAstNum);
      }
      else {
        voTempItem.setNoutnum(dInvSumNum);
        voTempItem.setNoutassistnum(dInvSumAstNum);
      }

      if ((vSumLoc != null) && (vSumLoc.size() > 0)) {
        voaLoc = new LocatorVO[vSumLoc.size()];
        vSumLoc.copyInto(voaLoc);
        voTempItem.setLocator(voaLoc);

        voTempItem.sumLocator();
      }

      voTempItem.setPrimaryKey(sKey.toString());

      vResultItem.addElement(voTempItem);
    }

    GeneralBillItemVO[] voaRetItem = null;
    if (vResultItem.size() > 0) {
      voaRetItem = new GeneralBillItemVO[vResultItem.size()];
      vResultItem.copyInto(voaRetItem);
    }
    return voaRetItem;
  }

  public void sumInvQtyNotUseDelRow()
  {
    this.m_voaItem = sumInvQtyNotUseDelRow(this.m_voaItem);
  }

  public GeneralBillItemVO[] sumInvQtyNotUseDelRow(GeneralBillItemVO[] voaParamItem)
  {
    return sumInvQty(voaParamItem, false);
  }

  public void sumShouldNotUseDelRow()
  {
    this.m_voaItem = sumShouldNotUseDelRow(this.m_voaItem);
  }

  public GeneralBillItemVO[] sumShouldNotUseDelRow(GeneralBillItemVO[] voaParamItem)
  {
    return sumShouldNum(voaParamItem, false);
  }

  public void sumShouldNum()
  {
    this.m_voaItem = sumShouldNum(this.m_voaItem, true);
  }

  protected GeneralBillItemVO[] sumShouldNum(GeneralBillItemVO[] voaParamItem, boolean bUseDelRow)
  {
    if ((voaParamItem == null) || (voaParamItem.length == 0)) {
      return voaParamItem;
    }

    GeneralBillItemVO[] voaItem = (GeneralBillItemVO[])(GeneralBillItemVO[])voaParamItem.clone();

    int iBillInOutFlag = getBillInOutFlag();

    int iVoStatus1 = 3;
    int iVoStatus2 = 3;

    UFDouble dInvSumNum = null;
    UFDouble dInvSumAstNum = null;

    UFDouble dItemNum = null;
    UFDouble dItemAstNum = null;
    Hashtable htBodyKey = new Hashtable();

    String sKey = null;
    String sKey2 = null;
    Vector vResultItem = new Vector();
    GeneralBillItemVO voTempItem = null;

    for (int i = 0; i < voaItem.length; i++)
    {
      iVoStatus1 = voaItem[i].getStatus();
      if ((iVoStatus1 == 3) && (!bUseDelRow))
        continue;
      dInvSumNum = this.ZERO;
      dInvSumAstNum = this.ZERO;

      sKey = voaItem[i].getInvUniqueKey();
      if (htBodyKey.containsKey(sKey)) {
        continue;
      }
      htBodyKey.put(sKey, "");

      for (int j = i; j < voaItem.length; j++) {
        iVoStatus2 = voaItem[j].getStatus();

        if ((iVoStatus2 == 3) && (!bUseDelRow)) {
          continue;
        }
        sKey2 = voaItem[j].getInvUniqueKey();

        if (!sKey.equals(sKey2))
          continue;
        if ((iBillInOutFlag == 1) && (voaItem[j].getNshouldinnum() != null))
          dItemNum = voaItem[j].getNshouldinnum();
        else if ((iBillInOutFlag == -1) && (voaItem[j].getNshouldoutnum() != null))
          dItemNum = voaItem[j].getNshouldoutnum();
        else {
          dItemNum = this.ZERO;
        }
        if ((iBillInOutFlag == 1) && (voaItem[j].getNneedinassistnum() != null))
          dItemAstNum = voaItem[j].getNneedinassistnum();
        else if ((iBillInOutFlag == -1) && (voaItem[j].getNshouldoutassistnum() != null))
          dItemAstNum = voaItem[j].getNshouldoutassistnum();
        else {
          dItemAstNum = this.ZERO;
        }
        if (iVoStatus2 == 3) {
          dInvSumNum = dInvSumNum.sub(dItemNum);
          dInvSumAstNum = dInvSumAstNum.sub(dItemAstNum);
        }
        else {
          dInvSumNum = dInvSumNum.add(dItemNum);
          dInvSumAstNum = dInvSumAstNum.add(dItemAstNum);
        }

      }

      voTempItem = (GeneralBillItemVO)voaItem[i].clone();

      if (iBillInOutFlag == 1) {
        voTempItem.setNshouldinnum(dInvSumNum);
        voTempItem.setNneedinassistnum(dInvSumAstNum);
      }
      else {
        voTempItem.setNshouldoutnum(dInvSumNum);
        voTempItem.setNshouldoutassistnum(dInvSumAstNum);
      }

      voTempItem.setPrimaryKey(sKey.toString());

      vResultItem.addElement(voTempItem);
    }

    GeneralBillItemVO[] voaRetItem = null;
    if (vResultItem.size() > 0) {
      voaRetItem = new GeneralBillItemVO[vResultItem.size()];
      vResultItem.copyInto(voaRetItem);
    }
    return voaRetItem;
  }

  public String getLockKey()
  {
    return this.sLockKey;
  }

  public void setLockKey(String sLockKey)
  {
    this.sLockKey = sLockKey;
  }

  public UFDate getLoginDate()
  {
    Object oo = getHeaderValue("clogdatenow");
    if ((oo == null) || (oo.toString().trim().length() <= 0))
      oo = getHeaderValue("dbilldate");
    if (oo == null)
      return null;
    if (oo.getClass() == UFDate.class)
      return (UFDate)oo;
    return new UFDate(oo.toString());
  }

  public boolean isCheckCredit()
  {
    return this.isCheckCredit;
  }

  public boolean isCheckPeriod()
  {
    return this.isCheckPeriod;
  }

  public void setIsCheckCredit(boolean bCheckCredit)
  {
    this.isCheckCredit = bCheckCredit;
  }

  public void setIsCheckPeriod(boolean bCheckPeriod)
  {
    this.isCheckPeriod = bCheckPeriod;
  }

  public BatchcodeVO getItemBatchCodeVO(int row) {
    if ((this.m_voaItem != null) && (row >= 0) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null)) {
      return this.m_voaItem[row].getBatchCodeVO();
    }
    return null;
  }

  public void setItemBatchCodeVOs(int row, BatchcodeVO bcvo) {
    if ((this.m_voaItem != null) && (row >= 0) && (row < this.m_voaItem.length) && (this.m_voaItem[row] != null))
      this.m_voaItem[row].setBatchCodeVO(bcvo);
  }

  public boolean isRwtPuUserConfirmFlag()
  {
    return this.isRwtPuUserConfirmFlag;
  }

  public void setIsRwtPuUserConfirmFlag(boolean bConfirmFlag)
  {
    this.isRwtPuUserConfirmFlag = bConfirmFlag;
  }

  public StoreadminBodyVO[] getStoreAdminBodyVOs()
  {
    ArrayList al = new ArrayList();
    if ((this.m_voaItem != null) && (this.m_voaItem.length > 0))
    {
      for (int i = 0; i < this.m_voaItem.length; i++) {
        StoreadminBodyVO vo = new StoreadminBodyVO();
        if (this.m_voHeader != null) {
          vo.setPk_corp(this.m_voHeader.getPk_corp());
          vo.setCcalbodyid(this.m_voHeader.getPk_calbody());
          vo.setCwarehouseid(this.m_voHeader.getCwarehouseid());
        }

        vo.setCinventoryid(this.m_voaItem[i].getCinventoryid());
        al.add(vo);
      }

    }
    else if (this.m_voHeader != null) {
      StoreadminBodyVO vo = new StoreadminBodyVO();
      vo.setPk_corp(this.m_voHeader.getPk_corp());
      vo.setCcalbodyid(this.m_voHeader.getPk_calbody());
      vo.setCwarehouseid(this.m_voHeader.getCwarehouseid());
      al.add(vo);
    }
    StoreadminBodyVO[] vos = null;

    if (al.size() > 0) {
      vos = new StoreadminBodyVO[al.size()];
      al.toArray(vos);
    }

    return vos;
  }

  public Object[] getBodyDefValues(int iserial) {
    if ((this.m_voaItem == null) || (this.m_voaItem.length == 0)) {
      return null;
    }
    String sDefFld = "vuserdef" + String.valueOf(iserial);
    Vector v = new Vector();
    for (int i = 0; i < this.m_voaItem.length; i++) {
      v.add(this.m_voaItem[i].getAttributeValue(sDefFld));
    }
    return v.toArray();
  }

  public String getCbilltypecode() {
    if (this.m_voHeader == null) {
      return null;
    }
    return this.m_voHeader.getCbilltypecode();
  }

  public Object getHeadDefValue(int iserial) {
    if (this.m_voHeader == null) {
      return null;
    }
    String sDefFld = "vuserdef" + String.valueOf(iserial);
    return this.m_voHeader.getAttributeValue(sDefFld);
  }

  public String getCbilltypedef() {
    return "icbill";
  }

  public boolean isGetPlanPriceAtBs() {
    return this.isGetPlanPriceAtBs;
  }

  public void setGetPlanPriceAtBs(boolean isGetPlanPriceAtBs) {
    this.isGetPlanPriceAtBs = isGetPlanPriceAtBs;
  }
}