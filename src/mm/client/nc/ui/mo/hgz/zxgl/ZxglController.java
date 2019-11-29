package nc.ui.mo.hgz.zxgl;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.vo.bgzg.pub.IBgzgButton;
import nc.vo.mm.hgz.button.IBHgzZgButton;
import nc.vo.mo.hgz.HgzHeadVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 在线隔离=合格证打印（制盖）界面控制
 * @author 刘信彬
 *
 */
public class ZxglController extends AbstractManageController implements ISingleController{

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getCardButtonAry() {
		// TODO Auto-generated method stub
		return new int[]{
//				IBillButton.Brow,
				IBillButton.Query,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Print,
				IBillButton.Save,
				IBillButton.Delete,
				IBillButton.Cancel,
				//返回按钮
				IBillButton.Return,
				IBillButton.Refresh,
				
				IBHgzZgButton.sctm,//生成条码
//				IBHgzButton.scxxjyd,//生成下线检验单
		};
	}

	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getBillType() {
		// TODO Auto-generated method stub
		return "ZXGL";
	}

	public String[] getBillVoName() {
		// TODO Auto-generated method stub
		return new String[]{
				HYBillVO.class.getName(),
				HgzHeadVO.class.getName(),
				HgzHeadVO.class.getName()
		};
	}

	public String getBodyCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBodyZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBusinessActionType() {
		// TODO Auto-generated method stub
		return 1;
	}

	public String getChildPkField() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_hgz";
	}

	public Boolean isEditInGoing() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return false;
	}

	public String[] getListBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getListButtonAry() {
		// TODO Auto-generated method stub
		 return new int[]{
				IBillButton.Query,
				IBillButton.Add,
//				IBillButton.Edit,
				IBillButton.Print,
				IBillButton.Save,
//				IBillButton.Delete,
				IBillButton.Cancel,
				//卡片按钮
				IBillButton.Card,
				IBillButton.Refresh,
				
//				IBHgzZgButton.sctm,//生成条码
				IBHgzZgButton.scxxjyd,//生成下线检验单
//				IBHgzButton.print,//打印
		};
	}

	public String[] getListHeadHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShowListRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isShowListTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSingleDetail() {
		// TODO Auto-generated method stub
		return false;
	}

}
