package nc.bs.mo.mo1020;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import nc.bs.bd.b30.BatchcodeRuleBO;
import nc.bs.er.er2010.JxdBO;
import nc.bs.framework.common.NCLocator;
import nc.bs.me.me3030.YieldInfoBO;
import nc.bs.me.me3030.YieldInfoDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.mm.pub.DdDMO;
import nc.bs.mm.pub.KzcsDMO;
import nc.bs.mm.pub.MMBusinessObject;
import nc.bs.mm.pub.pub1020.DisassembleDMO;
import nc.bs.mm.pub.pub1025.MMLockBO;
import nc.bs.mm.pub.pub1030.SCDDAPT;
import nc.bs.mm.pub.pub1060.ReeditLogDMO;
import nc.bs.mo.mo2010.PickmBO;
import nc.bs.mo.mo2010.PickmDMO;
import nc.bs.pd.pd3030.RtDMO;
import nc.bs.pd.pd4010.v5.BomDMO;
import nc.bs.pub.billcodemanage.BillcodeRuleBO;
import nc.bs.pub.lock.LockBO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.pub.plantcalendar.PlantCalendarBO;
import nc.bs.sf.sf1020.KhwlszDMO;
import nc.bs.sf.sf2020.PgdDMO;
import nc.bs.sm.createcorp.CreatecorpDMO;
import nc.itf.scm.to.service.IOuter;
import nc.itf.so.service.ISOToPUTO_BillConvertDMO;
import nc.itf.uap.bd.prayvsbusitype.IPrayvsBusiQry;
import nc.itf.uap.bd.produce.IProduceQry;
import nc.vo.bd.b431.InvbasdocVO;
import nc.vo.bd.b431.ProduceVO;
import nc.vo.bd.fd.DdVO;
import nc.vo.mm.pub.FreeItemVO;
import nc.vo.mm.pub.FreeItemVOTookKit;
import nc.vo.mm.pub.KzcsVO;
import nc.vo.mm.pub.MMBusinessException;
import nc.vo.mm.pub.pub1020.BomHeaderVO;
import nc.vo.mm.pub.pub1020.BomItemVO;
import nc.vo.mm.pub.pub1020.BomVO;
import nc.vo.mm.pub.pub1020.ProduceCoreVO;
import nc.vo.mm.pub.pub1020.RtBItemVO;
import nc.vo.mm.pub.pub1025.PoVO;
import nc.vo.mm.pub.pub1030.MOSourceVO;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mm.pub.pub1030.MoItemVO;
import nc.vo.mm.pub.pub1030.MoVO;
import nc.vo.mm.pub.pub1030.OrsVO;
import nc.vo.mm.pub.pub1030.PickmHeaderVO;
import nc.vo.mm.pub.pub1030.PickmVO;
import nc.vo.mo.mo1020.ProductVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.rewrite.ParaRewriteVO;
import nc.vo.sf.sf1020.KhwlszVO;

public class MoBO extends MMBusinessObject
{
  public void createMO(MoVO[] mos)
    throws MMBusinessException
  {
    if (mos.length < 1) {
      return;
    }

    try
    {
      for (int i = 0; i < mos.length; i++)
      {
        mos[i].getHeadVO().setFjlid(null);
        createMO(mos[i], null, new UFBoolean(false));

        backwrite(new MoVO[] { mos[i] }, new UFDouble(0), mos[0].getHeadVO().getZdrid(), new UFDateTime(System.currentTimeMillis()));
      }

    }
    catch (Exception ex)
    {
      if ((ex instanceof MMBusinessException)) {
        ex.printStackTrace();
        throw ((MMBusinessException)ex);
      }
      ex.printStackTrace();
      throw new MMBusinessException(getstrbyid("UPP50081020-000001"), ex);
    }
  }

  public void ejbCreate()
  {
  }

  public ArrayList createMO(MoVO mo, ArrayList polist, UFBoolean isPlatForm)
    throws MMBusinessException
  {
    PoVO[] pvos = null;
    if (polist != null) {
      pvos = new PoVO[polist.size()];
      for (int i = 0; i < polist.size(); i++) {
        pvos[i] = ((PoVO)polist.get(i));
      }
    }
    MoHeaderVO mhead = mo.getHeadVO();

    if (isNull(mhead.getFjlid())) {
      ProductVO product = getProduct(mhead.getPk_produce(), mhead.getJhwgsl(), mhead.getFreeitemVO());

      mhead.setJldwid(product.getMeasPK());
      if (!isNull(product.getAssMeasPK())) {
        mhead.setFjlid(product.getAssMeasPK());

        mhead.setFjlhsl(product.getAssHsl());
        if ((mhead.getFjlhsl() != null) && (!mhead.getFjlhsl().equals(new UFDouble(0))))
        {
          mhead.setFjhsl(mhead.getJhwgsl().div(mhead.getFjlhsl()));
        }
      }

    }

    if (isNull(mhead.getFjlid())) {
      mhead.setFjlhsl(null);
      mhead.setFjhsl(null);
    }

    ArrayList anOutList = null;
    try
    {
      long time1 = System.currentTimeMillis();
      anOutList = insertCompleteMO(mo, pvos);
      long time2 = System.currentTimeMillis();
      long timeinsert = time2 - time1;
      System.out.print("zhanj insertcompletMO");
      System.out.print(timeinsert);

      if ((((MoHeaderVO)mo.getParentVO()).getDdlx().intValue() == 8) && (((MoHeaderVO)mo.getParentVO()).getLylx().intValue() == 11))
      {
        PgdDMO pgddmo = new PgdDMO();
        pgddmo.updateZscsl(((MoHeaderVO)mo.getParentVO()).getLyfbid());
      }

      KzcsVO csVO = null;
      try {
        csVO = new KzcsDMO().queryAllKzcs(mhead.getPk_corp(), mhead.getGcbm(), "sfgxbl");
      }
      catch (Exception ex) {
        reportException(ex);
        throw new BusinessException("UPP50081020-000278");
      }
      if (!isNull(csVO)) {
        if (csVO.getCsz2().trim().equalsIgnoreCase("Y"))
        {
          createSubOrderByRoute(mhead);
        }
        else
          createSubOrderByBom(mhead);
      }
    }
    catch (Exception ex)
    {
      reportException(ex);
      throw new MMBusinessException(getstrbyid("UPP50081020-000001"), ex);
    }
    finally
    {
    }

    if (!isPlatForm.booleanValue()) {
      PickmBO pkbo = null;
      try {
        PickmVO pick = (PickmVO)PfUtilTools.runChangeData("A2", "A3", mo);

        pick.setChildrenVO(null);

        if (mo.getHeadVO().getJhkgrq().after(mo.getHeadVO().getBusiDate()))
        {
          ((PickmHeaderVO)pick.getParentVO()).setLogDate(mo.getHeadVO().getJhkgrq().toString());
        }
        else {
          ((PickmHeaderVO)pick.getParentVO()).setLogDate(mo.getHeadVO().getBusiDate().toString());
        }

        pkbo = new PickmBO();

        long time3 = System.currentTimeMillis();
        Integer successflag = pkbo.savePickm(pick);
        long time4 = System.currentTimeMillis();
        long timesavepick = time4 - time3;
        System.out.print("zhanj 创建备料计划");
        System.out.print(timesavepick);
        if (successflag.intValue() == 1) {
          throw new BusinessException(getstrbyid("UPP50081020-000002"));
        }

        if (successflag.intValue() != 0)
          throw new BusinessException(getstrbyid("UPP50081020-000003"));
      }
      catch (Exception ex)
      {
        reportException(ex);
        if ((ex instanceof MMBusinessException)) {
          throw ((MMBusinessException)ex);
        }
        throw new MMBusinessException(getstrbyid("UPP50081020-000004"), ex);
      }

    }

    MoHeaderVO mohead = null;
    try {
      MoDMO dmo = new MoDMO();
      mohead = dmo.findHeaderByPrimaryKey(String.valueOf(anOutList.get(0)));
    }
    catch (Exception ex)
    {
      reportException(ex);
      if ((ex instanceof MMBusinessException)) {
        throw ((MMBusinessException)ex);
      }
      throw new MMBusinessException("find mohead wrong", ex);
    }

    anOutList.add(mohead);
    return anOutList;
  }

  public PickmVO findClosablePickm(String pk_moid)
    throws MMBusinessException
  {
    try
    {
      PickmDMO dmo = new PickmDMO();
      PickmHeaderVO[] pkheaders = dmo.findHeaderByLyid(new Integer(1), pk_moid);

      if ((pkheaders == null) || (pkheaders.length == 0)) {
        return null;
      }
      PickmVO pick = null;
      for (int i = 0; (pick == null) && (i < pkheaders.length); i++) {
        if (pkheaders[i].getBljhlx().intValue() == 0) {
          pick = new PickmVO();
          pkheaders[i].setPk_pickmid(null);
          pick.setParentVO(pkheaders[i]);
          pick.setChildrenVO(dmo.findItemsForHeader(pkheaders[i].getPrimaryKey()));
        }
      }

      return pick;
    } catch (Exception ex) {
      reportException(ex);
    throw new MMBusinessException("", ex);
    }
  }

  public void freeArray(String[] pks, String userid)
    throws MMBusinessException
  {
    try
    {
      new LockBO().freePKArray(pks, userid, "mm_mo");
    }
    catch (Exception ex) {
      reportException(ex);
      throw new MMBusinessException(getstrbyid("UPP50081020-000005") + ex.getLocalizedMessage());
    }
    finally
    {
    }
  }

  public String[] getAvaVersion(String pk_corp, String gcbm, String wlbmid, String dx, UFDouble sl, FreeItemVO freevo)
    throws MMBusinessException
  {
    try
    {
      MoDMO dmo = new MoDMO();
      String[] vers = dmo.getAvaVersion(pk_corp, gcbm, wlbmid, dx, sl, freevo);

      return vers; } catch (Exception ex) {
    
    throw new MMBusinessException("", ex);
      }
  }

