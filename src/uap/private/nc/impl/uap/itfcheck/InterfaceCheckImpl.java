package nc.impl.uap.itfcheck;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import org.dom4j.DocumentException;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.uap.itfcheck.IInterfaceCheck;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.itfcheck.SENDMESSAGEVO;
import nc.vo.uap.itfcheck.XbusRestRequestEntity;
import nc.vo.uap.itfcheck.XmlAggEntity;
import nc.vo.uap.itfcheck.XmlBEntity;
import nc.vo.uap.itfcheck.XmlHEntity;


/**
 * ��װ����⡢����  ���� ʵ����
 * 
 * */
public class InterfaceCheckImpl implements IInterfaceCheck {

	/**
	 * ��װ
	 * */
	public JSONObject assembleItfData(JSONObject json, String hname,String bname) {
		XmlAggEntity hxml = null;
		XmlAggEntity bxml = null;
		if(!isEmpty(hname)){
			return returnMsg(false, "δ��ȡ���ӿ������ĵ���ţ�����", null);
		}
		//��ȡxml�����ļ�
		ReadItfConfigXml cfgxml = new ReadItfConfigXml();
		XmlAggEntity xmlcfg = null;
		try {
			hxml = cfgxml.getConfigXml(hname);
			if(isEmpty(bname)){
				bxml = cfgxml.getConfigXml(bname);
			}
		} catch (DocumentException e) {
			//e.printStackTrace();
			return returnMsg(false, "��ȡ�ӿ������ļ��쳣������", e.getMessage());
		}
		
		//���������ļ�
		XmlHEntity hhvo = hxml.getParentVO();
		String hdatatype = hhvo.getDatatype();
		ArrayList<XbusRestRequestEntity> istr = null;
		if("JSON".equals(hdatatype)){
			try {
				istr = assembleJson(hxml,json);
			} catch (BusinessException e) {
				//e.printStackTrace();
				return returnMsg(false, "JSON�ӿ�ת��ʧ�ܣ����飡", e.getMessage());
			}
		}else{
			try {
				istr = assembleXbus(hxml,bxml,json);
			} catch (BusinessException e) {
				//e.printStackTrace();
				return returnMsg(false,"XBUS�ӿ�ת��ʧ�ܣ����飡", e.getMessage());
			}
		}
		
		if(istr!=null){
			JSONArray jrr = new JSONArray();
			for (int i = 0; i < istr.size(); i++) {
				XbusRestRequestEntity xe = istr.get(i);
				JSONObject job = new JSONObject();
				job.put("url", xe.getUrl());
				job.put("msgSendTime", xe.getMsgSendTime());
				job.put("msgToken", xe.getMsgToken());
				job.put("password", xe.getPassword());
				job.put("serviceId", xe.getServiceId());
				job.put("sourceAppCode", xe.getSourceAppCode());
				job.put("tempField", xe.getTempField());
				job.put("version", xe.getVersion());
				job.put("msgBody", xe.getMsgBody());
				job.put("version", xe.getVersion());
				jrr.add(job);
			}
			return returnMsg(true,"�ӿ�ת�����", jrr.toString());
		}else{
			return returnMsg(false,"�ӿ�ת��ʧ�ܣ����飡",null);
		}
		
	}

	/**
	 * ���xbus ��ִ�ĵ����ַ���
	 * @return JSONObject
	 * */
	public JSONObject disassembleItfData(String str, String itfcode) {
		// ��ȡxml�����ļ�
		ReadItfConfigXml cfgxml = new ReadItfConfigXml();
		XmlAggEntity xmlcfg = null;
		try {
			xmlcfg = cfgxml.getConfigXml(itfcode);
		} catch (DocumentException e) {
			//e.printStackTrace();
			return returnMsg(false, "��ȡ�ӿ������ļ�" + itfcode + ".xml�쳣�����飡",e.getMessage());
		}
		//�ǿ�У��
		if(!isEmpty(str)){
			return returnMsg(false, "δ��ȡ����Ҫ���������ַ������飡",null);
		}
		
		//edit by zwx 2019-10-10 xbus����ռ�����ֽ�
//		int flen = str.length();
		int flen= str.getBytes().length;
		int xlen = getXbusLen(xmlcfg);
		
		if(xlen!=flen){
			return returnMsg(false, "������ĳ��ȡ�"+flen+"����ӿ����ó��ȡ�"+xlen+"�����ȣ����飡",null);
		}
		
		
		//��ȡ�ַ�
		JSONObject jdata = xbusSubString(xmlcfg, str);
		if(jdata.size()>0){
			return returnMsg(true, "���Ľ������", jdata.toString());
		}else{
			return returnMsg(false, "���Ľ�ȡʧ��", jdata.toString());
		}
	}
	
