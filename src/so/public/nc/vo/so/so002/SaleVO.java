package nc.vo.so.so002;

import java.util.ArrayList;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.IPfBackCheck2;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pub.isnull.NullFieldInfo;
import nc.vo.so.pub.ISoPrintCount;

public class SaleVO extends CircularlyAccessibleValueObject
  implements ISoPrintCount, IPfBackCheck2, NullFieldInfo
{
  public static final int FSTATUS_NULL = 0;
  public static final int FSTATUS_FREE = 1;
  public static final int FSTATUS_AUDITED = 2;
  public static final int FSTATUS_FREEZE = 3;
  public static final int FSTATUS_CLOSE = 4;
  public static final int FSTATUS_BLANKOUT = 5;
  public static final int FSTATUS_FINISH = 6;
  public static final int FSTATUS_AUDITING = 7;
  public static final int FSTATUS_AUDITFAIL = 8;
  public static final int FCOUNTERACTFLAG_NORMAL = 0;
  public static final int FCOUNTERACTFLAG_COUNTERACT_FINISH = 1;
  public static final int FCOUNTERACTFLAG_COUNTERACT_GEN = 2;
  public String m_csaleid;
  public String m_pk_corp;
  public String m_vreceiptcode;
  public String m_creceipttype;
  public String m_cbiztype;
  public Integer m_finvoiceclass;
  public Integer m_finvoicetype;
  public String m_vaccountyear;
  public UFBoolean m_binitflag;
  public UFDate m_dbilldate;
  public String m_cdeptid;
  public String m_cemployeeid;
  public String m_coperatorid;
  public String m_ctermprotocolid;
  public String m_csalecorpid;
  public String m_creceiptcustomerid;
  public String m_vreceiveaddress;
  public String m_creceiptcorpid;
  public String m_ctransmodeid;
  public UFDouble m_ndiscountrate;
  public String m_cwarehouseid;
  public String m_veditreason;
  public UFBoolean m_bfreecustflag;
  public String m_cfreecustid;
  public Integer m_ibalanceflag;
  public UFDouble m_nsubscription;
  public String m_ccreditnum;
  public UFDouble m_nevaluatecarriage;
  public UFDate m_dmakedate;
  public String m_capproveid;
  public UFDate m_dapprovedate;
  public Integer m_fstatus;
  public String m_vnote;
  public String m_vdef1;
  public String m_vdef2;
  public String m_vdef3;
  public String m_vdef4;
  public String m_vdef5;
  public String m_vdef6;
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
  public Integer iprintcount;
  public Integer m_fcounteractflag;
  public boolean m_bcodechanged;
  public String m_ccalbodyid;
  public String m_ccurrencyid;
  public String m_ccustbankid;
  public String m_cdispatcherid;
  public UFBoolean m_isGather;
  public UFDouble m_nnetmny;
  public UFDouble m_nstrikemny;
  public UFDouble m_ntotalsummny;
  public UFDateTime m_ts;
  public String m_voldreceiptcode;
  public String m_vprintcustname;
  public String m_dbilltime;
  public String m_daudittime;
  public String m_dmoditime;
  public String ccustbaseid;
  public String cjje1;
  public String cjje2;

  public SaleVO()
  {
  }

  public SaleVO(String newCsaleid)
  {
    this.m_csaleid = newCsaleid;
  }

  public ScmPrintlogVO getScmPrintlogVO()
  {
    ScmPrintlogVO printlogvo = new ScmPrintlogVO();
    printlogvo.setCbillid(getCsaleid());
    printlogvo.setCbilltypecode(getVreceiptcode());
    printlogvo.setCbilltypecode("32");

    printlogvo.setCoperatorid(getCoperatorid());
    printlogvo.setIoperatetype(new Integer(0));
    printlogvo.setPk_corp(getPk_corp());
    printlogvo.setTs((getTs() == null) ? "" : getTs().toString());
    return printlogvo;
  }

  public Object clone()
  {
    Object o = null;
    try {
      o = super.clone(); } catch (Exception e) {
    }
    SaleVO sale = (SaleVO)o;

    return sale;
  }

  public String getCjje1() {
		return this.cjje1;
  }

  public String setCjje1(String cjje1) {
		return this.cjje1 = cjje1;
  }

  public String getCjje2() {
		return this.cjje2;
  }

  public String setCjje2(String cjje2) {
		return this.cjje2 = cjje2;
  }
	
  public String getEntityName()
  {
    return "Sale";
  }

  public String getPrimaryKey()
  {
    return this.m_csaleid;
  }

  public void setPrimaryKey(String newCsaleid)
  {
    this.m_csaleid = newCsaleid;
  }

  public String getCsaleid()
  {
    return this.m_csaleid;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public String getVreceiptcode()
  {
    return this.m_vreceiptcode;
  }

  public String getCreceipttype()
  {
    return this.m_creceipttype;
  }

  public String getCbiztype()
  {
    return this.m_cbiztype;
  }

  public Integer getFinvoiceclass()
  {
    return this.m_finvoiceclass;
  }

  public Integer getFinvoicetype()
  {
    return this.m_finvoicetype;
  }

  public String getVaccountyear()
  {
    return this.m_vaccountyear;
  }

  public UFBoolean getBinitflag()
  {
    return this.m_binitflag;
  }

  public UFDate getDbilldate()
  {
    return this.m_dbilldate;
  }

  public String getCdeptid()
  {
    return this.m_cdeptid;
  }

  public String getCemployeeid()
  {
    return this.m_cemployeeid;
  }

  public String getCoperatorid()
  {
    return this.m_coperatorid;
  }

  public String getCtermprotocolid()
  {
    return this.m_ctermprotocolid;
  }

  public String getCsalecorpid()
  {
    return this.m_csalecorpid;
  }

  public String getCreceiptcustomerid()
  {
    return this.m_creceiptcustomerid;
  }

  public String getVreceiveaddress()
  {
    return this.m_vreceiveaddress;
  }

  public String getCreceiptcorpid()
  {
    return this.m_creceiptcorpid;
  }

  public String getCtransmodeid()
  {
    return this.m_ctransmodeid;
  }

  public UFDouble getNdiscountrate()
  {
    return this.m_ndiscountrate;
  }

  public String getCwarehouseid()
  {
    return this.m_cwarehouseid;
  }

  public String getVeditreason()
  {
    return this.m_veditreason;
  }

  public UFBoolean getBfreecustflag()
  {
    return this.m_bfreecustflag;
  }

  public String getCfreecustid()
  {
    return this.m_cfreecustid;
  }

  public Integer getIbalanceflag()
  {
    return this.m_ibalanceflag;
  }

  public UFDouble getNsubscription()
  {
    return this.m_nsubscription;
  }

  public String getCcreditnum()
  {
    return this.m_ccreditnum;
  }

  public UFDouble getNevaluatecarriage()
  {
    return this.m_nevaluatecarriage;
  }

  public UFDate getDmakedate()
  {
    return this.m_dmakedate;
  }

  public String getCapproveid()
  {
    return this.m_capproveid;
  }

  public UFDate getDapprovedate()
  {
    return this.m_dapprovedate;
  }

  public Integer getFstatus()
  {
    return this.m_fstatus;
  }

  public String getVnote()
  {
    return this.m_vnote;
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

  public String getPk_defdoc1()
  {
    return this.m_pk_defdoc1;
  }

  public String getPk_defdoc2()
  {
    return this.m_pk_defdoc2;
  }

  public String getPk_defdoc3()
  {
    return this.m_pk_defdoc3;
  }

  public String getPk_defdoc4()
  {
    return this.m_pk_defdoc4;
  }

  public String getPk_defdoc5()
  {
    return this.m_pk_defdoc5;
  }

  public String getPk_defdoc6()
  {
    return this.m_pk_defdoc6;
  }

  public String getPk_defdoc7()
  {
    return this.m_pk_defdoc7;
  }

  public String getPk_defdoc8()
  {
    return this.m_pk_defdoc8;
  }

  public String getPk_defdoc9()
  {
    return this.m_pk_defdoc9;
  }

  public String getPk_defdoc10()
  {
    return this.m_pk_defdoc10;
  }

  public String getPk_defdoc11()
  {
    return this.m_pk_defdoc11;
  }

  public String getPk_defdoc12()
  {
    return this.m_pk_defdoc12;
  }

  public String getPk_defdoc13()
  {
    return this.m_pk_defdoc13;
  }

  public String getPk_defdoc14()
  {
    return this.m_pk_defdoc14;
  }

  public String getPk_defdoc15()
  {
    return this.m_pk_defdoc15;
  }

  public String getPk_defdoc16()
  {
    return this.m_pk_defdoc16;
  }

  public String getPk_defdoc17()
  {
    return this.m_pk_defdoc17;
  }

  public String getPk_defdoc18()
  {
    return this.m_pk_defdoc18;
  }

  public String getPk_defdoc19()
  {
    return this.m_pk_defdoc19;
  }

  public String getPk_defdoc20()
  {
    return this.m_pk_defdoc20;
  }

  public void setCsaleid(String newCsaleid)
  {
    this.m_csaleid = newCsaleid;
  }

  public void setPk_corp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setVreceiptcode(String newVreceiptcode)
  {
    this.m_vreceiptcode = newVreceiptcode;
  }

  public void setCreceipttype(String newCreceipttype)
  {
    this.m_creceipttype = newCreceipttype;
  }

  public void setCbiztype(String newCbiztype)
  {
    this.m_cbiztype = newCbiztype;
  }

  public void setFinvoiceclass(Integer newFinvoiceclass)
  {
    this.m_finvoiceclass = newFinvoiceclass;
  }

  public void setFinvoicetype(Integer newFinvoicetype)
  {
    this.m_finvoicetype = newFinvoicetype;
  }

  public void setVaccountyear(String newVaccountyear)
  {
    this.m_vaccountyear = newVaccountyear;
  }

  public void setBinitflag(UFBoolean newBinitflag)
  {
    this.m_binitflag = newBinitflag;
  }

  public void setDbilldate(UFDate newDbilldate)
  {
    this.m_dbilldate = newDbilldate;
  }

  public void setCdeptid(String newCdeptid)
  {
    this.m_cdeptid = newCdeptid;
  }

  public void setCemployeeid(String newCemployeeid)
  {
    this.m_cemployeeid = newCemployeeid;
  }

  public void setCoperatorid(String newCoperatorid)
  {
    this.m_coperatorid = newCoperatorid;
  }

  public void setCtermprotocolid(String newCtermprotocolid)
  {
    this.m_ctermprotocolid = newCtermprotocolid;
  }

  public void setCsalecorpid(String newCsalecorpid)
  {
    this.m_csalecorpid = newCsalecorpid;
  }

  public void setCreceiptcustomerid(String newCreceiptcustomerid)
  {
    this.m_creceiptcustomerid = newCreceiptcustomerid;
  }

  public void setVreceiveaddress(String newVreceiveaddress)
  {
    this.m_vreceiveaddress = newVreceiveaddress;
  }

  public void setCreceiptcorpid(String newCreceiptcorpid)
  {
    this.m_creceiptcorpid = newCreceiptcorpid;
  }

  public void setCtransmodeid(String newCtransmodeid)
  {
    this.m_ctransmodeid = newCtransmodeid;
  }

  public void setNdiscountrate(UFDouble newNdiscountrate)
  {
    this.m_ndiscountrate = newNdiscountrate;
  }

  public void setCwarehouseid(String newCwarehouseid)
  {
    this.m_cwarehouseid = newCwarehouseid;
  }

  public void setVeditreason(String newVeditreason)
  {
    this.m_veditreason = newVeditreason;
  }

  public void setBfreecustflag(UFBoolean newBfreecustflag)
  {
    this.m_bfreecustflag = newBfreecustflag;
  }

  public void setCfreecustid(String newCfreecustid)
  {
    this.m_cfreecustid = newCfreecustid;
  }

  public void setIbalanceflag(Integer newIbalanceflag)
  {
    this.m_ibalanceflag = newIbalanceflag;
  }

  public void setNsubscription(UFDouble newNsubscription)
  {
    this.m_nsubscription = newNsubscription;
  }

  public void setCcreditnum(String newCcreditnum)
  {
    this.m_ccreditnum = newCcreditnum;
  }

  public void setNevaluatecarriage(UFDouble newNevaluatecarriage)
  {
    this.m_nevaluatecarriage = newNevaluatecarriage;
  }

  public void setDmakedate(UFDate newDmakedate)
  {
    this.m_dmakedate = newDmakedate;
  }

  public void setCapproveid(String newCapproveid)
  {
    this.m_capproveid = newCapproveid;
  }

  public void setDapprovedate(UFDate newDapprovedate)
  {
    this.m_dapprovedate = newDapprovedate;
  }

  public void setFstatus(Integer newFstatus)
  {
    this.m_fstatus = newFstatus;
  }

  public void setVnote(String newVnote)
  {
    this.m_vnote = newVnote;
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

  public void setPk_defdoc1(String newPk_defdoc1)
  {
    this.m_pk_defdoc1 = newPk_defdoc1;
  }

  public void setPk_defdoc2(String newPk_defdoc2)
  {
    this.m_pk_defdoc2 = newPk_defdoc2;
  }

  public void setPk_defdoc3(String newPk_defdoc3)
  {
    this.m_pk_defdoc3 = newPk_defdoc3;
  }

  public void setPk_defdoc4(String newPk_defdoc4)
  {
    this.m_pk_defdoc4 = newPk_defdoc4;
  }

  public void setPk_defdoc5(String newPk_defdoc5)
  {
    this.m_pk_defdoc5 = newPk_defdoc5;
  }

  public void setPk_defdoc6(String newPk_defdoc6)
  {
    this.m_pk_defdoc6 = newPk_defdoc6;
  }

  public void setPk_defdoc7(String newPk_defdoc7)
  {
    this.m_pk_defdoc7 = newPk_defdoc7;
  }

  public void setPk_defdoc8(String newPk_defdoc8)
  {
    this.m_pk_defdoc8 = newPk_defdoc8;
  }

  public void setPk_defdoc9(String newPk_defdoc9)
  {
    this.m_pk_defdoc9 = newPk_defdoc9;
  }

  public void setPk_defdoc10(String newPk_defdoc10)
  {
    this.m_pk_defdoc10 = newPk_defdoc10;
  }

  public void setPk_defdoc11(String newPk_defdoc11)
  {
    this.m_pk_defdoc11 = newPk_defdoc11;
  }

  public void setPk_defdoc12(String newPk_defdoc12)
  {
    this.m_pk_defdoc12 = newPk_defdoc12;
  }

  public void setPk_defdoc13(String newPk_defdoc13)
  {
    this.m_pk_defdoc13 = newPk_defdoc13;
  }

  public void setPk_defdoc14(String newPk_defdoc14)
  {
    this.m_pk_defdoc14 = newPk_defdoc14;
  }

  public void setPk_defdoc15(String newPk_defdoc15)
  {
    this.m_pk_defdoc15 = newPk_defdoc15;
  }

  public void setPk_defdoc16(String newPk_defdoc16)
  {
    this.m_pk_defdoc16 = newPk_defdoc16;
  }

  public void setPk_defdoc17(String newPk_defdoc17)
  {
    this.m_pk_defdoc17 = newPk_defdoc17;
  }

  public void setPk_defdoc18(String newPk_defdoc18)
  {
    this.m_pk_defdoc18 = newPk_defdoc18;
  }

  public void setPk_defdoc19(String newPk_defdoc19)
  {
    this.m_pk_defdoc19 = newPk_defdoc19;
  }

  public void setPk_defdoc20(String newPk_defdoc20)
  {
    this.m_pk_defdoc20 = newPk_defdoc20;
  }

  public void validate()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    if ((this.m_cdeptid == null) || (this.m_cdeptid.length() == 0)) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004064"));
    }
    if ((this.m_creceipttype == null) || (this.m_creceipttype.length() == 0)) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000807"));
    }
    if (this.m_cbiztype == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000003"));
    }
    if ((this.m_creceiptcorpid == null) || (this.m_creceiptcorpid.length() == 0)) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001899"));
    }
    if ((this.m_csalecorpid == null) || (this.m_csalecorpid.length() == 0)) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004128"));
    }
    if (this.m_dbilldate == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000799"));
    }
    if ((this.m_vprintcustname == null) || (this.m_vprintcustname.length() == 0)) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000159"));
    }
    if ((this.m_bfreecustflag != null) && (this.m_bfreecustflag.booleanValue()) && ((
      (this.m_cfreecustid == null) || (this.m_cfreecustid.length() == 0)))) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002272"));
    }

    StringBuffer message = new StringBuffer();
    message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000137"));
    if (errFields.size() > 0) {
      String[] temp = (String[])(String[])errFields.toArray(new String[0]);

      message.append(temp[0].toString());
      for (int i = 1; i < temp.length; ++i) {
        message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000000"));

        message.append(temp[i].toString());
      }

      throw new NullFieldException(message.toString());
    }
  }

  public String[] getAttributeNames()
  {
    return new String[] { "ts", "pk_corp", "vreceiptcode", "creceipttype", "cbiztype", "finvoiceclass", "finvoicetype", "vaccountyear", "binitflag", "dbilldate", "ccustomerid", "cdeptid", "cemployeeid", "coperatorid", "ctermprotocolid", "csalecorpid", "creceiptcustomerid", "vreceiveaddress", "creceiptcorpid", "ctransmodeid", "ndiscountrate", "cwarehouseid", "veditreason", "bfreecustflag", "cfreecustid", "ibalanceflag", "nsubscription", "ccreditnum", "nevaluatecarriage", "dmakedate", "capproveid", "dapprovedate", "fstatus", "vnote", "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10", "ccustbankid", "ccalbodyid", "ccurrencyid", "cdispatcherid", "bcodechanged", "voldreceiptcode", "ntotalsummny", "nstrikemny", "nnetmny", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", "pk_defdoc8", "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13", "pk_defdoc14", "pk_defdoc15", "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20", "iprintcount", "fcounteractflag", "vprintcustname", "dbilltime", "daudittime", "dmoditime", "ccustbaseid","cjje1","cjje2" };
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("csaleid")) {
      return this.m_csaleid;
    }
    if (attributeName.equals("pk_corp")) {
      return this.m_pk_corp;
    }

    if (attributeName.equals("ccustbaseid")) {
      return this.ccustbaseid;
    }
    if (attributeName.equals("vreceiptcode")) {
      return this.m_vreceiptcode;
    }
    if (attributeName.equals("creceipttype")) {
      return this.m_creceipttype;
    }
    if (attributeName.equals("cbiztype")) {
      return this.m_cbiztype;
    }
    if (attributeName.equals("finvoiceclass")) {
      return this.m_finvoiceclass;
    }
    if (attributeName.equals("finvoicetype")) {
      return this.m_finvoicetype;
    }
    if (attributeName.equals("vaccountyear")) {
      return this.m_vaccountyear;
    }
    if (attributeName.equals("binitflag")) {
      return this.m_binitflag;
    }
    if (attributeName.equals("dbilldate")) {
      return this.m_dbilldate;
    }
    if (attributeName.equals("cdeptid")) {
      return this.m_cdeptid;
    }
    if (attributeName.equals("cemployeeid")) {
      return this.m_cemployeeid;
    }
    if (attributeName.equals("coperatorid")) {
      return this.m_coperatorid;
    }
    if (attributeName.equals("ctermprotocolid")) {
      return this.m_ctermprotocolid;
    }
    if (attributeName.equals("csalecorpid")) {
      return this.m_csalecorpid;
    }
    if (attributeName.equals("creceiptcustomerid")) {
      return this.m_creceiptcustomerid;
    }
    if (attributeName.equals("vreceiveaddress")) {
      return this.m_vreceiveaddress;
    }
    if (attributeName.equals("creceiptcorpid")) {
      return this.m_creceiptcorpid;
    }
    if (attributeName.equals("ctransmodeid")) {
      return this.m_ctransmodeid;
    }
    if (attributeName.equals("ndiscountrate")) {
      return this.m_ndiscountrate;
    }
    if (attributeName.equals("cwarehouseid")) {
      return this.m_cwarehouseid;
    }
    if (attributeName.equals("veditreason")) {
      return this.m_veditreason;
    }
    if (attributeName.equals("bfreecustflag")) {
      return this.m_bfreecustflag;
    }
    if (attributeName.equals("cfreecustid")) {
      return this.m_cfreecustid;
    }
    if (attributeName.equals("ibalanceflag")) {
      return this.m_ibalanceflag;
    }
    if (attributeName.equals("nsubscription")) {
      return this.m_nsubscription;
    }
    if (attributeName.equals("ccreditnum")) {
      return this.m_ccreditnum;
    }
    if (attributeName.equals("nevaluatecarriage")) {
      return this.m_nevaluatecarriage;
    }
    if (attributeName.equals("dmakedate")) {
      return this.m_dmakedate;
    }
    if (attributeName.equals("capproveid")) {
      return this.m_capproveid;
    }
    if (attributeName.equals("dapprovedate")) {
      return this.m_dapprovedate;
    }
    if (attributeName.equals("fstatus")) {
      return this.m_fstatus;
    }
    if (attributeName.equals("vnote")) {
      return this.m_vnote;
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

    if (attributeName.equals("vdef11")) {
      return this.m_vdef11;
    }
    if (attributeName.equals("vdef12")) {
      return this.m_vdef12;
    }
    if (attributeName.equals("vdef13")) {
      return this.m_vdef13;
    }
    if (attributeName.equals("vdef14")) {
      return this.m_vdef14;
    }
    if (attributeName.equals("vdef15")) {
      return this.m_vdef15;
    }
    if (attributeName.equals("vdef16")) {
      return this.m_vdef16;
    }
    if (attributeName.equals("vdef17")) {
      return this.m_vdef17;
    }
    if (attributeName.equals("vdef18")) {
      return this.m_vdef18;
    }
    if (attributeName.equals("vdef19")) {
      return this.m_vdef19;
    }
    if (attributeName.equals("vdef20")) {
      return this.m_vdef20;
    }
    if (attributeName.equals("pk_defdoc1")) {
      return this.m_pk_defdoc1;
    }
    if (attributeName.equals("pk_defdoc2")) {
      return this.m_pk_defdoc2;
    }
    if (attributeName.equals("pk_defdoc3")) {
      return this.m_pk_defdoc3;
    }
    if (attributeName.equals("pk_defdoc4")) {
      return this.m_pk_defdoc4;
    }
    if (attributeName.equals("pk_defdoc5")) {
      return this.m_pk_defdoc5;
    }
    if (attributeName.equals("pk_defdoc6")) {
      return this.m_pk_defdoc6;
    }
    if (attributeName.equals("pk_defdoc7")) {
      return this.m_pk_defdoc7;
    }
    if (attributeName.equals("pk_defdoc8")) {
      return this.m_pk_defdoc8;
    }
    if (attributeName.equals("pk_defdoc9")) {
      return this.m_pk_defdoc9;
    }
    if (attributeName.equals("pk_defdoc10")) {
      return this.m_pk_defdoc10;
    }
    if (attributeName.equals("pk_defdoc11")) {
      return this.m_pk_defdoc11;
    }
    if (attributeName.equals("pk_defdoc12")) {
      return this.m_pk_defdoc12;
    }
    if (attributeName.equals("pk_defdoc13")) {
      return this.m_pk_defdoc13;
    }
    if (attributeName.equals("pk_defdoc14")) {
      return this.m_pk_defdoc14;
    }
    if (attributeName.equals("pk_defdoc15")) {
      return this.m_pk_defdoc15;
    }
    if (attributeName.equals("pk_defdoc16")) {
      return this.m_pk_defdoc16;
    }
    if (attributeName.equals("pk_defdoc17")) {
      return this.m_pk_defdoc17;
    }
    if (attributeName.equals("pk_defdoc18")) {
      return this.m_pk_defdoc18;
    }
    if (attributeName.equals("pk_defdoc19")) {
      return this.m_pk_defdoc19;
    }
    if (attributeName.equals("pk_defdoc20")) {
      return this.m_pk_defdoc20;
    }

    if (attributeName.equals("ccustbankid")) {
      return this.m_ccustbankid;
    }
    if (attributeName.equals("ccalbodyid")) {
      return this.m_ccalbodyid;
    }
    if (attributeName.equals("ccurrencyid")) {
      return this.m_ccurrencyid;
    }
    if (attributeName.equals("ts")) {
      return this.m_ts;
    }
    if (attributeName.equals("cdispatcherid")) {
      return this.m_cdispatcherid;
    }
    if (attributeName.equals("bcodechanged")) {
      return new UFBoolean(this.m_bcodechanged);
    }
    if (attributeName.equals("voldreceiptcode")) {
      return this.m_voldreceiptcode;
    }

    if (attributeName.equals("ntotalsummny")) {
      return this.m_ntotalsummny;
    }
    if (attributeName.equals("nstrikemny")) {
      return this.m_nstrikemny;
    }
    if (attributeName.equals("nnetmny")) {
      return this.m_nnetmny;
    }
    if (attributeName.equals("iprintcount")) {
      return this.iprintcount;
    }
    if (attributeName.equals("fcounteractflag")) {
      return this.m_fcounteractflag;
    }
    if (attributeName.equals("vprintcustname")) {
      return this.m_vprintcustname;
    }
    if (attributeName.equals("dbilltime")) {
      return this.m_dbilltime;
    }
    if (attributeName.equals("daudittime")) {
      return this.m_daudittime;
    }
    if (attributeName.equals("dmoditime")) {
      return this.m_dmoditime;
    }
    if(attributeName.equals("cjje1")){
      return this.cjje1;
    }
    if(attributeName.equals("cjje2")){
      return this.cjje2;
    }

    return null;
  }

  public void setAttributeValue(String name, Object value)
  {
    try
    {
      if (name.startsWith("vdef"))
        value = (value == null) ? (String)null : value.toString();
      if (name.equals("csaleid")) {
        this.m_csaleid = ((String)value);
      }
      else if (name.equals("ccustbaseid")) {
        this.ccustbaseid = ((String)value);
      }
      else if (name.equals("pk_corp")) {
        this.m_pk_corp = ((String)value);
      }
      else if (name.equals("vreceiptcode")) {
        this.m_vreceiptcode = ((String)value);
      }
      else if (name.equals("creceipttype")) {
        this.m_creceipttype = ((String)value);
      }
      else if (name.equals("cbiztype")) {
        this.m_cbiztype = ((String)value);
      }
      else if (name.equals("finvoiceclass")) {
        this.m_finvoiceclass = ((Integer)value);
      }
      else if (name.equals("finvoicetype")) {
        this.m_finvoicetype = ((Integer)value);
      }
      else if (name.equals("vaccountyear")) {
        this.m_vaccountyear = ((String)value);
      }
      else if (name.equals("binitflag")) {
        this.m_binitflag = ((UFBoolean)value);
      }
      else if (name.equals("dbilldate")) {
        this.m_dbilldate = ((UFDate)value);
      }
      else if (name.equals("cdeptid")) {
        this.m_cdeptid = ((String)value);
      }
      else if (name.equals("cemployeeid")) {
        this.m_cemployeeid = ((String)value);
      }
      else if (name.equals("coperatorid")) {
        this.m_coperatorid = ((String)value);
      }
      else if (name.equals("ctermprotocolid")) {
        this.m_ctermprotocolid = ((String)value);
      }
      else if (name.equals("csalecorpid")) {
        this.m_csalecorpid = ((String)value);
      }
      else if (name.equals("creceiptcustomerid")) {
        this.m_creceiptcustomerid = ((String)value);
      }
      else if (name.equals("vreceiveaddress")) {
        this.m_vreceiveaddress = ((String)value);
      }
      else if (name.equals("creceiptcorpid")) {
        this.m_creceiptcorpid = ((String)value);
      }
      else if (name.equals("ctransmodeid")) {
        this.m_ctransmodeid = ((String)value);
      }
      else if (name.equals("ndiscountrate")) {
        this.m_ndiscountrate = ((UFDouble)value);
      }
      else if (name.equals("cwarehouseid")) {
        this.m_cwarehouseid = ((String)value);
      }
      else if (name.equals("veditreason")) {
        this.m_veditreason = ((String)value);
      }
      else if (name.equals("bfreecustflag")) {
        this.m_bfreecustflag = ((UFBoolean)value);
      }
      else if (name.equals("cfreecustid")) {
        this.m_cfreecustid = ((String)value);
      }
      else if (name.equals("ibalanceflag")) {
        this.m_ibalanceflag = ((Integer)value);
      }
      else if (name.equals("nsubscription")) {
        this.m_nsubscription = ((UFDouble)value);
      }
      else if (name.equals("ccreditnum")) {
        this.m_ccreditnum = ((String)value);
      }
      else if (name.equals("nevaluatecarriage")) {
        this.m_nevaluatecarriage = ((UFDouble)value);
      }
      else if (name.equals("dmakedate")) {
        this.m_dmakedate = ((UFDate)value);
      }
      else if (name.equals("capproveid")) {
        this.m_capproveid = ((String)value);
      }
      else if (name.equals("dapprovedate")) {
        this.m_dapprovedate = ((UFDate)value);
      }
      else if (name.equals("fstatus")) {
        this.m_fstatus = ((Integer)value);
      }
      else if (name.equals("vnote")) {
        this.m_vnote = ((String)value);
      }
      else if (name.equals("vdef1")) {
        this.m_vdef1 = ((String)value);
      }
      else if (name.equals("vdef2")) {
        this.m_vdef2 = ((String)value);
      }
      else if (name.equals("vdef3")) {
        this.m_vdef3 = ((String)value);
      }
      else if (name.equals("vdef4")) {
        this.m_vdef4 = ((String)value);
      }
      else if (name.equals("vdef5")) {
        this.m_vdef5 = ((String)value);
      }
      else if (name.equals("vdef6")) {
        this.m_vdef6 = ((String)value);
      }
      else if (name.equals("vdef7")) {
        this.m_vdef7 = ((String)value);
      }
      else if (name.equals("vdef8")) {
        this.m_vdef8 = ((String)value);
      }
      else if (name.equals("vdef9")) {
        this.m_vdef9 = ((String)value);
      }
      else if (name.equals("vdef10")) {
        this.m_vdef10 = ((String)value);
      }
      else if (name.equals("vdef11")) {
        this.m_vdef11 = ((String)value);
      }
      else if (name.equals("vdef12")) {
        this.m_vdef12 = ((String)value);
      }
      else if (name.equals("vdef13")) {
        this.m_vdef13 = ((String)value);
      }
      else if (name.equals("vdef14")) {
        this.m_vdef14 = ((String)value);
      }
      else if (name.equals("vdef15")) {
        this.m_vdef15 = ((String)value);
      }
      else if (name.equals("vdef16")) {
        this.m_vdef16 = ((String)value);
      }
      else if (name.equals("vdef17")) {
        this.m_vdef17 = ((String)value);
      }
      else if (name.equals("vdef18")) {
        this.m_vdef18 = ((String)value);
      }
      else if (name.equals("vdef19")) {
        this.m_vdef19 = ((String)value);
      }
      else if (name.equals("vdef20")) {
        this.m_vdef20 = ((String)value);
      }
      else if (name.equals("pk_defdoc1")) {
        this.m_pk_defdoc1 = ((String)value);
      }
      else if (name.equals("pk_defdoc2")) {
        this.m_pk_defdoc2 = ((String)value);
      }
      else if (name.equals("pk_defdoc3")) {
        this.m_pk_defdoc3 = ((String)value);
      }
      else if (name.equals("pk_defdoc4")) {
        this.m_pk_defdoc4 = ((String)value);
      }
      else if (name.equals("pk_defdoc5")) {
        this.m_pk_defdoc5 = ((String)value);
      }
      else if (name.equals("pk_defdoc6")) {
        this.m_pk_defdoc6 = ((String)value);
      }
      else if (name.equals("pk_defdoc7")) {
        this.m_pk_defdoc7 = ((String)value);
      }
      else if (name.equals("pk_defdoc8")) {
        this.m_pk_defdoc8 = ((String)value);
      }
      else if (name.equals("pk_defdoc9")) {
        this.m_pk_defdoc9 = ((String)value);
      }
      else if (name.equals("pk_defdoc10")) {
        this.m_pk_defdoc10 = ((String)value);
      }
      else if (name.equals("pk_defdoc11")) {
        this.m_pk_defdoc11 = ((String)value);
      }
      else if (name.equals("pk_defdoc12")) {
        this.m_pk_defdoc12 = ((String)value);
      }
      else if (name.equals("pk_defdoc13")) {
        this.m_pk_defdoc13 = ((String)value);
      }
      else if (name.equals("pk_defdoc14")) {
        this.m_pk_defdoc14 = ((String)value);
      }
      else if (name.equals("pk_defdoc15")) {
        this.m_pk_defdoc15 = ((String)value);
      }
      else if (name.equals("pk_defdoc16")) {
        this.m_pk_defdoc16 = ((String)value);
      }
      else if (name.equals("pk_defdoc17")) {
        this.m_pk_defdoc17 = ((String)value);
      }
      else if (name.equals("pk_defdoc18")) {
        this.m_pk_defdoc18 = ((String)value);
      }
      else if (name.equals("pk_defdoc19")) {
        this.m_pk_defdoc19 = ((String)value);
      }
      else if (name.equals("pk_defdoc20")) {
        this.m_pk_defdoc20 = ((String)value);
      }
      else if (name.equals("ccustbankid")) {
        this.m_ccustbankid = ((String)value);
      }
      else if (name.equals("ccalbodyid")) {
        this.m_ccalbodyid = ((String)value);
      }
      else if (name.equals("ccurrencyid")) {
        this.m_ccurrencyid = ((String)value);
      }
      else if (name.equals("ts")) {
        this.m_ts = new UFDateTime(value.toString());
      }
      else if (name.equals("cdispatcherid")) {
        this.m_cdispatcherid = ((String)value);
      }
      else if (name.equals("bcodechanged")) {
        this.m_bcodechanged = ((UFBoolean)value).booleanValue();
      }
      else if (name.equals("voldreceiptcode")) {
        this.m_voldreceiptcode = ((String)value);
      }
      else if (name.equals("ntotalsummny")) {
        this.m_ntotalsummny = new UFDouble(value.toString());
      }
      else if (name.equals("nstrikemny")) {
        this.m_nstrikemny = new UFDouble(value.toString());
      }
      else if (name.equals("nnetmny")) {
        this.m_nnetmny = new UFDouble(value.toString());
      }
      else if (name.equals("iprintcount")) {
        this.iprintcount = ((Integer)value);
      }
      else if (name.equals("fcounteractflag")) {
        this.m_fcounteractflag = ((Integer)value);
      }
      else if (name.equals("vprintcustname")) {
        this.m_vprintcustname = ((String)value);
      }
      else if (name.equals("dbilltime")) {
        this.m_dbilltime = ((String)value);
      }
      else if (name.equals("daudittime")) {
        this.m_daudittime = ((String)value);
      }
      else if (name.equals("dmoditime")) {
        this.m_dmoditime = ((String)value);
      }else if(name.equals("cjje1")){
    	this.cjje1 = ((String)value);
      }else if(name.equals("cjje2")){
    	this.cjje2=((String)value);
      }
    }
    catch (ClassCastException e)
    {
      throw new ClassCastException(NCLangRes4VoTransl.getNCLangRes().getStrByID("sopub", "UPPsopub-000173", null, new String[] { name, value + "" }));
    }
  }

  public boolean getBcodechanged()
  {
    return this.m_bcodechanged;
  }

  public String getCcalbodyid()
  {
    return this.m_ccalbodyid;
  }

  public String getCcurrencyid()
  {
    return this.m_ccurrencyid;
  }

  public String getCcustbankid()
  {
    return this.m_ccustbankid;
  }

  public String getcdispatcherid()
  {
    return this.m_cdispatcherid;
  }

  public UFBoolean getIsGather()
  {
    return this.m_isGather;
  }

  public UFDouble getNnetmny() {
    return this.m_nnetmny;
  }

  public UFDouble getNstrikemny()
  {
    return this.m_nstrikemny;
  }

  public UFDouble getNtotalsummny()
  {
    return this.m_ntotalsummny;
  }

  public UFDateTime getTs()
  {
    return this.m_ts;
  }

  public String getVoldreceiptcode()
  {
    return this.m_voldreceiptcode;
  }

  public void setBcodechanged(boolean newBinitflag)
  {
    this.m_bcodechanged = newBinitflag;
  }

  public void setCcalbodyid(String newCcalbodyid)
  {
    this.m_ccalbodyid = newCcalbodyid;
  }

  public void setCcurrencyid(String newCcurrencyid)
  {
    this.m_ccurrencyid = newCcurrencyid;
  }

  public void setCcustbankid(String newCcustbankid)
  {
    this.m_ccustbankid = newCcustbankid;
  }

  public void setcdispatcherid(String newcdispatcherid)
  {
    this.m_cdispatcherid = newcdispatcherid;
  }

  public void setIsGather(UFBoolean newValue)
  {
    this.m_isGather = newValue;
  }

  public void setNnetmny(UFDouble newNnetmny)
  {
    this.m_nnetmny = newNnetmny;
  }

  public void setNstrikemny(UFDouble newNstrikemny)
  {
    this.m_nstrikemny = newNstrikemny;
  }

  public void setNtotalsummny(UFDouble newNtotalsummny)
  {
    this.m_ntotalsummny = newNtotalsummny;
  }

  public void setTs(UFDateTime newTs)
  {
    this.m_ts = newTs;
  }

  public void setVoldreceiptcode(String newVreceiptcode)
  {
    this.m_voldreceiptcode = newVreceiptcode;
  }

  public Integer getIprintcount()
  {
    return this.iprintcount;
  }

  public void setIprintcount(Integer iprintcount)
  {
    this.iprintcount = iprintcount;
  }

  public Integer getFcounteractflag()
  {
    return this.m_fcounteractflag;
  }

  public void setFcounteractflag(Integer m_fcounteractflag)
  {
    this.m_fcounteractflag = m_fcounteractflag;
  }

  public void setVprintcustname(String vprintcustname) {
    this.m_vprintcustname = vprintcustname; }

  public String getVprintcustname() {
    return this.m_vprintcustname;
  }

  public String getDbilltime()
  {
    return this.m_dbilltime;
  }

  public void setDbilltime(String dbilltime)
  {
    this.m_dbilltime = dbilltime;
  }

  public String getDaudittime()
  {
    return this.m_daudittime;
  }

  public void setDaudittime(String daudittime)
  {
    this.m_daudittime = daudittime;
  }

  public String getDmoditime()
  {
    return this.m_dmoditime;
  }

  public void setDmoditime(String dmoditime)
  {
    this.m_dmoditime = dmoditime;
  }

  public String[] getNullFieldNames() {
    return new String[0];
  }

  public int[] getNullFieldTypes() {
    return new int[0];
  }
}