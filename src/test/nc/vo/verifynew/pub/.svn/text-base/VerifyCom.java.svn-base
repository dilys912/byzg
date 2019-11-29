package nc.vo.verifynew.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import nc.bs.logging.Log;
import nc.impl.arap.proxy.Proxy;
import nc.itf.arap.prv.IArapVerifyLogPrivate;
import nc.vo.arap.verifynew.DisplayVO;
import nc.vo.arap.verifynew.MethodVO;
import nc.vo.pub.BusinessException;

public class VerifyCom
  implements Serializable
{
  private static final long serialVersionUID = 1692818055197199032L;
  private IDataFilter m_filter;
  private Hashtable<String, IVerifyMethod> m_verifymethods = new Hashtable();
  private IDataSave m_saver;
  private VerifyVO[] m_debitdata;
  private VerifyVO[] m_creditdata;
  private ArrayList<VerifyLogVO> m_logs = new ArrayList();
  private Hashtable<String, DefaultVerifyRuleVO> m_rule;
  private String[] m_verifySequence;
  private VerifyVO[] m_debitSelected;
  private VerifyVO[] m_creditSelected;
  private Hashtable<Object, Object> m_context = new Hashtable();

  private Hashtable<String, Checker> m_checkers = new Hashtable();

  public Hashtable<String, DefaultVerifyRuleVO> getM_rule()
  {
    return this.m_rule;
  }

  public void setM_rule(Hashtable<String, DefaultVerifyRuleVO> m_rule)
  {
    this.m_rule = m_rule;
  }

  public VerifyCom()
  {
  }

  public VerifyCom(IDataFilter filter, IDataSave saver, String path)
  {
    this.m_filter = filter;
    this.m_saver = saver;
    try
    {
      MethodVO[] methods = Proxy.getIArapVerifyLogPrivate().getAllMethods();
      for (int i = 0; i < methods.length; i++)
      {
        Checker temp2 = null;
        try
        {
          temp2 = methods[i].getCheck_class() != null ? (Checker)(Checker)Class.forName(methods[i].getCheck_class()).newInstance() : null;
        }
        catch (ClassNotFoundException e)
        {
        }

        this.m_verifymethods.put(methods[i].getFa(), methods[i].getImpl_class());
        if (temp2 != null)
          this.m_checkers.put(methods[i].getFa(), temp2);
      }
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
    Set key = this.m_verifymethods.keySet();
    Iterator it = key.iterator();
    while (it.hasNext())
    {
      String k = (String)it.next();
      IVerifyMethod rule = (IVerifyMethod)this.m_verifymethods.get(k);
      rule.setVerifycom(this);
    }
  }

  public void filter(ConditionVO condition)
    throws BusinessException
  {
    VerifyVO[] all = (VerifyVO[])(VerifyVO[])this.m_filter.onFilterData(condition);
    this.m_debitdata = this.m_filter.getDebitData(all);
    this.m_creditdata = this.m_filter.getCreditData(all);
  }

  public DisplayVO[] filterLog(ConditionVO condition, boolean isSum)
    throws BusinessException
  {
    DisplayVO[] all = (DisplayVO[])(DisplayVO[])this.m_filter.onFilterLog(condition, isSum);
    return all;
  }

  public Object[] filterDj(ConditionVO condition)
    throws BusinessException
  {
    Object[] all = (Object[])this.m_filter.onFilterDj(condition);
    return all;
  }

  public void clearLog()
  {
    this.m_logs.clear();
  }

  public void clearAll()
  {
    this.m_logs.clear();
    this.m_creditdata = null;
    this.m_debitdata = null;
    this.m_rule = null;
    this.m_debitSelected = null;
    this.m_creditSelected = null;
  }

  public void clearData()
  {
    this.m_logs.clear();
    this.m_creditdata = null;
    this.m_debitdata = null;
    this.m_debitSelected = null;
    this.m_creditSelected = null;
  }

  public void verify(VerifyVO[] debit, VerifyVO[] credit)
  {
    this.m_logs = new ArrayList();
    for (int i = 0; i < this.m_verifySequence.length; i++)
    {
      DefaultVerifyRuleVO rule = getRuleVO(this.m_verifySequence[i]);
      if (rule == null)
      {
        continue;
      }
      IVerifyMethod method = (IVerifyMethod)this.m_verifymethods.get(this.m_verifySequence[i]);
      ArrayList rs = method.onVerify(getM_debitSelected(), getM_creditSelected(), rule);
      if (rs != null)
        this.m_logs.addAll(rs);
    }
  }

  public String save()
    throws BusinessException
  {
    String result = null;
    if (this.m_logs.size() > 0) {
      VerifyLogVO[] logs = new VerifyLogVO[this.m_logs.size()];
      logs = (VerifyLogVO[])(VerifyLogVO[])this.m_logs.toArray(logs);
      result = this.m_saver.onSave(logs, null);
    }

    return result;
  }

  public String[] unSave(String[] hepch)
    throws BusinessException
  {
    String[] result = this.m_saver.unSave(hepch);
    clearAll();
    return result;
  }

  public ArrayList<VerifyLogVO> getM_logs()
  {
    return this.m_logs;
  }

  public void setM_logs(ArrayList<VerifyLogVO> m_logs)
  {
    this.m_logs = m_logs;
  }

  public IDataSave getM_saver()
  {
    return this.m_saver;
  }

  public void setM_saver(IDataSave m_saver)
  {
    this.m_saver = m_saver;
  }

  public Hashtable<String, IVerifyMethod> getM_verifymethods()
  {
    return this.m_verifymethods;
  }

  public void setM_verifymethods(Hashtable<String, IVerifyMethod> m_verifymethods)
  {
    this.m_verifymethods = m_verifymethods;
  }

  public VerifyVO[] getM_creditdata()
  {
    return this.m_creditdata;
  }

  public void setM_creditdata(VerifyVO[] m_creditdata)
  {
    this.m_creditdata = m_creditdata;
  }

  public VerifyVO[] getM_debitdata()
  {
    return this.m_debitdata;
  }

  public void setM_debitdata(VerifyVO[] m_debitdata)
  {
    this.m_debitdata = m_debitdata;
  }

  public IDataFilter getM_filter()
  {
    return this.m_filter;
  }

  public void setM_filter(IDataFilter m_filter)
  {
    this.m_filter = m_filter;
  }

  public void setM_rule(DefaultVerifyRuleVO[] m_rule)
  {
    if (this.m_rule == null)
    {
      this.m_rule = new Hashtable();
    }
    if (m_rule == null)
      return;
    this.m_rule.clear();
    for (int i = 0; i < m_rule.length; i++)
    {
      if (m_rule[i] == null)
        continue;
      this.m_rule.put(m_rule[i].getM_verifyName(), m_rule[i]);
    }
  }

  public DefaultVerifyRuleVO getRuleVO(String name)
  {
    DefaultVerifyRuleVO result = (DefaultVerifyRuleVO)this.m_rule.get(name);
    return result;
  }

  public VerifyVO[] getM_creditSelected()
  {
    return this.m_creditSelected;
  }

  public void setM_creditSelected(VerifyVO[] selected)
  {
    this.m_creditSelected = selected;
  }

  public VerifyVO[] getM_debitSelected()
  {
    return this.m_debitSelected;
  }

  public void setM_debitSelected(VerifyVO[] selected)
  {
    this.m_debitSelected = selected;
  }

  public String[] getM_verifySequence()
  {
    return this.m_verifySequence;
  }

  public void setM_verifySequence(String[] sequence)
  {
    this.m_verifySequence = sequence;
  }

  public void freeVerifyLog(String[] pk_logs)
  {
  }

  public Hashtable<Object, Object> getM_context()
  {
    return this.m_context;
  }

  public void setM_context(Hashtable<Object, Object> m_context)
  {
    this.m_context = m_context;
  }

  public Hashtable<String, Checker> getM_checkers()
  {
    return this.m_checkers;
  }

  public void setM_checkers(Hashtable<String, Checker> m_checkers)
  {
    this.m_checkers = m_checkers;
  }
}