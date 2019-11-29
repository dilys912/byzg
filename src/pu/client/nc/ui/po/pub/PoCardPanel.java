package nc.ui.po.pub;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.itf.scm.cenpur.service.CentrPurchaseUtil;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.busi.ComAbstrDefaultRefModel;
import nc.ui.bd.ref.busi.JobmngfilDefaultRefModel;
import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.po.OrderAfterBillHelper;
import nc.ui.po.OrderHelper;
import nc.ui.po.oper.PoToftPanel;
import nc.ui.po.oper.ReplenishUI;
import nc.ui.po.oper.RevisionUI;
import nc.ui.po.ref.OtherRefModel;
import nc.ui.po.ref.PoCustBankRefModel;
import nc.ui.po.ref.PoDeliverAddressRefModel;
import nc.ui.po.ref.PoFreeCustBankRefModel;
import nc.ui.po.ref.PoInputInvRefModel;
import nc.ui.po.ref.PoReceiveAddrRefModel;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuGetUIValueTool;
import nc.ui.pu.pub.PuProjectPhaseRefModel;
import nc.ui.pu.pub.PuTool;
import nc.ui.pu.pub.PubHelper;
import nc.ui.pu.pub.PurDeptRefModel;
import nc.ui.pu.pub.PurPsnRefModel;
import nc.ui.pu.ref.PuBizTypeRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillSortListener;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.scm.freecust.UFRefGridUI;
import nc.ui.scm.hqhp.QPSchmRefModel;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.ui.scm.pu.ParaVOForBatch;
import nc.ui.scm.pub.CacheTool;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.ref.WarehouseRefModel;
import nc.ui.scm.service.LocalCallService;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.po.OrderstatusVO;
import nc.vo.po.afterbill.OrderAfterBillVO;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.po.pub.PoSaveCheckParaVO;
import nc.vo.po.pub.PoVendorVO;
import nc.vo.po.pub.RetPoVrmAndParaPriceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ProductCode;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;
import nc.vo.scm.field.pu.FieldMaxLength;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.service.ServcallVO;

 
//zhwj

