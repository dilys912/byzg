package nc.ui.ic.pub.lot;
 
import java.util.ArrayList;
import java.util.Hashtable;
 
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.ic001.BatchCodeDlg;
import nc.ui.ic.pub.bill.ClientUISortCtl;
import nc.ui.ic.pub.bill.SortClientUI;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.session.ClientLink;
 
/**
 * 此处插入类型说明。
 * 创建者：Zhang Xin
 * 创建日期：2001-05-08
 * 功能：
 * 修改日期，修改人，修改原因，注释标志：
 */
public class LotNumbDlg
 extends UIDialog
 implements java.awt.event.ItemListener,BillEditListener,nc.ui.pub.bill.BillTableMouseListener,SortClientUI {
 private javax.swing.JPanel ivjUIDialogContentPane = null;
 private UIPanel ivjUIPanel1 = null;
 private UIButton ivjBtnCancel = null;
 private UIButton ivjBtnOK = null;
 private UIButton ivjBtnQueryAll = null;
 private UIButton ivjBtnRefresh = null;
 private UIButton ivjBtnNew = null;
 private UIButton ivjBtnEdit = null;
 
 private UILabel ivjLbFreeItem1 = null;
 private UILabel ivjLbUnitName = null;
 private UITextField ivjTfFreeItem1 = null;
 private UITextField ivjTfUnitName = null;
 IvjEventHandler ivjEventHandler = new IvjEventHandler();
 
 private UICheckBox ckIsQueryZeroLot = null;
 private UILabel ivjLbInventory = null;
 private UITextField ivjTfInventory = null;
 private UITextField ivjTfWareHouse = null;
 //
 private ArrayList m_alAllData = null;
 protected ArrayList m_alFreeItemValue = null; //自由项值
 //是否多选，目前只应用库存
 private boolean m_bIsMutiSel = false;
 //1:先入后出，2:按保质期顺序。
 
 private boolean m_bIsQureyZeroLot = false; //是否查询零结存批次号。
 protected boolean m_bIsTrackedBill = false; //出库是否跟踪入库
 //使用者传入的参数
 /** 是否废品库管理 */
 protected boolean m_bisWasteWH = false;
 private Hashtable m_hashLotNumbVO = null; //存放批次号对应VO的哈希表
 private nc.vo.scm.ic.bill.InvVO m_invvo = null;
 protected Integer m_iOutPriority = null; //出库优先顺序0：先入先出
 protected Integer m_isTrackedBillflag = null; //是否跟踪到单据
 private LotNumbRefVO m_lnrvoSelVO = null; //存放用户选择批次号的LotNumbRefVO
 private Object m_params[]= null;
 protected String m_strAssistMeaUnitID = null; //辅计量ID
 private String m_strBillBodyID = null; //单据表体ID
 private String m_strBillCode = null; //单据号
 private String m_strBillHeaderID = null; //单据表头ID
 private String m_strBillType = null; //单据类型
 protected String m_strCalbodyName = null; //库存组织名称
 protected String m_strCorpID = null; //公司ID
 protected String m_strFreeItem = null;
 protected String m_strInvCode = null; //存货编码
 protected String m_strInvID = null; //存货ID
 protected String m_strInvName = null; //存货名称
 protected String m_strInvSpec = null; //规格
 protected String m_strInvType = null; //型号
 //
 //传出的参数
 private String m_strLotNumb = null; //批次号
 protected String m_strMeasUnit = null; //主计量单位
 protected String m_strNowBillHid = null;
 protected String m_strPk_calbody = null; //库存组织ID
 protected String m_strWareHouseCode = null; //仓库编码
 protected String m_strWareHouseID = null; //仓库ID
 protected String m_strWareHouseName = null; //仓库名称
 private UFDouble m_udInvAssistQty = null; //本批次结存辅数量
 private UFDouble m_udInvQty = null; //本批次结存实数量
 private UFDate m_ufdValidate = null; //失效日期
 //add by zwx 2014-12-26 
 protected String m_cspaceid=null;//货位id
 protected String m_csname=null;//货位名称
 
 private BatchCodeDlg m_BatchCodeDlg = null;
 //
 private String m_strNowSrcBid = null;
 
 private ClientLink m_cl=null;
 
 private BillCardPanel m_card=null;
 private String m_sRNodeName="400120";
 private String m_sRNodeKey="batchcode";
  
  private ClientUISortCtl m_cardBodySortCtl;//卡片表体排序控制
  
  /**
   * get
   * 创建日期：(2001-10-26 14:31:14)
   * @param key java.lang.String
   */
  protected ClientUISortCtl getCardBodySortCtl() {
    return m_cardBodySortCtl;
  }
  
    
  public BillListPanel getBillListPanel(){
    return null;
  }
  
  public void beforeSortEvent(boolean iscard,boolean ishead,String key){
    if(m_alAllData!=null && m_alAllData.size()>0){
      getCardBodySortCtl().addRelaSortData(m_alAllData);
    }
  }
  
  public void afterSortEvent(boolean iscard,boolean ishead,String key){
    if(m_alAllData!=null && m_alAllData.size()>0){
      m_alAllData = (ArrayList)getCardBodySortCtl().getRelaSortData(0);
    }
  }
 
 class IvjEventHandler implements java.awt.event.ActionListener {
  public void actionPerformed(java.awt.event.ActionEvent e) {
   if (e.getSource() == LotNumbDlg.this.getBtnOK())
    onOK();
   if (e.getSource() == LotNumbDlg.this.getBtnCancel())
    onCancel();
   if (e.getSource() == LotNumbDlg.this.getBtnRefresh())
    onRefresh();
   if (e.getSource() == LotNumbDlg.this.getBtnQueryAll())
    onQueryAll();
   if (e.getSource() == LotNumbDlg.this.getBtnNew())
    onNew();
   if (e.getSource() == LotNumbDlg.this.getBtnEdit())
    onEdit();
  };
 };
/**
 * LotNumbDlg 构造子注解。
 */
public LotNumbDlg() {
 super();
 initialize();
}
/**
 * LotNumbDlg 构造子注解。
 * @param parent java.awt.Container
 */
public LotNumbDlg(java.awt.Container parent) {
 super(parent);
 initialize();
}
/**
 * LotNumbDlg 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 */
public LotNumbDlg(java.awt.Container parent, String title) {
 super(parent, title);
 initialize();
}
/**
 * LotNumbDlg 构造子注解。
 * @param owner java.awt.Frame
 */
public LotNumbDlg(java.awt.Frame owner) {
 super(owner);
 initialize();
}
/**
 * LotNumbDlg 构造子注解。
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public LotNumbDlg(java.awt.Frame owner, String title) {
 super(owner, title);
 initialize();
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 20:47:36)
 * @return java.lang.String
 */
public String getAssQuant() {
 return null;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 20:48:20)
 * @return java.lang.String
 */
public String getAssUnit() {
 if(getSelVOs() != null && getSelVOs()[0] != null)
  return getSelVOs()[0].getCastunitid();
 else
  return null;
}
 
 
 
 
 

/**
 * 返回 BtnCancel 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnCancel() {
 if (ivjBtnCancel == null) {
  try {
   ivjBtnCancel = new nc.ui.pub.beans.UIButton();
   ivjBtnCancel.setName("BtnCancel");
   ivjBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/);
   ivjBtnCancel.setBounds(560, 321, 70, 21);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjBtnCancel;
}
 

/**
 * 返回 BtnOK 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnOK() {
 if (ivjBtnOK == null) {
  try {
   ivjBtnOK = new nc.ui.pub.beans.UIButton();
   ivjBtnOK.setName("BtnOK");
   ivjBtnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "确定"*/);
   ivjBtnOK.setBounds(480, 321, 70, 21);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjBtnOK;
}
 
 
 
