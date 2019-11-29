package nc.ui.fa.fa20120301;

import java.awt.Container;
import nc.ui.fa.base.FAQueryDlg;
import nc.ui.pub.beans.UIPanel;
import nc.vo.fa.fa20120301.QueryVO;

public class QryDLGModel extends FAQueryDlg
{
  private QryPanel ivjUIPanel = null;

  public QryDLGModel()
  {
    initialize();
  }

  public QryDLGModel(Container parent)
  {
    super(parent);
    initialize();
  }

  public QueryVO getQryConditionVO()
  {
    QueryVO queryVO = getQueryPane().getNormalConditionVO();
    return queryVO;
  }

  public QryPanel getQueryPane()
  {
    if (this.ivjUIPanel == null) {
      try {
        this.ivjUIPanel = new QryPanel("");
        this.ivjUIPanel.setName("QueryPane");
        this.ivjUIPanel.setLocation(0, 0);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIPanel;
  }

  public void initialize()
  {
    initNormalPanel();
  }

  public void initNormalPanel()
  {
    getUIPanelNormal().add(getQueryPane());
  }
}