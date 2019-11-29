package nc.ui.gl.balancebooks;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import nc.bs.logging.Log;
import nc.itf.fi.pub.Accsubj;
import nc.itf.glcom.para.IGlPara;
import nc.ui.bd.BDGLOrgBookAccessor;
import nc.ui.bd.b02.BdinfoBO_Client;
import nc.ui.gl.accbook.PrintDialog;
import nc.ui.gl.accbook.TableFormatTackle;
import nc.ui.glcom.control.GlComboBox;
import nc.ui.glcom.numbertool.GlCurrAmountFormat;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialogEvent;
import nc.ui.pub.beans.UIDialogListener;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintDirectEntry;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.print.datastruct.CellRange;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b02.BdinfoVO;
import nc.vo.bd.b02.SubjassVO;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.gl.accbook.ColFormatVO;
import nc.vo.gl.accbook.PrintCondVO;
import nc.vo.gl.balancebooks.BalanceBSVO;
import nc.vo.glcom.ass.AssVO;
import nc.vo.glcom.balance.GLQueryObj;
import nc.vo.glcom.balance.GlQryFormatVO;
import nc.vo.glcom.balance.GlQueryVO;
import nc.vo.glcom.tools.BaseCorpChooser;
import nc.vo.glcom.tools.GLPubProxy;
import nc.vo.pub.print.PrintTempletmanageHeaderVO;
import nc.vo.sm.UserVO;

