package nc.impl.so.service;

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
import nc.itf.scm.so.so001.ISaleOrder;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

import org.apache.commons.lang.StringUtils;

public class CloseSaleOrderImpl implements IHttpServletAdaptor {
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
        String result = closeSaleOrder(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }
	private ClientEnvironment m_ceSingleton;
	   public ClientEnvironment getClientEnvironment()
	    {
	        if(m_ceSingleton == null)
	            m_ceSingleton = ClientEnvironment.getInstance();
	        return m_ceSingleton;
	    }
	IUAPQueryBS dao =(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	public String closeSaleOrder(String json) {
		// TODO Auto-generated method stub
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//��ȡ����Դ
		InvocationInfoProxy.getInstance().setUserDataSource(source);//��������Դ
		
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
		for(int i = 0 ; i <lsmap.size() ; i ++){
			Map mls = lsmap.get(i);
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
				corpList = (List) dao.executeQuery(corpSql, new MapListProcessor());
				if(corpList == null || corpList.size() == 0){
					return "[{\"status\":\"error\",\"message\":\"��˾��NCϵͳ�����ڣ�����\"}]"; 
				}
			} catch (BusinessException e4) {
				e4.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e4.getMessage()+"\"}]";
			}
			Map map2 = (Map) corpList.get(0);
			String pk_corp = map2.get("pk_corp").toString();
			String billcode = mls.get("billcode")==null?"":mls.get("billcode").toString();//����������
			if(StringUtils.isBlank(billcode)){
				return "[{\"status\":\"error\",\"message\":\"���˵���Ϊ��\"}]"; 
			}
			String usercode = mls.get("usercode")==null?"N":mls.get("usercode").toString();
			if(StringUtils.isBlank(usercode)){
				return "[{\"status\":\"error\",\"message\":\"�ص��˱���Ϊ��\"}]";
			}
			String operatid = "";
			try {
				operatid = getUserId(usercode);
			} catch (BusinessException e2) {
				e2.printStackTrace();
				return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e2.getMessage()+"\"}]";
			}
			if(StringUtils.isBlank(operatid)){
				return "[{\"status\":\"error\",\"message\":\"�ص��˱�����NCϵͳ�����ڣ�����\"}]"; 
			}
			String finished = mls.get("finished")==null?"":mls.get("finished").toString();//�����ر�
			if(StringUtils.isBlank(finished)){
				finished = "N";
			}
			String bifreceiptfinish = mls.get("bifreceiptfinish")==null?"":mls.get("bifreceiptfinish").toString();//���������ر�
			if(StringUtils.isBlank(bifreceiptfinish)){
				bifreceiptfinish = "N";
			}
			String bifinventoryfinish = mls.get("bifinventoryfinish")==null?"":mls.get("bifinventoryfinish").toString();//���������ر�
			if(StringUtils.isBlank(bifinventoryfinish)){
				bifinventoryfinish = "N";
			}
			//���������Y����ô���˾ͱ�����Y
			if(bifinventoryfinish.equals("Y")){
				bifreceiptfinish = "Y";
			}
			String bifinvoicefinish = mls.get("bifinvoicefinish")==null?"":mls.get("bifinvoicefinish").toString();//��Ʊ�����ر�
			if(StringUtils.isBlank(bifinvoicefinish)){
				bifinvoicefinish = "N";
			}
			if(finished.equals("Y")){
				SaleOrderVO aggvo = new SaleOrderVO();
				try {
					aggvo = getaggvo(billcode, pk_corp);
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e1.getMessage()+"\"}]";
				}
				
				if(aggvo == null){
					return "[{\"status\":\"error\",\"message\":\"������"+billcode+"��NCϵͳδ�ҵ���ɾ�������鵥��\"}]";
				}
				ArrayList list = null;
				UFDate dbilldate = new UFDate(new Date());
				try {
					InvocationInfoProxy.getInstance().setUserCode(operatid);//�����Ƶ���
					InvocationInfoProxy.getInstance().setCorpCode(pk_corp);//���ù�˾
					list = (ArrayList) new PfUtilBO().processAction("OrderFinish", "30", dbilldate.toString(), null, aggvo, null);
				} catch (Exception e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���˶���"+billcode+"�ر�ʧ��:"+e.getMessage()+"\"}]";
				}
				if(list!=null&&list.size()>0){
					return "[{\"status\":\"success\",\"message\":\"���˶���"+billcode+"�رճɹ�\"}]";
				}
				return "[{\"status\":\"success\",\"message\":\"���˶�����"+billcode+"�رճɹ�\"}]";
			}else{
				SaleOrderVO aggvo = new SaleOrderVO();
				try {
					aggvo = getaggvo(billcode, pk_corp);
				} catch (BusinessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e2.getMessage()+"\"}]";
				}
				if(aggvo == null){
					return "[{\"status\":\"error\",\"message\":\"������"+billcode+"��NCϵͳδ�ҵ���ɾ�������鵥��\"}]";
				}
				SaleorderBVO newbvos[] = new SaleorderBVO[aggvo.getBodyVOs().length];
				SaleorderBVO oldbvos[] = aggvo.getBodyVOs();
				String[] bids = new String[aggvo.getBodyVOs().length];
				for(int h = 0 ; h<oldbvos.length ; h++){
					bids[i] = oldbvos[i].getPrimaryKey();
				}
				for(int k = 0 ; k<oldbvos.length ; k++){
					SaleorderBVO bvo = new SaleorderBVO();
					bvo = oldbvos[k];
					bvo.setFinished(new UFBoolean(finished));
					bvo.setBifreceiptfinish(new UFBoolean(bifreceiptfinish));
					bvo.setBifinventoryfinish(new UFBoolean(bifinventoryfinish));
					bvo.setBifinvoicefinish(new UFBoolean(bifinvoicefinish));
					bvo.setIsDel7D(false);
					bvo.setCoperatorid(operatid);
					String m_headts = "";
					try {
						m_headts = getHeadValue(bvo.getCsaleid());
					} catch (BusinessException e1) {
						e1.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e1.getMessage()+"\"}]";
					}
					bvo.m_headts = new UFDateTime(m_headts);
					String  ts = "";
					try {
						ts = getAnyValue(bvo.getPrimaryKey());
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
					}
					bvo.setExets(new UFDateTime(ts));
					ClientLink clientLink = new ClientLink(pk_corp, operatid, aggvo.getHeadVO().getDbilldate(), null, null, null, null, null, null, false, null, null, null);
					bvo.setClientLink(clientLink);
					newbvos[k] = bvo;
				}
				try {
					ISaleOrder dmo = (ISaleOrder) NCLocator.getInstance().lookup(ISaleOrder.class.getName());
					//SaleOrderDMO dmo = new SaleOrderDMO();
				    dmo.updateOrderEnd(newbvos);
				} catch (Exception e) {
					e.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���˶���"+billcode+"�ر�ʧ��:"+e.getMessage()+"\"}]";
				}
				return "[{\"status\":\"success\",\"message\":\"���˶���"+billcode+"�رճɹ�\"}]";
			}
		}
		return null;
	}

