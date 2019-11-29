package nc.ui.arap.pubdj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.FIBException;
import nc.ui.arap.global.TempletController;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.arap.templetcache.DjlxTempletCacheNew;
import nc.ui.bd.def.CardDefShowUtil;
import nc.ui.ep.dj.ARAPDjDataBuffer;
import nc.ui.ep.dj.ARAPDjSettingParam;
import nc.ui.ep.dj.DjPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.trade.bill.DefaultDefShowStrategyByBillItem;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.bd.CorpVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.ep.dj.DJZBVOUtility;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

public class ArapDjBillCardPanel extends BillCardPanel
{
  private static final long serialVersionUID = -4520750681786416700L;
  private static String m_strDjdl = null;

  private static Integer m_intOldSysCode = null;

  private ARAPDjSettingParam m_settingParam = null;
  private ArapDjPanel parent = null;

  public boolean isloaded = false;
  private DjCardPanelTreater m_cardTreater;

  public ArapDjBillCardPanel(ARAPDjSettingParam setting)
  {
    this.m_settingParam = setting;
  }

  public ArapDjBillCardPanel(ArapDjPanel parent)
  {
    this.parent = parent;
    this.m_settingParam = this.parent.getDjSettingParam();
    initPop();
    setTatolRowShow(true);
    BillRendererVO voCell = new BillRendererVO();
    voCell.setShowThMark(true);
    voCell.setShowZeroLikeNull(true);
    setBodyShowFlags(voCell);
    if ((this.parent.m_Parent instanceof DjPanel)) {
      addBodyEditListener2(TempletController.getInstance().getBillEditListener(((DjPanel)this.parent.m_Parent).getNodeCode(), 0, (DjPanel)this.parent.m_Parent));

      addEditListener(TempletController.getInstance().getBillEditListener(((DjPanel)this.parent.m_Parent).getNodeCode(), 0, (DjPanel)this.parent.m_Parent));
    }
    else if ((this.parent.m_Parent instanceof PubDjPanel)) {
      addBodyEditListener2(TempletController.getInstance().getBillEditListener2(((PubDjPanel)this.parent.m_Parent).getNodeCode(), (PubDjPanel)this.parent.m_Parent));

      addEditListener(TempletController.getInstance().getBillEditListener2(((PubDjPanel)this.parent.m_Parent).getNodeCode(), (PubDjPanel)this.parent.m_Parent));
    }

    addBodyMouseListener(new BillTableMouseListener()
    {
      public void mouse_doubleclick(BillMouseEnent e)
      {
        try {
          ArapDjBillCardPanel.this.parent.fireSelectedBodyRow(ArapDjBillCardPanel.this.parent);
        }
        catch (Exception ee)
        {
        }
      }
    });
    this.parent.getUITabbedPane1().addChangeListener(this.parent.ivjEventHandler);
  }

  public ARAPDjDataBuffer getDjDataBuffer()
  {
    if (this.parent == null) {
      Log.getInstance(getClass()).warn("WARNING:data buffer is null");
      return null;
    }
    return this.parent.getDjDataBuffer();
  }