public class PoCardPanel extends BillCardPanel implements BillEditListener,
        BillEditListener2, ActionListener, BillBodyMenuListener,
        IBillModelSortPrepareListener,BillCardBeforeEditListener, BillSortListener {
    private String m_strCbiztype = null;
    private POPubSetUI2 m_cardPoPubSetUI2 = null;
    private PoCntSelDlg cntSelDlg = null;
    //查询后续状态：订单的后续状态
    private HashMap h_mapAfterBill = new HashMap();
    //调用者，该调用者需实现接口nc.ui.po.pub.PoCardPanelInterface
    private Container m_conInvoker = null;
    private String[] m_saBodyRefItemKey = null;
    //表头、表体所有参照的ITEMKEY
    private String[] m_saHeadRefItemKey = null;
    private static String NO_AFTERBILL = "";
    //
    private static ArrayList m_listFixedFields = new ArrayList();
    //有后续操作的行 不可修改的项：
    //行号、合同号、存货、自由项、批次号、扣税类别、税率、币种、使用部门、项目、项目阶段、收货仓库、收发货地址、折本汇率、折辅汇率、赠品  V5：、收货公司、收货库存组织
    private static String[] saFixedItem = new String[] { "crowno", "ccontractcode",
            "cinventorycode", "vfree", "vproducenum", "idiscounttaxtype",
            "ntaxrate", "ccurrencytype", "cusedept", "cproject",
            "cprojectphase", "cwarehouse", "vreceiveaddress",
            "nexchangeotobrate", "nexchangeotoarate", "blargess","arrvcorpname","arrvstoorgname"};
    /*V5注意：收票公司业务逻辑不尽相同，需要处理：参见beforeEdit()*/ 
    static{
    	for(int i=0;i<saFixedItem.length;i++){
    		m_listFixedFields.add(saFixedItem[i]);
    	}
    }
    /**
     * 作者：李亮 功能：数据改变时触发此事件 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-10-21 WYF 加入含税单价修改后触发的处理
     */
    public void afterEdit(BillEditEvent e) {

        //表头
        if (e.getPos() == BillItem.HEAD) {
            //不是编辑状态，不处理编辑后事件
            if(!getBillData().getHeadItem(e.getKey()).isEnabled()){
              return;
            }
            if (e.getSource() == getHeadItem("ccurrencytypeid").getComponent()) {
                //币种
                afterEditWhenHeadCurrency(e);
            } else if (e.getSource() == getHeadItem("ntaxrate").getComponent()) {
                //税率
                afterEditWhenHeadTaxRate(e);
            } else if (e.getSource() == getHeadItem("nexchangeotobrate")
                    .getComponent()
                    || e.getSource() == getHeadItem("nexchangeotoarate")
                            .getComponent()) {
                //汇率
                afterEditWhenHeadExchangeRate(e);
            } else if (e.getSource() == getHeadItem("cvendormangid")
                    .getComponent()) {
                afterEditWhenHeadVendor(e);
            } else if (e.getSource() == getHeadItem("cfreecustid")
                    .getComponent()) {
                //散户
                afterEditWhenHeadFreecust(e);
            } else if (e.getSource() == getHeadItem("cdeptid").getComponent()) {
                //部门
                afterEditWhenHeadDept(e);
            } else if (e.getSource() == getHeadItem("cemployeeid")
                    .getComponent()) {
                //业务员
                afterEditWhenHeadEmployee(e);
            } else if (e.getSource() == getHeadItem("creciever").getComponent()) {
                //收货方
                afterEditWhenHeadReciever(e);
            } else if (e.getSource() == getHeadItem("dorderdate")
                    .getComponent()) {
                //订单日期
                afterEditWhenHeadOrderDate(e);
            }
            /*V5 Del:
            else if (e.getSource() == getHeadItem("cstoreorganization")
                    .getComponent()) {
                //库存组织
                afterEditWhenHeadStoreOrg(e,getHeadItem("cstoreorganization").getValue());
            }
            */ 
            else if (e.getSource() == getHeadItem("vmemo").getComponent()) {
                //备注
                afterEditWhenHeadMemo(e);
            }
//            else if (e.getSource() == getHeadItem("cdeliveraddress").getComponent()) {
//                //供应商收发货地址
//                afterEditWhenHeadAddr(e);
//            }
            else if (e.getSource() == getHeadItem("bdeliver").getComponent()){
                afterEditWhenHeadDeliver(e);
            }
            //表头自定义项
            else if (e.getKey().startsWith("vdef")){
                DefSetTool.afterEditHead(getBillData(),e.getKey(),"pk_defdoc"+e.getKey().substring(e.getKey().indexOf("vdef")+4));
            }
            
            //zhwj 
            else if(e.getSource() == getHeadItem("iszgprice").getComponent()){
            	 UFBoolean iszgprice = getHeadItem("iszgprice").getValueObject()==null?new UFBoolean(false):
            		 new UFBoolean(getHeadItem("iszgprice").getValueObject().toString());
            	 
            	 String[] iszgpricevalues = getIszgpricevalues(iszgprice);
            	 
            	 if(iszgpricevalues==null||iszgpricevalues.length!=2
            	    ||iszgpricevalues[0]==null||iszgpricevalues[0].equals("")
            		||iszgpricevalues[1]==null||iszgpricevalues[1].equals("")){
            		 MessageDialog.showErrorDlg(null, "错误", "自定义项档案BY1104设置有误，请检查！") ;
            		 return;
            	 }
            	 
            	 int rows = getBillModel().getRowCount();
            	 if(rows>0){
            		 for(int i=0;i<rows;i++){
            			 getBillModel().setValueAt(iszgpricevalues[0], i, "vdef17");
            			 getBillModel().setValueAt(iszgpricevalues[1], i, "pk_defdoc17");
            			 getBillModel().setRowState(i, BillModel.MODIFICATION);
            		 }
            	 }
            }
            
            
        		// 无表体行
        		if (PuTool.isLastCom(this, e) && getBillModel().getRowCount() <= 0) {
        			onActionAppendLine();
        		}
        		PuTool.setFocusOnLastCom(this, e);
        }
        //表体
        else if (e.getPos() == BillItem.BODY) {
            int iRow = e.getRow();

            //请单独保留些IF，用于判断与辅数量的关系
            if (e.getKey().equals("nordernum")) {
                /*V5:取消辅计量管理存货必须先输入辅数量的控制
            	afterEditWhenBodyNum(e);
            	*/
                calDplanarrvdate(e.getRow(),e.getRow());
            }
            if (e.getKey().equals("crowno")) {
                //行号
                BillRowNo.afterEditWhenRowNo(this, e, "crowno");
            } else if (e.getKey().equals("cinventorycode")) {
                //修改存货
                afterEditWhenBodyInventory(e);
            } else if (e.getKey().equals("ccurrencytype")) {
                //币种
                afterEditWhenBodyCurrency(e);
            } else if (e.getKey().equals("idiscounttaxtype")
                    || e.getKey().equals("nordernum")
                    || e.getKey().equals("noriginalcurprice")
                    || e.getKey().equals("noriginalnetprice")
                    || e.getKey().equals("ntaxrate")
                    || e.getKey().equals("noriginalcurmny")
                    || e.getKey().equals("noriginaltaxmny")
                    || e.getKey().equals("noriginaltaxpricemny")
                    || e.getKey().equals("ndiscountrate")
                    || e.getKey().equals("nconvertrate")
                    || e.getKey().equals("nassistnum")
                    || e.getKey().equals("norgtaxprice")
                    || e.getKey().equals("norgnettaxprice")) {
                //数量、单价、金额
                afterEditWhenBodyRelationsCal(e);
            }  else if (e.getKey().equals("nexchangeotobrate")) {
                afterEditWhenBodyExchangeRate(e);
            } else if (e.getKey().equals("cassistunitname")) {
                //辅计量单位
                afterEditWhenBodyAssistunit(e);
            } else if (e.getKey().equals("vfree")) {
                //自由项
                PoEditTool.afterEditWhenBodyFree(getBillModel(), iRow, "vfree");
            } else if (e.getKey().equals("cproject")) {
                //项目
                afterEditWhenBodyProject(e);
            } else if (e.getKey().equals("vmemo")) {
                //备注
                afterEditWhenBodyMemo(e);
            } else if (e.getKey().equals("ccontractcode")) {
                //合同
                afterEditWhenBodyCntCode(e);
            } else if (e.getKey().equals("vreceiveaddress")) {
                //收发货地址
                afterEditWhenBodyAddr(e);
            } else if (e.getKey().equals("vvenddevaddr")) {
                //供应商收发货地址
                afterEditWhenBodyAddrVendor(e);
            } else if (e.getKey().startsWith("vdef")){
                //表体自定义项
                DefSetTool.afterEditBody(getBillModel(),e.getRow(),e.getKey(),"pk_defdoc"+e.getKey().substring(e.getKey().indexOf("vdef")+4));
            } else if(e.getKey().startsWith("arrvcorpname")){
            	//收货公司
            	afterEditBodyArrCorp(e);
            } else if(e.getKey().startsWith("arrvstoorgname")){
            	//收货库存组织
            	afterEditWhenBodyArrStoOrg(e);
            } else if (e.getKey().equals("cwarehouse")) {
            	//收货仓库
                afterEditWhenBodyArrWare(e);
            } else if(e.getKey().equals("reqcorpname")){
            	//需求公司
            	afterEditWhenBodyReqCorp(e);
            } else if(e.getKey().equals("reqstoorgname")){
            	//需求库存组织
            	afterEditWhenBodyReqStoOrg(e);
            } else if(e.getKey().equals("reqwarename")){
            	//需求仓库
            	afterEditWhenBodyReqWare(e);
            }
            
            //zhwj
            if(e.getKey().startsWith("vdef17")){
            	String vdef17 = getBillModel().getValueAt(e.getRow(),"vdef17")==null?"":
            		getBillModel().getValueAt(e.getRow(),"vdef17").toString();
            	
            	if(vdef17!=null&&vdef17.equals("是")){
            		checkZGPrice(e.getRow());
            	}
            }
            
        }
    }
    /**
	 * 编辑后事件：需求仓库
	 */
	private void afterEditWhenBodyReqWare(BillEditEvent e) {
        
		String strPkCalBody = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"pk_reqstoorg"));
		String strPkWare = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"pk_creqwareid"));
		if(strPkWare == null){
			SCMEnv.out("修改需求仓库时，带出需求库存组织、需求公司默认值,仓库为空，直接返回。");
			return;
		}
		Object[] oaRet = null;
		try{
	    	//设置需求库存组织
	    	if(strPkCalBody == null){
	    		oaRet = (Object[]) CacheTool.getCellValue("bd_stordoc","pk_stordoc","pk_calbody",strPkWare);
	    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
	    			SCMEnv.out("根据仓库档案ID[ID值：“"+strPkWare+"”]不能获取所属库存组织ID!");
	    		}else{
	    			strPkCalBody = oaRet[0].toString().trim();
	    			//-----------------
	    			setBodyValueAt(strPkCalBody,e.getRow(),"pk_reqstoorg");
	        		oaRet = (Object[]) CacheTool.getCellValue("bd_calbody","pk_calbody","bodyname",strPkCalBody);
		    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
		    			SCMEnv.out("根据所属库存组织ID[ID值：“"+strPkCalBody+"”]不能获取所属库存组织名称!");
		    		}else{
		    			//--------------
		    			setBodyValueAt(oaRet[0],e.getRow(),"reqstoorgname");
		    		}
	    		}
	    	}
	    	/*
	    	//设置需求公司
	    	if(strReqCorp == null){
	    		oaRet = (Object[]) CacheTool.getCellValue("bd_stordoc","pk_stordoc","pk_corp",strPkWare);
	    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
	    			SCMEnv.out("根据需求仓库档案ID[ID值：“"+strPkWare+"”]不能获取所属公司ID!");
	    		}else{
	    			strReqCorp = oaRet[0].toString().trim();
	    			//----------------
	    			setBodyValueAt(strReqCorp,e.getRow(),"pk_reqcorp");
	        		oaRet = (Object[]) CacheTool.getCellValue("bd_corp","pk_corp","unitname",strReqCorp);
		    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
		    			SCMEnv.out("根据所属公司ID[ID值：“"+strReqCorp+"”]不能获取所属公司名称!");
		    		}else{
		    			//------------------
		    			setBodyValueAt(oaRet[0],e.getRow(),"reqcorpname");
		    		}
	    		}
	    	}
	    	*/
		}catch(BusinessException be){
			SCMEnv.out(be.getMessage());
		}
	}
    /**
	 * 编辑后事件：需求库存组织
	 */
	private void afterEditWhenBodyReqStoOrg(BillEditEvent e) {
		/*
    	String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"pk_reqcorp"));
		String strPkCalBody = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"pk_reqstoorg"));
    	//设置需求公司
    	if(strReqCorp == null && strPkCalBody != null){
    		Object[] oaRet = null;
    		try{
    			oaRet = (Object[]) CacheTool.getCellValue("bd_calbody","pk_calbody","pk_corp",strPkCalBody);
    		}catch(BusinessException be){
    			SCMEnv.out(be.getMessage());
    		}
    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
    			SCMEnv.out("根据需求库存组织ID[ID值：“"+strPkCalBody+"”]不能获取所属公司ID!");
    			return;
    		}
        	setBodyValueAt(oaRet[0],e.getRow(),"pk_reqcorp");	
    		try{
    			oaRet = (Object[]) CacheTool.getCellValue("bd_corp","pk_corp","unitname",oaRet[0].toString().trim());
    		}catch(BusinessException be){
    			SCMEnv.out(be.getMessage());
    		}
    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
    			SCMEnv.out("根据公司ID[ID值：“"+oaRet[0]+"”]不能获取公司名称!");
    			return;
    		}
        	setBodyValueAt(oaRet[0],e.getRow(),"reqcorpname");	
    	}
    	*/
		// 清空需求仓库
		setBodyValueAt(null, e.getRow(), "reqwarename");
		setBodyValueAt(null, e.getRow(), "pk_creqwareid");
	}
    /**
	 * 编辑后事件：需求公司
	 */
	private void afterEditWhenBodyReqCorp(BillEditEvent e) {
		// 清空需求库存组织
		setBodyValueAt(null, e.getRow(), "reqstoorgname");
		setBodyValueAt(null, e.getRow(), "pk_reqstoorg");
		// 清空需求仓库
		setBodyValueAt(null, e.getRow(), "reqwarename");
		setBodyValueAt(null, e.getRow(), "pk_creqwareid");
	}
    /**
     * <p>功能：编辑后事件：收货仓库
     * <p>作者：晁志平
     * <p>日期：2006-03-08
     * <p>@since V31SP1
     */
    private void afterEditWhenBodyArrWare(BillEditEvent e) {

        //修改仓库时，到货地址可能变化
        String sRevPk = getHeadItem("creciever").getValue();
        int iRow = e.getRow();

        //如表头的收货方为空，则默认为仓库的地址，可编辑
        if (PuPubVO.getString_TrimZeroLenAsNull(sRevPk) == null) {

            Object sWareId = getBillModel().getValueAt(iRow, "cwarehouseid");
            Object[][] oaVal = null;
            //如果仓库的地址为空，则默认为表头库存组织默认地址、地区、地点
            if (PuPubVO.getString_TrimZeroLenAsNull(sWareId) == null) {
            	/*V5：删除cstoreorganization的相关调整
                String sStorOrgId = getHeadItem("cstoreorganization").getValue();
                oaVal = CacheTool.getMultiColValue("bd_calbody", "pk_calbody", new String[]{"area","pk_areacl","pk_address"}, new String[]{sStorOrgId});
                String sStorOrgAddr = null;
                String sStorOrgAreaId = null;
                String sStorOrgAddrId = null;
                if(oaVal != null && oaVal.length > 0 && oaVal[0] != null){
                    sStorOrgAddr = (String) oaVal[0][0];
                    sStorOrgAreaId = (String) oaVal[0][1];
                    sStorOrgAddrId = (String) oaVal[0][2];
                }
                getBillModel().setValueAt(sStorOrgAddr, iRow, "vreceiveaddress");
                getBillModel().setValueAt(sStorOrgAreaId, iRow, "cdevareaid");
                getBillModel().setValueAt(sStorOrgAddrId, iRow, "cdevaddrid");
                oaVal = CacheTool.getMultiColValue("bd_areacl", "pk_areacl", new String[]{"areaclname"}, new String[]{sStorOrgAreaId});
                String sAreaName = null;
                if(oaVal != null && oaVal.length > 0 && oaVal[0] != null){
                	sAreaName = (String) oaVal[0][0];
                }
                getBillModel().setValueAt(sAreaName, iRow, "cdevareaname");
                oaVal = CacheTool.getMultiColValue("bd_address", "pk_address", new String[]{"addrname"}, new String[]{sStorOrgAddrId});
                String sAddrName = null;
                if(oaVal != null && oaVal.length > 0 && oaVal[0] != null){
                	sAddrName = (String) oaVal[0][0];
                }
                getBillModel().setValueAt(sAddrName, iRow, "cdevaddrname");
                */
            } else {
                oaVal = CacheTool.getMultiColValue("bd_stordoc", "pk_stordoc", new String[]{"storaddr","pk_address"}, new String[]{sWareId.toString()});
                String sWareAddr = null;
                String sWareAddrId = null;
                if(oaVal != null && oaVal.length > 0 && oaVal[0] != null){
                	sWareAddr = (String) oaVal[0][0];
                	sWareAddrId = (String) oaVal[0][1];
                }                
                //地点名称
                oaVal = CacheTool.getMultiColValue("bd_address", "pk_address", new String[]{"addrname"}, new String[]{sWareAddrId});
                String sWareAddrName = null;
                if(oaVal != null && oaVal.length > 0 && oaVal[0] != null){
                	sWareAddrName = (String) oaVal[0][0];
                }
                //仓库所属地区由仓库所属地点带出
                oaVal = CacheTool.getMultiColValue("bd_address", "pk_address", new String[]{"pk_areacl"}, new String[]{sWareAddrId});
                String sWareAreaId = null;
                if(oaVal != null && oaVal.length > 0 && oaVal[0] != null){
                	sWareAreaId = (String) oaVal[0][0];
                }
                //地区名称
                oaVal = CacheTool.getMultiColValue("bd_areacl", "pk_areacl", new String[]{"areaclname"}, new String[]{sWareAreaId});
                String sWareAreaName = null;
                if(oaVal != null && oaVal.length > 0 && oaVal[0] != null){
                	sWareAreaName = (String) oaVal[0][0];
                }
                if(PuPubVO.getString_TrimZeroLenAsNull(sWareAddr) != null){
                  getBillModel().setValueAt(sWareAddr, iRow, "vreceiveaddress");
                }
                if(PuPubVO.getString_TrimZeroLenAsNull(sWareAreaId) != null){
                  getBillModel().setValueAt(sWareAreaId, iRow, "cdevareaid");
                }
                if(PuPubVO.getString_TrimZeroLenAsNull(sWareAddrId) != null){
                  getBillModel().setValueAt(sWareAddrId, iRow, "cdevaddrid");
                }
                if(PuPubVO.getString_TrimZeroLenAsNull(sWareAreaName) != null){
                  getBillModel().setValueAt(sWareAreaName, iRow, "cdevareaname");
                }
                if(PuPubVO.getString_TrimZeroLenAsNull(sWareAddrName) != null){
                  getBillModel().setValueAt(sWareAddrName, iRow, "cdevaddrname");
                }
            }

        }
        //表头收货方不空，则取表头收货方相关默认地址
        else{
            setDefaultValueWhenHeadRecieverChged(new BillEditEvent(getBodyItem("cwarehouse").getComponent(),null,"cwarehouse",e.getRow(),BillItem.BODY),sRevPk);
        }
        /*V5：删除cstoreorganization后的相关调整
        //检查仓库是否与库存组织不符
        if(getHeadItem("cstoreorganization").getValue() != null){ 
	        PuTool.afterEditWareValidWithStoreOrg(getBillModel(), e, getHeadItem(
	                "cstoreorganization").getValue(), "cwarehouseid",
	                new String[] { "cwarehouse" });
        }
        */

        //---V5:修改收货仓库时，带出收货库存组织、收货公司默认值
        
		String strPkCalBody = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"pk_arrvstoorg"));
		String strPkWare = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"cwarehouseid"));
		if(strPkWare == null){
			SCMEnv.out("修改收货仓库时，带出收货库存组织、收货公司默认值,仓库为空，直接返回。");
			return;
		}
		Object[] oaRet = null;
		try{
	    	//设置收货库存组织
	    	if(strPkCalBody == null){
	    		oaRet = (Object[]) CacheTool.getCellValue("bd_stordoc","pk_stordoc","pk_calbody",strPkWare);
	    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
	    			SCMEnv.out("根据仓库档案ID[ID值：“"+strPkWare+"”]不能获取所属库存组织ID!");
	    		}else{
	    			strPkCalBody = oaRet[0].toString().trim();
	    			//-----------------
	    			setBodyValueAt(strPkCalBody,e.getRow(),"pk_arrvstoorg");
	        		oaRet = (Object[]) CacheTool.getCellValue("bd_calbody","pk_calbody","bodyname",strPkCalBody);
		    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
		    			SCMEnv.out("根据所属库存组织ID[ID值：“"+strPkCalBody+"”]不能获取所属库存组织名称!");
		    		}else{
		    			//--------------
		    			setBodyValueAt(oaRet[0],e.getRow(),"arrvstoorgname");
		    		}
	    		}
	    	}
	    	/*
	    	//设置收货公司
	    	if(strArrCorp == null){
	    		oaRet = (Object[]) CacheTool.getCellValue("bd_stordoc","pk_stordoc","pk_corp",strPkWare);
	    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
	    			SCMEnv.out("根据仓库档案ID[ID值：“"+strPkWare+"”]不能获取所属公司ID!");
	    		}else{
	    			strArrCorp = oaRet[0].toString().trim();
	    			//----------------
	    			setBodyValueAt(strArrCorp,e.getRow(),"pk_arrvcorp");
	        		oaRet = (Object[]) CacheTool.getCellValue("bd_corp","pk_corp","unitname",strArrCorp);
		    		if(oaRet == null || oaRet.length == 0 || oaRet[0] == null || oaRet[0].toString().trim().length() == 0){
		    			SCMEnv.out("根据所属公司ID[ID值：“"+strArrCorp+"”]不能获取所属公司名称!");
		    		}else{
		    			//------------------
		    			setBodyValueAt(oaRet[0],e.getRow(),"arrvcorpname");
		    		}
	    		}
	    	}
	    	*/
		}catch(BusinessException be){
			SCMEnv.out(be.getMessage());
		}
    }
    /**
     * <p>编辑后事件：收货库存组织
     * <p> i.清空收货仓库
     * <p>ii.带入计划到货日期
     */
    private void afterEditWhenBodyArrStoOrg(BillEditEvent e){
    	int iRow = e.getRow();
    	//清空收货仓库
    	setBodyValueAt(null,iRow,"cwarehouse");
    	setBodyValueAt(null,iRow,"cwarehouseid");
    	//带入计划到货日期
        calDplanarrvdate(iRow, iRow);
    }
    /**
	 * 编辑后事件：收货公司
	 */
	private void afterEditBodyArrCorp(BillEditEvent e) {
		// 清空收货库存组织
		setBodyValueAt(null, e.getRow(), "arrvstoorgname");
		setBodyValueAt(null, e.getRow(), "pk_arrvstoorg");
		// 清空收货仓库
		setBodyValueAt(null, e.getRow(), "cwarehouse");
		setBodyValueAt(null, e.getRow(), "cwarehouseid");
	}
    /**
	 * 作者：李亮 功能：表体选中行改变时触发此事件 该方法为监听事件方法 参数：BillEditEvent e 捕捉到的BillEditEvent事件
	 * 返回：无 例外：无 日期：(2001-10-20 11:39:21) 修改日期，修改人，修改原因，注释标志：
	 */
    public void bodyRowChange(BillEditEvent e) {
    }
    /**
     * PoOrderManage 构造子注解。
     */
    public PoCardPanel(Container cntInvoker, String sCorp, String sBillType, POPubSetUI2 setUi) {

        if(setUi != null){
            m_cardPoPubSetUI2 = setUi;
        }else{
            m_cardPoPubSetUI2 = new POPubSetUI2();
        }
        setContainer(cntInvoker);
        setBillType(sBillType);
        setCorp(sCorp);
        initi();
    }

    /**
     * 作者：李亮 功能：用于响应ActionListener事件的函数 参数：ActionEvent e 事件 返回：无 例外：无
     * 日期：(2002-4-22 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    public void actionPerformed(ActionEvent e) {

        //HEAD
        if (e.getSource() == ((UIRefPane) getHeadItem("cemployeeid")
                .getComponent()).getUIButton()) {
            setRefPane_Head("cemployeeid");
        }else if (e.getSource() == ((UIRefPane) getHeadItem("cdeliveraddress")
                    .getComponent()).getUIButton()) {
            setRefPane_Head("cdeliveraddress");
        }
    }

    /**
     * 作者：王印芬 功能：修改表体备注后的相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenBodyAddr(BillEditEvent e) {

        String str = ((UIRefPane) getBodyItem("vreceiveaddress").getComponent()).getUITextField().getText();
        if (PuPubVO.getString_TrimZeroLenAsNull(str) != null) {
            setBodyValueAt(str, e.getRow(), "vreceiveaddress");
        } else {
            //表体地址清空，则表体相关地区、地点也清空
            setBodyValueAt("",e.getRow(), "vreceiveaddress");
            setBodyValueAt(null, e.getRow(), "cdevareaid");
            setBodyValueAt(null, e.getRow(), "cdevaddrid");
            setBodyValueAt(null, e.getRow(), "cdevareaname");
            setBodyValueAt(null, e.getRow(), "cdevaddrname");
            return;
        }
        //收发货地址变化后对地区、地点默认值的影响
        Object oVal = ((UIRefPane) getBodyItem("vreceiveaddress").getComponent()).getRefValue("bd_custaddr.pk_custaddr");
        String sAddrId = null;
        if (oVal != null && oVal.toString().trim().length() > 0){
            sAddrId = oVal.toString().trim();
        }
        //设置默认值（注：手工修改则不设置）
	    if (PuPubVO.getString_TrimZeroLenAsNull(sAddrId) != null){
		    Object[][] objs = CacheTool.getMultiColValue("bd_custaddr",
		            "pk_custaddr", new String[] { "pk_areacl", "pk_address" },
		            new String[] { sAddrId });
		    if (objs != null && objs.length == 1) {
		        setBodyValueAt(objs[0][0], e.getRow(), "cdevareaid");
		        setBodyValueAt(objs[0][1], e.getRow(), "cdevaddrid");
		        String[] aryAddr = new String[] {
		                "cdevareaname->getColValue(bd_areacl,areaclname,pk_areacl,cdevareaid)",
		                "cdevaddrname->getColValue(bd_address,addrname,pk_address,cdevaddrid)" };
		        getBillModel().execFormulas(e.getRow(), aryAddr);
		    }
	    }
    }
    /**
     * 作者：晁志平 功能：修改表体供应商收发货地址后的相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无
     * 例外：无 日期：(2004-11-17 13:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenBodyAddrVendor(BillEditEvent e) {

        String str = ((UIRefPane) getBodyItem("vvenddevaddr").getComponent()).getUITextField().getText();
        if (PuPubVO.getString_TrimZeroLenAsNull(str) != null) {
            setBodyValueAt(str, e.getRow(), "vvenddevaddr");
        } else {
            //表体地址清空，则表体相关地区、地点也清空
            setBodyValueAt(null,e.getRow(), "vvenddevaddr");
            setBodyValueAt(null, e.getRow(), "cvenddevareaid");
            setBodyValueAt(null, e.getRow(), "cvenddevaddrid");
            setBodyValueAt(null, e.getRow(), "cvenddevareaname");
            setBodyValueAt(null, e.getRow(), "cvenddevaddrname");
            return;
        }
        //供应商收发货地址变化后对地区、地点默认值的影响
        Object oVal = ((UIRefPane) getBodyItem("vvenddevaddr").getComponent()).getRefValue("bd_custaddr.pk_custaddr");
        String sAddrId = null;
        if (oVal != null && oVal.toString().trim().length() > 0){
	            sAddrId = oVal.toString().trim();
	    }
	    //设置默认值（注：手工修改则不设置）
		if (PuPubVO.getString_TrimZeroLenAsNull(sAddrId) != null){
		    Object[][] objs = CacheTool.getMultiColValue("bd_custaddr",
	              "pk_custaddr", new String[] { "pk_areacl", "pk_address" },
	              new String[] { sAddrId });
		    if (objs != null && objs.length == 1) {
	          setBodyValueAt(objs[0][0], e.getRow(), "cvenddevareaid");
	          setBodyValueAt(objs[0][1], e.getRow(), "cvenddevaddrid");
	          String[] aryAddr = new String[] {
	                  "cvenddevareaname->getColValue(bd_areacl,areaclname,pk_areacl,cvenddevareaid)",
	                  "cvenddevaddrname->getColValue(bd_address,addrname,pk_address,cvenddevaddrid)" };
	          getBillModel().execFormulas(e.getRow(), aryAddr);
		    }
	    }
    }
    /**
     * 作者：李亮 功能：修改辅计量单位后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-16 wyf
     * 函数calRelation()去掉是否固定换算率的参数
     */
    private void afterEditWhenBodyAssistunit(BillEditEvent e) {

        //辅计量单位
        PoEditTool.afterEditWhenBodyAssistunit(getBillModel(), e, new String[] {
                "cbaseid", "cmessureunit", "cassistunit", "cassistunitname",
                "nconvertrate" });

        //换算率改变，计算（数量，单价，含税单价，金额，税率，税额，价税合计,扣率，扣率单价）之间的关系
        BillEditEvent tempE = new BillEditEvent(e.getSource(), e.getValue(),
                "nconvertrate", e.getRow());
        calRelation(tempE);

    }

    /**
     * 作者：王印芬 功能：修改合同后的相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-27 王印芬 合同号参照改变后，加入处理
     * 2002-07-02 王印芬 针对合同号参照的修改，相应修改取getValue的参数 2009-07-27 王印芬
     * 加入代码，以防止双击合同号参照的TEXT区域时，合同号清空的问题
     */
    private void afterEditWhenBodyCntCode(BillEditEvent e) {

        int iRow = e.getRow();
        if (e.getValue() == null || e.getValue().toString().trim().length() < 1) {
            //是否可编辑
            setCellEditable(iRow, "ccontractcode", getBodyItem("ccontractcode") != null && getBodyItem("ccontractcode").isEdit());
            setCellEditable(iRow, "ccurrencytype", getBodyItem("ccurrencytype") != null && getBodyItem("ccurrencytype").isEdit());
            setCellEditable(iRow, "noriginalcurprice", getBodyItem("noriginalcurprice") != null && getBodyItem("noriginalcurprice").isEdit());
            setCellEditable(iRow, "norgtaxprice", getBodyItem("norgtaxprice") != null && getBodyItem("norgtaxprice").isEdit());
            setCellEditable(iRow, "noriginalnetprice", getBodyItem("noriginalnetprice") != null && getBodyItem("noriginalnetprice").isEdit());
            setCellEditable(iRow, "noriginalcurmny", getBodyItem("noriginalcurmny") != null && getBodyItem("noriginalcurmny").isEdit());
            //合同ID 合同行ID 合同号 币种
            setBodyValueAt(null, iRow, "ccontractid");
            setBodyValueAt(null, iRow, "ccontractrowid");
            setBodyValueAt(null, iRow, "ccontractcode");
        } else {
        	try{
	        	AbstractRefModel refmodelCt = ((UIRefPane) getBodyItem("ccontractcode").getComponent()).getRefModel();
	            Object[] oRet = new Object[5];
	            oRet[0] = (String) refmodelCt.getValue("ct_b.pk_ct_manage");
	            oRet[1] = (String) refmodelCt.getValue("ct_b.pk_ct_manage_b");
	            oRet[2] = (String) refmodelCt.getValue("ct.ct_code");
	            oRet[3] = (String) refmodelCt.getValue("ct.currid");
	            oRet[4] = (BigDecimal) refmodelCt.getValue("ct_b.oriprice");
	            //设置值
	            setBodyValueAt(oRet[0], iRow, "ccontractid");
	            setBodyValueAt(oRet[1], iRow, "ccontractrowid");
        	}catch(Exception ee){
        		SCMEnv.out(ee);
        	}
            //设置界面其他项及可编辑性
            setRelated_Cnt(iRow, iRow, true, false);
        }
        //该段代码防止双击合同号参照的TEXT区域时，合同号清空的问题
        UIRefPane pane = ((UIRefPane) getBodyItem("ccontractcode")
                .getComponent());
        pane.setPK(getBodyValueAt(iRow, "ccontractrowid"));
    }

    /**
     * 作者：李亮 功能：修改表体币种后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-15 wyf
     * 提取代码到公共方法setBodyCurrRelated()中 wyf add/modify/delete 2002-05-15 begin/end
     * 2002-07-12 wyf 原先币种变化后取的为默认价，现取为合同价 2002-10-18 wyf 修改前：驱动数据重新计算的KEY为数量，
     * 修改后：为原币单价，该改变为限制所有的改变都会影响计划到货日期的修改
     */
    private void afterEditWhenBodyCurrency(BillEditEvent e) {

        int iRow = e.getRow();
        //设置汇率、金额等的精度
        //setBodyCurrRelated(iRow,iRow,true) ;
        setExchangeRateBody(iRow, true);

        if (e.getValue() == null || e.getValue().toString().trim().equals("")) {
            return;
        }
        setDefaultPrice(iRow, iRow);

        String sCurrTypeId = PuPubVO
                .getString_TrimZeroLenAsNull(getBodyValueAt(iRow,
                        "ccurrencytypeid"));
        if (sCurrTypeId == null) {
            return;
        }

        //以下值应重新触发，因可能币种位数不同，需重新计算
        //因无法得到旧币种ID，因此不管币种精度是否改变，均重新计算，虽然有可能导致数据改变。
        String sChangedKey = "nordernum";
        afterEditWhenBodyRelationsCal(new BillEditEvent(
                getBodyItem(sChangedKey), getBodyValueAt(iRow, sChangedKey),
                sChangedKey, iRow, BillItem.BODY));

        updateUI();
    }

    /**
     * 作者：李亮 功能：修改表体汇率后的相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-04-22 wyf 增加对合同的支持 wyf
     * add/modify/delete 2002-04-22 begin/end
     */
    private void afterEditWhenBodyExchangeRate(BillEditEvent e) {

        ////汇率
        //if ((e.getValue() != null) &&
        // (!(e.getValue().toString().trim().equals("")))){
        //UFDouble nexchangeotobrate = new
        // UFDouble(e.getValue().toString().trim());
        //if (nexchangeotobrate.doubleValue() < 0){
        //MessageDialog.showWarningDlg(this,"提示","汇率不能小于0");
        //getBillModel().setValueAt(null,e.getRow(),"nexchangeotobrate");
        //return ;
        //}
        //}

        //WYF add 2002-11-11 begin
        //最高限价
        setRelated_MaxPrice(e.getRow(), e.getRow());
        //WYF add 2002-11-11 end

    }

    /**
     * 作者：李亮 功能：修改存货后的相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenBodyInventory(BillEditEvent e) {
        
        //关闭合计开关
    	boolean		bOldNeedCalc = getBillModel().isNeedCalculate() ; 
    	getBillModel().setNeedCalculate(false) ;

        UIRefPane refpane = (UIRefPane) getBodyItem("cinventorycode")
                .getComponent();
        String[] saMangId = refpane.getRefPKs();

        nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
        timer.start();

        //为多选作准备
        //当前操作行
        int iOperationRow = e.getRow();
        //操作后的行数组
        int[] iaRow = PuTool.getRowsAfterMultiSelect(iOperationRow,
                saMangId == null ? 1 : saMangId.length);

        //新增(或插入的)行数
        int iNewLen = iaRow.length - 1;
        if (iOperationRow == getRowCount() - 1) {
            //增行
            onActionAppendLine(iNewLen);
            //置行状态为修改状态
            getBillModel().setRowState(iOperationRow,BillModel.MODIFICATION);
        } else {
            //插行
            onActionInsertLines(iOperationRow, iNewLen);
            //置行状态为修改状态
            getBillModel().setRowState(iOperationRow+(saMangId == null ? 1 : saMangId.length)-1,BillModel.MODIFICATION);
        }
        timer.addExecutePhase("插行或增行");/*-=notranslate=-*/

        //结束行
        int iEndRow = iaRow[iNewLen];

        //执行表体公式(读取内存参照信息)
        InvInfoParse.setInvEditFormulaManInfo(this, refpane, iOperationRow,
                iEndRow);
        timer.addExecutePhase("参照中取编码、名称、ID等");/*-=notranslate=-*/

        //辅计量
        setRelated_AssistUnit(iOperationRow, iEndRow);
        timer.addExecutePhase("辅计量");/*-=notranslate=-*/

        //自由项
        for (int i = iOperationRow; i <= iEndRow; i++) {
            setBodyValueAt(null, i, "vfree");
            setBodyValueAt(null, i, "vfree1");
            setBodyValueAt(null, i, "vfree2");
            setBodyValueAt(null, i, "vfree3");
            setBodyValueAt(null, i, "vfree4");
            setBodyValueAt(null, i, "vfree5");
            setBodyValueAt(null, i, "vproducenum");
            
        }
        //setEnabled_BodyFree(iOperationRow,iEndRow) ;
        timer.addExecutePhase("自由项、批次号");/*-=notranslate=-*/

        //税率 如果税率为空，设置存货基本档案中默认税率
        setRelated_Taxrate(iOperationRow, iEndRow);
        timer.addExecutePhase("税率");/*-=notranslate=-*/

        //计划到货日期
        calDplanarrvdate(iOperationRow, iEndRow);
        timer.addExecutePhase("计划到货日期");/*-=notranslate=-*/

        //上层来源单据

        //合同ID
        ArrayList listHaveCntRowId = new ArrayList();
        ArrayList listHaveCntIndex = new ArrayList();
        ArrayList listNotCntIndex = new ArrayList();
        String sUpSourceType = null;
        for (int i = iOperationRow; i <= iEndRow; i++) {
            sUpSourceType = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
                    i, "cupsourcebilltype"));
            //处理相关合同信息
            if (sUpSourceType != null
                    && (sUpSourceType
                            .equals(nc.vo.scm.pu.BillTypeConst.CT_BEFOREDATE) || sUpSourceType
                            .equals(nc.vo.scm.pu.BillTypeConst.CT_ORDINARY))) {
                String sCntRowId = (String) getBodyValueAt(i, "ccontractrowid");
                listHaveCntRowId.add(sCntRowId);
                listHaveCntIndex.add(new Integer(i));
            } else {
                //上层来源不是合同
                getBillModel().setValueAt(null, i, "ccontractid");
                getBillModel().setValueAt(null, i, "ccontractrowid");
                getBillModel().setValueAt(null, i, "ccontractcode");
                listNotCntIndex.add(new Integer(i));
            }
        }

        //已有合同的行
        int iHaveCntLen = listHaveCntIndex.size();
        ArrayList listNoPriceIndex = new ArrayList();
        if (iHaveCntLen > 0) {
            RetCtToPoQueryVO[] voaCntInfo = PoPublicUIClass
                    .getCntInfoArray((String[]) listHaveCntRowId
                            .toArray(new String[iHaveCntLen]));

            for (int i = 0; i < iHaveCntLen; i++) {
                UFDouble dPrice = OrderPubVO.getPriceValueByPricePolicy(
                        voaCntInfo[i], PuTool.getPricePriorPolicy(getCorp()));
                if (dPrice == null) {
                    listNoPriceIndex.add(listHaveCntIndex.get(i));
                }
            }
            //需重新寻找默认价的行
            int iNotPriceLen = listNoPriceIndex.size();
            if (iNotPriceLen > 0) {
                setDefaultPrice((Integer[]) listNoPriceIndex
                        .toArray(new Integer[iNotPriceLen]));
            }
        }
        //需重新寻找合同的行(赠品不关联合同在setRelated_Cnt(Integer[],boolean)方法中处理)
        int iNotCntLen = listNotCntIndex.size();
        if (iNotCntLen > 0) {
            setRelated_Cnt((Integer[]) listNotCntIndex
                    .toArray(new Integer[iNotCntLen]), false, false);
        }
        timer.addExecutePhase("取价");/*-=notranslate=-*/

        //最高限价
        setRelated_MaxPrice(iOperationRow, iEndRow);
        timer.addExecutePhase("最高限价");/*-=notranslate=-*/

    	//输入提示
    	if(getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
    		|| getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)){
    		OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),OrderHeaderVO.class.getName(),OrderItemVO.class.getName());
    		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    		ficr.setFreeItemRenderer(this, voCurr);
    	}
        timer.addExecutePhase("输入提示");/*-=notranslate=-*/

        /* 表体可编辑重新设定
         * 原因：如辅计量，原来是空行，所以以BeforeEdit事件中设定为不可编辑
         * 但如果增加此行存货是有辅计量管理的，那么不做此重新设定，赠此行存货编辅计量仍不可编辑
         */
        if (getIContainer() != null
                && getIContainer().getOperState() == IPoCardPanel.OPER_STATE_EDIT) {
            //设置单据的可编辑性
            int iRow = e.getRow();
            //设置可编辑性
            setEnabled_Body(iRow);
        }
        timer.addExecutePhase("表体可编辑重新设定");/*-=notranslate=-*/
        
        //支持用户定义的编辑后公式执行
        if (getIContainer() != null
                && getIContainer().getOperState() == IPoCardPanel.OPER_STATE_EDIT) {
            BillItem it = getBodyItem(e.getKey());
            if(it.getEditFormulas() != null && it.getEditFormulas().length > 0){
                getBillModel().execFormulas(it.getEditFormulas(),iOperationRow, iEndRow);
            }
        }
        timer.addExecutePhase("存货编辑后公司执行");/*-=notranslate=-*/
    	
        //显示对应存货编码、名称
        String strVendorId = getHeadItem("cvendormangid").getValue();
        PuTool.loadVendorInvInfos(strVendorId,saMangId,getBillModel(),iOperationRow, iEndRow);
        timer.addExecutePhase("显示对应存货编码、名称");/*-=notranslate=-*/

    	//v50:增加空行处理
    	if("cinventorycode".equalsIgnoreCase(e.getKey())){
    		onActionAppendLine(1);
    	}
        timer.addExecutePhase("补充一空行");/*-=notranslate=-*/
        
        //打开合计开关
    	getBillModel().setNeedCalculate(bOldNeedCalc) ;
        timer.addExecutePhase("打开合计开关");/*-=notranslate=-*/
    	
        timer.showAllExecutePhase("存货多选结束");/*-=notranslate=-*/
        
    }

    /**
     * 作者：王印芬 功能：修改表体备注后的相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenBodyMemo(BillEditEvent e) {

        String sValue = ((UIRefPane) getBodyItem("vmemo").getComponent())
                .getUITextField().getText();
        if (PuPubVO.getString_TrimZeroLenAsNull(sValue) != null) {
            setBodyValueAt(sValue, e.getRow(), "vmemo");
        } else {
            setBodyValueAt(((UIRefPane) getBodyItem("vmemo").getComponent())
                    .getText(), e.getRow(), "vmemo");
        }
    }
    /**
     * 作者：王印芬 功能：修改表体项目后的相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenBodyProject(BillEditEvent e) {
        int iRow = e.getRow();
        //项目
        getBillModel().setValueAt(null, iRow, "cprojectphase");
        getBillModel().setValueAt(null, iRow, "cprojectphaseid");
        getBillModel().setValueAt(null, iRow, "cprojectphasebaseid");
        //设定可编辑性
        setEnabled_BodyProjectPhase(iRow);
    }

    /**
     * 作者：李亮 功能：修改数量、单价、金额后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-10-18 wyf
     * 修改前：驱动数据重新计算的KEY为数量， 修改后：为原币单价，该改变为适应：只有数量改变会影响计划到货日期的修改 2003-01-20 wyf
     * 修改前：数量变化导致计划到货日期变化， 修改后：如果数量由空或变为有值，则导致计划到货日期变化，否则不变
     */

    private void afterEditWhenBodyRelationsCal(BillEditEvent e) {

        int iRow = e.getRow();
        if ((!(e.getKey().equals("idiscounttaxtype")))
                && (e.getValue() != null)
                && (!(e.getValue().toString().trim().equals("")))) {
            UFDouble ndata = new UFDouble(e.getValue().toString().trim());
            if (ndata.doubleValue() < 0) {
                if (e.getKey().equals("ntaxrate")) {
                    MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000025")/*@res "税率不能小于0"*/);
                    getBillModel().setValueAt(null, iRow, "ntaxrate");
                    return;
                } else if (e.getKey().equals("noriginalnetprice")) {
                    MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000026")/*@res "净单价不能小于0"*/);
                    getBillModel().setValueAt(null, iRow, "noriginalnetprice");
                    return;
                } else if (e.getKey().equals("noriginalcurprice")) {
                    MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000027")/*@res "单价不能小于0"*/);
                    getBillModel().setValueAt(null, iRow, "noriginalcurprice");
                    return;
                }
            }
            if (e.getKey().equals("ndiscountrate")) {
                //if ((ndata.doubleValue() < 0) || (ndata.doubleValue() > 100))
                // {
                if (ndata.compareTo(VariableConst.ZERO) < 0) {
                    //MessageDialog.showWarningDlg(this,"提示","扣率必须大于0小于100！");
                    MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000028")/*@res "扣率必须大于0"*/);
                    getBillModel().setValueAt(null, iRow, "ndiscountrate");
                    return;
                }
            }
        }

        //wyf 2002-10-21 add begin
        //原有的订单数量
        Object oNum = null;
        if (e.getKey().equals("nordernum")) {
            oNum = e.getOldValue();
        } else {
            oNum = getBodyValueAt(iRow, "nordernum");
        }
        UFDouble dOldNum = (oNum == null || oNum.toString().trim().equals("")) ? VariableConst.ZERO
                : new UFDouble(oNum.toString());
        //wyf 2002-10-21 add end

        //计算数量关系
        calRelation(e);

        //wyf 2003-01-20 modify begin
        //数量变化引起计划到货日期变化
        oNum = getBodyValueAt(iRow, "nordernum");
        UFDouble dCurNum = (oNum == null || oNum.toString().trim().equals("")) ? VariableConst.ZERO
                : new UFDouble(oNum.toString());
        //if ( dOldNum.compareTo(dCurNum)!=0 ) {
        if (dOldNum.compareTo(VariableConst.ZERO) == 0
                && dCurNum.compareTo(VariableConst.ZERO) != 0) {
            //calDplanarrvdate(iRow,e.getValue()) ;
            calDplanarrvdate(iRow, iRow);
        }
        //wyf 2003-01-20 modify end

    }
    /**
     * 作者：李亮 功能：修改表头币种后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2001-12-11 ljq
     * 根据不同的币种，设置表头、表体折辅汇率、折本汇率的精度 2002-06-07 wyf 修改行状态，以便保存时更新 2002-11-11 WYF
     * 加入对最高限价的处理
     */
    private void afterEditWhenHeadCurrency(BillEditEvent e) {
        
        //关闭合计开关
    	boolean		bOldNeedCalc = getBillModel().isNeedCalculate() ; 
    	getBillModel().setNeedCalculate(false) ;
    	
        try {
            String sCurrId = getHeadItem("ccurrencytypeid").getValue();
            String dOrderDate = getHeadItem("dorderdate").getValue();

            //=================表头
            setExchangeRateHead(dOrderDate, sCurrId);

            //=================表体
            if (sCurrId == null || sCurrId.trim().equals("")
                    || getRowCount() == 0) {
            	//
            	getBillModel().setNeedCalculate(bOldNeedCalc) ;
                return;
            }
            ArrayList listAllCurrId = new ArrayList();
            listAllCurrId.add(sCurrId);
            BusinessCurrencyRateUtil bca = new BusinessCurrencyRateUtil(getCorp());
            if(bca.getLocalCurrPK() != null && !listAllCurrId.contains(bca.getLocalCurrPK())){
                listAllCurrId.add(bca.getLocalCurrPK());
            }
            if(bca.getFracCurrPK() != null && !listAllCurrId.contains(bca.getFracCurrPK())){
                listAllCurrId.add(bca.getFracCurrPK());
            }            
//            String[] saCurrId = (String[]) listAllCurrId.toArray(new String[listAllCurrId.size()]);
//            HashMap mapRateMny = m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
//            HashMap mapRate = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),saCurrId);
//            HashMap mapRateEditable = m_cardPoPubSetUI2.getBothExchRateEditableBatch(getCorp(),saCurrId);
//            HashMap mapRateVal = m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),saCurrId,dOrderDate);
            //改变表体没有币种的行
            String sBodyCurrId = null;
            int iLen = getRowCount();
            for (int i = 0; i < iLen; i++) {
                sBodyCurrId = (String) getBillModel().getValueAt(i,"ccurrencytypeid");
                //add by shikun 2014-03-22 宝钢金属下的制罐包装和越南制罐的表头币种变换后表体输入精度问题
                //add  :  (getParentCorpCode().equals("10395")) || 
                if ((getParentCorpCode().equals("10395")) || sBodyCurrId == null || sBodyCurrId.equals("")) {
                    getBillModel().setValueAt(sCurrId, i, "ccurrencytypeid");
                    //获取币种名称
//                    execBodyFormula(i, "ccurrencytype");//优化 V31
                    //驱动表体相关币种的改动
                    afterEditWhenBodyCurrency(new BillEditEvent(getBodyItem(
                            "ccurrencytypeid").getComponent(), sCurrId,
                            "ccurrencytypeid", i));
                    //设置修改标志
                    getBillModel().setRowState(i, BillModel.MODIFICATION);
                }
            }
            getBillModel().execEditFormulaByKey(-1, "ccurrencytype");//优化 V31
        } catch (Exception exp) {
            PuTool.outException(this, exp);
            //打开合计开关
        	getBillModel().setNeedCalculate(bOldNeedCalc) ;
            return;
        }
        //打开合计开关
    	getBillModel().setNeedCalculate(bOldNeedCalc) ;
    }
    /**
     * 作者：李亮 功能：修改表头部门后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     * 
     * @变更记录：
     * <p>2006-06-21, Czp ,V5, 放开采购员受采购部门约束的限制
     */
    private void afterEditWhenHeadDept(BillEditEvent e) {
    	/*

        UIRefPane paneDept = (UIRefPane) getHeadItem("cdeptid").getComponent();
        String sDeptId = PuPubVO.getString_TrimZeroLenAsNull(paneDept
                .getRefPK());
        if (sDeptId == null) {
            return;
        }

        UIRefPane panePsn = (UIRefPane) getHeadItem("cemployeeid")
                .getComponent();
        String sPsnDeptId = PuPubVO.getString_TrimZeroLenAsNull(panePsn
                .getRefModel().getValue("bd_psndoc.pk_deptdoc"));
        if (!sDeptId.equals(sPsnDeptId)) {
            panePsn.setPK(null);
        }
        
        */
    }
    /**
     * 作者：晁志平 功能：修改表头是否发运后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2004-12-13 9:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenHeadDeliver(BillEditEvent e) {
        /*
        String strDeliver =  getHeadItem("bdeliver").getValue();
        if("false".equalsIgnoreCase(strDeliver)){
            getHeadItem("ctransmodeid").setValue(null);
            getHeadItem("ctransmodeid").setEnabled(false);
        }else if ("true".equalsIgnoreCase(strDeliver)){
            getHeadItem("ctransmodeid").setEnabled(true);
        }
        */
    }

    /**
     * 作者：李亮 功能：修改表头业务员后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenHeadEmployee(BillEditEvent e) {

        String sPsnId = ((UIRefPane) getHeadItem("cemployeeid").getComponent())
                .getRefPK();
        if (PuPubVO.getString_TrimZeroLenAsNull(sPsnId) == null) {
            return;
        }

        //由业务员带出默认部门
        UIRefPane ref = (UIRefPane) (getHeadItem("cemployeeid").getComponent());
        //业务员所属部门
        Object sDeptId = ref.getRefModel().getValue("bd_psndoc.pk_deptdoc");
        getHeadItem("cdeptid").setValue(sDeptId);

        //设置业务员默认采购组织
        setPurOrg(sPsnId);
    }
    /**
     *
     * 设置业务员所属采购组织
     * 
     * 两种情况不必设置 ：
     * 
     * 1)、原来采购组织存在值
     * 2)、此业务员分配到多个采购组织
     *
     * **/
    private void setPurOrg(String sPsnId){
        //目前缓存方法不支持一个主键返回多个值的情况
        String strPurId = PuTool.getPurIdByPsnId(sPsnId);
        if(strPurId != null && getHeadItem("cpurorganization") != null && getHeadItem("cpurorganization").getValue() == null){
            getHeadItem("cpurorganization").setValue(strPurId);
        }
    }
    /**
     * 作者：李亮 功能：修改表头汇率后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenHeadExchangeRate(BillEditEvent e) {
        
        //关闭合计开关
    	boolean		bOldNeedCalc = getBillModel().isNeedCalculate() ; 
    	getBillModel().setNeedCalculate(false) ;
    	
        if ((e.getValue() != null)
                && (!(e.getValue().toString().trim().equals("")))) {
            UFDouble nexchangeotobrate = new UFDouble(e.getValue().toString()
                    .trim());
            if (nexchangeotobrate.doubleValue() < 0) {
                MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000029")/*@res "汇率不能小于0"*/);
                getHeadItem(e.getKey()).setValue(null);
                return;
            }
        }
        for (int i = 0; i < getRowCount(); i++) {
            Object o = getBillModel().getValueAt(i, e.getKey());
            if (PuPubVO.getString_TrimZeroLenAsNull(o) == null) {
                getBillModel().setValueAt(e.getValue(), i, e.getKey());
                getBillModel().setRowState(i, BillModel.MODIFICATION);
            }
        }
        
        //打开合计开关 
    	getBillModel().setNeedCalculate(bOldNeedCalc) ;
    }

    /**
     * 作者：李亮 功能：修改表头散户后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2003-02-25 wyf
     * 修改供应商默认信息VO为取自公共类中的VO
     */
    private void afterEditWhenHeadFreecust(BillEditEvent e) {

        String cfreecustid = ((UIRefPane) (getHeadItem("cfreecustid")
                .getComponent())).getRefPK();
        String cvendormangid = ((UIRefPane) (getHeadItem("cvendormangid")
                .getComponent())).getRefPK();
        //setRefPaneBank("cfreecustid", cvendorbaseid, cfreecustid);
        setRefPane_Head("caccountbankid");

        if ((e.getValue() != null)
                && (!(e.getValue().toString().trim().equals("")))) {
            String[] aryBanks = getFreeCustBank(cfreecustid);
            if (aryBanks != null && aryBanks.length != 0) {
                getHeadItem("caccountbankid").setValue(aryBanks[0]);
                getHeadItem("account").setValue(aryBanks[1]);
            }
        } else {
            PoVendorVO vendorVO = null;
            //if ((cvendormangid != null) && (getHashtableVendor() != null) &&
            // (getHashtableVendor().containsKey(cvendormangid.trim()))){
            if ((cvendormangid != null)) {
                vendorVO = PoPublicUIClass.getVendDefaultInfo(cvendormangid);
            }
            if (vendorVO != null) {
                getHeadItem("caccountbankid").setValue(vendorVO.getCcustbank());
                getHeadItem("account").setValue(vendorVO.getCaccount());
            }
        }
    }

    /**
     * 作者：李亮 功能：修改表头备注后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenHeadMemo(BillEditEvent e) {
        //setHeadItem("vmemo",e.getValue());
        setHeadItem("vmomo", ((UIRefPane) getHeadItem("vmemo").getComponent())
                .getRefName());

    }
    /**
     * 作者：李亮 功能：修改订单日期后的相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-27 wyf 加入合同相关信息清空的处理
     * 2002-06-07 wyf 修改行状态，以便保存时更新 2002-11-11 WYF 加入对最高限价的处理
     */
    private void afterEditWhenHeadOrderDate(BillEditEvent e) {
        
        //关闭合计开关
    	boolean		bOldNeedCalc = getBillModel().isNeedCalculate() ; 
    	getBillModel().setNeedCalculate(false) ;

        //订单日期变化引起表体的计划到货日期变化
        if (e.getValue() != null && !e.getValue().toString().trim().equals("")) {
            //改变表头汇率
            String dorderdate = (String) e.getValue();
            setExchangeRateHead(dorderdate, getHeadItem("ccurrencytypeid")
                    .getValue());

            int iCount = getRowCount();
            //计划到货日期
            calDplanarrvdate(0, iCount - 1);
//            String[] saCurrId = new String[iCount];
//            for (int i = 0; i < iCount; i++) {
//                //根据日期重新计算表体汇率
//                saCurrId[i] = (String) getBillModel().getValueAt(i,"ccurrencytypeid");
//            }
//            saCurrId = m_cardPoPubSetUI2.getDistinctStrArray(saCurrId);
//            int iLen = (saCurrId == null) ? 0 : saCurrId.length;
//            if(iLen > 0){
//                HashMap mapRate = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),saCurrId);
//                HashMap mapRateEditable = m_cardPoPubSetUI2.getBothExchRateEditableBatch(getCorp(),saCurrId);
//                String dOrderDate = getHeadItem("dorderdate").getValue();
//                HashMap mapRateVal = m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),saCurrId,dOrderDate);
	            for (int i = 0; i < iCount; i++) {
	                //根据日期重新计算表体汇率
	                String sCurrId = (String) getBillModel().getValueAt(i,"ccurrencytypeid");
	                if (PuPubVO.getString_TrimZeroLenAsNull(sCurrId) != null) {
	                    //设置修改标志
	                    getBillModel().setRowState(i, BillModel.MODIFICATION);
	                    setExchangeRateBody(i, true);
	                }
	            }
//            }
        }
        //合同
        int iCount = getRowCount();
        if (iCount > 0) {
            for (int i = 0; i < iCount; i++) {
                getBillModel().setValueAt(null, i, "ccontractid");
                getBillModel().setValueAt(null, i, "ccontractrowid");
                getBillModel().setValueAt(null, i, "ccontractcode");
            }
            setRelated_Cnt(0, iCount - 1, false, false);

            setRelated_MaxPrice(0, iCount - 1);
        }
        
        //打开合计开关
    	getBillModel().setNeedCalculate(bOldNeedCalc) ;

    }

    /**
     * 作者：李亮 功能：修改表头收货方后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void afterEditWhenHeadReciever(BillEditEvent e) {
        String sReveivePk = ((UIRefPane) (getHeadItem("creciever").getComponent())).getRefPK();
        setDefaultValueWhenHeadRecieverChged(e,sReveivePk);
    }
    private void setDefaultValueWhenHeadRecieverChged(BillEditEvent e,String sReveivePk){
        UIRefPane paneRef = ((UIRefPane) (getBodyItem("vreceiveaddress").getComponent()));
        //表头收货方为空
        if (PuPubVO.getString_TrimZeroLenAsNull(sReveivePk) == null) {
            //如果表头收货方无值，则表体收发货地址不走参照(手工输入、仓库带出、库存组织带出)
            paneRef.setButtonVisible(false);
            for (int i = 0; i < getRowCount(); i++) {
                afterEditWhenBodyArrWare(new BillEditEvent(getBodyItem("cwarehouse").getComponent(),null,"cwarehouse",i,BillItem.HEAD));
                //可编辑
                getBillModel().setRowState(i, BillModel.MODIFICATION);
            }
        }
        //表头收货方非空 : 默认为表头收货方的到货地址。参照客户的收发货地址
        else {
            paneRef.setButtonVisible(true);
            setRefPane_Body("vreceiveaddress");
            //取默认地址
            Object[] oaRet = null;
			try {
				oaRet = (Object[]) nc.ui.scm.pub.CacheTool.getCellValue(
	                    "bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", sReveivePk);
			} catch (Exception ee) {
				/**不必抛出*/
				SCMEnv.out(e);
			}			
            String sRevBaseId = (String) oaRet[0];
            Object[][] oa2Ret = null;
            try {
                oa2Ret = PubHelper.queryResultsFromAnyTable("bd_custaddr",
                        new String[] { "addrname","pk_custaddr" }, "pk_cubasdoc='"
                                + sRevBaseId
                                + "' AND ISNULL(defaddrflag,'N')='Y'");
            } catch (Exception ex) {
            }
            String sAddr = (oa2Ret == null || oa2Ret[0] == null || oa2Ret[0].length == 0) ? null : PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
            String sAddrId = (oa2Ret == null || oa2Ret[0] == null || oa2Ret[0].length == 0) ? null : PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][1]);
		    Object[][] objs = CacheTool.getMultiColValue("bd_custaddr",
		            "pk_custaddr", new String[] { "pk_areacl", "pk_address" },
		            new String[] { sAddrId });
            String strAreaId = null,strAreaName = null,strAddrId = null,strAddrName = null;
		    if (objs != null && objs.length == 1) {
		        strAreaId = (objs[0] == null || objs[0].length == 0) ? null : PuPubVO.getString_TrimZeroLenAsNull(objs[0][0]);
		        strAddrId = (objs[0] == null || objs[0].length < 1) ? null : PuPubVO.getString_TrimZeroLenAsNull(objs[0][1]);
		        if(strAreaId != null){
			    	oa2Ret = CacheTool.getMultiColValue("bd_areacl","pk_areacl", new String[] { "areaclname" },new String[] { strAreaId });
			    	strAreaName = (oa2Ret == null || oa2Ret[0] == null || oa2Ret[0].length == 0) ? null : PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
		        }
		        if(strAddrId != null){
			    	oa2Ret = CacheTool.getMultiColValue("bd_address","pk_address", new String[] { "addrname" },new String[] { strAddrId });
			    	strAddrName = (oa2Ret == null || oa2Ret[0] == null || oa2Ret[0].length == 0) ? null : PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
		        }
		    }
		    int iCnt = getRowCount();      
	    	//2006-02-23   Czp    V5修改：考虑自定义模板中的可编辑性设置 
	        boolean bEditUserDef = getBillModel().getItemByKey("vreceiveaddress").isEdit();
		    if(e.getPos() == BillItem.HEAD){  
		    	for (int i = 0; i < iCnt; i++) {
		            if (sAddr == null) {
		                getBillModel().setValueAt("", i, "vreceiveaddress");
		            }else{
			            getBillModel().setValueAt(sAddr, i, "vreceiveaddress");
		            }
	                getBillModel().setValueAt(strAddrId, i, "cdevaddrid");
	                getBillModel().setValueAt(strAreaId, i, "cdevareaid");
	                getBillModel().setValueAt(strAddrName, i, "cdevaddrname");
	                getBillModel().setValueAt(strAreaName, i, "cdevareaname");
	                getBillModel().setCellEditable(i, "vreceiveaddress", bEditUserDef);
	                getBillModel().setRowState(i, BillModel.MODIFICATION);
	            }
		    }else if(e.getPos() == BillItem.BODY){
	            if (sAddr == null) {
	                getBillModel().setValueAt("", e.getRow(), "vreceiveaddress");
	            }else{
		            getBillModel().setValueAt(sAddr, e.getRow(), "vreceiveaddress");
	            }
                getBillModel().setValueAt(strAddrId, e.getRow(), "cdevaddrid");
                getBillModel().setValueAt(strAreaId, e.getRow(), "cdevareaid");
                getBillModel().setValueAt(strAddrName, e.getRow(), "cdevaddrname");
                getBillModel().setValueAt(strAreaName, e.getRow(), "cdevareaname");
                getBillModel().setCellEditable(e.getRow(), "vreceiveaddress", bEditUserDef);
                getBillModel().setRowState(e.getRow(), BillModel.MODIFICATION);
            }
        }
    }

    /**
     * 作者：李亮 功能：修改表头税率后动作 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-06-07 wyf 修改行状态，以便保存时更新
     * 2002-10-21 wyf 修改数量等的关系计算，调用公用方法
     */
    private void afterEditWhenHeadTaxRate(BillEditEvent e) {
        
        //关闭合计开关
    	boolean		bOldNeedCalc = getBillModel().isNeedCalculate() ; 
    	getBillModel().setNeedCalculate(false) ;
    	
        if ((e.getValue() != null)
                && (!(e.getValue().toString().trim().equals("")))) {
            UFDouble ntaxrate = new UFDouble(e.getValue().toString().trim());
            if (ntaxrate.doubleValue() < 0) {
                MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000025")/*@res "税率不能小于0"*/);
                getHeadItem("ntaxrate").setValue(null);
                return;
            }
        }
//        HashMap mapRateMny = m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel()));
//        BusinessCurrencyRateUtil bca = m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());
        
        for (int i = 0; i < getRowCount(); i++) {
            Object o = getBillModel().getValueAt(i, "ntaxrate");
            if ((o == null) || (o.toString().trim().equals(""))) {
                getBillModel().setValueAt(e.getValue(), i, "ntaxrate");

                //计算（数量，单价，含税单价，金额，税率，税额，价税合计,扣率，扣率单价）之间的关系
                BillEditEvent tempe = new BillEditEvent(e.getSource(), e
                        .getValue(), "ntaxrate", i);
                calRelation(tempe);
                getBillModel().setRowState(i, BillModel.MODIFICATION);
            }
        }
        //打开合计开关
        getBillModel().setNeedCalculate(bOldNeedCalc) ;
    }

    /**
     * 作者：李亮 功能：修改表头供应商后的相应变化 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2001-10-20 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-04-25 wyf 修改取价方式 wyf
     * add/modify/delete 2002-04-22 begin/end 2002-05-27 wyf 加入合同相关信息清空的处理
     * 2002-06-07 wyf 修改行状态，以便保存时更新
     */
    private void afterEditWhenHeadVendor(BillEditEvent e) {
        
        //关闭合计开关
    	boolean		bOldNeedCalc = getBillModel().isNeedCalculate() ; 
    	getBillModel().setNeedCalculate(false) ;
    	
    	/* V5 支持从数据库中读取公式执行 
        //供应商
        String formula = "cvendorbaseid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendormangid)";
        execHeadFormula(formula);
        //供应商全称
        formula = "ccustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,cvendorbaseid)";
        execHeadFormula(formula);
        */
        // V5 支持数据库中定义的公式
        execHeadFormulas(getHeadItem("cvendormangid").getEditFormulas());
            	
        //设置散户是否可编辑
        setEnabled_HeadFreeCust(isRevise());
        setEnabled_HeadAccount(isRevise());

        //设置表头默认数据
        setDefaultHeadafterVendor(e);
        //合同
        int iCount = getRowCount();
        String[] saMangId = new String[iCount];
        if (iCount > 0) {
            for (int i = 0; i < iCount; i++) {
                saMangId[i] = (String)getBodyValueAt(i,"cmangid");
                getBillModel().setValueAt(null, i, "ccontractid");
                getBillModel().setValueAt(null, i, "ccontractrowid");
                getBillModel().setValueAt(null, i, "ccontractcode");
                getBillModel().setRowState(i, BillModel.MODIFICATION);
            }
            setRelated_Cnt(0, iCount - 1, false, false);
        }
        //供应商收发货地址、地区、地点
        String sRevBaseId = getHeadItem("cvendorbaseid").getValue();

        if(getRowCount() > 0){
        	setDefaultValueToBodyVendorAddr(sRevBaseId,0,getRowCount()-1);
        }
        
        //显示供应商的存货编码及名称
        String strVendorId = getHeadItem("cvendormangid").getValue();        
        String[][] saCodeNames = PuTool.getVendorInvInfos(strVendorId,saMangId);
        if(saCodeNames != null && saCodeNames.length == saMangId.length){
            for (int i = 0; i < iCount; i++) {
                setBodyValueAt(saCodeNames[i][0], i, "vvendinventorycode");
                setBodyValueAt(saCodeNames[i][1], i, "vvendinventoryname");
            }
        }else{
            for (int i = 0; i < iCount; i++) {
                setBodyValueAt(null, i, "vvendinventorycode");
                setBodyValueAt(null, i, "vvendinventoryname");
            }
        }
        //打开合计开关
        getBillModel().setNeedCalculate(bOldNeedCalc) ;
        
    }
    /**
     * 根据客商基本档案ID关联的默认地址信息设置表体供应商的地址、地区、地点
     * */
	private void setDefaultValueToBodyVendorAddr(String sRevBaseId, int iBeginRow,int iEndRow){

	    Object[][] oa2Ret = null;
	    try {
	        oa2Ret = PubHelper.queryResultsFromAnyTable("bd_custaddr",
	                new String[] { "addrname","pk_custaddr" }, "pk_cubasdoc='"
	                        + sRevBaseId
	                        + "' AND ISNULL(defaddrflag,'N')='Y'");
	    } catch (Exception ex) {
	    }
	    if(oa2Ret == null || oa2Ret.length == 0){
	    	SCMEnv.out("无默认地址、地区定义，直接返回!");/*-=notranslate=-*/
	    	return ;
	    }
	    String sAddr = null;
	    String sAddrId = null;

	    if (oa2Ret != null && oa2Ret.length == 1 && oa2Ret[0] != null) {
	    	sAddr = PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
	    	sAddrId = PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][1]);
	    }	    
	    Object[][] objs = CacheTool.getMultiColValue("bd_custaddr",
	            "pk_custaddr", new String[] { "pk_areacl", "pk_address" },
	            new String[] { sAddrId });
	    String strAreaId = null,strAreaName = null,strAddrId = null,strAddrName = null;
	    if (objs != null && objs.length == 1 && objs[0] != null) {
	        strAreaId = PuPubVO.getString_TrimZeroLenAsNull(objs[0][0]);
	        strAddrId = PuPubVO.getString_TrimZeroLenAsNull(objs[0][1]);
	        if(strAreaId != null){
		    	oa2Ret = CacheTool.getMultiColValue("bd_areacl","pk_areacl", new String[] { "areaclname" },new String[] { strAreaId });
			    if (oa2Ret != null && oa2Ret.length == 1 && oa2Ret[0] != null) {
			    	strAreaName = PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
			    }
	        }
	        if(strAddrId != null){
		    	oa2Ret = CacheTool.getMultiColValue("bd_address","pk_address", new String[] { "addrname" },new String[] { strAddrId });
		    	if (oa2Ret != null && oa2Ret.length == 1 && oa2Ret[0] != null) {
		    		strAddrName = PuPubVO.getString_TrimZeroLenAsNull(oa2Ret[0][0]);
			    }
	        }
	    }
		for (int i = iBeginRow; i <= iEndRow; i++) {
	        if (sAddr == null) {
	            getBillModel().setValueAt("", i, "vvenddevaddr");
	        }else{
	            getBillModel().setValueAt(sAddr, i, "vvenddevaddr");
	        }
	        getBillModel().setValueAt(strAddrId, i, "cvenddevaddrid");
	        getBillModel().setValueAt(strAreaId, i, "cvenddevareaid");
	        getBillModel().setValueAt(strAddrName, i, "cvenddevaddrname");
	        getBillModel().setValueAt(strAreaName, i, "cvenddevareaname");
	        getBillModel().setRowState(i, BillModel.MODIFICATION);
	    }
	}
    /**
     * 作者：晁志平 功能：表头表尾编辑前处理 参数：BillItemEvent e 捕捉到的BillItemEvent事件 返回：无 例外：无
     * 日期：(2002-7-22 11:39:21)
     * 修改日期，修改人，修改原因，注释标志
     */
    public boolean beforeEdit(BillItemEvent e) {
        //表头
        /*
        if (e.getSource().equals(getHeadItem("ctransmodeid"))) {
            //发运方式：在“是否发运”钩选时有效
            if ("false".equalsIgnoreCase(getHeadItem("bdeliver").getValue())){
                getHeadItem("ctransmodeid").setValue(null);
                getHeadItem("ctransmodeid").setEnabled(false);
            }else if ("true".equalsIgnoreCase(getHeadItem("bdeliver").getValue())){
                getHeadItem("ctransmodeid").setEnabled(true);
            }
        }else */
		//限制为仓储属性库存组织(20050310-注意：目前此处参照已经启动showmodal())
    	/* V5 Del:
		if (e.getSource().equals(getHeadItem("cstoreorganization"))){
			PuTool.restrictStoreOrg(getHeadItem("cstoreorganization").getComponent(),true);
		}
		*/
        return true;
    }
    /**
     * 作者：王印芬 功能：编辑前处理 参数：ActionEvent e 捕捉到的ActionEvent事件 返回：无 例外：无
     * 日期：(2002-7-22 11:39:21) 修改日期，修改人，修改原因，注释标志： 2003-10-08 wyf 加入对自动增行的处理
     */
    public boolean beforeEdit(BillEditEvent e) {
        if (e.getPos() == BillItem.BODY) {
            //校正扣税类别读到索引与界面显示不一致
            if(!e.getKey().equals("idiscounttaxtype")){
                initDisBeforeEdit(e.getRow());
            }
            
            //V31SP1
            if(PuPubVO.getString_TrimZeroLenAsNull(getBillModel().getValueAt(e.getRow(),"ntaxrate")) == null){
                getBillModel().setValueAt(new UFDouble(0.0),e.getRow(),"ntaxrate");
            }
            //V31标准化模板调整-累计执行数量、本币相关金额、辅币相关金额
            if(OrderPubVO.isNaccNumKey(e.getKey())
                    ||OrderPubVO.isLocalMnyKey(e.getKey())
                    ||OrderPubVO.isAssistMnyKey(e.getKey())){
                return false;
            }
            //
            if (getIContainer() != null
                    && getIContainer().getOperState() == IPoCardPanel.OPER_STATE_EDIT) {
                //设置单据的可编辑性
                int iRow = e.getRow();
                //设置可编辑性
                setEnabled_Body(iRow);
                //对修订的补充设定:如果订单行有后续生成、关闭行，则固定项目设置为不可以修改
                if (isRevise()) {
                    setReviseEnable_Body(iRow);
                }
                //不可编辑则不再向下进行
                String sKey = e.getKey();
                if (!getBillModel().isCellEditable(iRow, getBodyColByKey(sKey))) {
                    return true;
                }

                //存货
                if (sKey.equals("cinventorycode")) {
                    beforeEditBodyInventory(e);
                }
                //合同
                else if (sKey.equals("ccontractcode")) {
                	try{
	                    //设置公司ID、供应商ID、存货ID、日期设置合同号参照
	                    UIRefPane pane = ((UIRefPane) getBodyItem("ccontractcode").getComponent());
	                    nc.itf.ct.ref.IValiCtRefModel ctRefModel = (nc.itf.ct.ref.IValiCtRefModel) pane.getRefModel();
	                    ctRefModel.setWhereParm(getHeadItem("pk_corp").getValue(), 
	                    		getHeadItem("cvendorbaseid").getValue(), 
	                    		(String) getBodyValueAt(iRow,"cbaseid"), 
	                    		new UFDate(getHeadItem("dorderdate").getValue()));
	                    ((AbstractRefModel) ctRefModel).reloadData();
	                    /*支持采购单产品安装调整
						  pane.setRefModel(new nc.ui.ct.ref.ValiCtRefModel(getHeadItem("pk_corp")
	                            .getValue(), getHeadItem("cvendorbaseid")
	                            .getValue(), (String) getBodyValueAt(iRow,
	                            "cbaseid"), new UFDate(getHeadItem("dorderdate")
	                            .getValue())));
	                    */
                	}catch(Exception ee){
                		SCMEnv.out(ee);
                	}
                //表体自由项
                } else if (sKey.equals("vfree")) {
                    return PuTool
                            .beforeEditInvBillBodyFree(this, e,
                                    new String[] { "cmangid", "cinventorycode",
                                            "cinventoryname", "cspecification",
                                            "ctype" }, new String[] { "vfree",
                                            "vfree1", "vfree2", "vfree3",
                                            "vfree4", "vfree5" });
                }
                //表体备注
                else if (sKey.equals("vmemo")) {
                    PuTool.beforeEditBillBodyMemo(this, e);
                }
                //表体收发货地址
                else if (sKey.equals("vreceiveaddress")) {
                    //
                    stopEditing();

                    Object ob = getBodyValueAt(e.getRow(), "vreceiveaddress");
                    if (ob == null) {
                        ((UIRefPane) getBodyItem("vreceiveaddress")
                                .getComponent()).setText("");
                    } else {
                        ((UIRefPane) getBodyItem("vreceiveaddress")
                                .getComponent()).setText((String) ob);
                    }
                    //如表头的收货方为空，则默认为收货仓库的地址，可编辑
                    setRefPane_Body("vreceiveaddress");
                }
                //表体供应商收发货地址
                else if (sKey.equals("vvenddevaddr")) {
                    //
                    stopEditing();

                    Object ob = getBodyValueAt(e.getRow(), "vvenddevaddr");
                    if (ob == null) {
                        ((UIRefPane) getBodyItem("vvenddevaddr")
                                .getComponent()).setText("");
                    } else {
                        ((UIRefPane) getBodyItem("vvenddevaddr")
                                .getComponent()).setText((String) ob);
                    }
                    //如表头的收货方为空，则默认为收货仓库的地址，可编辑
                    setRefPane_Body("vvenddevaddr");
                }
                //批次号
                else if (sKey.equals("vproducenum")) {
                    beforeEditBodyProduceNum(e);
                }
                //辅单位
                else if (sKey.equals("cassistunitname")) {
                    PoEditTool.beforeEditBodyAssistUnit(this, e, "cbaseid",
                            "cassistunit");
                }
                //项目阶段
                else if (sKey.equals("cprojectphase")) {
                    //停止编辑!!!
                    stopEditing();
                    String sPK = (String) getBodyValueAt(iRow,
                            "cprojectphaseid");
                    //项目ID
                    String sProjectId = (String) getBodyValueAt(iRow,
                            "cprojectid");
                    //项目阶段参照
                    UIRefPane refPane = (UIRefPane) getBodyItem("cprojectphase")
                            .getComponent();
                    PuProjectPhaseRefModel refmodelJobPhase = (PuProjectPhaseRefModel) (refPane)
                            .getRefModel();
                    refmodelJobPhase.setProjectid(sProjectId);

                    setBodyValueAt(sPK, iRow, "cprojectphaseid");
                    refPane.setPK(sPK);
                }
                //折本
                else if (sKey.equals("nexchangeotobrate")) {
                    //必须调用!!!!!!!!
                    stopEditing();
                    //
                    String sCurrId = (String) getBodyValueAt(iRow,
                            "ccurrencytypeid");
                    getBodyItem("nexchangeotobrate")
                            .setDecimalDigits(
                                    m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),
                                            sCurrId)[0]);
                }//折辅
                else if (sKey.equals("nexchangeotoarate")) {
                    stopEditing();
                    String sCurrId = (String) getBodyValueAt(iRow,
                            "ccurrencytypeid");
                    getBodyItem("nexchangeotoarate")
                            .setDecimalDigits(
                                    m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),
                                            sCurrId)[1]);
                }//金额
                else if (sKey.equals("noriginalcurmny")) {
                    stopEditing();
                    String sCurrId = (String) getBodyValueAt(iRow,
                            "ccurrencytypeid");
                    getBodyItem("noriginalcurmny").setDecimalDigits(
                            m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(sCurrId));
                }//税额
                else if (sKey.equals("noriginaltaxmny")) {
                    stopEditing();
                    String sCurrId = (String) getBodyValueAt(iRow,
                            "ccurrencytypeid");
                    getBodyItem("noriginaltaxmny").setDecimalDigits(
                            m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(sCurrId));
                }//价税合计
                else if (sKey.equals("noriginaltaxpricemny")) {
                    stopEditing();
                    String sCurrId = (String) getBodyValueAt(iRow,
                            "ccurrencytypeid");
                    getBodyItem("noriginaltaxpricemny").setDecimalDigits(
                            m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(sCurrId));
                }//是否赠品
                else if (sKey.equals("blargess")) {
                    return beforeEditBodyBlargess(e);
                }
                
                //----V5新增：
                
                //优质优价方案
                else if(sKey.equals("cqpbaseschemename")){
                	return beforeEditBodyCQP(e);
                }
                //收货公司
                else if(sKey.equals("arrvcorpname")){
                	return beforeEditBodyArrCorp(e);
                }
                //收货库存组织
                else if(sKey.equals("arrvstoorgname")){
                	return beforeEditBodyArrStoOrg(e);
                }
                //收货仓库
                else if(sKey.equals("cwarehouse")){
                	return beforeEditBodyArrWare(e);
                }
                //收票公司
                else if(sKey.equals("invoicecorpname")){
                	return beforeEditBodyInvoiceCorp(e);
                }
                //需求公司
                else if(sKey.equals("reqcorpname")){
                	return beforeEditBodyReqCorp(e);
                }
                //需求库存组织
                else if(sKey.equals("reqstoorgname")){
                	return beforeEditBodyReqStoOrg(e);
                }
                //需求仓库
                else if(sKey.equals("reqwarename")){
                	return beforeEditBodyReqWare(e);
                }
                //自由项
                else if(sKey.startsWith("vfree")){
                    PoEditTool.setCellEditable_Vfree(getBillModel(), iRow, "cmangid","vfree");
                }
            }
        }
        return true;
    }
    /**
     * <p>获取当前明确的公司集合：返回形式如('1001','1002')
     * @param iCurrCorp 0登录公司、1需求公司、2收货公司、3收票公司
     * @param isLimittedBySelfCorp	是否受本公司限制,暂时未启用
     * @return 公司主键集合，返回值算法描述(基于需求：不支持第三方收货)如下，
     * <p> i.如果不同公司只有一个或三个(含以上)时，返回null；
     * <p>ii.如果不同公司有二个时返回形式如('1001','1002')的字符串
     * @author czp
     * @date 2006-03-08
     */
    public static String getCorpSet(BillCardPanel card,int iCurrCorp,int iRowPos, boolean isLimittedBySelfCorp){
    	ArrayList listCorp = new ArrayList();
    	String strLoginCorp = PoPublicUIClass.getLoginPk_corp();
    	listCorp.add(strLoginCorp);
    	String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(card.getBodyValueAt(iRowPos,"pk_reqcorp"));
    	String strArrCorp = PuPubVO.getString_TrimZeroLenAsNull(card.getBodyValueAt(iRowPos,"pk_arrvcorp"));
    	String strInvoiceCorp = PuPubVO.getString_TrimZeroLenAsNull(card.getBodyValueAt(iRowPos,"pk_invoicecorp"));
    	switch (iCurrCorp) {
		case 1://需求公司
			if(isLimittedBySelfCorp && strReqCorp != null && !listCorp.contains(strReqCorp)){
				listCorp.add(strReqCorp);
			}
			if(strArrCorp != null && !listCorp.contains(strArrCorp)){
				listCorp.add(strArrCorp);
			}
			if(strInvoiceCorp != null && !listCorp.contains(strInvoiceCorp)){
				listCorp.add(strInvoiceCorp);
			}
			break;
		case 2://收货公司
			if(strReqCorp != null && !listCorp.contains(strReqCorp)){
				listCorp.add(strReqCorp);
			}
			if(isLimittedBySelfCorp && strArrCorp != null && !listCorp.contains(strArrCorp)){
				listCorp.add(strArrCorp);
			}
			if(strInvoiceCorp != null && !listCorp.contains(strInvoiceCorp)){
				listCorp.add(strInvoiceCorp);
			}
			break;
		case 3://收票公司
			if(strReqCorp != null && !listCorp.contains(strReqCorp)){
				listCorp.add(strReqCorp);
			}
			if(strArrCorp != null && !listCorp.contains(strArrCorp)){
				listCorp.add(strArrCorp);
			}
			if(isLimittedBySelfCorp && strInvoiceCorp != null && !listCorp.contains(strInvoiceCorp)){
				listCorp.add(strInvoiceCorp);
			}
			//为保证：“当“收货公司＝登录公司”时，收票公司只能为登录公司”，即V5不支持“集中采购，集中收货，分散结算”模式
			if(strLoginCorp.equals(strArrCorp)){
				listCorp = new ArrayList();
				listCorp.add(strLoginCorp);
				listCorp.add(strLoginCorp);
			}
			break;

		default:
			break;
		}
    	if(listCorp.size() == 2){
    		return " ('"+listCorp.get(0)+"','"+listCorp.get(1)+"') ";
    	}
    	return null;
    }
    /**
	 * 编辑前事件：需求仓库
	 *   i.	如果业务类型为公司业务类型，不可编辑
	 *  ii.	如果上层来源单据ID非空，则不可编辑
	 * iii.	如果需求公司无值，不可编辑
	 *  iv.	如果需求公司有值、需求库存组织无值，则参照内容为需求公司下所有库存组织下的仓库档案
	 *   v.	如果需求公司、需求库存组织有值，则参照内容为需求库存组织下的仓库档案
	 */
	private boolean beforeEditBodyReqWare(BillEditEvent e) {
		String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"pk_reqcorp"));
		if(strReqCorp == null){
			SCMEnv.out("采购订单行需求公司未录入,表体“需求库存组织”项目不可编辑");
			return false;
		}
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"cupsourcebillid"));
		if (strUpSrcBillId != null) {
			SCMEnv.out("采购订单行是有来源的单据行,表体“需求仓库”项目不可编辑");
			return false;
		}
		//如果是公司业务类型，则不可编辑(原因，需求公司专为集中采购设计)
		String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
				"cbiztype").getValue());
		if (strBusiType == null) {
			SCMEnv.out("未获取业务类型主键，“需求仓库”项目不可编辑");
			return false;
		}
		String strReqStoOrg = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(), "pk_reqstoorg"));		
		PuTool.restrictWarehouseRefByStoreOrg(this,strReqCorp,strReqStoOrg,"reqwarename");
		return true;
	}
    /**
	 * 编辑前事件：需求库存组织
	 *   i.	如果需求公司空，不可编辑
	 *  ii.	如果上层来源单据ID非空，不可编辑
	 * iii.	如果业务类型为公司业务类型(原因，需求公司专为集中采购设计)，不可编辑
	 *  iv.	如果需求公司已经有值，则设置参照内容为需求公司下的所有需求库存组织
	 *   v.	如果需求公司没有值，<1>收货公司、收票公司均无值 或 有值但与登录公司一致，设置参照为全集团库存组织档案；<2>其它情况，在如下范围{登录公司、收货公司、收票公司}
	 */
	private boolean beforeEditBodyReqStoOrg(BillEditEvent e) {
		String strReqCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"pk_reqcorp"));
		if(strReqCorp == null){
			SCMEnv.out("采购订单行需求公司未录入,表体“需求库存组织”项目不可编辑");
			return false;
		}
		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"cupsourcebillid"));
		if (strUpSrcBillId != null) {
			SCMEnv.out("采购订单行是有来源的单据行,表体“需求库存组织”项目不可编辑");
			return false;
		}
		//如果是公司业务类型，则不可编辑(原因，需求公司专为集中采购设计)
		String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
				"cbiztype").getValue());
		if (strBusiType == null) {
			SCMEnv.out("未获取业务类型主键，“需求库存组织”项目不可编辑");
			return false;
		}
		UIRefPane paneReqStoOrg = ((UIRefPane) getBodyItem("reqstoorgname")
				.getComponent());
		AbstractRefModel rmArrStoOrg = paneReqStoOrg.getRefModel();
		rmArrStoOrg.setSealedDataShow(false);//不显示封存数据，参见AbstractRefModel::addSealCondition(whereClause);
		rmArrStoOrg.setWherePart(" bd_calbody.pk_corp = '" + strReqCorp + "' ");
		return true;
	}
    /**
	 * <p>编辑前事件：需求公司
	 * <p>  i.单据有来源时(如果上层来源单据ID非空)，不可编辑
	 * <p> ii.如果是公司业务类型(原因，需求公司专为集中采购设计)，不可编辑
	 * <p>iii.如果业务类型是集团业务类型，<1>收货公司、收票公司均无值 或 有值但与登录公司一致，设置参照为集团公司档案；<2>其它情况，在如下范围{登录公司、收货公司、收票公司}
	 */
	private boolean beforeEditBodyReqCorp(BillEditEvent e) {

		String strUpSrcBillId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"cupsourcebillid"));
		if (strUpSrcBillId != null) {
			SCMEnv.out("采购订单行是有来源的单据行,表体“需求公司”项目不可编辑");
			return false;
		}
		String strRpFlag = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"breceiveplan"));
		if (PuPubVO.getUFBoolean_NullAs(strRpFlag,UFBoolean.FALSE).booleanValue()) {
			SCMEnv.out("采购订单行已经有到货计划行,表体“需求公司”项目不可编辑");
			return false;
		}	
		//如果是公司业务类型，则不可编辑(原因，需求公司专为集中采购设计)
		String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
				"cbiztype").getValue());
		if (strBusiType == null) {
			SCMEnv.out("未获取业务类型主键，“需求公司”项目不可编辑");
			return false;
		}
		boolean isCentrPur = false;
		try {
			isCentrPur = CentrPurchaseUtil.isGroupBusiType(strBusiType);
		} catch (Exception ex) {
			SCMEnv.out("调用是否集团业务类型工具方法时出错：" + ex.getMessage());
		}
		UIRefPane paneArrCorp = ((UIRefPane) getBodyItem("reqcorpname")
				.getComponent());
		paneArrCorp.setRefNodeName("权限公司目录");
		AbstractRefModel rmArrCorp = paneArrCorp.getRefModel();
		if (!isCentrPur) {
			rmArrCorp.addWherePart("and bd_corp.pk_corp = '" + PoPublicUIClass.getLoginPk_corp() + "' ");
			return true;
		}
		String strCorpSet = getCorpSet(this,1,e.getRow(),false);
		if (strCorpSet != null) {
			rmArrCorp.addWherePart("and bd_corp.pk_corp in " + strCorpSet + " ");
		}
		return true;
	}
    /**
	 * 编辑前事件：收票公司
	 * <p> i.如果业务类型为公司业务类型，不可编辑
	 * <p>ii.如果业务类型为集团业务类型，<1>需求公司、收货公司均无值 或 有值但与登录公司一致，设置参照为集团公司档案；<2>其它情况，在如下范围{登录公司、需求公司、收货公司}
	 * @author czp
	 * @date   2006-03-08
	 */
	private boolean beforeEditBodyInvoiceCorp(BillEditEvent e) {

		// 如果是公司业务类型，则不可编辑
		String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
				"cbiztype").getValue());
		if (strBusiType == null) {
			SCMEnv.out("未获取业务类型主键，“发票公司”项目不可编辑");
			return false;
		}
		boolean isCentrPur = false;
		try {
			isCentrPur = CentrPurchaseUtil.isGroupBusiType(strBusiType);
		} catch (Exception ex) {
			SCMEnv.out("调用是否集团业务类型工具方法时出错：" + ex.getMessage());
		}
		if (!isCentrPur) {
			SCMEnv.out("不是集团业务类型,表体“发票公司”项目不可编辑");
			return false;
		}
        String sHId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"corderid"));
		String sBId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"corder_bid"));
		if(sHId != null && sBId != null){
			if(!NO_AFTERBILL.equals(h_mapAfterBill.get(sHId))){	
				OrderAfterBillVO voAfter = (OrderAfterBillVO) h_mapAfterBill.get(sHId);		
				boolean bInvoice = false;
		        if(voAfter != null){
		        	bInvoice = voAfter.isHaveAfterBill(sBId,OrderstatusVO.STATUS_INVOICE);
		        }
				//补充逻辑：如果本行有累计发票数量不为零，也认为生成了发票
				UFDouble ufdNaccInvoiceNum = PuPubVO.getUFDouble_NullAsZero(getBodyValueAt(e.getRow(),"naccuminvoicenum"));
				bInvoice = bInvoice || ufdNaccInvoiceNum.doubleValue() != 0.0;
				//订单行有后续发票生成，则不可以修改
				if(bInvoice){
		        	SCMEnv.out("订单行有后续发票生成，不能再修改收票公司");/*-=notranslate=-*/
					return false;
				}
				//订单行入库且已经暂估或生成结算单，不能再修改收票公司
		        boolean bPs = false;
		        if(voAfter != null){
		        	bPs = voAfter.isHaveAfterBill(sBId,OrderstatusVO.STATUS_SETTLE);
		        }
		        if(bPs){
		        	SCMEnv.out("订单行入库且已经暂估或生成结算单，不能再修改收票公司");/*-=notranslate=-*/
		        	return false;
		        }
			}
		}
		/*
		 * 集团业务类型：重新设置参照:
		 */
		UIRefPane paneInvoiceCorp = ((UIRefPane) getBodyItem("invoicecorpname").getComponent());
		paneInvoiceCorp.setRefNodeName("公司目录");
		AbstractRefModel rmInvoiceCorp = paneInvoiceCorp.getRefModel();
		String strCorpSet = getCorpSet(this,3,e.getRow(),false);
		if (strCorpSet != null) {
			rmInvoiceCorp.addWherePart("and bd_corp.pk_corp in " + strCorpSet+ " ");
		}
		return true;
	}
    /**
	 * <p>
	 * 编辑前事件：收货仓库
	 * <p>
	 *   i.如果收货公司无值，不可编辑
	 * <p>
	 *  ii.如果收货公司有值、收货库存组织无值，则参照收货公司的所有仓库档案
	 * <p>
	 * iii.如果收货公司、收货库存组织均有值，则参照收货库存组织下的所有仓库档案
	 */
	private boolean beforeEditBodyArrWare(BillEditEvent e) {
		String strArrCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"pk_arrvcorp"));
		if(strArrCorp == null){
			return false;
		}
		String strArrStoOrg = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
				e.getRow(), "pk_arrvstoorg"));
		PuTool.restrictWarehouseRefByStoreOrg(this,strArrCorp,strArrStoOrg,"cwarehouse");
		return true;
	}
    /**
	 * <p>
	 * 编辑前事件：收货库存组织
	 * <p>
	 *   i.收货公司未录入，不可编辑
	 * <p>
	 *  ii.设置参照内容为收货公司下的库存组织档案
	 */
	private boolean beforeEditBodyArrStoOrg(BillEditEvent e) {
		String strArrCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"pk_arrvcorp"));
		if(strArrCorp == null){
			return false;
		}
		UIRefPane paneArrStoOrg = ((UIRefPane) getBodyItem("arrvstoorgname")
				.getComponent());
		AbstractRefModel rmArrStoOrg = paneArrStoOrg.getRefModel();
		rmArrStoOrg.setSealedDataShow(false);//不显示封存数据，参见AbstractRefModel::addSealCondition(whereClause);
		rmArrStoOrg.setPk_corp(strArrCorp);
		rmArrStoOrg.setWherePart(" bd_calbody.pk_corp = '" + strArrCorp + "' ");
		return true;
	}
    /**
	 * <p>编辑前事件：收货公司
	 *  i.如果业务类型是公司业务类型，不可编辑
	 * ii.如果业务类型是集团业务类型，<1>需求公司、收票公司均无值 或 有值但与登录公司一致，设置参照为集团公司档案；<2>其它情况，在如下范围{登录公司、需求公司、收票公司}
	 */
	private boolean beforeEditBodyArrCorp(BillEditEvent e) {

		// 如果是公司业务类型，则不可编辑
		String strBusiType = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
				"cbiztype").getValue());
		if (strBusiType == null) {
			SCMEnv.out("未获取业务类型主键，“收货公司”项目不可编辑");
			return false;
		}
		boolean isCentrPur = false;
		try {
			isCentrPur = CentrPurchaseUtil.isGroupBusiType(strBusiType);
		} catch (Exception ex) {
			SCMEnv.out("调用是否集团业务类型工具方法时出错：" + ex.getMessage());
		}
		if (!isCentrPur) {
			SCMEnv.out("不是集团业务类型,表体“收货公司”项目不可编辑");
			return false;
		}
		/*
		 * 集团业务类型：重新设置参照:
		 */
		UIRefPane paneArrCorp = ((UIRefPane) getBodyItem("arrvcorpname")
				.getComponent());
		paneArrCorp.setRefNodeName("公司目录");
		AbstractRefModel rmArrCorp = paneArrCorp.getRefModel();
		String strCorpSet = getCorpSet(this,2,e.getRow(),false);
		if (strCorpSet != null) {
			rmArrCorp.addWherePart("and bd_corp.pk_corp in " + strCorpSet + " ");
		}
		return true;
	}
	/**
	 * 编辑前事件：优质优价方案<br>
	 * <p>
	 * 可编辑，要求同时满足，<br>
	 * <p>
	 * 1)、存货录入<br>
	 * 2)、收货公司非空<br>
	 * 3)、收货公司=采购公司<br>
	 */
	private boolean beforeEditBodyCQP(BillEditEvent e) {

		// 优质优价方案参照模型：nc.ui.scm.hqhp.QPSchmRefModel
		
		// 如果存货、公司为空，则不可编辑
		String strArrvCorp = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
				e.getRow(), "pk_arrvcorp"));
		if (strArrvCorp == null) {
			SCMEnv.out("收货公司未确定，“优质优价方案”项目不可编辑");
			return false;
		}
		String strCbaseid = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(
				e.getRow(), "cbaseid"));
		if (strCbaseid == null) {
			SCMEnv.out("存货未确定，“优质优价方案”项目不可编辑");
			return false;
		}
		if(!strArrvCorp.equals(PoPublicUIClass.getLoginPk_corp())){
			SCMEnv.out("收货公司与采购公司不相同，“优质优价方案”项目不可编辑");
			return false;
		}
		//订单行优质优价方案已经生成价格结算单，不能再修改
		String sHId = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("corderid").getValue());
		if(sHId != null){
			String sBId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(e.getRow(),"corder_bid"));
			if(sBId != null){
		        OrderAfterBillVO voAfter = (OrderAfterBillVO) h_mapAfterBill.get(sHId);
		        boolean bPs = false;
		        if(voAfter != null){
		        	bPs = voAfter.isHaveAfterBill(sBId,OrderstatusVO.STATUS_PS);
		        }
		        if(bPs){
		        	SCMEnv.out("订单行优质优价方案已经生成价格结算单，不能再修改");/*-=notranslate=-*/
		        	return false;
		        }
			}
		}
		// 重新设置参照
		UIRefPane paneBodyCQP = ((UIRefPane) getBodyItem("cqpbaseschemename")
				.getComponent());
		paneBodyCQP.setRefModel(new QPSchmRefModel(strArrvCorp, strCbaseid));
		paneBodyCQP.setButtonVisible(true);
		paneBodyCQP.setReturnCode(false);
		paneBodyCQP.setRefInputType(1);
		//
		return true;
	}
    /**
	 * 作者：晁志平 功能：是否赠品编辑前处理 参数：BillEditEvent e 返回：无 例外：无 日期：(2004-10-29 10:39:21)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
    public boolean beforeEditBodyBlargess(BillEditEvent e) {
        stopEditing();
        //是否生成到货计划
        String sRcPlan = getBodyValueAt(e.getRow(), "breceiveplan").toString();
        if (sRcPlan != null && "Y".equalsIgnoreCase(sRcPlan.trim())) {
            return false;
        }
        return true;
    }

    /**
     * 作者：王印芬 功能：存货编辑前处理 参数：BillEditEvent e 返回：无 例外：无 日期：(2002-7-22 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    public boolean beforeEditBodyInventory(BillEditEvent e) {
        //停止编辑!!!
        stopEditing();

        int iRow = e.getRow();
        String sClassId = null;
        //上层来源为合同
        String sUpSourceType = (String) getBodyValueAt(iRow,
                "cupsourcebilltype");
        if (sUpSourceType != null
                && (sUpSourceType.equals(BillTypeConst.CT_BEFOREDATE) || sUpSourceType
                        .equals(BillTypeConst.CT_ORDINARY))) {
            String sCntRowId = (String) getBodyValueAt(iRow, "ccontractrowid");
            RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(sCntRowId);
            if (voCntInfo != null
                    && voCntInfo.getIinvCtl() == RetCtToPoQueryVO.INVCLASSCTL) {
                //存货大类合同的存货只能在该类中
                sClassId = voCntInfo.getCInvClass();
            }
        }

        //根据是否存货大类合同置参照
        UIRefPane pane = ((UIRefPane) getBodyItem("cinventorycode")
                .getComponent());
        pane.setPk_corp(getCorp());
        PoInputInvRefModel refModel = new PoInputInvRefModel(getCorp(),
                sClassId);
        
        try{
		    boolean checker = false;
		    Object[] oaTemp = (Object[]) CacheTool.getCellValue("bd_busitype","pk_busitype","verifyrule",m_strCbiztype);
			if(oaTemp != null && oaTemp.length > 0 && oaTemp[0] != null){
				checker = "S".equalsIgnoreCase(oaTemp[0].toString().trim());
			}
		    if(checker){
		        refModel.addWherePart(" and (sellproxyflag = 'Y') ");
		    }
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        pane.setRefModel(refModel);

        return true;
    }

    /**
     * 作者：王印芬 功能：批次号编辑前处理 参数：ActionEvent e 捕捉到的ActionEvent事件 返回：无 例外：无
     * 日期：(2002-7-22 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private boolean beforeEditBodyProduceNum(BillEditEvent e) {

        int iRow = e.getRow();
        ParaVOForBatch vo = new ParaVOForBatch();
        //传入FieldName
        vo.setMangIdField("cmangid");
        vo.setInvCodeField("cinventorycode");
        vo.setInvNameField("cinventoryname");
        vo.setSpecificationField("cspecification");
        vo.setInvTypeField("ctype");
        vo.setMainMeasureNameField("cmessureunitname");
        vo.setAssistUnitIDField("cassistunit");
        vo.setIsAstMg(new UFBoolean(PuTool
                .isAssUnitManaged((String) getBodyValueAt(iRow, "cbaseid"))));
        vo.setWarehouseIDField("cwarehouseid");
        vo.setFreePrefix("vfree");
        //设置卡片模板,公司等
        vo.setCardPanel(this);
        vo.setPk_corp(getCorp());
        vo.setEvent(e);
        try {
            nc.ui.pu.pub.PuTool.beforeEditWhenBodyBatch(vo);
        } catch (Exception exp) {
            PuTool.outException(this, exp);
        }

        return true;
    }

    /**
     * 作者：李亮 功能：计算表体行的预计到货日期 参数：int iCurRow 表体行 Object oReason 导致计划到货日期需重新计算的原因，
     * 为：订单日期、存货、库存组织 之值 返回：无 例外：无 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     * 2002-04-22 wyf wyf add/modify/delete 2002-03-21 begin/end 2003-01-20 wyf
     * 修改为：如果修改计划到货日期的原因为空，不修改原值 如果未得到采购提前期，则不再重新设置计划到货日期 2003-02-26 wyf
     * 修改一个类转换错误
     */
    private void calDplanarrvdate(int iBeginRow, int iEndRow) {

        //订单日期
        String sOrderDate = getHeadItem("dorderdate").getValue();
        if (PuPubVO.getUFDate(sOrderDate) == null) {
            return;
        }
        /* V5 Del:
        String sStoreId = getHeadItem("cstoreorganization").getValue();
        if (PuPubVO.getString_TrimZeroLenAsNull(sStoreId) == null) {
            return;
        }
        */
        //预计到货日期
        UFDate dOrderDate = new UFDate(sOrderDate.trim());
        Vector vecRow = new Vector();
        Vector vecBaseId = new Vector();
        Vector vecStorId = new Vector();
        Vector vecOrderNum = new Vector();
        for (int i = iBeginRow; i <= iEndRow; i++) {
            //如果原来有值，则不改变
            Object oPlanArrvDate = getBodyValueAt(i, "dplanarrvdate");
            if (PuPubVO.getString_TrimZeroLenAsNull(oPlanArrvDate) != null) {
                continue;
            }
            //基本ID
            String sBaseId = PuPubVO
                    .getString_TrimZeroLenAsNull((String) getBodyValueAt(i,
                            "cbaseid"));
            if (sBaseId == null) {
                continue;
            }
            String sStoreId = PuPubVO
            .getString_TrimZeroLenAsNull((String) getBodyValueAt(i,
            "pk_arrvstoorg"));
            if (sStoreId == null) {
                continue;
            }
            //数量不为0
            UFDouble dOrderNum = PuPubVO.getUFDouble_ZeroAsNull(getBodyValueAt(
                    i, "nordernum"));
            if (dOrderNum == null) {
                continue;
            }
            vecRow.add(new Integer(i));
            vecBaseId.add(sBaseId);
            vecOrderNum.add(dOrderNum);
            vecStorId.add(sStoreId);
        }

        int iReSetLen = vecBaseId.size();
        if (iReSetLen == 0) {
            return;
        }

        //计算预计到货日期
        UFDate[] daPlanDate = PoPublicUIClass.getDPlanArrvDateArray(
        		getCorp(),
        		(String[]) vecStorId.toArray(new String[iReSetLen]),
        		dOrderDate, 
        		(String[]) vecBaseId.toArray(new String[iReSetLen]),
                (UFDouble[]) vecOrderNum.toArray(new UFDouble[iReSetLen]));

        //设置新值
        for (int i = 0; i < iReSetLen; i++) {
            setBodyValueAt(daPlanDate[i], ((Integer) vecRow.get(i)).intValue(),
                    "dplanarrvdate");
        }
    }

    /**
     * 作者：李亮 功能：计算表体行的数量关系 参数：BillEditEvent e 捕捉到的BillEditEvent事件 返回：无 例外：无
     * 日期：(2001-10-20 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-16 wyf
     * 加入控制数量、价格优先的方式 2002-10-21 wyf 加入对含税单价的计算
     */
    private void calRelation(BillEditEvent e) {

        int iRow = e.getRow();
        //设置精度
        setRowDigits_Mny(getCorp(), iRow,getBillModel(),m_cardPoPubSetUI2);
        //计算
        int[] iaDescription = { RelationsCal.DISCOUNT_TAX_TYPE_NAME,
                RelationsCal.DISCOUNT_TAX_TYPE_KEY, RelationsCal.TAXRATE,
                RelationsCal.MONEY_ORIGINAL, RelationsCal.TAX_ORIGINAL,
                RelationsCal.SUMMNY_ORIGINAL, RelationsCal.NUM,
                RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.PRICE_ORIGINAL,

                RelationsCal.NET_TAXPRICE_ORIGINAL,//净含税单价
                RelationsCal.TAXPRICE_ORIGINAL,//含税单价

                RelationsCal.DISCOUNT_RATE, RelationsCal.CONVERT_RATE,
                RelationsCal.NUM_ASSIST, RelationsCal.IS_FIXED_CONVERT };

        //是否固定换算率
        String sBaseId = (String) getBillModel().getValueAt(iRow, "cbaseid");
        String sAssistUnit = (String) getBillModel().getValueAt(iRow,
                "cassistunit");
        String sFixFlag = PuTool.isFixedConvertRate(sBaseId, sAssistUnit) ? "Y"
                : "N";
        //获取扣税类别，不翻译
        String strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_INNER_NO_TRANS;//应税内含
        int index = getBillModel().getBodyColByKey("idiscounttaxtype");
        if(index >= 0){
            if(getBillModel().getBodyItems()[getBillModel().getBodyColByKey("idiscounttaxtype")] != null){
                BillItem bi = getBillModel().getBodyItems()[getBillModel().getBodyColByKey("idiscounttaxtype")];
                if(bi.getComponent() instanceof UIComboBox){
                    index = ((UIComboBox)bi.getComponent()).getSelectedIndex();
                    if(index == 0){
                        strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_INNER_NO_TRANS;//应税内含
                    }else if (index == 1){
                        strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER_NO_TRANS;//应税外加
                    }else if (index == 2){
                        strDisType = VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT_NO_TRANS;//不计税
                    }
                }
            }
        }
        String[] saKeys = {
                strDisType,
                "idiscounttaxtype", "ntaxrate", "noriginalcurmny",
                "noriginaltaxmny", "noriginaltaxpricemny", "nordernum",
                "noriginalnetprice", "noriginalcurprice", "norgnettaxprice",
                "norgtaxprice", "ndiscountrate", "nconvertrate", "nassistnum",
                sFixFlag };

        //数量优先
        RelationsCal.calculate(e, this, new int[] { PuTool
                .getPricePriorPolicy(getCorp()) }, iaDescription, saKeys, OrderItemVO.class.getName());
    }

    /**
     * 作者：WYF 功能：清空一个订单VO的后续单据信息 由使用者主动调用该函数，以使与后续单据信息同步。 参数：OrderVO voOrder
     * 订单VO 返回：无 例外：无 日期：(2004-05-25 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    public void clearAfterBillInfo(String sOrderId) {

        if (PuPubVO.getString_TrimZeroLenAsNull(sOrderId) == null) {
            return;
        }
        //清掉
        h_mapAfterBill.remove(sOrderId);

    }

    /**
     * 作者：WYF 功能：实现显示指定位置的采购订单，完全重置显示 参数：OrderVO voShow 订单 返回：无 例外：无
     * 日期：(2001-10-20 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    public void displayCurVO(OrderVO voViewCur, boolean bLoadVendCodeName) {

        nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
        timeDebug.start();

        //上一张单据的公司
        String sOldPk_corp = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
                "pk_corp").getValue());

        //可能是集团采购，涉及多单位，因此要设置参照
        setCorp(voViewCur.getHeadVO().getPk_corp());

        //清除界面原数据后显示新数据
        addNew();
        getBillModel().clearBodyData();

        //设置参照
        if (!voViewCur.getHeadVO().getPk_corp().equals(sOldPk_corp)) {
            setRefPane();
        }
        timeDebug.addExecutePhase("设置参照");/*-=notranslate=-*/

        //设置数量、价格精度
        setHeadDigits(getCorp());
        setBodyDigits_CorpRelated(getCorp(), getBillModel());
        timeDebug.addExecutePhase("设置数量、价格精度");/*-=notranslate=-*/

        //设置VO
        setBillValueVO(voViewCur);
        timeDebug.addExecutePhase("设置VO");/*-=notranslate=-*/
        
        //设置供应商全称
        String formula = "ccustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,cvendorbaseid)";
        execHeadFormula(formula);

        //设置表头快捷录入的显示：取第一行的值
        OrderItemVO voFirstItem = voViewCur.getBodyVO()[0];
        getHeadItem("ccurrencytypeid").setValue(
                voFirstItem.getCcurrencytypeid());
        //设置表头币种精度
        resetHeadCurrDigits();
        getHeadItem("nexchangeotobrate").setValue(voFirstItem.getNexchangeotobrate());
        getHeadItem("nexchangeotoarate").setValue(voFirstItem.getNexchangeotoarate());
        getHeadItem("cprojectid").setValue(voFirstItem.getCprojectid());
        getHeadItem("idiscounttaxtype").setValue(voFirstItem.getIdiscounttaxtype());
        getHeadItem("ntaxrate").setValue(voFirstItem.getNtaxrate());
        //设置表体与币种相关的数值
        try{
        	resetBodyValueRelated_Curr(getCorp(),voFirstItem.getCcurrencytypeid(), getBillModel(),new BusinessCurrencyRateUtil(getCorp()),getRowCount(),m_cardPoPubSetUI2);
        }catch(Exception e){
        	SCMEnv.out(e);
        }
        timeDebug.addExecutePhase("重新设置币种相关的数值");/*-=notranslate=-*/

        //表头备注
        ((UIRefPane) getHeadItem("vmemo").getComponent()).setText(voViewCur
                .getHeadVO().getVmemo());

        //表头供应商收发货地址
        ((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).setText(voViewCur
                .getHeadVO().getCdeliveraddress());
        
        //关闭合计开关
    	boolean		bOldNeedCalc = getBillModel().isNeedCalculate() ; 
    	getBillModel().setNeedCalculate(false) ;
    	
        //表体公式
        getBillModel().execLoadFormula();
        timeDebug.addExecutePhase("表体公式");/*-=notranslate=-*/

        //打开合计开关
    	getBillModel().setNeedCalculate(bOldNeedCalc) ;
    	
        //设置自由项
        PoPublicUIClass.setFreeColValue(getBillModel(), "vfree");
        //计算并设置换算率
        PuTool.setBillModelConvertRate(getBillModel(), new String[] {
                "cbaseid", "cassistunit", "nordernum", "nassistnum",
                "nconvertrate" });
        timeDebug.addExecutePhase("计算并设置换算率");/*-=notranslate=-*/

        //处理来源单据类型、来源单据号
        PuTool.loadSourceInfoAll(this,BillTypeConst.PO_ORDER);
        timeDebug.addExecutePhase("处理来源单据类型、来源单据号");/*-=notranslate=-*/

        //最高限价
        PoPublicUIClass.loadCardMaxPrice(this, getCorp(),m_cardPoPubSetUI2);
        timeDebug.addExecutePhase("最高限价");/*-=notranslate=-*/

        //显示供应商的存货编码及名称
        /*
        String strVendorId = voShow.getHeadVO().getCvendormangid();  
        String[] saMangId = new String[voShow.getBodyVO().length];
        for (int i = 0; i < saMangId.length; i++) {
            saMangId[i] = voShow.getBodyVO()[i].getCmangid();
        }
        PuTool.loadVendorInvInfos(strVendorId,saMangId,getBillModel(),0,saMangId.length -1);
        */
	    //显示供应商的存货编码及名称
		if(bLoadVendCodeName){	
	        String strVendorId = voViewCur.getHeadVO().getCvendormangid();  
	        String[] saMangId = new String[voViewCur.getBodyVO().length];
	        for (int i = 0; i < saMangId.length; i++) {
	            saMangId[i] = voViewCur.getBodyVO()[i].getCmangid();
	        }
	        PuTool.loadVendorInvInfos(strVendorId,saMangId,getBillModel(),0,saMangId.length -1);
		}
        //
        updateValue();
        updateUI();

        timeDebug.showAllExecutePhase("采购订单卡片显示");/*-=notranslate=-*/

    }
    /**
     * 作者：WYF 功能：在刚刚保存完毕后修改此方法显示订单，为提高效率设计
     * 保存前界面必有待保存的订单，此时只需设置一些可能会发生变化的信息，再按行号排序即可。 参数：OrderVO voJustSaved
     * 保存完毕的订单，其中必有头体ID、头体TS、审批人、审批时间 返回：无 例外：无 日期：(2004-06-07 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    public void displayCurVOAfterJustSave(OrderVO voJustSaved) {

        nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
        timeDebug.start();

        if (voJustSaved == null) {
            return;
        }

        //删除无用的行
        int[] iaInvNullRow = PuTool.getRowsWhenAttrNull(getBillModel(),
                OrderItemVO.FILTERKEY_WHENSAVE);
        if (iaInvNullRow != null) {
            getBillModel().delLine(iaInvNullRow);
        }

        //界面行号数组、表体VO数组
        String[] saUIRowNo = (String[]) PuGetUIValueTool.getArray(
                getBillModel(), "crowno", String.class);
        OrderVO voCheck = voJustSaved.getCheckVO();
        OrderItemVO[] voaItem = voCheck.getBodyVOsByRowNos(saUIRowNo);

        int iUILen = getRowCount();
        String sRowNo = null;
        for (int i = 0; i < iUILen; i++) {
            sRowNo = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(i,
                    "crowno"));
            if (sRowNo == null) {
                continue;
            }
            if (voaItem[i] == null) {
                continue;
            }

            //与行号对应的VO
            setBodyValueAt(voaItem[i].getCorderid(), i, "corderid");
            setBodyValueAt(voaItem[i].getCorder_bid(), i, "corder_bid");
            setBodyValueAt(voaItem[i].getTs(), i, "ts");
            setBodyValueAt(voaItem[i].getVproducenum(), i, "vproducenum");
            /*
            setNmoney(lightVo.getNmoney());
            setNtaxmny(lightVo.getNtaxmny());
            setNtaxpricemny(lightVo.getNtaxpricemny());
            setNassistcurmny(lightVo.getNassistcurmny());
            setNassisttaxmny(lightVo.getNassisttaxmny());
            setNassisttaxpricemny(lightVo.getNassisttaxpricemny());
            */
            setBodyValueAt(voaItem[i].getNmoney(), i, "nmoney");
            setBodyValueAt(voaItem[i].getNtaxmny(), i, "ntaxmny");
            setBodyValueAt(voaItem[i].getNtaxpricemny(), i, "ntaxpricemny");
            setBodyValueAt(voaItem[i].getNassistcurmny(), i, "nassistcurmny");
            setBodyValueAt(voaItem[i].getNassisttaxmny(), i, "nassisttaxmny");
            setBodyValueAt(voaItem[i].getNassisttaxpricemny(), i, "nassisttaxpricemny");
            
            //切忘该方法!!!!!
            getBillModel().setRowState(i, BillModel.NORMAL);
        }

        setHeadItem("corderid", voCheck.getHeadVO().getCorderid());
        setHeadItem("vordercode", voCheck.getHeadVO().getVordercode());
        setHeadItem("forderstatus", voCheck.getHeadVO().getForderstatus());
        setHeadItem("ts", voCheck.getHeadVO().getTs());

        setTailItem("cauditpsn", voCheck.getHeadVO().getCauditpsn());
        setTailItem("dauditdate", voCheck.getHeadVO().getDauditdate());
        setTailItem("tmaketime", voCheck.getHeadVO().getTmaketime());
        setTailItem("taudittime", voCheck.getHeadVO().getTaudittime());
        setTailItem("tlastmaketime", voCheck.getHeadVO().getTlastmaketime());
        timeDebug.addExecutePhase("设值");/*-=notranslate=-*/

        //按行号排序
        getBillModel().sortByColumn("crowno", true);
        timeDebug.addExecutePhase("按行号排序");/*-=notranslate=-*/

        updateUI();

        timeDebug.showAllExecutePhase("采购订单卡片显示");/*-=notranslate=-*/

    }
     /**
     * 作者：王印芬 功能：寻找某些行的合同 参数：Integer[] iaRow 需查询信息的行 返回：HashMap KEY: Integer
     * VALUE: RetCtToPoQueryVO 可能返回NULL 例外：无 日期：(2003-10-30 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private HashMap findCntInfoForRowArray(Integer[] iaRow) {

        //参数正确性判断
        if (iaRow == null) {
            return null;
        }

        //公司
        String pk_corp = getHeadItem("pk_corp").getValue();
        //供应商管理ID
        String sVendBaseId = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
                "cvendorbaseid").getValue());
        //日期
        String sDate = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
                "dorderdate").getValue());

        //有供应商及日期时才需重新寻找合同
        if (pk_corp == null || sVendBaseId == null || sDate == null) {
            return null;
        }

        //行总长度
        int iTotalLen = iaRow.length;
        //KEY:行号 VALUE:RetCtToPoQueryVO 合同VO
        HashMap mapRowAndCnt = new HashMap();

        //已有合同行ID的字段
        Vector vecHaveCntIndex = new Vector();
        Vector vecCntRowId = new Vector();
        //需查询合同的行
        Vector vecNoCntIndex = new Vector();
        Vector vecNoCntBaseId = new Vector();

        //临时变量
        int iRow = 0;
        String sBaseId = null;

        //区分已有合同及无合同的行
        for (int i = 0; i < iTotalLen; i++) {
            iRow = iaRow[i].intValue();

            String sCntRowId = (String) getBodyValueAt(iRow, "ccontractrowid");
            if (PuPubVO.getString_TrimZeroLenAsNull(sCntRowId) != null) {
                vecHaveCntIndex.add(iaRow[i]);
                vecCntRowId.add(sCntRowId);
            } else {
                sBaseId = (String) getBodyValueAt(iRow, "cbaseid");
                if (PuPubVO.getString_TrimZeroLenAsNull(sBaseId) != null) {
                    vecNoCntIndex.add(iaRow[i]);
                    vecNoCntBaseId.add(sBaseId);
                }
            }
        }

        //已有合同的行
        int iHaveCntLen = vecHaveCntIndex.size();
        if (iHaveCntLen > 0) {
            RetCtToPoQueryVO[] voaCtInfo = PoPublicUIClass
                    .getCntInfoArray((String[]) vecCntRowId
                            .toArray(new String[iHaveCntLen]));
            for (int i = 0; i < iHaveCntLen; i++) {
                iRow = ((Integer) vecHaveCntIndex.get(i)).intValue();
                if (voaCtInfo == null || voaCtInfo[i] == null) {
                    sBaseId = (String) getBodyValueAt(iRow, "cbaseid");
                    vecNoCntIndex.add(vecHaveCntIndex.get(i));
                    vecNoCntBaseId.add(sBaseId);
                } else {
                    mapRowAndCnt.put(vecHaveCntIndex.get(i),
                            new RetCtToPoQueryVO[] { voaCtInfo[i] });
                }
            }
        }
        //无合同的行
        int iNoCntLen = vecNoCntIndex.size();
        if (iNoCntLen > 0) {
            RetCtToPoQueryVO[][] voa2CntInfo = PoPublicUIClass.getCntInfoList(
                    pk_corp, (String[]) PoPublicUIClass.getSameValueArray(
                            sVendBaseId, iNoCntLen), (String[]) vecNoCntBaseId
                            .toArray(new String[iNoCntLen]), new UFDate(sDate));

            if (voa2CntInfo != null) {
                for (int i = 0; i < iNoCntLen; i++) {
                    if (voa2CntInfo[i] != null) {
                        mapRowAndCnt.put(vecNoCntIndex.get(i), voa2CntInfo[i]);
                    }
                }
            }
        }

        return mapRowAndCnt.size() == 0 ? null : mapRowAndCnt;
    }

    /**
     * 作者：王印芬 功能：得到调用者 参数：无 返回：无 例外：无 日期：(2003-10-09 13:24:16)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private Container getContainer() {
        return m_conInvoker;
    }

    private String[] getFreeCustBank(String cfreecustid) {
        String[] sbanks = null;
        try {
            sbanks = OrderHelper.getFreecustBank(cfreecustid);
        } catch (Exception e) {
            SCMEnv.out(e);
        }

        return sbanks;
    }

    /**
     * 作者：王印芬 功能：得到调用者 参数：无 返回：无 例外：无 日期：(2003-10-09 13:24:16)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private IPoCardPanel getIContainer() {
        if (getContainer() == null || !(getContainer() instanceof IPoCardPanel)) {
            return null;
        }

        return (IPoCardPanel) getContainer();
    }

    /**
     * 作者：WYF 功能：得到所有表头参照ITEMKEY 参数：无 返回：无 例外：无 日期：(2003-10-14 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private String[] getRefItemKeysBody() {
        if (m_saBodyRefItemKey == null) {
            m_saBodyRefItemKey = new String[] { "cinventorycode",
                    "cassistunitname", "ccontractcode", "ccurrencytype",
                    "cusedept", "cproject", "cprojectphase", "cwarehouse",
                    "cdevaddrname", "cdevareaname","coperatorname", "coperator", "vmemo","cvenddevareaname","cvenddevaddrname","vreceiveaddress","vvenddevaddr" };
        }
        return m_saBodyRefItemKey;
    }

    /**
     * 作者：WYF 功能：得到所有表头参照ITEMKEY 参数：无 返回：无 例外：无 日期：(2003-10-14 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private String[] getRefItemKeysHead() {
        if (m_saHeadRefItemKey == null) {
            m_saHeadRefItemKey = new String[] { "ccurrencytypeid", "cunfreeze",
                    "pk_corp", "cbiztype", "cpurorganization",
                    "cstoreorganization", /*"cvendormangid",*/ "caccountbankid",
                    "cdeliveraddress", "cemployeeid", "cdeptid", "creciever",
                    "cgiveinvoicevendor", "ctransmodeid", "cfreecustid",
                    "ctermprotocolid", "cprojectid", "vmemo" };
        }
        return m_saHeadRefItemKey;
    }

    /**
     * 作者：WYF 功能：获取需要保存的单据VO。 参数：PoCardPanel pnlCard 订单单据卡片 OrderVO voOld
     * 旧的订单VO，如果无，为NULL；如果有，则传完整的单据VO 返回：OrderVO 需保存的单据VO 单据及行的状态均已设置完成 例外：
     * 日期：(2003-9-9 11:37:13) 修改日期，修改人，修改原因，注释标志： 2003-10-08 wyf 修改对加入的空行的处理
     * 2004-03-02 wyf 修改删除的单据行旧合同号为空，导致数据错误的问题
     */
    public OrderVO getSaveVO(OrderVO voOld) {

        //停止编辑，以保证所有项均可取到与界面一样的值
        stopEditing();
        /*
        OrderVO vo = (OrderVO) getBillValueChangeVO(OrderVO.class.getName(),OrderHeaderVO.class.getName(),OrderItemVO.class.getName());
        if(vo == null 
                || vo.getHeadVO() == null
                || vo.getBodyVO() == null 
                || vo.getBodyVO().length == 0){
            SCMEnv.out("未修改任何项目，保存操作无效");
            return null;
        }
        */
        //使用锁定时有可能供应商基本ID无值，此处需重新设置
        String sVendMangId = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
                "cvendormangid").getValue());
        if (sVendMangId != null
                && PuPubVO
                        .getString_TrimZeroLenAsNull(getHeadItem("cvendorbaseid")) == null) {
            Object[] oaRet = null;
			try {
				oaRet = (Object[]) nc.ui.scm.pub.CacheTool.getCellValue(
	                    "bd_cumandoc", "pk_cumandoc", "pk_cubasdoc", sVendMangId);
			} catch (Exception ee) {
				/**不必抛出*/
				SCMEnv.out(ee);
			}			
            String sVendBaseId = (String) oaRet[0];
            setHeadItem("cvendorbaseid", sVendBaseId);
        }

        //模版非空项判断
        try {
            PuTool.validateNotNullField(this);
        } catch (Exception e) {
            PuTool.outException(this, e);
            return null;
        }

        //得到界面VO
        int iRowCount = getRowCount();
        //分别得到表头及有存货的表体
        OrderHeaderVO voUIHead = (OrderHeaderVO) getBillData()
                .getHeaderValueVO("nc.vo.po.OrderHeaderVO");
        //表头备注
        String sHeadMemo = ((UIRefPane) getHeadItem("vmemo").getComponent())
                .getUITextField().getText();
        voUIHead.setVmemo(sHeadMemo);
        //表头供应商收发货地址
        String sHeadAddr = ((UIRefPane) getHeadItem("cdeliveraddress")
                .getComponent()).getUITextField().getText();
        voUIHead.setCdeliveraddress(sHeadAddr);

        Vector vecUIBody = new Vector();
        for (int i = 0; i < iRowCount; i++) {
            if (PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(i,
                    OrderItemVO.FILTERKEY_WHENSAVE)) != null) {
                OrderItemVO voItem = (OrderItemVO) getBillModel().getBodyValueRowVO(i, "nc.vo.po.OrderItemVO");
                vecUIBody.addElement(voItem);
            }
        }

        OrderVO voSaved = new OrderVO();
        voSaved.setParentVO(voUIHead);
        voSaved.setChildrenVO((OrderItemVO[]) vecUIBody
                .toArray(new OrderItemVO[vecUIBody.size()]));

        //V5 增加检查
        try {
            voSaved.validateCentrPur();
        } catch (ValidationException e) {
            PuTool.outException(this, e);
            return null;
        }
        //订单ID
        String sOrderId = voSaved.getHeadVO().getCorderid();
        //是否新增单据，还是修改
        boolean bNewBill = (PuPubVO.getString_TrimZeroLenAsNull(sOrderId) == null) ? true
                : false;

        //改变的单据需与原单据作比较处理
        if (!bNewBill) {
            //界面的表体VO及长度
            OrderItemVO[] voaNewItem = voSaved.getBodyVO();
            int iNewLen = voaNewItem.length;
            //得到所有需传到后台的行
            Vector vecAllItem = new Vector();
            for (int i = 0; i < iNewLen; i++) {
                vecAllItem.addElement(voaNewItem[i]);
            }

            //原有的表体VO
            OrderItemVO[] voaOldItem = voOld.getBodyVO();
            int iOldLen = voaOldItem.length;
            //对原有单据表体循环
            //		DELETED 不在新的表体中
            //		UPDATED 在新的表体中，且新的表体改变
            //		NEW 新表体的ID为空
            for (int i = 0; i < iOldLen; i++) {
                //当前行ID
                String sOldBId = voaOldItem[i].getCorder_bid();
                //是否在新订单中仍旧存在
                boolean bExisted = false;

                //寻找与之匹配的行
                for (int j = 0; j < iNewLen; j++) {
                    String sNewBId = voaNewItem[j].getCorder_bid();
                    if (PuPubVO.isEqual(sOldBId, sNewBId)) {
                        bExisted = true;
                        break;
                    }
                }
                //表明该行被删除
                if (!bExisted) {
                    OrderItemVO voDeletedCloneItem = (OrderItemVO) voaOldItem[i]
                            .clone();
                    voDeletedCloneItem.setStatus(VOStatus.DELETED);
                    //voaOldItem[i].setStatus(VOStatus.DELETED) ;
                    vecAllItem.addElement(voDeletedCloneItem);
                }
            }
            //重新构造VO
            OrderItemVO[] voaAllBody = new OrderItemVO[vecAllItem.size()];
            vecAllItem.copyInto(voaAllBody);
            voSaved.setChildrenVO(voaAllBody);
        }

        //-----------置VOStatus状态
        if (bNewBill) {
            //设置操作类型、单据状态、操作员
            voSaved.setStatus(VOStatus.NEW);
            voSaved.getHeadVO().setStatus(VOStatus.NEW);
            voSaved.getHeadVO().setForderstatus(nc.vo.scm.pu.BillStatus.FREE);
        } else {
            //设置操作类型
            voSaved.setStatus(VOStatus.UPDATED);
            voSaved.getHeadVO().setStatus(VOStatus.UPDATED);
            //修改：必为第一版：修订则不修改状态
            if (voSaved.getHeadVO().getNversion().equals(
                    OrderHeaderVO.NVERSION_FIRST)) {
                voSaved.getHeadVO().setForderstatus(
                        nc.vo.scm.pu.BillStatus.FREE);
            }
        }
        //表体
        int iBodyLen = voSaved.getBodyVO().length;
        for (int i = 0; i < iBodyLen; i++) {
            String sOrderBId = voSaved.getBodyVO()[i].getCorder_bid();
            if (PuPubVO.getString_TrimZeroLenAsNull(sOrderBId) == null) {
                voSaved.getBodyVO()[i].setStatus(VOStatus.NEW);
            }
        }

        //处理自定义项
        OrderHeaderVO voHead = voSaved.getHeadVO();
