package nc.ui.by.invapp.h0h001;

import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.by.invapp.h0h001.INVCLATTRIBUTEVO01;
import nc.vo.by.invapp.pub.IBYBillType;
import nc.vo.trade.pub.HYBillVO;

public class TreeCardController implements ITreeCardController,ISingleController{

	public boolean isAutoManageTree() {
		return true;
	}

	public boolean isChildTree() {
		return false;
	}

	public boolean isTableTree() {
		return false;
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBillButton.Edit,
				IBillButton.Cancel,
				IBillButton.Save,
				IBillButton.Refresh
		};
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
 		return IBYBillType.TA01; 
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				INVCLATTRIBUTEVO01.class.getName(),
				INVCLATTRIBUTEVO01.class.getName()
		};
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return 1;
	}

	public String getChildPkField() {
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "pk_invclattribute";
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
		return false;
	}

}
