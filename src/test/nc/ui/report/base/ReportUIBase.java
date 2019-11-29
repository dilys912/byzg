package nc.ui.report.base;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.pub.report.ReportSortUtil;
import nc.ui.trade.report.controller.IReportCtl;
import nc.ui.trade.report.controller.ReportCtl;
import nc.ui.trade.report.group.GroupTableModel;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.pub.report.ReportModelVO;
import nc.vo.pub.report.SubtotalContext;
import nc.vo.trade.report.ConvertTool;
import nc.vo.trade.report.IReportModelSelectType;
import nc.vo.trade.report.ReportDataType2UFDateType;
import nc.vo.trade.report.ReportVO;
import nc.vo.trade.report.ReportVOMetaClass;
import nc.vo.trade.report.TableField;

public abstract class ReportUIBase extends ToftPanel
{
  private UIPanel conditionPanel = null;

  private UISplitPane veriSplitPane = null;

  private ReportBaseClass m_report = null;

  private ReportBaseClass m_reportForPrint = null;
  protected static final char columnSystemDelimiter = '_';
  private ArrayList reportModelColumnGroups = new ArrayList();
  private IReportCtl m_uiCtl;
  private ReportModelVO[] copyOfReportModelVOs = null;

  private QueryDLG m_qryDlg = null;

  private ButtonAssets button_action_map = new ButtonAssets(this);

  private UITable groupTable = null;

  private HashMap groupMap = new HashMap();

  private ArrayList groupKeys = new ArrayList();

  private CircularlyAccessibleValueObject[] allBodyDataVO = null;

  private boolean needGroup = false;

  public ReportUIBase()
  {
    initialize();
  }

  public ReportUIBase(FramePanel fp)
  {
    setFrame(fp);
    initialize();
  }

  protected void backupReportModelVOs()
  {
    ArrayList al = new ArrayList();
    for (int i = 0; i < getReportBase().getAllBodyVOs().length; i++)
    {
      if (getReportBase().getAllBodyVOs()[i].getSelectType().equals(IReportModelSelectType.UNSELECTED))
        continue;
      al.add(getReportBase().getAllBodyVOs()[i].clone());
    }

    this.copyOfReportModelVOs = ((ReportModelVO[])(ReportModelVO[])al.toArray(new ReportModelVO[0]));
  }

  protected String combineDefaultSqlWhere(String customSqlWhere)
  {
    String result = null;
    boolean isCustomSqlWhereEmpty = (customSqlWhere == null) || (customSqlWhere.length() == 0);

    boolean isDefaultSqlWhereEmpty = (getUIControl().getDefaultSqlWhere() == null) || (getUIControl().getDefaultSqlWhere().trim().length() == 0);

    if (isCustomSqlWhereEmpty)
      result = getUIControl().getDefaultSqlWhere();
    if (isDefaultSqlWhereEmpty)
      result = customSqlWhere;
    if ((!isCustomSqlWhereEmpty) && (!isDefaultSqlWhereEmpty)) {
      result = customSqlWhere + " and " + getUIControl().getDefaultSqlWhere();
    }
    if ((result != null) && (result.trim().length() == 0))
      result = null;
    return result;
  }

  protected String convertReportModelFieldNameToVOFieldName(String reportFieldName)
  {
    if (reportFieldName.indexOf('_') == -1) {
      return reportFieldName;
    }
    return reportFieldName.substring(0, reportFieldName.indexOf('_')) + "." + reportFieldName.substring(reportFieldName.indexOf('_') + 1, reportFieldName.length());
  }

  protected String convertVOFieldNameToReportModelFieldName(String voFieldName)
  {
    if (voFieldName.indexOf('.') == -1) {
      return voFieldName;
    }
    return voFieldName.substring(0, voFieldName.indexOf('.')) + "_" + voFieldName.substring(voFieldName.indexOf('.') + 1, voFieldName.length());
  }

