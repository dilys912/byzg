package nc.vo.by.invapp.h0h005;
	
import nc.vo.pub.SuperVO;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2012-07-20 13:58:54
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class CargContrastdocVO extends SuperVO {
	private String pk_corp;
	private String ts;
	private Integer dr;
	private String memo;
	private String pk_cargcontrastdoc;
	private String pk_cargdoc;
	private String cscode;
	private String csname;
	private String oldcscode;
	private String oldcsname;
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	
 

	public String getPk_cargdoc() {
		return pk_cargdoc;
	}

	public void setPk_cargdoc(String pk_cargdoc) {
		this.pk_cargdoc = pk_cargdoc;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPk_cargcontrastdoc() {
		return pk_cargcontrastdoc;
	}

	public void setPk_cargcontrastdoc(String pk_cargcontrastdoc) {
		this.pk_cargcontrastdoc = pk_cargcontrastdoc;
	}

	public String getCscode() {
		return cscode;
	}

	public void setCscode(String cscode) {
		this.cscode = cscode;
	}

	public String getCsname() {
		return csname;
	}

	public void setCsname(String csname) {
		this.csname = csname;
	}

	public String getOldcscode() {
		return oldcscode;
	}

	public void setOldcscode(String oldcscode) {
		this.oldcscode = oldcscode;
	}

	public String getOldcsname() {
		return oldcsname;
	}

	public void setOldcsname(String oldcsname) {
		this.oldcsname = oldcsname;
	}

	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2012-07-20 13:58:54
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2012-07-20 13:58:54
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_cargcontrastdoc";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2012-07-20 13:58:54
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "bd_cargcontrastdoc";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2012-07-20 13:58:54
	  */
     public CargContrastdocVO() {
		super();	
	}    
} 
