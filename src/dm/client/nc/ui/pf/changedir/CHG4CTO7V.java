package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����4CTO7V��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2012-8-8)
 * @author��ƽ̨�ű�����
 */
public class CHG4CTO7V extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG4CTO7V ������ע�⡣
 */
public CHG4CTO7V() {
	super();
}
/**
* ��ú������ȫ¼�����ơ�
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return null;
}
/**
* �����һ���������ȫ¼�����ơ�
* @return java.lang.String[]
*/
public String getOtherClassName() {
	return null;
}
/**
* ����ֶζ�Ӧ��
* @return java.lang.String[]
*/
public String[] getField() {
	return new String[] {
		"H_pk_corp->H_pk_corp",
		"H_pk_transcust->B_pk_transcust",
		"H_pk_defdoc18->B_fytype",
		"H_vuserdef14->B_vuserdef20",
		"B_vuserdef17->B_vuserdef12",	//ȡĬ�ϵ�һ��Ϊ��ͷ�����ܽ���ͷ��12��ֵ�����壬����һ���ظ���
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
* ��ù�ʽ��
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[]{"H_vuserdef18->getColValue(bd_sendtype,sendname,pk_sendtype,B_fytype)"};
}
/**
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
