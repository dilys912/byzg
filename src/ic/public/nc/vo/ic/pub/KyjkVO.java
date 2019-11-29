package nc.vo.ic.pub;

import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;

public class KyjkVO extends CircularlyAccessibleValueObject {

	private String cust;//客商
	private String maker;//制单人
	private String billdate;//单据日期
	private String memo;//备注
	private String vbatchcode;//
	private String vproductbatch;//备注
	

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

	public String getCust() {
		return cust;
	}

	public void setCust(String cust) {
		this.cust = cust;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}


	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String[] getAttributeNames() {
		return new String[]{"vbatchcode","cust","maker","vproductbatch","billdate","memo"};
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		
		if (attributeName.equals("vbatchcode"))
		   return this.vbatchcode;
		if (attributeName.equals("cust"))
			   return this.cust;
		if (attributeName.equals("maker"))
			   return this.maker;
		if (attributeName.equals("vproductbatch"))
			   return this.vproductbatch;
		if (attributeName.equals("billdate"))
			   return this.billdate;
		if (attributeName.equals("memo"))
			   return this.memo;
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
		      if (name.equals("vbatchcode")) {
		        this.vbatchcode = sTrimedValue;
		      } else if (name.equals("cust")) {
		        this.cust = sTrimedValue;
		      } else if (name.equals("maker")) {
		        this.maker = sTrimedValue;
		      } else if (name.equals("vproductbatch")) {
		        this.vproductbatch = sTrimedValue;
		      } else if (name.equals("billdate")) {
		        this.billdate = sTrimedValue;
		      } else if (name.equals("memo")) {
		        this.memo = sTrimedValue;
		      } 
		    }
		    catch (ClassCastException e) {
		      throw new ClassCastException();
		    }
	}

	@Override
	public String getEntityName() {
		return "Kybillhead";
	}

	@Override
	public void validate() throws ValidationException {
		
	}


}
