/*
 * �������� 2005-12-20
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 * 
 * 
 * �˷��ݹ�      zhwj 2012-08-09
 */
public interface IEstimate_wwyf {

	
	/**������
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 *
	 * �������ڣ�(2014-11-03) 
	 * @return nc.vo.ps.estimate.EstimateVO[] �鵽��VO��������
	 * @param unitCode int
	 * @exception java.rmi.RemoteException �쳣˵����
	 */
	public abstract wwEstimateVO[] queryEstimate_wwyf(String unitCode,
			ConditionVO conditionVO[], String sZG, String sEstPriceSource)
			throws BusinessException;
	
	
	/**
	 * ��������:�˷��ݹ�   
	 * �������:VO[],��ǰ����ԱID,��ǰ����
	 * ������������
	 */
	public abstract void estimate_wwyf(wwEstimateVO[] vOs, ArrayList paraList,ClientLink cl)
			throws BusinessException;
	
	/**������
	 * ��������:ȡ���ݹ�
	 * �������:EstimateVO[],��ǰ����ԱID
	 * ���ߣ�
	 */
	public abstract void antiEstimate_wwyf(wwEstimateVO[] vOs, String cOperator)
			throws BusinessException;
}