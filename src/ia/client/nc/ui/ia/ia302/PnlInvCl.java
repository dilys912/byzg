package nc.ui.ia.ia302;

import java.util.Vector;
import javax.swing.table.TableColumnModel;
import nc.ui.ia.pub.IATableModel;
import nc.ui.ia.pub.IAUITable;
import nc.ui.ml.NCLangRes;

public class PnlInvCl extends PrAllocTabPanel
{
  private static final long serialVersionUID = 3348751405600759981L;

  public PnlInvCl()
  {
    modiTitle();
  }

  private void modiTitle()
  {
    this.m_vColName.clear();
    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001449"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001446"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000649"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001945"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001775"));

    this.m_TableModel.setDataVector(new Vector(), this.m_vColName);
    this.m_iNumColumn = this.m_UITable.getColumnModel().getColumnIndex(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000649"));

    this.m_iMnyColumn = this.m_UITable.getColumnModel().getColumnIndex(NCLangRes.getInstance().getStrByID("common", "UC000-0001945"));

    this.m_iPriceColumn = this.m_UITable.getColumnModel().getColumnIndex(NCLangRes.getInstance().getStrByID("common", "UC000-0001775"));

    setEditEnable(3, 4);

    int[] cols = { 3, 4 };

    this.m_UITable.setNotNullColumnByModelColumns(cols);
  }

  public void setEdit(boolean b)
  {
    if (b) {
      setEditEnable(3, 4);
    }
    else
      setEditDisable(3, 4);
  }
}