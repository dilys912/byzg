package nc.ui.so.so001.order;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jxl.write.WritableWorkbook;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.so.service.Isoorderservice;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

public class SaleOrderAdminUI extends SaleBillUI {
	private boolean b_init;

	public SaleOrderAdminUI() {
	}

	public SaleOrderAdminUI(String pk_corp, String billtype, String busitype,
			String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
	}

	public String getBillButtonAction(ButtonObject bo) {
		return null;
	}

	public String getBillButtonState() {
		return null;
	}

	public ButtonObject[] getBillButtons() {
		if (!this.b_init) {
			initBtnGrp();
			this.b_init = true;
		}

		if (this.strShowState.equals("列表"))
			return this.aryListButtonGroup;
		if (this.strShowState.equals("卡片")) {
			return this.aryButtonGroup;
		}
		return this.aryBatchButtonGroup;
	}

	private void initBtnGrp() {
		ButtonObject[] aryListButtonGroup = { this.boBusiType, this.boAdd,
				this.boSave, this.boMaintain, this.boLine, this.boAudit,
				this.boAction, this.boQuery, this.boBrowse, this.boCard,
				this.boPrntMgr, this.boAssistant, this.boAsstntQry,
				this.boImport };// 'this.boImport' add by zwx 2014-9-1

		ButtonObject[] aryButtonGroup = { this.boBusiType, this.boAdd,
				this.boSave, this.boMaintain, this.boLine, this.boAudit,
				this.boAction, this.boQuery, this.boBrowse, this.boReturn,
				this.boPrntMgr, this.boAssistant };

		ButtonObject[] aryBatchButtonGroup = { this.boBatch, this.boLine,
				this.boDocument, this.boBack };

		ButtonObject[] bomButtonGroup = { this.boBomSave, this.boBomEdit,
				this.boBomCancel, this.boBomReturn, this.boOrderQuery,
				this.boBomPrint };

		IFuncExtend funcExtend = getFuncExtend();
		if (funcExtend != null) {
			ButtonObject[] boExtend = this.m_funcExtend.getExtendButton();
			if ((boExtend != null) && (boExtend.length > 0)) {
				ButtonObject[] botempcard = new ButtonObject[aryButtonGroup.length
						+ boExtend.length];

				System.arraycopy(aryButtonGroup, 0, botempcard, 0,
						aryButtonGroup.length);

				System.arraycopy(boExtend, 0, botempcard,
						aryButtonGroup.length, boExtend.length);

				aryButtonGroup = botempcard;

				ButtonObject[] botemplist = new ButtonObject[aryListButtonGroup.length
						+ boExtend.length];

				System.arraycopy(aryListButtonGroup, 0, botemplist, 0,
						aryListButtonGroup.length);

				System.arraycopy(boExtend, 0, botemplist,
						aryListButtonGroup.length, boExtend.length);

				aryListButtonGroup = botemplist;
			}
		}

		ButtonObject[] boExtend = getExtendBtns();
		if ((boExtend != null) && (boExtend.length > 0)) {
			ButtonObject[] botempcard = new ButtonObject[aryButtonGroup.length
					+ boExtend.length];

			System.arraycopy(aryButtonGroup, 0, botempcard, 0,
					aryButtonGroup.length);

			System.arraycopy(boExtend, 0, botempcard, aryButtonGroup.length,
					boExtend.length);

			aryButtonGroup = botempcard;

			ButtonObject[] botemplist = new ButtonObject[aryListButtonGroup.length
					+ boExtend.length];

			System.arraycopy(aryListButtonGroup, 0, botemplist, 0,
					aryListButtonGroup.length);

			System.arraycopy(boExtend, 0, botemplist,
					aryListButtonGroup.length, boExtend.length);

			aryListButtonGroup = botemplist;
		}

		this.aryBatchButtonGroup = aryBatchButtonGroup;
		this.aryButtonGroup = aryButtonGroup;
		this.aryListButtonGroup = aryListButtonGroup;
		this.bomButtonGroup = bomButtonGroup;
	}

	public String getBillID() {
		if ("退货".equals(this.strState)) {
			return this.id;
		}

		if (this.strShowState == "列表") {
			return (String) getBillListPanel().getHeadBillModel().getValueAt(
					this.num, "csaleid");
		}
		if (this.strShowState == "卡片") {
			return (String) getBillCardPanel().getHeadItem("csaleid")
					.getValueObject();
		}

		return null;
	}

	public String getNodeCode() {
		return "40060301";
	}

