package nc.ui.gl.assbalance;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nc.ui.bd.BDGLOrgBookAccessor;
import nc.ui.gl.accbook.BillFormatVO;
import nc.ui.gl.accbook.TableDataModel;
import nc.ui.gl.accbook.TableFormatTackle;
import nc.ui.gl.datacache.GLParaDataCache;
import nc.ui.gl.datacache.GLPeriodDataCatch;
import nc.ui.glcom.displayformattool.ShowContentCenter;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.vo.bd.b02.AccsubjVO;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.gl.accbook.ColFormatVO;
import nc.vo.gl.accbook.CurrTypeConst;
import nc.vo.gl.balancesubjass.BalanceSubjAssBSVO;
import nc.vo.glcom.ass.AssVO;
import nc.vo.glcom.balance.GLQueryObj;
import nc.vo.glcom.balance.GlQryFormatVO;
import nc.vo.glcom.balance.GlQueryVO;
import nc.vo.glcom.glperiod.GlPeriodVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.util.Convertor;

public class BalanceSubjAssUI extends UIPanel
  implements ItemListener, ListSelectionListener
{
  private UIComboBox ivjcbFormat = null;
  private UILabel ivjlbPeriod = null;
  private UILabel ivjlbSubj = null;
  private UILabel ivjUILabel12 = null;
  private UILabel ivjUILabel121 = null;
  private UILabel ivjUILabel123 = null;
  private UILabel ivjUILabel124 = null;
  private UITablePane ivjUITablePane = null;
  private UILabel ivjlbCurrType = null;
  protected UITable fixTable = new UITable();
  private FixTableModel fixModel = new FixTableModel();
  private BillFormatVO format;
  private AccsubjVO m_AccsubjVO = null;
  private TableFormatTackle m_TableFormatTackle = null;
  private UIPanel ivjHeadPanel = null;
  private GlQueryVO m_qryVo;
  private Vector vTempBody = new Vector();
  private TableModel m_TableModel = new TableModel();
  private UILabel UILabel = null;
  private UILabel UILabelOrgNameAndCode = null;
  private Object colWs = null;

  private void initTableForMA()
  {
    UITable table = getUITablePane().getTable();
    TableModel model = getM_TableModel();

    ColFormatVO[] colVOs = null;
    colVOs = getM_TableFormatTackle().getColFormatVOs();
    int j = 0;
    for (int i = 0; i < colVOs.length; i++) {
      if (colVOs[i].isVisiablity()) {
        colVOs[i].setColWidth(((Integer)((Vector)this.colWs).elementAt(j)).intValue());
        j++;
      }
    }

    this.m_TableFormatTackle.setColFormatVO(colVOs);

    model.setFormatVO(this.m_TableFormatTackle);
    this.fixModel.setFormatVO(this.m_TableFormatTackle);

    table.setModel(model);
    TableColumnModel colModel = this.fixTable.getColumnModel();
    this.fixTable.setModel(this.fixModel);

    this.fixModel.fireTableStructureChanged();
    model.fireTableStructureChanged();

    this.m_TableFormatTackle.setColWidth(table);
    this.m_TableFormatTackle.setVisiablity(table);
    this.m_TableFormatTackle.setMultiHead(table);

    this.m_TableFormatTackle.setFixColWidth(this.fixTable);
    this.m_TableFormatTackle.setFixVisiablity(this.fixTable);
    this.m_TableFormatTackle.setFixMultiHead(this.fixTable);

    this.fixTable.setAutoResizeMode(0);

    getUITablePane().setRowHeaderView(this.fixTable);
    getUITablePane().setCorner("UPPER_LEFT_CORNER", this.fixTable.getTableHeader());
    table.setAutoResizeMode(0);
    table.setSelectionMode(0);
    this.fixTable.setSelectionMode(0);
  }
  private String getOrgNames(GlQueryVO qryVO) { String[] strPkorgbooks = qryVO.getPk_glorgbook();
    String strorgName = "";
    GlorgbookVO[] orgbookVOs = new GlorgbookVO[strPkorgbooks.length];
    try {
      for (int i = 0; i < strPkorgbooks.length; i++) {
        orgbookVOs[i] = BDGLOrgBookAccessor.getGlOrgBookVOByPrimaryKey(strPkorgbooks[i]);
        strorgName = strorgName + "," + orgbookVOs[i].getGlorgbookcode() + orgbookVOs[i].getGlorgbookname();
      }
    } catch (Exception ee) {
      handleException(ee);
    }
    strorgName = strorgName.substring(1);

    return strorgName; }

  protected TableFormatTackle getM_TableFormatTackle()
  {
    return this.m_TableFormatTackle;
  }
  protected void setM_TableFormatTackle(TableFormatTackle newM_TableFormatTackle) {
    this.m_TableFormatTackle = newM_TableFormatTackle;
  }

  protected Object getColWs()
  {
    return this.colWs;
  }

  protected void setColWs(Object colWs)
  {
    this.colWs = colWs;
  }

  public BalanceSubjAssUI()
  {
    initialize();
  }

  public BalanceSubjAssUI(LayoutManager p0)
  {
    super(p0);
  }

  public BalanceSubjAssUI(LayoutManager p0, boolean p1)
  {
    super(p0, p1);
  }

  public BalanceSubjAssUI(boolean p0)
  {
    super(p0);
  }

  private UIComboBox getcbFormat()
  {
    if (this.ivjcbFormat == null) {
      try {
        this.ivjcbFormat = new UIComboBox();
        this.ivjcbFormat.setName("cbFormat");
        this.ivjcbFormat.setBounds(648, 30, 71, 22);

        this.ivjcbFormat.addItem(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000125"));
        this.ivjcbFormat.addItem(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000126"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjcbFormat;
  }

  private String[] getColData(int iKey)
  {
    UITable table = getUITablePane().getTable();
    TableModel model = (TableModel)table.getModel();

    ColFormatVO[] cols = this.m_TableFormatTackle.getColFormatVOs();

    int iCol = -1;

    String[] strRet = new String[model.getRowCount()];

    for (int i = 0; i < strRet.length; i++) {
      if (iKey == 0) {
        try {
          Object objMark = model.getData()[i].getValue(90);

          if (objMark == null)
          {
            AssVO[] aAssVO = model.getData()[i].getAssVOs();
            strRet[i] = "";
            for (int j = 0; j < aAssVO.length; j++)
              strRet[i] = (strRet[i] + "[" + aAssVO[j].getCheckvaluename() + "]");
          }
          else
          {
            strRet[i] = objMark.toString();
          }
        } catch (Exception e) {
          handleException(e);
        }

      }
      else if (iKey == 1000) {
        strRet[i] = "";
      }
      else {
        for (int j = 0; (iCol == -1) && (j < cols.length); j++) {
          if (cols[j].getColKey() == iKey) {
            iCol = j - this.m_TableFormatTackle.getFixedColSize();
            break;
          }
        }

        Object o = model.getValueAt(i, iCol);
        strRet[i] = (o == null ? "" : o.toString());
      }
    }

    return strRet;
  }

  public void getcolWidth()
  {
  }

  public FixTableModel getFixModel()
  {
    return this.fixModel;
  }

  public BillFormatVO getFormat()
  {
    if (this.format == null)
    {
      this.format = new BillFormatVO();
      this.format.setNumberFormat(true);
    }
    return this.format;
  }

  private UIPanel getHeadPanel()
  {
    if (this.ivjHeadPanel == null) {
      try {
        this.UILabelOrgNameAndCode = new UILabel();
        this.UILabel = new UILabel();
        this.ivjHeadPanel = new UIPanel();
        this.ivjHeadPanel.setName("HeadPanel");
        this.ivjHeadPanel.setPreferredSize(new Dimension(0, 60));
        this.ivjHeadPanel.setLayout(null);
        this.UILabel.setBounds(266, 30, 63, 22);
        this.UILabel.setText(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000127"));
        this.UILabelOrgNameAndCode.setBounds(339, 30, 212, 22);
        this.UILabelOrgNameAndCode.setText("");
        this.UILabelOrgNameAndCode.setBorder(BorderFactory.createEtchedBorder(1));
        getHeadPanel().add(getlbPeriod(), getlbPeriod().getName());
        getHeadPanel().add(getUILabel12(), getUILabel12().getName());
        getHeadPanel().add(getUILabel124(), getUILabel124().getName());
        getHeadPanel().add(getUILabel123(), getUILabel123().getName());
        getHeadPanel().add(getcbFormat(), getcbFormat().getName());
        getHeadPanel().add(getlbSubj(), getlbSubj().getName());
        getHeadPanel().add(getUILabel121(), getUILabel121().getName());
        getHeadPanel().add(getlbCurrType(), getlbCurrType().getName());

        this.ivjHeadPanel.add(this.UILabel, null);
        this.ivjHeadPanel.add(this.UILabelOrgNameAndCode, null);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjHeadPanel;
  }

  private UILabel getlbCurrType()
  {
    if (this.ivjlbCurrType == null) {
      try {
        this.ivjlbCurrType = new UILabel();
        this.ivjlbCurrType.setName("lbCurrType");
        this.ivjlbCurrType.setFont(new Font("dialog", 0, 12));
        this.ivjlbCurrType.setBorder(new EtchedBorder());
        this.ivjlbCurrType.setText("");
        this.ivjlbCurrType.setBounds(619, 6, 100, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjlbCurrType;
  }
  public UILabel getlbCurrTypeForPrint() {
    if (this.ivjlbCurrType == null) {
      try {
        this.ivjlbCurrType = new UILabel();
        this.ivjlbCurrType.setName("lbCurrType");
        this.ivjlbCurrType.setFont(new Font("dialog", 0, 12));
        this.ivjlbCurrType.setBorder(new EtchedBorder());
        this.ivjlbCurrType.setText("");
        this.ivjlbCurrType.setBounds(555, 4, 100, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjlbCurrType;
  }

  public UILabel getlbPeriod()
  {
    if (this.ivjlbPeriod == null) {
      try {
        this.ivjlbPeriod = new UILabel();
        this.ivjlbPeriod.setName("lbPeriod");
        this.ivjlbPeriod.setFont(new Font("dialog", 0, 12));
        this.ivjlbPeriod.setBorder(new EtchedBorder());
        this.ivjlbPeriod.setText("");
        this.ivjlbPeriod.setBounds(55, 30, 182, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjlbPeriod;
  }

  public UILabel getlbSubj()
  {
    if (this.ivjlbSubj == null) {
      try {
        this.ivjlbSubj = new UILabel();
        this.ivjlbSubj.setName("lbSubj");
        this.ivjlbSubj.setFont(new Font("dialog", 0, 12));
        this.ivjlbSubj.setBorder(new EtchedBorder());
        this.ivjlbSubj.setText("");
        this.ivjlbSubj.setBounds(55, 4, 350, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjlbSubj;
  }

  public TableModel getM_TableModel()
  {
    return this.m_TableModel;
  }

  public String[] getPrintData(int iKey)
  {
    switch (iKey) {
    case 45:
      return new String[] { this.m_AccsubjVO.getSubjcode() };
    case 46:
      return new String[] { this.m_AccsubjVO.getDispname() };
    case 1:
    case 2:
    case 104:
      return getColData(iKey);
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    case 16:
    case 17:
    case 18:
    case 19:
    case 20:
    case 21:
    case 22:
    case 23:
    case 24:
    case 25:
    case 26:
    case 27:
    case 28:
    case 29:
    case 30:
    case 31:
    case 32:
    case 33:
    case 34:
    case 35:
    case 36:
    case 60:
    case 61:
    case 62:
    case 63:
    case 64:
    case 65:
    case 66:
    case 67:
    case 90:
      return getColData(iKey);
    case 0:
      return getColData(iKey);
    case 37:
    case 38:
    case 39:
    case 40:
    case 41:
    case 42:
    case 43:
    case 44:
    case 47:
    case 48:
    case 49:
    case 50:
    case 51:
    case 52:
    case 53:
    case 54:
    case 55:
    case 56:
    case 57:
    case 58:
    case 59:
    case 68:
    case 69:
    case 70:
    case 71:
    case 72:
    case 73:
    case 74:
    case 75:
    case 76:
    case 77:
    case 78:
    case 79:
    case 80:
    case 81:
    case 82:
    case 83:
    case 84:
    case 85:
    case 86:
    case 87:
    case 88:
    case 89:
    case 91:
    case 92:
    case 93:
    case 94:
    case 95:
    case 96:
    case 97:
    case 98:
    case 99:
    case 100:
    case 101:
    case 102:
    case 103: } return getColData(1000);
  }

  public int getSelectedRow() {
    return getUITablePane().getTable().getSelectedRow();
  }

  private UILabel getUILabel12()
  {
    if (this.ivjUILabel12 == null) {
      try {
        this.ivjUILabel12 = new UILabel();
        this.ivjUILabel12.setName("UILabel12");
        this.ivjUILabel12.setFont(new Font("dialog", 0, 12));
        this.ivjUILabel12.setText(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000128"));
        this.ivjUILabel12.setBounds(12, 30, 39, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel12;
  }

  private UILabel getUILabel121()
  {
    if (this.ivjUILabel121 == null) {
      try {
        this.ivjUILabel121 = new UILabel();
        this.ivjUILabel121.setName("UILabel121");
        this.ivjUILabel121.setFont(new Font("dialog", 0, 12));
        this.ivjUILabel121.setText(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000129"));
        this.ivjUILabel121.setBounds(12, 4, 39, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel121;
  }

  private UILabel getUILabel123()
  {
    if (this.ivjUILabel123 == null) {
      try {
        this.ivjUILabel123 = new UILabel();
        this.ivjUILabel123.setName("UILabel123");
        this.ivjUILabel123.setFont(new Font("dialog", 0, 12));
        this.ivjUILabel123.setText(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000130"));
        this.ivjUILabel123.setBounds(567, 6, 41, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel123;
  }

  private UILabel getUILabel124()
  {
    if (this.ivjUILabel124 == null) {
      try {
        this.ivjUILabel124 = new UILabel();
        this.ivjUILabel124.setName("UILabel124");
        this.ivjUILabel124.setFont(new Font("dialog", 0, 12));
        this.ivjUILabel124.setText(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000091"));
        this.ivjUILabel124.setBounds(581, 30, 59, 22);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUILabel124;
  }

  protected UITablePane getUITablePane()
  {
    if (this.ivjUITablePane == null) {
      try {
        this.ivjUITablePane = new UITablePane();
        this.ivjUITablePane.setName("UITablePane");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUITablePane;
  }

  private void handleException(Throwable exception)
  {
  }

  private ColFormatVO[] initColFormatVO(GLQueryObj[] bodyQryObjs)
  {
    int colNum = 30;
    if ((bodyQryObjs != null) && (bodyQryObjs.length > 0))
      colNum += bodyQryObjs.length * 2;
    ColFormatVO[] colFormats = new ColFormatVO[colNum];

    int iCol = -1;
    int iCorpDispMode = 0;

    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(90);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000009"));
    colFormats[iCol].setColWidth(55);
    colFormats[iCol].setFrozen(true);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setMultiHeadStr(null);
    colFormats[iCol].setAlignment(2);

    if (this.m_qryVo != null) {
      iCorpDispMode = this.m_qryVo.getFormatVO().getCorpDispLocation();
    }
    if (iCorpDispMode == 1) {
      iCol++;
      colFormats[iCol] = new ColFormatVO();
      colFormats[iCol].setColKey(104);
      colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000131"));
      colFormats[iCol].setColWidth(80);
      colFormats[iCol].setFrozen(true);
      colFormats[iCol].setVisiablity(true);
      colFormats[iCol].setMultiHeadStr(null);
      colFormats[iCol].setAlignment(2);
    }

    if (this.m_qryVo != null) {
      boolean blnHasSubjAccCounted = false;
      GLQueryObj[] allQryObjs = this.m_qryVo.getFormatVO().getQryObjs();

      int i = 0;
      int iCountHeadAss = 0;
      for (int j = 0; j < allQryObjs.length; j++) {
        if (allQryObjs[j].getHeadEle()) {
          if (!allQryObjs[j].getType().equals("会计科目")) {
            iCountHeadAss++;
          }

        }
        else if (allQryObjs[j].getType().equals("会计科目")) {
          iCol++;
          colFormats[iCol] = new ColFormatVO();
          colFormats[iCol].setColKey(45);
          colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000010"));
          colFormats[iCol].setColWidth(60);
          colFormats[iCol].setFrozen(true);
          colFormats[iCol].setVisiablity(true);
          colFormats[iCol].setAlignment(2);
          colFormats[iCol].setMultiHeadStr(null);
          colFormats[iCol].setUserData(new int[] { i, 45 });

          blnHasSubjAccCounted = true;
          iCol++;
          colFormats[iCol] = new ColFormatVO();
          colFormats[iCol].setColKey(46);
          colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000011"));
          colFormats[iCol].setColWidth(100);
          colFormats[iCol].setFrozen(true);
          colFormats[iCol].setVisiablity(true);
          colFormats[iCol].setAlignment(2);
          colFormats[iCol].setMultiHeadStr(null);
          colFormats[iCol].setUserData(new int[] { i, 46 });

          i++;
        }
        else {
          iCol++;
          colFormats[iCol] = new ColFormatVO();
          colFormats[iCol].setColKey(0);
          try {
            AssVO[] ass = (AssVO[])allQryObjs[j].getValueRange();
            colFormats[iCol].setColName(allQryObjs[j].getTypeName() + NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000033"));
          }
          catch (Exception e) {
            colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000132") + (i + 1));
          }
          colFormats[iCol].setColWidth(60);
          colFormats[iCol].setFrozen(true);
          colFormats[iCol].setVisiablity(true);
          colFormats[iCol].setAlignment(2);
          colFormats[iCol].setMultiHeadStr(null);
          if (blnHasSubjAccCounted) {
            colFormats[iCol].setUserData(new int[] { i - 1 + iCountHeadAss, 5 });
          }
          else
          {
            colFormats[iCol].setUserData(new int[] { i + iCountHeadAss, 5 });
          }

          iCol++;

          colFormats[iCol] = new ColFormatVO();
          colFormats[iCol].setColKey(0);
          try {
            AssVO[] ass = (AssVO[])allQryObjs[j].getValueRange();
            colFormats[iCol].setColName(allQryObjs[j].getTypeName() + NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000034"));
          }
          catch (Exception e) {
            colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000133") + (i + 1));
          }
          colFormats[iCol].setColWidth(100);
          colFormats[iCol].setFrozen(true);
          colFormats[iCol].setVisiablity(true);
          colFormats[iCol].setAlignment(2);
          colFormats[iCol].setMultiHeadStr(null);
          if (blnHasSubjAccCounted) {
            colFormats[iCol].setUserData(new int[] { i - 1 + iCountHeadAss, 6 });
          }
          else {
            colFormats[iCol].setUserData(new int[] { i + iCountHeadAss, 6 });
          }

          i++;
        }
      }

    }

    if (iCorpDispMode == 0) {
      iCol++;
      colFormats[iCol] = new ColFormatVO();
      colFormats[iCol].setColKey(104);
      colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000131"));
      colFormats[iCol].setColWidth(80);
      colFormats[iCol].setFrozen(true);
      colFormats[iCol].setVisiablity(true);
      colFormats[iCol].setMultiHeadStr(null);
      colFormats[iCol].setAlignment(2);
    }
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(43);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000485"));
    colFormats[iCol].setColWidth(40);
    colFormats[iCol].setFrozen(true);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setMultiHeadStr(null);
    colFormats[iCol].setAlignment(2);

    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(2);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000012"));
    colFormats[iCol].setColWidth(40);
    colFormats[iCol].setFrozen(true);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(2);
    colFormats[iCol].setMultiHeadStr(null);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(35);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000134"));
    colFormats[iCol].setColWidth(28);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(0);
    colFormats[iCol].setMultiHeadStr(null);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(60);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000050"));
    colFormats[iCol].setColWidth(60);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000459"));
    colFormats[iCol].setDataType(66);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(61);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000051"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000459"));
    colFormats[iCol].setDataType(67);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(62);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000052"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000459"));
    colFormats[iCol].setDataType(68);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(63);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000053"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000459"));
    colFormats[iCol].setDataType(69);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(11);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000050"));
    colFormats[iCol].setColWidth(60);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000044"));
    colFormats[iCol].setDataType(66);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(12);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000051"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000044"));
    colFormats[iCol].setDataType(67);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(13);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000052"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000044"));
    colFormats[iCol].setDataType(68);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(14);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000053"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000044"));
    colFormats[iCol].setDataType(69);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(15);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000050"));
    colFormats[iCol].setColWidth(60);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000045"));
    colFormats[iCol].setDataType(66);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(16);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000051"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000045"));
    colFormats[iCol].setDataType(67);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(17);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000052"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000045"));
    colFormats[iCol].setDataType(68);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(18);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000053"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000045"));
    colFormats[iCol].setDataType(69);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(19);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000050"));
    colFormats[iCol].setColWidth(60);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000046"));
    colFormats[iCol].setDataType(66);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(20);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000051"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000046"));
    colFormats[iCol].setDataType(67);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(21);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000052"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000046"));
    colFormats[iCol].setDataType(68);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(22);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000053"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000046"));
    colFormats[iCol].setDataType(69);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(23);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000050"));
    colFormats[iCol].setColWidth(60);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000047"));
    colFormats[iCol].setDataType(66);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(24);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000051"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000047"));
    colFormats[iCol].setDataType(67);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(25);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000052"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000047"));
    colFormats[iCol].setDataType(68);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(26);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000053"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000047"));
    colFormats[iCol].setDataType(69);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(36);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000134"));
    colFormats[iCol].setColWidth(28);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(0);
    colFormats[iCol].setMultiHeadStr(null);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(64);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000050"));
    colFormats[iCol].setColWidth(60);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000048"));
    colFormats[iCol].setDataType(66);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(65);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000051"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000048"));
    colFormats[iCol].setDataType(67);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(66);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000052"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000048"));
    colFormats[iCol].setDataType(68);
    iCol++;
    colFormats[iCol] = new ColFormatVO();
    colFormats[iCol].setColKey(67);
    colFormats[iCol].setColName(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000053"));
    colFormats[iCol].setColWidth(85);
    colFormats[iCol].setFrozen(false);
    colFormats[iCol].setVisiablity(true);
    colFormats[iCol].setAlignment(4);
    colFormats[iCol].setMultiHeadStr(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000048"));
    colFormats[iCol].setDataType(69);
    return colFormats;
  }

  private void initialize()
  {
    try
    {
      setName("BalanceSubjAssUI");
      setLayout(new BorderLayout());
      setSize(765, 399);
      add(getHeadPanel(), "North");
      add(getUITablePane(), "Center");
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    initTable(null);
    this.fixTable.getTableHeader().addMouseMotionListener(new MyMouseMotionAdapter());
    getUITablePane().getTable().getTableHeader().addMouseMotionListener(new MyMouseMotionAdapter());
    getcbFormat().addItemListener(this);
    getUITablePane().getTable().getSelectionModel().addListSelectionListener(this);
    this.fixTable.getSelectionModel().addListSelectionListener(this);

    BillFormatVO formatVO = getFormat();
    formatVO.setNumberFormat(false);

    resetFormat(formatVO);
    getcbFormat().setSelectedItem(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000126"));
  }

  private void initTable(GLQueryObj[] bodyQryObjs) {
    UITable table = getUITablePane().getTable();
    TableModel model = getM_TableModel();

    ColFormatVO[] colVOs = null;
    colVOs = initColFormatVO(bodyQryObjs);

    this.m_TableFormatTackle = new TableFormatTackle();
    this.m_TableFormatTackle.setColFormatVO(colVOs);

    model.setFormatVO(this.m_TableFormatTackle);
    this.fixModel.setFormatVO(this.m_TableFormatTackle);

    table.setModel(model);
    TableColumnModel colModel = this.fixTable.getColumnModel();
    this.fixTable.setModel(this.fixModel);

    this.fixModel.fireTableStructureChanged();
    model.fireTableStructureChanged();

    this.m_TableFormatTackle.setColWidth(table);
    this.m_TableFormatTackle.setVisiablity(table);
    this.m_TableFormatTackle.setMultiHead(table);

    this.m_TableFormatTackle.setFixColWidth(this.fixTable);
    this.m_TableFormatTackle.setFixVisiablity(this.fixTable);
    this.m_TableFormatTackle.setFixMultiHead(this.fixTable);

    this.fixTable.setAutoResizeMode(0);

    getUITablePane().setRowHeaderView(this.fixTable);
    getUITablePane().setCorner("UPPER_LEFT_CORNER", this.fixTable.getTableHeader());
    table.setAutoResizeMode(0);
    table.setSelectionMode(0);
    this.fixTable.setSelectionMode(0);
  }

  public void itemStateChanged(ItemEvent e)
  {
    if (e.getStateChange() == 1) {
      if (getcbFormat().getSelectedItem().toString().trim().equals(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000126")))
        getFormat().setNumberFormat(false);
      else {
        getFormat().setNumberFormat(true);
      }
      resetFormat(getFormat());
    }
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      BalanceSubjAssUI aBalanceSubjAssUI = new BalanceSubjAssUI();
      frame.setContentPane(aBalanceSubjAssUI);
      frame.setSize(aBalanceSubjAssUI.getSize());
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
      frame.show();
      Insets insets = frame.getInsets();
      frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
      frame.setVisible(true);
    } catch (Throwable exception) {
      System.err.println("nc.ui.pub.beans.UIPanel 的 main() 中发生异常");
      exception.printStackTrace(System.out);
    }
  }

  private void onColResized() {
    this.fixTable.setPreferredScrollableViewportSize(new Dimension(this.fixTable.getColumnModel().getTotalColumnWidth(), this.fixTable.getHeight()));
  }
  private void recoveTableFormat() {
    UITable table = getUITablePane().getTable();
    UITable fixTable = (UITable)(UITable)getUITablePane().getRowHeader().getComponents()[0];
    TableFormatTackle vo = ((TableModel)table.getModel()).getFormatVO();
    ColFormatVO[] colFormats = vo.getColFormatVOs();

    for (int i = 0; i < colFormats.length; i++) {
      colFormats[i].setVisiablity(true);
    }
    vo.setFixVisiablity(fixTable);
    vo.setMultiHead(table);
    vo.setVisiablity(table);
  }

  public void resetFormat(BillFormatVO format)
  {
    ArrayList arr = new ArrayList();
    arr.add(new int[] { 90 });
    if (format.isMultiOrg())
      arr.add(new int[] { 104 });
    if (format.isMultiCurrType()) {
      arr.add(new int[] { 2 });
    }
    if (format.isIsMultiyear()) {
      arr.add(new int[] { 43 });
    }
    arr.add(new int[] { 35 });
    arr.add(new int[] { 36 });
    if (format.isAuxCurrType()) {
      arr.add(new int[] { 62 });
      arr.add(new int[] { 13 });
      arr.add(new int[] { 17 });
      arr.add(new int[] { 21 });
      arr.add(new int[] { 25 });
      arr.add(new int[] { 66 });
    }
    else if (format.isLocCurrType()) {
      arr.add(new int[] { 63 });
      arr.add(new int[] { 14 });
      arr.add(new int[] { 18 });
      arr.add(new int[] { 22 });
      arr.add(new int[] { 26 });
      arr.add(new int[] { 67 });
    } else {
      arr.add(new int[] { 61 });
      arr.add(new int[] { 12 });
      arr.add(new int[] { 16 });
      arr.add(new int[] { 20 });
      arr.add(new int[] { 24 });
      arr.add(new int[] { 65 });
      if (format.isLocAuxCurrType()) {
        arr.add(new int[] { 62 });
        arr.add(new int[] { 13 });
        arr.add(new int[] { 17 });
        arr.add(new int[] { 21 });
        arr.add(new int[] { 25 });
        arr.add(new int[] { 66 });
      }
      arr.add(new int[] { 63 });
      arr.add(new int[] { 14 });
      arr.add(new int[] { 18 });
      arr.add(new int[] { 22 });
      arr.add(new int[] { 26 });
      arr.add(new int[] { 67 });
    }
    if (format.isNumberFormat()) {
      arr.add(new int[] { 60 });
      arr.add(new int[] { 11 });
      arr.add(new int[] { 15 });
      arr.add(new int[] { 19 });
      arr.add(new int[] { 23 });
      arr.add(new int[] { 64 });
    }

    BalanceSubjAssBSVO[] data = this.fixModel.getData();
    int iAssNums = 3;
    if ((data != null) && (data.length != 0)) {
      iAssNums = data[0].getAssVOs() != null ? data[0].getAssVOs().length : data[1].getAssVOs().length;
    }

    if (this.m_qryVo != null) {
      GLQueryObj[] allQryObjs = this.m_qryVo.getFormatVO().getQryObjs();

      if ((format.getAssShowType() == 3) || (format.getAssShowType() == 1)) {
        boolean blnIsSubjAcc = false;
        int i = 0;
        int iCountHeadAss = 0;
        for (int j = 0; j < allQryObjs.length; j++) {
          if (allQryObjs[j].getHeadEle()) {
            if (!allQryObjs[j].getType().equals("会计科目")) {
              iCountHeadAss++;
            }
          }
          else if (allQryObjs[j].getType().equals("会计科目")) {
            blnIsSubjAcc = true;
            arr.add(new int[] { 46, i, 46 });

            i++;
          }
          else if (blnIsSubjAcc) {
            arr.add(new int[] { 0, i - 1 + iCountHeadAss, 6 });

            i++;
          } else {
            arr.add(new int[] { 0, i + iCountHeadAss, 6 });

            i++;
          }
        }
      }

      if ((format.getAssShowType() == 3) || (format.getAssShowType() == 2)) {
        boolean blnIsSubjAcc = false;
        int i = 0;
        int iCountHeadAss = 0;
        for (int j = 0; j < allQryObjs.length; j++) {
          if (allQryObjs[j].getHeadEle()) {
            if (!allQryObjs[j].getType().equals("会计科目")) {
              iCountHeadAss++;
            }
          }
          else if (allQryObjs[j].getType().equals("会计科目")) {
            blnIsSubjAcc = true;
            arr.add(new int[] { 45, i, 45 });

            i++;
          }
          else if (blnIsSubjAcc) {
            arr.add(new int[] { 0, i - 1 + iCountHeadAss, 5 });

            i++;
          } else {
            arr.add(new int[] { 0, i + iCountHeadAss, 5 });

            i++;
          }
        }

      }

    }

    setTableColVisibility(arr);
  }

  public void setFixModel(FixTableModel newFixModel)
  {
    this.fixModel = newFixModel;
  }

  public void setM_TableModel(TableModel newM_TableModel)
  {
    this.m_TableModel = newM_TableModel;
  }

  private void setTableColVisibility(ArrayList colsNum)
  {
    UITable table = getUITablePane().getTable();
    UITable fixTable = (UITable)(UITable)getUITablePane().getRowHeader().getComponents()[0];

    TableFormatTackle vo = ((TableModel)table.getModel()).getFormatVO();

    ColFormatVO[] colFormats = vo.getColFormatVOs();

    for (int i = 0; i < colFormats.length; i++) {
      boolean flag = false;

      for (int j = 0; j < colsNum.size(); j++) {
        int[] iKey = (int[])(int[])colsNum.get(j);
        if (colFormats[i].getColKey() == iKey[0]) {
          if ((colFormats[i].getColKey() == 0) || (colFormats[i].getColKey() == 46) || (colFormats[i].getColKey() == 45))
          {
            int[] info = (int[])(int[])colFormats[i].getUserData();
            if ((info[0] == iKey[1]) && (info[1] == iKey[2]))
              flag = true;
          } else {
            flag = true;
          }
          if (flag)
            break;
        }
      }
      colFormats[i].setVisiablity(flag);
    }
    vo.setFixVisiablity(fixTable);
    vo.setVisiablity(table);
    vo.setMultiHead(table);
    vo.setAlignment(table);
    vo.setFixAlignment(fixTable);
  }

  public void setTableData(TableDataModel dataModel, GlQueryVO qryVO)
    throws Exception
  {
    BalanceSubjAssBSVO[] vos = dataModel == null ? null : (BalanceSubjAssBSVO[])(BalanceSubjAssBSVO[])dataModel.getData();

    if (vos == null)
    {
      vos = new BalanceSubjAssBSVO[0];
      ((TableModel)getUITablePane().getTable().getModel()).setData(vos);
      this.fixModel.setData(vos);
      return;
    }
    this.m_qryVo = qryVO;

    ShowContentCenter newCenter = new ShowContentCenter();

    GLQueryObj[] allQryObjs = qryVO.getFormatVO().getQryObjs();
    Vector vTempHead = new Vector();
    Vector vTempBody = new Vector();
    Vector vBodyAssLoc = new Vector();
    Vector vHeadAssLoc = new Vector();
    int adjustValue = 0;
    for (int i = 0; i < allQryObjs.length; i++)
    {
      if (allQryObjs[i].getType().equals("会计科目"))
        adjustValue--;
      if (allQryObjs[i].getHeadEle())
      {
        vTempHead.add(allQryObjs[i]);
        if (!allQryObjs[i].getType().equals("会计科目")) {
          vHeadAssLoc.add(new Integer(i + adjustValue));
        }
      }
      else
      {
        vTempBody.add(allQryObjs[i]);
        if (!allQryObjs[i].getType().equals("会计科目"))
          vBodyAssLoc.add(new Integer(i + adjustValue));
      }
    }
    GLQueryObj[] body = (GLQueryObj[])(GLQueryObj[])Convertor.convertVectorToArray(vTempBody);
    GLQueryObj[] head = (GLQueryObj[])(GLQueryObj[])Convertor.convertVectorToArray(vTempHead);
    Integer[] bodyAssLocation = (Integer[])(Integer[])Convertor.convertVectorToArray(vBodyAssLoc);
    Integer[] headAssLocation = (Integer[])(Integer[])Convertor.convertVectorToArray(vHeadAssLoc);
    initTable(body);

    getlbCurrType().setText(qryVO.getCurrTypeName());
    if (qryVO.isQueryByPeriod())
    {
      getlbPeriod().setText(qryVO.getYear() + "." + qryVO.getPeriod() + "-" + qryVO.getEndYear() + "." + qryVO.getEndPeriod());
    }
    else
    {
      getlbPeriod().setText(qryVO.getDate() + "----" + qryVO.getEndDate());
    }

    if ((head != null) && (head.length > 0)) {
      int assIndex = -1;
      String sTemp = "";
      for (int i = 0; i < head.length; i++) {
        if (head[i].getType().equals("会计科目")) {
          String subjName = ShowContentCenter.getShowAccsubj(qryVO.getBaseGlOrgBook(), qryVO.getPk_accsubj()[0], qryVO.getSubjVersionYear(), qryVO.getSubjVerisonPeriod());

          sTemp = sTemp + NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000135") + subjName + NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000136");
        } else {
          assIndex++;
          String assInfo = ShowContentCenter.getShowAss(qryVO.getBaseGlOrgBook(), new AssVO[] { qryVO.getAssVos()[headAssLocation[assIndex].intValue()] });

          sTemp = sTemp + assInfo;
        }
      }
      getlbSubj().setText(sTemp);
    } else {
      getlbSubj().setText("");
    }
    getUILabelOrgNameAndCode().setText(getOrgNames(qryVO));

    Boolean isLocalFrac = GLParaDataCache.getInstance().IsLocalFrac(qryVO.getBaseGlOrgBook());

    String pk_locCurrtype = GLParaDataCache.getInstance().PkLocalCurr(qryVO.getBaseGlOrgBook());

    if (isLocalFrac.booleanValue()) {
      String pk_auxCurrtype = GLParaDataCache.getInstance().PkFracCurr(qryVO.getBaseGlOrgBook());

      ((TableModel)getUITablePane().getTable().getModel()).setCurrtypePks(pk_locCurrtype, pk_auxCurrtype);
    }
    else
    {
      ((TableModel)getUITablePane().getTable().getModel()).setCurrtypePks(pk_locCurrtype, null);
    }

    if ((!qryVO.isMultiCorpCombine()) && (qryVO.getPk_glorgbook().length > 1))
      getFormat().setMultiOrg(true);
    else {
      getFormat().setMultiOrg(false);
    }
    getFormat().setLocAuxCurrType(qryVO.isLocalFrac());
    if (qryVO.getCurrTypeName().equals(CurrTypeConst.ALL_CURRTYPE))
    {
      getFormat().setMultiCurrType(true);
    } else {
      getFormat().setMultiCurrType(false);
      if (qryVO.getCurrTypeName().equals(CurrTypeConst.LOC_CURRTYPE))
      {
        getFormat().setLocCurrType(true);
      }if (qryVO.getCurrTypeName().equals(CurrTypeConst.AUX_CURRTYPE))
      {
        getFormat().setAuxCurrType(true);
      }
    }
    if (qryVO.isQueryByPeriod())
    {
      if (qryVO.getYear().equals(qryVO.getEndYear()))
        getFormat().setIsMultiyear(false);
      else {
        getFormat().setIsMultiyear(true);
      }
    }
    else
    {
      UFDate date1 = qryVO.getDate();
      UFDate date2 = qryVO.getEndDate();

      GlPeriodVO period1 = null; GlPeriodVO period2 = null;
      try
      {
        period1 = GLPeriodDataCatch.getInstance().getGLperiodVOByDate(qryVO.getPk_glorgbook()[0], date1);
        period2 = GLPeriodDataCatch.getInstance().getGLperiodVOByDate(qryVO.getPk_glorgbook()[0], date2);
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      String year1 = period1.getYear();
      String year2 = period2.getYear();

      if (year1.equals(year2))
        getFormat().setIsMultiyear(false);
      else {
        getFormat().setIsMultiyear(true);
      }

    }

    ((TableModel)getUITablePane().getTable().getModel()).m_QuantityDigit = null;
    ((TableModel)getUITablePane().getTable().getModel()).setData(vos);
    this.fixModel.setData(vos);

    resetFormat(getFormat());
    if (this.colWs != null) {
      initTableForMA();
    }

    getUITablePane().invalidate();
    getUITablePane().repaint();
    getUITablePane().getTable().updateUI();
    getUITablePane().updateUI();
    repaint();
  }

  public void valueChanged(ListSelectionEvent e)
  {
    try
    {
      if (e.getSource() == this.fixTable.getSelectionModel())
      {
        int i = this.fixTable.getSelectedRow();
        getUITablePane().getTable().setRowSelectionInterval(i, i);
        getUITablePane().getTable().scrollRectToVisible(getUITablePane().getTable().getCellRect(i, getUITablePane().getTable().getSelectedColumn(), true));
      }
      if (e.getSource() == getUITablePane().getTable().getSelectionModel())
      {
        int i = getUITablePane().getTable().getSelectedRow();
        this.fixTable.setRowSelectionInterval(i, i);
      }
    }
    catch (Exception ex)
    {
    }
  }

  private UILabel getUILabelOrgNameAndCode()
  {
    return this.UILabelOrgNameAndCode;
  }

  class MyMouseMotionAdapter extends MouseMotionAdapter
  {
    MyMouseMotionAdapter()
    {
    }

    public void mouseDragged(MouseEvent e)
    {
      String resizeColName = null;
      int resizeColWidth = -1;
      if (e.getSource() == BalanceSubjAssUI.this.fixTable.getTableHeader()) {
        if (BalanceSubjAssUI.this.fixTable.getTableHeader().getResizingColumn() != null)
          BalanceSubjAssUI.this.onColResized();
        resizeColName = BalanceSubjAssUI.this.fixTable.getTableHeader().getResizingColumn().getHeaderValue().toString();
        resizeColWidth = BalanceSubjAssUI.this.fixTable.getTableHeader().getResizingColumn().getWidth();
      }
      else if (e.getSource() == BalanceSubjAssUI.this.getUITablePane().getTable().getTableHeader()) {
        resizeColName = null;
        if (BalanceSubjAssUI.this.getUITablePane().getTable().getTableHeader().getResizingColumn().getHeaderValue() != null) {
          resizeColName = BalanceSubjAssUI.this.getUITablePane().getTable().getTableHeader().getResizingColumn().getHeaderValue().toString();
        }

        resizeColWidth = BalanceSubjAssUI.this.getUITablePane().getTable().getTableHeader().getResizingColumn().getWidth();
      }

      ColFormatVO[] formatvos = BalanceSubjAssUI.this.fixModel.getVo().getColFormatVOs();
      for (int i = 0; i < formatvos.length; i++)
      {
        if ((!formatvos[i].getColName().equals(resizeColName)) && ((formatvos[i].getMultiHeadStr() == null) || (!formatvos[i].getMultiHeadStr().equals(resizeColName))))
        {
          continue;
        }
        formatvos[i].setColWidth(resizeColWidth);

        break;
      }
    }
  }
}