package nc.bs.pub.action;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mm.pub.pub1030.MoItemVO;
import nc.vo.mm.pub.pub1030.MoVO;
import nc.vo.mm.pub.pub1030.PickmHeaderVO;
import nc.vo.mm.pub.pub1030.PickmVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class N_A2_SAVE extends AbstractCompiler2
{
  private Hashtable m_methodReturnHas = new Hashtable();
  private Hashtable m_keyHas = null;

  public Object runComClass(PfParameterVO vo)
    throws BusinessException
  {
    this.m_tmpVo = vo;
    Object retObj = null;

    MoVO m_vo = (MoVO)getVo();
    ArrayList m_arrayPO = (ArrayList)getUserObj();

    setParameter("polist", m_arrayPO);
    setParameter("isPlat", new UFBoolean(true));

    if (retObj != null) {
      this.m_methodReturnHas.put("modifyATP", retObj);
    }

    long time1 = System.currentTimeMillis();

    retObj = runClass("nc.bs.mo.mo1020.MoBO", "createMO", "nc.vo.mm.pub.pub1030.MoVO:A2,&polist:ARRAYLIST,&isPlat:UFBOOLEAN", vo, this.m_keyHas, this.m_methodReturnHas);
    long time2 = System.currentTimeMillis();
    long timesavemo = time2 - time1;
    System.out.print("zhanj nc.bs.mo.mo1020.MoBO:createMO");
    System.out.print(timesavemo);

    if (retObj != null) {
      this.m_methodReturnHas.put("createMO", retObj);
    }

    MoHeaderVO mhead = (MoHeaderVO)m_vo.getParentVO();
    ArrayList pklist = (ArrayList)retObj;
    mhead.setPk_moid((String)pklist.get(0));

    m_vo.setChildrenVO(new MoItemVO[0]);
    setVo(m_vo);

    if (mhead.getDdlx().intValue() == 1) return retObj;

    PickmVO pkvo = (PickmVO)changeData(m_vo, "A2", "A3");
    if (mhead.getJhkgrq().after(mhead.getBusiDate()))
      ((PickmHeaderVO)pkvo.getParentVO()).setLogDate(mhead.getJhkgrq().toString());
    else
      ((PickmHeaderVO)pkvo.getParentVO()).setLogDate(mhead.getBusiDate().toString());
    setParameter("pkmVO", pkvo);
    long time3 = System.currentTimeMillis();
    runClass("nc.bs.mo.mo2010.PickmBO", "savePickm", "&pkmVO:nc.vo.mm.pub.pub1030.PickmVO", vo, this.m_keyHas, this.m_methodReturnHas);
    long time4 = System.currentTimeMillis();
    long timepick = time4 - time3;
    System.out.print("zhanj nc.bs.mo.mo2010.PickmBOsavePickm");
    System.out.print(timepick);

    return retObj;
  }

  public String getCodeRemark()
  {
    return "\tObject retObj  =null;\n\t//解析参数\n\tnc.vo.mo.mo1020.MoVO m_vo  = (nc.vo.mo.mo1020.MoVO) getVo ();\t//标准参数MO\n\tArrayList m_arrayPO  = (ArrayList) getUserObj ();\t\t//标准参数POARRAYLIST\n\t//#############################################\n\t//创建生产订单并记录订单关系\n\tsetParameter ( \"polist\", m_arrayPO);\n\tsetParameter ( \"isPlat\", new UFBoolean (true));\n\t//##################################################\n\t//可用量接口\n\t//在createMO方法中处理可用量\n//\trunClassCom@\"nc.bs.mm.pub.pub1030.SCDDAPT\",\"modifyATP\",\"nc.vo.pub.AggregatedValueObject:01\"@;\n\t//##################################################\n\tretObj=runClassCom@\"nc.bs.mo.mo1020.MoBO\",\"createMO\",\"nc.vo.mo.mo1020.MoVO:A2,&polist:ARRAYLIST,&isPlat:UFBOOLEAN\"@;\n\t//#############################################\n\treturn retObj;\n";
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