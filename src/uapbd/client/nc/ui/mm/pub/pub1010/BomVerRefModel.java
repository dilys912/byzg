package nc.ui.mm.pub.pub1010;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ml.NCLangRes;

public class BomVerRefModel extends AbstractRefModel
{
  final String[] fieldname = { NCLangRes.getInstance().getStrByID("1009", "UC000-0002905"), NCLangRes.getInstance().getStrByID("1009", "UC000-0002447"), NCLangRes.getInstance().getStrByID("1009", "UC000-0002366"), NCLangRes.getInstance().getStrByID("1009", "UC000-0002282"), NCLangRes.getInstance().getStrByID("1009", "UC000-0002285"), NCLangRes.getInstance().getStrByID("1009", "UC000-0002284"), NCLangRes.getInstance().getStrByID("1009", "UC000-0001219"), NCLangRes.getInstance().getStrByID("1009", "UC000-0001376") };
  final String[] fieldcode = { "version", "sfmr", "sftpmr", "sl", "slxx", "slsx", "gdsl", "memo" };
  final String title = NCLangRes.getInstance().getStrByID("1009", "UPP-000072");
  final String pkcode = "version";
  final String tablename = "bd_bom";
  final String[] hiddenFieldCode = { "pk_bomid", "pk_produce", "wlbmid" };

  public BomVerRefModel()
  {
    setMatchPkWithWherePart(true);
    setWherePart("where pk_corp='" + getPk_corp() + "' and bblx=0");
  }

  public int getDefaultFieldCount()
  {
    return this.fieldcode.length;
  }

  public String[] getFieldCode()
  {
    return this.fieldcode;
  }

  public String[] getFieldName()
  {
    return this.fieldname;
  }
  public String[] getHiddenFieldCode() {
    return this.hiddenFieldCode;
  }

  public String getPkFieldCode()
  {
    return "version";
  }

  public String getRefNameField()
  {
    return this.fieldcode[0];
  }

  public String getRefTitle()
  {
    return this.title;
  }

  public String getTableName()
  {
    return "bd_bom";
  }
}