package nc.vo.ic.pub.lot;

import java.util.ArrayList;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.ic.pub.bill.SwitchObject;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.bill.FreeVO;

public class LotNumbRefVO extends CircularlyAccessibleValueObject {
	public String m_vbatchcode;
	public String m_corpid;
	public String m_cgeneralhid;
	public String m_cgeneralbid;
	public String m_cwarehouseid;
	public String m_cinventoryid;
	public String m_correspondhid;
	public String m_correspondbid;
	public UFDate m_dvalidate;
	public UFDouble m_noutnum;
	public UFDouble m_noutassistnum;
	public UFDouble m_ninnum;
	public UFDouble m_ninassistnum;
	public String m_cbilltypecode;
	public String m_vbillcode;
	public String m_castunitid;
	public FreeVO m_freevo;
	public Integer m_iOnhandnumType = new Integer(0);
	public String m_cqualitylevelid;
	public UFDouble m_hsl;
	public UFDouble m_ngrossnum;
	public BatchcodeVO m_batchcodevo = new BatchcodeVO();
	public String m_castunitname;
	public String m_cspaceid;//add by zwx 2014-12-26 添加 【货位】
	public String m_csname;//add by zwx 2014-12-26 货位名称

	public LotNumbRefVO() {
	}

	public LotNumbRefVO(String newVbatchcode) {
		this.m_vbatchcode = newVbatchcode;
	}

	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (Exception e) {
		}
		LotNumbRefVO lotNumbRef = (LotNumbRefVO) o;