  protected String[] createConditionsFromConditionVO(ConditionVO[] vos)
  {
    String[] conditions = new String[vos.length];
    for (int i = 0; i < vos.length; i++)
    {
      conditions[i] = (vos[i].getFieldName() + vos[i].getOperaCode());
      if (vos[i].getRefResult() != null)
      {
        int tmp63_62 = i;
        String[] tmp63_61 = conditions; tmp63_61[tmp63_62] = (tmp63_61[tmp63_62] + vos[i].getRefResult().getRefName());
      }
      else
      {
        int tmp96_95 = i;
        String[] tmp96_94 = conditions; tmp96_94[tmp96_95] = (tmp96_94[tmp96_95] + vos[i].getValue());
      }
    }
    return conditions;
  }

  protected IReportCtl createIReportCtl()
  {
    return new ReportCtl();
  }

  protected QueryDLG createQueryDLG()
  {
    QueryDLG dlg = new QueryDLG();

    dlg.setTempletID(getUIControl()._getPk_corp(), getModuleCode(), getUIControl()._getOperator(), null);
    dlg.setNormalShow(false);
    return dlg;
  }

  protected TableField createTableFieldFromReportModelVO(ReportModelVO vo)
  {
    return new TableField(convertReportModelFieldNameToVOFieldName(vo.getColumnCode()), vo.getColumnUser(), (vo.getDataType().intValue() == 1) || (vo.getDataType().intValue() == 2));
  }

  protected String[] getAllColumnCodes()
  {
    ReportModelVO[] vos = getReportBase().getAllBodyVOs();
    String[] names = new String[vos.length];
    for (int i = 0; i < vos.length; i++)
    {
      names[i] = vos[i].getColumnCode();
    }
    return names;
  }

  protected void getAllFieldsAndDataType(ArrayList fields, ArrayList dataTypes)
  {
    if ((fields == null) || (dataTypes == null)) {
      throw new IllegalArgumentException("getQueryFieldsAndDataType param is null");
    }

    ReportModelVO[] vos = getModelVOs();

    for (int i = 0; i < vos.length; i++)
    {
      TableField f = createTableFieldFromReportModelVO(vos[i]);
      fields.add(f.getFieldName());
      dataTypes.add(new Integer(ReportDataType2UFDateType.convert(vos[i].getDataType())));
    }
  }

  private ButtonObject[] getAllBtnAry()
  {
    if (this.button_action_map.getVisibleButtonsByOrder().size() == 0)
      return null;
    return (ButtonObject[])(ButtonObject[])this.button_action_map.getVisibleButtonsByOrder().toArray(new ButtonObject[0]);
  }

  protected String[] getColumnGroupsByColumnCode(String column_code)
  {
    for (int i = 0; i < this.reportModelColumnGroups.size(); i++)
    {
      ArrayList al = (ArrayList)this.reportModelColumnGroups.get(i);
      if (al.contains(column_code)) {
        return (String[])(String[])al.toArray(new String[0]);
      }
    }
    return new String[0];
  }

  protected UIPanel getConditionPanel()
  {
    if (this.conditionPanel == null)
    {
      this.conditionPanel = new UIPanel();
      this.conditionPanel.setName("ConditionPanel");
      FlowLayout l = new FlowLayout();
      l.setAlignment(0);
      l.setHgap(10);

      this.conditionPanel.setLayout(l);
    }
    return this.conditionPanel;
  }

  protected TableField[] getInvisibleFields()
  {
    ReportModelVO[] vos = getModelVOs();
    ArrayList invisible = new ArrayList();
    for (int i = 0; i < vos.length; i++)
    {
      if (vos[i].getSelectType().intValue() != IReportModelSelectType.VISIBLE.intValue()) {
        continue;
      }
      try
      {
        getReportBase().getBillTable().getColumn(vos[i].getColumnUser());
      }
      catch (Exception e)
      {
        invisible.add(createTableFieldFromReportModelVO(vos[i]));
      }

    }

    TableField[] invisibleFields = (TableField[])(TableField[])invisible.toArray(new TableField[0]);

    return invisibleFields;
  }

