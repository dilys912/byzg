package nc.ui.mo.mo6600;

import java.util.Hashtable;

/**
 * 
 * 抽象的业务代理类
 * 
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends
		nc.ui.trade.bsdelegate.BusinessDelegator
{

	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception
	{

		Hashtable dataHashTable = new Hashtable();

		nc.vo.mo.mo6600.GlItemBVO[] bodyVOs1 = (nc.vo.mo.mo6600.GlItemBVO[]) queryByCondition(
				nc.vo.mo.mo6600.GlItemBVO.class, getBodyCondition(
						nc.vo.mo.mo6600.GlItemBVO.class, key));
		if (bodyVOs1 != null && bodyVOs1.length > 0)
		{

			dataHashTable.put("mm_glzb_b", bodyVOs1);
		}

		return dataHashTable;

	}

	/**
	 * 
	 * 该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key)
	{

		if (bodyClass == nc.vo.mo.mo6600.GlItemBVO.class)
			return "pk_xjgl = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
