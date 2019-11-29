package nc.vo.pi;

import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.ui.pub.ClientEnvironment;
import nc.vo.bd.CorpVO;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.field.pu.FieldDBValidateInterface;
import nc.vo.scm.field.pu.FieldDBValidateVO;
import nc.vo.scm.field.pu.TaxRateField;
import nc.vo.scm.field.pu.ToARateField;
import nc.vo.scm.field.pu.ToBRateField;
import nc.vo.scm.pub.SCMEnv;

public class InvoiceHeaderVO extends CircularlyAccessibleValueObject
  implements FieldDBValidateInterface
{
  private String m_cinvoiceid;
  private String m_pk_corp;
  private String m_vinvoicecode;
  private Integer m_iinvoicetype;
  private String m_cdeptid;
  private String m_cfreecustid;
  private String m_cemployeeid;
  private UFDate m_dinvoicedate;
  private UFDate m_darrivedate;
  private String m_caccountbankid;
  private String m_cpayunit;
  private Integer m_finitflag;
  private String m_cvoucherid;
  private String m_coperator;
  private String m_vmemo;
  private String m_caccountyear;
  private Integer m_ibillstatus;
  private String m_cbilltype;
  private String m_coperatoridnow;
  private String m_vdef1;
  private String m_vdef2;
  private String m_vdef3;
  private String m_vdef4;
  private String m_vdef5;
  private String m_vdef6;
  private String m_vdef7;
  private String m_vdef8;
  private String m_vdef9;
  private String m_vdef10;
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
  private static UFDate ENABLED_DATE = null;
  private String m_pk_purcorp;
  private String m_tmaketime;
  private String m_taudittime;
  private String m_tlastmaketime;
  private Integer m_iprintcount;
  private String m_cauditpsn;
  private String m_cbiztype;
  private String m_ccurrencytypeid;
  private String m_ctermprotocolid;
  private String m_cvendorbaseid;
  private String m_cvendormangid;
  private String m_cvendorphone = null;
  private UFDate m_dauditdate;
  private Integer m_dr;
  private Integer m_idiscounttaxtype = null;
  private UFDouble m_nexchangeotoarate;
  private UFDouble m_nexchangeotobrate;
  private UFDouble m_ntaxrate;
  public static final int DEFINE = 2;
  private String m_cstoreorganization;
  private String m_cuserid;
  private String m_ts;
  public static final int NORMAL = 1;
  public static final int SPECIAL = 0;
  public static final int VIRTUAL = 3;

  public InvoiceHeaderVO()
  {
  }

  public InvoiceHeaderVO(String newCinvoiceid)
  {
    this.m_cinvoiceid = newCinvoiceid;
  }

  public Object clone()
  {
    Object o = null;
    try {
      o = super.clone(); } catch (Exception localException) {
    }
    InvoiceHeaderVO invoice = (InvoiceHeaderVO)o;

    for (int i = 0; i < getAttributeNames().length; ++i) {
      invoice.setAttributeValue(getAttributeNames()[i], getAttributeValue(getAttributeNames()[i]));
    }

    return invoice;
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

  public String getEntityName()
  {
    return "Invoice";
  }

  public String getPrimaryKey()
  {
    return this.m_cinvoiceid;
  }

  public void setPrimaryKey(String newCinvoiceid)
  {
    this.m_cinvoiceid = newCinvoiceid;
  }

  public String getCinvoiceid()
  {
    return this.m_cinvoiceid;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public String getPk_purcorp()
  {
    return this.m_pk_purcorp;
  }

  public void setPk_purcorp(String newPk_purcorp)
  {
    this.m_pk_purcorp = newPk_purcorp;
  }

  public String getVinvoicecode()
  {
    return this.m_vinvoicecode;
  }

  public Integer getIinvoicetype()
  {
    return this.m_iinvoicetype;
  }

  public String getCdeptid()
  {
    return this.m_cdeptid;
  }

  public String getCfreecustid()
  {
    return this.m_cfreecustid;
  }

  public String getCemployeeid()
  {
    return this.m_cemployeeid;
  }

  public UFDate getDinvoicedate()
  {
    return this.m_dinvoicedate;
  }

  public UFDate getDarrivedate()
  {
    return this.m_darrivedate;
  }

  public String getCaccountbankid()
  {
    return this.m_caccountbankid;
  }

  public String getCpayunit()
  {
    return this.m_cpayunit;
  }

  public Integer getFinitflag()
  {
    return this.m_finitflag;
  }

  public String getCvoucherid()
  {
    return this.m_cvoucherid;
  }

  public String getCoperator()
  {
    return this.m_coperator;
  }

  public String getVmemo()
  {
    return this.m_vmemo;
  }

  public String getCaccountyear()
  {
    return this.m_caccountyear;
  }

  public Integer getIbillstatus()
  {
    return this.m_ibillstatus;
  }

  public String getCbilltype()
  {
    return this.m_cbilltype;
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

  public void setCinvoiceid(String newCinvoiceid)
  {
    this.m_cinvoiceid = newCinvoiceid;
  }

  public void setPk_corp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setVinvoicecode(String newVinvoicecode)
  {
    this.m_vinvoicecode = newVinvoicecode;
  }

  public void setIinvoicetype(Integer newIinvoicetype)
  {
    this.m_iinvoicetype = newIinvoicetype;
  }

  public void setCdeptid(String newCdeptid)
  {
    this.m_cdeptid = newCdeptid;
  }

  public void setCfreecustid(String newCfreecustid)
  {
    this.m_cfreecustid = newCfreecustid;
  }

  public void setCemployeeid(String newCemployeeid)
  {
    this.m_cemployeeid = newCemployeeid;
  }

  public void setDinvoicedate(UFDate newDinvoicedate)
  {
    this.m_dinvoicedate = newDinvoicedate;
  }

  public void setDarrivedate(UFDate newDarrivedate)
  {
    this.m_darrivedate = newDarrivedate;
  }

  public void setCaccountbankid(String newCaccountbankid)
  {
    this.m_caccountbankid = newCaccountbankid;
  }

  public void setCpayunit(String newCpayunit)
  {
    this.m_cpayunit = newCpayunit;
  }

  public void setFinitflag(Integer newFinitflag)
  {
    this.m_finitflag = newFinitflag;
  }

  public void setCvoucherid(String newCvoucherid)
  {
    this.m_cvoucherid = newCvoucherid;
  }

  public void setCoperator(String newCoperator)
  {
    this.m_coperator = newCoperator;
  }

  public void setVmemo(String newVmemo)
  {
    this.m_vmemo = newVmemo;
  }

  public void setCaccountyear(String newCaccountyear)
  {
    this.m_caccountyear = newCaccountyear;
  }

  public void setIbillstatus(Integer newIbillstatus)
  {
    this.m_ibillstatus = newIbillstatus;
  }

  public void setCbilltype(String newCbilltype)
  {
    this.m_cbilltype = newCbilltype;
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

  public void validate()
    throws ValidationException
  {
    validateNull();
    validateExchangeRate();
    validateDate();
    validateLength();
  }

  public String[] getAttributeNames()
  {
    return new String[] { 
      "cinvoiceid", 
      "pk_corp", 
      "vinvoicecode", 
      "iinvoicetype", 
      "cdeptid", 
      "cfreecustid", 
      "cstoreorganization", 
      "cvendormangid", 
      "cvendorbaseid", 
      "cemployeeid", 
      "dinvoicedate", 
      "darrivedate", 
      "cbiztype", 
      "caccountbankid", 
      "cpayunit", 
      "finitflag", 
      "cvoucherid", 
      "ctermprotocolid", 
      "coperator", 
      "cuserid", 
      "caccountyear", 
      "cbilltype", 
      "ibillstatus", 
      "dauditdate", 
      "cauditpsn", 
      "vmemo", 
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
      "ccurrencytypeid", 
      "nexchangeotobrate", 
      "nexchangeotoarate", 
      "coperatoridnow", 
      "ts", 
      "cvendorphone", 
      "vreceiptcode", 
      "cuserid", 
      "iprintcount", 
      "pk_purcorp", 
      "tmaketime", 
      "taudittime", 
      "tlastmaketime" };
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("cinvoiceid")) {
      return this.m_cinvoiceid;
    }
    if (attributeName.equals("pk_corp")) {
      return this.m_pk_corp;
    }
    if (attributeName.equals("vinvoicecode")) {
      return this.m_vinvoicecode;
    }
    if (attributeName.equals("iinvoicetype")) {
      return this.m_iinvoicetype;
    }
    if (attributeName.equals("cdeptid")) {
      return this.m_cdeptid;
    }
    if (attributeName.equals("cfreecustid")) {
      return this.m_cfreecustid;
    }

    if (attributeName.equals("cstoreorganization")) {
      return this.m_cstoreorganization;
    }

    if (attributeName.equals("cvendormangid")) {
      return this.m_cvendormangid;
    }
    if (attributeName.equals("cvendorbaseid")) {
      return this.m_cvendorbaseid;
    }
    if (attributeName.equals("cemployeeid")) {
      return this.m_cemployeeid;
    }
    if (attributeName.equals("dinvoicedate")) {
      return this.m_dinvoicedate;
    }
    if (attributeName.equals("darrivedate")) {
      return this.m_darrivedate;
    }
    if (attributeName.equals("cbiztype")) {
      return this.m_cbiztype;
    }
    if (attributeName.equals("caccountbankid")) {
      return this.m_caccountbankid;
    }
    if (attributeName.equals("cpayunit")) {
      return this.m_cpayunit;
    }
    if (attributeName.equals("finitflag")) {
      return this.m_finitflag;
    }
    if (attributeName.equals("cvoucherid")) {
      return this.m_cvoucherid;
    }
    if (attributeName.equals("ctermprotocolid")) {
      return this.m_ctermprotocolid;
    }
    if (attributeName.equals("coperator")) {
      return this.m_coperator;
    }
    if (attributeName.equals("cuserid")) {
      if (this.m_cuserid == null) {
        return this.m_coperator;
      }
      return this.m_cuserid;
    }
    if (attributeName.equals("caccountyear")) {
      return this.m_caccountyear;
    }
    if (attributeName.equals("cbilltype")) {
      return this.m_cbilltype;
    }
    if (attributeName.equals("ibillstatus")) {
      return this.m_ibillstatus;
    }
    if (attributeName.equals("dauditdate")) {
      return this.m_dauditdate;
    }
    if (attributeName.equals("cauditpsn")) {
      return this.m_cauditpsn;
    }
    if (attributeName.equals("vmemo")) {
      return this.m_vmemo;
    }
    if (attributeName.equals("vdef1")) {
      return this.m_vdef1;
    }
    if (attributeName.equals("vdef2")) {
      return this.m_vdef2;
    }
    if (attributeName.equals("vdef3")) {
      return this.m_vdef3;
    }
    if (attributeName.equals("vdef4")) {
      return this.m_vdef4;
    }
    if (attributeName.equals("vdef5")) {
      return this.m_vdef5;
    }
    if (attributeName.equals("vdef6")) {
      return this.m_vdef6;
    }
    if (attributeName.equals("vdef7")) {
      return this.m_vdef7;
    }
    if (attributeName.equals("vdef8")) {
      return this.m_vdef8;
    }
    if (attributeName.equals("vdef9")) {
      return this.m_vdef9;
    }
    if (attributeName.equals("vdef10")) {
      return this.m_vdef10;
    }
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
    if (attributeName.equals("ccurrencytypeid"))
      return this.m_ccurrencytypeid;
    if (attributeName.equals("nexchangeotobrate"))
      return this.m_nexchangeotobrate;
    if (attributeName.equals("nexchangeotoarate"))
      return this.m_nexchangeotoarate;
    if (attributeName.equals("ts"))
      return this.m_ts;
    if (attributeName.equals("cvendorphone"))
      return this.m_cvendorphone;
    if (attributeName.equals("ntaxrate"))
      return this.m_ntaxrate;
    if (attributeName.equals("vreceiptcode"))
    {
      return this.m_vinvoicecode; }
    if (attributeName.equals("cuserid"))
    {
      return this.m_coperator; }
    if (attributeName.equals("coperatoridnow"))
      return this.m_coperatoridnow;
    if (attributeName.equals("iprintcount"))
      return this.m_iprintcount;
    if (attributeName.equals("pk_purcorp"))
      return this.m_pk_purcorp;
    if (attributeName.equals("tmaketime"))
      return this.m_tmaketime;
    if (attributeName.equals("taudittime"))
      return this.m_taudittime;
    if (attributeName.equals("tlastmaketime")) {
      return this.m_tlastmaketime;
    }

    return null;
  }

  public void setAttributeValue(String name, Object value)
  {
    try
    {
      if (name.equals("cinvoiceid")) {
        this.m_cinvoiceid = ((String)value); return;
      }
      if (name.equals("pk_corp")) {
        this.m_pk_corp = ((String)value); return;
      }
      if (name.equals("vinvoicecode")) {
        this.m_vinvoicecode = ((String)value); return;
      }
      if (name.equals("iinvoicetype")) {
        this.m_iinvoicetype = ((Integer)value); return;
      }
      if (name.equals("cdeptid")) {
        this.m_cdeptid = ((String)value); return;
      }
      if (name.equals("cfreecustid")) {
        this.m_cfreecustid = ((String)value); return;
      }

      if (name.equals("cstoreorganization")) {
        this.m_cstoreorganization = ((String)value); return;
      }

      if (name.equals("cvendormangid")) {
        this.m_cvendormangid = ((String)value); return;
      }
      if (name.equals("cvendorbaseid")) {
        this.m_cvendorbaseid = ((String)value); return;
      }
      if (name.equals("cemployeeid")) {
        this.m_cemployeeid = ((String)value); return;
      }
      if (name.equals("dinvoicedate")) {
        this.m_dinvoicedate = new UFDate(value.toString()); return;
      }
      if (name.equals("darrivedate")) {
        this.m_darrivedate = new UFDate(value.toString()); return;
      }
      if (name.equals("cbiztype")) {
        this.m_cbiztype = ((String)value); return;
      }
      if (name.equals("caccountbankid")) {
        this.m_caccountbankid = ((String)value); return;
      }
      if (name.equals("cpayunit")) {
        this.m_cpayunit = ((String)value); return;
      }
      if (name.equals("finitflag")) {
        if (value != null) {
          if ((value.toString().equals("N")) || (value.toString().equals("0"))) {
            this.m_finitflag = new Integer(0); return; }
          if ((value.toString().equals("Y")) || (value.toString().equals("1"))) {
            this.m_finitflag = new Integer(1); return;
          }
        }
        this.m_finitflag = new Integer(0); return;
      }

      if (name.equals("cvoucherid")) {
        this.m_cvoucherid = ((String)value); return;
      }
      if (name.equals("ctermprotocolid")) {
        this.m_ctermprotocolid = ((String)value); return;
      }
      if (name.equals("coperator")) {
        this.m_coperator = ((String)value); return;
      }
      if (name.equals("cuserid")) {
        this.m_cuserid = ((String)value); return;
      }
      if (name.equals("caccountyear")) {
        this.m_caccountyear = ((String)value); return;
      }
      if (name.equals("cbilltype")) {
        this.m_cbilltype = ((String)value); return;
      }
      if (name.equals("ibillstatus")) {
        this.m_ibillstatus = (((value == null) || (value.toString().trim().equals("")) || (value.toString().trim().equals(""))) ? new Integer(0) : (Integer)value); return;
      }
      if (name.equals("dauditdate")) {
    	  this.m_dauditdate = (value == null ? null : new UFDate(value.toString()));      }
      if (name.equals("cauditpsn")) {
        this.m_cauditpsn = ((String)value); return;
      }
      if (name.equals("vmemo")) {
        this.m_vmemo = ((String)value); return;
      }
      if (name.equals("vdef1")) {
        this.m_vdef1 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef2")) {
        this.m_vdef2 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef3")) {
        this.m_vdef3 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef4")) {
        this.m_vdef4 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef5")) {
        this.m_vdef5 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef6")) {
        this.m_vdef6 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef7")) {
        this.m_vdef7 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef8")) {
        this.m_vdef8 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef9")) {
        this.m_vdef9 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef10")) {
        this.m_vdef10 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return;
      }
      if (name.equals("vdef11")) {
        this.m_vdef11 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("vdef12")) {
        this.m_vdef12 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("vdef13")) {
        this.m_vdef13 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("vdef14")) {
        this.m_vdef14 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("vdef15")) {
        this.m_vdef15 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("vdef16")) {
        this.m_vdef16 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("vdef17")) {
        this.m_vdef17 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("vdef18")) {
        this.m_vdef18 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("vdef19")) {
        this.m_vdef19 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("vdef20")) {
        this.m_vdef20 = (((value == null) || (value.toString().trim().length() == 0)) ? null : value.toString()); return; }
      if (name.equals("pk_defdoc1")) {
        this.m_pk_defdoc1 = ((String)value); return; }
      if (name.equals("pk_defdoc2")) {
        this.m_pk_defdoc2 = ((String)value); return; }
      if (name.equals("pk_defdoc3")) {
        this.m_pk_defdoc3 = ((String)value); return; }
      if (name.equals("pk_defdoc4")) {
        this.m_pk_defdoc4 = ((String)value); return; }
      if (name.equals("pk_defdoc5")) {
        this.m_pk_defdoc5 = ((String)value); return; }
      if (name.equals("pk_defdoc6")) {
        this.m_pk_defdoc6 = ((String)value); return; }
      if (name.equals("pk_defdoc7")) {
        this.m_pk_defdoc7 = ((String)value); return; }
      if (name.equals("pk_defdoc8")) {
        this.m_pk_defdoc8 = ((String)value); return; }
      if (name.equals("pk_defdoc9")) {
        this.m_pk_defdoc9 = ((String)value); return; }
      if (name.equals("pk_defdoc10")) {
        this.m_pk_defdoc10 = ((String)value); return; }
      if (name.equals("pk_defdoc11")) {
        this.m_pk_defdoc11 = ((String)value); return; }
      if (name.equals("pk_defdoc12")) {
        this.m_pk_defdoc12 = ((String)value); return; }
      if (name.equals("pk_defdoc13")) {
        this.m_pk_defdoc13 = ((String)value); return; }
      if (name.equals("pk_defdoc14")) {
        this.m_pk_defdoc14 = ((String)value); return; }
      if (name.equals("pk_defdoc15")) {
        this.m_pk_defdoc15 = ((String)value); return; }
      if (name.equals("pk_defdoc16")) {
        this.m_pk_defdoc16 = ((String)value); return; }
      if (name.equals("pk_defdoc17")) {
        this.m_pk_defdoc17 = ((String)value); return; }
      if (name.equals("pk_defdoc18")) {
        this.m_pk_defdoc18 = ((String)value); return; }
      if (name.equals("pk_defdoc19")) {
        this.m_pk_defdoc19 = ((String)value); return; }
      if (name.equals("pk_defdoc20")) {
        this.m_pk_defdoc20 = ((String)value); return; }
      if (name.equals("dr")) {
        this.m_dr = (((value == null) || (value.toString().trim().equals("")) || (value.toString().trim().equals(""))) ? new Integer(0) : (Integer)value); return; }
      if (name.equals("ccurrencytypeid")) {
        this.m_ccurrencytypeid = ((String)value); return; }
      if (name.equals("nexchangeotobrate")) {
        this.m_nexchangeotobrate = new UFDouble(value.toString()); return; }
      if (name.equals("nexchangeotoarate")) {
    	  m_nexchangeotoarate =  (value==null?null:new	UFDouble(value.toString()));}
      if (name.equals("ts")) {
        this.m_ts = ((String)value); return; }
      if (name.equals("cvendorphone")) {
        this.m_cvendorphone = ((String)value); return; }
      if (name.equals("ntaxrate")) {
        this.m_ntaxrate = ((UFDouble)value); return; }
      if (name.equals("coperatoridnow")) {
        this.m_coperatoridnow = ((String)value); return; }
      if (name.equals("iprintcount")) {
        this.m_iprintcount = ((Integer)value); return; }
      if (name.equals("pk_purcorp")) {
        this.m_pk_purcorp = ((String)value); return; }
      if (name.equals("tmaketime")) {
        this.m_tmaketime = ((String)value); return; }
      if (name.equals("taudittime")) {
        this.m_taudittime = ((String)value); return; }
      if (name.equals("tlastmaketime"))
        this.m_tlastmaketime = ((String)value);
    }
    catch (ClassCastException e)
    {
      throw new ClassCastException("setAttributeValue方法中为 " + name + " 赋值时类型转换错误！（值：" + value + "）");
    }
  }

  public String getCauditpsn()
  {
    return this.m_cauditpsn;
  }

  public String getCbiztype()
  {
    return this.m_cbiztype;
  }

  public String getCcurrencytypeid()
  {
    return this.m_ccurrencytypeid;
  }

  public String getCtermprotocolid()
  {
    return this.m_ctermprotocolid;
  }

  public String getCvendorbaseid()
  {
    return this.m_cvendorbaseid;
  }

  public String getCvendormangid()
  {
    return this.m_cvendormangid;
  }

  public String getCvendorphone()
  {
    return this.m_cvendorphone;
  }

  public UFDate getDauditdate()
  {
    return this.m_dauditdate;
  }

  public Integer getDr()
  {
    return this.m_dr;
  }

  public Integer getIdiscounttaxtype()
  {
    return this.m_idiscounttaxtype;
  }

  public UFDouble getNexchangeotoarate()
  {
    return this.m_nexchangeotoarate;
  }

  public UFDouble getNexchangeotobrate()
  {
    return this.m_nexchangeotobrate;
  }

  public UFDouble getNtaxrate()
  {
    return this.m_ntaxrate;
  }

  public void setCauditpsn(String newCauditpsn)
  {
    this.m_cauditpsn = newCauditpsn;
  }

  public void setCbiztype(String newCbiztype)
  {
    this.m_cbiztype = newCbiztype;
  }

  public void setCcurrencytypeid(String newCcurrencytypeid)
  {
    this.m_ccurrencytypeid = newCcurrencytypeid;
  }

  public void setCtermprotocolid(String newCtermProtocolid)
  {
    this.m_ctermprotocolid = newCtermProtocolid;
  }

  public void setCvendorbaseid(String newCvendorbaseid)
  {
    this.m_cvendorbaseid = newCvendorbaseid;
  }

  public void setCvendormangid(String newCvendormangid)
  {
    this.m_cvendormangid = newCvendormangid;
  }

  public void setCvendorphone(String newCvendorphone)
  {
    this.m_cvendorphone = newCvendorphone;
  }

  public void setDauditdate(UFDate newDauditdate)
  {
    this.m_dauditdate = newDauditdate;
  }

  public void setDr(Integer newDr)
  {
    this.m_dr = newDr;
  }

  public void setIdiscounttaxtype(Integer newIdiscounttaxtype)
  {
    this.m_idiscounttaxtype = newIdiscounttaxtype;
  }

  public void setNexchangeotoarate(UFDouble newNexchangeotoarate)
  {
    this.m_nexchangeotoarate = newNexchangeotoarate;
  }

  public void setNexchangeotobrate(UFDouble newNexchangeotobrate)
  {
    this.m_nexchangeotobrate = newNexchangeotobrate;
  }

  public void setNtaxrate(UFDouble newNtaxrate)
  {
    this.m_ntaxrate = newNtaxrate;
  }

  public void validateDate()
    throws ValidationException
  {
    if (getDinvoicedate().after(getDarrivedate())) {
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000213"));
    }

    int bInit = getFinitflag().intValue();
    if (bInit != 0) {
      if (getEnabledDate() == null) {
        throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000214"));
      }
      if (getDinvoicedate().after(getEnabledDate())) {
        String[] value = { String.valueOf(getEnabledDate()) };
        throw new ValidationException(
          NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000215", null, value) + NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000216"));
      }
    }
  }

  private static UFDate getEnabledDate()
  {
    String m_pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
    if (ENABLED_DATE != null) {
      return ENABLED_DATE;
    }
    if (ENABLED_DATE == null) {
      String[] strEnabled = (String[])null;
      try {
        strEnabled = SFServiceFacility.getCreateCorpQueryService().queryEnabledPeriod(
          m_pk_corp, 
          "PO");
      } catch (Exception e) {
        SCMEnv.out(e);
      }

      if (strEnabled != null) {
        String strEnabledDate = strEnabled[3];
        if (strEnabledDate != null)
          ENABLED_DATE = new UFDate(strEnabledDate);
        else
          ENABLED_DATE = null;
      }
      else {
        ENABLED_DATE = null;
      }

    }

    return ENABLED_DATE;
  }

  public void validateExchangeRate()
    throws ValidationException
  {
    if ((this.m_nexchangeotobrate != null) && 
      (this.m_nexchangeotobrate.doubleValue() < 0.0D)) {
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000217"));
    }

    if ((this.m_nexchangeotoarate == null) || 
      (this.m_nexchangeotoarate.doubleValue() >= 0.0D)) return;
    throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000218"));
  }

  public void validateLength()
    throws ValidationException
  {
    if ((this.m_vinvoicecode == null) || 
      (this.m_vinvoicecode.length() <= 30)) return;
    throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000219"));
  }

  public void validateNull()
    throws ValidationException
  {
    StringBuffer errInfo = new StringBuffer();
    if ((this.m_cbiztype == null) || (this.m_cbiztype.trim().equals(""))) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000220"));
    }

    if (this.m_iinvoicetype == null) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000221"));
    }
    if ((this.m_dinvoicedate == null) || (this.m_dinvoicedate.equals(""))) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000222"));
    }
    if ((this.m_darrivedate == null) || (this.m_darrivedate.equals(""))) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000223"));
    }

    if ((this.m_cvendormangid == null) || (this.m_cvendormangid.trim().equals(""))) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000224"));
    }
    if ((this.m_cdeptid == null) || (this.m_cdeptid.trim().equals(""))) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000225"));
    }
    if ((this.m_cemployeeid == null) || (this.m_cemployeeid.trim().equals(""))) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000226"));
    }
    if ((this.m_coperator == null) || (this.m_coperator.trim().equals(""))) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000227"));
    }
    if ((this.m_ccurrencytypeid == null) || (this.m_ccurrencytypeid.trim().equals(""))) {
      errInfo.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001755"));
    }

    String sMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("40040401", "UPP40040401-000228");
    if (errInfo.toString().length() > 0) {
      errInfo.append("");
      sMessage = sMessage + errInfo.toString();
      throw new NullFieldException(sMessage);
    }
  }

  public String getCstoreorganization()
  {
    return this.m_cstoreorganization;
  }

  public String getCuserid()
  {
    return this.m_cuserid;
  }

  public static String[] getDbFields()
  {
    return new String[] { 
      "cinvoiceid", 
      "pk_corp", 
      "vinvoicecode", 
      "iinvoicetype", 
      "cdeptid", 
      "cfreecustid", 
      "cstoreorganization", 
      "cvendormangid", 
      "cvendorbaseid", 
      "cemployeeid", 
      "dinvoicedate", 
      "darrivedate", 
      "cbiztype", 
      "caccountbankid", 
      "cpayunit", 
      "finitflag", 
      "cvoucherid", 
      "ctermprotocolid", 
      "coperator", 
      "caccountyear", 
      "cbilltype", 
      "ibillstatus", 
      "dauditdate", 
      "cauditpsn", 
      "vmemo", 
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
      "iprintcount", 
      "pk_purcorp", 
      "tmaketime", 
      "taudittime", 
      "tlastmaketime" };
  }

  public FieldDBValidateVO[] getFieldDBValidateVOs()
  {
    return new FieldDBValidateVO[] { 
      new FieldDBValidateVO(
      TaxRateField.class, 
      new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003078") }, 
      new UFDouble[] { this.m_ntaxrate }), 
      new FieldDBValidateVO(
      ToARateField.class, 
      new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002095") }, 
      new UFDouble[] { this.m_nexchangeotoarate }), 
      new FieldDBValidateVO(
      ToBRateField.class, 
      new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002092") }, 
      new UFDouble[] { this.m_nexchangeotobrate }) };
  }

  public String getTs()
  {
    return this.m_ts;
  }

  public String getTmaketime()
  {
    return this.m_tmaketime;
  }

  public void setTmaketime(String sNewTmaketime)
  {
    this.m_tmaketime = sNewTmaketime;
  }

  public String getTaudittime()
  {
    return this.m_taudittime;
  }

  public void setTaudittime(String sNewTaudittime)
  {
    this.m_taudittime = sNewTaudittime;
  }

  public String getTlastmaketime()
  {
    return this.m_tlastmaketime;
  }

  public void setTlastmaketime(String sNewTlastmaketime)
  {
    this.m_tlastmaketime = sNewTlastmaketime;
  }

  public void setCstoreorganization(String newCstoreorganization)
  {
    this.m_cstoreorganization = newCstoreorganization;
  }

  public void setCuserid(String newCuserid)
  {
    this.m_cuserid = newCuserid;
  }

  public void setTs(String sNewTs)
  {
    this.m_ts = sNewTs;
  }

  public String getCoperatoridnow()
  {
    return this.m_coperatoridnow;
  }

  public void setCoperatoridnow(String m_coperatoridnow)
  {
    this.m_coperatoridnow = m_coperatoridnow;
  }
}