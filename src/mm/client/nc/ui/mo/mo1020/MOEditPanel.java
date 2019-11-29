package nc.ui.mo.mo1020;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.mm.prv.IMO;
import nc.itf.pd.inner.IDisassemble;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.mm.pub.MMLog;
import nc.ui.mm.pub.MMToftPanel;
import nc.ui.mm.pub.pub1010.BomVerRefModel;
import nc.ui.mm.pub.pub1010.RouteVerRefModel;
import nc.ui.mm.pub.pub1010.WkRefModel;
import nc.ui.mm.pub.pub1040.FreeItemUIUtilies;
import nc.ui.mm.pub.pub1050.IDataViewer;
import nc.ui.mo.mo0451.BzRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.pf.PfUtilClient;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.bd.fd.DdVO;
import nc.vo.mm.materialserver.BatchUtil;
import nc.vo.mm.proxy.MMProxy;
import nc.vo.mm.pub.FactoryVO;
import nc.vo.mm.pub.FreeItemVO;
import nc.vo.mm.pub.pub1020.ProduceCoreVO;
import nc.vo.mm.pub.pub1025.PoVO;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mm.pub.pub1030.MoItemVO;
import nc.vo.mm.pub.pub1030.MoVO;
import nc.vo.mo.mo1020.ProductVO;
import nc.vo.pd.proxy.PDProxy;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sf.sf1020.KhwlszVO;
import nc.vo.sm.UserVO;

