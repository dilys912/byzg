package nc.vo.scm.merge;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
import java.util.*;

/**
 * 作者：方益 功能：该抽象类完成一些基本的功能: 1)将一组CircularlyAccessibleValueObject分成几组(根据分组策略)
 * 2)将一组CircularlyAccessibleValueObject合并成一个CircularlyAccessibleValueObject对象
 * 3)将一组CircularlyAccessibleValueObject分成几组,然后将每组合并成一个CircularlyAccessibleValueObject对象
 * 4)设置分组,求和,求平均,求加权平均等属性. 创建日期：(2003-02-10 16:09:40)
 * 
 * 2004-07-30.02 yangtao:string. add trim().length() verifying.
 */
public abstract class AbstractVOMerger {
	/**
	 * AbstractVoMerger 构造子注解。
	 */
	public AbstractVOMerger() {
		super();
	}

	public abstract String[] getAveragingAttr();

	public abstract String[] getGroupingAttr();

	/**
	 * 作者：方益 功能：获得分组关键字
	 */
	protected String getGroupKey(CircularlyAccessibleValueObject vo) {
		String[] groupNames = getGroupingAttr();
		String key = "";
		for (int i = 0; groupNames != null && i < groupNames.length; i++) {
			String temp = " ";

			Object ob = vo.getAttributeValue(groupNames[i]);
			if (ob != null) {
				// 0：STRING 字符型
				// 1：INTEGER 整数
				// 2：DECIMAL 小数
				// 3：DATE 日期
				// 4：BOOLEAN 逻辑
				temp = ob.toString();
			}
			key += temp;
		}

		// 将正数量和负数量的单据分成不同的组
		Object temp = null;
		Number num = null;
		if (getNumAttr() != null && vo.getAttributeValue(getNumAttr()) != null) {
			temp = vo.getAttributeValue(getNumAttr());

			if (temp != null && !temp.toString().trim().equals("")) {
				num = (Number) temp;
				if (num.doubleValue() < 0) {
					key += key + "-";
				}

			}
		}

		return key;
	}

	/**
	 * 作者：方益 功能：获得分组关键字
	 */
	protected String getGroupKey1(CircularlyAccessibleValueObject vo) {
		String[] groupNames = getGroupingAttr();
		String key = "";
		for (int i = 0; groupNames != null && i < groupNames.length; i++) {
			String temp = " ";

			Object ob = vo.getAttributeValue(groupNames[i]);
			if (ob != null) {
				// 0：STRING 字符型
				// 1：INTEGER 整数
				// 2：DECIMAL 小数
				// 3：DATE 日期
				// 4：BOOLEAN 逻辑
				temp = ob.toString();
			}
			key += temp;
		}

		return key;
	}

	/**
	 * 
	 * @return
	 */
	public abstract String getNumAttr();

	/**
	 * 作者：方益 功能： 参数： 返回： 例外： 日期：(2003-02-10 16:18:19) 修改日期，修改人，修改原因，注释标志：
	 */
	private String[] getOtherAttr(CircularlyAccessibleValueObject vo) {
		String[] atts = vo.getAttributeNames();
		Vector v = new Vector();
		String[] sumAtt = getSummingAttr();
		String[] avgAtt = getAveragingAttr();
		String[] proavgAtt = getProavgingAttr();

		for (int i = 0; atts != null && i < atts.length; i++) {

			boolean exist = false;
			for (int j = 0; sumAtt != null && j < sumAtt.length; j++) {
				if (atts[i].equals(sumAtt[j])) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				for (int j = 0; avgAtt != null && j < avgAtt.length; j++) {
					if (atts[i].equals(avgAtt[j])) {
						exist = true;
						break;
					}
				}
			}
			if (!exist) {
				for (int j = 0; proavgAtt != null && j < proavgAtt.length; j++) {
					if (atts[i].equals(proavgAtt[j])) {
						exist = true;
						break;
					}
				}
			}

			if (!exist)
				v.add(atts[i]);

		}

		String[] others = null;
		if (v.size() > 0) {
			others = new String[v.size()];
			v.copyInto(others);
		}
		return others;
	}

