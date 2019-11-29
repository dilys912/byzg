package nc.vo.bgzg.b0003;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class GZHGZVO extends SuperVO {

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	} 
	public String invcode;
	public String invname;
	public UFDouble num;
	public String djh;
	public String bgy;
	
	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}

	public String getInvname() {
		return invname;
	}

	public void setInvname(String invname) {
		this.invname = invname;
	}

	public UFDouble getNum() {
		return num;
	}

	public void setNum(UFDouble num) {
		this.num = num;
	}

	public String getDjh() {
		return djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getBgy() {
		return bgy;
	}

	public void setBgy(String bgy) {
		this.bgy = bgy;
	} 
	

	

}
