package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע���������ⵥά��������
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-9-24)
 * @author ƽ̨�ű�����
 */
public class N_TA02_UNAPPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_ZL08_UNAPPROVE ������ע�⡣
 */
public N_TA02_UNAPPROVE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
	 procUnApproveFlow (vo); 
	Object retObj=runClass( "nc.bs.trade.comstatus.BillUnApprove", "unApproveBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
	nc.bs.by.invapp.h0h002.ApproveBill appbill = new nc.bs.by.invapp.h0h002.ApproveBill();
	appbill.deleteInvManAndProduce(vo);
	return retObj;
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n	 procUnApproveFlow (vo); \n	Object retObj=runClassCom@ \"nc.bs.trade.comstatus.BillUnApprove\", \"unApproveBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n	return retObj;\n";}
/*
* ��ע�����ýű�������HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}
