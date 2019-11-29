package nc.ui.pub.print;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.print.datastruct.PreDownInfo;
import nc.ui.pub.print.datastruct.PrintCellData;
import nc.ui.pub.print.datastruct.PrintData;
import nc.ui.pub.print.datastruct.TemplateInfo;
import nc.ui.pub.print.debug.PrintDebug;
import nc.ui.pub.print.debug.PrintDebugData;
import nc.ui.pub.print.ide.printsetting.PrintSettingDlg;
import nc.ui.pub.print.ide.printsetting.PrintSettingModel;
import nc.ui.pub.print.output.AbstractTemplateOutputTask;
import nc.ui.pub.print.output.AbstractTemplateParser;
import nc.ui.pub.print.output.DefaultDataSourceOrganizer;
import nc.ui.pub.print.output.DefaultTemplateDataAccessor;
import nc.ui.pub.print.output.DefaultTemplateParser;
import nc.ui.pub.print.output.IDataSourceOrganizer;
import nc.ui.pub.print.output.ITemplateDataAccessor;
import nc.ui.pub.print.output.OutputJobUtils;
import nc.ui.pub.print.output.VoucherDataSourceOrganizer;
import nc.ui.pub.print.output.excel.ExcelOutputDialog;
import nc.ui.pub.print.output.excel.ExcelOutputSetting;
import nc.ui.pub.print.output.excel.ExcelOutputTask;
import nc.ui.pub.print.output.preview.PreviewPanel;
import nc.ui.pub.print.output.preview.PreviewTask;
import nc.ui.pub.print.output.printer.ShareEnvBatchPrintTask;
import nc.ui.pub.print.output.printer.ShareEnvTemplatePrintTask;
import nc.ui.pub.print.output.printer.SingleEnvTemplatePrintTask;
import nc.vo.pub.print.PrintCellVO;
import nc.vo.pub.print.PrintDatasourceVO;
import nc.vo.pub.print.PrintTempletmanageHeaderVO;
import nc.vo.pub.print.PrintVO;

public class PrintEntry
{
  private Container parent = null;

  private IDataSourceOrganizer m_dsOrganizer = new DefaultDataSourceOrganizer();

  private ITemplateDataAccessor m_printDataAccessor = new DefaultTemplateDataAccessor();

  private AbstractTemplateParser m_templateParser = null;

  private AbstractTemplateOutputTask m_outputTask = null;

  private String m_strPreString = null;

  private int batchExportType = 1;

  private IPrintListener printListener = null;

  public PrintEntry(Component parent)
  {
    this.parent = ((Container)parent);
  }

  public PrintEntry(Container parent, IDataSource dataListener)
  {
    this.parent = parent;

    this.m_dsOrganizer = new DefaultDataSourceOrganizer();

    if (dataListener != null)
      this.m_dsOrganizer.addDataSource(dataListener);
  }

  public PrintEntry(Component parent, IDataSource dataListener)
  {
    if (parent != null) {
      this.parent = ((Container)parent);
    }
    this.m_dsOrganizer = new DefaultDataSourceOrganizer();

    if (dataListener != null)
      this.m_dsOrganizer.addDataSource(dataListener);
  }

  public void setDataSource(IDataSource dataListener)
  {
    if (dataListener != null)
      this.m_dsOrganizer.addDataSource(dataListener);
  }

  /** @deprecated */
  public void setTemplateID(String templateID)
  {
    setTemplateID(templateID, true);
  }

  public void setTemplateID(String pkCorp, String funCode, String pkUser, String pkBusiType)
  {
    setTemplateID(pkCorp, funCode, pkUser, pkBusiType, null);
  }

  public void setTemplateID(String pkCorp, String funCode, String pkUser, String pkBusiType, String nodeKey)
  {
    setTemplateID(pkCorp, funCode, pkUser, pkBusiType, nodeKey, "1");
  }

