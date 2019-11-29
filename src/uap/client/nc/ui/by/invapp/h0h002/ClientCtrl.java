package nc.ui.by.invapp.h0h002; 
  
import nc.ui.by.invapp.billmanage.AbstractCtrl;
import nc.ui.by.invapp.button.ButtonTool;
import nc.ui.by.invapp.button.IBYButton;
import nc.vo.by.invapp.pub.IBYBillType;
import nc.vo.by.invapp.pub.IBYConst;
  
/** 
 * 
 * */ 
public class ClientCtrl extends AbstractCtrl { 
  
 	public String getBillType() { 
 		return IBYBillType.TA02; 
 	} 
  
 	public String[] getBillVoName() { 
 		return new String[] {  
 				nc.vo.by.invapp.h0h002.MyExAggVO.class.getName(), 
 				nc.vo.by.invapp.h0h002.InvappdocVO.class.getName(), 
 				nc.vo.by.invapp.h0h002.ConvertVO.class.getName()
				}; 
 	} 
  
 	public int[] getCardButtonAry() { 
 		return	ButtonTool.insertButtons(new int[] {IBYButton.Commit,IBYButton.Audit,IBYButton.CancelAudit},
 				IBYConst.CARD_BUTTONS_M, IBYConst.CARD_BUTTONS_M.length-1); 
 	} 
  
 	public int[] getListButtonAry() { 
 		return IBYConst.LIST_BUTTONS_M; 
 	} 
  
 	public String getPkField() { 
 		return "pk_invappdoc"; 
 	} 
  
 	@Override 
 	public String getChildPkField() { 
 		return null; 
 	} 
 	public int getBusinessActionType() {
		// ��ƽ̨
		return nc.ui.trade.businessaction.IBusinessActionType.PLATFORM;
	}

	public Boolean isEditInGoing() throws Exception {
		// �ύ���Ƿ��ܹ��޸�
		return false;
	}

	public boolean isExistBillStatus() {
		// TODO �Ƿ�ʹ�õ���״̬������ť״̬
		return true;
	}
  
} 
