package nc.ui.ic.ic201;

import nc.ui.po.pub.CPoToIcDeputyUI;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pub.SCMEnv;

public class FromPoUI
  implements IFromPoUI
{
  public void onNewReplenishInvBillByOrder(ClientUI cont, String m_sCorpID)
  {
    try
    {
      AggregatedValueObject[] vos = null;

      CPoToIcDeputyUI obj = new CPoToIcDeputyUI(cont, m_sCorpID);

      if (obj != null) {
        vos = obj.getVOs();
      }

      if ((vos == null) || (vos.length == 0) || (vos[0] == null)) {
        return;
      }

      AggregatedValueObject[] voRetvos = (AggregatedValueObject[])PfChangeBO_Client.pfChangeBillToBillArray(vos, "21", BillTypeConst.m_purchaseIn);

      if ((voRetvos == null) || (voRetvos.length == 0) || (voRetvos[0] == null)) {
        return;
      }
      for (int i = 0; i < voRetvos.length; ++i) {
        ((GeneralBillVO)voRetvos[i]).getHeaderVO().setFreplenishflag(new UFBoolean(true));
      }

      GenMethod.setFieldBacktoAnother(voRetvos, new String[] { "nshouldbacknum", "nshouldbackastnum" }, new String[] { "nshouldinnum", "nneedinassistnum" });

      GenMethod.setNegative(voRetvos, "nshouldinnum", "nneedinassistnum");

      String sBusiTypeID = (voRetvos[0].getParentVO().getAttributeValue("cbiztype") == null) ? null : (String)voRetvos[0].getParentVO().getAttributeValue("cbiztype");
      cont.setBillRefResultVO(sBusiTypeID, voRetvos);
    }
    catch (Exception e)
    {
      SCMEnv.error(e);
      cont.showErrorMessage(e.getMessage());
    }
  }
}