  public void revisemo(String tablename, String user, MoVO viewvo)
    throws MMBusinessException
  {
    try
    {
      if (viewvo == null)
        return;
      ReeditLogDMO dmo = new ReeditLogDMO();

      dmo.trunsferData2Log(tablename, user, viewvo.getHeadVO().getPk_moid());

      SCDDAPT scddapt = new SCDDAPT();
      scddapt.modifyATP(viewvo);
    }
    catch (Exception e) {
      reportException(e);

      throw new MMBusinessException("修订出错", e);
    }
  }

  private String getBillCode(String billtype, String pkcorp, BillCodeObjValueVO vo)
    throws MMBusinessException
  {
    try
    {
      String scddh = new BillcodeRuleBO().getBillCode(billtype, pkcorp, null, vo);
      String str1 = scddh;
      return str1;
    }
    catch (Exception ex)
    {
      reportException(ex);
      throw new MMBusinessException(getstrbyid("UPP50081020-000277"), ex);
    } finally {
    }
    //throw localObject;
  }

  public MoItemVO[] getItems(String pkmo)
    throws MMBusinessException
  {
    try
    {
      MoDMO dmo = new MoDMO();
      MoItemVO[] items = dmo.findItemsForHeader(pkmo);
      return items;
    } catch (Exception ex) {
      reportException(ex);
    throw new MMBusinessException("", ex);
    }
  }

  public KhwlszVO[] getKhwls(String pk_corp, String wlbmid, String ksid)
    throws MMBusinessException
  {
    try
    {
      KhwlszDMO dmo = new KhwlszDMO();
      KhwlszVO khwl = new KhwlszVO();
      khwl.setPk_corp(pk_corp);
      khwl.setWlbmid(wlbmid);
      khwl.setKsid(ksid);
      KhwlszVO[] khwls = dmo.queryByCondition(khwl);
      return khwls;
    } catch (Exception ex) {
      reportException(ex);
    throw new MMBusinessException("", ex);
    }
  }

