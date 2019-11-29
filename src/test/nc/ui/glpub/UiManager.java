package nc.ui.glpub;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.EmptyStackException;

import org.apache.poi.poifs.property.Parent;

import nc.lfw.core.comp.MenubarComp;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.MessageListener;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.uif.pub.exception.UifRuntimeException;

public class UiManager extends ToftPanel
  implements IParent, PropertyChangeListener, ILinkApprove, ILinkMaintain, ILinkQuery
{
  PanelStack staPanel = null;
  MessageListener ml;
 
  
  public UiManager(FramePanel panel)
  {
    setFrame(panel);
   
    initialize();
  }

  public void addMessageListener(MessageListener ml) {
    super.addMessageListener(ml);
    this.ml = ml;

    IUiPanel framePanel = (IUiPanel)getStack().peek();
    framePanel.showMe(this);
    try {
      ToftPanel tp = (ToftPanel)framePanel;
      tp.removeMessageListener(ml);
      tp.addMessageListener(ml);
    }
    catch (Exception e)
    {
    }
  }

  public void closeMe()
  {
    try
    {
      if (getStack().size() > 1) {
        getStack().pop();
        IUiPanel framePanel = (IUiPanel)getStack().peek();
        framePanel.nextClosed();
      }
    } catch (EmptyStackException e) {
      System.out.println("没关系！");
    }
  }

  public ButtonObject[] getButtons()
  {
    return getCurrentPanel().getButtons();
  }

  public ClientEnvironment getClientEnvironment()
  {
    return super.getClientEnvironment();
  }

  public IUiPanel getCurrentPanel()
  {
    return (IUiPanel)getStack().peek();
  }

  public FramePanel getFrame()
  {
    return super.getFrame();
  }

  public PanelStack getStack() {
    if (this.staPanel == null) {
      this.staPanel = new PanelStack();
      this.staPanel.addPropertyChangeListener(this);
    }
    return this.staPanel;
  }

  public String getTitle()
  {
    return getCurrentPanel().getTitle();
  }

  public UIPanel getUiManager()
  {
    return this;
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
      setName("UiManager");

      GridLayout layout = new GridLayout();
      layout.setColumns(1);
      layout.setRows(1);
      setLayout(layout);
      setSize(774, 419);
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
    try
    {
      String strClassName = getParameter("classname");
      showNext(strClassName);
    } catch (Throwable err) {
      handleException(err);
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    getCurrentPanel().onButtonClicked(bo);
  }

  public boolean onClosing() {
    try {
      while (!getStack().isEmpty()) {
        boolean blnClosed = ((ToftPanel)getStack().get(getStack().size() - 1)).onClosing();

        if (!blnClosed)
          return blnClosed;
        getStack().pop();
      }
      return true;
    }
    catch (Exception e) {
      System.out.println("关闭当前窗口时出错！");
    }return true;
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("currentpanel"))
      showCurrentPanel();
  }

  private void showCurrentPanel() {
    try {
      removeAll();
      IUiPanel framePanel = (IUiPanel)getStack().peek();
      framePanel.showMe(this);
      try {
        ToftPanel tp = (ToftPanel)framePanel;
        tp.removeMessageListener(this.ml);
        tp.addMessageListener(this.ml);
      } catch (Exception e) {
        framePanel.invoke(this.ml, "addMessageListener");
      }

      setButtons(getButtons());
      try {
        setTitleText(framePanel.getTitle());
      }
      catch (NullPointerException e)
      {
      }
      validate();
      repaint();
    }
    catch (Throwable e)
    {
    }
  }

  public Object showFirst()
  {
    try
    {
      while (true)
      {
        if (getStack().size() <= 2) {
          getStack().pop();
          break;
        }
        getStack().popWithoutNotify();
      }
    } catch (EmptyStackException e) {
      System.out.println("没关系！");
    }
    return getStack().peek();
  }

  public Object showNext()
  {
    String strParaName = "classname" + getStack().size();
    String strClassName = getParameter(strParaName);
    return showNext(strClassName);
  }

  public Object showNext(String strFrameName)
  {
    try
    {
      IUiPanel newPanel = (IUiPanel)Class.forName(strFrameName).newInstance();

      getStack().push(newPanel);
      return newPanel;
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }

  public Object showNext(String strFrameName, Object[] objPara)
  {
    IUiPanel newPanel = null;
    try {
      Class moduleClass = Class.forName(strFrameName);
      Constructor cs = null;

      if (objPara != null)
      {
        Class[] paraClass = new Class[objPara.length];
        for (int i = 0; i < objPara.length; i++) {
          paraClass[i] = objPara[i].getClass();
        }

        try
        {
          cs = moduleClass.getConstructor(paraClass);
          newPanel = (IUiPanel)cs.newInstance(objPara);
        }
        catch (NoSuchMethodException e) {
          newPanel = (IUiPanel)moduleClass.newInstance();
        }
        getStack().push(newPanel);
        return newPanel;
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
    return newPanel;
  }

  public void showNext(IUiPanel newPanel)
  {
    try
    {
      getStack().push(newPanel);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public void updateButton(ButtonObject bo)
  {
    super.updateButton(bo);
  }

  public void updateButtons()
  {
    super.updateButtons();
  }

  public void doApproveAction(ILinkApproveData approvedata) {
    try {
      ILinkApprove app = (ILinkApprove)getCurrentPanel();
      if (app == null)
        return;
      app.doApproveAction(approvedata);
    } catch (ClassCastException e) {
      throw new UifRuntimeException("current Panel does not implement ILinkApprove");
    }
  }

  public void doMaintainAction(ILinkMaintainData maintaindata)
  {
    try
    {
      ILinkMaintain app = (ILinkMaintain)getCurrentPanel();
      if (app == null)
        return;
      app.doMaintainAction(maintaindata);
    } catch (ClassCastException e) {
      throw new UifRuntimeException("current Panel does not implement ILinkMaintain");
    }
  }

  public void doQueryAction(ILinkQueryData querydata)
  {
    try {
      ILinkQuery app = (ILinkQuery)getCurrentPanel();
      if (app == null)
        return;
      app.doQueryAction(querydata);
    } catch (ClassCastException e) {
      throw new UifRuntimeException("current Panel does not implement ILinkQuery");
    }
  }
}