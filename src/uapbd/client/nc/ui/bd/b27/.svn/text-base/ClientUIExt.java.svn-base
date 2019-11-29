package nc.ui.bd.b27;

import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.pub.tools.BannerDialog;
import nc.vo.bd.BDMsg;
import nc.vo.bd.b27.CargdocVO;
import nc.vo.bd.b27.LocalModel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.coderule.CodeRuleLegal;
import org.apache.commons.lang.StringUtils;

/**
 * 此处插入类型说明。 创建日期：(2001-06-05 17:35:42)
 * 
 * @author：祝奇
 */
public class ClientUIExt extends nc.ui.pub.ToftPanel implements
		javax.swing.event.TreeSelectionListener, ILinkMaintain {
	private static final long serialVersionUID = 6245369132912651170L;
	private NodePanel ivjNodePanel1 = null;
	private nc.ui.pub.beans.UIRefPane ivjStoreRef = null;
	private nc.ui.pub.beans.UITree ivjTreeList1 = null;
	private nc.ui.pub.beans.UILabel ivjUILabel1 = null;
	private nc.ui.pub.beans.UIPanel ivjUIPanel1 = null;
	private nc.ui.pub.beans.UIScrollPane ivjUIScrollPane1 = null;
	private nc.ui.pub.beans.UISplitPane ivjUISplitPane1 = null;
	private ButtonObject m_boSetup = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10081004", "UPP10081004-000015")
	/*
	 * @res "设置"
	 */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
			"UPP10081004-000015")/* @res "设置" */, 2, "设置"); /*
															 * -=notranslate =-
															 */
	private ButtonObject m_boPrint = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UC001-0000007")/* @res "打印" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10081004", "UC001-0000007")/*
																		 * @res
																		 * "打印"
																		 */, 2,
			"打印"); /* -=notranslate=- */
	private ButtonObject m_boPreView = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10081004", "UPP10081004-000016")/*
																		 * @res
																		 * "预览"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UPP10081004-000016")/* @res "预览" */, 2, "预览"); /*
																	 * -=notranslate
																	 * =-
																	 */
	private ButtonObject m_boOutput = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10081004", "UPP10081004-000017")/*
																		 * @res
																		 * "输出"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UPP10081004-000017")/* @res "输出" */, 2, "输出"); /*
																	 * -=notranslate
																	 * =-
																	 */
	private ButtonObject m_boInsert = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UC001-0000002")/* @res "增加" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10081004", "UC001-0000002")/*
																		 * @res
																		 * "增加"
																		 */, 2,
			"增加"); /* -=notranslate=- */
	private ButtonObject m_boUpdate = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UC001-0000045")/* @res "修改" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10081004", "UC001-0000045")/*
																		 * @res
																		 * "修改"
																		 */, 2,
			"修改"); /* -=notranslate=- */
	private ButtonObject m_boDelete = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UC001-0000039")/* @res "删除" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10081004", "UC001-0000039")/*
																		 * @res
																		 * "删除"
																		 */, 2,
			"删除"); /* -=notranslate=- */
	private ButtonObject m_boOK = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UC001-0000001")/* @res "保存" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10081004", "UC001-0000001")/*
																		 * @res
																		 * "保存"
																		 */, 2,
			"保存"); /* -=notranslate=- */
	private ButtonObject m_boCancel = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UC001-0000008")/* @res "取消" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10081004", "UC001-0000008")/*
																		 * @res
																		 * "取消"
																		 */, 2,
			"取消"); /* -=notranslate=- */
	private ButtonObject m_boRefresh = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UC001-0000009")/* @res "刷新" */, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("10081004", "UC001-0000009")/*
																		 * @res
																		 * "刷新"
																		 */, 2,
			"刷新"); /* -=notranslate=- */
	private ButtonObject m_boCelerity = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("10081004", "UPT10081004-000001")/*
																		 * @res
																		 * "快速定义"
																		 */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
					"UPT10081004-000001")/* @res "快速定义" */, 2, "快速定义"); /*
																		 * -=notranslate
																		 * =-
																		 */
	private ButtonObject m_excel = new ButtonObject("EXCEL导入", "EXCEL导入", 2,
			"EXCEL导入");

	private ButtonObject[] m_MainButtonGroup = { m_boCelerity, m_boInsert,
			m_boUpdate, m_boDelete, m_boOK, m_boCancel, m_boRefresh,
			/* m_boSetup, */
			m_boPrint, m_excel /*
								 * , m_boPreView, m_boOutput
								 */};

	private int m_iCurrentStatus = 0; // 浏览
	private DefaultTreeModel m_tm = new DefaultTreeModel(
			new DefaultMutableTreeNode(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10081004", "UPP10081004-000018")/*
																 * @res "货位档案"
																 */), false);
	private CargdocVO[] m_data = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private CelerityDialog ivjCelerityDialog1 = null;
	// lxdong 增加
	private javax.swing.tree.DefaultMutableTreeNode curr1Node;
	private javax.swing.tree.DefaultMutableTreeNode curr2Node;
	private javax.swing.tree.DefaultMutableTreeNode curr3Node;

	class IvjEventHandler implements nc.ui.pub.beans.ValueChangedListener {
		public void valueChanged(nc.ui.pub.beans.ValueChangedEvent event) {
			if (event.getSource() == ClientUIExt.this.getStoreRef())
				connEtoC1(event);
		};
	};

	/**
	 * ClientUIExt 构造子注解。
	 */
	public ClientUIExt() {
		super();
		initialize();
	}

	/**
	 * connEtoC1:
	 * (StoreRef.valueChanged.valueChanged(nc.ui.pub.beans.ValueChangedEvent)
	 * --> ClientUIExt.WarehouseChanged()V)
	 * 
	 * @param arg1
	 *            nc.ui.pub.beans.ValueChangedEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(nc.ui.pub.beans.ValueChangedEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.WarehouseChanged();
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-4-30 14:25:35)
	 * 
	 * @param vo
	 *            nc.vo.bd.b27.CargdocVO author：李效东
	 *            因为原constructTree方法递归太深，造成溢出错误（StackOverflowError）
	 *            所以从写了constructNewTree方法
	 * 
	 */
	public void constructNewTree(CargdocVO[] vos) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) m_tm.getRoot();
		if (vos != null) {
			for (int i = 0; i < vos.length; i++) {
				switch (vos[i].getCodelev().intValue()) {
				case 1: {
					DefaultMutableTreeNode me = new DefaultMutableTreeNode(
							vos[i].getCscode() + " " + vos[i].getCsname());
					me.setUserObject(m_data[i]);
					curr1Node = me;
					m_tm.insertNodeInto(me, root, root.getChildCount());
					break;
				}
				case 2: {
					DefaultMutableTreeNode me = new DefaultMutableTreeNode(
							vos[i].getCscode() + " " + vos[i].getCsname());
					me.setUserObject(m_data[i]);
					curr2Node = me;
					m_tm.insertNodeInto(me, curr1Node, curr1Node
							.getChildCount());
					break;
				}
				case 3: {
					DefaultMutableTreeNode me = new DefaultMutableTreeNode(
							vos[i].getCscode() + " " + vos[i].getCsname());
					me.setUserObject(m_data[i]);
					curr3Node = me;
					m_tm.insertNodeInto(me, curr2Node, curr2Node
							.getChildCount());
					break;

				}
				case 4: {
					DefaultMutableTreeNode me = new DefaultMutableTreeNode(
							vos[i].getCscode() + " " + vos[i].getCsname());
					me.setUserObject(m_data[i]);
					// curr4Node=me;
					m_tm.insertNodeInto(me, curr3Node, curr3Node
							.getChildCount());
					break;
				}
				}
			}

			// if (vos[i].getCodelev().intValue() == 1) {
			// DefaultMutableTreeNode me =
			// new DefaultMutableTreeNode(vos[i].getCscode() + " " +
			// vos[i].getCsname());
			// me.setUserObject(m_data[i]);
			// m_tm.insertNodeInto(me, root, root.getChildCount());
			// }

		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-05-30 11:48:01)
	 * 
	 * @param tm
	 *            javax.swing.tree.DefaultTreeModel
	 * @param parent
	 *            javax.swing.tree.DefaultMutableTreeNode
	 * @param me
	 *            nc.vo.bd.b14.InvclVO
	 */
	public void constructTree(DefaultTreeModel tm,
			DefaultMutableTreeNode parent, int meIndex) {
		if (m_data == null)
			return;
		int parentLevel = 0;
		if (parent == null) {
			parent = (DefaultMutableTreeNode) tm.getRoot();
		} else {
			parentLevel = parent.getLevel();
		}

		if (meIndex == m_data.length)
			return;

		int meLevel = m_data[meIndex].getCodelev().intValue();
		String meEndFlag = m_data[meIndex].getEndflag().toString();
		if (meLevel == parentLevel + 1) {
			DefaultMutableTreeNode me = new DefaultMutableTreeNode(
					m_data[meIndex].getCscode() + " "
							+ m_data[meIndex].getCsname());
			me.setUserObject(m_data[meIndex]);
			tm.insertNodeInto(me, parent, parent.getChildCount());
			if (meEndFlag.equals("N")) {
				constructTree(tm, me, meIndex + 1);
			} else {
				constructTree(tm, parent, meIndex + 1);
			}
		} else {
			constructTree(tm, (DefaultMutableTreeNode) parent.getParent(),
					meIndex);
		}
	}

	/**
	 * 返回 CelerityDialog1 特性值。
	 * 
	 * @return nc.ui.bd.b27.CelerityDialog
	 */
	/* 警告：此方法将重新生成。 */
	private CelerityDialog getCelerityDialog1() {
		if (ivjCelerityDialog1 == null) {
			try {
				ivjCelerityDialog1 = new nc.ui.bd.b27.CelerityDialog(this);
				ivjCelerityDialog1.setName("CelerityDialog1");
				ivjCelerityDialog1
						.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjCelerityDialog1;
	}

	/**
	 * 返回 NodePanel1 特性值。
	 * 
	 * @return nc.ui.bd.b27.NodePanel
	 */
	/* 警告：此方法将重新生成。 */
	private NodePanel getNodePanel1() {
		if (ivjNodePanel1 == null) {
			try {
				ivjNodePanel1 = new nc.ui.bd.b27.NodePanel();
				ivjNodePanel1.setName("NodePanel1");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjNodePanel1;
	}

	/**
	 * 返回 StoreRef 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIRefPane getStoreRef() {
		if (ivjStoreRef == null) {
			try {
				ivjStoreRef = new nc.ui.pub.beans.UIRefPane();
				ivjStoreRef.setName("StoreRef");
				// ivjStoreRef.setBounds(39, 1, 160, 20);
				ivjStoreRef.setRefNodeName("仓库档案");
				// user code begin {1}
				ivjStoreRef.setWhereString("where csflag='Y' and pk_corp='"
						+ ClientEnvironment.getInstance().getCorporation()
								.getPk_corp() + "'");
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjStoreRef;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (00-6-6 13:33:25)
	 * 
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
				"UPP10081004-000018")/* @res "货位档案" */;
	}

	/**
	 * 返回 TreeList1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITree
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITree getTreeList1() {
		if (ivjTreeList1 == null) {
			try {
				ivjTreeList1 = new nc.ui.pub.beans.UITree();
				ivjTreeList1.setName("TreeList1");
				ivjTreeList1.setBounds(0, 0, 78, 72);
				// user code begin {1}
				ivjTreeList1
						.setPreferredSize(new java.awt.Dimension(300, 9000));
				ivjTreeList1.putClientProperty("JTree.lineStyle", "Angled");
				((DefaultTreeSelectionModel) ivjTreeList1.getSelectionModel())
						.setSelectionMode(1);
				ivjTreeList1.setModel(m_tm);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTreeList1;
	}

	/**
	 * 返回 UILabel1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getUILabel1() {
		if (ivjUILabel1 == null) {
			try {
				ivjUILabel1 = new nc.ui.pub.beans.UILabel();
				ivjUILabel1.setName("UILabel1");
				ivjUILabel1
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10081004", "UC000-0000153")/* @res "仓库" */);
				// ivjUILabel1.setBounds(5, 0, 33, 20);
				ivjUILabel1.setILabelType(5/** 必输框 */
				);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUILabel1;
	}

	/**
	 * 返回 UIPanel1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getUIPanel1() {
		if (ivjUIPanel1 == null) {
			try {
				ivjUIPanel1 = new nc.ui.pub.beans.UIPanel();
				ivjUIPanel1.setName("UIPanel1");
				ivjUIPanel1.setPreferredSize(new java.awt.Dimension(300, 30));
				ivjUIPanel1.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 2));
				ivjUIPanel1.setMinimumSize(new java.awt.Dimension(300, 20));
				ivjUIPanel1.setMaximumSize(new java.awt.Dimension(1000, 20));
				getUIPanel1().add(getUILabel1(), getUILabel1().getName());
				getUIPanel1().add(getStoreRef(), getStoreRef().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIPanel1;
	}

	/**
	 * 返回 UIScrollPane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getUIScrollPane1() {
		if (ivjUIScrollPane1 == null) {
			try {
				ivjUIScrollPane1 = new nc.ui.pub.beans.UIScrollPane();
				ivjUIScrollPane1.setName("UIScrollPane1");
				ivjUIScrollPane1.setPreferredSize(new java.awt.Dimension(300,
						80));
				getUIScrollPane1().setViewportView(getTreeList1());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIScrollPane1;
	}

	/**
	 * 返回 UISplitPane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UISplitPane getUISplitPane1() {
		if (ivjUISplitPane1 == null) {
			try {
				ivjUISplitPane1 = new nc.ui.pub.beans.UISplitPane(1);
				ivjUISplitPane1.setName("UISplitPane1");
				ivjUISplitPane1.setDividerLocation(300);

				UIScrollPane scrPnl = new UIScrollPane();
				scrPnl.setPreferredSize(new java.awt.Dimension(400, 200));
				scrPnl.setViewportView(getNodePanel1());

				getUISplitPane1().add(scrPnl, "right");
				getUISplitPane1().add(getUIScrollPane1(), "left");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUISplitPane1;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		System.out.println(exception);
		exception.printStackTrace(System.out);
	}

	/**
	 * 初始化连接
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	/* 警告：此方法将重新生成。 */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getStoreRef().addValueChangedListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ClientUIExt");
			setSize(774, 419);
			add(getUIPanel1(), "North");
			add(getUISplitPane1(), "Center");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		this.setButtons(this.m_MainButtonGroup);
		switchButtonStatu(0);

		// user code end
	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) {
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			ClientUIExt aClientUI;
			aClientUI = new ClientUIExt();
			frame.setContentPane(aClientUI);
			frame.setSize(aClientUI.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.show();
			java.awt.Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame
					.getHeight()
					+ insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("nc.ui.pub.ToftPanel 的 main() 中发生异常");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * 
	 * @version (00-6-1 10:32:59)
	 * 
	 * @param bo
	 *            ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		if (bo == m_boCelerity) {
			onCelerity();
		}
		if (bo == m_boSetup) {
			onSetup();
			return;
		}
		if (bo == m_boPrint) {
			onPrint();
			return;
		}
		if (bo == m_boPreView) {
			onPreView();
			return;
		}
		if (bo == m_boOutput) {
			onOutput();
			return;
		}
		if (bo == m_boInsert) {
			onInsert();
			return;
		}
		if (bo == m_boUpdate) {
			onUpdate();
			return;
		}
		if (bo == m_boDelete) {
			onDelete();
			return;
		}
		if (bo == m_boRefresh) {
			onRefresh();
			return;
		}
		if (bo == m_boOK) {
			onOK();
			return;
		}
		if (bo == m_boCancel) {
			onCancel();
			return;
		}
		if (bo == m_excel) {
			onImport();
			return;
		}
	}

	// excle的方法
	private void onImport() {
		Daoru();
	}

	public static WritableWorkbook ww = null;
	private int res;
	private File txtFile = null;
	private nc.ui.pub.beans.UITextField txtfFileUrl = null;// 文本框,用于显示文件路径

	/**
	 * 李江涛 时间：2014-09-03 原因：货位导入
	 * */

	@SuppressWarnings("static-access")
	private void Daoru() {
		try {

			nc.ui.pub.beans.UIFileChooser fileChooser = new nc.ui.pub.beans.UIFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(true);
			res = fileChooser.showOpenDialog(this);
			if (res == 0) {
				getTFLocalFile().setText(
						fileChooser.getSelectedFile().getAbsolutePath());
				txtFile = fileChooser.getSelectedFile();
				String filepath = txtFile.getAbsolutePath();
				final WriteToExcel exceldata = new WriteToExcel();
				exceldata.creatFile(filepath);
				Runnable checkRun = new Runnable() {
					public void run() {

						BannerDialog dialog = new BannerDialog(getParent());
						dialog.start();
						try {
							exceldata.readData(0);
							CargdocVO[] bvos = WriteToExcel.wbvo;

							if (bvos != null && bvos.length > 0) {
								CargdocVO[] cvo = CargdocBO_Client
										.queryAllByStordoc(getStoreRef()
												.getRefPK());
								HashMap<String, String> map = new HashMap<String, String>();
								if (cvo != null && cvo.length > 0) {
									for (int i = 0; i < cvo.length; i++) {
										CargdocVO voi = cvo[i];
										String cscodei = voi.getCscode();
										if (!map.containsKey(cscodei)) {
											map.put(cscodei, voi.getCsname());
										}
									}
								}
								StringBuffer info = new StringBuffer("");
								for (int i = 0; i < bvos.length; i++) {
									Object csname = map
											.get(bvos[i].getCscode());
									String err = "";
									if (csname != null && !"".equals(csname)) {
										err = "货位编码【" + bvos[i].getCscode()
												+ "】货位名称【" + csname
												+ "】在当前仓库已存在";
									}
									if (err.trim().length() > 0) {
										info.append("第(" + (i + 4) + ")行：("
												+ err + ")\n");
									}
								}
								if (info.toString().trim().length() > 0) {
									showErrorMessage(info.toString() + " 请核实!");
									return;
								}
								for (int j = 0; j < bvos.length; j++) {
									bvos[j].setCsattr(1);
									bvos[j].setCodelev(1);
									boolean isend = isEnd(bvos[j], bvos);
									int k1 = bvos[j].getCscode().length();
									bvos[j].setCodelev(k1 / 2);
									bvos[j].setEndflag(new UFBoolean(isend));
									bvos[j].setInpriority(0);
									bvos[j].setIschecked(UFBoolean.FALSE);
									bvos[j].setIsrmplace(UFBoolean.FALSE);
									bvos[j].setOutpriority(0);
									bvos[j].setSealflag(UFBoolean.FALSE);
									bvos[j].setVolume(new UFDouble(0.00));
									bvos[j].setPk_stordoc(getStoreRef()
											.getRefPK());
								}
								IVOPersistence ivop = (IVOPersistence) NCLocator
										.getInstance().lookup(
												IVOPersistence.class.getName());
								//添加
								ivop.insertVOArray(bvos);
								// 获取选择的这个仓库所有的货位
								cvo = CargdocBO_Client.queryAllByStordoc(getStoreRef().getRefPK());
								// 循环这个货位的所有编码进行末级判断
								for (int j = 0; j < cvo.length; j++) {
									boolean isend = isEnd(cvo[j], cvo);
									int k1 = cvo[j].getCscode().length();
									cvo[j].setCodelev(k1 / 2);
									cvo[j].setEndflag(new UFBoolean(isend));
								}
								IVOPersistence top = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
								//判断后进行更新
								top.updateVOArray(cvo);
							} else {
								showErrorMessage("没有取到数据,请检查EXCEL表格!");
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
							showErrorMessage(e.getMessage());
						} finally {
							dialog.end();
						}
					}

					private boolean isEnd(CargdocVO cargdocVO, CargdocVO[] bvos) {
						if (bvos.length == 1) {
							return true;
						}
						for (int i = 0; i < bvos.length; i++) {
							String aSource = bvos[i].getCscode();
							if (aSource.equals(cargdocVO.getCscode())) {
								continue;
							}
							if (aSource.startsWith(cargdocVO.getCscode())) {
								return false;
							}
						}
						return true;
					}
				};
				new Thread(checkRun).start();
			} else {
				return;
			}
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}

	}

	private nc.ui.pub.beans.UITextField getTFLocalFile() {
		if (txtfFileUrl == null) {
			try {
				txtfFileUrl = new nc.ui.pub.beans.UITextField();
				txtfFileUrl.setName("txtfFileUrl");
				txtfFileUrl.setBounds(270, 160, 230, 26);
				txtfFileUrl.setMaxLength(2000);
				txtfFileUrl.setEditable(false);

			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}
		return txtfFileUrl;
	}

	public void onCancel() {
		this.showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESSING());

		this.m_iCurrentStatus = 0;
		DefaultMutableTreeNode me = (DefaultMutableTreeNode) this
				.getTreeList1().getSelectionPath().getLastPathComponent();
		CargdocVO meVO = null;
		if (me.getParent() != null) {
			meVO = (CargdocVO) me.getUserObject();
		}
		this.getNodePanel1().setNode(null, meVO, getStoreRef().getRefPK());
		this.getTreeList1().setEnabled(true);
		this.switchButtonStatu(0);
		this.getNodePanel1().switchStatu(0);
		this.getStoreRef().setEnabled(true);
		this.showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESS_FINISHED());
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-06-06 12:53:22)
	 */
	public void onCelerity() {

		if (getStoreRef().getRefPK() == null) {
			this.showHintMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		ivjCelerityDialog1 = null;
		if (getCelerityDialog1().showModal() == UIDialog.ID_OK) {
			this.showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESSING());
			/*
			 * //如果货位总数大于500000，则抛出异常 try { long totalAmount = 1; for(int
			 * levelLength : getCelerityDialog1().getParam()) { if(levelLength >
			 * 0) totalAmount *= levelLength; } if(totalAmount > 500000) {
			 * showErrorMessage
			 * (NCLangResOnserver.getInstance().getStrByID("10081004",
			 * "UPP10081004-000039")货位数据量过大,系统无法支持.); return; } } catch
			 * (BusinessException e1) { Logger.error(e1.getMessage(), e1);
			 * showErrorMessage(e1.getMessage()); return; }
			 */

			new Thread(new Runnable() {
				public void run() {
					BannerDialog dialog = new BannerDialog(ClientUIExt.this);
					dialog.setStartText(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("bdpub", "UPPbdpub-000177")/*
																	 * @res
																	 * "正在进行后台操作,
																	 * 请稍等..."
																	 */);
					dialog.start();
					try {
						CargdocBO_Client.celerityMaintain(getStoreRef()
								.getRefPK(), getCelerityDialog1().getParam(),
								getCelerityDialog1().getNames());

						onRefresh();

						if (getTreeList1().getSelectionPath() == null
								|| getTreeList1().getSelectionPath()
										.getPathCount() == 1) {
							setButton(m_boInsert, true);
							setButton(m_boUpdate, false);
							setButton(m_boDelete, false);
						}
					} catch (Exception e) {
						Logger.error(e.getMessage(), e);
						dialog.end();
						showErrorMessage(e.getMessage());
						return;
					}
					dialog.end();
				}

			}).start();

			this.showHintMessage(nc.vo.bd.BDMsg.MSG_DATA_SAVE_SUCCESS());
		}

	}

	public void onDelete() {
		if (getStoreRef().getRefPK() == null) {
			this.showHintMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}

		DefaultMutableTreeNode me = (DefaultMutableTreeNode) this
				.getTreeList1().getSelectionPath().getLastPathComponent();
		if (me == null || me.getParent() == null) {
			this.showHintMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}

		if (nc.ui.pub.beans.MessageDialog.showOkCancelDlg(this, null,
				nc.vo.bd.BDMsg.MSG_SURE_DELETE()) != nc.ui.pub.beans.MessageDialog.ID_OK)
			return;
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) me.getParent();
		showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESSING());
		CargdocVO parentVO = null;
		if (parent.getParent() != null) {
			parentVO = (CargdocVO) parent.getUserObject();
		}
		CargdocVO curNode = null;
		boolean isParentHaveChildren = true;
		try {
			curNode = (CargdocVO) me.getUserObject();
			isParentHaveChildren = CargdocBO_ClientExt.delete(curNode,
					parentVO == null ? null : parentVO.getPk_cargdoc());
		} catch (BusinessException re) {
			this.showErrorMessage(re.getMessage());
			return;
		} catch (Exception e) {
			this.showHintMessage(e.toString());
			return;
		}

		// 保存成功，修改本地内容

		if (!isParentHaveChildren) // 修改上级节点末级标志。
		{
			parentVO.setEndflag(new UFBoolean("Y"));
		}

		this.m_tm.removeNodeFromParent(me);
		getTreeList1().setSelectionInterval(0, 0);
		showHintMessage(nc.vo.bd.BDMsg.MSG_DATA_ASSIGN_SUCCESS());

	}

	/**
	 * 在此处插入方法说明。 创建日期：(00-8-2 10:37:16)
	 */
	public void onInsert() {
		if (getStoreRef().getRefPK() == null) {
			this.showHintMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		this.getStoreRef().setEnabled(false);
		this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"10081004", "UC001-0000002")/* @res "增加" */);
		this.m_iCurrentStatus = 1;
		this.switchButtonStatu(1);
		// this.getNodePanel1().switchStatu(1); 下移 lxdong
		this.getTreeList1().setEnabled(false);
		DefaultMutableTreeNode selected = ((DefaultMutableTreeNode) (this
				.getTreeList1().getSelectionPath().getLastPathComponent()));
		// //lxdong 增加
		// this.getNodePanel1().switchStatu(1,((CargdocVO)selected.getUserObject()).getCscode());

		if (selected.getParent() == null) {
			getNodePanel1().switchStatu(1);
			this.getNodePanel1().setNode(null, null, getStoreRef().getRefPK());
		} else {
			// lxdong 增加
			this.getNodePanel1().switchStatu(1,
					((CargdocVO) selected.getUserObject()).getCscode());

			this.getNodePanel1().setNode(
					(CargdocVO) (selected.getUserObject()), null,
					getStoreRef().getRefPK());
		}
		this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"10081004", "UPP10081004-000019")/* @res "请在右侧编辑货位信息。" */);
	}

	/**
	 * 在此处插入方法说明。 创建日期：(00-8-2 10:37:16) 修改：李效东 2002-04-05 增加对收货货位的判断
	 */
	public void onOK() {
		this.showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESSING());
		if (this.m_iCurrentStatus == 1) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) this
					.getTreeList1().getSelectionPath().getLastPathComponent();
			CargdocVO parentVO = null;
			if (parent.getParent() != null) {
				parentVO = (CargdocVO) parent.getUserObject();
			}
			CargdocVO curNode = null;
			try {
				curNode = this.getNodePanel1().getCurNewNode();
				// lxdong 增加
				if (CargdocBO_Client.queryByStorCodeIsRmplace(getStoreRef()
						.getRefPK(), new UFBoolean("Y"))
						&& curNode.getIsRMPlace().booleanValue()) {
					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("10081004", "UPP10081004-000020")/*
																		 * @res
																		 * "只能设置一个收货货位"
																		 */
							+ "!");
					return;
				}

				curNode.setPk_cargdoc(CargdocBO_ClientExt.insert(curNode,
						parentVO == null ? null : parentVO.getPk_cargdoc()));
			} catch (BusinessException re) {
				this.showErrorMessage(re.getMessage());
				return;
			} catch (Exception e) {
				this.showErrorMessage(e.toString());
				return;
			} // 保存成功，修改本地内容
			DefaultMutableTreeNode me = new DefaultMutableTreeNode(curNode
					.getCscode()
					+ " " + curNode.getCsname());
			me.setUserObject(curNode);
			this.m_tm.insertNodeInto(me, parent, parent.getChildCount());
			if (parent.getParent() != null) // 修改上级节点末级标志。
			{
				parentVO.setEndflag(new UFBoolean("N"));
			} // 刷新显示
			this.getNodePanel1().setNode(parentVO, null,
					getStoreRef().getRefPK());
			this.m_iCurrentStatus = 0;
			this.switchButtonStatu(0);
			getNodePanel1().switchStatu(0);
			this.getTreeList1().setEnabled(true);
		} else if (this.m_iCurrentStatus == 2) {
			CargdocVO curNode = null;
			try {
				curNode = this.getNodePanel1().getCurEditNode();
				// lxdong 增加
				// sxj 修改时不用检查。 2004-06-15
				DefaultMutableTreeNode me = (DefaultMutableTreeNode) this
						.getTreeList1().getSelectionPath()
						.getLastPathComponent();
				CargdocVO meVO = null;
				if (me.getParent() != null) {
					meVO = (CargdocVO) me.getUserObject();
				}

				if (curNode.getIsRMPlace().booleanValue()
						&& (meVO != null && !meVO.getIsRMPlace().booleanValue())
						&& CargdocBO_Client.queryByStorCodeIsRmplace(
								getStoreRef().getRefPK(), new UFBoolean("Y"))) {

					showErrorMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("10081004", "UPP10081004-000020")/*
																		 * @res
																		 * "只能设置一个收货货位"
																		 */
							+ "!");
					return;
				}

				CargdocBO_ClientExt.update(curNode);
			} catch (BusinessException re) {
				this.showErrorMessage(re.getMessage());
				return;
			} catch (Exception e) {
				this.showErrorMessage(e.toString());
				return;
			} // 保存成功，修改本地内容
			DefaultMutableTreeNode me = (DefaultMutableTreeNode) this
					.getTreeList1().getSelectionPath().getLastPathComponent();
			me.setUserObject(curNode);
			java.util.Enumeration child = me.breadthFirstEnumeration();
			while (true) {
				if (!child.hasMoreElements())
					break;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) child
						.nextElement();
				CargdocVO v = (CargdocVO) node.getUserObject();
				v.setSealflag(curNode.getSealflag());
			}

			this.m_iCurrentStatus = 0;
			this.switchButtonStatu(0);
			getNodePanel1().switchStatu(0);
			this.getTreeList1().setEnabled(true);
		}
		this.getStoreRef().setEnabled(true);
		this.showHintMessage(nc.vo.bd.BDMsg.MSG_DATA_SAVE_SUCCESS());
		// 根据编码规则设置按钮状态.
		valueChanged(null);
	}

	public void onOutput() {
		showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESS_FINISHED());
	}

	public void onPreView() {
		// showHintMessage("预览数据!");
	}

	public void onPrint() {
		java.util.Vector d = new java.util.Vector();

		java.util.Enumeration e = ((DefaultMutableTreeNode) m_tm.getRoot())
				.preorderEnumeration();
		e.nextElement();
		while (e.hasMoreElements()) {
			d.addElement(((DefaultMutableTreeNode) e.nextElement())
					.getUserObject());
		}

		if (d == null || d.size() == 0) {
			showHintMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		String[][] colname = new String[][] { {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UC000-0003834")/* @res "货位编码" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UC000-0003833")/* @res "货位名称" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UPP10081004-000021")/* @res "保管员" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UPP10081004-000022")/* @res "暂封标志" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UPP10081004-000023")/* @res "货位容量" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UC000-0001376")/* @res "备注" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UPP10081004-000024")/* @res "级次" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UPP10081004-000025") /* @res "是否末级" */} };

		Object[][] data = new Object[d.size()][8];
		String old_pk = getNodePanel1().getPersonRef().getRefPK();
		for (int i = 0; i < d.size(); i++) {
			CargdocVO me = (CargdocVO) d.elementAt(i);
			data[i][0] = me.getCscode();
			data[i][1] = me.getCsname();
			getNodePanel1().getPersonRef().setPK(me.getPk_psndoc());
			data[i][2] = getNodePanel1().getPersonRef().getRefName();
			data[i][3] = me.getSealflag();
			data[i][4] = me.getVolume();
			data[i][5] = me.getMemo();
			data[i][6] = me.getCodelev();
			data[i][7] = me.getEndflag();
		}
		getNodePanel1().getPersonRef().setPK(old_pk);
		int[] colwidth = new int[] { 80, 100, 50, 50, 80, 120, 60, 60 };
		int[] alignflag = new int[] { 0, 0, 0, 0, 2, 0, 2, 0 };
		String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
				"UPP10081004-000018")/* @res "货位档案" */;
		Font font = new java.awt.Font("dialog", java.awt.Font.BOLD, 30);
		Font font1 = new java.awt.Font("dialog", java.awt.Font.PLAIN, 12);
		String topstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
				"UC000-0000404")/* @res "公司" */
				+ ":"
				+ getClientEnvironment().getCorporation().getUnitname()
				+ "   "
				+ nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UC000-0000153")/* @res "仓库" */
				+ ":" + getStoreRef().getRefName();
		String botstr = nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
				"UC000-0000674")/* @res "制表人" */
				+ ":"
				+ getClientEnvironment().getUser().getUserName()
				+ "    "
				+ nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
						"UC000-0000677")/* @res "制表日期" */
				+ ":" + getClientEnvironment().getDate();
		//
		PrintDirectEntry print = new PrintDirectEntry();
		print.setTitle(title); // 标题 可选
		print.setTitleFont(font); // 标题字体 可选
		print.setContentFont(font1); // 内容字体（表头、表格、表尾） 可选
		print.setTopStr(topstr); // 表头信息 可选
		print.setBottomStr(botstr); // 表尾信息 可选
		print.setColNames(colname); // 表格列名（二维数组形式）
		print.setData(data); // 表格数据
		print.setColWidth(colwidth); // 表格列宽 可选
		print.setAlignFlag(alignflag); // 表格每列的对齐方式（0-左, 1-中, 2-右）可选
		// print.setCombinCellRange(combineCellRange); //多表头部分――合并单元格范围
		print.preview(); // 预览
	}

	// public void onRefresh() {
	// setButton(m_boUpdate, false);
	// setButton(m_boDelete, false);
	// getTreeList1().removeTreeSelectionListener(this);
	// m_tm = new DefaultTreeModel(new DefaultMutableTreeNode(
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("10081004",
	// "UPP10081004-000018")/* @res "货位档案" */), false);
	// getTreeList1().setModel(m_tm);
	//
	// if (getStoreRef().getRefPK() == null) {
	// this.showHintMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
	// getTreeList1().addTreeSelectionListener(this);
	// getTreeList1().setSelectionInterval(0, 0);
	// return;
	// }
	//
	// try {
	// showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESSING());
	// ICargdoc service = NCLocator.getInstance().lookup(ICargdoc.class);
	// m_data = service.queryAllByStordoc(
	// getStoreRef().getRefPK(), null, null);
	// // 因为原constructTree方法递归太深，造成溢出错误（StackOverflowError）
	// // 所以从写了constructNewTree方法
	// constructNewTree(m_data);
	// // constructTree(m_tm, null, 0);
	// showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESS_FINISHED());
	// } catch (BusinessException re) {
	// this.showErrorMessage(re.getMessage());
	// return;
	// } catch (Exception e) {
	// handleException(e);
	// showErrorMessage(e.toString());
	// return;
	// }
	// getTreeList1().addTreeSelectionListener(this);
	// getTreeList1().setSelectionInterval(0, 0);
	//
	// }

	public void onRefresh() {
		setButton(m_boUpdate, false);
		setButton(m_boDelete, false);
		getTreeList1().removeTreeSelectionListener(this);
		m_tm = new DefaultTreeModel(new DefaultMutableTreeNode(NCLangRes
				.getInstance().getStrByID("10081004", "UPP10081004-000018")),
				false);
		getTreeList1().setModel(m_tm);
		if (getStoreRef().getRefPK() == null) {
			showHintMessage(BDMsg.MSG_CHOOSE_DATA());
			getTreeList1().addTreeSelectionListener(this);
			getTreeList1().setSelectionInterval(0, 0);
			return;
		}
		try {
			showHintMessage(BDMsg.MSG_PROCESSING());
			m_data = CargdocBO_Client.queryAllByStordoc(getStoreRef()
					.getRefPK());
			constructNewTree(m_data);
			showHintMessage(BDMsg.MSG_PROCESS_FINISHED());
		} catch (BusinessException re) {
			showErrorMessage(re.getMessage());
			return;
		} catch (Exception e) {
			handleException(e);
			showErrorMessage(e.toString());
			return;
		}
		getTreeList1().addTreeSelectionListener(this);
		getTreeList1().setSelectionInterval(0, 0);
	}

	public void onSetup() {
		showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESS_FINISHED());
	}

	public void onUpdate() {
		if (getStoreRef().getRefPK() == null) {
			this.showHintMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}

		DefaultMutableTreeNode selected = ((DefaultMutableTreeNode) (this
				.getTreeList1().getSelectionPath().getLastPathComponent()));

		if (selected == null || selected.getParent() == null) {
			this.showHintMessage(nc.vo.bd.BDMsg.MSG_CHOOSE_DATA());
			return;
		}
		this.getStoreRef().setEnabled(false);
		this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"10081004", "UC001-0000045")/* @res "修改" */);
		this.m_iCurrentStatus = 2;
		this.switchButtonStatu(2);
		this.getNodePanel1().switchStatu(
				2,
				LocalModel.isLastLevel(((CargdocVO) selected.getUserObject())
						.getCscode()));
		this.getTreeList1().setEnabled(false);

		if (selected.getParent() == null) {
			this.getNodePanel1().setNode(null,
					(CargdocVO) (selected.getUserObject()),
					getStoreRef().getRefPK());
		} else if (selected.getParent().getParent() == null) {
			this.getNodePanel1().setNode(null,
					(CargdocVO) (selected.getUserObject()),
					getStoreRef().getRefPK());
		} else {
			this.getNodePanel1()
					.setNode(
							(CargdocVO) (((DefaultMutableTreeNode) selected
									.getParent()).getUserObject()),
							(CargdocVO) (selected.getUserObject()),
							getStoreRef().getRefPK());
		}

	}

	/**
	 * 设置按钮状态 创建日期：(2000-8-17 16:26:15)
	 * 
	 * @param bo
	 *            ierp.sm.core.ui.ButtonObject
	 * @param b
	 *            boolean
	 */
	private void setButton(ButtonObject bo, boolean b) {
		bo.setEnabled(b);
		this.updateButton(bo);
	}

	/**
	 * 在此处插入方法说明。 创建日期：(2000-8-17 17:00:47)
	 * 
	 * @param status
	 *            int
	 */
	protected void switchButtonStatu(int status) {
		if (status == 0 || status == 3) // 浏览
		{
			// setButton(m_boSetup, true);
			setButton(m_boPrint, true);
			// setButton(m_boPreView, true);
			// setButton(m_boOutput, true);
			setButton(m_boInsert, true);
			setButton(m_boUpdate, true);
			setButton(m_boDelete, true);
			setButton(m_boOK, false);
			setButton(m_boCancel, false);
			setButton(m_boRefresh, true);
			if (getTreeList1().getSelectionPath() == null
					|| getTreeList1().getSelectionPath().getPathCount() == 1) {
				setButton(m_boUpdate, false);
				setButton(m_boDelete, false);
			}
		} else if (status == 1 || status == 2) // 增加、修改
		{
			// setButton(m_boSetup, false);
			setButton(m_boPrint, false);
			// setButton(m_boPreView, false);
			// setButton(m_boOutput, false);
			setButton(m_boInsert, false);
			setButton(m_boUpdate, false);
			setButton(m_boDelete, false);
			setButton(m_boOK, true);
			setButton(m_boCancel, true);
			setButton(m_boRefresh, false);
		}

	}

	/**
	 * 在此处插入方法说明。 创建日期：(00-8-1 20:57:31)
	 */

	public void valueChanged(TreeSelectionEvent e) {
		if (getTreeList1().getSelectionPath() == null)
			return;
		DefaultMutableTreeNode selected = (DefaultMutableTreeNode) getTreeList1()
				.getSelectionPath().getLastPathComponent();
		if (selected.getParent() == null) {
			getNodePanel1().setNode(null, null, getStoreRef().getRefPK());
			setButton(m_boDelete, false);
			setButton(m_boUpdate, false);
		} else {
			setButton(m_boUpdate, true);
			getNodePanel1().setNode(null, (CargdocVO) selected.getUserObject(),
					getStoreRef().getRefPK());
			if (LocalModel.isLastLevel(((CargdocVO) selected.getUserObject())
					.getCscode()))
				setButton(m_boInsert, false);
			else
				setButton(m_boInsert, true);
			setButton(m_boDelete, true);
		}
	}

	// public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
	// if (this.getTreeList1().getSelectionPath() == null)
	// return;
	// DefaultMutableTreeNode selected = (DefaultMutableTreeNode) this
	// .getTreeList1().getSelectionPath().getLastPathComponent();
	// if (selected.getParent() == null) {
	// this.getNodePanel1().setNode(null, null, getStoreRef().getRefPK());
	// setButton(this.m_boDelete, false);
	// setButton(this.m_boUpdate, false);
	// } else {
	// setButton(this.m_boUpdate, true);
	// this.getNodePanel1().setNode(null,
	// (CargdocVO) selected.getUserObject(),
	// getStoreRef().getRefPK());
	// if (LocalModel.isLastLevel(((CargdocVO) selected.getUserObject())
	// .getCscode())) {
	// setButton(this.m_boInsert, false);
	// } else {
	// setButton(this.m_boInsert, true);
	// }
	// setButton(this.m_boDelete, true);
	// // if(selected.getChildCount()==0)
	// // {
	// // setButton(this.m_boDelete,true);
	// // }
	// // else
	// // {
	// // setButton(this.m_boDelete,false);
	// // }
	// }
	// if (selected.getUserObject() instanceof CargdocVO) {
	// if (selected.getChildCount() == 0) {
	// CargdocVO current = (CargdocVO) selected.getUserObject();
	// ICargdoc service = NCLocator.getInstance().lookup(
	// ICargdoc.class);
	// try {
	// CargdocVO[] subDocs = service
	// .queryCargdocVOsByStorePkAndIsrmAndSuperCode(
	// getStoreRef().getRefPK(), current
	// .getCscode(), null);
	// if (subDocs != null && subDocs.length > 0) {
	// List<CargdocVO> allDoc = new ArrayList<CargdocVO>();
	// allDoc.addAll(Arrays.asList(m_data));
	// allDoc.addAll(Arrays.asList(subDocs));
	// m_data = allDoc.toArray(new CargdocVO[0]);
	// for (CargdocVO subDoc : subDocs) {
	// DefaultMutableTreeNode me = new DefaultMutableTreeNode(
	// subDoc.getCscode() + " "
	// + subDoc.getCsname());
	// me.setUserObject(subDoc);
	// m_tm.insertNodeInto(me, selected, selected
	// .getChildCount());
	// }
	// }
	// } catch (BusinessException e1) {
	// Logger.error(e1.getMessage(), e1);
	// showErrorMessage(BDMsg.MSG_DATA_LOAD_FAIL());
	// }
	// }
	// }
	// }

	/**
	 * Comment
	 */
	public void WarehouseChanged() {
		onRefresh();
		return;
	}

	@Override
	public boolean onClosing() {
		if (m_iCurrentStatus == 1 || m_iCurrentStatus == 2) {
			int i = showYesNoMessage(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("uifactory", "UPPuifactory-000058")/*
																	 * @res
																	 * "正在编辑状态，未保存的数据将会丢失，是否退出？"
																	 */);
			if (i == nc.ui.pub.beans.MessageDialog.ID_YES) {
				return true;
			} else {
				return false;
			}
		}
		return super.onClosing();
	}

	public void doMaintainAction(ILinkMaintainData maintaindata) {
		String pk_bill = maintaindata.getBillID();
		if (StringUtils.isBlank(pk_bill))
			return;

		CargdocVO selectedVO = null;

		IUAPQueryBS queryService = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());

		try {
			selectedVO = (CargdocVO) queryService.retrieveByPK(CargdocVO.class,
					pk_bill, new String[] { "pk_stordoc", "cscode" });
		} catch (BusinessException ex) {
			Logger.error(ex.getMessage(), ex);
			MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
					.getStrByID("_bill", "UPP_Bill-000034")/* @res "提示" */,
					NCLangRes.getInstance().getStrByID("_beans",
							"UPP_Beans-000079")/* @res "定位" */
							+ NCLangRes.getInstance().getStrByID("common",
									"UC001-0000053")/* @res "数据" */
							+ NCLangRes.getInstance().getStrByID("_bill",
									"UPP_Bill-000262")/* @res "为空" */)/*
																	 * @res
																	 * "定位数据为空"
																	 */;
		}

		// 设置仓库档案的参照PK
		getStoreRef().setPK(selectedVO.getPk_stordoc());
		// 加载数据到树上(懒加载，只加载第一级节点)
		onRefresh();

		UITree entireTree = getTreeList1();

		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) entireTree
				.getModel().getRoot();

		String[] parentCodes = parseParentCodes(selectedVO.getCscode());
		for (int i = 0; i < parentCodes.length; i++) {
			// 定位父节点
			parentNode = locateInParentNodeByCode(parentNode, parentCodes[i]);
			if (parentNode != null) {
				// 因为是懒加载树，所以每次setSelectionPath来触发valueChanged事件，加载下一级儿子节点
				entireTree.setSelectionPath(new TreePath(parentNode.getPath()));
			}
		}

		DefaultMutableTreeNode selectedNode = locateInParentNodeByPk(
				(DefaultMutableTreeNode) entireTree.getModel().getRoot(),
				selectedVO.getPrimaryKey());

		if (selectedNode != null)
			entireTree.setSelectionPath(new TreePath(selectedNode.getPath()));
		else {
			MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
					.getStrByID("_bill", "UPP_Bill-000034")/* @res "提示" */,
					NCLangRes.getInstance().getStrByID("_beans",
							"UPP_Beans-000079")/* @res "定位" */
							+ NCLangRes.getInstance().getStrByID("common",
									"UC001-0000053")/* @res "数据" */
							+ NCLangRes.getInstance().getStrByID("_bill",
									"UPP_Bill-000262")/* @res "为空" */)/*
																	 * @res
																	 * "定位数据为空"
																	 */;
		}

	}

	/**
	 * 在指定的父结点和要定位的档案主键下查找要定位的档案.
	 * 
	 * @param parentNode
	 * @param locatingPk
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DefaultMutableTreeNode locateInParentNodeByPk(
			DefaultMutableTreeNode parentNode, String locatingPk) {
		DefaultMutableTreeNode selectedNode = null;
		Enumeration<DefaultMutableTreeNode> enumberation = parentNode
				.preorderEnumeration();
		enumberation.nextElement();

		while (enumberation.hasMoreElements()) {
			DefaultMutableTreeNode currentNode = enumberation.nextElement();
			CargdocVO currentVO = (CargdocVO) currentNode.getUserObject();
			if (currentVO.getPrimaryKey().equals(locatingPk)) {
				selectedNode = currentNode;
				break;
			}
		}
		return selectedNode;
	}

	/**
	 * 根据指定的货位档案编码解析其所有父结点的编码.
	 * 
	 * @param currentCode
	 * @return
	 */
	private String[] parseParentCodes(String currentCode) {
		CodeRuleLegal rule = new CodeRuleLegal("2.2.2.2");
		List<String> rsList = new ArrayList<String>();
		try {
			while (true) {
				String parentCode = rule.getParentCode(currentCode);
				if (parentCode != null && parentCode.trim().length() > 0) {
					// 避免根节点没有编码的情况
					rsList.add(parentCode);
				}
				currentCode = parentCode;
			}
		} catch (ValidationException e) {
			Logger.error(e.getMessage());
			// 据此结束循环
		}
		Collections.reverse(rsList);
		return rsList.toArray(new String[0]);

	}

	/**
	 * 在指定的父结点和要定位的档案编码下查找要定位的档案.
	 * 
	 * @param parentNode
	 * @param locatingCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DefaultMutableTreeNode locateInParentNodeByCode(
			DefaultMutableTreeNode parentNode, String locatingCode) {
		DefaultMutableTreeNode selectedNode = null;
		Enumeration<DefaultMutableTreeNode> enumberation = parentNode
				.preorderEnumeration();
		enumberation.nextElement();

		while (enumberation.hasMoreElements()) {
			DefaultMutableTreeNode currentNode = enumberation.nextElement();
			CargdocVO currentVO = (CargdocVO) currentNode.getUserObject();
			if (currentVO.getCscode().equals(locatingCode)) {
				selectedNode = currentNode;
				break;
			}
		}
		return selectedNode;
	}

}