package nc.ui.ic.pub.lot;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Hashtable;
import nc.ui.ic.service.IICPub_LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pub.SCMEnv;

public class LotNumbRefPane extends UIRefPane
  implements IICPub_LotNumbRefPane
{
  protected String m_strCorpID = null;
  protected String m_strWareHouseName = null;
  protected String m_strWareHouseCode = null;
  protected String m_strWareHouseID = null;
  protected String m_strPk_calbody = null;
  protected String m_strCalbodyName = null;
  protected String m_strInventoryName = null;
  protected String m_strInventoryCode = null;
  protected String m_strInventoryID = null;

  protected boolean m_bisWasteWH = false;

  protected String m_strInventorySpec = null;
  protected String m_strInventoryType = null;
  protected String m_strMeasUnit = null;
  protected String m_strAssistMeaUnitID = null;

  protected ArrayList m_alFreeItemValue = null;

  protected String m_strFreeItem = null;
  protected boolean m_bisAssistUOM = false;
  protected boolean m_bisFreeMgn = false;
  protected boolean m_bIsTrackedBill = false;
  protected Integer m_iOutPriority = null;

  private String m_strNowBillHid = null;

  private String m_strNowSrcBid = null;

  private boolean m_bisClicked = false;

  private String[] m_aryParams = null;

  private LotNumbDlg m_dlgLotNumb = null;

  private boolean m_bIsMutiSel = false;
  private InvVO m_invvo = null;

  public LotNumbRefPane()
  {
    setReturnCode(true);

    getUITextField().setMaxLength(30);
    setIsBatchData(false);
  }

  public LotNumbRefPane(LayoutManager p0)
  {
    super(p0);
  }

  public LotNumbRefPane(LayoutManager p0, boolean p1)
  {
    super(p0, p1);
  }

  public LotNumbRefPane(boolean p0)
  {
    super(p0);
  }

  public boolean checkData()
  {
    if ((this.m_strInventoryID == null) || (this.m_strWareHouseID == null) || ((this.m_bisFreeMgn) && ((this.m_strFreeItem == null) || (this.m_strFreeItem.trim().length() == 0))) || ((this.m_bisAssistUOM) && ((this.m_strAssistMeaUnitID == null) || (this.m_strAssistMeaUnitID.trim().length() == 0)))) {
      return false;
    }
    getLotNumbDlg().setFreeItem(this.m_strFreeItem);
    getLotNumbDlg().setFreeItemValue(this.m_alFreeItemValue);
    getLotNumbDlg().setInvCode(this.m_strInventoryCode);
    getLotNumbDlg().setInvID(this.m_strInventoryID);
    getLotNumbDlg().setInvName(this.m_strInventoryName);
    getLotNumbDlg().setMeasUnit(this.m_strMeasUnit);
    getLotNumbDlg().setInvSpec(this.m_strInventorySpec);
    getLotNumbDlg().setInvType(this.m_strInventoryType);
    getLotNumbDlg().setWareHouseCode(this.m_strWareHouseCode);
    getLotNumbDlg().setWareHouseID(this.m_strWareHouseID);
    getLotNumbDlg().setWareHouseName(this.m_strWareHouseName);
    getLotNumbDlg().setCorpID(this.m_strCorpID);
    getLotNumbDlg().setAssistMeaUnitID(this.m_strAssistMeaUnitID);
    getLotNumbDlg().setWasteWH(this.m_bisWasteWH);
    getLotNumbDlg().setOutPriority(this.m_iOutPriority);
    getLotNumbDlg().setIsTrackedBill(this.m_bIsTrackedBill);
    getLotNumbDlg().m_strNowBillHid = this.m_strNowBillHid;
    getLotNumbDlg().setInvvo(this.m_invvo);
    getLotNumbDlg().setStrNowSrcBid(getStrNowSrcBid());
    try
    {
      getLotNumbDlg().setData();
    }
    catch (Exception e)
    {
      SCMEnv.out("Can not read data from server!");
      SCMEnv.error(e);
    }
    if ((getHTLotNumbVO() != null) && (getHTLotNumbVO().size() > 0) && 
      (getHTLotNumbVO().containsKey(getText().trim()))) {
      getLotNumbDlg().setSelVO((LotNumbRefVO)getHTLotNumbVO().get(getText().trim()));
      getLotNumbDlg().onOK();
      return true;
    }

    clearReturnData();

    return false;
  }

  public void clearReturnData()
  {
    getLotNumbDlg().setSelVO(null);
  }

  public String[] getaryParams()
  {
    return this.m_aryParams;
  }

  public Hashtable getHTLotNumbVO()
  {
    return this.m_dlgLotNumb.getHTLotNumbVO();
  }

  public LotNumbDlg getLotNumbDlg()
  {
    if (this.m_dlgLotNumb == null)
    {
      this.m_dlgLotNumb = new LotNumbDlg(getParent(), this.m_bIsMutiSel);
    }

    return this.m_dlgLotNumb;
  }

  public LotNumbRefVO getLotNumbRefVO()
  {
    return getLotNumbDlg().getSelVO();
  }

  public LotNumbRefVO[] getLotNumbRefVOs()
  {
    return getLotNumbDlg().getSelVOs();
  }

  public String getRefBillCode()
  {
    return getLotNumbDlg().getBillCode();
  }

  public String getRefBillType()
  {
    return getLotNumbDlg().getBillType();
  }

  public UFDate getRefInvalideDate()
  {
    return getLotNumbDlg().getValidate();
  }

  public UFDouble getRefInvAssistQty()
  {
    return getLotNumbDlg().getInvAssistQty();
  }

  public UFDouble getRefInvQty()
  {
    return getLotNumbDlg().getInvQty();
  }

  public String getRefLotNumb()
  {
    return getLotNumbDlg().getLotNumb();
  }

  public String getRefTableBodyID()
  {
    return getLotNumbDlg().getBillBodyID();
  }

  public String getRefTableHeaderID()
  {
    return getLotNumbDlg().getBillHeaderID();
  }

  public boolean isClicked()
  {
    return this.m_bisClicked;
  }

  public void onButtonClicked()
  {
    if ((this.m_strInventoryID == null) || ((this.m_strWareHouseID == null) && (this.m_strPk_calbody == null)) || ((!this.m_bIsMutiSel) && (this.m_bisFreeMgn) && 
    	//edit by hk
    		(((!("1078".equals(m_strCorpID)))||(!("1108".equals(m_strCorpID))))&&((this.m_strFreeItem == null) || (this.m_strFreeItem.trim().length() == 0)))) ||
    	//edit by hk end
    		((!this.m_bIsMutiSel) && (this.m_bisAssistUOM) && ((this.m_strAssistMeaUnitID == null) || (this.m_strAssistMeaUnitID.trim().length() == 0))))
    {
      StringBuffer sbErrorMsg = new StringBuffer();
      sbErrorMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000453"));
      if (this.m_strInventoryID == null) {
        sbErrorMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000454"));
      }

      if ((this.m_strWareHouseID == null) && (this.m_strPk_calbody == null)) {
        sbErrorMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000455"));
      }
   	  
    	  if ((!this.m_bIsMutiSel) && (this.m_bisFreeMgn) && ((this.m_strFreeItem == null) || (this.m_strFreeItem.trim().length() == 0)))
    	  {
    		  sbErrorMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000456"));
    	  }
    	  
      if ((!this.m_bIsMutiSel) && (this.m_bisAssistUOM) && ((this.m_strAssistMeaUnitID == null) || (this.m_strAssistMeaUnitID.trim().length() == 0)))
      {
        sbErrorMsg.append(NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000457"));
      }
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("4008other", "UPP4008other-000458"), sbErrorMsg.toString());

      return;
    }

    getLotNumbDlg().setFreeItem(this.m_strFreeItem);
    getLotNumbDlg().setFreeItemValue(this.m_alFreeItemValue);
    getLotNumbDlg().setInvCode(this.m_strInventoryCode);
    getLotNumbDlg().setInvID(this.m_strInventoryID);
    getLotNumbDlg().setInvName(this.m_strInventoryName);
    getLotNumbDlg().setMeasUnit(this.m_strMeasUnit);
    getLotNumbDlg().setInvSpec(this.m_strInventorySpec);
    getLotNumbDlg().setInvType(this.m_strInventoryType);
    getLotNumbDlg().setWareHouseCode(this.m_strWareHouseCode);
    getLotNumbDlg().setWareHouseID(this.m_strWareHouseID);
    getLotNumbDlg().setWareHouseName(this.m_strWareHouseName);
    getLotNumbDlg().setCorpID(this.m_strCorpID);
    getLotNumbDlg().setAssistMeaUnitID(this.m_strAssistMeaUnitID);
    getLotNumbDlg().setWasteWH(this.m_bisWasteWH);
    getLotNumbDlg().setIsTrackedBill(this.m_bIsTrackedBill);
    getLotNumbDlg().setOutPriority(this.m_iOutPriority);
    getLotNumbDlg().setPk_calbody(this.m_strPk_calbody);
    getLotNumbDlg().setCalbodyName(this.m_strCalbodyName);
    getLotNumbDlg().setInvvo(this.m_invvo);

    getLotNumbDlg().setStrNowSrcBid(getStrNowSrcBid());
    try
    {
      getLotNumbDlg().setData();
    }
    catch (Exception e) {
      SCMEnv.out("Can not read data from server!");
      SCMEnv.error(e);
    }

    getLotNumbDlg().setVOtoUI();

    if (getLotNumbDlg().showModal() == 1)
    {
      getUITextField().setText(getRefLotNumb());
      this.m_bisClicked = true;
    }
    else {
      getLotNumbDlg().destroy();
      this.m_bisClicked = false;
    }
    getUITextField().setRequestFocusEnabled(true);
    getUITextField().grabFocus();
  }

  public GeneralBillVO queryAllLotData(GeneralBillVO gvo)
  {
    if ((gvo == null) && (gvo.getItemCount() == 0)) {
      return null;
    }

    GeneralBillVO voRet = null;
    try {
      voRet = LotNumbRefHelper.queryAllLotData(gvo);
    } catch (Exception e) {
      SCMEnv.error(e);
    }

    return voRet;
  }

  public void setaryParams(String[] newM_aryParams)
  {
    this.m_aryParams = newM_aryParams;
  }

  public void setIsMutiSel(boolean bIsMutiSel)
  {
    this.m_bIsMutiSel = bIsMutiSel;
  }

  public void setParameter(WhVO whvo, InvVO invvo)
  {
    this.m_invvo = invvo;
    if (whvo != null)
    {
      this.m_strCorpID = ((String)whvo.getAttributeValue("pk_corp"));
      this.m_strWareHouseID = ((String)whvo.getAttributeValue("cwarehouseid"));
      this.m_strWareHouseCode = ((String)whvo.getAttributeValue("cwarehousecode"));
      this.m_strWareHouseName = ((String)whvo.getAttributeValue("cwarehousename"));
      this.m_strPk_calbody = ((String)whvo.getAttributeValue("pk_calbody"));
      this.m_strCalbodyName = ((String)whvo.getAttributeValue("vcalbodyname"));

      if ((whvo.getIsWasteWh() != null) && (whvo.getIsWasteWh().intValue() == 1)) {
        this.m_bisWasteWH = true;
      }
      else
        this.m_bisWasteWH = false;
    }
    else
    {
      this.m_bisClicked = false;
      this.m_strWareHouseID = null;
      this.m_strCorpID = null;
      this.m_strPk_calbody = null;
    }
    if (invvo != null) {
      this.m_strInventoryID = ((String)invvo.getAttributeValue("cinventoryid"));
      this.m_strInventoryCode = ((String)invvo.getAttributeValue("cinventorycode"));
      this.m_strInventoryName = ((String)invvo.getAttributeValue("invname"));

      this.m_strInventorySpec = ((String)invvo.getAttributeValue("invspec"));
      this.m_strInventoryType = ((String)invvo.getAttributeValue("invtype"));
      this.m_strMeasUnit = ((String)invvo.getAttributeValue("measdocname"));
      this.m_strAssistMeaUnitID = ((String)invvo.getAttributeValue("castunitid"));
      if (invvo.getOuttrackin() != null)
        this.m_bIsTrackedBill = invvo.getOuttrackin().booleanValue();
      this.m_iOutPriority = invvo.getOutpriority();

      if ((invvo.getIsFreeItemMgt() != null) && (invvo.getIsFreeItemMgt().intValue() == 1)) {
        this.m_bisFreeMgn = true;
        this.m_strFreeItem = ((String)invvo.getAttributeValue("vfree0"));
        this.m_alFreeItemValue = new ArrayList();
        for (int i = 0; i < 10; i++) {
          this.m_alFreeItemValue.add(i, invvo.getFreeItemValue("vfree" + (i + 1)));
        }
      }
      else
      {
        this.m_bisFreeMgn = false;
        this.m_strFreeItem = null;
        this.m_alFreeItemValue = null;
      }

      if ((invvo.getIsAstUOMmgt() != null) && (invvo.getIsAstUOMmgt().intValue() == 1)) {
        this.m_bisAssistUOM = true;
      }
      else {
        this.m_bisAssistUOM = false;
      }

      this.m_bisClicked = false;
      getLotNumbDlg().m_strNowBillHid = invvo.getCgeneralhid();
      this.m_strNowBillHid = invvo.getCgeneralhid();
    }
    else
    {
      this.m_bisClicked = false;

      this.m_strInventoryID = null;
    }

    clearReturnData();
  }

  protected void setParams(String[] lotparams, ArrayList freename, ArrayList freevalue, ArrayList freevalueID)
  {
    if (lotparams != null)
    {
      this.m_strWareHouseID = lotparams[0];
      this.m_strWareHouseCode = lotparams[1];
      this.m_strWareHouseName = lotparams[2];
      this.m_strInventoryID = lotparams[3];
      this.m_strInventoryCode = lotparams[4];
      this.m_strInventoryName = lotparams[5];

      this.m_strInventorySpec = lotparams[6];
      this.m_strInventoryType = lotparams[7];
      this.m_strMeasUnit = lotparams[8];

      setaryParams(lotparams);
      clearReturnData();
    }
    else {
      this.m_strWareHouseID = null;
      this.m_strInventoryID = null;
      clearReturnData();
    }
  }

  public void setWHParams(WhVO whvo)
  {
    if (whvo != null)
    {
      this.m_strCorpID = ((String)whvo.getAttributeValue("pk_corp"));
      this.m_strWareHouseID = ((String)whvo.getAttributeValue("cwarehouseid"));
      this.m_strWareHouseCode = ((String)whvo.getAttributeValue("cwarehousecode"));
      this.m_strWareHouseName = ((String)whvo.getAttributeValue("cwarehousename"));
      this.m_strPk_calbody = ((String)whvo.getAttributeValue("pk_calbody"));
      this.m_strCalbodyName = ((String)whvo.getAttributeValue("vcalbodyname"));

      if ((whvo.getIsWasteWh() != null) && (whvo.getIsWasteWh().intValue() == 1))
        this.m_bisWasteWH = true;
      else
        this.m_bisWasteWH = false;
    }
    else {
      this.m_bisClicked = false;
      this.m_strWareHouseID = null;
    }

    clearReturnData();
  }

  public void setParameter(CircularlyAccessibleValueObject whvo, CircularlyAccessibleValueObject invvo)
  {
    setParameter((WhVO)whvo, (InvVO)invvo);
  }
  public String getStrNowSrcBid() {
    return this.m_strNowSrcBid;
  }
  public void setStrNowSrcBid(String nowSrcBid) {
    this.m_strNowSrcBid = nowSrcBid;
  }
}