  public void setTemplateID(String pkCorp, String funCode, String pkUser, String pkBusiType, String nodeKey, String orgtype)
  {
    try
    {
      this.m_printDataAccessor.setTemplateID(pkCorp, funCode, pkUser, pkBusiType, nodeKey, orgtype);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
  }

  /** @deprecated */
  public void setTemplateID(String templateID, boolean changeIDArray)
  {
    try
    {
      this.m_printDataAccessor.setTemplateID(templateID, changeIDArray);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      MessageDialog.showErrorDlg(this.parent, null, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000244"));
    }
  }

  public void setTemplateIDs(String[] templateIDs)
  {
    try {
      this.m_printDataAccessor.setTemplateIDs(templateIDs);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      MessageDialog.showErrorDlg(this.parent, null, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000244"));
    }
  }

  public int selectTemplate()
  {
    if ((this.m_printDataAccessor.getTemplateIDs() == null) || (this.m_printDataAccessor.getTemplateIDs().length == 0)) {
      MessageDialog.showErrorDlg(this.parent, null, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000246") + "," + NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000243"));
      return -1;
    }

    PrintTempletmanageHeaderVO[] templateHeaderVOs = this.m_printDataAccessor.getAllTemplates();

    if ((templateHeaderVOs == null) || (templateHeaderVOs.length == 0)) {
      MessageDialog.showErrorDlg(this.parent, null, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000247"));
      return -1;
    }

    if (templateHeaderVOs.length == 1) {
      try {
        this.m_printDataAccessor.setTemplateID(templateHeaderVOs[0].getCtemplateid(), true);
      } catch (Exception e) {
        Logger.error(e.getMessage(), e);
      }
      return 1;
    }

    if ((templateHeaderVOs != null) && (templateHeaderVOs.length > 0) && (this.m_strPreString != null) && (this.m_strPreString.length() != 0))
    {
      ArrayList al = new ArrayList();
      for (int i = 0; i < templateHeaderVOs.length; i++) {
        if ((templateHeaderVOs[i].getVtemplatecode() != null) && (templateHeaderVOs[i].getVtemplatecode().trim().length() != 0) && (!templateHeaderVOs[i].getVtemplatecode().trim().startsWith(this.m_strPreString))) {
          continue;
        }
        al.add(templateHeaderVOs[i]);
      }
      templateHeaderVOs = new PrintTempletmanageHeaderVO[al.size()];
      for (int i = 0; i < templateHeaderVOs.length; i++) {
        templateHeaderVOs[i] = ((PrintTempletmanageHeaderVO)al.get(i));
      }
    }
    if ((templateHeaderVOs == null) || (templateHeaderVOs.length == 0)) {
      MessageDialog.showErrorDlg(this.parent, null, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000247"));
      return -1;
    }

    String[] templateNames = new String[templateHeaderVOs.length];
    String[] templateIds = new String[templateHeaderVOs.length];
    PrintTemplateSelectDlg.TemplateComboBoxItem[] templates = new PrintTemplateSelectDlg.TemplateComboBoxItem[templateHeaderVOs.length];
    for (int i = 0; i < templateHeaderVOs.length; i++) {
      PrintTemplateSelectDlg.TemplateComboBoxItem item = new PrintTemplateSelectDlg.TemplateComboBoxItem();
      item.name = templateHeaderVOs[i].getVtemplatename();
      item.id = templateHeaderVOs[i].getCtemplateid();
      templates[i] = item;
    }

    PrintTemplateSelectDlg selectTemplate = new PrintTemplateSelectDlg(this.parent, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000248"));
    selectTemplate.addTemplate(templates);

    selectTemplate.setLocation(200, 150);
    selectTemplate.show();
    int operateFlag = selectTemplate.getOperateFlag();
    if (operateFlag == 2)
      return -1;
    String oldID = this.m_printDataAccessor.getTemplateID();

    String templateID = selectTemplate.getTemplateId();

    if (!templateID.equals(oldID)) {
      try {
        this.m_printDataAccessor.setTemplateID(templateID, true);
      } catch (Exception e) {
        Logger.error(e.getMessage(), e);
      }
    }

    return operateFlag;
  }

  public void preview()
  {
    PreviewTask task = getPreviewTask();
    if (task != null)
      task.run();
  }

  public PreviewPanel previewInPanel()
  {
    PreviewTask task = getPreviewTask();
    if (task != null)
      return task.showPanel();
    return null;
  }

  public void previewInDialog(boolean modal)
  {
    PreviewTask task = getPreviewTask();
    if (task != null)
      task.showDialog(modal);
  }

  private PreviewTask getPreviewTask()
  {
    if ((getTemplateID() == null) || (getTemplateID().trim().length() == 0)) {
      MessageDialog.showErrorDlg(this.parent, null, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000242") + "," + NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000243"));
      return null;
    }

    this.m_templateParser = new DefaultTemplateParser(this.m_dsOrganizer, this.m_printDataAccessor);

    String taskName = NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000915");
    if ((getTemplateInfo() != null) && (getTemplateInfo().getTemplateName() != null)) {
      taskName = NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000916") + getTemplateInfo().getTemplateName();
    }

    PreviewTask previewTask = new PreviewTask(this.parent, taskName, this.m_templateParser);
    previewTask.setPrintListener(getPrintListener());
    return previewTask;
  }

  public void print()
  {
    print(true);
  }

  public void print(boolean bool)
  {
    print(true, true);
  }

  public void print(boolean isShowProgressDlg, boolean isShowPrintSettingDlg)
  {
    if (!checkPrecondition()) {
      return;
    }
    outputToPrinter(isShowProgressDlg, isShowPrintSettingDlg);
  }

  public boolean beginBatchPrint()
  {
    this.m_dsOrganizer.setPrepared(false);

    int type = setBatchExportType();

    return type != -1;
  }

  public void endBatchPrint()
  {
    if (!checkPrecondition()) {
      return;
    }
    this.m_dsOrganizer.setPrepared(true);

    if (this.batchExportType == 2) {
      outputToExcel(null, true);
    }
    else if (this.batchExportType == 1) {
      outputToPrinter(true, true);
    }
    else if (this.batchExportType == 0)
      preview();
  }

  public boolean beginVoucherBatchPrint()
  {
    if (this.m_printDataAccessor.getPrintData().getTemplateInfo().getPagination() != 1) {
      throw new RuntimeException(NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000240"));
    }

    this.m_dsOrganizer = new VoucherDataSourceOrganizer(getTemplateInfo());

    int type = setBatchExportType();

    return type != -1;
  }

  public void endVoucherBatchPrint()
  {
    ((VoucherDataSourceOrganizer)this.m_dsOrganizer).organizeVouchers();
    endBatchPrint();
  }

  public void refineVoucherDataSources()
  {
    if ((this.m_dsOrganizer != null) && (this.m_dsOrganizer.getDataSourceCount() > 0)) {
      IDataSourceOrganizer tmpDses = new VoucherDataSourceOrganizer(getTemplateInfo());
      IDataSource tmpDs = this.m_dsOrganizer.fetchNextDataSource();
      while (tmpDs != null) {
        tmpDses.addDataSource(tmpDs);
        tmpDs = this.m_dsOrganizer.fetchNextDataSource();
      }

      ((VoucherDataSourceOrganizer)tmpDses).organizeVouchers();
      this.m_dsOrganizer = tmpDses;
    }
  }

  public void cancel()
  {
    if (this.m_outputTask != null)
      this.m_outputTask.cancel();
  }

  public int exportExcelFile()
  {
    try
    {
      exportExcelFile2(true);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
    return 0;
  }

  int exportExcelFile(String fileName)
  {
    try
    {
      exportExcelFile2(fileName, true);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
    }
    return 0;
  }

  public void exportExcelFile2(boolean isAppend)
    throws Exception
  {
    exportExcelFile2(null, isAppend);
  }

  public void exportExcelFile2(String fileName, boolean isAppend)
    throws Exception
  {
    if (!checkPrecondition()) {
      return;
    }
    outputToExcel(fileName, isAppend);
  }

  /** @deprecated */
  public int exportFtpFile(String fileUrl, int port, String user, String password)
  {
    return 0;
  }

  /** @deprecated */
  public int exportHtmlFile(String fileName)
  {
    return 0;
  }

  /** @deprecated */
  public int exportTextFile()
  {
    return 0;
  }

  /** @deprecated */
  int exportTextFile(String splitStr, String fileName, boolean valueStyle)
  {
    return 0;
  }

  public IDataSource getDataSource()
  {
    if (this.m_dsOrganizer == null)
      return null;
    return this.m_dsOrganizer.getDataSource();
  }

  public PrintSettingModel getPrintSettingModel()
  {
    return this.m_printDataAccessor.getPrintSettingModel();
  }

  public TemplateInfo getTemplateInfo()
  {
    return this.m_printDataAccessor.getPrintData().getTemplateInfo();
  }

  public String getTemplateID()
  {
    return this.m_printDataAccessor.getTemplateID();
  }

  public int getBeginPageNo()
  {
    return getPrintSettingModel().getPageFrom();
  }

  public int getBreakPos()
  {
    if (getTemplateInfo() != null)
      return getTemplateInfo().getBreaPos();
    return 0;
  }

  public int getCopies()
  {
    return getPrintSettingModel().getCopies();
  }

  public int getEndPageNo()
  {
    return getPrintSettingModel().getPageTo();
  }

  public int getPageNumber()
  {
    return PrintEnvironment.getInstance().getCurTaskPageIndex();
  }

  public int getPagesCount()
  {
    return Math.max(0, getEndPageNo() - getBeginPageNo() + 1);
  }

  public PageSelectInfo getPageSelectInfo()
  {
    PageSelectInfo psi = new PageSelectInfo();
    psi.setCopies(getPrintSettingModel().getCopies());
    psi.setFromPage(getPrintSettingModel().getPageFrom());
    psi.setPageType(getPrintSettingModel().getPageWholeType());
    psi.setSelectType(getPrintSettingModel().getPageRangeType());
    if (getTemplateInfo() != null)
      psi.setStartShowPageNum(getTemplateInfo().getInitStartPageNo());
    psi.setToPage(getPrintSettingModel().getPageTo());
    return psi;
  }

  public PreDownInfo getPreDownInfo()
  {
    return getTemplateInfo().getPreDownInfo();
  }

  public boolean isSelectedAll()
  {
    return getPageSelectInfo().getSelectType() == 0;
  }

  public void setBeginPageNo(int newBeginPageNo)
  {
    getPrintSettingModel().setPageFrom(newBeginPageNo);
  }

  public void setCopies(int newCopies)
  {
    getPrintSettingModel().setCopies(newCopies);
  }

  public void setEndPageNo(int newEndPageNo)
  {
    getPrintSettingModel().setPageTo(newEndPageNo);
  }

  /** @deprecated */
  public void setPageSelectInfo(PageSelectInfo newPageSelectInfo)
  {
    setPageSelectInfo(newPageSelectInfo, true);
  }

  /** @deprecated */
  public void setPageSelectInfo(PageSelectInfo psi, boolean changeShowInfo)
  {
    getPrintSettingModel().setCopies(psi.getCopies());
    getPrintSettingModel().setPageFrom(psi.getFromPage());
    getPrintSettingModel().setPageRangeType(psi.getSelectType());
    getPrintSettingModel().setPageWholeType(psi.getPageType());
    getPrintSettingModel().setPageTo(psi.getToPage());
    if (getTemplateInfo() != null)
      getTemplateInfo().setInitStartPageNo(psi.getStartShowPageNum());
  }

  public void setSelectedAll(boolean newSelectedAll)
  {
    if (newSelectedAll)
      getPrintSettingModel().setPageRangeType(0);
    else
      getPrintSettingModel().setPageRangeType(2);
  }

  public void setStartPageNum(int startShowPageNum, boolean isAdjustTotalNum)
  {
    if (getTemplateInfo() != null)
      getTemplateInfo().setInitStartPageNo(startShowPageNum);
  }

  public int getInitStartPageNo()
  {
    if (getTemplateInfo() != null)
      return getTemplateInfo().getInitStartPageNo();
    return 0;
  }

  /** @deprecated */
  public void setInitStartPageNo(int initStartPageNo)
  {
    setStartPageNum(initStartPageNo, false);
  }

  public void setPreString(String prestring)
  {
    this.m_strPreString = prestring;
  }

  private int setBatchExportType()
  {
    ArrayList list = new ArrayList();

    String[] ss1 = { NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-001112"), NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000249"), NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000250") };

    list.add(NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000251"));

    UIComboBox combo = new UIComboBox();
    for (int i = 0; i < ss1.length; i++) {
      combo.addItem(ss1[i]);
    }
    list.add(combo);

    if (NCOptionPane.showConfirmDialog(this.parent, list.toArray(new Object[list.size()]), NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000252"), 2, -1) != 0) {
      this.batchExportType = -1;
    }
    else {
      this.batchExportType = combo.getSelectedIndex();
    }
    return this.batchExportType;
  }

  /** @deprecated */
  public void setPrintCell(PrintCellData[][] cell, TemplateInfo template)
  {
    this.m_printDataAccessor.getPrintData().setTemplateInfo(template);
    this.m_printDataAccessor.getPrintData().setCells(cell);
  }

  /** @deprecated */
  public boolean setPrintCell(BaseTable sp)
  {
    PrintData data = null;
    try {
      data = AccessTable.getPrintData(sp);
    } catch (Exception e) {
      Logger.error(e.getMessage(), e);
      MessageDialog.showErrorDlg(this.parent, null, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000244"));
      return false;
    }

    if (data == null) {
      return false;
    }
    this.m_printDataAccessor.setPrintData(data);

    return true;
  }

  /** @deprecated */
  public boolean setPrintCell(PrintData printData)
  {
    if (printData == null) {
      return false;
    }
    this.m_printDataAccessor.setPrintData(printData);

    return true;
  }

  public void setPrintListener(IPrintListener printListener)
  {
    this.printListener = printListener;
  }

  public IPrintListener getPrintListener()
  {
    return this.printListener;
  }

  public int getPrintType()
  {
    return this.m_printDataAccessor.getPrintSettingModel().getPrintType();
  }

  public void setPrintType(int printType)
  {
    this.m_printDataAccessor.getPrintSettingModel().setPrintType(printType);
  }

  private void outputToPrinter(boolean isShowProgressDlg, boolean isShowPrintSettingDlg)
  {
    boolean isBatchPrint = this.m_dsOrganizer.isBatchPrint();

    int mode = 1;
    if (isBatchPrint) {
      mode = 2;
    }
    PrintSettingModel psm = this.m_printDataAccessor.getPrintSettingModel();
    if (isShowPrintSettingDlg) {
      PrintSettingDlg dlg = new PrintSettingDlg(this.parent, this.m_printDataAccessor.getPrintData().getTemplateInfo(), mode);
      if (dlg.showModal() != 1) {
        return;
      }
      psm = dlg.getPrintSettingModel();
    }
    if (psm == null) {
      psm = new PrintSettingModel();
    }
    this.m_printDataAccessor.setPrintSettingModel(psm);

    if (PrintDebug.isOpen())
    {
      PrintDebugData.getInstance().reset();
    }

    this.m_templateParser = new DefaultTemplateParser(this.m_dsOrganizer, this.m_printDataAccessor);

    if (psm.getPrintJobMode() == 1) {
      if (!isBatchPrint) {
        this.m_outputTask = new ShareEnvTemplatePrintTask(this.parent, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000828"), this.m_templateParser);
      }
      else
      {
        this.m_outputTask = new ShareEnvBatchPrintTask(this.parent, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000917"), this.m_templateParser);
      }

    }
    else if (!isBatchPrint) {
      this.m_outputTask = new SingleEnvTemplatePrintTask(this.parent, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000828"), this.m_templateParser);
    }
    else {
      this.m_outputTask = new ShareEnvTemplatePrintTask(this.parent, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000917"), this.m_templateParser);
    }

    this.m_outputTask.setPrintListener(this.printListener);
    OutputJobUtils.start(this.m_outputTask, isShowProgressDlg, isShowProgressDlg);
  }

  private void outputToExcel(String excelFileName, boolean isAppend)
  {
    ExcelOutputSetting eos = null;

    boolean isMultiDs = this.m_dsOrganizer.getDataSourceCount() > 1;

    ExcelOutputDialog exportSettingDlg = new ExcelOutputDialog(this.parent, isMultiDs, excelFileName);
    exportSettingDlg.setPreferredSize(new Dimension(400, 400));

    if (exportSettingDlg.showModal() == 2) {
      return;
    }
    eos = exportSettingDlg.getOutputSetting();

    String destPath = eos.getExportDestPath();
    if ((destPath == null) || (destPath.trim().length() == 0)) {
      return;
    }
    if (this.m_templateParser == null) {
      this.m_templateParser = new DefaultTemplateParser(this.m_dsOrganizer, this.m_printDataAccessor);
    }

    this.m_outputTask = new ExcelOutputTask(this.parent, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000880"), this.m_templateParser, eos);

    OutputJobUtils.start(this.m_outputTask, true, true);
  }

  public ClientEnvironment getClientEnviroment()
  {
    return ClientEnvironment.getInstance();
  }

  private boolean checkPrecondition()
  {
    if (PrintControl.isTraining()) {
      MessageDialog.showErrorDlg(this.parent, null, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000918"));
      return false;
    }

    if ((getTemplateID() == null) || (getTemplateID().trim().length() == 0)) {
      MessageDialog.showErrorDlg(this.parent, null, NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000242") + "," + NCLangRes.getInstance().getStrByID("10100108", "UPP10100108-000243"));
      return false;
    }
    return true;
  }

  public PrintTempletmanageHeaderVO[] getAllTemplates() {
    return this.m_printDataAccessor.getAllTemplates();
  }

  public TemplateInfo getTemplate() {
    return this.m_printDataAccessor.getPrintData().getTemplateInfo();
  }

  public int dsCountInPool() {
    return this.m_dsOrganizer.getDataSourceCount();
  }

  public int getBatchExportType() {
    return this.batchExportType;
  }

  public void setBatchExportType(int batchExportType) {
    this.batchExportType = batchExportType;
  }

  public List<String> getUsedDataSourceVars()
  {
    if ((this.m_printDataAccessor instanceof DefaultTemplateDataAccessor)) {
      PrintVO vo = ((DefaultTemplateDataAccessor)this.m_printDataAccessor).getOldPrintVO();

      List vars = new ArrayList();

      if (vo == null) {
        return vars;
      }
      if ((vo.getDatasourceVOs() == null) || (vo.getDatasourceVOs().length == 0)) {
        return vars;
      }
      if ((vo.getCellVOs() == null) || (vo.getCellVOs().length == 0)) {
        return vars;
      }

      String strvars = null;
      for (PrintDatasourceVO dsVO : vo.getDatasourceVOs()) {
        for (PrintCellVO cellVO : vo.getCellVOs()) {
          if ((cellVO == null) || (dsVO == null) || (cellVO.getCcellcode() == null) || (dsVO.getCcellcode() == null) || (!cellVO.getCcellcode().trim().equals(dsVO.getCcellcode().trim())))
            continue;
          strvars = cellVO.getVvar();

          if (strvars == null) {
            continue;
          }
          String[] tmpVars = strvars.split("&");
          for (String tmpVar : tmpVars) {
            if ((tmpVar != null) && ((!tmpVar.startsWith("\"")) || (!tmpVar.endsWith("\"")))) {
              vars.add(tmpVar);
            }
          }
        }
      }
      return vars;
    }

    return new ArrayList();
  }

  public List<String> getUsedDataSourceVars(String templateId)
  {
    setTemplateID(templateId);
    return getUsedDataSourceVars();
  }
}