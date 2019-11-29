package nc.ui.gl.fc.rule;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nc.itf.gl.fc.rule.IFCRuleProxy;
import nc.ui.bd.b02.AccsubjBookBO_Client;
import nc.ui.bd.util.XTablePane;
import nc.ui.gl.accsubjref.AccsubjRefPane;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.formulaparse.FormulaParse;
import nc.vo.bd.b02.AccsubjVO;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.gl.fc.rule.FCRuleVO;
import nc.vo.gl.fc.rule.FcconvrefVO;
import nc.vo.gl.fc.rule.Fcrulebd;
import nc.vo.gl.fc.rule.PipeContext;
import nc.vo.gl.fc.rule.SoblinkVO;
import nc.vo.glcom.tools.GLPubProxy;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.util.table.TableDetail;
import nc.vo.util.table.XTableModel;

public class AccsubjRelation extends UIPanel
  implements IUiPanel, CellEditorListener, DataItemMap
{
  private UIPanel ParamPanel = null;

  private UICheckBox MatchByCode = null;

  private UIRadioButton chkDefaultAccsubj = null;

  private UIRadioButton chekErro = null;
  private XTablePane billpane;
  private AccsubjRefPane DefaultAccusbjRef = null;

  UILabel tip_rulename = new UILabel("tip_rulename");

  UILabel text_rulename = new UILabel("rulename");

  UILabel tip_srcbook = new UILabel("tip_srcbook");

  UILabel text_srcbook = new UILabel("srcbook");

  UILabel tip_desbook = new UILabel("tip_desbook");

  UILabel text_desbook = new UILabel("des_book");

  UILabel tip_handle_style = new UILabel("tip_handle_style");
  IParent2 m_parent;
  SoblinkVO linkinfo;
  FCRuleVO rulevo;
  HashSet srcSet = new HashSet(100);

  HashMap srcMap = null;

  HashMap desMap = null;

  AccsubjVO[] srcAV = null;

  String srcOrgBook = null;

  int maxRefCount = 0;

  Vector deleteRef = new Vector();

  boolean isforDisplay = false;

  String[] formula_edit = { "accsubjkey->getColValue(bd_accsubj,pk_accsubj,pk_accsubj,accsubjkey);", "accsubjname->getColValue(bd_accsubj,subjname ,pk_accsubj,accsubjkey);", "accsubjcode->getColValue(bd_accsubj,subjcode ,pk_accsubj,accsubjkey)" };

  String[] formula_load = { "accsubjname->getColValue(bd_accsubj,subjname,pk_accsubj,accsubjkey);", "accsubjcode->getColValue(bd_accsubj,subjcode,pk_accsubj,accsubjkey)" };

  FormulaParseFather fp = new FormulaParse();

  FCAccsubjTreeFilter filter = new FCAccsubjTreeFilter();

  ButtonObject btn_add = new ButtonObject(NCLangRes.getInstance().getStrByID("20021101", "UC001-0000002"), NCLangRes.getInstance().getStrByID("20021101", "UC001-0000002"), -1, "增加");

  ButtonObject btn_delete = new ButtonObject(NCLangRes.getInstance().getStrByID("20021101", "UC001-0000039"), NCLangRes.getInstance().getStrByID("20021101", "UC001-0000039"), -1, "删除");

  ButtonObject btn_import = new ButtonObject(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000027"), NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000027"), -1, "导入");

  ButtonObject btn_match = new ButtonObject(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000028"), NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000028"), -1, "自动匹配");

  ButtonObject btn_previous = new ButtonObject(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000029"), NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000029"), -1, "上一步");

  ButtonObject btn_finishPipe = new ButtonObject(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000030"), NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000030"), -1, "完成");

  ButtonObject btn_cancel = new ButtonObject(NCLangRes.getInstance().getStrByID("20021101", "UC001-0000008"), NCLangRes.getInstance().getStrByID("20021101", "UC001-0000008"), -1, "完成");
  ButtonObject[] btns;
  RadioBtnGroup btg_handle = new RadioBtnGroup();
  XTableModel model;
  AccsubjCellEditor src_editor = null;

  AccsubjCellEditor des_editor = null;

  PipeContext context = null;

  public void editingCanceled(ChangeEvent e)
  {
    int i = 0;
  }

  public void editingStopped(ChangeEvent e)
  {
    AccsubjCellEditor editor = (AccsubjCellEditor)e.getSource();
    int row = editor.getrow();
    int col = editor.getCol();

    Object key = editor.getCellEditorValue();
    Object oldvalue = getDataItemTrans(row, col);

    if (oldvalue != null) {
      this.srcSet.remove(oldvalue);
    }

    if ((key != null) && (col == 0))
    {
      for (int i = 0; i < this.model.getRowCount(); i++) {
        if ((key.equals(this.model.getValueAt(i, col == 0 ? "pk_src" : "pk_des"))) && (row != i)) {
          key = null;
          break;
        }
      }
    }
    String value = key == null ? null : key.toString();

    if (value == null) {
      this.model.setValueAt(null, row, col == 0 ? "srccode" : "descode");
      this.model.setValueAt(null, row, col == 0 ? "srcname" : "desname");
      this.model.setValueAt(null, row, col == 0 ? "pk_src" : "pk_des");
    } else {
      this.fp.setExpressArray(this.formula_load);
      Hashtable table = new Hashtable();
      table.put("accsubjkey", value);
      this.fp.setData(table);
      String[][] rs = this.fp.getValueSArray();
      int len = rs.length;

      this.model.setValueAt(rs[0][0], row, col == 0 ? "srcname" : "desname");
      this.model.setValueAt(rs[1][0], row, col == 0 ? "srccode" : "descode");
      this.model.setValueAt(value, row, col == 0 ? "pk_src" : "pk_des");

      this.srcSet.add(value);
    }
  }

  public Object getDataItemTrans(int row, int col)
  {
    Object value = null;
    if (col == 0)
      value = this.model.getValueAt(row, "pk_src");
    else if (col == 2)
      value = this.model.getValueAt(row, "pk_des");
    else {
      value = this.model.getValueAt(row, col);
    }
    return value;
  }

  public AccsubjRelation()
  {
    initialize();
  }

  private void initialize()
  {
    setLayout(new BorderLayout());
    setSize(774, 419);
    this.tip_rulename.setBounds(5, 5, 69, 22);
    this.tip_rulename.setText(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000016"));

    this.text_rulename.setBounds(70, 5, 178, 22);
    this.tip_srcbook.setBounds(250, 5, 52, 22);
    this.tip_srcbook.setText(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000017"));

    this.text_srcbook.setBounds(304, 5, 193, 22);
    this.tip_desbook.setBounds(499, 5, 60, 22);
    this.tip_desbook.setText(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000018"));

    this.text_desbook.setBounds(558, 5, 212, 22);
    this.tip_handle_style.setText(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000019"));

    this.tip_handle_style.setBounds(6, 42, 113, 22);
    add(getParamPanel(), "North");
    add(getbillpane(), "Center");

    initTable();
    initControl();
  }

  public void addListener(Object objListener, Object objUserdata)
  {
  }

  public ButtonObject[] getButtons()
  {
    if (this.btns == null) {
      this.btns = new ButtonObject[] { this.btn_add, this.btn_delete, this.btn_import, this.btn_match, this.btn_previous, this.btn_finishPipe, this.btn_cancel };
    }
    return this.btns;
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000020");
  }

  public Object invoke(Object objData, Object objUserData)
  {
    return null;
  }

  public void nextClosed()
  {
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.btn_previous)
      onPrevious();
    else if (bo == this.btn_finishPipe)
      onFinishPipe();
    else if (bo == this.btn_cancel)
      onCancel();
    else if (bo == this.btn_add)
      onAdd();
    else if (bo == this.btn_delete)
      onDelete();
    else if (bo == this.btn_import)
      onImport();
    else if (bo == this.btn_match)
      onMatch();
  }

  public void removeListener(Object objListener, Object objUserdata)
  {
  }

  public void showMe(IParent parent)
  {
    parent.getUiManager().removeAll();
    parent.getUiManager().add(this, getName());
    this.m_parent = ((IParent2)parent);
  }

  private UIPanel getParamPanel()
  {
    if (this.ParamPanel == null) {
      this.ParamPanel = new UIPanel();
      this.ParamPanel.setLayout(null);
      this.ParamPanel.setPreferredSize(new Dimension(10, 70));
      this.ParamPanel.add(getMatchByCode(), null);
      this.ParamPanel.add(getChkDefaultAccsubj(), null);
      this.ParamPanel.add(getChekErro(), null);
      this.ParamPanel.add(getDefaultAccusbjRef(), null);
      this.ParamPanel.add(this.tip_rulename, null);
      this.ParamPanel.add(this.text_rulename, null);
      this.ParamPanel.add(this.tip_srcbook, null);
      this.ParamPanel.add(this.text_srcbook, null);
      this.ParamPanel.add(this.tip_desbook, null);
      this.ParamPanel.add(this.text_desbook, null);
      this.ParamPanel.add(this.tip_handle_style, null);
    }
    return this.ParamPanel;
  }

  private UICheckBox getMatchByCode()
  {
    if (this.MatchByCode == null) {
      this.MatchByCode = new UICheckBox();
      this.MatchByCode.setText(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000021"));

      this.MatchByCode.setBounds(477, 41, 105, 22);
    }
    return this.MatchByCode;
  }

  private UIRadioButton getChkDefaultAccsubj()
  {
    if (this.chkDefaultAccsubj == null) {
      this.chkDefaultAccsubj = new UIRadioButton();
      this.chkDefaultAccsubj.setText(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000022"));

      this.chkDefaultAccsubj.setBounds(119, 42, 105, 22);
    }
    return this.chkDefaultAccsubj;
  }

  private UIRadioButton getChekErro()
  {
    if (this.chekErro == null) {
      this.chekErro = new UIRadioButton();
      this.chekErro.setText(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000023"));

      this.chekErro.setBounds(331, 42, 145, 22);
    }
    return this.chekErro;
  }

  private XTablePane getbillpane()
  {
    if (this.billpane == null) {
      this.billpane = new XTablePane();
      this.billpane.setName("billpane");
    }
    return this.billpane;
  }

  private AccsubjRefPane getDefaultAccusbjRef()
  {
    if (this.DefaultAccusbjRef == null) {
      this.DefaultAccusbjRef = new AccsubjRefPane();
      this.DefaultAccusbjRef.setBounds(228, 42, 100, 22);
      this.DefaultAccusbjRef.setNotLeafSelectedEnabled(false);
      this.DefaultAccusbjRef.setMultilSelectable(false);
      this.DefaultAccusbjRef.setTreefilter(this.filter);
    }
    return this.DefaultAccusbjRef;
  }

  public boolean isForDisplay()
  {
    return this.isforDisplay;
  }

  public void setIsforDisplay(boolean isforDisplay)
  {
    this.isforDisplay = isforDisplay;
  }

  void onFinishPipe()
  {
    fillRule(this.rulevo);
    String rs = verify();
    if (!"OK".equals(rs)) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20021102", "UPP20021102-000019"), rs);
      return;
    }
    try
    {
      if (this.rulevo.getState() == 2) {
        GLPubProxy.getRemoteFCRuleProxy().addFCRule(this.rulevo);
      }
      else
      {
        GLPubProxy.getRemoteFCRuleProxy().modifyFCRule(this.rulevo);
      }

      this.m_parent.endPipe();
    } catch (Exception ex) {
      String msg = ex.getMessage() != null ? NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000024") : ex.getMessage().length() != 0 ? ex.getMessage() : NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000024");

      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("20021102", "UPP20021102-000019"), msg);
      ex.printStackTrace();
    }
  }

  void onPrevious()
  {
    this.m_parent.closeMe();
  }

  void onCancel()
  {
    int i = MessageDialog.showYesNoCancelDlg(this, NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000177"), NCLangRes.getInstance().getStrByID("20021102", "UPP20021102-000117"));
    if (i == 4)
      onFinishPipe();
    else if (i == 2)
      return;
    this.m_parent.cancelPipe();
  }

  String verify()
  {
    String rs = "OK";
    String erro = "";
    boolean automatch = this.MatchByCode.isSelected();
    int index = 1;

    int count = 0;
    int size = this.model.getRowCount();
    for (int i = 0; i < size; i++) {
      if ((this.model.getValueAt(i, "pk_src") != null) && (this.model.getValueAt(i, "pk_des") != null)) {
        count++;
      }
    }
    if ((count == 0) && (!automatch)) {
      erro = erro + index++ + NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000025");
    }

    if ((this.rulevo.getRule().getSubjmatcherro().intValue() == 0) && (this.rulevo.getRule().getDefaultsubj() == null)) {
      erro = erro + index++ + NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000026");
    }

    return erro.length() == 0 ? rs : erro;
  }

  void changeRule(PipeContext pipeContext) throws Exception {
    this.context = pipeContext;
    this.linkinfo = ((SoblinkVO)this.context.getClientProperty("Soblink"));
    this.rulevo = ((FCRuleVO)this.context.getClientProperty("Rule"));
    changeRule(this.linkinfo, this.rulevo);
  }

  void changeRule(SoblinkVO link, FCRuleVO rule) throws Exception {
    this.linkinfo = link;
    this.rulevo = rule;
    if (!link.getPk_srcbook().equals(this.srcOrgBook)) {
      this.srcOrgBook = link.getPk_srcbook();
      this.srcAV = null;
    }
    this.srcSet.clear();
    this.deleteRef.clear();
    showRule();

    if (this.isforDisplay)
      updateStatus();
  }

  void showRule()
    throws Exception
  {
    this.model.clear();

    this.text_rulename.setText(this.rulevo.getRule().getRulename());
    this.text_srcbook.setText(this.linkinfo.getSrcorgbookname());
    this.text_desbook.setText(this.linkinfo.getDesorgbookname());

    ClientEnvironment env = ClientEnvironment.getInstance();

    AccperiodVO desperiod = null;
    AccperiodVO srcperiod = null;

    desperiod = FCUtil.getDesPeriod(this.linkinfo.getPk_desbook(), env.getBusinessDate());
    srcperiod = FCUtil.getDesPeriod(this.linkinfo.getPk_srcbook(), env.getBusinessDate());

    if (desperiod == null) {
      throw new Exception(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000238"));
    }
    if (srcperiod == null) {
      throw new Exception(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000239"));
    }

    getDes_editor().setPk_glorgbook(this.linkinfo.getPk_desbook(), desperiod.getPeriodyear(), desperiod.getVosMonth()[0].getMonth());
    getSrc_editor().setPk_glorgbook(this.linkinfo.getPk_srcbook(), srcperiod.getPeriodyear(), srcperiod.getVosMonth()[0].getMonth());

    Vector v = this.rulevo.getRef_accsubj();
    int size = null == v ? 0 : v.size();

    if (size > 0) {
      FcconvrefVO[] vos = new FcconvrefVO[size];
      v.copyInto(vos);
      HashMap map = new HashMap();

      List src = new ArrayList();
      List des = new ArrayList();

      for (int i = 0; i < size; i++) {
        src.add(vos[i].getPk_src());
        this.srcSet.add(vos[i].getPk_src());
        des.add(vos[i].getPk_des());
      }

      map.put("accsubjkey", src);
      this.fp.setExpressArray(this.formula_load);
      this.fp.setDataSArray(map);

      String[][] rs = this.fp.getValueSArray();
      for (int i = 0; i < size; i++) {
        vos[i].setSrcname(rs[0][i]);
        vos[i].setSrccode(rs[1][i]);
      }
      this.fp.setExpressArray(this.formula_load);
      map.put("accsubjkey", des);
      this.fp.setDataSArray(map);
      rs = this.fp.getValueSArray();
      for (int i = 0; i < size; i++) {
        vos[i].setDesname(rs[0][i]);
        vos[i].setDescode(rs[1][i]);
      }
      this.model.setBoyVos(vos);
    }
    else {
      this.model.clear();
    }
    this.maxRefCount = size;

    boolean subjcodesame = null == this.rulevo.getRule().getSubjcodesame() ? false : this.rulevo.getRule().getSubjcodesame().booleanValue();
    getMatchByCode().setSelected(subjcodesame);
    try
    {
      getDefaultAccusbjRef().setPk_GlOrgBook("2", this.linkinfo.getPk_desbook(), desperiod.getPeriodyear(), desperiod.getVosMonth()[0].getMonth());
    } catch (Exception ex) {
      getDefaultAccusbjRef().setPK(null);
      throw new Exception(NCLangRes.getInstance().getStrByID("20021102", "UPP20021102-000036"));
    }

    if (this.rulevo.getRule().getDefaultsubj() != null)
      try {
        getDefaultAccusbjRef().setPK(this.rulevo.getRule().getDefaultsubj());
      } catch (Exception ex) {
        getDefaultAccusbjRef().setPK(this.rulevo.getRule().getDefaultsubj());
      }
    else {
      getDefaultAccusbjRef().setPK(null);
    }

    int subjmatch = null == this.rulevo.getRule().getSubjmatcherro() ? 0 : this.rulevo.getRule().getSubjmatcherro().intValue();

    if (this.rulevo.getState() == 1) {
      boolean editable = this.context == null ? false : ((Boolean)this.context.getClientProperty("editable")).booleanValue();
      getChekErro().setEnabled(editable);
      getChkDefaultAccsubj().setEnabled(editable);
      getDefaultAccusbjRef().setEnabled((editable) && (subjmatch == 0));
    } else {
      getDefaultAccusbjRef().setEnabled(subjmatch == 0);
    }

    if (subjmatch == 0)
      this.btg_handle.setSelected(getChkDefaultAccsubj());
    else
      this.btg_handle.setSelected(getChekErro());
  }

  void onImport()
  {
    if (this.srcAV == null) {
      try {
        this.srcAV = AccsubjBookBO_Client.getEndChildrenByCode(this.linkinfo.getPk_srcbook(), null, null);
      } catch (Exception ex) {
        ex.printStackTrace();
      }

    }

    if (this.srcMap == null) {
      this.srcMap = new HashMap();
    }

    int size = null == this.srcAV ? 0 : this.srcAV.length;
    for (int i = 0; i < size; i++) {
      if (!this.srcSet.contains(this.srcAV[i].getPk_accsubj())) {
        FcconvrefVO vo = new FcconvrefVO();
        vo.setContent(new Integer(2));
        vo.setPk_src(this.srcAV[i].getPk_accsubj());
        vo.setPk_soblink(this.linkinfo.getPrimaryKey());
        try {
          vo.setPk_convertrule(this.rulevo.getPrimaryKey());
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        vo.setSrccode(this.srcAV[i].getSubjcode());
        vo.setSrcname(this.srcAV[i].getSubjname());
        this.model.addRow(vo);
        vo.setStatus(2);
        this.srcSet.add(this.srcAV[i].getPk_accsubj());
      }

      if (!this.srcMap.containsKey(this.srcAV[i].getPk_accsubj()))
        this.srcMap.put(this.srcAV[i].getPk_accsubj(), this.srcAV[i].getSubjcode());
    }
  }

  void onMatch()
  {
    this.srcMap = null;
    if (this.srcMap == null) {
      this.srcMap = new HashMap();
      try {
        this.srcAV = AccsubjBookBO_Client.getEndChildrenByCode(this.linkinfo.getPk_srcbook(), null, null);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      int size = null == this.srcAV ? 0 : this.srcAV.length;
      for (int i = 0; i < size; i++) {
        this.srcMap.put(this.srcAV[i].getPk_accsubj(), this.srcAV[i].getSubjcode());
      }
    }
    this.desMap = null;
    if (this.desMap == null) {
      this.desMap = new HashMap();
      AccsubjVO[] desAV = null;
      try {
        desAV = AccsubjBookBO_Client.getEndChildrenByCode(this.linkinfo.getPk_desbook(), null, null);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      int size = null == desAV ? 0 : desAV.length;
      for (int i = 0; i < size; i++) {
        this.desMap.put(desAV[i].getSubjcode(), desAV[i]);
      }
    }

    int rowCount = this.model.getRowCount();
    int oldstate = 0;
    for (int i = 0; i < rowCount; i++) {
      Object des = this.model.getValueAt(i, "pk_des");
      Object src = this.model.getValueAt(i, "pk_src");
      if ((null != src) && (null == des)) {
        AccsubjVO dv = (AccsubjVO)this.desMap.get(this.srcMap.get(src));
        if (dv != null) {
          this.model.setValueAt(dv.getPk_accsubj(), i, "pk_des");
          this.model.setValueAt(dv.getSubjcode(), i, "descode");
          this.model.setValueAt(dv.getSubjname(), i, "desname");
        }
      }
    }
  }

  void onAdd()
  {
    FcconvrefVO vo = new FcconvrefVO();
    vo.setContent(new Integer(1));
    vo.setPk_soblink(this.linkinfo.getPrimaryKey());
    try {
      vo.setPk_convertrule(this.rulevo.getPrimaryKey());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    this.model.addRow(vo);
    vo.setStatus(2);
  }

  void onDelete()
  {
    if (getbillpane().getTable().isEditing()) {
      getbillpane().getTable().getCellEditor().stopCellEditing();
    }

    int[] rowSelected = getbillpane().getTable().getSelectedRows();

    int rowCount = rowSelected != null ? rowSelected.length : 0;
    if (rowCount > 0) {
      rowSelected = FCConvertCenter.sortAscending(rowSelected);
      for (int i = rowCount - 1; i >= 0; i--) {
        Object key = this.model.getValueAt(rowSelected[i], "pk_src");
        if (key != null) {
          this.srcSet.remove(key);
        }
        this.model.deleteRow(rowSelected[i]);
      }
    }
  }

  void initTable()
  {
    TableDetail td = new TableDetail();
    String[] names = { NCLangRes.getInstance().getStrByID("20021101", "UC000-0003072"), NCLangRes.getInstance().getStrByID("20021101", "UC000-0003069"), NCLangRes.getInstance().getStrByID("20021101", "UC000-0003072"), NCLangRes.getInstance().getStrByID("20021101", "UC000-0003069") };

    String[] keys = { "srccode", "srcname", "descode", "desname" };

    Class[] colClasses = { AccsubjRefFlag.class, String.class, AccsubjRefFlag.class, String.class };

    td.setColumnCode(keys);
    td.setColumnName(names);
    td.setColClass(colClasses);
    td.setContenClass(FcconvrefVO.class);

    this.model = new XTableModel() {
      public boolean isCellEditable(int row, int col) {
        return AccsubjRelation.this.isCellEditable(row, col);
      }
    };
    this.model.init(keys, names, colClasses, FcconvrefVO.class);

    getbillpane().getTable().setModel(this.model);
    getbillpane().getTable().setAutoResizeMode(4);

    getSrc_editor().addCellEditorListener(this);

    getDes_editor().addCellEditorListener(this);

    TableColumnModel tcm = getbillpane().getTable().getColumnModel();
    GroupableTableHeader gt = new GroupableTableHeader(tcm);
    ColumnGroup group1 = new ColumnGroup(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000031"));

    group1.add(tcm.getColumn(0));
    tcm.getColumn(0).setCellEditor(getSrc_editor());
    group1.add(tcm.getColumn(1));
    gt.addColumnGroup(group1);
    ColumnGroup group2 = new ColumnGroup(NCLangRes.getInstance().getStrByID("20021101", "UPP20021101-000032"));

    group2.add(tcm.getColumn(2));
    tcm.getColumn(2).setCellEditor(getDes_editor());
    group2.add(tcm.getColumn(3));
    gt.addColumnGroup(group2);
    getbillpane().getTable().setTableHeader(gt);
  }

  void initControl()
  {
    this.btg_handle.add(this.chekErro, null);
    this.btg_handle.add(this.chkDefaultAccsubj, new Component[] { getDefaultAccusbjRef() });
    this.btg_handle.setSelected(this.chekErro);
  }

  boolean isCellEditable(int row, int col) {
    if (this.isforDisplay) {
      return false;
    }

    return (col == 0) || (col == 2);
  }

  public void bodyRowChange(BillEditEvent e)
  {
  }

  void fillRule(FCRuleVO rule)
  {
    CircularlyAccessibleValueObject[] vos = this.model.getBodyChangeVOS();

    Vector v = new Vector();
    int size = vos == null ? 0 : vos.length;

    for (int i = 0; i < size; i++) {
      int rowState = vos[i].getStatus();
      FcconvrefVO vo;
      switch (rowState) {
      case 2:
        if ((vos[i].getAttributeValue("pk_src") == null) || (vos[i].getAttributeValue("pk_des") == null)) continue;
        vo = (FcconvrefVO)vos[i];
        vo.setContent(new Integer(1));
        v.addElement(vo);
        break;
      case 1:
        if ((vos[i].getAttributeValue("pk_src") != null) && (vos[i].getAttributeValue("pk_des") != null)) {
          vo = (FcconvrefVO)vos[i];
          vo.setContent(new Integer(1));
        } else {
          vos[i].setStatus(3);
        }
        v.addElement((FcconvrefVO)vos[i]);
        break;
      case 3:
        v.addElement((FcconvrefVO)vos[i]);
      }
    }

    rule.setRef_accsubj(v);

    Fcrulebd bd = rule.getRule();

    int matchErro = getChkDefaultAccsubj().isSelected() ? 0 : 1;
    bd.setSubjmatcherro(new Integer(matchErro));

    if (matchErro == 0) {
      bd.setDefaultsubj(getDefaultAccusbjRef().getRefPK());
    }

    bd.setSubjcodesame(new UFBoolean(getMatchByCode().isSelected()));
    this.context.putClientProperty("Rule", rule);
  }

  void updateStatus()
  {
    if (this.isforDisplay) {
      Component[] comps = getParamPanel().getComponents();
      int count = null == comps ? 0 : comps.length;
      for (int i = 0; i < count; i++)
        comps[i].setEnabled(false);
    }
  }

  public AccsubjCellEditor getDes_editor()
  {
    if (this.des_editor == null)
      this.des_editor = new AccsubjCellEditor(this);
    return this.des_editor;
  }

  public AccsubjCellEditor getSrc_editor() {
    if (this.src_editor == null)
      this.src_editor = new AccsubjCellEditor(this);
    return this.src_editor;
  }

  class AccsubjRefFlag
  {
    AccsubjRefFlag()
    {
    }
  }
}