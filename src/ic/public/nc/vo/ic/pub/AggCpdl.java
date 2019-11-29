package nc.vo.ic.pub;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class AggCpdl extends AggregatedValueObject {

	private CpdljkVO header = null;
	private CpdljkBVO[] items = null;
	
	public CircularlyAccessibleValueObject[] getChildrenVO()
	  {
	    return this.items;
	  }

	  public CircularlyAccessibleValueObject getParentVO()
	  {
	    return this.header;
	  }

	  public void setChildrenVO(CircularlyAccessibleValueObject[] children)
	  {
	    this.items = ((CpdljkBVO[])children);
	  }

	  public void setParentVO(CircularlyAccessibleValueObject parent)
	  {
	    this.header = ((CpdljkVO)parent);
	  }

}
