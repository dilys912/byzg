package nc.ui.fa.fa20120301;

import java.awt.Container;
import nc.ui.fa.tools.FaQueryConditionClient;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIPanel;
import nc.vo.fa.fa20120301.QueryVO;
import nc.vo.pub.query.RefResultVO;

public class FilterDlg extends FaQueryConditionClient
{
  private String m_pk_corp = "";
  private QryPanel ivjUIPanel = null;

  public String getPk_corp() { return this.m_pk_corp;
  }

  public FilterDlg(Container parent, String pk_corp)
  {
    super(parent);

    if (!this.m_pk_corp.equals(pk_corp)) {
      this.ivjUIPanel = null;
      this.m_pk_corp = pk_corp;
    }

    initNormalPanel();
    initCondition();
  }

  public QueryVO getQryConditionVO()
  {
    QueryVO queryVO = getQueryPane().getNormalConditionVO();

    RefResultVO[] tmpvos = getMutiUnits();
    queryVO.setMutiUnit(tmpvos);
    return queryVO;
  }

  public QryPanel getQueryPane()
  {
    if (this.ivjUIPanel == null) {
      try {
        this.ivjUIPanel = new QryPanel(this.m_pk_corp);
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

  private void initCondition()
  {
    Object[][] comref = { { NCLangRes.getInstance().getStrByID("common", "UC000-0000270"), getQueryPane().getUIRefUseDept() }, { NCLangRes.getInstance().getStrByID("common", "UC000-0003127"), getQueryPane().getUIRefManDept() }, { NCLangRes.getInstance().getStrByID("common", "UC000-0003882"), getQueryPane().getUIRefAssCategory() }, { "period1", getQueryPane().getUIRefBeginPreiod() }, { "period2", getQueryPane().getUIRefEndPeriod() } };

    setCommonref(comref);
  }

  private void initNormalPanel()
  {
    getUIPanelNormal().add(getQueryPane());
  }
}