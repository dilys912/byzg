package nc.vo.ps.estimate;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.field.pu.FieldDBValidate;
import nc.vo.scm.field.pu.FieldDBValidateInterface;
import nc.vo.scm.field.pu.FieldDBValidateVO;
import nc.vo.scm.field.pu.MoneyField;
import nc.vo.scm.field.pu.NumField;
import nc.vo.scm.field.pu.PriceField;

public class wwEstimateVO extends CircularlyAccessibleValueObject
  implements FieldDBValidateInterface
{
  public String m_cstockid;
  public String m_vbillcode;
  public UFDate m_dbilldate;
  public String m_cprovidermangid;
  public String m_cproviderbaseid;
  public String m_cgeneralhid;
  public String m_cgeneralbid;
  public String m_cmangid;
  public String m_pk_invmandoc;
  public String m_cbaseid;
  public String m_vuserdef1;
  public String m_vuserdef2;
  public String m_vuserdef3;
  public String m_vuserdef4;
  public String m_vuserdef5;
  public String m_vuserdef6;
  public String m_vfree1;
  public String m_vfree2;
  public String m_vfree3;
  public String m_vfree4;
  public String m_vfree5;
  public UFDouble m_ninnum;
  public UFDouble m_nprice;
  public UFDouble m_nmoney;
  public UFDouble m_nsettlenum;
  public UFDouble m_nsettlemny;
  public String m_cwarehouseid;
  public String m_pk_corp;
  public String m_pk_invoicecorp;
  public String m_pk_purcorp;
  public UFDate m_dzgdate;
  public String m_cvendorid;
  public UFDouble m_ntaxprice;
  public UFDouble m_ntotalmoney;
  public UFDouble m_ntaxrate;
  public Integer m_idiscounttaxtype;
  public UFBoolean m_bzgyf;
  public UFDouble m_ntaxmoney;
  public UFDouble m_ndiscountrate;
  public UFDouble m_nnetprice;
  public UFDouble m_nnettaxprice;
  public UFBoolean m_bMomey = null;
  public int m_nPricePolicy = -1;

  public UFBoolean m_bCalculateCost = null;

  private String m_castunitid = null;
  private String m_cbilltypecode = null;
  public String m_cbiztype;
  private String m_ccalbodyid;
  private String m_cdispatcherid = null;
  private String m_cdptid = null;
  private String m_cfirstbillbid = null;
  private String m_cfirstbillhid = null;
  private String m_cfirsttype = null;
  private String m_vfirstbillcode = null;
  public String m_cgeneralbb3;
  private String m_coperatorid = null;

  private String m_cprojectid = null;
  private String m_cprojectphaseid = null;
  public String m_csourcebillbid;
  public String m_csourcebillhid;
  private String m_cwhsmanagerid = null;
  private UFDate m_dbizdate = null;
  public Integer m_dr1;
  public Integer m_dr2;
  public Integer m_dr3;
  private UFDouble m_hsl = null;
  private UFDouble m_ninassistnum = null;
//  private UFDouble m_noutassistnum = null;
  public UFDouble m_nmaterialmoney;
  private UFDouble m_nplannedmny = null;
  public String m_ts1;
  public String m_ts2;
  public String m_ts3;
  public String m_vbatchcode;
  public String m_vfree;
  private String m_vnote = null;

  private String m_vuserdefh1 = null;
  private String m_vuserdefh10 = null;
  private String m_vuserdefh2 = null;
  private String m_vuserdefh3 = null;
  private String m_vuserdefh4 = null;
  private String m_vuserdefh5 = null;
  private String m_vuserdefh6 = null;
  private String m_vuserdefh7 = null;
  private String m_vuserdefh8 = null;
  private String m_vuserdefh9 = null;

  private String m_vuserdefh11 = null;
  private String m_vuserdefh20 = null;
  private String m_vuserdefh12 = null;
  private String m_vuserdefh13 = null;
  private String m_vuserdefh14 = null;
  private String m_vuserdefh15 = null;
  private String m_vuserdefh16 = null;
  private String m_vuserdefh17 = null;
  private String m_vuserdefh18 = null;
  private String m_vuserdefh19 = null;

  private String m_Pk_defdoch1 = null;
  private String m_Pk_defdoch2 = null;
  private String m_Pk_defdoch3 = null;
  private String m_Pk_defdoch4 = null;
  private String m_Pk_defdoch5 = null;
  private String m_Pk_defdoch6 = null;
  private String m_Pk_defdoch7 = null;
  private String m_Pk_defdoch8 = null;
  private String m_Pk_defdoch9 = null;
  private String m_Pk_defdoch10 = null;

  private String m_Pk_defdoch11 = null;
  private String m_Pk_defdoch12 = null;
  private String m_Pk_defdoch13 = null;
  private String m_Pk_defdoch14 = null;
  private String m_Pk_defdoch15 = null;
  private String m_Pk_defdoch16 = null;
  private String m_Pk_defdoch17 = null;
  private String m_Pk_defdoch18 = null;
  private String m_Pk_defdoch19 = null;
  private String m_Pk_defdoch20 = null;

  private String m_vuserdef7 = null;
  private String m_vuserdef8 = null;
  private String m_vuserdef9 = null;
  private String m_vuserdef10 = null;

  private String m_vuserdef11 = null;
  private String m_vuserdef20 = null;
  private String m_vuserdef12 = null;
  private String m_vuserdef13 = null;
  private String m_vuserdef14 = null;
  private String m_vuserdef15 = null;
  private String m_vuserdef16 = null;
  private String m_vuserdef17 = null;
  private String m_vuserdef18 = null;
  private String m_vuserdef19 = null;

  private String m_Pk_defdocb1 = null;
  private String m_Pk_defdocb2 = null;
  private String m_Pk_defdocb3 = null;
  private String m_Pk_defdocb4 = null;
  private String m_Pk_defdocb5 = null;
  private String m_Pk_defdocb6 = null;
  private String m_Pk_defdocb7 = null;
  private String m_Pk_defdocb8 = null;
  private String m_Pk_defdocb9 = null;
  private String m_Pk_defdocb10 = null;

  private String m_Pk_defdocb11 = null;
  private String m_Pk_defdocb12 = null;
  private String m_Pk_defdocb13 = null;
  private String m_Pk_defdocb14 = null;
  private String m_Pk_defdocb15 = null;
  private String m_Pk_defdocb16 = null;
  private String m_Pk_defdocb17 = null;
  private String m_Pk_defdocb18 = null;
  private String m_Pk_defdocb19 = null;
  private String m_Pk_defdocb20 = null;
  public String m_ccustomerid  = null;
  public String m_pk_cubasdoc  = null;

  public wwEstimateVO()
  {
  }

  public UFBoolean getBMoney()
  {
    return this.m_bMomey;
  }

  public void setBMoney(UFBoolean b) {
    this.m_bMomey = b;
  }
  public void setBcalculatecost(UFBoolean b) {
    this.m_bCalculateCost = b;
  }

  public wwEstimateVO(String newCstockid)
  {
    this.m_cstockid = newCstockid;
  }

  public Object clone()
  {
    Object o = null;
    try {
      o = super.clone(); } catch (Exception e) {
    }
    saleEstimateVO estimate = (saleEstimateVO)o;

    return estimate;
  }

  public String getEntityName()
  {
    return "Estimate";
  }

  public String getPrimaryKey()
  {
    return this.m_cstockid;
  }

  public void setPrimaryKey(String newCstockid)
  {
    this.m_cstockid = newCstockid;
  }

  public String getCstockid()
  {
    return this.m_cstockid;
  }

  public String getVbillcode()
  {
    return this.m_vbillcode;
  }

  public String getVfirstbillcode() {
    return this.m_vfirstbillcode;
  }

  public UFDate getDbilldate()
  {
    return this.m_dbilldate;
  }
  public UFDate getDzgdate() {
    return this.m_dzgdate;
  }

  public String getCprovidermangid()
  {
    return this.m_cprovidermangid;
  }

  public String getCvendorid() {
    return this.m_cvendorid;
  }

  public String getCproviderbaseid()
  {
    return this.m_cproviderbaseid;
  }

  public String getCgeneralhid()
  {
    return this.m_cgeneralhid;
  }

  public String getCgeneralbid()
  {
    return this.m_cgeneralbid;
  }

  public String getCmangid()
  {
    return this.m_cmangid;
  }
  public String getPk_invmandoc(){
	return this.m_pk_invmandoc;
  }
  
  public String getCbaseid()
  {
    return this.m_cbaseid;
  }

  public String getVuserdef1()
  {
    return this.m_vuserdef1;
  }

  public String getVuserdef2()
  {
    return this.m_vuserdef2;
  }

  public String getVuserdef3()
  {
    return this.m_vuserdef3;
  }

  public String getVuserdef4()
  {
    return this.m_vuserdef4;
  }

  public String getVuserdef5()
  {
    return this.m_vuserdef5;
  }

  public String getVuserdef6()
  {
    return this.m_vuserdef6;
  }
  public String getVuserdef7() {
    return this.m_vuserdef7;
  }
  public String getVuserdef8() {
    return this.m_vuserdef8;
  }
  public String getVuserdef9() {
    return this.m_vuserdef9;
  }
  public String getVuserdef10() {
    return this.m_vuserdef10;
  }

  public String getVuserdef11() {
    return this.m_vuserdef11;
  }
  public String getVuserdef12() {
    return this.m_vuserdef12;
  }
  public String getVuserdef13() {
    return this.m_vuserdef13;
  }
  public String getVuserdef14() {
    return this.m_vuserdef14;
  }
  public String getVuserdef15() {
    return this.m_vuserdef15;
  }
  public String getVuserdef16() {
    return this.m_vuserdef16;
  }
  public String getVuserdef17() {
    return this.m_vuserdef17;
  }
  public String getVuserdef18() {
    return this.m_vuserdef18;
  }
  public String getVuserdef19() {
    return this.m_vuserdef19;
  }
  public String getVuserdef20() {
    return this.m_vuserdef20;
  }

  public String getPk_defdocb1() {
    return this.m_Pk_defdocb1;
  }
  public String getPk_defdocb2() {
    return this.m_Pk_defdocb2;
  }
  public String getPk_defdocb3() {
    return this.m_Pk_defdocb3;
  }
  public String getPk_defdocb4() {
    return this.m_Pk_defdocb4;
  }
  public String getPk_defdocb5() {
    return this.m_Pk_defdocb5;
  }
  public String getPk_defdocb6() {
    return this.m_Pk_defdocb6;
  }
  public String getPk_defdocb7() {
    return this.m_Pk_defdocb7;
  }
  public String getPk_defdocb8() {
    return this.m_Pk_defdocb8;
  }
  public String getPk_defdocb9() {
    return this.m_Pk_defdocb9;
  }
  public String getPk_defdocb10() {
    return this.m_Pk_defdocb10;
  }

  public String getPk_defdocb11() {
    return this.m_Pk_defdocb11;
  }
  public String getPk_defdocb12() {
    return this.m_Pk_defdocb12;
  }
  public String getPk_defdocb13() {
    return this.m_Pk_defdocb13;
  }
  public String getPk_defdocb14() {
    return this.m_Pk_defdocb14;
  }
  public String getPk_defdocb15() {
    return this.m_Pk_defdocb15;
  }
  public String getPk_defdocb16() {
    return this.m_Pk_defdocb16;
  }
  public String getPk_defdocb17() {
    return this.m_Pk_defdocb17;
  }
  public String getPk_defdocb18() {
    return this.m_Pk_defdocb18;
  }
  public String getPk_defdocb19() {
    return this.m_Pk_defdocb19;
  }
  public String getPk_defdocb20() {
    return this.m_Pk_defdocb20;
  }

  public String getVfree1()
  {
    return this.m_vfree1;
  }

  public String getVfree2()
  {
    return this.m_vfree2;
  }

  public String getVfree3()
  {
    return this.m_vfree3;
  }

  public String getVfree4()
  {
    return this.m_vfree4;
  }

  public String getVfree5()
  {
    return this.m_vfree5;
  }

  public UFDouble getNinnum()
  {
    return this.m_ninnum;
  }

  public UFDouble getNprice()
  {
    return this.m_nprice;
  }

  public UFDouble getNtaxprice() {
    return this.m_ntaxprice;
  }
  public UFDouble getNtotalmoney() {
    return this.m_ntotalmoney;
  }
  public UFDouble getNtaxrate() {
    return this.m_ntaxrate;
  }

  public int getNPricePolicy() {
    return this.m_nPricePolicy;
  }

  public void setNPricePolicy(int nPolicy) {
    this.m_nPricePolicy = nPolicy;
  }

  public UFDouble getNdiscountrate() {
    return this.m_ndiscountrate;
  }

  public Integer getIdiscounttaxtype() {
    return this.m_idiscounttaxtype;
  }
  public UFBoolean getBzgyf() {
    return this.m_bzgyf;
  }
  public UFBoolean getBcalculatecost() {
    return this.m_bCalculateCost;
  }

  public UFDouble getNmoney()
  {
    return this.m_nmoney;
  }

  public UFDouble getNsettlenum()
  {
    return this.m_nsettlenum;
  }

  public UFDouble getNsettlemny()
  {
    return this.m_nsettlemny;
  }

  public String getCwarehouseid()
  {
    return this.m_cwarehouseid;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public String getPk_purcorp() {
    return this.m_pk_purcorp;
  }

  public String getPk_invoicecorp() {
    return this.m_pk_invoicecorp;
  }

  public void setCstockid(String newCstockid)
  {
    this.m_cstockid = newCstockid;
  }

  public void setVbillcode(String newVbillcode)
  {
    this.m_vbillcode = newVbillcode;
  }

  public void setVfirstbillcode(String newVbillcode)
  {
    this.m_vfirstbillcode = newVbillcode;
  }

  public void setDbilldate(UFDate newDbilldate)
  {
    this.m_dbilldate = newDbilldate;
  }

  public void setDzgdate(UFDate newDbilldate) {
    this.m_dzgdate = newDbilldate;
  }

  public void setCprovidermangid(String newCprovidermangid)
  {
    this.m_cprovidermangid = newCprovidermangid;
  }

  public void setCvendorid(String newCprovidermangid) {
    this.m_cvendorid = newCprovidermangid;
  }

  public void setCproviderbaseid(String newCproviderbaseid)
  {
    this.m_cproviderbaseid = newCproviderbaseid;
  }

  public void setCgeneralhid(String newCgeneralhid)
  {
    this.m_cgeneralhid = newCgeneralhid;
  }

  public void setCgeneralbid(String newCgeneralbid)
  {
    this.m_cgeneralbid = newCgeneralbid;
  }

  public void setCmangid(String newCmangid)
  {
    this.m_cmangid = newCmangid;
  }
  
  public void setPk_invmandoc(String newPk_invmandoc){
	this.m_pk_invmandoc = newPk_invmandoc;
  }
  
  public void setCbaseid(String newCbaseid)
  {
    this.m_cbaseid = newCbaseid;
  }

  public void setVuserdef1(String newVuserdef1)
  {
    this.m_vuserdef1 = newVuserdef1;
  }

  public void setVuserdef2(String newVuserdef2)
  {
    this.m_vuserdef2 = newVuserdef2;
  }

  public void setVuserdef3(String newVuserdef3)
  {
    this.m_vuserdef3 = newVuserdef3;
  }

  public void setVuserdef4(String newVuserdef4)
  {
    this.m_vuserdef4 = newVuserdef4;
  }

  public void setVuserdef5(String newVuserdef5)
  {
    this.m_vuserdef5 = newVuserdef5;
  }

  public void setVuserdef6(String newVuserdef6)
  {
    this.m_vuserdef6 = newVuserdef6;
  }

  public void setVuserdef7(String newVuserdef6) {
    this.m_vuserdef7 = newVuserdef6;
  }

  public void setVuserdef8(String newVuserdef6) {
    this.m_vuserdef8 = newVuserdef6;
  }

  public void setVuserdef9(String newVuserdef6) {
    this.m_vuserdef9 = newVuserdef6;
  }

  public void setVuserdef10(String newVuserdef6) {
    this.m_vuserdef10 = newVuserdef6;
  }

  public void setVuserdef11(String newVuserdef6)
  {
    this.m_vuserdef11 = newVuserdef6;
  }

  public void setVuserdef12(String newVuserdef6) {
    this.m_vuserdef12 = newVuserdef6;
  }

  public void setVuserdef13(String newVuserdef6) {
    this.m_vuserdef13 = newVuserdef6;
  }

  public void setVuserdef14(String newVuserdef6) {
    this.m_vuserdef14 = newVuserdef6;
  }

  public void setVuserdef15(String newVuserdef6) {
    this.m_vuserdef15 = newVuserdef6;
  }

  public void setVuserdef16(String newVuserdef6) {
    this.m_vuserdef16 = newVuserdef6;
  }

  public void setVuserdef17(String newVuserdef6) {
    this.m_vuserdef17 = newVuserdef6;
  }

  public void setVuserdef18(String newVuserdef6) {
    this.m_vuserdef18 = newVuserdef6;
  }

  public void setVuserdef19(String newVuserdef6) {
    this.m_vuserdef19 = newVuserdef6;
  }

  public void setVuserdef20(String newVuserdef6) {
    this.m_vuserdef20 = newVuserdef6;
  }

  public void setPk_defdocb1(String newVuserdef6)
  {
    this.m_Pk_defdocb1 = newVuserdef6;
  }

  public void setPk_defdocb2(String newVuserdef6) {
    this.m_Pk_defdocb2 = newVuserdef6;
  }

  public void setPk_defdocb3(String newVuserdef6) {
    this.m_Pk_defdocb3 = newVuserdef6;
  }

  public void setPk_defdocb4(String newVuserdef6) {
    this.m_Pk_defdocb4 = newVuserdef6;
  }

  public void setPk_defdocb5(String newVuserdef6) {
    this.m_Pk_defdocb5 = newVuserdef6;
  }

  public void setPk_defdocb6(String newVuserdef6) {
    this.m_Pk_defdocb6 = newVuserdef6;
  }

  public void setPk_defdocb7(String newVuserdef6) {
    this.m_Pk_defdocb7 = newVuserdef6;
  }

  public void setPk_defdocb8(String newVuserdef6) {
    this.m_Pk_defdocb8 = newVuserdef6;
  }

  public void setPk_defdocb9(String newVuserdef6) {
    this.m_Pk_defdocb9 = newVuserdef6;
  }

  public void setPk_defdocb10(String newVuserdef6) {
    this.m_Pk_defdocb10 = newVuserdef6;
  }

  public void setPk_defdocb11(String newVuserdef6)
  {
    this.m_Pk_defdocb11 = newVuserdef6;
  }

  public void setPk_defdocb12(String newVuserdef6) {
    this.m_Pk_defdocb12 = newVuserdef6;
  }

  public void setPk_defdocb13(String newVuserdef6) {
    this.m_Pk_defdocb13 = newVuserdef6;
  }

  public void setPk_defdocb14(String newVuserdef6) {
    this.m_Pk_defdocb14 = newVuserdef6;
  }

  public void setPk_defdocb15(String newVuserdef6) {
    this.m_Pk_defdocb15 = newVuserdef6;
  }

  public void setPk_defdocb16(String newVuserdef6) {
    this.m_Pk_defdocb16 = newVuserdef6;
  }

  public void setPk_defdocb17(String newVuserdef6) {
    this.m_Pk_defdocb17 = newVuserdef6;
  }

  public void setPk_defdocb18(String newVuserdef6) {
    this.m_Pk_defdocb18 = newVuserdef6;
  }

  public void setPk_defdocb19(String newVuserdef6) {
    this.m_Pk_defdocb19 = newVuserdef6;
  }

  public void setPk_defdocb20(String newVuserdef6) {
    this.m_Pk_defdocb20 = newVuserdef6;
  }

  public void setVfree1(String newVfree1)
  {
    this.m_vfree1 = newVfree1;
  }

  public void setVfree2(String newVfree2)
  {
    this.m_vfree2 = newVfree2;
  }

  public void setVfree3(String newVfree3)
  {
    this.m_vfree3 = newVfree3;
  }

  public void setVfree4(String newVfree4)
  {
    this.m_vfree4 = newVfree4;
  }

  public void setVfree5(String newVfree5)
  {
    this.m_vfree5 = newVfree5;
  }

  public void setNinnum(UFDouble newNinnum)
  {
    this.m_ninnum = newNinnum;
  }

  public void setNprice(UFDouble newNprice)
  {
    this.m_nprice = newNprice;
  }

  public void setNmoney(UFDouble newNmoney)
  {
    this.m_nmoney = newNmoney;
  }

  public void setNtaxprice(UFDouble newNmoney)
  {
    this.m_ntaxprice = newNmoney;
  }

  public void setNtotalmoney(UFDouble newNmoney) {
    this.m_ntotalmoney = newNmoney;
  }

  public void setNtaxrate(UFDouble newNmoney) {
    this.m_ntaxrate = newNmoney;
  }

  public void setNdiscountrate(UFDouble newNmoney)
  {
    this.m_ndiscountrate = newNmoney;
  }

  public void setBzgyf(UFBoolean newNmoney) {
    this.m_bzgyf = newNmoney;
  }

  public void setIdiscounttaxtype(Integer newNmoney) {
    this.m_idiscounttaxtype = newNmoney;
  }

  public void setNsettlenum(UFDouble newNsettlenum)
  {
    this.m_nsettlenum = newNsettlenum;
  }

  public void setNsettlemny(UFDouble newNsettlemny)
  {
    this.m_nsettlemny = newNsettlemny;
  }

  public void setCwarehouseid(String newCwarehouseid)
  {
    this.m_cwarehouseid = newCwarehouseid;
  }

  public void setPk_corp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setPk_purcorp(String newPk_corp)
  {
    this.m_pk_purcorp = newPk_corp;
  }

  public void setPk_invoicecorp(String newPk_corp)
  {
    this.m_pk_invoicecorp = newPk_corp;
  }

  public void validate()
    throws ValidationException
  {
    StringBuffer errFields = new StringBuffer();

    String message = NCLangRes4VoTransl.getNCLangRes().getStrByID("40040503", "UPP40040503-000081");
    if (errFields.toString().length() > 0) {
      message = message + errFields.toString();

      throw new NullFieldException(message.toString());
    }

    FieldDBValidate.validate(this);
  }

  public String[] getAttributeNames()
  {
    return new String[] { "vbatchcode", "vbillcode", "dbilldate", "cprovidermangid", "cproviderbaseid", "cgeneralhid", "cgeneralbid", "cmangid", "cbaseid", "vuserdef1", "vuserdef2", "vuserdef3", "vuserdef4", "vuserdef5", "vuserdef6", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "noutnum", "nprice", "nmoney", "nsettlenum", "nsettlemny", "cwarehouseid", "pk_corp", "nmaterialmoney", "cgeneralbb3", "dr1", "ts1", "dr2", "ts2", "dr3", "ts3", "cbiztype", "ccalbodyid", "dzgdate", "ntaxprice", "ntotalmoney", "ntaxrate", "idiscounttaxtype", "bzgyf", "ntaxmoney", "m_ndiscountrate", "nnetprice", "nnettaxprice", "bcalculatecost","ccustomerid" };
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("ccustomerid"))
      return this.m_ccustomerid;
    if (attributeName.equals("pk_cubasdoc"))
        return this.m_pk_cubasdoc;
    if (attributeName.equals("cstockid"))
        return this.m_cstockid;
    if (attributeName.equals("vbillcode"))
      return this.m_vbillcode;
    if (attributeName.equals("vbatchcode"))
      return this.m_vbatchcode;
    if (attributeName.equals("dbilldate"))
      return this.m_dbilldate;
    if (attributeName.equals("cprovidermangid"))
      return this.m_cprovidermangid;
    if (attributeName.equals("cproviderbaseid"))
      return this.m_cproviderbaseid;
    if (attributeName.equals("cgeneralhid"))
      return this.m_cgeneralhid;
    if (attributeName.equals("cgeneralbid"))
      return this.m_cgeneralbid;
    if (attributeName.equals("cmangid"))
      return this.m_cmangid;
    if (attributeName.equals("pk_invmandoc"))
        return this.m_pk_invmandoc;
    if (attributeName.equals("cbaseid"))
      return this.m_cbaseid;
    if (attributeName.equals("vuserdef1"))
      return this.m_vuserdef1;
    if (attributeName.equals("vuserdef2"))
      return this.m_vuserdef2;
    if (attributeName.equals("vuserdef3"))
      return this.m_vuserdef3;
    if (attributeName.equals("vuserdef4"))
      return this.m_vuserdef4;
    if (attributeName.equals("vuserdef5"))
      return this.m_vuserdef5;
    if (attributeName.equals("vuserdef6"))
      return this.m_vuserdef6;
    if (attributeName.equals("vfree1"))
      return this.m_vfree1;
    if (attributeName.equals("vfree2"))
      return this.m_vfree2;
    if (attributeName.equals("vfree3"))
      return this.m_vfree3;
    if (attributeName.equals("vfree4"))
      return this.m_vfree4;
    if (attributeName.equals("vfree5"))
      return this.m_vfree5;
    if (attributeName.equals("ninnum"))
      return this.m_ninnum;
    if (attributeName.equals("nprice"))
      return this.m_nprice;
    if (attributeName.equals("nmoney"))
      return this.m_nmoney;
    if (attributeName.equals("nmaterialmoney"))
      return this.m_nmaterialmoney;
    if (attributeName.equals("nsettlenum"))
      return this.m_nsettlenum;
    if (attributeName.equals("nsettlemny"))
      return this.m_nsettlemny;
    if (attributeName.equals("cwarehouseid"))
      return this.m_cwarehouseid;
    if (attributeName.equals("pk_corp"))
      return this.m_pk_corp;
    if (attributeName.equals("cgeneralbb3"))
      return this.m_cgeneralbb3;
    if (attributeName.equals("dr1"))
      return this.m_dr1;
    if (attributeName.equals("ts1"))
      return this.m_ts1;
    if (attributeName.equals("dr2"))
      return this.m_dr2;
    if (attributeName.equals("ts2"))
      return this.m_ts2;
    if (attributeName.equals("dr3"))
      return this.m_dr3;
    if (attributeName.equals("ts3"))
      return this.m_ts3;
    if (attributeName.equals("cbiztype"))
      return this.m_cbiztype;
    if (attributeName.equals("vfree"))
      return this.m_vfree;
    if (attributeName.equals("ccalbodyid"))
      return this.m_ccalbodyid;
    if (attributeName.equals("csourcebillhid"))
      return this.m_csourcebillhid;
    if (attributeName.equals("csourcebillbid"))
      return this.m_csourcebillbid;
    if (attributeName.equals("dzgdate"))
      return this.m_dzgdate;
    if (attributeName.equals("ntaxprice"))
      return this.m_ntaxprice;
    if (attributeName.equals("ntotalmoney"))
      return this.m_ntotalmoney;
    if (attributeName.equals("ntaxrate"))
      return this.m_ntaxrate;
    if (attributeName.equals("bzgyf"))
      return this.m_bzgyf;
    if (attributeName.equals("idiscounttaxtype"))
      return this.m_idiscounttaxtype;
    if (attributeName.equals("ntaxmoney"))
      return this.m_ntaxmoney;
    if (attributeName.equals("ndiscountrate"))
      return this.m_ndiscountrate;
    if (attributeName.equals("nnetprice"))
      return this.m_nnetprice;
    if (attributeName.equals("nnettaxprice"))
      return this.m_nnettaxprice;
    if (attributeName.equals("ndiscountrate"))
      return this.m_ndiscountrate;
    if (attributeName.equals("bcalculatecost")) {
      return this.m_bCalculateCost;
    }
    return null;
  }

  public void setAttributeValue(String name, Object value)
  {
    try
    {
      if (name.equals("cstockid"))
        this.m_cstockid = ((String)value);
      else if (name.equals("ccustomerid"))
          this.m_ccustomerid = ((String)value);
      else if (name.equals("pk_cubasdoc"))
    	  this.m_pk_cubasdoc = ((String)value);
      else if (name.equals("vbillcode"))
        this.m_vbillcode = ((String)value);
      else if (name.equals("vbatchcode"))
        this.m_vbatchcode = ((String)value);
      else if (name.equals("dbilldate"))
        this.m_dbilldate = ((UFDate)value);
      else if (name.equals("cprovidermangid"))
        this.m_cprovidermangid = ((String)value);
      else if (name.equals("cproviderbaseid"))
        this.m_cproviderbaseid = ((String)value);
      else if (name.equals("cgeneralhid"))
        this.m_cgeneralhid = ((String)value);
      else if (name.equals("cgeneralbid"))
        this.m_cgeneralbid = ((String)value);
      else if (name.equals("cmangid"))
        this.m_cmangid = ((String)value);
      else if (name.equals("pk_invmandoc"))
    	  this.m_pk_invmandoc = ((String)value);
      else if (name.equals("cbaseid"))
        this.m_cbaseid = ((String)value);
      else if (name.equals("vuserdef1"))
        this.m_vuserdef1 = ((String)value);
      else if (name.equals("vuserdef2"))
        this.m_vuserdef2 = ((String)value);
      else if (name.equals("vuserdef3"))
        this.m_vuserdef3 = ((String)value);
      else if (name.equals("vuserdef4"))
        this.m_vuserdef4 = ((String)value);
      else if (name.equals("vuserdef5"))
        this.m_vuserdef5 = ((String)value);
      else if (name.equals("vuserdef6"))
        this.m_vuserdef6 = ((String)value);
      else if (name.equals("vfree1"))
        this.m_vfree1 = ((String)value);
      else if (name.equals("vfree2"))
        this.m_vfree2 = ((String)value);
      else if (name.equals("vfree3"))
        this.m_vfree3 = ((String)value);
      else if (name.equals("vfree4"))
        this.m_vfree4 = ((String)value);
      else if (name.equals("vfree5"))
        this.m_vfree5 = ((String)value);
      else if (name.equals("ninnum"))
        this.m_ninnum = ((UFDouble)value);
      else if (name.equals("nprice"))
        this.m_nprice = ((UFDouble)value);
      else if (name.equals("nmoney"))
        this.m_nmoney = ((UFDouble)value);
      else if (name.equals("nmaterialmoney"))
        this.m_nmaterialmoney = ((UFDouble)value);
      else if (name.equals("nsettlenum"))
        this.m_nsettlenum = ((UFDouble)value);
      else if (name.equals("nsettlemny"))
        this.m_nsettlemny = ((UFDouble)value);
      else if (name.equals("cwarehouseid"))
        this.m_cwarehouseid = ((String)value);
      else if (name.equals("pk_corp"))
        this.m_pk_corp = ((String)value);
      else if (name.equals("cgeneralbb3"))
        this.m_cgeneralbb3 = ((String)value);
      else if (name.equals("dr1"))
        this.m_dr1 = ((Integer)value);
      else if (name.equals("ts1"))
        this.m_ts1 = ((String)value);
      else if (name.equals("dr2"))
        this.m_dr2 = ((Integer)value);
      else if (name.equals("ts2"))
        this.m_ts2 = ((String)value);
      else if (name.equals("dr3"))
        this.m_dr3 = ((Integer)value);
      else if (name.equals("ts3"))
        this.m_ts3 = ((String)value);
      else if (name.equals("cbiztype"))
        this.m_cbiztype = ((String)value);
      else if (name.equals("vfree"))
        this.m_vfree = ((String)value);
      else if (name.equals("ccalbodyid"))
        this.m_ccalbodyid = ((String)value);
      else if (name.equals("csourcebillhid"))
        this.m_csourcebillhid = ((String)value);
      else if (name.equals("csourcebillbid"))
        this.m_csourcebillbid = ((String)value);
      else if (name.equals("dzgdate"))
        this.m_dzgdate = ((UFDate)value);
      else if (name.equals("ntaxprice"))
        this.m_ntaxprice = ((UFDouble)value);
      else if (name.equals("ntotalmoney"))
        this.m_ntotalmoney = ((UFDouble)value);
      else if (name.equals("ntaxrate"))
        this.m_ntaxrate = ((UFDouble)value);
      else if (name.equals("bzgyf"))
        this.m_bzgyf = ((UFBoolean)value);
      else if (name.equals("bcalculatecost"))
        this.m_bCalculateCost = ((UFBoolean)value);
      else if (name.equals("idiscounttaxtype"))
        this.m_idiscounttaxtype = ((Integer)value);
      else if (name.equals("ntaxmoney"))
        this.m_ntaxmoney = ((UFDouble)value);
      else if (name.equals("ndiscountrate"))
        this.m_ndiscountrate = ((UFDouble)value);
      else if (name.equals("nnetprice"))
        this.m_nnetprice = ((UFDouble)value);
      else if (name.equals("nnettaxprice"))
        this.m_nnettaxprice = ((UFDouble)value);
    }
    catch (ClassCastException e) {
      throw new ClassCastException("setAttributeValue方法中为 " + name + " 赋值时类型转换错误！（值：" + value + "）");
    }
  }

  public String getCastunitid()
  {
    return this.m_castunitid;
  }

  public String getCbilltypecode()
  {
    return this.m_cbilltypecode;
  }

  public String getCbiztype()
  {
    return this.m_cbiztype;
  }

  public String getCcalbodyid()
  {
    return this.m_ccalbodyid;
  }

  public String getCdispatcherid()
  {
    return this.m_cdispatcherid;
  }

  public String getCdptid()
  {
    return this.m_cdptid;
  }

  public String getCfirstbillbid()
  {
    return this.m_cfirstbillbid;
  }

  public String getCfirstbillhid()
  {
    return this.m_cfirstbillhid;
  }

  public String getCfirsttype()
  {
    return this.m_cfirsttype;
  }

  public String getCgeneralbb3()
  {
    return this.m_cgeneralbb3;
  }

  public String getCoperatorid()
  {
    return this.m_coperatorid;
  }

  public String getCprojectid()
  {
    return this.m_cprojectid;
  }

  public String getCprojectphaseid()
  {
    return this.m_cprojectphaseid;
  }

  public String getCsourcebillbid()
  {
    return this.m_csourcebillbid;
  }

  public String getCsourcebillhid()
  {
    return this.m_csourcebillhid;
  }

  public String getCwhsmanagerid()
  {
    return this.m_cwhsmanagerid;
  }

  public UFDate getDbizdate()
  {
    return this.m_dbizdate;
  }

  public Integer getDr1()
  {
    return this.m_dr1;
  }

  public Integer getDr2()
  {
    return this.m_dr2;
  }

  public Integer getDr3()
  {
    return this.m_dr3;
  }

  public FieldDBValidateVO[] getFieldDBValidateVOs()
  {
    return new FieldDBValidateVO[] { new FieldDBValidateVO(NumField.class, new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002282") }, new UFDouble[] { this.m_ninnum }), new FieldDBValidateVO(PriceField.class, new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000741") }, new UFDouble[] { this.m_nprice }), new FieldDBValidateVO(MoneyField.class, new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004112") }, new UFDouble[] { this.m_nmoney }) };
  }

  public UFDouble getHsl()
  {
    return this.m_hsl;
  }

  public UFDouble getNinassistnum(){
	  return this.m_ninassistnum;
  }
//  public UFDouble getNoutassistnum(){
//	  return this.m_noutassistnum;
//  }
  public UFDouble getNmaterialmoney()
  {
    return this.m_nmaterialmoney;
  }

  public UFDouble getNplannedmny()
  {
    return this.m_nplannedmny;
  }

  public String getTs1()
  {
    return this.m_ts1;
  }

  public String getTs2()
  {
    return this.m_ts2;
  }

  public String getTs3()
  {
    return this.m_ts3;
  }

  public String getVbatchcode()
  {
    return this.m_vbatchcode;
  }

  public String getVfree()
  {
    return this.m_vfree;
  }

  public String getVnote()
  {
    return this.m_vnote;
  }

  public String getVuserdefh1()
  {
    return this.m_vuserdefh1;
  }

  public String getVuserdefh10()
  {
    return this.m_vuserdefh10;
  }

  public String getVuserdefh2()
  {
    return this.m_vuserdefh2;
  }

  public String getVuserdefh3()
  {
    return this.m_vuserdefh3;
  }

  public String getVuserdefh4()
  {
    return this.m_vuserdefh4;
  }

  public String getVuserdefh5()
  {
    return this.m_vuserdefh5;
  }

  public String getVuserdefh6()
  {
    return this.m_vuserdefh6;
  }

  public String getVuserdefh7()
  {
    return this.m_vuserdefh7;
  }

  public String getVuserdefh8()
  {
    return this.m_vuserdefh8;
  }

  public String getVuserdefh9()
  {
    return this.m_vuserdefh9;
  }

  public String getVuserdefh11() {
    return this.m_vuserdefh11;
  }
  public String getVuserdefh12() {
    return this.m_vuserdefh12;
  }
  public String getVuserdefh13() {
    return this.m_vuserdefh13;
  }
  public String getVuserdefh14() {
    return this.m_vuserdefh14;
  }
  public String getVuserdefh15() {
    return this.m_vuserdefh15;
  }
  public String getVuserdefh16() {
    return this.m_vuserdefh16;
  }
  public String getVuserdefh17() {
    return this.m_vuserdefh17;
  }
  public String getVuserdefh18() {
    return this.m_vuserdefh18;
  }
  public String getVuserdefh19() {
    return this.m_vuserdefh19;
  }
  public String getVuserdefh20() {
    return this.m_vuserdefh20;
  }

  public String getPk_defdoch1() {
    return this.m_Pk_defdoch1;
  }
  public String getPk_defdoch2() {
    return this.m_Pk_defdoch2;
  }
  public String getPk_defdoch3() {
    return this.m_Pk_defdoch3;
  }
  public String getPk_defdoch4() {
    return this.m_Pk_defdoch4;
  }
  public String getPk_defdoch5() {
    return this.m_Pk_defdoch5;
  }
  public String getPk_defdoch6() {
    return this.m_Pk_defdoch6;
  }
  public String getPk_defdoch7() {
    return this.m_Pk_defdoch7;
  }
  public String getPk_defdoch8() {
    return this.m_Pk_defdoch8;
  }
  public String getPk_defdoch9() {
    return this.m_Pk_defdoch9;
  }
  public String getPk_defdoch10() {
    return this.m_Pk_defdoch10;
  }

  public String getPk_defdoch11() {
    return this.m_Pk_defdoch11;
  }
  public String getPk_defdoch12() {
    return this.m_Pk_defdoch12;
  }
  public String getPk_defdoch13() {
    return this.m_Pk_defdoch13;
  }
  public String getPk_defdoch14() {
    return this.m_Pk_defdoch14;
  }
  public String getPk_defdoch15() {
    return this.m_Pk_defdoch15;
  }
  public String getPk_defdoch16() {
    return this.m_Pk_defdoch16;
  }
  public String getPk_defdoch17() {
    return this.m_Pk_defdoch17;
  }
  public String getPk_defdoch18() {
    return this.m_Pk_defdoch18;
  }
  public String getPk_defdoch19() {
    return this.m_Pk_defdoch19;
  }
  public String getPk_defdoch20() {
    return this.m_Pk_defdoch20;
  }
  public String getCcustomerid() {
	return this.m_ccustomerid; 
  }
  public String getPk_cubasdoc() {
	  return this.m_pk_cubasdoc; 
  }
  public void setCastunitid(String newM_castunitid)
  {
    this.m_castunitid = newM_castunitid;
  }

  public void setCbilltypecode(String newM_cbilltypecode)
  {
    this.m_cbilltypecode = newM_cbilltypecode;
  }

  public void setCbiztype(String newCbiztype)
  {
    this.m_cbiztype = newCbiztype;
  }

  public void setCcalbodyid(String newM_ccalbodyid)
  {
    this.m_ccalbodyid = newM_ccalbodyid;
  }

  public void setCdispatcherid(String newM_cdispatcherid)
  {
    this.m_cdispatcherid = newM_cdispatcherid;
  }

  public void setCdptid(String newM_cdptid)
  {
    this.m_cdptid = newM_cdptid;
  }

  public void setCfirstbillbid(String newM_cfirstbillbid)
  {
    this.m_cfirstbillbid = newM_cfirstbillbid;
  }

  public void setCfirstbillhid(String newM_cfirstbillhid)
  {
    this.m_cfirstbillhid = newM_cfirstbillhid;
  }

  public void setCfirsttype(String newM_cfirsttype)
  {
    this.m_cfirsttype = newM_cfirsttype;
  }

  public void setCgeneralbb3(String newCgeneralbb3)
  {
    this.m_cgeneralbb3 = newCgeneralbb3;
  }

  public void setCoperatorid(String newM_coperatorid)
  {
    this.m_coperatorid = newM_coperatorid;
  }

  public void setCprojectid(String newM_cprojectid)
  {
    this.m_cprojectid = newM_cprojectid;
  }

  public void setCprojectphaseid(String newM_cprojectphaseid)
  {
    this.m_cprojectphaseid = newM_cprojectphaseid;
  }

  public void setCsourcebillbid(String newCsourcebillbid)
  {
    this.m_csourcebillbid = newCsourcebillbid;
  }

  public void setCsourcebillhid(String newCsourcebillhid)
  {
    this.m_csourcebillhid = newCsourcebillhid;
  }

  public void setCwhsmanagerid(String newM_cwhsmanagerid)
  {
    this.m_cwhsmanagerid = newM_cwhsmanagerid;
  }

  public void setDbizdate(UFDate newM_dbizdate)
  {
    this.m_dbizdate = newM_dbizdate;
  }

  public void setDr1(Integer newDr1)
  {
    this.m_dr1 = newDr1;
  }

  public void setDr2(Integer newDr2)
  {
    this.m_dr2 = newDr2;
  }

  public void setDr3(Integer newDr3)
  {
    this.m_dr3 = newDr3;
  }

  public void setHsl(UFDouble newM_hsl)
  {
    this.m_hsl = newM_hsl;
  }

//  public void setNoutassistnum(UFDouble newM_noutassistnum){
//	  
//	  this.m_noutassistnum = newM_noutassistnum;
//  }
  public void setNinassistnum(UFDouble newM_Ninassistnum){
	  
	  this.m_ninassistnum = newM_Ninassistnum;
  }
  
  public void setNmaterialmoney(UFDouble newNmaterialmoney)
  {
    this.m_nmaterialmoney = newNmaterialmoney;
  }

  public void setNplannedmny(UFDouble newM_nplannedmny)
  {
    this.m_nplannedmny = newM_nplannedmny;
  }

  public void setTs1(String newTs1)
  {
    this.m_ts1 = newTs1;
  }

  public void setTs2(String newTs2)
  {
    this.m_ts2 = newTs2;
  }

  public void setTs3(String newTs3)
  {
    this.m_ts3 = newTs3;
  }

  public void setVbatchcode(String newVbatchcode)
  {
    this.m_vbatchcode = newVbatchcode;
  }

  public void setVfree(String newVfree1)
  {
    this.m_vfree = newVfree1;
  }

  public void setVnote(String newM_vnote)
  {
    this.m_vnote = newM_vnote;
  }

  public void setVuserdefh1(String newM_vuserdefh1)
  {
    this.m_vuserdefh1 = newM_vuserdefh1;
  }

  public void setVuserdefh10(String newM_vuserdefh10)
  {
    this.m_vuserdefh10 = newM_vuserdefh10;
  }

  public void setVuserdefh2(String newM_vuserdefh2)
  {
    this.m_vuserdefh2 = newM_vuserdefh2;
  }

  public void setVuserdefh3(String newM_vuserdefh3)
  {
    this.m_vuserdefh3 = newM_vuserdefh3;
  }

  public void setVuserdefh4(String newM_vuserdefh4)
  {
    this.m_vuserdefh4 = newM_vuserdefh4;
  }

  public void setVuserdefh5(String newM_vuserdefh5)
  {
    this.m_vuserdefh5 = newM_vuserdefh5;
  }

  public void setVuserdefh6(String newM_vuserdefh6)
  {
    this.m_vuserdefh6 = newM_vuserdefh6;
  }

  public void setVuserdefh7(String newM_vuserdefh7)
  {
    this.m_vuserdefh7 = newM_vuserdefh7;
  }

  public void setVuserdefh8(String newM_vuserdefh8)
  {
    this.m_vuserdefh8 = newM_vuserdefh8;
  }

  public void setVuserdefh9(String newM_vuserdefh9)
  {
    this.m_vuserdefh9 = newM_vuserdefh9;
  }

  public void setVuserdefh11(String newVuserdef6)
  {
    this.m_vuserdefh11 = newVuserdef6;
  }

  public void setVuserdefh12(String newVuserdef6) {
    this.m_vuserdefh12 = newVuserdef6;
  }

  public void setVuserdefh13(String newVuserdef6) {
    this.m_vuserdefh13 = newVuserdef6;
  }

  public void setVuserdefh14(String newVuserdef6) {
    this.m_vuserdefh14 = newVuserdef6;
  }

  public void setVuserdefh15(String newVuserdef6) {
    this.m_vuserdefh15 = newVuserdef6;
  }

  public void setVuserdefh16(String newVuserdef6) {
    this.m_vuserdefh16 = newVuserdef6;
  }

  public void setVuserdefh17(String newVuserdef6) {
    this.m_vuserdefh17 = newVuserdef6;
  }

  public void setVuserdefh18(String newVuserdef6) {
    this.m_vuserdefh18 = newVuserdef6;
  }

  public void setVuserdefh19(String newVuserdef6) {
    this.m_vuserdefh19 = newVuserdef6;
  }

  public void setVuserdefh20(String newVuserdef6) {
    this.m_vuserdefh20 = newVuserdef6;
  }

  public void setPk_defdoch1(String newVuserdef6)
  {
    this.m_Pk_defdoch1 = newVuserdef6;
  }

  public void setPk_defdoch2(String newVuserdef6) {
    this.m_Pk_defdoch2 = newVuserdef6;
  }

  public void setPk_defdoch3(String newVuserdef6) {
    this.m_Pk_defdoch3 = newVuserdef6;
  }

  public void setPk_defdoch4(String newVuserdef6) {
    this.m_Pk_defdoch4 = newVuserdef6;
  }

  public void setPk_defdoch5(String newVuserdef6) {
    this.m_Pk_defdoch5 = newVuserdef6;
  }

  public void setPk_defdoch6(String newVuserdef6) {
    this.m_Pk_defdoch6 = newVuserdef6;
  }

  public void setPk_defdoch7(String newVuserdef6) {
    this.m_Pk_defdoch7 = newVuserdef6;
  }

  public void setPk_defdoch8(String newVuserdef6) {
    this.m_Pk_defdoch8 = newVuserdef6;
  }

  public void setPk_defdoch9(String newVuserdef6) {
    this.m_Pk_defdoch9 = newVuserdef6;
  }

  public void setPk_defdoch10(String newVuserdef6) {
    this.m_Pk_defdoch10 = newVuserdef6;
  }

  public void setPk_defdoch11(String newVuserdef6)
  {
    this.m_Pk_defdoch11 = newVuserdef6;
  }

  public void setPk_defdoch12(String newVuserdef6) {
    this.m_Pk_defdoch12 = newVuserdef6;
  }

  public void setPk_defdoch13(String newVuserdef6) {
    this.m_Pk_defdoch13 = newVuserdef6;
  }

  public void setPk_defdoch14(String newVuserdef6) {
    this.m_Pk_defdoch14 = newVuserdef6;
  }

  public void setPk_defdoch15(String newVuserdef6) {
    this.m_Pk_defdoch15 = newVuserdef6;
  }

  public void setPk_defdoch16(String newVuserdef6) {
    this.m_Pk_defdoch16 = newVuserdef6;
  }

  public void setPk_defdoch17(String newVuserdef6) {
    this.m_Pk_defdoch17 = newVuserdef6;
  }

  public void setPk_defdoch18(String newVuserdef6) {
    this.m_Pk_defdoch18 = newVuserdef6;
  }

  public void setPk_defdoch19(String newVuserdef6) {
    this.m_Pk_defdoch19 = newVuserdef6;
  }

  public void setPk_defdoch20(String newVuserdef6) {
    this.m_Pk_defdoch20 = newVuserdef6;
  }

  public void setCcustomerid(String newCcustomerid) {
	// TODO Auto-generated method stub
	this.m_ccustomerid = newCcustomerid;
  }
  public void setPk_cubasdoc(String newPk_cubasdoc) {
	  // TODO Auto-generated method stub
	  this.m_pk_cubasdoc = newPk_cubasdoc;
  }
}