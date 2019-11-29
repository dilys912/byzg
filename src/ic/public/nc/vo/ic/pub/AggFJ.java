package nc.vo.ic.pub;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class AggFJ extends AggregatedValueObject {

	private FJJKHVO header = null;
	private FJJKBVO[] items = null;
	
	public FJJKBVO[] getChildrenVO()
	  {
	    return this.items;
	  }

	  public FJJKHVO getParentVO()
	  {
	    return this.header;
	  }

	  public void setChildrenVO(CircularlyAccessibleValueObject[] children)
	  {
	    this.items = ((FJJKBVO[])children);
	  }

	  public void setParentVO(CircularlyAccessibleValueObject parent)
	  {
	    this.header = ((FJJKHVO)parent);
	  }

}
