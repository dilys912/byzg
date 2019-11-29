package nc.vo.ic.pub.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.VOCheckOut;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.freeze.FreezeVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.sn.SerialVO;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.cenpur.service.ChgDocPkVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;

public class VOCheck
{
  static final UFDouble ZERO = new UFDouble(0.0D);

  public static String getBodyErrorMessage(AggregatedValueObject voBill, ArrayList alRowNum, String sErrorHint)
  {
    if ((alRowNum == null) || (sErrorHint == null)) {
      return "";
    }
    if (alRowNum.size() == 0)
      return "";
    if ((voBill == null) || (voBill.getChildrenVO() == null))
      return "";
    StringBuffer sbErrorMesg = new StringBuffer();

    sbErrorMesg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008bill", "UPP4008bill-000320"));

    sbErrorMesg.append("\n" + voBill.getParentVO().getAttributeValue("vbillcode") + "\n");

    sbErrorMesg.append(sErrorHint + NCLangRes4VoTransl.getNCLangRes().getStrByID("scmpub", "UPPscmpub-001109"));

    CircularlyAccessibleValueObject[] voaItem = voBill.getChildrenVO();

    int iRowNow = 0;
    int k = 0;

    for (int i = 0; i < alRowNum.size(); i++) {
      iRowNow = Integer.parseInt(((ArrayList)alRowNum.get(i)).get(0).toString()) - 1;

      sbErrorMesg.append("\n" + voaItem[iRowNow].getAttributeValue("crowno"));

      k++;
    }
    if (k > 0)
    {
      return sbErrorMesg.toString();
    }
    return "";
  }

  private static boolean getNullAsSameDirect(String[] saKey, CircularlyAccessibleValueObject voItem) {
    for (int i = 0; i < saKey.length; i++) {
      if (voItem.getAttributeValue(saKey[i]) == null) return false;
    }
    return true;
  }

  public static boolean isNumDirectionRight(GeneralBillVO voBill)
  {
    boolean bRet = true;
    if ((voBill != null) && (voBill.getItemCount() > 0))
    {
      boolean isReplenish = false;
      isReplenish = voBill.getHeaderVO().getFreplenishflag().booleanValue();

      UFDouble ufd0 = new UFDouble(0);
      GeneralBillItemVO[] voaItem = voBill.getItemVOs();
      int size = voaItem.length;
      int ioutfalg = voBill.getBillInOutFlag();
      String sNumField = "ninnum";
      String sShldNumField = "nshouldinnum";
      if (ioutfalg == -1) {
        sNumField = "noutnum";
        sShldNumField = "nshouldoutnum";
      }
      UFDouble ufdnum = null;
      UFDouble ufdshldnum = null;
      UFDouble ufdprice = null;
      UFDouble ufdhsl = null;
      for (int i = 0; i < size; i++) {
        ufdnum = (UFDouble)voaItem[i].getAttributeValue(sNumField);
        ufdshldnum = (UFDouble)voaItem[i].getAttributeValue(sShldNumField);
        if (isReplenish)
        {
          if (((ufdshldnum != null) && (ufdshldnum.compareTo(ufd0) > 0)) || ((ufdnum != null) && (ufdnum.compareTo(ufd0) > 0))) {
            bRet = false;
            break;
          }
        }

        ufdprice = voaItem[i].getNprice();
        ufdhsl = voaItem[i].getHsl();
        if (((ufdprice != null) && (ufdprice.compareTo(ufd0) < 0)) || ((ufdhsl != null) && (ufdhsl.compareTo(ufd0) < 0))) {
          bRet = false;
          break;
        }
      }
    }
    return bRet;
  }
  public static boolean judgeSameDirectLine(String[] saKey, CircularlyAccessibleValueObject voItem) {
    UFDouble ufdRet = new UFDouble(1);
    for (int i = 0; i < saKey.length; i++) {
      ufdRet = ufdRet.multiply((UFDouble)voItem.getAttributeValue(saKey[i]));
    }
    return ufdRet.doubleValue() >= 0.0D;
  }

  public static void checkSamedirect(AggregatedValueObject vo, String[] sKey, String[] sKeyName)
    throws ValidationException
  {
    CircularlyAccessibleValueObject[] hivo = vo.getChildrenVO();
    ArrayList alRow = null;
    ArrayList al = new ArrayList();
    for (int i = 0; i < hivo.length; i++)
    {
      if ((!getNullAsSameDirect(sKey, hivo[i])) || 
        (judgeSameDirectLine(sKey, hivo[i]))) continue;
      alRow = new ArrayList();
      alRow.add(0, new Integer(i + 1));
      for (int j = 0; j < sKeyName.length; j++) {
        alRow.add(sKeyName[j] + " ");
      }
      al.add(alRow);
    }

    if (al.size() > 0) {
      StringBuffer sbError = new StringBuffer();
      for (int i = 0; i < al.size(); i++) {
        ArrayList alrowNow = (ArrayList)al.get(i);
        sbError.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000216", null, new String[] { alrowNow.get(0).toString() }));
      }
      NullFieldException ice = new NullFieldException();
      ice.setHint(sbError.toString());
      throw ice;
    }
  }

  public static void checkAssistUnitInput(AggregatedValueObject hvo, String sCheckBeforeCol, String strKey)
    throws ValidationException
  {
    ArrayList alBodyCheckStrings = new ArrayList();

    for (int i = 0; i < hvo.getChildrenVO().length; i++)
    {
      if ((null == hvo.getChildrenVO()[i].getAttributeValue("isAstUOMmgt")) || (!hvo.getChildrenVO()[i].getAttributeValue("isAstUOMmgt").toString().trim().equals("1")))
      {
        continue;
      }

      ArrayList alTempAL = new ArrayList();
      alTempAL.add(0, new Integer(i));
      alTempAL.add(1, sCheckBeforeCol);

      alTempAL.add("castunitname");
      alTempAL.add(strKey);

      alBodyCheckStrings.add(alTempAL);
    }

    validateBody(hvo.getChildrenVO(), alBodyCheckStrings);
  }

  public static void checkAssistUnitInputByID(AggregatedValueObject hvo, String sCheckBeforeCol, String strKey)
    throws ValidationException
  {
    ArrayList alBodyCheckStrings = new ArrayList();

    for (int i = 0; i < hvo.getChildrenVO().length; i++)
    {
      if ((null == hvo.getChildrenVO()[i].getAttributeValue("isAstUOMmgt")) || (!hvo.getChildrenVO()[i].getAttributeValue("isAstUOMmgt").toString().trim().equals("1")))
      {
        continue;
      }

      ArrayList alTempAL = new ArrayList();
      alTempAL.add(0, new Integer(i));
      alTempAL.add(1, sCheckBeforeCol);
      alTempAL.add("castunitid");

      alTempAL.add(strKey);

      alBodyCheckStrings.add(alTempAL);
    }

    validateBody(hvo.getChildrenVO(), alBodyCheckStrings);
  }

  public static void checkBusiNeed(AggregatedValueObject hvo, String sCheckBeforeCol, String[] sStrKeys)
    throws ValidationException
  {
    ArrayList alBodyCheckStrings = new ArrayList();

    for (int i = 0; i < hvo.getChildrenVO().length; i++)
    {
      if (((hvo instanceof GeneralBillVO)) || 
        (!(hvo instanceof SpecialBillVO))) {
        continue;
      }
      ArrayList alTempAL = new ArrayList();
      alTempAL.add(0, new Integer(i));
      alTempAL.add(1, sCheckBeforeCol);
      for (int j = 0; j < sStrKeys.length; j++) {
        alTempAL.add(sStrKeys[j]);
      }
      alBodyCheckStrings.add(alTempAL);
    }

    validateBody(hvo.getChildrenVO(), alBodyCheckStrings);
  }

  public static void checkdbizdate(AggregatedValueObject hvo, String sCheckBeforeCol)
    throws ValidationException
  {
    ArrayList alBodyCheckStrings = new ArrayList();

    for (int i = 0; i < hvo.getChildrenVO().length; i++)
    {
      if ((hvo instanceof GeneralBillVO))
      {
        ArrayList alTempAL = new ArrayList();
        alTempAL.add(0, new Integer(i));
        alTempAL.add(1, sCheckBeforeCol);
        alTempAL.add("dbizdate");
        alBodyCheckStrings.add(alTempAL);
      } else {
        if (!(hvo instanceof SpecialBillVO))
        {
          continue;
        }

      }

    }

    validateBody(hvo.getChildrenVO(), alBodyCheckStrings);
  }

  public static void checkFreeItemInput(AggregatedValueObject hvo, String sCheckBeforeCol)
    throws ValidationException
  {
    ArrayList alRowCheckString = null;
    ArrayList alBodyCheckStrings = new ArrayList();

    for (int i = 0; i < hvo.getChildrenVO().length; i++) {
      alRowCheckString = null;
      alRowCheckString = new ArrayList();

      if ((null == hvo.getChildrenVO()[i].getAttributeValue("isFreeItemMgt")) || (!hvo.getChildrenVO()[i].getAttributeValue("isFreeItemMgt").toString().trim().equals("1")))
      {
        continue;
      }

      for (int j = 1; j <= 10; j++) {
        String sVfreenameString = "vfreeid" + Integer.toString(j);
        String sVfreeString = "vfree" + Integer.toString(j);
        if ((null == hvo.getChildrenVO()[i].getAttributeValue(sVfreenameString)) || (hvo.getChildrenVO()[i].getAttributeValue(sVfreenameString).toString().trim().length() == 0))
        {
          continue;
        }

        alRowCheckString.add(sVfreeString);
      }

      if (alRowCheckString.size() > 0) {
        ArrayList alTempAL = new ArrayList();
        alTempAL.add(0, new Integer(i));
        alTempAL.add(1, sCheckBeforeCol);
        for (int j = 0; j < alRowCheckString.size(); j++) {
          alTempAL.add(alRowCheckString.get(j));
        }
        alBodyCheckStrings.add(alTempAL);
      }

    }

    validateBody(hvo.getChildrenVO(), alBodyCheckStrings);
  }

  public static void checkGreaterThanZeroInput(CircularlyAccessibleValueObject[] hivo, String price, String priceExplanation)
    throws ValidationException
  {
    if ((price == null) || (price.trim().length() == 0)) {
      return;
    }
    if ((priceExplanation == null) || (priceExplanation.trim().length() == 0)) {
      return;
    }

    price = price.trim();

    ArrayList al = new ArrayList();
    ArrayList alrow = null;

    boolean bErrorFlag = false;

    for (int i = 0; i < hivo.length; i++) {
      if (hivo[i].getStatus() == 0) {
        continue;
      }
      if (hivo[i].getStatus() == 3) {
        continue;
      }
      if ((hivo[i].getAttributeValue(price) == null) || (hivo[i].getAttributeValue(price).toString().trim().length() == 0))
      {
        continue;
      }

      if (((UFDouble)hivo[i].getAttributeValue(price)).getDouble() < 0.0D) {
        if (!bErrorFlag) {
          alrow = new ArrayList();
          alrow.add(0, new Integer(i + 1));
          bErrorFlag = true;
        }
        alrow.add(price);
      }

      if (bErrorFlag) {
        al.add(alrow);
        bErrorFlag = false;
      }
    }
    if (al.size() > 0) {
      ICPriceException ic = new ICPriceException(al);

      ic.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000156") + priceExplanation.trim() + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000157"));

