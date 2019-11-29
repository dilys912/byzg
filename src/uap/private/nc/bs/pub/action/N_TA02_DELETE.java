package nc.bs.pub.action;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.compiler.PfParameterVO;
import java.util.*;
import nc.vo.pub.BusinessException;
/**
 * �������ڣ�(2003-10-20)
 * @author��ƽ̨�ű�����
 */
public class N_TA02_DELETE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_HF_DELETE ������ע�⡣
 */
public N_TA02_DELETE() {
	super();
}
/*
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n	Object retObj  =null;\n	//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n	//����˵��:null\n	retObj  =runClassCom@ \"nc.bs.fa.bill07.ReduceBO\", \"delete\", \"nc.vo.fa.bill07.ReduceVO:HF\"@;\n	//##################################################\n	return retObj;\n";}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
	Object retObj  =null;
	//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
	//����˵��:null
	retObj = runClass("nc.bs.trade.business.HYPubBO","deleteBill","nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);

	if (retObj != null) {
		m_methodReturnHas.put( "delete",retObj);
	}
	//##################################################
	return retObj;
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
		throw new BusinessException("Remote Call", ex);
}
}
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