		return lotNumbRef;
	}

	public String getEntityName() {
		return "LotNumbRef";
	}
	
	//add by zwx 2014-12-26 
	public String getCspaceid() {
		return this.m_cspaceid;
	}

	public void setCspaceid(String m_cspaceid) {
		this.m_cspaceid = m_cspaceid;
	}
	
	public String getCsname() {
		return this.m_csname;
	}

	public void setCsname(String m_csname) {
		this.m_csname = m_csname;
	}
	
	//end by zwx
	
	public String getPrimaryKey() {
		return this.m_vbatchcode;
	}

	public void setPrimaryKey(String newVbatchcode) {
		this.m_vbatchcode = newVbatchcode;
	}

	public String getVbatchcode() {
		return this.m_vbatchcode;
	}

	public String getCorpid() {
		return this.m_corpid;
	}

	public FreeVO getFreeVO() {
		return this.m_freevo;
	}

	public String getCgeneralhid() {
		return this.m_cgeneralhid;
	}

	public String getCgeneralbid() {
		return this.m_cgeneralbid;
	}

	public String getCwarehouseid() {
		return this.m_cwarehouseid;
	}

	public String getCinventoryid() {
		return this.m_cinventoryid;
	}

	public String getCorrespondhid() {
		return this.m_correspondhid;
	}

	public String getCorrespondbid() {
		return this.m_correspondbid;
	}

	public UFDouble getNinnum() {
		return this.m_ninnum;
	}

	public UFDate getDvalidate() {
		return this.m_dvalidate;
	}

	public String getCqualitylevelid() {
		return this.m_cqualitylevelid;
	}

	public void setCqualitylevelid(String newCqualitylevelid) {
		this.m_cqualitylevelid = newCqualitylevelid;
	}

	public UFDouble getNoutnum() {
		return this.m_noutnum;
	}

	public UFDouble getNoutassistnum() {
		return this.m_noutassistnum;
	}

	public UFDouble getNgrossnum() {
		return this.m_ngrossnum;
	}

	public UFDouble getHsl() {
		return this.m_hsl;
	}

	public UFDouble getNinassistnum() {
		return this.m_ninassistnum;
	}

	public String getCbilltypecode() {
		return this.m_cbilltypecode;
	}

	public String getVbillcode() {
		return this.m_vbillcode;
	}

	public String getCastunitid() {
		return this.m_castunitid;
	}

	public void setVbatchcode(String newVbatchcode) {
		this.m_vbatchcode = newVbatchcode;
	}

	public void setCorpid(String newCorpid) {
		this.m_corpid = newCorpid;
	}

	public void setFreeVO(FreeVO freevo) {
		if (this.m_freevo == null) {
			this.m_freevo = new FreeVO();
		}
		this.m_freevo.getAttributeNames();
		if (freevo != null)
			for (int i = 1; i < 11; i++) {
				this.m_freevo.setAttributeValue("vfreevalue" + i, freevo
						.getAttributeValue("vfreevalue" + i));
				this.m_freevo.setAttributeValue("vfreeid" + i, freevo
						.getAttributeValue("vfreeid" + i));
				this.m_freevo.setAttributeValue("vfreename" + i, freevo
						.getAttributeValue("vfreename" + i));
			}
	}

	public void setCgeneralhid(String newCgeneralhid) {
		this.m_cgeneralhid = newCgeneralhid;
	}

	public void setCgeneralbid(String newCgeneralbid) {
		this.m_cgeneralbid = newCgeneralbid;
	}

	public void setCwarehouseid(String newCwarehouseid) {
		this.m_cwarehouseid = newCwarehouseid;
	}

	public void setCinventoryid(String newCinventoryid) {
		this.m_cinventoryid = newCinventoryid;
	}

	public void setCorrespondhid(String newCorrespondhid) {
		this.m_correspondhid = newCorrespondhid;
	}

	public void setCorrespondbid(String newCorrespondbid) {
		this.m_correspondbid = newCorrespondbid;
	}

	public void setDvalidate(UFDate newDvalidate) {
		this.m_dvalidate = newDvalidate;
	}

	public void setNoutnum(UFDouble newNoutnum) {
		this.m_noutnum = newNoutnum;
	}

	public void setNoutassistnum(UFDouble newNoutassistnum) {
		this.m_noutassistnum = newNoutassistnum;
	}

	public void setNinnum(UFDouble newNinnum) {
		this.m_ninnum = newNinnum;
	}

	public void setHsl(UFDouble newHsl) {
		this.m_hsl = newHsl;
	}

	public void setNgrossnum(UFDouble newNgrossnum) {
		this.m_ngrossnum = newNgrossnum;
	}

	public void setNinassistnum(UFDouble newNinassistnum) {
		this.m_ninassistnum = newNinassistnum;
	}

	public void setCbilltypecode(String newCbilltypecode) {
		this.m_cbilltypecode = newCbilltypecode;
	}

	public void setVbillcode(String newVbillcode) {
		this.m_vbillcode = newVbillcode;
	}

	public void setCastunitid(String newCastunitid) {
		this.m_castunitid = newCastunitid;
	}

	public void validate() throws ValidationException {
		ArrayList errFields = new ArrayList();

		if (this.m_vbatchcode == null) {
			errFields.add(new String("m_vbatchcode"));
		}

		StringBuffer message = new StringBuffer();
		message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4008vo",
				"UPP4008vo-000020"));
		if (errFields.size() > 0) {
			String[] temp = (String[]) (String[]) errFields
					.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID(
						"SCMCOMMON", "UPPSCMCommon-000000"));
				message.append(temp[i]);
			}

			throw new NullFieldException(message.toString());
		}
	}

	public String[] getAttributeNames() {
		return new String[] { "ionhandnumtype", "corpid", "cgeneralhid",
				"cgeneralbid", "cwarehouseid", "cinventoryid", "correspondhid",
				"correspondbid", "dvalidate", "noutnum", "noutassistnum",
				"ninnum", "ninassistnum", "cbilltypecode", "vbillcode",
				"castunitid", "castunitname", "vfree0", "cqualitylevelid",
				"hsl", "ngrossnum", "onhandnumtype", "pk_invmandoc"
				,"cspaceid","csname"//add by shikun 货位
				};
	}

	public Object getAttributeValue(String attributeName) {
		if (attributeName.equals("vbatchcode")) {
			return this.m_vbatchcode;
		}
		if (attributeName.equals("corpid")) {
			return this.m_corpid;
		}
		if (attributeName.equals("cgeneralhid")) {
			return this.m_cgeneralhid;
		}
		if (attributeName.equals("cgeneralbid")) {
			return this.m_cgeneralbid;
		}
		if (attributeName.equals("cwarehouseid")) {
			return this.m_cwarehouseid;
		}
		if (attributeName.equals("cinventoryid")) {
			return this.m_cinventoryid;
		}
		if (attributeName.equals("correspondhid")) {
			return this.m_correspondhid;
		}
		if (attributeName.equals("correspondbid")) {
			return this.m_correspondbid;
		}
		if (attributeName.equals("dvalidate")) {
			return this.m_dvalidate;
		}
		if (attributeName.equals("noutnum")) {
			return this.m_noutnum;
		}
		if (attributeName.equals("noutassistnum")) {
			return this.m_noutassistnum;
		}
		if (attributeName.equals("ninnum")) {
			return this.m_ninnum;
		}
		if (attributeName.equals("ninassistnum")) {
			return this.m_ninassistnum;
		}
		if (attributeName.equals("cbilltypecode")) {
			return this.m_cbilltypecode;
		}
		if (attributeName.equals("vbillcode")) {
			return this.m_vbillcode;
		}
		if (attributeName.equals("castunitid"))
			return this.m_castunitid;
		if (attributeName.equals("castunitname"))
			return this.m_castunitname;
		if (attributeName.equals("vfree0")) {
			if (this.m_freevo != null) {
				return this.m_freevo.getVfree0();
			}
			return null;
		}
		if (attributeName.equals("ionhandnumtype"))
			return this.m_iOnhandnumType;
		if (attributeName.equals("cqualitylevelid"))
			return this.m_cqualitylevelid;
		if (attributeName.equals("hsl"))
			return this.m_hsl;
		if (attributeName.equals("ngrossnum"))
			return this.m_ngrossnum;
		//add by zwx 2014-12-26
		if (attributeName.equals("cspaceid")) {
			return this.m_cspaceid;
		}
		if (attributeName.equals("csname")) {
			return this.m_csname;
		}
		//end 
		if (attributeName.equals("onhandnumtype"))
			return this.m_iOnhandnumType.intValue() == 0 ? NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4008ui", "UPP4008ui-000024")
					: NCLangRes4VoTransl.getNCLangRes().getStrByID("4008ui",
							"UPP4008ui-000025");
		if (isBatchField(attributeName)) {
			return this.m_batchcodevo.getAttributeValue(attributeName);
		}
		return null;
	}

	public void setAttributeValue(String name, Object value) {
		String sTrimedValue = null;

		if (value != null)
			if ((value instanceof String)) {
				sTrimedValue = ((String) value).trim();
				if (sTrimedValue.length() == 0)
					sTrimedValue = null;
			} else {
				sTrimedValue = value.toString().trim();
			}
		try {
			if (name.equals("corpid")) {
				this.m_corpid = sTrimedValue;
			} else if (name.equals("cgeneralhid")) {
				this.m_cgeneralhid = sTrimedValue;
			} else if (name.equals("cgeneralbid")) {
				this.m_cgeneralbid = sTrimedValue;
			} else if (name.equals("cwarehouseid")) {
				this.m_cwarehouseid = sTrimedValue;
			} else if (name.equals("cinventoryid")) {
				this.m_cinventoryid = sTrimedValue;
			} else if (name.equals("correspondhid")) {
				this.m_correspondhid = sTrimedValue;
			} else if (name.equals("correspondbid")) {
				this.m_correspondbid = sTrimedValue;
			} else if (name.equals("dvalidate")) {
				this.m_dvalidate = SwitchObject.switchObjToUFDate(sTrimedValue);
				this.m_batchcodevo.setAttributeValue(name, value);
			} else if (name.equals("noutnum")) {
				this.m_noutnum = SwitchObject.switchObjToUFDouble(sTrimedValue);
			} else if (name.equals("noutassistnum")) {
				this.m_noutassistnum = SwitchObject
						.switchObjToUFDouble(sTrimedValue);
			} else if (name.equals("ninnum")) {
				this.m_ninnum = SwitchObject.switchObjToUFDouble(sTrimedValue);
			} else if (name.equals("ninassistnum")) {
				this.m_ninassistnum = SwitchObject
						.switchObjToUFDouble(sTrimedValue);
			} else if (name.equals("cbilltypecode")) {
				this.m_cbilltypecode = sTrimedValue;
			} else if (name.equals("vbillcode")) {
				this.m_vbillcode = sTrimedValue;
			} else if (name.equals("castunitid")) {
				this.m_castunitid = sTrimedValue;
			} else if (name.equals("castunitname")) {
				this.m_castunitname = sTrimedValue;
			} else if (name.startsWith("vfree")) {
				setFreeValue(name, sTrimedValue);
			} else if (name.equals("ionhandnumtype")) {
				this.m_iOnhandnumType = SwitchObject
						.switchObjToInteger(sTrimedValue);
			} else if (name.equals("hsl")) {
				this.m_hsl = SwitchObject.switchObjToUFDouble(sTrimedValue);
			} else if (name.equals("ngrossnum")) {
				this.m_ngrossnum = SwitchObject
						.switchObjToUFDouble(sTrimedValue);
			} else if (name.equals("vbatchcode")) {
				this.m_vbatchcode = sTrimedValue;
				this.m_batchcodevo.setVbatchcode(sTrimedValue);
			} else if (name.equals("cqualitylevelid")) {
				this.m_cqualitylevelid = sTrimedValue;
				this.m_batchcodevo.setCqualitylevelid(sTrimedValue);
			} 
			//add by zwx 2014-12-26
			else if (name.equals("cspaceid")){
				this.m_cspaceid = sTrimedValue;
			}
			else if (name.equals("csname")){
				this.m_csname = sTrimedValue;
			}
			//end 
			else if (isBatchField(name)) {
				this.m_batchcodevo.setAttributeValue(name, value);
			}
		} catch (ClassCastException e) {
			throw new ClassCastException(
					NCLangRes4VoTransl.getNCLangRes()
							.getStrByID(
									"SCMCOMMON",
									"UPPSCMCommon-000005",
									null,
									new String[] { name,
											String.valueOf(sTrimedValue) }));
		}
	}

	public Integer getOnhandnumType() {
		return this.m_iOnhandnumType;
	}

	public void setOnhandnumType(Integer newOnhandnumType) {
		this.m_iOnhandnumType = newOnhandnumType;
	}

	public String getCastunitname() {
		return this.m_castunitname;
	}

	public void setCastunitname(String newM_castunitname) {
		this.m_castunitname = newM_castunitname;
	}

	public void setFreeValue(String name, Object value) {
		if (this.m_freevo == null) {
			this.m_freevo = new FreeVO();
		}
		this.m_freevo.setAttributeValue(name, value);
	}

	public void setVfree1(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree1(sNewValue);
	}

	public void setVfree10(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree10(sNewValue);
	}

	public void setVfree2(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree2(sNewValue);
	}

	public void setVfree3(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree3(sNewValue);
	}

	public void setVfree4(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree4(sNewValue);
	}

	public void setVfree5(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree5(sNewValue);
	}

	public void setVfree6(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree6(sNewValue);
	}

	public void setVfree7(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree7(sNewValue);
	}

	public void setVfree8(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree8(sNewValue);
	}

	public void setVfree9(String sNewValue) {
		if (this.m_freevo == null)
			this.m_freevo = new FreeVO();
		this.m_freevo.setVfree9(sNewValue);
	}

	private boolean isBatchField(String name) {
		if (this.m_batchcodevo != null) {
			String[] sAttr = this.m_batchcodevo.getAttributeNames();
			for (int i = 0; i < sAttr.length; i++) {
				if (sAttr[i].equals(name)) {
					return true;
				}
			}
		}
		return false;
	}
}