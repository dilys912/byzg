package nc.ui.ic.ic261;

/**
 * 创建者：顾焱
 * 创建日期：(2001-5-22 13:36:54)
 * 功能：
 * 修改日期，修改人，修改原因，注释标志：
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.vo.ic.ic261.ChoosespaceVO;
import nc.vo.pub.lang.UFBoolean;

public class ChooseSpace
	extends nc.ui.pub.beans.UIDialog
	implements BillEditListener {
	private javax.swing.JPanel ivjUIDialogContentPane= null;
	private nc.ui.pub.beans.UIButton ivjUIBtnCancel= null;
	private nc.ui.pub.beans.UIButton ivjUIBtnChsAll= null;
	private nc.ui.pub.beans.UIButton ivjUIBtnOK= null;
	
	
	// add by zip:2013/11/21 No 38
	private UIRefPane xyy_ckRef = null;
	private UIButton xyy_BtnOk = null;
	private UILabel lblSelCntMsg = null;
	// end
	
	IvjEventHandler ivjEventHandler= new IvjEventHandler();
	private nc.ui.pub.bill.BillModel ivjBillModelSpace= null;
	private boolean queryFlag= false;
	private nc.ui.pub.bill.BillScrollPane ivjBillScrollPaneSpace= null;
	private nc.ui.pub.beans.UIButton ivjUIBtnAllCancel= null;
	private ArrayList m_vos= new ArrayList();
	private String m_bodyvoname= "nc.vo.ic.ic261.ChoosespaceVO";

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == ChooseSpace.this.getUIBtnOK())
				connEtoC1();
			if (e.getSource() == ChooseSpace.this.getUIBtnCancel())
				connEtoC2();
			if (e.getSource() == ChooseSpace.this.getUIBtnChsAll())
				connEtoC3(e);
			if (e.getSource() == ChooseSpace.this.getUIBtnAllCancel())
				connEtoC4(e);
			// add by zip:2013/12/10  No 38
			if (e.getSource() == ChooseSpace.this.xyy_BtnOk)
				connEtoC5(e);
			// end
		};
	};
/**
 * ChooseSpace 构造子注解。
 */
public ChooseSpace() {
	super();
	initialize();
}
/**
 * ChooseSpace 构造子注解。
 * @param parent java.awt.Container
 */
public ChooseSpace(java.awt.Container parent) {
	super(parent);
	initialize();
}
/**
 * ChooseSpace 构造子注解。
 * @param parent java.awt.Container
 * @param title java.lang.String
 */
public ChooseSpace(java.awt.Container parent, String title) {
	super(parent, title);
	initialize();
}
/**
 * ChooseSpace 构造子注解。
 * @param owner java.awt.Frame
 */
public ChooseSpace(java.awt.Frame owner) {
	super(owner);
	initialize();
}
/**
 * ChooseSpace 构造子注解。
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public ChooseSpace(java.awt.Frame owner, String title) {
	super(owner, title);
	initialize();
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-3-23 2:02:27)
 * @param e ufbill.BillEditEvent
 */
public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
	String sKey= e.getKey().trim();
	int iRow= e.getRow();
	UFBoolean bChoose= null;
	String sNowCode= "";
	if (iRow < 0)
		return;
	// add by zip:2013/12/10 No 38
	String srcTxt =  lblSelCntMsg.getText();
	Integer len = new Integer(srcTxt.substring(5,srcTxt.length()-1));
	if((Boolean)e.getValue()) {
		len++;
	}else len--;
	lblSelCntMsg.setText("当前已选择"+len+"行");
	// end
	if (sKey.equals("flag")) {
		ChoosespaceVO[] cvo=
			(ChoosespaceVO[]) getBillModelSpace().getBodyValueVOs(m_bodyvoname);
		bChoose= cvo[iRow].getFlag();
		sNowCode= cvo[iRow].getCscode().trim();
		if (cvo[iRow].getCsendflag().trim().equals("N")) {
			for (int i= 0; i < cvo.length; i++) {
				String sCheckCode= cvo[i].getCscode().trim();
				if ((sCheckCode.length() > sNowCode.length())
					&& (sCheckCode.startsWith(sNowCode))) {
					getBillModelSpace().setValueAt(bChoose, i, "flag");
				}
			}
		}
	}
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-3-23 2:02:27)
 * @param e ufbill.BillEditEvent
 */
