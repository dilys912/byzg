package nc.ui.scm.pub.redun;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.AbstractReferQueryUI;
import nc.ui.pub.pf.IinitQueryData;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.bd.CorpVO;
import nc.vo.bd.def.DefVO;
import nc.vo.cache.ext.CacheToMapAdapter;
import nc.vo.logging.Debug;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.OnHandRefreshVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.redun.RedunUtil;
import nc.vo.scm.pub.redun.RedunVO;
import nc.vo.scm.pub.smart.ObjectUtils;

public class RedunSourceDlg extends AbstractReferQueryUI
  implements ActionListener, BillEditListener, BillEditListener2, BillTableMouseListener, RedunUtil
{
  private OnHandRefreshVO query_onhandvo = null;

  private RedunSourceDlg onHandWorkThread = null;

  private HashMap hsSourceHeadVos = null;
  private HashMap hsSourceBodyVos = null;
  protected RedunListPanel m_rlpBill;
  private static final String CACHE_REGION = "nc.ui.scm.pub.redun.RedunSourceDlg";
  private static CacheToMapAdapter hsdlgcache;
  private static AggregatedValueObject m_tmpRetVo = null;
  private static AggregatedValueObject[] m_tmpRetVos = null;
  private static Boolean bIsRetVOsFromThis = null;
  protected JPanel ivjUIDialogContentPane;
  protected UIButton ivjbtnCancel;
  protected UIButton ivjbtnOk;
  protected UIButton ivjbtnQuery;
  protected UIButton btnAdd;
  protected UIButton btnDel;
  protected UIButton btnCopy;
  protected UIButton btnPaste;
  protected UIButton btnFastRef;
  protected UIButton btnRefQry;
  protected UIButton ivjbtnSplitMode;
  protected UIPanel panlCmd;
  protected UIPanel panlCmdForSrc;
  protected UIPanel panlSelect;
  protected String m_whereStr;
  protected AggregatedValueObject retBillVo;
  protected AggregatedValueObject[] retBillVos;
  protected boolean isRelationCorp = true;
  protected Integer m_BD501;
  protected Integer m_BD502;
  protected Integer m_BD505;
  protected Integer m_BD503;
  private UFBoolean m_bSCM01;
  private UFBoolean m_bSCM02;
  UIPanel m_OnhandinfoPanel;
  protected DefaultSourceDlgCtl m_defaultsourcectl;
  protected DefaultTargetDlgCtl m_defaulttargetctl;
  protected UISplitPane ivjAstPanel;
  public static Icon addicon;
  public static Icon delicon;

  public HashMap getHsSourceHeadVos()
  {
    if (this.hsSourceHeadVos == null)
      this.hsSourceHeadVos = new HashMap();
    return this.hsSourceHeadVos;
  }

  public HashMap getHsSourceBodyVos()
  {
    if (this.hsSourceBodyVos == null)
      this.hsSourceBodyVos = new HashMap();
    return this.hsSourceBodyVos;
  }

  public void clearSourceVosBuffer()
  {
    getHsSourceHeadVos().clear();
    getHsSourceBodyVos().clear();
  }

  public void setSourceVosToBuffer(AggregatedValueObject[] vos)
  {
    clearSourceVosBuffer();
    if ((vos == null) || (vos.length <= 0))
      return;
    CircularlyAccessibleValueObject[] itemvos = null;
    try {
      for (int i = 0; i < vos.length; i++) {
        if (vos[i] == null)
          continue;
        if ((vos[i].getParentVO() != null) && (vos[i].getParentVO().getPrimaryKey() != null))
          getHsSourceHeadVos().put(vos[i].getParentVO().getPrimaryKey(), vos[i].getParentVO());
        itemvos = vos[i].getChildrenVO();
        if ((itemvos == null) || (itemvos.length <= 0))
          continue;
        for (int k = 0; k < itemvos.length; k++)
          if ((itemvos[k] != null) && (itemvos[k].getPrimaryKey() != null))
            getHsSourceBodyVos().put(itemvos[k].getPrimaryKey(), itemvos[k]);
      }
    }
    catch (Exception e) {
      SCMEnv.error(e.getMessage());
    }
  }

  public void setSourceVosToBuffer(CircularlyAccessibleValueObject[] headvos, CircularlyAccessibleValueObject[] bodyvos)
  {
    clearSourceVosBuffer();
    try {
      if (headvos != null) {
        for (int i = 0; i < headvos.length; i++) {
          if (headvos[i] == null)
            continue;
          if (headvos[i].getPrimaryKey() != null) {
            getHsSourceHeadVos().put(headvos[i].getPrimaryKey(), headvos[i]);
          }
        }
      }
      if (bodyvos != null)
        for (int i = 0; i < bodyvos.length; i++) {
          if (bodyvos[i] == null)
            continue;
          if (bodyvos[i].getPrimaryKey() != null)
            getHsSourceBodyVos().put(bodyvos[i].getPrimaryKey(), bodyvos[i]);
        }
    }
    catch (Exception e) {
      SCMEnv.error(e.getMessage());
    }
  }

  public CircularlyAccessibleValueObject getHeadSourceVoFromBuffer(String cheadid) {
    if (cheadid == null)
      return null;
    return (CircularlyAccessibleValueObject)getHsSourceHeadVos().get(cheadid);
  }

  public CircularlyAccessibleValueObject getBodySourceVoFromBuffer(String cbodyid)
  {
    if (cbodyid == null)
      return null;
    return (CircularlyAccessibleValueObject)getHsSourceBodyVos().get(cbodyid);
  }

  public AggregatedValueObject[] getSelectSourceVos()
  {
    AggregatedValueObject[] retvos = getbillListPanel().getSourceSelectedVOs(getDefaultSourceDlgCtl().getSourceVoClassName(), getDefaultSourceDlgCtl().getSourceVoHeadClassName(), getDefaultSourceDlgCtl().getSourceVoBodyClassName(), getDefaultSourceDlgCtl().getSourcevoPkname());

    if ((retvos == null) || (retvos.length <= 0)) {
      return retvos;
    }
    String cbillid = null;
    CircularlyAccessibleValueObject vo = null;
    CircularlyAccessibleValueObject[] bodyvos = null;
    for (int i = 0; i < retvos.length; i++) {
      try {
        vo = getHeadSourceVoFromBuffer(retvos[i].getParentVO().getPrimaryKey());
        if (vo != null)
          retvos[i].setParentVO(vo);
        bodyvos = retvos[i].getChildrenVO();
        for (int k = 0; k < bodyvos.length; k++) {
          vo = getBodySourceVoFromBuffer(bodyvos[k].getPrimaryKey());
          if (vo != null)
            bodyvos[k] = vo;
        }
      } catch (Exception e) {
        SCMEnv.error(e.getMessage());
      }
    }
    return retvos;
  }

  public synchronized void setOnHandWorkThread(RedunSourceDlg  aOnHandWorkThread)
  {
    this.onHandWorkThread = aOnHandWorkThread;
  }

  public RedunSourceDlg  getOnHandWorkThread()
  {
    return this.onHandWorkThread;
  }

  public synchronized void setQueryOnHandRefreshVO(OnHandRefreshVO onhandvo)
  {
    this.query_onhandvo = onhandvo;
  }

  public OnHandRefreshVO getQueryOnHandRefreshVO()
  {
    return this.query_onhandvo;
  }

  private static CacheToMapAdapter getCacheToMapAdapter()
  {
    if (hsdlgcache == null) {
      hsdlgcache = CacheToMapAdapter.getInstance("nc.ui.scm.pub.redun.RedunSourceDlg");
    }
    return hsdlgcache;
  }

  public static RedunSourceDlg getBillSourceDlg(String pk_corp, String sBizType, String srcBillType, String dstBillType, String soperator)
  {
    if (getCacheToMapAdapter() == null)
      return null;
    return (RedunSourceDlg)getCacheToMapAdapter().get(getCacheKey(pk_corp, sBizType, srcBillType, dstBillType, soperator));
  }

  private static String getCacheKey(String pk_corp, String sBizType, String srcBillType, String dstBillType, String soperator)
  {
    return ((pk_corp == null) || (pk_corp.trim().length() <= 0) ? "" : pk_corp.trim()) + ((sBizType == null) || (sBizType.trim().length() <= 0) ? "" : sBizType.trim()) + ((soperator == null) || (soperator.trim().length() <= 0) ? "" : soperator.trim()) + ((srcBillType == null) || (srcBillType.trim().length() <= 0) ? "" : srcBillType.trim()) + "TO" + ((dstBillType == null) || (dstBillType.trim().length() <= 0) ? "" : dstBillType.trim());
  }

  public static void setBillSourceDlgToCache(RedunSourceDlg dlg)
  {
    if (dlg == null)
      return;
    if (getCacheToMapAdapter() == null)
      return;
    getCacheToMapAdapter().put(getCacheKey(dlg.getPkCorp(), dlg.getBusinessType(), dlg.getBillType(), dlg.getCurrentBillType(), dlg.getOperator()), dlg);
  }

  private static String[] getBillTypeAndBiz(ButtonObject bo)
  {
    if (bo == null) {
      return null;
    }
    String tmpString = bo.getTag();
    int findIndex = tmpString.indexOf(":");

    String billType = tmpString.substring(0, findIndex);

    String businessType = tmpString.substring(findIndex + 1);
    if (businessType.trim().length() == 0) {
      businessType = null;
    }
    return new String[] { billType, businessType };
  }

  public static boolean makeFlag()
  {
    if (bIsRetVOsFromThis != null) {
      return false;
    }
    return PfUtilClient.makeFlag;
  }

  public static void childButtonClicked(ButtonObject bo, String pkCorp, String FunNode, String pkOperator, String currentBillType, Container parent)
  {
    bIsRetVOsFromThis = Boolean.FALSE;
    m_tmpRetVo = null;
    m_tmpRetVos = null;

    String[] rets = getBillTypeAndBiz(bo);

    if ((rets != null) && (rets[0] != null) && (rets[0].equalsIgnoreCase("makeflag"))) {
      Debug.debug("******自制单据******");
      PfUtilClient.makeFlag = true;
      bIsRetVOsFromThis = null;
      return;
    }

    RedunSourceDlg dlg = getBillSourceDlg(pkCorp, rets == null ? null : rets[1], rets == null ? null : rets[0], currentBillType, pkOperator);

    if (dlg != null) {
      dlg.getbillListPanel().getHeadBillModel().clearBodyData();
      dlg.getbillListPanel().getBodyBillModel().clearBodyData();

      dlg.showInvOnhand(null);
      dlg.clearInvOnhand();
      dlg.getQueyDlg().showModal();
      if (dlg.getQueyDlg().isCloseOK())
      {
        dlg.setQueyWhere(dlg.getQueyDlg().getWhereSQL());
        dlg.loadHeadData();
        dlg.showModal();
      } else {
        return;
      }
      if (dlg.getResult() == 1) {
        m_tmpRetVo = dlg.getRetVo();
        m_tmpRetVos = dlg.getRetVos();
        bIsRetVOsFromThis = Boolean.TRUE;
      } else {
        bIsRetVOsFromThis = Boolean.FALSE;
      }
      dlg.getbillListPanel().getHeadBillModel().clearBodyData();
      dlg.getbillListPanel().getBodyBillModel().clearBodyData();
      dlg.retBillVos = null;
      dlg.retBillVo = null;
    } else {
      PfUtilClient.childButtonClicked(bo, pkCorp, FunNode, pkOperator, currentBillType, parent);

      bIsRetVOsFromThis = new Boolean(PfUtilClient.isCloseOK());
      if (PfUtilClient.isCloseOK()) {
        m_tmpRetVo = PfUtilClient.getRetVo();
        m_tmpRetVos = PfUtilClient.getRetVos();
      } else {
        m_tmpRetVo = null;
        m_tmpRetVos = null;
      }
    }
  }

  public static AggregatedValueObject[] getRetsVos()
  {
    if (bIsRetVOsFromThis != null) {
      return m_tmpRetVos;
    }
    return PfUtilClient.getRetVos();
  }

  public static boolean isCloseOK()
  {
    if (bIsRetVOsFromThis != null) {
      return bIsRetVOsFromThis.booleanValue();
    }
    return PfUtilClient.isCloseOK();
  }

  public RedunSourceDlg(String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, String templateId, String currentBillType, Container parent)
  {
    super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, parent);

    this.m_whereStr = getQueryWhere();
    initialize();
    setBillSourceDlgToCache(this);
  }

  public RedunSourceDlg(String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, String templateId, String currentBillType, String nodeKey, Object userObj, Container parent)
  {
    super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId, currentBillType, nodeKey, userObj, parent);

    this.m_whereStr = getQueryWhere();
    initialize();
    setBillSourceDlgToCache(this);
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == getbtnOk())
      onOk();
    else if (e.getSource() == getbtnCancel())
      closeCancel();
    else if (e.getSource() == getbtnQuery())
      onQuery();
    else if (e.getSource() == getbtnAdd())
      onAddLine();
    else if (e.getSource() == getbtnDel())
      onDelLine();
    else if (e.getSource() == getbtnCopy())
      onCopyLine();
    else if (e.getSource() == getbtnPaste())
      onPasteLine();
    else if (e.getSource() == getbtnFastRef())
      onFastRef();
    else if (e.getSource() == getbtnRefQry())
      onRefQuery();
    else if (e.getSource() == getbtnSplitMode())
      onBtnSplitMode();
  }

  public void addBillUI()
  {
    try
    {
      if ((isShowInvHandPane()) && (getOnhandinfoPanel() != null))
      {
        getUIDialogContentPane().add(getAstPanel(), "Center");
        clearInvOnhand();
      } else {
        getUIDialogContentPane().add(getbillListPanel(), "Center");
        getUIDialogContentPane().add(getPanlCmd(), "South");
      }

      addListenerEvent();
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  public void addListenerEvent()
  {
    getbtnOk().addActionListener(this);
    getbtnCancel().addActionListener(this);
    getbtnQuery().addActionListener(this);

    getbtnAdd().addActionListener(this);
    getbtnDel().addActionListener(this);

    getbtnFastRef().addActionListener(this);
    getbtnRefQry().addActionListener(this);
    getbtnSplitMode().addActionListener(this);

    getbillListPanel().addHeadEditListener(this);
    if (isShowTargetPane()) {
      getbillListPanel().addBodyEditListener(this);
    }
    getbillListPanel().getParentListPanel().addEditListener2(this);
    if (isShowTargetPane()) {
      getbillListPanel().getChildListPanel().addEditListener2(this);
    }
    getbillListPanel().getParentListPanel().addMouseListener(this);
    if (isShowTargetPane())
      getbillListPanel().getChildListPanel().addMouseListener(this);
    getbillListPanel().addMouseListener(this);

    ListSelectionListener lsl = null ;

    getbillListPanel().getParentListPanel().getTable().getSelectionModel().addListSelectionListener(lsl);
  }

  public void afterEdit(BillEditEvent e)
  {
    if (e.getPos() == 1) {
      BillEditEvent neweven = null;
      if (e.getKey().startsWith("chd_"))
        neweven = new BillEditEvent(e.getSource(), e.getOldValue(), e.getKey().substring("chd_".length()), e.getRow(), 1);
      else {
        neweven = new BillEditEvent(e.getSource(), e.getOldValue(), e.getValue(), e.getKey(), e.getRow(), 0);
      }
      if (getDefaultTargetDlgCtl() != null)
        getDefaultTargetDlgCtl().afterEdit(neweven);
    }
  }

  public void headRowChange(int iRow)
  {
    BillModel headModel = getbillListPanel().getHeadBillModel();
    int i = 0; for (int loop = getbillListPanel().getHeadTable().getRowCount(); i < loop; i++) {
      if (headModel.getRowState(i) != -1) {
        headModel.setRowState(i, -1);
      }
    }
    int[] selrows = getbillListPanel().getHeadTable().getSelectedRows();
    if (selrows != null) {
      int i1 = 0; for (int loop = selrows.length; i1 < loop; i1++)
        if (headModel.getRowState(selrows[i1]) != 4)
          headModel.setRowState(selrows[i1], 4);
    }
  }

  public void bodyRowChange(BillEditEvent e)
  {
    if (e.getSource() == getbillListPanel().getBodyTable()) {
      if (getDefaultTargetDlgCtl() != null)
        getDefaultTargetDlgCtl().bodyRowChange(e);
    }
    else if ((isShowInvHandPane()) && (getDefaultSourceDlgCtl() != null))
      showInvOnhand(getDefaultSourceDlgCtl().getOnHandRefreshVO(e.getRow()));
  }

  protected RedunVO[] combineRedunVOs(RedunVO[] vosOne, RedunVO[] vosTwo)
  {
    if ((vosOne == null) && (vosTwo == null))
      return null;
    int iSize1 = vosOne == null ? 0 : vosOne.length;
    int iSize2 = vosTwo == null ? 0 : vosTwo.length;
    RedunVO[] retVos = new RedunVO[iSize1 + iSize2];
    for (int i = 0; i < iSize1; i++) {
      retVos[i] = new RedunVO();
      retVos[i] = vosOne[i];
    }
    for (int i = 0; i < iSize2; i++) {
      retVos[(i + iSize1)] = new RedunVO();
      retVos[(i + iSize1)] = vosTwo[i];
    }
    return retVos;
  }

  protected UISplitPane getAstPanel()
  {
    if (this.ivjAstPanel == null)
    {
      this.ivjAstPanel = new UISplitPane();
      this.ivjAstPanel.setOrientation(0);
      this.ivjAstPanel.setTopComponent(getbillListPanel());
      this.ivjAstPanel.setOneTouchExpandable(true);

      UIPanel panelxx = new UIPanel();
      panelxx.setLayout(new BorderLayout());
      getPanlCmd().setBorder(BorderFactory.createBevelBorder(1));
      panelxx.add(getPanlCmd(), "North");
      panelxx.add(getOnhandinfoPanel(), "Center");

      this.ivjAstPanel.setBottomComponent(panelxx);
      this.ivjAstPanel.setDividerLocation((int)(getSize().getHeight() * 2.0D / 3.0D));
    }

    return this.ivjAstPanel;
  }

  public RedunListPanel getbillListPanel()
  {
    if (this.m_rlpBill == null)
    {
      if (this.m_rlpBill != null) {
        this.m_rlpBill.getHeadBillModel().clearBodyData();
        this.m_rlpBill.getBodyBillModel().clearBodyData();
      }
      if (this.m_rlpBill == null) {
        try
        {
          this.m_rlpBill = new RedunListPanel(true);
          this.m_rlpBill.setName("billListPanel");

          BillTempletVO tvo = this.m_rlpBill.getDefaultTemplet(getBillType(), getBusinessType(), getOperator(), getPkCorp());

          if ((tvo != null) && (tvo.getChildrenVO() != null)) {
            BillTempletBodyVO[] bvos = (BillTempletBodyVO[])(BillTempletBodyVO[])tvo.getChildrenVO();
            for (int i = 0; i < bvos.length; i++) {
              bvos[i].setTable_code("main");
              bvos[i].setTable_name("main");
              bvos[i].setResid_tabname(null);
              bvos[i].setOptions(null);
            }
          }

          RedunListData billDataVo = new RedunListData(tvo);

          RedunListData billDataVo2 = null;
          if (isShowTargetPane()) {
            tvo = this.m_rlpBill.getDefaultTemplet(getCurrentBillType(), getBusinessType(), getOperator(), getPkCorp());

            if ((tvo != null) && (tvo.getChildrenVO() != null))
            {
              tvo = (BillTempletVO)ObjectUtils.serializableClone(tvo);

              if (getDefaultTargetDlgCtl() != null) {
                getDefaultTargetDlgCtl().alterTemplateVO(tvo);
              }
              BillTempletBodyVO[] bvos = (BillTempletBodyVO[])(BillTempletBodyVO[])tvo.getChildrenVO();
              for (int i = 0; i < bvos.length; i++) {
                bvos[i].setTable_code("body");
                bvos[i].setTable_name("body");
                bvos[i].setResid_tabname(null);
                bvos[i].setOptions(null);
              }
              billDataVo2 = new RedunListData(tvo);
            }

          }

          initUserDef(billDataVo, billDataVo2);

          this.m_rlpBill.setRedunListData(billDataVo, billDataVo2);

          initPrecition();

          initColHide();

          this.m_rlpBill.getHeadTable().setSelectionMode(2);
          if (isShowTargetPane()) {
            this.m_rlpBill.getBodyTable().setSelectionMode(2);

            getbillListPanel().getBodyUIPanel().add(getPanlCmdSrc(), "North");
          }

          if (!isShowTargetPane()) {
            getbillListPanel().getBodyUIPanel().setVisible(false);
          }

          this.m_rlpBill.setEnabled(true);
          this.m_rlpBill.getParentListPanel().setEnabled(false);
        }
        catch (Throwable ex) {
          handleException(ex);
        }
      }

      if (getDefaultSourceDlgCtl() != null) {
        getDefaultSourceDlgCtl().initOther();
      }
      if ((isShowTargetPane()) && (getDefaultTargetDlgCtl() != null)) {
        getDefaultTargetDlgCtl().initOther();
      }
    }
    return this.m_rlpBill;
  }

  protected UIButton getbtnCancel()
  {
    if (this.ivjbtnCancel == null) {
      try {
        this.ivjbtnCancel = new UIButton();
        this.ivjbtnCancel.setName("btnCancel");
        this.ivjbtnCancel.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjbtnCancel;
  }

  protected UIButton getbtnOk()
  {
    if (this.ivjbtnOk == null) {
      try {
        this.ivjbtnOk = new UIButton();
        this.ivjbtnOk.setName("btnOk");
        this.ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjbtnOk;
  }

  protected UIButton getbtnQuery()
  {
    if (this.ivjbtnQuery == null) {
      try {
        this.ivjbtnQuery = new UIButton();
        this.ivjbtnQuery.setName("btnQuery");
        this.ivjbtnQuery.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"));
      }
      catch (Throwable ivjExc) {
        handleException(ivjExc);
      }
    }
    return this.ivjbtnQuery;
  }

  protected UIButton getbtnAdd()
  {
    if (this.btnAdd == null) {
      try {
        this.btnAdd = new UIButton();
        this.btnAdd.setName("btnAdd");

        if (addicon != null) {
          this.btnAdd.setIcon(addicon);
        }

        this.btnAdd.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000002"));
      }
      catch (Throwable ex) {
        handleException(ex);
      }
    }
    return this.btnAdd;
  }

  protected UIButton getbtnCopy()
  {
    if (this.btnCopy == null) {
      try {
        this.btnCopy = new UIButton();
        this.btnCopy.setName("btnCopy");
        this.btnCopy.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000014"));
      }
      catch (Throwable ex) {
        handleException(ex);
      }
    }
    return this.btnCopy;
  }

  protected UIButton getbtnPaste()
  {
    if (this.btnPaste == null) {
      try {
        this.btnPaste = new UIButton();
        this.btnPaste.setName("btnPaste");
        this.btnPaste.setText(NCLangRes.getInstance().getStrByID("common", "粘贴行"));
      }
      catch (Throwable ex) {
        handleException(ex);
      }
    }
    return this.btnPaste;
  }

  protected UIButton getbtnFastRef()
  {
    if (this.btnFastRef == null) {
      try {
        this.btnFastRef = new UIButton();
        this.btnFastRef.setName("btnFastRef");
        this.btnFastRef.setText("快速生成");
      }
      catch (Throwable ex)
      {
        handleException(ex);
      }
    }
    return this.btnFastRef;
  }

  protected UIButton getbtnRefQry()
  {
    if (this.btnRefQry == null) {
      try {
        this.btnRefQry = new UIButton();
        this.btnRefQry.setName("btnRefQry");
        this.btnRefQry.setText(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000145"));
      }
      catch (Throwable ex) {
        handleException(ex);
      }
    }
    return this.btnRefQry;
  }

  protected UIButton getbtnDel()
  {
    if (this.btnDel == null) {
      try {
        this.btnDel = new UIButton();
        this.btnDel.setName("btnDel");
        if (delicon != null) {
          this.btnDel.setIcon(delicon);
        }

        this.btnDel.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000039"));
      }
      catch (Throwable ex) {
        handleException(ex);
      }
    }
    return this.btnDel;
  }

  public boolean getIsBusinessType()
  {
    return true;
  }

  protected UIPanel getPanlCmd()
  {
    if (this.panlCmd == null) {
      try {
        this.panlCmd = new UIPanel();
        this.panlCmd.setName("PanlCmd");
        this.panlCmd.setPreferredSize(new Dimension(0, 40));
        this.panlCmd.setLayout(new FlowLayout());

        this.panlCmd.add(getbtnOk(), getbtnOk().getName());
        this.panlCmd.add(getbtnCancel(), getbtnCancel().getName());
        if (isShowTargetPane()) {
          this.panlCmd.add(getbtnFastRef(), getbtnFastRef().getName());
        } else {
          this.panlCmd.add(getbtnQuery(), getbtnQuery().getName());
          this.panlCmd.add(getbtnRefQry(), getbtnRefQry().getName());
        }
        if (isShowSplitButton())
          this.panlCmd.add(getbtnSplitMode(), getbtnSplitMode().getName());
      }
      catch (Throwable ex) {
        handleException(ex);
      }
    }
    return this.panlCmd;
  }

  public AggregatedValueObject getRetVo()
  {
    return this.retBillVo;
  }

  public AggregatedValueObject[] getRetVos()
  {
    return this.retBillVos;
  }

  protected JPanel getUIDialogContentPane()
  {
    if (this.ivjUIDialogContentPane == null) {
      try {
        this.ivjUIDialogContentPane = new JPanel();
        this.ivjUIDialogContentPane.setName("UIDialogContentPane");
        this.ivjUIDialogContentPane.setLayout(new BorderLayout());
      }
      catch (Throwable ex)
      {
        handleException(ex);
      }
    }
    return this.ivjUIDialogContentPane;
  }

  public void getSystemPara()
  {
  }

  protected void handleException(Throwable exception)
  {
    exception.printStackTrace(System.out);
  }

  protected void initialize()
  {
    try
    {
      setName("BillSourceUI");
      setDefaultCloseOperation(2);
      Dimension dd = Toolkit.getDefaultToolkit().getScreenSize();
      int w = 900; int h = 700;
      if (dd != null) {
        w = (int)dd.getWidth() * 7 / 8;
        h = (int)dd.getHeight() * 7 / 8;
      }
      w = (w > 900) || (w <= 0) ? 900 : w;
      h = (h > 700) || (h <= 0) ? 700 : h;
      setSize(w, h);
      setTitle(NCLangRes.getInstance().getStrByID("102220", "UPP102220-000114"));

      setContentPane(getUIDialogContentPane());
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  public void loadHeadData()
  {
    try
    {
      clearSourceVosBuffer();

      getbillListPanel().getHeadBillModel().clearBodyData();
      if (isShowTargetPane()) {
        getbillListPanel().getBodyBillModel().clearBodyData();
      }

      Object[] reObjects = getDefaultSourceDlgCtl().queryAllData(this.m_whereStr);

      if ((reObjects != null) && (reObjects[0] != null) && (((Object[])(Object[])reObjects[0]).length > 0))
      {
        setSourceVosToBuffer((CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])reObjects[0], (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])reObjects[1]);

        getDefaultSourceDlgCtl().setSourceVOToUI((CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])reObjects[0], (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])reObjects[1]);

        if ((reObjects.length >= 3) && (reObjects[2] != null) && (reObjects[2].toString().trim().length() > 0)) {
          showHintMessage(reObjects[3].toString());
        }

      }

    }
    catch (Exception e)
    {
      System.out.println("数据加载失败！");
      e.printStackTrace(System.out);
      showErroMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000255"));
    }
  }

  /** @deprecated */
  protected void loadUserQuery(Container parent, String className, String pkCorp, String pkOperator, String FunNode, String businessType, String currBillType, String sourceBillType, Object userObj)
  {
    try
    {
      Class c = Class.forName(className);
      Class[] ArgsClass = { Container.class };
      Object[] Arguments = { parent };
      Constructor ArgsConstructor = c.getConstructor(ArgsClass);
      Object retObj = ArgsConstructor.newInstance(Arguments);
      ((IinitQueryData)retObj).initData(pkCorp, pkOperator, FunNode, businessType, currBillType, sourceBillType, userObj);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void mouse_doubleclick(BillMouseEnent e)
  {
    if (e.getSource() == getbillListPanel().getHeadTable())
    {
      if (isShowTargetPane()) {
        if ((isShowTargetPane()) && (getDefaultTargetDlgCtl() != null) && (getDefaultTargetDlgCtl().getClass() != DefaultTargetDlgCtl.class))
          getDefaultTargetDlgCtl().onAddLine();
      }
      else {
        int row = e.getRow();

        BillModel headModel = getbillListPanel().getHeadBillModel();

        if (headModel.getRowState(row) == 4) {
          headModel.setRowState(row, -1);
        }
        else
          headModel.setRowState(row, 4);
      }
    }
  }

  protected void onAddLine()
  {
    if (!isShowTargetPane())
      return;
    if (getDefaultTargetDlgCtl() != null)
      getDefaultTargetDlgCtl().onAddLine();
  }

  protected void onCopyLine()
  {
  }

  protected void onPasteLine()
  {
  }

  public void stopEditing()
  {
    if ((getbillListPanel().getBodyTable() != null) && (getbillListPanel().getBodyTable().isEditing()) && 
      (getbillListPanel().getBodyTable().getCellEditor() != null))
      getbillListPanel().getBodyTable().getCellEditor().stopCellEditing();
  }

  protected void onFastRef()
  {
    stopEditing();

    if (getbillListPanel().getBodyBillModel().getRowCount() <= 0) {
      showErroMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199"));

      return;
    }

    if (getDefaultTargetDlgCtl() != null)
      try
      {
        AggregatedValueObject[] billvos = getTagetVOsFromUI();

        if ((billvos == null) || (billvos.length <= 0))
          return;
        if (!getDefaultTargetDlgCtl().onCheckData(billvos)) {
          return;
        }
        if (!getDefaultTargetDlgCtl().onFastSave(billvos)) {
          return;
        }
        getbillListPanel().getBodyBillModel().clearBodyData();

        getDefaultSourceDlgCtl().refreshUISrcDataAfterTgrSave(billvos);
      }
      catch (Exception e) {
        e.printStackTrace();
        showErroMessage(e.getMessage());
      }
  }

  protected void onDelLine()
  {
    if (getDefaultTargetDlgCtl() != null)
      getDefaultTargetDlgCtl().onDelLine();
  }

  protected void onOk()
  {
    this.retBillVos = getRetVOsFromUI();

    if ((this.retBillVos == null) || (this.retBillVos.length <= 0)) {
      showErroMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000199"));

      return;
    }

    this.retBillVo = this.retBillVos[0];

    getAlignmentX();
    closeOK();
  }

  protected void close()
  {
    clearSourceVosBuffer();
    clearInvOnhand();
    super.close();
  }

  protected void onQuery()
  {
    QueryConditionClient queryCondition = getQueyDlg();
    queryCondition.showModal();
    if (queryCondition.isCloseOK())
    {
      showInvOnhand(null);

      this.m_whereStr = queryCondition.getWhereSQL();
      loadHeadData();

      getbillListPanel().setBodyValueVO(null);

      JTable table = getbillListPanel().getParentListPanel().getTable();
      int iRowCount = table.getRowCount();
      if ((iRowCount > 0) && (table.getSelectedRow() < 0))
        table.changeSelection(0, 0, false, false);
    }
  }

  protected void onRefQuery()
  {
    if (getbillListPanel().getHeadBillModel().getRowCount() <= 0)
      return;
    int selrow = getbillListPanel().getHeadTable().getSelectedRow();
    if (selrow < 0) {
      return;
    }
    String srcbillid = (String)getbillListPanel().getSourceHeadValueAt(selrow, getSourcevoPkname());

    new SourceBillFlowDlg(this, getBillType(), srcbillid, getBusinessType(), getOperator(), getPkCorp()).showModal();
  }

  public String getSourcevoPkname()
  {
    return getDefaultSourceDlgCtl().getSourcevoPkname();
  }

  public String getTargetvoPkname()
  {
    return getDefaultTargetDlgCtl().getTargetvoPkname();
  }

  public DefaultSourceDlgCtl getDefaultSourceDlgCtl()
  {
    if (this.m_defaultsourcectl == null)
      this.m_defaultsourcectl = createDefaultSourceDlgCtl(getBillType(), getCurrentBillType());
    return this.m_defaultsourcectl;
  }

  protected DefaultSourceDlgCtl createDefaultSourceDlgCtl(String soucebilltype, String targetbilltype)
  {
    return new DefaultSourceDlgCtl(this);
  }

  public DefaultTargetDlgCtl getDefaultTargetDlgCtl()
  {
    if (this.m_defaulttargetctl == null)
      this.m_defaulttargetctl = createDefaultTargetDlgCtl(getBillType(), getCurrentBillType());
    return this.m_defaulttargetctl;
  }

  protected DefaultTargetDlgCtl createDefaultTargetDlgCtl(String soucebilltype, String targetbilltype)
  {
    return new DefaultTargetDlgCtl(this);
  }

  protected void initPrecition()
  {
    if (getDefaultSourceDlgCtl() != null)
      getDefaultSourceDlgCtl().initHeadPanePrecition();
    if (getDefaultTargetDlgCtl() != null)
      getDefaultTargetDlgCtl().initBodyPanePrecition();
  }

  protected void initColHide()
  {
    if (getDefaultSourceDlgCtl() != null) {
      String[] hidecols = getDefaultSourceDlgCtl().getSourceHeadHideCol();
      if (hidecols != null) {
        for (int i = 0; i < hidecols.length; i++) {
          getbillListPanel().hideHeadTableCol(hidecols[i]);
        }
      }
      hidecols = getDefaultSourceDlgCtl().getSourceBodyHideCol();
      if (hidecols != null) {
        for (int i = 0; i < hidecols.length; i++) {
          getbillListPanel().hideHeadTableCol("chd_" + hidecols[i]);
        }
      }
    }
    if ((isShowTargetPane()) && (getDefaultTargetDlgCtl() != null)) {
      String[] hidecols = getDefaultTargetDlgCtl().getTargetHeadHideCol();
      if (hidecols != null) {
        for (int i = 0; i < hidecols.length; i++) {
          getbillListPanel().hideBodyTableCol(hidecols[i]);
        }
      }
      hidecols = getDefaultTargetDlgCtl().getTargetBodyHideCol();
      if (hidecols != null)
        for (int i = 0; i < hidecols.length; i++)
          getbillListPanel().hideBodyTableCol("chd_" + hidecols[i]);
    }
  }

  protected void initUserDef(BillListData source, BillListData target)
  {
    try
    {
      DefVO[] headdefs = null;
      DefVO[] bodydefs = null;
      if (source != null) {
        String sbilltype = getBillType();
        if (sbilltype.trim().length() == 2) {
          if (sbilltype.startsWith("5"))
            sbilltype = "tobill";
          else if (sbilltype.startsWith("4")) {
            sbilltype = "icbill";
          }
        }
        headdefs = DefSetTool.getDefHead(getPkCorp(), sbilltype);
        bodydefs = DefSetTool.getDefBody(getPkCorp(), sbilltype);
        String userkey = getDefaultSourceDlgCtl() == null ? "vdef" : getDefaultSourceDlgCtl().getSourceHeadUserDefKey();
        if (headdefs != null)
          source.updateItemByDef(headdefs, userkey, true);
        userkey = getDefaultSourceDlgCtl() == null ? "vdef" : getDefaultSourceDlgCtl().getSourceBodyUserDefKey();
        if (bodydefs != null) {
          source.updateItemByDef(bodydefs, userkey, false);
        }
      }
      if (target != null) {
        String dbilltype = getCurrentBillType();
        if (dbilltype.trim().length() == 2) {
          if (dbilltype.startsWith("5"))
            dbilltype = "tobill";
          else if (dbilltype.startsWith("4"))
            dbilltype = "icbill";
        }
        headdefs = DefSetTool.getDefHead(getPkCorp(), dbilltype);
        bodydefs = DefSetTool.getDefBody(getPkCorp(), dbilltype);
        String userkey = getDefaultTargetDlgCtl() == null ? "vdef" : getDefaultTargetDlgCtl().getTargetHeadUserDefKey();
        if (headdefs != null)
          target.updateItemByDef(headdefs, userkey, true);
        userkey = getDefaultTargetDlgCtl() == null ? "vdef" : getDefaultTargetDlgCtl().getTargetBodyUserDefKey();
        if (bodydefs != null)
          target.updateItemByDef(bodydefs, userkey, false);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showHintMessage(String hintmsg) {
    MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000132"), hintmsg);
  }

  public void showErroMessage(String errmsg)
  {
    MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000059"), errmsg);
  }

  public boolean beforeEdit(BillEditEvent e)
  {
    if ((e.getSource() != null) && (e.getSource().getClass() == BillItem.class) && (((BillItem)e.getSource()).getPos() == 1)) {
      BillEditEvent neweven = null;
      if (e.getKey().startsWith("chd_"))
        neweven = new BillEditEvent(e.getSource(), e.getOldValue(), e.getValue(), e.getKey().substring("chd_".length()), e.getRow(), 1);
      else {
        neweven = new BillEditEvent(e.getSource(), e.getOldValue(), e.getValue(), e.getKey(), e.getRow(), 0);
      }
      if (getDefaultTargetDlgCtl() != null) {
        return getDefaultTargetDlgCtl().beforeEdit(neweven);
      }
      return false;
    }
    return false;
  }

  public boolean isShowTargetPane()
  {
    if (this.m_bSCM01 == null)
      initSysParam();
    return this.m_bSCM01.booleanValue();
  }

  public boolean isShowInvHandPane()
  {
    if (this.m_bSCM02 == null)
      initSysParam();
    return this.m_bSCM02.booleanValue();
  }

  public UIPanel getOnhandinfoPanel()
  {
    if (this.m_OnhandinfoPanel == null) {
      try {
        this.m_OnhandinfoPanel = ((UIPanel)Class.forName("nc.ui.ic.pub.QueryOnHandInfoPanel").newInstance());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return this.m_OnhandinfoPanel;
  }

  public void clearInvOnhand()
  {
    if (!isShowInvHandPane())
      return;
    if (getOnhandinfoPanel() == null)
      return;
    try {
      setQueryOnHandRefreshVO(null);
      setOnHandWorkThread(null);

      getOnhandinfoPanel().getClass().getMethod("clearData", null).invoke(getOnhandinfoPanel(), null);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void showInvOnhand(OnHandRefreshVO onhandvo)
  {
    if (!isShowInvHandPane())
      return;
    if (getOnhandinfoPanel() == null) {
      return;
    }
    setQueryOnHandRefreshVO(onhandvo);
    try
    {
      /*if ((getOnHandWorkThread() != null) && (getOnHandWorkThread().getRunState() == 1)) {
        return;
      }
      setOnHandWorkThread(new RedunSourceDlg(this));
      getOnHandWorkThread().start();*/
    } catch (Exception ex) {
      ex.printStackTrace();
      setQueryOnHandRefreshVO(null);
      setOnHandWorkThread(null);
    }
  }

  public AggregatedValueObject[] getTagetVOsFromUI()
  {
    AggregatedValueObject[] retAggVos = null;
    if ((isShowTargetPane()) && (getbillListPanel().getBodyBillModel().getRowCount() > 0))
    {
      try
      {
        retAggVos = (AggregatedValueObject[])(AggregatedValueObject[])Array.newInstance(Class.forName(getDefaultSourceDlgCtl().getTargetVoClassName()), getbillListPanel().getBodyBillModel().getRowCount());

        int i = 0; for (int loop = getbillListPanel().getBodyBillModel().getRowCount(); i < loop; i++)
        {
          retAggVos[i] = getbillListPanel().getBodyRedunModel().getBodyValueBillRowVO(i, getDefaultSourceDlgCtl().getTargetVoClassName(), getDefaultSourceDlgCtl().getTargetVoHeadClassName(), getDefaultSourceDlgCtl().getTargetVoBodyClassName());
        }

      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return retAggVos;
  }

  public AggregatedValueObject[] getRetVOsFromUI()
  {
    AggregatedValueObject[] retAggVos = getTagetVOsFromUI();

    if (((retAggVos == null) || (retAggVos.length <= 0)) && (getbillListPanel().getHeadBillModel().getRowCount() > 0))
    {
      retAggVos = getbillListPanel().getSourceSelectedVOs(getDefaultSourceDlgCtl().getSourceVoClassName(), getDefaultSourceDlgCtl().getSourceVoHeadClassName(), getDefaultSourceDlgCtl().getSourceVoBodyClassName(), getSourcevoPkname());

      if ((retAggVos != null) && (retAggVos.length > 0) && (getDefaultTargetDlgCtl() != null) && (getDefaultTargetDlgCtl().isChangeVo(getBillType(), getCurrentBillType()))) {
        try
        {
          retAggVos = PfChangeBO_Client.pfChangeBillToBillArray(retAggVos, getBillType(), getCurrentBillType());
        } catch (Exception e) {
          showErroMessage(e.getMessage());
          e.printStackTrace();
        }
      }
    }
    return retAggVos;
  }

  protected UIPanel getPanlCmdSrc()
  {
    if (this.panlCmdForSrc == null) {
      try {
        this.panlCmdForSrc = new UIPanel();
        this.panlCmdForSrc.setName("panlCmdForSrc");
        this.panlCmdForSrc.setPreferredSize(new Dimension(0, 30));
        this.panlCmdForSrc.setLayout(new FlowLayout());

        if (isShowTargetPane()) {
          this.panlCmdForSrc.add(getbtnQuery(), getbtnQuery().getName());
          this.panlCmdForSrc.add(getbtnAdd(), getbtnAdd().getName());

          this.panlCmdForSrc.add(getbtnDel(), getbtnDel().getName());
          this.panlCmdForSrc.add(getbtnRefQry(), getbtnRefQry().getName());
        }
      }
      catch (Throwable ex)
      {
        handleException(ex);
      }
    }
    return this.panlCmdForSrc;
  }

  protected boolean isShowSplitButton()
  {
    return true;
  }

  protected UIButton getbtnSplitMode()
  {
    if (this.ivjbtnSplitMode == null) {
      try {
        this.ivjbtnSplitMode = new UIButton();
        this.ivjbtnSplitMode.setName("lbSplitMode");
        this.ivjbtnSplitMode.setText(NCLangRes.getInstance().getStrByID("40040401", "UPP40040401-000181"));
      }
      catch (Throwable ivjExc)
      {
      }

    }

    return this.ivjbtnSplitMode;
  }

  protected void onBtnSplitMode()
  {
  }

  public void setQueyWhere(String newWhere)
  {
    this.m_whereStr = newWhere;
  }

  public void initSysParam()
  {
    String[] para = { "SCM01", "SCM02" };
    Hashtable h = null;
    try {
      h = SysInitBO_Client.queryBatchParaValues(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), para);
    } catch (Exception e) {
      Logger.error(e.getMessage());
    }
    if (h == null) {
      h = new Hashtable(1);
    }
    Object otemp = null;
    otemp = h.get("SCM01");

    this.m_bSCM01 = ((otemp == null) || (otemp.toString().trim().length() <= 0) ? new UFBoolean(false) : new UFBoolean(otemp.toString()));

    otemp = h.get("SCM02");

    this.m_bSCM02 = ((otemp == null) || (otemp.toString().trim().length() <= 0) ? new UFBoolean(false) : new UFBoolean(otemp.toString()));
  }

  static
  {
    try
    {
      URL url = RedunSourceDlg.class.getClassLoader().getResource("images/tableImages/Down.gif");
      addicon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(url));
      url = RedunSourceDlg.class.getClassLoader().getResource("images/tableImages/Up.gif");
      delicon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(url));
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}