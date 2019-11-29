package nc.ui.dm.pub.ref;

import nc.ui.pub.ClientEnvironment;

/**
 * 承运商参照模型。
 * 创建日期：(2002-5-24 13:02:30)
 * @author：左小军
 */
public class TrancustRefModel extends nc.ui.bd.ref.AbstractRefModel {
	private int m_DefaultFieldCount= 3;
	private String[] m_aryFieldCode= { "custcode", "custname", "bd_delivorg.vdoname", "pk_trancust"  };
	private String[] m_aryFieldName= { 
	        nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002077")/*@res "承运商编码"*/, 
	        nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002073")/*@res "承运商名称"*/, 
	        nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0001039")/*@res "发运组织名称"*/, 
	        nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0002072")/*@res "承运商主键"*/ };
	private String m_sPkFieldCode= "pk_trancust";
	private String[] m_sHideFieldCode= {"bd_cumandoc.pk_cumandoc"};
	private String m_sRefTitle= nc.ui.ml.NCLangRes.getInstance().getStrByID("scmpub","UPPscmpub-000247")/*@res "承运商参照"*/;
	private String m_sTableName=
		"bd_cubasdoc inner join dm_trancust on bd_cubasdoc.pk_cubasdoc=dm_trancust.pkcusmandoc inner join bd_cumandoc on bd_cumandoc.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc inner join bd_delivorg on bd_delivorg.pk_delivorg=dm_trancust.pkdelivorg and bd_delivorg.dr=0 left join dm_baseprice on dm_trancust.pk_trancust = dm_baseprice.pk_transcust ";
	//eric 2012-8-13 增加根据价格表过滤
	     //private String m_sTableName=
		//"bd_cubasdoc  join bd_cumandoc on bd_cubasdoc.pk_cubasdoc=bd_cumandoc.pk_cubasdoc join dm_trancust on bd_cumandoc.pk_cumandoc=dm_trancust.pkcusmandoc";
/**
 * RouteRefModel 构造子注解。
 */
public TrancustRefModel() {
	super();
  String part = "where bd_cumandoc.custflag in ('0','2') and dm_trancust.dr = 0 and bd_cumandoc.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPk_corp() +"' " ;
	setWherePart( part );
	setGroupPart(" custcode, custname, bd_delivorg.vdoname, pk_trancust, bd_cumandoc.pk_cumandoc");
}
/**
 * getDefaultFieldCount 方法注解。
 */
public int getDefaultFieldCount() {
	return m_DefaultFieldCount;
}
/**
 * 显示字段列表
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldCode() {
	return m_aryFieldCode;
}
/**
 * 显示字段中文名
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
public java.lang.String[] getFieldName() {
	return m_aryFieldName;
}
/**
 * 此处插入方法说明。
 * 创建日期：(2001-9-6 10:56:48)
 */
public String[] getHiddenFieldCode() {
	return m_sHideFieldCode;
}
/**
 * 主键字段名
 * @return java.lang.String
 */
public String getPkFieldCode() {
	return m_sPkFieldCode;
}
/**
 * 参照标题
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
public String getRefTitle() {
	return m_sRefTitle;
}
/**
 * 参照数据库表或者视图名
 * 创建日期：(01-4-4 0:57:23)
 * @return java.lang.String
 */
public String getTableName() {
	return m_sTableName;
}

/**
 * @return 返回 isMatchPkWithWherePart。
 */
protected boolean isMatchPkWithWherePart() {
	return true;
}
/**
 * 置入发运组织PK。
 * 功能：
 * 参数：
 * 返回：
 * 例外：
 * 日期：(2002-9-26 15:11:32)
 * 修改日期，修改人，修改原因，注释标志：
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