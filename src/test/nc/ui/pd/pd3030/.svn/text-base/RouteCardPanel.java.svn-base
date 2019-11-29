package nc.ui.pd.pd3030;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.ml.NCLangRes;
import nc.ui.mm.pub.MMToftPanel;
import nc.ui.mm.pub.UIRule;
import nc.ui.mm.pub.pub1010.RcRefModel;
import nc.ui.mm.pub.pub1010.RgRefModel;
import nc.ui.mm.pub.pub1010.WkRefModel;
import nc.ui.mm.pub.pub1025.DataDictionaryReader;
import nc.ui.mm.pub.pub1040.FreeItemUIUtilies;
import nc.ui.pd.pd3020.RtTmpData;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.vo.bd.fd.DdVO;
import nc.vo.mm.materialserver.BatchUtil;
import nc.vo.mm.pub.FunctionBase;
import nc.vo.mm.pub.pub1020.ProduceCoreVO;
import nc.vo.mm.pub.pub1020.RtBAtItemVO;
import nc.vo.mm.pub.pub1020.RtBItemVO;
import nc.vo.mm.pub.pub1020.RtHeaderVO;
import nc.vo.mm.pub.pub1020.RtItemVO;
import nc.vo.mm.pub.pub1020.RtVO;
import nc.vo.pd.pd3030.InitVO;
import nc.vo.pd.pd3030.RtBAtVO;
import nc.vo.pd.pd3030.RtBVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