	/**
	 * ����HTTP����
	 * @param type== JSON/XBUS
	 * */
	public JSONObject sendRequest(JSONObject j, String type,String erpBillCode,String bcBillCode,String corp) {
		//json  -->
		XbusRestRequestEntity xre = new XbusRestRequestEntity();
		xre.setUrl(j.getString("url"));
		xre.setMsgToken(j.getString("msgToken"));
		xre.setPassword(j.getString("password"));
		xre.setServiceId(j.getString("serviceId"));
		xre.setSourceAppCode(j.getString("sourceAppCode"));
		xre.setTempField(j.getString("tempField"));
		xre.setVersion(j.getString("version"));
		xre.setMsgBody(j.getString("msgBody"));
		xre.setMsgSendTime(getServiceTime());
		
		SENDMESSAGEVO svo = null;
		HttpRequest hr = new HttpRequest();
		JSONObject rjson = null;
		if ("JSON".equals(type)) {
			rjson = hr.sendXbus(xre, "JSON");
		} else {
			rjson = hr.sendXbus(xre, "XBUS");
		}
		String s = (String) rjson.get("states");
		if ("success".equals(s)) {
			String message = (String) rjson.get("message");
			String data = (String) rjson.get("data");
			svo = recordInsertVO(xre.getServiceId(), xre.getMsgBody(),new Integer(0), message + data);
			//edit by zwx 2019-11-17 �����ֶ�
//			svo = recordInsertVO(xre.getServiceId(), xre.getMsgBody(),new Integer(0), message + data,corp,erpBillCode,bcBillCode);
			InsertVO(svo);//�洢��¼
			return returnMsg(true, "�����ͳɹ�", message + data);
		} else {
			String message = (String) rjson.get("message");
			String data = (String) rjson.get("data");
			//edit by zwx 2019-11-17 �����ֶ�
			svo = recordInsertVO(xre.getServiceId(), xre.getMsgBody(),new Integer(-1), message + data);
//			svo = recordInsertVO(xre.getServiceId(), xre.getMsgBody(),new Integer(-1), message + data,corp,erpBillCode,bcBillCode);
			InsertVO(svo);
			return returnMsg(false, "������ʧ��", message + data);
		}

	}
	
