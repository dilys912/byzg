package nc.vo.by.invapp.pub;


import nc.ui.trade.button.IBillButton;
/**
 * ˵��:��������
 */
public interface IBYConst {
	/** ��׼����Ƭ���水ť*/
	public int[] TREECARD_BUTTONS={IBillButton.Add,IBillButton.Edit,IBillButton.Delete,IBillButton.Save,
			IBillButton.Cancel,IBillButton.Refresh};
	/** ��׼���б����水ť*/
	public int[]TREELIST_BUTTONS={IBillButton.Add,IBillButton.Edit,IBillButton.Delete,
            IBillButton.Refresh,   };
    /** ��׼�б����水ť     */
    public int[] LIST_BUTTONS={ IBillButton.Query, IBillButton.Add, IBillButton.Edit,IBillButton.Delete, 
            IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Refresh};
    /** ��׼�����б���ť     */
    public int[] LIST_BUTTONS_M={ IBillButton.Query, IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Card,IBillButton.Refresh,IBillButton.Copy };
    /**  ��׼������Ƭ��ť--������     */
    public int[] CARD_BUTTONS_M_CSH={ IBillButton.Brow,IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Return,IBillButton.Refresh,IBillButton.Print 
            };   
    /**  ��׼������Ƭ��ť     */
    public int[] CARD_BUTTONS_M={ IBillButton.Brow,IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
            IBillButton.Line, IBillButton.Save, IBillButton.Cancel,IBillButton.Return,IBillButton.Refresh,IBillButton.Print 
            };  
}