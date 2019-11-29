package nc.vo.ic.pub;

import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFDouble;

public class CpdljkBVO extends CircularlyAccessibleValueObject {

	private String cinventorycode;//存货code
	private UFDouble nshouldinnum;//应收数量
	private UFDouble ninnum;//实收数量
	private String vnotebody;//备注
	
	
	
	public String getCinventorycode() {
		return cinventorycode;
	}

	public void setCinventorycode(String cinventorycode) {
		this.cinventorycode = cinventorycode;
	}

	public UFDouble getNshouldinnum() {
		return nshouldinnum;
	}

	public void setNshouldinnum(UFDouble nshouldinnum) {
		this.nshouldinnum = nshouldinnum;
	}

	public UFDouble getNinnum() {
		return ninnum;
	}

	public void setNinnum(UFDouble ninnum) {
		this.ninnum = ninnum;
	}

	public String getVnotebody() {
		return vnotebody;
	}

	public void setVnotebody(String vnotebody) {
		this.vnotebody = vnotebody;
	}


	@Override
	public String[] getAttributeNames() {
		return new String[]{"cinventorycode","vnotebody","nshouldinnum","ninnum"};
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		
		if (attributeName.equals("cinventorycode"))
			   return this.cinventorycode;
		if (attributeName.equals("vnotebody"))
			   return this.vnotebody;
		if (attributeName.equals("nshouldinnum"))
			   return this.nshouldinnum;
		if (attributeName.equals("ninnum"))
			   return this.ninnum;
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
		      if (name.equals("cinventorycode")) {
		        this.cinventorycode = sTrimedValue;
		      } else if (name.equals("vnotebody")) {
		        this.vnotebody = sTrimedValue;
		      } else if (name.equals("nshouldinnum")) {
		        this.nshouldinnum = SwitchObject.switchObjToUFDouble(value);
		      } else if (name.equals("ninnum")) {
		        this.ninnum = SwitchObject.switchObjToUFDouble(value);
		      }
		    }
		    catch (ClassCastException e) {
		      throw new ClassCastException();
		    }
	}

	@Override
	public String getEntityName() {
		return "Cpdlbillbody";
	}

	@Override
	public void validate() throws ValidationException {
		
	}

}
