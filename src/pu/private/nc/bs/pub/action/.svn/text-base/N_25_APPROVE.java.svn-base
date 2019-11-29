package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.pu.pub.PubDMO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.pub.compiler.BatchWorkflowRet;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.uap.pf.PFBusinessException;

public class N_25_APPROVE extends AbstractCompiler2
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
      Object inObject1 = getUserObj();
      Hashtable m_sysHasNoPassAndGonging = null;
      if (inObject == null) throw new BusinessException("错误：您希望审批的采购发票没有数据");
      InvoiceVO[] inVO = (InvoiceVO[])(InvoiceVO[])inObject;
      setParameter("INVO", inVO);
      setParameter("INVO1", inObject1);

      Object oLockRet = null;
      try
      {
        oLockRet = runClass("nc.bs.pu.pub.PubDMO", "lockPkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]", vo, this.m_keyHas, this.m_methodReturnHas);

        runClass("nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]", vo, this.m_keyHas, this.m_methodReturnHas);
        runClass("nc.bs.pu.pub.PubDMO", "isBillStateChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]", vo, this.m_keyHas, this.m_methodReturnHas);

        m_sysHasNoPassAndGonging = procFlowBacth(vo);

        String[] key = new String[inVO.length];
        for (int i = 0; i < key.length; i++) key[i] = inVO[i].getHeadVO().getCinvoiceid();
        setParameter("KEYS", key);
        Hashtable t = (Hashtable)runClass("nc.bs.pi.InvoiceDMO", "queryForSaveAuditBatch", "&KEYS:String[]", vo, this.m_keyHas, this.m_methodReturnHas);
        if ((t != null) && (t.size() > 0))
          for (int i = 0; i < inVO.length; i++) {
            ArrayList list = (ArrayList)t.get(inVO[i].getHeadVO().getCinvoiceid());
            inVO[i].getHeadVO().setDauditdate((UFDate)list.get(0));
            inVO[i].getHeadVO().setCauditpsn((String)list.get(1));
            inVO[i].getHeadVO().setIbillstatus((Integer)list.get(2));
            inVO[i].getHeadVO().setTs((String)list.get(3));
          }
      }
      catch (Exception e)
      {
        PubDMO.throwBusinessException("nc.bs.pub.action.N_25_APPROVE", e);
      }
      finally {
        if ((oLockRet != null) && (((UFBoolean)oLockRet).booleanValue())) {
          runClass("nc.bs.pu.pub.PubDMO", "freePkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]", vo, this.m_keyHas, this.m_methodReturnHas);
        }
      }
      setParameter("PFDATE", getUserDate().toString());

      Object retJS = runClass("nc.bs.pi.InvoiceImpl", "doSettleArray", "nc.vo.pi.InvoiceVO[]:25,&PFDATE:String", vo, this.m_keyHas, this.m_methodReturnHas);

      Object filteredObj = runClass("nc.bs.pi.InvoiceImpl", "filterVirtualInvoice", "nc.vo.pi.InvoiceVO[]:25", vo, this.m_keyHas, this.m_methodReturnHas);

      if (filteredObj != null) {
        UFBoolean bSucceed = new UFBoolean(false);
        InvoiceVO[] filteredVOs = (InvoiceVO[])(InvoiceVO[])filteredObj;
        if (retJS != null) {
          ArrayList list = (ArrayList)retJS;
          if ((list != null) && (list.size() > 1)) {
            bSucceed = (UFBoolean)list.get(1);
            if (bSucceed.booleanValue())
            {
              for (int i = 0; i < filteredVOs.length; i++) {
                InvoiceItemVO[] bodyVO = filteredVOs[i].getBodyVO();
                for (int j = 0; j < bodyVO.length; j++) {
                  bodyVO[j].setNaccumsettnum(bodyVO[j].getNinvoicenum());
                  bodyVO[j].setNaccumsettmny(bodyVO[j].getNmoney());
                }
              }
            }
          } else {
            bSucceed = new UFBoolean(true);
          }
        } else {
          bSucceed = new UFBoolean(true);
        }
        setParameter("INVOS", filteredVOs);
        if (bSucceed.booleanValue()) {
          boolean b = true;
          for (int i = 0; i < inVO.length; i++) {
            if ((inVO[i] != null) && (inVO[i].getHeadVO().getIbillstatus().intValue() != 3)) {
              b = false;
              break;
            }
          }
          if (b) runClass("nc.bs.pi.InvoiceImpl", "adjustForZGYF", "&INVOS:nc.vo.pi.InvoiceVO[]", vo, this.m_keyHas, this.m_methodReturnHas); 
        }
        else {
          runClass("nc.bs.pi.InvoiceImpl", "adjustForZGYF", "&INVOS:nc.vo.pi.InvoiceVO[]", vo, this.m_keyHas, this.m_methodReturnHas);
        }
      }

      BatchWorkflowRet bwr = new BatchWorkflowRet();
      bwr.setNoPassAndGoing(m_sysHasNoPassAndGonging);
      bwr.setUserObj(getVos());

      return new Object[] { bwr };
    } catch (Exception ex) {
      if ((ex instanceof BusinessException))
        throw ((BusinessException)ex);
      throw new PFBusinessException(ex.getMessage(), ex);
    }
   
  }

  public String getCodeRemark()
  {
    return "  Object[] inObject  =getVos ();\n    Object inObject1  = getUserObj ();\n    Hashtable m_sysHasNoPassAndGonging = null;\n    if (inObject == null) throw new nc.vo.pub.BusinessException ( \"错误：您希望审批的采购发票没有数据\");\n   nc.vo.pi.InvoiceVO[] inVO  = (nc.vo.pi.InvoiceVO[])inObject;\n    setParameter ( \"INVO\",inVO);\n    setParameter ( \"INVO1\",inObject1);\n  \n    Object oLockRet = null;\n   try {\n     //对采购发票申请业务锁\n      oLockRet=runClassCom@ \"nc.bs.pu.pub.PubDMO\", \"lockPkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n  \n      //并发控制\n      runClassCom@ \"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n      runClassCom@ \"nc.bs.pu.pub.PubDMO\", \"isBillStateChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n \n      m_sysHasNoPassAndGonging = procFlowBacth(vo);\n   }catch (Exception e) {\n      nc.bs.pu.pub.PubDMO.throwBusinessException(\"nc.bs.pub.action.N_25_APPROVE\",e);\n    }finally {\n      //解业务锁\n      if(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())\n        runClassCom@ \"nc.bs.pu.pub.PubDMO\", \"freePkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n   }\n   \n    setParameter ( \"PFDATE\",getUserDate().toString());\n    //审批时自动结算\n   Object retJS = runClassCom@\"nc.bs.pi.InvoiceImpl\",\"doSettleArray\",\"nc.vo.pi.InvoiceVO[]:25,&PFDATE:String\"@;\n    //方法说明:滤掉虚拟发票,返回非虚拟发票数组\n   Object filteredObj = runClassCom@\"nc.bs.pi.InvoiceImpl\",\"filterVirtualInvoice\",\"nc.vo.pi.InvoiceVO[]:25\"@;\n  \n    //滤掉虚拟发票后，组织暂估应付冲减VO, 调用应付的调差接口, 冲减暂估应付\n   if (filteredObj != null){\n     nc.vo.pub.lang.UFBoolean bSucceed = new nc.vo.pub.lang.UFBoolean(false);\n      nc.vo.pi.InvoiceVO[] filteredVOs = (nc.vo.pi.InvoiceVO[]) filteredObj;\n      if(retJS != null){\n        java.util.ArrayList list = (java.util.ArrayList)retJS;\n        if(list != null && list.size() > 1){\n          bSucceed = (nc.vo.pub.lang.UFBoolean)list.get(1);\n         if(bSucceed.booleanValue()){\n            //结算成功后,设置累计结算数量和累计结算金额,方便后续的暂估应付冲减\n           for(int i = 0; i < filteredVOs.length; i++){\n              nc.vo.pi.InvoiceItemVO bodyVO[] = filteredVOs[i].getBodyVO();\n             for(int j = 0; j < bodyVO.length; j++){\n               bodyVO[j].setNaccumsettnum(bodyVO[j].getNinvoicenum());\n               bodyVO[j].setNaccumsettmny(bodyVO[j].getNmoney());\n              }\n           }\n         }\n       }else{\n          bSucceed = new nc.vo.pub.lang.UFBoolean(true);\n        }\n     }else{\n        bSucceed = new nc.vo.pub.lang.UFBoolean(true);\n      }\n     setParameter ( \"INVOS\",filteredVOs);\n      if(bSucceed.booleanValue()) runClassCom@\"nc.bs.pi.InvoiceImpl\",\"adjustForZGYF\",\"&INVOS:nc.vo.pi.InvoiceVO[]\"@;\n    }\n   \n    nc.bs.pub.compiler.BatchWorkflowRet bwr = new nc.bs.pub.compiler.BatchWorkflowRet();\n      bwr.setNoPassAndGoing(m_sysHasNoPassAndGonging);\n      bwr.setUserObj(getVos()); \n      \n    return new Object[]{bwr};\n";
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