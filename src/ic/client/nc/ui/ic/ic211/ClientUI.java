package nc.ui.ic.ic211;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.itf.bgzg.pub.BGZGProxy;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.ic.auditdlg.ClientUIInAndOut;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.ICBcurrArithUI;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ic.pub.pf.ICBillQuery;
import nc.ui.ic.pub.pf.QryInBillDlg;
import nc.ui.ic.pub.print.PrintDataInterface;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.scm.pub.billutil.ClientCacheHelper;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.recordtime.RecordTimeHelper;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic700.Bill53Const;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.merge.DefaultVOMerger;
import nc.vo.scm.print.PrintConst;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.ctrl.GenMsgCtrl;
import nc.vo.scm.pub.session.ClientLink;

/**
 * 销售出库
 * UI类
 * */
public class ClientUI extends GeneralBillClientUI {
	private FormMemoDlg ivjFormMemoDlg1 = null;//备注

	private ClientUIInAndOut m_dlgInOut = null;

	private ArrayList m_alBrwLendBusitype = null;//收发类别

	private final String m_sBusiTypeItemKey = "cbiztype";//业务类型

	private final String m_sPNodeCode = "40080802";//节点号

	private boolean m_isQCstartup = false;

	private boolean m_isCheckQCstartup = false;
	private ICBcurrArithUI clsCurrArith;
	/****************************/
	private Hashtable invidinfo;// 记录存货信息
	private ArrayList PilenoList;// 记录扫描过的垛号
	private String oldInvID = new String();// 记录选择的存货
	private boolean flag;// 垛号扫描标志
	private static File filename = new File("");
	private static String readStr = "";

