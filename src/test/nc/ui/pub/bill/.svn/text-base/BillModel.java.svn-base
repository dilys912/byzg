package nc.ui.pub.bill;

//import I;
import java.awt.Color;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.table.ISortableTableModel;
import nc.ui.pub.beans.util.MiscUtils;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.vo.jcom.util.SortUtils;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.SuperVOGetterSetter;
import nc.vo.pub.general.GeneralSuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class BillModel extends DefaultTableModel
  implements ISortableTableModel
{
  public static final int UNSTATE = -1;
  public static final int NORMAL = 0;
  public static final int ADD = 1;
  public static final int MODIFICATION = 2;
  public static final int DELETE = 3;
  public static final int SELECTED = 4;
  protected BillItem[] m_biBodyItems = null;

  protected Hashtable<String, Integer> htBodyItems = null;

  private Hashtable<String, BillItem> htBillItems = new Hashtable();

  private Vector vViewBodyCache = null;

  private Vector<Vector> vViewRowCopy = null;

  private Vector<Vector> vDeleteRow = null;

  protected DefaultTableModel m_tmlTotal = null;

  protected DefaultTableModel m_tmlRowNO = null;

  private FixRowNOModel fixRowHeaderModel = null;

  private UserRowNOModel userRowHeaderModel = null;

  private UserFixRowTableModel m_userFixRowModel = null;

  protected Vector<RowAttribute> vRowAttrib = new Vector();

  protected int fixCol = -1;

  protected BillTotalListener btl = null;

  private boolean isFirstSetBodyDataVO = false;
  protected FormulaParse m_formulaParse;
  protected boolean m_bRowEditState = false;

  protected Vector m_editRow = null;

  protected ArrayList<RowAttribute> aryNotEditRow = null;

  private boolean m_ascending = true;
  private int[] m_indexes;
  private int m_sortColumn = -1;

  protected EventListenerList sortRelaObjectListener = new EventListenerList();

  private IBillModelSortPrepareListener sortPrepareListener = null;

  protected BillSortListener bsl = null;

  protected EventListenerList afterSortListener = new EventListenerList();

  private int sorttype = -1;

  private boolean m_bEnabled = true;

  private boolean m_bNeedCalculate = false;

  private Hashtable<Cell, Color> hashCellBackColor = new Hashtable();

  private Hashtable<Cell, Color> hashCellForeColor = new Hashtable();

  private BillModelFormulaProcessor formulaProcessor = new BillModelFormulaProcessor(this);

  private BillScrollPane billScrollPane = null;

  private boolean changeTable = true;

  private BillModelCellEditableController cellEditableController = null;

  private String totalTitle = NCLangRes.getInstance().getStrByID("common", "UC000-0001146");

  private boolean ignoreScaleWhenSetValue = false;

  public BillModel()
  {
  }

  public BillModel(Object[][] data, Object[] columnNames)
  {
    super(data, columnNames);
  }

  public BillModel(Object[] columnNames, int numRows)
  {
    super(columnNames, numRows);
  }

  public BillModel(int numRows, int numColumns)
  {
    super(numRows, numColumns);
  }

  public BillModel(Vector columnNames, int numRows)
  {
    super(columnNames, numRows);
  }

  public BillModel(Vector data, Vector columnNames)
  {
    super(data, null);
  }

  public void addLine()
  {
    addLine(1);
  }

  void addLine(int count)
  {
    if ((this.m_biBodyItems == null) || (count <= 0))
      return;
    for (int j = 0; j < count; j++)
    {
      int lastRow = getRowCount() - 1;
      Vector vNewRow = creteRowVector(lastRow);

      this.vRowAttrib.addElement(createRowAttribute());

      addRow(vNewRow);

      vNewRow = new Vector();
      vNewRow.add(null);
      getRowNOTableModel().addRow(vNewRow);
    }

    fireTableChanged(new TableModelEvent(this));
  }

  private Vector<Object> creteRowVector(int row)
  {
    Vector vNewRow = new Vector(this.m_biBodyItems.length);
    for (int i = 0; i < this.m_biBodyItems.length; i++) {
      Object value = null;
      if ((row > -1) && (this.m_biBodyItems[i].isLock())) {
        value = getValueAt(row, i);
      } else if (this.m_biBodyItems[i].getDefaultValue() != null) {
        value = this.m_biBodyItems[i].getDefaultValue();
        if (value != null) {
          switch (this.m_biBodyItems[i].getDataType()) {
          case 4:
            if ((value.toString().equalsIgnoreCase("true")) || (value.toString().equalsIgnoreCase("Y")) || (value.toString().equals("1")))
            {
              value = UFBoolean.TRUE;
            }
            else value = UFBoolean.FALSE;

            break;
          case 1:
            value = new Integer(value.toString());
            break;
          case 2:
            value = new UFDouble(value.toString());
            value = ((UFDouble)value).setScale(0 - this.m_biBodyItems[i].getDecimalDigits(), 4);

            break;
          case 3:
          }
        }
      }

      vNewRow.add(value);
    }

    return vNewRow;
  }

  private RowAttribute createRowAttribute() {
    RowAttribute ra = new RowAttribute();

    return ra;
  }

  public void addSortListener(BillSortListener bsl)
  {
    this.bsl = bsl;
  }

  public void addTotalListener(BillTotalListener btl)
  {
    this.btl = btl;
  }

  public void addDecimalListener(IBillModelDecimalListener bdl)
  {
    if ((bdl instanceof IBillModelDecimalListener2)) {
      String[] targets = ((IBillModelDecimalListener2)bdl).getTarget();
      if ((targets != null) && (targets.length > 0))
        for (int i = 0; i < targets.length; i++)
          getItemByKey(targets[i]).addDecimalListener(bdl);
    }
    else
    {
      BillItem[] items = getBodyItems();
      for (int i = 0; i < items.length; i++)
        if (bdl.isTarget(items[i]))
          items[i].addDecimalListener(bdl);
    }
  }

  private void setItemDecimal(CircularlyAccessibleValueObject bodyRowVO, int row, BillItem item)
  {
    IBillModelDecimalListener decimalListener = null;
    if ((decimalListener = item.getDecimalListener()) != null) {
      String source = decimalListener.getSource();
      Object pkValue = bodyRowVO.getAttributeValue(source);
      if (pkValue != null)
        item.setDecimalDigits(decimalListener.getDecimalFromSource(row, pkValue));
    }
  }

  /** @deprecated */
  protected void allocate()
  {
    this.m_indexes = new int[getRowCount()];
    for (int i = 0; i < this.m_indexes.length; i++)
      this.m_indexes[i] = i;
  }

  public void clearBodyData()
  {
    this.dataVector = new Vector();

    getRowNOTableModel().setNumRows(0);

    this.vRowAttrib = new Vector();

    clearCellEdit();

    this.m_indexes = null;

    clearTotalModel();

    setNotEditAllowedRows(null);

    if (this.vDeleteRow != null)
      this.vDeleteRow.removeAllElements();
    if (this.vViewRowCopy != null) {
      this.vViewRowCopy.removeAllElements();
    }

    fireTableChanged(new TableModelEvent(this));
  }

  void clearCellColor(String type)
  {
    if ("all".equals(type)) {
      this.hashCellBackColor.clear();
      this.hashCellForeColor.clear();
    } else if ("back".equals(type)) {
      this.hashCellBackColor.clear();
    } else {
      this.hashCellForeColor.clear();
    }
  }

  public void clearCellEdit()
  {
    for (int i = 0; i < this.dataVector.size(); i++)
      getRowAttribute(i).clearCellEdit();
  }

  public void clearRowData(int row, String[] keys)
  {
    if ((row < 0) || (row >= getRowCount()))
      return;
    if (keys == null) {
      for (int i = 0; i < getColumnCount(); i++)
        setValueAt("", row, i);
    }
    else
      for (int i = 0; i < keys.length; i++)
        setValueAt("", row, keys[i]);
  }

  public void clearTotalModel()
  {
    if (this.m_tmlTotal != null)
      for (int i = 0; i < this.m_tmlTotal.getColumnCount(); i++)
      {
        this.m_tmlTotal.setValueAt(null, 0, i);
      }
  }

  /** @deprecated */
  public int convertIntoModelRow(int row)
  {
    if ((row < 0) || (row >= getRowCount())) {
      return -1;
    }

    return row;
  }

  /** @deprecated */
  public int convertIntoViewRow(int row)
  {
    if ((row < 0) || (row >= getRowCount())) {
      return -1;
    }

    return row;
  }

  public void copyLine(int[] row)
  {
    if (this.vViewRowCopy == null)
      this.vViewRowCopy = new Vector();
    this.vViewRowCopy.removeAllElements();
    for (int i = 0; i < row.length; i++)
      if ((row[i] > -1) && (row[i] < this.dataVector.size())) {
        Vector vRow = (Vector)this.dataVector.elementAt(row[i]);

        this.vViewRowCopy.add(vRow);
      }
  }

  protected FormulaParse createDefaultFormulaParse()
  {
    return new FormulaParse();
  }

  protected DefaultTableModel createDefaultRowNumberModel()
  {
    return new RowNumberModel();
  }

  protected DefaultTableModel createDefaultTotalTableModel()
  {
    return new TotalTableModel();
  }

  private UFDouble createUFDouble(Object o) {
    if ((o instanceof UFDouble))
      return (UFDouble)o;
    String v;
//    String v;
    if ((o == null) || ((v = o.toString().trim()).length() == 0))
      v = "0";
    return new UFDouble(v);
  }

  public void delLine(int[] array)
  {
    if (this.vDeleteRow == null) {
      this.vDeleteRow = new Vector();
    }

    int[] row = (int[])(int[])array.clone();

    Arrays.sort(row);
    for (int i = row.length - 1; i >= 0; i--) {
      if ((getRowStateModel(row[i]) == 0) || (getRowStateModel(row[i]) == 2))
      {
        this.vDeleteRow.add((Vector)this.dataVector.elementAt(row[i]));
      }

      removeCellColors(row[i], "all");
      removeRow(row[i]);

      getRowNOTableModel().removeRow(row[i]);

      this.vRowAttrib.remove(row[i]);
    }

    reCalcurateAll();
    fireTableChanged(new TableModelEvent(this));
  }

  /** @deprecated */
  public void execEditFormula(int row, int column)
  {
    column = getBodyColByCol(column);
    execEditFormulaByModelColumn(row, column);
  }

  public void execEditFormulaByKey(int row, String key)
  {
    execEditFormulaByModelColumn(row, getBodyColByKey(key));
  }

  void execEditFormulaByModelColumn(int row, int column)
  {
    this.formulaProcessor.execEditFormulaByModelColumn(row, column);
  }

  /** @deprecated */
  public void execEditFormulas(int row, int column)
  {
    column = getBodyColByCol(column);
    execEditFormulasByModelColumn(row, column);
  }

  public void execEditFormulasByKey(int row, String key)
  {
    execEditFormulasByModelColumn(row, getBodyColByKey(key));
  }

  void execEditFormulasByModelColumn(int row, int column)
  {
    this.formulaProcessor.execEditFormulasByModelColumn(row, column);
  }

  public void execFormula(int row, String[] formulas)
  {
    this.formulaProcessor.execFormula(row, formulas);
  }

  public void execFormulas(String[] formulas)
  {
    execFormulas(formulas, -1, -1);
  }

  public void execFormulas(String[] formulas, int beginRow, int endRow)
  {
    this.formulaProcessor.execFormulas(formulas, beginRow, endRow, false);
  }

  public void execEditFormulas(int row) {
    this.formulaProcessor.execEditFormulas(row, row, false);
  }

  public void execFormulas(int row, String[] formulas)
  {
    this.formulaProcessor.execFormulas(row, formulas);
  }

  public void execFormulasWithVO(CircularlyAccessibleValueObject[] VOs, String[] formulas)
  {
    this.formulaProcessor.execFormulasWithVO(VOs, formulas);
  }

  public void execLoadFormula()
  {
    if (getRowCount() > 0)
      this.formulaProcessor.execLoadFormula();
  }

  /** @deprecated */
  public void execLoadFormula(int column)
  {
    column = getBodyColByCol(column);
    this.formulaProcessor.execLoadFormula(column);
  }

  public void execLoadFormulaByKey(String key)
  {
    this.formulaProcessor.execLoadFormula(getBodyColByKey(key));
  }

  /** @deprecated */
  public void execLoadFormulas(int column)
  {
    column = getBodyColByCol(column);
    this.formulaProcessor.execLoadFormulas(column);
  }

  public void execLoadFormulasByKey(String key)
  {
    this.formulaProcessor.execLoadFormulas(getBodyColByKey(key));
  }

  public boolean execValidateForumlas(String[] formulas, String[] itemkeys, int[] rows)
  {
    return getFormulaProcessor().execValidateForumlas(formulas, itemkeys, rows);
  }

  public Vector getBillModelData()
  {
    Vector data = new Vector();

    data.addElement(this.vRowAttrib);

    data.addElement(this.dataVector);
    return data;
  }

  BillScrollPane getBillScrollPane()
  {
    return this.billScrollPane;
  }

  /** @deprecated */
  public int getBodyColByCol(int col)
  {
    int n = -1;

    if (this.fixCol > -1) {
      col += this.fixCol;
    }
    for (int i = 0; i < getBodyItems().length; i++) {
      BillItem item = getBodyItems()[i];
      if (item.isShow())
        n++;
      if (n == col)
        return i;
    }
    Logger.info("没有找到" + col + "列对应实际列.");
    return -1;
  }

  /** @deprecated */
  public int getBodyColByIndex(int index)
  {
    int n = 0;
    for (int i = 0; i <= index; i++) {
      BillItem item = getBodyItems()[i];
      if (item.isShow())
        n++;
    }
    if (n > 0)
      return n + this.fixCol;
    return -1;
  }

  public int getBodyColByKey(String strKey)
  {
    if (strKey == null)
      return -1;
    try {
      if ((this.htBodyItems != null) && (this.htBodyItems.containsKey(strKey = strKey.trim())))
      {
        return ((Integer)this.htBodyItems.get(strKey)).intValue();
      }
    } catch (Exception e) {
      Logger.debug(e.getMessage());
    }
    Logger.info("没有找到" + strKey + "对应关键字列.");
    return -1;
  }

  public BillItem[] getBodyItems()
  {
    return this.m_biBodyItems;
  }

  /** @deprecated */
  public String getBodyKeyByCol(int col)
  {
    String key = null;
    int n = -1;

    if (this.fixCol > -1) {
      col += this.fixCol;
    }
    for (int i = 0; i < getBodyItems().length; i++) {
      BillItem item = getBodyItems()[i];
      if (item.isShow())
        n++;
      if (n == col)
        return item.getKey();
    }
    System.out.println("没有找到" + col + "列对应实际列.");
    return key;
  }

  public CircularlyAccessibleValueObject[] getBodySelectedVOs(String bodyVOName)
  {
    return getBodySelectedVOs(bodyVOName, -1);
  }

  public CircularlyAccessibleValueObject[] getBodySelectedVOs(String bodyVOName, int count)
  {
    try
    {
      Class bodyVOClass = Class.forName(bodyVOName);
      Vector vBodyVOs = getSelectedVector(bodyVOName, count, false)[0];
      if (vBodyVOs != null) {
        CircularlyAccessibleValueObject[] bodyVOs = (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])Array.newInstance(bodyVOClass, vBodyVOs.size());

        vBodyVOs.copyInto(bodyVOs);
        return bodyVOs;
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace(System.out);
    }
    return null;
  }

  ArrayList getBodySelectedVOsAndRowNos(String bodyVOName, int count, boolean hasRowNumber)
  {
    Vector[] vecs = getSelectedVector(bodyVOName, count, hasRowNumber);
    if ((vecs == null) || (vecs.length == 0))
      return null;
    Vector vecBody = vecs[0];
    Vector vecNo = vecs[1];
    if (vecBody == null)
      return null;
    int allCount = vecBody.size();
    ArrayList list = new ArrayList(allCount);
    for (int i = 0; i < allCount; i++) {
      list.add(new Object[] { vecBody.elementAt(i), vecNo.elementAt(i) });
    }
    return list;
  }

  public CircularlyAccessibleValueObject[] getBodyValueChangeVOs(String bodyVOName)
  {
    try
    {
      Class bodyVOClass = Class.forName(bodyVOName);
      Vector vBodyVOs = getValueChangeVector(bodyVOName);
      if (vBodyVOs != null) {
        CircularlyAccessibleValueObject[] bodyVOs = (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])Array.newInstance(bodyVOClass, vBodyVOs.size());

        vBodyVOs.copyInto(bodyVOs);
        return bodyVOs;
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace(System.out);
    }
    return null;
  }

  public GeneralSuperVO[] getBodyValueChangeVOs(GeneralSuperVO initVO)
  {
    try
    {
      Vector vBodyVOs = getValueChangeVector(initVO);
      if (vBodyVOs != null) {
        GeneralSuperVO[] bodyVOs = GeneralSuperVO.createVOArray(initVO, vBodyVOs.size(), true);

        vBodyVOs.copyInto(bodyVOs);
        return bodyVOs;
      }
    } catch (Throwable ex) {
      ex.printStackTrace(System.out);
    }
    return null;
  }

  public CircularlyAccessibleValueObject getBodyValueRowVO(int row, String bodyVOName)
  {
    try
    {
      CircularlyAccessibleValueObject bodyRowVO = (CircularlyAccessibleValueObject)Class.forName(bodyVOName).newInstance();

      for (int j = 0; j < getBodyItems().length; j++) {
        BillItem item = getBodyItems()[j];
        Object aValue = getValueAt(row, j);
        aValue = item.converType(aValue);
        bodyRowVO.setAttributeValue(item.getKey(), aValue);
      }

      switch (getRowState(row)) {
      case 1:
        bodyRowVO.setStatus(2);
        break;
      case 2:
        bodyRowVO.setStatus(1);
      }

      return bodyRowVO;
    } catch (ClassNotFoundException e) {
      e.printStackTrace(System.out);
    } catch (InstantiationException e) {
      e.printStackTrace(System.out);
    } catch (IllegalAccessException e) {
      e.printStackTrace(System.out);
    }
    return null;
  }

  public void getBodyValueVOs(CircularlyAccessibleValueObject[] bodyVOs) {
    for (int i = 0; (i < bodyVOs.length) && 
      (i <= getRowCount()); i++)
    {
      for (int j = 0; j < getBodyItems().length; j++) {
        BillItem item = getBodyItems()[j];
        Object aValue = getValueAt(i, j);
        aValue = item.converType(aValue);
        bodyVOs[i].setAttributeValue(item.getKey(), aValue);
      }

      switch (getRowState(i)) {
      case 1:
        bodyVOs[i].setStatus(2);
        break;
      case 2:
        bodyVOs[i].setStatus(1);
      }
    }
  }

  public CircularlyAccessibleValueObject[] getBodyValueVOs(String bodyVOName)
  {
    try
    {
      Class bodyVOClass = Class.forName(bodyVOName);
      int length = this.dataVector.size();
      CircularlyAccessibleValueObject[] bodyVOs = (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])Array.newInstance(bodyVOClass, length);

      for (int i = 0; i < this.dataVector.size(); i++) {
        bodyVOs[i] = ((CircularlyAccessibleValueObject)Class.forName(bodyVOName).newInstance());

        BillItem[] items = getBodyItems();

        for (int j = 0; j < items.length; j++) {
          BillItem item = items[j];
          Object aValue = getValueAt(i, j);
          aValue = item.converType(aValue);
          bodyVOs[i].setAttributeValue(item.getKey(), aValue);
        }

        switch (getRowState(i)) {
        case 1:
          bodyVOs[i].setStatus(2);
          break;
        case 2:
          bodyVOs[i].setStatus(1);
        }
      }

      return bodyVOs;
    } catch (ClassNotFoundException e) {
      e.printStackTrace(System.out);
    } catch (InstantiationException e) {
      e.printStackTrace(System.out);
    } catch (IllegalAccessException e) {
      e.printStackTrace(System.out);
    }
    return null;
  }

  Hashtable[] getCellColor(int[] rows, int[] cols)
  {
    if ((rows == null) || (cols == null) || (rows.length == 0) || (cols.length == 0))
    {
      return null;
    }if ((this.hashCellBackColor.size() == 0) && (this.hashCellForeColor.size() == 0))
      return null;
    Hashtable[] hashColors = { new Hashtable(), new Hashtable() };

    for (int i = 0; i < rows.length; i++) {
      int row = rows[i];
      for (int j = 0; j < cols.length; j++) {
        Cell cc = new Cell(row, cols[j]);
        Color color;
        if ((color = (Color)this.hashCellBackColor.get(cc)) != null)
          hashColors[0].put(new Cell(i, j), color);
        if ((color = (Color)this.hashCellForeColor.get(cc)) != null)
          hashColors[1].put(new Cell(i, j), color);
      }
    }
    if ((hashColors[0].size() == 0) && (hashColors[1].size() == 0))
      return null;
    return hashColors;
  }

  public Class<?> getColumnClass(int columnIndex)
  {
    if (getBodyItems() == null)
      return super.getColumnClass(columnIndex);
    switch (getBodyItems()[columnIndex].getDataType()) {
    case 4:
      return Boolean.class;
    }
    return String.class;
  }

  public int getColumnCount()
  {
    if (getBodyItems() == null)
      return super.getColumnCount();
    return getBodyItems().length;
  }

  public String getColumnName(int col)
  {
    if (getBodyItems() == null)
      return super.getColumnName(col);
    return getBodyItems()[col].getName();
  }

  public Vector getDeleteRow()
  {
    return this.vDeleteRow;
  }

  public int getEditRow()
  {
    if ((this.m_editRow == null) || (!this.dataVector.contains(this.m_editRow)))
      return -1;
    return this.dataVector.indexOf(this.m_editRow);
  }

  public FormulaParse getFormulaParse()
  {
    if (this.m_formulaParse == null) {
      this.m_formulaParse = createDefaultFormulaParse();
    }
    return this.m_formulaParse;
  }

  BillModelFormulaProcessor getFormulaProcessor()
  {
    return this.formulaProcessor;
  }

  public BillItem getItemByKey(String key)
  {
    return (BillItem)this.htBillItems.get(key);
  }

  public int getItemIndex(Object itemOrKey)
  {
    if (itemOrKey == null)
      throw new NullPointerException("itemOrkey is null.");
    BillItem[] items = getBodyItems();
    if (items == null)
      return -1;
    if ((itemOrKey instanceof String)) {
      for (int i = 0; i < items.length; i++)
        if (itemOrKey.equals(items[i].getKey()))
          return i;
    }
    else if ((itemOrKey instanceof BillItem)) {
      BillItem item = (BillItem)itemOrKey;
      for (int i = 0; i < items.length; i++) {
        if (item.getKey().equals(items[i].getKey()))
          return i;
      }
    }
    throw new IllegalArgumentException("itemOrKey not found!");
  }

  public int getPasteLineNumer() {
    if (this.vViewRowCopy == null)
      return 0;
    return this.vViewRowCopy.size();
  }

  public boolean getRowEditState()
  {
    return this.m_bRowEditState;
  }

  public DefaultTableModel getRowNOTableModel()
  {
    if (this.m_tmlRowNO == null) {
      this.m_tmlRowNO = createDefaultRowNumberModel();
    }
    return this.m_tmlRowNO;
  }

  public int getRowState(int row)
  {
    return getRowStateModel(row);
  }

  private int getRowStateModel(int row)
  {
    RowAttribute ra = getRowAttribute(row);

    if (ra == null) {
      return -1;
    }
    return ra.getRowState();
  }

  public RowAttribute getRowAttribute(int row)
  {
    if (this.dataVector == null)
      return null;
    if (this.dataVector.size() <= row) {
      return null;
    }
    return (RowAttribute)this.vRowAttrib.get(row);
  }

  public void addRowAttributeObject(int row, String key, Object o) {
    RowAttribute ra = getRowAttribute(row);
    if (ra != null)
      ra.addAttributeObject(key, o);
  }

  public Object getRowAttributeObject(int row, String key)
  {
    RowAttribute ra = getRowAttribute(row);
    if (ra != null) {
      return ra.getAttributeObject(key);
    }
    return null;
  }

  public void removeRowAttributeObject(int row, String key) {
    RowAttribute ra = getRowAttribute(row);
    if (ra != null)
      ra.removeAttributeObject(key);
  }

  protected Vector getSelectedVector(String bodyVOName)
  {
    return getSelectedVector(bodyVOName, -1, false)[0];
  }

  private Vector[] getSelectedVector(String bodyVOName, int number, boolean hasRowNumber)
  {
    Vector bodyVOs = new Vector();
    Vector rowNumber = hasRowNumber ? new Vector() : null;
    try {
      for (int i = 0; i < this.dataVector.size(); i++)
        if (getRowState(i) == 4) {
          CircularlyAccessibleValueObject bodyVO = (CircularlyAccessibleValueObject)Class.forName(bodyVOName).newInstance();

          for (int j = 0; j < getBodyItems().length; j++) {
            BillItem item = getBodyItems()[j];
            Object aValue = getValueAtModel(i, j);
            aValue = item.converType(aValue);
            bodyVO.setAttributeValue(item.getKey(), aValue);
          }
          bodyVOs.add(bodyVO);
          if (hasRowNumber)
            rowNumber.addElement(new Integer(i));
          if ((number != -1) && (bodyVOs.size() == number))
            return new Vector[] { bodyVOs, rowNumber };
        }
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace(System.out);
    } catch (InstantiationException e) {
      e.printStackTrace(System.out);
    } catch (IllegalAccessException e) {
      e.printStackTrace(System.out);
    }
    return new Vector[] { bodyVOs, rowNumber };
  }

  public int getSortColumn()
  {
    return this.m_sortColumn;
  }

  public String getSortColumnKey()
  {
    String itemkey = null;
    if (this.m_sortColumn > 0)
      itemkey = getBodyItems()[this.m_sortColumn].getKey();
    return itemkey;
  }

  /** @deprecated */
  public int[] getSortIndex()
  {
    if ((this.dataVector != null) && (
      (this.m_indexes == null) || (this.m_indexes.length != this.dataVector.size()))) {
      allocate();
    }

    return this.m_indexes;
  }

  public IBillModelSortPrepareListener getSortPrepareListener()
  {
    return this.sortPrepareListener;
  }

  public DefaultTableModel getTotalTableModel()
  {
    if (this.m_tmlTotal == null) {
      this.m_tmlTotal = createDefaultTotalTableModel();
    }
    if (this.m_biBodyItems != null)
      this.m_tmlTotal.setNumRows(1);
    return this.m_tmlTotal;
  }

  public Object getValueAt(int row, int column)
  {
    return getValueAtModel(row, column);
  }

  public Object getValueAt(int rowIndex, String strKey)
  {
    int colIndex = getBodyColByKey(strKey);
    return getValueAt(rowIndex, colIndex);
  }

  Object getValueAtModel(int row, int column)
  {
    Vector rowVector = getRowVectorAtModel(row);
    if ((column >= 0) && (rowVector != null) && (rowVector.size() > column)) {
      Object aValue = rowVector.elementAt(column);

      if ((getBodyItems()[column].getDataType() == 4) && 
        (aValue != null) && ((aValue instanceof UFBoolean))) {
        aValue = Boolean.valueOf(((UFBoolean)aValue).booleanValue());
      }
      if ((aValue instanceof String)) {
        String sv = ((String)aValue).trim();
        if (sv.length() == 0) {
          aValue = null;
        }
      }
      return aValue;
    }
    return null;
  }

  Object getValueAtModel(int rowIndex, String strKey)
  {
    int colIndex = getBodyColByKey(strKey);
    return getValueAtModel(rowIndex, colIndex);
  }

  protected Vector getValueChangeVector(String bodyVOName)
  {
    Vector bodyVOs = new Vector();
    try {
      if (this.vDeleteRow != null) {
        for (int i = 0; i < this.vDeleteRow.size(); i++) {
          CircularlyAccessibleValueObject bodyVO = (CircularlyAccessibleValueObject)Class.forName(bodyVOName).newInstance();

          Vector rowVector = (Vector)this.vDeleteRow.elementAt(i);
          for (int j = 0; j < getBodyItems().length; j++) {
            BillItem item = getBodyItems()[j];

            Object aValue = rowVector.elementAt(j);
            aValue = item.converType(aValue);
            bodyVO.setAttributeValue(item.getKey(), aValue);
          }

          bodyVO.setStatus(3);
          bodyVOs.add(bodyVO);
        }
      }
      for (int i = 0; i < this.dataVector.size(); i++)
        if (getRowState(i) != 0) {
          CircularlyAccessibleValueObject bodyVO = (CircularlyAccessibleValueObject)Class.forName(bodyVOName).newInstance();

          for (int j = 0; j < getBodyItems().length; j++) {
            BillItem item = getBodyItems()[j];

            Object aValue = getValueAt(i, j);
            aValue = item.converType(aValue);
            bodyVO.setAttributeValue(item.getKey(), aValue);
          }

          switch (getRowState(i)) {
          case 1:
            bodyVO.setStatus(2);
            break;
          case 2:
            bodyVO.setStatus(1);
          }

          bodyVOs.add(bodyVO);
        }
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace(System.out);
    } catch (InstantiationException e) {
      e.printStackTrace(System.out);
    } catch (IllegalAccessException e) {
      e.printStackTrace(System.out);
    }
    return bodyVOs;
  }

  protected Vector getValueChangeVector(GeneralSuperVO initVO)
  {
    Vector bodyVOs = new Vector();
    if (this.vDeleteRow != null) {
      for (int i = 0; i < this.vDeleteRow.size(); i++) {
        GeneralSuperVO bodyVO = GeneralSuperVO.createVOArray(initVO, 1, false)[0];

        Vector rowVector = (Vector)this.vDeleteRow.elementAt(i);
        for (int j = 0; j < getBodyItems().length; j++) {
          BillItem item = getBodyItems()[j];
          Object aValue = rowVector.elementAt(j);
          aValue = item.converType(aValue);
          bodyVO.setAttributeValue(item.getKey(), aValue);
        }

        bodyVO.setStatus(3);
        bodyVOs.add(bodyVO);
      }
    }
    for (int i = 0; i < this.dataVector.size(); i++) {
      if (getRowState(i) != 0) {
        GeneralSuperVO bodyVO = GeneralSuperVO.createVOArray(initVO, 1, false)[0];

        for (int j = 0; j < getBodyItems().length; j++) {
          BillItem item = getBodyItems()[j];
          Object aValue = getValueAt(i, j);
          aValue = item.converType(aValue);
          bodyVO.setAttributeValue(item.getKey(), aValue);
        }

        switch (getRowState(i)) {
        case 1:
          bodyVO.setStatus(2);
          break;
        case 2:
          bodyVO.setStatus(1);
        }

        bodyVOs.add(bodyVO);
      }
    }
    return bodyVOs;
  }

  public void insertRow(int row)
  {
    insertRowModel(row);
  }

  private void insertRowModel(int row)
  {
    if (row < 0)
      return;
    Vector vNewRow = creteRowVector(row);

    insertRow(row, vNewRow);
    vNewRow = new Vector();
    vNewRow.add(null);
    getRowNOTableModel().insertRow(row, vNewRow);

    this.vRowAttrib.insertElementAt(createRowAttribute(), row);

    fireTableChanged(new TableModelEvent(this));
  }

  public boolean isCellEditable(int row, int col)
  {
    boolean editable = isCellEditableModel(row, col);

    BillModelCellEditableController editableController = getCellEditableController();
    if (editableController != null) {
      editable = editableController.isCellEditable(editable, row, getBodyItems()[col].getKey());
    }

    return editable;
  }

  private boolean isCellEditableModel(int row, int col)
  {
    if ((row < 0) || (col < 0)) {
      return false;
    }

    if (!this.m_bEnabled) {
      return false;
    }

    BillItem item = getBodyItems()[col];

    Boolean cellEdit = getRowAttribute(row).isCellEdit(item.getKey());
    if (cellEdit != null) {
      return cellEdit.booleanValue();
    }
    if (!item.isEnabled()) {
      return false;
    }
    boolean rowedit = true;

    if (getRowEditState()) {
      if (getEditRow() != -1) {
        if (getEditRow() != row)
          rowedit = false;
      }
      else rowedit = !isNotEditRowModel(row);
    }

    return rowedit;
  }

  public boolean isHasSelectRow()
  {
    int i = 0; for (int j = getRowCount(); i < j; i++) {
      if (getRowStateModel(i) == 4)
        return true;
    }
    return false;
  }

  public boolean isNeedCalculate()
  {
    return this.m_bNeedCalculate;
  }

  protected boolean isNotEditRow(int row)
  {
    return isNotEditRowModel(row);
  }

  protected boolean isNotEditRowModel(int row) {
    if ((row > -1) && (row < getRowCount())) {
      return !getRowAttribute(row).isEdit();
    }

    return false;
  }

  public void pasteLine(int row)
  {
    pasteLineModel(row);
  }

  private void pasteLineModel(int row)
  {
    if (this.vViewRowCopy == null)
      return;
    if (row < 0) {
      return;
    }
    int[] rows = new int[this.vViewRowCopy.size()];
    for (int i = 0; i < this.vViewRowCopy.size(); i++) {
      Vector vRow = transferBodyRowData((Vector)this.vViewRowCopy.elementAt(i));

      insertRow(row + i, vRow);
      rows[i] = (row + i);

      vRow = new Vector();
      vRow.add(null);
      getRowNOTableModel().insertRow(row, vRow);

      this.vRowAttrib.insertElementAt(createRowAttribute(), row + i);
    }
    reCalcurateAll();

    fireTableChanged(new TableModelEvent(this));
  }

  void pasteLineToTail()
  {
    pasteLineModel(getRowCount());
  }

  public void reCalcurate(int column)
  {
    if (isNeedCalculate()) {
      UFDouble total = new UFDouble(0.0D);
      UFDouble aValue = null;
      BillItem item = getBodyItems()[column];
      if (this.btl == null)
        for (int i = 0; i < this.dataVector.size(); i++) {
          Object o = getValueAtModel(i, column);
          total = total.add(createUFDouble(o));
        }
      else {
        total = this.btl.calcurateTotal(item.getKey());
      }
      if (total != null) {
        int digit = 0;
        if (item.getDataType() == 2) {
          digit = 0 - item.getDecimalDigits();
        }
        aValue = total.setScale(digit, 4);
      }
      if (this.m_tmlTotal != null)
        this.m_tmlTotal.setValueAt(aValue, 0, column);
    }
  }

  public void reCalcurateAll()
  {
    if ((!isNeedCalculate()) || (getBodyItems() == null))
      return;
    for (int column = 0; column < getBodyItems().length; column++) {
      if ((!getBodyItems()[column].isTotal()) || ((getBodyItems()[column].getDataType() != 1) && (getBodyItems()[column].getDataType() != 2))) {
        continue;
      }
      reCalcurate(column);
    }
  }

  void removeCellColors(int row, String type)
  {
    if ("all".equals(type)) {
      removeCellColors(row, "back");
      removeCellColors(row, "fore");
    }
    Hashtable hash = "back".equals(type) ? this.hashCellBackColor : this.hashCellForeColor;

    int colcount = getColumnCount();
    for (int i = 0; i < colcount; i++)
      hash.remove(new Cell(row, i));
  }

  public void removeTotalListener()
  {
    this.btl = null;
  }

  public void removeTotalListener(BillTotalListener btl)
  {
    this.btl = null;
  }

  public void removeDecimalListener()
  {
    BillItem[] items = getBodyItems();
    for (int i = 0; i < items.length; i++)
      items[i].removeDecimalListener();
  }

  public void resumeValue()
  {
    this.dataVector = transferBodyData(this.vViewBodyCache);
    getRowNOTableModel().setNumRows(this.dataVector.size());
    int size = this.dataVector.size();

    this.vRowAttrib = new Vector(size);
    for (int i = 0; i < size; i++) {
      RowAttribute ra = createRowAttribute();
      ra.setRowState(0);
      this.vRowAttrib.addElement(ra);
    }
    this.vDeleteRow = null;
    reCalcurateAll();
  }

  public void setBillModelData(Vector vData)
  {
    if (vData == null)
      return;
    if (vData.size() < 2) {
      return;
    }
    this.vRowAttrib = ((Vector)vData.elementAt(0));
    this.dataVector = ((Vector)vData.elementAt(1));

    setSortIndex(null);
    getRowNOTableModel().setNumRows(this.dataVector.size());
    newRowsAdded(new TableModelEvent(this, 0, getRowCount() - 1, -1, 1));
  }

  void setBillScrollPane(BillScrollPane billScrollPane)
  {
    this.billScrollPane = billScrollPane;
  }

  public void setBodyDataVO(CircularlyAccessibleValueObject[] bodyVOs)
  {
    this.isFirstSetBodyDataVO = true;
    clearBodyData();
    if ((bodyVOs == null) || (bodyVOs.length == 0))
      return;
    boolean needCalculate = isNeedCalculate();
    setNeedCalculate(false);
    setChangeTable(false);
    if (((bodyVOs[0] instanceof SuperVO)) && (!(bodyVOs[0] instanceof GeneralSuperVO)))
    {
      BillItem[] items = getBodyItems();
      String[] fieldnames = new String[items.length];
      for (int i = 0; i < fieldnames.length; i++)
        fieldnames[i] = items[i].getKey();
      Method[] mts = SuperVOGetterSetter.getGetMethods(bodyVOs[0].getClass(), fieldnames);
      try
      {
        this.dataVector.ensureCapacity(bodyVOs.length);
        for (int i = 0; i < bodyVOs.length; i++) {
          addLine();
          SuperVO bodyRowVO = (SuperVO)bodyVOs[i];
          for (int ii = 0; ii < items.length; ii++) {
            BillItem item = items[ii];
            Object aValue;
 //           Object aValue;
            if (mts[ii] != null) {
              aValue = bodyRowVO.invokeGetMethod(mts[ii], fieldnames[ii]);
            }
            else {
              aValue = bodyRowVO.getAttributeValue(fieldnames[ii]);
            }

            if (item.getDataType() == 2) {
              setItemDecimal(bodyRowVO, i, item);
            }
            setValueAtModel(aValue, i, ii);
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    } else {
      for (int i = 0; i < bodyVOs.length; i++) {
        addLine();
        setBodyRowVOModel(bodyVOs[i], i);
      }
    }
    setNeedCalculate(needCalculate);

    if (getSortColumn() > 0) {
      sortByColumn(getSortColumn(), this.m_ascending);
    }

    setChangeTable(true);
    fireTableChanged(new TableModelEvent(this));
    this.isFirstSetBodyDataVO = false;
  }

  public void setBodyItems(BillItem[] newItems)
  {
    this.m_biBodyItems = newItems;
    this.htBodyItems = new Hashtable();
    this.htBillItems.clear();
    setSortColumn(-1);
    if (newItems != null) {
      for (int i = 0; i < newItems.length; i++) {
        this.htBodyItems.put(newItems[i].getKey(), new Integer(i));
        this.htBillItems.put(newItems[i].getKey(), newItems[i]);
      }
    }
    if (this.m_tmlTotal != null) {
      setTotalTableModel(null);
      getTotalTableModel();
    }
  }

  public void setBodyRowVO(CircularlyAccessibleValueObject bodyRowVO, int row)
  {
    setBodyRowVOModel(bodyRowVO, row);
  }

  private void setBodyRowVOModel(CircularlyAccessibleValueObject bodyRowVO, int row)
  {
    if (bodyRowVO == null) {
      return;
    }

    BillItem[] items = getBodyItems();
    for (int i = 0; i < items.length; i++) {
      BillItem item = items[i];
      String key = item.getKey();
      Object aValue = bodyRowVO.getAttributeValue(key);

      if (item.getDataType() == 2) {
        setItemDecimal(bodyRowVO, row, item);
      }

      setValueAtModel(aValue, row, i);
    }
  }

  public void setBodyRowVOs(CircularlyAccessibleValueObject[] bodyVOs, int[] rows)
  {
    if ((bodyVOs == null) || (bodyVOs.length == 0) || (rows == null) || (bodyVOs.length != rows.length))
    {
      return;
    }
    int rowcount = getRowCount();
    boolean needCalculate = isNeedCalculate();
    setNeedCalculate(false);
    setChangeTable(false);
    if (((bodyVOs[0] instanceof SuperVO)) && (!(bodyVOs[0] instanceof GeneralSuperVO)))
    {
      BillItem[] items = getBodyItems();
      String[] fieldnames = new String[items.length];
      for (int i = 0; i < fieldnames.length; i++)
        fieldnames[i] = items[i].getKey();
      Method[] mts = SuperVOGetterSetter.getGetMethods(bodyVOs[0].getClass(), fieldnames);
      try
      {
        for (int i = 0; i < bodyVOs.length; i++) {
          if ((rows[i] < 0) || (rows[i] >= rowcount))
            continue;
          SuperVO bodyRowVO = (SuperVO)bodyVOs[i];
          for (int ii = 0; ii < items.length; ii++)
          {
            Object aValue;
 //           Object aValue;
            if (mts[ii] != null) {
              aValue = bodyRowVO.invokeGetMethod(mts[ii], fieldnames[ii]);
            }
            else {
              aValue = bodyRowVO.getAttributeValue(fieldnames[ii]);
            }

            setValueAtModel(aValue, rows[i], ii);
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    } else {
      for (int i = 0; i < bodyVOs.length; i++) {
        if ((rows[i] < 0) || (rows[i] >= rowcount))
          continue;
        setBodyRowVOModel(bodyVOs[i], rows[i]);
      }
    }
    setNeedCalculate(needCalculate);
    setChangeTable(true);
    fireTableChanged(new TableModelEvent(this));
  }

  void setCellColor(int row, int col, Color color, String type)
  {
    Cell cell = new Cell(row, col);
    if ("all".equals(type)) {
      this.hashCellBackColor.put(cell, color);
      this.hashCellForeColor.put(cell, color);
    } else if ("back".equals(type)) {
      this.hashCellBackColor.put(cell, color);
    } else {
      this.hashCellForeColor.put(cell, color);
    }
  }

  public void setCellEditable(int row, String key, boolean edit)
  {
    getRowAttribute(row).addCellEdit(key, edit);
  }

  public void setDataVector(Vector newData)
  {
    if (newData == null) {
      throw new IllegalArgumentException("setDataVector() - Null parameter");
    }

    this.dataVector = new Vector(0);

    this.dataVector = newData;

    setSortIndex(null);

    int size = newData.size();

    getRowNOTableModel().setNumRows(size);

    this.vRowAttrib = new Vector(size);
    for (int i = 0; i < size; i++) {
      RowAttribute ra = createRowAttribute();
      ra.setRowState(0);
      this.vRowAttrib.addElement(ra);
    }

    newRowsAdded(new TableModelEvent(this, 0, getRowCount() - 1, -1, 1));
  }

  public void setEditRow(int newRow)
  {
    if ((newRow < 0) || (newRow > getRowCount() - 1))
      this.m_editRow = null;
    else
      this.m_editRow = ((Vector)this.dataVector.elementAt(newRow));
  }

  public void setEnabled(boolean isEnabled)
  {
    this.m_bEnabled = isEnabled;
  }

  public boolean isEnabled()
  {
    return this.m_bEnabled;
  }

  public void setEnabledAllItems(boolean isEnabled)
  {
    BillItem[] items = getBodyItems();
    if (items != null)
      for (int i = 0; i < items.length; i++)
        items[i].setEnabled(isEnabled);
  }

  void setEnabledByEditFlag(boolean isEnabled)
  {
    BillItem[] items = getBodyItems();
    if (items != null)
      if (isEnabled)
        for (int i = 0; i < items.length; i++)
          items[i].setEnabled(items[i].isEdit());
      else
        for (int i = 0; i < items.length; i++)
          items[i].setEnabled(isEnabled);
  }

  public void setFixCol(int col)
  {
    this.fixCol = col;
  }

  public void setFormulaParse(FormulaParse newFormulaParse)
  {
    this.m_formulaParse = newFormulaParse;
  }

  public void setNeedCalculate(boolean doCalculate)
  {
    if (this.m_bNeedCalculate != doCalculate) {
      this.m_bNeedCalculate = doCalculate;
      if (this.m_bNeedCalculate)
        reCalcurateAll();
    }
  }

  public void setNotEditAllowedRows(int[] rows)
  {
    if ((rows == null) || (rows.length == 0))
      return;
    if (this.aryNotEditRow == null) {
      this.aryNotEditRow = new ArrayList();
    } else {
      for (int i = 0; i < this.aryNotEditRow.size(); i++) {
        ((RowAttribute)this.aryNotEditRow.get(i)).setEdit(true);
      }
      this.aryNotEditRow.clear();
    }
    for (int i = 0; i < rows.length; i++)
      if (rows[i] < getRowCount()) {
        getRowAttribute(rows[i]).setEdit(false);
        this.aryNotEditRow.add(getRowAttribute(rows[i]));
      }
  }

  public void setRowEditState(boolean newRowEditState)
  {
    this.m_bRowEditState = newRowEditState;
  }

  /** @deprecated */
  public void setRowSort(boolean newRowSort)
  {
  }

  public void setRowState(int row, int state)
  {
    RowAttribute ra = getRowAttribute(row);
    ra.setRowState(state);

    getRowNOTableModel().setValueAt("*", row, 0);
  }

  protected void setSortColumn(int newSortColumn)
  {
    this.m_sortColumn = newSortColumn;
  }

  public void setSortColumn(String key)
  {
    setSortColumn(getBodyColByKey(key));
  }

  public void setSortIndex(int[] indexes)
  {
    this.m_indexes = indexes;
  }

  public void setSortPrepareListener(IBillModelSortPrepareListener sortPrepareListener)
  {
    this.sortPrepareListener = sortPrepareListener;
  }

  public void setTotalTableModel(DefaultTableModel newTotal)
  {
    this.m_tmlTotal = newTotal;
  }

  public void setValueAt(Object aValue, int row, int column)
  {
    setValueAtModel(aValue, row, column);
  }

  public void setValueAt(Object aValue, int row, String strKey) {
    int col = getBodyColByKey(strKey);
    setValueAt(aValue, row, col);
  }

  void setValueAtModel(Object aValue, int row, int column)
  {
    if ((row < 0) || (column < 0))
      return;
    BillItem item = getBodyItems()[column];
    Vector rowVector = (Vector)this.dataVector.elementAt(row);
    int datatype = item.getDataType();

    if (aValue != null)
    {
      String strValue;
      switch (datatype)
      {
      case 1:
        if ((aValue instanceof Integer)) break;
        strValue = aValue.toString().trim();
        if (strValue.length() > 0) {
          int pos = strValue.indexOf('.');
          if (pos >= 0)
            strValue = strValue.substring(0, pos);
          if (strValue.length() == 0)
            aValue = null;
          else
            aValue = new Integer(strValue);
        } else {
          aValue = null;
        }break;
      case 2:
        if ((!this.isFirstSetBodyDataVO) && 
          (item.getDecimalListener() != null)) {
          item.setDecimalDigits(item.getDecimalListener().getDecimalFromItem(row, item));
        }

        if (aValue.getClass() != UFDouble.class) {
          strValue = aValue.toString().trim();
          aValue = strValue.length() > 0 ? new UFDouble(strValue) : null;
        }
        if ((aValue == null) || 
          (isIgnoreScaleWhenSetValue())) break;
        aValue = ((UFDouble)aValue).setScale(0 - item.getDecimalDigits(), 4); break;
      case 4:
        if (!(aValue instanceof String)) break;
        aValue = UFBoolean.valueOf(aValue.toString());
        break;
      case 6:
        UIComboBox combo = (UIComboBox)item.getComponent();
        int index = combo.getItemIndexByObject(aValue);
        if ((index < 0) && (item.isWithIndex())) {
          if ("".equals(aValue))
            index = 0;
          else {
            index = Integer.parseInt(aValue.toString());
          }
        }
        if (index < 0) break;
        aValue = combo.getItemNameAt(index); break;
      case 3:
      case 5:
      }

    }

    rowVector.setElementAt(aValue, column);

    if ((isNeedCalculate()) && 
      (item.isTotal()) && (
      (datatype == 1) || (datatype == 2)))
    {
      reCalcurate(column);
    }
    if (isChangeTable())
      fireTableChanged(new TableModelEvent(this, row, row, column));
  }

  void setValueAtModel(Object aValue, int row, String strKey) {
    int col = getBodyColByKey(strKey);
    setValueAtModel(aValue, row, col);
  }

  public void sortByColumn(int column, boolean ascending, int[] noldrow)
  {
    this.m_ascending = ascending;

    setSortColumn(column);

    Comparator cp = getItemComparator(getBodyItems()[column]);

    SortUtils.sort(this.dataVector, getSortObjectList(), getSortObjectArray(), noldrow, cp);

    if (this.bsl != null) {
      String itemkey = getBodyItems()[column].getKey();
      this.bsl.afterSort(itemkey);
    }

    EventListener[] ls = this.afterSortListener.getListeners(BillSortListener2.class);

    int currentrow = -1;
    if ((getBillScrollPane() != null) && (noldrow != null) && (noldrow.length > 0)) {
      currentrow = noldrow[0];
    }
    for (int i = 0; i < ls.length; i++) {
      ((BillSortListener2)ls[i]).currentRowChange(currentrow);
    }

    this.sorttype = -1;
  }

  private Comparator getItemComparator(BillItem item) {
    Comparator cp = null;

    if (item.getItemComparator() == null) {
      IBillModelSortPrepareListener sl = getSortPrepareListener();
      String itemkey = item.getKey();
      if (sl != null)
        this.sorttype = sl.getSortTypeByBillItemKey(itemkey);
      else {
        switch (getItemByKey(itemkey).getDataType()) {
        case 4:
          this.sorttype = 4;
          break;
        case 0:
          this.sorttype = 0;
          break;
        }

      }

      cp = new ColumnComparator(this.sorttype);
    } else {
      cp = new ColumnComparator(item.getItemComparator());
    }

    return cp;
  }

  public void sortByColumn(int column, boolean ascending)
  {
    sortByColumn(column, ascending, new int[0]);
  }

  public void sortByColumn(String key, boolean ascending)
  {
    sortByColumn(getBodyColByKey(key), ascending);
  }

  private Vector transferBodyData(Vector vValue)
  {
    if (vValue == null) {
      return new Vector();
    }
    int size = vValue.size();
    Vector vData = new Vector(size);

    for (int i = 0; i < size; i++) {
      Vector vRow = (Vector)vValue.elementAt(i);
      Vector vNewRow = transferBodyRowData(vRow);
      vData.addElement(vNewRow);
    }
    return vData;
  }

  private Vector<Object> transferBodyRowData(Vector v)
  {
    return v == null ? new Vector() : new Vector(v);
  }

  public void updateValue()
  {
    this.vViewBodyCache = transferBodyData(this.dataVector);

    int size = this.dataVector.size();

    for (int i = 0; i < size; i++) {
      RowAttribute ra = getRowAttribute(i);
      ra.setRowState(0);
      ra.updataCellEdit();
    }
    this.vDeleteRow = null;
  }

  private Vector getRowVectorAtModel(int row)
  {
    if (row < 0)
      return null;
    if ((this.dataVector == null) || (this.dataVector.size() <= row))
      return null;
    Vector rowVector = (Vector)this.dataVector.elementAt(row);
    return rowVector;
  }

  private boolean isChangeTable()
  {
    return this.changeTable;
  }

  private void setChangeTable(boolean changeTable)
  {
    this.changeTable = changeTable;
  }

  public UserFixRowTableModel getUserFixRowModel()
  {
    if (this.m_userFixRowModel == null) {
      this.m_userFixRowModel = new UserFixRowTableModel(this);
    }
    return this.m_userFixRowModel;
  }

  void setUserFixRowModel(UserFixRowTableModel m) {
    this.m_userFixRowModel = m;
    if (m != null)
      m.setParentModel(this);
  }

  FixRowNOModel getFixRowHeaderModel() {
    if (this.fixRowHeaderModel == null)
      this.fixRowHeaderModel = new FixRowNOModel();
    return this.fixRowHeaderModel;
  }

  UserRowNOModel getUserRowHeaderModel() {
    if (this.userRowHeaderModel == null)
      this.userRowHeaderModel = new UserRowNOModel();
    return this.userRowHeaderModel;
  }

  public BillModelCellEditableController getCellEditableController() {
    return this.cellEditableController;
  }

  public void setCellEditableController(BillModelCellEditableController cellEditableController)
  {
    this.cellEditableController = cellEditableController;
  }

  private boolean isIgnoreScaleWhenSetValue() {
    return this.ignoreScaleWhenSetValue;
  }

  public void addSortRelaObjectListener(IBillRelaSortListener l) {
    this.sortRelaObjectListener.add(IBillRelaSortListener.class, l);
  }

  public void addSortRelaObjectListener2(IBillRelaSortListener2 l) {
    this.sortRelaObjectListener.add(IBillRelaSortListener2.class, l);
  }

  public void addBillSortListener2(BillSortListener2 l) {
    this.afterSortListener.add(BillSortListener2.class, l);
  }

  public void removeSortRelaObjectListener(IBillRelaSortListener l) {
    this.sortRelaObjectListener.remove(IBillRelaSortListener.class, l);
  }

  public void removeSortRelaObjectListener2(IBillRelaSortListener2 l) {
    this.sortRelaObjectListener.remove(IBillRelaSortListener2.class, l);
  }

  public void removeBillSortListener2(BillSortListener2 l) {
    this.afterSortListener.remove(BillSortListener2.class, l);
  }

  private List[] getSortObjectList() {
    List[] ret = null;

    ArrayList sortObject = new ArrayList();
    sortObject.add(this.vRowAttrib);

    EventListener[] ls = this.sortRelaObjectListener.getListeners(IBillRelaSortListener.class);
    for (int i = 0; i < ls.length; i++) {
      List o = ((IBillRelaSortListener)ls[i]).getRelaSortObject();
      if (o != null) {
        sortObject.add(o);
      }
    }
    ret = new List[sortObject.size()];
    sortObject.toArray(ret);

    return ret;
  }

  private Object[][] getSortObjectArray() {
    Object[][] ret = (Object[][])null;

    ArrayList sortObject = new ArrayList();

    EventListener[] ls = this.sortRelaObjectListener.getListeners(IBillRelaSortListener2.class);
    for (int i = 0; i < ls.length; i++) {
      Object[] o = (Object[])((IBillRelaSortListener2)ls[i]).getRelaSortObjectArray();
      if (o != null) {
        sortObject.add(o);
      }
    }
    ret = new Object[sortObject.size()][];

    for (int i = 0; i < sortObject.size(); i++) {
      ret[i] = ((Object[])sortObject.get(i));
    }

    return ret;
  }

  /** @deprecated */
  public void setIgnoreScaleWhenSetValue(boolean ignoreScaleWhenSetValue)
  {
    this.ignoreScaleWhenSetValue = ignoreScaleWhenSetValue;
  }

  public boolean isSortAscending()
  {
    return this.m_ascending;
  }

  class ColumnComparator
    implements Comparator
  {
    int sorttype = -1;
    Comparator<Object> comp = null;

    public ColumnComparator(int sorttype)
    {
      this.sorttype = sorttype;
    }

    public ColumnComparator() {
      this.comp = comp;
    }

    public ColumnComparator(Comparator<Object> itemComparator) {
		// TODO Auto-generated constructor stub
	}

	public int compare(Object o1, Object o2)
    {
      int result = -1;

      Object co1 = ((Vector)o1).get(BillModel.this.getSortColumn());
      Object co2 = ((Vector)o2).get(BillModel.this.getSortColumn());

      if (this.comp == null)
      {
        result = compareObject(co1, co2);
      }
      else {
        result = this.comp.compare(co1, co2);
      }

      return BillModel.this.m_ascending ? result : -result;
    }

    private int compareObject(Object o1, Object o2) {
      int type = this.sorttype;

      if (o1 == o2)
        return 0;
      if (o1 == null)
      {
        return -1;
      }if (o2 == null) {
        return 1;
      }

      int ret = 0;

      switch (type) {
      case -1:
        if ((!(o1 instanceof Comparable)) || (!(o2 instanceof Comparable))) break;
        ret = ((Comparable)o1).compareTo((Comparable)o2);
        break;
      case 1:
      case 2:
        UFDouble uo1 = new UFDouble(o1.toString());
        UFDouble uo2 = new UFDouble(o2.toString());

        ret = uo1.compareTo(uo2);
        break;
      case 0:
      default:
        ret = MiscUtils.compareStringByBytes(o1, o2);
      }

      return ret;
    }
  }

  public class TotalTableModel extends DefaultTableModel
  {
    public TotalTableModel()
    {
    }

    public int getColumnCount()
    {
      return BillModel.this.getColumnCount();
    }

    public String getColumnName(int col) {
      return BillModel.this.getColumnName(col);
    }

    public boolean isCellEditable(int row, int col) {
      return false;
    }
  }

  public class UserRowNOModel extends DefaultTableModel
  {
    public UserRowNOModel()
    {
    }

    public int getColumnCount()
    {
      return 1;
    }

    public int getRowCount() {
      int row = 0;
      if (BillModel.this == null)
        return row;
      if (BillModel.this.m_userFixRowModel != null)
        row += BillModel.this.m_userFixRowModel.getRowCount();
      return row;
    }

    public Object getValueAt(int row, int column) {
      return "" + row;
    }
  }

  public class FixRowNOModel extends DefaultTableModel
  {
    public FixRowNOModel()
    {
    }

    public int getColumnCount()
    {
      return 1;
    }

    public int getRowCount() {
      int row = 0;
      if (BillModel.this == null)
        return row;
      if (BillModel.this.m_tmlTotal != null)
        row += BillModel.this.m_tmlTotal.getRowCount();
      return row;
    }

    public Object getValueAt(int row, int column) {
      return BillModel.this.totalTitle;
    }
  }

  public class RowNumberModel extends DefaultTableModel
  {
    public RowNumberModel()
    {
    }

    public int getColumnCount()
    {
      return 1;
    }

    public String getColumnName(int col) {
      return "   ";
    }

    public boolean isCellEditable(int row, int col) {
      return false;
    }

    public void setNumRows(int rowCount) {
      int old = getRowCount();
      if (old == rowCount) {
        return;
      }
      this.dataVector.setSize(rowCount);
      if (rowCount > old)
      {
        justifyRows(old, rowCount);
      }
      fireTableChanged(new TableModelEvent(this));
    }

    private void justifyRows(int from, int to) {
      this.dataVector.setSize(getRowCount());

      for (int i = from; i < to; i++) {
        if (this.dataVector.elementAt(i) == null) {
          this.dataVector.setElementAt(new Vector(), i);
        }
        ((Vector)this.dataVector.elementAt(i)).setSize(getColumnCount());
      }
    }

    public Object getValueAt(int row, int column) {
      if ((row >= 0) && (column >= 0)) {
        String value = row + 1 + "";
        if (BillModel.this.getRowState(row) == 4) {
          value = value + "*";
        }
        return value;
      }
      return null;
    }
  }
}