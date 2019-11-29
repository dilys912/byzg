package nc.vo.report;

import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;

public class MaterialCostBVO extends CircularlyAccessibleValueObject {

	private Integer xh;//序号
	private String chzj;//存货主键
	private String chbm;//存货编码
	private String chmc;//存货名称
	private String chdw;//存货单位
	private UFDouble dj;//单价
	private UFDouble bzdh;//标准单耗
	private UFDouble sjyl;//实际用量	
	private UFDouble je;//金额
	

	@Override
	public String[] getAttributeNames() {
		return new String[]{"xh","chmc","chdw","dj","bzdh","sjyl","je","chzj","chbm"};
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		if (attributeName.equals("xh"))
			   return this.xh;
		if (attributeName.equals("chzj"))
			   return this.chzj;
		if (attributeName.equals("chbm"))
			   return this.chbm;
		if (attributeName.equals("chmc"))
			   return this.chmc;
		if (attributeName.equals("chdw"))
			   return this.chdw;
		if (attributeName.equals("dj"))
			   return this.dj;
		if (attributeName.equals("bzdh"))
			   return this.bzdh;
		if (attributeName.equals("sjyl"))
			   return this.sjyl;
		if (attributeName.equals("je"))
			   return this.je;
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
	      if (name.equals("xh")) {
	        this.xh = SwitchObject.switchObjToInteger(value);
	      } else if (name.equals("chmc")) {
	        this.chmc = sTrimedValue;
	      } else if (name.equals("chbm")) {
	        this.chbm = sTrimedValue;
	      } else if (name.equals("chdw")) {
	        this.chdw = sTrimedValue;
	      } else if (name.equals("chzj")) {
	        this.chzj = sTrimedValue;
	      } else if (name.equals("dj")) {
	        this.dj = SwitchObject.switchObjToUFDouble(value);
	      } else if (name.equals("bzdh")) {
	        this.bzdh = SwitchObject.switchObjToUFDouble(value);
	      } else if (name.equals("sjyl")) {
	        this.sjyl = SwitchObject.switchObjToUFDouble(value);
	      } else if (name.equals("je")) {
	        this.je = SwitchObject.switchObjToUFDouble(value);
	      }
	    }
	    catch (ClassCastException e) {
	      throw new ClassCastException();
	    }
	}

	public String getChbm() {
		return chbm;
	}

	public void setChbm(String chbm) {
		this.chbm = chbm;
	}

	@Override
	public String getEntityName() {
		return null;
	}

	@Override
	public void validate() throws ValidationException {
	}

	
	public String getChzj() {
		return chzj;
	}

	public void setChzj(String chzj) {
		this.chzj = chzj;
	}

	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

	public String getChmc() {
		return chmc;
	}

	public void setChmc(String chmc) {
		this.chmc = chmc;
	}

	public String getChdw() {
		return chdw;
	}

	public void setChdw(String chdw) {
		this.chdw = chdw;
	}

	public UFDouble getDj() {
		return dj;
	}

	public void setDj(UFDouble dj) {
		this.dj = dj;
	}

	public UFDouble getBzdh() {
		return bzdh;
	}

	public void setBzdh(UFDouble bzdh) {
		this.bzdh = bzdh;
	}

	public UFDouble getSjyl() {
		return sjyl;
	}

	public void setSjyl(UFDouble sjyl) {
		this.sjyl = sjyl;
	}

	public UFDouble getJe() {
		return je;
	}

	public void setJe(UFDouble je) {
		this.je = je;
	}


}
