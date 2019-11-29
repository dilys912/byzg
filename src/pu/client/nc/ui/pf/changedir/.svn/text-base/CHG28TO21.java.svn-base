package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于29TO21的VO的动态转换类。
 *
 * 创建日期：(2005-6-1)
 * @author：平台脚本生成
 */
public class CHG28TO21 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG29TO21 构造子注解。
 */
public CHG28TO21() {
	super();
}
/**
* 获得后续类的全录经名称。
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return "nc.ui.pu.conversion.AskbillToOrderVOConversion";
}
/**
* 获得另一个后续类的全录径名称。
* @return java.lang.String[]
*/
public String getOtherClassName() {
	return null;
}
/**
* 获得字段对应。
* @return java.lang.String[]
*/
public String[] getField() {
	return new String[] {
		//表头 -----------------------------
		"H_cbiztype->B_cbiztype",
		"H_pk_corp->H_pk_corp",
		"H_coperator->SYSOPERATOR",
		"H_dorderdate->SYSDATE",
		"H_caccountyear->SYSACCOUNTYEAR",
		"H_cvendormangid->B_cvendormangid",
		"H_cvendorbaseid->B_cvendorbaseid",
		"H_cdeptid->H_cdeptid",
		"H_ctermprotocolid->H_ctermprotocolid",
		"H_cemployeeid->H_cemployeeid",
		"H_cpurorganization->H_pk_purorg",
		"H_vdef1->H_vdef1",
		"H_vdef2->H_vdef2",
		"H_vdef3->H_vdef3",
		"H_vdef4->H_vdef4",
		"H_vdef5->H_vdef5",
		"H_vdef6->H_vdef6",
		"H_vdef7->H_vdef7",
		"H_vdef8->H_vdef8",
		"H_vdef9->H_vdef9",
		"H_vdef10->H_vdef10",
		"H_vdef11->H_vdef11",
		"H_vdef12->H_vdef12",
		"H_vdef13->H_vdef13",
		"H_vdef14->H_vdef14",
		"H_vdef15->H_vdef15",
		"H_vdef16->H_vdef16",
		"H_vdef17->H_vdef17",
		"H_vdef18->H_vdef18",
		"H_vdef19->H_vdef19",
		"H_vdef20->H_vdef20",
		"H_pk_defdoc1->H_pk_defdoc1",
		"H_pk_defdoc2->H_pk_defdoc2",
		"H_pk_defdoc3->H_pk_defdoc3",
		"H_pk_defdoc4->H_pk_defdoc4",
		"H_pk_defdoc5->H_pk_defdoc5",
		"H_pk_defdoc6->H_pk_defdoc6",
		"H_pk_defdoc7->H_pk_defdoc7",
		"H_pk_defdoc8->H_pk_defdoc8",
		"H_pk_defdoc9->H_pk_defdoc9",
		"H_pk_defdoc10->H_pk_defdoc10",
		"H_pk_defdoc11->H_pk_defdoc11",
		"H_pk_defdoc12->H_pk_defdoc12",
		"H_pk_defdoc13->H_pk_defdoc13",
		"H_pk_defdoc14->H_pk_defdoc14",
		"H_pk_defdoc15->H_pk_defdoc15",
		"H_pk_defdoc16->H_pk_defdoc16",
		"H_pk_defdoc17->H_pk_defdoc17",
		"H_pk_defdoc18->H_pk_defdoc18",
		"H_pk_defdoc19->H_pk_defdoc19",
		"H_pk_defdoc20->H_pk_defdoc20",
		"H_vmemo->H_vmemo",
		
		//并发检查上游用到
		"B_cupsourcehts->H_ts",
		"B_cupsourcebts->B_ts",
		
		//表体 --------------------------------
		
		//采购公司：
		"B_pk_corp->H_pk_corp",
		//上层单据公司
		"B_pk_upsrccorp->B_pk_upsrccorp",
		//需求信息(在VO对照后续类中还有处理)
		"B_pk_reqcorp->B_pk_reqcorp",
		"B_pk_reqstoorg->B_pk_reqstoorg",
		"B_pk_creqwareid->B_pk_creqwareid",
		//收货信息(在VO对照后续类中还有处理)
		"B_pk_arrvstoorg->B_pk_reqstoorg",
		"B_cwarehouseid->B_pk_creqwareid",
		//项目、项目阶段
		"B_cprojectid->B_cprojectid",
		"B_cprojectphaseid->B_cprojectphaseid",
		//辅计量
		"B_cassistunit->B_cassistunit",	
		//辅数量
		"B_nassistnum->B_nassistnum",
		//批次号
		"B_vproducenum->B_vproducenum",
		//存货
		"B_cmangid->B_cmangid",
		"B_cbaseid->B_cbaseid",
		"B_vfree1->B_vfree1",
		"B_vfree2->B_vfree2",
		"B_vfree3->B_vfree3",
		"B_vfree4->B_vfree4",
		"B_vfree5->B_vfree5",
		//价格审批单号及单据相关ID
		"B_vpriceauditcode->H_vpriceauditcode",
		"B_cpriceauditid->B_cpriceauditid",
		"B_cpriceaudit_bid->B_cpriceaudit_bid",
		"B_cpriceaudit_bb1id->B_cpriceaudit_bb1id",
		//源头单据ID
		"B_cupsourcebilltype->B_cupsourcebilltype",
		"B_cupsourcebillid->B_cupsourcebillid",
		"B_cupsourcebillrowid->B_cupsourcebillrowid",
		"B_csourcebilltype->B_csourcebilltype",
		"B_csourcebillid->B_csourcebillid",
		"B_csourcerowid->B_csourcebillrowid",
		//币种
		"B_ccurrencytypeid->H_ccurrencytypeid",
		
		//数量、单价、金额相关：	
		"B_nordernum->B_nordernum",				//订单数量
		"B_ntaxrate->B_ntaxrate",				//税率
		"B_noriginalcurprice->B_norderprice",	//原币单价
		"B_norgtaxprice->B_nordertaxprice",		//原币含税单价
		"B_noriginalnetprice->B_norderprice",	//原币净单价
		"B_norgnettaxprice->B_nordertaxprice",	//原币净含税单价
		"B_noriginalcurmny->B_norgmny",			//原币金额
		"B_noriginaltaxmny->B_norgtaxmny",		//原币税额
		"B_noriginaltaxpricemny->B_norgsummny",	//原币价税合计
		//计划到货日期
		"B_dplanarrvdate->B_darrvdate",
		//自定义项
		"B_vdef1->B_vdef1",
		"B_vdef2->B_vdef2",
		"B_vdef3->B_vdef3",
		"B_vdef4->B_vdef4",
		"B_vdef5->B_vdef5",
		"B_vdef6->B_vdef6",
		"B_vdef7->B_vdef7",
		"B_vdef8->B_vdef8",
		"B_vdef9->B_vdef9",
		"B_vdef10->B_vdef10",
		"B_vdef11->B_vdef11",
		"B_vdef12->B_vdef12",
		"B_vdef13->B_vdef13",
		"B_vdef14->B_vdef14",
		"B_vdef15->B_vdef15",
		"B_vdef16->B_vdef16",
		"B_vdef17->B_vdef17",
		"B_vdef18->B_vdef18",
		"B_vdef19->B_vdef19",
		"B_vdef20->B_vdef20",
		"B_pk_defdoc1->B_pk_defdoc1",
		"B_pk_defdoc2->B_pk_defdoc2",
		"B_pk_defdoc3->B_pk_defdoc3",
		"B_pk_defdoc4->B_pk_defdoc4",
		"B_pk_defdoc5->B_pk_defdoc5",
		"B_pk_defdoc6->B_pk_defdoc6",
		"B_pk_defdoc7->B_pk_defdoc7",
		"B_pk_defdoc8->B_pk_defdoc8",
		"B_pk_defdoc9->B_pk_defdoc9",
		"B_pk_defdoc10->B_pk_defdoc10",
		"B_pk_defdoc11->B_pk_defdoc11",
		"B_pk_defdoc12->B_pk_defdoc12",
		"B_pk_defdoc13->B_pk_defdoc13",
		"B_pk_defdoc14->B_pk_defdoc14",
		"B_pk_defdoc15->B_pk_defdoc15",
		"B_pk_defdoc16->B_pk_defdoc16",
		"B_pk_defdoc17->B_pk_defdoc17",
		"B_pk_defdoc18->B_pk_defdoc18",
		"B_pk_defdoc19->B_pk_defdoc19",
		"B_pk_defdoc20->B_pk_defdoc20",
		//备注
		"B_vmemo->B_vmemo",
		//eric
		"H_ctransmodeid->H_vdef7"
	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[] {
		"H_forderstatus->0",
		"H_dr->0",
		"H_bislatest->\"Y\"",
		"H_bisreplenish->\"N\"",
		"H_nversion->1",
		"H_cdeptid->getColValue(bd_psndoc,pk_deptdoc,pk_psndoc,H_cemployeeid)",
		"H_breturn->\"N\"",
		"H_iprintcount->0",
		"B_iisactive->0",
		"B_forderrowstatus->0",
		"B_idiscounttaxtype->1",
		"B_iisreplenish->0",
		"B_ndiscountrate->100",
		"B_dr->0",
		"B_status->2",
		"B_breceiveplan->\"N\"",
		"B_blargess->\"N\"",
		//合同
		"B_ccontractid->iif(B_fpricetype == \"0\", B_cquotebillid, null)",
		"B_ccontractrowid->iif(B_fpricetype == \"0\", B_cquotebill_bid, null)",
		//收票公司(在VO对照后续类中还有处理)
		"B_pk_invoicecorp->iif(B_pk_reqcorp == null, H_pk_corp, B_pk_reqcorp)",
		//收货信息(在VO对照后续类中还有处理)
		"B_pk_arrvcorp->iif(B_pk_reqcorp == null, H_pk_corp, B_pk_reqcorp)"
	};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
