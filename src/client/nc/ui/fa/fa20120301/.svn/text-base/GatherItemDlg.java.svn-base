package nc.ui.fa.fa20120301;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;

public class GatherItemDlg extends UIDialog
  implements ActionListener
{
  private JPanel ivjUIDialogContentPane = null;
  Vector m_vContent = null;
  private JButton ivjBnCancel = null;
  private JButton ivjBnOk = null;
  private GatherItemPane ivjGatherItemPane1 = null;
  private boolean m_isEditable = false;

  public GatherItemDlg()
  {
    super(null);
    initialize();
  }

  public GatherItemDlg(Container parent)
  {
    super(parent);
    initialize();
  }

  public GatherItemDlg(Container parent, String title)
  {
    super(parent, title);
  }

  public GatherItemDlg(Frame owner)
  {
    super(owner);
  }

  public GatherItemDlg(Frame owner, String title)
  {
    super(owner, title);
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == getBnOk()) {
      saveData();
      closeOK();
      return;
    }
    if (e.getSource() == getBnCancel()) {
      resetData();
      closeCancel();
      return;
    }
  }

  private JButton getBnCancel()
  {
    if (this.ivjBnCancel == null) {
      try {
        this.ivjBnCancel = new UIButton();
        this.ivjBnCancel.setName("BnCancel");
        this.ivjBnCancel.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"));
        this.ivjBnCancel.setBounds(238, 274, 62, 21);
        this.ivjBnCancel.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBnCancel;
  }

  private JButton getBnOk()
  {
    if (this.ivjBnOk == null) {
      try {
        this.ivjBnOk = new UIButton();
        this.ivjBnOk.setName("BnOk");
        this.ivjBnOk.setText(NCLangRes.getInstance().getStrByID("common", "UC001-0000044"));
        this.ivjBnOk.setBounds(130, 273, 62, 21);
        this.ivjBnOk.setMargin(new Insets(0, 0, 0, 0));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBnOk;
  }

  private GatherItemPane getGatherItemPane1()
  {
    if (this.ivjGatherItemPane1 == null) {
      try {
        this.ivjGatherItemPane1 = new GatherItemPane();
        this.ivjGatherItemPane1.setName("GatherItemPane1");
        this.ivjGatherItemPane1.setLocation(3, 8);
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjGatherItemPane1;
  }

  private JPanel getUIDialogContentPane()
  {
    if (this.ivjUIDialogContentPane == null) {
      try {
        this.ivjUIDialogContentPane = new UIPanel();
        this.ivjUIDialogContentPane.setName("UIDialogContentPane");
        this.ivjUIDialogContentPane.setLayout(null);
        getUIDialogContentPane().add(getBnOk(), getBnOk().getName());
        getUIDialogContentPane().add(getGatherItemPane1(), getGatherItemPane1().getName());
        getUIDialogContentPane().add(getBnCancel(), getBnCancel().getName());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIDialogContentPane;
  }

  private void handleException(Throwable exception)
  {
  }

  private void initialize()
  {
    try
    {
      setName("GatherItemDlg");
      setDefaultCloseOperation(2);
      setResizable(false);
      setSize(440, 339);
      setTitle(NCLangRes.getInstance().getStrByID("201233", "UPP201233-000145"));
      setContentPane(getUIDialogContentPane());
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }

    getBnOk().addActionListener(this);
    getBnCancel().addActionListener(this);
  }

  public boolean isEditable()
  {
    return this.m_isEditable;
  }

  public void resetData()
  {
    getGatherItemPane1().resetData();
  }

  public String saveData()
  {
    return getGatherItemPane1().saveData();
  }

  public void setContent(Vector v)
  {
  }

  public void setEditable(boolean newEditable)
  {
    this.m_isEditable = newEditable;
    getBnOk().setEnabled(this.m_isEditable);
  }
}