public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {}
/**
 * Comment
 */
public void CancelAll_onClick(java.awt.event.ActionEvent actionEvent) {
	int RowNum=getBillModelSpace().getRowCount();
	if (RowNum<=0)
	{
		return;
	}
	// add by zip:2013/12/10 No 38
	lblSelCntMsg.setText("当前已选择0行");
	// end
	for(int i=0;i<RowNum;i++)
	{
		getBillModelSpace().setValueAt(new Boolean(false),i,0);
	}
	return;
}
/**
 * Comment
 */
public void ChooseAll_onClick(java.awt.event.ActionEvent actionEvent) {
	for(int i=0;i<getBillModelSpace().getRowCount();i++)
	{
		getBillModelSpace().setValueAt(new Boolean(true),i,0);
	}
	// add by zip:2013/12/10 No 38
	lblSelCntMsg.setText("当前已选择"+getBillModelSpace().getRowCount()+"行");
	// end
	return;
}
/**
 * connEtoC1:  (UIBtnOK.action. --> ChooseSpace.uIBtnOK_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC1() {
	try {
		// user code begin {1}
		// user code end
		this.onOK();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (UIBtnCancel.action. --> ChooseSpace.uIBtnCancel_ActionEvents()V)
 */
/* 警告：此方法将重新生成。 */
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.onCancel();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (UIBtnChsAll.action.actionPerformed(java.awt.event.ActionEvent) --> ChooseSpace.uIBtnChsAll_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.ChooseAll_onClick(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (UIBtnAllCancel.action.actionPerformed(java.awt.event.ActionEvent) --> ChooseSpace.uIBtnAllCancel_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.CancelAll_onClick(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}

// add by zip:2013/11/21 No 38
private void connEtoC5(java.awt.event.ActionEvent e) {
	int rc = ivjBillModelSpace.getRowCount();
	String code = xyy_ckRef.getRefCode();
	int selCnt = 0;
	for (int i = 0; i < rc; i++) {
		String cc = (String) ivjBillModelSpace.getValueAt(i, "cscode");
		if(cc.startsWith(code)) {
			ivjBillModelSpace.setValueAt(true, i, "flag");
		}
		selCnt += ivjBillModelSpace.getValueAt(i, "flag")!= null && UFBoolean.valueOf(ivjBillModelSpace.getValueAt(i, "flag").toString()).booleanValue() ? 1 : 0;
	}
	lblSelCntMsg.setText("当前已选择"+selCnt+"行");
}
// end

/**
 * 返回 BillModelSpace 特性值。
 * @return nc.ui.pub.bill.BillModel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.bill.BillModel getBillModelSpace() {
	if (ivjBillModelSpace == null) {
		try {
			ivjBillModelSpace = new nc.ui.pub.bill.BillModel();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBillModelSpace;
}
/**
 * 返回 BillScrollPaneSpace 特性值。
 * @return nc.ui.pub.bill.BillScrollPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.bill.BillScrollPane getBillScrollPaneSpace() {
	if (ivjBillScrollPaneSpace == null) {
		try {
			ivjBillScrollPaneSpace = new nc.ui.pub.bill.BillScrollPane();
			ivjBillScrollPaneSpace.setName("BillScrollPaneSpace");
			ivjBillScrollPaneSpace.setBounds(39, 23, 344, 167);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBillScrollPaneSpace;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-6-5 17:01:31)
 * @return boolean
 */
public boolean getQueryFlag() {
	return queryFlag;
}
/**
 * 返回 UIBtnAllCancel 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnAllCancel() {
	if (ivjUIBtnAllCancel == null) {
		try {
			ivjUIBtnAllCancel = new nc.ui.pub.beans.UIButton();
			ivjUIBtnAllCancel.setName("UIBtnAllCancel");
			ivjUIBtnAllCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000042")/*@res "全消"*/);
			ivjUIBtnAllCancel.setBounds(129, 202, 70, 22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnAllCancel;
}
/**
 * 返回 UIButton3 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnCancel() {
	if (ivjUIBtnCancel == null) {
		try {
			ivjUIBtnCancel = new nc.ui.pub.beans.UIButton();
			ivjUIBtnCancel.setName("UIBtnCancel");
			ivjUIBtnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000008")/*@res "取消"*/);
//			ivjUIBtnCancel.setLocation(313, 202);
			ivjUIBtnCancel.setBounds(280,202,70,22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnCancel;
}
/**
 * 返回 UIButton1 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnChsAll() {
	if (ivjUIBtnChsAll == null) {
		try {
			ivjUIBtnChsAll = new nc.ui.pub.beans.UIButton();
			ivjUIBtnChsAll.setName("UIBtnChsAll");
			ivjUIBtnChsAll.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000041")/*@res "全选"*/);
