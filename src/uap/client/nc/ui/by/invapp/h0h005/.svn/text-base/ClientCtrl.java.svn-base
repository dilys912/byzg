package nc.ui.by.invapp.h0h005;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.by.invapp.h0h005.CargContrastdocVO;
import nc.vo.by.invapp.pub.IBYBillType;
import nc.vo.trade.pub.HYBillVO;

public class ClientCtrl implements ICardController,ISingleController{

	ClientEnvironment ce = ClientEnvironment.getInstance();

	public ClientCtrl() {
		super();
	}
	
    public String getBillType(){
	   return IBYBillType.TA05; 
	}

	public String[] getBillVoName() {
		return new String[]{
				HYBillVO.class.getName(),
				CargContrastdocVO.class.getName(),
				CargContrastdocVO.class.getName(),
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
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Delete,
				IBillButton.Cancel,
				IBillButton.Refresh,
				IBillButton.ImportBill
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
