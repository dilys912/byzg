package nc.vo.by.invapp.pub.Toolkits;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;


/**
 * 说明:前后台公用工具类
 */
public class Toolkits {
	
    /**
     * 功能:
     * 检查传入的参数是否为空。
     * 
     * 如果value的类型为String，并且value.length()为0，返回true。
     * 如果value的类型为Object[]，并且value.length为0，返回true。
     * 如果value的类型为Collection，并且value.size()为0，返回true。
     * 如果value的类型为Dictionary，并且value.size()为0，返回true。 
     * 如果value的类型为Map，并且value.size()为0，返回true。 
     * 否则返回false。
     * @param value 被检查值。
     * @return
     * @return boolean 如果被检查值为null，返回true。 
     */
    @SuppressWarnings("unchecked")
	public static boolean isEmpty(Object value) {
        if (value == null){
            return true;
        }
        if ((value instanceof String) && (((String) value).trim().length() <= 0)){
            return true;
        }
        if ((value instanceof Object[]) && (((Object[]) value).length <= 0)) {
            return true;
        }
        //判断数组中的值是否全部为空null.
        if (value instanceof Object[]) {
            Object[] t = (Object[]) value;
            for (int i = 0; i < t.length; i++) {
                if (t[i] != null) {
                    return false;
                }
            }
            return true;
        }
        if ((value instanceof Collection) && ((Collection) value).size() <= 0){
            return true;
        }
        if ((value instanceof Dictionary) && ((Dictionary) value).size() <= 0){
            return true;
        }
        if ((value instanceof Map) && ((Map) value).size() <= 0){
            return true;
        }
        return false;
    }
    /**
     * 功能:转换int数组为List 
     * @param iarray
     * @return
     * @return:List
     */
    @SuppressWarnings("unchecked")
	public static LinkedList toList(int [] iarray){
        LinkedList<Integer> list=new LinkedList<Integer>();
        for (int i = 0; i < iarray.length; i++) {
            list.add(new Integer(iarray[i]));
        }
        return list;
    }
    

	public static UFDouble getUfdoubleNRound(UFDouble value,int n){
		value = new UFDouble(value.doubleValue(),n);
		return value;
	}
    
    /**
     * 功能: 转换List为int数组
     * @param list
     * @return
     * @return:int[]
     */
    @SuppressWarnings("unchecked")
	public static int[] toArray(List list){
        Object[] arr1=list.toArray();
        int[] arr2=new int[list.size()];
        for (int i = 0; i < arr1.length; i++) {
            arr2[i] = ((Integer)arr1[i]).intValue();
        }
        return arr2;
    }
    
    
    /**
     * 将一个String数组变成带括号字符,便于带in条件的SQL语句使用
     * 例:('111','222','333','')
     * @param array
     * @return
     */
    public static String combinArrayToString(String[] array){
        if(isEmpty(array)){
            return "('')";
        }
        StringBuffer str=new StringBuffer("('");
        for (int i = 0; i < array.length; i++) {
            str.append(array[i]);
            str.append("','");
        }
        str.append("')");
        return str.toString();
    }
    
    /**
     * 将一个String数组变成带括号字符,便于带in条件的SQL语句使用
     * 例:'111','222','333',''
     * @param array
     * @return
     */
    public static String combinArrayToString2(String[] array){
      
        StringBuffer str=new StringBuffer("");
        for (int i = 0; i < array.length; i++) {
        	str.append("'");
        	str.append(array[i]);
            str.append("',");
        }
        str.append("''");
        return str.toString();
    }
    
    public static  String combinArrayToString3(String[] pkvaluses) {
    	String str = "'";
    	if(pkvaluses != null){
    		for(int i=0;i<pkvaluses.length;i++){
    			if(i == pkvaluses.length-1){
    				str += pkvaluses[i]+"'";
    			}else{
    				str += pkvaluses[i] + "','";
    			}
    		}
    		return str;
    	}else{
    		return null;
    	}
	}

    
    /**
     * 功能: 替换字符
     * @param str_par 原始字符串
     * @param old_item 被替换的字符串
     * @param new_item 替换的字符串
     * @return
     * @return:String
     */
    public static synchronized String replaceAll(String str_par, String old_item, String new_item)
    {
        String str_return = "";
        String str_remain = str_par;
        boolean bo_1 = true;
        while (bo_1)
        {
            int li_pos = str_remain.indexOf(old_item);
            if (li_pos < 0)
            {
                break;
            } // 如果找不到,则返回
            String str_prefix = str_remain.substring(0, li_pos);
            str_return = str_return + str_prefix + new_item; // 将结果字符串加上原来前辍
            str_remain = str_remain.substring(li_pos + old_item.length(), str_remain.length());
        }
        str_return = str_return + str_remain; // 将剩余的加上
        return str_return;
    }
    