//			ivjUIBtnChsAll.setLocation(25, 202);
			ivjUIBtnChsAll.setBounds(50,202, 70,22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnChsAll;
}
/**
 * 返回 UIButton2 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getUIBtnOK() {
	if (ivjUIBtnOK == null) {
		try {
			ivjUIBtnOK = new nc.ui.pub.beans.UIButton();
			ivjUIBtnOK.setName("UIBtnOK");
			ivjUIBtnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000044")/*@res "确定"*/);
//			ivjUIBtnOK.setLocation(221, 202);
			ivjUIBtnOK.setBounds(205,202,70,22);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBtnOK;
}
/**
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getUIDialogContentPane() {
	if (ivjUIDialogContentPane == null) {
		try {
			/* 
			 * comment by zip:2013/11/21 No 38
			ivjUIDialogContentPane = new javax.swing.JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(null);
			getUIDialogContentPane().add(getUIBtnChsAll(), getUIBtnChsAll().getName());
			getUIDialogContentPane().add(getUIBtnOK(), getUIBtnOK().getName());
			getUIDialogContentPane().add(getUIBtnCancel(), getUIBtnCancel().getName());
			getUIDialogContentPane().add(getBillScrollPaneSpace(), getBillScrollPaneSpace().getName());
			getUIDialogContentPane().add(getUIBtnAllCancel(), getUIBtnAllCancel().getName());
			*/
			// user code begin {1}
			// user code end
			
			
			// add by zip:2013/11/21 No 38
			ivjUIDialogContentPane = new javax.swing.JPanel(new BorderLayout());
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			
			
			UIPanel xyy_northPnl = new UIPanel(new FlowLayout(FlowLayout.LEFT));
			xyy_ckRef = new UIRefPane("货位档案");
			xyy_BtnOk = new UIButton("确定");
			lblSelCntMsg = new UILabel("当前已选择0行");
			xyy_northPnl.add(xyy_ckRef);
			xyy_northPnl.add(xyy_BtnOk);
			xyy_northPnl.add(lblSelCntMsg);
			ivjUIDialogContentPane.add(xyy_northPnl,BorderLayout.NORTH);
			
			ivjUIDialogContentPane.add(getBillScrollPaneSpace(),BorderLayout.CENTER);
			
			UIPanel pp = new UIPanel(new FlowLayout());
			pp.add(getUIBtnChsAll());
			pp.add(getUIBtnOK());
			pp.add(getUIBtnCancel());
			pp.add(getUIBtnAllCancel());
			ivjUIDialogContentPane.add(pp,BorderLayout.SOUTH);
			// end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIDialogContentPane;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-6-11 15:59:54)
 * @return java.util.ArrayList
 */
