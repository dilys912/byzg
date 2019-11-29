package nc.vo.by.invapp.h0h002;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;

@SuppressWarnings("serial")
public class MyExAggVO extends HYBillVO implements IExAggVO {
	@SuppressWarnings("unchecked")
	private HashMap hmchidvos = new HashMap();

	public MyExAggVO() {
		super();
	}

	@SuppressWarnings("unchecked")
	public CircularlyAccessibleValueObject[] getAllChildrenVO() {
		ArrayList al = new ArrayList();
		for (int i = 0; i < getTableCodes().length; i++) {
			CircularlyAccessibleValueObject[] cvos = getTableVO(getTableCodes()[i]);
			if (cvos != null && cvos.length > 0)
				al.addAll(Arrays.asList(cvos));
		}
		return (nc.vo.pub.SuperVO[]) al.toArray(new nc.vo.pub.SuperVO[0]);
	}

	public SuperVO[] getChildVOsByParentId(String tableCode, String parentid) {
		return null;
	}

	public String getDefaultTableCode() {
		return getTableCodes()[0];
	}

	@SuppressWarnings("unchecked")
	public HashMap getHmEditingVOs() throws Exception {
		return null;
	}

	public String getParentId(SuperVO item) {
		return null;
	}

	public String[] getTableCodes() {
		return new String[] { "bd_convert_app"};
	}

	public String[] getTableNames() {
		return new String[] { "¼ÆÁ¿µµ°¸"};
	}

	public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {
		Object o = hmchidvos.get(tableCode);
		if (o != null)
			return (nc.vo.pub.CircularlyAccessibleValueObject[]) o;
		return null;
	}

	public void setParentId(SuperVO item, String id) {
	}

	@SuppressWarnings("unchecked")
	public void setTableVO(String tableCode,
			CircularlyAccessibleValueObject[] values) {
		if (values == null || values.length == 0)
			hmchidvos.remove(tableCode);
		else
			hmchidvos.put(tableCode, values);
	}
}