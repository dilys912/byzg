package nc.vo.bd.b14;

/**
 * 存货分类基本档案VO
 * @author yhj 2014-02-22
 */
import java.util.ArrayList;
import nc.vo.bd.BDMsg;
import nc.vo.pub.*;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

@SuppressWarnings("serial")
public class InvclVO extends CircularlyAccessibleValueObject {

	public InvclVO() {
	}

	public InvclVO(String newPk_invcl) {
		m_pk_invcl = newPk_invcl;
	}

	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (Exception e) {
		}
		InvclVO invcl = (InvclVO) o;
		return invcl;
	}

	public String getEntityName() {
		return "Invcl";
	}

	public String getPrimaryKey() {
		return m_pk_invcl;
	}

	public void setPrimaryKey(String newPk_invcl) {
		m_pk_invcl = newPk_invcl;
	}

	public String getPk_invcl() {
		return m_pk_invcl;
	}

	public String getInvclasscode() {
		return m_invclasscode;
	}

	public String getInvclassname() {
		return m_invclassname;
	}

	public Integer getInvclasslev() {
		return m_invclasslev;
	}

	public UFDouble getAvgprice() {
		return m_avgprice;
	}

	public UFBoolean getEndflag() {
		return m_endflag;
	}

	public void setPk_invcl(String newPk_invcl) {
		m_pk_invcl = newPk_invcl;
	}

	public void setInvclasscode(String newInvclasscode) {
		m_invclasscode = newInvclasscode;
	}

	public void setInvclassname(String newInvclassname) {
		m_invclassname = newInvclassname;
	}

	public String getMemo() {
		return m_memo;
	}

	public void setMemo(String m_memo) {
		this.m_memo = m_memo;
	}

	public void setInvclasslev(Integer newInvclasslev) {
		m_invclasslev = newInvclasslev;
	}

	public void setAvgprice(UFDouble newAvgprice) {
		m_avgprice = newAvgprice;
	}

	public void setEndflag(UFBoolean newEndflag) {
		m_endflag = newEndflag;
	}

	public void validate() throws ValidationException {
		ArrayList errFields = new ArrayList();
		if (m_pk_invcl == null) errFields.add(new String("m_pk_invcl"));
		if (m_invclasscode == null) errFields.add(new String("m_invclasscode"));
		if (m_invclassname == null) errFields.add(new String("m_invclassname"));
		if (m_invclasslev == null) errFields.add(new String("m_invclasslev"));
		StringBuffer message = new StringBuffer();
		message.append(BDMsg.MSG_NULL_FIELD());
		if (errFields.size() > 0) {
			String temp[] = (String[]) (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(",");
				message.append(temp[i]);
			}

			throw new NullFieldException(message.toString());
		} else {
			return;
		}
	}

	public String[] getAttributeNames() {
		return (new String[] {
				"invclasscode", "invclassname", "invclasslev", "avgprice", "endflag", "averagecost", "averagepurahead", "averagemmahead", "memo", "pk_corp"
		});
	}

	public Object getAttributeValue(String attributeName) {
		if (attributeName.equals("pk_invcl")) return m_pk_invcl;
		if (attributeName.equals("invclasscode")) return m_invclasscode;
		if (attributeName.equals("invclassname")) return m_invclassname;
		if (attributeName.equals("invclasslev")) return m_invclasslev;
		if (attributeName.equals("avgprice")) return m_avgprice;
		if (attributeName.equals("endflag")) return m_endflag;
		if (attributeName.equals("averagecost")) return m_averagecost;
		if (attributeName.equals("averagepurahead")) return m_averagepurahead;
		if (attributeName.equals("averagemmahead")) return m_averagemmahead;
		if (attributeName.equals("pk_corp")) return m_pk_corp;
		else if (attributeName.equals("memo")) return m_memo;
		return null;
	}

	public void setAttributeValue(String name, Object value) {
		try {
			if (name.equals("pk_invcl")) m_pk_invcl = (String) value;
			else if (name.equals("invclasscode")) m_invclasscode = (String) value;
			else if (name.equals("invclassname")) m_invclassname = (String) value;
			else if (name.equals("invclasslev")) m_invclasslev = (Integer) value;
			else if (name.equals("avgprice")) m_avgprice = (UFDouble) value;
			else if (name.equals("endflag")) m_endflag = (UFBoolean) value;
			else if (name.equals("averagecost")) m_averagecost = (UFDouble) value;
			else if (name.equals("averagepurahead")) m_averagepurahead = (Integer) value;
			else if (name.equals("averagemmahead")) m_averagemmahead = (Integer) value;
			else if (name.equals("pk_corp")) m_pk_corp = (String) value;
			else if (name.equals("memo")) m_memo = (String) value;
		} catch (ClassCastException e) {
			throw new ClassCastException((new StringBuilder()).append("ClassCastException: AttributeName is ").append(name).toString());
		}
	}

	public static StringField getPk_invclField() {
		if (m_pk_invclField == null) try {
			m_pk_invclField = new StringField();
			m_pk_invclField.setName("pk_invcl");
			m_pk_invclField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_pk_invclField;
	}

	public static StringField getInvclasscodeField() {
		if (m_invclasscodeField == null) try {
			m_invclasscodeField = new StringField();
			m_invclasscodeField.setName("invclasscode");
			m_invclasscodeField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_invclasscodeField;
	}

	public static StringField getInvclassnameField() {
		if (m_invclassnameField == null) try {
			m_invclassnameField = new StringField();
			m_invclassnameField.setName("invclassname");
			m_invclassnameField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_invclassnameField;
	}

	public static IntegerField getInvclasslevField() {
		if (m_invclasslevField == null) try {
			m_invclasslevField = new IntegerField();
			m_invclasslevField.setName("invclasslev");
			m_invclasslevField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_invclasslevField;
	}

	public static UFDoubleField getAvgpriceField() {
		if (m_avgpriceField == null) try {
			m_avgpriceField = new UFDoubleField();
			m_avgpriceField.setName("avgprice");
			m_avgpriceField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_avgpriceField;
	}

	public static UFBooleanField getEndflagField() {
		if (m_endflagField == null) try {
			m_endflagField = new UFBooleanField();
			m_endflagField.setName("endflag");
			m_endflagField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_endflagField;
	}

	public static StringField getMemoField() {
		if (m_memoField == null) try {
			m_memoField = new StringField();
			m_memoField.setName("memo");
			m_memoField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_memoField;
	}

	public FieldObject[] getFields() {
		FieldObject fields[] = {
				getPk_invclField(), getInvclasscodeField(), getInvclassnameField(), getInvclasslevField(), getAvgpriceField(), getEndflagField(), getPk_corpField(), getMemoField()
		};
		return fields;
	}

	public UFDouble getAveragecost() {
		return m_averagecost;
	}

	public static UFDoubleField getAveragecostField() {
		if (m_averagecostField == null) try {
			m_averagecostField = new UFDoubleField();
			m_averagecostField.setName("averagecost");
			m_averagecostField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_averagecostField;
	}

	public Integer getAveragemmahead() {
		return m_averagemmahead;
	}

	public static IntegerField getAveragemmaheadField() {
		if (m_averagemmaheadField == null) try {
			m_averagemmaheadField = new IntegerField();
			m_averagemmaheadField.setName("averagemmahead");
			m_averagemmaheadField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_averagemmaheadField;
	}

	public Integer getAveragepurahead() {
		return m_averagepurahead;
	}

	public static IntegerField getAveragepuraheadField() {
		if (m_averagepuraheadField == null) try {
			m_averagepuraheadField = new IntegerField();
			m_averagepuraheadField.setName("averagepurahead");
			m_averagepuraheadField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_averagepuraheadField;
	}

	public String getPk_corp() {
		return m_pk_corp;
	}

	public static StringField getPk_corpField() {
		if (m_pk_corpField == null) try {
			m_pk_corpField = new StringField();
			m_pk_corpField.setName("pk_corp");
			m_pk_corpField.setLabel("null");
		} catch (Throwable exception) {
			handleException(exception);
		}
		return m_pk_corpField;
	}

	public String getSealdate() {
		return m_sealdate;
	}

	public void setAveragecost(UFDouble newAvgprice) {
		m_averagecost = newAvgprice;
	}

	public void setAveragemmahead(Integer newInvclasslev) {
		m_averagemmahead = newInvclasslev;
	}

	public void setAveragepurahead(Integer newInvclasslev) {
		m_averagepurahead = newInvclasslev;
	}

	public void setPk_corp(String newM_pk_corp) {
		m_pk_corp = newM_pk_corp;
	}

	public void setSealdate(String newM_sealdate) {
		m_sealdate = newM_sealdate;
	}

	public String toString() {
		return (new StringBuilder()).append(getInvclasscode()).append(" ").append(getInvclassname()).toString();
	}

	private String m_memo;
	public String m_pk_invcl;
	public String m_invclasscode;
	public String m_invclassname;
	public Integer m_invclasslev;
	public UFDouble m_avgprice;
	public UFBoolean m_endflag;
	private static StringField m_memoField;
	private static StringField m_pk_invclField;
	private static StringField m_invclasscodeField;
	private static StringField m_invclassnameField;
	private static IntegerField m_invclasslevField;
	private static UFDoubleField m_avgpriceField;
	private static UFBooleanField m_endflagField;
	public UFDouble m_averagecost;
	private static UFDoubleField m_averagecostField;
	public Integer m_averagemmahead;
	private static IntegerField m_averagemmaheadField;
	public Integer m_averagepurahead;
	private static IntegerField m_averagepuraheadField;
	public String m_pk_corp;
	private static StringField m_pk_corpField;
	public String m_sealdate;
}