	public abstract String[] getProavgingAttr();

	public abstract String[] getSummingAttr();

	/**
	 * 作者：方益 功能：将VO按照分组属性值进行分组 参数： 返回： 例外： 日期：(2003-02-10 16:18:19)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected Vector[] group(CircularlyAccessibleValueObject[] vos) {

		Hashtable hash = new Hashtable();
		for (int i = 0; vos != null && i < vos.length; i++) {
			// add by zip:2013/12/16 No 115
			Object blargessflag = vos[i].getAttributeValue("blargessflag");
			if(blargessflag!= null && (Boolean)blargessflag) continue;
			// end
			
			String key = getGroupKey(vos[i]);
			Vector v = (Vector) hash.get(key);
			if (v == null) {
				v = new Vector();
				hash.put(key, v);
			}
			v.add(vos[i]);
		}
		int len = hash.size();
		Vector[] groups = new Vector[len];

		Enumeration en = hash.elements();
		int i = 0;
		while (en.hasMoreElements()) {
			groups[i] = (Vector) en.nextElement();
			i++;
		}
		return groups;
	}
	
	/**
	 * 取消了将正数量和负数量的单据分成不同的组
	 */
	protected Vector[] group1(CircularlyAccessibleValueObject[] vos) {

		Hashtable hash = new Hashtable();
		for (int i = 0; vos != null && i < vos.length; i++) {
			// add by zip:2013/12/16 No 115
			Object blargessflag = vos[i].getAttributeValue("blargessflag");
			if(blargessflag!= null && (Boolean)blargessflag) continue;
			// end
			String key = getGroupKey1(vos[i]);
			Vector v = (Vector) hash.get(key);
			if (v == null) {
				v = new Vector();
				hash.put(key, v);
			}
			v.add(vos[i]);
		}
		int len = hash.size();
		Vector[] groups = new Vector[len];

		Enumeration en = hash.elements();
		int i = 0;
		while (en.hasMoreElements()) {
			groups[i] = (Vector) en.nextElement();
			i++;
		}
		return groups;
	}

