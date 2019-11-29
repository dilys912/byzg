package nc.vo.ar.m20060301;

import nc.vo.pub.SuperVO;

public class ITFBZVO extends SuperVO {
	private String  pk_arap_bz;
	private String  billno_bz;
	private String  billno_yf;
	private String  vouchid;
	private String  ksbm_cl;
	private String  gys_account;
	private String  pk_corp;
	private String  vdef1;
	private String  vdef2;
	private String  vdef3;
	private String  vdef4;
	private String  vdef5;
	private String  vdef6;
	private String  vdef7;
	private String  dr;
	private String  ts;
	public String getPk_arap_bz() {
		return pk_arap_bz;
	}

	public void setPk_arap_bz(String pk_arap_bz) {
		this.pk_arap_bz = pk_arap_bz;
	}

	public String getBillno_bz() {
		return billno_bz;
	}

	public void setBillno_bz(String billno_bz) {
		this.billno_bz = billno_bz;
	}

	public String getBillno_yf() {
		return billno_yf;
	}

	public void setBillno_yf(String billno_yf) {
		this.billno_yf = billno_yf;
	}

	public String getVouchid() {
		return vouchid;
	}

	public void setVouchid(String vouchid) {
		this.vouchid = vouchid;
	}

	public String getKsbm_cl() {
		return ksbm_cl;
	}

	public void setKsbm_cl(String ksbm_cl) {
		this.ksbm_cl = ksbm_cl;
	}

	public String getGys_account() {
		return gys_account;
	}

	public void setGys_account(String gys_account) {
		this.gys_account = gys_account;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
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

	public String getVdef6() {
		return vdef6;
	}

	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}

	public String getVdef7() {
		return vdef7;
	}

	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}

	public String getDr() {
		return dr;
	}

	public void setDr(String dr) {
		this.dr = dr;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_arap_bz ";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "itf_arap_bz";
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

		return pk_arap_bz;

	}
 
	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2012-7-30
	 * 
	 * @param newPk_glzb_b
	 *            String
	 */
	public void setPrimaryKey(String newpk_arap_bz)
	{

		pk_arap_bz = newpk_arap_bz;

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

		return "itf_arap_bz";

	}
}
