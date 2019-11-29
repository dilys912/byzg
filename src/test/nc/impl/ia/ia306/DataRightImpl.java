package nc.impl.ia.ia306;

import nc.bs.ia.ia306.COperate;
import nc.bs.ia.ia306.DataRightDMO;
import nc.bs.ia.ia402.PeriodController;
import nc.bs.ia.pub.Transaction;
import nc.bs.ml.NCLangResOnserver;
import nc.impl.ia.pub.CommonDataImpl;
import nc.itf.ia.ia306.IDataRight;
import nc.vo.ia.ia306.AccountCheckVO;
import nc.vo.ia.ia306.ParamVO308;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.Log;
import nc.vo.pub.BusinessException;

public class DataRightImpl
  implements IDataRight
{
  public ParamVO308 pPortal(ParamVO308 p)
    throws BusinessException
  {
    ParamVO308 vo = new ParamVO308();
    try {
      COperate c = new COperate();
      if (p.getMethodID() == 0) {
        AccountCheckVO[] vos = c.checkNab(p);
        vo.setErrorAccounts(vos);
      }
      else
      {
        c.modifyNabPrecision(p);

        c.modifyIAGL(p);

        c.modifyIAPF(p);
      }

      c.modifyPriceModeFlag(p);
    } catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    return vo;
  }

  public void hisModify(ParamVO308 p) throws BusinessException {
    try {
      CommonDataImpl cbo = new CommonDataImpl();
      DataRightDMO dmo = new DataRightDMO();
      String[][] temp = (String[][])null;

      String startPeriod = cbo.getStartPeriod(p.getPkCorp());
      String unclosedPeriod = cbo.getUnClosePeriod(p.getPkCorp());
      StringBuffer sb = new StringBuffer(unclosedPeriod);

      if (!(sb.toString().equals("00"))) {
        unclosedPeriod = sb.deleteCharAt(4).toString();
      }
      else {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("20141060", "UPP20141060-000008"));
      }

      String period = p.getPeriod();
      if (period.compareTo(startPeriod) <= 0) {
        period = startPeriod;
      }

      String[] periods = dmo.getPeriods(p.getPkCorp(), period);

      Transaction transaction = new Transaction();

      for (int i = 0; i < periods.length; ++i) {
        Class[] para = new Class[2];
        para[0] = String.class;
        para[1] = String.class;

        String[] value = new String[2];
        value[0] = p.getPkCorp();
        value[1] = periods[i];

        transaction.newTransaction(DataRightImpl.class.getName(), "modifyAccountDataInMonth", para, value);
      }

      Integer[] temps = cbo.getDataPrecision(p.getPkCorp());
      int pricePrecision = temps[1].intValue();

      dmo.modifyPrecision(p);

      dmo.modifyVbatchNull(p.getPkCorp(), period);

      dmo.modifyIAIC(p.getPkCorp(), period);

      if (p.getPeriod().compareTo(startPeriod) <= 0) {
        String[][] data = dmo.getQiChuSumData(p.getPkCorp(), startPeriod);
        if ((data != null) && (data.length > 0)) {
          String tabname = dmo.getTempTable(data);
          String idTab = dmo.getIDTempTable(data);
          Log.info("数据修正：修正缺少期初结存开始 ");
          if (dmo.checkNoQiChuNab(tabname, p.getPkCorp())) {
            dmo.insertNoQiChuNab(tabname, idTab);
          }
          Log.info("数据修正：修正缺少期初结存结束 ");

          Log.info("数据修正：修正期初结存开始 ");
          dmo.qiChuNabUpdate(tabname, pricePrecision);
          Log.info("数据修正：修正期初结存结束 ");
        }
      }

      String tabname = null;

      Log.info("数据修正：修正上年结转开始 ");
      sb = new StringBuffer("");
      String year = period.substring(0, 4);
      sb.append("select distinct caccountyear from ia_monthledger ");
      sb.append("where frecordtypeflag = 5 and caccountyear >= '" + year + "'");
      sb.append(" and pk_corp='" + p.getPkCorp() + "' ");
      String[][] re = cbo.queryData(sb.toString());
      if ((re != null) && (re.length > 0)) {
        for (int i = 0; i < re.length; ++i) {
          String sLastYearMonth = cbo.getLastMonthInYear(p.getPkCorp(), re[i][0]);

          temp = dmo.getLastYearSum(p.getPkCorp(), re[i][0], sLastYearMonth);
          if ((temp != null) && (temp.length > 0)) {
            tabname = dmo.getTempTable(temp);
            dmo.lastYearUpdate(tabname, pricePrecision, p.getPkCorp(), re[i][0] + sLastYearMonth);
          }

        }

      }

      Log.info("数据修正：修正检查上年结转结束 ");

      temp = dmo.getMonthSumData(p.getPkCorp(), period);

      if ((temp == null) || (temp.length == 0)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("20141060", "UPP20141060-000009"));
      }

      dmo.modifyOnlyQC(p.getPkCorp());

      tabname = dmo.getTempTable(temp);
      String idTab = dmo.getIDTempTable(temp);

      Log.info("数据修正：修正没有本月发生 但存货总帐有本月合计开始 ");
      if ((idTab != null) && (idTab.trim().length() != 0)) {
        dmo.monthDYUpdate(idTab, p.getPkCorp(), period);
      }
      Log.info("数据修正：修正没有本月发生 但存货总帐有本月合计结束 ");

      Log.info("数据修正：修正缺少本月合计 开始 ");
      if (dmo.checkNoMonthTotal(tabname, p.getPkCorp(), startPeriod, unclosedPeriod))
      {
        dmo.insertMonthTotal(tabname, idTab, startPeriod, unclosedPeriod);
      }
      Log.info("数据修正：修正缺少本月合计 结束 ");

      Log.info("数据修正：修正缺少本年累计 开始 ");
      dmo.insertYearTotal(tabname, idTab, startPeriod, unclosedPeriod);
      Log.info("数据修正：修正缺少本年累计 结束 ");

      Log.info("数据修正：修正本月合计 开始 ");
      dmo.monthUpdate(tabname, pricePrecision);
      Log.info("数据修正：修正本月合计 结束 ");

      Log.info("数据修正：修正本年累计 开始 ");
      for (int i = 0; i < periods.length; ++i)
      {
        temp = dmo.getYearSumData(p.getPkCorp(), periods[i], startPeriod);

        if ((temp != null) && (temp.length > 0)) {
          tabname = dmo.getTempTable(temp);
          Log.info("数据修正：修正没有本年发生 但存货总帐有本年累计开始 +periods[i] ");
          dmo.yearDYUpdate(tabname, periods[i], p.getPkCorp());
          Log.info("数据修正：修正没有本年发生 但存货总帐有本年累计结束 +periods[i]");

          Log.info("数据修正：修正本年累计开始 " + periods[i]);
          dmo.yearUpdate(tabname, pricePrecision);
          Log.info("数据修正：修正本年累计结束 " + periods[i]);
        }

      }

      Log.info("数据修正：修正本年累计结束 ");
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    finally {
    }
  }

  public void modifyAccountDataInMonth(String pk_corp, String period) throws BusinessException {
    CommonDataImpl cbo = new CommonDataImpl();

    String startPeriod = cbo.getStartPeriod(pk_corp);

    period = period.substring(0, 4) + "-" + period.substring(4);

    if (startPeriod.equalsIgnoreCase(period)) {
      PeriodController.redoBeginAccount(pk_corp, period.substring(0, 4));
      PeriodController.doBeginAccount(pk_corp, period.substring(0, 4));
    }

    PeriodController.redoPeriodAccount(pk_corp, period);
    PeriodController.doPeriodAccount(pk_corp, period);

    if (PeriodController.IsPeriodAB(pk_corp, period)) {
      PeriodController.redoABAccount(pk_corp, period);
      PeriodController.doABAccount(pk_corp, period);
    }
  }
}