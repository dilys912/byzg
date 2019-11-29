package nc.vo.ia.bill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.scm.bd.FreeItemVO;
import nc.vo.scm.bd.IInvVO;
import nc.vo.scm.bd.InvVO;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pub.smart.ISmartVOMeta;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.scm.pub.smart.SmartVO;

public class BillItemVO extends SmartVO
  implements IInvVO
{
  private static final long serialVersionUID = -8898787172958129398L;
  private InvVO m_voInv = new InvVO();

  FreeVO m_voFree = new FreeVO();

  public static String genColsSql(String prefix, HashMap colsExcluded, HashMap colsGenerated)
  {
    StringBuffer sql = new StringBuffer(1000);
    BillItemVO vo = new BillItemVO();
    if (prefix == null) {
      prefix = vo.getVOMeta().getTable();
      prefix = prefix + ".";
    } else if (prefix.length() != 0) {
      prefix = prefix + ".";
    }
    boolean returnCols = false;
    if ((colsGenerated != null) && (colsGenerated.size() == 0)) {
      returnCols = true;
    }

    String[] itemNames = vo.getMetaFieldNames();
    int index = 0;
    for (int i = 0; i < itemNames.length; i++)
    {
      if (((colsExcluded != null) && (colsExcluded.containsKey(itemNames[i]))) || (itemNames[i].equals("dr")))
      {
        continue;
      }
      Map map = vo.getVOMeta().getColumnsIndexByName();

      SmartFieldMeta sfm = (SmartFieldMeta)map.get(itemNames[i]);
      if (!sfm.isPersistence())
        continue;
      sql.append(" " + prefix + itemNames[i] + ",");
      if (returnCols) {
        colsGenerated.put(new Integer(index++), itemNames[i]);
      }
    }

    sql.append(" " + prefix + "ts ");
    if (returnCols) {
      colsGenerated.put(new Integer(index), "ts");
    }
    return sql.toString();
  }

  public static String genColsValueSql(BillItemVO bvo, Boolean isAnd, String prefix)
  {
    if (bvo == null) {
      return "";
    }
    String sAndOr = isAnd.booleanValue() ? " and " : " or  ";
    if (prefix == null) {
      prefix = bvo.getVOMeta().getTable();
      prefix = prefix + ".";
    } else if (prefix.length() != 0) {
      prefix = prefix + ".";
    }
    StringBuffer sql = new StringBuffer(1000);

    String[] colNames = bvo.getAttributeNames();
    for (int i = 0; i < colNames.length; i++) {
      if ((colNames[i].equals("nnumber")) && 
        (bvo.getNnumber() != null)) {
        if (bvo.getNnumber().doubleValue() == -99999.0D)
          sql.append(prefix + "nnumber<0 " + sAndOr);
        else {
          sql.append(prefix + "nnumber= " + bvo.getNnumber() + sAndOr);
        }

      }

      if (colNames[i].equals("csqlclause")) {
        if ((bvo.getCSQLClause() != null) && (bvo.getCSQLClause().length() != 0))
          sql.append(bvo.getCSQLClause() + sAndOr);
      }
      else {
        Object oTemp = bvo.getAttributeValue(colNames[i]);
        Map map = bvo.getVOMeta().getColumnsIndexByName();

        SmartFieldMeta sfm = (SmartFieldMeta)map.get(colNames[i]);
        if ((oTemp != null) && (sfm.isPersistence())) {
          if ((sfm.getType() == 2) || (sfm.getType() == 1))
          {
            sql.append(prefix + colNames[i] + " = " + oTemp.toString() + sAndOr);
          }
          else
          {
            sql.append(prefix + colNames[i] + " = '" + oTemp.toString() + "'" + sAndOr);
          }
        }
      }
    }

    return sql.substring(0, sql.length() > 0 ? sql.length() - 4 : 0);
  }

  public boolean equals(Object other)
  {
    if (other == null) {
      return false;
    }
    boolean result = false;

    String[] sleft = getMapNames();
    String[] sright = ((BillItemVO)other).getMapNames();

    Arrays.sort(sleft);
    Arrays.sort(sright);

    if ((sleft != null) && (sright != null) && (sleft.length == sright.length)) {
      boolean temp = true;
      for (int i = 0; i < sleft.length; i++) {
        Object oleft = getAttributeValue(sleft[i]);
        Object oright = ((BillItemVO)other).getAttributeValue(sright[i]);
        if ((oleft != null) && (oright != null) && (!oleft.toString().equals(oright.toString())))
        {
          temp = false;
          break;
        }if (((oleft != null) || (oright == null)) && ((oleft == null) || (oright != null)))
          continue;
        temp = false;
        break;
      }

      if (temp == true)
        result = true;
    }
    else if ((sleft == null) && (sright == null)) {
      result = true;
    }
    else {
      result = false;
    }
    return result;
  }

  public String[] getAttributeNames()
  {
    String[] ret = null;
    String[] ret1 = super.getAttributeNames();
    String[] ret2 = this.m_voInv.getAttributeNames();
    ret = new String[ret1.length + ret2.length];
    System.arraycopy(ret1, 0, ret, 0, ret1.length);
    System.arraycopy(ret2, 0, ret, ret1.length, ret2.length);
    return ret;
  }

  public Class getAttributeType(String sKey)
  {
    SmartFieldMeta sfm = (SmartFieldMeta)getVOMeta().getColumnsIndexByName().get(sKey);

    int iType = sfm.getType();
    Class result = null;
    switch (iType) {
    case 2:
      result = Integer.class;
      break;
    case 3:
      result = String.class;
      break;
    case 0:
      result = UFBoolean.class;
      break;
    case 4:
      result = UFDate.class;
      break;
    case 5:
      result = UFDateTime.class;
      break;
    case 1:
      result = UFDouble.class;
      break;
    case 6:
      result = UFTime.class;
      break;
    }

    return result;
  }

  public Object getAttributeValue(String sKey)
  {
    if (sKey.startsWith("bdef")) {
      sKey = "v" + sKey.substring(1);
    }

    if (getVOMeta().getColumnsIndexByName().containsKey(sKey)) {
      return super.getAttributeValue(sKey);
    }

    return this.m_voInv.getAttributeValue(sKey);
  }

  public UFBoolean getBadjustedItemflag()
  {
    Object value = getAttributeValue("badjusteditemflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBauditbatchflag()
  {
    Object value = getAttributeValue("bauditbatchflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBlargessflag()
  {
    Object value = getAttributeValue("blargessflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBretractflag()
  {
    Object value = getAttributeValue("bretractflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBrtvouchflag()
  {
    Object value = getAttributeValue("brtvouchflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBtransferincometax()
  {
    Object value = getAttributeValue("btransferincometax");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public String getCadjustbillid()
  {
    return (String)getAttributeValue("cadjustbillid");
  }

  public String getCadjustbillitemid()
  {
    return (String)getAttributeValue("cadjustbillitemid");
  }

  public String getCastunitid()
  {
    return (String)getAttributeValue("castunitid");
  }

  public String getCastunitname()
  {
    return (String)getAttributeValue("castunitname");
  }

  public String getCauditorid()
  {
    return (String)getAttributeValue("cauditorid");
  }

  public String getCauditorname()
  {
    return (String)getAttributeValue("cauditorname");
  }

  public String getCbill_bid()
  {
    return (String)getAttributeValue("cbill_bid");
  }

  public String getCbillid()
  {
    return (String)getAttributeValue("cbillid");
  }

  public String getCbilltypecode()
  {
    return (String)getAttributeValue("cbilltypecode");
  }

  public String getVbomcodecode()
  {
    return (String)getAttributeValue("vbomcodecode");
  }

  public String getVbomcodename()
  {
    return (String)getAttributeValue("vbomcodename");
  }

  public String getCcrspbillitemid()
  {
    return (String)getAttributeValue("ccrspbillitemid");
  }

  public String getCcsaleadviceitemid()
  {
    return (String)getAttributeValue("ccsaleadviceitemid");
  }

  public String getCfacardid()
  {
    return (String)getAttributeValue("cfacardid");
  }

  public String getCfadevicecode()
  {
    return (String)getAttributeValue("cfadevicecode");
  }

  public String getCfadeviceid()
  {
    return (String)getAttributeValue("cfadeviceid");
  }

  public String getCfadevicename()
  {
    return (String)getAttributeValue("cfadevicename");
  }

  public String getCfirstbillid()
  {
    return (String)getAttributeValue("cfirstbillid");
  }

  public String getCfirstbillitemid()
  {
    return (String)getAttributeValue("cfirstbillitemid");
  }

  public String getCfirstbilltypecode()
  {
    return (String)getAttributeValue("cfirstbilltypecode");
  }

  public String getCicbilltype()
  {
    return (String)getAttributeValue("cicbilltype");
  }

  public String getCicbillcode()
  {
    return (String)getAttributeValue("cicbillcode");
  }

  public String getCicbillid()
  {
    return (String)getAttributeValue("cicbillid");
  }

  public String getCicitemid()
  {
    return (String)getAttributeValue("cicitemid");
  }

  public String getCinbillitemid()
  {
    return (String)getAttributeValue("cinbillitemid");
  }

  public String getCinvclid()
  {
    return (String)getAttributeValue("cinvclid");
  }

  public String getCinventorycode()
  {
    return (String)getAttributeValue("cinventorycode");
  }

  public String getCinventoryid()
  {
    return (String)getAttributeValue("cinventoryid");
  }

  public String getCinventorymeasname()
  {
    return (String)getAttributeValue("cinventorymeasname");
  }

  public String getCinventoryname()
  {
    return (String)getAttributeValue("cinventoryname");
  }

  public String getCinventoryspec()
  {
    return (String)getAttributeValue("cinventoryspec");
  }

  public String getCinventorytype()
  {
    return (String)getAttributeValue("cinventorytype");
  }

  public String getCinvkind()
  {
    return (String)getAttributeValue("cinvkind");
  }

  public String getCprojectcode()
  {
    return (String)getAttributeValue("cprojectcode");
  }

  public String getCprojectid()
  {
    return (String)getAttributeValue("cprojectid");
  }

  public String getCprojectname()
  {
    return (String)getAttributeValue("cprojectname");
  }

  public String getCprojectphase()
  {
    return (String)getAttributeValue("cprojectphase");
  }

  public String getCprojectphasecode()
  {
    return (String)getAttributeValue("cprojectphasecode");
  }

  public String getCprojectphasename()
  {
    return (String)getAttributeValue("cprojectphasename");
  }

  public String getCsaleadviceid()
  {
    return (String)getAttributeValue("csaleadviceid");
  }

  public String getCsaleaudititemid()
  {
    return (String)getAttributeValue("csaleaudititemid");
  }

  public String getCsourcebillid()
  {
    return (String)getAttributeValue("csourcebillid");
  }

  public String getCsourcebillitemid()
  {
    return (String)getAttributeValue("csourcebillitemid");
  }

  public String getCsourcebilltypecode()
  {
    return (String)getAttributeValue("csourcebilltypecode");
  }

  public String getCSQLClause()
  {
    return (String)getAttributeValue("csqlclause");
  }

  public String getCsumrtvouchid()
  {
    return (String)getAttributeValue("csumrtvouchid");
  }

  public String getCvendorid()
  {
    return (String)getAttributeValue("cvendorid");
  }

  public String getCvoucherid()
  {
    return (String)getAttributeValue("cvoucherid");
  }

  public String getCwp()
  {
    return (String)getAttributeValue("cwp");
  }

  public String getCwpcode()
  {
    return (String)getAttributeValue("cwpcode");
  }

  public String getCwpname()
  {
    return (String)getAttributeValue("cwpname");
  }

  public UFDate getDauditdate()
  {
    Object value = getAttributeValue("dauditdate");
    return SmartVODataUtils.getUFDate(value);
  }

  public UFDate getDbizdate()
  {
    Object value = getAttributeValue("dbizdate");
    return SmartVODataUtils.getUFDate(value);
  }

  public UFDouble getDdrawrate()
  {
    Object value = getAttributeValue("ddrawrate");
    return SmartVODataUtils.getUFDouble(value);
  }

  public Integer getDr()
  {
    Object value = getAttributeValue("dr");
    return SmartVODataUtils.getInteger(value);
  }

  public Integer getFdatagetmodelflag()
  {
    Object value = getAttributeValue("fdatagetmodelflag");
    return SmartVODataUtils.getInteger(value);
  }

  public Integer getFolddatagetmodelflag()
  {
    Object value = getAttributeValue("folddatagetmodelflag");
    return SmartVODataUtils.getInteger(value);
  }

  public UFBoolean getFoutadjustableflag()
  {
    Object value = getAttributeValue("foutadjustableflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public Integer getFpricemodeflag()
  {
    Object value = getAttributeValue("fpricemodeflag");
    return SmartVODataUtils.getInteger(value);
  }

  public Integer getIauditsequence()
  {
    Object value = getAttributeValue("iauditsequence");
    return SmartVODataUtils.getInteger(value);
  }

  public InvVO getInvVO()
  {
    this.m_voInv.setCinventoryid(getCinventoryid());

    for (int i = 0; i < 5; i++) {
      this.m_voInv.setAttributeValue("vfree" + i, getAttributeValue("vfree" + i));
    }

    return this.m_voInv;
  }

  public Integer getIrownumber()
  {
    Object value = getAttributeValue("irownumber");
    return SmartVODataUtils.getInteger(value);
  }

  public UFDouble getNassistnum()
  {
    Object value = getAttributeValue("nassistnum");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNadjustnum()
  {
    Object value = getAttributeValue("nadjustnum");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNchangerate()
  {
    Object value = getAttributeValue("nchangerate");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNdrawsummny()
  {
    Object value = getAttributeValue("ndrawsummny");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNexpaybacktax()
  {
    Object value = getAttributeValue("nexpaybacktax");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNincometax()
  {
    Object value = getAttributeValue("nincometax");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNinvarymny()
  {
    Object value = getAttributeValue("ninvarymny");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNmoney()
  {
    Object value = getAttributeValue("nmoney");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNnumber()
  {
    Object value = getAttributeValue("nnumber");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNoriginalprice()
  {
    Object value = getAttributeValue("noriginalprice");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNoutvarymny()
  {
    Object value = getAttributeValue("noutvarymny");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNplanedmny()
  {
    Object value = getAttributeValue("nplanedmny");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNplanedprice()
  {
    Object value = getAttributeValue("nplanedprice");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNprice()
  {
    Object value = getAttributeValue("nprice");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNsettledretractnum()
  {
    Object value = getAttributeValue("nsettledretractnum");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNsettledsendnum()
  {
    Object value = getAttributeValue("nsettledsendnum");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNsimulatemny()
  {
    Object value = getAttributeValue("nsimulatemny");
    return SmartVODataUtils.getUFDouble(value);
  }

  public String getPk_corp()
  {
    return (String)getAttributeValue("pk_corp");
  }

  public String getPk_defdoc1()
  {
    return (String)getAttributeValue("pk_defdoc1");
  }

  public String getPk_defdoc10()
  {
    return (String)getAttributeValue("pk_defdoc10");
  }

  public String getPk_defdoc11()
  {
    return (String)getAttributeValue("pk_defdoc11");
  }

  public String getPk_defdoc12()
  {
    return (String)getAttributeValue("pk_defdoc12");
  }

  public String getPk_defdoc13()
  {
    return (String)getAttributeValue("pk_defdoc13");
  }

  public String getPk_defdoc14()
  {
    return (String)getAttributeValue("pk_defdoc14");
  }

  public String getPk_defdoc15()
  {
    return (String)getAttributeValue("pk_defdoc15");
  }

  public String getPk_defdoc16()
  {
    return (String)getAttributeValue("pk_defdoc16");
  }

  public String getPk_defdoc17()
  {
    return (String)getAttributeValue("pk_defdoc17");
  }

  public String getPk_defdoc18()
  {
    return (String)getAttributeValue("pk_defdoc18");
  }

  public String getPk_defdoc19()
  {
    return (String)getAttributeValue("pk_defdoc19");
  }

  public String getPk_defdoc2()
  {
    return (String)getAttributeValue("pk_defdoc2");
  }

  public String getPk_defdoc20()
  {
    return (String)getAttributeValue("pk_defdoc20");
  }

  public String getPk_defdoc3()
  {
    return (String)getAttributeValue("pk_defdoc3");
  }

  public String getPk_defdoc4()
  {
    return (String)getAttributeValue("pk_defdoc4");
  }

  public String getPk_defdoc5()
  {
    return (String)getAttributeValue("pk_defdoc5");
  }

  public String getPk_defdoc6()
  {
    return (String)getAttributeValue("pk_defdoc6");
  }

  public String getPk_defdoc7()
  {
    return (String)getAttributeValue("pk_defdoc7");
  }

  public String getPk_defdoc8()
  {
    return (String)getAttributeValue("pk_defdoc8");
  }

  public String getPk_defdoc9()
  {
    return (String)getAttributeValue("pk_defdoc9");
  }

  public String getTs()
  {
    return (String)getAttributeValue("ts");
  }

  public String getVastunitname()
  {
    return (String)getAttributeValue("vastunitname");
  }

  public String getVauditorname()
  {
    return (String)getAttributeValue("vauditorname");
  }

  public String getVbatch()
  {
    return (String)getAttributeValue("vbatch");
  }

  public String getVbillcode()
  {
    return (String)getAttributeValue("vbillcode");
  }

  public String getVbomcode()
  {
    return (String)getAttributeValue("vbomcode");
  }

  public String getVdef1()
  {
    return (String)getAttributeValue("vdef1");
  }

  public String getVdef10()
  {
    return (String)getAttributeValue("vdef10");
  }

  public String getVdef11()
  {
    return (String)getAttributeValue("vdef11");
  }

  public String getVdef12()
  {
    return (String)getAttributeValue("vdef12");
  }

  public String getVdef13()
  {
    return (String)getAttributeValue("vdef13");
  }

  public String getVdef14()
  {
    return (String)getAttributeValue("vdef14");
  }

  public String getVdef15()
  {
    return (String)getAttributeValue("vdef15");
  }

  public String getVdef16()
  {
    return (String)getAttributeValue("vdef16");
  }

  public String getVdef17()
  {
    return (String)getAttributeValue("vdef17");
  }

  public String getVdef18()
  {
    return (String)getAttributeValue("vdef18");
  }

  public String getVdef19()
  {
    return (String)getAttributeValue("vdef19");
  }

  public String getVdef2()
  {
    return (String)getAttributeValue("vdef2");
  }

  public String getVdef20()
  {
    return (String)getAttributeValue("vdef20");
  }

  public String getVdef3()
  {
    return (String)getAttributeValue("vdef3");
  }

  public String getVdef4()
  {
    return (String)getAttributeValue("vdef4");
  }

  public String getVdef5()
  {
    return (String)getAttributeValue("vdef5");
  }

  public String getVdef6()
  {
    return (String)getAttributeValue("vdef6");
  }

  public String getVdef7()
  {
    return (String)getAttributeValue("vdef7");
  }

  public String getVdef8()
  {
    return (String)getAttributeValue("vdef8");
  }

  public String getVdef9()
  {
    return (String)getAttributeValue("vdef9");
  }

  public String getVfirstbillcode()
  {
    return (String)getAttributeValue("vfirstbillcode");
  }

  public String getVfirstrowno()
  {
    return (String)getAttributeValue("vfirstrowno");
  }

  public String getVfree0()
  {
    return (String)getAttributeValue("vfree0");
  }

  public String getVfree1()
  {
    return (String)getAttributeValue("vfree1");
  }

  public String getVfree2()
  {
    return (String)getAttributeValue("vfree2");
  }

  public String getVfree3()
  {
    return (String)getAttributeValue("vfree3");
  }

  public String getVfree4()
  {
    return (String)getAttributeValue("vfree4");
  }

  public String getVfree5()
  {
    return (String)getAttributeValue("vfree5");
  }

  public FreeVO getVOFree()
  {
    return this.m_voFree;
  }

  public Class getVOMetaClass()
  {
    return BillItemVOMeta.class;
  }

  public String getVproducebatch()
  {
    return (String)getAttributeValue("vproducebatch");
  }

  public String getVprojectname()
  {
    return (String)getAttributeValue("vprojectname");
  }

  public String getVsourcebillcode()
  {
    return (String)getAttributeValue("vsourcebillcode");
  }

  public String getVsourcerowno()
  {
    return (String)getAttributeValue("vsourcerowno");
  }

  public String getVvendorname()
  {
    return (String)getAttributeValue("vvendorname");
  }

  public void setAttributeValue(String sKey, Object oValue)
  {
    if (getVOMeta().getColumnsIndexByName().containsKey(sKey))
    {
      if (sKey.equals("blargessflag")) {
        UFBoolean blargessflag = new UFBoolean("N");
        if ((oValue != null) && ((oValue.toString().equals(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPPSCMCommon-000244"))) || (oValue.toString().equals("Y"))))
        {
          blargessflag = new UFBoolean("Y");
        }
        super.setAttributeValue(sKey, blargessflag);
      } else if (sKey.equals(getVOMeta().getPkColName()))
      {
        if ((getCsourcebilltypecode() != null) && (getCsourcebilltypecode().equals("4C")) && (getCcsaleadviceitemid() == null))
        {
          setCsaleadviceid(getCbillid());
          if (oValue != null) {
            setCcsaleadviceitemid(oValue.toString());
          }
        }
      }
      super.setAttributeValue(sKey, oValue);
    }
    else {
      this.m_voInv.setAttributeValue(sKey, oValue);
    }
  }

  public void setBadjustedItemflag(UFBoolean value)
  {
    setAttributeValue("badjusteditemflag", value);
  }

  public void setBauditbatchflag(UFBoolean value)
  {
    setAttributeValue("bauditbatchflag", value);
  }

  public void setBlargessflag(UFBoolean value)
  {
    setAttributeValue("blargessflag", value);
  }

  public void setBretractflag(UFBoolean value)
  {
    setAttributeValue("bretractflag", value);
  }

  public void setBrtvouchflag(UFBoolean value)
  {
    setAttributeValue("brtvouchflag", value);
  }

  public void setBtransferincometax(UFBoolean value)
  {
    setAttributeValue("btransferincometax", value);
  }

  public void setCadjustbillid(String value)
  {
    setAttributeValue("cadjustbillid", value);
  }

  public void setCadjustbillitemid(String value)
  {
    setAttributeValue("cadjustbillitemid", value);
  }

  public void setCastunitid(String value)
  {
    setAttributeValue("castunitid", value);
  }

  public void setCastunitname(String value)
  {
    setAttributeValue("castunitname", value);
  }

  public void setCauditorid(String value)
  {
    setAttributeValue("cauditorid", value);
  }

  public void setCauditorname(String value)
  {
    setAttributeValue("cauditorname", value);
  }

  public void setCbill_bid(String value)
  {
    setAttributeValue("cbill_bid", value);
  }

  public void setCbillid(String value)
  {
    setAttributeValue("cbillid", value);
  }

  public void setCbilltypecode(String value)
  {
    setAttributeValue("cbilltypecode", value);
  }

  public void setVbomcodecode(String value)
  {
    setAttributeValue("vbomcodecode", value);
  }

  public void setVbomcodename(String value)
  {
    setAttributeValue("vbomcodename", value);
  }

  public void setCcrspbillitemid(String value)
  {
    setAttributeValue("ccrspbillitemid", value);
  }

  public void setCcsaleadviceitemid(String value)
  {
    setAttributeValue("ccsaleadviceitemid", value);
  }

  public void setCfacardid(String value)
  {
    setAttributeValue("cfacardid", value);
  }

  public void setCfadevicecode(String value)
  {
    setAttributeValue("cfadevicecode", value);
  }

  public void setCfadeviceid(String value)
  {
    setAttributeValue("cfadeviceid", value);
  }

  public void setCfadevicename(String value)
  {
    setAttributeValue("cfadevicename", value);
  }

  public void setCfirstbillid(String value)
  {
    setAttributeValue("cfirstbillid", value);
  }

  public void setCfirstbillitemid(String value)
  {
    setAttributeValue("cfirstbillitemid", value);
  }

  public void setCfirstbilltypecode(String value)
  {
    setAttributeValue("cfirstbilltypecode", value);
  }

  public void setCicbilltype(String value)
  {
    setAttributeValue("cicbilltype", value);
  }

  public void setCicbillcode(String value)
  {
    setAttributeValue("cicbillcode", value);
  }

  public void setCicbillid(String value)
  {
    setAttributeValue("cicbillid", value);
  }

  public void setCicitemid(String value)
  {
    setAttributeValue("cicitemid", value);
  }

  public void setCinbillitemid(String value)
  {
    setAttributeValue("cinbillitemid", value);
  }

  public void setCinvclid(String value)
  {
    setAttributeValue("cinvclid", value);
  }

  public void setCinventorycode(String value)
  {
    setAttributeValue("cinventorycode", value);
  }

  public void setCinventoryid(String value)
  {
    setAttributeValue("cinventoryid", value);
  }

  public void setCinventorymeasname(String value)
  {
    setAttributeValue("cinventorymeasname", value);
  }

  public void setCinventoryname(String value)
  {
    setAttributeValue("cinventoryname", value);
  }

  public void setCinventoryspec(String value)
  {
    setAttributeValue("cinventoryspec", value);
  }

  public void setCinventorytype(String value)
  {
    setAttributeValue("cinventorytype", value);
  }

  public void setCinvkind(String value)
  {
    setAttributeValue("cinvkind", value);
  }

  public void setCprojectcode(String value)
  {
    setAttributeValue("cprojectcode", value);
  }

  public void setCprojectid(String value)
  {
    setAttributeValue("cprojectid", value);
  }

  public void setCprojectname(String value)
  {
    setAttributeValue("cprojectname", value);
  }

  public void setCprojectphase(String value)
  {
    setAttributeValue("cprojectphase", value);
  }

  public void setCprojectphasecode(String value)
  {
    setAttributeValue("cprojectphasecode", value);
  }

  public void setCprojectphasename(String value)
  {
    setAttributeValue("cprojectphasename", value);
  }

  public void setCsaleadviceid(String value)
  {
    setAttributeValue("csaleadviceid", value);
  }

  public void setCsaleaudititemid(String value)
  {
    setAttributeValue("csaleaudititemid", value);
  }

  public void setCsourcebillid(String value)
  {
    setAttributeValue("csourcebillid", value);
  }

  public void setCsourcebillitemid(String value)
  {
    setAttributeValue("csourcebillitemid", value);
  }

  public void setCsourcebilltypecode(String value)
  {
    setAttributeValue("csourcebilltypecode", value);
  }

  public void setCSQLClause(String value)
  {
    setAttributeValue("csqlclause", value);
  }

  public void setCsumrtvouchid(String value)
  {
    setAttributeValue("csumrtvouchid", value);
  }

  public void setCvendorid(String value)
  {
    setAttributeValue("cvendorid", value);
  }

  public void setCvoucherid(String value)
  {
    setAttributeValue("cvoucherid", value);
  }

  public void setCwp(String value)
  {
    setAttributeValue("cwp", value);
  }

  public void setCwpcode(String value)
  {
    setAttributeValue("cwpcode", value);
  }

  public void setCwpname(String value)
  {
    setAttributeValue("cwpname", value);
  }

  public void setDauditdate(UFDate value)
  {
    setAttributeValue("dauditdate", value);
  }

  public void setDbizdate(UFDate value)
  {
    setAttributeValue("dbizdate", value);
  }

  public void setDdrawrate(UFDouble value)
  {
    setAttributeValue("ddrawrate", value);
  }

  public void setDr(Integer value)
  {
    setAttributeValue("dr", value);
  }

  public void setFdatagetmodelflag(Integer value)
  {
    setAttributeValue("fdatagetmodelflag", value);
  }

  public void setFolddatagetmodelflag(Integer value)
  {
    setAttributeValue("folddatagetmodelflag", value);
  }

  public void setFoutadjustableflag(UFBoolean value)
  {
    setAttributeValue("foutadjustableflag", value);
  }

  public void setFpricemodeflag(Integer value)
  {
    setAttributeValue("fpricemodeflag", value);
  }

  public void setIauditsequence(Integer value)
  {
    setAttributeValue("iauditsequence", value);
  }

  public void setInvVO(InvVO voInv)
  {
    this.m_voInv = voInv;
    if (voInv != null) {
      setCinventoryid(this.m_voInv.getCinventoryid());

      for (int i = 0; i < 5; i++)
        setAttributeValue("vfree" + i, this.m_voInv.getAttributeValue("vfree" + i));
    }
  }

  public void setIrownumber(Integer value)
  {
    setAttributeValue("irownumber", value);
  }

  public void setNassistnum(UFDouble value)
  {
    setAttributeValue("nassistnum", value);
  }

  public void setNadjustnum(UFDouble value)
  {
    setAttributeValue("nadjustnum", value);
  }

  public void setNchangerate(UFDouble value)
  {
    setAttributeValue("nchangerate", value);
  }

  public void setNdrawsummny(UFDouble value)
  {
    setAttributeValue("ndrawsummny", value);
  }

  public void setNexpaybacktax(UFDouble value)
  {
    setAttributeValue("nexpaybacktax", value);
  }

  public void setNincometax(UFDouble value)
  {
    setAttributeValue("nincometax", value);
  }

  public void setNinvarymny(UFDouble value)
  {
    setAttributeValue("ninvarymny", value);
  }

  public void setNmoney(UFDouble value)
  {
    setAttributeValue("nmoney", value);
  }

  public void setNnumber(UFDouble value)
  {
    setAttributeValue("nnumber", value);
  }

  public void setNoriginalprice(UFDouble value)
  {
    setAttributeValue("noriginalprice", value);
  }

  public void setNoutvarymny(UFDouble value)
  {
    setAttributeValue("noutvarymny", value);
  }

  public void setNplanedmny(UFDouble value)
  {
    setAttributeValue("nplanedmny", value);
  }

  public void setNplanedprice(UFDouble value)
  {
    setAttributeValue("nplanedprice", value);
  }

  public void setNprice(UFDouble value)
  {
    setAttributeValue("nprice", value);
  }

  public void setNsettledretractnum(UFDouble value)
  {
    setAttributeValue("nsettledretractnum", value);
  }

  public void setNsettledsendnum(UFDouble value)
  {
    setAttributeValue("nsettledsendnum", value);
  }

  public void setNsimulatemny(UFDouble value)
  {
    setAttributeValue("nsimulatemny", value);
  }

  public void setPk_corp(String value)
  {
    setAttributeValue("pk_corp", value);
  }

  public void setPk_defdoc1(String value)
  {
    setAttributeValue("pk_defdoc1", value);
  }

  public void setPk_defdoc10(String value)
  {
    setAttributeValue("pk_defdoc10", value);
  }

  public void setPk_defdoc11(String value)
  {
    setAttributeValue("pk_defdoc11", value);
  }

  public void setPk_defdoc12(String value)
  {
    setAttributeValue("pk_defdoc12", value);
  }

  public void setPk_defdoc13(String value)
  {
    setAttributeValue("pk_defdoc13", value);
  }

  public void setPk_defdoc14(String value)
  {
    setAttributeValue("pk_defdoc14", value);
  }

  public void setPk_defdoc15(String value)
  {
    setAttributeValue("pk_defdoc15", value);
  }

  public void setPk_defdoc16(String value)
  {
    setAttributeValue("pk_defdoc16", value);
  }

  public void setPk_defdoc17(String value)
  {
    setAttributeValue("pk_defdoc17", value);
  }

  public void setPk_defdoc18(String value)
  {
    setAttributeValue("pk_defdoc18", value);
  }

  public void setPk_defdoc19(String value)
  {
    setAttributeValue("pk_defdoc19", value);
  }

  public void setPk_defdoc2(String value)
  {
    setAttributeValue("pk_defdoc2", value);
  }

  public void setPk_defdoc20(String value)
  {
    setAttributeValue("pk_defdoc20", value);
  }

  public void setPk_defdoc3(String value)
  {
    setAttributeValue("pk_defdoc3", value);
  }

  public void setPk_defdoc4(String value)
  {
    setAttributeValue("pk_defdoc4", value);
  }

  public void setPk_defdoc5(String value)
  {
    setAttributeValue("pk_defdoc5", value);
  }

  public void setPk_defdoc6(String value)
  {
    setAttributeValue("pk_defdoc6", value);
  }

  public void setPk_defdoc7(String value)
  {
    setAttributeValue("pk_defdoc7", value);
  }

  public void setPk_defdoc8(String value)
  {
    setAttributeValue("pk_defdoc8", value);
  }

  public void setPk_defdoc9(String value)
  {
    setAttributeValue("pk_defdoc9", value);
  }

  public void setTs(String value)
  {
    setAttributeValue("ts", value);
  }

  public void setVastunitname(String value)
  {
    setAttributeValue("vastunitname", value);
  }

  public void setVauditorname(String value)
  {
    setAttributeValue("vauditorname", value);
  }

  public void setVbatch(String value)
  {
    setAttributeValue("vbatch", value);
  }

  public void setVbillcode(String value)
  {
    setAttributeValue("vbillcode", value);
  }

  public void setVbomcode(String value)
  {
    setAttributeValue("vbomcode", value);
  }

  public void setVdef1(String value)
  {
    setAttributeValue("vdef1", value);
  }

  public void setVdef10(String value)
  {
    setAttributeValue("vdef10", value);
  }

  public void setVdef11(String value)
  {
    setAttributeValue("vdef11", value);
  }

  public void setVdef12(String value)
  {
    setAttributeValue("vdef12", value);
  }

  public void setVdef13(String value)
  {
    setAttributeValue("vdef13", value);
  }

  public void setVdef14(String value)
  {
    setAttributeValue("vdef14", value);
  }

  public void setVdef15(String value)
  {
    setAttributeValue("vdef15", value);
  }

  public void setVdef16(String value)
  {
    setAttributeValue("vdef16", value);
  }

  public void setVdef17(String value)
  {
    setAttributeValue("vdef17", value);
  }

  public void setVdef18(String value)
  {
    setAttributeValue("vdef18", value);
  }

  public void setVdef19(String value)
  {
    setAttributeValue("vdef19", value);
  }

  public void setVdef2(String value)
  {
    setAttributeValue("vdef2", value);
  }

  public void setVdef20(String value)
  {
    setAttributeValue("vdef20", value);
  }

  public void setVdef3(String value)
  {
    setAttributeValue("vdef3", value);
  }

  public void setVdef4(String value)
  {
    setAttributeValue("vdef4", value);
  }

  public void setVdef5(String value)
  {
    setAttributeValue("vdef5", value);
  }

  public void setVdef6(String value)
  {
    setAttributeValue("vdef6", value);
  }

  public void setVdef7(String value)
  {
    setAttributeValue("vdef7", value);
  }

  public void setVdef8(String value)
  {
    setAttributeValue("vdef8", value);
  }

  public void setVdef9(String value)
  {
    setAttributeValue("vdef9", value);
  }

  public void setVfirstbillcode(String value)
  {
    setAttributeValue("vfirstbillcode", value);
  }

  public void setVfirstrowno(String value)
  {
    setAttributeValue("vfirstrowno", value);
  }

  public void setVfree0(String value)
  {
    setAttributeValue("vfree0", value);
  }

  public void setVfree1(String value)
  {
    setAttributeValue("vfree1", value);
  }

  public void setVfree2(String value)
  {
    setAttributeValue("vfree2", value);
  }

  public void setVfree3(String value)
  {
    setAttributeValue("vfree3", value);
  }

  public void setVfree4(String value)
  {
    setAttributeValue("vfree4", value);
  }

  public void setVfree5(String value)
  {
    setAttributeValue("vfree5", value);
  }

  public void setVOFree(FreeVO value)
  {
    this.m_voFree = value;
  }

  public void setVproducebatch(String value)
  {
    setAttributeValue("vproducebatch", value);
  }

  public void setVprojectname(String value)
  {
    setAttributeValue("vprojectname", value);
  }

  public void setVsourcebillcode(String value)
  {
    setAttributeValue("vsourcebillcode", value);
  }

  public void setVsourcerowno(String value)
  {
    setAttributeValue("vsourcerowno", value);
  }

  public void setVvendorname(String value)
  {
    setAttributeValue("vvendorname", value);
  }

  public String getPk_reqcorp()
  {
    return (String)getAttributeValue("pk_reqcorp");
  }

  public String getPk_reqstoorg()
  {
    return (String)getAttributeValue("pk_reqstoorg");
  }

  public String getPk_creqwareid()
  {
    return (String)getAttributeValue("pk_creqwareid");
  }

  public String getPk_invoicecorp()
  {
    return (String)getAttributeValue("pk_invoicecorp");
  }

  public void setPk_reqcorp(String creqcorp)
  {
    setAttributeValue("pk_reqcorp", creqcorp);
  }

  public void setPk_reqstoorg(String creqstoorg)
  {
    setAttributeValue("pk_reqstoorg", creqstoorg);
  }

  public void setPk_creqwareid(String cwareid)
  {
    setAttributeValue("pk_creqwareid", cwareid);
  }

  public void setPk_invoicecorp(String invoicecorp)
  {
    setAttributeValue("pk_invoicecorp", invoicecorp);
  }

  public void setNreasonalwastnum(UFDouble nreasonalwastnum)
  {
    setAttributeValue("nreasonalwastnum", nreasonalwastnum);
  }

  public void setNreasonalwastprice(UFDouble nreasonalwastprice)
  {
    setAttributeValue("nreasonalwastprice", nreasonalwastprice);
  }

  public void setNreasonalwastmny(UFDouble nreasonalwastmny)
  {
    setAttributeValue("nreasonalwastmny", nreasonalwastmny);
  }

  public UFDouble getNreasonalwastnum()
  {
    Object value = getAttributeValue("nreasonalwastnum");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNreasonalwastprice()
  {
    Object value = getAttributeValue("nreasonalwastprice");
    return SmartVODataUtils.getUFDouble(value);
  }

  public UFDouble getNreasonalwastmny()
  {
    Object value = getAttributeValue("nreasonalwastmny");
    return SmartVODataUtils.getUFDouble(value);
  }

  public void verify()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    String sErrorString = NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000094");

    Vector vNumString = new Vector(1, 1);
    int iRow = -1;

    String sBillType = "";
    if (getIrownumber() == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003389"));
    }
    else {
      iRow = getIrownumber().intValue();
    }
    if (getPk_corp() == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000787"));
    }

    if (getCbilltypecode() == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000807"));
    }
    else {
      sBillType = getCbilltypecode();
    }
    if ((getFpricemodeflag() == null) && (!sBillType.equals("IF")) && (!sBillType.equals("IB")))
    {
      String[] value = { String.valueOf(iRow) };
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000265", null, value));
    }

    if (getCinventoryid() == null) {
      String[] value = { String.valueOf(iRow) };
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000266", null, value));
    }

    if ((getNnumber() == null) && (!sBillType.equals("I9")) && (!sBillType.equals("IA")) && (!sBillType.equals("IB")) && (!sBillType.equals("IG")))
    {
      String[] value = { String.valueOf(iRow) };
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000143", null, value));
    }
    else if (getNnumber() != null) {
      double dNumber = getNnumber().doubleValue();
      if ((dNumber < 0.0D) && (sBillType.equals("I1")))
      {
        vNumString.addElement(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000095"));
      }

    }

    if ((getNmoney() == null) && (((getFpricemodeflag().intValue() != 5) && (sBillType.equals("I0"))) || (sBillType.equals("I1"))))
    {
      String[] value = { String.valueOf(iRow) };
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000144", null, value));
    }
    else if ((getNmoney() == null) && ((sBillType.equals("I9")) || (sBillType.equals("IA"))))
    {
      String[] value = { String.valueOf(iRow) };
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000144", null, value));
    }
    else if (getNmoney() != null) {
      double dMoney = getNmoney().doubleValue();
      if ((dMoney < 0.0D) && (sBillType.equals("I1")))
      {
        vNumString.addElement(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000096"));
      }

    }

    if ((getNprice() == null) && (sBillType.equals("I1"))) {
      String[] value = { String.valueOf(iRow) };
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000145", null, value));
    }
    else if (getNprice() != null) {
      double dPrice = getNprice().doubleValue();
      if ((dPrice < 0.0D) && (!sBillType.equals("I0"))) {
        String[] value = { String.valueOf(iRow) };
        vNumString.addElement(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000146", null, value));
      }

    }

    StringBuffer message = new StringBuffer();
    int iLength = vNumString.size();
    for (int i = 0; i < iLength; i++) {
      if (i != 0) {
        message.append(", ");
      }
      message.append(vNumString.elementAt(i));
    }
    if (errFields.size() > 0) {
      if (vNumString.size() != 0) {
        message.append(", ");
      }
      message.append(sErrorString);
      String[] temp = (String[])(String[])errFields.toArray(new String[errFields.size()]);

      message.append(temp[0]);
      for (int i = 1; i < temp.length; i++) {
        message.append(", ");
        message.append(temp[i]);
      }

      throw new NullFieldException(message.toString());
    }if (message.length() != 0)
      throw new ValidationException(message.toString());
  }

  public BillItemVO()
  {
    this.m_voInv.setFreeItemVO(new FreeItemVO());
  }
}