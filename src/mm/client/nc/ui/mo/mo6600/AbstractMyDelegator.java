package nc.ui.mo.mo6600;

import java.util.Hashtable;

/**
 * 
 * �����ҵ�������
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
	 * �÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
	 * 
	 */
	public String getBodyCondition(Class bodyClass, String key)
	{

		if (bodyClass == nc.vo.mo.mo6600.GlItemBVO.class)
			return "pk_xjgl = '" + key + "' and isnull(dr,0)=0 ";

		return null;
	}

}
