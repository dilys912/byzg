package nc.ui.po.pub;

import java.awt.Container;
import nc.ui.pu.inter.IPuToIc_QueDlg;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.scm.pub.SCMEnv;

public class CPoToIcDeputyUI
  implements IPuToIc_QueDlg
{
  private Container parent = null;
  PoToBackIcQueDLG dlg = null;

  public CPoToIcDeputyUI(Container parent, String sCorp)
  {
    this.dlg = new PoToBackIcQueDLG(parent, sCorp);

    this.parent = parent;
  }

  public AggregatedValueObject[] getVOs()
  {
    if (this.dlg.showModal() == 1) {
      POToBackIcDLG poToBackIcDLG = new POToBackIcDLG(this.parent, this.dlg);
      if (poToBackIcDLG.showModal() == 1) {
        AggregatedValueObject[] vos = poToBackIcDLG.getRetVos();
        if ((vos != null) && (vos.length > 0) && (vos[0] != null)) {
          return vos;
        }
      }
    }
    SCMEnv.out("没有查询到采购订单数据，返回NULL！");
    return null;
  }
}