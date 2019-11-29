package nc.ui.mo.mo0451;

/**
 * 此处插入类型说明。
 * 创建日期：(2003-4-3 11:08:07)
 * @author：岳云飞
 */

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.mm.pub.IClose;
import nc.ui.mm.pub.IUIState;
import nc.ui.mm.pub.MMToftPanel;
import nc.ui.pub.bill.*;
import nc.ui.pub.beans.*;
import nc.ui.pub.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import nc.vo.pub.query.*;
import nc.vo.pub.*;
import nc.vo.rino.pda.BasicdocVO;
import nc.ui.pub.report.*;
import nc.ui.pub.rino.DbUtil;
import nc.ui.pub.print.*;
import nc.ui.trade.business.HYPubBO_Client;

import nc.vo.mm.proxy.MMProxy;
import nc.vo.mm.pub.pub1030.PgaHeaderVO;
import nc.vo.mm.pub.pub1030.PgaVO;
import nc.vo.mm.pub.pub1030.PgaItemVO;

public class ClientUI extends nc.ui.mm.pub.MMToftPanel implements IClose//BillEditListener,ActionListener
{

	private PgaListPanel listPanel = null;
	private PgaEditPanel cardPanel = null;
	private UIRefPane rfpFactory = null;
	private UIPanel pnlList = null;

	private final CardLayout layout = new CardLayout();
	//state
	public final static int STAT_INIT = 0;
	public final static int STAT_ADD = 1;
	public final static int STAT_VIEW = 2;
	public final static int STAT_EDIT = 3;

	//public final static int STAT_OK=4;
	private int CurrentStat = 0;

	//查询模板
	private QueryCondition queryCondition = null;
	//保存查询条件
	private ConditionVO[] conditions = null;

