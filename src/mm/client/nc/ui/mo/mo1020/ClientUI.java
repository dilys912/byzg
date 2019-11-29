package nc.ui.mo.mo1020;

import java.awt.BorderLayout;
import java.io.PrintStream;
import nc.ui.ml.NCLangRes;
import nc.ui.mm.pub.MMToftPanel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.vo.pub.BusinessException;

public class ClientUI extends MMToftPanel
  implements ILinkMaintain, ILinkQuery
{
  private BasePanel basePanel = null;
  private ButtonObject m_boAdd;
  private ButtonObject m_boDel;
  private ButtonObject m_boEdit;
  private ButtonObject m_boQuery;
  private ButtonObject m_boCopy;
  private ButtonObject m_boBatchRevise;
  private ButtonObject m_boSave;
  private ButtonObject m_boCancel;
  private ButtonObject m_boSwitch;
  private ButtonObject m_boViewSource;
  private ButtonObject m_boViewAtp;
  private ButtonObject m_boJoinQuery;
  private ButtonObject m_boBillAction;
  private ButtonObject m_boQueryAction;
  private ButtonObject m_boReviseAction;
  private ButtonObject m_boSimPut;
  private ButtonObject m_boMoPut;
  private ButtonObject m_boMoCancel;
  private ButtonObject m_boOutSubmit;
  private ButtonObject m_boRefresh;
  private ButtonObject m_boPrint;
  private ButtonObject m_boReturn;
  private ButtonObject m_borevisemo;
  private ButtonObject m_boqueryrevisemo;
  private ButtonObject[] m_aryButtons;
  private ButtonObject[] m_arySimput;

  public ClientUI()
  {
    initialize();
  }

  private BasePanel getBasePanel()
  {
    if (this.basePanel == null) {
      this.basePanel = new BasePanel(this);
    }
    return this.basePanel;
  }

  private String getstrbyid(String number) {
    return NCLangRes.getInstance().getStrByID("50081020", number);
  }

  public String getTitle()
  {
    String title = null;
    try {
      title = getBasePanel().getTempletTitle(); } catch (Exception ex) {
    }
    if (title == null) {
      title = getstrbyid("UPP50081020-000084");
    }
    return title;
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
      initButtons();
      setSize(774, 419);
      setLayout(new BorderLayout());
      add(getBasePanel(), "Center");
    }
    catch (Exception ivjExc) {
      handleException(ivjExc);
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_boQuery) {
      getBasePanel().onQuery();  //查询
      return;
    }
    if (bo == this.m_boAdd) {
      getBasePanel().onAdd(); //增加
      return;
    }
    if (bo == this.m_boDel) {
      getBasePanel().onDelete(); //删除
      return;
    }
    if (bo == this.m_boEdit) {
      try {
		getBasePanel().onModify();  //修改
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return;
    }
    if (bo == this.m_boSwitch) {
      getBasePanel().onSwitch();  //切换
      return;
    }
    if (bo == this.m_boViewSource) {
      getBasePanel().onViewSource();   //反查
      return;
    }
    if (bo == this.m_boSimPut) {
      getBasePanel().onSimPut();  //模拟投放
      return;
    }
    if (bo == this.m_boMoPut) {
      getBasePanel().onMoPut();  //订单投放
      return;
    }
    if (bo == this.m_boMoCancel) {
      getBasePanel().onMoCancel();  //取消投放
      return;
    }
    if (bo == this.m_boOutSubmit) {
      getBasePanel().onOutSubmit();  //委外
      return;
    }
    if (bo == this.m_boRefresh) {
      getBasePanel().onRefresh();  //刷新
      return;
    }
    if (bo == this.m_boSave) {
      getBasePanel().onSave();  //确定
      return;
    }
    if (bo == this.m_boCancel) {
      getBasePanel().onCancel();  //取消
      return;
    }
    if (bo == this.m_boPrint) {
      getBasePanel().onPrint();  //打印
      return;
    }
    if (bo == this.m_boReturn) {
      getBasePanel().onReturn();  //返回
      return;
    }
    if (bo == this.m_boJoinQuery) {
      getBasePanel().onJoinQuery();  //联查
      return;
    }
    if (bo == this.m_boViewAtp) {
      getBasePanel().onViewAtp();  //存量查询
      return;
    }
    if (bo == this.m_boCopy) {
      getBasePanel().onCopy();  //复制
      return;
    }
    if (bo == this.m_boBatchRevise) {
      getBasePanel().onBatchRevise();  //批改
      return;
    }
    if (bo == this.m_borevisemo) {
      try {
		getBasePanel().onRevisemo();
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return;
    }
    if (bo == this.m_boqueryrevisemo)
    {
      getBasePanel().onQueryrevisemo();
      return;
    }
  }

  public void onHelp()
  {
  }

  public boolean onClosing()
  {
    return getBasePanel().onClosing();
  }

  public void onPrint()
  {
  }

  public boolean onRefresh()
  {
    getBasePanel().onRefresh();
    return false;
  }

  public void setState(int iStat, String para)
  {
    switch (iStat)
    {
    case 0:
      this.m_boQuery.setEnabled(true);
      this.m_boAdd.setEnabled(true);
      this.m_boDel.setEnabled(false);
      this.m_boEdit.setEnabled(false);
      this.m_boReviseAction.setEnabled(false);

      this.m_boCopy.setEnabled(false);
      this.m_boBatchRevise.setEnabled(false);
      this.m_boSave.setEnabled(false);
      this.m_boCancel.setEnabled(false);
      this.m_boSwitch.setEnabled(false);
      this.m_boQueryAction.setEnabled(false);

      this.m_boBillAction.setEnabled(false);
      this.m_boRefresh.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      setButtons(this.m_aryButtons);
      break;
    case 1:
      this.m_boQuery.setEnabled(true);
      this.m_boAdd.setEnabled(true);
      this.m_boDel.setEnabled(true);

      this.m_boReviseAction.setEnabled(true);

      this.m_boEdit.setEnabled(true);
      this.m_boCopy.setEnabled(true);
      this.m_boBatchRevise.setEnabled(true);
      this.m_boSave.setEnabled(false);
      this.m_boCancel.setEnabled(false);
      this.m_boSwitch.setEnabled(true);
      this.m_boQueryAction.setEnabled(true);

      this.m_boBillAction.setEnabled(true);
      this.m_boRefresh.setEnabled(true);
      this.m_boPrint.setEnabled(true);
      this.m_boSwitch.setHint(getstrbyid("UPP50081020-000085"));
      setButtons(this.m_aryButtons);
      break;
    case 2:
      this.m_boSwitch.setHint(getstrbyid("UPP50081020-000086"));
      updateButtons();
      break;
    case 3:
    case 4:
    case 5:
    case 7:
      this.m_boQuery.setEnabled(false);
      this.m_boAdd.setEnabled(false);
      this.m_boDel.setEnabled(false);
      this.m_boEdit.setEnabled(false);
      this.m_boReviseAction.setEnabled(false);

      this.m_boCopy.setEnabled(false);
      this.m_boBatchRevise.setEnabled(false);
      this.m_boSave.setEnabled(true);
      this.m_boCancel.setEnabled(true);
      this.m_boSwitch.setEnabled(false);
      this.m_boQueryAction.setEnabled(false);

      this.m_boBillAction.setEnabled(false);
      this.m_boRefresh.setEnabled(false);
      this.m_boPrint.setEnabled(false);
      updateButtons();
      break;
    case 6:
      setButtons(this.m_arySimput);
    }
  }

  private void initButtons()
  {
    this.m_boAdd = new ButtonObject(NCLangRes.getInstance().getStrByID("50081020", "UPT50081020-000020"), getstrbyid("UPP50081020-000087"), 2, "增加");
    this.m_boDel = new ButtonObject(getstrbyid("UPT50081020-000021"), getstrbyid("UPP50081020-000088"), 2, "删除");
    this.m_boEdit = new ButtonObject(getstrbyid("UPT50081020-000022"), getstrbyid("UPP50081020-000089"), 2, "修改");
    this.m_boQuery = new ButtonObject(getstrbyid("UPT50081020-000039"), getstrbyid("UPP50081020-000090"), 2, "查询");
    this.m_boCopy = new ButtonObject(getstrbyid("UPT50081020-000034"), getstrbyid("UPP50081020-000091"), 2, "复制");
    this.m_boBatchRevise = new ButtonObject(getstrbyid("UPT50081020-000032"), getstrbyid("UPP50081020-000092"), 2, "批改");
    this.m_boSave = new ButtonObject(getstrbyid("UPT50081020-000027"), getstrbyid("UPP50081020-000093"), 2, "确定");
    this.m_boCancel = new ButtonObject(getstrbyid("UPT50081020-000028"), getstrbyid("UPP50081020-000094"), 2, "取消");
    this.m_boSwitch = new ButtonObject(getstrbyid("UPP50081020-000095"), getstrbyid("UPP50081020-000085"), 2, "切换");
    this.m_boViewSource = new ButtonObject(getstrbyid("UPT50081020-000035"), getstrbyid("UPP50081020-000096"), 2, "反查");
    this.m_boJoinQuery = new ButtonObject(getstrbyid("UPT50081020-000033"), getstrbyid("UPP50081020-000097"), 2, "联查");
    this.m_boViewAtp = new ButtonObject(getstrbyid("UPP50081020-000098"), getstrbyid("UPP50081020-000099"), 2, "存量查询");

    this.m_boBillAction = new ButtonObject(getstrbyid("UPT50081020-000031"), getstrbyid("UPT50081020-000021"), 2, "单据动作");
    this.m_boSimPut = new ButtonObject(getstrbyid("UPT50081020-000023"), getstrbyid("UPP50081020-000100"), 2, "模拟投放");
    this.m_boMoPut = new ButtonObject(getstrbyid("UPT50081020-000011"), getstrbyid("UPP50081020-000101"), 2, "订单投放");
    this.m_boMoCancel = new ButtonObject(getstrbyid("UPT50081020-000012"), getstrbyid("UPP50081020-000102"), 2, "取消投放");
    this.m_boOutSubmit = new ButtonObject(getstrbyid("UPT50081020-000010"), getstrbyid("UPP50081020-000103"), 2, "委外");

    this.m_boRefresh = new ButtonObject(getstrbyid("UPT50081020-000024"), getstrbyid("UPP50081020-000104"), 2, "刷新");
    this.m_boPrint = new ButtonObject(getstrbyid("UPT50081020-000029"), getstrbyid("UPP50081020-000105"), 2, "打印");
    this.m_borevisemo = new ButtonObject(getstrbyid("UPT50081020-000040"), getstrbyid("UPT50081020-000040"), 2, getstrbyid("UPT50081020-000040"));
    this.m_boqueryrevisemo = new ButtonObject(getstrbyid("UPT50081020-000041"), getstrbyid("UPT50081020-000041"), 2, getstrbyid("UPT50081020-000041"));
    this.m_boReviseAction = new ButtonObject(getstrbyid("UPT50081020-000042"), getstrbyid("UPT50081020-000042"), 2, getstrbyid("UPT50081020-000042"));

    this.m_boReviseAction.addChildButton(this.m_borevisemo);
    this.m_boReviseAction.addChildButton(this.m_boqueryrevisemo);

    this.m_boBillAction.addChildButton(this.m_boSimPut);
    this.m_boBillAction.addChildButton(this.m_boMoPut);
    this.m_boBillAction.addChildButton(this.m_boMoCancel);
    this.m_boBillAction.addChildButton(this.m_boOutSubmit);
    this.m_boQueryAction = new ButtonObject(getstrbyid("UPT50081020-000043"), getstrbyid("UPT50081020-000043"), 2, getstrbyid("UPT50081020-000043"));
    this.m_boQueryAction.addChildButton(this.m_boJoinQuery);
    this.m_boQueryAction.addChildButton(this.m_boViewSource);
    this.m_boQueryAction.addChildButton(this.m_boViewAtp);

    this.m_boReturn = new ButtonObject(getstrbyid("UPT50081020-000030"), getstrbyid("UPP50081020-000106"), 2, "返回");
    this.m_aryButtons = new ButtonObject[] { this.m_boAdd, this.m_boDel, this.m_boEdit, this.m_boReviseAction, this.m_boQueryAction, this.m_boQuery, this.m_boCopy, this.m_boBatchRevise, this.m_boSave, this.m_boCancel, this.m_boSwitch, this.m_boBillAction, this.m_boRefresh, this.m_boPrint };

    this.m_arySimput = new ButtonObject[] { this.m_boReturn };
  }

  public void doMaintainAction(ILinkMaintainData maintaindata)
  {
  }

  public void doQueryAction(ILinkQueryData querydata)
  {
    getBasePanel().getEditPanel().loadData(querydata.getBillType(), null, querydata.getBillID());
    getBasePanel().setState(2);
  }
}