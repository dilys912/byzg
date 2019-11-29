package nc.ui.gl.detail;

import nc.ui.pub.ButtonObject;

public class DetailToftPanel extends ToftPanelView
{
  protected void initButtons()
  {
    this.m_Buttons = new ButtonObject[3];
    this.m_Buttons[0] = new ButtonObject(ButtonKey.bnLC);
    this.m_Buttons[0].setTag(ButtonKey.bnLC);
    this.m_Buttons[0].setCode("联查凭证");
    this.m_Buttons[1] = new ButtonObject(ButtonKey.bnPrint);
    this.m_Buttons[1].setTag(ButtonKey.bnPrint);
    this.m_Buttons[1].setCode("打印");
    this.m_Buttons[2] = new ButtonObject(ButtonKey.bnReturn);
    this.m_Buttons[2].setTag(ButtonKey.bnReturn);
    this.m_Buttons[2].setCode("返回");
    setButtons(this.m_Buttons);
  }
}