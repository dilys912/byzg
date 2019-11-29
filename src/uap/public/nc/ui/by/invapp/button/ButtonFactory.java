package nc.ui.by.invapp.button;

/**
 * ˵��: ��ť����
 */
public class ButtonFactory
{
	/**
	 * ButtonFactory ������ע�⡣
	 */
	private ButtonFactory()
	{
		super();
	}

	/**
	 * ֱ�Ӵ�����ť
	 * @param id
	 * @param code
	 * @param name
	 * @return
	 */
	public static nc.vo.trade.button.ButtonVO createButtonVO(int id, String code, String name)
	{
		nc.vo.trade.button.ButtonVO btn = new nc.vo.trade.button.ButtonVO();
		btn.setBtnNo(id);
		btn.setBtnName(code);
		btn.setHintStr(name);
		btn.setBtnCode(code);
		btn.setBtnChinaName(code);//��������ťʱ�õ� ��Ӧ buttonObj���code
		return btn;
	}
	
}