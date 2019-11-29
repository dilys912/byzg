package nc.ui.ic.isolation;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;


import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.NCTableModel;
import nc.vo.ic.pub.bill.GeneralBillVO;

public class ShowDiaoboSalesOutResult extends nc.ui.pub.beans.UIDialog implements java.awt.event.ActionListener, MouseListener{
	
	private static final long serialVersionUID = 1L;
	private nc.ui.pub.beans.UITable jtable = null;//显示“新账号”
	private NCTableModel tablemodel=null;
	private nc.ui.pub.beans.UIPanel ivjUIPanel1 = null;
	private nc.ui.pub.beans.UIPanel ivjUIPanel2 = null;
	private nc.ui.pub.beans.UIButton ivjUIButton1 = null;//按钮“确认”
	private nc.ui.pub.beans.UIButton ivjUIButton2 = null;//“取消”
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private ToftPanel toftPanel=null;
	private ArrayList list =null;
//	InvOnHandItemVO[]result=null;
	String result[]=null;
	String liang[] ;

	String tou[][] ;

	public ShowDiaoboSalesOutResult(ToftPanel toftPanel,ArrayList list ) {
		// TODO Auto-generated constructor stub
		
		super(toftPanel);
		this.toftPanel=toftPanel;
		this.list=list;
		
		
		setName("AddAccount");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 500);
		setTitle("销售退货查询Sales returns queries");
		setContentPane(getUIDialogContentPane());
		jtable.addMouseListener(this);
		jtable.addSortListener();
		JScrollPane ak = new JScrollPane(jtable);
		ak.setBounds(40, 5, 400, 200);
		this.add(ak);
	}
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getUIPanel1(), "Center");
				getUIDialogContentPane().add(getUIPanel2(), "South");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}
	private nc.ui.pub.beans.UIPanel getUIPanel2() {
		if (ivjUIPanel2 == null) {
			try {
				ivjUIPanel2 = new nc.ui.pub.beans.UIPanel();
				ivjUIPanel2.setName("UIPanel2");
				getUIPanel2().add(getUIButton1(), getUIButton1().getName());
				getUIPanel2().add(getUIButton2(), getUIButton2().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanel2;
	}
	private NCTableModel getTableModel() {
		// TODO Auto-generated method stub
		if (tablemodel == null) {
			try {
				tablemodel = new NCTableModel();
				String cname[]=new String[]{"客户","仓库编号","产品名称","存货主键","批次号"};
				String cname_en[]=new String[]{"Customers", "Warehouse number", "Product name", "The primary key of inventories", "Batch number"};
				String [][] date;
				int length=0;
				if(list!=null && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						GeneralBillVO vo=(GeneralBillVO) list.get(i);
						length=length+vo.getChildrenVO().length;
					}
				}
				date=new String[length][cname.length];
				if(list!=null && list.size()>0){
					int index=0;
					for (int i = 0; i < list.size(); i++) {
						GeneralBillVO vo=(GeneralBillVO) list.get(i);
						for (int j = 0; j < vo.getChildrenVO().length; j++) {
							date[j+index][0]=vo.getChildrenVO()[j].getAttributeValue("vrevcustname")==null?"":vo.getChildrenVO()[j].getAttributeValue("vrevcustname").toString();
							date[j+index][1]=vo.getHeaderVO().getAttributeValue("cwarehouseid")==null?"":vo.getHeaderVO().getAttributeValue("cwarehouseid").toString();
							date[j+index][2]=vo.getChildrenVO()[j].getAttributeValue("invname")==null?"":vo.getChildrenVO()[j].getAttributeValue("invname").toString();
							date[j+index][3]=vo.getChildrenVO()[j].getAttributeValue("cinventorycode")==null?"":vo.getChildrenVO()[j].getAttributeValue("cinventorycode").toString();
							date[j+index][4]=vo.getChildrenVO()[j].getAttributeValue("vbatchcode")==null?"":vo.getChildrenVO()[j].getAttributeValue("vbatchcode").toString();
						}
						index=index+vo.getChildrenVO().length;
						
					}
				}else{
					date=new String[1][cname.length];
					date[0][0]="列1ccalbodyid";
					date[0][1]="列2cinventoryid";
					date[0][2]="列3cvendorid";
					date[0][3]="列4cwarehouseid";
					date[0][4]="列pk_invbasdoc";
				}
				tablemodel.setDataVector(date, ClientEnvironment.getInstance().getLanguage().equalsIgnoreCase("zh-cn")?cname:cname_en);
			
			
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return tablemodel;
	}
	private nc.ui.pub.beans.UITable getUILabel1() {
		if (jtable == null) {
			try {
				jtable = new UITable();
				jtable.setName("jtable");
				jtable.setBounds(0, 0, 800, 500);
				jtable.setDefaultHeader(false);
				
				jtable.setModel(getTableModel());
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return jtable;
	}
	
	private nc.ui.pub.beans.UIButton getUIButton1() {
		if (ivjUIButton1 == null) {
			try {
				ivjUIButton1 = new nc.ui.pub.beans.UIButton();
				ivjUIButton1.setName("UIButton1");
				ivjUIButton1.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801","UPP2004365801-000020")/*@res "确定"*/);
				ivjUIButton1.addActionListener(this);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIButton1;
	}
	private nc.ui.pub.beans.UIButton getUIButton2() {
		if (ivjUIButton2 == null) {
			try {
				ivjUIButton2 = new nc.ui.pub.beans.UIButton();
				ivjUIButton2.setName("UIButton2");
				ivjUIButton2.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("2004365801","UPP2004365801-000021")/*@res "取消"*/);
				ivjUIButton2.addActionListener(this);
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIButton2;
	}
	private nc.ui.pub.beans.UIPanel getUIPanel1() {
		if (ivjUIPanel1 == null) {
			try {
				ivjUIPanel1 = new nc.ui.pub.beans.UIPanel();
				ivjUIPanel1.setName("UIPanel1");
				ivjUIPanel1.setLayout(null);
				getUIPanel1().add(getUILabel1(), getUILabel1().getName());
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
		Logger.error(exception.getMessage(), exception);
		MessageDialog.showErrorDlg(this, null, exception.getMessage());
	}
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == this.getUIButton1()) {//确认
		//	int a2 = jtable.getSelectedRow();
			int ii[]=	jtable.getSelectedRows();
//			InvOnHandItemVO[]  vos=new InvOnHandItemVO[ii.length];
			String vos[]=new String[ii.length];
			for (int i = 0; i < ii.length; i++) {
//				InvOnHandItemVO  vo=new InvOnHandItemVO();
//				vo.setAttributeValue("bodyname", jtable.getModel().getValueAt(ii[i],0));
//				vo.setAttributeValue("ccalbodyid", jtable.getModel().getValueAt(ii[i],1));
//				vo.setAttributeValue("invname", jtable.getModel().getValueAt(ii[i],2));
//				vo.setAttributeValue("cwarehouseid", jtable.getModel().getValueAt(ii[i],3));
				//vo.setAttributeValue("unitname", jtable.getModel().getValueAt(ii[i],4));
//				vo.setAttributeValue("pk_batchcode", jtable.getModel().getValueAt(ii[i],4));
				
				vos[i]=jtable.getModel().getValueAt(ii[i],4).toString();
			}
			this.result=vos;
			
		}
		if (arg0.getSource() == this.getUIButton2()) {//取消
			this.closeCancel();
		}
		this.closeCancel();
	}
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
