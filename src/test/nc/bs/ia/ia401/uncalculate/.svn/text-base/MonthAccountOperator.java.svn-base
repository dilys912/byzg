package nc.bs.ia.ia401.uncalculate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.ia.ia401.IProcess;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ia.pub.IAContext;
import nc.bs.ia.pub.IATool;
import nc.bs.ia.pub.TempTableDefine;
import nc.vo.ia.ia504.GeneralledgerVO;
import nc.vo.ia.pub.IRowSet;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ia.pub.SqlConstant;
import nc.vo.ia.pub.TimeLog;
import nc.vo.pub.lang.UFDouble;

public class MonthAccountOperator
  implements IProcess
{
  private IAContext context = null;
  private String temptable = null;

  public MonthAccountOperator(IAContext context, String temptable) {
    this.context = context;
    this.temptable = temptable;
  }

  public void process() {
    TimeLog.logStart();
    cancelGeranerlAccount();
    TimeLog.info("取消存货总帐");

    TimeLog.logStart();
    cancelDetailAccount();
    TimeLog.info("取消存货明细账");

    TimeLog.logStart();
    cancelFQSK_WTDXDetailAccount();
    TimeLog.info("取消商品明细账、委托代销明细账");
  }

  private void cancelGeranerlAccount()
  {
    IRowSet rowset = groupbyDetailAccount();

    Map index = getGerneralAccount(this.temptable);
    calculateGeneralAccount(index, rowset);

    int size = index.size();
    GeneralledgerVO[] vos = new GeneralledgerVO[size];
    vos = (GeneralledgerVO[])(GeneralledgerVO[])index.values().toArray(vos);
    saveGeneralAccountToDB(vos);
  }

  private IRowSet groupbyDetailAccount()
  {
    IRowSet rowset = null;
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select c.rdcenter,c.inventory,sum(c.nmoney),");
    sql.append(" sum(c.nsendmny),sum(c.nsendmny1),");
    sql.append("sum(c.nnotsendmoney),sum(c.nnotsendmoney1) ");
    sql.append(" from ");
    sql.startParentheses();

    sql.append(constructGroupDetailAccount());

    sql.append(" union all ");
    sql.startParentheses();
    sql.append(constuctWTDXDetailAccount());
    sql.endParentheses();

    sql.append(" union all ");
    sql.startParentheses();
    sql.append(constuctFQSKDetailAccount());
    sql.endParentheses();

    sql.endParentheses();
    sql.append(" c ");
    sql.append(" group by c.rdcenter,c.inventory ");
    rowset = DataAccessUtils.query(sql.toString());

    return rowset;
  }

  private String constructGroupDetailAccount() {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select a.crdcenterid ");
    sql.append(" rdcenter,a.cinventoryid inventory ,a.nmoney nmoney,");
    sql.append("0 nsendmny,0 nsendmny1,0 nnotsendmoney,0 nnotsendmoney1 ");
    sql.append(" from ");
    sql.append(this.temptable);
    sql.append(" c,v_ia_inoutledger a  where ");
    sql.append(" a.pk_corp", this.context.getCorp());
    sql.append(" and a.fdispatchflag", 1);

    sql.append(" and a.cbilltypecode ", "!= ", "IH");

    sql.append(" and a.caccountyear ", this.context.getAuditYear());
    sql.append(" and a.caccountmonth", this.context.getAuditMonth());
    sql.append(" and a.dauditdate ", ">=", this.context.getBeginDate());
    sql.append(" and a.dauditdate", "<=", this.context.getEndDate());

    sql.append(" and a.crdcenterid = c.crdcenterid ");
    sql.append(" and a.cinventoryid = c.cinventoryid ");

    sql.append(" and ");
    sql.append(SqlConstant.getInstance().notWTDX_FQSK_FAPIAO("a", "a", this.context.getFqsk(), this.context.getWtdx()));

    sql.append(" and ");
    sql.startParentheses();
    sql.append(" a.fdatagetmodelflag", 7);
    sql.append(" or ");
    sql.append(" a.fdatagetmodelflag", 12);
    sql.endParentheses();

    return sql.toString();
  }

  private String constuctWTDXDetailAccount() {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select  a.crdcenterid rdcenter,a.cinventoryid inventory, ");
    sql.append(" 0 nmoney, coalesce(ndebitmny,0) nsendmny, ");
    sql.append(" coalesce(ncreditmny,0) nsendmny1,0 nnotsendmoney, ");
    sql.append(" 0 nnotsendmoney1 from ");
    sql.append(this.temptable);
    sql.append(" c,ia_goodsledger a where ");
    sql.append(" a.pk_corp", this.context.getCorp());
    sql.append(" and a.caccountyear ", this.context.getAuditYear());
    sql.append(" and a.caccountmonth", this.context.getAuditMonth());
    sql.append(" and a.bissend = 'Y' ");
    sql.append(" and a.crdcenterid = c.crdcenterid ");
    sql.append(" and a.cinventoryid = c.cinventoryid ");

    sql.append(" and ");
    sql.startParentheses();
    sql.append(" a.fdatagetmodelflag", 7);
    sql.append(" or ");
    sql.append(" a.fdatagetmodelflag", 12);
    sql.endParentheses();

    return sql.toString();
  }

  private String constuctFQSKDetailAccount() {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select a.crdcenterid rdcenter,a.cinventoryid inventory,");
    sql.append(" 0 nmoney,0 nsendmny,0 nsendmny1, ");
    sql.append(" coalesce(ndebitmny,0) nnotsendmoney, ");
    sql.append(" coalesce(ncreditmny,0) nnotsendmoney1");
    sql.append(" from ");
    sql.append(this.temptable);
    sql.append(" c,ia_goodsledger a where ");
    sql.append(" a.pk_corp", this.context.getCorp());
    sql.append(" and a.caccountyear ", this.context.getAuditYear());
    sql.append(" and a.caccountmonth", this.context.getAuditMonth());
    sql.append(" and a.bissend = 'N' ");
    sql.append(" and a.crdcenterid = c.crdcenterid ");
    sql.append(" and a.cinventoryid = c.cinventoryid ");

    sql.append(" and ");
    sql.startParentheses();
    sql.append(" a.fdatagetmodelflag", 7);
    sql.append(" or ");
    sql.append(" a.fdatagetmodelflag", 12);
    sql.endParentheses();

    return sql.toString();
  }

  private Map getGerneralAccount(String temptable)
  {
    Map index = new HashMap();
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select cgeneralledgerid, noutmny, nabmny, nabnum, ");
    sql.append(" nemitdebitmny, nemitcreditmny, nemitabmny, nemitabnum, ");
    sql.append(" nentrustdebitmny, nentrustcreditmny, nentrustabmny, ");
    sql.append(" nentrustabnum ,a.crdcenterid, a.cinventoryid ");
    sql.append(" from ia_generalledger a ,");
    sql.append(temptable);
    sql.append(" b where  ");
    sql.append(" a.pk_corp", this.context.getCorp());
    sql.append(" and a.crdcenterid = b.crdcenterid ");
    sql.append(" and a.cinventoryid = b.cinventoryid ");
    sql.append(" and a.frecordtypeflag = 4 ");
    sql.append(" and a.dr = 0 ");

    IRowSet rowset = DataAccessUtils.query(sql.toString());
    while (rowset.next()) {
      GeneralledgerVO vo = new GeneralledgerVO();
      vo.setCgeneralledgerid(rowset.getString(0));
      vo.setNoutmny(rowset.getUFDouble(1));
      vo.setNabmny(rowset.getUFDouble(2));
      vo.setNabnum(rowset.getUFDouble(3));

      vo.setNemitdebitmny(rowset.getUFDouble(4));
      vo.setNemitcreditmny(rowset.getUFDouble(5));
      vo.setNemitabmny(rowset.getUFDouble(6));
      vo.setNemitabnum(rowset.getUFDouble(7));

      vo.setNentrustdebitmny(rowset.getUFDouble(8));
      vo.setNentrustcreditmny(rowset.getUFDouble(9));
      vo.setNentrustabmny(rowset.getUFDouble(10));
      vo.setNentrustabnum(rowset.getUFDouble(11));

      vo.setCrdcenterid(rowset.getString(12));
      vo.setCinventoryid(rowset.getString(13));

      if ((vo.getNabnum() == null) || (vo.getNabnum().doubleValue() == 0.0D)) {
        vo.setNabprice(null);
      }
      else {
        UFDouble price = vo.getNabmny().div(vo.getNabnum());
        price = this.context.adjustPrice(price);
        vo.setNabprice(price);
      }

      if ((vo.getNemitabnum() == null) || (vo.getNemitabnum().doubleValue() == 0.0D)) {
        vo.setNemitprice(null);
      }
      else {
        UFDouble price = vo.getNemitabmny().div(vo.getNemitabnum());
        price = this.context.adjustPrice(price);
        vo.setNemitprice(price);
      }
      if ((vo.getNentrustabnum() == null) || (vo.getNentrustabnum().doubleValue() == 0.0D)) {
        vo.setNentrustprice(null);
      }
      else {
        UFDouble price = vo.getNentrustabmny().div(vo.getNentrustabnum());
        price = this.context.adjustPrice(price);
        vo.setNentrustprice(price);
      }
      index.put(vo.getCrdcenterid() + vo.getCinventoryid(), vo);
    }

    return index;
  }

  private void calculateGeneralAccount(Map index, IRowSet rowset)
  {
    while (rowset.next()) {
      String crdcenterid = rowset.getString(0);
      String cinventoryid = rowset.getString(1);
      UFDouble noutmny = rowset.getUFDouble(2);

      UFDouble nemitdebitmny = rowset.getUFDouble(3);
      UFDouble nemitcreditmny = rowset.getUFDouble(4);
      UFDouble nemitabmny = IATool.getInstance().sub(nemitdebitmny, nemitcreditmny);

      UFDouble nentrustdebitmny = rowset.getUFDouble(5);
      UFDouble nentrustcreditmny = rowset.getUFDouble(6);
      UFDouble nentrustabmny = IATool.getInstance().sub(nentrustdebitmny, nentrustcreditmny);

      GeneralledgerVO vo = (GeneralledgerVO)index.get(crdcenterid + cinventoryid);

      vo.setNoutmny(IATool.getInstance().sub(vo.getNoutmny(), noutmny));
      vo.setNabmny(IATool.getInstance().add(vo.getNabmny(), noutmny));
      if ((vo.getNabnum() == null) || (vo.getNabnum().doubleValue() == 0.0D)) {
        vo.setNabprice(null);
      }
      else {
        UFDouble price = vo.getNabmny().div(vo.getNabnum());
        price = this.context.adjustPrice(price);
        vo.setNabprice(price);
      }

      vo.setNemitdebitmny(IATool.getInstance().sub(vo.getNemitdebitmny(), nemitdebitmny));
      vo.setNemitcreditmny(IATool.getInstance().sub(vo.getNemitcreditmny(), nemitcreditmny));

      vo.setNemitabmny(IATool.getInstance().sub(vo.getNemitabmny(), nemitabmny));
      if ((vo.getNemitabnum() == null) || (vo.getNemitabnum().doubleValue() == 0.0D)) {
        vo.setNemitprice(null);
      }
      else {
        UFDouble price = vo.getNemitabmny().div(vo.getNemitabnum());
        price = this.context.adjustPrice(price);
        vo.setNemitprice(price);
      }

      vo.setNentrustdebitmny(IATool.getInstance().sub(vo.getNentrustdebitmny(), nentrustdebitmny));

      vo.setNentrustcreditmny(IATool.getInstance().sub(vo.getNentrustcreditmny(), nentrustcreditmny));

      vo.setNentrustabmny(IATool.getInstance().sub(vo.getNentrustabmny(), nentrustabmny));
      if ((vo.getNentrustabnum() == null) || (vo.getNentrustabnum().doubleValue() == 0.0D))
      {
        vo.setNentrustprice(null);
      }
      else {
        UFDouble price = vo.getNentrustabmny().div(vo.getNentrustabnum());
        price = this.context.adjustPrice(price);
        vo.setNentrustprice(price);
      }
    }
  }

  private void saveGeneralAccountToDB(GeneralledgerVO[] vos)
  {
    int length = vos.length;
    if (length == 0) {
      return;
    }

    SqlBuilder sql = new SqlBuilder();
    sql.append(" update ia_generalledger set ");
    sql.append(" nmonthprice = null, ");
    sql.append(" noutmny = ? , ");
    sql.append(" nabmny = ? , ");
    sql.append(" nabprice = ? , ");

    sql.append(" nemitdebitmny = ? , ");
    sql.append(" nemitcreditmny = ? , ");
    sql.append(" nemitabmny = ? , ");
    sql.append(" nemitprice = ? , ");

    sql.append(" nentrustdebitmny = ? , ");
    sql.append(" nentrustcreditmny = ? , ");
    sql.append(" nentrustabmny = ? , ");
    sql.append(" nentrustprice = ? ");

    sql.append(" where cgeneralledgerid=? ");

    String[] parameters = { "UFDouble", "UFDouble", "UFDouble", "UFDouble", "UFDouble", "UFDouble", "UFDouble", "UFDouble", "UFDouble", "UFDouble", "UFDouble", "String" };

    ArrayList data = new ArrayList();
    for (int i = 0; i < length; i++) {
      List row = new ArrayList();
      row.add(vos[i].getNoutmny());
      row.add(vos[i].getNabmny());
      row.add(vos[i].getNabprice());

      row.add(vos[i].getNemitdebitmny());
      row.add(vos[i].getNemitcreditmny());
      row.add(vos[i].getNemitabmny());
      row.add(vos[i].getNemitprice());

      row.add(vos[i].getNentrustdebitmny());
      row.add(vos[i].getNentrustcreditmny());
      row.add(vos[i].getNentrustabmny());
      row.add(vos[i].getNentrustprice());

      row.add(vos[i].getCgeneralledgerid());
      data.add(row);
    }
    DataAccessUtils.batchUpdate(sql.toString(), parameters, data);
  }

  private void cancelDetailAccount()
  {
    IRowSet rowset = getBillItemIDs();
    saveDetailAccountToDB(rowset);
  }

  private IRowSet getBillItemIDs() {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" select cbill_bid from v_ia_inoutledger v , ");
    sql.append(this.temptable);
    sql.append(" b where  ");
    sql.append(" v.pk_corp", this.context.getCorp());
    sql.append(" and v.fdispatchflag = 1 ");

    sql.append(" and v.cbilltypecode ", "!=", "IH");

    sql.append(" and v.caccountyear ", this.context.getAuditYear());
    sql.append(" and v.caccountmonth", this.context.getAuditMonth());
    sql.append(" and v.dauditdate ", ">=", this.context.getBeginDate());
    sql.append(" and v.dauditdate", "<=", this.context.getEndDate());

    sql.append(" and v.crdcenterid = b.crdcenterid ");
    sql.append(" and v.cinventoryid = b.cinventoryid ");

    sql.append(" and ");
    sql.startParentheses();
    sql.append(" v.fdatagetmodelflag", 7);
    sql.append(" or ");
    sql.append(" v.fdatagetmodelflag", 12);
    sql.endParentheses();

    sql.append(" and ");
    sql.startParentheses();
    sql.append(" v.cbilltypecode != 'IA' ");
    sql.append(" or v.fdatagetmodelflag ", "!= ", 7);
    sql.endParentheses();

    IRowSet rowset = DataAccessUtils.query(sql.toString());
    return rowset;
  }

  private void saveDetailAccountToDB(IRowSet rowset) {
    int size = rowset.size();
    if (size == 0) {
      return;
    }
    String[][] data = new String[size][2];
    int i = 0;
    while (rowset.next()) {
      data[i][0] = rowset.getString(0);
      i++;
    }
    TempTableDefine define = new TempTableDefine();
    String indexTable = define.getUpdateBillItemIndexTable(data);

    SqlBuilder sql = new SqlBuilder();
    sql.append(" update ia_bill_b set ");
    sql.append(" nmoney = null,");
    sql.append(" nprice = null,");
    sql.append(" ndrawsummny= null, ");
    sql.append(" fdatagetmodelflag", 5);
    sql.append(" from ");
    sql.append(indexTable);
    sql.append(" b where ia_bill_b.cbill_bid = b.cbill_bid ");

    DataAccessUtils.update(sql.toString());
  }

  private void cancelFQSK_WTDXDetailAccount() {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" update ia_goodsledger set ");
    sql.append(" ndebitprice = null,");
    sql.append(" ncreditprice = null,");
    sql.append(" ndebitmny = null,");
    sql.append(" fdatagetmodelflag ", 5);
    sql.append(" from ");
    sql.append(this.temptable);
    sql.append(" c where  ");
    sql.append(" ia_goodsledger.pk_corp", this.context.getCorp());
    sql.append(" and ia_goodsledger.caccountyear ", this.context.getAuditYear());
    sql.append(" and ia_goodsledger.caccountmonth", this.context.getAuditMonth());

    sql.append(" and ia_goodsledger.crdcenterid = c.crdcenterid ");
    sql.append(" and ia_goodsledger.cinventoryid = c.cinventoryid ");

    sql.append(" and ");
    sql.startParentheses();
    sql.append(" ia_goodsledger.fdatagetmodelflag", 7);
    sql.append(" or ");
    sql.append(" ia_goodsledger.fdatagetmodelflag", 12);
    sql.endParentheses();

    DataAccessUtils.update(sql.toString());
  }
}