package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����29TO21��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2005-6-1)
 * @author��ƽ̨�ű�����
 */
public class CHG28TO21 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHG29TO21 ������ע�⡣
 */
public CHG28TO21() {
	super();
}
/**
* ��ú������ȫ¼�����ơ�
* @return java.lang.String[]
*/
public String getAfterClassName() {
	return "nc.ui.pu.conversion.AskbillToOrderVOConversion";
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
		//��ͷ -----------------------------
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
		
		//������������õ�
		"B_cupsourcehts->H_ts",
		"B_cupsourcebts->B_ts",
		
		//���� --------------------------------
		
		//�ɹ���˾��
		"B_pk_corp->H_pk_corp",
		//�ϲ㵥�ݹ�˾
		"B_pk_upsrccorp->B_pk_upsrccorp",
		//������Ϣ(��VO���պ������л��д���)
		"B_pk_reqcorp->B_pk_reqcorp",
		"B_pk_reqstoorg->B_pk_reqstoorg",
		"B_pk_creqwareid->B_pk_creqwareid",
		//�ջ���Ϣ(��VO���պ������л��д���)
		"B_pk_arrvstoorg->B_pk_reqstoorg",
		"B_cwarehouseid->B_pk_creqwareid",
		//��Ŀ����Ŀ�׶�
		"B_cprojectid->B_cprojectid",
		"B_cprojectphaseid->B_cprojectphaseid",
		//������
		"B_cassistunit->B_cassistunit",	
		//������
		"B_nassistnum->B_nassistnum",
		//���κ�
		"B_vproducenum->B_vproducenum",
		//���
		"B_cmangid->B_cmangid",
		"B_cbaseid->B_cbaseid",
		"B_vfree1->B_vfree1",
		"B_vfree2->B_vfree2",
		"B_vfree3->B_vfree3",
		"B_vfree4->B_vfree4",
		"B_vfree5->B_vfree5",
		//�۸��������ż��������ID
		"B_vpriceauditcode->H_vpriceauditcode",
		"B_cpriceauditid->B_cpriceauditid",
		"B_cpriceaudit_bid->B_cpriceaudit_bid",
		"B_cpriceaudit_bb1id->B_cpriceaudit_bb1id",
		//Դͷ����ID
		"B_cupsourcebilltype->B_cupsourcebilltype",
		"B_cupsourcebillid->B_cupsourcebillid",
		"B_cupsourcebillrowid->B_cupsourcebillrowid",
		"B_csourcebilltype->B_csourcebilltype",
		"B_csourcebillid->B_csourcebillid",
		"B_csourcerowid->B_csourcebillrowid",
		//����
		"B_ccurrencytypeid->H_ccurrencytypeid",
		
		//���������ۡ������أ�	
		"B_nordernum->B_nordernum",				//��������
		"B_ntaxrate->B_ntaxrate",				//˰��
		"B_noriginalcurprice->B_norderprice",	//ԭ�ҵ���
		"B_norgtaxprice->B_nordertaxprice",		//ԭ�Һ�˰����
		"B_noriginalnetprice->B_norderprice",	//ԭ�Ҿ�����
		"B_norgnettaxprice->B_nordertaxprice",	//ԭ�Ҿ���˰����
		"B_noriginalcurmny->B_norgmny",			//ԭ�ҽ��
		"B_noriginaltaxmny->B_norgtaxmny",		//ԭ��˰��
		"B_noriginaltaxpricemny->B_norgsummny",	//ԭ�Ҽ�˰�ϼ�
		//�ƻ���������
		"B_dplanarrvdate->B_darrvdate",
		//�Զ�����
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
		//��ע
		"B_vmemo->B_vmemo",
		//eric
		"H_ctransmodeid->H_vdef7"
	};
}
/**
* ��ù�ʽ��
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
		//��ͬ
		"B_ccontractid->iif(B_fpricetype == \"0\", B_cquotebillid, null)",
		"B_ccontractrowid->iif(B_fpricetype == \"0\", B_cquotebill_bid, null)",
		//��Ʊ��˾(��VO���պ������л��д���)
		"B_pk_invoicecorp->iif(B_pk_reqcorp == null, H_pk_corp, B_pk_reqcorp)",
		//�ջ���Ϣ(��VO���պ������л��д���)
		"B_pk_arrvcorp->iif(B_pk_reqcorp == null, H_pk_corp, B_pk_reqcorp)"
	};
}
/**
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
