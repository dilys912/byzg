package nc.ui.ia.ia501;

import java.util.Vector;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.print.IDataSource;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class QueryPrintDataSource
  implements IDataSource
{
  private CircularlyAccessibleValueObject[] m_vo = null;
  private BillData m_bd = null;
  private String m_sModuleCode = "";

  public QueryPrintDataSource()
  {
  }

  public QueryPrintDataSource(CircularlyAccessibleValueObject[] vo, BillData bd, String sModuleCode)
    throws Exception
  {
    this.m_vo = vo;
    this.m_bd = bd;
    this.m_sModuleCode = sModuleCode;
  }

  public String[] getAllDataItemExpress()
  {
    String[] sKey = null;
    Vector vTemp = new Vector();

    for (int i = 0; i < this.m_bd.getHeadShowItems().length; i++)
    {
      vTemp.addElement(this.m_bd.getHeadShowItems()[i].getKey());
    }
    if (this.m_vo.length > 0)
    {
      for (int i = 0; i < this.m_vo[0].getAttributeNames().length; i++)
      {
        vTemp.addElement(this.m_vo[0].getAttributeNames()[i]);
      }
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
    String[] sName = null;
    Vector vTemp = new Vector();

    for (int i = 0; i < this.m_bd.getHeadShowItems().length; i++)
    {
      vTemp.addElement(this.m_bd.getHeadShowItems()[i].getName());
    }
    if (this.m_vo.length > 0)
    {
      for (int i = 0; i < this.m_vo[0].getAttributeNames().length; i++)
      {
        vTemp.addElement(this.m_vo[0].getAttributeNames()[i]);
      }
    }
    for (int i = 0; i < this.m_bd.getTailShowItems().length; i++)
    {
      vTemp.addElement(this.m_bd.getTailShowItems()[i].getName());
    }

    sName = new String[vTemp.size()];
    vTemp.copyInto(sName);

    return sName;
  }

  public String[] getDependentItemExpressByExpress(String itemName)
  {
    return null;
  }

  public String[] getItemValuesByExpress(String itemExpress)
  {
    String[] sReturnValue = null;
    Vector vData = new Vector();

    BillItem bt = null;

    if ((this.m_vo != null) && (this.m_vo.length > 0))
    {
      String[] bodynames = this.m_vo[0].getAttributeNames();
      boolean bIsBodyField = false;
      for (int i = 0; i < bodynames.length; i++)
      {
        if (!bodynames[i].equalsIgnoreCase(itemExpress))
          continue;
        bIsBodyField = true;
        break;
      }

      if (!bIsBodyField)
      {
        bt = this.m_bd.getHeadItem(itemExpress);
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
            if (((UICheckBox)bt.getComponent()).isSelected()) {
              strValue = NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000244");
            }
            else
            {
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
      else
      {
        for (int i = 0; i < this.m_vo.length; i++)
        {
          String sTemp = "";
          Object oTemp = this.m_vo[i].getAttributeValue(itemExpress);
          if (oTemp != null)
          {
            sTemp = oTemp.toString();
          }
          else
          {
            sTemp = "";
          }
          vData.addElement(sTemp);
        }
      }

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
      bt = this.m_bd.getTailItem(itemExpress);
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
    else
    {
      Object bodyitem = this.m_vo[0].getAttributeValue(itemExpress);
      if (bodyitem != null)
      {
        if ((bodyitem.getClass().getName().equalsIgnoreCase("UFDouble")) || (bodyitem.getClass().getName().equalsIgnoreCase("Integer")))
        {
          isNum = true;
        }
        else
        {
          isNum = false;
        }
      }

    }

    return isNum;
  }
}