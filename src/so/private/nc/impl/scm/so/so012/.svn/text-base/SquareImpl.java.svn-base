package nc.impl.scm.so.so012;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.scm.so.so012.ISquare;
import nc.itf.scm.so.so012.ISquareQuery;
import nc.itf.uap.pf.IPFMetaModel;
import nc.vo.ic.ic700.WastageBillVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.pub.pfflow04.MessagedriveVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.so.credit.CreditUtil;
import nc.vo.so.so001.BillTools;
import nc.vo.so.so012.SquareHeaderVO;
import nc.vo.so.so012.SquareItemVO;
import nc.vo.so.so012.SquareTotalVO;
import nc.vo.so.so012.SquareVO;
import nc.vo.sp.pub.GeneralSqlString;

public class SquareImpl extends AbstractCompiler2
  implements ISquare, ISquareQuery
{
  public SquareVO findByPrimaryKey(String key)
    throws BusinessException
  {
    SquareVO square = null;
    try {
      SquareDMO dmo = new SquareDMO();
      square = dmo.findByPrimaryKey(key);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException("SquareBean::findByPrimaryKey(SquarePK) Exception!", e);
    }

    return square;
  }

  public String insert(SquareVO square)
    throws BusinessException
  {
    try
    {
      IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
      srv.checkDefDataType(square);

      SquareDMO dmo = new SquareDMO();
      String key = dmo.insert(square);
      return key;
    } catch (Exception e) {
      reportException(e);
    throw new BusinessException("SquareBO::insert(SquareVO) Exception!", e);
    }
  }

  public void delete(SquareVO vo)
    throws BusinessException
  {
    try
    {
      SquareDMO dmo = new SquareDMO();
      dmo.delete(vo);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException("SquareBO::delete(SquarePK) Exception!", e);
    }
  }

  public void update(SquareVO square)
    throws BusinessException
  {
    try
    {
      SquareDMO dmo = new SquareDMO();
      dmo.update(square);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException("SquareBO::update(SquareVO) Exception!", e);
    }
  }

  public Object[] batchSquare(SquareVO[] voSources, UFDate userDate)
    throws BusinessException
  {
    Object[] retObj = null;
    ArrayList alReturn = new ArrayList();
    SCMEnv.out(">>>>>>>>>>\t结算开始\t\t<<<<<<<<<<");

    Object oTs = null;

    for (int i = 0; i < voSources.length; i++)
    {
      if (voSources[i] == null) {
        throw new BusinessException("", new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000018")));
      }

      if (!(voSources[i] instanceof SquareVO)) {
        throw new BusinessException("", new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000019")));
      }

      SquareVO inVO = voSources[i];
      try
      {
        SquareInputDMO dmo = new SquareInputDMO();
        dmo.checkSquareVO(inVO);
      } catch (Exception e) {
        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException(e.getMessage(), e);
      }
      try
      {
        SCMEnv.out(">>>>>>>>>>\t循环执行单张单据结算开始\t\t<<<<<<<<<<");

        ISquare squareBo = (ISquare)NCLocator.getInstance().lookup(ISquareQuery.class.getName());

        oTs = squareBo.exeSquareBalance(inVO, userDate);

        String sCaleidRet = inVO.getParentVO().getAttributeValue("csaleid").toString();

        for (int j = 0; j < voSources.length; j++) {
          if (!voSources[j].getParentVO().getAttributeValue("csaleid").equals(sCaleidRet))
            continue;
          voSources[j].getParentVO().setAttributeValue("ts", new UFDateTime(oTs.toString()));
        }

      }
      catch (Exception e)
      {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException(e.getMessage());
      }

    }

    retObj = alReturn.toArray();

    return retObj;
  }

  public Object[] batchUnSquare(SquareVO[] voSources, UFDate userDate)
    throws BusinessException
  {
    Object[] retObj = null;
    ArrayList alReturn = new ArrayList();
    try
    {
      SquareInputDMO dmo = new SquareInputDMO();

      for (int i = 0; i < voSources.length; i++)
      {
        if (voSources[i] == null) {
          throw new BusinessException("", new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000018")));
        }

        if (!(voSources[i] instanceof SquareVO)) {
          throw new BusinessException("", new BusinessException(NCLangResOnserver.getInstance().getStrByID("400607", "UPP400607-000019")));
        }

        SquareVO inVO = voSources[i];
        try
        {
          dmo.checkSquareVO(inVO);
        } catch (Exception e) {
          if ((e instanceof BusinessException)) {
            throw ((BusinessException)e);
          }
          throw new BusinessException(e.getMessage(), e);
        }

        try
        {
          String beanname = ISquareQuery.class.getName();
          ISquare squareBo = (ISquare)NCLocator.getInstance().lookup(beanname);

          Vector vTs = (Vector)squareBo.exeUnSquareBalance(inVO, userDate);

          SquareItemVO[] itemVO = (SquareItemVO[])(SquareItemVO[])inVO.getChildrenVO();

          for (int n = 0; n < itemVO.length; n++) {
            String sCorderbid = itemVO[n].getCorder_bid();

            for (int j = 0; j < voSources.length; j++) {
              SquareItemVO[] tempVO = (SquareItemVO[])(SquareItemVO[])voSources[j].getChildrenVO();

              for (int m = 0; m < tempVO.length; m++) {
                if (!tempVO[m].getCorder_bid().equals(sCorderbid))
                  continue;
                tempVO[m].setTs(new UFDateTime(vTs.elementAt(n).toString()));
              }

            }

          }

        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);
          if ((e instanceof BusinessException)) {
            throw ((BusinessException)e);
          }
          throw new BusinessException(e.getMessage());
        }

      }

      retObj = alReturn.toArray();
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException(e.getMessage());
    }

    return retObj;
  }

  public Object exeSquareBalance(SquareVO vo, UFDate userDate)
    throws BusinessException
  {
    try
    {
      PfUtilBO bo = new PfUtilBO();

      Object oTs = bo.processAction("SoSquare", "33", userDate.toString(), new PfUtilWorkFlowVO(), vo, null);

      return oTs;
    } catch (Exception ex) {
      if ((ex instanceof BusinessException)) {
        throw ((BusinessException)ex);
      }
    
    throw new BusinessException(ex.getMessage(), ex);
    }
  }

  public Object exeUnSquareBalance(SquareVO vo, UFDate userDate)
    throws BusinessException
  {
    try
    {
      PfUtilBO bo = new PfUtilBO();
      Object oTs = bo.processAction("UnSoSquare", "33", userDate.toString(), new PfUtilWorkFlowVO(), vo, null);

      return oTs;
    } catch (Exception ex) {
      if ((ex instanceof BusinessException))
        throw ((BusinessException)ex);
    
    throw new BusinessException(ex.getMessage(), ex);
    }
  }

  public SquareTotalVO[] queryAllData(String key)
    throws BusinessException
  {
    SquareTotalVO[] VOs = null;
    try
    {
      SquareDMO dmo = new SquareDMO();
      VOs = (SquareTotalVO[])(SquareTotalVO[])dmo.queryTotalData(key);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException("SquareBean::queryTotalData(pkCorp) Exception!", e);
    }

    return VOs;
  }

  public SquareTotalVO[] queryAllDataUnBal(String key)
    throws BusinessException
  {
    SquareTotalVO[] VOs = null;
    try {
      SquareDMO dmo = new SquareDMO();
      VOs = (SquareTotalVO[])(SquareTotalVO[])dmo.queryTatalDataUnBal(key);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException("SquareBean::queryTotalData(pkCorp) Exception!", e);
    }

    return VOs;
  }

  public SquareHeaderVO[] queryAllHeadData(String key)
    throws BusinessException
  {
    SquareHeaderVO[] square = null;
    try
    {
      SquareDMO dmo = new SquareDMO();
      square = (SquareHeaderVO[])(SquareHeaderVO[])dmo.queryAllHeadData(key);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException("SquareBean::queryAllHeadData(pkCorp) Exception!", e);
    }

    return square;
  }

  public SquareItemVO[] queryBodyByOut(String strWhere)
    throws BusinessException
  {
    try
    {
      SquareDMO dmo = new SquareDMO();
      SquareItemVO[] sales = dmo.findItemsForHeaderByOut(strWhere);
      return sales;
    } catch (Exception e) {
      reportException(e);
    throw new BusinessException("SaleReceiptBO::queryBodyByOut(String) Exception!", e);
    }
  }

  public SquareItemVO[] queryBodyData(String strWhere)
    throws BusinessException
  {
    try
    {
      SquareDMO dmo = new SquareDMO();
      SquareItemVO[] sales = dmo.findItemsForHeader(strWhere);
      return sales;
    } catch (Exception e) {
      reportException(e);
    throw new BusinessException("SaleReceiptBO::queryBodyData(String) Exception!", e);
    }
  }

  public boolean setAbnormal(SquareVO square)
    throws BusinessException
  {
    try
    {
      SquareDMO dmo = new SquareDMO();
      boolean key = dmo.setAbnormal(square);
      return key;
    } catch (Exception e) {
      reportException(e);
    throw new BusinessException("SquareBO::squareByOut(SquareVO) Exception!", e);
    }
  }

  public void squareByInvoice(SquareVO square)
    throws BusinessException
  {
    try
    {
      SquareDMO dmo = new SquareDMO();
      boolean key = dmo.squareByInvoice(square);
    }
    catch (Exception e)
    {
      //boolean key;
      reportException(e);
      throw new BusinessException("SquareBO::squareByInvoice(SquareVO) Exception!", e);
    }
  }

  public void squareByOut(SquareVO square)
    throws BusinessException
  {
    try
    {
      SquareDMO dmo = new SquareDMO();
      boolean key = dmo.squareByOut(square);
    }
    catch (Exception e)
    {
     // boolean key;
      reportException(e);
      throw new BusinessException("SquareBO::squareByOut(SquareVO) Exception!", e);
    }
  }

  private void reportException(Exception e)
  {
    SCMEnv.out(e.getMessage());
  }

  protected Object getBeanHome(Class homeClass, String beanName)
    throws NamingException
  {
    Object objref = getInitialContext().lookup("java:comp/env/ejb/" + beanName);

    return PortableRemoteObject.narrow(objref, homeClass);
  }

  protected InitialContext getInitialContext()
    throws NamingException
  {
    return new InitialContext();
  }

  public void Ic4453toSquare(WastageBillVO[] WastageVO)
    throws BusinessException
  {
    if (WastageVO.length >= 1) {
      WastageBillVO[] aryRetVO = null;
      aryRetVO = (WastageBillVO[])(WastageBillVO[])SplitBillVOs.getSplitVOs("nc.vo.ic.ic700.WastageBillVO", "nc.vo.ic.ic700.WastageBillHVO", "nc.vo.ic.ic700.WastageBillBVO", WastageVO, new String[] { "vbillcode" }, new String[] { "cbiztypeid" });

      for (int i = 0; i < aryRetVO.length; i++) {
        WastageBillVO billVO = aryRetVO[i];

        if (billVO.getBodyValue(0, "cbiztypeid") == null) {
          throw new BusinessException("途损单传入到结算的业务类型为空");
        }
        MessagedriveVO[] message = null;
        IPFMetaModel bo = (IPFMetaModel)NCLocator.getInstance().lookup(IPFMetaModel.class.getName());

        message = bo.queryAllMsgdrvVOs(null, "4C", (String)billVO.getBodyValue(0, "cbiztypeid"), "SIGN");

        if ((message == null) || (message.length == 0)) {
          continue;
        }
        SquareVO sqVO = null;

        PfUtilBO pfbo = null;
        try
        {
          pfbo = new PfUtilBO();

          for (int j = 0; j < message.length; j++)
          {
            String tmpAction = null;
            if (message[j].getActiontype().toString().equals("ManualBalance"))
            {
              tmpAction = "AutoBalance";
            } else if (message[j].getActiontype().toString().equals("ManualCostBal"))
            {
              tmpAction = "AutoCostBal";
            } else if (message[j].getActiontype().toString().equals("ManualIncomeBal"))
            {
              tmpAction = "AutoIncomeBal";
            } else if ((!message[j].getActiontype().toString().equals("AutoBalance")) && (!message[j].getActiontype().toString().equals("AutoCostBal")) && (!message[j].getActiontype().toString().equals("AutoIncomeBal")) && (!message[j].getActiontype().toString().equals("EstimationIncom")))
              {
                continue;
              }


            sqVO = (SquareVO)changeData(billVO, "4453", "33");
            sqVO = chgPriceMnyForWaste(sqVO);
            if (tmpAction != null) {
              pfbo.processAction(tmpAction, "33", WastageVO[0].getHeadValue("dapprovedate") == null ? new UFDate(System.currentTimeMillis()).toString() : WastageVO[0].getHeadValue("dapprovedate").toString(), null, sqVO, null);
            }
            else
            {
              pfbo.processAction(message[j].getActiontype().toString(), "33", WastageVO[0].getHeadValue("dapprovedate") == null ? new UFDate(System.currentTimeMillis()).toString() : WastageVO[0].getHeadValue("dapprovedate").toString(), null, sqVO, null);
            }

          }

        }
        catch (Exception e)
        {
          reportException(e);
          throw new BusinessException(e.getMessage());
        }
      }
    }
  }

  private SquareVO chgPriceMnyForWaste(SquareVO wastvo_old)
    throws Exception
  {
    SquareVO wastvo = (SquareVO)wastvo_old.clone();
    SquareItemVO[] bvos = (SquareItemVO[])(SquareItemVO[])wastvo.getChildrenVO();
    if ((bvos == null) || (bvos.length == 0)) return wastvo;
    for (int i = 0; i < bvos.length; i++) {
      if (bvos[i].getAttributeValue("pk_corp") == null) {
        bvos[i].setAttributeValue("pk_corp", wastvo.getParentVO().getAttributeValue("pk_corp"));
      }
    }

    Hashtable ht = new Hashtable();

    Hashtable htass = new Hashtable();

    ArrayList al = new ArrayList();
    if ((bvos[0].getAttributeValue("cupreceipttype") != null) && (bvos[0].getAttributeValue("cupreceipttype").equals("4C"))) {
      int i = 0; for (int iLen = bvos.length; i < iLen; i++) {
        if ((bvos[i].getAttributeValue("cupbillbodyid") == null) || (bvos[i].getAttributeValue("cupbillbodyid").toString().trim().length() <= 0)) {
          continue;
        }
        if (!al.contains((String)bvos[i].getAttributeValue("cupbillbodyid"))) {
          al.add((String)bvos[i].getAttributeValue("cupbillbodyid"));
        }
      }
    }
    else if ((bvos[0].getAttributeValue("cupreceipttype") != null) && (bvos[0].getAttributeValue("cupreceipttype").equals("7I")))
    {
      ArrayList alass = new ArrayList();
      int i = 0; for (int iLen = bvos.length; i < iLen; i++) {
        if ((bvos[i].getAttributeValue("cupbillbodyid") == null) || (bvos[i].getAttributeValue("cupbillbodyid").toString().trim().length() <= 0))
          continue;
        if (!alass.contains((String)bvos[i].getAttributeValue("cupbillbodyid"))) {
          alass.add((String)bvos[i].getAttributeValue("cupbillbodyid"));
        }
      }
      htass = getICStockByAssign((String[])alass.toArray(new String[alass.size()]));
    }

    String[] ickeys = null;
    if (al.size() > 0) {
      ickeys = (String[])al.toArray(new String[al.size()]);
    }
    else {
      ickeys = (String[])htass.values().toArray(new String[htass.size()]);
    }
    if (ickeys == null) return wastvo_old;
    GeneralBillItemVO[] gbvos = getICItemsByIds(ickeys);
    if ((gbvos == null) || (gbvos.length == 0)) return wastvo_old;
    Hashtable htforic = new Hashtable();

    for (int i = 0; i < gbvos.length; i++) {
      htforic.put(gbvos[i].getCgeneralbid(), gbvos[i]);
    }
    GeneralBillItemVO bvo = null;
    String skeytp = null;

    if ((bvos[0].getAttributeValue("cupreceipttype") != null) && (bvos[0].getAttributeValue("cupreceipttype").equals("4C"))) {
      int i = 0; for (int iLen = bvos.length; i < iLen; i++) {
        if ((bvos[i].getAttributeValue("cupbillbodyid") == null) || (bvos[i].getAttributeValue("cupbillbodyid").toString().trim().length() <= 0))
          continue;
        if (!htforic.containsKey(bvos[i].getAttributeValue("cupbillbodyid")))
          continue;
        bvo = (GeneralBillItemVO)htforic.get(bvos[i].getAttributeValue("cupbillbodyid"));
        dochgPrice(bvos[i], bvo);
      }

    }
    else if ((bvos[0].getAttributeValue("cupreceipttype") != null) && (bvos[0].getAttributeValue("cupreceipttype").equals("7I")))
    {
      ArrayList alass = new ArrayList();
      int i = 0; for (int iLen = bvos.length; i < iLen; i++) {
        if ((bvos[i].getAttributeValue("cupbillbodyid") == null) || (bvos[i].getAttributeValue("cupbillbodyid").toString().trim().length() <= 0))
          continue;
        skeytp = (String)htass.get(bvos[i].getAttributeValue("cupbillbodyid"));
        if ((skeytp == null) || (!htforic.containsKey(skeytp)))
          continue;
        bvo = (GeneralBillItemVO)htforic.get(skeytp);
        dochgPrice(bvos[i], bvo);
      }

    }

    return wastvo;
  }
  private void dochgPrice(SquareItemVO wastvo, GeneralBillItemVO icvo) throws Exception {
    BillTools.initItemKeys("33");
    if ((icvo.getAttributeValue("naccumwastnum") != null) && (icvo.getNoutnum() != null) && (icvo.getNoutnum().compareTo(icvo.getAttributeValue("naccumwastnum")) <= 0))
    {
      UFDouble noriginalcursummny = CreditUtil.convertObjToUFDouble(icvo.getAttributeValue("nquotemny"));
      noriginalcursummny = noriginalcursummny.multiply(-1.0D);
      wastvo.setAttributeValue("noriginalcursummny", noriginalcursummny);
      BillTools.calcEditFunFor33(new UFDate(System.currentTimeMillis()).toString(), null, null, "noriginalcursummny", wastvo, "33");
    }
    else
    {
      wastvo.setAttributeValue("norgqttaxnetprc", icvo.getAttributeValue("nquoteprice"));
      BillTools.calcEditFunFor33(new UFDate(System.currentTimeMillis()).toString(), null, null, "norgqttaxnetprc", wastvo, "33");
    }
  }

  private GeneralBillItemVO[] getICItemsByIds(String[] ids)
    throws Exception
  {
    SmartDMO sdmo = new SmartDMO();
    GeneralBillItemVO[] bvos = (GeneralBillItemVO[])(GeneralBillItemVO[])sdmo.selectBy(GeneralBillItemVO.class, new String[] { "cquotecurrency", "nquoteprice", "nquotemny", "naccumwastnum", "noutnum", "noutassistnum", "cgeneralbid" }, " 1=1 " + GeneralSqlString.formInSQL("cgeneralbid", ids));

    return bvos;
  }

  private Hashtable<String, String> getICStockByAssign(String[] assigns)
    throws Exception
  {
    Hashtable ht = new Hashtable();
    String sql = " select pk_sign_b ,pk_sourcebill_b from dm_sign_b where pk_sourcebilltype='4C' ";
    sql = sql + GeneralSqlString.formInSQL("pk_sign_b", assigns);
    SmartDMO sdmo = new SmartDMO();
    Object[] o = sdmo.selectBy2(sql);
    if ((o == null) || (o.length == 0)) return ht;
    Object[] o1 = null;
    for (int i = 0; i < o.length; i++) {
      o1 = (Object[])(Object[])o[i];
      if ((o1 != null) && (o1.length > 0) && (o1[0] != null) && (o1[1] != null)) {
        ht.put((String)o1[0], (String)o1[1]);
      }
    }
    return ht;
  }

  public void Ic4453toUnSquare(WastageBillVO[] WastageVO)
    throws BusinessException
  {
    if (WastageVO.length >= 1)
      for (int i = 0; i < WastageVO.length; i++)
      {
        WastageBillVO billVO = WastageVO[i];
        try
        {
          MessagedriveVO[] message = null;
          if (billVO.getBodyValue(0, "cbiztypeid") == null)
            throw new BusinessException("途损单传入到结算的业务类型为空");
          IPFMetaModel bo = (IPFMetaModel)NCLocator.getInstance().lookup(IPFMetaModel.class.getName());

          message = bo.queryAllMsgdrvVOs(null, "4C", billVO.getBodyValue(0, "cbiztypeid").toString(), "SIGN");

          if ((message != null) && (message.length != 0))
          {
            SquareTotalVO[] VOs = null;
            SquareVO[] squareVOs = null;
            SquareVO tmpVO = null;

            SquareDMO dmo = new SquareDMO();
            VOs = (SquareTotalVO[])(SquareTotalVO[])dmo.queryTatalDataUnBal(" sd.csaleid= '" + billVO.getHeadValue("cwastagebillid").toString() + "' and sd.dr=0 ");
            squareVOs = changeTotalToSquareVO(VOs);
            if (squareVOs != null) {
              tmpVO = squareVOs[0];

              PfUtilBO pfbo = null;

              pfbo = new PfUtilBO();
              pfbo.processAction("UnSoSquare", "33", WastageVO[0].getLogdate().toString(), null, tmpVO, null);

              SquareInputDMO temInputSquare = new SquareInputDMO();
              temInputSquare.setAfterOutAbandonCheck(billVO.getHeadValue("cwastagebillid").toString());
            }
          }
        } catch (Exception e) {
          reportException(e);
          throw new BusinessException("SquareBO::squareByOut(SquareVO) Exception!", e);
        }
      }
  }

  public SquareVO[] changeTotalToSquareVO(SquareTotalVO[] totalVOs)
  {
    SquareVO[] squareVOs = null;

    Vector vSquareVec = null;
    if ((totalVOs != null) && (totalVOs.length > 0))
    {
      for (int i = 0; i < totalVOs.length; i++)
      {
        String sCsaleid_h = totalVOs[i].getAttributeValue("csaleid_h").toString();

        String sCustid = totalVOs[i].getAttributeValue("ccustomerid").toString();

        Object oArHid = totalVOs[i].getAttributeValue("carhid");

        Object oIaHid = totalVOs[i].getAttributeValue("ciahid");

        boolean bInComeFlag = totalVOs[i].getAttributeValue("bincomeflag") == null ? false : ((UFBoolean)totalVOs[i].getAttributeValue("bincomeflag")).booleanValue();

        boolean bCostFlag = totalVOs[i].getAttributeValue("bcostflag") == null ? false : ((UFBoolean)totalVOs[i].getAttributeValue("bcostflag")).booleanValue();

        if (vSquareVec == null)
        {
          vSquareVec = new Vector();

          SquareVO sqVO = new SquareVO();
          SquareHeaderVO headerVO = buildHeader(totalVOs[i]);
          sqVO.setParentVO(headerVO);

          if ((oArHid == null) && (bInComeFlag))
          {
            SquareItemVO itemVO = buildItem(totalVOs[i]);
            SquareItemVO[] itemVOArray = new SquareItemVO[1];
            itemVOArray[0] = itemVO;
            sqVO.setChildrenVO(itemVOArray);
          }
          vSquareVec.add(sqVO);
        }
        else if ((oArHid == null) && (bInComeFlag))
        {
          SquareVO sqVO = new SquareVO();
          SquareHeaderVO headerVO = buildHeader(totalVOs[i]);
          sqVO.setParentVO(headerVO);

          SquareItemVO itemVO = buildItem(totalVOs[i]);
          SquareItemVO[] itemVOArray = new SquareItemVO[1];
          itemVOArray[0] = itemVO;
          sqVO.setChildrenVO(itemVOArray);

          vSquareVec.add(sqVO);
        }
        else
        {
          int iLength = vSquareVec.size();
          for (int j = 0; j < iLength; j++)
          {
            SquareHeaderVO tempHeaderVO = (SquareHeaderVO)((SquareVO)vSquareVec.elementAt(j)).getParentVO();

            String sTempCsaleid = tempHeaderVO.getCsaleid();
            String sTempCustomerid = tempHeaderVO.getCcustomerid();
            String sTempCarhid = tempHeaderVO.getCarhid();

            if ((sTempCsaleid.equals(sCsaleid_h)) && (sTempCustomerid.equals(sCustid)) && (oArHid != null) && (oArHid.equals(sTempCarhid)))
            {
              break;
            }

            if (j != vSquareVec.size() - 1)
              continue;
            SquareVO sqVO = new SquareVO();
            SquareHeaderVO headerVO = buildHeader(totalVOs[i]);
            sqVO.setParentVO(headerVO);

            vSquareVec.addElement(sqVO);
          }

        }

      }

      if ((vSquareVec != null) && (vSquareVec.size() > 0))
      {
        squareVOs = new SquareVO[vSquareVec.size()];

        for (int i = 0; i < vSquareVec.size(); i++)
        {
          Vector vItems = new Vector();

          SquareVO sqVO = (SquareVO)vSquareVec.elementAt(i);
          SquareHeaderVO headerVO = (SquareHeaderVO)sqVO.getParentVO();

          SquareItemVO[] itemVOs = null;
          String sCsaleid = headerVO.getAttributeValue("csaleid").toString();
          String sCustid = headerVO.getAttributeValue("ccustomerid").toString();

          Object oArHid = headerVO.getAttributeValue("carhid");

          boolean bInComeFlag = headerVO.getAttributeValue("bincomeflag") == null ? false : ((UFBoolean)headerVO.getAttributeValue("bincomeflag")).booleanValue();

          boolean bCostFlag = headerVO.getAttributeValue("bcostflag") == null ? false : ((UFBoolean)headerVO.getAttributeValue("bcostflag")).booleanValue();

          if ((oArHid != null) || (!bInComeFlag))
          {
            for (int j = 0; j < totalVOs.length; j++)
            {
              if ((totalVOs[j].getAttributeValue("csaleid_h").toString().equals(sCsaleid)) && (totalVOs[j].getAttributeValue("ccustomerid").toString().equals(sCustid)) && (oArHid != null) && (oArHid.equals(totalVOs[j].getAttributeValue("carhid"))))
              {
                SquareItemVO itemVO = buildItem(totalVOs[j]);
                vItems.addElement(itemVO);
              }
              else
              {
                if ((!totalVOs[j].getAttributeValue("csaleid_h").toString().equals(sCsaleid)) || (!totalVOs[j].getAttributeValue("ccustomerid").toString().equals(sCustid)))
                {
                  continue;
                }
                SquareItemVO itemVO = buildItem(totalVOs[j]);
                vItems.addElement(itemVO);
              }
            }

          }

          if ((vItems.size() != 0) && (sqVO.getChildrenVO() == null))
          {
            itemVOs = new SquareItemVO[vItems.size()];
            vItems.copyInto(itemVOs);
            sqVO.setChildrenVO(itemVOs);
          }

          squareVOs[i] = new SquareVO();
          squareVOs[i] = sqVO;
        }

      }

    }

    return squareVOs;
  }

  private SquareItemVO buildItem(SquareTotalVO totalVO)
  {
    SquareItemVO itemVO = new SquareItemVO();
    if (totalVO != null)
    {
      String[] sName = itemVO.getAttributeNames();
      for (int i = 0; i < sName.length; i++)
      {
        if (sName[i].equals("csaleid")) {
          itemVO.setAttributeValue("csaleid", totalVO.getAttributeValue("csaleid_b"));
        }
        else if (sName[i].equals("creceipttype")) {
          itemVO.setAttributeValue("creceipttype", totalVO.getAttributeValue("creceipttype_b"));
        }
        else if (sName[i].equals("vdef1")) {
          itemVO.setAttributeValue("vdef1", totalVO.getAttributeValue("vdef1_b"));
        }
        else if (sName[i].equals("vdef2")) {
          itemVO.setAttributeValue("vdef2", totalVO.getAttributeValue("vdef2_b"));
        }
        else if (sName[i].equals("vdef3")) {
          itemVO.setAttributeValue("vdef3", totalVO.getAttributeValue("vdef3_b"));
        }
        else if (sName[i].equals("vdef4")) {
          itemVO.setAttributeValue("vdef4", totalVO.getAttributeValue("vdef4_b"));
        }
        else if (sName[i].equals("vdef5")) {
          itemVO.setAttributeValue("vdef5", totalVO.getAttributeValue("vdef5_b"));
        }
        else if (sName[i].equals("vdef6")) {
          itemVO.setAttributeValue("vdef6", totalVO.getAttributeValue("vdef6_b"));
        }
        else if (sName[i].equals("vdef7")) {
          itemVO.setAttributeValue("vdef7", totalVO.getAttributeValue("vdef7_b"));
        }
        else if (sName[i].equals("vdef8")) {
          itemVO.setAttributeValue("vdef8", totalVO.getAttributeValue("vdef8_b"));
        }
        else if (sName[i].equals("vdef9")) {
          itemVO.setAttributeValue("vdef9", totalVO.getAttributeValue("vdef9_b"));
        }
        else if (sName[i].equals("vdef10")) {
          itemVO.setAttributeValue("vdef10", totalVO.getAttributeValue("vdef10_b"));
        }
        else if (sName[i].equals("vdef11")) {
          itemVO.setAttributeValue("vdef11", totalVO.getAttributeValue("vdef11_b"));
        }
        else if (sName[i].equals("vdef12")) {
          itemVO.setAttributeValue("vdef12", totalVO.getAttributeValue("vdef12_b"));
        }
        else if (sName[i].equals("vdef13")) {
          itemVO.setAttributeValue("vdef13", totalVO.getAttributeValue("vdef13_b"));
        }
        else if (sName[i].equals("vdef14")) {
          itemVO.setAttributeValue("vdef14", totalVO.getAttributeValue("vdef14_b"));
        }
        else if (sName[i].equals("vdef15")) {
          itemVO.setAttributeValue("vdef15", totalVO.getAttributeValue("vdef15_b"));
        }
        else if (sName[i].equals("vdef16")) {
          itemVO.setAttributeValue("vdef16", totalVO.getAttributeValue("vdef16_b"));
        }
        else if (sName[i].equals("vdef17")) {
          itemVO.setAttributeValue("vdef17", totalVO.getAttributeValue("vdef17_b"));
        }
        else if (sName[i].equals("vdef18")) {
          itemVO.setAttributeValue("vdef18", totalVO.getAttributeValue("vdef18_b"));
        }
        else if (sName[i].equals("vdef19")) {
          itemVO.setAttributeValue("vdef19", totalVO.getAttributeValue("vdef19_b"));
        }
        else if (sName[i].equals("vdef20")) {
          itemVO.setAttributeValue("vdef20", totalVO.getAttributeValue("vdef20_b"));
        }
        else if (sName[i].equals("pk_defdoc1")) {
          itemVO.setAttributeValue("pk_defdoc1", totalVO.getAttributeValue("pk_defdoc1_b"));
        }
        else if (sName[i].equals("pk_defdoc2")) {
          itemVO.setAttributeValue("pk_defdoc2", totalVO.getAttributeValue("pk_defdoc2_b"));
        }
        else if (sName[i].equals("pk_defdoc3")) {
          itemVO.setAttributeValue("pk_defdoc3", totalVO.getAttributeValue("pk_defdoc3_b"));
        }
        else if (sName[i].equals("pk_defdoc4")) {
          itemVO.setAttributeValue("pk_defdoc4", totalVO.getAttributeValue("pk_defdoc4_b"));
        }
        else if (sName[i].equals("pk_defdoc5")) {
          itemVO.setAttributeValue("pk_defdoc5", totalVO.getAttributeValue("pk_defdoc5_b"));
        }
        else if (sName[i].equals("pk_defdoc6")) {
          itemVO.setAttributeValue("pk_defdoc6", totalVO.getAttributeValue("pk_defdoc6_b"));
        }
        else if (sName[i].equals("pk_defdoc7")) {
          itemVO.setAttributeValue("pk_defdoc7", totalVO.getAttributeValue("pk_defdoc7_b"));
        }
        else if (sName[i].equals("pk_defdoc8")) {
          itemVO.setAttributeValue("pk_defdoc8", totalVO.getAttributeValue("pk_defdoc8_b"));
        }
        else if (sName[i].equals("pk_defdoc9")) {
          itemVO.setAttributeValue("pk_defdoc9", totalVO.getAttributeValue("pk_defdoc9_b"));
        }
        else if (sName[i].equals("pk_defdoc10")) {
          itemVO.setAttributeValue("pk_defdoc10", totalVO.getAttributeValue("pk_defdoc10_b"));
        }
        else if (sName[i].equals("pk_defdoc11")) {
          itemVO.setAttributeValue("pk_defdoc11", totalVO.getAttributeValue("pk_defdoc11_b"));
        }
        else if (sName[i].equals("pk_defdoc12")) {
          itemVO.setAttributeValue("pk_defdoc12", totalVO.getAttributeValue("pk_defdoc12_b"));
        }
        else if (sName[i].equals("pk_defdoc13")) {
          itemVO.setAttributeValue("pk_defdoc13", totalVO.getAttributeValue("pk_defdoc13_b"));
        }
        else if (sName[i].equals("pk_defdoc14")) {
          itemVO.setAttributeValue("pk_defdoc14", totalVO.getAttributeValue("pk_defdoc14_b"));
        }
        else if (sName[i].equals("pk_defdoc15")) {
          itemVO.setAttributeValue("pk_defdoc15", totalVO.getAttributeValue("pk_defdoc15_b"));
        }
        else if (sName[i].equals("pk_defdoc16")) {
          itemVO.setAttributeValue("pk_defdoc16", totalVO.getAttributeValue("pk_defdoc16_b"));
        }
        else if (sName[i].equals("pk_defdoc17")) {
          itemVO.setAttributeValue("pk_defdoc17", totalVO.getAttributeValue("pk_defdoc17_b"));
        }
        else if (sName[i].equals("pk_defdoc18")) {
          itemVO.setAttributeValue("pk_defdoc18", totalVO.getAttributeValue("pk_defdoc18_b"));
        }
        else if (sName[i].equals("pk_defdoc19")) {
          itemVO.setAttributeValue("pk_defdoc19", totalVO.getAttributeValue("pk_defdoc19_b"));
        }
        else if (sName[i].equals("pk_defdoc20")) {
          itemVO.setAttributeValue("pk_defdoc20", totalVO.getAttributeValue("pk_defdoc20_b"));
        }
        else if (sName[i].equals("ts")) {
          itemVO.setAttributeValue("ts", totalVO.getAttributeValue("ts_b"));
        }
        else if (sName[i].equals("dr")) {
          itemVO.setAttributeValue("dr", totalVO.getAttributeValue("dr_b"));
        }
        else {
          itemVO.setAttributeValue(sName[i], totalVO.getAttributeValue(sName[i]));
        }
      }
    }

    return itemVO;
  }

  private SquareHeaderVO buildHeader(SquareTotalVO totalVO)
  {
    SquareHeaderVO headerVO = new SquareHeaderVO();
    if (totalVO != null)
    {
      String[] sName = headerVO.getAttributeNames();
      for (int i = 0; i < sName.length; i++)
      {
        if (sName[i].equals("csaleid")) {
          headerVO.setAttributeValue("csaleid", totalVO.getAttributeValue("csaleid_h"));
        }
        else if (sName[i].equalsIgnoreCase("bEstimation")) {
          headerVO.setAttributeValue("bEstimation", totalVO.getAttributeValue("bEstimation"));
        }
        else if (sName[i].equals("creceipttype")) {
          headerVO.setAttributeValue("creceipttype", totalVO.getAttributeValue("creceipttype_h"));
        }
        else if (sName[i].equals("vdef1")) {
          headerVO.setAttributeValue("vdef1", totalVO.getAttributeValue("vdef1_h"));
        }
        else if (sName[i].equals("vdef2")) {
          headerVO.setAttributeValue("vdef2", totalVO.getAttributeValue("vdef2_h"));
        }
        else if (sName[i].equals("vdef3")) {
          headerVO.setAttributeValue("vdef3", totalVO.getAttributeValue("vdef3_h"));
        }
        else if (sName[i].equals("vdef4")) {
          headerVO.setAttributeValue("vdef4", totalVO.getAttributeValue("vdef4_h"));
        }
        else if (sName[i].equals("vdef5")) {
          headerVO.setAttributeValue("vdef5", totalVO.getAttributeValue("vdef5_h"));
        }
        else if (sName[i].equals("vdef6")) {
          headerVO.setAttributeValue("vdef6", totalVO.getAttributeValue("vdef6_h"));
        }
        else if (sName[i].equals("vdef7")) {
          headerVO.setAttributeValue("vdef7", totalVO.getAttributeValue("vdef7_h"));
        }
        else if (sName[i].equals("vdef8")) {
          headerVO.setAttributeValue("vdef8", totalVO.getAttributeValue("vdef8_h"));
        }
        else if (sName[i].equals("vdef9")) {
          headerVO.setAttributeValue("vdef9", totalVO.getAttributeValue("vdef9_h"));
        }
        else if (sName[i].equals("vdef10")) {
          headerVO.setAttributeValue("vdef10", totalVO.getAttributeValue("vdef10_h"));
        }
        else if (sName[i].equals("vdef11")) {
          headerVO.setAttributeValue("vdef11", totalVO.getAttributeValue("vdef11_h"));
        }
        else if (sName[i].equals("vdef12")) {
          headerVO.setAttributeValue("vdef12", totalVO.getAttributeValue("vdef12_h"));
        }
        else if (sName[i].equals("vdef13")) {
          headerVO.setAttributeValue("vdef13", totalVO.getAttributeValue("vdef13_h"));
        }
        else if (sName[i].equals("vdef14")) {
          headerVO.setAttributeValue("vdef14", totalVO.getAttributeValue("vdef14_h"));
        }
        else if (sName[i].equals("vdef15")) {
          headerVO.setAttributeValue("vdef15", totalVO.getAttributeValue("vdef15_h"));
        }
        else if (sName[i].equals("vdef16")) {
          headerVO.setAttributeValue("vdef16", totalVO.getAttributeValue("vdef16_h"));
        }
        else if (sName[i].equals("vdef17")) {
          headerVO.setAttributeValue("vdef17", totalVO.getAttributeValue("vdef17_h"));
        }
        else if (sName[i].equals("vdef18")) {
          headerVO.setAttributeValue("vdef18", totalVO.getAttributeValue("vdef18_h"));
        }
        else if (sName[i].equals("vdef19")) {
          headerVO.setAttributeValue("vdef19", totalVO.getAttributeValue("vdef19_h"));
        }
        else if (sName[i].equals("vdef20")) {
          headerVO.setAttributeValue("vdef20", totalVO.getAttributeValue("vdef20_h"));
        }
        else if (sName[i].equals("pk_defdoc1")) {
          headerVO.setAttributeValue("pk_defdoc1", totalVO.getAttributeValue("pk_defdoc1_h"));
        }
        else if (sName[i].equals("pk_defdoc2")) {
          headerVO.setAttributeValue("pk_defdoc2", totalVO.getAttributeValue("pk_defdoc2_h"));
        }
        else if (sName[i].equals("pk_defdoc3")) {
          headerVO.setAttributeValue("pk_defdoc3", totalVO.getAttributeValue("pk_defdoc3_h"));
        }
        else if (sName[i].equals("pk_defdoc4")) {
          headerVO.setAttributeValue("pk_defdoc4", totalVO.getAttributeValue("pk_defdoc4_h"));
        }
        else if (sName[i].equals("pk_defdoc5")) {
          headerVO.setAttributeValue("pk_defdoc5", totalVO.getAttributeValue("pk_defdoc5_h"));
        }
        else if (sName[i].equals("pk_defdoc6")) {
          headerVO.setAttributeValue("pk_defdoc6", totalVO.getAttributeValue("pk_defdoc6_h"));
        }
        else if (sName[i].equals("pk_defdoc7")) {
          headerVO.setAttributeValue("pk_defdoc7", totalVO.getAttributeValue("pk_defdoc7_h"));
        }
        else if (sName[i].equals("pk_defdoc8")) {
          headerVO.setAttributeValue("pk_defdoc8", totalVO.getAttributeValue("pk_defdoc8_h"));
        }
        else if (sName[i].equals("pk_defdoc9")) {
          headerVO.setAttributeValue("pk_defdoc9", totalVO.getAttributeValue("pk_defdoc9_h"));
        }
        else if (sName[i].equals("pk_defdoc10")) {
          headerVO.setAttributeValue("pk_defdoc10", totalVO.getAttributeValue("pk_defdoc10_h"));
        }
        else if (sName[i].equals("pk_defdoc11")) {
          headerVO.setAttributeValue("pk_defdoc11", totalVO.getAttributeValue("pk_defdoc11_h"));
        }
        else if (sName[i].equals("pk_defdoc12")) {
          headerVO.setAttributeValue("pk_defdoc12", totalVO.getAttributeValue("pk_defdoc12_h"));
        }
        else if (sName[i].equals("pk_defdoc13")) {
          headerVO.setAttributeValue("pk_defdoc13", totalVO.getAttributeValue("pk_defdoc13_h"));
        }
        else if (sName[i].equals("pk_defdoc14")) {
          headerVO.setAttributeValue("pk_defdoc14", totalVO.getAttributeValue("pk_defdoc14_h"));
        }
        else if (sName[i].equals("pk_defdoc15")) {
          headerVO.setAttributeValue("pk_defdoc15", totalVO.getAttributeValue("pk_defdoc15_h"));
        }
        else if (sName[i].equals("pk_defdoc16")) {
          headerVO.setAttributeValue("pk_defdoc16", totalVO.getAttributeValue("pk_defdoc16_h"));
        }
        else if (sName[i].equals("pk_defdoc17")) {
          headerVO.setAttributeValue("pk_defdoc17", totalVO.getAttributeValue("pk_defdoc17_h"));
        }
        else if (sName[i].equals("pk_defdoc18")) {
          headerVO.setAttributeValue("pk_defdoc18", totalVO.getAttributeValue("pk_defdoc18_h"));
        }
        else if (sName[i].equals("pk_defdoc19")) {
          headerVO.setAttributeValue("pk_defdoc19", totalVO.getAttributeValue("pk_defdoc19_h"));
        }
        else if (sName[i].equals("pk_defdoc20")) {
          headerVO.setAttributeValue("pk_defdoc20", totalVO.getAttributeValue("pk_defdoc20_h"));
        }
        else if (sName[i].equals("ts")) {
          headerVO.setAttributeValue("ts", totalVO.getAttributeValue("ts_h"));
        }
        else if (sName[i].equals("dr")) {
          headerVO.setAttributeValue("dr", totalVO.getAttributeValue("dr_h"));
        }
        else {
          headerVO.setAttributeValue(sName[i], totalVO.getAttributeValue(sName[i]));
        }
      }
    }

    return headerVO;
  }
}