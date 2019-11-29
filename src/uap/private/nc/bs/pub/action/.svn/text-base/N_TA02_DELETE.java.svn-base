package nc.bs.pub.action;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.compiler.PfParameterVO;
import java.util.*;
import nc.vo.pub.BusinessException;
/**
 * 创建日期：(2003-10-20)
 * @author：平台脚本生成
 */
public class N_TA02_DELETE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_HF_DELETE 构造子注解。
 */
public N_TA02_DELETE() {
	super();
}
/*
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n	Object retObj  =null;\n	//####重要说明：生成的业务组件方法尽量不要进行修改####\n	//方法说明:null\n	retObj  =runClassCom@ \"nc.bs.fa.bill07.ReduceBO\", \"delete\", \"nc.vo.fa.bill07.ReduceVO:HF\"@;\n	//##################################################\n	return retObj;\n";}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
	Object retObj  =null;
	//####重要说明：生成的业务组件方法尽量不要进行修改####
	//方法说明:null
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
* 备注：设置脚本变量的HAS
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
