 
package nc.ui.arap.h101;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;


/** 
 * @author  
 * @time 2011-10-17
 * @类型名  : WriteToExcel
 * @功能 : 获取Execel数据
 */
public class WriteToExcel {

    public static Workbook w   = null;
    public static int rows=0;
    public static String pk_corp="";
    
    /**
     * 功能: 打开创建文件
     */
    public static void creatFile(String sourceFile){
        try {
            /** 创建只读的Excel工作薄的对象*/
            w = Workbook.getWorkbook(new File(sourceFile));            
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 功能:获取表体的值 
     * @param <object>
     */
    @SuppressWarnings("unchecked")
	public SuperVO[] readData(int sheetNum) throws BusinessException{
        ArrayList list = new ArrayList();
        Sheet ws = w.getSheet(sheetNum);
        rows = ws.getRows();//行数
        String strNum="应该为数字型";
    	pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();//公司
    	for(int i=2;i<rows;i++){
            Cell[] cells = ws.getRow(i);
            SuperVO vo = null;//存放Excel中数据
            StringBuffer info = new StringBuffer("");//存放错误信息
        	String name = "";
            try{
            	try{
	            name = getString(cells[0].getContents());
            	}catch(Exception e){
            		e.printStackTrace();
            	}
                if(!info.toString().trim().equals("")){
                	vo.setAttributeValue("vdef1", info.toString());
                }
                list.add(vo);
                
            }catch(Exception e){
            	continue;
            }
        }
        return null;
    }
    
    private  String BLXS(String xs,int x) {
    	//小数点处理成x位
		UFDouble blxsz = new UFDouble(xs,x);
		String value=getString(blxsz);
		return value;
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
    
	  public  String getString(String obj){
		  if(obj == null){
			  try{
				  return obj==null?"":obj.toString().trim();
			  }catch(Exception e){
				  return "";
			  }
		  }else{
			  return "";
		  }
	  }
	  public  String getString(Object obj){
		  if(obj == null){
			  try{
				  return obj==null?"":obj.toString().trim();
			  }catch(Exception e){
				  return "";
			  }
		  }else{
			  return "";
		  }
	  }
	  
	  public String getStringFromExe(Object obj){
		  if(obj == null){
			  try{
				  return obj==null?"":String.valueOf(obj).trim();
			  }catch(Exception e){
				  return "";
			  }
		  }else{
			  return "";
		  }
	  }
}

