package nc.ui.mo.mo6600;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.businessaction.IBusinessActionType;

import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.ui.trade.button.IBillButton;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * Create on 2006-4-6 16:00:51
 * 
 * @author authorName
 * @version tempProject version
 */

public class mo6600Ctrl extends AbstractManageController
{

	public String[] getCardBodyHideCol()
	{
		return null;
	}

	public int[] getCardButtonAry()
	{

		return new int[] { IBillButton.Add, IBillButton.Query,
				IBillButton.Delete, IBillButton.Save, IBillButton.Edit,
				IButton.Confirm, IButton.Update_Num,IBillButton.Cancel,
				IBillButton.Return,IBillButton.Line, IButton.Print,
				/*
				 * 2019-07-31 刘信彬
				 * 下线
				 */
				IButton.Offline,
//				IButton.SORT_NO
				};

	}

	public int[] getListButtonAry()
	{
		return new int[] { IBillButton.Add, IBillButton.Query,
				IBillButton.Delete, IBillButton.Save, IBillButton.Edit,
				IButton.Confirm, IButton.Update_Num,IButton.Print,
				/*
				 * 2019-07-31 刘信彬
				 * 下线
				 */
				IButton.Offline,
//				IButton.SORT_NO 
				};

	}

	public boolean isShowCardRowNo()
	{
		return false;
	}

	public boolean isShowCardTotal()
	{
		return false;
	}

	public String getBillType()
	{
		return "JYDJ";
	}

	public String[] getBillVoName()
	{
		return new String[] { GlBillVO.class.getName(),
				GlHeadVO.class.getName(), GlItemBVO.class.getName() };
	}

	public String getBodyCondition()
	{
		return null;
	}

	public String getBodyZYXKey()
	{
		return null;
	}

	public int getBusinessActionType()
	{
		return IBusinessActionType.PLATFORM;
	}

	public String getChildPkField()
	{
		return null;
	}

	public String getHeadZYXKey()
	{
		return null;
	}

	public String getPkField()
	{
		return null;
	}

	public Boolean isEditInGoing() throws Exception
	{
		return null;
	}

	public boolean isExistBillStatus()
	{
		return true;
	}

	public boolean isLoadCardFormula()
	{
		return false;
	}

	public String[] getListBodyHideCol()
	{
		return null;
	}

	public String[] getListHeadHideCol()
	{
		return null;
	}

	public boolean isShowListRowNo()
	{
		return true;
	}

	public boolean isShowListTotal()
	{
		return true;
	}
}
