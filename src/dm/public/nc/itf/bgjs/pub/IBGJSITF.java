package nc.itf.bgjs.pub;

import java.util.ArrayList;
import java.util.HashMap;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public interface IBGJSITF {
      public Object getObjValue(Object obj) throws BusinessException;
      
      
      public ArrayList getVOsfromSql(String sql,String voname) throws BusinessException;
      

  	// 保存VO数组带PK
  	public void insertVOsWithPK(Object[] objs) throws BusinessException ;

  	// 保存VO数组不带PK
  	public void insertVOsArr(Object[] objs) throws BusinessException ;

  	// 删除VO数组
  	public void deleteVOsArr(Object[] objs) throws BusinessException ;

  	// 删除VO
  	public void deleteVO(Object obj) throws BusinessException ;

  	// 更新VO数组
  	public void updateVOsArr(Object[] objs) throws BusinessException ;

  	// 更新VO
  	public void updateVO(Object obj) throws BusinessException ;

  	// 保存VO返回PK
  	public String insertVObackPK(Object obj) throws BusinessException ;

  	// 执行update语句
  	public void updateBYsql(String sql) throws BusinessException ;

  	//获取结案数据
  	public ArrayList getJieAnData(HashMap hminfo) throws BusinessException ;
  	
  	//结案
  	public ArrayList doJieAn(ArrayList list) throws BusinessException ;
  	
  	//取消结案
  	public ArrayList doUNJieAn(ArrayList list) throws BusinessException ;
  	
}
