package nc.ui.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ButtonObject
{
  private String m_strName = null;

  private String m_strHint = null;

  private int m_nPower = 0;

  private ButtonObject m_boParent = null;

  private Vector m_vecChildren = null;

  private boolean m_bEnabled = true;

  private boolean m_bVisible = true;

  private boolean m_bPower = true;

  private Object m_data = null;

  private String m_internalTag = null;

  private int m_intModifiers = 0;

  private boolean m_isCheckboxGroup = false;

  private boolean m_isExclusiveMode = true;

  private boolean m_isPowerContrl = true;

  private boolean m_isSelected = false;

  private boolean m_isSeperator = false;

  private String m_strCode = null;

  private String m_strDisplayHotkey = null;

  private String m_strHotKey = null;

  private String m_strTag = null;

  private boolean bUnionFunc = false;
  private ArrayList alUnionFuncbtn;

  public ButtonObject(String strName)
  {
    this(strName, strName);
  }

  public ButtonObject(String name, ButtonObject[] children)
  {
    this(name);
    addChileButtons(children);
  }

  public ButtonObject(String name, String hint)
  {
    this(name, hint, 0);
  }

  public ButtonObject(String name, String hint, int power)
  {
    this.m_strName = name;
    this.m_strHint = hint;
    this.m_nPower = power;
    this.m_strCode = name;
    this.m_internalTag = (name + String.valueOf(Math.random()));
  }

  public ButtonObject(String name, String hint, String code)
  {
    this(name, hint, 0, code);
  }

  public ButtonObject(String name, String hint, int power, String code)
  {
    this.m_strName = name;
    this.m_strHint = hint;
    this.m_nPower = power;
    this.m_strCode = code;
    this.m_internalTag = (name + String.valueOf(Math.random()));
  }

  public void addChildButton(ButtonObject bo)
  {
    bo.setParent(this);
    getChildren().addElement(bo);
  }

  public ButtonObject[] getChildButtonGroup()
  {
    ButtonObject[] bos = new ButtonObject[getChildren().size()];
    return (ButtonObject[])(ButtonObject[])getChildren().toArray(bos);
  }

  public Vector getChildren()
  {
    if (this.m_vecChildren == null) {
      try {
        this.m_vecChildren = new Vector();
      } catch (Throwable e) {
        handleException(e);
      }
    }
    return this.m_vecChildren;
  }

  public String getHint()
  {
    return this.m_strHint;
  }

  public String getName()
  {
    return this.m_strName;
  }

  public ButtonObject getParent()
  {
    return this.m_boParent;
  }

  public int getPower()
  {
    return this.m_nPower;
  }

  private static void handleException(Throwable exception)
  {
    exception.printStackTrace(System.err);
  }

  public boolean isEnabled()
  {
    return this.m_bEnabled;
  }

  public boolean isVisible()
  {
    return this.m_bVisible;
  }

  public void removeAllChildren()
  {
    getChildren().removeAllElements();
  }

  public void removeChildButton(ButtonObject bo)
  {
    getChildren().remove(bo);
  }

  public void setChildButtonGroup(ButtonObject[] bos)
  {
    if (bos != null) {
      Vector children = getChildren();
      children.removeAllElements();
      for (int i = 0; i < bos.length; i++) {
        bos[i].setParent(this);
        children.addElement(bos[i]);
      }
    }
  }

  public void setEnabled(boolean b)
  {
    this.m_bEnabled = b;
  }

  public void setHint(String strHint)
  {
    this.m_strHint = strHint;
  }

  public void setName(String newvalue)
  {
    this.m_strName = newvalue;
  }

  public void setParent(ButtonObject newvalue)
  {
    this.m_boParent = newvalue;
  }

  void setPower(int nPower)
  {
    this.m_nPower = nPower;
  }

  public void setVisible(boolean visi)
  {
    this.m_bVisible = visi;
  }

  /** @deprecated */
  public void addChileButtons(ButtonObject[] aryBtns)
  {
    setChildButtonGroup(aryBtns);
  }

  public int getChildCount()
  {
    if (this.m_vecChildren == null) {
      return 0;
    }
    return getChildren().size();
  }

  public String getCode()
  {
    return this.m_strCode;
  }

  public Object getData()
  {
    return this.m_data;
  }

  public String getDisplayHotkey()
  {
    return this.m_strDisplayHotkey;
  }

  public String getHotKey()
  {
    return this.m_strHotKey;
  }

  public String getInternalTag()
  {
    return this.m_internalTag;
  }

  public int getModifiers()
  {
    return this.m_intModifiers;
  }

  public ButtonObject[] getSelectedChildButton()
  {
    ButtonObject[] bo = null;
    if ((isCheckboxGroup()) && (getChildCount() > 0)) {
      List list = new ArrayList();
      ButtonObject[] ary = getChildButtonGroup();
      for (int i = 0; i < ary.length; i++) {
        if (ary[i].isSelected()) {
          list.add(ary[i]);
        }
      }
      if (list.size() > 0) {
        bo = new ButtonObject[list.size()];
        list.toArray(bo);
      }
    }
    return bo;
  }

  public String getTag()
  {
    return this.m_strTag;
  }

  public boolean isCheckboxGroup()
  {
    return this.m_isCheckboxGroup;
  }

  public boolean isExclusiveMode()
  {
    return this.m_isExclusiveMode;
  }

  public boolean isPower()
  {
    return this.m_bPower;
  }

  public boolean isPowerContrl()
  {
    return this.m_isPowerContrl;
  }

  public boolean isSelected()
  {
    return this.m_isSelected;
  }

  public boolean isSeperator()
  {
    return this.m_isSeperator;
  }

  public void setCheckboxGroup(boolean isCheckBoxGroup)
  {
    this.m_isCheckboxGroup = isCheckBoxGroup;
  }

  public void setCode(String newCode)
  {
    this.m_strCode = newCode;
  }

  public void setData(Object newData)
  {
    this.m_data = newData;
  }

  public void setDisplayHotkey(String newDisplayHotkey)
  {
    this.m_strDisplayHotkey = newDisplayHotkey;
  }

  public void setExclusiveMode(boolean mode)
  {
    this.m_isExclusiveMode = mode;
  }

  public void setHotKey(String newHotKey)
  {
    this.m_strHotKey = newHotKey;
  }

  public void setModifiers(int newModifiers)
  {
    this.m_intModifiers = newModifiers;
  }

  public void setPower(boolean newPower)
  {
    this.m_bPower = newPower;
  }

  public void setPowerContrl(boolean isPowercontrl)
  {
    this.m_isPowerContrl = isPowercontrl;
  }

  public void setSelected(boolean isSelected)
  {
    this.m_isSelected = isSelected;
    if ((getParent() != null) && (getParent().isCheckboxGroup()) && (getParent().isExclusiveMode()) && (isSelected)) {
      ButtonObject[] bos = getParent().getChildButtonGroup();
      int count = bos == null ? 0 : bos.length;
      for (int i = 0; i < count; i++) {
        ButtonObject child = bos[i];
        if ((child.isSelected()) && (!equals(child)))
          child.setSelected(false);
      }
    }
  }

  public void setSeperator(boolean isSeperator)
  {
    this.m_isSeperator = isSeperator;
  }

  public void setTag(String tag)
  {
    this.m_strTag = tag;
  }

  public void setUnionFuncFlag(boolean bFlag)
  {
    this.bUnionFunc = bFlag;
  }

  public boolean isUnionFunc()
  {
    return this.bUnionFunc;
  }

  /** @deprecated */
  public void addUnionfuncbtn(ButtonObject btnobj)
  {
    addUnionFuncBtn(btnobj);
  }

  public void addUnionFuncBtn(ButtonObject btnobj)
  {
    if (this.alUnionFuncbtn == null) {
      this.alUnionFuncbtn = new ArrayList();
    }
    this.alUnionFuncbtn.add(btnobj);
  }

  /** @deprecated */
  public ArrayList getUnionfuncbtn()
  {
    return this.alUnionFuncbtn;
  }

  public ArrayList getUnionFuncBtns()
  {
    return this.alUnionFuncbtn;
  }

  public String toString()
  {
    return this.m_strName;
  }
}