package nc.vo.uap.itfcheck;

import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.pub.SuperVO;

public class XmlHEntity extends SuperVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pk_config_h;
	private String itfcode;
	private String itfname;
	private String itftype;
	private String itfaddress;
	private String sendsys;
	private String receivesys;
	private String receivedatabase;
	private String datatype;
	private String vnote;
	private String msgtoken;
	private String password;
	private String serviceid;
	private String sourceappcode;
	private String tempfield;
	private String version;
	private String pk_corp;
	private Integer dr;
	private String ts;
	//add by wang 2019-08-13 º”»Îxbus≈‰÷√◊÷∂Œ

	public String[] getAttributeNames() {
		return new String[] {"pk_config_h", "itfcode","itfname", "itftype", "itfaddress", "sendsys", "receivesys",
				"receivedatabase", "datatype", "vnote","msgtoken","password","serviceid","sourceappcode",
				"tempfield","version", "pk_corp", "dr","ts" };
	}

	public Object getAttributeValue(String attributeName) {
		if (attributeName.equals("pk_config_h"))
			return this.pk_config_h;
		if (attributeName.equals("itfcode"))
			return this.itfcode;
		if (attributeName.equals("itfname"))
			return this.itfname;
		if (attributeName.equals("itftype"))
			return this.itftype;
		if (attributeName.equals("itfaddress"))
			return this.itfaddress;
		if (attributeName.equals("sendsys"))
			return this.sendsys;
		if (attributeName.equals("receivesys"))
			return this.receivesys;
		if (attributeName.equals("receivedatabase"))
			return this.receivedatabase;
		if (attributeName.equals("datatype"))
			return this.datatype;
		if (attributeName.equals("vnote"))
			return this.vnote;
		if (attributeName.equals("pk_corp"))
			return this.pk_corp;
		if (attributeName.equals("dr"))
			return this.dr;
		if (attributeName.equals("ts"))
			return this.ts;
		if (attributeName.equals("msgtoken"))
			return this.msgtoken;
		if (attributeName.equals("password"))
			return this.password;
		if (attributeName.equals("serviceid"))
			return this.serviceid;
		if (attributeName.equals("sourceappcode"))
			return this.sourceappcode;
		if (attributeName.equals("tempfield"))
			return this.tempfield;
		if (attributeName.equals("version"))
			return this.version;
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
			if (name.equals("pk_config_h")) {
				this.pk_config_h = sTrimedValue;
			} else if (name.equals("itfcode")) {
				this.itfcode = sTrimedValue;
			} else if (name.equals("itfname")) {
				this.itfname = sTrimedValue;
			} else if (name.equals("itftype")) {
				this.itftype = sTrimedValue;
			} else if (name.equals("itfaddress")) {
				this.itfaddress = sTrimedValue;
			} else if (name.equals("sendsys")) {
				this.sendsys = sTrimedValue;
			} else if (name.equals("receivesys")) {
				this.receivesys = sTrimedValue;
			} else if (name.equals("receivedatabase")) {
				this.receivedatabase = sTrimedValue;
			} else if (name.equals("datatype")) {
				this.datatype = sTrimedValue;
			} else if (name.equals("vnote")) {
				this.vnote = sTrimedValue;
			} else if (name.equals("pk_corp")) {
				this.pk_corp = sTrimedValue;
			} else if (name.equals("dr")) {
				this.dr = SwitchObject.switchObjToInteger(value);
			} else if (name.equals("ts")) {
				this.ts = sTrimedValue;
			} else if (name.equals("msgtoken")) {
				this.msgtoken = sTrimedValue;
			} else if (name.equals("password")) {
				this.password = sTrimedValue;
			} else if (name.equals("serviceid")) {
				this.serviceid = sTrimedValue;
			} else if (name.equals("sourceappcode")) {
				this.sourceappcode = sTrimedValue;
			} else if (name.equals("tempfield")) {
				this.tempfield = sTrimedValue;
			} else if (name.equals("version")) {
				this.version = sTrimedValue;
			}
		} catch (ClassCastException e) {
			throw new ClassCastException();
		}
	}

	public String getPk_config_h() {
		return pk_config_h;
	}

	public void setPk_config_h(String pk_config_h) {
		this.pk_config_h = pk_config_h;
	}

	public String getItfcode() {
		return itfcode;
	}

	public void setItfcode(String itfcode) {
		this.itfcode = itfcode;
	}

	public String getItfname() {
		return itfname;
	}

	public void setItfname(String itfname) {
		this.itfname = itfname;
	}

	public String getItftype() {
		return itftype;
	}

	public void setItftype(String itftype) {
		this.itftype = itftype;
	}

	public String getItfaddress() {
		return itfaddress;
	}

	public void setItfaddress(String itfaddress) {
		this.itfaddress = itfaddress;
	}

	public String getSendsys() {
		return sendsys;
	}

	public void setSendsys(String sendsys) {
		this.sendsys = sendsys;
	}

	public String getReceivesys() {
		return receivesys;
	}

	public void setReceivesys(String receivesys) {
		this.receivesys = receivesys;
	}

	public String getReceivedatabase() {
		return receivedatabase;
	}

	public void setReceivedatabase(String receivedatabase) {
		this.receivedatabase = receivedatabase;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getVnote() {
		return vnote;
	}

	public void setVnote(String vnote) {
		this.vnote = vnote;
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
	
	public String getMsgtoken() {
		return msgtoken;
	}

	public void setMsgtoken(String msgtoken) {
		this.msgtoken = msgtoken;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getSourceappcode() {
		return sourceappcode;
	}

	public void setSourceappcode(String sourceappcode) {
		this.sourceappcode = sourceappcode;
	}

	public String getTempfield() {
		return tempfield;
	}

	public void setTempfield(String tempfield) {
		this.tempfield = tempfield;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return pk_config_h;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_config_h";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "itf_config_h";
	}

}
