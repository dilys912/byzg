package nc.vo.baoyin.alert;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class StordocVO extends SuperVO {
	
	 public String pk_stordoc;
	    public String pk_corp;
	    public String storcode;
	    public String storname;
	    public String storaddr;
	    public String principalcode;
	    public String phone;
	    public UFBoolean gubflag;
	    public UFBoolean proflag;
	    public UFBoolean mrpflag;
	    public UFBoolean csflag;
	    public UFBoolean sealflag;
	    public String def1;
	    public String def2;
	    public String def3;
	    public String def4;
	    public String def5;
	    public String memo;
	    public UFBoolean isatpaffected;
	    public UFBoolean iscalculatedinvcost;
	    public UFBoolean isCapitalStor;
	    public UFBoolean isdirectstore;
	    public UFBoolean isForeignStor;
	    public UFBoolean isGatherSettle;
	    public UFBoolean isstoreontheway;
	    public String pk_address;
	    public String pk_calbody;
	    
	    public String getPk_stordoc() {
			return pk_stordoc;
		}


		public void setPk_stordoc(String pk_stordoc) {
			this.pk_stordoc = pk_stordoc;
		}


		public String getPk_corp() {
			return pk_corp;
		}


		public void setPk_corp(String pk_corp) {
			this.pk_corp = pk_corp;
		}


		public String getStorcode() {
			return storcode;
		}


		public void setStorcode(String storcode) {
			this.storcode = storcode;
		}


		public String getStorname() {
			return storname;
		}


		public void setStorname(String storname) {
			this.storname = storname;
		}


		public String getStoraddr() {
			return storaddr;
		}


		public void setStoraddr(String storaddr) {
			this.storaddr = storaddr;
		}


		public String getPrincipalcode() {
			return principalcode;
		}


		public void setPrincipalcode(String principalcode) {
			this.principalcode = principalcode;
		}


		public String getPhone() {
			return phone;
		}


		public void setPhone(String phone) {
			this.phone = phone;
		}


		public UFBoolean getGubflag() {
			return gubflag;
		}


		public void setGubflag(UFBoolean gubflag) {
			this.gubflag = gubflag;
		}


		public UFBoolean getProflag() {
			return proflag;
		}


		public void setProflag(UFBoolean proflag) {
			this.proflag = proflag;
		}


		public UFBoolean getMrpflag() {
			return mrpflag;
		}


		public void setMrpflag(UFBoolean mrpflag) {
			this.mrpflag = mrpflag;
		}


		public UFBoolean getCsflag() {
			return csflag;
		}


		public void setCsflag(UFBoolean csflag) {
			this.csflag = csflag;
		}


		public UFBoolean getSealflag() {
			return sealflag;
		}


		public void setSealflag(UFBoolean sealflag) {
			this.sealflag = sealflag;
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


		public String getMemo() {
			return memo;
		}


		public void setMemo(String memo) {
			this.memo = memo;
		}


		public UFBoolean getIsatpaffected() {
			return isatpaffected;
		}


		public void setIsatpaffected(UFBoolean isatpaffected) {
			this.isatpaffected = isatpaffected;
		}


		public UFBoolean getIscalculatedinvcost() {
			return iscalculatedinvcost;
		}


		public void setIscalculatedinvcost(UFBoolean iscalculatedinvcost) {
			this.iscalculatedinvcost = iscalculatedinvcost;
		}


		public UFBoolean getIsCapitalStor() {
			return isCapitalStor;
		}


		public void setIsCapitalStor(UFBoolean isCapitalStor) {
			this.isCapitalStor = isCapitalStor;
		}


		public UFBoolean getIsdirectstore() {
			return isdirectstore;
		}


		public void setIsdirectstore(UFBoolean isdirectstore) {
			this.isdirectstore = isdirectstore;
		}


		public UFBoolean getIsForeignStor() {
			return isForeignStor;
		}


		public void setIsForeignStor(UFBoolean isForeignStor) {
			this.isForeignStor = isForeignStor;
		}


		public UFBoolean getIsGatherSettle() {
			return isGatherSettle;
		}


		public void setIsGatherSettle(UFBoolean isGatherSettle) {
			this.isGatherSettle = isGatherSettle;
		}


		public UFBoolean getIsstoreontheway() {
			return isstoreontheway;
		}


		public void setIsstoreontheway(UFBoolean isstoreontheway) {
			this.isstoreontheway = isstoreontheway;
		}


		public String getPk_address() {
			return pk_address;
		}


		public void setPk_address(String pk_address) {
			this.pk_address = pk_address;
		}


		public String getPk_calbody() {
			return pk_calbody;
		}


		public void setPk_calbody(String pk_calbody) {
			this.pk_calbody = pk_calbody;
		}


		

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_stordoc";
	}


	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_stordoc";
	}


	@Override
	public java.lang.String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

}
