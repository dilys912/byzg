package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.uap.pf.PFBusinessException;

public class N_45_SAVE extends AbstractCompiler2
{
  private Hashtable m_methodReturnHas = new Hashtable();
  private Hashtable m_keyHas = null;

  public Object runComClass(PfParameterVO vo)
    throws BusinessException
  {
    try
    {
      this.m_tmpVo = vo;

      AggregatedValueObject invo = getVo();
      setParameter("INCURVO", invo);
      Object retObj = null;
      Object alLockedPK = null;
      try
      {
        alLockedPK = runClass("nc.bs.ic.pub.bill.ICLockBO", "lockBill", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("lockBill", retObj);
        }

        runClass("nc.bs.ic.pub.check.CheckDMO", "checkSourceBillTimeStamp_new", "&INCURVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("checkSourceBillTimeStamp_new", retObj);
        }

        setParameter("BillType", "45");
        setParameter("BillDate", getUserDate().toString());
        setParameter("ActionName", "SAVEBASE");
        setParameter("P3", null);
        setParameter("P5", getUserObj());
        retObj = runClass("nc.bs.pub.pf.PfUtilBO", "processAction", "&ActionName:String,&BillType:String,&BillDate:String,&P3:nc.vo.pub.pf.PfUtilWorkFlowVO,&INCURVO:nc.vo.pub.AggregatedValueObject,&P5:Object", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("processAction", retObj);
        }
      }
      catch (Exception e)
      {
        setParameter("EXC", e.getMessage());
        setParameter("FUN", "����");
        runClass("nc.bs.ic.pub.check.CheckBO", "insertBusinessExceptionlog", "&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("insertBusinessExceptionlog", retObj);
        }

        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException("Remote Call", e);
      }
      finally
      {
        setParameter("ALLPK", (ArrayList)alLockedPK);
        if (alLockedPK != null)
          runClass("nc.bs.ic.pub.bill.ICLockBO", "unlockBill", "&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList", vo, this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("unlockBill", retObj);
        }
      }

      return retObj;
    } catch (Exception ex) {
    	 throw new PFBusinessException(ex.getMessage(), ex);
    }
   
  }

  public String getCodeRemark()
  {
    return "\t//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********\nnc.vo.pub.AggregatedValueObject invo=(nc.vo.pub.AggregatedValueObject)getVo();\nsetParameter(\"INCURVO\",invo);\nObject retObj=null;\nObject alLockedPK=null;\ntry{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݼ�ҵ����\nalLockedPK=runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//�÷���<������>\n//����˵��:�����Դ����ʱ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkSourceBillTimeStamp_new\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n\tsetParameter(\"BillType\",\"45\");\n\tsetParameter(\"BillDate\",getUserDate().toString());\n\tsetParameter(\"ActionName\",\"SAVEBASE\");\n\tsetParameter(\"P3\",null);\n\tsetParameter(\"P5\",getUserObj());\n\tretObj=runClassCom@\"nc.bs.pub.pf.PfUtilBO\",\"processAction\",\"&ActionName:String,&BillType:String,&BillDate:String,&P3:nc.vo.pub.pf.PfUtilWorkFlowVO,&INCURVO:nc.vo.pub.AggregatedValueObject,&P5:Object\"@;\n}catch(Exception e){\n//############################\n//����ҵ����־���÷�����������\nsetParameter(\"EXC\",e.getMessage());\nsetParameter(\"FUN\",\"����\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String\"@;\n//###########################\n     if (e instanceof BusinessException)\n\t\t\tthrow (BusinessException) e;\n\t\telse\n\t\t\tthrow new BusinessException(\"Remote Call\", e);\n}\nfinally{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݽ�ҵ����\nsetParameter(\"ALLPK\",(ArrayList)alLockedPK);\nif(alLockedPK!=null)\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList\"@;\n//##################################################\n}\nreturn retObj;\n//************************************************************************\n";
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