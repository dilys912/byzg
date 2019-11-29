package nc.vo.by.invapp.h0h002;
	
import nc.vo.pub.*;
import nc.vo.pub.lang.*;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 * ��������:2014-07-08 21:31:03
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class DCONVERTVO extends SuperVO {
	private String pk_convert;
	private UFBoolean isstorebyconvert;
	private UFDateTime ts;
	private String pk_measdoc;
	private String pk_invbasdoc;
	private String pk_measdoc_stor;
	private UFDouble mainmeasrate;
	private String pk_measdoc_sub;
	private UFBoolean fixedflag;
	private UFDouble showorder;
	private UFDouble dr;

	public static final String PK_CONVERT = "pk_convert";
	public static final String ISSTOREBYCONVERT = "isstorebyconvert";
	public static final String PK_MEASDOC = "pk_measdoc";
	public static final String PK_INVBASDOC = "pk_invbasdoc";
	public static final String PK_MEASDOC_STOR = "pk_measdoc_stor";
	public static final String MAINMEASRATE = "mainmeasrate";
	public static final String PK_MEASDOC_SUB = "pk_measdoc_sub";
	public static final String FIXEDFLAG = "fixedflag";
	public static final String SHOWORDER = "showorder";
			
	/**
	 * ����pk_convert��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return String
	 */
	public String getPk_convert () {
		return pk_convert;
	}   
	/**
	 * ����pk_convert��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newPk_convert String
	 */
	public void setPk_convert (String newPk_convert ) {
	 	this.pk_convert = newPk_convert;
	} 	  
	/**
	 * ����isstorebyconvert��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return UFBoolean
	 */
	public UFBoolean getIsstorebyconvert () {
		return isstorebyconvert;
	}   
	/**
	 * ����isstorebyconvert��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newIsstorebyconvert UFBoolean
	 */
	public void setIsstorebyconvert (UFBoolean newIsstorebyconvert ) {
	 	this.isstorebyconvert = newIsstorebyconvert;
	} 	  
	/**
	 * ����ts��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return UFDateTime
	 */
	public UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newTs UFDateTime
	 */
	public void setTs (UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
	/**
	 * ����pk_measdoc��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return String
	 */
	public String getPk_measdoc () {
		return pk_measdoc;
	}   
	/**
	 * ����pk_measdoc��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newPk_measdoc String
	 */
	public void setPk_measdoc (String newPk_measdoc ) {
	 	this.pk_measdoc = newPk_measdoc;
	} 	  
	/**
	 * ����pk_invbasdoc��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return String
	 */
	public String getPk_invbasdoc () {
		return pk_invbasdoc;
	}   
	/**
	 * ����pk_invbasdoc��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newPk_invbasdoc String
	 */
	public void setPk_invbasdoc (String newPk_invbasdoc ) {
	 	this.pk_invbasdoc = newPk_invbasdoc;
	} 	  
	/**
	 * ����pk_measdoc_stor��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return String
	 */
	public String getPk_measdoc_stor () {
		return pk_measdoc_stor;
	}   
	/**
	 * ����pk_measdoc_stor��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newPk_measdoc_stor String
	 */
	public void setPk_measdoc_stor (String newPk_measdoc_stor ) {
	 	this.pk_measdoc_stor = newPk_measdoc_stor;
	} 	  
	/**
	 * ����mainmeasrate��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return UFDouble
	 */
	public UFDouble getMainmeasrate () {
		return mainmeasrate;
	}   
	/**
	 * ����mainmeasrate��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newMainmeasrate UFDouble
	 */
	public void setMainmeasrate (UFDouble newMainmeasrate ) {
	 	this.mainmeasrate = newMainmeasrate;
	} 	  
	/**
	 * ����pk_measdoc_sub��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return String
	 */
	public String getPk_measdoc_sub () {
		return pk_measdoc_sub;
	}   
	/**
	 * ����pk_measdoc_sub��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newPk_measdoc_sub String
	 */
	public void setPk_measdoc_sub (String newPk_measdoc_sub ) {
	 	this.pk_measdoc_sub = newPk_measdoc_sub;
	} 	  
	/**
	 * ����fixedflag��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return UFBoolean
	 */
	public UFBoolean getFixedflag () {
		return fixedflag;
	}   
	/**
	 * ����fixedflag��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newFixedflag UFBoolean
	 */
	public void setFixedflag (UFBoolean newFixedflag ) {
	 	this.fixedflag = newFixedflag;
	} 	  
	/**
	 * ����showorder��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return UFDouble
	 */
	public UFDouble getShoworder () {
		return showorder;
	}   
	/**
	 * ����showorder��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newShoworder UFDouble
	 */
	public void setShoworder (UFDouble newShoworder ) {
	 	this.showorder = newShoworder;
	} 	  
	/**
	 * ����dr��Getter����.
	 * ��������:2014-07-08 21:31:03
	 * @return UFDouble
	 */
	public UFDouble getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.
	 * ��������:2014-07-08 21:31:03
	 * @param newDr UFDouble
	 */
	public void setDr (UFDouble newDr ) {
	 	this.dr = newDr;
	} 	  
 
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2014-07-08 21:31:03
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2014-07-08 21:31:03
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_convert";
	}
    
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2014-07-08 21:31:03
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "bd_convert";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2014-07-08 21:31:03
	  */
     public DCONVERTVO() {
		super();	
	}    
} 