    /**
     * 合计
     * @param value
     * @return
     */
    public static double total(double [] value){
        double j=0;
        for (int i = 0; i < value.length; i++) {
            j=j+value[i];
        }
        return j;
    }
    
    

    /**
     * 功能: 比较两个字符串的大小,返回true表示后面比前面大
     * @param String sa,String sb要比较的两个字符串
     * @return boolean
     */
    public static boolean compareStr(String sa,String sb){
        boolean flag=false;
        int i=sa.compareTo(sb);
        if (i<0){
            flag = true;
            return flag;
        }
        return flag;
    }
    
    /**
     * 功能: 比较两个数值的大小,返回true表示后面比前面大
     * @param String sa,String sb要比较的两个字符串
     * @return boolean
     */
    public static boolean compareDou(UFDouble sa,UFDouble sb){
        boolean flag=false;
        int i=sa.compareTo(sb);
        if (i<0){
            flag = true;
            return flag;
        }
        return flag;
    }
    
	/**
	 * <H3>方法作用</H3>获取单据号<BR>
	 * 
	 * @param billTypecode
	 * @param pk_corp
	 * @param customBillCode
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	public static String getBillNO(String billTypecode,String pk_corp,String customBillCode,BillCodeObjValueVO vo) throws BusinessException{
		return new BillcodeGenerater ().getBillCode(billTypecode,pk_corp,customBillCode,vo);
	}
	
	//把"1"转化成1时用下标找TOBIG[1]就是对应的
    private static final String[] TOBIG = new String[] { "零", "壹", "贰", "叁",
        "肆", "伍", "陆", "柒", "捌", "玖" };
    // 这里是单位从低到高的排列
    private static final String POS[] = new String[] { "", "拾", "佰", "仟", "万",
        "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿" };
	/**
	 * 将数字转换成大写
	 * @param number java.lang.string
	 * @return java.lang.String
	 */
	public static String getCapital(String number){  
		String capital;
    	int len_zs=number.indexOf(".");               //整数
    	if(len_zs<0){
    		capital = getCapitalOfInt(number)+"整";//.concat("整");
    	}else{
    	int len_xs=number.substring(len_zs).length(); //小数
         capital = getCapitalOfInt(number.substring(0,len_zs)).concat("点").concat(getCapitalOfFloat(number.substring(len_zs+1,len_zs+len_xs)));
    	}
    	return capital;
	}
	
	
 	
	/**
	 * 将整数转换成大写
     * @return java.lang.String
	 * */
    public static String getCapitalOfInt(String str)
    {
       String newStr ="";
       for (int i = 0, j = str.length(); i < j; i++)
       {
        String s = str.substring(j - i - 1, j - i);
        newStr = TOBIG[Integer.parseInt(s)].concat(POS[i])+newStr;
       }
       newStr = newStr.replace("零仟", "零");
       newStr = newStr.replace("零佰", "零");
       newStr = newStr.replace("零拾", "零");
       newStr = newStr.replace("零万", "万");
       for(int i= 0;i<8;i++)
        newStr = newStr.replace("零零", "零");
       newStr = newStr.replace("零仟", "仟");
       newStr = newStr.replace("零佰", "佰");
       newStr = newStr.replace("零拾", "拾");
       newStr = newStr.replace("零万", "万");
       newStr = newStr.replace("零亿", "亿");
       if(newStr.endsWith("零"))
        newStr = newStr.substring(0,newStr.length()-1);
       return newStr;
    }
    
    /**
     * 将小数转换成大写
     * @return java.lang.String
     * */
    public static String getCapitalOfFloat(String str)
    {
       String newStr ="";
       for (int i = 0, j = str.length(); i < j; i++)
       {
        String s = str.substring(j - i - 1, j - i);
        newStr = TOBIG[Integer.parseInt(s)]+newStr;
       }
       return newStr;
    }
    
