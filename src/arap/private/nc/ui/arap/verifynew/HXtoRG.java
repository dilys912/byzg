package nc.ui.arap.verifynew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.taskdefs.optional.metamata.MParse;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.itfcheck.IInterfaceCheck;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.ui.po.pub.PoPublicUIClass;
import nc.utils.modify.is.IdetermineService;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pa.Key;
/**
 * 
 * @author gt   JC-->RG
 * 2019-10-25
 *内外销核销信息传标财定时任务
 */
public class HXtoRG  implements IBusinessPlugin{

	public int getImplmentsType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Key[] getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAlertMessage implementReturnFormatMsg(Key[] arg0, String arg1,
			UFDate arg2) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	 private Map queryFph(String djh){
		   List list = null;
		   Map<String,String> map=new HashMap<String,String>();
		      String sql = "select s.vreceiptcode,s.vdef17,s.ccreditnum from arap_djzb z " +
		        "inner join arap_djfb f on z.vouchid=f.vouchid " +
		        "inner join so_saleinvoice s on f.ddlx=s.csaleid " +
		        "where z.djbh='"+djh+"'";
		      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		      try {
		       list = (List)receiving.executeQuery(sql, new MapListProcessor());
		      } catch (BusinessException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		      }
		      if(list.size()==0){
		    	  return map;
		      }
		       map = (Map) list.get(0);
		   return map;
		  }
		  //查询方法   add by gt 2019-10-24
		  private  int  checking(String djPK,String corp){
			  int zt = 0;
			  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			  String check="select zyx39 from ARAP_djzb where vouchid='"+djPK+"' and dwbm='"+corp+"' and nvl(dr,0) = 0";
			  
		 	  try {
			   String billno = (String) receiving.executeQuery(check, new ColumnProcessor());
			   if(billno !=null && "Y".equals(billno)){
				   return  zt=1;
			   }else{
				   return zt=0;
			   }
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return zt;  
		  }
		  
		  //插入方法 add by gt 2019-10-24
		  private void inserting(String djPK,String corp) {
			  IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
			  String update="update ARAP_djzb set zyx39='Y' where vouchid='"+djPK+"' and dwbm='"+corp+"' and nvl(dr,0) = 0";
		      try {
		    	  ipubdmo.executeUpdate(update);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		  //add by gt 2019-10-25
		  private String addZero(String str){
			String mm=Integer.valueOf(str)<10?"0"+str:str;
			return mm;
			
		}
	//edit by gt 
	public String implementReturnMessage(Key[] arg0, String arg1, UFDate arg2)
			throws BusinessException {
//		String corp;
		//国内公司校验
//	    String corp =arg0[0].getValue() != null ? arg0[0].getValue().toString().trim() : "";//参照公司基本档案
//		if(corp.length()>4){
//			return (new StringBuilder("\u7B2C\u4E00\u4E2A\u53C2\u6570:")).append(corp).toString();
//		}
//		String corp = PoPublicUIClass.getLoginPk_corp();//获取当前登录公司
		String corp="1017";
			IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
			Boolean result = idetermineService.check(corp);
			if(result)//判断当前公司是否为国内公司，否则不执行
		{ 
	  StringBuffer voBuffer=new StringBuffer();//存已传过的单据的单据号
	  int fszt=0;//单据发送是否成功
	  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	  String dqsj=new UFDateTime(System.currentTimeMillis()).toString();//当前系统时间
	  String djsj=dqsj.substring(0, 10);
	//  String djsj="2019-10-24";
	  dqsj=dqsj.replace("-", "").replace(" ", "").replace(":", "");
      StringBuffer sql=new StringBuffer();
      sql.append(" select clb1.clbh,clb1.clrq,clb1.clr,zb.djbh,fb.deptid,zb.zyx17,zb.scomment,fb.bzbm,zb.zyx26,zb.prepay,fb.dfbbwsje,zb.vouchid ");
      sql.append(" from  arap_djclb clb1, arap_djfb fb, arap_djzb zb");
      sql.append(" where clb1.fb_oid = fb.fb_oid and clb1.vouchid = zb.vouchid and clb1.dr = 0  and clb1.clbz in (0)  and clb1.dwbm = '"+corp+"'  and clb1.clrq >= '"+djsj+"' and clb1.clrq <= '"+djsj+"'");
      
	try {
		List listData = (List) receiving.executeQuery(sql.toString(), new MapListProcessor());
		  if(listData.size()>0){
		   	   for(int j=0;j<listData.size();j++){
		   		   Map map=(Map)listData.get(j);
		   		   String djh=map.get("djbh").toString();//单据编号
		   		   String djPK=map.get("vouchid").toString();//单据主键
		   		   String depid=map.get("deptid")==null?"0":map.get("deptid").toString();//部门主键
		   		   Map map2=queryFph(djh);
		   		   String fph=map2.get("vdef17")==null?"a":map2.get("vdef17").toString();//发票号--纸质发票号
		   		   if(fph == "a" || depid == "0"){
		   			System.out.println("该单据:"+djh+"不合适");
		   			continue;
		   		   }
		   		   String bzzj=map.get("bzbm").toString();//币种主键
		   		   String wxfph="";//外销发票号
		   		//校验该条单据是否传过
		   		    int billno=checking(djPK,corp);
			   		if(billno == 1){
			   			voBuffer.append(djh+",");
			   			continue;
			   		}
		   	   String bz="select currtypecode from bd_currtype where pk_currtype='"+bzzj+"'";//根据币种主键查出币种代码
		   	   String queryDepit="select deptcode from bd_deptdoc where PK_DEPTDOC='"+depid+"' and canceled='N' and pk_corp='"+corp+"'";//根据部门主键查出部门编码
		   	   String zst=map.get("scomment").toString();//子收条号
		   	   String bzlx = (String) receiving.executeQuery(bz, new ColumnProcessor());//查询币种
		   	   //判断内外销
		   	   if("CNY".equals(bzlx)){
		   		 wxfph=null;//外销单据号
		   	   }else{
		   		 wxfph=map.get("zyx17").toString();
		   		 fph=null;
		   	   }
		   	   String djdm=map2.get("ccreditnum")==null?"fp0001dm":map2.get("ccreditnum").toString().replace(" ", "");//发票代码
	   		   String zrzx = (String) receiving.executeQuery(queryDepit, new ColumnProcessor());//责任中心--部门编码
	   		   String hxrq=map.get("clrq").toString();//分配日期--处理日期
	   		   if(hxrq==null || hxrq.length()==0){
	   			   hxrq=dqsj.substring(0, 10);
	   			   hxrq=hxrq.replaceAll("-", "");
	   		   }else{
	   			 hxrq=hxrq.replace("-", "");
	   		   }
	   		   String h=dqsj.substring(9, 10);//时
	   		   String m=dqsj.substring(11, 12);//分
	   		   String ss=dqsj.substring(13, 14);//秒
	   		  
	   		   String fpsj=addZero(h)+addZero(m)+addZero(ss)+"00";//分配时间-8位精确到毫秒
	   		   String clr=map.get("clr").toString();//处理人
	   		   String fpje=map.get("dfbbwsje").toString();//分配金额
	   		   int hxje=0;//核销金额
	   		   if(fpje=="" || fpje==null){
	   			   fpje="0";
	   		   }else{
	   			   hxje=Integer.valueOf(fpje);//分配金额--核销金额-贷方本币无税金额
	   		   }
	   		   String sfyf=map.get("prepay").toString();//分配用途
	   		   if("Y".equals(sfyf)){
	   			    sfyf="01";
	   		   }else{
	   			    sfyf="99";
	   		   }
	   		   System.out.println("责任中心："+zrzx+"核销日期："+hxrq);
		   		
		   			//XBUS SEND 调用代码  start 
		   			JSONObject val = new JSONObject();
		   			    val.put("SUB_RECEIPT_CODE",zst);//子收条号
		   			    val.put("INVOICE_NO",fph);//发票号
		   			    val.put("INVOICE_CODE",djdm);//发票代码
		   			    val.put("RED_INVOICE_NO",null);//负数发票号
		   			    val.put("RED_INVOICE_CODE",null);//负数发票代码
		   			    val.put("COST_CENTER",zrzx);//责任中心
		   			    val.put("APPORT_DATE",hxrq);//分配日期
		   			    val.put("APPORT_TIME",fpsj);//分配时间
		   			    val.put("APPORT_ID","L00031");//分配人工号
		   			    val.put("APPORT_TYPE","1");//分配类型--默认
		   			    val.put("APPORT_PURPOSE",sfyf);//分配用途
		   			    val.put("APPORT_STATUS","1");//分配状态--默认
		   			    val.put("APPORT_AMT",hxje);//分配金额--核销金额
		   			    val.put("ROLLBK_ID",null);//回退人工号
		   			    val.put("ROLLBK_DATE",null);//回退日期
		   			    val.put("INTEREST_START_DATE",null);//起息日
		   			    val.put("INTEREST_END_DATE",null);//结息日
		   			    val.put("INTEREST_DATE_COUNT","0");//利息天数
		   			    val.put("INTEREST_RATE","0");//利率
		   			    val.put("DISC_AMT","0");//贴息金额
		   			    val.put("F_INVOICE_NO",wxfph);//外销发票号
		   			    val.put("SYSTEM_ID","JC");//系统别
		   			  

//		   			此XML接口电文总长度为:225
//		   			【接口调用示例代码---供参考】
		   			IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
		   			JSONObject s = ifc.assembleItfData(val, "JCRG18",null); 
		   			System.out.println("s的值："+s);
		   			String state = (String) s.get("state"); 
		   			if("success".equals(state)){ 
		   				fszt=0;
		   			    String content = s.getString("content"); 
		   			    JSONArray sjrr =JSONArray.fromObject(content); //s.get("content"); 
		   			    if(sjrr.size()>0){ 
		   			        for (int i = 0; i < sjrr.size(); i++) { 
		   			            JSONObject rjb = (JSONObject) sjrr.get(i); 
		   			            JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
		   			            if("success".equals(rs.getString("state"))){ 
		   			                //成功逻辑 
		   			            	System.out.println("发送成功");
		   			            	inserting(djPK,corp);
		   			            }else{ 
		   			                //失败逻辑 
		   			            	System.out.println("发送失败");
//		   			                showErrorMessage("单据"+fph+"发送失败！！！"+s);
		   			            } 
		   			        } 
		   			    } 
		   			} else{
						  System.out.println("字段有误"+s);
//						  showErrorMessage("字段有误"+s);
						  fszt=1;
					  }
		   		   
		   	   }
		   	 //成功逻辑 
		 	  if(voBuffer==null || voBuffer.length()==0 && fszt!=1){
//		 	      showWarningMessage("所选单据全部发送成功");
		 	      System.out.println("所选单据全部发送成功");
		 	  }else if(voBuffer.length()!=0 && fszt!=1){
//		 		  showWarningMessage("所选单据已发送，其中单据"+voBuffer+"已被传过，无需再传");
		 		  System.out.println("所选单据已发送，其中单据"+voBuffer+"已被传过，无需再传");
		 	    }
		      }else{
		    	  System.out.println("此期间无核销单据");
		      }
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
    } 
		
		
		return "执行完成";
}

	public Object implementReturnObject(Key[] arg0, String arg1, UFDate arg2)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean implementWriteFile(Key[] arg0, String arg1, String arg2,
			UFDate arg3) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}}
