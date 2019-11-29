package nc.ui.bd.ref.busi;

import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.pub.ClientEnvironment;

public class CargdocDefaultRefModel extends AbstractRefTreeModel
{
  public CargdocDefaultRefModel(String refNodeName)
  {
    setRefNodeName(refNodeName);
  }

  public void setRefNodeName(String refNodeName) {
    this.m_strRefNodeName = refNodeName;
    setFieldCode(new String[] { "bd_cargdoc.cscode", "bd_cargdoc.csname", "bd_cargdoc.memo", "bd_cargdoc.sealflag" });

    setHiddenFieldCode(new String[] { "bd_cargdoc.pk_cargdoc" });
    setTableName("bd_cargdoc inner join bd_stordoc on bd_stordoc.pk_stordoc=bd_cargdoc.pk_stordoc and bd_stordoc.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"'");
    setPkFieldCode("bd_cargdoc.pk_cargdoc");
    setCodingRule("2/2/2/2/2/2/2/2/2");
    setDefaultFieldCount(3);
    resetFieldName();
  }
}