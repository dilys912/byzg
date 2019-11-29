package nc.vo.ic.pub;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

public class CpdljkVO extends CircularlyAccessibleValueObject {

	private String cbilltypecode;//生成订单号
	private String ccustomerid;//客商
	private String coperatorid;//制单人
	private String dbilldate;//单据日期
	private String vnote;//备注
	private String vuserdef1;//产品类型
	private String vuserdef2;//生成批次号
	//add by zwx 2017-6-12 增加导入xml的文件名
	private String xmlname;//xml文件名
	
	public String getXmlname() {
		return xmlname;
	}

	public void setXmlname(String xmlname) {
		this.xmlname = xmlname;
	}
	
	//end by zwx 

	public String getVuserdef2() {
		return vuserdef2;
	}

	public void setVuserdef2(String vuserdef2) {
		this.vuserdef2 = vuserdef2;
	}

	public String getCbilltypecode() {
		return cbilltypecode;
	}

	public void setCbilltypecode(String cbilltypecode) {
		this.cbilltypecode = cbilltypecode;
	}

	public String getCcustomerid() {
		return ccustomerid;
	}

	public void setCcustomerid(String ccustomerid) {
		this.ccustomerid = ccustomerid;
	}

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

	public String getVnote() {
		return vnote;
	}

	public void setVnote(String vnote) {
		this.vnote = vnote;
	}

	public String getVuserdef1() {
		return vuserdef1;
	}

	public void setVuserdef1(String vuserdef1) {
		this.vuserdef1 = vuserdef1;
	}

	@Override
	public String[] getAttributeNames() {
		return new String[]{"ccustomerid","coperatorid","dbilldate","vuserdef2","vnote","cbilltypecode","vuserdef1","xmlname"};//edit by zwx 2017-6-12增加xmlname
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		
		if (attributeName.equals("ccustomerid"))
			   return this.ccustomerid;
		if (attributeName.equals("coperatorid"))
			   return this.coperatorid;
		if (attributeName.equals("dbilldate"))
			   return this.dbilldate;
		if (attributeName.equals("vnote"))
			   return this.vnote;
		if (attributeName.equals("vuserdef2"))
			   return this.vuserdef2;
		if (attributeName.equals("cbilltypecode"))
			   return this.cbilltypecode;
		if (attributeName.equals("vuserdef1"))
			   return this.vuserdef1;
		//add by zwx 2017-6-12 
		if(attributeName.equals("xmlname")){
			   return this.xmlname;
		}
		//end by zwx
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
		      if (name.equals("ccustomerid")) {
		        this.ccustomerid = sTrimedValue;
		      } else if (name.equals("coperatorid")) {
		        this.coperatorid = sTrimedValue;
		      } else if (name.equals("dbilldate")) {
		        this.dbilldate = sTrimedValue;
		      } else if (name.equals("vnote")) {
		        this.vnote = sTrimedValue;
		      } else if (name.equals("vuserdef2")) {
			        this.vuserdef2 = sTrimedValue;
		      } else if (name.equals("cbilltypecode")) {
		        this.cbilltypecode = sTrimedValue;
		      } else if (name.equals("vuserdef1")) {
		        this.vuserdef1 = sTrimedValue;
		      }
		      //add by zwx 2017-6-12
		      else if(name.equals("xmlname")){
		    	  this.xmlname = sTrimedValue;
		      }
		      //end by zwx 
		    }
		    catch (ClassCastException e) {
		      throw new ClassCastException();
		    }
	}

	@Override
	public String getEntityName() {
		return "Cpdlbillhead";
	}

	@Override
	public void validate() throws ValidationException {
		
	}

}
