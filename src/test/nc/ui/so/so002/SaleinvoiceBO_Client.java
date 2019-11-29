package nc.ui.so.so002;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.framework.common.NCLocator;
import nc.itf.scm.so.so002.ISaleinvoice;
import nc.itf.scm.so.so002.ISaleinvoiceQuery;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.so.pub.AvgSaleQueryVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;

public class SaleinvoiceBO_Client
{
  private static String beanName = ISaleinvoice.class.getName();
  private static String beanNameQ = ISaleinvoiceQuery.class.getName();

  public static void delete(String arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.invoiceDelete(arg0);
  }

  public static ArrayList insert(SaleinvoiceVO arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    ArrayList o = null;
    o = bo.insert(arg0);
    return o;
  }

  public static void close(String arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.close(arg0);
  }

  public static ArrayList update(SaleinvoiceVO arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    ArrayList o = null;
    o = bo.update(arg0);
    return o;
  }

  public static AvgSaleQueryVO getAvgSaleData(AvgSaleQueryVO arg0, String arg1) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    AvgSaleQueryVO o = null;
    o = bo.getAvgSaleData(arg0, arg1);
    return o;
  }

  public static SaleinvoiceVO queryData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleinvoiceVO o = null;
    o = bo.queryData(arg0);
    return o;
  }

  public static int getStatus(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    int o = 0;
    o = bo.getStatus(arg0);
    return o;
  }

  public static double getInvoiceNumber(String arg0, String arg1) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    double o = 0.0D;
    o = bo.getInvoiceNumber(arg0, arg1);
    return o;
  }

  public static String[] getOrderNumber(String arg0, String arg1) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    String[] o = null;
    o = bo.getOrderNumber(arg0, arg1);
    return o;
  }

  public static SaleinvoiceVO queryInitData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleinvoiceVO o = null;
    o = bo.queryInitData(arg0);
    return o;
  }

  public static String getVerifyrule(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    String o = null;
    o = bo.getVerifyrule(arg0);
    return o;
  }

  public static SaleorderHVO[] queryOrder(String arg0, String arg1, String arg2) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleorderHVO[] o = null;
    o = bo.queryOrder(arg0, arg1, arg2);
    return o;
  }

  public static SaleVO[] queryAllHeadData(String arg0, String arg1, String arg2) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleVO[] o = null;
    o = bo.queryAllHeadData(arg0, arg1, arg2);
    return o;
  }

  public static SaleinvoiceBVO[] queryBodyData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleinvoiceBVO[] o = null;
    o = bo.queryBodyData(arg0);
    return o;
  }

  public static SaleVO[] queryHeadAllData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleVO[] o = null;
    o = bo.queryHeadAllData(arg0);
    return o;
  }

  public static SaleVO[] queryAllData() throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleVO[] o = null;
    o = bo.queryAllData();
    return o;
  }

  public static void approve(SaleinvoiceVO arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.approve(arg0);
  }

  public static void approve0(String arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.approve0(arg0);
  }

  public static void checkArsubValidity(Hashtable arg0, boolean arg1) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.checkArsubValidity(arg0, arg1);
  }

  public static Hashtable fillDataWithARSubAcct(Hashtable arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    Hashtable o = null;
    o = bo.fillDataWithARSubAcct(arg0);
    return o;
  }

  public static void freeze(String arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.freeze(arg0);
  }

  public static String[][] getCustomerInfo(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    String[][] o = (String[][])null;
    o = bo.getCustomerInfo(arg0);
    return o;
  }

  public static SaleinvoiceVO getDataFrom4CTo32(SaleinvoiceVO arg0, String arg1) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleinvoiceVO o = null;
    o = bo.getDataFrom4CTo32(arg0, arg1);
    return o;
  }

  public static String[][] getInventoryInfo(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    String[][] o = (String[][])null;
    o = bo.getInventoryInfo(arg0);
    return o;
  }

  public static String getInvoiceCode(String arg0, String arg1) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    String o = null;
    o = bo.getInvoiceCode(arg0, arg1);
    return o;
  }

  public static String[][] getInvoiceType() throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    String[][] o = (String[][])null;
    o = bo.getInvoiceType();
    return o;
  }

  public static String[][] getLang(String[][] arg0, String arg1, int arg2, int arg3) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    String[][] o = (String[][])null;
    o = bo.getLang(arg0, arg1, arg2, arg3);
    return o;
  }

  public static Hashtable getOutNumber(Hashtable arg0, String arg1) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    Hashtable o = null;
    o = bo.getOutNumber(arg0, arg1);
    return o;
  }

  public static double getOutNumber(String arg0, String arg1, String arg2) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    double o = 0.0D;
    o = bo.getOutNumber(arg0, arg1, arg2);
    return o;
  }

  public static String[][] getWarehouseInfo(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    String[][] o = (String[][])null;
    o = bo.getWarehouseInfo(arg0);
    return o;
  }

  public static void giveup(String arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.giveup(arg0);
  }

  public static ArrayList insertInit(SaleinvoiceVO arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    ArrayList o = null;
    o = bo.insertInit(arg0);
    return o;
  }

  public static boolean isCodeExist(String arg0, String arg1) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    boolean o = false;
    o = bo.isCodeExist(arg0, arg1);
    return o;
  }

  public static Hashtable queryBillCodeBySource(int arg0, String arg1) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    Hashtable o = null;
    o = bo.queryBillCodeBySource(arg0, arg1);
    return o;
  }

  public static SaleinvoiceBVO[] queryBodyAllData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleinvoiceBVO[] o = null;
    o = bo.queryBodyAllData(arg0);
    return o;
  }

  public static SaleinvoiceVO[] queryDataBills(String[] arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleinvoiceVO[] o = null;
    o = bo.queryDataBills(arg0);
    return o;
  }

  public static SaleVO queryHeadData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleVO o = null;
    o = bo.queryHeadData(arg0);
    return o;
  }

  public static SaleinvoiceBVO[] queryInitBodyData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleinvoiceBVO[] o = null;
    o = bo.queryInitBodyData(arg0);
    return o;
  }

  public static SaleorderHVO[] queryInitOrderData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleorderHVO[] o = null;
    o = bo.queryInitOrderData(arg0);
    return o;
  }

  public static SaleorderBVO[] queryOrderBodyList(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleorderBVO[] o = null;
    o = bo.queryOrderBodyList(arg0);
    return o;
  }

  public static SaleorderHVO[] queryOrderData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleorderHVO[] o = null;
    o = bo.queryOrderData(arg0);
    return o;
  }

  public static SaleorderHVO[] queryOrderList(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleorderHVO[] o = null;
    o = bo.queryOrderList(arg0);
    return o;
  }

  public static SaleinvoiceVO[] queryOrderStore(SaleinvoiceVO[] arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    SaleinvoiceVO[] o = null;
    o = bo.queryOrderStore(arg0);
    return o;
  }

  public static Hashtable queryStrikeData(String arg0) throws Exception
  {
    ISaleinvoiceQuery bo = (ISaleinvoiceQuery)NCLocator.getInstance().lookup(beanNameQ);
    Hashtable o = null;
    o = bo.queryStrikeData(arg0);
    return o;
  }

  public static void saveSaleInvoiceForImport(SaleinvoiceVO arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.saveSaleInvoiceForImport(arg0);
  }

  public static ArrayList updateInit(SaleinvoiceVO arg0) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    ArrayList o = null;
    o = bo.updateInit(arg0);
    return o;
  }

  public static void updateLock(String arg0, String arg1) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.invoiceUpdateLock(arg0, arg1);
  }

  public static void writeToARSub(SaleinvoiceVO arg0, Hashtable arg1, boolean arg2) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    bo.writeToARSub(arg0, arg1, arg2);
  }

  public static SaleinvoiceVO autoUniteInvoiceToUI(SaleinvoiceVO arg0, ClientLink arg1) throws Exception
  {
    ISaleinvoice bo = (ISaleinvoice)NCLocator.getInstance().lookup(beanName);
    SaleinvoiceVO vo = bo.autoUniteInvoiceToUI(arg0, arg1);
    return vo;
  }
}