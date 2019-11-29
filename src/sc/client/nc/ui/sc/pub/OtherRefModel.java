package nc.ui.sc.pub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;

public class OtherRefModel extends AbstractRefModel
{
  public static String REF_ASSMEAS = NCLangRes.getInstance().getStrByID("common", "UC000-0003975");

  public static String REF_JOBPHASE = NCLangRes.getInstance().getStrByID("common", "UC000-0004177");

  public static String REF_CUSTBANK = NCLangRes.getInstance().getStrByID("40120002", "UPP40120002-000011");

  public static String REF_VBATCH = NCLangRes.getInstance().getStrByID("40120002", "UPP40120002-000012");

  private String sType = REF_ASSMEAS;

  public OtherRefModel()
  {
    setHiddenFieldCode(new String[] { "bd_convert.pk_measdoc" });
  }

  public OtherRefModel(String sRefType)
  {
    this.sType = sRefType;
    if (this.sType.equals(REF_ASSMEAS))
      setHiddenFieldCode(new String[] { "bd_convert.pk_measdoc" });
    if (this.sType.equals(REF_JOBPHASE))
      setHiddenFieldCode(new String[] { "bd_jobphase.pk_jobphase" });
    if (this.sType.equals(REF_CUSTBANK))
      setHiddenFieldCode(new String[] { "bd_custbank.pk_custbank" });
    if (this.sType.equals(REF_VBATCH))
      setHiddenFieldCode(new String[] { "cvendorid " });
  }

  public String[] getFieldCode()
  {
    if (this.sType.equals(REF_ASSMEAS))
      return new String[] { "bd_measdoc.shortname", "bd_measdoc.measname" };
    if (this.sType.equals(REF_JOBPHASE)) {
      return new String[] { "bd_jobphase.jobphasecode", "bd_jobphase.jobphasename" };
    }
    if (this.sType.equals(REF_CUSTBANK)) {
      return new String[] { "bd_custbank.account", "bd_custbank.accname", "bd_custbank.accaddr", "bd_custbank.defflag" };
    }
    if (this.sType.equals(REF_VBATCH)) {
      StringBuffer strbuf = new StringBuffer();
      strbuf.append(" sum(case rtrim(isourcetype) when '0' then nnum when '1' then -nnum  when '2' then -nnum when '3' then nnum end) ");
      //add by hk 
      if("1078".equals(pk_corp)||"1108".equals(pk_corp)){    	  
    	  return new String[] { "(vbatch||'&'|| vfree1) vbatch_hk", "vbatch ","vfree1", strbuf.toString() };
      }
      //add by hk end
	  return new String[] { "vbatch ", strbuf.toString() };
    }
    return null;
  }

