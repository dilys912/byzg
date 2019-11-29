package nc.ui.pub.querymodel;

import java.io.File;
import java.io.PrintStream;
import javax.swing.filechooser.FileFilter;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.queryengine.IFmdPrintInfo;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UILabel;
import nc.ui.reportquery.demo.ExampleFileFilter;
import nc.ui.reportquery.demo.FormatPanel;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.pub.headerlens.FmdPrintInfoNew;
import nc.vo.pub.headerlens.FmdprintinfoVO;
import nc.vo.pub.querymodel.FormatModelDef;
import nc.vo.pub.querymodel.FormatModelTree;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.sm.config.Account;

public class QueryNodeUI extends ToftPanel
{
  protected boolean canceled;
  private FormatPanel m_mainPanel = null;

  private String m_title = "";

  protected String m_dsName = null;

  protected ButtonObject m_boColSet = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000124"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000124"), -1, "公式列");

  protected ButtonObject m_boFilter = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000123"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000123"), -1, "过滤");

  protected ButtonObject m_boFind = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000121"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000121"), -1, "查找");

  protected ButtonObject m_boSort = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000122"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000122"), -1, "排序");

  protected ButtonObject m_boProcess = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000120"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000120"), -1, "数据处理");

  protected ButtonObject m_boRecover = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000125"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000125"), -1, "恢复");

  protected ButtonObject m_boRefresh = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000131"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000131"), -1, "刷新");

  private FormatModelDef m_fmd = null;

  protected ButtonObject m_boPrintMenu = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000126"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000126"), -1, "输出");

  protected ButtonObject m_boPrintPreview = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000128"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000128"), -1);

  protected ButtonObject m_boPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001262"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001262"), -1, "打印执行");

  protected ButtonObject m_boExportExcel = new ButtonObject(NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000129"), NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000129"), -1);
  protected ButtonObject[] m_btngrpUI;
  protected UIFileChooser m_fileChooser;

  public QueryNodeUI()
  {
    this.m_boPrintMenu.setChildButtonGroup(new ButtonObject[] { this.m_boPrintPreview, this.m_boExportExcel });

    this.m_boProcess.setChildButtonGroup(new ButtonObject[] { this.m_boFind, this.m_boFilter, this.m_boSort, this.m_boColSet, this.m_boRecover });

    this.m_btngrpUI = new ButtonObject[] { this.m_boRefresh, this.m_boProcess, this.m_boPrintMenu };

    this.m_fileChooser = null;

    initialize();
  }

  public QueryNodeUI(FramePanel fp)
  {
    this.m_boPrintMenu.setChildButtonGroup(new ButtonObject[] { this.m_boPrintPreview, this.m_boExportExcel });

    this.m_boProcess.setChildButtonGroup(new ButtonObject[] { this.m_boFind, this.m_boFilter, this.m_boSort, this.m_boColSet, this.m_boRecover });

    this.m_btngrpUI = new ButtonObject[] { this.m_boRefresh, this.m_boProcess, this.m_boPrintMenu };

    this.m_fileChooser = null;

    setFrame(fp);
    initialize();
  }

  public String checkPrerequisite()
  {
    if (isCanceled())
    {
      System.out.println("it won't come out");
      return "___DONT_SHOW_FRAME";
    }
    return null;
  }

  public FormatModelDef getFormatDef(String pkFmtDef, String dsName)
  {
    FormatModelDef fmd = null;
    System.out.println("数据源：" + dsName);
    if (dsName == null) {
      fmd = (FormatModelDef)FormatModelTree.getDefaultInstance().findObject(pkFmtDef);
    }
    else {
      fmd = (FormatModelDef)FormatModelTree.getInstance(dsName).findObject(pkFmtDef);
    }

    return fmd;
  }

  public FormatPanel getMainPanel()
  {
    return this.m_mainPanel;
  }

  public String getTitle()
  {
    return this.m_title;
  }

  private void handleException(Throwable exception)
  {
    System.out.println("--------- 未捕捉到的异常 ---------");
    exception.printStackTrace(System.out);
  }

  private void initialize()
  {
    try
    {
      setName("QueryNodeUI");
      setSize(774, 419);
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    this.m_mainPanel = new FormatPanel();
    add(this.m_mainPanel, "Center");
    initUI();
    setButtons(this.m_btngrpUI);
  }

  private void initUI()
  {
    try
    {
      String pkQryNode = getParameter("pkQryNode");
      this.m_dsName = getParameter("dsName");

      if ((this.m_dsName == null) || (this.m_dsName.equals("@INIT_DSN_DEFAULT@")))
      {
        String dsName = ClientEnvironment.getInstance().getConfigAccount().getDataSourceName();

        if (dsName == null) {
          dsName = "design";
        }
        this.m_dsName = dsName;
      }

      if (pkQryNode == null) {
        System.out.println("参数为空");
        this.canceled = true;
        return;
      }

      if (pkQryNode.indexOf(";") != -1) {
        String[] ids = QueryUtil.delimString(pkQryNode, ";");
        int iLen = ids.length;
        if (iLen == 3) {
          String lang = getClientEnvironment().getLanguage();
          if (lang != null)
            if (lang.equals("english")) {
              pkQryNode = ids[2];
              System.out.println("英文——" + pkQryNode);
            } else if (lang.equals("tradchn")) {
              pkQryNode = ids[1];
              System.out.println("中文繁体——" + pkQryNode);
            } else {
              pkQryNode = ids[0];
              System.out.println("中文简体——" + pkQryNode);
            }
        }
        else if (iLen > 1) {
          pkQryNode = ids[0];
        }
      }

      ModelUtil.buildQEObjectTree(this.m_dsName);

      FormatModelDef fmd = getFormatDef(pkQryNode, this.m_dsName);
      if (fmd == null) {
        String strHint = NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001535", null, new String[] { pkQryNode, this.m_dsName });

        System.out.println(strHint);
        MessageDialog.showWarningDlg(this, "UPP10241201-000099", strHint);

        this.canceled = true;
        return;
      }

      String moduleCode = getModuleCode();
      getMainPanel().setModuleCode(moduleCode);

      boolean bParamSet = getMainPanel().setFormatModelDef(fmd, true, this.m_dsName);

      if (!bParamSet) {
        System.out.println("参数设置被取消");
        this.canceled = true;
        return;
      }

      this.m_title = fmd.getDisplayName();
      setTitleText(this.m_title);

      this.m_fmd = fmd;
    } catch (Exception e) {
      this.canceled = true;
      e.printStackTrace();
    }
  }

  public boolean isCanceled()
  {
    return this.canceled;
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_boPrintPreview)
      onPrintPreview();
    else if (bo == this.m_boPrint)
      onPrint();
    else if (bo == this.m_boFind)
      onFind();
    else if (bo == this.m_boSort)
      onSort();
    else if (bo == this.m_boFilter)
      onFilter();
    else if (bo == this.m_boColSet)
      onFormulaCols();
    else if (bo == this.m_boRecover)
      onRecover();
    else if (bo == this.m_boRefresh)
      onRefresh();
    else if (bo == this.m_boExportExcel)
      onExportExcel();
  }

  public void onFilter()
  {
    getMainPanel().filter();
  }

  public void onFind()
  {
    getMainPanel().locateData();
  }

  public void onFormulaCols()
  {
    getMainPanel().handleFormulaCols();
  }

  public void onPrint()
  {
    try
    {
      preparePrint();
      getMainPanel().print();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onPrintPreview()
  {
    try
    {
      preparePrint();
      getMainPanel().preview();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onRecover()
  {
    getMainPanel().restore();
  }

  public void onRefresh()
  {
    getMainPanel().refresh();

    showHintMessage(NCLangRes.getInstance().getStrByID("10241201", "UCH007"));
  }

  public void onSort()
  {
    getMainPanel().sort();
  }

  private void preparePrint()
    throws Exception
  {
    String fmdID = this.m_fmd.getID();
    FmdprintinfoVO[] fmdprintinfoVOs = null;
    try {
      IFmdPrintInfo name = (IFmdPrintInfo)NCLocator.getInstance().lookup(IFmdPrintInfo.class.getName());

      fmdprintinfoVOs = name.getAllFmdPrintInfo(fmdID, this.m_dsName);
    } catch (Exception e) {
      System.out.println("查询打印格式表出错" + e.getMessage());
      Logger.error(e.getMessage(), e);
    }
    boolean isNewRecord = (fmdprintinfoVOs == null) || (fmdprintinfoVOs.length == 0);

    FmdPrintInfoNew printinfo = isNewRecord ? new FmdPrintInfoNew() : (FmdPrintInfoNew)fmdprintinfoVOs[0].getProp();

    String title = "";
    if (getFrame() != null) {
      title = getFrame().getLbTitle().getText();
    }

    getMainPanel().setPrintInfo(printinfo);
    getMainPanel().setPrinttitle(title);
  }

  protected UIFileChooser getFileChooser()
  {
    if (this.m_fileChooser == null) {
      this.m_fileChooser = new UIFileChooser();
      FileFilter newFilter = new ExampleFileFilter("xls");

      if (this.m_fileChooser.getFileFilter() != null) {
        this.m_fileChooser.removeChoosableFileFilter(this.m_fileChooser.getFileFilter());
      }

      String defaultname = getTitle() == null ? "" : getTitle();
      this.m_fileChooser.setSelectedFile(new File(defaultname));
      this.m_fileChooser.addChoosableFileFilter(newFilter);
      this.m_fileChooser.setFileFilter(newFilter);
    }
    return this.m_fileChooser;
  }

  public void onExportExcel()
  {
    try
    {
      getMainPanel().export2Excel();
    }
    catch (Exception e) {
      e.printStackTrace();
      MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-001269"), e.getMessage());
    }
  }

  public void setCanceled(boolean canceled)
  {
    this.canceled = canceled;
  }
}