  private String[] insert(MoVO mo)
    throws BusinessException
  {
    try
    {
      MoDMO dmo = new MoDMO();
      MoHeaderVO mhead = (MoHeaderVO)mo.getParentVO();

      if (isNull(mhead.getScddh())) {
        mhead.setScddh(getBillCode(new MOCodeCondition(mhead)));
      }

      checkVO(mo);

      boolean isexist = dmo.billExisted(mhead);
      if (isexist) {
        throw new BusinessException(getstrbyid("UPP50081020-000007"));
      }

      dmo.checkPchUnique(mhead);

      String[] keys = new String[3];
      keys[0] = dmo.insert(mo);

      if (keys[0] == null) {
        throw new BusinessException(getstrbyid("UPP50081020-000008"));
      }

      keys[1] = mhead.getScddh();

      keys[2] = dmo.queryBillHeadTs(new String[] { mo.getHeadVO().getPrimaryKey() })[0];

      return keys;
    } catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  private boolean isNull(Object o)
  {
    return (o == null) || (o.toString().trim().length() == 0);
  }

  public int lockArray(String[] pks, String userid)
    throws MMBusinessException, BusinessException
  {
    try
    {
      Integer reslock = new MMLockBO().lockPKs(pks, userid, "mm_mo", "pk_moid");
      if (reslock.intValue() == -1) {
        throw new BusinessException(getstrbyid("UPP50081020-000009"));
      }
      if (reslock.intValue() == 1) {
        throw new BusinessException(getstrbyid("UPP50081020-000010"));
      }
      int i = reslock.intValue() == 2 ? 1 : 0;
      return i;
    }
    catch (MMBusinessException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BusinessException(getstrbyid("UPP50081020-000011") + ex.getMessage());
    }
    finally
    {
    }

    //throw localObject;
  }

  public PraybillVO motopro(MoVO mo, String userid, UFDate busiDate)
    throws MMBusinessException
  {
    MoHeaderVO head = (MoHeaderVO)mo.getParentVO();

    PraybillVO pro = new PraybillVO();
    PraybillHeaderVO prhead = new PraybillHeaderVO();
    PraybillItemVO pritem = new PraybillItemVO();

    prhead.setStatus(2);
    pritem.setStatus(2);
    try {
      MoDMO dmo = new MoDMO();

      prhead.setPk_corp(head.getPk_corp());

      prhead.setDpraydate(busiDate);

      prhead.setCdeptid(head.getScbmid());

      prhead.setCpraypsn(head.getYwyid());

      prhead.setIpraysource(new Integer(1));

      boolean withm = dmo.isWithMateria(head.getPk_corp(), head.getGcbm(), head.getWlbmid());

      prhead.setIpraytype(withm ? new Integer(0) : new Integer(1));

      IPrayvsBusiQry prayvsBusiQry = (IPrayvsBusiQry)NCLocator.getInstance().lookup(IPrayvsBusiQry.class.getName());
      String busitype = prayvsBusiQry.getBusitypeProd(prhead.getIpraytype().intValue(), prhead.getPk_corp());
      if (busitype == null) {
        throw new BusinessException(getstrbyid("UPP50081020-000012"));
      }

      prhead.setCbiztype(busitype);

      prhead.setCoperator(userid);

      prhead.setIbillstatus(new Integer(0));

      BillCodeObjValueVO vo = new BillCodeObjValueVO();
      String[] names = { "库存组织", "部门", "业务类型" };
      String[] values = { head.getGcbm(), prhead.getCdeptid(), prhead.getCbiztype() };

      vo.setAttributeValue(names, values);
      String billcode = getBillCode("20", head.getPk_corp(), vo);
      prhead.setVpraycode(billcode);

      pritem.setPk_corp(head.getPk_corp());

      String pkman = dmo.getManIDFromProID(head.getPk_produce());
      pritem.setCmangid(pkman);

      pritem.setCbaseid(head.getWlbmid());

      pritem.setNpraynum(head.getJhwgsl());

      pritem.setCassistunit(head.getFjlid());
      pritem.setNassistnum(head.getFjhsl());

      pritem.setDdemanddate(head.getJhwgrq());

      pritem.setDsuggestdate(head.getJhkgrq());

      pritem.setCsourcebilltype(isNull(head.getXsddh()) ? "A2" : "30");

      pritem.setCsourcebillid(isNull(head.getXsddh()) ? head.getPrimaryKey() : head.getXsddh());

      pritem.setCupsourcebilltype("A2");

      pritem.setCupsourcebillid(head.getPrimaryKey());

      pritem.setVfree1(head.getZyx1());
      pritem.setVfree2(head.getZyx2());
      pritem.setVfree3(head.getZyx3());
      pritem.setVfree4(head.getZyx4());
      pritem.setVfree5(head.getZyx5());
      try {
        pritem.setVproducenum(head.getPch());
      } catch (Throwable th) {
        System.out.println(getstrbyid("UPP50081020-000006"));
      }

      pritem.setPk_reqstoorg(head.getGcbm());
      pritem.setPk_purcorp(head.getPk_corp());
      pritem.setPk_reqcorp(head.getPk_corp());
      pritem.setNpriceauditbill(new Integer(0));

      pro.setParentVO(prhead);
      pro.setChildrenVO(new PraybillItemVO[] { pritem });
      return pro; } catch (Exception ex) {
    
    throw new MMBusinessException(getstrbyid("UPP50081020-000013"), ex);
      }
  }

  private OrsVO[] organizeOrs(MoVO mvo, PoVO[] pvos)
  {
    if ((pvos == null) || (pvos.length == 0)) {
      return null;
    }
    OrsVO[] orss = new OrsVO[pvos.length];
    MoHeaderVO mhead = (MoHeaderVO)mvo.getParentVO();

    for (int i = 0; i < pvos.length; i++) {
      orss[i] = new OrsVO();
      orss[i].setPk_corp(pvos[i].getPk_corp());
      orss[i].setGcbm(pvos[i].getGcbm());
      orss[i].setPk_poid(pvos[i].getPrimaryKey());
      orss[i].setJhddh(pvos[i].getJhddh());
      orss[i].setLylx(new Integer(0));
      orss[i].setLyid(mhead.getPk_moid());
      orss[i].setLydjh(mhead.getScddh());
      orss[i].setJldwid(mhead.getJldwid());
      orss[i].setDr(new Integer(0));

      double d1 = pvos[i].getXqsl().doubleValue();
      double d2 = mhead.getJhwgsl().doubleValue();
      orss[i].setSl(d1 < d2 ? pvos[i].getXqsl() : mhead.getJhwgsl());
    }
    return orss;
  }

  public String[] saveMO(MoVO[] mvoArray, PickmVO[] pkvoArray)
    throws MMBusinessException
  {
    try
    {
      if ((mvoArray == null) || (mvoArray.length == 0) || (pkvoArray == null) || (pkvoArray.length == 0) || (mvoArray.length != pkvoArray.length))
      {
        throw new BusinessException(getstrbyid("UPP50081020-000217"));
      }

      PickmBO pkbo = new PickmBO();

      String[] keys = new String[mvoArray.length];

      for (int i = 0; i < mvoArray.length; i++)
      {
        mvoArray[i].getParentVO().setStatus(2);

        mvoArray[i].getHeadVO().setLylx(new Integer(2));
        if (isNull(mvoArray[i].getHeadVO().getLyid()))
          mvoArray[i].getHeadVO().setLyid("7523579");
        ArrayList idArray = insertCompleteMO(mvoArray[i], null);
        if ((idArray == null) || (idArray.size() < 1)) {
          throw new BusinessException(getstrbyid("UPP50081020-000015"));
        }

        keys[i] = ((String)idArray.get(0));

        ((PickmHeaderVO)pkvoArray[i].getParentVO()).setLylx(new Integer(1));

        ((PickmHeaderVO)pkvoArray[i].getParentVO()).setLyid(keys[i]);
        ((PickmHeaderVO)pkvoArray[i].getParentVO()).setLydjh(((MoHeaderVO)mvoArray[i].getParentVO()).getScddh());

        Integer successflag = pkbo.savePickm(pkvoArray[i]);
        if (successflag.intValue() == 1) {
          throw new BusinessException(getstrbyid("UPP50081020-000016"));
        }

        if (successflag.intValue() != 0) {
          throw new BusinessException(getstrbyid("UPP50081020-000017"));
        }

      }

      return keys;
    } catch (MMBusinessException re) {
      throw re;
    } catch (Exception ex) {
      reportException(ex);
    throw new MMBusinessException("", ex);
    }
  }

  public ArrayList update(MoVO mo)
    throws MMBusinessException
  {
    try
    {
      checkVO(mo);

      checkTimeStamp(new MoVO[] { mo });

      MoDMO dmo = new MoDMO();

      dmo.checkPchUnique(mo.getHeadVO());

      ArrayList retList = new ArrayList(2);
      retList.add(dmo.update(mo));
      if (retList.get(0) == null) {
        throw new BusinessException(getstrbyid("UPP50081020-000008"));
      }

      retList.add(mo.getHeadVO().getScddh());

      PgdDMO pgddmo = new PgdDMO();
      if ((((MoHeaderVO)mo.getParentVO()).getDdlx().intValue() == 8) && (((MoHeaderVO)mo.getParentVO()).getLylx().intValue() == 11))
      {
        pgddmo.updateZscsl(((MoHeaderVO)mo.getParentVO()).getLyid());
      }

      pgddmo.xgyxjByScddid(new String[] { ((MoHeaderVO)mo.getParentVO()).getPk_moid() }, ((MoHeaderVO)mo.getParentVO()).getYxj());

      retList.add(dmo.queryBillHeadTs(new String[] { mo.getHeadVO().getPk_moid() })[0]);

      return retList;
    } catch (Exception e) {
      reportException(e);
    throw new MMBusinessException("", e);
    }
  }

  private ArrayList updateAfterPut(MoVO mo)
    throws BusinessException
  {
    try
    {
      MoDMO dmo = new MoDMO();

      dmo.updateAfterPut(mo);

      ArrayList anOutList = new ArrayList(2);
      anOutList.add(mo.getHeadVO().getPrimaryKey());
      anOutList.add(dmo.queryBillHeadTs(new String[] { mo.getHeadVO().getPrimaryKey() })[0]);

      return anOutList; } catch (Exception e) {
    
    throw new BusinessException(e.getMessage());
      }
  }

  public void writeDczt(MoVO[] mos)
    throws MMBusinessException
  {
    String[] keys = new String[mos.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = ((MoHeaderVO)mos[i].getParentVO()).getPk_moid();
    }
    String userid = ((MoHeaderVO)mos[0].getParentVO()).getUserid();
    try {
      int lock = lockArray(keys, userid);
      if (lock==0) {
        throw new BusinessException(getstrbyid("UPP50081020-000018"));
      }

      MoDMO dmo = new MoDMO();

      for (int i = 0; i < mos.length; i++) {
        MoHeaderVO ihead = (MoHeaderVO)mos[i].getParentVO();
        if (ihead.getSfdc().booleanValue())
          dmo.writeDczt(ihead);
      }
    }
    catch (Exception ex) {
      reportException(ex);
      throw new MMBusinessException("", ex);
    } finally {
      freeArray(keys, userid);
    }
  }

  public void checkTimeStamp(MoVO[] voNowBills)
    throws BusinessException, MMBusinessException
  {
    if ((voNowBills == null) || (voNowBills.length == 0) || (voNowBills[0] == null) || (voNowBills[0].getParentVO() == null))
    {
      return;
    }
    try
    {
      boolean isAllNew = true;
      for (int i = 0; (isAllNew) && (i < voNowBills.length); i++) {
        if (voNowBills[i].getHeadVO().getStatus() != 2) {
          isAllNew = false;
        }
      }
      if (isAllNew) {
        return;
      }

      String[] sNowHeadTs = new String[voNowBills.length];
      for (int i = 0; i < voNowBills.length; i++) {
        if ((voNowBills[i].getHeadVO().getStatus() != 2) && (isNull(voNowBills[i].getHeadVO().getTs())))
        {
          throw new BusinessException(getstrbyid("UPP50081020-000019"));
        }

        sNowHeadTs[i] = voNowBills[i].getHeadVO().getTs();
      }

      String[] sNowHeadPKs = new String[voNowBills.length];
      for (int i = 0; i < voNowBills.length; i++) {
        sNowHeadPKs[i] = voNowBills[i].getHeadVO().getPk_moid();
      }

      MoDMO dmo = new MoDMO();
      String[] sOldHeadTs = dmo.queryBillHeadTs(sNowHeadPKs);

      if ((sOldHeadTs == null) || (sOldHeadTs.length == 0)) {
        throw new BusinessException(getstrbyid("UPP50081020-000020"));
      }

      for (int i = 0; i < sNowHeadPKs.length; i++)
        if (voNowBills[i].getHeadVO().getStatus() != 2) {
          if (isNull(sOldHeadTs[i])) {
            StringBuffer errmsg = new StringBuffer("");
            errmsg.append(getstrbyid("UPP50081020-000021"));

            errmsg.append(voNowBills[i].getHeadVO().getScddh());

            errmsg.append(getstrbyid("UPP50081020-000022"));

            throw new BusinessException(errmsg.toString());
          }if (!sNowHeadTs[i].equals(sOldHeadTs[i])) {
            StringBuffer errmsg = new StringBuffer("");
            errmsg.append(getstrbyid("UPP50081020-000021"));

            errmsg.append(voNowBills[i].getHeadVO().getScddh());

            errmsg.append(getstrbyid("UPP50081020-000023"));

            throw new BusinessException(errmsg.toString());
          }
        }
    }
    catch (Exception ex) {
      throw new BusinessException(ex.getMessage());
    }
  }

  private void checkVO(MoVO mo)
    throws BusinessException
  {
    try
    {
      mo.validate();
    } catch (Exception ex) {
      throw new BusinessException(ex.getMessage());
    }
  }

  public void delete(MoVO[] mvos)
    throws MMBusinessException
  {
    if ((mvos == null) || (mvos.length == 0)) {
      return;
    }

    String[] keys = new String[mvos.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = mvos[i].getHeadVO().getPk_moid();
    }

    String userid = mvos[0].getHeadVO().getUserid();
    try {
      MoDMO dmo = new MoDMO();

      int lock = lockArray(keys, userid);
      if (lock==0) {
        throw new BusinessException(getstrbyid("UPP50081020-000024"));
      }

      checkTimeStamp(mvos);

      SCDDAPT apt = new SCDDAPT();
      for (int i = 0; i < keys.length; i++) {
        MoVO atpmo = new MoVO();
        atpmo.setParentVO(dmo.findHeaderByPrimaryKey(keys[i]));
        apt.modifyATPWhenDeleteBill(atpmo);
      }

      dmo.delete(keys);
      PgdDMO pgddmo = new PgdDMO();
      for (int i = 0; i < mvos.length; i++)
      {
        if ((((MoHeaderVO)mvos[i].getParentVO()).getDdlx().intValue() == 8) && (((MoHeaderVO)mvos[i].getParentVO()).getLylx().intValue() == 11))
        {
          pgddmo.updateZscsl(((MoHeaderVO)mvos[i].getParentVO()).getLyid());
        }

        returnBillCode(new MOCodeCondition(mvos[i].getHeadVO()), mvos[i].getHeadVO().getScddh());
      }

      new SourceDMO().delete(keys);
      for (int i = 0; i < mvos.length; i++) {
        returnBillCode(new MOCodeCondition(mvos[i].getHeadVO()), mvos[i].getHeadVO().getScddh());
      }

      new OrsDMO().delete(keys);
    }
    catch (Exception ex)
    {
      reportException(ex);
      throw new MMBusinessException(getstrbyid("UPP50081020-000025"), ex);
    }
    finally
    {
      try {
        freeArray(keys, userid);
      }
      catch (Exception ex)
      {
      }
    }
  }

  public String[] batchRevise(MoVO[] vos, Integer[] remakeFlags)
    throws MMBusinessException
  {
    PfUtilBO pf = null;
    try
    {
      pf = new PfUtilBO();
      ArrayList alist = null;
      String[] strTss = new String[vos.length];
      for (int i = 0; i < vos.length; i++) {
        alist = (ArrayList)pf.processAction("UPDATE", "A2", vos[0].getHeadVO().getBusiDate().toString(), null, vos[i], remakeFlags[i]);

        strTss[i] = ((String)alist.get(2));
      }

      String[] i = strTss;
      return i;
    }
    catch (Exception ex)
    {
      if ((ex instanceof MMBusinessException))
        throw ((MMBusinessException)ex);
      throw new MMBusinessException(getstrbyid("UPP50081020-000026"), ex);
    }
    finally
    {
    }

    //throw localObject;
  }

  public void finishMOByPgd(String[] strMOPKs, String logDate, String time)
    throws MMBusinessException
  {
    MoVO[] mos = new MoVO[strMOPKs.length];
    try
    {
      MoDMO dmo = new MoDMO();
      for (int i = 0; i < strMOPKs.length; i++) {
        mos[i] = new MoVO();
        mos[i].setParentVO(dmo.findHeaderByPrimaryKey(strMOPKs[i]));
        mos[i].getHeadVO().setSjwgrq(new UFDate(logDate));
        mos[i].getHeadVO().setSjjssj(time);
      }
    } catch (Exception ex) {
      throw new MMBusinessException("", ex);
    }

    finish(mos);
  }

  public String[] finish(MoVO[] mos)
    throws MMBusinessException
  {
    if ((mos == null) || (mos.length == 0)) {
      return null;
    }

    String userid = ((MoHeaderVO)mos[0].getParentVO()).getUserid();
    String username = ((MoHeaderVO)mos[0].getParentVO()).getUserName();

    String[] keys = new String[mos.length];
    for (int i = 0; i < mos.length; i++) {
      keys[i] = ((MoHeaderVO)mos[i].getParentVO()).getPrimaryKey();
    }
    try
    {
      MoDMO dmo = new MoDMO();

      int lock = lockArray(keys, userid);
      if (lock==0) {
        throw new BusinessException(getstrbyid("UPP50081020-000027"));
      }

      checkTimeStamp(mos);

      MoDMO mdmo = new MoDMO();
      for (int i = 0; i < mos.length; i++) {
        MoHeaderVO mhead = (MoHeaderVO)mos[i].getParentVO();
        String ddzt = mdmo.getBillzt(mhead.getPk_moid());
        if (ddzt == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000028", null, new String[] { mhead.getScddh() }) + getstrbyid("UPP50081020-000029"));
        }

        if (!ddzt.equalsIgnoreCase("B")) {
          String ztshow = ddzt.equalsIgnoreCase("C") ? getstrbyid("UPP50081020-000031") : ddzt.equalsIgnoreCase("A") ? getstrbyid("UPP50081020-000030") : getstrbyid("UPP50081020-000032");

          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000028", null, new String[] { mhead.getScddh() }) + NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000033", null, new String[] { ztshow }));
        }

      }

      SCDDAPT apt = new SCDDAPT();
      for (int i = 0; i < mos.length; i++)
      {
        mos[i].getHeadVO().setZt("B");
        apt.modifyATPWhenCloseBill(mos[i]);
        mos[i].getHeadVO().setZt("C");
      }

      dmo.finish(mos);

      String[] timeStamps = dmo.queryBillHeadTs(keys);

      boolean startER = false;
      try {
        CreatecorpDMO crtdmo = new CreatecorpDMO();
        startER = crtdmo.isEnabled(mos[0].getHeadVO().getPk_corp(), "QC");
      }
      catch (Exception e) {
      }
      if (startER)
      {
        MoHeaderVO[] mheads = new MoHeaderVO[mos.length];
        for (int i = 0; i < mos.length; i++) {
          mheads[i] = mos[i].getHeadVO();
        }

        MOSourceVO[] srcs = new SourceDMO().querySource(mheads);

        if (srcs == null) {
          srcs = new MOSourceVO[0];
        }

        ArrayList alist = new ArrayList(srcs.length);
        if ((srcs != null) && (srcs.length > 0)) {
          for (int i = 0; i < srcs.length; i++) {
            if ((srcs[i] == null) || (srcs[i].getLylx() == null) || (srcs[i].getLylx().intValue() != 3))
              continue;
            alist.add(srcs[i].getLyid());
          }

        }

        if (alist.size() > 0) {
          String[] strIDs = new String[alist.size()];
          alist.toArray(strIDs);

          JxdBO jxdBO = new JxdBO();
          jxdBO.finishJxd(strIDs, userid);
        }
      }
      String[] mheads = timeStamps;
      return mheads;
    }
    catch (Exception ex)
    {
      reportException(ex);
      throw new MMBusinessException("", ex);
    }
    finally {
      try {
        freeArray(keys, userid); } catch (Exception ex) {
      }
    }
    //throw localObject;
  }

  private void returnBillCode(MOCodeCondition codeC, String scddh)
    throws BusinessException
  {
    try
    {
      BillCodeObjValueVO vo = codeC.toCodeObjValue();

      new BillcodeRuleBO().returnBillCodeOnDelete(codeC.getCorpID(), "A2", scddh, vo);
    }
    catch (MMBusinessException re)
    {
      throw re;
    } catch (Exception ex) {
      reportException(ex);
      throw new BusinessException(ex.getMessage());
    }
    finally
    {
    }
  }

  private String getstrbyid(String number)
  {
    return NCLangResOnserver.getInstance().getStrByID("50081020", number);
  }

  private String getBillCode(MOCodeCondition codeC)
    throws BusinessException
  {
    try
    {
      BillCodeObjValueVO vo = codeC.toCodeObjValue();

      String scddh = new BillcodeRuleBO().getBillCode("A2", codeC.getCorpID(), null, vo);
      String str1 = scddh;
      return str1;
    }
    catch (MMBusinessException re)
    {
      throw re;
    }
    catch (Exception ex)
    {
      reportException(ex);
      throw new BusinessException(ex.getMessage());
    }
    finally
    {
    }

    //throw localObject;
  }

  public DdVO[] getDds(String bname, String zdname)
    throws MMBusinessException
  {
    try
    {
      DdDMO dmo = new DdDMO();
      DdVO[] dds = dmo.customQuery(bname, zdname);
      return dds;
    } catch (Exception ex) {
      reportException(ex);
    throw new MMBusinessException(getstrbyid("UPP50081020-000034"), ex);
    }
  }

  public String[] getPickmStat(String[] sMOPKs)
    throws MMBusinessException
  {
    try
    {
      return new PickmDMO().getPickmztFromLyid(sMOPKs, new Integer(1));
    } catch (Exception ex) {
      reportException(ex);
    throw new MMBusinessException("", ex);
    }
  }

  public ProductVO getProduct(String pk_produce, UFDouble sl, FreeItemVO freevo)
    throws MMBusinessException
  {
    try
    {
      MoDMO dmo = new MoDMO();

      if (isNull(pk_produce)) {
        return null;
      }

      ProductVO product = dmo.getProduct(pk_produce);

      String[] res = dmo.getAvaVersion(product.getCorpPK(), product.getFactoryPK(), product.getInventoryPK(), "AB", sl, freevo);

      product.setBomver(res[1]);
      product.setRtver(res[3]);

      if (product.IsAssistUnit().booleanValue()) {
        DisassembleDMO disDMO = new DisassembleDMO();
        ProduceCoreVO coreVO = disDMO.getFjldwInfo(product.getInventoryPK());

        product.setAssMeasPK(coreVO.getFjldwid());
        product.setAssMeasName(coreVO.getFjldwmc());
        product.setAssHsl(coreVO.getMainmeasrate());
        product.setFixFlag(coreVO.getFixedflag());
      }
      return product;
    } catch (Exception ex) {
      ex.printStackTrace();
    throw new MMBusinessException(getstrbyid("UPP50081020-000035"), ex);
    }
  }

  public MOSourceVO[] getSources(String pk_moid)
    throws MMBusinessException
  {
    try
    {
      return new SourceDMO().queryByMO(pk_moid); } catch (Exception ex) {
    
    throw new MMBusinessException("", ex);
      }
  }

  public MoVO[] moput(MoVO[] mvs)
    throws MMBusinessException
  {
    if ((mvs == null) || (mvs.length == 0) || (mvs[0] == null) || (mvs[0].getParentVO() == null))
    {
      return null;
    }

    String userid = ((MoHeaderVO)mvs[0].getParentVO()).getUserid();

    String[] keys = new String[mvs.length];
    for (int i = 0; i < mvs.length; i++)
      keys[i] = ((MoHeaderVO)mvs[i].getParentVO()).getPrimaryKey();
    try
    {
      MoDMO dmo = new MoDMO();

      int lock = lockArray(keys, userid);
      if (lock==0) {
        throw new BusinessException(getstrbyid("UPP50081020-000036"));
      }

      long time1 = System.currentTimeMillis();
      checkTimeStamp(mvs);
      long time2 = System.currentTimeMillis();
      long timetime = time2 - time1;
      System.out.print("检查时间戳zhanj");
      System.out.print(timetime);

      for (int i = 0; i < mvs.length; i++) {
        MoHeaderVO mhead = (MoHeaderVO)mvs[i].getParentVO();
        String ddzt = dmo.getBillzt(mhead.getPk_moid());
        if (ddzt == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000028", null, new String[] { mhead.getScddh() }) + getstrbyid("UPP50081020-000029"));
        }

        if (!ddzt.equalsIgnoreCase("A")) {
          String ztshow = ddzt.equalsIgnoreCase("C") ? getstrbyid("UPP50081020-000038") : ddzt.equalsIgnoreCase("B") ? getstrbyid("UPP50081020-000037") : getstrbyid("UPP50081020-000039");

          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000028", null, new String[] { mhead.getScddh() }) + getstrbyid("UPP50081020-000040") + NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000033", null, new String[] { ztshow }));
        }

      }

      validateBcInfo(mvs);
      long time3 = System.currentTimeMillis();

      fillBatchCodes(mvs);
      long time4 = System.currentTimeMillis();
      long timebatch = time4 - time3;
      System.out.print("根据参数生成批次号zhanj");
      System.out.print(timebatch);

      long time5 = System.currentTimeMillis();
      dmo.moput(mvs);
      long time6 = System.currentTimeMillis();
      long timeput = time6 - time5;
      System.out.print("投放bsmo:zhanj");
      System.out.print(timeput);

      if (checkMEstart(mvs[0].getHeadVO().getPk_corp())) {
        MoHeaderVO[] headers = new MoHeaderVO[mvs.length];
        for (int i = 0; i < mvs.length; i++) {
          headers[i] = mvs[i].getHeadVO();
        }
        new YieldInfoDMO().createYieldInfoByMO(headers);
      }

      String[] tss = dmo.queryBillHeadTs(keys);
      for (int i = 0; i < tss.length; i++) {
        mvs[i].getHeadVO().setTs(tss[i]);
      }
      MoVO[] i = mvs;
      return i;
    }
    catch (MMBusinessException re)
    {
      throw re;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new MMBusinessException("", ex);
    }
    finally {
      try {
        freeArray(keys, userid); } catch (Exception ex) {
      }
    }
    //throw localObject;
  }

  private void validateBcInfo(MoVO[] vos)
    throws BusinessException
  {
    try
    {
      HashMap workShopMap = new HashMap(vos.length);

      for (int i = 0; i < vos.length; i++) {
        if ((!isNull(vos[i].getHeadVO().getBcid())) && (!isNull(vos[i].getHeadVO().getBzid())))
          continue;
        String strWorkShopID = vos[i].getHeadVO().getGzzxid();
        if ((strWorkShopID == null) || (workShopMap.containsKey(strWorkShopID)))
          continue;
        workShopMap.put(strWorkShopID, vos[i].getHeadVO().getGzzxmc());
      }

      if (workShopMap.size() > 0)
      {
        String[] keysWorkShop = new String[workShopMap.size()];
        workShopMap.keySet().toArray(keysWorkShop);
        UFBoolean[] flags = new MoDMO().ifSplitByWt(keysWorkShop);
        for (int i = 0; i < flags.length; i++) {
          if (flags[i].booleanValue()) {
            String gzzxmc = (String)workShopMap.get(keysWorkShop[i]);

            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000041", null, new String[] { gzzxmc }));
          }

        }

      }

    }
    catch (Exception e)
    {
      throw new BusinessException(e.getMessage());
    }
  }

  public String[] mounput(MoVO[] mvos)
    throws MMBusinessException
  {
    if ((mvos == null) || (mvos.length == 0)) {
      return null;
    }

    String[] keys = new String[mvos.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = mvos[i].getHeadVO().getPk_moid();
    }

    String userid = mvos[0].getHeadVO().getUserid();
    try
    {
      int lock = lockArray(keys, userid);
      if (lock==0) {
        throw new BusinessException(getstrbyid("UPP50081020-000036"));
      }

      checkTimeStamp(mvos);

      MoDMO dmo = new MoDMO();

      for (int i = 0; i < keys.length; i++) {
        if (dmo.havefl(keys[i])) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000043", null, new String[] { i + 1 + "" }) + getstrbyid("UPP50081020-000044"));
        }

        if (dmo.countWritem(keys[i]) > 0) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000045", null, new String[] { i + 1 + "" }) + getstrbyid("UPP50081020-000045"));
        }

        Integer res = dmo.checkPgdComplete(keys[i]);
        if (res.intValue() != 0) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000043", null, new String[] { i + 1 + "" }) + getstrbyid("UPP50081020-000046"));
        }

      }

      for (int i = 0; i < keys.length; i++) {
        String ddzt = dmo.getBillzt(keys[i]);
        if (ddzt == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000047", null, new String[] { i + 1 + "" }) + getstrbyid("UPP50081020-000048"));
        }

        if (!ddzt.equalsIgnoreCase("B")) {
          String ztshow = ddzt.equalsIgnoreCase("C") ? getstrbyid("UPP50081020-000031") : ddzt.equalsIgnoreCase("A") ? getstrbyid("UPP50081020-000030") : getstrbyid("UPP50081020-000032");

          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000047", null, new String[] { i + 1 + "" }) + ztshow + NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000033", null, new String[] { ztshow }));
        }

      }

      dmo.mounput(keys);

      new nc.bs.sf.sf2010.MoDMO().setDrForHeaders(keys);

      if (checkMEstart(mvos[0].getHeadVO().getPk_corp())) {
        new YieldInfoBO().deleteByMOArray(keys);
      }

      String[] i = dmo.queryBillHeadTs(keys);
      return i;
    }
    catch (MMBusinessException re)
    {
      throw re;
    } catch (Exception ex) {
      reportException(ex);
      throw new MMBusinessException("", ex);
    }
    finally {
      freeArray(keys, userid);
    }//throw localObject;
  }

  private MOSourceVO[] organizeSource(MoVO mvo, PoVO[] pvos)
  {
    if ((pvos == null) || (pvos.length == 0)) {
      return null;
    }
    MOSourceVO[] srcs = new MOSourceVO[pvos.length];
    MoHeaderVO mhead = (MoHeaderVO)mvo.getParentVO();

    for (int i = 0; i < pvos.length; i++) {
      srcs[i] = new MOSourceVO();
      srcs[i].setPk_corp(pvos[i].getPk_corp());
      srcs[i].setGcbm(pvos[i].getGcbm());

      srcs[i].setLylx(new Integer(0));
      srcs[i].setLyid(pvos[i].getPrimaryKey());
      srcs[i].setLyfbid(null);
      srcs[i].setLydjh(pvos[i].getJhddh());
      srcs[i].setJldwid(mhead.getJldwid());

      double d1 = pvos[i].getXqsl().doubleValue();
      double d2 = mhead.getJhwgsl().doubleValue();
      srcs[i].setSl(d1 < d2 ? pvos[i].getXqsl() : mhead.getJhwgsl());

      srcs[i].setScddid(mhead.getPrimaryKey());
    }

    return srcs;
  }

  public String[] over(MoVO[] mvos)
    throws MMBusinessException
  {
    if ((mvos == null) || (mvos.length == 0)) {
      return null;
    }

    String[] keys = new String[mvos.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = mvos[i].getHeadVO().getPk_moid();
    }

    String userid = mvos[0].getHeadVO().getUserid();
    try
    {
      int lock = lockArray(keys, userid);
      if (lock==0) {
        throw new BusinessException(getstrbyid("UPP50081020-000027"));
      }

      checkTimeStamp(mvos);

      MoDMO mdmo = new MoDMO();
      for (int i = 0; i < keys.length; i++) {
        String ddzt = mdmo.getBillzt(keys[i]);
        if (ddzt == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000043", null, new String[] { i + 1 + "" }) + getstrbyid("UPP50081020-000048"));
        }

        if (!ddzt.equalsIgnoreCase("C")) {
          String ztshow = ddzt.equalsIgnoreCase("B") ? getstrbyid("UPP50081020-000050") : ddzt.equalsIgnoreCase("A") ? getstrbyid("UPP50081020-000030") : getstrbyid("UPP50081020-000032");

          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000043", null, new String[] { i + 1 + "" }) + NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000033", null, new String[] { ztshow }));
        }

      }

      MoDMO dmo = new MoDMO();
      dmo.over(keys);

      String[] ddzt = dmo.queryBillHeadTs(keys);
      return ddzt;
    }
    catch (Exception ex)
    {
      reportException(ex);
      throw new MMBusinessException("", ex);
    }
    finally {
      freeArray(keys, userid);
    }//throw localObject;
  }

  public MoHeaderVO[] queryByWhere(String wherePart)
    throws MMBusinessException
  {
    try
    {
      MoDMO dmo = new MoDMO();
      MoHeaderVO[] heads = dmo.queryHeadByWhere(wherePart);
      if ((heads == null) || (heads.length == 0)) {
        return heads;
      }

      MOSourceVO[] srcs = new SourceDMO().querySource(heads);
      for (int i = 0; i < heads.length; i++) {
        heads[i].setLylx(srcs[i].getLylx());
        heads[i].setLyid(srcs[i].getLyid());
        heads[i].setLydjh(srcs[i].getLydjh());
        heads[i].setLyfbid(srcs[i].getLyfbid());
      }

      if ((heads != null) && (heads.length > 0)) {
        KhwlszDMO khwldmo = new KhwlszDMO();
        HashMap hmSpecial = new HashMap(heads.length);

        for (int i = 0; i < heads.length; i++) {
          if (isNull(heads[i].getKsid())) {
            continue;
          }
          String key = heads[i].getWlbmid() + heads[i].getKsid();
          if (hmSpecial.containsKey(key)) {
            heads[i].setKhwlVOs((KhwlszVO[])(KhwlszVO[])hmSpecial.get(key));
          }
          else
          {
            KhwlszVO khwl = new KhwlszVO();
            khwl.setPk_corp(heads[i].getPk_corp());
            khwl.setWlbmid(heads[i].getWlbmid());
            khwl.setKsid(heads[i].getKsid());

            KhwlszVO[] khwls = khwldmo.queryByCondition(khwl);
            hmSpecial.put(key, khwls);
            heads[i].setKhwlVOs(khwls);
          }
        }
      }
      return heads;
    } catch (Exception e) {
      reportException(e);
    throw new MMBusinessException("", e);
    }
  }

  public String[] unfinish(MoVO[] mvos)
    throws MMBusinessException
  {
    if ((mvos == null) || (mvos.length == 0)) {
      return null;
    }

    String[] keys = new String[mvos.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = mvos[i].getHeadVO().getPk_moid();
    }

    String userid = mvos[0].getHeadVO().getUserid();
    try
    {
      int lock = lockArray(keys, userid);
      if (lock==0) {
        throw new BusinessException(getstrbyid("UPP50081020-000051"));
      }

      checkTimeStamp(mvos);

      MoDMO dmo = new MoDMO();
      MoDMO dmo20 = new MoDMO();

      for (int i = 0; i < keys.length; i++) {
        MoHeaderVO mhead = dmo20.findHeaderByPrimaryKey(keys[i]);
        if ((!mhead.getSfdc().booleanValue()) || (mhead.getDczt().intValue() <= 0))
          continue;
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000052", null, new String[] { mhead.getScddh() }) + getstrbyid("UPP50081020-000053"));
      }

      MoDMO mdmo = new MoDMO();
      for (int i = 0; i < keys.length; i++) {
        String ddzt = mdmo.getBillzt(keys[i]);
        if (ddzt == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000047", null, new String[] { i + 1 + "" }) + getstrbyid("UPP50081020-000048"));
        }

        if (!ddzt.equalsIgnoreCase("C")) {
          String ztshow = ddzt.equalsIgnoreCase("B") ? getstrbyid("UPP50081020-000050") : ddzt.equalsIgnoreCase("A") ? getstrbyid("UPP50081020-000030") : getstrbyid("UPP50081020-000032");

          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000047", null, new String[] { i + 1 + "" }) + NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000033", null, new String[] { ztshow }));
        }

      }

      SCDDAPT apt = new SCDDAPT();

      dmo.unfinish(keys);
      for (int i = 0; i < keys.length; i++) {
        MoVO atpmo = new MoVO();
        atpmo.setParentVO(mdmo.findHeaderByPrimaryKey(keys[i]));
        apt.modifyATPWhenOpenBill(atpmo);
      }

      String[] i = dmo.queryBillHeadTs(keys);
      return i;
    }
    catch (Exception ex)
    {
      reportException(ex);
      throw new MMBusinessException("", ex);
    }
    finally {
      freeArray(keys, userid);
    }//throw localObject;
  }

  public String[] unover(MoVO[] mvos)
    throws MMBusinessException
  {
    if ((mvos == null) || (mvos.length == 0)) {
      return null;
    }

    String[] keys = new String[mvos.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = mvos[i].getHeadVO().getPk_moid();
    }

    String userid = mvos[0].getHeadVO().getUserid();
    try
    {
      int lock = lockArray(keys, userid);
      if (lock==0) {
        throw new BusinessException(getstrbyid("UPP50081020-000051"));
      }

      checkTimeStamp(mvos);

      MoDMO mdmo = new MoDMO();
      for (int i = 0; i < keys.length; i++) {
        String ddzt = mdmo.getBillzt(keys[i]);
        if (ddzt == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000047", null, new String[] { i + 1 + "" }) + getstrbyid("UPP50081020-000048"));
        }

        if (!ddzt.equalsIgnoreCase("D")) {
          String ztshow = ddzt.equalsIgnoreCase("B") ? getstrbyid("UPP50081020-000050") : ddzt.equalsIgnoreCase("A") ? getstrbyid("UPP50081020-000030") : getstrbyid("UPP50081020-000054");

          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000047", null, new String[] { i + 1 + "" }) + NCLangResOnserver.getInstance().getStrByID("50081020", "UPP50081020-000033", null, new String[] { ztshow }));
        }

      }

      MoDMO dmo = new MoDMO();
      dmo.unover(keys);

      String[] ddzt = dmo.queryBillHeadTs(keys);
      return ddzt;
    }
    catch (Exception ex)
    {
      reportException(ex);
      throw new MMBusinessException("", ex);
    }
    finally {
      freeArray(keys, userid);
    }//throw localObject;
  }

  private void createSubOrderByBom(MoHeaderVO mhead)
    throws BusinessException
  {
    PfUtilBO pfUtil = null;
    try
    {
      BomDMO bdmo = new BomDMO();
      BomVO bomVO = bdmo.findBomByVersion(mhead.getPk_corp(), mhead.getGcbm(), mhead.getWlbmid(), mhead.getBomver(), mhead.getFreeitemVO());

      if ((bomVO == null) || (bomVO.getParentVO() == null) || (bomVO.getChildrenVO() == null) || (bomVO.getChildrenVO().length == 0))
      {
        return;
      }

      BomHeaderVO bomHeader = (BomHeaderVO)bomVO.getParentVO();

      BomItemVO[] bomItems = (BomItemVO[])(BomItemVO[])bomVO.getChildrenVO();

      ArrayList al = new ArrayList(bomItems.length);
      for (int i = 0; i < bomItems.length; i++) {
        if (bomItems[i].getZxlx().intValue() == 0) {
          al.add(bomItems[i]);
        }
      }
      bomItems = new BomItemVO[al.size()];
      if (al.size() > 0) {
        al.toArray(bomItems);
      }
      bomVO.setChildrenVO(bomItems);
      if ((bomVO.getChildrenVO() == null) || (bomVO.getChildrenVO().length == 0))
      {
        return;
      }

      String[] sInvProducePKs = new String[bomItems.length];
      String[] sInvBasdocPKs = new String[bomItems.length];
      for (int i = 0; i < bomItems.length; i++) {
        sInvProducePKs[i] = bomItems[i].getPk_produce();
        sInvBasdocPKs[i] = bomItems[i].getZxbmid();
      }
      UFBoolean[] createFlags = new MoDMO().isCreateSubOrder(sInvProducePKs);

      String[] dateOfSubOrders = getDateByFixAhead(sInvBasdocPKs, mhead.getGcbm(), mhead.getJhkgrq().toString());

      int idx = 1;
      for (int i = 0; i < bomItems.length; i++) {
        if (!createFlags[i].booleanValue())
        {
          continue;
        }
        MoHeaderVO subHeader = (MoHeaderVO)mhead.clone();
        subHeader.setScddh(null);
        subHeader.setPk_moid(null);
        subHeader.setBomver(null);
        subHeader.setRtver(null);
        subHeader.setFjhsl(null);

        subHeader.setJhwgrq(mhead.getJhkgrq());
        if (isNull(dateOfSubOrders[i]))
          subHeader.setJhkgrq(mhead.getJhkgrq());
        else {
          subHeader.setJhkgrq(new UFDate(dateOfSubOrders[i]));
        }

        subHeader.setWlbmid(bomItems[i].getZxbmid());
        subHeader.setPk_produce(bomItems[i].getPk_produce());
        subHeader.setInvspec(bomItems[i].getInvspec());
        subHeader.setInvtype(bomItems[i].getInvtype());

        subHeader.setJhwgsl(mhead.getJhwgsl().multiply(bomItems[i].getSl()).multiply(bomItems[i].getShxs().add(1.0D)).div(bomHeader.getSl()));

        subHeader.setJldwid(bomItems[i].getJldwid());

        subHeader.setFreeitemVO(bomItems[i].getFreeitemVO());

        subHeader.setLyfbid(null);
        subHeader.setLylx(new Integer(1));
        subHeader.setLyid(mhead.getPk_moid());
        subHeader.setLydjh(mhead.getScddh());
        subHeader.setSjscddid(mhead.getPk_moid());
        subHeader.setSjscddh(mhead.getScddh());

        if ((mhead.getPch() != null) && (mhead.getPch().trim().length() > 0)) {
          subHeader.setPch(mhead.getPch() + "/Z-" + idx++);
        }

        ProductVO product = getProduct(subHeader.getPk_produce(), subHeader.getJhwgsl(), subHeader.getFreeitemVO());

        subHeader.setScbmid(product.getPk_deptdoc());
        subHeader.setYwyid(product.getPk_psndoc3());
        subHeader.setBomver(product.getBomver());
        subHeader.setRtver(product.getRtver());

        subHeader.setFjlid(product.getAssMeasPK());
        subHeader.setFjldw(product.getAssMeasName());
        subHeader.setFjlhsl(product.getAssHsl());
        if ((subHeader.getFjlhsl() != null) && (subHeader.getFjlhsl().doubleValue() > 0.0D))
        {
          subHeader.setFjhsl(subHeader.getJhwgsl().div(subHeader.getFjlhsl()));
        }

        subHeader.setGzzxid(new MoDMO().findMainWorkShop(subHeader.getPk_produce(), subHeader.getRtver()));

        BomVO bomVOsfdc = bdmo.findBomByVersion(subHeader.getPk_corp(), subHeader.getGcbm(), subHeader.getWlbmid(), subHeader.getBomver());

        if ((bomVOsfdc != null) && (bomVOsfdc.getParentVO() != null)) {
          BomHeaderVO bomHeadersfdc = (BomHeaderVO)bomVOsfdc.getParentVO();

          if (isNull(bomHeadersfdc.getSfdc()))
            subHeader.setAttributeValue("sfdc", "");
          else
            subHeader.setSfdc(bomHeadersfdc.getSfdc());
        }
        else
        {
          subHeader.setAttributeValue("sfdc", "");
        }

        MoVO subVO = new MoVO();
        subVO.setParentVO(subHeader);

        if (pfUtil == null)
        {
          pfUtil = new PfUtilBO();
        }
        pfUtil.processAction("SAVE", "A2", subHeader.getBusiDate().toString(), null, subVO, null);
      }
    }
    catch (MMBusinessException re) {
      throw re;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      reportException(ex);
      throw new BusinessException(ex.getMessage());
    }
  }

  private void createSubOrderByRoute(MoHeaderVO mhead)
    throws BusinessException
  {
    PfUtilBO pfUtil = null;
    try
    {
      RtBItemVO[] items = new RtDMO().getAllGxhl(mhead.getPk_corp(), mhead.getGcbm(), mhead.getWlbmid(), mhead.getJhkgrq(), mhead.getRtver(), mhead.getJhwgsl(), mhead.getFreeitemVO());

      if ((items == null) || (items.length == 0)) {
        return;
      }

      String[] sInvProducePKs = new String[items.length];
      String[] sInvBasdocPKs = new String[items.length];
      for (int i = 0; i < items.length; i++) {
        sInvProducePKs[i] = items[i].getPk_produce();
        sInvBasdocPKs[i] = items[i].getWlbmid();
      }
      UFBoolean[] createFlags = new MoDMO().isCreateSubOrder(sInvProducePKs);

      String[] dateOfSubOrders = getDateByFixAhead(sInvBasdocPKs, mhead.getGcbm(), mhead.getJhkgrq().toString());

      int idx = 1;
      for (int i = 0; i < items.length; i++) {
        if (!createFlags[i].booleanValue()) {
          continue;
        }
        MoHeaderVO subHeader = (MoHeaderVO)mhead.clone();
        subHeader.setScddh(null);
        subHeader.setPk_moid(null);
        subHeader.setBomver(null);
        subHeader.setRtver(null);
        subHeader.setFjhsl(null);

        subHeader.setJhwgrq(mhead.getJhkgrq());
        if (isNull(dateOfSubOrders[i]))
          subHeader.setJhkgrq(mhead.getJhkgrq());
        else {
          subHeader.setJhkgrq(new UFDate(dateOfSubOrders[i]));
        }
        subHeader.setWlbmid(items[i].getWlbmid());
        subHeader.setPk_produce(items[i].getPk_produce());
        subHeader.setInvspec(items[i].getInvspec());
        subHeader.setInvtype(items[i].getInvtype());

        subHeader.setFreeitemVO(items[i].getFreeitemVO());

        subHeader.setJhwgsl(mhead.getJhwgsl().multiply(items[i].getXhsl()).div(items[i].getFxsl()));

        subHeader.setJldwid(items[i].getJldwid());
        subHeader.setLylx(new Integer(1));
        subHeader.setLyid(mhead.getPk_moid());
        subHeader.setLydjh(mhead.getScddh());
        subHeader.setSjscddid(mhead.getPk_moid());
        subHeader.setSjscddh(mhead.getScddh());

        if ((mhead.getPch() != null) && (mhead.getPch().trim().length() > 0)) {
          subHeader.setPch(mhead.getPch() + "/Z-" + idx++);
        }

        ProductVO product = getProduct(subHeader.getPk_produce(), subHeader.getJhwgsl(), subHeader.getFreeitemVO());

        subHeader.setScbmid(product.getPk_deptdoc());
        subHeader.setYwyid(product.getPk_psndoc3());
        subHeader.setBomver(product.getBomver());
        subHeader.setRtver(product.getRtver());

        subHeader.setFjlid(product.getAssMeasPK());
        subHeader.setFjldw(product.getAssMeasName());
        subHeader.setFjlhsl(product.getAssHsl());
        if ((subHeader.getFjlhsl() != null) && (subHeader.getFjlhsl().doubleValue() > 0.0D))
        {
          subHeader.setFjhsl(subHeader.getJhwgsl().div(subHeader.getFjlhsl()));
        }

        subHeader.setGzzxid(new MoDMO().findMainWorkShop(subHeader.getPk_produce(), subHeader.getRtver()));

        MoVO subVO = new MoVO();
        subVO.setParentVO(subHeader);

        if (pfUtil == null)
        {
          pfUtil = new PfUtilBO();
        }
        pfUtil.processAction("SAVE", "A2", subHeader.getBusiDate().toString(), null, subVO, null);
      }
    }
    catch (MMBusinessException re) {
      throw re;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new BusinessException(ex.getMessage());
    }
  }

  public InvbasdocVO[] getInvBasDocVO(String[] sInvPKs)
    throws MMBusinessException
  {
    try
    {
      return new MoDMO().getInvBasDocVO(sInvPKs); } catch (Exception ex) {
    
    throw new MMBusinessException("", ex);
      }
  }

  public String getManIDFromProID(String pk_produce)
    throws MMBusinessException
  {
    try
    {
      return new MoDMO().getManIDFromProID(pk_produce); } catch (Exception ex) {
    
    throw new MMBusinessException("", ex);}
  }

  public void updatePrintedFlag(String[] sMOPKs)
    throws MMBusinessException
  {
    try
    {
      MoDMO dmo = new MoDMO();
      dmo.updatePrintedFlag(sMOPKs);
    } catch (Exception ex) {
      throw new MMBusinessException("", ex);
    }
  }

  public void deleteBySource(String[] srcIDs, String[] srcRowIDs, Integer srcType)
    throws MMBusinessException
  {
    if (((srcIDs == null) || (srcIDs.length == 0)) && ((srcRowIDs == null) || (srcRowIDs.length == 0)))
    {
      return;
    }
    try {
      MoDMO dmo = new MoDMO();
      MoHeaderVO[] headers = dmo.findHeadersBySource(srcIDs, srcRowIDs, srcType);

      if (headers.length > 0) {
        MoVO[] vos = new MoVO[headers.length];
        for (int i = 0; i < headers.length; i++) {
          vos[i] = new MoVO();
          vos[i].setParentVO(headers[i]);
        }
        PfUtilBO pfBO = new PfUtilBO();
        pfBO.processBatch("DELETE", "A2", new UFDate().toString(), vos, null, null);
      }

    }
    catch (MMBusinessException re)
    {
      throw re;
    } catch (Exception ex) {
      throw new MMBusinessException("", ex);
    }
  }

  private ArrayList insertCompleteMO(MoVO mo, PoVO[] pvos)
    throws BusinessException
  {
    ArrayList anOutList = null;
    try {
      MoDMO mdmo = new MoDMO();

      MoHeaderVO mhead = (MoHeaderVO)mo.getParentVO();
      mhead.setStatus(2);
      MoVO moatp = new MoVO();
      moatp.setParentVO(mhead);

      if ((isNull(moatp.getHeadVO().getPk_corp())) || (isNull(moatp.getHeadVO().getJhwgrq())) || (isNull(moatp.getHeadVO().getWlbmid())))
      {
        throw new BusinessException("insertcompletemo atp wrong ");
      }new SCDDAPT().modifyATP(moatp);

      String[] keys = insert(mo);
      if (keys == null) {
        throw new BusinessException(getstrbyid("UPP50081020-000055"));
      }

      anOutList = new ArrayList(keys.length);
      for (int i = 0; i < keys.length; i++) {
        anOutList.add(keys[i]);
      }

      mhead.setPrimaryKey(keys[0]);

      MOSourceVO[] srcs = null;
      SourceDMO srcDMO = new SourceDMO();
      srcs = organizeSource(mo, pvos);
      if ((srcs == null) && (!isNull(mhead.getLyid()))) {
        srcs = new MOSourceVO[1];
        srcs[0] = new MOSourceVO();
        srcs[0].setPk_corp(mhead.getPk_corp());
        srcs[0].setGcbm(mhead.getGcbm());
        srcs[0].setJldwid(mhead.getJldwid());
        srcs[0].setLydjh(mhead.getLydjh());
        srcs[0].setLyid(mhead.getLyid());
        srcs[0].setLyfbid(mhead.getLyfbid());
        srcs[0].setLylx(mhead.getLylx());
        srcs[0].setScddh(mhead.getScddh());
        srcs[0].setScddid(mhead.getPk_moid());
        srcs[0].setSl(mhead.getJhwgsl());
        srcs[0].setYtid(mhead.getYtid());
        srcs[0].setYtlx(mhead.getYtlx());
        srcs[0].setYtfbid(mhead.getYtfbid());
        if (!isNull(mhead.getYtdjh()))
          srcs[0].setYtdjh(mhead.getYtdjh());
        else if (!isNull(mhead.getXsddh())) {
          srcs[0].setYtdjh(mhead.getXsddh());
        }
      }
      if (srcs != null) {
        srcDMO.insertArray(srcs);
      }

      OrsDMO odmo = new OrsDMO();
      OrsVO[] orss = organizeOrs(mo, pvos);
      if (orss != null)
        odmo.insertArray(orss);
    }
    catch (Exception ex) {
      throw new BusinessException(ex.getMessage());
    }
    return anOutList;
  }

  public String saveMOWithMorePickms(MoVO mvo, PickmVO[] pkvoArray)
    throws MMBusinessException
  {
    try
    {
      if ((mvo == null) || (pkvoArray == null) || (pkvoArray.length == 0)) {
        throw new BusinessException(getstrbyid("UPP50081020-000218"));
      }

      PickmBO pkbo = new PickmBO();

      mvo.getParentVO().setStatus(2);

      ArrayList idArray = insertCompleteMO(mvo, null);
      if ((idArray == null) || (idArray.size() < 1)) {
        throw new BusinessException(getstrbyid("UPP50081020-000015"));
      }

      String key = (String)idArray.get(0);

      for (int i = 0; i < pkvoArray.length; i++)
      {
        ((PickmHeaderVO)pkvoArray[i].getParentVO()).setLylx(new Integer(1));

        ((PickmHeaderVO)pkvoArray[i].getParentVO()).setLyid(key);
        ((PickmHeaderVO)pkvoArray[i].getParentVO()).setLydjh(((MoHeaderVO)mvo.getParentVO()).getScddh());

        Integer successflag = pkbo.savePickm(pkvoArray[i]);
        if (successflag.intValue() == 1) {
          throw new BusinessException(getstrbyid("UPP50081020-000016"));
        }

        if (successflag.intValue() != 0) {
          throw new BusinessException(getstrbyid("UPP50081020-000017"));
        }

      }

      return key;
    } catch (MMBusinessException re) {
      throw re;
    } catch (Exception ex) {
      reportException(ex);
    throw new MMBusinessException("", ex);}
  }

  public String getMainWCenterID(String pk_rtid)
    throws MMBusinessException
  {
    try
    {
      MoDMO dmo = new MoDMO();
      String strWCenterID = dmo.getMainWCenter(pk_rtid);
      return strWCenterID; } catch (Exception ex) {
    
    throw new MMBusinessException("", ex);}
  }

  public String getRtVerByMainWCenter(MoVO movo, String strWkID)
    throws MMBusinessException
  {
    try
    {
      MoDMO dmo = new MoDMO();
      String version = dmo.getRVerByMainWCenter(movo.getHeadVO().getPk_produce(), strWkID, (FreeItemVO)movo.getHeadVO().getAttributeValue(FreeItemVOTookKit.FREE_FIELD_VO));

      return version; } catch (Exception ex) {
    
    throw new MMBusinessException("", ex);}
  }

  private String[] getDateByFixAhead(String[] strInvPKs, String factoryID, String curDate)
    throws BusinessException
  {
    String[] retDates = new String[strInvPKs.length];
    for (int i = 0; i < strInvPKs.length; i++) {
      retDates[i] = curDate;
    }

    PlantCalendarBO calendar = null;
    try
    {
      ProduceVO[] pvos = getProduceQry().queryProduceVOsByInvbasPKs(strInvPKs, factoryID);

      ProduceVO[] vos = new ProduceVO[strInvPKs.length];

      for (int i = 0; i < strInvPKs.length; i++) {
        for (int j = 0; j < pvos.length; j++) {
          if (!strInvPKs[i].toString().equals(pvos[j].getPk_invbasdoc().toString()))
            continue;
          vos[i] = pvos[j];
        }

      }

      calendar = new PlantCalendarBO();
      Integer curDateIndex = calendar.getCalendarIndex(factoryID, null, curDate);

      for (int i = 0; i < strInvPKs.length; i++)
      {
        if ((vos[i] == null) || (vos[i].getFixedahead() == null))
        {
          continue;
        }
        int interval = (int)Math.ceil(vos[i].getFixedahead().doubleValue());

        int kgrqIndex = Math.max(curDateIndex.intValue() - interval, 0);
        retDates[i] = calendar.getDateByIndex(factoryID, null, new Integer(kgrqIndex));
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new BusinessException(ex.getMessage());
    }
    finally
    {
    }

    return retDates;
  }

  private boolean checkMEstart(String pk_corp)
    throws BusinessException
  {
    try
    {
      CreatecorpDMO crtdmo = new CreatecorpDMO();
      boolean isstart = crtdmo.isEnabled(pk_corp, "ME");
      return isstart; } catch (Exception ex) {
    
    throw new BusinessException(ex.getMessage());
      }
  }

  public ArrayList getFlushMO(MoVO[] mos)
    throws MMBusinessException
  {
    try
    {
      MoHeaderVO[] mohead = null;
      mohead = new MoHeaderVO[mos.length];
      for (int i = 0; i < mos.length; i++) {
        mohead[i] = mos[i].getHeadVO();
      }

      ArrayList al = new ArrayList();

      for (int i = 0; i < mohead.length; i++)
      {
        if ((!mohead[i].getSfdc().booleanValue()) || (mohead[i].getDczt().intValue() >= 1))
          continue;
        al.add(mohead[i]);
      }

      if (al.size() <= 0) {
        return null;
      }
      mohead = new MoHeaderVO[al.size()];
      al.toArray(mohead);

      ArrayList alist = new ArrayList(5);
      alist.add(mohead);
      alist.add(mohead[0].getUserid());
      alist.add(mohead[0].getBusiDate());
      alist.add(new Boolean(true));

      alist.add(null);

      return alist;
    } catch (Exception ex) {
      reportException(ex);
      if ((ex instanceof MMBusinessException))
        throw ((MMBusinessException)ex);
    
    throw new MMBusinessException("", new BusinessException(ex.getMessage()));
    }
  }

  private void fillBatchCodes(MoVO[] vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.length == 0))
      return;
    try
    {
      MoDMO dmo = new MoDMO();
      String pk_corp = vos[0].getHeadVO().getPk_corp();
      String strFactory = vos[0].getHeadVO().getGcbm();

      String[] strInvBasKeys = new String[vos.length];
      for (int i = 0; i < strInvBasKeys.length; i++) {
        strInvBasKeys[i] = vos[i].getHeadVO().getWlbmid();
      }
      HashMap invMap = dmo.findBatchedManageKeys(strInvBasKeys, strFactory);

      BatchcodeRuleBO batchCode = new BatchcodeRuleBO();

      BillCodeObjValueVO objVO = new BillCodeObjValueVO();
      PickmDMO pkDMO = new PickmDMO();
      for (int i = 0; i < vos.length; i++)
      {
        if ((!isNull(vos[i].getHeadVO().getPch())) || (!invMap.containsKey(vos[i].getHeadVO().getWlbmid())))
        {
          continue;
        }

        String invManPk = (String)invMap.get(vos[i].getHeadVO().getWlbmid());

        String[] names = { "库存组织", "操作员", "部门", "工作中心" };
        String[] values = { vos[i].getHeadVO().getGcbm(), vos[i].getHeadVO().getUserid(), vos[i].getHeadVO().getScbmid(), vos[i].getHeadVO().getGzzxid() };

        objVO.setAttributeValue(names, values);

        HashMap retMap = null;
        try {
          retMap = batchCode.getBillCodeByRule("A2", pk_corp, new String[] { invManPk }, objVO);
        }
        catch (Exception e) {
          throw new BusinessException(getstrbyid("UPP50081020-000279"));
        }

        vos[i].getHeadVO().setPch((String)retMap.get(invManPk));

        pkDMO.setPch(new Integer(1), vos[i].getHeadVO().getPk_moid(), vos[i].getHeadVO().getPch());
      }
    }
    catch (MMBusinessException re) {
      throw re;
    }
    catch (Exception ex) {
      throw new BusinessException(ex.getMessage());
    }
  }

  private void returnCodes(MoHeaderVO[] headers)
    throws BusinessException
  {
    if ((headers == null) || (headers.length == 0))
      return;
    try
    {
      BillcodeRuleBO bbo = new BillcodeRuleBO();
      String pk_corp = headers[0].getPk_corp();
      BillCodeObjValueVO objVO = new BillCodeObjValueVO();
      for (int i = 0; i < headers.length; i++) {
        if (isNull(headers[i].getPch())) {
          continue;
        }
        String[] names = { "库存组织", "操作员", "部门", "工作中心" };
        String[] values = { headers[i].getGcbm(), headers[i].getUserid(), headers[i].getScbmid(), headers[i].getGzzxid() };

        objVO.setAttributeValue(names, values);

        bbo.returnBillCodeOnDelete(pk_corp, "AP", headers[i].getPch(), objVO);
      }
    }
    catch (MMBusinessException re) {
      throw re;
    } catch (Exception ex) {
      throw new BusinessException(ex.getMessage());
    }
  }

  public void backwrite(MoVO[] movo, UFDouble jhwgsl, String cuderid, UFDateTime tDateTime)
    throws MMBusinessException
  {
    try
    {
      int i = 0; if (i < movo.length) {
        MoHeaderVO mhead = movo[i].getHeadVO();
        if ((!isNull(mhead.getLyid())) && (mhead.getLyid().length() > 0) && (!isNull(mhead.getLylx())) && ((mhead.getLylx().intValue() == 7) || (mhead.getLylx().intValue() == 8) || (mhead.getLylx().intValue() == 9) || (mhead.getLylx().intValue() == 10)))
        {
          String m_pk_corp = new String();
          m_pk_corp = mhead.getPk_corp();

          String[] m_cbodyid = new String[1];

          String[] m_cheadid = new String[1];

          UFDouble[] m_dOldNum = new UFDouble[1];

          UFDouble[] m_dNum = new UFDouble[1];

          String m_coperatorid = new String();
          m_coperatorid = mhead.getUserid();

          m_cbodyid[0] = mhead.getLyfbid();
          m_cheadid[0] = mhead.getLyid();
          if (isNull(jhwgsl))
          {
            m_dOldNum[0] = mhead.getJhwgsl();
            m_dNum[0] = new UFDouble(0);
          }
          else
          {
            m_dOldNum[0] = jhwgsl;
            m_dNum[0] = mhead.getJhwgsl();
          }

          ParaRewriteVO prvo = new ParaRewriteVO();
          prvo.setPk_corp(m_pk_corp);
          prvo.setCHeadIdArray(m_cheadid);
          prvo.setCBodyIdArray(m_cbodyid);
          prvo.setDOldNumArray(m_dOldNum);
          prvo.setOperatorid(m_coperatorid);
          prvo.setTime(tDateTime.toString());
          prvo.setDNumArray(m_dNum);

          IOuter toOuter = (IOuter)NCLocator.getInstance().lookup(IOuter.class.getName());
          toOuter.backMoToOrder(prvo, "A2");
        } else if ((!isNull(mhead.getLyid())) && (mhead.getLyid().length() > 0) && (!isNull(mhead.getLylx())) && (mhead.getLylx().intValue() == 6))
        {
          Object[][] backinfo = new Object[1][6];

          backinfo[0][0] = null;
          backinfo[0][1] = null;
          backinfo[0][2] = null;
          backinfo[0][3] = null;
          backinfo[0][4] = null;
          backinfo[0][0] = mhead.getLyfbid();

          if (isNull(jhwgsl)) {
            backinfo[0][5] = new UFDouble(0.0D - mhead.getJhwgsl().doubleValue());
          }
          else
          {
            backinfo[0][5] = new UFDouble(mhead.getJhwgsl().doubleValue() - jhwgsl.doubleValue());
          }

          ISOToPUTO_BillConvertDMO billConvert = (ISOToPUTO_BillConvertDMO)NCLocator.getInstance().lookup(ISOToPUTO_BillConvertDMO.class.getName());
          billConvert.writeBackArrangeInfo(backinfo, cuderid, tDateTime);
        }
        i++;
      }

    }
    catch (Exception e)
    {
      if ((e instanceof MMBusinessException)) {
        e.printStackTrace();
        throw ((MMBusinessException)e);
      }
      e.printStackTrace();
      throw new MMBusinessException("", e);
    }
    finally
    {
    }
  }

  private IProduceQry getProduceQry()
  {
    return (IProduceQry)NCLocator.getInstance().lookup(IProduceQry.class.getName());
  }

  private class MOCodeCondition
  {
    private String corpid;
    private String factoryid;
    private String userid;
    private String deptid;
    private String workshopid;

    MOCodeCondition(MoHeaderVO mhead)
    {
      this.corpid = mhead.getPk_corp();
      this.factoryid = mhead.getGcbm();
      this.userid = mhead.getUserid();
      this.deptid = mhead.getScbmid();
      this.workshopid = mhead.getGzzxid();
    }

    void setCorpID(String newCorp)
    {
      this.corpid = newCorp;
    }

    void setFactoryID(String newfactory) {
      this.factoryid = newfactory;
    }

    void setUserID(String newuser) {
      this.userid = newuser;
    }

    void setDeptID(String newdept) {
      this.deptid = newdept;
    }

    void setWorkshopID(String newWorkshop) {
      this.workshopid = newWorkshop;
    }

    String getCorpID() {
      return this.corpid;
    }

    BillCodeObjValueVO toCodeObjValue() {
      BillCodeObjValueVO vo = new BillCodeObjValueVO();
      String[] names = { "库存组织", "操作员", "部门", "工作中心" };
      String[] values = { this.factoryid, this.userid, this.deptid, this.workshopid };

      vo.setAttributeValue(names, values);
      return vo;
    }
  }
}