  public void loadTempletArapDj(Integer iSyscode, String strCorpPK, String strDjdl, String strDjlxbm)
  {
    m_strDjdl = strDjdl;
    if (iSyscode == null)
    {
      if ("yt".equals(strDjdl))
        m_intOldSysCode = new Integer(13);
      else
        m_intOldSysCode = new Integer(0);
    }
    else
      m_intOldSysCode = iSyscode;
    if (this.parent.cache != null)
    {
      try
      {
        DjLXVO djlxvo = (DjLXVO)MyClientEnvironment.getValue(strCorpPK, "DJLX", strDjlxbm);
        String pk_djlx = djlxvo.getPrimaryKey();

        Log.getInstance(getClass()).warn(strCorpPK);
        Log.getInstance(getClass()).warn(strDjlxbm);
        Log.getInstance(getClass()).warn(pk_djlx);

        loadTempletLocal(strCorpPK, strDjdl, strDjlxbm);
        this.parent.ivjBillCardPanelDj.setBillValueVO(new DJZBVO());

        this.parent.ivjBillCardPanelDj.resetTreater(m_strDjdl);
        this.parent.ivjBillCardPanelDj.getM_cardTreater().initTempletBody(strCorpPK, strDjdl, strDjlxbm);
        this.parent.ivjBillCardPanelDj.setHeadTailShowThMark(getHeadItems(), true);
        this.parent.ivjBillCardPanelDj.setShowThMark(true);
        this.parent.ivjBillCardPanelDj.setTatolRowShow(true);
        this.parent.ivjBillCardPanelDj.setAutoExecHeadEditFormula(true);
        if (this.parent.ivjBillCardPanelDj.getBodyItem("payflag") != null) {
          this.parent.ivjBillCardPanelDj.getBodyItem("payflag").setEnabled(false);
        }

        this.parent.getUITabbedPane1().remove(0);
        this.parent.setTabChanged(false);
        this.parent.getUITabbedPane1().insertTab(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000021"), null, this.parent.ivjBillCardPanelDj, null, 0);

        this.parent.getUITabbedPane1().setSelectedIndex(0);
        this.parent.setTabChanged(true);
        this.parent.ivjBillCardPanelDj.isloaded = true;
      }
      catch (Exception e)
      {
        Log.getInstance(getClass()).error(e.getMessage(), e);
      }

    }
    else
    {
      try
      {
        super.loadTemplet(((INodeCode)this.parent.m_Parent).getNodeCode(), null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp(), strDjlxbm);

        resetTreater(m_strDjdl);
        getM_cardTreater().initTempletBody(strCorpPK, strDjdl, strDjlxbm);

        setHeadTailShowThMark(getHeadItems(), true);
        setShowThMark(true);
        setTatolRowShow(true);

        String[] strItems = { ArapConstant.DEFBODY };
        String[] strHeads = { ArapConstant.DEFHEAD };
        String[] strPrefix = { "zyx" };

        new CardDefShowUtil(this.parent.ivjBillCardPanelDj, new DefaultDefShowStrategyByBillItem()).showDefWhenRef(strItems, strPrefix, false);
        new CardDefShowUtil(this.parent.ivjBillCardPanelDj, new DefaultDefShowStrategyByBillItem()).showDefWhenRef(strHeads, strPrefix, true);
      }
      catch (Exception e)
      {
        Log.getInstance(getClass()).error(e.getMessage(), e);
      }
    }
  }

  public BillCardPanel loadTempletLocal(String strCorpPK, String strDjdl, String strDjlxbm)
  {
    this.parent.ivjBillCardPanelDj = ((ArapDjBillCardPanel)this.parent.cache.getTemplet(getDjSettingParam().getPk_user(), strCorpPK, ((INodeCode)this.parent.m_Parent).getNodeCode(), strDjlxbm));
    return this.parent.ivjBillCardPanelDj;
  }

  public ARAPDjSettingParam getDjSettingParam()
  {
    if (this.m_settingParam == null)
      Log.getInstance(getClass()).warn("setting is null");
    else
      Log.getInstance(getClass()).warn("setting is not null");
    return this.m_settingParam;
  }

  public void setBillValueVO(AggregatedValueObject billVO)
  {
    super.setBillValueVO(billVO);
  }

  public boolean delLine()
  {
    int row = getBillTable().getSelectedRow();
    Object djxtflag = getBodyValueAt(row, "djxtflagname");
    if ((null != djxtflag) && (djxtflag.toString().length() > 0) && 
      (DJZBVOConsts.XTConfirmed.equals(DJZBVOUtility.getXtFlag(djxtflag)))) {
      this.parent.m_Parent.showErrorMessage(NCLangRes.getInstance().getStrByID("2006", "UPT2006-v51-000012"));

      return false;
    }

    boolean b = super.delLine();
    Logger.debug("delete line");
    try {
      this.m_cardTreater.changeBodyCurrency(new BillEditEvent(getBodyItem("bbhl"), getBodyValueAt(0, "bbhl"), "bbhl", 0, 1));
      try
      {
        this.m_cardTreater.setHeadJe();
      } catch (Exception e) {
        Log.getInstance(getClass()).warn("setHeadJe 出错: " + e);
      }
    }
    catch (Throwable ee)
    {
    }

    return b;
  }

  public void addLine()
  {
    stopEditing();
    this.parent.m_DjzbitemsIndex += 1;

    ARAPDjDataBuffer dataBuffer = this.parent.getDjDataBuffer();

    String strDjdl = dataBuffer.getCurrentDjdl();

    DJZBVO mDjzbvo = dataBuffer.getCurrentDJZBVO();
    DJZBItemVO[] olddjzbitems = mDjzbvo == null ? null : (DJZBItemVO[])(DJZBItemVO[])mDjzbvo.getChildrenVO();

    if ((getBillModel().getRowCount() < 1) && ((olddjzbitems == null) || (olddjzbitems.length < 1)))
    {
      this.parent.m_DjzbitemsIndex = 0;
    }
    super.addLine();
    DJZBItemVO djzbitemtemp = (DJZBItemVO)getBillModel().getBodyValueRowVO(getBillModel().getRowCount() - 1, "nc.vo.ep.dj.DJZBItemVO");

    DJZBItemVO djzbitem = new DJZBItemVO();
    String[] sAttriname = djzbitemtemp.getAttributeNames();
    for (int i = 0; i < sAttriname.length; i++) {
      if (djzbitemtemp.getAttributeValue(sAttriname[i]) != null) {
        djzbitem.setAttributeValue(sAttriname[i], djzbitemtemp.getAttributeValue(sAttriname[i]));
      }

    }

    djzbitem.setStatus(2);

    djzbitem.setDjzbitemsIndex(new Integer(this.parent.m_DjzbitemsIndex));

    int rowCount = this.parent.m_DjzbitemsIndex + 1;
    rowCount = getBillModel().getRowCount();

    setBodyValueAt(new Integer(this.parent.m_DjzbitemsIndex), rowCount - 1, "djzbitemsIndex");

    getBillModel().setBodyRowVO(djzbitem, rowCount - 1);

    if ((strDjdl != null) && (strDjdl.equals("ys")))
      setBodyValueAt(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000768"), rowCount - 1, "kslb");
    try {
      this.m_cardTreater.setItemValueHtoB(rowCount - 1);
    } catch (Exception e2) {
      Log.getInstance(getClass()).warn(e2);
    }
    try
    {
      this.m_cardTreater.setItemValueBtoB(rowCount - 2, rowCount - 1);
    } catch (Exception e2) {
      Log.getInstance(getClass()).warn(e2);
    }
    getBodyValueAt(0, "jfybje");
    adjustCurrencyDecimalDigit();
  }

  public boolean insertLine()
  {
    stopEditing();
    boolean success = true;

    int oldrowCount = getBillModel().getRowCount();
    success = super.insertLine();
    int newrowCount = getBillModel().getRowCount();
    if (newrowCount - oldrowCount < 1)
    {
      return success;
    }
    this.parent.m_DjzbitemsIndex += 1;
    ARAPDjDataBuffer dataBuffer = this.parent.getDjDataBuffer();
    DJZBVO mDjzbvo = dataBuffer.getCurrentDJZBVO();
    DJZBItemVO[] olddjzbitems = mDjzbvo != null ? (DJZBItemVO[])(DJZBItemVO[])mDjzbvo.getChildrenVO() : null;

    if ((getBillModel().getRowCount() < 1) && ((olddjzbitems == null) || (olddjzbitems.length < 1)))
    {
      this.parent.m_DjzbitemsIndex = 0;
    }int row = getBodyPanel().getTable().getSelectedRow();
    DJZBItemVO djzbitemtemp = (DJZBItemVO)getBillModel().getBodyValueRowVO(row, "nc.vo.ep.dj.DJZBItemVO");

    DJZBItemVO djzbitem = new DJZBItemVO();
    String[] sAttriname = djzbitemtemp.getAttributeNames();
    for (int i = 0; i < sAttriname.length; i++) {
      if (djzbitemtemp.getAttributeValue(sAttriname[i]) != null) {
        djzbitem.setAttributeValue(sAttriname[i], djzbitemtemp.getAttributeValue(sAttriname[i]));
      }

    }

    djzbitem.setStatus(2);

    djzbitem.setDjzbitemsIndex(new Integer(this.parent.m_DjzbitemsIndex));

    row = getBodyPanel().getTable().getSelectedRow();
    Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000655") + row + " Index=" + this.parent.m_DjzbitemsIndex);

    setBodyValueAt(new Integer(this.parent.m_DjzbitemsIndex), row, "djzbitemsIndex");

    getBillModel().setBodyRowVO(djzbitem, row);

    String strDjdl = dataBuffer.getCurrentDjdl();

    if ((strDjdl != null) && (strDjdl.equals("ys")))
      setBodyValueAt(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000768"), row, "kslb");
    try
    {
      this.m_cardTreater.setItemValueHtoB(row);
    } catch (Exception e) {
      Log.getInstance(getClass()).warn(e);
    }
    try
    {
      this.m_cardTreater.setItemValueBtoB(row, row - 1);
    } catch (Exception e2) {
      Log.getInstance(getClass()).warn(e2);
    }

    return success;
  }

  public void pasteLine()
  {
    int begin = getBillTable().getSelectedRow();
    int oldrowCount = getBillModel().getRowCount();
    super.pasteLine();
    int newrowCount = getBillModel().getRowCount();

    Logger.debug("oldrowCount= " + oldrowCount + " newrowCount= " + newrowCount);

    if (newrowCount - oldrowCount < 1) {
      return;
    }
    int k = begin; for (int i = 0; k < newrowCount; k++)
    {
      setBodyValueAt(new Integer(k), k, "djzbitemsIndex");

      if (i < newrowCount - oldrowCount) {
        this.parent.m_DjzbitemsIndex += 1;
        setBodyValueAt(null, k, "fb_oid");
        i++;
      }

    }

    try
    {
      this.m_cardTreater.setHeadJe();
    } catch (Exception e) {
      Log.getInstance(getClass()).warn("setHeadJe 出错: " + e);
    }
  }

  public void refresh()
  {
  }

  public void setHeadItem(String strKey, Object Value)
  {
    if (strKey.equalsIgnoreCase("zzzt"))
      setHeadItemZzzt(strKey, Value);
    else
      super.setHeadItem(strKey, Value); 
  }

  public void setHeadItemZzzt(String strKey, Object Value) {
    if (strKey.equalsIgnoreCase("zzzt")) {
      super.setHeadItem("zzzt", Value);
      String strZzzt_MC;
     
      if ((Value == null) || (Value.toString().trim().length() == 0))
        strZzzt_MC = DJZBHeaderVO.getZzztMc(new Integer(0));
      else {
        strZzzt_MC = DJZBHeaderVO.getZzztMc(new Integer(Value.toString()));
      }
      super.setHeadItem("zzzt_mc", strZzzt_MC);
    }
  }

  public void adjustCurrencyDecimalDigit() {
    int intMaxDigit = -1;
    for (int i = 0; i < getRowCount(); i++) {
      String pkCurrency = (String)getBodyValueAt(i, "bzbm");
      if ((pkCurrency == null) || (pkCurrency.trim().length() == 0))
      {
        continue;
      }

      int digitTemp = 2;
      try {
        digitTemp = Currency.getCurrDigit(pkCurrency);
      }
      catch (FIBException e) {
        Logger.debug(e);
      }
      if (intMaxDigit == -1)
        intMaxDigit = digitTemp;
      if (intMaxDigit < digitTemp)
        intMaxDigit = digitTemp;
    }
    BillItem billItem = getBodyItem("ybye");
    if (billItem != null) {
      billItem.setDecimalDigits(intMaxDigit);
    }
    billItem = getBodyItem("jfybje");
    if (billItem != null) {
      billItem.setDecimalDigits(intMaxDigit);
    }
    billItem = getBodyItem("jfybwsje");
    if (billItem != null) {
      billItem.setDecimalDigits(intMaxDigit);
    }
    billItem = getBodyItem("jfybje");
    if (billItem != null) {
      billItem.setDecimalDigits(intMaxDigit);
    }

    billItem = getBodyItem("dfybje");
    if (billItem != null) {
      billItem.setDecimalDigits(intMaxDigit);
    }
    billItem = getBodyItem("dfybwsje");
    if (billItem != null) {
      billItem.setDecimalDigits(intMaxDigit);
    }
    billItem = getBodyItem("dfybje");
    if (billItem != null)
      billItem.setDecimalDigits(intMaxDigit);
  }

  public void adjustHeadDecimalDigit()
  {
    int intMaxBenBiJinEDigit = -1;
    for (int i = 0; i < getRowCount(); i++) {
      String pkCurrency = (String)getBodyValueAt(i, "bzbm");
      if ((pkCurrency == null) || (pkCurrency.trim().length() == 0))
      {
        continue;
      }

      int digitTemp = 2;
      try {
        digitTemp = Currency.getCurrDigit(pkCurrency);
      }
      catch (FIBException e) {
        Logger.debug(e);
      }
      if (intMaxBenBiJinEDigit == -1)
        intMaxBenBiJinEDigit = digitTemp;
      if (intMaxBenBiJinEDigit < digitTemp)
        intMaxBenBiJinEDigit = digitTemp;
    }
    if (intMaxBenBiJinEDigit == -1)
      return;
    BillItem billItem = getHeadItem("ybje");
    if (billItem != null)
      billItem.setDecimalDigits(intMaxBenBiJinEDigit);
  }

  public Integer getTempletSysCode()
  {
    if (m_intOldSysCode == null)
      return new Integer(0);
    return m_intOldSysCode;
  }

  public DjCardPanelTreater getM_cardTreater()
  {
    return this.m_cardTreater;
  }

  public void setM_cardTreater(DjCardPanelTreater treater)
  {
    this.m_cardTreater = treater;
  }

  private void initPop() {
    getBodyPanel().getPmBody().remove(getBodyPanel().getMiAddLine());

    getBodyPanel().getPmBody().remove(getBodyPanel().getMiInsertLine());

    getBodyPanel().getPmBody().remove(getBodyPanel().getMiPasteLine());

    getBodyPanel().getPmBody().remove(getBodyPanel().getMiPasteLineToTail());

    getBodyPanel().getPmBody().remove(getBodyPanel().getMiDelLine());

    UIMenuItem addItem = new UIMenuItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000012"));

    getBodyPanel().setMiAddLine(addItem);
    addItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        ArapDjBillCardPanel.this.addLine();
      }
    });
    UIMenuItem insertItem = new UIMenuItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000016"));

    getBodyPanel().setMiInsertLine(insertItem);
    insertItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ArapDjBillCardPanel.this.insertLine();
      }
    });
    UIMenuItem pasteItem = new UIMenuItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000015"));

    getBodyPanel().setMiPasteLine(pasteItem);
    pasteItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ArapDjBillCardPanel.this.pasteLine();
      }
    });
    UIMenuItem deleteItem = new UIMenuItem(NCLangRes.getInstance().getStrByID("common", "UC001-0000013"));

    getBodyPanel().setMiDelLine(deleteItem);
    deleteItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ArapDjBillCardPanel.this.delLine();
      }
    });
    UIMenuItem PasteLineToTail = new UIMenuItem(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000769"));

    PasteLineToTail.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        int oldrowCount = ArapDjBillCardPanel.this.getBillModel().getRowCount();

        ArapDjBillCardPanel.this.pasteLineToTail();
        int newrowCount = ArapDjBillCardPanel.this.getBillModel().getRowCount();

        Logger.debug("oldrowCount= " + oldrowCount + " newrowCount= " + newrowCount);

        if (newrowCount - oldrowCount < 1)
          return;
        DJZBItemVO[] djzbitems = null;

        for (int k = oldrowCount; k < newrowCount; k++)
        {
          ArapDjBillCardPanel.this.parent.m_DjzbitemsIndex += 1;
          Logger.debug("k= " + k + " index= " + ArapDjBillCardPanel.this.parent.m_DjzbitemsIndex);

          ArapDjBillCardPanel.this.setBodyValueAt(new Integer(ArapDjBillCardPanel.this.parent.m_DjzbitemsIndex), k, "djzbitemsIndex");

          ArapDjBillCardPanel.this.setBodyValueAt(null, k, "fb_oid");
        }

        djzbitems = (DJZBItemVO[])(DJZBItemVO[])ArapDjBillCardPanel.this.getBillModel().getBodyValueVOs("nc.vo.ep.dj.DJZBItemVO");

        ARAPDjDataBuffer dataBuffer = ArapDjBillCardPanel.this.parent.getDjDataBuffer();
        DJZBVO mDjzbvo = dataBuffer.getCurrentDJZBVO();

        DJZBItemVO[] olddjzbitems = (DJZBItemVO[])(DJZBItemVO[])mDjzbvo.getChildrenVO();

        DJZBItemVO[] newDjzbitems = null;
        int i = 1;
        i = olddjzbitems.length;
        newDjzbitems = new DJZBItemVO[i + newrowCount - oldrowCount];

        for (int j = 0; j < i; j++) {
          newDjzbitems[j] = olddjzbitems[j];
        }

        for (int k = oldrowCount; k < newrowCount; k++)
        {
          Logger.debug("index k=: " + djzbitems[k].getDjzbitemsIndex());

          newDjzbitems[i] = djzbitems[k];
          newDjzbitems[i].setFb_oid(null);
          newDjzbitems[i].setStatus(2);

          i++;
        }

        mDjzbvo.setChildrenVO(newDjzbitems);
        try
        {
          ArapDjBillCardPanel.this.m_cardTreater.setHeadJe();
        } catch (Exception ee) {
          Log.getInstance(getClass()).warn("setHeadJe 出错: " + ee);
        }
      }
    });
    getBodyPanel().getPmBody().add(getBodyPanel().getMiAddLine());

    getBodyPanel().getPmBody().add(getBodyPanel().getMiInsertLine());

    getBodyPanel().getPmBody().add(getBodyPanel().getMiPasteLine());

    getBodyPanel().getPmBody().add(PasteLineToTail);

    getBodyPanel().getPmBody().add(getBodyPanel().getMiDelLine());

    setBodyMenuShow(true);
  }

  public ArapDjPanel getM_Parent()
  {
    return this.parent;
  }

  public void resetTreater(String strDjdl)
  {
    if (("ys".equals(strDjdl)) || ("fk".equals(strDjdl)) || ("fj".equals(strDjdl)) || ("wf".equals(strDjdl)) || ("ss".equals(strDjdl)) || ("yt".equalsIgnoreCase(strDjdl)))
    {
      if (!(this.m_cardTreater instanceof DjCardPanelTreaterJf))
        this.m_cardTreater = new DjCardPanelTreaterJf(this);
    }
    else if ("hj".equals(strDjdl)) {
      if (!(this.m_cardTreater instanceof DjCardPanelTreaterHj)) {
        this.m_cardTreater = new DjCardPanelTreaterHj(this);
      }
    }
    else if (!(this.m_cardTreater instanceof DjCardPanelTreaterDf))
      this.m_cardTreater = new DjCardPanelTreaterDf(this);
  }

  public void cardAfterEdit(BillEditEvent e) {
    ArapDjBillCardPanel billCardPanel = this;
    try
    {
      billCardPanel.getM_cardTreater().changeBodyTXLX(e);
    } catch (Exception ex) {
    }
    try {
      billCardPanel.getM_cardTreater().changePk_corp(e, null);
    }
    catch (Exception ee)
    {
    }
    try
    {
      billCardPanel.getM_cardTreater().execHeadFormulas_dddw(e);
    } catch (Exception e2) {
      Logger.error(e2.getMessage(), e2);
    }

    try
    {
      billCardPanel.getM_cardTreater().changeBHjksmc(e);
    } catch (Exception e2) {
      Logger.debug(e2.getMessage());
    }
    try
    {
      billCardPanel.getM_cardTreater().changeAccountid(e);
    }
    catch (Throwable kse)
    {
    }

    try
    {
      billCardPanel.getM_cardTreater().changeAccidName(e);
    }
    catch (Throwable kse)
    {
    }
    try
    {
      billCardPanel.getM_cardTreater().changeDdh(e);
    } catch (Throwable kse) {
      Logger.debug("changeDdh 出错:" + kse);
    }

    if ((e.getKey().equals("kskhyh")) && (e.getPos() == 0)) {
      billCardPanel.getM_cardTreater().execHeadFormulas_Account();
    }

    try
    {
      billCardPanel.getM_cardTreater().changeSkyhzh(e);
    }
    catch (Throwable kse)
    {
    }

    try
    {
      billCardPanel.getM_cardTreater().changeNotetype(e);
    } catch (Throwable kse) {
      Log.getInstance(getClass()).warn("changeNotetype 加载公式出错:" + kse);
    }

    ARAPDjDataBuffer dataBuffer = this.parent.getDjDataBuffer();

    String strDjdl = dataBuffer.getCurrentDjdl();
    try
    {
      billCardPanel.getM_cardTreater().changeBodyHtmc(e);
    } catch (Throwable kse) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000789") + kse);
    }

    try
    {
      if (strDjdl.equals("ss"))
        billCardPanel.getM_cardTreater().changeBzbm(e);
      billCardPanel.getM_cardTreater().changeBItemBzbm(e, true);
    } catch (Throwable kse) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000781") + kse);
    }

    try
    {
      if (((e.getKey().equals("djrq")) || (e.getKey().equals("bzbm"))) && (e.getPos() == 0))
      {
        String pk_currtype = (String)billCardPanel.getHeadItem("bzbm").getValueObject();
        String date = billCardPanel.getHeadItem("djrq").getValue();

        billCardPanel.getM_cardTreater().changeBzbm_H(pk_currtype, date, true);
      }
    } catch (Exception ee) {
      Log.getInstance(getClass()).error(ee.getMessage(), ee);
    }

    try
    {
      if (strDjdl.equals("ss"))
        billCardPanel.getM_cardTreater().changeHeadFbhl(e, true);
    } catch (Throwable kse) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000781") + kse);
    }

    try
    {
      billCardPanel.getM_cardTreater().changeZy(e);
    } catch (Exception kse) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000782") + kse);
    }

    try
    {
      billCardPanel.getM_cardTreater().changeBodyCurrency(e);
    } catch (Throwable e0) {
      Logger.error(e0.getMessage(), e0);
    }

    try
    {
      if ((e == null) || (e.getKey() == null))
      {
        return;
      }

      if (e.getKey().equalsIgnoreCase("szxmmc"))
        this.parent.changeSzxm(e.getRow());
    }
    catch (Throwable ee) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000786") + ee);
    }

    try
    {
      this.parent.changeXmmc1(e);
    } catch (Throwable kse) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000787") + kse);
    }

    try
    {
      if (e.getPos() == 0)
        billCardPanel.getM_cardTreater().changeKsbm_cl(e);
    } catch (Throwable kse) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000788") + kse);
    }

    try
    {
      billCardPanel.getM_cardTreater().changeKsbm_cl2(e);
    } catch (Throwable kse) {
      Log.getInstance(getClass()).warn("changeKsbm_cl2 加载公式出错:" + kse);
    }
    try
    {
      billCardPanel.getM_cardTreater().changeBodyYwy(e);
    } catch (Throwable kse) {
      Log.getInstance(getClass()).warn("changeKsbm_cl2 加载公式出错:" + kse);
    }
    try
    {
      billCardPanel.getM_cardTreater().changeBodyKsmc(e);
    } catch (Throwable kse) {
      Log.getInstance(getClass()).warn("changeKsbm_cl2 加载公式出错:" + kse);
    }
    try
    {
      billCardPanel.getM_cardTreater().changeChbm_cl(e);
    } catch (Throwable kse) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000789") + kse);
    }

    try
    {
      billCardPanel.getM_cardTreater().changeIscorresp(e);
    } catch (Throwable kse) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000790") + kse);
    }
  }

  public void cardBodyRowChange(BillEditEvent e) {
    ArapDjBillCardPanel billCardPanel = this;
    try
    {
      billCardPanel.getBodyItem("bankacc").setEnabled(true); } catch (Exception ex) {
    }
    try {
      this.parent.m_SelectedRow = e.getRow();
      this.parent.fireSelectedBodyRow(this.parent);
    } catch (Exception ee) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000776") + ee);
    }

    try
    {
      this.parent.selectedSzxm(e.getRow());
    }
    catch (Throwable ee) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000791") + ee);
    }

    try
    {
      billCardPanel.getM_cardTreater().rCResetBody(e.getRow(), false, false);
    } catch (Exception ee) {
      Log.getInstance(getClass()).error(ee.getMessage(), ee);
    }
  }

  public boolean cardBeforeEdit(BillEditEvent be) {
    Object djxtflag = getBodyValueAt(be.getRow(), "djxtflagname");
    if ((null != djxtflag) && (djxtflag.toString().length() > 0) && 
      (DJZBVOConsts.XTConfirmed.equals(DJZBVOUtility.getXtFlag(djxtflag)))) {
      this.parent.m_Parent.showErrorMessage(NCLangRes.getInstance().getStrByID("2006", "UPT2006-v51-000012"));

      return false;
    }
    try
    {
      if ((getBodyItem("sl") != null) && (getBodyValueAt(be.getRow(), "sl") == null)) {
        setBodyValueAt(new UFDouble(0), be.getRow(), "sl");
      }
      ArapDjBillCardPanel billCardPanel = this;
      billCardPanel.getM_cardTreater().changeYhzhEidt(be);
      billCardPanel.getBodyItem("jfbbje");

      billCardPanel.getM_cardTreater().resetKsmc(be);
      billCardPanel.getM_cardTreater().resetYwymc(be);
      billCardPanel.getM_cardTreater().resetSFkyhzh(be);

      billCardPanel.getM_cardTreater().resetContractno(be);
      try {
        billCardPanel.getM_cardTreater().resetOrdercust(be);
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
      }

      if ((be.getKey().equals("jfbbje")) || (be.getKey().equals("dfbbje")) || (be.getKey().equals("jffbje")) || (be.getKey().equals("dffbje")) || (be.getKey().equals("bbhl")) || (be.getKey().equals("fbhl")))
      {
        try
        {
          billCardPanel.getM_cardTreater().rCResetBody(be.getRow(), false, false);
        } catch (Exception ee) {
          Log.getInstance(getClass()).error(ee.getMessage(), ee);
        }
      }
      try {
        if (be.getKey().equals("xmjdmc"))
        {
          Object value = billCardPanel.getBodyValueAt(be.getRow(), "xmbm2");

          if (value != null)
            billCardPanel.getM_cardTreater().resetJobid(be.getRow(), value.toString());
        }
      }
      catch (Throwable ee)
      {
        Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000775") + ee);
      }
      if (be.getKey().equals("htmc"))
        billCardPanel.getM_cardTreater().changeHtByBm(be, true);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }

    return true;
  }
}