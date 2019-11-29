package nc.ui.fa.fa20120301pattern;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import nc.pub.fi.framework.common.command.ICommand;
import nc.pub.fi.framework.common.manager.ICommandManager;
import nc.ui.fa.uifactory.manage.FAManageEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.button.ButtonManager;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.fa.depdetail.DepdetailVO;

public class DepEventHandler extends FAManageEventHandler
{
  public DepEventHandler(BillManageUI billUI, IControllerBase control)
  {
    super(billUI, control);
  }

  public void setEnabled(int i, boolean bl) {
    super.getButtonManager().getButton(i).setEnabled(bl);
  }

  public boolean isEnabled(int i) {
    return super.getButtonManager().getButton(i).isEnabled();
  }

  protected void setTableModel()
  {
    Vector rows = new Vector();
    Vector columns = new Vector();
    columns.add("119");
    columns.add("foo");
    columns.add("bar");
    columns.add("ja");
    columns.add("ko");
    columns.add("zh");

    rows.add(columns);

    Vector columns1 = new Vector();

    columns1.add("2119");
    columns1.add("2foo");
    columns1.add("2bar");
    columns1.add("2ja");
    columns1.add("2ko");
    columns1.add("2zh");

    rows.add(columns1);

    Vector captions = new Vector();
    captions.add("ÐòºÅ");
    captions.add("ÐÕÃû");
    captions.add("¼®¹á");
    captions.add("×´Ì¬");
    captions.add("²¹³ä");
    captions.add("±¸×¢");

    BillModel bm = new BillModel();

    BillItem[] billItems = new BillItem[captions.size()];

    for (int i = 0; i < captions.size(); i++) {
      BillItem billItem = new BillItem();

      billItem.setPos(i + 1);
      billItem.setKey((String)captions.elementAt(i));
      billItem.setName((String)captions.elementAt(i));
      billItem.setWidth(80);
      billItem.setShow(true);

      billItem.setTableCode("deptotal");
      billItem.setTableName("deptotal");
      billItems[i] = billItem;
    }

    bm.setBodyItems(billItems);

    bm.setDataVector(rows);

    ((BillManageUI)getBillUI()).getBillListWrapper().getBillListPanel()
      .getBodyScrollPane("deptotal").setTableModel(bm);

    DepdetailVO depvo = new DepdetailVO();
    int len1 = Enum0301.m_sFields1.length;

    billItems = new BillItem[len1];
    int i = 0; for (int headlen = Enum0301.m_sHeader1.length; i < headlen; i++)
    {
      BillItem billItem = new BillItem();
      billItem.setPos(i + 1);
      billItem.setKey(Enum0301.m_sFields1[i]);
      billItem.setName(Enum0301.m_sHeader1[i]);
      billItem.setWidth(80);
      billItem.setShow(true);

      billItem.setTableCode(depvo.getTableCodes()[0]);
      billItem.setTableName(depvo.getTableNames()[0]);

      billItems[i] = billItem;
    }

    BillItem billItem = new BillItem();
    billItem.setPos(Enum0301.m_sHeader1.length);
    billItem.setKey(Enum0301.m_sFields1[Enum0301.m_sHeader1.length]);
    billItem.setName("fk_card");
    billItem.setWidth(80);
    billItem.setShow(false);

    billItem.setTableCode(depvo.getTableCodes()[0]);
    billItem.setTableName(depvo.getTableNames()[0]);

    billItems[Enum0301.m_sHeader1.length] = billItem;

    bm = new BillModel();
    bm.setBodyItems(billItems);
    Vector newItem = new Vector();
    Vector vecLine = new Vector();
    for (i = 0; i < billItems.length; i++) {
      newItem.add("111");
    }

    vecLine.add(newItem);
    bm.setDataVector(vecLine);

    ((BillManageUI)getBillUI()).getBillListWrapper().getBillListPanel()
      .getBodyScrollPane("workloan").setTableModel(bm);

    ((BillManageUI)getBillUI()).getBillCardPanel()
      .getBodyPanel("workloan").setTableModel(bm);
  }

  public void onBoAdd(ButtonObject bo) throws Exception
  {
    setTableModel();
  }

  public void onBoRefresh() throws Exception
  {
    Map cacheButtonState = ((DepdetailUI)getBillManageUI())
      .getButtonsState();

    ((DepdetailUI)getBillManageUI()).onRefresh();
    if (((DepdetailUI)getBillManageUI()).isIsdepdetailquery()) {
      ((DepdetailUI)getBillManageUI()).setButtonState();
    } else {
      if (cacheButtonState.isEmpty()) {
        return;
      }
      Iterator keys = cacheButtonState.keySet().iterator();
      while (keys.hasNext()) {
        Object key = keys.next();
        setEnabled(Integer.parseInt(key.toString()), 
          ((String)cacheButtonState.get(key.toString()))
          .equals("1"));
      }

      ((DepdetailUI)getBillManageUI()).updateFireButtons();
    }
  }

  public void onBoPrint()
    throws Exception
  {
    ((DepdetailUI)getBillManageUI()).onPrint();
  }

  public BillManageUI getBillManageUI2() {
    return (BillManageUI)getBillUI();
  }

  public void onBoSave()
    throws Exception
  {
    getCommandManager().getCommand(0).execute();
  }

  public void onBoCancel() throws Exception
  {
    getCommandManager().getCommand(7).execute();
    getCommandManager().getCommand(300).execute();
  }

  protected void onBoQuery() throws Exception
  {
    Vector bm = ((BillManageUI)getBillUI()).getBillCardPanel()
      .getBodyPanel("workloan").getTableModel().getDataVector();

    String tmp = null;
  }

  protected void onBoEdit()
    throws Exception
  {
    getCommandManager().getCommand(3).execute();
  }

  protected void buttonActionBefore(AbstractBillUI billUI, int intBtn)
    throws Exception
  {
    ((DepdetailUI)getBillManageUI()).setHeadItemEnabled(true);
  }
}