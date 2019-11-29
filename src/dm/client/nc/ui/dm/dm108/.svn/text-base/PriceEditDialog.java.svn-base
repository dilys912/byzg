package nc.ui.dm.dm108;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

public class PriceEditDialog extends UIDialog implements ActionListener {

	private UITextField textField;

	private UILabel label;
	private UILabel label1;
	private UILabel label2;
	private UIPanel panel;

	private UIButton okbutton;

	private UIButton cancelbutton;

//	private String cscode;
	private String[] cscodes;

	private UITextField ywlx;
	private UITextField ywlx1;
	private UITextField ywlx2;
	private String pk_yw;

	private ClientEnvironment ce = ClientEnvironment.getInstance();

	public PriceEditDialog() {
		super();
		setSize(416, 228);
		setTitle("单价批修改"); 
		addListenerEvent();
		getContentPane().add(getPanel());
	}

	public void addListenerEvent() {
		getOkbutton().addActionListener(this);
		getCancelbutton().addActionListener(this);

	}
	protected UIPanel getPanel() {
		if (panel == null) {
			panel = new UIPanel();
			panel.setLayout(null);
			panel.add(getOkbutton());
			panel.add(getCancelbutton());
			panel.add(getLabel());
			panel.add(getLabel1());
			panel.add(getLabel2());
			panel.add(getTextField());
			panel.add(getTextField1());
			panel.add(getTextField2());
		}
		return panel;
	}

	protected UILabel getLabel() {
		if (label == null) {
			label = new UILabel();
			label.setText("无税单价：");
			label.setBounds(105, 49, 71, 18);
		}
		return label;
	}
	
	protected UILabel getLabel1() {
		if (label1 == null) {
			label1 = new UILabel();
			label1.setText("含税单价：");
			label1.setBounds(105, 80, 71, 18);
		}
		return label1;
	}
	
	protected UILabel getLabel2() {
		if (label2 == null) {
			label2 = new UILabel();
			label2.setText("税率：");
			label2.setBounds(105, 112, 71, 18);
		}
		return label2;
	}
	
	protected UITextField getTextField() {
		if (ywlx == null) {
			ywlx = new UITextField();
			ywlx.setTextType("TextDbl");
			ywlx.setBounds(163, 49, 122, 20);
			ywlx.setName("ywlx");
		}
		return ywlx;
	}
	protected UITextField getTextField1() {
		if (ywlx1 == null) {
			ywlx1 = new UITextField();
			ywlx1.setTextType("TextDbl");
			ywlx1.setBounds(163, 80, 122, 20);
			ywlx1.setName("ywlx1");
		}
		return ywlx1;
	}
	protected UITextField getTextField2() {
		if (ywlx2 == null) {
			ywlx2 = new UITextField();
			ywlx2.setTextType("TextDbl");
			ywlx2.setBounds(163, 112, 122, 20);
			ywlx2.setName("ywlx2");
		}
		return ywlx2;
	}
	// 确定按纽
	protected UIButton getOkbutton() {
		if (okbutton == null) {
			okbutton = new UIButton();
			okbutton.setPreferredSize(new Dimension(80, 20));
			okbutton.setBounds(101, 145, 75, 20);
			okbutton.setText("确定");
		}
		return okbutton;
	}

	// 取消按纽
	protected UIButton getCancelbutton() {
		if (cancelbutton == null) {
			cancelbutton = new UIButton();
			cancelbutton.setBounds(246, 145, 75, 20);
			cancelbutton.setText("取消");
		}
		return cancelbutton;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getOkbutton()) {
			onOK();
		} else if (e.getSource() == getCancelbutton()) {
			cscodes = new String[]{"0.00","0.00","0.00"};
			this.close();
		}

	}

	public void onOK() {
		 String[] ocscode = new String[3]; 
		 if ((getTextField().getText()==null||getTextField().getText().equals(""))
				 ||(getTextField1().getText()==null||getTextField1().getText().equals(""))) {
			 ocscode[0] = getTextField().getText();
			 ocscode[1] = getTextField1().getText();
			 ocscode[2] = getTextField2().getText();
//			String ocscode = getTextField().getRefPK();
			setCscode(ocscode);
			closeOK();
		}else{
			MessageDialog.showErrorDlg(this, "", "无税单价与含税单价只可输入其中一个。");
			
		}
	}

	public String[] getCscode() {
		return cscodes;
	}

	public void setCscode(String[] cscode) {
		this.cscodes = cscode;
	}

}