/**
 * 返回 LbFreeItem1 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getLbFreeItem1() {
 if (ivjLbFreeItem1 == null) {
  try {
   ivjLbFreeItem1 = new nc.ui.pub.beans.UILabel();
   ivjLbFreeItem1.setName("LbFreeItem1");
   ivjLbFreeItem1.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000447")/*@res "自  由  项"*/);
   ivjLbFreeItem1.setBounds(350, 30, 80, 22);
   ivjLbFreeItem1.setVisible(!m_bIsMutiSel);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjLbFreeItem1;
}
 
 
 
 
 
 
 
 
 

/**
 * 返回 LbUnitName 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getLbUnitName() {
 if (ivjLbUnitName == null) {
  try {
   ivjLbUnitName = new nc.ui.pub.beans.UILabel();
   ivjLbUnitName.setName("LbUnitName");
   ivjLbUnitName.setPreferredSize(new java.awt.Dimension(62, 22));
   ivjLbUnitName.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0003527")/*@res "计量单位"*/);
   ivjLbUnitName.setBounds(0, 30, 80, 22);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjLbUnitName;
}
 

/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public String getLotNumb() {
 
 m_strLotNumb = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getVbatchcode();
 return m_strLotNumb;
 
}
 
 
 
/**
 * 返回 TfFreeItem1 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField getTfFreeItem1() {
 if (ivjTfFreeItem1 == null) {
  try {
   ivjTfFreeItem1 = new nc.ui.pub.beans.UITextField();
   ivjTfFreeItem1.setName("TfFreeItem1");
   ivjTfFreeItem1.setPreferredSize(new java.awt.Dimension(100, 20));
   ivjTfFreeItem1.setBounds(450, 30, 250, 20);
   ivjTfFreeItem1.setMaxLength(250);
   ivjTfFreeItem1.setVisible(!m_bIsMutiSel);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjTfFreeItem1;
}
 
 
 
 
 
 
 
 
 

/**
 * 返回 TfUnitName 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField getTfUnitName() {
 if (ivjTfUnitName == null) {
  try {
   ivjTfUnitName = new nc.ui.pub.beans.UITextField();
   ivjTfUnitName.setName("TfUnitName");
   ivjTfUnitName.setPreferredSize(new java.awt.Dimension(100, 20));
   ivjTfUnitName.setBounds(100, 30, 250, 20);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjTfUnitName;
}
 

/**
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getUIDialogContentPane() {
 if (ivjUIDialogContentPane == null) {
  try {
   ivjUIDialogContentPane = new javax.swing.JPanel();
   ivjUIDialogContentPane.setName("UIDialogContentPane");
   ivjUIDialogContentPane.setLayout(null);
   getUIDialogContentPane().add(getUIPanel1(), getUIPanel1().getName());
   getUIDialogContentPane().add(getBtnQueryAll(), getBtnQueryAll().getName());
   getUIDialogContentPane().add(getBtnOK(), getBtnOK().getName());
   getUIDialogContentPane().add(getBtnCancel(), getBtnCancel().getName());
   getUIDialogContentPane().add(getBtnRefresh(), getBtnRefresh().getName());
   getUIDialogContentPane().add(getBtnNew(), getBtnNew().getName());
   getUIDialogContentPane().add(getBtnEdit(), getBtnEdit().getName());
   getUIDialogContentPane().add(getBillCardPanel(), getBillCardPanel().getName());
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjUIDialogContentPane;
}
 

/**
 * 返回 UIPanel1 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getUIPanel1() {
 if (ivjUIPanel1 == null) {
  try {
   ivjUIPanel1 = new nc.ui.pub.beans.UIPanel();
   ivjUIPanel1.setName("UIPanel1");
   ivjUIPanel1.setLayout(null);
   ivjUIPanel1.setBounds(10, 0, 800, 80);
   getUIPanel1().add(getLbWareHouseID(), getLbWareHouseID().getName());
   getUIPanel1().add(getTfWareHouse(), getTfWareHouse().getName());
   getUIPanel1().add(getLbUnitName(), getLbUnitName().getName());
   getUIPanel1().add(getTfUnitName(), getTfUnitName().getName());
   getUIPanel1().add(getLbFreeItem1(), getLbFreeItem1().getName());
   getUIPanel1().add(getTfFreeItem1(), getTfFreeItem1().getName());
   getUIPanel1().add(getLbInventory(), getLbInventory().getName());
   getUIPanel1().add(getTfInventory(), getTfInventory().getName());
   getUIPanel1().add(getckIsQueryZeroLot(), getckIsQueryZeroLot().getName());
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjUIPanel1;
}
 
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {
 
 /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
 // nc.vo.scm.pub.SCMEnv.out("--------- 未捕捉到的异常 ---------");
 // nc.vo.scm.pub.SCMEnv.error(exception);
}
/**
 * 初始化连接
 * @exception java.lang.Exception 异常说明。
 */
