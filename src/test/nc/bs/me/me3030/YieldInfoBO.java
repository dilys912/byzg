package nc.bs.me.me3030;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.bs.me.me3020.GzzxflszDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.mm.pub.ReferenceManager;
import nc.bs.mo.mo1020.MoDMO;
import nc.bs.mo.mo1030.WrAssistDMO;
import nc.bs.mm.pub.MMBusinessObject;
import nc.itf.qc.log.IPlatformlog;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.me.me3030.YieldInfoVO;
import nc.vo.mm.pub.MMBusinessException;
import nc.vo.mo.mo1030.WrHeaderVO;
import nc.vo.mo.mo1030.WrItemVO;
import nc.vo.mo.mo1030.WrVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class YieldInfoBO extends MMBusinessObject{
  public YieldInfoBO(){
  }

  /**
   * ejbCreate
   */
  public void ejbCreate(){
  }

  /**
   * 根据工作中心类id查找定义的单据模板id
   */
  public String getBillTempletID(String pk_wkclassid) throws MMBusinessException{
    try{
      GzzxflszDMO dmo = new GzzxflszDMO();
      String strTemplateID = dmo.findCcxxmbidByWkClassid(pk_wkclassid);
      return strTemplateID;
    } catch(Exception ex){
      throw new MMBusinessException("" , ex);
    }
  }

  /**
   * 查询方法
   */
  public YieldInfoVO[] queryByWhere(String whereClause) throws MMBusinessException{
    try{
      YieldInfoDMO dmo = new YieldInfoDMO();
      YieldInfoVO[] vos = dmo.queryByWhere(whereClause);
      return vos;
    } catch(Exception ex){
      throw new MMBusinessException("" , ex);
    }
  }

  /**
   * 取成分检验标志
   */
  public boolean[] getIscheck(String[] invcodes , String corp , String calbody) throws MMBusinessException{
    try{
      YieldInfoDMO dmo = new YieldInfoDMO();

      boolean[] iselementcheck = (new YieldInfoDMO()).getIscheck(invcodes , corp , calbody);

      return iselementcheck;
    } catch(Exception ex){
      throw new MMBusinessException("" , ex);
    }
  }

  /**
   * 取产出序号
   */
  public String[] getCcxhs(String strWkID , int count) throws MMBusinessException{
    try{
      YieldInfoDMO dmo = new YieldInfoDMO();
      String[] ccxhs = dmo.getCcxhs(strWkID , count);
      return ccxhs;
    } catch(Exception ex){
      throw new MMBusinessException("" , ex);
    }
  }

  /**
   * 查询生产订单的相关显示信息
   */
  public YieldInfoVO getMoRelatedInfo(String strMoID) throws MMBusinessException{
    try{
      YieldInfoDMO dmo = new YieldInfoDMO();
      return dmo.findMoRelatedInfo(strMoID);
    } catch(Exception ex){
      throw new MMBusinessException("" , ex);
    }
  }

  /**
   * 保存方法，根据vo状态分别做增加、删除和修改处理
   */
  public String[] save(YieldInfoVO[] vos) throws MMBusinessException{
    if(vos == null){
      return null;
    }
    try{
      YieldInfoDMO dmo = new YieldInfoDMO();
      //产出序号唯一校验
      checkCcxhDuplicated(vos);
      //检查引用
      checkReferrence(vos);
      //保存
      String[] keys = new String[vos.length];
      //先删除
      for(int i = 0;i < vos.length;i++){
	if(vos[i].getStatus() == VOStatus.DELETED){
	  dmo.delete(vos[i]);
	}
      }
      //再增改
      for(int i = 0;i < vos.length;i++){
	keys[i] = vos[i].getPk_ddccxxid();
	switch(vos[i].getStatus()){
	  case VOStatus.UNCHANGED:
	    break;
	  case VOStatus.NEW:
	    keys[i] = dmo.insert(vos[i]);
	    break;
	  case VOStatus.UPDATED:
	    dmo.update(vos[i]);
	  if(isNull(vos[i].getPk_ddccxxid())||isNull(vos[i].getCcxh()))
	  	throw new BusinessException("");
	  else{
		  IPlatformlog pfl = (IPlatformlog)NCLocator.getInstance().lookup(IPlatformlog.class.getName());
	//	  pfl.updateProdserialnumForMM(new String[] {vos[i].getPk_ddccxxid().toString()},new String[] {vos[i].getCcxh().toString()});
//	  	PlatformlogDMO pdmo=new PlatformlogDMO();
//	  	pdmo.updateProdserialnumForMM(new String[] {vos[i].getPk_ddccxxid().toString()},new String[] {vos[i].getCcxh().toString()});
	  	
	  	
	  //	nc.bs.qc.log.PlatformlogDMO.updateProdserialnumForMM(String[] aStrary_ID, String[] aStrary_Name)
	  WrAssistDMO wrdmo=new  WrAssistDMO();
	  
	  wrdmo.synWrCcxh(new String[] {vos[i].getPk_ddccxxid()},new String[] {vos[i].getCcxh()});
	  }
	    break;
	}
      }
      return keys;
    } catch(Exception ex){
      throw new MMBusinessException("" , ex);
    }
  }

  /**
   * 检查是否被引用
   * @param vos
   * @throws BusinessException
   * created on 2004-7-2
   */
  private void checkReferrence(YieldInfoVO[] vos) throws BusinessException{
    try{
      YieldInfoDMO dmo = new YieldInfoDMO();
      //先取到所有删除的和要修改的主键
      //删除的需要检查引用，修改的需要检查是否ccxh变化决定是否检查引用
      ArrayList al = new ArrayList(vos.length);
      ArrayList alUpdate = new ArrayList(vos.length);
      HashMap updateMap = new HashMap();
      for(int i = 0;i < vos.length;i++){
	if(vos[i].getStatus() == VOStatus.DELETED){
	  al.add(vos[i].getCcxh());
	} else if(vos[i].getStatus() == VOStatus.UPDATED){
	  alUpdate.add(vos[i].getPk_ddccxxid());
	  updateMap.put(vos[i].getPk_ddccxxid() , vos[i]);
	}
      }
  	//由于修改产出序号，将要回写完工报告，现在不做以下判断（20050607zj)
//      //过滤修改态的vo主键，如果产出序号不变化则不必检查引用
//      //把需要检查引用的主键加到al中
//      if(alUpdate.size() > 0){
//	//查询对应的VO
//	String[] updateKeys = new String[alUpdate.size()];
//	alUpdate.toArray(updateKeys);
//	YieldInfoVO[] updatedVOs = dmo.queryInternalByKeys(updateKeys);

//	//比较产出序号是否变化
//	YieldInfoVO updatingVO;
//	for(int i = 0;i < updatedVOs.length;i++){
//	  updatingVO = (YieldInfoVO)updateMap.get(updatedVOs[i].getPk_ddccxxid());
//	  if(updatedVOs[i].getCcxh() == null){
//	    if(updatingVO.getCcxh() != null){
//	      al.add(updatedVOs[i].getCcxh());
//	    }
//	  } else if(!updatedVOs[i].getCcxh().equals(updatingVO.getCcxh())){
//	    al.add(updatedVOs[i].getCcxh());
//	  }
//	}
  //   }

      if(al.size() > 0){
//	ReferenceManagerDMO refDMO = new ReferenceManagerDMO();
	for(int i = 0;i < al.size();i++){
	  ReferenceManager.isReferenced("mm_ddccxx" , (String)al.get(i));
	}
      }
    } catch(Exception ex){
      throw new BusinessException(ex.getMessage());
    }
  }

  /**
   * 检查单据号是否重复
   */
  private void checkCcxhDuplicated(YieldInfoVO[] vos) throws BusinessException{
    try{
    	if(vos.length<1)
    		return;
      YieldInfoDMO dmo = new YieldInfoDMO();
      //统计要增加的产出序号
      HashMap map = new HashMap(vos.length);
      for(int i = 0;i < vos.length;i++){
	if(vos[i].getStatus() == VOStatus.DELETED
	    || vos[i].getCcxh() == null
	    || vos[i].getCcxh().trim().length() == 0){
	  continue;
	}
	if(map.containsKey(vos[i].getCcxh())){
	  throw new ValidationException(
	      nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030" , "UPP50203030-000000" , null
	      , new String[]{vos[i].getCcxh()}) /*@res""产出序号重复: vos[i].getCcxh()'""*/ + "'!");
	} else{
	  map.put(vos[i].getCcxh() , vos[i].getPk_ddccxxid());
	}
      }
      if(map.size() == 0){
	return;
      }
      //把要删除的VO的产出序号除去
      for(int i = 0;i < vos.length;i++){
	if(vos[i].getStatus() == VOStatus.DELETED){
	  if(map.containsKey(vos[i].getCcxh())){
	    map.remove(vos[i].getCcxh());
	  }
	}
      }
      if(map.size() == 0){
	return;
      }
      //检查这些序号是否已经存在
      String[] newCcxhs = new String[map.size()];
      map.keySet().toArray(newCcxhs);
      String[] keysByCcxhs = dmo.getPksByCcxhs(newCcxhs,vos[0].getGzzxid());
      //抛出异常
      if(keysByCcxhs == null || keysByCcxhs.length == 0){
	return;
      }
      StringBuffer errmsg = new StringBuffer("");
      for(int i = 0;i < newCcxhs.length;i++){
	if(keysByCcxhs[i] != null
	    && !keysByCcxhs[i].equals(map.get(newCcxhs[i]))){
	  errmsg.append("'").append(newCcxhs[i]).append("', ");
	}
      }
      if(errmsg.length() > 0){
	errmsg.insert(0 , nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030"
	    , "UPP50203030-000001") /*@res""校验失败，下列产出序号已经存在: ""*/);
	errmsg.delete(errmsg.length() - 2 , errmsg.length());
	errmsg.append("!");
	throw new ValidationException(errmsg.toString());
      }
    } catch(Exception ex){
      throw new BusinessException(ex.getMessage());
    }
  }

  /*
   *
   * 将YieldInfoVO转换为WrVO
   *
   * @author zhanj
   * fixed by hoofi 2005-02-05
   */
  private WrVO[] convertYield2Wr(YieldInfoVO[] vos , String userid) throws MMBusinessException{

    try{
      MoDMO dmo = new MoDMO();
      //提取所有的生产订单id
      HashMap moMap = new HashMap(vos.length);
      nc.vo.mm.pub.pub1030.MoHeaderVO moHeader;
      for(int i = 0;i < vos.length;i++){
	if(!moMap.containsKey(vos[i].getScddid())){
	  moHeader = dmo.findHeaderByPrimaryKey(vos[i].getScddid());
	  moMap.put(moHeader.getPk_moid() , moHeader);
	}
      }
      //查询物料是否批次管理
      String[] invcodes = new String[vos.length];
      for(int i = 0;i < invcodes.length;i++){
	invcodes[i] = vos[i].getWlbm();
      }
      String pk_corp = vos[0].getPk_corp();
      boolean[] bBatchManageFlags = (new YieldInfoDMO()).getBatchManageFlags(invcodes , pk_corp);
      //转化成完工报告VO数组
      WrVO[] wrVOs = new WrVO[vos.length];
      for(int i = 0;i < vos.length;i++){
	moHeader = (nc.vo.mm.pub.pub1030.MoHeaderVO)moMap.get(vos[i].getScddid());
	//检查批次管理的物料是否有批次号
	if(bBatchManageFlags[i] && isNull(moHeader.getPch())){
	  throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030" , "UPP50203030-000002" , null
	      , new String[]{vos[i].getWlbm()}) /*@res""物料 {""*/ + vos[i].getWlbm()
	      + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030"
	      , "UPP50203030-000003") /*@res""} 对应的订单没有批次号，报检操作取消!""*/);
	}
	//表头
	WrHeaderVO wrHeaderVO = new WrHeaderVO();
	wrHeaderVO.setPk_corp(vos[i].getPk_corp());
	wrHeaderVO.setScbmid(moHeader.getScbmid()); //报检部门
