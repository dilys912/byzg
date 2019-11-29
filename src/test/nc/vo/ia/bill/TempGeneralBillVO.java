package nc.vo.ia.bill;

import nc.vo.pub.SuperVO;

public class TempGeneralBillVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String querykey;
	private String vbillcode;
	private String cbilltypecode;
	private String pk_corp;
	private String cbillid;
	private String cdeptid;
	private String cemployeeid;
	private String coperatorid;
	private String cdispatchid;
	private String ccustomvendorid;
	private String cbill_bid;
	private Double nmoney;
	private Double nnumber;
	private String cinventoryid;
	private Double irownumber;
	private String ts;
	private String dr;
	private String bestimateflag;

	
	public String getBestimateflag() {
		return bestimateflag;
	}

	public void setBestimateflag(String bestimateflag) {
		this.bestimateflag = bestimateflag;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getDr() {
		return dr;
	}

	public void setDr(String dr) {
		this.dr = dr;
	}

	public String getQuerykey() {
		return querykey;
	}

	public void setQuerykey(String querykey) {
		this.querykey = querykey;
	}

	public String getVbillcode() {
		return vbillcode;
	}

	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}

	public String getCbilltypecode() {
		return cbilltypecode;
	}

	public void setCbilltypecode(String cbilltypecode) {
		this.cbilltypecode = cbilltypecode;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCbillid() {
		return cbillid;
	}

	public void setCbillid(String cbillid) {
		this.cbillid = cbillid;
	}

	public String getCdeptid() {
		return cdeptid;
	}

	public void setCdeptid(String cdeptid) {
		this.cdeptid = cdeptid;
	}

	public String getCemployeeid() {
		return cemployeeid;
	}

	public void setCemployeeid(String cemployeeid) {
		this.cemployeeid = cemployeeid;
	}

	public String getCoperatorid() {
		return coperatorid;
	}

	public void setCoperatorid(String coperatorid) {
		this.coperatorid = coperatorid;
	}

	public String getCdispatchid() {
		return cdispatchid;
	}

	public void setCdispatchid(String cdispatchid) {
		this.cdispatchid = cdispatchid;
	}

	public String getCcustomvendorid() {
		return ccustomvendorid;
	}

	public void setCcustomvendorid(String ccustomvendorid) {
		this.ccustomvendorid = ccustomvendorid;
	}

	public String getCbill_bid() {
		return cbill_bid;
	}

	public void setCbill_bid(String cbill_bid) {
		this.cbill_bid = cbill_bid;
	}

	public Double getNmoney() {
		return nmoney;
	}

	public void setNmoney(Double nmoney) {
		this.nmoney = nmoney;
	}

	public Double getNnumber() {
		return nnumber;
	}

	public void setNnumber(Double nnumber) {
		this.nnumber = nnumber;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public Double getIrownumber() {
		return irownumber;
	}

	public void setIrownumber(Double irownumber) {
		this.irownumber = irownumber;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "generalmiddle";
	}

}
