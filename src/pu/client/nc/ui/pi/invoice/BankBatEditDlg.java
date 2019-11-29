package nc.ui.pi.invoice;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.poi.poifs.property.Parent;

import nc.net.sf.json.JSONObject;
import nc.ui.bd.b28.CostsubjBO_Client;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.sm.login.ShowDialog;
import nc.vo.bd.b28.CostsubjVO;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;


public class BankBatEditDlg extends nc.ui.pub.beans.UIDialog{

	private nc.ui.pub.beans.UIButton ivjBtnCancel = null;

	private nc.ui.pub.beans.UIButton ivjBtnOK = null;
	
	private nc.ui.pub.beans.UIButton ivjBtnGetQc = null;

	private nc.ui.pub.beans.UIPanel ivjPnlMain = null;

	private nc.ui.pub.beans.UIPanel ivjPnlTop = null;

	private javax.swing.JPanel ivjUIDialogContentPane = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	private BillScrollPane m_ivjTablePane = null;

	private nc.ui.pub.beans.UIPanel ivjPnlRight = null;

	private Hashtable<String, String> ht_table = null;

	private SelectFldVO[] fieldVOs = null;
	
	int a=0;

	private List returnValue = null;
	
	public List getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(List returnValue) {
		this.returnValue = returnValue;
	}
	
	public List listValue=null;
    
	public List getListValue() {
		return listValue;
	}

	public void setListValue(List listValue) {
		this.listValue = listValue;
	}

	public boolean isOk=false;