//				wrHeaderVO.setGzzxbmid(vos[i].getGzzxid()); //工作中心
	wrHeaderVO.setUserid(userid);
	//表体
	WrItemVO wrItemVO = new WrItemVO();
	wrItemVO.setPk_corp(vos[i].getPk_corp());
	wrItemVO.setPk_wr_bid(vos[i].getPk_ddccxxid()); //产出信息行id
	wrItemVO.setCcxh(vos[i].getCcxh()); //产出序号
	wrItemVO.setScddid(vos[i].getScddid()); //生产订单id
	wrItemVO.setScddh(vos[i].getScddh()); //生产订单号
	wrItemVO.setWlbmid(moHeader.getWlbmid()); //物料id
	wrItemVO.setPk_produce(moHeader.getPk_produce());
	wrItemVO.setPch(moHeader.getPch()); //批次号
	wrItemVO.setElementcheckflag(new UFBoolean(true)); //成分检验
	wrItemVO.setApplycheckdate(vos[0].getZdy30());
	wrItemVO.setGzzxid(vos[i].getGzzxid()); //工作中心
	//
	wrVOs[i] = new WrVO();
	wrVOs[i].setParentVO(wrHeaderVO);
	wrVOs[i].setChildrenVO(new WrItemVO[]{wrItemVO});

      }
      return wrVOs;
    } catch(Exception e){
      e.printStackTrace();
      throw new MMBusinessException("" , e);
    }

  }

  /**
   * fixed by hoofi 2005-02-05
   * @param pk_ddccxxid
   * @param userid
   * @return
   * @throws MMBusinessException
   */
  public WrVO[] buildElementCheckVO(String pk_ddccxxid , String userid) throws MMBusinessException{
    try{
      if(pk_ddccxxid == null || pk_ddccxxid.trim().length() == 0){
	return null;
      }
//			throw new BusinessException("传入参数为空pk_ddccxxid，userid");
      YieldInfoVO vo = (new YieldInfoDMO()).findByPrimaryKey(pk_ddccxxid);
      return convertYield2Wr(new YieldInfoVO[]{vo} , userid);

    } catch(Exception e){
      e.printStackTrace();
      throw new MMBusinessException("buildElementCheckVO wrong" , e);
    }

  }
