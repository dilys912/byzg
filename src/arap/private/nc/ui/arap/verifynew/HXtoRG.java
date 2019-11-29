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
 *������������Ϣ����ƶ�ʱ����
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
		  //��ѯ����   add by gt 2019-10-24
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
		  
		  //���뷽�� add by gt 2019-10-24
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
		//���ڹ�˾У��
//	    String corp =arg0[0].getValue() != null ? arg0[0].getValue().toString().trim() : "";//���չ�˾��������
//		if(corp.length()>4){
//			return (new StringBuilder("\u7B2C\u4E00\u4E2A\u53C2\u6570:")).append(corp).toString();
//		}
//		String corp = PoPublicUIClass.getLoginPk_corp();//��ȡ��ǰ��¼��˾
		String corp="1017";
			IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
			Boolean result = idetermineService.check(corp);
			if(result)//�жϵ�ǰ��˾�Ƿ�Ϊ���ڹ�˾������ִ��
		{ 
	  StringBuffer voBuffer=new StringBuffer();//���Ѵ����ĵ��ݵĵ��ݺ�
	  int fszt=0;//���ݷ����Ƿ�ɹ�
	  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	  String dqsj=new UFDateTime(System.currentTimeMillis()).toString();//��ǰϵͳʱ��
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
		   		   String djh=map.get("djbh").toString();//���ݱ��
		   		   String djPK=map.get("vouchid").toString();//��������
		   		   String depid=map.get("deptid")==null?"0":map.get("deptid").toString();//��������
		   		   Map map2=queryFph(djh);
		   		   String fph=map2.get("vdef17")==null?"a":map2.get("vdef17").toString();//��Ʊ��--ֽ�ʷ�Ʊ��
		   		   if(fph == "a" || depid == "0"){
		   			System.out.println("�õ���:"+djh+"������");
		   			continue;
		   		   }
		   		   String bzzj=map.get("bzbm").toString();//��������
		   		   String wxfph="";//������Ʊ��
		   		//У����������Ƿ񴫹�
		   		    int billno=checking(djPK,corp);
			   		if(billno == 1){
			   			voBuffer.append(djh+",");
			   			continue;
			   		}
		   	   String bz="select currtypecode from bd_currtype where pk_currtype='"+bzzj+"'";//���ݱ�������������ִ���
		   	   String queryDepit="select deptcode from bd_deptdoc where PK_DEPTDOC='"+depid+"' and canceled='N' and pk_corp='"+corp+"'";//���ݲ�������������ű���
		   	   String zst=map.get("scomment").toString();//��������
		   	   String bzlx = (String) receiving.executeQuery(bz, new ColumnProcessor());//��ѯ����
		   	   //�ж�������
		   	   if("CNY".equals(bzlx)){
		   		 wxfph=null;//�������ݺ�
		   	   }else{
		   		 wxfph=map.get("zyx17").toString();
		   		 fph=null;
		   	   }
		   	   String djdm=map2.get("ccreditnum")==null?"fp0001dm":map2.get("ccreditnum").toString().replace(" ", "");//��Ʊ����
	   		   String zrzx = (String) receiving.executeQuery(queryDepit, new ColumnProcessor());//��������--���ű���
	   		   String hxrq=map.get("clrq").toString();//��������--��������
	   		   if(hxrq==null || hxrq.length()==0){
	   			   hxrq=dqsj.substring(0, 10);
	   			   hxrq=hxrq.replaceAll("-", "");
	   		   }else{
	   			 hxrq=hxrq.replace("-", "");
	   		   }
	   		   String h=dqsj.substring(9, 10);//ʱ
	   		   String m=dqsj.substring(11, 12);//��
	   		   String ss=dqsj.substring(13, 14);//��
	   		  
	   		   String fpsj=addZero(h)+addZero(m)+addZero(ss)+"00";//����ʱ��-8λ��ȷ������
	   		   String clr=map.get("clr").toString();//������
	   		   String fpje=map.get("dfbbwsje").toString();//������
	   		   int hxje=0;//�������
	   		   if(fpje=="" || fpje==null){
	   			   fpje="0";
	   		   }else{
	   			   hxje=Integer.valueOf(fpje);//������--�������-����������˰���
	   		   }
	   		   String sfyf=map.get("prepay").toString();//������;
	   		   if("Y".equals(sfyf)){
	   			    sfyf="01";
	   		   }else{
	   			    sfyf="99";
	   		   }
	   		   System.out.println("�������ģ�"+zrzx+"�������ڣ�"+hxrq);
		   		
		   			//XBUS SEND ���ô���  start 
		   			JSONObject val = new JSONObject();
		   			    val.put("SUB_RECEIPT_CODE",zst);//��������
		   			    val.put("INVOICE_NO",fph);//��Ʊ��
		   			    val.put("INVOICE_CODE",djdm);//��Ʊ����
		   			    val.put("RED_INVOICE_NO",null);//������Ʊ��
		   			    val.put("RED_INVOICE_CODE",null);//������Ʊ����
		   			    val.put("COST_CENTER",zrzx);//��������
		   			    val.put("APPORT_DATE",hxrq);//��������
		   			    val.put("APPORT_TIME",fpsj);//����ʱ��
		   			    val.put("APPORT_ID","L00031");//�����˹���
		   			    val.put("APPORT_TYPE","1");//��������--Ĭ��
		   			    val.put("APPORT_PURPOSE",sfyf);//������;
		   			    val.put("APPORT_STATUS","1");//����״̬--Ĭ��
		   			    val.put("APPORT_AMT",hxje);//������--�������
		   			    val.put("ROLLBK_ID",null);//�����˹���
		   			    val.put("ROLLBK_DATE",null);//��������
		   			    val.put("INTEREST_START_DATE",null);//��Ϣ��
		   			    val.put("INTEREST_END_DATE",null);//��Ϣ��
		   			    val.put("INTEREST_DATE_COUNT","0");//��Ϣ����
		   			    val.put("INTEREST_RATE","0");//����
		   			    val.put("DISC_AMT","0");//��Ϣ���
		   			    val.put("F_INVOICE_NO",wxfph);//������Ʊ��
		   			    val.put("SYSTEM_ID","JC");//ϵͳ��
		   			  

//		   			��XML�ӿڵ����ܳ���Ϊ:225
//		   			���ӿڵ���ʾ������---���ο���
		   			IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
		   			JSONObject s = ifc.assembleItfData(val, "JCRG18",null); 
		   			System.out.println("s��ֵ��"+s);
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
		   			                //�ɹ��߼� 
		   			            	System.out.println("���ͳɹ�");
		   			            	inserting(djPK,corp);
		   			            }else{ 
		   			                //ʧ���߼� 
		   			            	System.out.println("����ʧ��");
//		   			                showErrorMessage("����"+fph+"����ʧ�ܣ�����"+s);
		   			            } 
		   			        } 
		   			    } 
		   			} else{
						  System.out.println("�ֶ�����"+s);
//						  showErrorMessage("�ֶ�����"+s);
						  fszt=1;
					  }
		   		   
		   	   }
		   	 //�ɹ��߼� 
		 	  if(voBuffer==null || voBuffer.length()==0 && fszt!=1){
//		 	      showWarningMessage("��ѡ����ȫ�����ͳɹ�");
		 	      System.out.println("��ѡ����ȫ�����ͳɹ�");
		 	  }else if(voBuffer.length()!=0 && fszt!=1){
//		 		  showWarningMessage("��ѡ�����ѷ��ͣ����е���"+voBuffer+"�ѱ������������ٴ�");
		 		  System.out.println("��ѡ�����ѷ��ͣ����е���"+voBuffer+"�ѱ������������ٴ�");
		 	    }
		      }else{
		    	  System.out.println("���ڼ��޺�������");
		      }
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
    } 
		
		
		return "ִ�����";
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
