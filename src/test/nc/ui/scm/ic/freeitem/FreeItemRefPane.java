package nc.ui.scm.ic.freeitem;

import java.awt.LayoutManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.scm.pub.CacheTool;
import nc.vo.pub.BusinessException;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.freeitem.DefdefHeaderVO;
import nc.vo.scm.ic.freeitem.DefdefVO;

public class FreeItemRefPane extends UIRefPane
{
  private FreeItemDlg m_dlgFreeItemDlg;
  private String m_sCode = "";
  private String m_sInventoryName = "";
  private String m_sSpec = "";
  private String m_sType = "";

  private ArrayList m_alFreeItemID = new ArrayList();

  private ArrayList m_alFreeItemNameIn = new ArrayList();

  private ArrayList m_alFreeItemReturnName = new ArrayList();

  private ArrayList m_alFreeItemValue = new ArrayList();

  private ArrayList m_alFreeItemValueIn = new ArrayList();

  private ArrayList m_alFreeItemReturnValue = new ArrayList();

  private ArrayList m_alFreeItemReturnValueName = new ArrayList();

  private boolean JustClicked = false;

  private Hashtable m_hHashtable = new Hashtable();

  private Hashtable m_hInherentConsult = new Hashtable();

  public FreeItemRefPane()
  {
    setReturnCode(true);
    setEditable(false);
    setIsBatchData(false);
  }

  public FreeItemRefPane(LayoutManager p0)
  {
    super(p0);
    setIsBatchData(false);
  }

  public FreeItemRefPane(LayoutManager p0, boolean p1)
  {
    super(p0, p1);
    setIsBatchData(false);
  }

  public FreeItemRefPane(boolean p0)
  {
    super(p0);
    setIsBatchData(false);
  }

  protected ArrayList checkFreeItemNameOID()
  {
    if ((null == this.m_alFreeItemID) || (this.m_alFreeItemID.size() == 0)) {
      return null;
    }
    boolean bAllNull = true;
    for (int i = 0; i < this.m_alFreeItemID.size(); i++) {
      if ((null == this.m_alFreeItemID.get(i)) || (this.m_alFreeItemID.get(i).toString().trim().length() == 0))
        continue;
      bAllNull = false;
      break;
    }

    if (bAllNull) {
      return null;
    }
    return this.m_alFreeItemID;
  }

  protected boolean checkOpenFlag()
  {
    if ((null == this.m_sCode) || (null == this.m_sInventoryName)) {
      System.out.println("没有存货编码或名称");
      return false;
    }
    if ((this.m_sCode.equals("")) || (this.m_sInventoryName.equals("")) || (null == checkFreeItemNameOID()))
    {
      System.out.println("没有存货编码或名称或自由项设置");
      return false;
    }
    return true;
  }

  public void clearReturnAllValue()
  {
    this.m_alFreeItemReturnName = null;
    this.m_alFreeItemNameIn = null;
    this.m_alFreeItemReturnValue = null;
    this.m_alFreeItemReturnValueName = null;
  }