public String isAlreadyCompCheck(String ccxhid)
throws MMBusinessException{
    try{
    	String errorccxh=new String(); 
    	
    	
        IPlatformlog qcPlatformlog = (IPlatformlog)NCLocator.getInstance().lookup(IPlatformlog.class.getName());
    	return errorccxh = qcPlatformlog.isAlreadyCompCheck(ccxhid);
   
} catch(Exception e){
    e.printStackTrace();
    if(e instanceof MMBusinessException){
      throw (MMBusinessException)e;
    }
    throw new MMBusinessException("" , e);
  }
}
  public void applyCheck(UFDateTime date , YieldInfoVO[] vos , String strCheckTypeID , String strUserID , String corp
      , String calbody) throws MMBusinessException{
    try{
      YieldInfoDMO infodmo = new YieldInfoDMO();
//      //获得物料属性，检验该物料是否成分报检
//      String[] invcode = new String[vos.length];
//
//      for(int i = 0;i < vos.length;i++){
//	invcode[i] = vos[i].getWlbm();
//
//      }
//      ;
//      boolean[] ischeck = infodmo.getIscheck(invcode , corp , calbody);
//      StringBuffer error = new StringBuffer("");
//
//      for(int i = 0;i < vos.length;i++){
//	if(!ischeck[i]){
//
//	  if(!isNull(vos[i].getCcxh())){
//
//	    error.append(vos[i].getCcxh().toString()).append(",");
//	  }
//	}
//      }
//      ;
//
//      if(error.length() != 0){
//	error.insert(0 , "{");
//	error.insert(0 , nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030" , "UPP50203030-000067") /*"产出序号*/);
//	error.delete(error.length() - 1 , error.length());
//	error.append("}");
//	throw new BusinessException(error + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030"
//	    , "UPP50203030-000068"));
//
//      }
//      ;

      //获得产出序号，判断是否重复报检
//      String[] ccxh = new String[vos.length];
//
//      StringBuffer errorend = new StringBuffer("");
//      for(int i = 0;i < vos.length;i++){
//	ccxh[i] = vos[i].getCcxh();
//	StringBuffer errortemp = new StringBuffer("");
//	String errorccxh = "";
////		       if(new String(new nc.bs.qc.log.PlatformlogBO().isAlreadyCompCheck(ccxh[i]))==null)
////		       		errorccxh="";
//
//	errorccxh = new nc.bs.qc.log.PlatformlogBO().isAlreadyCompCheck(ccxh[i]);
//
//	if(errorccxh != null && errorccxh.length() != 0){
//	  errortemp.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030" , "UPP50203030-000067") /*"产出序号"*/);
//
//	  errortemp.append(vos[i].getCcxh().toString());
//	  errortemp.append(errorccxh);
//	}
//	if(errortemp.length() != 0){
//	  errorend.append("\n").append(errortemp).append("\n");}
//
//      }
//      ;
//      if(errorend.length() != 0){
//	throw new BusinessException(errorend.toString());
//      }
      MoDMO dmo = new MoDMO();
      //提取所有的生产订单id
      HashMap moMap = new HashMap(vos.length);
      nc.vo.mm.pub.pub1030.MoHeaderVO moHeader;
      for(int i = 0;i < vos.length;i++){
	if(!moMap.containsKey(vos[i].getScddid())){
	  moHeader = dmo.findHeaderByPrimaryKey(vos[i].getScddid());
	  moMap.put(moHeader.getPk_moid() , moHeader);
	}
      }
      //查询物料是否批次管理
      String[] invcodes = new String[vos.length];
      for(int i = 0;i < invcodes.length;i++){
	invcodes[i] = vos[i].getWlbm();
      }
      String pkcorp = vos[0].getPk_corp();
      boolean[] bBatchManageFlags = (new YieldInfoDMO()).getBatchManageFlags(invcodes , pkcorp);
      //转化成完工报告VO数组
      WrVO[] wrVOs = new WrVO[vos.length];
      for(int i = 0;i < vos.length;i++){
	moHeader = (nc.vo.mm.pub.pub1030.MoHeaderVO)moMap.get(vos[i].getScddid());
	//检查批次管理的物料是否有批次号
	if(bBatchManageFlags[i] && isNull(moHeader.getPch())){
	  throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030" , "UPP50203030-000002" , null
	      , new String[]{vos[i].getWlbm()}) /*@res""物料 {""*/ + vos[i].getWlbm()
	      + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030"
	      , "UPP50203030-000003") /*@res""} 对应的订单没有批次号，报检操作取消!""*/);
	}
	//表头
	WrHeaderVO wrHeaderVO = new WrHeaderVO();
	wrHeaderVO.setPk_corp(vos[i].getPk_corp());
	wrHeaderVO.setScbmid(moHeader.getScbmid()); //报检部门