	public String getTitle() {
		return getBillListPanel().getBillListData().getTitle();
	}

	protected void initButtons() {
		PfUtilClient.retBusinessBtn(this.boBusiType, getCorpPrimaryKey(), "30");

		if ((this.boBusiType.getChildButtonGroup() != null)
				&& (this.boBusiType.getChildButtonGroup().length > 0)) {
			this.boBusiType.setTag(this.boBusiType.getChildButtonGroup()[0]
					.getTag());
			this.boBusiType.getChildButtonGroup()[0].setSelected(true);
			this.boBusiType.setCheckboxGroup(true);
		}

		// 维护
		this.boMaintain.removeAllChildren();
		this.boMaintain.addChildButton(this.boEdit);
		this.boMaintain.addChildButton(this.boCancel);
		this.boMaintain.addChildButton(this.boBlankOut);
		this.boMaintain.addChildButton(this.boCopyBill);

		this.boLine.removeAllChildren();
		this.boLine.addChildButton(this.boAddLine);
		this.boLine.addChildButton(this.boDelLine);
		this.boLine.addChildButton(this.boInsertLine);
		this.boLine.addChildButton(this.boCopyLine);
		this.boLine.addChildButton(this.boPasteLine);

		this.boBrowse.removeAllChildren();
		this.boBrowse.addChildButton(this.boRefresh);
		this.boBrowse.addChildButton(this.boFind);
		this.boBrowse.addChildButton(this.boFirst);
		this.boBrowse.addChildButton(this.boPre);
		this.boBrowse.addChildButton(this.boNext);
		this.boBrowse.addChildButton(this.boLast);
		this.boBrowse.addChildButton(this.boListSelectAll);
		this.boBrowse.addChildButton(this.boListDeselectAll);

		retElseBtn("Order002", "Order001");
		retElseBtn("Order002", "Order003");

		this.boAsstntQry.removeAllChildren();
		this.boAsstntQry.addChildButton(this.boOrderQuery);
		this.boAsstntQry.addChildButton(this.boOnHandShowHidden);
		this.boAsstntQry.addChildButton(this.boAuditFlowStatus);
		this.boAsstntQry.addChildButton(this.boCustCredit);
		this.boAsstntQry.addChildButton(this.boOrderExecRpt);
		this.boAsstntQry.addChildButton(this.boCustInfo);
		this.boAsstntQry.addChildButton(this.boPrifit);

		this.boPrntMgr.removeAllChildren();
		this.boPrntMgr.addChildButton(this.boPreview);
		this.boPrntMgr.addChildButton(this.boPrint);

		if (this.strShowState.equals("列表")) {
			setButtons(getBillButtons());
		} else if (this.strShowState.equals("卡片")) {
			if (this.strState.equals("BOM"))
				setButtons(this.bomButtonGroup);
			else
				setButtons(getBillButtons());
		}
	}

	protected void onNewByOther(AggregatedValueObject[] saleVOs) {
		super.onNewByOther(saleVOs);
		try {
			if (saleVOs != null && saleVOs.length > 0) {
				nc.vo.so.so001.SaleorderBVO body = (nc.vo.so.so001.SaleorderBVO) saleVOs[0]
						.getChildrenVO()[0];
				String bodyid = body.getCsourcebillid();
				String value = (String) HYPubBO_Client.findColValue(
						"ct_manage", "def8", " pk_ct_manage='" + bodyid + "'");
				if (value != null && value.trim().equals("送货")) {
					BillItem item = this.getBillCardPanel().getHeadItem(
							"bdeliver");
					if (item != null) {
						item.setValue(new UFBoolean(true));
					}
				}
			}
		} catch (Exception ex) {

		}
	}

