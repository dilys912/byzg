 
package nc.ui.by.invapp.h0h005;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.by.invapp.h0h005.CargContrastdocVO;
import nc.vo.by.invapp.pub.Toolkits.Toolkits;
import nc.vo.pub.BusinessException;


/** 
 * @author  施坤 
 * @time 2011-10-17
 * @类型名  : WriteToExcel
 * @功能 : 获取Execel数据
 */
public class WriteToExcel {

    public static Workbook w   = null;
    public static int rows=0;
    public static CargContrastdocVO[] wbvo = null;

	private HashMap<String, HashMap<String, String>> newCargtmap = new HashMap<String, HashMap<String, String>>();
	
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
     */
    @SuppressWarnings({ "unchecked" })
    public CargContrastdocVO[] readData(int sheetNum) throws BusinessException{
        ArrayList list = new ArrayList();
        Sheet ws = w.getSheet(sheetNum);
        rows = ws.getRows();//行数

		if (rows <= 1) {
			return null;
		}
		
		getCargMap();
		
        for(int i=1;i<rows;i++){
            Cell[] c = ws.getRow(i);
            CargContrastdocVO ch = new CargContrastdocVO();
			
		    ch.setCscode(getString(c[1].getContents()));
		    HashMap<String, String> map = newCargtmap.get(getString(c[1].getContents()));
		    if (map!=null) {
			    ch.setCsname(map.get("csname"));
			    ch.setPk_cargdoc(map.get("pk_cargdoc"));
			}
		    
		    ch.setOldcscode(getString(c[3].getContents()));
		    ch.setOldcsname(getString(c[4].getContents()));
		    ch.setMemo(getString(c[5].getContents()));
		    
			list.add(ch);
        }
        if(list!=null && list.size()>0){
        	wbvo = (CargContrastdocVO[]) list.toArray(new CargContrastdocVO[list.size()]);
        }
        return wbvo;
    }
	

	/**
	 * 查询所有导入的存货分类编码、存货分类名称
	 * */
	@SuppressWarnings("unchecked")
	public void getCargMap(){

		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

		StringBuffer sqlcarg = new StringBuffer();
		sqlcarg.append(" select distinct carg.cscode, carg.csname, carg.pk_cargdoc ") 
		.append("   from bd_cargdoc carg ") 
		.append("  inner join bd_stordoc stor on stor.pk_stordoc = carg.pk_stordoc ") 
		.append("  inner join bd_calbody cal on cal.pk_calbody = stor.pk_calbody ") 
		.append("  where cal.pk_corp = '"+pk_corp+"' ") 
		.append("    and nvl(carg.dr, 0) = 0 ") 
		.append("    and nvl(stor.dr, 0) = 0 ") 
		.append("    and nvl(cal.dr, 0) = 0 ") 
		.append("    and nvl(carg.sealflag, 'N') = 'N' ");
		ArrayList al = new ArrayList();
		try {
			al = (ArrayList) qurey.executeQuery(sqlcarg.toString(),new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(al!=null&&al.size()>0){
			for (int i = 0; i < al.size(); i++) {
				HashMap<String, String> hm = (HashMap<String, String>)al.get(i);
				String cscode = hm.get("cscode");
				if(!newCargtmap.containsKey(cscode)){
					newCargtmap.put(cscode, hm);
				}
			}
		}
	}

	  public  String getString(Object obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return obj==null?"":obj.toString().trim();
			  }catch(Exception e){
				  return "";
			  }
		  }else{
			  return "";
		  }
	  }
	
}