//        String[] saVdefKey = new String[] { "vdef1", "vdef2", "vdef3", "vdef4",
//                "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10" };
//        int iVdefLen = saVdefKey.length;
//        for (int i = 0; i < iVdefLen; i++) {
//            JComponent component = getHeadItem(saVdefKey[i]).getComponent();
//            if (component instanceof UIRefPane) {
//                voHead.setAttributeValue(saVdefKey[i], ((UIRefPane) component)
//                        .getRefName());
//            }
//        }
        //计算主辅币
        String sExpMsg = PoChangeUI.setVONativeAndAssistCurrValue(voSaved);
        if (sExpMsg != null) {
            MessageDialog.showHintDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, sExpMsg);
            return null;
        }

        String sOrderCorp = getHeadItem("pk_corp").getValue();
        //设置订单VO当前所处公司
        if (!sOrderCorp.equals(PoPublicUIClass.getLoginPk_corp())) {
            voSaved.setGroupPkCorp(PoPublicUIClass.getLoginPk_corp());

            //取得登陆公司与单据公司对应的业务类型
            Object o = voSaved.getParentVO().getAttributeValue("cbiztype");
            String pk_busitype = (o == null ? null : o.toString());
            try {
                if (pk_busitype != null) {
                    String whereStr = " businame = (select businame from bd_busitype where pk_busitype = '"
                            + pk_busitype
                            + "') and pk_corp = '"
                            + PoPublicUIClass.getLoginPk_corp() + "'";
                    Object[][] objs = PubHelper.queryResultsFromAnyTable(
                            "bd_busitype", new String[] { "pk_busitype" },
                            whereStr);
                    //VO登陆公司业务类型赋值
                    if (objs != null && objs[0] != null && objs[0][0] != null) {
                        voSaved.setPkBusinessType(objs[0][0].toString());
                    }
                }
            } catch (Exception e) {
            }
        }

        //如果是补货订单，设置表体均为补货
        iBodyLen = voSaved.getBodyVO().length;
        for (int i = 0; i < iBodyLen; i++) {
            if (voHead.getBisreplenish() != null
                    && voHead.getBisreplenish().booleanValue()) {
                voSaved.getBodyVO()[i]
                        .setIisreplenish(OrderItemVO.IISREPLENISH_YES);
            } else {
                voSaved.getBodyVO()[i]
                        .setIisreplenish(OrderItemVO.IISREPLENISH_NO);
            }
        }

        //设置旧VO
        if (bNewBill) {
            voSaved.setOldVO(null);
        } else {
            voSaved.setOldVO(voOld);
        }
        

        //进行VO检查
        try {
            //组织检查的参数VO
            PoSaveCheckParaVO voPara = new PoSaveCheckParaVO();
            voPara.iDigit_Curr_FinanceLocal = m_cardPoPubSetUI2
                    .getMoneyDigitByCurr_Finance(m_cardPoPubSetUI2
                            .getCurrArith_Finance(sOrderCorp).getLocalCurrPK());
            voSaved.validate(voPara);
        } catch (Exception e) {
            PuTool.outException(this, e);
            return null;
        }

        //为满足审批流，设置操作员；
        PoPublicUIClass.setCuserId(new OrderVO[] { voSaved });

        return voSaved;
    }

    /**
     * 作者：WYF 功能：接口IBillModelSortPrepareListener 的实现方法 参数：String sItemKey
     * ITEMKEY 返回：无 例外：无 日期：(2004-03-24 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    public int getSortTypeByBillItemKey(String sItemKey) {

        if ("crowno".equals(sItemKey) || "csourcebillrowno".equals(sItemKey)
                || "cancestorbillrowno".equals(sItemKey)) {
            return BillItem.DECIMAL;
        }

        return getBillModel().getItemByKey(sItemKey).getDataType();
    }

    /**
     * 作者：李亮 功能：初始化订单卡片 参数：无 返回：无 例外：无 日期：(2001-04-21 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void initi() {

        setBillBeforeEditListenerHeadTail(this);

        nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
        timeDebug.start();

        BillData bd = null;
        try {
            bd = new BillData(getDefaultTemplet(getBillType(), null,
                    PoPublicUIClass.getLoginUser(), getCorp()));

        } catch (java.lang.Exception e) {
            PuTool.outException(this, e);
            return;
        }
        if (bd == null) {
            return;
        }
        timeDebug.addExecutePhase("加载模板");/*-=notranslate=-*/

        //设置表头参照列可见
        initiHideItems(bd);
        timeDebug.addExecutePhase("设置表头参照列可见");/*-=notranslate=-*/

        //设置表头，表体的ComboBox
        initiComboBox(bd);
        timeDebug.addExecutePhase("设置表头，表体的ComboBox");/*-=notranslate=-*/

        //最大最小值
        initiMinMaxValue(bd);
        timeDebug.addExecutePhase("最大最小值");/*-=notranslate=-*/

        //设置参照风格
        initiRefPane(bd);
        timeDebug.addExecutePhase("设置参照风格");/*-=notranslate=-*/

        //ITEM的可编辑性
        initiEnabledItems(bd);
        timeDebug.addExecutePhase("ITEM的可编辑性");/*-=notranslate=-*/

        try {
            //自定义项远程调用描述类
//            ServletCallDiscription[] scdsDef = nc.ui.scm.pub.def.DefSetTool
//                    .getTwoSCDs(getCorp());
            //全部远程调用描述类
        	ServcallVO[] scdsAll = new ServcallVO[1];
//            scdsAll[0] = scdsDef[0];
//            scdsAll[1] = scdsDef[1];
            scdsAll[0] = new ServcallVO();
            scdsAll[0].setBeanName("nc.itf.po.IOrder");
            scdsAll[0].setMethodName("initSatus");
            scdsAll[0].setParameter(new Object[] { getCorp() });
            scdsAll[0].setParameterTypes(new Class[] { String.class });
            //批量远程调用
           LocalCallService.callService(scdsAll);
//            nc.ui.scm.pub.def.DefSetTool.setTwoOBJs(new Object[] { objs[0],
//                    objs[1] });
        } catch (Exception e) {
            PuTool.outException(this, e);
        }

        //处理自定义项
        nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
                new BillCardPanel(bd), //当前模板DATA
                getCorp(), //公司主键
                ScmConst.PO_Order,  //单据类型
                "vdef", //单据模板中单据头的自定义项前缀
                "vdef" //单据模板中单据体的自定义项前缀
        );
        timeDebug.addExecutePhase("处理自定义项");/*-=notranslate=-*/

        //---------------设置数据
        setBillData(bd);
        timeDebug.addExecutePhase("设置数据");/*-=notranslate=-*/

        //---------------是对MODEL进行设置的必须放在后面
        //初始化业务类型设置
        //initiStatus();
        //timeDebug.addExecutePhase("初始化业务类型设置");

        //行号的设置
        BillRowNo.loadRowNoItem(this, "crowno");
        timeDebug.addExecutePhase("行号的设置");/*-=notranslate=-*/

        //设置排序的可用性
        getBodyPanel().addTableSortListener();
        getBillModel().setRowSort(true);
        timeDebug.addExecutePhase("设置排序的可用性");/*-=notranslate=-*/

        //千分位显示
        setBodyShowThMark(true);
        //是否显示合计行
        setTatolRowShow(true);
        //初始化表体收发货地址显示风格
        //setColType( getCorp() );
        //翻译
        PuTool.setTranslateRender(this);
        //监听
        initiListener();

        //---------------设置界面可编辑性
        setEnabled(false);
        timeDebug.addExecutePhase("其他");/*-=notranslate=-*/

        timeDebug.showAllExecutePhase("订单卡片加载");/*-=notranslate=-*/
    }

    /**
     * 设置表头、表体中的ComboBox
     *
     * @param
     * @return
     * @exception
     * @see
     * @since 2001-05-20
     */
    private void initiComboBox(BillData bd) {

        //表头扣税类别
        UIComboBox cbbHType = (UIComboBox) (bd.getHeadItem("idiscounttaxtype")
                .getComponent());
        cbbHType.setFont(this.getFont());
        cbbHType.setBackground(this.getBackground());
        cbbHType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_INNER);
        cbbHType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER);
        cbbHType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT);
        cbbHType.setSelectedIndex(1);

        cbbHType.setTranslate(true);

        bd.getHeadItem("idiscounttaxtype").setWithIndex(true);
        //表体扣税类别
        UIComboBox cbbBType = null;
        cbbBType = (UIComboBox) (bd.getBodyItem("idiscounttaxtype")
                .getComponent());
        cbbBType.setFont(this.getFont());
        cbbBType.setBackground(this.getBackground());

        cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_INNER);
        cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER);
        cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT);
        cbbBType.setSelectedIndex(1);

        cbbBType.setTranslate(true);

        bd.getBodyItem("idiscounttaxtype").setWithIndex(true);
    }

    /**
     * 作者：WYF 功能：设置不可编辑的字段 参数：无 返回：无 例外：无 日期：(2003-10-08 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void initiEnabledItems(BillData bd) {

        //表头中业务类型不能编辑
        bd.getHeadItem("cbiztype").setEnabled(false);
    }

    /**
     * 作者：李亮 功能：设置表头列的可见性 参数： boolean true 是否可见 返回：无 例外：无 日期：(2002-3-13
     * 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void initiHideItems(BillData bd) {
        bd.getHeadItem("pk_corp").setShow(true);
        bd.getHeadItem("cbiztype").setShow(true);
        bd.getHeadItem("cpurorganization").setShow(true);
//        bd.getHeadItem("cstoreorganization").setShow(true);
        bd.getHeadItem("cvendormangid").setShow(true);
//        bd.getHeadItem("cfreecustid").setShow(true);
        //bd.getHeadItem("caccountbankid").setShow(true);
//        bd.getHeadItem("cdeliveraddress").setShow(true);
        bd.getHeadItem("cemployeeid").setShow(true);
        bd.getHeadItem("cdeptid").setShow(true);
        //bd.getHeadItem("creciever").setShow(true);
        //bd.getHeadItem("cgiveinvoicevendor").setShow(true);
        //bd.getHeadItem("ctransmodeid").setShow(true);
        //bd.getHeadItem("ctermprotocolid").setShow(true);
    }

    /**
     * 作者：王印芬 功能：增加卡片界面监听 参数：无 返回：无 例外：无 日期：(2002-4-22 11:39:21)
     * 修改日期，修改人，修改原因，注释标志： 2002-07-22 wyf 加入自由项及编辑前监听 2002-08-20 wyf 加入批次号监听
     * 2002-09-17 ljq 去掉批次号监听 2002-09-18 wyf 加入仓库监听
     */
    private void initiListener() {
        //单据加监听
        addEditListener(this);
        addBodyEditListener2(this);
        addBodyMenuListener(this);

        //HEAD
        ((UIRefPane) getHeadItem("cemployeeid").getComponent()).getUIButton().addActionListener(this);
        //
        ((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).getUIButton().addActionListener(this);
        //行号排序监听
        getBillModel().setSortPrepareListener(this);
        //排序后事件
        getBillModel().addSortListener(this);
        
    }

    /**
     * 作者：王印芬 功能：对是小数的控件设置其可输入的最小值 参数：无 返回：无 例外：无 日期：(2002-8-26 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void initiMinMaxValue(BillData bd) {

        //======================表头
        //税率 折本汇率 折辅汇率
        String[] saHeadItem = new String[] { "ntaxrate", "nexchangeotobrate",
                "nexchangeotoarate", "nprepaymny", "nprepaymaxmny" };
        int iHeadLen = saHeadItem.length;
        for (int i = 0; i < iHeadLen; i++) {
            BillItem hItem = bd.getHeadItem(saHeadItem[i]);
            if (hItem != null) {
                UIRefPane refPanel = (UIRefPane) hItem.getComponent();
                UITextField textField = (UITextField) refPanel.getUITextField();
                textField.setMinValue(0.0);
            }
        }

        //======================表体
        //税率 折本汇率 折辅汇率 单价 无税单价 含税单价
        String[] saBodyItem = new String[] { "ntaxrate", "nexchangeotobrate",
                "nexchangeotoarate", "noriginalcurprice", "noriginalnetprice",
                "norgtaxprice", "norgnettaxprice" };
        int iBodyLen = saBodyItem.length;
        for (int i = 0; i < iBodyLen; i++) {
            BillItem bItem = bd.getBodyItem(saBodyItem[i]);
            if (bItem != null) {
                UIRefPane refPanel = (UIRefPane) bItem.getComponent();
                UITextField textField = (UITextField) refPanel.getUITextField();
                textField.setMinValue(0.0);
            }
        }

        return;

    }
    /**
     * 作者：李亮 功能：设置参照的可用性。 参数：无 返回：无 例外：无 日期：(2001-10-20 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void initiRefPane(BillData bd) {

        try {
        	
//	        int iHeadLen = getRefItemKeysHead().length;
//	        for (int i = 0; i < iHeadLen; i++) {
//	            if(bd.getHeadItem(getRefItemKeysHead()[i]) == null || bd.getHeadItem(getRefItemKeysHead()[i]).getComponent() == null){
//	                SCMEnv.out("模板出现空表头项："+getRefItemKeysHead()[i]);/*-=notranslate=-*/
//	                continue;
//	            }
//	            ((UIRefPane) bd.getHeadItem(getRefItemKeysHead()[i]).getComponent())
//	                    .setIsCustomDefined(true);
//	        }
//	        int iBodyLen = getRefItemKeysBody().length;
//	        for (int i = 0; i < iBodyLen; i++) {
//	            if(bd.getBodyItem(getRefItemKeysBody()[i]) == null || bd.getBodyItem(getRefItemKeysBody()[i]).getComponent() == null){
//	                SCMEnv.out("模板出现表体空项："+getRefItemKeysBody()[i]);/*-=notranslate=-*/
//	                continue;
//	            }
//	            ((UIRefPane) bd.getBodyItem(getRefItemKeysBody()[i]).getComponent())
//	                    .setIsCustomDefined(true);
//	        }
//	        int iTailLen = getRefItemKeysTail().length;
//	        for (int i = 0; i < iTailLen; i++) {
//	            if(bd.getTailItem(getRefItemKeysTail()[i]) == null || bd.getTailItem(getRefItemKeysTail()[i]).getComponent() == null){
//	                SCMEnv.out("模板出现空表头项："+getRefItemKeysTail()[i]);/*-=notranslate=-*/
//	                continue;
//	            }
//	            ((UIRefPane) bd.getTailItem(getRefItemKeysTail()[i]).getComponent())
//	                    .setIsCustomDefined(true);
//	        }
	
	        //-------------------表头
	        //备注
	        ((UIRefPane) bd.getHeadItem("vmemo").getComponent())
	                .setReturnCode(false);
	        ((UIRefPane) bd.getHeadItem("vmemo").getComponent())
	                .setAutoCheck(false);
	        //银行
	        ((UIRefPane) bd.getHeadItem("caccountbankid").getComponent())
	                .setReturnCode(true);
	        //供应商收发货地址
	        ((UIRefPane) bd.getHeadItem("cdeliveraddress").getComponent())
	                .setReturnCode(false);
	        ((UIRefPane) bd.getHeadItem("cdeliveraddress").getComponent())
	                .setAutoCheck(false);
	        ((UIRefPane) bd.getHeadItem("cdeliveraddress").getComponent())
	        		.setButtonVisible(true);
	        //散户，因散户移到PUB，因此直接引用	WYF
	        ((UIRefPane) bd.getHeadItem("cfreecustid").getComponent()).getRef()
	                .setRefUI(new UFRefGridUI(this));
	
	        //-------------------表体
	        //自由项
	        FreeItemRefPane firpFreeItemRefPane = new FreeItemRefPane();
	        firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree").getLength());
	        bd.getBodyItem("vfree").setComponent(firpFreeItemRefPane);
	        //批次号
	        if (bd.getBodyItem("vproducenum").isShow()) {
	//            nc.ui.ic.pub.lot.LotNumbRefPane lotRef = new nc.ui.ic.pub.lot.LotNumbRefPane();
//	        	IICPub_LotNumbRefPane lotRef = null;
	        	LotNumbRefPane lotRef = new LotNumbRefPane();
//	        	try{
//	        	    lotRef = (IICPub_LotNumbRefPane) InterServUI.getInterInstance(ProductCode.PROD_IC,InterRegister.IC0012);
//	        	}catch(Exception e){
//	        	    SCMEnv.out("获取存货批次号时出现异常，批次号参照不能正常使用！");/*-=notranslate=-*/
//	        	}
	        	if(lotRef != null){
	        	    lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
	        	}
	            bd.getBodyItem("vproducenum").setComponent((JComponent)lotRef);
	        }
	        //辅计量
	        ((UIRefPane) bd.getBodyItem("cassistunitname").getComponent())
	                .setReturnCode(false);
	        ((UIRefPane) bd.getBodyItem("cassistunitname").getComponent())
	                .setRefInputType(1);
	        ((UIRefPane) bd.getBodyItem("cassistunitname").getComponent())
	                .setCacheEnabled(false);
	        //备注
	        UIRefPane paneBodyMemo = ((UIRefPane) bd.getBodyItem("vmemo")
	                .getComponent());
	        paneBodyMemo.setButtonVisible(true);
	        paneBodyMemo.setReturnCode(false);
	        paneBodyMemo.setAutoCheck(false);
	        paneBodyMemo.setRefInputType(1);
	        //收发货地址
	        UIRefPane paneBodyAddr = ((UIRefPane) bd.getBodyItem("vreceiveaddress").getComponent());
	        paneBodyAddr.setRefModel(new PoReceiveAddrRefModel(getCorp(),null));
	        paneBodyAddr.setButtonVisible(true);
	        paneBodyAddr.setReturnCode(false);
	        paneBodyAddr.setAutoCheck(false);
	        paneBodyAddr.setRefInputType(1);
	        //供应商收发货地址
	        UIRefPane paneBodyAddrVendor = ((UIRefPane) bd.getBodyItem("vvenddevaddr").getComponent());
	        paneBodyAddrVendor.setRefModel(new PoReceiveAddrRefModel(getCorp(),null));
	        paneBodyAddrVendor.setButtonVisible(true);
	        paneBodyAddrVendor.setReturnCode(false);
	        paneBodyAddrVendor.setAutoCheck(false);
	        paneBodyAddrVendor.setRefInputType(1);
	        //仓库
	        UIRefPane paneBodyStor = ((UIRefPane) bd.getBodyItem("cwarehouse").getComponent());
	        paneBodyStor.setRefModel(new WarehouseRefModel(getCorp()));
        }catch (Exception e){
            PuTool.outException(e);
        }
    }
    /**
     * 作者：WYF 功能：修订完成后清空后续单据信息 参数：无 返回：无 例外：无 日期：(2003-11-11 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private boolean isHaveAfterBill(String sOrderId) {

        if (PuPubVO.getString_TrimZeroLenAsNull(sOrderId) == null) {
            return true;
        }
        Object oValue = h_mapAfterBill.get(sOrderId);

        if (oValue == null || oValue instanceof String) {
            return false;
        }

        return true;

    }

    /**
     * 作者：王印芬 功能：是修订还是修改 参数：无 返回：boolean 修订返回true；修改返回false 例外：无 日期：(2004-5-27
     * 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private boolean isRevise() {

        UFDouble dVersion = PuPubVO.getUFDouble_NullAsZero(getHeadItem(
                "nversion").getValue());
        Integer iVersion = dVersion.intValue() == 0 ? OrderHeaderVO.NVERSION_FIRST
                : new Integer(dVersion.intValue());
        return !(iVersion.compareTo(OrderHeaderVO.NVERSION_FIRST) == 0);
    }

    /**
     * 作者：王印芬 功能：得到界面某行是否关闭 参数：int iRow 指定行 返回：boolean 打开行返回true；否则为false 例外：
     * 日期：(2004-5-27 13:24:16) 修改日期，修改人，修改原因，注释标志：
     */
    private boolean isRowActive(int iRow) {

        Integer iActive = PuPubVO.getInteger_NullAs(getBodyValueAt(iRow,
                "iisactive"), OrderItemVO.IISACTIVE_ACTIVE);

        return iActive.compareTo(OrderItemVO.IISACTIVE_ACTIVE) == 0;
    }

    /**
     * 作者：WYF 功能：修订完成后清空后续单据信息 参数：无 返回：无 例外：无 日期：(2003-11-11 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private boolean isRowHaveAfterBill(int iRow) {
        String sHId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(iRow,
                "corderid"));
        String sBId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(iRow,
                "corder_bid"));

        if (sHId == null || sBId == null) {
            return false;
        }

        if (isHaveAfterBill(sHId)) {
            Object oValue = h_mapAfterBill.get(sHId);
            OrderAfterBillVO voAfter = (OrderAfterBillVO) oValue;
            return voAfter.isHaveAfterBill(sBId) ? true : false;
        }
        return false;
    }

    /**
     * 作者：WYF 功能：修订完成后清空后续单据信息 参数：无 返回：无 例外：无 日期：(2003-11-11 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    public boolean loadAfterBillInfo(OrderVO voOrder) {

        if (h_mapAfterBill.containsKey(voOrder.getHeadVO().getCorderid())) {
            return true;
        }

        OrderAfterBillVO voAfterBill = null;
        try {
            voAfterBill = OrderAfterBillHelper.queryAnyAfterBill(voOrder);
        } catch (Exception e) {
            PuTool.outException(this, e);
            return false;
        }

        if (voAfterBill == null) {
            h_mapAfterBill.put(voOrder.getHeadVO().getCorderid(), NO_AFTERBILL);
        } else {
            h_mapAfterBill.put(voOrder.getHeadVO().getCorderid(), voAfterBill);
        }

        return true;
    }

    /**
     * 作者：王印芬 功能：增加一行单据体 参数：无 返回：无 例外：无 日期：(2001-4-8 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    public void onActionAppendLine() {

        onActionAppendLine(1);
    }

    /**
     * 作者：王印芬 功能：增加N行单据体 参数：int iAppendCount 增加的行数 返回：无 例外：无 日期：(2001-4-8
     * 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    public void onActionAppendLine(int iAppendCount) {

        //showHintMessage( CommonConstant.SPACE_MARK + "增加订单行" +
        // CommonConstant.SPACE_MARK ) ;

        if (iAppendCount <= 0) {
            return;
        }
        int iRow = getBillModel().getRowCount() - 1;
        //增行
        getBodyPanel().addLine(iAppendCount);

        //需处理的行
        int[] iaRow = PuTool.getRowsAfterMultiSelect(iRow, iAppendCount + 1);

        //开始、结束行
        int iLen = iaRow.length;
        int iBeginRow = iaRow[1];
        int iEndRow = iaRow[iLen - 1];

//        String[] saCurrid = getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel());
//        String[] saCurridDis = m_cardPoPubSetUI2.getDistinctStrArray(saCurrid);
//        HashMap mapRate = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),saCurridDis);
//        HashMap mapRateEditable = m_cardPoPubSetUI2.getBothExchRateEditableBatch(getCorp(),saCurridDis);
//        HashMap mapRateVal = m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),saCurridDis,getHeadItem("dorderdate").getValue());
        for (int i = iBeginRow; i <= iEndRow; i++) {
            //设置新增行的默认值
            setDefaultBody(i);
            //合同号不可编辑
            setCellEditable(i, "ccontractcode", false);
        }
        getBillModel().execEditFormulaByKey(-1, "ccurrencytype"); //V31优化到循环外面
        //设置行号
        BillRowNo.addLineRowNos(this, BillTypeConst.PO_ORDER, "crowno",
                iAppendCount);

        //设置项目的自动携带
        PuTool.setBodyProjectByHeadProject(this, "cprojectid", "cprojectid",
                "cproject", iBeginRow, iEndRow);

        //备注
        setDefaultValueToVmemo();
        //设置表体“收发货地址、地区、地点”和“供应商收发货地址、地区、地点”默认值
        setDefaultValueWhenAppInsLines(iBeginRow,iEndRow);
        
    }
    /**
     * 初始化表体扣税类别,解决增行时表体扣税类别仍按表体上一次编辑的扣税类别而未按表头扣税类别计算
     * */
    private void initDisBeforeEdit(int iRow){
	    BillItem bi = getBillModel().getBodyItems()[getBillModel().getBodyColByKey("idiscounttaxtype")];
	    UIComboBox cmbBody = (UIComboBox) bi.getComponent();
	    int iRealIndex = cmbBody.getItemIndexByValue(getBodyValueAt(iRow,"idiscounttaxtype"));
	    cmbBody.setSelectedIndex(iRealIndex);
//	    BillEditEvent eventInit = new BillEditEvent(
//	           cmbBody,
//	           getBodyValueAt(iRow,"idiscounttaxtype"),
//	           "idiscounttaxtype",
//	           iRow,
//	           BillItem.BODY
//	    );
//	    afterEdit(eventInit);
	}
    /**
     * 设置表体“收发货地址、地区、地点”和“供应商收发货地址、地区、地点”默认值
     * */
    private void setDefaultValueWhenAppInsLines(int iBeginRow, int iEndRow){
        //“收发货地址”
        for (int i = iBeginRow; i <= iEndRow; i++) {
            afterEditWhenBodyArrWare(new BillEditEvent(getBodyItem("cwarehouse").getComponent(),null,"cwarehouse",i,BillItem.HEAD));
        }
	//“客商收发货地址”
	String strVendorId = getHeadItem("cvendorbaseid").getValue();
	setDefaultValueToBodyVendorAddr(strVendorId,iBeginRow,iEndRow);
	//地址优先取表头“客商收发货地址”
	String strVendorAddr = getHeadItem("cdeliveraddress").getValue();
	strVendorAddr = PuPubVO.getString_TrimZeroLenAsNull(strVendorAddr);
	if (strVendorAddr != null) {
		for (int i = iBeginRow; i <= iEndRow; i++) {
			getBillModel().setValueAt(strVendorAddr, i, "vvenddevaddr");
		}
	}
    }

    /**
     * 作者：王印芬 功能：复制当前订单 参数：无 返回：无 例外：无 日期：(2002-3-13 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     * 
     * 20050407 czp 优化：1、表头、表体置必要值(含默认值及数值类型值)；2、去除表头表体公式执行
     */
    public void onActionCopyBill(OrderVO voOld) {
      
        //关闭合计开关
    	boolean		bOldNeedCalc = getBillModel().isNeedCalculate() ; 
    	getBillModel().setNeedCalculate(false) ;

        nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
        timeDebug.start();

        //数据准备
        OrderVO voNew = (OrderVO) voOld.clone();

        //-------------表头
        OrderHeaderVO voHead = (OrderHeaderVO) voNew.getParentVO();
        //存货受托属性限制（见beforEditBodyInventory()）
        m_strCbiztype = voHead.getCbiztype();
        //订单主键
        voHead.setPrimaryKey(null);
        getHeadItem("corderid").setValue(null);
        //订单编码
        voHead.setVordercode(null);
        getHeadItem("vordercode").setValue(null);
        //审批人
        voHead.setCauditpsn(null);
        getTailItem("cauditpsn").setValue(null);
        //审批日期
        voHead.setDauditdate(null);
        getTailItem("dauditdate").setValue(null);
        //修订日期
        voHead.setDrevisiondate(null);
        getHeadItem("drevisiondate").setValue(null);
        //预付款限额
        voHead.setNprepaymaxmny(null);
        getHeadItem("nprepaymaxmny").setValue(null);
        //预付款
        voHead.setNprepaymny(null);
        getHeadItem("nprepaymny").setValue(null);

        //订单日期
        voHead.setDorderdate(PoPublicUIClass.getLoginDate());
        getHeadItem("dorderdate").setValue(PoPublicUIClass.getLoginDate());
        //订单状态
        voHead.setForderstatus(nc.vo.scm.pu.BillStatus.FREE);
        getHeadItem("forderstatus").setValue(nc.vo.scm.pu.BillStatus.FREE);
        //制单人
        voHead.setCoperator(PoPublicUIClass.getLoginUser());
        getTailItem("coperator").setValue(PoPublicUIClass.getLoginUser());
        //会计年度
        voHead.setCaccountyear(ClientEnvironment.getInstance().getAccountYear());
        getHeadItem("caccountyear").setValue(ClientEnvironment.getInstance().getAccountYear());
        //TS
        voHead.setTs(null);
        getHeadItem("ts").setValue(null);
        //制单时间
        voHead.setTmaketime(null);
        getTailItem("tmaketime").setValue(null);
        //最后修改时间
        voHead.setTlastmaketime(null);
        getTailItem("tlastmaketime").setValue(null);
        //审批时间        
        voHead.setTaudittime(null);
        getTailItem("taudittime").setValue(null);
        //版本
        voHead.setNversion(OrderHeaderVO.NVERSION_FIRST);
        getHeadItem("nversion").setValue(OrderHeaderVO.NVERSION_FIRST);
        //是否最新版本
        voHead.setBislatest(OrderHeaderVO.BISLATEST_YES);
        getHeadItem("bislatest").setValue(OrderHeaderVO.BISLATEST_YES);
        //是否补货：补货只能参照 20050407 XY
        voHead.setBisreplenish(OrderHeaderVO.BISREPLENISH_NO);
        getHeadItem("bisreplenish").setValue(OrderHeaderVO.BISREPLENISH_NO);
        //打印次数
        voHead.setIprintcount(null);
        if(getTailItem("iprintcount") != null){
            getTailItem("iprintcount").setValue(null);
        }
        //since v50 支持权限
        ((UIRefPane)getHeadItem("cvendormangid").getComponent()).getRefModel().setisRefEnable(true);
        getHeadItem("cvendormangid").setValue(voHead.getCvendormangid());
        if(((UIRefPane)getHeadItem("cvendormangid").getComponent()).getRefPK() == null){
        	voHead.setCvendormangid(null);
        	voHead.setCvendorbaseid(null);
        	getHeadItem("cvendorbaseid").setValue(null);
        	BillEditEvent bee = new BillEditEvent(
        			getHeadItem("cvendormangid").getComponent(),
        			"",
        			"cvendormangid",
        			-1,
        			BillItem.HEAD);
        	afterEditWhenHeadVendor(bee);
        }

        //-------------表体
        OrderItemVO[] voaItem = voNew.getBodyVO();
        int iLen = voaItem.length;

        //计划到货日期数据准备
        UFDate[] dplandates = new UFDate[iLen];
        try {
            dplandates = PoPublicUIClass.getDPlanArrvDateArray(
            		voHead.getPk_corp(),
            		//---V5 modify:------
            		//voHead.getCstoreorganization(), 
            		(String[]) OrderPubVO.getFieldArrayIncludeNull(new OrderVO[] { voOld },OrderItemVO.class, "pk_arrvstoorg", String.class),
            		//-------------------
            		voHead.getDorderdate(), 
            		(String[]) OrderPubVO.getFieldArrayIncludeNull(new OrderVO[] { voOld },OrderItemVO.class, "cbaseid", String.class),
                    (UFDouble[]) OrderPubVO.getFieldArrayIncludeNull(new OrderVO[] { voOld }, OrderItemVO.class,"nordernum", UFDouble.class));
            if(dplandates == null || dplandates.length != iLen){
                dplandates = new UFDate[iLen]; 
                SCMEnv.out("程序BUG：到货日期数据出现长度不匹配");
            }
        } catch (Exception e) {
            dplandates = new UFDate[iLen]; 
            PuTool.outException(this, e);
            SCMEnv.out("计算计划到货日期出错，不影响流程");
        }

        timeDebug.addExecutePhase("计划到货日期数据准备");/*-=notranslate=-*/
        //表体字段赋值
        for (int i = 0; i < voaItem.length; i++) {

            //计划到货日期
            voaItem[i].setDplanarrvdate(dplandates[i]);
            setBodyValueAt(dplandates[i],i,"dplanarrvdate");

            //表体主键
            voaItem[i].setCorder_bid(null);
            setBodyValueAt(null,i,"corder_bid");
            //表头主键
            voaItem[i].setCorderid(null);
            setBodyValueAt(null,i,"corderid");

            //累计到货数量
            voaItem[i].setNaccumarrvnum(null);
            setBodyValueAt(null,i,"naccumarrvnum");        
            //累计发票数量
            voaItem[i].setNaccuminvoicenum(null);
            setBodyValueAt(null,i,"naccuminvoicenum");
            //累计到货计划数量
            voaItem[i].setNaccumrpnum(null);
            setBodyValueAt(null,i,"naccumrpnum");
            //累计入库数量
            voaItem[i].setNaccumstorenum(null);
            setBodyValueAt(null,i,"naccumstorenum");
            //累计途耗数量
            voaItem[i].setNaccumwastnum(null);
            setBodyValueAt(null,i,"naccumwastnum");
            //累计退货数量
            voaItem[i].setNbackarrvnum(null);
            setBodyValueAt(null,i,"nbackarrvnum");
            //累计退库数量
            voaItem[i].setNbackstorenum(null);
            setBodyValueAt(null,i,"nbackstorenum");
            //确认数量
            voaItem[i].setNconfirmnum(null);
            setBodyValueAt(null,i,"nconfirmnum");
            //确认日期
            voaItem[i].setDconfirmdate(null);
            setBodyValueAt(null,i,"dconfirmdate");
            //修正日期
            voaItem[i].setDcorrectdate(null);
            setBodyValueAt(null,i,"dcorrectdate");
            //对方订单号
            voaItem[i].setVvendorordercode(null);
            setBodyValueAt(null,i,"vvendorordercode");
            //对方订单行号
            voaItem[i].setVvendororderrow(null);
            setBodyValueAt(null,i,"vvendororderrow");
            //操作员
            voaItem[i].setCoperator(null);
            setBodyValueAt(null,i,"coperator");
            //修正行
            voaItem[i].setCcorrectrowid(null);
            setBodyValueAt(null,i,"ccorrectrowid");
            
            /* V31 调整，订单复制保留来源
             * //来源单据ID 
             * voaItem[i].setCsourcebillid(null); 
             * //来源单据类型
             * voaItem[i].setCsourcebilltype(null); 
             * //来源单据行ID
             * voaItem[i].setCsourcerowid(null); 
             * //上层来源单据ID
             * voaItem[i].setCupsourcebillid(null); 
             * //上层来源单据行ID
             * voaItem[i].setCupsourcebillrowid(null); 
             * //上层来源单据类型
             * voaItem[i].setCupsourcebilltype(null);
             */
            
            //补货的复制要清空来源
            if(voOld.isReplenish()){
              //来源单据ID 
              voaItem[i].setCsourcebillid(null); 
              setBodyValueAt(null,i,"csourcebillid");
              //来源单据类型
              voaItem[i].setCsourcebilltype(null); 
              setBodyValueAt(null,i,"csourcebilltype");
              //来源单据行ID
              voaItem[i].setCsourcerowid(null); 
              setBodyValueAt(null,i,"csourcerowid");
              //上层来源单据ID
              voaItem[i].setCupsourcebillid(null); 
              setBodyValueAt(null,i,"cupsourcebillid");
              //上层来源单据行ID
              voaItem[i].setCupsourcebillrowid(null); 
              setBodyValueAt(null,i,"cupsourcebillrowid");
              //上层来源单据类型
              voaItem[i].setCupsourcebilltype(null);
              setBodyValueAt(null,i,"cupsourcebilltype");
            }
            //TS
            voaItem[i].setTs(null);
            setBodyValueAt(null,i,"ts");

            //基于效率考虑：上游时间戳置空{不考虑效率且是更合理的处理：设置数据库最新时间戳}
            voaItem[i].setCupsourcehts(null);
            setBodyValueAt(null,i,"cupsourcehts");
            voaItem[i].setCupsourcebts(null);
            setBodyValueAt(null,i,"upsourcebts");  
            
            //表体字段置缺省值
            voaItem[i].setForderrowstatus(OrderItemVO.FORDERROWSTATUS_FREE);
            setBodyValueAt(OrderItemVO.FORDERROWSTATUS_FREE,i,"forderrowstatus");
            voaItem[i].setIisactive(OrderItemVO.IISACTIVE_ACTIVE);
            setBodyValueAt(OrderItemVO.IISACTIVE_ACTIVE,i,"iisactive");
            voaItem[i].setIisreplenish(OrderItemVO.IISREPLENISH_NO);
            setBodyValueAt(OrderItemVO.IISREPLENISH_NO,i,"iisreplenish");
            
            //V3增加
            voaItem[i].setBreceiveplan(OrderItemVO.BRECEIVEPLAN_NO);
            setBodyValueAt(OrderItemVO.BRECEIVEPLAN_NO,i,"breceiveplan");

            //-----------V31增加
            
            //累计生成日计划数量
            voaItem[i].setNaccumdayplnum(null);
            setBodyValueAt(null,i,"naccumdayplnum");  
            //累计发运数量
            voaItem[i].setNaccumdevnum(null);
            setBodyValueAt(null,i,"naccumdevnum");   
        }
        
        timeDebug.addExecutePhase("设置默认值");/*-=notranslate=-*/
        
        //行号(设置行号操作要在重新寻找合同之前，因为用到行号)
        BillRowNo.setVORowNoByRule(voNew, BillTypeConst.PO_ORDER, "crowno");
        timeDebug.addExecutePhase("行号");/*-=notranslate=-*/
        
        //如有必要，重新寻找合同
        PoChangeUI.setCloneVOCntRelated(this, voNew);
        timeDebug.addExecutePhase("如有必要，重新寻找合同");/*-=notranslate=-*/
      
        //设置单据页面可编辑
        setEnabled(true);

        //V31：等同V30显示VO到界面处理--------------------------------------------------------------------------

        //上一张单据的公司
        String sOldPk_corp = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
                "pk_corp").getValue());

        //可能是集团采购，涉及多单位，因此要设置参照
        setCorp(voNew.getHeadVO().getPk_corp());

        //设置参照
        if (!voNew.getHeadVO().getPk_corp().equals(sOldPk_corp)) {
            setRefPane();
        }
        timeDebug.addExecutePhase("设置参照");/*-=notranslate=-*/

        //设置数量、价格精度
        setHeadDigits(getCorp());
        setBodyDigits_CorpRelated(getCorp(), getBillModel());
        timeDebug.addExecutePhase("设置数量、价格精度");/*-=notranslate=-*/

        //设置VO
