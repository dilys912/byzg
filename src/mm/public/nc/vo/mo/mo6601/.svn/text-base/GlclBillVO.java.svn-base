package nc.vo.mo.mo6601;

import java.util.Arrays;

import nc.vo.trade.pub.HYBillVO;

public class GlclBillVO extends HYBillVO 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 357736308302204912L;

	public GlclItemVO[] getChildrenVO() {
		return (GlclItemVO[]) super.getChildrenVO();
	}

	public GlclHeadVO getParentVO() {
		return (GlclHeadVO) super.getParentVO();
	}

	public void setChildrenVO(GlclItemVO[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{ 
			super.setChildrenVO(Arrays.asList(children).toArray(new GlclItemVO[0]));
		}
	}

	public void setParentVO(GlclHeadVO parent) {
		super.setParentVO(parent);
	}
}
