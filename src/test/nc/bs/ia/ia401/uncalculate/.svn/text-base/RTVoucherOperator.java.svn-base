package nc.bs.ia.ia401.uncalculate;

import java.util.HashMap;
import nc.bs.ia.ia306.command.voucher.CostDeleteCommand;
import nc.bs.ia.ia401.IProcess;
import nc.bs.ia.pub.DataAccessUtils;
import nc.bs.ia.pub.IAContext;
import nc.vo.ia.ia306.IARtvoucherVO;
import nc.vo.ia.pub.IRowSet;
import nc.vo.ia.pub.SqlBuilder;

public class RTVoucherOperator
  implements IProcess
{
  private IAContext context = null;
  private String temptable = null;

  private boolean isPlanedInventory = false;

  public RTVoucherOperator(IAContext context, String temptable, boolean isPlanedInventory)
  {
    this.context = context;
    this.temptable = temptable;
    this.isPlanedInventory = isPlanedInventory;
  }

  public void process() {
    IRowSet rowset = getBillIItemDs();
    if (rowset.size() == 0) {
      return;
    }
    process(rowset);
  }

  public IRowSet getBillIItemDs() {
    SqlBuilder sql = new SqlBuilder();

    sql.append(" select cbill_bid from v_ia_inoutledger v ,");
    sql.append(this.temptable);
    sql.append(" e  where ");
    sql.append(" v.pk_corp", this.context.getCorp());
    sql.append(" and v.dauditdate ", ">=", this.context.getBeginDate());
    sql.append(" and v.dauditdate ", "<=", this.context.getEndDate());
    sql.append(" and v.caccountyear", this.context.getAuditYear());
    sql.append(" and v.caccountmonth", this.context.getAuditMonth());
    sql.append(" and v.crdcenterid = e.crdcenterid ");
    sql.append(" and v.cinventoryid = e.cinventoryid");

    sql.append(" and v.fdispatchflag", 1);
    sql.append(" and v.fdatagetmodelflag", 7);
    sql.append(" and brtvouchflag", "Y");

    if (this.isPlanedInventory) {
      String[] cbilltypecodes = new String[2];
      cbilltypecodes[0] = "IE";
      cbilltypecodes[1] = "IA";

      sql.append(" and v.cbilltypecode", cbilltypecodes);
    }

    IRowSet rowset = DataAccessUtils.query(sql.toString());
    return rowset;
  }

  private void process(IRowSet rowset) {
    int size = rowset.size();
    String[] cbill_bids = new String[size];
    int i = 0;
    while (rowset.next()) {
      cbill_bids[i] = rowset.getString(0);
      i++;
    }

    IARtvoucherVO vo = new IARtvoucherVO();

    vo.setCorpID(this.context.getCorp());
    vo.setLoginDate(this.context.getAuditDate());
    vo.setUserID(this.context.getAuditUser());
    vo.setLoginYear(this.context.getAuditYear());
    vo.setLoginMonth(this.context.getAuditMonth());
    vo.setBillItemIDs(cbill_bids);

    vo.setMethodId(13);
    CostDeleteCommand command = new CostDeleteCommand(false);
    HashMap data = new HashMap();
    data.put("para", vo);
    command.execute(data);
  }
}