	/**
	 * 作者：方益 功能：将一组VO合并成一个VO 参数： 返回： 例外： 日期：(2003-02-10 16:18:19)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected CircularlyAccessibleValueObject merge(
			CircularlyAccessibleValueObject[] vos) {
		return merge(vos, true);
	}

	/**
	 * 作者：方益 功能：将一组VO合并成一个VO 参数： 返回： 例外： 日期：(2003-02-10 16:18:19)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	protected CircularlyAccessibleValueObject merge(
			CircularlyAccessibleValueObject[] vos, boolean isOtherAttr) {

		// 输入参数合法性判断
		if (vos == null || vos.length == 0)
			return null;
		CircularlyAccessibleValueObject vo = null;
		try {
			vo = (CircularlyAccessibleValueObject) vos[0].getClass()
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 求和
		String[] sumAttr = getSummingAttr();
		for (int i = 0; sumAttr != null && i < sumAttr.length; i++) {
			int iValue = 0;
			double dValue = 0;
			int flag = 0;// 0:表示iValue和dValue都没有被加过 1:表示iValue曾经加过
			// 2:表示dValue曾经加过
			for (int j = 0; j < vos.length; j++) {
				Object ob = vos[j].getAttributeValue(sumAttr[i]);
				if (ob != null && ob.getClass().equals(Integer.class)) {
					iValue += ((Integer) ob).intValue();
					flag = 1;
				}
				if (ob != null && ob.getClass().equals(UFDouble.class)) {
					dValue += ((UFDouble) ob).doubleValue();
					flag = 2;
				}
			}
			if (flag == 0)
				vo.setAttributeValue(sumAttr[i], null);
			if (flag == 1)
				vo.setAttributeValue(sumAttr[i], new Integer(iValue));
			if (flag == 2)
				vo.setAttributeValue(sumAttr[i], new UFDouble(dValue));
		}

		// 求平均
		String[] averageAttr = getAveragingAttr();
		for (int i = 0; averageAttr != null && i < averageAttr.length; i++) {
			int iValue = 0;
			double dValue = 0;
			int flag = 0;// 0:表示iValue和dValue都没有被加过 1:表示iValue曾经加过
			// 2:表示dValue曾经加过
			for (int j = 0; j < vos.length; j++) {
				Object ob = vos[j].getAttributeValue(averageAttr[i]);
				if (ob != null && ob.getClass().equals(Integer.class)) {
					iValue += ((Integer) ob).intValue();
					flag = 1;
				}
				if (ob != null && ob.getClass().equals(UFDouble.class)) {
					dValue += ((UFDouble) ob).doubleValue();
					flag = 2;
				}
			}
			if (flag == 0)
				vo.setAttributeValue(averageAttr[i], null);
			if (flag == 1)
				vo.setAttributeValue(averageAttr[i], new Integer((int) iValue
						/ vos.length));
			if (flag == 2)
				vo.setAttributeValue(averageAttr[i], new UFDouble(dValue
						/ vos.length));
		}

		// 求加权平均
		if (getNumAttr() != null) {
			String[] proavgAttr = getProavgingAttr();
			for (int i = 0; proavgAttr != null && i < proavgAttr.length; i++) {

				double dValue = 0;
				double num = 0;
				int flag = 0;// 0:表示iValue和dValue都没有被加过 1:表示iValue曾经加过
				// 2:表示dValue曾经加过
				for (int j = 0; j < vos.length; j++) {
					Object numOb = vos[j].getAttributeValue(getNumAttr());
					if (numOb != null && !numOb.toString().trim().equals("")) {
						double rowNum = ((Number) numOb).doubleValue();

						Object ob = vos[j].getAttributeValue(proavgAttr[i]);
						if (ob != null && ob.getClass().equals(Integer.class)) {
							dValue += Math.abs(((Number) ob).doubleValue()
									* rowNum);
							flag = 1;
						}
						if (ob != null && ob.getClass().equals(UFDouble.class)) {
							dValue += Math.abs(((Number) ob).doubleValue()
									* rowNum);
							flag = 2;
						}
						num += Math.abs(rowNum);
					}
				}
				if (num > 0) {
					if (flag == 0)
						vo.setAttributeValue(proavgAttr[i], null);
					if (flag == 1)
						vo.setAttributeValue(proavgAttr[i], new Integer(
								(int) (dValue / num)));
					if (flag == 2)
						vo.setAttributeValue(proavgAttr[i], new UFDouble(dValue
								/ num));
				}
			}
		}
		String[] otherAttr = getGroupingAttr();
		if (isOtherAttr) {
			// 其他属性设置
			otherAttr = getOtherAttr(vos[0]);
		}

		for (int i = 0; otherAttr != null && i < otherAttr.length; i++) {
			boolean bSame = true;
			Object first = vos[0].getAttributeValue(otherAttr[i]);

			for (int j = 1; j < vos.length; j++) {
				Object next = vos[j].getAttributeValue(otherAttr[i]);

				if (first == null) {
					if (next != null)
						bSame = false;
				} else {
					if (next == null)
						bSame = false;
					else {
						if (!first.equals(next))
							bSame = false;
					}
				}
			}

			if (bSame)
				vo.setAttributeValue(otherAttr[i], first);
			else
				vo.setAttributeValue(otherAttr[i], null);
		}

		return vo;

	}

	/**
	 * 作者：方益 功能：将一组VO,先进行分组,然后进行合并;返回一组合并后的VO 参数： 返回： 例外： 日期：(2003-02-10
	 * 16:18:19) 修改日期，修改人，修改原因，注释标志：
	 */
	public CircularlyAccessibleValueObject[] mergeByGroup(
			CircularlyAccessibleValueObject[] vos) throws BusinessException {
		return mergeByGroup(vos, true);
	}

