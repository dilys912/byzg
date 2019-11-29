package nc.vo.rc.receive;

import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.ObjectUtils;

public class ArriveorderHeaderVO extends CircularlyAccessibleValueObject
{
  private ArriveorderItemVO m_voBody = null;
  public String m_carriveorderid;
  public String m_pk_corp;
  public String m_varrordercode;
  public UFDate m_dreceivedate;
  public String m_ctransmodeid;
  public String m_cstoreorganization;
  public String m_creceivepsn;
  public String m_cdeptid;
  public String m_cemployeeid;
  public String m_caccountyear;
  public Integer m_ibillstatus;
  public String m_coperator;
  public String m_cbilltype;
  public String m_vmemo;
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
  private UFBoolean bisback;
  private String carriveorderrowid;
  private String carriveorderrowts;
  public String isauditted;
  private UFBoolean isByChk;
  public UFBoolean m_bcheckin;
  public String m_cauditpsn;
  public String m_cbiztype;
  public String m_cbiztypename;
  public String m_cemployee;
  public String m_cfreecustid;
  public String m_creceivepsnlist;
  public String m_ctransmode;
  private String m_cuserid;
  public String m_cvendorbaseid;
  public String m_cvendormangid;
  public UFDate m_dauditdate;
  public String m_deptname;
  private UFBoolean[] m_RowExt = null;

  private String[] m_RowIds = null;
  private String[] m_RowTss = null;
  private String[] m_UpsourceRowIds = null;
  private String m_UpsourceRowId = null;
  private String m_UpsourceBillType = null;
  private String m_ts;
  public String m_vendor;
  public String m_vmemoname;
  private UFDouble ufdAccWar;
  private UFDouble ufdElg;
  private UFDouble ufdElgNot;
  private String vbackreasonh;
  private String m_coperatoridnow;
  private Integer m_iprintcount;
  private String m_pk_purcorp;
  private UFDateTime m_tmaketime;
  private UFDateTime m_taudittime;
  private UFDateTime m_tlastmaketime;
  private UFDouble m_num;
  private UFDouble m_naccumnum;

  public void setBodyVo(ArriveorderItemVO itemVo)
  {
    this.m_voBody = itemVo;
  }

  public ArriveorderItemVO getBodyVo()
  {
    return this.m_voBody;
  }

  public boolean isAuditted()
  {
    return (getIbillstatus() != null) && ((getIbillstatus().intValue() == 2) || (getIbillstatus().intValue() == 3));
  }

  public UFDouble getBBNum()
  {
    return this.m_num;
  }

  public UFDouble getBBNaccumnum()
  {
    return this.m_naccumnum;
  }

  public void setBBNum(UFDouble ufdNewVal)
  {
    this.m_num = ufdNewVal;
  }

  public void setBBNaccumnum(UFDouble ufdNewVal)
  {
    this.m_naccumnum = ufdNewVal;
  }

  public void setTmaketime(UFDateTime newVal)
  {
    this.m_tmaketime = newVal;
  }

  public void setTlastmaketime(UFDateTime newVal)
  {
    this.m_tlastmaketime = newVal;
  }

  public void setTaudittime(UFDateTime newVal)
  {
    this.m_taudittime = newVal;
  }

  public UFDateTime getTmaketime()
  {
    return this.m_tmaketime;
  }

  public UFDateTime getTlastmaketime()
  {
    return this.m_tlastmaketime;
  }

  public UFDateTime getTaudittime()
  {
    return this.m_taudittime;
  }

  public ArriveorderHeaderVO()
  {
  }

  public ArriveorderHeaderVO(String newCarriveorderid)
  {
    this.m_carriveorderid = newCarriveorderid;
  }

  public Object clone()
  {
    Object oCloned = null;
    try {
      oCloned = ObjectUtils.serializableClone(this);
    } catch (Exception e) {
      SCMEnv.out(e);
    }
    return oCloned;
  }

  public String getPk_purcorp()
  {
    return this.m_pk_purcorp;
  }

  public void setPk_purcorp(String strNewVal)
  {
    this.m_pk_purcorp = strNewVal;
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
    return "Arriveorder";
  }

  public String getPrimaryKey()
  {
    return this.m_carriveorderid;
  }

  public void setPrimaryKey(String newCarriveorderid)
  {
    this.m_carriveorderid = newCarriveorderid;
  }

  public String getCarriveorderid()
  {
    return this.m_carriveorderid;
  }

  public String getPk_corp()
  {
    return this.m_pk_corp;
  }

  public String getVarrordercode()
  {
    return this.m_varrordercode;
  }

