package nc.vo.ic.pub;

import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;

public class LPWSJKBVO extends CircularlyAccessibleValueObject {

	private String cinventorycode;//存货编码
	private String vnotebody;//备注
	private UFDouble vuserdef1;//转成垛数
	private UFDouble vuserdef2;//保持成品尾数数
	private UFDouble vuserdef3;//废品数
	private String vbatchcode;//批次号
	private String vproductbatch;//生产订单号
	
	public String getCinventorycode() {
		return cinventorycode;
	}

	public void setCinventorycode(String cinventorycode) {
		this.cinventorycode = cinventorycode;
	}

	public String getVnotebody() {
		return vnotebody;
	}

	public void setVnotebody(String vnotebody) {
		this.vnotebody = vnotebody;
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

	public UFDouble getVuserdef3() {
		return vuserdef3;
	}

	public void setVuserdef3(UFDouble vuserdef3) {
		this.vuserdef3 = vuserdef3;
	}

	public String getVbatchcode() {
		return vbatchcode;
	}

	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}

	public String getVproductbatch() {
		return vproductbatch;
	}

	public void setVproductbatch(String vproductbatch) {
		this.vproductbatch = vproductbatch;
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
		if (attributeName.equals("vuserdef3"))
			   return this.vuserdef3;
		if (attributeName.equals("vnotebody"))
			   return this.vnotebody;
		if (attributeName.equals("vbatchcode"))
			   return this.vbatchcode;
		if (attributeName.equals("vproductbatch"))
			   return this.vproductbatch;
		
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
		      } else if (name.equals("vuserdef3")) {
		        this.vuserdef3 = SwitchObject.switchObjToUFDouble(value);
		      } else if (name.equals("vnotebody")) {
		        this.vnotebody = sTrimedValue;
		      } else if (name.equals("vbatchcode")) {
		        this.vbatchcode = sTrimedValue;
		      } else if (name.equals("vproductbatch")) {
		        this.vproductbatch = sTrimedValue;
		      } 
		    }
		    catch (ClassCastException e) {
		      throw new ClassCastException();
		    }
	}

	@Override
	public String getEntityName() {
		return "LPWSJKBVO";
	}

	@Override
	public void validate() throws ValidationException {
		
	}

}
