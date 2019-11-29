package nc.ui.bd.languagetransformations;
import nc.ui.pub.ClientEnvironment;
public class Transformations {
	
	public static String getLstrFromMuiStr(String sourcestr)
	{
		return getLstrFromMuiStr(sourcestr,null,null);
	}
	 public static  String getLstrFromMuiStr(String sourcestr,String splitchar,String splitsubchar )
	   {
		StringBuffer ChinessStr=new StringBuffer();
		 StringBuffer OtherStr=new StringBuffer();
		 String tempStr=null;
		 splitchar=splitsubchar==null?"&":splitchar;
		 splitchar=splitsubchar==null?"@":splitsubchar;
		 String [] splitsource=sourcestr.split("&");
		 for(int i=0;i<splitsource.length;i++)
		 {
			 tempStr=splitsource[i];
			 if(tempStr.indexOf("@")>0)
			 {
				 ChinessStr.append(tempStr.substring(0,tempStr.indexOf("@")));
				 OtherStr.append(tempStr.substring(tempStr.indexOf("@")+1,tempStr.length()));
			 }
			 else 
			 {
				 ChinessStr.append(tempStr);
				 OtherStr.append(tempStr);
			 }
		 }

		return ChinessStr.append(OtherStr).toString();
	   }
	 public static  String getLstrFromMuiStr(String sourcestr,String otherstr)
	 {
		 if("simpchn".equalsIgnoreCase(ClientEnvironment.getInstance().getLanguage()))
		 {
			 return sourcestr;
		 }
		 else 
			 return otherstr;
	 }      
}
