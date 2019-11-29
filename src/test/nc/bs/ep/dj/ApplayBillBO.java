package nc.bs.ep.dj;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import nc.bs.arap.billcooperation.BillCooperateBO;
import nc.bs.arap.callouter.FipCallFacade;
import nc.bs.arap.global.ArapExtInfRunBO;
import nc.bs.arap.outer.ArapPubShenheInterface;
import nc.bs.arap.outer.ArapPubUnShenheInterface;
import nc.bs.arap.outer.IArapPubEffectInterface;
import nc.bs.arap.outer.IArapPubUnEffectInterface;
import nc.bs.bd.b120.AccidDMO;
import nc.bs.ep.itemconfig.ItemconfigBO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.arap.proxy.Proxy;
import nc.itf.dmp.pub.IPFManaCache;
import nc.itf.fi.pub.KeyLock;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.arap.global.BusiTransVO;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.arap.verifynew.BusinessShowException;
import nc.vo.bd.b120.AccidVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.dmp.coststart.StarttableVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.ep.dj.ShenheException;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ExAggregatedVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class ApplayBillBO
{
  private FipCallFacade fipcallfacade;

  public ResMessage accountContr(DJZBVO dj)
    throws BusinessException, ShenheException
  {
    return shenhe_Sq(dj, true);
  }

  public DJZBVO afterShenheInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubShenheInterface)busitransvos[i].getInfClass()).afterShenheAct(dJZB);
        } catch (ClassCastException e) {
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();

          Log.getInstance(getClass()).debug(strerr);
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public ExAggregatedVO afterEffectInf(BusiTransVO[] busitransvos, ExAggregatedVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((IArapPubEffectInterface)busitransvos[i].getInfClass()).afterEffectAct(dJZB);
        } catch (ClassCastException e) {
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();

          Log.getInstance(getClass()).debug(strerr);
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public ExAggregatedVO afterUnEffectInf(BusiTransVO[] busitransvos, ExAggregatedVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((IArapPubUnEffectInterface)busitransvos[i].getInfClass()).afterUnEffectAct(dJZB);
        } catch (ClassCastException e) {
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();

          Log.getInstance(getClass()).debug(strerr);
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public DJZBVO afterUnShenheInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubUnShenheInterface)busitransvos[i].getInfClass()).afterUnShenheAct(dJZB);
        } catch (ClassCastException e) {
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();

          Log.getInstance(getClass()).debug(strerr);
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public ResMessage auditABill(DJZBVO dj)
    throws BusinessException
  {
    long t = System.currentTimeMillis();
    ResMessage res = new ResMessage();
    res.isSuccess = true;
    res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000543");
    DJZBBO curdjzbbo = new DJZBBO();
    res.intValue = -1;

    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])dj.getChildrenVO();

    if ((head.getShr() == null) || (head.getShr().trim().length() < 1)) {
      head.setShr(head.getLrr());
      if (dj.m_isQr == true) {
        head.setShrq((UFDate)dj.m_objOne);
        head.setShkjnd((String)dj.m_objTwo);
        head.setShkjqj((String)dj.m_objThree);
      } else {
        head.setShrq(head.getDjrq());
        head.setShkjnd(head.getDjkjnd());
        head.setShkjqj(head.getDjkjqj());
      }
    }
    ArapDjBsCheckerBO bocheck = new ArapDjBsCheckerBO();
    try
    {
      boolean bIssettled = bocheck.isSettled(head.getDwbm(), head.getPzglh().intValue(), dj);
      if (bIssettled)
        throw new BusinessShowException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000071"), new Exception(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000071")));
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000071"), e);
    }

    DJZBDAO djdmo = null;
    try {
      djdmo = new DJZBDAO();
      ARAPDjBSUtil.supplementItems(dj);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("distributeDjzb  Exception!", e);
    }
    try {
      dj.setParam_Ext_Save();
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("nc.bs.ep.dj.ApplayBillBO.auditABill(DJZBVO) setParam_Ext_Save:" + e);
    }

    Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000544"));
    res.djzbvo = dj;

    long t1 = System.currentTimeMillis();
    ArapExtInfRunBO extbo = new ArapExtInfRunBO();
    BusiTransVO[] busitransvos = extbo.initBusiTrans("shenhe", head.getPzglh());

    beforeShenheInf(busitransvos, dj);
    Log.getInstance(getClass()).debug("外接口审核单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

    ARAPDjVOUtil.supplementValidationInfo(dj);
    try
    {
      bocheck.supplementAllInfos(new DJZBVO[] { dj });
    } catch (Exception e) {
      throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000545"), e);
    }

    res.djzbvo = dj;
    Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000546") + (System.currentTimeMillis() - t));
    try
    {
      res.intValue = 2;

      if (head.getSsflag().equals("1")) {
        ItemconfigBO itemconfigbo = new ItemconfigBO();

        curdjzbbo.lock_item_bill(items, head.getLrr(), 1);
        itemconfigbo.ShenHeSave(dj);
        curdjzbbo.lock_item_bill(items, head.getLrr(), 1);
      }

      if ((head.getXtflag() != null) && (head.getXtflag().equals("审核或签字确认")))
      {
        BillCooperateBO bic = new BillCooperateBO();
        bic.doCooperate(dj);
      }

      Log.getInstance(getClass()).debug("ApplayBillBo4");

      long t2 = System.currentTimeMillis();

      afterShenheInf(busitransvos, dj);

      Log.getInstance(getClass()).debug("ApplayBillBo5");
      Log.getInstance(getClass()).debug("外接口审核单据前动作后所用时间:" + (System.currentTimeMillis() - t2));
      if (dj.m_Resmessage != null) {
        res.strMessage += dj.m_Resmessage.strMessage;
        res.isSuccess = dj.m_Resmessage.isSuccess;
      }

      String tablename = "arap_djzb";
      if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
        tablename = "arap_item";
      res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), tablename);
      head.setTs(new UFDateTime(res.m_Ts));
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }

    return res;
  }

  public ResMessage auditABill2(DJZBVO dj, BusiTransVO[] busitransvos)
    throws BusinessException
  {
    return auditABill2(dj, busitransvos, true);
  }

  public ResMessage auditABill2(DJZBVO dj, BusiTransVO[] busitransvos, boolean checkFlow)
    throws BusinessException
  {
    EffectAction ea = new EffectAction();
    dj = ea.doEffectAction(dj);

    ResMessage res = new ResMessage();
    res.isSuccess = true;
    res.strMessage = "";
    DJZBBO curdjzbbo = new DJZBBO();
    res.intValue = -1;

    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    DJZBDAO djdmo = null;
    try {
      djdmo = new DJZBDAO();

      ApplayBillDMO dm = new ApplayBillDMO();

      int iStatus = Proxy.getIPFWorkflowQry().queryWorkflowStatus((head.getXslxbm() == null) || (head.getXslxbm().trim().length() < 1) ? "KHHH0000000000000001" : head.getXslxbm().trim(), head.getDjlxbm(), head.getVouchid());

      if ((checkFlow) && (iStatus != 4) && (iStatus != 5))
      {
        res.isSuccess = false;
        res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000549");
        return res;
      }

      if (head.getIsQr().equalsIgnoreCase("Y"))
      {
        head.setSxbz(new Integer(0));
      }
      else if ((dj.getCanBeCommissioned()) && (!DJZBVOConsts.FromFTS.equals(head.getLybz())))
        head.setSxbz(new Integer(5));
      else {
        head.setSxbz(new Integer(10));
      }

      dm.setFlagBill_distribute((DJZBHeaderVO)dj.getParentVO());

      if (head.getIsQr().equalsIgnoreCase("Y"))
      {
        res.intValue = 2;
        res.djzbvo = dj;
        res.m_Ts = head.getts().toString();

        return res;
      }

    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);

      throw new BusinessShowException(e.getMessage());
    }

    DJZBItemVO[] items = dj.getChildrenVO() == null ? null : (DJZBItemVO[])(DJZBItemVO[])dj.getChildrenVO();

    if (items == null)
    {
      try {
        if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
        {
          items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
        }
        else items = djdmo.findItemsForHeader(head.getPrimaryKey()); 
      }
      catch (Exception e) {
        res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000148");
        res.isSuccess = false;
        return res;
      }
      dj.setChildrenVO(items);
    }

    ARAPDjVOUtil.supplementValidationInfo(dj);

    res.djzbvo = dj;

    long t1 = System.currentTimeMillis();
    beforeShenheInf(busitransvos, dj);
    Log.getInstance(getClass()).debug("外接口审核单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

    res.djzbvo = dj;
    try
    {
      if (head.getSsflag().equals("1")) {
        ItemconfigBO itemconfigbo = new ItemconfigBO();
        curdjzbbo.lock_item_bill(items, head.getLrr(), 1);

        itemconfigbo.ShenHeSave(dj);
        curdjzbbo.lock_item_bill(items, head.getLrr(), 1);
      }

      Log.getInstance(getClass()).debug("协同单据...");
      if ((head.getXtflag() != null) && (head.getXtflag().equals("审核或签字确认")))
      {
        BillCooperateBO bic = new BillCooperateBO();
        bic.doCooperate(dj);
      }

      long t2 = System.currentTimeMillis();

      afterShenheInf(busitransvos, dj);
      Log.getInstance(getClass()).debug("外接口审核单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      if (dj.m_Resmessage != null) {
        res.strMessage += dj.m_Resmessage.strMessage;
        res.isSuccess = dj.m_Resmessage.isSuccess;
      }

      if (!dj.getCanBeCommissioned())
      {
        sendMessage(dj);
      }
      try {
        res = shenhe_Sq(dj, true);
      }
      catch (Exception e) {
        throw e;
      }
      if (!res.isSuccess)
      {
        return res;
      }
      res.intValue = 2;

      String tablename = "arap_djzb";
      if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
        tablename = "arap_item";
      res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), tablename);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);

      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessShowException(e.getMessage());
    }

    Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000548"));
    return res;
  }

  public String[] bankBill(DJZBVO[] djs)
    throws BusinessException
  {
    String[] result = new String[djs.length];

    return result;
  }

  public DJZBVO beforeShenheInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubShenheInterface)busitransvos[i].getInfClass()).beforeShenheAct(dJZB);
        } catch (ClassCastException e) {
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();

          Log.getInstance(getClass()).debug(strerr);
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public ExAggregatedVO beforeEffectInf(BusiTransVO[] busitransvos, ExAggregatedVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((IArapPubEffectInterface)busitransvos[i].getInfClass()).beforeEffectAct(dJZB);
        } catch (ClassCastException e) {
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();

          Log.getInstance(getClass()).debug(strerr);
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public ExAggregatedVO beforeUnEffectInf(BusiTransVO[] busitransvos, ExAggregatedVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((IArapPubUnEffectInterface)busitransvos[i].getInfClass()).beforeUnEffectAct(dJZB);
        } catch (ClassCastException e) {
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();

          Log.getInstance(getClass()).debug(strerr);
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public DJZBVO beforeUnShenheInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubUnShenheInterface)busitransvos[i].getInfClass()).beforeUnShenheAct(dJZB);
        }
        catch (ClassCastException e) {
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();

          Log.getInstance(getClass()).debug(strerr);
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public String[] confirmBill(DJZBVO[] djs)
    throws BusinessException
  {
    String[] result = new String[djs.length];

    return result;
  }

  public String getPk_invclByPk(String key)
    throws BusinessException
  {
    String pk_invcl = null;
    try {
      ApplayBillDMO dmo = new ApplayBillDMO();
      pk_invcl = dmo.getPk_invclByPk(key);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
    return pk_invcl;
  }

  public Integer iBillAfter(String sourceId)
    throws BusinessException
  {
    int iRe = 0;
    try
    {
      if ((sourceId == null) || (sourceId.trim().length() < 2))
        return null;
      DJZBDAO dmo = new DJZBDAO();
      Integer iCon = dmo.getClbzByPkey(sourceId);
      if (iCon != null)
      {
        switch (iCon.intValue()) {
        case -2:
          throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000553"), new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000554")));
        case -1:
          throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000553"), new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000555")));
        case 0:
          throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000553"), new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000556")));
        case 1:
          throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000553"), new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000557")));
        case 2:
          throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000553"), new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000558")));
        case 3:
          throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000553"), new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000559")));
        case 4:
          throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000553"), new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000560")));
        case 5:
          throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000553"), new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000561")));
        case 6:
          throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000553"), new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000562")));
        }

      }
      else
      {
        iRe = -999;
      }
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
    return new Integer(iRe);
  }

  public void insertShenhe_sell(DJZBVO dj)
    throws BusinessException
  {
    try
    {
      DJZBBO djbo = new DJZBBO();
      dj = djbo.insert(dj);

      ARAPDjAlienBill app = new ARAPDjAlienBill();
      app.auditOneBill(dj);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
  }

  public boolean isApplayBill(DJZBVO vo, String produid)
    throws BusinessException
  {
    String dwbm = vo.getParentVO().getAttributeValue("dwbm").toString();

    String shnd = vo.getParentVO().getAttributeValue("shkjnd").toString();
    String shrq = vo.getParentVO().getAttributeValue("shkjqj").toString();
    String[] period = null;
    try
    {
      period = Proxy.getICreateCorpQueryService().querySettledPeriod(dwbm, produid);

      if ((period != null) && 
        (period[0] != null) && (period[1] != null) && (!period[0].trim().equals("")) && (!period[1].trim().equals("")))
      {
        if ((period[0] + period[1]).compareTo(shnd + shrq) >= 0) {
          throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000563"));
        }
      }
    }
    catch (Exception e)
    {
      throw new BusinessShowException(e.getMessage());
    }
    return true;
  }

  public ResMessage isDistributes(DJZBVO vo)
    throws BusinessException
  {
    try
    {
      try
      {
        ARAPDjBSUtil.supplementCanCommission(vo, null);
        ARAPDjBSUtil.supplementSignature(vo);
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        throw new BusinessShowException(e.getMessage());
      }

      DJZBDAO djdmo = new DJZBDAO();

      DJZBHeaderVO head = (DJZBHeaderVO)vo.getParentVO();
      ResMessage res = new ResMessage();
      res.isSuccess = true;
      res.intValue = 2;
      res.djzbvo = vo;

      if (head.getts() == null)
        return res;
      res.m_Ts = head.getts().toString();

      djdmo.distributeDjzb_cf(head.getVouchid(), head.getts().toString(), head.getDjdl(), head.getDjzt());

      return res;
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
    
  }

  public boolean isUsedFC()
    throws BusinessException
  {
    StarttableVO vo = Proxy.getIPFManaCache().getCostStartInfo(InvocationInfoProxy.getInstance().getUserDataSource(), 2, new UFBoolean("N"));

    if ((vo == null) || (vo.getIsStart() == null)) {
      return false;
    }

    return vo.getIsStart().booleanValue();
  }

  public boolean isUsedHC()
    throws BusinessException
  {
    StarttableVO vo = Proxy.getIPFManaCache().getCostStartInfo(InvocationInfoProxy.getInstance().getUserDataSource(), 1, new UFBoolean("N"));

    if ((vo == null) || (vo.getIsStart() == null)) {
      return false;
    }

    return vo.getIsStart().booleanValue();
  }

  public boolean lockBill(DJZBVO vo)
    throws BusinessException
  {
    try
    {
      if (!KeyLock.lockKey(vo.getParentVO().getPrimaryKey(), "123", null))
      {
        throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000564"));
      }
    }
    catch (Exception e) {
      throw new BusinessShowException(e.getMessage());
    }

    return false;
  }

  public String qr(DJZBVO dj)
    throws BusinessException
  {
    String result = "";
    try
    {
      ApplayBillDMO dm = new ApplayBillDMO();
      dm.qr((DJZBHeaderVO)dj.getParentVO());
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }

    return result;
  }

  public void sendMessage(DJZBVO dj)
    throws BusinessException
  {
    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    long t = System.currentTimeMillis();

    if (!head.getDjdl().equals("ss"))
    {
      ExAggregatedVO djVo = new ExAggregatedVO(dj);
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();
      BusiTransVO[] busitransvos = extbo.initBusiTrans("effect", head.getPzglh());

      beforeEffectInf(busitransvos, djVo);

      DapMsgVO PfStateVO = DjbsStatTool.getDapmsgVo(dj);

      PfStateVO.setMsgType(0);
      PfStateVO.setRequestNewTranscation(false);

      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000534"));

      getFipcallfacade().sendMessage(PfStateVO, dj);

      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000565") + (System.currentTimeMillis() - t));

      t = System.currentTimeMillis();
      try
      {
        afterEffectInf(busitransvos, djVo);
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException(e.getMessage(), e);
      }
      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000540") + (System.currentTimeMillis() - t));
    }
  }

  public void sendMessage_del(DJZBVO dj) throws BusinessException
  {
    sendMessage_del(dj, UFBoolean.FALSE);
  }

  public void sendMessage_del(DJZBVO dj, UFBoolean NewTransaction)
    throws BusinessException
  {
    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    if (!head.getDjdl().equals("ss"))
    {
      DapMsgVO PfStateVO = DjbsStatTool.getDapmsgVo(dj);
      PfStateVO.setRequestNewTranscation(false);

      PfStateVO.setMsgType(1);

      ArapExtInfRunBO extbo = new ArapExtInfRunBO();
      BusiTransVO[] busitransvos = extbo.initBusiTrans("uneffect", head.getPzglh());

      ExAggregatedVO djvo = new ExAggregatedVO(dj);
      if ((((DJZBHeaderVO)dj.getParentVO()).getPrepay() != null) && (((DJZBHeaderVO)dj.getParentVO()).getPrepay().booleanValue()) && ((((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("sk")) || (((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("fk"))))
      {
        DapMsgVO pfvo = DjbsStatTool.getDapmsgVOforPrePay(dj);

        pfvo.setMsgType(1);

        getFipcallfacade().sendMessage(pfvo, dj);
      }

      beforeUnEffectInf(busitransvos, djvo);

      Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000534"));

      getFipcallfacade().sendMessage(PfStateVO, dj);

      afterUnEffectInf(busitransvos, djvo);
    }

    Log.getInstance(getClass()).debug("sendMessage_del is over!");
  }

  public void hasVoucher(DJZBVO dj) throws BusinessException {
    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    boolean isAccCanBack = true;
    if (!head.getDjdl().equals("ss"))
    {
      if ((((DJZBHeaderVO)dj.getParentVO()).getPrepay() != null) && (((DJZBHeaderVO)dj.getParentVO()).getPrepay().booleanValue()) && ((((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("sk")) || (((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("fk"))))
      {
        DapMsgVO pfvo = DjbsStatTool.getDapmsgVOforPrePay(dj);

        pfvo.setMsgType(1);
        isAccCanBack = getFipcallfacade().isEditBillTypeOrProc(head.getDwbm(), pfvo.getSys(), pfvo.getProc(), pfvo.getBusiType(), pfvo.getProcMsg());
        if (!isAccCanBack)
          throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000566"));
      }
    }
  }

  public ResMessage shenhe_Sq(DJZBVO dj, boolean isShenHe)
    throws BusinessException, ShenheException
  {
    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    ResMessage res = new ResMessage();
    res.isSuccess = true;
    res.djzbvo = dj;
    if (head.getts() != null)
      res.m_Ts = head.getts().toString();
    if ((head.getDjdl() == null) || (head.getDjdl().trim().length() < 1))
      return res;
    res = shenhe_Sq_All(dj, isShenHe);
    if (head.getts() != null)
      res.m_Ts = head.getts().toString();
    return res;
  }

  /** @deprecated */
  public ResMessage shenhe_Sq_fj(DJZBVO dj, boolean isShenHe)
    throws BusinessException
  {
    return shenhe_Sq_All(dj, isShenHe);
  }

  /** @deprecated */
  public ResMessage shenhe_Sq_fk(DJZBVO dj, boolean isShenHe)
    throws BusinessException
  {
    return shenhe_Sq_All(dj, isShenHe);
  }

  /** @deprecated */
  public ResMessage shenhe_Sq_hj(DJZBVO dj, boolean isShenHe)
    throws BusinessException
  {
    return shenhe_Sq_All(dj, isShenHe);
  }

  /** @deprecated */
  public ResMessage shenhe_Sq_sj(DJZBVO dj, boolean isShenHe)
    throws BusinessException, ShenheException
  {
    return shenhe_Sq_sk(dj, isShenHe);
  }

  /** @deprecated */
  public ResMessage shenhe_Sq_sk(DJZBVO dj, boolean isShenHe)
    throws BusinessException, ShenheException
  {
    return shenhe_Sq_All(dj, isShenHe);
  }

  /** @deprecated */
  public ResMessage shenhe_Sq_wf(DJZBVO dj, boolean isShenHe)
    throws BusinessException
  {
    return shenhe_Sq_All(dj, isShenHe);
  }

  /** @deprecated */
  public ResMessage shenhe_Sq_ws(DJZBVO dj, boolean isShenHe)
    throws BusinessException, ShenheException
  {
    return shenhe_Sq_All(dj, isShenHe);
  }

  private ResMessage shenhe_Sq_All(DJZBVO dj, boolean isShenHe)
    throws BusinessException
  {
    DJZBItemVO[] items = null;
    String pk_accid = "";
    String pk_currtype = "";
    AccidVO accidvo = null;
    UFDouble changedValue = new UFDouble(0.0D);
    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();
    ResMessage res = new ResMessage();
    res.isSuccess = true;
    res.djzbvo = dj;
    ShenheException shenheE = new ShenheException();
    ArapBusinessException shygsqex = new ArapBusinessException();
    res.intValue = -1;
    Vector v = new Vector();
    try {
      if ((dj.getChildrenVO() == null) || (dj.getChildrenVO().length < 1))
      {
        DJZBDAO djdmo = new DJZBDAO();
        items = djdmo.findItemsForHeader(head.getVouchid());
      } else {
        items = (DJZBItemVO[])(DJZBItemVO[])dj.getChildrenVO();
      }

      AccidDMO acciddmo = new AccidDMO();
      Hashtable hAccidVO = new Hashtable();
      for (int i = 0; i < items.length; i++) {
        res.isSuccess = true;
        res.intValue = 1;
        res.strMessage = "";
        pk_accid = items[i].getAccountid();
        pk_currtype = items[i].getBzbm();

        if ((pk_accid == null) || (pk_accid.trim().length() == 0))
          continue;
        String strHashKey = pk_accid + pk_currtype;
        if (hAccidVO.get(strHashKey) == null) {
          accidvo = acciddmo.findByPkandBz(pk_accid, pk_currtype);
          if ((accidvo == null) || (accidvo.getPrimaryKey() == null))
          {
            if ((!"ws".equals(head.getDjdl())) && (!"wf".equals(head.getDjdl())))
              continue;
            res.isSuccess = false;
            res.intValue = 1;
            res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000599");
            shenheE = new ShenheException(res.strMessage);
            shenheE.m_ResMessage = res;
            throw shenheE;
          }

          hAccidVO.put(strHashKey, accidvo);
        } else {
          accidvo = (AccidVO)hAccidVO.get(strHashKey);
        }
        if (accidvo != null)
        {
          if ("1".equals(accidvo.getFrozenflag())) {
            res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000569");
            res.isSuccess = false;
            res.intValue = 1;
            shenheE.m_ResMessage = res;
            shenheE.setBusiStyle(0);
            throw shenheE;
          }if ("2".equals(accidvo.getFrozenflag())) {
            res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000570");
            res.isSuccess = false;
            res.intValue = 1;
            shenheE.m_ResMessage = res;
            throw shenheE;
          }
        }

        if (accidvo.getCurrmny() == null)
          accidvo.setCurrmny(new UFDouble(0.0D));
        changedValue = items[i].getYbye();

        int shzb = isShenHe ? 1 : -1;
        int zfbz = getDJZFBZ(head.getDjdl(), items[i]);
        changedValue = new UFDouble(accidvo.getCurrmny().doubleValue() + shzb * zfbz * changedValue.doubleValue());

        if ((changedValue.doubleValue() < (accidvo.getDeficit() == null ? 0.0D : accidvo.getDeficit().doubleValue())) && (changedValue.doubleValue() < accidvo.getCurrmny().doubleValue()) && (accidvo.getIscont().booleanValue()))
        {
          if ("0".equals(accidvo.getContype()))
          {
            res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000600", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });
            res.itemRow = i;
            res.accidname = accidvo.getAccidname();
            res.pk_accid = accidvo.getPk_accid();
            v.addElement(res.strMessage); } else {
            if ("1".equals(accidvo.getContype()))
            {
              res.isSuccess = false;
              res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000601", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });
              res.itemRow = i;
              res.accidname = accidvo.getAccidname();
              res.pk_accid = accidvo.getPk_accid();
              shygsqex = new ArapBusinessException(res.strMessage);
              shygsqex.m_ResMessage = res;
              throw shygsqex;
            }

            if (items[i].getIsSqed().booleanValue()) {
              res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000602", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });
            } else {
              if (items[i].getSqflag().intValue() == 0) {
                res.isSuccess = false;
                res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000582", null, new String[] { String.valueOf(i + 1) });
                res.intValue = 9999;
                res.itemRow = i;
                res.accidname = accidvo.getAccidname();
                res.pk_accid = accidvo.getPk_accid();
                shenheE = new ShenheException(res.strMessage);
                shenheE.setBusiStyle(1);
                shenheE.m_ResMessage = res;
                throw shenheE;
              }if (items[i].getSqflag().intValue() == 9) {
                res.isSuccess = false;
                res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000583", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });
                res.itemRow = i;
                res.accidname = accidvo.getAccidname();
                res.pk_accid = accidvo.getPk_accid();
                res.intValue = -9;
                shygsqex = new ArapBusinessException(res.strMessage);
                shygsqex.m_ResMessage = res;
                throw shygsqex;
              }

              res.isSuccess = false;
              res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000584", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });
              res.itemRow = i;
              res.accidname = accidvo.getAccidname();
              res.pk_accid = accidvo.getPk_accid();
              res.intValue = -8;
              shygsqex = new ArapBusinessException(res.strMessage);
              shygsqex.m_ResMessage = res;
              throw shygsqex;
            }
          }

        }

        if (accidvo.getHighmnyiscon().booleanValue()) {
          if ((changedValue.doubleValue() > (accidvo.getHighmny() == null ? 0.0D : accidvo.getHighmny().doubleValue())) && (changedValue.doubleValue() > accidvo.getCurrmny().doubleValue()) && (accidvo.getHighmnyiscon().booleanValue()))
          {
            if ("0".equals(accidvo.getHighmnycontype()))
            {
              res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000585", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });

              res.itemRow = i;
              res.accidname = accidvo.getAccidname();
              res.pk_accid = accidvo.getPk_accid();
              v.addElement(res.strMessage); } else {
              if ("1".equals(accidvo.getHighmnycontype()))
              {
                res.isSuccess = false;
                res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000586", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });

                res.itemRow = i;
                res.accidname = accidvo.getAccidname();
                res.pk_accid = accidvo.getPk_accid();
                shygsqex = new ArapBusinessException(res.strMessage);
                shygsqex.m_ResMessage = res;
                throw shygsqex;
              }

              if (items[i].getIsSqed().booleanValue()) {
                res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000587", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });
              }
              else {
                if (items[i].getSqflag().intValue() == 0) {
                  res.isSuccess = false;
                  res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000588", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });

                  res.intValue = 9999;
                  res.itemRow = i;
                  res.accidname = accidvo.getAccidname();
                  res.pk_accid = accidvo.getPk_accid();
                  shenheE = new ShenheException(res.strMessage);
                  shenheE.setBusiStyle(1);
                  shenheE.m_ResMessage = res;
                  throw shenheE;
                }if (items[i].getSqflag().intValue() == 9) {
                  res.isSuccess = false;
                  res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000583", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });

                  res.itemRow = i;
                  res.accidname = accidvo.getAccidname();
                  res.pk_accid = accidvo.getPk_accid();
                  res.intValue = -9;
                  shygsqex = new ArapBusinessException(res.strMessage);

                  shygsqex.m_ResMessage = res;
                  throw shygsqex;
                }

                res.isSuccess = false;
                res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000584", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });

                res.itemRow = i;
                res.accidname = accidvo.getAccidname();
                res.pk_accid = accidvo.getPk_accid();
                res.intValue = -8;
                shygsqex = new ArapBusinessException(res.strMessage);
                shygsqex.m_ResMessage = res;
                throw shygsqex;
              }
            }
          }

        }

        accidvo.setCurrmny(changedValue);
      }
      Enumeration voKeys = hAccidVO.keys();
      while (voKeys.hasMoreElements()) {
        String nextKey = (String)voKeys.nextElement();
        accidvo = (AccidVO)hAccidVO.get(nextKey);
        acciddmo.update_curYe(accidvo);
      }
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessShowException(e.getMessage(), e);
    }
    res.strMessage = "";
    if ((v != null) && (v.size() > 0))
      res.strMessage = v.elementAt(0).toString();
    return res;
  }

  private int getDJZFBZ(String djdl, DJZBItemVO item)
  {
    if ("sk".equals(djdl)) return 1;
    if ("fk".equals(djdl)) return -1;
    if ("sj".equals(djdl)) return 1;
    if ("fj".equals(djdl)) return -1;
    if ("ws".equals(djdl)) return 1;
    if ("wf".equals(djdl)) return -1;
    if ("hj".equals(djdl))
    {
      if (item.getFx().intValue() == 1) return -1;
      return 1;
    }
    return 0;
  }

  public ResMessage unAuditABill(DJZBVO dj)
    throws BusinessException
  {
    ResMessage res = new ResMessage();
    res.isSuccess = true;
    DJZBBO curdjzbbo = new DJZBBO();
    res.djzbvo = dj;
    res.intValue = -1;

    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    DJZBDAO djdmo = null;
    try {
      djdmo = new DJZBDAO();
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }

    DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])dj.getChildrenVO();

    if (items == null)
    {
      try {
        if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
        {
          items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
        }
        else items = djdmo.findItemsForHeader(head.getPrimaryKey()); 
      }
      catch (Exception e) {
        res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000148");
        res.isSuccess = false;
        return res;
      }
      dj.setChildrenVO(items);
    }

    try
    {
      dj.setParam_Ext_Save();
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }

    res.djzbvo = dj;
    List lst = new ArrayList();

    for (int i = 0; i < items.length; i++) {
      if ((items[i].getPausetransact() == null) || (!items[i].getPausetransact().booleanValue()))
        continue;
      throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000609"));
    }

    for (int i = 0; i < items.length; i++) {
      if ((DJZBVOConsts.BODY_PAY_FAILURE.equals(items[i].getPayflag())) || (DJZBVOConsts.BODY_PAY_DOWN.equals(items[i].getPayflag()))) {
        items[i].setPayflag(null);
        lst.add(items[i]);
      }
    }

    try
    {
      if (lst.size() > 0) {
        djdmo.wszzFB((DJZBItemVO[])(DJZBItemVO[])lst.toArray(new DJZBItemVO[0]));
      }
      ArapDjBsCheckerBO checkbo = new ArapDjBsCheckerBO();
      ResMessage tempres = checkbo.checkUnApproveBill(dj);
      if ((tempres != null) && (!tempres.isSuccess)) {
        throw new BusinessException(tempres.strMessage);
      }

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();
      BusiTransVO[] busitransvos = extbo.initBusiTrans("unshenhe", head.getPzglh());

      beforeUnShenheInf(busitransvos, dj);
      Log.getInstance(getClass()).debug("外接口反审核单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

      if ((!head.getDjdl().equals("ss")) && (head.getSsflag().equals("1")))
      {
        ItemconfigBO itemconfigbo = new ItemconfigBO();
        curdjzbbo.lock_item_bill(items, head.getLrr(), 1);

        itemconfigbo.unShenHe((DJZBVO)dj.clone(), false);
        curdjzbbo.lock_item_bill(items, head.getLrr(), 1);
      }

      res = shenhe_Sq(dj, false);
      if (!res.isSuccess) {
        return res;
      }
      res.intValue = 1;

      if ((head.getXtflag() != null) && (head.getXtflag().equals("审核或签字确认")))
      {
        curdjzbbo.deleteOutBill(head.getVouchid());
      }

      long t2 = System.currentTimeMillis();

      afterUnShenheInf(busitransvos, dj);
      Log.getInstance(getClass()).debug("外接口反审核单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      if (dj.m_Resmessage != null) {
        res.strMessage += dj.m_Resmessage.strMessage;
        res.isSuccess = dj.m_Resmessage.isSuccess;
      }

      res.intValue = 2;
      String tablename = "arap_djzb";
      if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
        tablename = "arap_item";
      res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), tablename);
      head.setTs(new UFDateTime(res.m_Ts));
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessShowException(e.getMessage());
    }

    return res;
  }

  public ResMessage unAuditABill2(DJZBVO dj, BusiTransVO[] busitransvos) throws BusinessException
  {
    return unAuditABill2(dj, busitransvos, UFBoolean.FALSE, UFBoolean.TRUE);
  }

  public ResMessage unAuditABill2(DJZBVO dj, BusiTransVO[] busitransvos, UFBoolean canWorkFlow, UFBoolean isClientInvoked)
    throws BusinessException
  {
    ResMessage res = new ResMessage();
    res.isSuccess = true;
    DJZBBO curdjzbbo = new DJZBBO();
    res.djzbvo = dj;
    res.intValue = -1;

    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    DJZBDAO djdmo = null;

    if ((head.getSpzt() != null) && (head.getSpzt().trim().equals("0")))
    {
      res.isSuccess = false;
      res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000611");
      return res;
    }
    try
    {
      int iStatus = Proxy.getIPFWorkflowQry().queryWorkflowStatus((head.getXslxbm() == null) || (head.getXslxbm().trim().length() < 1) ? "KHHH0000000000000001" : head.getXslxbm().trim(), head.getDjlxbm(), head.getVouchid());

      if ((iStatus != 4) && (iStatus != 5) && (!canWorkFlow.booleanValue()))
      {
        res.isSuccess = false;
        res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000612");
        return res;
      }

      djdmo = new DJZBDAO();

      ApplayBillDMO dm = new ApplayBillDMO();

      dm.setunFlagBill_distribute((DJZBHeaderVO)dj.getParentVO());
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage(), e);
    }

    if (head.getSxbz().intValue() == 0)
    {
      res.intValue = 2;
      res.djzbvo = dj;
      res.m_Ts = head.getts().toString();

      return res;
    }

    DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])dj.getChildrenVO();

    if (items == null)
    {
      try {
        if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
        {
          items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
        }
        else items = djdmo.findItemsForHeader(head.getPrimaryKey()); 
      }
      catch (Exception e) {
        res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000148");
        res.isSuccess = false;
        return res;
      }
      dj.setChildrenVO(items);
    }

    res.djzbvo = dj;
    if ((isClientInvoked.booleanValue()) || (new Integer(0).equals(head.getZgyf()))) {
      for (int i = 0; i < items.length; i++) {
        if ((items[i].getPausetransact() == null) || (!items[i].getPausetransact().booleanValue()))
        {
          continue;
        }

        throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000609"));
      }
    }

    List lst = new ArrayList();

    for (int i = 0; i < items.length; i++) {
      if ((DJZBVOConsts.BODY_PAY_FAILURE.equals(items[i].getPayflag())) || (DJZBVOConsts.BODY_PAY_DOWN.equals(items[i].getPayflag()))) {
        items[i].setPayflag(null);
        lst.add(items[i]);
      }
    }

    long t1 = System.currentTimeMillis();

    beforeUnShenheInf(busitransvos, dj);
    Log.getInstance(getClass()).debug("外接口反审核单据前动作前所用时间:" + (System.currentTimeMillis() - t1));
    try
    {
      if (lst.size() > 0) {
        djdmo.wszzFB((DJZBItemVO[])(DJZBItemVO[])lst.toArray(new DJZBItemVO[0]));
      }
      if (head.getSsflag().equals("1"))
      {
        ItemconfigBO itemconfigbo = new ItemconfigBO();
        curdjzbbo.lock_item_bill(items, head.getLrr(), 1);

        itemconfigbo.unShenHe((DJZBVO)dj.clone(), false);
        curdjzbbo.lock_item_bill(items, head.getLrr(), 1);
      }

      res = shenhe_Sq(dj, false);
      if (!res.isSuccess) {
        return res;
      }
      res.intValue = 1;

      if ((head.getXtflag() != null) && (head.getXtflag().equals("审核或签字确认")))
      {
        curdjzbbo.deleteOutBill(head.getVouchid());
      }

      long t2 = System.currentTimeMillis();

      afterUnShenheInf(busitransvos, dj);
      Log.getInstance(getClass()).debug("外接口反审核单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      if (dj.m_Resmessage != null) {
        res.strMessage += dj.m_Resmessage.strMessage;
        res.isSuccess = dj.m_Resmessage.isSuccess;
      }

      res.intValue = 2;

      if (head.getSxbz().intValue() == 10)
      {
        sendMessage_del(dj, UFBoolean.FALSE);
      }

      String tablename = "arap_djzb";
      if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
        tablename = "arap_item";
      res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), tablename);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessShowException(e.getMessage());
    }

    return res;
  }

  public String[] unBankBill(DJZBVO[] djs)
    throws BusinessException
  {
    String[] result = new String[djs.length];

    return result;
  }

  public String[] unConfirmBill(DJZBVO[] djs)
    throws BusinessException
  {
    String[] result = new String[djs.length];

    return result;
  }

  public DJZBVO befUnQr(DJZBVO dj)
    throws BusinessException
  {
    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])dj.getChildrenVO();

    DJZBBO djzbbo = new DJZBBO();
    long t1 = System.currentTimeMillis();
    ArapExtInfRunBO extbo = new ArapExtInfRunBO();

    BusiTransVO[] busitransvos = extbo.initBusiTrans("del", head.getPzglh());

    djzbbo.beforeDelInf(busitransvos, dj);
    Log.getInstance(getClass()).warn("del外接口修改单据前动作前所用时间:" + (System.currentTimeMillis() - t1));
    Proxy.getIWorkflowMachine().deleteCheckFlow(head.getDjlxbm(), head.getVouchid(), head.getShr(), true);

    DJZBDAO djdmo = new DJZBDAO();
    try {
      djdmo.distributeDjzb_Item(head.getVouchid(), head.getts().toString(), head.getDjdl());

      ItemconfigBO itemconfigbo = new ItemconfigBO();
      djzbbo.lock_item_bill(items, head.getLrr(), 1);
      itemconfigbo.unShenHe((DJZBVO)dj.clone(), false);
      djzbbo.lock_item_bill(items, head.getLrr(), 1);
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException(e.getMessage(), e);
    }

    return dj;
  }

  public ResMessage unQr(DJZBVO dj)
    throws BusinessException
  {
    try
    {
      dj = befUnQr(dj);
      DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

      ApplayBillDMO dm = new ApplayBillDMO();
      dm.unQr(head);
      DJZBBO djbo = new DJZBBO();
      djbo.returnBillCode(dj, false);
      return afterUnQr(dj);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
      throw new BusinessException("ApplayBillBO::unQr(DJZBVO) Exception!", e);
    }
    
  }

  public ResMessage afterUnQr(DJZBVO dj)
    throws BusinessException
  {
    ResMessage res = new ResMessage();
    res.isSuccess = true;
    DJZBDAO djdmo = new DJZBDAO();
    DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

    DJZBBO djzbbo = new DJZBBO();

    if ((head.getXtflag() != null) && (head.getXtflag().equals("保存"))) {
      DJZBBO zbbo = new DJZBBO();

      zbbo.deleteOutBill(head.getVouchid());
    }
    ArapExtInfRunBO extbo = new ArapExtInfRunBO();

    BusiTransVO[] busitransvos = extbo.initBusiTrans("del", head.getPzglh());

    djzbbo.afterDelInf(busitransvos, dj);

    String tablename = "arap_djzb";
    if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
      tablename = "arap_item";
    Log.getInstance(getClass()).warn("del外接口修改单据前动作后所用时间:");
    try
    {
      res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), tablename);
    } catch (Exception e) {
      throw new BusinessException(e);
    }
    return res;
  }

  public ResMessage unYhqr(DJZBVO dj)
    throws BusinessException
  {
    ResMessage res = new ResMessage();
    try
    {
      DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

      ApplayBillDMO dm = new ApplayBillDMO();
      dm.unYhqr(head);
      DJZBDAO djdmo = new DJZBDAO();
      res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);

      throw new BusinessShowException(e.getMessage());
    }

    return res;
  }

  public ResMessage yhqr(DJZBVO dj)
    throws BusinessException, BusinessException
  {
    ResMessage res = new ResMessage();

    res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102", "UPP2006030102-000616");
    try
    {
      DJZBHeaderVO head = (DJZBHeaderVO)dj.getParentVO();

      boolean bCanCommissioned = dj.getCanBeCommissioned();

      ApplayBillDMO dm = new ApplayBillDMO();
      dm.yhqr(head, bCanCommissioned);

      DJZBDAO djdmo = new DJZBDAO();

      res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
      EffectAction ea = new EffectAction();
      dj = ea.doEffectAction(dj);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);

      throw new BusinessShowException(e.getMessage());
    }

    return res;
  }

  public FipCallFacade getFipcallfacade() {
    if (this.fipcallfacade == null) {
      this.fipcallfacade = new FipCallFacade();
    }
    return this.fipcallfacade;
  }
}