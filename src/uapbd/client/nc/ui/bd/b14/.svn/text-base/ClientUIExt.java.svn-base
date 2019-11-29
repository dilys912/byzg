package nc.ui.bd.b14;

import java.awt.Font;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.print.PrintDirectEntry;
import nc.vo.bd.BDMsg;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.access.tree.BDTreeCreator;
import nc.vo.bd.access.tree.CAVONodeTreeCreateStrategy;
import nc.vo.bd.b14.InvclVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

/**
 * 此处插入类型说明。 创建日期：(2001-05-30 10:01:04)
 *
 * @author：祝奇
 */
public class ClientUIExt extends nc.ui.pub.ToftPanel implements
		javax.swing.event.TreeSelectionListener {
	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	
	private static final long serialVersionUID = 1113549209239426012L;

	private nc.ui.pub.beans.UISplitPane ivjUISplitPane1 = null;

	private NodePanelExt ivjNodePanel1 = null;

	private nc.ui.pub.beans.UITree ivjTreeList1 = null;

	private ButtonObject m_boSetup = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000002")/*@res "设置"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000002")/*@res "设置"*/, 2,"设置");	/*-=notranslate=-*/

	private ButtonObject m_boPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000007")/*@res "打印"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000007")/*@res "打印"*/, 2,"打印");	/*-=notranslate=-*/

	private ButtonObject m_boPreView = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000003")/*@res "预览"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000003")/*@res "预览"*/, 2,"预览");	/*-=notranslate=-*/

	private ButtonObject m_boOutput = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000004")/*@res "输出"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000004")/*@res "输出"*/, 2,"输出");	/*-=notranslate=-*/

	private ButtonObject m_boInsert = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000002")/*@res "增加"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000002")/*@res "增加"*/, 2,"增加");	/*-=notranslate=-*/

	private ButtonObject m_boUpdate = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000045")/*@res "修改"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000045")/*@res "修改"*/, 2,"修改");	/*-=notranslate=-*/

	private ButtonObject m_boDelete = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000039")/*@res "删除"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000039")/*@res "删除"*/, 2,"删除");	/*-=notranslate=-*/

	private ButtonObject m_boOK = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000001")/*@res "保存"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000001")/*@res "保存"*/, 2,"保存");	/*-=notranslate=-*/

	private ButtonObject m_boCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000008")/*@res "取消"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000008")/*@res "取消"*/, 2,"取消");	/*-=notranslate=-*/

	private ButtonObject m_boRefresh = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000009")/*@res "刷新"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000009")/*@res "刷新"*/, 2,"刷新");	/*-=notranslate=-*/

	private ButtonObject[] m_MainButtonGroup = { m_boInsert, m_boUpdate,
			m_boDelete, m_boOK, m_boCancel, m_boRefresh,/* m_boSetup, */
			m_boPrint /* m_boPreView, m_boOutput */};

	private int m_iCurrentStatus = 0; //浏览

	private DefaultTreeModel m_tm = new DefaultTreeModel(
			new DefaultMutableTreeNode(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC000-0001443")/*@res "存货分类"*/), false);

	private InvclVO[] m_data = null;

	private nc.ui.pub.beans.UIScrollPane ivjUIScrollPane1 = null;

	private String m_pk_corp = null;

	private int[] iCodingRule;

	private String grpCtlLevel = null;

	private int UPDATE_STATUS=2;

	private int INSERT_STATUS=1;
	
	//关闭窗口时判断标志，
	private boolean isOnClosing=true;

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUIExt() {
		super();
		initialize();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-9-16 16:52:47)
	 */
	public boolean checkSealState(DefaultMutableTreeNode node) {
		if (node.getParent() == null) {
			return true;
		} else {
			DefaultMutableTreeNode fatherNode = (DefaultMutableTreeNode) node
					.getParent();
			Object fatherVO = fatherNode.getUserObject();
			if (fatherVO instanceof InvclVO) {
				InvclVO vo = (InvclVO) fatherVO;
				if (vo.getSealdate() != null) {
					return false;
				} else
					return checkSealState(fatherNode);
			}

			else {

				return true;
			}

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

		int meLevel = m_data[meIndex].getInvclasslev().intValue();
		String meEndFlag = m_data[meIndex].getEndflag().toString();
		if (meLevel == parentLevel + 1) {
			DefaultMutableTreeNode me = new DefaultMutableTreeNode(
					m_data[meIndex].getInvclasscode() + " "
							+ m_data[meIndex].getInvclassname());
			me.setUserObject(m_data[meIndex]);
			tm.insertNodeInto(me, parent, parent.getChildCount());
			if (meEndFlag.equals("N"))
				constructTree(tm, me, meIndex + 1);
			else
				constructTree(tm, parent, meIndex + 1);
		} else {
			constructTree(tm, (DefaultMutableTreeNode) parent.getParent(),
					meIndex);
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
	public void constructTreeByLoop(DefaultTreeModel tm) {
		
		if (m_data == null)
			return;

		String coderule="";
		for (int i = 0; i < iCodingRule.length; i++) {
			if(i!=iCodingRule.length-1)
				coderule+=iCodingRule[i]+"/";
			else
				coderule+=iCodingRule[i];
		}
		final DefaultMutableTreeNode root = (DefaultMutableTreeNode) tm.getRoot();
		tm=BDTreeCreator.createTree(m_data,new CAVONodeTreeCreateStrategy(coderule,"invclasscode") {
			public DefaultMutableTreeNode getRootNode() {
				return root;
			}
		});
		m_tm=tm;
	}

	/**
	 * 返回 NodePanel1 特性值。
	 *
	 * @return nc.ui.bd.b14.NodePanelExt
	 */
	/* 警告：此方法将重新生成。 */
	private NodePanelExt getNodePanel1() {
		if (ivjNodePanel1 == null) {
			try {
				
				ivjNodePanel1 = new nc.ui.bd.b14.NodePanelExt();
				ivjNodePanel1.setName("NodePanel1");
//				ivjNodePanel1
//						.setPreferredSize(new java.awt.Dimension(100, 100));
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
	 * 此处插入方法说明。 创建日期：(2003-2-20 9:39:54)
	 *
	 * @return java.lang.String
	 */
	public java.lang.String getPk_corp() {
		return m_pk_corp;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 *
	 * @version (00-6-6 13:33:25)
	 *
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC000-0001443")/*@res "存货分类"*/;
	}

	/**
	 * 返回 UITree1 特性值。
	 *
	 * @return nc.ui.pub.beans.UITree
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITree getTreeList1() {
		if (ivjTreeList1 == null) {
			try {
				ivjTreeList1 = new nc.ui.pub.beans.UITree();
				ivjTreeList1.setName("TreeList1");
				ivjTreeList1.setAutoscrolls(true);
//				ivjTreeList1.setPreferredSize(new java.awt.Dimension(200,
//						500));
//				ivjTreeList1.setBounds(0, 0, 350, 400);
				// user code begin {1}
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
				ivjUIScrollPane1.setAutoscrolls(true);
				ivjUIScrollPane1.setPreferredSize(new java.awt.Dimension(350,
						500));
				ivjUIScrollPane1
						.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjUIScrollPane1
						.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
				
				UIScrollPane scrPnl = new UIScrollPane();
				scrPnl.setPreferredSize(new java.awt.Dimension(400,200));
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
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ClientUI");
			setSize(774, 419);
			add(getUISplitPane1(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		//设置按钮
		this.setButtons(this.m_MainButtonGroup);
		//设置当前公司
		setPk_corp(nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
				.getPk_corp());

		switchButtonStatu(0);
		setButton(m_boUpdate, false);
		setButton(m_boDelete, false);
		//取得编码级次,集团控制到几级
		try {
			//重读参数。
			LocalModel.initPara();
			iCodingRule = LocalModel.getCodeRule();
			grpCtlLevel = LocalModel.getGrpCtllevel();
		} catch (Exception e) {
			e.printStackTrace();
		}

		onRefresh();
		// user code end
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-9-16 16:52:47)
	 */
	public boolean isFatherNodeSealed(DefaultMutableTreeNode node) {
		Object fatherVO = node.getUserObject();
		if (fatherVO instanceof InvclVO) {
			InvclVO vo = (InvclVO) fatherVO;
			if (vo.getSealdate() != null) {
				return true;
			} else
				return false;
		}

		else {

			return false;
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-7-4 20:05:23) 是否为集团登录
	 *
	 * @return boolean
	 */
	public boolean isGrpLogin() {

		if (nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
				.getPk_corp().equals("0001")) {
			return true;
		}
		return false;
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
			ClientUI aClientUI;
			aClientUI = new ClientUI();
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
	}

	public void onCancel() {
//		this.showHintMessage("正准备取消本次操作...");
		this.m_iCurrentStatus = 0;
		DefaultMutableTreeNode me = (DefaultMutableTreeNode) this
				.getTreeList1().getSelectionPath().getLastPathComponent();
		InvclVO meVO = null;
		if (me.getParent() != null) {
			meVO = (InvclVO) me.getUserObject();
		}
		this.getNodePanel1().setNode(meVO, null);
		this.getTreeList1().setEnabled(true);
		this.switchButtonStatu(0);
		this.getNodePanel1().switchStatu(0);
		valueChanged(null);
//		this.showHintMessage("本次操作取消完毕。");
	}

	public void onDelete() {

		DefaultMutableTreeNode me = (DefaultMutableTreeNode) this
				.getTreeList1().getSelectionPath().getLastPathComponent();
		if (me == null)
			return;

		if (nc.ui.pub.beans.MessageDialog.showOkCancelDlg(this, null,nc.vo.bd.BDMsg.MSG_SURE_DELETE()) != nc.ui.pub.beans.MessageDialog.ID_OK)
			return;
//		showHintMessage("正在删除...");
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) me.getParent();

		InvclVO parentVO = null;
		if (parent.getParent() != null) {
			parentVO = (InvclVO) parent.getUserObject();
		}
		InvclVO curNode = null;
		boolean isParentHaveChildren = true;
		try {
			curNode = (InvclVO) me.getUserObject();
			isParentHaveChildren = InvclBO_ClientExt.delete(curNode,
					parentVO == null ? null : parentVO.getPk_invcl());
		} catch (BusinessException re) {
			this.showErrorMessage(re.getMessage());
			return;
		} catch (Exception e) {
			e.printStackTrace();
//			this.showHintMessage(e.toString());
			return;
		}

		//保存成功，修改本地内容

		if (!isParentHaveChildren && parentVO != null) //修改上级节点末级标志。
		{
			parentVO.setEndflag(new UFBoolean("Y"));
		}

		this.m_tm.removeNodeFromParent(me);
		getTreeList1().setSelectionInterval(0, 0);
		showHintMessage(nc.vo.bd.BDMsg.MSG_DATA_DELETE_SUCCESS());

	}

	/**
	 * 在此处插入方法说明。 创建日期：(00-8-2 10:37:16)
	 */
	public void onInsert() {
		
		DefaultMutableTreeNode selected = ((DefaultMutableTreeNode) (this
				.getTreeList1().getSelectionPath().getLastPathComponent()));

		if (isFatherNodeSealed(selected)) {
			showErrorMessage(MultiLangTrans.getTransStr("MO4",new String[]{nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000005")/*@res "封存"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000002")/*@res "增加"*/}));
			return;
		}
		this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000002")/*@res "增加"*/);
		this.m_iCurrentStatus = 1;
		this.switchButtonStatu(1);
		this.getNodePanel1().switchStatu(1);
		this.getTreeList1().setEnabled(false);

		if (selected.getParent() == null) {
			this.getNodePanel1().setNode(null, null);
		} else {
			this.getNodePanel1().setNode((InvclVO) (selected.getUserObject()),
					null);
		}

	}

	/**
	 * 在此处插入方法说明。 创建日期：(00-8-2 10:37:16)
	 */
	public void onOK() {
		showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESSING());
		if (this.m_iCurrentStatus == 1) {
			TreePath treePath = getTreeList1().getSelectionPath();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) this
					.getTreeList1().getSelectionPath().getLastPathComponent();
			InvclVO parentVO = null;
			if (parent.getParent() != null) {
				parentVO = (InvclVO) parent.getUserObject();
			}
			InvclVO curNode = null;
			try {
				curNode = this.getNodePanel1().getCurNewNode();
				curNode.setPk_corp(getPk_corp());
				curNode.setPk_invcl(InvclBO_ClientExt.insert(curNode,
						parentVO == null ? null : parentVO.getPk_invcl()));
				//从数据库中读出InvclVO（包含dr，ts等字段）
				curNode = InvclBO_ClientExt.findByPrimaryKey(curNode.m_pk_invcl);
			} catch (BusinessException re) {
				this.showErrorMessage(re.getMessage());
				this.isOnClosing=false;
				return;
			} catch (Exception e) {
				e.printStackTrace();
//				this.showErrorMessage(e.toString());
				return;
			}
			//保存成功，修改本地内容

			DefaultMutableTreeNode me = new DefaultMutableTreeNode(curNode
					.getInvclasscode()
					+ " " + curNode.getInvclassname());
			me.setUserObject(curNode);
			this.m_tm.insertNodeInto(me, parent, parent.getChildCount());

			if (parent.getParent() != null) //修改上级节点末级标志。
			{
				parentVO.setEndflag(new UFBoolean("N"));
			}
			//刷新显示
			this.getNodePanel1().setNode(parentVO, null);

			this.m_iCurrentStatus = 0;
			this.switchButtonStatu(0);
			getNodePanel1().switchStatu(0);
			this.getTreeList1().setEnabled(true);
//			TreePath treePath = new TreePath(me.getPath());
			getTreeList1().setSelectionPath(treePath);
		} else if (this.m_iCurrentStatus == 2) {
			TreePath tp = getTreeList1().getSelectionPath();
			InvclVO curNode = null;
			DefaultMutableTreeNode me = (DefaultMutableTreeNode) this
					.getTreeList1().getSelectionPath().getLastPathComponent();

			try {
				curNode = this.getNodePanel1().getCurEditNode();
				//检查解封的条件
				if (curNode.getSealdate() == null) {
					if (!checkSealState(me)) {
						this.showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000006")/*@res "存货分类的上级存货分类必须全部解封"*/);
						return;
					}
				}

				InvclBO_ClientExt.update(curNode);
				//从数据库中读出InvclVO（包含dr，ts等字段）
				curNode = InvclBO_ClientExt.findByPrimaryKey(curNode.m_pk_invcl);
			} catch (BusinessException re) {
				this.showErrorMessage(re.getMessage());
				this.isOnClosing=false;
				return;
			} catch (Exception e) {
				e.printStackTrace();
//				this.showErrorMessage(e.toString());
				return;
			}
			//保存成功，修改本地内容

			me.setUserObject(curNode);

			this.m_iCurrentStatus = 0;
			this.switchButtonStatu(0);
			getNodePanel1().switchStatu(0);
			this.getTreeList1().setEnabled(true);
			this.getTreeList1().requestFocus();
			this.getTreeList1().updateUI();
			getTreeList1().setSelectionPath(tp);
		}
		showHintMessage(nc.vo.bd.BDMsg.MSG_DATA_SAVE_SUCCESS());
		valueChanged(null);
		this.isOnClosing=true;
	}

	public void onOutput() {
		showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESS_FINISHED());
	}

	public void onPreView() {
//		showHintMessage("预览数据!");
	}

	public void onPrint() {
        Vector d = new Vector();
        Enumeration e = ((DefaultMutableTreeNode)m_tm.getRoot()).preorderEnumeration();
        e.nextElement();
        while(e.hasMoreElements()) 
            try
            {
                d.addElement(((DefaultMutableTreeNode)e.nextElement()).getUserObject());
            }
            catch(Exception ex) { }
        if(d == null || d.size() == 0)
        {
            showHintMessage(BDMsg.MSG_CHOOSE_DATA());
            return;
        }
        String colname[][] = {
            {
                NCLangRes.getInstance().getStrByID("10081202", "UC000-0001449"), NCLangRes.getInstance().getStrByID("10081202", "UC000-0001446"), NCLangRes.getInstance().getStrByID("10081202", "UC000-0001775"), NCLangRes.getInstance().getStrByID("10081202", "UPP10081202-000007"), NCLangRes.getInstance().getStrByID("10081202", "UPP10081202-000008"), NCLangRes.getInstance().getStrByID("10081202", "UPP10081202-000009"), NCLangRes.getInstance().getStrByID("10081202", "UPP10081202-000010"), NCLangRes.getInstance().getStrByID("10081202", "UPP10081202-000011")
            }
        };
        Object data[][] = new Object[d.size()][8];
        for(int i = 0; i < d.size(); i++)
        {
            InvclVO me = (InvclVO)d.elementAt(i);
            data[i][0] = me.getInvclasscode();
            data[i][1] = me.getInvclassname();
            data[i][2] = me.getAvgprice();
            data[i][3] = me.getAveragecost();
            data[i][4] = me.getAveragepurahead();
            data[i][5] = me.getAveragemmahead();
            data[i][6] = me.getInvclasslev();
            data[i][7] = me.getEndflag();
        }

        int colwidth[] = {
            150, 250, 100, 100, 100, 100, 60, 60
        };
        int alignflag[] = {
            0, 0, 2, 2, 2, 2, 2, 0
        };
        String title = NCLangRes.getInstance().getStrByID("10081202", "UC000-0001443");
        Font font = new Font("dialog", 1, 30);
        Font font1 = new Font("dialog", 0, 12);
        String topstr = NCLangRes.getInstance().getStrByID("10081202", "UPP10081202-000012");
        String botstr = (new StringBuilder()).append(NCLangRes.getInstance().getStrByID("10081202", "UC000-0000674")).append(":").append(getClientEnvironment().getUser().getUserName()).append("    ").append(NCLangRes.getInstance().getStrByID("10081202", "UC000-0000677")).append(":").append(getClientEnvironment().getDate()).toString();
        PrintDirectEntry print = new PrintDirectEntry();
        print.setTitle(title);
        print.setTitleFont(font);
        print.setContentFont(font1);
        print.setTopStr(topstr);
        print.setBottomStr(botstr);
        print.setColNames(colname);
        print.setData(data);
        print.setColWidth(colwidth);
        print.setAlignFlag(alignflag);
        print.preview();
    }

	public void onRefresh() {
		getTreeList1().removeTreeSelectionListener(this);
		m_tm = new DefaultTreeModel(new DefaultMutableTreeNode(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC000-0001443")/*@res "存货分类"*/), false);
		getTreeList1().setModel(m_tm);
		try {
			showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESSING());
			m_data = InvclBO_ClientExt.queryAll(getPk_corp());
			//constructTree(m_tm, null, 0);
			//getCodeRule();
			constructTreeByLoop(m_tm);
			getTreeList1().setModel(m_tm);
			showHintMessage(nc.vo.bd.BDMsg.MSG_PROCESS_FINISHED());
		} catch (BusinessException re) {
			this.showErrorMessage(re.getMessage());
		} catch (Exception e) {
			handleException(e);
			showErrorMessage(e.toString());
		}
		getTreeList1().addTreeSelectionListener(this);
		getTreeList1().setSelectionInterval(0, 0);
		
	}

	public void onSetup() {
//		showHintMessage("设置数据!");
	}

	public void onUpdate() {
		this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UC001-0000045")/*@res "修改"*/);
		this.m_iCurrentStatus = 2;
		this.switchButtonStatu(2);
		this.getNodePanel1().switchStatu(2);
		this.getTreeList1().setEnabled(false);
		DefaultMutableTreeNode selected = ((DefaultMutableTreeNode) (this
				.getTreeList1().getSelectionPath().getLastPathComponent()));
		if (selected.getParent() == null) {
			this.getNodePanel1().setNode(null,
					(InvclVO) (selected.getUserObject()));
		} else if (selected.getParent().getParent() == null) {
			this.getNodePanel1().setNode(null,
					(InvclVO) (selected.getUserObject()));
		} else {
			this.getNodePanel1().setNode(
					(InvclVO) (((DefaultMutableTreeNode) selected.getParent())
							.getUserObject()),
					(InvclVO) (selected.getUserObject()));
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
	 * 根据当前树节点判断是否可以编辑、删除 如果创建公司与当前公司不同，不能编辑 如果创建公司与当前公司不同，不能删除
	 */
	public void setButtonStatusByTreeNode() {
		if (getTreeList1().getSelectionPath() == null) {
			return;
		}
		DefaultMutableTreeNode selected = (DefaultMutableTreeNode) getTreeList1()
				.getSelectionPath().getLastPathComponent();
		if (selected.getParent() == null) {
			this.getNodePanel1().setNode(null, null);
			setButton(this.m_boDelete, false);
			setButton(this.m_boUpdate, false);
		} else {
			//如果创建公司与当前公司不同，不能编辑
			if (((InvclVO) selected.getUserObject()).getPk_corp().equals(
					getPk_corp())) {
				setButton(this.m_boUpdate, true);
			} else {
				setButton(this.m_boUpdate, false);
			}
			this.getNodePanel1().setNode(null,
					(InvclVO) selected.getUserObject());
			try {
				if (LocalModel.isLastLevel(((InvclVO) selected.getUserObject())
						.getInvclasscode())) {
					setButton(this.m_boInsert, false);
				} else {
					setButton(this.m_boInsert, true);
				}
			} catch (Exception ef) {
			}
			if (selected.getChildCount() == 0) {
				//如果创建公司与当前公司不同，不能删除
				if (((InvclVO) selected.getUserObject()).getPk_corp().equals(
						getPk_corp())) {
					setButton(this.m_boDelete, true);
				} else {
					setButton(this.m_boDelete, false);
				}
			} else {
				setButton(this.m_boDelete, false);
			}
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-2-20 9:39:54)
	 *
	 * @param newM_pk_corp
	 *            java.lang.String
	 */
	public void setPk_corp(java.lang.String newM_pk_corp) {
		m_pk_corp = newM_pk_corp;
	}

	/**
	 * 在此处插入方法说明。 创建日期：(2000-8-17 17:00:47)
	 *
	 * @param status
	 *            int
	 */
	protected void switchButtonStatu(int status) {
		if (status == 0 || status == 3) //浏览
		{
			//setButton(m_boSetup, true);
			setButton(m_boPrint, true);
			//setButton(m_boPreView, true);
			//setButton(m_boOutput, true);
			setButton(m_boInsert, true);
			setButton(m_boUpdate, true);
			setButton(m_boDelete, true);
			setButton(m_boOK, false);
			setButton(m_boCancel, false);
			setButton(m_boRefresh, true);
			setButtonStatusByTreeNode();
		} else if (status == 1 || status == 2) //增加、修改
		{
			//setButton(m_boSetup, false);
			setButton(m_boPrint, false);
			//setButton(m_boPreView, false);
			//setButton(m_boOutput, false);
			setButton(m_boInsert, false);
			setButton(m_boUpdate, false);
			setButton(m_boDelete, false);
			setButton(m_boOK, true);
			setButton(m_boCancel, true);
			setButton(m_boRefresh, false);
		}

		int[] rule = null;
		try {
			rule = LocalModel.getCodeRule();
		} catch (Exception e) {
		}
		if (rule == null) {
			setButton(m_boInsert, false);
			setButton(m_boUpdate, false);
			setButton(m_boDelete, false);
			showHintMessage(MultiLangTrans.getTransStr("MT8",new String[]{nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000013")/*@res "存货编码原则"*/}));
		}
	}

	/**
	 * 在此处插入方法说明。 创建日期：(00-8-1 20:57:31)
	 */
	public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
		DefaultMutableTreeNode selected =  null;
				
		int lev = 0;
		try {
			selected = (DefaultMutableTreeNode) getTreeList1().getSelectionPath().getLastPathComponent();
			lev = LocalModel.getlevel(((InvclVO) selected.getUserObject())
					.getInvclasscode());
		} catch (Exception ex) {
//			ex.printStackTrace();
			//lev +=1;
		}
		if (selected == null){
			return;
		}
		//
		
		setButton(this.m_boUpdate, true);
		setButton(this.m_boInsert, true);
		setButton(this.m_boDelete, false);
		
		//集团操作
        if (isGrpLogin()){
        	if (selected.getParent() == null) {
        		this.getNodePanel1().setNode(null, null);
				setButton(this.m_boDelete, false);
				setButton(this.m_boUpdate, false);
				setButton(this.m_boInsert, true);
        	}else{
        		//增加,修改按钮
        		setButton(this.m_boUpdate, true);
        		
				this.getNodePanel1().setNode(null,
				(InvclVO) selected.getUserObject());
				// 删除按钮,末级才可以删除
				if (selected.isLeaf()) {
					setButton(this.m_boDelete, true);
				}
	    }
        	try {
				if (LocalModel.isLastLevel(((InvclVO) selected.getUserObject())
						.getInvclasscode())) {
					setButton(this.m_boInsert, false);
				} 
				
			} catch (Exception ef) {
			}
		   
        //公司操作	
        }else{
         if (selected.getParent() == null) {//根节点
         	setButton(this.m_boDelete, false);
			setButton(this.m_boUpdate, false);
			if ("0".equals(grpCtlLevel)) { //控制到0级，可以增加
				this.getNodePanel1().setNode(null, null);
				
				setButton(this.m_boInsert, true);
			}else{
				setButton(this.m_boInsert, false);
			}
			
		} else {
			/* 编辑按钮控制*/
			//如果创建公司与当前公司不同，不能编辑
			if (((InvclVO) selected.getUserObject()).getPk_corp().equals(
					getPk_corp())) {
				setButton(this.m_boUpdate, true);
			} else {
				setButton(this.m_boUpdate, false);
			}
			this.getNodePanel1().setNode(null,
					(InvclVO) selected.getUserObject());
					
			/*删除按钮控制*/
			if (selected.isLeaf()) {
						//如果创建公司与当前公司不同，不能删除
						if (((InvclVO) selected.getUserObject()).getPk_corp().equals(
								getPk_corp())) {
							setButton(this.m_boDelete, true);
						} else {
							setButton(this.m_boDelete, false);
						}
					} else {
						setButton(this.m_boDelete, false);
					}
		    }
			/*增加按钮控制*/
			try {
				if (LocalModel.isLastLevel(((InvclVO) selected.getUserObject())
						.getInvclasscode())) {
					setButton(this.m_boInsert, false);
				} else{ //当前选中节点的级次
				if ( "末级".equals(grpCtlLevel)) {
					setButton(this.m_boInsert, false);
				} else {
					if (lev < Integer.parseInt(grpCtlLevel)) {
						setButton(this.m_boInsert, false);
					} else {
						setButton(this.m_boInsert, true);
					}
				}
				}
				
			} catch (Exception ef) {
			}
        }

			
		int[] rule = null;
		try {
			rule = LocalModel.getCodeRule();
		} catch (Exception ef) {
		}
		if (rule == null) {
			setButton(m_boInsert, false);
			setButton(m_boUpdate, false);
			setButton(m_boDelete, false);
			showHintMessage(MultiLangTrans.getTransStr("MT8",new String[]{nc.ui.ml.NCLangRes.getInstance().getStrByID("10081202","UPP10081202-000013")/*@res "存货编码原则"*/}));
		}
	}

	/* (non-Javadoc)
	 * @see nc.ui.pub.ToftPanel#onClosing()
	 */
	public boolean onClosing() {
			if(m_iCurrentStatus==UPDATE_STATUS ||m_iCurrentStatus==INSERT_STATUS){
//				int i = showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000058")/*@res "正在编辑状态，未保存的数据将会丢失，是否退出？"*/);
				int i = MessageDialog.showYesNoCancelDlg(this, null, NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UCH001"),UIDialog.ID_CANCEL);
				switch (i) {
				case UIDialog.ID_YES:
				{					
					onOK();
					if (this.isOnClosing) {
						return true;
					}
					this.isOnClosing = true;
					return false;
				}
				case UIDialog.ID_NO:
				{
					return true;
				}
				case UIDialog.ID_CANCEL:
				{
					return false;
				}
					

				default:
					return true;
				}
			}
		return super.onClosing();
	}
}