//				wrHeaderVO.setGzzxbmid(vos[i].getGzzxid()); //工作中心
	wrHeaderVO.setUserid(strUserID);
	wrHeaderVO.setGcbm(vos[i].getGcbm());//工厂编码
	wrHeaderVO.setPk_wrid(moHeader.getPk_moid());//生产订单ID
	//  djh对应质检单的完工报告号，现屏蔽 wrHeaderVO.setDjh(moHeader.getScddh());//生产订单号
	//表体
	WrItemVO wrItemVO = new WrItemVO();
	wrItemVO.setPk_corp(vos[i].getPk_corp());
	wrItemVO.setGcbm(vos[i].getGcbm());//工厂编码
	wrItemVO.setPk_wr_bid(vos[i].getPk_ddccxxid()); //产出信息行id
	wrItemVO.setCcxh(vos[i].getCcxh()); //产出序号
	wrItemVO.setScddid(vos[i].getScddid()); //生产订单id
	wrItemVO.setScddh(vos[i].getScddh()); //生产订单号
	wrItemVO.setWlbmid(moHeader.getWlbmid()); //物料id
	wrItemVO.setPk_produce(moHeader.getPk_produce());
	wrItemVO.setPch(moHeader.getPch()); //批次号
	wrItemVO.setElementcheckflag(new UFBoolean(true)); //成分检验
	wrItemVO.setApplycheckdate(vos[0].getZdy30());
	wrItemVO.setGzzxid(vos[i].getGzzxid()); //工作中心
	//
	wrVOs[i] = new WrVO();
	wrVOs[i].setParentVO(wrHeaderVO);
	wrVOs[i].setChildrenVO(new WrItemVO[]{wrItemVO});
      }
      //调用保存方法
