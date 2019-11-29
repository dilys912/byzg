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
import nc.vo.ps.estimate.GeneralHVO;
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
public interface IEstimate_yf {

	/**
	 * ��������:�˷��ݹ�      zhwj 2012-08-09
	 * �������:VO[],��ǰ����ԱID,��ǰ����
	 * �������ܺ���
	 * �޸ģ���־ƽ FOR  V30
	 */
	public abstract void estimate_yfn(EstimateVO VOs[], ArrayList paraList,ClientLink cl)
			throws BusinessException;


	/**zhwj
	 * ͨ����λ���뷵��ָ����˾���м�¼VO���顣�����λ����Ϊ�շ������м�¼��
	 *
	 * �������ڣ�(2001-5-30) 
	 * @return nc.vo.ps.estimate.EstimateVO[] �鵽��VO��������
	 * @param unitCode int
	 * @exception java.rmi.RemoteException �쳣˵����
	 */
	public abstract EstimateVO[] queryEstimate_yfn(String unitCode,
			ConditionVO conditionVO[], String sZG, String sEstPriceSource)
			throws BusinessException;

	/**zhwj
	 * ��������:ȡ���ݹ�
	 * �������:EstimateVO[],��ǰ����ԱID
	 * ���ߣ��ܺ���
	 * ������2001-5-24 14:41:50
	 * �޸ģ���־ƽ��FOR��V30
	 */
	public abstract void antiEstimate_yfn(EstimateVO VOs[], String cOperator)
			throws BusinessException;

	
}