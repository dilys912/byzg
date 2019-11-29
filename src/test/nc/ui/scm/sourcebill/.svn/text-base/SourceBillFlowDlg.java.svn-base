package nc.ui.scm.sourcebill;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenu;
import nc.ui.pub.beans.UIMenuBar;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.scm.sourcebill.LightBillVO;

public class SourceBillFlowDlg extends UIDialog
  implements ActionListener
{
  private static final long serialVersionUID = 1L;
  String billID = null;

  String billType = null;

  String userID = null;

  String pk_corp = null;

  String bizType = null;

  BillFlowViewer m_panelBillFlowView = null;

  UIMenuBar mainMenuBar = new UIMenuBar();

  UIMenu opMenu = new UIMenu(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000741"));

  UIMenuItem exitMenu = new UIMenuItem(NCLangRes.getInstance().getStrByID("scmcommon", "UPPSCMCommon-000272"));

  UIMenuItem sourceMenu = new UIMenuItem(NCLangRes.getInstance().getStrByID("common", "UC000-0002745"));

  UIMenuItem resetMenu = new UIMenuItem(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000742"));

  public SourceBillFlowDlg(Container parent, String billType, String billID, String bizType, String userID, String pk_corp)
  {
    super(parent);

    this.mainMenuBar.add(this.opMenu);
    this.opMenu.insert(this.resetMenu, 0);
    this.opMenu.insert(this.sourceMenu, 1);
    this.opMenu.insertSeparator(2);
    this.opMenu.insert(this.exitMenu, 3);
    this.resetMenu.addActionListener(this);
    this.exitMenu.addActionListener(this);
    this.sourceMenu.addActionListener(this);

    this.billID = billID;
    this.billType = billType;
    this.bizType = bizType;
    this.userID = userID;
    this.pk_corp = pk_corp;
    LightBillVO voBillInfo = new LightBillVO();
    voBillInfo.setID(billID);
    voBillInfo.setType(billType);
    voBillInfo.setBizType(bizType);
    voBillInfo.setUserID(userID);
    voBillInfo.setPk_corp(pk_corp);
    init(voBillInfo);
  }

  public SourceBillFlowDlg(Container parent, LightBillVO voBillInfo)
  {
    super(parent);

    this.mainMenuBar.add(this.opMenu);
    this.opMenu.insert(this.resetMenu, 0);
    this.opMenu.insert(this.sourceMenu, 1);
    this.opMenu.insertSeparator(2);
    this.opMenu.insert(this.exitMenu, 3);
    this.resetMenu.addActionListener(this);
    this.exitMenu.addActionListener(this);
    this.sourceMenu.addActionListener(this);

    this.billID = voBillInfo.getID();
    this.billType = voBillInfo.getType();
    this.bizType = voBillInfo.getBizType();
    this.userID = voBillInfo.getUserID();
    this.pk_corp = voBillInfo.getPk_corp();
    init(voBillInfo);
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == this.exitMenu) {
      dispose();
      setVisible(false);
      closeOK();
    } else if (e.getSource() == this.resetMenu) {
      this.m_panelBillFlowView.initBillNodes();
    } else if (e.getSource() == this.sourceMenu) {
      BillNodePanel node = this.m_panelBillFlowView.getSelectedNode();
      if ((node != null) && (node.getModel() != null)) {
        SourceBillFlowDlg f = new SourceBillFlowDlg(this, node.getModel().getType(), node.getModel().getID(), this.bizType, this.userID, this.pk_corp);

        f.showModal();
      }
    }
  }

  private void init(LightBillVO voBillInfo)
  {
    setJMenuBar(this.mainMenuBar);
    setModal(true);
    Color color = getContentPane().getBackground();
    Font font = new Font(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000735"), 0, 12);

    this.mainMenuBar.setBackground(color);
    this.mainMenuBar.setFont(font);
    this.opMenu.setBackground(color);
    this.opMenu.setFont(font);
    this.exitMenu.setBackground(color);
    this.exitMenu.setFont(font);
    this.resetMenu.setBackground(color);
    this.resetMenu.setFont(font);
    this.sourceMenu.setBackground(color);
    this.sourceMenu.setFont(font);
    setTitle(NCLangRes.getInstance().getStrByID("scmpub", "UPPscmpub-000743"));

    setSize(700, 520);
    getContentPane().setLayout(new BorderLayout());

    LightBillVO voRet = querySourceBillVO(voBillInfo);
    if (voRet == null) {
      System.out.println("query bill graph ERROR.");
      return;
    }
    this.m_panelBillFlowView = new BillFlowViewer(voRet, voBillInfo.getBizType(), voBillInfo.getUserID(), voBillInfo.getPk_corp());

    UIScrollPane scrollPane = new UIScrollPane(this.m_panelBillFlowView);
    getContentPane().add(scrollPane, "Center");
    UIPanel bottomPanel = new UIPanel();
    bottomPanel.setPreferredSize(new Dimension(1, 20));
    getContentPane().add(bottomPanel, "South");
  }

  private LightBillVO querySourceBillVO(LightBillVO voBillInfo)
  {
    LightBillVO voRet = null;
    try
    {
      voRet = SourceBillHelper.querySourceBillGraph(voBillInfo);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return voRet;
  }
}