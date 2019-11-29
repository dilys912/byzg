package nc.bs.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * ����61TOA3��VO�Ķ�̬ת���ࡣ
 *
 * �������ڣ�(2005-2-3)
 * @author��ƽ̨�ű�����
 */
public class CHG61TOA3 extends nc.bs.pf.change.VOConversion {
/**
 * CHG61TOA3 ������ע�⡣
 */
public CHG61TOA3() {
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
		"H_gcbm->H_cwareid",
		"H_ylbmid->H_cdeptid",
		"H_lyid->H_corderid",
		"H_lydjh->H_vordercode",
		"H_bljhdh->B_corder_bid",
		"H_jhwgsl->B_nordernum",
		"H_fjhwgsl->B_nassistnum",
		"H_ksid->H_cvendormangid",
		"H_fbid->B_corder_bid",
		"H_zdrq->SYSDATE",
		"H_logDate->SYSDATE",
		"H_operid->SYSOPERATOR",
		"H_wlbmid->B_cbaseid",
		"H_fjldwid->B_cassistunit",
		"H_pch->B_vproducenum",
		"H_freeitemvalue1->B_vfree1",
		"H_freeitemvalue2->B_vfree2",
		"H_freeitemvalue3->B_vfree3",
		"H_freeitemvalue4->B_vfree4",
		"H_freeitemvalue5->B_vfree5",
		"H_zdy1->B_vdef1",
		"H_zdy2->B_vdef2",
		"H_zdy3->B_vdef3",
		"H_zdy4->B_vdef4",
		"H_zdy5->B_vdef5"
		,"H_bomver->B_bomvers" //shikun bomvers
	};
}
/**
* ��ù�ʽ��
* @return java.lang.String[]
*/
public String[] getFormulas() {
	return new String[] {
		"H_pk_produce->getProduceID(H_cwareid ,  B_cmangid )",
		"H_zt->\"A\"",
		"H_lylx->2",
		"H_sfct->\"Y\"",
		"H_zjbz->1",
		"H_bljhlx->0",
		"H_zdrmc->getColValue(sm_user ,user_name ,cuserid , H_cauditpsn )",
		"H_fjlhsl->iif(B_nassistnum==null,null,iif(B_nassistnum==0,0,B_nordernum/B_nassistnum))"
	};
}
/**
* �����û��Զ��庯����
*/
public UserDefineFunction[] getUserDefineFunction() {
	try {
		//��0���Զ��庯��
		UserDefineFunction func0 = new UserDefineFunction();
		func0.setClassName("nc.bs.mm.pub.CommonDataDMO");
		func0.setMethodName("getProduceID");
		func0.setReturnType(Class.forName("java.lang.String"));
		func0.setArgTypes(new Class[] {
			Class.forName("java.lang.String"),
			Class.forName("java.lang.String")
			});
		func0.setArgNames(new String[] {
			"&pk_calbody",
			"&pk_invmandoc"
			});

		UserDefineFunction[] allFuncs = new UserDefineFunction[1];
		allFuncs[0] = func0;

		return allFuncs;

	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	return null;
}
}
