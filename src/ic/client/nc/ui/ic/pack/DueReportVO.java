package nc.ui.ic.pack;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

@SuppressWarnings("serial")
public class DueReportVO extends SuperVO {
//custname,sum(numtao) numtao,sum(tuopan) tuopan,sum(dinggai) dinggai,sum(dianguangzhi) dianguangzhi 
	public String pk_cubasdoc;//客商基本主键
	public String custname;//客商名称
	public UFDouble vdef0;//累计发出(套)
	public UFDouble vdef1;//托盘
	public UFDouble vdef2;//顶盖
	public UFDouble vdef3;//垫罐纸
	public UFDouble vdef4;
	public UFDouble vdef5;
	public UFDouble vdef6;
	public String vdef7;
	public String vdef8;
	public String vdef9;
	public UFDouble vdef10;
	public UFDouble vdef11;
	public UFDouble vdef12;
	public UFDouble vdef13;
	public UFDouble vdef14;
	
	
	public UFDouble getVdef0() {
		return vdef0;
	}
	public void setVdef0(UFDouble vdef0) {
		this.vdef0 = vdef0;
	}
	public UFDouble getVdef1() {
		return vdef1;
	}
	public void setVdef1(UFDouble vdef1) {
		this.vdef1 = vdef1;
	}
	public UFDouble getVdef2() {
		return vdef2;
	}
	public void setVdef2(UFDouble vdef2) {
		this.vdef2 = vdef2;
	}
	public UFDouble getVdef3() {
		return vdef3;
	}
	public void setVdef3(UFDouble vdef3) {
		this.vdef3 = vdef3;
	}
	public UFDouble getVdef4() {
		return vdef4;
	}
	public void setVdef4(UFDouble vdef4) {
		this.vdef4 = vdef4;
	}
	public UFDouble getVdef5() {
		return vdef5;
	}
	public void setVdef5(UFDouble vdef5) {
		this.vdef5 = vdef5;
	}
	public UFDouble getVdef6() {
		return vdef6;
	}
	public void setVdef6(UFDouble vdef6) {
		this.vdef6 = vdef6;
	}
	public String getVdef7() {
		return vdef7;
	}
	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}
	public String getVdef8() {
		return vdef8;
	}
	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}
	public String getVdef9() {
		return vdef9;
	}
	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	public UFDouble getVdef10() {
		return vdef10;
	}
	public void setVdef10(UFDouble vdef10) {
		this.vdef10 = vdef10;
	}
	public UFDouble getVdef11() {
		return vdef11;
	}
	public void setVdef11(UFDouble vdef11) {
		this.vdef11 = vdef11;
	}
	public UFDouble getVdef12() {
		return vdef12;
	}
	public void setVdef12(UFDouble vdef12) {
		this.vdef12 = vdef12;
	}
	public UFDouble getVdef13() {
		return vdef13;
	}
	public void setVdef13(UFDouble vdef13) {
		this.vdef13 = vdef13;
	}
	public UFDouble getVdef14() {
		return vdef14;
	}
	public void setVdef14(UFDouble vdef14) {
		this.vdef14 = vdef14;
	}
	public String getPk_cubasdoc() {
		return pk_cubasdoc;
	}
	public void setPk_cubasdoc(String pk_cubasdoc) {
		this.pk_cubasdoc = pk_cubasdoc;
	}
	public String getCustname() {
		return custname;
	}
	public void setCustname(String custname) {
		this.custname = custname;
	}
	
	
	

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_corp";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_corp";
	}
	
	public DueReportVO() {
		super();	
	}

}
