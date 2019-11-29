package nc.vo.ic.pub;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;

public class LPWSJKHVO extends CircularlyAccessibleValueObject {

	private String coperatorid;//制单人1 
	private String dbilldate;//单据日期 1
	private String vuserdef1;//产品类型 1
	private String vnote;//备注 1
	
	
	public String getCoperatorid() {
		return coperatorid;
	}

	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}

	public String getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(String dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getVuserdef1() {
		return vuserdef1;
	}

	public void setVuserdef1(String vuserdef1) {
		this.vuserdef1 = vuserdef1;
	}

	public String getVnote() {
		return vnote;
	}

	public void setVnote(String vnote) {
		this.vnote = vnote;
	}

	@Override
	public String[] getAttributeNames() {
		return new String[]{"coperatorid","vnote","dbilldate","vuserdef1"};
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		
		if (attributeName.equals("coperatorid"))
			   return this.coperatorid;
		if (attributeName.equals("vnote"))
			   return this.vnote;
		if (attributeName.equals("dbilldate"))
			   return this.dbilldate;
		if (attributeName.equals("vuserdef1"))
			   return this.vuserdef1;
		return null;
	}

	@Override
	public void setAttributeValue(String name, Object value) {
		 String sTrimedValue = null;

		    if (value != null)
		      if ((value instanceof String)) {
		        sTrimedValue = (String)value;
		        if (sTrimedValue.trim().length() == 0)
		          sTrimedValue = null;
		      } else {
		        sTrimedValue = value.toString().trim();
		      }
		    try
		    {
		      if (name.equals("coperatorid")) {
		        this.coperatorid = sTrimedValue;
		      } else if (name.equals("vnote")) {
		        this.vnote = sTrimedValue;
		      } else if (name.equals("dbilldate")) {
		        this.dbilldate = sTrimedValue;
		      } else if (name.equals("vuserdef1")) {
		        this.vuserdef1 = sTrimedValue;
		      } 
		    }
		    catch (ClassCastException e) {
		      throw new ClassCastException();
		    }
	}

	@Override
	public String getEntityName() {
		return "LPWSJKHVO";
	}

	@Override
	public void validate() throws ValidationException {
		
	}


}
