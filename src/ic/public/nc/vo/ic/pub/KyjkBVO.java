package nc.vo.ic.pub;

import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;

public class KyjkBVO extends CircularlyAccessibleValueObject {

	private String inventory;//´æ»õ
	private UFDouble number;//ÊýÁ¿
	
	

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public UFDouble getNumber() {
		return number;
	}

	public void setNumber(UFDouble number) {
		this.number = number;
	}

	@Override
	public String[] getAttributeNames() {
		return new String[]{"inventory","number"};
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		
		if (attributeName.equals("inventory"))
			   return this.inventory;
		if (attributeName.equals("number"))
			   return this.number;
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
		      if (name.equals("inventory")) {
		        this.inventory = sTrimedValue;
		      } else if (name.equals("number")) {
		        this.number = SwitchObject.switchObjToUFDouble(value);
		      } 
		    }
		    catch (ClassCastException e) {
		      throw new ClassCastException();
		    }
	}

	@Override
	public String getEntityName() {
		return "Kybillbody";
	}

	@Override
	public void validate() throws ValidationException {
		
	}

}
