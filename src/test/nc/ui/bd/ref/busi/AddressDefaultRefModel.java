package nc.ui.bd.ref.busi;

import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.vo.ml.NCLangRes4VoTransl;

public class AddressDefaultRefModel extends AbstractRefGridTreeModel
{
  public AddressDefaultRefModel(String refNodeName)
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

    setFieldCode(new String[] { "addrcode", "addrname" });

    setHiddenFieldCode(new String[] { "pk_address", "bd_address.pk_areacl" });
    setPkFieldCode("pk_address");
    setWherePart("where ( bd_address.pk_corp ='0001' or bd_address.pk_corp='" + getPk_corp() + "') ");

    setTableName("bd_address");
    setDefaultFieldCount(2);
    setDocJoinField("bd_address.pk_areacl");
    setOrderPart(" addrcode");

    resetFieldName();
  }
}