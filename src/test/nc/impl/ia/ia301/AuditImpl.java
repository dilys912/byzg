package nc.impl.ia.ia301;

import java.util.HashMap;
import nc.bs.ia.ia301.IADBUtils;
import nc.bs.ia.ia301.Status;
import nc.bs.ia.ia301.command.BillAccount;
import nc.bs.ia.ia301.command.CostQuery;
import nc.bs.ia.ia301.command.IndividualAccount;
import nc.bs.ia.ia301.command.OtherTransactionBillAccount;
import nc.bs.ia.pub.CommonDataDMO;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ia.pub.IAContext;
import nc.bs.ia.pub.IATool;
import nc.bs.ia.pub.LockOperator;
import nc.bs.ia.pub.TempTableDefine;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.ia.ia301.IAudit;
import nc.itf.ia.ia301.IAuditQuery;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.ia301.AuditBillVO;
import nc.vo.ia.ia301.AuditResultVO;
import nc.vo.ia.ia301.ParamVO;
import nc.vo.ia.ia501.IaInoutledgerVO;
import nc.vo.ia.ia505.AssistantLedgerVO;
import nc.vo.ia.pub.ExceptionUtils;
import nc.vo.ia.pub.SqlBuilder;
import nc.vo.ia.pub.TimeLog;
import nc.vo.pub.BusinessException;

public class AuditImpl
  implements IAuditQuery, IAudit
{
  public HashMap getAuditBill(ParamVO voParam)
    throws BusinessException
  {
    HashMap data = new HashMap();
    try {
      if (voParam.bIfForHandInput) {
        AuditBillVO[] vos = IADBUtils.getBillItemsWithItemID(voParam.getBill_bids());

        data.put("handinput", vos);
      }
      else {
        String[] systemInfo = voParam.getSystemInfo();
        IAContext context = IAContext.create(systemInfo);
        CostQuery commond = new CostQuery(context);
        data = commond.query(voParam);
      }
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    return data;
  }

  public boolean individualBillAccount(String[] systemInfo, BillItemVO voBillItem, AssistantLedgerVO[] voaAssi)
    throws BusinessException
  {
    try
    {
      IAContext context = IAContext.create(systemInfo);
      IndividualAccount account = new IndividualAccount(context);
      account.calculate(voaAssi, voBillItem);
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    finally {
      TimeLog.infoAll();
    }
    return true;
  }

  public ParamVO pPortal(ParamVO voParam)
    throws BusinessException
  {
    String[] systemInfo = voParam.getSystemInfo();
    ParamVO vo = new ParamVO();
    try {
      String sign = Status.createKey(systemInfo[0], systemInfo[2]);
      String info = Status.getHero().getState(sign);
      vo.setState(info);
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    return vo;
  }

  public ParamVO audit(ParamVO voParam) throws BusinessException {
    String[] systemInfo = voParam.getSystemInfo();
    ParamVO vo = null;
    try {
      IAContext context = IAContext.create(systemInfo);
      context.setAutofillJHJForCCPRKD(voParam.isAutofillJHJForCCPRKD());
      OtherTransactionBillAccount action = new OtherTransactionBillAccount(context);

      AuditResultVO[] vos = action.calculate(voParam.getBill_bids());
      CommonDataDMO.objectReference(vos);
      vo = new ParamVO();
      vo.setResult(vos);
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    finally {
      TimeLog.infoAll();
    }
    return vo;
  }

  public boolean billaccountbyVO(String[] systemInfo, IaInoutledgerVO[] ledgerVOs, Integer begiAccoFlag)
    throws BusinessException
  {
    int qichuFlag = begiAccoFlag.intValue();
    if (qichuFlag == 0) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000047"));
    }

    if ((ledgerVOs == null) || (ledgerVOs.length == 0))
      return false;
    try
    {
      IAContext context = IAContext.create(systemInfo);
      BillAccount account = new BillAccount(context);
      account.calculate(ledgerVOs);
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    finally {
      TimeLog.infoAll();
    }
    return true;
  }

  public void remove(String corp, String user) throws BusinessException {
    Status.getHero().remove(corp + user);
  }

  public void savePrices(String[][] SavePrices, String userID) throws BusinessException
  {
    String[] itemids = new String[SavePrices.length];

    for (int i = 0; i < SavePrices.length; i++) {
      itemids[i] = SavePrices[i][0];
    }
    LockOperator lock = new LockOperator(userID);
    try
    {
      IATool.getInstance().lockBillItems(itemids, lock);
      TempTableDefine define = new TempTableDefine();

      String temptable = define.getUpdateBillPrices(SavePrices);

      SqlBuilder sql = new SqlBuilder();

      sql.append(" update ia_bill_b set ");
      sql.append(" ia_bill_b.nprice = b.nprice,");
      sql.append(" ia_bill_b.nmoney = b.nmoney,");
      sql.append(" ia_bill_b.fdatagetmodelflag", 1);
      sql.append("  from ");
      sql.append(temptable);
      sql.append(" b where ia_bill_b.cbill_bid= b.cbill_bid ");

      sql.append(" and ");
      sql.startParentheses();
      sql.append(" ia_bill_b.brtvouchflag", "N");
      sql.append(" or ia_bill_b.brtvouchflag is null ");
      sql.endParentheses();

      sql.append(" and ia_bill_b.iauditsequence ", -1);

      int count = DataAccessUtils.update(sql.toString());

      if (count != SavePrices.length) {
        String message = NCLangResOnserver.getInstance().getStrByID("20146010", "UPP20146010-000042");

        ExceptionUtils.wrappBusinessException(message);
      }
    }
    catch (Exception ex)
    {
      ExceptionUtils.marsh(ex);
    }
    finally {
      lock.unlock();
    }
  }
}