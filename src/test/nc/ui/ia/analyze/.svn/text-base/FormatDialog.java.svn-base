package nc.ui.ia.analyze;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.table.TableColumnModel;
import nc.ui.ia.pub.IATableModel;
import nc.ui.ia.pub.IATableSelectionEvent;
import nc.ui.ia.pub.IAUITable;
import nc.ui.ia.pub.IIATableSelectionListener;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITablePane;
import nc.vo.ia.analyze.StatisticsVO;
import nc.vo.ia.pub.Log;

public class FormatDialog extends UIDialog
  implements ActionListener, IIATableSelectionListener
{
  private static final long serialVersionUID = 1L;
  private UIButton ivjUIButtonCancel = null;
  private JPanel ivjUIDialogContentPane = null;
  private UIButton ivjUIButtonOK = null;
  private IAUITable m_tUITable = null;
  private IATableModel m_mTableModel = null;

  private UIPanel ivjUIPanel = null;

  private UIButton ivjUIButtonDown = null;
  private UIButton ivjUIButtonUP = null;
  private UITablePane ivjUITablePane = null;

  private Object[][] m_allChoices = (Object[][])null;

  private String[] m_arysColNames = { NCLangRes.getInstance().getStrByID("201490", "UPP201490-000002"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000003"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000004"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000005"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000006") };

  private boolean m_bInvclSelect = false;
  private boolean m_bInvSelect = false;
  private int m_iCodePos = 3;
  private int m_iCrossFlag = 0;
  private int m_iLastRow = 0;
  private int m_iMaxSelect = 10;
  private int m_iMinSelect = 1;

  private Vector<StatisticsVO> m_vChoice = new Vector();

  private StatisticsVO[] m_voaStatistics = null;

  public FormatDialog(Container parent)
  {
    super(parent);
    initialize((Object[][])null);
  }

  public FormatDialog(Container parent, String title)
  {
    super(parent, title);
    initialize((Object[][])null);
  }

  public FormatDialog(Frame owner)
  {
    super(owner);
    initialize((Object[][])null);
  }

  public FormatDialog(Frame owner, String title)
  {
    super(owner, title);
    initialize((Object[][])null);
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == getUIButtonCancel()) {
      closeCancel();
    }
    if (e.getSource() == getUIButtonOK()) {
      onButtonOKClicked();
    }
    if (e.getSource() == getUIButtonUP()) {
      onButtonUPClicked();
    }
    if (e.getSource() == getUIButtonDown())
      onButtonDownClicked();
  }

  private UIButton getUIButtonCancel()
  {
    if (this.ivjUIButtonCancel == null) {
      try {
        this.ivjUIButtonCancel = new UIButton();
        this.ivjUIButtonCancel.setName("UIButtonCancel");
        this.ivjUIButtonCancel.setToolTipText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"));

        this.ivjUIButtonCancel.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"));

        this.ivjUIButtonCancel.setPreferredSize(new Dimension(60, 22));

        this.ivjUIButtonCancel.addActionListener(this);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonCancel;
  }

  private UIButton getUIButtonOK()
  {
    if (this.ivjUIButtonOK == null) {
      try {
        this.ivjUIButtonOK = new UIButton();
        this.ivjUIButtonOK.setName("UIButtonOK");
        this.ivjUIButtonOK.setToolTipText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044"));

        this.ivjUIButtonOK.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044"));

        this.ivjUIButtonOK.setPreferredSize(new Dimension(60, 22));
        this.ivjUIButtonOK.addActionListener(this);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonOK;
  }

  private JPanel getUIDialogContentPane()
  {
    if (this.ivjUIDialogContentPane == null) {
      try {
        this.ivjUIDialogContentPane = new JPanel();
        this.ivjUIDialogContentPane.setName("UIDialogContentPane");
        this.ivjUIDialogContentPane.setLayout(new BorderLayout());
        getUIDialogContentPane().add(getUIPanel(), "South");
        getUIDialogContentPane().add(getUITablePane(), "Center");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIDialogContentPane;
  }

  private void handleException(Throwable exception)
  {
    Log.error(exception);
  }

  private UIPanel getUIPanel()
  {
    if (this.ivjUIPanel == null) {
      try {
        this.ivjUIPanel = new UIPanel();
        this.ivjUIPanel.setName("UIPanel");
        this.ivjUIPanel.add(getUIButtonUP(), getUIButtonUP().getName());
        this.ivjUIPanel.add(getUIButtonDown(), getUIButtonDown().getName());
        this.ivjUIPanel.add(getUIButtonOK(), getUIButtonOK().getName());
        this.ivjUIPanel.add(getUIButtonCancel(), getUIButtonCancel().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIPanel;
  }

  public FormatDialog(Container parent, Object[][] data, int maxRow, int minRow, int flag)
  {
    super(parent);
    this.m_iMaxSelect = maxRow;
    this.m_iMinSelect = minRow;
    this.m_iCrossFlag = flag;
    initialize(data);
  }

  public void afterEdit(IATableSelectionEvent e)
  {
    int i = e.getRow();
    int column = e.getColumn();
    Object check = this.m_mTableModel.getValueAt(i, column);
    int iCrossPos = -1;
    if (this.m_iCrossFlag != 0) {
      iCrossPos = this.m_tUITable.getColumnModel().getColumnIndex(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000004"));
    }

    if (this.m_iMaxSelect == 1) {
      if (i == this.m_iLastRow) {
        this.m_iLastRow = -1;
      }
      if (this.m_iLastRow != -1)
      {
        this.m_mTableModel.setValueAt(Boolean.FALSE, this.m_iLastRow, 0);
      }
      this.m_iLastRow = i;
    }

    if ((column == 0) && 
      (check != null)) {
      if (check.equals(Boolean.TRUE)) {
        int allrow = getSelectRow(0) - 1;
        if (allrow >= this.m_iMaxSelect) {
          this.m_mTableModel.setValueAt(Boolean.FALSE, i, 0);
          String[] value = { String.valueOf(allrow) };

          MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000007", null, value));

          return;
        }

        return;
      }

      if ((check.equals(Boolean.FALSE)) && (this.m_iCrossFlag != 0)) {
        int allrow = getSelectRow(0);
        if (allrow <= 1) {
          int cou = this.m_mTableModel.getRowCount();
          for (int j = 0; j < cou; j++) {
            if (!this.m_mTableModel.getValueAt(j, iCrossPos).equals(Boolean.TRUE))
              continue;
            this.m_mTableModel.setValueAt(Boolean.FALSE, j, iCrossPos);
          }
        }

        this.m_mTableModel.setValueAt(Boolean.FALSE, i, iCrossPos);
        return;
      }
    }

    if (column == iCrossPos) {
      int allrow = getSelectRow(0);
      if (allrow <= 1) {
        this.m_mTableModel.setValueAt(Boolean.FALSE, i, column);
      }
      else if ((check != null) && 
        (check.equals(Boolean.TRUE)))
        if (this.m_mTableModel.getValueAt(i, 0).equals(Boolean.TRUE)) {
          if (getSelectRow(column) > 1) {
            this.m_mTableModel.setValueAt(Boolean.FALSE, i, column);
            MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000009"));
          }

        }
        else
        {
          this.m_mTableModel.setValueAt(Boolean.FALSE, i, column);
        }
    }
  }

  private void createChoiceVector()
  {
    int iRowCount = this.m_mTableModel.getRowCount();
    this.m_vChoice.removeAllElements();
    StatisticsVO voRow = null;
    this.m_bInvSelect = false;
    this.m_bInvclSelect = false;

    int iCross = -1;
    for (int i = 0; i < iRowCount; i++) {
      if (this.m_mTableModel.getValueAt(i, 0).equals(Boolean.TRUE)) {
        if (this.m_mTableModel.getValueAt(i, 2).equals(Boolean.TRUE)) {
          iCross = i;
        }
        else {
          voRow = new StatisticsVO();
          voRow.setCross(false);
          voRow.setFieldName(this.m_mTableModel.getValueAt(i, 1).toString());
          voRow.setFieldCode(this.m_mTableModel.getValueAt(i, this.m_iCodePos).toString());

          this.m_vChoice.add(voRow);
          if (voRow.getFieldName().equals(NCLangRes.getInstance().getStrByID("common", "UC000-0001439")))
          {
            this.m_bInvSelect = true;
          } else {
            if (!voRow.getFieldName().equals(NCLangRes.getInstance().getStrByID("common", "UC000-0001443"))) {
              continue;
            }
            this.m_bInvclSelect = true;
          }
        }
      }
    }
    if (iCross >= 0) {
      voRow = new StatisticsVO();
      voRow.setCross(true);
      voRow.setFieldName(this.m_mTableModel.getValueAt(iCross, 1).toString());
      voRow.setFieldCode(this.m_mTableModel.getValueAt(iCross, this.m_iCodePos).toString());

      this.m_vChoice.add(voRow);
      if (voRow.getFieldName().equals(NCLangRes.getInstance().getStrByID("common", "UC000-0001439")))
      {
        this.m_bInvSelect = true;
      }
      else if (voRow.getFieldName().equals(NCLangRes.getInstance().getStrByID("common", "UC000-0001443")))
      {
        this.m_bInvclSelect = true;
      }
    }
  }

  public Vector getSelect()
  {
    return (Vector)this.m_vChoice.clone();
  }

  public int getSelectRow(int column)
  {
    int couRow = 0;
    int cou = this.m_mTableModel.getRowCount();
    for (int i = 0; i < cou; i++) {
      if (this.m_mTableModel.getValueAt(i, column).equals(Boolean.TRUE)) {
        couRow++;
      }
    }
    return couRow;
  }

  public StatisticsVO[] getStatistics()
  {
    if (this.m_vChoice.size() > 0) {
      this.m_voaStatistics = new StatisticsVO[this.m_vChoice.size()];
      for (int i = 0; i < this.m_vChoice.size(); i++) {
        this.m_voaStatistics[i] = ((StatisticsVO)this.m_vChoice.elementAt(i));
      }
    }
    return this.m_voaStatistics;
  }

  private UIButton getUIButtonDown()
  {
    if (this.ivjUIButtonDown == null) {
      try {
        this.ivjUIButtonDown = new UIButton();
        this.ivjUIButtonDown.setName("UIButtonDown");
        this.ivjUIButtonDown.setToolTipText(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000010"));

        this.ivjUIButtonDown.setText(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000010"));

        this.ivjUIButtonDown.setPreferredSize(new Dimension(60, 22));
        this.ivjUIButtonDown.addActionListener(this);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonDown;
  }

  private UIButton getUIButtonUP()
  {
    if (this.ivjUIButtonUP == null) {
      try {
        this.ivjUIButtonUP = new UIButton();
        this.ivjUIButtonUP.setName("UIButtonUP");
        this.ivjUIButtonUP.setToolTipText(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000011"));

        this.ivjUIButtonUP.setText(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000011"));

        this.ivjUIButtonUP.setPreferredSize(new Dimension(60, 22));
        this.ivjUIButtonUP.addActionListener(this);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIButtonUP;
  }

  public IAUITable getUITabel()
  {
    return this.m_tUITable;
  }

  private UITablePane getUITablePane()
  {
    if (this.ivjUITablePane == null) {
      try {
        this.ivjUITablePane = new UITablePane();
        this.ivjUITablePane.setName("UITablePane");

        this.m_tUITable = new IAUITable();
        this.ivjUITablePane.setTable(this.m_tUITable);
        this.m_mTableModel = new IATableModel();
        this.m_tUITable.setModel(this.m_mTableModel);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUITablePane;
  }

  public void IATableSelectionChanged(IATableSelectionEvent e)
  {
  }

  private void initialize(Object[][] data)
  {
    try
    {
      this.m_allChoices = data;

      setName("FormatDialog");
      setDefaultCloseOperation(2);
      setSize(361, 266);
      setTitle(NCLangRes.getInstance().getStrByID("201490", "UPP201490-000001"));

      setContentPane(getUIDialogContentPane());
    }
    catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    this.m_mTableModel.setDataVector(this.m_allChoices, this.m_arysColNames);
    this.m_mTableModel.setEditableRowColumn(-99, 0);
    this.m_mTableModel.setEditableRowColumn(-99, 2);
    this.m_tUITable.addIATableSelectionListener(this);
    this.m_tUITable.removeColumn(this.m_tUITable.getColumnModel().getColumn(4));
    this.m_tUITable.removeColumn(this.m_tUITable.getColumnModel().getColumn(3));
    if (this.m_iCrossFlag == 0) {
      this.m_tUITable.removeColumn(this.m_tUITable.getColumnModel().getColumn(2));
      this.m_iCodePos = 2;
    }

    this.m_tUITable.setSelectionMode(0);

    createChoiceVector();
  }

  public boolean isInvclSelect()
  {
    return this.m_bInvclSelect;
  }

  public boolean isInvSelect()
  {
    return this.m_bInvSelect;
  }

  private void onButtonDownClicked()
  {
    int row = this.m_tUITable.getSelectedRow();
    if (row < this.m_mTableModel.getRowCount() - 1)
    {
      int cou = this.m_mTableModel.getColumnCount();
      for (int i = 0; i < cou; i++) {
        Object temp = this.m_mTableModel.getValueAt(row + 1, i);
        this.m_mTableModel.setValueAt(this.m_mTableModel.getValueAt(row, i), row + 1, i);

        this.m_mTableModel.setValueAt(temp, row, i);
      }
      this.m_tUITable.setRowSelectionInterval(row + 1, row + 1);
    }
  }

  private void onButtonOKClicked()
  {
    createChoiceVector();

    if (this.m_vChoice.size() < this.m_iMinSelect) {
      String[] value = { String.valueOf(this.m_iMinSelect) };

      MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270"), NCLangRes.getInstance().getStrByID("201490", "UPP201490-000012", null, value));

      return;
    }

    closeOK();
  }

  private void onButtonUPClicked()
  {
    int row = this.m_tUITable.getSelectedRow();
    if (row > 0)
    {
      int cou = this.m_mTableModel.getColumnCount();
      for (int i = 0; i < cou; i++) {
        Object temp = this.m_mTableModel.getValueAt(row - 1, i);
        this.m_mTableModel.setValueAt(this.m_mTableModel.getValueAt(row, i), row - 1, i);

        this.m_mTableModel.setValueAt(temp, row, i);
      }
      this.m_tUITable.setRowSelectionInterval(row - 1, row - 1);
    }
  }

  public void setStatistics(StatisticsVO[] newStatistics)
  {
    this.m_voaStatistics = newStatistics;
  }
}