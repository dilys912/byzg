package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.ic.pub.ILockIC;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.uap.pf.PFBusinessException;

public class N_46_SIGN extends AbstractCompiler2
{
  private Hashtable m_methodReturnHas = new Hashtable();
  private Hashtable m_keyHas = null;

  public Object runComClass(PfParameterVO vo)
    throws BusinessException
  {
    try
    {
      this.m_tmpVo = vo;

      StringBuffer sErr = new StringBuffer();
      AggregatedValueObject[] inCurObjects = getVos();
      Object retObj = null;
      if ((inCurObjects == null) || (inCurObjects.length == 0)) throw new BusinessException("Remote Call", new BusinessException("���󣺵���û������"));

      if (!(inCurObjects[0] instanceof GeneralBillVO)) throw new BusinessException("Remote Call", new BusinessException("���󣺵������Ͳ�ƥ��"));

      GeneralBillVO[] inCurVOs = null;
      inCurVOs = (GeneralBillVO[])(GeneralBillVO[])inCurObjects;
      inCurObjects = null;

      setParameter("INCURVOS", inCurVOs);
      ArrayList alLockedPK = null;
      setParameter("ILOCKS", (ILockIC[])inCurVOs);
      try
      {
        alLockedPK = (ArrayList)runClass("nc.bs.ic.pub.bill.ICLockBO", "lockICBills", "&INCURVOS:nc.vo.pub.AggregatedValueObject[]", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.ic2a3.AccountctrlDMO", "checkAccountStatus", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkTimeStamps", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.ic.ic202.GeneralHBO", "setOOSNum", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);

        SCMEnv.out("=====================================================");
        SCMEnv.out("=====================================================");
        SCMEnv.out("=====================================================");

        String sBarcodeCheckErr = (String)runClass("nc.bs.ic.pub.check.CheckBarcodeDMO", "checkBarcodeAbsent", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);
        if (sBarcodeCheckErr != null) {
          sErr.append(sBarcodeCheckErr);
        }

        retObj = runClass("nc.bs.ic.ic202.GeneralHBO", "signBills", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]", vo, this.m_keyHas, this.m_methodReturnHas);

        setParameter("ERR", sErr.toString());
        runClass("nc.bs.ic.pub.check.CheckDMO", "insertBusinesslog", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String", vo, this.m_keyHas, this.m_methodReturnHas);

        if ((retObj != null) && (!(retObj instanceof ArrayList))) throw new BusinessException("Remote Call", new BusinessException("����ǩ�ֶ����ķ���ֵ���ʹ���"));

        setParameter("icbilltype", "46");
        setParameter("iabilltype", "I3");
        setParameter("PFPARAVO", vo);
        runClass("nc.bs.ic.pub.ictoia.Ic2IaDMO", "saveIABills", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&icbilltype:String,&iabilltype:String,&PFPARAVO:nc.vo.pub.compiler.PfParameterVO", vo, this.m_keyHas, this.m_methodReturnHas);
      }
      catch (Exception e)
      {
        setParameter("EXC", e.getMessage());
        setParameter("FUN", "ǩ�� ");
        runClass("nc.bs.ic.pub.check.CheckBO", "insertBusinessExceptionlog", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&EXC:String,&FUN:String", vo, this.m_keyHas, this.m_methodReturnHas);

        for (int i = 0; i < inCurVOs.length; i++) {
          setParameter("IBC", inCurVOs[i]);
          if (inCurVOs[i].getHeaderVO().getStatus() == 2)
            runClass("nc.bs.ic.pub.check.CheckDMO", "returnBillCode", "&IBC:nc.vo.scm.pub.IBillCode", vo, this.m_keyHas, this.m_methodReturnHas);
        }
        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException("Remote Call", e);
      }
      finally
      {
        if (alLockedPK != null) {
          setParameter("ALPK", alLockedPK);
          runClass("nc.bs.ic.pub.bill.ICLockBO", "unlockBills", "&INCURVOS:nc.vo.pub.AggregatedValueObject[],&ALPK:ArrayList", vo, this.m_keyHas, this.m_methodReturnHas);
        }
      }
      setParameter("ERR", sErr.toString());
      setParameter("FUN", "ǩ��");
      runClass("nc.bs.ic.pub.check.CheckDMO", "insertBusinesslog", "&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String", vo, this.m_keyHas, this.m_methodReturnHas);

      ArrayList alRet = new ArrayList();
      if (sErr.toString().trim().length() == 0)
        alRet.add(null);
      else
        alRet.add(sErr.toString());
      alRet.add(retObj);

      SMGeneralBillVO smbillvo = inCurVOs[0].getSmallBillVO();
      alRet.add(smbillvo);
      inCurVOs = null;
      return new Object[] { alRet };
    }
    catch (Exception ex) {
      if ((ex instanceof BusinessException))
        throw ((BusinessException)ex);
      throw new PFBusinessException(ex.getMessage(), ex);
    }
    
  }

  public String getCodeRemark()
  {
    return "\t//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n\t//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********\n//############################\nStringBuffer sErr=new StringBuffer();\nnc.vo.pub.AggregatedValueObject[] inCurObjects=getVos();\nObject retObj=null;\nif(inCurObjects== null||inCurObjects.length==0)  throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵���û������\"));\n//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�\nif(!(inCurObjects[0] instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵������Ͳ�ƥ��\"));\n//2,���ݺϷ���������ת��Ϊ��浥�ݡ�\nnc.vo.ic.pub.bill.GeneralBillVO[] inCurVOs=null;\ninCurVOs=(nc.vo.ic.pub.bill.GeneralBillVO[])inCurObjects;\ninCurObjects=null;\n//��ȡƽ̨����Ĳ���\nsetParameter(\"INCURVOS\",inCurVOs);\nArrayList alLockedPK=null;\nsetParameter(\"ILOCKS\",(nc.vo.ic.pub.ILockIC[])inCurVOs);\ntry{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݼ�ҵ����\nalLockedPK=(ArrayList)runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockICBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[]\"@;\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:����Ƿ���ʡ�<������>\n//Ŀǰ�Ǹ��ݵ��ݱ���ҵ�����ڼ�顣������ݵ�¼���ڼ�飬�뽫checkAccountStatus��ΪcheckAccountStatus1\nrunClassCom@\"nc.bs.ic.ic2a3.AccountctrlDMO\",\"checkAccountStatus\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//#############################################################\n//����˵��:������ⵥ�ݼ�ҵ����\n//alLockedPK=runClassCom@\"nc.bs.ic.pub.bill.GeneralBillBO\",\"lockBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[]\"@;\n//##################################################\n//�÷���<��������>\n//����˵��:����浥��ʱ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamps\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:��д�ۼƲ�ȱ����\n//���Ӷ�������Դ���ݵĲ���Ʒ��ⵥ�ݻ�д��ȱ�������ӿڵ��ж����� 20030212\nrunClassCom@\"nc.bs.ic.ic202.GeneralHBO\",\"setOOSNum\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//##################################################\nnc.vo.scm.pub.SCMEnv.out(\"=====================================================\");\nnc.vo.scm.pub.SCMEnv.out(\"=====================================================\");\nnc.vo.scm.pub.SCMEnv.out(\"=====================================================\");\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:����ǩ��\n//У�����������ʹ������ �÷�����������\nString sBarcodeCheckErr=(String)runClassCom@\"nc.bs.ic.pub.check.CheckBarcodeDMO\",\"checkBarcodeAbsent\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\nif (sBarcodeCheckErr!=null )\nsErr.append(sBarcodeCheckErr);\n//#################\n//#################\nretObj =runClassCom@ \"nc.bs.ic.ic202.GeneralHBO\", \"signBills\", \"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//##################################################\n//############################\n//����ҵ����־���÷�����������\nsetParameter(\"ERR\",sErr.toString());\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String\"@;\n//############################\n//�������ǰ�����������Ƿ�ƥ��\nif (retObj != null && ! (retObj instanceof ArrayList)) throw new BusinessException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"����ǩ�ֶ����ķ���ֵ���ʹ���\"));\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:���ɴ�����ݽӿ�\n//nc.bs.ic.pub.ictoia.Ic2IaDMO.saveIABills(inCurVOs,\"46\",\"I3\",vo);\nsetParameter(\"icbilltype\",\"46\");\nsetParameter(\"iabilltype\",\"I3\");\nsetParameter(\"PFPARAVO\",vo);\nrunClassCom@\"nc.bs.ic.pub.ictoia.Ic2IaDMO\",\"saveIABills\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&icbilltype:String,&iabilltype:String,&PFPARAVO:nc.vo.pub.compiler.PfParameterVO\"@;\n//##################################################\n}catch(Exception e){\n//############################\n//����ҵ����־���÷�����������\nsetParameter(\"EXC\",e.getMessage());\nsetParameter(\"FUN\",\"ǩ�� \");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&EXC:String,&FUN:String\"@;\n//###########################\n//���������״̬���˻ص��ݺ�\nfor(int i=0;i<inCurVOs.length;i++){\nsetParameter(\"IBC\",(nc.vo.scm.pub.IBillCode)inCurVOs[i]);\nif(inCurVOs[i].getHeaderVO().getStatus()==nc.vo.pub.VOStatus.NEW)\n         runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"returnBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n}\n          if (e instanceof BusinessException)\n\t\t\tthrow (BusinessException) e;\n\t\telse\n\t\t\tthrow new BusinessException(\"Remote Call\", e);\n}\nfinally {\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݽ�ҵ����\nif(alLockedPK!=null){\nsetParameter(\"ALPK\",alLockedPK);\n runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[],&ALPK:ArrayList\"@;\n}}\n//����ҵ����־���÷�����������\nsetParameter(\"ERR\",sErr.toString());\nsetParameter(\"FUN\",\"ǩ��\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String\"@;\n//############################\t\nArrayList alRet=new ArrayList();\nif(sErr.toString().trim().length()==0)\n\talRet.add(null);\nelse\n\talRet.add(sErr.toString());\nalRet.add(retObj);\n//���С�͵���VO��ǰ̨���� \nnc.vo.ic.pub.smallbill.SMGeneralBillVO smbillvo = inCurVOs[0].getSmallBillVO();\nalRet.add(smbillvo);\ninCurVOs=null;\nreturn new Object[]{alRet};\n\t//************************************************************************\n";
  }

  private void setParameter(String key, Object val)
  {
    if (this.m_keyHas == null) {
      this.m_keyHas = new Hashtable();
    }
    if (val != null)
      this.m_keyHas.put(key, val);
  }
}