  protected void fromOIDtoName()
  {
    if (null == this.m_alFreeItemID) {
      return;
    }

    this.m_alFreeItemNameIn = null;
    this.m_alFreeItemNameIn = new ArrayList();

    UIRefPane urpUIRefPane = null;

    for (int j = 0; j < this.m_alFreeItemID.size(); j++) {
      if ((null == this.m_alFreeItemID.get(j)) || (this.m_alFreeItemID.get(j).toString().trim().length() == 0))
      {
        continue;
      }
      String sOID = this.m_alFreeItemID.get(j).toString().trim();

      String sInherentConsultName = null;
      if (this.m_hInherentConsult.containsKey(sOID)) {
        sInherentConsultName = (String)this.m_hInherentConsult.get(sOID);
      }
      else
      {
        try
        {
          Object obj = CacheTool.getCellValue("bd_defdef", "pk_defdef", "pk_bdinfo", sOID);
          if ((obj != null) && (((Object[])(Object[])obj)[0] != null) && (((Object[])(Object[])obj)[0].toString().trim().length() != 0))
          {
            obj = CacheTool.getCellValue("bd_bdinfo", "pk_bdinfo", "refnodename", ((Object[])(Object[])obj)[0].toString());

            if ((obj != null) && (((Object[])(Object[])obj)[0] != null) && (((Object[])(Object[])obj)[0].toString().trim().length() != 0))
            {
              sInherentConsultName = ((Object[])(Object[])obj)[0].toString().trim();
              this.m_hInherentConsult.put(sOID, sInherentConsultName);
            }
          }
        }
        catch (BusinessException e)
        {
          System.out.println(e.getMessage());
        }
      }

      if (null != sInherentConsultName) {
        ArrayList alTemp = new ArrayList();
        alTemp.add(new Integer(1));
        alTemp.add(sInherentConsultName);
        this.m_alFreeItemNameIn.add(alTemp);
        this.m_alFreeItemReturnName.add(sInherentConsultName);

        if (null != this.m_alFreeItemValue.get(j)) {
          urpUIRefPane = null;
          urpUIRefPane = new UIRefPane();
          urpUIRefPane.setRefNodeName(alTemp.get(1).toString().trim());
          urpUIRefPane.setPK(this.m_alFreeItemValue.get(j).toString());
          this.m_alFreeItemReturnValueName.add(urpUIRefPane.getRefName());
        } else {
          this.m_alFreeItemReturnValueName.add(null);
        }

      }
      else if (this.m_hHashtable.containsKey(sOID)) {
        ArrayList alTemp = new ArrayList();
        alTemp = (ArrayList)this.m_hHashtable.get(sOID);
        this.m_alFreeItemNameIn.add(alTemp);
      }
      else
      {
        try
        {
          DefdefVO dvoDefdefVO = DefHelper.findByPrimaryKey(sOID);
          if ((null == dvoDefdefVO) || (null == dvoDefdefVO.getParentVO()))
          {
            System.out.println("Error:User def ref is not found." + sOID);
          }
          else if ((dvoDefdefVO.getChildrenVO() == null) || (dvoDefdefVO.getChildrenVO().length == 0)) {
            System.out.println("ERROR:User def ref DATA are not found." + sOID);

            ArrayList alTemp = new ArrayList();
            alTemp.add(new Integer(0));
            alTemp.add(((DefdefHeaderVO)(DefdefHeaderVO)dvoDefdefVO.getParentVO()).getDefname().toString().trim());

            alTemp.add((DefdefHeaderVO)(DefdefHeaderVO)dvoDefdefVO.getParentVO());
            this.m_alFreeItemNameIn.add(alTemp);

            this.m_hHashtable.put(sOID, alTemp);
          }
          else {
            System.out.println("Hint:User def ref data are found." + sOID);

            ArrayList alTemp = new ArrayList();
            alTemp.add(new Integer(2));
            alTemp.add(((DefdefHeaderVO)(DefdefHeaderVO)dvoDefdefVO.getParentVO()).getDefname().toString().trim());
            alTemp.add(((DefdefHeaderVO)(DefdefHeaderVO)dvoDefdefVO.getParentVO()).getPrimaryKey().toString().trim());

            alTemp.add(((DefdefHeaderVO)(DefdefHeaderVO)dvoDefdefVO.getParentVO()).getLengthnum());

            alTemp.add(((DefdefHeaderVO)(DefdefHeaderVO)dvoDefdefVO.getParentVO()).getPk_defdoclist());
            this.m_alFreeItemNameIn.add(alTemp);

            this.m_hHashtable.put(sOID, alTemp);
          }
        } catch (Exception e) {
          System.out.println("数据通讯失败！");
        }
      }
    }
  }

