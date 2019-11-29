package nc.ui.scm.goldtax;

import static nc.vo.jcom.lang.StringUtil.isEmpty;

import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

//import org.apache.commons.validator.Msg;

//import org.apache.jk.core.Msg;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.vo.scm.goldtax.GoldTaxVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * 传金税公共对话框，通常的使用方法如下：
 * <pre>
 * // 创建对话框
 * TransGoldTaxDlg dlg = new TransGoldTaxDlg(this);
 * // 设置金税数据
 * dlg.setGoldTaxVOs(goldTaxVOs);
 * // 显示对话框
 * if (UIDialog.ID_OK == dlg.showModal()) {
 *     // 传金税成功后的处理
 *     // 只有当返回 ID_OK 时才表示传金税成功，否则都是失败
 *     ......
 * }
 * </pre>
 * 
 * @author 蒲强华
 * @since 2008-9-1
 */
public class TransGoldTaxDlg extends UIDialog {
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JLabel checker = null;
	private JLabel payee = null;
	private UIRefPane checkerRef = null;
	private UIRefPane payeeRef = null;
	private UIButton transferBtn = null;
	private UIButton cancelBtn = null;


	/** 金税VO */
	private GoldTaxVO[] goldTaxVOs = null;

	/**
	 * @param owner 父面板
	 */
	public TransGoldTaxDlg(Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle(Msg.get("UPPscmpub-001188"));//"传金税"
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			payee = new JLabel();
			payee.setBounds(new Rectangle(55, 85, 65, 18));
			payee.setText(Msg.get("UPPscmpub-001187"));//"收款人"
			checker = new JLabel();
			checker.setBounds(new Rectangle(55, 40, 65, 18));
			checker.setText(Msg.get("UPPscmpub-001186"));//"审核人"
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(checker, null);
			jContentPane.add(payee, null);
			jContentPane.add(getCheckerRef(), null);
			jContentPane.add(getPayeeRef(), null);
			jContentPane.add(getTransferBtn(), null);
			jContentPane.add(getCancelBtn(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes checkerRef	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private UIRefPane getCheckerRef() {
		if (checkerRef == null) {
			checkerRef = new UIRefPane();
			checkerRef.setRefNodeName("人员档案");
			checkerRef.setBounds(new Rectangle(128, 40, 100, 20));
		}
		return checkerRef;
	}

	/**
	 * This method initializes payeeRef	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private UIRefPane getPayeeRef() {
		if (payeeRef == null) {
			payeeRef = new UIRefPane();
			payeeRef.setRefNodeName("人员档案");
			payeeRef.setBounds(new Rectangle(128, 85, 100, 20));
		}
		return payeeRef;
	}

	/**
	 * This method initializes transferBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getTransferBtn() {
		if (transferBtn == null) {
			transferBtn = new UIButton();
			transferBtn.setBounds(new Rectangle(55, 135, 75, 20));
			transferBtn.setText(Msg.common("UC001-0000056"));//"导出"

			transferBtn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (nullValidate()) {
						String filename = getFilename(FileDialog.SAVE);
						if (!isEmpty(filename)) {
							close();
							export(filename);
							closeOK();
						}
					}
				}
			});
		}
		return transferBtn;
	}

	/**
	 * This method initializes cancelBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new UIButton();
			cancelBtn.setBounds(new Rectangle(155, 135, 75, 20));
			cancelBtn.setText(Msg.common("UC001-0000008"));//"取消"
			cancelBtn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					closeCancel();
				}
			});
		}
		return cancelBtn;
	}

	public void setGoldTaxVOs(GoldTaxVO[] goldTaxVOs) {
		this.goldTaxVOs  = goldTaxVOs;
	}

	/**
	 * @return 审核人名称
	 */
	private String getChecker() {
		return getCheckerRef().getText();
	}

	/**
	 * @return 收款人名称
	 */
	private String getPayee() {
		return getPayeeRef().getText();
	}

	/**
	 * 将金税数据导出到指定文件
	 * 
	 * @param filename 要导出的文件名
	 */
	private void export(String filename) {
		// 设置审核人和付款人
		for (GoldTaxVO tax : this.goldTaxVOs) {
			tax.getParentVO().setChecker(getChecker());
			tax.getParentVO().setPayee(getPayee());
		}

		String corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		// 创建传送器
		GoldTaxTransport transport = new GoldTaxTransport(corp);
		// 合并分单处理
		GoldTaxVO[] taxVOs = transport.mergeAndSplit(goldTaxVOs);
		// 保存到文件
		transport.saveToFiles(taxVOs, filename);
	}

	/**
	 * 从金税单据文件导入为金税VO数组
	 * 
	 * @return 从金税单据文件解析的金税数组，null表示用户在取消了操作
	 */
	public GoldTaxVO[] importGoldTax() {
		String filename = getFilename(FileDialog.LOAD);
		if (isEmpty(filename)) {
			return null;
		}
		String corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		// 创建传送器
		GoldTaxTransport transport = new GoldTaxTransport(corp);
		return transport.loadFromFile(filename);
	}

	/**
	 * 弹出文件选择框向用户询问导出文件名
	 * 
	 * @param mode the mode of the dialog; either
	 *              <code>FileDialog.LOAD</code> or <code>FileDialog.SAVE</code>
	 * @return 如果用户选择取消返回 null，否则返回完整文件名(路径+名称)
	 */
	private String getFilename(int mode) {
		FileDialog fileDialog = new FileDialog(this, "", mode);
		fileDialog.setFile("fp.txt");
		fileDialog.setVisible(true);
		String directory = fileDialog.getDirectory();
		String file = fileDialog.getFile();
		if (isEmpty(directory) || isEmpty(file)) {
			return null;
		} else {
			String filename = directory + file;
			SCMEnv.out(filename);
			return filename;
		}
	}

	/**
	 * 字段的非空校验
	 * 
	 * @return 通过校验返回 true，否则返回 false
	 */
	private boolean nullValidate() {
		if (null == goldTaxVOs || goldTaxVOs.length == 0) {
			MessageDialog.showErrorDlg(this, null, NCLangRes.getInstance().getStrByID("scmpub", "scmpub-000017")/*没有金税数据*/);
			return false;
		}
		if (isEmpty(getChecker())) {
			MessageDialog.showErrorDlg(this, null, NCLangRes.getInstance().getStrByID("scmpub", "scmpub-000018")/*请选择审核人*/);
			return false;
		}
		if (isEmpty(getPayee())) {
			MessageDialog.showErrorDlg(this, null, NCLangRes.getInstance().getStrByID("scmpub", "scmpub-000019")/*请选择收款人*/);
			return false;
		}
		return true;
	}

}
