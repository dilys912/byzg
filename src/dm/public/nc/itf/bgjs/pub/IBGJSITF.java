package nc.itf.bgjs.pub;

import java.util.ArrayList;
import java.util.HashMap;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public interface IBGJSITF {
      public Object getObjValue(Object obj) throws BusinessException;
      
      
      public ArrayList getVOsfromSql(String sql,String voname) throws BusinessException;
      

  	// ����VO�����PK
  	public void insertVOsWithPK(Object[] objs) throws BusinessException ;

  	// ����VO���鲻��PK
  	public void insertVOsArr(Object[] objs) throws BusinessException ;

  	// ɾ��VO����
  	public void deleteVOsArr(Object[] objs) throws BusinessException ;

  	// ɾ��VO
  	public void deleteVO(Object obj) throws BusinessException ;

  	// ����VO����
  	public void updateVOsArr(Object[] objs) throws BusinessException ;

  	// ����VO
  	public void updateVO(Object obj) throws BusinessException ;

  	// ����VO����PK
  	public String insertVObackPK(Object obj) throws BusinessException ;

  	// ִ��update���
  	public void updateBYsql(String sql) throws BusinessException ;

  	//��ȡ�᰸����
  	public ArrayList getJieAnData(HashMap hminfo) throws BusinessException ;
  	
  	//�᰸
  	public ArrayList doJieAn(ArrayList list) throws BusinessException ;
  	
  	//ȡ���᰸
  	public ArrayList doUNJieAn(ArrayList list) throws BusinessException ;
  	
}