	/**
	 * �����ݿ�����¼
	 * */
	private void InsertVO(SuperVO svo) {
		BaseDAO base = new BaseDAO();
		try {
			base.insertVO(svo);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param billcode ���ݺ�
	 * @param val ��Ϣ��
	 * @param in 0==�ɹ�   1==ʧ��
	 * @param mess ִ����Ϣ
	 * */
	private SENDMESSAGEVO recordInsertVO(String billcode, String val, Integer in,String mess) {
		long lt = new Date().getTime();
		SENDMESSAGEVO svo = new SENDMESSAGEVO();
		svo.setMessage_body(val);
		svo.setMessage_code(lt+"");
		svo.setMessage_state(in);
		svo.setMessage_group_name(billcode);
		svo.setMessage_operate_version(new UFDouble(0.00));
		svo.setMessage_operate_info(mess);
		svo.setPk_corp("0001");
		svo.setDr(new Integer(0));
		return svo;
	}
	
	/*//edit by zwx 2019-11-17 ���ͼ�¼�����ӹ�˾��ERP���ݺš���Ƶ��ݺ�
	*//**
	 * @param billcode ���ݺ�
	 * @param val ��Ϣ��
	 * @param in 0==�ɹ�   1==ʧ��
	 * @param mess ִ����Ϣ
	 * *//*
	private SENDMESSAGEVO recordInsertVO(String billcode, String val, Integer in,String mess,String corp,String erpCode,String bcCode) {
		long lt = new Date().getTime();
		SENDMESSAGEVO svo = new SENDMESSAGEVO();
		svo.setMessage_body(val);
		svo.setMessage_code(lt+"");
		svo.setMessage_state(in);
		svo.setMessage_group_name(billcode);
		svo.setMessage_operate_version(new UFDouble(0.00));
		svo.setMessage_operate_info(mess);
		//edit by zwx 2019-11-17 �����ֶ�
		svo.setPk_corp(corp);
		svo.setErp_code(erpCode);
		svo.setBc_code(bcCode);
		svo.setDr(new Integer(0));
		return svo;
	}*/





	
	/**
	 * �������ĵ���ȡ�ַ�
	 * */
	private JSONObject xbusSubString(XmlAggEntity xmlcfg,String str) {
		JSONObject json = new JSONObject();
		if(xmlcfg==null){
			return json;
		}
		int sumn = 0;
		XmlBEntity[] xbs = xmlcfg.getChildrenVO();
		if(xbs.length>0){
			for (int i = 0; i < xbs.length; i++) {
				String fcode = xbs[i].getFieldcode();
				int flen = xbs[i].getFieldlength();
				String ftype = xbs[i].getFieldtype();
//				String v = str.substring(0, flen);
//				str = str.substring(flen, str.length());
				//edit by zwx  2019-10-10 ���ճ��Ƚ�ȡ
				byte[] by = str.getBytes();
				String v = getByteCut(by,sumn,flen);				
				//end by zwx
				if("C".equals(ftype)){
					json.put(fcode, v.trim());					
				}else if("N".equals(ftype)){
//					json.put(fcode, v.trim());
					//edit by zwx 2019-10-12 ���ڰ����벻����С��������
					if(v.trim().contains(".")){
						json.put(fcode, v.trim());
					}else{
						//add by zwx 2019-10-11 ����С����λ���ƶ�С����
						Integer precise = xbs[i].getFieldprecise()==null?new Integer(0):xbs[i].getFieldprecise();
						if(precise.intValue()==0){
							json.put(fcode, v.trim());
						}else{
							if(new Integer(v.trim()).intValue()==0){//Ϊ0����ҪС�����ƶ�
								json.put(fcode, v.trim());
							}else{
								StringBuffer plen = new StringBuffer();
								plen.append("1");
								for(int j=0;j<precise.intValue();j++){
									plen.append("0");
								}
								UFDouble afterV = new UFDouble(v.trim()).div(new UFDouble(plen.toString()));
								json.put(fcode, afterV);
							}
							
						}
						//end by zwx
					}
					//end by zwx
					
					
				}else if("D".equals(ftype)){
					json.put(fcode, v.trim());
				}
				sumn= sumn+flen;
			}
		}
		return json;
	}
	
	/**
	 * �����е���ֵ����
	 * */
	private String xbusStrToVal(XmlBEntity b,String s) {
		String vs = "";
		int flen = b.getFieldlength();
		int fpre = b.getFieldprecise();
		String ftype = b.getFieldtype();
		if("N".equals(ftype)){
			String zs = s.substring(0, flen-fpre);
			String xs = s.substring(flen-fpre);
			int izs = Integer.parseInt(zs); 
			int ixs = Integer.parseInt("0."+xs); 
			UFDouble nv = new UFDouble(0);
			nv.add(izs);
			nv.add(ixs);
			vs = nv.toString();
		}else if("D".equals(ftype)){
			DateFormat format1 = new SimpleDateFormat("yyyyMMdd");  
			DateFormat format2= new SimpleDateFormat("yyyy-MM-dd"); 
			try {
				Date d = format1.parse(s);
				vs = format2.format(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if("C".equals(ftype)){
			vs = s;
		}
		
		return vs;
	}
	
	/**
	 * ��ȡ�����ļ��нӿ��ֶ��ܳ���
	 * */
	private int getXbusLen(XmlAggEntity xmlcfg) {
		int sumn = 0;
		if(xmlcfg==null){
			return sumn;
		}
		XmlBEntity[] xbs = xmlcfg.getChildrenVO();
		if(xbs.length>0){
			for (int i = 0; i < xbs.length; i++) {
				int flen = xbs[i].getFieldlength();
				sumn= sumn+flen;
			}
		}
		return sumn;
	}
	
	
	/**
	 * ��ȡ������ʱ��
	 * ΪӦ��ϵͳ������Ϣ��ʱ�䣬��ʽΪYYYYMMDDHHMMSSsss����ȷ�����룬��17λ��
	 * */
	private String getServiceTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String dt =formatter.format(new Date());
		dt = dt.replace("-", "");
		dt = dt.replace("/", "");
		dt = dt.replace(" ", "");
		dt = dt.replace(":", "");
		dt = dt.replace(".", "");
		return dt;
	}
	
	/**
	 * ��װ��ִ
	 * */
	private JSONObject returnMsg(boolean b,String m,String c) {
		JSONObject json = new JSONObject();
		if(b){
			json.put("state", "success");
			json.put("message", m);
			json.put("content", c);
		}else{
			json.put("state", "error");
			json.put("message", m);
			json.put("content", c);
		}
		return json;
	}
	
	/**
	 * ��װ---JSON��ʽ����
	 * @param json 
	 * @param xmlbs 
	 * @return 
	 * @throws BusinessException 
	 * */
	private ArrayList<XbusRestRequestEntity> assembleJson(XmlAggEntity hxml, JSONObject json) throws BusinessException {
		XmlHEntity xmlh = hxml.getParentVO();
		XmlBEntity[] xmlbs = hxml.getChildrenVO();
		ArrayList<XbusRestRequestEntity> rstr = new ArrayList<XbusRestRequestEntity>();
		JSONObject rjson = new JSONObject();
		boolean existbody = false;//�Ƿ��б���
		//��ͷѭ��У��
		for (int i = 0; i < xmlbs.length; i++) {
			XmlBEntity b = xmlbs[i];
			String tab = b.getHeadorbady();
			String field = b.getFieldcode();
			Integer flength = b.getFieldlength();
			Integer fprecise = b.getFieldprecise();//����
			if("H".equals(tab)){
				Object of = json.get(field);
				if(of==null){
					throw new BusinessException("��ͷ�ӿ�ȱ�١�"+field+"���ֶε�ֵ");
				}else{
					String jfval = of.toString();
					String ms = checkDataType(b,jfval);//��������У��
					if(ms!=null){
						throw new BusinessException(ms);
					}
					Integer jfvlen = jfval.length();
					if(jfvlen.compareTo(flength)==1){
						throw new BusinessException("��ͷ�ӿ��ֶΡ�"+field+"�����ȵ�ֵ���ڹ涨����"+flength);
					}
					rjson.put(field, jfval);
				}
			}else{
				existbody = true;
			}
		}
		//����ѭ��У��
		if(existbody){
			JSONArray bodylist = json.getJSONArray("bodylist");
			if (bodylist==null) {
				throw new BusinessException("�ӿ�ȱ�ٱ��塾bodylist����ֵ");
			}
			JSONArray jarr = new JSONArray();
			for (int i = 0; i < bodylist.size(); i++) {
				JSONObject bjson = new JSONObject();
				JSONObject body = bodylist.getJSONObject(i);
				for (int j = 0; j < xmlbs.length; j++) {
					XmlBEntity b = xmlbs[j];
					String tb = b.getHeadorbady();
					String fb = b.getFieldcode();
					Integer fl = b.getFieldlength();
					if("B".equals(tb)){
						Object ojf = body.get(fb);
						if(ojf==null){
							throw new BusinessException("����ӿ�ȱ�١�"+fb+"���ֶε�ֵ");
						}else{
							String jfv = ojf.toString();
							String ms = checkDataType(b,jfv);//��������У��
							if(ms!=null){
								throw new BusinessException(ms);
							}
							Integer jfvlen = jfv.length();
							if(jfvlen.compareTo(fl)==1){
								throw new BusinessException("����ӿ��ֶΡ�"+fb+"�����ȵ�ֵ���ڹ涨����"+jfvlen);
							}
							bjson.put(fb, jfv);
						}
					}
				}
				jarr.add(bjson);
			}
			rjson.put("bodylist", jarr);
		}
		XbusRestRequestEntity he = new XbusRestRequestEntity(xmlh.getItfaddress(), rstr.toString(), null, xmlh.getMsgtoken(), xmlh.getPassword(), xmlh.getServiceid(), xmlh.getSourceappcode(), xmlh.getTempfield(), xmlh.getVersion(), null);
		rstr.add(he);
		return rstr;
	}
	
	/**
	 * ��װ---���ĸ�ʽ����
	 * @param json 
	 * @param xmlbs 
	 * @return 
	 * @throws BusinessException 
	 * */
	private ArrayList<XbusRestRequestEntity> assembleXbus(XmlAggEntity hxml,XmlAggEntity bxml, JSONObject json) throws BusinessException {
		XmlHEntity hxmlh = hxml.getParentVO();
		XmlBEntity[] hxmlbs = hxml.getChildrenVO();
		ArrayList<XbusRestRequestEntity> rstr = new ArrayList<XbusRestRequestEntity>();
		// ��ͷѭ��У��
		StringBuffer hstr = new StringBuffer();
		for (int i = 0; i < hxmlbs.length; i++) {
			XmlBEntity b = hxmlbs[i];
			//String tab = b.getHeadorbady();
			String field = b.getFieldcode();
			String ismustenter = b.getIsMustEnter();
			if("".equals(ismustenter)||ismustenter==null){
				ismustenter = "N";
			}
			String fisnull = b.getIsNull();
			if("".equals(fisnull)||fisnull==null){
				ismustenter = "N";
			}
			Integer flength = b.getFieldlength();
			Integer fprecise = b.getFieldprecise();// ����
			Object of = json.get(field);
			if (of == null) {
				if("Y".equals(ismustenter)){
					throw new BusinessException("��ͷ�ӿ�ȱ�١�" + field + "���ֶε�ֵ");
				}else{
					if("Y".equals(fisnull)){
						String rval = fieldMakeUp(b, "");	
						hstr.append(rval);
					}else{
						throw new BusinessException("��ͷ�ӿڡ�" + field + "������Ϊ��");
					}
				}
			} else {
				String jfval = of.toString();
				String ms = checkDataType(b,jfval);//��������У��
				if(ms!=null){
					throw new BusinessException(ms);
				}
				String rval = fieldMakeUp(b, jfval);
				Integer jfvlen = rval.length();
				if (jfvlen.compareTo(flength) == 1) {
					throw new BusinessException("��ͷ�ӿ��ֶΡ�" + field+ "�����ȵ�ֵ���ڹ涨����" + flength);
				}
				if("N".equals(fisnull)){
					if(jfvlen.intValue()<=0){
						throw new BusinessException("��ͷ�ӿ��ֶΡ�" + field + "������С���㣬�����Ϲ淶");
					}
				}
				hstr.append(rval);
			}
		}
		XbusRestRequestEntity he = new XbusRestRequestEntity(hxmlh.getItfaddress(), hstr.toString(), null, hxmlh.getMsgtoken(), hxmlh.getPassword(), hxmlh.getServiceid(), hxmlh.getSourceappcode(), hxmlh.getTempfield(), hxmlh.getVersion(), null);
		rstr.add(he);
		if (!json.has("bodylist")) {
			if(bxml!=null){
				throw new BusinessException("δ��ȡ��JSON���ݱ���bodylist��Ϣ��������Ϣ��Ҫ��JSONObject.put('bodylist', JSONArray)��������");
			}
			return rstr;
		}else{
			JSONArray bodylist = json.getJSONArray("bodylist");
			if(bxml==null){
				throw new BusinessException("JSON���ݴ��ڱ��壬�������ļ�δ��ȡ�����壬����");
			}
			
			XmlHEntity bxmlh = bxml.getParentVO();
			XmlBEntity[] bxmlbs = bxml.getChildrenVO();
			
			for (int i = 0; i < bodylist.size(); i++) {
				JSONObject bjson = new JSONObject();
				JSONObject body = bodylist.getJSONObject(i);
				StringBuffer bstr = new StringBuffer();
				for (int j = 0; j < bxmlbs.length; j++) {
					XmlBEntity b = bxmlbs[j];
					//String tb = b.getHeadorbady();
					String fb = b.getFieldcode();
					Integer fl = b.getFieldlength();
					Object ojf = body.get(fb);
					if(ojf==null){
						throw new BusinessException("����ӿ�ȱ�١�"+fb+"���ֶε�ֵ");
					}else{
						String jfval = ojf.toString();
						String ms = checkDataType(b,jfval);//��������У��
						if(ms!=null){
							throw new BusinessException(ms);
						}
						String rval = fieldMakeUp(b, jfval);
						
						Integer jfvlen = rval.length();
						if (jfvlen.compareTo(fl) == 1) {
							throw new BusinessException("����ӿ��ֶΡ�"+fb+"�����ȵ�ֵ���ڹ涨����"+jfvlen);
						}
						bstr.append(rval);
					}
				}
				XbusRestRequestEntity be = new XbusRestRequestEntity(bxmlh.getItfaddress(), bstr.toString(), null, bxmlh.getMsgtoken(), bxmlh.getPassword(), bxmlh.getServiceid(), bxmlh.getSourceappcode(), bxmlh.getTempfield(), bxmlh.getVersion(), null);
				rstr.add(be);
			}
			return rstr;
		}
	}
	
	/**
	 * ��������У��
	 * 
	 * */
	private String checkDataType(XmlBEntity b, String jfval) {
		String ms = null;
		String fd = b.getFieldcode();
		String dt = b.getFieldtype();
		if("N".equals(dt)){
			if(!isNum1(jfval)){
				ms = "�ֶΡ�"+fd+"������ΪN(��ֵ)�д��ڷǷ����ݣ�����";
			}else{
				Integer nxs = b.getFieldprecise();//С��
				if(nxs==null){
					nxs = 0;
				}
				Integer nzs = b.getFieldlength()-nxs;//����
				//����С��������λУ��
				String[] ns = jfval.split("\\.");//��С����ָ�
				String zs = ns[0];
				String xs = null;
				if(ns.length>1){
					xs = ns[1];
				}
				//�ж�����
				if(zs!=null){
					if(nzs<zs.length()){
						ms = "�ֶΡ�"+fd+"������ΪN(��ֵ)���������ֳ������ȡ�"+nzs+"��������";
					}else{
						if(xs!=null){
							if(nxs<xs.length()){
								ms = "�ֶΡ�"+fd+"������ΪN(��ֵ)�о��ȳ�����"+nxs+"��λ������";
							}							
						}
					}
				}
			}
			
		}else if("D".equals(dt)){
			if(!isValidDate(jfval)){
				ms = "�ֶΡ�"+fd+"������ΪD(����)�д��ڷǷ����ݣ�����";
			}
		}
		return ms;
	}



	/**
	 * �����ֶβ���
	 * 
	 * */
	private String fieldMakeUp(XmlBEntity b,String val) {
		String ftype = b.getFieldtype();
		Integer flen = b.getFieldlength();
		Integer fprec = b.getFieldprecise();
		//��ֵ����Ϊ��
		if(fprec==null){
			fprec = new Integer(0);
		}
		if("C".equals(ftype)){
			Integer vlen = val.length();
			if(vlen.compareTo(flen)==-1){
				val = makeUpVal(val, flen, "BOTTOM", "SPACE");
			}
		}else if("N".equals(ftype)){
			int zsnum = flen - fprec ;//����λ��= �ֶγ��� - ���ȡ�С��λ���� 
			String[] ns = val.split("\\.");//��С����ָ�
			if(ns.length<2){//����
				String zs = ns[0];
				String zss = new String();
				String xss = new String();
				if(zs!=null&&zs.length()>0){
					if(Integer.valueOf(zs)>0||Integer.valueOf(zs)==0){//add by zsh 2019��10��11�� �ж��Ƿ���������
						 zss = makeUpVal(zs, zsnum, "TOP", "ZERO");
						 xss = makeUpVal("", fprec, "BOTTOM", "ZERO");
						 val = zss+xss;			
						}else{
						xss = makeDownVal(flen,zs);
						val = zss+xss;
						}
				}else{
					zss = makeUpVal(zs, zsnum, "TOP", "ZERO");
					xss = makeUpVal("", fprec, "BOTTOM", "ZERO");
					val = zss+xss;
				}
				
//				val = zss+xss;
			}else{
				String zs = ns[0];
				String xs = ns[1];
				if(Integer.valueOf(zs)>0||Integer.valueOf(zs)==0){//�ж��Ƿ���������
				String zss = makeUpVal(zs, zsnum, "TOP", "ZERO");
				String xss = makeUpVal(xs, fprec, "BOTTOM", "ZERO");
				val = zss+xss;
				}else{
				String zss = makeDownVal(flen,zs+xs);
//				String xss = makeUpVal(xs, fprec, "BOTTOM", "ZERO");
				val = zss;	
				}
			}
		}else if("D".equals(ftype)){
			val = val.replace("/", "");
			val = val.replace("-", "");
			val = val.replace(" ", "");
		}
		return val;
	}
	
	/**
	 * ����/���ո�
	 * len=���󳤶�
	 * act=TOP/BOTTOM   v=space/zero
	 * */
	private String makeUpVal(String val,Integer len,String act,String v) {
//		int vallen = val.length();
		//add by zwx 2019-10-10 ���Ļ�ȡ�ֽڳ���
		int vallen = val.getBytes().length;
		int munum = len-vallen;
		if(munum>0){
			StringBuffer mustr = new StringBuffer();
			if("SPACE".equals(v)){
				for (int i = 0; i < munum; i++) {
					mustr.append(" ");
				}
			}else{
				for (int i = 0; i < munum; i++) {
					mustr.append("0");
				}
			}
			//ǰ��
			if("TOP".equals(act)){
				val = mustr+val;
			}else{
				//��
				val = val+mustr;
			}
		}
		return val;
	}
	
	/**
	 * ��С��������У��
	 * 
	 * */
	private boolean isNum1(String str){
        //��С����
        Pattern pattern = Pattern.compile("^[-+]?[0-9]+(\\.[0-9]+)?$");

        if(pattern.matcher(str).matches()){
            //����
            return true;
        } else {
            //������
            return false;
        }
    }
	
	/**
	 * ������У��
	 * 
	 * */
	private boolean isValidDate(String str) {
		boolean convertSuccess = true;
		SimpleDateFormat format = null;
		int iof = str.indexOf("-");
		if (iof > 1) {
			format = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			format = new SimpleDateFormat("yyyy/MM/dd");
		}
		try {
			// ����lenientΪfalse.
			// ����SimpleDateFormat��ȽϿ��ɵ���֤���ڣ�����2007/02/29�ᱻ���ܣ���ת����2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
			// ���throw java.text.ParseException����NullPointerException����˵����ʽ����
			convertSuccess = false;
		}
		return convertSuccess;
	}
	
    /**
     * �ǿշ�null�ж�
     * @return �ǿ�==true / ��==false
     * */
    private boolean isEmpty(String s) {
		boolean b = false;
		if(s != null && s != ""){
			b = true;
		}
		return b;
	}

    
    
    /**
     * ��ȡ�ֽ�
     * @param bytes
     * @param startIn
     * @param len
     * @return
     */
    public String getByteCut(byte[] bytes,int startIn,int len){
    	byte[] destPos = new byte[len];
    	System.arraycopy(bytes, startIn, destPos, 0, len);
    	return new String(destPos);
    }
    //end by zwx
    /**
	 * add by zsh 
     * ����ǰ��0	 
     * @param bytes
     * @param startIn
     * @param len
     * @return
     */
    private String makeDownVal(int a,String val){
    	int num = val.length();
    	int munum = a - num;
    	StringBuffer mustr = new StringBuffer();
    	for (int i = 0; i < munum; i++) {
			mustr.append("0");
		}
    	String vals = new String();
    	vals ="-"+ mustr;
    	val = vals+val.replace("-", "");
		return val;
    	
    }

	public JSONObject sendRequest(JSONObject rjb, String type) {
		// TODO Auto-generated method stub
		return null;
	}


}