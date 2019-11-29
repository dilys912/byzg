package nc.ui.sc.order;

import java.awt.CardLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import nc.bs.bd.b21.CurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.itf.sc.IDataPowerForSC;
import nc.itf.scm.cenpur.service.CentrPurchaseUtil;
import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.ui.bd.b21.CurrtypeQuery;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.ct.ref.ValiCtRefModel;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.po.pub.PoCntSelDlg;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.po.ref.PoInputInvRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillRelaSortListener;
import nc.ui.pub.billcodemanage.BillcodeRuleBO_Client;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.ui.sc.adjust.AdjustbillHelper;
import nc.ui.sc.ct.ScFromCtHelper;
import nc.ui.sc.pub.BillEdit;
import nc.ui.sc.pub.OtherRefModel;
import nc.ui.sc.pub.ProjectPhase;
import nc.ui.sc.pub.PublicHelper;
import nc.ui.sc.pub.ScTool;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.inv.InvTool;
import nc.ui.scm.pu.ParaVOForBatch;
import nc.ui.scm.pub.CollectSettingDlg;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.IBillExtendFun;
import nc.ui.scm.pub.cache.CacheTool;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.BillPanelTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.print.ISetBillVO;
import nc.ui.scm.pub.print.ScmPrintTool;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.pub.report.OrientDialog;
import nc.ui.scm.service.LocalCallService;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.ui.scm.sourcebill.SourceBillTool;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.bd.b20.CurrtypeVO;
import nc.vo.mm.pub.pub1020.DisConditionVO;
import nc.vo.mm.pub.pub1020.DisassembleVO;
import nc.vo.pp.ask.PriceauditHeaderVO;
import nc.vo.pp.ask.PriceauditItemMergeVO;
import nc.vo.pp.ask.PriceauditMergeVO;
import nc.vo.pu.exception.RwtPiToPoException;
import nc.vo.pu.exception.RwtPiToScException;
import nc.vo.pu.exception.RwtScToPrException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.formulaset.util.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.sc.order.OrderHeaderVO;
import nc.vo.sc.order.OrderItemVO;
import nc.vo.sc.order.OrderVO;
import nc.vo.sc.order.SCMessageVO;
import nc.vo.sc.pub.BD_ConvertVO;
import nc.vo.sc.pub.CustDefaultVO;
import nc.vo.sc.pub.RetScVrmAndParaPriceVO;
import nc.vo.sc.pub.SCPubVO;
import nc.vo.sc.pub.ScConstants;
import nc.vo.scm.cenpur.service.ChgDocPkVO;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;
import nc.vo.scm.datapower.BtnPowerVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.scm.service.ServcallVO;
import nc.vo.sm.UserVO;

