package nc.ui.ep.dj.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.pub.SuperDMO;
import nc.bs.trade.business.HYSuperDMO;
import nc.impl.arap.proxy.Proxy;
import nc.itf.fi.pub.SysInit;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.arap.global.PubData;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.arap.pubdj.ArapDjBillCardPanel;
import nc.ui.arap.pubdj.ArapDjPanel;
import nc.ui.arap.pubdj.DjVOChanger;
import nc.ui.arap.pubdj.PubDjPanel;
import nc.ui.arap.pubdj.TempletUtil;
import nc.ui.ep.dj.ARAPDjDataBuffer;
import nc.ui.ep.dj.ARAPDjUIController;
import nc.ui.ep.dj.ArapBillWorkPageConst;
import nc.ui.ep.dj.ArapButtonStatUtil;
import nc.ui.ep.dj.Arap_ProcAction;
import nc.ui.ep.dj.DjPanel;
import nc.ui.ep.dj.ShenheLog;
import nc.ui.ic.pub.bill.query.QueryConditionDlg;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.global.DjCheckParamVO;
import nc.vo.arap.global.DjVOTreaterAid;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.arap.pub.StrResPorxy;
import nc.vo.arap.pub.VOCompress;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.ep.dj.DJZBVOTreator;
import nc.vo.ep.dj.DJZBVOUtility;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public abstract class ARAPDjCtlDjEdit extends ARAPDjCtlParent
  implements ARAPDjUIController
{
  public ARAPDjCtlDjEdit(PubDjPanel pubDjPanel, ArapDjPanel arapDjPanel)
  {
    super(pubDjPanel, arapDjPanel);
  }

  public ARAPDjCtlDjEdit(DjPanel djPanel, ArapDjPanel arapDjPanel)
  {
    super(djPanel, arapDjPanel);
  }
  private boolean checkNullBefSave(DJZBVO djzbvo) {
    DJZBHeaderVO headvo = (DJZBHeaderVO)djzbvo.getParentVO();
    DJZBItemVO[] items = (DJZBItemVO[])djzbvo.getChildrenVO();

    String hpaydate = headvo.getPaydate();
    for (DJZBItemVO bvo : items) {
      String bpaydate = bvo.getPaydate();
      if ((hpaydate == null) || (("".equals(hpaydate.trim())) && (bpaydate != null) && (!"".equals(bpaydate.trim()))))
      {
        headvo.setPaydate(bpaydate);
        break;
      }if ((hpaydate == null) || ("".equals(hpaydate.trim())) || ((bpaydate != null) && (!"".equals(bpaydate.trim()))))
        continue;
      bvo.setPaydate(hpaydate);
    }

    String wldx = null;
    String RES = "";

    StringBuffer sb = new StringBuffer();

    boolean istr = true;
    int i = 0; for (int size = items.length; i < size; i++) {
      if (items[i].getWldx() != null) {
        if (items[i].getWldx().intValue() == 2) {
          wldx = "deptid";
          RES = NCLangRes4VoTransl.getNCLangRes().getStrByID("200602", "UPT200602-v35-000024");
        }
        else if (items[i].getWldx().intValue() == 3) {
          wldx = "ywybm";
          RES = NCLangRes4VoTransl.getNCLangRes().getStrByID("200602", "UPT200602-v35-000026");
        }
        else {
          wldx = "ksbm_cl";

          RES = NCLangRes4VoTransl.getNCLangRes().getStrByID("200602", "UPT200602-v35-000025");
        }
      }
      String line = "";
      istr = true;
      if ((items[i].getBzbm() == null) || (items[i].getBzbm().length() == 0)) {
        line = line + NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000193") + 
          NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000516");
        istr = false;
      }
      if (items[i].getJfybje().add(items[i].getDfybje()).doubleValue() == 0.0D) {
        line = line + NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-001056") + 
          NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000516");
        istr = false;
      }
      if (items[i].getJfbbje().add(items[i].getDfbbje()).doubleValue() == 0.0D) {
        line = line + NCLangRes4VoTransl.getNCLangRes().getStrByID("200602", "UPT200602-v35-000023") + 
          NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000516");
        istr = false;
      }
      if ((wldx != null) && (!"fj".equalsIgnoreCase(headvo.getDjdl())) && (!"sj".equalsIgnoreCase(headvo.getDjdl())) && 
        (!"hj".equalsIgnoreCase(headvo.getDjdl())) && (!"ss".equalsIgnoreCase(headvo.getDjdl())) && (
        (items[i].getAttributeValue(wldx) == null) || (items[i].getAttributeValue(wldx).toString().length() == 0))) {
        line = line + RES;
        istr = false;
      }

      if (istr) continue;
      sb.append(
        NCLangRes4VoTransl.getNCLangRes().getStrByID("200602", "UPT200602-v35-000020", null, new String[] { String.valueOf(i + 1) })).append(line)
        .append(NCLangRes4VoTransl.getNCLangRes()
        .getStrByID("200602", "UPT200602-v35-000021"));
    }

    if (sb.length() > 0) {
      showErrorMessage(sb.toString());
      return false;
    }
    return true;
  }

  protected boolean add()
  {
    if (((this.m_parent instanceof DjPanel)) && (this.m_djPanel.getM_TabIndex() == 1)) {
      ARAPDjCtlSearchNor ser = new ARAPDjCtlSearchNor(this.m_djPanel, this.m_arapDjPanel);
      ser.changeTab(1, true);
    }

    getBillCardPanelDj().updateValue();
    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();
    if (getDjSettingParam().getIsQc()) {
      String key = PubData.getQcCode(getDjSettingParam().getSyscode());
      try {
        Object val = MyClientEnvironment.getValue(getDjSettingParam().getPk_corp(), key, null);
        if ((val != null) && (ArapButtonStatUtil.QC_CLOSED.equalsIgnoreCase(val.toString().trim()))) {
          showErrorMessage(NCLangRes.getInstance().getStrByID("2008", "UPP2008-000099"));
          return false;
        }
      } catch (Exception localException1) {
      }
    }
    String strDjdl = dataBuffer.getCurrentDjdl();
    String strDjlxbm = dataBuffer.getCurrentDjlxbm();
    String strDjlxoid = dataBuffer.getCurrentDjlxoid();
    if ((getArapDjPanel1().getBillCardPanelDj() instanceof ArapDjBillCardPanel)) {
      ArapDjBillCardPanel djBillCardPanel = getArapDjPanel1()
        .getBillCardPanelDj();
      Integer intOldSysCode = djBillCardPanel.getTempletSysCode();
      if (intOldSysCode.intValue() != 0) {
        getArapDjPanel1().loadDjTemplet(new Integer(0), 
          getDjSettingParam().getPk_corp(), strDjdl, strDjlxbm);
      }
    }
    Logger.debug("strDjlxoid:" + strDjlxoid);
    this.m_arapDjPanel.getdjlxRef().setPK(strDjlxoid);

    if (this.m_arapDjPanel.getdjlxRef().getRefPK() == null) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000327"));

      return false;
    }

    DJZBVO currentDJZBVO = dataBuffer.getCurrentDJZBVO();

    getArapDjPanel1().setM_DjState(1);

    getBillCardPanelDj().getBillData().clearViewData();

    int mDjzbitemsIndex = 0;
    getArapDjPanel1().setDjzbitemsIndex(0);

    DJZBVO mDjzbvo = new DJZBVO();

    DJZBHeaderVO head = new DJZBHeaderVO();
    DJZBItemVO djzbitem = new DJZBItemVO();

    djzbitem.setDjzbitemsIndex(new Integer(mDjzbitemsIndex));
    djzbitem.setStatus(2);
    djzbitem.setDjbh("1");
    DJZBItemVO[] djzbitems = (DJZBItemVO[])null;
    djzbitems = new DJZBItemVO[1];
    djzbitems[0] = djzbitem;

    mDjzbvo.setChildrenVO(djzbitems);
    mDjzbvo.setParentVO(head);
    try
    {
      dataBuffer.setCurrentDJZBVO(mDjzbvo);
    }
    catch (Exception localException2)
    {
    }

    DjLXVO djlx = null;
    try
    {
      djlx = MyClientEnvironment.getDjlxVOByPK(getDjSettingParam().getPk_corp(), strDjlxoid);
    } catch (Exception e1) {
      Logger.error(e1.getMessage(), e1);
    }
    if ((djlx != null) && (djlx.getIschangedeptpsn() != null) && (djlx.getIschangedeptpsn().booleanValue())) {
      try {
        PsndocVO psnvo = Proxy.getIUserManageQuery().getPsndocByUserid(getDjSettingParam().getPk_corp(), getDjSettingParam().getPk_user());

        if (psnvo != null) {
          if (getBillCardPanelDj().getHeadItem("ywybm") != null)
            getBillCardPanelDj().setHeadItem("ywybm", psnvo.getPk_psndoc());
          if (getBillCardPanelDj().getHeadItem("deptid") != null)
            getBillCardPanelDj().setHeadItem("deptid", psnvo.getPk_deptdoc());
          BillEditEvent e = new BillEditEvent("", "", "ywybm", 0, 0);
          getArapDjPanel1().getBillCardPanelDj().getM_cardTreater().changeBodyYwy(e);
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }

    getBillCardPanelDj().setHeadItem("spzt", null);
    getBillCardPanelDj().setHeadItem("zzzt", "0");
    getBillCardPanelDj().setHeadItem("bbje", "0.00");
    getBillCardPanelDj().setHeadItem("fbje", "0.00");
    getBillCardPanelDj().setHeadItem("ybje", "0.00");
    getBillCardPanelDj().setHeadItem("pzglh", Integer.valueOf(getDjSettingParam().getSyscode()));

    getBillCardPanelDj().setHeadItem("djrq", 
      getDjSettingParam().getLoginDate());
    getBillCardPanelDj().setHeadItem("dwbm", 
      getDjSettingParam().getPk_corp());
    head.setDjdl(strDjdl);
    getBillCardPanelDj().setHeadItem("zgyfname", head.getAttributeValue("zgyfname"));

    if (strDjdl.equals("ss")) {
      getBillCardPanelDj().setHeadItem("begin_date", 
        getDjSettingParam().getLoginDate());
      getBillCardPanelDj().setHeadItem("end_date", 
        getDjSettingParam().getLoginDate());
    }

    getBillCardPanelDj().setHeadItem("effectdate", 
      getDjSettingParam().getLoginDate());

    getBillCardPanelDj().setHeadItem("djdl", strDjdl);
    getBillCardPanelDj().setHeadItem("lybz", head.getLybz());

    getBillCardPanelDj().setHeadItem("ywbm", head.getYwbm());
    getBillCardPanelDj().setHeadItem("djzt", "1");

    getBillCardPanelDj().setHeadItem("shrq", "");

    getBillCardPanelDj().setTailItem("shr", "");
    getBillCardPanelDj().setTailItem("enduser", "");
    getBillCardPanelDj().setTailItem("zdr", "");

    getBillCardPanelDj().setHeadItem("ywbm", strDjlxoid);
    if (((strDjdl.equalsIgnoreCase("sk")) || (strDjdl.equalsIgnoreCase("fk"))) && 
      (getDjSettingParam().getSyscode() == 2)) {
      getBillCardPanelDj().setHeadItem("wldx", new Integer(3));
    } else {
      BillItem itemWldx = getBillCardPanelDj().getHeadItem("wldx");
      if (itemWldx != null) {
        UIComboBox wldx = (UIComboBox)itemWldx
          .getComponent();

        if (wldx.getModel().getSize() == 0) {
          wldx.setSelectedIndex(-1);
        }
        else {
          wldx.setSelectedIndex(0);
        }
      }

    }

    getBillCardPanelDj().setHeadItem("djlxbm", strDjlxbm);

    if (getDjSettingParam().getIsQc()) {
      try {
        UFDate qyrq = getDjSettingParam().getQyrq2();

        getBillCardPanelDj().setHeadItem("djrq", qyrq.getDateBefore(1));
        getBillCardPanelDj().setHeadItem("effectdate", qyrq.getDateBefore(1));
      }
      catch (Throwable localThrowable) {
      }
      getBillCardPanelDj().setHeadItem("qcbz", 
        ArapConstant.UFBOOLEAN_TRUE);

      getBillCardPanelDj().setHeadItem("shrq", 
        getDjSettingParam().getLoginDate());
      getBillCardPanelDj().setTailItem("shr", 
        getDjSettingParam().getPk_user());
      getBillCardPanelDj().setHeadItem("djzt", "2");
    }
    else {
      getBillCardPanelDj().setHeadItem("qcbz", 
        ArapConstant.UFBOOLEAN_FALSE);
    }

    getBillCardPanelDj().setTailItem("lrr", 
      getDjSettingParam().getPk_user());
    getBillCardPanelDj().setTailItem("lybz", new Integer(0));

    getBillCardPanelDj().setHeadItem("djbh", "");

    getBillCardPanelDj().execHeadFormula(
      "djlxbm->getColvalue(arap_djlx,djlxbm,djlxoid,ywbm)");

    if (getBillCardPanelDj().getHeadItem("bzbm") != null) {
      String pk_currtype = (String)getBillCardPanelDj().getHeadItem("bzbm")
        .getValueObject();
      if ((pk_currtype == null) || (pk_currtype.trim().length() == 0)) {
        try
        {
          String bzbm = dataBuffer.getDefcurrency(
            getDjSettingParam().getPk_corp(), getDjSettingParam().getLocalCurrPK());

          ((UIRefPane)getBillCardPanelDj().getHeadItem(
            "bzbm").getComponent()).setPK(bzbm);

          getArapDjPanel1().getBillCardPanelDj().getM_cardTreater()
            .changeBzbm_H((String)
            getBillCardPanelDj().getHeadItem("bzbm")
            .getValueObject(), 
            (String)getBillCardPanelDj().getHeadItem("djrq")
            .getValueObject(), true);
        }
        catch (Exception eee) {
          Log.getInstance(getClass()).error(eee.getMessage(), eee);
        }
      }

    }

    setBBeditable();
    try
    {
      getArapDjPanel1().getBillCardPanelDj().getM_cardTreater()
        .resetAccount();
    } catch (Exception localException3) {
    }
    getArapDjPanel1().getBillCardPanelDj().getM_cardTreater().resetSSDjbh();
    if (getBillCardPanelDj().getHeadItem("fkyhzh") != null)
      getBillCardPanelDj().getHeadItem("fkyhzh").setEnabled(true);
    getArapDjPanel1().getBillCardPanelDj().getM_cardTreater().resetYHZH(null);
    getBillCardPanelDj().transferFocusTo(0);

    return true;
  }

  protected boolean del()
    throws Exception
  {
    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

    Vector delVouchid = new Vector();
    int delCount = 0;

    ResMessage resmessage = new ResMessage();

    if (this.m_djPanel.getM_TabIndex() == 1)
    {
      int selectedCount = 0;

      int count = 0;
      String resMessage = "";
      Vector v = new Vector();

      count = getBillListPanel1().getHeadBillModel().getRowCount();
      if (count < 1) {
        showErrorMessage(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000331"));
        return false;
      }
      List<DJZBVO> selectvos = this.m_djPanel.getListAllSelectedVOs();
      for (DJZBVO vo : selectvos)
      {
        selectedCount++;

        DJZBHeaderVO head = (DJZBHeaderVO)vo
          .getParentVO();
        if ((head.getLybz() != null) && (head.getLybz().intValue() == 9) && (!PubData.canDelXtDj(getDjSettingParam().getPk_corp(), getDjSettingParam().getSyscode())))
        {
          v.add(head.getDjbh() + ":" + StrResPorxy.getDjDelXtMsg());
        }
        else {
          DJZBItemVO[] items = (DJZBItemVO[])null;

          if (vo.getChildrenVO() == null) {
            try {
              items = doBsQueryItemAction(head.getVouchid());
            }
            catch (Exception e) {
              resMessage = NCLangRes.getInstance().getStrByID(
                "2006030102", "UPP2006030102-000332", 
                null, 
                new String[] { head.getDjbh() });
              v.addElement(resMessage);
              continue;
            }
            vo.setChildrenVO(items);
          }
          //add by hk
          if("1078".equals(pk_corp)||"1108".equals(pk_corp)){
        	  DJZBItemVO[] itemvos = (DJZBItemVO[]) vo.getChildrenVO();
        	  for (int i = 0; i < itemvos.length; i++) {
        		  if(!update_b_pk.contains(itemvos[i].getDdhh())){
        			  update_b_pk.add(itemvos[i].getDdhh());
        		  }
        	  }
    	  }
          //add by hk end
          try {
            DjCheckParamVO paramvo = new DjCheckParamVO();
            paramvo.setCurUser(getDjSettingParam().getPk_user());
            resmessage = this.m_arapDjPanel.getM_treater(vo)
              .befDelCheckVo(vo, paramvo);

            if (!resmessage.isSuccess) {
              resMessage = NCLangRes.getInstance()
                .getStrByID("2006030102", "UPP2006030102-000333", 
                null, 
                new String[] { head.getDjbh() }) + 
                resmessage.strMessage;
              v.addElement(resMessage);
            }
            else {
              VOCompress.CompressAll(vo);
              vo.m_isQr = false;
              if (getDjSettingParam().getIsQc()) {
                Proxy.getIArapBillPrivate().deleteBill(vo);
              } else if (head.getDjzt().intValue() == -99) {
                Proxy.getIArapBillPrivate().deleteTempDj(vo);
              }
              else if ("yt".equalsIgnoreCase(head.getDjdl()))
              {
                new Arap_ProcAction().runClass(this.m_djPanel, head.getDjlxbm(), "DELETE", vo, null);
                Proxy.getIArapTBBillPrivate().deleteTb(vo);
              }
              else {
                PfUtilClient.processAction(this.m_djPanel, 
                  "DELETE", head.getDjlxbm(), 
                  head.getDjrq().toString(), vo, null);
              }delVouchid.addElement(head.getVouchid());

              int idx = this.m_djPanel.getHeadRowInexByPK(head.getVouchid());

              if (idx != -1) {
                getBillListPanel1().getHeadBillModel().delLine(
                  new int[] { idx });
              }
              this.m_djPanel.getVoCache().removeVo(vo.getParentVO());
              resMessage = NCLangRes.getInstance().getStrByID(
                "2006030102", "UPP2006030102-000334", 
                null, 
                new String[] { head.getDjbh() });
              delCount++;
              v.addElement(resMessage);
              try
              {
                dataBuffer.removeDJZBVO(head.getVouchid());
              } catch (Exception localException1) {
              }
            }
          }
          catch (Exception e) {
            resMessage = NCLangRes.getInstance()
              .getStrByID("2006030102", "UPP2006030102-000333", 
              null, 
              new String[] { head.getDjbh() }) + 
              e.getMessage();
            v.addElement(resMessage);
          }
        }

      }

      if (selectedCount < 1) {
        showErrorMessage(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000335"));

        return false;
      }

      if (v.size() > 0)
      {
        ShenheLog f = new ShenheLog(this.m_djPanel);
        Double w = new Double((
          this.m_djPanel.getToolkit().getScreenSize().getWidth() - f.getWidth()) / 2.0D);
        Double h = new Double((
          this.m_djPanel.getToolkit().getScreenSize().getHeight() - f.getHeight()) / 2.0D);
        f.setLocation(w.intValue(), h.intValue());
        f.f_setText(v);
        f.showModal();
      }

      if (delCount < 1) {
        showErrorMessage(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000336"));
        return false;
      }

    }
    else
    {
      String oid = dataBuffer.getCurrentDJZBVOPK();
      if (!delOneDj()) {
        showErrorMessage(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000337"));
        showHintMessage(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000337"));
        return false;
      }

      delVouchid.addElement(dataBuffer.getCurrentDJZBVOPK());
      delCount = 1;

      int[] rows = new int[1];

      rows[0] = getBillListPanel1().getHeadTable().getSelectedRow();
      getBillListPanel1().getHeadBillModel().delLine(rows);
      this.m_djPanel.getListAllSelectedVOs().remove(dataBuffer.getCurrentDJZBVOPK());

      dataBuffer.removeDJZBVO(oid);
    }

    if (getBillListPanel1().getHeadBillModel().getRowCount() >= 1)
    {
      try
      {
        this.m_djPanel.changePage(0);
      }
      catch (Throwable e)
      {
        Logger.debug(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000338") + 
          e);
      }
    }
    else getBillListPanel1().getBodyBillModel().clearBodyData();

    this.m_parent.endPressBtn(this.bo);
    return true;
  }

  protected void addRow()
  {
    if ("hj".equalsIgnoreCase(this.m_parent.getDjDataBuffer().getCurrentDjdl())) {
      int rows = getBillCardPanelDj().getRowCount();
      double sum = 0.0D;

      for (int i = 0; i < rows; i++) {
        UFDouble jfje = (UFDouble)getBillCardPanelDj().getBodyValueAt(i, "jfybje");
        UFDouble dfje = (UFDouble)getBillCardPanelDj().getBodyValueAt(i, "dfybje");
        sum += (jfje == null ? 0.0D : jfje.doubleValue());
        sum -= (dfje == null ? 0.0D : dfje.doubleValue());
      }
      getBillCardPanelDj().addLine();

      if (sum > 0.0D) {
        UFDouble value = new UFDouble(sum);
        getBillCardPanelDj().setBodyValueAt(value, rows, "dfybje");
        BillEditEvent e = new BillEditEvent(getBillCardPanelDj().getBodyItem("dfybje"), value, "dfybje", rows, 1);
        try {
          this.m_arapDjPanel.getBillCardPanelDj().getM_cardTreater().changeBodyCurrency(e);
        } catch (Exception e1) {
          Logger.debug(e1.getMessage());
        }
      } else if (sum < 0.0D) {
        UFDouble value = new UFDouble(-sum);
        getBillCardPanelDj().setBodyValueAt(value, rows, "jfybje");
        BillEditEvent e = new BillEditEvent(getBillCardPanelDj().getBodyItem("jfybje"), value, "jfybje", rows, 1);
        try {
          this.m_arapDjPanel.getBillCardPanelDj().getM_cardTreater().changeBodyCurrency(e);
        } catch (Exception e1) {
          Logger.debug(e1.getMessage());
        }
      }
    } else {
      getBillCardPanelDj().addLine();
    }

    this.m_parent.endPressBtn(this.bo);
  }

  protected boolean cancel()
  {
    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

    if (getArapDjPanel1().getM_DjState() == 3)
    {
      try
      {
        DJZBVO currentDJZBVO = dataBuffer.getCurrentDJZBVO();
        DJZBVO oldDJZBVO = currentDJZBVO.getm_OldVO();
        getArapDjPanel1().setDj(oldDJZBVO);
      } catch (Throwable e) {
        Logger.debug(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000174") + 
          e);
      }
    } else {
      try {
        dataBuffer.setCurrentDJZBVO(null);
      }
      catch (Exception localException) {
      }
      clearDjhead();
    }

    getArapDjPanel1().setM_DjState(0);
    this.m_parent.endPressBtn(this.bo);
    return true;
  }

  protected void clearDjhead()
  {
    try
    {
      getBillCardPanelDj().getBillModel().clearBodyData();
      BillItem[] items = getBillCardPanelDj()
        .getHeadItems();

      for (int i = 0; i < items.length; i++) {
        getBillCardPanelDj().setHeadItem(items[i].getKey(), null);
      }

      getBillCardPanelDj().setHeadItem("sscause", null);
      getBillCardPanelDj().setHeadItem("fktjbm", null);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void copyDj()
    throws Exception
  {
    getBillCardPanelDj().transferFocusTo(0);

    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();
    DJZBVO currentDJZBVO = dataBuffer.getCurrentDJZBVO();
    DJZBVO djzbVOCloned = (DJZBVO)dataBuffer
      .getCurrentDJZBVO().clone();

    String strDjdl = dataBuffer.getCurrentDjdl();
    String strDjlxbm = dataBuffer.getCurrentDjlxbm();

    DJZBHeaderVO header = (DJZBHeaderVO)djzbVOCloned.getParentVO();
    header.setSxbz(new Integer(0));
    header.setSxr(null);
    header.setSxkjnd(null);
    header.setSxkjqj(null);
    header.setSxrq(null);

    header.setYhqrr(null);
    header.setYhqrkjnd(null);
    header.setYhqrkjqj(null);
    header.setYhqrrq(null);
    header.setDjbh(null);
    header.setFromSaveTotemporarily(UFBoolean.FALSE);
    header.setSscause(null);
    header.setZgyf(null);
    header.setM_xtMoreTimes(UFBoolean.TRUE);
    header.setIsselectedpay(null);
    header.setVouchid(null);
    header.setZgyf(new Integer(0));
    header.setFromSaveTotemporarily(UFBoolean.FALSE);

    header.setZzzt(new Integer(0));
    header.setPayman(null);
    dataBuffer.setCurrentDJZBVO(djzbVOCloned);
    if ((getArapDjPanel1().getBillCardPanelDj() instanceof ArapDjBillCardPanel)) {
      ArapDjBillCardPanel djBillCardPanel = getArapDjPanel1()
        .getBillCardPanelDj();
      Integer intOldSysCode = djBillCardPanel.getTempletSysCode();
      if (intOldSysCode.intValue() != 0) {
        getArapDjPanel1().loadDjTemplet(new Integer(0), 
          getDjSettingParam().getPk_corp(), strDjdl, strDjlxbm);
      }
      DJZBHeaderVO head = (DJZBHeaderVO)djzbVOCloned.getParentVO();
      head.setLybz(new Integer(0));
      getArapDjPanel1().setDj(djzbVOCloned);
    }
    getArapDjPanel1().setM_DjState(1);
    getBillCardPanelDj().setHeadItem("djbh", null);
    getBillCardPanelDj().setHeadItem("vouchid", null);
    getBillCardPanelDj().setHeadItem("spzt", null);
    getBillCardPanelDj().setHeadItem("sxbz", 
      new Integer(0));
    getBillCardPanelDj().setHeadItem("sxbzmc", null);
    getBillCardPanelDj().setHeadItem("jszxzf", 
      new Integer(0));
    getBillCardPanelDj().setHeadItem("zzzt", 
      new Integer(0));
    String strZzzt_MC = DJZBHeaderVO.getZzztMc(new Integer(
      0));
    getBillCardPanelDj().setHeadItem("zzzt_mc", strZzzt_MC);
    getBillCardPanelDj().setHeadItem("pzglh", Integer.valueOf(getDjSettingParam().getSyscode()));
    getBillCardPanelDj().setHeadItem("sxr", null);
    getBillCardPanelDj().setHeadItem("sxkjnd", null);
    getBillCardPanelDj().setHeadItem("sxkjqj", null);
    getBillCardPanelDj().setHeadItem("sxrq", null);
    getBillCardPanelDj().setHeadItem("yhqrr", null);
    getBillCardPanelDj().setHeadItem("yhqrkjnd", null);
    getBillCardPanelDj().setHeadItem("yhqrkjqj", null);
    getBillCardPanelDj().setHeadItem("yhqrrq", null);
    getBillCardPanelDj().setHeadItem("sscause", null);

    getBillCardPanelDj().setTailItem("lybz", new Integer(0));

    if (strDjdl.equals("ss"))
      getBillCardPanelDj().setHeadItem("lybz", new Integer(0));
    getBillCardPanelDj().setHeadItem("djzt", "1");
    getBillCardPanelDj().setTailItem("zdr", null);
    getBillCardPanelDj().setHeadItem("zdrq", null);
    if (!strDjdl.equals("ss"))
      getBillCardPanelDj().setHeadItem("sscause", null);
    getBillCardPanelDj().setHeadItem("fktjbm", null);
    getBillCardPanelDj().setHeadItem("yhqrr", null);
    getBillCardPanelDj().setHeadItem("effectdate", 
      getDjSettingParam().getLoginDate());
    getBillCardPanelDj().setTailItem("enduser", null);
    getBillCardPanelDj().setHeadItem("begin_date", 
      getDjSettingParam().getLoginDate());
    getBillCardPanelDj().setHeadItem("end_date", 
      getDjSettingParam().getLoginDate());

    if (getBillCardPanelDj().getHeadItem("isselectedpay") != null)
      getBillCardPanelDj().setHeadItem("isselectedpay", null);
    if (getBillCardPanelDj().getHeadItem("vouchertypeno") != null) {
      getBillCardPanelDj().setHeadItem("vouchertypeno", null);
    }

    if (getBillCardPanelDj().getHeadItem("payman") != null)
      getBillCardPanelDj().setHeadItem("payman", null);
    if (getBillCardPanelDj().getHeadItem("paydate") != null)
      getBillCardPanelDj().setHeadItem("paydate", null);
    for (int i = 0; i < getBillCardPanelDj().getBillModel().getRowCount(); i++) {
      if (getBillCardPanelDj().getBodyItem("payman") != null)
        getBillCardPanelDj().setBodyValueAt(null, i, "payman");
      if (getBillCardPanelDj().getBodyItem("paymanname") != null)
        getBillCardPanelDj().setBodyValueAt(null, i, "paymanname");
      if (getBillCardPanelDj().getBodyItem("paydate") != null)
        getBillCardPanelDj().setBodyValueAt(null, i, "paydate");
      if (getBillCardPanelDj().getBodyItem("djxtflagname") != null)
        getBillCardPanelDj().setBodyValueAt(null, i, "djxtflagname");
      getBillCardPanelDj().setBodyValueAt(null, i, "vouchid");
      getBillCardPanelDj().setBodyValueAt(null, i, "item_bill_pk");
      getBillCardPanelDj().setBodyValueAt(null, i, "item_bill_djbh");
      getBillCardPanelDj().setBodyValueAt(null, i, "othersysflag");
      getBillCardPanelDj().setBodyValueAt(null, i, "pausetransact");
      getBillCardPanelDj().setBodyValueAt(null, i, "tbbh");
      getBillCardPanelDj().setBodyValueAt(null, i, "ddlx");
      getBillCardPanelDj().setBodyValueAt(null, i, "ph");
      getBillCardPanelDj().setBodyValueAt(null, i, "jsfsbm");
      getBillCardPanelDj().setBodyValueAt(null, i, "djbh");
      getBillCardPanelDj().setBodyValueAt(null, i, "cksqsh");
      getBillCardPanelDj().setBodyValueAt(null, i, "xyzh");
      getBillCardPanelDj().setBodyValueAt(null, i, "ddhh");
      getBillCardPanelDj().setBodyValueAt(null, i, "fph");
      getBillCardPanelDj().setBodyValueAt(null, i, "tbbh");
      getBillCardPanelDj().setBodyValueAt(getDjSettingParam().getLoginDate(), i, "qxrq");
      BillItem clbh = getBillCardPanelDj().getBodyItem("clbh");
      if (clbh != null) {
        getBillCardPanelDj().setBodyValueAt(null, i, "clbh");
      }
      BillItem fphid = getBillCardPanelDj().getBodyItem("fphid");
      if (fphid != null) {
        getBillCardPanelDj().setBodyValueAt(null, i, "fphid");
      }
      BillItem ddhid = getBillCardPanelDj().getBodyItem("ddhid");
      if (ddhid != null) {
        getBillCardPanelDj().setBodyValueAt(null, i, "ddhid");
      }
      BillItem ckdid = getBillCardPanelDj().getBodyItem("ckdid");
      if (ckdid != null) {
        getBillCardPanelDj().setBodyValueAt(null, i, "ckdid");
      }
      BillItem ckdh = getBillCardPanelDj().getBodyItem("ckdh");
      if (ckdh != null) {
        getBillCardPanelDj().setBodyValueAt(null, i, "ckdh");
      }
      BillItem payflag = getBillCardPanelDj().getBodyItem("payflag");
      if (payflag != null) {
        getBillCardPanelDj().setBodyValueAt(null, i, "payflag");
      }
      BillItem payflagname = getBillCardPanelDj().getBodyItem("payflagname");
      if (payflagname != null) {
        getBillCardPanelDj().setBodyValueAt(null, i, "payflagname");
      }
      BillItem encode = getBillCardPanelDj().getBodyItem("encode");
      if (encode != null) {
        getBillCardPanelDj().setBodyValueAt(null, i, "encode");
      }

      BillItem isverifyfinished = getBillCardPanelDj().getBodyItem("isverifyfinished");
      if (isverifyfinished != null) {
        getBillCardPanelDj().setBodyValueAt(null, i, "isverifyfinished");
      }
      BillItem verifyfinisheddate = getBillCardPanelDj().getBodyItem("verifyfinisheddate");
      if (verifyfinisheddate != null) {
        getBillCardPanelDj().setBodyValueAt("3000-01-01", i, "verifyfinisheddate");
      }
      if (strDjdl.equals("ss")) {
        getBillCardPanelDj().setBodyValueAt(
          getDjSettingParam().getLoginDate(), i, "begin_date");
        getBillCardPanelDj().setBodyValueAt(
          getDjSettingParam().getLoginDate(), i, "end_date");
        getBillCardPanelDj().setBodyValueAt(null, i, "closer");
        getBillCardPanelDj().setBodyValueAt(null, i, "closer_name");
        getBillCardPanelDj().setBodyValueAt(null, i, "closedate");
        getBillCardPanelDj().setBodyValueAt(
          getBillCardPanelDj().getBodyValueAt(i, "jfybje"), i, 
          "ybye");
        getBillCardPanelDj().setBodyValueAt(
          getBillCardPanelDj().getBodyValueAt(i, "jfbbje"), i, 
          "bbye");
        getBillCardPanelDj().setBodyValueAt(
          getBillCardPanelDj().getBodyValueAt(i, "jffbje"), i, 
          "fbye");
      }
      else if ((strDjdl.equalsIgnoreCase("ys")) || 
        (strDjdl.equalsIgnoreCase("fk"))) {
        getBillCardPanelDj().setBodyValueAt(
          getBillCardPanelDj().getBodyValueAt(i, "jfybje"), i, 
          "ybye");
        getBillCardPanelDj().setBodyValueAt(
          getBillCardPanelDj().getBodyValueAt(i, "jfbbje"), i, 
          "bbye");
        getBillCardPanelDj().setBodyValueAt(
          getBillCardPanelDj().getBodyValueAt(i, "jffbje"), i, 
          "fbye"); } else {
        if ((!strDjdl.equalsIgnoreCase("yf")) && 
          (!strDjdl.equalsIgnoreCase("sk"))) continue;
        getBillCardPanelDj().setBodyValueAt(
          getBillCardPanelDj().getBodyValueAt(i, "dfybje"), i, 
          "ybye");
        getBillCardPanelDj().setBodyValueAt(
          getBillCardPanelDj().getBodyValueAt(i, "dfbbje"), i, 
          "bbye");
        getBillCardPanelDj().setBodyValueAt(
          getBillCardPanelDj().getBodyValueAt(i, "dffbje"), i, 
          "fbye");
      }

    }

    if (!getDjSettingParam().getIsQc())
    {
      getBillCardPanelDj().setHeadItem("shrq", null);
      getBillCardPanelDj().setTailItem("lrr", 
        getDjSettingParam().getPk_user());
      getBillCardPanelDj().setTailItem("shr", null);
      getBillCardPanelDj().setHeadItem("djrq", 
        getDjSettingParam().getLoginDate());
    }
    else {
      getBillCardPanelDj().setTailItem("shr", 
        getDjSettingParam().getPk_user());
      getBillCardPanelDj().setTailItem("lrr", 
        getDjSettingParam().getPk_user());
      getBillCardPanelDj().setHeadItem("shrq", 
        getDjSettingParam().getLoginDate());
      getBillCardPanelDj().setHeadItem("djrq", 
        getDjSettingParam().getLoginDate());
      try
      {
        UFDate qyrq = getDjSettingParam().getQyrq2();

        getBillCardPanelDj().setHeadItem("djrq", qyrq.getDateBefore(1));
      } catch (Throwable e) {
        Logger.debug(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000365") + 
          getDjSettingParam().getQyrq()[0] + 
          NCLangRes.getInstance().getStrByID(
          "2006030102", "UPP2006030102-000366") + 
          getDjSettingParam().getQyrq()[1] + 
          NCLangRes.getInstance().getStrByID(
          "2006030102", "UPP2006030102-000367") + 
          e + "\n**********************************\n");
      }

    }

    try
    {
      String pk_currtype = (String)getBillCardPanelDj().getHeadItem("bzbm")
        .getValueObject();
      String date = (String)getBillCardPanelDj().getHeadItem("djrq").getValueObject();
      getArapDjPanel1().getBillCardPanelDj().getM_cardTreater()
        .changeBzbm_H(pk_currtype, date, true);
    }
    catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000368") + 
        e);
    }

    getArapDjPanel1().getBillCardPanelDj().getM_cardTreater()
      .resetAccount();
    this.m_parent.endPressBtn(this.bo);
  }

  protected boolean delOneDj()
    throws Exception
  {
    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

    if (dataBuffer.getCurrentDJZBVO() == null) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000369"));

      return false;
    }
    String djzboid = null;
    DJZBHeaderVO djzbhead = (DJZBHeaderVO)dataBuffer
      .getCurrentDJZBVO().getParentVO();
    if (djzbhead == null) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000369"));

      return false;
    }
    djzboid = djzbhead.getVouchid();
    if ((djzboid == null) || (djzboid.length() < 1)) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000369"));

      return false;
    }

    try
    {
      DjCheckParamVO paramvo = new DjCheckParamVO();
      paramvo.setCurUser(getDjSettingParam().getPk_user());
      ResMessage resmessage = this.m_arapDjPanel.getM_treater(dataBuffer.getCurrentDJZBVO())
        .befDelCheckVo(dataBuffer.getCurrentDJZBVO(), paramvo);
      if (!resmessage.isSuccess) {
        showErrorMessage(resmessage.strMessage);
        return false;
      }
      if (getDjSettingParam().getIsQc()) {
        Proxy.getIArapBillPrivate().deleteBill(
          dataBuffer.getCurrentDJZBVO());
      } else if (djzbhead.getDjzt().intValue() == -99) {
        Proxy.getIArapBillPrivate().deleteTempDj(
          dataBuffer.getCurrentDJZBVO());
      }
      else if ("yt".equalsIgnoreCase(djzbhead.getDjdl()))
      {
        new Arap_ProcAction().runClass(this.m_djPanel, djzbhead.getDjlxbm(), "DELETE", dataBuffer.getCurrentDJZBVO(), null);
        Proxy.getIArapTBBillPrivate().deleteTb(dataBuffer.getCurrentDJZBVO());
      }
      else {
        PfUtilClient.processAction(getArapDjPanel1(), 
          "DELETE", djzbhead.getDjlxbm(), 
          djzbhead.getDjrq().toString(), dataBuffer.getCurrentDJZBVO(), 
          null);
      }
    } catch (Exception e) {
      if ((e instanceof ArapBusinessException)) {
        String errmsg = ((ArapBusinessException)e).m_ResMessage.strMessage;
        Logger.debug("删除单据出错:" + e);
        showErrorMessage(
          NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000370") + 
          errmsg);
        return false;
      }

      Logger.debug("删除单据出错:" + e);
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000371") + 
        e.getMessage());
      return false;
    }
    getArapDjPanel1().fireDelDj(getArapDjPanel1());
    try {
      dataBuffer.setCurrentDJZBVO(null);
    }
    catch (Exception localException1)
    {
    }
    getBillCardPanelDj().getBillModel().clearBodyData();
    getBillCardPanelDj().getBillData().clearViewData();

    BillItem[] items = getBillCardPanelDj()
      .getHeadShowItems();
    for (int i = 0; i < items.length; i++) {
      items[i].setValue("");
    }

    getArapDjPanel1().setM_DjState(0);
    return true;
  }

  protected void delLastLine()
  {
    int itemCount = 0;
    itemCount = getBillCardPanelDj().getBillModel().getRowCount();
    if (itemCount > 0) {
      int row = itemCount - 1;
      UFDouble jfybje = getBillCardPanelDj()
        .getBodyValueAt(row, "jfybje") == null ? 
        new UFDouble(0) : 
        (UFDouble)getBillCardPanelDj()
        .getBodyValueAt(row, "jfybje");

      UFDouble jfbbje = getBillCardPanelDj()
        .getBodyValueAt(row, "jfbbje") == null ? 
        new UFDouble(0) : 
        (UFDouble)getBillCardPanelDj()
        .getBodyValueAt(row, "jfbbje");

      UFDouble dfybje = getBillCardPanelDj()
        .getBodyValueAt(row, "dfybje") == null ? 
        new UFDouble(0) : 
        (UFDouble)getBillCardPanelDj()
        .getBodyValueAt(row, "dfybje");
      UFDouble dfbbje = getBillCardPanelDj()
        .getBodyValueAt(row, "dfbbje") == null ? 
        new UFDouble(0) : 
        (UFDouble)getBillCardPanelDj()
        .getBodyValueAt(row, "dfbbje");

      if (Math.abs(jfybje.doubleValue()) + 
        Math.abs(jfbbje.doubleValue()) + 
        Math.abs(dfbbje.doubleValue()) + Math.abs(dfybje
        .doubleValue()) <= 0.0D)
        getBillCardPanelDj().getBillModel().delLine(new int[] { row });
    }
  }

  protected void delRow()
  {
    if (getBillCardPanelDj().getBillModel().getRowCount() < 1) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000373"));

      return;
    }
    if (getBillCardPanelDj().getBillTable().getSelectedRow() < 0) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000374"));

      return;
    }

    getBillCardPanelDj().delLine();
    this.m_parent.endPressBtn(this.bo);
  }

  protected boolean edit()
  {
    getBillCardPanelDj().transferFocusTo(0);
    getBillCardPanelDj().updateValue();

    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();
    DJZBVO djzbvo = dataBuffer.getCurrentDJZBVO();

    String strNodeID = getDjSettingParam().getNodeID();
    DjCheckParamVO paramvo = new DjCheckParamVO();
    paramvo.setCurUser(getDjSettingParam().getPk_user());
    DJZBHeaderVO header = (DJZBHeaderVO)djzbvo.getParentVO();
    if ((new Integer(1).equals(header.getDjzt())) && ("0".equals(header.getSpzt())) && (header.getShr() != null) && (header.getShrq() != null)) {
      try
      {
        if (!Proxy.getIPFWorkflowQry().isCheckman(header.getVouchid(), getDjSettingParam().getPk_user()))
        {
          showErrorMessage(NCLangRes.getInstance()
            .getStrByID("2006", 
            "UPT2006-v50-000993"));
          return false;
        }
      }
      catch (Exception e1)
      {
        e1.printStackTrace();
        return false;
      }
    }
    ResMessage res = getArapDjPanel1().getM_treater(djzbvo).check_Edit(djzbvo, paramvo, "20100307".equals(strNodeID));
    if (!res.isSuccess) {
      MessageDialog.showErrorDlg(getArapDjPanel1(), 
        NCLangRes.getInstance().getStrByID("2006030102", 
        "UPP2006030102-000375"), 
        res.strMessage);
      return false;
    }
    getArapDjPanel1().setM_DjState(3);
    getBillCardPanelDj().getHeadItem("djbh").getComponent().setEnabled(
      false);

    String strDjdl = dataBuffer.getCurrentDjdl();
    try
    {
      if (!strDjdl.equals("hj"))
      {
        String pk_currtype = (String)getBillCardPanelDj().getHeadItem("bzbm")
          .getValueObject();
        String date = (String)getBillCardPanelDj().getHeadItem("djrq")
          .getValueObject();
        getArapDjPanel1().getBillCardPanelDj().getM_cardTreater()
          .changeBzbm_H(pk_currtype, date, false);
      }
    }
    catch (Exception e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000368") + 
        e);
    }

    setBBeditable();
    try
    {
      getArapDjPanel1().getBillCardPanelDj().getM_cardTreater()
        .resetAccount();
    }
    catch (Exception localException1)
    {
    }
    getBillCardPanelDj().getBillTable().getSelectionModel()
      .setSelectionInterval(0, 0);
    try {
      getArapDjPanel1().getBillCardPanelDj().getM_cardTreater()
        .rCResetBody(0, false, false);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
    getArapDjPanel1().getBillCardPanelDj().getM_cardTreater().resetSSDjbh();

    if (strDjdl.equalsIgnoreCase("ss")) {
      getBillCardPanelDj().getHeadItem("bzbm").setEnabled(false);
    }

    DJZBVO clonedDJZBVO = (DJZBVO)djzbvo.clone();
    djzbvo.setm_OldVO(clonedDJZBVO);
    this.m_parent.endPressBtn(this.bo);
    return true;
  }

  public ResMessage befYsfCheckVo(DJZBVO djzbvo)
  {
    ResMessage resmessage = new ResMessage();

    resmessage.isSuccess = true;

    DJZBHeaderVO head = null;

    head = (DJZBHeaderVO)djzbvo.getParentVO();
    DJZBItemVO[] items = (DJZBItemVO[])null;
    items = (DJZBItemVO[])djzbvo.getChildrenVO();
    int i = -1;
    UFBoolean isYsf = null;
    String djdl = null;

    if ((head.getIslocked() != null) && (!head.getIslocked().booleanValue())) {
      resmessage.isSuccess = false;
      resmessage.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000016");
      return resmessage;
    }

    for (i = 0; i < items.length; i++) {
      if ((items[i].getYbye() != null) && 
        (Math.abs(items[i].getYbye().doubleValue()) > 0.0D))
        break;
    }
    if (i == items.length) {
      resmessage.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000361");
      resmessage.isSuccess = false;
    }

    isYsf = head.getPrepay();
    djdl = head.getDjdl();

    if ((isYsf != null) && (isYsf.booleanValue())) {
      resmessage.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000362");
      resmessage.isSuccess = false;
    }

    if ((!djdl.equals("sk")) && (!djdl.equals("fk")))
    {
      resmessage.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000363");
      resmessage.isSuccess = false;
    }

    boolean bflag = false;
    try {
      bflag = MyClientEnvironment.isSettled(head.getDwbm(), head.getPzglh().intValue());
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      resmessage.isSuccess = false;
      resmessage.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000070");
      Log.getInstance(getClass()).warn("取结账信息出错");
      return resmessage;
    }
    if (bflag) {
      resmessage.isSuccess = false;
      resmessage.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000071");
      return resmessage;
    }

    return resmessage;
  }

  protected boolean on_Ysf()
    throws Exception
  {
    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();

    if (dataBuffer.getCurrentDJZBVO() == null) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000381"));

      return false;
    }
    String djzboid = null;
    DJZBHeaderVO djzbhead = (DJZBHeaderVO)dataBuffer
      .getCurrentDJZBVO().getParentVO();
    if (djzbhead == null) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000381"));

      return false;
    }
    djzboid = djzbhead.getVouchid();
    if ((djzboid == null) || (djzboid.length() < 1)) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000381"));

      return false;
    }
    ResMessage res = null;
    if (djzbhead.getPzglh().intValue() == 1)
    {
      String key = "AP17";
      djzbhead.m_IsContract = SysInit.getParaBoolean(djzbhead.getDwbm(), key).booleanValue();
    }

    try
    {
      ResMessage resmessage = 
        befYsfCheckVo(dataBuffer.getCurrentDJZBVO());
      if (!resmessage.isSuccess)
      {
        MessageDialog.showErrorDlg(getArapDjPanel1(), 
          NCLangRes.getInstance().getStrByID("2006030102", 
          "UPP2006030102-000375"), 
          resmessage.strMessage);
        return false;
      }
      VOCompress.CompressAll(dataBuffer.getCurrentDJZBVO());
      res = Proxy.getIArapBillPrivate()
        .ysfDj(dataBuffer.getCurrentDJZBVO());
    }
    catch (Exception e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000382") + 
        e);
      MessageDialog.showErrorDlg(getArapDjPanel1(), 
        NCLangRes.getInstance().getStrByID("2006030102", 
        "UPP2006030102-000375"), 
        NCLangRes.getInstance()
        .getStrByID("2006030102", 
        "UPP2006030102-000383") + 
        e.getMessage());
      return false;
    }

    djzbhead.setPrepay(ArapConstant.UFBOOLEAN_TRUE);
    djzbhead.setTs(new UFDateTime(res.m_Ts));
    getBillCardPanelDj().setHeadItem("prepay", ArapConstant.UFBOOLEAN_TRUE);
    getBillCardPanelDj().setHeadItem("ts", res.m_Ts);

    getArapDjPanel1().setM_DjState(0);
    return true;
  }

  protected boolean onAdd()
  {
    add();

    if ((getDjSettingParam().getSyscode() == 3) && 
      (!getDjSettingParam().getIsQc())) {
      this.m_djPanel.m_boNewByBudget.setVisible(true);
      this.m_djPanel.m_boNewByBudgetYT.setVisible(true);
      this.m_djPanel.m_boSplitSSItem.setVisible(true);
      updateButton(this.m_djPanel.m_boNewByBudget);
      updateButton(this.m_djPanel.m_boNewByBudgetYT);
      updateButton(this.m_djPanel.m_boSplitSSItem);
    }
    this.m_parent.endPressBtn(this.bo);
    //add by hk 
    String djdl = getBillCardPanelDj().getHeadItem("djdl").getValue();
    if("fk".equals(djdl)&&("1078".equals(pk_corp)||"1108".equals(pk_corp))){
    	int i = showOkCancelMessage("是否从应付单取数？");
	    if(i==1){
			if (getQryDlg().showModal() == QueryConditionClient.ID_CANCEL) {
				return true;
			}
			DJZBHeaderVO[] djzb=null;
			try {
				djzb = getdjzbheadvo();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stockListDialog = new DJZBHeaderListDialog(stockListDialog,djzb,DJZBItemVO.class.getName());
			stockListDialog.showModal(); // 打开窗口
			if(stockListDialog.getSelectVo().size()<=0){
				showErrorMessage("未选择数据。");
				return false;
			}
			List djzbvo = stockListDialog.getSelectVo();
			List djfbvo = new ArrayList();
			//查询子表VO数组
			for (int j = 0; j < djzbvo.size(); j++) {
				DJZBHeaderVO vo = (DJZBHeaderVO) djzbvo.get(j);
				String id = vo.getPrimaryKey();
				try {
					CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client.queryBodyAllData("D1",id, null);
					for (int k = 0; k < tmpBodyVo.length; k++) {
						djfbvo.add(tmpBodyVo[k]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			DJZBHeaderVO hvo = (DJZBHeaderVO)djzbvo.get(0);
			DJZBItemVO[] bvo = new DJZBItemVO[djfbvo.size()];
			UFDouble ybje = new UFDouble(0);
			UFDouble bbje = new UFDouble(0);
			for (int j = 0; j < bvo.length; j++) {
				bvo[j]=(DJZBItemVO) djfbvo.get(j);
				if("1078".equals(pk_corp)){
					bvo[j].setSzxmid("1078A21000000000077O");
				}else if("1108".equals(pk_corp)){
					bvo[j].setSzxmid("11081110000000000KUL");
				}
				bvo[j].setJffbje(bvo[j].getDffbje());
				bvo[j].setDffbje(new UFDouble(0));
				ybje = ybje.add(bvo[j].getYbye());
				bbje = bbje.add(bvo[j].getBbye());
				bvo[j].setJfbbje(bvo[j].getDfbbje());
				bvo[j].setJfbbsj(bvo[j].getDfbbsj());
				bvo[j].setJfshl(bvo[j].getDfshl());
				bvo[j].setJfybje(bvo[j].getDfybje());
				bvo[j].setJfybsj(bvo[j].getDfybsj());
				bvo[j].setJfybwsje(bvo[j].getDfybwsje());
				bvo[j].setWbfbbje(bvo[j].getDfbbwsje());
//				bvo[j].setJfybje(bvo[j].getYbye());
//				bvo[j].setJfbbje(bvo[j].getBbye());
				bvo[j].setDfbbje(new UFDouble(0));
				bvo[j].setDfbbsj(new UFDouble(0));
				bvo[j].setDfshl(new UFDouble(0));
				bvo[j].setDfybje(new UFDouble(0));
				bvo[j].setDfybsj(new UFDouble(0));
				bvo[j].setDfybwsje(new UFDouble(0));
				bvo[j].setDfbbwsje(new UFDouble(0));
				bvo[j].setJsfsbm("D1");
				bvo[j].setDdlx(bvo[j].getVouchid());
				bvo[j].setDdhh(bvo[j].getFb_oid());
				bvo[j].setVouchid("");
				bvo[j].setFb_oid("");
				bvo[j].setStatus(VOStatus.NEW);
			}
			hvo.setYbje(ybje);
			hvo.setBbje(bbje);
			hvo.setLrr(ClientEnvironment.getInstance().getUser().getPrimaryKey());
			hvo.setDjbh("");
			hvo.setDjrq(ClientEnvironment.getInstance().getDate());
			hvo.setDjdl("fk");
			hvo.setShr("");
			hvo.setShrq(null);
			AggregatedValueObject billVO = new DJZBVO();
			billVO.setParentVO(hvo);
			billVO.setChildrenVO(bvo);
			getBillCardPanelDj().setBillValueVO(billVO);
			getBillCardPanelDj().getBillModel().execLoadFormula();
	    }
    }
    //add by hk end
    return true;
  }
  //add by hk
  IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator.getInstance().lookup(
		  IUAPQueryBS.class.getName());
  DJZBHeaderListDialog stockListDialog = null;
  List update_b_pk = new ArrayList<String>();
  private DJZBHeaderVO[] getdjzbheadvo() throws Exception {
	  DJZBHeaderVO[] queryresult = null;
	  String whereSql = getQryDlg().getWhereSQL()==null?"1=1":getQryDlg().getWhereSQL();
	  StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct h.* ") 
		.append(" from arap_djzb h left join arap_djfb b on b.vouchid = h.vouchid and nvl(b.dr,0)=0 ") 
		.append(" where "+whereSql+" and nvl(h.djzt,0)=2 and nvl(b.ybye,0) > 0 and (h.dwbm = '1078' or h.dwbm = '1108') and nvl(h.dr,0)=0 and h.djdl = 'yf'  ");
		
		List list = (List) sessionManager.executeQuery(sql.toString(),
					new BeanListProcessor(DJZBHeaderVO.class));
		queryresult = new DJZBHeaderVO[list.size()];
		for (int i = 0; i < list.size(); i++) {
			queryresult[i]=(DJZBHeaderVO)list.get(i);
		}
		return queryresult;
  }
  QueryConditionDlg queryDLG = null;
	public QueryConditionDlg getQryDlg() {
		if( queryDLG==null )
		{
			queryDLG = new QueryConditionDlg(queryDLG);
			queryDLG.hideNormal();
			queryDLG.setTempletID("1078A210000000010M6L");
			queryDLG.setIsWarningWithNoInput(false);	
		}
		return queryDLG;
	}
	//add by hk end

  public boolean onClosing() {
    if ((this.m_djPanel == null) || (this.m_djPanel.getCurrWorkPage() == ArapBillWorkPageConst.CARDPAGE)) {
      boolean isEditting = getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_EDIT;
      boolean idNew = getArapDjPanel1().getM_DjState() == ArapBillWorkPageConst.WORKSTAT_NEW;
      if ((isEditting) || (idNew)) {
        switch (MessageDialog.showYesNoCancelDlg(this.m_djPanel, null, NCLangRes.getInstance()
          .getStrByID("common", "UCH001"), 2)) {
        case 4:
          return save();
        case 8:
          cancel();
          break;
        case 5:
        case 6:
        case 7:
        default:
          return false;
        }
      }
    }
    return true;
  }

  protected boolean onDelete() {
    try {
      if (showOkCancelMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000384")) != 
        1) {
        showHintMessage(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000385"));
        return true;
      }
      getArapDjPanel1().setM_DjState(2);
      del();
      getArapDjPanel1().setM_DjState(0);
      updateD1();
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000386") + 
        e);
      getArapDjPanel1().setM_DjState(0);
      return false;
    }

    return true;
  }

  protected boolean onYsf()
  {
    try
    {
      on_Ysf();

      ARAPDjDataBuffer dataBuffer = getDjDataBuffer();
      String djzboid = dataBuffer.getCurrentDJZBVOPK();
      dataBuffer.putDJZBVO(djzboid, dataBuffer.getCurrentDJZBVO());
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000387") + 
        e);
    }
    this.m_parent.endPressBtn(this.bo);
    return true;
  }

  public boolean save()
  {
    getBillCardPanelDj().stopEditing();
    try
    {
      delLastLine();
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
    DJZBVO djzbvo1 = getDjDataBuffer().getCurrentDJZBVO();

    int itemCount = getBillCardPanelDj().getBillModel().getRowCount();
    if (itemCount <= 0) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000184"));

      return false;
    }
    try {
      getBillCardPanelDj().dataNotNullValidate();
    } catch (ValidationException e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000388") + 
        e.getMessage());
      return false;
    }

    ArapDjBillCardPanel djBillCard = (ArapDjBillCardPanel)getBillCardPanelDj();
    try
    {
      TempletUtil.adjustCurrencyDecimalDigit(djBillCard, 
        getDjSettingParam().getPk_corp());
    } catch (BusinessException e1) {
      Logger.debug(e1.getMessage());
      return false;
    }
    DJZBVO djzbvo = (DJZBVO)getBillCardPanelDj()
      .getBillValueVO("nc.vo.ep.dj.DJZBVO", 
      "nc.vo.ep.dj.DJZBHeaderVO", "nc.vo.ep.dj.DJZBItemVO");

    djzbvo.setm_OldVO(djzbvo1);
    DjVOTreaterAid.convertDJZBVO(djzbvo);
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();

    if (!checkNullBefSave(djzbvo)) {
      return false;
    }
    DjCheckParamVO paramVO = new DjCheckParamVO();
    try {
      paramVO.setPoperiod("PO", (String[])MyClientEnvironment.getValue(((DJZBHeaderVO)djzbvo.getParentVO()).getDwbm(), "POBegin", null));
      paramVO.setPoperiod("AR", (String[])MyClientEnvironment.getValue(((DJZBHeaderVO)djzbvo.getParentVO()).getDwbm(), "ARBegin", null));
      paramVO.setPoperiod("AP", (String[])MyClientEnvironment.getValue(((DJZBHeaderVO)djzbvo.getParentVO()).getDwbm(), "APBegin", null));
      paramVO.setPoperiod("EP", (String[])MyClientEnvironment.getValue(((DJZBHeaderVO)djzbvo.getParentVO()).getDwbm(), "EPBegin", null));
    } catch (Exception e) {
      Log.getInstance(getClass()).warn(e.getMessage(), e);
    }
    paramVO.setCurUser(getDjSettingParam().getPk_user());
    DjLXVO djlx;
    try
    {
      djlx = (DjLXVO)MyClientEnvironment.getValue(head.getDwbm(), "DJLX", head.getDjlxbm());
    }
    catch (Exception e)
    {
      
      Log.getInstance(getClass()).warn(e.getMessage(), e);
      showHintMessage(e.getMessage());
      showErrorMessage(e.getMessage());
      return false;
    }
    //DjLXVO djlx;
    head.setJszxzf(djlx.getIsjszxzf());
    if (!this.m_arapDjPanel.getM_treater(djzbvo).befSaveCheckAVO(djzbvo, paramVO)) {
      this.m_parent.showErrorMessage(StrResPorxy.getSaveErrMsg());
      return false;
    }
    //add by hk
    if("1078".equals(pk_corp)||"1108".equals(pk_corp)){
    	int row = getBillCardPanelDj().getBillModel().getRowCount();
    	for (int i = 0; i < row; i++) {
    		if(getBillCardPanelDj().getBillModel().getValueAt(i, "ddhh") == null){
    			continue;
    		}
    		String lyhzj = getBillCardPanelDj().getBillModel().getValueAt(i, "ddhh").toString();//来源行主键
    		update_b_pk.add(lyhzj);
    		StringBuffer qureyjesql = new StringBuffer();
  		  	qureyjesql.append(" select ybye from arap_djfb where fb_oid = '"+lyhzj+"'; ");
  		    try {
				Map result = (Map) sessionManager.executeQuery(qureyjesql.toString(), new MapProcessor());
				UFDouble ybye = new UFDouble(result.get("ybye").toString());
				UFDouble jfybje = new UFDouble(getBillCardPanelDj().getBillModel().getValueAt(i, "jfybje").toString());
				if(jfybje.compareTo(ybye) > 0){
					showErrorMessage("第"+(i+1)+"行数据超额支付"+jfybje.sub(ybye)+"！");
					return true;
				}
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    //add by hk end
    if (getArapDjPanel1().getM_DjState() == 1)
    {
      if (saveNewBill(djzbvo, true)) {
        this.m_parent.endPressBtn(this.bo);
        updateD1();
        return true;
      }
      return true;
    }

    if (getArapDjPanel1().getM_DjState() == 3)
    {
      boolean a =saveEditBill(djzbvo);
    	updateD1();
      return a;
    }
    showErrorMessage(
      NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000389"));

    return false;
  }

  //add by hk
  String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
  private void updateD1() {
	  if((!"1078".equals(pk_corp))||(!"1108".equals(pk_corp))){
		  return;
	  }
	  if(update_b_pk.size()<=0||update_b_pk == null){
		  return;
	  }
	  for (int i = 0; i < update_b_pk.size(); i++) {
		  String lyhzj = update_b_pk.get(i).toString();//来源行主键
		  StringBuffer qureysql = new StringBuffer();
		  qureysql.append(" select je.ybzje ybzje,fb.dfybje dfybje,je.bbzje bbzje,fb.dfbbje dfbbje from ( ") 
		  .append(" select sum(b.jfybje) ybzje,sum(b.jfbbje) bbzje,b.ddhh from arap_djfb b ") 
		  .append(" left join arap_djzb h  ") 
		  .append(" on b.vouchid = h.vouchid  ") 
		  .append(" and nvl(h.dr,0)=0 ") 
		  .append(" where b.ddhh = '"+lyhzj+"' and nvl(b.dr,0)=0 and h.djdl = 'fk' and (h.dwbm = '1078' or h.dwbm = '1108') ") 
		  .append(" group by b.ddhh ") 
		  .append(" ) je  ") 
		  .append(" left join arap_djfb fb on je.ddhh = fb.fb_oid ");
		  try {
			Map result = (Map) sessionManager.executeQuery(qureysql.toString(), new MapProcessor());
			UFDouble ybye = new UFDouble(result.get("dfybje").toString()).sub(new UFDouble(result.get("ybzje").toString()));
			UFDouble bbye = new UFDouble(result.get("dfbbje").toString()).sub(new UFDouble(result.get("bbzje").toString()));
			StringBuffer updatesql = new StringBuffer();
			updatesql.append(" update arap_djfb set ybye = "+ybye+",bbye = "+bbye+" where fb_oid = '"+lyhzj+"' ") ;
			IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
			ipubdmo.executeUpdate(updatesql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
      }
  }

public boolean save(boolean needLock)
    throws Exception
  {
    getBillCardPanelDj().stopEditing();
    try
    {
      delLastLine();
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }

    int itemCount = getBillCardPanelDj().getBillModel().getRowCount();
    if (itemCount <= 0) {
      showErrorMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000184"));

      return false;
    }
    try {
      getBillCardPanelDj().dataNotNullValidate();
    } catch (ValidationException e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      showHintMessage(
        NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000388") + 
        e.getMessage());
      throw new Exception(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000388") + 
        e.getMessage());
    }

    TempletUtil.adjustCurrencyDecimalDigit(getBillCardPanelDj(), 
      getDjSettingParam().getPk_corp());

    DJZBVO djzbvo = getDjDataBuffer().getCurrentDJZBVO();
    try
    {
      djzbvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel).befSaveChangeAVo(djzbvo);
    } catch (Exception e) {
      String errMsg = NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000377") + 
        e.getMessage();
      showErrorMessage(errMsg);
      return false;
    }

    if (getArapDjPanel1().getM_DjState() == 1) {
      return saveNewBill(djzbvo, needLock);
    }
    if (getArapDjPanel1().getM_DjState() == 3)
    {
      return false;
    }
    showErrorMessage(
      NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000389"));

    return false;
  }

  protected void setBBeditable()
  {
    BillItem jfbbje = getBillCardPanelDj().getBodyItem(
      "jfbbje");

    jfbbje.setEnabled(true);

    BillItem dfbbje = getBillCardPanelDj().getBodyItem(
      "dfbbje");

    dfbbje.setEnabled(true);
  }

  public boolean treatEvent(ButtonObject btObj)
  {
    super.treatEvent(btObj);
    if (btObj == this.m_parent.m_boAdd)
    {
      onAdd();
      return true;
    }if (btObj == this.m_parent.m_boCopy)
    {
      try
      {
        copyDj();
      } catch (Throwable e) {
        Logger.debug(NCLangRes.getInstance()
          .getStrByID("2006030102", "UPP2006030102-000394") + 
          e);
      }
      return true;
    }if (btObj == this.m_parent.m_boDelDj)
    {
      onDelete();
      return true;
    }if (btObj == this.m_parent.m_boEdit)
    {
      edit();
      return true;
    }if (btObj == this.m_parent.m_boAddRow)
    {
      addRow();
      return true;
    }if (btObj == this.m_parent.m_boDelRow)
    {
      delRow();
      return true;
    }if (btObj == this.m_parent.m_boYsf)
    {
      onYsf();
      return true;
    }
    if (btObj == this.m_parent.m_boSave)
    {
      save();

      return true;
    }if (btObj == this.m_parent.m_boCancel)
    {
      cancel();
      return true;
    }
    if (btObj == this.m_parent.m_boTempSave) {
      onTempSave();
      return true;
    }
    return false;
  }

  protected boolean prepareSave(DJZBVO djzbvo)
  {
    DJZBHeaderVO headvo = (DJZBHeaderVO)djzbvo.getParentVO();
    try
    {
      getArapDjPanel1().getBillCardPanelDj().getM_cardTreater()
        .setHeadJe();

      headvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel).befSaveChangeHVo(djzbvo, 
        getArapDjPanel1().getM_DjState());
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000395") + 
        e);
      showErrorMessage(e.getMessage());
      return false;
    }
    if (!getDjSettingParam().getIsQc())
      headvo.setSpzt(null);
    djzbvo.setParentVO(headvo);

    DjCheckParamVO paramVO = new DjCheckParamVO();
    try {
      paramVO.setPoperiod("PO", (String[])MyClientEnvironment.getValue(headvo.getDwbm(), "POBegin", null));
      paramVO.setPoperiod("AR", (String[])MyClientEnvironment.getValue(headvo.getDwbm(), "ARBegin", null));
      paramVO.setPoperiod("AP", (String[])MyClientEnvironment.getValue(headvo.getDwbm(), "APBegin", null));
      paramVO.setPoperiod("EP", (String[])MyClientEnvironment.getValue(headvo.getDwbm(), "EPBegin", null));
    }
    catch (Exception ee) {
      Log.getInstance(getClass()).warn(ee.getMessage(), ee);
    }
    paramVO.setCurUser(getDjSettingParam().getPk_user());
    try
    {
      this.m_arapDjPanel.getM_treater(djzbvo).befSaveCheckHVo(headvo, paramVO);
    } catch (Exception e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000396") + 
        e);
      showErrorMessage(e.getMessage());
      return false;
    }

    DJZBItemVO[] itemsvo = (DJZBItemVO[])null;
    try {
      itemsvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel)
        .befSaveChangeBVo(djzbvo);
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000397") + 
        e);
      showErrorMessage(e.getMessage());
      return false;
    }
    djzbvo.setChildrenVO(itemsvo);
    try
    {
      this.m_arapDjPanel.getM_treater(djzbvo).befSaveCheckBVo(djzbvo, paramVO);
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000398") + 
        e);
      showErrorMessage(e.getMessage());
      return false;
    }

    try
    {
      djzbvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel).befSaveChangeAVo(djzbvo);
    } catch (Exception e) {
      String errMsg = NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000377") + 
        e.getMessage();
      showErrorMessage(errMsg);
      return false;
    }
    if (getArapDjPanel1().getM_DjState() == 9999)
    {
      headvo.setSpzt("1");
    }VOCompress.CompressAll(djzbvo);
    return true;
  }

  protected boolean saveNewBill(DJZBVO djzbvo, boolean needLock) {
    Log.getInstance(getClass()).warn("****************3*********");
    if (!prepareSave(djzbvo))
      return false;
    long begin = System.currentTimeMillis();
    DJZBHeaderVO headvo = (DJZBHeaderVO)djzbvo.getParentVO();
    try
    {
      djzbvo = doBsSaveAction(djzbvo);

      headvo = (DJZBHeaderVO)djzbvo.getParentVO();
      if ((getDjSettingParam().getIsQc()) || (djzbvo.getCheckState() == 1)) {
        headvo.setDjzt(new Integer(2));

        headvo.setSpzt("1");
      } else if (djzbvo.getCheckState() == 2)
      {
        headvo.setSpzt("0");
      }
    } catch (Exception e) {
      ArapBusinessException E = null;
      String errmessgae = "";
      if ((e instanceof ArapBusinessException)) {
        E = (ArapBusinessException)e;
        showErrorMessage(E.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
        showHintMessage(E.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
        return false;
      }if ((e instanceof RemoteException))
      {
        RemoteException re = (RemoteException)e;

        if ((re.detail instanceof ArapBusinessException))
        {
          E = (ArapBusinessException)re.detail;
          showErrorMessage(E.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
          showHintMessage(E.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
          return false;
        }
      }
      else if ((e instanceof BusinessException))
      {
        if ((e.getMessage() != null) && (e.getMessage().toUpperCase().indexOf("SHENHE_SQ") >= 0)) {
          errmessgae = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000378");
        }
      }
      Logger.debug("增加单据保存时出错:" + e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000400") + errmessgae + "\n" + e.getMessage());
      return false;
    }
    if ((djzbvo.m_Resmessage.SysErrMsg != null) && (djzbvo.m_Resmessage.SysErrMsg.trim().length() != 0)) {
      showWarningMessage(djzbvo.m_Resmessage.SysErrMsg);
    }

    headvo = (DJZBHeaderVO)djzbvo.getParentVO();

    String[] hItems = { "vouchid", "djmboid", "djlxbm", "djbh", "spzt", "ts", "djzt", "shr", "shrq", "effectdate" };
    String[] bItems = { "fb_oid", "vouchid", "ywbm", "djbh", "dwbm", "pk_arap_fengcun", "shenpi" };
    updateCardPanel1(hItems, bItems, djzbvo);
    getArapDjPanel1().fireSaveNewDj(getArapDjPanel1());

    getDjSettingParam().setPreDjInfo(headvo.getPrimaryKey(), getDjSettingParam().getPk_user());
    updateCardPanel2(djzbvo);

    long end2 = System.currentTimeMillis();
    Logger.debug("调用保存用时:" + (end2 - begin));
    return true;
  }

  public boolean onTempSave() {
    ArapDjBillCardPanel djBillCard = (ArapDjBillCardPanel)getBillCardPanelDj();
    try
    {
      TempletUtil.adjustCurrencyDecimalDigit(djBillCard, getDjSettingParam().getPk_corp());
    } catch (BusinessException e) {
      Logger.debug(e.getMessage());
      return false;
    }
    DJZBVO djzbvo = (DJZBVO)getBillCardPanelDj().getBillValueVO("nc.vo.ep.dj.DJZBVO", "nc.vo.ep.dj.DJZBHeaderVO", "nc.vo.ep.dj.DJZBItemVO");
    if ((djzbvo.getChildrenVO() == null) || (djzbvo.getChildrenVO().length == 0)) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("200602", "UPT200602-v35-000019"));
      return false;
    }
    if (getArapDjPanel1().getM_DjState() == 1)
    {
      return tempSaveNew(djzbvo, true);
    }if (getArapDjPanel1().getM_DjState() == 3)
    {
      return tempEdit(djzbvo);
    }
    showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000389"));
    return false;
  }

  private boolean tempEdit(DJZBVO djzbvo)
  {
    DJZBItemVO[] itemsvo = (DJZBItemVO[])null;

    ResMessage res_hycz = new ResMessage();
    res_hycz.isSuccess = false;
    ResMessage res_Zjcz = new ResMessage();
    res_Zjcz.isSuccess = false;
    boolean fromSave = false;
    DJZBHeaderVO headvo = (DJZBHeaderVO)djzbvo.getParentVO();
    if (headvo.getDjzt().intValue() == 1) {
      fromSave = true;
    }
    try
    {
      headvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel).befSaveChangeHVo(djzbvo, getArapDjPanel1().getM_DjState());
    } catch (Throwable e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000186"));
      return false;
    }
    if ((headvo.getZzzt() != null) && (DJZBVOConsts.FTS_RECEIVER_SUCCESS.intValue() == headvo.getZzzt().intValue()))
    {
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-V50-000535"));
      showHintMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-V50-000535"));
      return true;
    }

    headvo.setDjzt(new Integer(-99));
    headvo.setZzzt(new Integer(0));
    try
    {
      itemsvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel)
        .befSaveChangeBVo(djzbvo);
      djzbvo.setChildrenVO(getDjDataBuffer().getEditDjzbItemsVO(itemsvo));
    } catch (Throwable e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000186"));
      return false;
    }

    djzbvo.setm_OldVO(getDjDataBuffer().getCurrentDJZBVO().getm_OldVO());
    try
    {
      DJZBVOTreator.slimDJVOByCompare(djzbvo.getm_OldVO(), djzbvo);
      djzbvo = Proxy.getIArapBillPrivate().editTempDj(djzbvo, new UFBoolean(fromSave));

      headvo = (DJZBHeaderVO)djzbvo.getParentVO();
      Log.getInstance(getClass()).debug("******set pzglh1*****" + headvo.getPzglh());
    } catch (Exception e) {
      ArapBusinessException SE = null;
      if ((e instanceof ArapBusinessException)) {
        SE = (ArapBusinessException)e;
        showErrorMessage(SE.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
        showHintMessage(SE.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
        return false;
      }if ((e instanceof RemoteException))
      {
        RemoteException re = (RemoteException)e;

        if ((re.detail instanceof ArapBusinessException)) {
          SE = (ArapBusinessException)re.detail;
          showErrorMessage(SE.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
          showHintMessage(SE.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
          return false;
        }
      }
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000379") + e.getMessage());
      return false;
    }
    itemsvo = (DJZBItemVO[])djzbvo.getChildrenVO();
    String[] hItems = { "spzt", "ts", "enduser", "djzt", "shr", "lrr", "shrq" };
    String[] bItems = { "fb_oid", "ywbm", "djbh", "dwbm", "pk_arap_fengcun", "shenpi" };
    updateCardPanel1(hItems, bItems, djzbvo);
    List lst = new ArrayList();
    for (int row = 0; row < itemsvo.length; row++) {
      if (warnSFKUpdated(itemsvo[row]))
        lst.add(new Integer(row));
    }
    getArapDjPanel1().fireSaveEditDj(getArapDjPanel1());
    updateCardPanel2(djzbvo);

    if (lst.size() > 0)
      showWarningMessage(StrResPorxy.getDjSfkxyChangedMsg());
    this.m_parent.endPressBtn(this.bo);
    return true;
  }

  private boolean tempSaveNew(DJZBVO djzbvo, boolean needLock)
  {
    DJZBHeaderVO headvo = (DJZBHeaderVO)djzbvo.getParentVO();
    try {
      getArapDjPanel1().getBillCardPanelDj().getM_cardTreater().setHeadJe();
      headvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel).befSaveChangeHVo(djzbvo, getArapDjPanel1().getM_DjState());
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000395") + e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000186"));
      return false;
    }
    if (!getDjSettingParam().getIsQc())
      headvo.setSpzt(null);
    djzbvo.setParentVO(headvo);

    DJZBItemVO[] itemsvo = (DJZBItemVO[])null;
    try {
      itemsvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel).befSaveChangeBVo(djzbvo);
    } catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance()
        .getStrByID("2006030102", "UPP2006030102-000397") + e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000186"));
      return false;
    }

    headvo.setDjzt(new Integer(-99));
    if ((itemsvo != null) && (itemsvo.length == 0))
      itemsvo = (DJZBItemVO[])null;
    djzbvo.setChildrenVO(itemsvo);
    try
    {
      djzbvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel).befSaveChangeAVo(djzbvo);
    } catch (Exception e) {
      String errMsg = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377") + e.getMessage();
      showErrorMessage(errMsg);
      return false;
    }
    if (getArapDjPanel1().getM_DjState() == 9999)
    {
      headvo.setSpzt("1");
    }VOCompress.CompressAll(djzbvo);
    try {
      djzbvo = Proxy.getIArapBillPrivate().saveTempDj(djzbvo);
      headvo = (DJZBHeaderVO)djzbvo.getParentVO();
    } catch (Exception e) {
      ArapBusinessException E = null;
      String errmessgae = "";
      if ((e instanceof ArapBusinessException)) {
        E = (ArapBusinessException)e;
        showErrorMessage(E.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
        showHintMessage(E.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
        return false;
      }if ((e instanceof RemoteException))
      {
        RemoteException re = (RemoteException)e;

        if ((re.detail instanceof ArapBusinessException))
        {
          E = (ArapBusinessException)re.detail;
          showErrorMessage(E.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
          showHintMessage(E.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
          return false;
        }
      }
      else if ((e instanceof BusinessException))
      {
        if (e.getMessage().toUpperCase().indexOf("SHENHE_SQ") >= 0) {
          errmessgae = NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000378");
        }
      }
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000400") + errmessgae + "\n" + e.getMessage());
      return false;
    }

    headvo = (DJZBHeaderVO)djzbvo.getParentVO();

    String[] hItems = { "vouchid", "djmboid", "djlxbm", "djbh", "spzt", "ts", "djzt", "shr", "shrq" };
    String[] bItems = { "fb_oid", "vouchid", "ywbm", "djbh", "dwbm", "pk_arap_fengcun", "shenpi" };
    updateCardPanel1(hItems, bItems, djzbvo);
    getArapDjPanel1().fireSaveNewDj(getArapDjPanel1());

    getDjSettingParam().setPreDjInfo(headvo.getPrimaryKey(), getDjSettingParam().getPk_user());
    updateCardPanel2(djzbvo);
    this.m_parent.endPressBtn(this.bo);
    return true;
  }

  protected boolean saveEditBill(DJZBVO djzbvo)
  {
    DJZBItemVO[] itemsvo = (DJZBItemVO[])null;

    ResMessage res_hycz = new ResMessage();
    res_hycz.isSuccess = false;
    ResMessage res_Zjcz = new ResMessage();
    res_Zjcz.isSuccess = false;
    DJZBHeaderVO headvo = (DJZBHeaderVO)djzbvo.getParentVO();
    Integer originDjzt = headvo.getDjzt();
    try {
      headvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel).befSaveChangeHVo(djzbvo, 
        getArapDjPanel1().getM_DjState());
    } catch (Throwable e) {
      showErrorMessage(e.getMessage());
      return false;
    }

    DjCheckParamVO paramVO = new DjCheckParamVO();
    try {
      paramVO.setPoperiod("PO", (String[])MyClientEnvironment.getValue(headvo.getDwbm(), "POBegin", null));
      paramVO.setPoperiod("AR", (String[])MyClientEnvironment.getValue(headvo.getDwbm(), "ARBegin", null));
      paramVO.setPoperiod("AP", (String[])MyClientEnvironment.getValue(headvo.getDwbm(), "APBegin", null));
      paramVO.setPoperiod("EP", (String[])MyClientEnvironment.getValue(headvo.getDwbm(), "EPBegin", null));
    } catch (Exception ee) {
      Log.getInstance(getClass()).warn(ee.getMessage(), ee);
    }
    paramVO.setCurUser(getDjSettingParam().getPk_user());
    try
    {
      this.m_arapDjPanel.getM_treater(djzbvo).befSaveCheckHVo(headvo, paramVO);
    } catch (Throwable e) {
      showErrorMessage(e.getMessage());
      return false;
    }
    try
    {
      itemsvo = new DjVOChanger(this.m_parent, this.m_arapDjPanel)
        .befSaveChangeBVo(djzbvo);
      djzbvo.setChildrenVO(getDjDataBuffer().getEditDjzbItemsVO(itemsvo));
    } catch (Throwable e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000186") + e.getMessage());
      return false;
    }

    try
    {
      this.m_arapDjPanel.getM_treater(djzbvo).befSaveCheckBVo(djzbvo, paramVO);
    }
    catch (Throwable e) {
      Logger.debug(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000398") + e.getMessage());
      showErrorMessage(e.getMessage());
      return false;
    }
    djzbvo.setm_OldVO(getDjDataBuffer().getCurrentDJZBVO().getm_OldVO());
    djzbvo.m_isQr = getDjSettingParam().getIsQr();

    if (originDjzt.intValue() == -99)
      djzbvo.getParentVO().setAttributeValue("djzt", originDjzt);
    try
    {
      DJZBVOTreator.slimDJVOByCompare(djzbvo.getm_OldVO(), djzbvo);

      djzbvo = doBsEditAction(djzbvo);

      headvo = (DJZBHeaderVO)djzbvo.getParentVO();
      if (djzbvo.getCheckState() == 1) {
        headvo.setDjzt(new Integer(2));
        headvo.setSpzt("1");
      }
      else if (djzbvo.getCheckState() == 2)
      {
        headvo.setSpzt("0");
      }
    } catch (Exception e) {
      ArapBusinessException SE = null;
      if ((e instanceof ArapBusinessException)) {
        SE = (ArapBusinessException)e;
        showErrorMessage(SE.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
        showHintMessage(SE.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
        return false;
      }if ((e instanceof RemoteException))
      {
        RemoteException re = (RemoteException)e;

        if ((re.detail instanceof ArapBusinessException)) {
          SE = (ArapBusinessException)re.detail;
          showErrorMessage(SE.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
          showHintMessage(SE.m_ResMessage.strMessage + NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000377"));
          return false;
        }
      }
      Logger.debug("修改单据保存时出错:" + e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000379") + e.getMessage());
      return false;
    }
    if ((djzbvo.m_Resmessage.SysErrMsg != null) && (djzbvo.m_Resmessage.SysErrMsg.trim().length() != 0)) {
      showWarningMessage(djzbvo.m_Resmessage.SysErrMsg);
    }

    itemsvo = (DJZBItemVO[])djzbvo.getChildrenVO();
    if (headvo.getEffectdate() == null)
      headvo.setEffectdate(itemsvo[0].getQxrq());
    String[] hItems = { "djbh", "spzt", "ts", "enduser", "djzt", "shr", "lrr", "shrq", "effectdate" };
    String[] bItems = { "fb_oid", "ywbm", "djbh", "dwbm", "pk_arap_fengcun", "shenpi" };
    updateCardPanel1(hItems, bItems, djzbvo);
    List lst = new ArrayList();
    for (int row = 0; row < itemsvo.length; row++) {
      if (warnSFKUpdated(itemsvo[row]))
        lst.add(new Integer(row));
    }
    getArapDjPanel1().fireSaveEditDj(getArapDjPanel1());
    updateCardPanel2(djzbvo);

    if (lst.size() > 0)
      showWarningMessage(StrResPorxy.getDjSfkxyChangedMsg());
    this.m_parent.endPressBtn(this.bo);
    return true;
  }

  private void updateCardPanel1(String[] hItems, String[] bItems, DJZBVO djzbvo) {
    List lst = new ArrayList();
    DJZBHeaderVO headvo = (DJZBHeaderVO)djzbvo.getParentVO();
    DJZBItemVO[] itemsvo = (DJZBItemVO[])djzbvo.getChildrenVO();
    if (headvo.getQcbz().booleanValue())
      getBillCardPanelDj().setHeadItem("sxbzmc", headvo.getAttributeValue("sxbzmc"));
    int i = 0; for (int size = hItems.length; i < size; i++) {
      if ("spzt".equalsIgnoreCase(hItems[i]))
        getBillCardPanelDj().setHeadItem("spzt", DJZBVOUtility.getUIStringVerifyingStatus(headvo.getSpzt()));
      getBillCardPanelDj().setHeadItem(hItems[i], headvo.getAttributeValue(hItems[i]));
    }
    for (int row = 0; (itemsvo != null) && (row < itemsvo.length); row++) {
       i = 0; for (int size = bItems.length; i < size; i++)
        getBillCardPanelDj().setBodyValueAt(itemsvo[row].getAttributeValue(bItems[i]), row, bItems[i]);
      itemsvo[row].m_oldjfbbje = itemsvo[row].getJfbbje();
      itemsvo[row].setOldybye(itemsvo[row].getYbye());
      itemsvo[row].setOldfbye(itemsvo[row].getFbye());
      itemsvo[row].setOldbbye(itemsvo[row].getBbye());
      itemsvo[row].setItem_bill_pk_old(itemsvo[row].getItem_bill_pk());
      if (warnSFKUpdated(itemsvo[row]))
        lst.add(new Integer(row));
    }
    try {
      getDjDataBuffer().saveCurrentDJZBVO(djzbvo);
    }
    catch (Exception localException) {
    }
  }

  private void updateCardPanel2(DJZBVO djzbvo) {
    if ((djzbvo.m_Resmessage.strMessage != null) && (djzbvo.m_Resmessage.strMessage.trim().length() > 0)) {
      showWarningMessage(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000121") + djzbvo.m_Resmessage.strMessage);
      showHintMessage(djzbvo.m_Resmessage.strMessage);
    }
    getArapDjPanel1().setM_DjState(0);
    getBillCardPanelDj().getBillTable().getSelectionModel().setSelectionInterval(0, 0);
    try {
      getArapDjPanel1().selectedSzxm(0);
    }
    catch (Throwable localThrowable) {
    }
  }

  private boolean warnSFKUpdated(DJZBItemVO item) {
    if ((item == null) || (item.getIsSFKXYChanged() == null) || (!item.getIsSFKXYChanged().booleanValue()))
      return false;
    String[] attrs = item.getM_strChangedAtts();
    if (attrs == null) return true;
    List lst = new ArrayList();
    int i = 0; for (int size = attrs.length; i < size; i++) {
      lst.add(attrs[i]);
    }

    return (lst.contains("effectdate")) || (lst.contains("bbhl")) || (lst.contains("fbhl")) || 
      (lst.contains("jfybje")) || (lst.contains("dfybje")) || (lst.contains("jfshl")) || 
      (lst.contains("dfshl")) || (lst.contains("sfkxyh"));
  }

  protected abstract DJZBVO doBsSaveAction(DJZBVO paramDJZBVO)
    throws Exception;

  protected abstract DJZBVO doBsEditAction(DJZBVO paramDJZBVO)
    throws Exception;

  protected abstract DJZBItemVO[] doBsQueryItemAction(String paramString)
    throws Exception;
}