public class MOEditPanel extends MOAbstractPanel
  implements ValueChangedListener, ItemListener, IDataViewer
{
  private MOBillCardPanel cardPanel = null;

  private MoVO oldData = null;
  public UFDouble jhwgsl = null;

  private HashMap hInvCustom = null;

  private PoVO[] baseplanorder = null;

  private int iuistat = 0;

  private Hashtable hashFjl = null;

  private boolean isFixAssistRate = true;

  private MyTaskList myTaskList = new MyTaskList();

  private PsndocVO m_curPsn = null;

  private boolean m_isYxjChanged = false;
  private String ytid;
  private String ytfbid;
  private Integer ytlx;
  private final MMToftPanel tp;

  public MOEditPanel(MMToftPanel mmtoft, MMToolKit tk, DdVO[] dds)
  {
    super(mmtoft, tk, dds);
    this.tp = mmtoft;
    initialize();
  }

  private boolean checkData(MoVO vo)
  {
    try
    {
      vo.validate();
    } catch (ValidationException ex) {
      reportError(ex.getMessage());
      return false;
    }

    MoHeaderVO head = (MoHeaderVO)vo.getParentVO();
    if (head.getDdlx().intValue() == 2) {
      reportError(getstrbyid("UPP50081020-000120"));
      return false;
    }
    return true;
  }

  public BillData getBillData()
  {
    return getCardPanel().getBillData();
  }

  public MOBillCardPanel getCardPanel()
  {
    if (this.cardPanel == null) {
      this.cardPanel = new MOBillCardPanel(this.m_toolkit, null, getUserPK(), getUnitPK());

      this.cardPanel.setName("CardPanel");

      this.cardPanel.setAutoExecHeadEditFormula(true);
    }
    return this.cardPanel;
  }

  private UIComboBox getCbbDdlx()
  {
    return (UIComboBox)getCardPanel().getHeadItem("ddlxshow").getComponent();
  }

  public MoVO getCurrentData()
  {
    MoVO mVO = (MoVO)getCardPanel().getBillValueVO(MoVO.class.getName(), MoHeaderVO.class.getName(), MoItemVO.class.getName());

    if ((mVO == null) || (!checkData(mVO))) {
      return null;
    }

    MoHeaderVO mhead = (MoHeaderVO)mVO.getParentVO();

    mhead.setWlbm(getRfpWlbm().getRefCode());
    mhead.setWlmc(getRfpWlbm().getRefName());

    Object value = getRfpWlbm().getRefValue("bd_invbasdoc.invspec");
    mhead.setInvspec(value == null ? null : value.toString().trim());

    value = getRfpWlbm().getRefValue("bd_invbasdoc.invtype");
    mhead.setInvtype(value == null ? null : value.toString().trim());

    mhead.setScbm(getRfpDept().getRefName());

    mhead.setGzzxmc(getRfpWCenter().getRefName());

    mhead.setBcmc(getRfpBc().getRefName());

    mhead.setBzmc(getRfpBz().getRefName());

    mhead.setYwy(getRfpPsn().getRefName());

    mhead.setKsbm(getRfpCustomer().getRefCode());
    mhead.setKsmc(getRfpCustomer().getRefName());

    mhead.initVO();

    mhead.setStatus(isNull(mhead.getPrimaryKey()) ? 2 : 1);

    return mVO;
  }

  public MoVO getPrintData()
  {
    MoVO vo = getCurrentData();
    if (vo == null) {
      return null;
    }

    MoHeaderVO head = (MoHeaderVO)vo.getParentVO();
    if (head == null) {
      head = new MoHeaderVO();
    }

    head.setGcbm(this.m_toft.getFactoryVO().getName());
    return vo;
  }

  private UIRefPane getRfpDept()
  {
    return (UIRefPane)getCardPanel().getHeadItem("scbmid").getComponent();
  }

  private String getstrbyid(String number)
  {
    return NCLangRes.getInstance().getStrByID("50081020", number);
  }

  private UIRefPane getRfpPsn() {
    return (UIRefPane)getCardPanel().getHeadItem("ywyid").getComponent();
  }

  private UIRefPane getRfpWlbm()
  {
    return (UIRefPane)getCardPanel().getHeadItem("pk_produce").getComponent();
  }

  private UITextField getTfItem(int pos, String strKey)
  {
    UITextField t_tf = null;
    BillItem bi = null;
    if (pos == 0)
      bi = getCardPanel().getHeadItem(strKey);
    else {
      bi = getCardPanel().getBodyItem(strKey);
    }
    if (bi == null) {
      return null;
    }
    if ((bi.getDataType() == 0) || (bi.getDataType() == 1) || (bi.getDataType() == 2) || (bi.getDataType() == 3))
    {
      t_tf = ((UIRefPane)bi.getComponent()).getUITextField();
    }
    return t_tf;
  }

  private UITextField getTfJhwgsl()
  {
    return ((UIRefPane)getCardPanel().getHeadItem("jhwgsl").getComponent()).getUITextField();
  }

  private UITextField getTfFjhsl()
  {
    return ((UIRefPane)getCardPanel().getHeadItem("fjhsl").getComponent()).getUITextField();
  }

  private UITextField getTfHsl()
  {
    return ((UIRefPane)getCardPanel().getHeadItem("fjlhsl").getComponent()).getUITextField();
  }

  protected void initialize()
  {
    setLayout(new BorderLayout());
    add(getCardPanel(), "Center");

    initPanel();
  }

  private void initPanel()
  {
    getCbbDdlx().removeAllItems();
    for (int i = 0; i < getDdsOfMO().length; i++) {
      if (getDdsOfMO()[i].getZdname().equals("ddlx")) {
        getCbbDdlx().addItem(getDdsOfMO()[i].getQzsm());
      }

    }

    getRfpDept().getRefModel().addWherePart(" and bd_deptdoc.pk_calbody = '" + this.m_toft.getFactoryCode() + "'");

    getRfpWlbm().getRefModel().addWherePart(" and bd_produce.pk_calbody='" + this.m_toft.getFactoryCode() + "' and bd_produce.matertype = 'PR' ");

    getRfpWCenter().setIsCustomDefined(true);
    getRfpWCenter().setRefModel(new WkRefModel());
    getRfpWCenter().getRefModel().setWherePart(getRfpWCenter().getRefModel().getWherePart() + " and pd_wk.pk_corp = '" + getUnitPK() + "' and pd_wk.gcbm = '" + getCalbodyPK() + "'");

    getRfpBc().setIsCustomDefined(true);
    getRfpBc().setRefModel(new MobcrefModel());
    getRfpBc().getRefModel().setWherePart(getRfpBc().getRefModel().getWherePart() + " and bd_wt.pk_corp = '" + getUnitPK() + "' and bd_wt.gcbm ='" + getCalbodyPK() + "'");

    getRfpBc().getRefModel().addWherePart(" and pd_wk.pk_wkid is null");

    getRfpBz().setIsCustomDefined(true);
    getRfpBz().setRefModel(new BzRefModel());
    getRfpBz().getRefModel().setWherePart("where pk_corp = '" + getUnitPK() + "' and gcbm = '" + getCalbodyPK() + "'");

    getRfpBomVer().setIsCustomDefined(true);
    BomVerRefModel bom_ver_model = new BomVerRefModel();
    bom_ver_model.setWherePart(bom_ver_model.getWherePart() + " and gcbm = '" + getCalbodyPK() + "'");

    getRfpBomVer().setRefModel(bom_ver_model);

    getRfpRtVer().setIsCustomDefined(true);
    RouteVerRefModel rt_ver_model = new RouteVerRefModel();
    rt_ver_model.setWherePart(rt_ver_model.getWherePart() + " and gcbm = '" + getCalbodyPK() + "'");

    getRfpRtVer().setRefModel(rt_ver_model);

    getCardPanel().addBillEditListenerHeadTail(new BillEditListener() {
      public void afterEdit(BillEditEvent e) {
        MOEditPanel.this.myTaskList.processTaskList(e.getKey());
      }

      public void bodyRowChange(BillEditEvent e)
      {
      }
    });
    getTfItem(0, "jhkssj").setTextType("TextTime");
    getTfItem(0, "jhjssj").setTextType("TextTime");

    getTfItem(0, "jhwgsl").setDelStr("-");
    getTfItem(0, "sjwgsl").setDelStr("-");
    getTfItem(0, "rksl").setDelStr("-");
    getTfItem(0, "fjlhsl").setDelStr("-");
    getTfItem(0, "fjhsl").setDelStr("-");
    getTfItem(0, "fwcsl").setDelStr("-");
    getTfItem(0, "frksl").setDelStr("-");

    getCardPanel().getHeadItem("jhwgsl").setDecimalDigits(this.m_toft.getScaleNum());

    getCardPanel().getHeadItem("sjwgsl").setDecimalDigits(this.m_toft.getScaleNum());

    getCardPanel().getHeadItem("rksl").setDecimalDigits(this.m_toft.getScaleNum());

    getCardPanel().getHeadItem("fjlhsl").setDecimalDigits(this.m_toft.getScaleConvertionRate());

    getCardPanel().getHeadItem("fjhsl").setDecimalDigits(this.m_toft.getScaleAssistantNum());

    getCardPanel().getHeadItem("fwcsl").setDecimalDigits(this.m_toft.getScaleAssistantNum());

    getCardPanel().getHeadItem("frksl").setDecimalDigits(this.m_toft.getScaleAssistantNum());

    getTfItem(0, "jhwgsl").setMaxValue(9999999999.9999981D);
    getTfItem(0, "sjwgsl").setMaxValue(9999999999.9999981D);
    getTfItem(0, "rksl").setMaxValue(9999999999.9999981D);
    getTfItem(0, "fjlhsl").setMaxValue(9999999999.9999981D);
    getTfItem(0, "fjhsl").setMaxValue(9999999999.9999981D);
    getTfItem(0, "fwcsl").setMaxValue(9999999999.9999981D);
    getTfItem(0, "frksl").setMaxValue(9999999999.9999981D);
    getTfItem(0, "scddh").setDelStr("?,");
    getTfItem(0, "xsddh").setDelStr("?,");
    getTfItem(0, "memo").setDelStr("?,");
    try
    {
      IUserManageQuery userQry = (IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class.getName());
      this.m_curPsn = userQry.getPsndocByUserid(this.tp.getUnitCode(), this.tp.getUser().getPrimaryKey());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private int isRemakePickm(MoVO newVO)
  {
    if ((this.oldData == null) || (this.oldData.getParentVO() == null)) {
      return 1;
    }
    MoHeaderVO oldhead = (MoHeaderVO)this.oldData.getParentVO();
    MoHeaderVO newhead = (MoHeaderVO)newVO.getParentVO();

    if (!isNull(newhead.getPzh())) {
      if (!oldhead.getJhwgsl().equals(newhead.getJhwgsl())) {
        return 2;
      }
      if (!oldhead.getJhkgrq().equals(newhead.getJhkgrq())) {
        return 2;
      }
      return 0;
    }

    if (!oldhead.getJhwgsl().equals(newhead.getJhwgsl())) {
      return 1;
    }

    if (!oldhead.getDdlx().equals(newhead.getDdlx())) {
      return 1;
    }

    if (!oldhead.getJhkgrq().equals(newhead.getJhkgrq())) {
      return 1;
    }

    if (!oldhead.getScbmid().equals(newhead.getScbmid())) {
      return 1;
    }

    if (isNull(oldhead.getBomver())) {
      if (!isNull(newhead.getBomver())) {
        return 1;
      }
    }
    else
    {
      if (isNull(newhead.getBomver())) {
        return 1;
      }

      if (!oldhead.getBomver().equals(newhead.getBomver())) {
        return 1;
      }
    }

    if (isNull(oldhead.getRtver())) {
      if (!isNull(newhead.getRtver())) {
        return 1;
      }
    }
    else
    {
      if (isNull(newhead.getRtver())) {
        return 1;
      }
      if (!oldhead.getRtver().equals(newhead.getRtver())) {
        return 1;
      }
    }
    return 0;
  }

  public void itemStateChanged(ItemEvent e)
  {
    if ((e.getSource().equals(getCbbDdlx())) && (e.getItem().equals(getCbbDdlx().getSelectedItem())))
    {
      Object oddlx = getCbbDdlx().getSelectedItem();
      if (isNull(oddlx)) {
        getCardPanel().getHeadItem("ddlx").setValue(null);
        return;
      }
      for (int i = 0; i < getDdsOfMO().length; i++) {
        if ((!getDdsOfMO()[i].getZdname().equals("ddlx")) || (!getDdsOfMO()[i].getQzsm().equals(oddlx)))
          continue;
        if (getDdsOfMO()[i].getSzqz().intValue() == 2) {
          reportError(getstrbyid("UPP50081020-000120"));
          getCbbDdlx().setSelectedItem(getDdsOfMO()[0].getQzsm());
          getCardPanel().getHeadItem("ddlx").setValue(new Integer(0));

          break;
        }getCardPanel().getHeadItem("ddlx").setValue(getDdsOfMO()[i].getSzqz());

        break;
      }
    }
  }

  public MoVO saveData()
  {
    try
    {
      getCardPanel().getBillData().dataNotNullValidate();
      MoVO vo = getCurrentData();
      if (isNull(vo))
        return null;
      vo.getHeadVO().setYtid(this.ytid);
      vo.getHeadVO().setYtfbid(this.ytfbid);
      vo.getHeadVO().setYtlx(this.ytlx);

      if (isNull(vo)) {
        return null;
      }
     if(!getParentCorpCode().equals("10395"))
     {
    	 if (FreeItemUIUtilies.checkNullForFreeItem(getCardPanel())) {
		 reportError(getstrbyid("UPP50081020-000228"));
		 return null;
		 }
     }

      if (this.iuistat == 5)
      {
        MoHeaderVO oldhead = (MoHeaderVO)this.oldData.getParentVO();
        MoHeaderVO newhead = (MoHeaderVO)vo.getParentVO();
        boolean saveflag = true;
        if ((isNull(oldhead.getYjwgrq())) && (isNull(newhead.getYjwgrq())))
          saveflag = false;
        else if ((!isNull(oldhead.getYjwgrq())) && (!isNull(newhead.getYjwgrq())) && (oldhead.getYjwgrq().equals(newhead.getYjwgrq())))
        {
          saveflag = false;
        }
        if (!saveflag) {
          reportHint(getstrbyid("UPP50081020-000121"));
          return vo;
        }
      }

      MoHeaderVO mhead = (MoHeaderVO)vo.getParentVO();

      ArrayList outList = null;
      if (this.iuistat == 3) {
        mhead.setStatus(2);

        ArrayList ainlist = null;
        if (this.baseplanorder != null) {
          ainlist = new ArrayList(this.baseplanorder.length);
          for (int i = 0; i < this.baseplanorder.length; i++) {
            ainlist.add(this.baseplanorder[i]);
          }
        }
        outList = MMProxy.getRemoteMO().createMOByPOArray(vo, ainlist, new UFBoolean(false));

        reportHint(getstrbyid("UPP50081020-000122"));

        MMLog.writeLog(this.tp.getTitle(), 0, getstrbyid("UPP50081020-000123"), null, new MoVO[] { vo });

        MoHeaderVO mohead = (MoHeaderVO)outList.get(3);
        if ((!isNull(mohead)) && (!isNull(mohead.getDczt())))
          mhead.setDczt(mohead.getDczt());
      }
      else if (this.iuistat == 7)
      {
        if (vo.getHeadVO().getJhwgsl().compareTo(isNull(vo.getHeadVO().getSjwgsl()) ? new UFDouble(0) : vo.getHeadVO().getSjwgsl()) < 0)
        {
          this.tp.showWarningMessage(getstrbyid("UPP50081020-000282") + vo.getHeadVO().getSjwgsl());
          return null;
        }

        mhead.setStatus(1);

        if ((!isNull(vo.getHeadVO().getLyid())) && (vo.getHeadVO().getLyid().length() > 0) && (!isNull(vo.getHeadVO().getLylx())) && ((vo.getHeadVO().getLylx().intValue() == 6) || (vo.getHeadVO().getLylx().intValue() == 7) || (vo.getHeadVO().getLylx().intValue() == 8) || (vo.getHeadVO().getLylx().intValue() == 9) || (vo.getHeadVO().getLylx().intValue() == 10)))
        {
          ClientEnvironment.getInstance(); MMProxy.getRemoteMO().backwrite(new MoVO[] { vo }, this.jhwgsl, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getServerTime());
        }

        MMProxy.getRemoteMO().revisemo("mm_mo", this.tp.getUser().getUserName(), vo);

        //begin add by shikun 制盖修订订单保存时，提示备料计划已出库
        String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		  if ("1078".equals(pk_corp)||"1108".equals(pk_corp)) {//制盖使用该功能1078
			  String pk_moid = vo.getHeadVO().getPk_moid()==null?"":vo.getHeadVO().getPk_moid();
			  //校验备料计划是否发货
			  StringBuffer sql = new StringBuffer();
			  sql.append(" select pk_pickmid ") 
			  .append("   from mm_pickm_b ") 
			  .append("  where nvl(dr, 0) = 0 ") 
			  .append("    and (nvl(ljcksl, 0) > 0 or nvl(ljyfsl, 0) > 0) ") 
			  .append("    and pk_pickmid in ") 
			  .append("        (select pk_pickmid from mm_pickm where lyid = '"+pk_moid+"'); ") ;
			  IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			  Object pk_pickmid = qurey.executeQuery(sql.toString(), new ColumnProcessor());
			  if (pk_pickmid!=null) { 
				  reportWarning("该生产订单对应的备料计划已经进行报出库或出库，不进行备料计划更新！");
			  }
		  }
        //end 制盖修订订单保存时，提示备料计划已出库
        outList = MMProxy.getRemoteMO().update_MO(vo);

        MMLog.writeLog(this.tp.getTitle(), 2, getstrbyid("UPT50081020-000012"), null, new MoVO[] { vo });

        String msgHint = getstrbyid("UPP50081020-000124");

        reportHint(msgHint);
      }

      if (outList != null)
      {
        mhead.setPrimaryKey((String)outList.get(0));
        if (isNull(mhead.getScddh())) {
          mhead.setScddh((String)outList.get(1));
        }
        mhead.setTs((String)outList.get(2));
      }
      else if (this.iuistat == 4) {
        mhead.setStatus(1);
        int remake = isRemakePickm(vo);

        if ((!isNull(vo.getHeadVO().getLyid())) && (vo.getHeadVO().getLyid().length() > 0) && (!isNull(vo.getHeadVO().getLylx())) && ((vo.getHeadVO().getLylx().intValue() == 6) || (vo.getHeadVO().getLylx().intValue() == 7) || (vo.getHeadVO().getLylx().intValue() == 8) || (vo.getHeadVO().getLylx().intValue() == 9) || (vo.getHeadVO().getLylx().intValue() == 10)))
        {
          ClientEnvironment.getInstance(); MMProxy.getRemoteMO().backwrite(new MoVO[] { vo }, this.jhwgsl, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getServerTime());
        }

        outList = (ArrayList)PfUtilClient.processAction(this, "UPDATE", "A2", this.m_toft.getLogDate(), vo, new Integer(remake));

        MMLog.writeLog(this.tp.getTitle(), 2, getstrbyid("UPT50081020-000012"), null, new MoVO[] { vo });

        String msgHint = getstrbyid("UPP50081020-000124");

        if (remake > 0) {
          msgHint = msgHint + getstrbyid("UPP50081020-000125");
        }
        reportHint(msgHint);
      }
      if (outList != null)
      {
        mhead.setPrimaryKey((String)outList.get(0));
        if (isNull(mhead.getScddh())) {
          mhead.setScddh((String)outList.get(1));
        }
        mhead.setTs((String)outList.get(2));
      }

      return vo;
    } catch (Exception ex) {
      ex.printStackTrace();
      reportError(getstrbyid("UPP50081020-000126") + ex.getMessage());
    }
    return null;
  }

  private void setNameValues()
  {
    String strddlx = getCardPanel().getHeadItem("ddlx").getValue();

    for (int i = 0; i < getDdsOfMO().length; i++) {
      if ((!getDdsOfMO()[i].getZdname().equals("ddlx")) || (!getDdsOfMO()[i].getSzqz().equals(Integer.valueOf(strddlx)))) {
        continue;
      }
      getCbbDdlx().setSelectedItem(getDdsOfMO()[i].getQzsm());
      break;
    }

    String strzt = getCardPanel().getHeadItem("zt").getValue();
    for (int i = 0; i < getDdsOfMO().length; i++) {
      if ((!getDdsOfMO()[i].getZdname().equals("zt")) || (!getDdsOfMO()[i].getZfqz().equals(strzt)))
        continue;
      getCardPanel().getHeadItem("ztshow").setValue(getDdsOfMO()[i].getQzsm());

      break;
    }
  }

  public void setState(int newstate)
  {
    this.iuistat = newstate;
  }

  private void setTsxValue()
  {
    BillItem[] bis = getCardPanel().getHeadItems();
    Vector vTsxKey = new Vector();
    for (int i = 0; i < bis.length; i++) {
      if (bis[i].getKey().startsWith("Tsx_Syg_")) {
        vTsxKey.addElement(bis[i].getKey());
      }
    }
    if (vTsxKey.size() == 0) {
      return;
    }
    String[] keys = new String[vTsxKey.size()];
    vTsxKey.copyInto(keys);

    KhwlszVO[] khwls = null;
    if ((!isNull(getRfpWlbm().getRefPK())) && (!isNull(getRfpCustomer().getRefPK()))) {
      try
      {
        String keyIndex = getRfpWlbm().getRefPK() + getRfpCustomer().getRefPK();

        if ((this.hInvCustom != null) && (this.hInvCustom.containsKey(keyIndex))) {
          khwls = (KhwlszVO[])(KhwlszVO[])this.hInvCustom.get(keyIndex);
        }
        else {
          khwls = MMProxy.getRemoteMO().getKhwls(getUnitPK(), getRfpWlbm().getRefValue("bd_produce.pk_invbasdoc").toString(), getRfpCustomer().getRefPK());

          if (khwls == null) {
            khwls = new KhwlszVO[0];
          }
          this.hInvCustom.put(keyIndex, khwls);
        }
      } catch (Exception ex) {
        System.out.println("取客户物料特殊项异常: " + ex.getMessage());
      }
    }

    HashMap hTsxValue = null;
    if (khwls == null) {
      hTsxValue = new HashMap(0);
    } else {
      hTsxValue = new HashMap(khwls.length);
      for (int i = 0; i < khwls.length; i++) {
        hTsxValue.put("Tsx_Syg_" + khwls[i].getTsxid(), khwls[i].getTsxqz());
      }

    }

    for (int i = 0; i < keys.length; i++) {
      Object value = hTsxValue.containsKey(keys[i]) ? hTsxValue.get(keys[i]) : null;

      getCardPanel().getHeadItem(keys[i]).setValue(value);
    }
  }

  public void valueChanged(ValueChangedEvent e)
  {
    if (e.getSource().equals(getRfpWlbm())) {
      wlbmChanged();
    } else if (e.getSource().equals(getCardPanel().getHeadItem("yxj").getComponent())) {
      if (MessageDialog.showYesNoDlg(this, "提示", "优先级已修改，是否需要更改未完工派工单的优先级") == 4) {
        setIsYxjChanged(false);
      }
      else
        setIsYxjChanged(true);
    } else if (e.getSource().equals(getRfpCustomer())) {
      setTsxValue();
    } else if (e.getSource().equals(getRfpRtVer())) {
      workWouteChanged();
    } else if (e.getSource().equals(getRfpDept())) {
      deptChanged();
    } else if ((e.getSource().equals(getRfpWCenter())) || (e.getSource() == getRfpJhkgrq()) || (e.getSource() == getRfpJhwgrq()))
    {
      setBcRestriction(null, null, null);

      if (e.getSource().equals(getRfpWCenter())) {
        String wcenterID = getRfpWCenter().getRefPK();
        if (!isNull(wcenterID)) {
          getRfpBz().getRefModel().addWherePart(" and gzzxid = '" + wcenterID + "'");
          try
          {
            String rtver = MMProxy.getRemoteMO().getRtVerByMainWCenter(getCurrentData(), wcenterID);

            getRfpRtVer().setPK(rtver);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    } else if ((e.getSource() == getRfpBc()) && 
      (!isNull(getRfpBc().getRefPK()))) {
      getRfpBz().setPK(getRfpBc().getRefValue("pd_pga.pk_pgaid"));
    }
  }

  private void wlbmChanged()
  {
    getCardPanel().getHeadItem("wlbmid").setValue(getRfpWlbm().getRefValue("bd_produce.pk_invbasdoc"));

    getCardPanel().getHeadItem("invspec").setValue(getRfpWlbm().getRefValue("bd_invbasdoc.invspec"));

    getCardPanel().getHeadItem("invtype").setValue(getRfpWlbm().getRefValue("bd_invbasdoc.invtype"));

    getRfpDept().setPK(getRfpWlbm().getRefValue("bd_produce.pk_deptdoc3"));
    try
    {
      String strJhwgsl = getTfItem(0, "jhwgsl").getText();
      UFDouble jhwgsl = isNull(strJhwgsl) ? null : new UFDouble(strJhwgsl.trim());

      ProductVO product = MMProxy.getRemoteMO().getMoProduct(getRfpWlbm().getRefPK(), jhwgsl, null);

      if (product == null) {
        reportError("业务员在数据库中没有相应的记录");
        return;
      }

      getCardPanel().getHeadItem("jldwid").setValue(product.getMeasPK());

      getCardPanel().getHeadItem("jldw").setValue(product.getMeasName());

      if (product.IsAssistUnit().booleanValue())
      {
        getCardPanel().getHeadItem("fjlid").setValue(product.getAssMeasPK());

        getCardPanel().getHeadItem("fjldw").setValue(product.getAssMeasName());

        getCardPanel().getHeadItem("fjlhsl").setValue(product.getAssHsl());

        boolean isFixAssistRate = (product.getFixFlag() != null) && (product.getFixFlag().booleanValue());

        getCardPanel().getHeadItem("fjlhsl").setEnabled(!isFixAssistRate);

        getCardPanel().getHeadItem("fjhsl").setEnabled(true);
      }
      else {
        getCardPanel().getHeadItem("fjhsl").setEnabled(false);

        getCardPanel().getHeadItem("fjlhsl").setEnabled(false);

        getCardPanel().getHeadItem("fjlid").setValue(null);
        getCardPanel().getHeadItem("fjldw").setValue(null);
        getCardPanel().getHeadItem("fjlhsl").setValue(null);
        getCardPanel().getHeadItem("fjhsl").setValue(null);
      }

      if (this.m_curPsn == null) {
        getCardPanel().getHeadItem("ywyid").setValue(product.getPk_psndoc3());
      }

      getCardPanel().getHeadItem("sfdc").setValue(product.getConverseFlag());

      getRfpBomVer().getRefModel().addWherePart(" and pk_produce = '" + getRfpWlbm().getRefPK() + "'");

      getRfpBomVer().setPK(product.getBomver());

      getRfpRtVer().getRefModel().addWherePart(" and pk_produce = '" + getRfpWlbm().getRefPK() + "'");

      getRfpRtVer().setPK(product.getRtver());

      setTsxValue();
      FreeItemUIUtilies.buildFreeForHeadInvChange(getCardPanel());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      reportError(getstrbyid("UPP50081020-000220") + ex.getMessage());
    }

    deptChanged();
  }

  public MOEditPanel(MMToftPanel mmtoft)
  {
    this(mmtoft, null);
  }

  public MOEditPanel(MMToftPanel mmtoft, MMToolKit tk)
  {
    this(mmtoft, tk, null);
  }

  public void backupTsxValue(MoHeaderVO[] mheads)
  {
    if ((mheads == null) || (mheads.length == 0)) {
      return;
    }

    if (this.hInvCustom == null) {
      this.hInvCustom = new HashMap();
    }

    for (int i = 0; i < mheads.length; i++) {
      if ((isNull(mheads[i].getKsid())) || (isNull(mheads[i].getPk_produce())))
      {
        continue;
      }

      String keyIndex = mheads[i].getPk_produce() + mheads[i].getKsid();

      this.hInvCustom.put(keyIndex, mheads[i].getAllKhwl());
    }
  }

  public PoVO[] getBasePlanOrder()
  {
    return this.baseplanorder;
  }

  private UIRefPane getRfpBc()
  {
    return (UIRefPane)getCardPanel().getHeadItem("bcid").getComponent();
  }

  private UIRefPane getRfpJhkgrq() {
    return (UIRefPane)getCardPanel().getHeadItem("jhkgrq").getComponent();
  }

  private UIRefPane getRfpJhwgrq() {
    return (UIRefPane)getCardPanel().getHeadItem("jhwgrq").getComponent();
  }

  private UIRefPane getRfpBz() {
    return (UIRefPane)getCardPanel().getHeadItem("bzid").getComponent();
	 // return null;
  }

  private UIRefPane getRfpBomVer() {
    return (UIRefPane)getCardPanel().getHeadItem("bomver").getComponent();
  }

  private UIRefPane getRfpCustomer()
  {
    return (UIRefPane)getCardPanel().getHeadItem("ksid").getComponent();
  }

  private UIRefPane getRfpRtVer()
  {
    return (UIRefPane)getCardPanel().getHeadItem("rtver").getComponent();
  }

  private UIRefPane getRfpWCenter()
  {
    return (UIRefPane)getCardPanel().getHeadItem("gzzxid").getComponent();
  }

  public void setBaseplanorder(PoVO[] newBaseplanorder)
  {
    this.baseplanorder = newBaseplanorder;
  }

  public void setEnabled(boolean enable)
  {
    getCardPanel().setEnabled(enable);
    super.setEnabled(enable);
  }

  public void setStateForWR()
  {
    getCardPanel().getHeadItem("pk_produce").setEnabled(false);
    getCardPanel().getHeadItem("ddlxshow").setEnabled(false);
    getCardPanel().getHeadItem("jhwgsl").setEnabled(false);
  }

  public MOEditPanel()
  {
    this.tp = null;
    this.m_toft = new MMToftPanel() {
      public String getTitle() {
        return null;
      }

      public void onButtonClicked(ButtonObject bo) {
      }

      public void onHelp() {
      }

      public void onPrint() {
      }

      public boolean onRefresh() {
        return false;
      }
    };
    this.m_toolkit = new MMToolKit(this.m_toft.getUnitCode(), this.m_toft.getFactoryCode());
    initialize();
  }

  public void loadData(String strBillType, String strBusiType, String strBillID)
  {
    try
    {
      StringBuffer where = new StringBuffer("");
      where.append("where mm_mo.pk_moid = '").append(strBillID).append("'");

      MoHeaderVO[] headers = MMProxy.getRemoteMO().queryMoByWhere(where.toString());
      FreeItemUIUtilies.setFreeItemVOForCirVO(headers);

      MoVO vo = new MoVO();

      if ((headers != null) && (headers.length > 0)) {
        vo.setParentVO(headers[0]);
      }
      setRefPaneRestriction(vo.getHeadVO());
      setData(vo);
      FreeItemUIUtilies.buildFreeForHeader(getCardPanel(), vo.getParentVO());

      setEnabled(false);
    }
    catch (Exception ex)
    {
    }
  }

  private void setData(MoVO vo)
  {
    try
    {
      setEnabled(true);
      getCardPanel().getBillData().clearViewData();

      this.oldData = vo;
      try {
        getCbbDdlx().removeItemListener(this);

        if (this.hashFjl == null) {
          this.hashFjl = new Hashtable();
        }
        String wlbmid = vo.getHeadVO().getWlbmid();
        ProduceCoreVO fjlVo = null;

        if ((!isNull(wlbmid)) && (!this.hashFjl.containsKey(wlbmid))) {
          try {
            this.hashFjl.put(wlbmid, PDProxy.getRemoteDisassemble().getFjldwInfo(wlbmid));
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }

        if (!isNull(wlbmid))
          fjlVo = (ProduceCoreVO)this.hashFjl.get(wlbmid);
        if (!isNull(fjlVo)) {
          this.isFixAssistRate = (fjlVo.getFixedflag() == null ? true : fjlVo.getFixedflag().booleanValue());
        }

        getRfpWlbm().getRefModel();
        getCardPanel().setBillValueVO(vo);
        setIsYxjChanged(false);
        setNameValues();

        getCardPanel().execHeadLoadFormulas();
      } finally {
        getCbbDdlx().addItemListener(this);
      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      reportError(getstrbyid("UPP50081020-000127") + ex.getMessage());
    }
  }

  public int setModifyData(MoVO mvo)
  {
    setRefPaneRestriction(mvo.getHeadVO());

    setData(mvo);

    if (isNull(mvo.getHeadVO().getJhwgsl()))
      this.jhwgsl = new UFDouble(0);
    else {
      this.jhwgsl = mvo.getHeadVO().getJhwgsl();
    }

    FreeItemUIUtilies.buildFreeForHeader(getCardPanel(), mvo.getParentVO());

    MoHeaderVO head = mvo.getHeadVO();
    if (this.iuistat == 7)
    {
      getCardPanel().setEnabled(true);
      getCardPanel().getHeadItem("scddh").setEnabled(false);
      getCardPanel().getHeadItem("pk_produce").setEnabled(false);
    }
    else if (!head.getZt().equals("A"))
    {
      getCardPanel().setEnabled(false);

      if (head.getZt().equalsIgnoreCase("B"))
      {
        getCardPanel().getHeadItem("yjwgrq").setEnabled(true);
        getCardPanel().getHeadItem("memo").setEnabled(true);
      }
      setState(5);
      reportHint(getstrbyid("UPP50081020-000128"));
    }
    else {
      setState(4);
      getCardPanel().setEnabled(true);
      reportHint(getstrbyid("UPP50081020-000129"));
    }

    if ((isNull(mvo.getHeadVO().getFjlid())) || (this.isFixAssistRate)) {
      getCardPanel().getHeadItem("fjldw").setEnabled(false);
      getCardPanel().getHeadItem("fjhsl").setEnabled(false);
      getCardPanel().getHeadItem("fjlhsl").setEnabled(false);
    }

    getCardPanel().getHeadItem("scddh").setEnabled(false);
    if (head.getDdlx().intValue() == 1)
      getCardPanel().getHeadItem("pk_produce").setEnabled(true);
    else {
      getCardPanel().getHeadItem("pk_produce").setEnabled(false);
    }
    return this.iuistat;
  }

  private void setRefPaneRestriction(MoHeaderVO header)
  {
    String sdate = null;
    String edate = null;
    String pk_produce = null;
    String deptID = null;
    String wcenterID = null;
    if (!isNull(header.getPk_produce()))
    {
      pk_produce = header.getPk_produce();
    }
    if (!isNull(header.getScbmid()))
    {
      deptID = header.getScbmid();
    }
    if (!isNull(header.getGzzxid()))
    {
      wcenterID = header.getGzzxid();
    }
    if (!isNull(header.getJhkgrq()))
    {
      sdate = header.getJhkgrq().toString();
    }

    if (!isNull(header.getJhwgrq()))
    {
      edate = header.getJhwgrq().toString();
    }

    if (!isNull(deptID)) {
      getRfpWCenter().getRefModel().addWherePart(" and ssbmid = '" + deptID + "'");
    }
    if ((!isNull(wcenterID)) && (!isNull(sdate)) && (!isNull(edate))) {
      setBcRestriction(wcenterID, sdate, edate);
    }
    if (!isNull(wcenterID))
      getRfpBz().getRefModel().addWherePart(" and gzzxid = '" + wcenterID + "'");
    else if (!isNull(deptID)) {
      getRfpBz().getRefModel().addWherePart(" and bmid = '" + deptID + "'");
    }

    if (!isNull(pk_produce)) {
      getRfpBomVer().getRefModel().addWherePart(" and pk_produce = '" + pk_produce + "'");

      getRfpRtVer().getRefModel().addWherePart(" and pk_produce = '" + pk_produce + "'");
    }
  }

  public void setNewData(MoVO mvo)
  {
    setState(3);

    if (mvo == null) {
      mvo = new MoVO();
      mvo.setParentVO(new MoHeaderVO());
      mvo.getHeadVO().setPk_corp(this.m_toft.getUnitCode());
      mvo.getHeadVO().setGcbm(this.m_toft.getFactoryCode());
      mvo.getHeadVO().setDdlx(new Integer(0));
      mvo.getHeadVO().setDdlxShow(getDescriptOfDdlx(mvo.getHeadVO().getDdlx()));

      mvo.getHeadVO().setLylxShow(getstrbyid("UPP50081020-000257"));
    }

    mvo.getHeadVO().setZt("A");
    mvo.getHeadVO().setZtShow(getDescriptOfState(mvo.getHeadVO().getZt()));
    mvo.getHeadVO().setDczt(new Integer(0));
    mvo.getHeadVO().setDyzt(new Integer(0));
    mvo.getHeadVO().setDyztShow(getstrbyid("UPP50081020-000258"));
    mvo.getHeadVO().setPk_moid(null);
    mvo.getHeadVO().setPk_mokzid(null);
    mvo.getHeadVO().setRksl(null);
    mvo.getHeadVO().setSfydc(new UFBoolean(false));
    mvo.getHeadVO().setSjjssj(null);
    mvo.getHeadVO().setSjkgrq(null);
    mvo.getHeadVO().setSjkssj(null);
    mvo.getHeadVO().setSjwgrq(null);
    mvo.getHeadVO().setSjwgsl(null);
    mvo.getHeadVO().setTs(null);
    mvo.getHeadVO().setYjwgrq(null);
    mvo.getHeadVO().setZdrid(this.m_toft.getUser().getUserName());
    mvo.getHeadVO().setZdrq(new UFDate(this.m_toft.getLogDate()));

    this.ytid = mvo.getHeadVO().getYtid();
    this.ytfbid = mvo.getHeadVO().getYtfbid();
    this.ytlx = mvo.getHeadVO().getYtlx();

    mvo.getHeadVO().setStatus(2);
    setRefPaneRestriction(mvo.getHeadVO());

    setData(mvo);
    FreeItemUIUtilies.buildFreeForHeader(getCardPanel(), mvo.getParentVO());

    if ((isNull(mvo.getHeadVO().getFjlid())) || (this.isFixAssistRate)) {
      getCardPanel().getHeadItem("fjldw").setEnabled(false);
      getCardPanel().getHeadItem("fjhsl").setEnabled(false);
      getCardPanel().getHeadItem("fjlhsl").setEnabled(false);
    }

    getCardPanel().getHeadItem("scddh").setEnabled(true);
    getCardPanel().getHeadItem("pk_produce").setEnabled(true);

    setTsxValue();
    reportHint(getstrbyid("UPP50081020-000130"));

    if (this.m_curPsn != null)
      getRfpPsn().setPK(this.m_curPsn.getPk_psndoc());
  }

  public void updatePrintedFlag()
  {
    getCardPanel().getHeadItem("dyzt").setValue(new Integer(1));
    getCardPanel().getHeadItem("dyztshow").setValue(getstrbyid("UPP50081020-000064"));
  }

  private void setBcRestriction(String strWkID, String strBeginDate, String strEndDate)
  {
    StringBuffer restricSql = new StringBuffer("");

    if (isNull(strWkID))
      strWkID = getRfpWCenter().getRefPK();
    restricSql.append(" and pd_wk.pk_wkid = '").append(strWkID).append("'");

    if (isNull(strBeginDate))
      strBeginDate = getRfpJhkgrq().getText();
    restricSql.append(" and pd_lb.rq >= '").append(strBeginDate).append("'");

    if (isNull(strEndDate))
      strEndDate = getRfpJhwgrq().getText();
    restricSql.append(" and pd_lb.rq <= '").append(strEndDate).append("'");

    getRfpBc().getRefModel().addWherePart(restricSql.toString());
  }

  private void workWouteChanged()
  {
    Object strRtID = getRfpRtVer().getRefValue("pk_rtid");
    if (!isNull(strRtID))
      try {
        String strMainWCenterID = MMProxy.getRemoteMO().getMainWCenterID(strRtID.toString());

        if (!isNull(strMainWCenterID))
          getRfpWCenter().setPK(strMainWCenterID);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
  }

  private void deptChanged()
  {
    String strDeptID = getRfpDept().getRefPK();
    if (!isNull(strDeptID))
    {
      getRfpWCenter().getRefModel().addWherePart(" and ssbmid = '" + strDeptID + "'");

      getRfpBz().getRefModel().addWherePart(" and bmid = '" + strDeptID + "'");
    }
  }

  private void setBomAndRouteVer(BillEditEvent e)
  {
    if (isNull(getRfpWlbm().getRefPK())) {
      return;
    }
    Object oroadtype = getRfpWlbm().getRefValue("roadtype");
    Integer roadtype = new Integer(isNull(oroadtype) ? "0" : oroadtype.toString());

    if (roadtype.intValue() == 0)
      return;
    try
    {
      String strJhwgsl = e == null ? getTfItem(0, "jhwgsl").getText() : (String)e.getValue();

      UFDouble jhwgsl = strJhwgsl == null ? null : new UFDouble(strJhwgsl.trim());

      String[] vers = MMProxy.getRemoteMO().getAvaVersion(getUnitPK(), getCalbodyPK(), (String)getRfpWlbm().getRefValue("bd_produce.pk_invbasdoc"), "AB", jhwgsl, null);

      getCardPanel().getHeadItem("bomver").setValue(vers[0]);

      getCardPanel().getHeadItem("rtver").setValue(vers[2]);
    } catch (Exception ex) {
      ex.printStackTrace();
      reportException(ex);
    }
  }

  private void adjustAssissNum(String id, String value)
  {
    int alterFlag;
    if (id.equals("jhwgsl")) {
      alterFlag = 0;
    }
    else
    {
      //int alterFlag;
      if (id.equals("fjhsl")) {
        alterFlag = 1;
      }
      else
      {
     //   int alterFlag;
        if (id.equals("fjlhsl")) {
          alterFlag = 2;
        }
        else
          return;
      }
    }
   // int alterFlag;
    UFDouble[] nums = new UFDouble[3];
    String tmpStr = getTfItem(0, "fjhsl").getText();
    if (alterFlag == 1) {
      tmpStr = value;
    }
    nums[0] = (isNull(tmpStr) ? null : new UFDouble(tmpStr));
    tmpStr = getTfItem(0, "jhwgsl").getText();
    if (alterFlag == 0) {
      tmpStr = value;
    }
    nums[1] = (isNull(tmpStr) ? null : new UFDouble(tmpStr));
    tmpStr = getTfItem(0, "fjlhsl").getText();
    if (alterFlag == 2) {
      tmpStr = value;
    }
    nums[2] = (isNull(tmpStr) ? null : new UFDouble(tmpStr));

    nums = BatchUtil.getUFDoubleByConversion(nums, alterFlag, this.isFixAssistRate);

    getCardPanel().getHeadItem("fjhsl").setValue(nums[0]);
    getCardPanel().getHeadItem("jhwgsl").setValue(nums[1]);
    getCardPanel().getHeadItem("fjlhsl").setValue(nums[2]);
  }

  public boolean isYxjChanged()
  {
    return this.m_isYxjChanged;
  }

  public void setIsYxjChanged(boolean yxjChanged) {
    this.m_isYxjChanged = yxjChanged;
  }

  class MyTaskList
    implements ITaskList
  {
    private HashMap taskMap = new HashMap(20);

    private HashMap instanceMap = new HashMap(20);

    private HashMap influencedMap = new HashMap(20);

    private HashMap process = new HashMap(10);

    MyTaskList()
    {
      initInternal();
    }

    private void initInternal()
    {
      this.taskMap.put("pk_produce", new MOEditPanel.InvTask());
      this.taskMap.put("scbmid", new MOEditPanel.DeptTask());
      this.taskMap.put("gzzxid", new MOEditPanel.WCenterTask());
      this.taskMap.put("bcid", new MOEditPanel.BcTask());
      this.taskMap.put("ksid", new MOEditPanel.CustomerTask());
      this.taskMap.put("rtver", new MOEditPanel.RouterVerTask());
      this.taskMap.put("jhkgrq", new MOEditPanel.RqTask());
      this.taskMap.put("jhwgrq", this.taskMap.get("jhkgrq"));
      this.taskMap.put("jhwgsl", new MOEditPanel.JhwgslTask());
      this.taskMap.put("fjhsl", new MOEditPanel.FjhslTask());
      this.taskMap.put("fjlhsl", new MOEditPanel.HslTask());
      this.taskMap.put("freeitemvalue1", new MOEditPanel.ZyxTask());
      this.taskMap.put("freeitemvalue2", this.taskMap.get("freeitemvalue1"));
      this.taskMap.put("freeitemvalue3", this.taskMap.get("freeitemvalue1"));
      this.taskMap.put("freeitemvalue4", this.taskMap.get("freeitemvalue1"));
      this.taskMap.put("freeitemvalue5", this.taskMap.get("freeitemvalue1"));

      this.influencedMap.put("pk_produce", new String[] { "rtver", "scbmid", "fjlhsl" });

      this.influencedMap.put("scbmid", new String[] { "gzzxid" });

      this.influencedMap.put("gzzxid", new String[] { "bcid", "rtver", "scbmid" });

      this.influencedMap.put("rtver", new String[] { "gzzxid" });

      this.influencedMap.put("jhkgrq", new String[] { "bcid" });

      this.influencedMap.put("jhwgrq", new String[] { "bcid" });

      this.influencedMap.put("fjlhsl", new String[] { "jhwgsl" });

      this.influencedMap.put("jhwgsl", new String[] { "rtver" });

      this.influencedMap.put("freeitemvalue1", new String[] { "rtver" });

      this.influencedMap.put("freeitemvalue2", new String[] { "rtver" });

      this.influencedMap.put("freeitemvalue3", new String[] { "rtver" });

      this.influencedMap.put("freeitemvalue4", new String[] { "rtver" });

      this.influencedMap.put("freeitemvalue5", new String[] { "rtver" });
    }

    public void registerTask(String identifier, String taskClassName)
    {
      throw new UnsupportedOperationException(MOEditPanel.this.getstrbyid("UPP50081020-000119"));
    }

    public void registerInfluence(String identifier, String[] influenced)
      throws UnsupportedOperationException
    {
      throw new UnsupportedOperationException(MOEditPanel.this.getstrbyid("UPP50081020-000119"));
    }

    public void processTaskList(String identifier)
    {
      this.process.clear();
      doTask(identifier);
    }

    private void doTask(String identifier)
    {
      if ((identifier == null) || (!this.taskMap.containsKey(identifier)) || (this.process.containsKey(identifier)))
      {
        return;
      }

      ITask t = (ITask)this.taskMap.get(identifier);
      if (!t.processTask()) return;

      this.process.put(identifier, identifier);

      String[] subIDs = null;
      if (this.influencedMap.containsKey(identifier))
        subIDs = (String[])(String[])this.influencedMap.get(identifier);
      else {
        subIDs = new String[0];
      }
      for (int i = 0; i < subIDs.length; i++)
        doTask(subIDs[i]);
    }
  }

  class CustomerTask
    implements ITask
  {
    CustomerTask()
    {
    }

    public boolean processTask()
    {
      MOEditPanel.this.setTsxValue();
      return false;
    }
  }

  class RouterVerTask
    implements ITask
  {
    RouterVerTask()
    {
    }

    public boolean processTask()
    {
      Object judgecode = MOEditPanel.this.getRfpRtVer().getRefValue("judgecode");
      ((UIRefPane)MOEditPanel.this.getCardPanel().getHeadItem("judgecode").getComponent()).setText(judgecode == null ? null : judgecode.toString());

      Object strRtID = MOEditPanel.this.getRfpRtVer().getRefValue("pk_rtid");
      if (!MOEditPanel.this.isNull(strRtID)) {
        try {
          String strMainWCenterID = MMProxy.getRemoteMO().getMainWCenterID(strRtID.toString());

          if (!MOEditPanel.this.isNull(strMainWCenterID))
          {
            MOEditPanel.this.getRfpWCenter().getRefModel().addWherePart("");
            MOEditPanel.this.getRfpWCenter().setPK(strMainWCenterID);
          }
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
          return false;
        }
      }
      return true;
    }
  }

  class BcTask
    implements ITask
  {
    BcTask()
    {
    }

    public boolean processTask()
    {
      if (MOEditPanel.this.isNull(MOEditPanel.this.getRfpBc().getRefPK()))
        MOEditPanel.this.getRfpBz().setPK(null);
      else {
        MOEditPanel.this.getRfpBz().setPK(MOEditPanel.this.getRfpBc().getRefValue("pd_pga.pk_pgaid"));
      }
      return true;
    }
  }

  class WCenterTask
    implements ITask
  {
    WCenterTask()
    {
    }

    public boolean processTask()
    {
      MOEditPanel.this.setBcRestriction(null, null, null);
      String wcenterID = MOEditPanel.this.getRfpWCenter().getRefPK();
      String wcenterID2 = String.valueOf(MOEditPanel.this.getRfpWCenter().getRefValue("pk_wkid"));
      System.out.println(wcenterID);
      if (!MOEditPanel.this.isNull(wcenterID))
      {
        MOEditPanel.this.getRfpDept().setPK(MOEditPanel.this.getRfpWCenter().getRefValue("ssbmid"));

        MOEditPanel.this.getRfpBz().getRefModel().addWherePart(" and gzzxid = '" + wcenterID + "'");
        try
        {
          MoVO currentvo = new MoVO();
          MoVO mVO = (MoVO)MOEditPanel.this.getCardPanel().getBillValueVO(MoVO.class.getName(), MoHeaderVO.class.getName(), MoItemVO.class.getName());

          String wlbmid = mVO.getHeadVO().getWlbmid();
          currentvo.setParentVO(mVO.getParentVO());
          currentvo.getHeadVO().setWlbmid(wlbmid);
          currentvo.getHeadVO().setFreeitemVO(mVO.getHeadVO().getFreeitemVO());

          String rtver = MMProxy.getRemoteMO().getRtVerByMainWCenter(currentvo, wcenterID);

          MOEditPanel.this.getRfpRtVer().setPK(rtver);
        } catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
      }
      else {
        MOEditPanel.this.getRfpBz().getRefModel().addWherePart("");
      }
      return true;
    }
  }

  class ZyxTask
    implements ITask
  {
    ZyxTask()
    {
    }

    public boolean processTask()
    {
      MOEditPanel.this.getCardPanel().stopEditing();
      MoVO mVO = (MoVO)MOEditPanel.this.getCardPanel().getBillValueVO(MoVO.class.getName(), MoHeaderVO.class.getName(), MoItemVO.class.getName());

      String wlbmid = mVO.getHeadVO().getWlbmid();
      FreeItemVO freeitemvo = mVO.getHeadVO().getFreeitemVO();
      String[] returnstr = null;

      if (wlbmid == null) {
        return true;
      }
      try
      {
        returnstr = MMProxy.getRemoteMO().getAvaVersion(MOEditPanel.this.m_toft.getUnitCode(), MOEditPanel.this.m_toft.getFactoryCode(), wlbmid, "AB", null, freeitemvo);
      }
      catch (Exception e) {
      }
      if ((returnstr != null) && (returnstr[1] != null))
      {
        MOEditPanel.this.getRfpBomVer().setPK(returnstr[1]);
      }
      if ((returnstr != null) && (returnstr[3] != null))
      {
        MOEditPanel.this.getRfpRtVer().setPK(returnstr[3]);
      }

      return true;
    }
  }

  class RqTask
    implements ITask
  {
    RqTask()
    {
    }

    public boolean processTask()
    {
      MOEditPanel.this.setBcRestriction(null, null, null);
      return true;
    }
  }

  class DeptTask
    implements ITask
  {
    DeptTask()
    {
    }

    public boolean processTask()
    {
      String strDeptID = MOEditPanel.this.getRfpDept().getRefPK();
      if (!MOEditPanel.this.isNull(strDeptID))
      {
        MOEditPanel.this.getRfpWCenter().getRefModel().addWherePart(" and ssbmid = '" + strDeptID + "'");

        if (!strDeptID.equals(MOEditPanel.this.getRfpWCenter().getRefValue("ssbmid"))) {
          MOEditPanel.this.getRfpWCenter().setPK(null);
        }

      }
      else
      {
        MOEditPanel.this.getRfpWCenter().getRefModel().addWherePart("");
      }
      return true;
    }
  }

  class HslTask
    implements ITask
  {
    HslTask()
    {
    }

    public boolean processTask()
    {
      MOEditPanel.this.adjustAssissNum("fjlhsl",MOEditPanel.this.getTfHsl().getText());
      return true;
    }
  }

  class FjhslTask
    implements ITask
  {
    FjhslTask()
    {
    }

    public boolean processTask()
    {
      MOEditPanel.this.adjustAssissNum("fjhsl", MOEditPanel.this.getTfFjhsl().getText());
      return true;
    }
  }

  class JhwgslTask
    implements ITask
  {
    JhwgslTask()
    {
    }

    public boolean processTask()
    {
      MOEditPanel.this.adjustAssissNum("jhwgsl", MOEditPanel.this.getTfJhwgsl().getText());
      return true;
    }
  }

  class InvTask
    implements ITask
  {
    InvTask()
    {
    }

    public boolean processTask()
    {
      MOEditPanel.this.getCardPanel().getHeadItem("wlbmid").setValue(MOEditPanel.this.getRfpWlbm().getRefValue("bd_produce.pk_invbasdoc"));

      MOEditPanel.this.getCardPanel().getHeadItem("invspec").setValue(MOEditPanel.this.getRfpWlbm().getRefValue("bd_invbasdoc.invspec"));

      MOEditPanel.this.getCardPanel().getHeadItem("invtype").setValue(MOEditPanel.this.getRfpWlbm().getRefValue("bd_invbasdoc.invtype"));

      MOEditPanel.this.getCardPanel().getHeadItem("wlth").setValue(MOEditPanel.this.getRfpWlbm().getRefValue("bd_invbasdoc.graphid"));

      MOEditPanel.this.getRfpDept().setPK(MOEditPanel.this.getRfpWlbm().getRefValue("bd_produce.pk_deptdoc3"));

      ProductVO product = null;
      try
      {
        String strJhwgsl = MOEditPanel.this.getTfJhwgsl().getText();
        UFDouble jhwgsl = MOEditPanel.this.isNull(strJhwgsl) ? null : new UFDouble(strJhwgsl.trim());

        product = MMProxy.getRemoteMO().getMoProduct(MOEditPanel.this.getRfpWlbm().getRefPK(), jhwgsl, null);

        if (product == null) {
          MOEditPanel.this.reportError("物料编码在数据库中没有相应的记录");
          return false;
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        MOEditPanel.this.reportError(MOEditPanel.this.getstrbyid("UPP50081020-000220") + ex.getMessage());

        return false;
      }

      MOEditPanel.this.getCardPanel().getHeadItem("jldwid").setValue(product.getMeasPK());

      MOEditPanel.this.getCardPanel().getHeadItem("jldw").setValue(product.getMeasName());

      if (product.IsAssistUnit().booleanValue())
      {
        MOEditPanel.this.getCardPanel().getHeadItem("fjlid").setValue(product.getAssMeasPK());

        MOEditPanel.this.getCardPanel().getHeadItem("fjldw").setValue(product.getAssMeasName());

        MOEditPanel.this.getCardPanel().getHeadItem("fjlhsl").setValue(product.getAssHsl());

        boolean isFixAssistRate = (product.getFixFlag() != null) && (product.getFixFlag().booleanValue());

        MOEditPanel.this.getCardPanel().getHeadItem("fjlhsl").setEnabled(!isFixAssistRate);

        MOEditPanel.this.getCardPanel().getHeadItem("fjhsl").setEnabled(true);
      }
      else {
        MOEditPanel.this.getCardPanel().getHeadItem("fjhsl").setEnabled(false);

        MOEditPanel.this.getCardPanel().getHeadItem("fjlhsl").setEnabled(false);

        MOEditPanel.this.getCardPanel().getHeadItem("fjlid").setValue(null);
        MOEditPanel.this.getCardPanel().getHeadItem("fjldw").setValue(null);
        MOEditPanel.this.getCardPanel().getHeadItem("fjlhsl").setValue(null);
        MOEditPanel.this.getCardPanel().getHeadItem("fjhsl").setValue(null);
      }

      if (MOEditPanel.this.m_curPsn == null) {
        MOEditPanel.this.getCardPanel().getHeadItem("ywyid").setValue(product.getPk_psndoc3());
      }

      MOEditPanel.this.getCardPanel().getHeadItem("sfdc").setValue(product.getConverseFlag());

      MOEditPanel.this.getRfpBomVer().getRefModel().addWherePart(" and pk_produce = '" + MOEditPanel.this.getRfpWlbm().getRefPK() + "'");

      MOEditPanel.this.getRfpBomVer().setPK(product.getBomver());

      MOEditPanel.this.getRfpRtVer().getRefModel().addWherePart(" and pk_produce = '" + MOEditPanel.this.getRfpWlbm().getRefPK() + "'");

      MOEditPanel.this.getRfpRtVer().setPK(product.getRtver());

      MOEditPanel.this.setTsxValue();
      FreeItemUIUtilies.buildFreeForHeadInvChange(MOEditPanel.this.getCardPanel());

      return true;
    }
  }
  
  public String getParentCorpCode() {

		String ParentCorp = new String();
		String key = ClientEnvironment.getInstance().getCorporation()
				.getFathercorp();
		try {
			CorpVO corpVO = CorpBO_Client.findByPrimaryKey(key);
			ParentCorp = corpVO.getUnitcode();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ParentCorp;
	}
}