public class OrderUI extends ToftPanel
  implements BillEditListener, BillEditListener2, BillTableMouseListener, BillBodyMenuListener, ISetBillVO, IBillRelaSortListener, IBillExtendFun, ILinkMaintain, ILinkAdd, ILinkApprove, ILinkQuery
{
  private boolean m_bQueried = false;

  private ButtonTree m_btnTree = null;

  private ButtonObject boBusitype = null;

  private ButtonObject boAdd = null;

  private ButtonObject boSave = null;

  private ButtonObject boBillMaintain = null;
  private ButtonObject boEdit = null;
  private ButtonObject boCancel = null;
  private ButtonObject boDel = null;
  private ButtonObject boCopy = null;

  private ButtonObject boLineOperator = null;
  private ButtonObject boAddLine = null;
  private ButtonObject boDelLine = null;
  private ButtonObject boInsertLine = null;
  private ButtonObject boCopyLine = null;
  private ButtonObject boPasteLine = null;

  private ButtonObject boAction = null;
  private ButtonObject boSendAudit = null;
  private ButtonObject boAudit = null;
  private ButtonObject boUnaudit = null;
  private ButtonObject boCancelOut = null;

  private ButtonObject boQuery = null;

  private ButtonObject boBillBrowsed = null;
  private ButtonObject boFrash = null;
  private ButtonObject boLocate = null;
  private ButtonObject boFirst = null;
  private ButtonObject boPre = null;
  private ButtonObject boNext = null;
  private ButtonObject boLast = null;
  private ButtonObject boSelectAll = null;
  private ButtonObject boSelectNo = null;

  private ButtonObject boReturn = null;

  private ButtonObject m_btnPrints = null;
  private ButtonObject m_btnPrintPreview = null;
  private ButtonObject boPrint = null;
  private ButtonObject btnBillCombin = null;

  private ButtonObject m_btnOthersQueries = null;
  private ButtonObject boLinkQuery = null;
  private ButtonObject boQueryForAudit = null;

  private ButtonObject m_btnOthersFuncs = null;
  private ButtonObject boDocument = null;
  private boolean bWastControl;
  private CardLayout cardLayout = null;

  private ArrayList comeVOs = null;

  private boolean m_bAutoSendToAudit = true;
  private BillCardPanel m_BillCardPanel;
  private int m_billState = ScConstants.STATE_CARD;
  private OrderVO m_curOrderVO;
  private CurrencyRateUtil m_CurrArith;
  private int m_iCurBillIndex = 0;

  private int m_iPricePolicy = 6;

  private String m_strPriceDspValue = "";
  private boolean mbol_KeepIniMaker;
  private OrderUIQueryDlg m_query;
  private String m_queryCon = null;
  private OrderListPanel m_ScOrderListPanel;
  private String m_sHeadVmemo;
  String m_sPKCurrencyType = "";

  int m_intCurrencyDecimal = 2;

  String m_sPKCurrencyTypeAssit = "";

  int m_intCurrencyDecimalAssit = 2;

  int[] mintary_precisions = { 2, 2, 2, 2 };

  int mint_localmnyPrecision = 2;

  boolean mbol_HasGetDecimal = false;

  private boolean m_bCTStartUp = false;
  private ArrayList m_listVOs;
  private ScmPrintTool printList = null;

  private PoCntSelDlg m_CntSelDlg = null;

  private void createBtnInstances()
  {
    this.boBusitype = getBtnTree().getButton("业务类型");

    this.boAdd = getBtnTree().getButton("增加");

    this.boSave = getBtnTree().getButton("保存");

    this.boBillMaintain = getBtnTree().getButton("维护");
    this.boEdit = getBtnTree().getButton("修改");
    this.boCancel = getBtnTree().getButton("取消");
    this.boDel = getBtnTree().getButton("删除");
    this.boCopy = getBtnTree().getButton("复制");

    this.boLineOperator = getBtnTree().getButton("行操作");
    this.boAddLine = getBtnTree().getButton("增行");
    this.boDelLine = getBtnTree().getButton("删行");
    this.boInsertLine = getBtnTree().getButton("插入行");
    this.boCopyLine = getBtnTree().getButton("复制行");
    this.boPasteLine = getBtnTree().getButton("粘贴行");

    this.boAction = getBtnTree().getButton("执行");
    this.boSendAudit = getBtnTree().getButton("送审");
    this.boAudit = getBtnTree().getButton("审核");
    this.boUnaudit = getBtnTree().getButton("弃审");
    this.boCancelOut = getBtnTree().getButton("放弃转单");

    this.boQuery = getBtnTree().getButton("查询");

    this.boBillBrowsed = getBtnTree().getButton("浏览");
    this.boFrash = getBtnTree().getButton("刷新");
    this.boLocate = getBtnTree().getButton("定位");
    this.boFirst = getBtnTree().getButton("首页");
    this.boPre = getBtnTree().getButton("上页");
    this.boNext = getBtnTree().getButton("下页");
    this.boLast = getBtnTree().getButton("末页");
    this.boSelectAll = getBtnTree().getButton("全选");
    this.boSelectNo = getBtnTree().getButton("全消");

    this.boReturn = getBtnTree().getButton("卡片显示/列表显示");

    this.m_btnPrints = getBtnTree().getButton("打印管理");
    this.m_btnPrintPreview = getBtnTree().getButton("预览");
    this.boPrint = getBtnTree().getButton("打印");
    this.btnBillCombin = getBtnTree().getButton("合并显示");

    this.m_btnOthersQueries = getBtnTree().getButton("辅助查询");
    this.boLinkQuery = getBtnTree().getButton("联查");
    this.boQueryForAudit = getBtnTree().getButton("审批流状态");

    this.m_btnOthersFuncs = getBtnTree().getButton("辅助功能");
    this.boDocument = getBtnTree().getButton("文档管理");
  }

  private ButtonTree getBtnTree()
  {
    if (this.m_btnTree == null) {
      try {
        this.m_btnTree = new ButtonTree(getModuleCode());
      }
      catch (BusinessException be) {
        showHintMessage(be.getMessage());
        return null;
      }
    }
    return this.m_btnTree;
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      OrderUI aOrderUI = new OrderUI();
      frame.setContentPane(aOrderUI);
      frame.setSize(aOrderUI.getSize());
      
      //edit by yqq 2017-02-28反编译改
      frame.addWindowListener((WindowListener) new OrderUI());
    //  frame.addWindowListener(new OrderUI.1());

      frame.show();
      Insets insets = frame.getInsets();
      frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
      frame.setVisible(true);
    } catch (Throwable exception) {
      SCMEnv.out("nc.ui.pub.ToftPanel 的 main() 中发生异常");
      exception.printStackTrace(System.out);
    }
  }

  public OrderUI()
  {
    init();
  }

  public void actionPerformed(ActionEvent e)
  {
  }

  public void afterEdit(BillEditEvent e)
  {
    try
    {
      if (e.getPos() == 0)
      {
        if ((e.getKey().equals("cvendormangid")) || (e.getKey().equals("caccountbankid"))) {
          BillEdit.editCust(getBillCardPanel(), e.getKey());
        }

        if ((e.getKey().equals("cdeptid")) || (e.getKey().equals("cemployeeid"))) {
          BillEdit.editDeptAndEmployee(getBillCardPanel(), e.getKey());
        }

        if (e.getKey().equals("creciever")) {
          setReceiveAddress(0, e.getKey());
        }

        if (e.getKey().equals("cwareid")) {
          for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
            BillEdit.editArrDate(getBillCardPanel(), i, e.getKey(), ClientEnvironment.getInstance().getDate());
          }

          String strWareId = getBillCardPanel().getHeadItem("cwareid").getValue();

          afterEditStoreOrgToWarehouse(getBillCardPanel(), e, getCorpPrimaryKey(), strWareId, "cwarehousename", new String[] { "cwarehouseid", "warehouseorg" });
        }

        setHeadDefPK(e);

        if ((e.getKey().equals("cvendormangid")) || (e.getKey().equals("dorderdate"))) {
          setRelateCntAndDefaultPriceAllRow(false);
        }

        if (e.getKey().equals("cwareid")) {
          setRelateCntAndDefaultPriceAllRow(true);
        }

        setNotEditableWhenRelateCntAllRow();

        if (e.getKey().indexOf("vdef") >= 0) getBillCardPanel().execHeadEditFormulas();

      }

      if (e.getPos() == 1)
      {
        int rowindex = e.getRow();

        if (e.getKey().equals("crowno")) {
          BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, "61");
        }

        if (e.getKey().equals("ndiscountrate")) {
          Object odiscountrate = e.getValue();
          if ((odiscountrate == null) || (odiscountrate.equals(""))) {
            getBillCardPanel().setBodyValueAt("100", rowindex, "ndiscountrate");
          } else {
            double discount = new UFDouble(odiscountrate.toString()).doubleValue();
            if ((discount <= 0.0D) || (discount > 100.0D)) {
              showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000035"));
              getBillCardPanel().setBodyValueAt("100", rowindex, "ndiscountrate");
              getBillCardPanel().execBodyFormula(rowindex, "ndiscountrate");
            }
          }

        }

        int iBeginRow = 0; int iEndRow = 0;
        if (e.getKey().equals("cinventorycode")) {
          UIRefPane refpane = (UIRefPane)getBillCardPanel().getBodyItem("cinventorycode").getComponent();

          Object[] saMangId = (Object[])(Object[])refpane.getRefValues("bd_invmandoc.pk_invmandoc");
          Object[] saBaseId = (Object[])(Object[])refpane.getRefValues("bd_invmandoc.pk_invbasdoc");

          if ((saMangId == null) || (saBaseId == null) || (saMangId.length != saBaseId.length)) {
            SCMEnv.out("数据错误：存货管理档案ID为空或存货档案ID为空或二者长度不等，直接返回");
            return;
          }
          Object[] saCode = (Object[])(Object[])refpane.getRefValues("bd_invbasdoc.invcode");
          Object[] saName = (Object[])(Object[])refpane.getRefValues("bd_invbasdoc.invname");
          Object[] saSpec = (Object[])(Object[])refpane.getRefValues("bd_invbasdoc.invspec");
          Object[] saType = (Object[])(Object[])refpane.getRefValues("bd_invbasdoc.invtype");

          Object[] saMeasUnit = (Object[])(Object[])refpane.getRefValues("bd_invbasdoc.pk_measdoc");

          int iLen = saMangId.length;

          String[] saFormula = { "getColValue(bd_measdoc,measname,pk_measdoc,cmessureunit)", "getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)", "getColValue(bd_invbasdoc,assistunit,pk_invbasdoc,cbaseid)" };
          FormulaParse forParse = new FormulaParse();

          forParse.setExpressArray(saFormula);
          Hashtable[] htaVarry = new Hashtable[saFormula.length];
          int iFormulaLen = saFormula.length;

          String[] ssaMeasUnit = new String[saMeasUnit.length];
          for (int i = 0; i < saMeasUnit.length; i++)
            ssaMeasUnit[i] = StringUtil.toString((String)saMeasUnit[i]);
          String[] ssaBaseId = new String[saBaseId.length];
          for (int i = 0; i < saBaseId.length; i++) {
            ssaBaseId[i] = StringUtil.toString((String)saBaseId[i]);
          }
          for (int i = 0; i < iFormulaLen; i++) {
            htaVarry[i] = new Hashtable();
            htaVarry[i].put("cmessureunit", ssaMeasUnit);
            htaVarry[i].put("cbaseid", ssaBaseId);
          }

          forParse.setDataSArray(htaVarry);

          String[][] saRet = forParse.getValueSArray();

          String[] saUnitName = new String[iLen];

          String[] sAssisUnit = new String[iLen];

          String[] sIsAssisUnit = new String[iLen];

          if (saRet != null) {
            for (int i = 0; i < iLen; i++) {
              if (saRet[0] != null) {
                saUnitName[i] = saRet[0][i];
              }
              if (saRet[1] != null) {
                sAssisUnit[i] = saRet[1][i];
              }
              if (saRet[2] != null) {
                sIsAssisUnit[i] = saRet[2][i];
              }
            }

          }

          saFormula = new String[] { "getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)" };
          iFormulaLen = saFormula.length;

          forParse.setExpressArray(saFormula);
          htaVarry = new Hashtable[iFormulaLen];

          String[] ssAssisUnit = new String[sAssisUnit.length];
          for (int i = 0; i < sAssisUnit.length; i++) {
            ssAssisUnit[i] = StringUtil.toString(sAssisUnit[i]);
          }
          for (int i = 0; i < iFormulaLen; i++) {
            htaVarry[i] = new Hashtable();
            htaVarry[i].put("cassistunit", ssAssisUnit);
          }

          forParse.setDataSArray(htaVarry);

          saRet = forParse.getValueSArray();

          String[] sAssisUnitName = new String[iLen];
          if (saRet != null) {
            for (int i = 0; i < iLen; i++) {
              if (saRet[0] != null) {
                sAssisUnitName[i] = saRet[0][i];
              }
            }

          }

          int iInsertLen = saMangId == null ? 0 : saMangId.length - 1;
          iBeginRow = e.getRow();
          if (iBeginRow == getBillCardPanel().getRowCount() - 1)
          {
            for (int i = 0; i < iInsertLen; i++) {
              onAppendLine();
            }
          }
          else
          {
            onInsertLines(iBeginRow, iBeginRow + 1, iInsertLen);
          }
          iEndRow = iBeginRow + iInsertLen;

          int iPkIndex = 0;
          for (int i = iBeginRow; i <= iEndRow; i++) {
            iPkIndex = i - iBeginRow;

            getBillCardPanel().setBodyValueAt(saMangId[iPkIndex], i, "cmangid");

            getBillCardPanel().setBodyValueAt(saBaseId[iPkIndex], i, "cbaseid");

            getBillCardPanel().setBodyValueAt(saCode[iPkIndex], i, "cinventorycode");

            getBillCardPanel().setBodyValueAt(saName[iPkIndex], i, "cinventoryname");

            getBillCardPanel().setBodyValueAt(saSpec[iPkIndex], i, "invspec");

            getBillCardPanel().setBodyValueAt(saType[iPkIndex], i, "invtype");

            getBillCardPanel().setBodyValueAt(saMeasUnit[iPkIndex], i, "pk_measdoc");

            getBillCardPanel().setBodyValueAt(saUnitName[iPkIndex], i, "cmeasdocname");

            getBillCardPanel().setBodyValueAt(sAssisUnit[iPkIndex], i, "cassistunit");

            getBillCardPanel().setBodyValueAt(sAssisUnitName[iPkIndex], i, "cassistunitname");

            getBillCardPanel().setBodyValueAt(sIsAssisUnit[iPkIndex], i, "isassist");

            getBillCardPanel().getBillModel().setRowState(i, 1);
          }

          for (int i = iBeginRow; i <= iEndRow; i++)
          {
            String sMangId = getBillCardPanel().getBodyValueAt(i, "cmangid").toString();
            if ((sMangId == null) || (sMangId.trim().length() == 0))
              getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", false);
            else {
              getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", InvTool.isBatchManaged(sMangId));
            }

            getBillCardPanel().setBodyValueAt(null, i, "vproducenum");
          }

          String sUpSourceType = (String)getBillCardPanel().getBodyValueAt(iBeginRow, "cupsourcebilltype");
          if ((sUpSourceType != null) && ((sUpSourceType.equals("Z1")) || (sUpSourceType.equals("Z2"))))
          {
            String sCntRowId = (String)getBillCardPanel().getBodyValueAt(iBeginRow, "cupsourcebillrowid");
            String sCntId = (String)getBillCardPanel().getBodyValueAt(iBeginRow, "cupsourcebillid");

            RetCtToPoQueryVO voCntInfo = null;

            if ((voCntInfo != null) && (voCntInfo.getIinvCtl() == RetCtToPoQueryVO.INVCLASSCTL))
            {
              for (int i = iBeginRow; i <= iEndRow; i++) {
                getBillCardPanel().setBodyValueAt(sUpSourceType, i, "cupsourcebilltype");
                getBillCardPanel().setBodyValueAt(sCntRowId, i, "cupsourcebillrowid");
                getBillCardPanel().setBodyValueAt(sCntId, i, "cupsourcebillid");

                getBillCardPanel().setBodyValueAt(voCntInfo.getCContractID(), i, "ccontractid");
                getBillCardPanel().setBodyValueAt(voCntInfo.getCContractRowId(), i, "ccontractrowid");
                getBillCardPanel().setBodyValueAt(voCntInfo.getCContractCode(), i, "ccontractrcode");
              }

            }

          }

          int[] lintary_rows = new int[iEndRow - iBeginRow + 1];
          int i = iBeginRow; for (int j = 0; i <= iEndRow; j++) {
            lintary_rows[j] = i;

            i++;
          }

          setRelateCntAndDefaultPrice(lintary_rows, false);
        }

        if (e.getKey().equals("cinventorycode")) {
          BillEdit.editFreeItemsForMultiSelected(getBillCardPanel(), iBeginRow, iEndRow, e.getKey(), "cbaseid", "cmangid");
        }
        if (e.getKey().equals("vfree0")) {
          BillEdit.editFreeItem(getBillCardPanel(), rowindex, e.getKey(), "cbaseid", "cmangid");
        }

        if (e.getKey().equals("cwarehousename")) {
          setReceiveAddress(rowindex, e.getKey());
        }

        if (e.getKey().equals("cprojectname")) {
          Object cprojectid = getBillCardPanel().getBodyValueAt(rowindex, "cprojectid");
          if ((cprojectid != null) && (!cprojectid.toString().trim().equals(""))) {
            UIRefPane ref = (UIRefPane)getBillCardPanel().getBodyItem("cprojectphasename").getComponent();

            ref.setRefModel(new ProjectPhase(cprojectid.toString()));
            getBillCardPanel().setCellEditable(rowindex, "cprojectphasename", true);
          } else {
            getBillCardPanel().setBodyValueAt("", rowindex, "cprojectphasename");
            getBillCardPanel().setBodyValueAt("", rowindex, "cprojectphaseid");
            getBillCardPanel().setCellEditable(rowindex, "cprojectphasename", false);
            getBillCardPanel().updateUI();
          }

        }

        if (e.getKey().equals("ccurrencytype")) {
          setCurrencyState(rowindex, true);
          getBillCardPanel().updateUI();
          SCMEnv.out("金额精度：" + getBillCardPanel().getBodyItem("noriginalcurmny").getDecimalDigits());
        }

        int[] descriptions = { 0, 1, 2, 18, 9, 8, 7, 15, 11, 13, 12, 14 };

        String lStr_discounttaxtype = ScConstants.TaxType_Not_Including;
        Object oTemp = getBillCardPanel().getBodyValueAt(e.getRow(), "idiscounttaxtype");
        if (oTemp != null) {
          lStr_discounttaxtype = oTemp.toString();
        }

        String[] keys = { lStr_discounttaxtype, "idiscounttaxtype", "nordernum", "norgtaxprice", "noriginalcurprice", "norgnettaxprice", "noriginalnetprice", "ndiscountrate", "ntaxrate", "noriginalcurmny", "noriginaltaxmny", "noriginalsummny" };

        if ((e.getKey().equals("ndiscountrate")) || (e.getKey().equals("nordernum")) || (e.getKey().equals("noriginalnetprice")) || (e.getKey().equals("noriginalcurprice")) || (e.getKey().equals("ntaxrate")) || (e.getKey().equals("noriginalcurmny")) || (e.getKey().equals("noriginaltaxmny")) || (e.getKey().equals("noriginalsummny")) || (e.getKey().equals("norgtaxprice")) || (e.getKey().equals("norgnettaxprice"))) {
          if (getBillCardPanel().getBillModel().getValueAt(rowindex, "ntaxrate") == null)
            getBillCardPanel().getBillModel().setValueAt(new UFDouble(0), rowindex, "ntaxrate");
          RelationsCal.calculate(e, getBillCardPanel(), new int[] { this.m_iPricePolicy }, descriptions, keys, OrderItemVO.class.getName());
        }

        if (e.getKey().equals("ccurrencytype")) {
          RelationsCal.calculate(e, getBillCardPanel(), new int[] { this.m_iPricePolicy }, "nordernum", descriptions, keys, OrderItemVO.class.getName());
        }

        if (e.getKey().equals("cinventorycode")) {
          BillEdit.editAssistUnitForMultiSelected(getBillCardPanel(), iBeginRow, iEndRow, e.getKey());
        }

        if ((e.getKey().equals("cassistunitname")) || (e.getKey().equals("nordernum")) || (e.getKey().equals("nassistnum")) || (e.getKey().equals("measrate"))) {
          BillEdit.editAssistUnit(getBillCardPanel(), rowindex, e.getKey());
          BillEditEvent t = new BillEditEvent(e.getSource(), e.getValue(), "nordernum", e.getRow(), e.getPos());

          RelationsCal.calculate(t, getBillCardPanel(), new int[] { this.m_iPricePolicy }, descriptions, keys, OrderItemVO.class.getName());
        }

        if (e.getKey().equals("cinventorycode"))
        {
          setRelated_Taxrate(iBeginRow, iEndRow);

          for (int i = iBeginRow; i <= iEndRow; i++) {
            BillEditEvent l_BillEditEvent = new BillEditEvent(e.getSource(), e.getOldValue(), null, "ntaxrate", i, e.getPos());
            l_BillEditEvent.setTableCode(e.getTableCode());
            RelationsCal.calculate(l_BillEditEvent, getBillCardPanel(), new int[] { this.m_iPricePolicy }, descriptions, keys, OrderItemVO.class.getName());
          }

        }

        if ((e.getKey().equals("ccurrencytype")) || (e.getKey().equals("nexchangeotobrate")) || (e.getKey().equals("nexchangeotoarate"))) {
          setRelateCntAndDefaultPriceOneRow(e.getRow(), true);
        }

        setBodyDefPK(e);

        if (e.getKey().equals("ccontractrcode")) {
          afterEditWhenBodyCntCode(e);
        }

        setNotEditableWhenRelateCntAllRow();
      }
    }
    catch (Exception t)
    {
      SCMEnv.out("error!  afterEdit(BillEditEvent e)" + t.getMessage());
    }
  }

  private void afterEditWhenBodyCntCode(BillEditEvent e)
  {
    try
    {
      int lint_rowindex = e.getRow();
      if ((e.getValue() == null) || (e.getValue().toString().trim().length() < 1))
      {
        getBillCardPanel().setCellEditable(lint_rowindex, "ccontractrcode", (getBillCardPanel().getBodyItem("ccontractrcode") != null) && (getBillCardPanel().getBodyItem("ccontractrcode").isEdit()));
        getBillCardPanel().setCellEditable(lint_rowindex, "ccurrencytype", (getBillCardPanel().getBodyItem("ccurrencytype") != null) && (getBillCardPanel().getBodyItem("ccurrencytype").isEdit()));
        getBillCardPanel().setCellEditable(lint_rowindex, "noriginalcurprice", (getBillCardPanel().getBodyItem("noriginalcurprice") != null) && (getBillCardPanel().getBodyItem("noriginalcurprice").isEdit()));
        getBillCardPanel().setCellEditable(lint_rowindex, "norgtaxprice", (getBillCardPanel().getBodyItem("norgtaxprice") != null) && (getBillCardPanel().getBodyItem("norgtaxprice").isEdit()));
        getBillCardPanel().setCellEditable(lint_rowindex, "noriginalnetprice", (getBillCardPanel().getBodyItem("noriginalnetprice") != null) && (getBillCardPanel().getBodyItem("noriginalnetprice").isEdit()));
        getBillCardPanel().setCellEditable(lint_rowindex, "noriginalcurmny", (getBillCardPanel().getBodyItem("noriginalcurmny") != null) && (getBillCardPanel().getBodyItem("noriginalcurmny").isEdit()));

        getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractid");
        getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractrowid");
        getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractrcode");
      } else {
        ValiCtRefModel refmodelCt = (ValiCtRefModel)((UIRefPane)getBillCardPanel().getBodyItem("ccontractrcode").getComponent()).getRefModel();
        Object[] oRet = new Object[5];
        oRet[0] = ((String)refmodelCt.getValue("ct_b.pk_ct_manage"));
        oRet[1] = ((String)refmodelCt.getValue("ct_b.pk_ct_manage_b"));
        oRet[2] = ((String)refmodelCt.getValue("ct.ct_code"));
        oRet[3] = ((String)refmodelCt.getValue("ct.currid"));

        getBillCardPanel().setBodyValueAt(oRet[0], lint_rowindex, "ccontractid");
        getBillCardPanel().setBodyValueAt(oRet[1], lint_rowindex, "ccontractrowid");
        getBillCardPanel().setBodyValueAt(oRet[2], lint_rowindex, "ccontractrcode");

        if ((oRet[1] != null) && (oRet[1].toString().trim().length() > 0)) {
          String lStr_CtBid = oRet[1].toString().trim();
          RetCtToPoQueryVO[] lary_CtRetVO = ScFromCtHelper.queryCntByct_b(new String[] { lStr_CtBid });

          if ((lary_CtRetVO != null) && (lary_CtRetVO.length > 0) && (lary_CtRetVO.length == 1))
          {
            String lStr_ChangedKey = OrderItemVO.getPriceFieldByPricePolicy(this.m_iPricePolicy);

            UFDouble lUFD_Price = null;
            lUFD_Price = SCPubVO.getPriceValueByPricePolicy(lary_CtRetVO[0], this.m_iPricePolicy);

            if (lUFD_Price != null)
            {
              UFDouble lUFD_OldPrice = SCPubVO.getUFDouble_ValueAsValue(getBillCardPanel().getBodyValueAt(lint_rowindex, lStr_ChangedKey));

              getBillCardPanel().setBodyValueAt(lary_CtRetVO[0].getCContractID(), lint_rowindex, "ccontractid");
              getBillCardPanel().setBodyValueAt(lary_CtRetVO[0].getCContractRowId(), lint_rowindex, "ccontractrowid");
              getBillCardPanel().setBodyValueAt(lary_CtRetVO[0].getCContractCode(), lint_rowindex, "ccontractrcode");

              if ((lUFD_OldPrice == null) || (lUFD_Price.compareTo(lUFD_OldPrice) != 0)) {
                getBillCardPanel().setBodyValueAt(lUFD_Price, lint_rowindex, lStr_ChangedKey);

                int[] descriptions = { 0, 1, 2, 18, 9, 8, 7, 15, 11, 13, 12, 14 };

                Object oTemp = getBillCardPanel().getBodyValueAt(lint_rowindex, "idiscounttaxtype");
                String lStr_discounttaxtype = ScConstants.TaxType_Not_Including;
                if (oTemp != null) {
                  lStr_discounttaxtype = oTemp.toString();
                }

                String[] keys = { lStr_discounttaxtype, "idiscounttaxtype", "nordernum", "norgtaxprice", "noriginalcurprice", "norgnettaxprice", "noriginalnetprice", "ndiscountrate", "ntaxrate", "noriginalcurmny", "noriginaltaxmny", "noriginalsummny" };
                BillEditEvent l_BillEditEventTemp = new BillEditEvent(getBillCardPanel().getBodyItem(lStr_ChangedKey).getComponent(), getBillCardPanel().getBodyValueAt(lint_rowindex, lStr_ChangedKey), lStr_ChangedKey, lint_rowindex);
                RelationsCal.calculate(l_BillEditEventTemp, getBillCardPanel(), new int[] { this.m_iPricePolicy }, descriptions, keys, OrderItemVO.class.getName());
              }
            }

          }

        }

        setRelateCntAndDefaultPriceOneRow(lint_rowindex, true);
        setNotEditableWhenRelateCnt(new int[] { lint_rowindex });
      }

      UIRefPane pane = (UIRefPane)getBillCardPanel().getBodyItem("ccontractrcode").getComponent();
      pane.setPK(getBillCardPanel().getBodyValueAt(lint_rowindex, "ccontractrowid"));
    }
    catch (Exception l_Exception)
    {
      showErrorMessage("" + l_Exception.getMessage());
      l_Exception.printStackTrace(System.out);
      return;
    }
  }

  public boolean beforeEdit(BillEditEvent e)
  {
    if (e.getKey().equals("vproducenum"))
    {
      ParaVOForBatch vo = new ParaVOForBatch();
      int iRow = e.getRow();
      BillCardPanel bcPanel = getBillCardPanel();

      vo.setMangIdField("cmangid");
      vo.setInvCodeField("cinventorycode");
      vo.setInvNameField("cinventoryname");
      vo.setSpecificationField("invspec");
      vo.setInvTypeField("invtype");
      vo.setMainMeasureNameField("cassistunitname");
      vo.setAssistUnitIDField("cassistunit");

      vo.setIsAstMg(new UFBoolean(InvTool.isAssUnitManaged((String)bcPanel.getBodyValueAt(iRow, "cbaseid"))));

      vo.setWarehouseIDField("cwarehouseid");
      vo.setFreePrefix("vfree");

      vo.setCardPanel(getBillCardPanel());
      vo.setPk_corp(getCorpPrimaryKey());
      vo.setEvent(e);
      try
      {
        ScTool.beforeEditWhenBodyBatch(vo);
      } catch (Exception ex) {
        ex.printStackTrace();
      }

    }
    else if (e.getKey().equals("cwarehousename")) {
      ((UIRefPane)getBillCardPanel().getBodyItem("cwarehousename").getComponent()).setPk_corp(getCorpPrimaryKey());
      BillPanelTool.restrictWarehouseRefByStoreOrg(getBillCardPanel(), getCorpPrimaryKey(), getBillCardPanel().getHeadItem("cwareid").getValue(), "cwarehousename");
    }

    if (e.getKey().equals("cinventorycode"))
    {
      getBillCardPanel().stopEditing();

      int iRow = e.getRow();
      String sClassId = null;

      String sUpSourceType = (String)getBillCardPanel().getBodyValueAt(iRow, "cupsourcebilltype");
      if ((sUpSourceType != null) && ((sUpSourceType.equals("Z1")) || (sUpSourceType.equals("Z2"))))
      {
        String sCntRowId = (String)getBillCardPanel().getBodyValueAt(iRow, "cupsourcebillrowid");

        RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(sCntRowId);
        if ((voCntInfo != null) && (voCntInfo.getIinvCtl() == RetCtToPoQueryVO.INVCLASSCTL))
        {
          sClassId = voCntInfo.getCInvClass();
        }

      }

      UIRefPane ref = (UIRefPane)getBillCardPanel().getBodyItem("cinventorycode").getComponent();
      ref.setPk_corp(getPk_corp());
      PoInputInvRefModel refModel = new PoInputInvRefModel(getPk_corp(), sClassId);

      ref.setRefModel(refModel);
    }

    if (e.getKey().equals("ccontractrcode")) {
      int iRow = e.getRow();

      UIRefPane pane = (UIRefPane)getBillCardPanel().getBodyItem("ccontractrcode").getComponent();
      pane.setRefModel(new ValiCtRefModel(getBillCardPanel().getHeadItem("pk_corp").getValue(), getBillCardPanel().getHeadItem("cvendorid").getValue(), (String)getBillCardPanel().getBodyValueAt(iRow, "cbaseid"), new UFDate(getBillCardPanel().getHeadItem("dorderdate").getValue()), new Boolean(true)));
    }

    setChangePrecision(e.getRow());

    return true;
  }

  public void bodyRowChange(BillEditEvent e)
  {
    try
    {
      if ((this.m_billState != ScConstants.STATE_ADD) && (this.m_billState != ScConstants.STATE_MODIFY) && (this.m_billState != ScConstants.STATE_OTHER)) {
        return;
      }
      int rowindex = e.getRow();
      if (rowindex < 0) {
        return;
      }

      Object cprojectid = getBillCardPanel().getBodyValueAt(rowindex, "cprojectid");
      if ((cprojectid == null) || (cprojectid.toString().trim().equals(""))) {
        getBillCardPanel().setCellEditable(rowindex, "cprojectphasename", false);
      } else {
        UIRefPane ref = (UIRefPane)getBillCardPanel().getBodyItem("cprojectphasename").getComponent();

        ref.setRefModel(new ProjectPhase(cprojectid.toString()));
        getBillCardPanel().setCellEditable(rowindex, "cprojectphasename", true);
      }

      setCurrencyState(rowindex, false);

      Object cupsourcebillrowid = getBillCardPanel().getBodyValueAt(rowindex, "cupsourcebillrowid");
      if ((cupsourcebillrowid != null) && (!cupsourcebillrowid.toString().trim().equals("")))
      {
        String sUpSourceType = (String)getBillCardPanel().getBodyValueAt(rowindex, "cupsourcebilltype");
        if ((sUpSourceType != null) && ((sUpSourceType.equals("Z1")) || (sUpSourceType.equals("Z2"))))
        {
          getBillCardPanel().setCellEditable(e.getRow(), "cinventorycode", false);

          String sCntRowId = (String)getBillCardPanel().getBodyValueAt(rowindex, "cupsourcebillrowid");

          RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(sCntRowId);
          if (voCntInfo != null)
          {
            if ((voCntInfo.getIinvCtl() == RetCtToPoQueryVO.INVCLASSCTL) || (voCntInfo.getIinvCtl() == RetCtToPoQueryVO.INVNULLCTL))
            {
              getBillCardPanel().setCellEditable(e.getRow(), "cinventorycode", true);
            }
          }
        }
      }
      else {
        getBillCardPanel().setCellEditable(e.getRow(), "cinventorycode", true);
      }

      Object pk_invbasdoc = getBillCardPanel().getBodyValueAt(rowindex, "cbaseid");
      if (pk_invbasdoc == null) {
        getBillCardPanel().setCellEditable(e.getRow(), "cassistunitname", false);
        getBillCardPanel().setCellEditable(e.getRow(), "measrate", false);
        getBillCardPanel().setCellEditable(e.getRow(), "vfree0", false);
        return;
      }

      getBillCardPanel().setCellEditable(e.getRow(), "cassistunitname", true);
      getBillCardPanel().setCellEditable(e.getRow(), "measrate", true);
      getBillCardPanel().setCellEditable(e.getRow(), "vfree0", true);

      Object bisAssist = getBillCardPanel().getBodyValueAt(rowindex, "isassist");
      if ((bisAssist == null) || (bisAssist.equals("N"))) {
        getBillCardPanel().setCellEditable(rowindex, "cassistunitname", false);
        getBillCardPanel().setCellEditable(e.getRow(), "measrate", false);
      } else {
        getBillCardPanel().setCellEditable(rowindex, "cassistunitname", true);
        getBillCardPanel().setCellEditable(e.getRow(), "measrate", true);
        Object pk_measdoc = getBillCardPanel().getBodyValueAt(rowindex, "pk_measdoc");
        UIRefPane ref = (UIRefPane)getBillCardPanel().getBodyItem("cassistunitname").getComponent();
        ref.setWhereString(" bd_convert.pk_invbasdoc='" + pk_invbasdoc + "' and bd_measdoc.pk_measdoc<>'" + pk_measdoc + "' ");
        String unionPart = " union all ";
        unionPart = unionPart + "(select bd_measdoc.shortname,bd_measdoc.measname,bd_invbasdoc.pk_measdoc ";
        unionPart = unionPart + "from bd_invbasdoc ";
        unionPart = unionPart + "left join bd_measdoc  ";
        unionPart = unionPart + "on bd_invbasdoc.pk_measdoc=bd_measdoc.pk_measdoc ";
        unionPart = unionPart + "where bd_invbasdoc.pk_invbasdoc='" + pk_invbasdoc + "') ";
        ref.getRefModel().setGroupPart(unionPart);
      }

      Object pk_assmeasdoc = getBillCardPanel().getBodyValueAt(rowindex, "cassistunit");
      if ((pk_assmeasdoc == null) || (pk_assmeasdoc.toString().trim().equals(""))) {
        getBillCardPanel().setCellEditable(e.getRow(), "measrate", false);
      }
      else {
        BD_ConvertVO[] convertVO = null;
        if ((pk_invbasdoc != null) && (pk_invbasdoc.toString().trim().length() > 0) && (pk_assmeasdoc != null) && (pk_assmeasdoc.toString().trim().length() > 0)) {
          convertVO = OrderHelper.findBd_Converts(new String[] { (String)pk_invbasdoc }, new String[] { (String)pk_assmeasdoc });
        }

        if ((convertVO != null) && (convertVO.length > 0))
        {
          UFBoolean fixedflag = convertVO[0].getBfixedflag();
          if (fixedflag == null) {
            getBillCardPanel().setCellEditable(e.getRow(), "measrate", false);
          }
          else if (fixedflag.booleanValue())
            getBillCardPanel().setCellEditable(e.getRow(), "measrate", false);
          else
            getBillCardPanel().setCellEditable(e.getRow(), "measrate", true);
        }
        else {
          getBillCardPanel().setCellEditable(e.getRow(), "measrate", false);
        }

      }

      InvVO invVO = new InvVO();
      invVO = (InvVO)getBillCardPanel().getBodyValueAt(rowindex, "invvo");
      if ((invVO == null) || (invVO.getFreeItemVO() == null) || (!BillEdit.definedFreeItem(invVO.getFreeItemVO()))) {
        getBillCardPanel().setCellEditable(rowindex, "vfree0", false);
      } else {
        getBillCardPanel().setCellEditable(rowindex, "vfree0", true);
        FreeItemRefPane freeRef = (FreeItemRefPane)getBillCardPanel().getBodyItem("vfree0").getComponent();
        freeRef.setFreeItemParam(invVO);
      }
    }
    catch (Exception t) {
      t.printStackTrace();
    }
  }

  private boolean calNativeAndAssistCurrValue(OrderVO orderVO)
  {
    String strRateDate = ((OrderHeaderVO)orderVO.getParentVO()).getDorderdate().toString();

    UFDouble dTemp = null;
    try {
      if (getCurrArith().getLocalCurrPK() == null) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("401201", "UPP401201-000036"));
        return false;
      }

      if ((getCurrArith().isBlnLocalFrac()) && (getCurrArith().getFracCurrPK() == null)) {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("401201", "UPP401201-000037"));
        return false;
      }

      OrderItemVO[] itemVOs = (OrderItemVO[])(OrderItemVO[])orderVO.getChildrenVO();
      for (int i = 0; i < itemVOs.length; i++) {
        OrderItemVO itemVO = itemVOs[i];

        UFDouble nmoney = getCurrArith().getAmountByOpp(itemVO.getCcurrencytypeid(), getCurrArith().getLocalCurrPK(), itemVO.getNoriginalcurmny(), itemVO.getNexchangeotobrate(), strRateDate);

        UFDouble nsummny = null;
        if (itemVO.getNoriginalsummny() != null)
        {
          nsummny = getCurrArith().getAmountByOpp(itemVO.getCcurrencytypeid(), getCurrArith().getLocalCurrPK(), itemVO.getNoriginalsummny(), itemVO.getNexchangeotobrate(), strRateDate);
        }

        itemVOs[i].setNmoney(nmoney);
        itemVOs[i].setNsummny(nsummny);
        itemVOs[i].setNtaxmny((nsummny == null ? new UFDouble(0) : nsummny).sub(nmoney == null ? new UFDouble(0) : nmoney));

        if ((!getCurrArith().isBlnLocalFrac()) || (getCurrArith().getLocalCurrPK().equals(itemVO.getCcurrencytypeid()))) {
          itemVOs[i].setNexchangeotoarate(dTemp);
          itemVOs[i].setNassistcurmny(dTemp);
          itemVOs[i].setNassistsummny(dTemp);
          itemVOs[i].setNassisttaxmny(dTemp);
        }
        else
        {
          nmoney = getCurrArith().getAmountByOpp(itemVO.getCcurrencytypeid(), getCurrArith().getFracCurrPK(), itemVO.getNoriginalcurmny(), itemVO.getNexchangeotoarate(), strRateDate);

          nsummny = getCurrArith().getAmountByOpp(itemVO.getCcurrencytypeid(), getCurrArith().getFracCurrPK(), itemVO.getNoriginalsummny(), itemVO.getNexchangeotoarate(), strRateDate);

          itemVOs[i].setNassistcurmny(nmoney);
          itemVOs[i].setNassistsummny(nsummny);
          itemVOs[i].setNassisttaxmny((nsummny == null ? new UFDouble(0) : nsummny).sub(nmoney == null ? new UFDouble(0) : nmoney));
        }
      }

      orderVO.setChildrenVO(itemVOs);
      this.m_curOrderVO = orderVO;
    }
    catch (Exception e)
    {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), e.getMessage());
      return false;
    }

    return true;
  }

  private void clearList()
  {
    getBillListPanel().getBodyBillModel().clearBodyData();
    getBillListPanel().getParentListPanel().clearSelect();
    getBillListPanel().getHeadTable().clearSelection();

    this.boEdit.setEnabled(false);
    this.boDel.setEnabled(false);
    this.boReturn.setEnabled(true);
    setGroupButtonsState(new UFBoolean("N"));

    int rowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
    if (rowCount > 0) {
      this.boDocument.setEnabled(true);
      this.m_btnOthersFuncs.setEnabled(true);
    }
    if (rowCount == 1) {
      this.boEdit.setEnabled(true);
      this.boDel.setEnabled(true);
      setGroupButtonsState(new UFBoolean("Y"));
    }

    updateButtons();
  }

  public void execHeadTailFormula(OrderVO vo)
  {
    if (vo == null) {
      return;
    }

    OrderHeaderVO voHead = (OrderHeaderVO)vo.getParentVO();
    UIRefPane refpane = null;

    String[] saFormula = { "getColValue(bd_calbody,bodyname,pk_calbody,cwareid)" };
    FormulaParse forParse = new FormulaParse();

    forParse.setExpressArray(saFormula);

    Hashtable[] htaVarry = new Hashtable[saFormula.length];
    int iFormulaLen = saFormula.length;
    for (int i = 0; i < iFormulaLen; i++) {
      htaVarry[i] = new Hashtable();
      htaVarry[i].put("cwareid", new String[] { StringUtil.toString(voHead.getCwareid()) });
    }

    forParse.setDataSArray(htaVarry);

    String[][] saRet = forParse.getValueSArray();

    refpane = (UIRefPane)getBillCardPanel().getHeadItem("cwareid").getComponent();

    if ((PuPubVO.getString_TrimZeroLenAsNull(refpane.getUITextField().getText()) == null) && 
      (saRet != null) && (saRet[0] != null))
      refpane.getUITextField().setText(saRet[0][0]);
  }

  public String getBillButtonAction(ButtonObject bo)
  {
    String strAction = null;
    if (bo == this.boAction)
      strAction = "scorder001";
    return strAction;
  }

  public String getBillButtonState()
  {
    return "scorder001";
  }

  private BillCardPanel getBillCardPanel()
  {
    if (this.m_BillCardPanel == null) {
      try
      {
        this.m_BillCardPanel = new BillCardPanel();
        this.m_BillCardPanel.setName("CardPanel");

        this.m_BillCardPanel.loadTemplet("61", null, getOperatorID(), getPk_corp());

        this.m_BillCardPanel.addEditListener(this);
        this.m_BillCardPanel.addBodyMouseListener(this);
        this.m_BillCardPanel.addBodyEditListener2(this);
        this.m_BillCardPanel.addBodyMenuListener(this);

        setNormalPrecision2();
        initComboBox();
        initRefer();

        ScTool.changeBillDataByUserDef(this.m_BillCardPanel, getPk_corp(), "61", getOperatorID());

        BillRowNo.loadRowNoItem(this.m_BillCardPanel, "crowno");

        BillItem l_billitem = this.m_BillCardPanel.getHeadItem("cvendormangid");
        UIRefPane pane = (UIRefPane)l_billitem.getComponent();
        pane.getRef().getRefModel().setRefNameField("bd_cubasdoc.custshortname");
      }
      catch (Throwable ivjExc)
      {
        SCMEnv.out(ivjExc.toString());
        handleException(ivjExc);
      }
    }
    return this.m_BillCardPanel;
  }

  private String getBillCode()
    throws Exception
  {
    try
    {
      String vordercode = getBillCardPanel().getHeadItem("vordercode").getValue();
      if ((vordercode == null) || (vordercode.toString().trim().equals(""))) {
        vordercode = null;
      }
      String cwareid = getBillCardPanel().getHeadItem("cwareid").getValue();
      String cdeptid = getBillCardPanel().getHeadItem("cdeptid").getValue();
      String cpurid = getBillCardPanel().getHeadItem("cpurorganization").getValue();
      String cbiztype = getBillCardPanel().getBusiType();

      BillCodeObjValueVO objVO = new BillCodeObjValueVO();
      objVO.setAttributeValue("公司", getPk_corp());
      objVO.setAttributeValue("采购组织", cpurid);
      objVO.setAttributeValue("库存组织", cwareid);
      objVO.setAttributeValue("部门", cdeptid);
      objVO.setAttributeValue("业务类型", cbiztype);

      String billcode = BillcodeRuleBO_Client.getBillCode("61", getPk_corp(), vordercode, objVO);
      getBillCardPanel().getHeadItem("vordercode").setValue(vordercode);
      getBillCardPanel().updateUI();
      return billcode;
    }
    catch (Exception e) {
      SCMEnv.out(e);
      
      //edit by yqq 2017-02-28 反编译改的
      e.getMessage();
    }throw new Exception();
 //   }throw new Exception(e.getMessage());
    
  }

  private OrderListPanel getBillListPanel()
  {
    if (this.m_ScOrderListPanel == null) {
      try {
        this.m_ScOrderListPanel = new OrderListPanel(this);
        this.m_ScOrderListPanel.setName("ListPanel");

        this.m_ScOrderListPanel.loadTemplet("61", null, getOperatorID(), getPk_corp());

        this.m_ScOrderListPanel.hideTableCol();
        this.m_ScOrderListPanel.updateUI();

        this.m_ScOrderListPanel.setNormalPrecision();

        ScTool.changeListDataByUserDef(this.m_ScOrderListPanel, getPk_corp(), "61", getOperatorID());

        this.m_ScOrderListPanel.addMouseListener(this);
        this.m_ScOrderListPanel.getHeadTable().getSelectionModel().addListSelectionListener(this.m_ScOrderListPanel);

        this.m_ScOrderListPanel.getHeadTable().setCellSelectionEnabled(false);
        this.m_ScOrderListPanel.getHeadTable().setSelectionMode(2);

        this.m_ScOrderListPanel.getChildListPanel().setTotalRowShow(true);

        this.m_ScOrderListPanel.getHeadBillModel().addSortRelaObjectListener(this);

        UIComboBox discountTaxType = (UIComboBox)this.m_ScOrderListPanel.getBodyItem("idiscounttaxtype").getComponent();
        discountTaxType.addItem(ScConstants.TaxType_Including);
        discountTaxType.addItem(ScConstants.TaxType_Not_Including);
        discountTaxType.addItem(ScConstants.TaxType_No);
        this.m_ScOrderListPanel.getBodyItem("idiscounttaxtype").setWithIndex(true);
      }
      catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }

    return this.m_ScOrderListPanel;
  }

  private CurrencyRateUtil getCurrArith()
  {
    if (this.m_CurrArith == null) {
      this.m_CurrArith = new CurrencyRateUtil(getPk_corp());
    }
    return this.m_CurrArith;
  }

  public String getModuleCode()
  {
    return "401201";
  }

  private String getOperatorID()
  {
    return ClientEnvironment.getInstance().getUser().getPrimaryKey();
  }

  private String getPk_corp()
  {
    return ClientEnvironment.getInstance().getCorporation().getPk_corp();
  }

  private OrderUIQueryDlg getQryCondition()
  {
    return getQryCondition(null);
  }

  private OrderUIQueryDlg getQryCondition(String strPkCorp)
  {
    if (this.m_query == null) {
      try {
        if ((strPkCorp != null) && (!strPkCorp.trim().equals("")))
          this.m_query = new OrderUIQueryDlg(this, NCLangRes.getInstance().getStrByID("401201", "UPP401201-000002"), getOperatorID(), strPkCorp, getModuleCode());
        else {
          this.m_query = new OrderUIQueryDlg(this, NCLangRes.getInstance().getStrByID("401201", "UPP401201-000002"), getOperatorID(), getPk_corp(), getModuleCode());
        }

        DefSetTool.updateQueryConditionClientUserDef(this.m_query, getPk_corp(), "61", "sc_order.vdef", "sc_order_b.vdef");
      }
      catch (Throwable ivjExc)
      {
        SCMEnv.out(ivjExc.toString());
        handleException(ivjExc);
      }
    }

    return this.m_query;
  }

  private ArrayList getSelectedBills()
  {
    ArrayList arrOrderVO = new ArrayList();

    if (this.m_billState == ScConstants.STATE_CARD) {
      onSelectNo();
      getBillListPanel().getHeadTable().setRowSelectionInterval(getM_iCurBillIndex(), getM_iCurBillIndex());
      getBillListPanel().getHeadBillModel().setRowState(getM_iCurBillIndex(), 4);
      showHintMessage("");
    }
    try
    {
      OrderHeaderVO[] l_SelOrderHeaderVOs = (OrderHeaderVO[])(OrderHeaderVO[])getBillListPanel().getHeadBillModel().getBodySelectedVOs("nc.vo.sc.order.OrderHeaderVO");

      if ((l_SelOrderHeaderVOs == null) || (l_SelOrderHeaderVOs.length <= 0)) {
        return null;
      }
      for (int i = 0; i < l_SelOrderHeaderVOs.length; i++) {
        OrderItemVO[] l_OrderItemVOs = (OrderItemVO[])(OrderItemVO[])OrderHelper.findByPrimaryKey(l_SelOrderHeaderVOs[i].getCorderid()).getChildrenVO();
        OrderVO order = new OrderVO();
        order.setParentVO(l_SelOrderHeaderVOs[i]);
        order.setChildrenVO(l_OrderItemVOs);
        arrOrderVO.add(order);
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000039"));
    }
    return arrOrderVO;
  }

  public int getSortTypeByBillItemKey(String sItemKey)
  {
    if ("crowno".equals(sItemKey)) {
      return 2;
    }

    return getBillCardPanel().getBillModel().getItemByKey(sItemKey).getDataType();
  }

  private String getStorAdd(String cstorhouseid)
  {
    String addr = null;
    try {
      addr = PublicHelper.getStorAddr(cstorhouseid);
    } catch (Exception e) {
    }
    return addr;
  }

  public String getTitle()
  {
    String title = NCLangRes.getInstance().getStrByID("401201", "UPP401201-000040");
    if (this.m_BillCardPanel != null)
      title = this.m_BillCardPanel.getTitle();
    return title;
  }

  public void handleException(Throwable exception)
  {
  }

  private void init()
  {
    setName("ScOrder");

    initpara();
    this.cardLayout = new CardLayout();
    setLayout(this.cardLayout);
    add(getBillCardPanel(), getBillCardPanel().getName());
    add(getBillListPanel(), getBillListPanel().getName());

    this.m_listVOs = new ArrayList();

    createBtnInstances();

    initButtons();
    initState();

    updateUI();

    PfUtilClient.retBusinessBtn(this.boBusitype, getPk_corp(), "61");

    if ((this.boBusitype.getChildButtonGroup() != null) && (this.boBusitype.getChildButtonGroup().length != 0)) {
      this.boBusitype.setCheckboxGroup(true);

      BillPanelTool.initBusiAddBtns(this.boBusitype, this.boAdd, "61", getPk_corp());

      if ((this.boBusitype != null) && (this.boBusitype.getSelectedChildButton() != null) && (this.boBusitype.getSelectedChildButton().length > 0)) {
        getBillCardPanel().setBusiType(this.boBusitype.getSelectedChildButton()[0].getTag());
      }
    }

    for (int i = 0; i < this.boAction.getChildButtonGroup().length; i++) {
      if ((this.boAction.getChildButtonGroup()[i] == null) || 
        (this.boAction.getChildButtonGroup()[i].getTag() == null)) continue;
      if (this.boAction.getChildButtonGroup()[i].getTag().equals("APPROVE"))
        this.boAudit = this.boAction.getChildButtonGroup()[i];
      if (this.boAction.getChildButtonGroup()[i].getTag().equals("UNAPPROVE"))
        this.boUnaudit = this.boAction.getChildButtonGroup()[i];
    }
  }

  public void initpara()
  {
    try
    {
      Hashtable t = SysInitBO_Client.queryBatchParaValues(getPk_corp(), new String[] { "SC01", "SC04", "SC09", "SC10", "BD301" });
      if ((t != null) && (t.size() > 0)) {
        Object temp = null;
        if (t.get("SC01") != null)
        {
          temp = t.get("SC01");
          if ((temp.toString().trim().equals("是")) || (temp.toString().trim().equalsIgnoreCase("Y"))) {
            this.bWastControl = true;
          }
        }

        if (t.get("SC04") != null)
        {
          temp = t.get("SC04");
          if (temp.toString().trim().equals("无税价格优先"))
            this.m_iPricePolicy = 6;
          else {
            this.m_iPricePolicy = 5;
          }
        }

        if (t.get("SC09") != null)
        {
          temp = t.get("SC09");
          if ((temp.toString().trim().equals("是")) || (temp.toString().trim().equals("Y"))) {
            this.mbol_KeepIniMaker = true;
          }
        }

        if (t.get("SC10") != null)
        {
          temp = t.get("SC10");
          this.m_strPriceDspValue = temp.toString();
        }

        if (t.get("BD301") != null)
        {
          temp = t.get("BD301");
          this.m_sPKCurrencyType = temp.toString();
        }

      }

      Object[] objs = null;
      ServcallVO[] scds = new ServcallVO[3];

      scds[0] = new ServcallVO();
      scds[0].setBeanName("nc.itf.uap.sf.ICreateCorpQueryService");
      scds[0].setMethodName("isEnabled");
      scds[0].setParameter(new Object[] { getPk_corp(), "CT" });
      scds[0].setParameterTypes(new Class[] { String.class, String.class });

      scds[1] = new ServcallVO();
      scds[1].setBeanName("nc.itf.sc.IPublic");
      scds[1].setMethodName("getDigitBatch");
      
      //edit by yqq 2017-02-28 反编译改的
      scds[1].setParameter(new Object[] { getPk_corp(), new String[] { "BD501", "BD502", "BD503", "BD505" } });
      scds[1].setParameterTypes(new Class[] { String.class, java.lang.String.class });
   //   scds[1].setParameter(new Object[] { getPk_corp(), { "BD501", "BD502", "BD503", "BD505" } });
  //    scds[1].setParameterTypes(new Class[] { String.class, [Ljava.lang.String.class });

      scds[2] = new ServcallVO();
      scds[2].setBeanName("nc.itf.sc.IPublic");
      scds[2].setMethodName("getCurrDigitByPKCorp");
      scds[2].setParameter(new Object[] { getPk_corp() });
      scds[2].setParameterTypes(new Class[] { String.class });

      objs = LocalCallService.callService(scds);

      this.m_bCTStartUp = ((Boolean)objs[0]).booleanValue();

      this.m_bAutoSendToAudit = false;

      int[] lInt_ary = (int[])(int[])objs[1];
      if ((lInt_ary != null) && (lInt_ary.length == 4)) {
        this.mintary_precisions[0] = lInt_ary[0];
        this.mintary_precisions[1] = lInt_ary[1];
        this.mintary_precisions[2] = lInt_ary[2];
        this.mintary_precisions[3] = lInt_ary[3];
      }

      String s = (String)objs[2];
      if ((s != null) && (s.length() > 0)) {
        this.mint_localmnyPrecision = Integer.parseInt(s.trim());
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("40040502", "UPP40040502-000052"), e.getMessage());
      return;
    }
  }

  private void initButtons()
  {
    if (this.boAction.getChildCount() == 0) {
      this.boAudit.setTag("APPROVE");
      this.boUnaudit.setTag("UNAPPROVE");
    }

    if (this.m_billState == ScConstants.STATE_LIST) {
      setButtons(this.m_btnTree.getButtonArray());
      this.boReturn.setName(NCLangRes.getInstance().getStrByID("common", "UCH021"));
    }
    else {
      setButtons(this.m_btnTree.getButtonArray());
      this.boReturn.setName(NCLangRes.getInstance().getStrByID("common", "UCH022"));
    }
  }

  private void initComboBox()
  {
    try
    {
      UIComboBox discountTaxType = (UIComboBox)getBillCardPanel().getBodyItem("idiscounttaxtype").getComponent();

      discountTaxType.addItem(ScConstants.TaxType_Including);
      discountTaxType.addItem(ScConstants.TaxType_Not_Including);
      discountTaxType.addItem(ScConstants.TaxType_No);
      getBillCardPanel().getBodyItem("idiscounttaxtype").setWithIndex(true);
    } catch (Exception e) {
      SCMEnv.out(e);
    }
  }

  private void processAfterChange(String sUpBillType, AggregatedValueObject[] voaSourceVO)
  {
    if (sUpBillType.equals("28"))
    {
      OrderVO[] voaRet = null;

      PriceauditMergeVO[] voaAskBill = (PriceauditMergeVO[])(PriceauditMergeVO[])SplitBillVOs.getSplitVOs(PriceauditMergeVO.class.getName(), PriceauditHeaderVO.class.getName(), PriceauditItemMergeVO.class.getName(), (AggregatedValueObject[])voaSourceVO, null, new String[] { "cbiztype", "cvendormangid" });
      try
      {
        voaRet = (OrderVO[])(OrderVO[])PfChangeBO_Client.pfChangeBillToBillArray(voaAskBill, "28", "61");
      }
      catch (Exception e)
      {
        PuTool.outException(this, e);
        voaRet = null;
      }

      if (voaRet == null) {
        return;
      }

      onAddFromRef(voaRet);

      SourceBillTool.loadSourceInfoAll(getBillListPanel().getBodyBillModel(), "61");
    }
  }

  private void initRefer()
  {
    UIRefPane ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("cvendorid").getComponent();
    ref.setWhereString(" bd_cumandoc.pk_corp='" + getPk_corp() + "' and bd_cumandoc.frozenflag='N' and (bd_cumandoc.custflag='1' or bd_cumandoc.custflag='3' )");

    ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("creciever").getComponent();
    ref.setWhereString(" bd_cumandoc.frozenflag='N' and  bd_cumandoc.pk_corp='" + getPk_corp() + "' AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='2') ");

    ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("cgiveinvoicevendor").getComponent();
    ref.setWhereString("  bd_cumandoc.frozenflag='N' and bd_cumandoc.pk_corp='" + getPk_corp() + "' AND (bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='3') ");

    ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("cpurorganization").getComponent();
    ref.getRefModel().addWherePart(" and bd_purorg.ownercorp='" + getPk_corp() + "' ");

    ref = (UIRefPane)getBillCardPanel().getBodyItem("cinventorycode").getComponent();
    ref.setWhereString(" bd_invmandoc.pk_corp='" + getPk_corp() + "' and bd_invbasdoc.laborflag='N' and bd_invmandoc.sealflag='N' ");
    ref.setTreeGridNodeMultiSelected(true);
    ref.setMultiSelectedEnabled(true);

    ref = (UIRefPane)getBillCardPanel().getBodyItem("cwarehousename").getComponent();
    ref.setReturnCode(false);
    ref.setRefInputType(1);
    ref.setWhereString(" bd_stordoc.gubflag='N' and bd_stordoc.sealflag='N' and bd_stordoc.pk_corp='" + getPk_corp() + "' ");

    ref = (UIRefPane)getBillCardPanel().getBodyItem("cprojectname").getComponent();
    ref.setReturnCode(false);
    ref.setRefInputType(1);

    ref.setWhereString(" bd_jobmngfil.pk_corp ='" + getPk_corp() + "' ");

    ref = (UIRefPane)getBillCardPanel().getBodyItem("ccurrencytype").getComponent();
    ref.setReturnCode(false);
    ref.setRefInputType(1);

    ref = new UIRefPane();
    ref.setIsCustomDefined(true);
    ref.setRefModel(new OtherRefModel(OtherRefModel.REF_ASSMEAS));
    ref.setCacheEnabled(false);
    ref.setReturnCode(false);
    ref.setRefInputType(1);
    getBillCardPanel().getBodyItem("cassistunitname").setComponent(ref);

    ref = new UIRefPane();
    ref.setIsCustomDefined(true);

    ref.setRefModel(new ProjectPhase());
    ref.setCacheEnabled(false);
    ref.setReturnCode(false);
    ref.setRefInputType(1);
    getBillCardPanel().getBodyItem("cprojectphasename").setComponent(ref);

    ref = new UIRefPane();
    ref.setIsCustomDefined(true);
    ref.setRefModel(new OtherRefModel(OtherRefModel.REF_CUSTBANK));
    ref.setCacheEnabled(false);
    ref.setReturnCode(false);
    ref.setRefInputType(0);
    ref.setWhereString(" bd_custbank.pk_cubasdoc is null ");
    getBillCardPanel().getHeadItem("caccountbankid").setComponent(ref);

    ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("vmemo").getComponent();
    ref.setAutoCheck(false);
    ref.setReturnCode(false);

    ref = (UIRefPane)this.m_BillCardPanel.getBodyItem("vmemo").getComponent();
    ref.setTable(getBillCardPanel().getBillTable());
    ref.getRefModel().setRefCodeField(ref.getRefModel().getRefNameField());
    ref.getRefModel().setBlurFields(new String[] { ref.getRefModel().getRefNameField() });
    ref.setAutoCheck(false);

    BillData billData = this.m_BillCardPanel.getBillData();

    FreeItemRefPane freeRef = new FreeItemRefPane();
    freeRef.setMaxLength(billData.getBodyItem("vfree0").getLength());
    billData.getBodyItem("vfree0").setComponent(freeRef);

    LotNumbRefPane lotRef = new LotNumbRefPane();
    lotRef.setMaxLength(billData.getBodyItem("vproducenum").getLength());
    billData.getBodyItem("vproducenum").setComponent(lotRef);

    this.m_BillCardPanel.setBillData(billData);
  }

  private void initState()
  {
    getBillCardPanel().setTatolRowShow(true);

    getBillCardPanel().setEnabled(false);

    this.cardLayout.first(this);

    setButtonsState();

    updateButtons();
  }

  private void loadCustBank()
  {
    getBillCardPanel().execHeadFormula("cvendorid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)");
    String cvendorid = getBillCardPanel().getHeadItem("cvendorid").getValue();
    if (cvendorid != null) {
      UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("caccountbankid").getComponent();
      ref.setWhereString(" bd_custbank.pk_cubasdoc='" + cvendorid + "'");
      ref.getRefModel().reloadData();
    }
  }

  private void loadData(String billID)
  {
    try
    {
      long s = System.currentTimeMillis();
      if (billID != null) {
        this.m_curOrderVO = OrderHelper.findByPrimaryKey(billID);
        if (this.m_listVOs == null) this.m_listVOs = new ArrayList();
        this.m_listVOs.add(this.m_curOrderVO.getParentVO());
      } else {
        this.m_curOrderVO = OrderHelper.findByPrimaryKey(((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid());
      }

      this.m_curOrderVO.calPriceIncTax();
      getBillCardPanel().setBillValueVO(this.m_curOrderVO);
      loadCustBank();
      getBillCardPanel().setBillValueVO(this.m_curOrderVO);

      OrderHeaderVO voHead = (OrderHeaderVO)this.m_curOrderVO.getParentVO();
      String[] saKey = { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10" };
      int iLen = saKey.length;
      for (int i = 0; i < iLen; i++) {
        JComponent component = getBillCardPanel().getHeadItem(saKey[i]).getComponent();
        if ((component instanceof UIRefPane)) {
          voHead.setAttributeValue(saKey[i], ((UIRefPane)component).getText());
        }

      }

      getBillCardPanel().setBillValueVO(this.m_curOrderVO);

      loadPrecision();

      getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(8);
      getBillCardPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(8);
      getBillCardPanel().setBillValueVO(this.m_curOrderVO);
      loadPrecision();

      loadMeasRate();
      loadFreeItems();

      this.m_sHeadVmemo = ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getVmemo();
      UIRefPane ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("vmemo").getComponent();
      ref.setText(this.m_sHeadVmemo);

      long s1 = System.currentTimeMillis();

      getBillCardPanel().getBillModel().execLoadFormula();
      SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

      OrderItemVO[] orderItemVO = (OrderItemVO[])(OrderItemVO[])this.m_curOrderVO.getChildrenVO();
      for (int i = 0; i < orderItemVO.length; i++) {
        Integer idiscounttaxtype = orderItemVO[i].getIdiscounttaxtype();

        getBillCardPanel().getBillModel().setValueAt(idiscounttaxtype, i, "idiscounttaxtype");
      }

      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000253"));

      SCMEnv.out("数据加载：[共用时" + (System.currentTimeMillis() - s) / 1000L + "秒]");

      int ibillstate = ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getIbillstatus().intValue();

      String cbiztype = voHead.getCbiztype();
      if ((cbiztype != null) && (cbiztype.trim().length() > 0))
      {
        BillPanelTool.initBusiAddBtns(this.boBusitype, this.boAdd, "61", getPk_corp());

        updateButtons();
      }
      for (int i = 0; i < this.boAction.getChildButtonGroup().length; i++) {
        if ((this.boAction.getChildButtonGroup()[i] == null) || 
          (this.boAction.getChildButtonGroup()[i].getTag() == null)) continue;
        if (this.boAction.getChildButtonGroup()[i].getTag().equals("APPROVE"))
          this.boAudit = this.boAction.getChildButtonGroup()[i];
        if (this.boAction.getChildButtonGroup()[i].getTag().equals("UNAPPROVE")) {
          this.boUnaudit = this.boAction.getChildButtonGroup()[i];
        }
      }

      setOperateState(ibillstate);

      if ((!this.m_bAutoSendToAudit) && (ScTool.isNeedSendToAudit("61", this.m_curOrderVO)) && (this.m_billState == ScConstants.STATE_CARD))
        this.boSendAudit.setEnabled(true);
      else {
        this.boSendAudit.setEnabled(false);
      }
      updateButtons();

      SourceBillTool.loadSourceInfoAll(getBillCardPanel().getBillModel(), "61");

      execHeadTailFormula(this.m_curOrderVO);
    }
    catch (ValidationException e) {
      showErrorMessage(e.getMessage());
    } catch (Exception e) {
      SCMEnv.out("数据加载失败");
      SCMEnv.out(e);
    }
  }

  private void loadFreeItems()
  {
    try
    {
      int num = getBillCardPanel().getRowCount();
      ArrayList list = new ArrayList();
      for (int i = 0; i < num; i++) {
        list.add(getBillCardPanel().getBodyValueAt(i, "cmangid"));
      }
      list = AdjustbillHelper.queryFreeVOByInvIDs(list);

      if ((list == null) || (list.size() <= 0))
        return;
      InvVO invVO = new InvVO();
      FreeVO freeVO = new FreeVO();
      for (int i = 0; i < num; i++) {
        freeVO = (FreeVO)list.get(i);
        if (freeVO.getCinventoryid() == null)
          continue;
        freeVO.setVfree1(getBillCardPanel().getBodyValueAt(i, "vfree1") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree1").toString());
        freeVO.setVfree2(getBillCardPanel().getBodyValueAt(i, "vfree2") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree2").toString());
        freeVO.setVfree3(getBillCardPanel().getBodyValueAt(i, "vfree3") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree3").toString());
        freeVO.setVfree4(getBillCardPanel().getBodyValueAt(i, "vfree4") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree4").toString());
        freeVO.setVfree5(getBillCardPanel().getBodyValueAt(i, "vfree5") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree5").toString());

        Object pk_invmangdoc = getBillCardPanel().getBodyValueAt(i, "cmangid");
        Object pk_invbasedoc = getBillCardPanel().getBodyValueAt(i, "cbaseid");
        Object cinventorycode = getBillCardPanel().getBodyValueAt(i, "cinventorycode");
        Object cinventoryname = getBillCardPanel().getBodyValueAt(i, "cinventoryname");
        Object invspec = getBillCardPanel().getBodyValueAt(i, "invspec");
        Object invtype = getBillCardPanel().getBodyValueAt(i, "invtype");

        invVO = new InvVO();
        invVO.setFreeItemVO(freeVO);

        invVO.setCinvmanid(pk_invbasedoc.toString());
        invVO.setCinventoryid(pk_invmangdoc.toString());

        invVO.setIsFreeItemMgt(new Integer(1));
        invVO.setCinventorycode(cinventorycode == null ? "" : cinventorycode.toString());
        invVO.setInvname(cinventoryname == null ? null : cinventoryname.toString());
        invVO.setInvspec(invspec == null ? null : invspec.toString());
        invVO.setInvtype(invtype == null ? null : invtype.toString());

        getBillCardPanel().setBodyValueAt(freeVO.getVfree0(), i, "vfree0");
        getBillCardPanel().setBodyValueAt(invVO, i, "invvo");
      }
    }
    catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000022"));
      SCMEnv.out(e);
    }
  }

  private void loadFreeItems(ArrayList list)
  {
    try
    {
      int num = getBillCardPanel().getRowCount();

      if ((list == null) || (list.size() <= 0))
        return;
      InvVO invVO = new InvVO();
      FreeVO freeVO = new FreeVO();
      for (int i = 0; i < num; i++) {
        freeVO = (FreeVO)list.get(i);
        if (freeVO.getCinventoryid() == null)
          continue;
        freeVO.setVfree1(getBillCardPanel().getBodyValueAt(i, "vfree1") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree1").toString());
        freeVO.setVfree2(getBillCardPanel().getBodyValueAt(i, "vfree2") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree2").toString());
        freeVO.setVfree3(getBillCardPanel().getBodyValueAt(i, "vfree3") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree3").toString());
        freeVO.setVfree4(getBillCardPanel().getBodyValueAt(i, "vfree4") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree4").toString());
        freeVO.setVfree5(getBillCardPanel().getBodyValueAt(i, "vfree5") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree5").toString());

        Object pk_invmangdoc = getBillCardPanel().getBodyValueAt(i, "cmangid");
        Object pk_invbasedoc = getBillCardPanel().getBodyValueAt(i, "cbaseid");
        Object cinventorycode = getBillCardPanel().getBodyValueAt(i, "cinventorycode");
        Object cinventoryname = getBillCardPanel().getBodyValueAt(i, "cinventoryname");
        Object invspec = getBillCardPanel().getBodyValueAt(i, "invspec");
        Object invtype = getBillCardPanel().getBodyValueAt(i, "invtype");

        invVO = new InvVO();

        invVO.setFreeItemVO(freeVO);

        invVO.setCinvmanid(pk_invbasedoc.toString());
        invVO.setCinventoryid(pk_invmangdoc.toString());

        invVO.setIsFreeItemMgt(new Integer(1));
        invVO.setCinventorycode(cinventorycode == null ? "" : cinventorycode.toString());
        invVO.setInvname(cinventoryname == null ? null : cinventoryname.toString());
        invVO.setInvspec(invspec == null ? null : invspec.toString());
        invVO.setInvtype(invtype == null ? null : invtype.toString());

        getBillCardPanel().setBodyValueAt(freeVO.getVfree0(), i, "vfree0");
        getBillCardPanel().setBodyValueAt(invVO, i, "invvo");
      }
    }
    catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000022"));
      SCMEnv.out(e);
    }
  }

  private void loadFreeItemsAlready(ArrayList list)
  {
    try
    {
      int num = getBillCardPanel().getRowCount();

      if ((list == null) || (list.size() <= 0))
        return;
      InvVO invVO = new InvVO();
      FreeVO freeVO = new FreeVO();
      for (int i = 0; i < num; i++) {
        freeVO = (FreeVO)list.get(i);
        if (freeVO.getCinventoryid() == null)
          continue;
        freeVO.setVfree1(getBillCardPanel().getBodyValueAt(i, "vfree1") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree1").toString());
        freeVO.setVfree2(getBillCardPanel().getBodyValueAt(i, "vfree2") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree2").toString());
        freeVO.setVfree3(getBillCardPanel().getBodyValueAt(i, "vfree3") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree3").toString());
        freeVO.setVfree4(getBillCardPanel().getBodyValueAt(i, "vfree4") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree4").toString());
        freeVO.setVfree5(getBillCardPanel().getBodyValueAt(i, "vfree5") == null ? "" : getBillCardPanel().getBodyValueAt(i, "vfree5").toString());

        Object pk_invmangdoc = getBillCardPanel().getBodyValueAt(i, "cmangid");
        Object pk_invbasedoc = getBillCardPanel().getBodyValueAt(i, "cbaseid");
        Object cinventorycode = getBillCardPanel().getBodyValueAt(i, "cinventorycode");
        Object cinventoryname = getBillCardPanel().getBodyValueAt(i, "cinventoryname");
        Object invspec = getBillCardPanel().getBodyValueAt(i, "invspec");
        Object invtype = getBillCardPanel().getBodyValueAt(i, "invtype");

        invVO = new InvVO();
        invVO.setFreeItemVO(freeVO);

        invVO.setCinvmanid(pk_invbasedoc.toString());
        invVO.setCinventoryid(pk_invmangdoc.toString());

        invVO.setIsFreeItemMgt(new Integer(1));
        invVO.setCinventorycode(cinventorycode == null ? "" : cinventorycode.toString());
        invVO.setInvname(cinventoryname == null ? null : cinventoryname.toString());
        invVO.setInvspec(invspec == null ? null : invspec.toString());
        invVO.setInvtype(invtype == null ? null : invtype.toString());

        getBillCardPanel().setBodyValueAt(freeVO.getVfree0(), i, "vfree0");
        getBillCardPanel().setBodyValueAt(invVO, i, "invvo");
      }
    }
    catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000022"));
      SCMEnv.out(e);
    }
  }

  private void loadMeasRate()
  {
    try
    {
      int rowcount = getBillCardPanel().getRowCount();

      Vector vTemp1 = new Vector();
      Vector vTemp2 = new Vector();
      for (int i = 0; i < rowcount; i++) {
        Object pk_invbasdoc = getBillCardPanel().getBodyValueAt(i, "cbaseid");
        Object pk_measdoc = getBillCardPanel().getBodyValueAt(i, "cassistunit");
        if ((pk_invbasdoc != null) && (pk_measdoc != null)) {
          vTemp1.addElement((String)pk_invbasdoc);
          vTemp2.addElement((String)pk_measdoc);
        }
      }
      String[] cbaseid = new String[vTemp1.size()];
      String[] cmeasid = new String[vTemp2.size()];
      vTemp1.copyInto(cbaseid);
      vTemp2.copyInto(cmeasid);
      BD_ConvertVO[] convertVO = OrderHelper.findBd_Converts(cbaseid, cmeasid);
      int j = 0;

      for (int i = 0; i < rowcount; i++) {
        Object pk_invbasdoc = getBillCardPanel().getBodyValueAt(i, "cbaseid");
        Object pk_measdoc = getBillCardPanel().getBodyValueAt(i, "cassistunit");
        Object nordernum = getBillCardPanel().getBodyValueAt(i, "nordernum");
        Object nassistnum = getBillCardPanel().getBodyValueAt(i, "nassistnum");

        BD_ConvertVO convertTempVO = null;
        if ((pk_invbasdoc != null) && (pk_invbasdoc.toString().trim().length() > 0) && (pk_measdoc != null) && (pk_measdoc.toString().trim().length() > 0)) {
          convertTempVO = convertVO[j];
          j++;
        }

        if ((nordernum != null) && (nassistnum != null) && (nordernum.toString().trim().length() != 0) && (nassistnum.toString().trim().length() != 0) && 
          (convertTempVO != null) && (!convertTempVO.getBfixedflag().booleanValue())) {
          UFDouble x = new UFDouble(nordernum.toString());
          UFDouble y = new UFDouble(nassistnum.toString());
          UFDouble rate = x.div(y);
          getBillCardPanel().setBodyValueAt(rate, i, "measrate");
        }
        else if (convertTempVO == null) {
          getBillCardPanel().setBodyValueAt("", i, "measrate");
        }
        else {
          getBillCardPanel().setBodyValueAt(convertTempVO.getNmainmeasrate(), i, "measrate");
        }
      }
    }
    catch (Exception t)
    {
    }
  }

  private void loadMeasRate(BD_ConvertVO[] convertVO) {
    try {
      int rowcount = getBillCardPanel().getRowCount();

      int j = 0;

      for (int i = 0; i < rowcount; i++) {
        Object pk_invbasdoc = getBillCardPanel().getBodyValueAt(i, "cbaseid");
        Object pk_measdoc = getBillCardPanel().getBodyValueAt(i, "cassistunit");
        Object nordernum = getBillCardPanel().getBodyValueAt(i, "nordernum");
        Object nassistnum = getBillCardPanel().getBodyValueAt(i, "nassistnum");
        Object pk_mainbeasdoc = getBillCardPanel().getBodyValueAt(i, "pk_measdoc");

        BD_ConvertVO convertTempVO = null;
        if ((pk_invbasdoc != null) && (pk_invbasdoc.toString().trim().length() > 0) && (pk_measdoc != null) && (pk_measdoc.toString().trim().length() > 0)) {
          convertTempVO = convertVO[j];
          if (pk_measdoc.toString().trim().equals(pk_mainbeasdoc.toString().trim())) {
            convertTempVO.setBfixedflag(new UFBoolean("Y"));
            convertTempVO.setNmainmeasrate(new UFDouble(1));
          }
          j++;
        }

        if ((nordernum != null) && (nassistnum != null) && (nordernum.toString().trim().length() != 0) && (nassistnum.toString().trim().length() != 0) && 
          (convertTempVO != null) && (!convertTempVO.getBfixedflag().booleanValue())) {
          UFDouble x = new UFDouble(nordernum.toString());
          UFDouble y = new UFDouble(nassistnum.toString());
          UFDouble rate = x.div(y);
          getBillCardPanel().setBodyValueAt(rate, i, "measrate");
        }
        else if (convertTempVO == null) {
          getBillCardPanel().setBodyValueAt("", i, "measrate");
        }
        else {
          getBillCardPanel().setBodyValueAt(convertTempVO.getNmainmeasrate(), i, "measrate");
        }
      }
    }
    catch (Exception t)
    {
    }
  }

  private void loadPrecision()
  {
    try
    {
      boolean isBlnLocalFrac = getCurrArith().isBlnLocalFrac();

      String localCur = getCurrArith().getLocalCurrPK();

      String fracCur = getCurrArith().getFracCurrPK();

      int num = getBillCardPanel().getRowCount();
      for (int rowindex = 0; rowindex < num; rowindex++)
      {
        Object ccurrency = getBillCardPanel().getBodyValueAt(rowindex, "ccurrencytypeid");
        if ((ccurrency == null) || (ccurrency.toString().trim().equals("")))
        {
          continue;
        }
        boolean isLocal = localCur.equals(ccurrency);

        boolean isFrac = fracCur != null;

        if (isBlnLocalFrac) {
          if (isLocal)
          {
            Integer localPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(getCurrArith().getLocalCurrPK()).getCurrbusidigit();
            getBillCardPanel().setBodyValueAt(localPrecision, rowindex, "localprecision");
            getBillCardPanel().setBodyValueAt("0", rowindex, "fracprecision");
          }
          else
          {
            Integer localPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(getCurrArith().getFracCurrPK()).getCurrbusidigit();
            getBillCardPanel().setBodyValueAt(localPrecision, rowindex, "localprecision");

            if (isFrac) {
              getBillCardPanel().setBodyValueAt(localPrecision, rowindex, "fracprecision");
            }
            else
            {
              Integer fracPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(ccurrency.toString()).getCurrbusidigit();
              getBillCardPanel().setBodyValueAt(fracPrecision, rowindex, "fracprecision");
            }
          }

        }
        else
        {
          getBillCardPanel().setBodyValueAt("2", rowindex, "fracprecision");
          if (isLocal)
          {
            Integer localpresion = CurrtypeQuery.getInstance().getCurrtypeVO(getCurrArith().getLocalCurrPK()).getCurrbusidigit();
            getBillCardPanel().setBodyValueAt(localpresion, rowindex, "localprecision");
          }
          else
          {
            Integer localPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(ccurrency.toString()).getCurrbusidigit();
            getBillCardPanel().setBodyValueAt(localPrecision, rowindex, "localprecision");
          }

        }

        Integer currPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(ccurrency.toString()).getCurrbusidigit();
        getBillCardPanel().setBodyValueAt(currPrecision, rowindex, "mnyprecision");
      }

      if (!this.mbol_HasGetDecimal)
      {
        this.m_intCurrencyDecimal = CurrtypeQuery.getInstance().getCurrtypeVO(localCur).getCurrbusidigit().intValue();
        if (fracCur != null) this.m_intCurrencyDecimalAssit = CurrtypeQuery.getInstance().getCurrtypeVO(fracCur).getCurrbusidigit().intValue();

        this.mbol_HasGetDecimal = true;
      }

      setChangePrecision();
    }
    catch (Exception e) {
      SCMEnv.out(e);
      showErrorMessage("" + e.getMessage());
    }
  }

  private void loadReferListByCust(OrderVO oneVO)
  {
    if (oneVO == null)
      return;
    OrderHeaderVO headVO = (OrderHeaderVO)oneVO.getParentVO();
    OrderItemVO[] itemVOs = (OrderItemVO[])(OrderItemVO[])oneVO.getChildrenVO();
    String cvendormangid = ((OrderHeaderVO)oneVO.getParentVO()).getCvendormangid();
    if ((cvendormangid == null) || (cvendormangid.trim().equals(""))) {
      return;
    }
    try
    {
      CustDefaultVO custVO = OrderBHelper.getDefaultByCust(cvendormangid.toString());

      if (PuPubVO.getString_TrimZeroLenAsNull(custVO.getCdeptid()) == null)
        headVO.setCdeptid(((OrderHeaderVO)oneVO.getParentVO()).getCdeptid());
      else {
        headVO.setCdeptid(custVO.getCdeptid());
      }

      headVO.setCgiveinvoicevendor(custVO.getCcumandoc2());
      headVO.setCtermProtocolid(custVO.getCpayterm());
      headVO.setCtransmodeid(custVO.getCsendtype());
      headVO.setCvendorid(custVO.getCvendorid());
      String curencytype = custVO.getCcurrtype();
      for (int i = 0; i < itemVOs.length; i++) {
        String ccurencytype = itemVOs[i].getCcurrencytypeid();
        if ((ccurencytype == null) || (ccurencytype.trim().equals(""))) {
          itemVOs[i].setCcurrencytypeid(curencytype);
        }
      }
      String cbankid = PublicHelper.getCbankid(cvendormangid);
      headVO.setCaccountbankid(cbankid);

      getBillCardPanel().updateUI();
    } catch (Exception t) {
      t.printStackTrace();
    }
  }

  private void loadReferListByInvNew(OrderItemVO[] itemVO)
  {
    if ((itemVO == null) || (itemVO.length == 0))
      return;
    try
    {
      Vector vTemp = new Vector();
      for (int i = 0; i < itemVO.length; i++) {
        if (itemVO[i].getNtaxrate() == null) {
          vTemp.addElement(itemVO[i].getCbaseid());
        }
      }
      String[] cbaseid = new String[vTemp.size()];
      vTemp.copyInto(cbaseid);

      UFDouble[] taxrate = PublicHelper.getTaxRates(cbaseid);

      if ((taxrate != null) && (taxrate.length > 0)) {
        for (int i = 0; i < itemVO.length; i++) {
          if (itemVO[i].getNtaxrate() == null)
            itemVO[i].setNtaxrate(taxrate[i]);
        }
      }
    }
    catch (Exception t)
    {
      t.printStackTrace();
    }
  }

  public void mouse_doubleclick(BillMouseEnent e)
  {
    if ((e.getPos() == 0) && (this.m_billState == ScConstants.STATE_OTHER)) {
      refLoadData(e.getRow());
      return;
    }

    if (e.getPos() == 0)
      onDoubleClick();
    else if ((this.m_billState == ScConstants.STATE_ADD) || (this.m_billState != ScConstants.STATE_MODIFY));
  }

  private void onAdd()
  {
    getBillCardPanel().addNew();
    getBillCardPanel().setEnabled(true);
    getBillCardPanel().setTatolRowShow(true);
    try
    {
      getBillCardPanel().getHeadItem("dorderdate").setValue(ClientEnvironment.getInstance().getDate().toString());
      getBillCardPanel().getHeadItem("pk_corp").setValue(getPk_corp());

      if ((getBillCardPanel().getBusiType() != null) && (getBillCardPanel().getBusiType().trim().length() > 0)) {
        getBillCardPanel().getHeadItem("cbiztype").setValue(getBillCardPanel().getBusiType());
      } else {
        UIRefPane nRefPanel2 = (UIRefPane)getBillCardPanel().getHeadItem("cbiztype").getComponent();
        nRefPanel2.setValue(this.boBusitype.getSelectedChildButton()[0].getTag());
      }

      getBillCardPanel().getHeadItem("caccountyear").setValue(ClientEnvironment.getInstance().getAccountYear());

      getBillCardPanel().getTailItem("coperator").setValue(getOperatorID());

      setDefaultValueByUser();
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    this.m_billState = ScConstants.STATE_ADD;
    setButtonsState();

    rightButtonRightControl();
    onAppendLine();
    this.cardLayout.first(this);
    showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000071"));
  }

  private void onAddFromRef(OrderVO[] VOs)
  {
    if ((VOs == null) || (VOs.length == 0)) {
      return;
    }

    BillRowNo.setVOsRowNoByRule(VOs, "61", "crowno");

    this.m_billState = ScConstants.STATE_OTHER;
    this.cardLayout.last(this);
    setButtons(this.m_btnTree.getButtonArray());
    updateUI();
    this.boEdit.setEnabled(true);
    setButtonsState();

    getBillListPanel().setState(this.m_billState);

    for (int i = 0; i < VOs.length; i++)
    {
      OrderVO oneVO = VOs[i];

      ((OrderHeaderVO)oneVO.getParentVO()).setDorderdate(ClientEnvironment.getInstance().getDate());
      ((OrderHeaderVO)oneVO.getParentVO()).setCoperator(getOperatorID());

      ((OrderHeaderVO)oneVO.getParentVO()).setCaccountyear(ClientEnvironment.getInstance().getAccountYear());

      loadReferListByCust(oneVO);
      String recieve = ((OrderHeaderVO)oneVO.getParentVO()).getCreciever();

      OrderItemVO[] itemVO = (OrderItemVO[])(OrderItemVO[])oneVO.getChildrenVO();

      loadReferListByInvNew(itemVO);

      for (int j = 0; j < itemVO.length; j++) {
        itemVO[j].setIdiscounttaxtype(new Integer(1));
        itemVO[j].setBisactive(new UFBoolean(true));

        if (recieve != null)
          continue;
        String warehouseid = itemVO[j].getCwarehouseid();
        if ((warehouseid == null) || (warehouseid.trim().equals(""))) {
          continue;
        }
        String storaddr = itemVO[j].getCreceiveaddress();
        if ((storaddr == null) || (storaddr.trim().equals(""))) {
          String storadd = getStorAdd(warehouseid);
          itemVO[j].setCreceiveaddress(storadd);
        }

      }

    }

    int num = VOs.length;
    ArrayList vecVO = new ArrayList();
    for (int i = 0; i < num; i++) {
      vecVO.add(VOs[i]);
    }
    this.comeVOs = vecVO;
    getBillListPanel().setComeVO(vecVO);
    getBillListPanel().loadHeadData();
    getBillListPanel().loadBodyData(0);
  }

  private void onAppendLine()
  {
    getBillCardPanel().addLine();

    BillRowNo.addLineRowNo(getBillCardPanel(), "61", "crowno");

    int row = getBillCardPanel().getRowCount() - 1;

    getBillCardPanel().setBodyValueAt(ScConstants.PROCESSFLAG, row, "bismaterial");

    getBillCardPanel().setBodyValueAt(new Integer(1), row, "idiscounttaxtype");
    getBillCardPanel().setBodyValueAt("100", row, "ndiscountrate");
    getBillCardPanel().setBodyValueAt(new UFBoolean(true), row, "bisactive");

    if (row > 0)
    {
      Object ccurrtype = getBillCardPanel().getBodyValueAt(row - 1, "ccurrencytypeid");
      Object localRate = getBillCardPanel().getBodyValueAt(row - 1, "nexchangeotobrate");
      Object fracRate = getBillCardPanel().getBodyValueAt(row - 1, "nexchangeotoarate");
      Object taxrate = getBillCardPanel().getBodyValueAt(row - 1, "ntaxrate");

      getBillCardPanel().setBodyValueAt(ccurrtype, row, "ccurrencytypeid");
      getBillCardPanel().setBodyValueAt(localRate, row, "nexchangeotobrate");
      getBillCardPanel().setBodyValueAt(fracRate, row, "nexchangeotoarate");
      getBillCardPanel().setBodyValueAt(taxrate, row, "ntaxrate");
    }

    try
    {
      Object sReceiver = getBillCardPanel().getHeadItem("creciever").getValue();

      if ((sReceiver != null) && (!sReceiver.toString().trim().equals("")))
      {
        String cvendorbaseid = null;
        String defaddr = null;

        cvendorbaseid = PublicHelper.getCvendorbaseid(sReceiver.toString());
        defaddr = PublicHelper.getVdefaddr(cvendorbaseid);
        getBillCardPanel().setBodyValueAt(defaddr, row, "creceiveaddress");
      }
    }
    catch (Exception e)
    {
    }
    try {
      Object curCurrtype = getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid");
      if ((curCurrtype == null) || (curCurrtype.toString().equals(""))) {
        Object cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValue();
        if ((cvendormangid == null) || (cvendormangid.toString().equals(""))) {
          getBillCardPanel().setBodyValueAt(this.m_sPKCurrencyType, row, "ccurrencytypeid");
        }
        else {
          CustDefaultVO vo = OrderBHelper.getDefaultByCust(cvendormangid.toString());
          getBillCardPanel().setBodyValueAt(vo.getCcurrtype(), row, "ccurrencytypeid");
        }

      }

      Object curCurrtype2 = getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid");
      if ((curCurrtype2 == null) || (curCurrtype2.toString().equals(""))) {
        getBillCardPanel().setBodyValueAt(this.m_sPKCurrencyType, row, "ccurrencytypeid");
      }

      getBillCardPanel().getBillModel().execLoadFormula();
      setCurrencyState(row, true);
      getBillCardPanel().updateUI();
    } catch (Exception e) {
      SCMEnv.out(e);
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH036"));
  }

  public void onAudit()
  {
    try
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH048"));
      String corderid = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();

      OrderVO vo = OrderHelper.findByPrimaryKey(corderid);
      if (vo == null) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000042"));
      }
      else
      {
        String strErr = PuTool.getAuditLessThanMakeMsg(new OrderVO[] { vo }, "dorderdate", "vordercode", ClientEnvironment.getInstance().getDate());
        if (strErr != null) {
          throw new BusinessException(strErr);
        }

        ((OrderHeaderVO)vo.getParentVO()).setDauditdate(ClientEnvironment.getInstance().getBusinessDate());
        ((OrderHeaderVO)vo.getParentVO()).setCauditpsn(getOperatorID());
        ((OrderHeaderVO)vo.getParentVO()).setCcuroperator(getOperatorID());
        ((OrderHeaderVO)vo.getParentVO()).setTaudittime(new UFDateTime(new Date()).toString());

        OrderItemVO[] bodyVO = (OrderItemVO[])(OrderItemVO[])vo.getChildrenVO();
        Vector vTemp = new Vector();
        Object oTemp2 = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", ((OrderHeaderVO)vo.getParentVO()).getCbiztype());
        for (int i = 0; i < bodyVO.length; i++) {
          Object[][] oTemp1 = CacheTool.getAnyValue2("bd_produce", "outtype", "pk_calbody='" + ((OrderHeaderVO)vo.getParentVO()).getCwareid() + "' and pk_invbasdoc='" + bodyVO[i].getCbaseid() + "'");
          if ((oTemp1 != null) && (oTemp1.length > 0) && (oTemp2 != null)) {
            Object[] ooTemp2 = (Object[])(Object[])oTemp2;
            if ((ooTemp2.length > 0) && (oTemp1[0][0].equals("OB")) && (!ooTemp2[0].equals("N")))
              vTemp.addElement(bodyVO[i]);
          }
        }
        bodyVO = new OrderItemVO[vTemp.size()];
        vTemp.copyInto(bodyVO);
        vo.setChildrenVO(bodyVO);

        PfUtilClient.processBatchFlow(null, "APPROVE", "61", ClientEnvironment.getInstance().getDate().toString(), new OrderVO[] { vo }, null);
        if (!PfUtilClient.isSuccess()) {
          showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000043"));
          return;
        }
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000234"));

        loadData(null);

        if (this.m_listVOs.size() > getM_iCurBillIndex()) {
          ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).setDauditdate(ClientEnvironment.getInstance().getBusinessDate());
          ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).setCauditpsn(getOperatorID());
          ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).setIbillstatus(BillStatus.AUDITED);
        }

        this.m_curOrderVO = OrderHelper.findByPrimaryKey(corderid);
        this.m_listVOs.set(getM_iCurBillIndex(), this.m_curOrderVO.getParentVO());

        setButtonsState();
      }
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e);
      showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000043"));
      return;
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000234"));
  }

  public void onAuditList()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000403"));
    try
    {
      Vector vTmp = new Vector();
      BillModel bm = getBillListPanel().getHeadBillModel();
      for (int i = 0; i < bm.getRowCount(); i++) {
        if (4 == bm.getRowState(i)) {
          vTmp.addElement(new Integer(BillPanelTool.getIndexBeforeSort(getBillListPanel().getHeadBillModel(), i)));
        }
      }
      if (vTmp.size() <= 0) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000006"));
        return;
      }
      Integer[] selectRowsInteger = null;
      selectRowsInteger = new Integer[vTmp.size()];
      vTmp.copyInto(selectRowsInteger);
      int[] selectRows = null;
      selectRows = new int[vTmp.size()];
      for (int i = 0; i < selectRowsInteger.length; i++) {
        selectRows[i] = selectRowsInteger[i].intValue();
      }

      OrderVO[] VOs = (OrderVO[])(OrderVO[])getBillListPanel().getMultiSelectedVOs(OrderVO.class.getName(), OrderHeaderVO.class.getName(), OrderItemVO.class.getName());

      String strErr = PuTool.getAuditLessThanMakeMsg(VOs, "dorderdate", "vordercode", ClientEnvironment.getInstance().getDate());
      if (strErr != null) {
        throw new BusinessException(strErr);
      }

      for (int i = 0; i < selectRows.length; i++) {
        OrderHeaderVO headVO = (OrderHeaderVO)VOs[i].getParentVO();
        headVO.setCcuroperator(getOperatorID());
        headVO.setCauditpsn(getOperatorID());
        headVO.setTaudittime(new UFDateTime(new Date()).toString());
        VOs[i].setChildrenVO((OrderItemVO[])(OrderItemVO[])OrderHelper.findByPrimaryKey(headVO.getPrimaryKey()).getChildrenVO());

        OrderItemVO[] bodyVO = (OrderItemVO[])(OrderItemVO[])VOs[i].getChildrenVO();
        Vector vTemp = new Vector();
        Object oTemp2 = CacheTool.getCellValue("bd_busitype", "pk_busitype", "verifyrule", ((OrderHeaderVO)VOs[i].getParentVO()).getCbiztype());
        for (int j = 0; j < bodyVO.length; j++) {
          Object[][] oTemp1 = CacheTool.getAnyValue2("bd_produce", "outtype", "pk_calbody='" + ((OrderHeaderVO)VOs[i].getParentVO()).getCwareid() + "' and pk_invbasdoc='" + bodyVO[j].getCbaseid() + "'");

          if ((oTemp1 != null) && (oTemp1.length > 0) && (oTemp2 != null)) {
            Object[] ooTemp2 = (Object[])(Object[])oTemp2;
            if ((ooTemp2.length > 0) && (oTemp1[0][0].equals("OB")) && (!ooTemp2[0].equals("N")))
              vTemp.addElement(bodyVO[j]);
          }
        }
        bodyVO = new OrderItemVO[vTemp.size()];
        vTemp.copyInto(bodyVO);
        VOs[i].setChildrenVO(bodyVO);
      }

      PfUtilClient.processBatchFlow(null, "APPROVE", "61", ClientEnvironment.getInstance().getDate().toString(), VOs, null);
      if (!PfUtilClient.isSuccess()) {
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000235"));
        return;
      }

      for (int i = selectRows.length - 1; i >= 0; i--) {
        this.m_listVOs.remove(selectRows[i]);
      }
      getBillListPanel().getBillListData().setHeaderValueVO((OrderHeaderVO[])(OrderHeaderVO[])this.m_listVOs.toArray(new OrderHeaderVO[this.m_listVOs.size()]));

      if (getBillListPanel().getHeadTable().getRowCount() == 0) {
        this.boAudit.setEnabled(false);
        updateButtons();
      }

      getBillListPanel().getBodyBillModel().clearBodyData();
      getBillListPanel().getParentListPanel().clearSelect();

      if (this.m_listVOs.size() > 0) {
        int lint_CurBillIndex = getBillListPanel().getHeadTable().getSelectedRow();
        if (getBillListPanel().getHeadTable().getSelectedRow() < 0) {
          setM_iCurBillIndex(PuTool.getIndexBeforeSort(getBillListPanel(), 0));
          getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
          getBillListPanel().getHeadBillModel().setRowState(0, 4);
        } else {
          getBillListPanel().getHeadTable().setRowSelectionInterval(lint_CurBillIndex, lint_CurBillIndex);
          getBillListPanel().getHeadBillModel().setRowState(lint_CurBillIndex, 4);
          setM_iCurBillIndex(PuTool.getIndexBeforeSort(getBillListPanel(), lint_CurBillIndex));
        }
      }
      setButtonsState();
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000234"));
    } catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000235"), e.getMessage());
      getBillListPanel().getParentListPanel().clearSelect();
      SCMEnv.out(e);
    }
  }

  public void onUnAuditList()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000407"));
    try
    {
      Vector vTmp = new Vector();
      BillModel bm = getBillListPanel().getHeadBillModel();
      for (int i = 0; i < bm.getRowCount(); i++) {
        if (4 == bm.getRowState(i)) {
          vTmp.addElement(new Integer(BillPanelTool.getIndexBeforeSort(getBillListPanel().getHeadBillModel(), i)));
        }
      }
      if (vTmp.size() <= 0) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000011"));
        return;
      }

      Integer[] selectRowsInteger = null;
      selectRowsInteger = new Integer[vTmp.size()];
      vTmp.copyInto(selectRowsInteger);
      int[] selectRows = null;
      selectRows = new int[vTmp.size()];
      for (int i = 0; i < selectRowsInteger.length; i++) {
        selectRows[i] = selectRowsInteger[i].intValue();
      }

      OrderVO[] VOs = (OrderVO[])(OrderVO[])getBillListPanel().getMultiSelectedVOs(OrderVO.class.getName(), OrderHeaderVO.class.getName(), OrderItemVO.class.getName());

      for (int i = 0; i < selectRows.length; i++) {
        OrderHeaderVO headVO = (OrderHeaderVO)VOs[i].getParentVO();
        headVO.setCcuroperator(getOperatorID());
        headVO.setCauditpsn(getOperatorID());
        headVO.setTaudittime(null);
      }

      PfUtilClient.processBatch("UNAPPROVE", "61", ClientEnvironment.getInstance().getDate().toString(), VOs);
      if (!PfUtilClient.isSuccess()) {
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000409"));

        return;
      }

      for (int i = selectRows.length - 1; i >= 0; i--) {
        this.m_listVOs.remove(selectRows[i]);
      }

      getBillListPanel().getBillListData().getHeadBillModel().clearBodyData();
      getBillListPanel().getBillListData().setHeaderValueVO((OrderHeaderVO[])(OrderHeaderVO[])this.m_listVOs.toArray(new OrderHeaderVO[this.m_listVOs.size()]));

      if (getBillListPanel().getHeadTable().getRowCount() == 0) {
        this.boUnaudit.setEnabled(false);
        updateButtons();
      }

      getBillListPanel().getBodyBillModel().clearBodyData();
      getBillListPanel().getParentListPanel().clearSelect();

      if (this.m_listVOs.size() > 0) {
        int lint_CurBillIndex = getBillListPanel().getHeadTable().getSelectedRow();
        if (getBillListPanel().getHeadTable().getSelectedRow() < 0) {
          setM_iCurBillIndex(PuTool.getIndexBeforeSort(getBillListPanel(), 0));
          getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
          getBillListPanel().getHeadBillModel().setRowState(0, 4);
        } else {
          getBillListPanel().getHeadTable().setRowSelectionInterval(lint_CurBillIndex, lint_CurBillIndex);
          getBillListPanel().getHeadBillModel().setRowState(lint_CurBillIndex, 4);
          setM_iCurBillIndex(PuTool.getIndexBeforeSort(getBillListPanel(), lint_CurBillIndex));
        }
      }

      setButtonsState();

      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH011"));
    } catch (Exception e) {
      SCMEnv.out(e);
      getBillListPanel().getParentListPanel().clearSelect();
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000185"), e.getMessage());
    }
  }

  public void onBillLinkQuery()
  {
    String billID = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();
    String bizType = getBillCardPanel().getHeadItem("cbiztype").getValue();
    String userID = getOperatorID();
    String pk_corp = getCorpPrimaryKey();
    String billType = ScConstants.BILL_TYPE_SCORDER;

    SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, billType, billID, bizType, userID, pk_corp);

    soureDlg.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000019"));
  }

  public void onButtonClicked(ButtonObject btn)
  {
    getBillCardPanel().stopEditing();

    if (btn == this.boEdit) {
      onModify();
      getBillCardPanel().transferFocusTo(0);
      showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000072"));
    }

    if (btn.getParent() == this.boBusitype) {
      PfUtilClient.retAddBtn(this.boAdd, getPk_corp(), "61", btn);
      setButtons(this.m_btnTree.getButtonArray());
      getBillCardPanel().setBusiType(btn.getTag());
      this.boAdd.setEnabled(true);

      showSelBizType(btn);
      updateButtons();
      showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000065", null, new String[] { btn.getName() }));
    } else if (btn.getParent() == this.boAdd) {
      showHintMessage("正在增加...");
      PfUtilClient.childButtonClicked(btn, getPk_corp(), getModuleCode(), getOperatorID(), "61", this);
      if (PfUtilClient.makeFlag) {
        onAdd();
        getBillCardPanel().transferFocusTo(0);
      }
      else if (PfUtilClient.isCloseOK())
      {
        OrderVO[] VO = (OrderVO[])(OrderVO[])PfUtilClient.getRetVos();
        String tag = btn.getTag();
        int index = tag.indexOf(":");
        String billType = tag.substring(0, index);
        try
        {
          chgDataForOrderCorp(getBillCardPanel().getBusiType(), VO, billType);
        } catch (BusinessException e) {
          MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), e.getMessage());
          showHintMessage(e.getMessage());
          return;
        }

        onAddFromRef(VO);
        SourceBillTool.loadSourceInfoAll(getBillListPanel().getBodyBillModel(), "61");
      }

      initButtons();
    }

    if (btn == this.boCopy) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000350"));
      onCopy();
    }

    if (btn == this.boDel) {
      onDiscard();
    }

    if (btn == this.boSave) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000134"));
      onSave();
    }

    if (btn == this.boCancel) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000205"));
      onCancel();
    }

    if (btn == this.boAddLine) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000465"));
      onAppendLine();
    }

    if (btn == this.boDelLine) {
      onDelLine();
    }

    if (btn == this.boCopyLine) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH059"));
      onCopyLine();
    }

    if (btn == this.boInsertLine) {
      int row = getBillCardPanel().getBillTable().getSelectedRow();
      if (row > -1)
        onInsertLine();
      else {
        showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000066"));
      }
    }

    if (btn == this.boPasteLine) {
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH060"));
      onPasteLine();
    }

    if (btn == this.boPre) {
      onPrePage();
    }

    if (btn == this.boFirst) {
      onFirstPage();
    }

    if (btn == this.boNext) {
      onNextPage();
    }

    if (btn == this.boLast) {
      onLastPage();
    }

    if (btn == this.boPrint) {
      onPrint();
    }

    if (btn == this.btnBillCombin) {
      onBillCombin();
    }
    if (btn == this.boQueryForAudit) {
      onQueryForAudit();
    }
    if (btn == this.boSendAudit) {
      try {
        String corderid = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();

        OrderVO vo = OrderHelper.findByPrimaryKey(corderid);

        if (vo == null) {
          showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000042"));
          return;
        }
        onSendAudit(vo);
      }
      catch (Exception e) {
        showErrorMessage(e.getMessage());
        SCMEnv.out(e);
        return;
      }

      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH023"));
    }

    if (btn == this.m_btnPrintPreview) {
      onPrintPreview();
    }

    if (btn == this.boQuery) {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000360"));
      onQuery();
    }

    if (btn == this.boReturn) {
      if (this.m_billState == ScConstants.STATE_CARD) {
        onList();
        return;
      }
      if ((this.m_billState == ScConstants.STATE_LIST) || (this.m_billState == ScConstants.STATE_OTHER)) {
        onCard();
        return;
      }
    }

    if (btn == this.boSelectAll) {
      onSelectAll();
    }

    if (btn == this.boSelectNo) {
      onSelectNo();
    }

    if (btn == this.boFrash) {
      onFresh();
    }
    if (btn == this.boDocument) {
      onDocument();
    }
    if ((btn == this.boAudit) || (btn.getCode().equals("审批"))) {
      if (this.m_billState == ScConstants.STATE_LIST) {
        onAuditList();
      }
      if (this.m_billState == ScConstants.STATE_CARD) {
        onAudit();
      }
    }
    if (btn == this.boUnaudit) {
      if (this.m_billState == ScConstants.STATE_LIST) {
        onUnAuditList();
      }
      if (this.m_billState == ScConstants.STATE_CARD) {
        onUnAudit();
      }
    }
    if (btn == this.boCancelOut) {
      onCancelOut();
    }
    if (btn == this.boLocate) {
      onLocate();
    }
    if (btn == this.boLinkQuery)
      onBillLinkQuery();
  }

  public void onCancel()
  {
    try
    {
      if (this.m_billState == ScConstants.STATE_OTHER);
      getBillCardPanel().resumeValue();
      ((UIRefPane)getBillCardPanel().getHeadItem("vmemo").getComponent()).setText(this.m_sHeadVmemo);

      getBillCardPanel().setEnabled(false);

      if ((this.comeVOs == null) || (this.comeVOs.size() == 0))
        this.m_billState = ScConstants.STATE_CARD;
      this.boEdit.setEnabled(true);
      setButtonsState();
      getBillListPanel().setState(this.m_billState);

      if (this.m_billState == ScConstants.STATE_OTHER)
      {
        this.cardLayout.last(this);
        setButtons(this.m_btnTree.getButtonArray());
        updateUI();
        setButtonsState();
        getBillListPanel().loadBodyData(0);
        return;
      }

      loadData(null);
      if (this.m_listVOs.size() <= 0) {
        getBillCardPanel().getBillData().clearViewData();
        updateUI();
      }
    } catch (Exception e) {
      SCMEnv.out(e);
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH008"));
  }

  public void onCancelOut()
  {
    try
    {
      getBillListPanel().getComeVO().clear();

      getBillListPanel().loadHeadData();

      this.m_billState = ScConstants.STATE_CARD;
      setButtonsState();

      getBillListPanel().setState(this.m_billState);

      this.cardLayout.first(this);
      initButtons();

      setPgUpDownButtonsState(getM_iCurBillIndex());
      loadData(null);
    } catch (Exception e) {
      SCMEnv.out(e);
    }
  }

  private void onCard()
  {
    if (this.m_billState == ScConstants.STATE_OTHER) {
      int rowindex = getBillListPanel().getHeadTable().getSelectedRow();
      if (rowindex == -1) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000044"));
        return;
      }

      refLoadData(rowindex);
      return;
    }

    if (getBillListPanel().getHeadTable().getRowCount() > 0) {
      int rowSelected = getBillListPanel().getHeadTable().getSelectedRowCount();
      if ((rowSelected == 0) || (rowSelected > 1)) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000045"));
        return;
      }
    }

    this.m_billState = ScConstants.STATE_CARD;
    this.cardLayout.first(this);
    initButtons();
    setButtonsState();
    int listnum = getBillListPanel().getHeadTable().getRowCount();
    if (listnum <= 0) {
      getBillCardPanel().getBillData().clearViewData();
      return;
    }

    int lint_CurBillIndex = getBillListPanel().getHeadTable().getSelectedRow();
    setM_iCurBillIndex(PuTool.getIndexBeforeSort(getBillListPanel(), lint_CurBillIndex));

    setPgUpDownButtonsState(getM_iCurBillIndex());

    if (getM_iCurBillIndex() < 0)
      return;
    loadData(null);
    updateUI();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH021"));
  }

  private void onCopy()
  {
    if ((this.m_listVOs == null) || (this.m_listVOs.size() == 0)) {
      return;
    }
    UIRefPane ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("vmemo").getComponent();
    String headVmemo = ref.getUITextField().getText();
    String curOrderID = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();
    try
    {
      OrderVO orderVO = OrderHelper.findByPrimaryKey(curOrderID);
      ((OrderHeaderVO)orderVO.getParentVO()).setDorderdate(ClientEnvironment.getInstance().getDate());
      ((OrderHeaderVO)orderVO.getParentVO()).setVordercode(null);
      ((OrderHeaderVO)orderVO.getParentVO()).setCorderid(null);
      ((OrderHeaderVO)orderVO.getParentVO()).setCoperator(getOperatorID());
      ((OrderHeaderVO)orderVO.getParentVO()).setIbillstatus(BillStatus.FREE);
      ((OrderHeaderVO)orderVO.getParentVO()).setVmemo(headVmemo);

      ((OrderHeaderVO)orderVO.getParentVO()).setCauditpsn(null);
      ((OrderHeaderVO)orderVO.getParentVO()).setDauditdate(null);

      OrderItemVO[] itemVO = (OrderItemVO[])(OrderItemVO[])orderVO.getChildrenVO();
      Vector vTemp = new Vector();
      for (int i = 0; i < itemVO.length; i++) {
        itemVO[i].setCorder_bid(null);
        itemVO[i].setCupsourcebillid(null);
        itemVO[i].setCupsourcebillrowid(null);
        itemVO[i].setCupsourcebilltype(null);
        itemVO[i].setCsourcebillid(null);
        itemVO[i].setCsourcebillrow(null);
        itemVO[i].setCordersource(null);

        itemVO[i].setNaccumarrvnum(null);
        itemVO[i].setNaccuminvoicenum(null);
        itemVO[i].setNaccumstorenum(null);
        itemVO[i].setNaccumwastnum(null);

        itemVO[i].setCcontractid(null);
        itemVO[i].setCcontractrcode(null);
        itemVO[i].setCcontractrowid(null);

        vTemp.addElement(getBillCardPanel().getBodyValueAt(i, "measrate"));
      }
      orderVO.setChildrenVO(itemVO);

      getBillCardPanel().addNew();
      getBillCardPanel().setBillValueVO(orderVO);

      loadCustBank();

      loadPrecision();

      getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(8);
      getBillCardPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(8);
      getBillCardPanel().setBillValueVO(orderVO);
      loadPrecision();

      ref.setText(headVmemo);

      getBillCardPanel().getBillModel().execLoadFormula();

      for (int i = 0; i < itemVO.length; i++) {
        Integer idiscounttaxtype = itemVO[i].getIdiscounttaxtype();

        getBillCardPanel().setBodyValueAt(idiscounttaxtype, i, "idiscounttaxtype");

        String formula = "isassist->getColValue(bd_invbasdoc,assistunit,pk_invbasdoc,cbaseid)";
        getBillCardPanel().getBillModel().execFormula(i, new String[] { formula });

        BillEdit.editFreeItem(getBillCardPanel(), i, "cinventorycode", "cbaseid", "cmangid");
        loadFreeItems();

        getBillCardPanel().setBodyValueAt(vTemp.elementAt(i), i, "measrate");
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      showErrorMessage(e.getMessage());
    }
    this.m_billState = ScConstants.STATE_ADD;
    initButtons();
    setButtonsState();
    setRelateCntAndDefaultPriceAllRow(false);
    this.cardLayout.first(this);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH029"));
  }

  private void onCopyLine()
  {
    int[] row = getBillCardPanel().getBillTable().getSelectedRows();
    if (row.length == 0) {
      return;
    }
    for (int i = 0; i < row.length; i++)
    {
      Object value = getBillCardPanel().getBodyValueAt(row[i], "bismaterial");
      if ((value == null) || (value.toString().trim().equals(""))) {
        showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000046"));
        return;
      }
    }

    getBillCardPanel().copyLine();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH039"));
  }

  private void onDelLine()
  {
    int[] row = getBillCardPanel().getBillTable().getSelectedRows();
    if (row.length == 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), NCLangRes.getInstance().getStrByID("401201", "UPP401201-000067"));
      return;
    }

    Vector allDelRow = new Vector();

    for (int i = 0; i < row.length; i++) {
      allDelRow.addElement("" + row[i]);
      Object value = getBillCardPanel().getBodyValueAt(row[i], "bismaterial");
      if ((value == null) || (value.toString().trim().equals(""))) {
        continue;
      }
      for (int k = row[i] + 1; k < getBillCardPanel().getRowCount(); k++) {
        value = getBillCardPanel().getBodyValueAt(k, "bismaterial");
        if ((value != null) && (!value.toString().trim().equals("")))
          break;
        allDelRow.addElement("" + k);
        i++;
      }

    }

    int[] allrow = new int[allDelRow.size()];

    for (int i = 0; i < allDelRow.size(); i++) {
      allrow[i] = new Integer(allDelRow.elementAt(i).toString()).intValue();
    }

    getBillCardPanel().getBillModel().delLine(allrow);

    int lint_rowcount = getBillCardPanel().getRowCount();

    if (lint_rowcount > 0) {
      getBillCardPanel().getBillTable().setRowSelectionInterval(lint_rowcount - 1, lint_rowcount - 1);
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH037"));
  }

  private void onDiscard()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000068"));

    if (this.m_billState == ScConstants.STATE_LIST)
    {
      int lint_CurBillIndex = getBillListPanel().getHeadTable().getSelectedRow();
      setM_iCurBillIndex(PuTool.getIndexBeforeSort(getBillListPanel(), lint_CurBillIndex));
      if (getM_iCurBillIndex() == -1) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000047"));
        return;
      }

      int lint_RowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
      if (lint_RowCount > 1) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000045"));
        return;
      }
    }

    int ret = MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000219"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000224"), 8);
    if (ret != 4) {
      return;
    }

    String corderid = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();
    try
    {
      OrderVO vo = OrderHelper.findByPrimaryKey(corderid);
      if (vo == null) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000042"));
        return;
      }
      int i = ((OrderHeaderVO)vo.getParentVO()).getIbillstatus().intValue();
      if ((i == BillStatus.AUDITED.intValue()) || (i == BillStatus.AUDITING.intValue()) || (i == BillStatus.AUDITFAIL.intValue())) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000048"));
        return;
      }if (i == BillStatus.DELETED.intValue()) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000042"));
        return;
      }
      ((OrderHeaderVO)vo.getParentVO()).setCuroperator(getOperatorID());
      PfUtilClient.processBatch("DISCARD", "61", ClientEnvironment.getInstance().getDate().toString(), new OrderVO[] { vo });
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException))
        showErrorMessage(e.getMessage());
      SCMEnv.out(e);
      return;
    }

    if (this.m_billState == ScConstants.STATE_LIST) {
      String curpk = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();
      int listnum = getBillListPanel().getHeadTable().getRowCount();
      for (int i = 0; i < listnum; i++)
      {
        String billpk = getBillListPanel().getHeadBillModel().getValueAt(i, "corderid").toString();
        if (billpk.equals(curpk)) {
          getBillListPanel().getHeadBillModel().delLine(new int[] { i });
          getBillListPanel().getBillListData().clearCopyData();

          int curRow = i == listnum - 1 ? (i = listnum - 2) : i;
          if (curRow == -1) {
            clearList();
            this.boReturn.setEnabled(true);
            updateButtons();
            break;
          }

          getBillListPanel().getHeadTable().setRowSelectionInterval(curRow, curRow);
          getBillListPanel().loadBodyData(curRow);
          getBillListPanel().getHeadBillModel().setRowState(curRow, 4);

          String billstatus = getBillListPanel().getHeadBillModel().getValueAt(i, "ibillstatus").toString();
          setOperateState(new Integer(billstatus).intValue());
          break;
        }

      }

      updateUI();
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000068"));

    this.m_listVOs.remove(getM_iCurBillIndex());
    if (getM_iCurBillIndex() >= this.m_listVOs.size()) {
      setM_iCurBillIndex(this.m_listVOs.size() - 1);
    }
    if (this.m_billState == ScConstants.STATE_CARD) {
      if (getM_iCurBillIndex() < 0) {
        getBillCardPanel().getBillData().clearViewData();

        setButtonsState();
        return;
      }
      loadData(null);
      setPgUpDownButtonsState(getM_iCurBillIndex());
    }

    setButtonsState();
  }

  private void onDocument()
  {
    String[] strPks = null;
    String[] strCodes = null;
    HashMap mapBtnPowerVo = new HashMap();
    Integer iBillStatus = null;
    try
    {
      if (this.m_billState == ScConstants.STATE_CARD) {
        if (this.m_curOrderVO == null)
          return;
        strPks = new String[] { this.m_curOrderVO.getParentVO().getPrimaryKey() };
        strCodes = new String[] { ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getVordercode() };

        BtnPowerVO pVo = new BtnPowerVO(strCodes[0]);
        iBillStatus = ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getIbillstatus();
        if ((iBillStatus.intValue() == BillStatus.DELETED.intValue()) || (iBillStatus.intValue() == BillStatus.AUDITING.intValue()) || (iBillStatus.intValue() == BillStatus.AUDITED.intValue()))
        {
          pVo.setFileDelEnable("false");
        }
        mapBtnPowerVo.put(strCodes[0], pVo);

        if ((strPks == null) || (strPks.length <= 0)) {
          return;
        }
        DocumentManager.showDM(this, strPks, strCodes, mapBtnPowerVo);

        return;
      }

      if (this.m_billState == ScConstants.STATE_LIST) {
        int rowCount = getBillListPanel().getHeadTable().getRowCount();
        Vector tempV = new Vector();
        for (int i = 0; i < rowCount; i++) {
          if (getBillListPanel().getHeadBillModel().getRowState(i) == 4)
            tempV.addElement(new Integer(i));
        }
        if (tempV.size() > 0)
        {
          strPks = new String[tempV.size()];
          strCodes = new String[tempV.size()];
          for (int i = 0; i < tempV.size(); i++) {
            int index = ((Integer)tempV.get(i)).intValue();
            strPks[i] = ((String)getBillListPanel().getHeadBillModel().getValueAt(index, "corderid"));
            strCodes[i] = ((String)getBillListPanel().getHeadBillModel().getValueAt(index, "vordercode"));
          }
        }
      }
      if ((strPks == null) || (strPks.length <= 0)) {
        return;
      }
      DocumentManager.showDM(this, strPks, strCodes);
    } catch (Exception e) {
      SCMEnv.out(e);
      if ((e instanceof BusinessException))
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), e.getMessage());
      else
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), NCLangRes.getInstance().getStrByID("401201", "UPP401201-000007"));
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000025"));
  }

  public void onDoubleClick()
  {
    this.m_billState = ScConstants.STATE_CARD;
    this.cardLayout.first(this);
    initButtons();
    int lint_CurBillIndex = getBillListPanel().getHeadTable().getSelectedRow();
    setM_iCurBillIndex(PuTool.getIndexBeforeSort(getBillListPanel(), lint_CurBillIndex));
    setButtonsState();
    setPgUpDownButtonsState(getM_iCurBillIndex());
    loadData(null);
    updateUI();
  }

  private void onFirstPage()
  {
    setM_iCurBillIndex(0);

    if ((this.m_listVOs == null) || (this.m_listVOs.size() == 0)) {
      showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000009"));
      getBillCardPanel().getBillData().clearViewData();
      return;
    }
    loadData(null);
    setPgUpDownButtonsState(getM_iCurBillIndex());
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000026"));
  }

  private void onFresh()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000360"));
    try
    {
      OrderHeaderVO[] orderHeaderVO = OrderHelper.queryAllHead("", this.m_queryCon, new ClientLink(ClientEnvironment.getInstance()));

      getBillListPanel().setHeaderValueVO(orderHeaderVO);
      getBillListPanel().getHeadBillModel().execLoadFormula();

      this.m_listVOs = new ArrayList();
      for (int i = 0; i < orderHeaderVO.length; i++) {
        this.m_listVOs.add(orderHeaderVO[i]);
      }

      getBillListPanel().hideTableCol();
      if (this.m_listVOs.size() > 0) {
        getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
        getBillListPanel().getHeadBillModel().setRowState(0, 4);
        getBillListPanel().loadBodyData(0);

        String[] value = { String.valueOf(this.m_listVOs.size()) };
        showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000070", null, value));
      } else if (this.m_billState == ScConstants.STATE_LIST) {
        clearList();
        this.boReturn.setEnabled(true);
        updateButtons();
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000361"));
      }
    }
    catch (Exception e) {
      SCMEnv.out("列表表头数据加载失败");
      SCMEnv.out(e);
    }
    setPgUpDownButtonsState(getM_iCurBillIndex());
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH007"));
  }

  private void setBodyDefPK(BillEditEvent event)
  {
    if (event.getKey().equals("vdef1"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef1", "pk_defdoc1");
    else if (event.getKey().equals("vdef2"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef2", "pk_defdoc2");
    else if (event.getKey().equals("vdef3"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef3", "pk_defdoc3");
    else if (event.getKey().equals("vdef4"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef4", "pk_defdoc4");
    else if (event.getKey().equals("vdef5"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef5", "pk_defdoc5");
    else if (event.getKey().equals("vdef6"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef6", "pk_defdoc6");
    else if (event.getKey().equals("vdef7"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef7", "pk_defdoc7");
    else if (event.getKey().equals("vdef8"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef8", "pk_defdoc8");
    else if (event.getKey().equals("vdef9"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef9", "pk_defdoc9");
    else if (event.getKey().equals("vdef10"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef10", "pk_defdoc10");
    else if (event.getKey().equals("vdef11"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef11", "pk_defdoc11");
    else if (event.getKey().equals("vdef12"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef12", "pk_defdoc12");
    else if (event.getKey().equals("vdef13"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef13", "pk_defdoc13");
    else if (event.getKey().equals("vdef14"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef14", "pk_defdoc14");
    else if (event.getKey().equals("vdef15"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef15", "pk_defdoc15");
    else if (event.getKey().equals("vdef16"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef16", "pk_defdoc16");
    else if (event.getKey().equals("vdef17"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef17", "pk_defdoc17");
    else if (event.getKey().equals("vdef18"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef18", "pk_defdoc18");
    else if (event.getKey().equals("vdef19"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef19", "pk_defdoc19");
    else if (event.getKey().equals("vdef20"))
      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), event.getRow(), "vdef20", "pk_defdoc20");
  }

  private void setHeadDefPK(BillEditEvent event)
  {
    if (event.getKey().equals("vdef1"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef1", "pk_defdoc1");
    else if (event.getKey().equals("vdef2"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef2", "pk_defdoc2");
    else if (event.getKey().equals("vdef3"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef3", "pk_defdoc3");
    else if (event.getKey().equals("vdef4"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef4", "pk_defdoc4");
    else if (event.getKey().equals("vdef5"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef5", "pk_defdoc5");
    else if (event.getKey().equals("vdef6"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef6", "pk_defdoc6");
    else if (event.getKey().equals("vdef7"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef7", "pk_defdoc7");
    else if (event.getKey().equals("vdef8"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef8", "pk_defdoc8");
    else if (event.getKey().equals("vdef9"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef9", "pk_defdoc9");
    else if (event.getKey().equals("vdef10"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef10", "pk_defdoc10");
    else if (event.getKey().equals("vdef11"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef11", "pk_defdoc11");
    else if (event.getKey().equals("vdef12"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef12", "pk_defdoc12");
    else if (event.getKey().equals("vdef13"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef13", "pk_defdoc13");
    else if (event.getKey().equals("vdef14"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef14", "pk_defdoc14");
    else if (event.getKey().equals("vdef15"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef15", "pk_defdoc15");
    else if (event.getKey().equals("vdef16"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef16", "pk_defdoc16");
    else if (event.getKey().equals("vdef17"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef17", "pk_defdoc17");
    else if (event.getKey().equals("vdef18"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef18", "pk_defdoc18");
    else if (event.getKey().equals("vdef19"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef19", "pk_defdoc19");
    else if (event.getKey().equals("vdef20"))
      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), "vdef20", "pk_defdoc20");
  }

  private void onInsertLine()
  {
    getBillCardPanel().insertLine();

    BillRowNo.insertLineRowNo(getBillCardPanel(), "61", "crowno");

    int row = getBillCardPanel().getBillTable().getSelectedRow();
    if (row < 0) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("common", "UC001-0000016"), NCLangRes.getInstance().getStrByID("401201", "UPP401201-000066"));
      return;
    }

    getBillCardPanel().setBodyValueAt(ScConstants.PROCESSFLAG, row, "bismaterial");

    getBillCardPanel().setBodyValueAt(new Integer(1), row, "idiscounttaxtype");
    getBillCardPanel().setBodyValueAt("100", row, "ndiscountrate");
    getBillCardPanel().setBodyValueAt(new UFBoolean(true), row, "bisactive");

    if (row > 0)
    {
      Object ccurrtype = getBillCardPanel().getBodyValueAt(row - 1, "ccurrencytypeid");
      Object localRate = getBillCardPanel().getBodyValueAt(row - 1, "nexchangeotobrate");
      Object fracRate = getBillCardPanel().getBodyValueAt(row - 1, "nexchangeotoarate");
      Object taxrate = getBillCardPanel().getBodyValueAt(row - 1, "ntaxrate");

      getBillCardPanel().setBodyValueAt(ccurrtype, row, "ccurrencytypeid");
      getBillCardPanel().setBodyValueAt(localRate, row, "nexchangeotobrate");
      getBillCardPanel().setBodyValueAt(fracRate, row, "nexchangeotoarate");
      getBillCardPanel().setBodyValueAt(taxrate, row, "ntaxrate");
    }

    try
    {
      Object sReceiver = getBillCardPanel().getHeadItem("creciever").getValue();

      if ((sReceiver != null) && (!sReceiver.toString().trim().equals("")))
      {
        String cvendorbaseid = null;
        String defaddr = null;

        cvendorbaseid = PublicHelper.getCvendorbaseid(sReceiver.toString());
        defaddr = PublicHelper.getVdefaddr(cvendorbaseid);
        getBillCardPanel().setBodyValueAt(defaddr, row, "creceiveaddress");
      }
    }
    catch (Exception e)
    {
    }
    try {
      Object curCurrtype = getBillCardPanel().getBodyValueAt(row, "ccurrencytypeid");
      if ((curCurrtype == null) || (curCurrtype.toString().equals(""))) {
        Object cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValue();
        if ((cvendormangid == null) || (cvendormangid.toString().equals(""))) {
          getBillCardPanel().setBodyValueAt(this.m_sPKCurrencyType, row, "ccurrencytypeid");
        }
        else {
          CustDefaultVO vo = OrderBHelper.getDefaultByCust(cvendormangid.toString());
          getBillCardPanel().setBodyValueAt(vo.getCcurrtype(), row, "ccurrencytypeid");
        }
      }

      getBillCardPanel().getBillModel().execLoadFormula();
      setCurrencyState(row, true);
      getBillCardPanel().updateUI();
    } catch (Exception e) {
      SCMEnv.out(e);
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH038"));
  }

  private void onInsertLines(int iBeginRow, int iEndRow, int iInsertCount)
  {
    if (getBillCardPanel().getBillTable().getSelectedRowCount() <= 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000049"));
      return;
    }

    String defaddr = null;
    String defcurr = null;
    try {
      Object sReceiver = getBillCardPanel().getHeadItem("creciever").getValue();

      if ((sReceiver != null) && (!sReceiver.toString().trim().equals("")))
      {
        String cvendorbaseid = null;
        cvendorbaseid = PublicHelper.getCvendorbaseid(sReceiver.toString());
        defaddr = PublicHelper.getVdefaddr(cvendorbaseid);
      }
    }
    catch (Exception e)
    {
    }
    try {
      Object cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValue();
      if ((cvendormangid == null) || (cvendormangid.toString().equals(""))) {
        defcurr = this.m_sPKCurrencyType;
      } else {
        CustDefaultVO vo = OrderBHelper.getDefaultByCust(cvendormangid.toString());
        defcurr = vo.getCcurrtype();
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }

    int iCurRow = 0;
    for (int i = 0; i < iInsertCount; i++) {
      iCurRow = iBeginRow + i;
      getBillCardPanel().getBillModel().insertRow(iCurRow + 1);

      getBillCardPanel().setBodyValueAt(ScConstants.PROCESSFLAG, iCurRow + 1, "bismaterial");

      getBillCardPanel().setBodyValueAt(new Integer(1), iCurRow + 1, "idiscounttaxtype");
      getBillCardPanel().setBodyValueAt("100", iCurRow + 1, "ndiscountrate");
      getBillCardPanel().setBodyValueAt(defaddr, iCurRow + 1, "creceiveaddress");
      getBillCardPanel().setBodyValueAt(defcurr, iCurRow + 1, "ccurrencytypeid");
      getBillCardPanel().setBodyValueAt(new UFBoolean(true), iCurRow + 1, "bisactive");
      setCurrencyState(iCurRow + 1, true);
    }

    int iFinalEndRow = iBeginRow + iInsertCount + 1;

    BillRowNo.insertLineRowNos(getBillCardPanel(), "61", "crowno", iFinalEndRow, iInsertCount);

    getBillCardPanel().getBillModel().execLoadFormula();

    getBillCardPanel().updateUI();
  }

  private void onLastPage()
  {
    setM_iCurBillIndex(this.m_listVOs.size() - 1);

    loadData(null);
    setPgUpDownButtonsState(getM_iCurBillIndex());
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000029"));
  }

  private void onList()
  {
    this.m_billState = ScConstants.STATE_LIST;
    this.cardLayout.last(this);
    initButtons();
    getBillListPanel().setState(this.m_billState);

    OrderHeaderVO[] orderHeaderVO = new OrderHeaderVO[this.m_listVOs.size()];
    this.m_listVOs.toArray(orderHeaderVO);
    getBillListPanel().setHeaderValueVO(orderHeaderVO);
    getBillListPanel().getHeadBillModel().execLoadFormula();

    int rowcount = getBillListPanel().getHeadBillModel().getRowCount();
    if (rowcount == 0) {
      clearList();
      this.boReturn.setEnabled(true);
      updateButtons();
      return;
    }
    onSelectNo();
    String curpk = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();
    for (int i = 0; i < rowcount; i++) {
      Object corderid = getBillListPanel().getHeadBillModel().getValueAt(i, "corderid");
      if ((corderid == null) || (!corderid.toString().equals(curpk))) {
        continue;
      }
      getBillListPanel().loadBodyData(i);
      getBillListPanel().getHeadTable().setRowSelectionInterval(i, i);
      getBillListPanel().getHeadBillModel().setRowState(i, 4);

      String billstatus = getBillListPanel().getHeadBillModel().getValueAt(i, "ibillstatus").toString();
      setOperateState(new Integer(billstatus).intValue());

      break;
    }

    setButtonsState();
    updateUI();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH022"));
  }

  private void onLocate()
  {
    OrientDialog dlgOrient = new OrientDialog(this, getBillListPanel().getHeadBillModel(), getBillListPanel().getBillListData().getBodyItems(), getBillListPanel().getBodyTable());

    dlgOrient.showModal();
  }

  public void onMenuItemClick(ActionEvent event)
  {
    UIMenuItem menuItem = (UIMenuItem)event.getSource();

    if (menuItem.equals(getBillCardPanel().getAddLineMenuItem()))
      onAppendLine();
    else if (menuItem.equals(getBillCardPanel().getCopyLineMenuItem()))
      onCopyLine();
    else if (menuItem.equals(getBillCardPanel().getDelLineMenuItem()))
      onDelLine();
    else if (menuItem.equals(getBillCardPanel().getInsertLineMenuItem()))
      onInsertLine();
    else if (menuItem.equals(getBillCardPanel().getPasteLineMenuItem()))
      onPasteLine();
    else if (menuItem.equals(getBillCardPanel().getPasteLineToTailMenuItem()))
      onPasteLineToTail();
  }

  private void onModify()
  {
    try
    {
      if (this.m_billState == ScConstants.STATE_OTHER) {
        int rowindex = getBillListPanel().getHeadTable().getSelectedRow();
        if (rowindex == -1) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000044"));
          return;
        }
        refLoadData(rowindex);

        for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
        {
          Object oMangId = getBillCardPanel().getBodyValueAt(i, "cmangid");
          if ((oMangId == null) || (oMangId.toString().trim().length() == 0))
            getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", false);
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", InvTool.isBatchManaged(oMangId.toString().trim()));
          }

        }

        setDefaultValueByUser();

        setRelateCntAndDefaultPriceAllRow(false);

        setNotEditableWhenRelateCntAllRow();

        return;
      }

      if (this.m_billState == ScConstants.STATE_LIST)
      {
        int lint_CurBillIndex = getBillListPanel().getHeadTable().getSelectedRow();
        setM_iCurBillIndex(PuTool.getIndexBeforeSort(getBillListPanel(), lint_CurBillIndex));
        if (getM_iCurBillIndex() == -1) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000044"));
          return;
        }

        this.cardLayout.first(this);
        setButtons(this.m_btnTree.getButtonArray());

        loadData(null);

        for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
        {
          String sMangId = getBillCardPanel().getBodyValueAt(i, "cmangid").toString();
          if ((sMangId == null) || (sMangId.trim().length() == 0))
            getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", false);
          else {
            getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", InvTool.isBatchManaged(sMangId));
          }

          getBillCardPanel().setBodyValueAt(null, i, "vproducenum");
        }

      }

      rightButtonRightControl();
      updateButtons();
    } catch (Exception e) {
      SCMEnv.out(e);
    }

    this.m_billState = ScConstants.STATE_MODIFY;
    initButtons();
    setButtonsState();

    int rowindex = getBillCardPanel().getBillTable().getSelectedRow();
    getBillCardPanel().getBillTable().clearSelection();
    if (rowindex > -1) {
      int colindex = getBillCardPanel().getBillTable().getSelectedColumn();
      if (colindex > -1) {
        getBillCardPanel().getBillTable().setColumnSelectionInterval(colindex, colindex);
        getBillCardPanel().getBillTable().setRowSelectionInterval(rowindex, rowindex);
      }

    }

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
    {
      String sMangId = getBillCardPanel().getBodyValueAt(i, "cmangid").toString();
      if ((sMangId == null) || (sMangId.trim().length() == 0))
        getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", false);
      else {
        getBillCardPanel().getBillModel().setCellEditable(i, "vproducenum", InvTool.isBatchManaged(sMangId));
      }

    }

    setNotEditableWhenRelateCntAllRow();

    getBillCardPanel().updateValue();
    this.cardLayout.first(this);
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000030"));
  }

  private void onNextPage()
  {
    if (getM_iCurBillIndex() < this.m_listVOs.size() - 1) {
      this.m_iCurBillIndex += 1;
    }
    loadData(null);
    setPgUpDownButtonsState(getM_iCurBillIndex());
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000028"));
  }

  private void onPasteLine()
  {
    int row = getBillCardPanel().getBillTable().getSelectedRow();

    Object value = getBillCardPanel().getBodyValueAt(row, "bismaterial");
    if ((value == null) || (value.toString().trim().equals(""))) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000050"));
      return;
    }
    showHintMessage("");

    int oldRow = getBillCardPanel().getBillTable().getRowCount();

    getBillCardPanel().pasteLine();

    int newRow = getBillCardPanel().getBillTable().getRowCount();
    if (newRow >= oldRow) {
      BillRowNo.pasteLineRowNo(getBillCardPanel(), "61", "crowno", newRow - oldRow);
    }

    row = getBillCardPanel().getBillTable().getSelectedRow();

    int iBeginSetRow = row - (newRow - oldRow);
    int iEndSetRow = row - 1;
    for (int i = iBeginSetRow; i <= iEndSetRow; i++)
    {
      getBillCardPanel().setBodyValueAt("", i, "corder_bid");

      getBillCardPanel().setBodyValueAt("", i, "cordersource");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillid");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillrow");
      getBillCardPanel().setBodyValueAt("", i, "cupsourcebilltype");
      getBillCardPanel().setBodyValueAt("", i, "cupsourcebillid");
      getBillCardPanel().setBodyValueAt("", i, "cupsourcebillrowid");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillname");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillcode");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillrowno");
      getBillCardPanel().setBodyValueAt("", i, "cancestorbillname");
      getBillCardPanel().setBodyValueAt("", i, "cancestorbillcode");
      getBillCardPanel().setBodyValueAt("", i, "cancestorbillrowno");
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH040"));
  }

  public void onPasteLineToTail()
  {
    int iOrgRowCount = getBillCardPanel().getRowCount();
    getBillCardPanel().pasteLineToTail();

    int iPastedRowCount = getBillCardPanel().getRowCount() - iOrgRowCount;

    BillRowNo.addLineRowNos(getBillCardPanel(), "61", "crowno", iPastedRowCount);

    for (int i = iOrgRowCount; i <= iOrgRowCount + iPastedRowCount - 1; i++) {
      getBillCardPanel().setBodyValueAt("", i, "corder_bid");

      getBillCardPanel().setBodyValueAt("", i, "cordersource");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillid");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillrow");
      getBillCardPanel().setBodyValueAt("", i, "cupsourcebilltype");
      getBillCardPanel().setBodyValueAt("", i, "cupsourcebillid");
      getBillCardPanel().setBodyValueAt("", i, "cupsourcebillrowid");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillname");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillcode");
      getBillCardPanel().setBodyValueAt("", i, "csourcebillrowno");
      getBillCardPanel().setBodyValueAt("", i, "cancestorbillname");
      getBillCardPanel().setBodyValueAt("", i, "cancestorbillcode");
      getBillCardPanel().setBodyValueAt("", i, "cancestorbillrowno");
    }
  }

  private void onPrePage()
  {
    if (getM_iCurBillIndex() > 0) {
      this.m_iCurBillIndex -= 1;
    }
    loadData(null);
    setPgUpDownButtonsState(getM_iCurBillIndex());
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000027"));
  }

  private void onPrint()
  {
    if (this.m_billState == ScConstants.STATE_CARD) {
      OrderVO l_curOrderVO = (OrderVO)getBillCardPanel().getBillValueVO(OrderVO.class.getName(), OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
      Vector vAll = new Vector();
      OrderVO[] oneBill = null;
      vAll.add(l_curOrderVO);
      oneBill = new OrderVO[vAll.size()];
      vAll.copyInto(oneBill);
      ArrayList aryRslt = new ArrayList();
      aryRslt.add(oneBill[0]);

      ScmPrintTool printCard = null;
      if (printCard == null)
        printCard = new ScmPrintTool(this, getBillCardPanel(), aryRslt, getModuleCode());
      else {
        try {
          printCard.setData(aryRslt);
        }
        catch (Exception e)
        {
        }
      }
      try
      {
        printCard.onCardPrint(getBillCardPanel(), getBillListPanel(), "61");
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), printCard.getPrintMessage());
      } catch (BusinessException e) {
        SCMEnv.out(e);
      }
    }
    if ((this.m_billState == ScConstants.STATE_LIST) || (this.m_billState == ScConstants.STATE_OTHER)) {
      if (this.printList == null)
        this.printList = new ScmPrintTool(this, getBillCardPanel(), getSelectedBills(), getModuleCode());
      else
        try
        {
          this.printList.setData(getSelectedBills());
        }
        catch (Exception e) {
        }
      try {
        this.printList.onBatchPrint(getBillListPanel(), getBillCardPanel(), "61");
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), this.printList.getPrintMessage());
      } catch (BusinessException e) {
        showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000061"));
        SCMEnv.out(e);
      }
    }
  }

  private void onPrintPreview()
  {
    if (this.printList == null)
      this.printList = new ScmPrintTool(this, getBillCardPanel(), getSelectedBills(), getModuleCode());
    else
      try {
        this.printList.setData(getSelectedBills());
      }
      catch (Exception e)
      {
      }
    try {
      this.printList.onBatchPrintPreview(getBillListPanel(), getBillCardPanel(), "61");
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
    }
  }

  private void onQuery()
  {
    if (this.m_billState == ScConstants.STATE_OTHER) {
      return;
    }
    try
    {
      getQryCondition().showModal();
      if (!getQryCondition().isCloseOK()) {
        return;
      }

      this.m_query.setRefsDataPowerConVOs(ClientEnvironment.getInstance().getUser().getPrimaryKey(), new String[] { ClientEnvironment.getInstance().getCorporation().getPrimaryKey() }, new String[] { "部门档案", "采购组织", "供应商档案", "供应商档案", "客户档案", "库存组织", "人员档案", "存货档案", "存货分类" }, new String[] { "bd_deptdoc.deptcode", "sc_order.cpurorganization", "bd_cubasdoc.custcode", "sc_order.cgiveinvoicevendor", "sc_order.creciever", "sc_order.cwareid", "bd_psndoc.psncode", "bd_invbasdoc.invcode", "bd_invcl.invclasscode" }, new int[] { 0, 2, 0, 2, 2, 2, 0, 0, 0 });

      String whereSQL = getQryCondition().getWhereSQL();
      if ((whereSQL == null) || (whereSQL.trim().equals("")))
        whereSQL = " sc_order.pk_corp='" + getPk_corp() + "'";
      else {
        whereSQL = " sc_order.pk_corp='" + getPk_corp() + "' and " + whereSQL;
      }
      String lStr_temp = this.m_query.getUICheckBoxSQL();

      if ((lStr_temp != null) && (lStr_temp.trim().length() > 0)) {
        whereSQL = whereSQL + " and ( " + lStr_temp + " ) ";
      }

      ConditionVO[] condvo = this.m_query.getConditionVO();
      ArrayList listRet = ScTool.dealCondVosForPower(condvo);

      String strDataPowerSql = (String)listRet.get(1);

      if ((strDataPowerSql != null) && (strDataPowerSql.trim().length() > 0)) {
        if ((whereSQL != null) && (whereSQL.trim().length() > 0))
          whereSQL = whereSQL + " and " + strDataPowerSql + " ";
        else {
          whereSQL = strDataPowerSql + " ";
        }
      }

      this.m_queryCon = whereSQL;

      OrderHeaderVO[] orderHeaderVO = OrderHelper.queryAllHead("", this.m_queryCon, new ClientLink(ClientEnvironment.getInstance()));

      getBillListPanel().setHeaderValueVO(orderHeaderVO);

      this.m_listVOs = new ArrayList();
      for (int i = 0; i < orderHeaderVO.length; i++) {
        this.m_listVOs.add(orderHeaderVO[i]);
      }

      if ((this.m_listVOs == null) || (this.m_listVOs.size() == 0)) {
        this.m_bQueried = false;

        setButtonsState();
        getBillCardPanel().getBillData().clearViewData();
        clearList();
        showHintMessage(NCLangRes.getInstance().getStrByID("401200", "UPP401200-000012"));
        return;
      }

      if (this.m_listVOs.size() > 0) {
        getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
        getBillListPanel().getHeadBillModel().setRowState(0, 4);
        getBillListPanel().loadBodyData(0);
      }

      getQryCondition().destroy();
    }
    catch (Exception e) {
      this.m_bQueried = false;

      setButtonsState();
      SCMEnv.out("数据加载失败");
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000038"));
      SCMEnv.out(e);
    }

    if (this.m_billState != ScConstants.STATE_LIST) {
      onFirstPage();
    }
    this.m_bQueried = true;
    setButtonsState();

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH009"));
  }

  private void onBillCombin()
  {
    CollectSettingDlg dlg = new CollectSettingDlg(this, NCLangRes.getInstance().getStrByID("401201", "UPT401201-000033"));

    dlg.initData(getBillCardPanel(), new String[] { "cinventorycode", "cinventoryname", "invspec", "invtype", "ccurrencytype" }, null, new String[] { "noriginalcurmny", "noriginaltaxmny", "nordernum" }, null, new String[] { "noriginalcurprice", "noriginalnetprice" }, "nordernum");

    dlg.showModal();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000039"));
  }

  private void onQueryForAudit()
  {
    if ((this.m_listVOs.size() <= 0) || (getM_iCurBillIndex() == -1)) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000010"));
      return;
    }
    String corderid = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();

    FlowStateDlg approvestatedlg = new FlowStateDlg(this, "61", corderid);
    approvestatedlg.showModal();
  }

  private boolean onSave()
  {
    if (!getBillCardPanel().getBillData().execValidateFormulas()) {
      return false;
    }

    int rowCount = getBillCardPanel().getBillModel().getRowCount();

    if (this.m_billState != ScConstants.STATE_OTHER) {
      for (int i = rowCount - 1; i >= 0; i--) {
        Object tempObj = getBillCardPanel().getBillModel().getValueAt(i, "cinventorycode");
        if ((tempObj == null) || (tempObj.toString().trim().equals(""))) {
          getBillCardPanel().getBillModel().delLine(new int[] { i });
        }

      }

    }

    String lstr_biztype = getBillCardPanel().getHeadItem("cbiztype").getValue();
    if ((lstr_biztype == null) || (lstr_biztype.trim().length() == 0)) {
      getBillCardPanel().getHeadItem("cbiztype").setValue(getBillCardPanel().getBusiType());
    }
    try
    {
      ScTool.validateNotNullField(getBillCardPanel());
    } catch (Exception e) {
      SCMEnv.out(e);
      if ((e instanceof BusinessException))
        showErrorMessage(e.getMessage());
      return false;
    }
    try
    {
      long s = System.currentTimeMillis();
      if ((this.m_billState == ScConstants.STATE_ADD) || (this.m_billState == ScConstants.STATE_OTHER))
      {
        getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(8);
        getBillCardPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(8);

        this.m_curOrderVO = ((OrderVO)getBillCardPanel().getBillValueChangeVO(OrderVO.class.getName(), OrderHeaderVO.class.getName(), OrderItemVO.class.getName()));
        this.m_curOrderVO.setPk_corp(getPk_corp());

        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setDauditdate(null);
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setCauditpsn(null);

        UIRefPane ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("vmemo").getComponent();
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setVmemo(ref.getUITextField().getText());

        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setIbillstatus(BillStatus.FREE);

        String tTime = new UFDateTime(new Date()).toString();
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setCcuroperator(getOperatorID());
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setTmaketime(tTime);
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setTaudittime(null);
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setTlastmaketime(tTime);

        this.m_curOrderVO.validate();

        String cwareid = getBillCardPanel().getHeadItem("cwareid").getValue();
        if (!BillEdit.validateInv(getBillCardPanel(), getPk_corp(), cwareid, "cbaseid")) {        //????????????
          return false;
        }

        if (!calNativeAndAssistCurrValue(this.m_curOrderVO)) {
          return false;
        }

        if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
          return false;
        }

        String ls_HeadWareHouseOrg = ((UIRefPane)getBillCardPanel().getHeadItem("cwareid").getComponent()).getRefPK().trim();

        for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
          Object l_ob = getBillCardPanel().getBillModel().getValueAt(i, "warehouseorg");
          if ((l_ob == null) || (l_ob.toString().trim().equals("")) || 
            (ls_HeadWareHouseOrg.equals(l_ob.toString().trim()))) continue;
          throw new ValidationException(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000051"));
        }

        String curPK = null;

        ArrayList userObj = new ArrayList();
        userObj.add(getOperatorID());
        userObj.add(new Integer(0));
        userObj.add("cvendormangid");

        ArrayList aryRet = null;
 
        OrderItemVO[] tempVO = (OrderItemVO[])(OrderItemVO[])this.m_curOrderVO.getChildrenVO();
        if ((tempVO[0].getCupsourcebilltype() != null) && (tempVO[0].getCupsourcebilltype().equals("20"))) {
        	
           //edit by yqq 2017-02-28 参照请购单保存时界面不更新
			boolean bConfirm = false;
			this.m_curOrderVO.setUserConfirmFlag(new UFBoolean(false));
			do {
				if (this.m_curOrderVO.getUserConfirmFlag().booleanValue()) break;
				try {
					if (bConfirm)this.m_curOrderVO.setUserConfirmFlag(new UFBoolean(true));
					aryRet = (ArrayList) PfUtilClient.processActionNoSendMessage(this, "SAVEBASE","61", ClientEnvironment.getInstance().getDate().toString(),this.m_curOrderVO, userObj, null, null);
				} catch (RwtPiToPoException ex) {
		              ex.printStackTrace(System.out);
		              if (MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), ex.getMessage() + NCLangRes.getInstance().getStrByID("401201", "UPP401201-000064")) == 4)
		              {
		            	  bConfirm = true;
					} else
						return false;
				} catch (RwtPiToScException ex) {
					SCMEnv.out(ex);
		              if (MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), ex.getMessage() + NCLangRes.getInstance().getStrByID("401201", "UPP401201-000064")) == 4)
		              {
		            	  bConfirm = true;
					} else
						return false;
				}
			}

			while (bConfirm);
		//end by yqq 2017-02-28 参照请购单保存时界面不更新       	
/*          boolean bConfirmed = false;
          this.m_curOrderVO.setUserConfirmFlag(new UFBoolean(false));
          
          while (!this.m_curOrderVO.getUserConfirmFlag().booleanValue()) {       	  
            try {
              if (bConfirmed) this.m_curOrderVO.setUserConfirmFlag(new UFBoolean(true));
              aryRet = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "SAVEBASE", "61", ClientEnvironment.getInstance().getDate().toString(),this.m_curOrderVO, userObj, null, null);
            } catch (RwtScToPrException ex) {
              ex.printStackTrace(System.out);
              if (MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), ex.getMessage() + NCLangRes.getInstance().getStrByID("401201", "UPP401201-000064")) == 4)
              {
                bConfirmed = true;
              }else return false;
              
            }
            if (bConfirmed) continue;
          }*/
        } else {
          aryRet = (ArrayList)PfUtilClient.processActionNoSendMessage(this, "SAVEBASE", "61", ClientEnvironment.getInstance().getDate().toString(), this.m_curOrderVO, userObj, null, null);
        }
        this.m_curOrderVO = ((OrderVO)aryRet.get(1));
        if (this.m_curOrderVO != null) {
          curPK = ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getCorderid();
        }

        if (curPK == null) {
          return false;
        }
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setCoperator(getOperatorID());

        this.m_listVOs.add((OrderHeaderVO)this.m_curOrderVO.getParentVO());
        this.m_iCurBillIndex = (this.m_listVOs.size() - 1);

        if ((this.m_bAutoSendToAudit) && (ScTool.isNeedSendToAudit("61", this.m_curOrderVO))) {
          PfUtilClient.processAction("SAVE", "61", ClientEnvironment.getInstance().getDate().toString(), this.m_curOrderVO);
        }

        if (this.m_billState == ScConstants.STATE_ADD)
        {
          OrderHeaderVO[] headVOs = (OrderHeaderVO[])(OrderHeaderVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs(OrderHeaderVO.class.getName());
          int num = headVOs.length;
          OrderHeaderVO[] newBillHVO = new OrderHeaderVO[num + 1];
          for (int i = 0; i < num; i++)
            newBillHVO[i] = headVOs[i];
          newBillHVO[num] = ((OrderHeaderVO)this.m_curOrderVO.getParentVO());
          newBillHVO[num].setCorderid(curPK);

          getBillListPanel().setHeaderValueVO(newBillHVO);
          getBillListPanel().getHeadBillModel().execLoadFormula();
        }

      }

      if (this.m_billState == ScConstants.STATE_MODIFY)
      {
        String key = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();
        Object corder_bid = "";
        for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
          getBillCardPanel().setBodyValueAt(key, i, "corderid");

          Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
          if ((value != null) && (!value.toString().trim().equals("")))
            corder_bid = getBillCardPanel().getBodyValueAt(i, "corder_bid");
          else {
            getBillCardPanel().setBodyValueAt(corder_bid, i, "corder_bid");
          }
        }

        getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(8);
        getBillCardPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(8);

        this.m_curOrderVO = ((OrderVO)getBillCardPanel().getBillValueChangeVO(OrderVO.class.getName(), OrderHeaderVO.class.getName(), OrderItemVO.class.getName()));

        OrderItemVO[] items = (OrderItemVO[])(OrderItemVO[])this.m_curOrderVO.getChildrenVO();
        for (int i = 0; i < items.length; i++) {
          items[i].setPk_corp(getPk_corp());
          if ((items[i].getCorder_bid() == null) || (items[i].getCorder_bid().toString().trim().equals("")) || 
            (items[i].getStatus() == 3)) continue;
          items[i].setStatus(1);
        }

        UIRefPane ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("vmemo").getComponent();
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setVmemo(ref.getUITextField().getText());

        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setCcuroperator(getOperatorID());

        this.m_curOrderVO.validate(true);

        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setIbillstatus(BillStatus.FREE);
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setCauditpsn(null);
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setDauditdate(null);

        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setTlastmaketime(new UFDateTime(new Date()).toString());
        ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).setTaudittime(null);

        String cwareid = getBillCardPanel().getHeadItem("cwareid").getValue();
        if (!BillEdit.validateInv(getBillCardPanel(), getPk_corp(), cwareid, "cbaseid")) {
          return false;
        }

        if (!calNativeAndAssistCurrValue(this.m_curOrderVO)) {
          return false;
        }

        if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
          return false;
        }

        String ls_HeadWareHouseOrg = ((UIRefPane)getBillCardPanel().getHeadItem("cwareid").getComponent()).getRefPK().trim();

        for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
          Object l_ob = getBillCardPanel().getBillModel().getValueAt(i, "warehouseorg");
          if ((l_ob == null) || (l_ob.toString().trim().equals("")) || 
            (ls_HeadWareHouseOrg.equals(l_ob.toString().trim()))) continue;
          throw new ValidationException(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000051"));
        }

        ArrayList userObj = new ArrayList();
        userObj.add(getOperatorID());
        userObj.add(new Integer(0));
        userObj.add("cvendormangid");

        ArrayList aryRet = null;
        OrderItemVO[] tempVO = (OrderItemVO[])(OrderItemVO[])this.m_curOrderVO.getChildrenVO();

        if ((tempVO != null) && (tempVO.length > 0) && (tempVO[0].getCupsourcebilltype() != null) && (tempVO[0].getCupsourcebilltype().equals("20"))) {
          boolean bConfirmed = false;
          this.m_curOrderVO.setUserConfirmFlag(new UFBoolean(false));
          while (!this.m_curOrderVO.getUserConfirmFlag().booleanValue()) {
            try {
              if (bConfirmed) this.m_curOrderVO.setUserConfirmFlag(new UFBoolean(true));
              aryRet = (ArrayList)PfUtilClient.processAction("SAVEBASE", "61", ClientEnvironment.getInstance().getDate().toString(), this.m_curOrderVO, userObj);
            } catch (RwtScToPrException ex) {
              ex.printStackTrace(System.out);
              if (MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), ex.getMessage() + NCLangRes.getInstance().getStrByID("401201", "UPP401201-000064")) == 4)
              {
                bConfirmed = true;
              }
              else return false;
            }

            if (bConfirmed) continue;
          }
        } else {
          aryRet = (ArrayList)PfUtilClient.processAction("SAVEBASE", "61", ClientEnvironment.getInstance().getDate().toString(), this.m_curOrderVO, userObj);
        }

        this.m_curOrderVO = ((OrderVO)aryRet.get(1));

        this.m_listVOs.set(getM_iCurBillIndex(), (OrderHeaderVO)this.m_curOrderVO.getParentVO());

        if ((this.m_bAutoSendToAudit) && (ScTool.isNeedSendToAudit("61", this.m_curOrderVO))) {
          PfUtilClient.processAction("SAVE", "61", ClientEnvironment.getInstance().getDate().toString(), this.m_curOrderVO);
        }

        if (!PfUtilClient.isSuccess()) {
          return false;
        }
      }

      //edit by yqq 2017-02-28
      getBillCardPanel().updateUI();
 //     getBillCardPanel().updateValue();

      SCMEnv.out("保存结束：" + (System.currentTimeMillis() - s) / 1000L + "秒！");

      showHintMessage("保存成功");

      if ((this.m_billState == ScConstants.STATE_OTHER) && (getBillListPanel().getHeadBillModel().getRowCount() != 0))
      {
        this.cardLayout.last(this);
        setButtons(this.m_btnTree.getButtonArray());
        setButtonsState();
        getBillListPanel().loadBodyData(0);
        updateUI();
        return false;
      }
    }
    catch (ValidationException e)
    {
      showErrorMessage(e.getMessage());
      return false;
    }
    catch (Exception e) {
      SCMEnv.out("回退单据号开始...");
      try {
        if ((this.m_billState == ScConstants.STATE_OTHER) && (((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getVordercode() != null))
          PublicHelper.returnBillCode(this.m_curOrderVO);
      }
      catch (Exception ex) {
        SCMEnv.out("回退单据号异常结束");
      }
      SCMEnv.out("回退单据号正常结束");

      showErrorMessage("" + e.getMessage());
      SCMEnv.out(e);
      return false;
    }

    this.m_billState = ScConstants.STATE_CARD;
    setButtonsState();

    setPgUpDownButtonsState(getM_iCurBillIndex());

    refreshData();

    SourceBillTool.loadSourceInfoAll(getBillCardPanel().getBillModel(), "61");

    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH005"));

    return true;
  }

  private void onSelectAll()
  {
    int listHeadNum = getBillListPanel().getHeadBillModel().getRowCount();
    if (listHeadNum <= 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000069"));
      return;
    }
    for (int i = 0; i < listHeadNum; i++) {
      getBillListPanel().getHeadBillModel().setRowState(i, 4);
    }
    if (listHeadNum > 0) {
      getBillListPanel().getHeadTable().setRowSelectionInterval(0, listHeadNum - 1);
    }
    getBillListPanel().getHeadTable().updateUI();

    getBillListPanel().getBodyBillModel().clearBodyData();

    this.boEdit.setEnabled(false);
    this.boDel.setEnabled(false);
    this.boDocument.setEnabled(false);
    this.boReturn.setEnabled(true);

    int rowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
    if (rowCount > 0) {
      this.boDocument.setEnabled(true);
      this.m_btnOthersFuncs.setEnabled(true);
    }
    if (rowCount == 1) {
      this.boEdit.setEnabled(true);
    }

    updateButtons();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000033"));
  }

  private void onSelectNo()
  {
    int listHeadNum = getBillListPanel().getHeadBillModel().getRowCount();
    if (listHeadNum <= 0) {
      showHintMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000069"));
      return;
    }
    getBillListPanel().getHeadTable().clearSelection();
    for (int i = 0; i < listHeadNum; i++) {
      getBillListPanel().getHeadBillModel().setRowState(i, 0);
    }
    clearList();
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "4004COMMON000000034"));
  }

  private void onSendAudit(OrderVO vo)
  {
    if (vo == null) {
      return;
    }

    if ((this.m_bAutoSendToAudit) || (!ScTool.isNeedSendToAudit("61", vo)))
    {
      showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000052"));
      return;
    }

    try
    {
      PfUtilClient.processAction("SAVE", "61", ClientEnvironment.getInstance().getDate().toString(), vo);

      this.boSendAudit.setEnabled(false);
      updateButtons();

      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000266"));
    }
    catch (Exception e)
    {
      showErrorMessage(e.getMessage());
      SCMEnv.out(e);
      return;
    }
  }

  private void onSendMaterial()
  {
    try
    {
      Vector delRow = null;

      int rowcount = getBillCardPanel().getRowCount();

      for (int i = 0; i < rowcount; i++) {
        Object value = getBillCardPanel().getBodyValueAt(i, "bismaterial");
        if ((value == null) || (!value.toString().trim().equals(ScConstants.PROCESSFLAG))) {
          continue;
        }
        delRow = new Vector();
        for (int j = i + 1; j < rowcount; j++) {
          value = getBillCardPanel().getBodyValueAt(j, "bismaterial");
          if ((value != null) && (!value.toString().trim().equals("")))
            break;
          delRow.addElement("" + j);
        }
        int[] allrow = new int[delRow.size()];
        for (int k = 0; k < delRow.size(); k++) {
          allrow[k] = new Integer(delRow.elementAt(k).toString()).intValue();
        }

        getBillCardPanel().getBillModel().delLine(allrow);
        rowcount -= allrow.length;

        DisConditionVO condition = BillEdit.getDisConditionVO(getPk_corp(), ClientEnvironment.getInstance().getDate(), getOperatorID(), getBillCardPanel().getHeadItem("cwareid").getValue());
        value = getBillCardPanel().getBodyValueAt(i, "cbaseid");
        condition.setWlbmid(value.toString());
        value = getBillCardPanel().getBodyValueAt(i, "pk_measdoc");
        condition.setJldwid(value.toString());
        condition.setSl(new UFDouble("1"));

        DisassembleVO[] disassembleVOs = BillEdit.getBomVO(condition);

        int mateCount = BillEdit.insertItemVO(getBillCardPanel(), i, disassembleVOs);

        rowcount += mateCount;
      }

      getBillCardPanel().getBillModel().execLoadFormula();
    }
    catch (Exception e) {
      SCMEnv.out("发料失败");
      SCMEnv.out(e);
      return;
    }
  }

  public void onUnAudit()
  {
    showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH049"));
    try
    {
      String corderid = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getCorderid();

      OrderVO vo = OrderHelper.findByPrimaryKey(corderid);
      if (vo == null) {
        showWarningMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000042"));
      } else {
        ((OrderHeaderVO)vo.getParentVO()).setDauditdate(null);
        ((OrderHeaderVO)vo.getParentVO()).setCauditpsn(getOperatorID());
        ((OrderHeaderVO)vo.getParentVO()).setCcuroperator(getOperatorID());
        ((OrderHeaderVO)vo.getParentVO()).setTaudittime(null);

        PfUtilClient.processBatch(null, "UNAPPROVE" + getOperatorID(), "61", ClientEnvironment.getInstance().getDate().toString(), new OrderVO[] { vo }, null);
        if (!PfUtilClient.isSuccess()) {
          showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000185"));
          return;
        }
        loadData(null);

        if (this.m_listVOs.size() > getM_iCurBillIndex()) {
          ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).setDauditdate(null);
          ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).setCauditpsn(null);
          ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).setIbillstatus(BillStatus.FREE);
        }

        this.m_curOrderVO = OrderHelper.findByPrimaryKey(corderid);
        this.m_listVOs.set(getM_iCurBillIndex(), this.m_curOrderVO.getParentVO());

        setButtonsState();
      }
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UCH011"));
    } catch (Exception e) {
      SCMEnv.out(e);
      showErrorMessage(e.getMessage());
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000185"));
      return;
    }
  }

  private void refLoadData(int listRow)
  {
    ArrayList list = new ArrayList();
    try
    {
      this.cardLayout.first(this);
      setButtons(this.m_btnTree.getButtonArray());

      OrderVO vo = (OrderVO)this.comeVOs.get(listRow);

      getBillCardPanel().setBillValueVO(vo);
      getBillCardPanel().getBillModel().execLoadFormula();
      int rowcount = vo.getChildrenVO().length;

      OrderHeaderVO voHead = (OrderHeaderVO)vo.getParentVO();
      String[] saKey = { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10" };
      int iLen = saKey.length;
      for (int i = 0; i < iLen; i++) {
        JComponent component = getBillCardPanel().getHeadItem(saKey[i]).getComponent();
        if ((component instanceof UIRefPane)) {
          voHead.setAttributeValue(saKey[i], ((UIRefPane)component).getText());
        }

      }

      this.m_sHeadVmemo = ((OrderHeaderVO)vo.getParentVO()).getVmemo();
      UIRefPane ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("vmemo").getComponent();
      ref.setText(this.m_sHeadVmemo);

      for (int i = 0; i < rowcount; i++) {
        String sBodyVmemo = ((OrderItemVO[])(OrderItemVO[])vo.getChildrenVO())[i].getVmemo();
        ref = (UIRefPane)this.m_BillCardPanel.getBodyItem("vmemo").getComponent();
        ref.setText(sBodyVmemo);
      }

      for (int i = 0; i < rowcount; i++) {
        getBillCardPanel().setBodyValueAt(ScConstants.PROCESSFLAG, i, "bismaterial");
        getBillCardPanel().setBodyValueAt("100", i, "ndiscountrate");

        getBillCardPanel().setBodyValueAt(new Integer(1), i, "idiscounttaxtype");

        Object arrDate = getBillCardPanel().getBodyValueAt(i, "dplanarrvdate");
        if ((arrDate != null) && (!arrDate.toString().trim().equals("")))
          continue;
        int advancedDays = BillEdit.getAdvanceDays(getBillCardPanel(), i);
        getBillCardPanel().setBodyValueAt(ClientEnvironment.getInstance().getDate().getDateAfter(advancedDays), i, "dplanarrvdate");
      }

      getBillCardPanel().updateUI();

      for (int i = 0; i < rowcount; i++) {
        list.add(getBillCardPanel().getBodyValueAt(i, "cmangid"));
      }
      list = AdjustbillHelper.queryFreeVOByInvIDs(list);

      for (int i = 0; i < rowcount; i++)
      {
        int[] descriptions = { 0, 1, 2, 18, 9, 8, 7, 15, 11, 13, 12, 14 };

        Object oTemp = getBillCardPanel().getBodyValueAt(i, "idiscounttaxtype");
        String lStr_discounttaxtype = ScConstants.TaxType_Not_Including;
        if (oTemp != null) {
          lStr_discounttaxtype = oTemp.toString();
        }

        String[] keys = { lStr_discounttaxtype, "idiscounttaxtype", "nordernum", "norgtaxprice", "noriginalcurprice", "norgnettaxprice", "noriginalnetprice", "ndiscountrate", "ntaxrate", "noriginalcurmny", "noriginaltaxmny", "noriginalsummny" };

        Object nordernum = getBillCardPanel().getBodyValueAt(i, "nordernum");
        BillEdit.editAssistUnit(getBillCardPanel(), i, "cinventorycode");
        getBillCardPanel().setBodyValueAt(nordernum, i, "nordernum");
        getBillCardPanel().setCellEditable(i, "nordernum", true);
        Object measrate = getBillCardPanel().getBodyValueAt(i, "measrate");
        if ((measrate != null) && (!measrate.toString().equals("")) && (nordernum != null) && (!nordernum.toString().equals(""))) {
          UFDouble uOrdernum = new UFDouble(nordernum.toString());
          UFDouble uMeasrate = new UFDouble(measrate.toString());
          getBillCardPanel().setBodyValueAt(uOrdernum.div(uMeasrate), i, "nassistnum");
        }
        String formula = "isassist->getColValue(bd_invbasdoc,assistunit,pk_invbasdoc,cbaseid)";
        getBillCardPanel().getBillModel().execFormula(i, new String[] { formula });

        if (list == null)
          BillEdit.editFreeItemAlready(getBillCardPanel(), i, "cinventorycode", "cbaseid", "cmangid", null);
        else {
          BillEdit.editFreeItemAlready(getBillCardPanel(), i, "cinventorycode", "cbaseid", "cmangid", (FreeVO)list.get(i));
        }

        nordernum = getBillCardPanel().getBodyValueAt(i, "nordernum");
        BillItem item = getBillCardPanel().getBodyItem("nordernum");
        BillEditEvent e = new BillEditEvent(new BillCellEditor((UIRefPane)item.getComponent()), nordernum, "nordernum", i);
        RelationsCal.calculate(e, getBillCardPanel(), new int[] { this.m_iPricePolicy }, descriptions, keys, OrderItemVO.class.getName());

        item = getBillCardPanel().getBodyItem("noriginalcurprice");
        Object noriginalcurprice = getBillCardPanel().getBodyValueAt(i, "noriginalcurprice");
        BillEditEvent e1 = new BillEditEvent(new BillCellEditor((UIRefPane)item.getComponent()), null, noriginalcurprice, "noriginalcurprice", i, 1);
        RelationsCal.calculate(e1, getBillCardPanel(), new int[] { this.m_iPricePolicy }, descriptions, keys, OrderItemVO.class.getName());

        String creciever = ((OrderHeaderVO)vo.getParentVO()).getCreciever();
        if ((creciever != null) && (!creciever.trim().equals(""))) {
          setReceiveAddress(i, "creciever");
        }

        BillEdit.editCust(getBillCardPanel(), "cvendormangid");
        try
        {
          Object curCurrtype = getBillCardPanel().getBodyValueAt(i, "ccurrencytypeid");
          if ((curCurrtype == null) || (curCurrtype.toString().equals(""))) {
            Object cvendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValue();
            if ((cvendormangid != null) && (!cvendormangid.toString().equals("")))
            {
              CustDefaultVO custVO = OrderBHelper.getDefaultByCust(cvendormangid.toString());
              getBillCardPanel().setBodyValueAt(custVO.getCcurrtype(), i, "ccurrencytypeid");

              getBillCardPanel().getBillModel().execLoadFormula();
            }

          }

          Object curCurrtype2 = getBillCardPanel().getBodyValueAt(i, "ccurrencytypeid");
          if ((curCurrtype2 == null) || (curCurrtype2.toString().equals(""))) {
            getBillCardPanel().setBodyValueAt(this.m_sPKCurrencyType, i, "ccurrencytypeid");
          }

          getBillCardPanel().getBillModel().execLoadFormula();

          setCurrencyState(i, true);
          getBillCardPanel().updateUI();
        } catch (Exception t) {
          t.printStackTrace();
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }

    loadFreeItemsAlready(list);

    SourceBillTool.loadSourceInfoAll(getBillCardPanel().getBillModel(), "61");

    getBillCardPanel().getBillTable().clearSelection();

    getBillListPanel().getComeVO().remove(listRow);
    getBillListPanel().loadHeadData();
    this.boEdit.setEnabled(false);
    setButtonsState();

    updateButtons();

    for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
      Object sourceType = getBillCardPanel().getBodyValueAt(i, "cordersource");
      if ((sourceType != null) && ((sourceType.toString().trim().equals("Z1")) || (sourceType.toString().trim().equals("Z2")))) {
        getBillCardPanel().setCellEditable(i, "noriginalcurprice", false);
        getBillCardPanel().setCellEditable(i, "noriginalnetprice", false);
        getBillCardPanel().setCellEditable(i, "norgtaxprice", false);
        getBillCardPanel().setCellEditable(i, "norgnettaxprice", false);
      }
      else
      {
        getBillCardPanel().setCellEditable(i, "noriginalcurprice", true);
        getBillCardPanel().setCellEditable(i, "noriginalnetprice", true);
        getBillCardPanel().setCellEditable(i, "norgtaxprice", true);
        getBillCardPanel().setCellEditable(i, "norgnettaxprice", true);
      }
    }
  }

  private void refreshData()
  {
    long tTime = System.currentTimeMillis();

    this.m_curOrderVO.calPriceIncTax();
    getBillCardPanel().setBillValueVO(this.m_curOrderVO);
    loadCustBank();
    getBillCardPanel().setBillValueVO(this.m_curOrderVO);

    this.m_sHeadVmemo = ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getVmemo();
    UIRefPane ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("vmemo").getComponent();
    ref.setText(this.m_sHeadVmemo);

    OrderHeaderVO voHead = (OrderHeaderVO)this.m_curOrderVO.getParentVO();
    String[] saKey = { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10" };
    int iLen = saKey.length;
    for (int i = 0; i < iLen; i++) {
      JComponent component = getBillCardPanel().getHeadItem(saKey[i]).getComponent();
      if ((component instanceof UIRefPane)) {
        voHead.setAttributeValue(saKey[i], ((UIRefPane)component).getText());
      }
    }

    getBillCardPanel().getBillModel().execLoadFormula();

    int num = getBillCardPanel().getRowCount();

    Vector vTemp1 = new Vector();
    Vector vTemp2 = new Vector();
    for (int i = 0; i < num; i++) {
      Object pk_invbasdoc = getBillCardPanel().getBodyValueAt(i, "cbaseid");
      Object pk_measdoc = getBillCardPanel().getBodyValueAt(i, "cassistunit");
      if ((pk_invbasdoc != null) && (pk_measdoc != null)) {
        vTemp1.addElement((String)pk_invbasdoc);
        vTemp2.addElement((String)pk_measdoc);
      }
    }
    String[] cbaseid = new String[vTemp1.size()];
    String[] cmeasid = new String[vTemp2.size()];
    vTemp1.copyInto(cbaseid);
    vTemp2.copyInto(cmeasid);

    ArrayList list = new ArrayList();
    for (int i = 0; i < num; i++) {
      list.add(getBillCardPanel().getBodyValueAt(i, "cmangid"));
    }

    ArrayList listCombine = null;
    try {
      listCombine = OrderHelper.findConvertsandFreeVO(cbaseid, cmeasid, list);
    } catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000053"));
      SCMEnv.out(e);
    }

    BD_ConvertVO[] convertVO = (BD_ConvertVO[])(BD_ConvertVO[])listCombine.get(0);
    ArrayList list2 = (ArrayList)listCombine.get(1);

    loadMeasRate(convertVO);
    getBillCardPanel().updateValue();
    loadFreeItems(list2);
    loadPrecision();

    OrderItemVO[] orderItemVO = (OrderItemVO[])(OrderItemVO[])this.m_curOrderVO.getChildrenVO();
    for (int i = 0; i < orderItemVO.length; i++) {
      Integer idiscounttaxtype = orderItemVO[i].getIdiscounttaxtype();

      getBillCardPanel().getBillModel().setValueAt(idiscounttaxtype, i, "idiscounttaxtype");
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000253"));
    SCMEnv.out("数据加载：[共用时" + (System.currentTimeMillis() - tTime) / 1000L + "秒]");

    int ibillstate = ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getIbillstatus().intValue();

    String cbiztype = voHead.getCbiztype();
    if ((cbiztype != null) && (cbiztype.trim().length() > 0))
    {
      BillPanelTool.initBusiAddBtns(this.boBusitype, this.boAdd, "61", getPk_corp());

      updateButtons();
    }
    for (int i = 0; i < this.boAction.getChildButtonGroup().length; i++) {
      if ((this.boAction.getChildButtonGroup()[i] == null) || 
        (this.boAction.getChildButtonGroup()[i].getTag() == null)) continue;
      if (this.boAction.getChildButtonGroup()[i].getTag().equals("APPROVE"))
        this.boAudit = this.boAction.getChildButtonGroup()[i];
      if (this.boAction.getChildButtonGroup()[i].getTag().equals("UNAPPROVE")) {
        this.boUnaudit = this.boAction.getChildButtonGroup()[i];
      }
    }

    setOperateState(ibillstate);

    if ((!this.m_bAutoSendToAudit) && (ScTool.isNeedSendToAudit("61", this.m_curOrderVO)) && (this.m_billState == ScConstants.STATE_CARD))
      this.boSendAudit.setEnabled(true);
    else {
      this.boSendAudit.setEnabled(false);
    }
    updateButtons();
  }

  private void resetTableCellRenderer()
  {
    String[] sCols = { "noriginalcurmny", "noriginaltaxmny", "noriginalsummny", "nmoney", "ntaxmny", "nsummny", "nassistcurmny", "nassisttaxmny", "nassistsummny", "nexchangeotobrate", "nexchangeotoarate" };

    for (int i = 0; i < sCols.length; i++)
      if ((getBillCardPanel().getBodyItem(sCols[i]) != null) && (getBillCardPanel().getBodyItem(sCols[i]).isShow()))
        getBillCardPanel().getBodyPanel().resetTableCellRenderer(sCols[i]);
  }

  private void rightButtonRightControl()
  {
    if ((this.boLineOperator == null) || (this.boLineOperator.getChildCount() == 0)) {
      getBillCardPanel().getCopyLineMenuItem().setEnabled(false);
      getBillCardPanel().getDelLineMenuItem().setEnabled(false);
      getBillCardPanel().getPasteLineMenuItem().setEnabled(false);
      getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(false);
    }
    else
    {
      getBillCardPanel().getCopyLineMenuItem().setEnabled(this.boCopyLine.isPower());
      getBillCardPanel().getDelLineMenuItem().setEnabled(this.boDelLine.isPower());
      getBillCardPanel().getPasteLineMenuItem().setEnabled(this.boPasteLine.isPower());

      getBillCardPanel().getPasteLineToTailMenuItem().setEnabled(this.boPasteLine.isPower());
    }
  }

  public void setBillVO(AggregatedValueObject vo)
  {
    if (this.m_listVOs.size() == 0)
      return;
    this.m_curOrderVO = ((OrderVO)vo);

    this.m_curOrderVO.calPriceIncTax();
    getBillCardPanel().setBillValueVO(this.m_curOrderVO);
    loadCustBank();
    getBillCardPanel().setBillValueVO(this.m_curOrderVO);

    this.m_sHeadVmemo = ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getVmemo();
    UIRefPane ref = (UIRefPane)this.m_BillCardPanel.getHeadItem("vmemo").getComponent();
    ref.setText(this.m_sHeadVmemo);

    OrderHeaderVO voHead = (OrderHeaderVO)this.m_curOrderVO.getParentVO();
    String[] saKey = { "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10" };
    int iLen = saKey.length;
    for (int i = 0; i < iLen; i++) {
      JComponent component = getBillCardPanel().getHeadItem(saKey[i]).getComponent();
      if ((component instanceof UIRefPane)) {
        voHead.setAttributeValue(saKey[i], ((UIRefPane)component).getText());
      }
    }

    long s1 = System.currentTimeMillis();

    getBillCardPanel().getBillModel().execLoadFormula();
    SCMEnv.out("执行公式[共用时" + (System.currentTimeMillis() - s1) + "]");

    loadMeasRate();
    getBillCardPanel().updateValue();
    loadFreeItems();
    loadPrecision();

    OrderItemVO[] orderItemVO = (OrderItemVO[])(OrderItemVO[])this.m_curOrderVO.getChildrenVO();
    for (int i = 0; i < orderItemVO.length; i++) {
      Integer idiscounttaxtype = orderItemVO[i].getIdiscounttaxtype();

      getBillCardPanel().getBillModel().setValueAt(idiscounttaxtype, i, "idiscounttaxtype");
    }

    SourceBillTool.loadSourceInfoAll(getBillCardPanel().getBillModel(), "61");
  }

  private void setButtonsState()
  {
    this.boFrash.setEnabled(this.m_bQueried);
    this.boCancelOut.setEnabled(false);

    if (this.m_billState == ScConstants.STATE_OTHER) {
      this.boCancel.setEnabled(!this.boEdit.isEnabled());
      this.boSave.setEnabled(!this.boEdit.isEnabled());
      this.boLineOperator.setEnabled(!this.boEdit.isEnabled());

      this.boAdd.setEnabled(false);
      this.boDel.setEnabled(false);
      this.boQuery.setEnabled(false);
      this.boBusitype.setEnabled(false);

      this.boAddLine.setEnabled(true);
      this.boDelLine.setEnabled(true);

      this.boCopyLine.setEnabled(true);
      this.boInsertLine.setEnabled(true);
      this.boPasteLine.setEnabled(true);

      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boLast.setEnabled(false);
      this.boFirst.setEnabled(false);

      this.boReturn.setEnabled(false);

      this.boSelectAll.setEnabled(false);
      this.boSelectNo.setEnabled(false);

      this.boCopy.setEnabled(false);

      this.boSendAudit.setEnabled(false);

      this.boAction.setEnabled(false);
      this.boCancelOut.setEnabled(true);
      this.boLocate.setEnabled(false);

      this.boFrash.setEnabled(false);
      setGroupButtonsState(new UFBoolean("N"));
      getBillCardPanel().setEnabled(true);
    }

    if (this.m_billState == ScConstants.STATE_LIST) {
      this.boAdd.setEnabled(false);
      this.boBusitype.setEnabled(false);
      this.boLocate.setEnabled(true);
      this.boQuery.setEnabled(true);

      this.boSelectAll.setEnabled(true);
      this.boSelectNo.setEnabled(true);
      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boLast.setEnabled(false);
      this.boFirst.setEnabled(false);
      this.boCopy.setEnabled(false);

      if (getBillListPanel().getHeadTable().getSelectedRowCount() == 1)
        setGroupButtonsState(new UFBoolean("Y"));
      else {
        setGroupButtonsState(new UFBoolean("N"));
      }
      if (this.m_listVOs.size() > 0) {
        this.boLocate.setEnabled(true);
        if (this.m_bAutoSendToAudit) {
          this.boSendAudit.setEnabled(false);
        }
        else {
          OrderVO l_curOrderVO = (OrderVO)getBillCardPanel().getBillValueVO(OrderVO.class.getName(), OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
          if ((((OrderHeaderVO)l_curOrderVO.getParentVO()).getCorderid() != null) && (!((OrderHeaderVO)l_curOrderVO.getParentVO()).getCorderid().trim().equals("")) && (ScTool.isNeedSendToAudit("61", l_curOrderVO)) && (this.m_billState == ScConstants.STATE_CARD))
            this.boSendAudit.setEnabled(true);
          else {
            this.boSendAudit.setEnabled(false);
          }

        }

        String status = ((OrderHeaderVO)this.m_listVOs.get(getM_iCurBillIndex())).getIbillstatus().toString();
        if ((status != null) && (status.equals(BillStatus.AUDITED.toString()))) {
          this.boAudit.setEnabled(false);
          this.boUnaudit.setEnabled(true);
          this.boEdit.setEnabled(false);
          this.boDel.setEnabled(false);
        } else {
          this.boAudit.setEnabled(true);
          this.boUnaudit.setEnabled(false);
          this.boEdit.setEnabled(true);
          this.boDel.setEnabled(true);
        }
      } else {
        this.boLocate.setEnabled(false);
        this.boSelectAll.setEnabled(false);
        this.boSelectNo.setEnabled(false);
        this.boEdit.setEnabled(false);
        this.boDel.setEnabled(false);
      }
    }

    if (this.m_billState == ScConstants.STATE_CARD)
    {
      this.boBusitype.setEnabled(true);
      this.boAdd.setEnabled(true);
      this.boEdit.setEnabled(false);
      this.boSelectAll.setEnabled(false);
      this.boSelectNo.setEnabled(false);
      this.boSave.setEnabled(false);
      this.boCancel.setEnabled(false);
      this.boDel.setEnabled(false);
      this.boQuery.setEnabled(true);

      this.boLineOperator.setEnabled(false);
      this.boAddLine.setEnabled(true);
      this.boDelLine.setEnabled(true);
      this.boCopyLine.setEnabled(true);
      this.boInsertLine.setEnabled(true);
      this.boPasteLine.setEnabled(true);

      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boLast.setEnabled(false);
      this.boFirst.setEnabled(false);

      this.boReturn.setEnabled(true);

      if ((this.m_listVOs == null) || (this.m_listVOs.size() == 0)) {
        this.boCopy.setEnabled(false);
        this.boAction.setEnabled(false);

        this.boSendAudit.setEnabled(false);
        this.boAudit.setEnabled(false);
        setGroupButtonsState(new UFBoolean("N"));
      }
      else {
        this.boCopy.setEnabled(true);

        if (this.m_bAutoSendToAudit) {
          this.boSendAudit.setEnabled(false);
        }
        else {
          OrderVO l_curOrderVO = (OrderVO)getBillCardPanel().getBillValueVO(OrderVO.class.getName(), OrderHeaderVO.class.getName(), OrderItemVO.class.getName());
          if ((((OrderHeaderVO)l_curOrderVO.getParentVO()).getCorderid() != null) && (!((OrderHeaderVO)l_curOrderVO.getParentVO()).getCorderid().trim().equals("")) && (ScTool.isNeedSendToAudit("61", l_curOrderVO)) && (this.m_billState == ScConstants.STATE_CARD))
            this.boSendAudit.setEnabled(true);
          else {
            this.boSendAudit.setEnabled(false);
          }

        }

        String status = getBillCardPanel().getHeadItem("ibillstatus").getValue();
        if ((status != null) && (status.equals(BillStatus.FREE.toString()))) {
          this.boAction.setEnabled(true);
          this.boAudit.setEnabled(true);
          this.boUnaudit.setEnabled(false);
          this.boEdit.setEnabled(true);
          this.boDel.setEnabled(true);
        } else if ((status != null) && (!status.equals(BillStatus.FREE.toString()))) {
          this.boAction.setEnabled(true);
          this.boAudit.setEnabled(false);
          this.boUnaudit.setEnabled(true);
          this.boEdit.setEnabled(false);
          this.boDel.setEnabled(false);
        }
        setPgUpDownButtonsState(getM_iCurBillIndex());
        setGroupButtonsState(new UFBoolean("Y"));
      }

      getBillCardPanel().setEnabled(false);
    }
    if (this.m_billState == ScConstants.STATE_ADD)
    {
      this.boAdd.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boCancel.setEnabled(true);
      this.boSave.setEnabled(true);
      this.boDel.setEnabled(false);
      this.boQuery.setEnabled(false);
      this.boBusitype.setEnabled(false);

      this.boLineOperator.setEnabled(true);
      this.boAddLine.setEnabled(true);
      this.boDelLine.setEnabled(true);
      this.boCopyLine.setEnabled(true);
      this.boInsertLine.setEnabled(true);
      this.boPasteLine.setEnabled(true);

      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boLast.setEnabled(false);
      this.boFirst.setEnabled(false);

      this.boSelectAll.setEnabled(false);
      this.boSelectNo.setEnabled(false);
      this.boFrash.setEnabled(false);
      this.boLocate.setEnabled(false);
      this.boReturn.setEnabled(false);

      this.boCopy.setEnabled(false);

      this.boSendAudit.setEnabled(false);

      this.boAction.setEnabled(false);

      setGroupButtonsState(new UFBoolean("N"));
      getBillCardPanel().setEnabled(true);
    }
    if (this.m_billState == ScConstants.STATE_MODIFY)
    {
      this.boAdd.setEnabled(false);
      this.boEdit.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boCancel.setEnabled(true);
      this.boSave.setEnabled(true);
      this.boDel.setEnabled(false);
      this.boQuery.setEnabled(false);
      this.boBusitype.setEnabled(false);

      this.boLineOperator.setEnabled(true);
      this.boAddLine.setEnabled(true);
      this.boDelLine.setEnabled(true);
      this.boCopyLine.setEnabled(true);
      this.boInsertLine.setEnabled(true);
      this.boPasteLine.setEnabled(true);

      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boLast.setEnabled(false);
      this.boFirst.setEnabled(false);

      this.boSelectAll.setEnabled(false);
      this.boSelectNo.setEnabled(false);
      this.boFrash.setEnabled(false);
      this.boLocate.setEnabled(false);
      this.boReturn.setEnabled(false);

      this.boCopy.setEnabled(false);

      this.boSendAudit.setEnabled(false);

      this.boAction.setEnabled(false);
      setGroupButtonsState(new UFBoolean("N"));
      getBillCardPanel().setEnabled(true);

      getBillCardPanel().getHeadItem("dorderdate").setEnabled(false);
    }

    updateButtons();
  }

  private void setGroupButtonsState(UFBoolean state)
  {
    this.m_btnPrints.setEnabled(state.booleanValue());
    this.m_btnPrintPreview.setEnabled(state.booleanValue());
    this.boPrint.setEnabled(state.booleanValue());
    this.btnBillCombin.setEnabled(state.booleanValue());

    this.m_btnOthersQueries.setEnabled(state.booleanValue());
    this.boLinkQuery.setEnabled(state.booleanValue());
    this.boQueryForAudit.setEnabled(state.booleanValue());

    this.m_btnOthersFuncs.setEnabled(state.booleanValue());
    this.boDocument.setEnabled(state.booleanValue());
  }

  private void setChangePrecision()
  {
    int row = getBillCardPanel().getRowCount();
    int[] mnyPrecision = new int[row];
    int[] localPrecision = new int[row];
    int[] fracPrecision = new int[row];

    for (int i = 0; i < row; i++) {
      Object mnyPre = getBillCardPanel().getBodyValueAt(i, "mnyprecision");
      Object localPre = getBillCardPanel().getBodyValueAt(i, "localprecision");
      Object fracPre = getBillCardPanel().getBodyValueAt(i, "fracprecision");
      mnyPrecision[i] = (mnyPre == null ? 2 : Integer.parseInt(mnyPre.toString()));
      localPrecision[i] = (localPre == null ? 2 : Integer.parseInt(localPre.toString()));
      fracPrecision[i] = (fracPre == null ? 2 : Integer.parseInt(fracPre.toString()));
    }

    String[] mnyCols = { "noriginalcurmny", "noriginaltaxmny", "noriginalsummny", "nmoney", "ntaxmny", "nsummny", "nassistcurmny", "nassisttaxmny", "nassistsummny" };

    for (int i = 0; i < mnyCols.length; i++)
    {
      BillItem item = getBillCardPanel().getBodyItem(mnyCols[i]);

      if (!item.isShow())
      {
        continue;
      }

      for (int j = 0; j < mnyPrecision.length; j++)
      {
        if ((mnyCols[i].equals("nmoney")) || (mnyCols[i].equals("ntaxmny")) || (mnyCols[i].equals("nsummny")))
        {
          getBillCardPanel().getBodyItem(mnyCols[i]).setDecimalDigits(this.m_intCurrencyDecimal);
        }
        else if ((mnyCols[i].equals("nassistcurmny")) || (mnyCols[i].equals("nassisttaxmny")) || (mnyCols[i].equals("nassistsummny")))
        {
          getBillCardPanel().getBodyItem(mnyCols[i]).setDecimalDigits(this.m_intCurrencyDecimalAssit);
        }
        else getBillCardPanel().getBodyItem(mnyCols[i]).setDecimalDigits(mnyPrecision[j]);

        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(j, mnyCols[i]), j, mnyCols[i]);
      }

    }

    BillItem item = getBillCardPanel().getBodyItem("nexchangeotobrate");
    if (item.isShow())
    {
      for (int j = 0; j < localPrecision.length; j++) {
        getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(localPrecision[j]);

        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(j, "nexchangeotobrate"), j, "nexchangeotobrate");
      }
    }

    item = getBillCardPanel().getBodyItem("nexchangeotoarate");
    if (item.isShow())
    {
      for (int j = 0; j < fracPrecision.length; j++) {
        getBillCardPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(fracPrecision[j]);
        getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(j, "nexchangeotoarate"), j, "nexchangeotoarate");
      }
    }
  }

  private void setChangePrecision(int aint_row)
  {
    int mnyPrecision = 2;
    int localPrecision = 2;
    int fracPrecision = 2;

    Object mnyPre = getBillCardPanel().getBodyValueAt(aint_row, "mnyprecision");
    Object localPre = getBillCardPanel().getBodyValueAt(aint_row, "localprecision");
    Object fracPre = getBillCardPanel().getBodyValueAt(aint_row, "fracprecision");
    mnyPrecision = mnyPre == null ? 2 : Integer.parseInt(mnyPre.toString());
    localPrecision = localPre == null ? 2 : Integer.parseInt(localPre.toString());
    fracPrecision = fracPre == null ? 2 : Integer.parseInt(fracPre.toString());

    String[] mnyCols = { "noriginalcurmny", "noriginaltaxmny", "noriginalsummny", "nmoney", "ntaxmny", "nsummny", "nassistcurmny", "nassisttaxmny", "nassistsummny" };

    for (int i = 0; i < mnyCols.length; i++)
    {
      BillItem item = getBillCardPanel().getBodyItem(mnyCols[i]);

      if (!item.isShow())
      {
        continue;
      }

      if ((mnyCols[i].equals("nmoney")) || (mnyCols[i].equals("ntaxmny")) || (mnyCols[i].equals("nsummny")))
      {
        getBillCardPanel().getBodyItem(mnyCols[i]).setDecimalDigits(this.m_intCurrencyDecimal);
      }
      else if ((mnyCols[i].equals("nassistcurmny")) || (mnyCols[i].equals("nassisttaxmny")) || (mnyCols[i].equals("nassistsummny")))
      {
        getBillCardPanel().getBodyItem(mnyCols[i]).setDecimalDigits(this.m_intCurrencyDecimalAssit);
      }
      else getBillCardPanel().getBodyItem(mnyCols[i]).setDecimalDigits(mnyPrecision);

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(aint_row, mnyCols[i]), aint_row, mnyCols[i]);
    }

    BillItem item = getBillCardPanel().getBodyItem("nexchangeotobrate");
    if (item.isShow())
    {
      getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(localPrecision);

      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(aint_row, "nexchangeotobrate"), aint_row, "nexchangeotobrate");
    }

    item = getBillCardPanel().getBodyItem("nexchangeotoarate");
    if (item.isShow())
    {
      getBillCardPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(fracPrecision);
      getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(aint_row, "nexchangeotoarate"), aint_row, "nexchangeotoarate");
    }
  }

  private void setRelateCntAndDefaultPrice(int[] aintary_rows, boolean abol_SkipCT)
  {
    try
    {
      Vector lVec_rows = new Vector();
      for (int i = 0; i < aintary_rows.length; i++) {
        if ((SCPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(i, "cpriceauditid")) != null) || 
          (getBillCardPanel().getBodyValueAt(aintary_rows[i], "cmangid") == null) || (getBillCardPanel().getBodyValueAt(aintary_rows[i], "cmangid").toString().length() <= 0)) continue;
        lVec_rows.addElement(new Integer(aintary_rows[i]));
      }

      if (lVec_rows.size() > 0) {
        aintary_rows = new int[lVec_rows.size()];
        for (int i = 0; i < lVec_rows.size(); i++)
          aintary_rows[i] = ((Integer)lVec_rows.elementAt(i)).intValue();
      }
      else {
        return;
      }

      int li_rowcount = aintary_rows.length;
      if (li_rowcount == 0) {
        return;
      }

      String lStr_vendorbasid = getBillCardPanel().getHeadItem("cvendorid").getValue();
      lStr_vendorbasid = SCPubVO.getString_TrimZeroLenAsNull(lStr_vendorbasid);

      String lStr_vendormangid = getBillCardPanel().getHeadItem("cvendormangid").getValue();
      lStr_vendormangid = SCPubVO.getString_TrimZeroLenAsNull(lStr_vendormangid);

      String lStr_orderdate = getBillCardPanel().getHeadItem("dorderdate").getValue();
      lStr_orderdate = SCPubVO.getString_TrimZeroLenAsNull(lStr_orderdate);

      String lStr_wareid = getBillCardPanel().getHeadItem("cwareid").getValue();
      lStr_wareid = SCPubVO.getString_TrimZeroLenAsNull(lStr_wareid);

      String[] lStrary_baseid = new String[li_rowcount];
      String[] lStrary_mangid = new String[li_rowcount];

      for (int i = 0; i < li_rowcount; i++) {
        int lint_rowindex = aintary_rows[i];
        lStrary_baseid[i] = SCPubVO.getString_TrimZeroLenAsNull((String)getBillCardPanel().getBodyValueAt(lint_rowindex, "cbaseid"));
        lStrary_mangid[i] = SCPubVO.getString_TrimZeroLenAsNull((String)getBillCardPanel().getBodyValueAt(lint_rowindex, "cmangid"));
      }

      String[] lStrary_vendorbasid = null;

      if ((lStr_vendorbasid != null) && (lStr_vendorbasid.trim().length() > 0)) {
        lStrary_vendorbasid = (String[])(String[])SCPubVO.getSameValueArray(lStr_vendorbasid, li_rowcount);
      }

      String[] lStrary_vendormangid = null;

      if ((lStr_vendormangid != null) && (lStr_vendormangid.trim().length() > 0)) {
        lStrary_vendormangid = (String[])(String[])SCPubVO.getSameValueArray(lStr_vendormangid, li_rowcount);
      }

      String lStr_ChangedKey = OrderItemVO.getPriceFieldByPricePolicy(this.m_iPricePolicy);

      boolean[] lbolary_findCtPrice = new boolean[li_rowcount];
      for (int i = 0; i < li_rowcount; i++) {
        lbolary_findCtPrice[i] = false;
      }

      if (!abol_SkipCT) {
        if (SCPubVO.isRelateCnt())
        {
          if ((this.m_bCTStartUp) && (lStrary_mangid != null) && (lStrary_mangid.length > 0) && (lStrary_vendormangid != null) && (lStrary_vendormangid.length > 0) && (lStr_orderdate != null) && (lStr_orderdate.trim().length() > 0))
          {
            RetCtToPoQueryVO[][] lary2_CtRetVO = ScFromCtHelper.queryForCntAll(getPk_corp(), lStrary_baseid, lStrary_vendorbasid, new UFDate(lStr_orderdate));
            if ((lary2_CtRetVO != null) && (lary2_CtRetVO.length > 0) && (lary2_CtRetVO.length == li_rowcount))
            {
              for (int i = 0; i < li_rowcount; i++) {
                int lint_rowindex = aintary_rows[i];

                String sUpSourceType = (String)getBillCardPanel().getBodyValueAt(lint_rowindex, "cupsourcebilltype");
                boolean lbol_SourceTypeIsCT = (sUpSourceType != null) && ((sUpSourceType.equals("Z1")) || (sUpSourceType.equals("Z2")));
                if (!lbol_SourceTypeIsCT) {
                  getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractid");
                  getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractrowid");
                  getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractrcode");

                  if ((getBillCardPanel().getBodyValueAt(lint_rowindex, "corder_bid") == null) || (getBillCardPanel().getBodyValueAt(lint_rowindex, "corder_bid").toString().trim().length() == 0))
                    getBillCardPanel().getBillModel().setRowState(lint_rowindex, 1);
                  else {
                    getBillCardPanel().getBillModel().setRowState(lint_rowindex, 2);
                  }
                }

              }

              boolean lbol_NeedRelateCnt = false;
              for (int i = 0; i < li_rowcount; i++) {
                int lint_rowindex = aintary_rows[i];
                String sUpSourceType = (String)getBillCardPanel().getBodyValueAt(lint_rowindex, "cupsourcebilltype");
                boolean lbol_SourceTypeIsCT = (sUpSourceType != null) && ((sUpSourceType.equals("Z1")) || (sUpSourceType.equals("Z2")));
                if ((lbol_SourceTypeIsCT) || 
                  (lary2_CtRetVO[i] == null) || (lary2_CtRetVO[i].length <= 0)) continue;
                int iRet = MessageDialog.showYesNoDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("40040200", "UPP40040200-000034"));

                if (iRet != 4) break;
                lbol_NeedRelateCnt = true;
                break;
              }

              if (lbol_NeedRelateCnt) {
                for (int i = 0; i < li_rowcount; i++) {
                  int lint_rowindex = aintary_rows[i];
                  String sUpSourceType = (String)getBillCardPanel().getBodyValueAt(lint_rowindex, "cupsourcebilltype");
                  boolean lbol_SourceTypeIsCT = (sUpSourceType != null) && ((sUpSourceType.equals("Z1")) || (sUpSourceType.equals("Z2")));
                  if ((lbol_SourceTypeIsCT) || 
                    (lary2_CtRetVO[i] == null) || (lary2_CtRetVO[i].length <= 0))
                    continue;
                  RetCtToPoQueryVO lCtRetVO_Selected = null;
                  if (lary2_CtRetVO[i].length == 1) {
                    lCtRetVO_Selected = lary2_CtRetVO[i][0];
                  }
                  else
                  {
                    String lStr_crowno = SCPubVO.getString_TrimZeroLenAsNull((String)getBillCardPanel().getBodyValueAt(lint_rowindex, "crowno"));

                    String lStr_Baseid = SCPubVO.getString_TrimZeroLenAsNull((String)getBillCardPanel().getBodyValueAt(lint_rowindex, "cbaseid"));

                    HashMap lHMP_HeadData = new HashMap();
                    lHMP_HeadData.put("crowno", lStr_crowno);
                    lHMP_HeadData.put("cbaseid", lStr_Baseid);

                    if (this.m_CntSelDlg == null)
                      this.m_CntSelDlg = new PoCntSelDlg(this);
                    this.m_CntSelDlg.setOnOK(false);
                    this.m_CntSelDlg.setCntData(lary2_CtRetVO[i], lHMP_HeadData);
                    this.m_CntSelDlg.showModal();

                    if (this.m_CntSelDlg.isON_OK())
                      lCtRetVO_Selected = this.m_CntSelDlg.getRetVO();
                    else {
                      lCtRetVO_Selected = null;
                    }
                  }

                  if (lCtRetVO_Selected != null)
                  {
                    getBillCardPanel().setBodyValueAt(lCtRetVO_Selected.getCContractID(), lint_rowindex, "ccontractid");
                    getBillCardPanel().setBodyValueAt(lCtRetVO_Selected.getCContractRowId(), lint_rowindex, "ccontractrowid");
                    getBillCardPanel().setBodyValueAt(lCtRetVO_Selected.getCContractCode(), lint_rowindex, "ccontractrcode");

                    getBillCardPanel().setBodyValueAt(lCtRetVO_Selected.getCCurrencyId(), lint_rowindex, "ccurrencytypeid");

                    if ((getBillCardPanel().getBodyValueAt(lint_rowindex, "corder_bid") == null) || (getBillCardPanel().getBodyValueAt(lint_rowindex, "corder_bid").toString().trim().length() == 0))
                      getBillCardPanel().getBillModel().setRowState(lint_rowindex, 1);
                    else {
                      getBillCardPanel().getBillModel().setRowState(lint_rowindex, 2);
                    }

                    getBillCardPanel().setCellEditable(lint_rowindex, "ccurrencytype", false);
                    setCurrencyState(lint_rowindex, true);
                    getBillCardPanel().updateUI();

                    UFDouble lUFD_Price = null;
                    lUFD_Price = SCPubVO.getPriceValueByPricePolicy(lCtRetVO_Selected, this.m_iPricePolicy);

                    if (lUFD_Price != null)
                    {
                      UFDouble lUFD_OldPrice = SCPubVO.getUFDouble_ValueAsValue(getBillCardPanel().getBodyValueAt(lint_rowindex, lStr_ChangedKey));

                      lbolary_findCtPrice[i] = true;

                      getBillCardPanel().setBodyValueAt(lCtRetVO_Selected.getCContractID(), lint_rowindex, "ccontractid");
                      getBillCardPanel().setBodyValueAt(lCtRetVO_Selected.getCContractRowId(), lint_rowindex, "ccontractrowid");
                      getBillCardPanel().setBodyValueAt(lCtRetVO_Selected.getCContractCode(), lint_rowindex, "ccontractrcode");

                      if ((getBillCardPanel().getBodyValueAt(lint_rowindex, "corder_bid") == null) || (getBillCardPanel().getBodyValueAt(lint_rowindex, "corder_bid").toString().trim().length() == 0))
                        getBillCardPanel().getBillModel().setRowState(lint_rowindex, 1);
                      else {
                        getBillCardPanel().getBillModel().setRowState(lint_rowindex, 2);
                      }

                      if ((lUFD_OldPrice == null) || (lUFD_Price.compareTo(lUFD_OldPrice) != 0)) {
                        getBillCardPanel().setBodyValueAt(lUFD_Price, lint_rowindex, lStr_ChangedKey);

                        getBillCardPanel().setCellEditable(lint_rowindex, "noriginalcurprice", false);
                        getBillCardPanel().setCellEditable(lint_rowindex, "norgtaxprice", false);

                        int[] descriptions = { 0, 1, 2, 18, 9, 8, 7, 15, 11, 13, 12, 14 };
                        Object oTemp = getBillCardPanel().getBodyValueAt(lint_rowindex, "idiscounttaxtype");
                        String lStr_discounttaxtype = ScConstants.TaxType_Not_Including;
                        if (oTemp != null) {
                          lStr_discounttaxtype = oTemp.toString();
                        }

                        String[] keys = { lStr_discounttaxtype, "idiscounttaxtype", "nordernum", "norgtaxprice", "noriginalcurprice", "norgnettaxprice", "noriginalnetprice", "ndiscountrate", "ntaxrate", "noriginalcurmny", "noriginaltaxmny", "noriginalsummny" };
                        BillEditEvent l_BillEditEventTemp = new BillEditEvent(getBillCardPanel().getBodyItem(lStr_ChangedKey).getComponent(), getBillCardPanel().getBodyValueAt(lint_rowindex, lStr_ChangedKey), lStr_ChangedKey, lint_rowindex);
                        RelationsCal.calculate(l_BillEditEventTemp, getBillCardPanel(), new int[] { this.m_iPricePolicy }, descriptions, keys, OrderItemVO.class.getName());
                      }
                    }
                    else
                    {
                      lbolary_findCtPrice[i] = false;
                    }
                  }
                  else {
                    lbolary_findCtPrice[i] = false;

                    getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractid");
                    getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractrowid");
                    getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractrcode");

                    getBillCardPanel().setCellEditable(lint_rowindex, "ccurrencytype", (getBillCardPanel().getBodyItem("ccurrencytype") != null) && (getBillCardPanel().getBodyItem("ccurrencytype").isEdit()));

                    getBillCardPanel().setCellEditable(lint_rowindex, "noriginalcurprice", (getBillCardPanel().getBodyItem("noriginalcurprice") != null) && (getBillCardPanel().getBodyItem("noriginalcurprice").isEdit()));
                    getBillCardPanel().setCellEditable(lint_rowindex, "norgtaxprice", (getBillCardPanel().getBodyItem("norgtaxprice") != null) && (getBillCardPanel().getBodyItem("norgtaxprice").isEdit()));

                    if ((getBillCardPanel().getBodyValueAt(lint_rowindex, "corder_bid") == null) || (getBillCardPanel().getBodyValueAt(lint_rowindex, "corder_bid").toString().trim().length() == 0))
                      getBillCardPanel().getBillModel().setRowState(lint_rowindex, 1);
                    else {
                      getBillCardPanel().getBillModel().setRowState(lint_rowindex, 2);
                    }

                  }

                }

              }

            }

          }

        }
        else if ((this.m_bCTStartUp) && (lStrary_mangid != null) && (lStrary_mangid.length > 0) && (lStrary_vendormangid != null) && (lStrary_vendormangid.length > 0) && (lStr_orderdate != null) && (lStr_orderdate.trim().length() > 0))
        {
          RetCtToPoQueryVO[] lary_CtRetVO = ScFromCtHelper.queryForCnt(getPk_corp(), lStrary_baseid, lStrary_vendorbasid, new UFDate(lStr_orderdate));

          if ((lary_CtRetVO != null) && (lary_CtRetVO.length > 0) && (lary_CtRetVO.length == li_rowcount))
          {
            for (int i = 0; i < li_rowcount; i++)
            {
              int lint_rowindex = aintary_rows[i];

              getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractid");
              getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractrowid");
              getBillCardPanel().setBodyValueAt(null, lint_rowindex, "ccontractrcode");

              if (lary_CtRetVO[i] != null)
              {
                UFDouble lUFD_Price = null;
                lUFD_Price = SCPubVO.getPriceValueByPricePolicy(lary_CtRetVO[i], this.m_iPricePolicy);

                if (lUFD_Price != null)
                {
                  UFDouble lUFD_OldPrice = SCPubVO.getUFDouble_ValueAsValue(getBillCardPanel().getBodyValueAt(lint_rowindex, lStr_ChangedKey));

                  lbolary_findCtPrice[i] = true;

                  getBillCardPanel().setBodyValueAt(lary_CtRetVO[i].getCContractID(), lint_rowindex, "ccontractid");
                  getBillCardPanel().setBodyValueAt(lary_CtRetVO[i].getCContractRowId(), lint_rowindex, "ccontractrowid");
                  getBillCardPanel().setBodyValueAt(lary_CtRetVO[i].getCContractCode(), lint_rowindex, "ccontractrcode");

                  if ((lUFD_OldPrice == null) || (lUFD_Price.compareTo(lUFD_OldPrice) != 0)) {
                    getBillCardPanel().setBodyValueAt(lUFD_Price, lint_rowindex, lStr_ChangedKey);

                    int[] descriptions = { 0, 1, 2, 18, 9, 8, 7, 15, 11, 13, 12, 14 };
                    Object oTemp = getBillCardPanel().getBodyValueAt(lint_rowindex, "idiscounttaxtype");
                    String lStr_discounttaxtype = ScConstants.TaxType_Not_Including;
                    if (oTemp != null) {
                      lStr_discounttaxtype = oTemp.toString();
                    }

                    String[] keys = { lStr_discounttaxtype, "idiscounttaxtype", "nordernum", "norgtaxprice", "noriginalcurprice", "norgnettaxprice", "noriginalnetprice", "ndiscountrate", "ntaxrate", "noriginalcurmny", "noriginaltaxmny", "noriginalsummny" };
                    BillEditEvent l_BillEditEventTemp = new BillEditEvent(getBillCardPanel().getBodyItem(lStr_ChangedKey).getComponent(), getBillCardPanel().getBodyValueAt(lint_rowindex, lStr_ChangedKey), lStr_ChangedKey, lint_rowindex);
                    RelationsCal.calculate(l_BillEditEventTemp, getBillCardPanel(), new int[] { this.m_iPricePolicy }, descriptions, keys, OrderItemVO.class.getName());
                  }
                }
                else
                {
                  lbolary_findCtPrice[i] = false;
                }
              } else {
                lbolary_findCtPrice[i] = false;
              }
            }

          }

        }

      }

      Vector lVec_needDefault = new Vector();
      if (!abol_SkipCT)
      {
        for (int i = 0; i < lbolary_findCtPrice.length; i++) {
        	
        	
        	//edit by yqq 2017-02-28  反编译改
            if (!lbolary_findCtPrice[i] ) {
       //   if (lbolary_findCtPrice[i] == 0) {
            lVec_needDefault.addElement(new Integer(aintary_rows[i]));
          }
        }
      }
      else {
        for (int i = 0; i < aintary_rows.length; i++) {
          lVec_needDefault.addElement(new Integer(aintary_rows[i]));
        }
      }

      if (lVec_needDefault.size() > 0) {
        int li_rowcount2 = lVec_needDefault.size();
        int[] lintary_needDefault = new int[li_rowcount2];
        for (int i = 0; i < li_rowcount2; i++) {
          lintary_needDefault[i] = ((Integer)lVec_needDefault.elementAt(i)).intValue();
        }

        String[] lStrary_mangid2 = new String[li_rowcount2];
        String[] lStrary_currencytypeid2 = new String[li_rowcount2];
        UFDouble[] lUFDary_BRate2 = new UFDouble[li_rowcount2];
        UFDouble[] lUFDary_ARate2 = new UFDouble[li_rowcount2];

        for (int i = 0; i < li_rowcount2; i++)
        {
          int lint_rowindex = lintary_needDefault[i];

          getBillCardPanel().setCellEditable(lint_rowindex, "noriginalcurprice", true);
          getBillCardPanel().setCellEditable(lint_rowindex, "norgtaxprice", true);
          getBillCardPanel().setCellEditable(lint_rowindex, "ccurrencytype", true);

          lStrary_mangid2[i] = SCPubVO.getString_TrimZeroLenAsNull((String)getBillCardPanel().getBodyValueAt(lint_rowindex, "cmangid"));
          lStrary_currencytypeid2[i] = SCPubVO.getString_TrimZeroLenAsNull((String)getBillCardPanel().getBodyValueAt(lint_rowindex, "ccurrencytypeid"));
          lUFDary_BRate2[i] = SCPubVO.getUFDouble_ValueAsValue(getBillCardPanel().getBodyValueAt(lint_rowindex, "nexchangeotobrate"));
          lUFDary_ARate2[i] = SCPubVO.getUFDouble_ValueAsValue(getBillCardPanel().getBodyValueAt(lint_rowindex, "nexchangeotoarate"));
        }

        RetScVrmAndParaPriceVO l_voPara = new RetScVrmAndParaPriceVO(1);
        l_voPara.setPk_corp(getPk_corp());
        l_voPara.setStoOrgId(lStr_wareid);
        l_voPara.setVendMangId(lStr_vendormangid);
        l_voPara.setSaInvMangId(lStrary_mangid2);
        l_voPara.setSaCurrId(lStrary_currencytypeid2);
        l_voPara.setDaBRate(lUFDary_BRate2);
        l_voPara.setDaARate(lUFDary_ARate2);
        l_voPara.setDOrderDate(lStr_orderdate == null ? null : new UFDate(lStr_orderdate));
        l_voPara.setClientLink(new ClientLink(ClientEnvironment.getInstance()));

        RetScVrmAndParaPriceVO l_voRetPrice = OrderHelper.queryVrmAndParaPrices(l_voPara);

        for (int i = 0; i < li_rowcount2; i++) {
          int lint_rowindex = lintary_needDefault[i];
          UFDouble lUFD_Price = l_voRetPrice.getPriceAt(i);
          if (lUFD_Price == null)
          {
            continue;
          }
          if (l_voRetPrice.isSetPriceNoTaxAt(i)) {
            lStr_ChangedKey = "noriginalcurprice";
          }

          UFDouble lUFD_OldPrice = SCPubVO.getUFDouble_ValueAsValue(getBillCardPanel().getBodyValueAt(lint_rowindex, lStr_ChangedKey));

          if ((lUFD_OldPrice == null) || (lUFD_Price.compareTo(lUFD_OldPrice) != 0)) {
            getBillCardPanel().setBodyValueAt(lUFD_Price, lint_rowindex, lStr_ChangedKey);
            getBillCardPanel().setCellEditable(lint_rowindex, "noriginalcurprice", true);
            getBillCardPanel().setCellEditable(lint_rowindex, "norgtaxprice", true);
            getBillCardPanel().setCellEditable(lint_rowindex, "ccurrencytype", true);

            int[] descriptions = { 0, 1, 2, 18, 9, 8, 7, 15, 11, 13, 12, 14 };
            Object oTemp = getBillCardPanel().getBodyValueAt(lint_rowindex, "idiscounttaxtype");
            String lStr_discounttaxtype = ScConstants.TaxType_Not_Including;
            if (oTemp != null) {
              lStr_discounttaxtype = oTemp.toString();
            }

            String[] keys = { lStr_discounttaxtype, "idiscounttaxtype", "nordernum", "norgtaxprice", "noriginalcurprice", "norgnettaxprice", "noriginalnetprice", "ndiscountrate", "ntaxrate", "noriginalcurmny", "noriginaltaxmny", "noriginalsummny" };
            BillEditEvent l_BillEditEventTemp = new BillEditEvent(getBillCardPanel().getBodyItem(lStr_ChangedKey).getComponent(), getBillCardPanel().getBodyValueAt(lint_rowindex, lStr_ChangedKey), lStr_ChangedKey, lint_rowindex);
            RelationsCal.calculate(l_BillEditEventTemp, getBillCardPanel(), new int[] { this.m_iPricePolicy }, descriptions, keys, OrderItemVO.class.getName());
          }

        }

      }

    }
    catch (Exception e)
    {
      showErrorMessage("" + e.getMessage());
      SCMEnv.out(e);
      return;
    }
  }

  private void setNotEditableWhenRelateCnt(int[] aintary_rows)
  {
    for (int i = 0; i < aintary_rows.length; i++) {
      int lint_rowindex = aintary_rows[i];
      Object lObj_ccontractrcode = getBillCardPanel().getBodyValueAt(lint_rowindex, "ccontractrcode");

      if ((lObj_ccontractrcode != null) && (lObj_ccontractrcode.toString().trim().length() > 0)) {
        Object lObj_ccurrencytype = getBillCardPanel().getBodyValueAt(lint_rowindex, "ccurrencytype");
        Object lObj_noriginalcurprice = getBillCardPanel().getBodyValueAt(lint_rowindex, "noriginalcurprice");
        Object lObj_norgtaxprice = getBillCardPanel().getBodyValueAt(lint_rowindex, "norgtaxprice");
        Object lObj_cupsourcebilltype = getBillCardPanel().getBodyValueAt(lint_rowindex, "cupsourcebilltype");

        RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(getBillCardPanel().getBodyValueAt(lint_rowindex, "ccontractrowid").toString());
        if ((voCntInfo != null) && (voCntInfo.getDOrgPrice() != null)) {
          if ((lObj_ccurrencytype != null) && (lObj_ccurrencytype.toString().trim().length() > 0)) {
            getBillCardPanel().setCellEditable(lint_rowindex, "ccurrencytype", false);
          }

          if ((lObj_noriginalcurprice != null) && (lObj_noriginalcurprice.toString().trim().length() > 0)) {
            getBillCardPanel().setCellEditable(lint_rowindex, "noriginalcurprice", false);
          }

          if ((lObj_norgtaxprice != null) && (lObj_norgtaxprice.toString().trim().length() > 0)) {
            getBillCardPanel().setCellEditable(lint_rowindex, "norgtaxprice", false);
          }

          if ((lObj_cupsourcebilltype != null) && ((lObj_cupsourcebilltype.toString().trim().equals("Z2")) || (lObj_cupsourcebilltype.toString().trim().equals("Z2"))))
            getBillCardPanel().setCellEditable(lint_rowindex, "ccontractrcode", false);
        }
      }
    }
  }

  private void setNotEditableWhenRelateCntAllRow()
  {
    int li_rowcount = getBillCardPanel().getRowCount();
    if (li_rowcount > 0) {
      int[] lintary_all = new int[li_rowcount];
      for (int i = 0; i < li_rowcount; i++) {
        lintary_all[i] = i;
      }
      setNotEditableWhenRelateCnt(lintary_all);
    }
  }

  private void setRelateCntAndDefaultPriceOneRow(int aint_row, boolean abol_SkipCT)
  {
    setRelateCntAndDefaultPrice(new int[] { aint_row }, abol_SkipCT);
  }

  private void setRelateCntAndDefaultPriceAllRow(boolean abol_SkipCT)
  {
    int li_rowcount = getBillCardPanel().getRowCount();
    if (li_rowcount > 0) {
      int[] lintary_all = new int[li_rowcount];
      for (int i = 0; i < li_rowcount; i++) {
        lintary_all[i] = i;
      }
      setRelateCntAndDefaultPrice(lintary_all, abol_SkipCT);
    }
  }

  public static String getPriceFieldByPricePolicy(int iPolicy)
  {
    if (iPolicy == 5)
    {
      return "norgtaxprice";
    }if (iPolicy == 6)
    {
      return "noriginalcurprice";
    }

    return null;
  }

  public static UFDouble getPriceValueByPricePolicy(RetCtToPoQueryVO voCntInfo, int iPolicy)
  {
    if (voCntInfo == null) {
      return null;
    }

    if (iPolicy == 5)
    {
      return voCntInfo.getDOrgTaxPrice();
    }if (iPolicy == 6)
    {
      return voCntInfo.getDOrgPrice();
    }

    return null;
  }

  private void setCurrencyState(int rowindex)
  {
    try
    {
      Object ccurrency = getBillCardPanel().getBodyValueAt(rowindex, "ccurrencytypeid");
      if ((ccurrency == null) || (ccurrency.toString().trim().equals(""))) {
        getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", false);
        getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", false);
        return;
      }

      boolean isBlnLocalFrac = getCurrArith().isBlnLocalFrac();

      String localCur = getCurrArith().getLocalCurrPK();

      String fracCur = getCurrArith().getFracCurrPK();

      boolean isLocal = localCur.equals(ccurrency);

      boolean isFrac = fracCur != null;

      if (isBlnLocalFrac) {
        if (isLocal) {
          getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", false);
          getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", false);
        }
        else
        {
          getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", true);

          if (isFrac)
          {
            getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", false);
          }
          else
          {
            getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", true);
          }
        }
      }
      else
      {
        getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", false);
        if (isLocal)
          getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", false);
        else {
          getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", true);
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
    }
  }

  private void setCurrencyState(int rowindex, boolean isEdit)
  {
    if (!isEdit) {
      setCurrencyState(rowindex);
      return;
    }

    try
    {
      Object ccurrency = getBillCardPanel().getBodyValueAt(rowindex, "ccurrencytypeid");
      if ((ccurrency == null) || (ccurrency.toString().trim().equals(""))) {
        getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", false);
        getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", false);
        return;
      }

      String strRateDate = null;
      UFDouble localRate = null;
      UFDouble fracRate = null;

      if (isEdit)
      {
        strRateDate = getBillCardPanel().getHeadItem("dorderdate").getValue().toString();

        localRate = getCurrArith().getRate(ccurrency.toString(), getCurrArith().getLocalCurrPK(), strRateDate);
        fracRate = getCurrArith().getRate(ccurrency.toString(), getCurrArith().getFracCurrPK(), strRateDate);
      }

      boolean isBlnLocalFrac = getCurrArith().isBlnLocalFrac();

      String localCur = getCurrArith().getLocalCurrPK();

      String fracCur = getCurrArith().getFracCurrPK();

      boolean isLocal = localCur.equals(ccurrency);

      boolean isFrac = fracCur != null;

      Integer fracPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(ccurrency.toString()).getCurrbusidigit();

      if (isBlnLocalFrac) {
        if (isLocal) {
          getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", false);
          getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", false);
          getBillCardPanel().setBodyValueAt("1", rowindex, "nexchangeotobrate");
          getBillCardPanel().setBodyValueAt(null, rowindex, "nexchangeotoarate");

          getBillCardPanel().setBodyValueAt(fracPrecision, rowindex, "localprecision");
          getBillCardPanel().setBodyValueAt("0", rowindex, "fracprecision");
        }
        else
        {
          if (isEdit)
          {
            Integer localPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(getCurrArith().getFracCurrPK()).getCurrbusidigit();
            getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(localPrecision.intValue());
            getBillCardPanel().setBodyValueAt(localPrecision, rowindex, "localprecision");
          }

          getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", true);
          getBillCardPanel().setBodyValueAt(localRate, rowindex, "nexchangeotobrate");

          if (isFrac)
          {
            getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", false);
            getBillCardPanel().setBodyValueAt("1", rowindex, "nexchangeotoarate");

            getBillCardPanel().setBodyValueAt("0", rowindex, "fracprecision");
          }
          else
          {
            if (isEdit)
            {
              getBillCardPanel().getBodyItem("nexchangeotoarate").setDecimalDigits(fracPrecision.intValue());
              getBillCardPanel().setBodyValueAt(fracPrecision, rowindex, "fracprecision");
            }

            getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", true);
            getBillCardPanel().setBodyValueAt(fracRate, rowindex, "nexchangeotoarate");
          }
        }
      }
      else
      {
        getBillCardPanel().setCellEditable(rowindex, "nexchangeotoarate", false);
        getBillCardPanel().setBodyValueAt(null, rowindex, "nexchangeotoarate");
        getBillCardPanel().setBodyValueAt("0", rowindex, "fracprecision");
        if (isLocal) {
          getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", false);
          getBillCardPanel().setBodyValueAt("1", rowindex, "nexchangeotobrate");

          getBillCardPanel().setBodyValueAt(fracPrecision, rowindex, "localprecision");
        }
        else
        {
          if (isEdit)
          {
            Integer localPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(ccurrency.toString()).getCurrbusidigit();
            getBillCardPanel().getBodyItem("nexchangeotobrate").setDecimalDigits(localPrecision.intValue());
            getBillCardPanel().setBodyValueAt(localPrecision, rowindex, "localprecision");
          }

          getBillCardPanel().setCellEditable(rowindex, "nexchangeotobrate", true);
          getBillCardPanel().setBodyValueAt(localRate, rowindex, "nexchangeotobrate");
        }

      }

      if (isEdit)
      {
        Integer currPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(ccurrency.toString()).getCurrbusidigit();
        getBillCardPanel().setBodyValueAt(currPrecision, rowindex, "mnyprecision");
        setChangePrecision(rowindex);
      }

      if (isEdit) {
        getBillCardPanel().setBodyValueAt(null, rowindex, "nmoney");
        getBillCardPanel().setBodyValueAt(null, rowindex, "ntaxmny");
        getBillCardPanel().setBodyValueAt(null, rowindex, "nsummny");
        getBillCardPanel().setBodyValueAt(null, rowindex, "nassistcurmny");
        getBillCardPanel().setBodyValueAt(null, rowindex, "nassisttaxmny");
        getBillCardPanel().setBodyValueAt(null, rowindex, "nassistsummny");
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      if (isEdit)
        showErrorMessage("" + e.getMessage());
    }
  }

  private void setNormalPrecision2()
  {
    try
    {
      int numPrecision = 2;
      int assnumPrecision = 2;
      int ratePrecision = 2;
      int pricePrecision = 2;
      int moneyPrecision = 4;

      Integer localPrecision = new Integer(this.mint_localmnyPrecision);
      int localmnyPrecision = localPrecision.intValue();

      numPrecision = this.mintary_precisions[0];
      assnumPrecision = this.mintary_precisions[1];
      ratePrecision = this.mintary_precisions[2];
      pricePrecision = this.mintary_precisions[3];

      getBillCardPanel().getBodyItem("nordernum").setDecimalDigits(numPrecision);
      getBillCardPanel().getBodyItem("nbackarrvnum").setDecimalDigits(numPrecision);
      getBillCardPanel().getBodyItem("nbackstorenum").setDecimalDigits(numPrecision);

      getBillCardPanel().getBodyItem("nassistnum").setDecimalDigits(assnumPrecision);

      getBillCardPanel().getBodyItem("measrate").setDecimalDigits(ratePrecision);

      getBillCardPanel().getBodyItem("noriginalcurprice").setDecimalDigits(pricePrecision);
      getBillCardPanel().getBodyItem("noriginalnetprice").setDecimalDigits(pricePrecision);
      getBillCardPanel().getBodyItem("norgtaxprice").setDecimalDigits(pricePrecision);
      getBillCardPanel().getBodyItem("norgnettaxprice").setDecimalDigits(pricePrecision);

      getBillCardPanel().getBodyItem("noriginalcurmny").setDecimalDigits(moneyPrecision);
      getBillCardPanel().getBodyItem("noriginaltaxmny").setDecimalDigits(moneyPrecision);
      getBillCardPanel().getBodyItem("noriginalsummny").setDecimalDigits(moneyPrecision);

      getBillCardPanel().getBodyItem("nmoney").setDecimalDigits(localmnyPrecision);
      getBillCardPanel().getBodyItem("ntaxmny").setDecimalDigits(localmnyPrecision);
      getBillCardPanel().getBodyItem("nsummny").setDecimalDigits(localmnyPrecision);

      if (getCurrArith().getFracCurrPK() != null)
      {
        Integer fracPrecision = CurrtypeQuery.getInstance().getCurrtypeVO(getCurrArith().getFracCurrPK()).getCurrdigit();
        if (fracPrecision != null) {
          int fracmnyPrecision = fracPrecision.intValue();

          getBillCardPanel().getBodyItem("nassistcurmny").setDecimalDigits(fracmnyPrecision);
          getBillCardPanel().getBodyItem("nassisttaxmny").setDecimalDigits(fracmnyPrecision);
          getBillCardPanel().getBodyItem("nassistsummny").setDecimalDigits(fracmnyPrecision);
        }
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
    }
  }

  public void setOperateState(int ibillstate)
  {
    if ((this.m_listVOs == null) || (this.m_listVOs.size() == 0)) {
      this.boEdit.setEnabled(false);
      this.boDel.setEnabled(false);
      setGroupButtonsState(new UFBoolean("N"));
      return;
    }

    if ((ibillstate == 0) || (ibillstate == 4)) {
      this.boEdit.setEnabled(true);
      this.boDel.setEnabled(true);
      this.boAudit.setEnabled(true);
      this.boUnaudit.setEnabled(false);
    } else if (ibillstate == 2) {
      this.boEdit.setEnabled(false);
      this.boDel.setEnabled(false);
      this.boAudit.setEnabled(true);
      this.boUnaudit.setEnabled(false);
    } else {
      this.boEdit.setEnabled(false);
      this.boDel.setEnabled(false);
      this.boAudit.setEnabled(false);
      this.boUnaudit.setEnabled(true);
    }
    this.boReturn.setEnabled(true);

    setGroupButtonsState(new UFBoolean("Y"));
    updateButtons();
  }

  private void setPgUpDownButtonsState(int curPageIndex)
  {
    if (curPageIndex < 0) {
      this.boFirst.setEnabled(false);
      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boLast.setEnabled(false);
      updateButtons();
      return;
    }
    if (this.m_listVOs.size() - 1 == 0) {
      this.boFirst.setEnabled(false);
      this.boPre.setEnabled(false);
      this.boNext.setEnabled(false);
      this.boLast.setEnabled(false);
      updateButtons();
      return;
    }
    if (curPageIndex == 0) {
      if ((this.m_listVOs == null) || (this.m_listVOs.size() == 0)) {
        this.boFirst.setEnabled(false);
        this.boPre.setEnabled(false);
        this.boNext.setEnabled(false);
        this.boLast.setEnabled(false);
      } else {
        this.boFirst.setEnabled(false);
        this.boPre.setEnabled(false);
        this.boNext.setEnabled(true);
        this.boLast.setEnabled(true);
      }
      updateButtons();
      return;
    }
    if (curPageIndex == this.m_listVOs.size() - 1) {
      this.boFirst.setEnabled(true);
      this.boPre.setEnabled(true);
      this.boNext.setEnabled(false);
      this.boLast.setEnabled(false);
      updateButtons();
      return;
    }

    this.boFirst.setEnabled(true);
    this.boPre.setEnabled(true);
    this.boNext.setEnabled(true);
    this.boLast.setEnabled(true);

    updateButtons();
  }

  private void setReceiveAddress(int rowIndex, String key)
  {
    Object sReceiver = getBillCardPanel().getHeadItem("creciever").getValue();

    if (key.equals("creciever"))
    {
      if ((sReceiver != null) && (!sReceiver.toString().trim().equals("")))
      {
        String cvendorbaseid = null;
        String defaddr = null;
        try {
          cvendorbaseid = PublicHelper.getCvendorbaseid(sReceiver.toString());
          defaddr = PublicHelper.getVdefaddr(cvendorbaseid);
        }
        catch (Exception e) {
        }
        UIRefPane adressRef = new UIRefPane();
        adressRef.setRefNodeName("客商发货地址");
        adressRef.setReturnCode(true);
        adressRef.setWhereString(" where pk_cubasdoc='" + cvendorbaseid + "' ");

        getBillCardPanel().getBillTable().getColumn(NCLangRes.getInstance().getStrByID("common", "UC000-0000642")).setCellEditor(new BillCellEditor(adressRef));

        for (int i = 0; i < getBillCardPanel().getRowCount(); i++)
          getBillCardPanel().setBodyValueAt(defaddr, i, "creceiveaddress");
      }
      else
      {
        getBillCardPanel().getBillTable().getColumn(NCLangRes.getInstance().getStrByID("common", "UC000-0000642")).setCellEditor(new BillCellEditor(new UITextField()));

        String formula = "creceiveaddress->getColValue(bd_stordoc,storaddr,pk_stordoc,cwarehouseid)";
        for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
          Object address = getBillCardPanel().getBodyValueAt(rowIndex, "creceiveaddress");
          if ((address == null) || (address.toString().trim().equals(""))) {
            getBillCardPanel().getBillModel().execFormula(i, new String[] { formula });
          }
        }
      }
      getBillCardPanel().getBillTable().editingStopped(null);
      getBillCardPanel().updateUI();
      return;
    }

    if ((key.equals("cwarehousename")) && ((sReceiver == null) || (sReceiver.toString().trim().equals("")))) {
      Object address = getBillCardPanel().getBodyValueAt(rowIndex, "creceiveaddress");
      if ((address == null) || (address.toString().trim().equals(""))) {
        String formula = "creceiveaddress->getColValue(bd_stordoc,storaddr,pk_stordoc,cwarehouseid)";
        getBillCardPanel().getBillModel().execFormula(rowIndex, new String[] { formula });
      }
    }
  }

  private void setRelated_Taxrate(int iBeginRow, int iEndRow)
  {
    HashMap mapId = new HashMap();
    Vector vecRow = new Vector();
    for (int i = iBeginRow; i <= iEndRow; i++) {
      String sBaseId = PuPubVO.getString_TrimZeroLenAsNull((String)getBillCardPanel().getBodyValueAt(i, "cbaseid"));
      if (sBaseId != null) {
        mapId.put(sBaseId, "");
        vecRow.add(new Integer(i));
      }
    }
    int iSize = vecRow.size();
    if (iSize == 0) {
      return;
    }

    InvTool.loadBatchTaxrate((String[])(String[])mapId.keySet().toArray(new String[iSize]));

    int iRow = 0;
    for (int i = 0; i < iSize; i++) {
      iRow = ((Integer)vecRow.get(i)).intValue();

      String sBaseId = (String)getBillCardPanel().getBodyValueAt(iRow, "cbaseid");

      UFDouble dCurTaxRate = InvTool.getInvTaxRate(sBaseId);
      if (dCurTaxRate == null) dCurTaxRate = new UFDouble(0);
      getBillCardPanel().setBodyValueAt(dCurTaxRate, iRow, "ntaxrate");
    }
  }

  private void showSelBizType(ButtonObject bo)
  {
    ButtonObject[] boaAll = this.boBusitype.getChildButtonGroup();
    if (boaAll != null)
      for (int i = 0; i < boaAll.length; i++) {
        if (!bo.equals(boaAll[i]))
          continue;
        boaAll[i].setSelected(true);
        break;
      }
  }

  public boolean onClosing()
  {
    if ((this.m_billState == ScConstants.STATE_ADD) || (this.m_billState == ScConstants.STATE_MODIFY) || ((this.m_billState == ScConstants.STATE_OTHER) && (getBillCardPanel().isVisible()))) {
      int nReturn = MessageDialog.showYesNoCancelDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("common", "UCH001"));

      if (nReturn == 4) {
        return onSave();
      }

      return nReturn == 8;
    }

    return true;
  }

  public ButtonObject[] getExtendBtns()
  {
    return null;
  }

  public void onExtendBtnsClick(ButtonObject bo)
  {
  }

  public void setExtendBtnsStat(int iState)
  {
  }

  public void doMaintainAction(ILinkMaintainData maintaindata)
  {
    SCMEnv.out("进入委外订单接口...");

    SCMEnv.out("");

    if (maintaindata == null) return;

    String billID = maintaindata.getBillID();

    init();
    try
    {
      this.m_iCurBillIndex = 0;
      loadData(billID);
    } catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000041"));

      this.boAudit.setEnabled(false);
      this.btnBillCombin.setEnabled(false);
      this.boQueryForAudit.setEnabled(false);
      this.boDocument.setEnabled(false);
      updateButtons();

      return;
    }

    getBillCardPanel().setEnabled(false);

    setButtons(this.m_btnTree.getButtonArray());

    setButtonsState();

    int ibillstate = ((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getIbillstatus().intValue();
    if (((OrderHeaderVO)this.m_curOrderVO.getParentVO()).getPrimaryKey().equals(billID))
      setOperateState(ibillstate);
    updateButtons();
  }

  public void doAddAction(ILinkAddData adddata)
  {
    SCMEnv.out("");

    if (adddata.getUserObject() == null) return;

    SCMessageVO message = (SCMessageVO)adddata.getUserObject();

    processAfterChange("28", new AggregatedValueObject[] { message.getAskvo() });
  }

  public void doApproveAction(ILinkApproveData approvedata)
  {
    SCMEnv.out("");

    if (approvedata == null) return;

    String billID = approvedata.getBillID();

    init();
    try
    {
      this.m_iCurBillIndex = 0;
      loadData(billID);
    } catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("401201", "UPP401201-000041"));

      this.boAudit.setEnabled(false);
      this.btnBillCombin.setEnabled(false);
      this.boQueryForAudit.setEnabled(false);
      this.boDocument.setEnabled(false);
      updateButtons();

      return;
    }

    getBillCardPanel().setEnabled(false);

    setButtons(this.m_btnTree.getButtonArray());

    this.boAudit.setEnabled(true);
    this.btnBillCombin.setEnabled(true);
    this.boQueryForAudit.setEnabled(true);
    this.boDocument.setEnabled(true);
    this.m_btnOthersFuncs.setEnabled(true);
    updateButtons();
  }

  public void doQueryAction(ILinkQueryData querydata)
  {
    init();

    String billID = querydata.getBillID();

    OrderVO vo = null;
    try
    {
      this.m_iCurBillIndex = 0;

      vo = OrderHelper.findByPrimaryKey(billID);
      if (vo == null) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000397"));

        return;
      }
      String strPkCorp = vo.getPk_corp();

      SCMQueryConditionDlg queryDlg = new SCMQueryConditionDlg(this);
      if ((queryDlg.getAllTempletDatas() == null) || (queryDlg.getAllTempletDatas().length <= 0)) {
        queryDlg.setTempletID(querydata.getPkOrg(), getModuleCode(), getOperatorID(), null);
      }

      ArrayList alcorp = new ArrayList();
      alcorp.add(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
      queryDlg.initCorpRef("sc_order_corp", ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), alcorp);
      queryDlg.setCorpRefs("sc_order_corp", IDataPowerForSC.REFKEYSFORSC);

      ConditionVO[] voaCond = queryDlg.getDataPowerConVOs(strPkCorp, IDataPowerForSC.REFKEYSFORSC);

      String strDataPowerSql = new ConditionVO().getWhereSQL(voaCond);
      String whereSQL = "sc_order.corderid = '" + billID + "' ";
      if ((strDataPowerSql != null) && (strDataPowerSql.trim().length() > 0)) {
        if ((whereSQL != null) && (whereSQL.trim().length() > 0))
          whereSQL = whereSQL + " and " + strDataPowerSql + " ";
        else {
          whereSQL = strDataPowerSql + " ";
        }
      }

      OrderHeaderVO[] orderHeaderVO = OrderHelper.queryAllHead("", whereSQL, new ClientLink(ClientEnvironment.getInstance()));
      if ((orderHeaderVO == null) || (orderHeaderVO.length <= 0) || (orderHeaderVO[0] == null)) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("common", "SCMCOMMON000000161"));

        return;
      }
      billID = orderHeaderVO[0].getCorderid();

      loadData(billID);
    } catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("401201", "UPP401201-000034"));
    }

    getBillCardPanel().setEnabled(false);
  }

  public List getRelaSortObject()
  {
    return this.m_listVOs;
  }

  public void chgDataForOrderCorp(String sBiztypeId, OrderVO[] vos, String sUpBillType)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0) || (vos[0] == null)) {
      return;
    }
    if ((sBiztypeId == null) || (sBiztypeId.trim().length() == 0)) {
      SCMEnv.out("订单档案转换的前提是业务类型非空!");
      return;
    }

    boolean bFromCt = (sUpBillType != null) && ((sUpBillType.equals("Z1")) || (sUpBillType.equals("Z2")));

    if ((!CentrPurchaseUtil.isGroupBusiType(sBiztypeId)) && (!bFromCt)) {
      SCMEnv.out("订单档案转换的前提是业务类型为集团业务类型!");
      return;
    }

    ArrayList listVendorId = new ArrayList();
    ArrayList listProjectId = new ArrayList();
    ArrayList listInventoryId = new ArrayList();
    ChgDocPkVO chgDocVo = null;
    String strCntCorpId = null;
    String strPurCorpId = null;
    int iLen = vos.length;
    int jLen = 0;
    OrderItemVO[] items = null;
    OrderHeaderVO head = null;
    for (int i = 0; i < iLen; i++) {
      if ((vos[i].getChildrenVO() == null) || (vos[i].getChildrenVO().length == 0) || (vos[i].getChildrenVO()[0] == null))
      {
        continue;
      }

      head = (OrderHeaderVO)vos[i].getParentVO();

      strPurCorpId = getCorpPrimaryKey();

      items = (OrderItemVO[])(OrderItemVO[])vos[i].getChildrenVO();

      chgDocVo = new ChgDocPkVO();
      chgDocVo.setSrcCorpId(items[0].getPk_corp());
      chgDocVo.setDstCorpId(strPurCorpId);
      chgDocVo.setSrcBasId(head.getCvendorid());
      listVendorId.add(chgDocVo);

      jLen = items.length;
      for (int j = 0; j < jLen; j++) {
        if (items[j] == null) {
          continue;
        }
        strCntCorpId = items[j].getPk_corp();

        if (PuPubVO.getString_TrimZeroLenAsNull(items[j].getCprojectid()) != null) {
          chgDocVo = new ChgDocPkVO();
          chgDocVo.setSrcCorpId(strCntCorpId);
          chgDocVo.setDstCorpId(strPurCorpId);
          chgDocVo.setSrcManId(items[j].getCprojectid());
          listProjectId.add(chgDocVo);
        }

        if (PuPubVO.getString_TrimZeroLenAsNull(items[j].getCbaseid()) != null) {
          chgDocVo = new ChgDocPkVO();
          chgDocVo.setSrcCorpId(strCntCorpId);
          chgDocVo.setDstCorpId(strPurCorpId);
          chgDocVo.setSrcBasId(items[j].getCbaseid());
          listInventoryId.add(chgDocVo);
        }
      }
    }

    int iSize = listVendorId.size();
    if (iSize == 0) {
      return;
    }

    ChgDocPkVO[] vendorVos = null;
    vendorVos = (ChgDocPkVO[])(ChgDocPkVO[])listVendorId.toArray(new ChgDocPkVO[iLen]);
    vendorVos = ChgDataUtil.chgPkCuByCorpBase(vendorVos);

    ChgDocPkVO[] projectVos = null;
    projectVos = (ChgDocPkVO[])(ChgDocPkVO[])listProjectId.toArray(new ChgDocPkVO[listProjectId.size()]);
    projectVos = ChgDataUtil.chgPkjobByCorp(projectVos);

    ChgDocPkVO[] inventoryVos = null;
    inventoryVos = (ChgDocPkVO[])(ChgDocPkVO[])listInventoryId.toArray(new ChgDocPkVO[listInventoryId.size()]);
    inventoryVos = ChgDataUtil.chgPkInvByCorpBase(inventoryVos);

    int iPosProj = 0;
    int iPosBody = 0;
    for (int i = 0; i < iLen; i++) {
      if ((vos[i].getChildrenVO() == null) || (vos[i].getChildrenVO().length == 0) || (vos[i].getChildrenVO()[0] == null))
      {
        continue;
      }

      head.setCvendormangid(vendorVos[i].getDstManId());

      items = (OrderItemVO[])(OrderItemVO[])vos[i].getChildrenVO();
      jLen = items.length;
      for (int j = 0; j < jLen; j++) {
        if (items[j] == null)
        {
          continue;
        }
        if (PuPubVO.getString_TrimZeroLenAsNull(items[j].getCprojectid()) != null) {
          items[j].setCprojectid(projectVos[iPosProj].getDstManId());
          iPosProj++;
        }

        if (PuPubVO.getString_TrimZeroLenAsNull(items[j].getCbaseid()) != null) {
          items[j].setCmangid(inventoryVos[iPosBody].getDstManId());
          iPosBody++;
        }
      }
    }
  }

  private void afterEditStoreOrgToWarehouse(BillCardPanel pnlBill, BillEditEvent e, String pk_corp, String sCalBodyId, String sKeyBodyWareRef, String[] saKeyBodyWarehouse)
  {
    if ((pnlBill == null) || (e == null) || (pk_corp == null) || (pk_corp.trim().length() == 0) || (sKeyBodyWareRef == null) || (sKeyBodyWareRef.trim().length() == 0))
    {
      SCMEnv.out("方法nc.ui.sc.order.OrderUI.afterEditStoreOrgToWarehouse(BillListPanel, BillEditEvent, String, String, String, String[])传入参数错误！");
      return;
    }

    if ((sCalBodyId != null) && (sCalBodyId.trim().length() != 0))
    {
      int iRowCount = pnlBill.getRowCount();
      for (int i = 0; i < iRowCount; i++)
      {
        pnlBill.setBodyValueAt(null, i, sKeyBodyWareRef);

        if (saKeyBodyWarehouse != null) {
          int iWareColLen = saKeyBodyWarehouse.length;
          for (int j = 0; j < iWareColLen; j++) {
            if ((saKeyBodyWarehouse[j] == null) || (saKeyBodyWarehouse[j].trim().length() <= 0))
              continue;
            pnlBill.setBodyValueAt(null, i, saKeyBodyWarehouse[j]);

            if (this.m_billState == ScConstants.STATE_MODIFY) {
              pnlBill.getBillModel().setRowState(i, 2);
            }
          }
        }

      }

    }

    BillPanelTool.restrictWarehouseRefByStoreOrg(pnlBill, pk_corp, sCalBodyId, sKeyBodyWareRef);
  }

  private int getM_iCurBillIndex()
  {
    return this.m_iCurBillIndex;
  }

  public void setM_iCurBillIndex(int curBillIndex)
  {
    this.m_iCurBillIndex = curBillIndex;
  }

  private void setDefaultValueByUser()
  {
    UIRefPane cemployeeid = (UIRefPane)getBillCardPanel().getHeadItem("cemployeeid").getComponent();

    if ((cemployeeid != null) && (cemployeeid.getRefPK() == null)) {
      IUserManageQuery qrySrv = (IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
      PsndocVO voPsnDoc = null;
      try {
        voPsnDoc = qrySrv.getPsndocByUserid(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), getOperatorID());
      }
      catch (BusinessException be) {
        SCMEnv.out(be);
      }
      if (voPsnDoc != null) {
        cemployeeid.setPK(voPsnDoc.getPk_psndoc());

        UIRefPane cdeptid = (UIRefPane)getBillCardPanel().getHeadItem("cdeptid").getComponent();
        cdeptid.setPK(voPsnDoc.getPk_deptdoc());
      }
    }
  }
}