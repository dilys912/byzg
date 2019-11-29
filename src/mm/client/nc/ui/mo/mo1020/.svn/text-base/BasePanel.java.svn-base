package nc.ui.mo.mo1020;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashMap;
import nc.itf.mm.prv.IMO;
import nc.ui.common.CommonUtil;
import nc.ui.common.PublicUtil;
import nc.ui.common.util.Util;
import nc.ui.ic.pub.InvOnHandDialog;
import nc.ui.ml.NCLangRes;
import nc.ui.mm.pub.MMToftPanel;
import nc.ui.mm.pub.pub1040.FreeItemUIUtilies;
import nc.ui.mm.pub.pub1050.SourceBillFlowDlg;
import nc.ui.mm.pub.pub1060.ReeditViewDialog;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.lock.LockBO_Client;
import nc.ui.pub.print.PrintEntry;
import nc.vo.bd.b431.InvbasdocVO;
import nc.vo.bd.fd.DdVO;
import nc.vo.mm.proxy.MMProxy;
import nc.vo.mm.pub.pub1025.PoVO;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mm.pub.pub1030.MoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

public class BasePanel extends UIPanel
{
  private int iuistat;
  protected final MMToftPanel tp;
  private final CardLayout layout = new CardLayout();
  protected MOViewPanel viewPanel;
  private MOEditPanel editPanel;
  private QueryCondition queryCondition;
  DdVO[] m_dds;
  private SimPanel simPanel;
  private boolean m_withSim;
  private String lockedpk;
  private MMToolKit toolKit;
  private String m_QuerySQL;
  UIDialog m_iohdDlg;

  public void onSave()
  {
    MoVO vo = getEditPanel().saveData();
    if (vo == null)
      return;
    try
    {
      free();
    }
    catch (Exception e)
    {
      this.tp.showWarningMessage(getstrbyid("UPP50081020-000072") + e.getLocalizedMessage());
      e.printStackTrace();
      return;
    }
    try
    {
      getViewPanel().updateSoleMO(vo, this.iuistat == 3);
      setState(1);
      updateUI();
    }
    catch (Exception ex)
    {
      try
      {
        free();
      }
      catch (Exception e)
      {
        this.tp.showWarningMessage(getstrbyid("UPP50081020-000072") + e.getLocalizedMessage());
        e.printStackTrace();
        return;
      }
      this.tp.showWarningMessage(getstrbyid("UPP50081020-000079") + ex.getLocalizedMessage());
      ex.printStackTrace();
    }
  }

  public BasePanel(MMToftPanel toftp)
  {
    this.iuistat = 0;
    this.viewPanel = null;
    this.editPanel = null;
    this.queryCondition = null;
    this.m_dds = null;
    this.simPanel = null;
    this.m_withSim = false;
    this.lockedpk = null;
    this.toolKit = null;
    this.m_QuerySQL = null;
    this.m_iohdDlg = null;
    this.tp = toftp;
    initialize();
  }

  private String getstrbyid(String number)
  {
    return NCLangRes.getInstance().getStrByID("50081020", number);
  }

  protected void free()
    throws Exception
  {
    try
    {
      if (!isNull(this.lockedpk))
      {
        LockBO_Client.freePK(this.lockedpk, this.tp.getUser().getPrimaryKey(), "mm_mo");
        this.lockedpk = null;
      }
    }
    catch (Exception ex)
    {
      throw ex;
    }
  }

  protected DdVO[] getDds()
  {
    if (this.m_dds == null)
      try
      {
        this.m_dds = MOAbstractPanel.getDdsOfMO();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        this.tp.showWarningMessage(getstrbyid("UPP50081020-000219") + ex.getMessage());
      }
    return this.m_dds;
  }

  protected MOEditPanel getEditPanel()
  {
    if (this.editPanel == null)
    {
      this.editPanel = new MOEditPanel(this.tp, getToolKit(), getDds());
      this.editPanel.setName("MOEdit");
    }
    return this.editPanel;
  }