public ArrayList getvos() {
	return m_vos;
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
	// user code begin {1}
	// user code end
	getUIBtnOK().addActionListener(ivjEventHandler);
	getUIBtnCancel().addActionListener(ivjEventHandler);
	getUIBtnChsAll().addActionListener(ivjEventHandler);
	getUIBtnAllCancel().addActionListener(ivjEventHandler);
	xyy_BtnOk.addActionListener(ivjEventHandler);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("ChooseSpace");
		//setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(420, 260);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000154")/*@res "选择货位"*/);
		setContentPane(getUIDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	try {
		String[] saBodyColName= { nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0004044")/*@res "选择"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0003834")/*@res "货位编码"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0003833")/*@res "货位名称"*/, "id", nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000182")/*@res "是否未级货位"*/ }; //显示名称
		String[] saBodyColKeyName=
			{ "flag", "cscode", "csname", "pk_cargdoc", "csendflag" };
		//关键字

		BillItem[] biaBody= new BillItem[saBodyColName.length];

		for (int i= 0; i < saBodyColName.length; i++) {
			biaBody[i]= new BillItem();
			biaBody[i].setName(saBodyColName[i]);
			biaBody[i].setKey(saBodyColKeyName[i]);
			biaBody[i].setWidth(100);
			biaBody[i].setEnabled(true);
			biaBody[i].setEdit(false);
			biaBody[i].setDataType(BillItem.STRING);
		}
		biaBody[0].setEdit(true);
		biaBody[0].setDataType(BillItem.BOOLEAN);
		biaBody[3].setShow(false);
		biaBody[4].setShow(false);

		getBillModelSpace().setBodyItems(biaBody);

		getBillScrollPaneSpace().setTableModel(getBillModelSpace());

		getBillModelSpace().addLine();

		getBillScrollPaneSpace().addEditListener(this);

	} catch (Exception e) {
		nc.vo.scm.pub.SCMEnv.error(e);

	}

	// user code end
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		ChooseSpace aChooseSpace;
		aChooseSpace = new ChooseSpace();
		aChooseSpace.setModal(true);
		aChooseSpace.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aChooseSpace.show();
		java.awt.Insets insets = aChooseSpace.getInsets();
		aChooseSpace.setSize(aChooseSpace.getWidth() + insets.left + insets.right, aChooseSpace.getHeight() + insets.top + insets.bottom);
		aChooseSpace.setVisible(true);
	} catch (Throwable exception) {
		nc.vo.scm.pub.SCMEnv.out("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
		nc.vo.scm.pub.SCMEnv.error(exception);
	}
}
/**
 * Comment
 */
public void onCancel() {
	this.closeCancel();
	return;
}
/**
 * 确定
   将货位信息保存到公用变量m_vos
 */
public void onOK() {
	m_vos= new ArrayList();
	ChoosespaceVO[] vos=
		(ChoosespaceVO[]) getBillModelSpace().getBodyValueVOs(m_bodyvoname);
	if (vos != null && vos.length != 0) {
		for (int i= 0; i < vos.length; i++) {
			if ((vos[i].getFlag() != null) && (vos[i].getFlag().booleanValue())) {
				m_vos.add(vos[i].getPk_cargdoc().trim());
			}
		}
		if (m_vos.size() == 0) {
			nc.ui.pub.beans.MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000151")/*@res "警告！"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("4008spec","UPP4008spec-000183")/*@res "未选择任何货位！"*/);
			return;
		}
		this.closeOK();
		return;
	}
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-6-5 17:03:06)
 * @param flag boolean
 */
public void setQueryFlag(boolean flag) {
	queryFlag=flag;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-6-5 14:14:23)
 * @param vos nc.vo.ic.ic261.CargdocVO[]
 */
public void setValue(ChoosespaceVO[] vos) {
	getBillModelSpace().setBodyDataVO(vos);
}

public void setStorPK(String storPK) {
	if(xyy_ckRef.getRefModel().getRefSql().indexOf(" and bd_cargdoc.pk_stordoc") == -1) {
		xyy_ckRef.getRefModel().addWherePart(" and bd_cargdoc.pk_stordoc='"+storPK+"'");
	}
}

}