	  public static UFDate getUFDate(Object obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return new UFDate(obj.toString().trim());
			  }catch(Exception e){
				  return null;
			  }
		  }else{
			  return null;
		  }
	  }
	  public static UFDouble getUFDouble(Object obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return new UFDouble(obj.toString().trim());
			  }catch(Exception e){
				  return new UFDouble(0);
			  }
		  }else{
			  return new UFDouble(0);
		  }
	  }
	  public  static UFDouble getUFDouble(String obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return new UFDouble(obj.trim());
			  }catch(Exception e){
				  return new UFDouble(0);
			  }
		  }else{
			  return new UFDouble(0);
		  }
	  }
	  
	  public  static UFDouble getUFDouble(UFDouble ufd){
		  if(!Toolkits.isEmpty(ufd)){
			  try{
				  return ufd;
			  }catch(Exception e){
				  return new UFDouble(0);
			  }
		  }else{
			  return new UFDouble(0);
		  }
	  }
	  
	  public  static Integer getInteger(Object obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return Integer.parseInt(obj.toString().trim());
			  }catch(Exception e){
				  return new Integer(0);
			  }
		  }else{
			  return new Integer(0);
		  }
	  }
	  public static Integer getInteger(String obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return Integer.parseInt(obj.trim());
			  }catch(Exception e){
				  return new Integer(0);
			  }
		  }else{
			  return new Integer(0);
		  }
	  }
	  
	  public static String getString(String obj){
		  if(!Toolkits.isEmpty(obj)){
			  try{
				  return obj==null?"":obj.trim();
			  }catch(Exception e){
				  return "";
			  }
		  }else{
			  return "";
		  }
	  }
	  public static String getString(Object obj){
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

		
	/**
	 * @param vos
	 * @param idfield
	 * @param textfield
	 * @param fatheridname
	 * @return
	 * 转换成NODE结点的格式
	 */
	public static List<Map<String, String>> getListTreeData(SuperVO[] vos, String idfield,
			String textfield, String fatheridname) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		if(isEmpty(vos)||isEmpty(idfield)||isEmpty(textfield)){
			return null;
		}
		for (int i = 0; i < vos.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			String id = getString(vos[i].getAttributeValue(idfield));
			String[] texts = textfield.split("[+]");
			String text = "";
			for (int j = 0; j < texts.length; j++) {
				text = text + getString(vos[i].getAttributeValue(texts[j]))+"  ";
			}
			String fatherid = getString(vos[i].getAttributeValue(fatheridname));
			map.put("id", id);  
			map.put("text", text);  
			map.put("parentId", fatherid);  
			list.add(map);
		}
		return list;
	}
	
	/**
	 * @param year 传入的年
	 * @param month 传入的月
	 * @param 月步长
	 * @return 根据传入的年月,返回下一个月的年跟月
	 * 注：返回值int数组,长度定长2位,第一位年，第二位月,月份没有补零,需调用者补零
	 */
	public static int[] getNextNMonth(int year, int month, int step) {
		if(step <= 0){
			return new int[] { year, month };
		}
		int nextmonth = month + 1;
		if (nextmonth > 12) {
			nextmonth = nextmonth - 12;
			year = year + 1;
		}
		if (step > 1) {
			int[] result = new int[2];
			result = getNextNMonth(year, nextmonth, step - 1);
			year = result[0];
			nextmonth = result[1];
		}
		return new int[] { year, nextmonth };
	}

	  /**
	   * 
	  * @Title: cutZero 
	  * @Description: 去掉小数后面的0
	  * @author zyb zhyb966@gmail.com 
	  * @date 2011-8-28 下午06:04:11 
	  * @return String    返回类型 
	  * @throws
	   */
	 public static String cutZero(String v){
		   if(v.indexOf(".") > -1){
		   while(true)
		   {
		      if(v.lastIndexOf("0") == (v.length() - 1)){
		      v = v.substring(0,v.lastIndexOf("0"));
		      }else{
		   break;
		      }
		   }
		   if(v.lastIndexOf(".") == (v.length() - 1)){
		   v = v.substring(0, v.lastIndexOf("."));
		   }
		   }
		   return v;
		   }
}
