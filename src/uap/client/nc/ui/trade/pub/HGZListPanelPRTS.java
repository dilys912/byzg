package nc.ui.trade.pub;

import javax.swing.ImageIcon;

import nc.ui.pub.print.IExDataSource;
import nc.ui.pub.bill.*;
import nc.ui.pub.beans.*;

/**
 * �˴���������˵���� �������ڣ�(2003-6-3 11:28:26)
 * 
 * @author��������
 */
public class HGZListPanelPRTS implements nc.ui.pub.print.IDataSource {
	BillListPanel m_listpanel;
	String m_modulecode;
	String m_barcode = "";

	/**
	 * MonnthApplyPRTS ������ע�⡣
	 */
	public HGZListPanelPRTS(String modulecode, BillListPanel bp) {
		super();
		m_listpanel = bp;
		m_modulecode = modulecode;
	}

	/**
	 * 
	 * �õ����е���������ʽ���� Ҳ���Ƿ������ж����������ı��ʽ
	 * 
	 */
	public java.lang.String[] getAllDataItemExpress() {
		int headCount = 0;
		int bodyCount = 0;
		if (m_listpanel.getHeadBillModel().getBodyItems() != null) {
			headCount = m_listpanel.getHeadBillModel().getBodyItems().length;
		}
		if (m_listpanel.getBodyBillModel().getBodyItems() != null) {
			bodyCount = m_listpanel.getBodyBillModel().getBodyItems().length;
		}
		int count = headCount + bodyCount;
		String[] expfields = new String[count];
		try {
			for (int i = 0; i < headCount; i++) {
				expfields[i] = "h_"
						+ m_listpanel.getHeadBillModel().getBodyItems()[i]
								.getKey();
			}
			for (int j = 0; j < bodyCount; j++) {
				expfields[j + headCount] = m_listpanel.getBodyBillModel()
						.getBodyItems()[j].getKey();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  getAllDataItemExpress()");
		}
		return expfields;
	}

	public java.lang.String[] getAllDataItemNames() {
		int headCount = 0;
		int bodyCount = 0;

		if (m_listpanel.getHeadBillModel().getBodyItems() != null) {
			headCount = m_listpanel.getHeadBillModel().getBodyItems().length;
		}
		if (m_listpanel.getBodyBillModel().getBodyItems() != null) {
			bodyCount = m_listpanel.getBodyBillModel().getBodyItems().length;
		}

		int count = headCount + bodyCount;
		String[] namefields = new String[count];
		try {
			for (int i = 0; i < headCount; i++) {
				namefields[i] = m_listpanel.getHeadBillModel().getBodyItems()[i]
						.getName();
			}
			for (int j = 0; j < bodyCount; j++) {
				namefields[j + headCount] = m_listpanel.getBodyBillModel()
						.getBodyItems()[j].getName();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  getAllDataItemNames()");
		}
		return namefields;
	}

	/**
	 * 
	 * ������������������飬���������ֻ��Ϊ 1 ���� 2 ���� null : û������ ���� 1 : �������� ���� 2 : ˫������
	 * 
	 */
	public java.lang.String[] getDependentItemExpressByExpress(String itemName) {
		return null;
	}

	/*
	 * �������е��������Ӧ������ ������ ����������� ���أ� �������Ӧ�����ݣ�ֻ��Ϊ String[]��
	 */
	public java.lang.String[] getItemValuesByExpress(String itemExpress) {
		int row = m_listpanel.getHeadTable().getSelectedRow();
		System.out.println(row);
		if (itemExpress.startsWith("h_")) {
			Object obj = m_listpanel.getHeadBillModel().getValueAt(row,
					itemExpress.substring(2));
			if (obj != null)
				return new String[] { obj.toString() };
		} else if (itemExpress.startsWith("t_")) {
			Object obj = m_listpanel.getHeadBillModel().getValueAt(row,
					itemExpress.substring(2));
			if (obj != null)
				return new String[] { obj.toString() };
		} else {
			int[] rowCount = m_listpanel.getHeadTable().getSelectedRows();
			String[] retStr = new String[rowCount.length];
			for (int i = 0; i < rowCount.length; i++) {
				Object obj = m_listpanel.getHeadBillModel().getValueAt(rowCount[i],
						itemExpress);
				if (obj != null)
					retStr[i] = obj.toString();
				else
					retStr[i] = "";
			}
			return retStr;
		}
		return null;
	}

	/*
	 * ���ظ�����Դ��Ӧ�Ľڵ����
	 */
	public String getModuleName() {
		return m_modulecode;
	}

	/*
	 * ���ظ��������Ƿ�Ϊ������ ������ɲ������㣻��������ֻ��Ϊ�ַ������� �硰������Ϊ�������������롱Ϊ��������
	 */
	public boolean isNumber(String itemExpress) {
		try {
			if (itemExpress.startsWith("h_")) {
				BillItem item = m_listpanel.getHeadItem(itemExpress
						.substring(2));
				if (item == null)
					return false;
				if (item.getDataType() == 1 || item.getDataType() == 2) {
					return true;
				}
			} else if (itemExpress.startsWith("t_")) {
				BillItem item = m_listpanel.getBodyItem(itemExpress
						.substring(2));
				if (item == null)
					return false;
				if (item.getDataType() == 1 || item.getDataType() == 2) {
					return true;
				}
			} else {

				BillItem item = m_listpanel.getBodyItem(itemExpress);
				if (item == null) {
					return false;
				} else if (item.getDataType() == 1 || item.getDataType() == 2) {
					return true;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  isNumber()");
			return false;
		}
		return false;
	}

	
}
