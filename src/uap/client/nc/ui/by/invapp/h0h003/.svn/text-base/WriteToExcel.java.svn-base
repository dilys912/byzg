 
package nc.ui.by.invapp.h0h003;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.by.invapp.h0h003.InvContrastdocVO;
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
    public static InvContrastdocVO[] wbvo = null;
    public static String pk_corp="";

	private HashMap<String, HashMap<String, String>> newinvtmap = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, HashMap<String, String>> invcltmap = new HashMap<String, HashMap<String, String>>();
	private int count = 1;
	
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
    public InvContrastdocVO[] readData(int sheetNum) throws BusinessException{
        ArrayList list = new ArrayList();
        Sheet ws = w.getSheet(sheetNum);
        rows = ws.getRows();//行数

		if (rows <= 1) {
			return null;
		}
		List<String> invcodelist = new ArrayList<String>();
		//得到所有的存货编码，为了查询存货名称
		for (int i = 1; i < rows; i++) {
			Cell[] c = ws.getRow(i);
			invcodelist.add(Toolkits.cutZero(getString(c[3].getContents())));
			//800个查询一次
			if(rows>=800){
				if(i==count*800||i==rows-1){
					String invcodes = Toolkits.combinArrayToString(invcodelist.toArray(new String[0]));
					getInvNameMap(invcodes);
					invcodelist = new ArrayList<String>();
					count++;
				}
			}
		}
		//如果导入的存货编码小于800查询一次
		if(rows<800){
			String invcodes = Toolkits.combinArrayToString(invcodelist.toArray(new String[0]));
			getInvNameMap(invcodes);
		}
		
		getInvClMap();
		
        for(int i=1;i<rows;i++){
            Cell[] c = ws.getRow(i);
			InvContrastdocVO ch = new InvContrastdocVO();
			
			String pk_invcl = newinvtmap.get(getString(c[3].getContents()))==null?"":newinvtmap.get(getString(c[3].getContents())).get("pk_invcl");
		    if (!"".equals(pk_invcl)) {
				ch.setPk_invcl(pk_invcl);
			    ch.setInvclasscode(invcltmap.get(pk_invcl).get("invclasscode"));
			    ch.setInvclassname(invcltmap.get(pk_invcl).get("invclassname"));
			}
		    
		    ch.setInvcode(getString(c[3].getContents()));
		    HashMap<String, String> map = newinvtmap.get(getString(c[3].getContents()));
		    if (map!=null) {
			    ch.setInvname(map.get("invname"));
			    ch.setPk_invbasdoc(map.get("pk_invbasdoc"));
			    ch.setInvspec(map.get("invspec"));
			    ch.setInvtype(map.get("invtype"));
			}
		    
		    ch.setOldinvcode(getString(c[7].getContents()));
		    ch.setOldinvname(getString(c[8].getContents()));
		    ch.setOldinvspec(getString(c[9].getContents()));
		    ch.setOldinvtype(getString(c[10].getContents()));
		    ch.setMemo(getString(c[11].getContents()));
		    System.out.println(getString(c[3].getContents()));
			list.add(ch);
        }
        if(list!=null && list.size()>0){
        	wbvo = (InvContrastdocVO[]) list.toArray(new InvContrastdocVO[list.size()]);
        }
        return wbvo;
    }

	/**
	 * 查询所有导入的存货编码、存货名称
	 * */
	@SuppressWarnings("unchecked")
	public void getInvNameMap(String invcodes){
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select distinct(invcode),invname,pk_invbasdoc,invspec,invtype,pk_invcl from bd_invbasdoc where invcode in "+invcodes+" and nvl(dr,0)=0";
		ArrayList al = new ArrayList();
		try {
			al = (ArrayList) qurey.executeQuery(sql,new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(al!=null&&al.size()>0){
			for (int i = 0; i < al.size(); i++) {
				HashMap<String, String> hm = (HashMap<String, String>)al.get(i);
				String invcode = hm.get("invcode");
				if(!newinvtmap.containsKey(invcode)){
					newinvtmap.put(invcode, hm);
				}
			}
		}
	}
	

	/**
	 * 查询所有导入的存货分类编码、存货分类名称
	 * */
	@SuppressWarnings("unchecked")
	public void getInvClMap(){
		IUAPQueryBS qurey = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql = "select distinct(invclasscode),invclassname,pk_invcl from bd_invcl where nvl(dr,0)=0";
		ArrayList al = new ArrayList();
		try {
			al = (ArrayList) qurey.executeQuery(sql,new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(al!=null&&al.size()>0){
			for (int i = 0; i < al.size(); i++) {
				HashMap<String, String> hm = (HashMap<String, String>)al.get(i);
				String pk_invcl = hm.get("pk_invcl");
				if(!invcltmap.containsKey(pk_invcl)){
					invcltmap.put(pk_invcl, hm);
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

