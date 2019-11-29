package nc.ui.ic.pub.bill;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.ic001.BatchcodeHelper;
import nc.ui.ic.pub.BillFormulaContainer;
import nc.ui.ic.pub.ICCommonBusi;
import nc.ui.ic.pub.InvOnHandDialog;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ic.pub.print.PrintDataInterface;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillModelCellEditableController;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.pub.bill.BillSortListener2;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.print.PrintEntry;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.ui.scm.ic.exp.SetColor;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.ic.measurerate.InvMeasRate;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b15.MeasureRateVO;
import nc.vo.bd.def.DefVO;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.ic.ic700.ICDataSet;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.SpecialBillHeaderVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillVO;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICDateException;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICLocatorException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICRepeatException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pub.IBillCode;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.sm.UserVO;

public class SpecialBillBaseUI extends ToftPanel
  implements TableModelListener, BillEditListener, BillEditListener2, MouseListener, BillBodyMenuListener, IFreshTsListener, BillModelCellEditableController, ILinkApprove, ILinkQuery, ILinkMaintain, BillSortListener2
{
  protected ButtonObject m_boAdd;
  protected ButtonObject m_boChange;
  protected ButtonObject m_boDelete;
  protected ButtonObject m_boCopyBill;
  protected ButtonObject m_boJointAdd;
  protected ButtonObject m_boSave;
  protected ButtonObject m_boCancel;
  protected ButtonObject m_boAddRow;
  protected ButtonObject m_boDeleteRow;
  protected ButtonObject m_boInsertRow;
  protected ButtonObject m_boCopyRow;
  protected ButtonObject m_boPasteRow;
  protected ButtonObject m_boAuditBill;
  protected ButtonObject m_boCancelAudit;
  protected ButtonObject m_boQuery;
  protected ButtonObject m_boLocate;
  protected ButtonObject m_boPrint;
  protected ButtonObject m_boList;
  protected ButtonObject m_boOut;
  protected ButtonObject m_boIn;
  protected ButtonObject m_boRowQuyQty;
  protected ButtonObject m_billMng;
  protected ButtonObject m_billRowMng;
  protected ButtonObject[] m_aryButtonGroup;
  protected BillCardPanel ivjBillCardPanel = null;
  protected BillListPanel ivjBillListPanel = null;
  protected String m_Title;
  protected int m_iMode = 3;

  protected boolean m_bCopyRow = false;

  protected int m_iFirstSelListHeadRow = -1;

  protected int m_iLastSelListHeadRow = -1;
  protected int m_iLastSelCardBodyRow = -1;
  protected int m_iTotalListHeadNum;
  protected int m_iTotalListBodyNum;
  protected Color m_cNormalColor = null;

  protected boolean m_bExchangeColor = false;

  protected boolean m_bLocateErrorColor = false;

  protected int m_iFirstAddRows = 2;
  protected String m_sBillTypeCode;
  protected String m_sBillCode;
  protected final String m_sNumItemKey = "dshldtransnum";
  protected final String m_sAstItemKey = "nshldtransastnum";
  protected final String m_sHeaderTableName = "ic_special_h";

  protected final String m_sLotWarehouseSource = "coutwarehouseid";

  protected FreeItemRefPane m_firpFreeItemRefPane = new FreeItemRefPane();
  protected LotNumbRefPane m_lnrpLotNumbRefPane = new LotNumbRefPane();

  protected int m_iFirstSelectRow = -1;
  protected int m_iFirstSelectCol = -1;
  protected ClientUIInAndOut m_dlgInOut;
  protected SpecialBillVO m_voBill;
  protected boolean m_isWhInvRef = false;
  protected SpecialBillItemVO[] m_voBillItem;
  protected String m_sCorpID;
  protected String m_sUserID;
  protected String m_sLogDate;
  protected String m_sUserName;
  protected InvOnHandDialog m_iohdDlg;
  protected InvMeasRate m_voInvMeas = new InvMeasRate();
  protected QueryConditionDlgForBill ivjQueryConditionDlg;
  protected boolean m_bIsEditBillCode = false;
  private ClientUISortCtl m_listHeadSortCtl;
  private ClientUISortCtl m_listBodySortCtl;
  private ClientUISortCtl m_cardBodySortCtl;
  private BillFormulaContainer m_formulaParse = null;
  ArrayList m_alFormulBodyItem;
  ArrayList m_alFormulHeadItem;
  protected ArrayList m_alListData = new ArrayList();
  BillFormulaContainer m_billFormulaContain;
  protected ButtonObject m_boJointCheck;
  protected ButtonObject m_boPreview;
  protected PrintDataInterface m_dataSource;
  private DefVO[] m_defBody = null;
  private DefVO[] m_defHead = null;
  protected nc.ui.ic.pub.orient.OrientDialog m_dlgOrient;
  private Hashtable m_htWh = new Hashtable();

  protected int[] m_iaScale = { 2, 2, 2, 2, 2 };
  private InvoInfoBYFormula m_InvoInfoBYFormula;
  boolean m_isLocated = false;

  public boolean m_isQuryPlanprice = false;
  protected PrintEntry m_print;
  protected ButtonObject m_PrintMng;
  public static final String m_sBillRowNo = "crowno";
  public String m_sMainWhItemKey = "coutwarehouseid";

  protected String m_sOldBillCode = "";
  protected String m_sPNodeCode;
  protected final UFDouble ZERO = new UFDouble("0.0");

  public void afterEdit(BillEditEvent e)
  {
    String strColName = e.getKey().trim();
    String sID_name = GeneralMethod.getFromIDtoName(getBillCardPanel(), getBillListPanel(), strColName);

    int rownum = e.getRow();
    if ((sID_name != null) && (this.m_voBill != null)) {
      if ((e.getPos() == 0) && (null != getBillCardPanel().getBillData().getHeadItem(strColName))) {
        if ((getBillCardPanel().getHeadItem(strColName).getComponent() instanceof UIRefPane))
        {
          if (!sID_name.trim().equals(strColName)) {
            this.m_voBill.setHeaderValue(sID_name, ((UIRefPane)getBillCardPanel().getHeadItem(strColName).getComponent()).getRefName());

            this.m_voBill.setHeaderValue(strColName, ((UIRefPane)getBillCardPanel().getHeadItem(strColName).getComponent()).getRefPK());
          }
          else if (!GeneralMethod.getIDColName(getBillCardPanel(), strColName).equals(strColName))
          {
            sID_name = GeneralMethod.getIDColName(getBillCardPanel(), strColName);
            this.m_voBill.setHeaderValue(strColName, ((UIRefPane)getBillCardPanel().getHeadItem(strColName).getComponent()).getRefName());

            this.m_voBill.setHeaderValue(sID_name, ((UIRefPane)getBillCardPanel().getHeadItem(strColName).getComponent()).getRefPK());
          }

        }
        else
        {
          this.m_voBill.setHeaderValue(sID_name, e.getValue());
        }
      } else if ((null == getBillCardPanel().getBillData().getTailItem(strColName)) && 
        (e.getPos() == 1) && (null != getBillCardPanel().getBillData().getBodyItem(strColName))) {
        if ((getBillCardPanel().getBodyItem(strColName).getComponent() instanceof UIRefPane))
        {
          if (!sID_name.trim().equals(strColName)) {
            this.m_voBill.setItemValue(rownum, sID_name, ((UIRefPane)getBillCardPanel().getBodyItem(strColName).getComponent()).getRefName());

            this.m_voBill.setItemValue(rownum, strColName, ((UIRefPane)getBillCardPanel().getBodyItem(strColName).getComponent()).getRefPK());

            getBillCardPanel().setBodyValueAt(((UIRefPane)getBillCardPanel().getBodyItem(strColName).getComponent()).getRefName(), rownum, strColName);
          }
          else if (!GeneralMethod.getIDColName(getBillCardPanel(), strColName).equals(strColName))
          {
            sID_name = GeneralMethod.getIDColName(getBillCardPanel(), strColName);
            this.m_voBill.setItemValue(rownum, strColName, ((UIRefPane)getBillCardPanel().getBodyItem(strColName).getComponent()).getRefName());

            this.m_voBill.setItemValue(rownum, sID_name, ((UIRefPane)getBillCardPanel().getBodyItem(strColName).getComponent()).getRefPK());

            getBillCardPanel().setBodyValueAt(((UIRefPane)getBillCardPanel().getBodyItem(strColName).getComponent()).getRefName(), rownum, strColName);
          }

        }
        else
        {
          this.m_voBill.setItemValue(rownum, sID_name, e.getValue());
        }

      }

    }

    if (e.getKey().equals("coutwarehouseid"))
      afterWhOutEdit(e);
    else if (e.getKey().equals("cinwarehouseid"))
      afterWhInEdit(e);
    else if (e.getKey().equals("coutbsor")) {
      afterBsorEdit(new String[] { "coutbsor", "coutbsorname" }, new String[] { "coutdeptid", "coutdeptname" });
    }
    else if (e.getKey().equals("cinbsrid")) {
      afterBsorEdit(new String[] { "cinbsrid", "cinbsrname" }, new String[] { "cindeptid", "cindeptname" });
    }
    else if ((3 != this.m_iMode) && (e.getKey().startsWith("vuserdef"))) {
      afterDefEdit(e);
    }

    if (rownum == -1) {
      return;
    }
    if (e.getKey().equals("cinventorycode")) {
      afterInvMutiEdit(e);
    } else if (e.getKey().equals("vfree0")) {
      afterFreeItemEdit(e);
    } else if (e.getKey().equals("vbatchcode")) {
      afterLotEdit(e);
    } else if (e.getKey().equals("castunitname")) {
      afterAstUOMEdit(rownum);
    } else if (e.getKey().equals("je")) {
      mustNoNegative(strColName, rownum, getBillCardPanel(), this.m_voBill);
    } else if (e.getKey().equals("nprice")) {
      mustNoNegative(strColName, rownum, getBillCardPanel(), this.m_voBill);
    } else if (e.getKey().equals("dshldtransnum")) {
      mustNoNegative(strColName, rownum, getBillCardPanel(), this.m_voBill);
    } else if (e.getKey().equals("nshldtransastnum")) {
      mustNoNegative(strColName, rownum, getBillCardPanel(), this.m_voBill);
    } else if (e.getKey().equals("dvalidate")) {
      afterValidateEdit(e);
    } else if (e.getKey().equals("scrq")) {
      afterProducedateEdit(e);
    } else if (e.getKey().equals("hsl")) {
      mustNoNegative(strColName, rownum, getBillCardPanel(), this.m_voBill);
      afterHslEdit(rownum);
    } else if (e.getKey().equals("cvendorid")) {
      afterVendorEdit(rownum);
    } else if (e.getKey().equals("cvendorname")) {
      afterVendorEdit(rownum);
    }
    else if (e.getKey().equals("crowno")) {
      BillRowNo.afterEditWhenRowNo(getBillCardPanel(), e, this.m_sBillTypeCode);

      this.m_voBill.setItemValue(rownum, "crowno", getBillCardPanel().getBodyValueAt(rownum, "crowno"));
    }
  }

  public void bodyRowChange(BillEditEvent e)
  {
    getBillCardPanel().rememberFocusComponent();

    if (e.getSource() == getBillListPanel().getHeadTable())
    {
      clearOrientColor();

      listSelectionChanged(e);
    }

    if (e.getSource() == getBillListPanel().getBodyTable()) {
      this.m_iLastSelCardBodyRow = e.getRow();
    }
    if (e.getSource() == getBillCardPanel().getBillTable()) {
      int rownow = e.getRow();

      this.m_iLastSelCardBodyRow = rownow;

      setTailValue(rownow);
    }

    getBillCardPanel().restoreFocusComponent();
  }

  public String getTitle()
  {
    return this.m_Title;
  }

  protected void handleException(Throwable exception)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    SCMEnv.error(exception);
  }

  protected void initialize()
  {
    getCEnvInfo();
    initialize(this.m_sCorpID, this.m_sUserID, this.m_sUserName, null, null, this.m_sLogDate);
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      SpecialBillBaseUI aClientUI = new SpecialBillBaseUI();
      frame.setContentPane(aClientUI);
      frame.setSize(aClientUI.getSize());
      frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

      frame.show();
      Insets insets = frame.getInsets();
      frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);

      frame.setVisible(true);
    } catch (Throwable exception) {
      SCMEnv.out("nc.ui.pub.ToftPanel 的 main() 中发生异常");
      SCMEnv.error(exception);
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    showHintMessage(bo.getName());

    clearOrientColor();

    if (bo == this.m_boAdd)
      onAdd();
    else if (bo == this.m_boChange)
      onChange();
    else if (bo == this.m_boDelete)
      onDelete();
    else if (bo == this.m_boCopyBill)
      onCopyBill();
    else if (bo == this.m_boSave)
      onSave();
    else if (bo == this.m_boCancel) {
      onCancel();
    }
    else if (bo == this.m_boAddRow)
      onAddRow();
    else if (bo == this.m_boDeleteRow)
      onDeleteRow();
    else if (bo == this.m_boInsertRow)
      onInsertRow();
    else if (bo == this.m_boCopyRow)
      onCopyRow();
    else if (bo == this.m_boPasteRow) {
      onPasteRow();
    }
    else if (bo == this.m_boAuditBill)
      onAuditBill();
    else if (bo == this.m_boCancelAudit) {
      onCancelAudit();
    }
    else if (bo == this.m_boQuery)
      onQuery();
    else if (bo == this.m_boJointCheck)
      onJointCheck();
    else if (bo == this.m_boLocate)
      onLocate();
    else if (bo == this.m_boPrint)
      onPrint();
    else if (bo == this.m_boPreview)
      onPreview();
    else if (bo == this.m_boList) {
      onList();
    }
    else if (bo == this.m_boRowQuyQty)
      onRowQuyQty();
  }

  protected void setButtonState()
  {
    switch (this.m_iMode) {
    case 0:
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000287"));

      this.m_billMng.setEnabled(true);
      this.m_boAdd.setEnabled(false);
      this.m_boChange.setEnabled(false);
      this.m_boDelete.setEnabled(false);
      this.m_boJointAdd.setEnabled(false);
      this.m_boCopyBill.setEnabled(false);
      this.m_boSave.setEnabled(true);
      this.m_boCancel.setEnabled(true);

      this.m_billRowMng.setEnabled(true);
      this.m_boAddRow.setEnabled(true);
      this.m_boDeleteRow.setEnabled(true);
      this.m_boInsertRow.setEnabled(true);
      this.m_boCopyRow.setEnabled(true);
      this.m_boPasteRow.setEnabled(this.m_bCopyRow);

      this.m_boAuditBill.setEnabled(false);
      this.m_boCancelAudit.setEnabled(false);
      this.m_boOut.setEnabled(false);
      this.m_boIn.setEnabled(false);

      this.m_boQuery.setEnabled(false);
      this.m_boLocate.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.m_boPreview.setEnabled(false);
      this.m_boList.setEnabled(false);

      this.m_boRowQuyQty.setEnabled(true);
      break;
    case 2:
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UC001-0000045"));

      this.m_billMng.setEnabled(true);
      this.m_boAdd.setEnabled(false);
      this.m_boChange.setEnabled(false);
      this.m_boDelete.setEnabled(false);
      this.m_boJointAdd.setEnabled(false);
      this.m_boCopyBill.setEnabled(false);
      this.m_boSave.setEnabled(true);
      this.m_boCancel.setEnabled(true);

      this.m_billRowMng.setEnabled(true);
      this.m_boAddRow.setEnabled(true);
      this.m_boDeleteRow.setEnabled(true);
      this.m_boInsertRow.setEnabled(true);
      this.m_boCopyRow.setEnabled(true);
      this.m_boPasteRow.setEnabled(this.m_bCopyRow);

      this.m_boAuditBill.setEnabled(false);
      this.m_boCancelAudit.setEnabled(false);
      this.m_boOut.setEnabled(false);
      this.m_boIn.setEnabled(false);

      this.m_boQuery.setEnabled(false);
      this.m_boLocate.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      this.m_boPreview.setEnabled(false);
      this.m_boList.setEnabled(false);

      this.m_boRowQuyQty.setEnabled(true);
      break;
    case 3:
      showHintMessage(NCLangRes.getInstance().getStrByID("common", "UC001-0000021"));

      this.m_billMng.setEnabled(true);
      this.m_boAdd.setEnabled(true);
      this.m_boChange.setEnabled((this.m_iTotalListHeadNum > 0) && ((getBillCardPanel().getTailItem("cauditorid").getValue() == null) || (getBillCardPanel().getTailItem("cauditorid").getValue().length() == 0)));

      this.m_boDelete.setEnabled((this.m_iTotalListHeadNum > 0) && ((getBillCardPanel().getTailItem("cauditorid").getValue() == null) || (getBillCardPanel().getTailItem("cauditorid").getValue().length() == 0)));

      this.m_boJointAdd.setEnabled(true);

      this.m_boCopyBill.setEnabled(this.m_iTotalListHeadNum > 0);
      if ((this.m_iMode != 4) && (
        (getBillCardPanel().getHeadItem("vbillcode").getValue() == null) || (getBillCardPanel().getHeadItem("vbillcode").getValue().length() == 0)))
      {
        this.m_boCopyBill.setEnabled(false);
      }
      this.m_boSave.setEnabled(false);
      this.m_boCancel.setEnabled(false);

      this.m_billRowMng.setEnabled(false);
      this.m_boAddRow.setEnabled(false);
      this.m_boDeleteRow.setEnabled(false);
      this.m_boInsertRow.setEnabled(false);
      this.m_boCopyRow.setEnabled(false);
      this.m_boPasteRow.setEnabled(false);

      this.m_boCancelAudit.setEnabled((this.m_iTotalListHeadNum > 0) && (getBillCardPanel().getTailItem("cauditorid").getValue() != null) && (getBillCardPanel().getTailItem("cauditorid").getValue().length() != 0));

      if (this.m_boCancelAudit.isEnabled()) {
        this.m_boAuditBill.setEnabled(false);
      } else {
        this.m_boAuditBill.setEnabled(this.m_iTotalListHeadNum > 0);

        this.m_boCancelAudit.setEnabled(false);
      }

      this.m_boOut.setEnabled(this.m_iTotalListHeadNum > 0);
      this.m_boIn.setEnabled(this.m_iTotalListHeadNum > 0);

      this.m_boQuery.setEnabled(true);
      this.m_boLocate.setEnabled(false);
      this.m_boPrint.setEnabled(true);
      this.m_boPreview.setEnabled(true);
      this.m_boList.setEnabled(true);

      this.m_boRowQuyQty.setEnabled(true);
      break;
    case 4:
      this.m_billMng.setEnabled(true);
      this.m_boAdd.setEnabled(true);

      this.m_boChange.setEnabled((this.m_alListData != null) && (this.m_alListData.size() > 0) && (this.m_iLastSelListHeadRow >= 0) && (this.m_iLastSelListHeadRow < this.m_alListData.size()) && ((getBillListPanel().getHeadBillModel().getValueAt(this.m_iLastSelListHeadRow, "cauditorname") == null) || (getBillListPanel().getHeadBillModel().getValueAt(this.m_iLastSelListHeadRow, "cauditorname").toString().trim().length() == 0)));

      this.m_boDelete.setEnabled(this.m_boChange.isEnabled());

      this.m_boJointAdd.setEnabled(true);
      this.m_boCopyBill.setEnabled((this.m_iTotalListHeadNum > 0) && (getBillListPanel().getHeadTable().getSelectedRows().length == 1));

      this.m_boSave.setEnabled(false);
      this.m_boCancel.setEnabled(false);

      this.m_billRowMng.setEnabled(false);
      this.m_boAddRow.setEnabled(false);
      this.m_boDeleteRow.setEnabled(false);
      this.m_boInsertRow.setEnabled(false);
      this.m_boCopyRow.setEnabled(false);
      this.m_boPasteRow.setEnabled(false);

      this.m_boAuditBill.setEnabled(false);
      this.m_boCancelAudit.setEnabled(false);
      this.m_boOut.setEnabled(false);
      this.m_boIn.setEnabled(false);

      this.m_boQuery.setEnabled(true);
      this.m_boLocate.setEnabled(true);
      this.m_boPrint.setEnabled(true);
      this.m_boPreview.setEnabled(true);
      this.m_boList.setEnabled(true);

      this.m_boRowQuyQty.setEnabled(false);
    case 1:
    }

    if (this.m_aryButtonGroup != null)
      updateButtons();
  }

  protected BillCardPanel getBillCardPanel()
  {
    if (this.ivjBillCardPanel == null) {
      try {
        this.ivjBillCardPanel = new BillCardPanel();
        this.ivjBillCardPanel.setName("BillCardPanel");
        this.ivjBillCardPanel.setAlignmentY(0.0F);
        this.ivjBillCardPanel.setAlignmentX(0.0F);
        this.ivjBillCardPanel.setBounds(1, 1, 774, 418);

        BillData bd = new BillData(this.ivjBillCardPanel.getTempletData(this.m_sBillTypeCode, null, this.m_sUserID, this.m_sCorpID));

        if (bd == null) {
          SCMEnv.out("--> billdata null.");
          return this.ivjBillCardPanel;
        }

        if (bd.getBodyItem("vfree0") != null)
        {
          getFreeItemRefPane().setMaxLength(bd.getBodyItem("vfree0").getLength());

          bd.getBodyItem("vfree0").setComponent(getFreeItemRefPane());
        }

        if (bd.getBodyItem("vbatchcode") != null)
        {
          getLotNumbRefPane().setMaxLength(bd.getBodyItem("vbatchcode").getLength());

          bd.getBodyItem("vbatchcode").setComponent(getLotNumbRefPane());
        }

        bd = changeBillDataByUserDef(getDefHeadVO(), getDefBodyVO(), bd);
        this.ivjBillCardPanel.setBillData(bd);

        bd = BatchCodeDefSetTool.changeBillDataByBCUserDef(this.m_sCorpID, bd);
        this.ivjBillCardPanel.setBillData(bd);

        bd = setScale(bd);

        this.ivjBillCardPanel.setBillData(bd);

        if (bd.getHeadItem("cbilltypecode") != null)
          this.m_Title = bd.getHeadItem("cbilltypecode").getName();
        if ((this.ivjBillCardPanel.getTitle() != null) && (this.ivjBillCardPanel.getTitle().trim().length() > 0))
        {
          this.m_Title = this.ivjBillCardPanel.getTitle();
        }
        if (bd.getHeadItem("vbillcode") != null)
          bd.getHeadItem("vbillcode").setEnabled(false);
        this.ivjBillCardPanel.getBillTable().setSelectionMode(2);

        BillRowNo.loadRowNoItem(this.ivjBillCardPanel, "crowno");

        this.ivjBillCardPanel.getBodyPanel().setRowNOShow(Setup.bShowBillRowNo);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillCardPanel;
  }

  protected BillData changeBillDataByUserDef(DefVO[] defHead, DefVO[] defBody, BillData oldBillData)
  {
    if (defHead != null) {
      oldBillData.updateItemByDef(defHead, "vuserdef", true);
      for (int i = 1; i < 20; i++) {
        BillItem item = oldBillData.getHeadItem("vuserdef" + i);
        if ((item != null) && (item.getDataType() == 7)) {
          ((UIRefPane)item.getComponent()).setAutoCheck(true);
        }

      }

    }

    if (defBody == null) {
      return oldBillData;
    }
    oldBillData.updateItemByDef(defBody, "vuserdef", false);
    for (int i = 1; i < 20; i++) {
      BillItem item = oldBillData.getBodyItem("vuserdef" + i);
      if ((item != null) && (item.getDataType() == 7)) {
        ((UIRefPane)item.getComponent()).setAutoCheck(true);
      }

    }

    return oldBillData;
  }

  protected BillListPanel getBillListPanel()
  {
    if (this.ivjBillListPanel == null) {
      try {
        this.ivjBillListPanel = new BillListPanel();
        this.ivjBillListPanel.setName("BillListPanel");
        this.ivjBillListPanel.setMultiSelect(false);
        this.ivjBillListPanel.setVisible(false);
        this.ivjBillListPanel.setMaximumSize(new Dimension(2147483647, 2147483647));
        this.ivjBillListPanel.setBounds(0, 0, 774, 419);
        this.ivjBillListPanel.setMinimumSize(new Dimension(26, 60));

        this.ivjBillListPanel.loadTemplet(this.m_sBillTypeCode, null, this.m_sUserID, this.m_sCorpID);

        BillListData bd = this.ivjBillListPanel.getBillListData();
        bd = changeBillListDataByUserDef(bd);

        bd = BatchCodeDefSetTool.changeBillListDataByBCUserDef(this.m_sCorpID, bd);

        this.ivjBillListPanel.setListData(bd);

        this.ivjBillListPanel.getHeadTable().setSelectionMode(2);

        this.ivjBillListPanel.getChildListPanel().setTatolRowShow(true);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillListPanel;
  }

  public void onAdd()
  {
    if (this.m_iMode != 3)
    {
      onList();
    }

    getBillCardPanel().getBillModel().clearBodyData();

    getBillCardPanel().addNew();

    getBillCardPanel().updateValue();

    this.m_voBill = new SpecialBillVO();
    for (int i = 1; i <= this.m_iFirstAddRows; i++) {
      onAddRow();
    }

    this.m_iMode = 0;
    setButtonState();
    setBillState();

    setNewBillInitData();

    dispBodyRow(getBillCardPanel().getBillTable());

    this.m_sOldBillCode = "";

    firstSetColEditable();

    BillRowNo.addNewRowNo(getBillCardPanel(), this.m_sBillTypeCode, "crowno");

    if (getBillCardPanel().getHeadItem("vbillcode") != null) {
      getBillCardPanel().getHeadItem("vbillcode").setEnabled(this.m_bIsEditBillCode);
    }
    getBillCardPanel().setTailItem("iprintcount", new Integer(0));
    getBillCardPanel().transferFocusTo(0);

    InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    ficr.setFreeItemRenderer(getBillCardPanel(), this.m_voBill);
  }

  public void onAddRow()
  {
    getBillCardPanel().addLine();

    BillRowNo.addNewRowNo(getBillCardPanel(), this.m_sBillTypeCode, "crowno");
  }

  public void onCancel()
  {
    switch (MessageDialog.showYesNoDlg(this, null, NCLangRes.getInstance().getStrByID("common", "UCH067"), 8))
    {
    case 4:
      break;
    default:
      return;
    }

    getBillCardPanel().resumeValue();

    switchListToBill();
    SetColor.SetTableColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), getBillCardPanel(), new ArrayList(), this.m_cNormalColor, this.m_cNormalColor, this.m_bExchangeColor, this.m_bLocateErrorColor, "");

    this.m_iMode = 3;

    setButtonState();
    setBillState();

    InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    ficr.setFreeItemRenderer(getBillCardPanel(), null);
  }

  public void onChange()
  {
    if (this.m_iMode != 3)
    {
      onList();
    }

    this.m_iMode = 2;
    setButtonState();
    setBillState();

    firstSetColEditable();

    this.m_sOldBillCode = getBillCardPanel().getHeadItem("vbillcode").getValue();

    if (getBillCardPanel().getHeadItem("vbillcode") != null) {
      getBillCardPanel().getHeadItem("vbillcode").setEnabled(false);
    }
    if (this.m_sOldBillCode != null) {
      this.m_sOldBillCode = this.m_sOldBillCode.trim();
    }
    getBillCardPanel().transferFocusTo(0);

    InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    ficr.setFreeItemRenderer(getBillCardPanel(), this.m_voBill);

    setUpdateBillInitData();
  }

  protected void setUpdateBillInitData()
  {
    try
    {
      if (getBillCardPanel().getTailItem("clastmodiid") != null)
        getBillCardPanel().setTailItem("clastmodiid", this.m_sUserID);
      if (getBillCardPanel().getTailItem("clastmodiname") != null)
        getBillCardPanel().setTailItem("clastmodiname", this.m_sUserName);
      UFDateTime ufdPre = new UFDateTime(System.currentTimeMillis());
      if (getBillCardPanel().getTailItem("tlastmoditime") != null) {
        getBillCardPanel().setTailItem("tlastmoditime", ufdPre.toString());
      }
      if (this.m_voBill != null) {
        this.m_voBill.setHeaderValue("clastmodiid", this.m_sUserID);
        this.m_voBill.setHeaderValue("clastmodiname", this.m_sUserName);
        this.m_voBill.setHeaderValue("tlastmoditime", ufdPre.toString());
      }
    }
    catch (Exception e) {
      SCMEnv.error(e);
    }
  }

  public void onCopyBill()
  {
    if ((this.m_alListData == null) || (this.m_alListData.size() < this.m_iLastSelListHeadRow) || (this.m_iLastSelListHeadRow < 0))
    {
      return;
    }
    if (this.m_iMode != 3)
    {
      onList();
    }

    this.m_voBill = ((SpecialBillVO)((SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).clone());

    getBillCardPanel().addNew();
    getBillCardPanel().updateValue();

    this.m_iMode = 3;

    this.m_voBill.setHeaderValue("cspecialhid", null);
    this.m_voBill.setHeaderValue("vbillcode", null);
    this.m_voBill.setHeaderValue("ts", null);
    this.m_voBill.setHeaderValue("iprintcount", new Integer(0));

    SpecialBillItemVO[] voaMyItem = this.m_voBill.getItemVOs();
    for (int row = 0; row < this.m_voBill.getChildrenVO().length; row++) {
      voaMyItem[row].setPrimaryKey(null);
      voaMyItem[row].setNchecknum(null);
      voaMyItem[row].setNcheckastnum(null);
      voaMyItem[row].setNcheckgrsnum(null);
      voaMyItem[row].setNadjustnum(null);
      voaMyItem[row].setNadjustastnum(null);
      voaMyItem[row].setNadjustgrsnum(null);

      voaMyItem[row].setCsourcebillbid(null);
      voaMyItem[row].setCsourcebillhid(null);
      voaMyItem[row].setCsourcetype(null);
      voaMyItem[row].setVsourcebillcode(null);
      voaMyItem[row].setCfirstbillbid(null);
      voaMyItem[row].setCfirstbillhid(null);
      voaMyItem[row].setCfirsttype(null);

      voaMyItem[row].setTs(null);
    }

    setBillValueVO(this.m_voBill);

    setNewBillInitData();

    this.m_iMode = 0;
    setButtonState();
    setBillState();
    dispBodyRow(getBillCardPanel().getBillTable());

    this.m_sOldBillCode = "";

    if (getBillCardPanel().getHeadItem("vbillcode") != null) {
      getBillCardPanel().getHeadItem("vbillcode").setEnabled(this.m_bIsEditBillCode);
    }
    getBillCardPanel().getTailItem("iprintcount").setValue(new Integer(0));

    firstSetColEditable();

    InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    ficr.setFreeItemRenderer(getBillCardPanel(), this.m_voBill);
  }

  public void onCopyRow()
  {
    int[] iaRow = getBillCardPanel().getBillTable().getSelectedRows();
    if ((iaRow != null) && (iaRow.length > 0)) {
      this.m_bCopyRow = true;
      getBillCardPanel().copyLine();
      setButtonState();
      voBillCopyLine(iaRow);
    }
    else {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000025"));
    }
  }

  public void onDelete()
  {
    if ((this.m_alListData == null) || (this.m_alListData.size() == 0))
    {
      return;
    }

    try
    {
      switch (MessageDialog.showYesNoDlg(this, null, NCLangRes.getInstance().getStrByID("common", "UCH002"), 8))
      {
      case 4:
        if (this.m_iMode == 3) {
          SpecialBillVO vo = this.m_voBill;
          SpecialBillHeaderVO voHead = (SpecialBillHeaderVO)vo.getHeaderVO().clone();

          voHead.setAttributeValue("coperatorid", this.m_sUserID);

          vo.setStatus(0);

          PfUtilClient.processAction("DELETE", this.m_sBillTypeCode, this.m_sLogDate, vo);

          minusBillVO();
          showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000010"));
          this.m_iFirstSelListHeadRow = -1;
          switchListToBill();
        }
        else {
          int[] iSelectedRows = getBillListPanel().getHeadTable().getSelectedRows();
          if ((iSelectedRows != null) && (iSelectedRows.length != 1))
          {
            showErrorMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000144"));
            return;
          }

          SpecialBillVO vo = (SpecialBillVO)this.m_alListData.get(iSelectedRows[0]);
          SpecialBillHeaderVO voHead = (SpecialBillHeaderVO)vo.getHeaderVO().clone();

          voHead.setAttributeValue("coperatorid", this.m_sUserID);

          PfUtilClient.processAction("DELETE", this.m_sBillTypeCode, this.m_sLogDate, vo);

          minusBillVO();

          showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000010"));
          this.m_iFirstSelListHeadRow = -1;
          switchBillToList();
        }
        setButtonState();
        setBillState();
        break;
      default:
        return;
      } } catch (Exception e) {
      SCMEnv.out("数据通讯失败！");
      handleException(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), e.getMessage());
    } finally {
      if (this.m_iMode == 3)
        switchListToBill();
      else {
        switchBillToList();
      }
      setButtonState();
      setBillState();
    }
  }

  public void onDeleteRow()
  {
    getBillCardPanel().delLine();
    if (getBillCardPanel().getRowCount() < 1)
      onAddRow();
    dispBodyRow(getBillCardPanel().getBillTable());
  }

  public void onJointAdd()
  {
    setButtonState();
    setBillState();
  }

  public void onList()
  {
    if (this.m_iMode == 3) {
      this.m_iMode = 4;
      switchBillToList();
    } else {
      this.m_iMode = 3;
      switchListToBill();
    }
    this.m_iFirstSelListHeadRow = this.m_iLastSelListHeadRow;
    showBtnSwitch();
    setButtonState();
    setBillState();
  }

  protected void onLocate()
  {
    if ((this.m_alListData == null) || (this.m_alListData.size() < 1)) {
      return;
    }

    nc.ui.scm.pub.report.OrientDialog dlgOrient = null;
    if (this.m_iMode == 5) {
      dlgOrient = new nc.ui.scm.pub.report.OrientDialog(this, getBillCardPanel().getBillModel(), getBillCardPanel().getBodyItems(), getBillCardPanel().getBillTable());

      dlgOrient.showModal();
      if (dlgOrient.getResult() == 1)
        this.m_isLocated = true;
    }
    else
    {
      dlgOrient = new nc.ui.scm.pub.report.OrientDialog(this, getBillListPanel().getHeadBillModel(), getBillListPanel().getBillListData().getHeadItems(), getBillListPanel().getHeadTable());

      dlgOrient.showModal();
      if (dlgOrient.getResult() == 1)
        this.m_isLocated = true;
    }
  }

  public void onPasteRow()
  {
    try
    {
      int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
      int row = getBillCardPanel().getBillTable().getSelectedRow();
      if (row < 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000145"));
      }
      else {
        if ((this.m_voBillItem == null) || (this.m_voBillItem.length <= 0))
          return;
        getBillCardPanel().pasteLine();

        int istartrow = getBillCardPanel().getBillTable().getSelectedRow() - this.m_voBillItem.length;
        for (int i = 0; i < this.m_voBillItem.length; i++) {
          getBillCardPanel().getBillModel().setBodyRowVO((SpecialBillItemVO)this.m_voBillItem[i].clone(), istartrow + i);
        }
        iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount() - iRowCount;

        BillRowNo.pasteLineRowNo(getBillCardPanel(), this.m_sBillTypeCode, "crowno", iRowCount);

        voBillPastLine(row);
      }
    }
    finally {
      getBillCardPanel().getBillModel().setNeedCalculate(true);
    }
  }

  public void onPrint()
  {
    if (this.m_iMode == 3) {
      filterNullLine();

      SpecialBillVO vo = (SpecialBillVO)getBillCardPanel().getBillValueVO(SpecialBillVO.class.getName(), SpecialBillHeaderVO.class.getName(), SpecialBillItemVO.class.getName());

      if ((null == vo) || (null == vo.getParentVO()) || (null == vo.getChildrenVO())) {
        return;
      }

      if (getPrintEntry().selectTemplate() < 0) {
        return;
      }
      String sBillID = vo.getPrimaryKey();
      ScmPrintlogVO voSpl = new ScmPrintlogVO();
      voSpl.setCbillid(sBillID);
      voSpl.setVbillcode(vo.getVBillCode());
      voSpl.setCbilltypecode(vo.getBillTypeCode());
      voSpl.setCoperatorid((String)(String)vo.getParentVO().getAttributeValue("coperatorid"));
      voSpl.setIoperatetype(new Integer(0));
      voSpl.setPk_corp(this.m_sCorpID);
      voSpl.setTs((String)(String)vo.getParentVO().getAttributeValue("ts"));

      SCMEnv.out("ts=========tata" + voSpl.getTs());
      PrintLogClient plc = new PrintLogClient();
      plc.setPrintEntry(getPrintEntry());

      plc.setPrintInfo(voSpl);

      plc.addFreshTsListener(this);

      getPrintEntry().setPrintListener(plc);

      getDataSource().setVO(vo);
      getPrintEntry().setDataSource(getDataSource());
      getPrintEntry().print();
    }
    else {
      if ((null == this.m_alListData) || (this.m_alListData.size() == 0)) {
        return;
      }

      if (getPrintEntry().selectTemplate() < 0)
        return;
      ArrayList alBill = getSelectedBills();
      if (alBill == null) {
        return;
      }
      SpecialBillVO vo = null;
      PrintLogClient plc = new PrintLogClient();
      plc.setBatchPrint(true);
      PrintDataInterface ds = null;

      getPrintEntry().setPrintListener(plc);
      plc.setPrintEntry(getPrintEntry());

      plc.addFreshTsListener(this);
      try
      {
        getPrintEntry().beginBatchPrint();
        for (int i = 0; i < alBill.size(); i++) {
          vo = (SpecialBillVO)alBill.get(i);

          ScmPrintlogVO voSpl = new ScmPrintlogVO();
          voSpl.setCbillid(vo.getPrimaryKey());
          voSpl.setVbillcode(vo.getVBillCode());
          voSpl.setCbilltypecode(vo.getBillTypeCode());
          voSpl.setCoperatorid((String)(String)vo.getParentVO().getAttributeValue("coperatorid"));
          voSpl.setIoperatetype(new Integer(0));
          voSpl.setPk_corp(this.m_sCorpID);
          voSpl.setTs((String)(String)vo.getParentVO().getAttributeValue("ts"));

          SCMEnv.out("ts=========tata" + voSpl.getTs());

          plc.setPrintInfo(voSpl);

          if (plc.check()) {
            ds = getNewDataSource();
            ds.setVO(vo);
            getPrintEntry().setDataSource(ds);
          }

        }

        getPrintEntry().endBatchPrint();
      }
      catch (Exception e) {
        SCMEnv.error(e);
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000051") + e.getMessage());
      }
    }
    showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000133"));
  }

  public void onQuery()
  {
    getConditionDlg().hideNormal();

    String sWhereClause = "";
    if (getConditionDlg().showModal() == 1)
    {
      ConditionVO[] voCons = getConditionDlg().getConditionVO();

      voCons = packConditionVO(voCons);

      resetConditionVO(voCons);

      sWhereClause = getExtenWhere(voCons);

      QryConditionVO qcvo = new QryConditionVO();
      if (sWhereClause.length() != 0)
        qcvo.setQryCond("head.cbilltypecode='" + this.m_sBillTypeCode + "' " + sWhereClause);
      else {
        qcvo.setQryCond("head.cbilltypecode='" + this.m_sBillTypeCode + "'");
      }
      qcvo.setParam(1, getConditionDlg().getConditionVO());

      qcvo.setIntParam(0, 4);

      loadBillListPanel(qcvo);

      setButtonState();
      setBillState();

      if (this.m_iTotalListHeadNum > 0)
        showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000146") + this.m_iTotalListHeadNum + NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000147"));
      else {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000013"));
      }
    }
    showBtnSwitch();
  }

  public boolean onSave()
  {
    try
    {
      if (this.m_iLastSelListHeadRow < 0) {
        this.m_iLastSelListHeadRow = 0;

        this.m_iTotalListHeadNum = 0;
      }

      getBillCardPanel().getBillData().execBodyValidateFormulas();

      getBillCardPanel().tableStopCellEditing();
      getBillCardPanel().stopEditing();

      filterNullLine();
      if (getBillCardPanel().getRowCount() == 0) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000148"));
        return false;
      }

      if (!BillRowNo.verifyRowNosCorrect(getBillCardPanel(), "crowno")) {
        return false;
      }

      SpecialBillVO voNowBill = null;
      voNowBill = getBillVO();

      this.m_voBill.setIDItems(voNowBill);
      voNowBill.setHeaderValue("fbillflag", this.m_voBill.getHeaderValue("fbillflag"));
      voNowBill.setHeaderValue("icheckmode", this.m_voBill.getHeaderValue("icheckmode"));
      voNowBill.setHeaderValue("pk_calbody_in", this.m_voBill.getHeaderValue("pk_calbody_in"));
      voNowBill.setHeaderValue("pk_calbody_out", this.m_voBill.getHeaderValue("pk_calbody_out"));

      this.m_voBill.setHeaderValue("fassistantflag", voNowBill.getHeaderValue("fassistantflag"));

      if (!checkVO()) {
        return false;
      }
      String sHPK = null;
      int iRowCount = getBillCardPanel().getRowCount();
      SpecialBillItemVO[] voaItem = null;

      if (0 == this.m_iMode)
      {
        voNowBill.setChildrenVO((SpecialBillItemVO[])getChangedItemVOs());

        voNowBill.getParentVO().setAttributeValue("cbilltypecode", this.m_sBillTypeCode);

        voaItem = voNowBill.getItemVOs();

        SpecialBillVO[] voTempBill = new SpecialBillVO[1];
        voTempBill[0] = voNowBill;

        if ((iRowCount > 0) && (voNowBill.getChildrenVO() != null) && 
          (getBillCardPanel().getBodyItem("crowno") != null)) {
          for (int i = 0; i < iRowCount; i++)
          {
            voNowBill.setItemValue(i, "crowno", getBillCardPanel().getBodyValueAt(i, "crowno"));
          }

        }

        ArrayList alsPrimaryKey = null;

        if (this.m_sCorpID.equals(voNowBill.getVBillCode()))
          voNowBill.setVBillCode(null);
        voNowBill.getHeaderVO().setCoperatoridnow(this.m_sUserID);
        alsPrimaryKey = (ArrayList)PfUtilClient.processAction("SAVE", this.m_sBillTypeCode, this.m_sLogDate, voNowBill);

        voNowBill.setVBillCode(this.m_sCorpID);

        if ((alsPrimaryKey == null) || (alsPrimaryKey.size() < 2)) {
          SCMEnv.out("return data error.");
          return true;
        }
        if (alsPrimaryKey.get(0) != null)
          showErrorMessage((String)alsPrimaryKey.get(0));
        ArrayList alMyPK = (ArrayList)alsPrimaryKey.get(1);
        sHPK = alMyPK.get(0).toString();

        voNowBill.getParentVO().setPrimaryKey(sHPK);

        for (int i = 0; i < voaItem.length; i++) {
          voaItem[i].setPrimaryKey(alMyPK.get(i + 1).toString());
          voaItem[i].setAttributeValue("cspecialhid", sHPK);
        }

        BatchCodeDefSetTool.execFormulaForBatchCode(voaItem);
        voNowBill.setChildrenVO(voaItem);

        this.m_voBill.setIDItems(voNowBill);

        this.m_iLastSelListHeadRow = this.m_iTotalListHeadNum;
        addBillVO();
      } else if (2 == this.m_iMode)
      {
        voNowBill = null;
        voNowBill = getUpdatedBillVO();
        voNowBill.setHeaderValue("fbillflag", this.m_voBill.getHeaderValue("fbillflag"));
        voNowBill.setHeaderValue("icheckmode", this.m_voBill.getHeaderValue("icheckmode"));
        this.m_voBill.setHeaderValue("fassistantflag", voNowBill.getHeaderValue("fassistantflag"));

        if (null == voNowBill) {
          return false;
        }
        voNowBill.setChildrenVO((SpecialBillItemVO[])getChangedItemVOs());

        SpecialBillItemVO[] voaTempItem = (SpecialBillItemVO[])(SpecialBillItemVO[])getBillVO().getChildrenVO();

        voaItem = (SpecialBillItemVO[])(SpecialBillItemVO[])voNowBill.getChildrenVO();

        SpecialBillVO[] m_voTempBill = new SpecialBillVO[1];
        m_voTempBill[0] = voNowBill;

        if ((iRowCount > 0) && (voNowBill.getChildrenVO() != null) && 
          (getBillCardPanel().getBodyItem("crowno") != null)) {
          for (int i = 0; i < iRowCount; i++)
          {
            voNowBill.setItemValue(i, "crowno", getBillCardPanel().getBodyValueAt(i, "crowno"));
          }

        }

        voNowBill.getHeaderVO().setCoperatoridnow(this.m_sUserID);

        ArrayList alsPrimaryKey = (ArrayList)PfUtilClient.processAction("SAVE", this.m_sBillTypeCode, this.m_sLogDate, voNowBill, ((SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).clone());

        if ((alsPrimaryKey == null) || (alsPrimaryKey.size() < 2)) {
          SCMEnv.out("return data error.");
          return true;
        }
        if (alsPrimaryKey.get(0) != null)
          showErrorMessage((String)alsPrimaryKey.get(0));
        ArrayList alMyPK = (ArrayList)alsPrimaryKey.get(1);
        sHPK = voNowBill.getParentVO().getPrimaryKey();

        ArrayList alItemVO = new ArrayList();

        int iItemNumb = 1;
        for (int i = 0; i < voaTempItem.length; i++) {
          switch (voaTempItem[i].getStatus()) {
          case 0:
            alItemVO.add(voaTempItem[i]);
            break;
          case 2:
            if (iItemNumb < alMyPK.size()) {
              voaItem[i].setPrimaryKey(alMyPK.get(iItemNumb).toString());
              iItemNumb++;
              voaItem[i].setAttributeValue("cspecialhid", sHPK);
              alItemVO.add(voaItem[i]);
            } else {
              SCMEnv.out("保存时出现行对应不上现象，请程序员检查...");
            }
            break;
          case 1:
            alItemVO.add(voaItem[i]);
          }
        }

        voaItem = new SpecialBillItemVO[alItemVO.size()];
        for (int i = 0; i < alItemVO.size(); i++) {
          voaItem[i] = ((SpecialBillItemVO)alItemVO.get(i));
        }
        voNowBill.setChildrenVO(voaItem);

        this.m_voBill.setIDItems(voNowBill);

        this.m_alListData.set(this.m_iLastSelListHeadRow, this.m_voBill.clone());
      }

      InvAttrCellRenderer ficr = new InvAttrCellRenderer();
      ficr.setFreeItemRenderer(getBillCardPanel(), null);
      this.m_iMode = 3;

      switchListToBill();

      if (sHPK != null) {
        ArrayList alLastTs = qryLastTs(sHPK);
        freshTs(alLastTs);
      }

      this.m_iFirstSelListHeadRow = -1;
      setButtonState();
      setBillState();
      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000103"));
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000149"));
      handleException(e);
    }
    return true;
  }

  protected void initButtons()
  {
  }

  protected void listSelectionChanged(BillEditEvent e)
  {
    try
    {
      int rownow = e.getRow();
      if ((this.m_alListData == null) || (rownow < 0) || (this.m_iLastSelListHeadRow == rownow)) {
        return;
      }
      this.m_iLastSelListHeadRow = rownow;
      getBillListPanel().getHeadTable().setRowSelectionInterval(rownow, rownow);
      getBillListPanel().getBodyBillModel().clearBodyData();

      SpecialBillVO sbvotemp = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

      if (sbvotemp == null) {
        return;
      }

      SpecialBillItemVO[] voItems = (SpecialBillItemVO[])(SpecialBillItemVO[])sbvotemp.getChildrenVO();
      if ((voItems == null) || (voItems.length < 1)) {
        qryItems(new int[] { rownow }, new String[] { sbvotemp.getPrimaryKey() });
      }

      sbvotemp = (SpecialBillVO)this.m_alListData.get(rownow);
      voItems = (SpecialBillItemVO[])(SpecialBillItemVO[])sbvotemp.getChildrenVO();

      if ((voItems == null) || (voItems.length <= 0)) {
        if (this.m_sBillTypeCode.equals("4R"))
          showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000150"));
        else {
          showWarningMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000151"));
        }

      }

      getBillListPanel().getBodyBillModel().setBodyDataVO(voItems);
      dispBodyRow(getBillListPanel().getBodyTable());
      setButtonState();
      setBillState();
    }
    catch (Exception e1) {
      SCMEnv.out("数据通讯失败！");
      handleException(e1);
    }
  }

  public void onInsertRow()
  {
    getBillCardPanel().insertLine();

    dispBodyRow(getBillCardPanel().getBillTable());

    BillRowNo.insertLineRowNo(getBillCardPanel(), this.m_sBillTypeCode, "crowno");
  }

  protected void initVariable()
  {
  }

  public void onAuditBill()
  {
  }

  public void onCancelAudit()
  {
    if ((this.m_alListData == null) || (this.m_alListData.size() < this.m_iLastSelListHeadRow) || (this.m_iLastSelListHeadRow < 0))
    {
      return;
    }
    switch (MessageDialog.showYesNoDlg(this, null, NCLangRes.getInstance().getStrByID("common", "UCH068"), 8))
    {
    case 4:
      break;
    default:
      return;
    }

    try
    {
      SpecialBillVO vo = new SpecialBillVO();
      SpecialBillHeaderVO voHead = new SpecialBillHeaderVO();
      voHead.setAttributeValue("cspecialhid", this.m_voBill.getHeaderValue("cspecialhid"));
      voHead.setAttributeValue("coperatorid", this.m_sUserID);
      voHead.setAttributeValue("pk_corp", this.m_sCorpID);
      voHead.setAttributeValue("coperatoridnow", this.m_sUserID);
      voHead.setAttributeValue("cbilltypecode", this.m_sBillTypeCode);
      vo.setParentVO(voHead);

      PfUtilClient.processAction("DELETEOTHER", this.m_sBillTypeCode, this.m_sLogDate, vo);

      clearAuditBillFlag();
      filterNullLine();

      String sHPK = vo.getPrimaryKey().trim();
      if (sHPK != null) {
        ArrayList alLastTs = qryLastTs(sHPK);
        freshTs(alLastTs);
      }
      this.m_alListData.set(this.m_iLastSelListHeadRow, this.m_voBill.clone());

      this.m_iFirstSelListHeadRow = -1;
      switchListToBill();

      setButtonState();
      setBillState();
    }
    catch (Exception e) {
      SCMEnv.out("数据通讯失败！");
      handleException(e);
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("4008bill", "UPPSCMCommon-000059"), e.getMessage());
    }
  }

  protected void filterNullLine()
  {
    SpecialBillItemVO[] voaItem = getBillVO().getItemVOs();

    if (null == voaItem) {
      return;
    }
    if (voaItem.length < 1) {
      return;
    }

    Vector vTemp = new Vector();

    for (int i = 0; i < voaItem.length; i++)
    {
      if ((voaItem[i] != null) && (voaItem[i].getCinventoryid() != null) && (voaItem[i].getCinventoryid().trim().length() != 0)) {
        continue;
      }
      vTemp.addElement(new Integer(i));
    }
    int size = vTemp.size();
    if (size > 0) {
      int[] iaRows = new int[size];
      for (int i = 0; i < size; i++)
        iaRows[i] = new Integer(vTemp.elementAt(i).toString()).intValue();
      getBillCardPanel().getBillModel().delLine(iaRows);
    }
  }

  protected boolean checkVO()
  {
    try
    {
      String sAllErrorMessage = "";

      VOCheck.checkNullVO(this.m_voBill);

      VOCheck.checkNumInput(this.m_voBill.getChildrenVO(), "dshldtransnum");
      try
      {
        VOCheck.validate(this.m_voBill, GeneralMethod.getHeaderCanotNullString(getBillCardPanel()), GeneralMethod.getBodyCanotNullString(getBillCardPanel()));
      }
      catch (ICNullFieldException e)
      {
        String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

        sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
      }
      catch (ICHeaderNullFieldException e) {
        String sErrorMessage = GeneralMethod.getHeaderErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

        sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
      }

      try
      {
        VOCheck.checkGreaterThanZeroInput(this.m_voBill.getChildrenVO(), "nprice", NCLangRes.getInstance().getStrByID("common", "UC000-0000741"));
      }
      catch (ICPriceException e) {
        String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

        sAllErrorMessage = sAllErrorMessage + sErrorMessage + "\n";
      }

      if (sAllErrorMessage.trim().length() != 0) {
        showErrorMessage(sAllErrorMessage);
        return false;
      }

      SetColor.SetTableColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), getBillCardPanel(), new ArrayList(), this.m_cNormalColor, this.m_cNormalColor, this.m_bExchangeColor, this.m_bLocateErrorColor, "");

      return true;
    }
    catch (ICDateException e)
    {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

      showErrorMessage(sErrorMessage);

      SetColor.SetTableColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), getBillCardPanel(), e.getErrorRowNums(), this.m_cNormalColor, e.getExceptionColor(), this.m_bExchangeColor, this.m_bLocateErrorColor, e.getHint());

      return false;
    }
    catch (ICNullFieldException e) {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

      showErrorMessage(sErrorMessage);

      SetColor.SetTableColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), getBillCardPanel(), e.getErrorRowNums(), this.m_cNormalColor, e.getExceptionColor(), this.m_bExchangeColor, this.m_bLocateErrorColor, e.getHint());

      return false;
    }
    catch (ICNumException e) {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

      showErrorMessage(sErrorMessage);
      SetColor.SetTableColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), getBillCardPanel(), e.getErrorRowNums(), this.m_cNormalColor, e.getExceptionColor(), this.m_bExchangeColor, this.m_bLocateErrorColor, e.getHint());

      return false;
    }
    catch (ICPriceException e) {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

      showErrorMessage(sErrorMessage);
      SetColor.SetTableColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), getBillCardPanel(), e.getErrorRowNums(), this.m_cNormalColor, e.getExceptionColor(), this.m_bExchangeColor, this.m_bLocateErrorColor, e.getHint());

      return false;
    }
    catch (ICSNException e) {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

      showErrorMessage(sErrorMessage);
      SetColor.SetTableColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), getBillCardPanel(), e.getErrorRowNums(), this.m_cNormalColor, e.getExceptionColor(), this.m_bExchangeColor, this.m_bLocateErrorColor, e.getHint());

      return false;
    }
    catch (ICLocatorException e) {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

      showErrorMessage(sErrorMessage);
      SetColor.SetTableColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), getBillCardPanel(), e.getErrorRowNums(), this.m_cNormalColor, e.getExceptionColor(), this.m_bExchangeColor, this.m_bLocateErrorColor, e.getHint());

      return false;
    }
    catch (ICRepeatException e) {
      String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

      showErrorMessage(sErrorMessage);
      SetColor.SetTableColor(getBillCardPanel().getBillModel(), getBillCardPanel().getBillTable(), getBillCardPanel(), e.getErrorRowNums(), this.m_cNormalColor, e.getExceptionColor(), this.m_bExchangeColor, this.m_bLocateErrorColor, e.getHint());

      return false;
    }
    catch (ICHeaderNullFieldException e) {
      String sErrorMessage = GeneralMethod.getHeaderErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

      showErrorMessage(sErrorMessage);
      return false;
    } catch (NullFieldException e) {
      showErrorMessage(e.getHint());

      return false;
    } catch (ValidationException e) {
      SCMEnv.out("校验异常！其他未知故障...");
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000152"));
      handleException(e);
    }return false;
  }

  protected FreeItemRefPane getFreeItemRefPane()
  {
    return this.m_firpFreeItemRefPane;
  }

  protected LotNumbRefPane getLotNumbRefPane()
  {
    return this.m_lnrpLotNumbRefPane;
  }

  public void mouseClicked(MouseEvent e)
  {
    if ((e.getSource() == getBillListPanel().getHeadTable()) && 
      (e.getClickCount() == 2) && (getBillListPanel().getHeadTable().getSelectedRow() != -1))
    {
      onList();
    }
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mousePressed(MouseEvent e)
  {
    Object edd = e.getSource();
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void columnAdded(TableColumnModelEvent e)
  {
  }

  public void columnMarginChanged(ChangeEvent e)
  {
  }

  public void columnMoved(TableColumnModelEvent e)
  {
  }

  public void columnRemoved(TableColumnModelEvent e)
  {
  }

  public void afterFreeItemEdit(BillEditEvent e)
  {
    try
    {
      FreeVO fvoFreeVO = getFreeItemRefPane().getFreeVO();
      this.m_voBill.setItemFreeVO(e.getRow(), fvoFreeVO);
      int row = e.getRow();
      String[] sIKs = getClearIDs(1, "vfree0");
      clearRowData(row, sIKs);

      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000153"));
    } catch (Exception e2) {
      SCMEnv.error(e2);
    }
  }

  public void afterLotEdit(BillEditEvent e)
  {
    String s = e.getKey();
    WhVO whvo = this.m_voBill.getWhOut();

    getLotRefbyHand(s);
    pickupLotRef(s);
  }

  public void afterWhInEdit(BillEditEvent e)
  {
    try
    {
      WhVO voWh = getWhInfoByRef("cinwarehouseid");
      if (this.m_voBill != null) {
        this.m_voBill.setWhIn(voWh);
      }

    }
    catch (Exception e2)
    {
      SCMEnv.error(e2);
    }
  }

  public void afterWhOutEdit(BillEditEvent e)
  {
    try
    {
      WhVO voWh = getWhInfoByRef("coutwarehouseid");

      if (this.m_voBill != null) {
        this.m_voBill.setWhOut(voWh);

        this.m_voBill.clearInvQtyInfo();

        String[] sIKs = getClearIDs(1, "coutwarehouseid");
        int iRowCount = getBillCardPanel().getRowCount();
        for (int row = 0; row < iRowCount; row++) {
          clearRowData(row, sIKs);
        }

        String sWhID = null;
        String sCalID = null;
        if (getBillCardPanel().getHeadItem(this.m_sMainWhItemKey) != null) {
          sWhID = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey).getValue();
        }

        if ((sCalID == null) && (sWhID != null)) {
          sCalID = (String)((Object[])(Object[])CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody", sWhID))[0];

          ((SpecialBillHeaderVO)this.m_voBill.getParentVO()).setPk_calbody_in(sCalID);
          ((SpecialBillHeaderVO)this.m_voBill.getParentVO()).setPk_calbody_out(sCalID);
        }

        int rowCount = getBillCardPanel().getBillModel().getRowCount();
        String sID = null;
        ArrayList alIDs = new ArrayList();
        for (int i = 0; i < rowCount; i++) {
          sID = (String)getBillCardPanel().getBillModel().getValueAt(i, "cinventoryid");
          if (sID == null)
            continue;
          alIDs.add(sID);
        }

        if ((this.m_sCorpID != null) && (sCalID != null) && (sCalID.length() > 0) && (alIDs != null) && (alIDs.size() > 0))
        {
          long ITime = System.currentTimeMillis();
          ArrayList alParam = new ArrayList();
          alParam.add(sCalID);
          alParam.add(this.m_sCorpID);
          alParam.add(alIDs);
          Object objValue = GeneralBillHelper.queryInfo(new Integer(26), alParam);

          if (objValue != null) {
            ArrayList alPrice = (ArrayList)objValue;
            UFDouble ufPrice = null;
            for (int i = 0; i < rowCount; i++) {
              if (alPrice.get(i) != null) {
                ufPrice = (UFDouble)alPrice.get(i);
                this.m_voBill.getItemInv(i).setNplannedprice(ufPrice);
              }
            }
          }

        }

      }

      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000154"));
    }
    catch (Exception e2) {
      SCMEnv.error(e2);
    }
  }

  protected void clearRowData(int row)
  {
    BillModel bmBill = getBillCardPanel().getBillModel();
    if (bmBill != null) {
      int iRowCount = bmBill.getRowCount();
      BillItem[] items = getBillCardPanel().getBodyItems();
      if ((items == null) || (row >= iRowCount)) {
        SCMEnv.out("row too big.");
        return;
      }

      String sColKey = null;
      int iColCount = items.length;
      for (int col = 0; col < iColCount; col++) {
        if (items[col] != null) {
          sColKey = items[col].getKey();
          if (("cspecialhid".equals(sColKey)) || ("cspecialbid".equals(sColKey)) || ("crowno".equals(sColKey)) || ("ts".equals(sColKey)) || ("invsetparttype".equals(sColKey)) || ("cwarehousename".equals(sColKey)))
          {
            continue;
          }

          bmBill.setValueAt(null, row, col);
        }
      }

      if (this.m_voBill != null)
        this.m_voBill.clearItem(row);
    }
  }

  protected void clearRowData(int row, String[] saColKey)
  {
    BillModel bmBill = getBillCardPanel().getBillModel();
    int iRowCount = bmBill.getRowCount();
    if ((row >= iRowCount) || (saColKey == null) || (saColKey.length == 0)) {
      SCMEnv.out("row too big.");
      return;
    }

    for (int col = 0; col < saColKey.length; col++) {
      if ((saColKey[col] == null) || (getBillCardPanel().getBodyItem(saColKey[col]) == null))
        continue;
      try {
        bmBill.setValueAt(null, row, saColKey[col]);

        this.m_voBill.setItemValue(row, saColKey[col], null);
        if (saColKey[col].trim().equals("vfree0")) {
          for (int i = 0; i < 10; i++)
            this.m_voBill.setItemValue(row, "vfree" + Integer.toString(i + 1).trim(), null);
        }
      }
      catch (Exception e)
      {
        SCMEnv.out("nc.ui.ic.pub.bill.SpecialBillBaseUI.clearRowData(int, String [])：set value ERR.--->" + saColKey[col]);
      }
      finally
      {
      }
    }
  }

  protected void colEditableSet(String sItemKey, int iRow)
  {
    BillItem bi = getBillCardPanel().getBillData().getBodyItem(sItemKey);

    if ((!sItemKey.equals("cinventorycode")) && (!sItemKey.equals("cwarehousename")) && ((null == this.m_voBill.getItemValue(iRow, "cinventoryid")) || (this.m_voBill.getItemValue(iRow, "cinventoryid").toString().trim().length() == 0)))
    {
      bi.setEnabled(false);
      return;
    }
    bi.setEnabled(bi.isEdit());

    if (sItemKey.equals("vfree0")) {
      if ((null != this.m_voBill.getItemValue(iRow, "isFreeItemMgt")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "isFreeItemMgt").toString()).intValue() != 0))
      {
        bi.setEnabled(true);
      }
      else bi.setEnabled(false);
    }
    else if (sItemKey.equals("vbatchcode")) {
      if ((null != this.m_voBill.getItemValue(iRow, "isLotMgt")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "isLotMgt").toString()).intValue() != 0))
      {
        String ColName = getBillCardPanel().getBillData().getBodyItem("vbatchcode").getName();

        getBillCardPanel().getBodyPanel().getTable().getColumn(ColName).setCellEditor(new BillCellEditor(getLotNumbRefPane()));
      }
      else {
        bi.setEnabled(false);
      }
    } else if (sItemKey.equals("dvalidate")) {
      if ((null != this.m_voBill.getItemValue(iRow, "isValidateMgt")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "isValidateMgt").toString()).intValue() != 0))
      {
        bi.setEnabled(false);
      }
      else bi.setEnabled(false);
    }
    else if (sItemKey.equals("scrq")) {
      if ((null != this.m_voBill.getItemValue(iRow, "isValidateMgt")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "isValidateMgt").toString()).intValue() != 0))
      {
        bi.setEnabled(false);
      }
      else bi.setEnabled(false);
    }
    else if (sItemKey.equals("castunitname")) {
      if ((null != this.m_voBill.getItemValue(iRow, "isAstUOMmgt")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "isAstUOMmgt").toString()).intValue() != 0))
      {
        bi.setEnabled(true);
      }
      else bi.setEnabled(false);
    }
    else if (sItemKey.equals("hsl")) {
      if ((this.m_voBill != null) && (this.m_voBill.getItemValue(iRow, "isAstUOMmgt") != null) && (((Integer)this.m_voBill.getItemValue(iRow, "isAstUOMmgt")).intValue() == 1))
      {
        if ((null != this.m_voBill.getItemValue(iRow, "isSolidConvRate")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "isSolidConvRate").toString()).intValue() == 0))
        {
          bi.setEnabled(true);
        }
        else {
          bi.setEnabled(false);
        }
      }
      else
        bi.setEnabled(false);
    }
    else if (sItemKey.equals("dshldtransnum")) {
      Object oTempAstUnit = getBillCardPanel().getBodyValueAt(iRow, "castunitname");
      Object oTempAstNum = getBillCardPanel().getBodyValueAt(iRow, "nshldtransastnum");
      if ((this.m_voBill != null) && (this.m_voBill.getItemValue(iRow, "isAstUOMmgt") != null) && (((Integer)this.m_voBill.getItemValue(iRow, "isAstUOMmgt")).intValue() == 1))
      {
        if ((oTempAstNum == null) || (oTempAstNum.toString().trim().length() == 0) || (oTempAstUnit == null) || (oTempAstUnit.toString().trim().length() == 0))
        {
          getBillCardPanel().getBillData().getBodyItem("dshldtransnum").setEnabled(false);

          String[] args = new String[1];
          args[0] = String.valueOf(iRow + 1);
          String message = NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000343", null, args);

          showHintMessage(message);
        } else {
          getBillCardPanel().getBillData().getBodyItem("dshldtransnum").setEnabled(true);
        }
      }
      else getBillCardPanel().getBillData().getBodyItem("dshldtransnum").setEnabled(true);
    }

    if (sItemKey.equals("dshldtransnum")) {
      if ((null != this.m_voBill.getItemValue(iRow, "fbillrowflag")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "fbillrowflag").toString()).intValue() == 1))
      {
        bi.setEnabled(false);
      }
      else {
        Object oTempAstUnit = getBillCardPanel().getBodyValueAt(iRow, "castunitname");
        Object oTempAstNum = getBillCardPanel().getBodyValueAt(iRow, "nshldtransastnum");

        if ((this.m_voBill != null) && (this.m_voBill.getItemValue(iRow, "isAstUOMmgt") != null) && (((Integer)this.m_voBill.getItemValue(iRow, "isAstUOMmgt")).intValue() == 1))
        {
          if ((oTempAstNum == null) || (oTempAstNum.toString().trim().length() == 0) || (oTempAstUnit == null) || (oTempAstUnit.toString().trim().length() == 0))
          {
            bi.setEnabled(false);

            String[] args = new String[1];
            args[0] = String.valueOf(iRow + 1);
            String message = NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000344", null, args);

            showHintMessage(message);
          } else {
            bi.setEnabled(true);
          }
        }
        else bi.setEnabled(true);
      }
    }
    else if (sItemKey.equals("nshldtransastnum"))
      if ((null != this.m_voBill.getItemValue(iRow, "isAstUOMmgt")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "isAstUOMmgt").toString()).intValue() != 0))
      {
        bi.setEnabled(true);
      }
      else
        bi.setEnabled(false);
  }

  public void getLotRefbyHand(String ColName)
  {
    int iSelrow = getBillCardPanel().getBillTable().getSelectedRow();
    String strColName = ColName;
    if (strColName == null) {
      return;
    }
    String sbatchcode = (String)getBillCardPanel().getBodyValueAt(iSelrow, "vbatchcode");

    if ((sbatchcode != null) && (sbatchcode.trim().length() > 0) && (getLotNumbRefPane().isClicked()))
    {
      return;
    }
    boolean isLotRight = getLotNumbRefPane().checkData();

    if (!isLotRight)
      getBillCardPanel().setBodyValueAt("", iSelrow, "vbatchcode");
  }

  public void pickupLotRef(String colname)
  {
    String s = colname;

    String sLot = null;
    int rownum = getBillCardPanel().getBillTable().getSelectedRow();
    if (s == null) {
      return;
    }
    if (s.equals("vbatchcode")) {
      sLot = (String)getBillCardPanel().getBodyValueAt(rownum, "vbatchcode");
      BatchCodeDefSetTool.setBatchCodeInfo(getBillCardPanel(), rownum, (String)this.m_voBill.getItemValue(rownum, "cinventoryid"), sLot, this.m_sCorpID);
    }
    
    //add by zwx 2014-11-24 多个批次号输入自动增行 edit by hk
    nc.ui.ic.pub.lot.LotNumbRefPane lotRef = (nc.ui.ic.pub.lot.LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode").getComponent();
    LotNumbRefVO[] voLot = null;
	try {
		voLot = lotRef.getLotNumbRefVOs();
	} catch (Exception exc) {
		nc.vo.scm.pub.SCMEnv.error(exc);
	}
	String cinv = null;
	InvVO voInv = m_voBill.getItemInv(rownum);
	getBillCardPanel().setBodyValueAt(voLot[0].getVbatchcode(),(rownum), "vbatchcode");
	getBillCardPanel().setBodyValueAt(voLot[0].getNinassistnum(),(rownum), "nshldtransastnum");
	getBillCardPanel().setBodyValueAt(voLot[0].getNinnum(),(rownum), "dshldtransnum");
	m_voBill.setItemFreeVO(rownum, voLot[0].getFreeVO());
	getBillCardPanel().setBodyValueAt(voLot[0].getFreeVO().getVfree0(), rownum, "vfree0");
	getBillCardPanel().setBodyValueAt(voLot[0].getFreeVO().getVfree1(), rownum, "vfree1");
	if (voLot != null && voLot.length > 1) {
		for (int j = 0; j < voLot.length - 1; j++) {
			cinv = voLot[j].getCinventoryid();
			getBillCardPanel().copyLine();
			getBillCardPanel().pasteLine();
			m_voBill.setItemInv(rownum, voInv);
			getBillCardPanel().setBodyValueAt(voLot[j+1].getVbatchcode(),(rownum+j), "vbatchcode");
			getBillCardPanel().setBodyValueAt(voLot[j+1].getNinnum(),(rownum+j), "dshldtransnum");
			getBillCardPanel().setBodyValueAt(voLot[j+1].getNinassistnum(),(rownum+j), "nshldtransastnum");
			m_voBill.setItemFreeVO(rownum+j, voLot[j+1].getFreeVO());
			getBillCardPanel().setBodyValueAt(voLot[j+1].getFreeVO().getVfree0(), rownum+j, "vfree0");
			getBillCardPanel().setBodyValueAt(voLot[j+1].getFreeVO().getVfree1(), rownum+j, "vfree1");
		}
	}

	int rows = getBillCardPanel().getRowCount();
	for (int i = 0; i < rows; i++) {
		getBillCardPanel().setBodyValueAt((i+1)*10, i, "crowno");
	}
	//end by zwx
  }

  private void setvfreeitem(InvVO voInv, int iSelrow) {
	  try {
			if (voInv.getIsFreeItemMgt().intValue() == 1) {
				FreeVO freevo = getLotNumbRefPane().getLotNumbDlg().getFreeVO();
				if (freevo != null && freevo.getVfree0() != null) {
					InvVO invvo = m_voBill.getItemInv(iSelrow);
					if (invvo != null) invvo.setFreeItemVO(freevo);
					getFreeItemRefPane().setFreeItemParam(invvo);
					getBillCardPanel().setBodyValueAt(freevo.getVfree0(), iSelrow, "vfree0");
					for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
						if (getBillCardPanel().getBodyItem("vfree" + i) != null)

						getBillCardPanel().setBodyValueAt(freevo.getAttributeValue("vfree" + i), iSelrow, "vfree" + i);
						else getBillCardPanel().setBodyValueAt(null, iSelrow, "vfree" + i);
					}
				}
				m_voBill.setItemFreeVO(iSelrow, freevo);
			}
		} catch (Exception e) {

		}	
}

