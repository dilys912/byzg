package nc.impl.scm.so.so001;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.framework.common.NCLocator;
import nc.bs.scm.recordtime.RecordTimeImpl;
import nc.impl.scm.so.pub.CheckValueValidityImpl;
import nc.impl.scm.so.pub.CreditControlDMO;
import nc.impl.scm.so.pub.DoInterfaceForSO;
import nc.impl.scm.so.pub.OtherInterfaceDMO;
import nc.impl.scm.so.so008.OosinfoDMO;
import nc.impl.scm.so.so033.SaleReportDMO;
import nc.impl.so.sointerface.SOATP;
import nc.itf.ct.service.ICtToSo_QueryCt;
import nc.itf.ic.service.IICPub_MiscDMO;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.scm.so.pub.ICreditControl;
import nc.itf.scm.so.so001.ISaleOrder;
import nc.itf.scm.so.so001.ISaleOrderQuery;
import nc.itf.so.service.ISOToIC_SaleOrder;
import nc.itf.so.service.ISOToSoReturn;
import nc.itf.so.so120.ICreditMny;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ct.TypeVO;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.recordtime.RecordType;
import nc.vo.so.credit.AccountMnyVO;
import nc.vo.so.pub.CustCreditVO;
import nc.vo.so.pub.ProdNotInstallException;
import nc.vo.so.so001.BillTools;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

