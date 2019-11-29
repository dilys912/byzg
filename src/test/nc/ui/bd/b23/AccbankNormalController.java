package nc.ui.bd.b23;

import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.ISingleController;
import nc.vo.bd.b23.AccbankAggVO;
import nc.vo.bd.b23.AccbankVO;

public class AccbankNormalController extends AbstractManageController
  implements ISingleController
{
  public String getBillType()
  {
    return "10081602";
  }

  public String[] getBillVoName()
  {
    return new String[] { AccbankAggVO.class.getName(), AccbankVO.class.getName(), AccbankVO.class.getName() };
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
    return 1;
  }

  public String[] getCardBodyHideCol()
  {
    return null;
  }

  public int[] getCardButtonAry()
  {
    return new int[] { 20, 1, 3, 32, 0, 7, 31 };
  }

  public String getChildPkField()
  {
    return null;
  }

  public String getHeadZYXKey()
  {
    return null;
  }

  public String[] getListBodyHideCol()
  {
    return null;
  }

  public int[] getListButtonAry()
  {
    return new int[] { 1, 3, 32, 8, 5, 110, 6, 30 };
  }

  public String[] getListHeadHideCol()
  {
    return null;
  }

  public String getPkField()
  {
    return "pk_accbank";
  }

  public Boolean isEditInGoing()
    throws Exception
  {
    return null;
  }

  public boolean isExistBillStatus()
  {
    return false;
  }

  public boolean isLoadCardFormula()
  {
    return false;
  }

  public boolean isShowCardRowNo()
  {
    return false;
  }

  public boolean isShowCardTotal()
  {
    return false;
  }

  public boolean isShowListRowNo()
  {
    return true;
  }

  public boolean isShowListTotal()
  {
    return false;
  }

  public boolean isSingleDetail() {
    return false;
  }
}