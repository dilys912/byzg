package nc.ui.dm.dm102;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.dm.pub.cardpanel.DMBillCardPanel;
import nc.ui.dm.pub.cardpanel.DMBillListPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.vo.dm.dm102.OutbillHHeaderVO;
import nc.vo.dm.dm102.OutbillHVO;
import nc.vo.dm.dm102.OutbillVO;
import nc.vo.dm.dm104.DelivbillHHeaderVO;
import nc.vo.dm.dm104.DelivbillHItemVO;
import nc.vo.dm.pub.DMBillTypeConst;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class OutBillDlg extends UIDialog
  implements ListSelectionListener, BillEditListener, MouseListener
{
  protected Integer BD301;
  protected Integer BD501;
  protected Integer BD502;
  protected Integer BD503;
  protected Integer BD505;
  protected UFBoolean DM010;
  private DMBillCardPanel ivjBillCardPanel = null;

  private DMBillListPanel ivjBillListPanel = null;

  IvjEventHandler ivjEventHandler = new IvjEventHandler();

  private UIButton ivjUIButtonCancel = null;

  private UIButton ivjUIButtonOk = null;

  private UIButton ivjUIButtonPrint = null;

  private JPanel ivjUIDialogContentPane = null;

  private UIPanel ivjUIPanel1 = null;

  UFDouble[] m_delivbillinvnum = null;

  private DelivbillHHeaderVO m_delivHvo = new DelivbillHHeaderVO();

  private Hashtable m_htDispatcherForCbiztype = new Hashtable();

  private Hashtable m_htVOKeys = new Hashtable();
  private OutbillHVO[] m_hvos;
  protected int m_iFirstSelectCol = -1;

  protected int m_iFirstSelectRow = -1;

  protected boolean m_handlingAnotherHeadRow = false;

  protected int m_iLastSelectRow = -1;
  protected String[] m_itemANumKeys;
  protected String m_itemFactorKey;
  protected String[] m_itemNumKeys;
  protected String[] m_itemPriceKeys;
  private int m_listrow;
  private OutbillVO[] m_OutbillvosResoult;
  private String m_sBillTypeCode = DMBillTypeConst.m_delivCheckBill;
  protected String m_sCorpID;
  protected String m_sUserID;
  protected String m_sUserName;
  protected String m_sCorpName;
  protected String m_sDelivOrgPK;
  protected String m_sDelivOrgCode;
  protected String m_sDelivOrgName;
  private String m_sNodeCode = "40140412";

  private String m_sTitle = NCLangRes.getInstance().getStrByID("40140404", "UPT40140408-000068");

  private UFDate m_today = null;

  private DMDataVO[] m_planvos = null;

  public OutBillDlg(Container parent, String title, DMDataVO dmdvo)
  {
    super(parent, title);

    setDelivOrgPK((String)dmdvo.getAttributeValue("pkdelivorg"));
    setUserID((String)dmdvo.getAttributeValue("userid"));
    setUserName((String)dmdvo.getAttributeValue("username"));
    setCorpID((String)dmdvo.getAttributeValue("corpid"));
    setCorpName((String)dmdvo.getAttributeValue("corpname"));

    this.BD501 = ((Integer)dmdvo.getAttributeValue("BD501"));
    this.BD502 = ((Integer)dmdvo.getAttributeValue("BD502"));
    this.BD503 = ((Integer)dmdvo.getAttributeValue("BD503"));
    this.BD505 = ((Integer)dmdvo.getAttributeValue("BD505"));
    this.BD301 = ((Integer)dmdvo.getAttributeValue("BD301"));
    this.DM010 = ((UFBoolean)dmdvo.getAttributeValue("DM010"));

    initialize();
  }

  public void afterEdit(BillEditEvent e)
  {
    try
    {
      String strColName = e.getKey().trim();

      if ((this.m_handlingAnotherHeadRow) || (strColName.equals("doutnum"))) {
        this.m_handlingAnotherHeadRow = false;

        Object onum = null;
        UFDouble totalnum = new UFDouble(0);
        if ((this.m_handlingAnotherHeadRow) || (strColName.equals("doutnum")))
        {
          for (int i = 0; i < getBillListPanel().getBodyBillModel().getRowCount(); i++) {
            onum = getBillListPanel().getBodyBillModel().getValueAt(i, "doutnum");
            totalnum = totalnum.add(onum == null ? new UFDouble(0) : new UFDouble(onum.toString()));
          }

        }

        getBillListPanel().getHeadBillModel().setValueAt(totalnum, this.m_listrow, "dtotalout");
      }
    }
    catch (Exception e1)
    {
      reportException(e1);
      handleException(e1);
    }
  }

  public void bodyRowChange(BillEditEvent e)
  {
    int rownow = 0;
  }

  public void clearListRow()
  {
    this.m_listrow = -1;
  }

  protected DMBillListPanel getBillListPanel()
  {
    if (this.ivjBillListPanel == null) {
      try {
        this.ivjBillListPanel = new DMBillListPanel(false);
        this.ivjBillListPanel.setName("BillCardPanel");

        BillListData listdata = new BillListData(this.ivjBillListPanel.getTempletData("DM_BILL_TEMPLET_00CK"));

        this.m_itemNumKeys = new String[] { "dinvnum", "dtotalout", "doutnum", "donhandnum" };
        ArrayList alDecimalKey = new ArrayList();
        alDecimalKey.add(this.m_itemNumKeys);

        ArrayList alPrecision = new ArrayList();
        alPrecision.add(this.BD501);

        this.ivjBillListPanel.changeDecimalItemsPrecision(listdata, alDecimalKey, alPrecision);

        this.ivjBillListPanel.setListData(listdata);

        this.ivjBillListPanel.setCorp(getCorpID());

        this.ivjBillListPanel.setOperator(getUserID());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillListPanel;
  }

  public String getCorpID()
  {
    return this.m_sCorpID;
  }

  public String getCorpName()
  {
    return this.m_sCorpName;
  }

  public String getDelivOrgCode()
  {
    return this.m_sDelivOrgCode;
  }

  public String getDelivOrgName()
  {
    return this.m_sDelivOrgName;
  }

  public String getDelivOrgPK()
  {
    return this.m_sDelivOrgPK;
  }

  /** @deprecated */
  public DelivbillHHeaderVO getHvo()
  {
    return this.m_delivHvo;
  }

  private UIButton getUIButtonCancel()
  {
    if (this.ivjUIButtonCancel == null) {
      try {
        this.ivjUIButtonCancel = new UIButton();
        this.ivjUIButtonCancel.setName("UIButtonCancel");
        this.ivjUIButtonCancel.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonCancel;
  }

  private UIButton getUIButtonOk()
  {
    if (this.ivjUIButtonOk == null) {
      try {
        this.ivjUIButtonOk = new UIButton();
        this.ivjUIButtonOk.setName("UIButtonOk");
        this.ivjUIButtonOk.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonOk;
  }

  private UIButton getUIButtonPrint()
  {
    if (this.ivjUIButtonPrint == null) {
      try {
        this.ivjUIButtonPrint = new UIButton();
        this.ivjUIButtonPrint.setName("UIButtonPrint");
        this.ivjUIButtonPrint.setText(NCLangRes.getInstance().getStrByID("common", "UC000-0001146"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonPrint;
  }

  protected JPanel getUIDialogContentPane()
  {
    if (this.ivjUIDialogContentPane == null) {
      try {
        this.ivjUIDialogContentPane = new JPanel();
        this.ivjUIDialogContentPane.setName("UIDialogContentPane");
        this.ivjUIDialogContentPane.setLayout(new BorderLayout());
        getUIDialogContentPane().add(getBillListPanel(), "Center");
        getUIDialogContentPane().add(getUIPanel1(), "South");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIDialogContentPane;
  }

  private UIPanel getUIPanel1()
  {
    if (this.ivjUIPanel1 == null) {
      try {
        this.ivjUIPanel1 = new UIPanel();
        this.ivjUIPanel1.setName("UIPanel1");
        this.ivjUIPanel1.setMinimumSize(new Dimension(100, 100));
        getUIPanel1().add(getUIButtonOk(), getUIButtonOk().getName());
        getUIPanel1().add(getUIButtonCancel(), getUIButtonCancel().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIPanel1;
  }

  public String getUserID()
  {
    return this.m_sUserID;
  }

  public String getUserName()
  {
    return this.m_sUserName;
  }

  private void handleException(Throwable exception)
  {
  }

  private void initConnections()
    throws Exception
  {
    getUIButtonOk().addActionListener(this.ivjEventHandler);
    getUIButtonPrint().addActionListener(this.ivjEventHandler);
    getUIButtonCancel().addActionListener(this.ivjEventHandler);
  }

  private void initialize()
  {
    try
    {
      this.m_sBillTypeCode = "7U";

      setName("OutBillDlg");
      setDefaultCloseOperation(2);
      setSize(710, 500);
      setModal(true);
      setContentPane(getUIDialogContentPane());
      initConnections();
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getBillListPanel().setEnabled(true);
    getBillListPanel().getBodyTable().setEnabled(true);
    getBillListPanel().getHeadTable().getSelectionModel().addListSelectionListener(this);
    getBillListPanel().getHeadTable().addMouseListener(this);
    getBillListPanel().addMouseListener(this);
    getBillListPanel().addBodyEditListener(this);

    ((UIRefPane)getBillListPanel().getBodyItem("doutnum").getComponent()).setDelStr("-");
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mousePressed(MouseEvent e)
  {
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void onCancel(ActionEvent actionEvent)
  {
    setResult(2);
    close();
    fireUIDialogClosed(new UIDialogEvent(this, 202));
  }

  public void onOk(ActionEvent actionEvent)
  {
    try
    {
      nc.vo.dm.dm102.OutbillHItemVO[] items = null;

      GeneralBillVO[] gnrlbillvos = null;

      if (this.m_listrow < 0) {
        closeCancel();
        return;
      }
      if ((this.m_listrow < this.m_hvos.length) && (this.m_listrow >= 0)) {
        items = (nc.vo.dm.dm102.OutbillHItemVO[])(nc.vo.dm.dm102.OutbillHItemVO[])this.m_hvos[this.m_listrow].getChildrenVO();
      }
      if (items != null) {
        for (int i = 0; i < items.length; i++) {
          if (i < getBillListPanel().getBodyBillModel().getRowCount()) {
            Object obj = getBillListPanel().getBodyBillModel().getValueAt(i, "doutnum");
            if ((null == obj) || (obj.toString().trim().length() == 0))
              obj = new UFDouble(0);
            items[i].setDoutnum((UFDouble)obj);
          }
        }

      }

      if (!this.DM010.booleanValue())
      {
        for (int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++) {
          UFDouble ufdShouldOut = getBillListPanel().getHeadBillModel().getValueAt(i, "dinvnum") == null ? new UFDouble(0) : (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(i, "dinvnum");

          UFDouble ufdOut = getBillListPanel().getHeadBillModel().getValueAt(i, "dtotalout") == null ? new UFDouble(0) : (UFDouble)getBillListPanel().getHeadBillModel().getValueAt(i, "dtotalout");

          if (ufdShouldOut.doubleValue() < ufdOut.doubleValue()) {
            MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140404", "UPP40140404-000052"), NCLangRes.getInstance().getStrByID("40140404", "UPP40140404-000076"));

            return;
          }
        }

      }

      for (int k = 0; k < this.m_hvos.length; k++) {
        items = (nc.vo.dm.dm102.OutbillHItemVO[])(nc.vo.dm.dm102.OutbillHItemVO[])this.m_hvos[k].getChildrenVO();
        if ((items != null) && (items.length > 0)) {
          for (int l = 0; l < items.length; l++) {
            if ((items[l].getDoutnum() == null) || (items[l].getDoutnum().doubleValue() == 0.0D) || (items[l].getDoutnum().doubleValue() <= items[l].getDonhandnum().doubleValue()))
              continue;
            MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140404", "UPP40140404-000052"), NCLangRes.getInstance().getStrByID("40140404", "UPP40140404-000077"));

            return;
          }
        }

      }

      OutbillVO[] setvo = setVOs();
      if (setvo == null) {
        return;
      }
      this.m_OutbillvosResoult = DeliverydailyplanBO_Client.splitVOForICOutBill(setvo);

      int j = 0;
      gnrlbillvos = new GeneralBillVO[this.m_OutbillvosResoult.length];
      for (int i = 0; i < this.m_OutbillvosResoult.length; i++) {
        if (this.m_OutbillvosResoult[i].getChildrenVO()[0].getAttributeValue("vbilltype") != null) {
          for (j = 0; j < this.m_OutbillvosResoult[i].getChildrenVO().length; j++) {
            this.m_OutbillvosResoult[i].getChildrenVO()[j].setStatus(2);
            this.m_OutbillvosResoult[i].getChildrenVO()[j].setAttributeValue("csourcetype", "7D");
          }
          if (((String)this.m_OutbillvosResoult[i].getChildrenVO()[0].getAttributeValue("vbilltype")).equals("30"))
          {
            for (j = 0; j < this.m_OutbillvosResoult[i].getChildrenVO().length; j++) {
              this.m_OutbillvosResoult[i].getChildrenVO()[j].setAttributeValue("voutbilltype", BillTypeConst.m_saleOut);
            }
            gnrlbillvos[i] = ((GeneralBillVO)PfChangeBO_Client.pfChangeBillToBill(this.m_OutbillvosResoult[i], "7Y", "4C"));
          }
          else if (((String)this.m_OutbillvosResoult[i].getChildrenVO()[0].getAttributeValue("vbilltype")).equals(BillTypeConst.m_AllocationOrder))
          {
            for (j = 0; j < this.m_OutbillvosResoult[i].getChildrenVO().length; j++) {
              this.m_OutbillvosResoult[i].getChildrenVO()[j].setAttributeValue("voutbilltype", BillTypeConst.m_otherOut);
            }
            gnrlbillvos[i] = ((GeneralBillVO)PfChangeBO_Client.pfChangeBillToBill(this.m_OutbillvosResoult[i], "7Y", "4I"));
          }
          else {
            if ((!((String)this.m_OutbillvosResoult[i].getChildrenVO()[0].getAttributeValue("vbilltype")).equals("5C")) && (!((String)this.m_OutbillvosResoult[i].getChildrenVO()[0].getAttributeValue("vbilltype")).equals("5D")) && (!((String)this.m_OutbillvosResoult[i].getChildrenVO()[0].getAttributeValue("vbilltype")).equals("5E")) && (!((String)this.m_OutbillvosResoult[i].getChildrenVO()[0].getAttributeValue("vbilltype")).equals("5I")))
            {
              continue;
            }

            for (j = 0; j < this.m_OutbillvosResoult[i].getChildrenVO().length; j++) {
              this.m_OutbillvosResoult[i].getChildrenVO()[j].setAttributeValue("voutbilltype", BillTypeConst.m_allocationOut);
            }

            gnrlbillvos[i] = ((GeneralBillVO)PfChangeBO_Client.pfChangeBillToBill(this.m_OutbillvosResoult[i], "7Y", "4Y"));
          }

        }

      }

      this.m_planvos = DeliverydailyplanBO_Client.saveOutDM(gnrlbillvos, getPlanvos());
      closeOK();
    }
    catch (Exception e) {
      reportException(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40140404", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("40140404", "UPP40140404-000078") + e.getMessage());
    }
  }

  public void setCorpID(String newCorpID)
  {
    this.m_sCorpID = newCorpID;
  }

  public void setCorpName(String newCorpName)
  {
    this.m_sCorpName = newCorpName;
  }

  public void setDelivOrgCode(String newDelivOrgCode)
  {
    this.m_sDelivOrgCode = newDelivOrgCode;
  }

  public void setDelivOrgName(String newDelivOrgName)
  {
    this.m_sDelivOrgName = newDelivOrgName;
  }

  public void setDelivOrgPK(String newDelivOrgPK)
  {
    this.m_sDelivOrgPK = newDelivOrgPK;
  }

  public void setHvo(DelivbillHHeaderVO newHvo)
  {
    this.m_delivHvo = newHvo;
  }

  public void setOutbillHVOs(OutbillHVO[] hvos)
  {
    DMDataVO[] items = new DMDataVO[hvos.length];
    this.m_delivbillinvnum = new UFDouble[hvos.length];
    nc.vo.dm.dm104.OutbillHItemVO[] outitems = null;
    for (int i = 0; i < hvos.length; i++) {
      items[i] = ((DMDataVO)hvos[i].getParentVO());
      UFDouble dInvNum = (UFDouble)items[i].getAttributeValue("dinvnum");
      UFDouble dOutNum = (UFDouble)items[i].getAttributeValue("doutnum");
      this.m_delivbillinvnum[i] = dInvNum;
      if ((dInvNum != null) && (dOutNum != null)) {
        double d = dInvNum.doubleValue() - dOutNum.doubleValue();
        items[i].setAttributeValue("dinvnum", new UFDouble(d));
      }

      items[i].setAttributeValue("dtotalout", items[i].getAttributeValue("dinvnum"));
      if (hvos[i].getChildrenVO() != null) {
        ((nc.vo.dm.dm102.OutbillHItemVO)hvos[i].getChildrenVO()[0]).setDoutnum((UFDouble)items[i].getAttributeValue("dinvnum"));
      }

    }

    getBillListPanel().setHeaderValueVO(items);
    getBillListPanel().getHeadBillModel().execLoadFormula();
    this.m_hvos = hvos;
  }

  public void setUserID(String newUserID)
  {
    this.m_sUserID = newUserID;
  }

  public void setUserName(String newUserName)
  {
    this.m_sUserName = newUserName;
  }

  private OutbillVO[] setVOs()
  {
    int i = 0;
    ArrayList arylistPar = new ArrayList();
    ArrayList arylistChi = new ArrayList();
    nc.vo.dm.dm102.OutbillHItemVO[] items = null;
    nc.vo.dm.dm102.OutbillHItemVO[] newitems = null;
    OutbillHVO[] newhvos = null;
    OutbillVO[] resoultvos = null;

    if ((this.m_listrow < this.m_hvos.length) && (this.m_listrow >= 0)) {
      items = (nc.vo.dm.dm102.OutbillHItemVO[])(nc.vo.dm.dm102.OutbillHItemVO[])this.m_hvos[this.m_listrow].getChildrenVO();
      if (items != null) {
        for (i = 0; i < items.length; i++) {
          if (i < getBillListPanel().getBodyBillModel().getRowCount()) {
            if (getBillListPanel().getBodyBillModel().getValueAt(i, "doutnum") != null) {
              items[i].setDoutnum(new UFDouble(getBillListPanel().getBodyBillModel().getValueAt(i, "doutnum").toString()));
            }
            else
              items[i].setDoutnum(null);
          }
        }
      }
    }
    for (i = 0; i < this.m_hvos.length; i++) {
      items = (nc.vo.dm.dm102.OutbillHItemVO[])(nc.vo.dm.dm102.OutbillHItemVO[])this.m_hvos[i].getChildrenVO();
      if ((items != null) && (items.length > 0)) {
        for (int j = 0; j < items.length; j++)
          if ((items[j].getDoutnum() != null) && (items[j].getDoutnum().doubleValue() != 0.0D))
            arylistChi.add(items[j]);
      }
      if (arylistChi.size() > 0) {
        newitems = new nc.vo.dm.dm102.OutbillHItemVO[arylistChi.size()];
        newitems = (nc.vo.dm.dm102.OutbillHItemVO[])(nc.vo.dm.dm102.OutbillHItemVO[])arylistChi.toArray(new nc.vo.dm.dm102.OutbillHItemVO[0]);
        this.m_hvos[i].setChildrenVO(newitems);
        arylistChi.clear();
        arylistPar.add(this.m_hvos[i]);
      }
    }

    if (arylistPar.size() > 0) {
      newhvos = (OutbillHVO[])(OutbillHVO[])arylistPar.toArray(new OutbillHVO[0]);
    }

    if ((newhvos != null) && (newhvos.length > 0)) {
      resoultvos = new OutbillVO[newhvos.length];
      for (i = 0; i < newhvos.length; i++) {
        items = (nc.vo.dm.dm102.OutbillHItemVO[])(nc.vo.dm.dm102.OutbillHItemVO[])newhvos[i].getChildrenVO();
        OutbillHHeaderVO[] resultchildrenvos = new OutbillHHeaderVO[items.length];
        for (int j = 0; j < items.length; j++) {
          OutbillHHeaderVO childrenvo = (OutbillHHeaderVO)newhvos[i].getParentVO().clone();
          childrenvo.setAttributeValue("pkstroe", items[j].getAttributeValue("pkstroe"));
          childrenvo.setAttributeValue("vstorename", items[j].getAttributeValue("vstorename"));
          childrenvo.setAttributeValue("donhandnum", items[j].getAttributeValue("donhandnum"));
          childrenvo.setAttributeValue("doutnum", items[j].getAttributeValue("doutnum"));
          childrenvo.setAttributeValue("pkwhmanager", items[j].getAttributeValue("pkwhmanager"));
          childrenvo.setAttributeValue("pkinvsort", items[j].getAttributeValue("pkinvsort"));
          resultchildrenvos[j] = childrenvo;
        }
        resoultvos[i] = new OutbillVO();
        resoultvos[i].setChildrenVO(resultchildrenvos);
      }
    }

    UFDouble nNum = null;
    UFDouble nAssistNum = null;
    UFDouble nExchange = null;
    if ((null != resoultvos) && (resoultvos.length > 0)) {
      for (i = 0; i < resoultvos.length; i++) {
        OutbillHHeaderVO[] childrenvos = (OutbillHHeaderVO[])(OutbillHHeaderVO[])resoultvos[i].getChildrenVO();
        for (int j = 0; j < childrenvos.length; j++) {
          nNum = (UFDouble)childrenvos[j].getAttributeValue("dnum");
          nAssistNum = (UFDouble)childrenvos[j].getAttributeValue("dassistnum");
          if ((nNum != null) && (nAssistNum != null) && (nAssistNum.doubleValue() != 0.0D)) {
            nExchange = new UFDouble(nNum.doubleValue() / nAssistNum.doubleValue());
            if ((nExchange != null) && (nExchange.doubleValue() != 0.0D)) {
              double d = ((UFDouble)childrenvos[j].getAttributeValue("doutnum")).doubleValue() / nExchange.doubleValue();

              childrenvos[j].setAttributeValue("dassistnum", new UFDouble(d));
            }

          }

        }

      }

    }

    return resoultvos;
  }

  public void valueChanged(ListSelectionEvent e)
  {
    try
    {
      if (!e.getValueIsAdjusting())
      {
        nc.vo.dm.dm102.OutbillHItemVO[] items = null;
        DelivbillHItemVO dlvbillitem = null;

        if ((this.m_listrow < this.m_hvos.length) && (this.m_listrow >= 0)) {
          items = (nc.vo.dm.dm102.OutbillHItemVO[])(nc.vo.dm.dm102.OutbillHItemVO[])this.m_hvos[this.m_listrow].getChildrenVO();
          if (items != null) {
            for (int i = 0; i < items.length; i++) {
              if (i < getBillListPanel().getBodyBillModel().getRowCount())
                if (getBillListPanel().getBodyBillModel().getValueAt(i, "doutnum") != null) {
                  items[i].setDoutnum(new UFDouble(getBillListPanel().getBodyBillModel().getValueAt(i, "doutnum").toString()));
                }
                else
                  items[i].setDoutnum(null);
            }
          }
        }
        this.m_listrow = getBillListPanel().getHeadTable().getSelectedRow();

        if ((this.m_listrow < this.m_hvos.length) && (this.m_listrow >= 0)) {
          items = (nc.vo.dm.dm102.OutbillHItemVO[])(nc.vo.dm.dm102.OutbillHItemVO[])this.m_hvos[this.m_listrow].getChildrenVO();
        }

        getBillListPanel().getBodyBillModel().clearTotalModel();
        getBillListPanel().setBodyValueVO(items);
      }
      else if ((this.m_listrow < this.m_hvos.length) && (this.m_listrow >= 0)) {
        this.m_handlingAnotherHeadRow = true;
      }
    }
    catch (Exception e1)
    {
      reportException(e1);
      handleException(e1);
    }
  }

  public DMDataVO[] getPlanvos()
  {
    return this.m_planvos;
  }

  public void setPlanvos(DMDataVO[] planvos)
  {
    this.m_planvos = planvos;
  }

  class IvjEventHandler
    implements ActionListener
  {
    IvjEventHandler()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
      if (e.getSource() == OutBillDlg.this.getUIButtonOk())
        OutBillDlg.this.onOk(e);
      if (e.getSource() == OutBillDlg.this.getUIButtonCancel())
        OutBillDlg.this.onCancel(e);
    }
  }
}