public class RouteCardPanel extends UIPanel
  implements ActionListener, BillEditListener, BillEditListener2, BillBodyMenuListener
{
  private MMToftPanel m_toft;
  private BillCardPanel ivjCardPanel = null;
  private int m_nState;
  private RtData m_rtData;
  private RgRefModel m_refRg = null;

  private RcRefModel m_refRc = null;

  private WkRefModel m_refWk = null;
  private int m_nEditRow;
  private int m_nMaxLineNumber;

  public RouteCardPanel()
  {
    initialize();
  }

  public RouteCardPanel(MMToftPanel toft)
  {
    this.m_toft = toft;
    initialize();
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == getItemCombox("header", "BBLX")) {
      if (getItemCombox("header", "BBLX").getSelectedIndex() == 0) {
        getItemCheckBox("header", "sfmr").setEnabled(true);
        getItemCheckBox("header", "sfqs").setEnabled(true);
      }
      else {
        getItemCheckBox("header", "sfmr").setEnabled(false);
        getItemCheckBox("header", "sfqs").setEnabled(false);
        getCardPanel().setHeadItem("sfmr", new UFBoolean(false));
        getCardPanel().setHeadItem("sfqs", new UFBoolean(false));
      }
    }
    else if (e.getSource() == getItemCheckBox("header", "sfmr")) {
      if (this.m_rtData.getMaterialInfo().getGylxlx().intValue() == RtVO.TYPE_STANDARD) {
        getItemCheckBox("header", "sfqs").setSelected(getItemCheckBox("header", "sfmr").isSelected());
      }
    }
    else if (e.getSource() == getItemCheckBox("header", "sfqs")) {
      if (this.m_rtData.getMaterialInfo().getGylxlx().intValue() == RtVO.TYPE_STANDARD) {
        getItemCheckBox("header", "sfmr").setSelected(getItemCheckBox("header", "sfqs").isSelected());
      }
    }
    else if (e.getSource() == getItemCombox("header", "gdsl")) {
      UIComboBox cbb = getItemCombox("header", "fgdsl");
      cbb.removeActionListener(this);
      try {
        cbb.setSelectedIndex(getItemCombox("header", "gdsl").getSelectedIndex());
      } catch (Exception exc) {
      }
      cbb.addActionListener(this);
    }
    else if (e.getSource() == getItemCombox("header", "fgdsl")) {
      UIComboBox cbb = getItemCombox("header", "gdsl");
      cbb.removeActionListener(this);
      try {
        cbb.setSelectedIndex(getItemCombox("header", "fgdsl").getSelectedIndex());
      } catch (Exception exc) {
      }
      cbb.addActionListener(this);
    }
  }

  private void addChildrenForItem(RtItemVO rtItem)
  {
    String key = rtItem.getGxh();
    Hashtable hlCache = this.m_rtData.getHlCache();
    Hashtable atCache = this.m_rtData.getAtCache();

    if ((key != null) && (hlCache.containsKey(key))) {
      RtBVO vo = (RtBVO)hlCache.get(key);
      rtItem.setHlChanged(vo.isVoChanged());
      rtItem.setHls((RtBItemVO[])(RtBItemVO[])vo.getChildrenVO());
    }

    if ((key != null) && (atCache.containsKey(key))) {
      RtBAtVO vo = (RtBAtVO)atCache.get(key);
      rtItem.setAtChanged(vo.isVoChanged());
      rtItem.setAts((RtBAtItemVO[])(RtBAtItemVO[])vo.getChildrenVO());
    }
  }

  public boolean addRoute(UIRefPane rfpMaterial)
  {
    if (rfpMaterial.getRefPK() == null) {
      this.m_toft.showHintMessage(getStrByID("UPP-000016"));
      return false;
    }

    clearUI();

    RtHeaderVO header = getDefaultRtHeader(rfpMaterial);
    RtVO rt = new RtVO();
    rt.setParentVO(header);
    setRouteVO(rt);

    return true;
  }

  public void afterEdit(BillEditEvent e)
  {
    this.m_nEditRow = e.getRow();
    if (e.getPos() == 0) {
      if (e.getKey().equals("fzbm")) {
        rgChanged();
      }
    }
    else if (e.getPos() == 1)
      if (e.getKey().equals("rc")) {
        rcChanged();
      }
      else if (e.getKey().equals("gzzxbm")) {
        UIRefPane urp = getItem("body", "gzzxbm");
        getCardPanel().setBodyValueAt(urp.getRefPK(), e.getRow(), "gzzxbmid");
        getCardPanel().setBodyValueAt(urp.getRefName(), e.getRow(), "gzzxmc");
      }
      else if (e.getKey().equals("gxh"))
      {
        if ((e.getOldValue() != null) && (e.getValue() != null)) {
          RtBVO hlvo = (RtBVO)this.m_rtData.getHlCache().get(e.getOldValue());
          if (hlvo != null) {
            this.m_rtData.getHlCache().put(e.getValue(), hlvo);
          }
          RtBAtVO atvo = (RtBAtVO)this.m_rtData.getAtCache().get(e.getOldValue());
          if (atvo != null)
            this.m_rtData.getAtCache().put(e.getValue(), atvo);
        }
      }
  }

  public boolean beforeEdit(BillEditEvent e)
  {
    return true;
  }

  public void bodyRowChange(BillEditEvent e)
  {
    this.m_nEditRow = e.getRow();
  }

  private void clearUI()
  {
    getCardPanel().getBillData().setHeaderValueVO(new RtHeaderVO());

    setBodyData(null);

    this.m_rtData.reloadHlCache(null);
    this.m_rtData.reloadAtCache(null);
  }

  public RtItemVO getBodySelectedVO()
  {
    int count = getCardPanel().getBillTable().getRowCount();
    int row = getCardPanel().getBillTable().getSelectedRow();
    if ((row < 0) || (row >= count)) {
      MessageDialog.showHintDlg(this, null, getStrByID("UPP-000017"));
      return null;
    }

    return (RtItemVO)getCardPanel().getBillModel().getBodyValueRowVO(row, RtItemVO.class.getName());
  }

  public BillCardPanel getCardPanel()
  {
    if (this.ivjCardPanel == null)
    {
      try
      {
        this.ivjCardPanel = new BillCardPanel();
        this.ivjCardPanel.setName("CardPanel");

        this.ivjCardPanel.setPreferredSize(new Dimension(774, 419));
        this.ivjCardPanel.loadTemplet("10093030", null, this.m_toft.getUser().getPrimaryKey(), this.m_toft.getUnitCode());
      }
      catch (Throwable ivjExc)
      {
        handleException(ivjExc);
      }
    }
    return this.ivjCardPanel;
  }

  private RtHeaderVO getDefaultRtHeader(UIRefPane urpWl)
  {
    RtHeaderVO vo = new RtHeaderVO();

    RtData.copyProduceInfo(this.m_rtData.getMaterialInfo(), vo);

    vo.setPk_produce(urpWl.getRefPK());
    String wlbmid = (String)urpWl.getRefValue("bd_produce.pk_invbasdoc");
    vo.setWlbmid(wlbmid);
    vo.setWlbm(urpWl.getRefCode());
    vo.setWlmc(urpWl.getRefName());
    vo.setInvspec((String)urpWl.getRefValue("bd_invbasdoc.invspec"));
    vo.setInvtype((String)urpWl.getRefValue("bd_invbasdoc.invtype"));
    vo.setTh((String)urpWl.getRefValue("bd_invbasdoc.graphid"));

    vo.setSjdw(this.m_rtData.getInitData().getSjdw());
    vo.setSl(new UFDouble(1));
    ProduceCoreVO fjldw = this.m_rtData.getFjldw(wlbmid);
    if (fjldw != null) {
      vo.setFjldwid(fjldw.getFjldwid());
      vo.setFjldwmc(fjldw.getFjldwmc());
      vo.setJldwmc(fjldw.getMeasname());
      vo.setHsl(fjldw.getMainmeasrate());
      UFDouble[] srcSl = { null, vo.getSl(), vo.getHsl() };
      boolean fixedflag = fjldw.getFixedflag() == null ? false : fjldw.getFixedflag().booleanValue();
      UFDouble[] dstSl = BatchUtil.getUFDoubleByConversion(srcSl, 0, fixedflag);
      vo.setFsl(dstSl[0]);
    }
    vo.setBblx(new Integer(0));
    vo.setSfmr(new UFBoolean(false));
    vo.setSfqs(new UFBoolean(false));
    if (this.m_rtData.getInitData().getEcn().equals("Y")) {
      vo.setBblx(new Integer(1));
    }
    else if (this.m_rtData.getRtCache().isEmpty()) {
      vo.setSfqs(new UFBoolean(true));
      vo.setSfmr(new UFBoolean(true));
    }
    vo.setBBLX(FunctionBase.getQzsm(vo.getBblx(), this.m_rtData.getInitData().getBblxVO()));
    vo.setGYLXLX(FunctionBase.getQzsm(vo.getGylxlx(), this.m_rtData.getInitData().getGylxlxVO()));
    vo.setVersion(this.m_rtData.getNewVersion());
    try
    {
      FreeItemUIUtilies.setFreeItemVOForCirVO(new RtHeaderVO[] { vo });
    } catch (Exception e) {
      e.printStackTrace();
    }

    return vo;
  }

  public UIRefPane getItem(String flag, String itemName) {
    if (flag.equals("header"))
      return (UIRefPane)getCardPanel().getHeadItem(itemName).getComponent();
    if (flag.equals("body")) {
      return (UIRefPane)getCardPanel().getBodyItem(itemName).getComponent();
    }
    return null;
  }

  public UICheckBox getItemCheckBox(String flag, String itemName) {
    if (flag.equals("header"))
      return (UICheckBox)getCardPanel().getHeadItem(itemName).getComponent();
    if (flag.equals("body")) {
      return (UICheckBox)getCardPanel().getBodyItem(itemName).getComponent();
    }
    return null;
  }

  public UIComboBox getItemCombox(String flag, String itemName) {
    if (flag.equals("header"))
      return (UIComboBox)getCardPanel().getHeadItem(itemName).getComponent();
    if (flag.equals("body")) {
      return (UIComboBox)getCardPanel().getBodyItem(itemName).getComponent();
    }
    return null;
  }

  private RcRefModel getRefRcModel()
  {
    if (this.m_refRc == null) {
      this.m_refRc = new RcRefModel();
      this.m_refRc.addWherePart(" and pk_corp='" + this.m_toft.getUnitCode() + "' and gcbm='" + this.m_toft.getFactoryCode() + "'");
    }

    return this.m_refRc;
  }

  private RgRefModel getRefRgModel()
  {
    if (this.m_refRg == null) {
      this.m_refRg = new RgRefModel();
      this.m_refRg.addWherePart(" and pk_corp='" + this.m_toft.getUnitCode() + "' and gcbm='" + this.m_toft.getFactoryCode() + "'");
    }

    return this.m_refRg;
  }

  private WkRefModel getRefWkModel()
  {
    if (this.m_refWk == null) {
      this.m_refWk = new WkRefModel();
      this.m_refWk.addWherePart(" and pd_wk.pk_corp='" + this.m_toft.getUnitCode() + "' and pd_wk.gcbm='" + this.m_toft.getFactoryCode() + "'");
    }

    return this.m_refWk;
  }

  public RtVO getRouteVO()
  {
    getCardPanel().stopEditing();
    RtVO rt = (RtVO)getCardPanel().getBillValueVO(RtVO.class.getName(), RtHeaderVO.class.getName(), RtItemVO.class.getName());

    RtHeaderVO header = (RtHeaderVO)rt.getParentVO();

    UIRefPane urp = getItem("header", "wlbm");
    header.setPk_produce(urp.getRefPK());
    header.setWlbm(urp.getRefCode());
    header.setWlmc(urp.getRefName());
    String wlbmid = (String)urp.getRefValue("bd_produce.pk_invbasdoc");
    header.setWlbmid(wlbmid);
    ProduceCoreVO fjldw = this.m_rtData.getFjldw(wlbmid);
    if (fjldw != null) {
      header.setFjldwid(fjldw.getFjldwid());
      header.setFjldwmc(fjldw.getFjldwmc());
    }
    else {
      header.setFjldwid(null);
      header.setFjldwmc(null);
    }
    header.setPk_corp(this.m_toft.getUnitCode());
    header.setGcbm(this.m_toft.getFactoryCode());
    header.setBblx(FunctionBase.getIntegerValue(header.getBBLX(), this.m_rtData.getInitData().getBblxVO()));
    header.setGylxlx(FunctionBase.getIntegerValue(header.getGYLXLX(), this.m_rtData.getInitData().getGylxlxVO()));
    header.setRtType(header.getGylxlx());
    header.setZt("A");
    header.setFzbm(getItem("header", "fzbm").getRefCode());

    RtItemVO[] items = (RtItemVO[])(RtItemVO[])rt.getChildrenVO();
    items = (RtItemVO[])(RtItemVO[])UIRule.delNullVos(items, RtItemVO.class.getName(), new String[] { "gzzxbm" });

    for (int i = 0; i < items.length; i++) {
      items[i].setPk_corp(this.m_toft.getUnitCode());
      items[i].setGcbm(this.m_toft.getFactoryCode());

      items[i].setKzbz(this.m_rtData.getRtBDDReader().getNumberByQzsm("kzbz", items[i].getKZBZ()));
      items[i].setCheckmode(this.m_rtData.getRtBDDReader().getNumberByQzsm("checkmode", items[i].getCheckmode_show()));
      items[i].setSfwx(new UFBoolean(false));
      items[i].setSfjc(new UFBoolean(false));

      addChildrenForItem(items[i]);
    }
    return rt;
  }

  private void handleException(Throwable exception)
  {
    System.out.println("--------- Î´²¶×½µ½µÄÒì³£ ---------");
    exception.printStackTrace(System.out);
  }

  private void initComboBox(String flag, String key, DdVO[] items)
  {
    UIComboBox cbb = getItemCombox(flag, key);
    for (int i = 0; i < items.length; i++)
      cbb.addItem(items[i].getQzsm());
  }

  private void initFormat()
  {
    BillData bd = getCardPanel().getBillData();
    double MIN_VALUE = 1.0E-008D;
    double MAX_VALUE = 9999999999.9999981D;

    bd.getHeadItem("sl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getHeadItem("sl").getComponent()).setMinValue(MIN_VALUE);
    ((UIRefPane)bd.getHeadItem("sl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getHeadItem("fsl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getHeadItem("fsl").getComponent()).setMinValue(MIN_VALUE);
    ((UIRefPane)bd.getHeadItem("fsl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getHeadItem("ssl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getHeadItem("ssl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getHeadItem("ssl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getHeadItem("fssl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getHeadItem("fssl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getHeadItem("fssl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getHeadItem("esl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getHeadItem("esl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getHeadItem("esl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getHeadItem("fesl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getHeadItem("fesl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getHeadItem("fesl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getHeadItem("hsl").setDecimalDigits(this.m_toft.getScaleFactor());
    ((UIRefPane)bd.getHeadItem("hsl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getHeadItem("hsl").getComponent()).setMaxValue(999.99990000000003D);

    bd.getBodyItem("ddsj").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getBodyItem("ddsj").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getBodyItem("ddsj").getComponent()).setMaxValue(MAX_VALUE);

    bd.getBodyItem("zbsj").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getBodyItem("zbsj").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getBodyItem("zbsj").getComponent()).setMaxValue(MAX_VALUE);

    bd.getBodyItem("jgsj").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getBodyItem("jgsj").getComponent()).setMinValue(MIN_VALUE);
    ((UIRefPane)bd.getBodyItem("jgsj").getComponent()).setMaxValue(MAX_VALUE);

    bd.getBodyItem("cxsj").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getBodyItem("cxsj").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getBodyItem("cxsj").getComponent()).setMaxValue(MAX_VALUE);

    bd.getBodyItem("cssj").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getBodyItem("cssj").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getBodyItem("cssj").getComponent()).setMaxValue(MAX_VALUE);

    bd.getBodyItem("gdzqpl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getBodyItem("gdzqpl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getBodyItem("gdzqpl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getBodyItem("fgdzqpl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getBodyItem("fgdzqpl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getBodyItem("fgdzqpl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getBodyItem("zxcssl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getBodyItem("zxcssl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getBodyItem("zxcssl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getBodyItem("fzxcssl").setDecimalDigits(this.m_toft.getScaleNum());
    ((UIRefPane)bd.getBodyItem("fzxcssl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getBodyItem("fzxcssl").getComponent()).setMaxValue(MAX_VALUE);

    bd.getBodyItem("cdl").setDecimalDigits(this.m_toft.getScaleFactor());
    ((UIRefPane)bd.getBodyItem("cdl").getComponent()).setMinValue(0.0D);
    ((UIRefPane)bd.getBodyItem("cdl").getComponent()).setMaxValue(999.99990000000003D);
  }

  private void initialize()
  {
    try
    {
      setName("RouteCardPanel");
      setLayout(new BorderLayout());
      setSize(774, 419);
      add(getCardPanel(), "Center");
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
  }

  public boolean onAddLine()
  {
    if (this.m_nState == 1) {
      return false;
    }
    getCardPanel().addLine();
    sendDefaultToBody(getCardPanel().getBillTable().getRowCount() - 1);
    return true;
  }

  public boolean onDeleteLine()
  {
    if (this.m_nState == 1) {
      return false;
    }
    int[] rows = getCardPanel().getBillTable().getSelectedRows();
    if ((null == rows) || (rows.length == 0))
      return false;
    int id = this.m_toft.showYesNoMessage(getStrByID("UPP-000018"));
    if (id != 4) {
      return false;
    }
    getCardPanel().getBodyPanel().transferFocus();
    getCardPanel().stopEditing();
    getCardPanel().delLine();
    refreshMaxLineNumber();

    return true;
  }

  public void postInit()
  {
    initFormat();

    UIRefPane urp = getItem("header", "fzbm");
    urp.setIsCustomDefined(true);
    urp.getRef().setRefModel(getRefRgModel());
    urp.setReturnCode(true);

    urp = getItem("body", "rc");
    urp.setRefModel(getRefRcModel());
    urp.setIsCustomDefined(true);

    urp = getItem("body", "gzzxbm");
    urp.setIsCustomDefined(true);
    urp.getRef().setRefModel(getRefWkModel());

    urp = getItem("header", "wlbm");
    urp.setReturnCode(true);
    String wlWherePart = " and bd_produce.pk_calbody = '" + this.m_toft.getFactoryCode() + "' and ((matertype = 'MR' and (outtype = 'OB' or outtype = 'OD')) or matertype = 'PR' or virtualflag = 'Y')";

    if (this.m_rtData.getInitData().getSfjhwlkz().equals("Y")) {
      wlWherePart = wlWherePart + " and exists(select jhyid from pd_jhwl where jhyid = '" + this.m_toft.getUser().getPrimaryKey() + "' and jhlx = 5 and (sfwlgly = 'Y' or (pd_jhwl.pk_produce = bd_produce.pk_produce)))";
    }

    urp.getRefModel().addWherePart(wlWherePart);

    getItemCombox("header", "BBLX").addActionListener(this);
    getItemCombox("header", "gdsl").addActionListener(this);
    getItemCombox("header", "fgdsl").addActionListener(this);
    getItemCheckBox("header", "sfmr").addActionListener(this);
    getItemCheckBox("header", "sfqs").addActionListener(this);
    getCardPanel().addBodyEditListener2(this);
    getCardPanel().addEditListener(this);
    getCardPanel().removeBodyMenuListener();
    getCardPanel().addBodyMenuListener(this);

    InitVO ivo = this.m_rtData.getInitData();
    initComboBox("header", "BBLX", ivo.getBblxVO());
    initComboBox("header", "GYLXLX", ivo.getGylxlxVO());
    initComboBoxData("body", "KZBZ", this.m_rtData.getRtBDDReader().getQzsm("kzbz"));
    initComboBoxData("body", "checkmode_show", this.m_rtData.getRtBDDReader().getQzsm("checkmode"));

    getItemCombox("header", "sjdw").addItem(ivo.getSjdw());
  }

  private void rcChanged()
  {
    UIRefPane urp = getItem("body", "rc");
    String pk = urp.getRefPK();
    getCardPanel().setBodyValueAt(pk, this.m_nEditRow, "pk_rcid");
    if (pk == null) {
      return;
    }
    Object gzzxid = urp.getRefValue("gzzxid");
    getItem("body", "gzzxbm").setPK(gzzxid);
    getCardPanel().setBodyValueAt(gzzxid, this.m_nEditRow, "gzzxbmid");
    String gzzxbm = getItem("body", "gzzxbm").getRefCode();
    getCardPanel().setBodyValueAt(gzzxbm, this.m_nEditRow, "gzzxbm");
    String gzzxmc = getItem("body", "gzzxbm").getRefName();
    getCardPanel().setBodyValueAt(gzzxmc, this.m_nEditRow, "gzzxmc");

    String name = urp.getRefName();
    getCardPanel().setBodyValueAt(name, this.m_nEditRow, "gyms");

    Object zbsj = urp.getRefValue("zbsj");
    getCardPanel().setBodyValueAt(zbsj, this.m_nEditRow, "zbsj");

    Object jgsj = urp.getRefValue("jgsj");
    getCardPanel().setBodyValueAt(jgsj, this.m_nEditRow, "jgsj");

    Object ddsj = urp.getRefValue("ddsj");
    getCardPanel().setBodyValueAt(ddsj, this.m_nEditRow, "ddsj");

    Object cssj = urp.getRefValue("cssj");
    getCardPanel().setBodyValueAt(cssj, this.m_nEditRow, "cssj");

    Object cxsj = urp.getRefValue("cxsj");
    getCardPanel().setBodyValueAt(cxsj, this.m_nEditRow, "cxsj");

    Object sfjsd = urp.getRefValue("sfjsd");
    getCardPanel().setBodyValueAt(sfjsd, this.m_nEditRow, "sfjsd");

    Object sfjcd = urp.getRefValue("sfjcd");
    getCardPanel().setBodyValueAt(sfjcd, this.m_nEditRow, "sfjcd");

    Object sfjjd = urp.getRefValue("sfjjd");
    getCardPanel().setBodyValueAt(sfjjd, this.m_nEditRow, "sfjjd");

    Object sfdc = urp.getRefValue("sfdc");
    getCardPanel().setBodyValueAt(sfdc, this.m_nEditRow, "sfdc");

    Object hbh = urp.getRefValue("hbh");
    getCardPanel().setBodyValueAt(hbh, this.m_nEditRow, "hbh");

    Object checkmode = urp.getRefValue("checkmode");
    getCardPanel().setBodyValueAt(checkmode, this.m_nEditRow, "checkmode");
    getCardPanel().setBodyValueAt(this.m_rtData.getRtBDDReader().getQzsmByNumber("checkmode", new Integer(checkmode.toString())), this.m_nEditRow, "checkmode_show");
  }

  private void rgChanged()
  {
    UIRefPane urp = getItem("header", "fzbm");
    String pk = urp.getRefPK();
    getCardPanel().setHeadItem("pk_rgid", pk);
    getCardPanel().setHeadItem("fzmc", urp.getRefName());
    if (pk == null) {
      return;
    }

    RtItemVO[] items = null;
    try {
      items = this.m_rtData.getRgData().getRtItems(pk);
    } catch (Exception e) {
      this.m_toft.reportException(e);
    }

    getCardPanel().getBillModel().setBodyDataVO(items);
    refreshMaxLineNumber();
  }

  private void sendDefaultToBody(int currentRow)
  {
    getCardPanel().setBodyValueAt(this.m_rtData.getRtBDDReader().getQzsmByNumber("kzbz", new Integer(1)), currentRow, "KZBZ");

    getCardPanel().setBodyValueAt(new Integer(1), currentRow, "kzbz");

    getCardPanel().setBodyValueAt(this.m_rtData.getRtBDDReader().getNumberByQzsm("checkmode", this.m_rtData.getRtBDDReader().getQzsm("checkmode")[0]), currentRow, "checkmode");
    getCardPanel().setBodyValueAt(this.m_rtData.getRtBDDReader().getQzsm("checkmode")[0], currentRow, "checkmode_show");

    if (currentRow == getCardPanel().getBillTable().getRowCount() - 1) {
      getCardPanel().setBodyValueAt(++this.m_nMaxLineNumber + "0", currentRow, "gxh");
    }
    getCardPanel().setBodyValueAt(this.m_toft.getLogDate(), currentRow, "sdate");
    getCardPanel().setBodyValueAt("2999-12-31", currentRow, "edate");
    String cpk = getCardPanel().getHeadItem("pk_rtid").getValue();
    getCardPanel().setBodyValueAt(cpk, currentRow, "pk_rtid");

    if (this.m_nMaxLineNumber == 1)
      getCardPanel().setBodyValueAt(new UFBoolean(true), currentRow, "sfzx");
  }

  public void setBodyData(RtItemVO[] rtItems)
  {
    getCardPanel().getBillModel().setBodyDataVO(rtItems);
    getCardPanel().updateValue();
  }

  private void setBodyEdit(int nState)
  {
    BillItem[] bi = getCardPanel().getBodyItems();

    switch (nState) {
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
      for (int i = 0; i < bi.length; i++)
        bi[i].setEdit(true);
      break;
    case 1:
      for (int i = 0; i < bi.length; i++) {
        bi[i].setEdit(false);
      }
    }

    if (nState != 1) {
      getCardPanel().getBodyItem("gzzxmc").setEdit(false);
      getCardPanel().getBodyItem("ts").setEdit(false);
    }
  }

  private void setHeadEdit(int nState)
  {
    BillItem[] items = getCardPanel().getHeadShowItems();
    int gylxlx = this.m_rtData.getMaterialInfo().getGylxlx().intValue();
    switch (nState) {
    case 2:
    case 4:
    case 6:
      for (int i = 0; i < items.length; i++) {
        String key = items[i].getKey();
        if ((key.equals("esl")) || (key.equals("ssl")) || (key.equals("fesl")) || (key.equals("fssl"))) {
          if (gylxlx == RtVO.TYPE_VARBATCH) {
            items[i].setEnabled(true);
            items[i].setEdit(true);
          }
          else {
            items[i].setEnabled(false);
            items[i].setEdit(false);
          }
        }
        else if ((key.equals("gdsl")) || (key.equals("fgdsl"))) {
          if (gylxlx == RtVO.TYPE_FIXBATCH) {
            items[i].setEnabled(true);
            items[i].setEdit(true);
          }
          else {
            items[i].setEnabled(false);
            items[i].setEdit(false);
          }
        }
        else if (!key.startsWith("freeitem")) {
          items[i].setEnabled(true);
          items[i].setEdit(true);
        }
      }
      break;
    case 3:
    case 5:
      for (int i = 0; i < items.length; i++) {
        String key = items[i].getKey();
        if ((key.equals("esl")) || (key.equals("ssl")) || (key.equals("fesl")) || (key.equals("fssl"))) {
          if (gylxlx == RtVO.TYPE_VARBATCH) {
            items[i].setEnabled(true);
            items[i].setEdit(true);
          }
          else {
            items[i].setEnabled(false);
            items[i].setEdit(false);
          }
        }
        else if ((key.equals("gdsl")) || (key.equals("fgdsl"))) {
          if (gylxlx == RtVO.TYPE_FIXBATCH) {
            items[i].setEnabled(true);
            items[i].setEdit(true);
          }
          else {
            items[i].setEnabled(false);
            items[i].setEdit(false);
          }
        }
        else if ((key.equals("BBLX")) || (key.equals("sfqs")) || (key.equals("sfmr"))) {
          items[i].setEnabled(false);
          items[i].setEdit(false);
        }
        else if (!key.startsWith("freeitem")) {
          items[i].setEnabled(true);
          items[i].setEdit(true);
        }
      }
      break;
    case 1:
      for (int i = 0; i < items.length; i++) {
        items[i].setEnabled(false);
        items[i].setEdit(false);
      }

    }

    if (nState != 1) {
      getCardPanel().getHeadItem("wlbm").setEnabled(false);
      getCardPanel().getHeadItem("wlmc").setEnabled(false);
      getCardPanel().getHeadItem("invspec").setEnabled(false);
      getCardPanel().getHeadItem("invtype").setEnabled(false);
      getCardPanel().getHeadItem("th").setEnabled(false);
      getCardPanel().getHeadItem("zjldwmc").setEnabled(false);
      getCardPanel().getHeadItem("fjldwmc").setEnabled(false);
      getCardPanel().getHeadItem("hsl").setEnabled(false);
      getCardPanel().getHeadItem("fzmc").setEnabled(false);
      getCardPanel().getHeadItem("GYLXLX").setEnabled(false);
      getCardPanel().getHeadItem("ts").setEnabled(false);
      try {
        getCardPanel().getHeadItem("creator").setEnabled(false);
        getCardPanel().getHeadItem("creatorName").setEnabled(false);
      }
      catch (Exception e)
      {
      }
    }
    if ((nState == 4) || (nState == 5)) {
      getItem("header", "version").setEnabled(false);
      getItem("header", "judgecode").setEnabled(false);
    }
    else if ((nState == 2) || (nState == 6) || (nState == 3)) {
      getItem("header", "version").setEnabled(true);
      getItem("header", "judgecode").setEnabled(true);
    }
  }

  public void setRouteVO(RtVO rt)
  {
    getItemCombox("header", "BBLX").removeActionListener(this);

    this.m_nMaxLineNumber = 0;
    int hh = 0;
    RtItemVO[] items = (RtItemVO[])(RtItemVO[])rt.getChildrenVO();
    if (items != null) {
      for (int i = 0; i < items.length; i++) {
        try {
          hh = Integer.parseInt(items[i].getGxh());
        } catch (Exception e) {
          continue;
        }
        if (hh > this.m_nMaxLineNumber * 10) {
          this.m_nMaxLineNumber = (hh / 10);
        }
      }
    }

    getCardPanel().setBillValueVO(rt);

    FreeItemUIUtilies.buildFreeForHeader(getCardPanel(), rt.getParentVO());
    getCardPanel().updateValue();

    RtHeaderVO header = (RtHeaderVO)rt.getParentVO();
    String pk_rtid = header.getPk_rtid();
    this.m_rtData.reloadHlCache(pk_rtid);
    this.m_rtData.reloadAtCache(pk_rtid);
    getItem("header", "fzbm").setPK(header.getPk_rgid());
    getItem("header", "fjldwmc").setPK(header.getFjldwid());
    getItem("header", "wlbm").setPK(header.getPk_produce());

    getItemCombox("header", "BBLX").addActionListener(this);

    UIComboBox cbb = getItemCombox("header", "gdsl");
    int count = cbb.getItemCount();
    if (count > 0) {
      UFDouble gdsl = header.getGdsl();
      if (gdsl != null)
        for (int i = 0; i < count; i++) {
          UFDouble temp = new UFDouble((String)cbb.getItemAt(i));
          if (temp.compareTo(gdsl) == 0) {
            cbb.setSelectedIndex(i);
            break;
          }
        }
    }
  }

  public void setRtData(RtData rtData)
  {
    this.m_rtData = rtData;
  }

  public void setState(int nState)
  {
    this.m_nState = nState;
    if ((nState == -1) || (nState == 0)) {
      return;
    }
    setHeadEdit(nState);
    setBodyEdit(nState);

    getCardPanel().updateUI();
  }

  public void wlbmChanged()
  {
    UIRefPane urp = getItem("header", "wlbm");

    getCardPanel().setHeadItem("wlmc", urp.getRefName());
    getCardPanel().setHeadItem("invspec", urp.getRefValue("bd_invbasdoc.invspec"));
    getCardPanel().setHeadItem("invtype", urp.getRefValue("bd_invbasdoc.invtype"));
    getCardPanel().setHeadItem("th", urp.getRefValue("bd_invbasdoc.graphid"));

    FreeItemUIUtilies.buildFreeForHeadInvChange(getCardPanel());

    String wlbmid = (String)urp.getRefValue("bd_produce.pk_invbasdoc");
    if ((wlbmid == null) || (wlbmid.trim().length() == 0)) {
      return;
    }
    ProduceCoreVO fjldw = this.m_rtData.getFjldw(wlbmid);
    if (fjldw != null) {
      getCardPanel().setHeadItem("fjldwid", fjldw.getFjldwid());
      getCardPanel().setHeadItem("fjldwmc", fjldw.getFjldwmc());
      getCardPanel().setHeadItem("zjldwmc", fjldw.getMeasname());
      getCardPanel().setHeadItem("hsl", fjldw.getMainmeasrate());
    }

    RtHeaderVO produceInfo = this.m_rtData.getMaterialInfo();

    getCardPanel().setHeadItem("gylxlx", produceInfo.getGylxlx());
    getCardPanel().setHeadItem("GYLXLX", FunctionBase.getQzsm(produceInfo.getGylxlx(), this.m_rtData.getInitData().getGylxlxVO()));

    updateHeaderSl(produceInfo.getGylxlx().intValue(), fjldw);
  }

  private UFDouble[] getSrcSl(String fslKey, String zslKey, int row)
  {
    UFDouble[] srcSl = new UFDouble[3];
    String sl = getCardPanel().getHeadItem("hsl").getValue();
    srcSl[2] = (isNull(sl) ? null : new UFDouble(sl));

    if (row < 0) {
      sl = getCardPanel().getHeadItem(fslKey).getValue();
      srcSl[0] = (isNull(sl) ? null : new UFDouble(sl));
      sl = getCardPanel().getHeadItem(zslKey).getValue();
      srcSl[1] = (isNull(sl) ? null : new UFDouble(sl));
    }
    else {
      srcSl[0] = ((UFDouble)getCardPanel().getBodyValueAt(row, fslKey));
      srcSl[1] = ((UFDouble)getCardPanel().getBodyValueAt(row, zslKey));
    }
    return srcSl;
  }

  private boolean isFjldwChanged(String fjldwid)
  {
    String originFjldw = getCardPanel().getHeadItem("fjldwid").getValue();

    return !originFjldw.equals(fjldwid);
  }

  private boolean isNull(String str)
  {
    return (str == null) || (str.trim().length() == 0);
  }

  private void processFjldwsl(String fslKey, String zslKey, int row, int alterFlag, boolean isFix)
  {
  }

  private void setDstSl(UFDouble[] dstSl, String fslKey, String zslKey, int row)
  {
    getCardPanel().getHeadItem("hsl").setValue(dstSl[2]);

    if (row < 0) {
      getCardPanel().getHeadItem(fslKey).setValue(dstSl[0]);
      getCardPanel().getHeadItem(zslKey).setValue(dstSl[1]);
    }
    else {
      getCardPanel().setBodyValueAt(dstSl[0], row, fslKey);
      getCardPanel().setBodyValueAt(dstSl[1], row, zslKey);
    }
  }

  private void updateHeaderSl(int lx, ProduceCoreVO fjldw)
  {
    if ((fjldw != null) && (fjldw.getFixedflag() != null)) {
      processFjldwsl("fsl", "sl", -1, -1, fjldw.getFixedflag().booleanValue());
    }
    if (lx == RtVO.TYPE_STANDARD) {
      getCardPanel().getHeadItem("esl").setValue(null);
      getCardPanel().getHeadItem("ssl").setValue(null);
      getCardPanel().getHeadItem("gdsl").setValue(null);

      getCardPanel().getHeadItem("fesl").setValue(null);
      getCardPanel().getHeadItem("fssl").setValue(null);
      getCardPanel().getHeadItem("fgdsl").setValue(null);

      getCardPanel().getHeadItem("esl").setEnabled(false);
      getCardPanel().getHeadItem("esl").setEdit(false);

      getCardPanel().getHeadItem("fesl").setEnabled(false);
      getCardPanel().getHeadItem("fesl").setEdit(false);

      getCardPanel().getHeadItem("ssl").setEnabled(false);
      getCardPanel().getHeadItem("ssl").setEdit(false);

      getCardPanel().getHeadItem("fssl").setEnabled(false);
      getCardPanel().getHeadItem("fssl").setEdit(false);

      getCardPanel().getHeadItem("gdsl").setEnabled(false);
      getCardPanel().getHeadItem("gdsl").setEdit(false);

      getCardPanel().getHeadItem("fgdsl").setEnabled(false);
      getCardPanel().getHeadItem("fgdsl").setEdit(false);
    }
    else if (lx == RtVO.TYPE_VARBATCH) {
      getCardPanel().getHeadItem("gdsl").setValue(null);
      getCardPanel().getHeadItem("fgdsl").setValue(null);

      getCardPanel().getHeadItem("esl").setEnabled(true);
      getCardPanel().getHeadItem("esl").setEdit(true);

      getCardPanel().getHeadItem("fesl").setEnabled(true);
      getCardPanel().getHeadItem("fesl").setEdit(true);

      getCardPanel().getHeadItem("ssl").setEnabled(true);
      getCardPanel().getHeadItem("ssl").setEdit(true);

      getCardPanel().getHeadItem("fssl").setEnabled(true);
      getCardPanel().getHeadItem("fssl").setEdit(true);

      getCardPanel().getHeadItem("gdsl").setEnabled(false);
      getCardPanel().getHeadItem("gdsl").setEdit(false);

      getCardPanel().getHeadItem("fgdsl").setEnabled(false);
      getCardPanel().getHeadItem("fgdsl").setEdit(false);

      processFjldwsl("fssl", "ssl", -1, -1, true);
      processFjldwsl("fesl", "esl", -1, -1, true);
    }
    else if (lx == RtVO.TYPE_FIXBATCH) {
      getCardPanel().getHeadItem("esl").setValue(null);
      getCardPanel().getHeadItem("ssl").setValue(null);

      getCardPanel().getHeadItem("fesl").setValue(null);
      getCardPanel().getHeadItem("fssl").setValue(null);

      getCardPanel().getHeadItem("esl").setEnabled(false);
      getCardPanel().getHeadItem("esl").setEdit(false);

      getCardPanel().getHeadItem("fesl").setEnabled(false);
      getCardPanel().getHeadItem("fesl").setEdit(false);

      getCardPanel().getHeadItem("ssl").setEnabled(false);
      getCardPanel().getHeadItem("ssl").setEdit(false);

      getCardPanel().getHeadItem("fssl").setEnabled(false);
      getCardPanel().getHeadItem("fssl").setEdit(false);

      getCardPanel().getHeadItem("gdsl").setEnabled(true);
      getCardPanel().getHeadItem("gdsl").setEdit(true);

      getCardPanel().getHeadItem("fgdsl").setEnabled(true);
      getCardPanel().getHeadItem("fgdsl").setEdit(true);
    }
  }

  public void updateVecGdsl(String wlbmid)
  {
    UIComboBox cbb = getItemCombox("header", "gdsl");
    UIComboBox cbb1 = getItemCombox("header", "fgdsl");
    cbb.removeAllItems();
    cbb1.removeAllItems();
    Vector vecGdsl = this.m_rtData.getMaterialInfo().getVecGdsl();

    if (vecGdsl != null) {
      ProduceCoreVO fjldw = this.m_rtData.getFjldw(wlbmid);
      UFDouble hsl = fjldw == null ? null : fjldw.getMainmeasrate();

      for (int i = 0; i < vecGdsl.size(); i++) {
        UFDouble sl = (UFDouble)vecGdsl.elementAt(i);
        String strSl = sl.setScale(this.m_toft.getScaleNum(), 4).toString();
        cbb.addItem(strSl);
      }
    }
  }

  public RtHeaderVO getRtHeaderVO()
  {
    RtHeaderVO header = (RtHeaderVO)getCardPanel().getBillData().getHeaderValueVO(RtHeaderVO.class.getName());

    UIRefPane urp = getItem("header", "wlbm");
    header.setPk_produce(urp.getRefPK());
    header.setWlbm(urp.getRefCode());
    header.setWlmc(urp.getRefName());
    String wlbmid = (String)urp.getRefValue("bd_produce.pk_invbasdoc");
    header.setWlbmid(wlbmid);
    header.setPk_corp(this.m_toft.getUnitCode());
    header.setGcbm(this.m_toft.getFactoryCode());
    header.setBblx(FunctionBase.getIntegerValue(header.getBBLX(), this.m_rtData.getInitData().getBblxVO()));
    header.setGylxlx(FunctionBase.getIntegerValue(header.getGYLXLX(), this.m_rtData.getInitData().getGylxlxVO()));
    header.setRtType(header.getGylxlx());
    header.setZt("A");
    header.setFzbm(getItem("header", "fzbm").getRefCode());

    return header;
  }

  public void onMenuItemClick(ActionEvent e)
  {
    if (e.getSource() == getCardPanel().getBodyPanel().getMiAddLine()) {
      onAddLine();
    }
    else if (e.getSource() == getCardPanel().getBodyPanel().getMiDelLine()) {
      onDeleteLine();
    }
    else if (e.getSource() == getCardPanel().getBodyPanel().getMiInsertLine())
      onInsertLine();
  }

  private String getStrByID(String id)
  {
    return NCLangRes.getInstance().getStrByID("10093030", id);
  }

  private void refreshMaxLineNumber()
  {
    this.m_nMaxLineNumber = 0;

    int count = getCardPanel().getBillTable().getRowCount();
    if (count > 0)
      for (int i = 0; i < count; i++) {
        String gxh = (String)getCardPanel().getBodyValueAt(i, "gxh");
        int nGxh = 0;
        try {
          nGxh = Integer.parseInt(gxh) / 10;
        } catch (Exception e) {
          continue;
        }
        if (nGxh > this.m_nMaxLineNumber)
          this.m_nMaxLineNumber = nGxh;
      }
  }

  private void initComboBoxData(String pos, String itemKey, String[] values)
  {
    UIComboBox cbb = getItemCombox(pos, itemKey);
    for (int i = 0; i < values.length; i++)
      cbb.addItem(values[i]);
  }

  public boolean onInsertLine()
  {
    if (this.m_nState == 1) {
      return false;
    }
    int row = getCardPanel().getBillTable().getSelectedRow();
    if ((row < 0) || (row >= getCardPanel().getBillTable().getRowCount())) {
      return false;
    }
    getCardPanel().insertLine();
    sendDefaultToBody(row);
    return true;
  }
}