package nc.ui.ia.ia402;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.ui.pub.print.output.OutputJobUtils;
import nc.vo.jcom.lang.StringUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelExportTool {
	private List<Vector<Vector<Object>>> m_data = null; // Excel数据

	@SuppressWarnings("rawtypes")
	private List<List<Map>> m_data2 = null; // Excel数据(Map格式)

	private String[][] m_colnames = null; // 列名

	private String[] sheetNames = null; // 页签名称

	HSSFWorkbook wb = null;

	private HSSFCellStyle titleCellStyle = null;

	public ExcelExportTool() {
		super();
	}

	public void exportExcelFile() throws Exception {
		String[] result = OutputJobUtils.selSaveExcelFileName();
		if (result == null) return;
		String excelFileName = result[0];
		if (StringUtil.isEmpty(excelFileName)) return;
		createExcelFile(excelFileName);
	}

	private void createExcelFile(String filePath) throws Exception {
		if (wb == null) {
			wb = new HSSFWorkbook();
		}
		if (sheetNames == null) {
			sheetNames = new String[m_colnames.length];
			for (int i = 0; i < sheetNames.length; i++) {
				sheetNames[i] = "sheet" + (i + 1);
			}
		}
		for (int i = 0; i < sheetNames.length; i++) {
			HSSFSheet hs = wb.createSheet();
			wb.setSheetName(i, sheetNames[i], HSSFWorkbook.ENCODING_UTF_16);
			fillTitle2Sheet(hs, i);
			fillData2Sheet(hs, i);
		}
		FileOutputStream fileOut = null;
		try {// 写出文件
			fileOut = new FileOutputStream(filePath);
			wb.write(fileOut);// 把Workbook对象输出到文件workbook.xls中
		} finally {
			fileOut.close();
		}
	}

	// 得到标题的风格
	private HSSFCellStyle getTitleCellStyle() {
		if (titleCellStyle == null) {
			titleCellStyle = wb.createCellStyle();
			titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			titleCellStyle.setFillPattern(HSSFCellStyle.FINE_DOTS);
			titleCellStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			titleCellStyle.setFont(font);

		}
		return titleCellStyle;
	}

	// 填充Excel的标题
	private void fillTitle2Sheet(HSSFSheet hsheet, int idx) {
		String[] title = m_colnames[idx];
		if (title == null || title.length <= 0) return;
		HSSFRow row = hsheet.createRow(0);
		row.setHeight((short) (row.getHeight() * 2));
		HSSFCell cell = null;
		for (int i = 0; i < m_colnames[idx].length; i++) {
			cell = row.createCell((short) i);
			cell.setCellStyle(getTitleCellStyle());
			if (m_colnames[idx][i] != null) cell.setCellValue(m_colnames[idx][i].toString());
		}
	}

	// 填充sheet内容
	@SuppressWarnings("rawtypes")
	private void fillData2Sheet(HSSFSheet hsheet, int idx) {
		if (m_data2 != null && m_data2.size() > 0) {
			List<Map> data = m_data2.get(idx);
			String[] headers_curr = this.m_colnames[idx];
			HSSFRow row = null;
			HSSFCell cell = null;
			int rowid = hsheet.getLastRowNum();
			for (int i = 0; i < data.size(); i++) {
				row = hsheet.createRow(rowid + i + 1);
				Map rowObj = data.get(i);
				for (int j = 0; j < headers_curr.length; j++) {
					cell = row.createCell((short) j);
					if (rowObj.get(headers_curr[j]) != null) cell.setCellValue(rowObj.get(headers_curr[j]).toString());
				}
			}
		} else {
			Vector<Vector<Object>> data = m_data.get(idx);
			if (data == null || data.size() <= 0) return;
			HSSFRow row = null;
			HSSFCell cell = null;
			int rowid = hsheet.getLastRowNum();
			for (int i = 0; i < data.size(); i++) {
				row = hsheet.createRow(rowid + i + 1);
				Vector<Object> rowObj = data.get(i);
				for (int j = 0; j < rowObj.size(); j++) {
					cell = row.createCell((short) j);
					if (rowObj.get(j) != null) cell.setCellValue(rowObj.get(j).toString());
				}
			}
		}
	}

	public void setColNames(String[][] colnames) {
		this.m_colnames = colnames;
	}

	public void setData(List<Vector<Vector<Object>>> data) {
		this.m_data = data;
	}

	@SuppressWarnings("rawtypes")
	public void setData2(List<List<Map>> data2) {
		this.m_data2 = data2;
	}

	public void setSheetNames(String[] sheetNames) {
		this.sheetNames = sheetNames;
	}

}
