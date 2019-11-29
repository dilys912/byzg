package nc.vo.ic.pub;

import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;

public class FJJKBVO extends CircularlyAccessibleValueObject {

	private String cinventorycode;//存货编码
	private UFDouble vuserdef1;//成品尾数
	private UFDouble vuserdef2;//废品数
	
	public String getCinventorycode() {
		return cinventorycode;
	}

	public void setCinventorycode(String cinventorycode) {
		this.cinventorycode = cinventorycode;
	}

	public UFDouble getVuserdef1() {
		return vuserdef1;
	}

	public void setVuserdef1(UFDouble vuserdef1) {
		this.vuserdef1 = vuserdef1;
	}

	public UFDouble getVuserdef2() {
		return vuserdef2;
	}

	public void setVuserdef2(UFDouble vuserdef2) {
		this.vuserdef2 = vuserdef2;
	}


	@Override
	public String[] getAttributeNames() {
		return new String[]{"cinventorycode","vuserdef1","vuserdef2","vuserdef3"};
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		
		if (attributeName.equals("cinventorycode"))
			   return this.cinventorycode;
		if (attributeName.equals("vuserdef1"))
			   return this.vuserdef1;
		if (attributeName.equals("vuserdef2"))
			   return this.vuserdef2;		
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
		      } else if (name.equals("vuserdef1")) {
		        this.vuserdef1 = SwitchObject.switchObjToUFDouble(value);
		      } else if (name.equals("vuserdef2")) {
		        this.vuserdef2 = SwitchObject.switchObjToUFDouble(value);
		      } 
		    }
		    catch (ClassCastException e) {
		      throw new ClassCastException();
		    }
	}

	@Override
	public String getEntityName() {
		return "FJJKBVO";
	}

	@Override
	public void validate() throws ValidationException {
		
	}

}
