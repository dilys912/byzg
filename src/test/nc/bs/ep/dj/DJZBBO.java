package nc.bs.ep.dj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import nc.bs.arap.billcooperation.BillCooperateBO;
import nc.bs.arap.callouter.FipCallFacade;
import nc.bs.arap.global.ArapClassRunBO;
import nc.bs.arap.global.ArapExtInfRunBO;
import nc.bs.arap.global.CurrencyControlBO;
import nc.bs.arap.global.LoanController;
import nc.bs.arap.global.PubBO;
import nc.bs.arap.informer.DjDelInterface;
import nc.bs.arap.outer.ArapPubAddInterface;
import nc.bs.arap.outer.ArapPubDelInterface;
import nc.bs.arap.outer.ArapPubDelTemporarilyInterface;
import nc.bs.arap.outer.ArapPubEditInterface;
import nc.bs.arap.outer.ArapPubEditTemporarilyInterface;
import nc.bs.arap.outer.IArapPrePayPlugin;
import nc.bs.bd.b47.PaytermDMO;
import nc.bs.dao.DAOException;
import nc.bs.ep.dj_tb.TbDAO;
import nc.bs.ep.itemconfig.ItemconfigBO;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.pub.pf.PfUtilBO;
import nc.impl.arap.proxy.Proxy;
import nc.itf.arap.prv.IArapBillPrivate;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.KeyLock;
import nc.itf.fi.pub.SysInit;
import nc.itf.uap.pfxx.IPFxxEJBService;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.jdbc.framework.exception.DbException;
import nc.vo.arap.global.ArapDjCalculator;
import nc.vo.arap.global.BusiTransVO;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.arap.verifynew.BusinessShowException;
import nc.vo.bd.b47.PaytermHeaderVO;
import nc.vo.bd.b47.PaytermItemVO;
import nc.vo.bd.b47.PaytermVO;
import nc.vo.bd.b47.PaytermchItemVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.ep.dj.DJFBVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.ep.dj.DJZBVOTreator;
import nc.vo.ep.dj.DefdefVO;
import nc.vo.ep.dj.DjCondVO;
import nc.vo.ep.dj.DjfkxybVO;
import nc.vo.ep.dj.IFTSReceiverVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.sourcebill.LightBillVO;

