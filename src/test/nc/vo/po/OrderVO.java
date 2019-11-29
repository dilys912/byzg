package nc.vo.po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.po.base.IOrderstatus;
import nc.itf.scm.cenpur.service.CentrPurchaseUtil;
import nc.itf.scm.cenpur.service.ChgDataUtil;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.po.pub.PoSaveCheckParaVO;
import nc.vo.po.rp.OrderReceivePlanVO;
import nc.vo.pu.ic.IBillAction;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IGetBusiDataForFlow;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.pub.pf.IPfRetGroupPkCorp;
import nc.vo.scm.field.pu.FieldDBValidate;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.BillRowNoVO;
import nc.vo.scm.pub.IBillCode;
import nc.vo.scm.pub.IVendorInfo;
import nc.vo.scm.pub.IscmDefCheckVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.ObjectUtils;

public class OrderVO extends AggregatedValueObject
  implements IBillCode, IPfRetGroupPkCorp, IVendorInfo, IBillAction, IPfRetCheckInfo, IGetBusiDataForFlow, IscmDefCheckVO
{
  private UFBoolean m_ufbExistClosed = UFBoolean.FALSE;

  private static Hashtable hashDiscountFlag = null;

  private static Hashtable hashLaborFlag = null;

  private boolean m_bBodyChanged = false;

  private boolean m_bFirstTime = true;

  private boolean m_bFirstTimeSP = true;

  private boolean m_bFirstTimeStock = true;

  private boolean m_bFirstTimePrice = true;

  private String m_busitype = null;

  private String m_grourPkCorp = null;

  private int m_iStatus = 0;
  private OrderReceivePlanVO[] m_voaOldRP = null;

  private OrderReceivePlanVO[] m_voaRP = null;

  private OrderVO m_voOld = null;

  private OrderHeaderVO m_voParent = null;
  private OrderItemVO[] m_voaChildren = null;
  long currentTimestamp;
  long initialTimestamp;
  private String m_strBillAction = "未明确动作";

  private boolean m_bNeedCreatNewBillCode = true;

  private boolean m_bUsedByFunc = false;

  
  public boolean isUsedByFunc()
  {
    return this.m_bUsedByFunc;
  }

  public void setUsedByFunc(boolean bNewVal)
  {
    this.m_bUsedByFunc = bNewVal;
  }

  public boolean isNeedCreateNewBillCode()
  {
    return this.m_bNeedCreatNewBillCode;
  }

  public void setNeedCreateNewBillCode(boolean bNewVal)
  {
    this.m_bNeedCreatNewBillCode = bNewVal;
  }

  public boolean isExistClosed()
  {
    return this.m_ufbExistClosed == null ? false : this.m_ufbExistClosed.booleanValue();
  }

  public void setExistClosed(UFBoolean ufbNewVal)
  {
    this.m_ufbExistClosed = ufbNewVal;
  }

  public boolean isFirstTimeStock()
  {
    return this.m_bFirstTimeStock;
  }

  public void setFirstTimeStock(boolean newVal)
  {
    this.m_bFirstTimeStock = newVal;
  }

  public boolean isFirstTimePrice()
  {
    return this.m_bFirstTimePrice;
  }

  public void setFirstTimePrice(boolean newVal)
  {
    this.m_bFirstTimePrice = newVal;
  }

  public boolean isAllRowsClosed()
  {
    if ((this.m_voaChildren == null) || (this.m_voaChildren.length == 0)) {
      return true;
    }

    int iLen = this.m_voaChildren.length;
    for (int i = 0; i < iLen; i++) {
      if (this.m_voaChildren[i] == null) {
        continue;
      }
      if (this.m_voaChildren[i].isActive()) {
        return false;
      }
    }

    return true;
  }

  public String getBillAction()
  {
    return this.m_strBillAction;
  }

  public void setBillAction(String newVal)
  {
    this.m_strBillAction = newVal;
  }

  public ArrayList<String[]> getArrCorpArrStoOrgIds()
  {
    if ((getBodyVO() == null) || (getBodyVO().length == 0)) {
      return null;
    }
    ArrayList listCorp = new ArrayList();
    ArrayList listStoOrg = new ArrayList();
    ArrayList listBaseId = new ArrayList();
    String strCorp = null;
    String strStoOrg = null;
    String strBaseId = null;
    String strComKey = null;
    int iLen = getBodyVO().length;
    HashMap mapComKey = new HashMap();
    for (int i = 0; i < iLen; i++) {
      if (getBodyVO()[i] == null) {
        continue;
      }
      strCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyVO()[i].getPk_arrvcorp());
      strStoOrg = PuPubVO.getString_TrimZeroLenAsNull(getBodyVO()[i].getPk_arrvstoorg());
      strBaseId = PuPubVO.getString_TrimZeroLenAsNull(getBodyVO()[i].getCbaseid());
      if (strStoOrg == null) {
        continue;
      }
      strComKey = strCorp + strStoOrg + strBaseId;
      if (mapComKey.containsKey(strComKey)) {
        continue;
      }
      listStoOrg.add(strStoOrg);
      listCorp.add(strCorp);
      listBaseId.add(strBaseId);

      mapComKey.put(strComKey, "");
    }
    if (listStoOrg.size() == 0) {
      return null;
    }

    ArrayList listRet = new ArrayList();
    listRet.add((String[])(String[])listStoOrg.toArray(new String[listStoOrg.size()]));
    listRet.add((String[])(String[])listCorp.toArray(new String[listStoOrg.size()]));
    listRet.add((String[])(String[])listBaseId.toArray(new String[listStoOrg.size()]));

    return listRet;
  }

  public String[] getArrStoOrgIds()
  {
    if ((getBodyVO() == null) || (getBodyVO().length == 0)) {
      return null;
    }
    ArrayList listArrStoOrg = new ArrayList();
    int iLen = getBodyVO().length;
    for (int i = 0; i < iLen; i++) {
      if ((getBodyVO()[i] == null) || (PuPubVO.getString_TrimZeroLenAsNull(getBodyVO()[i].getPk_arrvstoorg()) == null) || (listArrStoOrg.contains(PuPubVO.getString_TrimZeroLenAsNull(getBodyVO()[i].getPk_arrvstoorg()))))
      {
        continue;
      }

      listArrStoOrg.add(PuPubVO.getString_TrimZeroLenAsNull(getBodyVO()[i].getPk_arrvstoorg()));
    }
    if (listArrStoOrg.size() == 0) {
      return null;
    }

    return (String[])(String[])listArrStoOrg.toArray(new String[listArrStoOrg.size()]);
  }

  public CircularlyAccessibleValueObject[] getChildrenVO()
  {
    return this.m_voaChildren;
  }

  public CircularlyAccessibleValueObject getParentVO()
  {
    return this.m_voParent;
  }

  public void setChildrenVO(CircularlyAccessibleValueObject[] voaChildren)
  {
    if ((voaChildren instanceof OrderItemVO[])) {
      this.m_voaChildren = ((OrderItemVO[])(OrderItemVO[])voaChildren);
    }
    else if ((voaChildren != null) && (voaChildren.length > 0)) {
      this.m_voaChildren = new OrderItemVO[voaChildren.length];
      for (int i = 0; i < voaChildren.length; i++)
        this.m_voaChildren[i] = ((OrderItemVO)voaChildren[i]);
    }
    else {
      this.m_voaChildren = null;
    }
  }

  public void setParentVO(CircularlyAccessibleValueObject voParent)
  {
    this.m_voParent = ((OrderHeaderVO)voParent);
  }

  public void setPrimaryKey(String sPK)
  {
    try
    {
      if (this.m_voParent != null)
        this.m_voParent.setPrimaryKey(sPK);
    } catch (Exception e) {
      SCMEnv.out(e);
    }
  }

  public Object clone()
  {
    Object oCloned = null;
    try {
      oCloned = ObjectUtils.serializableClone(this);
    } catch (Exception e) {
      SCMEnv.out(e);
    }
    return oCloned;
  }

  public BillCodeObjValueVO getBillCodeObjVO()
  {
    BillCodeObjValueVO voBCOV = new BillCodeObjValueVO();

    return voBCOV;
  }

  public String getBillTypeCode()
  {
    if (this.m_voParent != null) {
      return this.m_voParent.getCtypecode();
    }
    return null;
  }

  public String getPk_corp()
  {
    if (this.m_voParent != null) {
      return this.m_voParent.getPk_corp();
    }
    return null;
  }

  public String getVBillCode()
  {
    if (this.m_voParent != null) {
      return this.m_voParent.getVcode();
    }
    return null;
  }

  public void setVBillCode(String vCode)
  {
    if (this.m_voParent != null)
      this.m_voParent.setVcode(vCode);
  }

  public int getStatus()
  {
    return this.m_iStatus;
  }

  public void setStatus(int status)
  {
    this.m_iStatus = status;
  }

  public OrderVO()
  {
    this(0);
  }

  public OrderVO(int num)
  {
    this.m_voParent = new OrderHeaderVO();

    this.m_voaChildren = new OrderItemVO[num];
    for (int i = 0; i < num; i++)
      this.m_voaChildren[i] = new OrderItemVO();
  }

  public OrderVO(OrderHeaderVO voHead, OrderItemVO[] voaItem)
  {
    setParentVO(voHead);
    setChildrenVO(voaItem);
  }

  public static OrderVO getAtpVOByCloseVOs(OrderCloseItemVO[] voaClose)
  {
    if (voaClose == null) {
      return null;
    }

    OrderVO voOrder = new OrderVO();

    voOrder.getHeadVO().setCorderid(voaClose[0].getCorderid());
    voOrder.getHeadVO().setPk_corp(voaClose[0].getPk_corp());

    voOrder.getHeadVO().setDorderdate(voaClose[0].getDorderdate());
    voOrder.getHeadVO().setVordercode(voaClose[0].getVordercode());
    voOrder.getHeadVO().setCbiztype(voaClose[0].getCbiztype());

    voOrder.setChildrenVO(voaClose);

    return voOrder;
  }

  public static OrderVO getAtpVOByRPVOs(OrderReceivePlanVO[] voaRP)
  {
    return getAtpVOByRPVOs(null, voaRP);
  }

  public static OrderVO getAtpVOByRPVOs(OrderVO voOrder, OrderReceivePlanVO[] voaRP)
  {
    if (voaRP == null) {
      return null;
    }

    int iAtpLen = voaRP.length;

    OrderItemVO[] voaItem = new OrderItemVO[iAtpLen];
    for (int i = 0; i < iAtpLen; i++) {
      voaItem[i] = OrderItemVO.getAtpBodyVOByRPVO(voaRP[i]);
    }

    OrderVO voAtpOrder = new OrderVO();
    if (voOrder != null) {
      voAtpOrder.setParentVO(voOrder.getHeadVO());
    } else {
      voAtpOrder.getHeadVO().setCorderid(voaRP[0].getCorderid());
      voAtpOrder.getHeadVO().setPk_corp(voaRP[0].getPk_corp());

      voAtpOrder.getHeadVO().setDorderdate(voaRP[0].getDplanarrvdate());
      voAtpOrder.getHeadVO().setCbiztype(voaRP[0].getCbiztype());
    }
    voAtpOrder.setChildrenVO(voaItem);

    return voAtpOrder;
  }

  public OrderItemVO[] getBodyVO() {
    return this.m_voaChildren;
  }

  public OrderItemVO getBodyVOByBId(String sBId)
  {
    sBId = PuPubVO.getString_TrimZeroLenAsNull(sBId);
    if ((sBId == null) || (getBodyVO() == null)) {
      return null;
    }
    return getBodyVOsByBIds(new String[] { sBId })[0];
  }

  public OrderItemVO[] getBodyVOsByBIds(String[] saBId)
  {
    if ((saBId == null) || (getBodyVO() == null)) {
      return null;
    }

    HashMap hmapIdAndVO = new HashMap();
    int iLen = getBodyVO().length;
    for (int i = 0; i < iLen; i++) {
      hmapIdAndVO.put(getBodyVO()[i].getCorder_bid(), getBodyVO()[i]);
    }

    iLen = saBId.length;
    OrderItemVO[] voaItem = new OrderItemVO[iLen];
    for (int i = 0; i < iLen; i++) {
      voaItem[i] = ((OrderItemVO)hmapIdAndVO.get(saBId[i]));
    }

    return voaItem;
  }

  public OrderItemVO[] getBodyVOsByRowNos(String[] saRowNo)
  {
    if ((saRowNo == null) || (getBodyVO() == null)) {
      return null;
    }

    HashMap hmapRowNoAndVO = new HashMap();
    int iLen = getBodyVO().length;
    for (int i = 0; i < iLen; i++) {
      hmapRowNoAndVO.put(getBodyVO()[i].getCrowno(), getBodyVO()[i]);
    }

    iLen = saRowNo.length;
    OrderItemVO[] voaItem = new OrderItemVO[iLen];
    for (int i = 0; i < iLen; i++) {
      voaItem[i] = ((OrderItemVO)hmapRowNoAndVO.get(saRowNo[i]));
    }

    return voaItem;
  }

  public OrderVO getCheckVO()
  {
    OrderItemVO[] voaCheckItem = null;

    OrderItemVO[] voaAllItem = getBodyVO();
    if (voaAllItem != null) {
      Vector vecCheckBody = new Vector();
      int iTotalLen = voaAllItem.length;
      for (int i = 0; i < iTotalLen; i++)
      {
        if ((voaAllItem[i].getStatus() == 3) || (voaAllItem[i].isRevised()))
          continue;
        vecCheckBody.addElement(voaAllItem[i]);
      }

      int iCheckLen = vecCheckBody.size();
      if (iCheckLen != 0) {
        voaCheckItem = new OrderItemVO[iCheckLen];
        vecCheckBody.copyInto(voaCheckItem);
      }
    }

    OrderVO voCheck = (OrderVO)clone();
    voCheck.setChildrenVO(voaCheckItem);

    return voCheck;
  }

  public int getClosedBodyNum()
  {
    int iOpenLen = 0;
    int iBodyLen = getBodyVO().length;
    for (int i = 0; i < iBodyLen; i++) {
      if (getBodyVO()[i].isActive()) {
        iOpenLen++;
      }

    }

    return iBodyLen - iOpenLen;
  }

  public int getConfirmedBodyNum()
  {
    int iConfirmedLen = 0;
    int iBodyLen = getBodyVO().length;
    for (int i = 0; i < iBodyLen; i++) {
      if (getBodyVO()[i].getForderrowstatus().compareTo(OrderItemVO.FORDERROWSTATUS_CONFIRM) == 0) {
        iConfirmedLen++;
      }

    }

    return iConfirmedLen;
  }

  public String getGroupPkCorp()
  {
    return this.m_grourPkCorp;
  }

  public OrderHeaderVO getHeadVO() {
    return this.m_voParent;
  }

  public OrderVO getHistoryVO(String sCorrectUserId)
  {
    OrderVO voHistory = (OrderVO)clone();

    voHistory.setStatus(2);

    OrderHeaderVO voHead = voHistory.getHeadVO();
    voHead.setStatus(2);

    voHead.setCorderid(null);
    voHead.setBislatest(OrderHeaderVO.BISLATEST_NO);
    String strTemp = null;
    voHead.setTs(strTemp);

    OrderItemVO[] voaItem = voHistory.getBodyVO();
    int iBodyLen = voaItem.length;
    for (int i = 0; i < iBodyLen; i++) {
      voaItem[i].setStatus(2);

      voaItem[i].setCorder_bid(null);
      voaItem[i].setCorderid(null);

      voaItem[i].setDcorrectdate(getHeadVO().getDrevisiondate());
      voaItem[i].setCcorrectrowid(getBodyVO()[i].getCorder_bid());
      voaItem[i].setCoperator(sCorrectUserId);

      voaItem[i].setIisactive(OrderItemVO.IISACTIVE_REVISION);

      voaItem[i].setTs(null);
    }

    return voHistory;
  }

  public String getKey()
  {
    return "cvendormangid";
  }

  public OrderVO getMaxStockCheckVO()
  {
    OrderVO voCheck = getCheckVO();
    if (voCheck == null) {
      return null;
    }

    Vector vecAtp = new Vector();
    OrderItemVO[] voaItem = voCheck.getBodyVO();
    int iLen = voaItem.length;
    for (int i = 0; i < iLen; i++) {
      if (PuPubVO.getUFDouble_NullAsZero(voaItem[i].getNordernum()).compareTo(VariableConst.ZERO) > 0) {
        vecAtp.add(voaItem[i]);
      }
    }
    int iAtpLen = vecAtp.size();
    if (iAtpLen == 0) {
      return null;
    }

    OrderVO voAtp = (OrderVO)voCheck.clone();
    voAtp.setChildrenVO((OrderItemVO[])(OrderItemVO[])vecAtp.toArray(new OrderItemVO[iAtpLen]));
    return voAtp;
  }

  public OrderReceivePlanVO[] getOldRPVOs()
  {
    return this.m_voaOldRP;
  }

  public OrderVO getOldVO()
  {
    return this.m_voOld;
  }

  public String getPkBusinessType()
  {
    return this.m_busitype;
  }

  public Integer getPos()
  {
    return VariableConst.ZERO_INTEGER;
  }

  public OrderReceivePlanVO[] getRPVOs()
  {
    return this.m_voaRP;
  }

  public boolean isActive()
  {
    if (getBodyVO() == null) {
      return false;
    }

    int iBodyLen = getBodyVO().length;
    for (int i = 0; i < iBodyLen; i++) {
      if (!getBodyVO()[i].isActive()) {
        return false;
      }
    }

    return true;
  }

  public boolean isBodyChanged()
  {
    return this.m_bBodyChanged;
  }

  public boolean isCanBeAnnuled()
  {
    return isCanBeModified();
  }

  public boolean isCanBeAudited()
  {
    Integer iBillStatus = getHeadVO().getForderstatus();

    return (BillStatus.FREE.compareTo(iBillStatus) == 0) || (BillStatus.AUDITING.compareTo(iBillStatus) == 0);
  }

  public boolean isCanBeClosed()
  {
    Integer iBillStatus = getHeadVO().getForderstatus();
    if ((BillStatus.FREE.compareTo(iBillStatus) == 0) || (BillStatus.AUDITFAIL.compareTo(iBillStatus) == 0) || (BillStatus.AUDITING.compareTo(iBillStatus) == 0) || (BillStatus.FREEZE.compareTo(iBillStatus) == 0))
    {
      return false;
    }

    return getClosedBodyNum() != getBodyVO().length;
  }

  public boolean isCanBeModified()
  {
    Integer iBillStatus = getHeadVO().getForderstatus();

    return (BillStatus.FREE.compareTo(iBillStatus) == 0) || (BillStatus.AUDITFAIL.compareTo(iBillStatus) == 0);
  }

  public boolean isCanBeOpened()
  {
    Integer iBillStatus = getHeadVO().getForderstatus();
    if ((BillStatus.FREE.compareTo(iBillStatus) == 0) || (BillStatus.AUDITFAIL.compareTo(iBillStatus) == 0) || (BillStatus.AUDITING.compareTo(iBillStatus) == 0) || (BillStatus.FREEZE.compareTo(iBillStatus) == 0))
    {
      return false;
    }

    return getClosedBodyNum() != 0;
  }

  public boolean isCanBeUnAudited()
  {
    Integer iBillStatus = getHeadVO().getForderstatus();

    if ((BillStatus.FREE.compareTo(iBillStatus) == 0) || (BillStatus.AUDITFAIL.compareTo(iBillStatus) == 0) || (BillStatus.FREEZE.compareTo(iBillStatus) == 0) || (BillStatus.OUTPUT.compareTo(iBillStatus) == 0))
    {
      return false;
    }
    if ((getClosedBodyNum() > 0) || (getConfirmedBodyNum() > 0)) {
      return false;
    }

    if (isExistClosed()) {
      return false;
    }

    return OrderHeaderVO.NVERSION_FIRST.compareTo(PuPubVO.getInteger_NullAs(getHeadVO().getNversion(), OrderHeaderVO.NVERSION_FIRST)) == 0;
  }

  public boolean isCanDayPl()
  {
    Integer iBillStatus = getHeadVO() == null ? null : getHeadVO().getForderstatus();

    return (iBillStatus != null) && ((BillStatus.AUDITED.compareTo(iBillStatus) == 0) || (BillStatus.OUTPUT.compareTo(iBillStatus) == 0));
  }

  public boolean isDeliver()
  {
    if (getHeadVO() == null) {
      return false;
    }
    return getHeadVO().isDeliver();
  }

  public boolean isFirstTime()
  {
    return this.m_bFirstTime;
  }

  public boolean isFirstTimeSP()
  {
    return this.m_bFirstTimeSP;
  }

  public boolean isFrmOneContract()
  {
    OrderVO voShow = getCheckVO();

    String sOldCntId = PuPubVO.getString_TrimZeroLenAsNull(voShow.getBodyVO()[0].getCcontractid());

    if (sOldCntId == null) {
      return false;
    }
    String sCurCntId = null;

    int iBodyLen = voShow.getBodyVO().length;
    for (int i = 1; i < iBodyLen; i++) {
      sCurCntId = PuPubVO.getString_TrimZeroLenAsNull(voShow.getBodyVO()[i].getCcontractid());

      if ((sCurCntId == null) || (!sCurCntId.equals(sOldCntId))) {
        return false;
      }
      sOldCntId = sCurCntId;
    }
    return true;
  }

  private boolean isLaborDiscount(OrderItemVO item)
    throws ValidationException
  {
    if (hashLaborFlag == null) {
      hashLaborFlag = new Hashtable();
      hashDiscountFlag = new Hashtable();
      OrderItemVO[] allCheckingItems = getCheckVO().getBodyVO();
      if ((allCheckingItems != null) && (allCheckingItems.length > 0)) {
        int iLen = allCheckingItems.length;
        ArrayList aryDisBaseId = new ArrayList();
        String strTemp = null;
        for (int i = 0; i < iLen; i++) {
          strTemp = allCheckingItems[i].getCbaseid();
          if ((strTemp == null) || (strTemp.trim().equals("")) || (aryDisBaseId.contains(strTemp))) {
            continue;
          }
          aryDisBaseId.add(strTemp);
        }
        if (aryDisBaseId.size() > 0) {
          iLen = aryDisBaseId.size();
          String[] saBaseId = (String[])(String[])aryDisBaseId.toArray(new String[iLen]);
          try {
            Object[][] objs = ChgDataUtil.getMultiColValue2("bd_invbasdoc", "pk_invbasdoc", null, null, new String[] { "laborflag", "discountflag" }, saBaseId, null, null);

            if ((objs != null) && (objs.length == iLen))
              for (int i = 0; i < iLen; i++) {
                hashLaborFlag.put(saBaseId[i], objs[i][0]);
                hashDiscountFlag.put(saBaseId[i], objs[i][1]);
              }
          }
          catch (BusinessException be) {
            throw new ValidationException(be.getMessage());
          }
        }
      }
    }
    String strBaseId = item.getCbaseid();
    if ((strBaseId == null) || (strBaseId.trim().equals(""))) {
      SCMEnv.out("程序BUG，未获取采购订单表体存货的基本档案ID");
      return false;
    }
    if (!hashLaborFlag.containsKey(strBaseId)) {
      try {
        Object[][] objs = ChgDataUtil.getMultiColValue2("bd_invbasdoc", "pk_invbasdoc", null, null, new String[] { "laborflag", "discountflag" }, new String[] { strBaseId }, null, null);

        if ((objs != null) && (objs.length == 1)) {
          hashLaborFlag.put(strBaseId, objs[0][0]);
          hashDiscountFlag.put(strBaseId, objs[0][1]);
        }
      } catch (BusinessException be) {
        throw new ValidationException(be.getMessage());
      }
    }
    if (!hashLaborFlag.containsKey(strBaseId)) {
      SCMEnv.out("程序BUG或基础数据错误，未获取采购订单表体存货的劳务折属性值");
      return false;
    }
    boolean bLab = PuPubVO.getUFBoolean_NullAs(hashLaborFlag.get(strBaseId), new UFBoolean(false)).booleanValue();
    boolean bDis = PuPubVO.getUFBoolean_NullAs(hashDiscountFlag.get(strBaseId), new UFBoolean(false)).booleanValue();

    return (bLab) || (bDis);
  }

  public boolean isNew()
  {
    return getStatus() == 2;
  }

  public boolean isReturn()
  {
    if (getHeadVO() == null) {
      return false;
    }
    return getHeadVO().isReturn();
  }

  public boolean isReplenish()
  {
    if ((getHeadVO() == null) || (getHeadVO().getBisreplenish() == null)) {
      return false;
    }
    return getHeadVO().getBisreplenish().booleanValue();
  }

  public void setBodyChanged(boolean bValue)
  {
    this.m_bBodyChanged = bValue;
  }

  public void setFirstTime(boolean bFirstTime)
  {
    this.m_bFirstTime = bFirstTime;
  }

  public void setFirstTimeSP(boolean bFirstTime)
  {
    this.m_bFirstTimeSP = bFirstTime;
  }

  public void setGroupPkCorp(String pk_corp)
  {
    this.m_grourPkCorp = pk_corp;
  }

  public void setOldRPVOs(OrderReceivePlanVO[] voaOldRP)
  {
    this.m_voaOldRP = voaOldRP;
  }

  public void setOldVO(OrderVO voOld)
  {
    this.m_voOld = voOld;
  }

  public void setPkBusinessType(String busitype)
  {
    this.m_busitype = busitype;
  }

  public void setRPVOs(OrderReceivePlanVO[] voaRP)
  {
    this.m_voaRP = voaRP;
  }

  public static void splitOrderVOByRPVOs(int iAfterBillStatus, OrderVO[] voaOrder)
  {
    if (voaOrder == null) {
      return;
    }

    int iLen = voaOrder.length;
    for (int i = 0; i < iLen; i++)
    {
      Vector vecItem = new Vector();

      OrderItemVO[] voaItem = voaOrder[i].getBodyVO();

      String sStoOrgId = null;
      if (voaItem != null) {
        int iBodyLen = voaItem.length;
        for (int j = 0; j < iBodyLen; j++)
        {
          OrderItemVO[] voaPlanItem = voaItem[j].getBodyVOsByRPVOs(iAfterBillStatus, voaItem[j].getRPVOs());

          sStoOrgId = voaItem[j].getPk_arrvstoorg();

          int iPlanBodyLen = voaPlanItem.length;
          for (int iPlan = 0; iPlan < iPlanBodyLen; iPlan++) {
            if (PuPubVO.getString_TrimZeroLenAsNull(voaPlanItem[iPlan].getPk_arrvstoorg()) == null) {
              voaPlanItem[iPlan].setPk_arrvstoorg(sStoOrgId);
            }
            vecItem.add(voaPlanItem[iPlan]);
          }
        }
      }
      voaOrder[i].setChildrenVO((OrderItemVO[])(OrderItemVO[])vecItem.toArray(new OrderItemVO[vecItem.size()]));
    }
  }

  public void validate()
    throws ValidationException
  {
  }

  public void validate(PoSaveCheckParaVO voPara)
    throws ValidationException
  {
    OrderVO voShow = getCheckVO();

    OrderHeaderVO voShowHead = voShow.getHeadVO();
    OrderItemVO[] voaShowItem = voShow.getBodyVO();

    if ((voaShowItem == null) || (voaShowItem.length == 0)) {
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000174"));
    }

    validateLastReviseDate();

    voShowHead.validate();

    BillRowNoVO.validateRowNo(voaShowItem, "crowno");

    int iUIBodyLen = voaShowItem.length;
    for (int i = 0; i < iUIBodyLen; i++)
    {
      if (voaShowItem[i] == null)
      {
        continue;
      }

      voaShowItem[i].validate();

      if (isLaborDiscount(voaShowItem[i]))
        continue;
      if ((voShow.getHeadVO().getBisreplenish() != null) && (voShow.getHeadVO().getBisreplenish().booleanValue()) && 
        (PuPubVO.getUFDouble_NullAsZero(voaShowItem[i].getNordernum()).compareTo(VariableConst.ZERO) < 0)) {
        throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000175", null, new String[] { voaShowItem[i].getCrowno() }));
      }

      if ((voShow.getHeadVO().getBreturn() != null) && (voShow.getHeadVO().getBreturn().booleanValue())) {
        if (PuPubVO.getUFDouble_NullAsZero(voaShowItem[i].getNordernum()).compareTo(VariableConst.ZERO) > 0) {
          throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000176", null, new String[] { voaShowItem[i].getCrowno() }));
        }
      }
      else if (PuPubVO.getUFDouble_NullAsZero(voaShowItem[i].getNordernum()).compareTo(VariableConst.ZERO) < 0) {
        throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000177", null, new String[] { voaShowItem[i].getCrowno() }));
      }

    }

    for (int i = 0; i < iUIBodyLen; i++) {
      UFDate dPlanDate = voaShowItem[i].getDplanarrvdate();
      if ((dPlanDate == null) || (dPlanDate.toString().trim().equals("")))
        continue;
      if (dPlanDate.before(voShowHead.getDorderdate())) {
        String sCurLine = NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000178", null, new String[] { voaShowItem[i].getCrowno() });
        throw new ValidationException(sCurLine);
      }

    }

    validateRP();

    validatePrePayLimit(voPara.iDigit_Curr_FinanceLocal);

    validateFrmOneCntWhenRevise();

    FieldDBValidate.validate(this);
  }

  public void validateLastReviseDate()
    throws ValidationException
  {
    if ((getOldVO() == null) || (getOldVO().getHeadVO() == null) || (getOldVO().getHeadVO().getDrevisiondate() == null) || (getHeadVO() == null) || (getHeadVO().getDrevisiondate() == null))
    {
      return;
    }
    UFDate ufdLastDate = getOldVO().getHeadVO().getDrevisiondate();
    UFDate ufdThisDate = getHeadVO().getDrevisiondate();
    if (ufdThisDate.compareTo(ufdLastDate) < 0)
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004pub", "UPP4004pub-000200"));
  }

  public void validateDiscard()
    throws ValidationException
  {
    validateRP();
  }

  public static void validateDiscardVOs(OrderVO[] voaOrder)
    throws ValidationException
  {
    if (voaOrder == null) {
      return;
    }

    int iLen = voaOrder.length;
    for (int i = 0; i < iLen; i++)
      voaOrder[i].validateRP();
  }

  private void validateFrmOneCntWhenRevise()
    throws ValidationException
  {
    if (OrderHeaderVO.NVERSION_FIRST.equals(getHeadVO().getNversion())) {
      return;
    }

    String sOldCntId = null;
    if ((getOldVO().isFrmOneContract()) && (PuPubVO.getUFDouble_ZeroAsNull(getOldVO().getHeadVO().getNprepaymny()) != null))
    {
      sOldCntId = getOldVO().getBodyVO()[0].getCcontractid();

      boolean bException = false;
      if (!isFrmOneContract()) {
        bException = true;
      } else {
        String sNewCntId = getCheckVO().getBodyVO()[0].getCcontractid();
        if (!PuPubVO.isEqual(sNewCntId, sOldCntId)) {
          bException = true;
        }

      }

      if (bException)
        throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000179", null, new String[] { getHeadVO().getVordercode() }));
    }
  }

  public void validatePrePayLimit(int iFinanceLoaclDigit)
    throws ValidationException
  {
    OrderVO voCheck = getCheckVO();
    if (voCheck == null) {
      return;
    }

    if (voCheck.getHeadVO().getNprepaymaxmny() == null) {
      return;
    }

    int iBodyLen = voCheck.getBodyVO().length;
    UFDouble dSum = VariableConst.ZERO;
    for (int i = 0; i < iBodyLen; i++)
    {
      dSum = dSum.add(PuPubVO.getUFDouble_NullAsZero(voCheck.getBodyVO()[i].getNtaxpricemny()));
    }

    dSum = dSum.setScale(iFinanceLoaclDigit, 4);

    if (voCheck.getHeadVO().getNprepaymaxmny().compareTo(dSum) > 0)
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000180", null, new String[] { voCheck.getHeadVO().getNprepaymaxmny() + "", dSum + "" }));
  }

  private void validateRP()
    throws ValidationException
  {
    if (getBodyVO() == null) {
      return;
    }

    if (getHeadVO().getCorderid() == null) {
      return;
    }

    String sOrderCode = getHeadVO().getVordercode();

    OrderItemVO voOldItem = null;
    int iBodyLen = getBodyVO().length;
    for (int i = 0; i < iBodyLen; i++)
    {
      if ((getBodyVO()[i].getStatus() == 2) || (!getBodyVO()[i].isHaveRP()))
      {
        continue;
      }
      String sRowNo = getBodyVO()[i].getCrowno();

      if (getStatus() == 3) {
        throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000181", null, new String[] { sOrderCode }));
      }

      if (getBodyVO()[i].getStatus() == 3) {
        throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000182", null, new String[] { sOrderCode, sRowNo }));
      }

      if (getOldVO() != null) {
        voOldItem = getOldVO().getBodyVOByBId(getBodyVO()[i].getCorder_bid());
        if ((voOldItem != null) && (!voOldItem.getCmangid().equals(getBodyVO()[i].getCmangid()))) {
          throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000183", null, new String[] { sOrderCode, sRowNo }));
        }
        if ((voOldItem != null) && (!PuPubVO.isEqual(voOldItem.getCassistunit(), getBodyVO()[i].getCassistunit()))) {
          throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000184", null, new String[] { sOrderCode, sRowNo }));
        }
      }

      if (PuPubVO.getUFDouble_NullAsZero(getBodyVO()[i].getNordernum()).compareTo(VariableConst.ZERO) < 0)
      {
        throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000185", null, new String[] { sOrderCode, sRowNo }));
      }

      if (PuPubVO.getUFDouble_NullAsZero(getBodyVO()[i].getNaccumrpnum()).compareTo(PuPubVO.getUFDouble_NullAsZero(getBodyVO()[i].getNordernum())) <= 0)
        continue;
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000186", null, new String[] { sOrderCode, sRowNo, getBodyVO()[i].getNaccumrpnum() + "" }));
    }
  }

  public boolean setLastestVo(OrderVO voLight, int iCurOperType)
  {
    if ((voLight == null) || ((voLight.getHeadVO() == null) && (voLight.getBodyVO() == null))) {
      SCMEnv.out("刷新回来的VO或其表头或其表体存在空，刷新失败!");
      return false;
    }

    OrderItemVO[] itemsDst = getBodyVO();
    OrderItemVO[] itemsLight = voLight.getBodyVO();
    int iLenDst = itemsDst.length;
    int iLenLight = itemsLight.length;

    if (iCurOperType == 0)
    {
      if (iLenDst != iLenLight) {
        SCMEnv.out("新增刷新失败：刷新回来的表体行数与界面显示的表体行数不一致!");
        return false;
      }

      getHeadVO().refreshByHeaderVo(voLight.getHeadVO());

      for (int i = 0; i < iLenDst; i++) {
        if ((itemsDst[i] == null) || (itemsLight[i] == null)) {
          continue;
        }
        itemsDst[i].refreshByItemVo(itemsLight[i]);
      }

    }
    else if (iCurOperType == 1)
    {
      OrderVO voCheckDst = getCheckVO();
      if ((voCheckDst == null) || (voCheckDst.getHeadVO() == null)) {
        SCMEnv.out("修改刷新失败：真正要刷新的单据为空!");
        return false;
      }

      getHeadVO().refreshByHeaderVo(voLight.getHeadVO());

      HashMap mapIdVo = new HashMap();
      for (int i = 0; i < iLenLight; i++) {
        if ((itemsLight[i] == null) || (itemsLight[i].getCorder_bid() == null)) {
          continue;
        }
        mapIdVo.put(itemsLight[i].getCorder_bid(), itemsLight[i]);
      }

      itemsDst = voCheckDst.getBodyVO();
      iLenDst = itemsDst.length;
      for (int i = 0; i < iLenDst; i++) {
        if ((itemsDst[i] == null) || (itemsDst[i].getCorder_bid() == null)) {
          continue;
        }
        if (!mapIdVo.containsKey(itemsDst[i].getCorder_bid())) {
          SCMEnv.out("修改刷新失败：存在表体找不到匹配的刷新表体!");
          return false;
        }
        itemsDst[i].refreshByItemVo((OrderItemVO)mapIdVo.get(itemsDst[i].getCorder_bid()));
      }

      setChildrenVO(itemsDst);
    }
    else
    {
      SCMEnv.out("不支持的操作类型:" + iCurOperType + "!");
      return false;
    }
    return true;
  }

  public int getBodyLen()
  {
    if (getBodyVO() == null) {
      return 0;
    }
    return getBodyVO().length;
  }

  public void validateCentrPur()
    throws ValidationException
  {
    if ((getHeadVO() == null) || (PuPubVO.getString_TrimZeroLenAsNull(getHeadVO().getCbiztype()) == null) || (PuPubVO.getString_TrimZeroLenAsNull(getHeadVO().getPk_corp()) == null))
    {
      return;
    }
    if ((getBodyVO() == null) || (getBodyVO().length == 0)) {
      return;
    }

    ArrayList listErrRowNo1i = new ArrayList();
    ArrayList listErrRowNo1ii = new ArrayList();
    ArrayList listErrRowNo2 = new ArrayList();
    ArrayList listErrRowNo3 = new ArrayList();
    String strLoginCorp = getHeadVO().getPk_corp();
    String strReqCorp = null;
    String strArrCorp = null;
    String strInvoiceCorp = null;
    int iLen = getBodyLen();
    OrderItemVO[] items = getBodyVO();
    HashMap mapCorpId = null;

    boolean bCentrPur = false;
    OrderstatusVO[] statVos = null;
    try {
      bCentrPur = CentrPurchaseUtil.isGroupBusiType(getHeadVO().getCbiztype());
      IOrderstatus bo = (IOrderstatus)NCLocator.getInstance().lookup(IOrderstatus.class.getName());
      statVos = bo.queryStatusVOsByPkCorpAndIDs(strLoginCorp, new String[] { getHeadVO().getCbiztype() });
    } catch (BusinessException e) {
      throw new ValidationException(e.getMessage());
    }

    boolean bArrPlan = (statVos != null) && (statVos.length > 0) && (statVos[0] != null) && (statVos[0].getBisneedrp() != null) && (statVos[0].getBisneedrp().intValue() == 1);

    for (int i = 0; i < iLen; i++) {
      if (items[i] == null) {
        continue;
      }
      strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(items[i].getPk_reqcorp());
      strArrCorp = PuPubVO.getString_TrimZeroLenAsNull(items[i].getPk_arrvcorp());
      strInvoiceCorp = PuPubVO.getString_TrimZeroLenAsNull(items[i].getPk_invoicecorp());

      mapCorpId = new HashMap();
      mapCorpId.put(strLoginCorp, "");
      if (strReqCorp != null) {
        mapCorpId.put(strReqCorp, "");
      }
      if (strArrCorp != null) {
        mapCorpId.put(strArrCorp, "");
      }
      if (strInvoiceCorp != null) {
        mapCorpId.put(strInvoiceCorp, "");
      }
      if (mapCorpId.size() > 2) {
        listErrRowNo1i.add(i + 1 + "");
      }

      if ((!bCentrPur) && 
        (mapCorpId.size() > 1)) {
        listErrRowNo1ii.add(i + 1 + "");
      }

      if ((strLoginCorp.equals(strArrCorp)) && (!strLoginCorp.equals(strInvoiceCorp))) {
        listErrRowNo2.add(i + 1 + "");
      }

      if ((!bArrPlan) && (strArrCorp == null)) {
        listErrRowNo3.add(i + 1 + "");
      }

    }

    StringBuffer message = new StringBuffer("");
    String strNo1i = null;
    String strNo1ii = null;
    String strNo2 = null;
    String strNo3 = null;
    boolean bThrowException = false;

    if (listErrRowNo1i.size() > 0) {
      bThrowException = true;
      strNo1i = (String)listErrRowNo1i.get(0);
      for (int i = 1; i < listErrRowNo1i.size(); i++) {
        strNo1i = strNo1i + NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000000");
        strNo1i = strNo1i + " " + listErrRowNo1i.get(i) + " ";
      }
      message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPT4004020201-000122", null, new String[] { strNo1i }));
    }

    if (listErrRowNo1ii.size() > 0) {
      if (bThrowException) {
        message.append("\n");
      }
      bThrowException = true;
      strNo1ii = (String)listErrRowNo1ii.get(0);
      for (int i = 1; i < listErrRowNo1ii.size(); i++) {
        strNo1ii = strNo1ii + NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000000");
        strNo1ii = strNo1ii + " " + listErrRowNo1ii.get(i) + " ";
      }
      message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPT4004020201-000123", null, new String[] { strNo1ii }));
    }

    if (listErrRowNo2.size() > 0) {
      if (bThrowException) {
        message.append("\n");
      }
      bThrowException = true;
      strNo2 = (String)listErrRowNo2.get(0);
      for (int i = 1; i < listErrRowNo2.size(); i++) {
        strNo2 = strNo2 + NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000000");
        strNo2 = strNo2 + " " + listErrRowNo2.get(i) + " ";
      }
      message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPT4004020201-000125", null, new String[] { strNo2 }));
    }

    if (listErrRowNo3.size() > 0) {
      if (bThrowException) {
        message.append("\n");
      }
      bThrowException = true;
      strNo3 = (String)listErrRowNo3.get(0);
      for (int i = 1; i < listErrRowNo3.size(); i++) {
        strNo3 = strNo3 + NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000000");
        strNo3 = strNo3 + " " + listErrRowNo3.get(i) + " ";
      }
      message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPT4004020201-000126", null, new String[] { strNo3 }));
    }
    if (bThrowException)
      throw new ValidationException(message.toString());
  }

  public void setCheckNote(String strCheckNote)
  {
  }

  public void setCheckState(int icheckState)
  {
    if (getHeadVO() != null)
      switch (icheckState) {
      case 3:
        getHeadVO().setForderstatus(new Integer(0));
        break;
      case 2:
        getHeadVO().setForderstatus(new Integer(2));
        break;
      case 1:
        getHeadVO().setForderstatus(new Integer(3));
        break;
      case 0:
        getHeadVO().setForderstatus(new Integer(4));
        break;
      }
  }

  public boolean isCheckSave()
  {
    return (getHeadVO() != null) && (getHeadVO().getForderstatus() != null) && (getHeadVO().getForderstatus().intValue() == 0);
  }

  public boolean isCheckGoing()
  {
    return (getHeadVO() != null) && (getHeadVO().getForderstatus() != null) && (getHeadVO().getForderstatus().intValue() == 2);
  }

  public boolean isCheckPass()
  {
    return (getHeadVO() != null) && (getHeadVO().getForderstatus() != null) && (getHeadVO().getForderstatus().intValue() == 3);
  }

  public boolean isCheckNoPass()
  {
    return (getHeadVO() != null) && (getHeadVO().getForderstatus() != null) && (getHeadVO().getForderstatus().intValue() == 4);
  }

  public UFDouble getPfAssMoney()
  {
    return null;
  }

  public String getPfCurrency()
  {
    return null;
  }

  public UFDouble getPfLocalMoney()
  {
    if (getBodyLen() == 0) {
      return null;
    }
    UFDouble ufdSumNmny = new UFDouble(0.0D);
    for (int i = 0; i < getBodyLen(); i++) {
      if (getBodyVO()[i] == null) {
        continue;
      }
      ufdSumNmny = ufdSumNmny.add(PuPubVO.getUFDouble_NullAsZero(getBodyVO()[i].getNmoney()));
    }
    return ufdSumNmny;
  }

  public UFDouble getPfMoney()
  {
    if (getBodyLen() == 0) {
      return null;
    }
    UFDouble ufdSumNmny = new UFDouble(0.0D);
    for (int i = 0; i < getBodyLen(); i++) {
      if (getBodyVO()[i] == null) {
        continue;
      }
      ufdSumNmny = ufdSumNmny.add(PuPubVO.getUFDouble_NullAsZero(getBodyVO()[i].getNoriginalcurmny()));
    }
    return ufdSumNmny;
  }

  public Object[] getBodyDefValues(int iserial)
  {
    if (getBodyVO() == null) {
      return null;
    }
    Object[] oaRet = new Object[getBodyVO().length];
    for (int i = 0; i < oaRet.length; i++) {
      oaRet[i] = getBodyVO()[i].getAttributeValue("vdef" + iserial);
    }
    return oaRet;
  }

  public String getCbilltypedef()
  {
    return "21";
  }

  public Object getHeadDefValue(int iserial)
  {
    if (getHeadVO() == null) {
      return null;
    }
    return getHeadVO().getAttributeValue("vdef" + iserial);
  }
  



  
}