public class BalancebookView extends JPanel
  implements UIDialogListener, IDataSource
{
  private BalancebooksModel ivjBalancebooksModel = null;
  GlQueryVO qryVO = null;
  private BalancebookUI ivjBalancebookUI = null;
  private QueryDlg ivjDlg = null;
  private IParent m_parent;
  private PrintDialog ivjPrintDlg = null;
  private ToftPanelView parentView = null;
  private int iState = 0;
  private BalanceBSVO[] qryedData = null;

  private void printAsItIs()
    throws Exception
  {
    PrintDirectEntry printEntry = new PrintDirectEntry(this);

    printEntry.setTitle(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000483"));

    ColFormatVO[] formats = getBalancebookUI().getFixModel().getVo().getColFormatVOs();

    printEntry.setMargin(10, 10, 10, 10);

    int[] lineHeight = new int[getBalancebookUI().getFixModel().getRowCount() + 2];
    lineHeight[0] = 15;
    lineHeight[1] = 15;
    for (int i = 0; i < getBalancebookUI().getFixModel().getRowCount(); i++) {
      lineHeight[(i + 2)] = 15;
    }
    printEntry.setRowHeight(lineHeight);

    Vector vLine1 = new Vector();
    Vector vLine0 = new Vector();
    Vector align = new Vector();

    Vector vColWidths = new Vector();
    int fixCol = 0;

    for (int i = 0; i < formats.length; i++) {
      if (formats[i].isVisiablity()) {
        vLine0.add(formats[i].getMultiHeadStr() == null ? formats[i].getColName() : formats[i].getMultiHeadStr());

        vLine1.add(formats[i].getColName());
        vColWidths.add(new Integer(formats[i].getColWidth()));
        if (formats[i].getAlignment() == 2) {
          align.add(new Integer(0));
        }
        else if (formats[i].getAlignment() == 0) {
          align.add(new Integer(1));
        }
        else if (formats[i].getAlignment() == 4) {
          align.add(new Integer(2));
        }
        if (formats[i].isFrozen() == true) {
          fixCol++;
        }
      }
    }
    String[] line1 = new String[vLine1.size()];
    String[] line0 = new String[vLine0.size()];
    int[] iAlignFlag = new int[line1.length];
    for (int i = 0; i < iAlignFlag.length; i++) {
      iAlignFlag[i] = ((Integer)align.elementAt(i)).intValue();
    }
    int[] colWidth = new int[vColWidths.size()];
    for (int i = 0; i < colWidth.length; i++) {
      colWidth[i] = ((Integer)vColWidths.elementAt(i)).intValue();
    }
    vLine1.copyInto(line1);
    vLine0.copyInto(line0);
    String[][] colNames = new String[2][];
    colNames[0] = line0;
    colNames[1] = line1;
    printEntry.setColNames(colNames);
    printEntry.setAlignFlag(iAlignFlag);

    String[][] sTop = new String[2][1];
    sTop[0][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000061") + ": " + getBalancebookUI().getlbPeriod().getText());
    sTop[1][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000171") + getBalancebookUI().getlbCurrType().getText());
    printEntry.setTopStr(sTop);
    printEntry.setTopStrFixed(true);
    printEntry.setFixedRows(2);
    printEntry.setTopStrColRange(new int[] { line1.length - 1 });

    String[][] sBottom = new String[3][1];
    sBottom[0][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000172") + ClientEnvironment.getInstance().getCorporation().getUnitname());

    sBottom[1][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000173") + ClientEnvironment.getInstance().getUser().getUserName());

    sBottom[2][0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000174") + new Date().toLocaleString());

    printEntry.setBottomStr(sBottom);
    printEntry.setBottomStrAlign(new int[] { 0, 1 });
    printEntry.setBStrColRange(new int[] { line1.length - 1 });
    printEntry.setPageNumDisp(true);
    printEntry.setPageNumAlign(2);

    Vector vRange = new Vector();
    for (int i = 0; i < line0.length; i++) {
      if (line0[i].equals(line1[i])) {
        CellRange rangeTemp = new CellRange(0, i, 1, i);

        vRange.add(rangeTemp);
      }
    }
    int startCol = 0;
    int endCol = 0;
    boolean flag = false;
    while (startCol < line0.length) {
      flag = startCol != endCol;
      if ((endCol < line0.length) && (line0[startCol].equals(line0[endCol]))) {
        if (startCol < line0.length - 1) {
          endCol++; continue;
        }
        startCol++;
        continue;
      }if (flag) {
        if (endCol - startCol != 1) {
          CellRange rangeTemp = new CellRange(0, startCol, 0, endCol - 1);

          vRange.add(rangeTemp);
        }
        startCol = endCol;
      }
    }
    CellRange[] range = new CellRange[vRange.size()];

    vRange.copyInto(range);
    printEntry.setCombinCellRange(range);

    int iFixCount = 0;
    int iUNFixCount = 0;
    for (int i = 0; i < formats.length; i++) {
      if (formats[i].isVisiablity()) {
        if (i < getBalancebookUI().getFixModel().getColumnCount())
          iFixCount++;
        else {
          iUNFixCount++;
        }
      }
    }
    Object[][] data = new Object[getBalancebookUI().getFixModel().getRowCount()][line1.length];
    int[] fixLocation = new int[iFixCount];
    int[] unFixLocation = new int[iUNFixCount];
    int iFixIndex = 0;
    int iUnFixIndex = 0;
    for (int i = 0; i < formats.length; i++) {
      if (formats[i].isVisiablity()) {
        if (i < getBalancebookUI().getFixModel().getColumnCount()) {
          fixLocation[iFixIndex] = i;
          iFixIndex++;
        } else {
          unFixLocation[iUnFixIndex] = i;
          iUnFixIndex++;
        }
      }
    }
    for (int i = 0; i < getBalancebookUI().getFixModel().getRowCount(); i++) {
      int iFIndex = 0;
      for (int j = 0; j < iFixCount; j++) {
        data[i][j] = getBalancebookUI().getFixModel().getValueAt(i, fixLocation[iFIndex]);
        iFIndex++;
      }
      int iUnFIndex = 0;
      for (int j = iFixCount; j < line1.length; j++) {
        data[i][j] = getBalancebookUI().getM_model().getValueAt(i, unFixLocation[iUnFIndex] - getBalancebookUI().getFixModel().getColumnCount());

        iUnFIndex++;
      }
    }
    printEntry.setData(data);

    printEntry.setFixedCols(fixCol);
    Log.getInstance(getClass().getName()).info("-------------" + fixCol + "------------------");
    printEntry.setColWidth(colWidth);

    printEntry.preview();
  }

  public PrintDialog getPrintDlg()
  {
    if (this.ivjPrintDlg == null) {
      try {
        this.ivjPrintDlg = new PrintDialog(this);
        this.ivjPrintDlg.setName("PrintDlg");
        this.ivjPrintDlg.setDefaultCloseOperation(2);

        this.ivjPrintDlg.addUIDialogListener(this);

        String pk_user = ClientEnvironment.getInstance().getUser().getPrimaryKey();
        String pk_loginGlorgbook = ((GlorgbookVO)ClientEnvironment.getInstance().getValue("pk_glorgbook")).getPrimaryKey();

        PrintEntry printEntry = new PrintEntry(null, this);

        printEntry.setTemplateID(pk_loginGlorgbook, "20023005", pk_user, null, null, "2");
        PrintTempletmanageHeaderVO[] headvos = printEntry.getAllTemplates();
        PrintCondVO condvo = new PrintCondVO();
        condvo.setPrintModule(headvos);
        this.ivjPrintDlg.setPrintData(condvo);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjPrintDlg;
  }

  public BalancebookView()
  {
    initialize();
  }

  public BalancebookView(ToftPanelView tempParentView)
  {
    setParentView(tempParentView);
    initialize();
  }

  public BalancebookView(LayoutManager layout)
  {
    super(layout);
  }

  public BalancebookView(LayoutManager layout, boolean isDoubleBuffered)
  {
    super(layout, isDoubleBuffered);
  }

  public BalancebookView(boolean isDoubleBuffered)
  {
    super(isDoubleBuffered);
  }
  public void dialogClosed(UIDialogEvent e) {
    UiManager view = (UiManager)getIParent();
    if (e.getSource() == this.ivjDlg) {
      if (e.m_Operation == 202)
      {
        view.showHintMessage(NCLangRes.getInstance().getStrByID("commonres", "UC001-0000006") + NCLangRes.getInstance().getStrByID("commonres", "UCH026"));

        return;
      }
      if (e.m_Operation == 204) {
        this.ivjDlg.getResult();
        try {
          this.qryVO = this.ivjDlg.getqryVO();
          this.qryedData = ((BalanceBSVO[])(BalanceBSVO[])getBalancebooksModel().dealQuery(this.qryVO));
          getBalancebookUI().setTableData(this.qryedData, this.qryVO);
          view.showHintMessage(NCLangRes.getInstance().getStrByID("commonres", "UC001-0000006") + NCLangRes.getInstance().getStrByID("commonres", "UCH026"));
        }
        catch (Exception err) {
          err.printStackTrace();
        }
        this.iState = 1;
      }
    } else if (e.getSource() == getPrintDlg()) {
      if (e.m_Operation == 202)
      {
        view.showHintMessage(NCLangRes.getInstance().getStrByID("commonres", "UC001-0000007") + NCLangRes.getInstance().getStrByID("commonres", "UCH026"));
        return;
      }
      if (e.m_Operation == 204)
        try {
          PrintCondVO printvo = new PrintCondVO();
          getPrintDlg().setPrintData(printvo);
          printvo = getPrintDlg().getPrintData();
          if (printvo.isBlnPrintAsModule())
            print();
          else {
            printAsItIs();
          }
          view.showHintMessage(NCLangRes.getInstance().getStrByID("commonres", "UC001-0000007") + NCLangRes.getInstance().getStrByID("commonres", "UCH026"));
        } catch (Exception err) {
          Log.getInstance(getClass().getName()).info(err.getMessage());
        }
    }
  }

  public String[] getAllDataItemExpress()
  {
    return new String[] { "corp", "subjcode", "subjname", "currtype", "begindirection", "qtbegin", "begin", "fragbegin", "localbegin", "drquantity", "dr", "drfrag", "drlocal", "crquantity", "cr", "crfrag", "crlocal", "sumdrquantity", "sumdr", "sumdrfrag", "sumdrlocal", "sumcrquantity", "sumcr", "sumcrfrag", "sumcrlocal", "enddirection", "qtend", "end", "endfrag", "endlocal" };
  }

  public String[] getAllDataItemNames()
  {
    return null;
  }

  private BalancebooksModel getBalancebooksModel()
  {
    if (this.ivjBalancebooksModel == null) {
      try {
        this.ivjBalancebooksModel = new BalancebooksModel();
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBalancebooksModel;
  }

  public BalancebookUI getBalancebookUI()
  {
    if (this.ivjBalancebookUI == null) {
      try {
        this.ivjBalancebookUI = new BalancebookUI();
        this.ivjBalancebookUI.setName("BalancebookUI");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBalancebookUI;
  }

  public String[] getDependentItemExpressByExpress(String itemName)
  {
    return null;
  }

  private QueryDlg getDlg()
  {
    if (this.ivjDlg == null) {
      try {
        this.ivjDlg = new QueryDlg(this, null, "20023005");
        this.ivjDlg.setName("Dlg");

        this.ivjDlg.addUIDialogListener(this);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjDlg;
  }

  public IParent getIParent() {
    return this.m_parent;
  }

  public String[] getItemValuesByExpress(String itemExpress) {
    String[] ret = null;
    boolean blnItemIntoIf = false;
    if (itemExpress.equals("headdatescope"))
    {
      blnItemIntoIf = true;
      ret = new String[1];
      String strYear = getBalancebooksModel().getQueryVO().getYear();
      String strPeriod = getBalancebooksModel().getQueryVO().getPeriod();
      String strEndPeriod = getBalancebooksModel().getQueryVO().getEndPeriod();
      ret[0] = (NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000061") + "：" + strYear + "." + strPeriod + "-" + strYear + "." + strEndPeriod);
    } else {
      if (itemExpress.equals("headcorp"))
      {
        return new String[] { "" };
      }
      if (itemExpress.equals("headglorgbook"))
      {
        String[] strPkorgbooks = this.qryVO.getPk_glorgbook();
        String strorgName = "";
        GlorgbookVO[] orgbookVOs = new GlorgbookVO[strPkorgbooks.length];
        try {
          for (int i = 0; i < strPkorgbooks.length; i++) {
            orgbookVOs[i] = BDGLOrgBookAccessor.getGlOrgBookVOByPrimaryKey(strPkorgbooks[i]);
            strorgName = strorgName + "," + orgbookVOs[i].getGlorgbookname();
          }
        } catch (Exception ee) {
          handleException(ee);
        }
        strorgName = strorgName.substring(1);
        return new String[] { strorgName };
      }
      if (itemExpress.equals("headcurrtype"))
      {
        blnItemIntoIf = true;
        ret = new String[1];
        ret[0] = getBalancebooksModel().getQueryVO().getCurrTypeName();
      }
      else if (itemExpress.equals("initcreditloctotal"))
      {
        blnItemIntoIf = true;
        ret = new String[1];
        ret[0] = gettotal(12);
      }
      else if (itemExpress.equals("initdebitloctotal"))
      {
        blnItemIntoIf = true;
        ret = new String[1];
        ret[0] = gettotal(8);
      }
      else if (itemExpress.equals("endcreditloctotal"))
      {
        blnItemIntoIf = true;
        ret = new String[1];
        ret[0] = gettotal(36);
      }
      else if (itemExpress.equals("enddebitloctotal"))
      {
        blnItemIntoIf = true;
        ret = new String[1];
        ret[0] = gettotal(32);
      }
      else
      {
        BalanceBSVO[] tempVos = getBalancebooksModel().getData();
        ret = new String[tempVos.length];
        for (int i = 0; i < tempVos.length; i++)
        {
          try
          {
            int iKey = 0;
            if (itemExpress.equals("glorgbookname"))
            {
              blnItemIntoIf = true;
              iKey = 61;
            }
            if (itemExpress.equals("corp"))
            {
              blnItemIntoIf = true;
              iKey = 2;
            }
            else if (itemExpress.equals("subjcode"))
            {
              blnItemIntoIf = true;
              iKey = 0;
            }
            else if (itemExpress.equals("subjname"))
            {
              blnItemIntoIf = true;
              iKey = 1;
            }
            else if (itemExpress.equals("currtype"))
            {
              blnItemIntoIf = true;
              iKey = 3;
            }
            else if (itemExpress.equals("begindirection"))
            {
              blnItemIntoIf = true;
              iKey = 37;
            }
            else if (itemExpress.equals("qtbegin"))
            {
              blnItemIntoIf = true;
              iKey = 38;
            }
            else if (itemExpress.equals("begin"))
            {
              blnItemIntoIf = true;
              iKey = 40;
            }
            else if (itemExpress.equals("fragbegin"))
            {
              blnItemIntoIf = true;
              iKey = 45;
            }
            else if (itemExpress.equals("localbegin"))
            {
              blnItemIntoIf = true;
              iKey = 42;
            }
            else if (itemExpress.equals("drquantity"))
            {
              blnItemIntoIf = true;
              iKey = 13;
            }
            else if (itemExpress.equals("dr"))
            {
              blnItemIntoIf = true;
              iKey = 14;
            }
            else if (itemExpress.equals("drfrag"))
            {
              blnItemIntoIf = true;
              iKey = 15;
            }
            else if (itemExpress.equals("drlocal"))
            {
              blnItemIntoIf = true;
              iKey = 16;
            }
            else if (itemExpress.equals("crquantity"))
            {
              blnItemIntoIf = true;
              iKey = 17;
            }
            else if (itemExpress.equals("cr"))
            {
              blnItemIntoIf = true;
              iKey = 18;
            }
            else if (itemExpress.equals("crfrag"))
            {
              blnItemIntoIf = true;
              iKey = 19;
            }
            else if (itemExpress.equals("crlocal"))
            {
              blnItemIntoIf = true;
              iKey = 20;
            }
            else if (itemExpress.equals("sumdrquantity"))
            {
              blnItemIntoIf = true;
              iKey = 21;
            }
            else if (itemExpress.equals("sumdr"))
            {
              blnItemIntoIf = true;
              iKey = 22;
            }
            else if (itemExpress.equals("sumdrfrag"))
            {
              blnItemIntoIf = true;
              iKey = 23;
            }
            else if (itemExpress.equals("sumdrlocal"))
            {
              blnItemIntoIf = true;
              iKey = 24;
            }
            else if (itemExpress.equals("sumcrquantity"))
            {
              blnItemIntoIf = true;
              iKey = 25;
            }
            else if (itemExpress.equals("sumcr"))
            {
              blnItemIntoIf = true;
              iKey = 26;
            }
            else if (itemExpress.equals("sumfrag"))
            {
              blnItemIntoIf = true;
              iKey = 27;
            }
            else if (itemExpress.equals("sumlocal"))
            {
              blnItemIntoIf = true;
              iKey = 28;
            }
            else if (itemExpress.equals("enddirection"))
            {
              blnItemIntoIf = true;
              iKey = 43;
            }
            else if (itemExpress.equals("qtend"))
            {
              blnItemIntoIf = true;
              iKey = 39;
            }
            else if (itemExpress.equals("end"))
            {
              blnItemIntoIf = true;
              iKey = 41;
            }
            else if (itemExpress.equals("endfrag"))
            {
              blnItemIntoIf = true;
              iKey = 46;
            }
            else if (itemExpress.equals("endlocal"))
            {
              blnItemIntoIf = true;
              iKey = 44;
            }
            else if (itemExpress.equals("drqtbegin"))
            {
              blnItemIntoIf = true;
              iKey = 5;
            }
            else if (itemExpress.equals("drbegin"))
            {
              blnItemIntoIf = true;
              iKey = 6;
            }
            else if (itemExpress.equals("drfragbegin"))
            {
              blnItemIntoIf = true;
              iKey = 7;
            }
            else if (itemExpress.equals("drlocalbegin"))
            {
              blnItemIntoIf = true;
              iKey = 8;
            }
            else if (itemExpress.equals("crqtbegin"))
            {
              blnItemIntoIf = true;
              iKey = 9;
            }
            else if (itemExpress.equals("crbegin"))
            {
              blnItemIntoIf = true;
              iKey = 10;
            }
            else if (itemExpress.equals("crfragbegin"))
            {
              blnItemIntoIf = true;
              iKey = 11;
            }
            else if (itemExpress.equals("crlocalbegin"))
            {
              blnItemIntoIf = true;
              iKey = 12;
            }
            else if (itemExpress.equals("drqtend"))
            {
              blnItemIntoIf = true;
              iKey = 29;
            }
            else if (itemExpress.equals("drend"))
            {
              blnItemIntoIf = true;
              iKey = 30;
            }
            else if (itemExpress.equals("drfragend"))
            {
              blnItemIntoIf = true;
              iKey = 31;
            }
            else if (itemExpress.equals("drlocalend"))
            {
              blnItemIntoIf = true;
              iKey = 32;
            }
            else if (itemExpress.equals("crqtend"))
            {
              blnItemIntoIf = true;
              iKey = 33;
            }
            else if (itemExpress.equals("crend"))
            {
              blnItemIntoIf = true;
              iKey = 34;
            }
            else if (itemExpress.equals("crfragend"))
            {
              blnItemIntoIf = true;
              iKey = 35;
            }
            else if (itemExpress.equals("crlocalend"))
            {
              blnItemIntoIf = true;
              iKey = 36;
            }

            Object objTemp = getBalancebookUI().m_model.getValue(i, iKey);

            if (itemExpress.equals("currtype"))
            {
              blnItemIntoIf = true;
              if (objTemp == null) {
                objTemp = this.qryVO.getCurrTypeName();
              }
            }
            ret[i] = (objTemp == null ? "" : objTemp.toString());
          }
          catch (Exception e)
          {
            ret[i] = "";
          }
        }
      }
    }
    if (!blnItemIntoIf) {
      ret = null;
    }
    return ret;
  }

  public String getModuleName()
  {
    return "20023005";
  }
  private GLQueryObj[] getQryObjs(String pk_Subj, String subjCode) {
    GLQueryObj[] qryObjs = null;
    try {
      SubjassVO[] subjAssVOs = Accsubj.queryBDInfo(pk_Subj);
      int len = subjAssVOs.length;
      if (len == 0) {
        return null;
      }
      qryObjs = new GLQueryObj[subjAssVOs.length + 1];

      String[] BdinfoPks = new String[len];
      for (int i = 0; i < len; i++) {
        BdinfoPks[i] = subjAssVOs[i].getPk_bdinfo();
      }
      BdinfoVO[] bdinfoVos = BdinfoBO_Client.queryDetail(BdinfoPks);

      for (int i = 0; i < len; i++) {
        qryObjs[i] = new GLQueryObj();
        qryObjs[i].setType(bdinfoVos[i].getBdname());
        qryObjs[i].setTypeName(bdinfoVos[i].getDispName());
        AssVO[] assVOs = new AssVO[1];
        AssVO assvoTemp = new AssVO();
        assvoTemp.setPk_Checktype(bdinfoVos[i].getPk_bdinfo());
        assvoTemp.setChecktypename(bdinfoVos[i].getBdname());
        assvoTemp.setChecktypecode(bdinfoVos[i].getBdcode());
        assvoTemp.setPk_Checkvalue(null);
        assvoTemp.setCheckvaluecode(null);
        assvoTemp.setCheckvaluename("ALL");
        assVOs[0] = assvoTemp;
        qryObjs[i].setValueRange(assVOs);
      }

      qryObjs[len] = new GLQueryObj();
      qryObjs[len].setType("会计科目");
      qryObjs[len].setHeadEle(true);

      AssVO[] assVOs = new AssVO[1];
      AssVO assvoTemp = new AssVO();
      assvoTemp.setPk_Checktype("00010000000000000034");
      assvoTemp.setPk_Checkvalue(pk_Subj);
      assvoTemp.setCheckvaluecode(subjCode);

      assVOs[0] = assvoTemp;
      qryObjs[len].setValueRange(assVOs);

      return qryObjs;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return qryObjs;
  }
  private String gettotal(int key) {
    BalanceBSVO[] vos = getBalancebookUI().m_model.data;
    int len = getBalancebookUI().m_model.data.length;
    double dblRtn = 0.0D;
    Object objTemp = null;
    try {
      for (int i = 0; i < len; i++) {
        String strCode1 = vos[i].getValue(0).toString();
        if ((strCode1.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000075")) >= 0) || (strCode1.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000118")) >= 0) || (strCode1.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000119")) >= 0) || (strCode1.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000115")) >= 0))
        {
          vos[i].setValue(54, "dddd");
        }
        else {
          for (int j = 0; j < len; j++) {
            String strCode2 = vos[j].getValue(0).toString();
            if ((!strCode2.equals(strCode1)) && (strCode1.startsWith(strCode2))) {
              vos[i].setValue(54, "dddd");
            }
          }
        }
      }

      for (int i = 0; i < len; i++) {
        if (vos[i].getValue(54) == null) {
          dblRtn += (getBalancebookUI().m_model.data[i].getValue(key) == null ? 0.0D : new Double(getBalancebookUI().m_model.data[i].getValue(key).toString()).doubleValue());
        }

      }

      for (int i = 0; i < len; i++) {
        vos[i].setValue(54, null);
      }

      GlCurrAmountFormat format = new GlCurrAmountFormat();

      objTemp = format.formatAmount(dblRtn, "00010000000000000001");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return objTemp.toString();
  }

  private void handleException(Throwable exception)
  {
  }

  private void onMultiOrg()
  {
    try
    {
      UiManager view = (UiManager)getIParent();
      view.showHintMessage(NCLangRes.getInstance().getStrByID("20023005", "UPT20023005-000050") + "多单位列示");
      int selRow = getBalancebookUI().getSelectedRow();
      if ((selRow < 0) || (getBalancebooksModel().m_dataVos == null) || (getBalancebooksModel().m_dataVos.length == 0)) {
        return;
      }
      BalanceBSVO voData = (BalanceBSVO)getBalancebooksModel().getVO(selRow);
      String strCode = voData.getValue(0).toString();

      if ((strCode.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000179")) >= 0) || (strCode.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000115")) >= 0) || (strCode.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000075")) >= 0))
      {
        return;
      }String pk_accusbj = voData.getValue(50).toString();

      GlQueryVO tempQryVO = (GlQueryVO)this.qryVO.clone();
      tempQryVO.setPk_accsubj(new String[] { pk_accusbj });
      tempQryVO.setSubjCodes(new String[] { strCode });

      tempQryVO.getFormatVO().setMultiCorpCombine(false);
      BalanceBSVO[] objData = (BalanceBSVO[])(BalanceBSVO[])getBalancebooksModel().dealQuery(tempQryVO);
      getBalancebookUI().setTableData(objData, tempQryVO);
    }
    catch (Exception err)
    {
      err.printStackTrace();
    }
    this.iState = 2;
  }

  private void onReturn()
  {
    getBalancebooksModel().m_dataVos = this.qryedData;
    getBalancebooksModel().m_qryVO = this.qryVO;

    getBalancebookUI().setTableData(this.qryedData, this.qryVO);
    this.iState = 1;
    setButtonState();
  }

  private void initialize()
  {
    try
    {
      setName("BalancebookView");
      setLayout(new BorderLayout());
      setSize(717, 426);
      add(getBalancebookUI(), "Center");
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getBalancebookUI().setChartModel(getBalancebooksModel());
    setButtonState();
  }

  public boolean isNumber(String itemExpress)
  {
    return (itemExpress.equals("qtbegin")) || (itemExpress.equals("begin")) || (itemExpress.equals("fragbegin")) || (itemExpress.equals("localbegin")) || (itemExpress.equals("drquantity")) || (itemExpress.equals("dr")) || (itemExpress.equals("drfrag")) || (itemExpress.equals("drlocal")) || (itemExpress.equals("crquantity")) || (itemExpress.equals("cr")) || (itemExpress.equals("crfrag")) || (itemExpress.equals("crlocal")) || (itemExpress.equals("sumdrquantity")) || (itemExpress.equals("sumdr")) || (itemExpress.equals("sumdrfrag")) || (itemExpress.equals("sumdrlocal")) || (itemExpress.equals("sumcrquantity")) || (itemExpress.equals("sumcr")) || (itemExpress.equals("sumfrag")) || (itemExpress.equals("sumlocal")) || (itemExpress.equals("qtend")) || (itemExpress.equals("end")) || (itemExpress.equals("endfrag")) || (itemExpress.equals("endlocal")) || (itemExpress.equals("drqtbegin")) || (itemExpress.equals("drbegin")) || (itemExpress.equals("drfragbegin")) || (itemExpress.equals("drlocalbegin")) || (itemExpress.equals("crqtbegin")) || (itemExpress.equals("crbegin")) || (itemExpress.equals("crfragbegin")) || (itemExpress.equals("crlocalbegin")) || (itemExpress.equals("drqtend")) || (itemExpress.equals("drend")) || (itemExpress.equals("drfragend")) || (itemExpress.equals("drlocalend")) || (itemExpress.equals("crqtend")) || (itemExpress.equals("crend")) || (itemExpress.equals("crfragend")) || (itemExpress.equals("crlocalend"));
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      BalancebookView aBalancebookView = new BalancebookView();
      frame.setContentPane(aBalancebookView);
      frame.setSize(aBalancebookView.getSize());
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
      System.err.println("javax.swing.JPanel 的 main() 中发生异常");
      exception.printStackTrace(System.out);
    }
  }

  private void print()
  {
    PrintEntry pEntry = new PrintEntry(null, this);
    String pk_user = ClientEnvironment.getInstance().getUser().getPrimaryKey();

    String pk_loginGlorgbook = ((GlorgbookVO)ClientEnvironment.getInstance().getValue("pk_glorgbook")).getPrimaryKey();

    pEntry.setTemplateID(pk_loginGlorgbook, "20023005", pk_user, null, null, "2");

    PrintTempletmanageHeaderVO[] templateHeaderVOs = pEntry.getAllTemplates();
    String tempName = (String)getPrintDlg().getModuleCombo().getItemAt(getPrintDlg().getModuleCombo().getSelectedIndex());
    String m_templateID = null;

    for (int i = 0; i < templateHeaderVOs.length; i++) {
      if (templateHeaderVOs[i].getVtemplatename().equals(tempName)) {
        m_templateID = templateHeaderVOs[i].getCtemplateid();
        break;
      }
    }
    pEntry.setTemplateID(m_templateID);
    pEntry.preview();
  }

  public void setIParent(IParent parent) {
    this.m_parent = parent;
  }

  public void setButtonState() {
    if (getParentView() == null)
      return;
    ToftPanelView parentView = getParentView();

    if (this.iState == 0)
    {
      int i = 0;
      for (; (parentView.getButtons() != null) && (i < parentView.getButtons().length); i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (tag.equals(ButtonKey.bnQRY)) {
          btn.setEnabled(true);
        }
        else
        {
          btn.setEnabled(false);
        }
      }
    }
    else if (this.iState == 1)
    {
      int i = 0;
      for (; (parentView.getButtons() != null) && (i < parentView.getButtons().length); i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (!tag.equals(ButtonKey.bnListMultiOrg)) {
          if (!tag.equals(ButtonKey.bnReturn)) {
            btn.setEnabled(true);
          }
          else {
            btn.setEnabled(false);
          }

        }
        else if (!this.qryVO.getFormatVO().isMultiCorpCombine())
        {
          btn.setEnabled(false);
        }
        else if (!tag.equals(ButtonKey.bnReturn))
          btn.setEnabled(true);
        else {
          btn.setEnabled(false);
        }

      }

    }
    else if (this.iState == 2)
    {
      int i = 0;
      for (; (parentView.getButtons() != null) && (i < parentView.getButtons().length); i++) {
        ButtonObject btn = parentView.getButtons()[i];
        String tag = btn.getTag();
        if (!tag.equals(ButtonKey.bnListMultiOrg)) {
          btn.setEnabled(true);
        }
        else {
          btn.setEnabled(false);
        }
      }
    }

    parentView.updateButtons();
  }

  void tackleBnsEvent(int i)
    throws Exception
  {
    UiManager view = (UiManager)getIParent();
    int selRow = -1;
    GlQueryVO qryVO = getBalancebooksModel().getQueryVO();
    GlQueryVO queryVO;
    switch (i)
    {
    case 0:
      getDlg().showModal();
      break;
    case 5:
      onMultiOrg();
      break;
    case 6:
      onReturn();
      break;
    case 4:
      getPrintDlg().showme("20023005");

      break;
    case 1:
      view.showHintMessage(NCLangRes.getInstance().getStrByID("20023005", "UPT20023005-000050") + NCLangRes.getInstance().getStrByID("20023005", "UPT20023005-000042"));
      selRow = getBalancebookUI().getSelectedRow();
      if ((selRow < 0) || (getBalancebooksModel().m_dataVos == null) || (getBalancebooksModel().m_dataVos.length == 0)) {
        return;
      }
      BalanceBSVO voData = (BalanceBSVO)getBalancebooksModel().getVO(selRow);
      String strCode = voData.getValue(0).toString();

      if ((strCode.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000179")) >= 0) || (strCode.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000115")) >= 0) || (strCode.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000075")) >= 0))
      {
        return;
      }IUiPanel panel = (IUiPanel)this.m_parent.showNext("nc.ui.gl.detail.DetailToftPanel");

      queryVO = getBalancebooksModel().getQueryVO();
      queryVO = (GlQueryVO)queryVO.clone();

      if ((!queryVO.isMultiCorpCombine()) && (queryVO.getPk_glorgbook().length > 1))
      {
        queryVO.setPk_glorgbook(new String[] { (String)voData.getValue(59) });
      }

      String[] startPeriod = GLPubProxy.getRemoteGlPara().getStartPeriod(qryVO.getBaseGlOrgBook(), "2002");
      if ((startPeriod[0].compareTo(queryVO.getYear()) == 0) && (startPeriod[1].compareTo(queryVO.getPeriod()) > 0)) {
        queryVO.setPeriod(startPeriod[1]);
      }
      queryVO.setEndYear(queryVO.getYear());
      queryVO.setPk_accsubj(new String[] { (String)voData.getValue(50) });
      queryVO.setAccsubjCode(new String[] { (String)voData.getValue(0) });
      queryVO.getFormatVO().setMultiOrgCombine(queryVO.isMultiCorpCombine());
      String pk_loginGlorgbook = ((GlorgbookVO)ClientEnvironment.getInstance().getValue("pk_glorgbook")).getPrimaryKey();
      String newBase = BaseCorpChooser.getPk_BasCorp(queryVO.getPk_glorgbook(), pk_loginGlorgbook);
      queryVO.setBaseGlOrgBook(newBase);
      queryVO.getFormatVO().setCombineOppositeSubj(true);

      queryVO.setNeedDetailFreeItem(true);
      queryVO.setNeedSubjFreeItem(true);

      if (queryVO.getPk_glorgbook().length > 1)
        queryVO.setBooksType(4);
      panel.invoke(queryVO, "SetQueryVO");
      break;
    case 2:
      selRow = getBalancebookUI().getSelectedRow();
      if (selRow < 0)
        return;
      BalanceBSVO voData1 = (BalanceBSVO)getBalancebooksModel().getVO(selRow);
      String strCode1 = voData1.getValue(0).toString();
      if ((strCode1.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000179")) >= 0) || (strCode1.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000115")) >= 0) || (strCode1.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000075")) >= 0))
        return;
      GlQueryVO queryVO1 = getBalancebooksModel().getQueryVO();
      queryVO = (GlQueryVO)queryVO1.clone();

      if (!queryVO.getFormatVO().isMultiCorpCombine()) {
        queryVO.setPk_glorgbook(new String[] { voData1.getValue(59).toString() });
        queryVO.setBaseGlOrgBook(voData1.getValue(59).toString());
      }
      queryVO.setPk_accsubj(new String[] { voData1.getValue(50).toString() });
      queryVO.setAccsubjCode(new String[] { (String)voData1.getValue(0) });
      IUiPanel panel1 = (IUiPanel)this.m_parent.showNext("nc.ui.gl.triaccbooks.TriToftPanel");
      panel1.invoke(queryVO, "SetQueryVO");
      break;
    case 3:
      view.showHintMessage(NCLangRes.getInstance().getStrByID("20023005", "UPT20023005-000050") + NCLangRes.getInstance().getStrByID("20023005", "UPT20023005-000039"));
      selRow = getBalancebookUI().getSelectedRow();
      if (selRow < 0)
        return;
      BalanceBSVO voData2 = (BalanceBSVO)getBalancebooksModel().getVO(selRow);
      String strCode2 = voData2.getValue(0).toString();
      if ((strCode2.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000179")) >= 0) || (strCode2.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000115")) >= 0) || (strCode2.indexOf(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000075")) >= 0))
      {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000487"), NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000488"));
        return;
      }
      GlQueryVO queryVO2 = (GlQueryVO)qryVO.clone();

      if (!queryVO2.getFormatVO().isMultiCorpCombine())
      {
        queryVO2.setPk_glorgbook(new String[] { voData2.getValue(59).toString() });
        queryVO2.setBaseGlOrgBook(voData2.getValue(59).toString());
      }

      queryVO2.setAccsubjCode(new String[] { (String)voData2.getValue(0) });
      GLQueryObj[] qryObjs = getQryObjs(voData2.getValue(50).toString(), strCode2);
      if (qryObjs == null) {
        MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000487"), strCode2 + NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000489"));
        return;
      }
      if (queryVO2.getCurrTypeName().equals(NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000116"))) {
        queryVO2.setCurrTypeName(voData2.getValue(3).toString());
        queryVO2.setPk_currtype(voData2.getValue(4).toString());
      }
      queryVO2.setEndYear(queryVO2.getYear());
      queryVO2.getFormatVO().setQryObjs(qryObjs);
      IUiPanel panel2 = (IUiPanel)this.m_parent.showNext("nc.ui.gl.assbalance.ToftPanelView");
      panel2.invoke(queryVO2, "SetQueryVO");
    }

    setButtonState();
  }

  public ToftPanelView getParentView()
  {
    return this.parentView;
  }

  public void setParentView(ToftPanelView parentView)
  {
    this.parentView = parentView;
  }
}