/* 警告：此方法将重新生成。 */
private void initConnections() throws java.lang.Exception {
 getBtnQueryAll().addActionListener(ivjEventHandler);
 getBtnOK().addActionListener(ivjEventHandler);
 getBtnCancel().addActionListener(ivjEventHandler);
 getBtnRefresh().addActionListener(ivjEventHandler);
 getBtnNew().addActionListener(ivjEventHandler);
 getBtnEdit().addActionListener(ivjEventHandler);
}
 
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
 try {
  setModal(true);
  setName("LotNumbDlg");
  setSize(880, 394);
//  setResizable(false);
  setContentPane(getUIDialogContentPane());
  initConnections();
    m_cardBodySortCtl = new ClientUISortCtl(this,true,BillItem.BODY);
 } catch (java.lang.Throwable ivjExc) {
  handleException(ivjExc);
 }
 setUIEditable(false);
 getckIsQueryZeroLot().addItemListener(this);
}
 

/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
 try {
  LotNumbDlg aLotNumbDlg = null;
  aLotNumbDlg = new LotNumbDlg();
  aLotNumbDlg.setModal(true);
  aLotNumbDlg.addWindowListener(new java.awt.event.WindowAdapter() {
   public void windowClosing(java.awt.event.WindowEvent e) {
    System.exit(0);
   };
  });
  aLotNumbDlg.show();
  java.awt.Insets insets = aLotNumbDlg.getInsets();
  aLotNumbDlg.setSize(aLotNumbDlg.getWidth() + insets.left + insets.right, aLotNumbDlg.getHeight() + insets.top + insets.bottom);
  aLotNumbDlg.setVisible(true);
 } catch (Throwable exception) {
  nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
  nc.vo.scm.pub.SCMEnv.error(exception);
 }
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 18:27:15)
 */
public void onCancel() {
 //this.setVisible(false);
 //aLotNumbDlg.closeCancel();
 closeCancel();
 

 }
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 18:27:04)
 */
public void onOK() {
 //this.setVisible(false);
 getSelVO();
 if (m_lnrvoSelVO == null) {
  onCancel();
 } else {
  getLotNumb();
  getValidate();
 
  getBillBodyID();
  getBillHeaderID();
  getBillCode();
  getBillType();
 
  closeOK();
 }
}
 
private void onNew(){
 String pk_invmandoc = getInvID();
 getBatchCodeDlg().setData(pk_invmandoc,null);
 if(getBatchCodeDlg().showModal()==UIDialog.ID_CANCEL)
  return;
 
 //重新刷新界面
 onRefresh();
}
 
private void onEdit(){
 if(getSelVO()==null)
  return;
 String pk_invmandoc = getInvID();
 String vbatchcode = getSelVO().getVbatchcode();
 getBatchCodeDlg().setData(pk_invmandoc,vbatchcode);
 if(getBatchCodeDlg().showModal()==UIDialog.ID_CANCEL)
  return;
 onRefresh();
}
 
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 20:51:55)
 */
