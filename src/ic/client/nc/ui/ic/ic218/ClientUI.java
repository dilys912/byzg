package nc.ui.ic.ic218;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import nc.ui.ic.pub.print.PrintDataInterface;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.bill.uicontext.ICBusiCtlTools;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.scm.pub.FactoryLoginDialog;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.redun.RedunSourceDlg;
import nc.ui.to.outer.SettlePathDlgForIC;
import nc.ui.to.service.ITOToIC_QryDLg;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic700.Bill53Const;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.print.PrintConst;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pub.FactoryVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

/**
 * 调拨出库
 * */
public class ClientUI extends GeneralBillClientUI {
	private FactoryLoginDialog m_DlgFactory;
	ITOToIC_QryDLg m_dlgTOQry = null;
	private String m_sCalbodyid;
	private String m_sErr = null;
	ModifyCorpDlg m_dlgModifyCorp;
	SettlePathDlgForIC m_dlgModifySettlePath;
	// 垛号扫描
	/***********************/
	private Hashtable invidinfo;
	private ArrayList PilenoList;
	private String oldInvID = new String();
	private boolean flag;

	/***********************/
	public ClientUI() {
		initialize();
	}

	public ClientUI(String pk_corp, String billType, String businessType, String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);
	}

	protected void afterBillEdit(BillEditEvent e) {
		if (e.getKey().equals("cotherwhid")) {
			String sName = ((UIRefPane) getBillCardPanel().getHeadItem("cotherwhid").getComponent()).getRefName();

			if (this.m_voBill != null) this.m_voBill.setHeaderValue("cotherwhname", sName);
		} else if (getParentCorpCode().equals("10395")) {
			if (e.getKey().equals("StampNo")) {

				if (!String.valueOf(e.getValue()).equals("")) {

					DoSplitData(String.valueOf(e.getValue()), 0);
					getBillCardPanel().getHeadItem("StampNo").setValue("");
					for (int i = 0; i < getBillCardPanel().getBillTable().getRowCount(); i++) {
						getBillCardPanel().setBodyValueAt((i + 1) * 10, i, "crowno");
					}
				}
				getBillCardPanel().getHeadItem("StampNo").getComponent().requestFocusInWindow();
			} else if (e.getKey().equals("StampCount")) {
				if (!String.valueOf(e.getValue()).equals("")) {
					DoSplitData("", Integer.parseInt(e.getValue().toString()));
					getBillCardPanel().getHeadItem("StampCount").setValue("");
					for (int i = 0; i < getBillCardPanel().getBillTable().getRowCount(); i++) {
						getBillCardPanel().setBodyValueAt((i + 1) * 10, i, "crowno");
					}
				}
			} else if (e.getKey().equals("cwarehouseid") || e.getKey().equals("cinventorycode")) {
				if (flag) { return; }
				String cwarehouseid = ((UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid").getComponent()).getRefPK();

				if (cwarehouseid == null || cwarehouseid.equals("")) { return; }
				String sCode = ((UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid").getComponent()).getRefCode();
				/**
				 * @功能:根据存货、仓库带出默认货位
				 * @author ：林桂莹
				 * @2012/9/5
				 * 
				 * @since v50
				 */
				if (Iscsflag(cwarehouseid))// ||sCode.equals(arg0))
				{

					for (int i = 0; i < getBillCardPanel().getBillTable().getRowCount(); i++) {

						String cinventoryid = (String) getBillCardPanel().getBodyValueAt(i, "cinventoryid");
						if (cinventoryid == null || cinventoryid.equals("")) {

						return; }

						String Sql = "select * from (select d.pk_cargdoc,d.csname,d.cscode from ic_general_h a  ";
						Sql += "left join ic_general_b b on a.cgeneralhid=b.cgeneralhid  ";
						Sql += "left join ic_general_bb1 c on c.cgeneralbid=b.cgeneralbid  ";
						Sql += "left join bd_cargdoc d on c.cspaceid=d.pk_cargdoc  ";
						Sql += "where a.cbilltypecode ='4Y' and a.cwarehouseid='" + cwarehouseid + "' and  b.cinventoryid='" + cinventoryid + "'  ";
						Sql += "and d.pk_cargdoc is not null  and a.taccounttime is not null and nvl(b.dr,0)=0 order by a.taccounttime desc  ";
						Sql += ") where rownum=1  ";
						IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
						List list = null;
						try {
							list = (List) sessionManager.executeQuery(Sql, new ArrayListProcessor());
							if (!list.isEmpty()) {
								if (list.isEmpty()) {

									Sql = "select * from (select kp.cspaceid ,car.csname,car.cscode   ";
									Sql += "from   v_ic_onhandnum6 kp  ";
									Sql += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
									Sql += "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null and kp.cwarehouseid='" + cwarehouseid + "'  and  kp.cinventoryid='" + cinventoryid + "')  ";
									Sql += "where rownum=1  ";
									list = (List) sessionManager.executeQuery(Sql, new ArrayListProcessor());
									if (list.isEmpty()) { return; }
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
							setSpace(String.valueOf(values.get(0)), String.valueOf(values.get(1)), i);
						}
					}

				}
			}
		}
	}

	public void onAudit() {
		qryLocSN(this.m_iLastSelListHeadRow, 3);
		super.onAudit();
	}

	protected void afterBillItemSelChg(int iRow, int iCol) {
	}

	private void afterVoLoaded() {
		String[] keys = {
				"noutnum", "noutassistnum", "nshouldoutassistnum", "nshouldoutnum", "nmny"
		};

		if ((this.m_voBill != null) && (this.m_voBill.getHeaderVO() != null) && (this.m_voBill.getHeaderVO().getFreplenishflag().booleanValue())) {
			GeneralBillUICtl.setValueRange(getBillCardPanel(), keys, -10000000000000000.0D, 0.0D);
		} else GeneralBillUICtl.setValueRange(getBillCardPanel(), keys, 0.0D, 1.7976931348623157E+308D);

		if (getBillCardPanel().getHeadItem("alloctypename") != null) {
			getBillCardPanel().getHeadItem("alloctypename").setEnabled(false);
		}
		if (getBillCardPanel().getHeadItem("freplenishflag") != null) {
			getBillCardPanel().getHeadItem("freplenishflag").setEnabled(false);
		}

		calcSpace(this.m_voBill);
	}

	public boolean beforeBillItemEdit(BillEditEvent e) {
		if (e.getKey() == this.m_sMainCalbodyItemKey) {
			getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey).setEnabled(false);
		}
		return false;
	}

	protected void beforeBillItemSelChg(int iRow, int iCol) {
	}

	public String checkPrerequisite() {
		return this.m_sErr;
	}

	protected boolean checkVO(GeneralBillVO voBill) {
		try {
			boolean bCheck = true;
			bCheck = super.checkVO();

			return bCheck;
		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
		return false;
	}

	private ITOToIC_QryDLg createDlgQuery() {
		ITOToIC_QryDLg dlgQry = null;
		try {
			dlgQry = (ITOToIC_QryDLg) Class.forName("nc.ui.to.outer.QRYOrderDlg").newInstance();
		} catch (Exception e) {
		}

		return dlgQry;
	}

	public void filterRef(String sCorpID) {
		try {
			super.filterRef(sCorpID);

			String[] sConstraint = null;
			if (getCalbodyid() != null) {
				sConstraint = new String[1];
				sConstraint[0] = (" AND pk_calbody='" + getCalbodyid() + "'");
			}
			BillItem bi = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey);
			RefFilter.filtWh(bi, this.m_sCorpID, sConstraint);
		} catch (Exception e) {
			SCMEnv.error(e);
		}
	}

	public String getCalbodyid() {
		if ((this.m_sCalbodyid == null) && (isStartFromTO())) {
			FactoryVO vo = (FactoryVO) ClientEnvironment.getInstance().getValue("TO_CALBODY," + this.m_sCorpID + "," + this.m_sUserID);
			if (vo != null) {
				this.m_sCalbodyid = vo.getPrimaryKey();
			}
		}
		return this.m_sCalbodyid;
	}

	protected QueryConditionDlgForBill getConditionDlg() {
		if (this.ivjQueryConditionDlg == null) {
			this.ivjQueryConditionDlg = super.getConditionDlg();
			this.ivjQueryConditionDlg.setCombox("body.cfirsttype", new String[][] {
					{
							"", ""
					}, {
							"5C", NCLangRes.getInstance().getStrByID("40080618", "UPT40080618-000006")
					}, {
							"5D", NCLangRes.getInstance().getStrByID("40080618", "UPT40080618-000007")
					}, {
							"5E", NCLangRes.getInstance().getStrByID("40080618", "UPT40080618-000008")
					}, {
							"5I", NCLangRes.getInstance().getStrByID("40080618", "UPT40080618-000009")
					}
			});

			this.ivjQueryConditionDlg.setRefInitWhereClause("head.coutcalbodyid", "库存组织", "pk_corp=", "head.coutcorpid");

			this.ivjQueryConditionDlg.setRefInitWhereClause("head.cothercalbodyid", "库存组织", "pk_corp=", "head.cothercorpid");

			this.ivjQueryConditionDlg.setRefInitWhereClause("head.cotherwhid", "仓库档案", "bd_stordoc.pk_calbody=", "head.cothercalbodyid");

			this.ivjQueryConditionDlg.setCorpRefs("head.pk_corp", nc.ui.ic.pub.tools.GenMethod.getDataPowerFieldFromDlg(this.ivjQueryConditionDlg, false, new String[] {
					"head.cothercorpid", "head.coutcorpid", "body.creceieveid", "head.cothercalbodyid", "head.cotherwhid", "head.coutcalbodyid"
			}));

			this.ivjQueryConditionDlg.setCorpRefs("head.cothercorpid", new String[] {
					"head.cothercalbodyid", "head.cotherwhid", "body.creceieveid"
			});
			this.ivjQueryConditionDlg.setCorpRefs("head.coutcorpid", new String[] {
				"head.coutcalbodyid"
			});

			String[] otherfieldcodes = {
					"body.creceieveid", "head.cothercalbodyid", "head.cotherwhid", "head.coutcalbodyid"
			};
			nc.ui.ic.pub.tools.GenMethod.setDataPowerFlag(this.ivjQueryConditionDlg, false, otherfieldcodes);
			UIRefPane ref = null;
			for (int i = 0; i < otherfieldcodes.length; i++) {
				ref = this.ivjQueryConditionDlg.getRefPaneByNodeCode(otherfieldcodes[i]);
				if ((ref != null) && (ref.getRefModel() != null)) ref.getRefModel().setUseDataPower(false);
			}
		}
		return this.ivjQueryConditionDlg;
	}

	protected void getConDlginitself(QueryConditionDlgForBill queryCondition) {
		if (this.m_sCalbodyid != null) queryCondition.setRefInitWhereClause("head.pk_calbody", "库存组织", "pk_calbody='" + this.m_sCalbodyid + "' and pk_corp=", "pk_corp");
	}

	private FactoryLoginDialog getDlgFactory() {
		if (this.m_DlgFactory == null) this.m_DlgFactory = new FactoryLoginDialog(this, NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000294"));
		return this.m_DlgFactory;
	}

	private ITOToIC_QryDLg getTODlgQry() {
		if (this.m_dlgTOQry == null) {
			this.m_dlgTOQry = createDlgQuery();
		}
		return this.m_dlgTOQry;
	}

	public String getExtendQryCond(ConditionVO[] voaCond) {
		if (voaCond != null) {
			String sFieldCode = null;
			String sField = null;
			for (int i = 0; i < voaCond.length; i++) {
				if ((voaCond[i] == null) || (voaCond[i].getFieldCode() == null)) continue;
				sFieldCode = voaCond[i].getFieldCode().trim();

				if (sFieldCode.startsWith("order.")) {
					sField = sFieldCode.substring(sFieldCode.indexOf(".") + 1);
					nc.vo.ic.pub.GenMethod.setCondIn(voaCond[i], " select cbillid from to_bill where dr=0 ", "body.cfirstbillhid", sField);
				} else if (sFieldCode.startsWith("orderbody.")) {
					sField = sFieldCode.substring(sFieldCode.indexOf(".") + 1);
					nc.vo.ic.pub.GenMethod.setCondIn(voaCond[i], " select cbill_bid from to_bill_b where dr=0 ", "body.cfirstbillbid", sField);
				}

			}

		}

		if (this.m_sCalbodyid != null) { return super.getExtendQryCond(voaCond) + " and pk_calbody ='" + this.m_sCalbodyid + "' "; }
		return super.getExtendQryCond(voaCond);
	}

	protected ArrayList getFormulaItemHeader() {
		ArrayList arylistItemField = super.getFormulaItemHeader();

		arylistItemField.add(new String[] {
				"bodyname", "coutbodyname", "coutcalbodyid"
		});
		arylistItemField.add(new String[] {
				"bodyname", "cotherbodyname", "cothercalbodyid"
		});
		arylistItemField.add(new String[] {
				"unitname", "cotherunitname", "cothercorpid"
		});
		arylistItemField.add(new String[] {
				"unitname", "coutunitname", "coutcorpid"
		});

		return arylistItemField;
	}

	public String getTitle() {
		return this.m_sTitle;
	}

	public void initialize() {
		this.m_sCorpID = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		this.m_sUserID = ClientEnvironment.getInstance().getUser().getPrimaryKey();

		if ((isStartFromTO()) && (getCalbodyid() == null)) {
			getDlgFactory().showModal();
			if (getCalbodyid() == null) {
				this.m_sErr = NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000295");
				return;
			}
		}

		super.initialize();

		if ((getCalbodyid() != null) && (getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey) != null)) {
			getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey).setEnabled(false);
			getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey).setEdit(false);
		}

		if (getBillCardPanel().getHeadItem("cotherwhid") != null) {
			UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem("cotherwhid").getComponent();
			if (ref != null) {
				ref.getRefModel().setUseDataPower(false);
			}

		}

		String[] hidekey = {
				"nleftnum", "nleftastnum"
		};
		for (int i = 0; i < hidekey.length; i++) {
			try {
				getBillCardPanel().getBodyPanel().hideTableCol(hidekey[i]);
			} catch (Exception e) {
				SCMEnv.out(e.getMessage());
			}
		}

		for (int i = 0; i < hidekey.length; i++)
			try {
				getBillListPanel().getChildListPanel().hideTableCol(hidekey[i]);
			} catch (Exception e) {
				SCMEnv.out(e.getMessage());
			}

		InitGlobalVar();
	}

	protected void initPanel() {
		super.setBillInOutFlag(-1);

		super.setNeedBillRef(false);

		this.m_sBillTypeCode = BillTypeConst.m_allocationOut;

		this.m_sCurrentBillNode = "40080820";
	}

	private boolean isStartFromTO() {
		return false;
	}

	private void onAdd5C() {
		onAddToOrder("40093010", "40099901", "5C", getTODlgQry());
	}

	private void onAdd5D() {
		onAddToOrder("40093020", "40099902", "5D", getTODlgQry());
	}

	private void onAdd5E() {
		onAddToOrder("40093030", "40099903", "5E", getTODlgQry());
	}

	private void onAdd5I() {
		onAddToOrder("40093031", "40099904", "5I", getTODlgQry());
	}

	private void onAddSelf() {
		onAdd();
	}

	private void onAddToOrder(String funnode, String qrynodekey, String sourcetype, ITOToIC_QryDLg dlgQry) {
		if (dlgQry == null) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000293"));
			return;
		}

		AggregatedValueObject[] vos = dlgQry.getReturnVOs(this.m_sCorpID, this.m_sUserID, sourcetype, this.m_sBillTypeCode, funnode, qrynodekey, this);

		if (vos == null) { return; }
		try {
			AggregatedValueObject[] voRetvos = (AggregatedValueObject[]) PfChangeBO_Client.pfChangeBillToBillArray(vos, sourcetype, this.m_sBillTypeCode);

			setBillRefResultVO(null, voRetvos);
			afterVoLoaded();
		} catch (Exception e) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000297") + e.getMessage());
		}
	}

	public void onButtonClicked(ButtonObject bo) {
		if (bo.getCode().equals("onHandQuery")) {
			onHandnumQuery();
		} else if (bo.getCode().equals("onMergerQuery")) {//add by cm 2012-11-12
			onMergerQueryPrint();
			//			onPrintSumRow();
		} else if (bo == getButtonTree().getButton("自制")) {
			onAddSelf();
		} else if (bo == getButtonTree().getButton("三方调拨订单")) {
			bo.setTag("5C:0001AA100000000001ZC");
			RedunSourceDlg.childButtonClicked(bo, this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);

			if (RedunSourceDlg.isCloseOK()) {
				AggregatedValueObject[] vos = RedunSourceDlg.getRetsVos();

				onAddToOrder(vos);
			}

		} else if (bo == getButtonTree().getButton("公司间调拨订单")) {
			bo.setTag("5D:0001AA100000000001ZC");
			RedunSourceDlg.childButtonClicked(bo, this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);

			if (RedunSourceDlg.isCloseOK()) {
				AggregatedValueObject[] vos = RedunSourceDlg.getRetsVos();

				onAddToOrder(vos);
			}

		} else if (bo == getButtonTree().getButton("组织间调拨订单")) {
			bo.setTag("5E:0001AA100000000001ZC");
			RedunSourceDlg.childButtonClicked(bo, this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);

			if (RedunSourceDlg.isCloseOK()) {
				AggregatedValueObject[] vos = RedunSourceDlg.getRetsVos();

				onAddToOrder(vos);
			}

		} else if (bo == getButtonTree().getButton("组织内调拨订单")) {
			bo.setTag("5I:0001AA100000000001ZC");
			RedunSourceDlg.childButtonClicked(bo, this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);

			if (RedunSourceDlg.isCloseOK()) {
				AggregatedValueObject[] vos = RedunSourceDlg.getRetsVos();

				onAddToOrder(vos);
				for (int i = 0; i < getBillCardPanel().getBillTable().getRowCount(); i++) {
					BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem("cinventorycode").getComponent(), null, "cinventorycode", i);

					afterBillEdit(e);
				}
			}

		} else if (bo == getButtonTree().getButton("指定结算路径")) {
			onModifySettlePath();
		} else {
			super.onButtonClicked(bo);
			InitGlobalVar();
		}
	}

	private void onModifyOutCorp() {
		try {
			ArrayList alSelected = getSelectedBills();
			if ((alSelected == null) || (alSelected.size() == 0)) return;
			if (this.m_dlgModifyCorp == null) {
				this.m_dlgModifyCorp = new ModifyCorpDlg(this.m_sCorpID);
			}

			this.m_dlgModifyCorp.showModal();
			if (this.m_dlgModifyCorp.getResult() != 1) return;
			ArrayList alparam = this.m_dlgModifyCorp.getResValue();
			ArrayList alErrorCode = new ArrayList();

			ArrayList alSameCorpBillCode = new ArrayList();

			String sAppointedCorp = alparam.get(0).toString();
			GeneralBillHeaderVO headVO = null;

			for (int i = 0; i < alSelected.size(); i++) {
				GeneralBillVO rowVO = (GeneralBillVO) alSelected.get(i);
				headVO = rowVO.getHeaderVO();

				headVO.setCoperatoridnow(this.m_sUserID);
				headVO.setAttributeValue("clogdatenow", this.m_sLogDate);

				if ((headVO.getFallocflag().intValue() != 0) || ((headVO.getFbillflag().intValue() != 3) && (headVO.getFbillflag().intValue() != 4))) {
					alErrorCode.add(headVO.getVbillcode());
					alSelected.remove(i);
					i -= 1;
				} else if (sAppointedCorp.equals(headVO.getAttributeValue("cothercorpid"))) {
					alSameCorpBillCode.add(headVO.getVbillcode());
					alSelected.remove(i);
					i -= 1;
				}

			}

			ArrayList alResult = null;
			ArrayList alNOin = null;
			ArrayList alInvNotInOrg = null;

			if ((alSelected != null) && (alSelected.size() > 0)) {
				GeneralBillVO[] selVOs = new GeneralBillVO[alSelected.size()];
				selVOs = (GeneralBillVO[]) (GeneralBillVO[]) alSelected.toArray(selVOs);

				alResult = GeneralHHelper.modifyOutCorp(selVOs, alparam);

				alNOin = (ArrayList) alResult.get(0);
				Object oFreshUIInfo = alResult.get(1);
				alInvNotInOrg = (ArrayList) alResult.get(2);

				if (oFreshUIInfo != null) {
					SMGeneralBillVO[] voResult = null;
					voResult = (SMGeneralBillVO[]) (SMGeneralBillVO[]) oFreshUIInfo;
					String[] keys = {
							"ts", "coutcorpid", "coutunitname", "coutcalbodyid", "coutbodyname"
					};
					refreshHeadValue(voResult, keys);
				}

			}

			StringBuffer sbHintMessage = new StringBuffer();
			if (alErrorCode.size() > 0) {
				sbHintMessage.append(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000374")).append("\n").append(alErrorCode.toString());
			}

			if (alSameCorpBillCode.size() > 0) {
				sbHintMessage.append(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000389")).append("\n").append(alSameCorpBillCode.toString());
			}

			if ((alNOin != null) && (alNOin.size() > 0)) {
				if (sbHintMessage.length() > 0) sbHintMessage.append("\n");
				sbHintMessage.append(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000387")).append("\n").append(alNOin.toString());
			}

			if ((alInvNotInOrg != null) && (alInvNotInOrg.size() > 0)) {
				if (sbHintMessage.length() > 0) sbHintMessage.append("\n");
				sbHintMessage.append(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000388")).append("\n").append(alInvNotInOrg.toString());
			}

			if (sbHintMessage.length() != 0) MessageDialog.showHintDlg(this, null, sbHintMessage.toString());
			else {
				showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000376"));
			}
		} catch (Exception e) {
			SCMEnv.error(e);
			showHintMessage(e.getMessage());
		}
	}

	private void onModifySettlePath() {
		try {
			if (0 == this.m_iMode) {
				showHintMessage("请单据保存之后再指定结算！");
				return;
			}

			ArrayList alSelected = getSelectedBills();
			if ((alSelected == null) || (alSelected.size() != 1)) {
				showHintMessage("请选择一张单据！");
				return;
			}

			int iselrow = getBillListPanel().getHeadTable().getSelectedRow();
			GeneralBillVO voBill = (GeneralBillVO) alSelected.get(0);

			if (voBill.getHeaderVO().getCgeneralhid() == null) {
				showHintMessage("请单据保存之后再指定结算！");
				return;
			}

			GeneralBillItemVO[] voItems = voBill.getItemVOs();
			UFDouble ufd = null;

			for (int i = 0; i < voItems.length; i++) {
				ufd = (UFDouble) voItems[i].getAttributeValue("nsettlenum1");

				if ((ufd != null) && (ufd.doubleValue() != 0.0D)) { throw new BusinessException("已经做过调入调出结算，不能再指定结算路径！"); }
				if ("5D".equals(voItems[i].getCfirsttype())) continue;
				throw new BusinessException("只有公司间调拨定单可以指定结算路径！");
			}

			SettlePathDlgForIC dlgModifySettlePath = new SettlePathDlgForIC((String) voBill.getHeaderVO().getAttributeValue("cothercorpid"), this.m_sCorpID, this, "指定结算路径");

			if (dlgModifySettlePath == null) { return; }

			dlgModifySettlePath.showModal();
			if (dlgModifySettlePath.getResult() != 1) { return; }

			String cpathid = dlgModifySettlePath.getSelectedSettlePathID();

			if (cpathid == null) {
				SCMEnv.out("路径空！");
				return;
			}
			voBill.getHeaderVO().setCoperatoridnow(this.m_sUserID);
			GeneralBillVO voRet = GeneralHHelper.modifySettlePath(voBill, cpathid);
			if ((voRet != null) && (voRet.getHeaderVO() != null)) {
				voRet.getHeaderVO().setAttributeValue("csettlepathid", cpathid);
				String[] keys = {
						"ts", "csettlepathid", "cpathname"
				};
				refreshHeadValue(new GeneralBillVO[] {
					voRet
				}, keys);
				selectListBill(iselrow);
				showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000376"));
			}

		} catch (Exception e) {
			SCMEnv.error(e);
			showErrorMessage(e.getMessage());
		}
	}

	private void refreshHeadValue(AggregatedValueObject[] smallvos, String[] keys) {
		if (smallvos == null) return;
		HashMap htsmall = new HashMap();

		for (int i = 0; i < smallvos.length; i++) {
			htsmall.put(smallvos[i].getParentVO().getAttributeValue("cgeneralhid"), smallvos[i]);
		}

		if (this.m_alListData != null) {
			for (int i = 0; i < this.m_alListData.size(); i++) {
				GeneralBillVO voBill = (GeneralBillVO) this.m_alListData.get(i);
				String hid = voBill.getHeaderVO().getCgeneralhid();
				if (htsmall.containsKey(hid)) {
					AggregatedValueObject svo = (AggregatedValueObject) htsmall.get(hid);

					for (int j = 0; j < keys.length; j++) {
						voBill.getHeaderVO().setAttributeValue(keys[j], svo.getParentVO().getAttributeValue(keys[j]));
					}

					if ((this.m_voBill == null) || (this.m_voBill.getHeaderVO() == null) || (!this.m_voBill.getHeaderVO().getCgeneralhid().equals(hid))) continue;
					getBillCardPanel().getHeadItem("ts").setValue(svo.getParentVO().getAttributeValue("ts"));
					for (int j = 0; j < keys.length; j++) {
						this.m_voBill.getHeaderVO().setAttributeValue(keys[j], svo.getParentVO().getAttributeValue(keys[j]));
					}
					for (int j = 0; j < keys.length; j++) {
						if (getBillCardPanel().getHeadItem(keys[j]) != null) {
							getBillCardPanel().getHeadItem(keys[j]).setValue(svo.getParentVO().getAttributeValue(keys[j]));
						}

					}

				}

			}

			GeneralBillHeaderVO[] voh = new GeneralBillHeaderVO[this.m_alListData.size()];
			for (int i = 0; i < this.m_alListData.size(); i++) {
				if (this.m_alListData.get(i) != null) {
					voh[i] = ((GeneralBillHeaderVO) ((GeneralBillVO) this.m_alListData.get(i)).getParentVO());
				}

			}

			setListHeadData(voh);
		}
	}

	public void onUpdate() {
		super.onUpdate();
		afterVoLoaded();
	}

	protected void selectBillOnListPanel(int iBillIndex) {
	}

	protected void setButtonsStatus(int iBillMode) {
	}

	public void setCalbodyid(String newCalbodyid) {
		this.m_sCalbodyid = newCalbodyid;
	}

	protected void setNewBillInitData() {
		super.setNewBillInitData();
		try {
			if ((getBillCardPanel().getHeadItem(this.m_sMainCalbodyItemKey) != null) && (getCalbodyid() != null)) {
				getBillCardPanel().setHeadItem(this.m_sMainCalbodyItemKey, getCalbodyid());
			}

		} catch (Exception e) {
			SCMEnv.error(e);
		}
	}

	protected String freshStatusTs(String sBillPK) throws Exception {
		String sBillStatus = null;

		String sQryWhere = " head.cgeneralhid ='" + this.m_voBill.getPrimaryKey() + "'  ";
		QryConditionVO voQryCond = new QryConditionVO(sQryWhere);
		voQryCond.setIntParam(0, 500);

		GeneralBillVO voRetBill = (GeneralBillVO) GeneralBillHelper.queryBills(this.m_sBillTypeCode, voQryCond).get(0);

		this.m_alListData.remove(this.m_iLastSelListHeadRow);
		this.m_alListData.add(this.m_iLastSelListHeadRow, voRetBill);
		this.m_voBill = ((GeneralBillVO) this.m_alListData.get(this.m_iLastSelListHeadRow));

		ArrayList altemp = new ArrayList();
		altemp.add(this.m_voBill);
		setAlistDataByFormula(1, altemp);

		setBillVO(this.m_voBill);

		GeneralBillHeaderVO[] voaHeader = new GeneralBillHeaderVO[this.m_alListData.size()];
		for (int i = 0; i < this.m_alListData.size(); i++) {
			if (this.m_alListData.get(i) != null) {
				voaHeader[i] = ((GeneralBillHeaderVO) ((GeneralBillVO) this.m_alListData.get(i)).getParentVO());
				if ((voaHeader[i].getCgeneralhid() != null) && (voaHeader[i].getCgeneralhid().equals(sBillPK)) && (voaHeader[i].getFbillflag() != null)) sBillStatus = voaHeader[i].getFbillflag().toString();
			} else {
				SCMEnv.out("list data error!-->" + i);
				sBillStatus = "1";
			}
		}

		setListHeadData(voaHeader);
		selectListBill(this.m_iLastSelListHeadRow);

		return sBillStatus;
	}

	private void onAddToOrder(AggregatedValueObject[] vos) {
		if (vos == null) { return; }
		// add by zip: 2013/12/18
		for (int i = 0; i < vos.length; i++) {
			CircularlyAccessibleValueObject[] childVOs = vos[i].getChildrenVO();
			for (int j = 0; j < childVOs.length; j++) {
				if (childVOs[j].getAttributeValue("dbizdate") == null) {
					childVOs[j].setAttributeValue("dbizdate", ClientEnvironment.getInstance().getDate());
				}
			}
		}
		// end
		try {
			if ((vos != null) && (vos.length == 1)) {
				setRefBillsFlag(false);
				setBillRefResultVO(null, vos);
			} else {
				setRefBillsFlag(true);
				setBillRefMultiVOs(null, (GeneralBillVO[]) (GeneralBillVO[]) vos);
			}
		} catch (Exception e) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000297") + e.getMessage());
		}
	}

	public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton, boolean bExeFormule) {
		super.setBillVO(bvo, bUpdateBotton, bExeFormule);
		if (ICBusiCtlTools.isStringNull(bvo.getHeaderVO().getCgeneralhid())) afterVoLoaded();
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
		Wh = ((UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid").getComponent()).getRefPK();
		if (Wh == null || Wh.equals("") || Wh.equals("null")) {
			MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", "仓库不能为空!warehouse can not be empty!");
			return;
		}
		if ((pilecount == 0) && !pileno.equals("")) {

			if (PilenoList != null || PilenoList.toArray().length > 0) {
				if (PilenoList.indexOf(pileno) >= 0) {
					MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", Transformations.getLstrFromMuiStr("跺号@Stamp&:" + pileno

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
			SQL += "where kp.vfree1='" + pileno + "' and kp.pk_corp='" + getClientEnvironment().getInstance().getCorporation().getPrimaryKey() + "' and kp.cwarehouseid='" + Wh + "'  ";
			SQL += "group by kp.pk_corp, kp.ccalbodyid, kp.cwarehouseid, kp.cinventoryid, kp.vbatchcode,kp.cspaceid ,kp.vfree1,car.csname ";
			SQL += ")  where num>0 ";

			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List list = null;
			try {
				list = (List) sessionManager.executeQuery(SQL, new ArrayListProcessor());
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
				MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", Transformations.getLstrFromMuiStr("跺号@Stamp&:" + pileno + "&不存在!@Does not exist!"));
				return;
			} else if (invidinfo != null) {
				if (!invidinfo.containsKey(cinventoryid)) {
					MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", Transformations.getLstrFromMuiStr("当前垛号@The current stack number&:" + pileno + "&对应的存货不属于本次出库范围!@corresponding to the inventory does not belong to the scope of delivery!"));
					return;
				}

			}
			int Sindex = GetStartIndex(cinventoryid);
			String cinventorycode = String.valueOf(getBillCardPanel().getBodyValueAt(Sindex, "cinventorycode"));
			StampCount = new UFDouble("0");
			if (cinventorycode.substring(0, 2).equals("23")) {
				String sc = String.valueOf(getBillCardPanel().getHeadItem("vuserdef11").getValueObject());
				if (sc == null || sc.equals("") || sc.equals("null")) {
					MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", "整垛数量不能为空!The entire stack Quantity can not be empty!");
					return;
				}
				int strIndex = sc.indexOf(".");
				StampCount = strIndex >= 0 ? new UFDouble(sc.substring(0, strIndex)) : new UFDouble(sc);
				if (num.compareTo(StampCount) > 0) {
					if (MessageDialog.showYesNoDlg(this, "调拨出库Allocate a library", "当前垛号的整垛数量已超过该客户所要求的整垛数量，可联系销售人员修改收货单位整垛数量或重新下达订单,是否继续？Entire stack number of the current stack the entire stack more than the number required by the customers can contact the sales staff to modify the entire stack number of the receiving unit, or place orders again, whether to continue?") == 8) return;
				}
			}

			int row = GetInvIDRowRelation(cinventoryid);
			int recordcount = getBillCardPanel().getBillTable().getRowCount();

			int Eindex = GetEndIndex(cinventoryid);
			UFDouble nshououtnum = (UFDouble) getBillCardPanel().getBodyValueAt(Sindex, "nshouldoutnum");
			String oldpileno = String.valueOf(getBillCardPanel().getBodyValueAt(Eindex, "vfree0"));
			if ((oldpileno == null) || (oldpileno.equals("")) || (oldpileno.equals("null"))) {
				SetBodyNewValue(pileno, num, vbatchcode, csname, cspaceid, Eindex, nshououtnum);
				String key = "";
				try {
					key = this.m_voBill.getChildrenVO()[Eindex].getPrimaryKey();
				} catch (BusinessException e) {
					// TODO Auto-generated catch block

				}
				if (key != null && !key.equals("") && !key.equals("null")) {
					getBillCardPanel().getBillModel().setRowState(Eindex, BillModel.MODIFICATION);
				}
			} else if ((oldpileno != null) && (!oldpileno.equals("")) && (!oldpileno.equals("null"))) {
				if (Eindex != recordcount - 1) {
					getBillCardPanel().getBodyPanel().insertLine(Eindex + 1, 1);
				} else {
					getBillCardPanel().getBodyPanel().addLine(1);
				}

				GeneralBillItemVO newbi = voCopyLine(Eindex);

				getBillCardPanel().getBillModel().setBodyRowVO(newbi, Eindex + 1);

				String m_invcode = String.valueOf(getBillCardPanel().getBodyValueAt(Eindex, "cinventorycode"));
				String m_invid = String.valueOf(getBillCardPanel().getBodyValueAt(Eindex, "cinventoryid"));
				String m_invname = String.valueOf(((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).getRefName());
				getBillCardPanel().getBillModel().setBodyRowVO(newbi, Eindex + 1);

				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setPK(m_invid);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setName(m_invname);

				BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem("cinventorycode").getComponent(), m_invcode, "cinventorycode", Eindex + 1);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setPK(m_invid);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setName(m_invname);
				afterEdit(e);
				SetBodyNewValue(pileno, num, vbatchcode, csname, cspaceid, Eindex + 1, nshououtnum);
			}

		} else if ((pilecount > 0) && pileno.equals("")) {
			int OldCount = pilecount;
			String m_cinventoryid = new String();

			int selRow = getBillCardPanel().getBillTable().getSelectedRow();

			int recordcount = getBillCardPanel().getBillTable().getRowCount();
			if (selRow < -1 && (oldInvID == null || oldInvID.equals("") || oldInvID.equals("null"))) {
				MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", "请选择数据!Please select the data!");
				return;
			} else if (recordcount == 1) {
				m_cinventoryid = String.valueOf(getBillCardPanel().getBodyValueAt(0, "cinventoryid"));
			} else {
				m_cinventoryid = String.valueOf(getBillCardPanel().getBodyValueAt(selRow, "cinventoryid"));
			}
			if (m_cinventoryid == null || m_cinventoryid.equals("") || m_cinventoryid.equals("null")) {
				m_cinventoryid = oldInvID;
			} else {

				oldInvID = m_cinventoryid;
			}
			String pileo = new String();

			int row = GetInvIDRowRelation(m_cinventoryid);
			int Sindex = GetStartIndex(m_cinventoryid);
			int Eindex = GetEndIndex(m_cinventoryid);
			UFDouble nshououtnum = (UFDouble) getBillCardPanel().getBodyValueAt(Sindex, "nshouldoutnum");
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
				MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", Transformations.getLstrFromMuiStr("当前出库垛数已等于@The library stack number is equal to current&" + String.valueOf(pilecount) + "!"));
				return;
			}
			for (int i = Sindex; i <= Eindex; i++) {
				String n_cinventoryid = String.valueOf(getBillCardPanel().getBodyValueAt(i, "cinventoryid"));
				if (m_cinventoryid.equals(n_cinventoryid)) {

					String temp = String.valueOf(getBillCardPanel().getBodyValueAt(i, "vfree0"));
					int srtIndex = temp.indexOf(":");
					int endIndex = temp.indexOf("]");
					if (temp != null && !temp.equals("") && !temp.equals("null")) {
						pileo += "'" + temp.substring(srtIndex + 1, endIndex) + "',";
					}
				}

			}
			if (pileo.length() > 0) {
				pilecount = pilecount - row;
			}
			String cinventorycode = String.valueOf(getBillCardPanel().getBodyValueAt(Sindex, "cinventorycode"));
			StampCount = new UFDouble("0");
			if (cinventorycode.substring(0, 2).equals("23")) {
				String sc = String.valueOf(getBillCardPanel().getHeadItem("vuserdef11").getValueObject());
				if (sc == null || sc.equals("") || sc.equals("null")) {
					MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", "整垛数量不能为空!The entire stack Quantity can not be empty!");
				}
				int strIndex = sc.indexOf(".");
				StampCount = strIndex >= 0 ? new UFDouble(sc.substring(0, strIndex)) : new UFDouble(sc);
				ischekcount = true;
			}
			String SQL = "select * from (  ";
			SQL += "select  kp.cinventoryid, kp.cspaceid, SUM ( nvl( ninspacenum, 0.0 ) ) - SUM ( nvl( noutspacenum, 0.0 ) ) num,kp.vbatchcode,kp.vfree1,car.csname   ";
			SQL += "from v_ic_onhandnum6 kp  ";
			SQL += "left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc   ";
			SQL += "left join bd_invbasdoc inv  on man.pk_invbasdoc = inv.pk_invbasdoc  ";
			SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid  ";
			SQL += "where kp.cinventoryid='" + m_cinventoryid + "' and kp.pk_corp='" + getClientEnvironment().getInstance().getCorporation().getPrimaryKey() + "'  and kp.cwarehouseid='" + Wh + "' and (nvl(kp.cspaceid,'')<>'_________N/A________' or kp.cspaceid is null)  ";
			SQL += "group by kp.pk_corp, kp.ccalbodyid, kp.cwarehouseid, kp.cinventoryid, kp.vbatchcode,kp.cspaceid ,kp.vfree1,car.csname   ";
			SQL += " order by  vfree1   ";
			SQL += ")  where num>0  ";
			SQL += "	and vfree1 is not null and rownum <= " + pilecount;
			if (pileo.length() > 0) {
				SQL += " and  vfree1 not in(" + pileo.substring(0, pileo.length() - 1) + ")";

			}

			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List list = null;
			try {
				list = (List) sessionManager.executeQuery(SQL, new ArrayListProcessor());
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
				MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", Transformations.getLstrFromMuiStr("存货编码@Inventory coding&" + cinventorycode + "&现存量不足inadequate existing!"));
			}
			if (newcount < pilecount) {
				MessageDialog.showErrorDlg(this, "调拨出库Allocate a library", Transformations.getLstrFromMuiStr("出库垛数@The number of a library stack&" + String.valueOf(OldCount) + "& 大于现存量@Greater than the existing volume&" + String.valueOf(newcount) + "&垛 !@Pile!"));
				return;
			}
			if (pileo.length() <= 0) {
				newcount--;
			}
			if (Eindex != recordcount - 1) {
				getBillCardPanel().getBodyPanel().insertLine(Eindex + 1, newcount);
			} else {
				getBillCardPanel().getBodyPanel().addLine(newcount);
			}
			Iterator viterator = list.iterator();
			int j = pileo.length() > 0 ? Eindex + 1 : Eindex;
			GeneralBillItemVO newbi = voCopyLine(Eindex);
			String m_invcode = String.valueOf(getBillCardPanel().getBodyValueAt(Eindex, "cinventorycode"));
			String m_invid = String.valueOf(getBillCardPanel().getBodyValueAt(Eindex, "cinventoryid"));
			String m_invname = String.valueOf(((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).getRefName());

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
					if (ischekcount) {
						if (num.compareTo(StampCount) > 0) {
							if (MessageDialog.showYesNoDlg(this, "调拨出库Allocate a library", "当前垛号的整垛数量已超过该客户所要求的整垛数量，是否继续？Entire stack number of the current stack the entire stack more than the number required by the customers can contact the sales staff to modify the entire stack number of the receiving unit, or place orders again, whether to continue?") == 8) return;
						}
					}
					if (j > Eindex) {

						getBillCardPanel().getBillModel().setBodyRowVO(newbi, j);
						BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem("cinventorycode").getComponent(), m_invcode, "cinventorycode", j);
						((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setPK(m_invid);
						((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setName(m_invname);
						afterEdit(e);
					}
					String key = "";
					try {
						key = this.m_voBill.getChildrenVO()[j].getPrimaryKey();
					} catch (BusinessException e) {
						// TODO Auto-generated catch block

					}
					if (key != null && !key.equals("") && !key.equals("null")) {
						getBillCardPanel().getBillModel().setRowState(j, BillModel.MODIFICATION);
					}
					SetBodyNewValue(pileno, num, vbatchcode, csname, cspaceid, j, nshououtnum);
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
			String m_invid = String.valueOf(getBillCardPanel().getBodyValueAt(i, "cinventoryid"));

			String m_Pileno = String.valueOf(getBillCardPanel().getBodyValueAt(i, "vfree0"));
			int strIndex = m_Pileno.indexOf(":") + 1;
			int endIndex = m_Pileno.indexOf("]");

			if (m_Pileno != null && !m_Pileno.equals("") && !m_Pileno.equals("null")) {
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
	private void SetBodyNewValue(String pileno, UFDouble num, String vbatchcode, String csname, String cspaceid, int sindex, UFDouble nshououtnum) {
		getBillCardPanel().setBodyValueAt("[垛号:" + pileno + "]", sindex, "vfree0");
		getBillCardPanel().setBodyValueAt(nshououtnum, sindex, "nshouldoutnum");

		getBillCardPanel().setBodyValueAt(pileno, sindex, "vfree1");
		nc.vo.scm.ic.bill.FreeVO voFree = new nc.vo.scm.ic.bill.FreeVO();
		voFree.m_vfree0 = "[垛号:" + pileno + "]";
		voFree.m_vfreename1 = "垛号";
		voFree.m_vfree1 = pileno;
		voFree.setVfreeid1("0001A21000000004EIQW");
		m_voBill.setItemFreeVO(sindex, voFree);

		InvVO voInv = (InvVO) getBillCardPanel().getBodyValueAt(sindex, "invvo");
		if (voInv != null) {
			voInv.setFreeItemVO(voFree);
			getBillCardPanel().setBodyValueAt(voInv, sindex, "invvo");
		}
		BillEditEvent e4 = new BillEditEvent(getBillCardPanel().getBodyItem("nshouldoutnum").getComponent(), nshououtnum, "nshouldoutnum", sindex);
		afterShouldNumEdit(e4);

		if (cspaceid != null && !cspaceid.equals("") && !cspaceid.equals("null") && !cspaceid.equals("_________N/A________")) {
			getBillCardPanel().setBodyValueAt(csname, sindex, "vspacename");
			getBillCardPanel().setBodyValueAt(cspaceid, sindex, "cspaceid");
			setSpace(cspaceid, csname, sindex);
		}

		if (vbatchcode != null && !vbatchcode.equals("") && !vbatchcode.equals("null") && !vbatchcode.equals("_________N/A________")) {
			((LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode").getComponent()).setText(vbatchcode);
			((LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode").getComponent()).setValue(vbatchcode);
			getBillCardPanel().setBodyValueAt(vbatchcode, sindex, "vbatchcode");
			BillEditEvent e1 = new BillEditEvent(getBillCardPanel().getBodyItem("vbatchcode").getComponent(), vbatchcode, "vbatchcode", sindex);
			afterLotEdit(e1);
		}
		// pickupLotRef("vbatchcode");
		getBillCardPanel().setBodyValueAt(num, sindex, "noutnum");
		BillEditEvent e2 = new BillEditEvent(getBillCardPanel().getBodyItem("noutnum").getComponent(), num, "noutnum", sindex);
		afterEdit(e2);
		//		updateUI();
		//		getBillCardPanel().updateValue();

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
			assistnum = (UFDouble) getBillCardPanel().getBodyValueAt(i, m_sAstItemKey);
		} catch (Exception e4) {
		}
		UFDouble num = null;
		try {
			num = (UFDouble) getBillCardPanel().getBodyValueAt(i, m_sNumItemKey);
		} catch (Exception e2) {
		}
		UFDouble ngrossnum = null;
		try {
			ngrossnum = (UFDouble) getBillCardPanel().getBodyValueAt(i, m_sNgrossnum);
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
				if (m_alSerialData != null) m_alSerialData.set(i, null);
			} else {
				if (m_iBillInOutFlag > 0) {
					// 入库
					voLoc[0].setNoutspacenum(null);
					voLoc[0].setNinspacenum(num);
				} else {
					// 出库
					voLoc[0].setNinspacenum(null);
					voLoc[0].setNoutspacenum(num);
					if (m_alSerialData != null) m_alSerialData.set(i, null);
				}
			}
			// 毛重
			if (ngrossnum == null) {
				voLoc[0].setNingrossnum(ngrossnum);
				voLoc[0].setNoutgrossnum(ngrossnum);
				if (m_alSerialData != null) m_alSerialData.set(i, null);
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

		} else m_alLocatorData.set(i, null);

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

		this.m_voBill.getItemVOs()[row].setLocator((LocatorVO[]) m_alLocatorData.get(row));
		voaBillItem = (GeneralBillItemVO) m_voBill.getItemVOs()[row].clone();
		uicopyvo = (GeneralBillItemVO) getBillCardPanel().getBillModel().getBodyValueRowVO(row, GeneralBillItemVO.class.getName());
		uicopyvo = (GeneralBillItemVO) uicopyvo.clone();

		String firstbillhid = uicopyvo.getCfirstbillhid();
		String firstbillbid = uicopyvo.getCfirstbillbid();
		String firstbillcode = uicopyvo.getVfirstbillcode();

		String[] keys = uicopyvo.getAttributeNames();
		SmartVOUtilExt.copyVOByVO(voaBillItem, keys, uicopyvo, keys);

		if (StringIsNullOrEmpty(voaBillItem.getCfirstbillbid())) {
			voaBillItem.setCfirstbillbid(firstbillbid);
		}
		if (StringIsNullOrEmpty(voaBillItem.getCfirstbillhid())) {
			voaBillItem.setCfirstbillhid(firstbillhid);
		}
		if (StringIsNullOrEmpty(voaBillItem.getVfirstbillcode())) {
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
	 * @功能:返回公司的上级公司编码
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public String getParentCorpCode() {

		String ParentCorp = new String();
		String key = ClientEnvironment.getInstance().getCorporation().getFathercorp();
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
	public boolean Iscsflag(String primkey) {
		boolean rst = false;
		String SQL = "select csflag from bd_stordoc  where pk_stordoc ='" + primkey + "'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List list = null;
		try {
			list = (List) sessionManager.executeQuery(SQL, new ArrayListProcessor());

			if (list.isEmpty()) {
				return rst;
			} else {
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
						return rst = (new UFBoolean(String.valueOf(values.get(0)))).booleanValue();
					}
				}
			}
		} catch (BusinessException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return rst;
	}

	protected void setButtons() {
		if (getParentCorpCode().equals("10395")) {
			ButtonObject[] o_bo = getButtonTree().getButtonArray();
			ButtonObject[] bo = new ButtonObject[o_bo.length + 2];
			for (int i = 0; i < o_bo.length; i++) {
				bo[i] = o_bo[i];
			}
			bo[o_bo.length] = new ButtonObject("现存量打印", "现存量打印", "onHandQuery");
			bo[o_bo.length + 1] = new ButtonObject("合并打印", "合并打印", "onMergerQuery");
			setButtons(bo);
		} else super.setButtons();
	}

	public void onHandnumQuery() {
		//			GeneralBillVO header = (DelivbillHHeaderVO) m_currentbill.getParentVO();

		//				ToftPanel toftpanel = SFClientUtil.showNode("40083004", IFuncWindow.WINDOW_TYPE_DLG);
		//				nc.ui.ic.ic602.ClientUI ui = (nc.ui.ic.ic602.ClientUI)toftpanel;
		//				ui.setGvo(m_currentbill);
		//				ui.RefQuery();

		UITable table = this.getBillCardPanel().getBillTable();
		int[] rows = table.getSelectedRows();
		if (rows.length < 1) {
			MessageDialog.showErrorDlg(this, "提示Prompt", "请选择表体行数据！Please select the data!");
			return;
		}

		ArrayList invid = new ArrayList();
		for (int i = 0; i < rows.length; i++) {
			String Invid = String.valueOf(this.getBillCardPanel().getBillModel().getValueAt(i, "cinventoryid"));
			if (invid.indexOf(Invid) < 0) {
				invid.add(Invid);
			}
		}
		String[] pkInv = new String[invid.size()];
		for (int i = 0; i < invid.size(); i++) {
			pkInv[i] = String.valueOf(invid.get(i));
		}
		nc.ui.dm.dm104.QueryXcl dialog = new nc.ui.dm.dm104.QueryXcl(getBillCardPanel(), pkInv);
		dialog.showModal();
	}

	public boolean StringIsNullOrEmpty(Object obj) {
		return obj == null ? true : String.valueOf(obj).equals("") ? true : String.valueOf(obj).equalsIgnoreCase("null") ? true : false;
	}

	/***
	 * 合并按钮
	 *  2012-11-14
	 * Start by cm
	 */

	public void onMergerQueryPrint() {
		if (this.m_iCurPanel == BillMode.List) {
			//				MessageDialog.showErrorDlg(this, "提示", "列表状态");
			int selrow = getBillListPanel().getHeadTable().getSelectedRow();
			GeneralBillVO billvo = new GeneralBillVO();
			if (selrow >= 0) {
				billvo.setParentVO(getBillListPanel().getHeadBillModel().getBodyValueRowVO(selrow, GeneralBillHeaderVO.class.getName()));
				billvo.setChildrenVO(getBillListPanel().getBodyBillModel().getBodyValueVOs(GeneralBillItemVO.class.getName()));
			}
			GeneralBillVO gbvo = new GeneralBillVO();
			gbvo = getItemVO(billvo);
			try {
				printOnList1(gbvo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.m_iCurPanel == BillMode.Card) {
			//				MessageDialog.showErrorDlg(this, "提示", "卡片状态");
			//				GeneralBillVO billvo = (GeneralBillVO) getBillCardPanel().getBillData().getBillValueVO(GeneralBillVO.class.getName(), GeneralBillHeaderVO.class.getName(), GeneralBillItemVO.class.getName());
			GeneralBillVO billvo = null;
			if (m_iLastSelListHeadRow != -1 && null != m_alListData && m_alListData.size() != 0) {
				billvo = (GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow);
			}
			GeneralBillVO gbvo = new GeneralBillVO();
			gbvo = getItemVO(billvo);
			if (gbvo != null) {
				if (getPrintEntry().selectTemplate() < 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000048")/*
																													* @res
																													* "请先定义打印模板。"
																													*/);
					return;
				}
				printOnCard1(gbvo, true);
			}
		}
	}

	public GeneralBillVO getItemVO(GeneralBillVO vo) {
		int row = getBillListPanel().getBodyBillModel().getRowCount();
		if ((vo == null) || (vo.getHeaderVO().getCgeneralhid() == null) || (vo.getItemVOs() == null)) {
			showWarningMessage(Bill53Const.getSeleteOneBillNote());
			return null;
		}
		GeneralBillItemVO[] itemvos = vo.getItemVOs();
		HashSet hidset = new HashSet();
		HashMap<String, GeneralBillItemVO> hashmap = new HashMap<String, GeneralBillItemVO>();
		String stype = null;
		String invid = null;
		String vbatchcode = null;
		String sqlwhere = null;
		UFDouble noutnum = null;

		GeneralBillVO[] retvos = null;

		int i = 0;
		for (int j = 0; j < itemvos.length; j++) {
			itemvos[j].setVuserdef8("1");
			UFDouble ufdouble = itemvos[j].getNoutnum();
		}
		for (int loop = itemvos.length; i < loop; i++) {
			stype = itemvos[i].getCsourcetype();
			invid = itemvos[i].getCinventoryid();
			vbatchcode = itemvos[i].getVbatchcode();
			noutnum = itemvos[i].getNoutnum() == null ? new UFDouble(0) : itemvos[i].getNoutnum();
			String snoutnum = noutnum.toString();
			if (snoutnum.indexOf(".") == -1) {
				snoutnum = snoutnum + ".00000000";
			}
			UFDouble vnshouldoutassistnum = itemvos[i].getNshouldoutassistnum() == null ? new UFDouble(0) : itemvos[i].getNshouldoutassistnum();
			UFDouble vnshouldoutnum = itemvos[i].getNshouldoutnum() == null ? new UFDouble(0) : itemvos[i].getNshouldoutnum();
			UFDouble vnoutassistnum = itemvos[i].getNoutassistnum() == null ? new UFDouble(0) : itemvos[i].getNoutassistnum();
			UFDouble vnoutnum = itemvos[i].getNoutnum() == null ? new UFDouble(0) : itemvos[i].getNoutnum();
			String vuserdef9 = itemvos[i].getVuserdef7() == null ? "0" : itemvos[i].getVuserdef7();
			UFDouble vnmny = itemvos[i].getNmny() == null ? new UFDouble(0) : itemvos[i].getNmny();
			String rows = itemvos[i].getVuserdef8();

			//					if ((stype == null) || (invid == null))// || (vbatchcode == null))
			//						continue;
			//					showErrorMessage(noutnum.toString()+"==============");
			int rowswl = 1;
			if (!hashmap.containsKey(invid + vbatchcode + snoutnum)) {
				hashmap.put(invid + vbatchcode + snoutnum, itemvos[i]);
				//						showErrorMessage(invid+vbatchcode+noutnum.toString());
				//						showErrorMessage(noutnum.toString());
			} else {
				GeneralBillItemVO hashm = hashmap.get(invid + vbatchcode + snoutnum);
				int rowi = Integer.parseInt(hashm.getVuserdef8()) + 1;
				UFDouble vnshouldoutassistnum1 = hashm.getNshouldoutassistnum() == null ? new UFDouble(0) : hashm.getNshouldoutassistnum();
				UFDouble vnshouldoutnum1 = hashm.getNshouldoutnum() == null ? new UFDouble(0) : hashm.getNshouldoutnum();
				UFDouble vnoutassistnum1 = hashm.getNoutassistnum() == null ? new UFDouble(0) : hashm.getNoutassistnum();
				UFDouble vnoutnum1 = hashm.getNoutnum() == null ? new UFDouble(0) : hashm.getNoutnum();
				UFDouble vnmny1 = hashm.getNmny() == null ? new UFDouble(0) : hashm.getNmny();
				String vuserdef91 = hashm.getVuserdef7() == null ? "0" : hashm.getVuserdef7();
				//						UFDouble vnshouldoutnum1 = itemvos[i].getNshouldoutnum()==null?new UFDouble(0):itemvos[i].getNshouldoutnum();
				//						UFDouble vnoutassistnum1 = itemvos[i].getNoutassistnum()==null?new UFDouble(0):itemvos[i].getNoutassistnum();
				//						UFDouble vnoutnum1 = itemvos[i].getNoutnum()==null?new UFDouble(0):itemvos[i].getNoutnum();
				//						UFDouble vnmny1 = itemvos[i].getNmny()==null?new UFDouble(0):itemvos[i].getNmny();
				hashm.setNshouldoutassistnum(vnshouldoutassistnum1.add(vnshouldoutassistnum));
				hashm.setNshouldoutnum(vnshouldoutnum1.add(vnshouldoutnum));
				hashm.setNoutassistnum(vnoutassistnum1.add(vnoutassistnum));
				hashm.setNoutnum(vnoutnum1.add(vnoutnum));
				hashm.setNcountnum(new UFDouble(rowi));
				hashm.setVuserdef7((vnoutnum.multiply(rowi)).toString());
				hashm.setVuserdef8(rowi + "");
				hashm.setNmny(vnmny1.add(vnmny));
				hashmap.put(invid + vbatchcode + snoutnum, hashm);
			}
		}
		GeneralBillItemVO[] gbitemVO = new GeneralBillItemVO[hashmap.size()];
		int j = 0;
		Set<String> set = hashmap.keySet();//取出所有键
		TreeSet<String> ts = new TreeSet<String>();
		System.out.println("键的原序：");
		for (String s : set) {
			ts.add(s);
		}
		System.out.println("按键排序后的顺序：");
		for (String s : ts) {
			gbitemVO[j] = hashmap.get(s);
			j++;
		}

		//				Iterator<String> iter = hashmap.keySet().iterator();
		//			    while (iter.hasNext()) {
		//			    	gbitemVO[j] = hashmap.get(iter.next());
		//					j++;
		//				}
		for (int k = 0; k < gbitemVO.length; k++) {
			//					String vuserdef9 = gbitemVO[k].getVuserdef9()==null?"0":gbitemVO[k].getVuserdef9();
			String vuserdef8 = gbitemVO[k].getVuserdef8() == null ? "0" : gbitemVO[k].getVuserdef8();
			UFDouble vnoutnum = gbitemVO[k].getNoutnum() == null ? new UFDouble(0) : gbitemVO[k].getNoutnum();
			gbitemVO[k].setNoutnum(vnoutnum.div(new UFDouble(vuserdef8)));
		}
		//			    if(this.m_iCurPanel==BillMode.List){
		//			    	for (int k = 0; k < row; k++) {
		//			    		clearRowData(k);
		//					}
		//			    	getBillListPanel().getBodyBillModel().setBodyDataVO(gbitemVO);
		//			    	getBillListPanel().getBodyBillModel().execLoadFormula();
		//			    }
		//				if(this.m_iCurPanel==BillMode.Card){
		//					getBillCardPanel().getBillModel().setBodyDataVO(gbitemVO);
		//					getBillCardPanel().getBillModel().execLoadFormula();
		//			    }
		GeneralBillVO gbvo = new GeneralBillVO();
		gbvo.setParentVO(vo.getParentVO());
		gbvo.setChildrenVO(gbitemVO);
		return gbvo;
	}

	protected void printOnCard1(GeneralBillVO gbvo, boolean isPreview) {
		//			 	GeneralBillVO voBill = new GeneralBillVO();
		//			 	voBill = getItemVO(gbvo);
		//			 	GeneralBillItemVO[] voBillb = get
		//			 	for (int i = 0; i < voBill..length(); i++) {
		//					
		//				}
		// 单据主表
		GeneralBillHeaderVO headerVO = gbvo.getHeaderVO();
		String sBillID = headerVO.getPrimaryKey();

		// 构造PrintLogClient及设置PrintInfo.
		ScmPrintlogVO voSpl = new ScmPrintlogVO();
		voSpl.setCbillid(sBillID); // 单据主表的ID
		voSpl.setVbillcode(headerVO.getVbillcode());// 传入单据号，用于显示。
		voSpl.setCbilltypecode(headerVO.getCbilltypecode());
		voSpl.setCoperatorid(headerVO.getCoperatorid());
		voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
		voSpl.setPk_corp(getCorpPrimaryKey());
		voSpl.setTs(headerVO.getTs());// 单据主表的TS

		SCMEnv.out("ts=========tata" + voSpl.getTs());
		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		nc.ui.pub.print.PrintEntry pe = getPrintEntryNew();

		// 设置单据信息
		plc.setPrintInfo(voSpl);
		// 设置TS刷新监听.
		plc.addFreshTsListener(new FreshTsListener());
		// 设置打印监听
		getPrintEntry().setPrintListener(plc);

		plc.setPrintEntry(getPrintEntry());// 用于单打时
		// 设置单据信息
		plc.setPrintInfo(voSpl);

		GeneralBillItemVO[] vobillb = (GeneralBillItemVO[]) gbvo.getChildrenVO();
		//				showErrorMessage(""+vobillb.length);
		//				GeneralBillHeaderVO vbillh = (GeneralBillHeaderVO)gbvo.getParentVO();
		//			    showErrorMessage(vbillh.getCcustomername());
		for (int i = 0; i < vobillb.length; i++) {
			String vdef8 = vobillb[i].getVuserdef8();
			String vdef9 = vobillb[i].getVuserdef7();
			UFDouble noutnum = vobillb[i].getNoutnum();
			if (vdef8.endsWith("1")) {
				vobillb[i].setVuserdef7(noutnum.toString());
				vobillb[i].setNcountnum(new UFDouble(vdef8));
			}
			vobillb[i].setVuserdef7(noutnum.multiply(new UFDouble(vdef8)).toString());
		}
		gbvo.setChildrenVO(vobillb);
		// 向打印置入数据源，进行打印
		getDataSource().setVO(gbvo);
		getPrintEntry().setDataSource(getDataSource());

		// 打印提示信息
		String sPrintMsg = null;
		// 执行打印
		if (isPreview) {
			getPrintEntry().preview();
			sPrintMsg = plc.getPrintResultMsg(true);
		} else {
			getPrintEntry().print();
			sPrintMsg = plc.getPrintResultMsg(false);
		}

		MessageDialog.showHintDlg(this, null, sPrintMsg);

	}

	protected void printOnList1(GeneralBillVO alBill) throws InterruptedException {
		nc.ui.pub.print.PrintEntry pe = getPrintEntryNew();

		if (pe.selectTemplate() < 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000048")/*
																											 * @res "请先定义打印模板。"
																											 */);
			return;
		}

		pe.beginBatchPrint();

		PrintDataInterface ds = null;
		//				GeneralBillVO voBill = null;
		// 单据主表
		GeneralBillHeaderVO headerVO = null;
		// nc.vo.scm.print.PrintResultVO printResultVO = null;

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setBatchPrint(true);// 设置是批打
		// 设置打印监听
		pe.setPrintListener(plc);
		plc.setPrintEntry(pe);
		plc.addFreshTsListener(new FreshTsListener());
		GeneralBillItemVO[] vobillb = (GeneralBillItemVO[]) alBill.getChildrenVO();
		for (int j = 0; j < vobillb.length; j++) {
			String vdef8 = vobillb[j].getVuserdef8();
			String vdef9 = vobillb[j].getVuserdef7();
			UFDouble noutnum = vobillb[j].getNoutnum();
			if (vdef8.endsWith("1")) {
				vobillb[j].setVuserdef7(noutnum.toString());
				vobillb[j].setNcountnum(new UFDouble(vdef8));
			}
			vobillb[j].setVuserdef7(noutnum.multiply(new UFDouble(vdef8)).toString());
		}
		alBill.setChildrenVO(vobillb);
		// 打印操作
		for (int i = 0; i < alBill.getChildrenVO().length; i++) {
			//					alBill = alBill;
			headerVO = alBill.getHeaderVO();

			ScmPrintlogVO voSpl = new ScmPrintlogVO();
			voSpl = new ScmPrintlogVO();
			voSpl.setCbillid(headerVO.getPrimaryKey()); // 单据主表的ID
			voSpl.setVbillcode(headerVO.getVbillcode());// 传入单据号，用于显示。
			voSpl.setCbilltypecode(headerVO.getCbilltypecode());
			voSpl.setCoperatorid(headerVO.getCoperatorid());
			voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// 固定
			voSpl.setPk_corp(getCorpPrimaryKey());
			voSpl.setTs(headerVO.getTs());// 单据主表的TS

			SCMEnv.out("ts=========tata" + voSpl.getTs());
			// 设置单据信息
			plc.setPrintInfo(voSpl);

			if (plc.check()) {// 检查通过才执行打印，有错误的话自动插入打印日志，这里不用处理。
				ds = getDataSourceNew();
				ds.setVO(alBill);
				pe.setDataSource(ds);

				// 常量定义在Setup（很小）中，在现场很容易定制它。
				//						while (pe.dsCountInPool() > PrintConst.PL_MAX_TAST_NUM) {
				//							Thread.currentThread().sleep(PrintConst.PL_SLEEP_TIME); // 如果有PL_MAX_TAST_NUM个以上任务，就等待PL_SLEEP_TIME秒。
				//						}
			}

		}
		pe.endBatchPrint();

		MessageDialog.showHintDlg(this, null, plc.getPrintResultMsg(false));

	}


	/**
	 * 货位调整批次号批选，有垛号处理。
	 * @author shikun
	 * @time 2014-12-06 
	 * */
	@Override
	public void afterEditVfree1(Object vfree1, int row) {
		//获取表头字段仓库字段
		String cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject()==null?"":getBillCardPanel().getHeadItem("cwarehouseid").getValueObject().toString();
		//获得操作行的存货管理档案主键
		String cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid")==null?"":getBillCardPanel().getBodyValueAt(row, "cinventoryid").toString();
		
		if (vfree1==null||vfree1.equals("")||cwarehouseid.equals("")||cinventoryid.equals("")) { 
			return; 
		}
		
		String sql = getQuSql(cwarehouseid,cinventoryid,vfree1.toString());
		
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		MapListProcessor alp = new MapListProcessor();
		try {
			setCspaceidAndVbatchcodeAndNoutnum(row,query,alp,sql);
		} catch (BusinessException e) {
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 依据单件号查询对应的货位、批次号、最大数量（去除冻结数），查询语句。
	 * add by shikun 2014-04-12 
	 * */
	private String getQuSql(String cwarehouseid, String cinventoryid, String pathcoed) {
		StringBuffer sb = new StringBuffer("");
		sb.append(" select wglsl, cspaceid,csname, vbatchcode,cvendorid ") 
		.append("   from (select SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
		.append("                    nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0)) wglsl, ") 
		.append("                v1.cspaceid,car.csname, ") 
		.append("                v1.vbatchcode, v1.cvendorid ") 
		.append("           from v_ic_onhandnum6 v1 ") 
		.append("           left join bd_cargdoc car on nvl(car.dr,0)=0 and car.pk_cargdoc = v1.cspaceid ")
		.append("           left join ic_freeze icf on v1.cinventoryid = icf.cinventoryid ") 
		.append("                                  and icf.ccalbodyid = v1.ccalbodyid ") 
		.append("                                  and icf.cwarehouseid = v1.cwarehouseid ") 
		.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
		.append("                                  and icf.pk_corp = v1.pk_corp ") 
		.append("                                  and nvl(v1.cvendorid,'byzgyh') = nvl(icf.cvendorid,'byzgyh') ")
		.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
		.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
		.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
		.append("          where 1 = 1 ") 
		.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
		.append("            and v1.vfree1 = '"+pathcoed+"' ") 
		.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
		.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
		.append("          group by v1.cspaceid,car.csname, v1.vbatchcode, v1.cvendorid) ") 
		.append("  where 1 = 1 ") 
//		.append("    and wglsl > 0 ") 
		.append("    and wglsl = ") 
		.append("        (select max(SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
		.append("                        nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0))) wglsl ") 
		.append("           from v_ic_onhandnum6 v1 ") 
		.append("           left join ic_freeze icf on v1.cinventoryid = icf.cinventoryid ") 
		.append("                                  and icf.ccalbodyid = v1.ccalbodyid ") 
		.append("                                  and icf.cwarehouseid = v1.cwarehouseid ") 
		.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
		.append("                                  and icf.pk_corp = v1.pk_corp ") 
		.append("                                  and nvl(v1.cvendorid,'byzgyh') = nvl(icf.cvendorid,'byzgyh') ")
		.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
		.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
		.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
		.append("          where 1 = 1 ") 
		.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
		.append("            and v1.vfree1 = '"+pathcoed+"' ") 
		.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
		.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
		.append("          group by v1.cspaceid, v1.vbatchcode, v1.cvendorid) ") ;
		return sb.toString();
	}

	/**
	 * 依据单件号获取对应的货位、批次号、数量（去除冻结数），并对当前操作行进行赋值 
	 * add by shikun 2014-04-12 
	 * @throws BusinessException 
	 * */
	@SuppressWarnings("unchecked")
	private void setCspaceidAndVbatchcodeAndNoutnum(int row, IUAPQueryBS query, MapListProcessor alp, String sql) throws BusinessException {
		ArrayList addrList = (ArrayList) query.executeQuery(sql, alp);
		if (addrList != null && addrList.size() > 0) {
			Map addrMap = (Map) addrList.get(0);
			String vbatchcode = addrMap.get("vbatchcode") == null ? "" : addrMap.get("vbatchcode").toString();
			String cspaceid = addrMap.get("cspaceid") == null ? "" : addrMap.get("cspaceid").toString();
			String csname = addrMap.get("csname") == null ? "" : addrMap.get("csname").toString();
			String cvendorid = addrMap.get("cvendorid") == null ? "" : addrMap.get("cvendorid").toString();
			UFDouble wglsl = new UFDouble(addrMap.get("wglsl").toString()) == null ? new UFDouble(0.00) : new UFDouble(addrMap.get("wglsl").toString());
			if (wglsl.doubleValue()==0) {
				showWarningMessage("出库的数量已经为0，请注意！");
			}
			getBillCardPanel().getBillModel().setValueAt(vbatchcode, row, "vbatchcode");
			getBillCardPanel().getBillModel().setValueAt(cvendorid, row, "cvendorid");
			getBillCardPanel().getBillModel().setValueAt(wglsl, row, "noutnum");
			
			//edit by shikun 2014-10-13
			if (!"".equals(cspaceid)) {
				getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");
				setSpace(cspaceid, csname, row);
			}
			//end
			
			getBillCardPanel().getBillModel().execLoadFormula();
		}
	}

}