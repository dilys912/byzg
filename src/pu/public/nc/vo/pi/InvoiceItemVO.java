package nc.vo.pi;

import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.FieldObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.field.pu.FieldDBValidateInterface;
import nc.vo.scm.field.pu.FieldDBValidateVO;
import nc.vo.scm.field.pu.MoneyField;
import nc.vo.scm.field.pu.NumField;
import nc.vo.scm.field.pu.PriceField;
import nc.vo.scm.field.pu.TaxRateField;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.relacal.IRelaCalInfos;

public class InvoiceItemVO extends CircularlyAccessibleValueObject
  implements FieldDBValidateInterface, IRelaCalInfos
{
  private String m_cinvoice_bid;
  private String m_cinvoiceid;
  private String m_pk_corp;
  private String m_cusedeptid;
  private String m_corder_bid;
  private String m_corderid;
  private String m_cupsourcebilltype;
  private String m_cupsourcebillid;
  private String m_cupsourcebillrowid;
  private UFDouble m_ninvoicenum;
  private UFDouble m_naccumsettnum;
  private Integer m_idiscounttaxtype;
  private UFDouble m_ntaxrate;
  private String m_ccurrencytypeid;
  private UFDouble m_noriginalcurprice;
  private UFDouble m_noriginaltaxmny;
  private UFDouble m_noriginalcurmny;
  private UFDouble m_noriginalsummny;
  private UFDouble m_noriginalpaymentmny;
  private UFDouble m_nexchangeotobrate;
  private UFDouble m_nmoney;
  private UFDouble m_ntaxmny;
  private UFDouble m_nsummny;
  private UFDouble m_npaymentmny;
  private UFDouble m_naccumsettmny;
  private UFDouble m_nexchangeotoarate;
  private UFDouble m_nassistcurmny;
  private UFDouble m_nassisttaxmny;
  private UFDouble m_nassistsummny;
  private UFDouble m_nassistpaymny;
  private UFDouble m_nassistsettmny;
  private String m_cprojectid;
  private String m_cprojectphaseid;
  private String m_vmemo;
  private String m_vdef1;
  private String m_vdef2;
  private String m_vdef3;
  private String m_vdef4;
  private String m_vdef5;
  private String m_vdef6;
  public String m_vdef7;
  public String m_vdef8;
  public String m_vdef9;
  public String m_vdef10;
  public String m_vdef11;
  public String m_vdef12;
  public String m_vdef13;
  public String m_vdef14;
  public String m_vdef15;
  public String m_vdef16;
  public String m_vdef17;
  public String m_vdef18;
  public String m_vdef19;
  public String m_vdef20;
  public String m_pk_defdoc1;
  public String m_pk_defdoc2;
  public String m_pk_defdoc3;
  public String m_pk_defdoc4;
  public String m_pk_defdoc5;
  public String m_pk_defdoc6;
  public String m_pk_defdoc7;
  public String m_pk_defdoc8;
  public String m_pk_defdoc9;
  public String m_pk_defdoc10;
  public String m_pk_defdoc11;
  public String m_pk_defdoc12;
  public String m_pk_defdoc13;
  public String m_pk_defdoc14;
  public String m_pk_defdoc15;
  public String m_pk_defdoc16;
  public String m_pk_defdoc17;
  public String m_pk_defdoc18;
  public String m_pk_defdoc19;
  public String m_pk_defdoc20;
  private String m_vfree1;
  private String m_vfree2;
  private String m_vfree3;
  private String m_vfree4;
  private String m_vfree5;
  private UFDouble m_nplanprice;
  private String m_invcode;
  private String m_invname;
  private String m_invtype;
  private String m_invspec;
  private UFDouble m_nreasonwastenum;
  private String m_pk_upsrccorp;
  private String m_cbaseid;
  private String m_cmangid;
  private String m_csourcebillid;
  private String m_csourcebillrowid;
  private String m_csourcebilltype;
  private Integer m_dr;
  private int m_nShowRow = -1;
  private String m_vfree0;
  private UFDouble m_convertrate;
  private String m_crowno;
  private String m_csale_bid;
  private String m_cupsourcebts;
  private String m_cupsourcehts;
  private String m_cwarehouseid;
  private UFDouble m_norgnettaxprice;
  private String m_ts;
  private String m_vproducenum;
  private String b_cjje1;
  private String b_cjje2;
  private String b_cjje3;

  
  public InvoiceItemVO()
  {
  }

  public InvoiceItemVO(String newCinvoice_bid)
  {
    this.m_cinvoice_bid = newCinvoice_bid;
  }

  public String getVdef11()
  {
    return this.m_vdef11;
  }

  public String getVdef12()
  {
    return this.m_vdef12;
  }

  public String getVdef13()
  {
    return this.m_vdef13;
  }

  public String getVdef14()
  {
    return this.m_vdef14;
  }

  public String getVdef15()
  {
    return this.m_vdef15;
  }

  public String getVdef16()
  {
    return this.m_vdef16;
  }

  public String getVdef17()
  {
    return this.m_vdef17;
  }

  public String getVdef18()
  {
    return this.m_vdef18;
  }

  public String getVdef19()
  {
    return this.m_vdef19;
  }

  public String getVdef20()
  {
    return this.m_vdef20;
  }
  public String getB_cjje1() {
		return b_cjje1;
	}

	public void setB_cjje1(String b_cjje1) {
		this.b_cjje1 = b_cjje1;
	}

	public String getB_cjje2() {
		return b_cjje2;
	}

	public void setB_cjje2(String b_cjje2) {
		this.b_cjje2 = b_cjje2;
	}

	public String getB_cjje3() {
		return b_cjje3;
	}

	public void setB_cjje3(String b_cjje3) {
		this.b_cjje3 = b_cjje3;
	}

  public static final String[] getDbMnyFields_Org_Busi()
  {
    return new String[] { 
      "noriginalcurmny", 
      "noriginaltaxmny", 
      "noriginalsummny" };
  }

  public static final String[] getDbMnyFields_Org_Fina()
  {
    return new String[] { 
      "noriginalpaymentmny" };
  }

  public static final String[] getDbMnyFields_Local_Busi()
  {
    return new String[] { 
      "nmoney", 
      "ntaxmny", 
      "nsummny" };
  }

  public static final String[] getDbMnyFields_Local_Fina()
  {
    return new String[] { 
      "npaymentmny" };
  }

  public static final String[] getDbMnyFields_Assist_Busi()
  {
    return new String[] { 
      "nassistcurmny", 
      "nassisttaxmny", 
      "nassistsummny" };
  }

  public static final String[] getDbMnyFields_Assist_Fina()
  {
    return new String[] { 
      "nassistpaymny" };
  }

  public static String getPriceFieldByPricePolicy(int iPolicy)
  {
    if (iPolicy == 5)
    {
      return "norgtaxprice";
    }if (iPolicy == 6)
    {
      return "noriginalcurprice";
    }

    return null;
  }

  public String getPKDefDoc1()
  {
    return this.m_pk_defdoc1;
  }

  public String getPKDefDoc2()
  {
    return this.m_pk_defdoc2;
  }

  public String getPKDefDoc3()
  {
    return this.m_pk_defdoc3;
  }

  public String getPKDefDoc4()
  {
    return this.m_pk_defdoc4;
  }

  public String getPKDefDoc5()
  {
    return this.m_pk_defdoc5;
  }

  public String getPKDefDoc6()
  {
    return this.m_pk_defdoc6;
  }

  public String getPKDefDoc7()
  {
    return this.m_pk_defdoc7;
  }

  public String getPKDefDoc8()
  {
    return this.m_pk_defdoc8;
  }

  public String getPKDefDoc9()
  {
    return this.m_pk_defdoc9;
  }

  public String getPKDefDoc10()
  {
    return this.m_pk_defdoc10;
  }

  public String getPKDefDoc11()
  {
    return this.m_pk_defdoc11;
  }

  public String getPKDefDoc12()
  {
    return this.m_pk_defdoc12;
  }

  public String getPKDefDoc13()
  {
    return this.m_pk_defdoc13;
  }

  public String getPKDefDoc14()
  {
    return this.m_pk_defdoc14;
  }

  public String getPKDefDoc15()
  {
    return this.m_pk_defdoc15;
  }

  public String getPKDefDoc16()
  {
    return this.m_pk_defdoc16;
  }

  public String getPKDefDoc17()
  {
    return this.m_pk_defdoc17;
  }

  public String getPKDefDoc18()
  {
    return this.m_pk_defdoc18;
  }

  public String getPKDefDoc19()
  {
    return this.m_pk_defdoc19;
  }

  public String getPKDefDoc20()
  {
    return this.m_pk_defdoc20;
  }

  public void setVdef11(String newVdef11)
  {
    this.m_vdef11 = newVdef11;
  }

  public void setVdef12(String newVdef12)
  {
    this.m_vdef12 = newVdef12;
  }

  public void setVdef13(String newVdef13)
  {
    this.m_vdef13 = newVdef13;
  }

  public void setVdef14(String newVdef14)
  {
    this.m_vdef14 = newVdef14;
  }

  public void setVdef15(String newVdef15)
  {
    this.m_vdef15 = newVdef15;
  }

  public void setVdef16(String newVdef16)
  {
    this.m_vdef16 = newVdef16;
  }

  public void setVdef17(String newVdef17)
  {
    this.m_vdef17 = newVdef17;
  }

  public void setVdef18(String newVdef18)
  {
    this.m_vdef18 = newVdef18;
  }

  public void setVdef19(String newVdef19)
  {
    this.m_vdef19 = newVdef19;
  }

  public void setVdef20(String newVdef20)
  {
    this.m_vdef20 = newVdef20;
  }

  public void setPKDefDoc1(String newM_pk_defdoc1)
  {
    this.m_pk_defdoc1 = newM_pk_defdoc1;
  }

  public void setPKDefDoc2(String newM_pk_defdoc2)
  {
    this.m_pk_defdoc2 = newM_pk_defdoc2;
  }

  public void setPKDefDoc3(String newM_pk_defdoc3)
  {
    this.m_pk_defdoc3 = newM_pk_defdoc3;
  }

  public void setPKDefDoc4(String newM_pk_defdoc4)
  {
    this.m_pk_defdoc4 = newM_pk_defdoc4;
  }

  public void setPKDefDoc5(String newM_pk_defdoc5)
  {
    this.m_pk_defdoc5 = newM_pk_defdoc5;
  }

  public void setPKDefDoc6(String newM_pk_defdoc6)
  {
    this.m_pk_defdoc6 = newM_pk_defdoc6;
  }

  public void setPKDefDoc7(String newM_pk_defdoc7)
  {
    this.m_pk_defdoc7 = newM_pk_defdoc7;
  }

  public void setPKDefDoc8(String newM_pk_defdoc8)
  {
    this.m_pk_defdoc8 = newM_pk_defdoc8;
  }

  public void setPKDefDoc9(String newM_pk_defdoc9)
  {
    this.m_pk_defdoc9 = newM_pk_defdoc9;
  }

  public void setPKDefDoc10(String newM_pk_defdoc10)
  {
    this.m_pk_defdoc10 = newM_pk_defdoc10;
  }

  public void setPKDefDoc11(String newM_pk_defdoc11)
  {
    this.m_pk_defdoc11 = newM_pk_defdoc11;
  }

  public void setPKDefDoc12(String newM_pk_defdoc12)
  {
    this.m_pk_defdoc12 = newM_pk_defdoc12;
  }

  public void setPKDefDoc13(String newM_pk_defdoc13)
  {
    this.m_pk_defdoc13 = newM_pk_defdoc13;
  }

  public void setPKDefDoc14(String newM_pk_defdoc14)
  {
    this.m_pk_defdoc14 = newM_pk_defdoc14;
  }

  public void setPKDefDoc15(String newM_pk_defdoc15)
  {
    this.m_pk_defdoc15 = newM_pk_defdoc15;
  }

  public void setPKDefDoc16(String newM_pk_defdoc16)
  {
    this.m_pk_defdoc16 = newM_pk_defdoc16;
  }

  public void setPKDefDoc17(String newM_pk_defdoc17)
  {
    this.m_pk_defdoc17 = newM_pk_defdoc17;
  }

  public void setPKDefDoc18(String newM_pk_defdoc18)
  {
    this.m_pk_defdoc18 = newM_pk_defdoc18;
  }

  public void setPKDefDoc19(String newM_pk_defdoc19)
  {
    this.m_pk_defdoc19 = newM_pk_defdoc19;
  }

  public void setPKDefDoc20(String newM_pk_defdoc20)
  {
    this.m_pk_defdoc20 = newM_pk_defdoc20;
  }

  public Object clone()
  {
    Object o = null;
    try {
      o = super.clone(); } catch (Exception localException) {
    }
    InvoiceItemVO invoiceB = (InvoiceItemVO)o;

    for (int i = 0; i < getAttributeNames().length; i++) {
      invoiceB.setAttributeValue(getAttributeNames()[i], getAttributeValue(getAttributeNames()[i]));
    }

    return invoiceB;
  }

  public String getEntityName()
  {
    return "InvoiceB";
  }

  public String getPrimaryKey()
  {
    return this.m_cinvoice_bid;
  }

  public void setPrimaryKey(String newCinvoice_bid)
  {
    this.m_cinvoice_bid = newCinvoice_bid;
  }

  public String getCinvoice_bid()
  {
    return this.m_cinvoice_bid;
  }

  public String getCinvoiceid()
  {
    return this.m_cinvoiceid;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public String getCusedeptid()
  {
    return this.m_cusedeptid;
  }

  public String getCorder_bid()
  {
    return this.m_corder_bid;
  }

  public String getCorderid()
  {
    return this.m_corderid;
  }

  public String getCupsourcebilltype()
  {
    return this.m_cupsourcebilltype;
  }

  public String getCupsourcebillid()
  {
    return this.m_cupsourcebillid;
  }

  public String getCupsourcebillrowid()
  {
    return this.m_cupsourcebillrowid;
  }

  public UFDouble getNinvoicenum()
  {
    return this.m_ninvoicenum;
  }
  public UFDouble getNreasonwastenum() {
    return this.m_nreasonwastenum;
  }

  public UFDouble getNaccumsettnum()
  {
    return this.m_naccumsettnum;
  }

  public Integer getIdiscounttaxtype()
  {
    return this.m_idiscounttaxtype;
  }

  public UFDouble getNtaxrate()
  {
    return this.m_ntaxrate;
  }

  public String getCcurrencytypeid()
  {
    return this.m_ccurrencytypeid;
  }

  public UFDouble getNoriginalcurprice()
  {
    return this.m_noriginalcurprice;
  }

  public UFDouble getNoriginaltaxmny()
  {
    return this.m_noriginaltaxmny;
  }

  public UFDouble getNoriginalcurmny()
  {
    return this.m_noriginalcurmny;
  }

  public UFDouble getNoriginalsummny()
  {
    return this.m_noriginalsummny;
  }

  public UFDouble getNoriginalpaymentmny()
  {
    return this.m_noriginalpaymentmny;
  }

  public UFDouble getNexchangeotobrate()
  {
    return this.m_nexchangeotobrate;
  }

  public UFDouble getNmoney()
  {
    return this.m_nmoney;
  }

  public UFDouble getNtaxmny()
  {
    return this.m_ntaxmny;
  }

  public UFDouble getNsummny()
  {
    return this.m_nsummny;
  }

  public UFDouble getNpaymentmny()
  {
    return this.m_npaymentmny;
  }

  public UFDouble getNaccumsettmny()
  {
    return this.m_naccumsettmny;
  }

  public UFDouble getNexchangeotoarate()
  {
    return this.m_nexchangeotoarate;
  }

  public UFDouble getNassistcurmny()
  {
    return this.m_nassistcurmny;
  }

  public UFDouble getNassisttaxmny()
  {
    return this.m_nassisttaxmny;
  }

  public UFDouble getNassistsummny()
  {
    return this.m_nassistsummny;
  }

  public UFDouble getNassistpaymny()
  {
    return this.m_nassistpaymny;
  }

  public UFDouble getNassistsettmny()
  {
    return this.m_nassistsettmny;
  }

  public String getCprojectid()
  {
    return this.m_cprojectid;
  }

  public String getCprojectphaseid()
  {
    return this.m_cprojectphaseid;
  }

  public String getVmemo()
  {
    return this.m_vmemo;
  }

  public String getVdef1()
  {
    return this.m_vdef1;
  }

  public String getVdef2()
  {
    return this.m_vdef2;
  }

  public String getVdef3()
  {
    return this.m_vdef3;
  }

  public String getVdef4()
  {
    return this.m_vdef4;
  }

  public String getVdef5()
  {
    return this.m_vdef5;
  }

  public String getVdef6()
  {
    return this.m_vdef6;
  }

  public String getVdef7()
  {
    return this.m_vdef7;
  }

  public String getVdef8()
  {
    return this.m_vdef8;
  }

  public String getVdef9()
  {
    return this.m_vdef9;
  }

  public String getVdef10()
  {
    return this.m_vdef10;
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

  public void setCinvoice_bid(String newCinvoice_bid)
  {
    this.m_cinvoice_bid = newCinvoice_bid;
  }

  public void setCinvoiceid(String newCinvoiceid)
  {
    this.m_cinvoiceid = newCinvoiceid;
  }

  public void setPk_corp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setCusedeptid(String newCusedeptid)
  {
    this.m_cusedeptid = newCusedeptid;
  }

  public void setCorder_bid(String newCorder_bid)
  {
    this.m_corder_bid = newCorder_bid;
  }

  public void setCorderid(String newCorderid)
  {
    this.m_corderid = newCorderid;
  }

  public void setCupsourcebilltype(String newCupsourcebilltype)
  {
    this.m_cupsourcebilltype = newCupsourcebilltype;
  }

  public void setCupsourcebillid(String newCupsourcebillid)
  {
    this.m_cupsourcebillid = newCupsourcebillid;
  }

  public void setCupsourcebillrowid(String newCupsourcebillrowid)
  {
    this.m_cupsourcebillrowid = newCupsourcebillrowid;
  }

  public void setNinvoicenum(UFDouble newNinvoicenum)
  {
    this.m_ninvoicenum = newNinvoicenum;
  }

  public void setNreasonwastenum(UFDouble newNinvoicenum) {
    this.m_nreasonwastenum = newNinvoicenum;
  }

  public void setNaccumsettnum(UFDouble newNaccumsettnum)
  {
    this.m_naccumsettnum = newNaccumsettnum;
  }

  public void setIdiscounttaxtype(Integer newIdiscounttaxtype)
  {
    this.m_idiscounttaxtype = newIdiscounttaxtype;
  }

  public void setNtaxrate(UFDouble newNtaxrate)
  {
    this.m_ntaxrate = newNtaxrate;
  }

  public void setCcurrencytypeid(String newCcurrencytypeid)
  {
    this.m_ccurrencytypeid = newCcurrencytypeid;
  }

  public void setNoriginalcurprice(UFDouble newNoriginalcurprice)
  {
    this.m_noriginalcurprice = newNoriginalcurprice;
  }

  public void setNoriginaltaxmny(UFDouble newNoriginaltaxmny)
  {
    this.m_noriginaltaxmny = newNoriginaltaxmny;
  }

  public void setNoriginalcurmny(UFDouble newNoriginalcurmny)
  {
    this.m_noriginalcurmny = newNoriginalcurmny;
  }

  public void setNoriginalsummny(UFDouble newNoriginalsummny)
  {
    this.m_noriginalsummny = newNoriginalsummny;
  }

  public void setNoriginalpaymentmny(UFDouble newNoriginalpaymentmny)
  {
    this.m_noriginalpaymentmny = newNoriginalpaymentmny;
  }

  public void setNexchangeotobrate(UFDouble newNexchangeotobrate)
  {
    this.m_nexchangeotobrate = newNexchangeotobrate;
  }

  public void setNmoney(UFDouble newNmoney)
  {
    this.m_nmoney = newNmoney;
  }

  public void setNtaxmny(UFDouble newNtaxmny)
  {
    this.m_ntaxmny = newNtaxmny;
  }

  public void setNsummny(UFDouble newNsummny)
  {
    this.m_nsummny = newNsummny;
  }

  public void setNpaymentmny(UFDouble newNpaymentmny)
  {
    this.m_npaymentmny = newNpaymentmny;
  }

  public void setNaccumsettmny(UFDouble newNaccumsettmny)
  {
    this.m_naccumsettmny = newNaccumsettmny;
  }

  public void setNexchangeotoarate(UFDouble newNexchangeotoarate)
  {
    this.m_nexchangeotoarate = newNexchangeotoarate;
  }

  public void setNassistcurmny(UFDouble newNassistcurmny)
  {
    this.m_nassistcurmny = newNassistcurmny;
  }

  public void setNassisttaxmny(UFDouble newNassisttaxmny)
  {
    this.m_nassisttaxmny = newNassisttaxmny;
  }

  public void setNassistsummny(UFDouble newNassistsummny)
  {
    this.m_nassistsummny = newNassistsummny;
  }

  public void setNassistpaymny(UFDouble newNassistpaymny)
  {
    this.m_nassistpaymny = newNassistpaymny;
  }

  public void setNassistsettmny(UFDouble newNassistsettmny)
  {
    this.m_nassistsettmny = newNassistsettmny;
  }

  public void setCprojectid(String newCprojectid)
  {
    this.m_cprojectid = newCprojectid;
  }

  public void setCprojectphaseid(String newCprojectphaseid)
  {
    this.m_cprojectphaseid = newCprojectphaseid;
  }

  public void setVmemo(String newVmemo)
  {
    this.m_vmemo = newVmemo;
  }

  public void setVdef1(String newVdef1)
  {
    this.m_vdef1 = newVdef1;
  }

  public void setVdef2(String newVdef2)
  {
    this.m_vdef2 = newVdef2;
  }

  public void setVdef3(String newVdef3)
  {
    this.m_vdef3 = newVdef3;
  }

  public void setVdef4(String newVdef4)
  {
    this.m_vdef4 = newVdef4;
  }

  public void setVdef5(String newVdef5)
  {
    this.m_vdef5 = newVdef5;
  }

  public void setVdef6(String newVdef6)
  {
    this.m_vdef6 = newVdef6;
  }

  public void setVdef7(String newVdef7)
  {
    this.m_vdef7 = newVdef7;
  }

  public void setVdef8(String newVdef8)
  {
    this.m_vdef8 = newVdef8;
  }

  public void setVdef9(String newVdef9)
  {
    this.m_vdef9 = newVdef9;
  }

  public void setVdef10(String newVdef10)
  {
    this.m_vdef10 = newVdef10;
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

  public void validate()
    throws ValidationException
  {
    validateNull();
    validateInterface();
    validateFree();
    validateOriginalPrice();
  }

  public String[] getAttributeNames()
  {
    return new String[] { 
      "cinvoice_bid", 
      "crowno", 
      "cinvoiceid", 
      "pk_corp", 
      "cusedeptid", 
      "corder_bid", 
      "corderid", 
      "csourcebilltype", 
      "csourcebillid", 
      "csourcebillrowid", 
      "cupsourcebilltype", 
      "cupsourcebillid", 
      "cupsourcebillrowid", 
      "cmangid", 
      "cbaseid", 
      "ninvoicenum", 
      "naccumsettnum", 
      "idiscounttaxtype", 
      "ntaxrate", 
      "ccurrencytypeid", 
      "noriginalcurprice", 
      "noriginaltaxmny", 
      "noriginalcurmny", 
      "norgnettaxprice", 
      "noriginalsummny", 
      "noriginalpaymentmny", 
      "nexchangeotobrate", 
      "nmoney", 
      "ntaxmny", 
      "nsummny", 
      "npaymentmny", 
      "naccumsettmny", 
      "nexchangeotoarate", 
      "nassistcurmny", 
      "nassisttaxmny", 
      "nassistsummny", 
      "nassistpaymny", 
      "nassistsettmny", 
      "cprojectid", 
      "cprojectphaseid", 
      "cwarehouseid", 
      "vproducenum", 
      "vmemo", 
      "vfree1", 
      "vfree2", 
      "vfree3", 
      "vfree4", 
      "vfree5", 
      "vdef1", 
      "vdef2", 
      "vdef3", 
      "vdef4", 
      "vdef5", 
      "vdef6", 
      "vdef7", 
      "vdef8", 
      "vdef9", 
      "vdef10", 
      "vdef11", 
      "vdef12", 
      "vdef13", 
      "vdef14", 
      "vdef15", 
      "vdef16", 
      "vdef17", 
      "vdef18", 
      "vdef19", 
      "vdef20", 
      "pk_defdoc1", 
      "pk_defdoc2", 
      "pk_defdoc3", 
      "pk_defdoc4", 
      "pk_defdoc5", 
      "pk_defdoc6", 
      "pk_defdoc7", 
      "pk_defdoc8", 
      "pk_defdoc9", 
      "pk_defdoc10", 
      "pk_defdoc11", 
      "pk_defdoc12", 
      "pk_defdoc13", 
      "pk_defdoc14", 
      "pk_defdoc15", 
      "pk_defdoc16", 
      "pk_defdoc17", 
      "pk_defdoc18", 
      "pk_defdoc19", 
      "pk_defdoc20", 
      "dr", 
      "ts", 
      "vfree0", 
      "cupsourcehts", 
      "cupsourcebts", 
      "csale_bid", 
      "convertrate", 
      "nplanprice", 
      "invcode", 
      "invname", 
      "invspec", 
      "invtype", 
      "nreasonwastenum", 
      "pk_upsrccorp",
      "b_cjje1",
      "b_cjje2",
      "b_cjje3"};
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("cinvoice_bid")) {
      return this.m_cinvoice_bid;
    }
    if (attributeName.equals("crowno"))
      return this.m_crowno;
    if (attributeName.equals("cinvoiceid"))
      return this.m_cinvoiceid;
    if (attributeName.equals("pk_corp"))
      return this.m_pk_corp;
    if (attributeName.equals("cusedeptid"))
      return this.m_cusedeptid;
    if (attributeName.equals("corder_bid"))
      return this.m_corder_bid;
    if (attributeName.equals("corderid"))
      return this.m_corderid;
    if (attributeName.equals("csourcebilltype"))
      return this.m_csourcebilltype;
    if (attributeName.equals("csourcebillid"))
      return this.m_csourcebillid;
    if (attributeName.equals("csourcebillrowid"))
      return this.m_csourcebillrowid;
    if (attributeName.equals("cupsourcebilltype"))
      return this.m_cupsourcebilltype;
    if (attributeName.equals("cupsourcebillid"))
      return this.m_cupsourcebillid;
    if (attributeName.equals("cupsourcebillrowid"))
      return this.m_cupsourcebillrowid;
    if (attributeName.equals("cmangid"))
      return this.m_cmangid;
    if (attributeName.equals("cbaseid"))
      return this.m_cbaseid;
    if (attributeName.equals("ninvoicenum"))
      return this.m_ninvoicenum;
    if (attributeName.equals("naccumsettnum"))
      return this.m_naccumsettnum;
    if (attributeName.equals("idiscounttaxtype"))
      return this.m_idiscounttaxtype;
    if (attributeName.equals("ntaxrate"))
      return this.m_ntaxrate;
    if (attributeName.equals("ccurrencytypeid"))
      return this.m_ccurrencytypeid;
    if (attributeName.equals("noriginalcurprice"))
      return this.m_noriginalcurprice;
    if (attributeName.equals("noriginaltaxmny")) {
      return this.m_noriginaltaxmny;
    }

    if (attributeName.equals("norgnettaxprice")) {
      return this.m_norgnettaxprice;
    }

    if (attributeName.equals("noriginalcurmny"))
      return this.m_noriginalcurmny;
    if (attributeName.equals("noriginalsummny"))
      return this.m_noriginalsummny;
    if (attributeName.equals("noriginalpaymentmny"))
      return this.m_noriginalpaymentmny;
    if (attributeName.equals("nexchangeotobrate"))
      return this.m_nexchangeotobrate;
    if (attributeName.equals("nmoney"))
      return this.m_nmoney;
    if (attributeName.equals("ntaxmny"))
      return this.m_ntaxmny;
    if (attributeName.equals("nsummny"))
      return this.m_nsummny;
    if (attributeName.equals("npaymentmny"))
      return this.m_npaymentmny;
    if (attributeName.equals("naccumsettmny"))
      return this.m_naccumsettmny;
    if (attributeName.equals("nexchangeotoarate"))
      return this.m_nexchangeotoarate;
    if (attributeName.equals("nassistcurmny"))
      return this.m_nassistcurmny;
    if (attributeName.equals("nassisttaxmny"))
      return this.m_nassisttaxmny;
    if (attributeName.equals("nassistsummny"))
      return this.m_nassistsummny;
    if (attributeName.equals("nassistpaymny"))
      return this.m_nassistpaymny;
    if (attributeName.equals("nassistsettmny"))
      return this.m_nassistsettmny;
    if (attributeName.equals("cprojectid"))
      return this.m_cprojectid;
    if (attributeName.equals("cprojectphaseid")) {
      return this.m_cprojectphaseid;
    }

    if (attributeName.equals("cwarehouseid"))
      return this.m_cwarehouseid;
    if (attributeName.equals("vproducenum")) {
      return this.m_vproducenum;
    }

    if (attributeName.equals("vmemo"))
      return this.m_vmemo;
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
    if (attributeName.equals("vdef1"))
      return this.m_vdef1;
    if (attributeName.equals("vdef2"))
      return this.m_vdef2;
    if (attributeName.equals("vdef3"))
      return this.m_vdef3;
    if (attributeName.equals("vdef4"))
      return this.m_vdef4;
    if (attributeName.equals("vdef5"))
      return this.m_vdef5;
    if (attributeName.equals("vdef6"))
      return this.m_vdef6;
    if (attributeName.equals("vdef7"))
      return this.m_vdef7;
    if (attributeName.equals("vdef8"))
      return this.m_vdef8;
    if (attributeName.equals("vdef9"))
      return this.m_vdef9;
    if (attributeName.equals("vdef10"))
      return this.m_vdef10;
    if (attributeName.equals("vdef11"))
      return this.m_vdef11;
    if (attributeName.equals("vdef12"))
      return this.m_vdef12;
    if (attributeName.equals("vdef13"))
      return this.m_vdef13;
    if (attributeName.equals("vdef14"))
      return this.m_vdef14;
    if (attributeName.equals("vdef15"))
      return this.m_vdef15;
    if (attributeName.equals("vdef16"))
      return this.m_vdef16;
    if (attributeName.equals("vdef17"))
      return this.m_vdef17;
    if (attributeName.equals("vdef18"))
      return this.m_vdef18;
    if (attributeName.equals("vdef19"))
      return this.m_vdef19;
    if (attributeName.equals("vdef20"))
      return this.m_vdef20;
    if (attributeName.equals("pk_defdoc1"))
      return this.m_pk_defdoc1;
    if (attributeName.equals("pk_defdoc2"))
      return this.m_pk_defdoc2;
    if (attributeName.equals("pk_defdoc3"))
      return this.m_pk_defdoc3;
    if (attributeName.equals("pk_defdoc4"))
      return this.m_pk_defdoc4;
    if (attributeName.equals("pk_defdoc5"))
      return this.m_pk_defdoc5;
    if (attributeName.equals("pk_defdoc6"))
      return this.m_pk_defdoc6;
    if (attributeName.equals("pk_defdoc7"))
      return this.m_pk_defdoc7;
    if (attributeName.equals("pk_defdoc8"))
      return this.m_pk_defdoc8;
    if (attributeName.equals("pk_defdoc9"))
      return this.m_pk_defdoc9;
    if (attributeName.equals("pk_defdoc10"))
      return this.m_pk_defdoc10;
    if (attributeName.equals("pk_defdoc11"))
      return this.m_pk_defdoc11;
    if (attributeName.equals("pk_defdoc12"))
      return this.m_pk_defdoc12;
    if (attributeName.equals("pk_defdoc13"))
      return this.m_pk_defdoc13;
    if (attributeName.equals("pk_defdoc14"))
      return this.m_pk_defdoc14;
    if (attributeName.equals("pk_defdoc15"))
      return this.m_pk_defdoc15;
    if (attributeName.equals("pk_defdoc16"))
      return this.m_pk_defdoc16;
    if (attributeName.equals("pk_defdoc17"))
      return this.m_pk_defdoc17;
    if (attributeName.equals("pk_defdoc18"))
      return this.m_pk_defdoc18;
    if (attributeName.equals("pk_defdoc19"))
      return this.m_pk_defdoc19;
    if (attributeName.equals("pk_defdoc20"))
      return this.m_pk_defdoc20;
    if (attributeName.equals("dr"))
      return this.m_dr;
    if (attributeName.equals("ts"))
      return this.m_ts;
    if (attributeName.equals("vfree0"))
      return this.m_vfree0;
    if (attributeName.equals("cupsourcehts"))
      return this.m_cupsourcehts;
    if (attributeName.equals("cupsourcebts"))
      return this.m_cupsourcebts;
    if (attributeName.equals("csale_bid"))
      return this.m_csale_bid;
    if (attributeName.equals("convertrate"))
      return this.m_convertrate;
    if (attributeName.equals("nplanprice"))
      return this.m_nplanprice;
    if (attributeName.equals("invcode"))
      return this.m_invcode;
    if (attributeName.equals("invname"))
      return this.m_invname;
    if (attributeName.equals("invspec"))
      return this.m_invspec;
    if (attributeName.equals("invtype"))
      return this.m_invtype;
    if (attributeName.equals("nreasonwastenum"))
      return this.m_nreasonwastenum;
    if (attributeName.equals("pk_upsrccorp")) {
      return this.m_pk_upsrccorp;
    }
    if(attributeName.equals("b_cjje1")){
    	return this.b_cjje1;
    }
    if(attributeName.equals("b_cjje2")){
    	return this.b_cjje2;
    }
    if(attributeName.equals("b_cjje3")){
    	return this.b_cjje3;
    }

    return null;
  }

  public void setAttributeValue(String name, Object value)
  {
    try
    {
      if (name.equals("cinvoice_bid"))
        this.m_cinvoice_bid = ((String)value);
      else if (name.equals("crowno"))
        this.m_crowno = ((String)value);
      else if (name.equals("cinvoiceid"))
        this.m_cinvoiceid = ((String)value);
      else if (name.equals("pk_corp"))
        this.m_pk_corp = ((String)value);
      else if (name.equals("cusedeptid"))
        this.m_cusedeptid = ((String)value);
      else if (name.equals("corder_bid"))
        this.m_corder_bid = ((String)value);
      else if (name.equals("corderid"))
        this.m_corderid = ((String)value);
      else if (name.equals("csourcebilltype"))
        this.m_csourcebilltype = ((String)value);
      else if (name.equals("csourcebillid"))
        this.m_csourcebillid = ((String)value);
      else if (name.equals("csourcebillrowid"))
        this.m_csourcebillrowid = ((String)value);
      else if (name.equals("cupsourcebilltype"))
        this.m_cupsourcebilltype = ((String)value);
      else if (name.equals("cupsourcebillid"))
        this.m_cupsourcebillid = ((String)value);
      else if (name.equals("cupsourcebillrowid"))
        this.m_cupsourcebillrowid = ((String)value);
      else if (name.equals("cmangid"))
        this.m_cmangid = ((String)value);
      else if (name.equals("cbaseid"))
        this.m_cbaseid = ((String)value);
      else if (name.equals("ninvoicenum"))
        this.m_ninvoicenum = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("naccumsettnum"))
        this.m_naccumsettnum = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("idiscounttaxtype"))
        this.m_idiscounttaxtype = ((Integer)value);
      else if (name.equals("ntaxrate"))
        this.m_ntaxrate = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("ccurrencytypeid"))
        this.m_ccurrencytypeid = ((String)value);
      else if (name.equals("noriginalcurprice"))
        this.m_noriginalcurprice = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("noriginaltaxmny")) {
        this.m_noriginaltaxmny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      }
      else if (name.equals("norgnettaxprice")) {
        this.m_norgnettaxprice = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      }
      else if (name.equals("noriginalcurmny"))
        this.m_noriginalcurmny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("noriginalsummny"))
        this.m_noriginalsummny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("noriginalpaymentmny"))
        this.m_noriginalpaymentmny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("nexchangeotobrate"))
        this.m_nexchangeotobrate = ((value == null) || (value.toString().trim().equals("")) ? null : new UFDouble(value.toString()));
      else if (name.equals("nmoney"))
        this.m_nmoney = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("ntaxmny"))
        this.m_ntaxmny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("nsummny"))
        this.m_nsummny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("npaymentmny"))
        this.m_npaymentmny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("naccumsettmny"))
        this.m_naccumsettmny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("nexchangeotoarate"))
        this.m_nexchangeotoarate = ((value == null) || (value.toString().trim().equals("")) ? null : new UFDouble(value.toString()));
      else if (name.equals("nassistcurmny"))
        this.m_nassistcurmny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("nassisttaxmny"))
        this.m_nassisttaxmny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("nassistsummny"))
        this.m_nassistsummny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("nassistpaymny"))
        this.m_nassistpaymny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("nassistsettmny"))
        this.m_nassistsettmny = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("cprojectid"))
        this.m_cprojectid = ((String)value);
      else if (name.equals("cprojectphaseid")) {
        this.m_cprojectphaseid = ((String)value);
      }
      else if (name.equals("cwarehouseid"))
        this.m_cwarehouseid = ((String)value);
      else if (name.equals("vproducenum")) {
        this.m_vproducenum = ((String)value);
      }
      else if (name.equals("vmemo"))
      {
        this.m_vmemo = (value == null ? "" : (String)value);
      } else if (name.equals("invcode"))
        this.m_invcode = ((String)value);
      else if (name.equals("invname"))
        this.m_invname = ((String)value);
      else if (name.equals("invspec"))
        this.m_invspec = ((String)value);
      else if (name.equals("invtype"))
        this.m_invtype = ((String)value);
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
      else if (name.equals("vdef1"))
        this.m_vdef1 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef2"))
        this.m_vdef2 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef3"))
        this.m_vdef3 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef4"))
        this.m_vdef4 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef5"))
        this.m_vdef5 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef6"))
        this.m_vdef6 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef7"))
        this.m_vdef7 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef8"))
        this.m_vdef8 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef9"))
        this.m_vdef9 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef10"))
        this.m_vdef10 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef11"))
        this.m_vdef11 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef12"))
        this.m_vdef12 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef13"))
        this.m_vdef13 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef14"))
        this.m_vdef14 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef15"))
        this.m_vdef15 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef16"))
        this.m_vdef16 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef17"))
        this.m_vdef17 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef18"))
        this.m_vdef18 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef19"))
        this.m_vdef19 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("vdef20"))
        this.m_vdef20 = ((value == null) || (value.toString().trim().length() == 0) ? null : value.toString());
      else if (name.equals("pk_defdoc1"))
        this.m_pk_defdoc1 = ((String)value);
      else if (name.equals("pk_defdoc2"))
        this.m_pk_defdoc2 = ((String)value);
      else if (name.equals("pk_defdoc3"))
        this.m_pk_defdoc3 = ((String)value);
      else if (name.equals("pk_defdoc4"))
        this.m_pk_defdoc4 = ((String)value);
      else if (name.equals("pk_defdoc5"))
        this.m_pk_defdoc5 = ((String)value);
      else if (name.equals("pk_defdoc6"))
        this.m_pk_defdoc6 = ((String)value);
      else if (name.equals("pk_defdoc7"))
        this.m_pk_defdoc7 = ((String)value);
      else if (name.equals("pk_defdoc8"))
        this.m_pk_defdoc8 = ((String)value);
      else if (name.equals("pk_defdoc9"))
        this.m_pk_defdoc9 = ((String)value);
      else if (name.equals("pk_defdoc10"))
        this.m_pk_defdoc10 = ((String)value);
      else if (name.equals("pk_defdoc11"))
        this.m_pk_defdoc11 = ((String)value);
      else if (name.equals("pk_defdoc12"))
        this.m_pk_defdoc12 = ((String)value);
      else if (name.equals("pk_defdoc13"))
        this.m_pk_defdoc13 = ((String)value);
      else if (name.equals("pk_defdoc14"))
        this.m_pk_defdoc14 = ((String)value);
      else if (name.equals("pk_defdoc15"))
        this.m_pk_defdoc15 = ((String)value);
      else if (name.equals("pk_defdoc16"))
        this.m_pk_defdoc16 = ((String)value);
      else if (name.equals("pk_defdoc17"))
        this.m_pk_defdoc17 = ((String)value);
      else if (name.equals("pk_defdoc18"))
        this.m_pk_defdoc18 = ((String)value);
      else if (name.equals("pk_defdoc19"))
        this.m_pk_defdoc19 = ((String)value);
      else if (name.equals("pk_defdoc20"))
        this.m_pk_defdoc20 = ((String)value);
      else if (name.equals("dr"))
        this.m_dr = ((value == null) || (value.toString().trim().equals("")) || (value.toString().trim().equals("")) ? new Integer(0) : (Integer)value);
      else if (name.equals("ts"))
        this.m_ts = ((String)value);
      else if (name.equals("vfree0"))
        this.m_vfree0 = ((String)value);
      else if (name.equals("cupsourcehts"))
        this.m_cupsourcehts = ((String)value);
      else if (name.equals("cupsourcebts"))
        this.m_cupsourcebts = ((String)value);
      else if (name.equals("csale_bid"))
        this.m_csale_bid = ((String)value);
      else if (name.equals("convertrate"))
        this.m_convertrate = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("nplanprice"))
        this.m_nplanprice = (value == null ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("nreasonwastenum"))
        this.m_nreasonwastenum = ((value == null) || (value.toString().trim().equals("")) ? new UFDouble(0.0D) : new UFDouble(value.toString()));
      else if (name.equals("pk_upsrccorp"))
        this.m_pk_upsrccorp = ((String)value);
      else if (name.equals("b_cjje1"))
    	this.b_cjje1 = ((String)value);
      else if (name.equals("b_cjje2"))
      	this.b_cjje2 = ((String)value);
      else if (name.equals("b_cjje3"))
      	this.b_cjje3 = ((String)value);
    }
    catch (ClassCastException e) {
      throw new ClassCastException("setAttributeValue方法中为 " + name + " 赋值时类型转换错误！（值：" + value + "）");
    }
  }

  public FieldObject[] getFields()
  {
    return null;
  }

  public String getCbaseid()
  {
    return this.m_cbaseid;
  }

  public String getCmangid()
  {
    return this.m_cmangid;
  }

  public String getCsourcebillid()
  {
    return this.m_csourcebillid;
  }

  public String getCsourcebillrowid()
  {
    return this.m_csourcebillrowid;
  }

  public String getCsourcebilltype()
  {
    return this.m_csourcebilltype;
  }

  public void setCbaseid(String newCbaseid)
  {
    this.m_cbaseid = newCbaseid;
  }

  public void setCmangid(String newCmangid)
  {
    this.m_cmangid = newCmangid;
  }

  public void setCsourcebillid(String newCsourcebillid)
  {
    this.m_csourcebillid = newCsourcebillid;
  }

  public void setCsourcebillrowid(String newCsourcebillrowid)
  {
    this.m_csourcebillrowid = newCsourcebillrowid;
  }

  public void setCsourcebilltype(String newCsourcebilltype)
  {
    this.m_csourcebilltype = newCsourcebilltype;
  }

  public Integer getDr()
  {
    return this.m_dr;
  }

  public void setDr(Integer newDr)
  {
    this.m_dr = newDr;
  }

  public int getNShowRow()
  {
    return this.m_nShowRow;
  }

  public String getVfree0()
  {
    return this.m_vfree0;
  }

  public void setNShowRow(int newM_nShowRow)
  {
    this.m_nShowRow = newM_nShowRow;
  }

  public void setVfree0(String newVfree0)
  {
    this.m_vfree0 = newVfree0;
  }

  private void validateFree()
    throws ValidationException
  {
    if (((this.m_vfree1 != null) && (this.m_vfree1.trim().length() > 100)) || 
      ((this.m_vfree2 != null) && (this.m_vfree2.trim().length() > 100)) || 
      ((this.m_vfree3 != null) && (this.m_vfree3.trim().length() > 100)) || 
      ((this.m_vfree4 != null) && (this.m_vfree4.trim().length() > 100)) || (
      (this.m_vfree5 != null) && (this.m_vfree5.trim().length() > 100)))
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000201"));
  }

  private void validateInterface()
    throws ValidationException
  {
    String strValid = "";
    if ((this.m_ntaxrate != null) && (this.m_ntaxrate.doubleValue() < 0.0D)) {
      strValid = strValid + NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000202");
    }

    if (this.m_noriginalcurmny.doubleValue() == 0.0D) {
      if (strValid.length() > 1)
        strValid = strValid + NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000203");
      else {
        strValid = strValid + NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000204");
      }

    }

    if ((this.m_noriginalpaymentmny != null) && (this.m_noriginalpaymentmny.doubleValue() != 0.0D)) {
      UFDouble d1 = this.m_noriginalpaymentmny;
      UFDouble d2 = this.m_noriginalsummny == null ? VariableConst.ZERO : this.m_noriginalsummny;
      if (d1.compareTo(d2) > 0) {
        if (strValid.length() > 1)
          strValid = strValid + NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000205");
        else {
          strValid = strValid + NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000206");
        }
      }

    }

    if (strValid.length() > 1) {
      String[] value = { String.valueOf(this.m_nShowRow), strValid };
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000207", null, value));
    }
  }

  private void validateNull()
    throws ValidationException
  {
    StringBuffer errInfo = new StringBuffer();
    if ((this.m_cmangid == null) || (this.m_cmangid.equals(""))) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000208"));
    }
    if (this.m_noriginalcurmny == null) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000209"));
    }

    String sMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000210");
    if (errInfo.toString().length() > 0) {
      sMessage = sMessage + this.m_nShowRow + NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000211");
      sMessage = sMessage + errInfo.toString();

      throw new NullFieldException(sMessage);
    }
  }

  private void validateOriginalPrice()
    throws ValidationException
  {
    if ((this.m_noriginalcurprice != null) && 
      (this.m_noriginalcurprice.doubleValue() < 0.0D))
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000211"));
  }

  public UFDouble getConvertrate()
  {
    return this.m_convertrate;
  }

  public String getCrowno()
  {
    return this.m_crowno;
  }

  public String getCsale_bid()
  {
    return this.m_csale_bid;
  }

  public String getCupsourcebts()
  {
    return this.m_cupsourcebts;
  }

  public String getCupsourcehts()
  {
    return this.m_cupsourcehts;
  }

  public String getCwarehouseid()
  {
    return this.m_cwarehouseid;
  }

  public static String[] getDbFields()
  {
    return new String[] { 
      "cinvoice_bid", 
      "crowno", 
      "cinvoiceid", 
      "pk_corp", 
      "cusedeptid", 
      "corder_bid", 
      "corderid", 
      "csourcebilltype", 
      "csourcebillid", 
      "csourcebillrowid", 
      "cupsourcebilltype", 
      "cupsourcebillid", 
      "cupsourcebillrowid", 
      "cmangid", 
      "cbaseid", 
      "ninvoicenum", 
      "naccumsettnum", 
      "idiscounttaxtype", 
      "ntaxrate", 
      "ccurrencytypeid", 
      "noriginalcurprice", 
      "noriginaltaxmny", 
      "noriginalcurmny", 
      "noriginalsummny", 
      "norgnettaxprice", 
      "noriginalpaymentmny", 
      "nexchangeotobrate", 
      "nmoney", 
      "ntaxmny", 
      "nsummny", 
      "npaymentmny", 
      "naccumsettmny", 
      "nexchangeotoarate", 
      "nassistcurmny", 
      "nassisttaxmny", 
      "nassistsummny", 
      "nassistpaymny", 
      "nassistsettmny", 
      "cprojectid", 
      "cprojectphaseid", 
      "cwarehouseid", 
      "vproducenum", 
      "vmemo", 
      "vfree1", 
      "vfree2", 
      "vfree3", 
      "vfree4", 
      "vfree5", 
      "vdef1", 
      "vdef2", 
      "vdef3", 
      "vdef4", 
      "vdef5", 
      "vdef6", 
      "vdef7", 
      "vdef8", 
      "vdef9", 
      "vdef10", 
      "vdef11", 
      "vdef12", 
      "vdef13", 
      "vdef14", 
      "vdef15", 
      "vdef16", 
      "vdef17", 
      "vdef18", 
      "vdef19", 
      "vdef20", 
      "pk_defdoc1", 
      "pk_defdoc2", 
      "pk_defdoc3", 
      "pk_defdoc4", 
      "pk_defdoc5", 
      "pk_defdoc6", 
      "pk_defdoc7", 
      "pk_defdoc8", 
      "pk_defdoc9", 
      "pk_defdoc10", 
      "pk_defdoc11", 
      "pk_defdoc12", 
      "pk_defdoc13", 
      "pk_defdoc14", 
      "pk_defdoc15", 
      "pk_defdoc16", 
      "pk_defdoc17", 
      "pk_defdoc18", 
      "pk_defdoc19", 
      "pk_defdoc20", 
      "dr", 
      "ts", 
      "nreasonwastenum", 
      "pk_upsrccorp",
      "b_cjje1",
      "b_cjje2",
      "b_cjje3"};
  }

  public FieldDBValidateVO[] getFieldDBValidateVOs()
  {
    return new FieldDBValidateVO[] { 
      new FieldDBValidateVO(
      NumField.class, 
      new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000983") }, 
      new UFDouble[] { this.m_ninvoicenum }), 
      new FieldDBValidateVO(
      PriceField.class, 
      new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000741"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001160") }, 
      new UFDouble[] { this.m_noriginalcurprice, this.m_norgnettaxprice }), 
      new FieldDBValidateVO(
      MoneyField.class, 
      new String[] { 
      NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004112"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003084"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000227"), 
      NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002615"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002613"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002594"), 
      NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003969"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003967"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003948") }, 
      new UFDouble[] { 
      this.m_noriginalcurmny, this.m_noriginaltaxmny, this.m_noriginalsummny, 
      this.m_nmoney, this.m_ntaxmny, this.m_nsummny, 
      this.m_nassistcurmny, this.m_nassisttaxmny, this.m_nassistsummny }), 
      new FieldDBValidateVO(
      TaxRateField.class, 
      new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003078") }, 
      new UFDouble[] { this.m_ntaxrate }) };
  }

  public UFDouble getNorgnettaxprice()
  {
    return this.m_norgnettaxprice;
  }

  public String getTs()
  {
    return this.m_ts;
  }

  public String getVproducenum()
  {
    return this.m_vproducenum;
  }

  public void setConvertrate(UFDouble s)
  {
    this.m_convertrate = s;
  }

  public void setCrowno(String newCrowno)
  {
    this.m_crowno = newCrowno;
  }

  public void setCsale_bid(String newM_csale_bid)
  {
    this.m_csale_bid = newM_csale_bid;
  }

  public void setCupsourcebts(String sNewValue)
  {
    this.m_cupsourcebts = sNewValue;
  }

  public void setCupsourcehts(String sNewValue)
  {
    this.m_cupsourcehts = sNewValue;
  }

  public void setCwarehouseid(String newCwarehouseid)
  {
    this.m_cwarehouseid = newCwarehouseid;
  }

  public void setNorgnettaxprice(UFDouble newNoriginaltaxprice)
  {
    this.m_norgnettaxprice = newNoriginaltaxprice;
  }

  public void setTs(String sNewTs)
  {
    this.m_ts = sNewTs;
  }

  public void setVproducenum(String sValue)
  {
    this.m_vproducenum = sValue;
  }

  public UFDouble getNplanprice()
  {
    return this.m_nplanprice;
  }

  public void setNplanprice(UFDouble m_nplanprice)
  {
    this.m_nplanprice = m_nplanprice;
  }

  public String getPk_upsrccorp()
  {
    return this.m_pk_upsrccorp;
  }

  public void setPk_upsrccorp(String newPk_upsrccorp)
  {
    this.m_pk_upsrccorp = newPk_upsrccorp;
  }

  public String getCorpPk()
  {
    return getPk_corp();
  }

  public String getCurrTypePk()
  {
    return getCcurrencytypeid();
  }
}