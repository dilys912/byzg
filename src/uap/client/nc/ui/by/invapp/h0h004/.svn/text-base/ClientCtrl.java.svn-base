package nc.ui.by.invapp.h0h004;
import nc.ui.by.invapp.button.IBYButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.by.invapp.h0h004.IchandnumVO;
import nc.vo.by.invapp.pub.IBYBillType;
import nc.vo.trade.pub.HYBillVO;

public class ClientCtrl implements ICardController,ISingleController{

	ClientEnvironment ce = ClientEnvironment.getInstance();

	public ClientCtrl() {
		super();
	}
	
    public String getBillType(){
	   return IBYBillType.TA04; 
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				IchandnumVO.class.getName(),
				IchandnumVO.class.getName(),
		};
	}

	

	public int getBusinessActionType() {

		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return null;
	}
	
	public String getPkField() {
		return "pk_invcontrastdoc";
	}
	
	public boolean isSingleDetail() {
		return true;
	}

	public int[] getCardButtonAry() {
		return new int[]{
				IBYButton.BTN_KCDR,
				IBYButton.BTN_KCJY,
				IBYButton.BTN_GLDR,
				IBYButton.BTN_GLJY
			};
	}

	public String[] getCardBodyHideCol() {
		return null;
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}


	public String getBodyZYXKey() {
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return false;
	}

	public boolean isLoadCardFormula() {
		return true;
	}

	public String getBodyCondition() {
		return null;
	}

}
