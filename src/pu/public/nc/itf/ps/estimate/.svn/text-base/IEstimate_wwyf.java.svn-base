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
import nc.vo.ps.estimate.saleEstimateVO;
import nc.vo.ps.estimate.GeneralHVO;
import nc.vo.ps.estimate.wwEstimateVO;
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
public interface IEstimate_wwyf {

	
	/**王凯飞
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 *
	 * 创建日期：(2014-11-03) 
	 * @return nc.vo.ps.estimate.EstimateVO[] 查到的VO对象数组
	 * @param unitCode int
	 * @exception java.rmi.RemoteException 异常说明。
	 */
	public abstract wwEstimateVO[] queryEstimate_wwyf(String unitCode,
			ConditionVO conditionVO[], String sZG, String sEstPriceSource)
			throws BusinessException;
	
	
	/**
	 * 功能描述:运费暂估   
	 * 输入参数:VO[],当前操作员ID,当前日期
	 * 创建：王凯飞
	 */
	public abstract void estimate_wwyf(wwEstimateVO[] vOs, ArrayList paraList,ClientLink cl)
			throws BusinessException;
	
	/**王凯飞
	 * 功能描述:取消暂估
	 * 输入参数:EstimateVO[],当前操作员ID
	 * 作者：
	 */
	public abstract void antiEstimate_wwyf(wwEstimateVO[] vOs, String cOperator)
			throws BusinessException;
}