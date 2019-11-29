package nc.vo.ic.pub;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class AggKy extends AggregatedValueObject {

	private KyjkVO header = null;
	private KyjkBVO[] items = null;
	
	public KyjkBVO[] getChildrenVO()
	  {
	    return this.items;
	  }

	  public KyjkVO getParentVO()
	  {
	    return this.header;
	  }

	  public void setChildrenVO(CircularlyAccessibleValueObject[] children)
	  {
	    this.items = ((KyjkBVO[])children);
	  }

	  public void setParentVO(CircularlyAccessibleValueObject parent)
	  {
	    this.header = ((KyjkVO)parent);
	  }

}