  private QueryCondition getQueryCondition()
  {
    if (this.queryCondition == null)
    {
      this.queryCondition = new QueryCondition(this, this.tp, getDds());
      this.queryCondition.setDefaultCloseOperation(1);
    }
    return this.queryCondition;
  }

  private SimPanel getSimPanel()
  {
    if (this.simPanel == null)
      this.simPanel = new SimPanel(this.tp);
    return this.simPanel;
  }

  protected MMToolKit getToolKit()
  {
    if (this.toolKit == null)
      this.toolKit = new MMToolKit(this.tp.getUnitCode(), this.tp.getFactoryCode());
    return this.toolKit;
  }

  public MOViewPanel getViewPanel()
  {
    if (this.viewPanel == null)
    {
      this.viewPanel = new MOViewPanel(this.tp, getToolKit(), getDds());
      this.viewPanel.setName("View");
    }
    return this.viewPanel;
  }

  public String getTempletTitle()
  {
    return getEditPanel().getCardPanel().getTitle();
  }

  private void initialize()
  {
    try
    {
      setBounds(0, 0, 774, 419);
      setLayout(this.layout);
      add(getViewPanel(), getViewPanel().getName());
      add(getEditPanel(), getEditPanel().getName());
      setState(0);
    }
    catch (Exception ivjExc)
    {
      ivjExc.printStackTrace();
    }
  }

  private boolean isNull(Object o)
  {
    return MMToolKit.isNull(o);
  }

  protected boolean lock()
    throws BusinessException
  {
    try
    {
      this.lockedpk = getViewPanel().getSelectedPK();
      boolean lockFlag = LockBO_Client.lockPK(this.lockedpk, this.tp.getUser().getPrimaryKey(), "mm_mo");
      return lockFlag;
    }
    catch (Exception ex) {
    
    throw new BusinessException(ex.getMessage());
    }
  }

  public void onAdd()
  {
    getEditPanel().setNewData(null);
    setState(3);
    
  }

  public void onBatchRevise()
  {
    getViewPanel().onBatchRevise();
  }

  public void onCancel()
  {
    setState(1);
    this.tp.showHintMessage(getstrbyid("UPP50081020-000073"));
    try
    {
      free();
    }
    catch (Exception ex)
    {
      this.tp.showWarningMessage(getstrbyid("UPP50081020-000072") + ex.getLocalizedMessage());
      ex.printStackTrace();
      return;
    }
  }

  public void onDelete()
  {
    getViewPanel().onDelete();
  }

  public void onFinish()
  {
    getViewPanel().onFinish();
  }

  public void onMoCancel()
  {
    getViewPanel().onMoCancel();
    if (this.iuistat == 2)
    {
      MoVO vo = getViewPanel().getSelectedVO();
      if (vo != null)
      {
        getEditPanel().setModifyData(vo);
        getEditPanel().setEnabled(false);
      }
    }
  }

  public void onModify()
    throws BusinessException
  {
    this.tp.showHintMessage("");
    MoVO vo = getViewPanel().getSelectedVO();
    if (vo == null)
      return;
    boolean lock = lock();
    if (!lock)
    {
      this.tp.showWarningMessage(getstrbyid("UPP50081020-000051"));
      return;
    }
    try
    {
      int stat = getEditPanel().setModifyData(vo);
      setState(stat);
      getEditPanel().setState(4);
    }
    catch (Exception ex)
    {
      try
      {
        free();
      } catch (Exception localException1) {
      }
      ex.printStackTrace();
      this.tp.showWarningMessage(getstrbyid("UPP50081020-000074") + ex.getMessage());
    }
  }

  public void onMoPut()
  {
    getViewPanel().onMoPut();
    if (this.iuistat == 2)
    {
      MoVO vo = getViewPanel().getSelectedVO();
      if (vo != null)
      {
        getEditPanel().setModifyData(vo);
        getEditPanel().setEnabled(false);
      }
    }
  }