public class SaleOrderImpl
  implements ISaleOrderQuery, ISaleOrder, ISOToIC_SaleOrder, ISOToSoReturn
{
  public void orderDelete(String key)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      dmo.delete(key);
    } catch (Exception e) {
      throw new BusinessException("SaleOrderBO::delete(SalePK) Exception!");
    }
  }

  private void fillSaleOrderVO(SaleOrderVO vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.getChildrenVO() == null) || (vos.getChildrenVO().length <= 0))
    {
      return;
    }
    Hashtable rehs = null;
    String[] ids = null;
    try {
      SaleOrderDMO dmo = new SaleOrderDMO();
      SaleorderBVO[] bvos = (SaleorderBVO[])(SaleorderBVO[])vos.getChildrenVO();
      ids = new String[bvos.length];
      for (int i = 0; i < bvos.length; i++) {
        ids[i] = bvos[i].getCinventoryid();
      }
      rehs = dmo.getAnyValue("bd_invmandoc", "pk_invbasdoc", "pk_invmandoc", ids);

      if (rehs != null) {
        for (int i = 0; i < bvos.length; i++)
          if (bvos[i].getCinventoryid() != null) {
            Object value = rehs.get(bvos[i].getCinventoryid());
            if (value != null)
              bvos[i].setCinvbasdocid(value.toString());
          }
      }
    }
    catch (Exception e)
    {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public Hashtable getAllContractType(String[] ids)
    throws BusinessException
  {
    if ((ids == null) || (ids.length <= 0))
      return null;
    StringBuffer wheresql = null;
    Hashtable reobj = new Hashtable();
    try {
      SaleOrderDMO dmo = new SaleOrderDMO();
      int i = 0; int count = ids.length % 200 == 0 ? ids.length / 200 : ids.length / 200 + 1;
      for (; i < count; i++) {
        wheresql = new StringBuffer();
        int j = 0; int count1 = ids.length - i * 200 > 200 ? 200 : ids.length - i * 200;
        for (; j < count1; j++) {
          if (j > 0)
            wheresql.append(" , ");
          wheresql.append("'" + ids[(i * 200 + j)] + "'");
        }
        TypeVO[] vos = dmo.getAllContractType(" ct_manage.pk_ct_manage in ( " + wheresql.toString() + " ) ");

        if (vos != null)
          for (int k = 0; k < vos.length; k++)
            reobj.put(vos[k].getPrimaryKey(), vos[k]);
      }
    }
    catch (Exception e)
    {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return reobj;
  }

  public String[][] getBomChildInfo(String pk_corp, String invID)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      String[][] results = dmo.getBomChildInfo(pk_corp, invID);
      return results;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public String[][] getBomInfo(String pk_corp, String invID, String curdate)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      String[][] results = (String[][])null;
      try {
        results = dmo.getBomInfo(pk_corp, invID, curdate);
      }
      catch (Exception e) {
        reportException(e);
      }
      return results;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public TypeVO getContractType(String idContract)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      TypeVO results = dmo.getContractType(idContract);
      return results;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public UFDouble getCreditMoney(String sCusMID, String sPKCorp)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.getCreditMoney(sCusMID, sPKCorp);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public String[][] getCtCode(String custid, String invid, String ctdate)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      String[][] results = dmo.getCtCode(custid, invid, ctdate);
      return results;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public String[] getCustAddress(String sCustManID, String sPKCorp)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.getCustAddress(sCustManID, sPKCorp);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public CustCreditVO getCustCreditData(CustCreditVO voWhere, String strCorpID)
    throws BusinessException
  {
    CustCreditVO custData = new CustCreditVO();
    try {
      String strCustomerID = voWhere.getPk_cumandoc();
      String strBusiTypeID = voWhere.getCbiztype();
      String[] strCustomerIDs = { strCustomerID };
      Hashtable hret = null;

      ICreditControl dmo = new CreditControlDMO();
      SaleOrderDMO saledmo = new SaleOrderDMO();

      ICreditMny creditmny = (ICreditMny)NCLocator.getInstance().lookup(ICreditMny.class.getName());

      AccountMnyVO accountvo = new AccountMnyVO();
      accountvo.setAttributeValue("pk_cumandoc", strCustomerID);

      UFDouble[] retmnys = creditmny.getAccountMny(accountvo);

      if ((retmnys != null) && (retmnys.length >= 3))
      {
        custData.setOrderar(retmnys[0]);

        custData.setBizar(retmnys[1]);

        custData.setAr(retmnys[2]);
      }
      else {
        custData.setOrderar(null);

        custData.setBizar(null);

        custData.setAr(null);
      }

      hret = dmo.getARsPrepayNotCheck(3, strCustomerIDs);
      if (hret != null)
        custData.setCustsub((UFDouble)hret.get(strCustomerID));
      else {
        custData.setCustsub(null);
      }

      custData.setCreditmny(saledmo.getCreditMny(strCustomerID));

      custData.setCreditMoney(saledmo.getCreditMoney(strCustomerID, strCorpID));

      DMDataVO[] mnyvos = creditmny.getCreditMnyLeftNew(strCorpID, strCustomerID, strBusiTypeID);

      custData.setCreditvos(mnyvos);
    }
    catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return custData;
  }

  public UFDouble[] getCustManInfoAr(String cusmanid, String strCorpID)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.getCustManInfoAr(cusmanid, strCorpID);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public UFDate getDateByAheadPeriod(String pksendtype, String pksendstoreorg, String pktoarea, UFDate senddate, UFDate arrivedate)
    throws BusinessException
  {
    try
    {
      UFDate results = (UFDate)new SOToolsImpl().runComMethod("DM", "nc.bs.dm.pub.DmDMO", "getOtherDateByAheadPeriod", new Class[] { String.class, String.class, String.class, UFDate.class, UFDate.class }, new Object[] { pksendtype, pksendstoreorg, pktoarea, senddate, arrivedate });

      return results;
    }
    catch (ProdNotInstallException e) {
      SCMEnv.out(e.getMessage());
      return null;
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public UFDouble getFuncExceptionValue(String billType, String businessType, String actionName, String corpId, String operator, String[] funnode, SaleOrderVO vo)
    throws BusinessException
  {
    UFDouble reobj = null;
    try {
      BusiFuncCalcDMO dmo = new BusiFuncCalcDMO();
      reobj = dmo.getFuncExceptionValue(billType, businessType, actionName, corpId, operator, funnode, vo);
    }
    catch (Exception e)
    {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return reobj;
  }

  public double getInvInNumByBatch(String pk_corp, String invID, String vbatchcode)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      double num = dmo.getInvInNumByBatch(pk_corp, invID, vbatchcode);
      return num;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public ArrayList insert(SaleOrderVO sale)
    throws BusinessException
  {
    try
    {
      CheckValueValidityImpl dmocode = new CheckValueValidityImpl();
      String vreceiptcode = dmocode.getSysBillNO(sale);
      sale.getParentVO().setAttributeValue("vreceiptcode", vreceiptcode);

      IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());

      srv.checkDefDataType(sale);

      SaleOrderDMO dmo = new SaleOrderDMO();
      ArrayList key = dmo.insert(sale);
      return key;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public boolean isBomApproved(String orderID)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.isBomApproved(orderID);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public boolean isCtExist(String custid, String invid)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.isCtExist(custid, invid);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public boolean isInvBatched(String invid)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.isInvBatched(invid);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public boolean isInvBom(String invid)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.isInvBom(invid);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public boolean isInvExist(String pk_corp, String invid)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.isInvExist(pk_corp, invid);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public boolean isInvLocked(String sale_bid)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.isInvLocked(sale_bid);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public SaleorderBVO[] queryBodyAllData(String strWhere)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      SaleorderBVO[] saleorderBs = (SaleorderBVO[])(SaleorderBVO[])dmo.queryAllBodyData(strWhere);

      return saleorderBs;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public SaleorderBVO[] queryBodyAllDataByIDs(String[] bksys)
    throws BusinessException
  {
    if ((bksys == null) || (bksys.length <= 0)) {
      return null;
    }
    ArrayList reobj = new ArrayList();
    String[] oneids = null;
    try {
      SaleOrderDMO dmo = new SaleOrderDMO();
      int i = 0; int count = bksys.length % 200 == 0 ? bksys.length / 200 : bksys.length / 200 + 1;
      for (; i < count; i++)
      {
        oneids = new String[bksys.length - i * 200 > 200 ? 200 : bksys.length - i * 200];

        for (int j = 0; j < oneids.length; j++) {
          oneids[j] = bksys[(i * 200 + j)];
        }
        SaleorderBVO[] saleorderBs = (SaleorderBVO[])(SaleorderBVO[])dmo.queryAllBodyDataByIDs(oneids);

        if ((saleorderBs != null) && (saleorderBs.length > 0))
          for (int k = 0; k < saleorderBs.length; k++)
            reobj.add(saleorderBs[k]);
      }
    }
    catch (Exception e)
    {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }

    if (reobj.size() > 0)
      return (SaleorderBVO[])(SaleorderBVO[])reobj.toArray(new SaleorderBVO[0]);
    return null;
  }

  public UFDouble queryCachPayByOrdId(String csaleid)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      UFDouble num = dmo.queryCachPayByOrdId(csaleid);
      return num;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public AggregatedValueObject queryData(String strID)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      SaleOrderVO saleorder = dmo.queryData(strID);
      return saleorder;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public SaleorderHVO[] queryHeadAllData(String strWhere)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      SaleorderHVO[] saleorderHs = (SaleorderHVO[])(SaleorderHVO[])dmo.queryAllHeadData(strWhere);

      return saleorderHs;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public ArrayList queryInfo(Integer iSel, Object[] alQryCond)
    throws BusinessException
  {
    ArrayList item = new ArrayList();
    try
    {
      ArrayList ids = new ArrayList();
      for (int i = 0; i < alQryCond.length; i++) {
        ids.add((String)((ArrayList)alQryCond[i]).get(1));
      }

      IICPub_MiscDMO dmo = (IICPub_MiscDMO)NCLocator.getInstance().lookup(IICPub_MiscDMO.class.getName());

      item = dmo.getInvInfo(ids);
    }
    catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }

    return item;
  }

  public SaleorderBVO[] queryOrderEnd(String strWhere) throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      SaleorderBVO[] saleorderBs = dmo.queryOrderEnd(strWhere);
      if ((saleorderBs != null) && (saleorderBs.length != 0))
      {
        String[] orderbids = new String[1];

        String strSql = "SELECT so_saleorder_b.corder_bid FROM so_sale INNER JOIN so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid INNER JOIN so_saleexecute ON (so_saleorder_b.corder_bid = so_saleexecute.csale_bid and so_saleorder_b.csaleid = so_saleexecute.csaleid)  WHERE beditflag = 'N' ";

        if ((strWhere != null) && (!strWhere.equals(""))) {
          strSql = strSql + " and " + strWhere;
        }
        orderbids[0] = strSql;

        SaleReportDMO rdmo = new SaleReportDMO();

        Hashtable htInvoice = rdmo.queryInvoiceAuditSum(null, orderbids);

        Hashtable htReceipt = rdmo.queryReceiptAuditSum(null, orderbids);

        Hashtable htSoOutAudit = rdmo.querySoOutAuditSum(null, orderbids);

        for (int i = 0; i < saleorderBs.length; i++)
        {
          saleorderBs[i].setInvoiceauditNumber(htInvoice.get(saleorderBs[i].getCorder_bid()) == null ? null : (UFDouble)htInvoice.get(saleorderBs[i].getCorder_bid()));

          SCMEnv.out("***************************************************************");

          SCMEnv.out("*********************发票审批数量为：" + htInvoice.get(saleorderBs[i].getCorder_bid()) + "*********************");

          SCMEnv.out("***************************************************************");

          saleorderBs[i].setReceiptauditNumber(htReceipt.get(saleorderBs[i].getCorder_bid()) == null ? null : (UFDouble)htReceipt.get(saleorderBs[i].getCorder_bid()));

          SCMEnv.out("***************************************************************");

          SCMEnv.out("*********************发货审批数量为：" + htReceipt.get(saleorderBs[i].getCorder_bid()) + "*********************");

          SCMEnv.out("***************************************************************");

          saleorderBs[i].setOutstoreauditNumber(htSoOutAudit.get(saleorderBs[i].getCorder_bid()) == null ? null : (UFDouble)htSoOutAudit.get(saleorderBs[i].getCorder_bid()));

          SCMEnv.out("***************************************************************");

          SCMEnv.out("*********************出库审批数量为：" + htSoOutAudit.get(saleorderBs[i].getCorder_bid()) + "*********************");

          SCMEnv.out("***************************************************************");
        }
      }

      return saleorderBs;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public void returnCode(SaleOrderVO vo)
    throws BusinessException
  {
    if (vo == null)
      return;
    try
    {
      if ((((SaleorderHVO)vo.getParentVO()).getVoldreceiptcode() != null) && (((SaleorderHVO)vo.getParentVO()).getVoldreceiptcode().length() > 0))
      {
        CheckValueValidityImpl dmocode = new CheckValueValidityImpl();

        dmocode.returnBillNo(vo);
      }
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public void saveSaleBillForImport(SaleOrderVO salevo)
    throws BusinessException
  {
    if (salevo == null) {
      return;
    }
    try
    {
      fillSaleOrderVO(salevo);

      SOATP atpdmo = new SOATP();
      atpdmo.modifyATP(salevo);

      SaleOrderDMO saledmo = new SaleOrderDMO();
      ArrayList obj = saledmo.insert(salevo);

      salevo.setIAction(0);
      saledmo.renovateAR(DoInterfaceForSO.getBillInvokeCreditManager(), salevo);

      OtherInterfaceDMO otherdmo = new OtherInterfaceDMO();
      otherdmo.checkATP(salevo);

      OosinfoDMO oosdmo = new OosinfoDMO();
      oosdmo.insertDataFromOrder(salevo);

      String s1 = (String)obj.get(0);
      salevo.getParentVO().setPrimaryKey(s1);
      RecordTimeImpl timebo = new RecordTimeImpl();
      timebo.record(RecordType.SAVE, salevo);
    }
    catch (Exception e)
    {
      String code = ((SaleorderHVO)(SaleorderHVO)salevo.getParentVO()).getVreceiptcode();

      if ((code != null) && (code.trim().length() > 0))
      {
        ((SaleorderHVO)salevo.getParentVO()).setVoldreceiptcode(code);
        try
        {
          CheckValueValidityImpl checkdmo = new CheckValueValidityImpl();
          checkdmo.returnBillNo(salevo);
        } catch (BusinessException ex) {
          throw ex;
        } catch (Exception ex) {
          reportException(ex);
          throw new BusinessException("Remote Call", ex);
        }
      }

      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public ArrayList update(SaleOrderVO sale)
    throws BusinessException
  {
    try
    {
      CheckValueValidityImpl checkDmo = new CheckValueValidityImpl();
      checkDmo.isValueValidity(sale);
      SaleOrderDMO dmo = new SaleOrderDMO();
      return dmo.update(sale);
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public void orderUpdateLock(String id, String cfreezid)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      dmo.updateLock(id, cfreezid);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public void updateOrderEnd(SaleorderBVO[] saleorderB) throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      dmo.updateOrderEnd(saleorderB);
    } catch (Exception e) {
      reportException(e);
      throw BillTools.wrappBusinessExceptionForSO(e);
    }
  }

  public void updateRetinvFlag(String id)
    throws BusinessException
  {
    try
    {
      SaleOrderDMO dmo = new SaleOrderDMO();
      dmo.updateRetinvFlag(id);
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  private void reportException(Exception e) {
    SCMEnv.out(e.getMessage());
  }

  public AggregatedValueObject[] queryDataByWhere(String swhere) throws BusinessException
  {
    SaleOrderDMO dmo = null;
    AggregatedValueObject[] vos = null;
    try {
      dmo = new SaleOrderDMO();
      vos = dmo.queryDataByWhere(swhere);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }

    return vos;
  }

  public CircularlyAccessibleValueObject[] queryAllBodyDataByBIDs(String[] bodykeys) throws BusinessException
  {
    SaleOrderDMO dmo = null;
    CircularlyAccessibleValueObject[] vos = null;
    try {
      dmo = new SaleOrderDMO();
      vos = dmo.queryAllBodyDataByBIDs(bodykeys);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }

    return vos;
  }

  public void priceChangeToSquare(ArrayList pList, String sOperid) throws BusinessException
  {
    SaleOrderDMO dmo = null;
    try
    {
      dmo = new SaleOrderDMO();
      dmo.priceChangeToSquare(pList, sOperid);
    } catch (Exception e) {
      SCMEnv.out(e.getMessage());
      throw new BusinessRuntimeException(e.getMessage());
    }
  }

  public Hashtable queryForCntAll(String sPk_corp, String[] sman, String[] sven, UFDate date)
    throws BusinessException
  {
    try
    {
      Hashtable ht = null;

      if (!checkCTisExist(sPk_corp))
        return null;
      ICtToSo_QueryCt iInterInstance = (ICtToSo_QueryCt)NCLocator.getInstance().lookup(ICtToSo_QueryCt.class.getName());

      if (iInterInstance != null) {
        RetCtToPoQueryVO[][] sReturn = (RetCtToPoQueryVO[][])null;

        RetCtToPoQueryVO[] rect = null;
        RetCtToPoQueryVO tmp = null;
        ht = new Hashtable();

        for (int k = 0; k < sman.length; k++) {
          sReturn = iInterInstance.queryForCntAll(sPk_corp, new String[] { sman[k] }, new String[] { sven[k] }, date);

          if (sReturn.length <= 0)
            continue;
          rect = sReturn[0];
          if ((rect == null) || (rect.length == 0))
            continue;
          tmp = rect[0];//shikun
          for (int j = 1; j < rect.length; j++) {
            if (tmp.getActrualValidate().compareTo(rect[j].getActrualValidate()) <= 0)
              continue;
            tmp = rect[j];
          }
          ht.put(sman[k], tmp);
        }

      }

      return ht;
    } catch (Exception e) {
      if ((e instanceof ClassNotFoundException))
        return null;
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException(e.getMessage());
    }
  }

  public boolean checkCTisExist(String pk_corp)
  {
    try
    {
      String beanname = ICreateCorpQueryService.class.getName();
      ICreateCorpQueryService bs = (ICreateCorpQueryService)NCLocator.getInstance().lookup(beanname);

      return bs.isEnabled(pk_corp, "CT");
    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
    }return false;
  }

  public SaleOrderVO querySourceBillVOForLinkAdd(String billid, String billtype, String pk_corp, Object otherparam)
    throws BusinessException
  {
    try
    {
      StringBuffer sbf = new StringBuffer();
      sbf.append(" so_sale.csaleid='").append(billid).append("' ").append(" and so_sale.pk_corp = '").append(pk_corp).append("' ").append(" and fstatus = 2 ").append("  AND boutendflag = 'N' AND COALESCE(so_sale.bdeliver,'N') = 'N' AND COALESCE(so_saleorder_b.bdericttrans,'N') = 'N' AND so_saleexecute.bifinventoryfinish = 'N'  ").append("  and exists (select bd_invbasdoc.pk_invbasdoc from bd_invbasdoc where ").append(" so_saleorder_b.cinvbasdocid=bd_invbasdoc.pk_invbasdoc and bd_invbasdoc.laborflag='N' and bd_invbasdoc.discountflag='N' ) ").append("  and abs(isnull(so_saleexecute.ntotalshouldoutnum,0.0)+isnull(so_saleexecute.ntotalinventorynumber,0.0))<abs(so_saleorder_b.nnumber)*(1+isnull(nouttoplimit,0)/100.)+isnull(ntranslossnum,0) ").append(" and so_saleorder_b.frowstatus<>4 and so_saleorder_b.frowstatus<>6  ");

      SaleOrderDMO sdmo = new SaleOrderDMO();
      SaleOrderVO[] svos = (SaleOrderVO[])(SaleOrderVO[])sdmo.queryDataByWhere(sbf.toString());

      return (svos == null) || (svos.length == 0) ? null : svos[0];
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e); 
      throw new BusinessException(e.getMessage());
    }
  }
}