  protected TableField[] getVisibleFields()
  {
    ReportModelVO[] vos = getModelVOs();
    ArrayList visible = new ArrayList();
    for (int i = 0; i < vos.length; i++)
    {
      if (vos[i].getSelectType().intValue() != IReportModelSelectType.VISIBLE.intValue()) {
        continue;
      }
      try
      {
        getReportBase().getBillTable().getColumn(vos[i].getColumnUser());

        visible.add(createTableFieldFromReportModelVO(vos[i]));
      }
      catch (Exception e)
      {
      }

    }

    TableField[] visibleFields = (TableField[])(TableField[])visible.toArray(new TableField[0]);

    return visibleFields;
  }

  public QueryDLG getQryDlg()
  {
    if (this.m_qryDlg == null)
    {
      this.m_qryDlg = createQueryDLG();
    }
    return this.m_qryDlg;
  }

  public ReportBaseClass getReportBase()
  {
    if (this.m_report == null)
    {
      try
      {
        this.m_report = new ReportBaseClass();
        this.m_report.setName("ReportBase");
        this.m_report.setTempletID(getUIControl()._getPk_corp(), getModuleCode(), getUIControl()._getOperator(), null);

        this.m_report.getBillTable().getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
          public void valueChanged(ListSelectionEvent e)
          {
            ReportUIBase.this.updateAllButtons();
          }
        });
      }
      catch (Exception ex)
      {
        MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("uifactory_report", "UPPuifactory_report-000032"), NCLangRes.getInstance().getStrByID("uifactory_report", "UPPuifactory_report-000033"));
        Logger.error(ex.getMessage(), ex);
      }
    }
    return this.m_report;
  }

  protected UITable getGroupTable()
  {
    if (this.groupTable == null)
    {
      this.groupTable = new UITable(new DefaultTableModel());
      this.groupTable.addMouseListener(new MouseAdapter()
      {
        public void mouseReleased(MouseEvent e)
        {
          int row = ReportUIBase.this.groupTable.getSelectedRow();
          if (row == -1)
            return;
          int count = ReportUIBase.this.groupTable.getModel().getColumnCount();
          StringBuffer key = new StringBuffer();
          for (int i = 0; i < count; i++)
          {
            key.append(ReportUIBase.this.groupTable.getModel().getValueAt(row, i) == null ? "" : ReportUIBase.this.groupTable.getModel().getValueAt(row, i).toString());

            if (i != count - 1)
              key.append(":");
          }
          ArrayList tmpVO = (ArrayList)ReportUIBase.this.groupMap.get(key.toString());
          if ((tmpVO != null) && (tmpVO.size() > 0))
          {
            ReportUIBase.this.setBodyDataVO((CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])tmpVO.toArray(new CircularlyAccessibleValueObject[0]), false);
          }

          ReportUIBase.this.setHeadItems(ReportUIBase.this.convertVOKeysToModelKeys((String[])(String[])ReportUIBase.this.groupKeys.toArray(new String[0])), ReportUIBase.access$300(ReportUIBase.this, row));
        }
      });
    }

    return this.groupTable;
  }

  protected static Object[] access$300(ReportUIBase reportUIBase, int row) {
	// TODO Auto-generated method stub
	return null;
}

