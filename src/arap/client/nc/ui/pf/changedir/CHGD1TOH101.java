package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

/**
 * ����TC20TOTB90��VO�Ķ�̬ת���ࡣ
 * 
 * �������ڣ�(2007-11-13)
 * 
 * @author��ƽ̨�ű�����
 */
public class CHGD1TOH101 extends nc.ui.pf.change.VOConversionUI {
	/**
	 * CHGTC20TOTB90 ������ע�⡣
	 */
	public CHGD1TOH101() {
		super();
	}

	/**
	 * ��ú������ȫ¼�����ơ�
	 * 
	 * @return java.lang.String[]
	 */
	public String getAfterClassName() {
		return null;
	}

	/**
	 * �����һ���������ȫ¼�����ơ�
	 * 
	 * @return java.lang.String[]
	 */
	public String getOtherClassName() {
		return null;
	}

	/**
	 * ����ֶζ�Ӧ��
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getField() {
		return new String[] { 
//				"B_vdef1->B_vdef1", // ������B_��ʼ ��ͷ������H_��ʼ
				"B_ysdbh->B_vouchid",
				"B_yfdzbzj->B_fb_oid",
				"B_vdef1->B_zyx1",
				"B_vdef2->B_zyx2",
				"B_vdef3->B_zyx3",
				"B_vdef4->B_zyx4",
				"B_vdef5->B_zyx5",
				"B_vdef6->B_zyx6",
				"B_vdef7->B_zyx7",
				"B_vdef8->B_zyx8",
				"B_vdef9->B_zyx9",
				"B_vdef10->B_zyx10",
				"B_pk_corp->B_dwbm",
				"B_pk_ywy->B_ywybm",
				"B_pk_zfr->B_payman",
				"B_pk_szxm->B_szxmid",
				"B_zy->B_zy",
				"B_pk_bz->B_bzbm",
				"B_pk_jsfs->B_pj_jsfs",
				"B_pk_sfkxy->B_sfkxyh",
				"B_pk_wldx->B_wldx",
				"B_pk_bm->B_deptid",
				"B_pk_ks->B_ordercusmandoc",
				"B_pk_gys->B_hbbm",
				"B_pk_zx->B_jobid",
				"B_pk_zxxm->B_jobphaseid",
				"B_pk_xjllxm->B_cashitem",
				"B_bbhl->B_bbhl",
				"B_fbhl->B_fbhl",
				"B_dfybje->B_dfybje",
				"B_dffbje->B_dffbje",
				"B_dfbbje->B_dfbbje",
				"B_dfybwsje->B_dfybwsje",
				"B_dffbwsje->B_wbffbje",
				"B_dfbbwsje->B_dfbbwsje",
				"B_dfybsj->B_dfybsj",
				"B_dffbsj->B_dffbsj",
				"B_dfbbsj->B_dfbbsj",
				"B_ybye->B_ybye",
				"B_fbye->B_fbye",
				"B_bbye->B_bbye",
				"B_pk_ch->B_cinventoryid",
				"B_fkyhzh->B_bfyhzh",
				"B_skyhzh->B_dfyhzh",
				"B_dfsl->B_dfshl",
				"B_hsdj->B_hsdj",
				"B_kslb->B_kslb",
				"B_sl->B_sl",
				"B_ddh->B_ddh",
				"B_pk_xmmc->B_xm",
				"B_pk_sybm->B_usedept",
				"B_pk_km->B_kmbm",
				"B_qxrq->B_qxrq",
				"B_fph->B_fph",
				"B_zyx16->B_zyx16"
				};
	}

	/**
	 * ��ù�ʽ��
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getFormulas() {
		return null;
	}

	/**
	 * �����û��Զ��庯����
	 */
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
