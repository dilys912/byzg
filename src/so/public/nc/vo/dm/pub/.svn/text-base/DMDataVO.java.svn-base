package nc.vo.dm.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.IscmDefCheckVO;

public class DMDataVO extends CircularlyAccessibleValueObject
  implements IscmDefCheckVO
{
  private HashMap ht = new HashMap();

  private ArrayList al = null;

  public String sKeyofFreeVO = "keyoffreevo";

  private String sPrimaryKey = null;

  public Object clone()
  {
    try
    {
      DMDataVO dmdvo = (DMDataVO)getClass().newInstance();
      String[] sNames = getAttributeNames();
      if (null != sNames) {
        for (int i = 0; i < sNames.length; i++) {
          dmdvo.setAttributeValue(sNames[i], getAttributeValue(sNames[i]));
        }
      }

      dmdvo.setStatus(getStatus());
      return dmdvo; } catch (Exception e) {
    }
    return null;
  }

  public void combineOtherVO(DMDataVO otherVO)
  {
    if (otherVO != null) {
      String[] sOtherFieldNames = otherVO.getAttributeNames();
      if (sOtherFieldNames == null)
        return;
      for (int i = 0; i < sOtherFieldNames.length; i++)
        setAttributeValue(sOtherFieldNames[i], otherVO.getAttributeValue(sOtherFieldNames[i]));
    }
  }

  public void convertToArrayList()
  {
    String[] sNames = getAttributeNames();
    if ((sNames == null) || (sNames.length == 0))
      return;
    this.al = new ArrayList();
    ArrayList alKey = null;
    for (int i = 0; i < sNames.length; i++) {
      alKey = new ArrayList();
      alKey.add(sNames[i]);
      alKey.add(getAttributeValue(sNames[i]));
      this.al.add(alKey);
    }
    this.ht = null;
  }

  public void convertToHashTable()
  {
    this.ht = new HashMap();
    ArrayList alKey = null;
    String sName = null;
    for (int i = 0; i < this.al.size(); i++) {
      alKey = (ArrayList)this.al.get(i);
      sName = alKey.get(0).toString();
      setAttributeValue(sName, alKey.get(1));
    }
    this.al = null;
  }

  public void genValueRangeHashtableFromVO(CircularlyAccessibleValueObject vo, ValueRangeHashtable valueRangeHashtable)
  {
    ValueRange valueRange = new ValueRange();

    String[] sHeadKeyNames = vo.getAttributeNames();
    for (int i = 0; i < sHeadKeyNames.length; i++) {
      valueRange = new ValueRange();
      valueRange.setKey(sHeadKeyNames[i]);
      valueRange.setPrimaryKey(false);
      Object obj = vo.getAttributeValue(sHeadKeyNames[i]);
      if ((obj instanceof String))
        valueRange.setDataType(0);
      else if ((obj instanceof UFDouble))
        valueRange.setDataType(2);
      else if ((obj instanceof Integer))
        valueRange.setDataType(1);
      else if ((obj instanceof UFBoolean))
        valueRange.setDataType(3);
      else if ((obj instanceof BigDecimal))
        valueRange.setDataType(2);
      else {
        valueRange.setDataType(0);
      }
      valueRangeHashtable.putValueRange(valueRange);
    }
  }

  public Object[] getAllKeysFromHashtable(Hashtable hashtable)
  {
    Vector vValue = new Vector();
    Enumeration enTmp = hashtable.keys();

    while (enTmp.hasMoreElements()) {
      Object obj = enTmp.nextElement();
      vValue.add(obj);
    }

    if (vValue.size() == 0) {
      return new Object[0];
    }
    Object[] sKeys = new Object[vValue.size()];
    vValue.copyInto(sKeys);
    return sKeys;
  }

  public String[] getAllStrKeysFromHashtable(Hashtable htAllPKs)
  {
    Vector vAllPKs = new Vector();
    Enumeration enTmp = htAllPKs.keys();

    while (enTmp.hasMoreElements()) {
      Object obj = enTmp.nextElement();
      vAllPKs.add(obj.toString());
    }
    if (vAllPKs.size() == 0) {
      return new String[0];
    }
    String[] onlyPKs = new String[vAllPKs.size()];
    vAllPKs.copyInto(onlyPKs);
    return onlyPKs;
  }

  public String[] getAttributeNames()
  {
    if (null == this.ht)
      convertToHashTable();
    Vector vName = new Vector();
    Iterator enTmp = getHt().keySet().iterator();

    while (enTmp.hasNext()) {
      Object obj = enTmp.next().toString().trim();
      if ((this.ht.get(obj) instanceof DMFreeVO)) {
        String[] sNameOfSubVO = ((DMFreeVO)this.ht.get(obj)).getAttributeNames();

        if (null != sNameOfSubVO) {
          for (int i = 0; i < sNameOfSubVO.length; i++) {
            if (vName.indexOf(sNameOfSubVO[i]) == -1)
              vName.add(sNameOfSubVO[i]);
          }
        }
        continue;
      }if (vName.indexOf(obj) == -1) {
        vName.add(obj);
      }
    }

    if (vName.size() == 0) {
      return null;
    }
    String[] sNames = new String[vName.size()];
    vName.copyInto(sNames);
    return sNames;
  }

  public Object[] getAttributeValue(String[] attributeName)
  {
    Object[] objReturn = new Object[attributeName.length];
    for (int i = 0; i < attributeName.length; i++) {
      objReturn[i] = getAttributeValue(attributeName[i]);
    }
    return objReturn;
  }

  public Object getAttributeValue(String attributeName)
  {
    if (null == this.ht)
      convertToHashTable();
    if ((null != attributeName) && (getHt().containsKey(attributeName)))
      return getHt().get(attributeName);
    if ((null != attributeName) && (attributeName.startsWith("vfree"))) {
      if (null == getAttributeValue(this.sKeyofFreeVO)) {
        return null;
      }
      return ((DMFreeVO)getAttributeValue(this.sKeyofFreeVO)).getAttributeValue(attributeName);
    }

    return null;
  }

  public String getEntityName()
  {
    return null;
  }

  public DMFreeVO getFreeItemVO()
  {
    return (DMFreeVO)getAttributeValue(this.sKeyofFreeVO);
  }

  protected HashMap getHt()
  {
    return this.ht;
  }

  public Object[] getIndependenceValueFromKeys(String[] Keys)
  {
    Object[] objs = getAttributeValue(Keys);
    Hashtable htTemp = new Hashtable();
    for (int i = 0; i < objs.length; i++) {
      if (null != objs[i]) {
        htTemp.put(objs[i], objs[i]);
      }
    }
    return getAllKeysFromHashtable(htTemp);
  }

  public int getKeyNumber()
  {
    if (null == getHt()) {
      convertToHashTable();
    }
    return getHt().size();
  }

  public DMDataVO[] getMultiDataVOs(ArrayList alNewKeys, ArrayList alNewKeysGetValueType, ArrayList alOldKeysGetValueFromKeys, ArrayList alOldKeyBeforeAttach)
  {
    DMDataVO[] dmdvos = new DMDataVO[1];
    dmdvos[0] = new DMDataVO();
    Hashtable htForGen = new Hashtable();

    for (int i = 0; i < alOldKeysGetValueFromKeys.size(); i++) {
      if ((alOldKeysGetValueFromKeys.get(i) instanceof ArrayList)) {
        ArrayList altmp = (ArrayList)alOldKeysGetValueFromKeys.get(i);
        dmdvos = new DMDataVO[altmp.size()];
        for (int j = 0; j < dmdvos.length; j++) {
          dmdvos[j] = new DMDataVO();
        }
        break;
      }

    }

    for (int i = 0; i < alOldKeysGetValueFromKeys.size(); i++) {
      if ((alOldKeysGetValueFromKeys.get(i) instanceof String)) {
        String value = String.valueOf(getAttributeValue((String)alOldKeysGetValueFromKeys.get(i)));

        if (value != null)
          htForGen.put(alNewKeys.get(i), value);
      } else {
        ArrayList altmp = (ArrayList)alOldKeysGetValueFromKeys.get(i);
        for (int j = 0; j < altmp.size(); j++)
        {
          if (((Integer)alNewKeysGetValueType.get(i)).intValue() == 0) {
            dmdvos[j].setAttributeValue((String)alNewKeys.get(i), altmp.get(j));
          } else {
            if (((Integer)alNewKeysGetValueType.get(i)).intValue() != 1)
              continue;
            if (null == alOldKeyBeforeAttach) {
              dmdvos[j].setAttributeValue((String)alNewKeys.get(i), getAttributeValue((String)altmp.get(j)));
            }
            else
            {
              for (int k = 0; k < alOldKeyBeforeAttach.size(); k++) {
                dmdvos[j].setAttributeValue(alNewKeys.get(i + k).toString(), getAttributeValue(alOldKeyBeforeAttach.get(k).toString() + (String)altmp.get(j)));
              }

            }

          }

        }

      }

    }

    Object[] sKeys = getAllKeysFromHashtable(htForGen);
    for (int i = 0; i < dmdvos.length; i++) {
      for (int j = 0; j < sKeys.length; j++) {
        dmdvos[i].setAttributeValue((String)sKeys[j], htForGen.get(sKeys[j]));
      }
    }

    return dmdvos;
  }

  public DMDataVO getPackVOByValueRangeHashtable(ValueRangeHashtable packByVRH)
  {
    DMDataVO dmdvo = null;
    Class cMyClass = null;

    cMyClass = getClass();
    try {
      dmdvo = (DMDataVO)cMyClass.newInstance();
    } catch (Exception e) {
      dmdvo = new DMDataVO();
    }

    Enumeration enTmp = packByVRH.keys();

    while (enTmp.hasMoreElements()) {
      String sKey = enTmp.nextElement().toString();
      dmdvo.setAttributeValue(sKey, getAttributeValue(sKey));
    }

    return dmdvo;
  }

  public String getPrimaryKey()
  {
    return this.sPrimaryKey;
  }

  public String getStrPKs(String[] AreaPKs)
  {
    StringBuffer areapks = new StringBuffer();
    if ((AreaPKs != null) && (AreaPKs.length > 0) && (AreaPKs[0] != null) && (AreaPKs[0].length() > 0))
    {
      areapks.append("'");
      areapks.append(AreaPKs[0]);
      areapks.append("'");
      for (int i = 1; i < AreaPKs.length; i++) {
        if ((AreaPKs[i] != null) && (AreaPKs[i].trim().length() != 0)) {
          areapks.append(",'");
          areapks.append(AreaPKs[i]);
          areapks.append("'");
        }
      }
    }
    return areapks.toString();
  }

  public StringBuffer getStrPKs(String sFieldName, String[] AreaPKs)
  {
    StringBuffer areapks = new StringBuffer();
    if ((sFieldName != null) && (sFieldName.trim().length() != 0) && (AreaPKs != null) && (AreaPKs.length > 0) && (AreaPKs[0] != null) && (AreaPKs[0].length() > 0))
    {
      int iModNum = 200;
      sFieldName = sFieldName.trim();
      areapks.append("((0=1");
      int j = 0;
      for (int i = 0; i < AreaPKs.length; i++) {
        if ((AreaPKs[i] != null) && (AreaPKs[i].trim().length() != 0)) {
          if ((i - j) % iModNum == 0) {
            areapks.append(") or ");
            areapks.append(sFieldName).append(" in (");
            areapks.append("'").append(AreaPKs[i]).append("'");
          }
          else {
            areapks.append(",'");
            areapks.append(AreaPKs[i]);
            areapks.append("'");
          }
        } else j++;
      }

      areapks.append("))");
    }
    return areapks;
  }

  public void removeAttributeName(String name)
  {
    if (null == this.ht)
      convertToHashTable();
    this.ht.remove(name);
  }

  public void setAttributeValue(String name, Object value)
  {
    if (null == this.ht)
      convertToHashTable();
    if ((null != name) && (name.trim().length() != 0))
      if (value != null) {
        if ((value instanceof String)) {
          value = value.toString().trim();
        }
        if (name.startsWith("vfree")) {
          if (null == getAttributeValue(this.sKeyofFreeVO)) {
            setAttributeValue(this.sKeyofFreeVO, new DMFreeVO());
          }
          if (value.toString().trim().length() == 0)
            value = null;
          ((CircularlyAccessibleValueObject)getAttributeValue(this.sKeyofFreeVO)).setAttributeValue(name, value);
        }
        else {
          getHt().put(name, value);
        }
      }
      else if (name.startsWith("vfree0")) {
        if (null == getAttributeValue(this.sKeyofFreeVO)) {
          setAttributeValue(this.sKeyofFreeVO, new DMFreeVO());
          ((DMDataVO)getAttributeValue(this.sKeyofFreeVO)).removeAttributeName(name);
        }
      }
      else {
        getHt().put(name, null);
      }
  }

  public void setDrTo(int drStatus)
  {
    setAttributeValue("dr", new Integer(drStatus));
  }

  public void setFreeItemVO(DMFreeVO newM_freevo)
  {
    setAttributeValue(this.sKeyofFreeVO, newM_freevo);
  }

  public void setPrimaryKey(String key)
  {
    this.sPrimaryKey = key;
  }

  public DMDataVO translateFromOtherVO(CircularlyAccessibleValueObject otherVO)
  {
    DMDataVO ddvo = new DMDataVO();
    String[] sNames = otherVO.getAttributeNames();
    if (null != sNames) {
      for (int i = 0; i < sNames.length; i++) {
        ddvo.setAttributeValue(sNames[i], otherVO.getAttributeValue(sNames[i]));
      }
    }

    return ddvo;
  }

  public CircularlyAccessibleValueObject translateToOtherVO(String sVOClassName)
    throws Exception
  {
    try
    {
      if ((null == sVOClassName) || (sVOClassName.trim().length() == 0))
        return null;
      Class cMyClass = Class.forName(sVOClassName);
      CircularlyAccessibleValueObject vo = (CircularlyAccessibleValueObject)cMyClass.newInstance();

      String[] sNames = getAttributeNames();
      if (null != sNames) {
        for (int i = 0; i < sNames.length; i++) {
          vo.setAttributeValue(sNames[i], getAttributeValue(sNames[i]));
        }
      }

      return vo; 
      } catch (Exception ee) {
    	  throw ee;
    }
    
  }

  public void translateToOtherVO(CircularlyAccessibleValueObject vo)
  {
    if (null == vo)
      return;
    String[] sNames = getAttributeNames();
    if (null != sNames)
      for (int i = 0; i < sNames.length; i++) {
        Object o = getAttributeValue(sNames[i]);
        if ((o instanceof CircularlyAccessibleValueObject)) {
          CircularlyAccessibleValueObject newvo = (CircularlyAccessibleValueObject)o;
          for (int j = 0; j < newvo.getAttributeNames().length; j++) {
            if (null == newvo.getAttributeValue(newvo.getAttributeNames()[j]))
              continue;
            vo.setAttributeValue(newvo.getAttributeNames()[j], newvo.getAttributeValue(newvo.getAttributeNames()[j]));
          }
        }
        else
        {
          vo.setAttributeValue(sNames[i], getAttributeValue(sNames[i]));
        }
      }
  }

  public void validate()
    throws ValidationException
  {
  }

  public Object[] getBodyDefValues(int iserial)
  {
    String[] defValue = new String[1];
    if (getCbilltypedef().equalsIgnoreCase("7D"))
      defValue[0] = ((String)getAttributeValue("vuserdef_b_" + (iserial - 1)));
    else {
      defValue[0] = ((String)getAttributeValue("vuserdef_b_" + iserial));
    }
    return defValue;
  }

  public String getCbilltypedef()
  {
    String vbilltype = (String)getAttributeValue("vbilltype");
    return vbilltype;
  }

  public Object getHeadDefValue(int iserial)
  {
    if (getCbilltypedef().equalsIgnoreCase("7D")) {
      return getAttributeValue("vuserdef" + (iserial - 1));
    }
    return getAttributeValue("vuserdef" + iserial);
  }

  public String getPk_corp()
  {
    String vbilltype = getCbilltypedef();
    String pk_corp = null;
    if (vbilltype.equalsIgnoreCase("7D")) {
      pk_corp = (String)getAttributeValue("pkarrivecorp");
    }
    if (vbilltype.equalsIgnoreCase("7F")) {
      pk_corp = (String)getAttributeValue("pkcorpforgenoid");
    }
    if (vbilltype.equalsIgnoreCase("7V")) {
      pk_corp = (String)getAttributeValue("pkcorpforgenoid");
    }
    else {
      pk_corp = (String)getAttributeValue("pksalecorp");
    }
    return pk_corp;
  }
}