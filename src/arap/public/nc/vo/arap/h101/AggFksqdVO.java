package nc.vo.arap.h101;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class AggFksqdVO extends AggregatedValueObject {
	private FksqdVO header = null;
	private FksqdBVO[] items = null;
	@Override
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
	    this.items = ((FksqdBVO[])(FksqdBVO[])children);
	  }

	  public void setParentVO(CircularlyAccessibleValueObject parent)
	  {
	    this.header = ((FksqdVO)parent);
	  }

}