protected void setBillValueVO(SpecialBillVO bvo)
  {
    setBillValueVO(bvo, true);
    for (int i = 1; i <= 20; i++) {
      String key = "vuserdef" + i;
      BillItem item = getBillCardPanel().getHeadItem(key);
      if ((item != null) && (item.getDataType() == 7)) {
        String pk = null;
        pk = (String)bvo.getHeaderValue("pk_defdoc" + i);
        if ((pk != null) && (pk.length() > 0))
          ((UIRefPane)item.getComponent()).setPK(bvo.getHeaderValue("pk_defdoc" + i));
      }
    }
  }

  protected void setBodyInvValue(int row, InvVO voInv)
  {
    try
    {
      getBillCardPanel().setBodyValueAt(voInv.getCinventoryid(), row, "cinventoryid");
    } catch (Exception e) {
    }
    try {
      getBillCardPanel().setBodyValueAt(voInv.getCinventorycode(), row, "cinventorycode");
    } catch (Exception e) {
    }
    try {
      getBillCardPanel().setBodyValueAt(voInv.getInvname(), row, "invname");
    } catch (Exception e) {
    }
    try {
      getBillCardPanel().setBodyValueAt(voInv.getInvspec(), row, "invspec");
    } catch (Exception e) {
    }
    try {
      getBillCardPanel().setBodyValueAt(voInv.getInvtype(), row, "invtype");
    } catch (Exception e) {
    }
    try {
      getBillCardPanel().setBodyValueAt(voInv.getMeasdocname(), row, "measdocname");
    }
    catch (Exception e) {
    }
    try {
      getBillCardPanel().setBodyValueAt(voInv.getCastunitid(), row, "castunitid");
    }
    catch (Exception e) {
    }
    try {
      getBillCardPanel().setBodyValueAt(voInv.getCastunitname(), row, "castunitname");
    } catch (Exception e) {
    }
    try {
      getBillCardPanel().setBodyValueAt(voInv.getHsl(), row, "hsl");

      this.m_voBill.setItemValue(row, "hsl", voInv.getHsl());

      this.m_voBill.setItemValue(row, "isSolidConvRate", voInv.getIsSolidConvRate());
    }
    catch (Exception e)
    {
    }
    try {
      getBillCardPanel().setBodyValueAt(voInv.getNplannedprice(), row, "jhdj");
    }
    catch (Exception e)
    {
    }

    try
    {
      getBillCardPanel().setBodyValueAt("", row, "vfree0");
    }
    catch (Exception e)
    {
    }
  }

  protected void setNewBillInitData()
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      if (ce == null) {
        SCMEnv.out("ce null ERR.");
        return;
      }
      try {
        getBillCardPanel().setHeadItem("dbilldate", this.m_sLogDate);
        getBillCardPanel().setHeadItem("vshldarrivedate", this.m_sLogDate);
      }
      catch (Exception e) {
      }
      try {
        getBillCardPanel().setTailItem("coperatorid", this.m_sUserID);
        getBillCardPanel().setTailItem("coperatorname", this.m_sUserName);
        getBillCardPanel().setTailItem("cauditorid", null);
        getBillCardPanel().setTailItem("cauditorname", null);
        getBillCardPanel().setTailItem("vadjuster", null);
        getBillCardPanel().setTailItem("vadjustername", null);

        if (getBillCardPanel().getTailItem("clastmodiid") != null)
          getBillCardPanel().setTailItem("clastmodiid", this.m_sUserID);
        if (getBillCardPanel().getTailItem("clastmodiname") != null) {
          getBillCardPanel().setTailItem("clastmodiname", this.m_sUserName);
        }
        UFDateTime ufdPre = new UFDateTime(System.currentTimeMillis());
        if (getBillCardPanel().getTailItem("tlastmoditime") != null) {
          getBillCardPanel().setTailItem("tlastmoditime", ufdPre.toString());
        }

        if (getBillCardPanel().getTailItem("tmaketime") != null) {
          getBillCardPanel().setTailItem("tmaketime", ufdPre.toString());
        }

        if (this.m_voBill != null) {
          this.m_voBill.setHeaderValue("coperatorid", this.m_sUserID);
          this.m_voBill.setHeaderValue("coperatorname", this.m_sUserName);
          this.m_voBill.setHeaderValue("cauditorid", null);
          this.m_voBill.setHeaderValue("cauditorname", null);
          this.m_voBill.setHeaderValue("vadjuster", null);
          this.m_voBill.setHeaderValue("vadjustername", null);
          this.m_voBill.setHeaderValue("clastmodiid", this.m_sUserID);
          this.m_voBill.setHeaderValue("clastmodiname", this.m_sUserName);
          this.m_voBill.setHeaderValue("tlastmoditime", ufdPre.toString());
          this.m_voBill.setHeaderValue("tmaketime", ufdPre.toString());
        }
      }
      catch (Exception e) {
      }
      try {
        getBillCardPanel().setHeadItem("pk_corp", this.m_sCorpID);
        getBillCardPanel().setHeadItem("vbillcode", this.m_sCorpID);
        if (this.m_voBill != null) {
          this.m_voBill.setHeaderValue("pk_corp", this.m_sCorpID);
          this.m_voBill.setHeaderValue("vbillcode", this.m_sCorpID);
        }
      }
      catch (Exception e)
      {
      }
    }
    catch (Exception e)
    {
    }
  }

  protected void setTailValue(int row)
  {
    if (this.m_voBill == null) {
      SCMEnv.out("no vobill.no taildata");
      return;
    }

    Object oInWhID = null;
    oInWhID = this.m_voBill.getHeaderValue("cinwarehouseid");
    Object oOutWhID = null;
    oOutWhID = this.m_voBill.getHeaderValue("coutwarehouseid");
    if (oOutWhID == null)
      oOutWhID = (String)this.m_voBill.getItemValue(row, "cwarehouseid");
    if (oOutWhID == null) {
      return;
    }
    Object oInvID = this.m_voBill.getItemValue(row, "cinventoryid");
    if (oInvID == null) {
      SCMEnv.out(row + "row data ERR");
      return;
    }

    BillItem biMax = getBillCardPanel().getTailItem("nmaxstocknum");
    BillItem biMin = getBillCardPanel().getTailItem("nminstocknum");
    BillItem biOpt = getBillCardPanel().getTailItem("norderpointnum");
    BillItem biSafe = getBillCardPanel().getTailItem("nsafestocknum");
    BillItem biWhQty = getBillCardPanel().getTailItem("bkxcl");
    BillItem biBdQty = getBillCardPanel().getTailItem("xczl");
    BillItem biOutWhQty = getBillCardPanel().getTailItem("outbkxcl");

    Object oInWhQty = null;
    Object oOutWhQty = null;
    Object oTotalQty = null;
    Object nmaxstocknum = null;
    Object nminstocknum = null;
    Object norderpointnum = null;
    Object nsafestocknum = null;
    Object nchzl = null;

    int iFlag = 0;
    if (((biMax != null) && (biMax.isShow())) || ((biMin != null) && (biMin.isShow())) || ((biOpt != null) && (biOpt.isShow())) || ((biSafe != null) && (biSafe.isShow()) && (this.m_sCorpID != null) && (oOutWhID != null) && (oInvID != null)))
    {
      nmaxstocknum = this.m_voBill.getItemValue(row, "nmaxstocknum");
      nminstocknum = this.m_voBill.getItemValue(row, "nminstocknum");
      norderpointnum = this.m_voBill.getItemValue(row, "norderpointnum");
      nsafestocknum = this.m_voBill.getItemValue(row, "nsafestocknum");
      nchzl = this.m_voBill.getItemInv(row).getChzl();

      if ((nmaxstocknum == null) && (nminstocknum == null) && (norderpointnum == null) && (nsafestocknum == null) && (nchzl == null))
      {
        iFlag++;
      }
    }
    if (((biWhQty != null) && (biWhQty.isShow())) || ((biBdQty != null) && (biBdQty.isShow()) && (this.m_sCorpID != null) && (oOutWhID != null) && (oInvID != null)))
    {
      iFlag += 2;
    }
    switch (iFlag) {
    case 0:
      break;
    case 1:
      iFlag = 202;
      break;
    case 2:
      iFlag = 201;
      break;
    case 3:
      iFlag = 200;
    }

    if (iFlag > 0)
    {
      ArrayList alIDs = new ArrayList();

      ArrayList alQty = null;
      try {
        String sCalID = (String)((Object[])(Object[])CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody", (String)oOutWhID))[0];

        alIDs.add(oOutWhID);
        alIDs.add(oInvID);
        alIDs.add(sCalID);
        alIDs.add(this.m_sCorpID);

        InvVO voInvTmp = (InvVO)SpecialBillHelper.queryInfo(new Integer(iFlag), alIDs);

        if (voInvTmp != null)
        {
          oOutWhQty = voInvTmp.getBkxcl();
          oTotalQty = voInvTmp.getXczl();
          nmaxstocknum = voInvTmp.getNmaxstocknum();
          nminstocknum = voInvTmp.getNminstocknum();
          norderpointnum = voInvTmp.getNorderpointnum();
          nsafestocknum = voInvTmp.getNsafestocknum();

          this.m_voBill.setItemValue(row, "inbkxcl", oInWhQty);
          this.m_voBill.setItemValue(row, "outbkxcl", oOutWhQty);
          this.m_voBill.setItemValue(row, "bkxcl", oOutWhQty);
          this.m_voBill.setItemValue(row, "xczl", oTotalQty);
          this.m_voBill.setItemValue(row, "nmaxstocknum", nmaxstocknum);
          this.m_voBill.setItemValue(row, "nminstocknum", nminstocknum);
          this.m_voBill.setItemValue(row, "norderpointnum", norderpointnum);
          this.m_voBill.setItemValue(row, "nsafestocknum", nsafestocknum);
          this.m_voBill.getItemInv(row).setChzl(new UFDouble(0.0D));
        }
      }
      catch (Exception e)
      {
        SCMEnv.error(e);
      }

    }

    BillItem biTail = getBillCardPanel().getTailItem("inbkxcl");
    if (biTail != null) {
      if (oInWhQty != null)
        biTail.setValue(new UFDouble(oInWhQty.toString()));
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("outbkxcl");
    if (biTail != null) {
      if (oOutWhQty != null)
        biTail.setValue(new UFDouble(oOutWhQty.toString()));
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("bkxcl");
    if (biTail != null) {
      if (oOutWhQty != null)
        biTail.setValue(new UFDouble(oOutWhQty.toString()));
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("xczl");
    if (biTail != null) {
      if (oTotalQty != null)
        biTail.setValue(new UFDouble(oTotalQty.toString()));
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("nmaxstocknum");
    if (biTail != null) {
      if (nmaxstocknum != null)
        biTail.setValue(new UFDouble(nmaxstocknum.toString()));
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("nminstocknum");
    if (biTail != null) {
      if (nminstocknum != null)
        biTail.setValue(new UFDouble(nminstocknum.toString()));
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("nsafestocknum");
    if (biTail != null) {
      if (nsafestocknum != null)
        biTail.setValue(new UFDouble(nsafestocknum.toString()));
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("norderpointnum");
    if (biTail != null)
      if (norderpointnum != null)
        biTail.setValue(new UFDouble(norderpointnum.toString()));
      else
        biTail.setValue(null);
  }

  protected void setTailValue(InvVO voInv)
  {
    BillItem biTail = null;
    biTail = getBillCardPanel().getTailItem("bkxcl");
    if (biTail != null) {
      if (voInv != null)
        biTail.setValue(voInv.getBkxcl());
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("outbkxcl");
    if (biTail != null) {
      if (voInv != null)
        biTail.setValue(voInv.getBkxcl());
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("xczl");
    if (biTail != null) {
      if (voInv != null)
        biTail.setValue(voInv.getBkxcl());
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("nmaxstocknum");
    if (biTail != null) {
      if (voInv != null)
        biTail.setValue(voInv.getNmaxstocknum());
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("nminstocknum");
    if (biTail != null) {
      if (voInv != null)
        biTail.setValue(voInv.getNminstocknum());
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("nsafestocknum");
    if (biTail != null) {
      if (voInv != null)
        biTail.setValue(voInv.getNsafestocknum());
      else
        biTail.setValue(null);
    }
    biTail = getBillCardPanel().getTailItem("norderpointnum");
    if (biTail != null)
      if (voInv != null)
        biTail.setValue(voInv.getNorderpointnum());
      else
        biTail.setValue(null);
  }

  protected void synchLineData(int iFirstLine, int iLastLine, int iCol, int iType)
  {
    if ((iFirstLine < 0) || (iLastLine < 0)) {
      return;
    }

    if (this.m_voBill == null) {
      this.m_voBill = new SpecialBillVO();
    }
    switch (iType) {
    case 1:
      this.m_voBill.insertItem(iFirstLine);
      break;
    case 0:
      break;
    case -1:
      this.m_voBill.removeItem(iFirstLine);
    }
  }

  public void tableChanged(TableModelEvent e)
  {
    if (((e.getType() == 1) || (e.getType() == -1)) && (e.getSource() == getBillCardPanel().getBillModel()))
    {
      synchLineData(e.getFirstRow(), e.getLastRow(), e.getColumn(), e.getType());
    }
  }

  public void actionPerformed(ActionEvent e)
  {
  }

  protected ClientUIInAndOut getAuditDlg(String sTitle, ArrayList alInVO, ArrayList alOutVO)
  {
    if (this.m_dlgInOut == null) {
      try
      {
        this.m_dlgInOut = new ClientUIInAndOut(this, sTitle);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    this.m_dlgInOut.setVO(this.m_voBill, alInVO, alOutVO, this.m_sBillTypeCode, this.m_voBill.getPrimaryKey().trim(), this.m_sCorpID, this.m_sUserID);

    this.m_dlgInOut.setName("BillDlg");

    return this.m_dlgInOut;
  }

  public void onMenuItemClick(ActionEvent e)
  {
    UIMenuItem item = (UIMenuItem)e.getSource();
    if (item == getBillCardPanel().getCopyLineMenuItem())
      onCopyRow();
    else if (item == getBillCardPanel().getPasteLineMenuItem())
      onPasteRow();
    else if (item == getBillCardPanel().getInsertLineMenuItem()) {
      onInsertRow();
    }
    else if (item == getBillCardPanel().getDelLineMenuItem()) {
      onDeleteRow();
    }
    else if (item == getBillCardPanel().getAddLineMenuItem()) {
      onAddRow();
    }
    else if (item == getBillCardPanel().getPasteLineToTailMenuItem())
      onPasteRowTail();
  }

  protected GeneralBillVO changeFromSpecialVOtoGeneralVO(SpecialBillVO sbvo, int iInOutFlag)
  {
    if ((sbvo == null) || (sbvo.getHeaderVO() == null) || (sbvo.getChildrenVO() == null))
    {
      return null;
    }
    int iItemNumb = sbvo.getChildrenVO().length;
    if (iItemNumb < 1) {
      return null;
    }
    GeneralBillVO gbvo = new GeneralBillVO(iItemNumb);

    gbvo.setParentVO(changeFromSpecialVOtoGeneralVOAboutHeader(gbvo, sbvo, iInOutFlag));

    for (int j = 0; j < iItemNumb; j++) {
      gbvo.setItem(j, changeFromSpecialVOtoGeneralVOAboutItem(gbvo, sbvo, iInOutFlag, j));
    }

    return gbvo;
  }

  public void afterInvEdit(Object value, int row)
  {
    try
    {
      setTailValue(null);

      if ((value == null) || (value.toString().trim().length() == 0)) {
        clearRowData(row);
      }
      else
      {
        String sTempID1 = (String)value;
        String sTempID2 = null;
        if (getBillCardPanel().getHeadItem("coutwarehouseid") != null)
          sTempID2 = getBillCardPanel().getHeadItem("coutwarehouseid").getValue();
        ArrayList alIDs = new ArrayList();
        alIDs.add(sTempID2);
        alIDs.add(sTempID1);
        alIDs.add(this.m_sUserID);
        alIDs.add(this.m_sCorpID);

        InvVO voInv = (InvVO)SpecialBillHelper.queryInfo(new Integer(0), alIDs);

        this.m_voBill.setItemInv(row, voInv);

        setBodyInvValue(row, voInv);

        setTailValue(row);

        String[] sIKs = getClearIDs(1, "cinventorycode");

        clearRowData(row, sIKs);

        showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000155"));
      }
    } catch (Exception e2) {
      SCMEnv.error(e2);
    }
  }

  public void afterNshldtransastnumEdit(BillEditEvent e)
  {
    int iRow = e.getRow();

    UFDouble value = this.ZERO;
    UFDouble hslnow = this.ZERO;
    if (getBillCardPanel().getBodyValueAt(iRow, "nshldtransastnum") != null) {
      value = new UFDouble(getBillCardPanel().getBodyValueAt(iRow, "nshldtransastnum").toString());
    }

    if (getBillCardPanel().getBodyValueAt(iRow, "hsl") != null) {
      hslnow = new UFDouble(getBillCardPanel().getBodyValueAt(iRow, "hsl").toString());
    }
    try
    {
      if ((null != this.m_voBill.getItemValue(iRow, "isAstUOMmgt")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "isAstUOMmgt").toString()).intValue() != 0) && ((null == this.m_voBill.getItemValue(iRow, "isSolidConvRate")) || (Integer.valueOf(this.m_voBill.getItemValue(iRow, "isSolidConvRate").toString()).intValue() != 0)))
      {
        getBillCardPanel().setBodyValueAt(value.multiply(hslnow), iRow, "dshldtransnum");
        if (this.m_voBill != null) {
          this.m_voBill.setItemValue(iRow, "dshldtransnum", getBillCardPanel().getBodyValueAt(iRow, "dshldtransnum"));
        }

      }

      calculateByHsl(iRow, "dshldtransnum", "nshldtransastnum", 1);
    }
    catch (Exception e2)
    {
      SCMEnv.error(e2);
    }
  }

  protected void getCEnvInfo()
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      try
      {
        this.m_sUserID = ce.getUser().getPrimaryKey();
      } catch (Exception e) {
        SCMEnv.out("test user id is 2011000001");
        this.m_sUserID = "2011000001";
      }

      try
      {
        this.m_sUserName = ce.getUser().getUserName();
      } catch (Exception e) {
        SCMEnv.out("test user name is 张三");
        this.m_sUserName = NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000156");
      }

      try
      {
        this.m_sCorpID = ce.getCorporation().getPrimaryKey();
        SCMEnv.out("---->corp id is " + this.m_sCorpID);
      }
      catch (Exception e)
      {
      }
      try {
        if (ce.getDate() != null)
          this.m_sLogDate = ce.getDate().toString();
      }
      catch (Exception e)
      {
      }
    }
    catch (Exception e)
    {
    }
  }

  protected void clearAuditBillFlag()
  {
    try
    {
      try
      {
        getBillCardPanel().setTailItem("cauditorid", "");
        getBillCardPanel().setTailItem("cauditorname", "");
        if (this.m_voBill != null) {
          this.m_voBill.setHeaderValue("cauditorid", "");
          this.m_voBill.setHeaderValue("cauditorname", "");
        }
      }
      catch (Exception e)
      {
      }
    }
    catch (Exception e)
    {
    }
  }

  protected void dispBodyRow(UITable mUITable)
  {
    mUITable.clearSelection();
    this.m_iTotalListBodyNum = mUITable.getRowCount();
    if (this.m_iTotalListBodyNum <= 0) {
      return;
    }
    if (this.m_iLastSelCardBodyRow < 0) {
      this.m_iLastSelCardBodyRow = 0;
    }
    if (this.m_iLastSelCardBodyRow > this.m_iTotalListBodyNum - 1) {
      this.m_iLastSelCardBodyRow = (this.m_iTotalListBodyNum - 1);
    }
    if (this.m_iLastSelCardBodyRow < 0) {
      this.m_iLastSelCardBodyRow = 0;
    }

    mUITable.setRowSelectionInterval(this.m_iLastSelCardBodyRow, this.m_iLastSelCardBodyRow);
  }

  protected void filterMeas(int row)
  {
    InvVO voInv = this.m_voBill.getItemInv(row);

    UIRefPane refCast = (UIRefPane)getBillCardPanel().getBodyItem("castunitname").getComponent();

    this.m_voInvMeas.filterMeas(this.m_sCorpID, voInv.getCinventoryid(), refCast);
  }

  public void filterRef(String cropid)
  {
    if ((getBillCardPanel().getHeadItem("cwarehouseid") != null) && (getBillCardPanel().getHeadItem("cwarehouseid").getComponent() != null))
    {
      ((UIRefPane)getBillCardPanel().getHeadItem("cwarehouseid").getComponent()).setWhereString("gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'");
    }

    if ((getBillCardPanel().getHeadItem("cwastewarehouseid") != null) && (getBillCardPanel().getHeadItem("cwastewarehouseid").getComponent() != null))
    {
      ((UIRefPane)getBillCardPanel().getHeadItem("cwastewarehouseid").getComponent()).setWhereString("gubflag='Y' and sealflag='N' and pk_corp='" + cropid + "'");
    }

    if ((getBillCardPanel().getHeadItem("coutwarehouseid") != null) && (getBillCardPanel().getHeadItem("coutwarehouseid").getComponent() != null))
    {
      ((UIRefPane)getBillCardPanel().getHeadItem("coutwarehouseid").getComponent()).setWhereString("gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'");
    }

    if ((getBillCardPanel().getHeadItem("cinwarehouseid") != null) && (getBillCardPanel().getHeadItem("cinwarehouseid").getComponent() != null))
    {
      ((UIRefPane)getBillCardPanel().getHeadItem("cinwarehouseid").getComponent()).setWhereString("gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'");
    }

    if ((getBillCardPanel().getHeadItem("ccustomerid") != null) && (getBillCardPanel().getHeadItem("ccustomerid").getComponent() != null))
    {
      UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("ccustomerid").getComponent();

      ref.getRefModel().setRefNameField("bd_cubasdoc.custshortname");

      ref.setWhereString("(custflag ='0' or custflag ='2') and bd_cumandoc.pk_corp='" + cropid + "'");
    }

    if ((getBillCardPanel().getHeadItem("cproviderid") != null) && (getBillCardPanel().getHeadItem("cproviderid").getComponent() != null))
    {
      UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("cproviderid").getComponent();

      ref.getRefModel().setRefNameField("bd_cubasdoc.custshortname");

      ref.setWhereString("(custflag ='1' or custflag ='3') and bd_cumandoc.pk_corp='" + cropid + "'");
    }

    BillItem bi = getBillCardPanel().getBodyItem("cinventorycode");
    if ((bi != null) && (bi.getComponent() != null))
    {
      UIRefPane invRef = (UIRefPane)bi.getComponent();
      invRef.setTreeGridNodeMultiSelected(true);
      invRef.setMultiSelectedEnabled(true);

      invRef.setWhereString(" bd_invbasdoc.discountflag='N' and bd_invbasdoc.laborflag='N'  and bd_invmandoc.sealflag ='N' and bd_invmandoc.pk_corp='" + cropid + "'");
    }

    if ((getBillCardPanel().getBodyItem("cwarehouseid") != null) && (getBillCardPanel().getBodyItem("cwarehouseid").getComponent() != null))
    {
      ((UIRefPane)getBillCardPanel().getBodyItem("cwarehouseid").getComponent()).setWhereString("gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'");
    }

    if ((getBillCardPanel().getBodyItem("cwarehousename") != null) && (getBillCardPanel().getBodyItem("cwarehousename").getComponent() != null))
    {
      ((UIRefPane)getBillCardPanel().getBodyItem("cwarehousename").getComponent()).setWhereString("gubflag='N' and sealflag='N' and pk_corp='" + cropid + "'");
    }
  }

  protected InvOnHandDialog getIohdDlg()
  {
    if (null == this.m_iohdDlg) {
      this.m_iohdDlg = new InvOnHandDialog(this);
    }
    return this.m_iohdDlg;
  }

  public UFBoolean isFixFlag(int row)
  {
    Integer isFixFlag = (Integer)this.m_voBill.getItemValue(row, "isSolidConvRate");
    return new UFBoolean(isFixFlag.intValue() == 1);
  }

  public void onRowQuyQty()
  {
    SpecialBillVO nowVObill = null;
    int rownow = -1;
    if (this.m_iMode != 4)
    {
      rownow = getBillCardPanel().getBillTable().getSelectedRow();
      nowVObill = this.m_voBill;
    }
    else
    {
      rownow = getBillListPanel().getBodyTable().getSelectedRow();
      nowVObill = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);
    }
    if (rownow < 0) {
      return;
    }
    String WhID = "";
    String InvID = "";

    if ((nowVObill != null) && (rownow >= 0))
    {
      if (this.m_sBillTypeCode == null) {
        return;
      }
      if ((this.m_sBillTypeCode.trim().equalsIgnoreCase("4L")) || (this.m_sBillTypeCode.trim().equalsIgnoreCase("4M")) || (this.m_sBillTypeCode.trim().equalsIgnoreCase("4N")))
      {
        WhID = (String)nowVObill.getItemValue(rownow, "cwarehouseid");
      }
      else {
        WhID = (String)nowVObill.getHeaderValue("coutwarehouseid");
      }
      InvID = (String)nowVObill.getItemValue(rownow, "cinventoryid");
    }

    if ((WhID == null) || (WhID.trim().equals("")) || (InvID == null) || (InvID.trim().equals(""))) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000157"));
      return;
    }

    getIohdDlg().setParam(WhID, InvID);
    getIohdDlg().showModal();
  }

  protected void setAuditBillFlag()
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      if (ce == null) {
        SCMEnv.out("ce null ERR.");
        return;
      }

      try
      {
        getBillCardPanel().setTailItem("cauditorid", this.m_sUserID);
        getBillCardPanel().setTailItem("cauditorname", this.m_sUserName);
        if (this.m_voBill != null) {
          this.m_voBill.setHeaderValue("cauditorid", this.m_sUserID);
          this.m_voBill.setHeaderValue("cauditorname", this.m_sUserName);
        }
      }
      catch (Exception e)
      {
      }
    }
    catch (Exception e)
    {
    }
  }

  protected void setDlg(InvOnHandDialog newDlg)
  {
    this.m_iohdDlg = newDlg;
  }

  protected void setInOrOutBillFlag(int iFlag)
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      if (ce == null) {
        SCMEnv.out("ce null ERR.");
        return;
      }

      try
      {
        if (-1 == iFlag) {
          getBillCardPanel().setTailItem("cauditorid", this.m_sUserID);
          getBillCardPanel().setTailItem("cauditorname", this.m_sUserName);
          if (this.m_voBill != null) {
            this.m_voBill.setHeaderValue("cauditorid", this.m_sUserID);
            this.m_voBill.setHeaderValue("cauditorname", this.m_sUserName);
          }
        } else if (1 == iFlag) {
          getBillCardPanel().setTailItem("vadjuster", this.m_sUserID);
          getBillCardPanel().setTailItem("vadjustername", this.m_sUserName);
          if (this.m_voBill != null) {
            this.m_voBill.setHeaderValue("vadjuster", this.m_sUserID);
            this.m_voBill.setHeaderValue("vadjustername", this.m_sUserName);
          }
        }
      }
      catch (Exception e)
      {
      }
    }
    catch (Exception e)
    {
    }
  }

  protected void switchBillToList()
  {
    try
    {
      getBillListPanel().getHeadBillModel().clearBodyData();
      getBillListPanel().getBodyBillModel().clearBodyData();

      if (this.m_iTotalListHeadNum < 1) {
        getBillListPanel().getHeadTable().clearSelection();
        getBillListPanel().getHeadTable().setRowSelectionInterval(0, 0);
        dispBodyRow(getBillListPanel().getBodyTable());
        return;
      }

      if (this.m_iLastSelListHeadRow < 0)
        this.m_iLastSelListHeadRow = 0;
      SpecialBillVO voTemp = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

      if (voTemp == null) {
        return;
      }

      SpecialBillItemVO[] voItems = voTemp.getItemVOs();

      if ((voItems == null) || (voItems.length < 1)) {
        qryItems(new int[] { this.m_iLastSelListHeadRow }, new String[] { voTemp.getPrimaryKey() });
      }

      voTemp = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);
      voItems = (SpecialBillItemVO[])(SpecialBillItemVO[])voTemp.getChildrenVO();

      getBillListPanel().getHeadBillModel().setSortColumn(null);
      getBillListPanel().getHeadBillModel().setBodyDataVO(getListHeaderVOs());

      getBillListPanel().getHeadTable().clearSelection();
      getBillListPanel().getHeadTable().setRowSelectionInterval(this.m_iLastSelListHeadRow, this.m_iLastSelListHeadRow);

      getBillListPanel().getBodyBillModel().setBodyDataVO(voItems);

      dispBodyRow(getBillListPanel().getBodyTable());
    }
    catch (Exception e1) {
      SCMEnv.out("数据通讯失败！");
      handleException(e1);
    }
  }

  protected void switchListToBill()
  {
    if ((this.m_alListData != null) && (this.m_alListData.size() > 0) && (this.m_iLastSelListHeadRow >= 0) && (this.m_iLastSelListHeadRow < this.m_alListData.size()))
    {
      this.m_voBill = null;
      this.m_voBill = ((SpecialBillVO)((SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).clone());
      getBillCardPanel().addNew();

      SpecialBillItemVO[] voaItem = this.m_voBill.getItemVOs();
      if ((voaItem == null) || (voaItem.length < 1)) {
        qryItems(new int[] { this.m_iLastSelListHeadRow }, new String[] { this.m_voBill.getPrimaryKey() });
      }

      voaItem = (SpecialBillItemVO[])(SpecialBillItemVO[])this.m_voBill.getChildrenVO();
      qryCalbodyByWhid(this.m_voBill.getHeaderVO());
      this.m_alListData.set(this.m_iLastSelListHeadRow, this.m_voBill);
      setBillValueVO(this.m_voBill);

      getBillCardPanel().getBillModel().execLoadFormula();
      getBillCardPanel().execHeadTailLoadFormulas();

      dispBodyRow(getBillCardPanel().getBillTable());

      getBillCardPanel().updateValue();
    }
    else {
      getBillCardPanel().getBillData().clearViewData();
      getBillCardPanel().updateValue();
    }
  }

  public void afterAstUOMEdit(int rownow)
  {
    String sAstUomPK = ((UIRefPane)getBillCardPanel().getBodyItem("castunitname").getComponent()).getRefPK();

    String sAstUomname = ((UIRefPane)getBillCardPanel().getBodyItem("castunitname").getComponent()).getRefName();

    this.m_voBill.setItemValue(rownow, "castunitid", sAstUomPK);
    this.m_voBill.setItemValue(rownow, "castunitname", sAstUomname);
    getBillCardPanel().setBodyValueAt(sAstUomPK, rownow, "castunitid");
    getBillCardPanel().setBodyValueAt(sAstUomname, rownow, "castunitname");
    try
    {
      MeasureRateVO voMeas = this.m_voInvMeas.getMeasureRate(this.m_voBill.getItemInv(rownow).getCinventoryid(), sAstUomPK);

      if (voMeas != null) {
        UFDouble hsl = voMeas.getMainmeasrate();
        getBillCardPanel().setBodyValueAt(hsl, rownow, "hsl");
        getBillCardPanel().updateUI();

        hsl = (UFDouble)getBillCardPanel().getBodyValueAt(rownow, "hsl");
        this.m_voBill.setItemValue(rownow, "hsl", hsl);
        this.m_voBill.setItemValue(rownow, "isSolidConvRate", voMeas.getFixedflag());
      }
      calculateByHsl(rownow, "dshldtransnum", "nshldtransastnum", 1);
    }
    catch (Exception e2) {
      SCMEnv.error(e2);
    }
  }

  public void afterVendorEdit(int rownow)
  {
    String sVendorPK = ((UIRefPane)getBillCardPanel().getBodyItem("cvendorname").getComponent()).getRefPK();

    String sVendorName = ((UIRefPane)getBillCardPanel().getBodyItem("cvendorname").getComponent()).getRefName();

    this.m_voBill.setItemValue(rownow, "cvendorid", sVendorPK);
    this.m_voBill.setItemValue(rownow, "cvendorname", sVendorName);
    getBillCardPanel().setBodyValueAt(sVendorPK, rownow, "cvendorid");
    getBillCardPanel().setBodyValueAt(sVendorName, rownow, "cvendorname");
  }

  public void afterDefEdit(BillEditEvent e)
  {
    if (e.getPos() == 0) {
      String sVdefPkKey = "pk_defdoc" + e.getKey().substring("vuserdef".length());

      DefSetTool.afterEditHead(getBillCardPanel().getBillData(), e.getKey(), sVdefPkKey);

      this.m_voBill.setHeaderValue(e.getKey(), getBillCardPanel().getHeadItem(e.getKey()).getValue());

      this.m_voBill.setHeaderValue(sVdefPkKey, getBillCardPanel().getHeadItem(sVdefPkKey).getValue());
    }
    else if (e.getPos() == 1) {
      String sVdefPkKey = "pk_defdoc" + e.getKey().substring("vuserdef".length());

      DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), e.getRow(), e.getKey(), sVdefPkKey);

      this.m_voBill.setItemValue(e.getRow(), e.getKey(), getBillCardPanel().getBodyValueAt(e.getRow(), e.getKey()));

      this.m_voBill.setItemValue(e.getRow(), sVdefPkKey, getBillCardPanel().getBodyValueAt(e.getRow(), sVdefPkKey));
    }
  }

  public void afterValidateEdit(BillEditEvent e)
  {
    int iSelrow = e.getRow();
    if ((null != getBillCardPanel().getBodyValueAt(iSelrow, e.getKey())) && (getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()).toString().trim().length() != 0))
    {
      int days = this.m_voBill.getItemInv(iSelrow).getQualityDay() == null ? 0 : this.m_voBill.getItemInv(iSelrow).getQualityDay().intValue();

      UFDate validateDate = new UFDate(getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()).toString());

      UFDate productDate = validateDate.getDateBefore(days);
      getBillCardPanel().setBodyValueAt(productDate, iSelrow, "scrq");
    }
    else {
      getBillCardPanel().setBodyValueAt("", iSelrow, "scrq");
    }
    this.m_voBill.setItemValue(iSelrow, "dvalidate", getBillCardPanel().getBodyValueAt(iSelrow, "dvalidate"));

    this.m_voBill.setItemValue(iSelrow, "scrq", getBillCardPanel().getBodyValueAt(iSelrow, "scrq"));
  }

  protected String[] getClearIDs(int iBillFlag, String sWhatChange)
  {
    if (sWhatChange == null)
      return null;
    String[] sReturnString = null;
    sWhatChange = sWhatChange.trim();
    if (sWhatChange.equals("cinventorycode"))
    {
      sReturnString = new String[6];
      sReturnString[0] = "vbatchcode";
      sReturnString[1] = "vfree0";
      sReturnString[2] = "dshldtransnum";
      sReturnString[3] = "nshldtransastnum";

      sReturnString[4] = "scrq";
      sReturnString[5] = "dvalidate"; } else {
      if ((sWhatChange.equals("cwarehousename")) && (iBillFlag == 1))
      {
        sReturnString = new String[6];
        sReturnString[0] = "vbatchcode";
        sReturnString[2] = "dshldtransnum";
        sReturnString[3] = "nshldtransastnum";
        sReturnString[4] = "scrq";
        sReturnString[5] = "dvalidate";

        return null;
      }if (sWhatChange.equals("vfree0"))
      {
        sReturnString = new String[3];
        sReturnString[0] = "vbatchcode";
        sReturnString[1] = "scrq";
        sReturnString[2] = "dvalidate";

        return null;
      }if (sWhatChange.equals("coutwarehouseid")) {
        sReturnString = new String[5];
        sReturnString[0] = "vbatchcode";
        sReturnString[1] = "dshldtransnum";
        sReturnString[2] = "nshldtransastnum";
        sReturnString[3] = "scrq";
        sReturnString[4] = "dvalidate";

        return null;
      }if ((sWhatChange.equals("cwarehouseid")) && (iBillFlag == 0)) {
        sReturnString = new String[5];
        sReturnString[0] = "vbatchcode";
        sReturnString[1] = "dshldtransnum";
        sReturnString[2] = "nshldtransastnum";
        sReturnString[3] = "scrq";
        sReturnString[4] = "dvalidate";

        return null;
      }
    }
    return sReturnString;
  }

  protected BillFormulaContainer getFormulaContainer()
  {
    if (this.m_formulaParse == null) {
      this.m_formulaParse = new BillFormulaContainer(getBillListPanel());
    }
    return this.m_formulaParse;
  }

  public static CircularlyAccessibleValueObject[] getBillBodyData(ArrayList alData) {
    ArrayList alBody = new ArrayList();
    for (int i = 0; i < alData.size(); i++) {
      AggregatedValueObject vo = (AggregatedValueObject)alData.get(i);
      CircularlyAccessibleValueObject[] itemVos = (CircularlyAccessibleValueObject[])vo.getChildrenVO();
      if ((itemVos != null) && (itemVos.length > 0)) {
        for (int j = 0; j < itemVos.length; j++)
        {
          alBody.add(itemVos[j]);
        }
      }
    }

    return (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])alBody.toArray(new CircularlyAccessibleValueObject[alBody.size()]);
  }

  protected void loadBillListPanel(QryConditionVO qcvo)
  {
    try
    {
      this.m_alListData = SpecialBillHelper.queryBills(this.m_sBillTypeCode, qcvo);

      if ((this.m_alListData != null) && (this.m_alListData.size() > 0))
      {
        this.m_iTotalListHeadNum = this.m_alListData.size();

        this.m_iLastSelListHeadRow = 0;

        SpecialBillVO sbvotemp = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

        sbvotemp.calConvRate();
        this.m_alListData.set(this.m_iLastSelListHeadRow, sbvotemp);

        getFormulaBillContainer().formulaBill(this.m_alListData, getFormulaItemHeader(), getFormulaItemBody());
        try
        {
          BatchCodeDefSetTool.execFormulaForBatchCode(getBillBodyData(this.m_alListData));
        } catch (Exception e) {
          SCMEnv.error(e);
        }
        switchBillToList();
      }
      else {
        dealNoData();
      }
      this.m_iMode = 4;
    } catch (Exception e) {
      SCMEnv.out("数据通讯失败！");
      showErrorMessage(e.getMessage());
      handleException(e);
    }
  }

  protected void reLoadBill()
  {
    try
    {
      this.m_iMode = 3;

      QryConditionVO qcvo = new QryConditionVO();
      qcvo.setQryCond("cbilltypecode='" + this.m_sBillTypeCode + "' and cspecialhid='" + this.m_voBill.getPrimaryKey().trim() + "'");

      this.m_voBill = ((SpecialBillVO)SpecialBillHelper.queryBills(this.m_sBillTypeCode, qcvo).get(0));

      if (this.m_voBill != null) {
        this.m_alListData.set(this.m_iLastSelListHeadRow, this.m_voBill.clone());
        switchListToBill();
      }
    } catch (Exception e) {
      handleException(e);
    }
  }

  public void setBodyMenuShow(boolean bShowFlag)
  {
    getBillCardPanel().setBodyMenuShow(bShowFlag);
  }

  protected void addBillVO()
  {
    if (this.m_alListData == null) {
      this.m_alListData = new ArrayList();
    }
    this.m_alListData.add(this.m_iLastSelListHeadRow, this.m_voBill.clone());

    this.m_iTotalListHeadNum = this.m_alListData.size();

    if (this.m_iLastSelListHeadRow < 0)
      this.m_iLastSelListHeadRow = 0;
  }

  public void afterHslEdit(int iRow)
  {
    if ((getBillCardPanel().getBodyValueAt(iRow, "hsl") == null) || (getBillCardPanel().getBodyValueAt(iRow, "hsl").toString().trim().length() == 0))
    {
      getBillCardPanel().setBodyValueAt(this.ZERO, iRow, "hsl");
    }
    UFDouble hsl = (UFDouble)getBillCardPanel().getBodyValueAt(iRow, "hsl");
    this.m_voBill.setItemValue(iRow, "hsl", hsl);

    if (this.m_voBill.getItemInv(iRow).getCinventoryid() != null)
      calculateByHsl(iRow, "dshldtransnum", "nshldtransastnum", 1);
  }

  protected void afterBsorEdit(String[] bsor, String[] dept)
  {
    if ((bsor == null) || (dept == null))
      return;
    UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem(bsor[0]).getComponent();
    String sName = ref.getRefName();
    String sPK = ref.getRefPK();

    String sDeptPK = null;
    String sDeptName = null;
    if ((sPK != null) && (sPK.trim().length() > 0)) {
      try {
        Object o = CacheTool.getCellValue("bd_psndoc", "pk_psndoc", "pk_deptdoc", sPK);
        if (o != null)
          sDeptPK = (String)((Object[])(Object[])o)[0];
      } catch (Exception ex) {
        SCMEnv.error(ex);
      }
      BillItem itDpt = getBillCardPanel().getHeadItem(dept[0]);
      if (itDpt != null) {
        ((UIRefPane)itDpt.getComponent()).setPK(sDeptPK);

        sDeptName = ((UIRefPane)itDpt.getComponent()).getRefName();
      }

    }

    if (this.m_voBill != null) {
      this.m_voBill.setHeaderValue(bsor[1], sName);
      this.m_voBill.setHeaderValue(dept[1], sDeptName);
    }
  }

  public void afterInvMutiEdit(BillEditEvent e)
  {
    try
    {
      long ITimeAll = System.currentTimeMillis();

      int row = e.getRow();

      String sItemKey = e.getKey();

      UIRefPane invRef = (UIRefPane)getBillCardPanel().getBodyItem("cinventorycode").getComponent();

      String[] refPks = invRef.getRefPKs();

      if ((refPks == null) || (refPks.length == 0)) {
        clearRowData(row);
        return;
      }
      invRef.setPK(null);

      String sWhID = null;
      String sCalID = null;
      if (getBillCardPanel().getHeadItem(this.m_sMainWhItemKey) != null) {
        sWhID = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey).getValue();
      }
      long ITime = System.currentTimeMillis();

      if (isQuryPlanprice())
      {
        if ((sCalID == null) && (sWhID != null))
        {
          sCalID = (String)((Object[])(Object[])CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "pk_calbody", sWhID))[0];
        }

      }

      SCMEnv.showTime(ITime, "存货解析参数设置:");

      ITime = System.currentTimeMillis();

      InvVO[] invVOs = null;
      if (isQuryPlanprice())
        invVOs = getInvoInfoBYFormula().getInvParseWithPlanPrice(refPks, sWhID, sCalID, true, true);
      else {
        invVOs = getInvoInfoBYFormula().getBillQuryInvVOs(refPks, true, true);
      }
      SCMEnv.showTime(ITime, "存货解析:");

      ITime = System.currentTimeMillis();

      boolean isLastRow = false;

      if ((refPks != null) && (refPks.length > 1)) {
        if (e.getRow() == getBillCardPanel().getRowCount() - 1) {
          isLastRow = true;
        }
        for (int i = refPks.length - 1; i >= 0; i--) {
          if (i < refPks.length - 1) {
            getBillCardPanel().insertLine();
          }
          else if (getBillCardPanel().getBillModel().getRowState(row) == 0) {
            getBillCardPanel().getBillModel().setRowState(row, 2);
          }
        }

        if (isLastRow)
        {
          BillRowNo.addLineRowNos(getBillCardPanel(), this.m_sBillTypeCode, "crowno", refPks.length);
        }
        else
        {
          BillRowNo.insertLineRowNos(getBillCardPanel(), this.m_sBillTypeCode, "crowno", e.getRow() + refPks.length - 1, refPks.length - 1);
        }

      }

      ITime = System.currentTimeMillis();

      setBodyInVO(invVOs, row);

      setTailValue(row);
      SCMEnv.showTime(ITime, "设置界面数据:");

      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000155"));

      SCMEnv.showTime(ITime, "存货界面设置:");

      SCMEnv.showTime(ITimeAll, "存货参照多选:");
    } catch (Exception e1) {
      showErrorMessage(e1.getMessage());
    }
  }

  public void afterProducedateEdit(BillEditEvent e)
  {
    int iSelrow = e.getRow();
    if ((null != getBillCardPanel().getBodyValueAt(iSelrow, e.getKey())) && (getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()).toString().trim().length() != 0))
    {
      int days = this.m_voBill.getItemInv(iSelrow).getQualityDay() == null ? 0 : this.m_voBill.getItemInv(iSelrow).getQualityDay().intValue();

      UFDate productDate = new UFDate(getBillCardPanel().getBodyValueAt(iSelrow, e.getKey()).toString());

      UFDate validateDate = productDate.getDateAfter(days);
      getBillCardPanel().setBodyValueAt(validateDate, iSelrow, "dvalidate");
    }
    else {
      getBillCardPanel().setBodyValueAt(null, iSelrow, "dvalidate");
    }
    this.m_voBill.setItemValue(iSelrow, "dvalidate", getBillCardPanel().getBodyValueAt(iSelrow, "dvalidate"));

    this.m_voBill.setItemValue(iSelrow, "scrq", getBillCardPanel().getBodyValueAt(iSelrow, "scrq"));
  }

  public boolean beforeEdit(BillEditEvent e)
  {
    return true;
  }

  protected void calculateByHsl(int iRow, String sMainNum, String sAstNum, int iWhichChanged)
  {
    Object temphsl = getBillCardPanel().getBodyValueAt(iRow, "hsl");
    if ((temphsl == null) || (temphsl.toString().trim().length() == 0))
    {
      temphsl = this.ZERO;
    }
    else {
      UFDouble hsl = (UFDouble)temphsl;
      if (hsl.doubleValue() <= 0.0D)
      {
        if ((this.m_voBill.getItemValue(iRow, "isAstUOMmgt") != null) && (this.m_voBill.getItemValue(iRow, "isAstUOMmgt").toString().trim().length() != 0))
        {
          if ((null != this.m_voBill.getItemValue(iRow, "fbillrowflag")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "fbillrowflag").toString()).intValue() == 1))
          {
            getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
            this.m_voBill.setItemValue(iRow, sAstNum, null);
            this.m_voBill.setItemValue(iRow, sMainNum, getBillCardPanel().getBodyValueAt(iRow, sMainNum));

            getBillCardPanel().setBodyValueAt(null, iRow, "hsl");
            this.m_voBill.setItemValue(iRow, "hsl", null);
          }
          else {
            getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
            this.m_voBill.setItemValue(iRow, sMainNum, null);
            this.m_voBill.setItemValue(iRow, sAstNum, getBillCardPanel().getBodyValueAt(iRow, sAstNum));

            getBillCardPanel().setBodyValueAt(null, iRow, "hsl");
            this.m_voBill.setItemValue(iRow, "hsl", null);
          }
        }
        else {
          this.m_voBill.setItemValue(iRow, sMainNum, getBillCardPanel().getBodyValueAt(iRow, sMainNum));

          getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
          getBillCardPanel().setBodyValueAt(null, iRow, "hsl");
          this.m_voBill.setItemValue(iRow, sAstNum, null);
          this.m_voBill.setItemValue(iRow, "hsl", null);
        }

      }
      else
      {
        UFDouble ufdMainNum = null;
        UFDouble ufdAstNum = null;
        if ((null != getBillCardPanel().getBodyValueAt(iRow, sMainNum)) && (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length() != 0))
        {
          ufdMainNum = (UFDouble)getBillCardPanel().getBodyValueAt(iRow, sMainNum);
        }if ((null != getBillCardPanel().getBodyValueAt(iRow, sAstNum)) && (getBillCardPanel().getBodyValueAt(iRow, sAstNum).toString().trim().length() != 0))
        {
          ufdAstNum = (UFDouble)getBillCardPanel().getBodyValueAt(iRow, sAstNum);
        }if (isFixFlag(iRow).booleanValue())
        {
          if (iWhichChanged == 1)
          {
            if ((null != this.m_voBill.getItemValue(iRow, "fbillrowflag")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "fbillrowflag").toString()).intValue() == 1))
            {
              if ((getBillCardPanel().getBodyValueAt(iRow, sMainNum) != null) && (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length() != 0) && (((UFDouble)getBillCardPanel().getBodyValueAt(iRow, sMainNum)).doubleValue() != 0.0D))
              {
                ufdAstNum = ufdMainNum.div(hsl);
                getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
                this.m_voBill.setItemValue(iRow, sAstNum, getBillCardPanel().getBodyValueAt(iRow, sAstNum));

                this.m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);
              } else if ((getBillCardPanel().getBodyValueAt(iRow, sMainNum) != null) && (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length() != 0) && (((UFDouble)getBillCardPanel().getBodyValueAt(iRow, sMainNum)).doubleValue() == 0.0D))
              {
                getBillCardPanel().setBodyValueAt(this.ZERO, iRow, sAstNum);
                this.m_voBill.setItemValue(iRow, sMainNum, this.ZERO);
                this.m_voBill.setItemValue(iRow, sAstNum, this.ZERO);
              } else {
                getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
                this.m_voBill.setItemValue(iRow, sMainNum, null);
                this.m_voBill.setItemValue(iRow, sAstNum, null);
              }

            }
            else if ((getBillCardPanel().getBodyValueAt(iRow, sAstNum) != null) && (getBillCardPanel().getBodyValueAt(iRow, sAstNum).toString().trim().length() != 0) && (((UFDouble)getBillCardPanel().getBodyValueAt(iRow, sAstNum)).doubleValue() != 0.0D))
            {
              ufdMainNum = ufdAstNum.multiply(hsl);
              getBillCardPanel().setBodyValueAt(ufdMainNum, iRow, sMainNum);

              this.m_voBill.setItemValue(iRow, sMainNum, getBillCardPanel().getBodyValueAt(iRow, sMainNum));

              this.m_voBill.setItemValue(iRow, sAstNum, ufdAstNum);
            } else if ((getBillCardPanel().getBodyValueAt(iRow, sAstNum) != null) && (getBillCardPanel().getBodyValueAt(iRow, sAstNum).toString().trim().length() != 0) && (((UFDouble)getBillCardPanel().getBodyValueAt(iRow, sAstNum)).doubleValue() == 0.0D))
            {
              getBillCardPanel().setBodyValueAt(this.ZERO, iRow, sMainNum);
              this.m_voBill.setItemValue(iRow, sMainNum, this.ZERO);
              this.m_voBill.setItemValue(iRow, sAstNum, this.ZERO);
            } else {
              getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
              this.m_voBill.setItemValue(iRow, sMainNum, null);
              this.m_voBill.setItemValue(iRow, sAstNum, null);
            }

          }
          else if ((getBillCardPanel().getBodyValueAt(iRow, sMainNum) != null) && (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length() != 0) && (((UFDouble)getBillCardPanel().getBodyValueAt(iRow, sMainNum)).doubleValue() != 0.0D))
          {
            ufdAstNum = ufdMainNum.div(hsl);
            getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
            this.m_voBill.setItemValue(iRow, sMainNum, ufdMainNum);

            this.m_voBill.setItemValue(iRow, sAstNum, getBillCardPanel().getBodyValueAt(iRow, sAstNum));
          }
          else if ((getBillCardPanel().getBodyValueAt(iRow, sMainNum) != null) && (getBillCardPanel().getBodyValueAt(iRow, sMainNum).toString().trim().length() != 0) && (((UFDouble)getBillCardPanel().getBodyValueAt(iRow, sMainNum)).doubleValue() == 0.0D))
          {
            getBillCardPanel().setBodyValueAt(this.ZERO, iRow, sAstNum);
            this.m_voBill.setItemValue(iRow, sMainNum, this.ZERO);
            this.m_voBill.setItemValue(iRow, sAstNum, this.ZERO);
          } else {
            getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
            this.m_voBill.setItemValue(iRow, sMainNum, null);
            this.m_voBill.setItemValue(iRow, sAstNum, null);
          }
        }
        else {
          if ((null != this.m_voBill.getItemValue(iRow, "fbillrowflag")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "fbillrowflag").toString()).intValue() == 1))
          {
            iWhichChanged = 1;
          }
          if (((null != ufdMainNum) && (ufdMainNum.doubleValue() != 0.0D)) || ((null != ufdAstNum) && (ufdAstNum.doubleValue() != 0.0D)))
          {
            if (iWhichChanged == 1)
            {
              if ((null != this.m_voBill.getItemValue(iRow, "fbillrowflag")) && (Integer.valueOf(this.m_voBill.getItemValue(iRow, "fbillrowflag").toString()).intValue() == 1))
              {
                if ((null == ufdMainNum) || (ufdMainNum.doubleValue() == 0.0D)) {
                  getBillCardPanel().setBodyValueAt(null, iRow, sAstNum);
                  getBillCardPanel().setBodyValueAt(null, iRow, sMainNum);
                } else {
                  ufdAstNum = ufdMainNum.div(hsl);
                  getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
                }

              }
              else if (ufdAstNum != null) {
                ufdMainNum = ufdAstNum.multiply(hsl);
                getBillCardPanel().setBodyValueAt(ufdMainNum, iRow, sMainNum);
              }
            }
            else if ((iWhichChanged == 0) && ((null == ufdAstNum) || (ufdAstNum.doubleValue() == 0.0D)))
            {
              if ((hsl != null) && (ufdMainNum != null)) {
                ufdAstNum = ufdMainNum.div(hsl);
                getBillCardPanel().setBodyValueAt(ufdAstNum, iRow, sAstNum);
              }

            }
            else if ((iWhichChanged == 0) && (null != ufdAstNum) && (ufdAstNum.doubleValue() != 0.0D))
            {
              hsl = ufdMainNum.div(ufdAstNum);
              getBillCardPanel().setBodyValueAt(hsl, iRow, "hsl");
              this.m_voBill.setItemValue(iRow, "hsl", getBillCardPanel().getBodyValueAt(iRow, "hsl"));
            }

          }

          this.m_voBill.setItemValue(iRow, sMainNum, getBillCardPanel().getBodyValueAt(iRow, sMainNum));

          this.m_voBill.setItemValue(iRow, sAstNum, getBillCardPanel().getBodyValueAt(iRow, sAstNum));
        }

      }

    }

    getBillCardPanel().getBillModel().execEditFormulaByKey(iRow, sMainNum);
  }

  protected BillData changeBillDataByUserDef(BillData oldBillData)
  {
    DefVO[] defs = null;

    defs = getDefHeadVO();
    if (defs != null)
    {
      oldBillData.updateItemByDef(defs, "vuserdef", true);
    }

    defs = getDefBodyVO();
    if (defs == null) {
      return oldBillData;
    }
    oldBillData.updateItemByDef(defs, "vuserdef", false);
    return oldBillData;
  }

  protected BillListData changeBillListDataByUserDef(BillListData oldBillData)
  {
    DefVO[] defs = null;

    defs = getDefHeadVO();
    if (defs != null) {
      oldBillData.updateItemByDef(defs, "vuserdef", true);
    }

    defs = getDefBodyVO();
    if (defs == null) {
      return oldBillData;
    }
    oldBillData.updateItemByDef(defs, "vuserdef", false);
    return oldBillData;
  }

  protected GeneralBillHeaderVO changeFromSpecialVOtoGeneralVOAboutHeader(GeneralBillVO gbvo, SpecialBillVO sbvo, int iInOutFlag)
  {
    String[] sHeaderItemKeyName = gbvo.getHeaderVO().getAttributeNames();
    for (int i = 0; i < sHeaderItemKeyName.length; i++) {
      if (sbvo.getHeaderValue(sHeaderItemKeyName[i]) != null) {
        gbvo.setHeaderValue(sHeaderItemKeyName[i], sbvo.getHeaderValue(sHeaderItemKeyName[i]));
      }

      if (sHeaderItemKeyName[i].trim().equals("cbilltypecode")) {
        if (iInOutFlag == -1)
          gbvo.setHeaderValue(sHeaderItemKeyName[i], BillTypeConst.m_otherOut);
        else
          gbvo.setHeaderValue(sHeaderItemKeyName[i], BillTypeConst.m_otherIn);
      } else if (sHeaderItemKeyName[i].trim().equals("cgeneralhid"))
        gbvo.setHeaderValue(sHeaderItemKeyName[i], "");
      else if (sHeaderItemKeyName[i].trim().equals("vbillcode"))
        gbvo.setHeaderValue(sHeaderItemKeyName[i], "");
      else if (sHeaderItemKeyName[i].trim().equals("coperatorid"))
        gbvo.setHeaderValue(sHeaderItemKeyName[i], this.m_sUserID);
      else if (sHeaderItemKeyName[i].trim().equals("coperatorname"))
        gbvo.setHeaderValue(sHeaderItemKeyName[i], this.m_sUserName);
      else if (sHeaderItemKeyName[i].trim().equals("dbilldate"))
        gbvo.setHeaderValue(sHeaderItemKeyName[i], this.m_sLogDate);
      else if (sHeaderItemKeyName[i].trim().equals("cauditorid"))
      {
        gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
      } else if (sHeaderItemKeyName[i].trim().equals("cauditorname"))
      {
        gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
      } else if (sHeaderItemKeyName[i].trim().equals("vadjuster"))
        gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
      else if (sHeaderItemKeyName[i].trim().equals("vadjustername"))
      {
        gbvo.setHeaderValue(sHeaderItemKeyName[i], null);
      }
      if (iInOutFlag == -1) {
        if (sHeaderItemKeyName[i].trim().equals("pk_calbody_out")) {
          gbvo.setHeaderValue("pk_calbody", sbvo.getHeaderValue("pk_calbody_out"));
        }
      }
      else if (sHeaderItemKeyName[i].trim().equals("pk_calbody_in")) {
        gbvo.setHeaderValue("pk_calbody", sbvo.getHeaderValue("pk_calbody_in"));
      }

    }

    gbvo.getHeaderVO().setCoperatorid(this.m_sUserID);
    gbvo.getHeaderVO().setCoperatoridnow(this.m_sUserID);
    gbvo.getHeaderVO().setPrimaryKey(null);

    gbvo.getHeaderVO().setTs(null);

    gbvo.getParentVO().setStatus(2);
    return (GeneralBillHeaderVO)gbvo.getParentVO();
  }

  protected GeneralBillItemVO changeFromSpecialVOtoGeneralVOAboutItem(GeneralBillVO gbvo, SpecialBillVO sbvo, int iInOutFlag, int j)
  {
    BillTypeConst billTypeConst = new BillTypeConst();
    Hashtable htbBillTypeName = billTypeConst.getHtbBillTypeName();
    String sBilltypecode = null;
    String sBilltypeName = null;
    sBilltypecode = (String)sbvo.getHeaderValue("cbilltypecode");
    if ((sBilltypecode != null) && (htbBillTypeName != null) && (htbBillTypeName.containsKey(sBilltypecode))) {
      sBilltypeName = (String)htbBillTypeName.get(sBilltypecode);
    }

    String[] sBodyItemKeyName = gbvo.getChildrenVO()[0].getAttributeNames();
    for (int i = 0; i < sBodyItemKeyName.length; i++) {
      if (sbvo.getItemValue(j, sBodyItemKeyName[i]) != null) {
        gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, sBodyItemKeyName[i]));
      }
      if (sBodyItemKeyName[i].equalsIgnoreCase("vnotebody")) {
        gbvo.setItemValue(j, "vnotebody", sbvo.getItemValue(j, "vnote"));
      }
      if (sBodyItemKeyName[i].equalsIgnoreCase("cinvbasid")) {
        gbvo.setItemValue(j, "cinvbasid", sbvo.getItemValue(j, "cinvmanid"));
      }

      UFDouble ufdTotal = sbvo.getItemValue(j, "dshldtransnum") == null ? this.ZERO : (UFDouble)sbvo.getItemValue(j, "dshldtransnum");

      UFDouble ufdAlreadyIn = sbvo.getItemValue(j, "nadjustnum") == null ? this.ZERO : (UFDouble)sbvo.getItemValue(j, "nadjustnum");

      UFDouble ufdAlreadyOut = sbvo.getItemValue(j, "nchecknum") == null ? this.ZERO : (UFDouble)sbvo.getItemValue(j, "nchecknum");

      UFDouble ufdHsl = (sbvo.getItemValue(j, "hsl") == null) || (sbvo.getItemValue(j, "hsl").toString().trim().length() == 0) ? this.ZERO : (UFDouble)sbvo.getItemValue(j, "hsl");

      if (sBodyItemKeyName[i].trim().equals("nshouldinnum"))
      {
        if (iInOutFlag != -1);
      }
      else if (sBodyItemKeyName[i].trim().equals("nneedinassistnum")) {
        if (iInOutFlag != -1)
        {
          if ((sbvo.getItemValue(j, "nshldtransastnum") == null) || (sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() == 0) || (ufdHsl.doubleValue() == 0.0D));
        }

      }
      else if (sBodyItemKeyName[i].trim().equals("ninnum"))
      {
        if (iInOutFlag != -1);
      }
      else if (sBodyItemKeyName[i].trim().equals("ninassistnum")) {
        if (iInOutFlag != -1)
        {
          if ((sbvo.getItemValue(j, "nshldtransastnum") == null) || (sbvo.getItemValue(j, "nshldtransastnum").toString().trim().length() == 0) || (ufdHsl.doubleValue() == 0.0D));
        }

      }
      else if (sBodyItemKeyName[i].trim().equals("nshouldoutnum"))
      {
        if (iInOutFlag != -1);
      }
      else if (sBodyItemKeyName[i].trim().equals("nshouldoutassistnum"))
      {
        if (iInOutFlag != -1);
      }
      else if (sBodyItemKeyName[i].trim().equals("noutnum"))
      {
        if (iInOutFlag != -1);
      }
      else if (sBodyItemKeyName[i].trim().equals("noutassistnum"))
      {
        if (iInOutFlag != -1);
      }
      else if (sBodyItemKeyName[i].trim().equals("nmny"))
        gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "je"));
      else if (sBodyItemKeyName[i].trim().equals("nplannedprice"))
        gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "jhdj"));
      else if (sBodyItemKeyName[i].trim().equals("nplannedmny"))
        gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "jhje"));
      else if (sBodyItemKeyName[i].trim().equals("csourcetype"))
        gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getHeaderValue("cbilltypecode"));
      else if (sBodyItemKeyName[i].trim().equals("vsourcebillcode")) {
        gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getHeaderValue("vbillcode"));
      }
      else if (sBodyItemKeyName[i].trim().equals("vsourcerowno")) {
        gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "crowno"));
      }
      else if (sBodyItemKeyName[i].trim().equals("csourcetypename")) {
        if (sBilltypeName != null) {
          gbvo.setItemValue(j, sBodyItemKeyName[i], sBilltypeName);
        }
      }
      else if (sBodyItemKeyName[i].trim().equals("csourcebillhid")) {
        gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getHeaderValue("cspecialhid"));
      }
      else if (sBodyItemKeyName[i].trim().equals("csourcebillbid")) {
        gbvo.setItemValue(j, sBodyItemKeyName[i], sbvo.getItemValue(j, "cspecialbid"));
      }
      else if (sBodyItemKeyName[i].trim().equals("dbizdate")) {
        gbvo.setItemValue(j, sBodyItemKeyName[i], this.m_sLogDate);
      }

      gbvo.setItemValue(j, "isprimarybarcode", sbvo.getItemValue(j, "isprimarybarcode"));

      gbvo.setItemValue(j, "issecondarybarcode", sbvo.getItemValue(j, "issecondarybarcode"));
    }
    gbvo.setItemValue(j, "cgeneralbid", null);
    gbvo.setItemValue(j, "cgeneralhid", null);

    gbvo.setItemValue(j, "ts", null);

    gbvo.getChildrenVO()[j].setStatus(2);
    return gbvo.getItemVOs()[j];
  }

  public void clearOrientColor()
  {
    if (this.m_isLocated) {
      if (this.m_iMode == 4)
        nc.ui.scm.pub.report.OrientDialog.clearOrientColor(getBillListPanel().getBodyTable());
      else
        nc.ui.scm.pub.report.OrientDialog.clearOrientColor(getBillCardPanel().getBillTable());
      this.m_isLocated = false;
    }
  }

  protected void clearUi()
  {
    try
    {
      SpecialBillVO voNullBill = new SpecialBillVO();
      voNullBill.setParentVO(new SpecialBillHeaderVO());
      voNullBill.setChildrenVO(new SpecialBillItemVO[] { new SpecialBillItemVO() });
      getBillCardPanel().setBillValueVO(voNullBill);
      getBillCardPanel().getBillModel().clearBodyData();

      getBillListPanel().getHeadBillModel().clearBodyData();
      getBillListPanel().getBodyBillModel().clearBodyData();
    }
    catch (Exception e)
    {
    }
  }

  protected void combinedItemVOsByNotNullValue(AggregatedValueObject vo, CircularlyAccessibleValueObject[] itemVOs, boolean bIsGeneralBillVO)
  {
    if ((vo.getChildrenVO().length != itemVOs.length) || (itemVOs.length == 0)) {
      return;
    }

    CircularlyAccessibleValueObject[] resultItemVOs = vo.getChildrenVO();
    String[] sNames = itemVOs[0].getAttributeNames();

    for (int i = 0; i < itemVOs.length; i++) {
      String sNextPK = "";
      if (bIsGeneralBillVO)
        sNextPK = ((GeneralBillItemVO)itemVOs[i]).getCgeneralbid();
      else {
        sNextPK = ((SpecialBillItemVO)itemVOs[i]).getCspecialbid();
      }
      for (int k = 0; k < vo.getChildrenVO().length; k++) {
        CircularlyAccessibleValueObject cvos = resultItemVOs[k];
        String sFirstPK = "";
        if (bIsGeneralBillVO)
          sFirstPK = ((GeneralBillItemVO)cvos).getCgeneralbid();
        else {
          sFirstPK = ((SpecialBillItemVO)cvos).getCspecialbid();
        }
        if (sFirstPK.equals(sNextPK)) {
          for (int j = 0; j < sNames.length; j++) {
            Object oValue = itemVOs[i].getAttributeValue(sNames[j]);
            if ((null != oValue) && (oValue.toString().trim().length() != 0)) {
              cvos.setAttributeValue(sNames[j], oValue);
            }
          }
          resultItemVOs[k] = ((CircularlyAccessibleValueObject)cvos.clone());
          break;
        }
      }
    }
    vo.setChildrenVO(resultItemVOs);
  }

  protected void dealNoData()
  {
    this.m_iTotalListHeadNum = 0;
    this.m_iFirstSelListHeadRow = -1;
    this.m_iLastSelListHeadRow = -1;
    this.m_iLastSelCardBodyRow = -1;
    clearUi();
    showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000013"));
  }

  protected void executeLoadFormularAfterQuery(ArrayList alVOs)
  {
    if ((null == alVOs) || (alVOs.size() == 0))
      return;
    for (int i = 0; i < alVOs.size(); i++)
    {
      getBillListPanel().setBodyValueVO(((AggregatedValueObject)alVOs.get(i)).getChildrenVO());
      try
      {
        getBillListPanel().getBodyBillModel().getFormulaParse().setNullAsZero(false);
        getBillListPanel().getBodyBillModel().execLoadFormula();
      } catch (Exception eee) {
        SCMEnv.error(eee);
      }

      if (((AggregatedValueObject)alVOs.get(i) instanceof SpecialBillVO)) {
        SpecialBillItemVO[] shvoSpecialHVOnow = new SpecialBillItemVO[getBillListPanel().getBodyTable().getRowCount()];

        for (int j = 0; j < shvoSpecialHVOnow.length; j++) {
          shvoSpecialHVOnow[j] = new SpecialBillItemVO();
        }
        getBillListPanel().getBodyBillModel().getBodyValueVOs(shvoSpecialHVOnow);

        combinedItemVOsByNotNullValue((SpecialBillVO)alVOs.get(i), shvoSpecialHVOnow, false);
      }
      else
      {
        GeneralBillItemVO[] shvoSpecialHVOnow = new GeneralBillItemVO[getBillListPanel().getBodyTable().getRowCount()];

        for (int j = 0; j < shvoSpecialHVOnow.length; j++) {
          shvoSpecialHVOnow[j] = new GeneralBillItemVO();
        }
        getBillListPanel().getBodyBillModel().getBodyValueVOs(shvoSpecialHVOnow);

        combinedItemVOsByNotNullValue((GeneralBillVO)alVOs.get(i), shvoSpecialHVOnow, true);
      }
    }
  }

  public void firstSetColEditable()
  {
    if ((getBillCardPanel().getBillTable().getRowCount() > 0) && (getBillCardPanel().getBillTable().getColumnCount() > 0))
    {
      getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
      getBillCardPanel().getBillTable().setColumnSelectionInterval(1, 1);
    }
  }

  public void freshTs(ArrayList alTs)
    throws Exception
  {
    if ((alTs == null) || (alTs.size() == 0))
      throw new Exception(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000027"));
    setTs(this.m_voBill, alTs);
    setTs(this.m_iLastSelListHeadRow, alTs);
    setUiTs(alTs);
  }

  protected ClientUIInAndOut getAuditDlg()
  {
    if (this.m_dlgInOut == null) {
      try
      {
        this.m_dlgInOut = new ClientUIInAndOut(this, "");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }

    }

    return this.m_dlgInOut;
  }

  protected SpecialBillVO getBillVO()
  {
    SpecialBillVO voNowBill = new SpecialBillVO(getBillCardPanel().getRowCount());

    getBillCardPanel().getBillValueVO(voNowBill);
    if ((voNowBill == null) || (voNowBill.getParentVO() == null) || (voNowBill.getChildrenVO() == null))
    {
      SCMEnv.out("表中无数据!");
      return null;
    }
    return voNowBill;
  }

  protected SpecialBillItemVO[] getChangedItemVOs()
  {
    BillModel bmTemp = getBillCardPanel().getBillModel();
    if (bmTemp == null) {
      SCMEnv.out("bm null ERROR!");
      return null;
    }
    Vector vBodyData = bmTemp.getDataVector();
    if ((vBodyData == null) || (vBodyData.size() == 0)) {
      SCMEnv.out("bd null ERROR!");
      return null;
    }
    SpecialBillItemVO[] voaItem = this.m_voBill.getItemVOs();

    int rowCount = vBodyData.size();
    int length = 0;
    Vector vDeleteRow = bmTemp.getDeleteRow();
    if (vDeleteRow != null)
      length = rowCount + vDeleteRow.size();
    else {
      length = rowCount;
    }
    SpecialBillItemVO[] voaBody = new SpecialBillItemVO[length];

    int iRowStatus = 1;

    for (int i = 0; i < vBodyData.size(); i++) {
      voaBody[i] = new SpecialBillItemVO();
      iRowStatus = bmTemp.getRowState(i);
      if ((1 == iRowStatus) || (2 == iRowStatus))
      {
        for (int j = 0; j < bmTemp.getBodyItems().length; j++) {
          BillItem item = bmTemp.getBodyItems()[j];
          Object aValue = bmTemp.getValueAt(i, item.getKey());
          aValue = item.converType(aValue);
          voaBody[i].setAttributeValue(item.getKey(), aValue);
        }
      }
      else if (0 == iRowStatus) {
        for (int j = 0; j < bmTemp.getBodyItems().length; j++) {
          BillItem item = bmTemp.getBodyItems()[j];

          Object aValue = bmTemp.getValueAt(i, item.getKey());
          aValue = item.converType(aValue);
          voaBody[i].setAttributeValue(item.getKey(), aValue);
        }

      }

      switch (iRowStatus) {
      case 1:
        voaBody[i].setStatus(2);
        break;
      case 2:
        voaBody[i].setStatus(1);
        break;
      case 0:
        voaBody[i].setStatus(0);
      }

      voaBody[i].setFreeItemVO(voaItem[i].getFreeItemVO());
    }

    if (vDeleteRow != null) {
      for (int i = 0; i < vDeleteRow.size(); i++)
      {
        voaBody[(i + rowCount)] = new SpecialBillItemVO();
        Vector rowVector = (Vector)vDeleteRow.elementAt(i);
        for (int j = 0; j < bmTemp.getBodyItems().length; j++) {
          BillItem item = bmTemp.getBodyItems()[j];
          int col = bmTemp.getBodyColByKey(item.getKey());
          Object aValue = rowVector.elementAt(col);
          if ((aValue != null) && 
            (item.getDataType() == 6)) {
            int k = 0;
            while (k < ((UIComboBox)item.getComponent()).getItemCount())
            {
              if (aValue.toString().equals(((UIComboBox)item.getComponent()).getItemAt(k)))
              {
                aValue = new Integer(k);
                break;
              }
              k++;
            }

          }

          voaBody[(i + rowCount)].setAttributeValue(item.getKey(), aValue);
        }
        voaBody[(i + rowCount)].setStatus(3);
      }
    }
    return voaBody;
  }

  protected QueryConditionDlgForBill getConditionDlg()
  {
    if (this.ivjQueryConditionDlg == null) {
      this.ivjQueryConditionDlg = new QueryConditionDlgForBill(this);

      this.ivjQueryConditionDlg.setTempletID(this.m_sCorpID, this.m_sPNodeCode, this.m_sUserID, null);

      ArrayList alCorpIDs = new ArrayList();
      try
      {
        alCorpIDs = (ArrayList)SpecialBillHelper.queryInfo(new Integer(10), this.m_sUserID);
      }
      catch (Exception e) {
        SCMEnv.error(e);
      }

      this.ivjQueryConditionDlg.setInitDate("dbilldate", this.m_sLogDate);

      this.ivjQueryConditionDlg.setShowPrintStatusPanel(true);

      this.ivjQueryConditionDlg.initQueryDlgRef();
      this.ivjQueryConditionDlg.initCorpRef("pk_corp", this.m_sCorpID, alCorpIDs);

      Object[][] arycombox = new Object[2][2];
      arycombox[0][0] = "Y";
      arycombox[0][1] = "Y";
      arycombox[1][0] = "N";
      arycombox[1][1] = "N";

      this.ivjQueryConditionDlg.setCombox("body.bqrybalrec", arycombox);

      this.ivjQueryConditionDlg.setCorpRefs("head.pk_corp", GenMethod.getDataPowerFieldFromDlgNotByProp(this.ivjQueryConditionDlg));
    }

    return this.ivjQueryConditionDlg;
  }

  protected PrintDataInterface getDataSource()
  {
    if (null == this.m_dataSource) {
      this.m_dataSource = new PrintDataInterface();
      BillData bd = getBillCardPanel().getBillData();
      this.m_dataSource.setBillData(bd);
      this.m_dataSource.setModuleName(this.m_sPNodeCode);
      this.m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
    }
    return this.m_dataSource;
  }

  protected PrintDataInterface getNewDataSource()
  {
    PrintDataInterface ds = new PrintDataInterface();
    BillData bd = getBillCardPanel().getBillData();
    ds.setBillData(bd);
    ds.setModuleName(this.m_sPNodeCode);
    ds.setTotalLinesInOnePage(getPrintEntry().getBreakPos());

    return ds;
  }

  public DefVO[] getDefBodyVO()
  {
    try
    {
      if (this.m_defBody == null)
        this.m_defBody = DefSetTool.getDefBody(this.m_sCorpID, this.m_sBillTypeCode);
    }
    catch (Exception e) {
      SCMEnv.error(e);
    }

    return this.m_defBody;
  }

  public DefVO[] getDefHeadVO()
  {
    try
    {
      if (this.m_defHead == null)
        this.m_defHead = DefSetTool.getDefHead(this.m_sCorpID, this.m_sBillTypeCode);
    }
    catch (Exception e) {
      SCMEnv.error(e);
    }

    return this.m_defHead;
  }

  protected String getExtenWhere()
  {
    StringBuffer sbWhere = new StringBuffer();

    ConditionVO[] voConds = getConditionDlg().getConditionVO();

    boolean isHaveCorp = false;

    if ((voConds != null) && (voConds.length > 0)) {
      int ilen = voConds.length;
      String sFieldCode = null;

      for (int i = 0; i < ilen; i++) {
        sFieldCode = voConds[i].getFieldCode();
        if (("pk_corp".equals(sFieldCode)) || ("head.pk_corp".equals(sFieldCode))) {
          isHaveCorp = true;

          voConds[i].setFieldCode("head.pk_corp");
        }
        sbWhere.append(voConds[i].getSQLStr());
      }

      if (!isHaveCorp) {
        int iAndBg = sbWhere.toString().indexOf("and");
        if (iAndBg > 0) {
          sbWhere.insert(iAndBg + 3, "(");
          sbWhere.append(")");
        }
        sbWhere.append(" AND head.pk_corp ='");
        sbWhere.append(this.m_sCorpID);
        sbWhere.append("'");
      }
    }
    else {
      sbWhere.append(" and head.pk_corp='");
      sbWhere.append(this.m_sCorpID);
      sbWhere.append("'");
    }

    return sbWhere.toString();
  }

  protected String getExtenWhere(ConditionVO[] voConds)
  {
    StringBuffer sbWhere = new StringBuffer();

    boolean isHaveCorp = false;

    if ((voConds != null) && (voConds.length > 0)) {
      int ilen = voConds.length;
      String sFieldCode = null;
      for (int i = 0; i < ilen; i++) {
        sFieldCode = voConds[i].getFieldCode();
        if (("pk_corp".equals(sFieldCode)) || ("head.pk_corp".equals(sFieldCode))) {
          isHaveCorp = true;

          voConds[i].setFieldCode("head.pk_corp");
        }
        sbWhere.append(voConds[i].getSQLStr());
      }

      if (!isHaveCorp) {
        int iAndBg = sbWhere.toString().indexOf("and");
        if (iAndBg > 0) {
          sbWhere.insert(iAndBg + 3, "(");
          sbWhere.append(")");
        }
        sbWhere.append(" AND head.pk_corp ='");

        sbWhere.append(this.m_sCorpID);
        sbWhere.append("'");
      }
    }
    else {
      sbWhere.append(" and head.pk_corp='");
      sbWhere.append(this.m_sCorpID);
      sbWhere.append("'");
    }

    return sbWhere.toString();
  }

  public BillFormulaContainer getFormulaBillContainer()
  {
    if (this.m_billFormulaContain == null)
    {
      this.m_billFormulaContain = new BillFormulaContainer(getBillListPanel());
    }
    return this.m_billFormulaContain;
  }

  protected ArrayList getFormulaItemBody()
  {
    if (this.m_alFormulBodyItem == null)
    {
      this.m_alFormulBodyItem = new ArrayList();

      String[] aryItemField31 = { "measname", "castunitname", "castunitid" };
      this.m_alFormulBodyItem.add(aryItemField31);

      String[] aryItemField9 = { "billtypename", "csourcetypename", "csourcetype" };
      this.m_alFormulBodyItem.add(aryItemField9);

      String[] aryItemField10 = { "billtypename", "cfirsttypename", "cfirsttype" };
      this.m_alFormulBodyItem.add(aryItemField10);

      String[] aryItemField15 = { "storname", "cwarehousename", "cwarehouseid" };
      this.m_alFormulBodyItem.add(aryItemField15);
    }

    return this.m_alFormulBodyItem;
  }

  protected ArrayList getFormulaItemHeader()
  {
    if (this.m_alFormulHeadItem == null) {
      this.m_alFormulHeadItem = new ArrayList();

      String[] aryItemField3 = { "psnname", "cinbsrname", "cinbsrid" };
      this.m_alFormulHeadItem.add(aryItemField3);

      String[] aryItemField4 = { "psnname", "coutbsorname", "coutbsor" };
      this.m_alFormulHeadItem.add(aryItemField4);

      String[] aryItemField19 = { "deptname", "cindeptname", "cindeptid" };
      this.m_alFormulHeadItem.add(aryItemField19);

      String[] aryItemField29 = { "deptname", "coutdeptname", "coutdeptid" };
      this.m_alFormulHeadItem.add(aryItemField29);

      String[] aryItemField15 = { "storname", "cinwarehousename", "cinwarehouseid" };
      this.m_alFormulHeadItem.add(aryItemField15);

      String[] aryItemField25 = { "storname", "coutwarehousename", "coutwarehouseid" };
      this.m_alFormulHeadItem.add(aryItemField25);

      String[] aryItemField26 = { "csflag", "isLocatorMgtOut", "coutwarehouseid" };
      this.m_alFormulHeadItem.add(aryItemField26);

      String[] aryItemField27 = { "gubflag", "iswastewhout", "coutwarehouseid" };
      this.m_alFormulHeadItem.add(aryItemField27);

      String[] aryItemField2 = { "user_name", "vadjustername", "vadjuster" };
      this.m_alFormulHeadItem.add(aryItemField2);

      String[] aryItemField12 = { "user_name", "cauditorname", "cauditorid" };
      this.m_alFormulHeadItem.add(aryItemField12);

      String[] aryItemField1 = { "user_name", "coperatorname", "coperatorid" };
      this.m_alFormulHeadItem.add(aryItemField1);

      String[] aryItemField111 = { "user_name", "clastmodiname", "clastmodiid" };
      this.m_alFormulHeadItem.add(aryItemField111);
    }

    return this.m_alFormulHeadItem;
  }

  public InvoInfoBYFormula getInvoInfoBYFormula()
  {
    if (this.m_InvoInfoBYFormula == null)
      this.m_InvoInfoBYFormula = new InvoInfoBYFormula(getCorpPrimaryKey());
    return this.m_InvoInfoBYFormula;
  }

  protected SpecialBillHeaderVO[] getListHeaderVOs()
  {
    SpecialBillHeaderVO[] voaHeader = null;
    if ((this.m_alListData != null) && (this.m_alListData.size() > 0)) {
      voaHeader = new SpecialBillHeaderVO[this.m_alListData.size()];
      for (int i = 0; i < this.m_alListData.size(); i++)
        voaHeader[i] = ((SpecialBillVO)this.m_alListData.get(i)).getHeaderVO();
    }
    return voaHeader;
  }

  public nc.ui.ic.pub.orient.OrientDialog getOrientDlg()
  {
    if (this.m_dlgOrient == null) {
      this.m_dlgOrient = new nc.ui.ic.pub.orient.OrientDialog(this);
    }
    return this.m_dlgOrient;
  }

  protected PrintEntry getPrintEntry()
  {
    if (null == this.m_print) {
      this.m_print = new PrintEntry(this, null);
      this.m_print.setTemplateID(this.m_sCorpID, this.m_sPNodeCode, this.m_sUserID, null);
    }
    return this.m_print;
  }

  protected ArrayList getSelectedBills()
  {
    ArrayList albill = new ArrayList();
    int iSelListHeadRowCount = getBillListPanel().getHeadTable().getSelectedRowCount();
    if (iSelListHeadRowCount <= 0)
    {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000009"));
      return null;
    }
    int[] arySelListHeadRows = new int[iSelListHeadRowCount];
    arySelListHeadRows = getBillListPanel().getHeadTable().getSelectedRows();

    SpecialBillVO[] voaBill = new SpecialBillVO[iSelListHeadRowCount];
    Vector vHeadPK = new Vector();
    Vector vIndex = new Vector();
    for (int i = 0; i < iSelListHeadRowCount; i++)
    {
      if ((this.m_alListData == null) || (this.m_alListData.size() <= arySelListHeadRows[i])) {
        continue;
      }
      voaBill[i] = ((SpecialBillVO)this.m_alListData.get(arySelListHeadRows[i]));
      if ((voaBill[i].getChildrenVO() != null) && (voaBill[i].getChildrenVO().length != 0)) {
        continue;
      }
      vHeadPK.addElement(((SpecialBillHeaderVO)voaBill[i].getParentVO()).getCspecialhid());
      vIndex.addElement(new Integer(arySelListHeadRows[i]));
    }

    if (vIndex.size() > 0)
    {
      String[] saPK = new String[vHeadPK.size()];
      int[] indexs = new int[vIndex.size()];
      vHeadPK.copyInto(saPK);

      for (int i = 0; i < vIndex.size(); i++)
      {
        indexs[i] = ((Integer)vIndex.get(i)).intValue();
      }
      qryItems(indexs, saPK);
    }
    for (int i = 0; i < arySelListHeadRows.length; i++)
    {
      if ((this.m_alListData == null) || (this.m_alListData.size() <= arySelListHeadRows[i])) {
        continue;
      }
      albill.add((SpecialBillVO)this.m_alListData.get(arySelListHeadRows[i]));
    }

    return albill;
  }

  protected SpecialBillVO getUpdatedBillVO()
  {
    SpecialBillVO voNowBill = new SpecialBillVO();

    voNowBill = (SpecialBillVO)getBillCardPanel().getBillValueChangeVO(SpecialBillVO.class.getName(), SpecialBillHeaderVO.class.getName(), SpecialBillItemVO.class.getName());

    if ((null == voNowBill) || (voNowBill.getParentVO() == null) || (voNowBill.getChildrenVO() == null))
    {
      SCMEnv.out("表中无数据!");
      return null;
    }
    return voNowBill;
  }

  protected WhVO getWhInfoByID(String sWhID)
  {
    WhVO voWh = null;

    if ((sWhID != null) && (this.m_htWh != null) && (this.m_htWh.containsKey(sWhID.trim())))
      voWh = (WhVO)this.m_htWh.get(sWhID.trim());
    else {
      try
      {
        voWh = (WhVO)SpecialBillHelper.queryInfo(new Integer(1), sWhID);

        this.m_htWh.put(sWhID.trim(), voWh);
      } catch (Exception e2) {
        SCMEnv.error(e2);
      }
    }
    return voWh;
  }

  protected WhVO getWhInfoByRef(String sItemKey)
  {
    WhVO voWh = null;
    try {
      String sID = ((UIRefPane)getBillCardPanel().getHeadItem(sItemKey).getComponent()).getRefPK();

      voWh = getWhInfoByID(sID);
    }
    catch (Exception e2) {
      SCMEnv.error(e2);
    }
    return voWh;
  }

  protected void handleException(Exception exception)
  {
    SCMEnv.out("--------- 未捕捉到的异常 ---------");
    SCMEnv.error(exception);
  }

  protected void initialize(String pk_corp, String sOperatorid, String sOperatorname, String sBiztypeid, String sGroupid, String sLogDate)
  {
    try
    {
      initVariable();

      initButtons();
      setButtons(this.m_aryButtonGroup);
      this.m_sUserID = sOperatorid;

      this.m_sUserName = sOperatorname;
      this.m_sCorpID = pk_corp;
      this.m_sLogDate = sLogDate;

      initSysParam();

      getBillCardPanel();
      getBillListPanel();
      setButtonState();
      setBillState();

      filterRef(this.m_sCorpID);

      this.m_cNormalColor = getBillCardPanel().getBillTable().getBackground();

      switchListToBill();

      setName("ClientUI");
      setLayout(null);

      setLayout(new CardLayout());

      add(getBillCardPanel(), "card");
      add(getBillListPanel(), "list");

      getBillCardPanel().getBillModel().setCellEditableController(this);
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getBillListPanel().addEditListener(this);
    getBillCardPanel().addEditListener(this);
    getBillCardPanel().addBodyEditListener2(this);

    getBillListPanel().getHeadTable().addMouseListener(this);
    getBillListPanel().getHeadBillModel().addBillSortListener2(this);
    this.m_listHeadSortCtl = new ClientUISortCtl(this, false, 0);
    this.m_listBodySortCtl = new ClientUISortCtl(this, false, 1);

    getBillCardPanel().getBillTable().addSortListener();
    this.m_cardBodySortCtl = new ClientUISortCtl(this, true, 1);
    getBillCardPanel().setAutoExecHeadEditFormula(true);

    getBillCardPanel().getBillModel().addTableModelListener(this);

    getBillCardPanel().addBodyMenuListener(this);

    getBillCardPanel().setVisible(true);
    getBillListPanel().setVisible(true);
    ((CardLayout)getLayout()).show(this, "card");

    showBtnSwitch();
  }

  protected void initSysParam()
  {
    try
    {
      String[] saParam = { "IC028", "IC010", "BD501", "BD502", "BD503", "BD504", "BD301", "IC030", "IC050" };

      ArrayList alAllParam = new ArrayList();

      ArrayList alParam = new ArrayList();
      alParam.add(this.m_sCorpID);
      alParam.add(saParam);
      alAllParam.add(alParam);

      alAllParam.add(this.m_sUserID);

      ArrayList alRetData = null;

      alRetData = (ArrayList)SpecialBillHelper.queryInfo(new Integer(11), alAllParam);

      if ((alRetData == null) || (alRetData.size() < 2)) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000045"));
        return;
      }

      String[] saParamValue = (String[])(String[])alRetData.get(0);

      if ((saParamValue != null) && (saParamValue.length >= alAllParam.size()))
      {
        if (saParamValue[2] != null) {
          this.m_iaScale[0] = Integer.parseInt(saParamValue[2]);
        }
        if (saParamValue[3] != null) {
          this.m_iaScale[1] = Integer.parseInt(saParamValue[3]);
        }
        if (saParamValue[4] != null) {
          this.m_iaScale[2] = Integer.parseInt(saParamValue[4]);
        }
        if (saParamValue[5] != null) {
          this.m_iaScale[3] = Integer.parseInt(saParamValue[5]);
        }
        if (saParamValue[6] != null) {
          this.m_iaScale[4] = Integer.parseInt(saParamValue[6]);
        }
        if ((saParamValue[7] != null) && ("Y".equalsIgnoreCase(saParamValue[7].trim()))) {
          this.m_bIsEditBillCode = true;
        }

        if ((saParamValue[8] != null) && ("仓库".equalsIgnoreCase(saParamValue[8].trim())))
          this.m_isWhInvRef = true;
        else {
          this.m_isWhInvRef = false;
        }
      }
    }
    catch (Exception e)
    {
      SCMEnv.out("can not get para" + e.getMessage());
      if ((e instanceof BusinessException))
        showErrorMessage(e.getMessage());
    }
  }

  public boolean isQuryPlanprice()
  {
    return this.m_isQuryPlanprice;
  }

  protected void minusBillVO()
  {
    if (this.m_iTotalListHeadNum <= 1) {
      this.m_alListData = new ArrayList();
      this.m_alListData.trimToSize();
      this.m_iLastSelListHeadRow = -1;
      this.m_iTotalListHeadNum = 0;
      return;
    }
    this.m_alListData.remove(this.m_iLastSelListHeadRow);
    this.m_alListData.trimToSize();

    this.m_iTotalListHeadNum = this.m_alListData.size();

    if (this.m_iLastSelListHeadRow > this.m_iTotalListHeadNum - 1)
      this.m_iLastSelListHeadRow = (this.m_iTotalListHeadNum - 1);
  }

  protected void mustNoNegative(String sFieldCode, int iRow, BillCardPanel bcp, SpecialBillVO vo)
  {
    Object oValue = bcp.getBodyValueAt(iRow, sFieldCode);
    if ((null == oValue) || (oValue.toString().trim().length() == 0))
      return;
    UFDouble ufdValue = (UFDouble)oValue;
    ufdValue = new UFDouble(Math.abs(ufdValue.doubleValue()), -1 * bcp.getBodyItem(sFieldCode).getDecimalDigits());

    bcp.setBodyValueAt(ufdValue, iRow, sFieldCode);
    vo.setItemValue(iRow, sFieldCode, ufdValue);
  }

  protected void onBizType(ButtonObject bo)
  {
    String sCurrentBillNode = "40081002";
    PfUtilClient.childButtonClicked(bo, this.m_sCorpID, sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);

    if (!PfUtilClient.makeFlag)
    {
      if (PfUtilClient.isCloseOK())
      {
        InvAttrCellRenderer ficr = new InvAttrCellRenderer();
        ficr.setFreeItemRenderer(getBillCardPanel(), this.m_voBill);

        SpecialBillVO voRet = (SpecialBillVO)PfUtilClient.getRetVo();
        if (voRet != null)
        {
          onAdd();

          setTempBillVO(voRet);

          resetAllInvInfo(voRet);

          setNewBillInitData();
        }
      }
    }
    else
    {
      onAdd();
    }
  }

  protected void onJointAdd(ButtonObject bo)
  {
    PfUtilClient.retAddBtn(this.m_boAdd, this.m_sCorpID, this.m_sBillTypeCode, bo);
    updateButtons();
  }

  public void onJointCheck()
  {
    if ((this.m_iLastSelListHeadRow >= 0) && (this.m_alListData != null) && (this.m_alListData.size() > this.m_iLastSelListHeadRow) && (this.m_alListData.get(this.m_iLastSelListHeadRow) != null))
    {
      SpecialBillVO voBill = null;
      SpecialBillHeaderVO voHeader = null;

      voBill = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);

      voHeader = voBill.getHeaderVO();

      if (voHeader == null) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000098"));
        return;
      }
      String sBillPK = null;
      String sBillTypeCode = null;

      sBillPK = voHeader.getCspecialhid();
      sBillTypeCode = voHeader.getCbilltypecode();

      if ((sBillPK == null) || (sBillTypeCode == null)) {
        showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000099"));
        return;
      }
      SourceBillFlowDlg soureDlg = new SourceBillFlowDlg(this, sBillTypeCode, sBillPK, null, this.m_sUserID, this.m_sCorpID);

      soureDlg.showModal();
    } else {
      showHintMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000154"));
    }
  }

  public void onPasteRowTail()
  {
    int iRowCount = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
    getBillCardPanel().pasteLineToTail();

    int iRowCount1 = getBillCardPanel().getBodyPanel().getTableModel().getRowCount();
    BillRowNo.addLineRowNos(getBillCardPanel(), this.m_sBillTypeCode, "crowno", iRowCount1 - iRowCount);
    if (this.m_voBillItem != null) {
      int row = getBillCardPanel().getBillTable().getRowCount() - this.m_voBillItem.length;
      voBillPastLine(row);
    }
  }

  public void onPreview()
  {
    long ITime = System.currentTimeMillis();
    filterNullLine();

    SpecialBillVO vo = this.m_voBill;

    if ((this.m_alListData != null) && (this.m_iMode == 4)) {
      if (this.m_iLastSelListHeadRow == -1)
        vo = (SpecialBillVO)this.m_alListData.get(0);
      else
        vo = (SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow);
    }
    if (null == vo) {
      vo = new SpecialBillVO();
    }
    if (null == vo.getParentVO()) {
      vo.setParentVO(new SpecialBillHeaderVO());
    }
    if ((null == vo.getChildrenVO()) || (vo.getChildrenVO().length == 0) || (vo.getChildrenVO()[0] == null))
    {
      SpecialBillItemVO[] ivo = new SpecialBillItemVO[1];
      ivo[0] = new SpecialBillItemVO();
      vo.setChildrenVO(ivo);
    }

    String sBillID = vo.getPrimaryKey();
    ScmPrintlogVO voSpl = new ScmPrintlogVO();
    voSpl.setCbillid(sBillID);
    voSpl.setVbillcode(vo.getVBillCode());
    voSpl.setCbilltypecode(vo.getBillTypeCode());
    voSpl.setCoperatorid((String)(String)vo.getParentVO().getAttributeValue("coperatorid"));
    voSpl.setIoperatetype(new Integer(0));
    voSpl.setPk_corp(this.m_sCorpID);
    voSpl.setTs((String)(String)vo.getParentVO().getAttributeValue("ts"));

    SCMEnv.out("ts=========tata" + voSpl.getTs());
    PrintLogClient plc = new PrintLogClient();
    plc.setPrintEntry(getPrintEntry());

    plc.setPrintInfo(voSpl);

    plc.addFreshTsListener(this);

    getPrintEntry().setPrintListener(plc);

    if (getPrintEntry().selectTemplate() < 0) {
      return;
    }
    SCMEnv.showTime(ITime, "1:");

    ITime = System.currentTimeMillis();
    getDataSource().setVO(vo);
    SCMEnv.showTime(ITime, "2:");

    ITime = System.currentTimeMillis();

    getPrintEntry().setDataSource(getDataSource());
    SCMEnv.showTime(ITime, "3:");

    ITime = System.currentTimeMillis();
    getPrintEntry().preview();
    SCMEnv.showTime(ITime, "4:");
  }

  protected ConditionVO[] packConditionVO(ConditionVO[] cvonow)
  {
    ConditionVO[] cvo = new ConditionVO[cvonow.length];
    for (int i = 0; i < cvonow.length; i++) {
      cvo[i] = ((ConditionVO)(ConditionVO)cvonow[i].clone());
    }

    ArrayList alcvo = new ArrayList();
    ConditionVO[] cvoFromAlcvo = null;

    for (int i = 0; i < cvo.length; i++)
    {
      if (cvo[i].getFieldCode().trim().equals("qbillstatus")) {
        String sValue = cvo[i].getRefResult().getRefPK().trim();
        ConditionVO cvonew = (ConditionVO)cvo[i].clone();

        cvonew.setFieldCode("1");
        cvonew.setOperaCode("=");
        cvonew.setDataType(1);
        cvonew.setValue("1");
        cvonew.setNoRight(true);
        alcvo.add(cvonew);
        if (sValue.equals("0")) {
          cvonew = (ConditionVO)cvo[i].clone();
          cvonew.setLogic(true);
          cvonew.setNoLeft(false);
          cvonew.setFieldCode("cauditorid");
          cvonew.setOperaCode("is not null");
          cvonew.setNoRight(true);
          alcvo.add(cvonew);
          cvonew = (ConditionVO)cvo[i].clone();
          cvonew.setLogic(true);
          cvonew.setNoLeft(true);
          cvonew.setFieldCode("len(rtrim(cauditorid))");
          cvonew.setOperaCode("<>");
          cvonew.setDataType(1);
          cvonew.setValue("0");
          cvonew.setNoRight(false);
          alcvo.add(cvonew);
        } else if (sValue.equals("1")) {
          cvonew = (ConditionVO)cvo[i].clone();
          cvonew.setLogic(true);
          cvonew.setNoLeft(false);
          cvonew.setFieldCode("cauditorid");
          cvonew.setOperaCode("is null");
          cvonew.setNoRight(true);
          alcvo.add(cvonew);
          cvonew = (ConditionVO)cvo[i].clone();
          cvonew.setLogic(false);
          cvonew.setNoLeft(true);
          cvonew.setFieldCode("len(rtrim(cauditorid))");
          cvonew.setOperaCode("=");
          cvonew.setDataType(1);
          cvonew.setValue("0");
          cvonew.setNoRight(false);
          alcvo.add(cvonew);
        } else if (!sValue.equals("2"));
        cvonew = (ConditionVO)cvo[i].clone();

        cvonew.setLogic(true);
        cvonew.setNoLeft(true);
        cvonew.setFieldCode("1");
        cvonew.setOperaCode("=");
        cvonew.setDataType(1);
        cvonew.setValue("1");
        alcvo.add(cvonew);
      }
      else {
        alcvo.add(cvo[i]);
      }
    }

    cvoFromAlcvo = new ConditionVO[alcvo.size()];
    for (int i = 0; i < alcvo.size(); i++) {
      cvoFromAlcvo[i] = ((ConditionVO)alcvo.get(i));
    }

    return cvoFromAlcvo;
  }

  protected SpecialBillVO qryBill(String pk_corp, String billType, String businessType, String operator, String billID)
  {
    if ((billID == null) || (billType == null) || (pk_corp == null)) {
      SCMEnv.out("no bill param");
      return null;
    }
    SpecialBillVO voRet = null;
    try {
      QryConditionVO voCond = new QryConditionVO("pk_corp='" + pk_corp + "' AND  cbilltypecode='" + billType + "' AND cspecialhid='" + billID + "'");

      ConditionVO[] voaCond = getConditionDlg().getConditionVO();
      voCond.setParam(1, voaCond);

      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000012"));
      ArrayList alListData = SpecialBillHelper.queryBills(this.m_sBillTypeCode, voCond);

      if ((alListData != null) && (alListData.size() > 0)) {
        executeLoadFormularAfterQuery(alListData);

        voRet = (SpecialBillVO)alListData.get(0);

        voRet.calConvRate();
      }
    }
    catch (Exception e)
    {
      handleException(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000015") + e.getMessage());
    }
    return voRet;
  }

  protected void qryItems(int[] iaIndex, String[] saBillPK)
  {
    if ((iaIndex == null) || (saBillPK == null) || (iaIndex.length != saBillPK.length)) {
      SCMEnv.out("param value ERR.");
      return;
    }
    try {
      QryConditionVO voCond = new QryConditionVO();

      voCond.setIntParam(0, 3);
      voCond.setParam(0, saBillPK);

      ArrayList alRetData = SpecialBillHelper.queryBills(this.m_sBillTypeCode, voCond);

      if ((alRetData == null) || (alRetData.size() == 0) || (iaIndex.length != alRetData.size()))
      {
        SCMEnv.out("ret item value ERR.");
        return;
      }

      getFormulaBillContainer().formulaBodys(null, alRetData);

      SpecialBillVO voBill = null;
      for (int i = 0; i < alRetData.size(); i++)
      {
        voBill = (SpecialBillVO)this.m_alListData.get(iaIndex[i]);

        if ((alRetData.get(i) != null) && (voBill != null))
          voBill.setChildrenVO(((SpecialBillVO)alRetData.get(i)).getChildrenVO());
      }
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
    }
  }

  public ArrayList qryLastTs(String sBillPK)
    throws Exception
  {
    try
    {
      ArrayList alFreshRet = (ArrayList)SpecialBillHelper.queryInfo(new Integer(16), sBillPK);

      ArrayList alTs = null;
      if ((alFreshRet != null) && (alFreshRet.size() >= 2) && (alFreshRet.get(1) != null))
      {
        alTs = (ArrayList)alFreshRet.get(1);
      }

      if ((alFreshRet != null) && (alFreshRet.size() >= 3) && 
        (this.m_sCorpID.equals(this.m_voBill.getVBillCode()))) {
        String billcode = (String)alFreshRet.get(2);
        getBillCardPanel().getHeadItem("vbillcode").setValue(billcode);
        this.m_voBill.setVBillCode(billcode);
        ((IBillCode)this.m_alListData.get(this.m_iLastSelListHeadRow)).setVBillCode(billcode);
      }

      return alTs;
    } catch (Exception e) {
      SCMEnv.error(e);
    }
    return null;
  }

  public void resetAllInvInfo(SpecialBillVO voBill)
  {
    try
    {
      if ((voBill == null) || (voBill.getItemVOs() == null) || (voBill.getItemVOs().length == 0))
      {
        SCMEnv.out("---- no item ");
        return;
      }

      getBillCardPanel().getBillModel().setNeedCalculate(true);

      SpecialBillItemVO[] voaItem = voBill.getItemVOs();

      ArrayList alInvID = new ArrayList(); ArrayList alInvID2 = null;

      for (int i = 0; i < voBill.getItemVOs().length; i++) {
        alInvID2 = new ArrayList();
        alInvID2.add(voaItem[i].getAttributeValue("cinventoryid"));
        alInvID2.add(voaItem[i].getAttributeValue("castunitid"));
        alInvID.add(alInvID2);
      }

      ArrayList alIDs = new ArrayList();
      alIDs.add(alInvID);
      alIDs.add(this.m_sUserID);
      alIDs.add(this.m_sCorpID);

      ArrayList alRetInvInfo = (ArrayList)SpecialBillHelper.queryInfo(new Integer(8), alIDs);

      if ((this.m_voBill != null) && (alRetInvInfo != null)) {
        for (int row = 0; row < alRetInvInfo.size(); row++) {
          InvVO voInv = (InvVO)alRetInvInfo.get(row);
          if (voInv != null) {
            this.m_voBill.setItemInv(row, voInv);

            setBodyInvValue(row, voInv);
            try
            {
              getBillCardPanel().getBillModel().removeTableModelListener(this);
              try
              {
                String sInvCode = null;
                if (voInv.getAttributeValue("cinventorycode") != null) {
                  sInvCode = voInv.getAttributeValue("cinventorycode").toString();
                }
                getBillCardPanel().setBodyValueAt(sInvCode, row, "cinventorycode");

                UIRefPane refInv = (UIRefPane)getBillCardPanel().getBodyItem("cinventorycode").getComponent();

                if (refInv != null) {
                  refInv.setValue(sInvCode);
                  refInv.setPK(voInv.getAttributeValue("cinventoryid"));
                }

                String sProjectName = null;
                if (voInv.getAttributeValue("cprojectname") != null) {
                  sInvCode = voInv.getAttributeValue("cprojectname").toString();
                }
                getBillCardPanel().setBodyValueAt(sInvCode, row, "cprojectname");

                refInv = (UIRefPane)getBillCardPanel().getBodyItem("cprojectname").getComponent();

                if (refInv != null) {
                  refInv.setValue(sInvCode);
                  refInv.setPK(voInv.getAttributeValue("cprojectid"));
                }

                String sProjectPhraseName = null;
                if (voInv.getAttributeValue("cprojectphrasename") != null) {
                  sInvCode = voInv.getAttributeValue("cprojectphrasename").toString();
                }
                getBillCardPanel().setBodyValueAt(sInvCode, row, "cprojectphrasename");

                refInv = (UIRefPane)getBillCardPanel().getBodyItem("cprojectphrasename").getComponent();

                if (refInv != null) {
                  refInv.setValue(sInvCode);
                  refInv.setPK(voInv.getAttributeValue("cprojectphraseid"));
                }
              }
              catch (Exception e)
              {
              }
              getBillCardPanel().getBillModel().addTableModelListener(this);
            } catch (Exception e) {
              SCMEnv.out("--->" + e.getMessage());
            }
          } else {
            SCMEnv.out("--->inv info nvl");
          }

        }

      }

      getBillCardPanel().getBillModel().setNeedCalculate(true);
      getBillCardPanel().getBillModel().reCalcurateAll();
    }
    catch (Exception e2) {
      SCMEnv.error(e2);
    }
  }

  protected void resetConditionVO(ConditionVO[] conVO)
  {
    if ((conVO != null) && (conVO.length != 0))
    {
      for (int i = 0; i < conVO.length; i++)
      {
        if ((!"like".equals(conVO[i].getOperaCode().trim())) || (conVO[i].getFieldCode() == null))
          continue;
        String sFieldCode = conVO[i].getFieldCode().trim();
        if (("invcl.invclasscode".equals(sFieldCode)) && (conVO[i].getValue().trim() != null))
        {
          conVO[i].setValue(conVO[i].getValue() + "%");
        }
        else if (conVO[i].getValue().trim() != null)
          conVO[i].setValue("%" + conVO[i].getValue() + "%");
      }
    }
  }

  protected void setBillState()
  {
    switch (this.m_iMode) {
    case 0:
      getBillCardPanel().setEnabled(true);
      getBillListPanel().setVisible(false);
      getBillCardPanel().setVisible(true);
      break;
    case 2:
      getBillCardPanel().setEnabled(true);
      getBillListPanel().setVisible(false);
      getBillCardPanel().setVisible(true);
      break;
    case 3:
      getBillCardPanel().setEnabled(false);
      getBillListPanel().setVisible(false);
      getBillCardPanel().setVisible(true);

      if (getBillCardPanel().getHeadItem("ishsl") == null) break;
      BillItem[] headItems = getBillCardPanel().getHeadItems();
      if (headItems != null) {
        for (int i = 0; i < headItems.length; i++) {
          if (headItems[i].getKey().equals("ishsl"))
            headItems[i].setEnabled(true);
          else
            headItems[i].setEnabled(false);
        }
      }
      break;
    case 4:
      getBillCardPanel().setVisible(false);
      getBillListPanel().setVisible(true);
    case 1:
    }
  }

  protected void setBillValueVO(SpecialBillVO bvo, boolean bExeformula)
  {
    try
    {
      long ITime = System.currentTimeMillis();
      getBillCardPanel().getBillModel().removeTableModelListener(this);
      getBillCardPanel().removeBillEditListenerHeadTail();
      getBillCardPanel().setBillValueVO(bvo);
      SCMEnv.showTime(ITime, "setBillValueVO1时间：");
      ITime = System.currentTimeMillis();

      setTailValue(0);
      SCMEnv.showTime(ITime, "setBillValueVO2:setTailValue时间：");

      ITime = System.currentTimeMillis();
      if (bExeformula)
      {
        getBillCardPanel().getBillModel().getFormulaParse().setNullAsZero(false);
        getBillCardPanel().getBillModel().execLoadFormula();
      }
      SCMEnv.showTime(ITime, "setBillValueVO3:execLoadFormula：");
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
    }
    finally
    {
      long ITime = System.currentTimeMillis();
      getBillCardPanel().getBillModel().addTableModelListener(this);
      getBillCardPanel().addBillEditListenerHeadTail(this);
      SCMEnv.showTime(ITime, "setBillValueVO4时间：");
    }
  }

  public void setBodyInVO(InvVO[] invVOs, int iBeginRow)
  {
    int iCurRow = 0;
    getBillCardPanel().getBillModel().setNeedCalculate(false);
    for (int i = 0; i < invVOs.length; i++) {
      iCurRow = iBeginRow + i;

      this.m_voBill.setItemInv(iCurRow, invVOs[i]);

      setBodyInvValue(iCurRow, invVOs[i]);

      String[] sIKs = getClearIDs(1, "cinventorycode");

      clearRowData(iCurRow, sIKs);
    }

    getBillCardPanel().getBillModel().setNeedCalculate(true);
    getBillCardPanel().getBillModel().reCalcurateAll();
  }

  protected void setCellRef(int rownow, int colnow, ListSelectionEvent e)
  {
  }

  public void setIsQuryPlanprice(boolean newQuryPlanprice)
  {
    this.m_isQuryPlanprice = newQuryPlanprice;
  }

  public void setQuryPlanprice(boolean newQuryPlanprice)
  {
    this.m_isQuryPlanprice = newQuryPlanprice;
  }

  protected BillData setScale(BillData bd)
  {
    BillItem[] biaCardBody = bd.getBodyItems();
    BillItem[] biaListBody = getBillListPanel().getBodyBillModel().getBodyItems();

    Hashtable htCardBody = new Hashtable();
    for (int i = 0; i < biaCardBody.length; i++) {
      htCardBody.put(biaCardBody[i].getKey(), new Integer(i));
    }
    Hashtable htListBody = new Hashtable();
    for (int i = 0; i < biaListBody.length; i++) {
      htListBody.put(biaListBody[i].getKey(), new Integer(i));
    }

    String[] saBodyNumItemKey = { "cysl", "dshldtransnum", "desl", "naccountnum", "nadjustnum", "nchecknum", "ztsl", "naccountgrsnum", "nadjustgrsnum", "ndiffgrsnum", "ncheckgrsnum", "nshldtransgrsnum" };

    for (int k = 0; k < saBodyNumItemKey.length; k++)
    {
      if (htCardBody.containsKey(saBodyNumItemKey[k])) {
        biaCardBody[Integer.valueOf(htCardBody.get(saBodyNumItemKey[k]).toString()).intValue()].setLength(13 + this.m_iaScale[0]);

        biaCardBody[Integer.valueOf(htCardBody.get(saBodyNumItemKey[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[0]);
      }

      if (htListBody.containsKey(saBodyNumItemKey[k])) {
        biaListBody[Integer.valueOf(htListBody.get(saBodyNumItemKey[k]).toString()).intValue()].setLength(13 + this.m_iaScale[0]);

        biaListBody[Integer.valueOf(htListBody.get(saBodyNumItemKey[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[0]);
      }

    }

    String[] saBodyAstNumItemKey = { "cyfsl", "naccountastnum", "ncheckastnum", "nadjustastnum", "nshldtransastnum" };

    for (int k = 0; k < saBodyAstNumItemKey.length; k++)
    {
      if (htCardBody.containsKey(saBodyAstNumItemKey[k])) {
        biaCardBody[Integer.valueOf(htCardBody.get(saBodyAstNumItemKey[k]).toString()).intValue()].setLength(13 + this.m_iaScale[1]);

        biaCardBody[Integer.valueOf(htCardBody.get(saBodyAstNumItemKey[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[1]);
      }

      if (htListBody.containsKey(saBodyAstNumItemKey[k])) {
        biaListBody[Integer.valueOf(htListBody.get(saBodyAstNumItemKey[k]).toString()).intValue()].setLength(13 + this.m_iaScale[1]);

        biaListBody[Integer.valueOf(htListBody.get(saBodyAstNumItemKey[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[1]);
      }

    }

    String[] saBodyPrice = { "jhdj", "nprice", "nplannedprice" };
    for (int k = 0; k < saBodyPrice.length; k++)
    {
      if (htCardBody.containsKey(saBodyPrice[k])) {
        biaCardBody[Integer.valueOf(htCardBody.get(saBodyPrice[k]).toString()).intValue()].setLength(13 + this.m_iaScale[3]);

        biaCardBody[Integer.valueOf(htCardBody.get(saBodyPrice[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[3]);
      }

      if (htListBody.containsKey(saBodyPrice[k])) {
        biaListBody[Integer.valueOf(htListBody.get(saBodyPrice[k]).toString()).intValue()].setLength(13 + this.m_iaScale[3]);

        biaListBody[Integer.valueOf(htListBody.get(saBodyPrice[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[3]);
      }

    }

    String[] saBodyMny = { "je", "jhje", "nmny", "nplannedmny" };
    for (int k = 0; k < saBodyMny.length; k++)
    {
      if (htCardBody.containsKey(saBodyMny[k])) {
        biaCardBody[Integer.valueOf(htCardBody.get(saBodyMny[k]).toString()).intValue()].setLength(13 + this.m_iaScale[4]);

        biaCardBody[Integer.valueOf(htCardBody.get(saBodyMny[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[4]);
      }

      if (htListBody.containsKey(saBodyMny[k])) {
        biaListBody[Integer.valueOf(htListBody.get(saBodyMny[k]).toString()).intValue()].setLength(13 + this.m_iaScale[4]);

        biaListBody[Integer.valueOf(htListBody.get(saBodyMny[k]).toString()).intValue()].setDecimalDigits(this.m_iaScale[4]);
      }

    }

    String[] saTailNumItemKey = { "xczl", "bkxcl", "neconomicnum", "nmaxstocknum", "nminstocknum", "norderpointnum", "nsafestocknum" };

    for (int k = 0; k < saTailNumItemKey.length; k++) {
      try
      {
        bd.getTailItem(saTailNumItemKey[k]).setLength(13 + this.m_iaScale[0]);
        bd.getTailItem(saTailNumItemKey[k]).setDecimalDigits(this.m_iaScale[0]);
      }
      catch (Exception e)
      {
      }
    }

    String[] saHeaderNumItemKey = { "nfixdisassemblymny" };
    for (int k = 0; k < saHeaderNumItemKey.length; k++)
      try
      {
        bd.getHeadItem(saHeaderNumItemKey[k]).setLength(13 + this.m_iaScale[0]);

        bd.getHeadItem(saHeaderNumItemKey[k]).setDecimalDigits(this.m_iaScale[0]);
      }
      catch (Exception e)
      {
      }
    for (int k = 0; k < saHeaderNumItemKey.length; k++) {
      try
      {
        getBillListPanel().getHeadItem(saHeaderNumItemKey[k]).setLength(13 + this.m_iaScale[0]);

        getBillListPanel().getHeadItem(saHeaderNumItemKey[k]).setDecimalDigits(this.m_iaScale[0]);
      }
      catch (Exception e)
      {
      }

    }

    if (htCardBody.containsKey("hsl")) {
      biaCardBody[Integer.valueOf(htCardBody.get("hsl").toString()).intValue()].setLength(13 + this.m_iaScale[2]);

      biaCardBody[Integer.valueOf(htCardBody.get("hsl").toString()).intValue()].setDecimalDigits(this.m_iaScale[2]);
    }

    if (htListBody.containsKey("hsl")) {
      biaListBody[Integer.valueOf(htListBody.get("hsl").toString()).intValue()].setLength(13 + this.m_iaScale[2]);

      biaListBody[Integer.valueOf(htListBody.get("hsl").toString()).intValue()].setDecimalDigits(this.m_iaScale[2]);
    }

    return bd;
  }

  public void setTempBillVO(SpecialBillVO bvo)
  {
    getBillCardPanel().getBillModel().removeTableModelListener(this);

    this.m_voBill = ((SpecialBillVO)bvo.clone());

    setBillValueVO(bvo);

    bvo.clearInvQtyInfo();

    getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);

    setTailValue(0);

    BillModel bmTemp = getBillCardPanel().getBillModel();
    int iRowCount = 0;
    if (bvo.getItemVOs() == null)
      iRowCount = bvo.getItemVOs().length;
    for (int row = 0; row < iRowCount; row++)
    {
      if (bmTemp != null) {
        bmTemp.setRowState(row, 1);
      }
    }
    getBillCardPanel().getBillModel().addTableModelListener(this);
  }

  public void setTs(int iIndex, ArrayList alTs)
    throws Exception
  {
    SpecialBillVO voListBill = null;

    if ((iIndex >= 0) && (this.m_alListData != null) && (this.m_alListData.size() > iIndex) && (this.m_alListData.get(iIndex) != null))
    {
      voListBill = (SpecialBillVO)this.m_alListData.get(iIndex);
    }
    if (voListBill != null)
      setTs(voListBill, alTs);
  }

  public void setTs(SpecialBillVO voThisBill, ArrayList alTs)
    throws Exception
  {
    if ((alTs == null) || (alTs.size() < 2) || (alTs.get(0) == null) || (alTs.get(1) == null))
    {
      throw new Exception(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000027"));
    }
    Hashtable htTs = new Hashtable();
    ArrayList alTemp = null;

    for (int i = 1; i < alTs.size(); i++) {
      alTemp = (ArrayList)alTs.get(i);
      if ((alTemp == null) || (alTemp.size() != 2) || (alTemp.get(0) == null) || (alTemp.get(1) == null))
      {
        continue;
      }
      htTs.put(alTemp.get(0), alTemp.get(1));
    }

    if (voThisBill != null) {
      voThisBill.setStatus(0);
      voThisBill.getHeaderVO().setTs(alTs.get(0).toString());

      int iRowCount = voThisBill.getItemCount();
      SpecialBillItemVO[] voaItem = voThisBill.getItemVOs();
      Object oTempTs = null;
      for (int row = 0; row < iRowCount; row++) {
        if ((voaItem[row].getCinventoryid() == null) || (voaItem[row].getPrimaryKey() == null))
          continue;
        oTempTs = htTs.get(voaItem[row].getPrimaryKey());
        if (oTempTs != null)
          voaItem[row].setTs(oTempTs.toString());
        else
          SCMEnv.out("-------Err-------frh ts -------" + row + voaItem[row].getPrimaryKey());
      }
    }
  }

  public void setUiTs(ArrayList alTs)
    throws Exception
  {
    long lTimes = System.currentTimeMillis();
    if ((alTs == null) || (alTs.size() < 2) || (alTs.get(0) == null) || (alTs.get(1) == null))
    {
      throw new Exception(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000027"));
    }
    Hashtable htTs = new Hashtable();
    ArrayList alTemp = null;
    for (int i = 1; i < alTs.size(); i++) {
      alTemp = (ArrayList)alTs.get(i);
      if ((alTemp == null) || (alTemp.size() != 2) || (alTemp.get(0) == null) || (alTemp.get(1) == null))
      {
        continue;
      }
      htTs.put(alTemp.get(0), alTemp.get(1));
    }

    getBillCardPanel().getBillModel().setNeedCalculate(false);

    getBillCardPanel().setHeadItem("ts", alTs.get(0).toString());

    int iRowCount = getBillCardPanel().getRowCount();
    Object oTempTs = null;
    String sRowPK = null; String sInvID = null;
    for (int row = 0; row < iRowCount; row++)
    {
      sRowPK = (String)getBillCardPanel().getBodyValueAt(row, "cspecialbid");
      sInvID = (String)getBillCardPanel().getBodyValueAt(row, "cinventoryid");
      if ((sInvID != null) && (sRowPK != null)) {
        oTempTs = htTs.get(sRowPK);
        if (oTempTs != null)
          getBillCardPanel().setBodyValueAt(oTempTs.toString(), row, "ts");
        else {
          SCMEnv.out("-------Err-------frh ts -------");
        }
      }
    }
    getBillCardPanel().getBillModel().setNeedCalculate(true);
    getBillCardPanel().getBillModel().reCalcurateAll();
    SCMEnv.showTime(lTimes, "setUiTs");
  }

  protected void voBillCopyLine(int[] row)
  {
    if (row != null) {
      this.m_voBillItem = new SpecialBillItemVO[row.length];
      SpecialBillItemVO uicopyvo = null;
      for (int i = 0; i < row.length; i++) {
        this.m_voBillItem[i] = ((SpecialBillItemVO)(SpecialBillItemVO)this.m_voBill.getChildrenVO()[row[i]].clone());
        uicopyvo = (SpecialBillItemVO)getBillCardPanel().getBillModel().getBodyValueRowVO(row[i], SpecialBillItemVO.class.getName());
        uicopyvo = (SpecialBillItemVO)uicopyvo.clone();
        String[] keys = uicopyvo.getAttributeNames();
        SmartVOUtilExt.copyVOByVO(this.m_voBillItem[i], keys, uicopyvo, keys);
      }
    }
    else {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000025"));
    }
  }

  protected void voBillPastLine(int row)
  {
    if ((row >= 0) && (this.m_voBillItem != null))
    {
      for (int i = 0; i < this.m_voBillItem.length; i++)
      {
        this.m_voBill.getChildrenVO()[(row + i)] = ((SpecialBillItemVO)this.m_voBillItem[i].clone());
        this.m_voBill.getChildrenVO()[(row + i)].setAttributeValue("crowno", getBillCardPanel().getBodyValueAt(row + i, "crowno"));
      }
    }
  }

  public void freshTs(String sBillID, String sTS, Integer iPrintCount)
  {
    SCMEnv.out("new Ts = " + sTS);
    SCMEnv.out("new iPrintCount = " + iPrintCount);

    if ((this.m_alListData == null) || (this.m_alListData.size() == 0)) {
      return;
    }

    int index = 0;
    SpecialBillVO voBill = null;
    SpecialBillHeaderVO headerVO = null;
    for (; index < this.m_alListData.size(); index++) {
      voBill = (SpecialBillVO)this.m_alListData.get(index);
      headerVO = voBill.getHeaderVO();

      if (sBillID.equals(headerVO.getPrimaryKey())) {
        break;
      }
    }
    if (index == this.m_alListData.size()) {
      return;
    }

    headerVO.setAttributeValue("ts", sTS);
    headerVO.setAttributeValue("iprintcount", iPrintCount);

    if (this.m_iMode == 4) {
      int iPrintColumn = getBillListPanel().getHeadBillModel().getBodyColByKey("ts");
      getBillListPanel().getHeadBillModel().setValueAt(sTS, index, iPrintColumn);
      iPrintColumn = getBillListPanel().getHeadBillModel().getBodyColByKey("iprintcount");
      getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, index, iPrintColumn);
    }
    else if (index == this.m_iLastSelListHeadRow) {
      getBillCardPanel().setHeadItem("ts", sTS);
      getBillCardPanel().setTailItem("iprintcount", iPrintCount);
    }
  }

  protected void qryCalbodyByWhid(SpecialBillHeaderVO voHead)
  {
  }

  public boolean isCellEditable(boolean value, int row, String itemkey)
  {
    if (this.m_iMode == 3) return false;

    getBillCardPanel().stopEditing();

    boolean isEditable = true;
    String sItemKey = itemkey;
    BillItem biCol = getBillCardPanel().getBodyItem(sItemKey);
    int iRow = row;

    if ((sItemKey == null) || (biCol == null)) {
      return false;
    }

    if (!biCol.isEdit())
    {
      return false;
    }

    if (this.m_voBill == null) {
      biCol.setEnabled(false);
      return false;
    }

    if ((sItemKey.equals("cinventorycode")) || (sItemKey.equals("cwarehousename"))) {
      if (sItemKey.equals("cinventorycode")) {
        StringBuffer swherewh = new StringBuffer();
        BillItem biWh = getBillCardPanel().getHeadItem("coutwarehouseid");
        if (biWh == null) return true;
        String cwhid = biWh.getValue();
        if (cwhid != null) {
          swherewh.append(" pk_invmandoc in (select cinventoryid from ic_numctl where cwarehouseid='" + cwhid + "' )");
        }

        if ((this.m_isWhInvRef) && (swherewh.length() > 0)) {
          RefFilter.filtInv(biCol, this.m_sCorpID, new String[] { swherewh.toString() });
        }

      }

      return true;
    }
    if ((null == this.m_voBill.getItemValue(iRow, "cinventoryid")) || (this.m_voBill.getItemValue(iRow, "cinventoryid").toString().trim().length() == 0))
    {
      return false;
    }

    InvVO voInv = this.m_voBill.getItemInv(iRow);

    if ((sItemKey.equals("castunitname")) || (sItemKey.equals("hsl")) || (sItemKey.equals("nshldtransastnum")) || (sItemKey.equals("nadjustastnum")) || (sItemKey.equals("ncheckastnum")) || (sItemKey.equals("cyfsl")))
    {
      if ((voInv.getIsAstUOMmgt() == null) || (voInv.getIsAstUOMmgt().intValue() != 1))
      {
        isEditable = false;
      }
      else if (sItemKey.equals("castunitname")) {
        filterMeas(iRow);
      }
      else if ((sItemKey.equals("hsl")) && (this.m_voBill.getItemValue(iRow, "isSolidConvRate") != null) && (((Integer)this.m_voBill.getItemValue(iRow, "isSolidConvRate")).intValue() == 1))
      {
        isEditable = false;
      }

    }
    else if (sItemKey.equals("vfree0")) {
      if ((voInv.getIsFreeItemMgt() == null) || (voInv.getIsFreeItemMgt().intValue() != 1))
      {
        isEditable = false;
      }
      else
      {
        getFreeItemRefPane().setFreeItemParam(voInv);
      }

    }
    else if (sItemKey.equals("vbatchcode")) {
      if ((voInv.getIsLotMgt() == null) || (voInv.getIsLotMgt().intValue() != 1))
      {
        isEditable = false;
      }
    }
    else if ((sItemKey.equals("dvalidate")) || (sItemKey.equals("scrq"))) {
      isEditable = true;

      if ((voInv.getIsValidateMgt() == null) || (voInv.getIsValidateMgt().intValue() != 1))
      {
        isEditable = false;
      }
    } else if (sItemKey.startsWith("vuserdef")) {
      BillItem item = getBillCardPanel().getBodyItem(sItemKey);
      if ((item != null) && (item.getDataType() == 7)) {
        String pk = null;
        pk = (String)this.m_voBill.getItemValue(iRow, "pk_defdoc" + sItemKey.substring("vuserdef".length()));
        if ((pk != null) && (pk.length() > 0) && (getBillCardPanel().getBodyValueAt(iRow, sItemKey) != null));
        ((UIRefPane)item.getComponent()).setPK(pk);
      } else if (getBillCardPanel().getBodyValueAt(iRow, sItemKey) == null) {
        ((UIRefPane)item.getComponent()).setPK(null);
      }
    }
    return isEditable;
  }

  private BatchcodeVO getBCVO(String pk_invmandoc, String vbatchcode) {
    ConditionVO[] voCons = new ConditionVO[2];
    voCons[0] = new ConditionVO();
    voCons[0].setFieldCode("pk_invbasdoc");
    voCons[0].setValue(pk_invmandoc);
    voCons[0].setLogic(true);
    voCons[0].setOperaCode("=");

    voCons[1] = new ConditionVO();
    voCons[1].setFieldCode("vbatchcode");
    voCons[1].setValue(vbatchcode);
    voCons[1].setLogic(true);
    voCons[1].setOperaCode("=");
    BatchcodeVO[] vos = null;
    try {
      vos = BatchcodeHelper.queryBatchcode(voCons, this.m_sCorpID);
    } catch (Exception e) {
      SCMEnv.error(e);
    }
    return (vos == null) || (vos.length == 0) ? null : vos[0];
  }

  protected ClientUISortCtl getListHeadSortCtl()
  {
    return this.m_listHeadSortCtl;
  }

  protected ClientUISortCtl getListBodySortCtl()
  {
    return this.m_listBodySortCtl;
  }

  protected ClientUISortCtl getCardBodySortCtl()
  {
    return this.m_cardBodySortCtl;
  }

  public void afterSortEvent(boolean iscard, boolean ishead, String key)
  {
    if (ishead) {
      this.m_alListData = ((ArrayList)getListHeadSortCtl().getRelaSortData(0));
    }
    else if (iscard) {
      if (this.m_voBill != null) {
        SpecialBillItemVO[] itemvos = (SpecialBillItemVO[])(SpecialBillItemVO[])getCardBodySortCtl().getRelaSortDataAsArray(0);

        this.m_voBill.setChildrenVO(itemvos);
        if ((this.m_iMode != 0) && (this.m_iMode != 1) && (this.m_iMode != 2) && (this.m_alListData != null) && (this.m_alListData.size() > this.m_iLastSelListHeadRow) && 
          (this.m_voBill.getHeaderVO().getCspecialhid() != null) && (this.m_voBill.getHeaderVO().getCspecialhid().equals(((SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).getHeaderVO().getCspecialhid()))) {
          try
          {
            ((SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).setChildrenVO((SpecialBillItemVO[])(SpecialBillItemVO[])ObjectUtils.serializableClone(itemvos));
          }
          catch (Exception e) {
            SCMEnv.error(e);
          }
        }
      }
    }
    else if ((this.m_alListData != null) && (this.m_alListData.size() > 0)) {
      SpecialBillItemVO[] itemvos = (SpecialBillItemVO[])(SpecialBillItemVO[])getListBodySortCtl().getRelaSortDataAsArray(0);

      ((SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).setChildrenVO(itemvos);

      if ((this.m_voBill != null) && (this.m_voBill.getHeaderVO().getCspecialhid() != null) && (this.m_voBill.getHeaderVO().getCspecialhid().equals(((SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).getHeaderVO().getCspecialhid())))
      {
        try
        {
          this.m_voBill.setChildrenVO((SpecialBillItemVO[])(SpecialBillItemVO[])ObjectUtils.serializableClone(itemvos));
        }
        catch (Exception e) {
          SCMEnv.error(e);
        }
      }
    }
  }

  public void beforeSortEvent(boolean iscard, boolean ishead, String key)
  {
    clearOrientColor();

    if (ishead) {
      SCMEnv.out("表头排序");
      if ((this.m_alListData == null) || (this.m_alListData.size() <= 1))
      {
        return;
      }
      getListHeadSortCtl().addRelaSortData(this.m_alListData);
    }
    else {
      SCMEnv.out("表体排序");

      if (iscard) {
        if (this.m_voBill != null)
          getCardBodySortCtl().addRelaSortData(this.m_voBill.getItemVOs());
      }
      else if ((this.m_alListData != null) && (this.m_alListData.size() > 0))
        getListBodySortCtl().addRelaSortData(((SpecialBillVO)this.m_alListData.get(this.m_iLastSelListHeadRow)).getItemVOs());
    }
  }

  public void currentRowChange(int newrow)
  {
    if (newrow >= 0)
    {
      if (this.m_iLastSelListHeadRow != newrow) {
        this.m_iLastSelListHeadRow = newrow;
        this.m_iFirstSelListHeadRow = -1;
        switchBillToList();
        this.m_iMode = 4;
        setButtonState();
        setBillState();
      }
    } else {
      if ((this.m_iLastSelListHeadRow < 0) || (this.m_iLastSelListHeadRow >= getBillListPanel().getHeadBillModel().getRowCount())) {
        this.m_iLastSelListHeadRow = 0;
        this.m_iFirstSelListHeadRow = -1;
      }
      switchBillToList();
      this.m_iMode = 4;
      setButtonState();
      setBillState();
    }
  }

  public boolean onClosing()
  {
    if ((this.m_iMode == 0) || (this.m_iMode == 2))
    {
      int iret = MessageDialog.showYesNoCancelDlg(this, null, NCLangRes.getInstance().getStrByID("common", "UCH001"));
      if (iret == 4) {
        try {
          boolean isok = onSave();
          if (!isok)
          {
            return false;
          }
        }
        catch (Exception e) {
          return false;
        }
        return true;
      }

      return iret == 8;
    }

    return true;
  }

  public void doQueryAction(ILinkQueryData querydata)
  {
    if (querydata == null)
      return;
    queryForLinkOper(querydata.getPkOrg(), querydata.getBillType(), querydata.getBillID());
  }

  public void doApproveAction(ILinkApproveData approvedata)
  {
    if (approvedata == null)
      return;
    queryForLinkOper(approvedata.getPkOrg(), approvedata.getBillType(), approvedata.getBillID());
  }

  public SpecialBillVO queryForLinkOper(String PkOrg, String billtype, String billid)
  {
    if (billid == null) {
      return null;
    }
    ICDataSet datas = GenMethod.queryData("ic_special_h", "cspecialhid", new String[] { "pk_corp" }, new int[] { 3 }, new String[] { billid }, " dr=0 ");

    String cbillpkcorp = datas == null ? null : (String)datas.getValueAt(0, 0);

    if ((cbillpkcorp == null) || (cbillpkcorp.trim().length() <= 0)) {
      MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000062"));
    }
    else
    {
      QueryConditionDlgForBill qrydlg = new QueryConditionDlgForBill(this);
      qrydlg.setTempletID(cbillpkcorp, this.m_sPNodeCode, this.m_sUserID, null);
      String swhere = " head.cbilltypecode='" + billtype + "' and head.cspecialhid='" + billid + "' ";
      QryConditionVO qcvo = new QryConditionVO(swhere);
      String[] refcodes = GenMethod.getDataPowerFieldFromDlg(qrydlg, false, null);
      qrydlg.setCorpRefs("head.pk_corp", refcodes);
      ConditionVO[] convos = ICCommonBusi.getDataPowerConsFromDlg(qrydlg, this.m_sPNodeCode, cbillpkcorp, this.m_sUserID, refcodes);
      if ((convos != null) && (convos.length > 0)) {
        qcvo.setParam(1, convos);

        String spartwhere = convos[0].getWhereSQL(convos);
        if ((spartwhere != null) && (spartwhere.trim().length() > 0))
          qcvo.setQryCond(swhere + " and " + spartwhere);
      }
      qcvo.setIntParam(0, 4);
      loadBillListPanel(qcvo);
    }

    SpecialBillVO voBill = null;
    if ((this.m_alListData != null) && (this.m_alListData.size() > 0)) {
      voBill = (SpecialBillVO)this.m_alListData.get(0);
    }

    onList();

    if (voBill != null) {
      String sbill_pk_corp = voBill.getHeaderVO().getPk_corp();
      if (!getClientEnvironment().getCorporation().getPrimaryKey().equals(sbill_pk_corp)) {
        setButtons(new ButtonObject[] { this.m_boPrint });
      }
    }
    setButtonState();
    setBillState();

    return voBill;
  }

  public void showBtnSwitch()
  {
    if (this.m_iMode == 4) {
      this.m_boList.setName(NCLangRes.getInstance().getStrByID("common", "UCH021"));
    }
    else {
      this.m_boList.setName(NCLangRes.getInstance().getStrByID("common", "UCH022"));
    }

    updateButton(this.m_boList);
  }

  public void setSortEnable(boolean iscard, boolean ishead, boolean isenable)
  {
    if (iscard) {
      getBillCardPanel().getBillTable().setSortEnabled(isenable);
      getBillCardPanel().getBillTable().removeSortListener();
    }
    else if (ishead) {
      getBillListPanel().getHeadTable().setSortEnabled(isenable);
      getBillListPanel().getHeadTable().removeSortListener();
    } else {
      getBillListPanel().getBodyTable().setSortEnabled(isenable);
      getBillListPanel().getBodyTable().removeSortListener();
    }
  }

  public void doMaintainAction(ILinkMaintainData maintaindata)
  {
    if (maintaindata == null)
      return;
    queryForLinkOper(this.m_sCorpID, this.m_sBillTypeCode, maintaindata.getBillID());
  }
}