  public void onOutSubmit()
  {
    getViewPanel().onOutSubmit();
  }

  public void onOver()
  {
    getViewPanel().onOver();
  }

  public boolean onClosing()
  {
    if ((this.iuistat == 5) || (this.iuistat == 3) || (this.iuistat == 4))
    {
      int id = MessageDialog.showYesNoCancelDlg(this, null, 
        NCLangRes.getInstance().getStrByID(
        "10092020", "MT3", null, new String[] { getstrbyid("UC001-0000001") }) + "?");

      if (id == 4) {
        onSave();
      }
      else if (id == 8)
        onCancel();
      else
        return false;
      return true;
    }

    return true;
  }

  public void onPrint()
  {
    PrintDataSource dataSource = null;
    MoHeaderVO[] heads = (MoHeaderVO[])null;
    if (this.iuistat == 1)
    {
      heads = getViewPanel().getPrintData();
      if ((heads == null) || (heads.length == 0))
        return;
      dataSource = new PrintDataSource("50081020", getEditPanel().getBillData(), heads);
    }
    else {
      MoVO vo = getEditPanel().getPrintData();
      if ((vo == null) || (vo.getParentVO() == null))
        return;
      heads = new MoHeaderVO[] { 
        (MoHeaderVO)vo.getParentVO() };

      dataSource = new PrintDataSource("50081020", getEditPanel().getBillData(), vo);
    }
    final String[] sMOPKs = new String[heads.length];
    StringBuffer errmsg = new StringBuffer("");
    for (int i = 0; i < heads.length; i++)
    {
      sMOPKs[i] = heads[i].getPk_moid();
      if ((heads[i].getDyzt() != null) && (heads[i].getDyzt().intValue() > 0)) {
        errmsg.append(heads[i].getScddh()).append(", ");
      }
    }
    if (errmsg.length() > 0)
    {
      errmsg.delete(errmsg.length() - 2, errmsg.length());
      errmsg.insert(0, getstrbyid("UPP50081020-000075"));
      errmsg.append(getstrbyid("UPP50081020-000076"));
      if (this.tp.showYesNoMessage(errmsg.toString()) != 4)
        return;
    }
    try
    {
      String[] sInvPKs = new String[heads.length];
      for (int i = 0; i < heads.length; i++) {
        sInvPKs[i] = heads[i].getWlbmid();
      }
      InvbasdocVO[] invVOs = MMProxy.getRemoteMO().getInvBasDocVO(sInvPKs);
      for (int i = 0; i < heads.length; i++) {
        heads[i].setInvBasdoc(invVOs[i]);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    PrintEntry print = new PrintEntry(null, null);
    print.setTemplateID(this.tp.getUnitCode(), "50081020", this.tp.getUser().getPrimaryKey(), null);
    print.setDataSource(dataSource);
    if (print.selectTemplate() <= 0)
    {
      return;
    }

    print.preview();
    updatePrintedFlag();
    Thread thread1 = new Thread()
    {
      public void run()
      {
        try
        {
          MMProxy.getRemoteMO().updatePrintedFlag(sMOPKs);
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    };
    thread1.setPriority(1);
    thread1.start();
  }

  public void onQuery()
  {
    getQueryCondition().showModal();
    if (getQueryCondition().isCloseOK())
      try
      {
        setData(null, null);
        this.m_QuerySQL = getQueryCondition().getSQL();
        MoHeaderVO[] heads = query();

        setFinishedNum(heads);

        FreeItemUIUtilies.setFreeItemVOForCirVO(heads);
        setData(heads, null);

        setState(1);
        this.tp.showHintMessage(getstrbyid("UPP50081020-000077"));
        getViewPanel().getListPanel().getBodyBillModel().reCalcurateAll();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        this.tp.showWarningMessage(ex.getLocalizedMessage());
      }
     
  }

  private void setFinishedNum(MoHeaderVO[] heads)
  {
    if (heads != null)
    {
      ArrayList moPks = null;
      try {
        moPks = CommonUtil.getPKList(heads);
      }
      catch (BusinessException e) {
        e.printStackTrace();
        return;
      }
      HashMap newMap = getFinishedInfoByMoPks(moPks);

      UFDouble wgfsl = new UFDouble();
      UFDouble wgzsl = new UFDouble();

      UFDouble trzsl = new UFDouble();
      UFDouble trfsl = new UFDouble();
      for (int i = 0; i < heads.length; i++) {
        HashMap map = (HashMap)newMap.get(heads[i].getPrimaryKey());
        if (map == null)
          continue;
        wgzsl = CommonUtil.getUFDoubleByKey(map, "产出主数量");
        wgfsl = CommonUtil.getUFDoubleByKey(map, "产出辅数量");
        trzsl = CommonUtil.getUFDoubleByKey(map, "投入主数量");
        trfsl = CommonUtil.getUFDoubleByKey(map, "投入辅数量");

        heads[i].setSjwgsl(wgzsl);
        heads[i].setRksl(wgzsl);
        heads[i].setFwcsl(wgfsl);
        heads[i].setFrksl(wgfsl);

        heads[i].setZdy3(trzsl.toString());
        heads[i].setZdy4(trfsl.toString());
      }
    }
  }

  private HashMap<String, HashMap> getFinishedInfoByMoPks(ArrayList<String> pkList)
  {
    if (pkList == null) return null;

    HashMap retMap = new HashMap();

    String pk_corp = Util.getCorp();
    for (int i = 0; i < pkList.size(); i++) {
      String currentPks = (String)pkList.get(i);

      ArrayList jqList = PublicUtil.getMapList("select m.orderid orderid, sum(s.outnum) 投入主数量, sum(s.toutnum) 投入辅数量, sum(o.ninnum) 产出主数量, sum(o.ninassnum) 产出辅数量 from baoyin_process_main m, baoyin_process_source s, baoyin_view_in_onhand o where nvl(m.dr, 0) = 0 and nvl(s.dr, 0) = 0 and m.pk_corp='" + pk_corp + "' and s.pk_process_main = m.pk_process_main and m.def05='JQ' and o.orderid = m.orderid and o.vbatchcode = s.packageid and o.cinvbasid = m.def01 and  m.orderid in (" + currentPks + ") group by m.orderid ");

      ArrayList otherOutList = PublicUtil.getMapList("select h.pk_defdoc20 orderid,sum(b.ninnum) 产出主数量,sum(b.ninassistnum) 产出辅数量 from ic_general_h h,ic_general_b b where h.cgeneralhid = b.cgeneralhid and h.pk_corp='" + pk_corp + "' and nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.cbilltypecode='46' and h.pk_defdoc20 in (" + currentPks + ") group by h.pk_defdoc20 ");

      ArrayList otherInList = PublicUtil.getMapList("select h.prodorder orderid, sum(o.noutnum) 投入主数量, sum(o.noutassistnum) 投入辅数量 from baoyin_packageref h, baoyin_packageref_st b, baoyin_view_out_onhand o where h.pk_packageref = b.pk_packageref and h.prodorder = o.orderid and h.pk_corp='" + pk_corp + "' and b.packnum = o.vbatchcode and h.prodorder in (" + currentPks + ") and nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0 group by h.prodorder");

      HashMap jqMap = CommonUtil.listToMap(jqList, "orderid");
      HashMap otherOutMap = CommonUtil.listToMap(otherOutList, "orderid");
      HashMap otherInMap = CommonUtil.listToMap(otherInList, "orderid");
      CommonUtil.mergeHashMap(retMap, jqMap);
      CommonUtil.mergeHashMap(retMap, otherOutMap);
      CommonUtil.mergeHashMap(retMap, otherInMap);
    }
    return retMap;
  }

  public void onRevisemo()
    throws BusinessException
  {
    this.tp.showHintMessage("");
    MoVO vo = getViewPanel().getSelectedVO();
    if (vo == null)
      return;
    if ((!vo.getHeadVO().getZt().equals("A")) && (!vo.getHeadVO().getZt().equalsIgnoreCase("B")))
    {
      this.tp.showWarningMessage(getstrbyid("UPP50081020-000280"));
      return;
    }
    boolean lock = lock();
    if (!lock)
    {
      this.tp.showWarningMessage(getstrbyid("UPP50081020-000051"));
      return;
    }
    try
    {
      setState(7);
      getEditPanel().setState(7);
      int stat = getEditPanel().setModifyData(vo);
      getEditPanel().setState(7);
      setState(stat);
      this.tp.showHintMessage(getstrbyid("UPP50081020-000082"));
    }
    catch (Exception ex)
    {
      try
      {
        free();
      } catch (Exception localException1) {
      }
      ex.printStackTrace();
      this.tp.showWarningMessage(getstrbyid("UPP50081020-000074") + ex.getMessage());
    }
  }

  public void onQueryrevisemo()
  {
    if (!isNull(getViewPanel().getSelectedVO().getHeadVO().getPk_moid()))
    {
      ReeditViewDialog dlg = new ReeditViewDialog(this.tp, getstrbyid("UPP50081020-000281"), "A2", "mm_mo");
      dlg.setBillKey(getViewPanel().getSelectedVO().getHeadVO().getPk_moid());
      dlg.setPrint_node_code("50081020");
      dlg.setCurrentBill(getViewPanel().getSelectedVO());
      dlg.show();
    }
  }

  public void onRefresh()
  {
    this.tp.showHintMessage("");
    if (isNull(this.m_QuerySQL))
      return;
    try
    {
      MoHeaderVO[] heads = query();
      FreeItemUIUtilies.setFreeItemVOForCirVO(heads);
      getViewPanel().setData(heads);
      setState(1);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      this.tp.showWarningMessage(ex.getLocalizedMessage());
    }
  }

  public void onReturn()
  {
    getSimPanel().clearData();
    this.tp.showHintMessage(getstrbyid("UPP50081020-000078"));
    setState(1);
  }

  public void onSimPut()
  {
    if (!this.m_withSim)
    {
      add(getSimPanel(), getSimPanel().getName());
      this.m_withSim = true;
    }
    int[] rows = getViewPanel().getSelectedRows();
    MoVO[] mvos = getViewPanel().getSelectedVOs();
    StringBuffer sb = new StringBuffer("");
    for (int i = 0; i < mvos.length; i++)
    {
      MoHeaderVO head = (MoHeaderVO)mvos[i].getParentVO();
      if ((!isNull(head.getZt())) && (!head.getZt().equals("A")))
      {
        sb.append(NCLangRes.getInstance()
          .getStrByID("50081020", "UPP50081020-000080", null, new String[] { 
          rows[i] + 1 + "" }));

        sb.append(head.getZtShow());
        sb.append("!");
        this.tp.showWarningMessage(sb.toString());
        return;
      }
      head.setBusiDate(new UFDate(this.tp.getLogDate()));
    }

    this.tp.showHintMessage(getstrbyid("UPP50081020-000081"));
    String errmsg = getSimPanel().onSimput(mvos);
    if (errmsg != null)
    {
      this.tp.showWarningMessage(errmsg);
      return;
    }
    try
    {
      this.layout.show(this, getSimPanel().getName());
      setState(6);
    }
    catch (Exception ex)
    {
      this.tp.showWarningMessage(ex.getLocalizedMessage());
      ex.printStackTrace();
      this.tp.showHintMessage("");
    }
  }

  public void onSwitch()
  {
    if (this.iuistat == 1)
    {
      MoVO vo = getViewPanel().getSelectedVO();
      if (vo != null)
      {
        getEditPanel().setModifyData(vo);
        getEditPanel().setEnabled(false);
        setState(2);
      }
    }
    else {
      setState(1);
    }
  }

  public void onUnFinish()
  {
    getViewPanel().onUnFinish();
  }

  public void onUnOver()
  {
    getViewPanel().onUnOver();
  }

  public void setState(int stat)
  {
    this.iuistat = stat;
    this.tp.setState(stat, "");
    switch (stat)
    {
    case 0:
    case 1:
      this.layout.show(this, getViewPanel().getName());
      break;
    case 2:
    case 3:
    case 4:
    case 5:
    case 7:
      this.layout.show(this, getEditPanel().getName());
      getEditPanel().grabFocus();
      break;
    case 6:
      this.layout.show(this, getSimPanel().getName());
    }
  }

  public void onViewSource()
  {
    getViewPanel().viewSource();
  }

  private MoHeaderVO[] query()
    throws Exception
  {
    MoHeaderVO[] heads = MMProxy.getRemoteMO().queryMoByWhere(this.m_QuerySQL);
    getEditPanel().backupTsxValue(heads);
    return heads;
  }

  public void setData(MoHeaderVO[] headers, PoVO[] pvos)
  {
    getViewPanel().setData(headers);
    getEditPanel().setBaseplanorder(pvos);
  }

  public void onCopy()
  {
    this.tp.showHintMessage("");
    MoVO vo = getViewPanel().getSelectedVO();
    if (vo == null)
      return;
    try
    {
      vo.getHeadVO().setLylx(null);
      vo.getHeadVO().setLylxShow(getstrbyid("UPP50081020-000062"));
      vo.getHeadVO().setLyid(null);
      vo.getHeadVO().setLydjh(null);
      vo.getHeadVO().setScddh(null);
      vo.getHeadVO().setJhwgsl(null);
      vo.getHeadVO().setFwcsl(null);
      vo.getHeadVO().setRksl(null);
      vo.getHeadVO().setFrksl(null);
      vo.getHeadVO().setSjkgrq(null);
      vo.getHeadVO().setSjkssj(null);
      vo.getHeadVO().setSjwgrq(null);
      vo.getHeadVO().setSjjssj(null);
      vo.getHeadVO().setYjwgrq(null);
      vo.getHeadVO().setPch(null);
      getEditPanel().setNewData(vo);
      FreeItemUIUtilies.buildFreeForHeader(getEditPanel().getCardPanel(), vo.getParentVO());
      setState(3);
      this.tp.showHintMessage(getstrbyid("UPP50081020-000082"));
    }
    catch (Exception ex)
    {
      try
      {
        free();
      } catch (Exception localException1) {
      }
      ex.printStackTrace();
      this.tp.showWarningMessage(getstrbyid("UPP50081020-000074") + ex.getMessage());
    }
  }

  public void onJoinQuery()
  {
    String pkey = getViewPanel().getSelectedPK();
    SourceBillFlowDlg dlg = new SourceBillFlowDlg(this);
    dlg.setBillType("A2");
    dlg.setBillID(pkey);
    dlg.setBillRowID(null);
    dlg.setBizType(null);
    dlg.showModal();
  }

  public void onViewAtp()
  {
    if (this.m_iohdDlg == null)
      this.m_iohdDlg = new InvOnHandDialog(this);
    MoVO vo = getViewPanel().getSelectedVO();
    if (vo == null)
      return;
    try
    {
      String WhID = "";
      String InvID = vo.getHeadVO().getPk_produce();
      InvID = MMProxy.getRemoteMO().getManIDFromProID(InvID);
      ((InvOnHandDialog)this.m_iohdDlg).setParam(WhID, InvID);
      this.m_iohdDlg.showModal();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void updatePrintedFlag()
  {
    if (this.iuistat != 1)
      getEditPanel().updatePrintedFlag();
    getViewPanel().updatePrintedFlag(getViewPanel().getSelectedRows());
  }
}