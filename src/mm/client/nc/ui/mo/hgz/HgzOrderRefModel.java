package nc.ui.mo.hgz;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.common.util.Util;

public class HgzOrderRefModel extends AbstractRefModel {

	public HgzOrderRefModel() {
	}

	public HgzOrderRefModel(String name) {

	}

	public int getDefaultFieldCount() {
		return 8;
	}

	public String[] getFieldCode() {
		return new String[] {
				"scddh", "bd_invbasdoc.invcode", "bd_invbasdoc.invname", "bd_invbasdoc.invspec", "jhkgrq", "jhwgrq", "jhwgsl", "rtver"
		};
	}

	public String[] getFieldName() {
		return new String[] {
				"��������", "��Ʒ����", "��Ʒ����", "���", "�ƻ���������", "�ƻ��깤����", "�ƻ�����", "����·�߰汾��"
		};
	}

	public String[] getHiddenFieldCode() {
		return new String[] {
			"mm_mo.pk_moid"
		};
	}

	public String getPkFieldCode() {
		return "mm_mo.pk_moid";
	}

	public String getRefTitle() {
		return "��������";
	}

	public String getRefNameField() {
		return "scddh";
	}

	public String getTableName() {
		return " mm_mo left outer join bd_invbasdoc on mm_mo.wlbmid=bd_invbasdoc.pk_invbasdoc left join mm_mokz on mm_mo.pk_moid = mm_mokz.pk_moid";
	}

	public String getWherePart() {
		return " nvl(mm_mo.dr,0)=0  and mm_mo.zt = 'B' and mm_mo.pk_cORp='" + Util.getCorp() + "'";
	}
}