//        setBillValueVO(voNew);
//        timeDebug.addExecutePhase("设置VO");/*-=notranslate=-*/
        
        //设置数值型值，字符型不设置
        String[] saDecimalKeyBody = new String[]{
//                "naccumarrvnum",
//                "naccumdayplnum",
//                "naccumdevnum",
//                "naccuminvoicenum",
//                "naccumrpnum",
//                "naccumstorenum",
//                "naccumwastnum",
                "nassistcurmny",
                "nassistnum",
                "nassisttaxmny",
                "nassisttaxpricemny",
//                "nbackarrvnum",
//                "nbackstorenum",
//                "nconfirmnum",
//                "nconvertrate",
                "ndiscountrate",
                "nexchangeotoarate",
                "nexchangeotobrate",
                "nmaxprice",
                "nmoney",
                "nnotarrvnum",
                "nnotstorenum",
                "nordernum",
                "norgnettaxprice",
                "norgtaxprice",
                "noriginalcurmny",
                "noriginalcurprice",
                "noriginalnetprice",
                "noriginaltaxmny",
                "noriginaltaxpricemny",
//                "nprice",
                "ntaxmny",
//                "ntaxprice",
                "ntaxpricemny",
                "ntaxrate"                
        };
        int jLen = saDecimalKeyBody.length;        
        for (int i = 0; i < iLen; i++) {
            for (int j = 0; j < jLen; j++) {
                setBodyValueAt(voaItem[i].getAttributeValue(saDecimalKeyBody[j]),i,saDecimalKeyBody[j]);   
            }
            setBodyValueAt(voaItem[i].getCcontractid(),i,"ccontractid");
            setBodyValueAt(voaItem[i].getccontractrowid(),i,"ccontractrowid");
            setBodyValueAt(voaItem[i].getCcurrencytypeid(),i,"ccurrencytypeid");
        }
        timeDebug.addExecutePhase("设置数值型值、合同信息、币种、行号");/*-=notranslate=-*/
        getBillModel().execLoadFormulaByKey("ccontractcode");//ccontractcode->getColValue(ct_manage,ct_code,pk_ct_manage,ccontractid)
        getBillModel().getValueAt(0,"ccontractcode");
        //设置供应商全称