	@SuppressWarnings( { "restriction" })
	public void onButtonClicked(ButtonObject bo) {
		// 接口测试 add by wbp 2017-11-29 
//		String json = "{\"user\":\"nic\",\"password\":\"123456\",\"corp\":\"1016\",\"zdrq\":\"测试合同\",\"ksid\":\"0001A210000000003P1O\", \"lb\":\"2017-11-03\",\"coperatorid\":\"2017-11-04\", \"memo\":\"2017-11-25\",\"bodylist\":[{\"cinventoryid\":\"0001A21000000004ETII\",\"cksl\":\"30\",\"cbodywarehouseid\":\"30\",\"vfree0\":\"30\"}]}";
//		Isoorderservice so =new SoOrderServiceImpl();
//		so.sendSoBill(json);
		// 接口测试 end by wbp 2017-11-29 
		// add by zwx 2014-8-20 校验 “单品折扣”存在折扣时，报价单位含税单价与报价含税净价不能相同。  eidt 2014-9-23
		if (bo.getCode().equals("保存")) {
			int row = getBillCardPanel().getBillModel().getRowCount();
			if (row > 0) {
				StringBuffer strb = new StringBuffer(
						"报价单位含税单价*(单价折扣/100)不等于报价单位含税净价，是否修改如下行号[报价单位含税净价][含税净价]:" + '\n');
				StringBuffer strb1 = new StringBuffer(
						"报价单位含税单价*(单价折扣/100)不等于报价单位含税净价，是否修改如下行号[报价单位含税净价][含税净价]:" + '\n');
				boolean flag = false;
				boolean flag1 = false;
				TreeMap<String, String> map = new  TreeMap<String, String>();
				TreeMap<String, String> map1 = new TreeMap<String, String>();
				for (int i = 0; i < row; i++) {
					String num = (i + 1) + "";
					// norgqttaxprc 报价单位含税单价
					UFDouble norgqttaxprc = getBillCardPanel().getBodyValueAt(
							i, "norgqttaxprc") == null ? new UFDouble(0)
							: new UFDouble(getBillCardPanel().getBodyValueAt(i,"norgqttaxprc").toString());
					// nitemdiscountrate 单价折扣  
					UFDouble nitemdiscountrate = getBillCardPanel()
							.getBodyValueAt(i, "nitemdiscountrate") == null ? new UFDouble(
							0)
							: new UFDouble(getBillCardPanel().getBodyValueAt(i,"nitemdiscountrate").toString());
					// norgqttaxnetprc 报价单位含税净价 
					UFDouble norgqttaxnetprc = getBillCardPanel()
							.getBodyValueAt(i, "norgqttaxnetprc") == null ? new UFDouble(
							0)
							: new UFDouble(getBillCardPanel().getBodyValueAt(i,"norgqttaxnetprc").toString());
							map1.put(num, norgqttaxnetprc.toString());
					if (nitemdiscountrate.equals(new UFDouble(100))) {
						if (norgqttaxprc.doubleValue() != norgqttaxnetprc
								.doubleValue()) {
							flag1 = true;
							map.put(num, norgqttaxprc.toString());
						}
					} else {
						// 报价单位含税净价(hsjj)=报价单位含税单价*（单价折扣/100） 
						UFDouble hsjj = norgqttaxprc.multiply(nitemdiscountrate.div(100));
						if (hsjj.doubleValue() != norgqttaxnetprc.doubleValue()) {
							flag = true;
							map.put(num, hsjj.toString());
						}
					}
				}

				StringBuffer info = new StringBuffer();
				Iterator iterator = map.keySet().iterator();
				while (iterator.hasNext()) {
					Object key = iterator.next();
					info.append("第[" + key + "]行："+map1.get(key)+"修改为：" + map.get(key) + '\n');
				}
				if (flag) {
					int flag2 = MessageDialog.showYesNoDlg(this, "错误Error",
							strb.append(info).toString());
					if (flag2 == UIDialog.ID_YES) {
						Iterator iter = map.keySet().iterator();
						while (iter.hasNext()) {
							Object key1 = iter.next();
							getBillCardPanel().setBodyValueAt(map.get(key1),Integer.parseInt(key1.toString()) - 1,"norgqttaxnetprc");
 							getBillCardPanel().setBodyValueAt(map.get(key1),Integer.parseInt(key1.toString()) - 1,"noriginalcurtaxnetprice");//edit 2014-11-13
						}
					}
					return;
				}
				if (flag1) {
					int flag2 = MessageDialog.showYesNoDlg(this, "错误Error",
							strb1.append(info).toString());
					if (flag2 == UIDialog.ID_YES) {
						Iterator iter = map.keySet().iterator();
						while (iter.hasNext()) {
							Object key1 = iter.next();
							getBillCardPanel().setBodyValueAt(map.get(key1),Integer.parseInt(key1.toString()) - 1,"norgqttaxnetprc");
 							getBillCardPanel().setBodyValueAt(map.get(key1),Integer.parseInt(key1.toString()) - 1,"noriginalcurtaxnetprice");//edit 2014-11-13
						}
					}
					return;
				}
			}
			// 功能:根据是否送货来判断，如果是则可以选择输入发货地点和到货地点.否则不可编辑。校验所选发货地点和到货地点是否有维护，没有则提示。wkf
						//范围:宝钢制盖--start
						
						String pk_corp = getCorpPrimaryKey();
						if(pk_corp.equals("1078")||pk_corp.equals("1100")){
							String isfh = getBillCardPanel().getHeadItem("vdef16").getValue().toString();
							if(isfh.equals("是")){
								String isfhdd = getBillCardPanel().getHeadItem("pk_defdoc17").getValue().toString();//发货地点Pk
								String isshdd = getBillCardPanel().getHeadItem("pk_defdoc19").getValue().toString();//到货地Pk
								String fhdd = getBillCardPanel().getHeadItem("vdef17").getValue().toString();//发货地点
								String dhdd = getBillCardPanel().getHeadItem("vdef19").getValue().toString();//到货地
								
								if(fhdd.equals("")||fhdd.equals(null)){
									showErrorMessage("当前选择为送货，请输入发货地点和到货地点!!");
									return;
								}
								
								boolean isok = CheckYF(isfhdd,isshdd);
								if(isok){
									showErrorMessage("\""+fhdd+"\"至\""+dhdd+"\"没有维护运费价格表!");
									return;
								}
							}
//							else{
//								String iszd = getBillCardPanel().getHeadItem("vdef17").getValue().toString().trim();
//								if(!iszd.equals("")||!iszd.equals(null)){
//									showErrorMessage("当前选择为自提，不用输入发货地点和送货地点!");
//									return;
//								}
//							}
							//wkf end
						}
			

		}
		// end zwx 2014-8-20 校验 “单品折扣”存在折扣时，报价单位含税单价与报价含税净价不能相同。   eidt 2014-9-23
		super.onButtonClicked(bo);

		if (bo.getParent() == this.boAdd && getSouceBillType().equals("Z4")) // 如果是来源于销售合同
		{
			for (int i = 0; i < getBillCardPanel().getRowCount(); ++i) {
				try {
					String csourceid = (String) this.getBillCardTools()
							.getBodyValue(i, "csourcebillbodyid");
					String csourcehid = (String) this.getBillCardTools()
							.getBodyValue(i, "csourcebillid");

					// 如果存在销售合同
					if (csourceid != null && !csourceid.trim().equals("")) {
						Object dpzk = (String) HYPubBO_Client.findColValue(
								"ct_manage_b", "to_char(dpzk,'990.999999')",
								" pk_ct_manage_b ='" + csourceid + "'");
						Object tsbl = (String) HYPubBO_Client.findColValue(
								"ct_manage_b", "to_char(tsbl,'990.999999')",
								" pk_ct_manage_b ='" + csourceid + "'");
						if (tsbl != null && !tsbl.toString().trim().equals("")) {
							getBillCardPanel().setBodyValueAt(tsbl, i, "tsbl");
						} else {
							getBillCardPanel().setBodyValueAt(0.0D, i, "tsbl");
						}
						if (dpzk != null && !dpzk.toString().trim().equals("")) {
							getBillCardPanel().setBodyValueAt(dpzk, i,"nitemdiscountrate");
						}

						String[] sFormula = {
								"noriginalcurdiscountmny->(100-nitemdiscountrate)/100*(noriginalcurtaxprice*nnumber)",
								"noriginalcursummny->(noriginalcurtaxprice*nnumber)-noriginalcurdiscountmny" };

						getBillCardPanel().execBodyFormulas(i, sFormula);

					}

				} catch (Exception ex) {
					Logger.error(ex);
				}

			}
		}
		// add by zwx 2014-9-1
		if (bo == this.boImport) {
			onQTImport();
		}
		// end by zwx
	}
	