protected void updateTitle()
  {
    updateTitle(getReportBase().getReportTitle());
  }

  protected void updateTitle(String strTitle)
  {
    setTitleText(strTitle);
  }

  protected void updateReportBase()
  {
    if (this.m_report == null)
      return;
    try
    {
      this.m_report.setTempletID(getUIControl()._getPk_corp(), getModuleCode(), getUIControl()._getOperator(), null);
    }
    catch (Exception e)
    {
      Logger.error(e.getMessage(), e);
    }
  }

  public ReportBaseClass getReportBaseForPrint()
  {
    if (this.m_reportForPrint == null)
    {
      try
      {
        this.m_reportForPrint = new ReportBaseClass();
        this.m_reportForPrint.setName("ReportBaseForPrint");
        this.m_reportForPrint.setTempletID(getUIControl()._getPk_corp(), getModuleCode(), getUIControl()._getOperator(), null);
      }
      catch (Exception ex)
      {
        System.out.println("基类:未找到报表模板......");
      }
    }
    return this.m_reportForPrint;
  }

  protected ReportVOMetaClass getReportVOMetaClassOfAllFields()
  {
    ArrayList fs = new ArrayList();
    ArrayList ds = new ArrayList();
    getAllFieldsAndDataType(fs, ds);
    String[] fieldsname = (String[])(String[])fs.toArray(new String[0]);
    Integer[] datatypes = (Integer[])(Integer[])ds.toArray(new Integer[0]);
    String[] fieldAlias = (String[])ConvertTool.createFieldAlias(fieldsname);

    return new ReportVOMetaClass(fieldsname, fieldAlias, datatypes, getUIControl().getAllTableAlias(), getUIControl().getTableJoinClause());
  }

  public String getTitle()
  {
    return getReportBase().getReportTitle();
  }

  public IReportCtl getUIControl()
  {
    if (this.m_uiCtl == null)
      this.m_uiCtl = createIReportCtl();
    return this.m_uiCtl;
  }

  public CircularlyAccessibleValueObject[] getVOFromUI()
  {
    ReportVOMetaClass voClass = getReportVOMetaClassOfAllFields();
    ReportItem[] items = getReportBase().getBody_Items();
    int rows = getReportBase().getRowCount();
    ReportVO[] result = new ReportVO[rows];
    for (int row = 0; row < rows; row++)
    {
      result[row] = voClass.createReportVO();
      for (int i = 0; i < items.length; i++)
      {
        result[row].setAttributeValue(items[i].getKey(), getReportBase().getBodyValueAt(row, items[i].getKey()));
      }

    }

    return result;
  }

  public CircularlyAccessibleValueObject[] getCurrentVO()
  {
    CircularlyAccessibleValueObject[] cvos = null;
    if ((cvos = getCurrentVOFromGroupMap()) == null)
    {
      cvos = getVOFromUI();
    }
    return cvos;
  }

  public CircularlyAccessibleValueObject[] getAllBodyDataVO()
  {
    if (this.allBodyDataVO == null)
    {
      try
      {
        this.allBodyDataVO = getVOFromUI();
      }
      catch (Exception e)
      {
        Logger.error(e.getMessage(), e);
      }
    }
    return this.allBodyDataVO;
  }

  protected void initColumnGroups()
  {
    ReportModelVO[] vos = getModelVOs();
    HashMap tmpHash = new HashMap();

    for (int i = 0; i < vos.length; i++)
    {
      int index = vos[i].getColumnSystem().indexOf('_');
      String key;
 //     String key;
      if (index != -1)
      {
        key = vos[i].getColumnSystem().substring(0, index);
      }
      else
      {
        key = vos[i].getColumnSystem();
      }
      ArrayList al;
      if (tmpHash.get(key) == null)
      {
        al = new ArrayList();
        tmpHash.put(key, al);
      }
      else
      {
        al = (ArrayList)tmpHash.get(key);
      }
      al.add(vos[i].getColumnCode());
    }

    this.reportModelColumnGroups.addAll(tmpHash.values());
  }

  private void initialize()
  {
    setName("GeneralPane");
    setSize(774, 419);

    UISplitPane horiSplitPane = new UISplitPane(0, false, getConditionPanel(), getVeriSplitPane());

    if (!getUIControl().isShowCondition())
    {
      horiSplitPane.setDividerLocation(0);
      horiSplitPane.setEnabled(false);
      horiSplitPane.setDividerSize(0);
    }
    else
    {
      horiSplitPane.setDividerLocation(80);
      horiSplitPane.setOneTouchExpandable(true);
      horiSplitPane.setDividerSize(6);
    }

    add(horiSplitPane);

    setPrivateButtons();

    getReportBase();

    setButtons(getAllBtnAry());

    updateAllButtons();

    backupReportModelVOs();

    if (getUIControl().getGroupKeys() != null) {
      this.needGroup = true;
    }
    initColumnGroups();

    setDigitFormat();

    getReportBase().setShowNO(true);
  }

  private void setVeriSplitEnabled(boolean enabled)
  {
    if (enabled)
    {
      this.veriSplitPane.setDividerLocation(200);
      this.veriSplitPane.setEnabled(true);
      this.veriSplitPane.setDividerSize(8);
      this.veriSplitPane.setOneTouchExpandable(true);
      getReportBase().setShowNO(true);
    }
    else
    {
      this.veriSplitPane.setDividerLocation(0);

      this.veriSplitPane.setDividerSize(0);
      this.veriSplitPane.setOneTouchExpandable(false);
      getReportBase().setShowNO(false);
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    try
    {
      if (this.button_action_map.get(bo) != null)
      {
        IButtonActionAndState action = (IButtonActionAndState)this.button_action_map.get(bo);

        action.execute();
      }
    }
    catch (BusinessException ex)
    {
      showErrorMessage(ex.getMessage());
      Logger.error(ex.getMessage(), ex);
    }
    catch (Exception e)
    {
      Logger.error(e.getMessage(), e);
    }

    updateAllButtons();
  }

  protected void onSort(String[] fields, int[] asc)
  {
    CircularlyAccessibleValueObject[] vos = null;
    if ((vos = getCurrentVO()) == null)
      return;
    getReportBase().getReportSortUtil().multiSort(vos, fields, asc);
    setBodyDataVO(vos, false);
  }

  private CircularlyAccessibleValueObject[] getCurrentVOFromGroupMap()
  {
    if (this.groupKeys.size() == 0)
      return null;
    int selectedRow = 0;
    if (getGroupTable().getSelectedRow() != -1)
      selectedRow = getGroupTable().getSelectedRow();
    String[] keys = getValuesFromGroupTable(selectedRow);
    String key = "";
    for (int i = 0; i < keys.length; i++)
    {
      key = key + keys[i];
      if (i != keys.length - 1)
        key = key + ":";
    }
    return this.groupMap.get(key) == null ? null : (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])((ArrayList)this.groupMap.get(key)).toArray(new CircularlyAccessibleValueObject[0]);
  }

  protected void onPrintTemplet()
  {
    getReportBase().printData();
  }

  protected void updateAllButtons()
  {
    boolean hasData = false;
    BillModel bm = this.m_report.getBillModel();
    if ((bm != null) && ((bm.getDataVector() == null) || (bm.getDataVector().size() == 0)))
    {
      hasData = false;
    }
    else
    {
      hasData = true;
    }
    setAllButtonState(hasData);
    updateButtons();
  }

  protected ReportModelVO[] getModelVOs()
  {
    return this.copyOfReportModelVOs;
  }

  private void setAllButtonState(boolean hasData)
  {
    Iterator it = this.button_action_map.keySet().iterator();
    while (it.hasNext())
    {
      ButtonObject obj = (ButtonObject)it.next();
      IButtonActionAndState state = (IButtonActionAndState)this.button_action_map.get(obj);

      int result = state.isButtonAvailable();
      if (result == 0)
      {
        obj.setEnabled(false);
      }
      else if (result == 1)
      {
        obj.setEnabled(true);
      }
      else if (result == 2)
      {
        obj.setEnabled(true);
      }
      else if (result == 3)
      {
        if (hasData)
        {
          obj.setEnabled(true);
        }
        else
          obj.setEnabled(false);
      }
    }
  }

  protected void onFilter(String strFomula)
    throws Exception
  {
    getReportBase().filter(strFomula);
  }

  protected void onCross(String[] rows, String[] cols, String[] values)
    throws Exception
  {
    getReportBase().drawCrossTable(rows, cols, values);
  }

  protected void onColumnFilter(String title, String[] fieldNames, String[] showNames, boolean isAdjustOrder)
    throws Exception
  {
    CircularlyAccessibleValueObject[] vos = getBodyDataVO();
    getReportBase().hideColumn(getAllColumnCodes());

    getReportBase().setReportTitle(title);

    setTitleText(title);
    getReportBase().showHiddenColumn(fieldNames);
    if (isAdjustOrder)
      setColumnOrder(fieldNames);
    if ((showNames != null) && (showNames.length == fieldNames.length))
      setColumnName(fieldNames, showNames);
    setBodyDataVO(vos, true);
  }

  private void setColumnName(String[] fieldNames, String[] showNames)
  {
    ReportItem[] items = getReportBase().getBody_Items();
    HashMap tmpHas = new HashMap();
    for (int i = 0; i < items.length; i++)
    {
      tmpHas.put(items[i].getKey(), items[i]);
    }

    for (int i = 0; i < fieldNames.length; i++)
    {
      if (!tmpHas.containsKey(fieldNames[i]))
        continue;
      ReportItem tmpItem = (ReportItem)tmpHas.get(fieldNames[i]);
      if (!tmpItem.getName().equals(showNames[i])) {
        tmpItem.setName(showNames[i]);
      }
    }

    getReportBase().setBody_Items(items);
  }

  public void onPrintDirect()
    throws Exception
  {
    PrintDirectEntry print = PrintManager.getDirectPrinter(getReportBase().getBillTable(), getReportBase().getHead_Items());

    print.setTitle(getTitle());
    print.preview();
  }

  public void onPrintPreview()
    throws Exception
  {
    getReportBase().previewData();
  }

  protected abstract void onQuery()
    throws Exception;

  protected void onRefresh()
    throws Exception
  {
  }

  protected void onSubTotal(SubtotalContext context)
    throws Exception
  {
    getReportBase().setSubtotalContext(context);
    getReportBase().subtotal();
  }

  protected void onGroup(String[] keys)
  {
    String[] colNames = getColumnNamesByKeys(convertVOKeysToModelKeys(keys));
    onGroup(keys, colNames);
  }

  protected void onGroup(String[] keys, String[] names)
  {
    String[] convertGroupKeys = convertVOKeysToModelKeys((String[])(String[])this.groupKeys.toArray(new String[0]));

    getReportBase().showHiddenColumn(convertGroupKeys);

    removeHeadItems(convertGroupKeys);

    this.groupKeys.clear();

    if ((keys == null) || (names == null) || (keys.length == 0) || (names.length == 0) || (keys.length != names.length))
    {
      setVeriSplitEnabled(false);

      setBodyDataVO(this.allBodyDataVO, false);
      return;
    }

    this.groupKeys.addAll(Arrays.asList(keys));

    setVeriSplitEnabled(true);

    if ((this.allBodyDataVO == null) || (this.allBodyDataVO.length == 0)) {
      this.allBodyDataVO = getVOFromUI();
    }
    this.groupMap.clear();

    for (int i = 0; i < this.allBodyDataVO.length; i++)
    {
      StringBuffer key = new StringBuffer();
      for (int j = 0; j < keys.length; j++)
      {
        key.append(this.allBodyDataVO[i].getAttributeValue(keys[j]) == null ? " " : this.allBodyDataVO[i].getAttributeValue(keys[j]).toString());

        if (j != keys.length - 1)
          key.append(":");
      }
      addVoToHashmap(key.toString(), this.allBodyDataVO[i]);
    }
    String[] convertedKeys = convertVOKeysToModelKeys(keys);
    extractItemsToHead(convertedKeys, names);
    getReportBase().hideColumn(convertedKeys);

    GroupTableModel model = new GroupTableModel();
    model.addColumns(names);
    model.addRows(this.groupMap.keySet());
    getGroupTable().setModel(model);

    ArrayList tmpVO = (ArrayList)this.groupMap.get(this.groupMap.keySet().iterator().next());

    if ((tmpVO != null) && (tmpVO.size() > 0))
    {
      setBodyDataVO((CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])tmpVO.toArray(new CircularlyAccessibleValueObject[0]), false);

      setHeadItems(convertedKeys, getValuesFromGroupTable(0));
    }
  }

  private void removeHeadItems(String[] strs)
  {
    ReportItem[] headItems = getReportBase().getHead_Items();
    ArrayList list = new ArrayList();
    if ((headItems != null) && (headItems.length > 0))
    {
      for (int i = 0; i < headItems.length; i++)
      {
        if (contains(strs, headItems[i].getKey()))
          continue;
        list.add(headItems[i]);
      }
    }
    getReportBase().setHead_Items((ReportItem[])(ReportItem[])list.toArray(new ReportItem[0]));
  }

  private String[] getValuesFromGroupTable(int row)
  {
    UITable table = getGroupTable();
    int count = table.getColumnCount();
    String[] str = new String[count];
    for (int i = 0; i < str.length; i++)
    {
      str[i] = ((String)table.getModel().getValueAt(row, i));
    }
    return str;
  }

  private void extractItemsToHead(String[] keys, String[] colNames)
  {
    String[] convertKeys = convertVOKeysToModelKeys(keys);
    ReportItem[] items = new ReportItem[keys.length];
    for (int i = 0; i < convertKeys.length; i++)
    {
      items[i] = new ReportItem();
      items[i].setKey(convertKeys[i]);
      items[i].setShow(true);
      items[i].setName(colNames[i]);
    }
    getReportBase().addHeadItem(items);
  }

  public String[] convertVOKeysToModelKeys(String[] keys)
  {
    if ((keys == null) || (keys.length == 0))
      return null;
    String[] convertedKeys = new String[keys.length];
    for (int i = 0; i < keys.length; i++)
    {
      convertedKeys[i] = convertVOFieldNameToReportModelFieldName(keys[i]);
    }
    return convertedKeys;
  }

  private void setHeadItems(String[] keys, Object[] values)
  {
    if ((keys == null) || (values == null) || (keys.length == 0) || (values.length == 0))
    {
      return;
    }if (keys.length != values.length)
    {
      System.out.println("键和值的数目不匹配");
      return;
    }
    for (int i = 0; i < keys.length; i++)
    {
      getReportBase().setHeadItem(keys[i], values[i]);
    }
  }

  protected String[] getColumnNamesByKeys(String[] keys)
  {
    if ((keys == null) || (keys.length == 0))
      return null;
    ReportModelVO[] fields = getModelVOs();

    ArrayList list = new ArrayList();
    if ((fields != null) && (fields.length != 0))
    {
      for (int i = 0; i < keys.length; i++)
      {
        for (int j = 0; j < fields.length; j++)
        {
          if (fields[j].getColumnCode().equals(keys[i]))
            list.add(fields[j].getColumnUser());
        }
      }
    }
    return (String[])(String[])list.toArray(new String[0]);
  }

  protected String getColumnNameByKey(String key)
  {
    if (key == null)
      return null;
    ReportModelVO[] fields = getModelVOs();
    if ((fields != null) && (fields.length != 0))
    {
      for (int j = 0; j < fields.length; j++)
      {
        if (fields[j].getColumnCode().equals(key))
          return fields[j].getColumnUser();
      }
    }
    return null;
  }

  private void addVoToHashmap(String key, CircularlyAccessibleValueObject vo)
  {
    ArrayList list = null;
    if ((list = (ArrayList)this.groupMap.get(key)) == null)
    {
      list = new ArrayList();
      list.add(vo);
      this.groupMap.put(key, list);
    }
    else {
      list.add(vo);
    }
  }

  protected CircularlyAccessibleValueObject[] processVOs(CircularlyAccessibleValueObject[] vos)
  {
    return vos;
  }

  protected void setColumnOrder(String[] column_codes)
  {
    ReportItem[] items = getReportBase().getBody_Items();

    ArrayList al = new ArrayList();
    HashMap tmpHas = new HashMap();
    for (int i = 0; i < items.length; i++)
    {
      tmpHas.put(items[i].getKey(), items[i]);
    }

    for (int i = 0; i < column_codes.length; i++)
    {
      if (!tmpHas.containsKey(column_codes[i]))
        continue;
      al.add(tmpHas.get(column_codes[i]));
      tmpHas.remove(column_codes[i]);
    }

    al.addAll(tmpHas.values());

    ReportItem[] newitems = (ReportItem[])(ReportItem[])al.toArray(new ReportItem[0]);
    getReportBase().setBody_Items(newitems);
  }

  protected void setDigitFormat()
  {
    ReportItem[] items = getReportBase().getBody_Items();

    for (int i = 0; i < items.length; i++)
    {
      if ((items[i].getDataType() != 2) && (items[i].getDataType() != 1))
        continue;
      items[i].setDecimalDigits(2);
    }
  }

  protected void setPrivateButtons()
  {
  }

  protected void unRegisterButton(ButtonObject obj)
  {
    if (obj != null)
      this.button_action_map.remove(obj);
  }

  protected void registerButton(ButtonObject obj, IButtonActionAndState action)
  {
    registerButton(obj, action, -1);
  }

  protected void registerButton(ButtonObject obj, IButtonActionAndState action, int pos)
  {
    if ((obj == null) || (action == null))
    {
      System.out.println("按钮或动作为空,不能加入");
      return;
    }
    if (this.button_action_map.get(obj) != null)
    {
      System.out.println("此按钮已经添加");
      return;
    }
    this.button_action_map.put(obj, action, pos);
  }

  public void showCondition(String[] conditions)
  {
    getConditionPanel().removeAll();
    if ((conditions == null) || (conditions.length == 0)) {
      return;
    }
    String[] temp = conditions;
    UILabel tmp = new UILabel(temp[0]);
    FontMetrics metrics = tmp.getFontMetrics(tmp.getFont());
    int[] widths = new int[conditions.length];
    for (int i = 0; i < conditions.length; i++)
    {
      widths[i] = metrics.stringWidth(conditions[i]);
    }

    Arrays.sort(widths);

    int width = widths[(widths.length - 1)];
    int heigth = metrics.getHeight();
    for (int i = 0; i < conditions.length; i++)
    {
      UILabel l = new UILabel(conditions[i]);

      getConditionPanel().add(l);
      l.setPreferredSize(new Dimension(width, heigth));
    }

    getConditionPanel().invalidate();
    getConditionPanel().repaint();
  }

  public CircularlyAccessibleValueObject[] getBodyDataVO()
  {
    return getReportBase().getBodyDataVO();
  }

  protected void setBodyDataVO(CircularlyAccessibleValueObject[] dataVO, boolean isLoadFormula)
  {
    getReportBase().setBodyDataVO(dataVO, isLoadFormula);
    if (this.needGroup)
    {
      this.needGroup = false;
      onGroup(getUIControl().getGroupKeys());
    }
    updateAllButtons();
  }

  protected TableField[] getVisibleFieldsByDataType(Integer type)
  {
    TableField[] visibleFields = getVisibleFields();
    ArrayList al = new ArrayList();
    ReportModelVO[] vos = getModelVOs();
    for (int i = 0; i < vos.length; i++)
    {
      if (!vos[i].getDataType().equals(type))
        continue;
      TableField f = createTableFieldFromReportModelVO(vos[i]);
      if (!Arrays.asList(visibleFields).contains(f))
        continue;
      al.add(f);
    }

    return (TableField[])(TableField[])al.toArray(new TableField[0]);
  }

  protected ArrayList getGroupKeys()
  {
    return this.groupKeys;
  }

  public boolean contains(String[] source, String element)
  {
    if (source != null) {
      for (int i = 0; i < source.length; i++)
      {
        if (source[i].equals(element))
          return true;
      }
    }
    return false;
  }

  private UISplitPane getVeriSplitPane()
  {
    if (this.veriSplitPane == null)
    {
      UIScrollPane scrollPane = new UIScrollPane();
      scrollPane.setViewportView(getGroupTable());
      this.veriSplitPane = new UISplitPane(1, false, scrollPane, getReportBase());

      setVeriSplitEnabled(false);
    }
    return this.veriSplitPane;
  }
}