package nc.ui.ic.ic212;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.scm.pub.redun.RedunSourceDlg;
import nc.ui.sp.pub.ShowMsgDlg;
import nc.vo.bd.CorpVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.fts.pub.exception.FTSBusinessException;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * 材料出库
 * */
public class ClientUI extends GeneralBillClientUI {
	private RatioOUTStructureDlg ivjRatioOUTStructureDlg = null;
	public String m_strProLine = new String();// 自定义变量 2012/7/31

	public ClientUI() {
		initialize();
		//add by xiaolong_fan.为配合刷卡功能,宝翼制罐下隐藏保存和签字按钮. No 5
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		String usercode = ClientEnvironment.getInstance().getUser().getUserCode();
		if ("1016".equals(pk_corp) && (!"zip".equals(usercode) && !"db".equals(usercode) && !"dzy".equals(usercode) && !"ltb".equals(usercode))) {
//			getButtonTree().getButton("保存").setVisible(false);//EDIT BY SHIKUN 2014-04-29 受控件类型的材料出库单，刷卡无效，暂时解封保存按钮
//			getButtonTree().getButton("签字").setVisible(false);
		}
		//end by xiaolong_fan.
	}

	/**
	 * 读卡线程
	 */
	Thread readcard = null;
	/**
	 * 线程关闭标示
	 */
	volatile boolean isclose = false;
	/**
	 * 句柄标示
	 */
	int icdev = -1;

