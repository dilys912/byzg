package nc.vo.rino.pda;

import nc.vo.pub.SuperVO;

/**
 * @author Administrator
 * @date Feb 18, 2014 3:05:40 PM
 * @type nc.ws.baoyin.BasicdocVO
 * @corporation 上海锐鸟软件有限公司
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
@SuppressWarnings("serial")
public class BasicdocVO extends SuperVO {

	private String tid;
	private String bdtype;
	private String bdid;
	private String bdname;
	private String proctype;
	private String sysflag;
	private String handledevice;
	private String handletime;
	private String memo;
	private String pk_corp;
	private Integer dr;
	private String ts;
	private String def1; // 仓库类型
	private String def2; // 货位所在仓库
	private String def3;
	private String def4;
	private String def5;

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getPKFieldName() {
		return "tid";
	}

	@Override
	public String getTableName() {
		return "pda_basicdoc";
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getBdtype() {
		return bdtype;
	}

	public void setBdtype(String bdtype) {
		this.bdtype = bdtype;
	}

	public String getBdid() {
		return bdid;
	}

	public void setBdid(String bdid) {
		this.bdid = bdid;
	}

	public String getBdname() {
		return bdname;
	}

	public void setBdname(String bdname) {
		this.bdname = bdname;
	}

	public String getProctype() {
		return proctype;
	}

	public void setProctype(String proctype) {
		this.proctype = proctype;
	}

	public String getSysflag() {
		return sysflag;
	}

	public void setSysflag(String sysflag) {
		this.sysflag = sysflag;
	}

	public String getHandledevice() {
		return handledevice;
	}

	public void setHandledevice(String handledevice) {
		this.handledevice = handledevice;
	}

	public String getHandletime() {
		return handletime;
	}

	public void setHandletime(String handletime) {
		this.handletime = handletime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
