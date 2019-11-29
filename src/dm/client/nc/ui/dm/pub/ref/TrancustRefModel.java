package nc.ui.dm.pub.ref;

import nc.ui.pub.ClientEnvironment;

/**
 * �����̲���ģ�͡�
 * �������ڣ�(2002-5-24 13:02:30)
 * @author����С��
 */
public class TrancustRefModel extends nc.ui.bd.ref.AbstractRefModel {
	private int m_DefaultFieldCount= 3;
	private String[] m_aryFieldCode= { "custcode", "custname", "bd_delivorg.vdoname", "pk_trancust"  };
	private String[] m_aryFieldName= { 
	        nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002077")/*@res "�����̱���"*/, 
	        nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002073")/*@res "����������"*/, 
	        nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001039")/*@res "������֯����"*/, 
	        nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002072")/*@res "����������"*/ };
	private String m_sPkFieldCode= "pk_trancust";
	private String[] m_sHideFieldCode= {"bd_cumandoc.pk_cumandoc"};
	private String m_sRefTitle= nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000247")/*@res "�����̲���"*/;
	private String m_sTableName=
		"bd_cubasdoc inner join dm_trancust on bd_cubasdoc.pk_cubasdoc=dm_trancust.pkcusmandoc inner join bd_cumandoc on bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc inner join bd_delivorg on bd_delivorg.pk_delivorg=dm_trancust.pkdelivorg and bd_delivorg.dr=0 left join dm_baseprice on dm_trancust.pk_trancust = dm_baseprice.pk_transcust ";
	//eric 2012-8-13 ���Ӹ��ݼ۸�����
	     //private String m_sTableName=
		//"bd_cubasdoc  join bd_cumandoc on bd_cubasdoc.pk_cubasdoc=bd_cumandoc.pk_cubasdoc join dm_trancust on bd_cumandoc.pk_cumandoc=dm_trancust.pkcusmandoc";
/**
 * RouteRefModel ������ע�⡣
 */
public TrancustRefModel() {
	super();
  String part = "where bd_cumandoc.custflag in ('0','2') and dm_trancust.dr = 0 and bd_cumandoc.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPk_corp() +"' " ;
	setWherePart( part );
	setGroupPart(" custcode, custname, bd_delivorg.vdoname, pk_trancust, bd_cumandoc.pk_cumandoc");
}
/**
 * getDefaultFieldCount ����ע�⡣
 */
public int getDefaultFieldCount() {
	return m_DefaultFieldCount;
}
/**
 * ��ʾ�ֶ��б�
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldCode() {
	return m_aryFieldCode;
}
/**
 * ��ʾ�ֶ�������
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldName() {
	return m_aryFieldName;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-6 10:56:48)
 */
public String[] getHiddenFieldCode() {
	return m_sHideFieldCode;
}
/**
 * �����ֶ���
 * @return java.lang.String
 */
public String getPkFieldCode() {
	return m_sPkFieldCode;
}
/**
 * ���ձ���
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public String getRefTitle() {
	return m_sRefTitle;
}
/**
 * �������ݿ�������ͼ��
 * �������ڣ�(01-4-4 0:57:23)
 * @return java.lang.String
 */
public String getTableName() {
	return m_sTableName;
}

/**
 * @return ���� isMatchPkWithWherePart��
 */
protected boolean isMatchPkWithWherePart() {
	return true;
}
/**
 * ���뷢����֯PK��
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-9-26 15:11:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param sDelivOrgPK java.lang.String
 */
public void setDelivOrgPK(String pkdelivorg) {
  String part = "where bd_cumandoc.custflag in ('1','2') and dm_trancust.dr = 0 and bd_cumandoc.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPk_corp() +"' " ;
  if( pkdelivorg != null ){
    part = part + " and dm_trancust.pkdelivorg='" + pkdelivorg + "'";
  }
	setWherePart( part );
}
}