package nc.ui.ic.ic281;

import java.awt.Container;
import java.awt.Frame;
import javax.swing.table.TableCellEditor;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.query.QueryTabModel;
import nc.vo.ic.pub.lang.ResBase;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.QueryConditionVO;

public class QueryConditionDlg extends QueryConditionDlgForBill
{
  public QueryConditionDlg()
  {
  }

  public QueryConditionDlg(Container parent)
  {
    super(parent);
  }

  public QueryConditionDlg(Container parent, String title)
  {
    super(parent, title);
  }

  public QueryConditionDlg(Frame parent)
  {
    super(parent);
  }

  public QueryConditionDlg(Frame parent, String title)
  {
    super(parent, title);
  }

  public void afterEdit(TableCellEditor editor, int row, int col)
  {
    super.afterEdit(editor, row, col);

    if ((col == this.COLVALUE) && ((editor instanceof UIRefCellEditor)))
    {
      Object temp = ((UIRefCellEditor)editor).getComponent();
      if (((temp instanceof UIRefPane)) && 
        (getFieldCodeByRow(row).equals("storcode"))) {
        UIRefPane pane = (UIRefPane)temp;
        if (pane.getRefPK() == null) {
          return;
        }

        Object obj = getValueRefObjectByFieldCode("cspacecode");
        if (obj == null)
          return;
        if (!(obj instanceof UIRefPane)) {
          return;
        }
        UIRefPane refp = (UIRefPane)obj;
        try
        {
          if ("N".equals((String)((Object[])(Object[])nc.ui.scm.pub.cache.CacheTool.getCellValue("bd_stordoc", "pk_stordoc", "csflag", pane.getRefPK()))[0]))
          {
            refp.setEnabled(false);
            setValue(null, null, "cspacecode");
            getUITablePane().getTable().updateUI();
          }
          else
          {
            refp.setEnabled(true);
          }
        }
        catch (BusinessException bex)
        {
          MessageDialog.showErrorDlg(this, "ÌבÊ¾", ResBase.getIFInvspacemag());
        }
      }
    }
  }

  public void setValue(String pkvalue, String namevalue, String fieldcode)
  {
    if (fieldcode == null) {
      return;
    }
    int rowcount = getTabModelInput().getRowCount();

    int initConditions = getConditionDatas().length;

    int immobilityRows = getImmobilityRows();

    Object obj = getValueRefObjectByFieldCode(fieldcode);
    if (obj == null)
      return;
    if (!(obj instanceof UIRefPane)) {
      return;
    }
    UIRefPane uirp = (UIRefPane)obj;
    uirp.setPK(pkvalue);

    UIRefCellEditor tc = new UIRefCellEditor(uirp);

    for (int row = immobilityRows; row < rowcount; row++) {
      Object fieldObj = getTabModelInput().getValueAt(row, this.COLFIELD);
      String fieldName = fieldObj == null ? "" : fieldObj.toString();
      int index_row = getIndexes(row);
      if ((index_row < 0) || (index_row >= initConditions) || 
        (!getConditionDatas()[index_row].getFieldCode().equals(fieldcode)))
        continue;
      super.afterEdit(tc, row, this.COLVALUE);
    }

    setConditionVO();
  }
}