	/**
	 * 作者：方益 功能：将一组VO,先进行分组,然后进行合并;返回一组合并后的VO 参数： 返回： 例外： 日期：(2003-02-10
	 * 16:18:19) 修改日期，修改人，修改原因，注释标志：
	 */
	protected CircularlyAccessibleValueObject[] mergeByGroup(
			CircularlyAccessibleValueObject[] vos, boolean isOtherAttr)
			throws BusinessException {

		// 输入条件合法性判断
		if (vos == null || vos.length == 0)
			return null;

		// 属性设置合法性检查
		validateAttrs(vos);

		// 如果分组条件为空,直接返回
		if (getGroupingAttr() == null || getGroupingAttr().length == 0)
			return vos;

		// 分组
		Vector[] groups = group(vos);

		// 按照组进行合并
		Vector v = new Vector();
		for (int i = 0; groups != null && i < groups.length; i++) {
			CircularlyAccessibleValueObject[] vos1 = null;
			if (groups[i].size() > 0) {
				vos1 = (CircularlyAccessibleValueObject[]) java.lang.reflect.Array
						.newInstance(groups[i].get(0).getClass(), groups[i]
								.size());
				groups[i].copyInto(vos1);
				v.add(merge(vos1, isOtherAttr));
			}
		}

		// 按照原来的数组类型返回合并后的VO
		Object[] obs = null;
		if (v.size() > 0) {
			obs = (Object[]) java.lang.reflect.Array.newInstance(vos[0]
					.getClass(), v.size());
			v.copyInto(obs);
		}

		return (CircularlyAccessibleValueObject[]) obs;
	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 */
	protected void validateAttrs(CircularlyAccessibleValueObject[] vos)
			throws BusinessException {

		if (vos == null || vos.length == 0)
			return;

		// 如果加权平均属性设置了,则必须设置数量属性
		if (getProavgingAttr() != null && getProavgingAttr().length > 0) {
			if (getNumAttr() == null || getNumAttr().length() == 0)
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("scmpub", "UPPscmpub-001015")/*
																	 * @res
																	 * "既然设置了加权平均属性,就必须设置数量属性"
																	 */);

		}

		// 检查所设置的属性在VO对象中是否存在,如果不存在则抛出异常
		String[] attrs = vos[0].getAttributeNames();
		Hashtable hash = new Hashtable();
		for (int i = 0; attrs != null && i < attrs.length; i++) {
			if (hash.get(attrs[i]) == null)
				hash.put(attrs[i], attrs[i]);
		}
		String[] groups = getGroupingAttr();
		for (int i = 0; groups != null && i < groups.length; i++) {
			if (hash.get(groups[i]) == null)
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("scmpub", "UPPscmpub-001115")/*
																	 * @res
																	 * "VO对象中不存在属性"
																	 */
						+ groups[i]);
		}

		String[] sums = getSummingAttr();
		for (int i = 0; sums != null && i < sums.length; i++) {
			if (hash.get(sums[i]) == null)
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("scmpub", "UPPscmpub-001115")/*
																	 * @res
																	 * "VO对象中不存在属性"
																	 */
						+ sums[i]);
		}

		String[] averages = getAveragingAttr();
		for (int i = 0; averages != null && i < averages.length; i++) {
			if (hash.get(averages[i]) == null)
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("scmpub", "UPPscmpub-001115")/*
																	 * @res
																	 * "VO对象中不存在属性"
																	 */
						+ averages[i]);
		}

		String[] proavgs = getProavgingAttr();
		for (int i = 0; proavgs != null && i < proavgs.length; i++) {
			if (hash.get(proavgs[i]) == null)
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("scmpub", "UPPscmpub-001115")/*
																	 * @res
																	 * "VO对象中不存在属性"
																	 */
						+ proavgs[i]);
		}

		// 检查数量属性的数据类型
		if (getNumAttr() != null && getNumAttr().length() > 0) {
			int i;
			for (i = 0; i < vos.length; i++) {
				Object ob = vos[i].getAttributeValue(getNumAttr());
				if (ob != null
						&& (ob instanceof Integer || ob instanceof UFDouble))
					break;
			}
			if (i >= vos.length)
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("scmpub", "UPPscmpub-001017")/*
																	 * @res
																	 * "所设置的数量属性的数据类型不符,或者数量属性值全部为空!"
																	 */);
		}
	}
}