public void setUIEditable(boolean b) {
 if (b) {
  getTfWareHouse().setEnabled(true);
  getTfFreeItem1().setEnabled(true);
  getTfInventory().setEnabled(true);
  getTfUnitName().setEnabled(true);
 } else {
  getTfWareHouse().setEnabled(false);
  getTfFreeItem1().setEnabled(false);
  getTfInventory().setEnabled(false);
  getTfUnitName().setEnabled(false);
 }
 
}
/**
 * 在表格中显示数据。
 * 创建日期：(2001-5-8 20:50:59)
 */
public void setVOtoBody() {
// LotNumbRefVO[] voaAllData = null;
 
// if (isTrackedBill()) {
//  getUITablePane1().getTable().setModel(getTrackedTableModel());
//  voaAllData = getTrackedTableModel().getAllData();
//
// } else {
//  getUITablePane1().getTable().setModel(getNotTrackedTableModel());
//  voaAllData = getNotTrackedTableModel().getAllData();
// }
// getUITablePane1().getTable().setModel(getBillCardPanel().getBillModel());
 
 //test注释
// voaAllData = (LotNumbRefVO[])getBillCardPanel().getBillModel().getBodyValueVOs(LotNumbRefVO.class.getName());
// 
// if (voaAllData != null && voaAllData.length > 0) {
//  getUITablePane1().getTable().setRowSelectionInterval(0, 0);
//  setSelVO(voaAllData[0]);
// }
 
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-5-8 20:50:45)
 */
public void setVOtoHeader() {
 if (m_strWareHouseID != null) {
  getLbWareHouseID().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000448")/*@res "仓    库"*/);
  getTfWareHouse().setText(
   getWareHouseCode() == null
    ? getWareHouseName()
    : (getWareHouseCode() + ", " + getWareHouseName()));
 } else {
  getLbWareHouseID().setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001825")/*@res "库存组织"*/);
  getTfWareHouse().setText(getCalbodyName());
 }
 
 getTfInventory().setText(
  (getInvCode() == null? "": (getInvCode() + ", ")) +
  (getInvName() == null? "": (getInvName() + ", ")) +
  (getInvSpec() == null? "": (getInvSpec() + ", ")) +
  (getInvType() == null? "": getInvType()));
 
 getTfUnitName().setText(getMeasUnit() == null ? "" : getMeasUnit());
 
 if (m_strFreeItem != null && m_strFreeItem.trim().length() > 0) {
  getTfFreeItem1().setText(m_strFreeItem.trim());
 } else {
  getTfFreeItem1().setText("");
 
 }
 return;
 
}
 
 private UILabel ivjLbWareHouseID = null;
 
 
 

/**
 * 返回 LbWareHouseID 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getLbWareHouseID() {
 if (ivjLbWareHouseID == null) {
  try {
   ivjLbWareHouseID = new nc.ui.pub.beans.UILabel();
   ivjLbWareHouseID.setName("LbWareHouseID");
   ivjLbWareHouseID.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000153")/*@res "仓库"*/);
   ivjLbWareHouseID.setBounds(0, 4, 80, 22);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjLbWareHouseID;
}
 
/**
 * LotNumbDlg 构造子注解。
 * @param parent java.awt.Container
 */
public LotNumbDlg(java.awt.Container parent,boolean isMutiSel) {
 super(parent);
 m_bIsMutiSel = isMutiSel;
 initialize();
}
 
/**
 * LotNumbDlg 构造子注解。
 */
public LotNumbDlg(boolean isMutiSel) {
 super();
 m_bIsMutiSel = isMutiSel;
 initialize();
}
 
/**
 * 根据传入本张单据表头主键,从alAllData中将入库单据表头主键与之相同的记录行过滤.
 * 创建日期：(2003-01-05 9:43:01)
 */
