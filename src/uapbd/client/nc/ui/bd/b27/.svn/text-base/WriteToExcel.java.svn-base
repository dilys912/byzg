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
 * 李江涛 时间：2014-09-03 功能 : 获取Excel数据
 */
public class WriteToExcel {

	public static Workbook w = null;
	public static int rows = 0;
	public static CargdocVO[] wbvo = null;
	public static String pk_cargdoc = "";

	/**
	 * 功能: 打开创建文件
	 */
	public static void creatFile(String sourceFile) {
		try {
			/** 创建只读的Excel工作薄的对象 */
			w = Workbook.getWorkbook(new File(sourceFile));
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能:获取表体的值
	 * 
	 * @param <object>
	 */

	@SuppressWarnings( { "unchecked" })
	public <object> CargdocVO[] readData(int sheetNum) throws BusinessException {
		ArrayList list = new ArrayList();
		Sheet ws = w.getSheet(sheetNum);
		rows = ws.getRows();// 行数
		for (int i = 0; i < rows; i++) {
			Cell[] cells = ws.getRow(i);
			CargdocVO bvo = new CargdocVO();// 存放Excel中数据
			String cscode = getString(cells[1].getContents());// 货位编号
			String csname = getString(cells[2].getContents());// 货位名称
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
	 * 字符与数值之间的校验(isnum为true时,为数值型，否则为字符型)
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
