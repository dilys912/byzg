package nc.bs.pu.pub;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.NamingException;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pu.pr.PraybillDMO;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.msg.PFMessageBO;
import nc.bs.pub.para.GlParaDMO;
import nc.bs.pub.para.SysInitDMO;
import nc.bs.scm.ic.freeitem.FreeItemDMO;
import nc.bs.scm.pub.ScmPubDMO;
import nc.bs.scm.pub.TempTableDMO;
import nc.itf.ic.service.IICPub_GeneralBillDMO;
import nc.itf.pu.inter.IPuToSc_PubDMO;
import nc.itf.pu.pub.fw.LockTool;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.bd.access.AccessorManager;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.access.IBDAccessor;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.po.OrderCloseItemVO;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pp.ask.AskbillHeaderVO;
import nc.vo.pp.ask.AskbillVO;
import nc.vo.pp.ask.PriceauditHeaderVO;
import nc.vo.pp.ask.PriceauditVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.msg.CommonMessageVO;
import nc.vo.pub.msg.UserNameObject;
import nc.vo.pub.para.SysInitVO;
import nc.vo.rc.receive.ArriveorderHeaderVO;
import nc.vo.rc.receive.ArriveorderVO;
import nc.vo.sc.adjust.AdjustbillHeaderVO;
import nc.vo.sc.adjust.AdjustbillVO;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;

