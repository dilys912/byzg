package nc.ui.gl.assbalance;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import javax.swing.JFrame;
import nc.bs.logging.Log;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIPanel;
import nc.vo.glcom.balance.GlQueryVO;
import nc.vo.pub.BusinessException;

public class ToftPanelView extends ToftPanel
  implements IUiPanel
{
  private BalanceSubjAssView ivjView = null;
  private ButtonObject[] m_Buttons = null;
  public IParent p_parent;

  public ToftPanelView()
  {
    initialize();
  }

  public void addListener(Object objListener, Object objUserdata)
  {
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("20023030", "UPP20023030-000170");
  }

  public void updateButtons()
  {
    super.updateButtons();
  }

  private BalanceSubjAssView getView()
  {
    if (this.ivjView == null) {
      try {
        this.ivjView = new BalanceSubjAssView(this);
        this.ivjView.setName("View");
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjView;
  }

  private void handleException(Throwable exception)
  {
    Log.getInstance(getClass().getName()).info("--------- 未捕捉到的异常 ---------");
    exception.printStackTrace(System.out);
  }
  private void initButtons() {
    this.m_Buttons = new ButtonObject[13];
    this.m_Buttons[0] = new ButtonObject(ButtonKey.bnQRY);
    this.m_Buttons[0].setTag(ButtonKey.bnQRY);
    this.m_Buttons[0].setCode("查询");
    this.m_Buttons[1] = new ButtonObject(ButtonKey.bnLocate);
    this.m_Buttons[1].setTag(ButtonKey.bnLocate);
    this.m_Buttons[1].setCode("定位");
    this.m_Buttons[2] = new ButtonObject(ButtonKey.bnDetail);
    this.m_Buttons[2].setTag(ButtonKey.bnDetail);
    this.m_Buttons[2].setCode("明细");
    this.m_Buttons[3] = new ButtonObject(ButtonKey.bnAccum);
    this.m_Buttons[3].setTag(ButtonKey.bnAccum);
    this.m_Buttons[3].setCode("累计");
    this.m_Buttons[4] = new ButtonObject(ButtonKey.bnRefresh);
    this.m_Buttons[4].setTag(ButtonKey.bnRefresh);
    this.m_Buttons[4].setCode("刷新");
    this.m_Buttons[5] = new ButtonObject(ButtonKey.bnPrint);
    this.m_Buttons[5].setTag(ButtonKey.bnPrint);
    this.m_Buttons[5].setCode("打印");
    this.m_Buttons[6] = new ButtonObject(ButtonKey.bnRecover);
    this.m_Buttons[6].setTag(ButtonKey.bnRecover);
    this.m_Buttons[6].setCode("还原");
    this.m_Buttons[7] = new ButtonObject(ButtonKey.bnSwitch);
    this.m_Buttons[7].setTag(ButtonKey.bnSwitch);
    this.m_Buttons[7].setCode("转换");
    this.m_Buttons[8] = new ButtonObject(ButtonKey.bnFirst);
    this.m_Buttons[8].setTag(ButtonKey.bnFirst);
    this.m_Buttons[8].setCode("首页");
    this.m_Buttons[9] = new ButtonObject(ButtonKey.bnPriv);
    this.m_Buttons[9].setTag(ButtonKey.bnPriv);
    this.m_Buttons[9].setCode("上一页");
    this.m_Buttons[10] = new ButtonObject(ButtonKey.bnNext);
    this.m_Buttons[10].setTag(ButtonKey.bnNext);
    this.m_Buttons[10].setCode("下一页");
    this.m_Buttons[11] = new ButtonObject(ButtonKey.bnEnd);
    this.m_Buttons[11].setTag(ButtonKey.bnEnd);
    this.m_Buttons[11].setCode("末页");
    this.m_Buttons[12] = new ButtonObject(ButtonKey.bnReturn);
    this.m_Buttons[12].setTag(ButtonKey.bnReturn);
    this.m_Buttons[12].setCode("返回");
    setButtons(this.m_Buttons);

    this.m_Buttons[1].setVisible(false);

    this.m_Buttons[3].setVisible(false);
    this.m_Buttons[6].setVisible(false);
    this.m_Buttons[12].setVisible(false);
  }

  private void initButtonsForLC() {
    this.m_Buttons = new ButtonObject[13];
    this.m_Buttons[0] = new ButtonObject(ButtonKey.bnQRY);
    this.m_Buttons[0].setTag(ButtonKey.bnQRY);
    this.m_Buttons[0].setCode("查询");
    this.m_Buttons[1] = new ButtonObject(ButtonKey.bnLocate);
    this.m_Buttons[1].setTag(ButtonKey.bnLocate);
    this.m_Buttons[1].setCode("定位");
    this.m_Buttons[2] = new ButtonObject(ButtonKey.bnDetail);
    this.m_Buttons[2].setTag(ButtonKey.bnDetail);
    this.m_Buttons[2].setCode("明细");
    this.m_Buttons[3] = new ButtonObject(ButtonKey.bnAccum);
    this.m_Buttons[3].setTag(ButtonKey.bnAccum);
    this.m_Buttons[3].setCode("累计");
    this.m_Buttons[4] = new ButtonObject(ButtonKey.bnRefresh);
    this.m_Buttons[4].setTag(ButtonKey.bnRefresh);
    this.m_Buttons[4].setCode("刷新");
    this.m_Buttons[5] = new ButtonObject(ButtonKey.bnPrint);
    this.m_Buttons[5].setTag(ButtonKey.bnPrint);
    this.m_Buttons[5].setCode("打印");
    this.m_Buttons[6] = new ButtonObject(ButtonKey.bnRecover);
    this.m_Buttons[6].setTag(ButtonKey.bnRecover);
    this.m_Buttons[6].setCode("还原");
    this.m_Buttons[7] = new ButtonObject(ButtonKey.bnSwitch);
    this.m_Buttons[7].setTag(ButtonKey.bnSwitch);
    this.m_Buttons[7].setCode("转换");

    this.m_Buttons[8] = new ButtonObject(ButtonKey.bnFirst);
    this.m_Buttons[8].setTag(ButtonKey.bnFirst);
    this.m_Buttons[8].setCode("首页");
    this.m_Buttons[9] = new ButtonObject(ButtonKey.bnPriv);
    this.m_Buttons[9].setTag(ButtonKey.bnPriv);
    this.m_Buttons[9].setCode("上一页");
    this.m_Buttons[10] = new ButtonObject(ButtonKey.bnNext);
    this.m_Buttons[10].setTag(ButtonKey.bnNext);
    this.m_Buttons[10].setCode("下一页");
    this.m_Buttons[11] = new ButtonObject(ButtonKey.bnEnd);
    this.m_Buttons[11].setTag(ButtonKey.bnEnd);
    this.m_Buttons[11].setCode("末页");
    this.m_Buttons[12] = new ButtonObject(ButtonKey.bnReturn);
    this.m_Buttons[12].setTag(ButtonKey.bnReturn);
    this.m_Buttons[12].setCode("返回");

    this.m_Buttons[0].setVisible(false);
    this.m_Buttons[1].setVisible(false);
    this.m_Buttons[3].setVisible(false);
    this.m_Buttons[4].setVisible(false);
    this.m_Buttons[6].setVisible(false);
    this.m_Buttons[8].setVisible(false);
    this.m_Buttons[9].setVisible(false);
    this.m_Buttons[10].setVisible(false);
    this.m_Buttons[11].setVisible(false);
    setButtons(this.m_Buttons);
  }

  private void initialize()
  {
    try
    {
      initButtons();
      setName("ToftPanelView");
      setLayout(new BorderLayout());
      setSize(774, 419);
      add(getView(), "Center");
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getView().setBillModel(new BalanceSubjAssModel());
  }

  public Object invoke(Object objData, Object objUserData)
  {
    if ((objUserData != null) && (objUserData.toString().equals("SetQueryVO")))
    {
      initButtonsForLC();
      getView().setQueryVos((GlQueryVO)((GlQueryVO)objData).clone());
    }
    return null;
  }

  public static void main(String[] args)
  {
    try
    {
      JFrame frame = new JFrame();

      ToftPanelView aToftPanelView = new ToftPanelView();
      frame.setContentPane(aToftPanelView);
      frame.setSize(aToftPanelView.getSize());
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
      System.err.println("nc.ui.pub.ToftPanel 的 main() 中发生异常");
      exception.printStackTrace(System.out);
    }
  }

  public void nextClosed()
  {
  }

  public void onButtonClicked(ButtonObject bo)
  {
    try
    {
      for (int i = 0; i < this.m_Buttons.length; i++) {
        if (bo.equals(this.m_Buttons[i]))
          getView().tackleBnsEvent(i);
      }
    }
    catch (BusinessException ex)
    {
      showErrorMessage(ex.getMessage());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void removeListener(Object objListener, Object objUserdata)
  {
  }

  public void showMe(IParent parent)
  {
    parent.getUiManager().removeAll();
    parent.getUiManager().add(this, getName());
    this.p_parent = parent;
    getView().setIParent(this.p_parent);
  }
}