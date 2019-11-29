package nc.vo.fp;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class FpHvo extends SuperVO{

	@Override
	public String getPKFieldName() {
		
		return "pk_qc";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "fp_qiancheng";
	}
	
	public  String pk_qc;
	public  String pk_billhid;
	public  String pk_billbid;
	public  String pk_corp;
	public  Integer dr;
	public  String ts;
	public  String qccode;
	public  UFDouble qcmoney;
	public  String remark;
	public  String def1;
	public  String def2;
	public  String def3;
	public  String def4;
	public  String def5;
	
	public String getPk_qc() {
		return pk_qc;
	}

	public void setPk_qc(String pk_qc) {
		this.pk_qc = pk_qc;
	}

	public String getPk_billhid() {
		return pk_billhid;
	}

	public void setPk_billhid(String pk_billhid) {
		this.pk_billhid = pk_billhid;
	}

	public String getPk_billbid() {
		return pk_billbid;
	}

	public void setPk_billbid(String pk_billbid) {
		this.pk_billbid = pk_billbid;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getQccode() {
		return qccode;
	}

	public void setQccode(String qccode) {
		this.qccode = qccode;
	}

	public UFDouble getQcmoney() {
		return qcmoney;
	}

	public void setQcmoney(UFDouble qcmoney) {
		this.qcmoney = qcmoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}
}
