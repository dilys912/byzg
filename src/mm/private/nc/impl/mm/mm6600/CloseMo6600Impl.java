package nc.impl.mm.mm6600;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mm.pub.pub1030.MoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFTime;

import org.apache.commons.lang.StringUtils;

public class CloseMo6600Impl implements IHttpServletAdaptor {
	public void doAction(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		InputStream in = request.getInputStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
        String s = "";  
        StringBuffer sb = new StringBuffer();  
        while ((s = br.readLine()) != null) {  
            sb.append(s);  
        }  
        String result = closeMo6600(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }
	
	IUAPQueryBS uap =(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	public String closeMo6600(String json) {
		// TODO Auto-generated method stub
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//��ȡ����Դ
		InvocationInfoProxy.getInstance().setUserDataSource(source);
		nc.net.sf.json.JSONArray jsonarray = null;
		try {
			jsonarray = new nc.net.sf.json.JSONArray().fromObject(json);
		} catch (Exception e) {
			return "[{\"status\":\"error\",\"message\":\"json��������-����json��ʽ:"+e.getMessage()+"\"}]";
		}
		List<Map> lsmap = new ArrayList<Map>();
		if(jsonarray != null&&jsonarray.size()>0){
			for(int i =0;i<jsonarray.size();i++){
				Map map = new HashMap();
				nc.net.sf.json.JSONObject jsonobject = jsonarray.getJSONObject(i);
				String key = null;
				String value = null;
				Iterator it = jsonobject.keys();
				while(it.hasNext()){
					key = (String) it.next();
					value = jsonobject.getString(key);
					map.put(key, value);
				}
				lsmap.add(map);
			}
		}
		for(int j = 0;j<lsmap.size();j++){
			Map mls = lsmap.get(j);
			String username=mls.get("username")==null?"":mls.get("username").toString();//�û���
			if(StringUtils.isNotBlank(username)){
				if(!username.equals("baosteel")){
					return "[{\"status\":\"error\",\"message\":\"�û�������\"}]"; 
				}
			} else {
				return "[{\"status\":\"error\",\"message\":\"�û���Ϊ��\"}]"; 
			}
			String pwd=mls.get("password")==null?"":mls.get("password").toString();//����
			if(StringUtils.isNotBlank(pwd)){
				if(!pwd.equals("123456")){
					return "[{\"status\":\"error\",\"message\":\"�������\"}]"; 
				}
			} else {
				return "[{\"status\":\"error\",\"message\":\"����Ϊ��\"}]"; 
			}
			String applyCorp=mls.get("corp")==null?"":mls.get("corp").toString();//���빫˾
			if(StringUtils.isBlank(applyCorp)){
				return "[{\"status\":\"error\",\"message\":\"��˾Ϊ��\"}]"; 
			}
			String corpSql = "select a.pk_corp from bd_corp a where a.unitcode = '"+applyCorp+"' and nvl(a.dr,0)=0";
			List corpList = null;
			try {
				corpList = (List) uap.executeQuery(corpSql, new MapListProcessor());
				if(corpList == null || corpList.size() == 0){
					return "[{\"status\":\"error\",\"message\":\"��˾��NCϵͳ�����ڣ�����\"}]"; 
				}
			} catch (BusinessException e4) {
				e4.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e4.getMessage()+"\"}]";
			}
			Map map = (Map) corpList.get(0);
			String pk_corp = map.get("pk_corp").toString();
			String billcode = mls.get("billcode")==null?"":mls.get("billcode").toString();//����������
			if(StringUtils.isBlank(billcode)){
				return "[{\"status\":\"error\",\"message\":\"����������Ϊ��\"}]"; 
			}
			String usercode = mls.get("usercode")==null?"":mls.get("usercode").toString();//�Ƶ��˱���
			if(StringUtils.isBlank(usercode)){
				return "[{\"status\":\"error\",\"message\":\"�Ƶ��˱���Ϊ��\"}]";
			}
			String operatid = "";
			try {
				operatid = getUserId(usercode);
			} catch (BusinessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e2.getMessage()+"\"}]";
			}
			if(StringUtils.isBlank(operatid)){
				return "[{\"status\":\"error\",\"message\":\"�ص��˱�����NCϵͳ�����ڣ�����\"}]"; 
			}
			MoVO aggvo = null;;
			try {
				aggvo = getAggVO(billcode, pk_corp);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
			}
			if(aggvo == null){
				return "[{\"status\":\"error\",\"message\":\"���ݺ�"+billcode+"��NCϵͳδ��ѯ�����ѱ�ɾ��\"}]";
			}
			if(aggvo.getHeadVO().getZt().equals("A")){
				String ctime = new UFDateTime(System.currentTimeMillis())
				.getTime();
				aggvo.getHeadVO().setZt("B");
				aggvo.getHeadVO().setSjkgrq(
				new UFDate(new Date()));
				aggvo.getHeadVO().setSjkssj(ctime);
				aggvo.getHeadVO().setShrid(operatid);
				aggvo.getHeadVO().setShrq(
						new UFDate(new Date()));
				aggvo.getHeadVO().setBusiDate(new UFDate(new Date()));
				try {
					ArrayList list = processAction("MOPUT", "A2", aggvo, pk_corp);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					if(e2 instanceof ClassCastException){
						
					}else{
						return "[{\"status\":\"error\",\"message\":\"��������"+billcode+"�ر�ʧ��:"+e2.getMessage()+"\"}]";
					}
				}
				MoVO aggvoFish = null;
				try {
					aggvoFish = getAggVO(billcode, pk_corp);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
				}
				aggvoFish.getHeadVO().setZt("C");
				aggvoFish.getHeadVO().setSjwgrq(
						new UFDate(new Date()));
				UFTime tsjjs = new UFTime(System.currentTimeMillis());
				aggvoFish.getHeadVO().setSjjssj(tsjjs.toString());
				try {
					ArrayList listFish = processAction("FINISH", "A2", aggvoFish, pk_corp);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					if(e1 instanceof ClassCastException){
						
					}else{
						return "[{\"status\":\"error\",\"message\":\"��������"+billcode+"�ر�ʧ��:"+e1.getMessage()+"\"}]";
					}
				}
				MoVO aggvoOver = null;
				try {
					aggvoOver = getAggVO(billcode, pk_corp);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
				}
				aggvoOver.getHeadVO().setZt("D");
				try {
					ArrayList listOver = processAction("OVER", "A2", aggvoOver, pk_corp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(e instanceof ClassCastException){
						
					}else{
						return "[{\"status\":\"error\",\"message\":\"��������"+billcode+"�ر�ʧ��:"+e.getMessage()+"\"}]";
					}
				}
				return "[{\"status\":\"success\",\"message\":\"��������"+billcode+"�رճɹ�\"}]";
			}else if(aggvo.getHeadVO().getZt().equals("B")){
				aggvo.getHeadVO().setZt("C");
				aggvo.getHeadVO().setSjwgrq(
						new UFDate(new Date()));
				UFTime tsjjs = new UFTime(System.currentTimeMillis());
				aggvo.getHeadVO().setSjjssj(tsjjs.toString());
				try {
					ArrayList listFish = processAction("FINISH", "A2", aggvo, pk_corp);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					if(e1 instanceof ClassCastException){
						
					}else{
						return "[{\"status\":\"error\",\"message\":\"��������"+billcode+"�ر�ʧ��:"+e1.getMessage()+"\"}]";
					}
				}
				MoVO aggvoOver = null;;
				try {
					aggvoOver = getAggVO(billcode, pk_corp);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
				}
				aggvoOver.getHeadVO().setZt("D");
				try {
					ArrayList listOver = processAction("OVER", "A2", aggvoOver, pk_corp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(e instanceof ClassCastException){
						
					}else{
						return "[{\"status\":\"error\",\"message\":\"��������"+billcode+"�ر�ʧ��:"+e.getMessage()+"\"}]";
					}
				}
				return "[{\"status\":\"success\",\"message\":\"��������"+billcode+"�رճɹ�\"}]";
			}else if(aggvo.getHeadVO().getZt().equals("C")){
				aggvo.getHeadVO().setZt("D");
				try {
					ArrayList listOver = processAction("OVER", "A2", aggvo, pk_corp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(e instanceof ClassCastException){
						
					}else{
						return "[{\"status\":\"error\",\"message\":\"��������"+billcode+"�ر�ʧ��:"+e.getMessage()+"\"}]";
					}
				}
				return "[{\"status\":\"success\",\"message\":\"��������"+billcode+"�رճɹ�\"}]";
			}else{
				return "[{\"status\":\"error\",\"message\":\"��������"+billcode+"�ѹر�,����ر�\"}]"; 
			}
		}
		return null;
	}
	/**
   	 * �������������ƻ�ȡ����������
   	 * @throws BusinessException 
   	 */
   	public String getScxId(String pk_corp) throws BusinessException{
   		String scxid = "";
   		String sql = "select a.pk_wkid from pd_wk a  where a.pk_corp = '"+pk_corp+"' and a.gzzxmc = '����' and nvl(a.dr,0)=0";
   		scxid = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return scxid;
   	}
    /**
     * ��ȡ���ɶ����ۺ�VO
     * src 2017��11��28��10:46:59
     * @throws BusinessException 
     */
	public MoVO getAggVO(String billcode,String corp) throws BusinessException{
		MoVO aggvo = new MoVO();
		MoHeaderVO hvo = new MoHeaderVO();
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from mm_mo a where a.scddh = '"+billcode+"' and a.pk_corp='"+corp+"' and nvl(a.dr,0)=0");
		List<MoHeaderVO> hvoList = null;
			hvoList = (List) uap.executeQuery(sb.toString(), new BeanListProcessor(MoHeaderVO.class));		
		if(hvoList != null && hvoList.size()>0){
			hvo = hvoList.get(0);
			hvo.setGzzxid(getScxId(corp));
			hvo.setUserid(hvo.getZdrid());
			aggvo.setParentVO(hvo);
			aggvo.setChildrenVO(null);
		}else{
			aggvo=null;
		}
		return aggvo;
	}
	public ArrayList processAction(String actionName,String billtype,MoVO aggvo,String corp) throws Exception{
		ArrayList list = null;
		InvocationInfoProxy.getInstance().setUserCode("0001AA1000000009DL6G");
		InvocationInfoProxy.getInstance().setCorpCode(corp);
		list = (ArrayList) new PfUtilBO().processAction(actionName, billtype, new UFDate(new Date()).toString(), null, aggvo, null);
		return list;
	}
	/**
   	 *�����Ƶ��˱����ȡ�Ƶ�������
   	 *by src 2017��12��12��10:58:50
   	 * @throws BusinessException 
   	 */
   	public String getUserId(String usercode) throws BusinessException{
   		String operateid = "";
   		String sql = "select cuserid from sm_user where user_code = '"+usercode+"' and nvl(dr,0)=0";
		operateid = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return operateid;
   	}
}
