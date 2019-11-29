package nc.bs.pub.action;

import java.util.Hashtable;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uap.pf.PFBusinessException;

public class N_25_DISCARD extends AbstractCompiler2
{
  private Hashtable m_methodReturnHas = new Hashtable();
  private Hashtable m_keyHas = null;

  public Object runComClass(PfParameterVO vo)
    throws BusinessException
  {
    try
    {
      this.m_tmpVo = vo;
      Object[] inObject = getVos();
      Object[] inObject1 = getUserObjAry();
      if (inObject == null) throw new BusinessException("错误：您希望保存的作废发票没有数据");
      InvoiceVO[] inVO = (InvoiceVO[])(InvoiceVO[])inObject;
      Object retObj = null;
      setParameter("INVO", inVO);
      setParameter("INVO1", inObject1);
      Object oLockRet = null;
      try
      {
        oLockRet = runClass("nc.bs.pu.pub.PubDMO", "lockPkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]", vo, this.m_keyHas, this.m_methodReturnHas);

        retObj = runClass("nc.bs.pi.InvoiceImpl", "discardInvoiceArray", "&INVO:nc.vo.pi.InvoiceVO[],&INVO1:OBJECT[]", vo, this.m_keyHas, this.m_methodReturnHas);

        for (int i = 0; i < inVO.length; i++) {
          setParameter("RETVO", inVO[i]);
          runClass("nc.bs.pu.pub.GetSysBillCode", "returnBillNo", "&RETVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);
        }
      } catch (Exception e) {
        PubDMO.throwBusinessException("nc.bs.pub.action.N_25_DISCARD", e);
      }
      finally {
        if ((oLockRet != null) && (((UFBoolean)oLockRet).booleanValue()))
          runClass("nc.bs.pu.pub.PubDMO", "freePkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]", vo, this.m_keyHas, this.m_methodReturnHas);
      }
      return retObj;
    } catch (Exception ex) {
      if ((ex instanceof BusinessException))
        throw ((BusinessException)ex);
      throw new PFBusinessException(ex.getMessage(), ex);
    }
   
  }

  public String getCodeRemark()
  {
    return "\tObject[] inObject =getVos ();\n\t\tObject[] inObject1  = getUserObjAry ();\n\t\tif (inObject  == null) throw new nc.vo.pub.BusinessException (\"错误：您希望保存的作废发票没有数据\");\n\t\tnc.vo.pi.InvoiceVO[] inVO = (nc.vo.pi.InvoiceVO[])inObject;\n\t\tObject retObj = null;\n\t\tsetParameter ( \"INVO\",inVO);\n\t\tsetParameter ( \"INVO1\",inObject1);\n\t\tObject oLockRet = null;\n\t\ttry {\n\t\t\t//对采购发票申请业务锁\n\t\t\toLockRet=runClassCom@ \"nc.bs.pu.pub.PubDMO\", \"lockPkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n\t\t\t//并发控制\n\t\t\trunClassCom@\"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n\t\t\t//方法说明:作废发票\n\t\t\tretObj =runClassCom@ \"nc.bs.pi.InvoiceImpl\", \"discardInvoiceArray\", \"&INVO:nc.vo.pi.InvoiceVO[],&INVO1:OBJECT[]\"@;\n\t\t\t//####单据号退回处理####\n\t\t\tfor(int i = 0; i < inVO.length; i ++){\n\t\t\t\tsetParameter ( \"RETVO\",inVO[i]);\n\t\t\t\trunClassCom@\"nc.bs.pu.pub.GetSysBillCode\",\"returnBillNo\",\"&RETVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t\t}\n\t\t}catch (Exception e) {\n\t\t\tnc.bs.pu.pub.PubDMO.throwBusinessException(\"nc.bs.pub.action.N_25_DISCARD\",e);\n\t\t}finally {\n\t\t\t//解业务锁\n\t\t\tif(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())\n\t\t\t\trunClassCom@\"nc.bs.pu.pub.PubDMO\", \"freePkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n\t\t}\n\t\treturn retObj;\n";
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