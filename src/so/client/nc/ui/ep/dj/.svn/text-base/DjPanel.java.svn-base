package nc.ui.ep.dj;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.impl.arap.proxy.Proxy;
import nc.itf.arap.prv.IArapBillPrivate;
import nc.itf.arap.prv.IArapItemPrivate;
import nc.itf.arap.prv.IArapTBBillPrivate;
import nc.itf.arap.pub.IArapBillPublic;
import nc.itf.fi.pub.Currency;
import nc.ui.arap.global.PubData;
import nc.ui.arap.global.TempletController;
import nc.ui.arap.outer.ArapAssistantInf;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.arap.pubdj.ARAPDjUIFactory;
import nc.ui.arap.pubdj.ArapDjBillCardPanel;
import nc.ui.arap.pubdj.ArapDjPanel;
import nc.ui.arap.pubdj.DjStateChangeListener;
import nc.ui.arap.selectedpay.Cache;
import nc.ui.arap.templetcache.DjlxTempletCacheNew;
import nc.ui.bd.def.ListDefShowUtil;
import nc.ui.ep.dj.controller.ARAPDjCtlDjRefresh;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.DefaultDefShowStrategyByBillItem;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.global.BusiTransVO;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.bd.CorpVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOUtility;
import nc.vo.ep.dj.DjCondVO;
import nc.vo.ep.dj.ShenheException;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.msg.MessageVO;

