package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于4CTO7V的VO的动态转换类。
 *
 * 创建日期：(2012-8-8)
 * @author：平台脚本生成
 */
public class CHG4CTO7V extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG4CTO7V 构造子注解。
 */
public CHG4CTO7V() {
	super();
}
/**
* 获得后续类的全录经名称。
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return null;
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
		"H_pk_corp->H_pk_corp",
		"H_pk_transcust->B_pk_transcust",
		"H_pk_defdoc18->B_fytype",
		"H_vuserdef14->B_vuserdef20",
		"B_vuserdef17->B_vuserdef12",	//取默认第一行为表头，不能将表头的12赋值给表体，导致一致重复。
		"B_vuserdef14->B_vbillcode",
		"B_vuserdef18->B_invcode",
		"B_vuserdef16->B_invname",
		"B_vuserdef12->B_num",
		"B_dinvnum->B_rowcount",
		"B_vuserdef15->B_csourcebillbid",
		"B_vuserdef7->B_cph",
		
		
	
//		"H_pk_defdoc10->H_custname",
//		"H_vuserdef9->H_cinventoryid", 
		 
	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[]{"H_vuserdef18->getColValue(bd_sendtype,sendname,pk_sendtype,B_fytype)"};
}
/**
* 返回用户自定义函数。
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
