package nc.vo.by.invapp.pub;

import nc.ui.trade.button.IBillButton;


/**
 * ˵��: ״̬��,�жϵ���״̬,�Զ����½��水ť״̬
 */
public class StateMachine {
	public StateMachine() {
		super();
	}

    /**
     * �����ṩ�Ķ����͵��ݵ�״̬���ж϶����Ƿ����ִ��
     * @param voCurState BgtStateVO
     * @param iRoleType int
     * @param btn String
     * @return boolean
     */
    public boolean isVaidAction(StateVO voState, int btn) {
        // ����ͨ����ť���ύ̬������̬ʱ�ɱ༭
        // �޸ģ�����̬ʱ�ɱ༭
        if(btn==IBillButton.Edit&&(voState.getBillStatus() == IBYBillStatus.FREE ||voState.getBillStatus() == IBYBillStatus.NOPASS)){
        	return true;}
        // ��ӡ������̬��δ��ˡ����̬ʱ�ɱ༭
        if(btn==IBillButton.Print&&(voState.getBillStatus() == IBYBillStatus.FREE ||voState.getBillStatus() == IBYBillStatus.NOPASS||voState.getBillStatus() == IBYBillStatus.CHECKPASS)){
        	return true;
        }// ɾ��������̬ʱ�ɱ༭
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