  public UFDate getDreceivedate()
  {
    return this.m_dreceivedate;
  }

  public String getCtransmodeid()
  {
    return this.m_ctransmodeid;
  }

  public String getCstoreorganization()
  {
    return this.m_cstoreorganization;
  }

  public String getCreceivepsn()
  {
    return this.m_creceivepsn;
  }

  public String getCdeptid()
  {
    return this.m_cdeptid;
  }

  public String getCemployeeid()
  {
    return this.m_cemployeeid;
  }

  public String getCaccountyear()
  {
    return this.m_caccountyear;
  }

  public Integer getIbillstatus()
  {
    return this.m_ibillstatus;
  }

  public String getCoperator()
  {
    return this.m_coperator;
  }

  public String getCbilltype()
  {
    return this.m_cbilltype;
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

  public void setCarriveorderid(String newCarriveorderid)
  {
    this.m_carriveorderid = newCarriveorderid;
  }

  public void setPk_corp(String newPk_corp)
  {
    this.m_pk_corp = newPk_corp;
  }

  public void setVarrordercode(String newVarrordercode)
  {
    this.m_varrordercode = newVarrordercode;
  }

  public void setDreceivedate(UFDate newDreceivedate)
  {
    this.m_dreceivedate = newDreceivedate;
  }

  public void setCtransmodeid(String newCtransmodeid)
  {
    this.m_ctransmodeid = newCtransmodeid;
  }

  public void setCstoreorganization(String newCstoreorganization)
  {
    this.m_cstoreorganization = newCstoreorganization;
  }

  public void setCreceivepsn(String strTmp)
  {
    this.m_creceivepsn = strTmp;
  }

  public void setCdeptid(String newCdeptid)
  {
    this.m_cdeptid = newCdeptid;
  }

  public void setCemployeeid(String newCemployeeid)
  {
    this.m_cemployeeid = newCemployeeid;
  }

  public void setCaccountyear(String newCaccountyear)
  {
    this.m_caccountyear = newCaccountyear;
  }

  public void setIbillstatus(Integer newIbillstatus)
  {
    this.m_ibillstatus = newIbillstatus;
  }

  public void setCoperator(String newCoperator)
  {
    this.m_coperator = newCoperator;
  }

  public void setCbilltype(String newCbilltype)
  {
    this.m_cbilltype = newCbilltype;
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

  public void validate()
    throws ValidationException
  {
    StringBuffer errInfo = new StringBuffer();

    if (this.m_carriveorderid == null) {
      errInfo.append("m_carriveorderid");
    }

    String sMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("40040301", "UPP40040301-000268");
    if (errInfo.toString().length() > 0) {
      sMessage = sMessage + errInfo.toString();

      throw new NullFieldException(sMessage);
    }
  }

  public String[] getAttributeNames()
  {
    return new String[] { "pk_corp", "varrordercode", "dreceivedate", "cfreecustid", "cvendormangid", "cvendorbaseid", "cbiztype", "ctransmodeid", "cstoreorganization", "creceivepsn", "cdeptid", "cemployeeid", "caccountyear", "ibillstatus", "coperator", "cbilltype", "vmemo", "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", "pk_defdoc8", "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13", "pk_defdoc14", "pk_defdoc15", "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20", "isauditted", "dauditdate", "cauditpsn", "cbiztypename", "vendor", "cemployee", "deptname", "ctransmode", "creceivepsnlist", "vmemoname", "ts", "cuserid", "bisback", "vbackreasonh", "bcheckin", "coperatoridnow", "iprintcount", "pk_purcorp", "tmaketime", "taudittime", "tlastmaketime" };
  }

  public Object getAttributeValue(String attributeName)
  {
    if (attributeName.equals("carriveorderid"))
      return this.m_carriveorderid;
    if (attributeName.equals("pk_corp"))
      return this.m_pk_corp;
    if (attributeName.equals("varrordercode"))
      return this.m_varrordercode;
    if (attributeName.equals("dreceivedate"))
      return this.m_dreceivedate;
    if (attributeName.equals("cfreecustid"))
      return this.m_cfreecustid;
    if (attributeName.equals("cvendormangid"))
      return this.m_cvendormangid;
    if (attributeName.equals("cvendorbaseid"))
      return this.m_cvendorbaseid;
    if (attributeName.equals("cbiztype"))
      return this.m_cbiztype;
    if (attributeName.equals("ctransmodeid"))
      return this.m_ctransmodeid;
    if (attributeName.equals("cstoreorganization"))
      return this.m_cstoreorganization;
    if (attributeName.equals("creceivepsn"))
      return this.m_creceivepsn;
    if (attributeName.equals("cdeptid"))
      return this.m_cdeptid;
    if (attributeName.equals("cemployeeid"))
      return this.m_cemployeeid;
    if (attributeName.equals("caccountyear"))
      return this.m_caccountyear;
    if (attributeName.equals("ibillstatus"))
      return this.m_ibillstatus;
    if (attributeName.equals("coperator"))
      return this.m_coperator;
    if (attributeName.equals("cbilltype"))
      return this.m_cbilltype;
    if (attributeName.equals("vmemo"))
      return this.m_vmemo;
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
    if (attributeName.equals("isauditted"))
      return this.isauditted;
    if (attributeName.equals("dauditdate"))
      return this.m_dauditdate;
    if (attributeName.equals("cauditpsn")) {
      return this.m_cauditpsn;
    }

    if (attributeName.equals("cbiztypename"))
      return this.m_cbiztypename;
    if (attributeName.equals("vendor"))
      return this.m_vendor;
    if (attributeName.equals("cemployee"))
      return this.m_cemployee;
    if (attributeName.equals("deptname"))
      return this.m_deptname;
    if (attributeName.equals("ctransmode"))
      return this.m_ctransmode;
    if (attributeName.equals("creceivepsnlist"))
      return this.m_creceivepsnlist;
    if (attributeName.equals("vmemoname"))
      return this.m_vmemoname;
    if (attributeName.equals("ts"))
      return this.m_ts;
    if (attributeName.equals("cuserid")) {
      if (this.m_cuserid == null) {
        return this.m_coperator;
      }
      return this.m_cuserid;
    }if (attributeName.equals("bisback"))
      return this.bisback;
    if (attributeName.equals("vbackreasonh"))
      return this.vbackreasonh;
    if (attributeName.equals("bcheckin"))
      return this.m_bcheckin;
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
      if (name.equals("carriveorderid"))
        this.m_carriveorderid = ((String)value);
      else if (name.equals("pk_corp"))
        this.m_pk_corp = ((String)value);
      else if (name.equals("varrordercode"))
        this.m_varrordercode = ((String)value);
      else if (name.equals("dreceivedate"))
        this.m_dreceivedate = ((UFDate)value);
      else if (name.equals("cfreecustid"))
        this.m_cfreecustid = ((String)value);
      else if (name.equals("cvendormangid"))
        this.m_cvendormangid = ((String)value);
      else if (name.equals("cvendorbaseid"))
        this.m_cvendorbaseid = ((String)value);
      else if (name.equals("cbiztype"))
        this.m_cbiztype = ((String)value);
      else if (name.equals("ctransmodeid"))
        this.m_ctransmodeid = ((String)value);
      else if (name.equals("cstoreorganization"))
        this.m_cstoreorganization = ((String)value);
      else if (name.equals("creceivepsn"))
        this.m_creceivepsn = ((String)value);
      else if (name.equals("cdeptid"))
        this.m_cdeptid = ((String)value);
      else if (name.equals("cemployeeid"))
        this.m_cemployeeid = ((String)value);
      else if (name.equals("caccountyear"))
        this.m_caccountyear = ((String)value);
      else if (name.equals("ibillstatus"))
        this.m_ibillstatus = ((Integer)value);
      else if (name.equals("coperator"))
        this.m_coperator = ((String)value);
      else if (name.equals("cbilltype"))
        this.m_cbilltype = ((String)value);
      else if (name.equals("vmemo"))
        this.m_vmemo = ((String)value);
      else if (name.equals("vdef1"))
        this.m_vdef1 = (value == null ? null : value.toString());
      else if (name.equals("vdef2"))
        this.m_vdef2 = (value == null ? null : value.toString());
      else if (name.equals("vdef3"))
        this.m_vdef3 = (value == null ? null : value.toString());
      else if (name.equals("vdef4"))
        this.m_vdef4 = (value == null ? null : value.toString());
      else if (name.equals("vdef5"))
        this.m_vdef5 = (value == null ? null : value.toString());
      else if (name.equals("vdef6"))
        this.m_vdef6 = (value == null ? null : value.toString());
      else if (name.equals("vdef7"))
        this.m_vdef7 = (value == null ? null : value.toString());
      else if (name.equals("vdef8"))
        this.m_vdef8 = (value == null ? null : value.toString());
      else if (name.equals("vdef9"))
        this.m_vdef9 = (value == null ? null : value.toString());
      else if (name.equals("vdef10"))
        this.m_vdef10 = (value == null ? null : value.toString());
      else if (name.equals("vdef11"))
        this.m_vdef11 = (value == null ? null : value.toString());
      else if (name.equals("vdef12"))
        this.m_vdef12 = (value == null ? null : value.toString());
      else if (name.equals("vdef13"))
        this.m_vdef13 = (value == null ? null : value.toString());
      else if (name.equals("vdef14"))
        this.m_vdef14 = (value == null ? null : value.toString());
      else if (name.equals("vdef15"))
        this.m_vdef15 = (value == null ? null : value.toString());
      else if (name.equals("vdef16"))
        this.m_vdef16 = (value == null ? null : value.toString());
      else if (name.equals("vdef17"))
        this.m_vdef17 = (value == null ? null : value.toString());
      else if (name.equals("vdef18"))
        this.m_vdef18 = (value == null ? null : value.toString());
      else if (name.equals("vdef19"))
        this.m_vdef19 = (value == null ? null : value.toString());
      else if (name.equals("vdef20"))
        this.m_vdef20 = (value == null ? null : value.toString());
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
      else if (name.equals("isauditted"))
        this.isauditted = ((String)value);
      else if (name.equals("dauditdate"))
        this.m_dauditdate = ((UFDate)value);
      else if (name.equals("cauditpsn")) {
        this.m_cauditpsn = ((String)value);
      }
      else if (name.equals("cbiztypename"))
        this.m_cbiztypename = ((String)value);
      else if (name.equals("vendor"))
        this.m_vendor = ((String)value);
      else if (name.equals("cemployee"))
        this.m_cemployee = ((String)value);
      else if (name.equals("ctransmode"))
        this.m_ctransmode = ((String)value);
      else if (name.equals("deptname"))
        this.m_deptname = ((String)value);
      else if (name.equals("creceivepsnlist"))
        this.m_creceivepsnlist = ((String)value);
      else if (name.equals("vmemoname"))
        this.m_vmemoname = ((String)value);
      else if (name.equals("ts"))
        this.m_ts = ((String)value);
      else if (name.equals("cuserid"))
        this.m_cuserid = ((String)value);
      else if (name.equals("bisback"))
        this.bisback = ((UFBoolean)value);
      else if (name.equals("vbackreasonh"))
        this.vbackreasonh = ((String)value);
      else if (name.equals("bcheckin"))
        this.m_bcheckin = ((UFBoolean)value);
      else if (name.equals("coperatoridnow"))
        this.m_coperatoridnow = ((String)value);
      else if (name.equals("iprintcount"))
        this.m_iprintcount = ((Integer)value);
      else if (name.equals("pk_purcorp"))
        this.m_pk_purcorp = ((String)value);
      else if (name.equals("tmaketime"))
        this.m_tmaketime = PuPubVO.getUFDateTime(value);
      else if (name.equals("taudittime"))
        this.m_taudittime = PuPubVO.getUFDateTime(value);
      else if (name.equals("tlastmaketime"))
        this.m_tlastmaketime = PuPubVO.getUFDateTime(value);
    }
    catch (ClassCastException e) {
      throw new ClassCastException("setAttributeValue方法中为 " + name + " 赋值时类型转换错误！（值：" + value + "）");
    }
  }

  public UFDouble getAccWar()
  {
    return this.ufdAccWar;
  }

  public UFBoolean getBcheckin()
  {
    return this.m_bcheckin;
  }

  public UFBoolean getBisback()
  {
    return this.bisback;
  }

  public String getCarriveorderrowid()
  {
    return this.carriveorderrowid;
  }

  public String getCarriveorderrowts()
  {
    return this.carriveorderrowts;
  }

  public String getCauditpsn()
  {
    return this.m_cauditpsn;
  }

  public String getCbiztype()
  {
    return this.m_cbiztype;
  }

  public String getCbiztypename()
  {
    return this.m_cbiztypename;
  }

  public String getCemployee()
  {
    return this.m_cemployee;
  }

  public String getCfreecustid()
  {
    return this.m_cfreecustid;
  }

  public String getCtransmode()
  {
    return this.m_ctransmode;
  }

  public String getCuserid()
  {
    return this.m_cuserid;
  }

  public String getCvendorbaseid()
  {
    return this.m_cvendorbaseid;
  }

  public String getCvendormangid()
  {
    return this.m_cvendormangid;
  }

  public UFDate getDauditdate()
  {
    return this.m_dauditdate;
  }

  public String getDeptname()
  {
    return this.m_deptname;
  }

  public UFDouble getElg()
  {
    return this.ufdElg;
  }

  public UFDouble getElgNot()
  {
    return this.ufdElgNot;
  }

  public String getIsauditted()
  {
    return this.isauditted;
  }

  public UFBoolean getIsByChk()
  {
    return this.isByChk;
  }

  public UFBoolean[] getRowExt()
  {
    return this.m_RowExt;
  }

  public String[] getRowIds()
  {
    return this.m_RowIds;
  }

  public String[] getRowTss()
  {
    return this.m_RowTss;
  }

  public String getTs()
  {
    return this.m_ts;
  }

  public String getVbackreasonh()
  {
    return this.vbackreasonh;
  }

  public String getVendor()
  {
    return this.m_vendor;
  }

  public String getVmemoname()
  {
    return this.m_vmemoname;
  }

  public void setAccWar(UFDouble newUfdAccWar)
  {
    this.ufdAccWar = newUfdAccWar;
  }

  public void setBcheckin(UFBoolean s)
  {
    this.m_bcheckin = s;
  }

  public void setBisback(UFBoolean newBisback)
  {
    this.bisback = newBisback;
  }

  public void setCarriveorderrowid(String newCarriveorderrowid)
  {
    this.carriveorderrowid = newCarriveorderrowid;
  }

  public void setCarriveorderrowts(String newCarriveorderrowts)
  {
    this.carriveorderrowts = newCarriveorderrowts;
  }

  public void setCauditpsn(String newM_cauditpsn)
  {
    this.m_cauditpsn = newM_cauditpsn;
  }

  public void setCbiztype(String newCbiztype)
  {
    this.m_cbiztype = newCbiztype;
  }

  public void setCbiztypename(String newCbiztypename)
  {
    this.m_cbiztypename = newCbiztypename;
  }

  public void setCemployee(String newCemployee)
  {
    this.m_cemployee = newCemployee;
  }

  public void setCfreecustid(String newM_cfreecustid)
  {
    this.m_cfreecustid = newM_cfreecustid;
  }

  public void setCtransmode(String newCtransmode)
  {
    this.m_ctransmode = newCtransmode;
  }

  public void setCuserid(String newM_cuserid)
  {
    this.m_cuserid = newM_cuserid;
  }

  public void setCvendorbaseid(String newM_cvendorbaseid)
  {
    this.m_cvendorbaseid = newM_cvendorbaseid;
  }

  public void setCvendormangid(String newM_cvendormangid)
  {
    this.m_cvendormangid = newM_cvendormangid;
  }

  public void setDauditdate(UFDate newM_dauditdate)
  {
    this.m_dauditdate = newM_dauditdate;
  }

  public void setDeptname(String newDeptname)
  {
    this.m_deptname = newDeptname;
  }

  public void setElg(UFDouble newUfdElg)
  {
    this.ufdElg = newUfdElg;
  }

  public void setElgNot(UFDouble newUfdElgNot)
  {
    this.ufdElgNot = newUfdElgNot;
  }

  public void setIsauditted(String newIsauditted)
  {
    this.isauditted = newIsauditted;
  }

  public void setIsByChk(UFBoolean newIsByChk)
  {
    this.isByChk = newIsByChk;
  }

  public void setRowExt(UFBoolean[] newM_RowExt)
  {
    this.m_RowExt = newM_RowExt;
  }

  public void setRowIds(String[] newM_RowIds)
  {
    this.m_RowIds = newM_RowIds;
  }

  public void setUpsourceBillType(String sBillType)
  {
    this.m_UpsourceBillType = sBillType;
  }

  public String getUpsourceBillType()
  {
    return this.m_UpsourceBillType;
  }

  public String[] getUpsourceRowIds()
  {
    return this.m_UpsourceRowIds;
  }

  public void setUpsourceRowIds(String[] newM_RowIds)
  {
    this.m_UpsourceRowIds = newM_RowIds;
  }

  public String getUpsourceRowId()
  {
    return this.m_UpsourceRowId;
  }

  public void setUpsourceRowId(String newM_RowIds)
  {
    this.m_UpsourceRowId = newM_RowIds;
  }

  public void setRowTss(String[] newM_RowTss)
  {
    this.m_RowTss = newM_RowTss;
  }

  public void setTs(String newM_ts)
  {
    this.m_ts = newM_ts;
  }

  public void setVbackreasonh(String newVbackreasonh)
  {
    this.vbackreasonh = newVbackreasonh;
  }

  public void setVendor(String newVendor)
  {
    this.m_vendor = newVendor;
  }

  public void setVmemoname(String s)
  {
    this.m_vmemoname = s;
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