//        //供应商全称
//        String formula = "ccustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,cvendorbaseid)";
//        execHeadFormula(formula);

        //设置表头快捷录入的显示：取第一行的值
        OrderItemVO voFirstItem = voNew.getBodyVO()[0];
        getHeadItem("ccurrencytypeid").setValue(voFirstItem.getCcurrencytypeid());
        //设置表头币种精度
        resetHeadCurrDigits();
        getHeadItem("nexchangeotobrate").setValue(voFirstItem.getNexchangeotobrate());
        getHeadItem("nexchangeotoarate").setValue(voFirstItem.getNexchangeotoarate());
        getHeadItem("cprojectid").setValue(voFirstItem.getCprojectid());
        getHeadItem("idiscounttaxtype").setValue(voFirstItem.getIdiscounttaxtype());
        getHeadItem("ntaxrate").setValue(voFirstItem.getNtaxrate());
        //设置表体与币种相关的数值
        try{
            resetBodyValueRelated_Curr(getCorp(),voFirstItem.getCcurrencytypeid(), getBillModel(),new BusinessCurrencyRateUtil(getCorp()),getRowCount(),m_cardPoPubSetUI2);
        }catch(Exception e){
        	SCMEnv.out(e);
        }
        timeDebug.addExecutePhase("重新设置币种相关的数值");/*-=notranslate=-*/

        //填充表头公式数据，避免不可参照出的数据
//        if (getIContainer() != null
//                && getIContainer().getOperState() != IPoCardPanel.OPER_STATE_EDIT) {
//            fillHeadTailRefShowText(voNew);
//        }
//        timeDebug.addExecutePhase("填充表头公式数据，避免不可参照出的数据");/*-=notranslate=-*/

//        //表头备注
//        ((UIRefPane) getHeadItem("vmemo").getComponent()).setText(voNew
//                .getHeadVO().getVmemo());

//        //表头供应商收发货地址
//        ((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).setText(voNew
//                .getHeadVO().getCdeliveraddress());

        //表体公式
//        getBillModel().execLoadFormula();
//        timeDebug.addExecutePhase("表体公式");/*-=notranslate=-*/

//        //设置自由项
//        PoPublicUIClass.setFreeColValue(getBillModel(), "vfree");
//        //计算并设置换算率
//        PuTool.setBillModelConvertRate(getBillModel(), new String[] {
//                "cbaseid", "cassistunit", "nordernum", "nassistnum",
//                "nconvertrate" });
//        timeDebug.addExecutePhase("计算并设置换算率");/*-=notranslate=-*/

        //处理来源单据类型、来源单据号
//        PuTool.loadSourceBillData(this, BillTypeConst.PO_ORDER);
//        PuTool.loadAncestorBillData(this, BillTypeConst.PO_ORDER);
//        timeDebug.addExecutePhase("处理来源单据类型、来源单据号");/*-=notranslate=-*/

        //最高限价
        PoPublicUIClass.loadCardMaxPrice(this, getCorp(),m_cardPoPubSetUI2);
        timeDebug.addExecutePhase("最高限价");/*-=notranslate=-*/

//        timeDebug.showAllExecutePhase("采购订单卡片显示");/*-=notranslate=-*/

        //------------------------------------------------------------------------------------------------------
        
        //设置表头的可编辑性
        setEnabled_Head(false);
        timeDebug.addExecutePhase("设置表头的可编辑性");/*-=notranslate=-*/

        //备注
        setDefaultValueToVmemo();
        timeDebug.addExecutePhase("备注");/*-=notranslate=-*/

        //收发货地址
        setDefaultValueToAddr();
        timeDebug.addExecutePhase("收发货地址");/*-=notranslate=-*/

        //20050310：显示调用表头编辑前处理
        /*V5 Del:
        if(getHeadItem("cstoreorganization") != null){
            beforeEdit(new BillItemEvent(getHeadItem("cstoreorganization")));
        }
        */
        updateValue();
        updateUI();

        //打开合计开关
    	getBillModel().setNeedCalculate(bOldNeedCalc) ;
    	timeDebug.addExecutePhase("打开合计开关");/*-=notranslate=-*/
        
        timeDebug.showAllExecutePhase("复制订单");/*-=notranslate=-*/

    }

    /**
     * 复制一行单据体
     *
     * @param
     * @return
     * @exception
     * @see
     * @since 2001-04-28
     */
    public void onActionCopyLine() {

        //showHintMessage( CommonConstant.SPACE_MARK + "复制订单行" +
        // CommonConstant.SPACE_MARK ) ;
        if (getRowCount() > 0) {
            if (getBillTable().getSelectedRowCount() <= 0) {
                MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000030")/*@res "复制行前请先选择表体行"*/);
                return;
            }
            copyLine();
        }

        //showHintMessage( CommonConstant.SPACE_MARK + "编辑订单" +
        // CommonConstant.SPACE_MARK ) ;
    }

    /**
     * 作者：王印芬 功能：删除一行单据体 参数： 返回： 例外： 日期：(2002-1-4 13:24:16) 修改日期，修改人，修改原因，注释标志：
     * 2002-07-08 wyf 删除一行后不自动增行，去掉判断，因为不存在行时，删行不可用 2004-05-27 wyf 加入对修订单据的判断
     */
    public void onActionDeleteLine() {

        //showHintMessage( CommonConstant.SPACE_MARK + "删除订单行" +
        // CommonConstant.SPACE_MARK ) ;

        //-------------有后续操作的行不可删除。（后续操作从发货开始）
        //主要为修订提供
        int[] iaSelectedRow = getBillTable().getSelectedRows();
        if (iaSelectedRow == null) {
            return;
        }
        int iSelectedCount = iaSelectedRow.length;
        for (int i = 0; i < iSelectedCount; i++) {
            String sBId = (String) getBodyValueAt(iaSelectedRow[i],
                    "corder_bid");
            if (PuPubVO.getString_TrimZeroLenAsNull(sBId) != null) {
                if (!isRowActive(iaSelectedRow[i])) {
                    //关闭的行不可删除
                    MessageDialog
                            .showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000031")/*@res "所选行存在已关闭的行，不能删除"*/);
                    return;
                }
                if (isRowHaveAfterBill(iaSelectedRow[i])) {
                    //有后续操作的行不可删除
                    MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/,
                            nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000032")/*@res "所选行存在有后续操作的行，不能删除"*/);
                    return;
                }
            }
        }

        //删行
        delLine();

    	//输入提示
    	if(getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
    		|| getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)){
    		OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),OrderHeaderVO.class.getName(),OrderItemVO.class.getName());
    		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    		ficr.setFreeItemRenderer((nc.ui.pub.bill.BillCardPanel)this, voCurr);
    	}
    }

    /**
     * 作者：王印芬 功能：插入一行单据体 参数： 返回： 例外： 日期：(2003-7-28 13:24:16) 修改日期，修改人，修改原因，注释标志：
     */
    public void onActionInsertLine() {

        //showHintMessage( CommonConstant.SPACE_MARK + "插入订单行" +
        // CommonConstant.SPACE_MARK ) ;

        if (getBillTable().getSelectedRowCount() <= 0) {
            MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "提示"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000033")/*@res "插行前请先选择表体行"*/);
            return;
        }
        onActionInsertLines(getBillTable().getSelectedRow(), 1);

    	//输入提示
    	if(getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
    		|| getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)){
    		OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),OrderHeaderVO.class.getName(),OrderItemVO.class.getName());
    		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    		ficr.setFreeItemRenderer((nc.ui.pub.bill.BillCardPanel)this, voCurr);
    	}

    }

    /**
     * 作者：王印芬 功能：插入多行单据体 参数：int iCurRow 在此行上插入 int iInsertCount 插入的行数 返回： 例外：
     * 日期：(2003-10-28 13:24:16) 修改日期，修改人，修改原因，注释标志：
     */
    private void onActionInsertLines(int iCurRow, int iInsertCount) {

        if (iInsertCount == 0) {
            return;
        }

        //增行
        getBodyPanel().insertLine(iCurRow, iInsertCount);

        int iLen = iInsertCount + 1;
        //增加的行
        int[] iaRow = PuTool.getRowsAfterMultiSelect(iCurRow, iLen);

        //开始、结束行
        int iBeginRow = iaRow[0];
        int iEndRow = iaRow[iLen - 2];

        //设值
//        String[] saCurrid = getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel());
//        String[] saCurridDis = m_cardPoPubSetUI2.getDistinctStrArray(saCurrid);
//        HashMap mapRate = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),saCurridDis);
//        HashMap mapRateEditable = m_cardPoPubSetUI2.getBothExchRateEditableBatch(getCorp(),saCurridDis);
//        HashMap mapRateVal = m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),saCurridDis,getHeadItem("dorderdate").getValue());

        for (int i = iBeginRow; i <= iEndRow; i++) {
            //设置新插行的默认值
            setDefaultBody(i);
            //合同号不可编辑
            setCellEditable(i, "ccontractcode", false);
        }
        getBillModel().execEditFormulaByKey(-1, "ccurrencytype"); //V31优化到循环外面
        //设置项目的自动携带
        PuTool.setBodyProjectByHeadProject(this, "cprojectid", "cprojectid",
                "cproject", iBeginRow, iEndRow);

        //设置行号
        BillRowNo.insertLineRowNos(this, BillTypeConst.PO_ORDER, "crowno",
                iaRow[iLen - 1], iInsertCount);

        //设置表体“收发货地址、地区、地点”和“供应商收发货地址、地区、地点”默认值
        setDefaultValueWhenAppInsLines(iBeginRow,iEndRow);
        
    }

    /**
     * 作者：王印芬 功能：修改当前订单 参数：无 返回：无 例外：无 日期：(2002-3-13 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    public void onActionModify() {

        //设置业务类型对存货参照的限制(见beforEditBodyInventory())
        m_strCbiztype = getHeadItem("cbiztype").getValue();

        //设置单据页面可编辑
        setEnabled(true);

        //设置表头表体的可编辑性
        setEnabled_Head(false);

        //表体备注
        setDefaultValueToVmemo();

        //收发货地址
        setDefaultValueToAddr();

        /** ************************************************************************************ */
        getBillTable().clearSelection();

        //全键盘
        //transferFocusTo(BillCardPanel.HEAD) ;

        //根据参数设置制单人
        setOperatorValue();

        //20050310：显示调用表头编辑前处理
        /*V5 Del:
        if(getHeadItem("cstoreorganization") != null){
            beforeEdit(new BillItemEvent(getHeadItem("cstoreorganization")));
        }
        */
        //固定值处理:基于V31华孚需求
        if(!(getContainer() instanceof RevisionUI)){
            PuTool.setBillCardPanelDefaultValue(this,this);
        }
        
        //设置自由项输入提示
    	if(getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
    		|| getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)){
    		OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),OrderHeaderVO.class.getName(),OrderItemVO.class.getName());
    		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    		ficr.setFreeItemRenderer(this, voCurr);
