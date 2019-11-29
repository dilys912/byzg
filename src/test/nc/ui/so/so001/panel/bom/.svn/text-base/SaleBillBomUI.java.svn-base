package nc.ui.so.so001.panel.bom;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.so.so001.SaleOrderBO_Client;
import nc.ui.so.so001.panel.card.BillTreeCardPanel;
import nc.ui.so.so001.panel.card.SOBillCardPanel;
import nc.ui.so.so001.panel.card.SOBillCardTools;
import nc.ui.so.so001.panel.list.SOBillListPanel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;
import nc.vo.so.so001.BomorderHeaderVO;
import nc.vo.so.so001.BomorderItemVO;
import nc.vo.so.so001.BomorderVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.sp.service.PriceAskResultVO;
import nc.vo.sp.service.SalePriceVO;

public abstract class SaleBillBomUI extends ToftPanel
  implements TreeSelectionListener, BillEditListener, BillEditListener2
{
  protected DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000130"));

  public Hashtable htInv = null;

  private BillTreeCardPanel ivjBillTreeCardPanel = null;

  public UFBoolean SA_15 = null;

  public Integer BD501 = null;
  public String strState;
  public boolean m_isCodeChanged = false;

  public ArrayList alInvsBom = new ArrayList();

  protected String invID = null;

  protected String strBomState = "ADD";

  protected String strOldState = null;

  protected String bomID = null;

  protected int orderrow = -1;

  protected boolean addData = true;

  protected ButtonObject boBomSave = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000008"), 3, "保存");

  protected ButtonObject boBomEdit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000045"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000291"), 2, "修改");

  protected ButtonObject boBomCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("40060301", "UPT40060301-000027"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000154"), 0, "放弃");

  protected ButtonObject boBomAction = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000026"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000323"), "执行");

  protected ButtonObject boBomAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000027"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000238"), "审批");

  protected ButtonObject boBomPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000169"), "打印");

  protected ButtonObject boBomCancelAudit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000028"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000214"), "弃审");

  protected ButtonObject boBomReturn = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000097"), "返回");

  public BillTreeCardPanel getBillTreeCardPanel()
  {
    if (this.ivjBillTreeCardPanel == null) {
      try {
        this.ivjBillTreeCardPanel = new BillTreeCardPanel();
        this.ivjBillTreeCardPanel.setName("BillTreeCardPanel");
        BillData bdData = new BillData(getBillTreeCardPanel().getTempletData("so0000000000bomorder"));

        getBillCardPanel().getFreeItemRefPane().setMaxLength(1000);

        bdData.getBodyItem("vfree0").setComponent(getBillCardPanel().getFreeItemRefPane());

        if (this.SA_15.booleanValue())
          bdData.getBodyItem("nprice").setEdit(false);
        else {
          bdData.getBodyItem("nprice").setEdit(true);
        }

        getBillTreeCardPanel().setBillData(bdData);

        String[] aryNum = { "nnumber", "slsx", "slxx" };
        for (int i = 0; i < aryNum.length; i++) {
          if (this.BD501 != null) {
            this.ivjBillTreeCardPanel.getBodyItem(aryNum[i]).setDecimalDigits(this.BD501.intValue());
          }
        }

        this.ivjBillTreeCardPanel.getCustTree().setModel(new DefaultTreeModel(this.root));

        this.ivjBillTreeCardPanel.getCustTree().setEnabled(false);
        this.ivjBillTreeCardPanel.getCustTree().setEditable(false);
        ((DefaultTreeSelectionModel)this.ivjBillTreeCardPanel.getCustTree().getSelectionModel()).setSelectionMode(1);

        this.ivjBillTreeCardPanel.getCustTree().addTreeSelectionListener(this);

        this.ivjBillTreeCardPanel.addEditListener(this);

        this.ivjBillTreeCardPanel.addBodyEditListener2(this);
      } catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return this.ivjBillTreeCardPanel;
  }

  void setBodyFreeValueBom(int row, InvVO voInv)
  {
    if (voInv != null) {
      voInv.setFreeItemValue("vfree1", (String)getBillTreeCardPanel().getBodyValueAt(row, "vfree1"));

      voInv.setFreeItemValue("vfree2", (String)getBillTreeCardPanel().getBodyValueAt(row, "vfree2"));

      voInv.setFreeItemValue("vfree3", (String)getBillTreeCardPanel().getBodyValueAt(row, "vfree3"));

      voInv.setFreeItemValue("vfree4", (String)getBillTreeCardPanel().getBodyValueAt(row, "vfree4"));

      voInv.setFreeItemValue("vfree5", (String)getBillTreeCardPanel().getBodyValueAt(row, "vfree5"));
    }
  }

  public void initBomTree(String invID, String invName, String invManID, String sfTzj)
  {
    this.root.removeAllChildren();
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(invName);
    this.root.add(node);
    this.htInv = new Hashtable();
    String[] curInvArray = { invID, invName, invManID, sfTzj };
    this.htInv.put(invName, curInvArray);
    getBomTree(node, invID);
    getBillTreeCardPanel().getCustTree().removeTreeSelectionListener(this);
    getBillTreeCardPanel().getCustTree().setModel(new DefaultTreeModel(this.root));

    getBillTreeCardPanel().getCustTree().addTreeSelectionListener(this);
  }

  public void getBomTree(DefaultMutableTreeNode node, String invbaseID)
  {
    try
    {
      String[][] results = SaleOrderBO_Client.getBomInfo(getCorpPrimaryKey(), invbaseID, getClientEnvironment().getDate().toString());

      if (results != null)
        for (int i = 0; i < results.length; i++) {
          DefaultMutableTreeNode child = new DefaultMutableTreeNode(results[i][1].trim());

          node.add(child);

          if (this.htInv.containsKey(results[i][1].trim())) {
            this.htInv.remove(results[i][1].trim());
          }
          this.htInv.put(results[i][1].trim(), results[i]);
          getBomTree(child, results[i][0].trim());
        }
    }
    catch (Exception e1)
    {
      SCMEnv.out("查询失败！");
    }
  }

  protected void setBomPrice()
  {
    for (int i = 0; i < getBillTreeCardPanel().getRowCount(); i++)
      if (this.SA_15.booleanValue())
        getBillTreeCardPanel().setCellEditable(i, "nprice", false);
      else
        getBillTreeCardPanel().setCellEditable(i, "nprice", true);
  }

  public void valueChanged(TreeSelectionEvent e)
  {
    Object o = e.getPath().getLastPathComponent();
    if (o.toString().trim().equals(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000130")))
    {
      getBillTreeCardPanel().getCustTree().setEditable(false);
      setBomPrice();
      return;
    }
    String wlname = o.toString();

    this.invID = ((String[])(String[])this.htInv.get(wlname))[0].toString();
    getBillTreeCardPanel().getBillModel().clearBodyData();
    String[][] childinfo = (String[][])null;
    try
    {
      Object oParent = e.getPath().getParentPath().getLastPathComponent();
      boolean needCheckParent = false;
      String idParent = null;
      if (oParent.toString().trim().equals(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000130")))
      {
        needCheckParent = false;
        idParent = null;
      } else {
        needCheckParent = true;
        idParent = ((String[])(String[])this.htInv.get(oParent.toString()))[0].toString();
      }

      if (this.strBomState.equals("ADD")) {
        getBillTreeCardPanel().getCustTree().setEnabled(false);
        if (this.bomID != null) {
          BomorderItemVO[] iteminfo = null;
          iteminfo = BomorderBO_Client.findItemsForWlbm(this.bomID, this.invID, idParent, needCheckParent);

          if ((iteminfo != null) && (iteminfo.length != 0)) {
            setBomBodyData(iteminfo);
            showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000139"));

            this.boBomSave.setEnabled(false);
            updateButton(this.boBomSave);
            getBillTreeCardPanel().getCustTree().setEnabled(true);
            getBillTreeCardPanel().setEnabled(false);
          } else {
            childinfo = SaleOrderBO_Client.getBomChildInfo(getCorpPrimaryKey(), this.invID);

            if ((childinfo == null) || (childinfo.length == 0)) {
              showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000140"));

              getBillTreeCardPanel().getCustTree().setEnabled(true);

              getBillTreeCardPanel().getCustTree().setEditable(false);

              setBomPrice();
              this.strBomState = "EDIT";
              return;
            }
            setBomBody(childinfo);
            this.boBomSave.setEnabled(true);
            updateButton(this.boBomSave);
            getBillTreeCardPanel().getCustTree().setEnabled(false);

            getBillTreeCardPanel().setEnabled(true);
          }
        }
        else {
          childinfo = SaleOrderBO_Client.getBomChildInfo(getCorpPrimaryKey(), this.invID);

          if ((childinfo == null) || (childinfo.length == 0)) {
            showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000140"));

            getBillTreeCardPanel().getCustTree().setEnabled(true);
            getBillTreeCardPanel().getCustTree().setEditable(false);
            setBomPrice();
            return;
          }
          setBomBody(childinfo);
          this.boBomSave.setEnabled(true);
          updateButton(this.boBomSave);
          getBillTreeCardPanel().getCustTree().setEnabled(false);
          getBillTreeCardPanel().setEnabled(true);
        }
      }
      else if (this.strBomState.equals("EDIT")) {
        getBillTreeCardPanel().getCustTree().setEnabled(false);
        BomorderItemVO[] iteminfo = BomorderBO_Client.findItemsForWlbm(this.bomID, this.invID, idParent, needCheckParent);

        if ((iteminfo != null) && (iteminfo.length != 0)) {
          setBomBodyData(iteminfo);
        } else {
          int isInsert = showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000141"));

          if (isInsert == 4) {
            childinfo = SaleOrderBO_Client.getBomChildInfo(getCorpPrimaryKey(), this.invID);

            if ((childinfo == null) || (childinfo.length == 0)) {
              showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000140"));

              getBillTreeCardPanel().getCustTree().setEnabled(true);

              getBillTreeCardPanel().getCustTree().setEditable(false);

              setBomPrice();
              return;
            }
            setBomBody(childinfo);
            this.addData = true;
          }
          else {
            getBillTreeCardPanel().getCustTree().setEnabled(true);
          }
        }
      } else {
        getBillTreeCardPanel().getCustTree().setEditable(true);
        if (this.bomID == null) {
          getBillTreeCardPanel().getCustTree().setEditable(false);
          setBomPrice();
          return;
        }
        BomorderItemVO[] iteminfo = BomorderBO_Client.findItemsForWlbm(this.bomID, this.invID, idParent, needCheckParent);

        if ((iteminfo != null) && (iteminfo.length != 0)) {
          setBomBodyData(iteminfo);
          getBillTreeCardPanel().setEnabled(false);
          this.addData = false;

          if ((this.strOldState.equals("新增")) || (this.strOldState.equals("修改"))) {
            this.boBomEdit.setEnabled(true);
            updateButtons();
          }

        }
        else if ((this.strOldState.equals("新增")) || (this.strOldState.equals("修改"))) {
          int isInsert = showYesNoMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000141"));

          if (isInsert == 4) {
            childinfo = SaleOrderBO_Client.getBomChildInfo(getCorpPrimaryKey(), this.invID);

            if ((childinfo == null) || (childinfo.length == 0)) {
              showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000140"));

              getBillTreeCardPanel().getCustTree().setEnabled(true);

              getBillTreeCardPanel().getCustTree().setEditable(false);

              setBomPrice();
              return;
            }
            setBomBody(childinfo);
            this.boBomSave.setEnabled(true);
            this.boBomEdit.setEnabled(false);
            this.boBomCancel.setEnabled(true);
            updateButtons();
            getBillTreeCardPanel().setEnabled(true);
            getBillTreeCardPanel().getCustTree().setEnabled(false);

            this.addData = true;
            this.strBomState = "EDIT";
            setPzdRowSelected();
          }
          else {
            getBillTreeCardPanel().getCustTree().setEnabled(true);
          }
        }
      }
    }
    catch (BusinessException be)
    {
      showErrorMessage(be.getMessage());
    } catch (Exception e1) {
      SCMEnv.out(e);
    }

    getBillTreeCardPanel().getCustTree().setEditable(false);
    setBomPrice();
    getBillTreeCardPanel().updateValue();
  }

  public void afterEdit(BillEditEvent e) {
    this.ivjBillTreeCardPanel.getBillModel().setNeedCalculate(false);
    try
    {
      if ((e.getPos() == 0) && 
        (e.getKey().equals("vreceiptcode"))) {
        this.m_isCodeChanged = true;
      }

      if (e.getKey().equals("bselect")) {
        Object tempo = this.ivjBillTreeCardPanel.getCustTree().getSelectionPath().getLastPathComponent();

        if ((tempo != null) && 
          (this.htInv.containsKey(tempo.toString())))
        {
          String temps = ((String[])(String[])this.htInv.get(tempo.toString()))[3];
          boolean isTzj = temps != null;

          if (isTzj)
          {
            for (int i = 0; i < this.ivjBillTreeCardPanel.getBillModel().getRowCount(); )
            {
              this.ivjBillTreeCardPanel.setBodyValueAt(new Boolean(false), i, "bselect");

              i++;
            }

            this.ivjBillTreeCardPanel.setBodyValueAt(new Boolean(true), e.getRow(), "bselect");
          }

        }

      }
      else if (e.getKey().equals("nnumber")) {
        UFDouble dFindPrice = findBomPrice(this.orderrow, e.getRow());
        if (dFindPrice != null) {
          this.ivjBillTreeCardPanel.setBodyValueAt(findBomPrice(this.orderrow, e.getRow()), e.getRow(), "nprice");
        }

      }
      else if (e.getKey().equals("vfree0")) {
        getBillCardPanel().afterFreeItemEditBom(e);
        this.ivjBillTreeCardPanel.setBodyValueAt(findBomPrice(this.orderrow, e.getRow()), e.getRow(), "nprice");
      }
    }
    catch (Exception ee) {
      SCMEnv.out(ee);
    }
  }

  public void bodyRowChange(BillEditEvent e) {
  }

  public boolean beforeEdit(BillEditEvent e) {
    boolean bret = true;

    if ((this.strState.equals("BOM")) && 
      (e.getKey().equals("vfree0"))) {
      try
      {
        this.ivjBillTreeCardPanel.stopEditing();
        InvVO voInv = null;
        if (this.alInvsBom.size() > e.getRow())
          voInv = (InvVO)this.alInvsBom.get(e.getRow());
        setBodyFreeValueBom(e.getRow(), voInv);
        getBillCardPanel().getFreeItemRefPane().setFreeItemParam(voInv);
      }
      catch (Exception ex) {
        SCMEnv.out("自由项设置失败!");
      }

    }

    return bret;
  }

  protected void loadBomCurrData() {
    try {
      BomorderVO bomorder = BomorderBO_Client.findByPrimaryKey(this.bomID);
      getBillTreeCardPanel().setBillValueVO(bomorder);
      long s1 = System.currentTimeMillis();
      getBillTreeCardPanel().getBillModel().execLoadFormula();
      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      initFreeItemBom();
      String currID = (String)getBillCardPanel().getHeadItem("ccurrencytypeid").getValueObject();

      getBillTreeCardPanel().setHeadItem("ccurrencytypeid", currID);

      getBillTreeCardPanel().updateValue();

      setPzdRowSelected();
      SCMEnv.out("数据加载成功！");
    } catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      SCMEnv.out("数据加载失败！");
    }
  }

  protected void loadBomData(int row)
  {
    this.bomID = ((String)getBillCardPanel().getBodyValueAt(row, "cbomorderid"));
    try {
      BomorderVO bomorder = BomorderBO_Client.findByPrimaryKey(this.bomID);
      getBillTreeCardPanel().setBillValueVO(bomorder);
      long s1 = System.currentTimeMillis();
      getBillTreeCardPanel().getBillModel().execLoadFormula();

      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      initFreeItemBom();

      getBillTreeCardPanel().updateValue();
      SCMEnv.out("数据加载成功！");
    } catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      SCMEnv.out("数据加载失败！");
    }
  }

  public void setBomBodyData(BomorderItemVO[] iteminfo)
  {
    getBillTreeCardPanel().getBillModel().setBodyDataVO(iteminfo);
    long s1 = System.currentTimeMillis();
    getBillTreeCardPanel().getBillModel().execLoadFormula();
    System.out.println("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

    initFreeItemBom();
  }

  private void initFreeItemBom()
  {
    this.alInvsBom = new ArrayList();
    try {
      Object[] o = new Object[getBillTreeCardPanel().getRowCount()];
      for (int i = 0; i < getBillTreeCardPanel().getRowCount(); i++) {
        String sTempID1 = (String)getBillTreeCardPanel().getBodyValueAt(i, "pk_produce_b");

        String sTempID2 = null;
        ArrayList alIDs = new ArrayList();
        alIDs.add(sTempID2);
        alIDs.add(sTempID1);
        alIDs.add(getClientEnvironment().getUser().getPrimaryKey());
        alIDs.add(getCorpPrimaryKey());
        o[i] = alIDs;
      }
      this.alInvsBom = SaleOrderBO_Client.queryInfo(new Integer(0), o);
      if (this.alInvsBom != null) {
        for (int i = 0; i < getBillTreeCardPanel().getRowCount(); i++) {
          InvVO voInv = (InvVO)this.alInvsBom.get(i);
          setBodyFreeValueBom(i, voInv);
          getBillTreeCardPanel().setBodyValueAt(voInv.getFreeItemVO().getWholeFreeItem(), i, "vfree0");
        }
      }
    }
    catch (Exception ex)
    {
      SCMEnv.out("配置单自由项设置失败!");
    }
  }

  public void setBomBody(String[][] childinfo)
  {
    try {
      Object tempo = getBillTreeCardPanel().getCustTree().getSelectionPath().getLastPathComponent();

      boolean isTzj = false;
      if ((tempo != null) && 
        (this.htInv.containsKey(tempo.toString())))
      {
        String temps = ((String[])(String[])this.htInv.get(tempo.toString()))[3];
        isTzj = temps != null;
      }

      for (int i = 0; i < childinfo.length; i++)
      {
        getBillTreeCardPanel().addLine();
        getBillTreeCardPanel().setBodyValueAt(this.invID, i, "wlbmid");
        getBillTreeCardPanel().setBodyValueAt(childinfo[i][0], i, "wlbm_bid");

        getBillTreeCardPanel().setBodyValueAt(childinfo[i][8], i, "pk_produce_b");

        if ((childinfo[i][1] != null) && (childinfo[i][1].equals("Y"))) {
          getBillTreeCardPanel().setBodyValueAt(childinfo[i][2], i, "slsx");

          getBillTreeCardPanel().setBodyValueAt(childinfo[i][3], i, "slxx");
        }

        getBillTreeCardPanel().setBodyValueAt(childinfo[i][4], i, "nnumber");

        getBillTreeCardPanel().setBodyValueAt(childinfo[i][5], i, "jldwid");

        getBillTreeCardPanel().setBodyValueAt(childinfo[i][7], i, "vrownote");

        getBillTreeCardPanel().setBodyValueAt(childinfo[i][9], i, "bselect");

        if (!isTzj) {
          if ("Y".equals(childinfo[i][9])) {
            getBillTreeCardPanel().setCellEditable(i, "bselect", false);
          }
        }
        else {
          getBillTreeCardPanel().setCellEditable(i, "bselect", true);
        }

      }

      long s1 = System.currentTimeMillis();
      getBillTreeCardPanel().getBillModel().execLoadFormula();
      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      initFreeItemBom();
      for (int i = 0; i < childinfo.length; i++)
      {
        getBillTreeCardPanel().setBodyValueAt(findBomPrice(this.orderrow, i), i, "nprice");
      }
    }
    catch (Exception e1)
    {
      SCMEnv.out(e1);
    }
  }

  protected AggregatedValueObject getBomVO()
  {
    BomorderVO bomorder = null;
    bomorder = (BomorderVO)getBillTreeCardPanel().getBillValueVO(BomorderVO.class.getName(), BomorderHeaderVO.class.getName(), BomorderItemVO.class.getName());

    ((BomorderHeaderVO)bomorder.getParentVO()).setPk_corp(getCorpPrimaryKey());

    ((BomorderHeaderVO)bomorder.getParentVO()).setCapproveid(getClientEnvironment().getUser().getPrimaryKey());

    return bomorder;
  }

  public UFDouble findBomPrice(int row, int bomrow)
  {
    if (this.SA_15.booleanValue()) {
      UFDouble saleprice = null;
      try
      {
        getBillCardTools().getCustInfo();

        SalePriceVO salepriceVO = new SalePriceVO();

        salepriceVO.setCropID(getCorpPrimaryKey());

        salepriceVO.setBizTypeID(getBillCardPanel().getBusiType());

        String strCustomerID = getBillCardPanel().getHeadItem("ccustomerid").getValue();

        salepriceVO.setCustomerID(strCustomerID);

        salepriceVO.setCustomerBaseID(getBillCardTools().getHeadStringValue("ccustbasid"));

        String strCurrencyID = getBillCardPanel().getHeadItem("ccurrencytypeid").getValue();

        salepriceVO.setCurrencyID(strCurrencyID);

        String strInventoryID = (String)getBillTreeCardPanel().getBodyValueAt(bomrow, "pk_produce_b");

        salepriceVO.setInventoryID(strInventoryID);

        salepriceVO.setSystemData(getClientEnvironment().getDate());

        UFDouble number = new UFDouble(getBillTreeCardPanel().getBodyValueAt(bomrow, "nnumber").toString());

        String cunitid = (String)getBillTreeCardPanel().getBodyValueAt(bomrow, "jldwid");

        salepriceVO.setMeasdocid(cunitid);
        if (number.getDouble() <= 0.0D)
          return null;
        salepriceVO.setNumber(number);

        String wlbm_bid = getBillTreeCardPanel().getBodyValueAt(bomrow, "wlbm_bid").toString();

        salepriceVO.setInventoryBaseID(wlbm_bid);

        String sFreeValue = (String)getBillTreeCardPanel().getBodyValueAt(bomrow, "vfree1");

        salepriceVO.setFree1(sFreeValue);
        sFreeValue = (String)getBillTreeCardPanel().getBodyValueAt(bomrow, "vfree2");

        salepriceVO.setFree2(sFreeValue);
        sFreeValue = (String)getBillTreeCardPanel().getBodyValueAt(bomrow, "vfree3");

        salepriceVO.setFree3(sFreeValue);
        sFreeValue = (String)getBillTreeCardPanel().getBodyValueAt(bomrow, "vfree4");

        salepriceVO.setFree4(sFreeValue);
        sFreeValue = (String)getBillTreeCardPanel().getBodyValueAt(bomrow, "vfree5");

        salepriceVO.setFree5(sFreeValue);

        salepriceVO.setDeptid(getBillCardTools().getHeadStringValue("cdeptid"));

        salepriceVO.setSaleStrucid(getBillCardTools().getHeadStringValue("csalecorpid"));

        if (getBillCardPanel().getBillTable().getSelectedRow() >= 0) {
          salepriceVO.setReceiptAreaid(getBillCardTools().getBodyStringValue(getBillCardPanel().getBillTable().getSelectedRow(), "creceiptareaid"));
        }

        String pk_invmandoc = BillTools.getColValue2("bd_invmandoc", "pk_invmandoc", "pk_corp", getCorpPrimaryKey(), "pk_invbasdoc", wlbm_bid);

        if (!SoVoTools.isEmptyString(pk_invmandoc)) {
          salepriceVO.setInventoryID(pk_invmandoc);
        }

        long s = System.currentTimeMillis();
        PriceAskResultVO resultvo = SOBillCardTools.findPrice(salepriceVO);

        if ((resultvo != null) && (resultvo.getErrMessage() != null) && (resultvo.getErrMessage().trim().length() > 0))
        {
          showWarningMessage(resultvo.getErrMessage());
        } else if (resultvo == null) {
          showWarningMessage(NCLangRes.getInstance().getStrByID("40060301", "UPP40060301-000131"));
        }

        if (resultvo != null) {
          saleprice = resultvo.getNum();
        }
        long s1 = System.currentTimeMillis();

        SCMEnv.out("取价时间：" + (s1 - s) + "毫秒");
      } catch (Exception e) {
        showErrorMessage(e.getMessage());
      }

      return saleprice;
    }
    for (int i = 0; i < getBillTreeCardPanel().getRowCount(); i++)
      getBillTreeCardPanel().setCellEditable(i, "nprice", true);
    return null;
  }

  protected void setPzdRowSelected()
  {
    Object tempo = getBillTreeCardPanel().getCustTree().getSelectionPath().getLastPathComponent();
    int i;
    if ((tempo != null) && 
      (this.htInv.containsKey(tempo.toString())))
    {
      String temps = ((String[])(String[])this.htInv.get(tempo.toString()))[3];
      boolean isTzj = temps != null;

      if (!isTzj)
        for (i = 0; i < getBillTreeCardPanel().getBillModel().getRowCount(); )
        {
          tempo = getBillTreeCardPanel().getBodyValueAt(i, "bselect");

          boolean isSelected = tempo != null;

          if (isSelected) {
            getBillTreeCardPanel().setCellEditable(i, "bselect", false);
          }
          else
            getBillTreeCardPanel().setCellEditable(i, "bselect", true);
          i++;
        }
    }
  }

  protected void handleException(Throwable exception)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    SCMEnv.out(exception);
    exception.printStackTrace();
  }

  public abstract SOBillCardPanel getBillCardPanel();

  public abstract SOBillCardTools getBillCardTools();

  public abstract SOBillListPanel getBillListPanel();
}