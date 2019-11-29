package nc.vo.uap.xbusreceive;

import nc.vo.pub.SuperVO;

public class SFKMessageVO extends SuperVO{
    String ts;
    String dr;
    String pk_corp;
    String bcBillno;
    String erpBillno;
    String message;
    String def1;
    String def2;
    String def3;
    String def4;
    String def5;
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_SFKMesage";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_SFKMesage";
	}

	public String getTs() {
		return ts;
	}

	
	public String getDr() {
		return dr;
	}

	public void setDr(String dr) {
		this.dr = dr;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getBcBillno() {
		return bcBillno;
	}

	public void setBcBillno(String bcBillno) {
		this.bcBillno = bcBillno;
	}

	public String getErpBillno() {
		return erpBillno;
	}

	public void setErpBillno(String erpBillno) {
		this.erpBillno = erpBillno;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
