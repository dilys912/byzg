package nc.vo.ic.pub;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class AggLPWS extends AggregatedValueObject {

	private LPWSJKHVO header = null;
	private LPWSJKBVO[] items = null;
	
	public LPWSJKBVO[] getChildrenVO()
	  {
	    return this.items;
	  }

	  public LPWSJKHVO getParentVO()
	  {
	    return this.header;
	  }

	  public void setChildrenVO(CircularlyAccessibleValueObject[] children)
	  {
	    this.items = ((LPWSJKBVO[])children);
	  }

	  public void setParentVO(CircularlyAccessibleValueObject parent)
	  {
	    this.header = ((LPWSJKHVO)parent);
	  }

}