//      PlatformlogBO qcBO = new PlatformlogBO();
      IPlatformlog qcPlatformlog = (IPlatformlog)NCLocator.getInstance().lookup(IPlatformlog.class.getName());
      /**
       * @todo 需要调整接口 2005-02-05 hoofi
       */
      String strError = qcPlatformlog.generateCheckbillFrmMM(new UFBoolean("true") , strCheckTypeID , "A2" , wrVOs , date);
//			String strError = qcBO.generateCheckbillDirectly(strCheckTypeID, "A4", wrVOs);
      if(strError != null && strError.trim().length() > 0){
	throw new BusinessException(strError);
      }
    } catch(Exception e){
      e.printStackTrace();
      if(e instanceof MMBusinessException){
        throw (MMBusinessException)e;
      }
      throw new MMBusinessException("" , e);
    }
  }

  /**
   * @param string
   * @return
   * created on 2004-9-2
   */
  private boolean isNull(String str){
    return str == null || str.trim().length() == 0;
  }

  /**
   * 根据生产订单删除产出信息
   * @param strMOIDs
   * @throws MMBusinessException
   * created on 2004-7-1
   */
  public void deleteByMOArray(String[] strMOIDs) throws MMBusinessException{
    if(strMOIDs == null || strMOIDs.length == 0){
      return;
    }
    try{
      YieldInfoVO[] vos = (new YieldInfoDMO()).queryByMOArray(strMOIDs);
      if(vos == null || vos.length == 0){
	return;
      }
      StringBuffer errorccxh=new StringBuffer("");
      for(int i = 0;i < vos.length;i++){
      	 String ccxhid=vos[i].getPk_ddccxxid();
	       StringBuffer error=new StringBuffer("");
	       String errorcomp=null;
	       //add by zhoujun 2011-8-23 宝钢金属NC 取消投放时发生错误，由于qc模块未安装造成无法找到实现类
	    	boolean isQcEnable = ((ICreateCorpQueryService) NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName())).isEnabled(vos[i].getPk_corp(), "C0");
	       if (isQcEnable) {
	    	   errorcomp=isAlreadyCompCheck(ccxhid);
	       }
	       //end
	    if(!isNull(errorcomp)&&errorcomp.length()>0)
	    {
          error.append(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("50203030"
        	    , "UPP50203030-000032")//生产订单号
          		).append(vos[i].getScddh()).append(errorcomp);
         }
	  if(error.length()>0)
	  	errorccxh.append(System.getProperty("line.separator")).append(error.toString());
      }
      if(errorccxh.length()>0)
      throw new BusinessException(errorccxh.toString());
 
      
      
//      String ccxhid=deletevo.getPk_ddccxxid();
//	   String errorccxh=null;
//	   if(!isNull(ccxhid)&&ccxhid.length()>0)
//	   {
//	    errorccxh=YieldInfoBO_Client.isAlreadyCompCheck(ccxhid);
//	   	};
//	  
//	
//	   if(!isNull(errorccxh)&&errorccxh.length()>0)
//	   {	toftp.showWarningMessage(errorccxh/*@res""该订单信息已经报检，不能删除""*/);
//		return;};

//      //检查引用
//      ReferenceManagerDMO refDMO = new ReferenceManagerDMO();
//      for(int i = 0;i < vos.length;i++){
//	refDMO.isReferenced("mm_ddccxx" , vos[i].getCcxh());
//      }
      //删除
      (new YieldInfoDMO()).deleteByMOArray(strMOIDs);
    } catch(Exception ex){
      ex.printStackTrace();
      throw new MMBusinessException(NCLangResOnserver.getInstance().getStrByID("50203030"
	  , "UPP50203030-000004") /*@res""删除时发生异常:""*/ , ex);
    }
  }
}
