package nc.ui.arap.pubdj;

import java.util.HashMap;

import javax.swing.JComponent;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.itf.fi.pub.Currency;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.arap.global.PubData;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.bd.CorpBO_Client;
import nc.ui.bd.b39.PhaseRefModel;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.ep.dj.ARAPDjDataBuffer;
import nc.ui.ep.dj.ARAPDjSettingParam;
import nc.ui.ep.dj.IArapRefModel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.global.ResMessage;
import nc.vo.bd.CorpVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public abstract class DjCardPanelTreater
{
  protected ArapDjBillCardPanel m_panel = null;
  private String m_OldYwymcWhere = null;

  public DjCardPanelTreater(ArapDjBillCardPanel cardPanel)
  {
    this.m_panel = cardPanel;
  }

  public void changeBHjksmc(BillEditEvent e)
    throws Exception
  {
  }

  public void resetOrdercust(BillEditEvent e)
    throws Exception
  {
  }

  public void resetSFkyhzh(BillEditEvent e)
    throws Exception
  {
  }

  public void changeYhzhEidt(BillEditEvent e)
    throws Exception
  {
  }

  public void changeKsbm_cl2(BillEditEvent e)
    throws Exception
  {
  }

  public void changeBodyYwy(BillEditEvent e)
  {
    resetYHZH(e);
    String[] formulas = (String[])null;

    ARAPDjDataBuffer dataBuffer = this.m_panel.getDjDataBuffer();

    String strDjdl = dataBuffer.getCurrentDjdl();
    if ((e.getPos() == 0) && (e.getKey().equals("ywybm"))) {
      String ks = this.m_panel.getHeadItem("ksbm_cl").getValue();
      if ((ks != null) && (ks.length() > 0))
        return;
      if ((strDjdl.equalsIgnoreCase("sk")) || (strDjdl.equalsIgnoreCase("ys")) || 
        (strDjdl.equalsIgnoreCase("sj"))) {
        formulas = new String[] { 
          "htaid->getColValue(bd_psndoc,pk_psnbasdoc,pk_psndoc,ywybm)", 
          "fkyhzh->getColValue(bd_psnaccbank,pk_accbank,pk_psnbasdoc,htaid)", 
          "fkyhmc->getColValue(bd_accbank,bankname,pk_accbank,fkyhzh)" };

        this.m_panel.getBillData().execHeadFormulas(formulas);
      }
    } else if ((e.getPos() == 1) && (e.getKey().equals("ywymc"))) {
      String ks = (String)this.m_panel.getBodyValueAt(e.getRow(), "ksmc");
      if ((ks != null) && (ks.length() > 0)) {
        return;
      }
      if ((strDjdl.equalsIgnoreCase("sk")) || (strDjdl.equalsIgnoreCase("ys")) || 
        (strDjdl.equalsIgnoreCase("sj"))) {
        formulas = new String[] { 
          "htaid->getColValue(bd_psndoc,pk_psnbasdoc,pk_psndoc,ywybm)", 
          "fkyhzh->getColValue(bd_psnaccbank,pk_accbank,pk_psnbasdoc,htaid)", 
          "bankacc_fk->getColValue(bd_accbank,bankacc,pk_accbank,fkyhzh)", 
          "fkyhmc->getColValue(bd_accbank,bankname,pk_accbank,fkyhzh)" };

        this.m_panel.getBillData().execBodyFormulas(e.getRow(), 
          formulas);
      }
    }
    if ((e.getPos() == 0) && (e.getKey().equals("ywybm"))) {
      resetYHZH(e);
      String ks = this.m_panel.getHeadItem("ksbm_cl").getValue();
      if ((ks != null) && (ks.length() > 0))
        return;
      if ((strDjdl.equalsIgnoreCase("fk")) || (strDjdl.equalsIgnoreCase("yf")) || 
        (strDjdl.equalsIgnoreCase("fj"))) {
        formulas = new String[] { 
          "htaid->getColValue(bd_psndoc,pk_psnbasdoc,pk_psndoc,ywybm)", 
          "skyhzh->getColValue(bd_psnaccbank,pk_accbank,pk_psnbasdoc,htaid)", 
          "skyhmc->getColValue(bd_accbank,bankname,pk_accbank,skyhzh)" };
        this.m_panel.getBillData().execHeadFormulas(formulas);
      }
    } else if ((e.getPos() == 1) && (e.getKey().equals("ywymc")))
    {
      String ks = (String)this.m_panel.getBodyValueAt(e.getRow(), "ksmc");
      if ((ks != null) && (ks.length() > 0)) {
        return;
      }
      if ((strDjdl.equalsIgnoreCase("fk")) || (strDjdl.equalsIgnoreCase("yf")) || 
        (strDjdl.equalsIgnoreCase("fj"))) {
        formulas = new String[] { 
          "htaid->getColValue(bd_psndoc,pk_psnbasdoc,pk_psndoc,ywybm)", 
          "skyhzh->getColValue(bd_psnaccbank,pk_accbank,pk_psnbasdoc,htaid)", 
          "bankacc->getColValue(bd_accbank,bankacc,pk_accbank,skyhzh)", 
          "skyhmc->getColValue(bd_accbank,bankname,pk_accbank,skyhzh)" };

        this.m_panel.getBillData().execBodyFormulas(e.getRow(), 
          formulas);
      }
    }
    resetYHZH(e);
  }

  public void changeKsmc(BillEditEvent e)
    throws Exception
  {
  }

  public void changeBodyJshj(BillEditEvent e)
    throws Exception
  {
  }

  public void changeAccidName(BillEditEvent e)
    throws Exception
  {
  }

  public void changeAccountid(BillEditEvent e)
    throws Exception
  {
    ARAPDjDataBuffer dataBuffer = this.m_panel.getDjDataBuffer();

    String strDjdl = dataBuffer.getCurrentDjdl();
    try {
      if ((e.getKey().equalsIgnoreCase("accountid")) && (e.getPos() == 0))
      {
        String[] formulas = (String[])null;
        if ((strDjdl.equals("ws")) || (strDjdl.equals("sj")) || 
          (strDjdl.equals("sk"))) {
          formulas = new String[] { 
            "zhbzbm->getColValue(bd_accid,pk_currtype,pk_accid,accountid)", 
            "skyhzh->getColValue(bd_accid,pk_accbank,pk_accid,accountid)", 
            "skyhmc->getColvalue(bd_accbank,bankname,pk_accbank,skyhzh)" };

          if ((this.m_panel.getHeadItem("accountid")
            .getValue() == null) || 
            (this.m_panel.getHeadItem("accountid")
            .getValue().trim().length() < 2))
          {
            this.m_panel.getHeadItem("skyhzh").setEnabled(
              true);
          }
          else this.m_panel.getHeadItem("skyhzh").setEnabled(
              false);
        }
        else
        {
          formulas = new String[] { 
            "zhbzbm->getColValue(bd_accid,pk_currtype,pk_accid,accountid)", 
            "fkyhzh->getColValue(bd_accid,pk_accbank,pk_accid,accountid)", 
            "fkyhmc->getColvalue(bd_accbank,bankname,pk_accbank,fkyhzh)" };

          if ((this.m_panel.getHeadItem("accountid")
            .getValue() == null) || 
            (this.m_panel.getHeadItem("accountid")
            .getValue().trim().length() < 2))
          {
            this.m_panel.getHeadItem("fkyhzh").setEnabled(
              true);
          }
          else this.m_panel.getHeadItem("fkyhzh").setEnabled(
              false);

        }

        this.m_panel.getBillData().execHeadFormulas(formulas);
      }

    }
    catch (Exception ee)
    {
      Logger.debug(ee);
    }
  }

  public void changeDdh(BillEditEvent e)
    throws Exception
  {
    if ((e.getPos() == 1) && (e.getKey().equals("ddh"))) {
      String ddh = this.m_panel.getBodyValueAt(e.getRow(), "ddh") == null ? null : 
        this.m_panel.getBodyValueAt(e.getRow(), "ddh")
        .toString();

      String value = CallPoFunAgent.getCtCodeByPoCode(this.m_panel.getDjSettingParam()
        .getPk_corp(), ddh);
      this.m_panel
        .setBodyValueAt(value, e.getRow(), "contractno");
    }
  }

  public void changeHtByBm(BillEditEvent e, boolean isRowChange)
  {
    String pk_currtype = (String)this.m_panel.getBodyValueAt(e.getRow(), 
      "bzbm");
    if (this.m_panel.getBodyItem("htmc") != null) {
      UIRefPane ref = (UIRefPane)this.m_panel
        .getBodyItem("htmc").getComponent();

      ref.setWhereString(" pk_currency='" + pk_currtype + "' and status in ('A','B' )");
    }
  }

  public void changeZy(BillEditEvent e)
    throws Exception
  {
    Logger.debug("key= " + e.getKey());
    if ((e.getKey().equals("zy")) && (e.getPos() == 1))
    {
      UIRefPane zy_ref = (UIRefPane)this.m_panel
        .getBodyItem("zy").getComponent();

      Object oldvalue = zy_ref.getText();
      Object oldname = zy_ref.getRefName();

      this.m_panel
        .setBodyValueAt(
        (oldname == null) || 
        (oldname.toString().trim().length() < 1) ? 
        oldvalue : 
        oldname, e.getRow(), "zy");
    }
  }

  public void changeSkyhzh(BillEditEvent e)
    throws Exception
  {
    try
    {
      String[] formulas = (String[])null;
      if ((e.getKey().equalsIgnoreCase("skyhzh")) && (e.getPos() == 0))
      {
        formulas = new String[] { "skyhmc->getColvalue(bd_accbank,bankname,pk_accbank,skyhzh)" };
      }
      else if ((e.getKey().equals("fkyhzh")) && (e.getPos() == 0)) {
        formulas = new String[] { "fkyhmc->getColvalue(bd_accbank,bankname,pk_accbank,fkyhzh)" };
      }
      else
        return;
      this.m_panel.getBillData().execHeadFormulas(formulas);
    } catch (Exception ee) {
      Logger.debug(ee);
    }
  }

  public void changeNotetype(BillEditEvent e)
    throws Exception
  {
    if ((e.getPos() == 0) && (e.getKey().equals("notetype")))
    {
      this.m_panel
        .execHeadFormula(
        "pj_jsfs->getColvalue(arap_note_type,settle_style,notetype_oid,notetype)");
    }
  }

  public void changeBItemBzbm(BillEditEvent e, boolean isSetHL)
    throws Exception
  {
    if (((e.getPos() == 1) && (e.getKey().equals("bzmc"))) || (e.getKey().equals("djrq"))) {
      if (this.m_panel.getBodyValueAt(e.getRow(), "bzmc") == null) return;
      UFDouble bbhl = (UFDouble)this.m_panel.getBodyValueAt(e.getRow(), "bbhl");
      UFDouble fbhl = (UFDouble)this.m_panel.getBodyValueAt(e.getRow(), "fbhl");
      String pk_currtype = this.m_panel.getBodyValueAt(e.getRow(), "bzbm").toString();
      String pk_frctype = this.m_panel.getDjSettingParam().getFracCurrPK();
      String pk_localtype = this.m_panel.getDjSettingParam().getLocalCurrPK();
      Logger.debug(
        "-------------------pk_currtype------------------:" + pk_currtype);
      boolean is_Bbhl = true;
      boolean is_Fbhl = true;
      if (pk_currtype == null)
        return;
      String date = this.m_panel.getHeadItem("djrq").getValue();
      if (this.m_panel.getDjSettingParam().getIsQc())
      {
        date = this.m_panel.getDjSettingParam().getQyrq2().toString();
      }Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000731") + date);
      int RateDig = 0;

      RateDig = Currency.getCurrDigit(pk_currtype);

      int FbRateDig = 0;
      int BbRateDig = 0;
      try
      {
        if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac() != null)
        {
          if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac().booleanValue())
          {
            if (pk_currtype.equals(pk_localtype))
            {
              is_Bbhl = false;
              is_Fbhl = false;
              bbhl = new UFDouble(1.0D);
              fbhl = new UFDouble(0.0D);
              BbRateDig = RateDig;
            } else if (pk_currtype.equals(pk_frctype)) {
              is_Bbhl = true;
              is_Fbhl = false;
              fbhl = new UFDouble(1.0D);

              BbRateDig = Currency.getRateDigit(this.m_panel.getDjSettingParam().getPk_corp(), pk_frctype, pk_localtype);
              bbhl = Currency.getRate(this.m_panel.getDjSettingParam().getPk_corp(), pk_frctype, pk_localtype, date);
            }
            else {
              is_Bbhl = true;
              is_Fbhl = true;

              FbRateDig = Currency.getRateDigit(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, pk_frctype);

              BbRateDig = Currency.getRateDigit(this.m_panel.getDjSettingParam().getPk_corp(), pk_frctype, pk_localtype);
              try {
                fbhl = Currency.getRate(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, pk_frctype, date);
                bbhl = Currency.getRate(this.m_panel.getDjSettingParam().getPk_corp(), pk_frctype, pk_localtype, date);
              } catch (Exception e2) {
                Logger.debug(
                  NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000732") + 
                  e2);
              }
            }
          }
          else
          {
            is_Fbhl = false;
            if (pk_currtype.equals(this.m_panel.getDjSettingParam().getLocalCurrPK()))
            {
              is_Bbhl = false;
              bbhl = new UFDouble(1.0D);
              BbRateDig = RateDig;
            } else if (pk_currtype.equals(this.m_panel.getDjSettingParam().getFracCurrPK()))
            {
              is_Bbhl = false;

              BbRateDig = Currency.getRateDigit(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, pk_frctype);
            }
            else {
              is_Bbhl = true;

              BbRateDig = Currency.getRateDigit(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, pk_localtype);
              try {
                bbhl = Currency.getRate(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, pk_localtype, date);
                bbhl = bbhl == null ? new UFDouble(0.0D) : bbhl;
              } catch (Exception e2) {
                Logger.debug(
                  NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000732") + 
                  e2);
              }
            }
          }
        }
      } catch (Exception ee) {
        Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000733") + ee);
      }
      try {
        setCurrency(e.getRow(), pk_currtype);
      } catch (Exception ex) {
        Logger.error(ex.getMessage(), ex);
      }

      if (this.m_panel.getBodyItem("jfbbje") != null)
        this.m_panel.getBodyItem("jfbbje").setEnabled(is_Bbhl);
      if (this.m_panel.getBodyItem("dfbbje") != null)
        this.m_panel.getBodyItem("dfbbje").setEnabled(is_Bbhl);
      if (this.m_panel.getBodyItem("jffbje") != null)
        this.m_panel.getBodyItem("jffbje").setEnabled(is_Fbhl);
      if (this.m_panel.getBodyItem("dffbje") != null) {
        this.m_panel.getBodyItem("dffbje").setEnabled(is_Fbhl);
      }
      this.m_panel.getBodyItem("bbhl").setEnabled(is_Bbhl);
      this.m_panel.getBodyItem("fbhl").setEnabled(is_Fbhl);

      BillItem btBbhl = this.m_panel.getBodyItem("bbhl");
      btBbhl.setDecimalDigits(BbRateDig);
      BillItem btFbhl = this.m_panel.getBodyItem("fbhl");
      btFbhl.setDecimalDigits(FbRateDig);
      if (isSetHL)
      {
    	  //add by xiaolong_fan.取期间汇率
          if(!getParentCorpCode().equalsIgnoreCase("10395"))
      	{
      		bbhl = Currency.getRate(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, pk_localtype, date);
      	}
      	else
      	{
      		bbhl=getMonthRate(pk_currtype,pk_localtype);
      	}
        bbhl = bbhl == null ? new UFDouble(0.0D) : bbhl;
        //end by xiaolong_fan.
        this.m_panel.setBodyValueAt(fbhl.setScale(FbRateDig, 4), e.getRow(), "fbhl");
        this.m_panel.setBodyValueAt(bbhl.setScale(BbRateDig, 4), e.getRow(), "bbhl");
        BillEditEvent e2 = new BillEditEvent(
          this.m_panel.getBodyItem("fbhl"), fbhl, "fbhl", e.getRow(), 1);
        try {
          changeBodyCurrency(e2);
        } catch (Exception eee) {
          Log.getInstance(getClass()).error(eee.getMessage(), eee);
        }
        this.m_panel.adjustHeadDecimalDigit();
      }
      else {
        bbhl = (UFDouble)this.m_panel
          .getBodyValueAt(e.getRow(), "bbhl");
        this.m_panel.setBodyValueAt(
          bbhl.setScale(BbRateDig, 4), e.getRow(), "bbhl");
        fbhl = (UFDouble)this.m_panel
          .getBodyValueAt(e.getRow(), "fbhl") == null ? 
          new UFDouble(0) : (UFDouble)
          this.m_panel.getBodyValueAt(e.getRow(), "fbhl");
        this.m_panel.setBodyValueAt(
          fbhl.setScale(FbRateDig, 4), e.getRow(), "fbhl");
      }
    }
  }

  public void changeBzbm(BillEditEvent e)
    throws Exception
  {
    if ((e.getKey().equals("bzbm")) || 
      ((e.getPos() == 1) && (e.getKey().equals("bzmc"))) || 
      (e.getKey().equals("djrq"))) {
      UFDouble bbhl = new UFDouble(0.0D);
      UFDouble fbhl = new UFDouble(0.0D);
      String pk_currtype = "";
      if (e.getPos() == 0)
      {
        pk_currtype = this.m_panel.getHeadItem("bzbm")
          .getValue();
      }
      else
        pk_currtype = ((UIRefPane)this.m_panel
          .getBodyItem(e.getKey()).getComponent()).getRefPK();
      if (pk_currtype == null) {
        pk_currtype = (String)this.m_panel.getBodyValueAt(e.getRow(), e.getKey());
      }
      String pk_frctype = this.m_panel.getDjSettingParam().getFracCurrPK();
      String pk_localtype = this.m_panel.getDjSettingParam().getLocalCurrPK();
      Logger.debug(
        "-------------------pk_currtype------------------:" + 
        pk_currtype);
      boolean is_Bbhl = true;
      boolean is_Fbhl = true;

      if (pk_currtype == null) {
        return;
      }
      String date = this.m_panel.getHeadItem("djrq").getValue();
      if (this.m_panel.getDjSettingParam().getIsQc())
      {
        date = this.m_panel.getDjSettingParam().getQyrq2().toString();
      }Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000731") + date);

      String pkcorp = this.m_panel.getDjSettingParam().getPk_corp();

      int RateDig = Currency.getRateDigit(pkcorp, pk_currtype);

      int FbRateDig = 0;
      int BbRateDig = 0;
      try
      {
        if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac() != null)
        {
          if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac().booleanValue())
          {
            if (pk_currtype.equals(pk_localtype))
            {
              is_Bbhl = false;
              is_Fbhl = false;
              bbhl = new UFDouble(1.0D);
              fbhl = new UFDouble(0.0D);
              BbRateDig = RateDig;
            } else if (pk_currtype.equals(pk_frctype)) {
              is_Bbhl = true;
              is_Fbhl = false;
              fbhl = new UFDouble(1.0D);

              BbRateDig = Currency.getRateDigit(pkcorp, pk_frctype, pk_localtype);
              bbhl = Currency.getRate(pkcorp, pk_frctype, pk_localtype, date);
            }
            else {
              is_Bbhl = true;
              is_Fbhl = true;

              BbRateDig = Currency.getRateDigit(pkcorp, pk_currtype, pk_frctype);

              BbRateDig = Currency.getRateDigit(pkcorp, pk_frctype, pk_localtype);
              try {
                fbhl = Currency.getRate(pkcorp, pk_currtype, pk_frctype, date);
                bbhl = Currency.getRate(pkcorp, pk_frctype, pk_localtype, date);
              } catch (Exception e2) {
                Logger.debug(
                  NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000732") + 
                  e2);
              }
            }
          }
          else {
            is_Fbhl = false;
            if (pk_currtype.equals(this.m_panel.getDjSettingParam().getLocalCurrPK()))
            {
              is_Bbhl = false;
              bbhl = new UFDouble(1.0D);
              BbRateDig = RateDig;
            } else if (pk_currtype.equals(this.m_panel.getDjSettingParam().getFracCurrPK()))
            {
              is_Bbhl = false;

              BbRateDig = Currency.getRateDigit(pkcorp, pk_currtype, pk_frctype);
            }
            else {
              is_Bbhl = true;

              BbRateDig = Currency.getRateDigit(pkcorp, pk_currtype, pk_localtype);
              try {
                bbhl = Currency.getRate(pkcorp, pk_currtype, pk_localtype, date);
                bbhl = bbhl == null ? new UFDouble(0.0D) : bbhl;
              } catch (Exception e2) {
                Logger.debug(
                  NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000732") + 
                  e2);
              }
            }
          }
        }
      } catch (Exception ee) {
        Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000733") + ee);
      }
      try {
        setCurrency(e.getRow(), pk_currtype);
      } catch (Exception ex) {
        Logger.error(ex.getMessage(), ex);
      }

      if (e.getPos() == 0)
      {
        changeDigByCurr(pk_currtype);

        this.m_panel.getHeadItem("bbhl").getComponent().setEnabled(is_Bbhl);
        this.m_panel.getHeadItem("fbhl").getComponent().setEnabled(is_Fbhl);

        BillItem btBbhl = this.m_panel.getHeadItem("bbhl");
        if (BbRateDig != 0)
          btBbhl.setDecimalDigits(BbRateDig);
        BillItem btFbhl = this.m_panel.getHeadItem("fbhl");
        btFbhl.setDecimalDigits(FbRateDig);

        this.m_panel.setHeadItem("fbhl", fbhl.setScale(FbRateDig, 4));
        BillEditEvent e2 = new BillEditEvent(
          this.m_panel.getHeadItem("fbhl"), fbhl, "fbhl", 0, 0);
        changeHeadFbhl(e2, false);
        this.m_panel.setHeadItem("bbhl", bbhl.setScale(BbRateDig, 4));
      }
      else {
        this.m_panel.setBodyValueAt(
          fbhl.setScale(FbRateDig, 4), e.getRow(), "fbhl");
        this.m_panel.setBodyValueAt(
          bbhl.setScale(BbRateDig, 4), e.getRow(), "bbhl");
      }
    }
  }

  public void changeBodyCurrency(BillEditEvent e)
    throws Exception
  {
    if ((e == null) || (e.getKey() == null) || (e.getPos() != 1))
      return;
    if ((e.getKey().indexOf("bbje") > -1) || (e.getKey().indexOf("fbje") > -1) || 
      (e.getKey().indexOf("bbhl") > -1) || (e.getKey().indexOf("fbhl") > -1))
    {
      changeBodyFBH(e);
    }
    else
      changeBodyJshj(e);
  }

  public void changeBzbm_H(String pk_currtype, String date, boolean bChangeHL)
    throws Exception
  {
    Logger.debug(
      NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000752") + 
      pk_currtype);
    boolean is_Bbhl = true;
    boolean is_Fbhl = true;
    if (pk_currtype == null)
      return;
    if (this.m_panel.getDjSettingParam().getIsQc())
    {
      date = this.m_panel.getDjSettingParam().getQyrq2().toString();
    }Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000731") + date);

    String pk_frctype = this.m_panel.getDjSettingParam().getFracCurrPK();
    String pk_localtype = this.m_panel.getDjSettingParam().getLocalCurrPK();
    String pkcorp = this.m_panel.getDjSettingParam().getPk_corp();

    int RateDig = 0;
    RateDig = Currency.getRateDigit(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype);

    int FbRateDig = 0;
    int BbRateDig = 0;
    UFDouble bbhl = new UFDouble(0.0D);
    UFDouble fbhl = new UFDouble(0.0D);
    if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac() == null)
      return;
    try
    {
      if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac() != null)
      {
        if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac().booleanValue())
        {
          if (pk_currtype.equals(pk_localtype))
          {
            is_Bbhl = false;
            is_Fbhl = false;
            bbhl = new UFDouble(1.0D);
            fbhl = new UFDouble(0.0D);
            BbRateDig = RateDig;
          } else if (pk_currtype.equals(pk_frctype)) {
            is_Bbhl = true;
            is_Fbhl = false;
            fbhl = new UFDouble(1.0D);

            BbRateDig = Currency.getRateDigit(pkcorp, pk_frctype, pk_localtype);
//            bbhl = Currency.getRate(pkcorp, pk_frctype, pk_localtype, date);
          //add by xiaolong_fan.取期间汇率
          if(!getParentCorpCode().equalsIgnoreCase("10395"))
      	{
      		bbhl = Currency.getRate(pkcorp, pk_currtype, pk_localtype, date);
      	}
      	else
      	{
      		bbhl=getMonthRate(pk_currtype,pk_localtype);
      	}
        bbhl = bbhl == null ? new UFDouble(0.0D) : bbhl;
        //end by xiaolong_fan.
          }
          else {
            is_Bbhl = true;
            is_Fbhl = true;

            FbRateDig = Currency.getRateDigit(pkcorp, pk_currtype, pk_frctype);

            BbRateDig = Currency.getRateDigit(pkcorp, pk_frctype, pk_localtype);
            try {
              fbhl = Currency.getRate(pkcorp, pk_currtype, pk_frctype, date);
              //add by xiaolong_fan.取期间汇率
//              bbhl = Currency.getRate(pkcorp, pk_frctype, pk_localtype, date);
              if(!getParentCorpCode().equalsIgnoreCase("10395"))
          	{
          		bbhl = Currency.getRate(pkcorp, pk_currtype, pk_localtype, date);
          	}
          	else
          	{
          		bbhl=getMonthRate(pk_currtype,pk_localtype);
          	}
            bbhl = bbhl == null ? new UFDouble(0.0D) : bbhl;
            //end by xiaolong_fan.
            } catch (Exception e2) {
              Logger.debug(
                NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000732") + 
                e2);
            }
          }
        }
        else {
          is_Fbhl = false;
          if (pk_currtype.equals(this.m_panel.getDjSettingParam().getLocalCurrPK()))
          {
            is_Bbhl = false;
            bbhl = new UFDouble(1.0D);
            BbRateDig = RateDig;
          }
          else {
            is_Bbhl = true;

            BbRateDig = Currency.getRateDigit(pkcorp, pk_currtype, pk_localtype);
            try {
            	if(!getParentCorpCode().equalsIgnoreCase("10395"))
            	{
            		bbhl = Currency.getRate(pkcorp, pk_currtype, pk_localtype, date);
            	}
            	else
            	{
            		bbhl=getMonthRate(pk_currtype,pk_localtype);
            	}
              bbhl = bbhl == null ? new UFDouble(0.0D) : bbhl;
            } catch (Exception e2) {
              Logger.debug(
                NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000732") + 
                e2);
            }
          }
        }
      }
    } catch (Exception ee) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000753") + ee);
    }
    int curdig = 
      Currency.getRateDigit(pkcorp, pk_currtype);
    this.m_panel.getHeadItem("ybje").setDecimalDigits(curdig);

    this.m_panel.getHeadItem("bbhl").getComponent().setEnabled(is_Bbhl);
    this.m_panel.getHeadItem("fbhl").getComponent().setEnabled(is_Fbhl);

    BillItem btBbhl = this.m_panel.getHeadItem("bbhl");
    if (BbRateDig != 0)
      btBbhl.setDecimalDigits(BbRateDig);
    BillItem btFbhl = this.m_panel.getHeadItem("fbhl");
    btFbhl.setDecimalDigits(FbRateDig);

    if (bChangeHL) {
      this.m_panel.setHeadItem("bbhl", bbhl);
      this.m_panel.setHeadItem("fbhl", fbhl);
    }
  }
  
  

  public void changeChbm_cl(BillEditEvent e)
    throws Exception
  {
    if (this.m_panel.getM_Parent().getM_DjState() == 9999) {
      return;
    }
    Logger.debug("key= " + e.getKey());
    if (((e.getKey().equalsIgnoreCase("chmc")) || (e.getKey().equalsIgnoreCase("invcode"))) && (e.getPos() == 1))
    {
      String value = (this.m_panel
        .getBodyValueAt(e.getRow(), "sl") == null) || 
        (this.m_panel.getBodyValueAt(e.getRow(), "sl")
        .toString().trim().length() < 1) ? 
        "0.00" : 
        String.valueOf(this.m_panel.getBodyValueAt(e.getRow(), 
        "sl"));
      if (value.equals("0.00")) {
        this.m_panel.setBodyValueAt(value, e.getRow(), "sl");
      }
      BillEditEvent e2 = new BillEditEvent(
        this.m_panel.getBodyItem("dj"), value, "dj", 
        e.getRow(), 1);
      changeBodyJshj(e2);
    }
  }

  public void changeDigByCurr(String currtype)
    throws Exception
  {
    int digit = 2;
    digit = 
      Currency.getCurrDigit(currtype);
    try
    {
      BillItem ybje = this.m_panel.getHeadItem(
        "ybje");
      ybje.setDecimalDigits(digit);

      BillItem ybye = this.m_panel.getBodyItem("ybye");

      ybye.setDecimalDigits(digit);

      BillItem JFYBWSJE = this.m_panel.getBodyItem("jfybwsje");
      if (JFYBWSJE != null) {
        JFYBWSJE.setDecimalDigits(digit);
      }

      BillItem DFYBWSJE = this.m_panel
        .getBodyItem("dfybwsje");
      if (DFYBWSJE != null) {
        DFYBWSJE.setDecimalDigits(digit);
      }

      BillItem jfybsj = this.m_panel.getBodyItem(
        "jfybsj");
      if (jfybsj != null) {
        jfybsj.setDecimalDigits(digit);
      }

      BillItem dfybsj = this.m_panel.getBodyItem(
        "dfybsj");
      if (dfybsj != null) {
        dfybsj.setDecimalDigits(digit);
      }

      BillItem jfybje = this.m_panel.getBodyItem(
        "jfybje");
      if (jfybje != null) {
        jfybje.setDecimalDigits(digit);
      }

      BillItem dfybje = this.m_panel.getBodyItem(
        "dfybje");
      if (dfybje != null) {
        dfybje.setDecimalDigits(digit);
      }

      BillItem txlx_ybje = this.m_panel.getBodyItem(
        "txlx_ybje");
      if (txlx_ybje != null) {
        txlx_ybje.setDecimalDigits(digit);
      }

      BillItem txlx_bbje = this.m_panel.getBodyItem(
        "txlx_bbje");
      if (txlx_bbje != null) {
        txlx_bbje.setDecimalDigits(digit);
      }

      BillItem txlx_fbje = this.m_panel.getBodyItem(
        "txlx_fbje");
      if (txlx_fbje != null)
        txlx_fbje.setDecimalDigits(digit);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  public void changeHeadFbhl(BillEditEvent e, boolean isChangeBL)
    throws Exception
  {
    ARAPDjDataBuffer dataBuffer = this.m_panel.getDjDataBuffer();

    String strDjdl = dataBuffer.getCurrentDjdl();

    if ((e.getKey().equals("fbhl")) && (e.getPos() == 0) && 
      (!strDjdl.equals("hj")))
    {
      int rowCount = this.m_panel.getRowCount();
      for (int i = 0; i < rowCount; i++);
    }
  }

  public void changeBodyKsmc(BillEditEvent e)
    throws Exception
  {
    if ((e.getKey().equals("ksmc")) && (e.getPos() == 1)) {
      String[] formulas = { 
        "hbbm->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ksbm_cl)", 
        "ksdz->getColValue(bd_cubasdoc,conaddr,pk_cubasdoc,hbbm)", 
        "kssh->getColValue(bd_cubasdoc,taxpayerid,pk_cubasdoc,hbbm)", 
        "pk_custbank->getColValue(bd_custbank,pk_custbank,pk_cubasdoc,hbbm)", 
        "kskhh->getColValue(bd_custbank,accname,pk_custbank,pk_custbank)", 
        "areacl_pk->getColValue(bd_cubasdoc,pk_areacl,pk_cubasdoc,hbbm)", 
        "areacl_name->getColValue(bd_areacl,areaclname,pk_areacl,areacl_pk)" };
      this.m_panel.getBillModel().execFormulas(e.getRow(), formulas);
    }
    setDJKH(e.getRow());
  }

  public void changeKsbm_cl(BillEditEvent e)
    throws Exception
  {
    Logger.debug("key= " + e.getKey());
    if (e.getKey().equals("ksbm_cl")) {
      resetYHZH(e);
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000754"));

      String[] formulas = { 
        "hbbm->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ksbm_cl)", 
        "ksdz->getColValue(bd_cubasdoc,conaddr,pk_cubasdoc,hbbm)", 
        "ksdh->getColValue(bd_cubasdoc,phone1,pk_cubasdoc,hbbm)", 
        "kssh->getColValue(bd_cubasdoc,taxpayerid,pk_cubasdoc,hbbm)", 
        "pk_custbank->getColValue(bd_custbank,pk_custbank,pk_cubasdoc,hbbm)", 
        "kskhh->getColValue(bd_custbank,accname,pk_custbank,pk_custbank)", 
        "kszh->getColValue(bd_custbank,account,pk_custbank,pk_custbank)" };

      this.m_panel.getBillData().execHeadFormulas(formulas);
    }
  }

  public void resetYHZH(BillEditEvent e)
  {
    ARAPDjDataBuffer dataBuffer = this.m_panel.getDjDataBuffer();

    String strDjdl = dataBuffer.getCurrentDjdl();
    try
    {
      if ((e == null) || (e.getPos() == 0)) {
        if ((strDjdl.equalsIgnoreCase("sk")) || (strDjdl.equalsIgnoreCase("ys")) || 
          (strDjdl.equalsIgnoreCase("sj"))) {
          UIRefPane ref = (UIRefPane)this.m_panel.getHeadItem("fkyhzh").getComponent();
          ref.getRefModel().setUseDataPower(true);
          String ks = this.m_panel.getHeadItem("ksbm_cl").getValue();
          String ywy = this.m_panel.getHeadItem("ywybm").getValue();
          if ((ks != null) && (ks.length() != 0)) {
            if ((ref.getRefModel() instanceof IArapRefModel))
              ((IArapRefModel)ref.getRefModel()).refreshWherePart("KS_REF", ks);
          } else if ((ywy != null) && (ywy.length() != 0)) {
            if ((ref.getRefModel() instanceof IArapRefModel))
              ((IArapRefModel)ref.getRefModel()).refreshWherePart("YWY_REF", ywy);
            ref.getRefModel().setUseDataPower(false);
          }
          else if ((ref.getRefModel() instanceof IArapRefModel)) {
            ((IArapRefModel)ref.getRefModel()).refreshWherePart("BUS_REF", null);
          }
          UIRefPane sref = (UIRefPane)this.m_panel.getHeadItem("skyhzh").getComponent();
          if ((sref.getRefModel() instanceof IArapRefModel)) {
            ((IArapRefModel)sref.getRefModel()).refreshWherePart("PRV_REF", null);
          }
        }
      }
      else if ((strDjdl.equalsIgnoreCase("sk")) || (strDjdl.equalsIgnoreCase("ys")) || 
        (strDjdl.equalsIgnoreCase("sj"))) {
        UIRefPane ref = (UIRefPane)this.m_panel.getBodyItem("bankacc_fk").getComponent();
        ref.getRefModel().setUseDataPower(true);
        String ks = (String)this.m_panel.getBodyValueAt(e.getRow(), "ksbm_cl");
        String ywy = (String)this.m_panel.getBodyValueAt(e.getRow(), "ywybm");
        if ((ks != null) && (ks.length() != 0)) {
          if ((ref.getRefModel() instanceof IArapRefModel))
            ((IArapRefModel)ref.getRefModel()).refreshWherePart("KS_REF", ks);
        }
        else if ((ywy != null) && (ywy.length() != 0)) {
          if ((ref.getRefModel() instanceof IArapRefModel))
            ((IArapRefModel)ref.getRefModel()).refreshWherePart("YWY_REF", ywy);
          ref.getRefModel().setUseDataPower(false);
        }
        else if ((ref.getRefModel() instanceof IArapRefModel)) {
          ((IArapRefModel)ref.getRefModel()).refreshWherePart("BUS_REF", null);
        }
        UIRefPane sref = (UIRefPane)this.m_panel.getBodyItem("bankacc").getComponent();
        if ((sref.getRefModel() instanceof IArapRefModel))
          ((IArapRefModel)sref.getRefModel()).refreshWherePart("PRV_REF", null);
      }
    }
    catch (Exception ex) {
      Logger.debug(ex.getMessage());
    }try {
      if ((e == null) || (e.getPos() == 0)) {
        if ((strDjdl.equalsIgnoreCase("fk")) || (strDjdl.equalsIgnoreCase("yf")) || 
          (strDjdl.equalsIgnoreCase("fj"))) {
          UIRefPane ref = (UIRefPane)this.m_panel.getHeadItem("skyhzh").getComponent();
          ref.getRefModel().setUseDataPower(true);
          String ks = this.m_panel.getHeadItem("ksbm_cl").getValue();
          String ywy = this.m_panel.getHeadItem("ywybm").getValue();
          if ((ks != null) && (ks.length() != 0)) {
            if ((ref.getRefModel() instanceof IArapRefModel))
              ((IArapRefModel)ref.getRefModel()).refreshWherePart("KS_REF", ks);
          }
          else if ((ywy != null) && (ywy.length() != 0)) {
            if ((ref.getRefModel() instanceof IArapRefModel))
              ((IArapRefModel)ref.getRefModel()).refreshWherePart("YWY_REF", ywy);
            ref.getRefModel().setUseDataPower(false);
          }
          else if ((ref.getRefModel() instanceof IArapRefModel)) {
            ((IArapRefModel)ref.getRefModel()).refreshWherePart("BUS_REF", null);
          }
          UIRefPane fref = (UIRefPane)this.m_panel.getHeadItem("fkyhzh").getComponent();
          if ((fref.getRefModel() instanceof IArapRefModel)) {
            ((IArapRefModel)fref.getRefModel()).refreshWherePart("BUS_REF", null);
          }
        }

      }
      else if ((strDjdl.equalsIgnoreCase("fk")) || (strDjdl.equalsIgnoreCase("yf")) || 
        (strDjdl.equalsIgnoreCase("fj"))) {
        UIRefPane ref = (UIRefPane)this.m_panel.getBodyItem("bankacc").getComponent();
        ref.getRefModel().setUseDataPower(true);
        String ks = (String)this.m_panel.getBodyValueAt(e.getRow(), "ksbm_cl");
        String ywy = (String)this.m_panel.getBodyValueAt(e.getRow(), "ywybm");
        if ((ks != null) && (ks.length() != 0)) {
          if ((ref.getRefModel() instanceof IArapRefModel))
            ((IArapRefModel)ref.getRefModel()).refreshWherePart("KS_REF", ks);
        }
        else if ((ywy != null) && (ywy.length() != 0)) {
          if ((ref.getRefModel() instanceof IArapRefModel))
            ((IArapRefModel)ref.getRefModel()).refreshWherePart("YWY_REF", ywy);
          ref.getRefModel().setUseDataPower(false);
        }
        else if ((ref.getRefModel() instanceof IArapRefModel)) {
          ((IArapRefModel)ref.getRefModel()).refreshWherePart("BUS_REF", null);
        }

        UIRefPane fref = (UIRefPane)this.m_panel.getBodyItem("bankacc_fk").getComponent();
        if ((fref.getRefModel() instanceof IArapRefModel))
          ((IArapRefModel)fref.getRefModel()).refreshWherePart("PRV_REF", null);
      }
    }
    catch (Exception localException1)
    {
    }
  }

  public void changePk_corp(BillEditEvent e, String corpID)
    throws Exception
  {
    DJZBHeaderVO headvo = (DJZBHeaderVO)getDjDataBuffer().getCurrentDJZBVO().getParentVO();
    DJZBItemVO[] itemvos = (DJZBItemVO[])getDjDataBuffer().getCurrentDJZBVO().getChildrenVO();
    String pk_corp = this.m_panel.getDjSettingParam().getPk_corp();
    if (pk_corp == null)
      return;
    if (corpID != null) {
      if ((pk_corp != null) && (pk_corp.equalsIgnoreCase(corpID))) {
        return;
      }
      pk_corp = corpID;
    }
    else if ((e.getKey().equalsIgnoreCase("dwbm")) && (e.getPos() == 0))
    {
      if ((pk_corp == null) || (pk_corp.trim().length() < 1))
        return;
    }
    else {
      return;
    }

    BillItem[] headitems = this.m_panel.getHeadShowItems();
    for (int i = 0; i < headitems.length; i++) {
      if (headitems[i].getDataType() == 5) {
        UIRefPane ref = (UIRefPane)headitems[i].getComponent();
        ref.getRefModel().setPk_corp(pk_corp);
        Object pk = headvo.getAttributeValue(headitems[i].getKey());
        if (pk == null)
          pk = itemvos[0].getAttributeValue(headitems[i].getKey());
        ref.setPK(pk);
      }
    }
    BillItem[] bodyitems = this.m_panel.getBodyShowItems();
    for (int i = 0; i < bodyitems.length; i++) {
      if (bodyitems[i].getDataType() == 5) {
        UIRefPane ref = (UIRefPane)bodyitems[i]
          .getComponent();
        if (ref.getRefModel() != null) {
          ref.getRefModel().setPk_corp(pk_corp);
        }
      }
    }

    Log.getInstance(getClass()).warn("end");
  }

  private ARAPDjDataBuffer getDjDataBuffer()
  {
    return this.m_panel.getDjDataBuffer();
  }
  public void changeBodyHtmc(BillEditEvent e) throws Exception {
    if ((e.getKey().equals("htmc")) && (e.getPos() == 1))
    {
      if ((((String[])MyClientEnvironment.getValue(this.m_panel.getDjSettingParam().getPk_corp(), "CDMBegin", null)).length != 0) || 
        (((String[])MyClientEnvironment.getValue(this.m_panel.getDjSettingParam().getPk_corp(), "FACBegin", null)).length != 0))
      {
        if ((this.m_panel.getBodyItem("htmc") != null) && (this.m_panel.getBodyItem("htbh") != null) && (this.m_panel.getBodyItem("htlx_pk") != null) && (this.m_panel.getBodyItem("htlx_mc") != null))
        {
          String[] f = { "htaid1->htbh+\"_\"+dwbm", "htmc->getColvalue(v_fi_fi_code,code,pk_billtype,htaid1)", "htlx_pk->getColvalue(v_fi_fi_code,contracttype,pk_billtype,htaid1)", "htlx_mc->getColvalue(fi_contype,resid_typename,pk_id,htlx_pk)" };
          this.m_panel.getBillModel().execFormulas(f);
        }
        else if ((this.m_panel.getBodyItem("htmc") != null) && (this.m_panel.getBodyItem("htbh") != null))
        {
          String[] f = { "htaid1->htbh+\"_\"+dwbm", "htmc->getColvalue(v_fi_fi_code,code,pk_billtype,htaid1)" };
          this.m_panel.getBillModel().execFormulas(f);
        }
      }
    }
  }

  public void changeBodyTXLX(BillEditEvent e)
    throws Exception
  {
    if (!e.getKey().equals("txlx_ybje"))
      return;
    int row = e.getRow();

    UFDouble txlx_ybje = (UFDouble)this.m_panel.getBodyValueAt(
      row, "txlx_ybje");
    String date = this.m_panel.getHeadItem("djrq").getValue();
    String pk_currtype = this.m_panel.getBodyValueAt(e.getRow(), 
      "bzbm").toString();
    this.m_panel.setBodyValueAt(getBodyFbje(pk_currtype, txlx_ybje, date, row), row, "txlx_fbje");

    UFDouble txlx_fbje = (UFDouble)this.m_panel.getBodyValueAt(
      row, "txlx_fbje");

    this.m_panel.setBodyValueAt(getBodyBbje(pk_currtype, txlx_ybje, txlx_fbje, date, row), row, "txlx_bbje");
  }

  private String getYhzh(UIRefPane ref) {
    String bankacc = ref.getRefValue("bankacc") == null ? null : 
      ref.getRefValue("bankacc").toString();
    if (bankacc == null)
      bankacc = ref.getRefValue("bd_accbank.bankacc") == null ? null : 
        ref.getRefValue("bd_accbank.bankacc").toString();
    return bankacc;
  }

  public void changeIscorresp(BillEditEvent e)
    throws Exception
  {
    if (e.getPos() != 0) {
      return;
    }
    ARAPDjDataBuffer dataBuffer = getDjDataBuffer();
    String strDjlxoid = dataBuffer.getCurrentDjlxoid();

    String strPKCorp = this.m_panel.getDjSettingParam().getPk_corp();

    DjLXVO temp = MyClientEnvironment.getDjlxVOByPK(strPKCorp, strDjlxoid);
    UFBoolean b = temp.getIscorresp();

    if ((b == null) || (!b.booleanValue())) {
      return;
    }
    String strDjdl = dataBuffer.getCurrentDjdl();
    if (this.m_panel.getBodyItem("qxrq") != null) {
      String qxrq = this.m_panel.getHeadItem("effectdate")
        .getValue();

      for (int row = 0; row < this.m_panel.getRowCount(); row++) {
        this.m_panel.setBodyValueAt(qxrq, row, 
          "qxrq");
      }
    }

    if (strDjdl.equals("ss")) {
      if (e.getKey().equalsIgnoreCase("begin_date"))
      {
        String begin_date = this.m_panel.getHeadItem(
          "begin_date").getValue();

        for (int row = 0; row < this.m_panel.getRowCount(); row++) {
          this.m_panel.setBodyValueAt(begin_date, row, 
            "begin_date");
        }

      }

      if (e.getKey().equalsIgnoreCase("end_date"))
      {
        String end_date = this.m_panel.getHeadItem("end_date")
          .getValue();

        for (int row = 0; row < this.m_panel.getRowCount(); row++) {
          this.m_panel.setBodyValueAt(end_date, row, 
            "end_date");
        }

      }

    }

    if (e.getKey().equals("item_bill_pk")) {
      for (int row = 0; row < this.m_panel.getRowCount(); row++)
        setItemValueHtoB_itembill(row);
    }
    if (e.getKey().equalsIgnoreCase("wldx"))
    {
      Object value = ((UIComboBox)this.m_panel
        .getHeadItem("wldx").getComponent()).getSelectedItem();

      for (int row = 0; row < this.m_panel.getRowCount(); row++) {
        this.m_panel.setBodyValueAt(
          value == null ? null : value, row, "wldx");
      }
    }
    else if (e.getKey().equalsIgnoreCase("deptid"))
    {
      String deptid = this.m_panel.getHeadItem("deptid")
        .getValue();

      String bmmc = ((UIRefPane)this.m_panel
        .getHeadItem("deptid").getComponent()).getRefName();
      for (int row = 0; row < this.m_panel.getRowCount(); row++) {
        this.m_panel.setBodyValueAt(deptid, row, "deptid");
        this.m_panel.setBodyValueAt(bmmc, row, "bmmc");
      }
    }
    else if (e.getKey().equalsIgnoreCase("zrdeptid"))
    {
      String deptid = this.m_panel.getHeadItem("zrdeptid")
        .getValue();

      String bmmc = ((UIRefPane)this.m_panel
        .getHeadItem("zrdeptid").getComponent()).getRefName();
      for (int row = 0; row < this.m_panel.getRowCount(); row++) {
        this.m_panel.setBodyValueAt(deptid, row, "zrdeptid");
        this.m_panel.setBodyValueAt(bmmc, row, "zrbmmc");
      }
    }
    else if (e.getKey().equalsIgnoreCase("jobid"))
    {
      String xmbm2 = this.m_panel.getHeadItem("jobid")
        .getValue();
      UIRefPane jobref = (UIRefPane)this.m_panel
        .getHeadItem("jobid").getComponent();

      String jobid = (String)jobref.getRefValue("bd_jobmngfil.pk_jobbasfil");

      String xmmc1 = ((UIRefPane)this.m_panel
        .getHeadItem("jobid").getComponent()).getRefName();

      for (int row = 0; row < this.m_panel.getRowCount(); row++) {
        this.m_panel.setBodyValueAt(jobid, row, "jobid");
        this.m_panel.setBodyValueAt(xmmc1, row, "xmmc1");
        this.m_panel.setBodyValueAt(xmbm2, row, "xmbm2");
      }
    }
    else if (e.getKey().equalsIgnoreCase("ksbm_cl"))
    {
      String ksbm_cl = this.m_panel.getHeadItem("ksbm_cl")
        .getValue();
      String hbbm = this.m_panel.getHeadItem("hbbm").getValue();

      String ksmc = ((UIRefPane)this.m_panel
        .getHeadItem("ksbm_cl").getComponent()).getRefName();
      UIRefPane ksref = (UIRefPane)this.m_panel
        .getHeadItem("ksbm_cl").getComponent();

      String custcode = ksref.getRefValue("bd_cubasdoc.custcode") == null ? null : 
        ksref.getRefValue("bd_cubasdoc.custcode").toString();

      for (int row = 0; row < this.m_panel.getRowCount(); row++) {
        BillEditEvent ev = new BillEditEvent("", this.m_panel.getBodyValueAt(row, "ksmc"), "ksmc", row, 1);
        this.m_panel.setBodyValueAt(ksmc, row, "ksmc");
        this.m_panel.setBodyValueAt(ksbm_cl, row, "ksbm_cl");
        this.m_panel.setBodyValueAt(hbbm, row, "hbbm");
        this.m_panel.setBodyValueAt(custcode, row, "ksbm");
        changeKsbm_cl(ev);
        changeKsbm_cl2(ev);
        changeBodyKsmc(ev);
        setDJKH(row);
      }
    } else if (e.getKey().equalsIgnoreCase("ywybm"))
    {
      String ywybm = this.m_panel.getHeadItem("ywybm").getValue();

      String ywymc = ((UIRefPane)this.m_panel
        .getHeadItem("ywybm").getComponent()).getRefName();

      for (int row = 0; row < this.m_panel.getRowCount(); row++) {
        BillEditEvent ev = new BillEditEvent("", ywymc, "ywymc", row, 1);
        this.m_panel.setBodyValueAt(ywybm, row, "ywybm");
        this.m_panel.setBodyValueAt(ywymc, row, "ywymc");
        changeBodyYwy(ev);
      }
    }
    else if (!e.getKey().equalsIgnoreCase("bbhl"))
    {
      if (e.getKey().equalsIgnoreCase("pjh"))
      {
        String pjh = this.m_panel.getHeadItem("pjh").getValue();
        for (int row = 0; row < this.m_panel.getRowCount(); row++) {
          this.m_panel.setBodyValueAt(pjh, row, "pjh");
        }
      }
      else if (e.getKey().equalsIgnoreCase("notetype"))
      {
        String notetypePK = this.m_panel.getHeadItem("notetype")
          .getValue();

        String notetypeMC = ((UIRefPane)this.m_panel
          .getHeadItem("notetype").getComponent()).getRefName();
        for (int row = 0; row < this.m_panel.getRowCount(); row++) {
          this.m_panel.setBodyValueAt(notetypePK, row, "notetype");
          this.m_panel.setBodyValueAt(notetypeMC, row, "notetype_name");
        }
      }
      else if (!e.getKey().equalsIgnoreCase("bzbm"))
      {
        if (e.getKey().equalsIgnoreCase("pj_jsfs"))
        {
          String pj_jsfs = this.m_panel.getHeadItem("pj_jsfs")
            .getValue();

          String pj_jsfs_mc = ((UIRefPane)this.m_panel
            .getHeadItem("pj_jsfs").getComponent()).getRefName();

          for (int row = 0; row < this.m_panel.getRowCount(); row++) {
            this.m_panel.setBodyValueAt(pj_jsfs, row, "pj_jsfs");
            this.m_panel.setBodyValueAt(pj_jsfs_mc, row, 
              "pj_jsfs_mc");
          }
        }
        else if (e.getKey().equalsIgnoreCase("accountid"))
        {
          String accountid = this.m_panel.getHeadItem("accountid")
            .getValue();

          String zhbzbm = this.m_panel.getHeadItem("zhbzbm")
            .getValue();

          String accidname = ((UIRefPane)this.m_panel
            .getHeadItem("accountid").getComponent()).getRefName();

          String skyhzh = this.m_panel.getHeadItem("skyhzh")
            .getValue();

          UIRefPane ref_skyhzh = (UIRefPane)this.m_panel
            .getHeadItem("skyhzh").getComponent();
          String bankacc = getYhzh(ref_skyhzh);

          String skyhmc = this.m_panel.getHeadItem("skyhmc")
            .getValue();

          String fkyhzh = this.m_panel.getHeadItem("fkyhzh")
            .getValue();

          UIRefPane ref_fkyhzh = (UIRefPane)this.m_panel
            .getHeadItem("fkyhzh").getComponent();

          String bankacc_fk = getYhzh(ref_fkyhzh);

          String fkyhmc = this.m_panel.getHeadItem("fkyhmc")
            .getValue();

          for (int row = 0; row < this.m_panel.getRowCount(); row++) {
            this.m_panel
              .setBodyValueAt(accountid, row, "accountid");
            this.m_panel.setBodyValueAt(zhbzbm, row, "zhbzbm");
            this.m_panel
              .setBodyValueAt(accidname, row, "accidname");
            if ((strDjdl.equals("ws")) || (strDjdl.equals("sj")) || 
              (strDjdl.equals("sk"))) {
              this.m_panel.setBodyValueAt(skyhzh, row, "skyhzh");
              this.m_panel
                .setBodyValueAt(bankacc, row, "bankacc");

              this.m_panel.setBodyValueAt(skyhmc, row, "skyhmc");
            }
            else {
              this.m_panel.setBodyValueAt(fkyhzh, row, "fkyhzh");
              this.m_panel.setBodyValueAt(bankacc_fk, row, 
                "bankacc_fk");

              this.m_panel.setBodyValueAt(fkyhmc, row, "fkyhmc");
            }
          }
        }
        else if (e.getKey().equalsIgnoreCase("skyhzh"))
        {
          String skyhzh = this.m_panel.getHeadItem("skyhzh")
            .getValue();

          UIRefPane ref_skyhzh = (UIRefPane)this.m_panel
            .getHeadItem("skyhzh").getComponent();
          String bankacc = getYhzh(ref_skyhzh);

          String skyhmc = this.m_panel.getHeadItem("skyhmc")
            .getValue();

          for (int row = 0; row < this.m_panel.getRowCount(); row++) {
            this.m_panel.setBodyValueAt(skyhzh, row, "skyhzh");
            this.m_panel.setBodyValueAt(bankacc, row, "bankacc");
            if (this.m_panel.getHeadItem("skyhmc").isEdit())
              continue;
            if (this.m_panel.getHeadItem("skyhmc")
              .isEnabled()) {
              continue;
            }
            this.m_panel.setBodyValueAt(skyhmc, row, "skyhmc");
          }

        }
        else if (e.getKey().equalsIgnoreCase("skyhmc"))
        {
          String skyhmc = this.m_panel.getHeadItem("skyhmc")
            .getValue();

          for (int row = 0; row < this.m_panel.getRowCount(); row++)
          {
            this.m_panel.setBodyValueAt(skyhmc, row, "skyhmc");
          }

        }
        else if (e.getKey().equalsIgnoreCase("fkyhzh"))
        {
          String fkyhzh = this.m_panel.getHeadItem("fkyhzh")
            .getValue();

          UIRefPane ref_fkyhzh = (UIRefPane)this.m_panel
            .getHeadItem("fkyhzh").getComponent();

          String bankacc_fk = getYhzh(ref_fkyhzh);

          String fkyhmc = this.m_panel.getHeadItem("fkyhmc")
            .getValue();

          for (int row = 0; row < this.m_panel.getRowCount(); row++) {
            this.m_panel.setBodyValueAt(fkyhzh, row, "fkyhzh");
            this.m_panel.setBodyValueAt(bankacc_fk, row, 
              "bankacc_fk");

            if (this.m_panel.getHeadItem("fkyhmc").isEdit())
              continue;
            if (this.m_panel.getHeadItem("fkyhmc")
              .isEnabled()) {
              continue;
            }
            this.m_panel.setBodyValueAt(fkyhmc, row, "fkyhmc");
          }

        }
        else if (e.getKey().equalsIgnoreCase("fkyhmc"))
        {
          String skyhmc = this.m_panel.getHeadItem("fkyhmc")
            .getValue();

          for (int row = 0; row < this.m_panel.getRowCount(); row++)
          {
            this.m_panel.setBodyValueAt(skyhmc, row, "fkyhmc");
          }
        }
      }
    }
  }

  public void execHeadFormulas_Account()
  {
    try
    {
      this.m_panel
        .execHeadFormula(
        "accname->getColvalue(bd_custbank,accname,pk_custbank,kskhyh)");
      this.m_panel
        .execHeadFormula(
        "account->getColvalue(bd_custbank,account,pk_custbank,kskhyh)");
    }
    catch (Exception localException)
    {
    }
  }

  public void execHeadFormulas_Accountid(BillCardPanel billcardpanel)
  {
    this.m_panel.execHeadFormula(
      "zhbzbm->getColValue(bd_accid,pk_currtype,pk_accid,accountid)");
  }

  public void execHeadFormulas_dddw(BillEditEvent e)
  {
    if ((e.getKey().equals("ddhbbm")) && (e.getPos() == 0)) {
      try {
        this.m_panel
          .execHeadFormula(
          "pk_cubasdoc->getColvalue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ddhbbm)");
        this.m_panel.getHeadItem("kskhyh").setValue(null);
        this.m_panel.getHeadItem("accname").setValue(null);
        this.m_panel.getHeadItem("account").setValue(null);

        resetAccount();
        this.m_panel
          .execHeadFormula(
          "kskhyh->getColvalue(bd_custbank,pk_custbank,pk_cubasdoc,pk_cubasdoc)");

        this.m_panel
          .execHeadFormula(
          "accname->getColvalue(bd_custbank,accname,pk_custbank,kskhyh)");
        this.m_panel
          .execHeadFormula(
          "account->getColvalue(bd_custbank,account,pk_custbank,kskhyh)");
      }
      catch (Exception localException)
      {
      }

      Log.getInstance(getClass()).warn("execHeadFormulas_dddw@@@@@@@@@@@ " + 
        this.m_panel.getHeadItem("accname").getValue());
    }
  }

  public void execHeadFormulas_ksbm_cl(BillCardPanel billcardpanel)
  {
    String[] formulas = { 
      "hbbm->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,ksbm_cl)", 
      "ksdz->getColValue(bd_cubasdoc,conaddr,pk_cubasdoc,hbbm)", 
      "ksdh->getColValue(bd_cubasdoc,phone1,pk_cubasdoc,hbbm)", 
      "kssh->getColValue(bd_cubasdoc,taxpayerid,pk_cubasdoc,hbbm)", 
      "pk_custbank->getColValue(bd_custbank,pk_custbank,pk_cubasdoc,hbbm)", 
      "kskhh->getColValue(bd_custbank,accname,pk_custbank,pk_custbank)", 
      "kszh->getColValue(bd_custbank,account,pk_custbank,pk_custbank)" };

    billcardpanel.getBillData().execHeadFormulas(formulas);
  }

  public void resetSSDjbh()
  {
    ARAPDjDataBuffer dataBuffer = this.m_panel.getDjDataBuffer();

    String strDjdl = dataBuffer.getCurrentDjdl();

    if ((strDjdl.equals("yf")) || (strDjdl.equals("fk")) || 
      (strDjdl.equals("fj")) || (strDjdl.equals("sj")) || (strDjdl.equals("ys")) || (strDjdl.equals("sk")))
      try
      {
        UIRefPane ref = (UIRefPane)this.m_panel
          .getHeadItem("item_bill_pk").getComponent();
        ref.setWhereString(" where dr=0 and djzt>=2 and (dwbm='" + 
          PubData.getPk_corp() + 
          "' or dwbm='" + this.m_panel.getDjSettingParam().getPk_corp() + 
          "')  and (zdr  is null)");

        UIRefPane ref1 = (UIRefPane)this.m_panel
          .getBodyItem("item_bill_djbh").getComponent();
        ref1.setWhereString(" where dr=0 and djzt>=2 and (dwbm='" + 
          PubData.getPk_corp() + 
          "' or dwbm='" + this.m_panel.getDjSettingParam().getPk_corp() + 
          "')  and (zdr  is null)");

        if ((this.m_panel.getM_Parent().getM_DjState() == 1) || (this.m_panel.getM_Parent().getM_DjState() == 3)) {
          ref.setWhereString(" where dr=0 and djzt=2 and (dwbm='" + 
            PubData.getPk_corp() + 
            "' or dwbm='" + this.m_panel.getDjSettingParam().getPk_corp() + 
            "')  and (zdr  is null)");

          ref1.setWhereString(" where dr=0 and djzt=2 and (dwbm='" + 
            PubData.getPk_corp() + 
            "' or dwbm='" + this.m_panel.getDjSettingParam().getPk_corp() + 
            "')  and (zdr  is null)");
        }
      }
      catch (Exception localException)
      {
      }
  }

  public void initTempletBody(String strCorpPK, String strDjdl, String strDjlxbm)
  {
    try
    {
      initDjHead(strDjdl, strDjlxbm);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }initBody(strCorpPK, strDjdl);
  }

  public void resetJobid(int row, String pk_jobmngfil)
    throws Exception
  {
    Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000794"));
    try
    {
      UIRefPane ref = (UIRefPane)this.m_panel
        .getBodyItem("xmjdmc").getComponent();

      PhaseRefModel model = (PhaseRefModel)ref
        .getRefModel();

      model.setJobID(pk_jobmngfil);
    }
    catch (Throwable ee) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000795") + ee);
    }
  }

  public void resetContractno(BillEditEvent e)
    throws Exception
  {
    if ((this.m_panel.getM_Parent().getM_DjState() == 1) || (this.m_panel.getM_Parent().getM_DjState() == 3) || 
      (this.m_panel.getM_Parent().getM_DjState() == -99) || 
      (this.m_panel.getM_Parent().getM_DjState() == -9) || 
      (this.m_panel.getM_Parent().getM_DjState() == -999) || 
      (this.m_panel.getM_Parent().getM_DjState() == 9999))
    {
      if ((e.getKey() != null) && (e.getPos() == 1))
        e.getKey().equals("contractno");
    }
  }

  public void resetYwymc(BillEditEvent e)
    throws Exception
  {
  }

  public void resetKsmc(BillEditEvent e)
    throws Exception
  {
  }

  public void rCResetBody(int row, boolean pass, boolean isSetHL)
    throws Exception
  {
    if ((this.m_panel.getM_Parent().getM_DjState() == 1) || (this.m_panel.getM_Parent().getM_DjState() == 3) || (pass)) {
      Object value = this.m_panel.getBodyValueAt(row, "bzmc");
      BillEditEvent e2 = new BillEditEvent(
        this.m_panel.getBodyItem("bzmc"), value, "bzmc", 
        row, 1);
      changeBItemBzbm(e2, isSetHL);
    }
  }

  public void setItemValueHtoB(int row)
    throws Exception
  {
    String dwbm = this.m_panel.getHeadItem("dwbm").getValue();
    if ((dwbm == null) || (dwbm.trim().length() == 0)) {
      return;
    }
    this.m_panel.setBodyValueAt(dwbm, row, "dwbm");

    setItemValueHtoB_bz(row);

    setItemValueHtoB_bbhl(row);

    setItemValueHtoB_fbhl(row);
    setItemValueHtoB_account(row);
    setItemValueHtoB_dept(row);
    setItemValueHtoB_ks(row);
    setItemValueHtoB_ywy(row);
    setItemValueHtoB_jsfs(row);
    setItemValueHtoB_pjh(row);

    setItemValueHtoB_effectdate(row);
    setItemValueHtoB_zrdept(row);

    ARAPDjDataBuffer dataBuffer = this.m_panel.getDjDataBuffer();

    String strDjdl = dataBuffer.getCurrentDjdl();
    doItemValueHtoB(row, strDjdl);
    setItemValueHtoB_jobid(row);
    setDJKH(row);
    rCResetBody(row, true, false);
  }

  public void setItemValueBtoB(int sourceRow, int tarRow)
    throws Exception
  {
    if ((sourceRow < 0) || (tarRow < 0))
      return;
    if ((this.m_panel.getRowCount() <= sourceRow) || 
      (this.m_panel.getRowCount() <= tarRow)) {
      return;
    }

    Object zy = this.m_panel.getBodyValueAt(sourceRow, "zy");
    this.m_panel.setBodyValueAt(zy, tarRow, "zy");
  }

  protected abstract void doItemValueHtoB(int paramInt, String paramString)
    throws Exception;

  protected void initPch(String strDjdl)
  {
    try
    {
      UIComboBox box = (UIComboBox)this.m_panel.getBodyItem("pch").getComponent();

      box.removeAllItems();

      box.addItem(" ");

      if ((strDjdl.equalsIgnoreCase("ys")) || (strDjdl.equals("sk")) || (strDjdl.equalsIgnoreCase("sj")) || (strDjdl.equalsIgnoreCase("ws")))
      {
        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000672"));

        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000673"));

        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000674"));
      } else if ((strDjdl.equalsIgnoreCase("yf")) || (strDjdl.equalsIgnoreCase("fk")) || (strDjdl.equalsIgnoreCase("fj")) || (strDjdl.equalsIgnoreCase("wf"))) {
        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000675"));

        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000676"));

        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000677"));
      } else if (strDjdl.equalsIgnoreCase("hj")) {
        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000672"));

        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000673"));

        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000674"));
        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000675"));

        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000676"));

        box.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000677"));
      }
    }
    catch (Exception e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000849") + e);
    }
  }

  protected void changeBody_ye(int row)
    throws Exception
  {
    ARAPDjDataBuffer dataBuffer = this.m_panel.getDjDataBuffer();

    String strDjdl = dataBuffer.getCurrentDjdl();

    UFDouble ybye = new UFDouble(0.0D);
    UFDouble bbye = new UFDouble(0.0D);
    UFDouble fbye = new UFDouble(0.0D);
    UFDouble shlye = new UFDouble(0.0D);
    String strybje = ""; String strfbje = ""; String strbbje = ""; String strShuL = "";

    if ((strDjdl.equals("yf")) || (strDjdl.equals("sk")) || (strDjdl.equals("sj")) || 
      (strDjdl.equals("ws")))
    {
      strybje = "dfybje";
      strbbje = "dfbbje";
      strfbje = "dffbje";
      strShuL = "dfshl";
    } else if ((strDjdl.equals("ys")) || (strDjdl.equals("fk")) || (strDjdl.equals("ss")) || 
      (strDjdl.equals("fj")) || (strDjdl.equals("wf")) || (strDjdl.equals("yt")))
    {
      strybje = "jfybje";
      strbbje = "jfbbje";
      strfbje = "jffbje";
      strShuL = "jfshl"; } else {
      strDjdl.equals("hj");
    }

    ybye = this.m_panel.getBodyValueAt(row, strybje) == null ? 
      new UFDouble(0.0D) : 
      (UFDouble)this.m_panel
      .getBodyValueAt(row, strybje);
    bbye = (UFDouble)this.m_panel.getBodyValueAt(
      row, strbbje);
    fbye = (UFDouble)this.m_panel.getBodyValueAt(
      row, strfbje);
    shlye = (UFDouble)this.m_panel.getBodyValueAt(
      row, strShuL);
    this.m_panel.setBodyValueAt(ybye, row, "ybye");
    this.m_panel.setBodyValueAt(bbye, row, "bbye");
    this.m_panel.setBodyValueAt(fbye, row, "fbye");
    this.m_panel.setBodyValueAt(shlye, row, "shlye");
  }

  public abstract void setHeadJe()
    throws Exception;

  protected void changeHead_Y_F_B_JE_YF()
    throws Exception
  {
    try
    {
      sumBtoH("dfybje", "ybje");
    }
    catch (Exception localException)
    {
    }

    try
    {
      sumBtoH("dffbje", "fbje");
    }
    catch (Exception localException1)
    {
    }

    try
    {
      sumBtoH("dfbbje", "bbje");
    }
    catch (Exception localException2)
    {
    }
  }

  protected void changeHead_Y_F_B_JE_YS()
    throws Exception
  {
    try
    {
      sumBtoH("jfybje", "ybje");
    }
    catch (Exception localException)
    {
    }

    try
    {
      sumBtoH("jffbje", "fbje");
    }
    catch (Exception localException1)
    {
    }

    try
    {
      sumBtoH("jfbbje", "bbje");
    }
    catch (Exception localException2) {
    }
  }

  protected void initBody(String strCorpPK, String strDjdl) {
    if (this.m_panel.getBodyItem("item_bill_pk") != null)
      ((UIRefPane)this.m_panel.getBodyItem("item_bill_pk").getComponent()).setReturnCode(true);
    try
    {
      if (this.m_panel.getBodyItem("zrbmmc") != null) {
        AbstractRefModel ref = ((UIRefPane)this.m_panel.getBodyItem("zrbmmc").getComponent()).getRefModel();
        ref.setUseDataPower(false);
      }

    }
    catch (Exception localException1)
    {
    }

    initSL(strCorpPK);

    initPch(strDjdl);

    ResMessage res_num_dj = null;
    try
    {
      res_num_dj = PubData.getDjNum(strCorpPK);
      if (res_num_dj.isSuccess) {
        BillItem dj = this.m_panel.getBodyItem("dj");
        if (dj != null) {
          dj.setDecimalDigits(res_num_dj.intValue);
        }

        BillItem hsdj = this.m_panel.getBodyItem("hsdj");
        if (hsdj != null) {
          hsdj.setDecimalDigits(res_num_dj.intValue);
        }
      }

    }
    catch (Exception e)
    {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000847") + res_num_dj.strMessage + "  " + e);
    }
    try
    {
      UIRefPane zy_ref = (UIRefPane)this.m_panel.getBodyItem("zy").getComponent();
      zy_ref.setRefNodeName("常用摘要");

      zy_ref.setButtonVisible(true);
    } catch (Exception e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000846") + e);
    }

    try
    {
      BillItem Btbbhl_B = this.m_panel.getBodyItem("bbhl");

      Btbbhl_B.setDecimalDigits(8);
    }
    catch (Exception e)
    {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000848") + e);
    }

    try
    {
      BillItem Btfbhl_B = this.m_panel.getBodyItem("fbhl");
      Btfbhl_B.setDecimalDigits(8);
    }
    catch (Exception e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000848") + e);
    }

    if (strDjdl.equalsIgnoreCase("hj"))
    {
      try
      {
        ((UIComboBox)this.m_panel.getBodyItem("sfbz").getComponent()).removeAllItems();

        ((UIComboBox)this.m_panel.getBodyItem("sfbz").getComponent()).addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000687"));
        ((UIComboBox)this.m_panel.getBodyItem("sfbz").getComponent()).addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000688"));
      }
      catch (Exception e) {
        Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000658") + e);
      }
    }
  }

  protected void initDjHead(String strDjdl, String strDjlxbm)
    throws Exception
  {
    if (((strDjdl.equals("yf")) || (strDjdl.equals("fk")) || 
      (strDjdl.equals("fj")) || (strDjdl.equals("sj")) || (strDjdl.equals("ys")) || (strDjdl.equals("sk"))) && 
      (this.m_panel.getHeadItem("item_bill_pk") != null)) {
      UIRefPane refPane = (UIRefPane)this.m_panel.getHeadItem(
        "item_bill_pk").getComponent();
      if (refPane != null) {
        refPane.setReturnCode(true);
      }

    }

    BillItem itemJszxzf = this.m_panel.getBodyItem("jszxzf");
    if (itemJszxzf != null)
      itemJszxzf.setEnabled(false);
    try {
      resetKm();
    } catch (Exception localException1) {
    }
    try {
      if (this.m_panel.getHeadItem("zrdeptid") != null) {
        AbstractRefModel ref = ((UIRefPane)this.m_panel.getHeadItem("zrdeptid").getComponent()).getRefModel();
        ref.setUseDataPower(false);
      }
    }
    catch (Exception localException2)
    {
    }
    try
    {
      UIRefPane zy_ref = (UIRefPane)this.m_panel.getHeadItem("scomment").getComponent();
      zy_ref.setRefNodeName("常用摘要");
      zy_ref.setAutoCheck(false);
      zy_ref.setReturnCode(false);
      zy_ref.setButtonVisible(true);
      zy_ref.setEnabled(false);
    }
    catch (Exception e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000846") + e);
    }
    try
    {
      UIRefPane zy_ref = (UIRefPane)this.m_panel.getHeadItem("kskhyh").getComponent();

      zy_ref.setReturnCode(true);
    }
    catch (Exception localException3)
    {
    }
    if ((strDjdl.equals("ss")) || (this.m_panel.getDjSettingParam().getSyscode() == 3)) {
      try
      {
        BillItem zyx16 = this.m_panel.getHeadItem("sscause");
        UIRefPane zy_ref = (UIRefPane)zyx16
          .getComponent();
        zy_ref.setRefNodeName("常用摘要");
        zy_ref.setReturnCode(false);
        zy_ref.setAutoCheck(false);
        zy_ref.setButtonVisible(true);
      }
      catch (Exception e) {
        Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000854") + e);
      }

    }

    try
    {
      UIRefPane zyx19_ref = (UIRefPane)this.m_panel.getHeadItem("ssbh").getComponent();
      String wherePart = zyx19_ref.getRefModel().getWherePart();
      Logger.debug("ssbh where sql= " + wherePart);
      String con_Djlxbm = " and djlxbm in (select itembilltype from arap_itemconfig where dwbm='" + 
        this.m_panel.getDjSettingParam().getPk_corp() + 
        "' and billtype='" + 
        strDjlxbm + "')";
      zyx19_ref.setWhereString(wherePart + con_Djlxbm);
      zyx19_ref.setReturnCode(true);
    }
    catch (Exception e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0000096") + e);
    }

    try
    {
      setBzDigit(strDjdl);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }

    try
    {
      BillItem btBbhl = this.m_panel.getHeadItem(
        "bbhl");
      btBbhl.setDecimalDigits(8);
    }
    catch (Exception e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000848") + e);
    }

    try
    {
      BillItem btFbhl = this.m_panel.getHeadItem(
        "fbhl");
      btFbhl.setDecimalDigits(8);
    } catch (Exception e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000848") + e);
    }

    try
    {
      if (strDjdl.equals("hj"))
      {
        if ((this.m_panel.getDjSettingParam().getIsBlnLocalFrac() != null) && 
          (!this.m_panel.getDjSettingParam().getIsBlnLocalFrac().booleanValue()))
        {
          this.m_panel.getBodyItem("fbhl").setEnabled(
            false);
        }

      }
      else if ((this.m_panel.getDjSettingParam().getIsBlnLocalFrac() != null) && 
        (!this.m_panel.getDjSettingParam().getIsBlnLocalFrac().booleanValue()))
      {
        this.m_panel.getHeadItem("fbhl").setEnabled(
          false);
      }

      if ((strDjdl.equals("fj")) || (strDjdl.equals("sj")) || (strDjdl.equals("ys")) || (strDjdl.equals("yf")))
      {
        ((UIRefPane)this.m_panel.getHeadItem(
          "skyhzh").getComponent()).setReturnCode(true);
        ((UIRefPane)this.m_panel.getHeadItem(
          "fkyhzh").getComponent()).setReturnCode(true);
        this.m_panel.getHeadItem("fkyhzh").setEnabled(true);
      }

      if ((strDjdl.equals("wf")) || (strDjdl.equals("ws")))
      {
        ((UIRefPane)this.m_panel.getHeadItem(
          "skyhzh").getComponent()).setReturnCode(true);
        ((UIRefPane)this.m_panel.getHeadItem(
          "fkyhzh").getComponent()).setReturnCode(true);
        this.m_panel.getHeadItem("fkyhzh").setEnabled(true);
      }
    }
    catch (Throwable e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000658") + e);
    }

    this.m_panel.setHeadItem("djdl", strDjdl);

    setHItemEnableBySys(strDjdl);
  }

  public void resetAccount()
    throws Exception
  {
    String pk_cubasdoc = null;

    pk_cubasdoc = this.m_panel.getHeadItem("pk_cubasdoc")
      .getValue();

    UIRefPane ref = (UIRefPane)this.m_panel
      .getHeadItem("kskhyh").getComponent();
    if ((pk_cubasdoc != null) && (pk_cubasdoc.trim().length() > 0))
      ref.setWhereString(" where pk_corp='" + 
        this.m_panel.getDjSettingParam().getPk_corp() + "' and pk_cubasdoc='" + 
        pk_cubasdoc + "'");
  }

  protected String getM_OldYwymcWhere()
  {
    return this.m_OldYwymcWhere;
  }

  protected void setM_OldYwymcWhere(String newM_OldYwymcWhere)
  {
    this.m_OldYwymcWhere = newM_OldYwymcWhere;
  }

  protected void initSL(String strCorpPK)
  {
    ResMessage res_num = PubData.getSlNum(strCorpPK);
    if (!res_num.isSuccess) {
      return;
    }
    BillItem jfshl_billitem = this.m_panel.getBodyItem("jfshl");
    if (jfshl_billitem != null) {
      jfshl_billitem.setDecimalDigits(res_num.intValue);
    }

    BillItem dfshl_billitem = this.m_panel.getBodyItem("dfshl");
    if (dfshl_billitem != null) {
      dfshl_billitem.setDecimalDigits(res_num.intValue);
    }
    BillItem shlye_billitem = this.m_panel.getBodyItem("shlye");
    if (shlye_billitem != null)
      shlye_billitem.setDecimalDigits(res_num.intValue);
  }

  protected void resetKm()
    throws Exception
  {
    UIRefPane ref_kmmc = (UIRefPane)this.m_panel.getBodyItem("kmmc").getComponent();

    ref_kmmc.setNotLeafSelectedEnabled(false);

    UIRefPane ref_kmbm = (UIRefPane)this.m_panel.getHeadItem("kmbm").getComponent();

    ref_kmbm.setNotLeafSelectedEnabled(false);
  }

  protected void resetKslb()
  {
    UIComboBox kslb = (UIComboBox)this.m_panel.getBodyItem("kslb").getComponent();

    kslb.removeAllItems();

    kslb.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000851"));

    kslb.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000768"));

    kslb.addItem(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000852"));

    this.m_panel.getBodyItem("kslb").setWithIndex(true);
  }

  protected void resetSzxmid(int FX)
    throws Exception
  {
    if (this.m_panel.getBodyItem("szxmmc") == null)
      return;
    try {
      UIRefPane ref = (UIRefPane)this.m_panel.getBodyItem("szxmmc").getComponent();

      if (FX == 1) {
        ref.setWhereString("( pk_corp='" + ref.getPk_corp() + "') and  (incomeflag ='Y' or ioflag='Y' )");
      }
      if (FX == 2)
        ref.setWhereString("( pk_corp='" + ref.getPk_corp() + "') and  (outflag ='Y' or ioflag='Y' )");
    }
    catch (Exception e) {
      Logger.debug(e.getMessage());
    }
  }

  protected void setItemValueHtoB_account(int row)
    throws Exception
  {
    try
    {
      if ((this.m_panel.getHeadItem("accountid") == null) || 
        (!this.m_panel.getHeadItem("accountid").isShow()))
        return;
      String accountid = this.m_panel.getHeadItem("accountid")
        .getValue();
      this.m_panel.setBodyValueAt(accountid, row, "accountid");

      String zhbzbm = this.m_panel.getHeadItem("zhbzbm")
        .getValue();
      this.m_panel.setBodyValueAt(zhbzbm, row, "zhbzbm");

      String accidname = ((UIRefPane)this.m_panel
        .getHeadItem("accountid").getComponent()).getRefName();
      this.m_panel.setBodyValueAt(accidname, row, "accidname");
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void setItemValueHtoB_jobid(int row) throws Exception
  {
    try
    {
      if ((this.m_panel.getHeadItem("jobid") == null) || 
        (!this.m_panel.getHeadItem("jobid").isShow()))
        return;
      String xmbm2 = this.m_panel.getHeadItem("jobid")
        .getValue();
      this.m_panel.setBodyValueAt(xmbm2, row, "xmbm2");
      String jobid = (String)((UIRefPane)this.m_panel
        .getHeadItem("jobid").getComponent()).getRefValue("bd_jobmngfil.pk_jobbasfil");
      this.m_panel.setBodyValueAt(jobid, row, "jobid");
      String xmmc1 = ((UIRefPane)this.m_panel
        .getHeadItem("jobid").getComponent()).getRefName();
      this.m_panel.setBodyValueAt(xmmc1, row, "xmmc1");
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void setItemValueHtoB_bbhl(int row)
    throws Exception
  {
    int bbhlDigits = this.m_panel.getHeadItem("bbhl")
      .getDecimalDigits();
    this.m_panel.getBodyItem("bbhl").setDecimalDigits(bbhlDigits);
    String bbhl = this.m_panel.getHeadItem("bbhl").getValue();
    if ((bbhl == null) || (bbhl.trim().length() == 0))
      return;
    this.m_panel.setBodyValueAt(bbhl, row, "bbhl");
  }

  protected void setItemValueHtoB_begin_date(int row)
    throws Exception
  {
    String begin_date = this.m_panel.getHeadItem("begin_date")
      .getValue();
    this.m_panel.setBodyValueAt(begin_date, row, "begin_date");
  }

  protected void setItemValueHtoB_effectdate(int row)
    throws Exception
  {
    if (this.m_panel.getBodyItem("qxrq") != null)
    {
      String begin_date = this.m_panel.getHeadItem("effectdate")
        .getValue();
      this.m_panel.setBodyValueAt(begin_date, row, "qxrq");
    }
  }

  protected void setItemValueHtoB_bz(int row)
    throws Exception
  {
    String pk_currency = this.m_panel.getHeadItem("bzbm")
      .getValue();
    if (pk_currency == null)
      return;
    this.m_panel.setBodyValueAt(pk_currency, row, "bzbm");
    String bzmc = ((UIRefPane)this.m_panel
      .getHeadItem("bzbm").getComponent()).getRefName();
    this.m_panel.setBodyValueAt(bzmc, row, "bzmc");
  }

  protected void setItemValueHtoB_dept(int row)
    throws Exception
  {
    try
    {
      if ((this.m_panel.getHeadItem("deptid") == null) || (!this.m_panel.getHeadItem("deptid").isShow())) {
        return;
      }

      String deptid = this.m_panel.getHeadItem("deptid")
        .getValue();
      if ((deptid == null) || (deptid.trim().length() == 0)) {
        return;
      }
      this.m_panel.setBodyValueAt(deptid, row, "deptid");
      String bmmc = ((UIRefPane)this.m_panel
        .getHeadItem("deptid").getComponent()).getRefName();
      this.m_panel.setBodyValueAt(bmmc, row, "bmmc");
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void setItemValueHtoB_zrdept(int row)
    throws Exception
  {
    try
    {
      if (this.m_panel.getHeadItem("zrdeptid") == null) {
        return;
      }

      String deptid = this.m_panel.getHeadItem("zrdeptid")
        .getValue();
      if ((deptid == null) || (deptid.trim().length() == 0)) {
        return;
      }
      this.m_panel.setBodyValueAt(deptid, row, "zrdeptid");
      String bmmc = ((UIRefPane)this.m_panel
        .getHeadItem("zrdeptid").getComponent()).getRefName();
      this.m_panel.setBodyValueAt(bmmc, row, "zrbmmc");
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void setItemValueHtoB_enddate(int row)
    throws Exception
  {
    String end_date = this.m_panel.getHeadItem("end_date")
      .getValue();
    this.m_panel.setBodyValueAt(end_date, row, "end_date");
  }

  protected void setItemValueHtoB_fbhl(int row)
    throws Exception
  {
    int fbhlDigits = this.m_panel.getHeadItem("fbhl")
      .getDecimalDigits();
    this.m_panel.getBodyItem("fbhl").setDecimalDigits(fbhlDigits);
    String fbhl = this.m_panel.getHeadItem("fbhl").getValue();
    if ((fbhl == null) || (fbhl.trim().length() == 0)) {
      return;
    }
    this.m_panel.setBodyValueAt(fbhl, row, "fbhl");
  }

  protected void setDJKH(int row) {
    try {
      int lybz = 0;
      String wldx = "";

      lybz = this.m_panel.getTailItem("lybz").getValue() == null ? 0 : 
        new Integer(
        this.m_panel.getTailItem("lybz").getValue()).intValue();
      wldx = this.m_panel.getBodyValueAt(row, "wldx") == null ? "" : 
        this.m_panel.getBodyValueAt(row, 
        "wldx").toString();

      if ((lybz != 3) && (lybz != 4) && (wldx.trim().equals(NCLangRes.getInstance().getStrByID("common", "UC000-0001589").trim()))) {
        BillItem billItem = this.m_panel.getBodyItem("ordercusmandoc_name");
        Object oOderCusName = this.m_panel.getBodyValueAt(row, "ordercusmandoc_name");

        if ((billItem != null) && ((!billItem.isShow()) || (oOderCusName == null)))
        {
          this.m_panel.setBodyValueAt(
            this.m_panel.getBodyValueAt(row, 
            "ksmc"), row, "ordercusmandoc_name");
          this.m_panel.setBodyValueAt(
            this.m_panel.getBodyValueAt(row, 
            "ksbm_cl"), row, "ordercusmandoc");
        }
      }
    }
    catch (Exception localException)
    {
    }
  }

  protected void setItemValueHtoB_fkyhzh(int row)
    throws Exception
  {
    if ((this.m_panel.getHeadItem("fkyhzh") == null) || (!this.m_panel.getHeadItem("fkyhzh").isShow()))
      return;
    String fkyhzh = this.m_panel.getHeadItem("fkyhzh").getValue();
    if ((fkyhzh == null) || (fkyhzh.trim().length() == 0)) {
      return;
    }
    this.m_panel.setBodyValueAt(fkyhzh, row, "fkyhzh");

    UIRefPane ref_fkyhzh = (UIRefPane)this.m_panel
      .getHeadItem("fkyhzh").getComponent();

    String bankacc_fk = getYhzh(ref_fkyhzh);
    this.m_panel.setBodyValueAt(bankacc_fk, row, "bankacc_fk");

    String fkyhmc = this.m_panel.getHeadItem("fkyhmc").getValue();
    this.m_panel.setBodyValueAt(fkyhmc, row, "fkyhmc");
  }

  protected void setItemValueHtoB_itembill(int row)
    throws Exception
  {
    if (!this.m_panel.getHeadItem("item_bill_pk").isShow())
      return;
    String item_bill_pk = this.m_panel.getHeadItem("item_bill_pk")
      .getValue();
    this.m_panel.setBodyValueAt(item_bill_pk, row, "item_bill_pk");
    String item_bill_djbh = null;
    if ((item_bill_pk != null) && (item_bill_pk.trim().length() > 0)) {
      item_bill_djbh = ((UIRefPane)this.m_panel
        .getHeadItem("item_bill_pk").getComponent()).getRefValue(
        "djbh").toString();
    }
    this.m_panel.setBodyValueAt(item_bill_djbh, row, 
      "item_bill_djbh");
  }

  protected void setItemValueHtoB_jsfs(int row)
    throws Exception
  {
    try
    {
      if (!this.m_panel.getHeadItem("pj_jsfs").isShow()) {
        return;
      }
      String pj_jsfs = this.m_panel.getHeadItem("pj_jsfs")
        .getValue();
      this.m_panel.setBodyValueAt(pj_jsfs, row, "pj_jsfs");
      String pj_jsfs_mc = ((UIRefPane)this.m_panel
        .getHeadItem("pj_jsfs").getComponent()).getRefName();
      this.m_panel.setBodyValueAt(pj_jsfs_mc, row, "pj_jsfs_mc");
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void setItemValueHtoB_ks(int row)
    throws Exception
  {
    try
    {
      if ((this.m_panel.getHeadItem("ksbm_cl") == null) || (!this.m_panel.getHeadItem("ksbm_cl").isShow()))
        return;
      String ksbm_cl = this.m_panel.getHeadItem("ksbm_cl")
        .getValue();
      if ((ksbm_cl == null) || (ksbm_cl.trim().length() == 0))
        return;
      this.m_panel.setBodyValueAt(ksbm_cl, row, "ksbm_cl");
      String ksmc = ((UIRefPane)this.m_panel
        .getHeadItem("ksbm_cl").getComponent()).getRefName();
      this.m_panel.setBodyValueAt(ksmc, row, "ksmc");
      try {
        BillEditEvent e3 = new BillEditEvent(
          this.m_panel.getBodyItem("ksmc"), ksmc, "ksmc", 
          row, 1);
        changeKsmc(e3);
        changeBodyKsmc(e3);
      } catch (Exception e33) {
        Logger.error(e33.getMessage(), e33);
      }
      UIRefPane ksref = (UIRefPane)this.m_panel
        .getHeadItem("ksbm_cl").getComponent();
      String custcode = ksref.getRefValue("bd_cubasdoc.custcode") == null ? null : 
        ksref.getRefValue("bd_cubasdoc.custcode").toString();

      String hbbm = this.m_panel.getHeadItem("hbbm").getValue();
      this.m_panel.setBodyValueAt(hbbm, row, "hbbm");
      this.m_panel.setBodyValueAt(custcode, row, "ksbm");
      setDJKH(row);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void setItemValueHtoB_notetype(int row)
    throws Exception
  {
    if (this.m_panel.getHeadItem("notetype") != null) {
      if (!this.m_panel.getHeadItem("notetype").isShow())
        return;
      String notetype = this.m_panel.getHeadItem("notetype")
        .getValue();
      this.m_panel.setBodyValueAt(notetype, row, "notetype");
      String notetype_name = ((UIRefPane)this.m_panel
        .getHeadItem("notetype").getComponent()).getRefName();
      this.m_panel.setBodyValueAt(notetype_name, row, 
        "notetype_name");
    }
  }

  protected void setItemValueHtoB_pjh(int row)
    throws Exception
  {
    try
    {
      if (this.m_panel.getHeadItem("pjh") == null)
        return;
      if (!this.m_panel.getHeadItem("pjh").isShow()) {
        return;
      }
      String pjh = this.m_panel.getHeadItem("pjh").getValue();
      this.m_panel.setBodyValueAt(pjh, row, "pjh");
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void setItemValueHtoB_skyhzh(int row)
    throws Exception
  {
    if ((this.m_panel.getHeadItem("skyhzh") == null) || (!this.m_panel.getHeadItem("skyhzh").isShow()))
      return;
    String skyhzh = this.m_panel.getHeadItem("skyhzh").getValue();
    if ((skyhzh == null) || (skyhzh.trim().length() == 0)) {
      return;
    }
    this.m_panel.setBodyValueAt(skyhzh, row, "skyhzh");

    UIRefPane ref_skyhzh = (UIRefPane)this.m_panel
      .getHeadItem("skyhzh").getComponent();
    String bankacc = getYhzh(ref_skyhzh);
    this.m_panel.setBodyValueAt(bankacc, row, "bankacc");

    String skyhmc = (String)this.m_panel.getHeadItem("skyhmc").getValueObject();
    this.m_panel.setBodyValueAt(skyhmc, row, "skyhmc");
  }

  protected void setItemValueHtoB_wldx(int row)
    throws Exception
  {
    Object wldxObj = ((UIComboBox)this.m_panel
      .getHeadItem("wldx").getComponent()).getSelectedItem();
    Object wldxValue = null;
    if ((wldxObj instanceof IConstEnum))
      wldxValue = ((IConstEnum)wldxObj).getValue();
    this.m_panel.setBodyValueAt(wldxValue, row, "wldx");
  }

  protected void setItemValueHtoB_ywy(int row)
    throws Exception
  {
    try
    {
      if ((this.m_panel.getHeadItem("ywybm") == null) || (!this.m_panel.getHeadItem("ywybm").isShow())) {
        return;
      }
      String ywybm = this.m_panel.getHeadItem("ywybm").getValue();
      if ((ywybm == null) || (ywybm.trim().length() == 0))
        return;
      this.m_panel.setBodyValueAt(ywybm, row, "ywybm");
      String ywymc = ((UIRefPane)this.m_panel
        .getHeadItem("ywybm").getComponent()).getRefName();
      this.m_panel.setBodyValueAt(ywymc, row, "ywymc");
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void sumBtoH(String Bkey, String Hkey)
    throws Exception
  {
    double subValue = 0.0D;

    long t = System.currentTimeMillis();

    int rowCount = this.m_panel.getRowCount();

    UFDouble value = null;

    for (int i = 0; i < rowCount; i++) {
      try {
        value = this.m_panel.getBodyValueAt(i, Bkey) == null ? 
          new UFDouble(0.0D) : 
          (UFDouble)this.m_panel
          .getBodyValueAt(i, Bkey);
        subValue += value.doubleValue();
      }
      catch (Exception e1) {
        Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000819") + i + NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000820") + e1);
      }

    }

    t -= System.currentTimeMillis();
    Logger.debug(" FOR TIME = " + t);

    int digit = this.m_panel.getHeadItem(Hkey).getDecimalDigits();
    this.m_panel.setHeadItem(Hkey, 
      new UFDouble(subValue).setScale(digit, 4));
  }

  protected void setCurrency(int row, String currency)
    throws Exception
  {
    int digit = 2;

    digit = Currency.getCurrDigit(currency);
    try
    {
      BillItem ybye = this.m_panel.getBodyItem("ybye");

      ybye.setDecimalDigits(digit);

      if (this.m_panel.getBodyValueAt(row, "ybye") != null) {
        this.m_panel.setBodyValueAt(((UFDouble)
          this.m_panel.getBodyValueAt(row, "ybye")).setScale(digit, 4), row, "ybye");
      }
      BillItem JFYBWSJE = this.m_panel.getBodyItem("jfybwsje");
      if (JFYBWSJE != null) {
        JFYBWSJE.setDecimalDigits(digit);

        if (this.m_panel.getBodyValueAt(row, "jfybwsje") != null) {
          this.m_panel.setBodyValueAt(
            ((UFDouble)this.m_panel.getBodyValueAt(
            row, "jfybwsje")).setScale(digit, 4), row, 
            "jfybwsje");
        }
      }

      BillItem DFYBWSJE = this.m_panel
        .getBodyItem("dfybwsje");
      if (DFYBWSJE != null) {
        DFYBWSJE.setDecimalDigits(digit);

        if (this.m_panel.getBodyValueAt(row, "dfybwsje") != null) {
          this.m_panel.setBodyValueAt(
            ((UFDouble)this.m_panel.getBodyValueAt(
            row, "dfybwsje")).setScale(digit, 4), row, 
            "dfybwsje");
        }
      }

      BillItem jfybsj = this.m_panel.getBodyItem(
        "jfybsj");
      if (jfybsj != null) {
        jfybsj.setDecimalDigits(digit);

        if (this.m_panel.getBodyValueAt(row, "jfybsj") != null) {
          this.m_panel.setBodyValueAt(
            ((UFDouble)this.m_panel.getBodyValueAt(
            row, "jfybsj")).setScale(digit, 4), row, 
            "jfybsj");
        }
      }

      BillItem dfybsj = this.m_panel.getBodyItem(
        "dfybsj");
      if (dfybsj != null) {
        dfybsj.setDecimalDigits(digit);

        if (this.m_panel.getBodyValueAt(row, "dfybsj") != null) {
          this.m_panel.setBodyValueAt(
            ((UFDouble)this.m_panel.getBodyValueAt(
            row, "dfybsj")).setScale(digit, 4), row, 
            "dfybsj");
        }
      }
      BillItem jfybje = this.m_panel.getBodyItem(
        "jfybje");
      if (jfybje != null) {
        jfybje.setDecimalDigits(digit);

        if (this.m_panel.getBodyValueAt(row, "jfybje") != null)
          this.m_panel.setBodyValueAt(
            ((UFDouble)this.m_panel.getBodyValueAt(
            row, "jfybje")).setScale(digit, 4), row, 
            "jfybje");
      }
      BillItem dfybje = this.m_panel.getBodyItem(
        "dfybje");
      if (dfybje != null) {
        dfybje.setDecimalDigits(digit);

        if (this.m_panel.getBodyValueAt(row, "dfybje") != null)
          this.m_panel.setBodyValueAt(
            ((UFDouble)this.m_panel.getBodyValueAt(
            row, "dfybje")).setScale(digit, 4), row, 
            "dfybje");
      }
      BillItem txlx_ybje = this.m_panel.getBodyItem(
        "txlx_ybje");
      if (txlx_ybje != null) {
        txlx_ybje.setDecimalDigits(digit);

        if (this.m_panel.getBodyValueAt(row, "txlx_ybje") != null)
          this.m_panel.setBodyValueAt(
            ((UFDouble)this.m_panel.getBodyValueAt(
            row, "txlx_ybje")).setScale(digit, 4), row, 
            "txlx_ybje");
      }
      BillItem txlx_bbje = this.m_panel.getBodyItem(
        "txlx_bbje");
      if (txlx_bbje != null) {
        txlx_bbje.setDecimalDigits(digit);

        if (this.m_panel.getBodyValueAt(row, "txlx_bbje") != null)
          this.m_panel.setBodyValueAt(
            ((UFDouble)this.m_panel.getBodyValueAt(
            row, "txlx_bbje")).setScale(digit, 4), row, 
            "txlx_bbje");
      }
      BillItem txlx_fbje = this.m_panel.getBodyItem(
        "txlx_fbje");
      if (txlx_fbje != null) {
        txlx_fbje.setDecimalDigits(digit);

        if (this.m_panel.getBodyValueAt(row, "txlx_fbje") != null)
          this.m_panel.setBodyValueAt(
            ((UFDouble)this.m_panel.getBodyValueAt(
            row, "txlx_fbje")).setScale(digit, 4), row, 
            "txlx_fbje");
      }
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
    }
  }

  protected void sumBtoH_2(String Bkey, String Hkey)
    throws Exception
  {
    double subValue = 0.0D;
    int rowCount = this.m_panel.getRowCount();
    UFDouble value = null;

    for (int i = 0; i < rowCount; i++) {
      try {
        value = this.m_panel.getBodyValueAt(i, Bkey) == null ? 
          new UFDouble(0.0D) : 
          (UFDouble)this.m_panel
          .getBodyValueAt(i, Bkey);

        subValue += value.doubleValue();
      }
      catch (Exception localException)
      {
      }
    }

    int digit = this.m_panel.getHeadItem(Hkey).getDecimalDigits();
    this.m_panel.setHeadItem(Hkey, 
      new UFDouble(subValue / 2.0D).setScale(digit, 4));
  }

  protected void setHItemEnableBySys(String strDjdl)
  {
    if (this.m_panel.getDjSettingParam().getSyscode() != 0)
    {
      if (this.m_panel.getDjSettingParam().getSyscode() != 1)
      {
        if ((this.m_panel.getDjSettingParam().getSyscode() == 2) || 
          (this.m_panel.getDjSettingParam().getSyscode() == -999))
        {
          setHItemEnableBySys_2(strDjdl);
        }
      }
    }
  }

  protected void setHItemEnableBySys_2(String strDjdl)
  {
  }

  protected void setBzDigit(String strDjdl)
    throws Exception
  {
    try
    {
      if (this.m_panel.getDjSettingParam().getFracCurrPK() != null) {
        BillItem fbje = this.m_panel.getHeadItem("fbje");
        fbje.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_f().intValue());

        BillItem fbye = this.m_panel.getBodyItem("fbye");
        fbye.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_f().intValue());

        BillItem jffbsj = this.m_panel.getBodyItem("jffbsj");
        if (jffbsj != null) {
          jffbsj.setDecimalDigits(
            this.m_panel.getDjSettingParam().getDigit_f().intValue());
        }

        BillItem dffbsj = this.m_panel.getBodyItem("dffbsj");
        if (dffbsj != null) {
          dffbsj.setDecimalDigits(
            this.m_panel.getDjSettingParam().getDigit_f().intValue());
        }

        BillItem Wbfybje = this.m_panel.getBodyItem("wbfybje");
        if (Wbfybje != null) {
          Wbfybje.setDecimalDigits(
            this.m_panel.getDjSettingParam().getDigit_f().intValue());
        }

        BillItem Wbffbje = this.m_panel.getBodyItem("wbffbje");
        if (Wbffbje != null)
          Wbffbje.setDecimalDigits(
            this.m_panel.getDjSettingParam().getDigit_f().intValue());
      }
    }
    catch (Exception e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000855") + e);
    }
    try
    {
      BillItem bbje = this.m_panel.getHeadItem(
        "bbje");
      bbje.setDecimalDigits(this.m_panel.getDjSettingParam().getDigit_b().intValue());

      BillItem bbye = this.m_panel.getBodyItem(
        "bbye");
      bbye.setDecimalDigits(this.m_panel.getDjSettingParam().getDigit_b().intValue());

      BillItem jfbbsj = this.m_panel.getBodyItem(
        "jfbbsj");
      if (jfbbsj != null) {
        jfbbsj.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_b().intValue());
      }

      BillItem dfbbsj = this.m_panel.getBodyItem(
        "dfbbsj");
      if (dfbbsj != null) {
        dfbbsj.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_b().intValue());
      }

      BillItem Wbfbbje = this.m_panel.getBodyItem(
        "wbfbbje");
      if (Wbfbbje != null) {
        Wbfbbje.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_b().intValue());
      }

      BillItem dfbbwsje = this.m_panel.getBodyItem("dfbbwsje");
      if (dfbbwsje != null)
        dfbbwsje.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_b().intValue());
    }
    catch (Exception e) {
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000856") + e);
    }

    if ((strDjdl.equals("ys")) || (strDjdl.equals("fk")) || 
      (strDjdl.equals("fj")) || (strDjdl.equals("wf")) || 
      (strDjdl.equals("ss"))) {
      try {
        if (this.m_panel.getDjSettingParam().getFracCurrPK() != null) {
          BillItem jffbje = this.m_panel.getBodyItem("jffbje");
          jffbje.setDecimalDigits(
            this.m_panel.getDjSettingParam().getDigit_f().intValue());
        }

        BillItem jfbbje = this.m_panel.getBodyItem("jfbbje");
        jfbbje.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_b().intValue());
      }
      catch (Exception e)
      {
        Log.getInstance(getClass()).error(e.getMessage(), e);
      }
    }
    else if ((strDjdl.equals("yf")) || (strDjdl.equals("sk")) || 
      (strDjdl.equals("sj")) || (strDjdl.equals("ws"))) {
      try {
        if (this.m_panel.getDjSettingParam().getFracCurrPK() != null) {
          BillItem jffbje = this.m_panel.getBodyItem("dffbje");
          jffbje.setDecimalDigits(
            this.m_panel.getDjSettingParam().getDigit_f().intValue());
        }

        BillItem jfbbje = this.m_panel.getBodyItem("dfbbje");
        jfbbje.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_b().intValue());
      }
      catch (Exception e)
      {
        Log.getInstance(getClass()).error(e.getMessage(), e);
      }
    }
    else {
      try {
        if (this.m_panel.getDjSettingParam().getFracCurrPK() != null) {
          BillItem jffbje = this.m_panel.getBodyItem("jffbje");
          jffbje.setDecimalDigits(
            this.m_panel.getDjSettingParam().getDigit_f().intValue());
        }

        BillItem jfbbje = this.m_panel.getBodyItem("jfbbje");
        jfbbje.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_b().intValue());
      }
      catch (Exception e)
      {
        Log.getInstance(getClass()).error(e.getMessage(), e);
      }
      try {
        if (this.m_panel.getDjSettingParam().getFracCurrPK() != null) {
          BillItem jffbje = this.m_panel.getBodyItem("dffbje");
          jffbje.setDecimalDigits(
            this.m_panel.getDjSettingParam().getDigit_f().intValue());
        }

        BillItem jfbbje = this.m_panel.getBodyItem("dfbbje");
        jfbbje.setDecimalDigits(
          this.m_panel.getDjSettingParam().getDigit_b().intValue());
      }
      catch (Exception e)
      {
        Log.getInstance(getClass()).error(e.getMessage(), e);
      }
    }
  }

  protected UFDouble getBodyBbhl(String pk_currtype, UFDouble ybje, UFDouble bbje, UFDouble fbje)
    throws Exception
  {
    UFDouble bbhl;
   // UFDouble bbhl;
    if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac().booleanValue())
      bbhl = Currency.getRateByAmount(this.m_panel.getDjSettingParam().getPk_corp(), this.m_panel.getDjSettingParam().getFracCurrPK(), this.m_panel.getDjSettingParam().getLocalCurrPK(), fbje, bbje);
    else
      bbhl = Currency.getRateByAmount(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, this.m_panel.getDjSettingParam().getLocalCurrPK(), ybje, bbje);
    return bbhl;
  }

  protected UFDouble getBodyBbje(String pk_currtype, UFDouble ybje, UFDouble fbje, String date, int row) throws Exception {
    if ((ybje == null) || (pk_currtype == null))
      return null;
    String localPk = this.m_panel.getDjSettingParam().getLocalCurrPK();
    if (localPk.equalsIgnoreCase(pk_currtype))
      return ybje;
    UFDouble bbhl = (UFDouble)this.m_panel.getBodyValueAt(row, "bbhl");
    UFDouble bbje;
   // UFDouble bbje;
    if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac().booleanValue())
    {
      if (fbje == null)
        fbje = getBodyFbje(pk_currtype, ybje, date, row);
      bbje = Currency.getAmountByOpp(this.m_panel.getDjSettingParam().getPk_corp(), this.m_panel.getDjSettingParam().getFracCurrPK(), localPk, fbje, bbhl, date);
    }
    else {
      bbje = Currency.getAmountByOpp(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, localPk, ybje, bbhl, date);
    }return bbje;
  }

  protected UFDouble getBodyFbhl(String pk_currtype, UFDouble ybje, UFDouble fbje) throws Exception
  {
    if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac().booleanValue())
      return Currency.getRateByAmount(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, this.m_panel.getDjSettingParam().getFracCurrPK(), ybje, fbje);
    return null;
  }

  protected UFDouble getBodyFbje(String pk_currtype, UFDouble ybje, String date, int row) throws Exception {
    if ((ybje == null) || (pk_currtype == null))
      return null;
    if (pk_currtype.equalsIgnoreCase(this.m_panel.getDjSettingParam().getFracCurrPK())) {
      return ybje;
    }
    UFDouble fbhl = (UFDouble)this.m_panel.getBodyValueAt(row, "fbhl");
    if (this.m_panel.getDjSettingParam().getIsBlnLocalFrac().booleanValue())
      return Currency.getAmountByOpp(this.m_panel.getDjSettingParam().getPk_corp(), pk_currtype, this.m_panel.getDjSettingParam().getFracCurrPK(), ybje, fbhl, date);
    return null;
  }

  protected void changeBodyFBH(BillEditEvent e) throws Exception {
    if ((e.getKey().indexOf("bbhl") > -1) || (e.getKey().indexOf("bbje") > -1))
      changeBodyBbhl_new(e);
    else if ((e.getKey().indexOf("fbhl") > -1) || (e.getKey().indexOf("fbje") > -1))
      changeBodyFbhl_new(e);
    else return;
  }

  protected abstract void changeBodyBbhl_new(BillEditEvent paramBillEditEvent)
    throws Exception;

  protected abstract void changeBodyFbhl_new(BillEditEvent paramBillEditEvent)
    throws Exception;
  
  /**
   * @功能:取月汇率
   * @author ：林桂莹
   * @2013/1/21
   * 
   * @since v50
   */
  private UFDouble getMonthRate(String curr,String oppcurr)
  {
	 UFDate serverdate= ClientEnvironment.getInstance().getDate();
	 int month= serverdate.getMonth();
	 int year=serverdate.getYear();
	 if(month==1)
	 {
		 year--;
		 month=12;
	 }
	 else 
		 month--;
	//add by zwx 2014-12-25 判断月份是否<10.若小于则在月份前添加'0'
    String newmonth="";
	if(month<10){
		newmonth = "0"+month;
	}else{
		newmonth = String.valueOf(month);
	}
	//end by zwx
     StringBuffer getRateSql=new StringBuffer();
     getRateSql.append(" SELECT rate.adjustrate ") 
	 .append("   FROM bd_adjustrate rate ") 
	 .append("  WHERE (nvl(dr, 0) = 0) ") 
	 .append("   and pk_currinfo =(select pk_currinfo from bd_currinfo where pk_corp='").append(ClientEnvironment.getInstance().getCorporation().getPk_corp()).append("' and pk_currtype='").append(curr).append("' and oppcurrtype='").append(oppcurr).append("')") 
	 .append("   and pk_accperiod = ( select pk_accperiod from bd_accperiod acc where acc.periodnum='12' and acc.periodyear='").append(year).append("') ")
//     .append("   and ratemonth ='0").append(month).append("' "); 
	 .append("   and ratemonth ='"+newmonth+"' "); //edit by zwx 2014-12-25 
     IUAPQueryBS sessionManager = (IUAPQueryBS) NCLocator
 	.getInstance().lookup(IUAPQueryBS.class.getName());
     HashMap rate = null;
     try {
    	 
    	  rate = (HashMap)sessionManager.executeQuery(getRateSql.toString(),new MapProcessor());
    	  if(rate==null||rate.size()==0)
    	  {
    			return new UFDouble(0.0);
    	  }
    	  String strrate=String.valueOf(rate.get("adjustrate"));
    	  if(strrate==null||strrate.equalsIgnoreCase("null"))
    	  {
    		  return new UFDouble(0.0); 
    	  }
    	  else 
    	  {
    		  return new UFDouble(strrate);
    	  }
         }
         catch (BusinessException e)
         {
        	 return new UFDouble(0.0);
         	
         }
	//return new UFDouble(0.0);
	  
  }
  
  /**
   * @功能:返回公司的上级公司编码
   * @author ：林桂莹
   * @2012/9/5
   * 
   * @since v50
   */
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