	//检查发货地点到到货地点是否有维护运费
  	public boolean CheckYF(String start,String end){
  		boolean isyfok = false;
  		String sql = new StringBuilder()
  		//pkdelivorg发运组织
  		//pk_sendtype发运方式
        .append("select pk_basicprice from dm_baseprice")
        .append(" where nvl(dr,0)=0")
        .append(" and pkfromaddress ='"+start+"'")
        .append(" and pktoaddress='"+end+"'")
      //  .append(" and pkdelivorg='"+kczz+"'")
      //  .append(" and pk_sendtype='"+fyfs+"'")
        .append(" and nvl(dr,0)=0").toString();
  		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		MapListProcessor alp = new MapListProcessor();
		ArrayList pk_basicprice = null;
		try {
			pk_basicprice = (ArrayList) query.executeQuery(sql, alp);
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
        if(pk_basicprice.size()<=0){
        	isyfok = true;
        }else{
        	isyfok = false;
        }
  		return isyfok;
  	}
	// add by zwx 2014-9-1
	public static WritableWorkbook ww = null;
	private int res;
	private File txtFile = null;
	private nc.ui.pub.beans.UITextField txtfFileUrl = null;// 文本框,用于显示文件路径

	private void onQTImport() {
		QTDaoru();

	}

	// add by zwx 2014-9-1
	@SuppressWarnings("static-access")
	private void QTDaoru() {
	    UIFileChooser fileChooser = new UIFileChooser();
	    fileChooser.setAcceptAllFileFilterUsed(true);
	    this.res = fileChooser.showOpenDialog(this);
		if (res == 0) {
			getTFLocalFile().setText(
					fileChooser.getSelectedFile().getAbsolutePath());
			txtFile = fileChooser.getSelectedFile();
			String filepath = txtFile.getAbsolutePath();
			final WriteToExcel exceldata = new WriteToExcel();
			exceldata.creatFile(filepath);
			Runnable checkRun = new Runnable() {
				@SuppressWarnings( { "unchecked" })
				public void run() {
					BannerDialog dialog = new BannerDialog(getParent());
					dialog.start();
					try {
						exceldata.readData(0);
						SaleOrderVO[] vos = WriteToExcel.ordervo;
						HashMap<String, SaleOrderVO> map = new HashMap<String, SaleOrderVO>(); // 相同单据号

						StringBuffer info = new StringBuffer("");
						for (int i = 0; i < vos.length; i++) {
							SaleorderHVO hvo = vos[i].getHeadVO();
							String err = hvo.getVdef20() == null ? "" : hvo
									.getVdef20().toString();
							if (err.trim().length() > 0) {
								info
										.append("第(" + (i + 5) + ")行字段：" + err
												+ "");
							}
						}
						if (!info.toString().trim().equals("")) {
							showErrorMessage(info.toString()
									+ "请核实上述部分字段是否存在或分配至当前公司!");
							showHintMessage("订单导入失败！");
						} else {
							for (int i = 0; i < vos.length; i++) {
								SaleOrderVO voi = vos[i];
								String code = voi.getHeadVO().getVreceiptcode();

								if (!map.containsKey(code)) {
									map.put(code, voi);
								} else {
									SaleorderBVO[] bvo = (SaleorderBVO[]) voi
											.getChildrenVO();
									SaleOrderVO aggvo = map.get(code);
									SaleorderBVO[] aggbvo = (SaleorderBVO[]) aggvo
											.getChildrenVO();
									SaleorderBVO[] newvo = new SaleorderBVO[bvo.length
											+ aggbvo.length];
									for (int j = 0; j < bvo.length; j++) {
										bvo[j].setCrowno(String
												.valueOf(((j + 1) * 10)));
										newvo[j] = bvo[j];
									}
									for (int j = 0; j < aggbvo.length; j++) {
										aggbvo[j].setCrowno(String.valueOf(((j
												+ bvo.length + 1) * 10)));
										newvo[bvo.length + j] = aggbvo[j];
									}
									map.get(code).setChildrenVO(newvo);
								}
							}
							Iterator it = map.entrySet().iterator();
							List<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
							while (it.hasNext()) {
								Map.Entry entry = (Map.Entry) it.next();
								SaleOrderVO value = (SaleOrderVO) entry.getValue();
								list.add(value);
							}
							IPubDMO ipubdmo = (IPubDMO) NCLocator.getInstance()
									.lookup(IPubDMO.class.getName());
							ipubdmo.N_XX_Action("PreKeep", "30",getClientEnvironment().getDate().toString(), list);

							showHintMessage("订单导入成功！");
  						}
					} catch (Exception e) {
						e.printStackTrace();
						showErrorMessage(e.getMessage());
						showHintMessage("订单导入失败！");
					} finally {
						dialog.end();

					}
				}
			};
			new Thread(checkRun).start();
		} else {
			return;
		}
	}

	// add by zwx 2014-9-1
	@SuppressWarnings("restriction")
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
	
	@Override
	  public void setNoEditItem() {
		  super.setNoEditItem();
		  if(getCorpPrimaryKey().equals("1078")||getCorpPrimaryKey().equals("1100")){
			  	getBillCardPanel().getBodyItem("noriginalcurtaxprice").setEnabled(true);
				getBillCardPanel().getBodyItem("noriginalcurprice").setEnabled(true);
				getBillCardPanel().getBodyItem("noriginalcursummny").setEnabled(true);
		  }
	  }

}