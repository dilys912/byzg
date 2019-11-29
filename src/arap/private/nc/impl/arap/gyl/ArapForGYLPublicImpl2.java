package nc.impl.arap.gyl;

import java.util.Hashtable;

import nc.bs.arap.gyl.ArapForGYL;
import nc.itf.arap.pub.IArapForGYLPublic;
import nc.itf.arap.pub.IArapForGYLPublic2;
import nc.vo.arap.gyl.AdjuestVO;
import nc.vo.arap.gyl.VerifyParamVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class ArapForGYLPublicImpl2
  implements IArapForGYLPublic2
{
  /**
   * 功能：应付单保存调ARAP接口
   * 作者:王凯飞
   * 日期:2014-11-18
   * 
   * */
  public void saveEffForCG2(DJZBVO paramDJZBVO) throws BusinessException {
	new ArapForGYL().saveEffForCG2(paramDJZBVO);
  }
}