	/**
	 * 读卡线程
	 * @param bo
	 */
	private void readCard(ButtonObject bo) {
		Runnable run = new Runnable() {
			public void run() {
				try {
					Object obj = null;
					String cardnumber = null;
					TestConn conn = new TestConn();
					if (icdev < 0) {
						for (int i = 0; i < 4; i++) {
							icdev = TestConn.TestConnect(i, 9600);
							if (icdev > 0) break;
						}
						System.out.println("handle===" + icdev);
						if (icdev > 0) {
							conn.testBeep(icdev, 500);
						}
					}
					isclose = false;
					while (true) {
						if (isclose) {
							conn.exit(icdev);
							icdev = -1;
							break;
						}
						Thread.sleep(100);
						cardnumber = String.valueOf(conn.card(icdev, 1));
						if ("1".equals(cardnumber) || "-132".equals(cardnumber)) {
							continue;
						} else {
							conn.testBeep(icdev, 500);
						}
						try {
							obj = getUAPQuery().executeQuery("select pk_psndoc,psncode  from bd_psndoc a where nvl(a.dr,0)=0 and a.def1='" + cardnumber + "'", new MapListProcessor());
						} catch (FTSBusinessException e) {
							e.printStackTrace();
						} catch (BusinessException e) {
							e.printStackTrace();
						}
						if (obj == null) {
							continue;
						}
						String psndoc = null;
						String psncode = "";
						if (obj != null && obj instanceof ArrayList) {
							ArrayList<HashMap<String, Object>> mapList = (ArrayList<HashMap<String, Object>>) obj;
							int size = mapList.size();
							for (int i = 0; i < size; i++) {
								Map map = mapList.get(i);
								psndoc = (String) map.get("pk_psndoc");
								psncode = psncode + " " + map.get("psncode");
								if (i == size - 1 && size > 1) {
									MessageDialog.showErrorDlg(ClientUI.this, "错误", "您所刷的IC卡绑定了多个人员，人员编码：" + psncode);
									psndoc = null;
									continue;
								}
							}
						}
						if (psndoc == null || "".equals(psndoc)) {
							continue;
						}
						try {
							if (getButtonTree().getButton("保存").isEnabled()) {
								getBillCardPanel().setHeadItem("cbizid", psndoc);
								BillEditEvent event = new BillEditEvent(getBillCardPanel().getHeadItem("cbizid"), obj, "cbizid");
								afterEdit(event);
								onButtonClicked(getButtonTree().getButton("保存"));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (!getButtonTree().getButton("保存").isEnabled() && getButtonTree().getButton("签字").isEnabled()) {
							onButtonClicked(getButtonTree().getButton("签字"));
							if (!getButtonTree().getButton("签字").isEnabled()) {
								conn.exit(icdev);
								icdev = -1;
								break;
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		if (bo.getParent() == getButtonTree().getButton("增加") || bo == getButtonTree().getButton("修改")) {
			readcard = new Thread(run);
			readcard.setDaemon(true);
			readcard.start();
		}

	}

	@Override
	// eric
	public boolean beforeEdit(BillItemEvent e) {
		if (e.getItem().getKey().equals("vuserdef14")) {// 产品线
			UIRefPane pane = (UIRefPane) ((BillItem) e.getSource()).getComponent();
			pane.getRefModel().addWherePart(" and pd_wk.pk_corp = '" + getCorpPrimaryKey() + "' ");
		}
		if (e.getItem().getKey().equals("vuserdef15")) {// 产品线
			UIRefPane pane = (UIRefPane) ((BillItem) e.getSource()).getComponent();

			pane.getRefModel().addWherePart(" and  mm_mo.pk_moid in (select pk_moid from mm_mokz where gzzxid = '" + ((UIRefPane) getBillCardPanel().getHeadItem("vuserdef14").getComponent()).getRefPK() + "') ");
			// n(select pk_moid from mm_mokz where gzzxid
		}
		// (UIRefPane)getBillCardPanel().getHeadItem("vuserdef15").getComponent();
		return super.beforeEdit(e);
	}

	public ClientUI(String pk_corp, String billType, String businessType, String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);
	}

	private void initRefWork() {
		String pkCalbody = ((UIRefPane) getBillCardPanel().getHeadItem("pk_calbody").getComponent()).getRefPK();
		UIRefPane uiRef = (UIRefPane) getBillCardPanel().getBodyItem("cworkcentername").getComponent();
		UIRefPane uiRefTL = (UIRefPane) getBillCardPanel().getBodyItem("cworksitename").getComponent();

		String whereWork = null;
		String whereTL = null;
		if (pkCalbody == null) whereWork = " and pd_wk.pk_corp='" + this.m_sCorpID + "'";
		else {
			whereWork = " and pd_wk.pk_corp='" + this.m_sCorpID + "'" + " and pd_wk.gcbm=" + "'" + pkCalbody + "'";
		}
		if (pkCalbody == null) whereTL = " and pd_tld.pk_corp='" + this.m_sCorpID + "'";
		else {
			whereTL = " and pd_tld.pk_corp='" + this.m_sCorpID + "'" + " and pd_tld.gcbm=" + "'" + pkCalbody + "'";
		}
		uiRef.getRefModel().setWherePart(uiRef.getRefModel().getWherePart() + whereWork);
		uiRefTL.getRefModel().setWherePart(uiRefTL.getRefModel().getWherePart() + whereTL);
	}

	protected void afterBillEdit(BillEditEvent e) {
	}

	protected void afterBillItemSelChg(int iRow, int iCol) {
	}

	public void afterEdit(BillEditEvent e) {
		int row = e.getRow();
		int pos = e.getPos();

		String sItemKey = e.getKey();
		
		//材料出库_扫描批次号
		String pk_corp = getClientEnvironment().getCorporation().getPrimaryKey().toString();
		if(pk_corp.equals("1078") || pk_corp.equals("1100")|| pk_corp.equals("1108")){
			if(sItemKey.equals("jhsm")){
				if (!e.getValue().toString().equals("")) {
					addData(e.getValue().toString(), 0);
				}
				getBillCardPanel().getHeadItem("jhsm").setValue("");
				for (int i = 0; i < getBillCardPanel().getBillTable()
						.getRowCount(); i++) {
					getBillCardPanel()
					.setBodyValueAt((i + 1) * 10, i, "crowno");
				}
				getBillCardPanel().getHeadItem("jhsm").getComponent()
				.requestFocusInWindow();
//			String vbatchcode = getBillCardPanel().getHeadItem("vnote").getValueObject().toString();
//			addData(vbatchcode);
			}
		}
		
		if (((pos == 1) && (row < 0)) || (sItemKey == null) || (sItemKey.length() == 0)) { return; }
		/**
		 * @功能:根据产品线、生产订单号 带出BOM子项出库
		 * @author ：林桂莹
		 * @2012/9/5
		 * 
		 * @since v50
		 */
		if (getParentCorpCode().equals("10395")) {
			StringBuffer Sql = new StringBuffer();
			String WhereSql = new String();
			String wlbmid = new String();
			String wlbmcode = new String();
			String version = new String();
			String invclasslev = new String();
			String invclassname = new String();
			String invclasscode = new String();
			String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			String wlbmname = new String();
			String jhwgsl = new String();
			String cdispatchercode = ((UIRefPane) getBillCardPanel().getHeadItem("cdispatcherid").getComponent()).getRefCode();
			cdispatchercode = cdispatchercode == null ? "" : cdispatchercode;
			// 产品线
			if (sItemKey.equals("vuserdef14")) {
				setProLine("");
				if (getBillCardPanel().getHeadItem(sItemKey.toString()).getValueObject().toString().equals("")) {
					return;
				} else if (!getBillCardPanel().getHeadItem(sItemKey.toString()).getValueObject().toString().equals("")) {
					setProLine(((UIRefPane) getBillCardPanel().getHeadItem("vuserdef14").getComponent()).getRefPK());
				}

				WhereSql = "select * from(select a.bomver,a.scddh,a.pk_moid,a.sjkgrq||' '||a.sjkssj as kgrq,a.wlbmid,h.invclassname,h.invclasscode,h.invclasslev,f.invcode,f.invname,a.jhwgsl   ";
				WhereSql = WhereSql + "from mm_mo  a left  join mm_mokz c on a.pk_moid=c.pk_moid  left join pd_wk b on c.gzzxid =B.PK_WKID  left join bd_invbasdoc f on f.pk_invbasdoc=a.wlbmid left join bd_invcl h  on f.pk_invcl=h.pk_invcl ";
				WhereSql = WhereSql + "where B.PK_CORP ='" + pkCorp + "'and a.dr=0  and zt='B' and (a.bomver is not null or a.bomver<>'') ";
				if (!getProLine().equals("")) {
					WhereSql += " and c.gzzxid ='" + getProLine() + "'";
				}
				WhereSql += "	order by sjkgrq||' '||sjkssj ) where rownum=1";
			}
			// 生产订单号
			if (sItemKey.equals("vuserdef15")) {
				if (getBillCardPanel().getHeadItem("vuserdef15") == null) {
					return;
				} else if (getBillCardPanel().getHeadItem("vuserdef15").getValueObject().toString().equals("")) { return; }
				WhereSql = "select a.bomver,a.scddh,a.pk_moid,a.sjkgrq||' '||a.sjkssj as kgrq,a.wlbmid,h.invclassname,h.invclasscode,h.invclasslev,f.invcode,f.invname,a.jhwgsl   ";
				WhereSql = WhereSql + "from mm_mo  a left  join mm_mokz c on a.pk_moid=c.pk_moid  left join pd_wk b on c.gzzxid =B.PK_WKID  left join bd_invbasdoc f on f.pk_invbasdoc=a.wlbmid left join bd_invcl h  on f.pk_invcl=h.pk_invcl  ";
				WhereSql = WhereSql + "where B.PK_CORP ='" + pkCorp + "'and a.dr=0 and zt='B' and (a.bomver is not null or a.bomver<>'') and a.scddh='" + getBillCardPanel().getHeadItem("vuserdef15").getValueObject().toString() + "' ";

				WhereSql += " order by sjkgrq||' '||sjkssj ";
			}

			if (sItemKey.equals("vuserdef14") || (sItemKey.equals("vuserdef15"))) {

				Sql.append("select j.pk_invmandoc,c.invcode,C.invname ,f.pk_measdoc, f.shortname ,f.measname,c.invspec,c.invtype,a.sl, c.pk_invbasdoc  ");
				Sql.append("from bd_bom_b a  ");
				Sql.append("left join bd_bom b  on a.pk_bomid=b.pk_bomid  ");
				Sql.append("left join bd_produce e on e.pk_produce=a.pk_produce  ");
				Sql.append("left join bd_invbasdoc c on e.pk_invbasdoc=c.pk_invbasdoc  ");
				Sql.append("left join bd_invmandoc j  on c.pk_invbasdoc=j.pk_invbasdoc  ");
				Sql.append("left join  bd_measdoc f on f.pk_measdoc = c.pk_measdoc  ");
				IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				List list;
				try {
					list = (List) sessionManager.executeQuery(WhereSql, new ArrayListProcessor());

					ArrayList values = new ArrayList();
					Iterator iterator = list.iterator();
					while (iterator.hasNext()) {
						Object obj = iterator.next();
						if (obj.getClass().isArray()) {
							int len = Array.getLength(obj);
							for (int i = 0; i < len; i++) {
								values.add(Array.get(obj, i));
							}
						}

						wlbmid = values.get(4).toString();

						if (values.get(0) == null || String.valueOf(values.get(0)) == null || String.valueOf(values.get(0)).equals("") || String.valueOf(values.get(0)).equals("null")) {
							MessageDialog.showErrorDlg(this, "材料出库", "生产订单的BOM版本没录入!");
							return;
						}

						version = values.get(0).toString();
						wlbmcode = values.get(8).toString();
						wlbmname = values.get(9).toString();
						invclasscode = values.get(6).toString();
						invclassname = values.get(5).toString();
						invclasslev = values.get(7).toString();
						jhwgsl = values.get(10).toString();
					}
					if (list.isEmpty()) {
						MessageDialog.showErrorDlg(this, "材料出库", "没找到生产订单!");
						return;

					}
					if (sItemKey.equals("vuserdef14")) {

						((UIRefPane) getBillCardPanel().getHeadItem("vuserdef15").getComponent()).setPK(values.get(2));
						((UIRefPane) getBillCardPanel().getHeadItem("vuserdef15").getComponent()).setText(String.valueOf(values.get(1)));
						getBillCardPanel().getHeadItem("pk_defdoc15").setValue(values.get(2).toString());
						getBillCardPanel().getHeadItem("vuserdef15").setValue(values.get(1).toString());
					}
					if (!version.equals("")) {

						Sql.append("where  b.wlbmid='" + wlbmid + "' and b.version='" + version + "' and j.pk_corp='" + pkCorp + "'");
						getBomListInsertBillItem(Sql.toString(), wlbmid, invclasscode, invclassname, invclasslev, jhwgsl, wlbmname, wlbmcode);
					}

				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		if (sItemKey.equals("ccostobjectname")) {
			String costobjectname = null;
			String costobjectid = null;
			String costobjectcode = null;
			if (getBillCardPanel().getBodyItem("ccostobjectname").getComponent() != null) {
				costobjectname = ((UIRefPane) getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).getRefName();
				costobjectid = ((UIRefPane) getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).getRefPK();
				((UIRefPane) getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).setPK(null);
			}

			if (this.m_voBill != null) {
				this.m_voBill.setItemValue(e.getRow(), "ccostobjectname", costobjectname);
				this.m_voBill.setItemValue(e.getRow(), "ccostobject", costobjectid);
				getBillCardPanel().setBodyValueAt(costobjectname, e.getRow(), "ccostobjectname");

				getBillCardPanel().setBodyValueAt(costobjectid, e.getRow(), "ccostobject");
			}

		} else if (sItemKey.equals("cworkcentername")) {
			String cworkcentername = null;
			String cpkworkcenter = null;

			if (getBillCardPanel().getBodyItem("cworkcentername").getComponent() != null) {
				cworkcentername = ((UIRefPane) getBillCardPanel().getBodyItem("cworkcentername").getComponent()).getRefName();

				cpkworkcenter = ((UIRefPane) getBillCardPanel().getBodyItem("cworkcentername").getComponent()).getRefPK();
			}

			if (this.m_voBill != null) {
				this.m_voBill.setItemValue(e.getRow(), "cworkcentername", cworkcentername);
				this.m_voBill.setItemValue(e.getRow(), "cworkcentername", cpkworkcenter);
				getBillCardPanel().setBodyValueAt(cpkworkcenter, e.getRow(), "cpkworkcenter");
			}

		} else if (sItemKey.equals("cworksitename")) {
			String cworksitename = null;
			String cpkworksite = null;

			if (getBillCardPanel().getBodyItem("cworksitename").getComponent() != null) {
				cworksitename = ((UIRefPane) getBillCardPanel().getBodyItem("cworksitename").getComponent()).getRefName();

				cpkworksite = ((UIRefPane) getBillCardPanel().getBodyItem("cpkworksite").getComponent()).getRefPK();
			}

			if (this.m_voBill != null) {
				this.m_voBill.setItemValue(e.getRow(), "cworksitename", cworksitename);
				this.m_voBill.setItemValue(e.getRow(), "cpkworksite", cpkworksite);
				getBillCardPanel().setBodyValueAt(cpkworksite, e.getRow(), "cpkworksite");
			}

		}

		else {
			super.afterEdit(e);
		}

		if ((sItemKey.equals("pk_calbody")) || (sItemKey.equals("cwarehouseid")) || sItemKey.equals("cinventorycode")) {
			if (!sItemKey.equals("cinventorycode")) {
				initRefWork();
			}
			/**
			 * @功能:根据仓库、存货带出默认货位
			 * @author ：林桂莹
			 * @2012/9/5
			 * 
			 * @since v50
			 */
			if (getParentCorpCode().equals("10395")) {
				if (sItemKey.equals("cwarehouseid") || sItemKey.equals("cinventorycode")) {
					String cwarehouseid = ((UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid").getComponent()).getRefPK();

					if (cwarehouseid == null || cwarehouseid.equals("")) {

					return; }

					if (Iscsflag(cwarehouseid))// ||sCode.equals(arg0))
					{
						for (int i = 0; i < getBillCardPanel().getBillTable().getRowCount(); i++) {
							String cinventoryid = (String) getBillCardPanel().getBodyValueAt(i, "cinventoryid");
							if (cinventoryid == null || cinventoryid.equals("")) {return; }
							
							
							//edit by yqq 2017-03-24 只带现存量表里有结存数量的货位，若一个仓库有多个货位都有结存数量，则带排序的第一个
/*							String SQL = "select * from (select d.pk_cargdoc,d.csname,d.cscode   from ic_general_h a  ";
							SQL += "left join ic_general_b b on a.cgeneralhid=b.cgeneralhid  ";
							SQL += "left join ic_general_bb1 c on c.cgeneralbid=b.cgeneralbid  ";   //单据表体附表- 货位
							SQL += "left join bd_cargdoc d on c.cspaceid=d.pk_cargdoc  ";
							SQL += " where a.cbilltypecode ='4D' and a.cwarehouseid='" + cwarehouseid + "' and b.cinventoryid='" + cinventoryid + "' and a.dr=0 ";
							SQL += "and d.pk_cargdoc is not null  and a.taccounttime is not null and nvl(b.dr,0)=0 order by a.taccounttime desc  ";
							SQL += ") where rownum=1  ";*/
							
							String SQL = "select h.pk_cargdoc, h.csname, h.cscode from (select * from (select d.pk_cargdoc,d.csname,d.cscode   from ic_general_h a  ";
							SQL += "left join ic_general_b b on a.cgeneralhid=b.cgeneralhid  ";
							SQL += "left join ic_general_bb1 c on c.cgeneralbid=b.cgeneralbid  ";   //单据表体附表- 货位
							SQL += "left join bd_cargdoc d on c.cspaceid=d.pk_cargdoc ";
							SQL += " where a.cbilltypecode ='4D' and a.cwarehouseid='" + cwarehouseid + "' and b.cinventoryid='" + cinventoryid + "'  and a.dr=0 ";
							SQL += "and d.pk_cargdoc is not null  and a.taccounttime is not null and nvl(b.dr,0)=0 order by a.taccounttime desc  ";
							SQL += ") where rownum=1 )h  left join ic_onhandnum_b e on h.pk_cargdoc = e.cspaceid  where e.nnum > 0 ";

							IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
							List list = null;
							try {
								list = (List) sessionManager.executeQuery(SQL, new ArrayListProcessor());
								if (list.isEmpty()) {
									//edit by yqq 2017-02-20  增加条件‘结存数量>0’
							/*		SQL = "select kp.cspaceid ,car.csname,car.cscode   ";
									SQL += "from   v_ic_onhandnum6 kp  ";   //ic_onhandnum与ic_onhandnum_b的视图
									SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
									SQL += "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null " +
											" and kp.cwarehouseid='" + cwarehouseid+ "'  " +
											" and kp.cinventoryid='" + cinventoryid + "'  ";//edit by shikun 2014-11-01 输入存货带出默认货位（现存量表及现存量辅表）
									*/
									
					/*				SQL = "select kp.cspaceid ,car.csname,car.cscode   ";
									SQL += "from   v_ic_onhandnum6 kp  ";   //ic_onhandnum与ic_onhandnum_b的视图
									SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
									SQL += "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null " +
											" and kp.cwarehouseid='" + cwarehouseid+ "'  " +
											" and kp.cinventoryid='" + cinventoryid + "'  "+
											"and kp.ninspacenum > 0";//edit by shikun 2014-11-01 输入存货带出默认货位（现存量表及现存量辅表）										//end by yqq 2017-02-20								
								//end by yqq 2017-02-20		 */										
									
									
									//edit by wbp 2017-6-13  之前写的sql语句带出货位 推翻重新写
									SQL = "select * from (select kp.cspaceid ,car.csname,car.cscode   ";
									SQL += "from   v_ic_onhandnum6 kp  ";   //ic_onhandnum与ic_onhandnum_b的视图
									SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
									SQL += "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null " +
											" and kp.cwarehouseid='" + cwarehouseid+ "'  " +
											" and kp.cinventoryid='" + cinventoryid + "'  "+
											"and kp.ninspacenum > 0 ) where rownum = 1";// 输入存货带出现存量表里有结存数量的货位（现存量表及现存量辅表）
									
									//end edit by wbp
									//add by wbp 2017-6-13  如果材料出库之前没有做过同样的料号单据的话就重新根据仓库和料号带出有现存量的货位
//									SQL = " select bd_cargdoc.pk_cargdoc, bd_cargdoc.csname, bd_cargdoc.cscode ";
//									SQL += "from ic_onhandnum_b   ";   //ic_onhandnum与ic_onhandnum_b的视图
//									SQL += " left join bd_cargdoc on bd_cargdoc.pk_cargdoc= ic_onhandnum_b.cspaceid   ";
//									SQL += " where cinventoryidb='"+cinventoryid+"' and cwarehouseidb='"+cwarehouseid+"' and nnum>0 " ;
									//end by wbp 
									
									list = (List) sessionManager.executeQuery(SQL, new ArrayListProcessor());
									if (list.isEmpty()) { 
//										return;  //edit  by wbp  2017-6-13 
										//add by wbp 2017-6-13 
										continue;
										//end add by wbp 
										}
								}
							
							
							
		/*		  String	SQL = "select * from (select kp.cspaceid ,car.csname,car.cscode   ";
							SQL += "from   v_ic_onhandnum6 kp  ";   //ic_onhandnum与ic_onhandnum_b的视图
							SQL += "left join bd_cargdoc   car on car.pk_cargdoc=kp.cspaceid   ";
							SQL += "  where   kp.cspaceid <>'_________N/A________' and kp.cspaceid is not null " +
									" and kp.cwarehouseid='" + cwarehouseid+ "'  " +
									" and kp.cinventoryid='" + cinventoryid + "'  "+
									"and kp.ninspacenum > 0 ) where rownum = 1";// 输入存货带出现存量表里有结存数量的货位（现存量表及现存量辅表）
							
							IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
							List list = null;	
							try {									
							list = (List) sessionManager.executeQuery(SQL, new ArrayListProcessor());
							if (list.isEmpty()) { return; }*/
							//end by yqq 2017-03-24
							

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
		}else if (sItemKey.equals("vbatchcode")) {//add by shikun 2014-11-24 依据批次号带出供应商
			String cwarehouseid = ((UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid").getComponent()).getRefPK();
			Object cinventoryid = getBillCardPanel().getBodyValueAt(e.getRow(), "cinventoryid");
            Object vbatchcode = getBillCardPanel().getBodyValueAt(e.getRow(), "vbatchcode");
            if (cinventoryid == null || cinventoryid.equals("")||cwarehouseid == null || cwarehouseid.equals("")||vbatchcode == null || vbatchcode.equals("")) { 
				return;
			}
			String pk_corp1 = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			StringBuffer sql = new StringBuffer();
/*			sql.append(" select distinct bas.custcode, bas.custname, bas.pk_cubasdoc, man.pk_cumandoc ") 
			.append("   from ic_onhandnum xcl ") 
			.append("  inner join bd_cumandoc man on nvl(man.dr, 0) = 0 ") 
			.append("                            and man.pk_cumandoc = xcl.cvendorid ") 
			.append("  inner join bd_cubasdoc bas on nvl(bas.dr, 0) = 0 ") 
			.append("                            and bas.pk_cubasdoc = man.pk_cubasdoc ") 
			.append("  where nvl(xcl.dr, 0) = 0 ") 
			.append("    and xcl.vlot = '"+vbatchcode+"' and xcl.cinventoryid='" + cinventoryid + "'") 
			.append("    and xcl.cwarehouseid = '"+cwarehouseid+"' and xcl.pk_corp = '"+pk_corp1+"' ");*/
			
			//edit by yqq 2017-02-10 依据批次号带出供应商,货位
		      sql.append(" select  bd.csname,bd.pk_cargdoc,bas.custcode, bas.custname, bas.pk_cubasdoc, man.pk_cumandoc ") 
		      .append("   from ic_onhandnum xcl ") 
		      .append("  left join bd_cumandoc man on nvl(man.dr, 0) = 0 ") 
		      .append("                            and man.pk_cumandoc = xcl.cvendorid ") 
		      .append("  left join bd_cubasdoc bas on nvl(bas.dr, 0) = 0 ") 
		      .append("                            and bas.pk_cubasdoc = man.pk_cubasdoc ")       
		      .append("  left join ic_onhandnum_b b on nvl(b.dr, 0) = 0 ") 
		      .append("                            and b.pk_onhandnum = xcl.pk_onhandnum ")             
		      .append("  left join bd_cargdoc bd on nvl(bd.dr, 0) = 0 ") 
		      .append("                            and bd.pk_cargdoc = b.cspaceid  ")      
		      .append("  where nvl(xcl.dr, 0) = 0 and b.nnum>0") 
		      .append("    and xcl.vlot = '"+vbatchcode+"' and xcl.cinventoryid='" + cinventoryid + "'") 
		      .append("    and xcl.cwarehouseid = '"+cwarehouseid+"' and xcl.pk_corp = '"+pk_corp1+"' ");
			
			
			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List list;
			try {
				list = (List) sessionManager.executeQuery(sql.toString(), new MapListProcessor());
				if (list!=null&&list.size()>0) {
					Map addrMap = (Map) list.get(0);
					String pk_cumandoc = addrMap.get("pk_cumandoc") == null ? "" : addrMap.get("pk_cumandoc").toString();//"0001A21000000005KN03"
					String custname = addrMap.get("custname") == null ? "" : addrMap.get("custname").toString();
				
					//end by yqq 2017-02-10 依据批次号带出供应商,货位				
 					String cspaceid = addrMap.get("pk_cargdoc") == null ? "" : addrMap.get("pk_cargdoc").toString();//"0001A21000000005KN03"					
					String vspacename = addrMap.get("csname") == null ? "" : addrMap.get("csname").toString();
					
					
          	//		getBillCardPanel().getBillModel().setValueAt(pk_cargdoc, row, "cspaceid");
			//		getBillCardPanel().getBillModel().setValueAt(csname, row, "vspacename");
					
					getBillCardPanel().getBillModel().setValueAt(pk_cumandoc, row, "cvendorid");
					getBillCardPanel().getBillModel().setValueAt(custname, row, "vvendorname");
					
					
					
					if (!"".equals(cspaceid)) {
						getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");
						getBillCardPanel().getBillModel().setValueAt(vspacename, row, "vspacename");
						setSpace(cspaceid, vspacename, row);
					}
					
				}
		} catch (BusinessException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//wkf 根据批次号效验现存量，并生成表体行数据
	private void addData(String pileno, int pilecount) {
		String cinventoryid = new String();
		String cinventorycode = new String();
		String cinventoryname = new String();
		String vfree1 = new String();
		UFDouble num = null;
		String cspaceid = new String();
		String vbatchcode = new String();
		String csname = new String();
		String Wh = new String();
		Wh = ((UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid")
				.getComponent()).getRefPK();
		if (Wh == null || Wh.equals("") || Wh.equals("null")) {
			MessageDialog.showErrorDlg(this, "材料出库Sale of the library",
					"仓库不能为空!Warehouse can not be empty!");
			return;
		}
		if ((pilecount == 0) && !pileno.equals(""))// 垛号扫描
		{
			String SQL = "select * from (  ";
			SQL += "select  kp.cinventoryid, kp.cspaceid, "
					+ "SUM ( nvl( kp.ninspacenum, 0.0 ) - nvl( kp.noutspacenum, 0.0 ) - nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0) ) num"
					+ ",kp.vbatchcode,kp.vfree1,car.csname,inv.invcode,inv.invname  ";
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
			SQL += "where kp.vfree1 ='"//根据批次号查出现存量信息
					+ pileno
					+ "' and kp.pk_corp='"
					+ getClientEnvironment().getInstance().getCorporation()
							.getPrimaryKey() + "' and kp.cwarehouseid='" + Wh
					+ "'  ";
			SQL += "group by kp.pk_corp, kp.ccalbodyid, kp.cwarehouseid, kp.cinventoryid, kp.vbatchcode,kp.cspaceid ,kp.vfree1,car.csname,inv.invcode,inv.invname ";
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
			if(list.size()==0 || list == null){
				showErrorMessage("卷号:"+pileno+"现存量不足!");
				return;
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
				vfree1 = (String) values.get(4);
				csname = (String) values.get(5);
				cinventorycode = (String) values.get(6);
				cinventoryname = (String) values.get(7);
			}
				int Eindex  = getBillCardPanel().getRowCount()-1;//noutnum
				Object invcode1 = getBillCardPanel().getBodyValueAt(Eindex, "cinventorycode");
				if(Eindex == 0 && invcode1 == null){//如果当前为第一行，并且存货编码项为空
					GeneralBillItemVO newbi = voCopyLine(Eindex);// (GeneralBillItemVO)
					newbi.setStatus(VOStatus.NEW);
					newbi.setCgeneralbid(null);
					newbi.setCgeneralhid(null);
					newbi.setTs(null);
					getBillCardPanel().getBillModel().setBodyRowVO(newbi,
							Eindex);

					String m_invcode = cinventorycode;
					String m_invid = cinventoryid;
					String m_invname = cinventoryname;
					getBillCardPanel().getBillModel().setBodyRowVO(newbi,
							Eindex);

					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setPK(m_invid);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setName(m_invname);

					BillEditEvent e = new BillEditEvent(getBillCardPanel()
							.getBodyItem("cinventorycode").getComponent(),
							m_invcode, "cinventorycode", Eindex);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setPK(m_invid);
					((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode")
							.getComponent()).setName(m_invname);
					afterEdit(e);
					UFDouble nshououtnum = new UFDouble(0);//应发数量
					SetBodyNewValue(vfree1, num, vbatchcode, csname, cspaceid,
							Eindex, nshououtnum);
					getBillCardPanel().getBillModel().setRowState(Eindex,
							BillModel.ADD);
				}
				else{
					//校验卷号重复
					for (int i = 0; i < Eindex+1; i++) {
						Object vfree2 = getBillCardPanel().getBodyValueAt(i, "vfree0");
						if(vfree2!=null){
							vfree2 = vfree2.toString().substring(4,vfree2.toString().length()-1);
						}else{
							vfree2 = "null";
						}
						if(!vfree2.equals(null)){
							if(vfree2.equals(vfree1)){
								showErrorMessage("卷号:"+vfree1+"重复！");
								return;
							}
						}
					}
					GeneralBillItemVO newbi = voCopyLine(Eindex);// (GeneralBillItemVO)
//					Object haha= getBillCardPanel().getBodyValueAt(Eindex, "nshouldoutnum");
//					String nshououtnum1 = ""; 
//					if(haha != null){
//						nshououtnum1 = haha.toString();
//					}else{
//						nshououtnum1 = "0";
//					}
					UFDouble nshououtnum = new UFDouble(0);
					onAddLine();
					newbi.setStatus(VOStatus.NEW);
					newbi.setCgeneralbid(null);
					newbi.setCgeneralhid(null);
					newbi.setTs(null);
					
					getBillCardPanel().getBillModel().setBodyRowVO(newbi,
							Eindex + 1);
					
					String m_invcode = cinventorycode;
					String m_invid = cinventoryid;
					String m_invname = cinventoryname;
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
					SetBodyNewValue(vfree1, num, vbatchcode, csname, cspaceid,
							Eindex + 1, nshououtnum);
					getBillCardPanel().getBillModel().setRowState(Eindex + 1,
							BillModel.ADD);
				}
			}
	}
	
	/**
	 * @功能:设置自由项、实发数量、批号、货位
	 * @author ：王凯飞
	 * @2014/12/19
	 */
	private void SetBodyNewValue(String pileno, UFDouble num,
			String vbatchcode, String csname, String cspaceid, int sindex,
			UFDouble nshououtnum) {
		// setSpace(cspaceid, csname, sindex);
		getBillCardPanel().setBodyValueAt("[卷号:" + pileno + "]", sindex,
				"vfree0");
		getBillCardPanel().setBodyValueAt(nshououtnum, sindex, "nshouldoutnum");

		nc.vo.scm.ic.bill.FreeVO voFree = new nc.vo.scm.ic.bill.FreeVO();
		voFree.m_vfree0 = "[卷号:" + pileno + "]";
		voFree.m_vfreename1 = "卷号";
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
	
	
	public boolean beforeBillItemEdit(BillEditEvent e) {
		return true;
	}

	protected void beforeBillItemSelChg(int iRow, int iCol) {
	}

	protected boolean checkVO(GeneralBillVO voBill) {
		return super.checkVO();
	}

	protected BillCardPanel getBillCardPanel() {
		if (this.ivjBillCardPanel == null) {
			try {
				this.ivjBillCardPanel = super.getBillCardPanel();
				BillData bd = this.ivjBillCardPanel.getBillData();

				if (bd.getBodyItem("cworkcentername") != null) {
					UIRefPane ref1 = new UIRefPane();
					ref1.setIsCustomDefined(true);

					WkRefModel model1 = new WkRefModel();

					ref1.setRefModel(model1);

					bd.getBodyItem("cworkcentername").setComponent(ref1);
				}

				if (bd.getBodyItem("cworksitename") != null) {
					UIRefPane ref1 = new UIRefPane();
					ref1.setIsCustomDefined(true);

					TldRefModel model1 = new TldRefModel();

					ref1.setRefModel(model1);
					bd.getBodyItem("cworksitename").setComponent(ref1);
				}

				this.ivjBillCardPanel.setBillData(bd);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjBillCardPanel;
	}

	protected ArrayList getFormulaItemBody() {
		ArrayList arylistItemField = super.getFormulaItemBody();
		if (arylistItemField != null) {
			String[] aryItemField20 = {
					"gzzxmc", "cworkcentername", "cworkcenterid"
			};
			arylistItemField.add(aryItemField20);

			String[] aryItemField21 = {
					"tldmc", "cworksitename", "cworksiteid"
			};
			arylistItemField.add(aryItemField21);
		}
		return arylistItemField;
	}

	private RatioOUTStructureDlg getRatioOUTDlg() {
		if (this.ivjRatioOUTStructureDlg == null) {
			try {
				this.ivjRatioOUTStructureDlg = new RatioOUTStructureDlg(this, this.m_ScaleValue.getNumScale());
				this.ivjRatioOUTStructureDlg.setName("RatioOUTStructureDlg");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjRatioOUTStructureDlg;
	}

	public String getTitle() {
		return super.getTitle();
	}

	protected void initPanel() {
		super.setBillInOutFlag(-1);

		super.setNeedBillRef(false);

		this.m_sBillTypeCode = BillTypeConst.m_materialOut;

		this.m_sCurrentBillNode = "40080804";
	}

	public void onButtonClicked(ButtonObject bo) {

		if (bo == getButtonTree().getButton("配比出库")) {
			onRatioOut();
		} else if (bo == getButtonTree().getButton("参照物资需求申请单")) {
			bo.setTag("422X:");
			RedunSourceDlg.childButtonClicked(bo, this.m_sCorpID, this.m_sCurrentBillNode, this.m_sUserID, this.m_sBillTypeCode, this);
			if (RedunSourceDlg.isCloseOK()) {
				AggregatedValueObject[] vos = RedunSourceDlg.getRetsVos();
				onAddToOrder(vos);
				readCard(bo);//add by shikun 2014-10-22 问题：受控件材料出库不能进行刷卡保存
			}
		} else if (bo == getButtonTree().getButton("刷卡签字")) {
			onReadICCard();
	    
		    
			  //add by yqq 20160906  材料出库单表体的实发数量为0不能保存
	    }else if (bo == getButtonTree().getButton("保存")){
		        onBoSave();			
				
		} else {
			super.onButtonClicked(bo);
			readCard(bo);			
		}
	}
	
	
    // add by yqq 20160906 材料出库单表体的实发数量为0不能保存
	private void onBoSave() {
		int size = this.getBillCardPanel().getBillModel().getRowCount();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
			
				UFDouble noutnum = getBillCardPanel().getBodyValueAt(i,"noutnum") == null ? 
						new UFDouble(0.00000): (UFDouble) (getBillCardPanel().getBodyValueAt(i,"noutnum"));
                 
     			if (noutnum.doubleValue()==0) {
    				showWarningMessage("表体的实发数量不能为0");
    				return;
    			} else {
    				continue;
    			}

			}
		}
		super.onSave();
	}

	// end by yqq
	

	@Override
	public boolean onClosing() {
		boolean close = super.onClosing();
		if (close) {
			isclose = true;
		}
		return close;
	}

	@Override
	public void onCancel() {
		super.onCancel();
		isclose = true;
	}

	public void onReadICCard() {
		TestConn conn = new TestConn();
		String cardnumber = null;
		try {
			cardnumber = conn.readICCard();
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.showHintDlg(this, "读卡异常", "读卡过程发生异常,请重新读卡");
		}
		if (cardnumber == null || "".equals(cardnumber)) return;
		Object obj = null;
		try {
			obj = getUAPQuery().executeQuery("select pk_psndoc from bd_psndoc a where a.def1='" + cardnumber + "'", new ColumnProcessor());
		} catch (FTSBusinessException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (obj == null) {
			MessageDialog.showHintDlg(this, "数据异常", "没有找到该卡号对应的人员信息");
			return;
		}
		getBillCardPanel().setHeadItem("cbizid", obj);
		BillEditEvent event = new BillEditEvent(getBillCardPanel().getHeadItem("cbizid"), obj, "cbizid");
		afterEdit(event);

	}

	public void onRatioOut() {
		if ((this.m_sCorpID == null) || (this.m_sUserID == null) || (this.m_sLogDate == null)) {
			SCMEnv.out("公司ID，操作员ID，登陆日期为空！");
			return;
		}

		this.ivjRatioOUTStructureDlg = null;

		getRatioOUTDlg().setParams(this.m_sCorpID, this.m_sCorpID, this.m_sUserID, this.m_sUserName, this.m_sLogDate);
		getRatioOUTDlg().showModal();
	}

	public void qryRationOutBill(ArrayList alHIDs) {
		try {
			Timer timer = new Timer();
			timer.start(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000277"));
			StringBuffer sbSql = new StringBuffer("head.cgeneralhid in (");
			if ((alHIDs == null) || (alHIDs.size() <= 0)) return;
			int size = alHIDs.size();
			for (int i = 0; i < size; ++i) {
				sbSql.append("'");
				sbSql.append(alHIDs.get(i));
				sbSql.append("'");
				if (i != size - 1) sbSql.append(",");
			}
			sbSql.append(")");

			QryConditionVO voCond = new QryConditionVO(sbSql.toString());
			if (this.m_bIsByFormula) voCond.setIntParam(0, 300);
			else {
				voCond.setIntParam(0, 100);
			}
			showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000250"));
			timer.showExecuteTime("Before 查询：：");
			ArrayList alListData = GeneralBillHelper.queryBills(this.m_sBillTypeCode, voCond);

			timer.showExecuteTime("查询时间：");

			if (this.m_bIsByFormula) {
				setAlistDataByFormula(20, alListData);
			}

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

				if (this.m_iBillQty > 0) showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000290", null, new String[] {
					new Integer(this.m_iBillQty).toString()
				}));
				else showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000243"));
			} else {
				dealNoData();
			}
			this.m_iMode = 3;
			setButtonStatus(true);
		} catch (Exception e) {
			handleException(e);
			showHintMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000251"));
			showWarningMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000252") + e.getMessage());
		}
	}

	protected void selectBillOnListPanel(int iBillIndex) {
	}

	protected void setButtonsStatus(int iBillMode) {
		getButtonTree().getButton("配比出库").setEnabled(true);
		if (iBillMode == BillMode.New || iBillMode == BillMode.Update || iBillMode == BillMode.QuickNew) {
			// getButtonTree().getButton("刷卡签字").setEnabled(true);
		} else {
			// getButtonTree().getButton("刷卡签字").setEnabled(false);
		}

	}

	private void onAddToOrder(AggregatedValueObject[] vos) {
		if (vos == null) return;
		try {
			if (vos.length <= 1) {
				setBillRefResultVO(null, vos);
			} else {
				setBillRefMultiVOs(null, (GeneralBillVO[]) (GeneralBillVO[]) vos);

				setRefBillsFlag(true);
			}
		} catch (Exception e) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008busi", "UPP4008busi-000297") + e.getMessage());
		}
	}

	/**
	 * @功能:获取BOM子项，插入表体
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	private void getBomListInsertBillItem(String sql, String wlbmid, String invclasscode, String invclassname, String invclasslev, String jhwgsl, String wlbmname, String wlbmcode) throws BusinessException {
		String getIncClassSql = "select invclasscode,invclassname  ";
		getIncClassSql = getIncClassSql + "  from bd_invcl a where (substr(a.invclasscode,0,4)='" + invclasscode.substring(0, 4) + "'  and LENGTHB(a.invclasscode)=4)or ( substr(a.invclasscode,0,6)='" + invclasscode.substring(0, 6) + "' and LENGTHB(a.invclasscode)=6) and invclasslev<" + invclasslev + " order by invclasscode desc";
		String getIncClass = getInvcl(getIncClassSql);
		String invclassSec = new String();
		String invclassThr = new String();
		if (getIncClass.length() > 0) {
			if (getIncClass.indexOf(",") > 0) {
				invclassSec = getIncClass.substring(0, getIncClass.indexOf(",") - 1);
				invclassThr = getIncClass.substring(getIncClass.indexOf(",") + 1, getIncClass.length() - 1);
			}
		}
		/******************************* */
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List list = (List) sessionManager.executeQuery(sql, new ArrayListProcessor());
		// ArrayList values = new ArrayList();
		int rowNo = getBillCardPanel().getBillTable().getRowCount();
		int curRow = 0;
		int rowNum = 10;
		if (rowNo == 0) {
			getBillCardPanel().getBodyPanel().getTableModel().addLine();
		}
		if (getBillCardPanel().getBodyValueAt(curRow, "crowno") != null) {
			rowNum = Integer.parseInt(getBillCardPanel().getBodyValueAt(curRow, "crowno").toString());
		}
		if (list.size() > 0) {
			int Row = getBillCardPanel().getBillTable().getRowCount();
			if (Row > 0) {
				for (int i = 0; i < Row; i++)

				{
					clearRow(i);
				}
			}
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
				for (int i = 0; i < len; i++) {
					values.add(Array.get(obj, i));
				}
			}
			String noutnum = String.valueOf(Double.parseDouble(values.get(8).toString()) * Double.parseDouble(jhwgsl));
			if ((rowNo < -1) || (rowNo - 1 < curRow))// 表体为空
			// 或者当前处理记录的行数大于表体的行数时新增
			{
				getBillCardPanel().getBodyPanel().addLine();
				getBillCardPanel().setBodyValueAt(values.get(0), curRow, "cinventoryid");// 存货ID
				getBillCardPanel().setBodyValueAt(values.get(1), curRow, "cinventorycode");// 存货编码
				getBillCardPanel().setBodyValueAt(values.get(2), curRow, "invname");// 存货编码
				getBillCardPanel().setBodyValueAt(wlbmcode, curRow, "ccostobject");// 成本对象

				getBillCardPanel().setBodyValueAt(wlbmname, curRow, "ccostobjectname");// 成本对象

				getBillCardPanel().setBodyValueAt(values.get(9), curRow, "ccostobjectbasid");

				getBillCardPanel().setBodyValueAt(invclassname, curRow, "vuserdef8");
				getBillCardPanel().setBodyValueAt(invclassSec, curRow, "vuserdef9");
				getBillCardPanel().setBodyValueAt(invclassThr, curRow, "vuserdef10");

				BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem("cinventorycode").getComponent(), values.get(1), "cinventorycode", curRow);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setPK(values.get(0));
				afterInvMutiEdit(e);
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(noutnum, curRow, "noutnum");//
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(noutnum, curRow, "nshouldoutnum");//
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()), curRow, "dbizdate");
				rowNo++;
			} else if (rowNo - 1 >= curRow) {

				getBillCardPanel().setBodyValueAt(values.get(1), curRow, "cinventorycode");// 存货编码bi
				getBillCardPanel().setBodyValueAt(values.get(2), curRow, "invname");// 存货编码
				getBillCardPanel().setBodyValueAt(values.get(0), curRow, "cinventoryid");// 存货ID

				getBillCardPanel().setBodyValueAt(wlbmcode, curRow, "ccostobject");// 成本对象

				getBillCardPanel().setBodyValueAt(wlbmname, curRow, "ccostobjectname");// 成本对象
				getBillCardPanel().setBodyValueAt(values.get(9), curRow, "ccostobjectbasid");

				getBillCardPanel().setBodyValueAt(invclassname, curRow, "vuserdef8");
				getBillCardPanel().setBodyValueAt(invclassSec, curRow, "vuserdef9");
				getBillCardPanel().setBodyValueAt(invclassThr, curRow, "vuserdef10");

				BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem("cinventorycode").getComponent(), values.get(1), "cinventorycode", curRow);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setPK(values.get(0));
				afterInvMutiEdit(e);
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(noutnum, curRow, "noutnum");//
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(noutnum, curRow, "nshouldoutnum");//
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()), curRow, "dbizdate");

			}
			curRow++;
		}
	}

	/**
	 * @功能:返回存货的二级、三级分类
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	private String getInvcl(String sql) {
		String invcl = new String();
		try {
			IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List list = (List) sessionManager.executeQuery(sql, new ArrayListProcessor());

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				ArrayList values = new ArrayList();
				Object obj = iterator.next();
				if (obj == null) {
					continue;
				}
				if (obj.getClass().isArray()) {
					int len = Array.getLength(obj);
					for (int i = 0; i < len; i++) {
						values.add(Array.get(obj, i));
					}
					invcl = invcl + values.get(1).toString() + ",";
				}
			}
			if (invcl.length() > 0) {
				invcl = invcl.substring(0, invcl.length() - 1);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return invcl;
	}

	/**
	 * @功能:设置货位名称
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
	 * @功能:取产品线ID
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public String getProLine() {
		return m_strProLine;
	}

	/**
	 * @功能:设置产品线ID
	 * @author ：林桂莹
	 * @2012/9/5
	 * 
	 * @since v50
	 */
	public void setProLine(String m_strProLine) {
		this.m_strProLine = m_strProLine;
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
}