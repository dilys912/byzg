package nc.ui.by.invapp.h0h002;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import com.sun.tools.javac.code.Attribute.Array;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.bd.languagetransformations.Transformations;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.CorpVO;
import nc.vo.by.invapp.h0h001.INVCLATTRIBUTEVO01;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 
 * */
@SuppressWarnings("serial")
public class ClientUI extends nc.ui.by.invapp.billmanage.AbstractMultiChildClientUI {

	Map<String, String> hsbills = new HashMap<String, String>();
	
	public ClientUI() {
		int aa = 8;
		aa = aa+8;
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		BillItem[] hsitems = getBillCardPanel().getHeadShowItems();
		
		for (BillItem billItem : hsitems) {
			hsbills.put(billItem.getKey(), billItem.getName());
		}
	}

	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.createController());
	}

	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	protected nc.ui.trade.bsdelegate.BusinessDelegator createBusinessDelegator() {
		return new nc.ui.by.invapp.billmanage.CommonBusinessDelegator(this);
	}
	
 	@Override
 	public boolean beforeEdit(BillItemEvent e) {
 		String ekey = e.getItem().getKey();
 		//高温蒸煮
 		if (ekey.equals("def16")) {
 	 		UIRefPane pane = (UIRefPane)getBillCardPanel().getHeadItem("def16").getComponent();
 		     if(pane!=null){
 		    	 String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ010' )";
 		    	 pane.getRef().getRefModel().addWherePart(sql,true);
 		     }
 		     pane.updateUI();
		}else if (ekey.equals("def1")) {
		     //产品大类
		     UIRefPane pane1 = (UIRefPane)getBillCardPanel().getHeadItem("def1").getComponent();
		     if(pane1!=null){
		    	 String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BY22' )";
		    	 pane1.getRef().getRefModel().addWherePart(sql,true);
		     }
		     pane1.updateUI();
			
		}
 		/*
		else if(ekey.equals("def10")){
			//固含量
			UIRefPane pane2 = (UIRefPane)getBillCardPanel().getHeadItem("def10").getComponent();
			if(pane2!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BY1016' )";
				pane2.getRef().getRefModel().addWherePart(sql,true);
			}
			pane2.updateUI();
			
		}
		*/
		else if(ekey.equals("def13")){
			//成品系统
			UIRefPane pane3 = (UIRefPane)getBillCardPanel().getHeadItem("def13").getComponent();
			if(pane3!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ007' )";
				pane3.getRef().getRefModel().addWherePart(sql,true);
			}
			pane3.updateUI();
			
		}else if(ekey.equals("def14")){
			//成品系列
			UIRefPane pane4 = (UIRefPane)getBillCardPanel().getHeadItem("def14").getComponent();
			if(pane4!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ008' )";
				pane4.getRef().getRefModel().addWherePart(sql,true);
			}
			pane4.updateUI();
			
		}else if(ekey.equals("def15")){
			//成品工艺
			UIRefPane pane5 = (UIRefPane)getBillCardPanel().getHeadItem("def15").getComponent();
			if(pane5!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ009' )";
				pane5.getRef().getRefModel().addWherePart(sql,true);
			}
			pane5.updateUI();
			
		}else if(ekey.equals("def17")){
			//默认设备
			UIRefPane pane6 = (UIRefPane)getBillCardPanel().getHeadItem("def17").getComponent();
			if(pane6!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ011' )";
				pane6.getRef().getRefModel().addWherePart(sql,true);
			}
			pane6.updateUI();
			
		}else if(ekey.equals("def18")){
			//材质
			UIRefPane pane7 = (UIRefPane)getBillCardPanel().getHeadItem("def18").getComponent();
			if(pane7!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ005' )";
				pane7.getRef().getRefModel().addWherePart(sql,true);
			}
			pane7.updateUI();
			
		}else if(ekey.equals("def12")){
			//规格(用于成品销售开票)
			UIRefPane pane8 = (UIRefPane)getBillCardPanel().getHeadItem("def12").getComponent();
			if(pane8!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ017' )";
				pane8.getRef().getRefModel().addWherePart(sql,true);
			}
			pane8.updateUI();
		}else if(ekey.equals("gldef1")){
			//库存分类
			UIRefPane pane = (UIRefPane)getBillCardPanel().getHeadItem("gldef1").getComponent();
			if(pane!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'kcfl' )";
				pane.getRef().getRefModel().addWherePart(sql,true);
			}
			pane.updateUI();
		}else if(ekey.equals("gldef3")){
			//受控件
			UIRefPane pane = (UIRefPane)getBillCardPanel().getHeadItem("gldef3").getComponent();
			if(pane!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ012' )";
				pane.getRef().getRefModel().addWherePart(sql,true);
			}
			pane.updateUI();
		}else if(ekey.equals("gldef12")){
			//规格(用于成品销售开票)
			UIRefPane pane = (UIRefPane)getBillCardPanel().getHeadItem("gldef12").getComponent();
			if(pane!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ017' )";
				pane.getRef().getRefModel().addWherePart(sql,true);
			}
			pane.updateUI();
		}else if(ekey.equals("gldef16")){
			//规格(用于成品合格证打印)
			UIRefPane pane = (UIRefPane)getBillCardPanel().getHeadItem("gldef16").getComponent();
			if(pane!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ016' )";
				pane.getRef().getRefModel().addWherePart(sql,true);
			}
			pane.updateUI();
		}else if(ekey.equals("scdef1")){
			//是否重点监控物料
			UIRefPane pane = (UIRefPane)getBillCardPanel().getHeadItem("scdef1").getComponent();
			if(pane!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'DBZ062' )";
				pane.getRef().getRefModel().addWherePart(sql,true);
			}
			pane.updateUI();
		}else if(ekey.equals("scdef3")){
			//备件生产线
			UIRefPane pane = (UIRefPane)getBillCardPanel().getHeadItem("scdef3").getComponent();
			if(pane!=null){
				String sql = " and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = 'BZ013' )";
				pane.getRef().getRefModel().addWherePart(sql,true);
			}
			pane.updateUI();
		}
 		return super.beforeEdit(e);
 	}
 	
 	@Override
 	protected void initSelfData() {
 		super.initSelfData();
 	}
 	
 	@Override
 	public void setDefaultData() throws Exception {
 		super.setDefaultData();
 		//初始化按钮状态，设置单据状态为null,wkf---start---
    	//getBillCardPanel().getHeadItem("vbillstatus").setValue(null);
    	//wkf---end---
 		setHeadValue("pk_corp_app", _getCorp().getPrimaryKey());
 		/**
 		 * 功能：带出库存组织
 		 * wangkaifei
 		 * 2014/08/08
 		 * start
 		 * */
 		//判断是否
 		String pkc = getClientEnvironment().getInstance().getCorporation().getPrimaryKey();
 		if(pkc != "0001"){
 	 		String pkcorp = getBillCardPanel().getHeadItem("pk_corp_app").getValue().toString();
 	 		if(pkcorp != null){
 	 			StringBuffer sb = new StringBuffer();
 				sb.append(" select pk_calbody from bd_calbody where nvl(dr,0)=0 and pk_corp = '"+pkcorp+"'");
 				IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
 				MapListProcessor alp = new MapListProcessor();
 				ArrayList<HashMap<String, String>> zuzhi = null;
 				try {
 					zuzhi = (ArrayList<HashMap<String, String>>) query.executeQuery(sb.toString(), alp);
 				} catch (Exception e2) {
 					// TODO: handle exception
 					e2.printStackTrace();
 				}
 				
 				if (zuzhi != null && zuzhi.size() > 0) {
 					HashMap<String, String> addrMap = zuzhi.get(0);
 					String bodyname = addrMap.get("pk_calbody");
 					getBillCardPanel().getHeadItem("pk_calbody").setValue(bodyname);
 				}
 	 		}
 		}
 		/**
 		 * 功能：带出库存组织
 		 * wangkaifei
 		 * 2014/08/08
 		 * end
 		 * */
 		
 	}
 	//判断是存货分类是否末级
 	private boolean isendflog(BillEditEvent e) {
		// TODO Auto-generated method stub
 		boolean endflog = false;
 		Object pk_invcl = getHeadValueObject(e.getKey());
 		if(pk_invcl != null){
 			String esql = "select invclasscode from bd_invcl where nvl(dr,0)=0 and pk_invcl = '"+pk_invcl+"'";
 			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			MapListProcessor alp = new MapListProcessor();
			ArrayList invclasscode = null;
			try {
				invclasscode = (ArrayList) query.executeQuery(esql.toString(), alp);
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			String value = null;
			if (invclasscode != null && invclasscode.size() > 0) {
				//Array list = (Array) invclasscode.get(0);
				//String invclass = list['invclasscode'];
				for (Object map : invclasscode) {
					Map row = (Map) map;
					Iterator<String> keys = row.keySet().iterator();
					while(keys.hasNext()){
						String key = keys.next(); 
						value = row.get(key).toString();
					}
				}
				String issql = "select invclasscode from bd_invcl where invclasscode like '"+value+"%' and invclasscode != '"+value+"'";
				IUAPQueryBS query1 = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				MapListProcessor alp1 = new MapListProcessor();
				ArrayList  isend= null;
				try {
					isend = (ArrayList) query.executeQuery(issql.toString(), alp1);
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
				if(isend !=null && isend.size()>0){
					endflog = false;
				}else{
					endflog = true;
				}
			}
 		}
 		return endflog;
	}
	/**
	 * 编辑后事件
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if (e.getKey().equals("pk_invcl")) {
			if(isendflog(e)){
				Object pk_invcl = getHeadValueObject("pk_invcl");
				if (pk_invcl!=null) {
					IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					String sql = "select * from bd_invcl_attribute where nvl(dr,0)=0 and pk_invcl = '"+pk_invcl+"' ";
					try {
						List<INVCLATTRIBUTEVO01> list = (List<INVCLATTRIBUTEVO01>) qurey.executeQuery(sql,new BeanListProcessor(INVCLATTRIBUTEVO01.class));
						if (list!=null&&list.size()>0) {
							INVCLATTRIBUTEVO01 sxvo = list.get(0);
							String[] fields = sxvo.getAttributeNames();
							for (int i = 0; i < fields.length; i++) {
								String itemkeyi = fields[i];
								if (!itemkeyi.equals("invclasscode")&&!itemkeyi.equals("invclassname")&&!itemkeyi.equals("invclasslev")&&!itemkeyi.equals("pk_invcl")
										&&!itemkeyi.equals("pk_corp")&&!itemkeyi.equals("pk_invclattribute")&&!itemkeyi.equals("ts")&&!itemkeyi.equals("dr")
										&&!itemkeyi.equals("isused")&&!itemkeyi.equals("sealflag")&&!itemkeyi.equals("fixedflag")) {
									BillItem headitem = getBillCardPanel().getHeadItem(itemkeyi);
									System.out.println(itemkeyi);
									int type = headitem.getDataType();
									if (type == 4) {//如果是逻辑类型，直接进行赋值
										setHeadValue(itemkeyi, UFBoolean.valueOf(sxvo.getAttributeValue(itemkeyi).toString()));
										if (itemkeyi.equals("qualitymanflag")) {
											getBillCardPanel().getHeadItem("qualitydaynum").setNull(UFBoolean.valueOf(sxvo.getAttributeValue(itemkeyi).toString()).booleanValue());
										}
									}else{
										//如果不是逻辑类型，并且值为‘Y’，那么设置为必输项目
//										if ("Y".equals(sxvo.getAttributeValue(itemkeyi).toString())) {
//											getBillCardPanel().getHeadItem(itemkeyi).setNull(true);
//										}else{
//											getBillCardPanel().getHeadItem(itemkeyi).setNull(false);
//										}
										//如果不是逻辑类型,并且为保质期天数则直接赋值。
										if (itemkeyi.equals("qualitydaynum")) {
											String num = sxvo.getAttributeValue(itemkeyi)==null?"":sxvo.getAttributeValue(itemkeyi).toString();
											setHeadValue(itemkeyi, num);
										}
									}
								}
							}
//							setInitHeadnullflagbyY();
						}
					} catch (BusinessException e1) {
						showErrorMessage(e1.getMessage());
					}
				}
//				else{
//					try {
//						setInitHeadnullflag();
//					} catch (BusinessException e1) {
//						showErrorMessage(e1.getMessage());
//					}
//				}
				//---设置物料属性维护单未勾选的项隐藏---start---wkf
				Object pkinvcl = getHeadValueObject("pk_invcl");
				if(pkinvcl !=null){
					//StringBuffer invsql = new StringBuffer();
					String invsql = "select qualitydaynum, qualitymanflag, materclass, gldef7, gldef16, isautoatpcheck, pk_stordoc, pk_invclass1, lowestprice,aheadbatch, "
							+ "discountflag, isfatherofbom, ecobatch, aheadcoff, flanmadenum, gldef19, primaryflag, scheattr_show, "
							+ "storname, isinvreturned, shipunitnum, psnname4, invtype, isselfapprsupplier, gldef17, issendbydatum, "
							+ "materstate, vcalbody, gldef15, pk_cubasdoc, issupplierstock, deptname3, sfzzcp, zdhd, isinvretfreeofchk, "
							+ "isrecurrentcheck, free5name, materclass_show, purchasestge, sfpchs, matertype_show, unitweight, fgysname, "
							+ "invlifeperiod, usableamount0, zgysname, pk_measname1, fcpclgsfa, glfree2, gldef4, wholemanaflag, "//
							+ "abcpurchase, glfree1, gldef18, scdef5, usableamount9, free5, fcpclgsfa_show, pk_meassalename, negallowed, def13, "
							+ "keepwasterate, pk_psndoc4, pk_prodline, supplytype_show, pk_cumandoc, gldef5, glfree3, currentamount, pk_psndoc3, "
							+ "autobalancemeas, refsaleprice, safetystocknum, gldef6, ismngstockbygrswt, psnname3, glfree4, minmulnum, width, invcode, "
							+ "realusableamount, usableamount13, scdef3, invpinpai, scdef4, def7, usableamount1, scscddms, wasterrate, free1name, planprice, "
							+ "def6, gldef1, usableamount4, usableamount12, issalable, free4name, pchscscd_show, gldef2, mantaxitem, "
							+ "zbczjyxm, isctlprodplanprice, maxstornum, isctlbyfixperiod, gldef10, setpartsflag, pk_measstoname, "
							+ "isprimarybarcode, iscanpurchased, usableamount14, lowstocknum, batchperiodnum,"
							+ "usableamount15, fixedahead, zgys, rationwtnum, def20, abcfundeg, gldef9, iselementcheck, usableamount7, "
							+ "scheattr, outtrackin, def11, usableamount6, blgdsczkxs, scdef1, pk_measdoc3,  sfrpc, pk_measdoc2,"//qualitydaynum,
							+ "fgys, pchscscd, isnoconallowed, invmnecode, cksj, sffzfw, chngamount, abcgrosspft, "
							+ "supplytype, storeunitnum, purwasterate, def8, invname, pk_measdoc1, pk_deptdoc3, sfcbdx, iscostbyorder, "
							+ "def12, usableamount5, isused, ybgys, def9, unitvolume, def5, pk_measdoc5, iscreatesonprodorder, "
							+ "invbarcode, usableamountbyfree2, assistunit, abctype, usableamount11, ckjlcname, batchrule, cgzz, "
							+ "outpriority, man, stocklowerdays, scscddms_show, stockbycheck, maxprice, def15, "
							+ "iscansold, pricemethod_show, cgzzname, jhj, scxybzsfzk, mainmeasuredoc, confirmtime, isuseroute, "
							+ "expaybacktax, netwtnum, iscansaleinvoice, pk_prodline1, def19, sellproxyflag, isspecialty, bas, factoryname, "
							+ "isctoutput, pk_rkjlcid, matertype, scdef2, weitunitnum, invshortname, flanlennum, virtualflag, gfwlbm, "
							+ "sumahead, usableamount3, pk_calbody, unitname, gldef13, def10, mainsuppliername, isappendant, length, "
							+ "usableamount2, isvirtual, wghxcl, gldef11, sfzb, usableamount10, nyzbmxs, stockupperdays, pk_dftfactory, "
							+ "def4, abssales, rkjlcname, free2name, roadtype_show, outnumhistorydays, batchrule_show, free3name, "
							+ "pk_taxitems, grosswtnum, glfree5, issecondarybarcode, ybgysname, pk_measproname, graphid, roundingnum, "
							+ "isstorebyconvert, def14, ckcb, outtype_show, accquiretime, materstate_show, pebegin, isconfigable, "
							+ "forinvname, combineflag, gldef3, costprice, zbxs, jyrhzdyw, gldef20, fixperiodbegin, def18, pricemethod, "
							+ "isfxjz, def1, gldef12, jyrhzdyw_show, chkfreeflag, wggdsczkxs, serialmanaflag, peend, flanwidenum, "
							+ "bomtype, gldef14, wghxcl_show, converseflag, free4, invspec, zbczjyxmname, free3, pro, roadtype, "
							+ "nbzyj, issend, def17, free2, usableamountbyfree1, datumofsend, pk_ckjlcid, iswholesetsend, height, "
							+ "def16, isinvretinstobychk, endahead, prevahead, usableamount8, lowlevelcode, prodarea, pk_measdoc, "
							+ "gldef8, primnessnum, accflag, laborflag, pk_measpurname, def2, free1, outtype, "
							+ "iscancalculatedinvcost, bomtype_show, usableamountbyfree0, def3, taxitemsname "
							+ "from bd_invcl_attribute where nvl(dr,0)=0 and pk_invcl = '"+pkinvcl+"'";
					
					IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					MapListProcessor alp = new MapListProcessor();
					ArrayList shuxing = null;
					try {
						shuxing = (ArrayList) query.executeQuery(invsql.toString(), alp);
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
					if (shuxing != null && shuxing.size() > 0) {
						for (Object map : shuxing) {
							Map row = (Map) map;
							Iterator<String> keys = row.keySet().iterator();
							while(keys.hasNext()){
								String key = keys.next(); 
								String value = row.get(key)==null?"N":row.get(key).toString();
								isNumDo(key,value,row);//处理默认值业务方法.
								if("Y".equals(value)){
									continue;
								}
								if("N".equals(value)){
									BillItem items = getBillCardPanel().getHeadItem(key);
									if(items != null){
//										getBillCardPanel().getHeaderPanel("6corpdef").hide();
										
										JComponent panle = items.getComponent();
										panle.setVisible(false);
										UILabel la = items.getCaptionLabel();
										la.setText(null);
									}
								}
							}
						}
						
					}
				}
				
				//---设置物料属性维护单未勾选的项隐藏---end---wkf
			}else{
				Object pk_invcl = getHeadValueObject("pk_invcl");
				if(!pk_invcl.equals(null)){
					showErrorMessage("存货分类不是末级分类,请重新选择！");
					getBillCardPanel().getHeadItem("pk_invcl").setValue("");
				}
			}
			
		}else if (e.getKey().equals("qualitymanflag")) {
			getBillCardPanel().getHeadItem("qualitydaynum").setNull(Boolean.valueOf(getHeadValueObject("qualitymanflag").toString()));
			Object qualitymanflag = getBillCardPanel().getHeadItem("qualitymanflag").getValueObject();
			BillItem qualty = getBillCardPanel().getHeadItem("qualitydaynum");
			if(qualitymanflag.equals("false")){
				qualty.setValue("");
				qualty.setEdit(false);
			}else{
				qualty.setEdit(true);
			}
		}
		/**
		 * 功能：申请公司带出库存组织
		 * wkf
		 * 2014-08-07
		 * start
		 * */
		
		else if(e.getKey().equals("pk_corp_app")){
			String pkc = getClientEnvironment().getInstance().getCorporation().getPrimaryKey();
	 		if(pkc != "0001"){
	 			String pkcorp = getBillCardPanel().getHeadItem("pk_corp_app").getValue().toString();
	 			StringBuffer sb = new StringBuffer();
	 			sb.append(" select pk_calbody from bd_calbody where nvl(dr,0)=0 and pk_corp = '"+pkcorp+"'");
	 			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	 			MapListProcessor alp = new MapListProcessor();
	 			ArrayList<HashMap<String, String>> zuzhi = null;
	 			try {
	 				zuzhi = (ArrayList<HashMap<String, String>>) query.executeQuery(sb.toString(), alp);
	 			} catch (Exception e2) {
	 				// TODO: handle exception
	 				e2.printStackTrace();
	 			}
	 			
	 			if (zuzhi != null && zuzhi.size() > 0) {
	 				HashMap<String, String> addrMap = zuzhi.get(0);
	 				String bodyname = addrMap.get("pk_calbody");
	 				getBillCardPanel().getHeadItem("pk_calbody").setValue(bodyname);
	 			}
	 			
	 		}
		}
		/**
		 * 功能：申请公司带出库存组织
		 * wkf
		 * 2014-08-07
		 * end
		 * */
		else if(e.getKey().equals("qualitydaynum")){
			Object qualitymanflag = getBillCardPanel().getHeadItem("qualitymanflag").getValueObject();
			BillItem qualty = getBillCardPanel().getHeadItem("qualitydaynum");
			if(qualitymanflag.equals("false")){
				showErrorMessage("保质期管理没有勾选，不能设置保质期天数！");
				qualty.setValue("");
				qualty.setEdit(false);
			}
		}
	
}
	/**
	 * 初始设置字段是否必输
	 * 
	@SuppressWarnings("unchecked")
	private void setInitHeadnullflagbyY() throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append(" select * ") 
		.append("   from pub_billtemplet_b ") 
		.append("  where pk_billtemplet in ") 
		.append("        (select pk_billtemplet ") 
		.append("           from pub_billtemplet ") 
		.append("          where pk_billtypecode = 'TA02') ") 
		.append("    and pos = 0 and showflag = 1 and nullflag = 1 ")
		.append("    and nvl(dr, 0) = 0 ");
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<BillTempletBodyVO> list = (List<BillTempletBodyVO>) qurey.executeQuery(sb.toString(),new BeanListProcessor(BillTempletBodyVO.class));
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				BillTempletBodyVO vo = list.get(i);
				String itemkeyi = vo.getItemkey();
				System.out.println(itemkeyi);
				Boolean isnull = vo.getNullflag();
				getBillCardPanel().getHeadItem(itemkeyi).setNull(isnull);
			}
		}
	}

	/**
	 * 初始设置字段是否必输
	 * 
	@SuppressWarnings("unchecked")
	private void setInitHeadnullflag() throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append(" select * ") 
		.append("   from pub_billtemplet_b ") 
		.append("  where pk_billtemplet in ") 
		.append("        (select pk_billtemplet ") 
		.append("           from pub_billtemplet ") 
		.append("          where pk_billtypecode = 'TA02') ") 
		.append("    and pos = 0 and showflag = 1 ")
		.append("    and nvl(dr, 0) = 0 ");
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<BillTempletBodyVO> list = (List<BillTempletBodyVO>) qurey.executeQuery(sb.toString(),new BeanListProcessor(BillTempletBodyVO.class));
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				BillTempletBodyVO vo = list.get(i);
				String itemkeyi = vo.getItemkey();
				System.out.println(itemkeyi);
				Boolean isnull = vo.getNullflag();
				getBillCardPanel().getHeadItem(itemkeyi).setNull(isnull);
			}
		}
	}
	
	*/
	/**
	  * SFClientUtil.openLinkedQueryDialog打开联查
	 */
	
	private void isNumDo(String key, String value,Map row) {
		// 处事带默认值的数据业务。wkf
		
		if(key.equals("qualitydaynum")){
			String value2 = row.get(key)==null?"":row.get(key).toString();
			getBillCardPanel().getHeadItem("qualitydaynum").setValue(value2);//给保质期天数设默认值
		}
		if(key.equals("qualitymanflag")){//如果为保质期管理且值为"N"，则隐藏保质期天数字段
			if("N".equals(value)){
				BillItem items = getBillCardPanel().getHeadItem("qualitydaynum");
				if(items != null){
					JComponent panle = items.getComponent();
					panle.setVisible(false);
					UILabel la = items.getCaptionLabel();
					la.setText(null);
				}
			}
		}
		//集团自由项1
		if(key.equals("free1")){
			if(value.equals(null)){
				BillItem items = getBillCardPanel().getHeadItem("free1");
				if(items != null){
					JComponent panle = items.getComponent();
					panle.setVisible(false);
					UILabel la = items.getCaptionLabel();
					la.setText(null);
				}
			}else{
				getBillCardPanel().getHeadItem("free1").setValue(value);
			}
		}
		//管理自由项1
		if(key.equals("glfree1")){
			if(value.equals(null)){
				BillItem items = getBillCardPanel().getHeadItem("glfree1");
				if(items != null){
					JComponent panle = items.getComponent();
					panle.setVisible(false);
					UILabel la = items.getCaptionLabel();
					la.setText(null);
				}
			}else{
				getBillCardPanel().getHeadItem("glfree1").setValue(value);
			}
		}
		//生产自定义项1
		if(key.equals("scdef1")){
			if(value.equals(null)){
				BillItem items = getBillCardPanel().getHeadItem("scdef1");
				if(items != null){
					JComponent panle = items.getComponent();
					panle.setVisible(false);
					UILabel la = items.getCaptionLabel();
					la.setText(null);
				}
			}else{
				getBillCardPanel().getHeadItem("scdef1").setValue(value);
			}
		}
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
		String pk = querydata.getBillID();
		setCurrentPanel(BillTemplateWrapper.CARDPANEL);
		if(!Toolkits.isEmpty(pk)){
			//加载数据
			try {
				getBufferData().addVOToBuffer(loadHeadData(pk));
				setListHeadData(getBufferData().getAllHeadVOsFromBuffer());
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	} 
	
}
