package nc.vo.by.invapp.h0h002;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
/**
 * 
 * 
 * 创建日期:(2005-6-6)
 * @author:
 */
@SuppressWarnings("serial")
public class ConvertVO extends SuperVO {

	private String pk_convert;
	private Integer dr;
	private UFBoolean fixedflag;
	private UFDouble mainmeasrate;
	private String pk_invappdoc;
	private String pk_invbasdoc;
	private String pk_measdoc;
	private String pk_measdoc_stor;
	private String pk_measdoc_sub;
	private Integer showorder;
	private String ts;
	private UFBoolean isstorebyconvert;

    public String getPk_invappdoc() {
		return pk_invappdoc;
	}
	public void setPk_invappdoc(String pk_invappdoc) {
		this.pk_invappdoc = pk_invappdoc;
	}
    public String getPk_convert() {
		return pk_convert;
	}
	public void setPk_convert(String pk_convert) {
		this.pk_convert = pk_convert;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public UFBoolean getFixedflag() {
		return fixedflag;
	}
	public void setFixedflag(UFBoolean fixedflag) {
		this.fixedflag = fixedflag;
	}
	public UFDouble getMainmeasrate() {
		return mainmeasrate;
	}
	public void setMainmeasrate(UFDouble mainmeasrate) {
		this.mainmeasrate = mainmeasrate;
	}
	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}
	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}
	public String getPk_measdoc() {
		return pk_measdoc;
	}
	public void setPk_measdoc(String pk_measdoc) {
		this.pk_measdoc = pk_measdoc;
	}
	public String getPk_measdoc_stor() {
		return pk_measdoc_stor;
	}
	public void setPk_measdoc_stor(String pk_measdoc_stor) {
		this.pk_measdoc_stor = pk_measdoc_stor;
	}
	public String getPk_measdoc_sub() {
		return pk_measdoc_sub;
	}
	public void setPk_measdoc_sub(String pk_measdoc_sub) {
		this.pk_measdoc_sub = pk_measdoc_sub;
	}
	public Integer getShoworder() {
		return showorder;
	}
	public void setShoworder(Integer showorder) {
		this.showorder = showorder;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public UFBoolean getIsstorebyconvert() {
		return isstorebyconvert;
	}
	public void setIsstorebyconvert(UFBoolean isstorebyconvert) {
		this.isstorebyconvert = isstorebyconvert;
	}
/**
 * <p>取得父VO主键字段.
 * <p>
 * 创建日期:(2005-6-6)
 * @return java.lang.String
 */
public java.lang.String getParentPKFieldName() {

	return  "pk_invappdoc";
}
/**
 * <p>取得表主键.
 * <p>
 * 创建日期:(2005-6-6)
 * @return java.lang.String
 */
public java.lang.String getPKFieldName() {

	return  "pk_convert";
}
/**
 * <p>返回表名称.
 * <p>
 * 创建日期:(2005-6-6)
 * @return java.lang.String
 */

public java.lang.String getTableName() {

	return "bd_convert_app";
}
}