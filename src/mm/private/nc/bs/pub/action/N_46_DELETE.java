package nc.bs.pub.action;

import nc.vo.pub.pf.PfUtilActionVO;
import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import java.math.*;
import java.util.*;
import nc.vo.pub.lang.*;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע��������Ʒ��ⵥ��ɾ��
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2012-11-2)
 * @author ƽ̨�ű�����
 */
public class N_46_DELETE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_46_DELETE ������ע�⡣
 */
public N_46_DELETE() {
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
	//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********
	Object inCurObject  =getVos ();
	Object retObj  =null;
	StringBuffer sErr  =new StringBuffer ();
	//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�
	if (! (inCurObject instanceof nc.vo.ic.pub.bill.GeneralBillVO[])) throw new BusinessException ( "Remote Call",new nc.vo.pub.BusinessException ( "������ϣ������Ŀ�����Ʒ������Ͳ�ƥ��"));
	if (inCurObject  == null) throw new BusinessException ( "Remote Call",new nc.vo.pub.BusinessException ( "������ϣ������Ŀ�����Ʒ���û������"));
	//2,���ݺϷ���������ת��Ϊ������Ʒ��⡣
	nc.vo.ic.pub.bill.GeneralBillVO inCurVO  =null;
	nc.vo.ic.pub.bill.GeneralBillVO[] inCurVOs  = (nc.vo.ic.pub.bill.GeneralBillVO[])inCurObject;
	inCurObject  =null;
	for (int i=0;i<inCurVOs.length;i  ++) {
		inCurVO  =inCurVOs[i];
		if (inCurVO!=null&&inCurVO.getHeaderVO ()!=null)
		inCurVO.getHeaderVO ().setStatus (nc.vo.pub.VOStatus.DELETED);
		//��ȡƽ̨����Ĳ���
		setParameter ( "INCURVO",inCurVO);
		Object alLockedPK  =null;
		try {

			//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
			//����˵��:����Ƿ���ʡ�<������>
			//Ŀǰ�Ǹ��ݵ��ݱ���ҵ�����ڼ�顣������ݵ�¼���ڼ�飬�뽫checkAccountStatus��ΪcheckAccountStatus1
			runClass( "nc.bs.ic.ic2a3.AccountctrlDMO", "checkAccountStatus", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
			//#############################################################
			//����˵��:������ⵥ�ݼ�ҵ����
			alLockedPK  =runClass( "nc.bs.ic.pub.bill.ICLockBO", "lockBill", "&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			//##################################################
			
			//����˵��:����ɾ����д���ߵ���
			runClass( "nc.impl.mm.mm6600.IMo6600Impl", "Writeback", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
			//�÷���<��������>
			//����˵��:����浥��ʱ���
			runClass( "nc.bs.ic.pub.check.CheckDMO", "checkTimeStamp", "&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			//����˵��:����������Ƿ�������
			runClass( "nc.bs.ic.pub.check.CheckBusiDMO", "checkRelativeRespondBill", "&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
			if (inCurVO.isHaveSourceBill ()) {
				//����˵��:��浥��ɾ��ʱ�޸Ŀ�����
				runClass( "nc.bs.ic.pub.bill.ICATP", "modifyATPWhenDeleteBill", "&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			}
			//##################################################
			//�����ݵ��ݸ������Ϳ�浥λ֮���ת�������û������ҵ����ʵʩ��Աע�͵���
			runClass( "nc.bs.ic.pub.bill.DesassemblyBO", "setMeasRateVO", "&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			//����˵��:����ɾ��
			runClass( "nc.bs.ic.ic202.GeneralHBO", "deleteBill", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
			//������䣬���û������ҵ����ʵʩ��Աע�͵���
			runClass( "nc.bs.ic.pub.bill.DesassemblyBO", "exeDesassembly", "&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			//����˵������д�ۼƳ�������
			setParameter ( "CURVO",null);
			setParameter ( "PREVO",inCurVO);
			runClass( "nc.bs.ic.pub.RewriteDMO", "reWriteCorNum", "&CURVO:nc.vo.ic.pub.bill.GeneralBillVO,&PREVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
			//�÷���<��������>
			//����˵��:�����������������һ��,���θ����,��������
			runClass( "nc.bs.ic.pub.check.CheckDMO", "checkDBL_New", "&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			//�÷���<��������>
			//����˵��:����������Ƿ������⸺���
			runClass( "nc.bs.ic.pub.check.CheckDMO", "checkInOutTrace", "&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			//�÷���<������>
			//����˵��:����λ�����Ƿ񳬳�
			//runClass("nc.bs.ic.pub.check.CheckDMO","checkCargoVolumeOut","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
			//�÷���<������>
			//����˵��:�����߿�桢��Ϳ�桢��ȫ��桢�ٶ�����
			Object s1  =runClass( "nc.bs.ic.pub.check.CheckDMO", "checkParam_new", "&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			if (s1!=null)
			sErr.append ( (String)s1);
			//����˵��:����Ʒ��ⵥ��д����������������
			setParameter ( "RWINPREVO",null);
			//ע�����˳����Ϊ��ɾ�����������Խ���ǰ��VO��Ϊoldvo����
			runClass( "nc.bs.ic.pub.RewriteDMO", "reWriteMMWRPI", "&RWINPREVO:nc.vo.ic.pub.bill.GeneralBillVO,&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
			//##################################################
			//�÷���<��������>
			//����˵��:����ɾ��ʱ�˻ص��ݺ�
			nc.vo.scm.pub.IBillCode ibc  = (nc.vo.scm.pub.IBillCode)inCurVO;
			setParameter ( "IBC",ibc);
			runClass( "nc.bs.ic.pub.check.CheckDMO", "returnBillCodeWhenDelete", "&IBC:nc.vo.scm.pub.IBillCode",vo,m_keyHas,m_methodReturnHas);
		}
		catch (Exception e) {
			//############################
			//����ҵ����־���÷�����������
			setParameter ( "EXC",e.getMessage ());
			setParameter ( "FUN", "ɾ��");
			runClass( "nc.bs.ic.pub.check.CheckBO", "insertBusinessExceptionlog", "&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
			//###########################
			if (e instanceof BusinessException)
			throw (BusinessException) e;
			else
			throw new BusinessException ( "Remote Call", e);
		}
		finally {
			//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
			//����˵��:������ⵥ�ݽ�ҵ����
			setParameter ( "ALLPK", (ArrayList)alLockedPK);
			if (alLockedPK!=null)
			runClass( "nc.bs.ic.pub.bill.ICLockBO", "unlockBill", "&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList",vo,m_keyHas,m_methodReturnHas);
			//##################################################
		}
	}
	//����ҵ����־���÷�����������
	setParameter ( "INCURVOS",inCurVOs);
	setParameter ( "ERR",sErr.toString ());
	setParameter ( "FUN", "ɾ��");
	runClass( "nc.bs.ic.pub.check.CheckDMO", "insertBusinesslog", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
	//############################
	if (inCurVO.isHaveSourceBill ()) {
		//����˵��:��������ʱ���,������
		setParameter ( "INPREVOATP",null);
		runClass( "nc.bs.ic.pub.bill.ICATP", "checkAtpInstantly", "&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVOATP:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	}
	inCurVO  =null;
	ArrayList alRet  =new ArrayList ();
	if (sErr.toString ().trim ().length ()  ==0)
	alRet.add (null);
	else
	alRet.add (sErr.toString ());
	//alRet.add (retObj);
	return retObj;
	//************************************************************************
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
	return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n	//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********\n	Object inCurObject  =getVos ();\n	Object retObj  =null;\n	StringBuffer sErr  =new StringBuffer ();\n	//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�\n	if (! (inCurObject instanceof nc.vo.ic.pub.bill.GeneralBillVO[])) throw new BusinessException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ������Ŀ�����Ʒ������Ͳ�ƥ��\"));\n	if (inCurObject  == null) throw new BusinessException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"������ϣ������Ŀ�����Ʒ���û������\"));\n	//2,���ݺϷ���������ת��Ϊ������Ʒ��⡣\n	nc.vo.ic.pub.bill.GeneralBillVO inCurVO  =null;\n	nc.vo.ic.pub.bill.GeneralBillVO[] inCurVOs  = (nc.vo.ic.pub.bill.GeneralBillVO[])inCurObject;\n	inCurObject  =null;\n	for (int i=0;i<inCurVOs.length;i  ++) {\n		inCurVO  =inCurVOs[i];\n		if (inCurVO!  =null&&inCurVO.getHeaderVO ()!  =null)\n		inCurVO.getHeaderVO ().setStatus (nc.vo.pub.VOStatus.DELETED);\n		//��ȡƽ̨����Ĳ���\n		setParameter ( \"INCURVO\",inCurVO);\n		Object alLockedPK  =null;\n		try {\n			//����˵��:����ɾ����д���ߵ���\n			runClassCom@ \"nc.impl.mm.mm6600.IMo6600Impl\", \"Writeback\", \"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n			//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n			//����˵��:����Ƿ���ʡ�<������>\n			//Ŀǰ�Ǹ��ݵ��ݱ���ҵ�����ڼ�顣������ݵ�¼���ڼ�飬�뽫checkAccountStatus��ΪcheckAccountStatus1\n			runClassCom@ \"nc.bs.ic.ic2a3.AccountctrlDMO\", \"checkAccountStatus\", \"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n			//#############################################################\n			//����˵��:������ⵥ�ݼ�ҵ����\n			alLockedPK  =runClassCom@ \"nc.bs.ic.pub.bill.ICLockBO\", \"lockBill\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n			//##################################################\n			//�÷���<��������>\n			//����˵��:����浥��ʱ���\n			runClassCom@ \"nc.bs.ic.pub.check.CheckDMO\", \"checkTimeStamp\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n			//����˵��:����������Ƿ�������\n			runClassCom@ \"nc.bs.ic.pub.check.CheckBusiDMO\", \"checkRelativeRespondBill\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n			//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n			if (inCurVO.isHaveSourceBill ()) {\n				//����˵��:��浥��ɾ��ʱ�޸Ŀ�����\n				runClassCom@ \"nc.bs.ic.pub.bill.ICATP\", \"modifyATPWhenDeleteBill\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n			}\n			//##################################################\n			//�����ݵ��ݸ������Ϳ�浥λ֮���ת�������û������ҵ����ʵʩ��Աע�͵���\n			runClassCom@ \"nc.bs.ic.pub.bill.DesassemblyBO\", \"setMeasRateVO\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n			//����˵��:����ɾ��\n			runClassCom@ \"nc.bs.ic.ic202.GeneralHBO\", \"deleteBill\", \"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n			//������䣬���û������ҵ����ʵʩ��Աע�͵���\n			runClassCom@ \"nc.bs.ic.pub.bill.DesassemblyBO\", \"exeDesassembly\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n			//����˵������д�ۼƳ�������\n			setParameter ( \"CURVO\",null);\n			setParameter ( \"PREVO\",inCurVO);\n			runClassCom@ \"nc.bs.ic.pub.RewriteDMO\", \"reWriteCorNum\", \"&CURVO:nc.vo.ic.pub.bill.GeneralBillVO,&PREVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n			//�÷���<��������>\n			//����˵��:�����������������һ��,���θ����,��������\n			runClassCom@ \"nc.bs.ic.pub.check.CheckDMO\", \"checkDBL_New\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n			//�÷���<��������>\n			//����˵��:����������Ƿ������⸺���\n			runClassCom@ \"nc.bs.ic.pub.check.CheckDMO\", \"checkInOutTrace\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n			//�÷���<������>\n			//����˵��:����λ�����Ƿ񳬳�\n			//runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkCargoVolumeOut\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n			//�÷���<������>\n			//����˵��:�����߿�桢��Ϳ�桢��ȫ��桢�ٶ�����\n			Object s1  =runClassCom@ \"nc.bs.ic.pub.check.CheckDMO\", \"checkParam_new\", \"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n			if (s1!  =null)\n			sErr.append ( (String)s1);\n			//����˵��:����Ʒ��ⵥ��д����������������\n			setParameter ( \"RWINPREVO\",null);\n			//ע�����˳����Ϊ��ɾ�����������Խ���ǰ��VO��Ϊoldvo����\n			runClassCom@ \"nc.bs.ic.pub.RewriteDMO\", \"reWriteMMWRPI\", \"&RWINPREVO:nc.vo.ic.pub.bill.GeneralBillVO,&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n			//##################################################\n			//�÷���<��������>\n			//����˵��:����ɾ��ʱ�˻ص��ݺ�\n			nc.vo.scm.pub.IBillCode ibc  = (nc.vo.scm.pub.IBillCode)inCurVO;\n			setParameter ( \"IBC\",ibc);\n			runClassCom@ \"nc.bs.ic.pub.check.CheckDMO\", \"returnBillCodeWhenDelete\", \"&IBC:nc.vo.scm.pub.IBillCode\"@;\n		}\n		catch (Exception e) {\n			//############################\n			//����ҵ����־���÷�����������\n			setParameter ( \"EXC\",e.getMessage ());\n			setParameter ( \"FUN\", \"ɾ��\");\n			runClassCom@ \"nc.bs.ic.pub.check.CheckBO\", \"insertBusinessExceptionlog\", \"&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String\"@;\n			//###########################\n			if (e instanceof BusinessException)\n			throw (BusinessException) e;\n			else\n			throw new BusinessException ( \"Remote Call\", e);\n		}\n		finally {\n			//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n			//����˵��:������ⵥ�ݽ�ҵ����\n			setParameter ( \"ALLPK\", (ArrayList)alLockedPK);\n			if (alLockedPK!  =null)\n			runClassCom@ \"nc.bs.ic.pub.bill.ICLockBO\", \"unlockBill\", \"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList\"@;\n			//##################################################\n		}\n	}\n	//����ҵ����־���÷�����������\n	setParameter ( \"INCURVOS\",inCurVOs);\n	setParameter ( \"ERR\",sErr.toString ());\n	setParameter ( \"FUN\", \"ɾ��\");\n	runClassCom@ \"nc.bs.ic.pub.check.CheckDMO\", \"insertBusinesslog\", \"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String\"@;\n	//############################\n	if (inCurVO.isHaveSourceBill ()) {\n		//����˵��:��������ʱ���,������\n		setParameter ( \"INPREVOATP\",null);\n		runClassCom@ \"nc.bs.ic.pub.bill.ICATP\", \"checkAtpInstantly\", \"&INCURVO:nc.vo.pub.AggregatedValueObject,&INPREVOATP:nc.vo.pub.AggregatedValueObject\"@;\n	}\n	inCurVO  =null;\n	ArrayList alRet  =new ArrayList ();\n	if (sErr.toString ().trim ().length ()  ==0)\n	alRet.add (null);\n	else\n	alRet.add (sErr.toString ());\n	//alRet.add (retObj);\n	return retObj;\n	//************************************************************************\n";}
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
