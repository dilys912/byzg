package nc.ui.ic.ic251;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.pub.InvOnHandHelper;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillHelper;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.ic.pub.locatorref.LocatorRefPane;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.scm.ic.exp.GeneralMethod;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.InvOnHandVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.ic.pub.sn.SerialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.ic.exp.ICDateException;
import nc.vo.scm.ic.exp.ICHeaderNullFieldException;
import nc.vo.scm.ic.exp.ICLocatorException;
import nc.vo.scm.ic.exp.ICNullFieldException;
import nc.vo.scm.ic.exp.ICNumException;
import nc.vo.scm.ic.exp.ICPriceException;
import nc.vo.scm.ic.exp.ICRepeatException;
import nc.vo.scm.ic.exp.ICSNException;
import nc.vo.scm.pub.SCMEnv;

import org.apache.commons.lang.StringUtils;

/**
 * 货位调整
 * */
public class ClientUI extends GeneralBillClientUI {
	private LocatorRefPane m_refLocator = null;
	private LocatorRefPane m_refLocator2 = null;

	protected QueryConditionDlgForBill ivjQueryConditionDlg = null;

	public String m_strProLine = new String();// 自定义变量 2012/7/31

	public ClientUI() {
		initialize();
		if (getBillCardPanel().getHeadItem("pk_defdoc19") != null) {
			String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			String whereSql = " a.pk_corp='" + pkCorp + "'";
			((UIRefPane) getBillCardPanel().getHeadItem("pk_defdoc19").getComponent()).setWhereString(whereSql);
			((UIRefPane) getBillCardPanel().getBodyItem("pk_defdoc19").getComponent()).setAutoCheck(false);
		}

	}

