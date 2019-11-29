package nc.bs.trade.comsave;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.trade.business.HYSuperDMO;
import nc.bs.trade.combase.HYComBase;
import nc.vo.pub.SuperVO;

public class ComSave extends HYComBase
{
  protected final void saveItemHas(HYSuperDMO dmo, Hashtable insertDataHas, Hashtable updateDataHas)
    throws Exception
  {
    Enumeration en = null;

    if (updateDataHas != null)
    {
      en = updateDataHas.elements();
      while (en.hasMoreElements())
      {
        Vector v = (Vector)en.nextElement();
        if (v.size() > 0)
        {
          SuperVO[] vos = new SuperVO[v.size()];
          v.copyInto(vos);
          dmo.updateArray(vos);
        }
      }
    }

    if (insertDataHas != null)
    {
      en = insertDataHas.elements();
      while (en.hasMoreElements())
      {
        Vector v = (Vector)en.nextElement();
        if (v.size() > 0)
        {
          SuperVO[] vos = new SuperVO[v.size()];
          v.copyInto(vos);
          dmo.insertArray(vos);
        }
      }
    }
  }

  protected final void saveItems(HYSuperDMO dmo, SuperVO[] items, String mainField, String billPk)
    throws Exception
  {
    if (items != null)
    {
      Hashtable insertDataHas = new Hashtable();
      Hashtable updateDataHas = new Hashtable();

      for (int i = 0; i < items.length; i++)
      {
        String tableName = items[i].getTableName();
        switch (items[i].getStatus())
        {
        case 2:
          items[i].setAttributeValue("dr", new Integer(0));
          if (mainField != null) {
            if (items[i].getParentPKFieldName() != null)
              items[i].setAttributeValue(items[i].getParentPKFieldName(), billPk);
            else {
              items[i].setAttributeValue(mainField, billPk);
            }
          }

          if (!insertDataHas.containsKey(tableName))
            insertDataHas.put(tableName, new Vector());
          ((Vector)insertDataHas.get(tableName)).add(items[i]);

          break;
        case 1:
          items[i].setAttributeValue("dr", new Integer(0));
          if (!updateDataHas.containsKey(tableName))
            updateDataHas.put(tableName, new Vector());
          ((Vector)updateDataHas.get(tableName)).add(items[i]);

          break;
        case 3:
          items[i].setAttributeValue("dr", new Integer(1));

          if (!updateDataHas.containsKey(tableName))
            updateDataHas.put(tableName, new Vector());
          ((Vector)updateDataHas.get(tableName)).add(items[i]);
        }

      }

      saveItemHas(dmo, insertDataHas, updateDataHas);
    }
  }

  public final void updateItems(HYSuperDMO dmo, SuperVO[] items)
    throws Exception
  {
    if (items != null)
    {
      Hashtable updateDataHas = new Hashtable();

      for (int i = 0; i < items.length; i++)
      {
        String tableName = items[i].getTableName();
        if (!updateDataHas.containsKey(tableName))
          updateDataHas.put(tableName, new Vector());
        ((Vector)updateDataHas.get(tableName)).add(items[i]);
      }

      saveItemHas(dmo, null, updateDataHas);
    }
  }
}