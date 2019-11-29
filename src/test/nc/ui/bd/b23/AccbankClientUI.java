package nc.ui.bd.b23;

import java.io.PrintStream;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.bd.ISettleCenter;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ToftPanel;
import nc.ui.trade.multiappinterface.MultiAppManager;
import nc.ui.trade.multiappinterface.PanelStack;
import nc.vo.bd.CorpVO;
import nc.vo.logging.Debug;

public class AccbankClientUI extends MultiAppManager
{
  private static final long serialVersionUID = -8705369404491445199L;

  public AccbankClientUI(FramePanel panel)
  {
    super(panel);
  }

  public String getFirstClassName()
  {
    if (isSettleCenter()) {
      return AccbankSettleUI.class.getName();
    }
    return AccbankNormalUI.class.getName();
  }

  private boolean isSettleCenter()
  {
    boolean isSettleCenter = false;
    try {
      ISettleCenter scService = (ISettleCenter)NCLocator.getInstance().lookup(ISettleCenter.class.getName());
      isSettleCenter = scService.isSettleCenter(getClientEnvironment().getCorporation().getPk_corp());

      return isSettleCenter;
    } catch (Exception e) {
      Debug.error(e.getMessage(), e);
    }
    return isSettleCenter;
  }

  public boolean onClosing() {
    try {
      boolean blnClosed = true;
      if (!getStack().isEmpty())
      {
        blnClosed = (blnClosed) && (((ToftPanel)getStack().get(getStack().size() - 1)).onClosing());
      }

      return blnClosed;
    }
    catch (Exception e) {
      System.out.println("关闭当前窗口时出错！");
    }return true;
  }
}