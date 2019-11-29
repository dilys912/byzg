package nc.ui.ia.analyze;

import java.util.Vector;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.report.ReportBaseClass;

public class IAAnalyzePrintDataSource
  implements IDataSource
{
  private ReportBaseClass m_report = null;
  private BillData m_bd = null;
  private String m_sModuleCode = "";

  public IAAnalyzePrintDataSource()
  {
  }

  public IAAnalyzePrintDataSource(BillData bd, ReportBaseClass panelBillCard, String sModuleCode)
    throws Exception
  {
    this.m_bd = bd;
    this.m_report = panelBillCard;
    this.m_sModuleCode = sModuleCode;
  }

  public String[] getAllDataItemExpress()
  {
    String[] sKey = null;
    Vector vTemp = new Vector(1, 1);

    for (int i = 0; i < this.m_bd.getHeadShowItems().length; i++)
    {
      vTemp.addElement(this.m_bd.getHeadShowItems()[i].getKey());
    }

    for (int i = 0; i < this.m_bd.getBodyShowItems().length; i++)
    {
      vTemp.addElement(this.m_bd.getBodyShowItems()[i].getKey());
    }

    for (int i = 0; i < this.m_bd.getTailShowItems().length; i++)
    {
      vTemp.addElement(this.m_bd.getTailShowItems()[i].getKey());
    }

    sKey = new String[vTemp.size()];
    vTemp.copyInto(sKey);

    return sKey;
  }

  public String[] getAllDataItemNames()
  {
    return null;
  }

  public String[] getDependentItemExpressByExpress(String itemName)
  {
    String[] sName = null;
    Vector vTemp = new Vector(1, 1);

    for (int i = 0; i < this.m_bd.getHeadShowItems().length; i++)
    {
      vTemp.addElement(this.m_bd.getHeadShowItems()[i].getName());
    }

    for (int i = 0; i < this.m_bd.getBodyShowItems().length; i++)
    {
      vTemp.addElement(this.m_bd.getBodyShowItems()[i].getName());
    }

    sName = new String[vTemp.size()];
    vTemp.copyInto(sName);

    return sName;
  }

  public String[] getItemValuesByExpress(String itemExpress)
  {
    String[] sReturnValue = null;
    Vector vData = new Vector(1, 1);

    BillItem bt = this.m_bd.getHeadItem(itemExpress);
    BillItem bt2 = this.m_bd.getTailItem(itemExpress);

    if ((bt == null) && (bt2 == null))
    {
      if (itemExpress.substring(0, 2).equals("bb"))
      {
        itemExpress = itemExpress.substring(2);
      }

      bt = this.m_bd.getBodyItem(itemExpress);

      if (bt != null)
      {
        int iRowCount = this.m_report.getBillModel().getRowCount();
        for (int i = 0; i < iRowCount; i++)
        {
          String sTemp = "";
          Object oTemp = this.m_report.getBillModel().getValueAt(i, itemExpress);
          if (oTemp != null)
          {
            sTemp = oTemp.toString();
          }

          vData.addElement(sTemp);
        }
      }
    }
    else
    {
      if (bt == null)
      {
        bt = this.m_bd.getTailItem(itemExpress);
      }

      if (bt != null)
      {
        int iDataType = bt.getDataType();
        String strValue = "";
        switch (iDataType)
        {
        case 0:
        case 1:
        case 2:
        case 3:
        case 7:
          if (!(bt.getComponent() instanceof UIRefPane)) break;
          strValue = ((UIRefPane)bt.getComponent()).getText(); break;
        case 5:
          if (!(bt.getComponent() instanceof UIRefPane)) break;
          strValue = ((UIRefPane)bt.getComponent()).getRefName(); break;
        case 6:
          if ((!(bt.getComponent() instanceof UIComboBox)) || 
            (((UIComboBox)bt.getComponent()).getSelectedItem() == null)) break;
          strValue = ((UIComboBox)bt.getComponent()).getSelectedItem().toString(); break;
        case 4:
          if (!(bt.getComponent() instanceof UICheckBox)) break;
          if (((UICheckBox)bt.getComponent()).isSelected())
            strValue = NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000244");
          else {
            strValue = NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000108");
          }

        }

        if (strValue == null)
        {
          strValue = "";
        }

        vData.addElement(strValue);
      }
    }

    if (vData.size() > 0)
    {
      sReturnValue = new String[vData.size()];
      vData.copyInto(sReturnValue);
    }

    return sReturnValue;
  }

  public String getModuleName()
  {
    return this.m_sModuleCode;
  }

  public boolean isNumber(String itemExpress)
  {
    boolean isNum = false;
    BillItem bt = this.m_bd.getHeadItem(itemExpress);
    int iDataType = -1;
    if (bt == null)
    {
      bt = this.m_bd.getBodyItem(itemExpress);
      if (bt == null)
      {
        bt = this.m_bd.getTailItem(itemExpress);
      }
    }

    if (bt != null)
    {
      iDataType = bt.getDataType();
      switch (iDataType)
      {
      case 1:
      case 2:
        isNum = true;
      }
    }

    return isNum;
  }
}