  public String[] getFieldName()
  {
    if (this.sType.equals(REF_ASSMEAS)) {
      return new String[] { NCLangRes.getInstance().getStrByID("40120002", "UPP40120002-000013"), NCLangRes.getInstance().getStrByID("common", "UC000-0003529") };
    }

    if (this.sType.equals(REF_JOBPHASE)) {
      return new String[] { NCLangRes.getInstance().getStrByID("common", "UC000-0004185"), NCLangRes.getInstance().getStrByID("common", "UC000-0004180") };
    }

    if (this.sType.equals(REF_CUSTBANK)) {
      return new String[] { NCLangRes.getInstance().getStrByID("common", "UC000-0004118"), NCLangRes.getInstance().getStrByID("common", "UC000-0004115"), NCLangRes.getInstance().getStrByID("40120002", "UPP40120002-000014"), NCLangRes.getInstance().getStrByID("40120002", "UPP40120002-000015") };
    }

    if (this.sType.equals(REF_VBATCH)) {
    	//add by hk 
    	if("1078".equals(pk_corp)||"1108".equals(pk_corp)){    		
    		return new String[] { "拼接","批次号","钢卷号", "数量" };
    	}
    	//add by hk end
      return new String[] { NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000182"), NCLangRes.getInstance().getStrByID("common", "UC000-0000249") };
    }

    return null;
  }

  public String getPkFieldCode()
  {
    if (this.sType.equals(REF_ASSMEAS))
      return "bd_convert.pk_measdoc";
    if (this.sType.equals(REF_JOBPHASE))
      return "bd_jobphase.pk_jobphase";
    if (this.sType.equals(REF_CUSTBANK))
      return "bd_custbank.pk_custbank";
    if (this.sType.equals(REF_VBATCH)){
    	//add by hk 
    	if("1078".equals(pk_corp)||"1108".equals(pk_corp)){
    		return "vbatch_hk";
    	}
    	//add by hk
    	return "vbatch";
     }
    return null;
  }

  public String getRefTitle()
  {
    if (this.sType.equals(REF_ASSMEAS)) {
      return NCLangRes.getInstance().getStrByID("common", "UC000-0003975");
    }
    if (this.sType.equals(REF_JOBPHASE)) {
      return NCLangRes.getInstance().getStrByID("common", "UC000-0004177");
    }
    if (this.sType.equals(REF_CUSTBANK)) {
      return NCLangRes.getInstance().getStrByID("40120002", "UPP40120002-000011");
    }
    if (this.sType.equals(REF_VBATCH)) {
      return NCLangRes.getInstance().getStrByID("40120002", "UPP40120002-000016");
    }
    return null;
  }

  protected String getSql(String strPatch, String[] strFieldCode, String[] hiddenFields, String strTableName, String strWherePart, String strGroupField, String strOrderField)
  {
    if (strTableName == null)
      return null;
    int iSelectFieldCount = strFieldCode == null ? 0 : strFieldCode.length;
    String strSql = "select " + strPatch + " ";
    for (int i = 0; i < iSelectFieldCount; i++) {
      strSql = strSql + strFieldCode[i];
      if (i < iSelectFieldCount - 1) {
        strSql = strSql + ",";
      }

    }

    if ((hiddenFields != null) && (hiddenFields.length > 0)) {
      for (int k = 0; k < hiddenFields.length; k++) {
        if ((hiddenFields[k] == null) || (hiddenFields[k].trim().length() <= 0))
          continue;
        strSql = strSql + ",";
        strSql = strSql + hiddenFields[k];
      }

    }

    strSql = strSql + " from " + strTableName;

    if ((strWherePart != null) && (strWherePart.trim().length() != 0)) {
      if (this.sType.equals(REF_VBATCH))
        strWherePart = strWherePart + " and vbatch is not null ";
      strWherePart = " where (" + strWherePart + " )";
    } else {
      strWherePart = " where 11=11 ";
    }strSql = strSql + " " + strWherePart;

    if ((getBlurFields() != null) && (getFieldIndex(getBlurFields()[0]) >= 0) && (getBlurValue() != null))
    {
      if ((getBlurValue().indexOf('*') != -1) || (getBlurValue().indexOf('?') != -1))
      {
        strSql = strSql + " and ( " + getBlurFields()[0] + " like '" + getBlurValue().replace('*', '%').replace('?', '_') + "' )";
      }

    }

    if (strGroupField != null) {
      if (this.sType.equals(REF_ASSMEAS))
    	  strSql = strSql + "  " + strGroupField;
      else strSql = strSql + "  group by  " + strGroupField;
    }
    else if (strOrderField != null) {
      strSql = strSql + " order by " + strOrderField;
    }

    return strSql;
  }

  public String getTableName()
  {
    if (this.sType.equals(REF_ASSMEAS))
      return " bd_convert left join bd_measdoc on bd_convert.pk_measdoc=bd_measdoc.pk_measdoc ";
    if (this.sType.equals(REF_JOBPHASE))
      return "bd_jobphase";
    if (this.sType.equals(REF_CUSTBANK))
      return "bd_custbank";
    if (this.sType.equals(REF_VBATCH)) {
      return "sc_materialledger";
    }

    return null;
  }
  //add by hk
  String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
  public int getDefaultFieldCount() {
	  if (this.sType.equals(REF_VBATCH)&&("1078".equals(pk_corp)||"1108".equals(pk_corp))) {
	      return 4;
	    }
	  return super.getDefaultFieldCount();
	}
  //add by hk end
}