	//btn
	private ButtonObject m_boAdd = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000011")/*@res""增加班组""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000007")/*@res""新增一条班组""*/, 2, "增加班组");
	private ButtonObject m_boEdit = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000012")/*@res""修改""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000008")/*@res""修改一条班组""*/, 2, "修改");

	private ButtonObject m_boDel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000013")/*@res""删除班组""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000009")/*@res""删除当前班组""*/, 2, "删除班组");
	private ButtonObject m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000014")/*@res""查询班组""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000010")/*@res""查询班组信息""*/, 2, "查询班组");

	private ButtonObject m_boAddLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UC001-0000012")/*@res""增行""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000011")/*@res""新增一作业员""*/, 2, "增行");
	private ButtonObject m_boDelLine = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UC001-0000013")/*@res""删行""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000012")/*@res""删除当前作业员班组""*/, 2, "删行");

	private ButtonObject m_boMove = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000015")/*@res""作业员调动""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000013")/*@res""调动当前作业员到其他班组""*/, 2, "作业员调动");

	private ButtonObject m_boLoad = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000016")/*@res""载入""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000016")/*@res""载入""*/, 2, "载入");

	private ButtonObject m_boLoadPga = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000017")/*@res""载入班组""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000014")/*@res""从部门载入班组""*/, 2, "载入班组");
	private ButtonObject m_boLoadMen = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000018")/*@res""载入作业员""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000015")/*@res""从部门载入作业员""*/, 2, "载入作业员");

	private ButtonObject m_boSave = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000019")/*@res""确定""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000016")/*@res""确认保存""*/, 2, "确定");
	private ButtonObject m_boCancel = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UC001-0000008")/*@res""取消""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UC001-0000008")/*@res""取消""*/, 2, "取消");

	private ButtonObject m_boRefresh = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UC001-0000009")/*@res""刷新""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000017")/*@res""刷新界面显示""*/, 2, "刷新");
	private ButtonObject m_boPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UC001-0000007")/*@res""打印""*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000018")/*@res""打印班组""*/, 2, "打印");
	//子菜单
	private ButtonObject[] m_aryBtnsLoad = {
			m_boLoadPga, m_boLoadMen
	};
	//菜单
	private ButtonObject[] m_aryButtons = {
			m_boAdd, m_boEdit, m_boDel, m_boQuery, m_boAddLine, m_boDelLine, m_boMove, m_boLoad,
			//			m_boLoadMen,
			m_boSave,
			m_boCancel,
			m_boRefresh,
			m_boPrint
	};

	public int getCurrentState() {
		if (CurrentStat == STAT_ADD) return IUIState.ADD_STATE;

		else if (CurrentStat == STAT_EDIT) return IUIState.MODIFY_STATE;
		else return IUIState.LIST_STATE;

	}

	public boolean isDataChanged() {
		return false;
	}

	public int save() {
		boolean flag = onSave();
		if (flag) return IUIState.SAVE_SUCCESS_QUIT;
		else return IUIState.SAVE_FAILURE_NOT_QUIT;
	}

	public void freeLock() throws Exception {
		getCardPanel().free();
	}

	/**
	 * ClientUI 构造子注解。
	 */
	public ClientUI() {
		super();
		initialize();
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-3 13:07:05)
	 * @return nc.ui.pub.beans.UILabel
	 */
	private UILabel createLbFactory() {
		return new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UC000-0001685")/*@res""工厂""*/);
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-3 13:05:14)
	 * @return nc.ui.pub.beans.UIPanel
	 */
	private nc.ui.pub.beans.UIPanel createNorthPnl() {
		UIPanel pnlListNorth = new UIPanel();
		pnlListNorth.setLayout(getNorthLayout());
		pnlListNorth.setPreferredSize(new java.awt.Dimension(774, 28));
		pnlListNorth.add(createLbFactory());
		pnlListNorth.add(getRfpFactory());
		return pnlListNorth;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-3 16:11:35)
	 */
	private PgaEditPanel getCardPanel() {
		if (this.cardPanel == null) {
			//cardPanel = new BillCardPanel();
			cardPanel = new PgaEditPanel(this, getCorpPrimaryKey(), getFactoryCode());
			cardPanel.setName("edit");
			cardPanel.loadTemplet("50080451", null, getUser().getPrimaryKey(), getUnitCode());
		}
		return cardPanel;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-3 11:18:23)
	 * @return nc.ui.pub.bill.BillListPanel
	 */
	private PgaListPanel getListPanel() {
		if (listPanel == null) {
			//listPanel = new BillListPanel();
			listPanel = new PgaListPanel(this);
			listPanel.setName("listpanel");
			listPanel.loadTemplet("50080451", null, getUser().getPrimaryKey(), getUnitCode());
			getListPanel().getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		}

		return listPanel;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-3 14:38:49)
	 * @return nc.ui.pub.beans.UILabelLayout
	 */
	private UILabelLayout getNorthLayout() {
		UILabelLayout factoryLayout = null;
		try {
			factoryLayout = new UILabelLayout();
			factoryLayout.setBottom(1);
			factoryLayout.setColumns(3);
			factoryLayout.setRight(5);
			factoryLayout.setRows(1);
			factoryLayout.setTop(1);
			factoryLayout.setHgap(10);
			factoryLayout.setLeft(10);
		} catch (java.lang.Throwable ex) {
			ex.printStackTrace();
		}
		return factoryLayout;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-3 16:14:56)
	 * @return nc.ui.pub.beans.UIPanel
	 */
	private UIPanel getPnlList() {
		if (pnlList == null) {
			pnlList = new UIPanel();
			pnlList.setName("list");
			pnlList.setSize(774, 419);
			pnlList.setLayout(new BorderLayout());
			pnlList.add(createNorthPnl(), "North");
			pnlList.add(getListPanel(), "Center");
		}
		return pnlList;
	}

	/**
	 * 查询界面
	 * 创建日期：(2001-8-20 10:34:48)
	 * @return nc.ui.mo.mo1020.QueryCondition
	 */
	private QueryCondition getQueryCondition() {
		if (queryCondition == null) {
			queryCondition = new QueryCondition(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000019")/*@res""班组查询""*/, getCorpPrimaryKey(), getFactoryCode());
			queryCondition.setDefaultCloseOperation(queryCondition.HIDE_ON_CLOSE);
			queryCondition.hideDefine();
			queryCondition.hideUnitButton();
			getQueryCondition().getNormalPanel().getRfWk().getRefModel().addWherePart(" and pd_wk.sffc='N' and pd_wk.pk_corp='" + getUnitCode() + "' and pd_wk.gcbm='" + getFactoryCode() + "'");
			getQueryCondition().getNormalPanel().getRfDept().getRefModel().addWherePart("and bd_deptdoc.pk_calbody = '" + getFactoryCode() + "'");
		}
		return queryCondition;
	}

	/**
	 * 得到工厂参照
	 * 创建日期：(2003-4-3 13:08:16)
	 * @return nc.ui.pub.beans.UIRefPane
	 */
	public UIRefPane getRfpFactory() {
		if (rfpFactory == null) {
			rfpFactory = new UIRefPane();
			//TODO MULLAN
			rfpFactory.setRefNodeName("库存组织"/*@res""库存组织""*/);
			rfpFactory.setEnabled(false);
		}
		return rfpFactory;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * @version (00-6-6 13:33:25)
	 *
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPT50080451-000008")/*@res""班组定义维护""*/;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-3 11:15:05)
	 */
	private void initialize() {

		try {
			setName("ClientUI");
			setSize(774, 419);
			setLayout(layout);

			add(getPnlList(), getPnlList().getName());

			add(getCardPanel(), getCardPanel().getName());

			setState(STAT_INIT);

			getRfpFactory().setPK(getFactoryCode());

			getListPanel().initListPanel();
			getCardPanel().initEditPanel();

			addIClose(this);
		} catch (Exception ivjExc) {
			//handleException(ivjExc);
			reportException(ivjExc);
		}
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-3 16:19:30)
	 */
	private void onAdd() {
		getCardPanel().getHeadItem("bzbm").setEnabled(true);
		PgaVO vo = new PgaVO();
		PgaHeaderVO head = new PgaHeaderVO();
		vo.setParentVO(head);
		getCardPanel().setBillValueVO(vo);
		showHintMessage("");
		setState(STAT_ADD);
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-9 10:45:17)
	 */
	private void onAddLine() {
		getCardPanel().addLine();
	}

	/**
	 * 子类实现该方法，响应按钮事件。
	 * @version (00-6-1 10:32:59)
	 *
	 * @param bo ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {

		if (bo == m_boAdd) {
			onAdd();
			return;
		}
		if (bo == m_boDel) {
			onDel();
			return;
		}
		if (bo == m_boEdit) {
			onEdit();
			return;
		}
		if (bo == m_boSave) {
			onSave();

			return;
		}
		if (bo == m_boCancel) {

			onCancel();
			return;
		}
		if (bo == m_boQuery) {
			onQuery();

			return;
		}
		if (bo == m_boAddLine) {
			onAddLine();

			return;
		}
		if (bo == m_boDelLine) {
			onDelLine();

			return;
		}
		if (bo == m_boLoadMen) {
			onLoadMen();

			return;
		}

		if (bo == m_boMove) {
			onReMove();

			return;
		}
		if (bo == m_boRefresh) {
			onRefresh();

			return;
		}
		if (bo == m_boPrint) {
			onPrint();

			return;
		}
		if (bo == m_boLoadPga) {
			onLoadPga();

			return;
		}
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-10 16:20:23)
	 */
	public void onCancel() {
		showHintMessage("");
		//解锁
		try {
			getCardPanel().free();
		} catch (Exception ex) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000020")/*@res""对班组解锁时异常：""*/+ ex.getLocalizedMessage());
			ex.printStackTrace();
			return;
		}

		setState(STAT_VIEW);
	}

	/**
	 * del班组。1.必须选择一条班组 2.该班组下没有作业员
	 * 创建日期：(2003-4-12 15:59:03)
	 */
	public void onDel() {
		showHintMessage("");
		int[] rows = getListPanel().getHeadTable().getSelectedRows();
		if (rows.length <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000021")/*@res""必须选择一条班组记录!""*/);
			return;
		}
		Vector v = new Vector();
		StringBuffer sbMsg = new StringBuffer();
		for (int i = 0; i < rows.length; i++) {
			PgaVO pvo = (PgaVO) getListPanel().getRowVO(rows[i]);
			PgaItemVO[] items = (PgaItemVO[]) pvo.getChildrenVO();
			PgaHeaderVO head = (PgaHeaderVO) pvo.getParentVO();
			//if (items.length > 0) {
			//sbMsg.append(head.getBzbm() + " : " + head.getBzmc() + "\n");
			//} else {
			v.add(head);
			//}

		}
		//if (sbMsg.length() > 0) {
		//showErrorMessage(" 班组下还有作业员不能删除 !\n" + sbMsg.toString());
		//return;
		//}
		//TODO MULLAN
		//if (showYesNoMessage("确认要删除 " + v.size() + " 班组 ?") != UIDialog.ID_YES) {
		int length = v.size();
		if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000022", null, new String[] {
			length + ""
		})/*@res""确认要删除{0}班组 ?""*/) != UIDialog.ID_YES) { return; }
		PgaHeaderVO[] heads = null;
		if (v.size() > 0) {
			heads = new PgaHeaderVO[v.size()];
			v.copyInto(heads);
		}
		try {
			//从数据库中删除
			MMProxy.getRemotePga().deleteHeaders(heads, getUser().getPrimaryKey());
			//从缓存中删除
			for (int i = 0; i < heads.length; i++) {

				getListPanel().hcache.remove(heads[i].getPrimaryKey());
			}
			//从界面中删除
			getListPanel().getHeadBillModel().delLine(rows);
			getListPanel().setBodyValueVO(null);
		} catch (Exception ex) {
			//加锁不成功...
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000023")/*@res""删除班组 异常:""*/+ ex.getMessage());
		}

		// add by zip:2014/3/3 pda
		try {
			for (PgaHeaderVO hVO : heads) {
				String sql = "select * from pda_basicdoc where bdid='" + hVO.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'";
				BasicdocVO checkVO = (BasicdocVO) DbUtil.getDMO().getBean(sql, BasicdocVO.class);
				if (checkVO != null) {
					String deleteSQL = "delete from pda_basicdoc where bdid='" + hVO.getPrimaryKey() + "' and sysflag='N'";
					DbUtil.getDMO().executeUpdate(deleteSQL);
					checkVO.setProctype("delete");
					DbUtil.getDMO().update(checkVO);
				}
			}
		} catch (Exception ex) {
			System.err.println("pda error ...");
		}
		// end
		updateUI();
	}

	/**删除作业员
	 *
	 * 创建日期：(2003-4-10 11:04:29)
	 */
	private void onDelLine() {

		int[] rows = getCardPanel().getBillTable().getSelectedRows();
		if (rows == null || rows.length < 1) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000024")/*@res""必须选择该班组下的一个或多个作业员!""*/);
			return;
		}
		for (int i = 0; i < rows.length; i++) {
			if (getCardPanel().getBodyPanel().getTableModel().getValueAt(rows[i], "sffzr").equals(new Boolean("true"))) {
				getCardPanel().clearHeadItemFzr();
				break;
			}
		}
		//String fzrid=getCardPanel().getRfBodyFzr().getRefPK();
		//if(fzrid!=null && fzrid.trim().length()>0)
		//getCardPanel().v.removeElement(fzrid);
		if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000025")/*@res""是否删除选定的行？""*/) == UIDialog.ID_YES) {
			getCardPanel().delLine();
		}
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-9 16:55:07)
	 */
	private void onEdit() {
		showHintMessage("");
		int[] rows = getListPanel().getHeadTable().getSelectedRows();
		if (rows.length > 1) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000021")/*@res""必须选择一条班组记录!""*/);
			return;
		}

		PgaVO vo = getListPanel().getRowVO(rows[0]);
		PgaHeaderVO head = (PgaHeaderVO) vo.getParentVO();
		getCardPanel().setBillValueVO(vo);

		try {
			//加锁
			boolean lock = getCardPanel().lock();
			if (!lock) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000026")/*@res""该订单正在被其他用户修改，请稍候再试！""*/);
				return;
			}
		} catch (Exception ex) {
			try {
				getCardPanel().free();//加锁不成功
			} catch (Exception exc) {
			}
			ex.printStackTrace();
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000027")/*@res""程序异常：""*/+ ex.getMessage());
			return;
		}

		getCardPanel().getHeadItem("bzbm").setEnabled(false);

		//将list的vo写入card
		getCardPanel().getRfbm().setPK(head.getBmid());
		getCardPanel().getRfGzzxid().setPK(head.getGzzxid());

		//子表参照通过Idcolname设定

		getCardPanel().updateValue();
		//修改时切换参照的过滤条件
		getCardPanel().setRefWherePart();

		//将原有的v 清空
		//getCardPanel().v=null;
		setState(STAT_EDIT);

	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(01-3-17 3:00:08)
	 */
	public void onHelp() {
	}

	/**
	 * 载入作业员。
	 * 创建日期：(2003-4-11 20:27:55)
	 */
	private void onLoadMen() {
		showHintMessage("");
		String bmname = getCardPanel().getRfbm().getRefName();
		if (bmname == null) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000028")/*@res""班组没有指定所属车间，不能进行作业员载入 !""*/);
			getCardPanel().getRfbm().grabFocus();
			return;
		}
		//TODO MULLAN
		//else if (showYesNoMessage("真的载入车间为:" + bmname + " 的作业员么?") == UIDialog.ID_YES) {
		else if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000029") + bmname/*@res""真的载入车间为:""*/) == UIDialog.ID_YES) {
			LoadMenDlg dlg = new LoadMenDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000031")/*@res""作业员载入""*/, getCardPanel().getRfbm().getRefPK());
			//dlg.getListPanel().getBodyBillModel().setBodyDataVO(items);

			if (dlg.showModal() == UIDialog.ID_OK) {
				//得到选中的人员VO[]
				PgaItemVO[] items = dlg.getSelectedItems();
				//删行
				int rowcount = getCardPanel().getBillTable().getRowCount();
				int[] rows = new int[rowcount];
				for (int i = 0; i < rowcount; i++)
					rows[i] = i;
				getCardPanel().getBillModel().delLine(rows);
				for (int i = 0; i < items.length; i++) {
					getCardPanel().getBillModel().addLine();
					getCardPanel().getBillModel().setBodyRowVO(items[i], i);
				}
				//clear the HeadItem "Fzrid,fzrcode,fzrname"
				getCardPanel().clearHeadItemFzr();
				updateUI();

			} else {
				return;
			}

		}

	}

	/**
	 * 载入班组。
	 * 创建日期：(2003-4-16 15:11:21)
	 */
	public void onLoadPga() {

		LoadPgaDlg dlg = new LoadPgaDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000032")/*@res""班组载入""*/);
		dlg.getRefWk().getRefModel().addWherePart("and bd_deptdoc.pk_calbody = '" + getFactoryCode() + "'");
		if (dlg.showModal() == UIDialog.ID_OK) {
			UIRefPane ref = dlg.getRefWk();
			try {
				PgaVO[] vos = MMProxy.getRemotePga().loadPga(ref.getRefPK(), getCorpPrimaryKey(), getFactoryCode());
				//if(vos==null || vos.length<=0)
				//showWarningMessage("班组载入失败!")	;
				//else
				if (vos != null && vos.length > 0) {
					//设置数据到界面同时设置缓存
					getListPanel().setData(vos);
					updateUI();
					setState(STAT_VIEW);

				}
			} //end try
			/*catch(nc.vo.pub.BusinessException ex)
			{
				showWarningMessage(ex.getHint());
				}
				*/
			catch (Exception ex) {
				showWarningMessage(ex.getMessage());
				ex.printStackTrace();
			}
			return;
		}
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(03-4-11 13:00:46)
	 */
	public void onPrint() {
		//数据源
		PrintDataSource dataSource = null;
		PgaVO vo = null;
		if (CurrentStat == STAT_EDIT) {
			vo = getCardPanel().getCurrentVO();
		} else if (CurrentStat == STAT_VIEW) {
			int[] rows = getListPanel().getHeadTable().getSelectedRows();
			if (rows.length > 1) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000033")/*@res""请选中一条班组记录!""*/);
				return;
			}
			vo = getListPanel().getRowVO(rows[0]);
		}
		if (vo == null || vo.getParentVO() == null) return;
		dataSource = new PrintDataSource("50080451", getCardPanel().getBillData(), vo);

		//打印
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null, null);
		//向打印置入数据源，进行打印
		print.setTemplateID(getUnitCode(), "50080451", getUser().getPrimaryKey(), null);
		print.setDataSource(dataSource);
		if (print.selectTemplate() > 0) print.preview();

	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-4 11:44:33)
	 */
	private void onQuery() {
		showHintMessage("");
		//显示查询界面
		getQueryCondition().showModal();

		if (getQueryCondition().isCloseOK()) {
			try {
				conditions = getQueryCondition().getConditionVO();
				PgaHeaderVO[] heads = MMProxy.getRemotePga().queryPgaByWhere(conditions);

				getListPanel().setData(heads);
				setState(STAT_VIEW);

				updateUI();

				//layout.show(this,getPnlList().getName());
			} catch (Exception ex) {
				ex.printStackTrace();
				//tp.showErrorMessage(ex.getLocalizedMessage());
			}
		}
	}

	/**
	 * 按刷新按钮后的处理方法。
	 * 创建日期：(01-3-17 3:00:46)
	 */
	public boolean onRefresh() {
		showHintMessage("");
		if (conditions == null || conditions.length == 0) { return false; }
		try {
			PgaHeaderVO[] heads = MMProxy.getRemotePga().queryPgaByWhere(conditions);
			getListPanel().setData(heads);
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000034")/*@res""提示：数据刷新完毕！""*/);
			setState(STAT_VIEW);
		} catch (Exception ex) {
			ex.printStackTrace();
			showErrorMessage(ex.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-12 18:40:07)
	 */
	private void onReMove() {
		showHintMessage("");
		int[] rows = getCardPanel().getBillTable().getSelectedRows();
		//共有多少行数据
		int row = getCardPanel().getBillTable().getRowCount();
		if (rows.length == row) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000035")/*@res""不能将所有作业员全部调出""*/);
			return;
		}
		if (rows.length != 1) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000036")/*@res""必须选择该班组下的一个作业员!""*/);
			return;
		}
		if (new nc.vo.pub.lang.UFBoolean(getCardPanel().getBodyPanel().getTableModel().getValueAt(rows[0], "sffzr").toString()).booleanValue()) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000037")/*@res""该作业员是负责人不能调出!""*/);
			return;
		}
		//Object pk_pga_bid=getCardPanel().getBodyPanel().getTableModel().getValueAt(rows[0],"pk_pga_bid");
		PgaItemVO item = (PgaItemVO) getCardPanel().getBillModel().getBodyValueRowVO(rows[0], PgaItemVO.class.getName());
		PgaHeaderVO head = (PgaHeaderVO) getCardPanel().getBillData().getHeaderValueVO(PgaHeaderVO.class.getName());
		PgaVO pvo = new PgaVO();
		pvo.setParentVO(head);
		pvo.setChildrenVO(new PgaItemVO[] {
			item
		});

		//f(pk_pga_bid==null || pk_pga_bid.toString().trim().length()<=0 )
		if (item.getPk_pga_bid() == null || item.getPk_pga_bid().trim().length() <= 0) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000038")/*@res""该作业员是新增的，不能对他调动!""*/);
			return;

		}

		MoveDlg mDlg = new MoveDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000006")/*@res""作业员调度""*/, pvo);

		if (mDlg.showModal() == UIDialog.ID_OK) {
			//System.out.println("aaa="+mDlg.getrefToBz().getRefPK()+"\n"+mDlg.getrefToBz().getRefCode()+"\n"+mDlg.getrefToBz().getRefName()+"\n"+pk_pga_bid.toString());
			Object pk = mDlg.getrefToBz().getRefPK();
			//没有要调往的车间，或是和当前车间一致
			if (pk == null || pk.toString().trim().length() <= 0 || pk.equals(getCardPanel().getRfbm().getRefPK())) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000039")/*@res""调入班组不能为空!""*/);
				return;

			}
			if (pk.toString().trim().equals(getCardPanel().getHeadItem("pk_pgaid").getValue().trim())) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000040")/*@res""调入班组和原班组相同不能进行调动""*/);
				return;
			}
			try {
				MMProxy.getRemotePga().removePsn(item.getPk_pga_bid(), mDlg.getrefToBz().getRefPK());
			} catch (Exception ex) {
				ex.printStackTrace();
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000041")/*@res""作业员调动失败""*/);
				return;
			}
			//从界面中delline
			getCardPanel().delLine();
			//从缓存中清除该两个界面
			//重新从db中查找。。。。
			/*
				hcache.(head.getPrimaryKey());
				hcache.remove(mDlg.getrefToBz().getRefPK());
			*/
			//当前
			getListPanel().hcache.put(head.getPrimaryKey(), getCardPanel().getCurrentVO());
			//调入的
			PgaVO toBzvo = (PgaVO) getListPanel().hcache.get(mDlg.getrefToBz().getRefPK());
			toBzvo.setChildrenVO(null);
			getListPanel().hcache.put(mDlg.getrefToBz().getRefPK(), toBzvo);

			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000042")/*@res""作业员调动成功""*/);
			setState(STAT_VIEW);
		}
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-4 9:03:22)
	 */
	private boolean onSave() {
		showHintMessage("");
		PgaVO pvo = getCardPanel().getCurrentVO();
		if (pvo == null) return false;

		switch (CurrentStat) {
		case STAT_ADD: //新增
		{

			//条件判断
			if (!getCardPanel().checkData(pvo, CurrentStat)) return false;
			try {
				//新增
				String[] pks = MMProxy.getRemotePga().insert_Pga(pvo);
				if (pks != null || pks.length != 0) {
					((PgaHeaderVO) pvo.getParentVO()).setPk_pgaid(pks[0]);
					for (int i = 0; i < pvo.getChildrenVO().length; i++) {
						pvo.getChildrenVO()[i].setPrimaryKey(pks[1 + i]);
						((nc.vo.mm.pub.pub1030.PgaItemVO) pvo.getChildrenVO()[i]).setPk_pgaid(pks[0]);
					}
					//pvo.setChildrenVO(null);
					getListPanel().hcache.put(pks[0], pvo);
				}

				// add by zip: 2014/3/3 pda
				try {
					PgaHeaderVO hVO = (PgaHeaderVO) pvo.getParentVO();
					if (hVO.getMemo() != null && hVO.getMemo().startsWith("PDA")) {
						BasicdocVO vo = new BasicdocVO();
						vo.setBdname(hVO.getBzbm());
						vo.setBdid(hVO.getPrimaryKey());
						vo.setBdtype("BZ");
						vo.setPk_corp(hVO.getPk_corp());
						vo.setProctype("add");
						vo.setSysflag("Y");
						DbUtil.getDMO().insert(vo);
					}
				} catch (Exception ex) {
					System.err.println("pda error ...");
				}
				// end

				//更新界面
				//getCardPanel().setBillValueVO(bom);
				int row = getListPanel().getHeadTable().getRowCount();
				getListPanel().getHeadBillModel().addLine();
				getListPanel().getHeadBillModel().setBodyRowVO(pvo.getParentVO(), row);
				getListPanel().getHeadTable().clearSelection();
				getListPanel().getHeadTable().getSelectionModel().clearSelection(); //setSelectionInterval(row,row);
				updateUI();
			} catch (Exception e) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000043")/*@res""数据插入异常""*/);
				e.printStackTrace();
				return false;
			}
			setState(STAT_VIEW);
			getListPanel().updateUI();
			break;
		}

		case STAT_EDIT: {

			//修改保存条件判断
			PgaVO vo = new PgaVO();
			vo.setParentVO(pvo.getParentVO());
			//得到子表所有更改的vo
			PgaItemVO[] changeItems = (PgaItemVO[]) getCardPanel().getBillModel().getBodyValueChangeVOs(PgaItemVO.class.getName());

			vo.setChildrenVO(changeItems);
			//条件判断

			if (!getCardPanel().checkData(pvo, CurrentStat)) { return false; }
			try {
				MMProxy.getRemotePga().update_Pga(vo);
			} catch (Exception ex) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000044")/*@res""数据更新异常""*/);
				ex.printStackTrace();
				return false;
			}
			//解锁
			try {
				getCardPanel().free();
			} catch (Exception ex) {
				showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("50080451", "UPP-000020")/*@res""对班组解锁时异常：""*/+ ex.getLocalizedMessage());
				ex.printStackTrace();
				return false;
			}

			//更新hash
			Object opk = ((PgaHeaderVO) pvo.getParentVO()).getPk_pgaid();

			//如果在缓存中
			if (opk != null && getListPanel().hcache.containsKey(opk)) {
				//hcache.remove(opk);
				//pvo.setChildrenVO(null);
				getListPanel().hcache.put(opk, pvo);

			}

			// add by zip: 2014/3/3 pda
			try {
				PgaHeaderVO hVO = (PgaHeaderVO) pvo.getParentVO();
				String sql = "select * from pda_basicdoc where bdid='" + hVO.getPrimaryKey() + "' and nvl(dr,0)=0 and sysflag='Y'";
				BasicdocVO checkVO = (BasicdocVO) DbUtil.getDMO().getBean(sql, BasicdocVO.class);
				boolean pdaFlag = hVO.getMemo() != null && hVO.getMemo().startsWith("PDA");
				if (checkVO == null && pdaFlag) {
					BasicdocVO bdVO = new BasicdocVO();
					bdVO.setBdname(hVO.getBzbm());
					bdVO.setBdid(hVO.getPrimaryKey());
					bdVO.setBdtype("BZ");
					bdVO.setPk_corp(hVO.getPk_corp());
					bdVO.setProctype("add");
					bdVO.setSysflag("Y");
					DbUtil.getDMO().insert(bdVO);
				} else if (checkVO != null && !checkVO.getBdname().equals(hVO.getBzbm()) && pdaFlag) {
					String deleteSQL = "delete from pda_basicdoc where bdid='" + hVO.getPrimaryKey() + "' and sysflag='N'";
					DbUtil.getDMO().executeUpdate(deleteSQL);
					checkVO.setBdname(hVO.getBzbm());
					DbUtil.getDMO().update(checkVO);
				} else if (checkVO != null && !pdaFlag) {
					String deleteSQL = "delete from pda_basicdoc where bdid='" + hVO.getPrimaryKey() + "' and sysflag='N'";
					DbUtil.getDMO().executeUpdate(deleteSQL);
					checkVO.setProctype("delete");
					DbUtil.getDMO().update(checkVO);
				}
			} catch (Exception ex) {
				System.err.println("pda error ...");
			}
			// end

			int row = getListPanel().getHeadTable().getSelectedRow();
			getListPanel().getHeadBillModel().setBodyRowVO(pvo.getParentVO(), row);
			getListPanel().getBodyBillModel().setBodyDataVO(pvo.getChildrenVO());

			setState(STAT_VIEW);
			getListPanel().updateUI();

			break;
		}

		}
		return true;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2003-4-3 15:02:22)
	 * @param stat int
	 */
	public void setState(int stat) {
		CurrentStat = stat;

		switch (stat) {
		case STAT_INIT: {

			m_boAdd.setEnabled(true);
			m_boEdit.setEnabled(false);
			m_boDel.setEnabled(false);
			m_boQuery.setEnabled(true);
			m_boAddLine.setEnabled(false);
			m_boDelLine.setEnabled(false);
			m_boMove.setEnabled(false);
			m_boLoadPga.setEnabled(true);
			m_boLoadMen.setEnabled(false);
			m_boSave.setEnabled(false);
			m_boCancel.setEnabled(false);
			m_boRefresh.setEnabled(false);
			m_boPrint.setEnabled(false);
			m_boLoad.addChileButtons(m_aryBtnsLoad);

			setButtons(this.m_aryButtons);
			layout.show(this, getPnlList().getName());
			break;
		}
		case STAT_ADD: {

			m_boAdd.setEnabled(false);
			m_boEdit.setEnabled(false);
			m_boDel.setEnabled(false);
			m_boQuery.setEnabled(false);
			m_boAddLine.setEnabled(true);
			m_boDelLine.setEnabled(true);
			m_boMove.setEnabled(false);
			m_boLoadPga.setEnabled(false);
			m_boLoadMen.setEnabled(true);
			m_boSave.setEnabled(true);
			m_boCancel.setEnabled(true);
			m_boRefresh.setEnabled(false);
			m_boPrint.setEnabled(true);
			updateButtons();

			layout.show(this, getCardPanel().getName());
			getCardPanel().grabFocus();

			break;
		}
		case STAT_VIEW: {

			m_boAdd.setEnabled(true);
			m_boEdit.setEnabled(true);
			m_boDel.setEnabled(true);
			m_boQuery.setEnabled(true);
			m_boAddLine.setEnabled(false);
			m_boDelLine.setEnabled(false);
			m_boMove.setEnabled(false);

			m_boLoadPga.setEnabled(true);
			m_boLoadMen.setEnabled(false);

			m_boSave.setEnabled(false);
			m_boCancel.setEnabled(false);
			m_boRefresh.setEnabled(true);
			m_boPrint.setEnabled(true);
			updateButtons();
			layout.show(this, getPnlList().getName());
			break;
		}

		case STAT_EDIT: {

			m_boAdd.setEnabled(false);
			m_boEdit.setEnabled(false);
			m_boDel.setEnabled(false);
			m_boQuery.setEnabled(false);
			m_boAddLine.setEnabled(true);
			m_boDelLine.setEnabled(true);
			m_boMove.setEnabled(true);

			m_boLoadPga.setEnabled(false);
			m_boLoadMen.setEnabled(true);

			m_boSave.setEnabled(true);
			m_boCancel.setEnabled(true);
			m_boRefresh.setEnabled(false);
			m_boPrint.setEnabled(true);
			updateButtons();

			layout.show(this, getCardPanel().getName());

			break;
		}
		}

	}

}