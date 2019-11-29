package nc.impl.mm.prv;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.mo.mo1020.MoBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.itf.mm.prv.IMO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.b431.InvbasdocVO;
import nc.vo.bd.fd.DdVO;
import nc.vo.mm.pub.FreeItemVO;
import nc.vo.mm.pub.pub1030.MOSourceVO;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mm.pub.pub1030.MoItemVO;
import nc.vo.mm.pub.pub1030.MoVO;
import nc.vo.mm.pub.pub1030.PickmVO;
import nc.vo.mo.mo1020.ProductVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sf.sf1020.KhwlszVO;

public class MOImpl
  implements IMO
{
  public String[] unfinish_MO(MoVO[] param0)
    throws BusinessException
  {
    return new MoBO().unfinish(param0);
  }

  public void freeArray_MO(String[] param0, String param1)
    throws BusinessException
  {
    new MoBO().freeArray(param0, param1);
  }

  public boolean lockArray_MO(String[] param0, String param1)
    throws BusinessException
  {
    return new MoBO().lockArray(param0, param1)==0?false:true;
  }

  public ProductVO getMoProduct(String param0, UFDouble param1, FreeItemVO param2)
    throws BusinessException
  {
    return new MoBO().getProduct(param0, param1, param2);
  }

  public void createMOByMOArray(MoVO[] param0)
    throws BusinessException
  {
    new MoBO().createMO(param0);
  }

  public ArrayList createMOByPOArray(MoVO param0, ArrayList param1, UFBoolean param2)
    throws BusinessException
  {
    return new MoBO().createMO(param0, param1, param2);
  }

  public String[] getAvaVersion(String param0, String param1, String param2, String param3, UFDouble param4, FreeItemVO param5)
    throws BusinessException
  {
    return new MoBO().getAvaVersion(param0, param1, param2, param3, param4, param5);
  }

  public MoHeaderVO[] queryMoByWhere(String param0)
    throws BusinessException
  {
    return new MoBO().queryByWhere(param0);
  }

  public DdVO[] getDds_MO(String param0, String param1)
    throws BusinessException
  {
    return new MoBO().getDds(param0, param1);
  }

  public void checkTimeStamp_Mo(MoVO[] param0)
    throws BusinessException
  {
    new MoBO().checkTimeStamp(param0);
  }

  public ArrayList getFlushMOForMO(MoVO[] param0)
    throws BusinessException
  {
    return new MoBO().getFlushMO(param0);
  }

  public MoItemVO[] getMoItems(String param0)
    throws BusinessException
  {
    return new MoBO().getItems(param0);
  }

  public String[] getPickmStat(String[] param0)
    throws BusinessException
  {
    return new MoBO().getPickmStat(param0);
  }

  public MoVO[] moput(MoVO[] param0)
    throws BusinessException
  {
    return new MoBO().moput(param0);
  }

  public String[] mounput(MoVO[] param0)
    throws BusinessException
  {
    return new MoBO().mounput(param0);
  }

  public String[] over(MoVO[] param0)
    throws BusinessException
  {
    return new MoBO().over(param0);
  }

  public String[] unover(MoVO[] param0)
    throws BusinessException
  {
    return new MoBO().unover(param0);
  }

  public void writeDczt(MoVO[] param0)
    throws BusinessException
  {
    new MoBO().writeDczt(param0);
  }

  public InvbasdocVO[] getInvBasDocVO(String[] param0)
    throws BusinessException
  {
    return new MoBO().getInvBasDocVO(param0);
  }

  public String getManIDFromProID(String param0)
    throws BusinessException
  {
    return new MoBO().getManIDFromProID(param0);
  }

  public void updatePrintedFlag(String[] param0)
    throws BusinessException
  {
    new MoBO().updatePrintedFlag(param0);
  }

  public void backwrite(MoVO[] param0, UFDouble param1, String param2, UFDateTime param3)
    throws BusinessException
  {
    new MoBO().backwrite(param0, param1, param2, param3);
  }

  public PickmVO findClosablePickm(String param0)
    throws BusinessException
  {
    return new MoBO().findClosablePickm(param0);
  }

  public void revisemo(String param0, String param1, MoVO param2)
    throws BusinessException
  {
    new MoBO().revisemo(param0, param1, param2);
  }

  public KhwlszVO[] getKhwls(String param0, String param1, String param2)
    throws BusinessException
  {
    return new MoBO().getKhwls(param0, param1, param2);
  }

  public PraybillVO motopro(MoVO param0, String param1, UFDate param2)
    throws BusinessException
  {
    return new MoBO().motopro(param0, param1, param2);
  }

  public String[] saveMO(MoVO[] param0, PickmVO[] param1)
    throws BusinessException
  {
    return new MoBO().saveMO(param0, param1);
  }

  public String[] batchRevise(MoVO[] param0, Integer[] param1)
    throws BusinessException
  {
    return new MoBO().batchRevise(param0, param1);
  }

  public void finishMOByPgd(String[] param0, String param1, String param2)
    throws BusinessException
  {
    new MoBO().finishMOByPgd(param0, param1, param2);
  }

  public void deleteBySource(String[] param0, String[] param1, Integer param2)
    throws BusinessException
  {
    new MoBO().deleteBySource(param0, param1, param2);
  }

  public String saveMOWithMorePickms(MoVO param0, PickmVO[] param1)
    throws BusinessException
  {
    return new MoBO().saveMOWithMorePickms(param0, param1);
  }

  public String getMainWCenterID(String param0)
    throws BusinessException
  {
    return new MoBO().getMainWCenterID(param0);
  }

  public String getRtVerByMainWCenter(MoVO param0, String param1)
    throws BusinessException
  {
    return new MoBO().getRtVerByMainWCenter(param0, param1);
  }

  public void delete_MO(MoVO[] param0)
    throws BusinessException
  {
    new MoBO().delete(param0);
  }

  public ArrayList update_MO(MoVO param0)
    throws BusinessException
  {
//	  return new MoBO().update(param0);
	  ArrayList list = new MoBO().update(param0);
	  //add by shikun 制盖修订功能可以同步备料计划（未发货的备料计划）
	  if (param0!=null) {
		  MoHeaderVO moHeader = (MoHeaderVO) param0.getHeadVO();
		  String pk_corp = moHeader.getPk_corp()==null?"":moHeader.getPk_corp();
		  if ("1078".equals(pk_corp)||"1108".equals(pk_corp)) {//制盖使用该功能1078
			  String pk_moid = moHeader.getPk_moid()==null?"":moHeader.getPk_moid();
			  //校验备料计划是否发货
			  StringBuffer sql = new StringBuffer();
			  sql.append(" select pk_pickmid ") 
			  .append("   from mm_pickm_b ") 
			  .append("  where nvl(dr, 0) = 0 ") 
			  .append("    and (nvl(ljcksl, 0) > 0 or nvl(ljyfsl, 0) > 0) ") 
			  .append("    and pk_pickmid in ") 
			  .append("        (select pk_pickmid from mm_pickm where lyid = '"+pk_moid+"'); ") ;
			  BaseDAO baseDAO = new BaseDAO();
			  Object pk_pickmid = baseDAO.executeQuery(sql.toString(), new ColumnProcessor());
			  if (pk_pickmid==null||"".equals(pk_pickmid)) { 
				  //更新备料计划
				  PfUtilTools pftool = new PfUtilTools();
				  nc.vo.mm.pub.pub1030.PickmVO pkvo  = (nc.vo.mm.pub.pub1030.PickmVO) pftool.runChangeData("A2", "A3", param0, null);
				  nc.bs.mo.mo2010.PickmBO pickmbo = new nc.bs.mo.mo2010.PickmBO();
				  pickmbo.ModifyLydj2(pkvo,new UFBoolean(true));
				  //备料计划更新完成
				  //begin 因为是投放的生产订单，所以要自动审核备料计划
				  nc.bs.mo.mo2010.PickmBO pickmBO = new nc.bs.mo.mo2010.PickmBO();
				  pickmBO.Check(pkvo);
				  //end 因为是投放的生产订单，所以要自动审核备料计划
			  }
			  //校验结束
		  }
	  }
	  //end shikun
    return list;
  }

  public MOSourceVO[] getSources(String param0)
    throws BusinessException
  {
    return new MoBO().getSources(param0);
  }

  public String[] finishMO(MoVO[] param0)
    throws BusinessException
  {
    return new MoBO().finish(param0);
  }
}