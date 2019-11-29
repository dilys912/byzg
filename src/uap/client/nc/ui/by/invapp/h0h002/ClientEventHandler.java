package nc.ui.by.invapp.h0h002; 
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.by.invapp.billmanage.AbstractEventHandler;
import nc.ui.cm.cm301.SubString;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.by.invapp.h0h001.INVCLATTRIBUTEVO01;
import nc.vo.by.invapp.h0h002.INVBASDOCVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;
 
/** 
 *
 * */ 
public class ClientEventHandler extends AbstractEventHandler{ 
	 
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) { 
		super(billUI, control); 
	} 
	
	@Override
	protected String getHeadCondition() {
		return null;
	}
	@Override
	public void onBoAudit() throws Exception {
		getBillUI().setUserObject(_getOperator());
		//super.onBoAudit();
		//onBoRefresh();
		BillItem vbills =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus");
		Object vbillstatus = getHeadValue("vbillstatus");
		if(vbillstatus != null && !vbillstatus.toString().equals("1")  && !vbillstatus.toString().equals("8")){
			super.onBoAudit();
		}else{
			getBillUI().showErrorMessage("当前单据状态不可审核！");
		}
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		int result=getBillUI().showYesNoMessage("弃审将删除已生成的存货基本档案、存货管理档案，物料生产档案。\n确定此操作吗?");
		if(result==UIDialog.ID_YES){
			super.onBoCancelAudit();
		}
	}
	
	@Override
	public void onBoQuery() throws Exception {
		onBoQuery2();
	}
	
	protected String strCon="";
	
	/**
	 * 功能: 查询加条件,按制单日期排序
	 * @throws Exception
	 */
	
	public void onBoQuery2() throws Exception {
		StringBuffer strWhere = getWhere();
	    if (askForQueryCondition(strWhere) == false)
	        return;// 用户放弃了查询
	    Object makedate = getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate");
	    Object vbillcode = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillcode");
	    if (!_getCorp().getPk_corp().equals("0001")) {
		    strWhere.append(" and pk_corp='"+_getCorp().getPk_corp()+"' ");
		}
	    if(makedate!=null&&vbillcode!=null)
	    	strWhere.append(" order by dmakedate desc,vbillcode desc");
	    else if(makedate!=null)
	    	strWhere.append(" order by dmakedate desc");
	    else if(vbillcode!=null)
	    	strWhere.append(" order by vbillcode desc");
	    String StringWhere = strWhere.toString();
	    StringWhere = StringWhere.replaceAll("8steel", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("1baseinfo", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("6corpdef", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("7physical", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("6usableamount", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("3control", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("4def", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("4stock", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("9def", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("2meas", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("5maninfo", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("5plan", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("3free", "bd_invappdoc");
        StringWhere = StringWhere.replaceAll("new", "bd_invappdoc");
	    
	    SuperVO[] queryVos = queryHeadVOs(StringWhere);
	    strCon=StringWhere;
	    getBufferData().clear();
	    addDataToBuffer(queryVos);
	    updateBuffer();
	}
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void onBoSave() throws Exception {
		if(isMaxkucun()){
			getBillUI().showErrorMessage("最高库存不能小于最低库存！");
			//getBillCardPanelWrapper().getBillCardPanel().getHeadItem("maxstornum").setValue("");
			return;
		}
		Object invcode = getHeadValue("invcode");
		if(isOkinvcode(invcode)){
			getBillUI().showErrorMessage("存货编码不合格！\n物料存货编码以21，22，23，24，25，26开头的\n其正确格式应为：存货分类编码+字符。\n且存货编码长度必须为14位！");
			return;
		}
		if (invcode!=null) { 
			String sql = "select pk_invbasdoc from bd_invbasdoc where nvl(dr,0)=0 and invcode = '"+invcode+"' ";
			IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List<INVBASDOCVO> list = (List<INVBASDOCVO>) qurey.executeQuery(sql,new BeanListProcessor(INVBASDOCVO.class));
			if (list!=null&&list.size()>0) {
				getBillUI().showErrorMessage("相同的存货编码已经存在，不可保存！");
				return;
			}
		}
		//初始化按钮状态，设置单据状态为null,保存后设回原来状态,wkf---start---
        	//getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setValue(
                //Integer.toString(IBillStatus.FREE));
        	//wkf--end---
		super.onBoSave();
		showOldHItems();
		super.onBoRefresh();
	}
	private boolean isOkinvcode(Object invcode) {
		// TODO Auto-generated method stub
		boolean invcodefruit = false;
		String pk_invcl = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invcl").getValueObject().toString();
		String queryinv = "select invclasscode from bd_invcl where nvl(dr,0)=0 and pk_invcl = '"+pk_invcl+"'";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		MapListProcessor alp = new MapListProcessor();
		ArrayList invclasscode = null;
		try {
			invclasscode = (ArrayList) query.executeQuery(queryinv.toString(), alp);
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
		String value = null;
		int invclasscodes =0;
		if (invclasscode != null && invclasscode.size() > 0) {
			for (Object map : invclasscode) {
				Map row = (Map) map;
				Iterator<String> keys = row.keySet().iterator();
				while(keys.hasNext()){
					String key = keys.next(); 
					value = row.get(key).toString();
					invclasscodes = value.length();
				}
			}
		}
		String invcode1 = invcode.toString();//存货编码
		int invcode1s = invcode1.length();
		if(value.startsWith("21") || value.startsWith("22") || value.startsWith("23") || value.startsWith("24") || value.startsWith("25") || value.startsWith("26")){
			if(invcode1s !=14){//存货编码为14位。
				invcodefruit = true; 
			}else{
				String invcodes = invcode1.substring(0, invclasscodes);
				if(!value.equals(invcodes)){
					invcodefruit = true; 
				}else{
					invcodefruit = false;
				}
			}
		}
		return invcodefruit;
	}
	private boolean isMaxkucun() {
		//功能：判断最低库存小于最高库存 wkf
		boolean isok = false;
		String max = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("maxstornum").getValue().toString();
		String min = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lowstocknum").getValue().toString();
		if(min != null)
		{
			UFDouble maxnum = new UFDouble(max);
			UFDouble minnum = new UFDouble(min);
			if(maxnum.doubleValue() < minnum.doubleValue()){
				isok = true;
			}else{
				isok = false;
			}
		}
		return isok;
	}
	private void showOldHItems() {
		BillItem[] hitems = getBillCardPanelWrapper().getBillCardPanel().getHeadItems();
		for (BillItem billItem : hitems) {
			OldShow(billItem);
		}
	}

	private void OldShow(BillItem billItem) {
		ClientUI ui  = (ClientUI) getBillUI();
		Map<String, String> init_showitem_map = ui.hsbills;
		Iterator<String> ite = init_showitem_map.keySet().iterator();
		while(ite.hasNext()){
			String key = ite.next();
			if(key.equals(billItem.getKey())){
				billItem.getComponent().setVisible(true);
				billItem.getCaptionLabel().setText(init_showitem_map.get(key));
			}
		}
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		showOldHItems();
	}
	@Override
	protected void onBoCommit() throws Exception {
		// TODO Auto-generated method stub
		//super.onBoCommit();
		Object vbillstatus = getHeadValue("vbillstatus");
		if(vbillstatus != null && !vbillstatus.toString().equals("1") && !vbillstatus.toString().equals("3")){
			super.onBoCommit();
		}else{
			getBillUI().showErrorMessage("当前单据状态不可提交！");
		}
	}
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
//		setInitHeadnullflag();
	}

	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
//		Object pk_invcl = getHeadValue("pk_invcl");
//		if (pk_invcl!=null) {
//			setAttnullflag(pk_invcl);
//		}
	}
	/**
	 * 根据物料分类属性设置字段是否必输
	 * 
	@SuppressWarnings("unchecked")
	private void setAttnullflag(Object pk_invcl) throws BusinessException {
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select * from bd_invcl_attribute where nvl(dr,0)=0 and pk_invcl = '"+pk_invcl+"' ";
		List<INVCLATTRIBUTEVO01> list = (List<INVCLATTRIBUTEVO01>) qurey.executeQuery(sql,new BeanListProcessor(INVCLATTRIBUTEVO01.class));
		if (list!=null&&list.size()>0) {
			INVCLATTRIBUTEVO01 sxvo = list.get(0);
			String[] fields = sxvo.getAttributeNames();
			for (int i = 0; i < fields.length; i++) {
				String itemkeyi = fields[i];
				if (!itemkeyi.equals("invclasscode")&&!itemkeyi.equals("invclassname")&&!itemkeyi.equals("invclasslev")&&!itemkeyi.equals("pk_invcl")
						&&!itemkeyi.equals("pk_corp")&&!itemkeyi.equals("pk_invclattribute")&&!itemkeyi.equals("ts")&&!itemkeyi.equals("dr")
						&&!itemkeyi.equals("isused")&&!itemkeyi.equals("sealflag")&&!itemkeyi.equals("fixedflag")) {
					System.out.println(itemkeyi);
					BillItem headitem = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(itemkeyi);
					int type = headitem.getDataType();
					if (type != 4) {//如果不是逻辑类型，并且值为‘Y’，那么设置为必输项目
						if ("Y".equals(sxvo.getAttributeValue(itemkeyi).toString())) {
							getBillCardPanelWrapper().getBillCardPanel().getHeadItem(itemkeyi).setNull(true);
						}else {
							getBillCardPanelWrapper().getBillCardPanel().getHeadItem(itemkeyi).setNull(false);
						}
					}else{
						if (itemkeyi.equals("qualitymanflag")) {
							getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qualitydaynum").setNull(UFBoolean.valueOf(sxvo.getAttributeValue(itemkeyi).toString()).booleanValue());
						}
					}
				}
			}
		}
	}*/

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
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(itemkeyi).setNull(isnull);
			}
		}
	}*/

} 
