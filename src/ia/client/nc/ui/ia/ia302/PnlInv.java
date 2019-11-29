package nc.ui.ia.ia302;

import java.util.Vector;
import javax.swing.table.TableColumnModel;
import nc.ui.ia.pub.IATableModel;
import nc.ui.ia.pub.IAUITable;
import nc.ui.ml.NCLangRes;

public class PnlInv extends PrAllocTabPanel
{
  private static final long serialVersionUID = 3768077452139049025L;

  public PnlInv()
  {
    modiTitle();
  }

  private void modiTitle()
  {
    this.m_vColName.clear();
    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0002930"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001480"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001453"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0003448"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001240"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000649"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001945"));

    this.m_vColName.addElement(NCLangRes.getInstance().getStrByID("common", "UC000-0001775"));

    this.m_TableModel.setDataVector(new Vector(), this.m_vColName);
    this.m_iNumColumn = this.m_UITable.getColumnModel().getColumnIndex(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000649"));

    this.m_iMnyColumn = this.m_UITable.getColumnModel().getColumnIndex(NCLangRes.getInstance().getStrByID("common", "UC000-0001945"));

    this.m_iPriceColumn = this.m_UITable.getColumnModel().getColumnIndex(NCLangRes.getInstance().getStrByID("common", "UC000-0001775"));

    setEditEnable(6, 7);

    int[] cols = { 6, 7 };

    this.m_UITable.setNotNullColumnByModelColumns(cols);
  }

  public void setEdit(boolean b)
  {
    if (b) {
      setEditEnable(6, 7);
    }
    else
      setEditDisable(6, 7);
  }
}