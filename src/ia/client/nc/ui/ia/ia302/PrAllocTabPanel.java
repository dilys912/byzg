package nc.ui.ia.ia302;

import java.awt.CardLayout;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nc.ui.ia.pub.IATableCellEditor;
import nc.ui.ia.pub.IATableCellRender;
import nc.ui.ia.pub.IATableModel;
import nc.ui.ia.pub.IATableSelectionEvent;
import nc.ui.ia.pub.IAUITable;
import nc.ui.ia.pub.IIATableSelectionListener;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextField;
import nc.vo.ia.pub.Log;
import nc.vo.pub.lang.UFDouble;

public class PrAllocTabPanel extends UIPanel
  implements IIATableSelectionListener
{
  private static final long serialVersionUID = 8502301633287430418L;
  private double m_dMaxValue = new UFDouble(1000000000000.0D).doubleValue();
  protected IAUITable m_UITable;
  protected IATableModel m_TableModel;
  protected int m_iNumColumn = -1;

  protected int m_iMnyColumn = -1;

  protected int m_iPriceColumn = -1;

  private boolean m_bError = false;

  private int[] m_iaPrec = null;

  private String[] m_saInvPK = null;

  private String m_sIsInv = null;

  protected Vector m_vColName = new Vector();

  private Vector m_vData = null;

  private UITablePane mTablePane1 = null;

  public PrAllocTabPanel()
  {
    initialize();
  }

  public void afterEdit(IATableSelectionEvent e)
  {
    int iRow = e.getRow();
    int iCol = e.getColumn();
    UFDouble dMny = null;
    UFDouble dPri = null;
    UFDouble dNum = null;

    if (this.m_UITable.isEditing()) {
      this.m_UITable.editingStopped(new ChangeEvent(this));
    }
    if (iRow != -1)
    {
      if ((this.m_TableModel.getValueAt(iRow, this.m_iMnyColumn) != null) && (this.m_TableModel.getValueAt(iRow, this.m_iMnyColumn).toString().trim().length() != 0))
      {
        dMny = new UFDouble(this.m_TableModel.getValueAt(iRow, this.m_iMnyColumn).toString().trim());
      }

      if ((this.m_TableModel.getValueAt(iRow, this.m_iPriceColumn) != null) && (this.m_TableModel.getValueAt(iRow, this.m_iPriceColumn).toString().trim().length() != 0))
      {
        dPri = new UFDouble(this.m_TableModel.getValueAt(iRow, this.m_iPriceColumn).toString().trim());
      }

      dNum = new UFDouble(this.m_TableModel.getValueAt(iRow, this.m_iNumColumn).toString().trim());

      if (iCol == this.m_iPriceColumn) {
        if (dPri != null) {
          if (dPri.doubleValue() < 0.0D) {
            ((PriceallocClientUI)getParent()).showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000006"));

            this.m_bError = true;
            return;
          }

          if (dMny != null) {
            UFDouble formPrice = dMny.div(dNum);
            formPrice = formPrice.setScale(-this.m_iaPrec[1], 4);

            if (!formPrice.equals(dPri)) {
              UFDouble mny = dNum.multiply(dPri);
              mny = mny.setScale(-this.m_iaPrec[2], 4);
              this.m_TableModel.setValueAt(mny, iRow, this.m_iMnyColumn);
            }
          }
          else {
            UFDouble mny = dNum.multiply(dPri);
            mny = mny.setScale(-this.m_iaPrec[2], 4);
            this.m_TableModel.setValueAt(mny, iRow, this.m_iMnyColumn);
          }

        }
        else
        {
          this.m_TableModel.setValueAt("", iRow, this.m_iMnyColumn);
        }
      }
      else if (iCol == this.m_iMnyColumn) {
        if (dMny != null) {
          if (dNum.doubleValue() != 0.0D) {
            UFDouble price = dMny.div(dNum);
            price = price.setScale(-this.m_iaPrec[1], 4);

            this.m_TableModel.setValueAt(price, iRow, this.m_iPriceColumn);
            if (price.doubleValue() < 0.0D) {
              ((PriceallocClientUI)getParent()).showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000006"));

              this.m_bError = true;
              return;
            }
          }
          else {
            this.m_TableModel.setValueAt("0", iRow, this.m_iMnyColumn);
            if (dPri == null) {
              ((PriceallocClientUI)getParent()).showHintMessage(NCLangRes.getInstance().getStrByID("20146020", "UPP20146020-000007"));

              this.m_bError = true;
              return;
            }
          }
        }
        else {
          this.m_TableModel.setValueAt("", iRow, this.m_iPriceColumn);
        }
      }
    }
    calculateTotalMny(this.m_vData);

    this.m_UITable.repaint();
    ((PriceallocClientUI)getParent()).showHintMessage("");
    this.m_bError = false;
    setInterfaceData(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000649"), -this.m_iaPrec[0]);

    setInterfaceData(NCLangRes.getInstance().getStrByID("common", "UC000-0001775"), -this.m_iaPrec[1]);

    setInterfaceData(NCLangRes.getInstance().getStrByID("common", "UC000-0001945"), -this.m_iaPrec[2]);
  }

  private UITablePane getUITablePane1()
  {
    if (this.mTablePane1 == null) {
      try {
        this.mTablePane1 = new UITablePane();
        this.mTablePane1.setName("UITablePane1");

        this.m_UITable = new IAUITable();
        this.m_TableModel = new IATableModel();
        this.m_UITable.setModel(this.m_TableModel);
        this.mTablePane1.setTable(this.m_UITable);
        mTablePane1.add(new JTextField("test"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.mTablePane1;
  }

  private void handleException(Throwable exception)
  {
    Log.error(exception);
  }

  public void IATableSelectionChanged(IATableSelectionEvent e)
  {
  }

  private void initialize()
  {
    try
    {
      setName("PrAllocTabPanel");
      setLayout(new CardLayout());
      setSize(774, 419);
      add(getUITablePane1(), getUITablePane1().getName());
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    this.m_UITable.setSelectionMode(0);
    this.m_UITable.addIATableSelectionListener(this);
  }

  public boolean iserror()
  {
    this.m_UITable.editCellAt(0, 2);
    if (this.m_UITable.isEditing()) {
      this.m_UITable.editingStopped(new ChangeEvent(this));
    }
    return this.m_bError;
  }

  private void setInterfaceData(String sColName, int iPrec)
  {
    UFDouble dTemp = null;
    UFDouble d = null;
    Object oTemp = null;
    String sTemp = null;
    int iColumnIndex = 0;
    int iCount = this.m_TableModel.getRowCount();
    for (int i = 0; i < iCount; i++) {
      iColumnIndex = this.m_UITable.getColumnModel().getColumnIndex(sColName);
      oTemp = this.m_TableModel.getValueAt(i, iColumnIndex);
      sTemp = "";
      if ((oTemp != null) && (oTemp.toString().length() != 0)) {
        dTemp = new UFDouble(oTemp.toString());
        d = dTemp.setScale(iPrec, 4);
        sTemp = d.toString();
      }
      this.m_TableModel.setValueAt(sTemp, i, iColumnIndex);
    }
  }

  public void display(Vector data, String sMode)
  {
    if (data == null)
    {
      Vector v = new Vector();

      this.m_TableModel.setDataVector(v);

      return;
    }

    this.m_TableModel.setEditableRowColumn(-99, -99);
    this.m_TableModel.setEditableRowColumn(-99, -99);

    this.m_sIsInv = sMode;
    if (this.m_sIsInv.equals("F"))
    {
      this.m_saInvPK = new String[data.size()];
      for (int i = 0; i < this.m_saInvPK.length; i++) {
        this.m_saInvPK[i] = ((Vector)data.elementAt(i)).elementAt(8).toString().trim();
      }

      this.m_vData = new Vector();
      Vector vTmp = new Vector();
      int iCount = data.size();
      for (int i = 0; i < iCount; i++) {
        vTmp.removeAllElements();
        vTmp = (Vector)((Vector)data.elementAt(i)).clone();
        vTmp.remove(8);
        this.m_vData.addElement(vTmp.clone());
      }
    }
    else
    {
      this.m_vData = data;
    }
    addTotalRow(this.m_vData);

    this.m_TableModel.setDataVector(this.m_vData);

    int rowSize = this.m_vData.size();
    int columnSize = ((Vector)this.m_vData.get(0)).size();

    int priceColumn = columnSize - 1;
    int mnyColumn = columnSize - 2;

    this.m_TableModel.setEditDisableRowColumn(rowSize - 1, priceColumn);
    this.m_TableModel.setEditDisableRowColumn(rowSize - 1, mnyColumn);

    UITextField t3 = new UITextField();
    t3.setTextType("TextDbl");
    t3.setMaxLength(16);
    t3.setNumPoint(this.m_iaPrec[0]);
    t3.setDelStr(" ");

    IATableCellEditor te = new IATableCellEditor(t3);
    te.setClickCountToStart(1);

    this.m_UITable.getColumnModel().getColumn(this.m_iNumColumn).setCellEditor(te);

    IATableCellRender tc3 = new IATableCellRender();
    tc3.setAlignMode(IATableCellRender.ALIGN_RIGHT);
    this.m_UITable.getColumnModel().getColumn(this.m_iNumColumn).setCellRenderer(tc3);
    setInterfaceData(NCLangRes.getInstance().getStrByID("2014", "UPP2014-000649"), -this.m_iaPrec[0]);

    UITextField t1 = new UITextField();
    t1.setTextType("TextDbl");
    t1.setMaxLength(13 + this.m_iaPrec[1]);
    t1.setNumPoint(this.m_iaPrec[1]);
    t1.setDelStr(" ");
    t1.setMaxValue(this.m_dMaxValue);

    IATableCellEditor te2 = new IATableCellEditor(t1);
    te2.setClickCountToStart(1);

    this.m_UITable.getColumnModel().getColumn(this.m_iPriceColumn).setCellEditor(te2);

    IATableCellRender tc1 = new IATableCellRender();
    tc1.setAlignMode(IATableCellRender.ALIGN_RIGHT);
    this.m_UITable.getColumnModel().getColumn(this.m_iPriceColumn).setCellRenderer(tc1);
    setInterfaceData(NCLangRes.getInstance().getStrByID("common", "UC000-0001775"), -this.m_iaPrec[1]);

    UITextField t2 = new UITextField();
    t2.setTextType("TextDbl");
    t2.setMaxLength(13 + this.m_iaPrec[2]);
    t2.setNumPoint(this.m_iaPrec[2]);
    t2.setDelStr(" ");
    t2.setMaxValue(this.m_dMaxValue);

    IATableCellEditor te3 = new IATableCellEditor(t2);
    te3.setClickCountToStart(1);

    this.m_UITable.getColumnModel().getColumn(this.m_iMnyColumn).setCellEditor(te3);

    IATableCellRender tc2 = new IATableCellRender();
    tc2.setAlignMode(IATableCellRender.ALIGN_RIGHT);
    this.m_UITable.getColumnModel().getColumn(this.m_iMnyColumn).setCellRenderer(tc2);
    setInterfaceData(NCLangRes.getInstance().getStrByID("common", "UC000-0001945"), -this.m_iaPrec[2]);
  }

  public Vector getdata()
  {
    Vector vResult = new Vector();
    Vector vTmp = new Vector();
    int iCount = this.m_vData.size();
    if (this.m_sIsInv.equals("F")) {
      for (int i = 0; i < iCount; i++) {
        vTmp = (Vector)((Vector)this.m_vData.elementAt(i)).clone();
        if (i < this.m_saInvPK.length) {
          vTmp.addElement(this.m_saInvPK[i]);
        }
        vResult.addElement(vTmp.clone());
      }
    }
    else {
      for (int i = 0; i < iCount; i++) {
        vTmp = (Vector)((Vector)this.m_vData.elementAt(i)).clone();
        vResult.addElement(vTmp.clone());
      }
    }
    return vResult;
  }

  protected void setEditDisable(int iCol1, int iCol2)
  {
    this.m_TableModel.setEditDisableRowColumn(-99, iCol1);
    this.m_TableModel.setEditDisableRowColumn(-99, iCol2);
  }

  protected void setEditEnable(int iCol1, int iCol2)
  {
    this.m_TableModel.setEditableRowColumn(-99, iCol1);
    this.m_TableModel.setEditableRowColumn(-99, iCol2);
  }

  public void setPrec(int[] newPrec)
  {
    this.m_iaPrec = newPrec;
  }

  private void addTotalRow(Vector vector) {
    Vector row = new Vector();
    Vector temp = (Vector)vector.get(0);
    int count = temp.size();

    UFDouble total = new UFDouble(0);
    UFDouble totalMny = new UFDouble(0);
    int size = vector.size();
    for (int i = 0; i < size; i++) {
      temp = (Vector)vector.get(i);
      UFDouble num = new UFDouble((String)temp.get(count - 3));
      total = total.add(num);

      if (temp.get(count - 2) != null) {
        UFDouble mny = new UFDouble((String)temp.get(count - 2));
        totalMny = totalMny.add(mny);
      }
    }
    for (int i = 0; i < count; i++) {
      row.add(null);
    }
    row.set(count - 3, total.toString());
    row.set(count - 2, totalMny.toString());
    String message = NCLangRes.getInstance().getStrByID("201490", "UPP201490-000024");

    row.set(0, message);
    vector.add(row);
  }

  public void removeTotal(Vector vector)
  {
    int size = vector.size();
    if (size > 1)
      vector.remove(size - 1);
  }

  private void calculateTotalMny(Vector vector)
  {
    int size = vector.size();
    Vector row = (Vector)vector.get(size - 1);
    int count = row.size();

    UFDouble total = new UFDouble(0);
    for (int i = 0; i < size - 1; i++) {
      Vector temp = (Vector)vector.get(i);
      Object value = temp.get(count - 2);
      UFDouble mny = null;
      if (value == null) {
        mny = new UFDouble("0");
      }
      else {
        mny = new UFDouble(value.toString());
      }
      total = total.add(mny);
    }
    if (total.doubleValue() > 0.0D) {
      row.set(count - 2, total.toString());
    }
    else
      row.set(count - 2, null);
  }
}