package nc.ui.cu;

import javax.swing.JComponent;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.MultiLangTrans;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;

public class ClientUI extends AbstractClientUI
{
  public void afterEdit(BillEditEvent e)
  {
    super.afterEdit(e);
    try
    {
      String table_code = e.getTableCode();

      if (table_code == null)
      {
        if (e.getKey().equals("custname")) {
          afterEditCustName(e);
        }

        if (e.getKey().equals("custprop")) {
          afterEditCustProp(e);
        }
        //客商申请通信地址取值于地点档案 by src 2018年4月14日20:48:59
        	if(e.getKey().equals("txaddress")){
        		String conaddr = this.getBillCardPanel().getHeadItem("conaddr").getValue();
        		if(conaddr==null||conaddr.equals("")){
        			this.getBillCardPanel().getHeadItem("conaddr").setValue(e.getValue());
        		}
        	}

      }
      else if (table_code.equals("BANK")) {
        if (e.getKey().equals("account")) {
          afterEditAccount(e);
        }
        if (e.getKey().equals("defflag")) {
          afterEditDefaultBank(e);
        }

      }
      else
      {
        if (e.getKey().equals("defaddrflag")) {
          afterEditDefaultAddress(e);
        }

        if (e.getKey().equals("addrname"))
          afterEditCustAddress(e);
      }
    }
    catch (Exception ex)
    {
      handleException(ex);
    }
  }

  private void afterEditCustName(BillEditEvent e) {
    String name = e.getValue() == null ? "" : e.getValue().toString();
    if ((name != null) && (name.length() > 0))
      if (name.length() > 40)
        getBillCardPanel().setHeadItem("custshortname", 
          name.substring(0, 40));
      else
        getBillCardPanel().setHeadItem("custshortname", name);
  }

  private void afterEditCustProp(BillEditEvent e)
  {
    boolean change = e.getValue() != null;

    if (change) {
      int rowCount = getBillCardPanel().getBillModel("BANK")
        .getRowCount();
      int[] row = new int[rowCount];
      for (int i = 0; i < rowCount; i++) {
        row[i] = i;
      }
      getBillCardPanel().getBillModel("BANK").delLine(row);
      if (e.getValue().equals(
        NCLangRes.getInstance().getStrByID("10080804", 
        "UPP10080804-000028")))
      {
        getBillCardPanel().getHeadItem("pk_corp1").setEnabled(false);
        getBillCardPanel().getHeadItem("pk_corp1").setValue(null);
      }
      else
      {
        getBillCardPanel().getHeadItem("pk_corp1").setEnabled(
          getBillCardPanel().getHeadItem("custprop")
          .getComponent().isEnabled());
      }
    }
  }

  private void afterEditDefaultBank(BillEditEvent e)
  {
    BillModel bankModel = getBillCardPanel().getBillModel("BANK");
    Boolean obj = (Boolean)bankModel.getValueAt(e.getRow(), "defflag");
    if ((obj != null) && (obj.booleanValue()))
      for (int i = 0; i < bankModel.getRowCount(); i++)
      {
        if ((!((Boolean)bankModel.getValueAt(i, "defflag"))
          .booleanValue()) || 
          (i == e.getRow())) continue;
        bankModel.setValueAt(new Boolean(false), i, "defflag");
        int old_rowState = bankModel.getRowState(i);
        if (old_rowState == 0)
          bankModel.setRowState(i, 2);
      }
  }

  private void afterEditAccount(BillEditEvent e)
    throws ValidationException
  {
    BillModel bankModel = getBillCardPanel().getBillModel("BANK");

    Object temp = bankModel.getValueAt(e.getRow(), "account");
    if ((temp == null) || (temp.toString().length() == 0))
      return;
    int RowCount = bankModel.getRowCount();
    if (RowCount == 1) {
      bankModel.setValueAt(new Boolean(true), 0, "defflag");
    }
    for (int j = 0; j < bankModel.getRowCount(); j++) {
      if (j == e.getRow())
        continue;
      if (bankModel.getValueAt(j, "account") == null)
        continue;
      if (temp.equals(bankModel.getValueAt(j, "account"))) {
        bankModel.setValueAt(null, e.getRow(), "account");
        throw new ValidationException(
          MultiLangTrans.getTransStr("MC1", new String[] { "[" + 
          NCLangRes.getInstance().getStrByID(
          "10080804", "UC000-0004118") + 
          "]" }));
      }
    }
  }

  private void afterEditCustAddress(BillEditEvent e) throws ValidationException
  {
    BillModel addrModel = getBillCardPanel().getBillModel("ADDR");
    Object temp = addrModel.getValueAt(e.getRow(), "addrname");
    if ((temp == null) || (temp.toString().length() == 0))
      return;
    int RowCount = addrModel.getRowCount();
    if (RowCount == 1) {
      addrModel.setValueAt(new Boolean(true), e.getRow(), "defaddrflag");
    }
    for (int j = 0; j < addrModel.getRowCount(); j++) {
      if (j == e.getRow())
        continue;
      if (addrModel.getValueAt(j, "addrname") == null)
        continue;
      if (temp.equals(addrModel.getValueAt(j, "addrname"))) {
        addrModel.setValueAt(null, e.getRow(), "addrname");
        showErrorMessage(
          MultiLangTrans.getTransStr("MC1", 
          new String[] { NCLangRes.getInstance()
          .getStrByID("10080804", "UPT10080804-000075") }));
      }
    }
  }

  private void afterEditDefaultAddress(BillEditEvent e)
  {
    BillModel addrModel = getBillCardPanel().getBillModel("ADDR");
    for (int i = 0; i < addrModel.getRowCount(); i++) {
      Boolean flag = (Boolean)addrModel.getValueAt(i, "defaddrflag");
      if ((flag != null) && (flag.booleanValue())) {
        addrModel.setValueAt(new Boolean(false), i, "defaddrflag");
        int old_rowState = addrModel.getRowState(i);
        if (old_rowState == 0)
          addrModel.setRowState(i, 2);
      }
    }
    addrModel.setValueAt(new Boolean(true), e.getRow(), "defaddrflag");
  }

  private void handleException(Throwable exception)
  {
    Logger.error(exception.getMessage(), exception);
    if (exception.getMessage() != null)
      showHintMessage(exception.getMessage());
  }

  protected ManageEventHandler createEventHandler()
  {
    return new MyEventHandler(this, getUIControl());
  }

  public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception
  {
  }

  protected void setHeadSpecialData(CircularlyAccessibleValueObject vo, int intRow) throws Exception
  {
  }

  protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception
  {
  }

  protected void initSelfData() {
    BillItem bank = getBillCardPanel().getBillModel("BANK").getItemByKey(
      "account");
    ((UIRefPane)bank.getComponent()).setAutoCheck(false);
  }

  public void setDefaultData()
    throws Exception
  {
  }
}