package nc.ui.ic.ic217;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.vo.bd.CorpVO;
import nc.vo.fts.pub.print.FreeVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.pub.SCMEnv;
import nc.ui.ic.pub.locatorref.LocatorRefPane;


/**
 * 其他出库
 * */
public class ClientUI extends GeneralBillClientUI {
	private boolean m_bIsOutBill = false;
	private UITextField m_uitfHinttext = new UITextField();
	/****************************/
	private Hashtable invidinfo;// 记录存货信息
	private ArrayList PilenoList;// 记录扫描过的垛号
	private String oldInvID = new String();// 记录选择的存货
	private boolean flag;// 垛号扫描

	/**********************/
	public ClientUI() {
		initialize();
	}

	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);
	}

	protected void afterBillEdit(BillEditEvent e) {
		if (e.getKey().equals("cotherwhid")) {
			String sName = ((UIRefPane) getBillCardPanel().getHeadItem(
					"cotherwhid").getComponent()).getRefName();

			if (this.m_voBill != null)
				this.m_voBill.setHeaderValue("cotherwhname", sName);
		}
		if (getParentCorpCode().equals("10395")) {
			if (e.getKey().equals("StampNo")) {
				if (!String.valueOf(e.getValue()).equals("")) {
					DoSplitData(e.getValue().toString(), 0);
					getBillCardPanel().getHeadItem("StampNo").setValue("");
					for (int i = 0; i < getBillCardPanel().getBillTable()
							.getRowCount(); i++) {
						getBillCardPanel().setBodyValueAt((i + 1) * 10, i,
								"crowno");
					}
				}
				getBillCardPanel().getHeadItem("StampNo").getComponent()
						.requestFocusInWindow();
			} else if (e.getKey().equals("StampCount")) {
				if (!String.valueOf(e.getValue()).equals("")) {
					DoSplitData("", Integer.parseInt(e.getValue().toString()));
					getBillCardPanel().getHeadItem("StampCount").setValue("");
					for (int i = 0; i < getBillCardPanel().getBillTable()
							.getRowCount(); i++) {
						getBillCardPanel().setBodyValueAt((i + 1) * 10, i,
								"crowno");
					}
				}
			}
			if (e.getKey().equals("cwarehouseid")
					|| e.getKey().equals("cinventorycode")) {
				if (flag) {
					return;
				}
				String cwarehouseid = ((UIRefPane) getBillCardPanel()
						.getHeadItem("cwarehouseid").getComponent()).getRefPK();

				if (cwarehouseid == null || cwarehouseid.equals("")) {
					return;
				}

				if (Iscsflag(cwarehouseid))// ||sCode.equals(arg0))
				{

					for (int i = 0; i < getBillCardPanel().getBillTable()
							.getRowCount(); i++) {

						String cinventoryid = (String) getBillCardPanel()
								.getBodyValueAt(i, "cinventoryid");
						if (cinventoryid == null || cinventoryid.equals("")) {
							return;
						}
						/**
						 * @功能:根据仓库、存货带出默认货位
						 * @author ：林桂莹
						 * @2012/9/5
						 * 
						 * @since v50
						 */
						String Sql = "select * from (select d.pk_cargdoc,d.csname,d.cscode from ic_general_h a  ";
						Sql += "left join ic_general_b b on a.cgeneralhid=b.cgeneralhid  ";
						Sql += "left join ic_general_bb1 c on c.cgeneralbid=b.cgeneralbid  ";
						Sql += "left join bd_cargdoc d on c.cspaceid=d.pk_cargdoc  ";
						Sql += "where a.cbilltypecode ='4I' and a.cwarehouseid='"
								+ cwarehouseid
								+ "' and  b.cinventoryid='"
								+ cinventoryid + "'  ";
						Sql += "and d.pk_cargdoc is not null  and a.taccounttime is not null and nvl(b.dr,0)=0 order by a.taccounttime desc  ";
						Sql += ") where rownum=1  ";
						IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
								.getInstance().lookup(
										IUAPQueryBS.class.getName());
						List list = null;
						try {
							list = (List) sessionManager.executeQuery(Sql,
									new ArrayListProcessor());

							if (list.isEmpty()) {

								Sql = "select * from (select kp.cspaceid ,car.csname,car.cscode   ";
								Sql += "from   v_ic_onhandnum6 kp  ";
								Sql += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
								Sql += "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null and kp.cwarehouseid='"
										+ cwarehouseid
										+ "'  and  kp.cinventoryid='"
										+ cinventoryid + "')  ";
								Sql += "where rownum=1  ";
								list = (List) sessionManager.executeQuery(Sql,
										new ArrayListProcessor());
								if (list.isEmpty()) {
									return;
								}
							}

						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						Iterator iterator = list.iterator();
						while (iterator.hasNext()) {
							ArrayList values = new ArrayList();
							Object obj = iterator.next();
							if (obj == null) {
								continue;
							}
							if (obj.getClass().isArray()) {
								int len = Array.getLength(obj);
								for (int j = 0; j < len; j++) {
									values.add(Array.get(obj, j));
								}
							}

							setSpace(String.valueOf(values.get(0)),
									String.valueOf(values.get(1)), i);
						}
					}

				}
			}
		}
	}

	protected void afterBillItemSelChg(int iRow, int iCol) {
	}

	public void afterWhEditNoClearCalbody(BillEditEvent e) {
		try {
			String sNewWhID = ((GeneralBillHeaderVO) this.m_voBill
					.getParentVO()).getCwarehouseid();

			if (sNewWhID == null) {
				getLotNumbRefPane().setWHParams(null);
			} else {
				int iQryMode = 1;

				Object oParam = sNewWhID;

				ArrayList alAllInvID = new ArrayList();
				boolean bHaveInv = getCurInvID(alAllInvID);

				WhVO voWh = null;

				getLotNumbRefPane().setWHParams(null);
				if (this.m_voBill != null) {
					this.m_voBill.setWh(null);
				}
				if (bHaveInv) {
					ArrayList alParam = new ArrayList();
					alParam.add(sNewWhID);
					iQryMode = 15;

					if ((this.m_voBill != null)
							&& (this.m_voBill.getWh() != null))
						alParam.add(this.m_voBill.getWh().getPk_calbody());
					else {
						alParam.add(null);
					}
					alParam.add(this.m_sCorpID);

					alParam.add(alAllInvID);
					oParam = alParam;
				}

				Object oRet = GeneralBillHelper.queryInfo(
						new Integer(iQryMode), oParam);

				if ((oRet instanceof ArrayList)) {
					ArrayList alRetValue = (ArrayList) oRet;
					if ((alRetValue != null) && (alRetValue.size() >= 2)) {
						voWh = (WhVO) alRetValue.get(0);

						freshPlanprice((ArrayList) alRetValue.get(1));
					}
				} else {
					voWh = (WhVO) oRet;
				}
				BillItem biCalBody = getBillCardPanel().getHeadItem(
						"pk_calbody");

				if (biCalBody != null) {
					if (voWh != null)
						biCalBody.setValue(voWh.getPk_calbody());
					else
						biCalBody.setValue(null);
				}
				BillItem biCalBodyname = getBillCardPanel().getHeadItem(
						"vcalbodyname");

				if (biCalBodyname != null) {
					if (voWh != null)
						biCalBodyname.setValue(voWh.getVcalbodyname());
					else {
						biCalBodyname.setValue(null);
					}
				}
				if (this.m_voBill != null) {
					this.m_voBill.setWh(voWh);

					this.m_voBill.clearInvQtyInfo();
					getLotNumbRefPane().setWHParams(voWh);
				}

				setBtnStatusSpace(true);
			}
		} catch (Exception e2) {
			SCMEnv.out(e2);
		}
	}

	public boolean beforeBillItemEdit(BillEditEvent e) {
		return true;
	}

	protected void beforeBillItemSelChg(int iRow, int iCol) {
	}

	protected boolean checkVO(GeneralBillVO voBill) {
		String sSourceBillType = getSourBillTypeCode();
		try {
			boolean bCheck = true;
			bCheck = super.checkVO();
			if (bCheck) {
				if ((sSourceBillType != null)
						&& ((sSourceBillType.equals(BillTypeConst.m_assembly))
								|| (sSourceBillType
										.equals(BillTypeConst.m_disassembly))
								|| (sSourceBillType
										.equals(BillTypeConst.m_transform))
								|| (sSourceBillType
										.equals(BillTypeConst.m_check)) || (sSourceBillType
								.equals(BillTypeConst.m_AllocationOrder)))) {
					VOCheck.checkGreaterThanZeroInput(voBill.getChildrenVO(),
							this.m_sNumItemKey, NCLangRes.getInstance()
									.getStrByID("common", "UC000-0002282"));
				}
			}
			return bCheck;
		} catch (ICPriceException e) {
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(
					getBillCardPanel(), e.getErrorRowNums(), e.getHint());
			showErrorMessage(sErrorMessage);
			return false;
		} catch (ValidationException e) {
			SCMEnv.out("校验异常！其他未知故障...");
			handleException(e);
			return false;
		} catch (Exception ex) {
			showErrorMessage(ex.getMessage());
		}
		return false;
	}

	public void errormessageshow(String error) {
		showErrorMessage(error);
	}

	public void filterWhRef(String sPk_calbody) {
		String[] sConstraint = null;
		if (sPk_calbody != null) {
			sConstraint = new String[1];
			sConstraint[0] = (" AND pk_calbody='" + sPk_calbody + "'");
		}
		BillItem bi = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey);
		RefFilter.filtWh(bi, this.m_sCorpID, sConstraint);
	}

	public BillCardPanel getBillCardPanel() {
		return super.getBillCardPanel();
	}

	public BillListPanel getBillListPanel() {
		return super.getBillListPanel();
	}

	public int getCardTableRowNum() {
		return getBillCardPanel().getRowCount();
	}

	public int getCurPanelParam() {
		return this.m_iCurPanel;
	}

	public int getLastSelListHeadRow() {
		return this.m_iLastSelListHeadRow;
	}

	public ArrayList getLoctorData() {
		return this.m_alLocatorData;
	}

	public ArrayList getSerialData() {
		return this.m_alSerialData;
	}

	private String getSysParams(String sCorpID, String sParamsCode)
			throws Exception {
		String sparams = null;
		try {
			sparams = SysInitBO_Client.getParaString(sCorpID, sParamsCode);
			if (sparams != null)
				sparams = sparams.trim();
		} catch (Exception e) {
			SCMEnv.error(e);
		}

		return sparams;
	}

	public String getTitle() {
		return super.getTitle();
	}

	public UITextField getUITxtFldStatus() {
		return this.m_uitfHinttext;
	}

	public void initialize() {
		super.initialize();
 getBillCardPanel().addBillEditListenerHeadTail(this);
		BillItem bi = getBillCardPanel().getHeadItem("cotherwhid");
		RefFilter.filtWh(bi, this.m_sCorpID, null);
	}

	protected void initPanel() {
		super.setBillInOutFlag(-1);

		super.setNeedBillRef(false);

		this.m_sBillTypeCode = BillTypeConst.m_otherOut;

		this.m_sCurrentBillNode = "40080808";
	}

	protected void initPanelold() {
		super.setBillInOutFlag(-1);

		super.setNeedBillRef(false);

		this.m_sBillTypeCode = BillTypeConst.m_otherOut;

		this.m_sCurrentBillNode = "40080808";
	}

	public void onButtonClicked(ButtonObject bo) {
		
		super.onButtonClicked(bo);
	}

	public boolean onSave() {
		boolean bisSave = super.onSave();
		return bisSave;
	}

	public void removeListHeadMouseListener() {
	}

	protected void selectBillOnListPanel(int iBillIndex) {
	}

	public void setAllData(ArrayList alListData) {
		try {
			this.m_bIsOutBill = true;
			this.m_alListData = alListData;
			if ((this.m_alListData != null) && (this.m_alListData.size() > 0)) {
				GeneralBillHeaderVO[] voh = new GeneralBillHeaderVO[this.m_alListData
						.size()];
				for (int i = 0; i < this.m_alListData.size(); i++) {
					if (this.m_alListData.get(i) != null) {
						voh[i] = ((GeneralBillHeaderVO) ((GeneralBillVO) this.m_alListData
								.get(i)).getParentVO());
					} else {
						SCMEnv.out("list data error!-->" + i);
					}
				}
				setListHeadData(voh);

				selectListBill(0);

				this.m_iLastSelListHeadRow = 0;

				this.m_iCurDispBillNum = -1;

				this.m_iBillQty = this.m_alListData.size();

				if ((this.m_iLastSelListHeadRow >= 0)
						&& (this.m_iCurDispBillNum != this.m_iLastSelListHeadRow)
						&& (this.m_alListData != null)
						&& (this.m_alListData.size() > this.m_iLastSelListHeadRow)
						&& (this.m_alListData.get(this.m_iLastSelListHeadRow) != null)) {
					for (int i = 0; i < this.m_alListData.size(); i++) {
						this.m_voBill = ((GeneralBillVO) this.m_alListData
								.get(i));

						if (i != 0)
							setBillVO(this.m_voBill);
						afterWhEditNoClearCalbody(null);
						this.m_alListData.set(i, this.m_voBill);
					}

					this.m_voBill = ((GeneralBillVO) this.m_alListData
							.get(this.m_iLastSelListHeadRow));

					setBillVO(this.m_voBill);
				}

				setButtonStatus(true);
			}

		} catch (Exception e) {
			handleException(e);
			showHintMessage(NCLangRes.getInstance().getStrByID("4008busi",
					"UPP4008busi-000292"));
		}
	}

	public void setAllLotRefAuto() {
		String sBatchField = "vbatchcode";
		for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
			if ((getBillCardPanel().getBodyValueAt(i, sBatchField) == null)
					|| (getBillCardPanel().getBodyValueAt(i, sBatchField)
							.toString().trim().length() == 0)) {
				continue;
			}

			getBillCardPanel().getBillTable().setRowSelectionInterval(i, i);

			getLotNumbRefPane().setParameter(this.m_voBill.getWh(),
					this.m_voBill.getItemInv(i));

			getLotNumbRefPane().setText(
					getBillCardPanel().getBodyValueAt(i, sBatchField)
							.toString().trim());

			getLotRefbyHand(sBatchField);
			pickupLotRef(sBatchField);
		}
	}

	public boolean setBillCodeAuto() {
		BillItem bi = getBillCardPanel().getHeadItem("vbillcode");
		if ((bi != null)
				&& ((bi.getValue() == null) || (bi.getValue().trim().length() == 0))
				&& (!this.m_bIsEditBillCode)) {
			this.m_voBill.setVBillCode(this.m_sCorpID);
			bi.setValue(this.m_sCorpID);
		}

		return true;
	}

	protected void setBillValueVO(GeneralBillVO bvo) {
		getBillCardPanel().getBillModel().removeTableModelListener(this);
		try {
			getBillCardPanel().setBillValueVO(bvo);

			getBillCardPanel().getBillModel().execLoadFormula();
			execHeadTailFormulas();

			bvo.clearInvQtyInfo();

			getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
		}

		getBillCardPanel().getBillModel().addTableModelListener(this);

		ctrlSourceBillUI(true);
	}

	public void setBillVO(GeneralBillVO bvo, boolean bIsOnlySet) {
		try {
			if (bIsOnlySet) {
				getBillCardPanel().addNew();
				setBillValueVO(bvo);
				this.m_voBill = bvo;
			} else {
				setBillVO(bvo);
			}

			getBillCardPanel().getBillModel().execLoadFormula();
			execHeadTailFormulas();
		} catch (Exception e) {
			SCMEnv.error(e);
		}
	}

	public void setBodyMenuShow(boolean bShowFlag) {
		getBillCardPanel().setBodyMenuShow(bShowFlag);
	}

	protected void setButtonsStatus(int iBillMode) {
	}

	public void setCardMode(int NewCardMode) {
		this.m_iMode = NewCardMode;
	}

	public void setCardPanelEnable(boolean bEnable) {
		getBillCardPanel().setEnabled(bEnable);
	}

	public void setHeadItemEnable(String sItemKey, boolean bCan) {
		BillItem bi = getBillCardPanel().getHeadItem(sItemKey);
		if (bi != null)
			bi.setEnabled(bCan);
	}

	public void setLastSelListHeadRow(int lastrow) {
		this.m_iLastSelListHeadRow = lastrow;
		selectListBill(lastrow);
	}

	public void setLoctorData(ArrayList alLoc) {
		this.m_alLocatorData = alLoc;
	}

	public void setSerialData(ArrayList alSN) {
		this.m_alSerialData = alSN;
	}

	public void setUITxtFldStatus(UITextField hinttext) {
		this.m_uitfHinttext = hinttext;
	}

	public void showErrorMessage(String sMessage) {
		MessageDialog.showErrorDlg(
				this,
				NCLangRes.getInstance().getStrByID("4008busi",
						"UPPSCMCommon-000059"), sMessage);
	}

	public void showHintMessage(String sMessage) {
		if (this.m_bIsOutBill) {
			getUITxtFldStatus().setText(sMessage);
		} else
			super.showHintMessage(sMessage);
	}

	/**
	 * @功能:垛号扫描功能
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	private void DoSplitData(String pileno, int pilecount) {
		// TODO Auto-generated method stub
		invidinfo = null;
		PilenoList = null;
		InitInvID();
		flag = true;
		String cinventoryid = new String();
		UFDouble num = null;
		String cspaceid = new String();
		String vbatchcode = new String();
		String csname = new String();
		String Wh = new String();
		UFDouble StampCount = new UFDouble();
		boolean ischekcount = false;
		Wh = ((UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid")
				.getComponent()).getRefPK();
		if (Wh == null || Wh.equals("") || Wh.equals("null")) {
			MessageDialog.showErrorDlg(this, "其他出库Other a library", "仓库不能为空!warehouse can not be empty!");
			return;
		}
		if ((pilecount == 0) && !pileno.equals(""))// 垛号扫描
		{

			if (PilenoList != null || PilenoList.toArray().length > 0) {
				if (PilenoList.indexOf(pileno) >= 0) {
					MessageDialog.showErrorDlg(this, "其他出库Other a library", Transformations.getLstrFromMuiStr("跺号@Stamp&:" + pileno

					+ "&已扫描过!@has scanned!"));
					return;
				}
			}

			String SQL = "select * from (  ";
			SQL += "select  kp.cinventoryid, kp.cspaceid, SUM ( nvl( ninspacenum, 0.0 ) ) - SUM ( nvl( noutspacenum, 0.0 ) ) num,kp.vbatchcode,kp.vfree1,car.csname  ";
			SQL += "from v_ic_onhandnum6 kp  ";
			SQL += "left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc   ";
			SQL += "left join bd_invbasdoc inv  on man.pk_invbasdoc = inv.pk_invbasdoc  ";
			SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid  ";
			SQL += "where kp.vfree1='"
					+ pileno
					+ "' and kp.pk_corp='"
					+ getClientEnvironment().getInstance().getCorporation()
							.getPrimaryKey() + "' and kp.cwarehouseid='" + Wh
					+ "'  ";
			SQL += "group by kp.pk_corp, kp.ccalbodyid, kp.cwarehouseid, kp.cinventoryid, kp.vbatchcode,kp.cspaceid ,kp.vfree1,car.csname ";
			SQL += ")  where num>0 ";

			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());
			List list = null;
			try {
				list = (List) sessionManager.executeQuery(SQL,
						new ArrayListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				ArrayList values = new ArrayList();
				Object obj = iterator.next();
				if (obj.getClass().isArray()) {
					int len = Array.getLength(obj);
					for (int i = 0; i < len; i++) {
						values.add(Array.get(obj, i));
					}
				}
				cinventoryid = String.valueOf(values.get(0));

				if (values.get(1) != null) {
					cspaceid = (String) values.get(1);
				}
				num = new UFDouble(String.valueOf(values.get(2)));
				if (values.get(3) != null) {
					vbatchcode = (String) values.get(3);
				}
				csname = (String) values.get(5);
			}
			if (cinventoryid == null || cinventoryid.equals("")) {
				MessageDialog.showErrorDlg(this, "其他出库Other a library", Transformations.getLstrFromMuiStr("跺号@Stamp&:" + pileno
						+ "&不存在!@Does not exist!"));
				return;
			} else if (invidinfo != null) {
				if (!invidinfo.containsKey(cinventoryid)) {
					MessageDialog.showErrorDlg(this, "其他出库Other a library", Transformations.getLstrFromMuiStr("当前垛号@The current stack number&:" + pileno
							+ "&对应的存货不属于本次出库范围!@corresponding to the inventory does not belong to the scope of delivery!"));
					return;
				}

			}
			int Sindex = GetStartIndex(cinventoryid);
			String cinventorycode = String.valueOf(getBillCardPanel()
					.getBodyValueAt(Sindex, "cinventorycode"));

			int row = GetInvIDRowRelation(cinventoryid);
			int recordcount = getBillCardPanel().getBillTable().getRowCount();

			int Eindex = GetEndIndex(cinventoryid);
			UFDouble nshououtnum = (UFDouble) getBillCardPanel()
					.getBodyValueAt(Sindex, "nshouldoutnum");
			String oldpileno = String.valueOf(getBillCardPanel()
					.getBodyValueAt(Eindex, "vfree0"));
			if ((oldpileno == null) || (oldpileno.equals(""))
					|| (oldpileno.equals("null"))) {
				SetBodyNewValue(pileno, num, vbatchcode, csname, cspaceid,
						Eindex, nshououtnum);
				String key="";
				try {
					 key=this.m_voBill.getChildrenVO()[Eindex].getPrimaryKey();
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					
				}
				if(key!=null&&!key.equals("")&&!key.equals("null"))
				{
					getBillCardPanel().getBillModel().setRowState(Eindex,BillModel.MODIFICATION);
				}
			} else if ((oldpileno != null) && (!oldpileno.equals(""))
					&& (!oldpileno.equals("null"))) {
				if (Eindex != recordcount - 1) {
					getBillCardPanel().getBodyPanel().insertLine(Eindex + 1, 1);
				} else {
					getBillCardPanel().getBodyPanel().addLine(1);
				}

				GeneralBillItemVO newbi = voCopyLine(Eindex);// (GeneralBillItemVO)
																// bi.clone();

				getBillCardPanel().getBillModel().setBodyRowVO(newbi,
						Eindex + 1);

				String m_invcode = String.valueOf(getBillCardPanel()
						.getBodyValueAt(Eindex, "cinventorycode"));
				String m_invid = String.valueOf(getBillCardPanel()
						.getBodyValueAt(Eindex, "cinventoryid"));
				String m_invname = String
						.valueOf(((UIRefPane) getBillCardPanel().getBodyItem(
								"cinventorycode").getComponent()).getRefName());
				getBillCardPanel().getBillModel().setBodyRowVO(newbi,
						Eindex + 1);

				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
						.getComponent()).setPK(m_invid);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
						.getComponent()).setName(m_invname);

				BillEditEvent e = new BillEditEvent(getBillCardPanel()
						.getBodyItem("cinventorycode").getComponent(),
						m_invcode, "cinventorycode", Eindex + 1);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
						.getComponent()).setPK(m_invid);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
						.getComponent()).setName(m_invname);
				afterEdit(e);
				SetBodyNewValue(pileno, num, vbatchcode, csname, cspaceid,
						Eindex + 1, nshououtnum);
			}

		} else if ((pilecount > 0) && pileno.equals(""))// 按垛数来出库
		{
			int OldCount = pilecount;
			String m_cinventoryid = new String();

			int selRow = getBillCardPanel().getBillTable().getSelectedRow();

			int recordcount = getBillCardPanel().getBillTable().getRowCount();
			if (selRow < -1
					&& (oldInvID == null || oldInvID.equals("") || oldInvID
							.equals("null"))) {
				MessageDialog.showErrorDlg(this, "其他出库Other a library", "请选择数据!Please select the data!");
				return;
			} else if (recordcount == 1) {
				m_cinventoryid = String.valueOf(getBillCardPanel()
						.getBodyValueAt(0, "cinventoryid"));
			} else {
				m_cinventoryid = String.valueOf(getBillCardPanel()
						.getBodyValueAt(selRow, "cinventoryid"));
			}
			if (m_cinventoryid == null || m_cinventoryid.equals("")
					|| m_cinventoryid.equals("null")) {
				m_cinventoryid = oldInvID;
			} else {

				oldInvID = m_cinventoryid;
			}
			String pileo = new String();

			int row = GetInvIDRowRelation(m_cinventoryid);
			int Sindex = GetStartIndex(m_cinventoryid);
			int Eindex = GetEndIndex(m_cinventoryid);
			UFDouble nshououtnum = (UFDouble) getBillCardPanel()
					.getBodyValueAt(Sindex, "nshouldoutnum");
			if (row > pilecount) {
				int[] array = new int[row - pilecount];
				int j = 0;
				for (int i = Eindex; Eindex - i < row - pilecount; i--) {
					array[j] = i;
					j++;
				}
				getBillCardPanel().getBillModel().delLine(array);
				return;
			} else if (pilecount == row) {
				MessageDialog.showErrorDlg(this, "其他出库Other a library",
						Transformations.getLstrFromMuiStr("当前出库垛数已等于@The library stack number is equal to current&" + String.valueOf(pilecount) + "!"));
				return;
			}
			for (int i = Sindex; i <= Eindex; i++) {
				String n_cinventoryid = String.valueOf(getBillCardPanel()
						.getBodyValueAt(i, "cinventoryid"));
				if (m_cinventoryid.equals(n_cinventoryid)) {

					String temp = String.valueOf(getBillCardPanel()
							.getBodyValueAt(i, "vfree0"));
					int srtIndex = temp.indexOf(":");
					int endIndex = temp.indexOf("]");
					if (temp != null && !temp.equals("")
							&& !temp.equals("null")) {
						pileo += "'" + temp.substring(srtIndex + 1, endIndex)
								+ "',";
					}
				}

			}
			if (pileo.length() > 0) {
				pilecount = pilecount - row;
			}
			String cinventorycode = String.valueOf(getBillCardPanel()
					.getBodyValueAt(Sindex, "cinventorycode"));
			StampCount = new UFDouble("0");

			String SQL = "select * from (  ";
			SQL += "select  kp.cinventoryid, kp.cspaceid, SUM ( nvl( ninspacenum, 0.0 ) ) - SUM ( nvl( noutspacenum, 0.0 ) ) num,kp.vbatchcode,kp.vfree1,car.csname   ";
			SQL += "from v_ic_onhandnum6 kp  ";
			SQL += "left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc   ";
			SQL += "left join bd_invbasdoc inv  on man.pk_invbasdoc = inv.pk_invbasdoc  ";
			SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid  ";
			SQL += "where kp.cinventoryid='"
					+ m_cinventoryid
					+ "' and kp.pk_corp='"
					+ getClientEnvironment().getInstance().getCorporation()
							.getPrimaryKey() + "'  and kp.cwarehouseid='" + Wh
					+ "' and (nvl(kp.cspaceid,'')<>'_________N/A________' or kp.cspaceid is null)  ";
			SQL += "group by kp.pk_corp, kp.ccalbodyid, kp.cwarehouseid, kp.cinventoryid, kp.vbatchcode,kp.cspaceid ,kp.vfree1,car.csname   ";
			SQL += " order by  vfree1   ";
			SQL += ")  where num>0  ";
			SQL += "	and vfree1 is not null and rownum <= " + pilecount;
			if (pileo.length() > 0) {
				SQL += " and  vfree1 not in("
						+ pileo.substring(0, pileo.length() - 1) + ")";

			}

			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());
			List list = null;
			try {
				list = (List) sessionManager.executeQuery(SQL,
						new ArrayListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Iterator iterator = list.iterator();
			int newcount = 0;
			while (iterator.hasNext()) {
				newcount++;
				iterator.next();
			}
			if (newcount == 0) {
				MessageDialog.showErrorDlg(this, "其他出库Other a library", Transformations.getLstrFromMuiStr("存货编码@Inventory coding&"
						+ cinventorycode + "&现存量不足!@inadequate existing!"));
			}
			if (newcount < pilecount) {
				MessageDialog.showErrorDlg(
						this,
						"其他出库Other a library",
						Transformations.getLstrFromMuiStr("出库垛数@The number of a library stack&" + String.valueOf(OldCount) + "& 大于现存量@Greater than the existing volume&"
								+ String.valueOf(newcount) + "&垛 !@Pile!"));
				return;
			}
			if (pileo.length() <= 0) {
				newcount--;
			}
			if (Eindex != recordcount - 1) {
				getBillCardPanel().getBodyPanel().insertLine(Eindex + 1,
						newcount);
			} else {
				getBillCardPanel().getBodyPanel().addLine(newcount);
			}
			Iterator viterator = list.iterator();
			int j = pileo.length() > 0 ? Eindex + 1 : Eindex;
			GeneralBillItemVO newbi = voCopyLine(Eindex);
			String m_invcode = String.valueOf(getBillCardPanel()
					.getBodyValueAt(Eindex, "cinventorycode"));
			String m_invid = String.valueOf(getBillCardPanel().getBodyValueAt(
					Eindex, "cinventoryid"));
			String m_invname = String
					.valueOf(((UIRefPane) getBillCardPanel().getBodyItem(
							"cinventorycode").getComponent()).getRefName());

			while (viterator.hasNext()) {
				Object obj = viterator.next();
				ArrayList values = new ArrayList();
				if (obj.getClass().isArray()) {
					int len = Array.getLength(obj);
					for (int i = 0; i < len; i++) {
						values.add(Array.get(obj, i));
					}
					cspaceid = String.valueOf(values.get(1));
					num = new UFDouble(String.valueOf(values.get(2)));
					vbatchcode = String.valueOf(values.get(3));
					pileno = String.valueOf(values.get(4));
					csname = String.valueOf(values.get(5));
					if (j > Eindex) {

						getBillCardPanel().getBillModel()
								.setBodyRowVO(newbi, j);
						BillEditEvent e = new BillEditEvent(getBillCardPanel()
								.getBodyItem("cinventorycode").getComponent(),
								m_invcode, "cinventorycode", j);
						((UIRefPane) getBillCardPanel().getBodyItem(
								"cinventorycode").getComponent())
								.setPK(m_invid);
						((UIRefPane) getBillCardPanel().getBodyItem(
								"cinventorycode").getComponent())
								.setName(m_invname);
						afterEdit(e);
					}
					String key="";
					try {
						 key=this.m_voBill.getChildrenVO()[j].getPrimaryKey();
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						
					}
					if(key!=null&&!key.equals("")&&!key.equals("null"))
					{
						getBillCardPanel().getBillModel().setRowState(j,BillModel.MODIFICATION);
					}
					SetBodyNewValue(pileno, num, vbatchcode, csname, cspaceid,
							j, nshououtnum);
					j++;

				}
			
			}

		}
		flag = false;
	}

	/**
	 * @功能:初始化存货的开始位置，结束位置，扫描过的垛号
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	private void InitInvID() {
		invidinfo = new Hashtable();
		boolean rst = true;
		int recount = 0;
		String old_invid = new String();
		PilenoList = new ArrayList();
		int index = 0;
		for (int i = 0; i < getBillCardPanel().getBillModel().getRowCount(); i++) {
			String m_invid = String.valueOf(getBillCardPanel().getBodyValueAt(
					i, "cinventoryid"));

			String m_Pileno = String.valueOf(getBillCardPanel().getBodyValueAt(
					i, "vfree0"));
			int strIndex = m_Pileno.indexOf(":") + 1;
			int endIndex = m_Pileno.indexOf("]") - 1;

			if (m_Pileno != null && !m_Pileno.equals("")
					&& !m_Pileno.equals("null")) {
				if (PilenoList.indexOf(m_Pileno) < 0) {

					PilenoList.add(m_Pileno.substring(strIndex, endIndex));
				}
			}
			if (old_invid.equals("") || !m_invid.equals(old_invid)) {
				index = i;
				if (recount > 1) {
					recount = 1;
				}
			}

			if (old_invid.equals("") || m_invid.equals(old_invid)) {
				recount++;

			}
			if (!m_invid.equals(old_invid)) {
				if (!invidinfo.containsKey(m_invid)) {
					ArrayList list = new ArrayList();
					// list.add(0, initBillItem[0]);//系统生成单据的存货数据
					list.add(0, m_invid);// 存货编码
					list.add(1, String.valueOf(index));// 新增单据时某个存货的开始索引
					list.add(2, String.valueOf(recount));// 记录存货的行数
					list.add(3, String.valueOf(recount - 1 + index));// 记录当前存货进行跺号处理的索引
					invidinfo.put(m_invid, list);
				}

			}
			if (m_invid.equals(old_invid)) {

				ArrayList list = (ArrayList) invidinfo.get(m_invid);
				int Sindex = Integer.parseInt(String.valueOf(list.get(1)));
				list.set(2, recount);
				list.add(3, String.valueOf(recount - 1 + Sindex));
				invidinfo.put(m_invid, list);
			}
			old_invid = m_invid;
		}

	}

	/**
	 * @功能:同一存货在表体的中出现的次数
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	private int GetInvIDRowRelation(String m_invid) {
		int count = 0;

		if (invidinfo.containsKey(m_invid)) {
			ArrayList li = (ArrayList) invidinfo.get(m_invid);
			count = Integer.parseInt(String.valueOf(li.get(2)));
		}
		return count;
	}

	/**
	 * @功能:同一存货在表体的中的开始位置
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	private int GetStartIndex(String m_invid) {
		int Index = 0;

		if (invidinfo.containsKey(m_invid)) {
			ArrayList li = (ArrayList) invidinfo.get(m_invid);
			Index = Integer.parseInt(String.valueOf(li.get(1)));
		}
		return Index;

	}

	/**
	 * @功能:同一存货在表体的中的结束位置
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	private int GetEndIndex(String m_invid) {
		int Index = 0;

		if (invidinfo.containsKey(m_invid)) {
			ArrayList li = (ArrayList) invidinfo.get(m_invid);
			Index = Integer.parseInt(String.valueOf(li.get(3)));
		}
		return Index;

	}

	/**
	 * @功能:设置自由项、实发数量、批号、货位
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	private void SetBodyNewValue(String pileno, UFDouble num,
			String vbatchcode, String csname, String cspaceid, int sindex,
			UFDouble nshououtnum) {
		getBillCardPanel().setBodyValueAt("[垛号:" + pileno + "]", sindex,
				"vfree0");
		getBillCardPanel().setBodyValueAt(nshououtnum, sindex, "nshouldoutnum");
		nc.vo.scm.ic.bill.FreeVO voFree = new nc.vo.scm.ic.bill.FreeVO();
		voFree.m_vfree0 = "[垛号:" + pileno + "]";
		voFree.m_vfreename1 = "垛号";
		voFree.m_vfree1 = pileno;
		voFree.setVfreeid1("0001A21000000004EIQW");
		m_voBill.setItemFreeVO(sindex, voFree);

		InvVO voInv = (InvVO) getBillCardPanel()
				.getBodyValueAt(sindex, "invvo");
		if (voInv != null) {
			voInv.setFreeItemVO(voFree);
			getBillCardPanel().setBodyValueAt(voInv, sindex, "invvo");
		}
		if(cspaceid!=null&&!cspaceid.equals("")&&!cspaceid.equals("null")&&!cspaceid.equals("_________N/A________"))
		{
	            getBillCardPanel().setBodyValueAt(csname, sindex, "vspacename");
		     getBillCardPanel().setBodyValueAt(cspaceid, sindex, "cspaceid");
		   setSpace(cspaceid, csname, sindex);
		}
		//setSpace(cspaceid, csname, sindex);
		BillEditEvent e4 = new BillEditEvent(getBillCardPanel().getBodyItem(
				"nshouldoutnum").getComponent(), nshououtnum, "nshouldoutnum",
				sindex);
		afterShouldNumEdit(e4);
		
		if(vbatchcode!=null&&!vbatchcode.equals("")&&!vbatchcode.equals("null")&&!vbatchcode.equals("_________N/A________"))
		{
			((LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode")
					.getComponent()).setText(vbatchcode);
			((LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode")
					.getComponent()).setValue(vbatchcode);
			getBillCardPanel().setBodyValueAt(vbatchcode, sindex, "vbatchcode");
		BillEditEvent e1 = new BillEditEvent(getBillCardPanel().getBodyItem(
				"vbatchcode").getComponent(), vbatchcode, "vbatchcode", sindex);
		afterLotEdit(e1);
		}
		getBillCardPanel().setBodyValueAt(num, sindex, "noutnum");
		BillEditEvent e2 = new BillEditEvent(getBillCardPanel().getBodyItem(
				"noutnum").getComponent(), num, "noutnum", sindex);
		afterEdit(e2);

	}

	public void bodyRowChange(BillEditEvent e) {
		super.bodyRowChange(e);

	}

	/**
	 * @功能:设置货位的值
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public void setSpace(String cspaceid, String vspacename, int i) {
		// TODO Auto-generated method stub
		getBillCardPanel().setBodyValueAt(cspaceid, i, "cspaceid");
		getBillCardPanel().setBodyValueAt(vspacename, i, "vspacename");
		LocatorVO voSpace = new LocatorVO();
		LocatorVO[] lvos = new LocatorVO[1];
		lvos[0] = voSpace;
		voSpace.setCspaceid(cspaceid);
		voSpace.setVspacename(vspacename);

		m_alLocatorData.remove(i);
		m_alLocatorData.add(i, lvos);
		UFDouble assistnum = null;
		try {
			assistnum = (UFDouble) getBillCardPanel().getBodyValueAt(i,
					m_sAstItemKey);
		} catch (Exception e4) {
		}
		UFDouble num = null;
		try {
			num = (UFDouble) getBillCardPanel()
					.getBodyValueAt(i, m_sNumItemKey);
		} catch (Exception e2) {
		}
		UFDouble ngrossnum = null;
		try {
			ngrossnum = (UFDouble) getBillCardPanel().getBodyValueAt(i,
					m_sNgrossnum);
		} catch (Exception e3) {
		}

		LocatorVO[] voLoc = (LocatorVO[]) m_alLocatorData.get(i);

		if (voLoc != null && voLoc.length == 1) {

			// 辅数量
			if (assistnum == null) {
				voLoc[0].setNinspaceassistnum(assistnum);
				voLoc[0].setNoutspaceassistnum(assistnum);
			} else {
				if (m_iBillInOutFlag > 0) {
					voLoc[0].setNinspaceassistnum(assistnum);
					voLoc[0].setNoutspaceassistnum(null);
				} else {
					voLoc[0].setNinspaceassistnum(null);
					voLoc[0].setNoutspaceassistnum(assistnum);
				}
			}
			// 数量
			if (num == null) {
				voLoc[0].setNinspacenum(num);
				voLoc[0].setNoutspacenum(num);
				if (m_alSerialData != null)
					m_alSerialData.set(i, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// 入库
					voLoc[0].setNoutspacenum(null);
					voLoc[0].setNinspacenum(num);
				} else {
					// 出库
					voLoc[0].setNinspacenum(null);
					voLoc[0].setNoutspacenum(num);
					if (m_alSerialData != null)
						m_alSerialData.set(i, null);
				}
			}
			// 毛重
			if (ngrossnum == null) {
				voLoc[0].setNingrossnum(ngrossnum);
				voLoc[0].setNoutgrossnum(ngrossnum);
				if (m_alSerialData != null)
					m_alSerialData.set(i, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// 入库
					voLoc[0].setNoutgrossnum(null);
					voLoc[0].setNingrossnum(ngrossnum);
				} else {
					// 出库
					voLoc[0].setNingrossnum(null);
					voLoc[0].setNoutgrossnum(ngrossnum);

				}

			}

		} else
			m_alLocatorData.set(i, null);

	}

	/**
	 * @功能:Copy表体
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	protected GeneralBillItemVO voCopyLine(int row) {
		// int[] row = getBillCardPanel().getBillTable().getSelectedRows();

		GeneralBillItemVO voaBillItem = new GeneralBillItemVO();
		GeneralBillItemVO uicopyvo = null;

		this.m_voBill.getItemVOs()[row]
				.setLocator((LocatorVO[]) m_alLocatorData.get(row));
		voaBillItem = (GeneralBillItemVO) m_voBill.getItemVOs()[row].clone();
		uicopyvo = (GeneralBillItemVO) getBillCardPanel().getBillModel()
				.getBodyValueRowVO(row, GeneralBillItemVO.class.getName());
		uicopyvo = (GeneralBillItemVO) uicopyvo.clone();
		
		String firstbillhid=uicopyvo.getCfirstbillhid();
		String  firstbillbid=uicopyvo.getCfirstbillbid();
		String firstbillcode=uicopyvo.getVfirstbillcode();
		
		String[] keys = uicopyvo.getAttributeNames();
		SmartVOUtilExt.copyVOByVO(voaBillItem, keys, uicopyvo, keys);
		
		if(StringIsNullOrEmpty(voaBillItem.getCfirstbillbid()))
		{
			voaBillItem.setCfirstbillbid(firstbillbid);
		}
		if(StringIsNullOrEmpty(voaBillItem.getCfirstbillhid()))
		{
			voaBillItem.setCfirstbillhid(firstbillhid);
		}
		if(StringIsNullOrEmpty(voaBillItem.getVfirstbillcode()))
		{
			voaBillItem.setVfirstbillcode(firstbillcode);
		}
		// 清除货位、序列号，这些数据是不复制的,和 m_alLoctorData,m_alSerialData保持一致
		// ydy 2004-07-02 货位复制
		// m_voaBillItem[i].setLocator((LocatorVO[])m_alLocatorData.get(row[i]));
		voaBillItem.setSerial(null);
		voaBillItem.setAttributeValue("cparentid", null);
		voaBillItem.setAttributeValue("ncorrespondnum", null);
		voaBillItem.setAttributeValue("ncorrespondastnum", null);

		// 清除条码数据 add by hanwei 2004-04-07
		voaBillItem.setBarCodeVOs(null);
		voaBillItem.setAttributeValue(IItemKey.NBARCODENUM, new UFDouble(0.0));

		voaBillItem.setBarcodeClose(new nc.vo.pub.lang.UFBoolean('N'));

		voaBillItem.setAttributeValue(IItemKey.NKDNUM, null);
		return voaBillItem;
	}

	/**
	 * @功能:初始化变量
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	private void InitGlobalVar() {
		// TODO Auto-generated method stub
		oldInvID = null;
		flag = false;
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
	/**
	 * @功能:判断是否货位仓库
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public boolean Iscsflag(String primkey) 
	{
		boolean rst=false;
		String SQL="select csflag from bd_stordoc  where pk_stordoc ='"+primkey+"'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
		.getInstance().lookup(IUAPQueryBS.class.getName());
List list = null;
try {
	list = (List) sessionManager.executeQuery(SQL,
			new ArrayListProcessor());

	if (list.isEmpty()) {
		return rst;
	}
	else 
	{
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			ArrayList values = new ArrayList();
			Object obj = iterator.next();
			if (obj == null) {
				continue;
			}
			if (obj.getClass().isArray()) {
				int len = Array.getLength(obj);
				for (int j = 0; j < len; j++) {
					values.add(Array.get(obj, j));
				}
				return  rst= (new UFBoolean(String.valueOf(values.get(0)))).booleanValue();
			}
		}
	}
	}catch(BusinessException e)
	{

			// TODO Auto-generated catch block
			e.printStackTrace();
	
	}
		return rst;
	}
	public boolean StringIsNullOrEmpty(Object obj) 
    {
    	return obj==null?true:String.valueOf(obj).equals("")?true:
    		  String.valueOf(obj).equalsIgnoreCase("null")?true:false;
    }

}