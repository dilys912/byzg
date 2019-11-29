package nc.ui.ia.ia204;

import nc.ui.ia.bill.BillClientUI;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.linkoperate.ILinkQueryData;

public class SaleBillClientUI extends BillClientUI
{
  private static final long serialVersionUID = 896295234373723203L;
  private static String m_sTitle = NCLangRes.getInstance().getStrByID("20143010", "UPP20143010-000260");

  private static String m_sBillType = "I5";

  public SaleBillClientUI()
  {
    super(m_sTitle, null, m_sBillType);
    loadTemplet(m_sTitle, m_sBillType);
  }

  public void doQueryAction(ILinkQueryData querydata)
  {
    super.doQueryAction(querydata);
  }

  protected String getFunCode()
  {
    return "20144010";
  }
}