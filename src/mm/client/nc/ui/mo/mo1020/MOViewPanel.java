package nc.ui.mo.mo1020;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.common.CommonUtil;
import nc.ui.common.PublicUtil;
import nc.ui.ml.NCLangRes;
import nc.ui.mm.pub.MMLog;
import nc.ui.mm.pub.MMToftPanel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UILabelLayout;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.lock.LockBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.sm.login.ShowDialog;
import nc.vo.bd.CorpVO;
import nc.vo.bd.fd.DdVO;
import nc.vo.mm.proxy.MMProxy;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mm.pub.pub1030.MoVO;
import nc.vo.mo.mo1020.ReviseVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

public class MOViewPanel extends MOAbstractPanel {
	private UIRefPane rfpFactory;
	public MOBillListPanel listPanel;
	private MOSourceViewer srcViewer;
	private Object lock;
	protected final MMToftPanel tp;

	public MOViewPanel(MMToftPanel mmtoft) {
		super(mmtoft);
		this.rfpFactory = null;
		this.listPanel = null;
		this.srcViewer = null;
		this.lock = new Object();
		this.tp = mmtoft;
		initialize();
	}

	private void freeSelectedVO() {
		try {
			int row = getListPanel().getHeadTable().getSelectedRow();
			String tpk = (String) getListPanel().getHeadBillModel().getValueAt(
					row, "pk_moid");
			if (!isNull(tpk))
				LockBO_Client.freePK(tpk, getUserPK(), "mm_mo");
		} catch (Exception ex) {
			reportError(getstrbyid("UPP50081020-000135")
					+ ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	private UILabel getLbFactory() {
		UILabel lbFactory = new UILabel();
		lbFactory.setText(getstrbyid("UC000-0001685"));
		return lbFactory;
	}

	public MOBillListPanel getListPanel() {
		if (this.listPanel == null) {
			this.listPanel = new MOBillListPanel(this.m_toolkit, null,
					getUserPK(), getUnitPK());
			this.listPanel.setName("ListPanel");
			this.listPanel.getHeadTable().setSelectionMode(2);

			this.listPanel.getParentListPanel().setTotalRowShow(true);
		}
	
		return this.listPanel;
	}

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
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return factoryLayout;
	}

	private UIPanel getPnlNorth() {
		UIPanel pnlNorth = new UIPanel();
		pnlNorth.setLayout(getNorthLayout());
		pnlNorth.setPreferredSize(new Dimension(774, 25));
		pnlNorth.add(getLbFactory());
		pnlNorth.add(getRfpFactory());
		return pnlNorth;
	}

	private String getstrbyid(String number) {
		return NCLangRes.getInstance().getStrByID("50081020", number);
	}

	public MoHeaderVO[] getPrintData() {
		int[] rows = getSelectedRows();
		if ((rows == null) || (rows.length == 0)) {
			reportError(getstrbyid("UPP50081020-000136"));
			return null;
		}
		MoHeaderVO[] headers = new MoHeaderVO[rows.length];
		for (int i = 0; i < rows.length; i++) {
			headers[i] = ((MoHeaderVO) getListPanel().getHeadBillModel()
					.getBodyValueRowVO(rows[i], MoHeaderVO.class.getName()));
			headers[i].setGcbm(getRfpFactory().getRefName());
		}

		return headers;
	}

	private UIRefPane getRfpFactory() {
		if (this.rfpFactory == null) {
			this.rfpFactory = new UIRefPane();
			this.rfpFactory.setRefNodeName("库存组织");
			this.rfpFactory.setEnabled(false);
		}
		return this.rfpFactory;
	}

	protected MoVO getRowVO(int row) {
		MoHeaderVO header = (MoHeaderVO) getListPanel().getHeadBillModel()
				.getBodyValueRowVO(row, MoHeaderVO.class.getName());
		header.initVO();
		MoVO vo = new MoVO();
		vo.setParentVO(header);
		return vo;
	}

	public String getSelectedPK() {
		int[] rows = getSelectedRows();
		if ((rows.length == 0) || (rows.length > 1)) {
			reportError(getstrbyid("UPP50081020-000137"));
			return null;
		}

		return (String) getListPanel().getHeadBillModel().getValueAt(rows[0],
				"pk_moid");
	}

	public int[] getSelectedRows() {
		return getListPanel().getHeadTable().getSelectedRows();
	}

	public MoVO getSelectedVO() {
		int[] rows = getSelectedRows();
		if ((rows == null) || (rows.length == 0) || (rows.length > 1)) {
			reportError(getstrbyid("UPP50081020-000137"));
			return null;
		}

		return getRowVO(rows[0]);
	}

	public MoVO[] getSelectedVOs() {
		int[] rows = getSelectedRows();
		if ((rows == null) || (rows.length == 0)) {
			reportError(getstrbyid("UPP50081020-000136"));
			return null;
		}
		MoVO[] mos = new MoVO[rows.length];
		for (int i = 0; i < rows.length; i++) {
			mos[i] = getRowVO(rows[i]);
			if (mos[i] == null) {
				return null;
			}
		}
		return mos;
	}

	private void initialize() {
		setSize(774, 419);
		setLayout(new BorderLayout());
		add(getPnlNorth(), "North");
		add(getListPanel(), "Center");

		initPanel();
		//getListPanel().getHeadBillModel().execLoadFormulaByKey(key);
	}

	private void initPanel() {
		getRfpFactory().setPK(getCalbodyPK());
		getListPanel().getHeadItem("jhwgsl").setDecimalDigits(
				this.m_toft.getScaleNum());
		getListPanel().getHeadItem("sjwgsl").setDecimalDigits(
				this.m_toft.getScaleNum());
		getListPanel().getHeadItem("rksl").setDecimalDigits(
				this.m_toft.getScaleNum());
		getListPanel().getHeadItem("fjlhsl").setDecimalDigits(
				this.m_toft.getScaleConvertionRate());
		getListPanel().getHeadItem("fjhsl").setDecimalDigits(
				this.m_toft.getScaleAssistantNum());
		getListPanel().getHeadItem("fwcsl").setDecimalDigits(
				this.m_toft.getScaleAssistantNum());
		getListPanel().getHeadItem("frksl").setDecimalDigits(
				this.m_toft.getScaleAssistantNum());
	
	}

	public void onDelete() {
		MoVO[] mvos = getSelectedVOs();
		if ((mvos == null) || (mvos.length == 0))
			return;
		StringBuffer errmsg = new StringBuffer("");
		int err_len = errmsg.length();
		for (int i = 0; i < mvos.length; i++) {
			if ((!isNull(mvos[i].getHeadVO().getZt()))
					&& (!mvos[i].getHeadVO().getZt().equals("B"))
					&& (!mvos[i].getHeadVO().getZt().equals("C")))
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138")).append("{");
			errmsg.append(mvos[i].getHeadVO().getScddh()).append(", ");
		}

		if (errmsg.length() > 0) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append("}").append(getstrbyid("UPP50081020-000139"));
			errmsg.append(System.getProperty("line.separator"));
		}
		if (errmsg.length() > 0) {
			errmsg.append(getstrbyid("UPP50081020-000140"));
			reportError(errmsg.toString());
			return;
		}

		checkGXDandICExist(mvos);
		int id = this.m_toft.showYesNoMessage(NCLangRes.getInstance()
				.getStrByID("50081020", "UPP50081020-000141", null,
						new String[] { mvos.length + "" }));

		if (id != 4)
			return;
		try {
			mvos[0].getHeadVO().initVO();
			PfUtilClient.processBatch(this, "DELETE", "A2",
					this.m_toft.getLogDate(), mvos, null);
			MMLog.writeLog(this.tp.getTitle(), 1,
					getstrbyid("UPT50081020-000011"),
					new String[] { getstrbyid("UPP50081020-000143") }, mvos);
			getListPanel().getHeadBillModel().delLine(getSelectedRows());
			getListPanel().getHeadTable().clearSelection();
			reportHint(getstrbyid("UPP50081020-000144"));
			boolean backwrite = false;
			for (int i = 0; i < mvos.length; i++) {
				if ((!isNull(mvos[i].getHeadVO().getLyid()))
						&& (mvos[i].getHeadVO().getLyid().length() > 0)
						&& (!isNull(mvos[i].getHeadVO().getLylx()))
						&& ((mvos[i].getHeadVO().getLylx().intValue() == 6)
								|| (mvos[i].getHeadVO().getLylx().intValue() == 7)
								|| (mvos[i].getHeadVO().getLylx().intValue() == 8)
								|| (mvos[i].getHeadVO().getLylx().intValue() == 9) || (mvos[i]
								.getHeadVO().getLylx().intValue() == 10)))
					backwrite = true;
			}
			if (backwrite) {
				ClientEnvironment.getInstance();
				MMProxy.getRemoteMO().backwrite(
						mvos,
						null,
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey(),
						ClientEnvironment.getServerTime());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			reportError(ex.getMessage());
		}
	}

	private void checkGXDandICExist(MoVO[] mvos) {
		if (mvos == null)
			return;
		StringBuffer pks = new StringBuffer();
		for (MoVO moVO : mvos) {
			pks.append("'" + moVO.getHeadVO().getPk_moid() + "',");
		}
		if (pks.length() > 0) {
			pks.deleteCharAt(pks.length() - 1);
		}
		String message = "所选择订单存在工序单";
		Object value = PublicUtil
				.getMapValue("select 1 from baoyin_process_main where nvl(dr,0)=0 and orderid in ("
						+ pks.toString() + ")");
		if (value != null) {
			this.m_toft.showErrorMessage(message);
			throw new BusinessRuntimeException(message);
		}
		value = PublicUtil
				.getMapValue("select 1 from ic_general_h where nvl(dr,0)=0 and pk_defdoc20 in ("
						+ pks.toString() + ")");
		if (value != null) {
			message = "所选择订单存在出入库单";
			this.m_toft.showErrorMessage(message);
			throw new BusinessRuntimeException(message);
		}
	}
	/**
	 * 生产订单维护强制完工
	 */
	@SuppressWarnings("all")	
	public void onFinish() {
		/**
		 * 彭佳 add 2018年8月16日10:42:54 宝翼公司设置非销售人员禁止强制完工
		 */
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//获取当前登录人主键
		String userPk = this.getUserPK();
		//获取当前登录公司
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		List list= new ArrayList();
		String usercode = null;
		String sqluser = "select user_code from sm_user where cuserid ='"+userPk+"' and nvl(dr,0)=0";
		try {
			list =  (List) bs.executeQuery(sqluser, new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		for(int i = 0;i<list.size();i++){
			Map map = new HashMap();
			map = (Map) list.get(0);
			usercode = map.get("user_code")==null?"":map.get("user_code").toString();
		}
		String sqldeptdoc = "select dept.deptname from bd_psndoc psn left join bd_deptdoc dept on psn.pk_deptdoc = dept.pk_deptdoc where psn.psncode ='"+usercode+"' and psn.pk_corp ='"+pk_corp+"'";
		if (pk_corp.equals("1016") || pk_corp.equals("1071")
				|| pk_corp.equals("1103") || pk_corp.equals("1097")
				|| pk_corp.equals("1017") || pk_corp.equals("1018")|| pk_corp.equals("1019")
				|| pk_corp.equals("1107")) {	
			try {
				List deptlist =(List) bs.executeQuery(sqldeptdoc, new MapListProcessor());
				if(deptlist.size()>0){
					Map deptmap = (Map) deptlist.get(0);
						if(!deptmap.get("deptname").equals("销售部")){
							ShowDialog.showErrorDlg(this, "提示", "非销售人员禁止强制完工");
							return;
						}		
				}else{
					ShowDialog.showErrorDlg(this, "提示", "该人员不属任何部门,无法强制完工");
					return;
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		MoVO[] mos = getSelectedVOs();
		if (mos == null)
			return;
		StringBuffer errmsg = new StringBuffer("");
		int err_len = errmsg.length();
		for (int i = 0; i < mos.length; i++) {
			if ((mos[i].getHeadVO().getZt() != null)
					&& (mos[i].getHeadVO().getZt().equalsIgnoreCase("B")))
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138")).append("<#")
						.append("{");
			errmsg.append(mos[i].getHeadVO().getScddh()).append(", ");
		}

		if (errmsg.length() > err_len) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append("}").append("#>")
					.append(getstrbyid("UPP50081020-000145"));
			errmsg.append(System.getProperty("line.separator"));
		}
		if (errmsg.length() > 0) {
			errmsg.append(getstrbyid("UPP50081020-000146"));
			reportError(errmsg.toString());
			return;
		}
		try {
			String C_desc = getDescriptOfState("C");
			for (int i = 0; i < mos.length; i++) {
				setCEInfo(mos[i].getHeadVO());
				mos[i].getHeadVO().setZt("C");
				mos[i].getHeadVO().setZtShow(C_desc);
				if (!isNull(mos[i].getHeadVO().getSjwgrq()))
					continue;
				mos[i].getHeadVO().setSjwgrq(
						new UFDate(this.m_toft.getLogDate()));
				UFTime tsjjs = new UFTime(System.currentTimeMillis());
				mos[i].getHeadVO().setSjjssj(tsjjs.toString());
			}
			if (getParentCorpCode().equals("10399")) {
				for (int i = 0; i < mos.length; i++) {
					String pk_moid = mos[i].getHeadVO().getPk_moid();
					UFDouble jhwgsl = mos[i].getHeadVO().getJhwgsl();
					UFDouble outNum = CommonUtil.getMOOutNum(pk_moid);
					if (outNum.compareTo(jhwgsl) < 0) {
						throw new BusinessException("选中的第" + (i + 1)
								+ "个生产订单出库数小于计划数,不能强制完工" + outNum + ":"
								+ jhwgsl);
					}
				}
			}
			Object[] newtss = PfUtilClient.processBatch(this, "FINISH", "A2",
					this.m_toft.getLogDate(), mos, null);
			MMLog.writeLog(this.tp.getTitle(), -1,
					getstrbyid("UPP50081020-000147"), null, mos);
			if (newtss != null) {
				for (int i = 0; i < mos.length; i++) {
					mos[i].getHeadVO().setTs((String) newtss[i]);
				}
			}
			updateMO(mos);
			reportHint(getstrbyid("UPP50081020-000148") + C_desc);
		} catch (Exception ex) {
			reportError(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	public void onMoCancel() {
		MoVO[] mos = getSelectedVOs();
		if (mos == null)
			return;
		StringBuffer errmsg = new StringBuffer("");
		int err_len = errmsg.length();
		for (int i = 0; i < mos.length; i++) {
			MoHeaderVO head = (MoHeaderVO) mos[i].getParentVO();
			if ((head.getSjwgsl() == null)
					|| (head.getSjwgsl().doubleValue() <= 0.0D))
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138")).append("{");
			errmsg.append(head.getScddh()).append(", ");
		}

		if (errmsg.length() > err_len) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append("}").append(getstrbyid("UPP50081020-000221"));
			errmsg.append(System.getProperty("line.separator"));
		}
		err_len = errmsg.length();
		for (int i = 0; i < mos.length; i++) {
			MoHeaderVO head = (MoHeaderVO) mos[i].getParentVO();
			if ((head.getZt() == null) || (head.getZt().equals("B")))
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138")).append("{");
			errmsg.append(head.getScddh()).append(", ");
		}

		if (errmsg.length() > err_len) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append("}").append(getstrbyid("UPP50081020-000222"));
			errmsg.append(System.getProperty("line.separator"));
		}
		if (errmsg.length() > 0) {
			errmsg.append(getstrbyid("UPP50081020-000152"));
			reportError(errmsg.toString());
			return;
		}
		try {
			String A_desc = getDescriptOfState("A");
			for (int i = 0; i < mos.length; i++) {
				mos[i].getHeadVO().setZt("A");
				mos[i].getHeadVO().setZtShow(A_desc);
				mos[i].getHeadVO().setSjkgrq(null);
				mos[i].getHeadVO().setSjkssj(null);
				mos[i].getHeadVO().setShrid(null);
				mos[i].getHeadVO().setShrq(null);
			}

			String[] newtss = MMProxy.getRemoteMO().mounput(mos);
			MMLog.writeLog(this.tp.getTitle(), -1,
					getstrbyid("UPT50081020-000008"), null, mos);
			if (newtss != null) {
				for (int i = 0; i < mos.length; i++) {
					mos[i].getHeadVO().setTs(newtss[i]);
				}
			}
			updateMO(mos);
			reportHint(getstrbyid("UPP50081020-000153"));
		} catch (Exception ex) {
			ex.printStackTrace();
			reportError(ex.getMessage());
		}
	}

	public void onMoPut() {
		long timemoput1 = System.currentTimeMillis();
		MoVO[] mvos = getSelectedVOs();
		if (mvos == null)
			return;
		StringBuffer errmsg = new StringBuffer("");
		int err_len = errmsg.length();
		for (int i = 0; i < mvos.length; i++) {
			if ((!isNull(mvos[i].getHeadVO().getZt()))
					&& (mvos[i].getHeadVO().getZt().equals("A")))
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138")).append("{");
			errmsg.append(mvos[i].getHeadVO().getScddh()).append(", ");
		}

		if (errmsg.length() > err_len) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append("}");
			errmsg.append(getstrbyid("UPP50081020-000154"));
			errmsg.append(System.getProperty("line.separator"));
		}
		if (errmsg.length() > 0) {
			errmsg.append(getstrbyid("UPP50081020-000155"));
			reportError(errmsg.toString());
			return;
		}
		try {
			String B_desc = getDescriptOfState("B");
			for (int i = 0; i < mvos.length; i++) {
				String ctime = new UFDateTime(System.currentTimeMillis())
						.getTime();
				mvos[i].getHeadVO().setZt("B");
				mvos[i].getHeadVO().setZtShow(B_desc);
				mvos[i].getHeadVO().setSjkgrq(
						new UFDate(this.m_toft.getLogDate()));
				mvos[i].getHeadVO().setSjkssj(ctime);
				mvos[i].getHeadVO().setShrid(
						this.m_toft.getUser().getPrimaryKey());
				mvos[i].getHeadVO().setShrq(
						new UFDate(this.m_toft.getLogDate()));
			}

			mvos[0].getHeadVO().initVO();
			mvos = (MoVO[]) PfUtilClient.processBatch(this, "MOPUT", "A2",
					this.m_toft.getLogDate(), mvos, null);
			updateMO(mvos);
			reportHint(getstrbyid("UPP50081020-000156"));
			MMLog.writeLog(this.tp.getTitle(), -1,
					getstrbyid("UPT50081020-000007"), null, mvos);
		} catch (Exception ex) {
			ex.printStackTrace();
			reportError(ex.getLocalizedMessage());
		}
		long timemoput2 = System.currentTimeMillis();
		long timetotal = timemoput2 - timemoput1;
		System.out.print("total zhanj");
		System.out.print(timetotal);
	}

	public void onOutSubmit() {
		MoVO[] mvos = getSelectedVOs();
		if ((mvos == null) || (mvos.length == 0))
			return;
		StringBuffer errmsg = new StringBuffer("");
		int err_len = errmsg.length();
		for (int i = 0; i < mvos.length; i++) {
			if ((mvos[i].getHeadVO().getZt() != null)
					&& (mvos[i].getHeadVO().getZt().equals("A")))
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138")).append("{");
			errmsg.append(mvos[i].getHeadVO().getScddh()).append(", ");
		}

		if (errmsg.length() > err_len) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append("}").append(getstrbyid("UPP50081020-000223"));
			errmsg.append(System.getProperty("line.separator"));
		}
		if (errmsg.length() > 0) {
			errmsg.append(getstrbyid("UPP50081020-000224"));
			reportError(errmsg.toString());
			return;
		}
		if (this.m_toft.showYesNoMessage(getstrbyid("UPP50081020-000159")) != 4)
			return;
		String ctime = new UFDateTime(System.currentTimeMillis()).getTime();
		String C_desc = getDescriptOfState("C");
		for (int i = 0; i < mvos.length; i++) {
			mvos[i].getHeadVO().initVO();
			mvos[i].getHeadVO().setZt("C");
			mvos[i].getHeadVO().setZtShow(C_desc);
			mvos[i].getHeadVO().setSjkgrq(new UFDate(this.m_toft.getLogDate()));
			mvos[i].getHeadVO().setSjkssj(ctime);
			mvos[i].getHeadVO().setSjwgrq(new UFDate(this.m_toft.getLogDate()));
			mvos[i].getHeadVO().setSjjssj(ctime);
			mvos[i].getHeadVO().setDdlx(new Integer(2));
			mvos[i].getHeadVO().setDdlxShow("委外");
		}

		try {
			String[] newtss = (String[]) PfUtilClient.processBatch(this,
					"OUTSUBMIT", "A2", this.m_toft.getLogDate(), mvos, null);
			MMLog.writeLog(this.tp.getTitle(), -1,
					getstrbyid("UPT50081020-000006"), null, mvos);
			if (newtss != null) {
				for (int i = 0; i < mvos.length; i++) {
					mvos[i].getHeadVO().setTs(newtss[i]);
				}
			}
			updateMO(mvos);
			reportHint(getstrbyid("UPP50081020-000160") + C_desc);
		} catch (Exception ex) {
			ex.printStackTrace();
			reportError(ex.getLocalizedMessage());
		}
	}

	public void onOver() {
		MoVO[] mvs = getSelectedVOs();
		if (mvs == null)
			return;
		StringBuffer errmsg = new StringBuffer("");
		int err_len = errmsg.length();
		for (int i = 0; i < mvs.length; i++) {
			if ((mvs[i].getHeadVO().getZt() != null)
					&& (mvs[i].getHeadVO().getZt().equalsIgnoreCase("C")))
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138"));
			errmsg.append(mvs[i].getHeadVO().getScddh()).append(", ");
		}

		if (errmsg.length() > err_len) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append(getstrbyid("UPP50081020-000161"));
			errmsg.append(System.getProperty("line.separator"));
		}
		if (errmsg.length() > 0) {
			errmsg.append(getstrbyid("UPP50081020-000162"));
			reportError(errmsg.toString());
			return;
		}
		try {
			String D_desc = getDescriptOfState("D");
			for (int i = 0; i < mvs.length; i++) {
				mvs[i].getHeadVO().setZt("D");
				mvs[i].getHeadVO().setZtShow(D_desc);
			}

			String[] newtss = MMProxy.getRemoteMO().over(mvs);
			MMLog.writeLog(this.tp.getTitle(), -1,
					getstrbyid("UPP50081020-000163"), null, mvs);
			if (newtss != null) {
				for (int i = 0; i < mvs.length; i++) {
					mvs[i].getHeadVO().setTs(newtss[i]);
				}
			}
			updateMO(mvs);
			reportHint(getstrbyid("UPP50081020-000148") + D_desc);
		} catch (Exception ex) {
			ex.printStackTrace();
			reportException(ex);
		}
	}
	/**
	 * 生产订单维护取消完工
	 */
	public void onUnFinish() {
		/**
		 * 彭佳  add 2018年8月21日13:49:49 宝翼公司设置非销售人员禁止取消完工
		 */
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//获取当前登录人主键
		String userPk = this.getUserPK();
		//获取当前登录公司
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		List list= new ArrayList();
		String usercode = null;
		String sqluser = "select user_code from sm_user where cuserid ='"+userPk+"' and nvl(dr,0)=0";
		try {
			list =  (List) bs.executeQuery(sqluser, new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		for(int i = 0;i<list.size();i++){
			Map map = new HashMap();
			map = (Map) list.get(0);
			usercode = map.get("user_code")==null?"":map.get("user_code").toString();
		}
		String sqldeptdoc = "select dept.deptname from bd_psndoc psn left join bd_deptdoc dept on psn.pk_deptdoc = dept.pk_deptdoc where psn.psncode ='"+usercode+"' and psn.pk_corp ='"+pk_corp+"'";
		if (pk_corp.equals("1016") || pk_corp.equals("1071")
				|| pk_corp.equals("1103") || pk_corp.equals("1097")
				|| pk_corp.equals("1017") || pk_corp.equals("1018")|| pk_corp.equals("1019")
				|| pk_corp.equals("1107")) {
			try {
				List deptlist = (List) bs.executeQuery(sqldeptdoc, new MapListProcessor());
					if (deptlist.size() > 0) {
						Map deptmap = (Map) deptlist.get(0);
						if (!deptmap.get("deptname").equals("销售部")) {
							ShowDialog.showErrorDlg(this, "提示", "非销售人员禁止取消完工");
							return;
						}
					} else {
						ShowDialog.showErrorDlg(this, "提示", "该人员不属任何部门,无法取消完工");
						return;
					}
	
				} catch (BusinessException e) {
					e.printStackTrace();
				}
		}
		MoVO[] mvs = getSelectedVOs();
		if (mvs == null)
			return;
		StringBuffer errmsg = new StringBuffer("");
		int err_len = errmsg.length();
		for (int i = 0; i < mvs.length; i++) {
			if (mvs[i].getHeadVO().getDdlx().intValue() != 2)
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138"));
			errmsg.append(mvs[i].getHeadVO().getScddh()).append(", ");
		}

		if (errmsg.length() > err_len) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append(getstrbyid("UPP50081020-000164"));
			errmsg.append(System.getProperty("line.separator"));
		}
		for (int i = 0; i < mvs.length; i++) {
			if ((mvs[i].getHeadVO().getZt() != null)
					&& (mvs[i].getHeadVO().getZt().equalsIgnoreCase("C")))
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138")).append("{");
			errmsg.append(mvs[i].getHeadVO().getScddh()).append(", ");
		}

		if (errmsg.length() > err_len) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append("}").append(getstrbyid("UPP50081020-000165"));
			errmsg.append(System.getProperty("line.separator"));
		}
		if (errmsg.length() > 0) {
			errmsg.append(getstrbyid("UPP50081020-000166"));
			reportError(errmsg.toString());
			return;
		}
		try {
			String B_desc = getDescriptOfState("B");
			for (int i = 0; i < mvs.length; i++) {
				mvs[i].getHeadVO().setZt("B");
				mvs[i].getHeadVO().setZtShow(B_desc);
			}

			String[] newtss = MMProxy.getRemoteMO().unfinish_MO(mvs);
			MMLog.writeLog(this.tp.getTitle(), -1,
					getstrbyid("UPP50081020-000167"), null, mvs);
			if (newtss != null) {
				for (int i = 0; i < mvs.length; i++) {
					mvs[i].getHeadVO().setTs(newtss[i]);
				}
			}
			updateMO(mvs);
			reportHint(getstrbyid("UPP50081020-000148") + B_desc);
		} catch (Exception ex) {
			reportError(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	protected void setCEInfo(MoHeaderVO headerVO) {
		ClientEnvironment cle = ClientEnvironment.getInstance();
		headerVO.setBusiDate(cle.getBusinessDate());
		headerVO.setUserid(cle.getUser() != null ? cle.getUser()
				.getPrimaryKey() : null);
		headerVO.setUserName(cle.getUser() != null ? cle.getUser()
				.getUserName() : null);
	}

	public void onUnOver() {
		MoVO[] mvs = getSelectedVOs();
		if (mvs == null)
			return;
		StringBuffer errmsg = new StringBuffer("");
		int err_len = errmsg.length();
		for (int i = 0; i < mvs.length; i++) {
			if ((mvs[i].getHeadVO().getZt() != null)
					&& (mvs[i].getHeadVO().getZt().equalsIgnoreCase("D")))
				continue;
			if (errmsg.length() == err_len)
				errmsg.append(getstrbyid("UPP50081020-000138")).append("{");
			errmsg.append(mvs[i].getHeadVO().getScddh()).append(", ");
		}

		if (errmsg.length() > err_len) {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.append("}").append(getstrbyid("UPP50081020-000168"));
			errmsg.append(System.getProperty("line.separator"));
		}
		if (errmsg.length() > 0) {
			errmsg.append(getstrbyid("UPP50081020-000169"));
			reportError(errmsg.toString());
			return;
		}
		try {
			String C_desc = getDescriptOfState("C");
			for (int i = 0; i < mvs.length; i++) {
				mvs[i].getHeadVO().setZt("C");
				mvs[i].getHeadVO().setZtShow(C_desc);
			}

			String[] newtss = MMProxy.getRemoteMO().unover(mvs);
			MMLog.writeLog(this.tp.getTitle(), -1,
					getstrbyid("UPP50081020-000170"), null, mvs);
			if (newtss != null) {
				for (int i = 0; i < mvs.length; i++) {
					mvs[i].getHeadVO().setTs(newtss[i]);
				}
			}
			updateMO(mvs);
			reportHint(getstrbyid("UPP50081020-000148") + C_desc);
		} catch (Exception ex) {
			ex.printStackTrace();
			reportException(ex);
		}
	}

	public void setData(MoHeaderVO[] heads) {
		if ((heads == null) || (heads.length == 0)) {
			getListPanel().getHeadBillModel().clearBodyData();
			return;
		}

		setNameByDD(heads);

		getListPanel().getBillListData().setHeaderValueVO(heads);
		//执行公式  add by Lin 2012/09/06
		getListPanel().getHeadBillModel().execLoadFormula();
	}

	protected void updateMO(MoVO[] vos) {
		if (vos == null)
			return;
		for (int i = 0; i < vos.length; i++) {
			String key = vos[i].getHeadVO().getPrimaryKey();
			int indexRow = 0;
			boolean find;
			for (find = false; (indexRow < getRowCount()) && (!find);) {
				String pk = (String) getListPanel().getHeadBillModel()
						.getValueAt(indexRow, "pk_moid");
				if (pk.equals(key))
					find = true;
				else {
					indexRow++;
				}
			}
			if (!find)
				getListPanel().getHeadBillModel().addLine();
			getListPanel().getHeadBillModel().setBodyRowVO(vos[i].getHeadVO(),
					indexRow);
			getListPanel().getHeadBillModel().setRowState(indexRow, 0);
		}

		updateUI();
	}

	public MOViewPanel(MMToftPanel mmtoft, MMToolKit tk) {
		super(mmtoft, tk);
		this.rfpFactory = null;
		this.listPanel = null;
		this.srcViewer = null;
		this.lock = new Object();
		this.tp = mmtoft;
		initialize();
	}

	public MOViewPanel(MMToftPanel mmtoft, MMToolKit tk, DdVO[] dds) {
		super(mmtoft, tk, dds);
		this.rfpFactory = null;
		this.listPanel = null;
		this.srcViewer = null;
		this.lock = new Object();
		this.tp = mmtoft;
		initialize();
	}

	public int getRowCount() {
		return getListPanel().getHeadTable().getRowCount();
	}

	public int getRowState(int row) {
		return getListPanel().getHeadBillModel().getRowState(row);
	}

	private MOSourceViewer getSrcViewer() {
		if (this.srcViewer == null)
			this.srcViewer = new MOSourceViewer(this, this.m_toft.getScaleNum());
		return this.srcViewer;
	}

	public void updateSoleMO(MoVO vo, boolean isAdd) {
		int indexRow;
		if (isAdd) {
			indexRow = getRowCount();
			getListPanel().getHeadBillModel().addLine();
			setNameByDD(new MoHeaderVO[] { vo.getHeadVO() });
		} else {
			indexRow = getListPanel().getHeadTable().getSelectedRowCount();
			if ((indexRow != 1)
					|| ((indexRow = getListPanel().getHeadTable()
							.getSelectedRow()) < 0)
					|| (indexRow >= getRowCount()))
				return;
		}
		getListPanel().getHeadBillModel()
				.setBodyRowVO(vo.getHeadVO(), indexRow);
		getListPanel().getHeadTable().clearSelection();
		getListPanel().getHeadTable().setRowSelectionInterval(indexRow,
				indexRow);
		getListPanel().getHeadBillModel().setRowState(indexRow, 0);
	}

	public void viewSource() {
		MoVO vo = getSelectedVO();
		if (vo != null)
			getSrcViewer().setHeadData((MoHeaderVO) vo.getParentVO());
		getSrcViewer().showModal(vo.getHeadVO().getSjscddh());
	}

	public void updatePrintedFlag(int[] rows) {
		if ((rows == null) || (rows.length == 0))
			return;
		int n = getRowCount();
		for (int i = 0; i < rows.length; i++) {
			if ((rows[i] < 0) || (rows[i] >= n))
				continue;
			getListPanel().getHeadBillModel().setValueAt(new Integer(1),
					rows[i], "dyzt");
			getListPanel().getHeadBillModel().setValueAt(
					NCLangRes.getInstance().getStrByID("50081020",
							"UPP50081020-000064"), rows[i], "dyztshow");
		}
	}

	public void onBatchRevise() {
		MoVO[] vos = getSelectedVOs();
		if ((vos == null) || (vos.length == 0))
			return;
		for (int i = 0; i < vos.length; i++) {
			if (vos[0].getHeadVO().getJhkgrq()
					.equals(vos[i].getHeadVO().getJhkgrq()))
				continue;
			reportError(getstrbyid("UPP50081020-000171"));
			return;
		}

		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < vos.length; i++) {
			if ((vos[i].getHeadVO().getZt().equals("C"))
					|| (vos[i].getHeadVO().getZt().equals("D"))) {
				reportError(getstrbyid("UPP50081020-000172"));
				return;
			}
			if ((vos[i].getHeadVO().getSjwgsl() != null)
					&& (vos[i].getHeadVO().getSjwgsl().doubleValue() > 0.0D)) {
				reportError(getstrbyid("UPP50081020-000173"));
				return;
			}
			if (!vos[i].getHeadVO().getZt().equals("B"))
				continue;
			reportError(getstrbyid("UPP50081020-000174"));
			return;
		}

		BatchReviseDialog dlgRevise = new BatchReviseDialog(this);
		dlgRevise.defineInitialFilters(vos);
		dlgRevise.showModal();
		if (!dlgRevise.isCloseOk())
			return;
		String pgflag = "1";
		for (int i = 0; i < vos.length; i++) {
			if ((!isNull(vos[i].getHeadVO().getBcid()))
					|| (!isNull(vos[i].getHeadVO().getBzid())))
				pgflag = "0";
		}
		ReviseVO revisedVO = dlgRevise.getRevisedData();
		if (revisedVO.isAllValueNull()) {
			reportError(getstrbyid("UPP50081020-000175"));
			return;
		}
		if (pgflag == "1") {
			if ((isNull(revisedVO.getDeptID()))
					|| (isNull(revisedVO.getWCenterID()))) {
				reportError(getstrbyid("UPP50081020-000256"));
				return;
			}
		} else if ((isNull(revisedVO.getDeptID()))
				|| (isNull(revisedVO.getWCenterID()))
				|| (isNull(revisedVO.getBcID()))) {
			reportError(getstrbyid("UPP50081020-000175"));
			return;
		}
		MoHeaderVO[] headers = new MoHeaderVO[vos.length];
		Integer[] pickmRemakeFlag = new Integer[vos.length];
		Integer notRemake = new Integer(0);
		Integer remake = new Integer(1);
		StringBuffer errmsg = new StringBuffer("");
		int err_len = errmsg.length();
		Vector v = new Vector();
		Vector vint = new Vector();
		for (int i = 0; i < vos.length; i++) {
			headers[i] = vos[i].getHeadVO();
			pickmRemakeFlag[i] = notRemake;
			String aversion = null;
			try {
				aversion = MMProxy.getRemoteMO().getRtVerByMainWCenter(vos[i],
						revisedVO.getWCenterID());
			} catch (Exception ex) {
				ex.printStackTrace();
				reportError(getstrbyid("UPP50081020-000176") + ex.getMessage());
			}
			if (aversion != null) {
				vos[i].getHeadVO().setScbmid(revisedVO.getDeptID());
				vos[i].getHeadVO().setScbm(revisedVO.getDeptName());
				vos[i].getHeadVO().setRtver(aversion);
				vos[i].getHeadVO().setGzzxid(revisedVO.getWCenterID());
				vos[i].getHeadVO().setGzzxmc(revisedVO.getWcenterName());
				if (!isNull(revisedVO.getBcID())) {
					vos[i].getHeadVO().setBcid(revisedVO.getBcID());
					vos[i].getHeadVO().setBcmc(revisedVO.getBcName());
				}
				if (!isNull(revisedVO.getBzID())) {
					vos[i].getHeadVO().setBzid(revisedVO.getBzID());
					vos[i].getHeadVO().setBzmc(revisedVO.getBzName());
				}
				pickmRemakeFlag[i] = remake;
				if (!isNull(vos[i].getHeadVO().getPzh()))
					pickmRemakeFlag[i] = notRemake;
				v.addElement(vos[i]);
				vint.addElement(pickmRemakeFlag[i]);
			} else {
				errmsg.append(vos[i].getHeadVO().getScddh()).append(", ");
			}
		}

		MoVO[] batchvos = new MoVO[v.size()];
		if (v.size() > 0)
			v.copyInto(batchvos);
		Integer[] pickmremake = new Integer[vint.size()];
		if (vint.size() > 0)
			vint.copyInto(pickmremake);
		if ((batchvos.length > 0) && (pickmremake.length > 0)) {
			String[] strTss = (String[]) null;
			try {
				strTss = MMProxy.getRemoteMO().batchRevise(batchvos,
						pickmremake);
				MMLog.writeLog(this.tp.getTitle(), 2,
						getstrbyid("UPT50081020-000022"), null, batchvos);
			} catch (Exception ex) {
				ex.printStackTrace();
				reportError(getstrbyid("UPP50081020-000177") + ex.getMessage());
			}
			for (int i = 0; i < batchvos.length; i++) {
				batchvos[i].getHeadVO().setTs(strTss[i]);
			}
			updateMO(batchvos);
		}
		if (errmsg.length() == 0) {
			reportHint(getstrbyid("UPP50081020-000178"));
		} else {
			errmsg.delete(errmsg.length() - 2, errmsg.length());
			errmsg.insert(0, getstrbyid("UPP50081020-000179"));
			reportHint(getstrbyid("UPP50081020-000180") + errmsg.toString()
					+ getstrbyid("UPP50081020-000181"));
		}
	}

	/**
	 * @功能:返回公司的上级公司编码
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public String getParentCorpCode() {

		String ParentCorp = new String();
		String key = ClientEnvironment.getInstance().getCorporation()
				.getFathercorp();
		try {
			CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
			ParentCorp = corpVO.getUnitcode();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ParentCorp;
	}
}