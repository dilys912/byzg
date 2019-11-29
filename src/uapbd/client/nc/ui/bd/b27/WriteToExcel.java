package nc.ui.bd.b27;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import nc.vo.bd.b27.CargdocVO;
import nc.vo.pub.BusinessException;

/**
 * ��� ʱ�䣺2014-09-03 ���� : ��ȡExcel����
 */
public class WriteToExcel {

	public static Workbook w = null;
	public static int rows = 0;
	public static CargdocVO[] wbvo = null;
	public static String pk_cargdoc = "";

	/**
	 * ����: �򿪴����ļ�
	 */
	public static void creatFile(String sourceFile) {
		try {
			/** ����ֻ����Excel�������Ķ��� */
			w = Workbook.getWorkbook(new File(sourceFile));
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����:��ȡ�����ֵ
	 * 
	 * @param <object>
	 */

	@SuppressWarnings( { "unchecked" })
	public <object> CargdocVO[] readData(int sheetNum) throws BusinessException {
		ArrayList list = new ArrayList();
		Sheet ws = w.getSheet(sheetNum);
		rows = ws.getRows();// ����
		for (int i = 0; i < rows; i++) {
			Cell[] cells = ws.getRow(i);
			CargdocVO bvo = new CargdocVO();// ���Excel������
			String cscode = getString(cells[1].getContents());// ��λ���
			String csname = getString(cells[2].getContents());// ��λ����
			bvo.setCscode(cscode);
			bvo.setCsname(csname);

			list.add(bvo);

		}

		if (list != null && list.size() > 0) {
			wbvo = (CargdocVO[]) list.toArray(new CargdocVO[list.size()]);
		}
		return wbvo;
	}

	/**
	 * �ַ�����ֵ֮���У��(isnumΪtrueʱ,Ϊ��ֵ�ͣ�����Ϊ�ַ���)
	 * */
	public static boolean checkStringToNum(String str) {
		Pattern pattern = Pattern.compile("[0.000000-9.000000]*");
		Matcher isNum = pattern.matcher(str);
		boolean isnum = isNum.matches();
		return isnum;
	}

	public String getString(String obj) {
		if (obj != null) {
			try {
				return obj == null ? "" : obj.toString().trim();
			} catch (Exception e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public String getString(Object obj) {
		if (obj != null) {
			try {
				return obj == null ? "" : obj.toString().trim();
			} catch (Exception e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public String getStringFromExe(Object obj) {
		if (obj != null) {
			try {
				return obj == null ? "" : String.valueOf(obj).trim();
			} catch (Exception e) {
				return "";
			}
		} else {
			return "";
		}
	}
}
