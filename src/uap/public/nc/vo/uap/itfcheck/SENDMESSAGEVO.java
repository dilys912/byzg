package nc.vo.uap.itfcheck;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 * ��������:2019-08-15 15:56:05
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class SENDMESSAGEVO extends SuperVO {
	private String pk_send_message;
	private String message_code;
	private String message_body;
	private Integer message_state;//0==�ɹ�   -1=ʧ��
	private String message_group_name;
	private UFDouble message_operate_version;
	private String message_operate_info;
	private String pk_corp;
	private UFDateTime ts;
	private Integer dr;
	
	//add by zwx 2019-11-17 �����ֶ�
	private String erp_code;//erp���ݺ�
	private String bc_code;//��Ƶ��ݺ�
	public static final String ERP_CODE = "erp_code";
	public static final String BC_CODE = "bc_code";
	

	public static final String PK_SEND_MESSAGE = "pk_send_message";
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
	 * ����pk_send_message��Getter����.
	 * ��������:2019-08-15 15:56:05
	 * @return String
	 */
	public String getPk_send_message () {
		return pk_send_message;
	}   
	/**
	 * ����pk_send_message��Setter����.
	 * ��������:2019-08-15 15:56:05
	 * @param newPk_send_message String
	 */
	public void setPk_send_message (String newPk_send_message ) {
	 	this.pk_send_message = newPk_send_message;
	} 	  
	/**
	 * ����message_code��Getter����.
	 * ��������:2019-08-15 15:56:05
	 * @return String
	 */
	public String getMessage_code () {
		return message_code;
	}   
	/**
	 * ����message_code��Setter����.
	 * ��������:2019-08-15 15:56:05
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
	
	//add by zwx 2019-11-17 �����ֶ�
	public String getErp_code() {
		return erp_code;
	}
	public void setErp_code(String erp_code) {
		this.erp_code = erp_code;
	}
	public String getBc_code() {
		return bc_code;
	}
	public void setBc_code(String bc_code) {
		this.bc_code = bc_code;
	}
	
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2019-08-15 15:56:05
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2019-08-15 15:56:05
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_send_message";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2019-08-15 15:56:05
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "ITF_SEND_MESSAGE";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2019-08-15 15:56:05
	  */
     public SENDMESSAGEVO() {
		super();	
	}    
} 
