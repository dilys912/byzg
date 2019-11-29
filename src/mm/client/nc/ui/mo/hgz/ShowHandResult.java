package nc.ui.mo.hgz;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JScrollPane;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.NCTableModel;
import nc.vo.mo.hgz.onhand.OnhandVO;
import nc.vo.pub.lang.UFDouble;

public class ShowHandResult extends nc.ui.pub.beans.UIDialog implements java.awt.event.ActionListener, MouseListener{
	
	private static final long serialVersionUID = 1L;
	private nc.ui.pub.beans.UITable jtable = null;//显示“新账号”
	private NCTableModel tablemodel=null;
	private nc.ui.pub.beans.UIPanel ivjUIPanel1 = null;
	private nc.ui.pub.beans.UIPanel ivjUIPanel2 = null;
	private nc.ui.pub.beans.UIButton ivjUIButton1 = null;//按钮“确认”
	private nc.ui.pub.beans.UIButton ivjUIButton2 = null;//“取消”
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private ToftPanel toftPanel=null;
	private ArrayList<OnhandVO> vo =null;
	private String[] sortFile;
	private Hashtable SelectVOtb;
	OnhandVO[]result=null;
	String liang[] ;

	String tou[][] ;

	private OnhandVO[] results;
	
	public OnhandVO[] getResults() {
		return results;
	}
	public void setResults(OnhandVO[] results) {
		this.results = results;
	}
	public ShowHandResult(ToftPanel toftPanel,ArrayList<OnhandVO> vo ) {
		// TODO Auto-generated constructor stub
		
		super(toftPanel);
		this.toftPanel=toftPanel;
		this.vo=vo;
		
		
		setName("AddAccount");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 500);
		setTitle("现存量Stock quantity");
		setContentPane(getUIDialogContentPane());
	    jtable.addMouseListener(this);
	    jtable.addSortListener();
	    
	    
	   // jtable.
	    
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
				String cname[]=new String[]{"序号","库存组织","仓库名称","产品名称","数量","批次","垛号"};
				String cname_en[]=new String[]{"Number","The inventory organization", "Warehouse Name", "Product Name", "number", "Batch", "StackNo."};
				String [][] data;
				//sortFile=new ArrayList();
				if(vo!=null ){
					data=new String[vo.size()][cname.length];
					OnhandVO item = null;
					String temp = null;
					SelectVOtb=new Hashtable();
					//sortFile=vo
					for (int i = 0; i < vo.size(); i++) {
						item = (OnhandVO) vo.get(i);
						SelectVOtb.put(String.valueOf(i+1), item);
						data[i][0]=String.valueOf(i+1);
						
						data[i][1]=item.getBodyname()==null?"":item.getBodyname().toString();
						data[i][2]=item.getStorname()==null?"":item.getStorname().toString();
						data[i][3]=item.getInvname()==null?"":item.getInvname().toString();
						if(item.getNum()!=null)
						{
							data[i][4]=item.getNum().toString();
						}
						data[i][5] = item.getVbatchcode();
						data[i][6] = item.getVfree();
					}
				}else{
					data=new String[1][cname.length];
					data[0][0]="列1ccalbodyid";
					data[0][1]="列2cinventoryid";
					data[0][2]="列3cvendorid";
					data[0][3]="列4num";
					data[0][4]="列vbatchcode";
					data[0][5]="列vfree1";
				}
				tablemodel.setDataVector(data, ClientEnvironment.getInstance().getLanguage().equalsIgnoreCase("simpchn")?cname:cname_en);
			
			
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		/*if(sortFile!=null||sortFile.length>0)
		{
			for(int i=0;i<sortFile.length;i++)
			tablemodel.setSortColumn(i);
			//tablemodel.
		}*/
		return tablemodel;
	}
	private nc.ui.pub.beans.UITable getUILabel1() {
		if (jtable == null) {
			try {
				jtable = new UITable();
				jtable.setName("jtable");
				jtable.setBounds(0, 0, 800, 500);
				jtable.setDefaultHeader(false);
				jtable.setFocusable(true);
				jtable.setSortEnabled(true);
				jtable.getTableHeader().addMouseListener(this);
				//.. TableSorter
				//jtable.add();
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
				ivjUIPanel1.addMouseListener(this);
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
			//jtable.v
			//jtable.u
			OnhandVO[]  vos=new OnhandVO[ii.length];
			for (int i = 0; i < ii.length; i++) {
				OnhandVO  vo=new OnhandVO();
		/*		vo.setAttributeValue("bodyname", jtable.getValueAt(ii[i],0));
				vo.setAttributeValue("ccalbodyid", jtable.getValueAt(ii[i],1));
				vo.setAttributeValue("invname", jtable.getValueAt(ii[i],2));
				vo.setAttributeValue("cwarehouseid", jtable.getValueAt(ii[i],3));
				//vo.setAttributeValue("unitname", jtable.getModel().getValueAt(ii[i],4));
				vo.setAttributeValue("pk_invbasdoc", jtable.getValueAt(ii[i],4));
			///	Object //freevo;
				//freevo
				FreeVO freevo=new FreeVO();
				freevo.setVfree1(String.valueOf(jtable.getValueAt(ii[i],5)));
				vo.setFreeItemVO(freevo);
				vo.setAssistnum(new UFDouble(ii[i]));*/
				String key=String.valueOf(jtable.getModel().getValueAt(ii[i],0));
				vo=(OnhandVO) SelectVOtb.get(key);
				vos[i]=vo;
			}
			this.setResults(vos);
			this.closeOK();
		}
		if (arg0.getSource() == this.getUIButton2()) {//取消
			this.closeCancel();
		}
		 
	}
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//arg0.
		if(arg0!=null)
		{
			
		}
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
		System.out.println("test");
	}
	
	
}