//    		updateUI();
    	}
        
      //since v51, 根据操作员设置采购员及采购部门
      setDefaultValueByUser();
    }
    /**
     * 根据操作员设置采购员及采购部门。
     * <p>
     * <b>examples:</b>
     * <p>
     * 使用示例
     * <p>
     * <b>参数说明</b>
     * <p>
     * @author czp
     * @time 2007-3-26 下午04:49:35
     */
    private void setDefaultValueByUser(){
      //取操作员对应业务员，设置采购员(采购员无值时才设置)
      if(PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("cemployeeid").getValueObject()) == null){
        IUserManageQuery iSrvUser = (IUserManageQuery) NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
        PsndocVO voPsnDoc = null;
        try{
          voPsnDoc = iSrvUser.getPsndocByUserid(getCorp(), PoPublicUIClass.getLoginUser());
        }catch(BusinessException be){
          SCMEnv.out(be);
        }
        if(voPsnDoc != null){
          UIRefPane refPanePrayPsn = (UIRefPane) getHeadItem("cemployeeid").getComponent();
          refPanePrayPsn.setPK(voPsnDoc.getPrimaryKey());
          //由采购员带出采购部门(如果采购员部门无值时才带出)
          if(PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("cdeptid").getValueObject()) == null){
            afterEditWhenHeadEmployee(null);
          }
        }
      }
    }
    /**
     * 作者：王印芬 功能：增加一张采购订单 参数：无 返回：无 例外：无 日期：(2002-3-13 11:39:21)
     * 修改日期，修改人，修改原因，注释标志： 2002-05-27 王印芬 先清除界面数据，现设置按钮状态，否则会导致按钮状态不正确
     */
    public void onActionNew(String sBizType) {

        //设置业务类型对存货参照的限制(见beforEditBodyInventory())
        m_strCbiztype = sBizType ;

        String sOldPk_corp = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
                "pk_corp").getValue());
        setCorp(PoPublicUIClass.getLoginPk_corp());

        //设置参照的公司主键(自制单据为本公司单据)
        //支持单据模板默认值、锁定值
        if (!PoPublicUIClass.getLoginPk_corp().equals(sOldPk_corp)) {
            setRefPane();
        }

        //设置单据页面可编辑
        setEnabled(true);

        //增加表头
        addNew();

        //设置表头默认数据
        setDefaultHead(sBizType);

        //设置表头的编辑性
        setEnabled_Head(false);

        //设置精度
        setBodyDigits_CorpRelated(getCorp(), getBillModel());

        //备注
        setDefaultValueToVmemo();

        //收发货地址
        setDefaultValueToAddr();

      //峰力听力不想要自动增行功能
      try{
         if(!nc.vo.scm.pub.CustomerConfigVO.getCustomerName().equalsIgnoreCase(nc.vo.scm.pub.CustomerConfigVO.NAME_FENGLITINGLI)){
        	 //v50:增加空行处理
        	 onActionAppendLine(1);
         }
       }catch(Exception e){
         SCMEnv.out(e);
       }
    }

    /**
     * 作者：王印芬 功能：行粘贴 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     * 2002-09-20 wyf 恢复对（上层）来源ID及来源行ID的复制 2002-11-25 wyf 对TS赋空 2003-02-27 wyf
     * 修改为：对所有复制的行进行项目清空及编辑性控制
     */
    public void onActionPasteLine() {

        //showHintMessage( CommonConstant.SPACE_MARK + "粘贴订单行" +
        // CommonConstant.SPACE_MARK ) ;
        //粘贴前的行数
        int iOrgRowCount = getRowCount();
        pasteLine();
        //增加的行数
        int iPastedRowCount = getRowCount() - iOrgRowCount;
        //设置行号
        BillRowNo.pasteLineRowNo(this, BillTypeConst.PO_ORDER, "crowno",
                iPastedRowCount);

        int iEndRow = getBillTable().getSelectedRow() - 1;
        int iBeginRow = iEndRow - iPastedRowCount + 1;

        //设置默认值
        setValueToPastedLines(iBeginRow, iEndRow);

        //showHintMessage( CommonConstant.SPACE_MARK + "编辑订单" +
        // CommonConstant.SPACE_MARK ) ;

    	//输入提示
    	if(getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
    		|| getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)){
    		OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),OrderHeaderVO.class.getName(),OrderItemVO.class.getName());
    		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    		ficr.setFreeItemRenderer((nc.ui.pub.bill.BillCardPanel)this, voCurr);
    	}

    }

    /**
     * 作者：王印芬 功能：行粘贴 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     * 2002-09-20 wyf 恢复对（上层）来源ID及来源行ID的复制 2002-11-25 wyf 对TS赋空 2003-02-27 wyf
     * 修改为：对所有复制的行进行项目清空及编辑性控制
     */
    public void onActionPasteLineToTail() {

        //showHintMessage( CommonConstant.SPACE_MARK + "粘贴订单行" +
        // CommonConstant.SPACE_MARK ) ;
        //粘贴前的行数
        int iOrgRowCount = getRowCount();
        pasteLineToTail();
        //增加的行数
        int iPastedRowCount = getRowCount() - iOrgRowCount;
        //设置行号
        BillRowNo.addLineRowNos(this, BillTypeConst.PO_ORDER, "crowno",
                iPastedRowCount);

        int iEndRow = getRowCount() - 1;
        int iBeginRow = iOrgRowCount;

        //设置默认值
        setValueToPastedLines(iBeginRow, iEndRow);

    	//输入提示
    	if(getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
    		|| getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH)){
    		OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),OrderHeaderVO.class.getName(),OrderItemVO.class.getName());
    		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
    		ficr.setFreeItemRenderer((nc.ui.pub.bill.BillCardPanel)this, voCurr);
    	}

    }

    /**
     * 作者：王印芬 功能：修订当前订单 参数：无 返回：无 例外：无 日期：(2002-3-13 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    public void onActionRevise(OrderVO voOrder) {

        //设置单据页面可编辑
        setEnabled(true);

        //设置修订订单的默认值
        setDefaultReviseItems(voOrder);

        //设置表头的可编辑性
        setEnabled_Head(true);

        //设置修订项的可编辑性
        setReviseEnabledCells_Head(voOrder);

        //表体备注
        setDefaultValueToVmemo();

        //收发货地址
        setDefaultValueToAddr();

        /** ************************************************************************************ */
        getBillTable().clearSelection();

        //全键盘
        //transferFocusTo(BillCardPanel.HEAD) ;
    }

    /**
     * 作者：王印芬 功能：处理表体浮动菜单 参数：无 返回：无 例外：无 日期：(2001-4-18 13:24:16)
     * 修改日期，修改人，修改原因，注释标志：
     */
    public void onMenuItemClick(java.awt.event.ActionEvent event) {

        //得到浮动菜单
        UIMenuItem menuItem = (UIMenuItem) event.getSource();

        if (menuItem.equals(getAddLineMenuItem())) {
            onActionAppendLine();
            if(getContainer() instanceof PoToftPanel){
            	((PoToftPanel)getContainer()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH036")/*@res "增行成功"*/);
            }
        } else if (menuItem.equals(getCopyLineMenuItem())) {
            onActionCopyLine();
            if(getContainer() instanceof PoToftPanel){
            	((PoToftPanel)getContainer()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH039")/*@res "复制行成功"*/);
            }
        } else if (menuItem.equals(getDelLineMenuItem())) {
            onActionDeleteLine();
            if(getContainer() instanceof PoToftPanel){
            	((PoToftPanel)getContainer()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH037")/*@res "删行成功"*/);
            }
        } else if (menuItem.equals(getInsertLineMenuItem())) {
            onActionInsertLine();
            if(getContainer() instanceof PoToftPanel){
            	((PoToftPanel)getContainer()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH038")/*@res "插入行成功"*/);
            }
        } else if (menuItem.equals(getPasteLineMenuItem())) {
            onActionPasteLine();
            if(getContainer() instanceof PoToftPanel){
            	((PoToftPanel)getContainer()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH040")/*@res "粘贴行成功"*/);
            }
        } else if (menuItem.equals(getPasteLineToTailMenuItem())) {
            onActionPasteLineToTail();
            if(getContainer() instanceof PoToftPanel){
            	((PoToftPanel)getContainer()).showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH040")/*@res "粘贴行成功"*/);
            }
        }
    }

    /**
     * 作者：WYF 功能：设置表体与币中有关的值的精度，此方法与列表界面同时使用 参数：String pk_corp 公司ID BillModel
     * billModel BillModel 返回：无 例外：无 日期：(2004-6-11 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    protected static void resetBodyValueRelated_Curr(String pk_corp,String strHeadCurId,BillModel billModel,BusinessCurrencyRateUtil bca,int iLen,POPubSetUI2 setUi) {
        //=====表体
        //金额类需合计
        String[] saMnyItem = new String[] { "noriginalcurmny",
                "noriginaltaxmny", "noriginaltaxpricemny" };
        int iMnyLen = saMnyItem.length;
        //金额的最大位数
        int iMaxMnyDigit = 0;

        //设为不合计，否则将占用非常多的时间
        boolean bOldNeedCalculate = billModel.isNeedCalculate();
        billModel.setNeedCalculate(false);

        //设值
//    	String[] saCurrId = getAllCurrIdFromCard(iLen,pk_corp,strHeadCurId,billModel);
//    	int iCnt = saCurrId == null ? 0 : saCurrId.length;
//    	String[] saPk_corp = new String[iCnt];
//    	for (int i = 0; i < iCnt; i++) {
//    	    saPk_corp[i] = pk_corp;
//        }    	
//    	HashMap mapRate = m_cardPoPubSetUI2.getBothExchRateDigit(saPk_corp,saCurrId);
//    	HashMap mapRateMny = m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);

        for (int i = 0; i < iLen; i++) {
            //折本、折辅汇率
            setRowDigits_ExchangeRate(pk_corp, i, billModel, setUi);
            billModel.setValueAt(billModel.getValueAt(i, "nexchangeotobrate"),
                    i, "nexchangeotobrate");
            billModel.setValueAt(billModel.getValueAt(i, "nexchangeotoarate"),
                    i, "nexchangeotoarate");
            //金额、税额、价税合计
            setRowDigits_Mny(pk_corp, i, billModel,setUi);
            //本币金额类
            int jLenMny = OrderItemVO.getDbMnyFields_Local_Busi().length;
            for (int j = 0; j < jLenMny; j++) {
                billModel.setValueAt(billModel.getValueAt(i, OrderItemVO.getDbMnyFields_Local_Busi()[j]), i,
                		OrderItemVO.getDbMnyFields_Local_Busi()[j]);
            }
            //辅币金额类
            jLenMny = OrderItemVO.getDbMnyFields_Assist_Busi().length;
            for (int j = 0; j < jLenMny; j++) {
                billModel.setValueAt(billModel.getValueAt(i, OrderItemVO.getDbMnyFields_Assist_Busi()[j]), i,
                		OrderItemVO.getDbMnyFields_Assist_Busi()[j]);
            }
            for (int j = 0; j < iMnyLen; j++) {
                billModel.setValueAt(billModel.getValueAt(i, saMnyItem[j]), i,
                        saMnyItem[j]);
            }
            if (billModel.getItemByKey(saMnyItem[0]).getDecimalDigits() > iMaxMnyDigit) {
                iMaxMnyDigit = billModel.getItemByKey(saMnyItem[0])
                        .getDecimalDigits();
            }
        }

        //计算合计
        for (int i = 0; i < iMnyLen; i++) {
            billModel.getItemByKey(saMnyItem[0]).setDecimalDigits(iMaxMnyDigit);
            billModel.reCalcurate(billModel.getBodyColByKey(saMnyItem[i]));
        }
        billModel.setNeedCalculate(bOldNeedCalculate);
    }
    /**
     * 作者：晁志平 功能：设置折本及折辅汇率小数位 
     * 参数：无
     * 返回：无 
     * 例外：无
     * 日期：2005-6-17 14:39:21
     * 修改日期，修改人，修改原因，注释标志
     */
    private void resetHeadCurrDigits() {
        //=====表头
        String sCurrId = getHeadItem("ccurrencytypeid").getValue();
        if (sCurrId != null && sCurrId.trim().length() > 0) {
            //得到汇率精度
            int[] iaExchRateDigit = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),
                    sCurrId);
            //设置精度
            getHeadItem("nexchangeotobrate").setDecimalDigits(
                    iaExchRateDigit[0]);
            getHeadItem("nexchangeotoarate").setDecimalDigits(
                    iaExchRateDigit[1]);
        }
    }
    /**
     * 作者：王印芬 功能：设置与公司有关的精度，此方法与列表界面同时使用 参数：ActionEvent e 事件 返回：无 例外：无
     * 日期：(2002-4-22 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-16 wyf
     * getDecimalDigits()修改为采用公共方法 2002-07-05 wyf 修正一个笔误，该笔误导致列表界面换算率精度不正确
     * 2002-10-28 wyf 加入对最高限价及含税单价的精度控制
     */
    protected static void setBodyDigits_CorpRelated(String pk_corp, BillModel bm) {

        int[] iaDigit = (int[]) PoPublicUIClass.getShowDigits(pk_corp);
        //数量类
        int iLen = OrderItemVO.getDbMainNumFields().length;
        for (int i = 0; i < iLen; i++) {
            BillItem item = bm
                    .getItemByKey(OrderItemVO.getDbMainNumFields()[i]);
            if (item != null && iaDigit != null && iaDigit.length > 0) {
                item.setDecimalDigits(iaDigit[0]);
            }
        }
        //辅数量类
        iLen = OrderItemVO.getDbAssistNumFields().length;
        for (int i = 0; i < iLen; i++) {
            BillItem item = bm
                    .getItemByKey(OrderItemVO.getDbAssistNumFields()[i]);
            if (item != null) {
                item.setDecimalDigits(iaDigit[1]);
            }
        }
        //单价类
        iLen = OrderItemVO.getDbPriceFields().length;
        for (int i = 0; i < iLen; i++) {
            BillItem item = bm.getItemByKey(OrderItemVO.getDbPriceFields()[i]);
            if (item != null) {
                item.setDecimalDigits(iaDigit[2]);
            }
        }
        bm.getItemByKey("nmaxprice").setDecimalDigits(iaDigit[2]);
        //换算率
        bm.getItemByKey("nconvertrate").setDecimalDigits(iaDigit[3]);

        //表体币种相关的设置为最大位数，以免被截位
        //折本、折辅
        bm.getItemByKey("nexchangeotobrate").setDecimalDigits(
                FieldMaxLength.DECIMALDIGIT_TOBRATE);
        bm.getItemByKey("nexchangeotoarate").setDecimalDigits(
                FieldMaxLength.DECIMALDIGIT_TOARATE);
        //金额
        bm.getItemByKey("noriginalcurmny").setDecimalDigits(
                FieldMaxLength.DECIMALDIGIT_MONEY);
        bm.getItemByKey("noriginaltaxmny").setDecimalDigits(
                FieldMaxLength.DECIMALDIGIT_MONEY);
        bm.getItemByKey("noriginaltaxpricemny").setDecimalDigits(
                FieldMaxLength.DECIMALDIGIT_MONEY);
        //本币金额类
        int jLenMny = OrderItemVO.getDbMnyFields_Local_Busi().length;
        for (int j = 0; j < jLenMny; j++) {
            bm.getItemByKey(OrderItemVO.getDbMnyFields_Local_Busi()[j]).setDecimalDigits(
                    FieldMaxLength.DECIMALDIGIT_MONEY);
        }
        //辅币金额类
        jLenMny = OrderItemVO.getDbMnyFields_Assist_Busi().length;
        for (int j = 0; j < jLenMny; j++) {
            bm.getItemByKey(OrderItemVO.getDbMnyFields_Assist_Busi()[j]).setDecimalDigits(
                    FieldMaxLength.DECIMALDIGIT_MONEY);
        }

    }

    /**
     * 作者：王印芬 功能：设置当前调用者 参数：无 返回：无 例外：无 日期：(2001-05-22 13:24:16)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setContainer(Container newCon) {
        m_conInvoker = newCon;
    }

    /**
     * 作者：李亮 功能：增加表体行时，设置默认数据 参数：int Row 第几行 返回：无 例外：无 日期：(2002-3-13 11:39:21)
     * 修改日期，修改人，修改原因，注释标志： 2002-06-07 WYF 删除默认设置当前操作人的代码 2002-06-18 WYF
     * 删除默认设置当前ID为一整数的代码 2003-01-20 WYF 修改对计划到货日期的计算
     */
    private void setDefaultBody(int iCurRow) {

        //ID
        setBodyValueAt(null, iCurRow, "corder_bid");
        //公司编码
        setBodyValueAt(getCorp(), iCurRow, "pk_corp");
        //各种状态
        setBodyValueAt(OrderItemVO.FORDERROWSTATUS_FREE, iCurRow,
                "forderrowstatus");
        setBodyValueAt(OrderItemVO.IISREPLENISH_NO, iCurRow, "iisreplenish");
        setBodyValueAt(OrderItemVO.IISACTIVE_ACTIVE, iCurRow, "iisactive");
        //V3增加
        setBodyValueAt(OrderItemVO.BRECEIVEPLAN_NO, iCurRow, "breceiveplan");
        //制单人
        setBodyValueAt(PoPublicUIClass.getLoginUser(), iCurRow, "coperator");
        //计算计划到货日期
        calDplanarrvdate(iCurRow, iCurRow);
        //扣税类别
        String text = null;
        //if (getHeadItem("idiscounttaxtype").getDataType() == 6) {
        JComboBox cbbHType = (JComboBox) (getHeadItem("idiscounttaxtype")
                .getComponent());
        if (cbbHType.getSelectedItem() != null) {
            text = cbbHType.getSelectedItem().toString();
        }
        //}
        if (text != null) {
            setBodyValueAt(text, iCurRow, "idiscounttaxtype");
        } else {
            setBodyValueAt(VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER, iCurRow,
                    "idiscounttaxtype");
        }

        //币种、汇率
        setBodyValueAt(getHeadItem("ccurrencytypeid").getValue(), iCurRow,
                "ccurrencytypeid");
//        execBodyFormula(iCurRow, "ccurrencytype"); //V31优化到循环外面
        //设置精度及可编辑性等
        setExchangeRateBody(iCurRow, false);
        setBodyValueAt(PuPubVO.getUFDouble_ValueAsValue(getHeadItem(
                "nexchangeotobrate").getValue()), iCurRow, "nexchangeotobrate");
        setBodyValueAt(PuPubVO.getUFDouble_ValueAsValue(getHeadItem(
                "nexchangeotoarate").getValue()), iCurRow, "nexchangeotoarate");
        //税率
        String o = getHeadItem("ntaxrate").getValue();
        setBodyValueAt(PuPubVO.getUFDouble_ValueAsValue(o), iCurRow, "ntaxrate");
        //扣率
        setBodyValueAt(VariableConst.HUNDRED_UFDOUBLE, iCurRow, "ndiscountrate");
        //设置项目信息
        if (iCurRow - 1 > 0) {
            setProjectInfoAs(iCurRow, iCurRow - 1);
        }
        //备注
        setDefaultValueToVmemo();
        //收发货地址(表头、体的“供应商收发货地址”及表要体的“收发货地址”)
        setDefaultValueToAddr();
        
        //---V5新增
        
        //收货公司：默认为当前登录公司
        setBodyValueAt(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), iCurRow, "pk_arrvcorp");
        setBodyValueAt(ClientEnvironment.getInstance().getCorporation().getUnitname(), iCurRow, "arrvcorpname");
        //收票公司：默认为当前登录公司
        setBodyValueAt(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), iCurRow, "pk_invoicecorp");
        setBodyValueAt(ClientEnvironment.getInstance().getCorporation().getUnitname(), iCurRow, "invoicecorpname");
        
        updateUI();
    }

    /**
     * 作者：李亮 功能：增加新订单时，设置表头默认数据 参数：int Row 第几行 返回：无 例外：无 日期：(2002-3-13 11:39:21)
     * 修改日期，修改人，修改原因，注释标志： 2002-06-07 WYF 删除默认设置当前操作人的代码 2003-10-08 WYF
     * 修改币种精度不正确的问题
     */
    private void setDefaultHead(String sBizType) {

        try {
            //公司编码
            setHeadItem("pk_corp", getCorp());
            //业务类型
            setHeadItem("cbiztype", sBizType);
            //状态
            setHeadItem("forderstatus", nc.vo.scm.pu.BillStatus.FREE);
            //制单人
            setTailItem("coperator", PoPublicUIClass.getLoginUser());
            //订单日期：系统注册日期
            setHeadItem("dorderdate", PoPublicUIClass.getLoginDate());
            //汇率：默认携带币种表中的汇率，如无数据，则默认为1，可修改
            String sLocalCurrId = m_cardPoPubSetUI2.getCurrArith_Busi(getCorp())
                    .getLocalCurrPK();
            getHeadItem("ccurrencytypeid").setValue(sLocalCurrId);
            //setHeadItem("nexchangeotobrate",
            // m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),sLocalCurrId,getLoginDate())[0]
            // );
            //setHeadItem("nexchangeotoarate",
            // m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),sLocalCurrId,getLoginDate())[1]
            // );
            setExchangeRateHead(getHeadItem("dorderdate").getValue(),
                    sLocalCurrId);
            //税率：默认为0，可修改
            //setHeadItem("ntaxrate", VariableConst.ZERO);
            //扣税类别
            setHeadItem("idiscounttaxtype",
                    VariableConst.IDISCOUNTTAXTYPE_OUTTER);
            //版本
            setHeadItem("nversion", OrderHeaderVO.NVERSION_FIRST);
            //是否最新版本
            setHeadItem("bislatest", OrderHeaderVO.BISLATEST_YES);
            //是否补货
            setHeadItem("bisreplenish", OrderHeaderVO.BISREPLENISH_NO);   
            //根据操作员设置采购员及采购部门 
            setDefaultValueByUser();
        } catch (Exception e) {
            PuTool.outException(this, e);
        }
    }

    /**
     * 作者：李亮 功能：改变界面数据后根据供应商设置OrderVO的一些默认值。 参数：int Row 第几行 返回：无 例外：无
     * 日期：(2001-10-16 11:39:21) 修改日期，修改人，修改原因，注释标志： 2003-10-20 wyf
     * 修改供应商清空后业务员等清空的处理
     */
    private void setDefaultHeadafterVendor(BillEditEvent e) {

        //供应商参照
        UIRefPane ref = (UIRefPane) (getHeadItem("cvendormangid")
                .getComponent());
        String sVendorMangId = ref.getRefPK();
        if (PuPubVO.getString_TrimZeroLenAsNull(sVendorMangId) == null) {
            UIRefPane psn = (UIRefPane) (getHeadItem("cemployeeid")
                    .getComponent());
            String whereString = "bd_psndoc.pk_corp = '"
                    + getHeadItem("pk_corp").getValueObject() + "'";

            psn.setWhereString(whereString);
            getHeadItem("cgiveinvoicevendor").setValue(null);
            getHeadItem("ctransmodeid").setValue(null);
            getHeadItem("ctermprotocolid").setValue(null);
            getHeadItem("caccountbankid").setValue(null);
            getHeadItem("account").setValue(null);
            getHeadItem("cdeliveraddress").setValue(null);
            return;
        }
        getHeadItem("caccountbankid").setValue(null);
        getHeadItem("account").setValue(null);
        getHeadItem("cdeliveraddress").setValue(null);
        setRefPane_Head("caccountbankid");
        setRefPane_Head("cdeliveraddress");

        PoVendorVO vendorVO = null;
        vendorVO = PoPublicUIClass.getVendDefaultInfo(sVendorMangId);
        if (vendorVO == null) {
            getHeadItem("cdeptid").setValue(null);
            getHeadItem("cemployeeid").setValue(null);
            getHeadItem("creciever").setValue(null);
            //收货方驱动设置表体“收发货地址、地区、地点”(调用表头收货方编辑后事件)
            afterEditWhenHeadReciever(new BillEditEvent(getHeadItem("creciever").getComponent(),null,"creciever",0,BillCardPanel.HEAD));
            getHeadItem("ctransmodeid").setValue(null);
            getHeadItem("ctermprotocolid").setValue(null);
            getHeadItem("ccurrencytypeid").setValue(null);
            getHeadItem("caccountbankid").setValue(null);
            getHeadItem("account").setValue(null);
            getHeadItem("cdeliveraddress").setValue(null);
            //发票方
            getHeadItem("cgiveinvoicevendor").setValue(sVendorMangId.trim());
            return;
        }
        //部门
        if (vendorVO.getCrespdept1() != null) {
            getHeadItem("cdeptid").setValue(vendorVO.getCrespdept1().trim());
            /*
            UIRefPane psn = (UIRefPane) (getHeadItem("cemployeeid")
                    .getComponent());
            String whereString = null;
            if ((vendorVO.getCrespdept1() != null)
                    && (!(vendorVO.getCrespdept1().trim().equals("")))) {
                whereString = "bd_psndoc.pk_deptdoc = '"
                        + vendorVO.getCrespdept1().trim() + "'";
            } else {
                whereString = "bd_psndoc.pk_corp = '"
                        + getHeadItem("pk_corp").getValue().trim() + "'";
            }
            psn.setWhereString(whereString);
            */
        }
        //业务员
        if (vendorVO.getCresppsn1() != null) {
            getHeadItem("cemployeeid").setValue(vendorVO.getCresppsn1().trim());
            //业务员带出部门
            if (PuPubVO.getString_TrimZeroLenAsNull(vendorVO.getCrespdept1()) == null) {
                Object[] oaValue = null;
    			try {
    				oaValue = (Object[]) nc.ui.scm.pub.CacheTool
                    .getCellValue("bd_psndoc", "pk_psndoc", "pk_deptdoc",
                            vendorVO.getCrespdept1());
    			} catch (Exception ee) {
    				/**不必抛出*/
    				SCMEnv.out(e);
    			}			
                getHeadItem("cdeptid").setValue(
                        oaValue == null ? null : (String) oaValue[0]);
            }
            //业务员带出默认采购组织
            setPurOrg(vendorVO.getCresppsn1().trim());
        }
        //发票方
        if (vendorVO.getCcusmandoc2() != null) {
            getHeadItem("cgiveinvoicevendor").setValue(
                    vendorVO.getCcusmandoc2().trim());
        } else {
            getHeadItem("cgiveinvoicevendor").setValue(
                    vendorVO.getCcumandoc().trim());
        }
        /*待定：20041207　收货方只与本公司有关，与供应商无关！？
        //收货方
        if (vendorVO.getCcusmandoc3() != null) {
            ((UIRefPane)getHeadItem("creciever").getComponent()).setPK(vendorVO.getCcusmandoc3());
        }else{
            ((UIRefPane)getHeadItem("creciever").getComponent()).setPK(null);
        }
        //收货方驱动设置表体“收发货地址、地区、地点”(调用表头收货方编辑后事件)
        setDefaultValueWhenHeadRecieverChged(new BillEditEvent(getHeadItem("creciever").getComponent(),null,"creciever",0,BillCardPanel.HEAD),vendorVO.getCcusmandoc3());
        */
        //发运方式
        if (vendorVO.getCsendtype() != null /*&& isBdeliver()*/) {
            getHeadItem("ctransmodeid")
                    .setValue(vendorVO.getCsendtype().trim());
        }
        //付款协议
        if (vendorVO.getCpayterm() != null) {
            getHeadItem("ctermprotocolid").setValue(
                    vendorVO.getCpayterm().trim());
        }
        //币种
        if (vendorVO.getCcurrtype1() != null) {
            getHeadItem("ccurrencytypeid").setValue(vendorVO.getCcurrtype1());
            afterEditWhenHeadCurrency(e);
        }
        //开户银行
        if (vendorVO.getCcustbank() != null) {
            getHeadItem("caccountbankid").setValue(vendorVO.getCcustbank().trim());
        }
        //帐号
        if (vendorVO.getCaccount() != null) {
            getHeadItem("account").setValue(vendorVO.getCaccount().trim());
        }
        //发货地址
        if (vendorVO.getCcustaddr() != null) {
            ((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).setPK(vendorVO.getCcustaddr());
            setHeadItem("cdeliveraddress",((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).getRefName());
        }
    }

    /**
     * 作者：王印芬 功能：走默认的价格路线 该函数由setCntRelated(Integer[])调用 参数： Integer[] iaRow
     * 需计算默认价格信息的表体所有行数组 返回：无 例外：无 日期：(2003-10-29 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void setDefaultPrice(Integer[] iaRow) {

        if (iaRow == null) {
            return;
        }
        int iTotalLen = iaRow.length;

        //公司
        String pk_corp = getHeadItem("pk_corp").getValue();
        //供应商管理ID
        String sVendId = getHeadItem("cvendormangid").getValue();
        //日期
        String sDate = getHeadItem("dorderdate").getValue();

        //取得默认价格并重新计算数量关系
        Vector vecStoOrgId = new Vector();
        Vector vecInvId = new Vector();
        Vector vecInvBaseId = new Vector();
        Vector vecCurrId = new Vector();
        Vector vecBRate = new Vector();
        Vector vecARate = new Vector();
        Vector vecIndex = new Vector();

        int iRow = 0;
        for (int i = 0; i < iTotalLen; i++) {
            iRow = iaRow[i].intValue();
            String sInvId = (String) getBillModel().getValueAt(iRow, "cmangid");
            String sInvBaseId = (String) getBillModel().getValueAt(iRow, "cbaseid");
            if (sInvId == null || sInvId.trim().equals("")) {
                continue;
            }
            String sCurrId = (String) getBillModel().getValueAt(iRow,"ccurrencytypeid");
            Object oBRate = getBodyValueAt(iRow, "nexchangeotobrate");
            Object oARate = getBodyValueAt(iRow, "nexchangeotoarate");
            
            //V5 add:空元素由后台处理
           	vecStoOrgId.addElement((String)PuPubVO.getString_TrimZeroLenAsNull(getBillModel().getValueAt(iRow,"pk_arrvstoorg")));
            
           	vecInvId.addElement(sInvId);
            vecInvBaseId.addElement(sInvBaseId);
            vecCurrId.addElement(sCurrId);
            vecBRate.addElement((UFDouble) oBRate);
            vecARate.addElement((UFDouble) oARate);
            vecIndex.addElement(iaRow[i]);
        }

        int iQueryLen = vecIndex.size();
        if (iQueryLen == 0) {
            return;
        }

        //存货 币种 折本汇率 折辅汇率
        //取默认价格
        RetPoVrmAndParaPriceVO voPara = new RetPoVrmAndParaPriceVO(1);
        voPara.setPk_corp(pk_corp);
        //V5 modify:
        //voPara.setStoOrgId(getHeadItem("cstoreorganization").getValue());
        voPara.setStoOrgIds((String[]) vecStoOrgId.toArray(new String[vecStoOrgId.size()]));
        voPara.setVendMangId(sVendId);
        voPara.setSaInvMangId((String[]) vecInvId
                .toArray(new String[iQueryLen]));
        voPara.setSaInvBaseId((String[]) vecInvBaseId
                .toArray(new String[iQueryLen]));
        voPara.setSaCurrId((String[]) vecCurrId.toArray(new String[iQueryLen]));
        voPara.setDaBRate((UFDouble[]) vecBRate
                .toArray(new UFDouble[iQueryLen]));
        voPara.setDaARate((UFDouble[]) vecARate
                .toArray(new UFDouble[iQueryLen]));
        voPara
                .setDOrderDate(PuPubVO.getString_TrimZeroLenAsNull(sDate) == null ? null
                        : new UFDate(sDate));

        //业务类型ID:XY+CZP，业务类型是集团业务类型时，不能用收货库存组织取价，即集团业务类型下，只取管理档案的价格(计划价、参考成本)
        String sBizTypeId = getHeadItem("cbiztype").getValue();
        voPara.setBizTypeId(sBizTypeId);
        
        //价格返回
        RetPoVrmAndParaPriceVO voRetPrice = PoPublicUIClass
                .getVrmOrParaPricesVO(voPara);

        int iPricePolicy = PuTool.getPricePriorPolicy(pk_corp);
        //取得默认价格并重新计算数量关系
        UFDouble dOldPrice = null;
        String sChangedKey = null;
        
//        String[] saCurrId = getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel());
//        HashMap mapRateMny = m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
//        BusinessCurrencyRateUtil bca = m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());        
        for (int i = 0; i < iQueryLen; i++) {
            iRow = ((Integer) vecIndex.elementAt(i)).intValue();
            if(voRetPrice == null || voRetPrice.getPriceAt(i) == null){
                //如果劳务折扣订单行询不到价则设置含税单价为1：详细参见需求“支持有金额但数量为0的劳务、折扣类存货的采购订单可以生成采购发票”
            	sChangedKey = "norgtaxprice";
                if(PuPubVO.getUFDouble_ZeroAsNull(getBodyValueAt(iRow,sChangedKey)) == null){
                	String strBaseId = PuPubVO.getString_TrimZeroLenAsNull(getBodyValueAt(iRow,"cbaseid"));
                	if(strBaseId != null 
                			&& (PuTool.isLabor(strBaseId)|| PuTool.isDiscount(strBaseId))){
    	            	setBodyValueAt(new UFDouble(1.0),iRow,sChangedKey);
    	                calRelation(new BillEditEvent(getBodyItem(sChangedKey).getComponent(), getBodyValueAt(iRow, sChangedKey),sChangedKey, iRow));
                	}
                }
                continue;
            }
            UFDouble dPrice = voRetPrice.getPriceAt(i);
            if (voRetPrice.isSetPriceNoTaxAt(i)) {
                sChangedKey = "noriginalcurprice";
            } else {
                sChangedKey = OrderItemVO
                        .getPriceFieldByPricePolicy(iPricePolicy);
            }

            //与原有值不同才重新计算
            dOldPrice = PuPubVO.getUFDouble_ValueAsValue(getBodyValueAt(iRow,
                    sChangedKey));
            if (dOldPrice == null || dPrice.compareTo(dOldPrice) != 0) {
                setBodyValueAt(dPrice, iRow, sChangedKey);
                //重新计算数量关系
                calRelation(new BillEditEvent(getBodyItem(sChangedKey).getComponent(), getBodyValueAt(iRow, sChangedKey),sChangedKey, iRow));
            }
        }
    }

    /**
     * 作者：王印芬 功能：走默认的价格路线 该函数由setCntRelated(int,int)调用 参数： int iBeginRow
     * 需计算默认价格信息的表体开始行 int iEndRow 需计算默认价格信息的表体结束行 返回：无 例外：无 日期：(2002-5-15
     * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-06-19 WYF 去掉缓存 2002-11-19 WYF
     * 修正一个错误，该错误在iBeginRow==iEndRow时不能循环
     */
    private void setDefaultPrice(int iBeginRow, int iEndRow) {

        Vector vecRow = new Vector();
        for (int i = iBeginRow; i <= iEndRow; i++) {
            vecRow.add(new Integer(i));
        }
        setDefaultPrice((Integer[]) vecRow.toArray(new Integer[vecRow.size()]));
    }

    /**
     * 作者：WYF 功能：设置修订订单的默认选项 参数：无 返回： 例外： 日期：(2003-9-9 19:42:01)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setDefaultReviseItems(OrderVO voOrder) {

        //版本号自动加1
        setHeadItem("nversion", new Integer(voOrder.getHeadVO().getNversion()
                .intValue() + 1));
        //修订日期设为当前日期
        setHeadItem("drevisiondate", PoPublicUIClass.getLoginDate());
    }

    /**
     * 作者：王印芬 功能：行粘贴 参数：无 返回：无 例外：无 日期：(2001-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     * 2002-09-20 wyf 恢复对（上层）来源ID及来源行ID的复制 2002-11-25 wyf 对TS赋空 2003-02-27 wyf
     * 修改为：对所有复制的行进行项目清空及编辑性控制
     */
    private void setDefaultValueToPastedLines(int iBeginRow, int iEndRow) {

        for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
            //复制新行缺省值
            setBodyValueAt(null, iRow, "corderid");
            setBodyValueAt(null, iRow, "corder_bid");

            /*
             * //数据库来源 setBodyValueAt(null, iRow, "csourcebilltype");
             * setBodyValueAt(null, iRow, "csourcebillid"); setBodyValueAt(null,
             * iRow, "csourcerowid"); setBodyValueAt(null, iRow,
             * "cupsourcebilltype"); setBodyValueAt(null, iRow,
             * "cupsourcebillid"); setBodyValueAt(null, iRow,
             * "cupsourcebillrowid");
             *
             * //界面来源 setBodyValueAt(null, iRow, "csourcebillname") ;
             * setBodyValueAt(null, iRow, "csourcebillcode") ;
             * setBodyValueAt(null, iRow, "csourcebillrowno") ;
             * setBodyValueAt(null, iRow, "cancestorbillname") ;
             * setBodyValueAt(null, iRow, "cancestorbillcode") ;
             * setBodyValueAt(null, iRow, "cancestorbillrowno") ;
             *
             * //增加属性来源 setBodyValueAt(null, iRow, "cupsourcehts");
             * setBodyValueAt(null, iRow, "cupsourcebts");
             */
            //累计数量
            setBodyValueAt(null, iRow, "naccumarrvnum");
            setBodyValueAt(null, iRow, "naccumstorenum");
            setBodyValueAt(null, iRow, "naccumwastnum");
            setBodyValueAt(null, iRow, "naccuminvoicenum");
            setBodyValueAt(null, iRow, "nconfirmnum");
            setBodyValueAt(null, iRow, "nbackarrvnum");
            setBodyValueAt(null, iRow, "nbackstorenum");
            setBodyValueAt(null, iRow, "naccumrpnum");

            setBodyValueAt(null, iRow, "ccorrectrowid");
            setBodyValueAt(null, iRow, "dconfirmdate");
            setBodyValueAt(null, iRow, "dcorrectdate");
            setBodyValueAt(null, iRow, "vvendorordercode");
            setBodyValueAt(null, iRow, "vvendororderrow");

            setBodyValueAt(null, iRow, "coperator");

            //表体字段置缺省值
            setBodyValueAt(OrderItemVO.FORDERROWSTATUS_FREE, iRow,
                    "forderrowstatus");
            setBodyValueAt(OrderItemVO.IISACTIVE_ACTIVE, iRow, "iisactive");

            //setBodyValueAt(OrderItemVO.IISREPLENISH_NO,iRow,"iisreplenish");
            UFBoolean bReplenish = PuPubVO.getUFBoolean_NullAs(getHeadItem(
                    "bisreplenish").getValue(), OrderHeaderVO.BISREPLENISH_NO);
            setBodyValueAt(
                    bReplenish.equals(OrderHeaderVO.BISREPLENISH_NO) ? OrderItemVO.IISREPLENISH_NO
                            : OrderItemVO.IISREPLENISH_YES, iRow,
                    "iisreplenish");

            //V3增加
            setBodyValueAt(OrderItemVO.BRECEIVEPLAN_NO, iRow, "breceiveplan");

            setBodyValueAt(null, iRow, "ts");

        }

    }
    /**
     * 作者：王印芬 功能：对备注是空的设置空串 参数：无 返回：无 例外：无 日期：(2003-10-15 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setDefaultValueToVmemo() {
        if (((UIRefPane) getHeadItem("vmemo").getComponent()).getText() == null) {
            ((UIRefPane) getHeadItem("vmemo").getComponent()).setText("");
        }

        int iRowCount = getRowCount();
        for (int i = 0; i < iRowCount; i++) {
            if (getBodyValueAt(i, "vmemo") == null) {
                setBodyValueAt("  ", i, "vmemo");
            }
        }
    }

    /**
     * 作者：晁志平 功能：对表头、体的“供应商收发货地址”及表体“收发货地址”是空的设置空串 参数：无 返回：无 例外：无 日期：(2004-11-17 15:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setDefaultValueToAddr() {
        if (((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).getText() == null) {
            ((UIRefPane) getHeadItem("cdeliveraddress").getComponent()).setText("");
        }

        int iRowCount = getRowCount();
        for (int i = 0; i < iRowCount; i++) {
            if (getBodyValueAt(i, "vvenddevaddr") == null) {
                setBodyValueAt("  ", i, "vvenddevaddr");
            }
            if (getBodyValueAt(i, "vreceiveaddress") == null) {
                setBodyValueAt("  ", i, "vreceiveaddress");
            }
        }
    }

    /**
     * 作者：王印芬 功能：设定合同相关属性是否可修改 参数：无 返回：无 例外：无 日期：(2002-5-13 11:39:21)
     * 修改日期，修改人，修改原因，注释标志： 2002-05-17 wyf 从合同过来的单据订单日期不可修改 2002-05-29 wyf
     * 加入与合同相关信息的表体的编辑控制属性
     */
    private void setEnabled_Body(int iRow) {
        //=========设定可编辑性
        int iRowCount = getRowCount();
        if (iRowCount == 0) {
            return;
        }
        //合同相关（合同编码、单价、币种）
        setEnabled_BodyCntRelated(iRow);
        //汇率
        setEnabled_BodyExchangeRate(iRow);
        //批次号
        PoEditTool.setCellEditable_VproduceNum(getBillModel(), iRow, "cmangid","vproducenum");
        //辅计量相关
        PoEditTool.setCellEditable_AssistUnitRelated(getBillModel(), iRow,new String[] { "cbaseid", "cassistunit", "cassistunitname","nassistnum", "nconvertrate" });
        //自由项
//        PoEditTool.setCellEditable_Vfree(getBillModel(), iRow, "cmangid","vfree");
        //项目阶段
        setEnabled_BodyProjectPhase(iRow);
    }
     /**
     * 作者：王印芬 功能：设定合同相关属性是否可修改 参数：int iRow 需设定可编辑性的行 返回：无 例外：无 日期：(2002-5-13
     * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-17 wyf 从合同过来的单据订单日期不可修改 2002-05-29
     * wyf 加入与合同相关信息的表体的编辑控制属性 2003-04-14 wyf 修改没有存货时的处理
     */
    private void setEnabled_BodyCntRelated(int iRow) {

        //价格优先设置 
        int iPricePolicy = PuTool.getPricePriorPolicy(getCorp());
        //是否从合同来
        String sUpSourceType = (String) getBodyValueAt(iRow,
                "cupsourcebilltype");
        String sCntRowId = (String) getBodyValueAt(iRow, "ccontractrowid");
        //上层来源不是合同
        if (sUpSourceType != null
                && (sUpSourceType.equals(BillTypeConst.CT_ORDINARY) || sUpSourceType
                        .equals(BillTypeConst.CT_BEFOREDATE))) {

            setCellEditable(iRow, "ccontractcode", false);
            RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(sCntRowId);
            //存货
            PoPublicUIClass.setInvEditableByPricePolicy(this, iRow, voCntInfo);
            //单价
            PoPublicUIClass.setPriceEditableByPricePolicy(this, iRow,
                    voCntInfo, iPricePolicy);
            //币种
            setCellEditable(iRow, "ccurrencytype", false);
        } else {
            //没有存货
            String sMangId = (String) getBodyValueAt(iRow, "cmangid");
            if (PuPubVO.getString_TrimZeroLenAsNull(sMangId) == null) {
                setCellEditable(iRow, "cinventorycode", getBodyItem("cinventorycode") != null && getBodyItem("cinventorycode").isEdit());
                setCellEditable(iRow, "ccontractcode", false);
                setCellEditable(iRow, "ccurrencytype", getBodyItem("ccurrencytype") != null && getBodyItem("ccurrencytype").isEdit());
                setCellEditable(iRow, "noriginalcurprice", getBodyItem("noriginalcurprice") != null && getBodyItem("noriginalcurprice").isEdit());
                setCellEditable(iRow, "norgtaxprice",getBodyItem("norgtaxprice") != null && getBodyItem("norgtaxprice").isEdit());
                return;
            }

            //=====存货可编辑
            setCellEditable(iRow, "cinventorycode", getBodyItem("cinventorycode") != null && getBodyItem("cinventorycode").isEdit());

            //=====合同相关
            //不与合同相关
            RetCtToPoQueryVO voCntInfo = PoPublicUIClass.getCntInfo(sCntRowId);
            if (voCntInfo == null) {
                setCellEditable(iRow, "ccontractcode", getBodyItem("ccontractcode") != null && getBodyItem("ccontractcode").isEdit());
                setCellEditable(iRow, "ccurrencytype", getBodyItem("ccurrencytype") != null && getBodyItem("ccurrencytype").isEdit());
                setCellEditable(iRow, "noriginalcurprice", getBodyItem("noriginalcurprice") != null && getBodyItem("noriginalcurprice").isEdit());
                setCellEditable(iRow, "norgtaxprice", getBodyItem("norgtaxprice") != null && getBodyItem("norgtaxprice").isEdit());
            } else {
                //合同参照
                setCellEditable(iRow, "ccontractcode", getBodyItem("ccontractcode") != null && getBodyItem("ccontractcode").isEdit());
                //币种
                setCellEditable(iRow, "ccurrencytype", false);
                //单价
                PoPublicUIClass.setPriceEditableByPricePolicy(this, iRow,
                        voCntInfo, iPricePolicy);
            }
        }

    }

    /**
     * 作者：王印芬 功能：设定汇率的可编辑性 参数：int indRow 需设定可编辑性的行 返回：无 例外：无 日期：(2002-6-25
     * 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void setEnabled_BodyExchangeRate(int iRow) {

        //=========表体
        String pk_corp = (String) getHeadItem("pk_corp").getValue();
        String sCurrId = (String) getBodyValueAt(iRow, "ccurrencytypeid");
        boolean[] baEditable = m_cardPoPubSetUI2.getBothExchRateEditable(pk_corp,
                sCurrId);

        //2006-02-23   Czp    V5修改：考虑自定义模板中的可编辑性设置 
        boolean bEditUserDef0 = getBillModel().getItemByKey("nexchangeotobrate").isEdit();
        boolean bEditUserDef1 = getBillModel().getItemByKey("nexchangeotoarate").isEdit();
        
        if (baEditable != null) {
            getBillModel().setCellEditable(iRow, "nexchangeotobrate",baEditable[0] & bEditUserDef0);
            getBillModel().setCellEditable(iRow, "nexchangeotoarate",baEditable[1] & bEditUserDef1);
        }
    }

    /**
     * 作者：WYF 功能：设置项目阶段的可编辑性 参数：int iRow 具体行 返回：无 例外：无 日期：(2004-6-22 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setEnabled_BodyProjectPhase(int iRow) {

        //2006-02-23   Czp    V5修改：考虑自定义模板中的可编辑性设置 
        boolean bEditUserDef = getBillModel().getItemByKey("cprojectphase").isEdit();
        //项目阶段
        String sProjectId = (String) getBodyValueAt(iRow, "cprojectid");
        if (sProjectId == null || sProjectId.trim().equals("")) {
            getBillModel().setCellEditable(iRow, "cprojectphase", false);
        } else {
            getBillModel().setCellEditable(iRow, "cprojectphase", bEditUserDef);
        }
    }

    /**
     * 作者：王印芬 功能：设定表头的可编辑性 目前该函数只调用一个函数，为了便于扩展， 并与函数setEnabled_BodyCells保持对称
     * 参数：无 返回：无 例外：无 日期：(2002-6-25 11:39:21) 修改日期，修改人，修改原因，注释标志：
     * 
     * <p>2006-05-22,Czp,V5支持用户定义单据模板上的“是否可修订”属性的定义
     */
    private void setEnabled_Head(boolean bRev) {

    	//since v501, 支持自由状态的单据号修改时可编辑(按用户定义)
    	if(bRev){
    		setEnabled_HeadOrderCode();
    	}
        setEnabled_HeadCntRelated(bRev);
        setEnabled_HeadFreeCust(bRev);
        setEnabled_HeadExchangeRate(bRev);
        setEnabled_HeadAccount(bRev);
        setEnabled_HeadBizType();

        //---------V31增加:

	 //补货订单不退货
        getHeadItem("breturn").setEnabled(!isBisreplenish() && getHeadItem("breturn").isEdit());
//	 //“是否发运”为“是”，“发运方式”才可编辑
//	 getHeadItem("ctransmodeid").setEnabled(isBdeliver());
    }

    /**
     * 作者：王印芬 功能：设定散户的可编辑性 参数：无 返回：无 例外：无 日期：(2002-6-25 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setEnabled_HeadAccount(boolean bRev) {
        //供应商及散户至少存在一个
        String sVendId = ((UIRefPane) (getHeadItem("cvendormangid")
                .getComponent())).getRefPK();
        String sFreeCustId = ((UIRefPane) (getHeadItem("cfreecustid")
                .getComponent())).getRefPK();
        if (PuPubVO.getString_TrimZeroLenAsNull(sVendId) == null
                && PuPubVO.getString_TrimZeroLenAsNull(sFreeCustId) == null) {
            getHeadItem("caccountbankid").setEnabled(false);
        } else {
        	if(bRev){
                getHeadItem("caccountbankid").setEnabled(getHeadItem("caccountbankid").isM_bReviseFlag() && getHeadItem("caccountbankid").isEdit());

        	}else{
        		getHeadItem("caccountbankid").setEnabled(getHeadItem("caccountbankid").isEdit());
        	}
        }
    }

    /**
     * 作者：王印芬 功能：设定业务类型的可编辑性 参数：无 返回：无 例外：无 日期：(2003-10-17 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setEnabled_HeadBizType() {

        if (PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("cbiztype")
                .getValue()) == null) {
            getHeadItem("cbiztype").setEnabled(getHeadItem("cbiztype").isEdit());
        } else {
            getHeadItem("cbiztype").setEnabled(false);
        }
    }
     /**
     * 作者：晁志平 功能：判断“是否补货” 参数：无 返回：无 例外：无 日期：(2004-12-16 12:09:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private boolean isBisreplenish() {

        String strVal = PuPubVO.getString_TrimZeroLenAsNull(getHeadItem(
                "bisreplenish").getValue());

        return (strVal != null && "true".equalsIgnoreCase(strVal));
    }
    /**
     * 作者：王印芬 功能：设定表头订单日期、供应商的可编辑性 参数：无 返回：无 例外：无 日期：(2002-6-25 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setEnabled_HeadCntRelated(boolean bRev) {

        //是否从合同来
        boolean bFrmCnt = false;
        for (int i = 0; i < getRowCount(); i++) {
            String sUpSourceType = (String) getBodyValueAt(i,
                    "cupsourcebilltype");
            //上层来源不是合同
            if (sUpSourceType != null
                    && (sUpSourceType.equals(BillTypeConst.CT_ORDINARY) || sUpSourceType
                            .equals(BillTypeConst.CT_BEFOREDATE))) {
                bFrmCnt = true;
                break;
            }
        }
        //=========表头 供应商不可编辑
        if(bRev){
	        getHeadItem("cvendormangid").setEnabled(!bFrmCnt && getHeadItem("cvendormangid").isM_bReviseFlag() && getHeadItem("cvendormangid").isEdit());
	        getHeadItem("dorderdate").setEnabled(!bFrmCnt && getHeadItem("dorderdate").isM_bReviseFlag() && getHeadItem("dorderdate").isEdit());
        }else{
	        getHeadItem("cvendormangid").setEnabled(!bFrmCnt && getHeadItem("cvendormangid").isEdit());
	        getHeadItem("dorderdate").setEnabled(!bFrmCnt && getHeadItem("dorderdate").isEdit());
        }
    }

    /**
     * 作者：王印芬 功能：设定表头汇率的可编辑性 参数：无 返回：无 例外：无 日期：(2002-6-25 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setEnabled_HeadExchangeRate(boolean bRev) {

        //=========表体
        String pk_corp = (String) getHeadItem("pk_corp").getValue();
        String sCurrId = (String) getHeadItem("ccurrencytypeid").getValue();
        boolean[] baEditable = m_cardPoPubSetUI2.getBothExchRateEditable(pk_corp,
                sCurrId);
        if (baEditable != null) {
        	if(bRev){
                getHeadItem("nexchangeotobrate").setEnabled(baEditable[0] && getHeadItem("nexchangeotobrate").isM_bReviseFlag() && getHeadItem("nexchangeotobrate").isEdit());
                getHeadItem("nexchangeotoarate").setEnabled(baEditable[1] && getHeadItem("nexchangeotoarate").isM_bReviseFlag() && getHeadItem("nexchangeotoarate").isEdit());
        	}else{
	            getHeadItem("nexchangeotobrate").setEnabled(baEditable[0] && getHeadItem("nexchangeotobrate").isEdit());
	            getHeadItem("nexchangeotoarate").setEnabled(baEditable[1] && getHeadItem("nexchangeotoarate").isEdit());
        	}
        }
    }

    /**
     * 作者：王印芬 功能：设定开户银行的可编辑性 参数：无 返回：无 例外：无 日期：(2003-10-15 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setEnabled_HeadFreeCust(boolean bRev) {
        //供应商参照
        UIRefPane ref = (UIRefPane) (getHeadItem("cvendormangid")
                .getComponent());
        String cvendormangid = ref.getRefPK();
        //散户
        String cfreecustid = getHeadItem("cfreecustid").getValue();
        if ((cvendormangid == null)
                || (cvendormangid.toString().trim().equals(""))) {
            ((UIRefPane) (getHeadItem("cfreecustid").getComponent()))
                    .setPK(null);
            getHeadItem("cfreecustid").setEnabled(false);
            if (cfreecustid != null) {
                getHeadItem("caccountbankid").setValue(null);
            }
        } else {
            try {
                PoVendorVO vendorVO = null;
                vendorVO = PoPublicUIClass.getVendDefaultInfo(cvendormangid);
                if (vendorVO == null) {
                    ((UIRefPane) (getHeadItem("cfreecustid").getComponent()))
                            .setPK(null);
                    getHeadItem("cfreecustid").setEnabled(false);
                    if (cfreecustid != null) {
                        getHeadItem("caccountbankid").setValue(null);
                    }
                } else {
                    String freecustflag = vendorVO.getFreecustflag();
                    if ((freecustflag == null)
                            || (freecustflag.trim().equals(""))
                            || (freecustflag.trim().toUpperCase().equals("N"))) {
                        ((UIRefPane) (getHeadItem("cfreecustid").getComponent()))
                                .setPK(null);
                        getHeadItem("cfreecustid").setEnabled(false);
                        if (cfreecustid != null) {
                            getHeadItem("caccountbankid").setValue(null);
                        }
                    } else {
                    	if(bRev){
                    		getHeadItem("cfreecustid").setEnabled(getHeadItem("cfreecustid").isM_bReviseFlag());
                    	}else{
                    		getHeadItem("cfreecustid").setEnabled(getHeadItem("cfreecustid").isEdit());
                    	}
                    }
                }
            } catch (Exception e) {
                ((UIRefPane) (getHeadItem("cfreecustid").getComponent()))
                        .setPK(null);
                getHeadItem("cfreecustid").setEnabled(false);
                if (cfreecustid != null) {
                    getHeadItem("caccountbankid").setValue(null);
                }
            }
        }
    }

    /**
     * 作者：王印芬 功能：设定订单编号的可编辑性 参数：无 返回：无 例外：无 日期：(2003-9-9 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setEnabled_HeadOrderCode() {
        String sId = PuPubVO
                .getString_TrimZeroLenAsNull(getHeadItem("corderid").getValue());
        boolean bEditFlag = getHeadItem("vordercode").isEdit();
        getHeadItem("vordercode").setEnabled(sId == null ? bEditFlag : false);
    }

    /**
     * 作者：王印芬 功能：设置表体折本及折辅汇率的值并设置其可编辑性 参数： int iRow boolean bResetExchValue
     * 是否需要重新设置表体行的汇率值 返回：无 例外：无 日期：(2002-5-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void setExchangeRateBody(int iRow, boolean bResetExchValue) {

        String dOrderDate = getHeadItem("dorderdate").getValue();
        String sCurrId = (String) getBodyValueAt(iRow, "ccurrencytypeid");
        if(sCurrId == null || sCurrId.trim().length() == 0){
            sCurrId = getHeadItem("ccurrencytypeid").getValue();
        }
        //首先设置显示精度
        setRowDigits_ExchangeRate(getCorp(), iRow, getBillModel(),m_cardPoPubSetUI2);
        //值
        if (bResetExchValue && dOrderDate != null
                && dOrderDate.trim().length() > 0) {
            UFDouble[] daRate = null;
            String strCurrDate = dOrderDate;
            if(strCurrDate == null || strCurrDate.trim().length() == 0){
                strCurrDate = PoPublicUIClass.getLoginDate()+"";
            }
            daRate = m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),sCurrId, new UFDate(dOrderDate));
            setBodyValueAt(daRate[0], iRow, "nexchangeotobrate");
            setBodyValueAt(daRate[1], iRow, "nexchangeotoarate");
        }
        //可编辑性        
        boolean[] baEditable = null;
        
        baEditable = m_cardPoPubSetUI2.getBothExchRateEditable(getCorp(),sCurrId);

        //2006-02-23   Czp    V5修改：考虑自定义模板中的可编辑性设置 
        boolean bEditUserDef0 = getBillModel().getItemByKey("nexchangeotobrate").isEdit();
        boolean bEditUserDef1 = getBillModel().getItemByKey("nexchangeotoarate").isEdit();
        getBillModel().setCellEditable(iRow, "nexchangeotobrate", baEditable[0] & bEditUserDef0);
        getBillModel().setCellEditable(iRow, "nexchangeotoarate", baEditable[1] & bEditUserDef1);

        //设置修改标志
        getBillModel().setRowState(iRow, BillModel.MODIFICATION);
    }

    /**
     * 作者：王印芬 功能：设置表头币种及汇率的可编辑性 参数：无 返回：无 例外：无 日期：(2002-5-13 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setExchangeRateHead(String dorderdate, String sCurrId) {

        sCurrId = (sCurrId == null || sCurrId.trim().length() == 0) ? null
                : sCurrId;
        //首先设置显示精度
        int[] iaExchRateDigit = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),
                sCurrId);
        getHeadItem("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
        getHeadItem("nexchangeotoarate").setDecimalDigits(iaExchRateDigit[1]);
        //设置值
        UFDouble[] daRate = m_cardPoPubSetUI2.getBothExchRateValue(getCorp(), sCurrId,
                new UFDate(dorderdate));
        getHeadItem("nexchangeotobrate").setValue(daRate[0]);
        getHeadItem("nexchangeotoarate").setValue(daRate[1]);
        //}
        //可编辑性
        boolean[] iaEditable = m_cardPoPubSetUI2.getBothExchRateEditable(getCorp(),
                sCurrId);
        getHeadItem("nexchangeotobrate").setEnabled(iaEditable[0] && getHeadItem("nexchangeotobrate").isEdit());
        getHeadItem("nexchangeotoarate").setEnabled(iaEditable[1] && getHeadItem("nexchangeotoarate").isEdit());

    }

    /**
     * 作者：王印芬 功能：设置表头精度 参数：String pk_corp 公司ID 返回：无 例外：无 日期：(2003-9-4 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setHeadDigits(String pk_corp) {
        //预付款限额、预付款
        int iDigit = 2;
        try {
            iDigit = m_cardPoPubSetUI2.getMoneyDigitByCurr_Finance(m_cardPoPubSetUI2
                    .getCurrArith_Finance(pk_corp).getLocalCurrPK());
        } catch (Exception e) {
            PuTool.outException(this, e);
        }

        //本币财务精度金额类
        int iLen = OrderHeaderVO.getDbMnyFields_Local_Finance().length;
        for (int i = 0; i < iLen; i++) {
            BillItem item = getHeadItem(OrderHeaderVO
                    .getDbMnyFields_Local_Finance()[i]);
            if (item != null) {
                item.setDecimalDigits(iDigit);
            }
        }

        //版本
        BillItem item3 = getHeadItem("nversion");
        if (item3 != null) {
            item3.setDecimalDigits(1);
        }
    }
    /**
     * 处理是否保留最初制单人参数，如果为“Y”则不修改，否则修改制单人为当前登录的操作员
     * **/
    private void setOperatorValue(){
        if (!BillTypeConst.PO_ORDER.equals(getBillType())){
            SCMEnv.out("非维护订单，不修改制单人!");/*-=notranslate=-*/
            return ;
        }
        String strParaVal = null;
        try{
            strParaVal= SysInitBO_Client.getParaString(PoPublicUIClass.getLoginPk_corp(),"PO060");
        }catch(Exception e){
            SCMEnv.out("读取参数时出现异常，不修改制单人!");/*-=notranslate=-*/
            return;
        }
        if ("Y".equalsIgnoreCase(strParaVal)){
            SCMEnv.out("是否保留最初制单人参数为‘Y’，不修改制单人!");/*-=notranslate=-*/
            return ;
        }
        //修改制单人
        setTailItem("coperator", PoPublicUIClass.getLoginUser());
    }
    /**
     * 作者：王印芬 功能：设置某行的项目信息与另一行相似 参数：int iCurRow 需设置项目信息的行 int iRefRow 参考行 返回：无
     * 例外：无 日期：(2003-05-03 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void setProjectInfoAs(int iCurRow, int iRefRow) {

        //if (iCurRow<0 || iRefRow<0 ) {
        //return ;
        //}

        String sPreviousProject = (String) getBillModel().getValueAt(iRefRow,
                "cprojectid");
        String sCurProject = (String) getBillModel().getValueAt(iCurRow,
                "cprojectid");
        sPreviousProject = (sPreviousProject == null
                || sPreviousProject.trim().equals("") ? sPreviousProject = "NULL"
                : sPreviousProject);
        sCurProject = (sCurProject == null || sCurProject.trim().equals("") ? sCurProject = "NULL"
                : sCurProject);
        if (sCurProject.equals(sPreviousProject)) {
            getBillModel().setValueAt(
                    getBillModel().getValueAt(iRefRow, "cprojectphaseid"),
                    iCurRow, "cprojectphaseid");
            getBillModel().setValueAt(
                    getBillModel().getValueAt(iRefRow, "cproject"), iCurRow,
                    "cproject");
            getBillModel().setValueAt(
                    getBillModel().getValueAt(iRefRow, "cprojectphase"),
                    iCurRow, "cprojectphase");
        }
    }

    /**
     * 作者：WYF 功能：设置参照的可用性。 参数：无 返回：无 例外：无 日期：(2003-10-08 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setRefPane() {

        setRefPane_Head();
        setRefPane_Body();
        setRefPane_Tail();
    }

    /**
     * 作者：WYF 功能：设置参照的可用性。 参数：无 返回：无 例外：无 日期：(2003-10-08 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setRefPane_Body() {

        //==================表头
        int iLen = getRefItemKeysBody().length;
        for (int i = 0; i < iLen; i++) {
            setRefPane_Body(getRefItemKeysBody()[i]);
        }
    }

    /**
     * 作者：WYF 功能：设置参照的可用性。 参数：无 返回：无 例外：无 日期：(2003-10-08 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setRefPane_Body(String sItemKey) {

        //==================表体
        UIRefPane refpane = null;

        if ("cinventorycode".equals(sItemKey)) {
            //存货
            refpane = (UIRefPane) getBodyItem("cinventorycode").getComponent();
            refpane.setTreeGridNodeMultiSelected(true);
            refpane.setMultiSelectedEnabled(true);
            refpane.setRefModel(new PoInputInvRefModel(getCorp(), null));
        } else if ("cassistunitname".equals(sItemKey)) {
            refpane = (UIRefPane) getBodyItem("cassistunitname").getComponent();
            refpane.setRefModel(new OtherRefModel("辅计量单位"));/*-=notranslate=-*/
        } else if ("ccurrencytype".equals(sItemKey)) {
            refpane = (UIRefPane) getBodyItem("ccurrencytype").getComponent();
            AbstractRefModel refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
        } else if ("cusedept".equals(sItemKey)) {
            //部门
        	refpane = (UIRefPane) getBodyItem("cusedept").getComponent();
        	AbstractRefModel refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
        } else if ("cproject".equals(sItemKey)) {
            //项目
        	JobmngfilDefaultRefModel refModel = new JobmngfilDefaultRefModel("项目管理档案");
            refModel.setPk_corp(getCorp());
            refModel
                    .addWherePart(" AND UPPER(ISNULL(bd_jobbasfil.sealflag,'N')) = 'N'");
        	refpane = (UIRefPane) getBodyItem("cproject").getComponent();
            refpane.setRefModel(refModel);
        } else if ("cprojectphase".equals(sItemKey)) {
            refpane = (UIRefPane) getBodyItem("cprojectphase").getComponent();
            refpane.setRefModel(new PuProjectPhaseRefModel(getCorp()));
        } 
        /*V5：放编辑前处理
        else if ("cwarehouse".equals(sItemKey)) {
            //设置仓库参照，过滤掉废品库
            PuTool.restrictWarehouseRefByStoreOrg(this, getCorp(), getHeadItem(
                    "cstoreorganization").getValue(), "cwarehouse");
        
        } */
        else if ("vmemo".equals(sItemKey)) {
            //备注
        	ComAbstrDefaultRefModel refModelCom = new ComAbstrDefaultRefModel("常用摘要");
        	refModelCom.setPk_corp(getCorp());
//            refModel.setRefNodeName("常用摘要");/*-=notranslate=-*/

            refpane = (UIRefPane) getBodyItem("vmemo").getComponent();
            refpane.setRefModel(refModelCom);

//            //备注
//            DefaultRefModel refModel = new DefaultRefModel();
//            refModel.setPk_corp(getCorp());
//            refModel.setRefNodeName("常用摘要");/*-=notranslate=-*/
//
//            refpane = (UIRefPane) getBodyItem("vmemo").getComponent();
//            refpane.setRefModel(refModel);
        }
        //收发货地址:受限于收货方
        else if ("vreceiveaddress".equals(sItemKey)) {
            String sReveivePk = ((UIRefPane) (getHeadItem("creciever")
                    .getComponent())).getRefPK();
            refpane = (UIRefPane)  getBodyItem("vreceiveaddress").getComponent();
            refpane.setRefModel(new PoReceiveAddrRefModel(getCorp(),
                        PuPubVO.getString_TrimZeroLenAsNull(sReveivePk)));
        }
        //客商收发货地址:受限于供应商
        else if ("vvenddevaddr".equals(sItemKey)) {
            String sVendorMangPk = ((UIRefPane) (getHeadItem("cvendormangid").getComponent())).getRefPK();
            refpane = (UIRefPane)  getBodyItem("vvenddevaddr").getComponent();
            refpane.setRefModel(new PoReceiveAddrRefModel(getCorp(),
                    PuPubVO.getString_TrimZeroLenAsNull(sVendorMangPk)));
        }
        if (refpane != null) {
            refpane.setPk_corp(getCorp());
        }
    }

    /**
     * 作者：WYF 功能：设置表头参照 参数：无 返回：无 例外：无 日期：(2003-10-08 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setRefPane_Head() {

        //==================表头
        int iLen = getRefItemKeysHead().length;
        for (int i = 0; i < iLen; i++) {
            setRefPane_Head(getRefItemKeysHead()[i]);
        }
    }

    /**
     * 作者：WYF 功能：设置表头参照 参数：无 返回：无 例外：无 日期：(2003-10-08 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setRefPane_Head(String sItemKey) {

        //==================表头
    	UIRefPane refpane = null;
    	AbstractRefModel refModel = null;
        if ("cprojectid".equals(sItemKey)) {
            //项目 cprojectid
        	refpane = (UIRefPane) getHeadItem("cprojectid").getComponent();
        	refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
            refModel
                    .addWherePart(" AND UPPER(ISNULL(bd_jobbasfil.sealflag,'N')) = 'N'");

        } else if ("ctermprotocolid".equals(sItemKey)) {
            //付款协议 ctermprotocolid
            refpane = (UIRefPane) getHeadItem("ctermprotocolid").getComponent();
            refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
        } else if ("ctransmodeid".equals(sItemKey)) {
            //发运方式 ctransmodeid
        	refpane = (UIRefPane) getHeadItem("ctransmodeid").getComponent();
        	refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
            refModel.setRefNodeName("发运方式");/*-=notranslate=-*/
        } 
        /*V5 Del:*/
