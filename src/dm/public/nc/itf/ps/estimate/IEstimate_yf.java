/*
 * 创建日期 2005-12-20
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.itf.ps.estimate;
 
import java.util.ArrayList;

import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ps.estimate.EstimateVO;
import nc.vo.ps.estimate.GeneralHVO;
import nc.vo.ps.settle.OorderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pub.session.ClientLink;

/**
 * @author xhq
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 * 
 * 
 * 运费暂估      zhwj 2012-08-09
 */
public interface IEstimate_yf {

	/**
	 * 功能描述:运费暂估      zhwj 2012-08-09
	 * 输入参数:VO[],当前操作员ID,当前日期
	 * 创建：熊海情
	 * 修改：晁志平 FOR  V30
	 */
	public abstract void estimate_yfn(EstimateVO VOs[], ArrayList paraList,ClientLink cl)
			throws BusinessException;


	/**zhwj
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 *
	 * 创建日期：(2001-5-30) 
	 * @return nc.vo.ps.estimate.EstimateVO[] 查到的VO对象数组
	 * @param unitCode int
	 * @exception java.rmi.RemoteException 异常说明。
	 */
	public abstract EstimateVO[] queryEstimate_yfn(String unitCode,
			ConditionVO conditionVO[], String sZG, String sEstPriceSource)
			throws BusinessException;

	/**zhwj
	 * 功能描述:取消暂估
	 * 输入参数:EstimateVO[],当前操作员ID
	 * 作者：熊海情
	 * 创建：2001-5-24 14:41:50
	 * 修改：晁志平　FOR　V30
	 */
	public abstract void antiEstimate_yfn(EstimateVO VOs[], String cOperator)
			throws BusinessException;

	
}