public abstract class DjPanel extends DjBasePanel
  implements IUiPanel
{
  private int m_lastWorkPage = -1;
  private boolean isInitied = false;
  private UIPanel ivjUIPanel2 = null;
  int m_TabIndex = 1;
  protected BillListPanel ivjBillListPanel1 = null;

  private ArapDjPanel ivjArapDjPanel1 = null;

  private Cache voCache = new Cache();
  private DjCondVO cur_Djcondvo = null;
  public int[] m_BtnStates;
  DjStateChange m_djStateChange = new DjStateChange();

  private JPanel ivjJPanel1 = null;

  private JPanel ivjVerifyPanel = null;
  public BusiTransVO[] m_CurBusiTransVO;
  public int pzglh;
  protected boolean m_isPayPanel = false;

  protected int PanelProp = 0;
  private transient String tempString;

  public DjPanel()
  {
    MyClientEnvironment.refresh();
  }

  public DjPanel(int SysCode)
  {
    this.pzglh = SysCode;
    setM_Syscode(SysCode);
  }

  public void postInit()
  {
    initialize();
  }

  public void addListener(Object objListener, Object objUserdata)
  {
  }

  public Object invoke(Object objData, Object objUserData)
  {
    if ("FTSReceiverBACK".equals(objUserData)) {
      DJZBVO curDj = null;
      try {
        curDj = Proxy.getIArapBillPublic().findArapBillByPK((String)objData);
      } catch (Exception e1) {
        Logger.debug(e1.getMessage());
        showHintMessage(e1.getMessage());
      }
      getDjDataBuffer().refreshDJZBVO((String)objData, curDj);
      if ((getM_TabIndex() == -1) && (getDjDataBuffer().getCurrentDJZBVOPK().equals(objData))) {
        getArapDjPanel1().setDj(curDj);
      }
      else {
        BillModel model = getBillListPanel1().getHeadBillModel();
        for (int row = 0; row < model.getRowCount(); row++) {
          if (model.getValueAt(row, "vouchid").equals(objData))
            model.setBodyRowVO(curDj.getParentVO(), row);
        }
      }
      refreshBtnStats();
    }
    else
    {
      getDjSettingParam().setIsBackFromFTS(false);

      String strUserData = (String)objUserData;

      if ((strUserData == null) || (strUserData.trim().length() == 0))
        return null;
      if (getM_TabIndex() == 1) {
        return null;
      }

      DJZBVO djzbvo = getDjDataBuffer().getCurrentDJZBVO();
      DJZBHeaderVO headerVO = (DJZBHeaderVO)djzbvo.getParentVO();

      headerVO.setJszxzf(new Integer(10));
      headerVO.setZzzt(new Integer(2));

      getArapDjPanel1().getBillCardPanelDj().setHeadItem("jszxzf", new Integer(10));
      getArapDjPanel1().getBillCardPanelDj().setHeadItem("jszxzf_mc", DJZBVOUtility.getJszxzfString(new Integer(10)));
      getArapDjPanel1().getBillCardPanelDj().setHeadItem("zzzt", new Integer(2));
      ARAPDjCtlDjRefresh refresher = new ARAPDjCtlDjRefresh(this, getArapDjPanel1());
      refresher.refresh(strUserData);
    }
    return null;
  }

  public void nextClosed()
  {
  }

  public void removeListener(Object objListener, Object objUserdata)
  {
  }

  public void showMe(IParent parent)
  {
    parent.getUiManager().add(this, getName());
    this.m_parent = parent;
    setFrame(parent.getFrame());
  }

  private void addDjlb(DJZBVO djzbvo)
    throws Exception
  {
    DJZBHeaderVO newDjzbheadVo = (DJZBHeaderVO)djzbvo.getParentVO();

    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

    dataBuffer.putDJZBVO(newDjzbheadVo.getVouchid(), djzbvo);

    getBillListPanel1().getHeadBillModel().addLine();

    int rowcount = getBillListPanel1().getHeadTable().getRowCount();
    getBillListPanel1().getHeadBillModel().setBodyRowVO(newDjzbheadVo, rowcount - 1);

    getBillListPanel1().getHeadTable().getSelectionModel().setSelectionInterval(rowcount - 1, rowcount - 1);
  }

  public void changeDigByCurr(DJZBItemVO[] items)
    throws Exception
  {
    getBillListPanel1().getBodyBillModel().clearBodyData();

    int digit = 2;

    int i = 0; for (int size = items.length; i < size; i++) {
      if (getBillListPanel1().getBodyBillModel().getRowCount() < items.length)
        getBillListPanel1().getBodyBillModel().addLine();
      String currtype = items[i].getBzbm();

      if (currtype != null)
        digit = 
          Currency.getCurrDigit(currtype);
      BillItem billItem = getBillListPanel1().getBodyItem("ybye");
      if (billItem != null) {
        billItem.setDecimalDigits(digit);
      }
      billItem = getBillListPanel1().getBodyItem("jfybje");
      if (billItem != null) {
        billItem.setDecimalDigits(digit);
      }
      billItem = getBillListPanel1().getBodyItem("jfybwsje");
      if (billItem != null) {
        billItem.setDecimalDigits(digit);
      }
      billItem = getBillListPanel1().getBodyItem("jfybje");
      if (billItem != null) {
        billItem.setDecimalDigits(digit);
      }

      billItem = getBillListPanel1().getBodyItem("dfybje");
      if (billItem != null) {
        billItem.setDecimalDigits(digit);
      }

      billItem = getBillListPanel1().getBodyItem("dfybwsje");
      if (billItem != null) {
        billItem.setDecimalDigits(digit);
      }
      billItem = getBillListPanel1().getBodyItem("dfybje");
      if (billItem != null)
        billItem.setDecimalDigits(digit);
      billItem = getBillListPanel1().getBodyItem("dfybsj");
      if (billItem != null)
        billItem.setDecimalDigits(digit);
      billItem = getBillListPanel1().getBodyItem("jfybsj");
      if (billItem != null)
        billItem.setDecimalDigits(digit);
      Integer intFBDigit = getDjSettingParam().getDigit_f();
      Integer intBBDigit = getDjSettingParam().getDigit_b();

      if (intFBDigit != null) {
        billItem = getBillListPanel1().getBodyItem("jffbje");
        if (billItem != null) {
          billItem.setDecimalDigits(intFBDigit.intValue());
        }

        billItem = getBillListPanel1().getBodyItem("dffbje");
        if (billItem != null) {
          billItem.setDecimalDigits(intFBDigit.intValue());
        }
        billItem = getBillListPanel1().getBodyItem("fbye");
        if (billItem != null) {
          billItem.setDecimalDigits(intFBDigit.intValue());
        }
      }
      if (intBBDigit != null) {
        billItem = getBillListPanel1().getBodyItem("jfbbje");
        if (billItem != null) {
          billItem.setDecimalDigits(intBBDigit.intValue());
        }

        billItem = getBillListPanel1().getBodyItem("dfbbje");
        if (billItem != null) {
          billItem.setDecimalDigits(intBBDigit.intValue());
        }
        billItem = getBillListPanel1().getBodyItem("bbye");
        if (billItem != null) {
          billItem.setDecimalDigits(intBBDigit.intValue());
        }
      }

      String strDjdl = getDjDataBuffer().getCurrentDjdl();
      int rateDigit = 2;
      if (currtype != null)
        rateDigit = Currency.getRateDigit(getDjSettingParam().getPk_corp(), currtype);
      BillItem btBbhl = getBillListPanel1().getBodyItem("bbhl");
      if (btBbhl != null) {
        btBbhl.setDecimalDigits(rateDigit);
      }

      billItem = getBillListPanel1().getBodyItem("dj");
      if (billItem != null) {
        try {
          ResMessage res_num_dj = PubData.getDjNum(getDjSettingParam().getPk_corp());
          if (res_num_dj.isSuccess) {
            billItem.setDecimalDigits(res_num_dj.intValue);
          }

        }
        catch (Exception localException1)
        {
        }

      }

      if ((strDjdl.equals("ys")) || (strDjdl.equals("fk")) || 
        (strDjdl.equals("fj")) || (strDjdl.equals("wf"))) {
        try {
          if (getDjSettingParam().getFracCurrPK() == null) 
        	  break ;
          BillItem jffbje = getBillListPanel1()
            .getBodyItem("jfybje");
          jffbje.setDecimalDigits(digit);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
        }
      }
      else if ((strDjdl.equals("yf")) || (strDjdl.equals("sk")) || 
        (strDjdl.equals("sj")) || (strDjdl.equals("ws"))) {
        try {
          if (getDjSettingParam().getFracCurrPK() != null) {
            BillItem jffbje = getBillListPanel1()
              .getBodyItem("dfybje");
            if (jffbje != null)
              jffbje.setDecimalDigits(digit);
          }
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
        }

      }

      ResMessage res_num = PubData.getSlNum(getDjSettingParam().getPk_corp());
      if (res_num.isSuccess)
      {
        BillItem shlItem = getBillListPanel1().getBodyItem("shl");
        if (shlItem != null) {
          shlItem.setDecimalDigits(res_num.intValue);
        }
        BillItem dfshl_billitem = getBillListPanel1().getBodyItem("dfshl");
        if (dfshl_billitem != null) {
          dfshl_billitem.setDecimalDigits(res_num.intValue);
        }
        BillItem jfshl_billitem = getBillListPanel1().getBodyItem("jfshl");
        if (jfshl_billitem != null) {
          jfshl_billitem.setDecimalDigits(res_num.intValue);
        }
        BillItem shlye_billitem = getBillListPanel1().getBodyItem("shlye");
        if (shlye_billitem != null)
          shlye_billitem.setDecimalDigits(res_num.intValue);
      }
      getBillListPanel1().getBodyBillModel().setBodyRowVO(items[i], i);
    }
  }

  public boolean changePage(int pageIndex)
    throws Exception
  {
    getBillListPanel1().getHeadTable().setRowSelectionInterval(pageIndex, 
      pageIndex);
    try
    {
      djlbHeadRowChange(pageIndex);
    } catch (Throwable e) {
      Logger.debug(e);
    }

    Object djoid = getBillListPanel1().getHeadBillModel().getValueAt(
      pageIndex, "vouchid");
    if (djoid != null)
    {
      DJZBVO cur_djzbvo = null;

      ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

      cur_djzbvo = dataBuffer
        .getDJZBVO((String)djoid);

      DJZBHeaderVO head = (DJZBHeaderVO)cur_djzbvo
        .getParentVO();

      getArapDjPanel1().setDj(cur_djzbvo);
    }
    return true;
  }

  public void changeSelect(BillEditEvent e)
  {
    if (e.getKey().equals("isselected"))
    {
      String vouchid = getBillListPanel1().getHeadBillModel().getValueAt(
        e.getRow(), "vouchid").toString();

      ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

      DJZBVO localDJZBVO = dataBuffer
        .getDJZBVO(vouchid);
    }
  }

  public void changeTab(int tabIndex, boolean isChange)
  {
    changeTab_Init(tabIndex, isChange, false);
  }

  private void changeTab_Init(int tabIndex, boolean isChange, boolean bInitializing)
  {
    String djoid = null;
    int row = getBillListPanel1().getHeadTable().getSelectedRow();

    DJZBVO cur_djzbvo = null;

    DJZBHeaderVO head = null;
    int rowcount_h = getBillListPanel1().getHeadBillModel().getRowCount();
    if (row >= rowcount_h)
      row = rowcount_h - 1;
    if ((row >= 0) && (rowcount_h > 0)) {
      djoid = getBillListPanel1().getHeadBillModel().getValueAt(row, 
        "vouchid").toString();

      ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

      cur_djzbvo = dataBuffer.getDJZBVO(djoid);
      head = (DJZBHeaderVO)cur_djzbvo.getParentVO();
    }

    if (isChange) {
      if (tabIndex == 1)
        ((CardLayout)getLayout()).show(this, NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000021"));
      else {
        ((CardLayout)getLayout()).show(this, NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000049"));
      }
    }
    setM_TabIndex(getM_TabIndex() * -1);

    if ((tabIndex == 1) && (isChange))
    {
      if (getJPanel1().getComponentCount() < 1)
      {
        getJPanel1().add(getArapDjPanel1(), "Center");
        try
        {
          getArapDjPanel1().addDjStateChangeListener(this.m_djStateChange);
        }
        catch (Throwable localThrowable1)
        {
        }

        getArapDjPanel1().setDjlxVO_copy();
        try
        {
          getArapDjPanel1().changeTab2(0);
        }
        catch (Throwable localThrowable2)
        {
        }

        try
        {
          getArapDjPanel1().setM_DjState(0);
        }
        catch (Throwable e) {
          Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000440") + 
            e);
        }

      }

    }

    if (getM_TabIndex() == 1)
    {
      if (!bInitializing) {
        getBillListPanel1().getHeadBillModel().execLoadFormula();
        getBillListPanel1().getBodyBillModel().execLoadFormula();
      }
      this.m_boNext.setName(NCLangRes.getInstance().getStrByID("common", "UCH021"));
    }
    else
    {
      this.m_boNext.setName(NCLangRes.getInstance().getStrByID("common", "UCH022"));

      int count = 0;
      count = getBillListPanel1().getHeadBillModel().getRowCount();

      if (count > 0)
      {
        try
        {
          if ((getDjSettingParam().getSyscode() < 0) && (getDjSettingParam().getSyscode() == -2));
          getArapDjPanel1().setDj(cur_djzbvo);
        }
        catch (Throwable e) {
          Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000441") + e);
        }

      }

    }

    getDjSettingParam().getIsQc();

    if (getDjSettingParam().getSyscode() != -9999) {
      setM_Syscode(getDjSettingParam().getSyscode());
    }

    showHintMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000329"));
    refreshBtnStats();
  }

  public boolean djlbHeadRowChange(int billlistRowIndex)
    throws Exception
  {
    if (billlistRowIndex < 0) {
      return true;
    }
    String djzboid = "";

    djzboid = getBillListPanel1().getHeadBillModel().getValueAt(
      billlistRowIndex, "vouchid").toString();

    String ywbm = getBillListPanel1().getHeadBillModel().getValueAt(
      billlistRowIndex, "ywbm").toString();
    String dwbm = getBillListPanel1().getHeadBillModel().getValueAt(
      billlistRowIndex, "dwbm").toString();

    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

    DjLXVO cur_djlxvo = dataBuffer.getBillTypeVO(dwbm, ywbm);
    if (cur_djlxvo != null)
    {
      dataBuffer.setCurrentBillTypeInfo(cur_djlxvo);
    }
    if (cur_djlxvo != null)
      getArapDjPanel1().setTitleText(cur_djlxvo.getDjlxmc());
    DJZBItemVO[] djzbitemvo = (DJZBItemVO[])null;

    DJZBVO curdjzbvo = dataBuffer.getDJZBVO(djzboid);
    djzbitemvo = curdjzbvo.getChildrenVO() == null ? null : 
      (DJZBItemVO[])curdjzbvo.getChildrenVO();

    String strDjdl = dataBuffer.getCurrentDjdl();

    if ((djzbitemvo != null) && (djzbitemvo.length > 0)) {
      dataBuffer.setCurrentDJZBVO(curdjzbvo);
    }
    else {
      try {
        if ((getDjSettingParam().getSyscode() == 3) || 
          (strDjdl.equals("ss"))) {
          djzbitemvo = Proxy.getIArapItemPrivate().querySsItems(djzboid);
        } else if ("yt".equalsIgnoreCase(strDjdl)) {
          djzbitemvo = Proxy.getIArapTBBillPrivate().querTbItemsByHPK(djzboid);
        }
        else {
          DJZBHeaderVO head = (DJZBHeaderVO)curdjzbvo.getParentVO();
          if (head.getDr().intValue() == 0)
            djzbitemvo = Proxy.getIArapBillPrivate().queryDjzbitems(djzboid);
          else
            djzbitemvo = Proxy.getIArapBillPrivate().queryDjzbitemsDelIncluded(djzboid);
        }
      }
      catch (Exception e) {
        Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000903") + e);
        showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000904"));
        return false;
      }

      curdjzbvo.setChildrenVO(djzbitemvo);
      dataBuffer.putDJZBVO(djzboid, curdjzbvo);
      dataBuffer.setCurrentDJZBVO(curdjzbvo);
    }

    try
    {
      changeDigByCurr(djzbitemvo);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }

    getBillListPanel1().getBodyBillModel().execLoadFormula();
    try
    {
      String[] strArrayCDMBegin = (String[])MyClientEnvironment.getValue(getDjSettingParam().getPk_corp(), "CDMBegin", null);
      if ((strArrayCDMBegin != null) && (strArrayCDMBegin.length != 0))
      {
        String[] formular = { "htmc->getColvalue(v_fi_fi_code,code,pk_id,htbh)" };
        getBillListPanel1().getBodyBillModel().execFormulas(formular);
      }

    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }

    String strLang = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000004", null, new String[] { String.valueOf(billlistRowIndex + 1) });
    showHintMessage(strLang);
    return true;
  }

  private void editDjlb(DJZBVO djzbvo)
    throws Exception
  {
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvo
      .getParentVO();
    int row = getBillListPanel1().getHeadTable().getSelectedRow();
    if (!head.getVouchid().equals(getBillListPanel1().getHeadBillModel().getValueAt(row, "vouchid").toString()))
    {
      for (int i = 0; i < getBillListPanel1().getHeadBillModel().getRowCount(); i++) {
        if (head.getVouchid().equals(getBillListPanel1().getHeadBillModel().getValueAt(i, "vouchid").toString())) {
          row = i;
          break;
        }
      }
    }
    getDjDataBuffer().putDJZBVO(head.getVouchid(), djzbvo);
    try {
      getBillListPanel1().getHeadBillModel().setBodyRowVO(head, row);
    }
    catch (Throwable e)
    {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000453") + e);
    }
  }

  public Vector<String> getAllSelectedDJPK() {
    Vector vSelectedPK = new Vector();
    int row = 0;
    for (; row < 
      getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
      if (getBillListPanel1().getHeadBillModel().getValueAt(row, 
        "isselected") == null) {
        continue;
      }
      if (!((Boolean)getBillListPanel1().getHeadBillModel().getValueAt(
        row, "isselected")).booleanValue()) {
        continue;
      }
      String djzboid = null;
      djzboid = getBillListPanel1().getHeadBillModel().getValueAt(
        row, "vouchid").toString();
      vSelectedPK.addElement(djzboid);
    }

    return vSelectedPK;
  }

  public void reserveListSelected() {
    int row = 0;
    for (; row < 
      getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
      String djzboid = null;
      djzboid = getBillListPanel1().getHeadBillModel().getValueAt(
        row, "vouchid").toString();
      DJZBVO vo = getDjDataBuffer().getDJZBVO(djzboid);
      if (getDjDataBuffer().getListSelectedVOs().contains(vo))
        getDjDataBuffer().getListSelectedVOs().remove(vo);
      if (getBillListPanel1().getHeadBillModel().getValueAt(row, 
        "isselected") == null) {
        continue;
      }
      if (!((Boolean)getBillListPanel1().getHeadBillModel().getValueAt(
        row, "isselected")).booleanValue()) {
        continue;
      }
      if (!getDjDataBuffer().getListSelectedVOs().contains(vo))
        getDjDataBuffer().getListSelectedVOs().add(vo);
    }
  }

  public List<DJZBVO> getListAllSelectedVOs()
  {
    List ret = getDjDataBuffer().getListSelectedVOs();
    int row = 0;
    for (; row < 
      getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
      String djzboid = null;
      djzboid = getBillListPanel1().getHeadBillModel().getValueAt(
        row, "vouchid").toString();
      DJZBVO vo = getDjDataBuffer().getDJZBVO(djzboid);
      if (getDjDataBuffer().getListSelectedVOs().contains(vo)) {
        getDjDataBuffer().getListSelectedVOs().remove(vo);
      }
      if (getBillListPanel1().getHeadBillModel().getValueAt(row, 
        "isselected") == null) {
        continue;
      }
      if ((!((Boolean)getBillListPanel1().getHeadBillModel().getValueAt(
        row, "isselected")).booleanValue()) || 
        (getDjDataBuffer().getListSelectedVOs().contains(vo))) continue;
      getDjDataBuffer().getListSelectedVOs().add(vo);
    }

    return ret;
  }

  public Vector<Object> getAllSelectedDJIndex()
  {
    Vector vSelectedPK = new Vector();
    int row = 0;
    for (; row < 
      getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
      if (getBillListPanel1().getHeadBillModel().getValueAt(row, 
        "isselected") == null) {
        continue;
      }
      if (((Boolean)getBillListPanel1().getHeadBillModel().getValueAt(
        row, "isselected")).booleanValue()) {
        vSelectedPK.addElement(new Integer(row));
      }
    }
    return vSelectedPK;
  }

  public void setDjlxbm()
  {
  }

  public Vector<Integer> getAllBodySelectedDJIndex()
  {
    Vector vSelectedPK = new Vector();
    int row = 0;
    for (; row < 
      getBillListPanel1().getBodyBillModel().getRowCount(); row++) {
      if (getBillListPanel1().getBodyBillModel().getValueAt(row, 
        "isselected") == null) {
        continue;
      }
      if (((Boolean)getBillListPanel1().getBodyBillModel().getValueAt(
        row, "isselected")).booleanValue()) {
        vSelectedPK.addElement(new Integer(row));
      }
    }
    return vSelectedPK;
  }

  public ArapDjPanel getArapDjPanel1()
  {
    if (this.ivjArapDjPanel1 == null) {
      try {
        this.ivjArapDjPanel1 = new ArapDjPanel(getDjSettingParam());

        this.cache = new DjlxTempletCacheNew(getDjSettingParam().getPk_user(), getDjSettingParam().getPk_corp(), getNodeCode(), this.pzglh, this.ivjArapDjPanel1);

        this.ivjArapDjPanel1.setTempletCache(this.cache);
        this.ivjArapDjPanel1.setName("ArapDjPanel1");
        this.ivjArapDjPanel1.setAutoscrolls(true);

        this.ivjArapDjPanel1.setM_Parent(this);

        this.ivjArapDjPanel1.getBillCardPanelDj().setShowThMark(true);

        this.ivjArapDjPanel1.setDjDataBuffer(getDjDataBuffer());

        getArapDjPanel1().getBillCardPanelDj().addBodyMouseListener(
          new BillTableMouseListener()
        {
          public void mouse_doubleclick(BillMouseEnent e) {
            try {
              DjPanel.this.refreshBtnStats();
            } catch (Throwable localThrowable) {
            }
          }
        });
      }
      catch (Throwable ivjExc) {
        handleException(ivjExc);
      }

    }

    return this.ivjArapDjPanel1;
  }

  public BillListPanel getBillListPanel1()
  {
    if (this.ivjBillListPanel1 == null) {
      try {
        this.ivjBillListPanel1 = new BillListPanel();
        this.ivjBillListPanel1.setName("BillListPanel1");

        loadBillListTemplate();
        try
        {
          ((UIComboBox)this.ivjBillListPanel1
            .getBodyItem("wldx").getComponent()).addItem(ARAPDjUIFactory.createWldxItemKH());
          ((UIComboBox)this.ivjBillListPanel1
            .getBodyItem("wldx").getComponent()).addItem(ARAPDjUIFactory.createWldxItemGYS());

          ((UIComboBox)this.ivjBillListPanel1
            .getBodyItem("wldx").getComponent()).addItem(ARAPDjUIFactory.createWldxItemBM());
          ((UIComboBox)this.ivjBillListPanel1
            .getBodyItem("wldx").getComponent()).addItem(ARAPDjUIFactory.createWldxItemYWY());
        }
        catch (Throwable e)
        {
          Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000850") + e);
        }
        try {
          UIRefPane zy_ref = (UIRefPane)getBillListPanel1()
            .getBodyItem("zy").getComponent();
          zy_ref.setRefNodeName("³£ÓÃÕªÒª");

          zy_ref.setButtonVisible(true);
        } catch (Exception e) {
          Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000846") + e);
        }

        this.ivjBillListPanel1.getParentListPanel().setTotalRowShow(true);
        BillRendererVO voCell = new BillRendererVO();
        voCell.setShowThMark(true);
        voCell.setShowZeroLikeNull(true);
        this.ivjBillListPanel1.getChildListPanel().setShowFlags(voCell);
        this.ivjBillListPanel1.getParentListPanel().setShowFlags(voCell);
        this.ivjBillListPanel1.setEnabled(true);
        addBillLinstener(this.ivjBillListPanel1);
        Integer intFBDigit = getDjSettingParam().getDigit_f();
        Integer intBBDigit = getDjSettingParam().getDigit_b();
        BillItem bbje = this.ivjBillListPanel1.getHeadBillModel().getItemByKey("bbje");
        bbje.setDecimalDigits(intBBDigit.intValue());
        BillItem ybje = this.ivjBillListPanel1.getHeadBillModel().getItemByKey("ybje");
        ybje.setDecimalDigits(2);

        BillItem fbje = this.ivjBillListPanel1.getHeadBillModel().getItemByKey("fbje");
        fbje.setDecimalDigits(intFBDigit.intValue());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjBillListPanel1;
  }

  protected void addBillLinstener(BillListPanel ivjBillListPanel1) {
    ivjBillListPanel1
      .addMouseListener(new BillTableMouseListener()
    {
      public void mouse_doubleclick(BillMouseEnent e) {
        if (e.getPos() == 0)
          DjPanel.this.changeTab(DjPanel.this.getM_TabIndex(), true);
      }
    });
  }

  protected void loadBillListTemplate()
  {
    this.ivjBillListPanel1.loadTemplet(
      getNodeCode(), 
      null, 
      getDjSettingParam().getPk_user(), 
      getDjSettingParam().getPk_corp(), "DW");
    String[] strItems = { ArapConstant.DEFBODY };
    String[] strHeads = { ArapConstant.DEFHEAD };
    String[] strPrefix = { "zyx" };
    try {
      new ListDefShowUtil(getBillListPanel1(), new DefaultDefShowStrategyByBillItem()).showDefWhenRef(strItems, strPrefix, false);
      new ListDefShowUtil(getBillListPanel1(), new DefaultDefShowStrategyByBillItem()).showDefWhenRef(strHeads, strPrefix, true);
    }
    catch (Exception e) {
      Logger.debug(e.getMessage());
    }
  }

  public BusiTransVO[] getCurBusiTransVO()
  {
    return this.m_CurBusiTransVO;
  }

  public int getIndexByKey(DJZBHeaderVO[] vos, String vouchid)
  {
    int i = -1;
    for (i = 0; i < vos.length; i++) {
      if (vos[i].getPrimaryKey().equals(vouchid))
        break;
    }
    if (i < vos.length) {
      return i;
    }
    return -1;
  }

  public int getBodyShowInexByPK(String strItemPK)
  {
    int intRowCount = getArapDjPanel1().getBillCardPanelDj().getBillTable().getRowCount();
    for (int index = 0; index < intRowCount; index++)
    {
      String strRowItemPK = (String)getArapDjPanel1().getBillCardPanelDj().getBodyValueAt(index, "fb_oid");

      if (strItemPK.trim().equals(strRowItemPK.trim()))
        return index;
    }
    return -1;
  }

  public JPanel getJPanel1()
  {
    if (this.ivjJPanel1 == null) {
      try {
        this.ivjJPanel1 = new JPanel();
        this.ivjJPanel1.setName("JPanel1");
        this.ivjJPanel1.setLayout(new BorderLayout());

        this.ivjJPanel1.setName(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000021"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjJPanel1;
  }

  public int getM_TabIndex()
  {
    return this.m_TabIndex;
  }

  public DJZBItemVO[] getSelectedBodyRowVO(DJZBHeaderVO selectedHeader)
  {
    String strHeaderPK = selectedHeader.getVouchid();

    DJZBVO djzbvo = getDjDataBuffer().getDJZBVO(
      strHeaderPK);

    DJZBItemVO[] itemVOs = (DJZBItemVO[])djzbvo
      .getChildrenVO();

    Vector vItemVOs = new Vector();
    int row = 0;
    for (; row < 
      getBillListPanel1().getBodyBillModel().getRowCount(); row++) {
      if (getBillListPanel1().getBodyBillModel().getValueAt(row, 
        "isselected") == null) {
        continue;
      }
      if (((Boolean)getBillListPanel1().getBodyBillModel().getValueAt(
        row, "isselected")).booleanValue()) {
        String strItemPK = getBillListPanel1().getBodyBillModel()
          .getValueAt(row, "fb_oid").toString();

        DJZBItemVO selectedItemVO = getDjDataBuffer()
          .getDJZBItemVOByItemPK(strItemPK, itemVOs);
        vItemVOs.addElement(selectedItemVO);
      }
    }
    DJZBItemVO[] selectedItemVOArray = new DJZBItemVO[vItemVOs
      .size()];
    vItemVOs.copyInto(selectedItemVOArray);
    return selectedItemVOArray;
  }

  public Vector<DJZBHeaderVO> getSelectedHeadRowVO()
  {
    Vector vHeaderPK = new Vector();
    int row = 0;
    for (; row < 
      getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
      if (getBillListPanel1().getHeadBillModel().getValueAt(row, 
        "isselected") == null) {
        continue;
      }
      if (((Boolean)getBillListPanel1().getHeadBillModel().getValueAt(
        row, "isselected")).booleanValue()) {
        String strHeadPK = getBillListPanel1().getHeadBillModel()
          .getValueAt(row, "vouchid").toString();
        DJZBVO vo = getDjDataBuffer().getDJZBVO(
          strHeadPK);
        DJZBHeaderVO head = (DJZBHeaderVO)vo
          .getParentVO();
        vHeaderPK.addElement(head);
      }
    }
    return vHeaderPK;
  }

  public DjStateChange getStateChange()
  {
    return this.m_djStateChange;
  }

  public UIPanel getUIPanel2()
  {
    if (this.ivjUIPanel2 == null) {
      try {
        this.ivjUIPanel2 = new UIPanel();
        this.ivjUIPanel2.setName("UIPanel2");
        this.ivjUIPanel2.setAutoscrolls(true);
        this.ivjUIPanel2.setLayout(new BorderLayout());
        getUIPanel2().add(getBillListPanel1(), "Center");

        this.ivjUIPanel2.setName(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000049"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjUIPanel2;
  }

  public JPanel getVerifyPanel()
  {
    if (this.ivjVerifyPanel == null) {
      try {
        this.ivjVerifyPanel = new JPanel();
        this.ivjVerifyPanel.setName("VerifyPanel");
        this.ivjVerifyPanel.setLayout(new BorderLayout());

        this.ivjVerifyPanel.setName(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000053"));
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjVerifyPanel;
  }

  private void handleException(Throwable exception)
  {
    Logger.warn(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000281") + exception.getMessage(), exception);
  }

  protected void initConnections()
    throws Exception
  {
    getBillListPanel1().addEditListener(
      TempletController.getInstance().getBillEditListener(getNodeCode(), 1, this));

    getBillListPanel1().addBodyEditListener(
      TempletController.getInstance().getBillEditListener(getNodeCode(), 2, this));
  }

  public void initData(MessageVO msgvo)
  {
    DJZBHeaderVO djzbheadervo = null;
    try
    {
      if (getDjSettingParam().getSyscode() == 3)
        djzbheadervo = Proxy.getIArapItemPrivate().findSsHeaderByPK(
          msgvo.getBillPK());
      else
        djzbheadervo = Proxy.getIArapBillPrivate().findHeaderByPrimaryKey(
          msgvo.getBillPK());
    }
    catch (Exception e) {
      if ((e instanceof ShenheException)) {
        Logger.debug(
          ((ShenheException)e).m_ResMessage.strMessage);
        showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000449"));
        return;
      }
      if (((e instanceof RemoteException)) && 
        ((((RemoteException)e).detail instanceof ShenheException))) {
        RemoteException remE = (RemoteException)e;
        ShenheException shE = (ShenheException)remE.detail;
        Logger.debug(shE.m_ResMessage.strMessage);
        showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000449"));
        return;
      }

      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000450") + e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000451"));
      return;
    }

    if (djzbheadervo == null) {
      showHintMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000424"));

      ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

      dataBuffer.setCurrentDJZBVO(null);
      return;
    }

    DJZBHeaderVO[] djzbheadervos = new DJZBHeaderVO[1];
    djzbheadervos[0] = djzbheadervo;
    try
    {
      refDjLb(djzbheadervos);

      getDjDataBuffer().clearBuffer();
      DJZBVO temp_djzbvo = null;

      temp_djzbvo = new DJZBVO();
      temp_djzbvo.setParentVO(djzbheadervo);
      temp_djzbvo.setChildrenVO(null);
      getDjDataBuffer().putDJZBVO(djzbheadervo.getVouchid(), 
        temp_djzbvo);

      getBillListPanel1().getHeadTable().getSelectionModel()
        .setSelectionInterval(0, 0);
      djlbHeadRowChange(0);
    }
    catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000453") + e);
    }

    try
    {
      getBillListPanel1().getHeadBillModel().execLoadFormula();
    } catch (Exception e) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037") + e);
    }

    changeTab(1, true);
  }

  protected void initialize()
  {
    try
    {
      ButtonObject[] btArray = getDjButtons();
      setButtons(btArray);

      if (-1 == getLinkedType()) {
        MyClientEnvironment.refresh();
      }
      setName("DjPanel");
      setAutoscrolls(true);
      setLayout(new CardLayout());
      setSize(774, 419);
      add(getUIPanel2(), getUIPanel2().getName());
      add(getJPanel1(), getJPanel1().getName());
      add(getVerifyPanel(), getVerifyPanel().getName());
      initConnections();
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
    setM_TabIndex(-1);
    try
    {
      changeTab_Init(getM_TabIndex(), false, true);
    }
    catch (Throwable localThrowable1)
    {
    }
  }

  public void onButtonClicked(ButtonObject bo)
  {
    beginPressBtn(bo);
    showProgressBar(true);
    if (!checkID(bo)) {
      return;
    }
    super.onButtonClicked(bo);

    boolean bEventCaughtByContorllers = false;
    ARAPDjUIController[] controllers = getUIControllers();
    if (controllers != null)
    {
      for (int i = 0; i < controllers.length; i++)
      {
        bEventCaughtByContorllers = controllers[i].treatEvent(bo);

        if (bEventCaughtByContorllers)
        {
          break;
        }

      }

    }

    if ((!bEventCaughtByContorllers) && 
      (getCurBusiTransVO() != null)) {
      for (int i = 0; i < getCurBusiTransVO().length; i++) {
        if (bo == getCurBusiTransVO()[i].getBtnAssistant())
          runExtAssistant(getCurBusiTransVO()[i]);
      }
    }
    showProgressBar(false);

    if ((bo != this.m_boAddRow) && (bo != this.m_boDelRow) && (bo != this.m_boNewByBudget) && 
      (bo != this.m_boSplitSSItem) && (bo != this.m_boBudgetImpl))
    {
      getArapDjPanel1().hideGroupingSerialNumber();
    }

    if ((bo != this.m_boPrint) && (bo != this.m_boPrint_All))
      refreshBtnStats();
  }

  public boolean onClosing()
  {
    return true;
  }

  public boolean refDjLb(DJZBHeaderVO[] djzbheadervo)
    throws Exception
  {
    getBillListPanel1().getBodyBillModel().clearBodyData();
    getBillListPanel1().getHeadBillModel().clearBodyData();

    if ((djzbheadervo == null) || (djzbheadervo.length < 1)) {
      return true;
    }
    getBillListPanel1().setHeaderValueVO(djzbheadervo);

    return true;
  }

  public void resetDjlxRefBySyscode(UIRefPane ref) {
    String strDjdl = getDjDataBuffer().getCurrentDjdl();

    if (getDjSettingParam().getSyscode() == 0)
      ref
        .setWhereString(" where ( dr=0 and dwbm='" + 
        getClientEnvironment().getCorporation()
        .getPrimaryKey() + 
        "') and (djdl='ys' or djdl='sk') and djlxbm<>'DR'");
    else if (getDjSettingParam().getSyscode() == 1)
      ref
        .setWhereString(" where ( dr=0 and dwbm='" + 
        getClientEnvironment().getCorporation()
        .getPrimaryKey() + 
        "') and (djdl='yf' or djdl='fk') and djlxbm<>'DR'");
    else if ((getDjSettingParam().getSyscode() == 2) && (!strDjdl.equals("ss")))
      ref
        .setWhereString(" where ( dr=0 and dwbm='" + 
        getClientEnvironment().getCorporation()
        .getPrimaryKey() + 
        "') and djlxbm<>'DR' and djdl<>'ss' and djdl<>'lb' and djdl<>'ts' and djdl<>'yt'");
    else if (getDjSettingParam().getSyscode() == -2)
      ref.setWhereString(" where ( dr=0 and dwbm='" + 
        getClientEnvironment().getCorporation().getPrimaryKey() + 
        "') and (isqr='Y') and djlxbm<>'DR'");
    else if ((strDjdl.equals("ss")) || (getDjSettingParam().getSyscode() == 3))
      ref.setWhereString(" where ( dr=0 and dwbm='" + 
        getClientEnvironment().getCorporation().getPrimaryKey() + 
        "') and (djdl='ss') and djlxbm<>'DR' ");
    else
      ref
        .setWhereString(" where ( dr=0 and dwbm='" + 
        getClientEnvironment().getCorporation()
        .getPrimaryKey() + 
        "') and (djdl<>'lb' and djdl<>'yt' and djdl<>'ss' and djdl<>'ts' ) ");
  }

  public void runExtAssistant(BusiTransVO bvo)
  {
    int row = 0;

    DJZBVO djzb = null;

    if (getM_TabIndex() == 1)
    {
      if (getBillListPanel1().getHeadBillModel().getRowCount() < 1) {
        return;
      }
      int row_h = getBillListPanel1().getHeadTable().getSelectedRow();
      if (row_h < 0) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000423"));
        return;
      }

      row = getBillListPanel1().getBodyTable().getSelectedRow();
      if (row < 0) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000943"));
        return;
      }

      if (getBillListPanel1().getHeadBillModel().getValueAt(row_h, 
        "vouchid") == null)
        return;
      String vouchid = getBillListPanel1().getHeadBillModel().getValueAt(
        row_h, "vouchid").toString();
      djzb = getDjDataBuffer().getDJZBVO(vouchid);
    }
    else {
      ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

      djzb = dataBuffer.getCurrentDJZBVO();
      row = getArapDjPanel1().getBillCardPanelDj().getBillTable()
        .getSelectedRow();

      if (row < 0) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000943"));
        return;
      }

    }

    DJZBItemVO[] items = (DJZBItemVO[])djzb
      .getChildrenVO();

    String fb_oid = items[row].getFb_oid();
    try {
      ((ArapAssistantInf)bvo.getInfClass())
        .runAssistant(djzb, fb_oid);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      showHintMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000944") + bvo.getUsesystemname());
    }
  }

  public void selectedAll()
  {
    getBillListPanel1().setEnabled(true);

    int row = 0;
    for (; row < 
      getBillListPanel1().getHeadBillModel().getRowCount(); row++)
      getBillListPanel1().getHeadBillModel().setValueAt(
        ArapConstant.UFBOOLEAN_TRUE, row, "isselected");
  }

  public void setButtonVisible(int tabIndex)
  {
    setM_Syscode(getDjSettingParam().getSyscode());
    refreshBtnStats();
  }

  public void setCurBusiTransVO(BusiTransVO[] newCurBusiTransVO)
  {
    try
    {
      if (newCurBusiTransVO == null)
      {
        newCurBusiTransVO = Proxy.getIArapBillPrivate()
          .initBusiTrans(getClientEnvironment().getCorporation()
          .getPrimaryKey(), 
          new Integer(getDjSettingParam().getSyscode()));
      }
      this.m_CurBusiTransVO = newCurBusiTransVO;
      int i = 0;
      do {
        this.m_boAssistant.addChildButton(
          this.m_CurBusiTransVO[i].getBtnAssistant());

        i++;

        if (this.m_CurBusiTransVO == null) break; 
      }
      while (i < this.m_CurBusiTransVO.length);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      showHintMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000947"));
    }
  }

  public void setDjlxbm(String djlxbm)
  {
    try
    {
      DjLXVO cur_djlxvo = (DjLXVO)MyClientEnvironment.getValue(getDjSettingParam().getPk_corp(), "DJLX", djlxbm);

      getDjDataBuffer().setCurrentBillTypeInfo(cur_djlxvo);
    }
    catch (Exception e) {
      MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000842"), NCLangRes.getInstance().getStrByID("2008", "UPP2008-000115"));
    }
  }

  protected void setM_bQc(boolean newM_bQc)
  {
    getDjSettingParam().setIsQc(newM_bQc);
    refreshBtnStats();
  }

  protected void setM_Node(String newM_Node)
  {
    getDjSettingParam().setNodeID(newM_Node);
  }

  public void setM_Syscode(int newM_Syscode)
  {
    getDjSettingParam().setSyscode(newM_Syscode);

    if (getDjSettingParam().getSyscode() == -1)
    {
      try
      {
        if (getM_TabIndex() != 1) return; setTitleText(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000411"));
      }
      catch (Exception localException) {
      }
    }
    else if (getDjSettingParam().getSyscode() == -2)
    {
      try
      {
        if (getM_TabIndex() != 1) return; setTitleText(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000412"));
      } catch (Exception localException1) {
      }
    }
    else if ((getDjSettingParam().getSyscode() == -9) || 
      (getDjSettingParam().getSyscode() == -99) || 
      (getDjSettingParam().getSyscode() == -999))
    {
      try
      {
        if (getM_TabIndex() != 1) return; setTitleText(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000942"));
      }
      catch (Exception localException2)
      {
      }
    }
    else if (getDjSettingParam().getSyscode() == 3)
      try
      {
        if (getM_TabIndex() != 1) return; setTitleText(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000941"));
      }
      catch (Exception localException3) {
      }
    else getDjSettingParam().getSyscode();
  }

  public void setM_TabIndex(int newM_TabIndex)
  {
    this.m_TabIndex = newM_TabIndex;
  }

  public void test()
  {
  }

  public void unSelectedAll()
  {
    int row = 0;
    for (; row < 
      getBillListPanel1().getHeadBillModel().getRowCount(); row++)
      getBillListPanel1().getHeadBillModel().setValueAt(
        ArapConstant.UFBOOLEAN_FALSE, row, "isselected");
  }

  public void updateButton(ButtonObject bo)
  {
    super.updateButton(bo);
  }

  public void refreshBtnStats()
  {
    if (getDjButtons() != null) {
      for (int i = 0; i < getDjButtons().length; i++) {
        getDjButtons()[i].setEnabled(false);
        if (getDjButtons()[i].getChildCount() > 0) {
          for (int j = 0; j < getDjButtons()[i].getChildCount(); j++) {
            getDjButtons()[i].getChildButtonGroup()[j].setEnabled(false);
          }
        }
      }
    }
    ButtonObject[] enableBtn = getEnableButtonArry();
    if (enableBtn != null) {
      for (int i = 0; i < enableBtn.length; i++) {
        enableBtn[i].setVisible(true);
        enableBtn[i].setEnabled(true);
      }
    }

    if (getCurrWorkPage() != getLastWorkPage()) {
      setButtons(getDjButtons());
    }
    updateButtons();
    setLastWorkPage(getCurrWorkPage());
  }

  public int getCurrWorkPage()
  {
    if (getM_TabIndex() == 1)
      return ArapBillWorkPageConst.LISTPAGE;
    if (getM_TabIndex() == -1) {
      if (getArapDjPanel1().getM_TabIndex() == 0) {
        if (getArapDjPanel1().m_iVitStat == ArapBillWorkPageConst.VITPAGE)
          return ArapBillWorkPageConst.VITPAGE;
        if (getArapDjPanel1().m_iVitStat == ArapBillWorkPageConst.MAKEUPPAGE) {
          return ArapBillWorkPageConst.MAKEUPPAGE;
        }
        return ArapBillWorkPageConst.CARDPAGE;
      }
      return ArapBillWorkPageConst.ZYXPAGE;
    }

    return ArapBillWorkPageConst.LISTPAGE;
  }

  protected int getLastWorkPage()
  {
    return this.m_lastWorkPage;
  }

  protected void setLastWorkPage(int newWorkpage)
  {
    this.m_lastWorkPage = newWorkpage;
  }

  protected ButtonObject[] getEnableButtonArry()
  {
    return new ButtonObject[0];
  }

  public boolean isInitied()
  {
    return this.isInitied;
  }

  public void setInitied(boolean isInitied)
  {
    this.isInitied = isInitied;
  }

  public int getHeadRowInexByPK(String strHeadPK)
  {
    int intRowCount = getBillListPanel1().getHeadTable().getRowCount();
    for (int index = 0; index < intRowCount; index++)
    {
      String strRowPK = getBillListPanel1().getHeadBillModel().getValueAt(index, "vouchid").toString();
      if (strRowPK.trim().equals(strHeadPK.trim()))
        return index;
    }
    return -1;
  }

  public Cache getVoCache()
  {
    return this.voCache;
  }

  public void setVoCache(Cache voCache)
  {
    this.voCache = voCache;
  }

  public DjCondVO getCur_Djcondvo()
  {
    return this.cur_Djcondvo;
  }

  public void setCur_Djcondvo(DjCondVO cur_Djcondvo)
  {
    this.cur_Djcondvo = cur_Djcondvo;
  }

  public void showErrorMessage(String err)
  {
    super.showErrorMessage(err);
    super.showHintMessage(err);
  }

  public void listBodyAfterEdit(BillEditEvent e) {
  }

  public void listBodyRowChange(BillEditEvent e) {
    getBillListPanel1().getBodyBillModel().setValueAt(Boolean.FALSE, 
      e.getOldRow(), "isselected");
    getBillListPanel1().getBodyBillModel().setValueAt(Boolean.TRUE, 
      e.getRow(), "isselected");
  }

  public void listHeadAfterEdit(BillEditEvent e) {
    changeSelect(e);
    try
    {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000910") + 
        e.getRow() + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000652"));
      djlbHeadRowChange(e.getRow());
    }
    catch (Throwable ee) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000911") + ee);
    }
  }

  public void listHeadRowChange(BillEditEvent e)
  {
    try
    {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000910") + 
        e.getRow() + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000652"));
      djlbHeadRowChange(e.getRow());
    }
    catch (Throwable ee) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000911") + ee);
    }
  }

  public void doApproveAction(ILinkApproveData approvedata) {
    try {
      this.PanelProp = 1;
      DJZBVO djzbvo = null;
      DjLXVO djlxvo = (DjLXVO)MyClientEnvironment.getValue(approvedata.getPkOrg(), "DJLX", approvedata.getBillType());
      try {
        if ("ss".equalsIgnoreCase(djlxvo.getDjdl())) {
          djzbvo = Proxy.getIArapBillPublic().findArapBillByPKSS(approvedata.getBillID());
        }
        else
          djzbvo = Proxy.getIArapBillPublic().findArapBillByPK(approvedata.getBillID());
      }
      catch (Exception e) {
        Logger.debug(e.getMessage());
        Log.getInstance(getClass()).error(e.getMessage(), e);
      }
      if (djzbvo == null) {
        return;
      }
      DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();
      getDjSettingParam().setPk_corp(head.getDwbm());
      int pzglh = ((DJZBHeaderVO)djzbvo.getParentVO()).getPzglh().intValue();
      if (((getNodeCode().startsWith("2006")) || (getNodeCode().startsWith("2008"))) && (pzglh == 2)) {
        setM_Syscode(2);
        setM_Node("2010030102");
        setDjlxbm(
          PubData.getDjlxbmByPkcorp(
          head.getDwbm(), 
          2));
      } else if ("ss".equalsIgnoreCase(djlxvo.getDjdl()))
      {
        setM_Syscode(3);
        setM_Node("20100306");
        ARAPDjDataBuffer dataBuffer = super.getDjDataBuffer();
        dataBuffer.setCurrentDjdl("ss");

        setDjlxbm(PubData.getDjlxbmByPkcorp(getClientEnvironment().getCorporation().getPrimaryKey(), 3));
      }
      ButtonObject[] btArray = getDjButtons();
      setButtons(btArray);
      setName("DjPanel");
      setAutoscrolls(true);
      setLayout(new CardLayout());
      setSize(774, 419);
      add(getJPanel1(), getJPanel1().getName());
      add(getVerifyPanel(), getVerifyPanel().getName());
      getDjDataBuffer().putDJZBVO(head.getVouchid(), djzbvo);
      ((CardLayout)getLayout()).show(this, NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000021"));
      getJPanel1().add(getArapDjPanel1(), "Center");
      getArapDjPanel1().addDjStateChangeListener(this.m_djStateChange);

      getArapDjPanel1().setM_DjState(0);
      setM_TabIndex(-1);

      getArapDjPanel1().setDj(djzbvo);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
    refreshBtnStats();
  }

  public void doMaintainAction(ILinkMaintainData maintaindata)
  {
    this.PanelProp = 4;
    setTempString(maintaindata.getBillID());
    doQryAction(new ILinkQueryData()
    {
      public String getBillID()
      {
        return DjPanel.this.getTempString();
      }

      public String getBillType()
      {
        return null;
      }

      public String getPkOrg()
      {
        return null;
      }

      public Object getUserObject()
      {
        return null;
      } } );
  }

  public void doQueryAction(ILinkQueryData querydata) {
    this.PanelProp = 2;
    doQryAction(querydata);
  }
  public void doQryAction(ILinkQueryData querydata) {
    ButtonObject[] btArray = getDjButtons();
    setButtons(btArray);
    String[] vouchids = (String[])null;
    try {
      vouchids = (String[])((Object[])querydata.getUserObject())[0];
    } catch (Exception localException1) {
    }
    if ((vouchids == null) && (querydata.getBillID() != null))
      vouchids = new String[] { querydata.getBillID() };
    if ((vouchids == null) || (vouchids.length == 0))
      return;
    DJZBVO[] djzbvos = (DJZBVO[])null;
    try
    {
      djzbvos = Proxy.getIArapBillPublic().findArapBillByPKs(vouchids);
      if (djzbvos == null) {
        Vector v = new Vector();
        int i = 0; for (int size = vouchids.length; i < size; i++) {
          v.add(vouchids[i]);
        }
        djzbvos = Proxy.getIArapBillPrivate().findByPrimaryKeys_SS(v);
      }
      if (djzbvos == null) {
        djzbvos = new DJZBVO[] { Proxy.getIArapTBBillPrivate().findTbByPrimaryKey(vouchids[0]) };
      }
    }
    catch (Exception e)
    {
      Logger.debug(e.getMessage());
    }
    if ((djzbvos == null) || (djzbvos.length == 0)) {
      return;
    }
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvos[0].getParentVO();
    getDjSettingParam().setPk_corp(head.getDwbm());
    initialize();
    int pzglh = head.getPzglh().intValue();
    if ("ss".equalsIgnoreCase(head.getDjdl())) {
      setM_Syscode(3);
      setM_Node("20100306");
      ARAPDjDataBuffer dataBuffer = super.getDjDataBuffer();
      dataBuffer.setCurrentDjdl("ss");
      loadBillListTemplate();

      setDjlxbm(PubData.getDjlxbmByPkcorp(getClientEnvironment().getCorporation().getPrimaryKey(), 3));
    }
    else if ("yt".equalsIgnoreCase(head.getDjdl())) {
      setM_Syscode(4);
      setM_Node("20100310");
      ARAPDjDataBuffer dataBuffer = super.getDjDataBuffer();
      dataBuffer.setCurrentDjdl("yt");
      setDjlxbm("DV");
      loadBillListTemplate();
    }
    else if (((getNodeCode().startsWith("2006")) || (getNodeCode().startsWith("2008"))) && (pzglh == 2)) {
      setM_Syscode(2);
      setM_Node("2010030102");
      loadBillListTemplate();
      setDjlxbm(
        PubData.getDjlxbmByPkcorp(
        head.getDwbm(), 
        2));
    }
    else if (((getNodeCode().startsWith("2010")) || (getNodeCode().startsWith("2008"))) && (pzglh == 0)) {
      setM_Syscode(0);
      setM_Node("2006030102");
      loadBillListTemplate();
      setDjlxbm(
        PubData.getDjlxbmByPkcorp(
        head.getDwbm(), 
        0));
    } else if (((getNodeCode().startsWith("2010")) || (getNodeCode().startsWith("2006"))) && (pzglh == 1)) {
      setM_Syscode(1);
      setM_Node("2008030102");
      loadBillListTemplate();
      setDjlxbm(
        PubData.getDjlxbmByPkcorp(
        head.getDwbm(), 
        1));
    }
    if (getM_TabIndex() == -1) {
      try {
        changeTab(getM_TabIndex(), true);
      } catch (Throwable e) {
        Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000912") + e);
      }
    }
    DJZBHeaderVO[] djzbheadervo = new DJZBHeaderVO[djzbvos.length];
    try {
      for (int i = 0; i < djzbvos.length; i++) {
        djzbheadervo[i] = ((DJZBHeaderVO)djzbvos[i].getParentVO());
        getDjDataBuffer().putDJZBVO(djzbheadervo[i].getVouchid(), djzbvos[i]);
      }
      refDjLb(djzbheadervo);
      getBillListPanel1().getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
      djlbHeadRowChange(0);
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000453") + e);
    }
    try
    {
      getBillListPanel1().getHeadBillModel().execLoadFormula();
    } catch (Exception e) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037") + e);
    }
    if (djzbvos.length == 1) {
      try {
        changeTab(getM_TabIndex(), true);
      } catch (Throwable e) {
        Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000912") + e);
      }
    }
    refreshBtnStats();
  }

  public void doAddAction(ILinkAddData adddata) {
    this.PanelProp = 3;
    if ((adddata == null) || (adddata.getUserObject() == null))
      return;
    DJZBVO[] djzbvos = (DJZBVO[])((Object[])adddata.getUserObject())[0];
    if ((djzbvos == null) || (djzbvos.length == 0)) {
      return;
    }
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvos[0].getParentVO();
    getDjSettingParam().setPk_corp(head.getDwbm());
    initialize();
    int pzglh = ((DJZBHeaderVO)djzbvos[0].getParentVO()).getPzglh().intValue();
    if (((getNodeCode().startsWith("2006")) || (getNodeCode().startsWith("2008"))) && (pzglh == 2)) {
      setM_Syscode(2);
      setM_Node("2010030102");
      loadBillListTemplate();
      setDjlxbm(
        PubData.getDjlxbmByPkcorp(
        head.getDwbm(), 
        2));
    }
    else if (((getNodeCode().startsWith("2010")) || (getNodeCode().startsWith("2008"))) && (pzglh == 0)) {
      setM_Syscode(0);
      setM_Node("2006030102");
      loadBillListTemplate();
      setDjlxbm(
        PubData.getDjlxbmByPkcorp(
        head.getDwbm(), 
        0));
    } else if (((getNodeCode().startsWith("2010")) || (getNodeCode().startsWith("2006"))) && (pzglh == 1)) {
      setM_Syscode(1);
      setM_Node("2008030102");
      loadBillListTemplate();
      setDjlxbm(
        PubData.getDjlxbmByPkcorp(
        head.getDwbm(), 
        1));
    }
    if (getM_TabIndex() == -1) {
      try {
        changeTab(getM_TabIndex(), true);
      } catch (Throwable e) {
        Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000912") + e);
      }

    }

    try
    {
      changeTab(getM_TabIndex(), true);
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000912") + e);
    }
    getArapDjPanel1().setDj(djzbvos[0]);
    getArapDjPanel1().setM_DjState(ArapBillWorkPageConst.WORKSTAT_NEW);
    refreshBtnStats();
  }

  public void setLinkData(Object[] args)
  {
    String[] vouchids = (String[])args[0];
    DJZBVO[] djzbvos = (DJZBVO[])null;
    try {
      djzbvos = Proxy.getIArapBillPublic().findArapBillByPKs(vouchids);
    }
    catch (Exception e) {
      Logger.debug(e.getMessage());
    }
    if (getM_TabIndex() == -1) {
      try {
        changeTab(getM_TabIndex(), true);
      } catch (Throwable e) {
        Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000912") + e);
      }
    }
    DJZBHeaderVO[] djzbheadervo = new DJZBHeaderVO[djzbvos.length];
    try {
      for (int i = 0; i < djzbvos.length; i++) {
        djzbheadervo[i] = ((DJZBHeaderVO)djzbvos[i].getParentVO());
        getDjDataBuffer().putDJZBVO(djzbheadervo[i].getVouchid(), djzbvos[i]);
      }
      refDjLb(djzbheadervo);
      getBillListPanel1().getHeadTable().getSelectionModel().setSelectionInterval(0, 0);
      djlbHeadRowChange(0);
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000453") + e);
    }
    try
    {
      getBillListPanel1().getHeadBillModel().execLoadFormula();
    } catch (Exception e) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000037") + e);
    }
    if (djzbvos.length == 1)
      try {
        changeTab(getM_TabIndex(), true);
      } catch (Throwable e) {
        Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000912") + e);
      }
    refreshBtnStats();
  }

  public String getTempString() {
    return this.tempString;
  }

  public void setTempString(String tempString) {
    this.tempString = tempString;
  }

  class DjStateChange
    implements DjStateChangeListener
  {
    DjStateChange()
    {
    }

    public void zyxStateChange(ArapDjPanel e)
    {
      Logger.debug("setZyxButtonsState");
      DjPanel.this.refreshBtnStats();
    }

    public void tabIndexChange(ArapDjPanel e)
    {
      Logger.debug("setButtonVisible");

      DjPanel.this.setButtonVisible(e.getM_TabIndex());
    }

    public void djStateChange(ArapDjPanel e)
    {
      Logger.debug("setButtonsState");
      DjPanel.this.refreshBtnStats();
    }

    public void actionPerformed(ActionEvent e)
    {
    }

    public void deletedDj(ArapDjPanel e)
    {
    }

    public void saveNewDj(ArapDjPanel e)
    {
      try {
        if (!DjPanel.this.m_isPayPanel)
          DjPanel.this.addDjlb(DjPanel.this.getDjDataBuffer().getCurrentDJZBVO());
      } catch (Throwable ee) {
        Logger.debug("addDjlb error: " + ee);
      }
    }

    public void selectedBodyRow(ArapDjPanel e)
    {
      try
      {
        DjPanel.this.refreshBtnStats();
      } catch (Exception ee) {
        Logger.debug("setVerifyVisible error: " + ee);
      }
    }

    public void saveEditDj(ArapDjPanel e)
    {
      try
      {
        if (!DjPanel.this.m_isPayPanel)
          DjPanel.this.editDjlb(DjPanel.this.getDjDataBuffer().getCurrentDJZBVO());
      } catch (Throwable ee) {
        Logger.debug("editDjlb error: " + ee);
      }
    }
  }
}