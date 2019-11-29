package nc.vo.ar.m20060301;

import nc.vo.pub.SuperVO;

public class ITFSNVO extends SuperVO{
private String PK_ITF_SERIAL;
private String PK_CORP;
private String DR;
private String TS;
private String BILLNO;
private String SERIALNUM;
private String SERIALNO;
private String BILLTYPE;
private String ISBC;
private String DEF1;
private String DEF2;
private String DEF3;
private String DEF4;
private String DEF5;
private String DEF6;
private String DEF7;
private String DEF8;
public String getPK_ITF_SERIAL() {
	return PK_ITF_SERIAL;
}
public void setPK_ITF_SERIAL(String pk_itf_serial) {
	PK_ITF_SERIAL = pk_itf_serial;
}
public String getPK_CORP() {
	return PK_CORP;
}
public void setPK_CORP(String pk_corp) {
	PK_CORP = pk_corp;
}
public String getDR() {
	return DR;
}
public void setDR(String dr) {
	DR = dr;
}
public String getTS() {
	return TS;
}
public void setTS(String ts) {
	TS = ts;
}
public String getBILLNO() {
	return BILLNO;
}
public void setBILLNO(String billno) {
	BILLNO = billno;
}
public String getSERIALNUM() {
	return SERIALNUM;
}
public void setSERIALNUM(String serialnum) {
	SERIALNUM = serialnum;
}
public String getSERIALNO() {
	return SERIALNO;
}
public void setSERIALNO(String serialno) {
	SERIALNO = serialno;
}
public String getBILLTYPE() {
	return BILLTYPE;
}
public void setBILLTYPE(String billtype) {
	BILLTYPE = billtype;
}
public String getISBC() {
	return ISBC;
}
public void setISBC(String isbc) {
	ISBC = isbc;
}
public String getDEF1() {
	return DEF1;
}
public void setDEF1(String def1) {
	DEF1 = def1;
}
public String getDEF2() {
	return DEF2;
}
public void setDEF2(String def2) {
	DEF2 = def2;
}
public String getDEF3() {
	return DEF3;
}
public void setDEF3(String def3) {
	DEF3 = def3;
}
public String getDEF4() {
	return DEF4;
}
public void setDEF4(String def4) {
	DEF4 = def4;
}
public String getDEF5() {
	return DEF5;
}
public void setDEF5(String def5) {
	DEF5 = def5;
}
public String getDEF6() {
	return DEF6;
}
public void setDEF6(String def6) {
	DEF6 = def6;
}
public String getDEF7() {
	return DEF7;
}
public void setDEF7(String def7) {
	DEF7 = def7;
}
public String getDEF8() {
	return DEF8;
}
public void setDEF8(String def8) {
	DEF8 = def8;
}
/**
 * 设置对象标识,用来唯一定位对象.
 * 
 * 创建日期:2012-7-30
 * 
 * @param newPk_glzb_b
 *            String
 */
public void setPrimaryKey(String newPK_ITF_SERIAL)
{

	PK_ITF_SERIAL = newPK_ITF_SERIAL;

}

public String getPKFieldName() {
	// TODO Auto-generated method stub
	return "PK_ITF_SERIAL ";
}

public String getParentPKFieldName() {
	// TODO Auto-generated method stub
	return null;
}


public String getTableName() {
	// TODO Auto-generated method stub
	return "itf_serial_numbers";
}
/**
 * 返回对象标识,用来唯一定位对象.
 * 
 * 创建日期:2012-7-30
 * 
 * @return String
 */
public String getPrimaryKey()
{

	return PK_ITF_SERIAL;

}

/**
 * 返回数值对象的显示名称.
 * 
 * 创建日期:2012-7-30
 * 
 * @return java.lang.String 返回数值对象的显示名称.
 */
public String getEntityName()
{

	return "itf_serial_numbers";

}


}
