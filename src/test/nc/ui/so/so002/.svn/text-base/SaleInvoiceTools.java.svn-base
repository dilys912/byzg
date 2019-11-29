package nc.ui.so.so002;

import java.util.ArrayList;
import java.util.HashMap;
import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.ui.so.so016.SOToolsBO_Client;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;

public class SaleInvoiceTools
{
  public UFBoolean BD302 = null;
  public BusinessCurrencyRateUtil currtype = null;

  public SaleInvoiceTools(UFBoolean bBD302, BusinessCurrencyRateUtil bccurrtype) { this.BD302 = bBD302;
    this.currtype = bccurrtype;
  }

  public SaleinvoiceVO getDataFrom4CTo32(SaleinvoiceVO saleinvoice)
  {
    SaleinvoiceBVO[] bodys = (SaleinvoiceBVO[])(SaleinvoiceBVO[])saleinvoice.getChildrenVO();
    SaleVO head = (SaleVO)saleinvoice.getParentVO();
    try
    {
      doSaleinvoiceBVOsByIC(bodys);

      SaleinvoiceBVO[][] vos = (SaleinvoiceBVO[][])(SaleinvoiceBVO[][])SplitBillVOs.getSplitVOs(bodys, new String[] { "creceipttype" });

      SaleinvoiceBVO[] btmp = null;

      int i = 0; for (int iLen = vos.length; i < iLen; i++) {
        btmp = (SaleinvoiceBVO[])vos[i];
        if ((btmp[0].getCreceipttype() != null) && (btmp[0].getCreceipttype().equals("3U"))) {
          doSaleinvoiceBVOsBy3U(btmp);
        }
        else if ((btmp[0].getCreceipttype() != null) && (btmp[0].getCreceipttype().equals("30"))) {
          doSaleinvoiceBVOsBy30(btmp, head);
        }

      }

    }
    catch (Exception e)
    {
      SCMEnv.out(e.getMessage());
    }

    return saleinvoice;
  }

  public void doSaleinvoiceBVOsByIC(SaleinvoiceBVO[] bodys)
    throws Exception
  {
    ArrayList cicbodyids = new ArrayList();
    for (int i = 0; i < bodys.length; i++) {
      cicbodyids.add(bodys[i].getCupsourcebillbodyid());
    }
    String[] fds = { "nquoteprice", "cquotecurrency", "cquoteunitid", "nquoteunitrate" };

    HashMap hsicrows = SOToolsBO_Client.getAnyValueSORow("ic_general_b", fds, "cgeneralbid", (String[])(String[])cicbodyids.toArray(new String[cicbodyids.size()]), null);

    if ((hsicrows != null) && (hsicrows.size() > 0)) {
      SORowData row = null;
      int k = 0; for (int loopk = bodys.length; k < loopk; k++) {
        if ((bodys[k] == null) && (bodys[k].getCupsourcebillbodyid() == null)) {
          continue;
        }
        row = (SORowData)hsicrows.get(bodys[k].getCupsourcebillbodyid());

        if (row == null) {
          continue;
        }
        bodys[k].setCcurrencytypeid(row.getString(1));
        bodys[k].setNquoteoriginalcurtaxnetprice(row.getUFDouble(0));

        bodys[k].setM_outquoteorgcurtaxnetprice(row.getUFDouble(0));
      }
    }
  }