	class IvjEventHandler implements java.awt.event.ActionListener, BillEditListener, BillEditListener2 {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == BankBatEditDlg.this.getBtnOK())
				connEtoC1();
			if (e.getSource() == BankBatEditDlg.this.getBtnCancel())
				connEtoC2();
			if (e.getSource() == BankBatEditDlg.this.getBtnGetQc())
				connEtoC3();
		};
        

		public boolean beforeEdit(BillEditEvent e) {
			List a = getListValue();
//			System.out.println(a);
			return true;
		}
		public void afterEdit(BillEditEvent e) {
//			String key = e.getKey();
//			if(key.equals("sjdkje")){
//				//ǩ�ʽ��
//				String cvmcdje = getUITablePane().getTableModel().getValueAt(i, "kdkje")==null?"0":getUITablePane().getTableModel().getValueAt(i, "kdkje").toString();
//				//ʵ�ʳ�ֽ��
//				String cvmsjje = getUITablePane().getTableModel().getValueAt(i, "sjdkje")==null?"0":getUITablePane().getTableModel().getValueAt(i, "sjdkje").toString();
//				UFDouble cvmcdje1  = new UFDouble(cvmcdje);
//				UFDouble cvmsjje1  = new UFDouble(cvmsjje);
//				if(cvmsjje1.sub(cvmcdje1).doubleValue()>0){
//				}
//				i++;
//				System.out.println("ʵ�ʵֿ۽��:"+cvmsjje);
//	    	}
	    }
		
		public void bodyRowChange(BillEditEvent e) {
		}
	};

	// ��֧��Ŀ
	private CostsubjVO getCostsubj(String name,String pk_corp) throws BusinessException {
		CostsubjVO  cvo = null;
		String wheres = " and costname = '"+name+"' ";
		CostsubjVO[] csb = CostsubjBO_Client.queryByCondition(pk_corp,wheres);
		if(csb.length>0){
			cvo = csb[0];
		}
		return 	cvo ;
	}

	
	
	/**
	 * DataSourceDlg ������ע�⡣
	 */
	public BankBatEditDlg() {
		super();
		initialize();
	}

	/**
	 * DataSourceDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public BankBatEditDlg(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * DataSourceDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 * @param title
	 *            java.lang.String
	 */
	public BankBatEditDlg(java.awt.Container parent, String title) {
		super(parent, title);
		initialize();
	}


	/**
	 * DataSourceDlg ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 */
	public BankBatEditDlg(java.awt.Frame owner) {
		super(owner);

	}

	/**
	 * DataSourceDlg ������ע�⡣
	 * 
	 * @param owner
	 *            java.awt.Frame
	 * @param title
	 *            java.lang.String
	 */
	public BankBatEditDlg(java.awt.Frame owner, String title) {
		super(owner, title);
	}
	
	/**
	 * Comment
	 */
	public void btnGetQc_ActionEvents() {
		isOk=true;
		closeOK();
		return;
	}

	/**
	 * Comment
	 */
	public void btnCancel_ActionEvents() {
		returnValue=null;
		isOk=false;
		closeCancel();
		return;
	}

	/**
	 * Comment
	 */
	public void btnOK_ActionEvents() {
		isOk=true;
		closeOK();
		return;
	}

	private String getTableCodeByName(String tablename) {
		String tablecode = null;
		Enumeration enum1 = ht_table.keys();
		while (enum1.hasMoreElements()) {
			tablecode = enum1.nextElement().toString();
			if (ht_table.get(tablecode).equals(tablename)) {
				break;
			} else {
				tablecode = null;
			}
		}
		return tablecode;
	}

	/**
	 * connEtoC1: (BtnOK.action. --> DataSourceDlg.btnOK_ActionEvents()V)
	 */
	private void connEtoC3() {
		try {
			List list = new ArrayList();
			if(a==0){
				 list = getReturnValue();
				 a++;
			}else{
				ShowDialog.showErrorDlg(this, "�쳣��ʾ", "�����ظ���ȡǩ��;");
			}
			for(int i = 0 ; i <list.size();i++){
				Map map = new HashMap();
				map = (Map) list.get(i);
				getUITablePane().addLine();
				getUITablePane().getTableModel().setValueAt(map.get("qcbh"), i, "qcbm");
				getUITablePane().getTableModel().setValueAt(map.get("kdkje"), i, "qcje");
			}
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC1: (BtnOK.action. --> DataSourceDlg.btnOK_ActionEvents()V)
	 */
	int b = 0;
	public List connEtoC1() {
		List  listResult = new ArrayList();
		try {
			this.btnOK_ActionEvents();
			for(int i = 0 ; i <getUITablePane().getTableModel().getRowCount();i++){
				JSONObject map = new JSONObject();
				String qcbm =  (getUITablePane().getTableModel().getValueAt(i, "qcbm")==null?"":getUITablePane().getTableModel().getValueAt(i, "qcbm")).toString();
				String qcje = getUITablePane().getTableModel().getValueAt(i, "qcje")==null?"":getUITablePane().getTableModel().getValueAt(i, "qcje").toString();
				String sjdkje = getUITablePane().getTableModel().getValueAt(i, "sjdkje")==null?"0":getUITablePane().getTableModel().getValueAt(i, "sjdkje").toString();
				UFDouble qc = new UFDouble(qcje);
				UFDouble sjd = new UFDouble(sjdkje);
				if(sjd.sub(qc).doubleValue()>0 && b<=0){
					ShowDialog.showErrorDlg(this.getUITablePane(), "������ʾ", "ʵ�ʵֿ۽��ܴ���ǩ�ʽ��");
					b++;
					return null;
				}else if(sjd.sub(qc).doubleValue()<=0){
					map.put("qcbm", qcbm.toString());
					map.put("qcje", qcje.toString());
					map.put("sjdkje", sjdkje.toString());
					listResult.add(map);
				}
			}
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		return listResult;
	}
	
	public List getAllValue(List <Map> mapresult){
		List<Map> list = mapresult;
		return list;
	}

	/**
	 * connEtoC2: (BtnCancel.action. -->
	 * DataSourceDlg.btnCancel_ActionEvents()V)
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC2() {
		try {
			// user code begin {1}
			// user code end
			this.btnCancel_ActionEvents();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}


	/**
	 * ���� BtnCancel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBtnCancel() {
		if (ivjBtnCancel == null) {
			try {
				ivjBtnCancel = new UIButton() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void processKeyEvent(java.awt.event.KeyEvent ev) {
						if (ev.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
							if (ev.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER || ev.getKeyChar() == java.awt.event.KeyEvent.VK_ENTER) {
								BankBatEditDlg.this.btnCancel_ActionEvents();
							}
						}
					}
				};
				ivjBtnCancel.setName("BtnCancel");
				ivjBtnCancel.setText("ȡ��");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBtnCancel;
	}

	/**
	 * ���� BtnOK ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	public nc.ui.pub.beans.UIButton getBtnOK() {
		if (ivjBtnOK == null) {
			try {
				ivjBtnOK = new UIButton() {
					public void processKeyEvent(java.awt.event.KeyEvent ev) {
						if (ev.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
							if (ev.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER || ev.getKeyChar() == java.awt.event.KeyEvent.VK_ENTER) {
								BankBatEditDlg.this.btnOK_ActionEvents();
							}
						}
					}
				};
				ivjBtnOK.setName("BtnOK");
				ivjBtnOK.setText("ȷ��");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjBtnOK;
	}
	
	/**
	 * ���� BtnOK ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	public nc.ui.pub.beans.UIButton getBtnGetQc() {
		if (ivjBtnGetQc == null) {
			try {
				ivjBtnGetQc = new UIButton() {
					public void processKeyEvent(java.awt.event.KeyEvent ev) {
						if (ev.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
							if (ev.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER || ev.getKeyChar() == java.awt.event.KeyEvent.VK_ENTER) {
								BankBatEditDlg.this.btnOK_ActionEvents();
							}
						}
					}
				};
				ivjBtnGetQc.setName("BtnGetQc");
				ivjBtnGetQc.setText("��ȡǩ��");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjBtnGetQc;
	}

	/**
	 * ���� PnlMain ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getPnlMain() {
		if (ivjPnlMain == null) {
			try {
				ivjPnlMain = new nc.ui.pub.beans.UIPanel();
				ivjPnlMain.setName("PnlMain");
				ivjPnlMain.setLayout(new java.awt.BorderLayout());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnlMain;
	}

	/**
	 * @return Returns the m_ivjTablePane.
	 */
	private BillScrollPane getUITablePane() {
		if (m_ivjTablePane == null) {
			m_ivjTablePane = new BillScrollPane();
			m_ivjTablePane.setName("Table");
			m_ivjTablePane.setRowNOShow(false);
		}
		return m_ivjTablePane;
	}

	/**
	 * ���� PnlBottom ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getPnlDown() {
		if (ivjPnlRight == null) {
			try {
				ivjPnlRight = new nc.ui.pub.beans.UIPanel();
				ivjPnlRight.setName("PnlRight");
				ivjPnlRight.setPreferredSize(new java.awt.Dimension(120, 50));
				getPnlDown().add(getBtnGetQc(), getBtnGetQc().getName());
				getPnlDown().add(getBtnOK(), getBtnOK().getName());
				getPnlDown().add(getBtnCancel(), getBtnCancel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnlRight;
	}

	/**
	 * ���� PnlTop ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getPnlTop() {
		if (ivjPnlTop == null) {
			try {
				ivjPnlTop = new nc.ui.pub.beans.UIPanel();
				ivjPnlTop.setName("PnlTop");
				ivjPnlTop.setPreferredSize(new java.awt.Dimension(0, 0));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnlTop;
	}

	/**
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getPnlTop(), "North");
				getUIDialogContentPane().add(getPnlMain(), "Center");
				getUIDialogContentPane().add(getPnlDown(), "South");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		System.out.println("--------- δ��׽�����쳣 ---------");
		exception.printStackTrace(System.out);
	}
	
	/**
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	/* ���棺�˷������������ɡ� */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getBtnGetQc().addActionListener(ivjEventHandler);
		getBtnOK().addActionListener(ivjEventHandler);
		getBtnCancel().addActionListener(ivjEventHandler);
		getUITablePane().addEditListener(ivjEventHandler);
		getUITablePane().addEditListener2(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("DataSourceDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(500, 350);
			setLocation(450, 200);
			setContentPane(getUIDialogContentPane());
			initConnections();
			initTable();
//			getUITablePane().addLine();
//			getUITablePane().getTableModel().setValueAt("1",0, "qcbm");
//			getUITablePane().getTableModel().setValueAt("1",0, "qcje");
//			getUITablePane().getTableModel().setValueAt(null,i, "sjdkje");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		try {
			getPnlMain().add(getUITablePane(), "Center");
			setTitle("ѡ��ǩ��");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initTable() {
		String[] names = {"ǩ�ʱ���","ǩ�ʽ��","ʵ�ʵֿ۽��"};
		String[] codes = {"qcbm","qcje","sjdkje"};
		BillModel oModel = new BillModel();
		BillItem[] biBodyItems = new BillItem[names.length];
		for (int i = 0; i < names.length; i++) {
			biBodyItems[i] = new BillItem();
			biBodyItems[i].setName(names[i]);
			biBodyItems[i].setKey(codes[i]);
			biBodyItems[i].setWidth(160);
			biBodyItems[i].setEdit(true);
			biBodyItems[i].setShow(true);
			biBodyItems[i].setDataType(BillItem.STRING);
		}
		biBodyItems[0].setDataType(BillItem.TEXTAREA);
		biBodyItems[0].setComboBoxData("ǩ�ʱ���");
		biBodyItems[0].setEnabled(false);
		
		biBodyItems[1].setDataType(BillItem.TEXTAREA);
		biBodyItems[1].setComboBoxData("ǩ�ʽ��");
		biBodyItems[1].setEnabled(false);
		
		biBodyItems[2].setDataType(BillItem.STRING);
		biBodyItems[2].setComboBoxData("ʵ�ʵֿ۽��");
		
		
		oModel.setBodyItems(biBodyItems);
		getUITablePane().setTableModel(oModel);
		
		getUITablePane().updateUI();
	}	


	public void keyPressed(java.awt.event.KeyEvent evt) {
		super.keyPressed(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			if (getBtnOK().hasFocus()) {
				btnOK_ActionEvents();
			} 
			else if (getBtnCancel().hasFocus()) {
				btnCancel_ActionEvents();
			} 
			else if(getBtnGetQc().hasFocus()){
				btnGetQc_ActionEvents();
			}
		} else if ((evt.getModifiers() & InputEvent.ALT_MASK) != 0) {
			if (evt.getKeyCode() == KeyEvent.VK_O) {
				btnOK_ActionEvents();
			} else if (evt.getKeyCode() == KeyEvent.VK_Z) {
				btnCancel_ActionEvents();
			}
		} else if (evt.getKeyCode() == KeyEvent.VK_F10) {
			btnOK_ActionEvents();
		} else if (evt.getKeyCode() == KeyEvent.VK_TAB) {
		} else if (evt.getKeyCode() == KeyEvent.VK_F2) {
		} else {
			return;
		}
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			BankBatEditDlg aDataSourceDlg = new BankBatEditDlg();
			aDataSourceDlg.setModal(true);
			aDataSourceDlg.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			aDataSourceDlg.show();
			java.awt.Insets insets = aDataSourceDlg.getInsets();
			aDataSourceDlg.setSize(aDataSourceDlg.getWidth() + insets.left + insets.right, aDataSourceDlg.getHeight() + insets.top + insets.bottom);
			aDataSourceDlg.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.beans.UIDialog �� main() �з����쳣");
			exception.printStackTrace(System.out);
		}
	}

	public SelectFldVO[] getFieldVOs() {
		return fieldVOs;
	}

	public void setFieldVOs(SelectFldVO[] fieldVOs) {
		this.fieldVOs = fieldVOs;
	}
	
}
