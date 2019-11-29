package nc.vo.uap.itfcheck;

import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.pub.SuperVO;

public class XmlBEntity extends SuperVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pk_config_b;
	private String pk_config_h;
	private Integer ordernum;//排序号
	private String headorbady;//表头、表体
	private String fieldcode;//接口字段
	private String fieldtype;//字段类型
	private Integer fieldlength;//字段长度
	private Integer fieldprecise;//数值精度
	private String fieldexplain;//字段描述
	private String sendsysfield;
	private String receivesysfield;
	private String ismustenter;//是否必输项
	private String isnull;//是否可为空
	private String checkcode;
	private String pk_corp;
	private Integer dr;
	private String ts;

	public String[] getAttributeNames() {
		return new String[] { "pk_config_b", "pk_config_h", "ordernum",
				"headorbady", "fieldcode", "fieldtype", "fieldlength",
				"fieldprecise", "fieldexplain", "sendsysfield",
				"receivesysfield", "ismustenter","isnull","checkcode","pk_corp", "dr", "ts" };
	}

	public Object getAttributeValue(String attributeName) {

		if (attributeName.equals("pk_config_b"))
			return this.pk_config_b;
		if (attributeName.equals("pk_config_h"))
			return this.pk_config_h;
		if (attributeName.equals("ordernum"))
			return this.ordernum;
		if (attributeName.equals("headorbady"))
			return this.headorbady;
		if (attributeName.equals("fieldcode"))
			return this.fieldcode;
		if (attributeName.equals("fieldtype"))
			return this.fieldtype;
		if (attributeName.equals("fieldlength"))
			return this.fieldlength;
		if (attributeName.equals("fieldprecise"))
			return this.fieldprecise;
		if (attributeName.equals("fieldexplain"))
			return this.fieldexplain;
		if (attributeName.equals("sendsysfield"))
			return this.sendsysfield;
		if (attributeName.equals("receivesysfield"))
			return this.receivesysfield;
		if (attributeName.equals("ismustenter"))
			return this.ismustenter;
		if (attributeName.equals("isnull"))
			return this.isnull;
		if (attributeName.equals("checkcode"))
			return this.checkcode;
		if (attributeName.equals("pk_corp"))
			return this.pk_corp;
		if (attributeName.equals("dr"))
			return this.dr;
		if (attributeName.equals("ts"))
			return this.ts;
		return null;
	}

	public void setAttributeValue(String name, Object value) {
		String sTrimedValue = null;

		if (value != null)
			if ((value instanceof String)) {
				sTrimedValue = (String) value;
				if (sTrimedValue.trim().length() == 0)
					sTrimedValue = null;
			} else {
				sTrimedValue = value.toString().trim();
			}
		try {
			if (name.equals("pk_config_b")) {
				this.pk_config_b = sTrimedValue;
			} else if (name.equals("pk_config_h")) {
				this.pk_config_h = sTrimedValue;
			} else if (name.equals("ordernum")) {
				this.ordernum = SwitchObject.switchObjToInteger(value);
			} else if (name.equals("headorbady")) {
				this.headorbady = sTrimedValue;
			} else if (name.equals("fieldcode")) {
				this.fieldcode = sTrimedValue;
			} else if (name.equals("fieldtype")) {
				this.fieldtype = sTrimedValue;
			} else if (name.equals("fieldlength")) {
				this.fieldlength = SwitchObject.switchObjToInteger(value);
			} else if (name.equals("fieldprecise")) {
				this.fieldprecise = SwitchObject.switchObjToInteger(value);
			} else if (name.equals("fieldexplain")) {
				this.fieldexplain = sTrimedValue;
			} else if (name.equals("sendsysfield")) {
				this.sendsysfield = sTrimedValue;
			} else if (name.equals("receivesysfield")) {
				this.receivesysfield = sTrimedValue;
			} else if (name.equals("ismustenter")) {
				this.ismustenter = sTrimedValue;
			} else if (name.equals("isnull")) {
				this.isnull = sTrimedValue;
			} else if (name.equals("checkcode")) {
				this.checkcode = sTrimedValue;
			} else if (name.equals("pk_corp")) {
				this.pk_corp = sTrimedValue;
			} else if (name.equals("dr")) {
				this.dr = SwitchObject.switchObjToInteger(value);
			} else if (name.equals("ts")) {
				this.ts = sTrimedValue;
			}
		} catch (ClassCastException e) {
			throw new ClassCastException();
		}
	}

	public String getPk_config_b() {
		return pk_config_b;
	}

	public void setPk_config_b(String pk_config_b) {
		this.pk_config_b = pk_config_b;
	}

	public String getPk_config_h() {
		return pk_config_h;
	}

	public void setPk_config_h(String pk_config_h) {
		this.pk_config_h = pk_config_h;
	}

	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}

	public String getHeadorbady() {
		return headorbady;
	}

	public void setHeadorbady(String headorbady) {
		this.headorbady = headorbady;
	}

	public String getFieldcode() {
		return fieldcode;
	}

	public void setFieldcode(String fieldcode) {
		this.fieldcode = fieldcode;
	}

	public String getFieldtype() {
		return fieldtype;
	}

	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}

	public Integer getFieldlength() {
		return fieldlength;
	}

	public void setFieldlength(Integer fieldlength) {
		this.fieldlength = fieldlength;
	}

	public Integer getFieldprecise() {
		return fieldprecise;
	}

	public void setFieldprecise(Integer fieldprecise) {
		this.fieldprecise = fieldprecise;
	}

	public String getFieldexplain() {
		return fieldexplain;
	}

	public void setFieldexplain(String fieldexplain) {
		this.fieldexplain = fieldexplain;
	}

	public String getSendsysfield() {
		return sendsysfield;
	}

	public void setSendsysfield(String sendsysfield) {
		this.sendsysfield = sendsysfield;
	}

	public String getReceivesysfield() {
		return receivesysfield;
	}

	public void setReceivesysfield(String receivesysfield) {
		this.receivesysfield = receivesysfield;
	}

	public String getIsMustEnter() {
		return ismustenter;
	}

	public void setIsMustEnter(String ismustenter) {
		this.ismustenter = ismustenter;
	}

	public String getIsNull() {
		return isnull;
	}
	
	public void setIsNull(String isnull) {
		this.isnull = isnull;
	}
	
	public String getCheckcode() {
		return checkcode;
	}

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	
	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}
	
	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return pk_config_b;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_config_b";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_config_h";
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "itf_config_b";
	}

}
