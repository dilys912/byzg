package nc.bs.pub.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.itf.arap.pub.IArapBillPublic;
import nc.itf.arap.pub.IArapForGYLPublic;
import nc.itf.ia.bill.IBill;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.BillTools;
import nc.vo.so.so012.SquareHeaderVO;
import nc.vo.so.so012.SquareItemVO;
import nc.vo.so.so012.SquareUtil;
import nc.vo.so.so012.SquareVO;

public class N_33_UnSoSquare extends AbstractCompiler2
{
  private Hashtable m_methodReturnHas = new Hashtable();

  private Hashtable m_keyHas = null;

  public Object runComClass(PfParameterVO vo)
    throws BusinessException
  {
    try
    {
      this.m_tmpVo = vo;

      Object inObject = getVo();

      if (!(inObject instanceof SquareVO)) {
        throw new RemoteException("Remote Call", new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000264")));
      }

      if (inObject == null) {
        throw new RemoteException("Remote Call", new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000265")));
      }

      SCMEnv.out("$$$$$$$$$$$$$$$$$$$$$$$$$$取消结算开始!" + new UFDateTime(System.currentTimeMillis()).toString());

      SquareVO inVO = (SquareVO)inObject;
      SquareHeaderVO headerVO = (SquareHeaderVO)inVO.getParentVO();

      UFBoolean bIncome = headerVO.getBincomeflag();
      UFBoolean bCost = headerVO.getBcostflag();
      String sBillType = headerVO.getCreceipttype();

      String pk_bill = (String)headerVO.getAttributeValue("csaleid");

      inObject = null;

      setParameter("INVO", inVO);
      setParameter("CSALEID", pk_bill);

      Object retObj = null;

      Object bFlag = null;
      bFlag = runClass("nc.impl.scm.so.pub.DataControlDMO", "lockPkForVo", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

      if (retObj != null) {
        this.m_methodReturnHas.put("lockPkForVo", retObj);
      }
      try
      {
        setParameter("SourceVO", inVO);

        runClass("nc.impl.scm.so.so012.SquareDMO", "checkParallel", "&SourceVO:nc.vo.so.so012.SquareVO", vo, this.m_keyHas, this.m_methodReturnHas);

        if (retObj != null) {
          this.m_methodReturnHas.put("checkParallel", retObj);
        }

        setParameter("corVO", inVO);

        retObj = runClass("nc.impl.scm.so.so012.SquareDMO", "manageSquareCancelVO", "&corVO:nc.vo.so.so012.SquareVO", vo, this.m_keyHas, this.m_methodReturnHas);

        if (retObj != null) {
          this.m_methodReturnHas.put("manageSquareCancelVO", retObj);
        }

        SquareVO voAR = null;
        SquareVO voIA = null;
        if ((retObj != null) && ((retObj instanceof ArrayList))) {
          ArrayList tmpList = (ArrayList)retObj;
          voAR = (SquareVO)tmpList.get(0);
          voIA = (SquareVO)tmpList.get(1);
        }

        if ((bIncome != null) && (bIncome.booleanValue()) && (voAR.getChildrenVO() != null) && (voAR.getChildrenVO().length > 0))
        {
          SCMEnv.out("实例IArapBillPublic!" + new UFDateTime(System.currentTimeMillis()).toString());

          IArapBillPublic bo = (IArapBillPublic)NCLocator.getInstance().lookup(IArapBillPublic.class.getName());

          SCMEnv.out("实例IArapBillPublic!结束" + new UFDateTime(System.currentTimeMillis()).toString());

          DJZBVO djvo = null;

          if (inVO.getParentVO().getAttributeValue("carhid") == null) {
            SCMEnv.out("IArapForGYLPublic.unAdjuest开始!" + new UFDateTime(System.currentTimeMillis()).toString());

            SquareItemVO[] items = (SquareItemVO[])(SquareItemVO[])inVO.getChildrenVO();
            if (items[0].getClbh() != null) {
              IArapForGYLPublic Arbo = (IArapForGYLPublic)NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());

              String[] clbhs = SquareUtil.getClbhs(items);
              if ((clbhs != null) && (clbhs.length > 0))
                Arbo.unAdjuest(clbhs, headerVO.getPk_corp());
            }
            SCMEnv.out("IArapForGYLPublic.unAdjuest结束!" + new UFDateTime(System.currentTimeMillis()).toString());
            SCMEnv.out("IArapBillPublic.findArapBillByPK 开始!" + new UFDateTime(System.currentTimeMillis()).toString());

            djvo = bo.findArapBillByPK(inVO.getParentVO().getAttributeValue("csaleid").toString());

            SCMEnv.out("IArapBillPublic.findArapBillByPK 结束!" + new UFDateTime(System.currentTimeMillis()).toString());
            SCMEnv.out("IArapBillPublic.unAuditOneArapBill 开始!" + new UFDateTime(System.currentTimeMillis()).toString());
            if ((djvo != null) && (djvo.getParentVO() != null) && (((DJZBHeaderVO)djvo.getParentVO()).getDjzt().intValue() == 2)) {
              bo.unAuditOneArapBill(djvo);
            }
            SCMEnv.out("IArapBillPublic.unAuditOneArapBill 结束!" + new UFDateTime(System.currentTimeMillis()).toString());

            SCMEnv.out("IArapBillPublic.deleteOutArapBillByPk 开始!" + new UFDateTime(System.currentTimeMillis()).toString());

            bo.deleteOutArapBillByPk(inVO.getParentVO().getAttributeValue("csaleid").toString());

            SCMEnv.out("IArapBillPublic.deleteOutArapBillByPk 结束!" + new UFDateTime(System.currentTimeMillis()).toString());
          }
          else
          {
            SCMEnv.out("IArapBillPublic.findArapBillByPK not income 开始!" + new UFDateTime(System.currentTimeMillis()).toString());

            djvo = bo.findArapBillByPK(inVO.getParentVO().getAttributeValue("carhid").toString());

            SCMEnv.out("IArapBillPublic.findArapBillByPK not income 结束!" + new UFDateTime(System.currentTimeMillis()).toString());
            SCMEnv.out("IArapForGYLPublic.deleteEff or unAuditOneArapBill 开始!" + new UFDateTime(System.currentTimeMillis()).toString());

            IArapForGYLPublic Arbo = (IArapForGYLPublic)NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());

            if ((headerVO.getBEstimation() != null) && (headerVO.getBEstimation().booleanValue()))
            {
              if (djvo != null)
                Arbo.deleteEff(djvo);
            }
            else
            {
              if ((djvo != null) && (((DJZBHeaderVO)djvo.getParentVO()).getDjzt().intValue() == 2)) {
                bo.unAuditOneArapBill(djvo);
              }
              SCMEnv.out("IArapForGYLPublic.deleteEff or unAuditOneArapBill 结束!" + new UFDateTime(System.currentTimeMillis()).toString());
              SCMEnv.out("IArapBillPublic.deleteOutBillbyWhere 开始!" + new UFDateTime(System.currentTimeMillis()).toString());

              bo.deleteOutBillbyWhere("where zb.vouchid='" + inVO.getParentVO().getAttributeValue("carhid").toString() + "' and zb.dr=0");
            }

            SCMEnv.out("IArapBillPublic.deleteOutBillbyWhere 结束!" + new UFDateTime(System.currentTimeMillis()).toString());

            SquareItemVO[] bodys = (SquareItemVO[])(SquareItemVO[])inVO.getChildrenVO();
            String[] clbhs = SquareUtil.getClbhs(bodys);
            if ((clbhs != null) && (clbhs.length > 0)) {
              SCMEnv.out("IArapForGYLPublic.unAdjuest 开始!" + new UFDateTime(System.currentTimeMillis()).toString());

              Arbo.unAdjuest(clbhs, headerVO.getPk_corp());
              SCMEnv.out("IArapForGYLPublic.unAdjuest 结束!" + new UFDateTime(System.currentTimeMillis()).toString());
            }
          }

        }

        if ((bCost != null) && (bCost.booleanValue()) && (voIA.getChildrenVO() != null) && (voIA.getChildrenVO().length > 0))
        {
          SquareItemVO[] itemVO = (SquareItemVO[])(SquareItemVO[])inVO.getChildrenVO();

          Hashtable ht = new Hashtable();

          if (itemVO != null) {
            for (int i = 0; i < itemVO.length; i++) {
              String sTemp = itemVO[i].getCiahid();
              if ((sTemp != null) && (!ht.contains(sTemp))) {
                ht.put(sTemp, sTemp);
              }
            }
          }

          if (ht.size() == 0) {
            String operatorid = (String)inVO.getParentVO().getAttributeValue("coperatorid");

            SCMEnv.out("IBill.deleteBillFromOutter_bill 开始!" + new UFDateTime(System.currentTimeMillis()).toString());

            IBill bo = (IBill)NCLocator.getInstance().lookup(IBill.class.getName());

            bo.deleteBillFromOutter_bill(pk_bill, operatorid);
            SCMEnv.out("IBill.deleteBillFromOutter_bill 结束!" + new UFDateTime(System.currentTimeMillis()).toString());
          }
          else
          {
            String[] sCiahid = new String[ht.size()];

            Enumeration e = ht.keys();
            int i = 0;
            while (e.hasMoreElements()) {
              sCiahid[i] = ht.get(e.nextElement()).toString();
              i += 1;
            }

            setParameter("CIAHID", sCiahid);
            String sPk_corp = inVO.getParentVO().getAttributeValue("pk_corp").toString();

            setParameter("PK_CORP", sPk_corp);
            String operatorid = (String)inVO.getParentVO().getAttributeValue("coperatorid");

            setParameter("OPERATORID", operatorid);

            SCMEnv.out("IBill.deleteIABillsByIAPks_bill 开始!" + new UFDateTime(System.currentTimeMillis()).toString());

            IBill bo = (IBill)NCLocator.getInstance().lookup(IBill.class.getName());

            bo.deleteIABillsByIAPks_bill(sCiahid, sPk_corp, operatorid);

            SCMEnv.out("IBill.deleteIABillsByIAPks_bill 结束!" + new UFDateTime(System.currentTimeMillis()).toString());
          }

        }

        SCMEnv.out("$$$$$$$$$$$$$$$$$$$$$$$$$$开始取消结算处理!" + new UFDateTime(System.currentTimeMillis()).toString());

        setParameter("SourceVO", inVO);

        if (sBillType.equals("32")) {
          retObj = runClass("nc.impl.scm.so.so012.SquareDMO", "squareCancelInvoiceNew", "&SourceVO:nc.vo.so.so012.SquareVO", vo, this.m_keyHas, this.m_methodReturnHas);

          if (retObj != null)
            this.m_methodReturnHas.put("squareCancelInvoiceNew", retObj);
        }
        else if (sBillType.equals("4453")) {
          setParameter("SourceVO", inVO);
          retObj = runClass("nc.impl.scm.so.so012.SquareDMO", "squareCancelWastageNew", "&SourceVO:nc.vo.so.so012.SquareVO", vo, this.m_keyHas, this.m_methodReturnHas);

          if (retObj != null) {
            this.m_methodReturnHas.put("squareCancelWastageNew", retObj);
          }

        }
        else if (sBillType.equals("4C")) {
          retObj = runClass("nc.impl.scm.so.so012.SquareDMO", "squareCancelOutNew", "&SourceVO:nc.vo.so.so012.SquareVO", vo, this.m_keyHas, this.m_methodReturnHas);

          if (retObj != null) {
            this.m_methodReturnHas.put("squareCancelOutNew", retObj);
          }
        }
        SCMEnv.out("$$$$$$$$$$$$$$$$$$$$$$$$$$结束取消结算处理!" + new UFDateTime(System.currentTimeMillis()).toString());
        SCMEnv.out("$$$$$$$$$$$$$$$$$$$$$$$$$$结束取消结算!" + new UFDateTime(System.currentTimeMillis()).toString());
      }
      catch (Exception e)
      {
        Log.getInstance(N_33_UnSoSquare.class).error(e);

        throw BillTools.wrappBusinessExceptionForSO(e);
      }
      finally
      {
        if ((bFlag != null) && (((UFBoolean)bFlag).booleanValue()))
        {
          runClass("nc.impl.scm.so.pub.DataControlDMO", "freePkForVo", "&INVO:nc.vo.pub.AggregatedValueObject", vo, this.m_keyHas, this.m_methodReturnHas);

          if (retObj != null) {
            this.m_methodReturnHas.put("freePkForVo", retObj);
          }
        }

      }

      inVO = null;
      pk_bill = null;
      return retObj;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw BillTools.wrappBusinessExceptionForSO(ex);
    }
  }

  public String getCodeRemark()
  {
    return "\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n\t//*************从平台取得由该动作传入的入口参数。***********\n\tObject inObject  =getVo ();\n\t//1,首先检查传入参数类型是否合法，是否为空。\n\tif (! (inObject instanceof nc.vo.so.so012.SquareVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的单据类型不匹配\"));\n\tif (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的请购单没有数据\"));\n\t//2,数据合法，把数据转换。\n\tnc.vo.so.so012.SquareVO inVO  = (nc.vo.so.so012.SquareVO)inObject;\n        nc.vo.so.so012.SquareHeaderVO headerVO=(nc.vo.so.so012.SquareHeaderVO)inVO.getParentVO();\n        UFBoolean bIncome=headerVO.getBincomeflag();\n        UFBoolean bCost=headerVO.getBcostflag();\n        String sBillType=headerVO.getCreceipttype();\n\t//单据ID\n\tString pk_bill  = (String)headerVO.getAttributeValue ( \"csaleid\");\n\tinVO.setChildrenVO (null);\n\tinObject  =null;\n\t//**************************************************************************************************\n\tsetParameter ( \"INVO\",inVO);\n\tsetParameter ( \"PKBILL\",pk_bill);\n\t//**************************************************************************************************\n\tObject retObj  =null;\n\t//##################################################\n\t//方法说明:加锁\n\tObject bFlag=null;\n\tbFlag=runClassCom@ \"nc.impl.scm.so.pub.DataControlDMO\", \"lockPkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t//##################################################\n\ttry {\n\t\tsetParameter ( \"SourceVO\",inVO);\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\t//方法说明:单据状态检测\n\t\trunClassCom@ \"nc.impl.scm.so.so012.SquareDMO\", \"checkParallel\", \"&SourceVO:nc.vo.so.so012.SquareVO\"@;\n\t\t//##################################################\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\t//方法说明:删除应收应付单据\n                if(bIncome!=null&&bIncome.booleanValue())\n                {\n\t\t  runClassCom@ \"nc.bs.ep.dj.DJZBBO\", \"deleteOutBill\", \"&PKBILL:String\"@;\n                  }\n\t\t//方法说明:删除存货接口单据\n                if(bCost!=null&&bCost.booleanValue())\n                {\n\t\t  String operatorid = (String)inVO.getParentVO ().getAttributeValue ( \"coperatorid\");\n\t\t  setParameter ( \"OPERATORID\",operatorid);\n\t\t  runClassCom@ \"nc.bs.ia.bill.BillBO\", \"deleteBillFromOutter\", \"&PKBILL:String,&OPERATORID:String\"@;\n                 }\n\t\t//方法说明:结算\n\t\tsetParameter ( \"SourceVO\",inVO);\n                 //如果是发票结算\n                 if(sBillType.equals(\"32\"))\n                 {\n\t\t  runClassCom@ \"nc.impl.scm.so.so012.SquareDMO\", \"squareCancelInvoiceNew\", \"&SourceVO:nc.vo.so.so012.SquareVO\"@;\n                  }\n                 //如果是出库单结算\n                 else if(sBillType.equals(\"4C\"))\n                 {\n                   runClassCom@ \"nc.impl.scm.so.so012.SquareDMO\", \"squareCancelOutNew\", \"&SourceVO:nc.vo.so.so012.SquareVO\"@;\n                  }\n                  \n\t\t//##################################################\n\t}\n\tcatch (Exception e) {\n\t\tif (e instanceof RemoteException) throw (RemoteException)e;\n\t\telse throw e;\n\t}\n\tfinally {\n\t\t//解业务锁\n\t\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t\t//方法说明:解锁\n\t\tif(bFlag!=null && ((nc.vo.pub.lang.UFBoolean)bFlag).booleanValue()){\n\t\t\trunClassCom@ \"nc.impl.scm.so.pub.DataControlDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\"@;\n\t\t\t//##################################################\n\t\t}\t}\n\t//*********返回结果******************************************************\n\tinVO  =null;\n\tpk_bill  =null;\n\treturn retObj;\n\t//************************************************************************\n";
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