  protected String getDlgReturnFreeItem()
  {
    this.m_alFreeItemValue = null;
    this.m_alFreeItemValue = new ArrayList();
    this.m_alFreeItemReturnName = null;
    this.m_alFreeItemReturnName = new ArrayList();
    this.m_alFreeItemReturnValueName = null;
    this.m_alFreeItemReturnValueName = new ArrayList();

    String[][] dlgReturnFreeItem = getFreeItemDlg().getReturnFreeItem();
    int j = 0;
    for (int i = 0; i < this.m_alFreeItemID.size(); i++) {
      if ((null == this.m_alFreeItemID.get(i)) || (this.m_alFreeItemID.get(i).toString().trim().length() == 0))
      {
        this.m_alFreeItemReturnName.add(null);
        this.m_alFreeItemValue.add(null);
        this.m_alFreeItemReturnValueName.add(null);
      }
      else if (j >= dlgReturnFreeItem.length) {
        this.m_alFreeItemReturnName.add(null);
        this.m_alFreeItemValue.add(null);
        this.m_alFreeItemReturnValueName.add(null);
      } else {
        this.m_alFreeItemReturnName.add(dlgReturnFreeItem[j][0]);
        this.m_alFreeItemValue.add(dlgReturnFreeItem[j][2]);
        this.m_alFreeItemReturnValueName.add(dlgReturnFreeItem[j][2]);
        j++;
      }

    }

    this.m_alFreeItemReturnValue = this.m_alFreeItemValue;

    if (null == this.m_alFreeItemReturnValueName) {
      return "";
    }
    String returnString = "";
    int k = 0;
    for (int i = 0; i < this.m_alFreeItemReturnValueName.size(); i++) {
      if ((dlgReturnFreeItem[k][0] == null) || (dlgReturnFreeItem[k][1] == null) || (dlgReturnFreeItem[k][2] == null) || (null == this.m_alFreeItemReturnValueName.get(i)))
      {
        continue;
      }

      returnString = returnString + "[" + dlgReturnFreeItem[k][0] + ":" + this.m_alFreeItemReturnValueName.get(i).toString().trim() + "]";

      k++;
      if (k > dlgReturnFreeItem.length - 1) {
        break;
      }
    }
    return returnString;
  }

  protected FreeItemDlg getFreeItemDlg()
  {
    if (this.m_dlgFreeItemDlg == null) {
      this.m_dlgFreeItemDlg = new FreeItemDlg(getParent());
      this.m_dlgFreeItemDlg.setLocationRelativeTo(this);
      this.m_dlgFreeItemDlg.setInventoryName(this.m_sCode, this.m_sInventoryName, this.m_sSpec, this.m_sType);

      this.m_dlgFreeItemDlg.setFreeItemName(this.m_alFreeItemNameIn);

      packFreeItemValue();
      this.m_dlgFreeItemDlg.setFreeItemValue(this.m_alFreeItemValueIn);

      this.m_dlgFreeItemDlg.addKeyListener(new IvjKeyListener());
      this.m_dlgFreeItemDlg.setVisible(true);
    }
    return this.m_dlgFreeItemDlg;
  }

  public ArrayList getFreeItemName()
  {
    if ((null == this.m_alFreeItemReturnName) || (this.m_alFreeItemReturnName.size() == 0)) {
      return null;
    }
    boolean bAllNull = true;
    for (int i = 0; i < this.m_alFreeItemReturnName.size(); i++) {
      if (null != this.m_alFreeItemReturnName.get(i)) {
        bAllNull = false;
        break;
      }
    }
    if (bAllNull) {
      return null;
    }
    return this.m_alFreeItemReturnName;
  }

  public ArrayList getFreeItemValue()
  {
    if ((null == this.m_alFreeItemReturnValueName) || (this.m_alFreeItemReturnValueName.size() == 0))
    {
      return null;
    }
    boolean bAllNull = true;
    for (int i = 0; i < this.m_alFreeItemReturnValueName.size(); i++) {
      if (null != this.m_alFreeItemReturnValueName.get(i)) {
        bAllNull = false;
        break;
      }
    }
    if (bAllNull) {
      return null;
    }
    return this.m_alFreeItemReturnValueName;
  }

  public ArrayList getFreeItemValueID()
  {
    if ((null == this.m_alFreeItemReturnValue) || (this.m_alFreeItemReturnValue.size() == 0))
    {
      return null;
    }
    boolean bAllNull = true;
    for (int i = 0; i < this.m_alFreeItemReturnValue.size(); i++) {
      if (null != this.m_alFreeItemReturnValue.get(i)) {
        bAllNull = false;
        break;
      }
    }
    if (bAllNull) {
      return null;
    }
    return this.m_alFreeItemReturnValue;
  }

