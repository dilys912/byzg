package nc.ui.baofc.payment;

import java.util.Calendar;
import java.util.Set;
import nc.bs.consts.SettleDCConsts;
import nc.bs.core.util.CalendarUtil;
import nc.itf.baofc.sv.Pay2FCSv;
import nc.ui.ap.m20080301.Ap20080306;
import nc.ui.arap.pubdj.ArapDjBillCardPanel;
import nc.ui.arap.pubdj.ArapDjPanel;
import nc.ui.baofc.pub.SvLocator;
import nc.ui.ep.dj.ARAPDjDataBuffer;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillData;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class BaoAp20080306 extends Ap20080306
{
  private static final long serialVersionUID = 7802040608978532669L;
  private Pay2FCSv sv = SvLocator.getPay2FCSv();

  public void onButtonClicked(ButtonObject bo)
  {
    getClientEnvironment(); UFDateTime serverTime = ClientEnvironment.getServerTime();
    UFDate businessDate = getClientEnvironment().getBusinessDate();
    Calendar serverCalendar = CalendarUtil.parse(serverTime.toString(), "yyyy-MM-dd");
    Calendar businessCalendar = CalendarUtil.parse(businessDate.toString(), "yyyy-MM-dd");
    String lxbm = getDjDataBuffer().getCurrentDjlxbm();
    if ((lxbm != null) && (lxbm.equals("D3")))
    {
      if (bo == this.m_boDelDj) {
        if (getDjDataBuffer().getCurrentDJZBVO() != null) {
          DJZBHeaderVO hvo = getDjDataBuffer().getCurrentDJZBVO().header;
          boolean canDel = true;
          if ((hvo.getZyx13() != null) && (hvo.getZyx13().equals("Y"))) {
            canDel = false;
          }
          if (!canDel) {
            showWarningMessage("单据已被拒单，不可执行删除操作！");
            return;
          }
          canDel = this.sv.canDeleteOrDeauditBecauseNotProtest(hvo.getPrimaryKey());
          if (!canDel) {
            showWarningMessage("单据已被拒单，不可执行删除操作！");
            return;
          }
        }

        super.onButtonClicked(bo);
      } else if (bo == this.m_boEdit) {
        if (getDjDataBuffer().getCurrentDJZBVO() != null) {
          DJZBHeaderVO hvo = getDjDataBuffer().getCurrentDJZBVO().header;
          boolean canEdit = true;
          if ((hvo.getZyx13() != null) && (hvo.getZyx13().equals("Y"))) {
            canEdit = false;
          }
          if (!canEdit) {
            showWarningMessage("单据已被拒单，不可执行修改操作！");
            return;
          }
          canEdit = this.sv.canDeleteOrDeauditBecauseNotProtest(hvo.getPrimaryKey());
          if (!canEdit) {
            showWarningMessage("单据已被拒单，不可执行修改操作！");
            return;
          }
        }

        super.onButtonClicked(bo);
      } else if (bo == this.m_boSave)
      {
        DJZBVO djVo = (DJZBVO)getArapDjPanel1().getBillCardPanelDj().getBillData()
          .getBillValueVO(DJZBVO.class.getName(), DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName());

        DJZBItemVO[] itemVos = djVo.items;
        for (int i = 0; i < itemVos.length; i++) {
          String jsfs = itemVos[i].getPj_jsfs();
          String szxm = itemVos[i].getSzxmid();

          if ((jsfs == null) || (!SettleDCConsts.SET_SETTLE_TYPE_OF_NOT_NULL_COSTSUBJ.contains(jsfs)) || (
            (szxm != null) && (szxm.trim().length() != 0))) continue;
          showErrorMessage("当[结算方式]不为“银行承兑汇票”或“商业承兑汇票”时，[收支项目]不可为空！");
          return;
        }

        super.onButtonClicked(bo);
      } else {
        super.onButtonClicked(bo);
      }
    }
    else super.onButtonClicked(bo);
  }
}