//        else if ("cstoreorganization".equals(sItemKey)) {
//            //库存组织 cstoreorganization
//            DefaultRefModel refModel = new DefaultRefModel();
//            refModel.setPk_corp(getCorp());
//            refModel.setRefNodeName("库存组织");/*-=notranslate=-*/
//
//            refpane = (UIRefPane) getHeadItem("cstoreorganization")
//                    .getComponent();
//            refpane.setRefModel(refModel);
//        } 
    	else if ("cpurorganization".equals(sItemKey)) {
            //采购组织 cpurorganization
            refpane = (UIRefPane) getHeadItem("cpurorganization").getComponent();
            refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
            refModel.addWherePart(" and ownercorp = '" + PoPublicUIClass.getLoginPk_corp() + "' ");
        } else if ("cbiztype".equals(sItemKey)) {
            //业务类型 pk_corp
            refpane = (UIRefPane) getHeadItem("cbiztype").getComponent();
            refpane.setRefModel(new PuBizTypeRefModel(getCorp(),
                    BillTypeConst.PO_ORDER));
        } else if ("ccurrencytypeid".equals(sItemKey)) {
            //币种

            refpane = (UIRefPane) getHeadItem("ccurrencytypeid").getComponent();
        	refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
        } else if ("cunfreeze".equals(sItemKey)) {
            //解冻人 cunfreeze
            refpane = (UIRefPane) getHeadItem("cunfreeze").getComponent();
            refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
        } else if ("pk_corp".equals(sItemKey)) {
            //公司 pk_corp
            refpane = (UIRefPane) getHeadItem("pk_corp").getComponent();
        	refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
        } else if ("caccountbankid".equals(sItemKey)) {
            //银行
            refpane = (UIRefPane) getHeadItem("caccountbankid").getComponent();
            if (PuPubVO.getString_TrimZeroLenAsNull(getHeadItem("cfreecustid")
                    .getValue()) != null) {
                refpane.setRefModel(new PoFreeCustBankRefModel(getHeadItem(
                        "cfreecustid").getValue()));
            } else {
                refpane.setRefModel(new PoCustBankRefModel(getHeadItem(
                        "cvendorbaseid").getValue()));
            }
        } else if ("cfreecustid".equals(sItemKey)) {
            ////散户
        } else if ("cgiveinvoicevendor".equals(sItemKey)) {
            //发票方
            refpane = (UIRefPane) getHeadItem("cgiveinvoicevendor")
            .getComponent();
            refModel = refpane.getRefModel(); 
            refModel.setPk_corp(getCorp());
            refModel.setRefNameField("bd_cubasdoc.custshortname");
        } else if ("cvendormangid".equals(sItemKey)) {
            //供应商
            refpane = (UIRefPane) getHeadItem("cgiveinvoicevendor")
            .getComponent();
            refModel = refpane.getRefModel(); 
            refModel.setPk_corp(getCorp());
            refModel.setRefNameField("bd_cubasdoc.custshortname");
        } else if ("creciever".equals(sItemKey)) {
            //收货方
        	refpane = (UIRefPane) (getHeadItem("creciever").getComponent());
        	refModel = refpane.getRefModel();
            refModel.setPk_corp(getCorp());
            refModel.setRefNameField("bd_cubasdoc.custshortname");
        } else if ("cdeptid".equals(sItemKey)) {
            //部门
            refpane = (UIRefPane) getHeadItem("cdeptid").getComponent();
            refpane.setRefModel(new PurDeptRefModel(getCorp()));
        } else if ("cemployeeid".equals(sItemKey)) {
            //业务员
            /* 这样设置导致问题：录入非本部门业务员时带不出相应部门
            refpane = (UIRefPane) getHeadItem("cemployeeid").getComponent();
            refpane.setRefModel(new PurPsnRefModel(getCorp(), getHeadItem(
                    "cdeptid").getValue()));
            */
            refpane = (UIRefPane) getHeadItem("cemployeeid").getComponent();
            refpane.setRefModel(new PurPsnRefModel(getCorp(), null));
        } else if ("vmemo".equals(sItemKey)) {
            //备注
        	ComAbstrDefaultRefModel refModelCom = new ComAbstrDefaultRefModel("常用摘要");
        	refModelCom.setPk_corp(getCorp());
//            refModel.setRefNodeName("常用摘要");/*-=notranslate=-*/

            refpane = (UIRefPane) getHeadItem("vmemo").getComponent();
            refpane.setRefModel(refModelCom);
            refpane.setButtonVisible(true);
//            //备注
//            refpane = new UIRefPane();
//            refpane.setRefNodeName("常用摘要");/*-=notranslate=-*/
//            refModel = refpane.getRefModel();
//            refModel.setPk_corp(getCorp());
//            getHeadItem("vmemo").setComponent(refpane);
//            refpane.setButtonVisible(true);
        }
        else if ("cdeliveraddress".equals(sItemKey)) {
            //供应商收发货地址
            refpane = (UIRefPane) getHeadItem("cdeliveraddress").getComponent();
            refpane.setRefModel(new PoDeliverAddressRefModel(getHeadItem(
                    "cvendorbaseid").getValue()));
        }
        if (refpane != null) {
            refpane.getRefModel().setPk_corp(getCorp());
        }
    }

    /**
     * 作者：WYF 功能：设置表尾参照 参数：无 返回：无 例外：无 日期：(2003-10-14 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setRefPane_Tail() {

        //==================表头
        UIRefPane refpane = null;

        //if ("cauditpsn".equals(sItemKey)) {
        //审批人 coperator
        refpane = (UIRefPane) getTailItem("cauditpsn").getComponent();
//        DefaultRefModel refModelAuditPsn = new DefaultRefModel();
        AbstractRefModel refModelAuditPsn = refpane.getRefModel();
        refModelAuditPsn.setPk_corp(getCorp());
//        refModelAuditPsn.setRefNodeName("操作员");/*-=notranslate=-*/
//        refpane.setRefModel(refModelAuditPsn);
        //}else if ("coperator".equals(sItemKey)) {
        //制单人 coperator
//        DefaultRefModel refModelOper = new DefaultRefModel();
//        refModelOper.setRefNodeName("操作员");/*-=notranslate=-*/

        refpane = (UIRefPane) getTailItem("coperator").getComponent();
        AbstractRefModel refModelOper = refpane.getRefModel();
        refModelOper.setPk_corp(getCorp());
//        refpane.setRefModel(refModelOper);
        //}

        if (refpane != null) {
            refpane.setPk_corp(getCorp());
        }
    }

    /**
     * 作者：王印芬 功能：存货、订单日期、币种修改后相应的最高限价变化 该函数由afterEdit存货、订单日期、币种改变后调用。 参数： int
     * iBeginRow 需计算合同相关信息的表体开始行 int iEndRow 需计算合同相关信息的表体结束行 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2003-11-14 wyf 批量加载
     */
    private void setRelated_AssistUnit(int iBeginRow, int iEndRow) {

        //批量加载
        String[] saBaseId = (String[]) PuGetUIValueTool.getArrayNotNull(
                getBillModel(), "cbaseid", String.class, iBeginRow, iEndRow);
        PuTool.loadBatchAssistManaged(saBaseId);

        //辅计量的行
        Vector vecAssistUnitIndex = new Vector();
        Vector vecBaseId = new Vector();
        Vector vecAssistId = new Vector();

        //计算值
        
        //设置默认辅计量
        String[] aryAssistunit = new String[] {
//                "<formulaset><cachetype>FOREDBCACHE</cachetype></formulaset>" +
                "cassistunit->getColValue(bd_invbasdoc,pk_measdoc2,pk_invbasdoc,cbaseid)",
                "cassistunitname->getColValue(bd_measdoc,measname,pk_measdoc,cassistunit)" };
        getBillModel().execFormulas(aryAssistunit,iBeginRow,iEndRow);
        //
        String sBaseId = null;
        String sAssistUnit = null;
        for (int iRow = iBeginRow; iRow <= iEndRow; iRow++) {
            sBaseId = (String) getBodyValueAt(iRow, "cbaseid");
            if (PuTool.isAssUnitManaged(sBaseId)) {
                sAssistUnit = (String) getBodyValueAt(iRow,"cassistunit");
                //为批量加载作准备
                if (PuPubVO.getString_TrimZeroLenAsNull(sAssistUnit) != null) {
                    vecAssistUnitIndex.add(new Integer(iRow));
                    vecBaseId.add(sBaseId);
                    vecAssistId.add(sAssistUnit);
                }
            } else {
                getBillModel().setValueAt(null, iRow, "cassistunitname");
                getBillModel().setValueAt(null, iRow, "cassistunit");
                getBillModel().setValueAt(null, iRow, "nassistnum");
                getBillModel().setValueAt(null, iRow, "nconvertrate");
            }
        }

        //批量设置辅计量的行
        int iAssistUnitLen = vecAssistUnitIndex.size();
        if (iAssistUnitLen > 0) {

            //批量加载
            PuTool.loadBatchInvConvRateInfo((String[]) vecBaseId
                    .toArray(new String[iAssistUnitLen]),
                    (String[]) vecAssistId.toArray(new String[iAssistUnitLen]));
            
//            String[] saCurrId = getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel());
//            HashMap mapRateMny = m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
//            BusinessCurrencyRateUtil bca = m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());
            
            //循环执行
            int iRow = 0;
            for (int i = 0; i < iAssistUnitLen; i++) {
                iRow = ((Integer) vecAssistUnitIndex.get(i)).intValue();

                Object[] oConvRate = PuTool.getInvConvRateInfo(
                        (String) vecBaseId.get(i), (String) vecAssistId.get(i));
                if (oConvRate == null) {
                    getBillModel().setValueAt(null, iRow, "nconvertrate");
                } else {
                    getBillModel().setValueAt((UFDouble) oConvRate[0], iRow,
                            "nconvertrate");
                }

                //换算率改变，重新计算
                BillEditEvent tempE = new BillEditEvent(
                        getBodyItem("nconvertrate"), getBodyValueAt(iRow,
                                "nconvertrate"), "nconvertrate", iRow);
                calRelation(tempE);
            }
        }

        //设置可编辑性
        //setEnabled_BodyAssistUnitRelated(iBeginRow,iEndRow) ;

    }

    /**
     * 作者：王印芬 功能：供应商、存货、订单日期修改后相应的合同变化 该函数由afterEdit供应商、存货、订单日期改变后调用。
     * 参数：Integer[] iaRow 需设置单的行 boolean bMustSetCnt 是否必须取合同值 调用此方法者设置本值，如：主观选择
     * 了合同参照，则为true；否则为false 返回：无 例外：无 日期：(2003-10-30 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setRelated_Cnt(Integer[] iaRow, boolean bMustSetCnt, boolean bPasteFlag) {

        String pk_corp = getHeadItem("pk_corp").getValue();
        //合同未启用,走默认
        if (!PuTool.isProductEnabled(pk_corp, ProductCode.PROD_CT)) {
            setDefaultPrice(iaRow);
            return;
        }
        //赠品不关联合同（手工参照合同除外）
        if (!bMustSetCnt) {                        
            iaRow = setDefaultPriceAndGetLargessLines(iaRow);
        }
        if (iaRow == null || iaRow.length == 0) {
            SCMEnv.out("所有赠品行均为赠品，直接返回，不作关联合同处理！");/*-=notranslate=-*/
            return;
        }
        //如果是行复制后的粘贴，不询合同
        if(bPasteFlag){
        	iaRow = getNoSourceCtLines(iaRow);
        }
        if (iaRow == null || iaRow.length == 0) {
            SCMEnv.out("所有行均为Z2参照，直接返回，不作关联合同处理！");/*-=notranslate=-*/
            return;
        }
        //寻找相关合同信息
        HashMap mapRowAndCnt = findCntInfoForRowArray(iaRow);
        if (!bMustSetCnt && mapRowAndCnt != null) {
            int iLen = mapRowAndCnt.size();
            RetCtToPoQueryVO[][] voaCnt = (RetCtToPoQueryVO[][]) mapRowAndCnt
                    .values().toArray(new RetCtToPoQueryVO[iLen][]);
            for (int i = 0; i < iLen; i++) {
                if (voaCnt[i] != null) {
                    int iRet = MessageDialog
                            .showYesNoDlg(
                                    this,
                                    nc.ui.ml.NCLangRes.getInstance()
                                            .getStrByID("SCMCOMMON",
                                                    "UPPSCMCommon-000270")/*
                                                                           * @res
                                                                           * "提示"
                                                                           */,
                                    nc.ui.ml.NCLangRes.getInstance()
                                            .getStrByID("40040200",
                                                    "UPP40040200-000034")/*
                                                                          * @res
                                                                          * "寻找到合同，是否关联？"
                                                                          */);
                    if (iRet == MessageDialog.ID_NO) {
                        mapRowAndCnt = null;
                    }
                    break;
                }
            }
        }

        //==============合同
        //供应商管理ID
        String sVendId = getHeadItem("cvendormangid").getValue();
        sVendId = PuPubVO.getString_TrimZeroLenAsNull(sVendId);
        //日期
        String sDate = getHeadItem("dorderdate").getValue();
        sDate = PuPubVO.getString_TrimZeroLenAsNull(sDate);

        //临时变量
        int iTotalLen = iaRow.length;
        int iRow = 0;
        String sInvId = null;
        String sChangedKey = null;
        UFDouble dPrice = null;
        UFDouble dOldPrice = null;

        //用来取非合同价的行及改变的KEY
        Vector vecPoPriceRow = new Vector();

        //价格优先设置
        int iPricePolicy = PuTool.getPricePriorPolicy(pk_corp);
        RetCtToPoQueryVO[] cntListVO = null;
        RetCtToPoQueryVO voCtInfo = null;
        String sBaseid = null;
        HashMap hHeadData = null;
        String sRowNo = null;
        ArrayList listRow = new ArrayList();
        ArrayList listId = new ArrayList();
        ArrayList listRowQP = new ArrayList();
        ArrayList listIdQP = new ArrayList(); 
        