  public void doSaleinvoiceBVOsBy3U(SaleinvoiceBVO[] bodys)
    throws Exception
  {
    ArrayList csourcebillbodyids = new ArrayList();
    for (int i = 0; i < bodys.length; i++) {
      csourcebillbodyids.add(bodys[i].getCsourcebillbodyid());
    }

    String[] fieldnames = { "pk_apply_b", "ccurrencytypeid", "ndiscountrate", "nitemdiscountrate", "nexchangeotobrate", "nexchangeotoarate", "ntaxrate", "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "nprice", "ntaxprice", "nnetprice", "ntaxnetprice", "noriginalcurnetprice", "noriginalcurprice", "noriginalcurtaxnetprice", "noriginalcurtaxprice", "nsummny", "noriginalcursummny", "pk_productline", "noriginalcurdiscountmny", "noriginalcurtaxmny", "noriginalcurmny" };

    HashMap hsrow = SOToolsBO_Client.getAnyValueSORow("so_apply_b", fieldnames, "pk_apply_b", (String[])(String[])csourcebillbodyids.toArray(new String[csourcebillbodyids.size()]), null);

    if ((hsrow != null) && (hsrow.size() > 0)) {
      SORowData row = null;
      int k = 0; for (int loopk = bodys.length; k < loopk; k++) {
        if ((bodys[k] == null) && (bodys[k].getCsourcebillbodyid() == null)) {
          continue;
        }
        row = (SORowData)hsrow.get(bodys[k].getCsourcebillbodyid());

        if (row == null) {
          continue;
        }
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

        bodys[k].setM_salequoteorgcurtaxnetprice(row.getUFDouble(17));

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

  public void doSaleinvoiceBVOsBy30(SaleinvoiceBVO[] bodys, SaleVO head)
    throws Exception
  {
    ArrayList csourcebillbodyids = new ArrayList();
    for (int i = 0; i < bodys.length; i++) {
      csourcebillbodyids.add(bodys[i].getCsourcebillbodyid());
    }

    String[] fieldnames = { "corder_bid", "ccurrencytypeid", "ndiscountrate", "nitemdiscountrate", "nexchangeotobrate", "nexchangeotoarate", "ntaxrate", "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice", "noriginalcurtaxnetprice", "nprice", "ntaxprice", "nnetprice", "ntaxnetprice", "norgqttaxprc", "norgqtprc", "norgqttaxnetprc", "norgqtnetprc", "nsummny", "noriginalcursummny", "cprolineid", "noriginalcurdiscountmny", "noriginalcurtaxmny", "noriginalcurmny", "ct_manageid", "nquoteunitrate", "nqttaxnetprc", "nqtnetprc", "nqttaxprc", "nqtprc" };

    HashMap hsrow = SOToolsBO_Client.getAnyValueSORow("so_saleorder_b", fieldnames, "corder_bid", (String[])(String[])csourcebillbodyids.toArray(new String[csourcebillbodyids.size()]), null);

    if ((hsrow != null) && (hsrow.size() > 0)) {
      SORowData row = null;
      int k = 0; for (int loopk = bodys.length; k < loopk; k++) {
        if ((bodys[k] == null) && (bodys[k].getCsourcebillbodyid() == null)) {
          continue;
        }
        row = (SORowData)hsrow.get(bodys[k].getCsourcebillbodyid());

        if (row == null)
          continue;
        bodys[k].setNdiscountrate(row.getUFDouble(2));

        bodys[k].setNitemdiscountrate(row.getUFDouble(3));

        if ((bodys[k].getCcurrencytypeid() != null) && (!bodys[k].getCcurrencytypeid().equals(row.getString(1))))
        {
          UFDouble[] currrates = getExchangerate(bodys[k].getCcurrencytypeid(), head.getDbilldate().toString());

          bodys[k].setNexchangeotobrate(currrates[0]);
          bodys[k].setNexchangeotoarate(currrates[1]);
          bodys[k].setNtaxrate(row.getUFDouble(6));

          bodys[k].setNquoteoriginalcurtaxprice(bodys[k].getNquoteoriginalcurtaxnetprice().div(bodys[k].getNdiscountrate()).multiply(100.0D).div(bodys[k].getNitemdiscountrate()).multiply(100.0D));
        }
        else {
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
          bodys[k].setNquotenetprice(row.getUFDouble(28));
          bodys[k].setNquoteprice(row.getUFDouble(30));
          bodys[k].setNquotetaxnetprice(row.getUFDouble(27));
          bodys[k].setNquotetaxprice(row.getUFDouble(29));
          bodys[k].setNquoteoriginalcurnetprice(row.getUFDouble(18));

          bodys[k].setNquoteoriginalcurprice(row.getUFDouble(16));

          bodys[k].setNquoteoriginalcurtaxprice(row.getUFDouble(15));

          bodys[k].setM_salequoteorgcurtaxnetprice(row.getUFDouble(17));

          bodys[k].setNsubquotenetprice(row.getUFDouble(28));
          bodys[k].setNsubquoteprice(row.getUFDouble(30));
          bodys[k].setNsubquotetaxnetprice(row.getUFDouble(27));

          bodys[k].setNsubquotetaxprice(row.getUFDouble(29));
          if (bodys[k].getNtaxprice() != null) {
            bodys[k].setNsummny(bodys[k].getNtaxprice().multiply(bodys[k].getNnumber()));
            bodys[k].setNmny(bodys[k].getNprice().multiply(bodys[k].getNnumber()));
          }
          bodys[k].setNsubtaxnetprice(row.getUFDouble(14));
          bodys[k].setCprolineid(row.getString(21));
          bodys[k].setNoriginalcurdiscountmny(row.getUFDouble(22));

          bodys[k].setNoriginalcurtaxmny(row.getUFDouble(23));
          if (bodys[k].getNoriginalcurprice() != null) {
            bodys[k].setNoriginalcurmny(bodys[k].getNoriginalcurprice().multiply(bodys[k].getNnumber()));
            bodys[k].setNoriginalcursummny(bodys[k].getNoriginalcurtaxprice().multiply(bodys[k].getNnumber()));
          }
          bodys[k].setCt_manageid(row.getString(25));

          bodys[k].setNsubsummny(bodys[k].getNoriginalcursummny());
          bodys[k].setNsubcursummny(bodys[k].getNsummny());
        }
      }
    }
  }

  private UFDouble[] getExchangerate(String currid, String billdate)
  {
    UFDouble[] retdb = new UFDouble[2];
    try {
      if ((this.BD302 == null) || (!this.BD302.booleanValue()))
      {
        try {
          retdb[0] = this.currtype.getRate(currid, null, billdate);
        }
        catch (Exception e) {
          SCMEnv.out(e.getMessage());
        }
      }
      else {
        UFDouble dCurr0 = this.currtype.getRate(this.currtype.getFracCurrPK(), this.currtype.getLocalCurrPK(), billdate);
        UFDouble dCurr1 = this.currtype.getRate(currid, this.currtype.getFracCurrPK(), billdate);

        retdb[0] = dCurr0;
        retdb[1] = (dCurr1 == null ? new UFDouble(0) : dCurr1);

        if (currid.equals(this.currtype.getLocalCurrPK())) {
          retdb[0] = new UFDouble(1);
          retdb[1] = new UFDouble(0);
        }
      }
    }
    catch (Exception exp) {
      exp.printStackTrace();
      throw new BusinessRuntimeException(exp.getMessage());
    }
    return retdb;
  }
}