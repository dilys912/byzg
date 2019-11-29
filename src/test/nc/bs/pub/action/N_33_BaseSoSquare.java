package nc.bs.pub.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.impl.scm.so.so012.IncomeAdjust;
import nc.impl.scm.so.so012.SquareDMO;
import nc.itf.arap.pub.IArapBillPublic;
import nc.itf.arap.pub.IArapForGYLPublic;
import nc.vo.arap.gyl.AdjuestVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.so.so001.BillTools;
import nc.vo.so.so012.SquareHeaderVO;
import nc.vo.so.so012.SquareItemVO;
import nc.vo.so.so012.SquareUtil;
import nc.vo.so.so012.SquareVO;

public class N_33_BaseSoSquare extends AbstractCompiler2
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

      if (inObject == null) {
        throw new RemoteException("Remote Call", 
          new BusinessException(
          NCLangResOnserver.getInstance()
          .getStrByID("sopub", "UPPsopub-000266")));
      }

      if (!(inObject instanceof SquareVO)) {
        throw new RemoteException("Remote Call", 
          new BusinessException(
          NCLangResOnserver.getInstance()
          .getStrByID("sopub", "UPPsopub-000264")));
      }

      boolean bArflag = false;
      boolean badjust = false;
      String clbh = null;

      SquareVO inVO = (SquareVO)inObject;

      Object retObj = null;

      DJZBVO d0VO = null;
      IArapBillPublic Djzbbo = (IArapBillPublic)NCLocator.getInstance()
        .lookup(IArapBillPublic.class.getName());

      IArapForGYLPublic Arbo = (IArapForGYLPublic)
        NCLocator.getInstance().lookup(IArapForGYLPublic.class.getName());
      ArrayList ysList = null;
      SquareHeaderVO headerVO = (SquareHeaderVO)inVO
        .getParentVO();
      UFBoolean bIncome = headerVO.getBincomeflag();
      UFBoolean bCost = headerVO.getBcostflag();
      String sBillType = headerVO.getCreceipttype();
      SquareItemVO[] items = (SquareItemVO[])inVO.getChildrenVO();
      try
      {
        setParameter("SourceVO", inVO);

        retObj = runClass("nc.impl.scm.so.so012.SquareDMO", 
          "correctMny", "&SourceVO:nc.vo.so.so012.SquareVO", vo, 
          this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("correctMny", retObj);
        }
        inVO = (SquareVO)retObj;
        setParameter("corVO", inVO);
        setParameter("Date", 
          new UFDate(getUserDate().toString().substring(0, 10)));

        retObj = runClass("nc.impl.scm.so.so012.SquareDMO", 
          "manageSquareVO", 
          "&corVO:nc.vo.so.so012.SquareVO,&Date:UFDate", vo, 
          this.m_keyHas, this.m_methodReturnHas);
        if (retObj != null) {
          this.m_methodReturnHas.put("manageSquareVO", retObj);
        }

        SquareVO voAR = null;
        SquareVO voIA = null;
        if ((retObj != null) && ((retObj instanceof ArrayList))) {
          ArrayList tmpList = (ArrayList)retObj;
          voAR = (SquareVO)tmpList.get(0);
          voIA = (SquareVO)tmpList.get(1);
        }

        if ((bIncome != null) && (bIncome.booleanValue()) && ((!"4453".equals(sBillType)) || (headerVO.getBEstimation() == null) || 
          (!headerVO.getBEstimation().booleanValue())))
        {
          if ((voAR != null) && (voAR.getChildrenVO() != null) && 
            (voAR.getChildrenVO().length > 0)) {
            BaseDAO dao = new BaseDAO();
            BusitypeVO busivo = (BusitypeVO)dao.retrieveByPK(BusitypeVO.class, headerVO.getCbiztype());
            String ysbusitype = null;
            if (busivo != null)
              ysbusitype = busivo.owetype;
            if (ysbusitype == null)
              ysbusitype = "D0";
            IncomeAdjust adjust = new IncomeAdjust();
            adjust.adjustIncomeVO(voAR);
            d0VO = (DJZBVO)changeData(voAR, "33", ysbusitype);

            adjust.adjustIncomeVO(d0VO, voAR);

            AdjuestVO[] adjvos = SquareUtil.getAdjuestVO(voAR);
            if ((adjvos != null) && (adjvos.length > 0)) {
              clbh = adjust.getClbh();
              SquareUtil.initClbhForSquare(inVO, clbh);
              Arbo.Adjuest(adjvos, clbh, 
                headerVO.getCoperatorid(), 
                getUserDate().toString().substring(0, 10), 
                headerVO.getPk_corp(), 1, 0);
              badjust = true;
            }

            if (retObj != null) {
              this.m_methodReturnHas.put("ToAr", retObj);
            }
            if ((headerVO.getBEstimation() != null) && 
              (headerVO.getBEstimation().booleanValue())) {
              if ("4C".equals(headerVO.getCreceipttype()))
                Arbo.saveEff(d0VO);
            } else {
              setParameter("ArVO", d0VO);
              setParameter("Date", getUserDate().toString());

              retObj = runClass("nc.impl.scm.so.so012.SquareDMO", 
                "ToAr", 
                "&ArVO:nc.vo.ep.dj.DJZBVO,&Date:String", 
                vo, this.m_keyHas, this.m_methodReturnHas);

              if (retObj != null) {
                this.m_methodReturnHas.put("ToAr", retObj);
              }
              ysList = (ArrayList)retObj;
              if ((ysList != null) && (ysList.get(1) != null)) {
                d0VO = (DJZBVO)ysList.get(1);
              }
              else
              {
                d0VO = null;
              }
            }
            bArflag = true;

            if (d0VO != null) {
              DJZBVO ysVO = d0VO;

              inVO.getParentVO().setAttributeValue(
                "carhid", 
                ((DJZBHeaderVO)ysVO.getParentVO())
                .getVouchid());

              DJZBItemVO[] dJzbbVO = (DJZBItemVO[])ysVO
                .getChildrenVO();
              SquareItemVO[] sQuarebVO = (SquareItemVO[])inVO
                .getChildrenVO();

              if (sQuarebVO != null) {
                int iSize = sQuarebVO.length;
                for (int i = 0; i < iSize; i++) {
                  if (dJzbbVO != null) {
                    int iSizeYS = dJzbbVO.length;
                    for (int j = 0; j < iSizeYS; j++) {
                      if (dJzbbVO[j].getDdhh() == null) {
                        continue;
                      }
                      if (!dJzbbVO[j]
                        .getDdhh()
                        .equals(
                        sQuarebVO[i]
                        .getCorder_bid()))
                        continue;
                      sQuarebVO[i]
                        .setCarbid(dJzbbVO[j]
                        .getFb_oid());
                    }
                  }
                }

              }

            }

          }

        }

        if (sBillType.equals("32")) {
          setParameter("SourceVO", inVO);
          retObj = runClass("nc.impl.scm.so.so012.SquareDMO", 
            "squareByInvoiceNew", 
            "&SourceVO:nc.vo.so.so012.SquareVO", vo, this.m_keyHas, 
            this.m_methodReturnHas);
          if (retObj != null)
            this.m_methodReturnHas.put("squareByInvoiceNew", retObj);
        }
        else if (sBillType.equals("4453")) {
          if ((headerVO.getBEstimation() != null) && 
            (headerVO.getBEstimation().booleanValue())) {
            AdjuestVO[] adjvos = SquareUtil.getAdjuestVO(voAR);
            if ((adjvos != null) && (adjvos.length > 0)) {
              IncomeAdjust adjust = new IncomeAdjust();
              clbh = adjust.getClbh();

              SquareUtil.initClbhForSquare(inVO, clbh);
              Arbo.Adjuest(adjvos, clbh, 
                headerVO.getCoperatorid(), 
                getUserDate().toString().substring(0, 10), 
                headerVO.getPk_corp(), 1, 0);
              badjust = true;
            }
          }
          setParameter("SourceVO", inVO);
          retObj = runClass("nc.impl.scm.so.so012.SquareDMO", 
            "squareByWastageNew", 
            "&SourceVO:nc.vo.so.so012.SquareVO", vo, this.m_keyHas, 
            this.m_methodReturnHas);
          if (retObj != null) {
            this.m_methodReturnHas.put("squareByWastageNew", retObj);
          }

        }
        else if (sBillType.equals("4C")) {
          setParameter("SourceVO", inVO);
          retObj = runClass("nc.impl.scm.so.so012.SquareDMO", 
            "squareByOutNew", 
            "&SourceVO:nc.vo.so.so012.SquareVO", vo, this.m_keyHas, 
            this.m_methodReturnHas);
          if (retObj != null) {
            this.m_methodReturnHas.put("squareByOutNew", retObj);
          }

        }

        if ((bCost != null) && (bCost.booleanValue()) && 
          (voIA != null) && (voIA.getChildrenVO() != null) && 
          (voIA.getChildrenVO().length > 0)) {
          setParameter("SquareToIaVO", voIA);
          setParameter("Date", 
            getUserDate().toString().substring(0, 10));
          setParameter("ParVO", getPfParameterVO());
          retObj = runClass(
            "nc.impl.scm.so.so012.SquareDMO", 
            "ToIa", 
            "&SquareToIaVO:nc.vo.so.so012.SquareVO,&Date:String,&ParVO:nc.vo.pub.compiler.PfParameterVO", 
            vo, this.m_keyHas, this.m_methodReturnHas);
          if (retObj != null) {
            this.m_methodReturnHas.put("ToIa", retObj);
          }

          if (retObj != null) {
            ArrayList alTotal = null;
            alTotal = (ArrayList)retObj;

            if (alTotal != null) {
              SquareItemVO[] itemVOs = (SquareItemVO[])inVO
                .getChildrenVO();

              if (itemVOs != null) {
                for (int i = 0; i < alTotal.size(); i++) {
                  HashMap hm = (HashMap)(HashMap)alTotal.get(i);

                  if (hm != null) {
                    for (int j = 0; j < itemVOs.length; j++) {
                      if (!hm.containsKey(
                        itemVOs[j].getCorder_bid())) continue;
                      ArrayList altemp = (ArrayList)
                        (ArrayList)hm
                        .get(itemVOs[j]
                        .getCorder_bid());
                      itemVOs[j].setCiahid(
                        altemp.get(0).toString());
                      itemVOs[j].setCiabid(
                        altemp.get(1).toString());
                    }
                  }
                }
              }

            }

          }

        }

        SquareDMO dwSquare = new SquareDMO();
        dwSquare.UpdateSquareYSCB(inVO);

        if ((bIncome != null) && (bIncome.booleanValue()) && ((headerVO.getBEstimation() == null) || (!headerVO.getBEstimation().booleanValue())))
        {
          DJZBVO ysVO = d0VO;

          setParameter("SquareVO", inVO);
          setParameter("ArVO", ysVO);
          runClass(
            "nc.impl.scm.so.so012.SquareDMO", 
            "SoBalance", 
            "&SquareVO:nc.vo.so.so012.SquareVO,&ArVO:nc.vo.ep.dj.DJZBVO", 
            vo, this.m_keyHas, this.m_methodReturnHas);
          if (retObj != null)
            this.m_methodReturnHas.put("SoBalance", retObj);
        }
      }
      catch (Exception e)
      {
        Log.getInstance(N_33_BaseSoSquare.class).error(e);

        throw BillTools.wrappBusinessExceptionForSO(e);
      }

      return retObj;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw BillTools.wrappBusinessExceptionForSO(ex);
    }
  }

  public String getCodeRemark()
  {
    return "\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n\t//*************从平台取得由该动作传入的入口参数。***********\n\tObject inObject  =getVo ();\n\t//1,首先检查传入参数类型是否合法，是否为空。\n\tif (inObject  == null) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的单据没有数据\"));\n        if (! (inObject instanceof nc.vo.so.so012.SquareVO)) throw new java.rmi.RemoteException ( \"Remote Call\",new nc.vo.pub.BusinessException ( \"错误：您希望保存的单据类型不匹配\"));\n\t//2,数据合法，把数据转换。\n\tnc.vo.so.so012.SquareVO inVO  = (nc.vo.so.so012.SquareVO)inObject;\n        Object retObj  =null;\n        java.util.ArrayList ysList  =null;\n        nc.vo.so.so012.SquareHeaderVO headerVO=(nc.vo.so.so012.SquareHeaderVO)inVO.getParentVO();\n        UFBoolean bIncome=headerVO.getBincomeflag();\n        UFBoolean bCost=headerVO.getBcostflag();\n        String sBillType=headerVO.getCreceipttype();\n                           \n       try{\n\tsetParameter ( \"SourceVO\",inVO);\n\t\n  \t//找平结算金额\n\tretObj =runClassCom@\"nc.impl.scm.so.so012.SquareDMO\",\"correctMny\",\"&SourceVO:nc.vo.so.so012.SquareVO\"@;\n\tinVO  = (nc.vo.so.so012.SquareVO)retObj;\n\tsetParameter ( \"corVO\",inVO);\n\tsetParameter ( \"Date\",new UFDate(getUserDate().toString().substring(0,10)));\n\t//根据规则拆分数据\n\t//##################################################\n\t//方法说明:按业务类型拆分数据对象\n\tretObj  =runClassCom@ \"nc.impl.scm.so.so012.SquareDMO\", \"manageSquareVO\", \"&corVO:nc.vo.so.so012.SquareVO,&Date:UFDate\"@;\n\t//##################################################\n\t//获取\n\tnc.vo.so.so012.SquareVO voAR  =null;\n\tnc.vo.so.so012.SquareVO voIA  =null;\n\tif (retObj != null && (retObj instanceof java.util.ArrayList)) {\n\t\tjava.util.ArrayList tmpList  = (java.util.ArrayList)retObj;\n\t\tvoAR  = (nc.vo.so.so012.SquareVO)tmpList.get (0);\n\t\tvoIA  = (nc.vo.so.so012.SquareVO)tmpList.get (1);\n\t}\n                //如果是收入结算,传应收生成应收单\n                if(bIncome!=null&&bIncome.booleanValue())\n                {\n                    \n\t\t    if (voAR != null && voAR.getChildrenVO() != null && voAR.getChildrenVO().length >0) \n                    {\t\n                     setParameter ( \"SquareToArVO\",voAR);\n                     setParameter ( \"Date\",getUserDate().toString());\n                     setParameter ( \"ParVO\",getPfParameterVO());\n                     retObj=runClassCom@ \"nc.impl.scm.so.so012.SquareDMO\", \"ToAr\", \"&SquareToArVO:nc.vo.so.so012.SquareVO,&Date:String,&ParVO:nc.vo.pub.compiler.PfParameterVO\"@;\n                     ysList= (java.util.ArrayList)retObj;\n\t\t     }\n                }\n                \n                 //如果成本结算单据，传存货生成存货成本结转单\n                 if(bCost!=null&&bCost.booleanValue())\n                 {\n                  if (voIA != null && voIA.getChildrenVO() != null && voIA.getChildrenVO().length >0)\n                   {\n                    setParameter ( \"SquareToIaVO\",voIA);\n                    setParameter ( \"Date\",getUserDate().toString().substring(0,10));\n                    setParameter ( \"ParVO\",getPfParameterVO());\n                    runClassCom@ \"nc.impl.scm.so.so012.SquareDMO\", \"ToIa\", \"&SquareToIaVO:nc.vo.so.so012.SquareVO,&Date:String,&ParVO:nc.vo.pub.compiler.PfParameterVO\"@;\n                    }\n                  }\n                 //如果是发票结算\n                 if(sBillType.equals(\"32\"))\n                 {\n                    setParameter ( \"SourceVO\",inVO);\n\t\t    retObj  =runClassCom@ \"nc.impl.scm.so.so012.SquareDMO\", \"squareByInvoiceNew\", \"&SourceVO:nc.vo.so.so012.SquareVO\"@;\n                   }\n                 //如果是出库单结算\n                 else if(sBillType.equals(\"4C\"))\n                 {\n                    setParameter ( \"SourceVO\",inVO);\n\t\t    retObj  =runClassCom@ \"nc.impl.scm.so.so012.SquareDMO\", \"squareByOutNew\", \"&SourceVO:nc.vo.so.so012.SquareVO\"@;\n                  }\n                 //收入结算，则核销\n                 if(bIncome!=null&&bIncome.booleanValue())\n                 {   \n                   if (ysList!=null&&ysList.get(1) != null)\n                   {\n                     nc.vo.ep.dj.DJZBVO ysVO = (nc.vo.ep.dj.DJZBVO)ysList.get(1);\n                     setParameter ( \"SquareVO\",inVO);\n                     setParameter ( \"ArVO\",ysVO);\n                     runClassCom@ \"nc.impl.scm.so.so012.SquareDMO\", \"SoBalance\", \"&SquareVO:nc.vo.so.so012.SquareVO,&ArVO:nc.vo.ep.dj.DJZBVO\"@;\n                     }\n                  }\n           }\n                    catch (Exception e)\n                    {\n                      if (e instanceof RemoteException) \n          \t      throw (RemoteException)e;\n\t\t      else throw e;\n\t             }\n\t//##################################################\n\treturn retObj;\n\t//##################################################\n";
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