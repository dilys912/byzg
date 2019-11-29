package nc.ui.pub.print.output.preview;

import java.awt.Container;
import nc.ui.pub.print.datastruct.PrintData;
import nc.ui.pub.print.debug.CloneUtil;
import nc.ui.pub.print.debug.PrintDebug;
import nc.ui.pub.print.debug.PrintDebugData;
import nc.ui.pub.print.output.AbstractTemplateOutputTask;
import nc.ui.pub.print.output.AbstractTemplateParser;
import nc.ui.pub.print.output.ITemplateDataAccessor;

public class PreviewTask extends AbstractTemplateOutputTask
{
  public PreviewTask(Container parent, String taskName)
  {
    super(parent, taskName);
  }

  public PreviewTask(Container parent, String taskName, AbstractTemplateParser parser) {
    super(parent, taskName, parser);
  }

  public void run()
  {
    if (PrintDebug.isOpen())
    {
      PrintData printData = getTemplateParser().getPrintDataAccessor().getPrintData();
      PrintDebugData.getInstance().setPrintData((PrintData)CloneUtil.deepClone(printData));
    }

    showFrame();
  }

  public void showFrame()
  {
    PreviewFrame previewFrame = new PreviewFrame(this.parent, this.m_taskName, this.m_templateParser);

    if (!previewFrame.refresh()) {
      return;
    }
    previewFrame.getPreviewCanvasPanel().setPrintListener(getPrintListener());
    previewFrame.previewPage();
    previewFrame.setVisible(true);
    previewFrame.setDefaultFocus();
  }

  public PreviewPanel showPanel()
  {
    PreviewPanel previewPanel = new PreviewPanel(this.parent, this.m_templateParser);

    if (!previewPanel.refresh()) {
      return null;
    }
    previewPanel.getCanvas().setPrintListener(getPrintListener());
    previewPanel.previewPage();

    return previewPanel;
  }

  public void showDialog(boolean modal)
  {
    PreviewDialog previewDlg = new PreviewDialog(this.parent, this.m_taskName, this, this.m_templateParser);

    previewDlg.setModal(modal);
    if (!previewDlg.refresh()) {
      return;
    }
    previewDlg.getPreviewCanvasPanel().setPrintListener(getPrintListener());
    previewDlg.show();
  }

  public int getEntireJobLength() {
    return 0;
  }

  public int getCurrentJobPoint() {
    return 0;
  }

  public String getHintMesage() {
    return null;
  }

  public String getDetailMesage() {
    return null;
  }

  public void cancelJob()
  {
  }
}