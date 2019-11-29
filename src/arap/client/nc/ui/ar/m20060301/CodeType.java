package nc.ui.ar.m20060301;

import nc.ui.bd.ref.AbstractRefModel;

public class CodeType extends AbstractRefModel{
	
	public CodeType() {
	}

	public CodeType(String name) {

	}

	public int getDefaultFieldCount() {
		return 2;
	}

	public String[] getFieldCode() {
		return new String[] {
				 "typeno", "instructions"
		};
	}

	public String[] getFieldName() {
		return new String[] {
				"��Ʊ���ͱ���", "��Ʊ����"
		};
	}
	
	public String[] getHiddenFieldCode() {
		return new String[] {
			"PK_INVOICE"
		};
	}


	public String getPkFieldCode() {
		return "PK_INVOICE";
	}

	public String getRefTitle() {
		return "��Ʊ����";
	}

	public String getRefNameField() {
		return "typeno";
	}

	public String getTableName() {
		return "MDMINVOICETYPE";
	}

	public String getWherePart() {
		return "";
	}

}