public class PubDMO extends DataManageObject
  implements IPuToSc_PubDMO
{
  private static Hashtable m_hasDigitRMB = null;

  public PubDMO()
    throws NamingException, nc.bs.pub.SystemException
  {
  }

  public PubDMO(String dbName)
    throws NamingException, nc.bs.pub.SystemException
  {
    super(dbName);
  }

  public void checkBusiAndInvs(AggregatedValueObject[] vos)
    throws Exception
  {
    try
    {
      if ((vos == null) || (vos.length > 0)) return;

      String[] cbiztypes = new String[vos.length];
      Vector tempV = new Vector();
      String cbiztype = null;

      for (int i = 0; i < vos.length; i++) {
        cbiztype = vos[i].getParentVO().getAttributeValue("cbiztype").toString();
        if ((cbiztype == null) || (cbiztype.length() <= 0)) continue; tempV.addElement(cbiztype);
      }
      if (tempV.size() == 0) return;
      cbiztypes = new String[tempV.size()];
      tempV.copyInto(cbiztypes);

      Hashtable rules = fetchArrayValue("bd_busitype", "verifyrule", "pk_busitype", cbiztypes);

      for (int i = 0; i < vos.length; i++) {
        cbiztype = vos[i].getParentVO().getAttributeValue("cbiztype").toString();
        String rule = null;
        if ((cbiztype != null) && (cbiztype.length() > 0) && (rules.containsKey(cbiztype))) {
          rule = rules.get(cbiztype).toString().trim();
        }

        if ((!rule.equalsIgnoreCase("V")) && (!rule.equalsIgnoreCase("S")))
          continue;
        CircularlyAccessibleValueObject[] childs = vos[i].getChildrenVO();

        if ((childs != null) && (childs.length != 0)) {
          String[] cmangids = new String[childs.length];
          for (int j = 0; j < childs.length; j++) {
            cmangids[j] = childs[j].getAttributeValue("cmangid").toString();
          }

          String[] fields = { "consumesettleflag", "sellproxyflag" };
          Object[][] props = queryArrayValue("bd_invmandoc", "pk_invmandoc", fields, cmangids);

          if ((rule.equalsIgnoreCase("V")) && 
            (props != null) && (props.length > 0)) {
            for (int t = 0; t < props.length; t++)
            {
              if ((props[t] != null) && (
                (props[t][0] == null) || (!props[t][0].toString().equalsIgnoreCase("Y")))) throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000207"));
            }

          }

          if ((!rule.equalsIgnoreCase("S")) || 
            (props == null) || (props.length <= 0)) continue;
          for (int t = 0; t < props.length; t++)
          {
            if ((props[t] != null) && (
              (props[t][1] == null) || (!props[t][1].toString().equalsIgnoreCase("Y")))) throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000207"));
          }
        }

      }

    }
    catch (BusinessException e)
    {
      SCMEnv.out(e);
      throw e;
    }
  }

  /** @deprecated */
  public UFBoolean checkTsChanged(String sBillType, String[] saBillid, String[] saTsh, String[] saBill_bid, String[] saTsb, String[] saBill_bbid, String[] saTsBb)
    throws Exception
  {
    boolean[] b = { false, false, false };

    if ((saBillid != null) && (saBillid.length > 0)) {
      b[0] = true;
      if ((saTsh == null) || (saTsh.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000116"));
      }
    }
    if ((saBill_bid != null) && (saBill_bid.length > 0)) {
      b[1] = true;
      if ((saTsb == null) || (saTsb.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000116"));
      }
    }
    if ((saBill_bbid != null) && (saBill_bbid.length > 0)) {
      b[2] = true;
      if ((saTsBb == null) || (saTsBb.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000116"));
      }
    }

    Object[] saObj = queryHBTsArrayByHBIDArray(sBillType, saBillid, saBill_bid, saBill_bbid);

    if ((saObj == null) || (saObj.length <= 0))
    {
      SCMEnv.out("没有查询到相应的时间戳，暂不处理，go on！");
      return new UFBoolean(true);
    }

    String[] saTmp = null;
    if (b[0]) {
 // if (b[0] != 0) {
      saTmp = (String[])(String[])saObj[0];
      if ((saTmp == null) || (saTmp.length <= 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
      for (int i = 0; i < saTmp.length; i++) {
        if (!saTsh[i].equals(saTmp[i])) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
        }
      }
    }
    if (b[1]) {
 // if (b[1] != 0) {
      saTmp = (String[])(String[])saObj[1];
      if ((saTmp == null) || (saTmp.length <= 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
      for (int i = 0; i < saTmp.length; i++) {
        if (!saTsb[i].equals(saTmp[i])) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
        }
      }
    }
    
    if (b[2]) {
 // if (b[2] != 0) {
      saTmp = (String[])(String[])saObj[2];
      if ((saTmp == null) || (saTmp.length <= 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
      for (int i = 0; i < saTmp.length; i++) {
        if (!saTsBb[i].equals(saTmp[i])) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
        }
      }
    }
    return new UFBoolean(true);
  }

  public UFBoolean checkTsNoChanged(String sBillType, String[] saBillid, String[] saTsh, String[] saBill_bid, String[] saTsb, String[] saBill_bbid, String[] saTsBb)
    throws BusinessException
  {
    boolean[] b = { false, false, false };

    if ((saBillid != null) && (saBillid.length > 0)) {
      b[0] = true;
      if ((saTsh == null) || (saTsh.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000116"));
      }
    }
    if ((saBill_bid != null) && (saBill_bid.length > 0)) {
      b[1] = true;
      if ((saTsb == null) || (saTsb.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000116"));
      }
    }
    if ((saBill_bbid != null) && (saBill_bbid.length > 0)) {
      b[2] = true;
      if ((saTsBb == null) || (saTsBb.length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000116"));
      }
    }

    Object[] saObj = queryHBTsArrayByHBIDArray(sBillType, saBillid, saBill_bid, saBill_bbid);
    if ((saObj == null) || (saObj.length <= 0))
    {
      SCMEnv.out("没有查询到相应的时间戳，暂不处理，go on！");
      return new UFBoolean(true);
    }

    String[] saTmp = null;
    if (b[0]) {
 // if (b[0] != 0) {
      saTmp = (String[])(String[])saObj[0];
      if ((saTmp == null) || (saTmp.length <= 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
      for (int i = 0; i < saTmp.length; i++) {
        if (!saTsh[i].equals(saTmp[i])) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
        }
      }
    }
    if (b[1]) {
 // if (b[1] != 0) {
      saTmp = (String[])(String[])saObj[1];
      if ((saTmp == null) || (saTmp.length <= 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
      for (int i = 0; i < saTmp.length; i++) {
        if (!saTsb[i].equals(saTmp[i])) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
        }
      }
    }
    if (b[2]) {
 // if (b[2] != 0) {
      saTmp = (String[])(String[])saObj[2];
      if ((saTmp == null) || (saTmp.length <= 0))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
      for (int i = 0; i < saTmp.length; i++) {
        if (!saTsBb[i].equals(saTmp[i])) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000117"));
        }
      }
    }
    return new UFBoolean(true);
  }

  public UFBoolean checkVoNoChanged(AggregatedValueObject vo)
    throws Exception
  {
    long beginTime = System.currentTimeMillis();

    UFBoolean ufbReslt = new UFBoolean(false);

    if (vo == null)
      return ufbReslt;
    if ((vo.getParentVO() == null) && ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0)))
    {
      return ufbReslt;
    }
    String sBillType = null;
    String[] saBillid = null;
    String[] saTsh = null;
    String[] saBill_bid = null;
    String[] saTsb = null;

    String strClassName = vo.getClass().getName();
    if (strClassName.equals("nc.vo.pr.pray.PraybillVO"))
      sBillType = "20";
    else if (strClassName.equals("nc.vo.po.OrderVO"))
      sBillType = "21";
    else if (strClassName.equals("nc.vo.po.OrderVO"))
      sBillType = "21";
    else if (strClassName.equals("nc.vo.rc.receive.ArriveorderVO"))
      sBillType = "23";
    else if (strClassName.equals("nc.vo.pi.InvoiceVO"))
      sBillType = "25";
    else if (strClassName.equals("nc.vo.ps.settle.SettlebillVO"))
      sBillType = "27";
    else if (strClassName.equals("nc.vo.pp.ask.AskbillVO"))
      sBillType = "29";
    else if (strClassName.equals("nc.vo.ps.factor.CostfactorVO"))
      sBillType = "2ACost";
    else if (strClassName.equals("nc.vo.ic.pub.bill.GeneralBillVO"))
      sBillType = "45";
    else if (strClassName.equals(""))
      sBillType = "47";
    else if (strClassName.equals(""))
      sBillType = "4T";
    else if (strClassName.equals("nc.vo.sc.order.OrderVO"))
      sBillType = "61";
    else if (strClassName.equals("nc.vo.sc.adjust.AdjustbillVO"))
      sBillType = "62";
    else if (strClassName.equals("nc.vo.ic.pub.bill.GeneralBillVO"))
      sBillType = "41";
    else if (strClassName.equals("nc.vo.ic.pub.bill.GeneralBillVO"))
      sBillType = "49";
    else if (strClassName.equals("nc.vo.ct.pub.ManageVO"))
      sBillType = "Z1";
    else if (strClassName.equals("nc.vo.ct.pub.ManageVO"))
      sBillType = "Z2";
    else if (strClassName.equals("nc.vo.so.so001.SaleOrderVO")) {
      sBillType = "30";
    }

    boolean bIsNew = (vo.getParentVO().getPrimaryKey() == null) || (vo.getParentVO().getPrimaryKey().trim().length() == 0);

    if (bIsNew) {
      saBillid = null;
      saTsh = null;
      if ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000118"));
      }
      boolean bIsHaveRef = false;
      for (int i = 0; i < vo.getChildrenVO().length; i++)
      {
        if (("21".equalsIgnoreCase(sBillType)) && (PuPubVO.getString_TrimZeroLenAsNull(vo.getChildrenVO()[i].getAttributeValue("cpriceaudit_bid")) != null))
        {
          sBillType = "28";
          bIsHaveRef = true;
          break;
        }
        if ((vo.getChildrenVO()[i].getAttributeValue("cupsourcebilltype") == null) || (vo.getChildrenVO()[i].getAttributeValue("cupsourcebilltype").toString().trim().equals("")))
        {
          continue;
        }

        sBillType = (String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebilltype");

        bIsHaveRef = true;
        break;
      }

      if (!bIsHaveRef) {
        return new UFBoolean(true);
      }
      Vector vUpId = new Vector();

      Vector vUpTs = new Vector();

      saBillid = new String[1];
      saTsh = new String[1];
      String strTmpId = null;
      String strTmpTs = null;
      for (int i = 0; i < vo.getChildrenVO().length; i++)
      {
        if ("28".equalsIgnoreCase(sBillType))
          strTmpId = (String)vo.getChildrenVO()[i].getAttributeValue("cpriceaudit_bid");
        else {
          strTmpId = (String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebillrowid");
        }

        if ((strTmpId != null) && (!strTmpId.trim().equals(""))) {
          strTmpTs = (String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebts");
          if ((strTmpTs == null) || (strTmpTs.trim().equals(""))) {
            SCMEnv.out("数据错误：未获取由参照单据转入的表体时间戳");
            return new UFBoolean(false);
          }

          vUpId.addElement(strTmpId);

          vUpTs.addElement(strTmpTs);

          if ((saBillid[0] != null) && (saBillid[0].trim().length() != 1))
            continue;
          if ("28".equalsIgnoreCase(sBillType))
            saBillid[0] = ((String)vo.getChildrenVO()[i].getAttributeValue("cpriceauditid"));
          else {
            saBillid[0] = ((String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebillid"));
          }
          saTsh[0] = ((String)vo.getChildrenVO()[i].getAttributeValue("cupsourcehts"));
          if ((saTsh[0] == null) || (saTsh[0].trim().equals(""))) {
            SCMEnv.out("数据错误：未获取由参照单据转入的表头时间戳");
            return new UFBoolean(false);
          }
        }

      }

      if ((vUpId.size() > 0) && (vUpTs.size() > 0) && (vUpId.size() == vUpTs.size())) {
        saBill_bid = new String[vUpId.size()];
        saTsb = new String[vUpTs.size()];
        vUpId.copyInto(saBill_bid);
        vUpTs.copyInto(saTsb);
      } else {
        SCMEnv.out("数据错误：有上层转入的单据，但上层ID及上层TS不匹配");
        return new UFBoolean(false);
      }

    }
    else
    {
      saBillid = new String[1];
      saTsh = new String[1];

      saBillid[0] = vo.getParentVO().getPrimaryKey();

      saTsh[0] = ((String)vo.getParentVO().getAttributeValue("ts"));
      if ((saTsh[0] == null) || (saTsh[0].trim().equals(""))) {
        SCMEnv.out("数据错误：未获取修改保存单据的表头时间戳");
        return new UFBoolean(false);
      }

      Vector vBid = new Vector();
      Vector vBts = new Vector();
      if ((vo.getChildrenVO() != null) && (vo.getChildrenVO().length > 0)) {
        for (int i = 0; i < vo.getChildrenVO().length; i++) {
          if (vo.getChildrenVO()[i].getStatus() != 2) {
            vBid.addElement(vo.getChildrenVO()[i].getPrimaryKey());
            vBts.addElement(vo.getChildrenVO()[i].getAttributeValue("ts"));
          }
        }
        if (vBid.size() <= 0) {
          SCMEnv.out("数据错误：未获取由参照单据转入的表体时间戳");
          return new UFBoolean(false);
        }
        saBill_bid = new String[vBid.size()];
        saTsb = new String[vBid.size()];
        for (int i = 0; i < vBid.size(); i++) {
          saBill_bid[i] = ((String)vBid.elementAt(i));
          saTsb[i] = ((String)vBts.elementAt(i));
        }
      }
    }

    checkTsNoChanged(sBillType, saBillid, saTsh, saBill_bid, saTsb, null, null);

    long endTime = System.currentTimeMillis();
    SCMEnv.out("单张单据并发检查消耗时间：" + (endTime - beginTime));
    return new UFBoolean(true);
  }

  public UFBoolean checkVosNoChanged(AggregatedValueObject[] vos)
    throws BusinessException
  {
    long beginTime = System.currentTimeMillis();

    UFBoolean ufbReslt = new UFBoolean(false);

    if ((vos == null) || (vos.length == 0)) {
      return ufbReslt;
    }

    String sBillType = null;

    String[] saBillid = null;

    String[] saTsh = null;

    String[] saBill_bid = null;

    String[] saTsb = null;

    Vector vBid = new Vector();
    Vector vBts = new Vector();

    String strClassName = vos[0].getClass().getName();
    if (strClassName.equals("nc.vo.pr.pray.PraybillVO"))
      sBillType = "20";
    else if (strClassName.equals("nc.vo.po.OrderVO"))
      sBillType = "21";
    else if (strClassName.equals("nc.vo.po.OrderVO"))
      sBillType = "21";
    else if (strClassName.equals("nc.vo.rc.receive.ArriveorderVO"))
      sBillType = "23";
    else if (strClassName.equals("nc.vo.pi.InvoiceVO"))
      sBillType = "25";
    else if (strClassName.equals("nc.vo.ps.settle.SettlebillVO"))
      sBillType = "27";
    else if (strClassName.equals("nc.vo.pp.ask.AskbillVO"))
      sBillType = "29";
    else if (strClassName.equals("nc.vo.ps.factor.CostfactorVO"))
      sBillType = "2ACost";
    else if (strClassName.equals("nc.vo.ic.pub.bill.GeneralBillVO"))
      sBillType = "45";
    else if (strClassName.equals(""))
      sBillType = "47";
    else if (strClassName.equals(""))
      sBillType = "4T";
    else if (strClassName.equals("nc.vo.sc.order.OrderVO"))
      sBillType = "61";
    else if (strClassName.equals("nc.vo.sc.adjust.AdjustbillVO"))
      sBillType = "62";
    else if (strClassName.equals("nc.vo.ic.pub.bill.GeneralBillVO"))
      sBillType = "41";
    else if (strClassName.equals("nc.vo.ic.pub.bill.GeneralBillVO"))
      sBillType = "49";
    else if (strClassName.equals("nc.vo.ct.pub.ManageVO"))
      sBillType = "Z1";
    else if (strClassName.equals("nc.vo.ct.pub.ManageVO"))
      sBillType = "Z2";
    else if (strClassName.equals("nc.vo.so.so001.SaleOrderVO")) {
      sBillType = "30";
    }

    saBillid = new String[vos.length];
    saTsh = new String[vos.length];
    for (int i = 0; i < saBillid.length; i++)
    {
      saBillid[i] = vos[i].getParentVO().getPrimaryKey();

      saTsh[i] = ((String)vos[i].getParentVO().getAttributeValue("ts"));
      if ((saTsh[i] == null) || (saTsh[i].trim().equals(""))) {
        SCMEnv.out("数据错误：未获取修改保存单据的表头时间戳");
        return new UFBoolean(false);
      }

      if ((vos[i].getChildrenVO() != null) && (vos[i].getChildrenVO().length > 0)) {
        for (int j = 0; j < vos[i].getChildrenVO().length; j++) {
          if (vos[i].getChildrenVO()[j].getStatus() != 2) {
            String ts = (String)vos[i].getChildrenVO()[j].getAttributeValue("ts");
            if ((ts == null) || (ts.trim().length() == 0)) {
              SCMEnv.out("数据错误：未获取由参照单据转入的表体时间戳");
              return new UFBoolean(false);
            }
            vBid.addElement(vos[i].getChildrenVO()[j].getPrimaryKey());
            vBts.addElement(vos[i].getChildrenVO()[j].getAttributeValue("ts"));
          }
        }
      }

      saBill_bid = new String[vBid.size()];
      saTsb = new String[vBid.size()];
      vBid.copyInto(saBill_bid);
      vBts.copyInto(saTsb);
    }

    checkTsNoChanged(sBillType, saBillid, saTsh, saBill_bid, saTsb, null, null);

    long endTime = System.currentTimeMillis();
    SCMEnv.out(vos.length + "张单据并发检查消耗时间：" + (endTime - beginTime));
    return new UFBoolean(true);
  }

  public void deleRelatStoBill(AggregatedValueObject[] vos, String billtype)
    throws Exception
  {
    if ((vos == null) || (vos.length == 0) || (billtype == null) || (billtype.trim().length() < 1))
    {
      SCMEnv.out("nc.bs.pu.pub.PubDMO.deleRelatStoBill(AggregatedValueObject [], String)传入参数不正确！");
      return;
    }

    String[] sourceids = new String[vos.length];
    for (int i = 0; i < vos.length; i++) {
      sourceids[i] = vos[i].getParentVO().getPrimaryKey();
    }

    String pk_corp = (String)vos[0].getParentVO().getAttributeValue("pk_corp");
    try
    {
      ICreateCorpQueryService corpDmo = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());
      if (corpDmo.isEnabled(pk_corp, "IC"))
      {
        IICPub_GeneralBillDMO icDmo = (IICPub_GeneralBillDMO)NCLocator.getInstance().lookup(IICPub_GeneralBillDMO.class.getName());
        icDmo.deleteBySourceID(sourceids, billtype);
      }
    } catch (Exception e) {
      throw e;
    }
  }

  public Hashtable fetchArrayValue(String tableName, String fieldName, String key, String[] values)
    throws Exception
  {
    Hashtable table = new Hashtable();

    String sSubSql = new TempTableDMO().insertTempTable(values, "t_pu_general", "pk_pu");

    StringBuffer buffer = new StringBuffer("select " + key + ", " + fieldName + " from " + tableName);
    buffer.append(" WHERE " + key + " IN " + sSubSql);

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(buffer.toString());
      rs = stmt.executeQuery();

      String keyValue = null;
      String fieldValue = null;
      while (rs.next()) {
        keyValue = rs.getString(1);
        if ((keyValue == null) || (keyValue.trim() == "")) {
          continue;
        }
        fieldValue = rs.getString(2);
        if ((fieldValue == null) || (fieldValue.trim() == "") || 
          (table.containsKey(keyValue))) continue;
        table.put(keyValue, fieldValue);
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return table;
  }

  public String fetchCellValue(String tableName, String fieldName, String key, String value)
    throws SQLException
  {
    String s = null;
    if ((value == null) || (value.trim().length() == 0)) return null;

    StringBuffer buffer = new StringBuffer("select " + fieldName + " from " + tableName);
    buffer.append(" where " + key + " = '" + value + "'");

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(buffer.toString());
      rs = stmt.executeQuery();

      if (rs.next())
        s = rs.getString(1);
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return s;
  }

  public String fetchSysPara(String pk_corp, String sparaCode)
    throws Exception
  {
    String str = null;
    try {
      SysInitDMO dmo = new SysInitDMO();
      SysInitVO vo = dmo.queryByParaCode(pk_corp, sparaCode);
      if (vo != null)
        str = vo.getValue();
    } catch (Exception e) {
      reportException(e);
      throw e;
    }
    return str;
  }

  public void freePkForInv(InvoiceVO vo)
    throws Exception
  {
    String sMethodName = "nc.bs.pu.pub.PubDMO.freePkForInv(InvoiceVO)";

    CircularlyAccessibleValueObject[] children = vo.getChildrenVO();
    if ((children == null) || (children.length == 0)) {
      return;
    }

    try
    {
      String sCuser = null;
      sCuser = (String)vo.getParentVO().getAttributeValue("cuserid");
      if ((sCuser == null) || (sCuser.trim().equals(""))) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000119"));
      }

      String[] saPk = null;
      Hashtable hash = new Hashtable();

      InvoiceItemVO[] vos = (InvoiceItemVO[])(InvoiceItemVO[])children;
      for (int i = 0; i < vos.length; i++) {
        String invmanid = vos[i].getCmangid();
        if ((invmanid != null) && (hash.get(invmanid) == null)) {
          hash.put(invmanid, invmanid);
        }
      }
      saPk = new String[hash.size()];
      Enumeration en = hash.elements();
      int i = 0;
      while (en.hasMoreElements()) {
        saPk[i] = ((String)en.nextElement());
        i++;
      }

      if (saPk != null)
      {
        LockTool.releaseLockForPks(saPk, sCuser);
      }
    }
    catch (Exception e) {
      reportException(e);
      throwBusinessException(sMethodName, e);
    }
  }

  public void freePkForVo(AggregatedValueObject vo)
    throws Exception
  {
    String sCuser = (String)vo.getParentVO().getAttributeValue("cuserid");
    SCMEnv.out("正在为操作员[ID:" + sCuser + "]释放NC业务锁...");
    if ((sCuser == null) || (sCuser.trim().equals("")))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000119"));
    String[] saPk = getPksForVoFree(vo);
    try
    {
      if (saPk != null)
      {
        LockTool.releaseLockForPks(saPk, sCuser);
      }

      SCMEnv.out("为操作员[ID:" + sCuser + "]释放NC业务锁成功结束。");
    } catch (Exception e) {
      SCMEnv.out("为操作员[ID:" + sCuser + "]释放NC业务锁异常结束。");
      reportException(e);
      throw e;
    }
  }

  public void freePkForVos(AggregatedValueObject[] vos)
    throws BusinessException
  {
    String sCuser = null;
    sCuser = (String)vos[0].getParentVO().getAttributeValue("cuserid");
    if ((sCuser == null) || (sCuser.trim().equals("")))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000119"));
    String[] saPk = getPksForVos(vos);
    try
    {
      if (saPk != null)
      {
        LockTool.releaseLockForPks(saPk, sCuser);
      }
    } catch (Exception e) {
      throwBusinessException(e);
    }
  }

  public static UFDouble[] getExchangeRate(String currTypeId, String sDate, String m_sUnitCode)
    throws Exception
  {
    UFDouble dCurrRate = null;
    UFDouble dAuxiRate = null;
    try
    {
      BusinessCurrencyRateUtil currArith = new BusinessCurrencyRateUtil(m_sUnitCode);
      if (currArith.getLocalCurrPK() == null) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000120"));
      }
      if ((currArith.isBlnLocalFrac()) && (currArith.getFracCurrPK() == null)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000121"));
      }

      if (currArith.isBlnLocalFrac())
      {
        if (currArith.isLocalCurrType(currTypeId))
        {
          dCurrRate = new UFDouble(1.0D);
          dAuxiRate = null;
        } else {
          dCurrRate = currArith.getRate(currArith.getFracCurrPK(), null, sDate);
          if (currArith.isFracCurrType(currTypeId))
          {
            dAuxiRate = new UFDouble(1.0D);
          }
          else dAuxiRate = currArith.getRate(currTypeId, null, sDate);
        }
      }
      else
      {
        if (currArith.isLocalCurrType(currTypeId))
        {
          dCurrRate = new UFDouble(1.0D);
        }
        else dCurrRate = currArith.getRate(currTypeId, null, sDate);

        dAuxiRate = null;
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      throw e;
    }

    UFDouble[] d = new UFDouble[2];
    d[0] = dCurrRate;
    d[1] = dAuxiRate;

    return d;
  }

  public UFDate[] getOrderDatesBatch(String[] pk_corps, String[] storeids, String[] cmangids, UFDouble[] npraynums, UFDate[] ddemanddates, UFDate dpraydate)
    throws Exception
  {
    UFDate[] date = null;
    try {
      PraybillDMO dmo = new PraybillDMO();
      int[] days = dmo.getAdvanceDaysBatch(pk_corps, storeids, cmangids, npraynums);
      if ((days != null) || (days.length != 0)) {
        UFDate d = null;
        for (int i = 0; i < days.length; i++) {
          d = ddemanddates[i].getDateBefore(days[i]);
          if (d.before(dpraydate))
            d = dpraydate;
          date[i] = d;
        }
      }
    }
    catch (Exception e) {
      reportException(e);
      throw e;
    }
    return date;
  }

  private String[] getPksForVo(AggregatedValueObject vo)
    throws BusinessException, BusinessException, SQLException
  {
    String[] saRslt = null;

    if (vo == null)
      return null;
    if ((vo.getParentVO() == null) && ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0)))
    {
      return null;
    }
    if (vo.getParentVO() == null) {
      SCMEnv.out("数据错误：无单据表头数据,加锁失败");
      return null;
    }

    Vector vPk = new Vector();

    boolean bIsNew = (vo.getParentVO().getPrimaryKey() == null) || (vo.getParentVO().getPrimaryKey().trim().equals(""));

    String sTmp = null;

    if (bIsNew)
    {
      boolean bIsRef = false;
      if ((vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000122"));
      }
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        sTmp = (String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebillrowid");
        if ((sTmp != null) && (!sTmp.trim().equals(""))) {
          bIsRef = true;
          break;
        }
      }

      if (!bIsRef) {
        SCMEnv.out("单据行全部自制，不必加锁");
        return null;
      }
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        sTmp = (String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebillrowid");
        if ((sTmp != null) && (!sTmp.trim().equals(""))) {
          if (!vPk.contains(sTmp))
            vPk.addElement(sTmp);
          sTmp = (String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebillid");
          if ((sTmp == null) || (sTmp.trim().equals(""))) {
            SCMEnv.out("存在数据错误：参照的单据上层行ID找不到匹配的上层头ID,加锁失败");
            return null;
          }
          if (!vPk.contains(sTmp))
            vPk.addElement(sTmp);
        }
      }
    }
    else
    {
      sTmp = vo.getParentVO().getPrimaryKey();
      if ((sTmp == null) || (sTmp.trim().equals(""))) {
        SCMEnv.out("数据错误：找不到表头ID,加锁失败");
        return null;
      }
      vPk.addElement(sTmp);
      if (vo.getChildrenVO().length > 0) {
        for (int i = 0; i < vo.getChildrenVO().length; i++) {
          sTmp = vo.getChildrenVO()[i].getPrimaryKey();
          if ((sTmp != null) && (sTmp.trim().length() > 0) && (!vPk.contains(sTmp)))
            vPk.addElement(sTmp);
        }
      }
    }
    if ((vPk != null) && (vPk.size() > 0)) {
      saRslt = new String[vPk.size()];
      vPk.copyInto(saRslt);
    }

    return saRslt;
  }

  private String[] getPksForVoFree(AggregatedValueObject vo)
    throws BusinessException, BusinessException, SQLException
  {
    String[] saRslt = null;

    if (vo == null) {
      return null;
    }

    if (vo.getParentVO() == null) {
      SCMEnv.out("数据错误：无单据表头数据,加锁失败");
      return null;
    }

    Vector vPk = new Vector();
    String sTmp = null;

    sTmp = vo.getParentVO().getPrimaryKey();
    if ((sTmp != null) && (!sTmp.trim().equals(""))) {
      vPk.addElement(sTmp);
    }

    if ((vo.getChildrenVO() != null) && (vo.getChildrenVO().length > 0)) {
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        sTmp = (String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebillrowid");
        if ((sTmp != null) && (!sTmp.trim().equals(""))) {
          if (!vPk.contains(sTmp))
            vPk.addElement(sTmp);
          sTmp = (String)vo.getChildrenVO()[i].getAttributeValue("cupsourcebillid");
          if (((sTmp == null) || (sTmp.trim().equals(""))) && 
            (!(vo instanceof PraybillVO))) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000123"));
          }

          if ((sTmp != null) && (!vPk.contains(sTmp))) {
            vPk.addElement(sTmp);
          }
        }

        sTmp = vo.getChildrenVO()[i].getPrimaryKey();
        if ((sTmp != null) && (sTmp.trim().length() > 0) && (!vPk.contains(sTmp))) {
          vPk.addElement(sTmp);
        }
      }
    }

    if ((vPk != null) && (vPk.size() > 0)) {
      saRslt = new String[vPk.size()];
      vPk.copyInto(saRslt);
    }
    return saRslt;
  }

  private String[] getPksForVos(AggregatedValueObject[] vos)
    throws BusinessException
  {
    String[] saRslt = null;

    if (vos == null) {
      return null;
    }
    Vector vPk = new Vector();
    for (int i = 0; i < vos.length; i++) {
      if ((vos[i].getParentVO() == null) && ((vos[i].getChildrenVO() == null) || (vos[i].getChildrenVO().length <= 0)))
      {
        return null;
      }
      if (vos[i].getParentVO() == null) {
        SCMEnv.out("数据错误：无单据表头数据,加锁失败");
        return null;
      }

      boolean bIsNew = (vos[i].getParentVO().getPrimaryKey() == null) || (vos[i].getParentVO().getPrimaryKey().trim().equals(""));

      String sTmp = null;

      if (bIsNew)
      {
        boolean bIsRef = false;
        if ((vos[i].getChildrenVO() == null) || (vos[i].getChildrenVO().length <= 0)) {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000122"));
        }
        for (int j = 0; j < vos[i].getChildrenVO().length; j++) {
          sTmp = (String)vos[i].getChildrenVO()[j].getAttributeValue("cupsourcebillrowid");

          if ((sTmp != null) && (!sTmp.trim().equals(""))) {
            bIsRef = true;
            break;
          }
        }

        if (!bIsRef) {
          SCMEnv.out("单据行全部自制，不必加锁");
          return null;
        }
        for (int j = 0; j < vos[i].getChildrenVO().length; j++) {
          sTmp = (String)vos[i].getChildrenVO()[j].getAttributeValue("cupsourcebillrowid");

          if ((sTmp != null) && (!sTmp.trim().equals(""))) {
            if (!vPk.contains(sTmp))
              vPk.addElement(sTmp);
            sTmp = (String)vos[i].getChildrenVO()[j].getAttributeValue("cupsourcebillid");
            if ((sTmp == null) || (sTmp.trim().equals(""))) {
              SCMEnv.out("存在数据错误：参照的单据上层行ID找不到匹配的上层头ID,加锁失败");
              return null;
            }
            if (!vPk.contains(sTmp))
              vPk.addElement(sTmp);
          }
        }
      }
      else
      {
        sTmp = vos[i].getParentVO().getPrimaryKey();
        if ((sTmp == null) || (sTmp.trim().equals(""))) {
          SCMEnv.out("数据错误：找不到表头ID,加锁失败");
          return null;
        }
        vPk.addElement(sTmp);
        if ((vos[i].getChildrenVO() != null) && (vos[i].getChildrenVO().length > 0)) {
          for (int j = 0; j < vos[i].getChildrenVO().length; j++) {
            sTmp = vos[i].getChildrenVO()[j].getPrimaryKey();
            if ((sTmp != null) && (sTmp.trim().length() > 0) && (!vPk.contains(sTmp)))
              vPk.addElement(sTmp);
          }
        }
      }
    }
    if ((vPk != null) && (vPk.size() > 0)) {
      saRslt = new String[vPk.size()];
      vPk.copyInto(saRslt);
    }

    return saRslt;
  }

  public UFDate[] getPlanArrvDates(String[] pk_corps, String[] storeids, String[] saBaseId, UFDouble[] npraynums, UFDate[] ddemanddates, UFDate dpraydate)
    throws Exception
  {
    UFDate[] date = null;
    try {
      PraybillDMO dmo = new PraybillDMO();
      int[] days = dmo.getAdvanceDaysBatch(pk_corps, storeids, saBaseId, npraynums);
      if ((days != null) && (days.length != 0)) {
        date = new UFDate[days.length];
        UFDate d = null;
        for (int i = 0; i < days.length; i++) {
          d = ddemanddates[i].getDateAfter(days[i]);
          if (d.before(dpraydate))
            d = dpraydate;
          date[i] = d;
        }
      }
    }
    catch (Exception e)
    {
      int i;
      reportException(e);
      throw e;
    } finally {
      if (date == null) {
        date = new UFDate[ddemanddates.length];
        for (int i = 0; i < date.length; i++) {
          date[i] = dpraydate;
        }
      }
    }
    return date;
  }

  public Object[][] getRateArray(String[] currTypeIds, UFDate date, String m_sUnitCode)
    throws BusinessException, NamingException, javax.transaction.SystemException
  {
    UFDouble dCurrRate = null;
    UFDouble dAuxiRate = null;
    String isEdit = null;
    String isEdit1 = null;
    Object[][] objs = new Object[currTypeIds.length][4];
    BusinessCurrencyRateUtil rate = null;
    try
    {
      rate = new BusinessCurrencyRateUtil(m_sUnitCode);
      GlParaDMO currArith = new GlParaDMO();

      for (int i = 0; i < currTypeIds.length; i++) {
        if (currArith.isLocalFrac(m_sUnitCode).booleanValue())
        {
          if (currArith.isLocalCurrType(m_sUnitCode, currTypeIds[i]))
          {
            dCurrRate = new UFDouble(1.0D);
            dAuxiRate = null;
            isEdit = "N";
            isEdit1 = "N";
          }
          else if (currArith.isFracCurrType(m_sUnitCode, currTypeIds[i]))
          {
            dAuxiRate = new UFDouble(1.0D);
            isEdit = "Y";
            isEdit1 = "N";
          }
          else {
            dAuxiRate = rate.getRate(m_sUnitCode, currTypeIds[i], date.toString());
            isEdit = "Y";
            isEdit1 = "Y";
          }
        }
        else
        {
          if (currArith.isLocalCurrType(m_sUnitCode, currTypeIds[i]))
          {
            dCurrRate = new UFDouble(1.0D);
            isEdit = "N";
            isEdit1 = "N";
          } else {
            dCurrRate = rate.getRate(m_sUnitCode, currTypeIds[i], date.toString());
            isEdit = "Y";
            isEdit1 = "N";
          }

          dAuxiRate = null;
        }
        objs[i][0] = dCurrRate;
        objs[i][1] = dAuxiRate;
        objs[i][2] = isEdit;
        objs[i][3] = isEdit1;
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000125"));
    }

    return objs;
  }

  public Object[] getRates(String currTypeId, String sDate, String m_sUnitCode)
    throws Exception
  {
    UFDouble dCurrRate = null;
    UFDouble dAuxiRate = null;
    String isEdit = null;
    String isEdit1 = null;
    try {
      GlParaDMO currArith = new GlParaDMO();
      BusinessCurrencyRateUtil rate = new BusinessCurrencyRateUtil(m_sUnitCode);
      if (currArith.isLocalFrac(m_sUnitCode).booleanValue())
      {
        if (currArith.isLocalCurrType(m_sUnitCode, currTypeId))
        {
          dCurrRate = new UFDouble(1.0D);
          dAuxiRate = null;
          isEdit = "N";
          isEdit1 = "N";
        }
        else if (currArith.isFracCurrType(m_sUnitCode, currTypeId))
        {
          dAuxiRate = new UFDouble(1.0D);
          isEdit = "Y";
          isEdit1 = "N";
        } else {
          dAuxiRate = rate.getRate(m_sUnitCode, currTypeId, sDate);
          isEdit = "Y";
          isEdit1 = "Y";
        }
      }
      else
      {
        if (currArith.isLocalCurrType(m_sUnitCode, currTypeId))
        {
          dCurrRate = new UFDouble(1.0D);
          isEdit = "N";
          isEdit1 = "N";
        } else {
          dCurrRate = rate.getRate(m_sUnitCode, currTypeId, sDate);
          isEdit = "Y";
          isEdit1 = "N";
        }

        dAuxiRate = null;
      }
    }
    catch (Exception e) {
      reportException(e);
      throw e;
    }

    Object[] d = new Object[4];
    d[0] = dCurrRate;
    d[1] = dAuxiRate;
    d[2] = isEdit;
    d[3] = isEdit1;

    return d;
  }

  public UFBoolean lockPkForInv(InvoiceVO vo)
    throws Exception
  {
    CircularlyAccessibleValueObject[] children = vo.getChildrenVO();
    if ((children == null) || (children.length == 0)) {
      return VariableConst.UFBOOLEAN_TRUE;
    }

    boolean isLockSucc = false;
    try
    {
      String sCuser = null;
      sCuser = (String)vo.getParentVO().getAttributeValue("cuserid");
      if ((sCuser == null) || (sCuser.trim().equals(""))) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000119"));
      }

      String[] saPk = null;
      Hashtable hash = new Hashtable();

      InvoiceItemVO[] vos = (InvoiceItemVO[])(InvoiceItemVO[])children;
      for (int i = 0; i < vos.length; i++) {
        String invmanid = vos[i].getCmangid();
        if ((invmanid != null) && (hash.get(invmanid) == null)) {
          hash.put(invmanid, invmanid);
        }
      }
      saPk = new String[hash.size()];
      Enumeration en = hash.elements();
      int i = 0;
      while (en.hasMoreElements()) {
        saPk[i] = ((String)en.nextElement());
        i++;
      }

      if ((saPk != null) && (saPk.length > 0))
      {
        isLockSucc = LockTool.setLockForPks(saPk, sCuser);
      }
      if (!isLockSucc) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000129"));
      }
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      throw e;
    }

    return new UFBoolean(isLockSucc);
  }

  public UFBoolean lockPkForVo(AggregatedValueObject vo)
    throws Exception
  {
    boolean isLockSucc = false;
    try
    {
      String sCuser = null;
      sCuser = (String)vo.getParentVO().getAttributeValue("cuserid");
      SCMEnv.out("正在为操作员[ID:" + sCuser + "]申请NC业务锁...");
      if ((sCuser == null) || (sCuser.trim().equals("")))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000119"));
      String[] saPk = getPksForVo(vo);
      if (saPk != null)
      {
        isLockSucc = LockTool.setLockForPks(saPk, sCuser);
        if (isLockSucc)
          SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁成功。");
        else {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000129"));
        }
      }
      else
      {
        SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁失败。失败原因：不需要枷锁的情况!");
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      throw e;
    }
    return new UFBoolean(isLockSucc);
  }

  public HashMap isDiscount(String[] cMangID)
    throws SQLException
  {
    if ((cMangID == null) || (cMangID.length == 0)) {
      return null;
    }
    StringBuffer subcon = new StringBuffer();
    HashMap retIDs = new HashMap();
    for (int i = 0; i < cMangID.length; i++) {
      if (i < cMangID.length - 1) {
        subcon.append("'");
        subcon.append(cMangID[i] + "',");
      } else if (i == cMangID.length - 1) {
        subcon.append("'");
        subcon.append(cMangID[i] + "'");
      }
    }
    if (subcon.toString() == null) {
      return null;
    }
    String sql = "select discountflag,pk_invbasdoc from bd_invbasdoc  where pk_invbasdoc in (" + subcon.toString() + ")";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String discountFlag = rs.getString(1);
        String invbas = rs.getString(2);
        if ((discountFlag == null) || (discountFlag.trim().length() == 0)) {
          discountFlag = "N";
        }
        retIDs.put(invbas, discountFlag);
      }
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return retIDs;
  }

  public HashMap isLabor(String[] cMangID)
    throws SQLException
  {
    if ((cMangID == null) || (cMangID.length == 0)) {
      return null;
    }
    StringBuffer subcon = new StringBuffer();
    HashMap retIDs = new HashMap();
    for (int i = 0; i < cMangID.length; i++) {
      if (i < cMangID.length - 1) {
        subcon.append("'");
        subcon.append(cMangID[i] + "',");
      } else if (i == cMangID.length - 1) {
        subcon.append("'");
        subcon.append(cMangID[i] + "'");
      }
    }
    if (subcon.toString() == null) {
      return null;
    }
    String sql = "select laborflag,pk_invbasdoc from bd_invbasdoc  where pk_invbasdoc in (" + subcon.toString() + ")";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String laborFlag = rs.getString(1);
        String invbas = rs.getString(2);
        if ((laborFlag == null) || (laborFlag.trim().length() == 0)) {
          laborFlag = "N";
        }
        retIDs.put(invbas, laborFlag);
      }
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return retIDs;
  }

  public void isBillStateChanged(AggregatedValueObject[] vos)
    throws Exception
  {
    if ((vos == null) || (vos.length == 0))
      return;
    String strTableName = null;
    String strIdCol = null;
    String strStateCol = "ibillstatus";
    String strPass = "3";

    if ((vos[0] instanceof PraybillVO)) {
      strTableName = "po_praybill";
      strIdCol = "cpraybillid";
    }
    else if ((vos[0] instanceof nc.vo.po.OrderVO)) {
      strTableName = "po_order";
      strIdCol = "corderid";
      strStateCol = "forderstatus";
    }
    else if ((vos[0] instanceof ArriveorderVO)) {
      strTableName = "po_arriveorder";
      strIdCol = "carriveorderid";
    }
    else if ((vos[0] instanceof InvoiceVO)) {
      strTableName = "po_invoice";
      strIdCol = "cinvoiceid";
    }
    else
    {
      SCMEnv.out("目前不支持的VO类型：" + vos[0].getClass().getName());
      return;
    }
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String strSqlUpt = "update " + strTableName + " set dr = 0 where " + strIdCol + " = ?";

    String strSqlSel = "select " + strStateCol + " from " + strTableName + " where " + strIdCol + " in ";

    String[] saId = null;
    int iLen = vos.length;
    try {
      con = getConnection();

      stmt = prepareStatement(con, strSqlUpt);
      saId = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        saId[i] = vos[i].getParentVO().getPrimaryKey();
        stmt.setString(1, saId[i]);
        executeUpdate(stmt);
      }
      executeBatch(stmt);

      String strSetIn = null;
      try {
        strSetIn = new TempTableDMO().insertTempTable(saId, "t_pu_general", "pk_pu");
      } catch (Exception e) {
        reportException(e);
        throw e;
      }
      strSqlSel = strSqlSel + strSetIn;
      stmt = con.prepareStatement(strSqlSel);
      rs = stmt.executeQuery();
      Object oStatus = null;
      while (rs.next()) {
        oStatus = rs.getObject(1);
        if ((oStatus == null) || 
          (!"3".equals(oStatus.toString().trim()))) continue;
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000252"));
      }

      SCMEnv.out("刷新状态后未发现有审批通过状态的单据");
    } catch (Exception e) {
      SCMEnv.out(e);
      throw e;
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (stmt != null) {
        stmt.close();
      }
      if (con != null)
        con.close();
    }
  }

  public UFBoolean lockPkForVos(AggregatedValueObject[] vos)
    throws BusinessException
  {
    boolean isLockSucc = false;
    try
    {
      String sCuser = null;
      sCuser = (String)vos[0].getParentVO().getAttributeValue("cuserid");
      SCMEnv.out("正在为操作员[ID:" + sCuser + "]申请NC业务锁...");
      if ((sCuser == null) || (sCuser.trim().equals("")))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000119"));
      String[] saPk = getPksForVos(vos);
      if (saPk != null)
      {
        isLockSucc = LockTool.setLockForPks(saPk, sCuser);
        if (isLockSucc)
          SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁成功。");
        else {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000129"));
        }
      }
      else
      {
        SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁失败。失败原因：不需要枷锁的情况!");
      }
    } catch (Exception e) {
      throwBusinessException(e);
    }
    return new UFBoolean(isLockSucc);
  }

  public ArrayList lockPkForVosReturnKeys(AggregatedValueObject[] vos)
    throws BusinessException
  {
    String sCuser = null;
    String[] saPk = null;
    try {
      sCuser = (String)vos[0].getParentVO().getAttributeValue("cuserid");
      SCMEnv.out("正在为操作员[ID:" + sCuser + "]申请NC业务锁...");
      if ((sCuser == null) || (sCuser.trim().equals("")))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000119"));
      saPk = getPksForVos(vos);
      if (saPk != null) {
        boolean isLockSucc = LockTool.setLockForPks(saPk, sCuser);
        if (isLockSucc)
          SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁成功。");
        else {
          throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000129"));
        }
      }
      else
      {
        SCMEnv.out("为操作员[ID:" + sCuser + "]申请NC业务锁失败。失败原因：不需要枷锁的情况!");
      }
    } catch (Exception e) {
      throwBusinessException(e);
    }
    ArrayList listRet = new ArrayList();
    listRet.add(sCuser);
    listRet.add(saPk);

    return listRet;
  }

  public void freePkForOprKeys(String sCuser, String[] saPk)
    throws BusinessException
  {
    if ((saPk == null) || (saPk.length == 0) || (sCuser == null))
    {
      return;
    }
    try {
      if (saPk != null)
        LockTool.releaseLockForPks(saPk, sCuser);
    }
    catch (Exception e) {
      throwBusinessException(e);
    }
  }

  public Object[][] queryArrayValue(String sTable, String sIdName, String[] saFields, String[] saId)
    throws Exception
  {
    return queryArrayValue(sTable, sIdName, saFields, saId, null);
  }

  public Object[][] queryArrayValue(String sTable, String sIdName, String[] saFields, String[] saId, String sWhere)
    throws Exception
  {
    return new ScmPubDMO().queryArrayValue(sTable, sIdName, saFields, saId, sWhere);
  }

  public Object[] queryHBTsArrayByHBIDArray(String sBillType, String[] saBillid, String[] saBill_bid, String[] saBill_bbid)
    throws BusinessException
  {
    beforeCallMethod("nc.bs.pu.pub.PubDMO", "queryHTsArrayByIDArray", new Object[] { saBillid, saBill_bid, saBill_bbid });

    Timer timer = new Timer();
    timer.start();

    if ((sBillType == null) || (sBillType.trim().equals("")) || (((saBillid == null) || (saBillid.length <= 0)) && ((saBill_bid == null) || (saBill_bid.length <= 0)) && ((saBill_bbid == null) || (saBill_bbid.length <= 0))))
    {
      SCMEnv.out("nc.bs.pu.pub.PubDMO.queryHBTsArrayByHBIDArray(String,String[],String[],String[])传入参数不正确：表头ID数组、表体ID数组及子子表ID数组同时为空");

      return null;
    }

    int iLen = 0;

    if (saBillid != null) {
      iLen = saBillid.length;
      for (int i = 0; i < iLen; i++) {
        if ((saBillid[i] == null) || (saBillid[i].trim().length() < 1)) {
          SCMEnv.out("nc.bs.pu.pub.PubDMO.queryHBTsArrayByHBIDArray(String,String[],String[],String[])传入参数不正确！");
          return null;
        }
      }
    }

    if (saBill_bid != null) {
      iLen = saBill_bid.length;
      for (int i = 0; i < iLen; i++) {
        if ((saBill_bid[i] == null) || (saBill_bid[i].trim().length() < 1)) {
          SCMEnv.out("nc.bs.pu.pub.PubDMO.queryHBTsArrayByHBIDArray(String,String[],String[],String[])传入参数不正确！");
          return null;
        }
      }
    }

    if (saBill_bbid != null) {
      iLen = saBill_bbid.length;
      for (int i = 0; i < iLen; i++) {
        if ((saBill_bbid[i] == null) || (saBill_bbid[i].trim().length() < 1)) {
          SCMEnv.out("nc.bs.pu.pub.PubDMO.queryHBTsArrayByHBIDArray(String,String[],String[],String[])传入参数不正确！");
          return null;
        }

      }

    }

    String strTableNameHead = null;
    String strTableNameBody = null;
    String strTableNameBBody = null;
    String strFieldNameHeadId = null;
    String strFieldNameBodyId = null;
    String strFieldNameBBodyId = null;
    if (sBillType.equals("20")) {
      strTableNameHead = "po_praybill";
      strTableNameBody = "po_praybill_b";
      strFieldNameHeadId = "cpraybillid";
      strFieldNameBodyId = "cpraybill_bid";
    } else if (sBillType.equals("21")) {
      strTableNameHead = "po_order";
      strTableNameBody = "po_order_b";
      strTableNameBBody = "po_order_bb";

      strFieldNameHeadId = "corderid";
      strFieldNameBodyId = "corder_bid";
      strFieldNameBBodyId = "corder_bbid";
    } else if (sBillType.equals("23")) {
      strTableNameHead = "po_arriveorder";
      strTableNameBody = "po_arriveorder_b";
      strTableNameBBody = "po_arriveorder_bb";

      strFieldNameHeadId = "carriveorderid";
      strFieldNameBodyId = "carriveorder_bid";
      strFieldNameBBodyId = "carriveorder_bid";
    } else if (sBillType.equals("25")) {
      strTableNameHead = "po_invoice";
      strTableNameBody = "po_invoice_b";
      strFieldNameHeadId = "cinvoiceid";
      strFieldNameBodyId = "cinvoice_bid";
    } else if (sBillType.equals("27")) {
      strTableNameHead = "po_settlebill";
      strTableNameBody = "po_settlebill_b";
      strFieldNameHeadId = "csettlebillid";
      strFieldNameBodyId = "csettlebill_bid";
    } else if (sBillType.equals("28")) {
      strTableNameHead = "po_priceaudit";
      strTableNameBody = "po_priceaudit_b";
      strFieldNameHeadId = "cpriceauditid";
      strFieldNameBodyId = "cpriceaudit_bid";
    } else if (sBillType.equals("29")) {
      strTableNameHead = "po_askbill";
      strTableNameBody = "po_askbill_b";
      strFieldNameHeadId = "caskbillid";
      strFieldNameBodyId = "caskbill_bid";
    } else if (sBillType.equals("2ACost")) {
      strTableNameHead = "po_costfactor";
      strTableNameBody = "po_costfactor_b";
      strFieldNameHeadId = "ccostfactorid";
      strFieldNameBodyId = "ccostfactor_bid";
    } else if ((sBillType.equals("45")) || (sBillType.equals("47")) || (sBillType.equals("4T")) || (sBillType.equals("41")) || (sBillType.equals("49")))
    {
      strTableNameHead = "ic_general_h";
      strTableNameBody = "ic_general_b";
      strTableNameBBody = "ic_general_bb3";
      strFieldNameHeadId = "cgeneralhid";
      strFieldNameBodyId = "cgeneralbid";
      strFieldNameBBodyId = "cgeneralbb3";
    } else if (sBillType.equals("21")) {
      strTableNameHead = "sc_order";
      strTableNameBody = "sc_order_b";
      strFieldNameHeadId = "corderid";
      strFieldNameBodyId = "corder_bid";
    } else if (sBillType.equals("61")) {
      strTableNameHead = "sc_order";
      strTableNameBody = "sc_order_b";
      strFieldNameHeadId = "corderid";
      strFieldNameBodyId = "corder_bid";
    } else if (sBillType.equals("62")) {
      strTableNameHead = "sc_adjustbill";
      strTableNameBody = "sc_adjustbill_b";
      strFieldNameHeadId = "cadjustbillid";
      strFieldNameBodyId = "cadjustbill_bid";
    } else if ((sBillType.equals("Z1")) || (sBillType.equals("Z2"))) {
      strTableNameHead = "ct_manage";
      strTableNameBody = "ct_manage_b";
      strFieldNameHeadId = "pk_ct_manage";
      strFieldNameBodyId = "pk_ct_manage_b";
    } else if (sBillType.equals("30")) {
      strTableNameHead = "so_sale";
      strTableNameBody = "so_saleorder_b";
      strFieldNameHeadId = "csaleid";
      strFieldNameBodyId = "corder_bid";
    } else if (sBillType.equals("2#")) {
      strTableNameHead = "po_saledata";
      strTableNameBody = "po_saledata";
      strFieldNameHeadId = "csale_bid";
      strFieldNameBodyId = "csale_bid";
    } else if (sBillType.equals("50")) {
      strTableNameHead = "ic_vmi_sum";
      strTableNameBody = "ic_vmi_sum";
      strFieldNameHeadId = "cvmihid";
      strFieldNameBodyId = "cvmihid";
    }

    Hashtable hashHTs = new Hashtable();
    Hashtable hashBTs = new Hashtable();
    Hashtable hashBbTs = new Hashtable();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      TempTableDMO dmoTempTbl = new TempTableDMO();

      Vector vTmp = new Vector();
      String[] saBillIdTmp = null;
      if (saBillid != null) {
        for (int i = 0; i < saBillid.length; i++) {
          if ((saBillid[i] == null) || (vTmp.contains(saBillid[i]))) {
            continue;
          }
          vTmp.addElement(saBillid[i]);
        }
        if (vTmp.size() > 0) {
          saBillIdTmp = new String[vTmp.size()];
          vTmp.copyInto(saBillIdTmp);
        }
      }

      String strIdSet = null;
      StringBuffer sbufSqlh = null;
      if ((saBillIdTmp != null) && (saBillIdTmp.length > 0)) {
        sbufSqlh = new StringBuffer(" SELECT ");
        sbufSqlh.append(strFieldNameHeadId);
        sbufSqlh.append(",ts ");
        sbufSqlh.append(" FROM ");
        sbufSqlh.append(strTableNameHead);
        sbufSqlh.append(" WHERE " + strFieldNameHeadId + " in ");
        strIdSet = dmoTempTbl.insertTempTable(saBillIdTmp, "t_pu_ps_33", "pk_pu");
        sbufSqlh.append(strIdSet);
        sbufSqlh.append(" and dr = 0 order by " + strFieldNameHeadId + " ");
      }

      String[] saBill_bidTmp = null;
      vTmp = new Vector();
      if (saBill_bid != null) {
        for (int i = 0; i < saBill_bid.length; i++) {
          if ((saBill_bid[i] == null) || (vTmp.contains(saBill_bid[i]))) {
            continue;
          }
          vTmp.addElement(saBill_bid[i]);
        }
        if (vTmp.size() > 0) {
          saBill_bidTmp = new String[vTmp.size()];
          vTmp.copyInto(saBill_bidTmp);
        }
      }

      StringBuffer sbufSqlb = null;
      if ((saBill_bidTmp != null) && (saBill_bidTmp.length > 0)) {
        sbufSqlb = new StringBuffer("SELECT ");
        sbufSqlb.append(strFieldNameBodyId);
        sbufSqlb.append(",ts ");
        sbufSqlb.append(" FROM ");
        sbufSqlb.append(strTableNameBody + " ");
        sbufSqlb.append(" WHERE " + strFieldNameBodyId + " in  ");
        strIdSet = dmoTempTbl.insertTempTable(saBill_bidTmp, "t_pu_ts_b", "pk_pu");
        sbufSqlb.append(strIdSet);
        sbufSqlb.append("and dr = 0 order by " + strFieldNameBodyId + " ");
      }

      String[] saBill_bbidTmp = null;
      vTmp = new Vector();
      if (saBill_bbid != null) {
        for (int i = 0; i < saBill_bbid.length; i++) {
          if ((saBill_bbid[i] == null) || (vTmp.contains(saBill_bbid[i]))) {
            continue;
          }
          vTmp.addElement(saBill_bbid[i]);
        }
        if (vTmp.size() > 0) {
          saBill_bbidTmp = new String[vTmp.size()];
          vTmp.copyInto(saBill_bbidTmp);
        }
      }

      StringBuffer sbufSqlbb = null;
      if ((saBill_bbidTmp != null) && (saBill_bbidTmp.length > 0) && (strTableNameBBody != null)) {
        sbufSqlbb = new StringBuffer("SELECT ");
        sbufSqlbb.append(strFieldNameBBodyId);
        sbufSqlbb.append(",ts ");
        sbufSqlbb.append(" FROM ");
        sbufSqlbb.append(strTableNameBBody);
        sbufSqlbb.append(" WHERE " + strFieldNameBBodyId + " in  ");
        strIdSet = dmoTempTbl.insertTempTable(saBill_bbidTmp, "t_pu_ts_bb", "pk_pu");
        sbufSqlbb.append(strIdSet);
        sbufSqlbb.append("and dr = 0 order by " + strFieldNameBBodyId + " ");
      }
      timer.addExecutePhase("临时表创建时间");

      con = getConnection();
      stmt = con.createStatement();

      if (sbufSqlh != null) {
        rs = stmt.executeQuery(sbufSqlh.toString());
        while (rs.next()) {
          hashHTs.put(rs.getString(1).trim(), rs.getString(2));
        }
        rs.close();
      }

      if (sbufSqlb != null) {
        rs = stmt.executeQuery(sbufSqlb.toString());
        while (rs.next()) {
          hashBTs.put(rs.getString(1).trim(), rs.getString(2));
        }
        rs.close();
      }

      if (sbufSqlbb != null) {
        rs = stmt.executeQuery(sbufSqlbb.toString());
        while (rs.next()) {
          hashBbTs.put(rs.getString(1).trim(), rs.getString(2));
        }
        rs.close();
      }
    } catch (Exception e) {
      throwBusinessException(e);
//      throwBusinessException(ee);
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    timer.addExecutePhase("执行查询时间");

    Object[] oRet = new Object[3];
    if (hashHTs.size() > 0) {
      iLen = saBillid.length;
      String[] saHTs = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        saHTs[i] = ((String)hashHTs.get(saBillid[i].trim()));
      }
      oRet[0] = saHTs;
    }
    if (hashBTs.size() > 0) {
      iLen = saBill_bid.length;
      String[] saBTs = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        saBTs[i] = ((String)hashBTs.get(saBill_bid[i].trim()));
      }
      oRet[1] = saBTs;
    }
    if (hashBbTs.size() > 0) {
      iLen = saBill_bbid.length;
      String[] saBbTs = new String[iLen];
      for (int i = 0; i < iLen; i++) {
        saBbTs[i] = ((String)hashBbTs.get(saBill_bbid[i].trim()));
      }
      oRet[2] = saBbTs;
    }
    timer.addExecutePhase("组织返回时间");

    timer.showAllExecutePhase("单据时间戳查询时间分布");

    return oRet;
  }

  public Hashtable queryHtResultFromAnyTable(String sTable, String sFieldName, String[] saFields, String sWhere)
    throws SQLException
  {
    StringBuffer sbufSql = new StringBuffer("SELECT DISTINCT ");
    sbufSql.append(sFieldName);
    sbufSql.append(",");
    sbufSql.append(saFields[0]);
    int iLen = saFields.length;
    for (int i = 1; i < iLen; i++) {
      sbufSql.append(",");
      sbufSql.append(saFields[i]);
    }
    sbufSql.append(" FROM ");
    sbufSql.append(sTable);
    if (PuPubVO.getString_TrimZeroLenAsNull(sWhere) != null)
    {
      sbufSql.append(" WHERE ");
      sbufSql.append(sWhere);
    }

    Hashtable hashRet = new Hashtable();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sbufSql.toString());

      while (rs.next()) {
        String sId = rs.getString(1);
        if (PuPubVO.getString_TrimZeroLenAsNull(sId) == null)
        {
          continue;
        }
        Object[] ob = new Object[saFields.length];
        for (int i = 1; i < saFields.length + 1; i++) {
          ob[(i - 1)] = rs.getObject(i + 1);
        }

        Vector vData = null;
        if (hashRet.containsKey(sId))
          vData = (Vector)hashRet.get(sId);
        else {
          vData = new Vector();
        }
        vData.addElement(ob);
        hashRet.put(sId, vData);
      }
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return hashRet.size() == 0 ? null : hashRet;
  }

  public Hashtable queryHtResultFromAnyTable2(String sTable, String sFieldName1, String sFieldName2, String[] saFields, String sWhere)
    throws SQLException
  {
    StringBuffer sbufSql = new StringBuffer("SELECT DISTINCT ");
    sbufSql.append(sFieldName1);
    sbufSql.append(",");
    sbufSql.append(sFieldName2);
    sbufSql.append(",");
    sbufSql.append(saFields[0]);
    int iLen = saFields.length;
    for (int i = 1; i < iLen; i++) {
      sbufSql.append(",");
      sbufSql.append(saFields[i]);
    }
    sbufSql.append(" FROM ");
    sbufSql.append(sTable);
    if (PuPubVO.getString_TrimZeroLenAsNull(sWhere) != null) {
      sbufSql.append(" WHERE ");
      sbufSql.append(sWhere);
    }

    Hashtable hashRet = new Hashtable();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sbufSql.toString());

      while (rs.next()) {
        String sId1 = rs.getString(1);
        if (PuPubVO.getString_TrimZeroLenAsNull(sId1) == null) {
          continue;
        }
        String sId2 = rs.getString(2);
        if (PuPubVO.getString_TrimZeroLenAsNull(sId2) == null)
        {
          continue;
        }
        Object[] ob = new Object[saFields.length];
        for (int i = 1; i < saFields.length + 1; i++) {
          ob[(i - 1)] = rs.getObject(i + 2);
        }

        Vector vData = null;
        if (hashRet.containsKey(sId1 + sId2))
          vData = (Vector)hashRet.get(sId1 + sId2);
        else {
          vData = new Vector();
        }
        vData.addElement(ob);
        hashRet.put(sId1 + sId2, vData);
      }
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return hashRet.size() == 0 ? null : hashRet;
  }

  public Object[][] queryResultsFromAnyTable(String sTable, String[] saFields, String sWhere)
    throws Exception
  {
    return new ScmPubDMO().queryResultsFromAnyTable(sTable, saFields, sWhere);
  }

  public static void setLinesStatus(AggregatedValueObject[] vos, Integer status)
  {
    if ((vos == null) || (vos.length == 0))
      return;
    if (status == null) status = new Integer(2);
    for (int i = 0; i < vos.length; i++)
      for (int j = 0; j < vos[i].getChildrenVO().length; j++)
        vos[i].getChildrenVO()[j].setStatus(status.intValue());
  }

  public static void setLineStatus(AggregatedValueObject vo, Integer status)
  {
    if ((vo == null) || (vo.getChildrenVO() == null) || (vo.getChildrenVO().length <= 0))
      return;
    if (status == null) status = new Integer(2);
    for (int i = 0; i < vo.getChildrenVO().length; i++)
      vo.getChildrenVO()[i].setStatus(status.intValue());
  }

  public static Hashtable getGroupForFirst(Object[] oaFirst, Object[] oaSecond)
  {
    Hashtable hashRet = new Hashtable();
    Vector vTmp = null;
    if ((oaFirst != null) && (oaSecond != null) && (oaFirst.length == oaSecond.length)) {
      int iLen = oaFirst.length;
      for (int i = 0; i < iLen; i++) {
        if (oaFirst[i] == null) {
          continue;
        }
        vTmp = (Vector)hashRet.get(oaFirst[i]);
        if (vTmp == null) {
          vTmp = new Vector();
        }
        vTmp.addElement(oaSecond[i]);
        hashRet.put(oaFirst[i], vTmp);
      }
    }
    return hashRet;
  }

  public static CircularlyAccessibleValueObject[] getBodysWithFree(CircularlyAccessibleValueObject[] items, String invKey, String freeKey)
    throws NamingException, BusinessException, SQLException
  {
    if ((items == null) || (items.length <= 0))
      return null;
    if ((invKey == null) || (invKey.trim().equals(""))) {
      SCMEnv.out("调用方法时参数错：未指明业务单据体上的存货管理档案的ITEMKEY 字串");
      return null;
    }
    if ((freeKey == null) || (freeKey.trim().equals(""))) {
      SCMEnv.out("调用方法时参数错：未指明业务单据体上的自由项0的ITEMKEY 字串");
      return null;
    }
    try {
      long tTime = System.currentTimeMillis();

      ArrayList allIdList = new ArrayList();
      for (int i = 0; i < items.length; i++) {
        if (items[i] == null)
          continue;
        allIdList.add((String)items[i].getAttributeValue(invKey));
      }

      FreeItemDMO freeDmo = new FreeItemDMO();
      Hashtable table = freeDmo.queryByInvIDsHash(allIdList);
      if ((table != null) && (table.size() > 0)) {
        Object oCmangid = null;
        FreeVO freeVO = null;
        for (int i = 0; i < items.length; i++) {
          oCmangid = items[i].getAttributeValue(invKey);
          if ((oCmangid == null) || (oCmangid.toString().trim().equals("")))
            continue;
          if (table.containsKey(oCmangid)) {
            freeVO = (FreeVO)table.get(oCmangid);
            if (freeVO == null)
              continue;
            if (items[i] == null)
              continue;
            freeVO.setVfree1((String)items[i].getAttributeValue("vfree1"));
            freeVO.setVfree2((String)items[i].getAttributeValue("vfree2"));
            freeVO.setVfree3((String)items[i].getAttributeValue("vfree3"));
            freeVO.setVfree4((String)items[i].getAttributeValue("vfree4"));
            freeVO.setVfree5((String)items[i].getAttributeValue("vfree5"));
            items[i].setAttributeValue(freeKey, freeVO.getVfree0());
          }
        }
      }
      tTime = System.currentTimeMillis() - tTime;
      SCMEnv.out("处理自由项时间：" + tTime + " ms!");
    } catch (Exception e) {
      SCMEnv.out(e);
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000130"), e);
    }
    return items;
  }

  public static double getRoundDouble(int priceDecimal, double d)
  {
    double temp = 1.0D;
    for (int i = 1; i <= priceDecimal; i++) temp *= 10.0D;

    long i = Math.round(d * temp);
    double dd = i / temp;

    return dd;
  }

  public static String processFirst(String s)
  {
    s = s.trim();

    int nNull = s.indexOf(" ");
    if (nNull > 0) {
      String s2 = s.substring(nNull);
      s = " and (" + s2 + ")";
      return " " + s + " ";
    }

    return s;
  }

  public static Hashtable getHashBodyByHeadKey(CircularlyAccessibleValueObject[] bodys, String strHeadKey)
  {
    if ((bodys == null) || (bodys.length <= 0) || (strHeadKey == null) || (strHeadKey.trim().equals("")))
    {
      return null;
    }Hashtable hRslt = new Hashtable();
    Vector v = null;
    String strHeadId = "";
    for (int i = 0; i < bodys.length; i++) {
      strHeadId = (String)bodys[i].getAttributeValue(strHeadKey);
      if (strHeadId == null)
        continue;
      v = (Vector)hRslt.get(strHeadId);
      if (v == null) {
        v = new Vector();
      }
      v.addElement(bodys[i]);
      hRslt.put(strHeadId, v);
    }

    Class c = bodys[0].getClass();

    if ((hRslt != null) && (hRslt.size() > 0)) {
      Enumeration keyset = hRslt.keys();
      Object key = null;
      CircularlyAccessibleValueObject[] items = null;
      while (keyset.hasMoreElements()) {
        key = keyset.nextElement();
        v = (Vector)hRslt.get(key);
        if ((v != null) && (v.size() > 0)) {
          items = (CircularlyAccessibleValueObject[])(CircularlyAccessibleValueObject[])Array.newInstance(c, v.size());
          v.copyInto(items);
          hRslt.put(key, items);
        }
      }
    }
    return hRslt;
  }

  public UFBoolean isBillCodeDuplicate(String billType, AggregatedValueObject VO)
    throws BusinessException, SQLException
  {
    beforeCallMethod("nc.bs.pu.pub.PubDMO", "isBillCodeDuplicate", new Object[] { billType, VO });

    String sql = null;
    String unitCode = null;
    String billCode = null;
    if ((billType != null) && (billType.trim().length() > 0)) {
      if (billType.trim().equals("20"))
      {
        sql = "select vpraycode from po_praybill where pk_corp = ? and vpraycode = ? and dr = 0";
        PraybillVO billVO = (PraybillVO)VO;
        unitCode = billVO.getHeadVO().getPk_corp();
        billCode = billVO.getHeadVO().getVpraycode();
      }
      else if (billType.trim().equals("21")) {
        nc.vo.po.OrderVO billVO = (nc.vo.po.OrderVO)VO;
        if (!billVO.getHeadVO().getBislatest().booleanValue()) {
          return VariableConst.UFBOOLEAN_FALSE;
        }

        sql = "select vordercode from po_order where pk_corp = ? and vordercode = ? and dr = 0 ";

        unitCode = billVO.getHeadVO().getPk_corp();
        billCode = billVO.getHeadVO().getVordercode();
      }
      else if (billType.trim().equals("28")) {
        PriceauditVO billVO = (PriceauditVO)VO;

        sql = "select vpriceauditcode from po_priceaudit where pk_corp = ? and vpriceauditcode = ? and dr = 0 ";

        unitCode = ((PriceauditHeaderVO)billVO.getParentVO()).getPk_corp();
        billCode = ((PriceauditHeaderVO)billVO.getParentVO()).getVpriceauditcode();
      }
      else if (billType.trim().equals("29")) {
        AskbillVO billVO = (AskbillVO)VO;

        sql = "select vaskbillcode from po_askbill where pk_corp = ? and vaskbillcode = ? and dr = 0 ";

        unitCode = ((AskbillHeaderVO)billVO.getParentVO()).getPk_corp();
        billCode = ((AskbillHeaderVO)billVO.getParentVO()).getVaskbillcode();
      }
      else if (billType.trim().equals("23"))
      {
        sql = "select varrordercode from po_arriveorder where pk_corp = ? and varrordercode = ? and dr = 0";
        ArriveorderVO billVO = (ArriveorderVO)VO;
        unitCode = ((ArriveorderHeaderVO)billVO.getParentVO()).getPk_corp();
        billCode = ((ArriveorderHeaderVO)billVO.getParentVO()).getVarrordercode();
      }
      else if (billType.trim().equals("61"))
      {
        sql = "select vordercode from sc_order where pk_corp = ? and vordercode = ? and dr = 0";
        nc.vo.sc.order.OrderVO billVO = (nc.vo.sc.order.OrderVO)VO;
        unitCode = ((nc.vo.sc.order.OrderHeaderVO)billVO.getParentVO()).getPk_corp();
        billCode = ((nc.vo.sc.order.OrderHeaderVO)billVO.getParentVO()).getVordercode();
      }
      else if (billType.trim().equals("62"))
      {
        sql = "select vadjustbillcode from sc_adjustbill where pk_corp = ? and vadjustbillcode = ? and dr = 0";
        AdjustbillVO billVO = (AdjustbillVO)VO;
        unitCode = ((AdjustbillHeaderVO)billVO.getParentVO()).getPk_corp();
        billCode = ((AdjustbillHeaderVO)billVO.getParentVO()).getVadjustbillcode();
      }
      else {
        SCMEnv.out("判断单据号是否重复时不支持该单据类型！");
        return VariableConst.UFBOOLEAN_FALSE;
      }
    }
    else throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000131"));

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, unitCode);
      stmt.setString(2, billCode);

      rs = stmt.executeQuery();

      while (rs.next()) {
        String s = rs.getString(1);
        if ((s != null) && (s.trim().length() > 0)) v.addElement(s); 
      }
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    if (v.size() > 0) return VariableConst.UFBOOLEAN_TRUE;
    return VariableConst.UFBOOLEAN_FALSE;
  }

  public UFBoolean isNoCloseOrderExist(String cMangID)
    throws SQLException
  {
    String sql = "select corder_bid from po_order_b where cmangid = ? and dr = 0 ";
    sql = sql + "and iisactive = " + OrderCloseItemVO.IISACTIVE_ACTIVE;

    Vector v = new Vector();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, cMangID);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String s = rs.getString(1);
        if ((s != null) && (s.trim().length() > 0)) v.addElement(s); 
      }
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    if (v.size() > 0) return new UFBoolean(true);
    return new UFBoolean(false);
  }

  public Hashtable queryHtResultFromAnyTable(String sTable, String sFieldName, String[] saFields, String[] saId)
    throws Exception
  {
    TempTableDMO dmoTempTable = new TempTableDMO();

    String sTempTableName = "t_pu_pub_01";
    String sTempId = "id";
    if (saId.length > 1) {
      sTempTableName = dmoTempTable.getTempTable(saId, sTempTableName, sTempId);
      return queryHtResultFromAnyTable(sTable + "," + sTempTableName, sFieldName, saFields, sFieldName + "=" + sTempTableName + "." + sTempId);
    }

    return queryHtResultFromAnyTable(sTable, sFieldName, saFields, sFieldName + "='" + saId[0] + "' ");
  }

  public static ArrayList splitCondsByNormalSize(Object[] ids, boolean isFilterNull)
  {
    if ((ids == null) || (ids.length == 0)) return null;

    int normalSize = 100;

    ArrayList strlist = new ArrayList();

    Vector tempV = null;
    int totalCount = 0;

    while (totalCount < ids.length) {
      int circuCount = 0;
      tempV = new Vector();
      while ((circuCount < normalSize) && (totalCount < ids.length))
      {
        if ((ids[totalCount] == null) && (isFilterNull)) {
          totalCount++;
          continue;
        }
        tempV.addElement(ids[totalCount]);
        circuCount++;
        totalCount++;
      }
      if (tempV.size() > 0) {
        Object[] objs = new Object[tempV.size()];
        tempV.copyInto(objs);
        strlist.add(objs);
      }
    }

    return strlist;
  }

  public static String[] splitCondsByNormalSize(String fieldName, Object objs)
  {
    if (fieldName == null) return null;
    if (objs == null) return null;
    String[] ids = null;
    if (objs.getClass().isArray()) {
      ids = (String[])(String[])objs;
    }
    else if ((objs instanceof Vector)) {
      Vector tV = (Vector)objs;
      if ((tV == null) || (tV.size() == 0)) return null;
      ids = new String[tV.size()];
      tV.copyInto(ids);
    } else {
      return null;
    }

    int normalSize = 100;

    String[] strArray = null;

    boolean isNullExist = false;
    StringBuffer strBuf = new StringBuffer();
    Vector tempV = new Vector();
    int totalCount = 0;

    while (totalCount < ids.length) {
      int circuCount = 0;
      while ((circuCount < normalSize) && (totalCount < ids.length))
      {
        if ((ids[totalCount] == null) || (ids[totalCount].equals(""))) {
          isNullExist = true;
          totalCount++;
          continue;
        }

        if (strBuf.length() == 0) {
          strBuf.append(" (" + fieldName + " = '" + ids[totalCount].toString() + "'");
        }
        else {
          strBuf.append(" or " + fieldName + " = '" + ids[totalCount].toString() + "'");
        }
        circuCount++;
        totalCount++;
      }
      if ((strBuf != null) && (strBuf.length() > 0)) {
        strBuf.append(")");
        tempV.add(strBuf.toString());
      }
      strBuf = new StringBuffer();
    }

    if (isNullExist) tempV.add(" " + fieldName + " is null");

    if ((tempV != null) && (tempV.size() > 0)) {
      strArray = new String[tempV.size()];
      tempV.copyInto(strArray);
    }

    return strArray;
  }

  public static void throwBusinessException1(String sMethod, Exception e)
    throws BusinessException
  {
    if ((e instanceof BusinessException)) {
      throw ((BusinessException)e);
    }
    SCMEnv.out(e);
    throw new BusinessException(e.getMessage());
  }

  private String[] getAreaClCodeByLike(String fatherClCode)
    throws SQLException
  {
    beforeCallMethod("nc.bs.pu.pub.PubDMO", "getAreaClCodeByLike", new Object[] { fatherClCode });

    String sql = "select areaclcode from bd_areacl where dr = 0 and areaclcode like '%" + fatherClCode + "%'";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();

      while (rs.next())
        v.addElement(rs.getString(1));
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    afterCallMethod("nc.bs.pu.pub.PubDMO", "getAreaClCodeByLike", new Object[] { fatherClCode });

    if (v.size() > 0) {
      String[] s = new String[v.size()];
      v.copyInto(s);
      return s;
    }
    return null;
  }

  public static int getCCurrDecimal(String strPkCorp)
    throws Exception
  {
    Integer iTmp = null;

    if ((m_hasDigitRMB == null) || (!m_hasDigitRMB.containsKey(strPkCorp))) {
      try {
        SysInitDMO dmo = new SysInitDMO();
        SysInitVO initVO = dmo.queryByParaCode(strPkCorp, "BD301");
        if (initVO != null)
        {
          String s = new POPubSetImpl().getCurrDigit(initVO.getPkvalue());
          if ((s != null) && (s.length() > 0))
            iTmp = new Integer(s.trim());
        }
      }
      catch (Exception e) {
        SCMEnv.out("取公司主键为：" + strPkCorp + " 的币种精度时出错！");
        SCMEnv.out(e);
        throw e;
      }
      if (m_hasDigitRMB == null) {
        m_hasDigitRMB = new Hashtable();
      }
      if (iTmp == null) {
        iTmp = new Integer(2);
      }
      m_hasDigitRMB.put(strPkCorp, iTmp);
    }

    iTmp = (Integer)m_hasDigitRMB.get(strPkCorp);

    if (iTmp == null) {
      return 2;
    }
    return iTmp.intValue();
  }

  /** @deprecated */
  public String[] getInSetsByIds(Object objs)
  {
    if (objs == null)
      return null;
    String[] ids = null;
    if (objs.getClass().isArray()) {
      ids = (String[])(String[])objs;
    } else if ((objs instanceof Vector)) {
      Vector tV = (Vector)objs;
      if ((tV == null) || (tV.size() == 0))
        return null;
      ids = new String[tV.size()];
      tV.copyInto(ids);
    } else {
      return null;
    }

    int normalSize = 40;

    String[] strArray = null;

    StringBuffer strBuf = new StringBuffer();
    Vector tempV = new Vector();
    int totalCount = 0;

    while (totalCount < ids.length) {
      int circuCount = 0;
      while ((circuCount < normalSize) && (totalCount < ids.length))
      {
        if ((ids[totalCount] == null) || (ids[totalCount].equals(""))) {
          totalCount++;
          continue;
        }

        if (strBuf.length() == 0)
          strBuf.append(" ('" + ids[totalCount].toString() + "'");
        else {
          strBuf.append(",'" + ids[totalCount].toString() + "'");
        }
        circuCount++;
        totalCount++;
      }
      if ((strBuf != null) && (strBuf.length() > 0)) {
        strBuf.append(") ");
        tempV.add(strBuf.toString());
      }
      strBuf = new StringBuffer();
    }

    if ((tempV != null) && (tempV.size() > 0)) {
      strArray = new String[tempV.size()];
      tempV.copyInto(strArray);
    }

    return strArray;
  }

  public static String getLocalCurrId(String strPkCorp)
    throws Exception
  {
    SysInitVO initVO = new SysInitDMO().queryByParaCode(strPkCorp, "BD301");

    if (initVO == null) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000132"));
    }
    return initVO.getPkvalue();
  }

  public static int getPricePriorPolicy(String pk_corp)
    throws Exception
  {
    String sPara = getPricePriorPolicyString(pk_corp);

    if (sPara.equals("含税价格优先")) {
      return 5;
    }
    return 6;
  }

  public static String getPricePriorPolicyString(String pk_corp)
    throws Exception
  {
    String sPara = null;
    try
    {
      sPara = new SysInitDMO().getParaString(pk_corp, "PO28");
    } catch (Exception e) {
      SCMEnv.out(e);
      throw e;
    }

    if (sPara == null) {
      sPara = "含税价格优先";
    }

    return sPara;
  }

  public String[] getSubAreaClassCode(String unitCode, String[] fatherClCode)
    throws Exception
  {
    if ((fatherClCode == null) || (fatherClCode.length == 0)) {
      return null;
    }

    String sCodeSubSql = new TempTableDMO().insertTempTable(fatherClCode, "t_pu_id_in_19", "id");

    String sql = "select pk_areacl from bd_areacl where dr = 0 and areaclcode in " + sCodeSubSql;

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Vector v = new Vector();
    for (int i = 0; i < fatherClCode.length; i++)
      v.addElement(fatherClCode[i]);
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);

      rs = stmt.executeQuery();

      Vector vTemp = new Vector();
      while (rs.next()) {
        String s = rs.getString(1);
        if ((s != null) && (s.trim().length() > 0)) {
          vTemp.addElement(s);
        }
      }
      IBDAccessor acc = AccessorManager.getAccessor("00010000000000000012", unitCode);
      if (vTemp.size() > 0)
        for (int i = 0; i < vTemp.size(); i++) {
          String pk_areacl = (String)vTemp.elementAt(i);
          if ((pk_areacl != null) && (pk_areacl.trim().length() > 0)) {
            BddataVO[] VOs = acc.getChildDocs(pk_areacl);
            if ((VOs != null) && (VOs.length > 0))
              for (int j = 0; j < VOs.length; j++)
                v.addElement(VOs[j].getCode());
          }
        }
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    String[] code = new String[v.size()];
    v.copyInto(code);

    return code;
  }

  public String[] getSubAreaCodes(String unitCode, String codeSegment, String operaCode)
    throws Exception
  {
    String[] fatherCode = null;

    if ((operaCode == null) || (operaCode.trim().length() == 0)) return null;
    if (operaCode.toLowerCase().trim().equals("like")) {
      fatherCode = getAreaClCodeByLike(codeSegment);
    }
    if ((fatherCode == null) || (fatherCode.length == 0)) {
      fatherCode = new String[1];
      fatherCode[0] = codeSegment;
    }

    String[] areaClCode = getSubAreaClassCode(unitCode, fatherCode);

    if ((areaClCode == null) || (areaClCode.length == 0)) {
      areaClCode = new String[1];
      areaClCode[0] = codeSegment;
    }

    return areaClCode;
  }

  public AggregatedValueObject getVendorAndInvBaseIDs(AggregatedValueObject voProcess)
    throws Exception
  {
    CircularlyAccessibleValueObject headVO = voProcess.getParentVO();
    CircularlyAccessibleValueObject[] bodyVO = voProcess.getChildrenVO();

    String[] saMangId = (String[])(String[])OrderPubVO.getDistinctFieldArray(bodyVO, "cmangid", String.class);
    String sMangIdSubSql = new TempTableDMO().insertTempTable(saMangId, "t_pu_id_in_20", "id");

    String sql1 = "select pk_cubasdoc from bd_cumandoc where dr = 0 and pk_cumandoc = ?";
    String sql2 = "select pk_invmandoc, pk_invbasdoc from bd_invmandoc where dr = 0 and pk_invmandoc in " + sMangIdSubSql;

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Hashtable t = new Hashtable();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql1);
      stmt.setString(1, (String)headVO.getAttributeValue("cvendormangid"));
      rs = stmt.executeQuery();

      if (rs.next()) {
        String s = rs.getString(1);
        if ((s != null) && (s.trim().length() > 0))
          headVO.setAttributeValue("cvendorbaseid", s);
      }
      if (stmt != null) {
        stmt.close();
      }
      stmt = con.prepareStatement(sql2);
      rs = stmt.executeQuery();
      while (rs.next()) {
        String s1 = rs.getString(1);
        String s2 = rs.getString(2);
        if ((s1 != null) && (s1.trim().length() > 0) && (s2 != null) && (s2.trim().length() > 0))
          t.put(s1, s2);
      }
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    for (int i = 0; i < bodyVO.length; i++) {
      String s1 = (String)bodyVO[i].getAttributeValue("cmangid");
      if ((s1 != null) && (s1.trim().length() > 0)) {
        Object o = t.get(s1);
        if (o != null) {
          bodyVO[i].setAttributeValue("cbaseid", o.toString());
        }
      }
    }
    voProcess.setParentVO(headVO);
    voProcess.setChildrenVO(bodyVO);
    return voProcess;
  }

  public String[] getVendorCodeByAreaClassCode(String unitCode, String fatherClCode, String operaCode)
    throws Exception
  {
    String[] fatherCode = null;

    if ((operaCode == null) || (operaCode.trim().length() == 0))
      return null;
    if (operaCode.toLowerCase().trim().equals("like")) {
      fatherCode = getAreaClCodeByLike(fatherClCode);
    }
    if ((fatherCode == null) || (fatherCode.length == 0)) {
      fatherCode = new String[1];
      fatherCode[0] = fatherClCode;
    }

    String[] areaClCode = getSubAreaClassCode(unitCode, fatherCode);
    if ((areaClCode == null) || (areaClCode.length == 0)) {
      return null;
    }

    String sCodeSubSql = new TempTableDMO().insertTempTable(areaClCode, "t_pu_id_in_21", "id");

    String sql = "select distinct custcode from bd_cubasdoc A, bd_cumandoc B, bd_areacl C ";
    sql = sql + "where B.pk_corp = '" + unitCode + "' and A.pk_cubasdoc = B.pk_cubasdoc and A.pk_areacl = C.pk_areacl and areaclcode in " + sCodeSubSql;

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();

      while (rs.next())
        v.addElement(rs.getString(1));
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    if (v.size() > 0) {
      String[] s = new String[v.size()];
      v.copyInto(s);
      return s;
    }
    return null;
  }

  public String[] getVendorCodeByAreaClassCodeForAskReport(String unitCode, String fatherClCode, String operaCode, String[] unitCodes)
    throws Exception
  {
    String[] fatherCode = null;

    if ((operaCode == null) || (operaCode.trim().length() == 0))
      return null;
    if (operaCode.toLowerCase().trim().equals("like")) {
      fatherCode = getAreaClCodeByLike(fatherClCode);
    }
    if ((fatherCode == null) || (fatherCode.length == 0)) {
      fatherCode = new String[1];
      fatherCode[0] = fatherClCode;
    }

    String[] areaClCode = getSubAreaClassCode(unitCode, fatherCode);
    if ((areaClCode == null) || (areaClCode.length == 0)) {
      return null;
    }

    String sCodeSubSql = new TempTableDMO().insertTempTable(areaClCode, "t_pu_id_in_21", "id");

    String sCorpSubSql = new TempTableDMO().insertTempTable(unitCodes, "t_pu_id_in_22", "id");

    String sql = "select distinct custcode from bd_cubasdoc A, bd_cumandoc B, bd_areacl C ";
    sql = sql + "where B.pk_corp in " + sCorpSubSql + " and A.pk_cubasdoc = B.pk_cubasdoc and A.pk_areacl = C.pk_areacl and areaclcode in " + sCodeSubSql;

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Vector v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();

      while (rs.next())
        v.addElement(rs.getString(1));
    }
    finally
    {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    if (v.size() > 0) {
      String[] s = new String[v.size()];
      v.copyInto(s);
      return s;
    }
    return null;
  }

  public UFBoolean isPrayInvCodeDuplicated(AggregatedValueObject VO)
    throws BusinessException, SQLException
  {
    if (VO == null) return new UFBoolean(true);

    Vector v = new Vector();

    PraybillVO billVO = (PraybillVO)VO;
    PraybillItemVO[] bodyVO = billVO.getBodyVO();
    if ((bodyVO == null) || (bodyVO.length == 0)) return new UFBoolean(true);

    Vector vTemp = new Vector();
    for (int i = 0; i < bodyVO.length; i++) {
      if (bodyVO[i].getBodyEditStatus() == 3) continue; vTemp.addElement(bodyVO[i]);
    }
    bodyVO = new PraybillItemVO[vTemp.size()];
    vTemp.copyInto(bodyVO);
    if ((bodyVO == null) || (bodyVO.length == 0)) return new UFBoolean(true);
    vTemp = new Vector();

    vTemp.addElement(bodyVO[0].getCbaseid().trim());
    for (int i = 1; i < bodyVO.length; i++) {
      String s = bodyVO[i].getCbaseid().trim();
      if (vTemp.contains(s)) v.addElement(s); else {
        vTemp.addElement(s);
      }
    }
    if (v.size() == 0) return new UFBoolean(true);

    String[] sTemp0 = new String[v.size()];
    v.copyInto(sTemp0);
    String sTemp1 = null;
    try {
      sTemp1 = new TempTableDMO().insertTempTable(sTemp0, "t_pu_id_in_20", "pk_pu");
    } catch (Exception e) {
      throw new SQLException(e.getMessage());
    }
    String sql = "select invcode from bd_invbasdoc where pk_invbasdoc in " + sTemp1;

    PreparedStatement stmt = null;
    Connection con = null;
    ResultSet rs = null;
    v = new Vector();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      rs = stmt.executeQuery();

      while (rs.next()) {
        String s = rs.getString(1);
        if ((s != null) && (s.trim().length() > 0)) v.addElement(s); 
      }
    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    if (v.size() > 0) {
      String s = "";
      for (int i = 0; i < v.size() - 1; i++) s = s + v.elementAt(i) + ",";
      s = s + v.elementAt(v.size() - 1);
      if (s.trim().length() > 0) throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("common", "UC000-0001480") + s + NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000208"));
    }

    return new UFBoolean(false);
  }

  public boolean isProdctEnabed(String strCorpId, String strModelCode)
    throws BusinessException
  {
    boolean flag = false;
    if ((strCorpId == null) || (strCorpId.trim().length() == 0) || (strModelCode == null) || (strModelCode.trim().length() == 0))
    {
      SCMEnv.out("参数公司主键或产品编码出现空值");
      return flag;
    }
    try {
      ICreateCorpQueryService corpDmo = (ICreateCorpQueryService)NCLocator.getInstance().lookup(ICreateCorpQueryService.class.getName());

      if (corpDmo.isEnabled(strCorpId, strModelCode))
        flag = true;
    }
    catch (Exception e) {
      throwBusinessException(e);
    }
    return flag;
  }

  public UFDouble[][] queryAdvancePara(String sPkCorp, String[] saStoreOrgId, String[] saInvBaseId)
    throws SQLException
  {
    String sql = "select fixedahead, aheadcoff, aheadbatch from bd_produce where pk_corp = ? and pk_calbody = ? and pk_invbasdoc = ?";

    int iLen = saInvBaseId.length;
    HashMap mapRet = new HashMap();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      for (int i = 0; i < iLen; i++) {
        stmt.setString(1, sPkCorp);
        stmt.setString(2, saStoreOrgId[i]);
        stmt.setString(3, saInvBaseId[i]);
        rs = stmt.executeQuery();

        if (rs.next()) {
          UFDouble[] daValue = new UFDouble[3];
          daValue[0] = PuPubVO.getUFDouble_NullAsZero(rs.getBigDecimal(1));
          daValue[1] = PuPubVO.getUFDouble_NullAsZero(rs.getBigDecimal(2));
          daValue[2] = PuPubVO.getUFDouble_NullAsZero(rs.getBigDecimal(3));
          mapRet.put(saStoreOrgId[i] + saInvBaseId[i], daValue);
        }

        rs.close();
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    UFDouble[][] daValue = new UFDouble[iLen][3];
    for (int i = 0; i < iLen; i++) {
      daValue[i] = ((UFDouble[])(UFDouble[])mapRet.get(saStoreOrgId[i] + saInvBaseId[i]));
    }
    return daValue;
  }

  public HashMap queryArrayValues(String sTable, String sIdName, String[] saFields, String[] saId, String sWhere)
    throws BusinessException
  {
    Log log = Log.getInstance(getClass().getName());

    StringBuffer sbufSql = new StringBuffer("SELECT ");
    sbufSql.append(sIdName);
    sbufSql.append(",");
    sbufSql.append(saFields[0]);
    int iLen = saFields.length;
    for (int i = 1; i < iLen; i++) {
      sbufSql.append(",");
      sbufSql.append(saFields[i]);
    }
    sbufSql.append(" FROM ");
    sbufSql.append(sTable);
    sbufSql.append(" WHERE  ");
    sbufSql.append(sIdName + " in ");
    String strIdSet = "";
    try {
      TempTableDMO dmoTmpTbl = new TempTableDMO();
      strIdSet = dmoTmpTbl.insertTempTable(saId, "t_pu_ps_35", "pk_pu");
      if ((strIdSet == null) || (strIdSet.trim().length() == 0))
        strIdSet = "('TempTableDMOError')";
    } catch (Exception e) {
      log.debug(e);
      throw new BusinessException(e.getMessage());
    }
    sbufSql.append(strIdSet + " ");

    if ((sWhere != null) && (sWhere.trim().length() > 1)) {
      sbufSql.append("and ");
      sbufSql.append(sWhere);
    }

    HashMap hRet = new HashMap();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sbufSql.toString());
      while (rs.next()) {
        String sId = rs.getString(1);
        Object[] ob = new Object[saFields.length];
        for (int i = 1; i < saFields.length + 1; i++) {
          ob[(i - 1)] = rs.getObject(i + 1);
        }
        hRet.put(sId, ob);
      }
    } catch (SQLException ee) {
      log.debug(ee);
      throw new BusinessException(ee.getMessage());
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return hRet;
  }

  public void sendMsgToUser(PraybillVO[] voaPray, String strSender)
    throws Exception
  {
    String sMethodName = "nc.bs.pu.pub.PubDMO.sendMsgToUser(PraybillVO [], String)";
    SCMEnv.out("审批完成发送消息开始...");

    if ((voaPray == null) || (voaPray.length <= 0)) {
      SCMEnv.out("传入请购单参数为空，发送消息结束");
      return;
    }
    int iLen = voaPray.length; int jLen = 0;

    String pk_corp = null;
    for (int i = 0; i < iLen; i++) {
      if ((voaPray[i] != null) && (voaPray[i].getParentVO() != null) && (voaPray[i].getParentVO().getAttributeValue("pk_corp") != null)) {
        pk_corp = (String)voaPray[i].getParentVO().getAttributeValue("pk_corp");
        break;
      }
    }
    if ((pk_corp == null) || (pk_corp.trim().equals(""))) {
      SCMEnv.out("数据错：未获取请购单所在公司");
      return;
    }
    String str = fetchSysPara(pk_corp, "PO43");
    if ((str == null) || (str.trim().length() == 0)) {
      str = "否";
    }
    if ("否".equals(str.trim())) {
      SCMEnv.out("请购单审批完成后，向操作员发消息参数为‘否’,直接返回");
      return;
    }
    PraybillItemVO[] items = null;

    Vector vPsn = new Vector();
    String strPsn = null;
    for (int i = 0; i < iLen; i++) {
      items = (PraybillItemVO[])(PraybillItemVO[])voaPray[i].getChildrenVO();
      if ((items == null) || (items.length <= 0))
        continue;
      jLen = items.length;
      for (int j = 0; j < jLen; j++) {
        if (items[j] == null)
          continue;
        strPsn = items[j].getCemployeeid();
        if ((strPsn == null) || (strPsn.trim().equals("")))
          continue;
        if (!vPsn.contains(strPsn.trim())) {
          vPsn.addElement(strPsn);
        }
      }
    }
    if (vPsn.size() == 0) {
      SCMEnv.out("请购单表体未录入专管业务员,直接返回");
      return;
    }

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    StringBuffer sbufSql = new StringBuffer("SELECT sm_userandclerk.pk_psndoc, sm_userandclerk.userid, sm_user.user_code, sm_user.user_name ");
    sbufSql.append("FROM sm_userandclerk LEFT OUTER JOIN sm_user ON sm_user.cuserid = sm_userandclerk.userid ");
    sbufSql.append("where sm_userandclerk.pk_psndoc in (select pk_psnbasdoc from bd_psndoc where ");
    iLen = vPsn.size();
    for (int i = 0; i < iLen; i++) {
      if (i == 0)
        sbufSql.append("pk_psndoc = '");
      else
        sbufSql.append("or pk_psndoc = '");
      sbufSql.append(vPsn.elementAt(i));
      sbufSql.append("' ");
    }
    sbufSql.append(") ");

    Hashtable hPsnIdUserId = new Hashtable();
    Hashtable hUserIdCode = new Hashtable();
    Hashtable hUserIdName = new Hashtable();
    Vector vId = null;
    String strPsnId = ""; String strUserId = ""; String strUserCode = ""; String strUserName = "";
    try {
      con = getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sbufSql.toString());

      while (rs.next()) {
        strPsnId = rs.getString(1);
        strUserId = rs.getString(2);
        strUserCode = rs.getString(3);
        strUserName = rs.getString(4);

        if ((strPsnId == null) || (strPsnId.trim().equals("")) || (strUserId == null) || (strUserId.trim().equals("")))
          continue;
        vId = (Vector)hPsnIdUserId.get(strPsnId);
        if (vId == null) {
          vId = new Vector();
        }
        vId.addElement(strUserId);
        hPsnIdUserId.put(strPsnId, vId);

        hUserIdCode.put(strUserId, strUserCode);

        hUserIdName.put(strUserId, strUserName);
      }

    }
    finally
    {
      try
      {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    if (hPsnIdUserId.size() <= 0) {
      SCMEnv.out("请购单专管业务员找不到相关操作员的关联定义，不能发送消息，直接返回");
      SCMEnv.out("可修改操作员档案(客户化->权限管理->操作员)，使之与请购单专管业务员对应");
      return;
    }

    Hashtable tablePsnMangBaseId = queryHtResultFromAnyTable("bd_psndoc", "pk_psndoc", new String[] { "pk_psnbasdoc" }, (String[])(String[])vPsn.toArray(new String[vPsn.size()]));

    Vector vUserId = new Vector(); Vector vInfo = new Vector(); Vector vTmp = new Vector();
    iLen = voaPray.length;
    String strOrderCode = null;
    int iSize = 0;
    for (int i = 0; i < iLen; i++) {
      if (voaPray[i].getParentVO() == null)
        continue;
      strOrderCode = (String)voaPray[i].getParentVO().getAttributeValue("vpraycode");
      items = (PraybillItemVO[])(PraybillItemVO[])voaPray[i].getChildrenVO();
      if ((items == null) || (items.length <= 0))
        continue;
      jLen = items.length;
      String strPsnMangId = null;
      for (int j = 0; j < jLen; j++) {
        if (items[j] == null)
          continue;
        strPsn = items[j].getCemployeeid();
        if ((strPsn == null) || (strPsn.trim().equals("")))
          continue;
        Object[] oaRet = (Object[])(Object[])((Vector)tablePsnMangBaseId.get(strPsn.trim())).get(0);
        strPsnMangId = (String)oaRet[0];
        vTmp = (Vector)hPsnIdUserId.get(strPsnMangId);
        if ((vTmp == null) || (vTmp.size() <= 0)) {
          continue;
        }
        iSize = vTmp.size();
        for (int m = 0; m < iSize; m++) {
          vUserId.addElement(vTmp.elementAt(m));
          String[] value = { strOrderCode };
          vInfo.addElement(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000139", null, value));
        }
      }
    }
    if (vUserId.size() <= 0) {
      SCMEnv.out("未正确获取操作员消息，直接返回");
      return;
    }

    CommonMessageVO[] cmVOs = new CommonMessageVO[vUserId.size()];
    UserNameObject[] uoVOs = null;
    UserNameObject uoVO = null;
    iLen = vUserId.size();
    SCMEnv.out("NCMMOUT：共获取操作员:#" + iLen + "#个");
    for (int i = 0; i < iLen; i++) {
      strUserId = (String)vUserId.elementAt(i);
      strUserCode = (String)hUserIdCode.get(strUserId);
      strUserName = (String)hUserIdName.get(strUserId);
      uoVO = new UserNameObject(strUserName == null ? "" : strUserName.trim());
      uoVO.setUserCode(strUserCode == null ? "" : strUserCode.trim());
      uoVO.setUserPK(strUserId);
      uoVOs = new UserNameObject[] { uoVO };
      cmVOs[i] = new CommonMessageVO();
      cmVOs[i].setMessageContent((String)vInfo.elementAt(i));
      cmVOs[i].setReceiver(uoVOs);
      cmVOs[i].setSender(strSender);
      cmVOs[i].setTitle(NCLangResOnserver.getInstance().getStrByID("4004pub", "UPP4004pub-000133"));
      cmVOs[i].setSendDataTime(new UFDateTime(System.currentTimeMillis()));
    }

    try
    {
      for (int i = 0; i < iLen; i++)
        new PFMessageBO().insertCommonMessage(cmVOs[i]);
    }
    catch (Exception e) {
      reportException(e);
      throwBusinessException(sMethodName, e);
    }
    SCMEnv.out("请购单审批完成发送消息正常结束,共发送##" + iLen + "##条消息");
  }

  public void setDefaultMeas(PraybillVO[] voaPray, Object oSrcVo)
    throws Exception
  {
    String pk_corp = ((PraybillHeaderVO)voaPray[0].getParentVO()).getPk_corp();
    PubDMO pubDmo = new PubDMO();

    String sDefaultUnit = pubDmo.fetchSysPara(pk_corp, "PO08");
    if ((sDefaultUnit == null) || (sDefaultUnit.trim().length() == 0))
      sDefaultUnit = "Y";
    HashMap hRate = new HashMap();
    SaleOrderVO srcVo = null;
    SaleorderBVO[] srcItems = null;
    if ((oSrcVo != null) && ((oSrcVo instanceof SaleOrderVO))) {
      srcVo = (SaleOrderVO)oSrcVo;
      srcItems = (SaleorderBVO[])(SaleorderBVO[])srcVo.getChildrenVO();
      ArrayList arrCbaseId = new ArrayList();
      ArrayList arrCassisUnit = new ArrayList();
      String[] sCbaseId = null;
      String[] sCassisUnit = null;
      if (srcItems != null) {
        for (int i = 0; i < srcItems.length; i++) {
          String key1 = srcItems[i].getCinvbasdocid();
          String key2 = srcItems[i].getCpackunitid();
          if ((key1 != null) && (key1.trim().length() > 0) && (key2 != null) && (key2.trim().length() > 0)) {
            arrCbaseId.add(key1);
            arrCassisUnit.add(key2);
          }
        }
      }
      if ((arrCbaseId.size() > 0) && (arrCassisUnit.size() > 0)) {
        sCbaseId = new String[arrCbaseId.size()];
        arrCbaseId.toArray(sCbaseId);
        sCassisUnit = new String[arrCassisUnit.size()];
        arrCassisUnit.toArray(sCassisUnit);
      }
      if ((sCbaseId != null) && (sCassisUnit != null))
        hRate = new ScmPubDMO().loadBatchInvConvRateInfo(sCbaseId, sCassisUnit);
    }
    try {
      int iLen = voaPray.length;
      for (int i = 0; i < iLen; i++) {
        int jLen = voaPray[i].getBodyVO().length;
        String[] sCbaseIds = new String[jLen];
        for (int j = 0; j < jLen; j++) {
          sCbaseIds[j] = voaPray[i].getBodyVO()[j].getCbaseid();
        }

        HashMap hAssisUnit = pubDmo.queryArrayValues("bd_invbasdoc", "pk_invbasdoc", new String[] { "pk_measdoc2" }, sCbaseIds, null);

        String[] sAssisUnit = new String[jLen];
        if (hAssisUnit != null) {
          for (int j = 0; j < jLen; j++) {
            sCbaseIds[j] = voaPray[i].getBodyVO()[j].getCbaseid();
            Object[] ob = (Object[])(Object[])hAssisUnit.get(sCbaseIds[j]);
            if ((ob != null) && (ob[0] != null)) {
              sAssisUnit[j] = ob[0].toString();
            }
          }
        }
        HashMap hConstrRate = new ScmPubDMO().loadBatchInvConvRateInfo(sCbaseIds, sAssisUnit);

        for (int j = 0; j < jLen; j++) {
          String sassistunit = null;
          String baseid = voaPray[i].getBodyVO()[j].getCbaseid();
          if ((sDefaultUnit.toUpperCase().equals("Y")) && 
            (hAssisUnit != null)) {
            Object[] ob = (Object[])(Object[])hAssisUnit.get(baseid);
            if ((ob != null) && (ob[0] != null))
              sassistunit = ob[0].toString();
            voaPray[i].getBodyVO()[j].setCassistunit(sassistunit);
          }

          sassistunit = voaPray[i].getBodyVO()[j].getCassistunit();
          if ((sassistunit != null) && (sassistunit.trim().length() > 0))
          {
            if ((sDefaultUnit.toUpperCase().equals("Y")) || (sDefaultUnit.toUpperCase().equals("是"))) {
              if ((hConstrRate != null) && (hConstrRate.size() > 0) && (voaPray[i].getBodyVO()[j].getNpraynum() != null)) {
                Object[] ob = (Object[])(Object[])hConstrRate.get(baseid + sassistunit);
                if ((ob != null) && (ob[0] != null)) {
                  UFDouble x = new UFDouble(ob[0].toString());
                  voaPray[i].getBodyVO()[j].setNassistnum(new UFDouble(voaPray[i].getBodyVO()[j].getNpraynum().doubleValue() / x.doubleValue()));
                }
              } else {
                if ((hConstrRate == null) || (hConstrRate.size() != 0) || (voaPray[i].getBodyVO()[j].getNpraynum() == null) || 
                  (voaPray[i].getBodyVO()[j].getCupsourcebilltype() == null) || (!voaPray[i].getBodyVO()[j].getCupsourcebilltype().equals("A1"))) continue;
                voaPray[i].getBodyVO()[j].setNassistnum(new UFDouble(voaPray[i].getBodyVO()[j].getNpraynum().doubleValue()));
              }

            }
            else if ((oSrcVo != null) && ((oSrcVo instanceof SaleOrderVO))) {
              String key1 = voaPray[i].getBodyVO()[j].getCbaseid();
              String key2 = voaPray[i].getBodyVO()[j].getCassistunit();
              if ((key1 == null) || (key1.trim().length() <= 0) || (key2 == null) || (key2.trim().length() <= 0))
                continue;
              Object[] ob = (Object[])(Object[])hRate.get(key1 + key2);
              UFBoolean bRate = null;
              if ((ob != null) && (ob[1] != null))
                bRate = (UFBoolean)ob[1];
              UFDouble ufRate = null;
              if ((bRate != null) && (bRate.booleanValue())) {
                ufRate = (UFDouble)ob[0];
                if ((ufRate != null) && (!ufRate.equals(new UFDouble("0.0")))) {
                  voaPray[i].getBodyVO()[j].setNassistnum(voaPray[i].getBodyVO()[j].getNpraynum().div(ufRate));
                }
              }
            }
          }
          else
            voaPray[i].getBodyVO()[j].setNassistnum(null);
        }
      }
    } catch (Exception e) {
      SCMEnv.out(e);
      throw e;
    }
  }

  public static void throwBusinessException(Exception e)
    throws BusinessException
  {
    if ((e instanceof BusinessException)) {
      throw ((BusinessException)e);
    }
    SCMEnv.out(e);
    throw new BusinessException(e.getMessage());
  }

  public static void throwBusinessException(String sMethod, Exception e)
    throws BusinessException
  {
    SCMEnv.out("出现异常的方法：" + sMethod);
    if ((e instanceof BusinessException)) {
      throw ((BusinessException)e);
    }
    SCMEnv.out(e);
    throw new BusinessException(e.getMessage());
  }

  public Hashtable queryHtResultFromAnyTable(String sTable, String sFieldName, String[] saFields)
    throws SQLException
  {
    return queryHtResultFromAnyTable(sTable, sFieldName, saFields, "");
  }

  public ArrayList queryInfosFor29To21(ArrayList listId)
    throws Exception
  {
    if ((listId == null) || (listId.size() == 0)) {
      return null;
    }

    StringBuffer sbufSql = new StringBuffer("select ");

    sbufSql.append("po_praybill_b.cpraybill_bid,po_praybill_b.pk_reqstoorg,po_praybill_b.cwarehouseid,po_praybill.cdeptid ");
    sbufSql.append("from po_praybill,po_praybill_b where po_praybill.cpraybillid = po_praybill_b.cpraybillid ");
    sbufSql.append("and po_praybill_b.cpraybill_bid in ");
    String strIdSet = "";
    try {
      TempTableDMO dmoTmpTbl = new TempTableDMO();
      strIdSet = dmoTmpTbl.insertTempTable(listId, "t_pu_ask_03", "pk_pu");
      if ((strIdSet == null) || (strIdSet.trim().length() == 0))
        strIdSet = "('TempTableDMOError')";
    } catch (Exception e) {
      SCMEnv.out(e);
      throw e;
    }
    sbufSql.append(strIdSet + " ");

    Hashtable hashRet = new Hashtable();
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery(sbufSql.toString());

      String strId = null;
      ArrayList listVal = null;
      while (rs.next()) {
        listVal = null;
        strId = rs.getString(1);
        if (strId != null) {
          listVal = new ArrayList();
          listVal.add(rs.getString(2));
          listVal.add(rs.getString(3));
          listVal.add(rs.getString(4));
          hashRet.put(strId, listVal);
        }
      }
    }
    finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      } catch (Exception e) {
      }
    }
    ArrayList listRet = null;
    if (hashRet.size() > 0) {
      int iLen = listId.size();
      listRet = new ArrayList();
      for (int i = 0; i < iLen; i++) {
        listRet.add((ArrayList)hashRet.get(listId.get(i)));
      }
    }
    return listRet;
  }

  public HashMap getRatesArray(String[] saPk_corp, String[] saCurrTypdId)
    throws BusinessException, SQLException
  {
    StringBuffer sbSql = new StringBuffer();

    ArrayList listTblData = new ArrayList();
    ArrayList listTmp = null;
    int iLen = saPk_corp.length;
    for (int i = 0; i < iLen; i++) {
      listTmp = new ArrayList();
      listTmp.add(new Integer(i));
      listTmp.add(saCurrTypdId[i]);
      listTmp.add(saPk_corp[i]);
      listTblData.add(listTmp);
    }
    String sTblName = null;
    try {
      TempTableDMO dmo = new TempTableDMO();
      sTblName = dmo.getTempStringTable("t_pu_pr_011", new String[] { "pk_pu", "cstoreorgid", "cbaseid" }, new String[] { "int", "char(20)", "char(20)" }, "pk_pu", listTblData);
    }
    catch (Exception e)
    {
      SCMEnv.out(e);
      throwBusinessException("nc.bs.pu.pub.PubDMO.getRatesArray", e);
    }

    sbSql.append("select bd_currinfo.ratedigit,bd_currinfo.pk_corp ,bd_currinfo.pk_currtype from bd_currinfo , ");
    sbSql.append(sTblName);
    sbSql.append(" as tmp ");
    sbSql.append("where bd_currinfo.pk_currtype = tmp.cbaseid and bd_currinfo.pk_corp = tmp.cstoreorgid ");

    HashMap hResult = new HashMap();
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sbSql.toString());
      rs = stmt.executeQuery();
      Integer iRate = null;
      while (rs.next()) {
        iRate = (Integer)rs.getObject(1);
        if (iRate == null) {
          iRate = new Integer(2);
        }
        hResult.put(rs.getString(2) + rs.getString(3), iRate);
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return hResult;
  }
}