public class DJZBBO
{
  private DJZBVO afterAddInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubAddInterface)busitransvos[i].getInfClass()).afterAddAct(dJZB);
        }
        catch (ClassNotFoundException e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public DJZBVO afterDelInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubDelInterface)busitransvos[i].getInfClass()).afterDelAct(dJZB);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          Log.getInstance(getClass()).info(busitransvos[i].getSystemname() + busitransvos[i].getNote() + "\n" + e);

          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  private DJZBVO afterEditInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubEditInterface)busitransvos[i].getInfClass()).afterEditAct(dJZB.getm_OldVO(), dJZB);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          Log.getInstance(getClass()).info(busitransvos[i].getSystemname() + busitransvos[i].getNote() + "\n" + e);

          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public ResMessage[] auditABills2(DJZBVO[] djs)
    throws BusinessException
  {
    if ((djs == null) || (djs.length < 1))
      return null;
    ResMessage[] res = new ResMessage[djs.length];

    ArapExtInfRunBO extbo = new ArapExtInfRunBO();

    BusiTransVO[] busitransvos = extbo.initBusiTrans("shenhe", ((DJZBHeaderVO)djs[0].getParentVO()).getPzglh());

    DJZBHeaderVO head = null;
    DJZBHeaderVO[] heads = null;
    DJZBVO[] dj_2 = new DJZBVO[djs.length]; DJZBVO[] dj_temp = null;
    Vector v = new Vector();
    int i = 0; for (int j = 0; i < djs.length; i++) {
      if (djs[i].getChildrenVO() == null) {
        v.addElement(djs[i].getParentVO());
      }
      else {
        dj_2[j] = djs[i];
        j++;
      }
    }
    try
    {
      if (v.size() > 0) {
        heads = new DJZBHeaderVO[v.size()];
        v.copyInto(heads);
        DJZBDAO dmo = new DJZBDAO();
        dj_temp = dmo.getDjVObyHeaderVos(heads);
        System.arraycopy(dj_temp, 0, dj_2, dj_2.length - v.size(), v.size());
      }

      ArapDjBsCheckerBO djchecker = new ArapDjBsCheckerBO();
      try {
        djchecker.supplementAllInfos(dj_2);
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        Log.getInstance(getClass()).info("补充单据信息错误！");
        throw new BusinessException(e.getMessage(), e);
      }
      try {
        res = djchecker.checkApproveBills(dj_2);
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        throw new BusinessException(e.getMessage(), e);
      }
      for (int i1 = 0; i1 < dj_2.length; i1++) {
        head = (DJZBHeaderVO)dj_2[i1].getParentVO();
        try
        {
          lockDJ(head.getVouchid());
          if (res[i1] == null)
          {
            res[i1] = Proxy.getIArapBillPrivate().auditABills_RequiresNew(dj_2[i1], busitransvos);
          }

          if (res[i1].isSuccess)
            res[i1].strMessage = (NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000511") + head.getDjbh() + res[i1].strMessage);
          else
            res[i1].strMessage = (NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000512") + head.getDjbh() + res[i1].strMessage);
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          if (res[i1] == null)
            res[i1] = new ResMessage();
          res[i1].isSuccess = false;
          res[i1].strMessage = e.getMessage();
          res[i1].strMessage = (NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000513") + head.getDjbh() + res[i1].strMessage);
        }

        res[i1].listIndex = head.listIndex;
        res[i1].vouchid = head.getVouchid();
      }
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage(), e);
    }

    Log.getInstance(getClass()).info("批审核返回...");
    return res;
  }

  private DJZBVO afterTempDelInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubDelTemporarilyInterface)busitransvos[i].getInfClass()).afterDelTemporarilyAct(dJZB);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessException(strerr);
        }
      }
    }

    return dJZB;
  }

  private DJZBVO beforeTempDelInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubDelTemporarilyInterface)busitransvos[i].getInfClass()).beforeDelTemporarilyAct(dJZB);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessException(strerr);
        }
      }
    }

    return dJZB;
  }

  private DJZBVO beforeAddInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubAddInterface)busitransvos[i].getInfClass()).beforeAddAct(dJZB);
        }
        catch (ClassNotFoundException e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
        }
        catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          Log.getInstance(getClass()).info(busitransvos[i].getSystemname() + busitransvos[i].getNote() + "\n" + e);

          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public DJZBVO beforeDelInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubDelInterface)busitransvos[i].getInfClass()).beforeDelAct(dJZB);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          Log.getInstance(getClass()).info(busitransvos[i].getSystemname() + busitransvos[i].getNote() + "\n" + e);

          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessShowException(strerr);
        }
      }
    }
    return dJZB;
  }

  private DJZBVO beforeEditInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubEditInterface)busitransvos[i].getInfClass()).beforeEditAct(dJZB.getm_OldVO(), dJZB);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          Log.getInstance(getClass()).info(busitransvos[i].getSystemname() + busitransvos[i].getNote() + "\n" + e);

          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessShowException(strerr);
        }
      }
    }

    return dJZB;
  }

  public String cancel_CloseDj_SS(DJZBVO vo)
    throws BusinessException
  {
    String ts = null;
    try {
      vo.m_isClose = true;
      DJZBHeaderVO head = (DJZBHeaderVO)(DJZBHeaderVO)vo.getParentVO();
      lockDJ(head.getVouchid());

      DJZBDAO dmo = new DJZBDAO();

      DJZBItemVO[] items = vo.getChildrenVO() == null ? null : (DJZBItemVO[])(DJZBItemVO[])vo.getChildrenVO();

      if (items == null)
      {
        items = dmo.findItemsForHeader_SS(head.getPrimaryKey());

        vo.setChildrenVO(items);
      }
      ApplayBillBO aBo = new ApplayBillBO();
      aBo.isDistributes(vo);
      head.setTs(new UFDateTime(dmo.getTsByPrimaryKey(head.getVouchid(), "arap_item")));

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();

      BusiTransVO[] busitransvos = extbo.initBusiTrans("add", head.getPzglh());
      beforeAddInf(busitransvos, vo);
      Log.getInstance(getClass()).info("外接口新增单据前动作前所用时间:" + (System.currentTimeMillis() - t1));
      long t2 = System.currentTimeMillis();

      afterAddInf(busitransvos, vo);
      Log.getInstance(getClass()).info("外接口新增单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      dmo.cancel_Close_SS(vo);
      ts = ((DJZBHeaderVO)vo.getParentVO()).getts().toString();

      for (int i = 0; i < items.length; i++) {
        items[i].setClosedate(null);
        items[i].setCloser(null);
        dmo.cancel_Close_SSItem(items[i]);
      }
      vo.m_isClose = false;
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      vo.m_isClose = false;
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
    return ts;
  }

  public String cancel_CloseDj_SSItem(DJZBVO vo, boolean bBillOpenNeeded)
    throws BusinessException
  {
    String ts = null;
    try {
      vo.m_isClose = true;
      DJZBHeaderVO head = (DJZBHeaderVO)(DJZBHeaderVO)vo.getParentVO();
      DJZBDAO dmo = new DJZBDAO();
      lockDJ(head.getVouchid());

      DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])vo.getChildrenVO();

      long t1 = System.currentTimeMillis();

      ArapExtInfRunBO extbo = new ArapExtInfRunBO();
      BusiTransVO[] busitransvos = extbo.initBusiTrans("add", head.getPzglh());
      beforeAddInf(busitransvos, vo);
      Log.getInstance(getClass()).info("外接口新增单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

      long t2 = System.currentTimeMillis();

      afterAddInf(busitransvos, vo);
      Log.getInstance(getClass()).debug("外接口新增单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      if (bBillOpenNeeded) {
        dmo.cancel_Close_SS(vo);
        ts = ((DJZBHeaderVO)vo.getParentVO()).getts().toString();
      } else {
        ts = dmo.distributeDjzb_Item(head.getVouchid(), head.getts().toString(), head.getDjdl());
      }
      for (int i = 0; i < items.length; i++) {
        items[i].setClosedate(null);
        items[i].setCloser(null);
        dmo.cancel_Close_SSItem(items[i]);
      }
      vo.m_isClose = false;
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      vo.m_isClose = false;
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::cancel_CloseDj_SSItem(vo) Exception!", e);
    }
    return ts;
  }

  private boolean checkYuTiParallell(DJZBVO djzbVO)
    throws Exception
  {
    DJZBItemVO[] itemVOs = (DJZBItemVO[])(DJZBItemVO[])djzbVO.getChildrenVO();

    String strYuTiHeadPK = null;

    String strYuTiHeadTS = null;
    for (int i = 0; i < itemVOs.length; i++) {
      DJZBItemVO itemVO = itemVOs[i];

      if ((itemVO.getDdlx() != null) && (itemVO.getDdlx().trim().length() > 0)) {
        strYuTiHeadPK = itemVO.getDdlx();
        strYuTiHeadTS = itemVO.getTs().toString();

        break;
      }

    }

    if (strYuTiHeadPK == null) {
      return true;
    }
    TbDAO yuTiDMO = new TbDAO();

    int iLockCount = yuTiDMO.lockYuTiByPKAndTS(strYuTiHeadPK, strYuTiHeadTS);

    return iLockCount == 1;
  }

  public String closeDj_SS(DJZBVO vo)
    throws BusinessException
  {
    DJZBVO djzbvoWithOpenItem = null;
    try {
      vo.m_isClose = true;
      DJZBDAO dmo = new DJZBDAO();
      DJZBHeaderVO head = (DJZBHeaderVO)(DJZBHeaderVO)vo.getParentVO();
      lockDJ(head.getVouchid());

      DJZBItemVO[] items = vo.getChildrenVO() == null ? null : (DJZBItemVO[])(DJZBItemVO[])vo.getChildrenVO();

      if (items == null) {
        items = dmo.findItemsForHeader_SS(head.getPrimaryKey());
        vo.setChildrenVO(items);
      }

      djzbvoWithOpenItem = filterSSDJZBVOWithOpenItem(vo);
      djzbvoWithOpenItem.m_isClose = true;

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();
      BusiTransVO[] busitransvos = extbo.initBusiTrans("del", head.getPzglh());

      beforeDelInf(busitransvos, djzbvoWithOpenItem);

      Log.getInstance(getClass()).debug("外接口删除单据前动作前所用时间:" + (System.currentTimeMillis() - t1));
      long t2 = System.currentTimeMillis();
      ApplayBillBO aBo = new ApplayBillBO();
      aBo.isDistributes(djzbvoWithOpenItem);
      head.setTs(new UFDateTime(dmo.getTsByPrimaryKey(head.getVouchid(), "arap_item")));

      afterDelInf(busitransvos, djzbvoWithOpenItem);
      Log.getInstance(getClass()).debug("外接口删除单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      dmo.close_SS(djzbvoWithOpenItem);

      DJZBItemVO[] openItems = (DJZBItemVO[])(DJZBItemVO[])djzbvoWithOpenItem.getChildrenVO();
      dmo.close_SSItem(openItems);
      vo.m_isClose = false;
      djzbvoWithOpenItem.m_isClose = false;
      return head.getts().toString();
    } catch (Exception e) {
      vo.m_isClose = false;
      if (djzbvoWithOpenItem != null)
        djzbvoWithOpenItem.m_isClose = false;
      Log.getInstance(getClass()).error(e.getMessage(), e);
      if ((e instanceof BusinessShowException))
        throw ((BusinessShowException)e); 
      throw new BusinessShowException(e.getMessage());
    }
    
  }
  public DJZBVO[] queryDJZBVOBySSPk(String sspk) throws BusinessException { DJZBDAO dmo = new DJZBDAO();
    String[] pks;
    try {
      pks = dmo.getDjPksBySSPk(sspk);
    }
    catch (DbException e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
    if ((null == pks) || (pks.length == 0))
      return null;
    return findByPrimaryKeys(pks);
  }

  public String closeDj_SSItem(DJZBVO vo, boolean bCloseBill)
    throws BusinessException
  {
    String ts = null;
    try {
      vo.m_isClose = true;
      DJZBDAO dmo = new DJZBDAO();
      DJZBHeaderVO head = (DJZBHeaderVO)(DJZBHeaderVO)vo.getParentVO();

      DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])vo.getChildrenVO();
      lockDJ(head.getVouchid());

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();
      BusiTransVO[] busitransvos = extbo.initBusiTrans("del", head.getPzglh());

      beforeDelInf(busitransvos, vo);

      Log.getInstance(getClass()).debug("外接口删除单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

      long t2 = System.currentTimeMillis();

      afterDelInf(busitransvos, vo);
      Log.getInstance(getClass()).debug("外接口删除单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      dmo.close_SSItem(items);

      if (bCloseBill == true) {
        dmo.close_SS(vo);
        ts = ((DJZBHeaderVO)vo.getParentVO()).getts().toString();
      } else {
        ts = dmo.distributeDjzb_Item(head.getVouchid(), head.getts().toString(), head.getDjdl());
      }
      vo.m_isClose = false;
    } catch (Exception e) {
      vo.m_isClose = false;
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
    return ts;
  }

  public DJZBVO cooperate_affirm(DJZBVO dJZB)
    throws BusinessException
  {
    try
    {
      DJZBHeaderVO head = (DJZBHeaderVO)dJZB.getParentVO();
      DJZBItemVO[] items = null;
      items = (DJZBItemVO[])(DJZBItemVO[])dJZB.getChildrenVO();
      if ((head.getHzbz() != null) && (head.getHzbz().trim().length() > 0)) {
        head.setLybz(new Integer(0));
      }
      List ddhhs = new ArrayList();

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();

      BusiTransVO[] busitransvos = extbo.initBusiTrans("add", head.getPzglh());

      beforeAddInf(busitransvos, dJZB);
      Log.getInstance(getClass()).debug("外接口新增单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

      DJZBDAO dmo = new DJZBDAO();

      String strDJBH = getDjbh(dJZB);
      head.setDjbh(strDJBH);

      for (int i = 0; i < items.length; i++) {
        items[i].setDjbh(strDJBH);
      }

      dJZB = dmo.update(doPayterm(dJZB));

      items = (DJZBItemVO[])(DJZBItemVO[])dJZB.getChildrenVO();

      List fbpks = new ArrayList();

      for (int i = 0; i < items.length; i++) {
        fbpks.add(items[i].getDdhh());
        if ((DJZBVOConsts.FromXT.equals(head.getLybz())) && (items[i].getDdhh() != null))
          ddhhs.add(items[i].getDdhh());
        if (((!head.getDjdl().equals("ys")) && (!head.getDjdl().equals("sk"))) || 
          (items[i] == null) || (items[i].getWldx().intValue() != 0) || (
          (items[i].getOrdercusmandoc() != null) && (items[i].getOrdercusmandoc().trim().length() >= 1)))
          continue;
        items[i].setOrdercusmandoc(items[i].getKsbm_cl());
      }

      dmo.updateXtFlag(fbpks, DJZBVOConsts.XTConfirmed);
      if ((head.getQcbz() == null) || (!head.getQcbz().booleanValue()))
      {
        if ((!"ss".equals(head.getDjdl())) && ("0".equals(head.getSsflag()))) {
          ItemconfigBO itemconfigbo = new ItemconfigBO();

          lock_item_bill(items, head.getLrr(), 1);

          itemconfigbo.ShenHeSave((DJZBVO)dJZB.clone());
        }

        if ((head.getXtflag() != null) && (head.getXtflag().equals("保存")))
        {
          BillCooperateBO billcooperatebo = new BillCooperateBO();

          billcooperatebo.doCooperate(dJZB);
        }

      }

      long t2 = System.currentTimeMillis();

      afterAddInf(busitransvos, dJZB);
      Log.getInstance(getClass()).debug("外接口新增单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      String sts = dmo.getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
      if (sts.trim().length() >= 3)
      {
        head.setTs(new UFDateTime(sts));
      }

      return dJZB;
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
  }

  public void deleteDjByPk(String[] ids)
    throws BusinessException
  {
    DJZBDAO dmo = new DJZBDAO();
    if (null == ids) return;
    for (String id : ids) {
      lockDJ(id);
    }

    dmo.deleteDjByPks(ids);
    Proxy.getIPFxxEJBService().deleteIDvsPKsByDocPKs(ids);
  }
  public DJZBVO updateDj(DJZBVO vo) throws BusinessException {
    return editDj(vo);
  }

  public DJZBVO deleteDj(DJZBVO vo)
    throws BusinessException
  {
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      DJZBHeaderVO head = (DJZBHeaderVO)(DJZBHeaderVO)vo.getParentVO();
      lockDJ(head.getVouchid());
      List fbpks = new ArrayList();
      DJZBItemVO[] items = null;
      if (vo.getChildrenVO() != null)
      {
        items = (DJZBItemVO[])(DJZBItemVO[])vo.getChildrenVO();
      }
      else
      {
        try
        {
          if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
            items = dmo.findItemsForHeader_SS(head.getPrimaryKey());
          else
            items = dmo.findItemsForHeader(head.getPrimaryKey());
        } catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          throw e;
        }
        vo.setChildrenVO(items);
      }

      for (DJZBItemVO item : items)
      {
        if ((null != item.getDdhh()) && (item.getDdhh().length() > 0)) {
          fbpks.add(item.getDdhh());
        }
      }
      ApplayBillBO aBo = new ApplayBillBO();
      aBo.isDistributes(vo);
      vo.setParam_Ext_Save();

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();

      BusiTransVO[] busitransvos = null;
      if (((DJZBHeaderVO)vo.getParentVO()).getDjzt().intValue() != 0) {
        busitransvos = extbo.initBusiTrans("del", head.getPzglh());

        beforeDelInf(busitransvos, vo);
      }
      else
      {
        new DjDelInterface().beforeDelAct(vo);
      }
      Log.getInstance(getClass()).debug("外接口删除单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

      if (head.getDjdl().equals("ss"))
      {
        dmo.delete_SS(vo);
      }
      else {
        if ((head.getQcbz() != null) && (head.getQcbz().booleanValue()) && 
          (head.getSxbz().intValue() == 10))
        {
          aBo.sendMessage_del(vo, UFBoolean.FALSE);
        }

        if (((DJZBHeaderVO)vo.getParentVO()).getDjzt().intValue() != 0)
        {
          if ("0".equals(head.getSsflag())) {
            ItemconfigBO itemconfigbo = new ItemconfigBO();
            lock_item_bill(items, head.getLrr(), 1);

            itemconfigbo.unShenHe((DJZBVO)vo.clone(), false);
          }

          deleteOutBill(head.getVouchid());
          dmo.updateXtFlag(fbpks, null);
          for (int i = 0; i < items.length; i++) {
            if ((items[i].getTbbh() != null) && (items[i].getTbbh().trim().length() > 0)) {
              throw new Exception(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000358"));
            }
          }
        }
        dmo.delete(vo);
        if (((DJZBHeaderVO)vo.getParentVO()).getDjzt().intValue() == 0) {
          return vo;
        }

      }

      long t2 = System.currentTimeMillis();
      if (((DJZBHeaderVO)vo.getParentVO()).getDjzt().intValue() != 0) {
        afterDelInf(busitransvos, vo);
      }
      Log.getInstance(getClass()).debug("外接口删除单据动作后所用时间:" + (System.currentTimeMillis() - t2));
      Proxy.getIPFxxEJBService().deleteIDvsPKByDocPK(head.getVouchid());
      returnBillCode(vo, false);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);

      throw new BusinessShowException(e.getMessage());
    }
    return vo;
  }

  public DJZBHeaderVO[] queryHead(String key) throws BusinessException
  {
    try {
      DJZBDAO dmo = new DJZBDAO();
      return dmo.queryHead(key);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e);
    }
  }

  public DJZBVO deleteTempDj(DJZBVO vo)
    throws BusinessException
  {
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      DJZBHeaderVO head = (DJZBHeaderVO)(DJZBHeaderVO)vo.getParentVO();
      lockDJ(head.getVouchid());
      DJZBItemVO[] items = null;
      if (vo.getChildrenVO() != null) {
        items = (DJZBItemVO[])(DJZBItemVO[])vo.getChildrenVO();
      }
      else {
        try {
          items = dmo.findItemsForHeader(head.getPrimaryKey());
        } catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          throw new BusinessException(e.getMessage(), e);
        }
        vo.setChildrenVO(items);
      }

      ApplayBillBO aBo = new ApplayBillBO();
      aBo.isDistributes(vo);

      ArapExtInfRunBO extbo = new ArapExtInfRunBO();

      BusiTransVO[] busitransvos = null;
      busitransvos = extbo.initBusiTrans("deltemp", head.getPzglh());
      beforeTempDelInf(busitransvos, vo);
      dmo.delete(vo);

      afterTempDelInf(busitransvos, vo);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException("DJZBBO::deleteTempDj(DJZBPK) Exception!", e);
    }
    return vo;
  }

  public void deleteOutBill(String[] ids, String cOperator)
    throws BusinessException
  {
    for (int i = 0; i < ids.length; i++)
      deleteOutBill(ids[i]);
  }

  public void deleteOutBillbyWhere(String whereString)
    throws BusinessException
  {
    String errmsg = "";
    try {
      DJZBVO dj = new DJZBVO();
      DJZBDAO djzb = new DJZBDAO();

      DJZBHeaderVO[] headers = djzb.queryHead(whereString);
      if ((headers != null) && (headers.length != 0))
      {
        try
        {
          for (int i = 0; i < headers.length; i++) {
            headers[i].m_isOtherOpration = true;
            if (headers[i].getDjzt().intValue() > 1)
            {
              errmsg = NCLangResOnserver.getInstance().getStrByID("200602", "UPT200602-v35-000017") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000516") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000517");
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("200602", "UPT200602-v35-000017") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000516") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000517"));
            }if (((headers[i].getDjzt().intValue() == 1) || (-99 == headers[i].getDjzt().intValue())) && (headers[i].getLybz().intValue() == 9)) {
              errmsg = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000519") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000516") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000517");
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000519") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000516") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000517"));
            }if ((headers[i].getSpzt() != null) && (headers[i].getSpzt().equalsIgnoreCase("0"))) {
              errmsg = NCLangResOnserver.getInstance().getStrByID("200602", "UPT200602-v35-000018") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000516") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000517");
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("200602", "UPT200602-v35-000018") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000516") + NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000517"));
            }

            dj = new DJZBVO();
            dj.setParentVO(headers[i]);
            try
            {
              dj.setParam_Ext_Save();
            }
            catch (Exception e) {
              Log.getInstance(getClass()).error(e.getMessage(), e);
              throw new BusinessShowException("nc.bs.ep.dj.DJZBBO.saveDj(DJZBVO) setParam_Ext_Save:" + e);
            }

            deleteDj(dj);
          }
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          throw e;
        }
      }
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage(), new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000520") + errmsg));
    }
  }

  public void deleteXtBill(String[] ids)
    throws BusinessException
  {
    if ((null == ids) || (ids.length == 0)) return;
    StringBuffer where = new StringBuffer();
    for (String fbpk : ids) {
      where.append("'").append(fbpk).append("',");
    }

    String whereString = "   where  exists (select vouchid from arap_djfb  where  zb.vouchid = arap_djfb.vouchid and arap_djfb.ddhh in( " + where.substring(0, where.length() - 1) + ") ) and  zb.dr=0 ";

    deleteOutBillbyWhere(whereString);
  }

  public void deleteOutBillandfb(String id, String fboid)
    throws BusinessException
  {
    Log.getInstance(getClass()).debug("ddlx is :" + id);
    String whereString = "  ,arap_djfb where  zb.vouchid = arap_djfb.vouchid and arap_djfb.ddlx = '" + id.trim() + "' and arap_djfb.ddhh = '" + fboid.trim() + "' and zb.dr=0";

    deleteOutBillbyWhere(whereString);
  }

  public String deleteZyx(DJFBVO djfbvo)
    throws BusinessException
  {
    try
    {
      DJFBDAO dmo = new DJFBDAO();

      dmo.delete(djfbvo);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::delete(DJZBPK) Exception!", e);
    }
    return "";
  }

  private DJZBVO doPayterm(DJZBVO vo)
    throws BusinessException
  {
    if ((vo == null) || (vo.getChildrenVO() == null))
      return vo;
    try
    {
      String sFkxyh = "";

      DjfkxybVO[] djfkxy = null;

      DJZBHeaderVO head = (DJZBHeaderVO)vo.getParentVO();

      String FCurr = Currency.getFracCurrPK(head.getDwbm());
      String LCurr = Currency.getLocalCurrPK(head.getDwbm());
      Integer shlDig = SysInit.getParaInt(head.getDwbm(), "BD501");
      if ((FCurr != null) && (FCurr.indexOf("null") >= 0))
        FCurr = null;
      if ((LCurr != null) && (LCurr.indexOf("null") >= 0)) {
        LCurr = null;
      }
      DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])vo.getChildrenVO();

      String currOid = "";
      CurrencyControlBO bzCon = new CurrencyControlBO();

      int iCount = items.length;
      for (int i = 0; i < iCount; i++) {
        if (!isSFKUpdated(items[i]))
          continue;
        sFkxyh = items[i].getSfkxyh();

        currOid = items[i].getBzbm();
        bzCon.setCurrencyType(currOid);

        if ((sFkxyh == null) || (sFkxyh.trim().length() == 0))
        {
          djfkxy = new DjfkxybVO[1];
          djfkxy[0] = new DjfkxybVO();

          djfkxy[0].setXydqr(items[i].getQxrq() == null ? head.getDjrq() : items[i].getQxrq());

          djfkxy[0].setBbye(bzCon.getFormat(LCurr, items[i].getBbye()));
          if (items[i].getFbye() == null)
            djfkxy[0].setFbye(new UFDouble(0));
          else
            djfkxy[0].setFbye(bzCon.getFormat(FCurr, items[i].getFbye()));
          djfkxy[0].setYbye(bzCon.getFormat(currOid, items[i].getYbye()));

          djfkxy[0].setDfbbje(bzCon.getFormat(LCurr, items[i].getDfbbje()));
          djfkxy[0].setDffbje(bzCon.getFormat(FCurr, items[i].getDffbje()));
          djfkxy[0].setDfybje(bzCon.getFormat(currOid, items[i].getDfybje()));

          djfkxy[0].setDfshl(items[i].getDfshl() == null ? ArapConstant.DOUBLE_ZERO : items[i].getDfshl().setScale(shlDig.intValue(), 4));

          if (items[i].getJfbbje() == null)
            djfkxy[0].setJfbbje(ArapConstant.DOUBLE_ZERO);
          else
            djfkxy[0].setJfbbje(bzCon.getFormat(LCurr, items[i].getJfbbje()));
          djfkxy[0].setJffbje(bzCon.getFormat(FCurr, items[i].getJffbje()));
          djfkxy[0].setJfybje(bzCon.getFormat(currOid, items[i].getJfybje()));

          djfkxy[0].setJfshl(items[i].getJfshl() == null ? ArapConstant.DOUBLE_ZERO : items[i].getJfshl().setScale(shlDig.intValue(), 4));
          djfkxy[0].setShlye(items[i].getShlye() == null ? ArapConstant.DOUBLE_ZERO : items[i].getShlye().setScale(shlDig.intValue(), 4));
        }
        else
        {
          PaytermDMO paydmo = new PaytermDMO();
          PaytermVO payVO = paydmo.findByPrimaryKey(sFkxyh.trim());

          PaytermHeaderVO payHead = (PaytermHeaderVO)payVO.getParentVO();

          PaytermItemVO[] payItems = (PaytermItemVO[])(PaytermItemVO[])payVO.getChildrenVO();

          int iFixaddDate = payHead.getFixadddate() == null ? 0 : payHead.getFixadddate().intValue();
          int iNum = payItems.length;

          djfkxy = new DjfkxybVO[iNum];

          UFDouble Itemyb = items[i].getYbye();
          UFDouble Itemfb = items[i].getFbye();
          UFDouble Itembb = items[i].getBbye();
          UFDouble Itemshl = items[i].getShlye();

          UFDouble ItemDfyb = items[i].getDfybje();
          UFDouble ItemDffb = items[i].getDffbje();
          UFDouble ItemDfbb = items[i].getDfbbje();
          UFDouble ItemDfshl = items[i].getDfshl();

          UFDouble ItemJfyb = items[i].getJfybje();
          UFDouble ItemJffb = items[i].getJffbje();
          UFDouble ItemJfbb = items[i].getJfbbje();
          UFDouble ItemJfshl = items[i].getJfshl();

          for (int k = 0; k < iNum; k++) {
            double rate = payItems[k].getAccrate().doubleValue();

            if (rate == 0.0D)
              continue;
            PaytermchItemVO[] vZk = payItems[k].getPtrateVOs();

            djfkxy[k] = new DjfkxybVO();

            if (payHead.getFixedflag().booleanValue()) {
              if (k == iNum - 1)
              {
                djfkxy[k].setBbye(bzCon.getFormat(LCurr, Itembb));
                djfkxy[k].setFbye(bzCon.getFormat(FCurr, Itemfb));
                djfkxy[k].setYbye(bzCon.getFormat(currOid, Itemyb));
                djfkxy[k].setShlye(Itemshl == null ? ArapConstant.DOUBLE_ZERO : Itemshl.setScale(shlDig.intValue(), 4));

                djfkxy[k].setDfbbje(bzCon.getFormat(LCurr, ItemDfbb));
                djfkxy[k].setDffbje(bzCon.getFormat(FCurr, ItemDffb));
                djfkxy[k].setDfybje(bzCon.getFormat(currOid, ItemDfyb));
                djfkxy[k].setDfshl(ItemDfshl == null ? ArapConstant.DOUBLE_ZERO : ItemDfshl.setScale(shlDig.intValue(), 4));

                djfkxy[k].setJfbbje(bzCon.getFormat(LCurr, ItemJfbb));
                djfkxy[k].setJffbje(bzCon.getFormat(FCurr, ItemJffb));
                djfkxy[k].setJfybje(bzCon.getFormat(currOid, ItemJfyb));
                djfkxy[k].setJfshl(ItemJfshl == null ? ArapConstant.DOUBLE_ZERO : ItemJfshl.setScale(shlDig.intValue(), 4));
              }
              else {
                djfkxy[k].setBbye(bzCon.getFormat(LCurr, getDRate(items[i].getBbye(), rate)));
                djfkxy[k].setFbye(bzCon.getFormat(FCurr, getDRate(items[i].getFbye(), rate)));
                djfkxy[k].setYbye(bzCon.getFormat(currOid, getDRate(items[i].getYbye(), rate)));
                djfkxy[k].setShlye(items[i].getShlye() == null ? ArapConstant.DOUBLE_ZERO : getDRate(items[i].getShlye(), rate).setScale(shlDig.intValue(), 4));

                djfkxy[k].setDfbbje(bzCon.getFormat(LCurr, getDRate(items[i].getDfbbje(), rate)));

                djfkxy[k].setDffbje(bzCon.getFormat(FCurr, getDRate(items[i].getDffbje(), rate)));

                djfkxy[k].setDfybje(bzCon.getFormat(currOid, getDRate(items[i].getDfybje(), rate)));

                djfkxy[k].setDfshl(items[i].getDfshl() == null ? ArapConstant.DOUBLE_ZERO : getDRate(items[i].getDfshl(), rate).setScale(shlDig.intValue(), 4));

                djfkxy[k].setJfbbje(bzCon.getFormat(LCurr, getDRate(items[i].getJfbbje(), rate)));

                djfkxy[k].setJffbje(bzCon.getFormat(FCurr, getDRate(items[i].getJffbje(), rate)));

                djfkxy[k].setJfybje(bzCon.getFormat(currOid, getDRate(items[i].getJfybje(), rate)));

                djfkxy[k].setJfshl(items[i].getJfshl() == null ? ArapConstant.DOUBLE_ZERO : getDRate(items[i].getJfshl(), rate).setScale(shlDig.intValue(), 4));
              }

              int date = 0;
              if ((payHead.getEffectoption() != null) && (payHead.getEffectoption().intValue() == 1))
                date = 1;
              djfkxy[k].setXydqr(getdate(items[i].getQxrq() == null ? head.getDjrq() : items[i].getQxrq(), payHead.getCheckdate(), k + date).getDateAfter(iFixaddDate));

              djfkxy[k].setFkxyb_oid(payItems[k].getPrimaryKey());
            }
            else {
              if (k == iNum - 1)
              {
                djfkxy[k].setBbye(bzCon.getFormat(LCurr, Itembb));
                djfkxy[k].setFbye(bzCon.getFormat(FCurr, Itemfb));
                djfkxy[k].setYbye(bzCon.getFormat(currOid, Itemyb));

                djfkxy[k].setDfbbje(bzCon.getFormat(LCurr, ItemDfbb));
                djfkxy[k].setDffbje(bzCon.getFormat(FCurr, ItemDffb));
                djfkxy[k].setDfybje(bzCon.getFormat(currOid, ItemDfyb));

                djfkxy[k].setJfbbje(bzCon.getFormat(LCurr, ItemJfbb));
                djfkxy[k].setJffbje(bzCon.getFormat(FCurr, ItemJffb));
                djfkxy[k].setJfybje(bzCon.getFormat(currOid, ItemJfyb));
                djfkxy[k].setShlye(Itemshl == null ? ArapConstant.DOUBLE_ZERO : Itemshl.setScale(shlDig.intValue(), 4));
                djfkxy[k].setDfshl(ItemDfshl == null ? ArapConstant.DOUBLE_ZERO : ItemDfshl.setScale(shlDig.intValue(), 4));
                djfkxy[k].setJfshl(ItemJfshl == null ? ArapConstant.DOUBLE_ZERO : ItemJfshl.setScale(shlDig.intValue(), 4));
              }
              else {
                djfkxy[k].setBbye(bzCon.getFormat(LCurr, getDRate(items[i].getBbye(), rate)));
                djfkxy[k].setFbye(bzCon.getFormat(FCurr, getDRate(items[i].getFbye(), rate)));
                djfkxy[k].setYbye(bzCon.getFormat(currOid, getDRate(items[i].getYbye(), rate)));

                djfkxy[k].setDfbbje(bzCon.getFormat(LCurr, getDRate(items[i].getDfbbje(), rate)));

                djfkxy[k].setDffbje(bzCon.getFormat(FCurr, getDRate(items[i].getDffbje(), rate)));

                djfkxy[k].setDfybje(bzCon.getFormat(currOid, getDRate(items[i].getDfybje(), rate)));

                djfkxy[k].setJfbbje(bzCon.getFormat(LCurr, getDRate(items[i].getJfbbje(), rate)));

                djfkxy[k].setJffbje(bzCon.getFormat(FCurr, getDRate(items[i].getJffbje(), rate)));

                djfkxy[k].setJfybje(bzCon.getFormat(currOid, getDRate(items[i].getJfybje(), rate)));

                djfkxy[k].setShlye(items[i].getShlye() == null ? ArapConstant.DOUBLE_ZERO : getDRate(items[i].getShlye(), rate).setScale(shlDig.intValue(), 4));
                djfkxy[k].setDfshl(items[i].getDfshl() == null ? ArapConstant.DOUBLE_ZERO : getDRate(items[i].getDfshl(), rate).setScale(shlDig.intValue(), 4));
                djfkxy[k].setJfshl(items[i].getJfshl() == null ? ArapConstant.DOUBLE_ZERO : getDRate(items[i].getJfshl(), rate).setScale(shlDig.intValue(), 4));
              }

              int iDateInter = payItems[k].getAcclimited().intValue();
              djfkxy[k].setXydqr((items[i].getQxrq() == null ? head.getDjrq() : items[i].getQxrq()).getDateAfter(iDateInter));

              djfkxy[k].setSfkxyfb_oid(payItems[k].getPrimaryKey());

              PaytermchItemVO zk = null;
              if ((vZk != null) && (vZk.length != 0)) {
                zk = vZk[(vZk.length - 1)];
                djfkxy[k].setLastzkl(zk.getRtntate());
                djfkxy[k].setLastzkrq((items[i].getQxrq() == null ? head.getDjrq() : items[i].getQxrq()).getDateAfter(zk.getRdata().intValue()));
              }

            }

            Itemyb = Itemyb.sub(djfkxy[k].getYbye());
            Itemfb = Itemfb.sub(djfkxy[k].getFbye());
            Itembb = Itembb.sub(djfkxy[k].getBbye());
            Itemshl = Itemshl.sub(djfkxy[k].getShlye());

            ItemDfyb = ItemDfyb.sub(djfkxy[k].getDfybje());
            ItemDffb = ItemDffb.sub(djfkxy[k].getDffbje());
            ItemDfbb = ItemDfbb.sub(djfkxy[k].getDfbbje());
            ItemDfshl = ItemDfshl.sub(djfkxy[k].getDfshl());

            ItemJfyb = ItemJfyb.sub(djfkxy[k].getJfybje());
            ItemJffb = ItemJffb.sub(djfkxy[k].getJffbje());
            ItemJfbb = ItemJfbb.sub(djfkxy[k].getJfbbje());
            ItemJfshl = ItemJfshl.sub(djfkxy[k].getJfshl());
          }

        }

        items[i].fkxyvos = djfkxy;
      }
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000521"), e);
    }
    return vo;
  }

  private boolean isSFKUpdated(DJZBItemVO item) {
    List lst = new ArrayList();
    String[] attrs = item.getM_strChangedAtts();
    if (null == attrs) return true;
    int i = 0; for (int size = attrs.length; i < size; i++)
      lst.add(attrs[i]);
    if (lst.contains("sfkxyh")) {
      return true;
    }

    return (null != item.getIsSFKXYChanged()) && (item.getIsSFKXYChanged().booleanValue()) && ((lst.contains("effectdate")) || (lst.contains("bbhl")) || (lst.contains("fbhl")) || (lst.contains("jfybje")) || (lst.contains("dfybje")) || (lst.contains("jfshl")) || (lst.contains("dfshl")));
  }

  public void editCost(DJZBVO djzbvo)
    throws BusinessException
  {
    try
    {
      PubBO pub = new PubBO();
      if (!pub.costIsUsed()) {
        return;
      }

      DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();
      lockDJ(head.getVouchid());
      DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])djzbvo.getChildrenVO();
      String[] header_pk = null;
      String[] itemoids = null;
      Double[] bbmnys = null;
      Vector itemoids_v = new Vector();
      Vector bbmnys_v = new Vector();
      for (int i = 0; i < items.length; i++)
      {
        if (items[i].getStatus() != 1)
          continue;
        itemoids_v.addElement(items[i].getFb_oid());
        UFDouble jfbbje = items[i].getJfbbje() == null ? new UFDouble(0.0D) : items[i].getJfbbje();

        UFDouble dfbbje = items[i].getDfbbje() == null ? new UFDouble(0.0D) : items[i].getDfbbje();

        bbmnys_v.addElement(jfbbje.add(dfbbje));
      }
      if (itemoids_v.size() < 1)
        return;
      itemoids = new String[itemoids_v.size()];
      itemoids_v.copyInto(itemoids);
      bbmnys = new Double[bbmnys_v.size()];
      bbmnys_v.copyInto(bbmnys);
      header_pk = new String[itemoids_v.size()];
      for (int i = 0; i < itemoids_v.size(); i++) {
        header_pk[i] = head.getVouchid();
      }
      if (!head.getDjdl().equals("ss"))
      {
        ArapClassRunBO executeBo = new ArapClassRunBO();

        Class[] paramtype = { header_pk.getClass(), itemoids.getClass(), String.class, Object.class, Object.class, bbmnys.getClass() };
        Object[] param = { header_pk, itemoids, "01", null, null, bbmnys };
        executeBo.runMethod("nc.bs.bank.costContent.CostcontentDMO", "calmny", paramtype, param);
      }

    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::editCost(DJZBVO) Exception!", e);
    }
  }

  public DJZBVO editDj(DJZBVO slimDJZBVO)
    throws BusinessException
  {
    DJZBVO djzbvo = slimDJZBVO;
    DJZBVO oldDZJBVO = djzbvo.getm_OldVO();
    if (DJZBHeaderVO.SLIMED.equalsIgnoreCase(((DJZBHeaderVO)slimDJZBVO.getParentVO()).getIsSlim())) {
      try {
        if ((null == slimDJZBVO) || (null == slimDJZBVO.getParentVO()) || (null == slimDJZBVO.getParentVO().getPrimaryKey())) {
          throw new BusinessShowException("nc.bs.ep.dj.DJZBBO.editDj(DJZBVO) editDj:");
        }
        if ("ss".equals(((DJZBHeaderVO)slimDJZBVO.getParentVO()).getDjdl()))
          oldDZJBVO = findByPrimaryKey_SS(slimDJZBVO.getParentVO().getPrimaryKey());
        else
          oldDZJBVO = findByPrimaryKey(slimDJZBVO.getParentVO().getPrimaryKey());
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        throw new BusinessShowException("nc.bs.ep.dj.DJZBBO.editDj(DJZBVO) editDj:" + e);
      }
      if (null == oldDZJBVO)
      {
        throw new BusinessShowException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000503"));
      }
      DJZBVOTreator.revertDJZBVO(oldDZJBVO, slimDJZBVO);
      ((DJZBHeaderVO)oldDZJBVO.getParentVO()).setTs(((DJZBHeaderVO)djzbvo.getParentVO()).getts());

      djzbvo.setm_OldVO(oldDZJBVO);
    }

    DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])djzbvo.getChildrenVO();
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();
    Map fbpks = new HashMap();
    List ddhhs = new ArrayList();
    int i = 0; for (int size = items.length; i < size; i++) {
      if (!DJZBVOConsts.XTConfirmed.equals(items[i].getDjxtflag()))
        fbpks.put(items[i].getFb_oid(), items[i]);
      items[i].setBilldate(((DJZBHeaderVO)djzbvo.getParentVO()).getDjrq());
      if ((DJZBVOConsts.FromXT.equals(head.getLybz())) && (items[i].getDdhh() != null))
        ddhhs.add(items[i].getDdhh());
    }
    if (fbpks.size() == 0) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006", "UPT2006-v51-000017"));
    }
    DJZBDAO dmo = new DJZBDAO();
    if (ddhhs.size() > 0) {
      dmo.updateXtFlag(ddhhs, null);
    }
    LoanController lcr = new LoanController();
    ResMessage res = lcr.checkBefSave(djzbvo);
    if (!res.isSuccess) {
      throw new BusinessException(res.SysErrMsg);
    }
    int k = 1;
    try {
      djzbvo.setParam_Ext_Save();
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("nc.bs.ep.dj.DJZBBO.editDj(DJZBVO) setParam_Ext_Save:" + e);
    }
    if (djzbvo.m_isQr)
    {
      return cooperate_affirm(djzbvo);
    }
    try
    {
      djzbvo.setParam_Ext_Save();
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("nc.bs.ep.dj.DJZBBO.editDj(DJZBVO) setParam_Ext_Save:" + e);
    }

    try
    {
      lockDJ(head.getVouchid());

      if (head.getDjzt().intValue() == -99) {
        head.setDjzt(new Integer(1));
        return saveFromTemporaryDj(djzbvo);
      }

      if (djzbvo.getChildrenVO() != null)
        items = (DJZBItemVO[])(DJZBItemVO[])djzbvo.getChildrenVO();
      String tablename = "arap_djzb";

      ApplayBillBO aBo = new ApplayBillBO();
      aBo.isDistributes(djzbvo);

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();

      BusiTransVO[] busitransvos = extbo.initBusiTrans("edit", head.getPzglh());

      beforeEditInf(busitransvos, djzbvo);
      Log.getInstance(getClass()).debug("外接口修改单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

      if (head.getDjdl().equals("ss")) {
        tablename = "arap_item";
        djzbvo = dmo.update_SS(djzbvo);
      } else {
        djzbvo = dmo.update(doPayterm(djzbvo));

        editCost(djzbvo);

        if (((head.getQcbz() == null) || (!head.getQcbz().booleanValue())) && 
          (head.getSsflag().equals("0"))) {
          ItemconfigBO itemconfigbo = new ItemconfigBO();
          lock_item_bill(items, head.getLrr(), 1);

          itemconfigbo.unShenHe((DJZBVO)oldDZJBVO.clone(), true);
          DJZBVO tempvo = (DJZBVO)djzbvo.clone();
          DJZBItemVO[] tempitems = (DJZBItemVO[])(DJZBItemVO[])djzbvo.getChildrenVO();
          ArrayList al = new ArrayList();
          for (int i1 = 0; i1 < tempitems.length; i1++) {
            if (tempitems[i1].getStatus() == 3) {
              continue;
            }
            al.add(tempitems[i1]);
          }
          if (al.size() > 0) {
            tempitems = new DJZBItemVO[al.size()];
            tempitems = (DJZBItemVO[])(DJZBItemVO[])al.toArray(tempitems);
            tempvo.setChildrenVO(tempitems);
          }
          itemconfigbo.ShenHeSave(tempvo);
        }

        if (((head.getQcbz() == null) || (!head.getQcbz().booleanValue())) && 
          (head.getXtflag() != null) && (head.getXtflag().equals("保存")))
        {
          deleteXtBill((String[])fbpks.keySet().toArray(new String[0]));

          BillCooperateBO billcooperatebo = new BillCooperateBO();
          DJZBVO vo = (DJZBVO)djzbvo.clone();
          vo.setChildrenVO((DJZBItemVO[])(DJZBItemVO[])fbpks.values().toArray(new DJZBItemVO[0]));
          billcooperatebo.doCooperate(vo);
        }

      }

      head.setTs(new UFDateTime(dmo.getTsByPrimaryKey(head.getVouchid(), tablename)));

      djzbvo.setParentVO(head);

      long t2 = System.currentTimeMillis();

      afterEditInf(busitransvos, djzbvo);
      Log.getInstance(getClass()).debug("外接口修改单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      String ts = dmo.getTsByPrimaryKey(head.getVouchid(), tablename);
      if (ts != null) {
        head.setTs(new UFDateTime(ts));
      }
      djzbvo.setParentVO(head);

      if (head.getSpzt() != null)
      {
        if (head.getSpzt().equals("0")) {
          djzbvo.setCheckState(2);
        }

      }

    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);

      throw new BusinessShowException(e.getMessage());
    }

    djzbvo.m_Resmessage.strMessage += res.strMessage;
    ((DJZBHeaderVO)djzbvo.getParentVO()).IsSlim = null;
    return djzbvo;
  }

  public void deleteOutBill(String id) throws BusinessException
  {
    Log.getInstance(getClass()).debug("ddlx is :" + id);
    String whereString = "   where  exists (select vouchid from arap_djfb  where  zb.vouchid = arap_djfb.vouchid and arap_djfb.ddlx = '" + id.trim() + "' ) and  zb.dr=0 ";

    deleteOutBillbyWhere(whereString);
  }

  public String editZyx(DJFBVO djfbvo)
    throws BusinessException
  {
    try
    {
      DJFBDAO dmo = new DJFBDAO();

      dmo.updateZYX(djfbvo);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::delete(DJZBPK) Exception!", e);
    }
    return "";
  }

  public boolean exist_ss(String pk_corp, String[] ywybm, String billCode, String vouchid)
    throws BusinessException
  {
    boolean b = false;
    try {
      DJZBDAO dmo = new DJZBDAO();
      b = dmo.exist_ss(pk_corp, ywybm, billCode, vouchid);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::exist_ss(pk_corp,ywybm,billCode) Exception!", e);
    }
    return b;
  }

  public boolean existByBm(String bm, String pk_corp)
    throws BusinessException
  {
    boolean b = false;
    try {
      DJZBDAO dmo = new DJZBDAO();
      b = dmo.existByBm(bm, pk_corp);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::getSPZT(key) Exception!", e);
    }
    return b;
  }

  public boolean existByKey(String key)
    throws BusinessException
  {
    boolean b = false;
    try {
      DJZBDAO dmo = new DJZBDAO();
      b = dmo.existByKey(key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::existByKey(key) Exception!", e);
    }
    return b;
  }

  private DJZBVO filterSSDJZBVOWithOpenItem(DJZBVO oldDJZBVO)
  {
    DJZBVO newDJZBVO = new DJZBVO();
    DJZBHeaderVO headerVO = (DJZBHeaderVO)oldDJZBVO.getParentVO();
    Vector vOpenItem = new Vector();

    DJZBItemVO[] allItemVOs = (DJZBItemVO[])(DJZBItemVO[])oldDJZBVO.getChildrenVO();

    String pkFunctionary = headerVO.getZdr();

    UFDate dateClose = headerVO.getZdrq();
    for (int i = 0; i < allItemVOs.length; i++) {
      boolean isClosed = ssItemIsClosed(allItemVOs[i]);

      if (isClosed)
        continue;
      allItemVOs[i].setCloser(pkFunctionary);
      allItemVOs[i].setClosedate(dateClose);
      vOpenItem.addElement(allItemVOs[i]);
    }

    DJZBItemVO[] openItemVOs = new DJZBItemVO[vOpenItem.size()];
    vOpenItem.copyInto(openItemVOs);

    newDJZBVO.setParentVO(headerVO);

    newDJZBVO.setChildrenVO(openItemVOs);
    return newDJZBVO;
  }

  public DJZBVO[] findByPk_bankrecive(String key)
    throws BusinessException
  {
    DJZBVO[] dJZBs = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      dJZBs = dmo.findByPk_bankrecive(key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findByPk_bankrecive(key) Exception!", e);
    }
    return dJZBs;
  }

  public DJZBVO findByPrimaryKey(String key)
    throws BusinessException
  {
    DJZBVO dJZB = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      dJZB = dmo.findByPrimaryKey(key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findByPrimaryKey(DJZBPK) Exception!", e);
    }
    return dJZB;
  }

  public DJZBVO[] findByPrimaryKeys(String[] keys) throws BusinessException {
    DJZBVO[] dJZB = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      dJZB = dmo.findByPrimaryKeys(keys);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findByPrimaryKey(DJZBPK) Exception!", e);
    }
    return dJZB;
  }

  public DJZBVO findByPrimaryKey_SS(String key)
    throws BusinessException
  {
    DJZBVO dJZB = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      dJZB = dmo.findByPrimaryKey_SS(key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findByPrimaryKey_SS(DJZBPK) Exception!", e);
    }
    return dJZB;
  }

  public DJZBVO[] findByPrimaryKeys_SS(Vector key) throws BusinessException
  {
    DJZBVO[] dJZB = null;

    String[] keys = new String[key.size()];
    key.copyInto(keys);
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      dJZB = dmo.findDjByPrimaryKeys_SS(keys);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findByPrimaryKey_SS(DJZBPK) Exception!", e);
    }
    return dJZB;
  }

  public DJZBVO[] findDj_sell()
    throws BusinessException
  {
    DJZBVO[] dJZBs = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      dJZBs = dmo.findDj_sell();
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findDj_sell( ) Exception!", e);
    }
    return dJZBs;
  }

  public DJZBItemVO[] findfengcunByKey(DJZBItemVO[] items)
    throws BusinessException
  {
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      for (int i = 0; i < items.length; i++) {
        items[i] = dmo.findfengcunByKey(items[i]);
      }
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findfengcunByKey(items) Exception!", e);
    }
    return items;
  }

  public DJZBHeaderVO findHeaderByPrimaryKey(String key)
    throws BusinessException
  {
    DJZBHeaderVO djzbheadervo = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      djzbheadervo = dmo.findHeaderByPrimaryKey(key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findHeaderByPrimaryKey(key) Exception!", e);
    }
    return djzbheadervo;
  }

  public DJZBHeaderVO findHeaderByPrimaryKey_SS(String key)
    throws BusinessException
  {
    DJZBHeaderVO djzbheadervo = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      djzbheadervo = dmo.findHeaderByPrimaryKey_SS(key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findHeaderByPrimaryKey(key) Exception!", e);
    }
    return djzbheadervo;
  }

  public DJZBVO[] findItem_ByHead(DJZBHeaderVO[] heads)
    throws BusinessException
  {
    DJZBVO[] vos = null;
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      vos = dmo.getDjVObyHeaderVos(heads);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findItem_ByHead(heads) Exception!", e);
    }
    return vos;
  }

  public String getAreaNameByCode(String area_Code)
    throws BusinessException
  {
    String area_Name = "";
    try {
      DJZBDAO dmo = new DJZBDAO();
      area_Name = dmo.getAreaNameByCode(area_Code);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::getAreaNameByCode( ) Exception!", e);
    }
    return area_Name;
  }

  public Integer getCheckflag(String vouchid)
    throws BusinessException
  {
    Integer checkflag = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      checkflag = dmo.getCheckflag(vouchid);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::getCheckflag( ) Exception!", e);
    }
    return checkflag;
  }

  public Integer getClbzByPkey(String key)
    throws BusinessException
  {
    Integer clbz = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      clbz = dmo.getClbzByPkey(key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::getClbzByPkey(DJZBPK) Exception!", e);
    }
    return clbz;
  }

  public Integer getCountByDjbh(String djbh, String pk_corp)
    throws BusinessException
  {
    Integer count = null;
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      count = dmo.getCountByDjbh(djbh, pk_corp);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::getCountByDjbh(djbh,pk_corp) Exception!", e);
    }
    return count;
  }

  private UFDate getdate(UFDate date, Integer sdc, int iCount)
  {
    int d1 = date.getDay();
    int m1 = date.getMonth();
    int y1 = date.getYear();
    int d2 = sdc.intValue();
    if (d1 > d2) {
      iCount++;
    }

    if (m1 + iCount > 12) {
      y1 += (m1 + iCount) / 12;
    }
    m1 = (m1 + iCount) % 12;
    if (m1 == 0) {
      m1 = 12;
    }
    if (UFDate.getDaysMonth(y1, m1) < d2) {
      d2 = UFDate.getDaysMonth(y1, m1);
    }

    return new UFDate(y1 + "-" + m1 + "-" + d2);
  }

  public String getDjbh(DJZBVO djzbvo)
    throws BusinessException
  {
    ArapBusinessException E = new ArapBusinessException();
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();
    DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])djzbvo.getChildrenVO();
    String Djbh = " ";

    BillCodeObjValueVO vo = new BillCodeObjValueVO();

    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0000404"), head.getDwbm());
    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001589"), items == null ? null : items[0].getKsbm_cl());
    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0000275"), items == null ? null : items[0].getKsbm_cl());
    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0004064"), items == null ? null : items[0].getDeptid());
    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPT2006030102-000013"), items == null ? null : items[0].getYwybm());

    Log.getInstance(getClass()).debug("单据类型编码:" + head.getDjlxbm() + " 单位:" + head.getDwbm());
    try
    {
      BillcodeGenerater codeRule = new BillcodeGenerater();
      Djbh = codeRule.getBillCode(head.getDjlxbm(), head.getDwbm(), null, vo);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      Log.getInstance(getClass()).debug("取单据号出错2: " + e + e.getMessage());
      E.setResMessage(new ResMessage());
      E.m_ResMessage.strMessage = (NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000525") + e.getMessage());
      E.m_Exception = e;
      throw E;
    }

    return Djbh;
  }

  private UFDouble getDRate(UFDouble je, double r1)
  {
    if (je == null) je = new UFDouble(0.0D);
    return new UFDouble(je.doubleValue() * r1 / 100.0D);
  }

  public Vector getFreePropertys(String m_szxmid)
    throws BusinessException
  {
    Vector v = new Vector();
    try {
      DJZBDAO dmo = new DJZBDAO();
      String[] frees = dmo.getFreePropertys(m_szxmid);
      if (frees == null)
        return null;
      DefdefDMO defdmo = new DefdefDMO();
      DefdefVO tempvo = null;
      for (int i = 0; i < 10; i++)
      {
        if (frees[i] == null)
          continue;
        tempvo = new DefdefVO();
        try
        {
          tempvo = defdmo.findByPrimaryKey(frees[i]);

          tempvo.setpk_costsubj(m_szxmid);

          tempvo.setm_freesxh(new Integer(i + 1));

          v.addElement(tempvo);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
        }
      }
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("TESTBean::getFreePropertys(String m_szxmid) Exception!", e);
    }
    return v;
  }

  public String getOfficialprintuser(String key)
    throws BusinessException
  {
    String Officialprintuser = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      Officialprintuser = dmo.getOfficialprintuser(key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::getOfficialprintuser(DJZBPK) Exception!", e);
    }
    return Officialprintuser;
  }

  public String getSPZTByPk(String tablename, String key)
    throws BusinessException
  {
    String zyx20 = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      zyx20 = dmo.getSPZTByPk(tablename, key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::getSPZT(key) Exception!", e);
    }
    return zyx20;
  }

  public String getTsByPrimaryKey(String key, String tableName)
    throws BusinessException
  {
    String ts = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      ts = dmo.getTsByPrimaryKey(key, tableName);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::getTsByPrimaryKey(key,tableName) Exception!", e);
    }
    return ts;
  }

  public DJZBVO[] getXtVOsByPk(String vouchid)
    throws BusinessException
  {
    List retLst = new ArrayList();
    try {
      DJZBDAO dmo = new DJZBDAO();
      List lst = dmo.getXtMsgBypk(vouchid);
      int i = 0; for (int size = lst.size(); i < size; i++)
        retLst.add(findByPrimaryKey(((String[])(String[])lst.get(i))[1]));
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }
    return (DJZBVO[])retLst.toArray(new DJZBVO[0]);
  }

  public Integer getXTCountBYpk(String vouchid)
    throws BusinessException
  {
    Integer count = null;
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      count = dmo.getXTCountBYpk(vouchid);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::getXTCountBYpk(vouchid) Exception!", e);
    }
    return count;
  }

  public DJZBVO insert(DJZBVO dJZB)
    throws BusinessException
  {
    if (-99 == ((DJZBHeaderVO)dJZB.getParentVO()).getDjzt().intValue()) {
      ((DJZBHeaderVO)dJZB.getParentVO()).setDjzt(new Integer(1));
      return insertOneDj(dJZB, false);
    }

    return insertOneDj(dJZB, true);
  }
  private DJZBVO insertOneDj(DJZBVO dJZB, boolean isNew) throws BusinessException {
    LoanController lcr = new LoanController();
    ResMessage res = lcr.checkBefSave(dJZB);

    if (!res.isSuccess)
      throw new BusinessException(res.SysErrMsg);
    try
    {
      DJZBHeaderVO head = (DJZBHeaderVO)dJZB.getParentVO();
      DJZBItemVO[] items = null;
      items = (DJZBItemVO[])(DJZBItemVO[])dJZB.getChildrenVO();
      if ((head.getHzbz() != null) && (head.getHzbz().trim().length() > 0)) {
        head.setLybz(new Integer(0));
      }
      if ((head.getDjdl().equals("ys")) || (head.getDjdl().equals("sk")))
      {
        for (int i = 0; i < items.length; i++) {
          if ((items[i] == null) || (items[i].getWldx().intValue() != 0) || (
            (items[i].getOrdercusmandoc() != null) && (items[i].getOrdercusmandoc().trim().length() >= 1)))
            continue;
          items[i].setOrdercusmandoc(items[i].getKsbm_cl());
        }
      }

      if ((head.getLybz().intValue() != 9) && (
        (head.getHzbz() != null) || (head.getDjbh() == null) || (head.getDjbh().trim().length() <= 0)))
      {
        head.setDjbh(getDjbh(dJZB));
      }

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();

      BusiTransVO[] busitransvos = null;
      busitransvos = extbo.initBusiTrans("add", head.getPzglh());

      beforeAddInf(busitransvos, dJZB);

      Log.getInstance(getClass()).debug("add外接口新增单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

      DJZBDAO dmo = new DJZBDAO();
      DJZBVO vo = null;

      if (isNew)
        vo = dmo.insert(dJZB);
      else {
        vo = dmo.update(dJZB);
      }

      if ((head.getQcbz() == null) || (!head.getQcbz().booleanValue()))
      {
        if ((!head.getDjdl().equals("ss")) && ("0".equals(head.getSsflag()))) {
          ItemconfigBO itemconfigbo = new ItemconfigBO();
          lock_item_bill(items, head.getLrr(), 1);

          itemconfigbo.ShenHeSave(vo);
          lock_item_bill(items, head.getLrr(), 1);
        }

        if ((head.getXtflag() != null) && (head.getXtflag().equals("保存")))
        {
          BillCooperateBO billcooperatebo = new BillCooperateBO();
          billcooperatebo.doCooperate(dJZB);
        }

      }

      long t2 = System.currentTimeMillis();

      afterAddInf(busitransvos, dJZB);

      Log.getInstance(getClass()).debug("add外接口新增单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      String sts = dmo.getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
      if (sts.trim().length() >= 3)
      {
        head.setTs(new UFDateTime(sts));
      }
      vo.m_Resmessage.strMessage += res.strMessage;

      return vo;
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
      throw new BusinessException(e.getMessage());
    }
   
  }

  public DJZBVO insert_Cooperate(DJZBVO dJZB)
    throws BusinessException
  {
    try
    {
      DJZBHeaderVO head = (DJZBHeaderVO)dJZB.getParentVO();
      DJZBItemVO[] items = null;
      items = (DJZBItemVO[])(DJZBItemVO[])dJZB.getChildrenVO();
      if ((head.getHzbz() != null) && (head.getHzbz().trim().length() > 0)) {
        head.setLybz(new Integer(0));
      }
      if ((head.getDjdl().equals("ys")) || (head.getDjdl().equals("sk")))
      {
        for (int i = 0; i < items.length; i++) {
          if ((items[i] == null) || (items[i].getWldx().intValue() != 0) || (
            (items[i].getOrdercusmandoc() != null) && (items[i].getOrdercusmandoc().trim().length() >= 1)))
            continue;
          items[i].setOrdercusmandoc(items[i].getKsbm_cl());
        }

      }

      if ((head.getLybz().intValue() != 9) && (
        (head.getDjbh() == null) || (head.getDjbh().trim().length() <= 0)))
      {
        head.setDjbh(getDjbh(dJZB));
      }

      DJZBDAO dmo = new DJZBDAO();

      DJZBVO vo = dmo.insert(dJZB);

      return vo;
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::insert_Cooperate(dJZB) Exception!", e);
    }
    
  }

  public DJZBVO insert_SS(DJZBVO dJZB)
    throws BusinessException
  {
    try
    {
      DJZBHeaderVO head = (DJZBHeaderVO)dJZB.getParentVO();

      boolean bYuTiLocked = checkYuTiParallell(dJZB);

      if (!bYuTiLocked)
        throw new Exception(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000526"));
      head.setDjbh(getDjbh(dJZB));
      dJZB.setParam_Ext_Save();

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();
      BusiTransVO[] busitransvos = extbo.initBusiTrans("add", head.getPzglh());
      beforeAddInf(busitransvos, dJZB);
      Log.getInstance(getClass()).debug("外接口新增单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

      DJZBDAO dmo = new DJZBDAO();

      if ((head.getDjbh() == null) || (head.getDjbh().trim().length() == 0)) {
        head.setDjbh(getDjbh(dJZB));
      }

      DJZBVO vo = dmo.insert_SS(dJZB);

      long t2 = System.currentTimeMillis();

      vo = afterAddInf(busitransvos, vo);
      Log.getInstance(getClass()).debug("外接口新增单据前动作后所用时间:" + (System.currentTimeMillis() - t2));

      String sts = dmo.getTsByPrimaryKey(head.getVouchid(), "arap_item");
      if (sts.trim().length() >= 3)
      {
        head.setTs(new UFDateTime(sts));
      }

      return vo;
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::insert(dJZB) Exception!", e);
    }
  }

  public boolean isFa(String pk_corp)
    throws BusinessException
  {
    try
    {
      Object[] retobj = Proxy.getICreateCorpQueryService().queryEnabledPeriod(pk_corp, "FA");

      return (retobj != null) && (((String[])(String[])retobj)[0] != null);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::isFa(pk_corp) Exception!", e);
    }
  }

  public boolean lock_item_bill(DJZBItemVO[] items, String user, int czflag)
    throws BusinessException
  {
    if ((items == null) || (items.length < 1)) {
      return true;
    }
    int leng = items.length;
    String[] djpks = null;

    Vector v = new Vector();

    for (int i = 0; i < leng; i++) {
      try
      {
        String djpk = items[i].getItem_bill_pk();

        if ((djpk != null) && (djpk.trim().length() > 0) && (
          (v.isEmpty()) || (v.indexOf(djpk) < 0)))
        {
          v.addElement(djpk);
        }
        if (czflag < 0) {
          djpk = items[i].getItem_bill_pk();
          if ((djpk != null) && (djpk.trim().length() > 0) && (
            (v.isEmpty()) || (v.indexOf(djpk) < 0)))
          {
            v.addElement(djpk);
          }
        }
      }
      catch (Exception e)
      {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        throw new BusinessShowException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000527"), e);
      }
    }

    if (v.size() > 0) {
      djpks = new String[v.size()];
      v.copyInto(djpks);
    }
    try {
      if ((djpks != null) && (djpks.length > 0))
      {
        ApplayBillDMO appdmo = new ApplayBillDMO();
        appdmo.updateForLockByPks("arap_item", djpks);

        for (String djpk : djpks)
          KeyLock.dynamicLock(djpk);
      }
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000527"), e);
    }

    return true;
  }

  public boolean officialPrint(DJZBHeaderVO head)
    throws BusinessException
  {
    boolean b = false;
    try {
      lockDJ(head.getVouchid());
      DJZBDAO dmo = new DJZBDAO();
      b = dmo.officialPrint(head);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::officialPrint(DJZBVO) Exception!", e);
    }
    return b;
  }

  public String pausetransact(String key, String pausetransact)
    throws BusinessException
  {
    String strTS;
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      lockDJ(key);
      strTS = dmo.pausetransact(key, pausetransact);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::pausetransact(key,pausetransact) Exception!", e);
    }

    return strTS;
  }

  public DJZBVO[] queryDjAll_Hz(DjCondVO djcon)
    throws BusinessException
  {
    DJZBVO[] djzbvos = null;
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      djzbvos = dmo.queryDjAll_Hz(djcon);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::queryDjAll(djcon) Exception!", e);
    }
    return djzbvos;
  }

  public DJZBHeaderVO[] queryDjLbbyTS(DjCondVO djcon)
    throws BusinessException
  {
    DJZBHeaderVO[] head = null;
    try {
      String key = djcon.getSqlWhere();
      DJZBDAO dmo = new DJZBDAO();
      head = dmo.queryHeadbyTS(key);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findByPrimaryKey(DJZBPK) Exception!", e);
    }
    return head;
  }

  public DJZBItemVO[] queryDjzbitems(String djzboid)
    throws BusinessException
  {
    DJZBItemVO[] dJZBitem = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      dJZBitem = dmo.findItemsForHeader(djzboid);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage(), e);
    }
    return dJZBitem;
  }

  public DJZBItemVO[] queryDjzbitemsDelIncluded(String djzboid)
    throws BusinessException
  {
    if ((djzboid == null) || (djzboid.length() == 0)) {
      return null;
    }
    DJZBItemVO[] dJZBitem = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      Vector v = dmo.findItemsByCondition(" where vouchid='" + djzboid + "'");
      if (v.size() > 0) {
        dJZBitem = new DJZBItemVO[v.size()];
        v.copyInto(dJZBitem);
      }
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::queryDjzbitemsDelIncluded(DJZBPK) Exception!", e);
    }
    return dJZBitem;
  }

  public DJZBItemVO[] queryDjzbitems_SS(String djzboid)
    throws BusinessException
  {
    DJZBItemVO[] dJZBitem = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      dJZBitem = dmo.findItemsForHeader_SS(djzboid);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findByPrimaryKey(DJZBPK) Exception!", e);
    }
    return dJZBitem;
  }

  public DJZBItemVO[] queryDjzbitems_SS4(String djzboid)
    throws BusinessException
  {
    DJZBItemVO[] dJZBitem = null;
    try {
      DJZBDAO dmo = new DJZBDAO();
      dJZBitem = dmo.findItemsForHeader_SS4(djzboid);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findByPrimaryKey(DJZBPK) Exception!", e);
    }
    return dJZBitem;
  }

  public DJFBVO queryZyx(String djfboid)
    throws BusinessException
  {
    DJFBVO dJZBitem = null;
    try {
      DJFBDAO dmo = new DJFBDAO();
      dJZBitem = dmo.findByPrimaryKey(djfboid);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBean::findByPrimaryKey(DJZBPK) Exception!", e);
    }
    return dJZBitem;
  }

  public void returnBillCode(DJZBVO djzbvo)
    throws BusinessException
  {
    returnBillCode(djzbvo, true);
  }

  public ArrayList<Object> save(DJZBVO djzbvo)
    throws BusinessException, BusinessException
  {
    ArrayList al = new ArrayList();
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();
    DJZBVO dj = null;

    if ((head != null) && (head.getDjdl().equals("ss")))
      dj = saveDj_SS(djzbvo);
    else if (djzbvo.m_isQr)
      dj = editDj(djzbvo);
    else {
      dj = saveDj(djzbvo);
    }
    String key = ((DJZBHeaderVO)(DJZBHeaderVO)dj.getParentVO()).getPrimaryKey();
    al.add(0, key);
    al.add(1, dj);
    return al;
  }

  public ArrayList saveAPBillForSettle(DJZBVO[] djzbvos)
    throws BusinessException, BusinessException
  {
    if ((djzbvos == null) || (djzbvos.length == 0))
      return null;
    ArrayList al = new ArrayList();
    for (int i = 0; i < djzbvos.length; i++) {
      ArrayList reAr = save(djzbvos[i]);
      al.add(i, reAr);
    }

    return al;
  }

  public DJZBVO saveDj(DJZBVO djzbvo)
    throws BusinessException
  {
    try
    {
      DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])djzbvo.getChildrenVO();
      int i = 0; for (int size = items.length; i < size; i++) {
        items[i].setBilldate(((DJZBHeaderVO)djzbvo.getParentVO()).getDjrq());
        if ((null == items[i].getQxrq()) || (items[i].getQxrq().toString().length() == 0)) {
          items[i].setQxrq(((DJZBHeaderVO)djzbvo.getParentVO()).getEffectdate());
        }
      }

      djzbvo.setParam_Ext_Save();
      ARAPDjBSUtil.supplementXTFlag(djzbvo);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
    return insert(doPayterm(djzbvo));
  }

  public DJZBVO saveDj_Cooperate(DJZBVO djzbvo)
    throws BusinessException
  {
    return insert_Cooperate(doPayterm(djzbvo));
  }

  public DJZBVO saveDj_Hz(DJZBVO djzbvo)
    throws BusinessException
  {
    return insert(doPayterm(djzbvo));
  }

  public DJZBVO saveDj_SS(DJZBVO djzbvo)
    throws BusinessException
  {
    return insert_SS(djzbvo);
  }

  public DJFBVO saveZyx(DJFBVO djfbvo)
    throws BusinessException
  {
    try
    {
      DJFBDAO dmo = new DJFBDAO();
      DJZBItemVO item = (DJZBItemVO)djfbvo.getParentVO();
      dmo.deleteItemsForHeader(item.getFb_oid());
      DJFBVO key = dmo.insert(djfbvo);

      return key;
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::insert(DJZBVO) Exception!", e);
    }
  }

  public void setBankRecivePk(String[] fboid, String pk_bankrecive, String[] tss)
    throws BusinessException
  {
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      for (int i = 0; i < fboid.length; i++) {
        dmo.setBankRecivePk(fboid[i], pk_bankrecive, tss[i]);
      }

    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::update(DJZBVO) Exception!", e);
    }
  }

  public void setOtherSysFlag(String fb_oid, String othersysflag, UFBoolean pausetransact)
    throws BusinessException
  {
    try
    {
      DJZBDAO dmo = new DJZBDAO();

      dmo.setOtherSysFlag(fb_oid, othersysflag, pausetransact);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::setOtherSysFlag(fb_oid,othersysflag,pausetransact) Exception!", e);
    }
  }

  private boolean ssItemIsClosed(DJZBItemVO itemVO)
  {
    String strCloser = itemVO.getCloser();

    return (strCloser != null) && (strCloser.trim().length() != 0);
  }

  public ResMessage[] unAuditABills2(DJZBVO[] djs)
    throws BusinessException
  {
    if ((djs == null) || (djs.length < 1))
      return null;
    ResMessage[] res = new ResMessage[djs.length];

    ArapExtInfRunBO extbo = new ArapExtInfRunBO();

    BusiTransVO[] busitransvos = extbo.initBusiTrans("unshenhe", ((DJZBHeaderVO)djs[0].getParentVO()).getPzglh());

    DJZBHeaderVO head = null;

    DJZBHeaderVO[] heads = null;
    DJZBVO[] dj_2 = new DJZBVO[djs.length]; DJZBVO[] dj_temp = null;
    Vector v = new Vector();
    int i = 0; for (int j = 0; i < djs.length; i++) {
      if (djs[i].getChildrenVO() == null) {
        v.addElement(djs[i].getParentVO());
      }
      else {
        dj_2[j] = djs[i];
        j++;
      }
    }
    try
    {
      if (v.size() > 0) {
        heads = new DJZBHeaderVO[v.size()];
        v.copyInto(heads);
        DJZBDAO dmo = new DJZBDAO();
        dj_temp = dmo.getDjVObyHeaderVos(heads);
        System.arraycopy(dj_temp, 0, dj_2, dj_2.length - v.size(), v.size());
      }
      try
      {
        ArapDjBsCheckerBO checkBo = new ArapDjBsCheckerBO();
        res = checkBo.checkUnApproveBills(dj_2);
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        throw new BusinessException(e.getMessage(), e);
      }
      for (int i1 = 0; i1 < dj_2.length; i1++) {
        head = (DJZBHeaderVO)dj_2[i1].getParentVO();

        if (res[i1] == null) {
          try {
            lockDJ(head.getVouchid());
            res[i1] = Proxy.getIArapBillPrivate().unAuditABill_RequiresNew(dj_2[i1], busitransvos);
            if (res[i1].isSuccess)
              res[i1].strMessage = (NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000532") + head.getDjbh() + "\n" + res[i1].strMessage);
            else
              res[i1].strMessage = (NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000533") + head.getDjbh() + "\n" + res[i1].strMessage);
          }
          catch (Exception e) {
            Log.getInstance(getClass()).error(e.getMessage(), e);
            if (res[i1] == null)
              res[i1] = new ResMessage();
            res[i1].isSuccess = false;

            res[i1].strMessage = e.getMessage();
            res[i1].strMessage = (NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000533") + head.getDjbh() + "\n" + res[i1].strMessage);
          }
        }

        res[i1].listIndex = head.listIndex;
        res[i1].vouchid = head.getVouchid();
      }
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("nc.bs.ep.dj.DJZBBO.unAuditABills2", e);
    }

    Log.getInstance(getClass()).debug("批反审核返回...");
    return res;
  }
  public UFBoolean getClbzByPkeys(String[] keys) throws BusinessException {
    ArapDjBsCheckerDAO dmo = new ArapDjBsCheckerDAO();

    Hashtable hashClbz = null;
    try {
      String temp_tab_name = dmo.CreateTempBillTab(keys, "tmp_arapbill1");
      hashClbz = dmo.getClbzByPkeys(keys, temp_tab_name);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }

    if ((null != hashClbz) && (hashClbz.size() > 0)) {
      return UFBoolean.TRUE;
    }
    return UFBoolean.FALSE;
  }

  public void wszz(DJZBVO dJZB)
    throws BusinessException
  {
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      dmo.wszz(dJZB);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException("DJZBBO::wszz(DJZBVO) Exception!", e);
    }
  }

  public ResMessage ysfDj(DJZBVO djzbvo)
    throws BusinessException, BusinessException
  {
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();
    String vouchid = head.getPrimaryKey();
    lockDJ(head.getVouchid());
    ResMessage res = new ResMessage();
    try {
      head.setPrepay(new UFBoolean(true));
      BusiTransVO[] busitransvos = null;
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();

      busitransvos = extbo.initBusiTrans("prepay", head.getPzglh());
      if (busitransvos != null) {
        for (int i = 0; i < busitransvos.length; i++) {
          try {
            ((IArapPrePayPlugin)busitransvos[i].getInfClass()).beforePrepayAct(djzbvo);
          }
          catch (Exception e)
          {
            Log.getInstance(getClass()).error(e.getMessage(), e);
            Log.getInstance(getClass()).debug(busitransvos[i].getSystemname() + busitransvos[i].getNote() + "\n" + e);

            String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
            throw new BusinessShowException(strerr);
          }
        }

      }

      DJZBDAO dmo = new DJZBDAO();
      dmo.ysfDj(vouchid);
      if (busitransvos != null) {
        for (int i = 0; i < busitransvos.length; i++) {
          try {
            ((IArapPrePayPlugin)busitransvos[i].getInfClass()).afterPrepayAct(djzbvo);
          }
          catch (Exception e)
          {
            Log.getInstance(getClass()).error(e.getMessage(), e);
            Log.getInstance(getClass()).debug(busitransvos[i].getSystemname() + busitransvos[i].getNote() + "\n" + e);

            String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
            throw new BusinessShowException(strerr);
          }
        }
      }

      ArapDjBsCheckerBO bocheck = new ArapDjBsCheckerBO();
      try
      {
        bocheck.supplementAllInfos(new DJZBVO[] { djzbvo });
      } catch (Exception e) {
        throw new BusinessShowException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000545"), e);
      }
      if (head.getSxbz().intValue() == 10)
      {
        DapMsgVO PfStateVO = DjbsStatTool.getDapmsgVOforPrePay(djzbvo);

        PfStateVO.setMsgType(0);

        Logger.debug(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000534"));

        FipCallFacade fcf = new FipCallFacade();
        fcf.sendMessage(PfStateVO, djzbvo);
      }

      res.m_Ts = dmo.getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      if ((e instanceof BusinessShowException))
        throw ((BusinessShowException)e);
      throw new BusinessShowException(e.getMessage());
    }
    return res;
  }

  public void returnBillCode(DJZBVO djzbvo, boolean isNewTrans)
    throws BusinessException
  {
    ArapBusinessException E = new ArapBusinessException();
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();

    DJZBItemVO[] items = djzbvo.getChildrenVO() == null ? null : (DJZBItemVO[])(DJZBItemVO[])djzbvo.getChildrenVO();

    if (items == null) {
      DJZBDAO djdmo;
      try { djdmo = new DJZBDAO();
      } catch (Exception e)
      {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        throw new BusinessShowException("DJZBBO::returnBillCode(DJZBVO djzbvo,boolean isNewTrans)", e);
      }

      try
      {
        if ((head.getPzglh().intValue() == 3) || (head.getDjdl().equals("ss")))
          items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
        else
          items = djdmo.findItemsForHeader(head.getPrimaryKey());
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        throw new BusinessShowException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000528"));
      }

      djzbvo.setChildrenVO(items);
    }

    BillCodeObjValueVO vo = new BillCodeObjValueVO();
    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0000404"), head.getDwbm());
    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001589"), items == null ? null : items[0].getKsbm_cl());
    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0000275"), items == null ? null : items[0].getKsbm_cl());
    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0004064"), items == null ? null : items[0].getDeptid());
    vo.setAttributeValue(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPT2006030102-000013"), items == null ? null : items[0].getYwybm());

    Log.getInstance(getClass()).debug("单据类型编码:" + head.getDjlxbm() + " 单位:" + head.getDwbm());
    try
    {
      if (!isNewTrans)
        Proxy.getIBillcodeRuleService().returnBillCodeOnDelete(head.getDwbm(), head.getDjlxbm(), head.getDjbh(), vo);
      else
        Proxy.getIBillcodeRuleService().returnBillCodeOnDelete(head.getDwbm(), head.getDjlxbm(), head.getDjbh(), vo);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      E.m_ResMessage.strMessage = (NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000529") + e.getMessage());
      E.m_Exception = e;
      throw E;
    }
  }

  public Vector filterDjHeaders(Integer p0, Integer p1, DjCondVO p2)
    throws BusinessException
  {
    if ((null == p0) || (null == p1) || (p0.intValue() < 0) || (p1.intValue() < 0))
      throw new IllegalArgumentException(); Vector ret;
    try {
      DJZBDAO dmo = new DJZBDAO();
      ret = dmo.queryDjLb_djcond(p0, p1, p2);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }return ret;
  }

  public Vector filterDjHeadersQry(Integer p0, Integer p1, DjCondVO p2)
    throws BusinessException
  {
    if ((null == p0) || (null == p1) || (p0.intValue() < 0) || (p1.intValue() < 0))
      throw new IllegalArgumentException(); Vector ret;
    try {
      DJZBDAO dmo = new DJZBDAO();
      ret = dmo.queryDjLbQ_djcond(p0, p1, p2);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }return ret;
  }

  public Vector filterDjHeadersSS(Integer p0, Integer p1, DjCondVO p2) throws BusinessException {
    if ((null == p0) || (null == p1) || (p0.intValue() < 0) || (p1.intValue() < 0))
      throw new IllegalArgumentException(); Vector ret;
    try {
      DJZBDAO dmo = new DJZBDAO();
      ret = dmo.queryDjLbP_SS(p0, p1, p2);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }return ret;
  }
  public Vector filterDjHeadersSS4(Integer p0, Integer p1, DjCondVO p2) throws BusinessException {
    if ((null == p0) || (null == p1) || (p0.intValue() < 0) || (p1.intValue() < 0))
      throw new IllegalArgumentException(); Vector ret;
    try {
      DJZBDAO dmo = new DJZBDAO();
      ret = dmo.queryDjLbP_SS_4(p0, p1, p2, p2.isCHz);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
    return ret;
  }
  public Vector filterDjHeadersWSZZ(Integer p0, Integer p1, DjCondVO p2) throws BusinessException {
    if ((null == p0) || (null == p1) || (p0.intValue() < 0) || (p1.intValue() < 0))
      throw new IllegalArgumentException(); Vector ret;
    try {
      DJZBDAO dmo = new DJZBDAO();
      ret = dmo.queryDjLbQ_Wszz(p0, p1, p2.m_NorCondVos, p2.m_DefCondVos, p2.isCHz, p2.refs, p2.pk_corp[0], p2.getSqlWhere());
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }
    return ret;
  }
  public Vector filterDjHeadersYHQR(Integer p0, Integer p1, DjCondVO p2) throws BusinessException {
    if ((null == p0) || (null == p1) || (p0.intValue() < 0) || (p1.intValue() < 0))
      throw new BusinessShowException(new IllegalArgumentException()); Vector ret;
    try {
      DJZBDAO dmo = new DJZBDAO();
      ret = dmo.queryDjLb_Yhrq(p0, p1, p2);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessShowException(e.getMessage());
    }return ret;
  }

  public void updateArapBillByFTSReceiver(IFTSReceiverVO vo)
    throws BusinessException
  {
    DJZBDAO dmo = null;
    DJZBVO zbvo = null;
    DJZBItemVO[] items = null;
    DJZBHeaderVO headvo = null;
    try
    {
      dmo = new DJZBDAO();
      zbvo = dmo.findByPrimaryKey(vo.getArapBillPk());
      zbvo.setm_OldVO((DJZBVO)zbvo.clone());
      headvo = (DJZBHeaderVO)zbvo.getParentVO();
      lockDJ(headvo.getVouchid());
      items = (DJZBItemVO[])(DJZBItemVO[])zbvo.getChildrenVO();
      headvo = (DJZBHeaderVO)zbvo.getParentVO();
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }
    if (null == headvo) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006", "UPP2006-000371"));
    }
    try
    {
      if ("FTS_RECEIVE_SAVE".equals(vo.getActName())) {
        if ((DJZBVOConsts.FTS_RECEIVER_GOING.equals(headvo.getZzzt())) || (DJZBVOConsts.FTS_RECEIVER_SUCCESS.equals(headvo.getZzzt()))) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006", "UPT2006-v51-000020"));
        }
        headvo.setZzzt(DJZBVOConsts.FTS_RECEIVER_GOING);
        headvo.setDjzt(new Integer(-99));
        headvo.setJszxzf(new Integer(10));
        dmo.updateHeader(headvo);
        return;
      }
      KeyLock.dynamicLock(headvo.getPrimaryKey());

      if ("FTS_RECEIVE_SUCCESS".equals(vo.getActName())) {
        if (null == vo.getBodyMoney()) {
          Log.getInstance(getClass()).debug("收款金额无效 ");
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-001091"));
        }
        Map originMap = getMapByDJZBItemVOs(items);
        double sum = 0.0D;
        Iterator it = vo.getBodyMoney().keySet().iterator();
        while (it.hasNext())
        {
          String fbpk = (String)it.next();
          UFDouble mny = (UFDouble)vo.getBodyMoney().get(fbpk);
          DJZBItemVO item = (DJZBItemVO)originMap.get(fbpk);
          if ((null == mny) || (mny.doubleValue() < 0.0D)) {
            Log.getInstance(getClass()).debug("收款金额无效 ");
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-001091"));
          }
          if (mny.doubleValue() == 0.0D) {
            item.setStatus(3);
            continue;
          }

          if (!mny.equals(item.getDfybje())) {
            item.setDfybje(mny);

            item = ArapDjCalculator.getInstance().calculateVO(item, "dfybje", headvo.getDjrq().toString(), headvo.getDjdl(), getPriorStrategy(headvo.getDwbm(), headvo.getDjdl(), headvo.getPzglh().intValue()));
          }
          item.setStatus(1);
          item.setChildrenVO(null);
          sum += mny.doubleValue();
        }

        if (sum == 0.0D) {
          headvo.setZzzt(DJZBVOConsts.FTS_RECEIVER_FAILURE);
          dmo.updateHeader(headvo);
        } else {
          headvo.setDjzt(new Integer(-99));
          headvo.setZzzt(DJZBVOConsts.FTS_RECEIVER_SUCCESS);
          headvo.setJszxzf(new Integer(10));

          sumBtoH(zbvo);
          try {
            PfUtilBO pfbo = new PfUtilBO();
            pfbo.processAction("SAVE", headvo.getDjlxbm(), headvo.getDjrq().toString(), null, zbvo, null);
          }
          catch (Exception e1) {
            Log.getInstance(getClass()).error(e1.getMessage(), e1);
            throw new BusinessException(e1.getMessage(), e1);
          }
        }

      }
      else if ("FTS_RECEIVE_CANCELSUCCESS".equals(vo.getActName()))
      {
        if (!canChangeFTSReceiver(vo.getArapBillPk())) {
          Log.getInstance(getClass()).debug("接口动作名称无效");
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-001090"));
        }
        headvo.setZzzt(DJZBVOConsts.FTS_RECEIVER_GOING);
        headvo.setDjzt(new Integer(-99));
        headvo.setJszxzf(new Integer(10));
        updateDjStatusToUpdate(zbvo);
        editTempDj(zbvo, vo, new UFBoolean(true));
      }
      else if ("FTS_RECEIVE_FAILURE".equals(vo.getActName()))
      {
        headvo.setZzzt(DJZBVOConsts.FTS_RECEIVER_FAILURE);
        headvo.setDjzt(new Integer(-99));

        dmo.updateHeader(headvo);
      }
      else
      {
        Log.getInstance(getClass()).debug("接口动作名称无效");
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006", "UPP2006-000372"));
      }
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }
  }

  public static int getSysFlag(int Syscode) {
    if (Syscode == -9)
      Syscode = 0;
    else if (Syscode == -99)
      Syscode = 1;
    else if (Syscode == -999)
      Syscode = 2;
    return Syscode;
  }
  private int[] getPriorStrategy(String corp, String djdl, int syscode) throws BusinessException {
    syscode = getSysFlag(syscode);
    String key = "";
    if (syscode == 0)
    {
      key = "AR21";
    } else if (syscode == 1)
    {
      key = "AP21";
    }
    else if (("ys".equalsIgnoreCase(djdl)) || ("sk".equalsIgnoreCase(djdl)) || ("sj".equalsIgnoreCase(djdl)))
      key = "EC22";
    else if (("yf".equalsIgnoreCase(djdl)) || ("fk".equalsIgnoreCase(djdl)) || ("fj".equalsIgnoreCase(djdl)))
      key = "EC23";
    else {
      key = null;
    }
    String strategy = SysInit.getParaString(corp, key);
    if ("含税价格优先".equals(strategy)) {
      return new int[] { 5 };
    }
    return new int[] { 6 };
  }

  private UFDouble sumB(String Bkey, DJZBItemVO[] items)
  {
    double subValue = 0.0D;

    for (int i = 0; i < items.length; i++) {
      if (items[i].getStatus() == 3)
        continue;
      try {
        subValue += (items[i].getAttributeValue(Bkey) == null ? 0.0D : ((UFDouble)items[i].getAttributeValue(Bkey)).doubleValue());
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        throw new BusinessRuntimeException(e.getMessage(), e);
      }
    }

    return new UFDouble(subValue);
  }

  private void sumBtoH(DJZBVO djvo) {
    DJZBHeaderVO head = (DJZBHeaderVO)djvo.getParentVO();
    DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])djvo.getChildrenVO();
    String strDjdl = head.getDjdl();
    UFDouble ybje = null;
    UFDouble bbje = null;
    UFDouble fbje = null;
    if ((strDjdl.equals("hj")) || (strDjdl.equals("sk")) || (strDjdl.equals("sj")) || (strDjdl.equals("ws")))
    {
      ybje = sumB("dfybje", items);
      bbje = sumB("dfbbje", items);
      fbje = sumB("dffbje", items);
    }

    head.setYbje(ybje);
    head.setBbje(bbje);
    head.setFbje(fbje);
  }

  public boolean canChangeFTSReceiver(String billpk)
    throws BusinessException
  {
    DJZBHeaderVO headvo;
    try
    {
      DJZBDAO dmo = new DJZBDAO();
      headvo = dmo.findHeaderByPrimaryKey(billpk);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage());
    }

    return (headvo.getShr() == null) || (headvo.getShr().length() <= 0);
  }

  private Map getMapByDJZBItemVOs(DJZBItemVO[] items)
  {
    Map ret = new HashMap();
    int i = 0; for (int size = items.length; i < size; i++)
      ret.put(items[i].getPrimaryKey(), items[i]);
    return ret;
  }
  public DJZBVO saveTempDj(DJZBVO djzbvo) throws BusinessException {
    try {
      djzbvo.setParam_Ext_Save();
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException("nc.bs.ep.dj.DJZBBO.saveTempDj(DJZBVO) setParam_Ext_Save:" + e);
    }
    DJZBVO dJZB = doPayterm(djzbvo);
    try {
      DJZBHeaderVO head = (DJZBHeaderVO)dJZB.getParentVO();
      DJZBItemVO[] items = null;
      items = (DJZBItemVO[])(DJZBItemVO[])dJZB.getChildrenVO();
      if ((head.getHzbz() != null) && (head.getHzbz().trim().length() > 0)) {
        head.setLybz(new Integer(0));
      }
      head.setDjbh(getDjbh(dJZB));
      if ((items != null) && ((head.getDjdl().equals("ys")) || (head.getDjdl().equals("sk")))) {
        for (int i = 0; i < items.length; i++) {
          if ((items[i] == null) || (null == items[i].getWldx()) || (items[i].getWldx().intValue() != 0) || (
            (items[i].getOrdercusmandoc() != null) && (items[i].getOrdercusmandoc().trim().length() >= 1)))
            continue;
          items[i].setOrdercusmandoc(items[i].getKsbm_cl());
        }
      }

      DJZBDAO dmo = new DJZBDAO();

      DJZBVO vo = dmo.insert(dJZB);
      String sts = dmo.getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
      if (sts.trim().length() >= 3)
      {
        head.setTs(new UFDateTime(sts));
      }
      return vo;
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException("DJZBBO::saveTempDj(dJZB) Exception!", e);
    }
  }

  public DJZBVO editTempDj(DJZBVO djzbvo, UFBoolean isFromSave)
    throws BusinessException
  {
    return editTempDj(djzbvo, null, isFromSave);
  }

  private DJZBVO editTempDj(DJZBVO slimDJZBVO, IFTSReceiverVO vo, UFBoolean isFromSave)
    throws BusinessException
  {
    DJZBVO djzbvo = slimDJZBVO;
    DJZBVO oldDZJBVO = slimDJZBVO.getm_OldVO();
    if (DJZBHeaderVO.SLIMED.equalsIgnoreCase(((DJZBHeaderVO)slimDJZBVO.getParentVO()).IsSlim))
    {
      try {
        if ((null == slimDJZBVO) || (null == slimDJZBVO.getParentVO()) || (null == slimDJZBVO.getParentVO().getPrimaryKey())) {
          throw new BusinessShowException("nc.bs.ep.dj.DJZBBO.editTempDj(DJZBVO) editTempDj:");
        }
        if ("ss".equals(((DJZBHeaderVO)slimDJZBVO.getParentVO()).getDjdl()))
          oldDZJBVO = findByPrimaryKey_SS(slimDJZBVO.getParentVO().getPrimaryKey());
        else
          oldDZJBVO = findByPrimaryKey(slimDJZBVO.getParentVO().getPrimaryKey());
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        throw new BusinessShowException("nc.bs.ep.dj.DJZBBO.editTempDj(DJZBVO) editTempDj:" + e);
      }

      if (null == oldDZJBVO)
      {
        throw new BusinessShowException("nc.bs.ep.dj.DJZBBO.editTempDj(DJZBVO) editTempDj:");
      }
      DJZBVOTreator.revertDJZBVO(oldDZJBVO, slimDJZBVO);
      djzbvo = slimDJZBVO;
      slimDJZBVO = (DJZBVO)djzbvo.clone();
      djzbvo.setm_OldVO(oldDZJBVO);
      ((DJZBHeaderVO)oldDZJBVO.getParentVO()).setTs(((DJZBHeaderVO)djzbvo.getParentVO()).getts());
    }
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();
    lockDJ(head.getVouchid());
    ApplayBillBO aBo = new ApplayBillBO();

    DJZBItemVO[] items = null;
    if (djzbvo.getChildrenVO() != null) {
      items = (DJZBItemVO[])(DJZBItemVO[])djzbvo.getChildrenVO();
    }
    long t1 = System.currentTimeMillis();
    ArapExtInfRunBO extbo = new ArapExtInfRunBO();
    BusiTransVO[] busitransvos = extbo.initBusiTrans("edittemp", head.getPzglh());
    if (!isFromSave.booleanValue()) {
      beforeTempEditInf(busitransvos, djzbvo);

      Log.getInstance(getClass()).debug("edittemp外接口修改单据前动作前所用时间:" + (System.currentTimeMillis() - t1));
    } else {
      head.setFromSaveTotemporarily(UFBoolean.TRUE);
      ((DJZBHeaderVO)oldDZJBVO.getParentVO()).setFromSaveTotemporarily(UFBoolean.TRUE);
    }

    String tablename = "arap_djzb";
    try {
      oldDZJBVO.setParam_Ext_Save();
      ARAPDjBSUtil.supplementXTFlag(oldDZJBVO);
      djzbvo.setParam_Ext_Save();
      ARAPDjBSUtil.supplementXTFlag(djzbvo);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage());
    }

    try
    {
      DJZBDAO dmo = new DJZBDAO();

      if (isFromSave.booleanValue()) {
        try
        {
          aBo.befUnQr(oldDZJBVO);
        } catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          throw new BusinessException(e.getMessage(), e);
        }
      }
      else
        aBo.isDistributes(djzbvo);
      if (isFromSave.booleanValue()) {
        try {
          aBo.afterUnQr(oldDZJBVO);
        } catch (Exception e) {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          throw new BusinessException(e.getMessage(), e);
        }
      }

      if (null != vo) {
        Map originMap = getMapByDJZBItemVOs(items);
        Iterator it = vo.getBodyMoney().keySet().iterator();
        while (it.hasNext())
        {
          String fbpk = (String)it.next();
          UFDouble mny = (UFDouble)vo.getBodyMoney().get(fbpk);
          DJZBItemVO item = (DJZBItemVO)originMap.get(fbpk);

          if (!mny.equals(item.getDfybje())) {
            item.setDfybje(mny);
            item = changeBodyByYbje(mny, item, head);
          }
          item.setStatus(1);
          item.setChildrenVO(null);
        }
        sumBtoH(djzbvo);
      }

      djzbvo = dmo.update(doPayterm(djzbvo));
      head.setTs(new UFDateTime(dmo.getTsByPrimaryKey(head.getVouchid(), tablename)));
      head.setFromSaveTotemporarily(UFBoolean.FALSE);
      djzbvo.setParentVO(head);

      if (!isFromSave.booleanValue()) {
        long t2 = System.currentTimeMillis();
        afterTempEditInf(busitransvos, djzbvo);
        Log.getInstance(getClass()).debug("edittemp外接口修改单据前动作后所用时间:" + (System.currentTimeMillis() - t2));
      }
      String ts = dmo.getTsByPrimaryKey(head.getVouchid(), tablename);
      if (ts != null) {
        head.setTs(new UFDateTime(ts));
      }
      djzbvo.setParentVO(head);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage());
    }

    return djzbvo;
  }

  public DJZBVO saveFromTemporaryDj(DJZBVO djzbvo)
    throws BusinessException
  {
    try
    {
      ARAPDjBSUtil.supplementXTFlag(djzbvo);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException("nc.bs.ep.dj.DJZBBO.saveFromTemporaryDj(DJZBVO) setParam_Ext_Save:" + e);
    }
    DJZBHeaderVO head = (DJZBHeaderVO)djzbvo.getParentVO();

    long t1 = System.currentTimeMillis();
    ArapExtInfRunBO extbo = new ArapExtInfRunBO();

    BusiTransVO[] busitransvos = null;

    busitransvos = extbo.initBusiTrans("edittemp", head.getPzglh());
    beforeTempEditInf(busitransvos, djzbvo);

    Log.getInstance(getClass()).debug("edittemp外接口新增单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

    djzbvo = insertOneDj(doPayterm(djzbvo), false);

    long t2 = System.currentTimeMillis();

    afterTempEditInf(busitransvos, djzbvo);

    Log.getInstance(getClass()).debug("edittemp外接口新增单据前动作后所用时间:" + (System.currentTimeMillis() - t2));
    return djzbvo;
  }

  private void updateDjStatusToUpdate(DJZBVO vo) {
    if (null == vo)
      return;
    DJZBItemVO[] items = (DJZBItemVO[])(DJZBItemVO[])vo.getChildrenVO();
    int i = 0; for (int size = items.length; i < size; i++) {
      items[i].setStatus(1);
      items[i].setChildrenVO(null);
    }
  }

  private DJZBVO beforeTempEditInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubEditTemporarilyInterface)busitransvos[i].getInfClass()).beforeEditTemporarilyAct(dJZB.getm_OldVO(), dJZB);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          Log.getInstance(getClass()).debug(busitransvos[i].getSystemname() + busitransvos[i].getNote() + "\n" + e);

          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessException(strerr);
        }
      }
    }

    return dJZB;
  }

  private DJZBVO afterTempEditInf(BusiTransVO[] busitransvos, DJZBVO dJZB)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubEditTemporarilyInterface)busitransvos[i].getInfClass()).afterEditTemporarilyAct(dJZB.getm_OldVO(), dJZB);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          Log.getInstance(getClass()).debug(busitransvos[i].getSystemname() + busitransvos[i].getNote() + "\n" + e);

          String strerr = busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage();
          throw new BusinessException(strerr);
        }
      }
    }

    return dJZB;
  }
  public LightBillVO[] getForwardBills(String curBillType, String curBillID, String forwardBillType) {
    try {
      return new DJZBDAO().getForwardBills(curBillType, curBillID, forwardBillType);
    } catch (DAOException e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessRuntimeException(e.getMessage(), e);
    }
  }

  private DJZBItemVO changeBodyByYbje(UFDouble ybje, DJZBItemVO item, DJZBHeaderVO head)
    throws Exception
  {
    int Syscode = 0;
    if (head.getPzglh().intValue() == -9)
      Syscode = 0;
    else if (head.getPzglh().intValue() == -99)
      Syscode = 1;
    else if (head.getPzglh().intValue() == -999)
      Syscode = 2;
    String key = "";
    if (Syscode == 0)
    {
      key = "AR21";
    } else if (Syscode == 1)
    {
      key = "AP21";
    }
    else if (("ys".equalsIgnoreCase(head.getDjdl())) || ("sk".equalsIgnoreCase(head.getDjdl())) || ("sj".equalsIgnoreCase(head.getDjdl())))
      key = "EC22";
    else if (("yf".equalsIgnoreCase(head.getDjdl())) || ("fk".equalsIgnoreCase(head.getDjdl())) || ("fj".equalsIgnoreCase(head.getDjdl()))) {
      key = "EC23";
    }
    try
    {
      int[] proi = new int[1];
      if ("含税价格优先".equals(SysInit.getParaString(head.getDwbm(), key)))
        proi[0] = 5;
      else {
        proi[0] = 6;
      }
      return ArapDjCalculator.getInstance().calculateVO(item, "dfybje", head.getDjrq().toString(), head.getDjdl(), proi);
    } catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }
  }

  public DJZBVO[] getDJByXXID(String key, String value) throws BusinessException
  {
    try
    {
      return new DJZBDAO().getDJByXXID(key, value);
    }
    catch (Exception e) {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }
  }

  public void lockDJ(String pk) throws BusinessException {
    String lock = null;
    try {
      lock = KeyLock.dynamicLock(pk);
    } catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006", "UPT2006-v51-000001", null, new String[] { "" }));
    }
    if (lock != null)
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("2006", "UPT2006-v51-000001", null, new String[] { lock }));
  }
}