  public FreeVO getFreeVO()
  {
    FreeVO fvoFVO = new FreeVO();
    if ((this.m_alFreeItemID != null) && (this.m_alFreeItemID.size() == 10) && (this.m_alFreeItemReturnName != null) && (this.m_alFreeItemReturnName.size() == 10) && (this.m_alFreeItemReturnValue != null) && (this.m_alFreeItemReturnValue.size() == 10))
    {
      for (int i = 1; i <= 10; i++) {
        fvoFVO.setAttributeValue("vfreeid" + Integer.toString(i).trim(), this.m_alFreeItemID.get(i - 1));

        fvoFVO.setAttributeValue("vfreename" + Integer.toString(i).trim(), this.m_alFreeItemReturnName.get(i - 1));

        fvoFVO.setAttributeValue("vfree" + Integer.toString(i).trim(), this.m_alFreeItemReturnValue.get(i - 1));
      }

    }
    else
    {
      for (int i = 1; i <= 10; i++) {
        fvoFVO.setAttributeValue("vfreeid" + Integer.toString(i).trim(), null);
        fvoFVO.setAttributeValue("vfreename" + Integer.toString(i).trim(), null);
        fvoFVO.setAttributeValue("vfree" + Integer.toString(i).trim(), null);
      }
    }

    fvoFVO.setAttributeValue("vfree0", getText() == null ? null : getText().trim());

    return fvoFVO;
  }

  protected boolean isJustClicked()
  {
    return this.JustClicked;
  }

  public void onButtonClicked()
  {
    int lengthOfInput = 0; int m_iMinusOfString = 0;
    String returnString = "";
    String tempString = "";
    this.m_dlgFreeItemDlg = null;
    if (checkOpenFlag()) {
      if (getFreeItemDlg().getResult() == 1)
      {
        returnString = getDlgReturnFreeItem();
        System.out.println(returnString);
        if (returnString.length() > getMaxLength() - m_iMinusOfString)
          lengthOfInput = getMaxLength() - m_iMinusOfString;
        else {
          lengthOfInput = returnString.length();
        }
        tempString = returnString.substring(0, lengthOfInput);

        boolean bIsEditable = isEditable();
        if (!bIsEditable) {
          setEditable(true);
        }
        setText(tempString);
        setEditable(bIsEditable);

        setJustClicked(true);
      } else {
        setJustClicked(false);
      }
    }
    getUITextField().setRequestFocusEnabled(true);
    getUITextField().grabFocus();
  }

  protected void packFreeItemName()
  {
    this.m_alFreeItemNameIn = null;
    if (null == this.m_alFreeItemReturnName) {
      return;
    }
    this.m_alFreeItemNameIn = new ArrayList();
    for (int j = 0; j < this.m_alFreeItemReturnName.size(); j++)
      if (null != this.m_alFreeItemReturnName.get(j))
        this.m_alFreeItemNameIn.add(this.m_alFreeItemReturnName.get(j));
  }

  protected void packFreeItemValue()
  {
    this.m_alFreeItemValueIn = null;
    if (null == this.m_alFreeItemValue) {
      return;
    }
    this.m_alFreeItemValueIn = new ArrayList();
    for (int j = 0; j < this.m_alFreeItemValue.size(); j++)
      if (null != this.m_alFreeItemValue.get(j))
        this.m_alFreeItemValueIn.add(this.m_alFreeItemValue.get(j));
  }

  protected void setFreeItemParam(ArrayList alParam)
  {
    if ((alParam == null) || (alParam.size() != 24)) {
      this.m_sCode = "";
      this.m_sInventoryName = "";
      this.m_sSpec = "";
      this.m_sType = "";
      this.m_alFreeItemValue = null;

      clearReturnAllValue();

      setText("");
    }
    else {
      this.m_sCode = null;
      this.m_sInventoryName = null;
      this.m_sSpec = null;
      this.m_sType = null;
      if (null != alParam.get(0)) {
        this.m_sCode = alParam.get(0).toString().trim();
      }
      if (null != alParam.get(1)) {
        this.m_sInventoryName = alParam.get(1).toString().trim();
      }
      if (null != alParam.get(2)) {
        this.m_sSpec = alParam.get(2).toString().trim();
      }
      if (null != alParam.get(3)) {
        this.m_sType = alParam.get(3).toString().trim();
      }
      this.m_alFreeItemID = null;
      this.m_alFreeItemValue = null;
      this.m_alFreeItemID = new ArrayList();
      this.m_alFreeItemValue = new ArrayList();
      for (int i = 4; i < 14; i++) {
        if (null == alParam.get(i))
          this.m_alFreeItemID.add(null);
        else {
          this.m_alFreeItemID.add(alParam.get(i).toString().trim());
        }
        if (null == alParam.get(i + 10))
          this.m_alFreeItemValue.add(null);
        else {
          this.m_alFreeItemValue.add(alParam.get(i + 10).toString().trim());
        }
      }
      this.m_alFreeItemID = checkFreeItemNameOID();
      this.m_alFreeItemReturnName = null;
      this.m_alFreeItemNameIn = null;
      this.m_alFreeItemReturnValue = null;
      this.m_alFreeItemReturnValueName = null;
      fromOIDtoName();
    }
  }

