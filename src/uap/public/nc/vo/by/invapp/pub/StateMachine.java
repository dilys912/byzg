package nc.vo.by.invapp.pub;

import nc.ui.trade.button.IBillButton;


/**
 * 说明: 状态机,判断单据状态,自动更新界面按钮状态
 */
public class StateMachine {
	public StateMachine() {
		super();
	}

    /**
     * 根据提供的动作和单据的状态，判断动作是否可以执行
     * @param voCurState BgtStateVO
     * @param iRoleType int
     * @param btn String
     * @return boolean
     */
    public boolean isVaidAction(StateVO voState, int btn) {
        // 评审通过按钮，提交态、自由态时可编辑
        // 修改，自由态时可编辑
        if(btn==IBillButton.Edit&&(voState.getBillStatus() == IBYBillStatus.FREE ||voState.getBillStatus() == IBYBillStatus.NOPASS)){
        	return true;}
        // 打印，自由态、未审核、审核态时可编辑
        if(btn==IBillButton.Print&&(voState.getBillStatus() == IBYBillStatus.FREE ||voState.getBillStatus() == IBYBillStatus.NOPASS||voState.getBillStatus() == IBYBillStatus.CHECKPASS)){
        	return true;
        }// 删除，自由态时可编辑
        else if(btn==IBillButton.Delete&&(voState.getBillStatus() == IBYBillStatus.FREE ||voState.getBillStatus() == IBYBillStatus.NOPASS)){
        	return true;
        }else if(btn == IBillButton.Copy&& (voState.getBillStatus() == IBYBillStatus.FREE ||voState.getBillStatus() == IBYBillStatus.NOPASS||voState.getBillStatus() == IBYBillStatus.CHECKPASS)){
			return true;
		}
        //shenhe
        else if(btn == IBillButton.Audit&& (voState.getBillStatus() == 3)){
			return true;
		}
        //qishenhe
        else if(btn == IBillButton.CancelAudit&& (voState.getBillStatus() == 1)){
			return true;
		}
        //qishenhe
        else if(btn == IBillButton.Commit&& (voState.getBillStatus() == 8)){
			return true;
		}
		return false;
	}

}