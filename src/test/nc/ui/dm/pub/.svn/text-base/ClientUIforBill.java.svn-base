package nc.ui.dm.pub;

import java.util.ArrayList;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillScrollPane;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;

public class ClientUIforBill extends DMToftPanel
{
  public ClientUIforBill()
  {
  }

  public ClientUIforBill(boolean bIsNotShow)
  {
    super(bIsNotShow);
  }

  public void afterDel(int iHeaderRow)
  {
    getAllVOs().remove(iHeaderRow);
    this.m_iLastSelListHeadRow -= 1;

    getBillCardPanel().setEnabled(false);
    switchButtonStatus(DMBillStatus.CardView);
    refreshListTable();

    refreshCardTable();
  }

  public void afterSave(DMVO dvosave)
  {
    if (dvosave.getHeaderVO().getStatus() == 2) {
      getAllVOs().add(dvosave);
      this.m_iLastSelListHeadRow = (getAllVOs().size() - 1);
    }
    else if (dvosave.getHeaderVO().getStatus() == 1) {
      getAllVOs().set(this.m_iLastSelListHeadRow, dvosave);
    }
    else if (dvosave.getHeaderVO().getStatus() == 3) {
      getAllVOs().remove(this.m_iLastSelListHeadRow);
      this.m_iLastSelListHeadRow -= 1;
    }

    getBillCardPanel().setEnabled(false);
    switchButtonStatus(DMBillStatus.CardView);
    refreshListTable();
    refreshCardTable();
  }

  public void initialize()
  {
    super.initialize();

    if (null != getBillListPanel().getBillListData()) {
      getBillListPanel().addEditListener(this);
      getBillListPanel().getHeadTable().addMouseListener(this);

      getBillListPanel().getChildListPanel().setTatolRowShow(true);
    }
  }

  public void onCancel()
  {
    super.onCancel();
    getBillCardPanel().setEnabled(false);
    switchButtonStatus(DMBillStatus.CardView);
    refreshCardTable();
    setEditFlag(DMBillStatus.CardView);
  }

  public void onEdit()
  {
    super.onEdit();
    switchButtonStatus(DMBillStatus.CardEdit);
    setEditFlag(DMBillStatus.CardEdit);
  }

  protected void switchButtonStatus(int status)
  {
    if (status == DMBillStatus.CardView)
    {
      setButton(this.boAdd, true);
      setButton(this.boEdit, true);
      setButton(this.boDel, true);

      setButton(this.boSave, false);
      setButton(this.boCancel, false);
      setButton(this.boLine, false);
      setButton(this.boAddLine, false);
      setButton(this.boDelLine, false);

      setButton(this.boSwith, true);
      setButton(this.boPrint, true);
      setButton(this.boQuery, true);
    } else if (status == DMBillStatus.CardEdit)
    {
      setButton(this.boAdd, false);
      setButton(this.boEdit, false);
      setButton(this.boDel, false);
      setButton(this.boLine, true);
      setButton(this.boAddLine, true);
      setButton(this.boDelLine, true);
      setButton(this.boSave, true);
      setButton(this.boCancel, true);

      setButton(this.boSwith, false);
      setButton(this.boPrint, false);
      setButton(this.boQuery, false);
    } else if (status == DMBillStatus.ListView)
    {
      setButton(this.boAdd, true);
      setButton(this.boEdit, true);
      setButton(this.boDel, true);
      setButton(this.boSave, false);
      setButton(this.boCancel, false);
      setButton(this.boAddLine, false);
      setButton(this.boDelLine, false);
      setButton(this.boLine, false);

      setButton(this.boSwith, true);
      setButton(this.boPrint, true);
      setButton(this.boQuery, true);
    }
  }
}