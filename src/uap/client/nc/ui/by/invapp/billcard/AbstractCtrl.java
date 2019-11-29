
package nc.ui.by.invapp.billcard;

import nc.ui.by.invapp.button.ButtonTool;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.by.invapp.pub.IBYConst;

/**
 * ˵��:����������,��Ƭ�ͽ���Ӧ�̳д���
 */
public class AbstractCtrl implements ICardController, ISingleController {

	public AbstractCtrl() {
		super();
	}
	public String[] getCardBodyHideCol() {
		return null;
	}
	public int[] getCardButtonAry() {
		return ButtonTool.deleteButton(IBillButton.Query, IBYConst.LIST_BUTTONS);
	}
	public boolean isShowCardRowNo() {
		return true;
	}
	public boolean isShowCardTotal() {
		return false;
	}
	public String getBillType() {
		return null;
	}
	public String[] getBillVoName() {
		return new String[]{};
	}
	public String getBodyCondition() {
		return null;
	}
	public String getBodyZYXKey() {
		return null;
	}
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
	public String getChildPkField() {
		return null;
	}
	public String getHeadZYXKey() {
		return null;
	}
	public String getPkField() {
		return null;
	}
	public Boolean isEditInGoing() throws Exception {
		return null;
	}
	public boolean isExistBillStatus() {
		return false;
	}
	public boolean isLoadCardFormula() {
		return false;
	}
	public boolean isSingleDetail() {
		return true;
	}

}
