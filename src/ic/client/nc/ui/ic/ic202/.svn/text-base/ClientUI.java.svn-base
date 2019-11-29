package nc.ui.ic.ic202;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.borland.dx.dataset.ColumnList;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wrback.state.IWriteBackStateService;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.ic.ic212.WkRefModel;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.vo.bd.CorpVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;

public class ClientUI extends GeneralBillClientUI {
	private static final String PKCORP = null;

	public ClientUI() {
		initialize();
	}

	public ClientUI(String pk_corp, String billType, String businessType,
			String operator, String billID) {
		super(pk_corp, billType, businessType, operator, billID);
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

				this.ivjBillCardPanel.setBillData(bd);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjBillCardPanel;
	}

	protected void afterBillEdit(BillEditEvent e) {
		if (e.getKey().equals("cwarehouseid")
				|| e.getKey().equals("cinventorycode")) {
			String cwarehouseid = ((UIRefPane) getBillCardPanel().getHeadItem(
					"cwarehouseid").getComponent()).getRefPK();
			if (getParentCorpCode().equals("10395")) {
				if (cwarehouseid == null || cwarehouseid.equals("")) {
					return;
				}
				String sCode = ((UIRefPane) getBillCardPanel().getHeadItem(
						"cwarehouseid").getComponent()).getRefCode();
				/**
				 * @功能:根据仓库、存货带出默认货位
				 * @author ：林桂莹
				 * @2012/9/5
				 * 
				 * @since v50
				 */
				if (Iscsflag(cwarehouseid))// ||sCode.equals(arg0))
				{

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
						Sql += "where a.cbilltypecode ='46' and a.cwarehouseid='"
								+ cwarehouseid
								+ "' and  b.cinventoryid='"
								+ cinventoryid + "'  ";
						Sql += " and d.pk_cargdoc is not null  and a.taccounttime is not null and nvl(b.dr,0)=0 order by a.taccounttime desc  ";
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
		}
	}

	protected void afterBillItemSelChg(int iRow, int iCol) {
	}

	public boolean beforeBillItemEdit(BillEditEvent e) {
		return true;
	}

	protected void beforeBillItemSelChg(int iRow, int iCol) {
	}

	protected boolean checkVO(GeneralBillVO voBill) {
		return checkVO();
	}

	public String getTitle() {
		return super.getTitle();
	}

	public void initialize() {
		super.initialize();
	}

	protected void initPanel() {
		super.setBillInOutFlag(1);

		super.setNeedBillRef(false);

		this.m_sBillTypeCode = BillTypeConst.m_productIn;

		this.m_sCurrentBillNode = "40080604";
	}

	void newMethod() {
	}

	// edit by yqq 2017-04-24 制盖公司导入重复后删除产成品入库后，回写生产订单的入库数量，实际完工数量
	/*
	 * public void onButtonClicked(ButtonObject bo) { super.onButtonClicked(bo);
	 * }
	 */

	public void onButtonClicked(ButtonObject bo) {
		if (bo == getButtonTree().getButton("删除"))
			try {
				onBoDelete(); // 删除时执行方法
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			super.onButtonClicked(bo);
	}

	private void onBoDelete() throws Exception {
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		if ("1108".equals(pk_corp)||"1078".equals(pk_corp)) { // 公司为1078
			try {
				// 获取VO
				GeneralBillVO make = (GeneralBillVO) getBillCardPanel()
						.getBillValueVO(GeneralBillVO.class.getName(),
								GeneralBillHeaderVO.class.getName(),
								GeneralBillItemVO.class.getName());

				int size = this.getBillCardPanel().getRowCount();
				UFDouble sum = new UFDouble(0.0);
				UFDouble sum1 = new UFDouble(0.0);

				String vbillcode = make.getHeaderValue("vbillcode") == null ? "": make.getHeaderValue("vbillcode").toString();
				StringBuffer sq = new StringBuffer();
				sq.append(" select h.vdiliveraddress")
				  .append(" from ic_general_h h  left join ic_general_b b on h.cgeneralhid = b.cgeneralhid ")
				  .append(" where h.vbillcode = '"+ vbillcode+ "' and (h.pk_corp='1108' or h.pk_corp='1078')and nvl(h.dr,0)=0 ");
				
				String vdiliveraddress = "";
				IUAPQueryBS uAPQueryBSQ = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					vdiliveraddress = (String) uAPQueryBSQ.executeQuery(sq.toString(), new ColumnProcessor());

				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				StringBuffer sq1 = new StringBuffer();
				sq1.append(" select b.cfirstbillhid")
				   .append(" from ic_general_h h  left join ic_general_b b on h.cgeneralhid = b.cgeneralhid ")
				   .append("  where h.vbillcode = '"+ vbillcode+ "' and (h.pk_corp='1108' or h.pk_corp='1078') and nvl(h.dr,0)=0 ");
				
				String cfirstbillhid = "";
				IUAPQueryBS uAPQueryBSQ1 = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				try {
					cfirstbillhid = (String) uAPQueryBSQ1.executeQuery(sq1.toString(), new ColumnProcessor());

				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (vdiliveraddress!=null&&vdiliveraddress.equals("PTS系统导入")) {//add by zwx 2019-8-15 增加非null校验
					for (int i = 0; i < size; i++) {
						UFDouble ninnum = getBillCardPanel().getBodyValueAt(i,"ninnum") == null ? new UFDouble(0.0)
								: (UFDouble) (getBillCardPanel().getBodyValueAt(i, "ninnum")); // 获取实入数量

						// 获取生产订单的状态,入库数量,实际完工数量
						StringBuffer sb1 = new StringBuffer();
						sb1.append(" select zt,rksl,sjwgsl ")
						   .append("   from mm_mo ")
						   .append("  where pk_moid = '"+ cfirstbillhid+ "' and (pk_corp='1108' or pk_corp='1078' ) and nvl(dr,0)=0 ");

						// String zt = "";
						ArrayList list = null;

						IUAPQueryBS uAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
						try {
							list = (ArrayList) uAPQueryBS.executeQuery(sb1.toString(), new MapListProcessor());

							// zt =
							// (String)uAPQueryBS.executeQuery(sb1.toString(),
							// new ColumnListProcessorProcessor());

						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Map vfree = new HashMap();

						if (list != null && list.size() > 0) {
							for (int z = 0; z < list.size(); z++) {
								Map listmap = (Map) list.get(z);
								String zt = listmap.get("zt") == null ? "": listmap.get("zt").toString();
								String rksl = listmap.get("rksl") == null ? "": listmap.get("rksl").toString();
								String sjwgsl = listmap.get("sjwgsl") == null ? "": listmap.get("sjwgsl").toString();

								UFDouble number = new UFDouble(rksl.toString());
								UFDouble number1 = new UFDouble(sjwgsl.toString());

								sum = number.sub(ninnum);
								sum1 = number1.sub(ninnum);

								StringBuffer sqq = new StringBuffer();
								sqq.append(" update mm_mo set sjwgsl=" + sum1 + " ,rksl="+ sum + ",zt='B' where pk_moid='" + cfirstbillhid+ "' and pk_corp =  '1108'");
								IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
						    	ipubdmo.executeUpdate(sqq.toString());								
							}
						}

					}			             
				}
			}
			catch (Exception e) {
				throw new BusinessException(e.getMessage());

			}
		}
		// 公司为1108删除回写合格证打印 add by zwx 2019-8-19
		if ("1108".equals(pk_corp)) { 
			
			if(super.m_iCurPanel == BillMode.List){
				GeneralBillVO vo = (GeneralBillVO) super.m_alListData.get(getBillListPanel().getHeadTable().getSelectedRow());
				GeneralBillItemVO[] bvos = (GeneralBillItemVO[]) vo.getChildrenVO();
				Object ph = bvos[0].getVbatchcode();
				if(ph!=null){
					IWriteBackStateService writeback=(IWriteBackStateService)NCLocator.getInstance().lookup(IWriteBackStateService.class.getName());
					writeback.delWriteBackRK(ph.toString(), pk_corp);
				}
			}else{
				Object ph = getBillCardPanel().getBodyValueAt(0, "vbatchcode");
				if(ph!=null){
					IWriteBackStateService writeback=(IWriteBackStateService)NCLocator.getInstance().lookup(IWriteBackStateService.class.getName());
					writeback.delWriteBackRK(ph.toString(), pk_corp);
				}
			}	
			
		}
		super.onDelete();
	}

	// end by yqq

	protected void selectBillOnListPanel(int iBillIndex) {
	}

	protected void setButtonsStatus(int iBillMode) {
	}

	/**
	 * @功能:设置货位
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
}