      throw ic;
    }
  }

  public static void checkInvalidateDateInput(AggregatedValueObject hvo, String sCheckBeforeCol)
    throws ValidationException
  {
    ArrayList alBodyCheckStrings = new ArrayList();

    for (int i = 0; i < hvo.getChildrenVO().length; i++)
    {
      if ((null == hvo.getChildrenVO()[i].getAttributeValue("isValidateMgt")) || (!hvo.getChildrenVO()[i].getAttributeValue("isValidateMgt").toString().trim().equals("1")))
      {
        continue;
      }

      ArrayList alTempAL = new ArrayList();
      alTempAL.add(0, new Integer(i));
      alTempAL.add(1, sCheckBeforeCol);
      alTempAL.add("dvalidate");
      alBodyCheckStrings.add(alTempAL);
    }

    validateBody(hvo.getChildrenVO(), alBodyCheckStrings);

    checkSameInvalidateDateInput(hvo, sCheckBeforeCol);
  }

  public static boolean checkIsDecimal(String sSource)
  {
    if ((sSource == null) || (sSource.trim().length() == 0)) {
      return false;
    }
    int iPos = sSource.indexOf(".");
    if (iPos > 0)
    {
      char cDecimal = '0';
      cDecimal = sSource.charAt(0);
      if (cDecimal == '0') {
        return true;
      }
      for (int i = iPos + 1; i < sSource.length(); i++)
      {
        cDecimal = sSource.charAt(i);
        if (cDecimal > '0') {
          return true;
        }
      }
    }
    return false;
  }

  public static String checkItemBusi(InvVO voInv, GeneralBillItemVO voItem)
    throws BusinessException
  {
    if ((voItem == null) || ((isNull(voItem.getNinnum())) && (isNull(voItem.getNoutnum())) && (isNull(voItem.getNinassistnum())) && (isNull(voItem.getNoutassistnum()))))
    {
      return null;
    }
    String s = NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000202", null, new String[] { voItem.getCrowno() }) + voItem.getCinventorycode();

    StringBuffer sErr = new StringBuffer();

    if ((isNull(voItem.getCinventoryid())) || (isNull(voInv)) || (isNull(voInv.getCinvmanid())))
    {
      sErr.append(s + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000158") + "\n");
      return sErr.toString();
    }

    if ((voItem.getNinnum() != null) && (voItem.getNoutnum() != null)) {
      sErr.append(s + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000159") + "\n");
      return sErr.toString();
    }

    UFDouble num = null;
    if (voItem.getNinnum() != null) {
      num = voItem.getNinnum();
    }
    if (voItem.getNoutnum() != null) {
      num = voItem.getNoutnum();
    }
    if (isNull(num)) {
      sErr.append(s + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000160") + "\n");
      return sErr.toString();
    }

    boolean isInput = false;

    for (int i = 1; i <= 10; i++) {
      if (!isNull(voItem.getAttributeValue("vfree" + String.valueOf(i)))) {
        isInput = true;
        break;
      }
    }

    if ((voInv.getIsFreeItemMgt() != null) && (voInv.getIsFreeItemMgt().intValue() == 1))
    {
      if (!isInput) {
        sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000161") + "\n");
        isInput = false;
      }

    }
    else if (isInput) {
      sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000162") + "\n");
      isInput = false;
    }

    if ((!isNull(voItem.getCastunitid())) && (!isNull(voItem.getHsl())) && ((!isNull(voItem.getNinassistnum())) || (!isNull(voItem.getNoutassistnum()))))
    {
      isInput = true;
    }

    if ((voInv.getIsAstUOMmgt() != null) && (voInv.getIsAstUOMmgt().intValue() == 1)) {
      if (!isInput) {
        sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000163") + "\n");
        isInput = false;
      }

    }
    else if (isInput) {
      sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000164") + "\n");
      isInput = false;
    }

    if (!isNull(voItem.getVbatchcode())) {
      isInput = true;
    }

    if ((voInv.getIsLotMgt() != null) && (voInv.getIsLotMgt().intValue() == 1))
    {
      if (!isInput) {
        sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000165") + "\n");
        isInput = false;
      }

    }
    else if (isInput) {
      sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000166") + "\n");
      isInput = false;
    }

    if (!isNull(voItem.getDvalidate())) {
      isInput = true;
    }

    if ((voInv.getIsValidateMgt() != null) && (voInv.getIsValidateMgt().intValue() == 1))
    {
      if (!isInput) {
        sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000167") + "\n");
        isInput = false;
      }

    }
    else if (isInput) {
      sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000168") + "\n");
      isInput = false;
    }

    if ((voItem.getCcorrespondbid() != null) && (voItem.getCcorrespondcode() != null) && (voItem.getCcorrespondhid() != null) && (voItem.getCcorrespondtype() != null))
    {
      isInput = true;
    }

    if ((voInv.getOuttrackin() != null) && (voInv.getOuttrackin().booleanValue()) && (voItem.getInOutFlag() == -1))
    {
      if (!isInput) {
        sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000169") + "\n");
        isInput = false;
      }

    }
    else if (isInput) {
      sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000170") + "\n");
      isInput = false;
    }

    int isnnum = 0;
    if (voItem.getSerial() != null)
    {
      SerialVO[] voSns = voItem.getSerial();
      for (int i = 0; i < voSns.length; i++) {
        if ((voSns[i] != null) && (voSns[i].getVserialcode() != null))
          isnnum++;
      }
      if ((num.doubleValue() != 0.0D) && (num.compareTo(new UFDouble(isnnum)) == 0)) {
        isInput = true;
      }
    }
    if ((voInv.getIsSerialMgt() != null) && (voInv.getIsSerialMgt().intValue() == 1)) {
      if ((num.doubleValue() == 0.0D) && (voItem.getSerial() == null)) {
        isInput = true;
      }
      if (!isInput) {
        sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000171"));
        isInput = false;
      }

    }
    else if ((voItem.getSerial() != null) && (voItem.getSerial().length > 0)) {
      sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000172"));
      isInput = false;
    }

    if (sErr.length() > 0) {
      return s + sErr.toString();
    }

    return null;
  }

  public static String checkItemTmplt(GeneralBillItemVO voItem, BillTempletVO btvo)
    throws BusinessException
  {
    if ((voItem == null) || (btvo == null)) {
      return null;
    }
    StringBuffer sErr = new StringBuffer();

    ArrayList alHead = getTempletNotNull(btvo, false);

    String sErrh = getNullErr(voItem, alHead);

    if ((sErrh != null) && (sErrh.length() > 0)) {
      sErr.append("\n" + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000173") + sErrh);
    }

    ArrayList alBody = getTempletNotNull(btvo, true);
    String sErrb = getNullErr(voItem, alBody);

    if ((sErrb != null) && (sErrb.length() > 0)) {
      sErr.append("\n" + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000174") + sErrb);
    }

    if (sErr.length() > 0) {
      return sErr.toString();
    }

    return null;
  }

  /** @deprecated */
  public static void checkLocatorInput(AggregatedValueObject hvo, Integer InorOut)
    throws ValidationException
  {
  }

  public static void checkSpaceInput(AggregatedValueObject hvo, Integer InorOut)
    throws ValidationException
  {
    if ((hvo == null) || (hvo.getParentVO() == null) || (hvo.getChildrenVO() == null) || (hvo.getChildrenVO().length == 0) || (!(hvo instanceof GeneralBillVO)))
    {
      return;
    }boolean bIsLocatorMgt = (null != hvo.getParentVO().getAttributeValue("isLocatorMgt")) && (hvo.getParentVO().getAttributeValue("isLocatorMgt").toString().trim().equals("1"));

    if (!bIsLocatorMgt)
      return;
    GeneralBillItemVO[] hivo = (GeneralBillItemVO[])(GeneralBillItemVO[])hvo.getChildrenVO();

    boolean bOutWh = (InorOut != null) && (InorOut.intValue() == -1);

    String sSpaceNumOutField = "noutspacenum";
    String sSpaceAssistNumOutField = "noutspaceassistnum"; String sSpaceGrossNumOutField = "noutgrossnum";
    String sSpaceNumInField = "ninspacenum";
    String sSpaceAssistNumInField = "ninspaceassistnum"; String sSpaceGrossNumInField = "ningrossnum";

    String sNumField = null; String sAssistNumField = null;
    String sSpaceNumField = null; String sSpaceAssistNumField = null;
    String sGrossNumField = null; String sSpaceGrossNumField = null;

    dealAppendNullLocatorVO((GeneralBillVO)hvo);

    ArrayList locatorvo = getLocatorVO(hivo);
    ArrayList al = new ArrayList();
    ArrayList alrow = null;
    int length = hivo.length;
    UFDouble ufdNumbtemp = null; UFDouble ufdNumbtempAss = null; UFDouble ufdNumbtempgrs = null;

    if (null == locatorvo) {
      return;
    }
    if (bOutWh) {
      sNumField = "noutnum";
      sAssistNumField = "noutassistnum";
      sGrossNumField = "noutgrossnum";
    } else {
      sNumField = "ninnum";
      sAssistNumField = "ninassistnum";
      sGrossNumField = "ningrossnum";
    }
    UFDouble numbtempcheck = ZERO; UFDouble numbtempAsscheck = ZERO; UFDouble numbtempGrscheck = ZERO;
    LocatorVO[] voaLineLocTemp = null;
    LocatorVO voLocTemp = null;
    UFDouble ufdSpacenum = null; UFDouble ufdSpaceastnum = null; UFDouble ufdSpacegrsnum = null;

    for (int i = 0; i < length; i++) {
      if (hivo[i].getStatus() == 3)
      {
        continue;
      }
      numbtempcheck = ZERO;
      numbtempAsscheck = ZERO;
      numbtempGrscheck = ZERO;

      ufdNumbtemp = (UFDouble)hivo[i].getAttributeValue(sNumField);

      if (bOutWh) {
        sSpaceNumField = sSpaceNumOutField;
        sSpaceAssistNumField = sSpaceAssistNumOutField;
        sSpaceGrossNumField = sSpaceGrossNumOutField;
      } else {
        sSpaceNumField = sSpaceNumInField;
        sSpaceAssistNumField = sSpaceAssistNumInField;
        sSpaceGrossNumField = sSpaceGrossNumInField;
      }

      ufdNumbtemp = ufdNumbtemp != null ? ufdNumbtemp : ZERO;

      ufdNumbtempAss = (UFDouble)(UFDouble)hivo[i].getAttributeValue(sAssistNumField);
      ufdNumbtempAss = ufdNumbtempAss != null ? ufdNumbtempAss : ZERO;

      ufdNumbtempgrs = (UFDouble)(UFDouble)hivo[i].getAttributeValue(sGrossNumField);
      ufdNumbtempgrs = ufdNumbtempgrs != null ? ufdNumbtempgrs : ZERO;

      if ((null == locatorvo.get(i)) || (((LocatorVO[])(LocatorVO[])locatorvo.get(i)).length == 0))
      {
        if ((ufdNumbtemp.doubleValue() != 0.0D) || (ufdNumbtempAss.doubleValue() != 0.0D) || (ufdNumbtempgrs.doubleValue() != 0.0D)) {
          alrow = new ArrayList();
          alrow.add(0, new Integer(i + 1));
          alrow.add(sNumField);
          al.add(alrow);
        }
      } else {
        voaLineLocTemp = (LocatorVO[])(LocatorVO[])locatorvo.get(i);
        int iLocCount = voaLineLocTemp.length;
        for (int loc = 0; loc < iLocCount; loc++) {
          voLocTemp = voaLineLocTemp[loc];
          ufdSpacenum = (UFDouble)voLocTemp.getAttributeValue(sSpaceNumField);
          if (ufdSpacenum != null)
          {
            numbtempcheck = ufdSpacenum.add(numbtempcheck);
          }
          ufdSpaceastnum = (UFDouble)voLocTemp.getAttributeValue(sSpaceAssistNumField);
          if (ufdSpaceastnum != null) {
            numbtempAsscheck = ufdSpaceastnum.add(numbtempAsscheck);
          }
          ufdSpacegrsnum = (UFDouble)voLocTemp.getAttributeValue(sSpaceGrossNumField);
          if (ufdSpacegrsnum != null) {
            numbtempGrscheck = ufdSpacegrsnum.add(numbtempGrscheck);
          }
        }
        boolean hasAdded = false;
        alrow = new ArrayList();
        if (!numbtempcheck.equals(ufdNumbtemp)) {
          if (!hasAdded) {
            alrow.add(0, new Integer(i + 1));
            hasAdded = true;
          }
          alrow.add(sNumField);
        }
        if (!numbtempAsscheck.equals(ufdNumbtempAss)) {
          if (!hasAdded) {
            alrow.add(0, new Integer(i + 1));
            hasAdded = true;
          }
          alrow.add(sAssistNumField);
        }
        if (!numbtempGrscheck.equals(ufdNumbtempgrs)) {
          if (!hasAdded) {
            alrow.add(0, new Integer(i + 1));
            hasAdded = true;
          }
          alrow.add(sGrossNumField);
        }
        if (hasAdded) {
          al.add(alrow);
        }
      }
    }

    if (al.size() > 0)
    {
      StringBuffer smsg = new StringBuffer(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000212"));
      smsg.append("\n");
      for (int i = 0; i < al.size(); i++) {
        if (i > 0)
          smsg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000100"));
        smsg.append(((ArrayList)al.get(i)).get(0).toString());
      }
      throw new ValidationException(smsg.toString());
    }
  }

  public static void checkLotInput(AggregatedValueObject hvo, String sCheckBeforeCol)
    throws ValidationException
  {
    ArrayList alBodyCheckStrings = new ArrayList();

    for (int i = 0; i < hvo.getChildrenVO().length; i++)
    {
      if ((null == hvo.getChildrenVO()[i].getAttributeValue("isLotMgt")) || (!hvo.getChildrenVO()[i].getAttributeValue("isLotMgt").toString().trim().equals("1")))
      {
        continue;
      }

      ArrayList alTempAL = new ArrayList();
      alTempAL.add(0, new Integer(i));
      alTempAL.add(1, sCheckBeforeCol);
      alTempAL.add("vbatchcode");
      alBodyCheckStrings.add(alTempAL);
    }

    validateBody(hvo.getChildrenVO(), alBodyCheckStrings);
  }

  protected static String checkNotNullItem(CircularlyAccessibleValueObject voItem, ArrayList alItem)
  {
    if ((voItem == null) || (alItem == null) || (alItem.size() == 0)) {
      return null;
    }
    StringBuffer sErr = new StringBuffer();
    String[] ss = null;
    String key = null;
    String name = null;
    if (alItem != null) {
      for (int i = 0; i < alItem.size(); i++) {
        ss = (String[])(String[])alItem.get(i);

        if ((ss == null) || (ss.length < 2))
          continue;
        key = ss[0];
        name = ss[1];

        if (isNull(voItem.getAttributeValue(key))) {
          sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000100") + name);
        }

      }

    }

    if (sErr.length() > 0) {
      return sErr.toString().substring(1) + "\n";
    }

    return null;
  }

  public static void checkNotZeroInput(CircularlyAccessibleValueObject[] hivo, String price, String priceExplanation)
    throws ValidationException
  {
    if ((price == null) || (price.trim().length() == 0)) {
      return;
    }
    if ((priceExplanation == null) || (priceExplanation.trim().length() == 0)) {
      return;
    }

    price = price.trim();

    ArrayList al = new ArrayList();
    ArrayList alrow = null;

    boolean bErrorFlag = false;

    for (int i = 0; i < hivo.length; i++) {
      if (hivo[i].getStatus() == 0) {
        continue;
      }
      if (hivo[i].getStatus() == 3) {
        continue;
      }
      if ((hivo[i].getAttributeValue(price) == null) || (hivo[i].getAttributeValue(price).toString().trim().length() == 0))
      {
        continue;
      }

      if (((UFDouble)hivo[i].getAttributeValue(price)).doubleValue() == 0.0D) {
        if (!bErrorFlag) {
          alrow = new ArrayList();
          alrow.add(0, new Integer(i + 1));
          bErrorFlag = true;
        }
        alrow.add(price);
      }

      if (bErrorFlag) {
        al.add(alrow);
        bErrorFlag = false;
      }
    }
    if (al.size() > 0) {
      ICPriceException ic = new ICPriceException(al);

      ic.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000156") + priceExplanation.trim() + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000176"));

      throw ic;
    }
  }

  public static void checkNullVO(AggregatedValueObject hvo)
    throws ValidationException
  {
    if ((null == hvo) || (null == hvo.getChildrenVO()) || (null == hvo.getParentVO()))
    {
      NullFieldException ice = new NullFieldException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000177"));

      ice.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000178"));
      throw ice;
    }
    if (hvo.getChildrenVO().length < 1) {
      NullFieldException ice = new NullFieldException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000177"));

      ice.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000178"));
      throw ice;
    }
    for (int i = 0; i < hvo.getChildrenVO().length; i++) {
      if (null == hvo.getChildrenVO()[i]) {
        NullFieldException ice = new NullFieldException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000177"));
        ice.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000179"));
        throw ice;
      }
    }
    String[] sHeaderNumberFieldCode = { "nfixdisassemblymny" };
    String[] sBodyNumberFieldCode = { "nshouldoutnum", "nshouldoutassistnum", "noutnum", "noutassistnum", "nshouldinnum", "nneedinassistnum", "ninnum", "ninassistnum", "ntranoutnum", "nprice", "nmny", "nplannedprice", "nplannedmny", "ntranoutastnum", "dshldtransnum", "nshldtransastnum", "nprice", "naccountnum", "naccountastnum", "nchecknum", "ncheckastnum", "nadjustnum", "nadjustastnum" };

    checkNumberFormat(hvo, sHeaderNumberFieldCode, sBodyNumberFieldCode);
  }

  public static void checkNumberFormat(AggregatedValueObject hvo, String[] sHeaderNumberFieldCode, String[] sBodyNumberFieldCode)
    throws ValidationException
  {
    for (int i = 0; i < sHeaderNumberFieldCode.length; i++) {
      Object objValue = hvo.getParentVO().getAttributeValue(sHeaderNumberFieldCode[i].trim());

      if ((objValue == null) || (objValue.toString().trim().length() == 0) || (!(objValue instanceof UFDouble)) || 
        (Math.abs(((UFDouble)objValue).doubleValue()) <= 1000000000000.0D)) continue;
      NullFieldException ice = new NullFieldException();
      ice.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000180"));
      throw ice;
    }

    for (int i = 0; i < hvo.getChildrenVO().length; i++) {
      CircularlyAccessibleValueObject bvo = hvo.getChildrenVO()[i];
      for (int j = 0; j < sBodyNumberFieldCode.length; j++) {
        Object objValue = bvo.getAttributeValue(sBodyNumberFieldCode[j].trim());
        if ((objValue == null) || (objValue.toString().trim().length() == 0) || (!(objValue instanceof UFDouble)) || 
          (Math.abs(((UFDouble)objValue).doubleValue()) <= 1000000000000.0D)) continue;
        NullFieldException ice = new NullFieldException();
        ice.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000181"));
        throw ice;
      }
    }
  }

  public static void checkNumInput(CircularlyAccessibleValueObject[] hivo, String sInputNumString)
    throws ValidationException
  {
    if ((sInputNumString == null) || (sInputNumString.trim().length() == 0)) {
      return;
    }

    sInputNumString = sInputNumString.trim();

    ArrayList al = new ArrayList();
    ArrayList alrow = null;

    int iTotalNumber = 0;

    boolean bErrorFlag = false;

    for (int i = 0; i < hivo.length; i++) {
      if (hivo[i].getStatus() == 3) {
        continue;
      }
      iTotalNumber++;

      if ((hivo[i].getAttributeValue(sInputNumString) == null) || (hivo[i].getAttributeValue(sInputNumString).toString().trim().length() == 0))
      {
        if (!bErrorFlag) {
          alrow = new ArrayList();
          alrow.add(0, new Integer(i + 1));
          bErrorFlag = true;
        }
        alrow.add(sInputNumString);
      }

      if (bErrorFlag) {
        al.add(alrow);
        bErrorFlag = false;
      }
    }
    if ((al.size() > 0) && (al.size() != iTotalNumber)) {
      ICNumException ic = new ICNumException(al);

      ic.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000182"));
      throw ic;
    }
  }

  public static void checkProjectVsDept(AggregatedValueObject avo, String sNumberField)
    throws ValidationException
  {
    if ((avo == null) || (avo.getChildrenVO() == null) || (avo.getChildrenVO().length == 0))
    {
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000183"));
    }int itemnum = avo.getChildrenVO().length;
    boolean hasProject = false;
    for (int i = 0; i < itemnum; i++) {
      Object o = avo.getChildrenVO()[i].getAttributeValue("cprojectid");
      UFDouble udNumber = avo.getChildrenVO()[i].getAttributeValue(sNumberField) == null ? null : (UFDouble)avo.getChildrenVO()[i].getAttributeValue(sNumberField);

      if ((o == null) || (o.toString().trim().length() <= 0) || (udNumber == null) || (udNumber.doubleValue() == 0.0D))
      {
        continue;
      }
      hasProject = true;
      break;
    }

    if (hasProject) {
      Object oTemp = avo.getParentVO().getAttributeValue("cdeptid");
      if ((oTemp == null) || (oTemp.toString().trim().length() == 0))
        throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000184"));
    }
  }

  public static void checkSameDirection(AggregatedValueObject vo, String[] sKey, String[] sKeyName)
    throws ValidationException
  {
    Boolean bGreaterThanZero = null;
    ArrayList al = new ArrayList();
    ArrayList alrow = null;

    CircularlyAccessibleValueObject[] hivo = vo.getChildrenVO();
    for (int j = 0; j < hivo.length; j++) {
      bGreaterThanZero = null;
      for (int i = 0; i < sKey.length; i++) {
        if (null != hivo[j].getAttributeValue(sKey[i].trim())) {
          if (null == bGreaterThanZero) {
            bGreaterThanZero = new Boolean(((UFDouble)(hivo[j].getAttributeValue(sKey[i].trim()) == null ? ZERO : hivo[j].getAttributeValue(sKey[i].trim()))).doubleValue() >= 0.0D);
          }
          else
          {
            if (bGreaterThanZero.booleanValue() == ((UFDouble)(hivo[j].getAttributeValue(sKey[i].trim()) == null ? ZERO : hivo[j].getAttributeValue(sKey[i].trim()))).doubleValue() >= 0.0D)
            {
              continue;
            }

            alrow = new ArrayList();
            alrow.add(0, new Integer(j + 1));
            alrow.add(1, sKeyName[i]);
            al.add(alrow);
            break;
          }
        }
      }

    }

    if (al.size() > 0) {
      StringBuffer sbError = new StringBuffer();
      for (int i = 0; i < al.size(); i++) {
        ArrayList alrowNow = (ArrayList)al.get(i);
        sbError.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000203", null, new String[] { alrowNow.get(0).toString() })).append(alrowNow.get(1).toString());
      }

      NullFieldException ice = new NullFieldException();
      ice.setHint(sbError.toString());
      throw ice;
    }
  }

  public static void checkSameInvalidateDateInput(AggregatedValueObject hvo, String sCheckBeforeCol)
    throws ValidationException
  {
    ArrayList alRowCheckString = null;
    ArrayList al = new ArrayList();

    Hashtable htInvalidateDate = new Hashtable();

    for (int i = 0; i < hvo.getChildrenVO().length; i++) {
      alRowCheckString = null;
      alRowCheckString = new ArrayList();
      if ((hvo.getChildrenVO()[i].getAttributeValue(sCheckBeforeCol) == null) || (hvo.getChildrenVO()[i].getAttributeValue(sCheckBeforeCol).toString().trim().length() == 0))
      {
        continue;
      }

      if ((hvo instanceof GeneralBillVO))
      {
        GeneralBillVO gbvoSpecialBillVO = (GeneralBillVO)hvo;
        if ((null != gbvoSpecialBillVO.getItemValue(i, "isValidateMgt")) && (gbvoSpecialBillVO.getItemValue(i, "isValidateMgt").toString().trim().equals("1")))
        {
          String sKey = ""; String sValue = "";
          sKey = gbvoSpecialBillVO.getItemValue(i, "cinventoryid").toString().trim() + "_" + (gbvoSpecialBillVO.getItemValue(i, "vbatchcode") == null ? "" : gbvoSpecialBillVO.getItemValue(i, "vbatchcode").toString().trim());

          sValue = gbvoSpecialBillVO.getItemValue(i, "dvalidate") == null ? "" : gbvoSpecialBillVO.getItemValue(i, "dvalidate").toString().trim();

          if (htInvalidateDate.containsKey(sKey))
          {
            if (!htInvalidateDate.get(sKey).toString().trim().equals(sValue))
            {
              alRowCheckString.add(Integer.toString(i + 1));
              al.add(alRowCheckString);
            }
          }
          else htInvalidateDate.put(sKey, sValue); 
        }
      }
      else {
        if (!(hvo instanceof SpecialBillVO))
          continue;
        SpecialBillVO sbvoSpecialBillVO = (SpecialBillVO)hvo;
        if ((null == sbvoSpecialBillVO.getItemValue(i, "isValidateMgt")) || (!sbvoSpecialBillVO.getItemValue(i, "isValidateMgt").toString().trim().equals("1")))
        {
          continue;
        }

        String sKey = ""; String sValue = "";
        sKey = sbvoSpecialBillVO.getItemValue(i, "cinventoryid").toString().trim() + "_" + (sbvoSpecialBillVO.getItemValue(i, "vbatchcode") == null ? "" : sbvoSpecialBillVO.getItemValue(i, "vbatchcode").toString().trim());

        sValue = sbvoSpecialBillVO.getItemValue(i, "dvalidate") == null ? "" : sbvoSpecialBillVO.getItemValue(i, "dvalidate").toString().trim();

        if (htInvalidateDate.containsKey(sKey))
        {
          if (htInvalidateDate.get(sKey).toString().trim().equals(sValue))
            continue;
          alRowCheckString.add(Integer.toString(i + 1));
          al.add(alRowCheckString);
        }
        else {
          htInvalidateDate.put(sKey, sValue);
        }
      }
    }

    if (al.size() > 0) {
      StringBuffer sbError = new StringBuffer();
      for (int i = 0; i < al.size(); i++) {
        ArrayList alrowNow = (ArrayList)al.get(i);
        sbError.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000204", null, new String[] { alrowNow.get(0).toString() }));
      }

      NullFieldException ice = new NullFieldException(sbError.toString());
      ice.setHint(sbError.toString());
      throw ice;
    }
  }

  public static void checkSNInput(CircularlyAccessibleValueObject[] hivo, String numb)
    throws ValidationException
  {
    if (!(hivo instanceof GeneralBillItemVO[]))
      return;
    GeneralBillItemVO[] gbivoGeneralBillItemVO = (GeneralBillItemVO[])(GeneralBillItemVO[])hivo;

    ArrayList snvo = getSNVO(hivo);
    ArrayList al = new ArrayList();
    ArrayList alSN = new ArrayList();
    ArrayList alrow = null;
    int length = hivo.length;
    UFDouble numbtemp = null;
    Hashtable hInvnameSN = new Hashtable();

    if (null == snvo) {
      return;
    }
    for (int i = 0; i < length; i++)
    {
      if (hivo[i].getStatus() == 3) {
        continue;
      }
      if ((null == gbivoGeneralBillItemVO[i].getAttributeValue("isSerialMgt")) || (!gbivoGeneralBillItemVO[i].getAttributeValue("isSerialMgt").toString().trim().equals("1")))
      {
        continue;
      }

      int numbtempCheck = 0;
      UFDouble valuenow = hivo[i].getAttributeValue(numb) == null ? ZERO : (UFDouble)hivo[i].getAttributeValue(numb);

      numbtemp = valuenow.abs();

      if (numbtemp.doubleValue() == 0.0D) {
        continue;
      }
      if ((null == snvo.get(i)) || (((SerialVO[])(SerialVO[])snvo.get(i)).length == 0))
      {
        alrow = new ArrayList();
        alrow.add(0, new Integer(i + 1));
        alrow.add(1, numb);
        al.add(alrow);
      }
      else {
        int l = ((SerialVO[])(SerialVO[])snvo.get(i)).length;
        boolean bSameSNFound = false;
        alrow = new ArrayList();
        String sInvID = null;
        String sInvname = null;
        String sKey = null;
        for (int j = 0; j < l; j++) {
          if ((null == ((SerialVO[])(SerialVO[])snvo.get(i))[j].getVserialcode()) || (((SerialVO[])(SerialVO[])snvo.get(i))[j].getVserialcode().trim().length() == 0))
            continue;
          numbtempCheck++;

          sInvID = gbivoGeneralBillItemVO[i].getCinventoryid().trim();
          sInvname = gbivoGeneralBillItemVO[i].getInvname();
          sKey = sInvID + ((SerialVO[])(SerialVO[])snvo.get(i))[j].getVserialcode().trim();

          if (hInvnameSN.containsKey(sKey)) {
            if (!bSameSNFound) {
              alrow = new ArrayList();
              alrow.add(0, new Integer(i + 1));
              alrow.add(1, sInvname);
              alrow.add(2, ((SerialVO[])(SerialVO[])snvo.get(i))[j].getVserialcode().trim());
              bSameSNFound = true;
            } else {
              alrow.add(((SerialVO[])(SerialVO[])snvo.get(i))[j].getVserialcode().trim());
            }
          }
          else hInvnameSN.put(sKey, sKey);

        }

        if (bSameSNFound) {
          alSN.add(alrow);
        }

        Double ddd = new Double(numbtempCheck);
        if (numbtemp.doubleValue() != ddd.doubleValue()) {
          alrow = new ArrayList();
          alrow.add(0, new Integer(i + 1));
          alrow.add(1, numb);
          al.add(alrow);
        }
      }

    }

    if (al.size() > 0) {
      ICSNException ice = new ICSNException(al);

      ice.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000185"));
      throw ice;
    }
    if (alSN.size() > 0) {
      StringBuffer sbError = new StringBuffer();
      for (int i = 0; i < alSN.size(); i++) {
        ArrayList alrowNow = (ArrayList)alSN.get(i);
        sbError.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000202", null, new String[] { alrowNow.get(0).toString() }));

        sbError.append(alrowNow.get(1).toString() + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000186"));

        for (int j = 2; j < alrowNow.size(); j++) {
          sbError.append("[" + alrowNow.get(j).toString() + "]");
        }

      }

      NullFieldException ice = new NullFieldException();
      ice.setHint(sbError.toString());
      throw ice;
    }
  }

  public static void checkSrcUnique(AggregatedValueObject[] voSrcs, ArrayList alKey)
    throws BusinessException
  {
    if ((alKey == null) || (alKey.size() == 0) || (voSrcs == null) || (voSrcs.length == 0)) {
      return;
    }
    if (alKey.size() != 4) {
      throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000187"));
    }

    String[] sHeadkey = null;
    String[] sHeadname = null;
    String[] sBodykey = null;
    String[] sBodyname = null;

    VOCheckOut vocheck = new VOCheckOut();

    if (alKey.get(0) != null) {
      sHeadkey = (String[])(String[])alKey.get(0);
      sHeadname = (String[])(String[])alKey.get(1);
      for (int i = 0; i < sHeadkey.length; i++) {
        vocheck.addHeadSamenessItem(sHeadkey[i], sHeadname[i]);
      }

    }

    if (alKey.get(2) != null) {
      sBodykey = (String[])(String[])alKey.get(2);
      sBodyname = (String[])(String[])alKey.get(3);
      for (int i = 0; i < sBodykey.length; i++) {
        vocheck.addBodySamenessItem(sBodykey[i], sBodyname[i]);
      }

    }

    vocheck.validate(voSrcs);
  }

  public static void checkStartDate(AggregatedValueObject vo, String sCheckDateCol, String sCheckDateName, String sStartDate)
    throws ValidationException
  {
    if ((vo == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0)) {
      return;
    }
    UFDate dstart = new UFDate(sStartDate);
    dstart = dstart.getDateBefore(1);
    CircularlyAccessibleValueObject[] voItems = (CircularlyAccessibleValueObject[])vo.getChildrenVO();
    StringBuffer msg = null;
    for (int i = 0; i < voItems.length; i++) {
      UFDate dbizdate = (UFDate)voItems[i].getAttributeValue(sCheckDateCol);

      if ((dbizdate == null) || 
        (!dbizdate.after(dstart))) continue;
      if (msg == null) {
        msg = new StringBuffer(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000188") + sStartDate + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000189") + sCheckDateName + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000190") + "\n");
      }

      msg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000206", null, new String[] { "" + (i + 1) }));
    }

    if (msg != null) {
      NullFieldException ice = new NullFieldException();
      ice.setHint(msg.toString());
      throw ice;
    }
  }

  public static void checkStartDateAfter(AggregatedValueObject vo, String sCheckDateCol, String sCheckDateName, String sStartDate)
    throws ValidationException
  {
    if ((vo == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length == 0)) {
      return;
    }
    UFDate dstart = new UFDate(sStartDate);

    CircularlyAccessibleValueObject[] voItems = (CircularlyAccessibleValueObject[])vo.getChildrenVO();
    StringBuffer msg = null;
    for (int i = 0; i < voItems.length; i++) {
      UFDate dbizdate = (UFDate)voItems[i].getAttributeValue(sCheckDateCol);

      if ((dbizdate == null) || 
        (!dbizdate.before(dstart))) continue;
      if (msg == null) {
        msg = new StringBuffer(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000188") + sStartDate + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000189") + sCheckDateName + NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000191") + "\n");
      }

      msg.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000206", null, new String[] { "" + (i + 1) }));
    }

    if (msg != null) {
      NullFieldException ice = new NullFieldException();
      ice.setHint(msg.toString());
      throw ice;
    }
  }

  public static void checkVMIWh(IQueryInfo iQuery, GeneralBillVO voBill)
    throws BusinessException
  {
    if (voBill != null)
    {
      boolean isDirect = false;
      boolean isforeignstor = false;

      boolean iscalculatedinvcost = false;
      String thistype = voBill.getBillTypeCode();
      if (voBill.getWh() != null) {
        if ((voBill.getWh().getIsgathersettle() != null) && (voBill.getWh().getIsgathersettle().booleanValue())) {
          isforeignstor = true;
        }

        if ((voBill.getWh().getIscalculatedinvcost() != null) && (voBill.getWh().getIscalculatedinvcost().booleanValue()))
          iscalculatedinvcost = true;
        if ((voBill.getWh().getIsdirectstore() != null) && (voBill.getWh().getIsdirectstore().booleanValue())) {
          isDirect = true;
        }
      }

      if (isDirect) {
        boolean isCanSave = false;

        if ((thistype != null) && ((thistype.equals("4E")) || (thistype.equals("4C")))) {
          String csourcetype = (String)voBill.getItemValue(0, "csourcetype");
          if ((csourcetype != null) && (csourcetype.equals("4Y")) && (thistype.equals("4C")))
            isCanSave = true;
          if ((thistype.equals("4E")) && (voBill.getHeaderVO().getFallocflag() != null) && (voBill.getHeaderVO().getFallocflag().intValue() == 0)) {
            isCanSave = true;
          }
        }
        if (!isCanSave) {
          throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000192"));
        }

      }

      if (voBill.getBillInOutFlag() != 1) {
        return;
      }

      String sBizType = voBill.getHeaderVO().getCbiztypeid();
      String verifyrule = null;
      if (sBizType != null) {
        if (iQuery != null) {
          verifyrule = iQuery.queryBusiTypeVerify(sBizType);
        }

        if ((verifyrule != null) && (verifyrule.equals("V")))
        {
          if ((!isforeignstor) || (!iscalculatedinvcost))
            throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000193"));
        }
        else if ((verifyrule != null) && (!verifyrule.equals("V")) && 
          (isforeignstor))
          throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000194"));
      }
    }
  }

  public void dealToHead(IQueryInfo iQuery, AggregatedValueObject[] preVo, GeneralBillVO[] nowVo)
    throws BusinessException
  {
    ArrayList alFirsthid = new ArrayList();
    Hashtable htFirst = new Hashtable();
    Hashtable htIC = new Hashtable();
    Vector vIC = null;

    String sFirsthid = null;
    GeneralBillVO vo = null;
    String cbilltypecode = null;
    HashMap htPrice = new HashMap();
    HashMap htFirstbid = new HashMap();

    if ((nowVo != null) && (nowVo.length > 0))
    {
      if (nowVo[0].getHeaderVO().getCbilltypecode().equals("4E")) {
        BillVO[] billvos = new BillVO[preVo.length];
        ArrayList al = new ArrayList();
        for (int k = 0; k < preVo.length; k++) {
          billvos[k] = ((BillVO)((BillVO)preVo[k]).clone());

          al.add(billvos[k]);
        }

        if (al.size() > 0) {
          billvos = new BillVO[al.size()];
          al.toArray(billvos);
          htPrice = iQuery.getHtBcurrPrice(billvos);
        }

      }

      GeneralBillItemVO[] voItems = null;
      String sRow = null;

      String pk_corp = null;
      String sIndispatchID = null;
      String sOutdispatchID = null;

      for (int j = 0; j < nowVo.length; j++)
      {
        vo = nowVo[j];
        cbilltypecode = vo.getHeaderVO().getCbilltypecode();

        if (j == 0)
        {
          pk_corp = vo.getHeaderVO().getPk_corp();
          ArrayList alValue = iQuery.getDispatcherID(pk_corp);

          sOutdispatchID = (String)alValue.get(0);
          sIndispatchID = (String)alValue.get(1);
        }
        if ("4E".equals(cbilltypecode))
          vo.getHeaderVO().setCdispatcherid(sIndispatchID);
        else if ("4Y".equals(cbilltypecode)) {
          vo.getHeaderVO().setCdispatcherid(sOutdispatchID);
        }
        pk_corp = vo.getHeaderVO().getPk_corp();
        if ((vo.getHeaderValue("fallocflag") != null) && (vo.getHeaderValue("coutcorpid") != null) && (vo.getHeaderValue("coutcalbodyid") != null))
        {
          if ((nowVo[j] == null) || (nowVo[j].getChildrenVO() == null))
            continue;
          voItems = (GeneralBillItemVO[])(GeneralBillItemVO[])nowVo[j].getChildrenVO();
          for (int i = 0; i < voItems.length; i++)
          {
            voItems[i].synLocator();

            if ("5C".equals(voItems[i].getCfirsttype())) {
              voItems[i].setCprojectid(null);
              voItems[i].setCprojectphaseid(null);

              ChgDocPkVO voVendor = new ChgDocPkVO();
              voVendor.setDstCorpId(nowVo[j].getHeaderVO().getPk_corp());
              voVendor.setSrcManId(voItems[i].getCvendorid());
              voVendor.setSrcCorpId((String)nowVo[j].getHeaderVO().getAttributeValue("coutcorpid"));
              ChgDocPkVO[] voDstVendors = ChgDataUtil.chgPkCuByCorp(new ChgDocPkVO[] { voVendor });
              if ((voDstVendors != null) && (voDstVendors.length > 0)) {
                voItems[i].setCvendorid(voDstVendors[0].getDstManId());
              }
            }
            else if (("5D".equals(voItems[i].getCfirsttype())) && 
              ("4E".equals(nowVo[j].getBillTypeCode()))) {
              voItems[i].setCprojectid(null);
              voItems[i].setCprojectphaseid(null);
            }

            if (htPrice.containsKey(voItems[i].getCfirstbillbid())) {
              voItems[i].setNprice((UFDouble)htPrice.get(voItems[i].getCfirstbillbid()));
            }
          }

        }
        else
        {
          sRow = String.valueOf(j);

          if ((nowVo[j] == null) || (nowVo[j].getChildrenVO() == null))
            continue;
          voItems = (GeneralBillItemVO[])(GeneralBillItemVO[])nowVo[j].getChildrenVO();

          for (int i = 0; i < voItems.length; i++)
          {
            voItems[i].synLocator();

            if (("4Y".equals(cbilltypecode)) && (!voItems[i].getCfirsttype().startsWith("5"))) {
              throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000195"));
            }

            if (("4Y".equals(cbilltypecode)) && (voItems[i].getCsourcetype() != null) && (!voItems[i].getCsourcetype().startsWith("5")) && (voItems[i].getCfirsttype() != null) && (voItems[i].getCfirsttype().startsWith("5")))
            {
              if (htIC.containsKey(sRow))
              {
                vIC = (Vector)htIC.get(sRow);
              }
              else
              {
                vIC = new Vector();
                htIC.put(sRow, vIC);
              }

              vIC.add(voItems[i].getCfirstbillhid());

              if (!htFirst.containsKey(voItems[i].getCfirstbillhid()))
              {
                htFirst.put(voItems[i].getCfirstbillhid(), voItems[i].getCfirstbillhid());
                alFirsthid.add(voItems[i].getCfirstbillhid());
              }
              if (!htFirstbid.containsKey(voItems[i].getCfirstbillbid()))
              {
                htFirstbid.put(voItems[i].getCfirstbillbid(), voItems[i].getCfirstbillbid());
              }

            }

            if ("5C".equals(voItems[i].getCfirsttype())) {
              voItems[i].setCprojectid(null);
              voItems[i].setCprojectphaseid(null);

              ChgDocPkVO voVendor = new ChgDocPkVO();
              voVendor.setDstCorpId(nowVo[j].getHeaderVO().getPk_corp());
              voVendor.setSrcManId(voItems[i].getCvendorid());
              voVendor.setSrcCorpId((String)nowVo[j].getHeaderVO().getAttributeValue("coutcorpid"));
              ChgDocPkVO[] voDstVendors = ChgDataUtil.chgPkCuByCorp(new ChgDocPkVO[] { voVendor });
              if ((voDstVendors != null) && (voDstVendors.length > 0))
                voItems[i].setCvendorid(voDstVendors[0].getDstManId());
            }
            else
            {
              if ((!"5D".equals(voItems[i].getCfirsttype())) || 
                (!"4E".equals(nowVo[j].getBillTypeCode()))) continue;
              voItems[i].setCprojectid(null);
              voItems[i].setCprojectphaseid(null);
            }
          }

        }

      }

      if (alFirsthid.size() > 0)
      {
        String[] shids = new String[alFirsthid.size()];
        alFirsthid.toArray(shids);
        ArrayList alOrder = null;

        Hashtable htOrder = new Hashtable();

        BillVO voHead = null;

        if (iQuery != null) {
          alOrder = iQuery.getTranHeadInfo(shids);
        }
        if (alOrder != null)
        {
          for (int i = 0; i < alOrder.size(); i++)
          {
            if (alOrder.get(i) == null) {
              continue;
            }
            sFirsthid = (String)((ArrayList)alOrder.get(i)).get(0);

            voHead = (BillVO)((ArrayList)alOrder.get(i)).get(1);

            if ((sFirsthid != null) && (voHead != null)) {
              htOrder.put(sFirsthid, voHead);
              BillItemVO[] voTobodys = voHead.getItemVOs();
              ArrayList alii = new ArrayList();
              if ((voTobodys != null) && (voTobodys.length > 0)) {
                for (int kk = 0; kk < voTobodys.length; kk++) {
                  if (htFirstbid.containsKey(voTobodys[kk].getCbill_bid())) {
                    alii.add(voTobodys[kk]);
                  }
                }
              }

              if (alii.size() > 0) {
                voTobodys = new BillItemVO[alii.size()];
                alii.toArray(voTobodys);
                voHead.setChildrenVO(voTobodys);
              }
            }

          }

        }

        if (htOrder.size() == alFirsthid.size())
        {
          for (int j = 0; j < nowVo.length; j++)
          {
            if (htIC.containsKey(String.valueOf(j)))
            {
              vIC = (Vector)htIC.get(String.valueOf(j));
              String[] cfirstids = new String[vIC.size()];
              vIC.copyInto(cfirstids);
              vIC = new Vector();
              for (int k = 0; k < cfirstids.length; k++)
              {
                if (htFirst.containsKey(cfirstids[k])) {
                  vIC.add((BillVO)htOrder.get(cfirstids[k]));
                }
              }

              BillVO[] voTos = new BillVO[vIC.size()];
              vIC.copyInto(voTos);
              checkSrcUnique(voTos, getUniKeysTO());
            }

            vo = nowVo[j];

            if ((nowVo[j] == null) || (nowVo[j].getChildrenVO() == null))
              continue;
            sFirsthid = vo.getItemVOs()[0].getCfirstbillhid();

            if ((sFirsthid == null) || (!htOrder.containsKey(sFirsthid)))
              continue;
            voHead = (BillVO)htOrder.get(sFirsthid);
            vo.getHeaderVO().setAttributeValue("fallocflag", voHead.getHeaderVO().getFallocflag());
            vo.getHeaderVO().setAttributeValue("coutcorpid", voHead.getHeaderVO().getCoutcorpid());
            vo.getHeaderVO().setAttributeValue("coutcalbodyid", voHead.getHeaderVO().getCoutcbid());
            vo.getHeaderVO().setAttributeValue("cothercorpid", voHead.getHeaderVO().getCincorpid());
            vo.getHeaderVO().setAttributeValue("cothercalbodyid", voHead.getHeaderVO().getCincbid());
            vo.getHeaderVO().setAttributeValue("cotherwhid", voHead.getItemVOs()[0].getCinwhid());
            vo.getHeaderVO().setCcustomerid((String)voHead.getItemVOs()[0].getAttributeValue("ccustomerid"));
            vo.getHeaderVO().setFreplenishflag(voHead.getHeaderVO().getBretractflag());
          }

        }
        else
        {
          throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000196"));
        }
      }
    }
  }

  public void filterVObyisIA(GeneralBillVO vo)
  {
    SCMEnv.out("$$$$$$$$$$$$$$$$$$$");

    GeneralBillHeaderVO billHeaderVO = (GeneralBillHeaderVO)vo.getParentVO();
    GeneralBillItemVO[] billItemVO = (GeneralBillItemVO[])(GeneralBillItemVO[])vo.getChildrenVO();

    if ((vo == null) || (billHeaderVO == null) || (billItemVO == null) || (billItemVO.length == 0)) {
      return;
    }
    ArrayList alVoItem = new ArrayList();

    if ((billHeaderVO.getIscalculatedinvcost() != null) && (billHeaderVO.getIscalculatedinvcost().booleanValue())) {
      for (int i = 0; i < billItemVO.length; i++)
      {
        if ((billItemVO[i].getIscancalculatedinvcost() != null) && (billItemVO[i].getIscancalculatedinvcost().booleanValue())) {
          alVoItem.add(billItemVO[i]);
        }
      }
    }

    if ((alVoItem != null) && (alVoItem.size() > 0)) {
      GeneralBillItemVO[] billItemVOnew = new GeneralBillItemVO[alVoItem.size()];
      alVoItem.toArray(billItemVOnew);
      vo.setChildrenVO(billItemVOnew);
    }
  }

  protected static ArrayList getLocatorVO(CircularlyAccessibleValueObject[] hivo)
  {
    GeneralBillItemVO[] items = (GeneralBillItemVO[])(GeneralBillItemVO[])hivo;
    ArrayList alLocatorVOnew = new ArrayList();
    for (int i = 0; i < items.length; i++) {
      alLocatorVOnew.add(items[i].getLocator());
    }
    return alLocatorVOnew;
  }

  protected static String getNullErr(CircularlyAccessibleValueObject voItem, ArrayList alItem)
  {
    if ((voItem == null) || (alItem == null) || (alItem.size() == 0)) {
      return null;
    }
    StringBuffer sErr = new StringBuffer();
    String[] ss = null;
    String key = null;
    String name = null;
    if (alItem != null) {
      for (int i = 0; i < alItem.size(); i++) {
        ss = (String[])(String[])alItem.get(i);

        if ((ss == null) || (ss.length < 2))
          continue;
        key = ss[0];
        name = ss[1];

        if (isNull(voItem.getAttributeValue(key))) {
          sErr.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000100") + name);
        }

      }

    }

    if (sErr.length() > 0) {
      return sErr.toString().substring(1) + "\n";
    }

    return null;
  }

  private static String[] getNumInfo(GeneralBillVO voBill, int[] iOperator)
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
    }String crowno = null;
    voItems = voBill.getItemVOs();
    if ((voItems != null) && (voItems.length > 0)) {
      for (int i = 0; i < voItems.length; i++)
      {
        if (voItems[i].getStatus() == 3)
          continue;
        int iResult = voItems[i].compareNum();
        crowno = voItems[i].getCrowno();

        for (int j = 0; j < iOperator.length; j++) {
          if (iResult == iOperator[j]) {
            if (sbMsg[j] == null) {
              if (iResult == 3)
                sbMsg[j] = new StringBuffer(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000018") + "\n");
              else if (iResult == 1)
                sbMsg[j] = new StringBuffer(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000019") + "\n");
            }
            sbMsg[j].append(crowno);
            sbMsg[j].append(" ");
            sbMsg[j].append(voItems[i].getCinventorycode());
            sbMsg[j].append(voItems[i].getInvname());
            sbMsg[j].append("\n");
          }
        }
      }
    }
    for (int i = 0; i < iOperator.length; i++) {
      if ((sbMsg[i] != null) && (sbMsg[i].length() > 0))
        sRet[i] = sbMsg[i].toString();
    }
    return sRet;
  }

  public ArrayList getProjectids(GeneralBillVO[] nowVOs)
  {
    if ((nowVOs == null) || (nowVOs.length == 0)) {
      return null;
    }

    ArrayList alProject = new ArrayList();

    for (int k = 0; k < nowVOs.length; k++) {
      GeneralBillVO voNow = nowVOs[k];
      GeneralBillItemVO[] voItems = voNow.getItemVOs();

      String pk_corp = voNow.getHeaderVO().getPk_corp();

      for (int i = 0; i < voItems.length; i++) {
        String projID = voItems[i].getCprojectid();
        String projPhID = voItems[i].getCprojectphaseid();
        if (((projID == null) || (projID.trim().length() == 0)) && ((projPhID == null) || (projPhID.trim().length() == 0)))
        {
          continue;
        }
        ArrayList alPjTem = new ArrayList();
        alPjTem.add(0, pk_corp);

        alPjTem.add(1, projID);

        alPjTem.add(2, projPhID);

        if (!alPjTem.isEmpty()) {
          alProject.add(alPjTem);
        }
      }

    }

    return alProject;
  }

  protected static ArrayList getSNVO(CircularlyAccessibleValueObject[] hivo)
  {
    GeneralBillItemVO[] items = (GeneralBillItemVO[])(GeneralBillItemVO[])hivo;
    ArrayList alSNVOnew = new ArrayList();
    for (int i = 0; i < items.length; i++) {
      alSNVOnew.add(items[i].getSerial());
    }
    return alSNVOnew;
  }

  private static ArrayList getTempletNotNull(BillTempletVO btvo, boolean isBody)
  {
    if ((btvo == null) || (btvo.getBodyVO() == null) || (btvo.getBodyVO().length == 0)) {
      return null;
    }
    ArrayList albodyCheckString = new ArrayList();

    String sCheckItemKey = null;
    String sCheckItemName = null;
    BillTempletBodyVO[] bodies = btvo.getBodyVO();

    for (int i = 0; i < bodies.length; i++)
    {
      sCheckItemKey = bodies[i].getItemkey();
      sCheckItemName = bodies[i].getDefaultshowname();

      if ((!bodies[i].getNullflag().booleanValue()) || (
        ((!isBody) || (bodies[i].getPos().intValue() != 1)) && ((isBody) || (bodies[i].getPos().intValue() == 1))))
        continue;
      albodyCheckString.add(new String[] { sCheckItemKey, sCheckItemName });
    }

    return albodyCheckString;
  }

  public static ArrayList getUniKeysPO()
  {
    ArrayList al = new ArrayList();
    al.add(new String[] { "cdeptid", "cvendormangid", "cbiztype" });
    al.add(new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004064"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000275"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000003") });
    al.add(new String[] { "pk_arrvcorp" });
    al.add(new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000404") });
    return al;
  }

  public static ArrayList getUniKeysSO()
  {
    ArrayList al = new ArrayList();
    al.add(new String[] { "bfreecustflag", "cdeptid", "ccustomerid", "cbiztype" });
    al.add(new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004032"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004064"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001589"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000003") });
    al.add(new String[] { "pk_corp", "cadvisecalbodyid", "cbodywarehouseid", "creccalbodyid", "crecwareid" });
    al.add(new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000404"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001006"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000994"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002254"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002236") });
    return al;
  }

  public static ArrayList getUniKeysTO()
  {
    ArrayList al = new ArrayList();
    al.add(new String[] { "bretractflag", "fallocflag", "cincorpid", "cincbid", "coutcorpid", "coutcbid", "ctakeoutcorpid", "ctakeoutcbid", "coutcurrtype" });
    al.add(new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000209"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003744"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003685"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003689"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003710"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003722"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000527"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000532"), "币种" });

    al.add(new String[] { "fallocflag", "cincorpid", "cincbid", "cinwhid", "cindeptid", "coutcorpid", "coutcbid", "ctakeoutcorpid", "ctakeoutcbid", "ctakeoutwhid", "ctakeoutdeptid", "pk_sendtype", "ccustomerid" });
    al.add(new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003744"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003685"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003689"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003683"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003700"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003710"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003722"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000527"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000532"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000525"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000539"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001024"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001589") });

    return al;
  }

  private static boolean isNull(Object value)
  {
    return (value == null) || (value.toString().trim().length() <= 0);
  }

  private static void dealAppendNullLocatorVO(GeneralBillVO vo)
  {
    GeneralBillHeaderVO hvo = (GeneralBillHeaderVO)vo.getParentVO();

    String sBillTypeCode = hvo.getCbilltypecode();
    if ((sBillTypeCode == null) || (sBillTypeCode.equals("42")) || (sBillTypeCode.equals("43")) || (sBillTypeCode.equals("4P")) || (sBillTypeCode.equals("4X"))) {
      return;
    }
    GeneralBillItemVO[] bvos = (GeneralBillItemVO[])(GeneralBillItemVO[])vo.getChildrenVO();
    LocatorVO[] voLocs = null;
    if ((hvo == null) || (bvos == null))
      return;
    for (int row = 0; row < bvos.length; row++) {
      voLocs = bvos[row].getLocator();
      if ((voLocs != null) && (voLocs.length != 0))
        continue;
      bvos[row].setCspaceid(GenMethod.STRING_NULL);
      bvos[row].synLocator();

      if ((bvos[row].getLocator() != null) && 
        (bvos[row].getLocator()[0] != null)) {
        bvos[row].getLocator()[0].setCwarehouseid(hvo.getCwarehouseid());
      }
      if (bvos[row].getLocStatus() == 0)
        bvos[row].setLocStatus(1);
    }
  }

  public static UFBoolean isNumMorethenShouldnum(AggregatedValueObject vo)
    throws BusinessException
  {
    UFBoolean ufbRet = new UFBoolean(true);
    GeneralBillVO voBill = (GeneralBillVO)vo;
    StringBuffer sbMsg = null;
    StringBuffer sbErr = null;
    int[] iOperators = { 3, 1 };
    String[] sMsgs = null;
    if (voBill != null) {
      sMsgs = getNumInfo((GeneralBillVO)vo, iOperators);
      if ((sMsgs != null) && (sMsgs.length > 0)) {
        for (int i = 0; i < sMsgs.length; i++) {
          if ((sMsgs[i] != null) && (sMsgs[i].length() > 0)) {
            ufbRet = new UFBoolean(false);
            if (sbMsg == null)
              sbMsg = new StringBuffer();
            sbMsg.append(sMsgs[i]);
          }
        }
      }

      if (sbMsg != null) {
        sbErr = new StringBuffer(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000197") + "\n");
        sbErr.append(sbMsg);
        throw new BusinessException(sbErr.toString());
      }
    }
    return ufbRet;
  }

  public void setDispatchID(String cDispatchid, AggregatedValueObject[] voaBill)
  {
    if ((cDispatchid != null) && (voaBill != null) && (voaBill.length > 0)) {
      int iLen = voaBill.length;
      GeneralBillHeaderVO voHead = null;
      for (int i = 0; i < iLen; i++) {
        voHead = (GeneralBillHeaderVO)(GeneralBillHeaderVO)voaBill[i].getParentVO();
        if ((voHead.getCdispatcherid() == null) || (voHead.getCdispatcherid().length() <= 0))
          voHead.setCdispatcherid(cDispatchid);
      }
    }
  }

  public AggregatedValueObject setFreezeInfo_Old(IQueryInfo iQuery, AggregatedValueObject preVo, AggregatedValueObject nowVo)
    throws BusinessException
  {
    if ((preVo == null) || (nowVo == null) || (iQuery == null)) {
      return nowVo;
    }

    CircularlyAccessibleValueObject[] voaItemSrc = preVo.getChildrenVO();

    CircularlyAccessibleValueObject[] voaItemDest = nowVo.getChildrenVO();
    if ((voaItemSrc == null) || (voaItemDest == null) || (voaItemSrc.length == 0) || (voaItemDest.length == 0) || (voaItemSrc.length != voaItemDest.length))
    {
      SCMEnv.out("特色VO：表体为空，或行数不一致。");
      return nowVo;
    }

    CircularlyAccessibleValueObject[] voItems = voaItemDest;

    if (!(nowVo instanceof GeneralBillVO)) {
      return nowVo;
    }
    nowVo = (GeneralBillVO)nowVo;

    String billtype = ((GeneralBillHeaderVO)nowVo.getParentVO()).getCbilltypecode();

    if ((billtype != null) && ((billtype.equals("4C")) || (billtype.equals("4D")) || (billtype.equals("4Y")) || (BillTypeConst.m_consignMachiningOut.equals(billtype))))
    {
      Hashtable htbid = new Hashtable();
      Hashtable htvo = new Hashtable();
      Vector v = new Vector();
      Vector vNew = new Vector();

      for (int i = 0; i < voItems.length; i++) {
        if (voItems[i].getAttributeValue("cfreezeid") != null) {
          String cfreezeid = (String)voItems[i].getAttributeValue("cfreezeid");
          v.add(cfreezeid);
          String bid = (String)voItems[i].getAttributeValue("csourcebillbid");
          if (bid != null) {
            htvo.put(bid, voItems[i]);
            htbid.put(cfreezeid, bid);
          }
        } else {
          vNew.add(voItems[i]);
        }

      }

      if (v.size() > 0) {
        String[] bids = new String[v.size()];
        v.copyInto(bids);
        try
        {
          FreezeVO[] fvos = null;

          fvos = iQuery.getFreezedVOs(bids);

          if ((fvos != null) && (fvos.length > 0))
          {
            String whid = fvos[0].getCwarehouseid();
            String bid = null;

            LocatorVO[] locs = new LocatorVO[1];

            GeneralBillItemVO voFirst = null;
            for (int i = 0; i < fvos.length; i++) {
              if (!whid.equals(fvos[i].getCwarehouseid()))
                throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000198"));
              if (htbid.containsKey(fvos[i].getCcorrespondbid())) {
                bid = (String)htbid.get(fvos[i].getCcorrespondbid());
              }
              if ((bid != null) && (htvo.containsKey(bid))) {
                voFirst = (GeneralBillItemVO)htvo.get(bid);
                GeneralBillItemVO vonew = (GeneralBillItemVO)voFirst.clone();
                voFirst.setNshouldoutnum(null);
                voFirst.setNshouldoutassistnum(null);

                vonew.setHsl((UFDouble)fvos[i].getAttributeValue("hsl"));
                vonew.setCastunitid(fvos[i].getCastunitid());
                vonew.setVbatchcode(fvos[i].getVbatchcode());
                vonew.setDvalidate(fvos[i].getDvalidate());
                vonew.setVfree1((String)fvos[i].getAttributeValue("vfree1"));
                vonew.setVfree2((String)fvos[i].getAttributeValue("vfree2"));
                vonew.setVfree3((String)fvos[i].getAttributeValue("vfree3"));
                vonew.setVfree4((String)fvos[i].getAttributeValue("vfree4"));
                vonew.setVfree5((String)fvos[i].getAttributeValue("vfree5"));
                vonew.setVfree6((String)fvos[i].getAttributeValue("vfree6"));
                vonew.setVfree7((String)fvos[i].getAttributeValue("vfree7"));
                vonew.setVfree8((String)fvos[i].getAttributeValue("vfree8"));
                vonew.setVfree9((String)fvos[i].getAttributeValue("vfree9"));
                vonew.setVfree10((String)fvos[i].getAttributeValue("vfree10"));
                vonew.setCfreezeid(fvos[i].getCfreezeid());
                locs[0] = new LocatorVO();
                locs[0].setCspaceid(fvos[i].getCspaceid());
                locs[0].setVspacecode(fvos[i].getCspacecode());
                locs[0].setVspacename(fvos[i].getCspacename());
                vonew.setLocator(locs);
                vonew.setDbizdate(new UFDate(System.currentTimeMillis()));

                vNew.add(vonew);
              }

            }

            if (nowVo.getParentVO().getAttributeValue("cwarehouseid") == null) {
              nowVo.getParentVO().setAttributeValue("cwarehouseid", whid);
            }

          }

        }
        catch (Exception e)
        {
          throw new BusinessException("Remote Call", e);
        }
      }

      if (vNew.size() > 0) {
        GeneralBillItemVO[] voItemsNew = new GeneralBillItemVO[vNew.size()];
        vNew.copyInto(voItemsNew);
        nowVo.setChildrenVO(voItemsNew);
      }
    }

    return nowVo;
  }

  public AggregatedValueObject setFreezeInfo(IQueryInfo iQuery, AggregatedValueObject preVo, AggregatedValueObject nowVo)
    throws BusinessException
  {
    if ((preVo == null) || (nowVo == null) || (iQuery == null)) {
      return nowVo;
    }

    CircularlyAccessibleValueObject[] voaItemSrc = preVo.getChildrenVO();

    CircularlyAccessibleValueObject[] voaItemDest = nowVo.getChildrenVO();
    if ((voaItemSrc == null) || (voaItemDest == null) || (voaItemSrc.length == 0) || (voaItemDest.length == 0) || (voaItemSrc.length != voaItemDest.length))
    {
      SCMEnv.out("特色VO：表体为空，或行数不一致。");
      return nowVo;
    }

    GeneralBillItemVO[] voItems = (GeneralBillItemVO[])(GeneralBillItemVO[])voaItemDest;

    if (!(nowVo instanceof GeneralBillVO)) {
      return nowVo;
    }

    String billtype = ((GeneralBillHeaderVO)nowVo.getParentVO()).getCbilltypecode();

    if ((billtype == null) || ((!billtype.equals("4C")) && (!billtype.equals("4D")) && (!billtype.equals("4F")) && (!billtype.equals("4Y"))))
    {
      return nowVo;
    }

    Hashtable htbid = new Hashtable();
    Hashtable htSrc = new Hashtable();
    Hashtable htfvo = new Hashtable();
    Vector v = new Vector();
    Vector vNew = new Vector();
    String cfreezeid = null;
    String csrcbid = null;

    for (int i = 0; i < voItems.length; i++)
    {
      if ((billtype.equals("4Y")) || (billtype.equals("4C"))) {
        cfreezeid = voItems[i].getCfirstbillbid();
      }
      else if ((billtype.equals("4D")) || (billtype.equals("4F"))) {
        cfreezeid = voItems[i].getCsourcebillbid();
      }

      if ((cfreezeid != null) && (!htbid.containsKey(cfreezeid))) {
        v.add(cfreezeid);
        htbid.put(cfreezeid, cfreezeid);
      }
      if (cfreezeid != null) {
        htSrc.put(voItems[i].getAttributeValue("csourcebillbid"), cfreezeid);
      }
    }

    if (v.size() > 0) {
      String[] bids = new String[v.size()];
      v.copyInto(bids);
      try
      {
        FreezeVO[] fvos = null;

        fvos = iQuery.getFreezedVOs(bids);

        if ((fvos != null) && (fvos.length > 0))
        {
          String whid = fvos[0].getCwarehouseid();
          ArrayList alfvo = null;
          for (int i = 0; i < fvos.length; i++)
          {
            if (htfvo.containsKey(fvos[i].getCcorrespondbid())) {
              alfvo = (ArrayList)htfvo.get(fvos[i].getCcorrespondbid());
            } else {
              alfvo = new ArrayList();
              htfvo.put(fvos[i].getCcorrespondbid(), alfvo);
            }
            alfvo.add(fvos[i]);
          }
        }
      }
      catch (Exception e)
      {
        throw new BusinessException("Remote Call", e);
      }
    }

    if (htfvo.size() == 0) {
      return nowVo;
    }
    boolean isHasFreeze = false;
    UFDouble nshouldnum = null;
    UFDouble nshouldastnum = null;
    for (int i = 0; i < voItems.length; i++) {
      isHasFreeze = false;
      csrcbid = voItems[i].getCsourcebillbid();
      nshouldnum = voItems[i].getNshouldoutnum();
      nshouldastnum = voItems[i].getNshouldoutassistnum();
      if (htSrc.containsKey(csrcbid))
      {
        if ((billtype.equals("4Y")) || (billtype.equals("4C")))
          cfreezeid = voItems[i].getCfirstbillbid();
        else if ((billtype.equals("4D")) || (billtype.equals("4F"))) {
          cfreezeid = voItems[i].getCsourcebillbid();
        }

        if (htfvo.containsKey(cfreezeid)) {
          ArrayList alf1 = (ArrayList)htfvo.get(cfreezeid);
          FreezeVO[] fvo1s = new FreezeVO[alf1.size()];
          alf1.toArray(fvo1s);
          UFDouble ntotalnum = new UFDouble(0.0D);
          UFDouble ntotalastnum = new UFDouble(0.0D);

          for (int j = 0; j < fvo1s.length; j++) {
            GeneralBillItemVO vonew = (GeneralBillItemVO)voItems[i].clone();

            if ((fvo1s[j].getNfreezenum() != null) && (j < fvo1s.length - 1)) {
              ntotalnum = ntotalnum.add(fvo1s[j].getNfreezenum());
            }
            if ((fvo1s[j].getNfreezeastnum() != null) && (j < fvo1s.length - 1)) {
              ntotalastnum = ntotalastnum.add(fvo1s[j].getNfreezeastnum());
            }

            if (j == fvo1s.length - 1) {
              if (nshouldnum != null) {
                vonew.setNshouldoutnum(nshouldnum.sub(ntotalnum));
              }

              if (nshouldastnum != null) {
                vonew.setNshouldoutassistnum(nshouldastnum.sub(ntotalastnum));
              }
            }
            else
            {
              vonew.setNshouldoutnum(fvo1s[j].getNfreezenum());
              vonew.setNshouldoutassistnum(fvo1s[j].getNfreezeastnum());
            }

            vonew.setNoutnum(fvo1s[j].getNfreezenum());
            vonew.setNoutassistnum(fvo1s[j].getNfreezeastnum());
            vonew.setHsl((UFDouble)fvo1s[j].getAttributeValue("hsl"));
            vonew.setCastunitid(fvo1s[j].getCastunitid());
            vonew.setVbatchcode(fvo1s[j].getVbatchcode());
            vonew.setDvalidate(fvo1s[j].getDvalidate());
            vonew.setCvendorid(fvo1s[j].getCvendorid());
            vonew.setVfree1((String)fvo1s[j].getAttributeValue("vfree1"));
            vonew.setVfree2((String)fvo1s[j].getAttributeValue("vfree2"));
            vonew.setVfree3((String)fvo1s[j].getAttributeValue("vfree3"));
            vonew.setVfree4((String)fvo1s[j].getAttributeValue("vfree4"));
            vonew.setVfree5((String)fvo1s[j].getAttributeValue("vfree5"));
            vonew.setVfree6((String)fvo1s[j].getAttributeValue("vfree6"));
            vonew.setVfree7((String)fvo1s[j].getAttributeValue("vfree7"));
            vonew.setVfree8((String)fvo1s[j].getAttributeValue("vfree8"));
            vonew.setVfree9((String)fvo1s[j].getAttributeValue("vfree9"));
            vonew.setVfree10((String)fvo1s[j].getAttributeValue("vfree10"));
            vonew.setCfreezeid(cfreezeid);
            vonew.setAttributeValue("cwarehouseid", fvo1s[j].getCwarehouseid());

            if (fvo1s[j].getCspaceid() != null) {
              vonew.setCspaceid(fvo1s[j].getCspaceid());
              LocatorVO[] locs = new LocatorVO[1];
              locs[0] = new LocatorVO();
              locs[0].setCspaceid(fvo1s[j].getCspaceid());
              locs[0].setVspacecode(fvo1s[j].getCspacecode());
              locs[0].setVspacename(fvo1s[j].getCspacename());
              locs[0].setNoutspacenum(vonew.getNoutnum());
              locs[0].setNoutspaceassistnum(vonew.getNoutassistnum());
              locs[0].setNoutgrossnum(vonew.getNoutgrossnum());
              vonew.setLocator(locs);
            }

            vonew.setDbizdate(new UFDate(System.currentTimeMillis()));

            vNew.add(vonew);
            isHasFreeze = true;
          }

        }

      }

      if (!isHasFreeze) {
        vNew.add(voItems[i]);
      }
    }

    if (vNew.size() > 0) {
      GeneralBillItemVO[] voItemsNew = new GeneralBillItemVO[vNew.size()];
      vNew.copyInto(voItemsNew);
      nowVo.setChildrenVO(voItemsNew);
    }

    return nowVo;
  }

  public AggregatedValueObject setOrderPrice(IQueryInfo iQuery, AggregatedValueObject preVo, AggregatedValueObject nowVo)
    throws BusinessException
  {
    if ((preVo == null) || (nowVo == null) || (iQuery == null)) {
      return nowVo;
    }
    try
    {
      CircularlyAccessibleValueObject[] voaItemSrc = preVo.getChildrenVO();

      CircularlyAccessibleValueObject[] voaItemDest = nowVo.getChildrenVO();
      if ((voaItemSrc == null) || (voaItemDest == null) || (voaItemSrc.length == 0) || (voaItemDest.length == 0) || (voaItemSrc.length != voaItemDest.length))
      {
        SCMEnv.out("特色VO：表体为空，或行数不一致。");
        return nowVo;
      }
      GeneralBillItemVO voItem = null;
      HashMap htfirstbid = new HashMap();
      ArrayList alid = new ArrayList();
      String fisttype = null;
      ArrayList alItem = null;
      for (int i = 0; i < voaItemDest.length; i++) {
        voItem = (GeneralBillItemVO)voaItemDest[i];

        if (("30".equals(voItem.getCsourcetype())) || ("3U".equals(voItem.getCsourcetype())))
        {
          continue;
        }

        if ((!"30".equals(voItem.getCfirsttype())) && (!"3U".equals(voItem.getCfirsttype())))
          continue;
        fisttype = voItem.getCfirsttype();
        if (htfirstbid.containsKey(voItem.getCfirstbillbid())) {
          alItem = (ArrayList)htfirstbid.get(voItem.getCfirstbillbid());
        } else {
          alItem = new ArrayList();
          htfirstbid.put(voItem.getCfirstbillbid(), alItem);
        }
        alItem.add(voItem);

        alid.add(voItem.getCfirstbillbid());
      }

      if ((fisttype != null) && (alid != null) && (alid.size() > 0)) {
        ArrayList alprice = iQuery.getOrderPrices(fisttype, alid);
        if ((alprice != null) && (alprice.size() == alid.size())) {
          ArrayList alrow = null;
          for (int i = 0; i < alid.size(); i++) {
            alrow = (ArrayList)alprice.get(i);
            alItem = (ArrayList)htfirstbid.get(alid.get(i));
            if ((alItem != null) && (alItem.size() > 0)) {
              for (int j = 0; j < alItem.size(); j++) {
                voItem = (GeneralBillItemVO)alItem.get(j);
                if (voItem.getNsaleprice() == null)
                  voItem.setNsaleprice((UFDouble)alrow.get(0));
                if (voItem.getNtaxprice() == null)
                  voItem.setNtaxprice((UFDouble)alrow.get(1));
                if (alrow.size() > 2)
                {
                  voItem.setBsafeprice((UFBoolean)alrow.get(2));
                }if (alrow.size() <= 3)
                  continue;
                voItem.setBreturnprofit((UFBoolean)alrow.get(3));
              }
            }
          }
        }
      }

    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Remote Call", e);
    }

    return nowVo;
  }

  public static void validate(AggregatedValueObject hvo, ArrayList HeaderCanotNullString, ArrayList BodyCanotNullString)
    throws ValidationException
  {
    CircularlyAccessibleValueObject HHVO = hvo.getParentVO();
    CircularlyAccessibleValueObject[] HIVO = hvo.getChildrenVO();
    validateHeader(HHVO, HeaderCanotNullString);

    ArrayList alBdoyCheckStrings = new ArrayList();
    if (BodyCanotNullString.size() > 0)
    {
      BodyCanotNullString.add(0, "cinventoryid");
      for (int i = 0; i < HIVO.length; i++) {
        ArrayList alTempAL = new ArrayList();
        alTempAL.add(0, new Integer(i));
        for (int j = 0; j < BodyCanotNullString.size(); j++) {
          alTempAL.add(BodyCanotNullString.get(j));
        }
        alBdoyCheckStrings.add(alTempAL);
      }
      validateBody(HIVO, alBdoyCheckStrings);
    }
  }

  public static void validateBody(CircularlyAccessibleValueObject[] hivo, ArrayList alCheckStrings)
    throws ValidationException
  {
    if ((null == alCheckStrings) || (alCheckStrings.size() == 0)) {
      return;
    }

    ArrayList al = new ArrayList();
    ArrayList alrow = null;

    boolean bErrorFlag = false;

    for (int i = 0; i < alCheckStrings.size(); i++)
    {
      int iRowNumber = Integer.parseInt(((ArrayList)(ArrayList)alCheckStrings.get(i)).get(0).toString());

      if (hivo[iRowNumber].getStatus() == 3)
      {
        continue;
      }
      String sCheckAskField = ((ArrayList)(ArrayList)alCheckStrings.get(i)).get(1).toString();

      if ((hivo[iRowNumber].getAttributeValue(sCheckAskField) != null) && (hivo[iRowNumber].getAttributeValue(sCheckAskField).toString().trim().length() != 0))
      {
        for (int j = 2; j < ((ArrayList)(ArrayList)alCheckStrings.get(i)).size(); j++)
        {
          String sCheckField = ((ArrayList)(ArrayList)alCheckStrings.get(i)).get(j).toString();

          if ((hivo[iRowNumber].getAttributeValue(sCheckField) != null) && (hivo[iRowNumber].getAttributeValue(sCheckField).toString().trim().length() != 0)) {
            continue;
          }
          if (!bErrorFlag) {
            alrow = new ArrayList();
            alrow.add(0, new Integer(iRowNumber + 1));
            bErrorFlag = true;
          }
          alrow.add(sCheckField);
        }

      }

      if (!bErrorFlag)
        continue;
      for (int j = 0; j < al.size(); j++) {
        if (!((ArrayList)(ArrayList)al.get(j)).get(0).toString().trim().equals(alrow.get(0).toString()))
        {
          continue;
        }

        for (int k = 1; k < ((ArrayList)(ArrayList)al.get(j)).size(); k++) {
          alrow.add(((ArrayList)(ArrayList)al.get(j)).get(k).toString());
        }
        al.set(j, alrow);
        bErrorFlag = false;
        break;
      }

      if (bErrorFlag) {
        al.add(alrow);
        bErrorFlag = false;
      }
    }

    if (al.size() > 0) {
      ICNullFieldException ic = new ICNullFieldException(al);

      ic.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000199"));

      throw ic;
    }
  }

  public static void validateHeader(CircularlyAccessibleValueObject hhvo, ArrayList alCheckStrings)
    throws ValidationException
  {
    if ((null == alCheckStrings) || (alCheckStrings.size() == 0)) {
      return;
    }

    ArrayList errFields = new ArrayList();
    for (int i = 0; i < alCheckStrings.size(); i++) {
      String sCheckField = alCheckStrings.get(i).toString().trim();
      if ((hhvo.getAttributeValue(sCheckField) != null) && (hhvo.getAttributeValue(sCheckField).toString().trim().length() != 0))
        continue;
      errFields.add(new String(sCheckField));
    }

    StringBuffer message = new StringBuffer();
    message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000200"));
    if (errFields.size() > 0)
    {
      String[] temp = (String[])(String[])errFields.toArray(new String[0]);
      message.append(temp[0]);
      for (int i = 1; i < temp.length; i++) {
        message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPPSCMCommon-000000"));
        message.append(temp[i]);
      }

      ICHeaderNullFieldException ic = new ICHeaderNullFieldException(errFields);

      ic.setHint(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008check", "UPP4008check-000201"));

      throw ic;
    }
  }

  public static void checkGrossNumInput(CircularlyAccessibleValueObject[] hivo, String sInputNumString, String sBeforeKey)
    throws ValidationException
  {
    if ((sInputNumString == null) || (sInputNumString.trim().length() == 0) || (sBeforeKey == null)) {
      return;
    }

    sInputNumString = sInputNumString.trim();
    ArrayList al = new ArrayList();
    ArrayList alrow = null;
    int iTotalNumber = 0;
    boolean bErrorFlag = false;

    for (int i = 0; i < hivo.length; i++) {
      if ((hivo[i].getStatus() == 3) || (hivo[i].getAttributeValue(sBeforeKey) == null) || (hivo[i].getAttributeValue(sBeforeKey).toString().trim().length() == 0))
      {
        continue;
      }

      iTotalNumber++;
      InvVO inv = ((GeneralBillItemVO)hivo[i]).getInv();

      if ((inv.getIsmngstockbygrswt() != null) && (inv.getIsmngstockbygrswt().intValue() == 1) && ((hivo[i].getAttributeValue(sInputNumString) == null) || (hivo[i].getAttributeValue(sInputNumString).toString().trim().length() == 0))) {
        if (!bErrorFlag) {
          alrow = new ArrayList();
          alrow.add(0, new Integer(i + 1));
          bErrorFlag = true;
        }
        alrow.add(sInputNumString);
      }

      if (bErrorFlag) {
        al.add(alrow);
        bErrorFlag = false;
      }
    }

    if (al.size() > 0) {
      ICNumException ic = new ICNumException(al);
      ic.setHint("表体存货为按毛重管理库存,请输入毛重！");
      throw ic;
    }
  }

  public static void checkSameValueInput(AggregatedValueObject hvo, String[] sItemKeys, String[] sItemNames, String sCheckBeforeCol)
    throws BusinessException
  {
    if ((hvo == null) || (hvo.getChildrenVO() == null) || (hvo.getChildrenVO().length == 0) || (sItemKeys == null) || (sItemKeys.length == 0)) {
      return;
    }
    if (sItemKeys.length != sItemNames.length) {
      throw new BusinessException("sItemKeys.length!=sItemNames.length");
    }

    ArrayList alRowCheckString = null;
    ArrayList al = new ArrayList();

    HashMap htValue = new HashMap();
    Object value = null;
    Object valueFirst = null;

    alRowCheckString = new ArrayList();

    for (int i = 0; i < hvo.getChildrenVO().length; i++)
    {
      if ((sCheckBeforeCol != null) && ((hvo.getChildrenVO()[i].getAttributeValue(sCheckBeforeCol) == null) || (hvo.getChildrenVO()[i].getAttributeValue(sCheckBeforeCol).toString().trim().length() == 0)))
      {
        continue;
      }

      if (hvo.getChildrenVO()[i].getStatus() == 3) {
        continue;
      }
      for (int j = 0; j < sItemKeys.length; j++) {
        value = hvo.getChildrenVO()[i].getAttributeValue(sItemKeys[j]);
        if (value != null) {
          valueFirst = htValue.get(sItemKeys[j]);
          if (valueFirst == null) {
            htValue.put(sItemKeys[j], value);
            valueFirst = value;
          }

          if ((value.toString().equals(valueFirst.toString())) || 
            (alRowCheckString.contains(sItemNames[j]))) continue;
          alRowCheckString.add(sItemNames[j]);
        }

      }

    }

    if (alRowCheckString.size() > 0) {
      StringBuffer sbError = new StringBuffer("下列数据项要求整列值相同:");
      for (int i = 0; i < alRowCheckString.size(); i++) {
        String name = (String)alRowCheckString.get(i);
        sbError.append("\n" + name);
      }
      BusinessException ice = new BusinessException(sbError.toString());

      throw ice;
    }
  }
}