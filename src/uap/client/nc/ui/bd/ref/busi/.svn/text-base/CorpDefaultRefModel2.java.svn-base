package nc.ui.bd.ref.busi;

import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.vo.bd.ref.RefIconConfigVO;

/**
 * 公司目录,权限公司目录
 * @version NC5.011
 * @author shikun
 */
public class CorpDefaultRefModel2 extends AbstractRefTreeModel {

	public CorpDefaultRefModel2(String refNodeName) {
		setRefNodeName(refNodeName);
		// TODO 自动生成构造函数存根
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		// *根据需求设置相应参数

		setFieldCode(new String[] { "bd_corp.unitcode", "bd_corp.unitname" });
		setHiddenFieldCode(new String[] { "bd_corp.pk_corp",
				"bd_corp.fathercorp"});

		setFatherField("bd_corp.fathercorp");
		setChildField("bd_corp.pk_corp");
		setPkFieldCode("bd_corp.pk_corp");

		setStrPatch("distinct");

		setRefTitle("公司目录（权限）");
		setStrPatch("distinct");

		//
		if (refNodeName.indexOf("权限") < 0) {
//			setTableName("bd_corp");
//			setWherePart(" ishasaccount='Y' ");
			setTableName("bd_corp INNER JOIN sm_user_role ON bd_corp.pk_corp = sm_user_role.pk_corp ");
			setWherePart("bd_corp.ishasaccount='Y'and sm_user_role.cuserid ='"
					+ getPk_user() + "' ");
			// 权限公司目录
		} else {
			setTableName("bd_corp INNER JOIN sm_user_role ON bd_corp.pk_corp = sm_user_role.pk_corp ");
			setWherePart("bd_corp.ishasaccount='Y'and sm_user_role.cuserid ='"
					+ getPk_user() + "' ");

		}
		RefIconConfigVO iconcfgVO = new RefIconConfigVO();
		iconcfgVO.setIconKey("树.公司目录");
		setIconCfgVO(iconcfgVO);
		resetFieldName();
		setOrderPart("bd_corp.unitcode");
	}

	/**
	 * 参照标题 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getRefTitle() {
		// 登陆界面多语言的问题。
		setFieldName(null);
		resetFieldName();
		// 每次翻译
		setRefTitle(null);
		return super.getRefTitle();

	}
}