private ArrayList filterNowBill(ArrayList alAllData, String sNowBillHid) {
 int place = -1;
 if (isTrackedBill() && m_strNowBillHid != null && m_strNowBillHid.trim().length() > 0) {
  for (int i = 0; i < alAllData.size(); i++) {
   String sHid = ((nc.vo.ic.pub.lot.LotNumbRefVO) alAllData.get(i)).getCgeneralhid();
   if (sNowBillHid.equalsIgnoreCase(sHid)) {
    place = i;
    break;
 
   }
  }
  if (place != -1 && (place >= 0 || place < alAllData.size())) {
   alAllData.remove(place);
  }
 }
 return alAllData;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-12 22:27:08)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getAssistMeaUnitID() {
 return m_strAssistMeaUnitID;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getBillBodyID() {
 
 m_strBillBodyID = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getCgeneralbid();
 return m_strBillBodyID;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getBillCode() {
 
 m_strBillCode = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getVbillcode();
 return m_strBillCode;
 
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getBillHeaderID() {
 
 m_strBillHeaderID = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getCgeneralhid();
 return m_strBillHeaderID;
 
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getBillType() {
 
 m_strBillType = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getCbilltypecode();
 return m_strBillType;
 
}
 
/**
 * 返回 BtnRefresh 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnQueryAll() {
 if (ivjBtnQueryAll == null) {
  try {
   ivjBtnQueryAll = new nc.ui.pub.beans.UIButton();
   ivjBtnQueryAll.setName("BtnQueryAll");
   ivjBtnQueryAll.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000449")/*@res "所有批次"*/);
   ivjBtnQueryAll.setBounds(400, 321, 70, 21);
   ivjBtnQueryAll.setVisible(m_bIsMutiSel);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 
 return ivjBtnQueryAll;
}
 
private nc.ui.pub.beans.UIButton getBtnNew() {
 if (ivjBtnNew == null) {
  try {
   ivjBtnNew = new nc.ui.pub.beans.UIButton();
   ivjBtnNew.setName("BtnNew");
   ivjBtnNew.setText("新增");
   ivjBtnNew.setBounds(240, 321, 70, 21);
   ivjBtnNew.setVisible(m_bIsMutiSel);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 
 return ivjBtnNew;
}
private nc.ui.pub.beans.UIButton getBtnEdit() {
 if (ivjBtnEdit == null) {
  try {
   ivjBtnEdit = new nc.ui.pub.beans.UIButton();
   ivjBtnEdit.setName("BtnEdit");
   ivjBtnEdit.setText("修改");
   ivjBtnEdit.setBounds(320, 321, 70, 21);
   ivjBtnEdit.setVisible(m_bIsMutiSel);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 
 return ivjBtnEdit;
}
 
/**
 * 返回 BtnRefresh 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBtnRefresh() {
 if (ivjBtnRefresh == null) {
  try {
   ivjBtnRefresh = new nc.ui.pub.beans.UIButton();
   ivjBtnRefresh.setName("BtnRefresh");
   ivjBtnRefresh.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000009")/*@res "刷新"*/);
   ivjBtnRefresh.setBounds(640, 321, 70, 21);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjBtnRefresh;
}
 
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-06-11 15:24:57)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 */
public java.lang.String getCalbodyName() {
 return m_strCalbodyName;
}
 
/**
 * 返回 BtnOK 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private UICheckBox getckIsQueryZeroLot() {
 if (ckIsQueryZeroLot == null) {
  try {
   ckIsQueryZeroLot = new UICheckBox();
   ckIsQueryZeroLot.setName("ckIsQueryZeroLot");
   ckIsQueryZeroLot.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000450")/*@res "是否查询零结存批次号"*/);
   ckIsQueryZeroLot.setBounds(0, 56, 500, 20);
   ckIsQueryZeroLot.setHorizontalAlignment(UICheckBox.EAST);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ckIsQueryZeroLot;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-12 22:27:08)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getCorpID() {
 return m_strCorpID;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-12 11:21:14)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.util.ArrayList
 */
public ArrayList getFreeItemValue() {
 return m_alFreeItemValue;
}
 
/**
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-5 9:25:48)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.scm.ic.bill.FreeVO
 */
public nc.vo.scm.ic.bill.FreeVO getFreeVO() {
 if(getSelVOs() != null && getSelVOs()[0] != null)
  return getSelVOs()[0].getFreeVO();
 else
  return null;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-13 20:25:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.util.Hashtable
 */
public java.util.Hashtable getHTLotNumbVO() {
 return m_hashLotNumbVO;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-15 16:43:27)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.pub.lang.UFDouble
 */
public UFDouble getInvAssistQty() {
 
  m_udInvAssistQty = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getNinassistnum();
  return m_udInvAssistQty;
 
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvCode() {
 return m_strInvCode;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvID() {
 return m_strInvID;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvName() {
 return m_strInvName;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-15 16:39:35)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public UFDouble getInvQty() {
 
 m_udInvQty = m_lnrvoSelVO==null? null:m_lnrvoSelVO.getNinnum();
 return m_udInvQty;
 
 }
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvSpec() {
 return m_strInvSpec;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getInvType() {
 return m_strInvType;
}
 
/**
 * 返回 LbInventoryID 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getLbInventory() {
 if (ivjLbInventory == null) {
  try {
   ivjLbInventory = new nc.ui.pub.beans.UILabel();
   ivjLbInventory.setName("LbInventory");
   ivjLbInventory.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000451")/*@res "存    货"*/);
   ivjLbInventory.setBounds(350, 4, 80, 22);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjLbInventory;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getMeasUnit() {
 return m_strMeasUnit;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-4 16:40:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return javax.swing.table.TableModel
 */
//public NotTrackedTableModel getNotTrackedTableModel() {
// if (m_tmNotTracked == null)
//  m_tmNotTracked = new NotTrackedTableModel(getBCBillData());
// return m_tmNotTracked;
//
//}
 
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-05-21 11:35:07)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.Integer
 */
protected java.lang.Integer getOutPriority() {
 return m_iOutPriority;
}
 
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-06-11 15:24:57)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.lang.String
 */
public java.lang.String getPk_calbody() {
 return m_strPk_calbody;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-22 8:59:58)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.ic.pub.lot.LotNumbRefVO
 */
public LotNumbRefVO getSelVO() {
 return m_lnrvoSelVO;
}
 
/**
 * 功能：返回选中的批次VO
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-4 18:26:40)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.ic.pub.lot.LotNumbRefVO[]
 */
public LotNumbRefVO[] getSelVOs() {
 LotNumbRefVO[] voRet = null;
 int[] isel = getBillCardPanel().getBillTable().getSelectedRows();
 voRet = new LotNumbRefVO[isel.length];
 for(int i=0;i<isel.length;i++){
  voRet[i] = (LotNumbRefVO)m_alAllData.get(isel[i]);
 }
 return voRet;
}
 
/**
 * 返回 TfInventoryID 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField getTfInventory() {
 if (ivjTfInventory == null) {
  try {
   ivjTfInventory = new nc.ui.pub.beans.UITextField();
   ivjTfInventory.setName("TfInventory");
   ivjTfInventory.setPreferredSize(new java.awt.Dimension(100, 20));
   ivjTfInventory.setBounds(450, 4, 250, 20);
   ivjTfInventory.setMaxLength(100);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjTfInventory;
}
 
/**
 * 返回 TfWareHouseID 特性值。
 * @return nc.ui.pub.beans.UITextField
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextField getTfWareHouse() {
 if (ivjTfWareHouse == null) {
  try {
   ivjTfWareHouse = new nc.ui.pub.beans.UITextField();
   ivjTfWareHouse.setName("TfWareHouse");
   ivjTfWareHouse.setPreferredSize(new java.awt.Dimension(100, 20));
   ivjTfWareHouse.setBounds(100, 4, 250, 20);
   ivjTfWareHouse.setMaxLength(100);
  } catch (java.lang.Throwable ivjExc) {
   handleException(ivjExc);
  }
 }
 return ivjTfWareHouse;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-11 11:31:45)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public String getTitle() {
 return nc.ui.ml.NCLangRes.getInstance().getStrByID("4008other","UPP4008other-000452")/*@res "批次号参照"*/;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-4 16:40:05)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return javax.swing.table.TableModel
 */
//public TrackedTableModel getTrackedTableModel() {
// if (m_tmTracked == null)
//  m_tmTracked = new TrackedTableModel();
// return m_tmTracked;
//
//}
 
/**
 * 返回 UITablePane1 特性值。
 * @return nc.ui.pub.beans.UITablePane
 */
/* 警告：此方法将重新生成。 */
//private nc.ui.pub.beans.UITablePane getUITablePane1() {
// if (ivjUITablePane1 == null) {
//  try {
//   ivjUITablePane1 = new nc.ui.pub.beans.UITablePane();
//   ivjUITablePane1.setName("UITablePane1");
//   ivjUITablePane1.setBounds(10, 84, 880, 221);
//   if(m_bIsMutiSel){
//    ivjUITablePane1.getTable().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//   }else{
//    ivjUITablePane1.getTable().getSelectionModel().setSelectionMode(javax.swing.DefaultListSelectionModel.SINGLE_SELECTION);
//   }
//   ivjUITablePane1.getTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
//   ivjUITablePane1.getTable().addMouseListener(this);
//   ivjUITablePane1.getTable().addSortListener();
//  } catch (java.lang.Throwable ivjExc) {
//   handleException(ivjExc);
//  }
// }
// return ivjUITablePane1;
//}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return nc.vo.pub.lang.UFDate
 */
public UFDate getValidate() {
 
 m_ufdValidate = m_lnrvoSelVO==null?null:m_lnrvoSelVO.getDvalidate();
 return m_ufdValidate;
 
}

//add by zwx 2014-12-26 

public String getCspaceid(){
	m_cspaceid=m_lnrvoSelVO==null?null:m_lnrvoSelVO.getCspaceid();
	return m_cspaceid;
}

public String getCsname(){
	m_csname=m_lnrvoSelVO==null?null:m_lnrvoSelVO.getCsname();
	return m_csname;
}

//end 
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getWareHouseCode() {
 return m_strWareHouseCode;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getWareHouseID() {
 return m_strWareHouseID;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return java.lang.String
 */
public java.lang.String getWareHouseName() {
 return m_strWareHouseName;
}
 
/**
 * 由VO传入库存管理中的该存货的批次是否按照先进先出显示
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：false 表明该存货是后进先出。
 * 例外：
 * 日期：(2001-6-18 14:04:36)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return boolean
 */
public boolean isFIFO1() {
 if (getOutPriority() != null && getOutPriority().intValue() == 1) { // 1=LIFO
  return false;
 } else {
  return true;
 }
}
 
/**
 *  * 功能：得到是否跟踪入库单。
 
 * 创建日期：(2001-5-8 18:35:15)
 * @return boolean
 */
public boolean isTrackedBill() {
 
 //if(getIsTrackedBill()){
 
 //return true;
 //}else{
 
 ////1、 出库批次参照依据初始参数设置，按先进先出或后进先出显示，用户可任意选择批次出库。
 ////2、 批次跟踪是否到单据号：1）按批号汇总结存，不跟踪到入库单据。
 ////2）按单据号+批号显示明细结存，跟踪到入库单据。
 
 //return false;
 //}
 //return true; //for test
 return m_bIsTrackedBill;
}
 
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-8-1 15:01:21)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @return boolean
 */
public boolean isWasteWH() {
 return m_bisWasteWH;
}
 
/**
 * Invoked when an item has been selected or deselected.
 * The code written for this method performs the operations
 * that need to occur when an item is selected (or deselected).
 */
public void itemStateChanged(java.awt.event.ItemEvent e) {
 m_bIsQureyZeroLot = getckIsQueryZeroLot().isSelected();
 onRefresh();
 
}
 
/**
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-4 17:19:19)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void onQueryAll() {
 m_params = new Object[]{
   m_strCorpID,
   m_strWareHouseID,
   m_strInvID,
   null,
   new Boolean(m_bisWasteWH),
   new Boolean(isTrackedBill()),
   m_iOutPriority,
   m_strPk_calbody,
   new Boolean(m_bIsQureyZeroLot),
   new Boolean(m_bIsMutiSel),
   getStrNowSrcBid()};
 ArrayList alBack = m_alFreeItemValue;
 m_alFreeItemValue = null;
 setData(); //
 m_alFreeItemValue = alBack;
 //setStrNowSrcBid(null);
 setVOtoBody(); //重新将数据放入表体
// getUITablePane1().getTable().repaint();
// getUITablePane1().getTable().updateUI();
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-12 16:55:10)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void onRefresh() {
 setData(); //
 setVOtoBody(); //重新将数据放入表体
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-12 22:27:08)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strAssistMeaUnitID java.lang.String
 */
public void setAssistMeaUnitID(java.lang.String newM_strAssistMeaUnitID) {
 m_strAssistMeaUnitID = newM_strAssistMeaUnitID;
}
 
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-06-11 15:24:57)
 * 修改日期，修改人，修改原因，注释标志：
 * @param newCalbodyName java.lang.String
 */
public void setCalbodyName(java.lang.String newCalbodyName) {
 m_strCalbodyName = newCalbodyName;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-7-12 22:27:08)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strCorpID java.lang.String
 */
public void setCorpID(java.lang.String newM_strCorpID) {
 m_strCorpID = newM_strCorpID;
}
 
/**
 * 根据传入的仓库和存货ID从数据库表头、表体中查出相关数据
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:38:16)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void setData() {
 if (((m_strWareHouseID == null || m_strWareHouseID.trim().length() <= 0)
  && (m_strPk_calbody == null || m_strPk_calbody.trim().length() <= 0))
  || m_strInvID == null
  || m_strInvID.trim().length() <= 0) {
  return;
 
 }
 Object params[] =
  {
   m_strCorpID,
   m_strWareHouseID,
   m_strInvID,
   m_strAssistMeaUnitID,
   new Boolean(m_bisWasteWH),
   new Boolean(isTrackedBill()),
   m_iOutPriority,
   m_strPk_calbody,
   new Boolean(m_bIsQureyZeroLot),
   new Boolean(m_bIsMutiSel),
   getStrNowSrcBid()};
 if(m_params != null)
  params = m_params;
 try {
  m_alAllData = LotNumbRefHelper.queryAllLotNum(params, m_alFreeItemValue);
  //nc.vo.scm.ic.bill.InvVO voInv = Inv
  //应该将传入的参数带到Server端，如: 仓库编码，存货编码
  LotNumbRefVO[] voaAllData = null;
  if (m_alAllData != null && m_alAllData.size() > 0) {
   //m_isTrackedBillflag = (Integer) m_alAllData.get(0);
   //m_alAllData.remove(0);
   //如果存货是跟踪到入库单的存货且传入的本单据表体主键有值,则调用过滤本单据主键的方法.
   if (isTrackedBill() && m_strNowBillHid != null && m_strNowBillHid.trim().length() > 0) {
    m_alAllData = filterNowBill(m_alAllData, m_strNowBillHid);
   }
   voaAllData = new LotNumbRefVO[m_alAllData.size()];
   m_alAllData.toArray(voaAllData);
 
   m_hashLotNumbVO = new Hashtable();
   for (int i = 0; i < voaAllData.length; i++) {
    voaAllData[i].setAttributeValue("cinventoryid", m_strInvID);
    m_hashLotNumbVO.put(voaAllData[i].getVbatchcode() == null ? "" : voaAllData[i].getVbatchcode(), voaAllData[i]);
    if(m_invvo != null)
     voaAllData[i].setFreeVO(m_invvo.getFreeItemVO());
   }
  
   BatchCodeDefSetTool.execFormulaBatchCode(voaAllData);
  } else {
   //add by zss 原因：期初余额录入批次号存货单据后，期初借出可参照出此批次号，
   //但这时期初余额的存货单据立刻删除，在期初借出中不能参照出此批次号存货，
   //但手工输入批次号能带出被删掉的存货信息(哈希表中数据未更新）
   m_hashLotNumbVO = null;
   voaAllData = null;
  }
  getBillCardPanel().getBillModel().setBodyDataVO(voaAllData);
  getBillCardPanel().updateValue();
 
 } catch (Exception e) {
  m_params = null;
  nc.vo.scm.pub.SCMEnv.error(e);
  nc.vo.scm.pub.SCMEnv.out("Can't get Data from server!");
 }
 m_params = null;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strFreeItem java.lang.String
 */
public void setFreeItem(String FreeItem) {
 m_strFreeItem = FreeItem;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-12 11:25:53)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void setFreeItemValue(ArrayList FreeItemValue) {
 
 m_alFreeItemValue = FreeItemValue;
 }
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strInventoryCode java.lang.String
 */
public void setInvCode(java.lang.String newInvCode) {
 m_strInvCode = newInvCode;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strInventoryID java.lang.String
 */
public void setInvID(String newInvID) {
 m_strInvID = newInvID;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strInventoryName java.lang.String
 */
public void setInvName(String newInvName) {
 m_strInvName = newInvName;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strSpec java.lang.String
 */
public void setInvSpec(String newstrSpec) {
 m_strInvSpec = newstrSpec;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strType java.lang.String
 */
public void setInvType(String newstrType) {
 m_strInvType = newstrType;
}
 
/**
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2005-1-4 16:50:29)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_invvo nc.vo.scm.ic.bill.InvVO
 */
public void setInvvo(nc.vo.scm.ic.bill.InvVO newM_invvo) {
 m_invvo = newM_invvo;
}
 
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-05-21 11:35:07)
 * 修改日期，修改人，修改原因，注释标志：
 * @param newIsTrackedBill boolean
 */
protected void setIsTrackedBill(boolean newIsTrackedBill) {
 m_bIsTrackedBill = newIsTrackedBill;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strMeasUnit java.lang.String
 */
public void setMeasUnit(java.lang.String newM_strMeasUnit) {
 m_strMeasUnit = newM_strMeasUnit;
}
 
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-05-21 11:35:07)
 * 修改日期，修改人，修改原因，注释标志：
 * @param newOutPriority java.lang.Integer
 */
protected void setOutPriority(java.lang.Integer newOutPriority) {
 m_iOutPriority = newOutPriority;
}
 
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-06-11 15:24:57)
 * 修改日期，修改人，修改原因，注释标志：
 * @param newPk_calbody java.lang.String
 */
public void setPk_calbody(java.lang.String newPk_calbody) {
 m_strPk_calbody = newPk_calbody;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-14 1:35:57)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_lnrvoSelVO nc.vo.ic.pub.lot.LotNumbRefVO
 */
public void setSelVO(nc.vo.ic.pub.lot.LotNumbRefVO newM_lnrvoSelVO) {
 m_lnrvoSelVO = newM_lnrvoSelVO;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-6-15 14:19:09)
 * 修改日期，修改人，修改原因，注释标志：
 *
 */
public void setVOtoUI() {
 setVOtoHeader();
 //将数据放入表头、表体
 setVOtoBody();
 
 
 

}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strWareHouseCode java.lang.String
 */
public void setWareHouseCode(java.lang.String newM_strWareHouseCode) {
 m_strWareHouseCode = newM_strWareHouseCode;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strWareHouseID java.lang.String
 */
public void setWareHouseID(java.lang.String newM_strWareHouseID) {
 m_strWareHouseID = newM_strWareHouseID;
}
 
/**
 * 此处插入方法说明。
 * 创建者：张欣
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-5-16 13:47:22)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newM_strWareHouseName java.lang.String
 */
public void setWareHouseName(java.lang.String newM_strWareHouseName) {
 m_strWareHouseName = newM_strWareHouseName;
}
 
/**
 * 此处插入方法说明。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2001-8-1 15:01:21)
 * 修改日期，修改人，修改原因，注释标志：
 *
 * @param newWasteWH boolean
 */
public void setWasteWH(boolean newWasteWH) {
 m_bisWasteWH = newWasteWH;
}
 
private BatchCodeDlg getBatchCodeDlg(){
 if(m_BatchCodeDlg==null){
  m_BatchCodeDlg=new BatchCodeDlg();
 }
 return m_BatchCodeDlg;
}
 
public String getStrNowSrcBid() {
 return m_strNowSrcBid;
}
public void setStrNowSrcBid(String nowSrcBid) {
 m_strNowSrcBid = nowSrcBid;
}
 
/**
 * 取得系统信息。
 */
private ClientLink getCEnvInfo() {
 if(m_cl==null){
  nc.ui.pub.ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
  m_cl=new ClientLink(ce);
 }
 return m_cl;
 
}
 
public BillCardPanel getBillCardPanel(){
 if(m_card==null){
  try {
   m_card = new nc.ui.pub.bill.BillCardPanel();
   m_card.setName("BillCardPanel");
   m_card.setBounds(10, 84, 780, 221);
   
   BillData bd = new BillData(m_card.getTempletData(m_sRNodeName, null, getCEnvInfo().getUser(), getCEnvInfo().getCorp(),m_sRNodeKey));
   if (bd == null) {
    nc.vo.scm.pub.SCMEnv.out("--> billdata null.");
    return m_card;
   }
   //自定义项
   BatchCodeDefSetTool.changeBillDataByBCDef(m_cl.getCorp(), bd);
 
   m_card.setBillData(bd);
   
   m_card.getBodyPanel().setBBodyMenuShow(false);
   m_card.getBodyPanel().setRowNOShow(false);
   m_card.setEnabled(false);
   m_card.setBodyMenuShow(false);
   m_card.addEditListener(this);
   m_card.addBodyMouseListener(this);
   
 } catch (java.lang.Throwable ivjExc) {
  handleException(ivjExc);
 }
 
 }
 return m_card;
}
public void afterEdit(BillEditEvent arg0) {
 // TODO Auto-generated method stub
 
}
public void bodyRowChange(BillEditEvent arg0) {
 int selrow = getBillCardPanel().getBillTable().getSelectedRow();
 
 if (selrow != -1) {
  m_lnrvoSelVO=(LotNumbRefVO)getBillCardPanel().getBillModel().getBodyValueVOs(LotNumbRefVO.class.getName())[selrow];
 }
 
}
public void mouse_doubleclick(BillMouseEnent arg0) {
 onOK();
 
}
 
}