	/**
	 * ��װ���������ۺ�VO
	 * by src 2017��12��11��14:13:17
	 * @throws BusinessException 
	 */
	public SaleOrderVO getaggvo(String billcode,String corp) throws BusinessException{
		SaleOrderVO aggvo = new SaleOrderVO();
		SaleorderHVO hvo = new SaleorderHVO();
		String sql1="select * from so_sale a where a.vreceiptcode = '"+billcode+"' and nvl(a.dr,0)=0 and a.pk_corp='"+corp+"'";
		List<SaleorderHVO> hvoList = null;
		List<SaleorderBVO> bvoList = null;
		hvoList = (List<SaleorderHVO>) dao.executeQuery(sql1, new BeanListProcessor(SaleorderHVO.class));
		if(hvoList.size()>0){
			hvo=(SaleorderHVO) hvoList.get(0);
			String hpk = hvo.getPrimaryKey();
			String sql2="select * from so_saleorder_b a where a.csaleid='"+hpk+"' and nvl(a.dr,0)=0";
			bvoList = (List<SaleorderBVO>) dao.executeQuery(sql2, new BeanListProcessor(SaleorderBVO.class));
			if(bvoList.size()>0){
				ClientLink clientLink = new ClientLink(corp, hvo.getCoperatorid(), hvo.getDbilldate(), null, null, null, null, null, null, false, null, null, null);
				SaleorderBVO bvo[] = new SaleorderBVO[bvoList.size()];
				bvo=bvoList.toArray(new SaleorderBVO[bvoList.size()]);
				for(int j =0 ; j<bvo.length;j++){
					bvo[j].setPkcorp(hvo.getPk_corp());
				}
				aggvo.setIAction(6);
				aggvo.setIsDel7D(false);
				aggvo.setClientLink(clientLink);
				aggvo.setParentVO(hvo);
				aggvo.setChildrenVO(bvo);
				return aggvo;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public String getAnyValue(String value) throws BusinessException
  {
		String sql = "select ts from so_saleexecute where csale_bid in ( '"+value+"' )";
		String ts = "";
		ts = (String) dao.executeQuery(sql, new ColumnProcessor());
		return ts;
  }
	
	 public String getHeadValue(String value) throws BusinessException
	  {
		String sql = "select ts from so_sale where csaleid in ( '"+value+"' )";
		String ts = "";
		ts = (String) dao.executeQuery(sql, new ColumnProcessor());
		return ts;
	  }
	 /**
	   	 *�����Ƶ��˱����ȡ�Ƶ�������
	   	 *by src 2017��12��12��10:58:50
	   	 * @throws BusinessException 
	   	 */
	   	public String getUserId(String usercode) throws BusinessException{
	   		String operateid = "";
	   		String sql = "select cuserid from sm_user where user_code = '"+usercode+"' and nvl(dr,0)=0";
			operateid = (String) dao.executeQuery(sql, new ColumnProcessor());
	   		return operateid;
	   	}
	 	
}
