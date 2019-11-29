package nc.vo.po;

import java.util.ArrayList;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.field.pu.BillCodeField;
import nc.vo.scm.field.pu.DefField;
import nc.vo.scm.field.pu.FieldDBValidateInterface;
import nc.vo.scm.field.pu.FieldDBValidateVO;
import nc.vo.scm.field.pu.MemoField;
import nc.vo.scm.field.pu.MoneyField;
import nc.vo.scm.pu.ObjectAnalyzer;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.ObjectUtils;
import nc.vo.scm.pub.smart.SmartVO;

public class OrderHeaderVO extends SmartVO
  implements FieldDBValidateInterface
{
  public static final UFBoolean BISDELIVER_NO = VariableConst.UFBOOLEAN_FALSE;

  public static final UFBoolean BISDELIVER_YES = VariableConst.UFBOOLEAN_TRUE;
  public static final UFBoolean BISLATEST_NO = VariableConst.UFBOOLEAN_FALSE;

  public static final UFBoolean BISLATEST_YES = VariableConst.UFBOOLEAN_TRUE;
  public static final UFBoolean BISREPLENISH_NO = VariableConst.UFBOOLEAN_FALSE;

  public static final UFBoolean BISREPLENISH_YES = VariableConst.UFBOOLEAN_TRUE;
  public static final UFBoolean BRETURN_NO = VariableConst.UFBOOLEAN_FALSE;

  public static final UFBoolean BRETURN_YES = VariableConst.UFBOOLEAN_TRUE;

  public static final Integer NVERSION_FIRST = VariableConst.ONE_INTEGER;

  public boolean isAuditted()
  {
    return (getForderstatus() != null) && ((getForderstatus().intValue() == 2) || (getForderstatus().intValue() == 3) || (getForderstatus().intValue() == 5));
  }

  public void setTmaketime(UFDateTime newVal)
  {
    setAttributeValue("tmaketime", newVal);
  }

  public void setTlastmaketime(UFDateTime newVal)
  {
    setAttributeValue("tlastmaketime", newVal);
  }

  public void setTaudittime(UFDateTime newVal)
  {
    setAttributeValue("taudittime", newVal);
  }

  public UFDateTime getTmaketime()
  {
    return SmartVODataUtils.getUFDateTime(getAttributeValue("tmaketime"));
  }

  public UFDateTime getTlastmaketime()
  {
    return SmartVODataUtils.getUFDateTime(getAttributeValue("tlastmaketime"));
  }

  public UFDateTime getTaudittime()
  {
    return SmartVODataUtils.getUFDateTime(getAttributeValue("taudittime"));
  }

  public OrderHeaderVO()
  {
  }

  public Class getVOMetaClass()
  {
    return OrderHeaderVOMeta.class;
  }

  public String getPk_corp()
  {
    return (String)getAttributeValue("pk_corp");
  }

  public void setPk_corp(String value)
  {
    setAttributeValue("pk_corp", value);
  }

  public String getVdef4()
  {
    return (String)getAttributeValue("vdef4");
  }

  public void setVdef4(String value)
  {
    setAttributeValue("vdef4", value);
  }

  public String getVdef18()
  {
    return (String)getAttributeValue("vdef18");
  }

  public void setVdef18(String value)
  {
    setAttributeValue("vdef18", value);
  }

  public Integer getDr()
  {
    Object value = getAttributeValue("dr");
    return SmartVODataUtils.getInteger(value);
  }

  public void setDr(Integer value)
  {
    setAttributeValue("dr", value);
  }

  public String getVdef16()
  {
    return (String)getAttributeValue("vdef16");
  }

  public void setVdef16(String value)
  {
    setAttributeValue("vdef16", value);
  }

  public String getVdef17()
  {
    return (String)getAttributeValue("vdef17");
  }

  public void setVdef17(String value)
  {
    setAttributeValue("vdef17", value);
  }

  public UFDate getDorderbegindate()
  {
    Object value = getAttributeValue("dorderbegindate");
    return SmartVODataUtils.getUFDate(value);
  }

  public void setDorderbegindate(UFDate value)
  {
    setAttributeValue("dorderbegindate", value);
  }

  public UFDate getDorderdate()
  {
    Object value = getAttributeValue("dorderdate");
    return SmartVODataUtils.getUFDate(value);
  }

  public void setDorderdate(UFDate value)
  {
    setAttributeValue("dorderdate", value);
  }

  public Integer getForderstatus()
  {
    Object value = getAttributeValue("forderstatus");
    return SmartVODataUtils.getInteger(value);
  }

  public void setForderstatus(Integer value)
  {
    setAttributeValue("forderstatus", value);
  }

  public String getVdef19()
  {
    return (String)getAttributeValue("vdef19");
  }

  public void setVdef19(String value)
  {
    setAttributeValue("vdef19", value);
  }

  public String getPk_defdoc6()
  {
    return (String)getAttributeValue("pk_defdoc6");
  }

  public void setPk_defdoc6(String value)
  {
    setAttributeValue("pk_defdoc6", value);
  }

  public UFDouble getNexchangeotobrate()
  {
    Object value = getAttributeValue("nexchangeotobrate");
    return SmartVODataUtils.getUFDouble(value);
  }

  public void setNexchangeotobrate(UFDouble value)
  {
    setAttributeValue("nexchangeotobrate", value);
  }

  public String getCfreecustid()
  {
    return (String)getAttributeValue("cfreecustid");
  }

  public void setCfreecustid(String value)
  {
    setAttributeValue("cfreecustid", value);
  }

  public String getCaccountbankid()
  {
    return (String)getAttributeValue("caccountbankid");
  }

  public void setCaccountbankid(String value)
  {
    setAttributeValue("caccountbankid", value);
  }

  public String getCreciever()
  {
    return (String)getAttributeValue("creciever");
  }

  public void setCreciever(String value)
  {
    setAttributeValue("creciever", value);
  }

  public String getPk_defdoc3()
  {
    return (String)getAttributeValue("pk_defdoc3");
  }

  public void setPk_defdoc3(String value)
  {
    setAttributeValue("pk_defdoc3", value);
  }

  public String getCaccountyear()
  {
    return (String)getAttributeValue("caccountyear");
  }

  public void setCaccountyear(String value)
  {
    setAttributeValue("caccountyear", value);
  }

  public UFBoolean getBreturn()
  {
    Object value = getAttributeValue("breturn");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public void setBreturn(UFBoolean value)
  {
    setAttributeValue("breturn", value);
  }

  public UFDate getDreplenishdate()
  {
    Object value = getAttributeValue("dreplenishdate");
    return SmartVODataUtils.getUFDate(value);
  }

  public void setDreplenishdate(UFDate value)
  {
    setAttributeValue("dreplenishdate", value);
  }

  public String getPk_defdoc2()
  {
    return (String)getAttributeValue("pk_defdoc2");
  }

  public void setPk_defdoc2(String value)
  {
    setAttributeValue("pk_defdoc2", value);
  }

  public String getPk_defdoc7()
  {
    return (String)getAttributeValue("pk_defdoc7");
  }

  public void setPk_defdoc7(String value)
  {
    setAttributeValue("pk_defdoc7", value);
  }

  public UFDouble getNtaxrate()
  {
    Object value = getAttributeValue("ntaxrate");
    return SmartVODataUtils.getUFDouble(value);
  }

  public void setNtaxrate(UFDouble value)
  {
    setAttributeValue("ntaxrate", value);
  }

  public String getVdef10()
  {
    return (String)getAttributeValue("vdef10");
  }

  public void setVdef10(String value)
  {
    setAttributeValue("vdef10", value);
  }

  public String getVdef5()
  {
    return (String)getAttributeValue("vdef5");
  }

  public void setVdef5(String value)
  {
    setAttributeValue("vdef5", value);
  }

  public UFDate getDorderenddate()
  {
    Object value = getAttributeValue("dorderenddate");
    return SmartVODataUtils.getUFDate(value);
  }

  public void setDorderenddate(UFDate value)
  {
    setAttributeValue("dorderenddate", value);
  }

  public String getCgiveinvoicevendor()
  {
    return (String)getAttributeValue("cgiveinvoicevendor");
  }

  public void setCgiveinvoicevendor(String value)
  {
    setAttributeValue("cgiveinvoicevendor", value);
  }

  public String getPk_defdoc13()
  {
    return (String)getAttributeValue("pk_defdoc13");
  }

  public void setPk_defdoc13(String value)
  {
    setAttributeValue("pk_defdoc13", value);
  }

  public UFDate getDvendorpressdate()
  {
    Object value = getAttributeValue("dvendorpressdate");
    return SmartVODataUtils.getUFDate(value);
  }

  public void setDvendorpressdate(UFDate value)
  {
    setAttributeValue("dvendorpressdate", value);
  }

  public String getVdef2()
  {
    return (String)getAttributeValue("vdef2");
  }

  public void setVdef2(String value)
  {
    setAttributeValue("vdef2", value);
  }

  public Integer getOrderstatus()
  {
    Object value = getAttributeValue("orderstatus");
    return SmartVODataUtils.getInteger(value);
  }

  public void setOrderstatus(Integer value)
  {
    setAttributeValue("orderstatus", value);
  }

  public String getCauditpsn()
  {
    return (String)getAttributeValue("cauditpsn");
  }

  public void setCauditpsn(String value)
  {
    setAttributeValue("cauditpsn", value);
  }

  public String getVorderendcode()
  {
    return (String)getAttributeValue("vorderendcode");
  }

  public void setVorderendcode(String value)
  {
    setAttributeValue("vorderendcode", value);
  }

  public String getPk_defdoc18()
  {
    return (String)getAttributeValue("pk_defdoc18");
  }

  public void setPk_defdoc18(String value)
  {
    setAttributeValue("pk_defdoc18", value);
  }

  public UFBoolean getBdeliver()
  {
    Object value = getAttributeValue("bdeliver");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public void setBdeliver(UFBoolean value)
  {
    setAttributeValue("bdeliver", value);
  }

  public String getPk_defdoc9()
  {
    return (String)getAttributeValue("pk_defdoc9");
  }

  public void setPk_defdoc9(String value)
  {
    setAttributeValue("pk_defdoc9", value);
  }

  public String getCtermprotocolid()
  {
    return (String)getAttributeValue("ctermprotocolid");
  }

  public void setCtermprotocolid(String value)
  {
    setAttributeValue("ctermprotocolid", value);
  }

  public UFBoolean getBisreplenish()
  {
    Object value = getAttributeValue("bisreplenish");
    UFBoolean bRet = SmartVODataUtils.getUFBoolean(value);
    if (bRet == null) {
      return BISREPLENISH_NO;
    }
    return bRet;
  }

  public void setBisreplenish(UFBoolean value)
  {
    setAttributeValue("bisreplenish", value);
  }

  public String getPk_defdoc19()
  {
    return (String)getAttributeValue("pk_defdoc19");
  }

  public void setPk_defdoc19(String value)
  {
    setAttributeValue("pk_defdoc19", value);
  }

  public String getPk_defdoc4()
  {
    return (String)getAttributeValue("pk_defdoc4");
  }

  public void setPk_defdoc4(String value)
  {
    setAttributeValue("pk_defdoc4", value);
  }

  public String getPk_defdoc5()
  {
    return (String)getAttributeValue("pk_defdoc5");
  }

  public void setPk_defdoc5(String value)
  {
    setAttributeValue("pk_defdoc5", value);
  }

  public String getVdef8()
  {
    return (String)getAttributeValue("vdef8");
  }

  public void setVdef8(String value)
  {
    setAttributeValue("vdef8", value);
  }

  public String getPk_defdoc8()
  {
    return (String)getAttributeValue("pk_defdoc8");
  }

  public void setPk_defdoc8(String value)
  {
    setAttributeValue("pk_defdoc8", value);
  }

  public String getCtransmodeid()
  {
    return (String)getAttributeValue("ctransmodeid");
  }

  public void setCtransmodeid(String value)
  {
    setAttributeValue("ctransmodeid", value);
  }

  public String getCvendormangid()
  {
    return (String)getAttributeValue("cvendormangid");
  }

  public void setCvendormangid(String value)
  {
    setAttributeValue("cvendormangid", value);
  }

  public String getCdeliveraddress()
  {
    return (String)getAttributeValue("cdeliveraddress");
  }

  public void setCdeliveraddress(String value)
  {
    setAttributeValue("cdeliveraddress", value);
  }

  public UFDouble getNprepaymaxmny()
  {
    Object value = getAttributeValue("nprepaymaxmny");
    return SmartVODataUtils.getUFDouble(value);
  }

  public void setNprepaymaxmny(UFDouble value)
  {
    setAttributeValue("nprepaymaxmny", value);
  }

  public String getCpurorganization()
  {
    return (String)getAttributeValue("cpurorganization");
  }

  public void setCpurorganization(String value)
  {
    setAttributeValue("cpurorganization", value);
  }

  public Integer getIdiscounttaxtype()
  {
    Object value = getAttributeValue("idiscounttaxtype");
    return SmartVODataUtils.getInteger(value);
  }

  public void setIdiscounttaxtype(Integer value)
  {
    setAttributeValue("idiscounttaxtype", value);
  }

  public String getVdef12()
  {
    return (String)getAttributeValue("vdef12");
  }

  public void setVdef12(String value)
  {
    setAttributeValue("vdef12", value);
  }

  public String getVorderbegincode()
  {
    return (String)getAttributeValue("vorderbegincode");
  }

  public void setVorderbegincode(String value)
  {
    setAttributeValue("vorderbegincode", value);
  }

  public String getPk_defdoc11()
  {
    return (String)getAttributeValue("pk_defdoc11");
  }

  public void setPk_defdoc11(String value)
  {
    setAttributeValue("pk_defdoc11", value);
  }

  public Integer getIprintcount()
  {
    Object value = getAttributeValue("iprintcount");
    return SmartVODataUtils.getInteger(value);
  }

  public void setIprintcount(Integer value)
  {
    setAttributeValue("iprintcount", value);
  }

  public UFDate getDrevisiondate()
  {
    Object value = getAttributeValue("drevisiondate");
    return SmartVODataUtils.getUFDate(value);
  }

  public void setDrevisiondate(UFDate value)
  {
    setAttributeValue("drevisiondate", value);
  }

  public String getVpaybillcode()
  {
    return (String)getAttributeValue("vpaybillcode");
  }

  public void setVpaybillcode(String value)
  {
    setAttributeValue("vpaybillcode", value);
  }

  public String getPk_defdoc10()
  {
    return (String)getAttributeValue("pk_defdoc10");
  }

  public void setPk_defdoc10(String value)
  {
    setAttributeValue("pk_defdoc10", value);
  }

  public String getVreceiptcode()
  {
    return (String)getAttributeValue("vreceiptcode");
  }

  public void setVreceiptcode(String value)
  {
    setAttributeValue("vreceiptcode", value);
  }

  public UFDouble getNprepaymny()
  {
    Object value = getAttributeValue("nprepaymny");
    return SmartVODataUtils.getUFDouble(value);
  }

  public void setNprepaymny(UFDouble value)
  {
    setAttributeValue("nprepaymny", value);
  }

  public String getCbiztype()
  {
    return (String)getAttributeValue("cbiztype");
  }

  public void setCbiztype(String value)
  {
    setAttributeValue("cbiztype", value);
  }

  public String getVordercode()
  {
    return (String)getAttributeValue("vordercode");
  }

  public void setVordercode(String value)
  {
    setAttributeValue("vordercode", value);
  }

  public String getPk_defdoc20()
  {
    return (String)getAttributeValue("pk_defdoc20");
  }

  public void setPk_defdoc20(String value)
  {
    setAttributeValue("pk_defdoc20", value);
  }

  public String getCcurrencytypeid()
  {
    return (String)getAttributeValue("ccurrencytypeid");
  }

  public void setCcurrencytypeid(String value)
  {
    setAttributeValue("ccurrencytypeid", value);
  }

  public String getVdef3()
  {
    return (String)getAttributeValue("vdef3");
  }

  public void setVdef3(String value)
  {
    setAttributeValue("vdef3", value);
  }

  public String getVdef13()
  {
    return (String)getAttributeValue("vdef13");
  }

  public void setVdef13(String value)
  {
    setAttributeValue("vdef13", value);
  }

  public Integer getIpaybillstatus()
  {
    Object value = getAttributeValue("ipaybillstatus");
    return SmartVODataUtils.getInteger(value);
  }

  public void setIpaybillstatus(Integer value)
  {
    setAttributeValue("ipaybillstatus", value);
  }

  public String getPk_defdoc12()
  {
    return (String)getAttributeValue("pk_defdoc12");
  }

  public void setPk_defdoc12(String value)
  {
    setAttributeValue("pk_defdoc12", value);
  }

  public String getPk_defdoc17()
  {
    return (String)getAttributeValue("pk_defdoc17");
  }

  public void setPk_defdoc17(String value)
  {
    setAttributeValue("pk_defdoc17", value);
  }

  public UFDouble getNexchangeotoarate()
  {
    Object value = getAttributeValue("nexchangeotoarate");
    return SmartVODataUtils.getUFDouble(value);
  }

  public void setNexchangeotoarate(UFDouble value)
  {
    setAttributeValue("nexchangeotoarate", value);
  }

  public String getCemployeeid()
  {
    return (String)getAttributeValue("cemployeeid");
  }

  public void setCemployeeid(String value)
  {
    setAttributeValue("cemployeeid", value);
  }

  public String getPk_defdoc15()
  {
    return (String)getAttributeValue("pk_defdoc15");
  }

  public void setPk_defdoc15(String value)
  {
    setAttributeValue("pk_defdoc15", value);
  }

  public String getVdef11()
  {
    return (String)getAttributeValue("vdef11");
  }

  public void setVdef11(String value)
  {
    setAttributeValue("vdef11", value);
  }

  public String getVdef7()
  {
    return (String)getAttributeValue("vdef7");
  }

  public void setVdef7(String value)
  {
    setAttributeValue("vdef7", value);
  }

  public String getPk_defdoc16()
  {
    return (String)getAttributeValue("pk_defdoc16");
  }

  public void setPk_defdoc16(String value)
  {
    setAttributeValue("pk_defdoc16", value);
  }

  public UFDate getDauditdate()
  {
    Object value = getAttributeValue("dauditdate");
    return SmartVODataUtils.getUFDate(value);
  }

  public void setDauditdate(UFDate value)
  {
    setAttributeValue("dauditdate", value);
  }

  public String getCorderid()
  {
    return (String)getAttributeValue("corderid");
  }

  public void setCorderid(String value)
  {
    setAttributeValue("corderid", value);
  }

  public String getVdef20()
  {
    return (String)getAttributeValue("vdef20");
  }

  public void setVdef20(String value)
  {
    setAttributeValue("vdef20", value);
  }

  public String getCunfreeze()
  {
    return (String)getAttributeValue("cunfreeze");
  }

  public void setCunfreeze(String value)
  {
    setAttributeValue("cunfreeze", value);
  }

  public String getVmemo()
  {
    return (String)getAttributeValue("vmemo");
  }

  public void setVmemo(String value)
  {
    setAttributeValue("vmemo", value);
  }

  public String getVdef1()
  {
    return (String)getAttributeValue("vdef1");
  }

  public void setVdef1(String value)
  {
    setAttributeValue("vdef1", value);
  }

  public String getPk_defdoc1()
  {
    return (String)getAttributeValue("pk_defdoc1");
  }

  public void setPk_defdoc1(String value)
  {
    setAttributeValue("pk_defdoc1", value);
  }

  public UFDouble getNsubscripmny()
  {
    Object value = getAttributeValue("nsubscripmny");
    return SmartVODataUtils.getUFDouble(value);
  }

  public void setNsubscripmny(UFDouble value)
  {
    setAttributeValue("nsubscripmny", value);
  }

  public String getVdef14()
  {
    return (String)getAttributeValue("vdef14");
  }

  public void setVdef14(String value)
  {
    setAttributeValue("vdef14", value);
  }

  public String getAccount()
  {
    return (String)getAttributeValue("account");
  }
  

  public void setAccount(String value)
  {
    setAttributeValue("account", value);
  }

  public Object getAttributeValue(String key)
  {
    Object oVal = super.getAttributeValue(key);
    if ((key.equals("cuserid")) && (oVal == null))
      oVal = super.getAttributeValue("coperator");
    else if ((key.equals("coperatoridnow")) && 
      (oVal == null)) {
      oVal = super.getAttributeValue("cuserid");
    }

    return oVal;
  }


  public void setAttributeValue(String name, Object value)
  {
    if ("nversion".equalsIgnoreCase(name))
    {
      Object oValue = value;
      if (value != null) {
        String sValue = value.toString();
        int iPointIndex = sValue.indexOf(".");
        if (iPointIndex > 0) {
          sValue = sValue.substring(0, iPointIndex);
          oValue = new Integer(sValue);
        }
      }
      value = PuPubVO.getInteger_NullAs(oValue, NVERSION_FIRST);
    }
    else if (("bislatest".equalsIgnoreCase(name)) && (value == null)) {
      value = VariableConst.UFBOOLEAN_TRUE;
    }
    else if (("bisreplenish".equalsIgnoreCase(name)) && (value == null)) {
      value = VariableConst.UFBOOLEAN_FALSE;
    }
    else if (("breturn".equalsIgnoreCase(name)) && (value == null)) {
      value = VariableConst.UFBOOLEAN_FALSE;
    }
    else if (("bdeliver".equalsIgnoreCase(name)) && (value == null)) {
      value = VariableConst.UFBOOLEAN_FALSE;
    }
    super.setAttributeValue(name, value);
  }

  public Integer getNversion()
  {
    Object value = getAttributeValue("nversion");
    Integer iRet = SmartVODataUtils.getInteger(value);
    if (iRet == null) {
      iRet = NVERSION_FIRST;
    }
    return iRet;
  }

  public void setNversion(Integer value)
  {
    setAttributeValue("nversion", value);
  }

  public void setStatus(Integer value)
  {
    setAttributeValue("status", value);
  }

  public String getVdef9()
  {
    return (String)getAttributeValue("vdef9");
  }

  public void setVdef9(String value)
  {
    setAttributeValue("vdef9", value);
  }

  public String getCdeptid()
  {
    return (String)getAttributeValue("cdeptid");
  }

  public void setCdeptid(String value)
  {
    setAttributeValue("cdeptid", value);
  }

  public UFBoolean getBislatest()
  {
    Object value = getAttributeValue("bislatest");
    UFBoolean bRet = SmartVODataUtils.getUFBoolean(value);
    if (bRet == null) {
      return BISLATEST_YES;
    }
    return bRet;
  }

  public void setBislatest(UFBoolean value)
  {
    setAttributeValue("bislatest", value);
  }

  public String getCoperator()
  {
    return (String)getAttributeValue("coperator");
  }

  public void setCoperator(String value)
  {
    setAttributeValue("coperator", value);
  }

  public String getVdef6()
  {
    return (String)getAttributeValue("vdef6");
  }

  public void setVdef6(String value)
  {
    setAttributeValue("vdef6", value);
  }

  public String getCvendorbaseid()
  {
    return (String)getAttributeValue("cvendorbaseid");
  }

  public void setCvendorbaseid(String value)
  {
    setAttributeValue("cvendorbaseid", value);
  }

  public String getVdef15()
  {
    return (String)getAttributeValue("vdef15");
  }

  public void setVdef15(String value)
  {
    setAttributeValue("vdef15", value);
  }

  public String getPk_defdoc14()
  {
    return (String)getAttributeValue("pk_defdoc14");
  }

  public void setPk_defdoc14(String value)
  {
    setAttributeValue("pk_defdoc14", value);
  }

  public String getCbiztypename()
  {
    return (String)getAttributeValue("cbiztypename");
  }

  public void setCbiztypename(String value)
  {
    setAttributeValue("cbiztypename", value);
  }

  public OrderHeaderVO(String newCorderid)
  {
    setCorderid(newCorderid);
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

  public String getCcontracttextpath()
  {
    return (String)getAttributeValue("ccontracttextpath");
  }

  public String getCD3Hid()
  {
    return (String)getAttributeValue("cd3hid");
  }

  public String getCD3Lrr()
  {
    return (String)getAttributeValue("cd3lrr");
  }

  public String getCtypecode()
  {
    return "21";
  }

  public String getCuserid()
  {
    return (String)getAttributeValue("cuserid");
  }

  public static final String[] getDbFields()
  {
    return new String[] { "corderid", "pk_corp", "vordercode", "dorderdate", "cpurorganization", "cfreecustid", "cvendormangid", "cvendorbaseid", "caccountbankid", "cdeptid", "cemployeeid", "cbiztype", "creciever", "cgiveinvoicevendor", "cdeliveraddress", "ctransmodeid", "ctermprotocolid", "cunfreeze", "caccountyear", "coperator", "forderstatus", "dauditdate", "cauditpsn", "vmemo", "vdef1", "vdef2", "vdef3", "vdef4", "vdef5", "vdef6", "vdef7", "vdef8", "vdef9", "vdef10", "vdef11", "vdef12", "vdef13", "vdef14", "vdef15", "vdef16", "vdef17", "vdef18", "vdef19", "vdef20", "pk_defdoc1", "pk_defdoc2", "pk_defdoc3", "pk_defdoc4", "pk_defdoc5", "pk_defdoc6", "pk_defdoc7", "pk_defdoc8", "pk_defdoc9", "pk_defdoc10", "pk_defdoc11", "pk_defdoc12", "pk_defdoc13", "pk_defdoc14", "pk_defdoc15", "pk_defdoc16", "pk_defdoc17", "pk_defdoc18", "pk_defdoc19", "pk_defdoc20", "ts", "dr", "ccontracttextpath", "nprepaymaxmny", "nprepaymny", "nversion", "bislatest", "drevisiondate", "bisreplenish", "breturn", "bdeliver", "iprintcount", "tmaketime", "taudittime", "tlastmaketime"};
  }

  public static final String[] getDbFieldsNoTs()
  {
    String[] saAll = getDbFields();
    String[] saRet = new String[saAll.length - 1];
    boolean bMetDR = false;
    int iPos = 0;
    for (int i = 0; i < saAll.length; i++) {
      iPos = i;
      if ("ts".equalsIgnoreCase(saAll[i])) {
        bMetDR = true;
      }
      else {
        if (bMetDR) {
          iPos--;
        }
        saRet[iPos] = saAll[i];
      }
    }
    return saRet;
  }

  public static final String getDbFieldsSelectString()
  {
    return PuPubVO.getSelectStringByFields("po_order", getDbFields());
  }

  public static final String[] getDbMnyFields_Local_Finance()
  {
    return new String[] { "nprepaymaxmny", "nprepaymny" };
  }

  public static final int getDbType(String sField)
  {
    if ((sField == null) || (sField.trim().length() == 0)) {
      return 0;
    }

    if ((sField.equals("corderid")) || (sField.equals("pk_corp")) || (sField.equals("vordercode")) || (sField.equals("dorderdate")) || (sField.equals("cpurorganization")) || (sField.equals("cfreecustid")) || (sField.equals("cvendormangid")) || (sField.equals("cvendorbaseid")) || (sField.equals("caccountbankid")) || (sField.equals("cdeptid")) || (sField.equals("cemployeeid")) || (sField.equals("cbiztype")) || (sField.equals("creciever")) || (sField.equals("cgiveinvoicevendor")) || (sField.equals("cdeliveraddress")) || (sField.equals("ctransmodeid")) || (sField.equals("ctermprotocolid")) || (sField.equals("cunfreeze")) || (sField.equals("caccountyear")) || (sField.equals("coperator")) || (sField.equals("dauditdate")) || (sField.equals("cauditpsn")) || (sField.equals("vmemo")) || (sField.equals("vdef1")) || (sField.equals("vdef2")) || (sField.equals("vdef3")) || (sField.equals("vdef4")) || (sField.equals("vdef5")) || (sField.equals("vdef6")) || (sField.equals("vdef7")) || (sField.equals("vdef8")) || (sField.equals("vdef9")) || (sField.equals("vdef10")) || (sField.equals("vdef11")) || (sField.equals("vdef12")) || (sField.equals("vdef13")) || (sField.equals("vdef14")) || (sField.equals("vdef15")) || (sField.equals("vdef16")) || (sField.equals("vdef17")) || (sField.equals("vdef18")) || (sField.equals("vdef19")) || (sField.equals("vdef20")) || (sField.equals("pk_defdoc1")) || (sField.equals("pk_defdoc2")) || (sField.equals("pk_defdoc3")) || (sField.equals("pk_defdoc4")) || (sField.equals("pk_defdoc5")) || (sField.equals("pk_defdoc6")) || (sField.equals("pk_defdoc7")) || (sField.equals("pk_defdoc8")) || (sField.equals("pk_defdoc9")) || (sField.equals("pk_defdoc10")) || (sField.equals("pk_defdoc11")) || (sField.equals("pk_defdoc12")) || (sField.equals("pk_defdoc13")) || (sField.equals("pk_defdoc14")) || (sField.equals("pk_defdoc15")) || (sField.equals("pk_defdoc16")) || (sField.equals("pk_defdoc17")) || (sField.equals("pk_defdoc18")) || (sField.equals("pk_defdoc19")) || (sField.equals("pk_defdoc20")) || (sField.equals("ts")) || (sField.equals("ccontracttextpath")) || (sField.equals("bislatest")) || (sField.equals("drevisiondate")) || (sField.equals("bisreplenish")) || (sField.equals("breturn")) || (sField.equals("bdeliver")) || (sField.equals("tmaketime")) || (sField.equals("taudittime")) || (sField.equals("tlastmaketime")))
    {
      return 1;
    }if ((sField.equals("nprepaymaxmny")) || (sField.equals("nprepaymny")))
    {
      return 3;
    }if ((sField.equals("forderstatus")) || (sField.equals("dr")) || (sField.equals("nversion")) || (sField.equals("iprintcount")))
    {
      return 4;
    }

    return 0;
  }

  public String getEntityName()
  {
    return "Order";
  }

  public FieldDBValidateVO[] getFieldDBValidateVOs()
  {
    return new FieldDBValidateVO[] { new FieldDBValidateVO(BillCodeField.class, new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003534") }, new String[] { getVordercode() }), new FieldDBValidateVO(DefField.class, new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000330"), NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000332"), NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000333"), NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000334"), NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000335"), NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000336"), NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000337"), NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000338"), NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000339"), NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000331") }, new String[] { getVdef1(), getVdef2(), getVdef3(), getVdef4(), getVdef5(), getVdef6(), getVdef7(), getVdef8(), getVdef9(), getVdef10() }), new FieldDBValidateVO(MemoField.class, new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001376") }, new String[] { getVmemo() }), new FieldDBValidateVO(MoneyField.class, new String[] { NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004187"), NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004189") }, new UFDouble[] { getNprepaymny(), getNprepaymaxmny() }) };
  }

  public String getPrimaryKey()
  {
    return getCorderid();
  }

  public int getStatus()
  {
    return 2;
  }

  public String getTs()
  {
    return (String)getAttributeValue("ts");
  }

  public String getVcode()
  {
    return getVordercode();
  }

  public boolean isDeliver()
  {
    if (getBdeliver() == null) {
      return false;
    }
    return getBdeliver().booleanValue();
  }

  public boolean isReturn()
  {
    if (getBreturn() == null) {
      return false;
    }
    return getBreturn().booleanValue();
  }

  public void setCcontracttextpath(String value)
  {
    setAttributeValue("ccontracttextpath", value);
  }

  public void setCD3Hid(String value)
  {
    setAttributeValue("cd3hid", value);
  }

  public void setCD3Lrr(String value)
  {
    setAttributeValue("cd3lrr", value);
  }

  public void setCuserid(String value)
  {
    setAttributeValue("cuserid", value);
  }

  public void setPrimaryKey(String newCorderid)
  {
    setCorderid(newCorderid);
  }

  public void setTs(String value)
  {
    setAttributeValue("ts", value);
  }

  public void setVcode(String value)
  {
    setVordercode(value);
  }

  public void validate()
    throws ValidationException
  {
    validateNull();
    validateDRevisionDate();
  }

  public void validateDRevisionDate()
    throws ValidationException
  {
    if ((PuPubVO.getUFDate(getDrevisiondate()) != null) && (getDauditdate().after(getDrevisiondate())))
    {
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000256"));
    }
  }

  public void validateNull()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    if (PuPubVO.getString_TrimZeroLenAsNull(getPk_corp()) == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000404")));
    }
    if ((getDorderdate() == null) || (getDorderdate().toString().trim().equals(""))) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003550")));
    }
    if ((getNversion() == null) || (getNversion().toString().trim().equals(""))) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002905")));
    }
    if ((getBislatest() == null) || (getBislatest().toString().trim().equals(""))) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002406")));
    }
    if (PuPubVO.getString_TrimZeroLenAsNull(getCbiztype()) == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000003")));
    }
    if (PuPubVO.getString_TrimZeroLenAsNull(getCpurorganization()) == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004091")));
    }
    if ((PuPubVO.getString_TrimZeroLenAsNull(getCvendormangid()) == null) || (PuPubVO.getString_TrimZeroLenAsNull(getCvendorbaseid()) == null))
    {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000275")));
    }
    if (PuPubVO.getString_TrimZeroLenAsNull(getCdeptid()) == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0004064")));
    }
    if (PuPubVO.getString_TrimZeroLenAsNull(getCemployeeid()) == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000298")));
    }
    if (PuPubVO.getString_TrimZeroLenAsNull(getCoperator()) == null) {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000661")));
    }

    if ((!PuPubVO.getInteger_NullAs(getNversion(), NVERSION_FIRST).equals(NVERSION_FIRST)) && (PuPubVO.getUFDate(getDrevisiondate()) == null))
    {
      errFields.add(new String(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000320")));
    }

    StringBuffer message = new StringBuffer();
    if (errFields.size() > 0)
    {
      String[] temp = (String[])(String[])errFields.toArray(new String[0]);
      String strTmp = " " + temp[0] + " ";
      for (int i = 1; i < temp.length; i++) {
        strTmp = strTmp + NCLangRes4VoTransl.getNCLangRes().getStrByID("SCMCOMMON", "UPPSCMCommon-000000");
        strTmp = strTmp + " " + temp[i] + " ";
      }
      message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("4004020201", "UPP4004020201-000098", null, new String[] { strTmp }));
      throw new NullFieldException(message.toString());
    }
  }

  public boolean refreshByHeaderVo(OrderHeaderVO lightVo)
  {
    if (lightVo == null) {
      return false;
    }

    setTs(lightVo.getTs());
    if (PuPubVO.getString_TrimZeroLenAsNull(lightVo.getCorderid()) != null) {
      setCorderid(lightVo.getCorderid());
    }
    if (PuPubVO.getString_TrimZeroLenAsNull(lightVo.getVordercode()) != null) {
      setVordercode(lightVo.getVordercode());
    }
    setForderstatus(lightVo.getForderstatus());
    setCauditpsn(lightVo.getCauditpsn());
    setDauditdate(lightVo.getDauditdate());
    setTaudittime(lightVo.getTaudittime());
    setTmaketime(lightVo.getTmaketime());
    setTlastmaketime(lightVo.getTlastmaketime());

    return true;
  }

  public String toString()
  {
    return ObjectAnalyzer.toString(this);
  }
}