	public ClientUI(String pk_corp, String billType, String businessType, String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);
	}

	protected void appendLocator(GeneralBillVO voBill) {
		GeneralBillItemVO[] voItems = (GeneralBillItemVO[]) (GeneralBillItemVO[]) voBill.getChildrenVO();
		if (voItems != null) {
			UFDouble uf1 = new UFDouble(0);
			SCMEnv.out("body rows=" + voItems.length);
			for (int i = 0; i < voItems.length; ++i) {
				voItems[i].setNoutnum(uf1);
			}
		}

		super.appendLocator(voBill);

		boolean isQueried = false;

		if (voItems != null) {
			SCMEnv.out("body rows=" + voItems.length);
			for (int i = 0; i < voItems.length; ++i) {
				LocatorVO[] lvos = (LocatorVO[]) voItems[i].getLocator();

				if ((lvos != null) && (lvos.length > 0)) {
					voItems[i].setCspaceid(lvos[0].getCspaceid());
					voItems[i].setVspacename(lvos[0].getVspacename());
					voItems[i].setNoutnum((lvos[0].getNoutspacenum() == null) ? lvos[0].getNinspacenum() : lvos[0].getNoutspacenum());
					voItems[i].setNoutassistnum(lvos[0].getNoutspaceassistnum());
					voItems[i].setNoutgrossnum((lvos[0].getNoutgrossnum() == null) ? lvos[0].getNingrossnum() : lvos[0].getNoutgrossnum());

					if (lvos.length > 1) {
						voItems[i].setCspace2id(lvos[1].getCspaceid());
						voItems[i].setVspace2name(lvos[1].getVspacename());
					}
				} else {
					voItems[i].setCspaceid(null);
					voItems[i].setVspacename(null);
				}
			}
		}
	}

	protected void afterBillEdit(BillEditEvent e) {
		try {
			if (e.getKey().equals(this.m_sMainCalbodyItemKey)) {
				afterCalbodyEdit(e);
			}
			int row = getBillCardPanel().getBillTable().getSelectedRow();
			String sItemKey = e.getKey();
			if (getParentCorpCode().equals("10395")) {
				StringBuffer Sql = new StringBuffer();
				String WhereSql = new String();
				String wlbmid = new String();
				String version = new String();
				String invclasslev = new String();
				String invclassname = new String();
				String invclasscode = new String();
				String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
				String wlbmname = new String();
				String jhwgsl = new String();

				if (sItemKey.equals("vuserdef14")) {

					if (getBillCardPanel().getHeadItem("vuserdef14") == null) {
						return;

					} else if (getBillCardPanel().getHeadItem(sItemKey.toString()).getValueObject().toString().equals("")) {
						return;
					} else if (!getBillCardPanel().getHeadItem(sItemKey.toString()).getValueObject().toString().equals("")) {
						setProLine(getBillCardPanel().getHeadItem("pk_defdoc14").getValueObject().toString());
					}
					WhereSql = "select * from(select a.bomver,a.scddh,a.pk_moid,a.sjkgrq||' '||a.sjkssj as kgrq,a.wlbmid,h.invclassname,h.invclasscode,h.invclasslev,f.invcode,f.invname,a.jhwgsl,h.pk_invcl   ";
					WhereSql = WhereSql + "from mm_mo  a left  join mm_mokz c on a.pk_moid=c.pk_moid  left join pd_wk b on c.gzzxid =B.PK_WKID  left join bd_invbasdoc f on f.pk_invbasdoc=a.wlbmid left join bd_invcl h  on f.pk_invcl=h.pk_invcl ";
					WhereSql = WhereSql + "where B.PK_CORP ='" + pkCorp + "'and a.dr=0 and zt='B' and (a.bomver is not null or a.bomver<>'')";
					if (!getProLine().equals("")) {

						WhereSql += " and c.gzzxid ='" + getProLine() + "'";

					}
					WhereSql += " order by sjkgrq||' '||sjkssj ) where rownum=1";
				}

				if (sItemKey.equals("vuserdef15")) {
					if (getBillCardPanel().getHeadItem("vuserdef15") == null) {

						return;

					} else if (getBillCardPanel().getHeadItem("vuserdef15").getValueObject().toString().equals("")) { return; }
					WhereSql = "select a.bomver,a.scddh,a.pk_moid,a.sjkgrq||' '||a.sjkssj as kgrq,a.wlbmid,h.invclassname,h.invclasscode,h.invclasslev,f.invcode,f.invname,a.jhwgsl,h.pk_invcl   ";
					WhereSql = WhereSql + "from mm_mo  a left  join mm_mokz c on a.pk_moid=c.pk_moid  left join pd_wk b on c.gzzxid =B.PK_WKID  left join bd_invbasdoc f on f.pk_invbasdoc=a.wlbmid left join bd_invcl h  on f.pk_invcl=h.pk_invcl  ";
					WhereSql = WhereSql + "where B.PK_CORP ='" + pkCorp + "'and a.dr=0 and zt='B' and (a.bomver is not null or a.bomver<>'') and a.pk_moid='" + getBillCardPanel().getHeadItem("pk_defdoc15").getValueObject().toString() + "' ";

					WhereSql += "  order by sjkgrq||' '||sjkssj ";
				}
				if (sItemKey.equals("vuserdef15") || (sItemKey.equals("vuserdef14"))) {

					Sql.append("select j.pk_invmandoc,c.invcode,C.invname ,f.pk_measdoc, f.shortname ,f.measname,c.invspec,c.invtype,a.sl, c.pk_invbasdoc  ");
					Sql.append("from bd_bom_b a  ");
					Sql.append("left join bd_bom b  on a.pk_bomid=b.pk_bomid  ");
					Sql.append("left join bd_produce e on e.pk_produce=a.pk_produce  ");
					Sql.append("left join bd_invbasdoc c on e.pk_invbasdoc=c.pk_invbasdoc  ");
					Sql.append("left join bd_invmandoc j  on c.pk_invbasdoc=j.pk_invbasdoc  ");
					Sql.append("left join bd_measdoc f on f.pk_measdoc = c.pk_measdoc  ");

					IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					List list = (List) sessionManager.executeQuery(WhereSql, new ArrayListProcessor());
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

						if (values.get(0) == null || String.valueOf(values.get(0)) == null || String.valueOf(values.get(0)).equals("") || String.valueOf(values.get(0)).equals("null")) {
							MessageDialog.showErrorDlg(this, "货位调整", "生产订单的BOM版本没录入!");
							return;
						}
						wlbmid = values.get(4).toString();
						version = values.get(0).toString();
						wlbmname = values.get(9).toString();
						invclasscode = values.get(6).toString();
						invclassname = values.get(5).toString();
						invclasslev = values.get(7).toString();
						jhwgsl = values.get(10).toString();

					}

					if (list.isEmpty()) {
						MessageDialog.showErrorDlg(this, "货位调整", "没找到生产订单!");
						return;

					}
					if (sItemKey.equals("vuserdef14")) {
						((UIRefPane) getBillCardPanel().getHeadItem("vuserdef15").getComponent()).setPK(values.get(2));
						((UIRefPane) getBillCardPanel().getHeadItem("vuserdef15").getComponent()).setText(String.valueOf(values.get(1)));
						getBillCardPanel().getHeadItem("vuserdef15").setValue(String.valueOf(values.get(1)));
						getBillCardPanel().getHeadItem("pk_defdoc15").setValue(String.valueOf(values.get(2)));
					}

					if (!version.equals("")) {
						Sql.append("where  b.wlbmid='" + wlbmid + "' and b.version='" + version + "'and j.pk_corp='" + pkCorp + "'");
						getBomListInsertBillItem(Sql.toString(), wlbmid, invclasscode, invclassname, invclasslev, jhwgsl, wlbmname);

					}

				}
			}
			if (("cinventorycode".equals(sItemKey)) || ("cwarehouseid".equals(sItemKey))) {
				getBillCardPanel().setBodyValueAt(null, row, "vspace2name");
				getBillCardPanel().setBodyValueAt(null, row, "cspace2id");

				getLocatorRefPane().setParam(this.m_voBill.getWh(), this.m_voBill.getItemInv(row));
				getLocatorRefPane2().setParam(this.m_voBill.getWh(), this.m_voBill.getItemInv(row));
				GeneralBillUICtl.synUi2Vo(getBillCardPanel(), this.m_voBill, new String[] {
						"vspace2name", "cspace2id"
				}, row);
			} else if ((sItemKey.equals("vspacename")) || (sItemKey.equals("vspace2name"))) {
				afterSpaceEdit(e);
			} else if ((this.m_sNumItemKey.equals(sItemKey)) || (this.m_sAstItemKey.equals(sItemKey))) {
				afterSpaceEdit(e);
				checkNum(row);
			} 
			/*
				* 根据输入钢卷、铝卷单件号带出批次号
				* @zl
				* 2013/11/13
				*/
			else if (("vfree1".equals(sItemKey)) || "vfree0".equals(sItemKey)) {
				//获得操作行的单件号
				String vfree = getBillCardPanel().getBodyValueAt(row, "vfree0") == null ? "" : getBillCardPanel().getBodyValueAt(row, "vfree0").toString();
				String Pathcoed = getvfree1(vfree);
				if ("".equals(Pathcoed)) {
					Pathcoed = getBillCardPanel().getBodyValueAt(row, "vfree1") == null ? "" : getBillCardPanel().getBodyValueAt(row, "vfree1").toString();
				}
				//获取表头字段仓库字段
				String cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject()==null?"":getBillCardPanel().getHeadItem("cwarehouseid").getValueObject().toString();
				//获得操作行的存货管理档案主键
				String cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid")==null?"":getBillCardPanel().getBodyValueAt(row, "cinventoryid").toString();
				
				if (Pathcoed.equals("")||cwarehouseid.equals("")||cinventoryid.equals("")) { 
					return; 
				}
				
				String sql = getQuSql(cwarehouseid,cinventoryid,Pathcoed);
				
				 //1、获取存货编码
				 //2、判断存货编码是不是属于钢卷、铝卷(以25010 /25011开头的)
				 //3、不属于钢卷、铝卷的话 提示：输入的单件号有误
				String vbillcode = getBillCardPanel().getBillModel().getValueAt(row, "cinventorycode") == null ? "" : getBillCardPanel().getBillModel().getValueAt(row, "cinventorycode").toString();
				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				MapListProcessor alp = new MapListProcessor();
				if (vbillcode.substring(0, 5).equals("25010") || vbillcode.substring(0, 5).equals("25011")) {
					setCspaceidAndVbatchcodeAndNoutnum(row,query,alp,sql);
				} else {
					//提示语“输入的单件号不属于钢卷、铝卷，是否继续”(getUI(), );
					int is = showOkCancelMessage("输入的单件号不属于钢卷、铝卷，是否继续");
					if (is == 1) {
						setCspaceidAndVbatchcodeAndNoutnum(row,query,alp,sql);
					} 
				}
			} 
			/**
			* 根据输入批次号带出货位、数量、供应商
			* @author shikun
			* 2014/11/18
			*/
		else if ("vbatchcode".equals(sItemKey)) {

			//获取表头字段仓库字段
			String cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject()==null?"":getBillCardPanel().getHeadItem("cwarehouseid").getValueObject().toString();
			//获得操作行的存货管理档案主键
			String cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid")==null?"":getBillCardPanel().getBodyValueAt(row, "cinventoryid").toString();
			//获得操作行的批次号
			String vbatchcode = getBillCardPanel().getBodyValueAt(row, "vbatchcode")==null?"":getBillCardPanel().getBodyValueAt(row, "vbatchcode").toString();
			//获得自由项一
			String vfree1 = getBillCardPanel().getBodyValueAt(row, "vfree1")==null?"":getBillCardPanel().getBodyValueAt(row, "vfree1").toString();
			
			//自由项一不为空时return.
			if (vbatchcode.equals("")||cwarehouseid.equals("")||cinventoryid.equals("")||!vfree1.equals("")) { 
				return; 
			}
			
			String sql = getQuSqlbyvbatchcode(cwarehouseid,cinventoryid,vbatchcode);
			
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			MapListProcessor alp = new MapListProcessor();
			setCspaceidAndCvendoridAndNoutnum(row,query,alp,sql);
		}else {
			afterSpaceEdit(e);
		}

		} catch (Exception error) {
			error.printStackTrace();
		}

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
	 * 依据批次号获取对应的货位、供应商、数量（去除冻结数），并对当前操作行进行赋值 
	 * add by shikun 2014-11-18 
	 * @throws BusinessException 
	 * */
	@SuppressWarnings("unchecked")
	private void setCspaceidAndCvendoridAndNoutnum(int row, IUAPQueryBS query, MapListProcessor alp, String sql) throws BusinessException {
		ArrayList addrList = (ArrayList) query.executeQuery(sql, alp);
		if (addrList != null && addrList.size() > 0) {
			Map addrMap = (Map) addrList.get(0);
			String cvendorid = addrMap.get("cvendorid") == null ? "" : addrMap.get("cvendorid").toString();
			String cspaceid = addrMap.get("cspaceid") == null ? "" : addrMap.get("cspaceid").toString();
			String csname = addrMap.get("csname") == null ? "" : addrMap.get("csname").toString();
			UFDouble wglsl = new UFDouble(addrMap.get("wglsl").toString()) == null ? new UFDouble(0.00) : new UFDouble(addrMap.get("wglsl").toString());
			if (wglsl.doubleValue()==0) {
				showErrorMessage("货位调整的数量已经为0，请注意！");
			}
			getBillCardPanel().getBillModel().setValueAt(cvendorid, row, "cvendorid");
			getBillCardPanel().getBillModel().setValueAt(wglsl, row, "noutnum");
			
			//edit by shikun 2014-11-18
			if (!"".equals(cspaceid)) {
				getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");
				setSpace2(cspaceid, csname, row);
			}
			//end
			
			getBillCardPanel().getBillModel().execLoadFormula();
		}
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
				showWarningMessage("货位调整的数量已经为0，请注意！");
			}
			getBillCardPanel().getBillModel().setValueAt(vbatchcode, row, "vbatchcode");
			getBillCardPanel().getBillModel().setValueAt(cvendorid, row, "cvendorid");
			getBillCardPanel().getBillModel().setValueAt(wglsl, row, "noutnum");
			
			//edit by shikun 2014-10-13
			if (!"".equals(cspaceid)) {
				getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");
				setSpace2(cspaceid, csname, row);
			}
			//end
			
			getBillCardPanel().getBillModel().execLoadFormula();
		}
	}

	
	@SuppressWarnings("unchecked")
	private void setSpace2(String cspaceid, String csname, int row) {
		// TODO Auto-generated method stub

		String cInsname = (String) getBillCardPanel().getBodyValueAt(row, "vspace2name");
		String cInspaceid = (String) getBillCardPanel().getBodyValueAt(row, "cspace2id");
		String cOutsname = csname;
		String cOutspaceid = cspaceid;
		
		if (cOutspaceid == null) cOutsname = null;
		if (cInspaceid == null) {
			cInsname = null;
		}

		if ((cOutspaceid != null) && (cOutspaceid.equals(cInspaceid))) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000145"));
			cOutsname = null;
			cOutspaceid = null;
		} else {
			showHintMessage("");
		}
		this.m_voBill.setItemValue(row, "cspaceid", cOutspaceid);
		this.m_voBill.setItemValue(row, "vspacename", cOutsname);
		this.m_voBill.setItemValue(row, "cspace2id", cInspaceid);
		this.m_voBill.setItemValue(row, "vspace2name", cInsname);

		getBillCardPanel().setBodyValueAt(cOutsname, row, "vspacename");
		getBillCardPanel().setBodyValueAt(cOutspaceid, row, "cspaceid");
		getBillCardPanel().setBodyValueAt(cInsname, row, "vspace2name");
		getBillCardPanel().setBodyValueAt(cInspaceid, row, "cspace2id");

		LocatorVO voInSpace = new LocatorVO();
		LocatorVO voOutSpace = new LocatorVO();
		LocatorVO[] lvos = new LocatorVO[2];
		
		LocatorVO[] locvos = (LocatorVO[]) this.m_alLocatorData.get(row);
		if (locvos!=null) {
			this.m_alLocatorData.remove(row);
		}
		
		this.m_alLocatorData.add(row, lvos);

		lvos[0] = null;
		lvos[1] = null;

		UFDouble dTempNum = null;
		UFDouble dTempAstNum = null;
		UFDouble dTempGrossNum = null;
		Object oTempValue = getBillCardPanel().getBodyValueAt(row, "noutnum");
		if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) dTempNum = new UFDouble(oTempValue.toString());
		oTempValue = getBillCardPanel().getBodyValueAt(row, "noutassistnum");
		if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) dTempAstNum = new UFDouble(oTempValue.toString());
		oTempValue = getBillCardPanel().getBodyValueAt(row, "noutgrossnum");
		if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) {
			dTempGrossNum = new UFDouble(oTempValue.toString());
		}

		if ((cOutspaceid != null) && (cOutspaceid.trim().length() > 0)) {
			voOutSpace.setCspaceid(cOutspaceid);
			voOutSpace.setVspacename(cOutsname);
			voOutSpace.setNoutspacenum(dTempNum);
			voOutSpace.setNoutspaceassistnum(dTempAstNum);
			voOutSpace.setNoutgrossnum(dTempGrossNum);
			lvos[0] = voOutSpace;
		}

		if ((cInspaceid != null) && (cInspaceid.trim().length() > 0)) {
			voInSpace.setCspaceid(cInspaceid);
			voInSpace.setVspacename(cInsname);
			voInSpace.setNinspacenum(dTempNum);
			voInSpace.setNinspaceassistnum(dTempAstNum);
			voInSpace.setNingrossnum(dTempGrossNum);
			lvos[1] = voInSpace;
		}
		
		this.m_alLocatorData.set(row, lvos);
		if (this.m_alSerialData != null) this.m_alSerialData.set(row, null);
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
		.append("    and wglsl > 0 ") 
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
	 * 依据批次号查询对应的货位、最大数量（去除冻结数）、供应商，查询语句。
	 * add by shikun 2014-04-12 
	 * */
	private String getQuSqlbyvbatchcode(String cwarehouseid, String cinventoryid, String vbatchcode) {
		StringBuffer sb = new StringBuffer("");
		sb.append(" select wglsl, cspaceid,csname, cvendorid ") 
		.append("   from (select SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
		.append("                    nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0)) wglsl, ") 
		.append("                v1.cspaceid,car.csname, ") 
		.append("                v1.cvendorid ") 
		.append("           from v_ic_onhandnum6 v1 ") 
		.append("           left join bd_cargdoc car on nvl(car.dr,0)=0 and car.pk_cargdoc = v1.cspaceid ")
		.append("           left join ic_freeze icf on v1.cinventoryid = icf.cinventoryid ") 
		.append("                                  and icf.ccalbodyid = v1.ccalbodyid ") 
		.append("                                  and icf.cwarehouseid = v1.cwarehouseid ") 
		.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
		.append("                                  and icf.pk_corp = v1.pk_corp ") 
		.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
		.append("                                  and nvl(v1.cvendorid,'byzgyh') = nvl(icf.cvendorid,'byzgyh') ") 
		.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
		.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
		.append("          where v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
		.append("            and v1.vbatchcode = '"+vbatchcode+"' ") 
		.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
		.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
		.append("          group by v1.cspaceid,car.csname, v1.cvendorid) ")
		.append("  where wglsl = ") 
		.append("        (select max(SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
		.append("                        nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0))) wglsl ") 
		.append("           from v_ic_onhandnum6 v1 ") 
		.append("           left join ic_freeze icf on v1.cinventoryid = icf.cinventoryid ") 
		.append("                                  and icf.ccalbodyid = v1.ccalbodyid ") 
		.append("                                  and icf.cwarehouseid = v1.cwarehouseid ") 
		.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
		.append("                                  and icf.pk_corp = v1.pk_corp ") 
		.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
		.append("                                  and nvl(v1.cvendorid,'byzgyh') = nvl(icf.cvendorid,'byzgyh') ") 
		.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
		.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
		.append("          where v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
		.append("            and v1.vbatchcode = '"+vbatchcode+"' ") 
		.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
		.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
		.append("          group by v1.cspaceid, v1.cvendorid) ") ;
		return sb.toString();
	}
	/**
	 * 功能：存货编码，转出货位编辑后事件
	 * 作者：王凯飞
	 * 日期：2013-11-13
	 * start
	 * */

	@SuppressWarnings("unchecked")
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if (e.getKey().equals("cinventorycode"))//存货编码
		{
			Object cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject();//获取表头字段仓库字段
			int row = e.getRow();//获得操作行
			Object cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid");//获得操作行的存货管理档案主键
			if (cwarehouseid==null||cinventoryid==null) {
				getBillCardPanel().getBillModel().setValueAt(null, row, "noutnum");
				getBillCardPanel().getBillModel().setValueAt(null, row, "cspaceid");
				
				return;
			}
			StringBuffer str = new StringBuffer("");//定义一个字符串缓存，用于装SQL语句
//			str.append(" select cspaceid,anum from ").append(" (select cspaceid, ").append("          SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0)) anum ").append("     from v_ic_onhandnum6 v1 ").append("    where pk_corp = '" + getCorpPrimaryKey() + "' ").append("      and cwarehouseid = '" + cwarehouseid + "' ").append("      and cinventoryid = '" + cinventoryid + "' ").append("    group by cspaceid) ").append(" where anum > 0 ").append(" and anum in(select max(SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0))) anum ").append("     from v_ic_onhandnum6 v1 ").append("    where pk_corp = '" + getCorpPrimaryKey() + "' ").append("      and cwarehouseid = '" + cwarehouseid + "' ").append("      and cinventoryid = '" + cinventoryid + "' ").append("      group by cspaceid) ");
			str.append(" select wglsl, cspaceid, csname ") 
			.append("   from (select SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
			.append("                    nvl(icf.nfreezenum, 0.0)+nvl(icf.ndefrznum, 0.0)) wglsl, ") 
			.append("                v1.cspaceid, car.csname ") 
			.append("           from v_ic_onhandnum6 v1 ") 
			.append("           left join bd_cargdoc car on nvl(car.dr,0)=0 and car.pk_cargdoc = v1.cspaceid ")
			.append("           left join ic_freeze icf ") 
			.append("             on v1.cinventoryid = icf.cinventoryid ") 
			.append("            and icf.ccalbodyid = v1.ccalbodyid ") 
			.append("            and icf.cwarehouseid = v1.cwarehouseid ") 
			.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
			.append("                                  and icf.pk_corp = v1.pk_corp ") 
			.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
			.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
			.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
			.append("          where 1 = 1 ") 
			.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
			.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
			.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
			.append("          group by v1.cspaceid,car.csname) ") 
			.append("  where 1 = 1 ") 
			.append("    and wglsl > 0 ") 
			.append("    and wglsl in (select max(SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
			.append("                    nvl(icf.nfreezenum, 0.0)+nvl(icf.ndefrznum, 0.0))) wglsl ") 
			.append("           from v_ic_onhandnum6 v1 ") 
			.append("           left join ic_freeze icf ") 
			.append("             on v1.cinventoryid = icf.cinventoryid ") 
			.append("            and icf.ccalbodyid = v1.ccalbodyid ") 
			.append("            and icf.cwarehouseid = v1.cwarehouseid ") 
			.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
			.append("                                  and icf.pk_corp = v1.pk_corp ") 
			.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
			.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
			.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
			.append("          where 1 = 1 ") 
			.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
			.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
			.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
			.append("          group by v1.cspaceid) ");
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());//使用IUAPQueryBS服务组件，进得查询操作
			MapListProcessor alp = new MapListProcessor();//集合处理器
			Object obj = null;//定义一个对象，用于存放query.executeQuery()的查询结果
			try {
				obj = query.executeQuery(str.toString(), alp);//执得查询
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			ArrayList addrList = (ArrayList) obj;//将查询结果转换成数组列表，放入addList中
			if (addrList != null && addrList.size() > 0) {
				Map addrMap = (Map) addrList.get(0);//取addrList数组中的第0行的值，放入addrMap中
				UFDouble addrName = new UFDouble(addrMap.get("wglsl").toString());//用addName存放addMap中anum数量的值
				String cspaceid = addrMap.get("cspaceid")==null?"":addrMap.get("cspaceid").toString();//用cspaceid存放addMap中cspaceid货位的值
				String csname = addrMap.get("csname")==null?"":addrMap.get("csname").toString();//用cspaceid存放addMap中cspaceid货位的值
				getBillCardPanel().getBillModel().setValueAt(addrName, row, "noutnum");//将装有数量的值放入noutnum字段
				
				//edit by shikun 2014-10-13
//				getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");//将装有货位的值放入cspaceid字段
				if (!"".equals(cspaceid)) {
					getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");
					setSpace2(cspaceid, csname, row);
				}
				//end
				
			}
			getBillCardPanel().getBillModel().execLoadFormula();//选中以上的面板中的模型，执行公式
		} 
		else if (e.getKey().equals("vspacename")) {//转出货位
			Object cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject();
			int row = e.getRow();
			Object cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid");
			Object cspaceid = getBillCardPanel().getBodyValueAt(row, "cspaceid");
			if (cwarehouseid==null||cinventoryid==null||cspaceid==null) {
				getBillCardPanel().getBillModel().setValueAt(null, row, "noutnum");
				return;
			}
			StringBuffer str = new StringBuffer("");
//			str.append(" select cspaceid, SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0)) anum ").append("   from v_ic_onhandnum6 v1 ").append("  where pk_corp = '" + getCorpPrimaryKey() + "' ").append("    and cwarehouseid = '" + cwarehouseid + "' ").append("    and cinventoryid = '" + cinventoryid + "' ").append("    and cspaceid='" + cspaceid + "' ").append("  group by cspaceid ");
			str.append(" select SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
			.append("                    nvl(icf.nfreezenum, 0.0)+nvl(icf.ndefrznum, 0.0)) wglsl, ") 
			.append("                v1.cspaceid,car.csname ") 
			.append("           from v_ic_onhandnum6 v1 ") 
			.append("           left join bd_cargdoc car on nvl(car.dr,0)=0 and car.pk_cargdoc = v1.cspaceid ")
			.append("           left join ic_freeze icf ") 
			.append("             on v1.cinventoryid = icf.cinventoryid ") 
			.append("            and icf.ccalbodyid = v1.ccalbodyid ") 
			.append("            and icf.cwarehouseid = v1.cwarehouseid ") 
			.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
			.append("                                  and icf.pk_corp = v1.pk_corp ") 
			.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
			.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
			.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
			.append("          where 1 = 1 ") 
			.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
			.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
			.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
			.append("            and v1.cspaceid='"+cspaceid+"' ") 
			.append("          group by v1.cspaceid,car.csname ");
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			MapListProcessor alp = new MapListProcessor();
			Object obj = null;
			try {
				obj = query.executeQuery(str.toString(), alp);
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
			ArrayList addrList = (ArrayList) obj;
			if (addrList != null && addrList.size() > 0) {
				Map addrMap = (Map) addrList.get(0);
				UFDouble addrName = new UFDouble(addrMap.get("wglsl").toString());
				getBillCardPanel().getBillModel().setValueAt(addrName, row, "noutnum");
			} else {
				getBillCardPanel().getBillModel().setValueAt(null, row, "noutnum");
			}
		} 
		else if (e.getKey().equals("cwarehouseid")) {//仓库
			Object cwarehouseid = getBillCardPanel().getHeadItem("cwarehouseid").getValueObject();
			if (cwarehouseid==null) {
				return;
			}
			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			int rows = getBillCardPanel().getBillTable().getRowCount();
			for (int row = 0; row < rows; row++) {
				Object cinventoryid = getBillCardPanel().getBodyValueAt(row, "cinventoryid");
				if (cinventoryid != null) {
					StringBuffer str = new StringBuffer("");
	//				str.append(" select cspaceid,anum from ").append(" (select cspaceid, ").append("          SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0)) anum ").append("     from v_ic_onhandnum6 v1 ").append("    where pk_corp = '" + getCorpPrimaryKey() + "' ").append("      and cwarehouseid = '" + cwarehouseid + "' ").append("      and cinventoryid = '" + cinventoryid + "' ").append("    group by cspaceid) ").append(" where anum > 0 ").append(" and anum in(select max(SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0))) anum ").append("     from v_ic_onhandnum6 v1 ").append("    where pk_corp = '" + getCorpPrimaryKey() + "' ").append("      and cwarehouseid = '" + cwarehouseid + "' ").append("      and cinventoryid = '" + cinventoryid + "' ").append("      group by cspaceid) ");
					str.append(" select wglsl, cspaceid,csname ") 
					.append("   from (select SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
					.append("                    nvl(icf.nfreezenum, 0.0) + nvl(icf.ndefrznum, 0.0)) wglsl, ") 
					.append("                v1.cspaceid,car.csname ") 
					.append("           from v_ic_onhandnum6 v1 ") 
					.append("           left join bd_cargdoc car on nvl(car.dr,0)=0 and car.pk_cargdoc = v1.cspaceid ")
					.append("           left join ic_freeze icf ") 
					.append("             on v1.cinventoryid = icf.cinventoryid ") 
					.append("            and icf.ccalbodyid = v1.ccalbodyid ") 
					.append("            and icf.cwarehouseid = v1.cwarehouseid ") 
					.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
					.append("                                  and icf.pk_corp = v1.pk_corp ") 
					.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
					.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
					.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
					.append("          where 1 = 1 ") 
					.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
					.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
					.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
					.append("          group by v1.cspaceid,car.csname) ") 
					.append("  where 1 = 1 ") 
					.append("    and wglsl > 0 ") 
					.append("    and wglsl in (select max(SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0) - ") 
					.append("                    nvl(icf.nfreezenum, 0.0)+nvl(icf.ndefrznum, 0.0))) wglsl ") 
					.append("           from v_ic_onhandnum6 v1 ") 
					.append("           left join ic_freeze icf ") 
					.append("             on v1.cinventoryid = icf.cinventoryid ") 
					.append("            and icf.ccalbodyid = v1.ccalbodyid ") 
					.append("            and icf.cwarehouseid = v1.cwarehouseid ") 
					.append("                                  and nvl(icf.cspaceid,'byzgyh') = nvl(v1.cspaceid,'byzgyh') ") 
					.append("                                  and icf.pk_corp = v1.pk_corp ") 
					.append("                                  and nvl(v1.vfree1,'byzgyh') = nvl(icf.vfree1,'byzgyh') ") 
					.append("                                  and nvl(v1.vbatchcode,'byzgyh') = nvl(icf.vbatchcode,'byzgyh') ") 
					.append("                                  and nvl(icf.dr, 0) = 0 and nvl(icf.cthawpersonid,'')='' ") 
					.append("          where 1 = 1 ") 
					.append("            and v1.pk_corp = '"+getCorpPrimaryKey()+"' ") 
					.append("            and v1.cwarehouseid = '"+cwarehouseid+"' ") 
					.append("            and v1.cinventoryid = '"+cinventoryid+"' ") 
					.append("          group by v1.cspaceid) ");
					MapListProcessor alp = new MapListProcessor();
					Object obj = null;
					try {
						obj = query.executeQuery(str.toString(), alp);//执得查询
					} catch (BusinessException e1) {
						e1.printStackTrace();
					}
					ArrayList addrList = (ArrayList) obj;
					if (addrList != null && addrList.size() > 0) {
						Map addrMap = (Map) addrList.get(0);
						UFDouble addrName = new UFDouble(addrMap.get("wglsl").toString());
						String cspaceid = addrMap.get("cspaceid")==null?"":addrMap.get("cspaceid").toString();
						String csname = addrMap.get("csname")==null?"":addrMap.get("csname").toString();
						getBillCardPanel().getBillModel().setValueAt(addrName, row, "noutnum");
						
						//edit by shikun 2014-10-13
//						getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");//将装有货位的值放入cspaceid字段
						if (!"".equals(cspaceid)) {
							getBillCardPanel().getBillModel().setValueAt(cspaceid, row, "cspaceid");
							setSpace2(cspaceid, csname, row);
						}
						//end
						
					}else{
						getBillCardPanel().getBillModel().setValueAt(null, row, "noutnum");
						getBillCardPanel().getBillModel().setValueAt(null, row, "cspaceid");
					}
				}
			}
			getBillCardPanel().getBillModel().execLoadFormula();
		}

	}

	/**
	 * 功能：存货编码，转出货位编辑后事件
	 * 作者：王凯飞
	 * 日期：2013-11-13
	 * end
	 * */

	@SuppressWarnings("deprecation")
	protected void afterBillItemSelChg(int iRow, int iCol) {
		String sItemKey = getBillCardPanel().getBillModel().getBodyKeyByCol(iCol).trim();

		if (sItemKey.equals("vspacename")) {
			filterSpace(iRow);
		} else if (sItemKey.equals("vspace2name")) filterSpace2(iRow);
	}

	@SuppressWarnings("restriction")
	public void afterCalbodyEdit(BillEditEvent e) {
		try {
			super.afterCalbodyEdit(e);

			String sNewID = ((UIRefPane) getBillCardPanel().getHeadItem(e.getKey()).getComponent()).getRefPK();

			String[] sConstraint = null;
			if (sNewID != null) {
				sConstraint = new String[1];
				sConstraint[0] = (" and csflag='Y' AND pk_calbody='" + sNewID + "'");
			} else {
				sConstraint = new String[1];
				sConstraint[0] = " and csflag='Y' ";
			}

			BillItem bi = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey);
			RefFilter.filtWh(bi, this.m_sCorpID, sConstraint);

			BillItem biWh = getBillCardPanel().getHeadItem(this.m_sMainWhItemKey);
			if (biWh != null) {
				biWh.setValue(null);
			}

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}
	}

	public void afterSpaceEdit(BillEditEvent e) {
		UIRefPane refOutSpace = null;
		if (getBillCardPanel().getBodyItem("vspacename") != null) {
			refOutSpace = (UIRefPane) getBillCardPanel().getBodyItem("vspacename").getComponent();
		}

		UIRefPane refInSpace = null;
		if (getBillCardPanel().getBodyItem("vspace2name") != null) {
			refInSpace = (UIRefPane) getBillCardPanel().getBodyItem("vspace2name").getComponent();
		}

		if ((refOutSpace == null) || (refInSpace == null)) {
			SCMEnv.out("set locator ref pls.");
			return;
		}

		String sItemKey = e.getKey();

		int row = e.getRow();

		String cOutsname = null;
		String cOutspaceid = null;
		String cInsname = null;
		String cInspaceid = null;

		if (e.getKey().equals("vspacename")) {
			cInsname = (String) getBillCardPanel().getBodyValueAt(row, "vspace2name");
			cInspaceid = (String) getBillCardPanel().getBodyValueAt(row, "cspace2id");
			cOutsname = refOutSpace.getRefName();
			cOutspaceid = refOutSpace.getRefPK();
		} else if (e.getKey().equals("vspace2name")) {
			cInsname = refInSpace.getRefName();
			cInspaceid = refInSpace.getRefPK();
			cOutsname = (String) getBillCardPanel().getBodyValueAt(row, "vspacename");
			cOutspaceid = (String) getBillCardPanel().getBodyValueAt(row, "cspaceid");
		} else {
			cInsname = (String) getBillCardPanel().getBodyValueAt(row, "vspace2name");
			cInspaceid = (String) getBillCardPanel().getBodyValueAt(row, "cspace2id");

			cOutsname = (String) getBillCardPanel().getBodyValueAt(row, "vspacename");
			cOutspaceid = (String) getBillCardPanel().getBodyValueAt(row, "cspaceid");
		}

		if (cOutspaceid == null) cOutsname = null;
		if (cInspaceid == null) {
			cInsname = null;
		}

		if ((cOutspaceid != null) && (cOutspaceid.equals(cInspaceid))) {
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000145"));

			if ((sItemKey != null) && (sItemKey.equals("vspace2name"))) {
				cInsname = null;
				cInspaceid = null;
			} else {
				cOutsname = null;
				cOutspaceid = null;
			}
		} else {
			showHintMessage("");
		}
		this.m_voBill.setItemValue(row, "cspaceid", cOutspaceid);
		this.m_voBill.setItemValue(row, "vspacename", cOutsname);
		this.m_voBill.setItemValue(row, "cspace2id", cInspaceid);
		this.m_voBill.setItemValue(row, "vspace2name", cInsname);

		getBillCardPanel().setBodyValueAt(cOutsname, row, "vspacename");
		getBillCardPanel().setBodyValueAt(cOutspaceid, row, "cspaceid");
		getBillCardPanel().setBodyValueAt(cInsname, row, "vspace2name");
		getBillCardPanel().setBodyValueAt(cInspaceid, row, "cspace2id");

		LocatorVO voInSpace = new LocatorVO();
		LocatorVO voOutSpace = new LocatorVO();
		LocatorVO[] lvos = new LocatorVO[2];

		lvos[0] = null;
		lvos[1] = null;

		UFDouble dTempNum = null;
		UFDouble dTempAstNum = null;
		UFDouble dTempGrossNum = null;
		Object oTempValue = getBillCardPanel().getBodyValueAt(row, "noutnum");
		if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) dTempNum = new UFDouble(oTempValue.toString());
		oTempValue = getBillCardPanel().getBodyValueAt(row, "noutassistnum");
		if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) dTempAstNum = new UFDouble(oTempValue.toString());
		oTempValue = getBillCardPanel().getBodyValueAt(row, "noutgrossnum");
		if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) {
			dTempGrossNum = new UFDouble(oTempValue.toString());
		}

		if ((cOutspaceid != null) && (cOutspaceid.trim().length() > 0)) {
			voOutSpace.setCspaceid(cOutspaceid);
			voOutSpace.setVspacename(cOutsname);
			voOutSpace.setNoutspacenum(dTempNum);
			voOutSpace.setNoutspaceassistnum(dTempAstNum);
			voOutSpace.setNoutgrossnum(dTempGrossNum);
			lvos[0] = voOutSpace;
		}

		if ((cInspaceid != null) && (cInspaceid.trim().length() > 0)) {
			voInSpace.setCspaceid(cInspaceid);
			voInSpace.setVspacename(cInsname);
			voInSpace.setNinspacenum(dTempNum);
			voInSpace.setNinspaceassistnum(dTempAstNum);
			voInSpace.setNingrossnum(dTempGrossNum);
			lvos[1] = voInSpace;
		}

		this.m_alLocatorData.set(row, lvos);

		if ((sItemKey == null) || (!sItemKey.equals("vspacename")) || (this.m_alSerialData == null) || (this.m_alSerialData.size() <= row)) return;
		this.m_alSerialData.set(row, null);
	}

	public boolean beforeBillItemEdit(BillEditEvent e) {

		return true;
	}

	protected void beforeBillItemSelChg(int iRow, int iCol) {
	}

	/**
	 * add by wkf start 2013-11-11 
	 * 功能：转出货位参照
	 * 作者：王凯飞
	 * 时间：2013-11-11
	 * */
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		if (e.getKey().equals("vspacename")) {//转出货位
			int row = getBillCardPanel().getBillTable().getSelectedRow();
			Object pk_invmandoc = getBillCardPanel().getBodyValueAt(row, "cinventoryid");//获取到存货管理档案id

			String cwarehouseid = (String) getBillCardPanel().getHeadItem("cwarehouseid").getValueObject();//获取仓库

			UIRefPane pane = (UIRefPane) getBillCardPanel().getBodyItem("vspacename").getComponent();//获得转出货位的组件
			StringBuffer where = new StringBuffer("");
			where.append("  and pk_cargdoc in (select a.cspaceid from  ").append("  (select cspaceid, SUM(nvl(v1.ninspacenum, 0.0) - nvl(v1.noutspacenum, 0.0)) anum ").append("   from v_ic_onhandnum6 v1 ").append("  where pk_corp = '" + getCorpPrimaryKey() + "' ").append("    and cwarehouseid ='" + cwarehouseid + "' ").append(" and cinventoryid = '" + pk_invmandoc + "'").append("  group by cspaceid) a ").append("  where anum>0) ");
			pane.getRefModel().addWherePart(where.toString());
		}
		return super.beforeEdit(e);
	}

	/**
	 * add by wkf end 2013-11-11 
	 * 作者：王凯飞
	 * 时间：2013-11-11
	 * */

	public boolean beforeEdit(BillItemEvent e) {

		if (e.getItem().getKey().equals("vuserdef14")) {//产品线
			UIRefPane pane = (UIRefPane) ((BillItem) e.getSource()).getComponent();
			pane.getRefModel().addWherePart(" and pd_wk.pk_corp = '" + getCorpPrimaryKey() + "' ");
		}
		if (e.getItem().getKey().equals("vuserdef15")) {//产品线
			UIRefPane pane = (UIRefPane) ((BillItem) e.getSource()).getComponent();

			pane.getRefModel().addWherePart(" and  mm_mo.pk_moid in (select pk_moid from mm_mokz where gzzxid = '" + ((UIRefPane) getBillCardPanel().getHeadItem("vuserdef14").getComponent()).getRefPK() + "') ");
			//n(select pk_moid from mm_mokz where gzzxid
		}

		return true;
	}

	public boolean isCellEditable(boolean value, int row, String itemkey) {
		if (this.m_iMode == 3) return false;

		getBillCardPanel().stopEditing();
		BillItem bi = getBillCardPanel().getBodyItem(itemkey);
		WhVO voWh = this.m_voBill.getWh();

		if (!itemkey.equals("cinventorycode")) {
			Object oTempInvCode = getBillCardPanel().getBodyValueAt(row, "cinventorycode");

			Object oTempInvName = getBillCardPanel().getBodyValueAt(row, "invname");

			if ((oTempInvCode == null) || (oTempInvCode.toString().trim().length() == 0)) {
				bi.setEnabled(false);
				showHintMessage(NCLangRes.getInstance().getStrByID("4008bill", "UPP4008bill-000026"));

				return false;
			}
		}
		if (itemkey.equals("vspacename")) {
			if ((voWh == null) || (voWh.getIsLocatorMgt() == null) || (voWh.getIsLocatorMgt().intValue() == 0)) {
				bi.setEnabled(false);
				return false;
			}
			filterSpace(row);
			bi.setEnabled(true);
			return true;
		}
		if (itemkey.equals("vspace2name")) {
			if ((voWh == null) || (voWh.getIsLocatorMgt() == null) || (voWh.getIsLocatorMgt().intValue() == 0)) {
				bi.setEnabled(false);
				return false;
			}
			filterSpace2(row);
			bi.setEnabled(true);
			return true;
		}

		return super.isCellEditable(value, row, itemkey);
	}

	protected void checkNum(int row) {
		UFDouble dNum = null;

		LocatorVO[] voaLoc = (LocatorVO[]) (LocatorVO[]) this.m_alLocatorData.get(row);

		if (getBillCardPanel().getBodyItem(this.m_sNumItemKey) != null) {
			if (getBillCardPanel().getBodyValueAt(row, this.m_sNumItemKey) != null) {
				dNum = (UFDouble) getBillCardPanel().getBodyValueAt(row, this.m_sNumItemKey);
			}
			if ((dNum != null) && (dNum.doubleValue() <= 0.0D)) {
				showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000144"));

				getBillCardPanel().setBodyValueAt(null, row, this.m_sNumItemKey);
				dNum = null;
			}

			if ((voaLoc != null) && (voaLoc.length >= 2)) {
				showHintMessage("");

				if (voaLoc[0] != null) {
					voaLoc[0].setNoutspacenum(dNum);
				}
				if (voaLoc[1] != null) {
					voaLoc[1].setNinspacenum(dNum);
				}
			}
		}
		dNum = null;

		if (getBillCardPanel().getBodyItem(this.m_sAstItemKey) != null) {
			if (getBillCardPanel().getBodyValueAt(row, this.m_sAstItemKey) != null) {
				dNum = (UFDouble) getBillCardPanel().getBodyValueAt(row, this.m_sAstItemKey);
			}
			if ((dNum != null) && (dNum.doubleValue() <= 0.0D)) {
				showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000144"));

				getBillCardPanel().setBodyValueAt(null, row, this.m_sAstItemKey);
				dNum = null;
			}

			if ((voaLoc != null) && (voaLoc.length >= 2)) {
				showHintMessage("");

				if (voaLoc[0] != null) {
					voaLoc[0].setNoutspaceassistnum(dNum);
				}
				if (voaLoc[1] != null) {
					voaLoc[1].setNinspaceassistnum(dNum);
				}
			}
		}

		dNum = null;

		if (getBillCardPanel().getBodyItem(this.m_sNgrossnum) != null) {
			if (getBillCardPanel().getBodyValueAt(row, this.m_sNgrossnum) != null) {
				dNum = (UFDouble) getBillCardPanel().getBodyValueAt(row, this.m_sNgrossnum);
			}
			if ((dNum != null) && (dNum.doubleValue() <= 0.0D)) {
				showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000144"));

				getBillCardPanel().setBodyValueAt(null, row, this.m_sNgrossnum);
				dNum = null;
			}

			if ((voaLoc == null) || (voaLoc.length < 2)) return;
			showHintMessage("");

			if (voaLoc[0] != null) {
				voaLoc[0].setNoutgrossnum(dNum);
			}
			if (voaLoc[1] != null) voaLoc[1].setNingrossnum(dNum);
		}
	}

	public boolean checkVO1() {
		try {
			VOCheck.checkNullVO(this.m_voBill);

			VOCheck.checkNumInput(this.m_voBill.getChildrenVO(), this.m_sNumItemKey);

			VOCheck.validate(this.m_voBill, GeneralMethod.getHeaderCanotNullString(getBillCardPanel()), GeneralMethod.getBodyCanotNullString(getBillCardPanel()));

			VOCheck.checkFreeItemInput(this.m_voBill, this.m_sNumItemKey);

			VOCheck.checkLotInput(this.m_voBill, this.m_sNumItemKey);

			VOCheck.checkAssistUnitInput(this.m_voBill, this.m_sNumItemKey, this.m_sAstItemKey);

			VOCheck.checkInvalidateDateInput(this.m_voBill, this.m_sNumItemKey);

			VOCheck.checkGreaterThanZeroInput(this.m_voBill.getChildrenVO(), "nprice", NCLangRes.getInstance().getStrByID("common", "UC000-0000741"));

			VOCheck.checkGreaterThanZeroInput(this.m_voBill.getChildrenVO(), this.m_sNumItemKey, NCLangRes.getInstance().getStrByID("common", "UC000-0002282"));
			VOCheck.checkGreaterThanZeroInput(this.m_voBill.getChildrenVO(), this.m_sAstItemKey, NCLangRes.getInstance().getStrByID("common", "UC000-0002282"));

			VOCheck.checkNotZeroInput(this.m_voBill.getChildrenVO(), this.m_sNumItemKey, NCLangRes.getInstance().getStrByID("common", "UC000-0002282"));
			VOCheck.checkNotZeroInput(this.m_voBill.getChildrenVO(), this.m_sAstItemKey, NCLangRes.getInstance().getStrByID("common", "UC000-0003971"));

			VOCheck.checkSNInput(this.m_voBill.getChildrenVO(), this.m_sNumItemKey);

			VOCheck.checkGrossNumInput(this.m_voBill.getChildrenVO(), this.m_sNgrossnum, this.m_sNumItemKey);

			return true;
		} catch (ICDateException e) {
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

			showErrorMessage(sErrorMessage);

			return false;
		} catch (ICNullFieldException e) {
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

			showErrorMessage(sErrorMessage);

			return false;
		} catch (ICNumException e) {
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

			showErrorMessage(sErrorMessage);

			return false;
		} catch (ICPriceException e) {
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

			showErrorMessage(sErrorMessage);

			return false;
		} catch (ICSNException e) {
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

			showErrorMessage(sErrorMessage);

			return false;
		} catch (ICLocatorException e) {
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

			showErrorMessage(sErrorMessage);

			return false;
		} catch (ICRepeatException e) {
			String sErrorMessage = GeneralMethod.getBodyErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

			showErrorMessage(sErrorMessage);

			return false;
		} catch (ICHeaderNullFieldException e) {
			String sErrorMessage = GeneralMethod.getHeaderErrorMessage(getBillCardPanel(), e.getErrorRowNums(), e.getHint());

			showErrorMessage(sErrorMessage);

			return false;
		} catch (NullFieldException e) {
			showErrorMessage(e.getHint());

			return false;
		} catch (ValidationException e) {
			SCMEnv.out("校验异常！其他未知故障...");
			handleException(e);
		}
		return false;
	}

	protected boolean checkVO(GeneralBillVO voBill) {
		return checkVO();
	}

	public void filterRef(String sCorpID) {
		super.filterRef(sCorpID);
		try {
			((UIRefPane) getBillCardPanel().getHeadItem(this.m_sMainWhItemKey).getComponent()).setWhereString("csflag='Y' AND " + RefFilter.getWhFiltStr(this.m_sCorpID, null));
		} catch (Exception e) {
		}
	}

	private void filterSpace(int row) {
		if (this.m_voBill == null) return;
		String sItemKey = "vspacename";
		WhVO voWh = this.m_voBill.getWh();

		String sName = (String) getBillCardPanel().getBodyValueAt(row, sItemKey);
		String spk = (String) getBillCardPanel().getBodyValueAt(row, "cspaceid");
		if (GenMethod.isNull(spk)) {
			spk = null;
			getBillCardPanel().setBodyValueAt(null, row, "cspaceid");
		}

		getLocatorRefPane().setOldValue(sName, null, spk);
		getLocatorRefPane().setPK(spk);

		getLocatorRefPane().setParam(voWh, this.m_voBill.getItemInv(row));
	}

	private void filterSpace2(int row) {
		if (this.m_voBill == null) return;
		String sItemKey = "vspace2name";
		WhVO voWh = this.m_voBill.getWh();

		String sName = (String) getBillCardPanel().getBodyValueAt(row, sItemKey);
		String spk = (String) getBillCardPanel().getBodyValueAt(row, "cspace2id");
		if (GenMethod.isNull(spk)) {
			spk = null;
			getBillCardPanel().setBodyValueAt(null, row, "cspace2id");
		}
		getLocatorRefPane2().setOldValue(sName, null, spk);
		getLocatorRefPane2().setPK(spk);

		getLocatorRefPane2().setParam(voWh, this.m_voBill.getItemInv(row));
	}

	protected QueryConditionDlgForBill getConditionDlg() {
		if (this.ivjQueryConditionDlg == null) {
			this.ivjQueryConditionDlg = super.getConditionDlg();

			this.ivjQueryConditionDlg.setCombox("qbillstatus", new String[][] {
				{
						"A", NCLangRes.getInstance().getStrByID("4008spec", "UPPSCMCommon-000217")
				}
			});
		}

		return this.ivjQueryConditionDlg;
	}

	protected boolean getIsInvTrackedBill(InvVO invvo) {
		return false;
	}

	protected LocatorRefPane getLocatorRefPane() {
		if (this.m_refLocator == null) {
			try {
				this.m_refLocator = new LocatorRefPane(-1);
				this.m_refLocator.setName("LotNumbRefPane");
				this.m_refLocator.setLocation(38, 1);

				this.m_refLocator.setInOutFlag(-1);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.m_refLocator;
	}

	private LocatorRefPane getLocatorRefPane2() {
		if (this.m_refLocator2 == null) {
			try {
				this.m_refLocator2 = new LocatorRefPane(1);
				this.m_refLocator2.setName("LotNumbRefPane2");
				this.m_refLocator2.setLocation(38, 1);

				this.m_refLocator2.setInOutFlag(1);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.m_refLocator2;
	}

	public String getTitle() {
		return super.getTitle();
	}

	public void initialize() {
		this.m_bAddBarCodeField = false;
		super.initialize();
		try {
			((UIRefPane) getBillCardPanel().getBodyItem("vuserdef5").getComponent()).setTextFieldVisible(true);
			((UIRefPane) getBillCardPanel().getHeadItem(this.m_sMainWhItemKey).getComponent()).setWhereString("csflag='Y' AND " + RefFilter.getWhFiltStr(this.m_sCorpID, null));
		} catch (Exception e) {
		}
	}

	protected void initPanel() {
		super.setBillInOutFlag(-1);

		super.setNeedBillRef(false);

		this.m_sBillTypeCode = BillTypeConst.m_spaceAdjust;

		this.m_sCurrentBillNode = "40081014";

		SCMEnv.out("disable line op.");

		getBillCardPanel().setBodyMenu(new UIMenuItem[] {
				getBillCardPanel().getAddLineMenuItem(), getBillCardPanel().getDelLineMenuItem(), getBillCardPanel().getInsertLineMenuItem(), getBillCardPanel().getCopyLineMenuItem(), getBillCardPanel().getPasteLineMenuItem()
		});
		try {
			BillData bd = getBillCardPanel().getBillData();
			if (bd != null) {
				try {
					bd.getBodyItem("vspacename").setComponent(getLocatorRefPane());
				} catch (Exception e) {
				}
				try {
					bd.getBodyItem("vspace2name").setComponent(getLocatorRefPane2());
				} catch (Exception e) {
				}
			}
			getBillCardPanel().setBillData(bd);
		} catch (Exception e) {
		}
	}

	void newMethod() {
	}

	public void onButtonClicked(ButtonObject bo) {
		getBillCardPanel().stopEditing();
		if (bo == getButtonTree().getButton("序列号")) onSNAssign();
		else if (bo == getButtonTree().getButton("查询")) onQuery();
		else if (bo == getButtonTree().getButton("增加")) onNew();
		// add by zip:2013/11/28:No 27
		else if (bo == getButtonTree().getButton("批量转入")) {
			onBatchConvert();
		}
		// end	
		else super.onButtonClicked(bo);
	}

	// add by zip:2013/11/28:No 27
	// 批量转入
	public void onBatchConvert() {
		try {
			// 1, 获取选择行
			int currRow = getBillCardPanel().getBillTable().getSelectedRow();
			if (currRow < 0) throw new Exception("请选择表体行");
			// 2, 判断选择行的一些关键字段是否填写
			String pk_calbody_key = "pk_calbody";
			String pk_invmandoc_key = "cinventoryid";
			String cwarehouseid_key = "cwarehouseid";
			String cspaceid_key = "cspaceid";
			String cspace2id_key = "cspace2id";
			String pk_calbody = (String) getBillCardPanel().getHeadItem(pk_calbody_key).getValueObject();
			String pk_invmandoc = (String) getBillCardPanel().getBodyValueAt(currRow, pk_invmandoc_key);
			String cwarehouseid = (String) getBillCardPanel().getHeadItem(cwarehouseid_key).getValueObject();
			String cspaceid = (String) getBillCardPanel().getBodyValueAt(currRow, cspaceid_key);//zc
			String cspace2id = (String) getBillCardPanel().getBodyValueAt(currRow, cspace2id_key);//zr
			if (StringUtils.isEmpty(cwarehouseid)) throw new Exception("请填写表头仓库");
			if (StringUtils.isEmpty(pk_invmandoc)) throw new Exception("请填写表体存货编码");
			if (StringUtils.isEmpty(cspaceid)) throw new Exception("请填写表体转出货位");
			if (StringUtils.isEmpty(cspace2id)) throw new Exception("请填写表体转入货位");
			// 3, 在选择行的下面插入行(行数由自由项决定)
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
			ArrayList m_alAllData = InvOnHandHelper.queryOnHandInfo(pk_corp, pk_calbody, cwarehouseid, pk_invmandoc, ClientEnvironment.getInstance().getDate().toString(), 1, 1, 1, 0);
			InvVO voInv = m_voBill.getItemInv(currRow);
			InvOnHandVO[] onHandVOs = (InvOnHandVO[]) m_alAllData.get(5);
			LotNumbRefVO[] lotVOs = null;
			if (onHandVOs != null && onHandVOs.length > 0) {
				lotVOs = new LotNumbRefVO[onHandVOs.length];
				for (int i = 0; i < onHandVOs.length; i++) {
					lotVOs[i] = new LotNumbRefVO(onHandVOs[i].getVbatchcode());
					lotVOs[i].setFreeValue("vfree0", onHandVOs[i].getVfree1());
					lotVOs[i].setAttributeValue("cinventoryid", pk_invmandoc);
					if (voInv != null) lotVOs[i].setFreeVO(voInv.getFreeItemVO());
				}
				BatchCodeDefSetTool.execFormulaBatchCode(lotVOs);
			} else {
				MessageDialog.showHintDlg(this, null, "当前存货不存在多个批次号或自由项");
				return;
			}
			BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem("vbatchcode").getComponent(), lotVOs[0].getVbatchcode(), "vbatchcode", currRow);
			afterLotEdit(e, lotVOs);
		} catch (Exception ex) {
			ex.printStackTrace();
			MessageDialog.showErrorDlg(this, "错误", ex.getMessage());
		}
	}

	// end

	protected void onNew() {
		super.onAdd();
		BillData bd = getBillCardPanel().getBillData();
	}

	public boolean onSave() {

		nc.vo.ic.pub.bill.GeneralBillVO obj = null;
		nc.vo.ic.pub.bill.GeneralBillHeaderVO head = null;
		nc.vo.ic.pub.bill.GeneralBillItemVO[] items = null;
		try {

			obj = (GeneralBillVO) this.getBillCardPanel().getBillValueChangeVO("nc.vo.ic.pub.bill.GeneralBillVO", "nc.vo.ic.pub.bill.GeneralBillHeaderVO", "nc.vo.ic.pub.bill.GeneralBillItemVO");
			if (obj != null) {
				items = (GeneralBillItemVO[]) obj.getChildrenVO();
				head = (GeneralBillHeaderVO) obj.getParentVO();
				if (head != null) {
					System.out.println("change表头：" + head.getCinventoryid());
				}
				if (items != null) {
					for (int i = 0; i < items.length; i++) {
						System.out.println("change子表：" + items[i].getCinventoryid());
					}
				}
			}

			obj = (GeneralBillVO) this.getBillCardPanel().getBillValueVO("nc.vo.ic.pub.bill.GeneralBillVO", "nc.vo.ic.pub.bill.GeneralBillHeaderVO", "nc.vo.ic.pub.bill.GeneralBillItemVO");
			if (obj != null) {
				items = (GeneralBillItemVO[]) obj.getChildrenVO();
				head = (GeneralBillHeaderVO) obj.getParentVO();
				if (head != null) {
					System.out.println("old表头：" + head.getCinventoryid());
				}
				if (items != null) {
					for (int i = 0; i < items.length; i++) {
						System.out.println("old子表：" + items[i].getCinventoryid());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// (GeneralBillVO)this.m_alListData.get(this.m_voBill)
		if (super.onSave()) {
			setBillVO((GeneralBillVO) this.m_alListData.get(this.m_iLastSelListHeadRow));
			return true;
		}
		this.m_voBill.getModifiedVO();
		return false;
	}

	public void onSNAssign() {
		qryLocSN(this.m_iLastSelListHeadRow, 3);

		int iCurSelBodyLine = -1;
		if (5 == this.m_iCurPanel) {
			iCurSelBodyLine = getBillCardPanel().getBillTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000146"));
				return;
			}
		} else {
			iCurSelBodyLine = getBillListPanel().getBodyTable().getSelectedRow();
			if (iCurSelBodyLine < 0) {
				showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000147"));
				return;
			}
		}
		InvVO voInv = null;

		WhVO voWh = null;

		if (5 == this.m_iCurPanel) {
			if ((this.m_voBill == null) || (this.m_voBill.getItemCount() <= 0)) {
				SCMEnv.out("bill null E.");
				return;
			}

			GeneralBillItemVO voItem = getBodyVO(iCurSelBodyLine);

			GeneralBillItemVO voItemPty = this.m_voBill.getItemVOs()[iCurSelBodyLine];

			if (voItemPty != null) {
				voItemPty.setIDItems(voItem);
			}
			if (voItem != null) {
				voInv = voItemPty.getInv();
			}
			if (this.m_voBill != null) {
				voWh = this.m_voBill.getWh();
			}

		} else if ((this.m_iLastSelListHeadRow >= 0) && (this.m_iLastSelListHeadRow < this.m_alListData.size())) {
			GeneralBillVO bvo = (GeneralBillVO) this.m_alListData.get(this.m_iLastSelListHeadRow);
			if (bvo == null) {
				SCMEnv.out("bill null E.");
				return;
			}
			voInv = bvo.getItemInv(iCurSelBodyLine);

			voWh = bvo.getWh();
		}

		if (voInv == null) return;
		if ((5 == this.m_iCurPanel) && (((2 == this.m_iMode) || (0 == this.m_iMode)))) {
			Object oQty = voInv.getAttributeValue(this.m_sNumItemKey);
			if ((oQty == null) || (oQty.toString().trim().length() == 0)) {
				showHintMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000148"));
				return;
			}

		}

		LocatorVO voOutLoc = null;
		if ((this.m_alLocatorData != null) && (iCurSelBodyLine < this.m_alLocatorData.size()) && (this.m_alLocatorData.get(iCurSelBodyLine) != null)) {
			voOutLoc = ((LocatorVO[]) (LocatorVO[]) this.m_alLocatorData.get(iCurSelBodyLine))[0];
		}

		if (voOutLoc == null) voOutLoc = new LocatorVO();
		getSerialAllocationDlg().setOutSNByLimitLoc(true);
		getSerialAllocationDlg().setDataVO(-1, voWh, voOutLoc, voInv, this.m_iMode, (SerialVO[]) (SerialVO[]) this.m_alSerialData.get(iCurSelBodyLine), new LocatorVO[] {
			voOutLoc
		});

		int result = getSerialAllocationDlg().showModal();
		if ((1 != result) || ((0 != this.m_iMode) && (2 != this.m_iMode))) { return; }

		ArrayList alRes = getSerialAllocationDlg().getDataSpaceVO();
		if ((alRes != null) && (alRes.size() > 0)) {
			setSpaceData(iCurSelBodyLine, alRes);
		} else {
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000149"));
			return;
		}

		SerialVO[] voaSN = getSerialAllocationDlg().getDataSNVO();

		this.m_alSerialData.set(iCurSelBodyLine, voaSN);
		if (voaSN != null) for (int i = 0; i < voaSN.length; ++i)
			SCMEnv.out("ret sn[" + i + "] is" + voaSN[i].getVserialcode());
	}

	private void qryLocSN(GeneralBillVO voMyBill, int iMode) {
		try {
			if ((voMyBill == null) || (voMyBill.getPrimaryKey() == null)) {
				int iFaceRowCount = getBillCardPanel().getRowCount();

				if (this.m_alLocatorData == null) {
					this.m_alLocatorData = new ArrayList();
					for (int i = 0; i < iFaceRowCount; ++i)
						this.m_alLocatorData.add(null);
				}
				if (this.m_alSerialData == null) {
					this.m_alSerialData = new ArrayList();
					for (int i = 0; i < iFaceRowCount; ++i)
						this.m_alSerialData.add(null);
				}
				SCMEnv.out("null bill,init loc ,sn");
				return;
			}

			if (this.m_iMode == 3) {
				int i = 0;
				int iRowCount = voMyBill.getItemCount();

				boolean hasLoc = true;
				WhVO voWh = voMyBill.getWh();
				if ((voWh != null) && (voWh.getIsLocatorMgt() != null) && (voWh.getIsLocatorMgt().intValue() != 0)) {
					for (i = 0; i < iRowCount; ++i) {
						if (voMyBill.getItemValue(i, "locator") == null) {
							hasLoc = false;
							break;
						}
					}
					if (hasLoc) {
						try {
							VOCheck.checkSpaceInput(voMyBill, new Integer(this.m_iBillInOutFlag));
						} catch (Exception e) {
							SCMEnv.error(e);
							hasLoc = false;
						}

					}

				}

				InvVO voInv = null;
				boolean hasSN = true;
				String[] sKeys = new String[iRowCount];
				GeneralBillItemVO item = null;
				for (i = 0; i < iRowCount; ++i) {
					item = voMyBill.getItemVOs()[i];

					sKeys[i] = (item.getCgeneralbid() + item.getCspaceid());
					voInv = voMyBill.getItemInv(i);

					if ((voInv == null) || (voInv.getIsSerialMgt() == null) || (voInv.getIsSerialMgt().intValue() == 0) || (voMyBill.getItemValue(i, "serial") != null)) {
						continue;
					}
					hasSN = false;
					break;
				}

				if (hasLoc) this.m_alLocatorData = voMyBill.getLocators();
				if (hasSN) this.m_alSerialData = voMyBill.getSNs();
				if ((hasLoc) && (hasSN)) { return; }

				if (this.m_alLocatorData == null) {
					this.m_alLocatorData = new ArrayList();
					for (i = 0; i < iRowCount; ++i)
						this.m_alLocatorData.add(null);
				}
				if (this.m_alSerialData == null) {
					this.m_alSerialData = new ArrayList();
					for (i = 0; i < iRowCount; ++i) {
						this.m_alSerialData.add(null);
					}
				}
				for (i = 0; (i < iRowCount) && (((voMyBill.getItemValue(i, "ninnum") == null) || (voMyBill.getItemValue(i, "ninnum").toString().length() <= 0))); ++i) {
					if ((voMyBill.getItemValue(i, "noutnum") != null) && (voMyBill.getItemValue(i, "noutnum").toString().length() > 0)) {
						break;
					}

				}

				if (i >= iRowCount) { return; }

				Integer iSearchMode = null;

				if ((!hasLoc) && (((iMode == 3) || (iMode == 23)))) {
					iSearchMode = new Integer(iMode);
				}

				if ((!hasSN) && (((iMode == 3) || (iMode == 4)))) {
					iSearchMode = new Integer(iMode);
				}
				if (iSearchMode == null) { return; }

				ArrayList alAllData = (ArrayList) GeneralBillHelper.queryInfo(iSearchMode, voMyBill.getPrimaryKey());

				ArrayList alTempLocatorData = null;
				ArrayList alTempSerialData = null;

				if (iMode == 3) if ((alAllData != null) && (alAllData.size() >= 2)) {
					alTempLocatorData = (ArrayList) alAllData.get(0);
					alTempSerialData = (ArrayList) alAllData.get(1);
				} else if (iMode == 4) {
					if ((alAllData != null) && (alAllData.size() >= 1)) alTempSerialData = (ArrayList) alAllData.get(0);
				} else if ((iMode == 23) && (alAllData != null) && (alAllData.size() >= 1)) {
					alTempLocatorData = (ArrayList) alAllData.get(0);
				}

				if (alTempLocatorData != null) {
					voMyBill.setLocators(alTempLocatorData);

					this.m_alLocatorData = voMyBill.getLocators();
				}

				if (alTempSerialData != null) {
					alTempSerialData = dropInSpaceSN(alTempSerialData, sKeys);
					voMyBill.setSNs(alTempSerialData);

					this.m_alSerialData = voMyBill.getSNs();
				}
			}

		} catch (Exception e) {
			SCMEnv.error(e);
		}
	}

	private ArrayList dropInSpaceSN(ArrayList alData, String[] sKeys) {
		if ((sKeys == null) || (alData == null) || (alData.size() == 0) || (sKeys.length == 0)) { return alData; }
		ArrayList al = new ArrayList();
		SerialVO[] voaSN = null;
		for (int j = 0; j < alData.size(); ++j) {
			Object oSNVOs = alData.get(j);
			Vector vSN = new Vector();
			if (oSNVOs != null) {
				voaSN = (SerialVO[]) (SerialVO[]) oSNVOs;
				if ((voaSN != null) && (voaSN.length > 0) && (voaSN[0] != null) && (voaSN[0].getCgeneralbid() != null) && (voaSN[0].getCspaceid() != null)) {
					for (int k = 0; k < voaSN.length; ++k) {
						String sKey = voaSN[k].getCgeneralbid() + voaSN[k].getCspaceid();
						for (int i = 0; i < sKeys.length; ++i) {
							if (sKey.equals(sKeys[i])) {
								vSN.add(voaSN[k]);
							}
						}
					}
				}
			}

			if ((vSN != null) && (vSN.size() > 0)) {
				SerialVO[] sn = new SerialVO[vSN.size()];
				vSN.copyInto(sn);
				al.add(sn);
			}
		}

		return al;
	}

	protected void qryLocSN(int iBillNum, int iMode) {
		GeneralBillVO voMyBill = null;

		if ((this.m_alListData != null) && (this.m_alListData.size() > iBillNum) && (iBillNum >= 0)) voMyBill = (GeneralBillVO) this.m_alListData.get(iBillNum);
		qryLocSN(voMyBill, iMode);
	}

	protected void selectBillOnListPanel(int iBillIndex) {
		if (this.m_alListData == null) return;
		GeneralBillVO voBill = (GeneralBillVO) this.m_alListData.get(iBillIndex);
		GeneralBillItemVO[] voItems = (GeneralBillItemVO[]) (GeneralBillItemVO[]) voBill.getChildrenVO();
		if (voItems != null) {
			SCMEnv.out("body rows=" + voItems.length);
			for (int i = 0; i < voItems.length; ++i) {
				LocatorVO[] lvos = (LocatorVO[]) voItems[i].getLocator();
				if (lvos == null) {
					qryLocSN(iBillIndex, 3);
					lvos = (LocatorVO[]) voItems[i].getLocator();
				}
				if ((lvos != null) && (lvos.length > 0)) {
					voItems[i].setCspaceid(lvos[0].getCspaceid());
					voItems[i].setVspacename(lvos[0].getVspacename());
				}

			}

			setListBodyData(voItems);
		}

		if (getBillListPanel().getBodyTable().getRowCount() > 0) getBillListPanel().getBodyTable().setRowSelectionInterval(0, 0);
	}

	protected void setButtonsStatus(int iBillMode) {
		if (iBillMode == 3) {
			if (this.m_iBillQty > 0) getButtonTree().getButton("删除").setEnabled(true);
		} else getButtonTree().getButton("删除").setEnabled(false);
	}

	protected void setSpaceData(int iCurSelBodyLine, ArrayList alRes) {
		if ((alRes == null) || (alRes.size() <= 0)) {
			SCMEnv.out("space is not ready!");
			return;
		}

		LocatorVO voLoc0 = null;
		LocatorVO voLoci = null;
		voLoc0 = (LocatorVO) alRes.get(0);
		for (int i = 1; i < alRes.size(); ++i) {
			voLoci = (LocatorVO) alRes.get(i);

			if ((voLoc0.getCspaceid() == null) || (!voLoc0.getCspaceid().equals(voLoci.getCspaceid()))) continue;
			showErrorMessage(NCLangRes.getInstance().getStrByID("4008spec", "UPP4008spec-000150"));
			return;
		}

		String cInsname = getLocatorRefPane2().getRefName();
		String cInspaceid = getLocatorRefPane2().getRefPK();

		this.m_voBill.setItemValue(iCurSelBodyLine, "cspaceid", voLoc0.getCspaceid());
		this.m_voBill.setItemValue(iCurSelBodyLine, "vspacename", voLoc0.getVspacename());

		getLocatorRefPane().setPK(voLoc0.getCspaceid());

		getBillCardPanel().setBodyValueAt(voLoc0.getCspaceid(), iCurSelBodyLine, "cspaceid");

		getBillCardPanel().setBodyValueAt(voLoc0.getVspacename(), iCurSelBodyLine, "vspacename");

		LocatorVO[] voaCurLoc = null;
		if ((this.m_alLocatorData != null) && (iCurSelBodyLine < this.m_alLocatorData.size())) {
			if ((this.m_alLocatorData.get(iCurSelBodyLine) != null) && (((LocatorVO[]) (LocatorVO[]) this.m_alLocatorData.get(iCurSelBodyLine)).length >= 2)) {
				voaCurLoc = (LocatorVO[]) (LocatorVO[]) this.m_alLocatorData.get(iCurSelBodyLine);
			} else voaCurLoc = new LocatorVO[2];

			UFDouble dTempNum = null;
			UFDouble dTempAstNum = null;
			UFDouble dTempGrossNum = null;
			Object oTempValue = getBillCardPanel().getBodyValueAt(iCurSelBodyLine, "noutnum");
			if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) dTempNum = new UFDouble(oTempValue.toString());
			oTempValue = getBillCardPanel().getBodyValueAt(iCurSelBodyLine, "noutassistnum");
			if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) dTempAstNum = new UFDouble(oTempValue.toString());
			oTempValue = getBillCardPanel().getBodyValueAt(iCurSelBodyLine, "noutgrossnum");
			if ((oTempValue != null) && (oTempValue.toString().trim().length() > 0)) {
				dTempGrossNum = new UFDouble(oTempValue.toString());
			}

			voaCurLoc[0] = voLoc0;

			voaCurLoc[0].setNoutgrossnum(dTempGrossNum);

			if (voaCurLoc[1] != null) {
				voaCurLoc[1].setNinspacenum(dTempNum);
				voaCurLoc[1].setNinspaceassistnum(dTempAstNum);
				voaCurLoc[1].setNingrossnum(dTempGrossNum);
			} else if ((cInspaceid != null) && (cInspaceid.trim().length() > 0)) {
				voaCurLoc[1] = new LocatorVO();
				voaCurLoc[1].setCspaceid(cInspaceid);
				voaCurLoc[1].setVspacename(cInsname);
				voaCurLoc[1].setNinspacenum(dTempNum);
				voaCurLoc[1].setNinspaceassistnum(dTempAstNum);
				voaCurLoc[1].setNingrossnum(dTempGrossNum);
			}

			this.m_alLocatorData.set(iCurSelBodyLine, voaCurLoc);
		}
	}

	public String getProLine() {
		return m_strProLine;
	}

	public void setProLine(String m_strProLine) {
		this.m_strProLine = m_strProLine;
	}

	private void getBomListInsertBillItem(String sql, String wlbmid, String invclasscode, String invclassname, String invclasslev, String jhwgsl, String wlbmname) throws BusinessException {

		/******************************* */
		IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List list = (List) sessionManager.executeQuery(sql, new ArrayListProcessor());

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

				getBillCardPanel().getBodyPanel().getTableModel().addLine();
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(values.get(1), curRow, "cinventorycode");// 存货编码
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(values.get(0), curRow, "cinventoryid");// 存货ID
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(values.get(2), curRow, "invname");// 存货编码

				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(rowNum, curRow, "crowno");
				BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem("cinventorycode").getComponent(), values.get(1), "cinventorycode", curRow);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setPK(values.get(0));
				afterEdit(e);
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(noutnum, curRow, "noutnum");//
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()), curRow, "dbizdate");
				rowNo++;
			} else if (rowNo >= curRow) {

				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(values.get(1), curRow, "cinventorycode");// 存货编码
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(values.get(0), curRow, "cinventoryid");// 存货ID
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(values.get(2), curRow, "invname");// 存货编码
				BillEditEvent e = new BillEditEvent(getBillCardPanel().getBodyItem("cinventorycode").getComponent(), values.get(1), "cinventorycode", curRow);
				((UIRefPane) getBillCardPanel().getBodyItem("cinventorycode").getComponent()).setPK(values.get(0));
				afterEdit(e);
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(noutnum, curRow, "noutnum");//
				getBillCardPanel().getBodyPanel().getTableModel().setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()), curRow, "dbizdate");
			}
			curRow++;
		}
		if (curRow < rowNo) {
			for (; curRow < rowNo; curRow++) {
				getBillCardPanel().getBodyPanel().getTableModel().delLine(new int[] {
					curRow
				});
			}
		}
	}

	private int IfZore(int rowCount) {
		// TODO Auto-generated method stub
		return rowCount == 0 ? 1 : rowCount;
	}

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
}