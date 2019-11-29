package nc.ui.bd.ref.busi;

import java.util.Hashtable;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;

public class CustmandocDefaultRefModel extends AbstractRefGridTreeModel
{
  public CustmandocDefaultRefModel(String refNodeName)
  {
    setRefNodeName(refNodeName);
  }

  public void setRefNodeName(String refNodeName)
  {
    this.m_strRefNodeName = refNodeName;
    setRootName(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001235"));

    setClassFieldCode(new String[] { "areaclcode", "areaclname", "pk_areacl", "pk_corp", "pk_fatherarea" });

    setFatherField("pk_fatherarea");
    setChildField("pk_areacl");
    setClassJoinField("pk_areacl");
    setClassTableName("bd_areacl");
    setClassDefaultFieldCount(2);

    setClassWherePart(" (pk_corp='" + getPk_corp() + "' or pk_corp= '" + "0001" + "')");

    setClassDataPower(true);

    setConfig(refNodeName);
    setHiddenFieldCode(new String[] { "bd_cubasdoc.taxpayerid", "bd_cumandoc.pk_cumandoc", "bd_cubasdoc.pk_cubasdoc", "bd_cubasdoc.pk_areacl" });

    setTableName("bd_cumandoc inner join bd_cubasdoc on bd_cumandoc.pk_cubasdoc=bd_cubasdoc.pk_cubasdoc ");

    setPkFieldCode("bd_cumandoc.pk_cumandoc");
    setStrPatch("distinct");

    setDocJoinField("bd_cubasdoc.pk_areacl");

    setMnecode(new String[] { "bd_cumandoc.cmnecode", "bd_cubasdoc.custname" });

    setRefQueryDlgClaseName("nc.ui.bd.b09.QueryDlgForRef");

    Hashtable contents = new Hashtable();
    contents.put("0", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", "UPPref-000371"));

    contents.put("1", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", "UPPref-000372"));

    contents.put("2", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", "UPPref-000373"));

    contents.put("3", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", "UPPref-000374"));

    Hashtable contents1 = new Hashtable();
    contents1.put("0", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", "UPPref-000375"));

    contents1.put("1", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", "UPPref-000376"));

    contents1.put("2", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", "UPPref-000377"));

    Hashtable contents2 = new Hashtable();
    contents2.put("0", NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001589"));

    contents2.put("1", NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000275"));

    contents2.put("2", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", "UPPref-000378"));

    contents2.put("3", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref", "UPPref-000379"));

    Hashtable convert = new Hashtable();
    convert.put("bd_cubasdoc.custprop", contents);
    convert.put("bd_cumandoc.custstate", contents1);
    convert.put("bd_cumandoc.custflag", contents2);
    setDispConvertor(convert);
    String strFomula = "getColValue(bd_settleunit, settleunitname, pk_settleunit ,bd_cumandoc.pk_settleunit)";
    setFormulas(new String[][] { { "bd_cumandoc.pk_settleunit", strFomula } });
    String wherePart = null;
    if (refNodeName.equals("客商档案")) {
      wherePart = " bd_cumandoc.pk_corp='" + getPk_corp() + "' AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2') ";
    }
    else if (refNodeName.equals("客户档案"))
    {
      wherePart = " bd_cumandoc.pk_corp='" + getPk_corp() + "'  AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='2') ";
    }
    else if (refNodeName.equals("供应商档案"))
    {
      wherePart = " bd_cumandoc.pk_corp='" + getPk_corp() + "' AND (bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='3') ";
    }
    else if (refNodeName.equals("客商档案包含冻结")) {
      wherePart = " bd_cumandoc.pk_corp='" + getPk_corp() + "'  AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2') ";
    }
    else if (refNodeName.equals("客户档案包含冻结")) {
      wherePart = " (bd_cumandoc.pk_corp='" + getPk_corp() + "'  AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='2')) ";
    }
    else if (refNodeName.equals("供应商档案包含冻结")) {
      wherePart = " bd_cumandoc.pk_corp='" + getPk_corp() + "'  AND (bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='3') ";
    }

    if (isGroupAssignData()) {
      String groupDataWherePart = " and bd_cumandoc.pk_cubasdoc in (select pk_cubasdoc from bd_cubasdoc where pk_corp='0001') ";
      setWherePart(wherePart + groupDataWherePart);
    } else {
      setWherePart(wherePart);
    }

    resetFieldName();
  }
}