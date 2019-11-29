package nc.vo.sc.settleCltj;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2014-06-18 13:10:05
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class SettleCltjVO extends SuperVO {
 	private String cvendormangid;
 	private String cprocessmangid;
 	private String cmaterialmangid;
 	private String vbatch;
 	private String vfree1;
 	private String cvendorid;
 	private UFDouble num;

	public String getCvendormangid() {
		return cvendormangid;
	}

	public void setCvendormangid(String cvendormangid) {
		this.cvendormangid = cvendormangid;
	}


	public String getCprocessmangid() {
		return cprocessmangid;
	}

	public void setCprocessmangid(String cprocessmangid) {
		this.cprocessmangid = cprocessmangid;
	}

	public String getCmaterialmangid() {
		return cmaterialmangid;
	}

	public void setCmaterialmangid(String cmaterialmangid) {
		this.cmaterialmangid = cmaterialmangid;
	}

	public String getVbatch() {
		return vbatch;
	}

	public void setVbatch(String vbatch) {
		this.vbatch = vbatch;
	}

	public String getVfree1() {
		return vfree1;
	}

	public void setVfree1(String vfree1) {
		this.vfree1 = vfree1;
	}

	public String getCvendorid() {
		return cvendorid;
	}

	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}

	public UFDouble getNum() {
		return num;
	}

	public void setNum(UFDouble num) {
		this.num = num;
	}

	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2014-06-18 13:10:05
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
		return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2014-06-18 13:10:05
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2014-06-18 13:10:05
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2014-06-18 13:10:05
	  */
     public SettleCltjVO() {
		super();	
	}  
} 