  public void setFreeItemParam(InvVO ivoVO)
  {
    this.m_sCode = "";
    this.m_sInventoryName = "";
    this.m_sSpec = "";
    this.m_sType = "";

    if ((ivoVO == null) || (null == ivoVO.getAttributeValue("cinventoryid")) || (ivoVO.getAttributeValue("cinventoryid").toString().trim().length() == 0) || (null == ivoVO.getAttributeValue("isFreeItemMgt")) || (ivoVO.getAttributeValue("isFreeItemMgt").toString().trim() == Integer.toString(0).trim()))
    {
      this.m_alFreeItemValue = null;

      clearReturnAllValue();

      setText("");
    }
    else {
      if (null != ivoVO.getAttributeValue("cinventorycode")) {
        this.m_sCode = ivoVO.getCinventorycode().trim();
      }
      if (null != ivoVO.getAttributeValue("invname")) {
        this.m_sInventoryName = ivoVO.getInvname().trim();
      }
      if (null != ivoVO.getAttributeValue("invspec")) {
        this.m_sSpec = ivoVO.getInvspec().toString().trim();
      }
      if (null != ivoVO.getAttributeValue("invtype")) {
        this.m_sType = ivoVO.getInvtype().toString().trim();
      }
      this.m_alFreeItemID = null;
      this.m_alFreeItemValue = null;
      this.m_alFreeItemNameIn = null;
      this.m_alFreeItemReturnName = null;
      this.m_alFreeItemReturnName = new ArrayList();
      this.m_alFreeItemNameIn = new ArrayList();
      this.m_alFreeItemID = new ArrayList();
      this.m_alFreeItemValue = new ArrayList();
      int iIDCount = 0; int iNameCount = 0;
      for (int i = 1; i <= 10; i++) {
        if (null == ivoVO.getAttributeValue("vfreeid" + Integer.toString(i).trim())) {
          this.m_alFreeItemID.add(null);
        } else {
          this.m_alFreeItemID.add(ivoVO.getAttributeValue("vfreeid" + Integer.toString(i).trim()).toString().trim());

          iIDCount++;
        }
        if (null == ivoVO.getAttributeValue("vfreename" + Integer.toString(i).trim())) {
          this.m_alFreeItemReturnName.add(null);
        } else {
          this.m_alFreeItemReturnName.add(ivoVO.getAttributeValue("vfreename" + Integer.toString(i).trim()).toString().trim());

          iNameCount++;
        }
        if (null == ivoVO.getAttributeValue("vfree" + Integer.toString(i).trim()))
          this.m_alFreeItemValue.add(null);
        else {
          this.m_alFreeItemValue.add(ivoVO.getAttributeValue("vfree" + Integer.toString(i).trim()).toString().trim());
        }
      }

      if ((null == ivoVO.getAttributeValue("vfree0")) || (ivoVO.getAttributeValue("vfree0").toString().trim().length() == 0))
        setText(null);
      else {
        setText(ivoVO.getAttributeValue("vfree0").toString().trim());
      }

      if (iNameCount != iIDCount) {
        this.m_sCode = "";
        this.m_sInventoryName = "";
        this.m_sSpec = "";
        this.m_sType = "";

        clearReturnAllValue();

        setText("");

        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000259"));
      }

      this.m_alFreeItemValueIn = this.m_alFreeItemValue;
      this.m_alFreeItemReturnValue = this.m_alFreeItemValue;
      this.m_alFreeItemReturnValueName = this.m_alFreeItemValue;

      fromOIDtoName();
    }
  }

  protected void setInherentConsult()
  {
  }

  protected void setJustClicked(boolean newJustClicked)
  {
    this.JustClicked = newJustClicked;
  }
  class IvjKeyListener extends KeyAdapter {
    IvjKeyListener() {
    }
    public void keyPressed(KeyEvent e) { if (e.getKeyCode() == 27)
        FreeItemRefPane.this.getFreeItemDlg().closeCancel();
    }
  }
}