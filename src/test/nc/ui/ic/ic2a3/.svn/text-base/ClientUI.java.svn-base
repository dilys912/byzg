package nc.ui.ic.ic2a3;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.bill.query.ICheckCondition;
import nc.ui.ic.pub.bill.query.QueryConditionDlg;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.VOTableModel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic2a3.AccountctrlHeaderVO;
import nc.vo.ic.ic2a3.AccountctrlItemVO;
import nc.vo.ic.ic2a3.AccountctrlVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class ClientUI extends ToftPanel
  implements ListSelectionListener, BillEditListener, ICheckCondition
{
  private ButtonObject m_boClose = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT400829-000004"), NCLangRes.getInstance().getStrByID("4008spec", "UPT400829-000004"), 0, "关账");
  private ButtonObject m_boOpen = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT400829-000005"), NCLangRes.getInstance().getStrByID("4008spec", "UPT400829-000005"), 0, "开账");
  private ButtonObject m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 0, "查询");
  private ButtonObject m_boDetail = new ButtonObject(NCLangRes.getInstance().getStrByID("4008spec", "UPT400829-000006"), NCLangRes.getInstance().getStrByID("4008spec", "UPT400829-000006"), 0, "明细");
  private ButtonObject m_boFresh = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), 0, "刷新");
  private ButtonObject[] m_boArray = { this.m_boClose, this.m_boOpen, this.m_boQuery, this.m_boDetail, this.m_boFresh };

  private BillCardPanel m_billCardPanel = null;

  private BillData m_billData = null;

  String m_sPkCorp = null;

  String m_sPkUser = null;

  String m_sUseName = null;

  UFDate m_dDate = null;

  ConfirmTimeDlg m_confirmTimeDlg = null;

  AccCtrlDatailDlg m_accCtrlDetailDlg = null;

  QueryConditionDlg m_QryConditionDlg = null;

  String m_sNodeCode = "400829";

  public ClientUI()
  {
    init();
  }

  public void afterEdit(BillEditEvent e)
  {
    String sItemKey = e.getKey();
    String sPkCalBody = getBillCardPanel().getHeadItem("calbodyname").getValue();
    String sPkWh = getBillCardPanel().getHeadItem("storname").getValue();
    if (sItemKey.equals("calbodyname"))
    {
      if (sPkCalBody != null)
      {
        String[] sConstraint = null;

        sConstraint = new String[1];
        sConstraint[0] = (" AND pk_calbody='" + sPkCalBody + "'");
        BillItem bi = getBillCardPanel().getHeadItem("storname");
        RefFilter.filtWh(bi, this.m_sPkCorp, sConstraint);

        bi.setValue(null);
      }
    }

    if (((sPkCalBody == null) || (sPkCalBody.trim().length() == 0)) && ((sPkWh == null) || (sPkWh.trim().length() == 0)))
    {
      BillItem bi = getBillCardPanel().getHeadItem("storname");
      RefFilter.filtWh(bi, this.m_sPkCorp, null);

      getBillCardPanel().getBillModel().clearBodyData();
      showHintMessage("");
    }
    else {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000412"));

      onFresh();
    }
  }

  public void bodyRowChange(BillEditEvent event)
  {
  }

  private AccCtrlDatailDlg getAccCtrlDetailDlg()
  {
    try
    {
      if (this.m_accCtrlDetailDlg == null)
      {
        this.m_accCtrlDetailDlg = new AccCtrlDatailDlg(this);
      }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }

    return this.m_accCtrlDetailDlg;
  }

  private BillCardPanel getBillCardPanel()
  {
    try
    {
      if (this.m_billCardPanel == null)
      {
        this.m_billCardPanel = new BillCardPanel();
        this.m_billCardPanel.setBillData(this.m_billData);
        this.m_billCardPanel.getBillTable().setRowSelectionAllowed(true);
        this.m_billCardPanel.getBillTable().setSelectionMode(0);
        this.m_billCardPanel.setBodyMenuShow(false);
      }

    }
    catch (Exception e)
    {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000413"));
      SCMEnv.error(e);
    }
    return this.m_billCardPanel;
  }

  public String getCalBodyPK()
  {
    String sPkCalBody = null;
    if (getBillCardPanel().getHeadItem("calbodyname") != null)
      sPkCalBody = getBillCardPanel().getHeadItem("calbodyname").getValue();
    return sPkCalBody;
  }

  private ConfirmTimeDlg getConfirmTimeDlg()
  {
    try
    {
      if (this.m_confirmTimeDlg == null)
      {
        this.m_confirmTimeDlg = new ConfirmTimeDlg(this);
      }

    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }

    return this.m_confirmTimeDlg;
  }

  private void getEnvirment()
  {
    ClientEnvironment env = getClientEnvironment();
    try
    {
      this.m_sPkCorp = env.getCorporation().getPrimaryKey();
      this.m_sPkUser = env.getUser().getPrimaryKey();
      this.m_sUseName = env.getUser().getUserName();
      this.m_dDate = env.getDate();
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
  }

  public String checkICCondition(ConditionVO[] conditions)
  {
    try
    {
      getQueryConditionDlg().checkOncetime(conditions, new String[] { "pk_corp" });

      getQueryConditionDlg().checkOneNotOther(conditions, new String[] { "pk_calbody", "cwarehouseid" }, true);
    }
    catch (BusinessException be)
    {
      return be.getMessage();
    }

    return null;
  }

  private QueryConditionDlg getQueryConditionDlg() {
    if (this.m_QryConditionDlg == null)
    {
      this.m_QryConditionDlg = new QueryConditionDlg(this);
      this.m_QryConditionDlg.setTempletID(this.m_sPkCorp, this.m_sNodeCode, this.m_sPkUser, null);

      this.m_QryConditionDlg.setCombox("faccountflag", new String[][] { { "%", NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000414") }, { "Y", NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000410") }, { "N", NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000411") } });

      this.m_QryConditionDlg.setDefaultValue("pk_corp", this.m_sPkCorp, null);
      this.m_QryConditionDlg.setConditionEditable("pk_corp", false);

      ConditionVO[] conditions = this.m_QryConditionDlg.getConditionVO();
      this.m_QryConditionDlg.addICheckCondition(this);
    }

    return this.m_QryConditionDlg;
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000415");
  }

  public String getWhPK()
  {
    String sWhPk = null;
    if (getBillCardPanel().getHeadItem("storname") != null)
      sWhPk = getBillCardPanel().getHeadItem("storname").getValue();
    return sWhPk;
  }

  private void init()
  {
    setButtons(this.m_boArray);
    setButtonState();

    getEnvirment();

    initBillData();
    initListener();
    add(getBillCardPanel(), "Center");
  }

  private void initBillData()
  {
    String[] sHeaderItemKeys = { "calbodyname", "storname" };

    String[] sBodyItemKeys = { "pk_accountctrl", "tstarttime", "tendtime", "faccountflag", "voperatorname", "ts" };

    String[] sHeaderItemNames = { NCLangRes.getInstance().getStrByID("common", "UC000-0001825"), NCLangRes.getInstance().getStrByID("common", "UC000-0000153") };

    String[] sBodyItemNames = { NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000416"), NCLangRes.getInstance().getStrByID("4008spec", "UPT400829-000001"), NCLangRes.getInstance().getStrByID("4008spec", "UPT400829-000002"), NCLangRes.getInstance().getStrByID("4008spec", "UPT400829-000003"), NCLangRes.getInstance().getStrByID("common", "UC000-0002188"), NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000409") };

    BillItem[] HeaderItem = new BillItem[sHeaderItemKeys.length];
    BillItem[] BodyItem = new BillItem[sBodyItemKeys.length];

    HeaderItem[0] = new BillItem();
    HeaderItem[0].setKey(sHeaderItemKeys[0]);
    HeaderItem[0].setName(sHeaderItemNames[0]);
    HeaderItem[0].setDataType(0);
    HeaderItem[0].setPos(0);
    HeaderItem[0].setShowOrder(0);
    HeaderItem[0].setShow(true);
    HeaderItem[0].setEdit(true);
    HeaderItem[0].setDataType(5);
    HeaderItem[0].setRefType("库存组织");
    ((UIRefPane)HeaderItem[0].getComponent()).getRefModel().setUseDataPower(true);

    HeaderItem[1] = new BillItem();
    HeaderItem[1].setKey(sHeaderItemKeys[1]);
    HeaderItem[1].setName(sHeaderItemNames[1]);
    HeaderItem[1].setDataType(0);
    HeaderItem[1].setPos(0);
    HeaderItem[1].setShowOrder(1);
    HeaderItem[1].setShow(true);
    HeaderItem[1].setEdit(true);
    HeaderItem[1].setDataType(5);
    HeaderItem[1].setRefType("仓库档案");

    for (int i = 0; i < BodyItem.length; i++)
    {
      BodyItem[i] = new BillItem();
      BodyItem[i].setKey(sBodyItemKeys[i]);
      BodyItem[i].setName(sBodyItemNames[i]);
      BodyItem[i].setDataType(0);
      BodyItem[i].setPos(1);
      BodyItem[i].setShowOrder(i);
      BodyItem[i].setEdit(false);
      BodyItem[i].setWidth(140);
    }
    BodyItem[0].setShow(false);

    this.m_billData = new BillData();
    this.m_billData.setBodyItems(BodyItem);
    this.m_billData.setHeadItems(HeaderItem);
  }

  private void initListener()
  {
    getBillCardPanel().getBillTable().getSelectionModel().addListSelectionListener(this);

    getBillCardPanel().addEditListener(this);
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage(bo.getName());
    if (bo.equals(this.m_boClose))
      onClose();
    else if (bo.equals(this.m_boOpen))
      onOpen();
    else if (bo.equals(this.m_boQuery))
      onQuery();
    else if (bo.equals(this.m_boDetail))
      onDetail();
    else if (bo.equals(this.m_boFresh))
      onFresh();
  }

  private void onClose()
  {
    int iRow = getBillCardPanel().getBillTable().getSelectedRow();
    int iRowCounts = getBillCardPanel().getBillModel().getRowCount();
    try
    {
      if (iRow >= 0)
      {
        AccountctrlHeaderVO header = (AccountctrlHeaderVO)getBillCardPanel().getBillModel().getBodyValueRowVO(iRow, AccountctrlHeaderVO.class.getName());

        header.setPk_calbody(getCalBodyPK());
        header.setPk_stordoc(getWhPK());

        AccountctrlHeaderVO[] newHeader = null;

        if ((header.getAttributeValue("tendtime") == null) || (header.getAttributeValue("tendtime").toString().trim().length() == 0))
        {
          ClientEnvironment env = getClientEnvironment();

          getConfirmTimeDlg().setDate(env.getDate());
          getConfirmTimeDlg().setTime(ClientEnvironment.getServerTime().getTime());

          getConfirmTimeDlg().setModal(true);
          getConfirmTimeDlg().showModal();

          if (getConfirmTimeDlg().getResult() == 1) {
            UFDateTime endTime = getConfirmTimeDlg().getDateTime();

            if ((endTime == null) || (endTime.toString().trim().length() == 0)) {
              showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000417"));
            } else {
              UFDateTime startTime = header.getTstarttime();

              if (endTime.before(startTime)) {
                showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000418"));
              }
              else if (showOkCancelMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000419") + endTime.toString() + NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000420")) == 1)
              {
                header.setTendtime(endTime);
                header.setFaccountflag(new UFBoolean("N"));
                header.setCoperatorid(this.m_sPkUser);
                header.setVoperatorname(this.m_sUseName);
                header.m_endtimeisnull = true;

                newHeader = AccountCtrlHelper.update(header, this.m_sPkCorp);
                if (newHeader != null) {
                  if (newHeader.length == 1) {
                    getBillCardPanel().getBillModel().setBodyRowVO(newHeader[0], iRow);
                  }
                  else if (newHeader.length == 2) {
                    getBillCardPanel().getBillModel().setBodyRowVO(newHeader[0], iRow);
                    getBillCardPanel().addLine();
                    getBillCardPanel().getBillModel().setBodyRowVO(newHeader[1], iRow + 1);
                  }

                  for (int i = 0; i < iRow; i++)
                    getBillCardPanel().getBillModel().setValueAt(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000411"), i, "faccountflag");
                  this.m_boClose.setEnabled(false);
                  this.m_boOpen.setEnabled(true);
                  updateButtons();
                  showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000421"));
                }

              }

            }

          }

        }
        else if (showOkCancelMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000419") + header.getTendtime().toString() + NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000420")) == 1)
        {
          header.setFaccountflag(new UFBoolean("N"));
          header.setCoperatorid(this.m_sPkUser);
          header.setVoperatorname(this.m_sUseName);
          header.m_endtimeisnull = false;

          newHeader = AccountCtrlHelper.update(header, this.m_sPkCorp);
          if (newHeader != null)
          {
            if (newHeader.length == 1) {
              getBillCardPanel().getBillModel().setBodyRowVO(newHeader[0], iRow);
            }
            else if (newHeader.length == 2) {
              getBillCardPanel().getBillModel().setBodyRowVO(newHeader[0], iRow);
              getBillCardPanel().addLine();
              getBillCardPanel().getBillModel().setBodyRowVO(newHeader[1], iRow + 1);
            }
            for (int i = 0; i < iRow; i++)
              getBillCardPanel().getBillModel().setValueAt(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000411"), i, "faccountflag");
            this.m_boClose.setEnabled(false);
            this.m_boOpen.setEnabled(true);
            updateButtons();
            showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000421"));
          }

        }
        else
        {
          return;
        }

      }
      else
      {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000422"));
      }
    }
    catch (Exception be)
    {
      showErrorMessage(be.getMessage());
      showHintMessage(be.getMessage());
    }
  }

  private void onDetail()
  {
    int iRow = getBillCardPanel().getBillTable().getSelectedRow();
    try {
      if (iRow < 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000422"));
      }
      else {
        String sPk_accountctrl = (String)getBillCardPanel().getBillModel().getValueAt(iRow, "pk_accountctrl");

        if (sPk_accountctrl == null) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000423"));
          return;
        }
        AccountctrlItemVO[] itemVOs = AccountCtrlHelper.queryRecord(sPk_accountctrl);
        if ((itemVOs != null) && (itemVOs.length != 0))
        {
          ((VOTableModel)getAccCtrlDetailDlg().getTable().getModel()).clearTable();
          ((VOTableModel)getAccCtrlDetailDlg().getTable().getModel()).addVO(itemVOs);

          getAccCtrlDetailDlg().setModal(true);
          getAccCtrlDetailDlg().showModal();
        } else {
          showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000424"));
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
  }

  private void onFresh()
  {
    try
    {
      String sPkCalBody = getCalBodyPK();
      String sPkWarehouse = getWhPK();

      if (((sPkCalBody == null) || (sPkCalBody.trim().length() == 0)) && ((sPkWarehouse == null) || (sPkWarehouse.trim().length() == 0)))
      {
        getBillCardPanel().getBillModel().clearBodyData();
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000425"));
        return;
      }

      AccountctrlVO[] VO = AccountCtrlHelper.fresh(sPkCalBody, sPkWarehouse, this.m_sPkCorp, this.m_sPkUser);

      if ((VO != null) && (VO.length != 0)) {
        AccountctrlHeaderVO[] headerVO = new AccountctrlHeaderVO[VO.length];

        for (int i = 0; i < VO.length; i++) {
          headerVO[i] = ((AccountctrlHeaderVO)VO[i].getParentVO());
        }
        getBillCardPanel().getBillData().setBodyValueVO(headerVO);

        if (sPkWarehouse != null)
          showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000426"));
        else if (sPkCalBody != null)
          showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000427"));
        else
          showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000428"));
      }
      else
      {
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000424"));
      }
      this.m_boClose.setEnabled(false);
      this.m_boOpen.setEnabled(false);
      this.m_boDetail.setEnabled(false);
      updateButtons();
    }
    catch (BusinessException be) {
      showErrorMessage(be.getMessage());
      showHintMessage(be.getMessage());
    } catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  private void onOpen()
  {
    int iRow = getBillCardPanel().getBillTable().getSelectedRow();
    int iRowCounts = getBillCardPanel().getBillTable().getRowCount();
    try
    {
      if (iRow < 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000429"));
      }
      else if (showOkCancelMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000430")) == 1)
      {
        AccountctrlHeaderVO headerVO = (AccountctrlHeaderVO)getBillCardPanel().getBillModel().getBodyValueRowVO(iRow, AccountctrlHeaderVO.class.getName());

        headerVO.setPk_calbody(getCalBodyPK());
        headerVO.setPk_stordoc(getWhPK());
        headerVO.setCoperatorid(this.m_sPkUser);
        headerVO.setVoperatorname(this.m_sUseName);
        headerVO.setFaccountflag(new UFBoolean("Y"));

        AccountctrlHeaderVO[] newHeaderVO = AccountCtrlHelper.update(headerVO, this.m_sPkCorp);

        if ((newHeaderVO != null) && (newHeaderVO[0] != null)) {
          getBillCardPanel().getBillModel().setBodyRowVO(newHeaderVO[0], iRow);
          for (int i = iRow; i < iRowCounts; i++)
            getBillCardPanel().getBillModel().setValueAt(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000410"), i, "faccountflag");
          this.m_boOpen.setEnabled(false);
          this.m_boClose.setEnabled(true);
          updateButtons();
          showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000431"));
        } else {
          showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000424"));
        }
      }
    }
    catch (BusinessException be)
    {
      showErrorMessage(be.getMessage());
      showHintMessage(be.getMessage());
    } catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  private void onQuery()
  {
    getQueryConditionDlg().hideNormal();

    getQueryConditionDlg().showModal();

    if (getQueryConditionDlg().isCloseOK())
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000190"));
      try
      {
        ConditionVO[] conVO = getQueryConditionDlg().getConditionVO();

        if (conVO != null)
        {
          for (int i = 0; i < conVO.length; i++)
          {
            if ((conVO[i].getFieldCode().equals("faccountflag")) && (conVO[i].getValue().equals("%")))
            {
              conVO[i].setOperaCode("like");
            } else if ("pk_calbody".equals(conVO[i].getFieldCode())) {
              String pk_calbody = conVO[i].getValue();
              getBillCardPanel().getHeadItem("calbodyname").setValue(pk_calbody);
              String[] sConstraint = null;
              sConstraint = new String[1];
              sConstraint[0] = (" AND pk_calbody='" + pk_calbody + "'");
              BillItem bi = getBillCardPanel().getHeadItem("storname");
              RefFilter.filtWh(bi, this.m_sPkCorp, sConstraint);

              bi.setValue(null);
            } else if ("cwarehouseid".equals(conVO[i].getFieldCode())) {
              String cwarehouseid = conVO[i].getValue();
              getBillCardPanel().getHeadItem("storname").setValue(cwarehouseid);
              BillItem bi = getBillCardPanel().getHeadItem("calbodyname");

              bi.setValue(null);
            }
          }
        }
        AccountctrlVO[] accVO = AccountCtrlHelper.query(conVO, this.m_sPkCorp, this.m_sPkUser);

        if ((accVO != null) && (accVO.length != 0))
        {
          AccountctrlHeaderVO[] accHeader = new AccountctrlHeaderVO[accVO.length];

          for (int i = 0; i < accVO.length; i++)
          {
            accHeader[i] = ((AccountctrlHeaderVO)accVO[i].getParentVO());
          }
          getBillCardPanel().getHeadItem("calbodyname").setValue(accHeader[0].getPk_calbody());

          getBillCardPanel().getBillModel().setBodyDataVO(accHeader);
          getBillCardPanel().updateValue();

          this.m_boClose.setEnabled(false);
          this.m_boOpen.setEnabled(false);
          this.m_boDetail.setEnabled(false);
          updateButtons();

          showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000432"));
        }
        else
        {
          getBillCardPanel().getBillModel().clearBodyData();
          this.m_boClose.setEnabled(false);
          this.m_boOpen.setEnabled(false);
          this.m_boDetail.setEnabled(false);
          updateButtons();
          showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000342"));
        }

      }
      catch (Exception e)
      {
        SCMEnv.error(e);
        if ((e instanceof BusinessException)) {
          showErrorMessage(e.getMessage());
          showHintMessage(e.getMessage());
        } else {
          showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000433"));
          showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000433"));
        }
      }
    }
  }

  private void setButtonState()
  {
    this.m_boClose.setEnabled(false);
    this.m_boOpen.setEnabled(false);
    this.m_boDetail.setEnabled(false);
  }

  public void valueChanged(ListSelectionEvent event)
  {
    if (event.getSource().equals(getBillCardPanel().getBillTable().getSelectionModel()))
    {
      int iRow = getBillCardPanel().getBillTable().getSelectedRow();

      if (iRow < 0) {
        return;
      }

      this.m_boDetail.setEnabled(true);
      String bAccFlag = (String)getBillCardPanel().getBillModel().getValueAt(iRow, "faccountflag");

      if (NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000410").equals(bAccFlag))
      {
        this.m_boClose.setEnabled(true);
        this.m_boOpen.setEnabled(false);
      }
      else {
        this.m_boClose.setEnabled(false);
        this.m_boOpen.setEnabled(true);
      }
      updateButtons();
    }
  }
}