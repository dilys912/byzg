package nc.ui.gl.balancebooks;

import nc.ui.pub.FramePanel;

public class UiManager extends nc.ui.glpub.UiManager
{
  public UiManager(FramePanel panel)
  {
    super(panel);
  }

  protected String getParameter(String key)
  {
    if (key.trim().toLowerCase().equals("classname")) {
      return "nc.ui.gl.balancebooks.ToftPanelView";
    }
    return super.getParameter(key);
  }
}