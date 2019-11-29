package nc.vo.uap.itfcheck;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2019-08-15 15:56:16
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class RECEIVEMESSAGEVO extends SuperVO {
	private String pk_receive_message;
	private String message_code;
	private String message_body;
	private Integer message_state;//0==成功   -1=失败
	private String message_group_name;
	private UFDouble message_operate_version;
	private String message_operate_info;
	private String pk_corp;
	private UFDateTime ts;
	private Integer dr;

	public static final String PK_RECEIVE_MESSAGE = "pk_receive_message";
	public static final String MESSAGE_CODE = "message_code";
	public static final String MESSAGE_BODY = "message_body";
	public static final String MESSAGE_STATE = "message_state";
	public static final String MESSAGE_GROUP_NAME = "message_group_name";
	public static final String MESSAGE_OPERATE_VERSION = "message_operate_version";
	public static final String MESSAGE_OPERATE_INFO = "message_operate_info";
	public static final String PK_CORP = "pk_corp";
	public static final String TS = "ts";
	public static final String DR = "dr";		
	/**
	 * 属性pk_receive_message的Getter方法.
	 * 创建日期:2019-08-15 15:56:16
	 * @return String
	 */
	public String getPk_receive_message () {
		return pk_receive_message;
	}   
	/**
	 * 属性pk_receive_message的Setter方法.
	 * 创建日期:2019-08-15 15:56:16
	 * @param newPk_receive_message String
	 */
	public void setPk_receive_message (String newPk_receive_message ) {
	 	this.pk_receive_message = newPk_receive_message;
	} 	  
	/**
	 * 属性message_code的Getter方法.
	 * 创建日期:2019-08-15 15:56:16
	 * @return String
	 */
	public String getMessage_code () {
		return message_code;
	}   
	/**
	 * 属性message_code的Setter方法.
	 * 创建日期:2019-08-15 15:56:16
	 * @param newMessage_code String
	 */
	public void setMessage_code (String newMessage_code ) {
	 	this.message_code = newMessage_code;
	} 	  
	
	public String getMessage_body() {
		return message_body;
	}
	public void setMessage_body(String message_body) {
		this.message_body = message_body;
	}
	public Integer getMessage_state() {
		return message_state;
	}
	public void setMessage_state(Integer message_state) {
		this.message_state = message_state;
	}
	public String getMessage_group_name() {
		return message_group_name;
	}
	public void setMessage_group_name(String message_group_name) {
		this.message_group_name = message_group_name;
	}
	public UFDouble getMessage_operate_version() {
		return message_operate_version;
	}
	public void setMessage_operate_version(UFDouble message_operate_version) {
		this.message_operate_version = message_operate_version;
	}
	public String getMessage_operate_info() {
		return message_operate_info;
	}
	public void setMessage_operate_info(String message_operate_info) {
		this.message_operate_info = message_operate_info;
	}
	public String getPk_corp() {
		return pk_corp;
	}
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2019-08-15 15:56:16
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2019-08-15 15:56:16
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_receive_message";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2019-08-15 15:56:16
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "ITF_RECEIVE_MESSAGE";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2019-08-15 15:56:16
	  */
     public RECEIVEMESSAGEVO() {
		super();	
	}    
} 
