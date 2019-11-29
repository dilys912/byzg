package nc.vo.baoyin.alert;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class DefdocVO extends SuperVO {
	
	public String pk_defdoc;
    public String pk_defdoc1;
    public String doccode;
    public String docname;
    public String docfathercode;
    public String docfathername;
    public Integer docsystype;
    public String pk_corp;
    public String pk_defdoclist;
    public UFBoolean sealflag;
    private Integer dr;
    

	public String getPk_defdoc() {
		return pk_defdoc;
	}

	public void setPk_defdoc(String pk_defdoc) {
		this.pk_defdoc = pk_defdoc;
	}

	public String getPk_defdoc1() {
		return pk_defdoc1;
	}

	public void setPk_defdoc1(String pk_defdoc1) {
		this.pk_defdoc1 = pk_defdoc1;
	}

	public String getDoccode() {
		return doccode;
	}

	public void setDoccode(String doccode) {
		this.doccode = doccode;
	}

	public String getDocname() {
		return docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	public String getDocfathercode() {
		return docfathercode;
	}

	public void setDocfathercode(String docfathercode) {
		this.docfathercode = docfathercode;
	}

	public String getDocfathername() {
		return docfathername;
	}

	public void setDocfathername(String docfathername) {
		this.docfathername = docfathername;
	}

	public Integer getDocsystype() {
		return docsystype;
	}

	public void setDocsystype(Integer docsystype) {
		this.docsystype = docsystype;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_defdoclist() {
		return pk_defdoclist;
	}

	public void setPk_defdoclist(String pk_defdoclist) {
		this.pk_defdoclist = pk_defdoclist;
	}

	public UFBoolean getSealflag() {
		return sealflag;
	}

	public void setSealflag(UFBoolean sealflag) {
		this.sealflag = sealflag;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_defdoc";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_defdoc";
	}

}