//        String[] saCurrId = getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel());
//        String[] saCurrIdDis = m_cardPoPubSetUI2.getDistinctStrArray(saCurrId);
//        HashMap mapRateMny = m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrIdDis);
//        HashMap mapRate = m_cardPoPubSetUI2.getBothExchRateDigit(getCorp(),saCurrIdDis);
//        HashMap mapRateEditable = m_cardPoPubSetUI2.getBothExchRateEditableBatch(getCorp(),saCurrIdDis);
//        String dOrderDate = getHeadItem("dorderdate").getValue();
//        HashMap mapRateVal = m_cardPoPubSetUI2.getBothExchRateValue(getCorp(),saCurrId,dOrderDate);
//        BusinessCurrencyRateUtil bca = m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());
        
        for (int i = 0; i < iTotalLen; i++) {

            iRow = iaRow[i].intValue();
            //存货管理ID
            sInvId = PuPubVO
                    .getString_TrimZeroLenAsNull((String) getBodyValueAt(iRow,
                            "cmangid"));

            if (sInvId == null || sDate == null) {
                //是否可编辑
                setCellEditable(iRow, "ccontractcode", false);
                setCellEditable(iRow, "ccurrencytype", getBodyItem("ccurrencytype") != null && getBodyItem("ccurrencytype").isEdit());
                setCellEditable(iRow, "noriginalcurprice", getBodyItem("noriginalcurprice") != null && getBodyItem("noriginalcurprice").isEdit());
                setCellEditable(iRow, "norgtaxprice", getBodyItem("norgtaxprice") != null && getBodyItem("norgtaxprice").isEdit());
                //合同ID 合同行ID 合同号 币种
                setBodyValueAt(null, iRow, "ccontractid");
                setBodyValueAt(null, iRow, "ccontractrowid");
                setBodyValueAt(null, iRow, "ccontractcode");

                continue;
            }

            //哪个单价ITEM被修改
            sChangedKey = null;
            dPrice = null;
            dOldPrice = null;

            //合同号始终可编辑
            setCellEditable(iRow, "ccontractcode", getBodyItem("ccontractcode") != null && getBodyItem("ccontractcode").isEdit());

            //得到合同相关信息:
            //[0]合同ID [1]合同行ID [2]合同编码 [3]合同币种 [4]价格
            cntListVO = null;
            voCtInfo = null;
            if (mapRowAndCnt != null) {
                cntListVO = (RetCtToPoQueryVO[]) mapRowAndCnt.get(iaRow[i]);
            }
            if (cntListVO != null && cntListVO.length > 1) {
                //行号
                sRowNo = PuPubVO
                        .getString_TrimZeroLenAsNull((String) getBodyValueAt(
                                iRow, "crowno"));
                //存货基础ID
                sBaseid = PuPubVO
                        .getString_TrimZeroLenAsNull((String) getBodyValueAt(
                                iRow, "cbaseid"));
                hHeadData = new HashMap();
                hHeadData.put("crowno", sRowNo);
                hHeadData.put("cbaseid", sBaseid);

                if (cntSelDlg == null){
                    cntSelDlg = new PoCntSelDlg(this);
                }
                //每次刷新定位缓存
                cntSelDlg.resetLocateHash();
                cntSelDlg.setOnOK(false);
                cntSelDlg.setCntData(cntListVO, hHeadData);
                cntSelDlg.showModal();
                if (cntSelDlg.isON_OK())
                    voCtInfo = cntSelDlg.getRetVO();
                else
                    voCtInfo = null;
            } else if (cntListVO != null) {
                voCtInfo = cntListVO[0];
            }

            //合同ID 合同号
            if (voCtInfo == null) {
                setBodyValueAt(null, iRow, "ccontractrowid");
                setBodyValueAt(null, iRow, "ccontractid");
                setBodyValueAt(null, iRow, "ccontractcode");
            } else {
                setBodyValueAt(voCtInfo.getCContractID(), iRow, "ccontractid");
                setBodyValueAt(voCtInfo.getCContractRowId(), iRow,"ccontractrowid");
                setBodyValueAt(voCtInfo.getCContractCode(), iRow,"ccontractcode");
                
                //since v50, xy,ljf增加带入优质优价方案,跨公司情况不带入，名称取值，放循环外批量执行
                if(PuPubVO.getString_TrimZeroLenAsNull(voCtInfo.getQPBaseSchemeID()) != null
                		&& PoPublicUIClass.getLoginPk_corp().equals(voCtInfo.getPk_Corp())){
	                setBodyValueAt(voCtInfo.getQPBaseSchemeID(), iRow,"cqpbaseschemeid");
	                listRowQP.add(new Integer(iRow));
	                listIdQP.add(voCtInfo.getQPBaseSchemeID());
                }
            }

            //币种ID
            if (voCtInfo == null) {
                //非合同币种可编辑
                setCellEditable(iRow, "ccurrencytype", getBodyItem("ccurrencytype") != null && getBodyItem("ccurrencytype").isEdit());
            } else {
                setBodyValueAt(voCtInfo.getCCurrencyId(), iRow,"ccurrencytypeid");
                //execBodyFormula(iRow, "ccurrencytype"); v31效率优化 放到循环外执行
                listRow.add(new Integer(iRow));
                if (!listId.contains(voCtInfo.getCCurrencyId())) {
                    listId.add(voCtInfo.getCCurrencyId());
                }
                //合同币种不可编辑
                setCellEditable(iRow, "ccurrencytype", false);
                //设置币种相关:汇率、精度等
                setExchangeRateBody(iRow, true);
            }

            //价格
            if (voCtInfo != null) {
                dPrice = OrderPubVO.getPriceValueByPricePolicy(voCtInfo,
                        iPricePolicy);
                sChangedKey = OrderItemVO
                        .getPriceFieldByPricePolicy(iPricePolicy);
                if (dPrice != null) {
                    dOldPrice = PuPubVO
                            .getUFDouble_ValueAsValue(getBodyValueAt(iRow,
                                    sChangedKey));
                    setBodyValueAt(dPrice, iRow, sChangedKey);
                }
            }

            if (voCtInfo != null && dPrice != null) {
                //设置价格的可编辑性
                PoPublicUIClass.setPriceEditableByPricePolicy(this, iRow,
                        voCtInfo, iPricePolicy);

                //重新计算数量关系：只新值不同于原值时才重新计算
                if (dOldPrice == null || dPrice.compareTo(dOldPrice) != 0) {
                    //询到价格时，才重新计算
                    if (sChangedKey == null) {
                        sChangedKey = OrderItemVO
                                .getPriceFieldByPricePolicy(iPricePolicy);
                    }
                    calRelation(new BillEditEvent(getBodyItem(sChangedKey)
                            .getComponent(), getBodyValueAt(iRow, sChangedKey),
                            sChangedKey, iRow));
                }
            } else {
                setCellEditable(iRow, "noriginalcurprice", getBodyItem("noriginalcurprice") != null && getBodyItem("noriginalcurprice").isEdit());
                setCellEditable(iRow, "norgtaxprice", getBodyItem("norgtaxprice") != null && getBodyItem("norgtaxprice").isEdit());

                //加入到批量查询中
                String sCurrId = PuPubVO
                        .getString_TrimZeroLenAsNull((String) getBodyValueAt(
                                iRow, "ccurrencytypeid"));
                Object oBRate = getBodyValueAt(iRow, "nexchangeotobrate");
                if (sCurrId == null && oBRate == null) {
                    continue;
                }

                //可用来寻找默认价的行
                vecPoPriceRow.add(iaRow[i]);
                continue;
            }
        }
        //批量执行显示币种名称公式
        //ccurrencytype->getColValue(bd_currtype,currtypename,pk_currtype,ccurrencytypeid)
        if (listRow.size() > 0 && listId.size() > 0) {
            Hashtable hashIdName = new Hashtable();
            int iSizeId = listId.size();
            try {
                Object[][] objs = PubHelper.queryArrayValue("bd_currtype","pk_currtype",new String[] { "currtypename" },(String[]) listId.toArray(new String[listId.size()]));
                if (objs != null && objs.length == iSizeId) {
                    for (int i = 0; i < iSizeId; i++) {
                        if (objs[i] == null || objs[i].length == 0) {
                            continue;
                        }
                        hashIdName.put(listId.get(i), objs[i][0]);
                    }
                    int iSizeRow = listRow.size();
                    Object oValId = null;
                    Object oValName = null;
                    int iPos = 0;
                    for (int i = 0; i < iSizeRow; i++) {
                        iPos = ((Integer) listRow.get(i)).intValue();
                        oValId = getBillModel().getValueAt(iPos,"ccurrencytypeid");
                        oValName = (String) hashIdName.get(oValId);
                        getBillModel().setValueAt(oValName, iPos,"ccurrencytype");
                    }
                }
            } catch (Exception e) {
                SCMEnv.out("获取币种名称时出错!" + e.getMessage());
            }
        }
        
        //since v50, xy,ljf增加带入优质优价方案,名称取值，放循环外批量执行
        //cqpbaseschemename->getColValue(scm_qpbasescheme,vschemename,cqpbaseschemeid,cqpbaseschemeid)
        if (listRowQP.size() > 0 && listIdQP.size() > 0) {
            Hashtable hashIdName = new Hashtable();
            int iSizeId = listIdQP.size();
            try {
                Object[][] objs = PubHelper.queryArrayValue("scm_qpbasescheme","cqpbaseschemeid",new String[] { "vschemename" },(String[]) listIdQP.toArray(new String[listIdQP.size()]));
                if (objs != null && objs.length == iSizeId) {
                    for (int i = 0; i < iSizeId; i++) {
                        if (objs[i] == null || objs[i].length == 0) {
                            continue;
                        }
                        hashIdName.put(listIdQP.get(i), objs[i][0]);
                    }
                    int iSizeRow = listRowQP.size();
                    Object oValId = null;
                    Object oValName = null;
                    int iPos = 0;
                    for (int i = 0; i < iSizeRow; i++) {
                        iPos = ((Integer) listRowQP.get(i)).intValue();
                        oValId = getBillModel().getValueAt(iPos,"cqpbaseschemeid");
                        oValName = (String) hashIdName.get(oValId);
                        getBillModel().setValueAt(oValName, iPos,"cqpbaseschemename");
                    }
                }
            } catch (Exception e) {
                SCMEnv.out("获取优质优价方案名称时出错!" + e.getMessage());
            }
        }
        
        
        int iPoPriceLen = vecPoPriceRow.size();
        if (iPoPriceLen == 0) {
            return;
        }
        //设置默认价
        setDefaultPrice((Integer[]) vecPoPriceRow
                .toArray(new Integer[iPoPriceLen]));
    }

    /**
     * 作者：王印芬 功能：供应商、存货、订单日期修改后相应的合同变化 该函数由afterEdit供应商、存货、订单日期改变后调用。 参数： int
     * iBeginRow 需计算合同相关信息的表体开始行 int iEndRow 需计算合同相关信息的表体结束行 boolean bMustSetCnt
     * 是否必须取合同值 调用此方法者设置本值，如：主观选择 了合同参照，则为true；否则为false 返回：无 例外：无 日期：(2002-3-13
     * 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-27 王印芬 加入对已有相关合同的行的处理，用于支持参照的多选
     * 2002-05-28 王印芬 修改PoPublicUIClass.addCntInfo(String,Object[])的空指针错误
     * 2002-06-20 王印芬 置合同号为始终可编辑
     */
    private void setRelated_Cnt(int iBeginRow, int iEndRow, boolean bMustSetCnt, boolean bPasteFlag) {
        Vector vecRow = new Vector();
        for (int i = iBeginRow; i <= iEndRow; i++) {
            vecRow.add(new Integer(i));
        }
        setRelated_Cnt((Integer[]) vecRow.toArray(new Integer[vecRow.size()]),
                bMustSetCnt, bPasteFlag);
    }

    /**
     * 作者：王印芬 功能：存货、订单日期、币种修改后相应的最高限价变化 该函数由afterEdit存货、订单日期、币种改变后调用。 参数： int
     * iBeginRow 需计算合同相关信息的表体开始行 int iEndRow 需计算合同相关信息的表体结束行 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void setRelated_MaxPrice(int iBeginRow, int iEndRow) {

        PoPublicUIClass.loadCardMaxPrice(this, getHeadItem("pk_corp")
                .getValue(), iBeginRow, iEndRow,m_cardPoPubSetUI2);
    }

    /**
     * 作者：王印芬 功能：存货修改后相应的税率变化 该函数由afterEdit存货、订单日期、币种改变后调用。 参数： int iBeginRow
     * 需计算税率相关信息的表体开始行 int iEndRow 需计算税率相关信息的表体结束行 返回：无 例外：无 日期：(2003-11-3
     * 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void setRelated_Taxrate(int iBeginRow, int iEndRow) {

        nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
        timeDebug.start();

        HashMap mapId = new HashMap();
        Vector vecRow = new Vector();
        for (int i = iBeginRow; i <= iEndRow; i++) {
            String sBaseId = PuPubVO
                    .getString_TrimZeroLenAsNull((String) getBodyValueAt(i,
                            "cbaseid"));
            if (sBaseId != null) {
                mapId.put(sBaseId, "");
                vecRow.add(new Integer(i));
            }
        }
        int iSize = vecRow.size();
        if (iSize == 0) {
            return;
        }
        timeDebug.addExecutePhase("得到待查询的ID");/*-=notranslate=-*/

        //批量加载税率
        PuTool.loadBatchTaxrate((String[]) mapId.keySet().toArray(
                new String[iSize]));
        timeDebug.addExecutePhase("批量加载税率");/*-=notranslate=-*/

        //重置
        int iRow = 0;
        String sBaseId = null;
        UFDouble dCurTaxRate = null;
        
//        String[] saCurrId = getAllCurrIdFromCard(getRowCount(),getCorp(),getHeadItem("ccurrencytypeid").getValue(),getBillModel());
//        HashMap mapRateMny = m_cardPoPubSetUI2.getMoneyDigitByCurr_Busi(saCurrId);
//        BusinessCurrencyRateUtil bca = m_cardPoPubSetUI2.getCurrArith_Busi(getCorp());        
        for (int i = 0; i < iSize; i++) {
            iRow = ((Integer) vecRow.get(i)).intValue();

            sBaseId = (String) getBodyValueAt(iRow, "cbaseid");

            //UFDouble dOldTaxRate =
            // PuPubVO.getUFDouble_NullAsZero(getBillModel().getValueAt(iRow,"ntaxrate"));
            dCurTaxRate = PuTool.getInvTaxRate(sBaseId);
            if (dCurTaxRate != null) {
                setBodyValueAt(dCurTaxRate, iRow, "ntaxrate");
                //税率改变，计算（数量，单价，含税单价，金额，税率，税额，价税合计,扣率，扣率单价）之间的关系
                BillEditEvent tempE = new BillEditEvent(
                        getBodyItem("ntaxrate"), dCurTaxRate, "ntaxrate", iRow);
                calRelation(tempE);
            }
        }
        timeDebug.addExecutePhase("重新计算关系");/*-=notranslate=-*/
        timeDebug.showAllExecutePhase("加载税率");/*-=notranslate=-*/

    }

    /**
     * 作者：WYF 功能：设置修订表体的可编辑性 参数：无 返回：无 例外：无 日期：(2004-5-27 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     * 
     * <p>2006-05-22,Czp,V5,支持单据模板的“是否可修订”属性
     */
    private void setReviseEnable_Body(int iRow) {

        //OrderItemVO[] voaItem = voOrder.getBodyVO() ;
        BillItem[] bitemaBody = getBodyItems();
        int iBodyItemLen = bitemaBody.length;
        //关闭的行
        if (!isRowActive(iRow)) {
            for (int i = 0; i < iBodyItemLen; i++) {
                setCellEditable(iRow, bitemaBody[i].getKey(), false);
            }
        } 
        //未关闭-有后续单据生成
        else if (isRowHaveAfterBill(iRow)) {        	
            for (int i = 0; i < iBodyItemLen; i++) {
            	if(m_listFixedFields.contains(bitemaBody[i].getKey())){
            		setCellEditable(iRow, bitemaBody[i].getKey(), false);
                }else{
            		setCellEditable(iRow, bitemaBody[i].getKey(), bitemaBody[i].isM_bReviseFlag());
            	}
            }
        } 
        //未关闭-无后续单据生成
        else{
            for (int i = 0; i < iBodyItemLen; i++) {
                setCellEditable(iRow, bitemaBody[i].getKey(), bitemaBody[i].isM_bReviseFlag());
            }
        }
    }

    /**
     * 作者：WYF 功能：设置修订的表头的可编辑性 参数：无 返回：无 例外：无 日期：(2003-9-8 11:39:21)
     * 修改日期，修改人，修改原因，注释标志：
     */
    private void setReviseEnabledCells_Head(OrderVO voOrder) {

        //-------------表体至少一行已执行时，表头栏目不可修改。（此处执行从发货开始）
        //boolean bBodyExeced =
        // voOrder.isHaveOperationFrom(OrderstatusVO.STATUS_SENDOUT) ;
        boolean bBodyExeced = isHaveAfterBill(voOrder.getHeadVO().getCorderid());

        //表头所有项
        BillItem[] baHeadItem = getHeadItems();
        int iHLen = baHeadItem.length;

        if (bBodyExeced) {
            //表体至少一行已执行时，表头栏目不可修改。（此处执行从发货开始）
            for (int i = 0; i < iHLen; i++) {
                baHeadItem[i].setEnabled(false);
            }
        } else {
            //-------------输出
            //输出前，除订单号、订单日期、业务类型、公司等默认项外，其他项可改
            //输出后，除默认项及（供应商、散户）外，其他项可改。

            ////表头可编辑
            for (int i = 0; i < iHLen; i++) {
                baHeadItem[i].setEnabled(baHeadItem[i].isM_bReviseFlag() && baHeadItem[i].isEdit());
            }

            //boolean bOutputed =
            // voOrder.isHaveOperationFrom(OrderstatusVO.STATUS_OUTPUT) ;
            boolean bOutputed = false;
            if (bBodyExeced
                    || voOrder.getHeadVO().getForderstatus().equals(
                            nc.vo.scm.pu.BillStatus.OUTPUT)) {
                bOutputed = true;
            }
            //因输出决定是否改变的项
            String[] saDecidedItem = new String[] { "cvendormangid",
                    "cfreecustid" };
            int iDecidedLen = saDecidedItem.length;
            for (int i = 0; i < iDecidedLen; i++) {
                getHeadItem(saDecidedItem[i]).setEnabled(!bOutputed && getHeadItem(saDecidedItem[i]).isM_bReviseFlag() && getHeadItem(saDecidedItem[i]).isEdit());
            }

            //重新设置表头的可编辑性
            //此处与前面的设置有冲突，可否再对结构进行优化
            setEnabled_Head(true);
        }

        //修订日期始终可用+用户决定
        getHeadItem("drevisiondate").setEnabled(getHeadItem("drevisiondate").isM_bReviseFlag() && getHeadItem("drevisiondate").isEdit());
        //始终不改变的项
        String[] saUnChangedItem = new String[] { "bisreplenish", "bislatest",
                "vordercode", "dorderdate", "pk_corp", "nversion",
		  "cbiztype", "nprepaymny","breturn","bdeliver" };
        int iUnChangedLen = saUnChangedItem.length;
        for (int i = 0; i < iUnChangedLen; i++) {
            getHeadItem(saUnChangedItem[i]).setEnabled(false);
        }

    }

//    /**
//     * 作者：李亮 功能：设置折本及折辅汇率小数位 参数：String pk_corp 公司ID int iflag 返回：无 例外：无
//     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-20 wyf
//     * 修改原先取最大精度为取各币种精度 wyf add/modify/delete 2002-05-20 begin/end
//     */
//    protected static void setRowDigits_ExchangeRate(String sPk_corp, int iRow,
//            BillModel billModel,HashMap mapRate) {
//        setRowDigits_ExchangeRate(sPk_corp,iRow,billModel,mapRate);
//    }
    /**
     * 作者：李亮 功能：设置折本及折辅汇率小数位 参数：String pk_corp 公司ID int iflag 返回：无 例外：无
     * 日期：(2002-3-13 11:39:21) 修改日期，修改人，修改原因，注释标志： 2002-05-20 wyf
     * 修改原先取最大精度为取各币种精度 wyf add/modify/delete 2002-05-20 begin/end
     */
    protected static void setRowDigits_ExchangeRate(String sPk_corp, int iRow,
            BillModel billModel,POPubSetUI2 setUi) {
        //取得币种
        String sCurrId = (String) billModel.getValueAt(iRow, "ccurrencytypeid");
        int[] iaExchRateDigit = null;
        //金额、税额、价税合计
        iaExchRateDigit = setUi.getBothExchRateDigit(sPk_corp,sCurrId);
        if(iaExchRateDigit == null || iaExchRateDigit.length == 0){
	        billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(2);
	        billModel.getItemByKey("nexchangeotoarate").setDecimalDigits(2);
        }else{
	        billModel.getItemByKey("nexchangeotobrate").setDecimalDigits(iaExchRateDigit[0]);
	        billModel.getItemByKey("nexchangeotoarate").setDecimalDigits(iaExchRateDigit[1]);
        }
    }
    /**
     * 作者：WYF 功能：设置行的金额小数位 参数：String pk_corp 公司ID 返回：无 例外：无 日期：(2005-5-16
     * 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    protected static void setRowDigits_Mny(String sPk_corp, int iRow,BillModel billModel,POPubSetUI2 setUi) {
        
        int iMnyDigit = 2;
        int iMnyDigitLocal = 2;
        int iMnyDigitAssi = 2;
        
        //取得币种
        String sCurrId = (String) billModel.getValueAt(iRow, "ccurrencytypeid");
        int[] iaDigits = null;

        try {
	        if(setUi.getCurrArith_Busi(sPk_corp).isBlnLocalFrac()){
	            iaDigits = setUi.getMoneyDigitByCurr_Busi_Batch(new String[]{sCurrId,setUi.getCurrArith_Busi(sPk_corp).getLocalCurrPK(),setUi.getCurrArith_Busi(sPk_corp).getFracCurrPK()});
	            if(iaDigits != null && iaDigits.length >= 3){
		            iMnyDigit = iaDigits[0];
		            iMnyDigitLocal = iaDigits[1];
		            iMnyDigitAssi = iaDigits[2];
	            }
	        }else{
	            iaDigits = setUi.getMoneyDigitByCurr_Busi_Batch(new String[]{sCurrId,setUi.getCurrArith_Busi(sPk_corp).getLocalCurrPK()});
	            if(iaDigits != null && iaDigits.length >= 2){
		            iMnyDigit = iaDigits[0];
		            iMnyDigitLocal = iaDigits[1];
	            }
	        }
        } catch (Exception e) {
            PuTool.outException(e);
            return;
        }
        //原币金额类
        int iLen = OrderItemVO.getDbMnyFields_Org_Busi().length;
        for (int i = 0; i < iLen; i++) {
            BillItem item = billModel.getItemByKey(OrderItemVO.getDbMnyFields_Org_Busi()[i]);
            if (item != null) {
                item.setDecimalDigits(iMnyDigit);
            }
        }
        //本币金额类
        iLen = OrderItemVO.getDbMnyFields_Local_Busi().length;
        for (int i = 0; i < iLen; i++) {
            BillItem item = billModel.getItemByKey(OrderItemVO.getDbMnyFields_Local_Busi()[i]);
            if (item != null) {
                item.setDecimalDigits(iMnyDigitLocal);
            }
        }
        //辅币金额类
        try {
            if (setUi.getCurrArith_Busi(sPk_corp).isBlnLocalFrac()) {
                iLen = OrderItemVO.getDbMnyFields_Assist_Busi().length;
                for (int i = 0; i < iLen; i++) {
                    BillItem item = billModel.getItemByKey(OrderItemVO.getDbMnyFields_Assist_Busi()[i]);
                    if (item != null) {
                        item.setDecimalDigits(iMnyDigitAssi);
                    }
                }
            }
        } catch (Exception e) {
            PuTool.outException(e);
            return;
        }
    }

    /**
     * 作者：王印芬 功能：设置粘贴行的值 参数：int iBeginRow 开始行 int iEndRow 结束行 返回：无 例外：无
     * 日期：(2004-06-10 11:39:21) 修改日期，修改人，修改原因，注释标志：
     */
    private void setValueToPastedLines(int iBeginRow, int iEndRow) {

        //设置默认值
        setDefaultValueToPastedLines(iBeginRow, iEndRow);

        //询价
        setRelated_Cnt(iBeginRow, iEndRow, false, true);

        //备注
        setDefaultValueToVmemo();

        //收发货地址
        setDefaultValueToAddr();

    }
    
/**
 *@功能：过滤有源且为合同的行索引,主要考虑复制行复制来源与关联到合同的一致性
 *@作者：晁志平
 *@since v50
 */
private Integer[] getNoSourceCtLines(Integer[] iaRow) {
	Object objUpSrcBillType = null;
	int iLen = iaRow == null ? 0 : iaRow.length;
	ArrayList listNotCntIndex = new ArrayList();
	for (int i = 0; i < iLen; i++) {
		if (iaRow[i] == null) {
			SCMEnv.out(new Exception("调用过滤有源且为合同的行索引方法时参数数组存在空元素!"));/*-=notranslate=-*/
			continue;
		}
		objUpSrcBillType = getBodyValueAt(iaRow[i].intValue(), "cupsourcebilltype");
		//
		if (objUpSrcBillType != null && "Z2".equalsIgnoreCase(objUpSrcBillType.toString().trim())) {
			continue;
		}
		listNotCntIndex.add(iaRow[i]);
	}
	if (listNotCntIndex.size() == 0) {
		return null;
	}
	return (Integer[]) listNotCntIndex.toArray(new Integer[listNotCntIndex.size()]);
}
/**
 *功能：过滤赠品行索引
 *作者：晁志平
 */
private Integer[] setDefaultPriceAndGetLargessLines(Integer[] iaRow) {
	Object objLargess = null;
	Object objPrice = null;
	int iLen = iaRow == null ? 0 : iaRow.length;
	ArrayList listNotCntIndex = new ArrayList();
	for (int i = 0; i < iLen; i++) {
		if (iaRow[i] == null) {
			SCMEnv.out(new Exception("调用过滤赠品行索引方法时参数数组存在空元素!"));/*-=notranslate=-*/
			continue;
		}
		objLargess = getBodyValueAt(iaRow[i].intValue(), "blargess");
		if (objLargess == null || "false".equalsIgnoreCase(objLargess.toString())) {
			listNotCntIndex.add(iaRow[i]);
		}else{
		    //单价
		    objPrice = getBodyValueAt(iaRow[i].intValue(), "noriginalcurprice");
		    if(objPrice == null || objPrice.toString().trim().length() == 0){
		        setBodyValueAt(new UFDouble(0.0),iaRow[i].intValue(), "noriginalcurprice");
		    }
		    //净单价
		    objPrice = getBodyValueAt(iaRow[i].intValue(), "noriginalnetprice");
		    if(objPrice == null || objPrice.toString().trim().length() == 0){
		        setBodyValueAt(new UFDouble(0.0),iaRow[i].intValue(), "noriginalnetprice");
		    }
		    //本币无税净单价
		    objPrice = getBodyValueAt(iaRow[i].intValue(), "nprice");
		    if(objPrice == null || objPrice.toString().trim().length() == 0){
		        setBodyValueAt(new UFDouble(0.0),iaRow[i].intValue(), "nprice");
		    }
		    //本币含税净单价
		    objPrice = getBodyValueAt(iaRow[i].intValue(), "ntaxprice");
		    if(objPrice == null || objPrice.toString().trim().length() == 0){
		        setBodyValueAt(new UFDouble(0.0),iaRow[i].intValue(), "ntaxprice");
		    }
		    //含税单价
		    objPrice = getBodyValueAt(iaRow[i].intValue(), "norgtaxprice");
		    if(objPrice == null || objPrice.toString().trim().length() == 0){
		        setBodyValueAt(new UFDouble(0.0),iaRow[i].intValue(), "norgtaxprice");
		    }
		    //净含税单价
		    objPrice = getBodyValueAt(iaRow[i].intValue(), "norgnettaxprice");
		    if(objPrice == null || objPrice.toString().trim().length() == 0){
		        setBodyValueAt(new UFDouble(0.0),iaRow[i].intValue(), "norgnettaxprice");
		    }
		}
	}
	if (listNotCntIndex.size() == 0) {
		return null;
	}
	return (Integer[]) listNotCntIndex.toArray(new Integer[listNotCntIndex.size()]);
}

/**
 * 作者：晁志平
 * 功能：审批后同步单据VO及单据卡片数据
 * 参数：OrderVO voReloading, 变更的数据VO(轻量)；OrderVO voCurr 现有缓存中VO(待刷新)
 * 返回：无 
 * 例外：无 
 * 日期：2005-4-14 10:24:16
 * 修改日期，修改人，修改原因，注释标志：
 * 
 */
public void reloadBillCardAfterAudit(OrderVO voReloading, OrderVO voCurr) {

    //同步表头：{时间戳、状态、审批人、审批日期}
    
    //当前VO
    if(voCurr == null || voCurr.getHeadVO() == null || voCurr.getBodyVO() == null){
        SCMEnv.out("用于同步的当前单据VO为空或VO表头为空或表体为空!以下信息供程序员参考：\n");
        SCMEnv.out(new Exception());
        MessageDialog.showHintDlg(getContainer(),"提示"/*notranslate*/,"用于同步的数据存在问题，请手工完成刷新当前界面"/*notranslate*/);
        return;
    }
    if(voReloading.getHeadVO() != null){
        OrderHeaderVO headCurr = voCurr.getHeadVO();
        OrderHeaderVO headReloading = voReloading.getHeadVO();
        //VO表头
        headCurr.setTs(headReloading.getTs());
        headCurr.setForderstatus(headReloading.getForderstatus());
        headCurr.setCauditpsn(headReloading.getCauditpsn());
        headCurr.setDauditdate(headReloading.getDauditdate());
        //模板表头、表体
        getHeadItem("ts").setValue(headReloading.getTs());
        getHeadItem("forderstatus").setValue(headReloading.getForderstatus());
        getTailItem("cauditpsn").setValue(headReloading.getCauditpsn());
        getTailItem("dauditdate").setValue(headReloading.getDauditdate());
        getTailItem("tmaketime").setValue(headReloading.getTmaketime());
        getTailItem("taudittime").setValue(headReloading.getTaudittime());
        getTailItem("tlastmaketime").setValue(headReloading.getTlastmaketime());
    }
    //更新表体：{时间戳、累计日计划数量、累计入库数量}    
    HashMap mapIdItemReLoad = getHashMapIdItem(voReloading);
    if(mapIdItemReLoad == null || mapIdItemReLoad.size() == 0){
        SCMEnv.out("用于同步的带有待刷新数据的单据VO(轻量)表体为空!以下信息供程序员参考：\n");
        SCMEnv.out(new Exception());
        MessageDialog.showHintDlg(getContainer(),"提示"/*notranslate*/,"用于同步的数据存在问题，请手工完成刷新当前界面"/*notranslate*/);
        return;
    }    
    HashMap mapIdItemCurr = getHashMapIdItem(voCurr);
    int iLen = getRowCount();
    String strCorder_bid = null;
    BillModel bm = getBillModel();
    OrderItemVO itemReload = null;
    OrderItemVO itemCurr = null;
    for (int i = 0; i < iLen; i++) {
        strCorder_bid = (String) bm.getValueAt(i,"corder_bid");
        if(strCorder_bid == null){
            continue;
        }
        itemReload = (OrderItemVO) mapIdItemReLoad.get(strCorder_bid);
        if(itemReload == null){
            continue;
        }
        //VO表体
        itemCurr = (OrderItemVO) mapIdItemCurr.get(strCorder_bid);
        if(itemCurr != null){
	        itemCurr.setTs(itemReload.getTs());
	        itemCurr.setNaccumdayplnum(itemReload.getNaccumdayplnum());
	        itemCurr.setNaccumstorenum(itemReload.getNaccumstorenum());
        }
        //模板表体
        bm.setValueAt(itemReload.getTs(),i,"ts");
        bm.setValueAt(itemReload.getNaccumdayplnum(),i,"naccumdayplnum");
        bm.setValueAt(itemReload.getNaccumstorenum(),i,"naccumstorenum");
    }
    
}
/**
 * 作者：晁志平
 * 功能：返回HashMap:{corder_bid=OrderItemVO}
 * 参数：OrderVO
 * 返回：无 
 * 例外：无 
 * 日期：2005-4-14 10:24:16
 * 修改日期，修改人，修改原因，注释标志：
 */
private HashMap getHashMapIdItem(OrderVO vo) {
    HashMap mapRet = new HashMap();
    if(vo == null || vo.getBodyVO() == null){
        return mapRet;
    }
    OrderItemVO[] items = vo.getBodyVO(); 
    int iLen = items.length;
    for (int i = 0; i < iLen; i++) {
        if (items[i] == null || items[i].getPrimaryKey() == null) {
            continue;
        }
        mapRet.put(items[i].getPrimaryKey(),items[i]);
    }
    return mapRet;
}
/**
 * 获取汇率精度设置工具
 * */
public POPubSetUI2 getPoPubSetUi2(){
    if(m_cardPoPubSetUI2 == null){
        m_cardPoPubSetUI2 = new POPubSetUI2();
    }
    return m_cardPoPubSetUI2;
}
/**
 * 排序后事件,主要是为了处理非BillModel内容的排序，如自定义的绘制器控件
 * @author czp
 * @since v50
 */
public void afterSort(String key) {
	//输入提示
	if(this.getBillData().getEnabled() && (getBillType().equalsIgnoreCase(BillTypeConst.PO_ORDER)
		|| getBillType().equalsIgnoreCase(BillTypeConst.PO_REPLENISH))){
		OrderVO voCurr = (OrderVO) getBillValueVO(OrderVO.class.getName(),OrderHeaderVO.class.getName(),OrderItemVO.class.getName());
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(this, voCurr);
	}
}



private IUAPQueryBS querybs = null;
public IUAPQueryBS getIUAPQueryBS(){
	if(querybs==null){
		querybs = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	}
	return querybs;
}

//zhwj
public String[] getIszgpricevalues(UFBoolean iszgprice){
	String value = "否";
	if(iszgprice.booleanValue()){
		value = "是";
	}
	StringBuffer sql = new StringBuffer();
	sql.append(" select doc.docname,doc.pk_defdoc from bd_defdoclist list ")
		.append(" inner join bd_defdoc doc ")
		.append(" on list.pk_defdoclist=doc.pk_defdoclist ")
		.append(" where list.doclistcode='BY1104' ")
		.append(" and doc.docname='"+value+"' ")
		.append(" and nvl(list.dr,0)=0 ")
		.append(" and nvl(doc.dr,0)=0 ");
	
	try {
		HashMap hm = (HashMap)getIUAPQueryBS().executeQuery(sql.toString(), new MapProcessor());
		
		if(hm!=null&&hm.size()>0){
			String[] values = new String[2];
			values[0] = hm.get("docname")==null?"":hm.get("docname").toString();
			values[1] = hm.get("pk_defdoc")==null?"":hm.get("pk_defdoc").toString();
			return values;
		}
		
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return null;
}


//zhwj
public void checkZGPrice(int row){
	String cmangid = getBillModel().getValueAt(row, "cmangid")==null?"":
		getBillModel().getValueAt(row, "cmangid").toString();
	if(cmangid!=null&&!cmangid.equals("")){
		String sql = "select nvl(def20,0) def20 from bd_invmandoc where pk_invmandoc='"+cmangid+"' and nvl(dr,0)=0";
		
		try {
			Object obj = getIUAPQueryBS().executeQuery(sql, new ColumnProcessor());
			if(obj==null||obj.toString().equals("")||obj.toString().equals("0")){
				MessageDialog.showErrorDlg(null, "错误", "该存货运费暂估单价未设置，请检查！");
			}
			
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
/**
 * add by shikun 2014-03-22 解决越南采购订单维护时，表头币种变后表体已有行的金额输入精度问题。
 * */
public String getParentCorpCode() {
  String ParentCorp = new String();
  String key = ClientEnvironment.getInstance().getCorporation()
    .getFathercorp();
  try {
    CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
    ParentCorp = corpVO.getUnitcode();
  }
  catch (BusinessException e) {
    e.printStackTrace();
  }
  return ParentCorp;
}




}