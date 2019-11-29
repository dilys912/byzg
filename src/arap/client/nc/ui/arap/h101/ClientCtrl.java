package nc.ui.arap.h101; 
  
import nc.ui.by.invapp.billmanage.AbstractCtrl;
import nc.ui.by.invapp.button.ButtonTool;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.pub.HYBillVO;
  
/** 
 * @author �Զ����� 
*/ 
public class ClientCtrl extends AbstractCtrl { 
  
 	public String getBillType() { 
 		return "H101"; 
 	} 
  
 	public String[] getBillVoName() { 
 		return new String[] {  
 				HYBillVO.class.getName(), 
 				nc.vo.arap.h101.FksqdVO.class.getName(), 
 				nc.vo.arap.h101.FksqdBVO.class.getName()}; 
 	} 
  //�޸ķ�����ȡ��ҵ�����ͣ�Busitype����ѯ����  by zy  2019.08.07
 	public int[] getCardButtonAry() { 
 		return CARD_BUTTONS_M;
// 		return ButtonTool.insertButton(IBillButton.Busitype, CARD_BUTTONS_M, 0);
 	} 
  
 	public int[] getListButtonAry() { 
// 		return super.getListButtonAry();
// 		return ButtonTool.insertButtons(new int[]{IBillButton.Busitype,IBillButton.Print}, LIST_BUTTONS_M, 0); 
 		return ButtonTool.insertButtons(new int[]{IBillButton.Print}, LIST_BUTTONS_M, 0); 
 	} 
 	//end
  
 	public String getPkField() { 
 		return "pk_fksqd"; 
 	} 
  
 	@Override 
 	public String getChildPkField() { 
 		return null; 
 	} 

	/** ��׼����Ƭ���水ť*/
	public int[] TREECARD_BUTTONS={IBillButton.Add,IBillButton.Edit,IBillButton.Delete,IBillButton.Save,
			IBillButton.Cancel,IBillButton.Refresh};
	/** ��׼���б���水ť*/
	public int[]TREELIST_BUTTONS={IBillButton.Add,IBillButton.Edit,IBillButton.Delete,
            IBillButton.Refresh,   };
    /** ��׼�����б�ť     */
    public int[] LIST_BUTTONS_M={ IBillButton.Query, IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Card,IBillButton.Refresh,IBillButton.Copy };
    /**  ��׼����Ƭ��ť     */
    public int[] CARD_BUTTONS_M={ IBillButton.Brow,IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Return,IBillButton.Refresh,IBillButton.Print 
            };  
} 
