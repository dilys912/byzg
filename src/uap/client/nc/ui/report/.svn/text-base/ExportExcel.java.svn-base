package nc.ui.report;

import java.io.*;

import javax.swing.JTable;

import jxl.*;

import jxl.write.*;
import jxl.write.biff.RowsExceededException;

/**
 * 用来导出Excel的工具
 */
public class ExportExcel {
        private static WritableWorkbook writableWorkbook;
        public static boolean writeJxlByTableModel(String filePatch, JTable table) {
                
                if (table == null || table.getRowCount() <= 0)
                        return false;
                if (!filePatch.endsWith(".xls")) {
                        System.out.println("=======不是正确的xls格式，请核查==========");
                        return false;
                }// end if
                try {
                        OutputStream os = new FileOutputStream(filePatch);
                        // 创建可写簿
                        writableWorkbook = Workbook.createWorkbook(os);
                        // 创建工作表
                        WritableSheet ws = writableWorkbook.createSheet("sheet", 0);
                        // 创建一个内容 第一个整数为 列，第二个整数位 行
                        Label label = null;
                        int rows = table.getRowCount();
                        int cols = table.getColumnCount();
                        
                        for (int col = 0; col < cols ; col++) {
                            String headerName = table.getTableHeader().getColumnModel().getColumn(col).getHeaderValue() == null ? "" :
                                            table.getTableHeader().getColumnModel().getColumn(col).getHeaderValue().toString();
                            label = new Label(col,0,headerName);
                           
                            ws.addCell(label);
                        }
                        
                        for (int row = 0; row < rows + 1; row++) {
                            for (int col = 0; col < cols ; col++) {
                                 
                                 label = new Label(col,row+1,table.getValueAt(row, col) == null ? "" : table.getValueAt(row, col).toString());
                               
                                 ws.addCell(label);
                            }
                        }

                        writableWorkbook.write();
                        writableWorkbook.close();
                        os.close();
                } catch (IOException e) {

                        e.printStackTrace();
                } catch (RowsExceededException e) {

                        e.printStackTrace();
                } catch (WriteException e) {

                        e.printStackTrace();
                } finally {
                        
                }
                return true;
        }
}