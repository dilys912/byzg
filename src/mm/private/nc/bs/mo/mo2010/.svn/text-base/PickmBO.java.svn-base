package nc.bs.mo.mo2010;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.bd.CorpBO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.mm.pub.DdDMO;
import nc.bs.mm.pub.FreeItemBO;
import nc.bs.mm.pub.KzcsDMO;
import nc.bs.mm.pub.MMBusinessObject;
import nc.bs.mm.pub.pub1020.DisassembleDMO;
import nc.bs.mm.pub.pub1025.MMLockBO;
import nc.bs.mm.pub.pub1030.ActionDrivenDMO;
import nc.bs.mm.pub.pub1030.BLJHAPT;
import nc.bs.mo.mo2035.HandandtakeBO;
import nc.bs.mo.mo2035.PickmBBO;
import nc.bs.pd.pd4010.v5.BomDMO;
import nc.bs.pub.billcodemanage.BillcodeRuleBO;
import nc.bs.pub.lock.LockBO;
import nc.impl.uap.bd.corp.CorpImpl;
import nc.itf.ic.api.ATP;
import nc.itf.ic.api.OnHand;
import nc.itf.ic.service.IICToPU_StoreadminDMO;
import nc.ui.bd.CorpBO_Client;
import nc.ui.pub.ClientEnvironment;
import nc.vo.bd.CorpVO;
import nc.vo.bd.fd.DdVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.OnhandnumItemVO;
import nc.vo.mm.materialserver.BatchUtil;
import nc.vo.mm.pub.FreeItemVO;
import nc.vo.mm.pub.FreeItemVOTookKit;
import nc.vo.mm.pub.KzcsVO;
import nc.vo.mm.pub.MMBusinessException;
import nc.vo.mm.pub.pub1020.BomBTdItemVO;
import nc.vo.mm.pub.pub1020.ProduceCoreVO;
import nc.vo.mm.pub.pub1025.MMConstant;
import nc.vo.mm.pub.pub1030.HandandtakeUnitVO;
import nc.vo.mm.pub.pub1030.PickmHeaderVO;
import nc.vo.mm.pub.pub1030.PickmItemVO;
import nc.vo.mm.pub.pub1030.PickmVO;
import nc.vo.mo.mo2010.InvreplVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.ic.ATPVO;
public class PickmBO extends MMBusinessObject
{
  public void ejbCreate()
  {
  }

  public PickmVO AcceptFromSuborder(PickmHeaderVO header, PickmItemVO item, HandandtakeUnitVO[] jjdvos)
    throws BusinessException, MMBusinessException
  {
    MMLockBO lock = null;
    try
    {
      lock = lock(header.getPk_pickmid(), header.getOperid(), header.getTs());

      String errmsg = validateAccept(header, item);
      if (errmsg != null) {
        throw new BusinessException(errmsg);
      }
      for (int i = 0; i < jjdvos.length; i++) {
        if (!jjdvos[i].getZt().equals("A"))
          continue;
        jjdvos[i].setJssl(jjdvos[i].getYjsl());
        jjdvos[i].setJsdjhid(header.getPk_pickmid());
        jjdvos[i].setJsdjbid(item.getPk_pickm_bid());
        jjdvos[i].setJsdjh(header.getBljhdh());
        jjdvos[i].setJsdjlx("A3");
        jjdvos[i].setZt("B");
        jjdvos[i].setJsscddh(header.getLydjh());
        jjdvos[i].setJsscddid(header.getLyid());
      }

      PickmBBO handandtakebo = new PickmBBO();
      handandtakebo.updateArrays(jjdvos, header.getOperid());

      PickmDMO dmo1 = new PickmDMO();
      PickmVO vo = dmo1.findByPrimary(header.getPk_pickmid());
      PickmVO localPickmVO1 = vo;
      return localPickmVO1;
    }
    catch (MMBusinessException e)
    {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new MMBusinessException(e.getMessage());
    }
    finally
    {
      if (lock != null)
        try {
          freelock(lock, header.getPk_pickmid(), header.getOperid());
        }
        catch (Exception e) {
          throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
        }
    }
    //throw localObject;
  }
  public String getWlmanid(String pk_produce) throws MMBusinessException {
    try {
      PickmDMO pickmdmo = new PickmDMO();
      String pk_mandocid = pickmdmo.getWlmanid(pk_produce);
      return pk_mandocid;
    }
    catch (Exception e) {
      e.printStackTrace();
    throw new MMBusinessException("RemoteCall::", new BusinessException(e.getMessage()));
    }
  }

