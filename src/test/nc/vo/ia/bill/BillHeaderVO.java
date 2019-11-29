package nc.vo.ia.bill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.pub.smart.ISmartVOMeta;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.scm.pub.smart.SmartVO;

public class BillHeaderVO extends SmartVO
{
  private static final long serialVersionUID = -4422200536395569398L;
  private ComboItemsVO m_comboItemsVO = new ComboItemsVO();

  public static String genColsSql(String prefix, HashMap colsExcluded, HashMap colsGenerated)
  {
    StringBuffer sql = new StringBuffer(1000);
    BillHeaderVO vo = new BillHeaderVO();

    if (prefix == null) {
      prefix = vo.getVOMeta().getTable();
      prefix = prefix + ".";
    }
    else if (prefix.length() != 0) {
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

  public static String genColsValueSql(BillHeaderVO hvo, Boolean isAnd, String prefix)
  {
    if (hvo == null) {
      return "";
    }

    String sAndOr = isAnd.booleanValue() ? " and " : " or ";

    if (prefix == null) {
      prefix = hvo.getVOMeta().getTable();
      prefix = prefix + ".";
    }
    else if (prefix.length() != 0) {
      prefix = prefix + ".";
    }

    StringBuffer sql = new StringBuffer(1000);

    String[] colNames = hvo.getAttributeNames();
    for (int i = 0; i < colNames.length; i++)
    {
      if (colNames[i].equals("csqlclause")) {
        if ((hvo.getCSQLClause() != null) && (hvo.getCSQLClause().length() != 0))
          sql.append(hvo.getCSQLClause() + sAndOr);
      }
      else
      {
        Object oTemp = hvo.getAttributeValue(colNames[i]);
        Map map = hvo.getVOMeta().getColumnsIndexByName();

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
    String[] sright = ((BillHeaderVO)other).getMapNames();

    Arrays.sort(sleft);
    Arrays.sort(sright);

    if ((sleft != null) && (sright != null) && (sleft.length == sright.length)) {
      boolean temp = true;
      for (int i = 0; i < sleft.length; i++) {
        Object oleft = getAttributeValue(sleft[i]);
        Object oright = ((BillHeaderVO)other).getAttributeValue(sright[i]);
        if ((oleft != null) && (oright != null) && (!oleft.toString().equals(oright.toString())))
        {
          temp = false;
          break;
        }
        if (((oleft != null) || (oright == null)) && ((oleft == null) || (oright != null)))
          continue;
        temp = false;
        break;
      }

      if (temp == true) {
        result = true;
      }
    }
    else if ((sleft == null) && (sright == null)) {
      result = true;
    }
    else
    {
      result = false;
    }
    return result;
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

  public UFBoolean getBauditedflag()
  {
    Object value = getAttributeValue("bauditedflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBcalculatedinvcost()
  {
    Object value = getAttributeValue("bcalculatedinvcost");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBingathersettle()
  {
    Object value = getAttributeValue("bingathersettle");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBoutgathersettle()
  {
    Object value = getAttributeValue("boutgathersettle");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBdisableflag()
  {
    Object value = getAttributeValue("bdisableflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBestimateflag()
  {
    Object value = getAttributeValue("bestimateflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public UFBoolean getBwithdrawalflag()
  {
    Object value = getAttributeValue("bwithdrawalflag");
    return SmartVODataUtils.getUFBoolean(value);
  }

  public String getCaccountmonth()
  {
    return (String)getAttributeValue("caccountmonth");
  }

  public String getCaccountyear()
  {
    return (String)getAttributeValue("caccountyear");
  }

  public String getCagentid()
  {
    return (String)getAttributeValue("cagentid");
  }

  public String getCbillid()
  {
    return (String)getAttributeValue("cbillid");
  }

  public String getCbilltypecode()
  {
    return (String)getAttributeValue("cbilltypecode");
  }

  public String getCbiztypeid()
  {
    return (String)getAttributeValue("cbiztypeid");
  }

  public String getCbiztypename()
  {
    return (String)getAttributeValue("cbiztypename");
  }

  public String getCbusinessbillid()
  {
    return (String)getAttributeValue("cbusinessbillid");
  }

  public String getCcustombasid()
  {
    return (String)getAttributeValue("ccustombasid");
  }

  public String getCcustomvendorid()
  {
    return (String)getAttributeValue("ccustomvendorid");
  }

  public String getCdeptcode()
  {
    return (String)getAttributeValue("cdeptcode");
  }

  public String getCdeptid()
  {
    return (String)getAttributeValue("cdeptid");
  }

  public String getCdispatchid()
  {
    return (String)getAttributeValue("cdispatchid");
  }

  public String getCemployeeid()
  {
    return (String)getAttributeValue("cemployeeid");
  }

  public String getCoperatorid()
  {
    return (String)getAttributeValue("coperatorid");
  }

  public String getCoperatorname()
  {
    return (String)getAttributeValue("coperatorname");
  }

  public String getCothercalbodyid()
  {
    return (String)getAttributeValue("cothercalbodyid");
  }

  public String getCotherwarehouseid()
  {
    return (String)getAttributeValue("cotherwarehouseid");
  }

  public String getCothercorpid()
  {
    return (String)getAttributeValue("cothercorpid");
  }

  public String getCoutcalbodyid()
  {
    return (String)getAttributeValue("coutcalbodyid");
  }

  public String getCoutcorpid()
  {
    return (String)getAttributeValue("coutcorpid");
  }

  public String getCrdcenterid()
  {
    return (String)getAttributeValue("crdcenterid");
  }

  public String getCsourcemodulename()
  {
    return (String)getAttributeValue("csourcemodulename");
  }

  public String getCSQLClause()
  {
    return (String)getAttributeValue("csqlclause");
  }

  public String getCstockrdcenterid()
  {
    return (String)getAttributeValue("cstockrdcenterid");
  }

  public String getCwarehouseid()
  {
    return (String)getAttributeValue("cwarehouseid");
  }

  public String getCwarehousemanagerid()
  {
    return (String)getAttributeValue("cwarehousemanagerid");
  }

  public UFDate getDbilldate()
  {
    Object value = getAttributeValue("dbilldate");
    return SmartVODataUtils.getUFDate(value);
  }

  public UFDate getDcheckdate()
  {
    Object value = getAttributeValue("dcheckdate");
    return SmartVODataUtils.getUFDate(value);
  }

  public Integer getDr()
  {
    Object value = getAttributeValue("dr");
    return SmartVODataUtils.getInteger(value);
  }

  public Integer getFallocflag()
  {
    Object value = getAttributeValue("fallocflag");
    return SmartVODataUtils.getInteger(value);
  }

  public Integer getFdispatchflag()
  {
    Object value = getAttributeValue("fdispatchflag");
    return SmartVODataUtils.getInteger(value);
  }

  public Integer getIdeptattr()
  {
    Object value = getAttributeValue("ideptattr");
    return SmartVODataUtils.getInteger(value);
  }

  public Integer getIprintcount()
  {
    Object value = getAttributeValue("iprintcount");
    return SmartVODataUtils.getInteger(value);
  }

  public UFDouble getNcost()
  {
    Object value = getAttributeValue("ncost");
    return SmartVODataUtils.getUFDouble(value);
  }

  public String getPk_areacl()
  {
    return (String)getAttributeValue("pk_areacl");
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

  public String getTmaketime()
  {
    return (String)getAttributeValue("tmaketime");
  }

  public String getTlastmaketime()
  {
    return (String)getAttributeValue("tlastmaketime");
  }

  public String getVadjpricefilecode()
  {
    return (String)getAttributeValue("vadjpricefilecode");
  }

  public String getVagentname()
  {
    return (String)getAttributeValue("vagentname");
  }

  public String getVbillcode()
  {
    return (String)getAttributeValue("vbillcode");
  }

  public String getVbiztypename()
  {
    return (String)getAttributeValue("vbiztypename");
  }

  public String getVcheckbillcode()
  {
    return (String)getAttributeValue("vcheckbillcode");
  }

  public String getVcustomvendorname()
  {
    return (String)getAttributeValue("vcustomvendorname");
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

  public String getVdeptname()
  {
    return (String)getAttributeValue("vdeptname");
  }

  public String getVdispatchname()
  {
    return (String)getAttributeValue("vdispatchname");
  }

  public String getVemployeename()
  {
    return (String)getAttributeValue("vemployeename");
  }

  public String getVnote()
  {
    return (String)getAttributeValue("vnote");
  }

  public Class getVOMetaClass()
  {
    return BillHeaderVOMeta.class;
  }

  public String getVoperatorname()
  {
    return (String)getAttributeValue("voperatorname");
  }

  public String getVoutcalbodyname()
  {
    return (String)getAttributeValue("voutcalbodyname");
  }

  public String getVrdcentername()
  {
    return (String)getAttributeValue("vrdcentername");
  }

  public String getVstockrdcentername()
  {
    return (String)getAttributeValue("vstockrdcentername");
  }

  public String getVwarehousemanagername()
  {
    return (String)getAttributeValue("vwarehousemanagername");
  }

  public String getVwarehousename()
  {
    return (String)getAttributeValue("vwarehousename");
  }

  public void setAttributeValue(String key, Object value)
  {
    if (key.equals("bestimateflag")) {
      UFBoolean bEstimateFlag = new UFBoolean("N");
      if ((value != null) && ((value.toString().equals(this.m_comboItemsVO.name_estimated_yes)) || (value.toString().equals("Y"))))
      {
        bEstimateFlag = new UFBoolean("Y");
      }
      super.setAttributeValue(key, bEstimateFlag);
    }
    else if (key.equals("bwithdrawalflag")) {
      UFBoolean bWithdrawalFlag = new UFBoolean("N");
      if ((value != null) && ((value.toString().equals(this.m_comboItemsVO.name_yes)) || (value.toString().equals("Y"))))
      {
        bWithdrawalFlag = new UFBoolean("Y");
      }
      super.setAttributeValue(key, bWithdrawalFlag);
    }
    else if (key.equals("fallocflag")) {
      if ((value != null) && (value.toString().trim().length() != 0)) {
        Integer fAllocFlag = new Integer(0);
        if ((value.toString().equals(this.m_comboItemsVO.name_transfer_instore)) || (value.toString().equals("1")))
        {
          this.m_comboItemsVO.getClass(); fAllocFlag = new Integer(1);
        }
        else if ((value.toString().equals(this.m_comboItemsVO.name_transfer_stock)) || (value.toString().equals("2")))
        {
          this.m_comboItemsVO.getClass(); fAllocFlag = new Integer(2);
        }
        super.setAttributeValue(key, fAllocFlag);
      }
    }
    else {
      super.setAttributeValue(key, value);
    }
  }

  public void setBauditedflag(UFBoolean value)
  {
    setAttributeValue("bauditedflag", value);
  }

  public void setBcalculatedinvcost(UFBoolean value)
  {
    setAttributeValue("bcalculatedinvcost", value);
  }

  public void setBoutgathersettle(UFBoolean value)
  {
    setAttributeValue("boutgathersettle", value);
  }

  public void setBingathersettle(UFBoolean value)
  {
    setAttributeValue("bingathersettle", value);
  }

  public void setBdisableflag(UFBoolean value)
  {
    setAttributeValue("bdisableflag", value);
  }

  public void setBestimateflag(UFBoolean value)
  {
    setAttributeValue("bestimateflag", value);
  }

  public void setBwithdrawalflag(UFBoolean value)
  {
    setAttributeValue("bwithdrawalflag", value);
  }

  public void setCaccountmonth(String value)
  {
    setAttributeValue("caccountmonth", value);
  }

  public void setCaccountyear(String value)
  {
    setAttributeValue("caccountyear", value);
  }

  public void setCagentid(String value)
  {
    setAttributeValue("cagentid", value);
  }

  public void setCbillid(String value)
  {
    setAttributeValue("cbillid", value);
  }

  public void setCbilltypecode(String value)
  {
    setAttributeValue("cbilltypecode", value);
  }

  public void setCbiztypeid(String value)
  {
    setAttributeValue("cbiztypeid", value);
  }

  public void setCbiztypename(String value)
  {
    setAttributeValue("cbiztypename", value);
  }

  public void setCbusinessbillid(String value)
  {
    setAttributeValue("cbusinessbillid", value);
  }

  public void setCcustombasid(String value)
  {
    setAttributeValue("ccustombasid", value);
  }

  public void setCcustomvendorid(String value)
  {
    setAttributeValue("ccustomvendorid", value);
  }

  public void setCdeptcode(String value)
  {
    setAttributeValue("cdeptcode", value);
  }

  public void setCdeptid(String value)
  {
    setAttributeValue("cdeptid", value);
  }

  public void setCdispatchid(String value)
  {
    setAttributeValue("cdispatchid", value);
  }

  public void setCemployeeid(String value)
  {
    setAttributeValue("cemployeeid", value);
  }

  public void setCoperatorid(String value)
  {
    setAttributeValue("coperatorid", value);
  }

  public void setCoperatorname(String value)
  {
    setAttributeValue("coperatorname", value);
  }

  public void setCothercalbodyid(String value)
  {
    setAttributeValue("cothercalbodyid", value);
  }

  public void setCotherwarehouseid(String value)
  {
    setAttributeValue("cotherwarehouseid", value);
  }

  public void setCothercorpid(String value)
  {
    setAttributeValue("cothercorpid", value);
  }

  public void setCoutcalbodyid(String value)
  {
    setAttributeValue("coutcalbodyid", value);
  }

  public void setCoutcorpid(String value)
  {
    setAttributeValue("coutcorpid", value);
  }

  public void setCrdcenterid(String value)
  {
    setAttributeValue("crdcenterid", value);
  }

  public void setCsourcemodulename(String value)
  {
    setAttributeValue("csourcemodulename", value);
  }

  public void setCSQLClause(String value)
  {
    setAttributeValue("csqlclause", value);
  }

  public void setCstockrdcenterid(String value)
  {
    setAttributeValue("cstockrdcenterid", value);
  }

  public void setCwarehouseid(String value)
  {
    setAttributeValue("cwarehouseid", value);
  }

  public void setCwarehousemanagerid(String value)
  {
    setAttributeValue("cwarehousemanagerid", value);
  }

  public void setDbilldate(UFDate value)
  {
    setAttributeValue("dbilldate", value);
  }

  public void setDcheckdate(UFDate value)
  {
    setAttributeValue("dcheckdate", value);
  }

  public void setDr(Integer value)
  {
    setAttributeValue("dr", value);
  }

  public void setFallocflag(Integer value)
  {
    setAttributeValue("fallocflag", value);
  }

  public void setFdispatchflag(Integer value)
  {
    setAttributeValue("fdispatchflag", value);
  }

  public void setIdeptattr(Integer value)
  {
    setAttributeValue("ideptattr", value);
  }

  public void setIprintcount(Integer value)
  {
    setAttributeValue("iprintcount", value);
  }

  public void setNcost(UFDouble value)
  {
    setAttributeValue("ncost", value);
  }

  public void setPk_areacl(String value)
  {
    setAttributeValue("pk_areacl", value);
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

  public void setTmaketime(String value)
  {
    setAttributeValue("tmaketime", value);
  }

  public void setTlastmaketime(String value)
  {
    setAttributeValue("tlastmaketime", value);
  }

  public void setVadjpricefilecode(String value)
  {
    setAttributeValue("vadjpricefilecode", value);
  }

  public void setVagentname(String value)
  {
    setAttributeValue("vagentname", value);
  }

  public void setVbillcode(String value)
  {
    setAttributeValue("vbillcode", value);
  }

  public void setVbiztypename(String value)
  {
    setAttributeValue("vbiztypename", value);
  }

  public void setVcheckbillcode(String value)
  {
    setAttributeValue("vcheckbillcode", value);
  }

  public void setVcustomvendorname(String value)
  {
    setAttributeValue("vcustomvendorname", value);
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

  public void setVdeptname(String value)
  {
    setAttributeValue("vdeptname", value);
  }

  public void setVdispatchname(String value)
  {
    setAttributeValue("vdispatchname", value);
  }

  public void setVemployeename(String value)
  {
    setAttributeValue("vemployeename", value);
  }

  public void setVnote(String value)
  {
    setAttributeValue("vnote", value);
  }

  public void setVoperatorname(String value)
  {
    setAttributeValue("voperatorname", value);
  }

  public void setVoutcalbodyname(String value)
  {
    setAttributeValue("voutcalbodyname", value);
  }

  public void setVrdcentername(String value)
  {
    setAttributeValue("vrdcentername", value);
  }

  public void setVstockrdcentername(String value)
  {
    setAttributeValue("vstockrdcentername", value);
  }

  public void setVwarehousemanagername(String value)
  {
    setAttributeValue("vwarehousemanagername", value);
  }

  public void setVwarehousename(String value)
  {
    setAttributeValue("vwarehousename", value);
  }

  public void setClastoperatorid(String value) {
    setAttributeValue("clastoperatorid", value);
  }

  public String getClastoperatorid() {
    return (String)getAttributeValue("clastoperatorid");
  }

  public void setClastoperatorname(String value) {
    setAttributeValue("clastoperatorname", value);
  }

  public String getClastoperatorname() {
    return (String)getAttributeValue("clastoperatorname");
  }

  public void verify()
    throws ValidationException
  {
    ArrayList errFields = new ArrayList();

    String sBillTypeCode = "";

    if (getPk_corp() == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000787"));
    }

    if (getCbilltypecode() == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000807"));
    }
    else
    {
      sBillTypeCode = getCbilltypecode();
    }
    if ((getDbilldate() == null) || (getDbilldate().toString().trim().length() == 0))
    {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000799"));
    }

    if (((getCrdcenterid() == null) || (getCrdcenterid().trim().length() == 0)) && (!sBillTypeCode.equals("IF")))
    {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPT20143010-000007"));
    }

    if (((getCstockrdcenterid() == null) || (getCstockrdcenterid().trim().length() == 0)) && (getCwarehouseid() != null) && (getCwarehouseid().length() != 0))
    {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPT20143010-000004"));
    }

    if (getBestimateflag() == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002454"));
    }

    if (getBwithdrawalflag() == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0002342"));
    }

    if (getBdisableflag() == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000257"));
    }

    if (getBauditedflag() == null) {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000263"));
    }

    if (((sBillTypeCode == "I5") || (sBillTypeCode == "I1")) && ((getCcustomvendorid() == null) || (getCcustomvendorid().trim().length() == 0)))
    {
      errFields.add(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001589"));
    }

    StringBuffer message = new StringBuffer();
    message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("20143010", "UPP20143010-000094"));

    if (errFields.size() > 0) {
      String[] temp = new String[errFields.size()];
      temp = (String[])(String[])errFields.toArray(temp);
      message.append(temp[0]);
      for (int i = 1; i < temp.length; i++) {
        message.append(", ");
        message.append(temp[i]);
      }

      throw new NullFieldException(message.toString());
    }
  }
}