	/**********************/
	public ClientUI() {
		initialize();
	}

	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);
	}

	protected void afterBillEdit(BillEditEvent e) {
		String sItemKey = e.getKey();
		if (getParentCorpCode().equals("10395")) {
			if (sItemKey.equals("StampNo")) {
				if (!e.getValue().toString().equals("")) {
					DoSplitData(e.getValue().toString(), 0);
				}
				getBillCardPanel().getHeadItem("StampNo").setValue("");
				for (int i = 0; i < getBillCardPanel().getBillTable()
						.getRowCount(); i++) {
					getBillCardPanel()
							.setBodyValueAt((i + 1) * 10, i, "crowno");
				}
				getBillCardPanel().getHeadItem("StampNo").getComponent()
						.requestFocusInWindow();
			} else if (sItemKey.equals("StampCount")) {
				if (!String.valueOf(e.getValue()).equals("")) {

					DoSplitData("", Integer.parseInt(e.getValue().toString()));

					for (int i = 0; i < getBillCardPanel().getBillTable()
							.getRowCount(); i++) {
						getBillCardPanel().setBodyValueAt((i + 1) * 10, i,
								"crowno");
					}
				}
			} else if (sItemKey.equals("cwarehouseid")
					|| sItemKey.equals("cinventorycode")) {

				getBillCardPanel().getBodyValueAt(e.getRow(), "");
				if (flag) {
					return;
				}
				String cwarehouseid = ((UIRefPane) getBillCardPanel()
						.getHeadItem("cwarehouseid").getComponent()).getRefPK();

				if (cwarehouseid == null || cwarehouseid.equals("")) {
					return;
				}

				/**
				 * @功能:根据仓库、存货带出默认货位
				 * @author ：林桂莹
				 * @2012/9/5
				 * 
				 * @since v50
				 */
				if (Iscsflag(cwarehouseid)) {
					for (int i = 0; i < getBillCardPanel().getBillTable()
							.getRowCount(); i++) {

						String cinventoryid = (String) getBillCardPanel()
								.getBodyValueAt(i, "cinventoryid");
						if (cinventoryid == null || cinventoryid.equals("")) {
							return;
						}

						String Sql = "select * from (select d.pk_cargdoc,d.csname,d.cscode from ic_general_h a  ";
						Sql += "left join ic_general_b b on a.cgeneralhid=b.cgeneralhid  ";
						Sql += "left join ic_general_bb1 c on c.cgeneralbid=b.cgeneralbid  ";
						Sql += "left join bd_cargdoc d on c.cspaceid=d.pk_cargdoc  ";
						Sql += "where a.cbilltypecode ='4C' and a.cwarehouseid='"
								+ cwarehouseid
								+ "' and  b.cinventoryid='"
								+ cinventoryid + "'  ";
						Sql += " and d.pk_cargdoc is not null  and a.taccounttime is not null and nvl(b.dr,0)=0  order by a.taccounttime desc  ";
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

							setSpace(String.valueOf(values.get(0)), String
									.valueOf(values.get(1)), i);
						}
					}
				}
			}
			// 增加罐子/盖子合计数、合计垛数、发运数、可拨数,并自动统计.xiaolong_fan.2013-01-09.
			UFDouble[][] heji = {
					{ new UFDouble(0), new UFDouble(0), new UFDouble(0),
							new UFDouble(0) },
					{ new UFDouble(0), new UFDouble(0), new UFDouble(0),
							new UFDouble(0) } };
			int row = getBillCardPanel().getBillModel().getRowCount();
			Map map = new HashMap();
			// 项目主键 cwarehouseid
			String cwarehouseid = getBillCardPanel()
					.getHeadItem("cwarehouseid").getValue() == null ? ""
					: getBillCardPanel().getHeadItem("cwarehouseid").getValue();
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			String sql = "select ninspacenum from v_ic_onhandnum6 where cwarehouseid='"
					+ cwarehouseid + "' and cinvbasid='";
			for (int j = 0; j < row; j++) {
				// 项目主键 cinvbasid
				String cinvbasid = String.valueOf(getBillCardPanel()
						.getBodyValueAt(j, "cinvbasid"));
				// 存货编码
				String invcode = String.valueOf(getBillCardPanel()
						.getBodyValueAt(j, "cinventorycode"));
				// 数量
				UFDouble num = new UFDouble(String.valueOf(getBillCardPanel()
						.getBodyValueAt(j, "noutnum") == null ? 0
						: getBillCardPanel().getBodyValueAt(j, "noutnum")));
				// 发运数项目主键 nshouldoutnum
				UFDouble fayshu = new UFDouble(String
						.valueOf(getBillCardPanel().getBodyValueAt(j,
								"nshouldoutnum") == null ? 0
								: getBillCardPanel().getBodyValueAt(j,
										"nshouldoutnum")));
				// 可拨数
				UFDouble keboshu = new UFDouble(0);
				if (!map.containsKey(cinvbasid)) {
					Object nnumRes = null;
					try {
						nnumRes = query.executeQuery(sql + cinvbasid + "'",
								new ColumnProcessor());
					} catch (BusinessException e1) {
						e1.printStackTrace();
					}
					if (nnumRes != null) {
						keboshu = new UFDouble(String.valueOf(nnumRes));
					}
				}
				if (invcode.startsWith("22") || invcode.startsWith("21")) {// 盖子
					heji[0][0] = heji[0][0].add(num);
					// 垛数
					heji[0][1] = heji[0][1].add(1);
					heji[0][2] = heji[0][2].add(fayshu);
					heji[0][3] = heji[0][3].add(keboshu);
				} else if (invcode.startsWith("23")) {// 罐子
					heji[1][0] = heji[1][0].add(num);
					heji[1][1] = heji[1][1].add(1);
					heji[1][2] = heji[1][2].add(fayshu);
					heji[1][3] = heji[1][3].add(keboshu);
				}
			}
			// 给合计字段赋值
			getBillCardPanel().setHeadItem("gzhjs", heji[1][0]);
			getBillCardPanel().setHeadItem("gzds", heji[1][1]);
			getBillCardPanel().setHeadItem("gzfys", heji[1][2]);
			getBillCardPanel().setHeadItem("gzkbs", heji[1][3]);
			getBillCardPanel().setHeadItem("gghjs", heji[0][0]);
			getBillCardPanel().setHeadItem("ggds", heji[0][1]);
			getBillCardPanel().setHeadItem("ggfys", heji[0][2]);
			getBillCardPanel().setHeadItem("ggkbs", heji[0][3]);
			// end by xiaolong_fan.
		}
	}

	protected void afterBillItemSelChg(int iRow, int iCol) {
	}

	public void afterInvEdit(BillEditEvent e) {
		SCMEnv.out("inv chg");
		try {
			String sItemKey = e.getKey();
			int row = e.getRow();

			if (e.getValue().toString().trim().length() == 0) {
				clearRowData(row);

				if (this.m_voBill != null) {
					this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey,
							null);
					this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey,
							null);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldNumItemKey);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldAstItemKey);
				}

				setTailValue(null);
			} else {
				String sTempID1 = ((UIRefPane) getBillCardPanel().getBodyItem(
						sItemKey).getComponent()).getRefPK();

				if ((sTempID1 != null) && (sTempID1.trim().length() != 0)) {
					String sTempID2 = null;
					if (getBillCardPanel().getHeadItem(this.m_sMainWhItemKey) != null) {
						sTempID2 = getBillCardPanel().getHeadItem(
								this.m_sMainWhItemKey).getValue();
					}
					ArrayList alIDs = new ArrayList();
					alIDs.add(sTempID2);
					alIDs.add(sTempID1);
					alIDs.add(this.m_sUserID);
					alIDs.add(this.m_sCorpID);

					InvVO voInv = (InvVO) GeneralBillHelper.queryInfo(
							new Integer(0), alIDs);

					if (this.m_voBill != null) {
						this.m_voBill.setItemInv(row, voInv);
						this.m_voBill.setItemValue(row, "cgeneralhid",
								this.m_voBill.getHeaderValue("cgeneralhid"));
					}

					setBodyInvValue(row, voInv);

					clearRowData(0, row, sItemKey);

					execEditFomulas(0, "cinventorycode");
				}

			}

			if ((getSourBillTypeCode() == null)
					|| (getSourBillTypeCode().trim().length() == 0)) {
				if (this.m_voBill != null) {
					this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey,
							null);
					this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey,
							null);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldNumItemKey);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldAstItemKey);
				}

			}

			setBtnStatusSN(e.getRow(), true);
		} catch (Exception e2) {
			SCMEnv.error(e2);
		}
	}

	public void afterInvEditforBarCode(String sItemKey, int row) {
		SCMEnv.out("inv chg");
		try {
			String sTempID1 = ((UIRefPane) getBillCardPanel().getBodyItem(
					sItemKey).getComponent()).getRefPK();

			if ((sTempID1 != null) && (sTempID1.trim().length() != 0)) {
				String sTempID2 = null;
				if (getBillCardPanel().getHeadItem(this.m_sMainWhItemKey) != null) {
					sTempID2 = getBillCardPanel().getHeadItem(
							this.m_sMainWhItemKey).getValue();
				}
				ArrayList alIDs = new ArrayList();
				alIDs.add(sTempID2);
				alIDs.add(sTempID1);
				alIDs.add(this.m_sUserID);
				alIDs.add(this.m_sCorpID);

				InvVO voInv = (InvVO) GeneralBillHelper.queryInfo(
						new Integer(0), alIDs);
				if (this.m_voBill != null) {
					this.m_voBill.setItemInv(row, voInv);
				}
				setBodyInvValue(row, voInv);

				clearRowData(0, row, sItemKey);
			}

			if ((getSourBillTypeCode() == null)
					|| (getSourBillTypeCode().trim().length() == 0)) {
				if (this.m_voBill != null) {
					this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey,
							null);
					this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey,
							null);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldNumItemKey);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldAstItemKey);
				}
			}

		} catch (Exception e2) {
			SCMEnv.error(e2);
		}
	}

	public void afterInvEditforBarCode(BillEditEvent e) {
		SCMEnv.out("inv chg");
		try {
			String sItemKey = e.getKey();
			int row = e.getRow();

			if (e.getValue().toString().trim().length() == 0) {
				clearRowData(row);

				if (this.m_voBill != null) {
					this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey,
							null);
					this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey,
							null);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldNumItemKey);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldAstItemKey);
				}

				setTailValue(null);
			} else {
				String sTempID1 = ((UIRefPane) getBillCardPanel().getBodyItem(
						sItemKey).getComponent()).getRefPK();

				if ((sTempID1 != null) && (sTempID1.trim().length() != 0)) {
					String sTempID2 = null;
					if (getBillCardPanel().getHeadItem(this.m_sMainWhItemKey) != null) {
						sTempID2 = getBillCardPanel().getHeadItem(
								this.m_sMainWhItemKey).getValue();
					}
					ArrayList alIDs = new ArrayList();
					alIDs.add(sTempID2);
					alIDs.add(sTempID1);
					alIDs.add(this.m_sUserID);
					alIDs.add(this.m_sCorpID);

					InvVO voInv = (InvVO) GeneralBillHelper.queryInfo(
							new Integer(0), alIDs);
					if (this.m_voBill != null) {
						this.m_voBill.setItemInv(row, voInv);
					}
					setBodyInvValue(row, voInv);

					clearRowData(0, row, sItemKey);
				}

			}

			if ((getSourBillTypeCode() == null)
					|| (getSourBillTypeCode().trim().length() == 0)) {
				if (this.m_voBill != null) {
					this.m_voBill.setItemValue(row, this.m_sShouldNumItemKey,
							null);
					this.m_voBill.setItemValue(row, this.m_sShouldAstItemKey,
							null);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldNumItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldNumItemKey);
				}
				if (getBillCardPanel().getBodyItem(this.m_sShouldAstItemKey) != null) {
					getBillCardPanel().setBodyValueAt(null, row,
							this.m_sShouldAstItemKey);
				}

			}

			setBtnStatusSN(e.getRow(), true);
		} catch (Exception e2) {
			SCMEnv.error(e2);
		}
	}

	public boolean beforeBillItemEdit(BillEditEvent e) {
		return true;
	}

	protected void beforeBillItemSelChg(int iRow, int iCol) {
	}

	public void bodyRowChange(BillEditEvent e) {
		super.bodyRowChange(e);
	}

	protected boolean checkVO(GeneralBillVO voBill) {
		return checkVO();
	}

	protected void execExtendFormula(ArrayList alListData) {
		if ((alListData == null) || (alListData.get(0) == null))
			return;
		int iLen = alListData.size();
		CircularlyAccessibleValueObject[] headVO = new CircularlyAccessibleValueObject[iLen];
		for (int i = 0; i < iLen; ++i) {
			headVO[i] = ((AggregatedValueObject) alListData.get(i))
					.getParentVO();
		}

		ClientCacheHelper.getColValue(headVO,
				new String[] { "pk_cubasdoctran" }, "dm_trancust",
				"pk_trancust", new String[] { "pkcusmandoc" },
				"cwastewarehouseid");

		ClientCacheHelper.getColValue(headVO, new String[] { "vcustname" },
				"bd_cubasdoc", "pk_cubasdoc", new String[] { "custname" },
				"pk_cubasdoctran");
	}

	public GeneralBillVO getBillVO() {
		GeneralBillVO billVO = super.getBillVO();
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null) {
			billVO.setHeaderValue("vdiliveraddress",
					((UIRefPane) getBillCardPanel().getHeadItem(
							"vdiliveraddress").getComponent()).getText());
		}

		return billVO;
	}

	protected QueryConditionDlgForBill getConditionDlg() {
		if (this.ivjQueryConditionDlg == null) {
			this.ivjQueryConditionDlg = super.getConditionDlg();
			this.ivjQueryConditionDlg
					.setCombox("freplenishflag",
							new String[][] {
									{
											"1",
											NCLangRes.getInstance().getStrByID(
													"4008busi",
													"UPP4008busi-000368") },
									{
											"2",
											NCLangRes.getInstance().getStrByID(
													"4008busi",
													"UPT40080602-000014") },
									{
											"3",
											NCLangRes.getInstance().getStrByID(
													"4008busi",
													"UPPSCMCommon-000217") } });

			this.ivjQueryConditionDlg.setCorpRefs("head.pk_corp",
					new String[] { "head.ccustomerid" });
		}

		return this.ivjQueryConditionDlg;
	}

	protected ClientUIInAndOut getDispenseDlg(String sTitle, ArrayList alInVO,
			ArrayList alOutVO) {
		if (this.m_dlgInOut == null) {
			try {
				this.m_dlgInOut = new ClientUIInAndOut(this, sTitle);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}

		this.m_voBill = ((GeneralBillVO) ((GeneralBillVO) this.m_alListData
				.get(this.m_iLastSelListHeadRow)).clone());

		this.m_dlgInOut.setVO(this.m_voBill, alInVO, alOutVO,
				this.m_sBillTypeCode, this.m_voBill.getPrimaryKey().trim(),
				this.m_sCorpID, this.m_sUserID);

		this.m_dlgInOut.setName("BillDlg");

		return this.m_dlgInOut;
	}

	private FormMemoDlg getFormMemoDlg1() {
		if (this.ivjFormMemoDlg1 == null) {
			try {
				this.ivjFormMemoDlg1 = new FormMemoDlg(this);
				this.ivjFormMemoDlg1.setName("FormMemoDlg1");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjFormMemoDlg1;
	}

	public String getTitle() {
		return super.getTitle();
	}

	public void initialize() {
		super.initialize();
		try {
			BillItem item = getBillCardPanel().getBodyItem("navlinvoicenum");
			if (item != null) {
				item.setShow(false);
				getBillCardPanel().getBodyPanel()
						.hideTableCol("navlinvoicenum");
			}
		} catch (Exception e) {
			SCMEnv.error(e.getMessage());
		}
	}

	protected void initPanel() {
		super.setBillInOutFlag(-1);

		super.setNeedBillRef(true);

		this.m_sBillTypeCode = BillTypeConst.m_saleOut;

		this.m_sCurrentBillNode = "40080802";

		getButtonTree().getButton("形成代管").setEnabled(false);

		getButtonTree().getButton("配套").setEnabled(true);
	}

	private void onPrintCert() {
		Object obj = null;
		try {
			if (!this.m_isCheckQCstartup) {
				this.m_isQCstartup = GenMethod.isProductEnabled(this.m_sCorpID,
						"QC");

				this.m_isCheckQCstartup = true;
			}
			if (!this.m_isQCstartup) {
				showHintMessage(NCLangRes.getInstance().getStrByID("4008other",
						"UPP4008other-000492"));

				return;
			}

			Class cl = Class.forName("nc.ui.qc.inter.CertService");
			obj = cl.newInstance();
		} catch (Exception e) {
			SCMEnv.error(e);
		}
		if (obj == null)
			return;
		ClientEnvironment ce = ClientEnvironment.getInstance();

		ClientLink client = new ClientLink(ce);
		GeneralBillVO voBill = null;
		if ((this.m_voBill != null) && (this.m_iCurPanel == 5)) {
			voBill = (GeneralBillVO) this.m_voBill.clone();

			// ((CertService)obj).printCert(this, voBill, client);
		} else {
			if ((this.m_iLastSelListHeadRow == -1)
					|| (null == this.m_alListData)
					|| (this.m_alListData.size() == 0))
				return;
			voBill = (GeneralBillVO) this.m_alListData
					.get(this.m_iLastSelListHeadRow);

			// ((CertService)obj).printCert(this, voBill, client);
		}
	}

	protected boolean isBrwLendBiztype() {
		try {
			GeneralBillVO voMyBill = null;

			String sBusitypeid = null;
			if (this.m_iCurPanel == 4) {
				if ((this.m_alListData != null)
						&& (this.m_iLastSelListHeadRow >= 0)
						&& (this.m_alListData.size() > this.m_iLastSelListHeadRow)
						&& (this.m_alListData.get(this.m_iLastSelListHeadRow) != null)) {
					voMyBill = (GeneralBillVO) this.m_alListData
							.get(this.m_iLastSelListHeadRow);

					sBusitypeid = (String) voMyBill.getHeaderValue("cbiztype");
				}

			} else if ((getBillCardPanel().getHeadItem("cbiztype") != null)
					&& (getBillCardPanel().getHeadItem("cbiztype")
							.getComponent() != null)) {
				UIRefPane ref = (UIRefPane) getBillCardPanel().getHeadItem(
						"cbiztype").getComponent();

				sBusitypeid = ref.getRefPK();
			}

			if ((sBusitypeid != null) && (this.m_alBrwLendBusitype == null)) {
				ArrayList alParam = new ArrayList();
				alParam.add(this.m_sCorpID);
				this.m_alBrwLendBusitype = ((ArrayList) GeneralBillHelper
						.queryInfo(new Integer(17), alParam));

				if (this.m_alBrwLendBusitype == null) {
					this.m_alBrwLendBusitype = new ArrayList();
				}
			}
			if ((sBusitypeid != null) && (this.m_alBrwLendBusitype != null)
					&& (this.m_alBrwLendBusitype.contains(sBusitypeid))) {
				return true;
			}
		} catch (Exception e) {
			SCMEnv.error(e);
		}
		return false;
	}

	public void onAdd() {
		super.onAdd();
		try {
			this.m_dTime = RecordTimeHelper.getTimeStamp();
		} catch (Exception e) {
			SCMEnv.error(e);
		}
	}

	public void onButtonClicked(ButtonObject bo) {
		showHintMessage(bo.getName());

		if (bo.getCode().equals("onHandQuery")) {
			onHandnumQuery();   //现存量打印
		} else if (bo.getCode().equals("onMergerQuery")) {// add by cm
			// 2012-11-12
			onMergerQueryPrint();  //合并打印
			// onPrintSumRow();
		}

		else if (bo == getButtonTree().getButton("形成代管")) {
			onFormMemo();
		} else if (bo == getButtonTree().getButton("配套")) {
			onDispense();
		} else if (bo == getButtonTree().getButton("汇总打印批次")) {
			onPrintLot();
		} else if (bo == getButtonTree().getButton("打印质证书")) {
			// onPrintCert();
		} else if (bo == getButtonTree().getButton("参照入库单")) {
			onRefInBill();
		} else if (bo == getButtonTree().getButton("保存")) {
			onSave();
			
	//add by yqq 20161205 签字前先判断在销售成本结转单中是否已经存在 		
		} else if (bo == getButtonTree().getButton("签字")) {
			onAudit();
	//end by yqq 20161205 		
		}else {
			if ((((bo == getButtonTree().getButton("增加"))
					|| (bo == getButtonTree().getButton("复制")) || (bo == getButtonTree()
					.getButton("修改"))))
					&& (getBillCardPanel().getHeadItem("ccustomerid") != null)
					&& (getBillCardPanel().getHeadItem("vdiliveraddress") != null)) {
				String sRefPK = ((UIRefPane) getBillCardPanel().getHeadItem(
						"ccustomerid").getComponent()).getRefPK();

				if (sRefPK != null) {
					((UIRefPane) getBillCardPanel().getHeadItem(
							"vdiliveraddress").getComponent())
							.setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"
									+ sRefPK + "')");
				}

			}

			super.onButtonClicked(bo);

		}
		InitGlobalVar();
	}
	
	
	//edit by yqq 2016-12-05 判断该笔数据是否已签字生成财务会计之销售成本结转	
	public void onAudit() {		
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();		
		 int size = this.getBillCardPanel().getBillModel().getRowCount();	
		 if(size>0){
	       for (int i = 0; i < size; i++){  
		        String sCrowno  = (String) getBillCardPanel().getBodyValueAt(i,"crowno "); //行号
		        String sCgeneralbid  = (String) getBillCardPanel().getBodyValueAt(i,"cgeneralbid "); //出库单行ID
			 	ArrayList list = getDr(pk_corp,sCgeneralbid);   //销售成本结转单的DR获取
			 	if (list.size() > 0){
			 	   for(int j =0;j<list.size();j++){
			 	        Map map = (Map) list.get(j);	 
				        String dr = map.get("dr")==null?"":map.get("dr").toString();
				        if(dr.equals("1")){
					       continue;				  
				        }else{
	    				   showWarningMessage("已生成销售成本结转单,不能再签字");
	    				   return;					
			            }
			 	 }
	     	  }
	        }
		 }	
			super.onAudit();
	}

	public ArrayList getDr(String pk_corp,String cgeneralbid){		
			StringBuffer sql = new StringBuffer();
			sql.append(" select ia.dr ") 
			.append("   from ia_bill_b b ") 
			.append("   left join ia_bill ia ") 
			.append("     on b.cbillid = ia.cbillid ") 
			.append("     where  b.cbilltypecode = 'I5' ") 
			.append("            and b.csourcebilltypecode = '4C' ") 
			.append("            and b.pk_corp='"+pk_corp+"'  ") 
			.append("            and b.csourcebillitemid='"+cgeneralbid+"' ");
		
			ArrayList list = new ArrayList();
			IUAPQueryBS uAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				list = (ArrayList)uAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;	  
	  }
	//end by yqq 20161205	

	private void onDispense() {
		if ((3 != this.m_iMode) || (isSigned() == 1)) {
			return;
		}

		if (getBillCardPanel().getBillTable().getSelectedRows().length < 1)
			return;
		if (2 == MessageDialog.showOkCancelDlg(this, null, NCLangRes
				.getInstance().getStrByID("4008busi", "UPP4008busi-000268"))) {
			return;
		}

		GeneralBillVO voBill = (GeneralBillVO) ((GeneralBillVO) this.m_alListData
				.get(this.m_iLastSelListHeadRow)).clone();

		GeneralBillVO voBillclone = (GeneralBillVO) voBill.clone();

		ArrayList alOutGeneralVO = new ArrayList();
		ArrayList alInGeneralVO = new ArrayList();

		ArrayList aloutitem = new ArrayList();
		ArrayList alinitem = new ArrayList();
		int[] rownums = getBillCardPanel().getBillTable().getSelectedRows();

		for (int i = 0; i < rownums.length; ++i) {
			if (!isSetInv(voBill, rownums[i]))
				continue;
			if (isDispensedBill(voBill, rownums[i])) {
				continue;
			}

			GeneralBillItemVO voParts = voBill.getItemVOs()[rownums[i]];
			UFDouble ufSetNum = null;

			ufSetNum = voParts.getNoutnum();
			voParts.setAttributeValue("nshouldinnum", voParts.getNoutnum());
			voParts.setAttributeValue("nneedinassistnum", voParts
					.getNoutassistnum());

			voParts.setAttributeValue("ninnum", voParts.getNoutnum());
			voParts.setAttributeValue("ninassistnum", voParts
					.getNoutassistnum());

			voParts.setAttributeValue("noutnum", null);
			voParts.setAttributeValue("noutassistnum", null);
			voParts.setAttributeValue("nshouldoutnum", null);
			voParts.setAttributeValue("nshouldoutassistnum", null);

			voParts.setAttributeValue("csourcetype", voBill.getHeaderVO()
					.getCbilltypecode());

			voParts.setAttributeValue("csourcebillhid", voBill.getHeaderVO()
					.getPrimaryKey());

			voParts.setAttributeValue("csourcebillbid",
					voBill.getItemVOs()[rownums[i]].getPrimaryKey());

			voParts.setAttributeValue("vsourcebillcode", voBill.getHeaderVO()
					.getVbillcode());

			voParts.setCgeneralbid(null);
			voParts.setCgeneralbb3(null);
			voParts.setCsourceheadts(null);
			voParts.setCsourcebodyts(null);
			voParts.setDbizdate(new UFDate(this.m_sLogDate));

			alinitem.add(voParts);

			voParts.setLocator(null);
			GeneralBillItemVO[] tempItemVO = splitInvKit(voParts, voBillclone
					.getHeaderVO(), ufSetNum);

			if ((tempItemVO != null) && (tempItemVO.length > 0)) {
				for (int j = 0; j < tempItemVO.length; ++j) {
					aloutitem.add(tempItemVO[j]);
				}
			} else {
				showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi",
						"UPP4008busi-000270"));

				return;
			}
		}

		if ((aloutitem.size() == 0) || (alinitem.size() == 0)) {
			return;
		}
		GeneralBillVO gbvoIn = new GeneralBillVO();
		voBill.getHeaderVO().setCoperatorid(this.m_sUserID);
		voBill.getHeaderVO().setDbilldate(new UFDate(this.m_sLogDate));

		gbvoIn.setParentVO(voBill.getParentVO());
		gbvoIn.getHeaderVO().setPrimaryKey(null);
		gbvoIn.getHeaderVO().setVbillcode(null);
		gbvoIn.getHeaderVO().setCbilltypecode(BillTypeConst.m_otherIn);

		gbvoIn.getHeaderVO().setStatus(2);
		gbvoIn.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

		GeneralBillItemVO[] inbodys = new GeneralBillItemVO[alinitem.size()];
		alinitem.toArray(inbodys);
		gbvoIn.setChildrenVO(inbodys);
		alInGeneralVO.add(gbvoIn);

		GeneralBillVO gbvoOut = new GeneralBillVO();
		gbvoOut.setParentVO(voBillclone.getParentVO());
		gbvoOut.getHeaderVO().setPrimaryKey(null);
		gbvoOut.getHeaderVO().setVbillcode(null);
		gbvoOut.getHeaderVO().setCbilltypecode(BillTypeConst.m_otherOut);

		gbvoOut.getHeaderVO().setStatus(2);
		gbvoOut.getHeaderVO().setAttributeValue("bdispenseflag", "Y");

		GeneralBillItemVO[] outbodys = new GeneralBillItemVO[aloutitem.size()];
		aloutitem.toArray(outbodys);

		gbvoOut.setChildrenVO(outbodys);

		BillRowNo.setVORowNoByRule(gbvoOut, BillTypeConst.m_otherOut, "crowno");

		alOutGeneralVO.add(gbvoOut);

		getDispenseDlg(
				NCLangRes.getInstance().getStrByID("4008busi",
						"UPP4008busi-000269"), alInGeneralVO, alOutGeneralVO)
				.showModal();

		if (!this.m_dlgInOut.isOK())
			return;
		try {
			filterNullLine();

			setDispenseFlag((GeneralBillVO) this.m_alListData
					.get(this.m_iLastSelListHeadRow), rownums);

			this.m_voBill = ((GeneralBillVO) ((GeneralBillVO) this.m_alListData
					.get(this.m_iLastSelListHeadRow)).clone());

			super.setButtonStatus(false);

			ctrlSourceBillButtons(true);
		} catch (Exception e) {
			handleException(e);
			MessageDialog.showErrorDlg(this, null, e.getMessage());
		}
	}

	protected void onRefInBill() {
		try {
			QryInBillDlg dlgBill = new QryInBillDlg("cgeneralhid",
					this.m_sCorpID, this.m_sUserID, "40089907", "1=1", "45",
					null, null, "4C", this);

			if (dlgBill == null) {
				return;
			}
			ICBillQuery dlgQry = new ICBillQuery(this);

			dlgQry.setTempletID(this.m_sCorpID, "40080608", this.m_sUserID,
					null, "40089907");
			dlgQry.initData(this.m_sCorpID, this.m_sUserID, "40089907", null,
					"4C", "45", null);

			if (dlgQry.showModal() == 1) {
				ConditionVO[] voCons = dlgQry.getConditionVO();

				StringBuffer sWhere = new StringBuffer(" 1=1 ");
				if ((voCons != null) && (voCons.length > 0)
						&& (voCons[0] != null)) {
					sWhere.append(" and " + dlgQry.getWhereSQL(voCons));
				}

				dlgBill.initVar("cgeneralhid", this.m_sCorpID, this.m_sUserID,
						null, sWhere.toString(), "45", null, null, "4C", null,
						this);

				dlgBill.setStrWhere(sWhere.toString());
				dlgBill.getBillVO();
				dlgBill.loadHeadData();
				dlgBill.addBillUI();
				dlgBill.setQueyDlg(dlgQry);

				GenMsgCtrl.printHint("will load qrybilldlg");
				if (dlgBill.showModal() == 1) {
					GenMsgCtrl.printHint("qrybilldlg closeok");

					AggregatedValueObject[] vos = dlgBill.getRetVos();
					GenMsgCtrl.printHint("qrybilldlg getRetVos");

					if (vos == null) {
						GenMsgCtrl.printHint("qrybilldlg getRetVos null");

						return;
					}

					GenMsgCtrl.printHint("qrybilldlg getRetVos is not null");

					AggregatedValueObject[] voRetvos = (AggregatedValueObject[]) PfChangeBO_Client
							.pfChangeBillToBillArray(vos, "45", "4C");

					GenMsgCtrl
							.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok");

					String cbiztype = null;
					if ((voRetvos != null) && (voRetvos.length > 0))
						cbiztype = (String) voRetvos[0].getParentVO()
								.getAttributeValue("cbiztype");
					setBillRefResultVO(cbiztype, voRetvos);
					if ((this.m_voBill.getItemVOs().length > 0)
							&& (this.m_voBill.getItemVOs()[0] != null)
							&& (this.m_voBill.getItemVOs()[0].getNoutnum() != null)) {
						this.m_alSerialData = this.m_voBill.getSNs();
						this.m_alLocatorData = this.m_voBill.getLocators();
					}

					GenMsgCtrl
							.printHint("qrybilldlg getRetVos pfChangeBillToBillArray ok setBillRefResultVO ok");
				}
			}

		} catch (Exception e) {
			showErrorMessage(e.getMessage());
		}
	}

	private void onFormMemo() {
		if ((this.m_iLastSelListHeadRow < 0) || (this.m_alListData == null)
				|| (this.m_alListData.size() <= this.m_iLastSelListHeadRow)
				|| (this.m_alListData.get(this.m_iLastSelListHeadRow) == null)) {
			return;
		}
		if (((GeneralBillVO) this.m_alListData.get(this.m_iLastSelListHeadRow))
				.getChildrenVO().length == 0) {
			return;
		}

		GeneralBillVO voBill = (GeneralBillVO) ((GeneralBillVO) this.m_alListData
				.get(this.m_iLastSelListHeadRow)).clone();

		voBill.setHeaderValue("coperatorid", this.m_sUserID);
		getFormMemoDlg1().setBillVO(voBill);
		getFormMemoDlg1().showModal();
	}

	public void onPrintLot() {
		showHintMessage(NCLangRes.getInstance().getStrByID("4008busi",
				"UPP4008busi-000248"));

		SCMEnv.out("打印批次汇总开始!\n");
		try {
			if ((this.m_iMode == 3) && (this.m_iCurPanel == 5)) {
				SCMEnv.out("打印批次汇总开始!表单打印!\n");

				GeneralBillVO vo = null;

				if ((this.m_iLastSelListHeadRow != -1)
						&& (null != this.m_alListData)
						&& (this.m_alListData.size() != 0)) {
					vo = (GeneralBillVO) this.m_alListData
							.get(this.m_iLastSelListHeadRow);

					if (getBillCardPanel().getHeadItem("vcustname") != null) {
						vo.setHeaderValue("vcustname", getBillCardPanel()
								.getHeadItem("vcustname").getValue());
					}
				}

				if (null == vo) {
					vo = new GeneralBillVO();
				}
				if (null == vo.getParentVO()) {
					vo.setParentVO(new GeneralBillHeaderVO());
				}
				if ((null == vo.getChildrenVO())
						|| (vo.getChildrenVO().length == 0)
						|| (vo.getChildrenVO()[0] == null)) {
					GeneralBillItemVO[] ivo = new GeneralBillItemVO[1];
					ivo[0] = new GeneralBillItemVO();
					vo.setChildrenVO(ivo);
				}

				if (getPrintEntry().selectTemplate() < 0)
					return;
				GeneralBillVO gvobak = (GeneralBillVO) vo.clone();

				DefaultVOMerger dvomerger = new DefaultVOMerger();
				dvomerger.setGroupingAttr(new String[] { "cinventoryid",
						"castunitid" });

				dvomerger.setSummingAttr(new String[] { "nshouldoutnum",
						"nshouldoutassistnum", "noutnum", "noutassistnum",
						"nmny" });

				GeneralBillItemVO[] itemvosnew = (GeneralBillItemVO[]) (GeneralBillItemVO[]) dvomerger
						.mergeByGroup(gvobak.getItemVOs());

				if (itemvosnew != null) {
					UFDouble udNum = null;
					UFDouble udMny = null;
					for (int k = 0; k < itemvosnew.length; ++k) {
						udNum = itemvosnew[k].getNoutnum();
						udMny = itemvosnew[k].getNmny();
						if ((udNum != null) && (udMny != null)) {
							itemvosnew[k].setNprice(udMny.div(udNum));
						}
						SCMEnv.out("cinventoryid:"
								+ itemvosnew[k].getCinventoryid() + "\n");

						SCMEnv.out("castunitid:"
								+ itemvosnew[k].getCastunitid() + "\n");

						SCMEnv.out("Vbatchcode:"
								+ itemvosnew[k].getVbatchcode() + "\n");

						SCMEnv.out("noutnum:" + udNum + "\n");
					}

				}

				gvobak.setChildrenVO(itemvosnew);

				getDataSource().setVO(gvobak);

				getPrintEntry().setDataSource(getDataSource());
				SCMEnv.out("打印批次汇总开始!表单打印结束!\n");
				getPrintEntry().preview();
			} else if (this.m_iCurPanel == 4) {
				SCMEnv.out("列表打印开始!\n");
				if ((null == this.m_alListData)
						|| (this.m_alListData.size() == 0)) {
					return;
				}
				if (getPrintEntry().selectTemplate() < 0)
					return;
				ArrayList alBill = getSelectedBills();

				setScaleOfListData(alBill);
				SCMEnv.out("列表打印:得到选中的单据并设置数量精度!\n");
				if (alBill == null)
					return;
				DefaultVOMerger dvomerger = null;
				for (int i = 0; i < alBill.size(); ++i) {
					SCMEnv.out("列表打印:开始合并表体行!\n");
					GeneralBillVO gvobak = (GeneralBillVO) alBill.get(i);

					dvomerger = new DefaultVOMerger();
					dvomerger.setGroupingAttr(new String[] { "cinventoryid",
							"castunitid" });

					dvomerger.setSummingAttr(new String[] { "nshouldoutnum",
							"nshouldoutassistnum", "noutnum", "noutassistnum",
							"nmny" });

					GeneralBillItemVO[] itemvosnew = (GeneralBillItemVO[]) (GeneralBillItemVO[]) dvomerger
							.mergeByGroup(gvobak.getItemVOs());

					SCMEnv.out("列表打印:得到合并后的表体行!\n");
					if (itemvosnew != null) {
						UFDouble udNum = null;
						UFDouble udMny = null;
						for (int k = 0; k < itemvosnew.length; ++k) {
							udNum = itemvosnew[k].getNoutnum();
							udMny = itemvosnew[k].getNmny();
							if ((udNum != null) && (udMny != null)) {
								itemvosnew[k].setNprice(udMny.div(udNum));
							}
							SCMEnv.out("cinventoryid:"
									+ itemvosnew[k].getCinventoryid() + "\n");

							SCMEnv.out("castunitid:"
									+ itemvosnew[k].getCastunitid() + "\n");

							SCMEnv.out("Vbatchcode:"
									+ itemvosnew[k].getVbatchcode() + "\n");

							SCMEnv.out("noutnum:" + udNum + "\n");
						}

						gvobak.setChildrenVO(itemvosnew);
						alBill.set(i, gvobak);
					}

				}

				SCMEnv.out("列表打印:得到合并后的单据!\n");
				getDataSource().setListVOs(alBill);
				getDataSource().setTotalLinesInOnePage(
						getPrintEntry().getBreakPos());

				getPrintEntry().setDataSource(getDataSource());
				getPrintEntry().preview();
			} else {
				showHintMessage(NCLangRes.getInstance().getStrByID("4008busi",
						"UPP4008busi-000249"));
			}
		} catch (Exception e) {
			SCMEnv.error(e);
			showHintMessage(NCLangRes.getInstance().getStrByID("4008busi",
					"UPPSCMCommon-000061")
					+ e.getMessage());
		}
	}

	public void onQuery() {
		try {
			Timer timer = new Timer();
			this.m_sBnoutnumnull = null;

			timer.start(NCLangRes.getInstance().getStrByID("4008busi",
					"UPP4008busi-000277"));

			int cardOrList = this.m_iCurPanel;//当前面板

			if ((this.m_bQuery) || (!this.m_bEverQry)) {//判断是查询还是刷新或者是否查询过
				getConditionDlg().showModal();//展示查询条件对话框，输入查询条件后确定进行查询
				timer.showExecuteTime("@@getConditionDlg().showModal()：");//显示查询所消耗的时间

				if (getConditionDlg().getResult() != 1) {//判断查询对话框中查询条件的个数是否等于1
					return;
				}

				this.m_bEverQry = true;//将是否曾经查询过的标识改为true，代表查询过

				setButtonStatus(true);//设置按钮的状态
			}

			QryConditionVO voCond = getQryConditionVO();//得到查询条件的vo对象

			this.m_voLastQryCond = voCond;//将查询条件得到的vo对象的内容给到最近一次查询条件得到的vo对象

			ConditionVO[] voaCond = getConditionDlg().getConditionVO();//得到查询vo的数组

			voCond.setParam(1, voaCond);

			voCond.setIntParam(0, 300);
			if (this.m_sBnoutnumnull != null) {
				voCond.setParam(33, this.m_sBnoutnumnull);
			}

			showHintMessage(NCLangRes.getInstance().getStrByID("4008busi",
					"UPP4008busi-000250"));

			timer.showExecuteTime("Before 查询：：");
			ArrayList alListData = GeneralBillHelper.queryBills(
					this.m_sBillTypeCode, voCond);
			timer.showExecuteTime("查询时间：");
			try {
				setAlistDataByFormula(20, alListData);

				timer.showExecuteTime("@@setAlistDataByFormula公式解析时间：");
				SCMEnv.out("0存货公式解析成功！");
			} catch (Exception e) {
			}
			execExtendFormula(alListData);
			if ((alListData != null) && (alListData.size() > 0)) {
				this.m_alListData = alListData;
				setListHeadData();

				if (5 == this.m_iCurPanel) {
					onSwitch();
				}

				selectListBill(0);

				this.m_iLastSelListHeadRow = 0;

				this.m_iCurDispBillNum = -1;

				this.m_iBillQty = this.m_alListData.size();

				if (this.m_iBillQty > 0) {
					showHintMessage(NCLangRes.getInstance().getStrByID(
							"4008busi",
							"UPP4008busi-000290",
							null,
							new String[] { new Integer(this.m_iBillQty)
									.toString() }));
				} else {
					showHintMessage(NCLangRes.getInstance().getStrByID(
							"4008busi", "UPP4008busi-000243"));
				}

				ctrlSourceBillUI(false);
				timer.showExecuteTime("@@将数据显示到页面时间：");
			} else {
				dealNoData();
			}

			setButtonStatus(true);

			if ((!this.m_bQuery) && (this.m_iCurPanel != cardOrList))
				onSwitch();
		} catch (Exception e) {
			handleException(e);
			showHintMessage(NCLangRes.getInstance().getStrByID("4008busi",
					"UPP4008busi-000251"));

			showWarningMessage(NCLangRes.getInstance().getStrByID("4008busi",
					"UPP4008busi-000252")
					+ e.getMessage());
		}
	}
	/**
	 * 防止出库数量超出方法
	 * @author 李江涛
	 * @param csourcebillbodyid 
	 * @Time 2014-09-18
	 * */

	public boolean FalseOK(String csourcetype, String csourcebillbodyid, UFDouble noutnum) {
		boolean ok = false;
		String sql = "";
		if ("7D".equals(csourcetype)) {//来源单据发运日计划
		sql = "select distinct (nvl(del.dnum,0) - nvl(del.doutnum,0)) as num "
				+ " from dm_delivdaypl del "
				+ "where nvl(del.dr,0)=0 and del.pk_delivdaypl = '" + csourcebillbodyid + "'";
		}else if ("7F".equals(csourcetype)) {//来源单据发运单
		sql = "select distinct (nvl(del.dsendnum,0) - nvl(del.doutnum,0)) as num "
				+ " from dm_delivdaypl del "
				+ " inner join dm_delivbill_b delb on del.pk_delivdaypl = delb.pkdayplan "
				+ " inner join ic_general_b b on delb.pk_delivbill_b = b.csourcebillbid and b.dr=0 "
				+ "where b.csourcebillbid = '" + csourcebillbodyid + "'";
		}else{
			return false;
		}
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		MapListProcessor alp = new MapListProcessor();
		ArrayList fyck = null;
		try {
			fyck = (ArrayList) query.executeQuery(
					sql.toString(), alp);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (fyck != null && fyck.size() > 0) {
			Map addrMap = (Map) fyck.get(0);
			UFDouble dsendnum1 = addrMap.get("num")==null?new UFDouble(0):new UFDouble(addrMap.get("num").toString());// 已发运数和已出库数进行减
			if (noutnum.compareTo(dsendnum1) > 0) {
				MessageDialog.showErrorDlg(this, "提示", "本次新增出库总数量"+ noutnum + "，" +
						"允许出库总数量"+ dsendnum1 + "，数量超出"+ noutnum.sub(dsendnum1) + "");
				ok = true;
			} else {
				ok = false;
			}
		} else {
			ok = false;
		}
		// 计算
		return ok;
	}
	/**
	 * @author 王凯飞
	 * @return boolean
	 * 范围:宝钢制盖
	 * 功能：判断销售订单是否为送货
	 * 日期:2015-02-06
	 * */
	private boolean checkCYSfromSO(String pk_corp) {
		boolean retuval = false;
		String csourcebillhid = getBillCardPanel().getBodyValueAt(0,"csourcebillhid") == null ? "" : getBillCardPanel()
				.getBodyValueAt(0, "csourcebillhid").toString();
		
		StringBuffer csql = new StringBuffer();
		csql.append(" select vdef16 ") 
		.append("   from so_sale ") 
		.append("  where nvl(dr, 0) = 0 ") 
		.append("    and pk_corp = '"+pk_corp+"' ") 
		.append("    and csaleid = '"+csourcebillhid+"' ") ;
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		MapListProcessor mapp= null;
		HashMap listv = null;
		  try {
			  listv = (HashMap) BGZGProxy.getIUAPQueryBS().executeQuery(csql.toString(), new MapProcessor());
		  } catch (BusinessException e) {
			  e.printStackTrace();
		  }
		String invcode = listv.get("vdef16")==null?"":listv.get("vdef16").toString();
		if(!invcode.equals("是")){
			retuval = false;
		}else{
			retuval = true;
		}
		
		return retuval;
	}
	@Override
	public boolean onSave() {
		//制盖业务变更(校验销售出库单保存时，如果销售订单'是否送货'选择为'是'，则必须选择承运商）--wkf--2015-02-06
		String pk_corp = getClientEnvironment().getCorporation().getPk_corp();
//		if("1078".equals(pk_corp) || "1100".equals(pk_corp)|| pk_corp.equals("1108")){
		//edit by zwx 制盖取消控制
		if("1100".equals(pk_corp)){
			
			boolean iscok = checkCYSfromSO(pk_corp);
			if(iscok){
				Object cys = getBillCardPanel().getHeadItem("cwastewarehouseid").getValueObject();
				if(cys.equals(null) || cys.equals("")){
					showErrorMessage("当前单据所参照的订单为送货，请选择承运商！");
					return false;
				}
			}
		}
		
		if (isBrwLendBiztype()) {
			this.m_alLocatorDataBackup = this.m_alLocatorData;
			this.m_alLocatorData = null;
			this.m_alSerialDataBackup = this.m_alSerialData;
			this.m_alSerialData = null;
		}
		// add by 李江涛 原因：防止出库数量超出 日期：2014-09-15//edit by shikun 2014-09-24
		int rw = getBillCardPanel().getRowCount();// 表体行数
		if (rw>0) {
			Boolean isold = false;//是否为修改单据
			//1、封装所有要校验的表体来源单据主键
			HashMap<String, String> listsour = new HashMap<String, String>();
			for (int i = 0; i < rw; i++) {
				String csourcebillbodyid = getBillCardPanel().getBodyValueAt(i,
						"csourcebillbid") == null ? "" : getBillCardPanel()
						.getBodyValueAt(i, "csourcebillbid").toString();
				String csourcetype = getBillCardPanel().getBodyValueAt(i,"csourcetype")==null?"":
					getBillCardPanel().getBodyValueAt(i,"csourcetype").toString();
				if (("7F".equals(csourcetype)||"7D".equals(csourcetype))&&!"".equals(csourcebillbodyid)&& !listsour.containsKey(csourcebillbodyid)) {
					listsour.put(csourcebillbodyid, csourcetype);
					String cgeneralbid = getBillCardPanel().getBodyValueAt(i,"cgeneralbid") == null ? 
							"" : getBillCardPanel().getBodyValueAt(i, "cgeneralbid").toString();
					if (!"".equals(cgeneralbid)) {
						isold = true;
					}
				}
			}
			//2、封装所有历史出库数量
			HashMap<String,UFDouble> oldlistbvos = new HashMap<String, UFDouble>();
			if (isold) {//修改单据保存
				Object cgeneralhid = getBillCardPanel().getHeadItem("cgeneralhid").getValueObject();
				String sql = "select cgeneralhid,cgeneralbid,noutnum from ic_general_b where cgeneralhid='"+cgeneralhid+"' and nvl(dr,0)=0 ";
				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
						IUAPQueryBS.class.getName());
				MapListProcessor alp = new MapListProcessor();
				ArrayList oldlist = null;
				try {
					oldlist = (ArrayList) query.executeQuery(
							sql.toString(), alp);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				if (oldlist!=null&&oldlist.size()>0) {
					for (int i = 0; i < oldlist.size(); i++) {
						Map map = (Map) oldlist.get(i);
						String cgeneralbid = map.get("cgeneralbid")==null?"":map.get("cgeneralbid").toString();
						UFDouble noutnum = map.get("noutnum")==null?new UFDouble(0):new UFDouble(map.get("noutnum").toString());
						oldlistbvos.put(cgeneralbid, noutnum);
					}
				}
			}
			//3、获取同一来源单据表体主键的实发数量和老数量的汇总数比较
			Iterator<Entry<String, String>> iter = listsour.entrySet().iterator();
			if (iter.hasNext()) {
				UFDouble noldoutnum = new UFDouble(0);//老出库数量
				UFDouble noutnum = new UFDouble(0);//新出库数量
				Entry<String, String> nextiter = iter.next();
				String csourcebillbodyid = nextiter.getKey();
				String csourcetype = listsour.get(csourcebillbodyid);
				for (int i = 0; i < rw; i++) {
					String csourcebillbid = getBillCardPanel().getBodyValueAt(i,"csourcebillbid") == null ? "" : getBillCardPanel().getBodyValueAt(i, "csourcebillbid").toString();
					if (csourcebillbodyid.equals(csourcebillbid)) {
						String cgeneralbid = getBillCardPanel().getBodyValueAt(i,"cgeneralbid") == null ? "" : getBillCardPanel().getBodyValueAt(i, "cgeneralbid").toString();
						UFDouble noldoutnumi = oldlistbvos.get(cgeneralbid)==null?new UFDouble(0):oldlistbvos.get(cgeneralbid);// 老出库数量
						UFDouble noutnumi = getBillCardPanel().getBillModel().getValueAt(i, "noutnum")==null?
								new UFDouble(0):new UFDouble(getBillCardPanel().getBillModel().getValueAt(i, "noutnum").toString());// 实发数量
						noldoutnum = noldoutnum.add(noldoutnumi);
						noutnum = noutnum.add(noutnumi);
					}
				}
				//判断同一来源单据表体主键的实发数量和老数量的大小
				if (noutnum.compareTo(noldoutnum) > 0) {
					if (FalseOK(csourcetype,csourcebillbodyid, noutnum.sub(noldoutnum))) {//传差额
						return false;
					}
				}
			}
		}
		// end 李江涛
		// 根据本次发货命令数,控制出库数量.xiaolong_fan.2012-12-06.
		int rowsNum = getBillCardPanel().getBillModel().getRowCount();
		// 实发数量
		Map numMap = new HashMap();
		// 控制数量
		Map conMap = new HashMap();
		for (int i = 0; i < rowsNum; i++) {
			// 存货编码
			Object cinventorycode = getBillCardPanel().getBillModel()
					.getValueAt(i, "cinventorycode");
			// 实发数量
			Object obj = (UFDouble) getBillCardPanel().getBillModel()
					.getValueAt(i, "noutnum");

			UFDouble noutnum = obj != null ? new UFDouble(String.valueOf(obj))
					: new UFDouble("0");

			// 控制数量
			obj = (UFDouble) getBillCardPanel().getBillModel().getValueAt(i,
					"vuserdef12");
			UFDouble connum = obj != null ? new UFDouble(String.valueOf(obj))
					: new UFDouble("0");
			if (numMap.containsKey(cinventorycode)) {
				UFDouble temp = new UFDouble(String.valueOf(numMap
						.get(cinventorycode)));
				numMap.put(cinventorycode, temp.add(noutnum));
			} else {
				numMap.put(cinventorycode, noutnum);
			}
			if (!conMap.containsKey(cinventorycode)) {
				conMap.put(cinventorycode, connum);
			}
		}
		Iterator it = numMap.keySet().iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			UFDouble cousum = (UFDouble) numMap.get(obj);
			if (conMap.containsKey(obj)) {
				UFDouble consum = (UFDouble) conMap.get(obj);
				if (consum.compareTo(new UFDouble("0")) > 0
						&& cousum.compareTo(consum) > 0) {
					showWarningMessage("存货:" + String.valueOf(obj) + "实发数量:"
							+ cousum + "大于本次发货命令数:" + consum);
					return false;
				}
			}
		}

		// 增加罐子/盖子合计数、合计垛数、发运数、可拨数,并自动统计.xiaolong_fan.2013-01-09.
		UFDouble[][] heji = {
				{ new UFDouble(0), new UFDouble(0), new UFDouble(0),
						new UFDouble(0) },
				{ new UFDouble(0), new UFDouble(0), new UFDouble(0),
						new UFDouble(0) } };
		int row = getBillCardPanel().getBillModel().getRowCount();
		Map map = new HashMap();
		// 项目主键 cwarehouseid
		String cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid")
				.getValue() == null ? "" : getBillCardPanel().getHeadItem(
				"cwarehouseid").getValue();
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select sum(nvl(ninspacenum,0)) from v_ic_onhandnum6 where cwarehouseid='"
				+ cwarehouseid + "' and cinvbasid='";
		for (int j = 0; j < row; j++) {
			// 项目主键 cinvbasid
			String cinvbasid = String.valueOf(getBillCardPanel()
					.getBodyValueAt(j, "cinvbasid"));
			// 存货编码
			String invcode = String.valueOf(getBillCardPanel().getBodyValueAt(
					j, "cinventorycode"));
			// 数量
			UFDouble num = new UFDouble(String.valueOf(getBillCardPanel()
					.getBodyValueAt(j, "noutnum") == null ? 0
					: getBillCardPanel().getBodyValueAt(j, "noutnum")));
			// 发运数项目主键 nshouldoutnum
			UFDouble fayshu = new UFDouble(String.valueOf(getBillCardPanel()
					.getBodyValueAt(j, "nshouldoutnum") == null ? 0
					: getBillCardPanel().getBodyValueAt(j, "nshouldoutnum")));
			// 可拨数
			UFDouble keboshu = new UFDouble(0);
			if (!map.containsKey(cinvbasid)) {
				Object nnumRes = null;
				try {
					nnumRes = query.executeQuery(sql + cinvbasid + "'",
							new ColumnProcessor());
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
				if (nnumRes != null) {
					keboshu = new UFDouble(String.valueOf(nnumRes));
				}
			}
			if (invcode.startsWith("22") || invcode.startsWith("21")) {// 盖子
				heji[0][0] = heji[0][0].add(num);
				// 垛数
				heji[0][1] = heji[0][1].add(1);
				heji[0][2] = heji[0][2].add(fayshu);
				heji[0][3] = heji[0][3].add(keboshu);
			} else if (invcode.startsWith("23")) {// 罐子
				heji[1][0] = heji[1][0].add(num);
				heji[1][1] = heji[1][1].add(1);
				heji[1][2] = heji[1][2].add(fayshu);
				heji[1][3] = heji[1][3].add(keboshu);
			}
		}
		// 给合计字段赋值
		getBillCardPanel().setHeadItem("gzhjs", heji[1][0]);
		m_voBill.getHeaderVO().setAttributeValue("gzhjs", heji[1][0]);
		m_voBill.getHeaderVO().setGzhjs(heji[1][0]);
		getBillCardPanel().setHeadItem("gzds", heji[1][1]);
		m_voBill.getHeaderVO().setAttributeValue("gzds", heji[1][1]);
		m_voBill.getHeaderVO().setGzds(heji[1][1]);
		getBillCardPanel().setHeadItem("gzfys", heji[1][2]);
		m_voBill.getHeaderVO().setAttributeValue("gzfys", heji[1][2]);
		m_voBill.getHeaderVO().setGzfys(heji[1][2]);
		getBillCardPanel().setHeadItem("gzkbs", heji[1][3]);
		m_voBill.getHeaderVO().setAttributeValue("gzkbs", heji[1][3]);
		m_voBill.getHeaderVO().setGzkbs(heji[1][3]);
		getBillCardPanel().setHeadItem("gghjs", heji[0][0]);
		m_voBill.getHeaderVO().setAttributeValue("gghjs", heji[0][0]);
		m_voBill.getHeaderVO().setGghjs(heji[0][0]);
		getBillCardPanel().setHeadItem("ggds", heji[0][1]);
		m_voBill.getHeaderVO().setAttributeValue("ggds", heji[0][1]);
		m_voBill.getHeaderVO().setGgds(heji[0][1]);
		getBillCardPanel().setHeadItem("ggfys", heji[0][2]);
		m_voBill.getHeaderVO().setAttributeValue("ggfys", heji[0][2]);
		m_voBill.getHeaderVO().setGgfys(heji[0][2]);
		getBillCardPanel().setHeadItem("ggkbs", heji[0][3]);
		m_voBill.getHeaderVO().setAttributeValue("ggkbs", heji[0][3]);
		m_voBill.getHeaderVO().setGgkbs(heji[0][3]);

		getBillCardPanel().updateUI();
		// getBillCardPanel().updateValue();//edit by shikun 2014-09-19
		// 此处造成billmodel的行状态为0，无法保存
		if (BillMode.New != m_iMode) {
			m_iMode = BillMode.Update;
			((GeneralBillVO) m_alListData.get(m_iLastSelListHeadRow))
					.getHeaderVO().setStatus(BillModel.MODIFICATION);
		}
		if (m_voBill.getHeaderVO().getStatus() != BillModel.ADD) {
			m_voBill.getHeaderVO().setStatus(BillModel.MODIFICATION);
			// m_voBill.getHeaderVO().getGzds();
		}
		// end by xiaolong_fan.

		return super.onSave();
	}

	public void onSNAssign() {
		if ((3 != this.m_iMode) && (isBrwLendBiztype())) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi",
					"UPP4008busi-000273"));

			return;
		}
		if (isBrwLendBiztype()) {
			GeneralBillVO voMyBill = null;
			if ((this.m_alListData != null)
					&& (this.m_iLastSelListHeadRow >= 0)
					&& (this.m_alListData.size() > this.m_iLastSelListHeadRow)
					&& (this.m_alListData.get(this.m_iLastSelListHeadRow) != null)) {
				voMyBill = (GeneralBillVO) this.m_alListData
						.get(this.m_iLastSelListHeadRow);

				String sBillPK = (String) voMyBill.getItemValue(0,
						"cfirstbillhid");

				if ((sBillPK == null) || (sBillPK.trim().length() == 0)) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"4008busi", "UPP4008busi-000274"));

					return;
				}
				showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi",
						"UPP4008busi-000275"));
			}

			return;
		}

		super.onSNAssign();
	}

	public void onSpaceAssign() {
		if ((3 != this.m_iMode) && (isBrwLendBiztype())) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi",
					"UPP4008busi-000273"));

			return;
		}
		if (isBrwLendBiztype()) {
			GeneralBillVO voMyBill = null;
			if ((this.m_alListData != null)
					&& (this.m_iLastSelListHeadRow >= 0)
					&& (this.m_alListData.size() > this.m_iLastSelListHeadRow)
					&& (this.m_alListData.get(this.m_iLastSelListHeadRow) != null)) {
				voMyBill = (GeneralBillVO) this.m_alListData
						.get(this.m_iLastSelListHeadRow);

				String sBillPK = (String) voMyBill.getItemValue(0,
						"cfirstbillhid");

				if ((sBillPK == null) || (sBillPK.trim().length() == 0)) {
					showErrorMessage(NCLangRes.getInstance().getStrByID(
							"4008busi", "UPP4008busi-000274"));

					return;
				}
				showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi",
						"UPP4008busi-000275"));
			}

			return;
		}

		super.onSpaceAssign();
	}

	GeneralBillItemVO searchInvKit(GeneralBillItemVO cvos) {
		ArrayList alInvKit = null;

		if ((cvos != null) && (cvos.getIsSet() != null)
				&& (cvos.getIsSet().intValue() == 1)) {
			return cvos;
		}
		return null;
	}

	protected void selectBillOnListPanel(int iBillIndex) {
	}

	protected void setBtnStatusSign() {
		setBtnStatusSign(true);
	}

	protected void setBtnStatusSign(boolean bUpdateButtons) {
		if ((3 != this.m_iMode) || (this.m_iLastSelListHeadRow < 0)
				|| (this.m_iBillQty <= 0)) {
			getButtonTree().getButton("签字").setEnabled(false);
			getButtonTree().getButton("取消签字").setEnabled(false);
			return;
		}
		int iSignFlag = isSigned();
		if (1 == iSignFlag) {
			getButtonTree().getButton("签字").setEnabled(false);
			getButtonTree().getButton("取消签字").setEnabled(true);

			getButtonTree().getButton("修改").setEnabled(false);
			getButtonTree().getButton("删除").setEnabled(false);
			getButtonTree().getButton("配套").setEnabled(false);
		} else if (0 == iSignFlag) {
			getButtonTree().getButton("签字").setEnabled(true);
			getButtonTree().getButton("取消签字").setEnabled(false);

			if (isCurrentTypeBill()) {
				getButtonTree().getButton("修改").setEnabled(true);
				getButtonTree().getButton("删除").setEnabled(true);
			} else {
				getButtonTree().getButton("修改").setEnabled(false);
				getButtonTree().getButton("删除").setEnabled(false);
			}

			if (5 == this.m_iCurPanel) {
				getButtonTree().getButton("配套").setEnabled(true);
			}

		} else {
			getButtonTree().getButton("签字").setEnabled(false);
			getButtonTree().getButton("取消签字").setEnabled(false);

			if (isCurrentTypeBill()) {
				getButtonTree().getButton("修改").setEnabled(true);
				getButtonTree().getButton("删除").setEnabled(true);
			} else {
				getButtonTree().getButton("修改").setEnabled(false);
				getButtonTree().getButton("删除").setEnabled(false);
			}
		}

		if (bUpdateButtons)
			updateButtons();
	}

	protected void setButtonsStatus(int iBillMode) {
		if (getButtonTree().getButton("形成代管") != null) {
			if ((iBillMode == 3) && (this.m_iBillQty > 0) && (isSigned() == 1)) {
				getButtonTree().getButton("形成代管").setEnabled(true);
			} else
				getButtonTree().getButton("形成代管").setEnabled(false);
		}

		if (getButtonTree().getButton("配套") != null) {
			if ((this.m_iCurPanel == 5) && (iBillMode == 3)
					&& (this.m_iBillQty > 0) && (isSigned() != 1)) {
				getButtonTree().getButton("配套").setEnabled(true);
			} else {
				getButtonTree().getButton("配套").setEnabled(false);
			}

		}

		if (getButtonTree().getButton("参照入库单") != null)
			if (iBillMode == 3)
				getButtonTree().getButton("参照入库单").setEnabled(true);
			else
				getButtonTree().getButton("参照入库单").setEnabled(false);
	}

	void setDispenseFlag(GeneralBillVO gvo, int[] rownums) {
		if ((gvo == null) || (gvo.getItemCount() == 0))
			return;
		ArrayList alBid = null;
		GeneralBillItemVO[] resultvos = gvo.getItemVOs();
		if (resultvos != null) {
			alBid = new ArrayList();

			for (int i = 0; i < rownums.length; ++i) {
				if (!isSetInv(gvo, rownums[i]))
					continue;
				resultvos[rownums[i]].setFbillrowflag(new Integer(3));

				alBid.add(resultvos[rownums[i]].getPrimaryKey());
			}
		}
	}

	public GeneralBillItemVO[] splitInvKit(GeneralBillItemVO itemvo,
			GeneralBillHeaderVO headervo, UFDouble nsetnum) {
		if (itemvo == null)
			return null;
		String sInvSetID = itemvo.getCinventoryid();

		if (sInvSetID != null) {
			ArrayList alInvvo = new ArrayList();
			try {
				alInvvo = GeneralBillHelper.queryPartbySetInfo(sInvSetID);
			} catch (Exception e2) {
				SCMEnv.error(e2);
			}

			if (alInvvo == null) {
				SCMEnv.out("该成套件没有配件，请检查数据库...");
				return null;
			}
			int rowcount = alInvvo.size();

			GeneralBillItemVO[] voParts = new GeneralBillItemVO[rowcount];
			UFDate db = new UFDate(this.m_sLogDate);
			for (int i = 0; i < rowcount; ++i) {
				voParts[i] = new GeneralBillItemVO();
				voParts[i].setInv((InvVO) alInvvo.get(i));
				voParts[i].setDbizdate(db);

				UFDouble nchildnum = (((InvVO) alInvvo.get(i))
						.getAttributeValue("childsnum") == null) ? new UFDouble(
						0)
						: new UFDouble(((InvVO) alInvvo.get(i))
								.getAttributeValue("childsnum").toString());

				UFDouble ntotalnum = null;
				if (nsetnum != null)
					ntotalnum = nchildnum.multiply(nsetnum);
				else
					ntotalnum = nchildnum;
				UFDouble hsl = (((InvVO) alInvvo.get(i))
						.getAttributeValue("hsl") == null) ? null
						: new UFDouble(((InvVO) alInvvo.get(i))
								.getAttributeValue("hsl").toString());

				UFDouble ntotalastnum = null;
				if ((hsl != null) && (hsl.doubleValue() != 0.0D)) {
					ntotalastnum = ntotalnum.div(hsl);
				}

				voParts[i].setAttributeValue("nshouldoutnum", ntotalnum);
				voParts[i].setAttributeValue("nshouldoutassistnum",
						ntotalastnum);

				voParts[i].setAttributeValue("noutnum", ntotalnum);
				voParts[i].setAttributeValue("noutassistnum", ntotalastnum);
				voParts[i].setCsourceheadts(null);
				voParts[i].setCsourcebodyts(null);

				voParts[i].setAttributeValue("csourcetype", headervo
						.getCbilltypecode());

				voParts[i].setAttributeValue("csourcebillhid", headervo
						.getPrimaryKey());

				voParts[i].setAttributeValue("csourcebillbid", itemvo
						.getPrimaryKey());

				voParts[i].setAttributeValue("vsourcebillcode", headervo
						.getVbillcode());

				voParts[i].setAttributeValue("creceieveid", itemvo
						.getCreceieveid());
				voParts[i].setAttributeValue("cprojectid", itemvo
						.getCprojectid());
				String s = "vuserdef";
				String ss = "pk_defdoc";
				for (int j = 0; j < 20; ++j) {
					voParts[i]
							.setAttributeValue(s + String.valueOf(j + 1),
									itemvo.getAttributeValue(s
											+ String.valueOf(j + 1)));

					voParts[i].setAttributeValue(ss + String.valueOf(j + 1),
							itemvo
									.getAttributeValue(ss
											+ String.valueOf(j + 1)));
				}

				voParts[i].setCgeneralhid(null);
				voParts[i].setCgeneralbid(null);
				voParts[i].setStatus(2);
			}
			return voParts;
		}
		return null;
	}

	public ICBcurrArithUI getCurrArith() {
		if (this.clsCurrArith == null) {
			try {
				this.clsCurrArith = new ICBcurrArithUI(ClientEnvironment
						.getInstance().getCorporation().getPrimaryKey());
			} catch (Exception e) {
				GenMethod.handleException(this, null, e);
			}
		}

		return this.clsCurrArith;
	}

	public void setListBodyData(GeneralBillItemVO[] voi) {
		if ((voi != null) && (voi.length > 0)) {
			setCurrDigit(4, (String) voi[0].getAttributeValue("cquotecurrency"));
		}
		super.setListBodyData(voi);
	}

	public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton,
			boolean bExeFormule) {
		if ((bvo != null) && (bvo.getChildrenVO() != null)
				&& (bvo.getChildrenVO().length > 0)) {
			setCurrDigit(5, (String) bvo.getChildrenVO()[0]
					.getAttributeValue("cquotecurrency"));
		}
		super.setBillVO(bvo, bUpdateBotton, bExeFormule);
	}

	public void setCurrDigit(int ipanelsatus, String cquotecurrency) {
		if ((cquotecurrency == null) || (cquotecurrency.trim().length() <= 0)) {
			return;
		}
		int iDigit = 2;
		try {
			ICBcurrArithUI currtype = getCurrArith();

			Integer Digit = currtype.getBusiCurrDigit(cquotecurrency);

			if (Digit != null) {
				iDigit = Digit.intValue();
			}
			if (ipanelsatus == 5) {
				BillItem item = getBillCardPanel().getBodyItem("nquotemny");
				if (item != null)
					item.setDecimalDigits(iDigit);
			} else {
				BillItem item = getBillListPanel().getBodyBillModel()
						.getItemByKey("nquotemny");
				if (item != null)
					item.setDecimalDigits(iDigit);
			}
		} catch (Exception e) {
			SCMEnv.error(e);
		}
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
			MessageDialog.showErrorDlg(this, "销售出库Sale of the library",
					"仓库不能为空!Warehouse can not be empty!");
			return;
		}
		if ((pilecount == 0) && !pileno.equals(""))// 垛号扫描
		{

			if (PilenoList != null || PilenoList.toArray().length > 0) {
				if (PilenoList.indexOf(pileno) >= 0) {
					MessageDialog.showErrorDlg(this, "销售出库Sale of the library",
							Transformations.getLstrFromMuiStr("跺号@Stamp&:"
									+ pileno

									+ "&已扫描过!@has scanned!"));
					return;
				}
			}

			String SQL = "select * from (  ";
			SQL += "select  kp.cinventoryid, kp.cspaceid, "
					+ "SUM ( nvl( kp.ninspacenum, 0.0 ) - nvl( kp.noutspacenum, 0.0 ) - nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0) ) num"
					+ ",kp.vbatchcode,kp.vfree1,car.csname  ";
			SQL += "from v_ic_onhandnum6 kp  ";
			SQL += "left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc   ";
			SQL += "left join bd_invbasdoc inv  on man.pk_invbasdoc = inv.pk_invbasdoc  ";
			SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid  ";
			// edit by shikun 2014-04-18 去除隔离数
			SQL += "left join ic_freeze icf on kp.cinventoryid = icf.cinventoryid ";
			SQL += "                       and icf.cwarehouseid = kp.cwarehouseid ";
			SQL += "                       and nvl(icf.cspaceid,'byzgyh') = nvl(kp.cspaceid,'byzgyh') ";
			SQL += "                       and icf.pk_corp = kp.pk_corp ";
			SQL += "                       and nvl(kp.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ";
			SQL += "                       and nvl(kp.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ";
			SQL += "                       and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ";
			// end shikun
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
				MessageDialog.showErrorDlg(this, "销售出库Sale of the library",
						"跺号Stamp:" + pileno + "不存在!Does not exist");
				return;
			} else if (invidinfo != null) {
				if (!invidinfo.containsKey(cinventoryid)) {
					MessageDialog
							.showErrorDlg(
									this,
									"销售出库Sale of the library",
									Transformations
											.getLstrFromMuiStr("当前垛号@Stamp&:"
													+ pileno
													+ "&对应的存货不属于本次出库范围!@corresponding to the inventory does not belong to the scope of delivery!"));
					return;
				}

			}
			int Sindex = GetStartIndex(cinventoryid);
			String cinventorycode = String.valueOf(getBillCardPanel()
					.getBodyValueAt(Sindex, "cinventorycode"));
			StampCount = new UFDouble("0");
			if (cinventorycode.substring(0, 2).equals("23")) {
				String sc = String.valueOf(getBillCardPanel().getHeadItem(
						"vuserdef11").getValueObject());
				if (sc == null || sc.equals("") || sc.equals("null")) {
					MessageDialog
							.showErrorDlg(this, "销售出库Sale of the library",
									"整垛数量不能为空!The entire stack Quantity can not be empty!");
					return;
				}
				int strIndex = sc.indexOf(".");
				StampCount = strIndex >= 0 ? new UFDouble(sc.substring(0,
						strIndex)) : new UFDouble(sc);
				if (num.compareTo(StampCount) > 0) {
					if (MessageDialog
							.showYesNoDlg(
									this,
									"销售出库Sale of the library",
									"当前垛号的整垛数量已超过该客户所要求的整垛数量，可联系销售人员修改收货单位整垛数量或重新下达订单,是否继续？Entire stack number of the current stack the entire stack more than the number required by the customers can contact the sales staff to modify the entire stack number of the receiving unit, or place orders again, whether to continue?") == 8)
						return;
				}
			}

			int row = GetInvIDRowRelation(cinventoryid);
			int recordcount = getBillCardPanel().getBillTable().getRowCount();

			int Eindex = GetEndIndex(cinventoryid);
			UFDouble nshououtnum = (UFDouble) getBillCardPanel()
					.getBodyValueAt(Sindex, "nshouldoutnum");
			String oldpileno = String.valueOf(getBillCardPanel()
					.getBodyValueAt(Eindex, "vfree0"));
			if ((oldpileno == null) || (oldpileno.equals(""))
					|| (oldpileno.equals("null"))) {
				// getBillCardPanel().getBillModel().setEditRow(newRow)
				SetBodyNewValue(pileno, num, vbatchcode, csname, cspaceid,
						Eindex, nshououtnum);
				String key = "";
				try {
					key = this.m_voBill.getChildrenVO()[Eindex].getPrimaryKey();
				} catch (BusinessException e) {
					// TODO Auto-generated catch block

				}
				if (key != null && !key.equals("") && !key.equals("null")) {
					getBillCardPanel().getBillModel().setRowState(Eindex,
							BillModel.MODIFICATION);
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

				// add by shikun 2014-09-18 单据保存后修改扫描垛号，复制行没有将单据行状态改为NEW=2
				newbi.setStatus(VOStatus.NEW);
				newbi.setCgeneralbid(null);
				newbi.setCgeneralhid(null);
				newbi.setTs(null);
				// end shikun

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
				getBillCardPanel().getBillModel().setRowState(Eindex + 1,
						BillModel.ADD);// add by shikun 2014-09-19 行状态为新增
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
				MessageDialog.showErrorDlg(this, "销售出库Sale of the library",
						"请选择数据!Please select the data!");
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
				MessageDialog
						.showErrorDlg(
								this,
								"销售出库Sale of the library",
								Transformations
										.getLstrFromMuiStr("当前出库垛数已等于@The library stack number is equal to current&"
												+ String.valueOf(pilecount)
												+ "!"));
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
			if (cinventorycode.substring(0, 2).equals("23")) {
				String sc = String.valueOf(getBillCardPanel().getHeadItem(
						"vuserdef11").getValueObject());
				if (sc == null || sc.equals("") || sc.equals("null")) {
					MessageDialog
							.showErrorDlg(this, "销售出库Sale of the library",
									"整垛数量不能为空!The entire stack Quantity can not be empty!");
				}
				int strIndex = sc.indexOf(".");
				StampCount = strIndex >= 0 ? new UFDouble(sc.substring(0,
						strIndex)) : new UFDouble(sc);
				ischekcount = true;
			}
			String SQL = "select * from (  ";
			SQL += "select  kp.cinventoryid, kp.cspaceid, "
					+ "SUM ( nvl( kp.ninspacenum, 0.0 ) - nvl( kp.noutspacenum, 0.0 )- nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0) ) num"
					+ ",kp.vbatchcode,kp.vfree1,car.csname   ";
			SQL += "from v_ic_onhandnum6 kp  ";
			SQL += "left join bd_invmandoc man on kp.cinventoryid = man.pk_invmandoc   ";
			SQL += "left join bd_invbasdoc inv  on man.pk_invbasdoc = inv.pk_invbasdoc  ";
			SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid  ";
			// edit by shikun 2014-04-18 去除隔离数
			SQL += " left join ic_freeze icf on kp.cinventoryid = icf.cinventoryid ";
			SQL += "                       and icf.cwarehouseid = kp.cwarehouseid ";
			SQL += "                       and nvl(icf.cspaceid,'byzgyh') = nvl(kp.cspaceid,'byzgyh') ";
			SQL += "                       and icf.pk_corp = kp.pk_corp ";
			SQL += "                       and nvl(kp.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ";
			SQL += "                       and nvl(kp.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ";
			SQL += "                       and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')=''  ";
			// end shikun
			SQL += "where kp.cinventoryid='"
					+ m_cinventoryid
					+ "' and kp.pk_corp='"
					+ getClientEnvironment().getInstance().getCorporation()
							.getPrimaryKey()
					+ "'  and kp.cwarehouseid='"
					+ Wh
					+ "'  and (nvl(kp.cspaceid,'')<>'_________N/A________' or kp.cspaceid is null)  ";
			SQL += "group by kp.pk_corp, kp.ccalbodyid, kp.cwarehouseid, kp.cinventoryid, kp.vbatchcode,kp.cspaceid ,kp.vfree1,car.csname   ";
			SQL += " order by  kp.vfree1   ";
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
				MessageDialog.showErrorDlg(this, "销售出库Sale of the library",
						Transformations
								.getLstrFromMuiStr("存货编码@Inventory coding&"
										+ cinventorycode
										+ "&现存量不足!@inadequate existing!"));
			}
			if (newcount < pilecount) {
				MessageDialog
						.showErrorDlg(
								this,
								"销售出库Sale of the library",
								Transformations
										.getLstrFromMuiStr("出库垛数@The number of a library stack&"
												+ String.valueOf(OldCount)
												+ "&大于现存量@Greater than the existing volume&"
												+ String.valueOf(newcount)
												+ "&垛 @Pile!"));
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
					if (ischekcount) {
						if (num.compareTo(StampCount) > 0) {
							if (MessageDialog
									.showYesNoDlg(
											this,
											"销售出库Sale of the library",
											"当前垛号的整垛数量已超过该客户所要求的整垛数量，是否继续？Entire stack number of the current stack the entire stack more than the number required by the customers can contact the sales staff to modify the entire stack number of the receiving unit, or place orders again, whether to continue") == 8)
								return;
						}
					}
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
					String key = "";
					try {
						key = this.m_voBill.getChildrenVO()[j].getPrimaryKey();
					} catch (BusinessException e) {
						// TODO Auto-generated catch block

					}
					if (key != null && !key.equals("") && !key.equals("null")) {
						getBillCardPanel().getBillModel().setRowState(Eindex,
								BillModel.MODIFICATION);
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
			int endIndex = m_Pileno.indexOf("]");

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

		String Stkey = getClientEnvironment().getInstance().getCorporation()
				.getFathercorp();
		try {
			CorpVO CorpVO = CorpBO_Client.findByPrimaryKey(Stkey);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		// setSpace(cspaceid, csname, sindex);
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
		if (cspaceid != null && !cspaceid.equals("")
				&& !cspaceid.equals("null")
				&& !cspaceid.equals("_________N/A________")) {
			getBillCardPanel().setBodyValueAt(csname, sindex, "vspacename");
			getBillCardPanel().setBodyValueAt(cspaceid, sindex, "cspaceid");
			setSpace(cspaceid, csname, sindex);
		}
		BillEditEvent e4 = new BillEditEvent(getBillCardPanel().getBodyItem(
				"nshouldoutnum").getComponent(), nshououtnum, "nshouldoutnum",
				sindex);
		afterShouldNumEdit(e4);

		if (vbatchcode != null && !vbatchcode.equals("")
				&& !vbatchcode.equals("null")
				&& !vbatchcode.equals("_________N/A________")) {
			getBillCardPanel().setBodyValueAt(vbatchcode, sindex, "vbatchcode");
			((LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode")
					.getComponent()).setText(vbatchcode);
			((LotNumbRefPane) getBillCardPanel().getBodyItem("vbatchcode")
					.getComponent()).setValue(vbatchcode);
			BillEditEvent e1 = new BillEditEvent(getBillCardPanel()
					.getBodyItem("vbatchcode").getComponent(), vbatchcode,
					"vbatchcode", sindex);
			afterLotEdit(e1);
		}
		// pickupLotRef("vbatchcode");
		getBillCardPanel().setBodyValueAt(num, sindex, "noutnum");
		BillEditEvent e2 = new BillEditEvent(getBillCardPanel().getBodyItem(
				"noutnum").getComponent(), num, "noutnum", sindex);
		afterEdit(e2);
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
	public boolean Iscsflag(String primkey) {
		boolean rst = false;
		String SQL = "select csflag from bd_stordoc  where pk_stordoc ='"
				+ primkey + "'";
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance()
				.lookup(IUAPQueryBS.class.getName());
		List list = null;
		try {
			list = (List) sessionManager.executeQuery(SQL,
					new ArrayListProcessor());

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
						return rst = (new UFBoolean(String.valueOf(values
								.get(0)))).booleanValue();
					}
				}
			}
		} catch (BusinessException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return rst;
	}

	public void onHandnumQuery() {
		// GeneralBillVO header = (DelivbillHHeaderVO)
		// m_currentbill.getParentVO();

		// ToftPanel toftpanel = SFClientUtil.showNode("40083004",
		// IFuncWindow.WINDOW_TYPE_DLG);
		// nc.ui.ic.ic602.ClientUI ui = (nc.ui.ic.ic602.ClientUI)toftpanel;
		// ui.setGvo(m_currentbill);
		// ui.RefQuery();

		UITable table = this.getBillCardPanel().getBillTable();
		int[] rows = table.getSelectedRows();
		if (rows.length < 1) {
			MessageDialog.showErrorDlg(this, "提示Prompt",
					"请选择表体行数据！Select the table body line data!");
			return;
		}

		ArrayList invid = new ArrayList();
		for (int i = 0; i < rows.length; i++) {
			String Invid = String.valueOf(this.getBillCardPanel()
					.getBillModel().getValueAt(rows[i], "cinventoryid"));
			if (invid.indexOf(Invid) < 0) {
				invid.add(Invid);
			}
		}
		String[] pkInv = new String[invid.size()];
		for (int i = 0; i < invid.size(); i++) {
			pkInv[i] = String.valueOf(invid.get(i));
		}
		nc.ui.dm.dm104.QueryXcl dialog = new nc.ui.dm.dm104.QueryXcl(
				getBillCardPanel(), pkInv);
		dialog.showModal();
	}

	protected void setButtons() {
		if (getParentCorpCode().equals("10395")) {
			ButtonObject[] o_bo = getButtonTree().getButtonArray();
			ButtonObject[] bo = new ButtonObject[o_bo.length + 2];
			for (int i = 0; i < o_bo.length; i++) {
				bo[i] = o_bo[i];
			}
			bo[o_bo.length] = new ButtonObject(Transformations
					.getLstrFromMuiStr("现存量打印", "OnHandNumPrint"),
					Transformations
							.getLstrFromMuiStr("现存量打印", "OnHandNumPrint"),
					"onHandQuery");
			bo[o_bo.length + 1] = new ButtonObject("合并打印", "合并打印",
					"onMergerQuery");
			setButtons(bo);
		} else
			super.setButtons();
	}

	public boolean StringIsNullOrEmpty(Object obj) {
		return obj == null ? true : String.valueOf(obj).equals("") ? true
				: String.valueOf(obj).equalsIgnoreCase("null") ? true : false;
	}

	protected String m_CurrentPanel = "LISTPANEL";

	/**
	 * 合并查询 by cm 2012-11-12
	 */
	public void onMergerQueryPrint() {
		if (this.m_iCurPanel == BillMode.List) {
			// MessageDialog.showErrorDlg(this, "提示", "列表状态");
			int selrow = getBillListPanel().getHeadTable().getSelectedRow();
			GeneralBillVO billvo = new GeneralBillVO();
			if (selrow >= 0) {
				billvo.setParentVO(getBillListPanel().getHeadBillModel()
						.getBodyValueRowVO(selrow,
								GeneralBillHeaderVO.class.getName()));
				billvo.setChildrenVO(getBillListPanel().getBodyBillModel()
						.getBodyValueVOs(GeneralBillItemVO.class.getName()));
			}
			GeneralBillVO gbvo = new GeneralBillVO();
			gbvo = getItemVO(billvo);
			if (gbvo != null) {
				try {
					printOnList1(gbvo);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (this.m_iCurPanel == BillMode.Card) {
			// MessageDialog.showErrorDlg(this, "提示", "卡片状态");
			// GeneralBillVO billvo = (GeneralBillVO)
			// getBillCardPanel().getBillData().getBillValueVO(GeneralBillVO.class.getName(),
			// GeneralBillHeaderVO.class.getName(),
			// GeneralBillItemVO.class.getName());
			GeneralBillVO billvo = null;
			if ((this.m_iLastSelListHeadRow != -1)
					&& (this.m_alListData != null)
					&& (this.m_alListData.size() != 0)) {
				billvo = (GeneralBillVO) this.m_alListData
						.get(this.m_iLastSelListHeadRow);
			}
			GeneralBillVO gbvo = new GeneralBillVO();
			gbvo = getItemVO(billvo);
			if (gbvo != null) {
				if (getPrintEntry().selectTemplate() < 0) {
					showHintMessage(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("4008bill", "UPP4008bill-000048")/*
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
		if ((vo == null) || (vo.getHeaderVO().getCgeneralhid() == null)
				|| (vo.getItemVOs() == null)) {
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
			noutnum = itemvos[i].getNoutnum() == null ? new UFDouble(0)
					: itemvos[i].getNoutnum();
			String snoutnum = noutnum.toString();
			if (snoutnum.indexOf(".") == -1) {
				snoutnum = snoutnum + ".00000000";
			}
			UFDouble vnshouldoutassistnum = itemvos[i].getNshouldoutassistnum() == null ? new UFDouble(
					0)
					: itemvos[i].getNshouldoutassistnum();
			UFDouble vnshouldoutnum = itemvos[i].getNshouldoutnum() == null ? new UFDouble(
					0)
					: itemvos[i].getNshouldoutnum();
			UFDouble vnoutassistnum = itemvos[i].getNoutassistnum() == null ? new UFDouble(
					0)
					: itemvos[i].getNoutassistnum();
			UFDouble vnoutnum = itemvos[i].getNoutnum() == null ? new UFDouble(
					0) : itemvos[i].getNoutnum();
			String vuserdef9 = itemvos[i].getVuserdef7() == null ? "0"
					: itemvos[i].getVuserdef7();
			UFDouble vnmny = itemvos[i].getNmny() == null ? new UFDouble(0)
					: itemvos[i].getNmny();
			String rows = itemvos[i].getVuserdef8();

			// if ((stype == null) || (invid == null))// || (vbatchcode ==
			// null))
			// continue;
			// showErrorMessage(noutnum.toString()+"==============");
			int rowswl = 1;
			if (!hashmap.containsKey(invid + vbatchcode + snoutnum)) {
				hashmap.put(invid + vbatchcode + snoutnum, itemvos[i]);
				// showErrorMessage(invid+vbatchcode+noutnum.toString());
				// showErrorMessage(noutnum.toString());
			} else {
				GeneralBillItemVO hashm = hashmap.get(invid + vbatchcode
						+ snoutnum);
				int rowi = Integer.parseInt(hashm.getVuserdef8()) + 1;
				UFDouble vnshouldoutassistnum1 = hashm.getNshouldoutassistnum() == null ? new UFDouble(
						0)
						: hashm.getNshouldoutassistnum();
				UFDouble vnshouldoutnum1 = hashm.getNshouldoutnum() == null ? new UFDouble(
						0)
						: hashm.getNshouldoutnum();
				UFDouble vnoutassistnum1 = hashm.getNoutassistnum() == null ? new UFDouble(
						0)
						: hashm.getNoutassistnum();
				UFDouble vnoutnum1 = hashm.getNoutnum() == null ? new UFDouble(
						0) : hashm.getNoutnum();
				UFDouble vnmny1 = hashm.getNmny() == null ? new UFDouble(0)
						: hashm.getNmny();
				String vuserdef91 = hashm.getVuserdef7() == null ? "0" : hashm
						.getVuserdef7();
				// UFDouble vnshouldoutnum1 =
				// itemvos[i].getNshouldoutnum()==null?new
				// UFDouble(0):itemvos[i].getNshouldoutnum();
				// UFDouble vnoutassistnum1 =
				// itemvos[i].getNoutassistnum()==null?new
				// UFDouble(0):itemvos[i].getNoutassistnum();
				// UFDouble vnoutnum1 = itemvos[i].getNoutnum()==null?new
				// UFDouble(0):itemvos[i].getNoutnum();
				// UFDouble vnmny1 = itemvos[i].getNmny()==null?new
				// UFDouble(0):itemvos[i].getNmny();
				hashm.setNshouldoutassistnum(vnshouldoutassistnum1
						.add(vnshouldoutassistnum));
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
		Set<String> set = hashmap.keySet();// 取出所有键
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

		// Iterator<String> iter = hashmap.keySet().iterator();
		// while (iter.hasNext()) {
		// gbitemVO[j] = hashmap.get(iter.next());
		// j++;
		// }
		for (int k = 0; k < gbitemVO.length; k++) {
			// String vuserdef9 =
			// gbitemVO[k].getVuserdef9()==null?"0":gbitemVO[k].getVuserdef9();
			String vuserdef8 = gbitemVO[k].getVuserdef8() == null ? "0"
					: gbitemVO[k].getVuserdef8();
			UFDouble vnoutnum = gbitemVO[k].getNoutnum() == null ? new UFDouble(
					0)
					: gbitemVO[k].getNoutnum();
			gbitemVO[k].setNoutnum(vnoutnum.div(new UFDouble(vuserdef8)));
		}
		// if(this.m_iCurPanel==BillMode.List){
		// for (int k = 0; k < row; k++) {
		// clearRowData(k);
		// }
		// getBillListPanel().getBodyBillModel().setBodyDataVO(gbitemVO);
		// getBillListPanel().getBodyBillModel().execLoadFormula();
		// }
		// if(this.m_iCurPanel==BillMode.Card){
		// getBillCardPanel().getBillModel().setBodyDataVO(gbitemVO);
		// getBillCardPanel().getBillModel().execLoadFormula();
		// }
		hashmap.clear();
		GeneralBillVO gbvo = new GeneralBillVO();
		gbvo.setParentVO(vo.getParentVO());
		gbvo.setChildrenVO(gbitemVO);
		return gbvo;
	}

	protected void printOnCard1(GeneralBillVO gbvo, boolean isPreview) {
		// GeneralBillVO voBill = new GeneralBillVO();
		// voBill = getItemVO(gbvo);
		// GeneralBillItemVO[] voBillb = get
		// for (int i = 0; i < voBill..length(); i++) {
		//					
		// }
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

		GeneralBillItemVO[] vobillb = (GeneralBillItemVO[]) gbvo
				.getChildrenVO();
		// GeneralBillHeaderVO vbillh = (GeneralBillHeaderVO)gbvo.getParentVO();
		// showErrorMessage(vbillh.getCcustomername());
		for (int i = 0; i < vobillb.length; i++) {
			String vdef8 = vobillb[i].getVuserdef8();
			String vdef9 = vobillb[i].getVuserdef7();
			UFDouble noutnum = vobillb[i].getNoutnum();
			if (vdef8.endsWith("1")) {
				vobillb[i].setVuserdef7(noutnum.toString());
				vobillb[i].setNcountnum(new UFDouble(vdef8));
			}
			vobillb[i].setVuserdef7(noutnum.multiply(new UFDouble(vdef8))
					.toString());
		}

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

	protected void printOnList1(GeneralBillVO alBill)
			throws InterruptedException {
		nc.ui.pub.print.PrintEntry pe = getPrintEntryNew();

		if (pe.selectTemplate() < 0) {
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000048")/*
													 * @res "请先定义打印模板。"
													 */);
			return;
		}

		pe.beginBatchPrint();

		PrintDataInterface ds = null;
		// GeneralBillVO voBill = null;
		// 单据主表
		GeneralBillHeaderVO headerVO = null;
		// nc.vo.scm.print.PrintResultVO printResultVO = null;

		nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
		plc.setBatchPrint(true);// 设置是批打
		// 设置打印监听
		pe.setPrintListener(plc);
		plc.setPrintEntry(pe);
		plc.addFreshTsListener(new FreshTsListener());
		GeneralBillItemVO[] vobillb = (GeneralBillItemVO[]) alBill
				.getChildrenVO();
		for (int j = 0; j < vobillb.length; j++) {
			String vdef8 = vobillb[j].getVuserdef8();
			String vdef9 = vobillb[j].getVuserdef7();
			UFDouble noutnum = vobillb[j].getNoutnum();
			if (vdef8.endsWith("1")) {
				vobillb[j].setVuserdef7(noutnum.toString());
				vobillb[j].setNcountnum(new UFDouble(vdef8));
			}
			vobillb[j].setVuserdef7(noutnum.multiply(new UFDouble(vdef8))
					.toString());
		}
		alBill.setChildrenVO(vobillb);
		// 打印操作
		for (int i = 0; i < alBill.getChildrenVO().length; i++) {
			// alBill = alBill;
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
				// while (pe.dsCountInPool() > PrintConst.PL_MAX_TAST_NUM) {
				// Thread.currentThread().sleep(PrintConst.PL_SLEEP_TIME); //
				// 如果有PL_MAX_TAST_NUM个以上任务，就等待PL_SLEEP_TIME秒。
				// }
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