  public String CancelCheck(PickmVO vo)
    throws MMBusinessException
  {
    PickmHeaderVO header = (PickmHeaderVO)(PickmHeaderVO)vo.getParentVO();
    String pk_corp = header.getPk_corp();
    String key = header.getPk_pickmid();
    String operid = header.getOperid();
    Integer bljhlx = header.getBljhlx();
    Integer lylx = header.getLylx();
    String lyid = header.getLyid();
    MMLockBO lock = null;
    try
    {
      lock = lock(key, operid, header.getTs());

      if (!header.getZt().equalsIgnoreCase("B")) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000020"));
      }
      PickmDMO dmo = new PickmDMO();
      if (dmo.isFreeze(key).booleanValue()) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000021"));
      }
      if (lylx.intValue() == 1) {
        String mozt = dmo.getMozt(key);
        if (bljhlx.intValue() < 10) {
          if (mozt == null)
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000022"));
          if ((bljhlx.intValue() == 0) && (!mozt.equalsIgnoreCase("A")))
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000023"));
          if ((bljhlx.intValue() > 0) && ((mozt.equals("C")) || (mozt.equals("D"))))
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000024"));
        }
      }
      String mozt = key;
      return mozt;
    }
    catch (Exception e)
    {
      reportException(e);
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    }
    finally
    {
      if (lock != null)
        try {
          freelock(lock, key, operid);
        }
        catch (Exception e) {
          throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
        }
    }
    //throw localObject;
  }

  public void CancelClose(PickmVO vo)
    throws MMBusinessException
  {
    PickmHeaderVO header = (PickmHeaderVO)(PickmHeaderVO)vo.getParentVO();
    String key = header.getPk_pickmid();
    String operid = header.getOperid();
    Integer bljhlx = header.getBljhlx();
    MMLockBO lock = null;
    try
    {
      lock = lock(key, operid, header.getTs());

      if (!header.getZt().equalsIgnoreCase("C")) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000025"));
      }

      PickmDMO dmo = new PickmDMO();
      Integer lylx = dmo.getPickmlylx(key);
      if (lylx.intValue() == 1) {
        String mozt = dmo.getMozt(key);
        if (bljhlx.intValue() < 10) {
          if (mozt == null)
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000026"));
          if ((mozt.equalsIgnoreCase("C")) || (mozt.equalsIgnoreCase("D"))) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000027"));
          }
        }
      }
      dmo.updateZt(key, "B");
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("Remote Call:", e);
    }
    finally
    {
      if (lock != null)
        try {
          freelock(lock, key, operid);
        }
        catch (Exception e) {
          throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
        }
    }
  }

  public void Check(PickmVO vo)
    throws MMBusinessException
  {
    ArrayList al = batchCheck(new PickmVO[] { vo });
    if (al.get(0) != null)
      throw new MMBusinessException("Remote Call::", new BusinessException(al.get(0).toString()));
  }

  public void Close(PickmVO vo)
    throws MMBusinessException
  {
    PickmHeaderVO header = (PickmHeaderVO)(PickmHeaderVO)vo.getParentVO();
    String key = header.getPk_pickmid();
    String operid = header.getOperid();
    Integer lylx = header.getLylx();
    String lyid = header.getLyid();
    boolean isSourceOutBill = false;
    boolean islocking = false;
    String[] keys = null;
    try
    {
      PickmHeaderVO[] headers = null;

      PickmDMO dmo = new PickmDMO();
      if (key == null) {
        isSourceOutBill = true;
        headers = dmo.findHeaderByLyid(lylx, lyid);
        if (headers == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000028"));
        }

        Vector v = new Vector();
        for (int i = 0; i < headers.length; i++)
          if (headers[i].getBljhlx().intValue() < 10)
            v.add(headers[i]);
        if (v.size() == 0) { BusinessException be;
          return; } headers = new PickmHeaderVO[v.size()];
        v.copyInto(headers);

        keys = new String[headers.length];
        for (int i = 0; i < headers.length; i++)
          keys[i] = headers[i].getPk_pickmid();
      }
      else
      {
        isSourceOutBill = false;
        keys = new String[] { key };
        header = dmo.findHeaderByPrimary(key);
        if (header == null)
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000028"));
        headers = new PickmHeaderVO[] { header };
      }

      islocking = lockArray(keys, operid, "mm_pickm");
      if (!islocking)
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000029"));
      for (int i = 0; i < headers.length; i++)
      {
        String zt = headers[i].getZt();

        String bljhdh = headers[i].getBljhdh();

        Integer bljhlx = headers[i].getBljhlx();
        key = headers[i].getPk_pickmid();
        if ((!isSourceOutBill) && 
          (zt.equals("C"))) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000030", null, new String[] { "(" + bljhdh + ")" }));
        }

        if (zt.equals("A"))
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000031", null, new String[] { "(" + bljhdh + ")" }));
        if (zt.equals("B")) {
          PickmItemVO[] items = dmo.findItemsForHeader(key);
          PickmVO pickm = new PickmVO();
          pickm.setParentVO(headers[i]);
          pickm.setChildrenVO(items);
          BLJHAPT bljhapt = new BLJHAPT();
          bljhapt.modifyATPWhenCloseBill(pickm);
          dmo.updateZt(key, "C");
        }
      }
    }
    catch (Exception e)
    {
      BusinessException be;
      reportException(e);
      throw new MMBusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000032"), e);
    }
    finally {
      if (islocking)
        try
        {
          freeArray(keys, operid, "mm_pickm");
        }
        catch (Exception e) {
          reportException(e);
          BusinessException be = new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000033"));
          throw new MMBusinessException("Close(String key,String operid)", be);
        }
    }
  }

  public ArrayList collectDeliverMaterial(PickmVO[] pickms, Object obj)
    throws MMBusinessException
  {
    String pk_corp = (String)((ArrayList)obj).get(0);

    String gcbm = (String)((ArrayList)obj).get(1);

    String userid = (String)((ArrayList)obj).get(2);

    String logdate = (String)((ArrayList)obj).get(3);
    try
    {
      if (((ArrayList)obj).size() > 4)
      {
        UFDate ksrq = (UFDate)((ArrayList)obj).get(4);

        UFDate jsrq = (UFDate)((ArrayList)obj).get(5);

        String[] pk_produces = (String[])(String[])((ArrayList)obj).get(6);

        PickmDMO pickmdmo = new PickmDMO();

        String cond1 = " pickm.pk_corp = '" + pk_corp + "' and pickm.gcbm = '" + gcbm + "' and pickm.zt = 'B'";
        PickmHeaderVO[] headers = pickmdmo.findHeaderByCond(cond1);
        if (headers == null) {
          return null;
        }

        Vector v = new Vector();
        PickmVO pickm = new PickmVO();
        StringBuffer cond2 = new StringBuffer("and pickmb.sfkfl = 'Y' and pickmb.blfs in (0,3) and pickmb.blly = 0 and pickmb.sfdc = 'N' and pickmb.flrq >= '" + ksrq + "' and pickmb.flrq <= '" + jsrq + "' and pickmb.pk_produce in (");
        cond2.append("'" + pk_produces[0]);
        for (int i = 1; i < pk_produces.length; i++) {
          cond2.append("','" + pk_produces[i]);
        }
        cond2.append("')");
        for (int i = 0; i < headers.length; i++) {
          PickmItemVO[] items = pickmdmo.findItemsForHeader(headers[i].getPrimaryKey(), cond2.toString());

          if (items == null) {
            continue;
          }
          pickm.setParentVO(headers[i]);
          pickm.setChildrenVO(items);
          v.add(pickm.clone());
        }

        if (v.size() > 0) {
          pickms = new PickmVO[v.size()];
          v.copyInto(pickms);
        }
        else {
          pickms = null;
        }
      }
      if (pickms != null)
      {
        pickms = new DeliverMaterial().structCollectDelieverPickmVO(pickms, userid, logdate);

        DeliverMaterial dm = new DeliverMaterial(pk_corp, gcbm, userid);
        dm.deliverMaterial(pickms, logdate);
      }
    }
    catch (MMBusinessException e) {
      e.printStackTrace();
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    }

    return null;
  }

  public PickmVO createPickmVO(PickmHeaderVO headvo)
    throws MMBusinessException
  {
    PickmVO pickm = null;
    Disassemble disassemble = null;
    try {
      Boolean sfgxbl = isGxbl(headvo.getPk_produce());
      if (sfgxbl.booleanValue()) {
        disassemble = new GylxDisassemble(headvo.getPk_corp(), headvo.getGcbm(), headvo.getDate());
      }
      else
        disassemble = new BomDisassemble(headvo.getPk_corp(), headvo.getGcbm(), headvo.getDate());
      pickm = disassemble.createPickm(headvo);

      if ((pickm != null) && (pickm.getChildrenVO() != null)) {
        Vector v = new Vector();
        for (int i = 0; i < pickm.getChildrenVO().length; i++) {
          if (pickm.getChildrenVO()[i] != null) {
            PickmItemVO item = (PickmItemVO)pickm.getChildrenVO()[i];
            Integer materclass = (Integer)item.getAttributeValue("materclass");
            if ((materclass.intValue() == 0) || (materclass.intValue() == 2)) {
              v.add(item);
            }
          }
        }
        if (v.size() > 0) {
          PickmItemVO[] items = new PickmItemVO[v.size()];
          v.copyInto(items);
          pickm.setChildrenVO(items);
        }
        else {
          pickm.setChildrenVO(null);
        }
      }
    } catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    }
    return pickm;
  }

  public void delete(String operid, Integer lylx, String[] lyids)
    throws BusinessException, MMBusinessException
  {
    boolean isLockSussess = false;
    String[] pickmids = null;
    try {
      PickmDMO dmo = new PickmDMO();
      PickmVO[] pickms = dmo.findByLyids(lylx, lyids);
      if ((pickms == null) || (pickms.length <= 0))
        return;
      Vector v = new Vector();
      Vector v1 = new Vector();
      for (int i = 0; i < pickms.length; i++) {
        if (pickms[i] != null) {
          v.add(((PickmHeaderVO)pickms[i].getParentVO()).getPrimaryKey());
          v1.add(pickms[i]);
        }
      }
      if (v.size() > 0) {
        pickmids = new String[v.size()];
        pickms = new PickmVO[v.size()];
        v.copyInto(pickmids);
        v1.copyInto(pickms);
      }
      isLockSussess = lockArray(pickmids, operid, "mm_pickm");
      if (!isLockSussess) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000034"));
      }

      boolean sfcw = false;
      String errMsg = "";

      for (int i = 0; i < pickms.length; i++) {
        if (!((PickmHeaderVO)pickms[i].getParentVO()).getZt().equals("A")) {
          errMsg = errMsg + ((PickmHeaderVO)pickms[i].getParentVO()).getBljhdh() + ",";
          sfcw = true;
        }
      }
      if (sfcw)
      {
        if (errMsg.length() > 1) {
          errMsg = NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000035", null, new String[] { "(" + errMsg.substring(0, errMsg.length() - 1) + ")" });
        }
        throw new BusinessException(errMsg);
      }

      BLJHAPT apt = new BLJHAPT();
      for (int i = 0; i < pickms.length; i++) {
        apt.modifyATPWhenDeleteBill(pickms[i]);
        dmo.delete(((PickmHeaderVO)pickms[i].getParentVO()).getPrimaryKey());
      }

      for (int i = 0; i < pickmids.length; i++)
        returnBillCodeOnDelete("A3", (String)pickms[0].getParentVO().getAttributeValue("pk_corp"), (String)pickms[0].getParentVO().getAttributeValue("gcbm"), operid, (String)pickms[i].getParentVO().getAttributeValue("bljhdh"));
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("delete(Integer lylx,String[] lyid)", e);
    }
    finally {
      if (isLockSussess)
        freeArray(pickmids, operid, "mm_pickm");
    }
  }

  public void deletePickm(PickmVO vo)
    throws MMBusinessException
  {
    MMLockBO lock = null;
    PickmHeaderVO header = (PickmHeaderVO)vo.getParentVO();
    String operid = header.getOperid();
    String key = header.getPk_pickmid();
    try
    {
      lock = lock(key, operid, header.getTs());

      if (!header.getZt().equalsIgnoreCase("A")) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000036"));
      }
      PickmDMO dmo = new PickmDMO();
      dmo.delete(key);

      returnBillCodeOnDelete("A3", (String)vo.getParentVO().getAttributeValue("pk_corp"), (String)vo.getParentVO().getAttributeValue("gcbm"), operid, (String)vo.getParentVO().getAttributeValue("bljhdh"));
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("delete(String key)", e);
    }
    finally
    {
      if (lock != null)
        try {
          freelock(lock, key, operid);
        }
        catch (Exception e) {
          throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
        }
    }
  }

  private ArrayList divInvByDate(String[] pk_invmandoc, String[] end_dates)
  {
    Hashtable h = new Hashtable();
    Hashtable hSort = new Hashtable();
    for (int i = 0; i < end_dates.length; i++) {
      if (h.containsKey(end_dates[i])) {
        Vector v = (Vector)h.get(end_dates[i]);
        Vector vSort = (Vector)hSort.get(end_dates[i]);
        v.add(pk_invmandoc[i]);
        vSort.add(new Integer(i));
        h.put(end_dates[i], v);
        hSort.put(end_dates[i], vSort);
      }
      else {
        Vector v = new Vector();
        Vector vSort = new Vector();
        v.add(pk_invmandoc[i]);
        vSort.add(new Integer(i));
        h.put(end_dates[i], v);
        hSort.put(end_dates[i], vSort);
      }
    }

    Enumeration enu = h.keys();
    ArrayList al = new ArrayList();
    while (enu.hasMoreElements()) {
      String date = (String)enu.nextElement();
      Vector inv = (Vector)h.get(date);
      Vector sort = (Vector)hSort.get(date);
      String[] s_inv = new String[inv.size()];
      Integer[] i_sort = new Integer[sort.size()];
      inv.copyInto(s_inv);
      sort.copyInto(i_sort);
      al.add(date);
      al.add(s_inv);
      al.add(i_sort);
    }
    return al;
  }

  public InvreplVO[] findByBtdwl(String pk_corp, String gcbm, String headwlid, String bodywlid, UFDate rq, String version, FreeItemVO fxfreeitemvo, FreeItemVO zxfreeitemvo)
    throws MMBusinessException
  {
    InvreplVO[] invrepls = null;
    try {
      BomDMO bomdmo = new BomDMO();
      ArrayList al = bomdmo.getTdwl(pk_corp, gcbm, headwlid, new String[] { bodywlid }, rq, version, fxfreeitemvo, new FreeItemVO[] { zxfreeitemvo });
      if ((al != null) && (al.size() > 0)) {
        if (al.get(0) == null) return null;
        BomBTdItemVO[] bombtditemvos = (BomBTdItemVO[])(BomBTdItemVO[])al.get(0);
        invrepls = new InvreplVO[bombtditemvos.length];
        for (int i = 0; i < bombtditemvos.length; i++) {
          invrepls[i] = new InvreplVO();
          invrepls[i].setTdwlid(bombtditemvos[i].getWlbmid());
          invrepls[i].setTdwlproduceid(bombtditemvos[i].getPk_produce());
          invrepls[i].setTdwlbm(bombtditemvos[i].getWlbm());
          invrepls[i].setTdwlmc(bombtditemvos[i].getWlmc());
          invrepls[i].setTdinvmandoc(bombtditemvos[i].getpk_invmandoc());
          invrepls[i].setTdwlgg(bombtditemvos[i].getInvspec());
          invrepls[i].setTdwlxh(bombtditemvos[i].getInvtype());
          invrepls[i].setJldwid(bombtditemvos[i].getJldwid());
          invrepls[i].setJldwmc(bombtditemvos[i].getJldwmc());
          invrepls[i].setTdxs(bombtditemvos[i].getTdxs());
          invrepls[i].setReplaceorder(bombtditemvos[i].getTdyxj());
          invrepls[i].setFreeitemVO(bombtditemvos[i].getFreeitemVO());
        }
      }
    }
    catch (Exception e)
    {
      reportException(e);
      throw new MMBusinessException("Remote Call:", e);
    }
    return invrepls;
  }

  public PickmVO findByPrimary(String hid)
    throws MMBusinessException
  {
    PickmVO pickm = null;
    try {
      PickmDMO dmo = new PickmDMO();
      pickm = dmo.findByPrimary(hid);
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("PickmBean::findByPrimary(String hid) Exception!", e);
    }
    return pickm;
  }

  public Vector findPickmByGxh(String[] scddid, String[] gxh, String[] gzzxid)
    throws MMBusinessException
  {
    try
    {
      PickmDMO dmo = new PickmDMO();
      Vector vPickm = dmo.findPickmByGxh(scddid, gxh, gzzxid);
      return vPickm;
    }
    catch (SQLException e) {
      reportException(e);
      throw new MMBusinessException("数据库异常:", e);
    }
    catch (NamingException e) {
      reportException(e);
      throw new MMBusinessException("nc.bs.mo.mo2010.PickmDMO 实例化异常:", e);
    }
    catch (Exception e) {
      reportException(e);
    throw new MMBusinessException(e.getMessage());
    }
  }

  public String[] findClByItemPrimary(String pk_pickm_bid)
    throws MMBusinessException
  {
    try
    {
      PickmDMO dmo = new PickmDMO();
      String[] strRet = dmo.findClByItemPrimary(pk_pickm_bid);
      return strRet;
    }
    catch (Exception e) {
      reportException(e);
    throw new MMBusinessException("findClByItemPrimary(String pk_pickm_bid)", e);
    }
  }

  public PickmHeaderVO[] findHeaderByVO(ConditionVO[] vos)
    throws MMBusinessException
  {
    PickmHeaderVO[] pickms = null;
    try {
      PickmDMO dmo = new PickmDMO();
      pickms = dmo.findHeaderByCond(getWhereSQL(vos));
    }
    catch (Exception e)
    {
      reportException(e);
      throw new MMBusinessException("PickmBean::findHeaderByVO(nc.vo.pub.query.ConditionVO[]) Exception!", e);
    }
    return pickms;
  }

  public PickmItemVO[] findItemsForHeader(String key)
    throws MMBusinessException
  {
    PickmItemVO[] items = null;
    try {
      PickmDMO dmo = new PickmDMO();
      items = dmo.findItemsForHeader(key);
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("PickmBean::findItemsForHeader(String key) Exception!", e);
    }
    return items;
  }

  public PickmItemVO[] findItemsForHeader(String key, String cond)
    throws MMBusinessException
  {
    PickmItemVO[] items = null;
    try {
      PickmDMO dmo = new PickmDMO();
      items = dmo.findItemsForHeader(key, cond);
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("PickmBean::findItemsForHeader(String key) Exception!", e);
    }
    return items;
  }

  public PickmVO deliverMaterial(PickmVO pickm, String userid, String logdate)
    throws MMBusinessException
  {
    String pk_pickmid = ((PickmHeaderVO)pickm.getParentVO()).getPk_pickmid();

    UFDateTime ts = ((PickmHeaderVO)pickm.getParentVO()).getTs();

    String pk_corp = ((PickmHeaderVO)pickm.getParentVO()).getPk_corp();

    String gcbm = ((PickmHeaderVO)pickm.getParentVO()).getGcbm();
    try
    {
      DeliverMaterial dm = new DeliverMaterial(pk_corp, gcbm, userid);
      dm.deliverMaterial(new PickmVO[] { pickm }, logdate);

      pickm = findByPrimary(pk_pickmid);

      return pickm;
    }
    catch (BusinessException e) {
      throw new MMBusinessException("Remote Call:", e);
    }
    catch (Exception e) {
      e.printStackTrace();
    throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    }
  }

  public void workingProcDM(PickmVO[] pickms, String userid, String logdate)
    throws MMBusinessException
  {
    boolean islocking = false;
    String[] pk_pickmids = new String[pickms.length];
    for (int i = 0; i < pk_pickmids.length; i++) {
      pk_pickmids[i] = ((PickmHeaderVO)pickms[i].getParentVO()).getPk_pickmid();
    }

    String pk_corp = ((PickmHeaderVO)pickms[0].getParentVO()).getPk_corp();

    String gcbm = ((PickmHeaderVO)pickms[0].getParentVO()).getGcbm();
    try
    {
      DeliverMaterial dm = new DeliverMaterial(pk_corp, gcbm, userid);
      dm.deliverMaterial(pickms, logdate);
    }
    catch (BusinessException e) {
      throw new MMBusinessException("Remote Call:", e);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    }
  }

  public void splitPickmItemForGzzx(String pk_moid, String gxh, String oldgzzxid, String[] newgzzxid, UFDouble oldsl, UFDouble[] newsl, String userid, String logdate)
    throws MMBusinessException
  {
    MMLockBO lock = null;
    String pk_pickmid = null;
    try {
      PickmDMO pickmdmo = new PickmDMO();

      PickmVO pickm = pickmdmo.findPickmByGxh(pk_moid, gxh, oldgzzxid);
      if ((pickm == null) || (pickm.getChildrenVO() == null) || (pickm.getChildrenVO().length == 0)) return; PickmHeaderVO header = (PickmHeaderVO)pickm.getParentVO();

      if (header.getBomver() != null) return; pk_pickmid = header.getPk_pickmid();

      lock = lock(header.getPk_pickmid(), userid, header.getTs());

      if (pickm.getParentVO().getAttributeValue("zt").equals("C")) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000242", null, new String[] { "(" + header.getBljhdh() + ")" }));
      }

      Vector v = new Vector();
      PickmItemVO[] items = (PickmItemVO[])(PickmItemVO[])pickm.getChildrenVO();
      for (int i = 0; i < items.length; i++) {
        for (int j = 0; j < newgzzxid.length; j++) {
          PickmItemVO item = (PickmItemVO)items[i].clone();

          UFDouble jhcksl = items[i].getDwde().multiply(newsl[j]);
          item.setJhcksl(jhcksl);

          if (item.getFjldwid() != null) {
            UFDouble[] ret = BatchUtil.getUFDoubleByConversion(new UFDouble[] { item.getJhcksl(), item.getJhcksl(), item.getFjlhsl() }, 0, true);
            item.setFjhcksl(ret[0]);
          }
          if (newgzzxid[j].equals(oldgzzxid))
          {
            item.setStatus(1);
          }
          else
          {
            item.setGzzxid(newgzzxid[j]);

            item.setLjcksl(null);

            item.setPk_pickm_bid(null);

            item.setStatus(2);

            if (pickmdmo.getGzzxwxcsid(item.getGzzxid()) != null) {
              item.setSfdc(new UFBoolean(false));
              item.setCkckid(pickmdmo.getMaterialWarehouse(item.getPk_produce()));
            }
          }

          v.add(item);
        }
      }
      if (v.size() > 0) {
        items = new PickmItemVO[v.size()];
        v.copyInto(items);
        pickm.setChildrenVO(items);

        pickmdmo.update(pickm);
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new MMBusinessException("Remote Call::", new BusinessException(e.getMessage()));
    }
    finally {
      if (lock != null)
        try {
          freelock(lock, pk_pickmid, userid);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
    }
  }

  private void freeArray(String[] pks, String userid, String tablename)
    throws MMBusinessException
  {
    try
    {
      new LockBO().freePKArray(pks, userid, tablename);
    }
    catch (Exception ex) {
      reportException(ex);
      throw new MMBusinessException(ex.getMessage());
    } finally {
    }
  }

  private void freelock(MMLockBO bo, String pk, String userid) throws BusinessException {
    if (bo == null)
      return;
    try {
      bo.freePK(pk, userid, "mm_pickm");
    } catch (Exception ex) {
      reportException(ex);
      throw new BusinessException(ex.getMessage());
    }
    finally
    {
    }
  }

  public ArrayList getATPNum(String pk_corp, String pk_calbody, String[] ckckids, String[] rkckids, String[] pk_produceids, String[] end_dates, FreeItemVO[] zyx, String[] pchs)
    throws MMBusinessException
  {
    try
    {
      ArrayList al = new ArrayList(4);

      UFDouble[] datp = new UFDouble[pk_produceids.length];
      UFDouble[] ckxcl = new UFDouble[pk_produceids.length];
      UFDouble[] rkxcl = new UFDouble[pk_produceids.length];
      UFDouble[] kcxcl = new UFDouble[pk_produceids.length];
      PickmDMO pickmdmo = new PickmDMO();

      ATP atp = (ATP)NCLocator.getInstance().lookup(ATP.class.getName());
      OnHand onHand = (OnHand)NCLocator.getInstance().lookup(OnHand.class.getName());

      for (int i = 0; i < pk_produceids.length; i++) {
        String invmanid = pickmdmo.getWlmanid(pk_produceids[i]);
        String pch = pchs == null ? null : pchs[i];
        String zyx1 = (zyx == null) || (zyx[i] == null) ? null : zyx[i].getZyx1();
        String zyx2 = (zyx == null) || (zyx[i] == null) ? null : zyx[i].getZyx2();
        String zyx3 = (zyx == null) || (zyx[i] == null) ? null : zyx[i].getZyx3();
        String zyx4 = (zyx == null) || (zyx[i] == null) ? null : zyx[i].getZyx4();
        String zyx5 = (zyx == null) || (zyx[i] == null) ? null : zyx[i].getZyx5();

        ATPVO[] atpParams = new ATPVO[1];
        atpParams[0] = new ATPVO();
        atpParams[0].setPk_corp(pk_corp);
        atpParams[0].setCcalbodyid(pk_calbody);
        atpParams[0].setCwarehouseid(ckckids[i]);
        atpParams[0].setCinventoryid(invmanid);
        atpParams[0].setVfree1(zyx1);
        atpParams[0].setVfree2(zyx2);
        atpParams[0].setVfree3(zyx3);
        atpParams[0].setVfree4(zyx4);
        atpParams[0].setVfree5(zyx5);
        atpParams[0].setVbatchcode(pch);
        atpParams[0].setDplandate(new UFDate(end_dates[i]));

        datp[i] = atp.apiQueryATP(atpParams)[0];

        OnhandnumItemVO[] onHandParams = new OnhandnumItemVO[1];
        onHandParams[0] = new OnhandnumItemVO();
        onHandParams[0].setPk_corp(pk_corp);
        onHandParams[0].setCcalbodyid(pk_calbody);
        onHandParams[0].setCwarehouseid(ckckids[i]);
        onHandParams[0].setCinventoryid(invmanid);
        onHandParams[0].setVfree1(zyx1);
        onHandParams[0].setVfree2(zyx2);
        onHandParams[0].setVfree3(zyx3);
        onHandParams[0].setVfree4(zyx4);
        onHandParams[0].setVfree5(zyx5);
        onHandParams[0].setVlot(pch);

        ckxcl[i] = onHand.apiQueryOnHand(onHandParams)[0];

        if (rkckids[i] != null)
        {
          onHandParams[0].setCwarehouseid(rkckids[i]);
          rkxcl[i] = onHand.apiQueryOnHand(onHandParams)[0];
        }

        onHandParams[0].setCwarehouseid(null);
        kcxcl[i] = onHand.apiQueryOnHand(onHandParams)[0];
      }
      al.add(datp);
      al.add(ckxcl);
      al.add(rkxcl);
      al.add(kcxcl);
      return al;
    }
    catch (Exception e) {
      reportException(e);
    throw new MMBusinessException("ATP query error ", new BusinessException(e.getMessage()));
    }
  }

  public String getBillCode(String billtype, String pkcorp, String gcbm, String operator)
    throws MMBusinessException
  {
    try
    {
      BillCodeObjValueVO vo = new BillCodeObjValueVO();
      String[] names = { "库存组织", "操作员" };
      String[] values = { gcbm, operator };
      vo.setAttributeValue(names, values);
      String scddh = getBillCode(billtype, pkcorp, vo);
      String str1 = scddh;
      return str1;
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.out);
      throw new MMBusinessException("Remote Call::", ex); } finally {
    }
   // throw localObject;
  }

  private String getBillCode(String billtype, String pkcorp, BillCodeObjValueVO billVO)
    throws BusinessException
  {
    try
    {
      String djh = new BillcodeRuleBO().getBillCode(billtype, pkcorp, null, billVO);
      String str1 = djh;
      return str1;
    }
    catch (Exception e)
    {
      e.printStackTrace(System.out);
      throw new BusinessException(e.getMessage());
      
    } finally {
    
    }
  }

  public void returnBillCode(String billtype, String pkcorp, String gcbm, String operator, String bljhdh)
    throws MMBusinessException
  {
    try
    {
      BillCodeObjValueVO vo = new BillCodeObjValueVO();
      String[] names = { "库存组织", "操作员" };
      String[] values = { gcbm, operator };
      vo.setAttributeValue(names, values);

      new BillcodeRuleBO().returnBillCodeOnDelete(pkcorp, billtype, bljhdh, vo);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.out);
      throw new MMBusinessException("Remote Call::", ex);
    }
    finally
    {
    }
  }

  public void returnBillCodeOnDelete(String billtype, String pkcorp, String gcbm, String operator, String bljhdh)
    throws MMBusinessException
  {
    try
    {
      BillCodeObjValueVO vo = new BillCodeObjValueVO();
      String[] names = { "库存组织", "操作员" };
      String[] values = { gcbm, operator };
      vo.setAttributeValue(names, values);

      new BillcodeRuleBO().returnBillCodeOnDelete(pkcorp, billtype, bljhdh, vo);
    }
    catch (Exception ex) {
      ex.printStackTrace(System.out);
      throw new MMBusinessException("Remote Call::", ex);
    }
    finally
    {
    }
  }

  public Boolean getDefaultSfct(String pk_corp, String gcbm)
    throws MMBusinessException
  {
    String sfct = null;
    try {
      KzcsDMO kdmo = new KzcsDMO();
      KzcsVO kvo = kdmo.queryAllKzcs(pk_corp, gcbm, "ctbzmrz");
      sfct = kvo.getCsz2();
    }
    catch (Exception e)
    {
      reportException(e);
      sfct = "N";
    }
    if ((sfct == null) || (sfct.trim().length() == 0))
      sfct = "N";
    else
      sfct = sfct.trim();
    if (sfct.equalsIgnoreCase("Y")) {
      return new Boolean(true);
    }
    return new Boolean(false);
  }

  private String getPickmzt(String bljhid)
    throws BusinessException
  {
    String zt = null;
    try {
      PickmDMO dmo = new PickmDMO();
      zt = dmo.getPickmzt(bljhid);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000039"));
    }

    return zt;
  }

  public Vector getReplaceInfo(String[] tdlyids)
    throws MMBusinessException
  {
    try
    {
      PickmDMO dmo = new PickmDMO();
      Vector replaceInfo = dmo.getReplaceInfo(tdlyids);
      return replaceInfo;
    }
    catch (Exception e) {
      reportException(e);
    throw new MMBusinessException("PickmBO::getReplaceInfo(String[] tdlyids) Exception!", e);
    }
  }

  private String getWhereSQL(ConditionVO[] conditions)
  {
    if ((conditions == null) || (conditions.length == 0))
      return null;
    String str = "";
    for (int i = 2; i < conditions.length; i++) {
      str = str + conditions[i].getSQLStr();
    }

    if (str.trim().length() > 0) {
      str = str.substring(str.indexOf(" ", 1));
      str = "(" + str + ")";
    }
    for (int i = 0; i < 2; i++) {
      str = str + conditions[i].getSQLStr();
    }
    return str;
  }

  public String getWHManager(String corpid, String gcbm, String warehouseid, String pk_produce)
    throws BusinessException, MMBusinessException
  {
    try
    {
      PickmDMO pickmDMO = new PickmDMO();
      String pk_invmandoc = pickmDMO.getWlmanid(pk_produce);

      IICToPU_StoreadminDMO storeadmin = (IICToPU_StoreadminDMO)NCLocator.getInstance().lookup(IICToPU_StoreadminDMO.class.getName());
      String whmanger = storeadmin.getWHManager(corpid, gcbm, warehouseid, pk_invmandoc);
      return whmanger;
    } catch (Exception e) {
      reportException(e);
      if ((e instanceof MMBusinessException))
        throw ((MMBusinessException)e);
    
    throw new MMBusinessException("getWHManager(String corpid,String gcbm,String warehouseid,String inventoryid)", e);
    }
  }

  protected String getWlbm(String wlid)
    throws BusinessException
  {
    String wlbm = null;
    try {
      PickmDMO dmo = new PickmDMO();
      wlbm = dmo.getWlbm(wlid);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000040"));
    }

    return wlbm;
  }

  public ArrayList insert(PickmVO pickm)
    throws MMBusinessException
  {
    try
    {
      PickmDMO dmo = new PickmDMO();
      String[] key = dmo.insert(pickm);
      ArrayList al = null;
      if (key != null) {
        al = new ArrayList();
        String ts = dmo.getTs(key[0]);
        al.add(ts);
        for (int i = 0; i < key.length; i++)
          al.add(key[i]);
      }
      return al;
    }
    catch (Exception e) {
      reportException(e);
    throw new MMBusinessException("PickmBO::insert(PickmVO) Exception!", e);
    }
  }

  public Boolean isWholeMgt(String pk_corp, String pk_invbasdoc)
    throws MMBusinessException
  {
    try
    {
      PickmDMO pickmdmo = new PickmDMO();
      return pickmdmo.isWholeMgt(pk_corp, pk_invbasdoc);
    } catch (Exception e) {
      e.printStackTrace();
    throw new MMBusinessException("RemoteCall::", new BusinessException(e.getMessage()));
    }
  }

  private Boolean isGxbl(String pk_produce)
    throws BusinessException
  {
    Boolean sfgxbl = new Boolean(false);
    try {
      PickmDMO pickmdmo = new PickmDMO();
      String value = pickmdmo.getBlfsByWl(pk_produce);
      sfgxbl = value.equals("2") ? new Boolean(true) : new Boolean(false);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("得到是否工序备料异常，默认为按BOM备料");
      sfgxbl = new Boolean(false);
    }
    return sfgxbl;
  }

  private MMLockBO lock(String pk, String userid, UFDateTime ts) throws BusinessException
  {
    MMLockBO bo = null;
    String errmsg = null;
    int ret = -1;
    try
    {
      bo = new MMLockBO();
      if (ts == null)
        ret = bo.lockPK(pk, userid, "mm_pickm", "pk_pickmid").intValue();
      else
        ret = bo.lockPKByTime(pk, userid, "mm_pickm", "pk_pickmid", ts).intValue();
    } catch (Exception ex) {
      freelock(bo, pk, userid);
      reportException(ex);
      throw new BusinessException(ex.getMessage());
    }
    switch (ret) {
    case 0:
      errmsg = NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000041");
      break;
    case 1:
      errmsg = NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000042");
      break;
    case 2:
      break;
    case 3:
      errmsg = NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000043");
      break;
    default:
      errmsg = NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000041");
    }
    if (errmsg != null) {
      throw new BusinessException(errmsg);
    }
    return bo;
  }

  private boolean lockArray(String[] pks, String userid, String tablename)
    throws MMBusinessException
  {
    try
    {
      boolean success = new LockBO().lockPKArray(pks, userid, tablename);

      boolean bool1 = success;
      return bool1;
    }
    catch (Exception ex)
    {
      reportException(ex);
      throw new MMBusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000044") + ex.getLocalizedMessage()); } finally {
    }
   // throw localObject;
  }

  public void ModifyLydj(PickmVO pickm, UFBoolean sfcxsc)
    throws MMBusinessException
  {
    PickmHeaderVO header = (PickmHeaderVO)pickm.getParentVO();
    if (header == null) return; header.setShrmc(null);
    header.setShrq(null);
    String operid = header.getOperid();
    MMLockBO lock = null;
    try
    {
      if (sfcxsc.booleanValue())
      {
        String bljhdh = getBillCode("A3", header.getPk_corp(), header.getGcbm(), header.getOperid());
        header.setBljhdh(bljhdh);
        delete(operid, header.getLylx(), new String[] { header.getLyid() });
        savePickm(pickm);
      }
      else {
        PickmDMO dmo = new PickmDMO();
        PickmHeaderVO[] headers = dmo.findHeaderByLyid(header.getLylx(), header.getLyid());
        if (headers == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000045"));
        }
        for (int i = 0; i < headers.length; i++)
        {
          String zt = headers[i].getZt();
          if (!zt.equals("A")) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000046", null, new String[] { "(" + headers[i].getBljhdh() + ")" }));
          }

          if (headers[i].getBljhlx().intValue() == 0) {
            header.setPrimaryKey(headers[i].getPrimaryKey());
            header.setSfct(headers[i].getSfct());
          }

        }

        lock = lock(header.getPrimaryKey(), operid, header.getTs());

        dmo.updatePickmItemWhenMOChanged(header.getPk_pickmid(), header.getAttributeValue("gzzxid") == null ? null : header.getAttributeValue("gzzxid").toString());

        dmo.updateHeader(header);
      }
    }
    catch (MMBusinessException e) {
      reportException(e);
      throw e;
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    }
    finally
    {
      try {
        freelock(lock, header.getPrimaryKey(), operid);
      }
      catch (Exception e) {
        throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
      }
    }
  }

  public ArrayList queryDictory(String[] tablename, String[] fieldname)
    throws MMBusinessException
  {
    try
    {
      DdDMO dmo = new DdDMO();
      ArrayList al = new ArrayList();
      for (int i = 0; i < tablename.length; i++) {
        DdVO[] ddvo = (DdVO[])dmo.queryAllNoTranslate(tablename[i], fieldname[i]);

        if ((tablename[i].equals("bd_bom_b")) && (fieldname[i].equals("zxlx")) && 
          (ddvo != null)) {
          Vector v = new Vector();
          for (int j = 0; j < ddvo.length; j++) {
            if (ddvo[j].getSzqz().intValue() < 5)
              v.add(ddvo[j]);
          }
          if (v.size() > 0) {
            ddvo = new DdVO[v.size()];
            v.copyInto(ddvo);
          }
          else {
            ddvo = new DdVO[0];
          }

        }

        al.add(ddvo);
      }
      return al;
    }
    catch (Exception e) {
      reportException(e);
    throw new MMBusinessException("PgdBO::queryDictory Exception!", e);
    }
  }

  public Vector queryPublic(String pk_corp, String gcbm)
    throws MMBusinessException
  {
    try
    {
      KzcsDMO kdmo = new KzcsDMO();
      Vector v = new Vector();

      String sfct = "N";
      try
      {
        KzcsVO kvo = kdmo.queryAllKzcs(pk_corp, gcbm, "ctbzmrz");
        if (kvo != null)
          sfct = kvo.getCsz2();
      } catch (Exception e) {
        reportException(e);
        sfct = "N";
      }
      if ((sfct == null) || (sfct.trim().length() == 0))
        sfct = "N";
      else
        sfct = sfct.trim();
      if (sfct.equalsIgnoreCase("Y"))
        v.add(new UFBoolean(true));
      else {
        v.add(new UFBoolean(false));
      }
      String bljhshxg = "N";
      try {
        KzcsVO kvo = kdmo.queryAllKzcs(pk_corp, gcbm, "bljhshxg");
        if (kvo != null)
          bljhshxg = kvo.getCsz2();
      } catch (Exception e) {
        reportException(e);
        bljhshxg = "N";
      }
      if ((bljhshxg == null) || (bljhshxg.trim().length() == 0))
        bljhshxg = "N";
      else
        bljhshxg = bljhshxg.trim();
      if (bljhshxg.equalsIgnoreCase("Y"))
        v.add(new UFBoolean(true));
      else {
        v.add(new UFBoolean(false));
      }
      String dcsfsgck = "N";
      try {
        KzcsVO kvo = kdmo.queryAllKzcs(pk_corp, gcbm, "dcsfsgck");
        if (kvo != null)
          dcsfsgck = kvo.getCsz2();
      } catch (Exception e) {
        reportException(e);
        dcsfsgck = "N";
      }
      if ((dcsfsgck == null) || (dcsfsgck.trim().length() == 0))
        dcsfsgck = "N";
      else
        dcsfsgck = dcsfsgck.trim();
      if (dcsfsgck.equalsIgnoreCase("Y"))
        v.add(new UFBoolean(true));
      else
        v.add(new UFBoolean(false));
      return v;
    } catch (Exception e) {
      e.printStackTrace();
    throw new MMBusinessException("Remote Call:", e);
    }
  }

  public Integer savePickm(PickmVO pickmvo)
    throws MMBusinessException
  {
    if (pickmvo == null) return new Integer(0);

    try
    {
      PickmHeaderVO header = (PickmHeaderVO)pickmvo.getParentVO();
      if ((header.getBljhdh() == null) || (header.getBljhdh().toString().trim().length() == 0)) {
        String bljhdh = getBillCode("A3", header.getPk_corp(), header.getGcbm(), header.getOperid());
        ((PickmHeaderVO)pickmvo.getParentVO()).setBljhdh(bljhdh);
      }

      DisassembleDMO disDMO = new DisassembleDMO();
      ProduceCoreVO pVO = disDMO.getFjldwInfo(header.getWlbmid());
      if ((pVO == null) || (pVO.getFjldwid() == null) || (pVO.getFjldwid().trim().length() == 0)) {
        header.setFjldwid(null);
        header.setFjhwgsl(null);
        header.setFjlhsl(null);
      }
      else if (header.getLylx().intValue() == 2)
      {
        header.setFjldwid(pVO.getFjldwid());
        header.setFjlhsl(pVO.getMainmeasrate());
        header.setFjhwgsl(BatchUtil.getUFDoubleByConversion(new UFDouble[] { header.getJhwgsl(), header.getJhwgsl(), header.getFjlhsl() }, 0, true)[0]);
      }

      Integer scddlx = (Integer)header.getAttributeValue("scddlx");
      if ((scddlx != null) && (scddlx.intValue() == 8)) {
        header.setCreateItemFlag(new Integer(1));
      }

      if ((header.getCreateItemFlag() == null) || (header.getCreateItemFlag().intValue() != 1))
      {
        if ((pickmvo.getChildrenVO() == null) || (pickmvo.getChildrenVO().length <= 0))
        {
          pickmvo = createPickmVO((PickmHeaderVO)pickmvo.getParentVO());
        }
      }

      BLJHAPT apt = new BLJHAPT();
      apt.modifyATP(pickmvo);

      PickmDMO dmo = new PickmDMO();
      String[] keys = dmo.insert(pickmvo);

      if (header.getLylx().intValue() == 1)
        dmo.updateMoSfdcByPickm(keys[0], header.getLyid());
    }
    catch (Exception e) {
      reportException(e);
      if ((e instanceof MMBusinessException)) {
        throw ((MMBusinessException)e);
      }
      throw new MMBusinessException("savePickm(PickmVO pickmvo) Exception!", e);
    }
    return new Integer(0);
  }

  public void setLockedFlag(ArrayList bids, ArrayList cfreezeids)
    throws MMBusinessException
  {
    if ((bids == null) || (bids.size() == 0) || (cfreezeids == null) || (cfreezeids.size() == 0))
    {
      return;
    }String[] sbids = new String[bids.size()];
    bids.toArray(sbids);
    String[] scfreezeids = new String[cfreezeids.size()];
    cfreezeids.toArray(scfreezeids);
    try {
      PickmDMO dmo = new PickmDMO();
      dmo.setLockedFlag(sbids, scfreezeids);
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
      throw new MMBusinessException("Remote Call::", e);
    }
  }

  public ArrayList update(PickmVO pickm)
    throws MMBusinessException
  {
    String[] strRet = null;
    try
    {
      String key = ((PickmHeaderVO)pickm.getParentVO()).getPrimaryKey();
      String zt = getPickmzt(key);

      if (zt == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000047"));
      }

      if (!zt.equalsIgnoreCase(((PickmHeaderVO)pickm.getParentVO()).getZt())) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000048"));
      }

      BLJHAPT apt = new BLJHAPT();
      apt.modifyATP(pickm);

      PickmDMO dmo = new PickmDMO();
      strRet = dmo.update(pickm);

      if (((PickmHeaderVO)pickm.getParentVO()).getLylx().intValue() == 1)
        dmo.updateMoSfdcByPickm(key, ((PickmHeaderVO)pickm.getParentVO()).getLyid());
    }
    catch (Exception e)
    {
      reportException(e);
      throw new MMBusinessException("PickmBO::update(PickmVO) Exception!", e);
    }
    if ((strRet != null) && (strRet.length > 0)) {
      ArrayList al = new ArrayList(strRet.length);
      for (int i = 0; i < strRet.length; i++) {
        al.add(strRet[i]);
      }
      return al;
    }
    return null;
  }

  private String validateAccept(PickmHeaderVO header, PickmItemVO item)
    throws BusinessException
  {
    Integer lylx = header.getLylx();
    Integer bljhlx = header.getBljhlx();
    String zt = header.getZt();
    if (lylx.intValue() != 1) {
      return NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000049");
    }
    if (bljhlx.intValue() > 0) {
      return NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000050");
    }
    if (zt.equals("A")) {
      return NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000051");
    }
    if (zt.equals("C")) {
      return NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000052");
    }
    if ((item.getBlly() == null) || (item.getBlly().intValue() == 0))
      return NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000226");
    return null;
  }

  public ArrayList batchCancelCheck(PickmVO[] vos)
    throws MMBusinessException
  {
    boolean islocking = false;
    ArrayList retCancelCheck = null;
    String[] lockPks = null;
    StringBuffer errmsg = new StringBuffer("");
    String userid = ((PickmHeaderVO)vos[0].getParentVO()).getOperid();
    String logdate = ((PickmHeaderVO)vos[0].getParentVO()).getLogDate();
    try {
      Vector v = new Vector();
      PickmDMO dmo = new PickmDMO();
      PickmHeaderVO header;
      for (int i = 0; i < vos.length; i++)
      {
        header = (PickmHeaderVO)(PickmHeaderVO)vos[i].getParentVO();
        String key = header.getPk_pickmid();
        String bljhdh = header.getBljhdh();
        if ((key == null) || (key.trim().length() == 0)) {
          PickmHeaderVO[] headers = dmo.findHeaderByLyid(header.getLylx(), header.getLyid());

          if (headers == null) {
            header = null;
          }
          else {
            for (int j = 0; j < headers.length; j++)
              if (headers[j].getBljhlx().intValue() == 0) {
                header = headers[j];
                break;
              }
          }
        }
        else
        {
          header = dmo.findHeaderByPrimary(key);
        }
        if (header == null) {
          errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000053", null, new String[] { "(" + bljhdh + ")" }) + "\n");
        }
        else {
          v.add(header);
        }
      }
      PickmHeaderVO[] headers = null;
      if (v.size() > 0) {
        headers = new PickmHeaderVO[v.size()];
        v.copyInto(headers);
        lockPks = new String[v.size()];
      }
      else {
        retCancelCheck = new ArrayList();
        retCancelCheck.add(errmsg.toString());
        retCancelCheck.add(null);
       // header = retCancelCheck;
        return retCancelCheck;
      }
      v.clear();
      PickmItemVO[] items;
      for (int i = 0; i < headers.length; i++) {
        items = dmo.findItemsForHeader(headers[i].getPk_pickmid());
        PickmVO pickm = new PickmVO();
        pickm.setParentVO(headers[i]);
        pickm.setChildrenVO(items);
        v.add(pickm);
        lockPks[i] = headers[i].getPk_pickmid();
      }
      vos = new PickmVO[v.size()];
      v.copyInto(vos);

      FreeItemBO fiBO = new FreeItemBO();
      fiBO.setFreeItemVOForAggVO(vos);

      islocking = lockArray(lockPks, userid, "mm_pickm");
      if (!islocking) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000054"));
      }
      retCancelCheck = validateCancelCheck(vos);
      if (retCancelCheck != null) {
        retCancelCheck.set(0, errmsg.toString() + retCancelCheck.get(0));
       // items = retCancelCheck;
        return retCancelCheck;
      }
      if (islocking) {
        freeArray(lockPks, userid, "mm_pickm");
        islocking = false;
      }
      Object[] obj = new Object[vos.length];
      GeneralBillVO[] generalbillvo = new GeneralBillVO[vos.length];
      for (int i = 0; i < obj.length; i++) {
        generalbillvo[i] = new GeneralBillVO();
         header = (PickmHeaderVO)vos[i].getParentVO();
        obj[i] =new String[] { header.getPk_corp(), header.getPk_pickmid() };
      }
      ActionDrivenDMO actionDirvenDMO = new ActionDrivenDMO();
      actionDirvenDMO.executeBatch("OUTDELETE", "4D", logdate, generalbillvo, obj);
      islocking = lockArray(lockPks, userid, "mm_pickm");

      dmo.updateLjyfslToZero(lockPks);
      dmo.updateZt(lockPks, "A", null, null);
      String ts = dmo.getTs(((PickmHeaderVO)(PickmHeaderVO)vos[0].getParentVO()).getPk_pickmid());
      for (int i = 0; i < vos.length; i++) {
         header = (PickmHeaderVO)(PickmHeaderVO)vos[i].getParentVO();
        header.setZt("A");
        header.setShrmc(null);
        header.setShrq(null);

        header.setTs(new UFDateTime(ts));
        header.setLjyfts(MMConstant.UFZERO);

        if (vos[i].getChildrenVO() != null) {
          for (int j = 0; j < vos[i].getChildrenVO().length; j++) {
            PickmItemVO item = (PickmItemVO)vos[i].getChildrenVO()[j];
            item.setLjyfsl(MMConstant.UFZERO);
            item.setLjcksl(MMConstant.UFZERO);
            if (item.getFjldwid() != null) {
              item.setFljyfsl(MMConstant.UFZERO);
              item.setFljcksl(MMConstant.UFZERO);
            }
          }
        }
      }

      retCancelCheck = new ArrayList();
      retCancelCheck.add(null);
      retCancelCheck.add(vos);
     // i = retCancelCheck;
      return retCancelCheck;
    }
    catch (MMBusinessException e)
    {
      throw e;
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    } finally {
      if (islocking)
        freeArray(lockPks, userid, "mm_pickm"); 
    }
   // throw localObject;
  }

  public ArrayList batchCancelClose(PickmVO[] vos)
    throws MMBusinessException
  {
    boolean islocking = false;
    ArrayList retCancelClose = null;
    String[] lockPks = null;
    StringBuffer errmsg = new StringBuffer("");
    String userid = ((PickmHeaderVO)vos[0].getParentVO()).getOperid();
    String logdate = ((PickmHeaderVO)vos[0].getParentVO()).getLogDate();
    try {
      Vector v = new Vector();
      PickmDMO dmo = new PickmDMO();
      PickmHeaderVO header;
      for (int i = 0; i < vos.length; i++)
      {
        header = (PickmHeaderVO)(PickmHeaderVO)vos[i].getParentVO();
        String key = header.getPk_pickmid();
        String bljhdh = header.getBljhdh();
        if ((key == null) || (key.trim().length() == 0)) {
          PickmHeaderVO[] headers = dmo.findHeaderByLyid(header.getLylx(), header.getLyid());

          if (headers == null) {
            header = null;
          }
          else {
            for (int j = 0; j < headers.length; j++)
              if (headers[j].getBljhlx().intValue() == 0) {
                header = headers[j];
                break;
              }
          }
        }
        else
        {
          header = dmo.findHeaderByPrimary(key);
        }
        if (header == null) {
          errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000053", null, new String[] { "(" + bljhdh + ")" }) + "\n");
        }
        else {
          v.add(header);
        }
      }
      PickmHeaderVO[] headers = null;
      if (v.size() > 0) {
        headers = new PickmHeaderVO[v.size()];
        v.copyInto(headers);
        lockPks = new String[v.size()];
      }
      else {
        retCancelClose = new ArrayList();
        retCancelClose.add(errmsg.toString());
        retCancelClose.add(null);
        //header = retCancelClose;
        return retCancelClose;
      }
      v.clear();
      PickmItemVO[] items;
      for (int i = 0; i < headers.length; i++) {
        items = dmo.findItemsForHeader(headers[i].getPk_pickmid());
        PickmVO pickm = new PickmVO();
        pickm.setParentVO(headers[i]);
        pickm.setChildrenVO(items);
        v.add(pickm);
        lockPks[i] = headers[i].getPk_pickmid();
      }
      vos = new PickmVO[v.size()];
      v.copyInto(vos);

      FreeItemBO fiBO = new FreeItemBO();
      fiBO.setFreeItemVOForAggVO(vos);

      islocking = lockArray(lockPks, userid, "mm_pickm");
      if (!islocking) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000055"));
      }
      retCancelClose = validateCancelClose(vos);
      if (retCancelClose != null) {
        retCancelClose.set(0, errmsg.toString() + retCancelClose.get(0));
      //  items = retCancelClose;
        return retCancelClose;
      }
      BLJHAPT bljhapt = new BLJHAPT();
      for (int i = 0; i < vos.length; i++)
      {
         header = (PickmHeaderVO)(PickmHeaderVO)vos[i].getParentVO();
        dmo.updateZt(header.getPk_pickmid(), "B");
        header.setZt("B");

        String ts = dmo.getTs(header.getPk_pickmid());
        header.setTs(new UFDateTime(ts));

        bljhapt.modifyATPWhenOpenBill(vos[i]);
      }

      retCancelClose = new ArrayList();
      retCancelClose.add(null);
      retCancelClose.add(vos);
     // i = retCancelClose;
      return retCancelClose;
    }
    catch (MMBusinessException e)
    {
      throw e;
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    } finally {
      if (islocking)
        freeArray(lockPks, userid, "mm_pickm"); 
    }
   // throw localObject;
  }

  public ArrayList batchCheck(PickmVO[] vos)
    throws MMBusinessException
  {
    boolean islocking = false;
    ArrayList retCheck = null;
    String[] lockPks = null;
    StringBuffer errmsg = new StringBuffer("");
    String userid = ((PickmHeaderVO)vos[0].getParentVO()).getOperid();
    String shrmc = ((PickmHeaderVO)vos[0].getParentVO()).getShrmc();
    String shrq = ((PickmHeaderVO)vos[0].getParentVO()).getLogDate();
    try {
      Vector v = new Vector();
      PickmDMO dmo = new PickmDMO();
      PickmHeaderVO header;
      for (int i = 0; i < vos.length; i++)
      {
        header = (PickmHeaderVO)(PickmHeaderVO)vos[i].getParentVO();
        String key = header.getPk_pickmid();
        String bljhdh = header.getBljhdh();
        if ((key == null) || (key.trim().length() == 0)) {
          PickmHeaderVO[] headers = dmo.findHeaderByLyid(header.getLylx(), header.getLyid());

          if (headers == null) {
            header = null;
          }
          else {
            for (int j = 0; j < headers.length; j++)
              if (headers[j].getBljhlx().intValue() == 0) {
                header = headers[j];
                break;
              }
          }
        }
        else
        {
          header = dmo.findHeaderByPrimary(key);
        }
        if (header == null) {
          errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000053", null, new String[] { "(" + bljhdh + ")" }) + "\n");
        }
        else {
          v.add(header);
        }
      }
      PickmHeaderVO[] headers = null;
      if (v.size() > 0) {
        headers = new PickmHeaderVO[v.size()];
        v.copyInto(headers);
        lockPks = new String[v.size()];
      }
      else {
        retCheck = new ArrayList();
        retCheck.add(errmsg.toString());
        retCheck.add(null);
        //header = retCheck;
        return retCheck;
      }
      v.clear();
      PickmItemVO[] items;
      for (int i = 0; i < headers.length; i++) {
        items = dmo.findItemsForHeader(headers[i].getPk_pickmid());
        PickmVO pickm = new PickmVO();
        pickm.setParentVO(headers[i]);
        pickm.setChildrenVO(items);
        v.add(pickm);
        lockPks[i] = headers[i].getPk_pickmid();
      }
      vos = new PickmVO[v.size()];
      v.copyInto(vos);

      FreeItemBO fiBO = new FreeItemBO();
      fiBO.setFreeItemVOForAggVO(vos);

      islocking = lockArray(lockPks, userid, "mm_pickm");
      if (!islocking) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000056"));
      }
      retCheck = validateCheck(vos, lockPks);
      if (retCheck != null) {
        retCheck.set(0, errmsg.toString() + retCheck.get(0));
        //items = retCheck;
        return retCheck;
      }
      dmo.updateZt(lockPks, "B", shrmc, shrq.toString());
      String ts = dmo.getTs(((PickmHeaderVO)(PickmHeaderVO)vos[0].getParentVO()).getPk_pickmid());
      for (int i = 0; i < vos.length; i++) {
         header = (PickmHeaderVO)(PickmHeaderVO)vos[i].getParentVO();
        header.setZt("B");
        header.setShrmc(shrmc);
        header.setShrq(new UFDate(shrq));
        header.setTs(new UFDateTime(ts));
      }

      retCheck = new ArrayList();
      retCheck.add(null);
      retCheck.add(vos);
     // i = retCheck;
      return retCheck;
    }
    catch (MMBusinessException e)
    {
      reportException(e);
      throw e;
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    } finally {
      if (islocking)
        freeArray(lockPks, userid, "mm_pickm"); 
    }
    //throw localObject;
  }

  public ArrayList batchClose(PickmVO[] vos)
    throws MMBusinessException
  {
    boolean islocking = false;
    boolean isOutSource = false;
    String[] lockPks = null;
    ArrayList retClose = null;
    StringBuffer errmsg = new StringBuffer("");
    String userid = ((PickmHeaderVO)vos[0].getParentVO()).getOperid();
    String logdate = ((PickmHeaderVO)vos[0].getParentVO()).getLogDate();
    try {
      Vector v = new Vector();
      PickmDMO dmo = new PickmDMO();
      PickmHeaderVO header;
      for (int i = 0; i < vos.length; i++) {
        header = (PickmHeaderVO)(PickmHeaderVO)vos[i].getParentVO();
        String key = header.getPk_pickmid();
        String bljhdh = header.getBljhdh();
        if ((key == null) || (key.trim().length() == 0)) {
          isOutSource = true;
          PickmHeaderVO[] headers = dmo.findHeaderByLyid(header.getLylx(), header.getLyid());
          if (headers == null) {
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000053", null, new String[] { "(" + bljhdh + ")" }) + "\n");
          }
          else {
            for (int j = 0; j < headers.length; j++) {
              if (headers[j].getBljhlx().intValue() < 10)
                v.add(headers[j]);
            }
          }
        }
        else
        {
          isOutSource = false;
          header = dmo.findHeaderByPrimary(key);
          if (header == null) {
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000053", null, new String[] { "(" + bljhdh + ")" }) + "\n");
          }
          else {
            v.add(header);
          }
        }
      }
      PickmHeaderVO[] headers = null;
      if (v.size() > 0) {
        headers = new PickmHeaderVO[v.size()];
        v.copyInto(headers);
        lockPks = new String[v.size()];
      }
      else {
        retClose = new ArrayList();
        retClose.add(errmsg.toString());
        retClose.add(null);
       // header = retClose;
        return retClose;
      }
      v.clear();
      PickmItemVO[] items;
      for (int i = 0; i < headers.length; i++) {
        items = dmo.findItemsForHeader(headers[i].getPk_pickmid());
        PickmVO pickm = new PickmVO();
        pickm.setParentVO(headers[i]);
        pickm.setChildrenVO(items);
        v.add(pickm);
        lockPks[i] = headers[i].getPk_pickmid();
      }
      vos = new PickmVO[v.size()];
      v.copyInto(vos);

      FreeItemBO fiBO = new FreeItemBO();
      fiBO.setFreeItemVOForAggVO(vos);

      islocking = lockArray(lockPks, userid, "mm_pickm");
      if (!islocking) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000057"));
      }
      retClose = validateClose(vos, isOutSource);
      if (retClose != null) {
        retClose.set(0, errmsg.toString() + retClose.get(0));
        //items = retClose;
        return retClose;
      }
      BLJHAPT bljhapt = new BLJHAPT();
      for (int i = 0; i < vos.length; i++)
      {
        bljhapt.modifyATPWhenCloseBill(vos[i]);

         header = (PickmHeaderVO)(PickmHeaderVO)vos[i].getParentVO();
        dmo.updateZt(header.getPk_pickmid(), "C");
        header.setZt("C");

        String ts = dmo.getTs(header.getPk_pickmid());
        header.setTs(new UFDateTime(ts));
      }

      retClose = new ArrayList();
      retClose.add(null);
      retClose.add(vos);
     // i = retClose;
      return retClose;
    }
    catch (MMBusinessException e)
    {
      throw e;
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    } finally {
      if (islocking)
        freeArray(lockPks, userid, "mm_pickm"); 
    }
    //throw localObject;
  }

  public void batchSavePickm(PickmVO[] pickmvos)
    throws MMBusinessException
  {
    for (int i = 0; i < pickmvos.length; i++)
      savePickm(pickmvos[i]);
  }

  public InvreplVO[] findTdlInfo(PickmVO vo)
    throws MMBusinessException
  {
    try
    {
      if ((vo == null) || (vo.getParentVO() == null) || (vo.getChildrenVO() == null))
        return null;
      PickmHeaderVO header = (PickmHeaderVO)vo.getParentVO();
      String pk_corp = header.getPk_corp();
      String gcbm = header.getGcbm();
      Vector v = new Vector();
      InvreplVO[] invrepls = null;
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        invrepls = null;
        PickmItemVO item = (PickmItemVO)vo.getChildrenVO()[i];
        Integer tdbz = item.getTdbz();
        if ((tdbz != null) && (tdbz.intValue() > 0)) {
          String bomver = item.getBomver();
          if (bomver != null) {
            invrepls = findByBtdwl(pk_corp, gcbm, item.getFxid(), item.getWlbmid(), item.getFlrq(), bomver, header.getFreeitemVO(), item.getFreeitemVO());
          }
          if ((invrepls != null) && (invrepls.length > 0)) {
            invrepls[0].setBtdwlbm(item.getClbm());
            invrepls[0].setBtdwlmc(item.getClmc());
            invrepls[0].setBtdwlgg(item.getClgg());
            invrepls[0].setBtdwlxh(item.getClxh());

            for (int j = 0; j < invrepls.length; j++) {
              invrepls[j].setRq(item.getFlrq().toString());
              v.add(invrepls[j]);
            }
          }
        }
      }
      if (v.size() > 0) {
        invrepls = new InvreplVO[v.size()];
        v.copyInto(invrepls);

        String[] pk_produces = new String[v.size()];
        String[] dates = new String[v.size()];
        String[] ckcks = new String[v.size()];
        String[] rkcks = new String[v.size()];
        FreeItemVO[] freeitemvo = new FreeItemVO[v.size()];
        for (int i = 0; i < invrepls.length; i++) {
          pk_produces[i] = invrepls[i].getTdwlproduceid();
          if (invrepls[i].getRq().compareTo(header.getDate()) > 0)
            dates[i] = invrepls[i].getRq();
          else
            dates[i] = header.getDate();
          freeitemvo[i] = invrepls[i].getFreeitemVO();
        }

        UFDouble[] atp = (UFDouble[])(UFDouble[])getATPNum(pk_corp, gcbm, ckcks, rkcks, pk_produces, dates, freeitemvo, null).get(0);
        for (int i = 0; i < invrepls.length; i++) {
          invrepls[i].setKyl(atp[i]);
        }
        return invrepls;
      }
    } catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("Remote Call:", e);
    }
    return null;
  }

  public PickmVO unAcceptFromSuborder(PickmHeaderVO header, PickmItemVO item, HandandtakeUnitVO[] jjdvos)
    throws BusinessException, MMBusinessException
  {
    MMLockBO lock = null;
    try
    {
      lock = lock(header.getPk_pickmid(), header.getOperid(), header.getTs());

      String errmsg = validateUnAccept(header, item);
      if (errmsg != null) {
        throw new BusinessException(errmsg);
      }
      Vector v1 = new Vector();
      Vector v2 = new Vector();
      Vector v3 = new Vector();
      Vector v4 = new Vector();
      Vector v5 = new Vector();
      for (int i = 0; i < jjdvos.length; i++) {
        if (!jjdvos[i].getZt().equals("B")) {
          continue;
        }
        v1.add(jjdvos[i].getJsdjbid());
        v2.add(MMConstant.UFZERO.sub(jjdvos[i].getYjsl()));
        if (jjdvos[i].getFyjsl() != null)
          v3.add(MMConstant.UFZERO.sub(jjdvos[i].getFyjsl()));
        v4.add(jjdvos[i].getFjldwid());
        v5.add(jjdvos[i].getPch());
        jjdvos[i].setJssl(null);
        jjdvos[i].setFjssl(null);
        jjdvos[i].setJsdjhid(null);
        jjdvos[i].setJsdjbid(null);
        if ((jjdvos[i].getJsdwid() != null) && (jjdvos[i].getJsdwid().length() > 0)) {
          jjdvos[i].setJsdjh(null);
          jjdvos[i].setJsdjlx(null);
          jjdvos[i].setJsscddh(null);
          jjdvos[i].setJsscddid(null);
        }
        jjdvos[i].setZt("A");
        jjdvos[i].setJsr(null);
        jjdvos[i].setJsrq(null);
      }

      if (v1.size() > 0)
      {
        HandandtakeBO handandtakebo = new HandandtakeBO();
        handandtakebo.unAccept(jjdvos, header.getOperid());

        String[] pk_pickmids = new String[v1.size()];
        UFDouble[] sls = new UFDouble[v2.size()];
        UFDouble[] fsls = new UFDouble[v2.size()];
        String[] fjldwid = new String[v2.size()];
        String[] pch = new String[v2.size()];
        v1.copyInto(pk_pickmids);
        v2.copyInto(sls);
        v3.copyInto(fsls);
        v4.copyInto(fjldwid);
        v5.copyInto(pch);
        nc.bs.mm.pub.pub1030.PickmDMO dmo = new nc.bs.mm.pub.pub1030.PickmDMO();
        dmo.setInfo(header.getPk_corp(), header.getGcbm(), header.getPk_pickmid(), pk_pickmids, sls, fsls, fjldwid, pch);

        PickmDMO dmo1 = new PickmDMO();
        PickmVO vo = dmo1.findByPrimary(header.getPk_pickmid());
        PickmVO localPickmVO1 = vo;
        return localPickmVO1;
      }
    }
    catch (MMBusinessException e)
    {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new MMBusinessException(e.getMessage());
    }
    finally
    {
      if (lock != null) {
        try {
          freelock(lock, header.getPk_pickmid(), header.getOperid());
        }
        catch (Exception e) {
          throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
        }
      }
    }
    return null;
  }

  private ArrayList validateCancelCheck(PickmVO[] vos)
    throws BusinessException
  {
    StringBuffer errmsg = new StringBuffer("");
    Vector v = new Vector();
    try {
      PickmDMO dmo = new PickmDMO();
      if (vos != null)
        for (int i = 0; i < vos.length; i++) {
          boolean isOk = true;
          PickmHeaderVO header = (PickmHeaderVO)vos[i].getParentVO();

          PickmItemVO[] items = (PickmItemVO[])(PickmItemVO[])vos[i].getChildrenVO();

          Integer bljhlx = header.getBljhlx();
          String bljhdh = header.getBljhdh();

          if (!header.getZt().equalsIgnoreCase("B")) {
            isOk = false;
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000058", null, new String[] { "(" + bljhdh + ")" }));
          }

          if (header.getLylx().intValue() == 1) {
            String mozt = dmo.getMozt(header.getLyid());
            if (bljhlx.intValue() < 10) {
              if (mozt == null) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000059", null, new String[] { "(" + bljhdh + ")" }));
              }
              else if ((bljhlx.intValue() == 0) && (!mozt.equals("A"))) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000060", null, new String[] { "(" + bljhdh + ")" }));
              }
              else if ((bljhlx.intValue() > 0) && (mozt.compareTo("B") == 1)) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000061", null, new String[] { "(" + bljhdh + ")" }));
              }
            }
          }

          if (items != null) {
            for (int j = 0; j < items.length; j++) {
              PickmItemVO item = items[j];

              if ((item.getLjcksl() != null) && (item.getLjcksl().doubleValue() > 0.0D)) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000062", null, new String[] { "(" + bljhdh + ")" }));
                break;
              }

              if (item.getCfreezeid() != null) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000063", null, new String[] { "(" + bljhdh + ")" }));
                break;
              }
            }
          }
          if (isOk)
            v.add(vos[i]);
        }
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    if (errmsg.toString().equals("")) {
      return null;
    }
    ArrayList retAl = new ArrayList();
    retAl.add(errmsg.toString());
    if (v.size() > 0) {
      PickmVO[] vos1 = new PickmVO[v.size()];
      v.copyInto(vos1);
      retAl.add(vos1);
    }
    else {
      retAl.add(null);
    }return retAl;
  }

  private ArrayList validateCancelClose(PickmVO[] vos)
    throws BusinessException
  {
    StringBuffer errmsg = new StringBuffer("");
    Vector v = new Vector();
    try {
      PickmDMO dmo = new PickmDMO();
      if (vos != null)
        for (int i = 0; i < vos.length; i++) {
          boolean isOk = true;
          PickmHeaderVO header = (PickmHeaderVO)vos[i].getParentVO();

          PickmItemVO[] items = (PickmItemVO[])(PickmItemVO[])vos[i].getChildrenVO();

          Integer bljhlx = header.getBljhlx();
          String bljhdh = header.getBljhdh();
          Integer lylx = header.getLylx();
          String lyid = header.getLyid();

          if (!header.getZt().equalsIgnoreCase("C")) {
            isOk = false;
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000064", null, new String[] { "(" + bljhdh + ")" }) + "\n");
          }

          if (lylx.intValue() == 1) {
            String mozt = dmo.getMozt(lyid);
            if (bljhlx.intValue() < 10) {
              if (mozt == null) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000065", null, new String[] { "(" + bljhdh + ")" }) + "\n");
              }
              else if (mozt.compareTo("B") > 0) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000066", null, new String[] { "(" + bljhdh + ")" }) + "\n");
              }
            }
          }

          if (isOk)
            v.add(vos[i]);
        }
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    if (errmsg.toString().equals("")) {
      return null;
    }
    ArrayList retAl = new ArrayList();
    retAl.add(errmsg.toString());
    if (v.size() > 0) {
      PickmVO[] vos1 = new PickmVO[v.size()];

      v.copyInto(vos1);
      retAl.add(vos1);
    } else {
      retAl.add(null);
    }return retAl;
  }

  private ArrayList validateCheck(PickmVO[] vos, String[] pk_pickms)
    throws BusinessException
  {
    StringBuffer errmsg = new StringBuffer("");
    Vector v = new Vector();
    try
    {
      PickmDMO dmo = new PickmDMO();
      if (vos != null) {
        HashMap hm = new HashMap();

        Vector v1 = dmo.checkWlfcMessage(pk_pickms);
        if (v1.size() > 0) {
          for (int i = 0; i < v1.size() / 3; i++) {
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000229", null, new String[] { "(" + v1.get(i) + ")", "(" + v1.get(i + 1) + "," + v1.get(i + 2) + ")" }) + "\n");
            if (!hm.containsKey(v1.get(i)))
              hm.put(v1.get(i), "bljhdh");
          }
        }
        v1.clear();

        if (v1.size() > 0) {
          for (int i = 0; i < v1.size() / 3; i++) {
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000230", null, new String[] { "(" + v1.get(i) + ")", "(" + v1.get(i + 1) + "," + v1.get(i + 2) + ")" }) + "\n");
            if (!hm.containsKey(v1.get(i)))
              hm.put(v1.get(i), "bljhdh");
          }
        }
        v1.clear();
        for (int i = 0; i < vos.length; i++) {
          boolean isOk = true;
          PickmHeaderVO header = (PickmHeaderVO)vos[i].getParentVO();
          String pk_corp=header.getPk_corp();
          PickmItemVO[] items = (PickmItemVO[])(PickmItemVO[])vos[i].getChildrenVO();

          Integer bljhlx = header.getBljhlx();
          String bljhdh = header.getBljhdh();
          Integer lylx = header.getLylx();
          String lyid = header.getLyid();
          UFBoolean mo_sfdc = null;
          if (hm.containsKey(bljhdh)) {
            isOk = false;
          }
          if(!getParentCorp(pk_corp).equals("10395"))
          {
        	  if (FreeItemVOTookKit.checkNullForFreeItem(vos[i])) {
                isOk = false;
              errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000225", null, new String[] { "(" + bljhdh + ")" }) + "\n");
        	  }
          }
          if (lylx.intValue() == 1) {
            mo_sfdc = dmo.getMosfdc(lyid);
          }

          if (!header.getZt().equals("A")) {
            isOk = false;
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000067", null, new String[] { "(" + bljhdh + ")" }) + "\n");
          }

          if ((header.getJhwgsl() == null) || (header.getJhwgsl().doubleValue() <= 0.0D)) {
            isOk = false;
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000068", null, new String[] { "(" + bljhdh + ")" }) + "\n");
          }

          if (items != null) {
            for (int j = 0; j < items.length; j++) {
              PickmItemVO item = items[j];

              if ((item.getCkckid() == null) || (item.getCkckid().trim().length() == 0)) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000069", null, new String[] { "(" + bljhdh + ")" }) + "\n");
              }

              if ((item.getDwde() == null) || (item.getDwde().doubleValue() < 0.0D)) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000070", null, new String[] { "(" + bljhdh + ")" }) + "\n");
              }

              if (item.getJhcksl() == null) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000071", null, new String[] { "(" + bljhdh + ")" }) + "\n");
              }
              else if ((bljhlx.intValue() < 10) && (item.getJhcksl().compareTo(new UFDouble(0)) < 0))
              {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000072", null, new String[] { "(" + bljhdh + ")" }) + "\n");
              }
              else if ((bljhlx.intValue() >= 10) && (item.getJhcksl().compareTo(new UFDouble(0)) > 0)) {
                isOk = false;
                errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000073", null, new String[] { "(" + bljhdh + ")" }) + "\n");
              }

              if ((lylx.intValue() == 2) || (!item.getSfdc().booleanValue()) || 
                (mo_sfdc == null) || (mo_sfdc.booleanValue())) continue;
              isOk = false;
              errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000074", null, new String[] { "(" + bljhdh + ")" }) + "\n");
            }

          }

          if (isOk)
            v.add(vos[i]);
        }
      }
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    if (errmsg.toString().equals("")) {
      return null;
    }
    ArrayList retAl = new ArrayList();
    retAl.add(errmsg.toString());
    if (v.size() > 0) {
      PickmVO[] vos1 = new PickmVO[v.size()];
      v.copyInto(vos1);
      retAl.add(vos1);
    }
    else {
      retAl.add(null);
    }return retAl;
  }

  private ArrayList validateClose(PickmVO[] vos, boolean isSourceOutBill)
    throws BusinessException
  {
    StringBuffer errmsg = new StringBuffer("");
    Vector v = new Vector();
    try {
      if (vos != null)
        for (int i = 0; i < vos.length; i++) {
          boolean isOk = true;
          PickmHeaderVO header = (PickmHeaderVO)vos[i].getParentVO();

          PickmItemVO[] items = (PickmItemVO[])(PickmItemVO[])vos[i].getChildrenVO();

          Integer bljhlx = header.getBljhlx();
          String bljhdh = header.getBljhdh();
          String zt = header.getZt();

          if (zt.equals("A")) {
            isOk = false;
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000031", null, new String[] { "(" + bljhdh + ")" }) + "\n");
          }
          else if ((zt.equals("C")) && 
            (!isSourceOutBill)) {
            isOk = false;
            errmsg.append(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000030", null, new String[] { "(" + bljhdh + ")" }) + "\n");
          }

          if (isOk)
            v.add(vos[i]);
        }
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    if (errmsg.toString().equals("")) {
      return null;
    }
    ArrayList retAl = new ArrayList();
    retAl.add(errmsg.toString());
    if (v.size() > 0) {
      PickmVO[] vos1 = new PickmVO[v.size()];
      v.copyInto(vos1);
      retAl.add(vos1);
    }
    else {
      retAl.add(null);
    }return retAl;
  }

  private String validateUnAccept(PickmHeaderVO header, PickmItemVO item)
    throws BusinessException
  {
    Integer lylx = header.getLylx();
    Integer bljhlx = header.getBljhlx();
    String zt = header.getZt();
    if (zt.equals("A")) {
      return NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000079");
    }
    if (zt.equals("C")) {
      return NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000080");
    }
    if ((item.getBlly() == null) || (item.getBlly().intValue() == 0)) {
      return NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000228");
    }
    return null;
  }
  public String getParentCorp(String key) throws BusinessException
  {
	  String ParentCorp = new String();
	
		//String ParentCorp = new String();
		CorpBO corpbo=new CorpBO();
		try {
			CorpVO  corpVO = corpbo.findByPrimaryKey(key);
			ParentCorp = corpVO.fathercorp;
			CorpVO f_corpVO=corpbo.findByPrimaryKey(ParentCorp);
			ParentCorp=f_corpVO.unitcode;
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return ParentCorp;

	  return ParentCorp;
  }
  
  /**
   * @author shikun
   * 功能：制盖在进行修订保存时，如果备料计划没有进行任何出库，则可以进行备料计划修改
   * */
  public void ModifyLydj2(PickmVO pickm, UFBoolean sfcxsc)
    throws MMBusinessException
  {
    PickmHeaderVO header = (PickmHeaderVO)pickm.getParentVO();
    if (header == null) return; header.setShrmc(null);
    header.setShrq(null);
    String operid = header.getOperid();
    MMLockBO lock = null;
    try
    {
      if (sfcxsc.booleanValue())
      {
        String bljhdh = getBillCode("A3", header.getPk_corp(), header.getGcbm(), header.getOperid());
        header.setBljhdh(bljhdh);
        //制盖删除备料计划，审核态也可以删除，只要没有进行出库
        delete2(operid, header.getLylx(), new String[] { header.getLyid() });
        savePickm(pickm);
      }
      else {
        PickmDMO dmo = new PickmDMO();
        PickmHeaderVO[] headers = dmo.findHeaderByLyid(header.getLylx(), header.getLyid());
        if (headers == null) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000045"));
        }
        for (int i = 0; i < headers.length; i++)
        {
          String zt = headers[i].getZt();
          if (!zt.equals("A")) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000046", null, new String[] { "(" + headers[i].getBljhdh() + ")" }));
          }

          if (headers[i].getBljhlx().intValue() == 0) {
            header.setPrimaryKey(headers[i].getPrimaryKey());
            header.setSfct(headers[i].getSfct());
          }

        }

        lock = lock(header.getPrimaryKey(), operid, header.getTs());

        dmo.updatePickmItemWhenMOChanged(header.getPk_pickmid(), header.getAttributeValue("gzzxid") == null ? null : header.getAttributeValue("gzzxid").toString());

        dmo.updateHeader(header);
      }
    }
    catch (MMBusinessException e) {
      reportException(e);
      throw e;
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
    }
    finally
    {
      try {
        freelock(lock, header.getPrimaryKey(), operid);
      }
      catch (Exception e) {
        throw new MMBusinessException("Remote Call:", new BusinessException(e.getMessage()));
      }
    }
  }

  /**
   * @author shikun
   * 制盖删除备料计划，审核态也可以删除，只要没有进行出库
   * */
  public void delete2(String operid, Integer lylx, String[] lyids)
    throws BusinessException, MMBusinessException
  {
    boolean isLockSussess = false;
    String[] pickmids = null;
    try {
      PickmDMO dmo = new PickmDMO();
      PickmVO[] pickms = dmo.findByLyids(lylx, lyids);
      if ((pickms == null) || (pickms.length <= 0))
        return;
      Vector v = new Vector();
      Vector v1 = new Vector();
      for (int i = 0; i < pickms.length; i++) {
        if (pickms[i] != null) {
          v.add(((PickmHeaderVO)pickms[i].getParentVO()).getPrimaryKey());
          v1.add(pickms[i]);
        }
      }
      if (v.size() > 0) {
        pickmids = new String[v.size()];
        pickms = new PickmVO[v.size()];
        v.copyInto(pickmids);
        v1.copyInto(pickms);
      }
      isLockSussess = lockArray(pickmids, operid, "mm_pickm");
      if (!isLockSussess) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000034"));
      }

      boolean sfcw = false;
      String errMsg = "";

      //begin 制盖删除备料计划，审核态也可以删除，只要没有进行出库
//      for (int i = 0; i < pickms.length; i++) {
//        if (!((PickmHeaderVO)pickms[i].getParentVO()).getZt().equals("A")) {
//          errMsg = errMsg + ((PickmHeaderVO)pickms[i].getParentVO()).getBljhdh() + ",";
//          sfcw = true;
//        }
//      }
      //end 制盖删除备料计划，审核态也可以删除，只要没有进行出库
      if (sfcw)
      {
        if (errMsg.length() > 1) {
          errMsg = NCLangResOnserver.getInstance().getStrByID("50082010", "UPP50082010-000035", null, new String[] { "(" + errMsg.substring(0, errMsg.length() - 1) + ")" });
        }
        throw new BusinessException(errMsg);
      }

      BLJHAPT apt = new BLJHAPT();
      for (int i = 0; i < pickms.length; i++) {
        apt.modifyATPWhenDeleteBill(pickms[i]);
        dmo.delete(((PickmHeaderVO)pickms[i].getParentVO()).getPrimaryKey());
      }

      for (int i = 0; i < pickmids.length; i++)
        returnBillCodeOnDelete("A3", (String)pickms[0].getParentVO().getAttributeValue("pk_corp"), (String)pickms[0].getParentVO().getAttributeValue("gcbm"), operid, (String)pickms[i].getParentVO().getAttributeValue("bljhdh"));
    }
    catch (Exception e) {
      reportException(e);
      throw new MMBusinessException("delete(Integer lylx,String[] lyid)", e);
    }
    finally {
      if (isLockSussess)
        freeArray(pickmids, operid, "mm_pickm");
    }
  }
  
}