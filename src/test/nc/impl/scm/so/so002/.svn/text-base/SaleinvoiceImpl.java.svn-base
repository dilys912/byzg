package nc.impl.scm.so.so002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.impl.scm.so.pub.CheckRelationDMO;
import nc.impl.scm.so.pub.CheckValueValidityImpl;
import nc.impl.scm.so.pub.DataControlDMO;
import nc.impl.scm.so.so001.SaleOrderDMO;
import nc.impl.scm.so.so016.SOToolsDMO;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.scm.so.so002.ISaleinvoice;
import nc.itf.scm.so.so002.ISaleinvoiceQuery;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.so.pub.AvgSaleQueryVO;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;

public class SaleinvoiceImpl
  implements ISaleinvoice, ISaleinvoiceQuery
{
  public void approve(SaleinvoiceVO sale)
    throws BusinessException
  {
    try
    {
      String strID = sale.getParentVO().getPrimaryKey();
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      dmo.approve(strID);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public void approve0(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      dmo.approve(strID);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public void checkArsubValidity(Hashtable hsArsub, boolean bstrikeflag)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      if (!dmo.checkArSubValidity(hsArsub, bstrikeflag))
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060501", "UPP40060501-000000"));
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060501", "UPP40060501-000000") + e.getMessage());
    }
  }

  public void close(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      dmo.close(strID);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public void invoiceDelete(String id)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      dmo.delete(id);
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
      throw new BusinessException(e.getMessage());
    }
  }

  public Hashtable fillDataWithARSubAcct(Hashtable hsarsubacct)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      Hashtable hsFullData = dmo.fillDatawithARSubAcct(hsarsubacct);
      return hsFullData;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  private void fillSaleInvoiceVO(SaleinvoiceVO vos)
    throws BusinessException
  {
    if ((vos == null) || (vos.getChildrenVO() == null) || (vos.getChildrenVO().length <= 0))
    {
      return;
    }Hashtable rehs = null;
    String[] ids = null;
    try
    {
      SaleinvoiceBVO[] bvos = (SaleinvoiceBVO[])(SaleinvoiceBVO[])vos.getChildrenVO();
      ids = new String[bvos.length];
      for (int i = 0; i < bvos.length; i++) {
        ids[i] = bvos[i].getCinventoryid();
      }
      SaleOrderDMO dmo = new SaleOrderDMO();
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

  public void freeze(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      dmo.freeze(strID);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public AvgSaleQueryVO getAvgSaleData(AvgSaleQueryVO voQuery, String strCorpID) throws BusinessException {
    AvgSaleQueryVO queryData = new AvgSaleQueryVO();
    queryData.setIqueryday(voQuery.getIqueryday());
    queryData.setDqueryDate(voQuery.getDquerydate());
    queryData.setCinvbasdocid(voQuery.getCinvbasdocid());
    queryData.setCinvmandocid(voQuery.getCinvmandocid());
    queryData.setCinvcode(voQuery.getCinvcode());
    queryData.setCinvname(voQuery.getCinvname());
    queryData.setCinvspec(voQuery.getCinvspec());
    queryData.setCinvtype(voQuery.getCinvtype());
    queryData.setCunitid(voQuery.getCunitid());
    queryData.setCunitname(voQuery.getCunitname());
    String pk_corp = strCorpID;
    if (pk_corp == null)
      return null;
    if (queryData.getCinvbasdocid() == null)
      return null;
    try {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      queryData.setNinvoicenum(dmo.getInvInvoiceNumber(queryData.getCinvbasdocid(), queryData.getCinvmandocid(), queryData.getDquerydate(), queryData.getIqueryday(), pk_corp));

      queryData.setNordernum(dmo.getInvOrderNumber(queryData.getCinvbasdocid(), queryData.getCinvmandocid(), queryData.getDquerydate(), queryData.getIqueryday(), pk_corp));

      queryData.setNoutnum(dmo.getInvOutNumber(queryData.getCinvmandocid(), queryData.getDquerydate(), queryData.getIqueryday(), pk_corp));
    }
    catch (Exception e)
    {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return queryData;
  }

  public String[][] getCustomerInfo(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      String[][] results = dmo.getCustomerInfo(strID);
      return results;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleinvoiceVO getDataFrom4CTo32(SaleinvoiceVO saleinvoice, String cfirsttype)
    throws BusinessException
  {
    ArrayList csourcebillbodyids = new ArrayList();
    SaleinvoiceBVO[] bodys = (SaleinvoiceBVO[])(SaleinvoiceBVO[])saleinvoice.getChildrenVO();
    for (int i = 0; i < bodys.length; i++) {
      csourcebillbodyids.add(bodys[i].getCsourcebillbodyid());
    }
    try
    {
      for (int i = 0; i < bodys.length; i++) {
        String cnewfirsttype = bodys[i].getCreceipttype();
        if ("3U".equals(cnewfirsttype)) {
          if (csourcebillbodyids.size() > 0) {
            String[] fieldnames = { "pk_apply_b", "ccurrencytypeid", "ndiscountrate", "nitemdiscountrate", "nexchangeotobrate", "nexchangeotoarate", "ntaxrate", "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "nprice", "ntaxprice", "nnetprice", "ntaxnetprice", "noriginalcurnetprice", "noriginalcurprice", "noriginalcurtaxnetprice", "noriginalcurtaxprice", "nsummny", "noriginalcursummny", "pk_productline", "noriginalcurdiscountmny", "noriginalcurtaxmny", "noriginalcurmny" };

            HashMap hsrow = SOToolsDMO.getAnyValueSORow("so_apply_b", fieldnames, "pk_apply_b", (String[])(String[])csourcebillbodyids.toArray(new String[csourcebillbodyids.size()]), null);

            if ((hsrow != null) && (hsrow.size() > 0)) {
              SORowData row = null;
              int k = 0; for (int loopk = bodys.length; k < loopk; k++) {
                if ((bodys[k] == null) && (bodys[k].getCsourcebillbodyid() == null))
                  continue;
                row = (SORowData)hsrow.get(bodys[k].getCsourcebillbodyid());
                if (row == null)
                  continue;
                bodys[k].setCcurrencytypeid(row.getString(1));
                bodys[k].setNdiscountrate(row.getUFDouble(2));
                bodys[k].setNitemdiscountrate(row.getUFDouble(3));
                bodys[k].setNexchangeotobrate(row.getUFDouble(4));
                bodys[k].setNexchangeotoarate(row.getUFDouble(5));
                bodys[k].setNtaxrate(row.getUFDouble(6));
                bodys[k].setNoriginalcurprice(row.getUFDouble(7));
                bodys[k].setNoriginalcurtaxprice(row.getUFDouble(8));
                bodys[k].setNoriginalcurnetprice(row.getUFDouble(9));
                bodys[k].setNoriginalcurtaxnetprice(row.getUFDouble(10));
                bodys[k].setNprice(row.getUFDouble(11));
                bodys[k].setNtaxprice(row.getUFDouble(12));
                bodys[k].setNnetprice(row.getUFDouble(13));
                bodys[k].setNtaxnetprice(row.getUFDouble(14));
                bodys[k].setNquotenetprice(row.getUFDouble(13));
                bodys[k].setNquoteprice(row.getUFDouble(11));
                bodys[k].setNquotetaxnetprice(row.getUFDouble(14));
                bodys[k].setNquotetaxprice(row.getUFDouble(12));
                bodys[k].setNquoteoriginalcurnetprice(row.getUFDouble(15));
                bodys[k].setNquoteoriginalcurprice(row.getUFDouble(16));
                bodys[k].setNquoteoriginalcurtaxprice(row.getUFDouble(18));
                bodys[k].setNquoteoriginalcurtaxnetprice(row.getUFDouble(17));
                bodys[k].setNsubquotenetprice(row.getUFDouble(13));
                bodys[k].setNsubquoteprice(row.getUFDouble(11));
                bodys[k].setNsubquotetaxnetprice(row.getUFDouble(14));
                bodys[k].setNsubquotetaxprice(row.getUFDouble(12));

                bodys[k].setNsubtaxnetprice(row.getUFDouble(14));
                bodys[k].setCprolineid(row.getString(21));
                bodys[k].setNoriginalcurdiscountmny(row.getUFDouble(22));
                bodys[k].setNoriginalcurtaxmny(row.getUFDouble(23));

                bodys[k].setNsubcursummny(row.getUFDouble(19));
              }
            }
          }
        }
        else if (csourcebillbodyids.size() > 0) {
          String[] fieldnames = { "corder_bid", "ccurrencytypeid", "ndiscountrate", "nitemdiscountrate", "nexchangeotobrate", "nexchangeotoarate", "ntaxrate", "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "nprice", "ntaxprice", "nnetprice", "ntaxnetprice", "noriginalcurnetprice", "noriginalcurprice", "noriginalcurtaxnetprice", "noriginalcurtaxprice", "nsummny", "noriginalcursummny", "cprolineid", "noriginalcurdiscountmny", "noriginalcurtaxmny", "noriginalcurmny", "ct_manageid" };

          HashMap hsrow = SOToolsDMO.getAnyValueSORow("so_saleorder_b", fieldnames, "corder_bid", (String[])(String[])csourcebillbodyids.toArray(new String[csourcebillbodyids.size()]), null);

          if ((hsrow != null) && (hsrow.size() > 0)) {
            SORowData row = null;
            int k = 0; for (int loopk = bodys.length; k < loopk; k++) {
              if ((bodys[k] == null) && (bodys[k].getCsourcebillbodyid() == null))
                continue;
              row = (SORowData)hsrow.get(bodys[k].getCsourcebillbodyid());
              if (row == null)
                continue;
              bodys[k].setCcurrencytypeid(row.getString(1));
              bodys[k].setNdiscountrate(row.getUFDouble(2));
              bodys[k].setNitemdiscountrate(row.getUFDouble(3));
              bodys[k].setNexchangeotobrate(row.getUFDouble(4));
              bodys[k].setNexchangeotoarate(row.getUFDouble(5));
              bodys[k].setNtaxrate(row.getUFDouble(6));
              bodys[k].setNoriginalcurprice(row.getUFDouble(7));
              bodys[k].setNoriginalcurtaxprice(row.getUFDouble(8));
              bodys[k].setNoriginalcurnetprice(row.getUFDouble(9));
              bodys[k].setNoriginalcurtaxnetprice(row.getUFDouble(10));
              bodys[k].setNprice(row.getUFDouble(11));
              bodys[k].setNtaxprice(row.getUFDouble(12));
              bodys[k].setNnetprice(row.getUFDouble(13));
              bodys[k].setNtaxnetprice(row.getUFDouble(14));
              bodys[k].setNquotenetprice(row.getUFDouble(13));
              bodys[k].setNquoteprice(row.getUFDouble(11));
              bodys[k].setNquotetaxnetprice(row.getUFDouble(14));
              bodys[k].setNquotetaxprice(row.getUFDouble(12));
              bodys[k].setNquoteoriginalcurnetprice(row.getUFDouble(15));
              bodys[k].setNquoteoriginalcurprice(row.getUFDouble(16));
              bodys[k].setNquoteoriginalcurtaxprice(row.getUFDouble(18));
              bodys[k].setNquoteoriginalcurtaxnetprice(row.getUFDouble(17));
              bodys[k].setNsubquotenetprice(row.getUFDouble(13));
              bodys[k].setNsubquoteprice(row.getUFDouble(11));
              bodys[k].setNsubquotetaxnetprice(row.getUFDouble(14));
              bodys[k].setNsubquotetaxprice(row.getUFDouble(12));

              bodys[k].setNsubtaxnetprice(row.getUFDouble(14));
              bodys[k].setCprolineid(row.getString(21));
              bodys[k].setNoriginalcurdiscountmny(row.getUFDouble(22));
              bodys[k].setNoriginalcurtaxmny(row.getUFDouble(23));

              bodys[k].setCt_manageid(row.getString(25));

              bodys[k].setNsubcursummny(row.getUFDouble(19));
            }
          }
        }
      }
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }

    return saleinvoice;
  }

  public String[][] getInventoryInfo(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      String[][] results = dmo.getInventoryInfo(strID);
      return results;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public String getInvoiceCode(String id, String pk_corp)
    throws BusinessException
  {
    String code = "";
    try {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      code = dmo.getInvoiceCode(id, pk_corp);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return code;
  }

  public double getInvoiceNumber(String saleid, String sale_bid)
    throws BusinessException
  {
    double invoicenumber = 0.0D;
    try {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      invoicenumber = dmo.getInvoiceNumber(saleid, sale_bid);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return invoicenumber;
  }

  public String[][] getInvoiceType()
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      String[][] results = dmo.getInvoiceType();
      return getLang(results, "soinvoicetype", 1, 2);
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public String[][] getLang(String[][] arySource, String sProdCode, int iContIndex, int iResIndex)
  {
    for (int i = 0; i < arySource.length; i++) {
      String sContent = arySource[i][iContIndex];
      String sResid = arySource[i][iResIndex];

      NCLangResOnserver langResOnserver = NCLangResOnserver.getInstance();

      String slang = langResOnserver.getString(sProdCode, sContent, sResid);

      arySource[i][iContIndex] = slang;
    }
    return arySource;
  }

  public String[] getOrderNumber(String orderID, String orderBID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      String[] result = dmo.getOrderNumber(orderID, orderBID);
      return result;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public double getOutNumber(String saleid, String sale_bid, String SO_06)
    throws BusinessException
  {
    double invoicenumber = 0.0D;
    try {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      invoicenumber = dmo.getOutNumber(saleid, sale_bid, SO_06);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return invoicenumber;
  }

  public Hashtable getOutNumber(Hashtable hsBid, String SO_06)
    throws BusinessException
  {
    Hashtable hsOutNumByBid = new Hashtable();
    try {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      hsOutNumByBid = dmo.getOutNumber(hsBid, SO_06);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return hsOutNumByBid;
  }

  public int getStatus(String key)
    throws BusinessException
  {
    int status = -1;
    try {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      status = dmo.getStatus(key);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return status;
  }

  public String getVerifyrule(String sBuzitype)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      return dmo.getVerifyrule(sBuzitype);
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public String[][] getWarehouseInfo(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      String[][] results = dmo.getWarehouseInfo(strID);
      return results;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public void giveup(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      dmo.giveup(strID);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public ArrayList insert(SaleinvoiceVO btVO)
    throws BusinessException
  {
    ArrayList listBillID = new ArrayList();
    try
    {
      IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
      srv.checkDefDataType(btVO);
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();

      listBillID = dmo.insert(btVO);
    }
    catch (BusinessException e) {
      throw e;
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }

    return listBillID;
  }

  public ArrayList insertInit(SaleinvoiceVO btVO)
    throws BusinessException
  {
    ArrayList listBillID = new ArrayList();
    try
    {
      IScm srv = (IScm)NCLocator.getInstance().lookup(IScm.class.getName());
      srv.checkDefDataType(btVO);
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();

      listBillID = dmo.insertInit(btVO);
    }
    catch (BusinessException e) {
      throw e;
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }

    return listBillID;
  }

  public boolean isCodeExist(String code, String pk_corp)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      return dmo.isCodeExist(code, pk_corp);
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleVO[] queryAllData()
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleVO[] sales = dmo.queryHeadAllData();
      return sales;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    } 
  }

  public SaleVO[] queryAllHeadData(String corpID, String userID, String curDate)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleVO[] sales = dmo.queryAllHeadData(corpID, userID, curDate);
      return sales;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public Hashtable queryBillCodeBySource(int type, String range)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      Hashtable h = dmo.queryBillCodeBySource(type, range);
      return h;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleinvoiceBVO[] queryBodyAllData(String strWhere) throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleinvoiceBVO[] saleinvoiceBs = (SaleinvoiceBVO[])(SaleinvoiceBVO[])dmo.queryAllBodyData(strWhere);
      return saleinvoiceBs;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleinvoiceBVO[] queryBodyData(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleinvoiceBVO[] saleinvoicebs = dmo.queryBodyData(strID);
      return saleinvoicebs;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleinvoiceVO queryData(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleinvoiceVO saleinvoice = dmo.queryData(strID);
      return saleinvoice;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleinvoiceVO[] queryDataBills(String[] strIDs)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleinvoiceVO[] saleinvoice = dmo.queryDataBills(strIDs);
      return saleinvoice;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleVO[] queryHeadAllData(String where)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleVO[] sales = (SaleVO[])(SaleVO[])dmo.queryAllHeadData(where);
      return sales;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleVO queryHeadData(String key)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleVO sale = dmo.queryHeadData(key);
      return sale;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleinvoiceBVO[] queryInitBodyData(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleinvoiceBVO[] saleinvoicebs = dmo.queryInitBodyData(strID);
      return saleinvoicebs;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleinvoiceVO queryInitData(String strID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleinvoiceVO saleinvoice = dmo.queryInitData(strID);
      return saleinvoice;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleorderHVO[] queryInitOrderData(String custID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleorderHVO[] sales = dmo.queryInitOrderData(custID);
      return sales;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleorderHVO[] queryOrder(String pk_corp, String userID, String curDate)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleorderHVO[] sales = dmo.queryOrder(pk_corp, userID, curDate);
      return sales;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleorderBVO[] queryOrderBodyList(String id)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleorderBVO[] sales = dmo.queryOrderBodyList(id);
      return sales;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public AggregatedValueObject[] queryDataByWhere(String swhere)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      return dmo.queryDataByWhere(swhere);
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleorderHVO[] queryOrderData(String custID)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleorderHVO[] sales = dmo.queryOrderData(custID);
      return sales;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException("SaleinvoiceBO::queryOrderData(String) Exception!", e);
    }
  }

  public SaleorderHVO[] queryOrderList(String pk_corp)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleorderHVO[] sales = dmo.queryOrderList(pk_corp);
      return sales;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public SaleinvoiceVO[] queryOrderStore(SaleinvoiceVO[] saleinvoice)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      SaleinvoiceVO[] sales = dmo.queryOrderStore(saleinvoice);
      return sales;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public Hashtable queryStrikeData(String cinvoiceid)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      Hashtable hs = dmo.queryStrikeData(cinvoiceid);
      return hs;
    }
    catch (Exception e) {
      reportException(e);
    throw new BusinessException(e.getMessage());
    }
  }

  public void saveSaleInvoiceForImport(SaleinvoiceVO saleinvoicevo)
    throws BusinessException
  {
    if (saleinvoicevo == null)
      return;
    try
    {
      fillSaleInvoiceVO(saleinvoicevo);

      UFBoolean isGather = ((SaleVO)saleinvoicevo.getParentVO()).getIsGather();

      if ((isGather != null) && (isGather.booleanValue())) {
        SaleinvoiceDMO saleinvoiceDMO = new SaleinvoiceDMO();

        saleinvoicevo = saleinvoiceDMO.getGather(saleinvoicevo);

        BillRowNoDMO billdmo = new BillRowNoDMO();
        BillRowNoDMO.setVORowNoByRule(saleinvoicevo, "32", "crowno");
      }

      CheckRelationDMO relationdmo = new CheckRelationDMO();
      relationdmo.isInvoiceRelation(saleinvoicevo);
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      ArrayList list = dmo.insert(saleinvoicevo);
      DataControlDMO datacontrol = new DataControlDMO();
      datacontrol.setTotalInvoiceNum(saleinvoicevo);
      datacontrol.autoSetInvoicetFinish(saleinvoicevo);

      CheckValueValidityImpl value = new CheckValueValidityImpl();
      value.checkSaleOrderTInvnu(saleinvoicevo);
    }
    catch (BusinessException e) {
      throw e;
    }
    catch (Exception e) {
      String code = ((SaleVO)(SaleVO)saleinvoicevo.getParentVO()).getVreceiptcode();
      if ((code != null) && (code.trim().length() > 0)) {
        ((SaleVO)saleinvoicevo.getParentVO()).setVoldreceiptcode(code);
        try {
          CheckValueValidityImpl checkdmo = new CheckValueValidityImpl();

          checkdmo.returnBillNo(saleinvoicevo);
        }
        catch (Exception ex) {
          throw new BusinessException(e.getMessage());
        }
      }
      throw new BusinessException(e.getMessage());
    }
  }

  public ArrayList update(SaleinvoiceVO btVO)
    throws BusinessException
  {
    ArrayList listBillID = new ArrayList();
    try {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      listBillID = dmo.update(btVO);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return listBillID;
  }

  public ArrayList updateInit(SaleinvoiceVO btVO)
    throws BusinessException
  {
    ArrayList listBillID = new ArrayList();
    try {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      listBillID = dmo.updateInit(btVO);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
    return listBillID;
  }

  public void invoiceUpdateLock(String id, String cfreezid)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      dmo.updateLock(id, cfreezid);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException(e.getMessage());
    }
  }

  public void writeToARSub(SaleinvoiceVO saleinvoice, Hashtable hsArsub, boolean bstrikeflag)
    throws BusinessException
  {
    try
    {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      dmo.writeToARSub(saleinvoice, hsArsub, bstrikeflag);
    }
    catch (BusinessException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
      throw new BusinessException(e.getMessage());
    }
  }

  private void reportException(Exception e) {
    SCMEnv.out(e);
  }

  public SaleinvoiceVO autoUniteInvoiceToUI(SaleinvoiceVO saleinvoice, ClientLink cl)
    throws BusinessException
  {
    SaleinvoiceVO vret = null;
    try {
      SaleinvoiceDMO dmo = new SaleinvoiceDMO();
      saleinvoice.getParentVO().setStatus(1);
      for (int i = 0; i < saleinvoice.getChildrenVO().length; i++) {
        saleinvoice.getChildrenVO()[i].setStatus(1);
      }
      vret = dmo.autoUniteInvoice(saleinvoice);
      PfUtilBO bo = new PfUtilBO();
      bo.processAction("PreModify", "32", cl.getLogonDate().toString(), null, saleinvoice, null);

      vret = dmo.queryData(saleinvoice.getPrimaryKey());
      vret.setHsArsubAcct(dmo.queryStrikeData(saleinvoice.getPrimaryKey()));
    }
    catch (BusinessException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
      throw new BusinessException(e.getMessage());
    }
    return vret;
  }

  public AggregatedValueObject querySourceBillVOForLinkAdd(String billid, String billtype, String pk_corp, Object otherparam)
    throws BusinessException
  {
    try
    {
      StringBuffer sbf = new StringBuffer();
      sbf.append(" so_saleinvoice.csaleid='").append(billid).append("' ").append(" and so_saleinvoice.pk_corp = '").append(pk_corp).append("' ").append(" and fstatus = 2 ").append(" AND COALESCE(so_saleexecute.ntotalinventorynumber, 0) != so_saleinvoice_b.nnumber ").append(" AND abs(COALESCE(so_saleexecute.ntotalshouldoutnum, 0) + COALESCE(so_saleexecute.ntotalinventorynumber, 0))< abs(so_saleinvoice_b.nnumber) ").append(" AND exists (SELECT so_sale.csaleid FROM so_sale ").append("WHERE so_saleinvoice_b.cupsourcebillid = so_sale.csaleid ").append("AND COALESCE(so_sale.bdeliver,'N') = 'N') ").append(" and exists(select so_saleexecute.csaleid from so_saleexecute where so_saleinvoice_b.cupsourcebillbodyid = so_saleexecute.csale_bid AND COALESCE(so_saleexecute.bifinventoryfinish,'N') = 'N')");

      SaleinvoiceDMO sdmo = new SaleinvoiceDMO();
      SaleOrderVO[] svos = (SaleOrderVO[])(SaleOrderVO[])sdmo.queryDataByWhere(sbf.toString());

      return (svos == null) || (svos.length == 0) ? null : svos[0];
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)){
        throw ((BusinessException)e); 
    }else{
    throw new BusinessException(e.getMessage());
    }
  }
}
}