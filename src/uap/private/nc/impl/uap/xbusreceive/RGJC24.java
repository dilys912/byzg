package nc.impl.uap.xbusreceive;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.taskdefs.UpToDate;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.itfcheck.IxbusReceive;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.uap.itfcheck.XmlAggEntity;
import nc.vo.uap.itfcheck.XmlBEntity;
import nc.vo.uap.itfcheck.XmlHEntity;
import nc.vo.uap.xbusreceive.SFKMessageVO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.bs.pub.pf.PfUtilBO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
/**
 * ������Ϣ����Ӧ��
 *
 */
public class RGJC24  implements IxbusReceive {
	
	public JSONObject exeScript(JSONObject jsonarray) {
			System.out.println("json��ʽ��"+jsonarray);
			JSONObject jsons = jsonarray.getJSONObject("content");		
			JSONArray resarr = new JSONArray();//�洢��ִ��Ϣ
			JSONObject msg = new JSONObject();
			DJZBVO djzbvo = new DJZBVO();
			SFKMessageVO sfkVO=new SFKMessageVO();
			StringBuffer message=new StringBuffer();//�洢У����Ϣ
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			String dqsj=new UFDateTime(System.currentTimeMillis()).toString();//ϵͳʱ��
			//��ͷ��VO����
			DJZBHeaderVO djzbHeader = new DJZBHeaderVO();
			djzbHeader.setDr(0);
			djzbHeader.setPzglh(1);
			djzbHeader.setDjdl("fk");
			String bzNo = jsons.getString("BILL_NO");
			String daterq = jsons.getString("VOUCHER_DATE").toString();
			HashMap map;
			try {
				map = getCustInfo(bzNo);
			} catch (DAOException e) {
				message.append("��ѯ�����Ӧ�Ŀ��̼������˻���Ϣ�쳣;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				//sfkVO.setPk_corp("");
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "��ѯ�����Ӧ�Ŀ��̼������˻���Ϣ�쳣", null,null);
			}
			if(map==null){
				
				message.append("δ��ѯ�������Ӧ�Ŀ��̼������˻���Ϣ;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				//sfkVO.setPk_corp("");
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "δ��ѯ�������Ӧ�Ŀ��̼������˻���Ϣ", null,null);
			}
			String corp = map.get("pk_corp")==null?"":map.get("pk_corp").toString();
			if(daterq == null || daterq.length()==0){
				daterq=dqsj.substring(0, 10);
			}else{
				daterq = daterq.substring(0, 4)+"-"+daterq.substring(4, 6)+"-"+daterq.substring(6, 8);
			}
			
			UFDate fkrq = new UFDate(daterq);//����ƾ֤����
			String yearStr = daterq.substring(0, 4);
			String monthStr = daterq.substring(5, 7);
			String kjqj = null;
			
			try {
				kjqj = getPeriodPk(yearStr);
			} catch (DAOException e2) {
				message.append("��ѯERP���û���ڼ䣺"+kjqj+"�쳣"+e2.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "��ѯERP���û���ڼ䣺"+kjqj+"�쳣"+e2.getMessage(), null,null);
			}
			if(kjqj.equals("")){
				message.append("��ҪERP���û���ڼ䣺"+kjqj+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "��ҪERP���û���ڼ䣺"+kjqj, null,null);
			}
			String kjyf = null;
			try {
				kjyf = getPeriodMonthPk(monthStr,kjqj);
			} catch (DAOException e2) {
				message.append("��ѯERP���û���·ݣ�"+yearStr+kjyf+"�쳣"+e2.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "��ѯERP���û���·ݣ�"+yearStr+kjyf+"�쳣"+e2.getMessage(), null,null);
			}
			if(kjyf.equals("")){
				message.append("��ҪERP���û���·ݣ�"+yearStr+kjyf+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "��ҪERP���û���·ݣ�"+yearStr+kjyf, null,null);
			}
			
			
			String ks = map.get("ks")==null?"":map.get("ks").toString();
			String account = map.get("account")==null?"":map.get("account").toString();
			
			djzbHeader.setDjbh(bzNo);//���ݺ� ��֧�嵥��
			djzbHeader.setDjkjnd(String.valueOf(fkrq.getYear()));
			djzbHeader.setDjkjqj(String.valueOf(fkrq.getMonth()));
			djzbHeader.setDjlxbm("D3");
			djzbHeader.setQcbz(new UFBoolean(false));
			String ywbm;
			try {
				ywbm = getYwbm(corp);
			} catch (DAOException e) {
				message.append("��ѯ��������쳣��"+e.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "��ѯ��������쳣��"+e.getMessage(), null,null);
			}
			if(ywbm.equals("")){
				message.append("ERPδ��ѯ����˾������"+corp+"��Ӧ���տҵ������;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERPδ��ѯ����˾������"+corp+"��Ӧ���տҵ������", null,null);
			}
			djzbHeader.setYwbm(ywbm);//�������� //��Ҫ���ݹ�˾����ѯ�õ���Ӧ�Ĺ�˾�ĸ������
			djzbHeader.setDjrq(fkrq);//��������
			djzbHeader.setDwbm(corp);//��˾ //������ִ����ĸ���˾����Ҫ��ѯmdm�ַ����Ӧ�Ĺ�˾
			
			djzbHeader.setEffectdate(fkrq);//��Ч����
			djzbHeader.setKsbm_cl(ks);//��Ӧ�� 
			String bz = jsons.getString("CURRENCY_CODE");
			HashMap bzMap;
			try {
				bzMap = getBzInfo(bz, kjqj,kjyf);
			} catch (DAOException e) {
				message.append("��ѯ����/������Ϣ�쳣;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "��ѯ����/������Ϣ�쳣", null,null);
			}
			if(bzMap==null){
				message.append("δ��ѯ����ǰ����/������Ϣ;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corp);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "δ��ѯ����ǰ����/������Ϣ", null,null);
			}
			String bzpk = bzMap.get("bz").toString();
			String rate = bzMap.get("rate").toString();
			djzbHeader.setBbhl(new UFDouble(rate));//���һ��� // ��Ҫ���ݱ��ֲ�ѯ��ǰ�·ݻ���
			
			djzbHeader.setBzbm(bzpk);//���� //��Ҫmdm�ַ���ת��
//			djzbHeader.setWldx(Integer.valueOf(jsons.getString("DESC6")));//��������
			//�����VO����
			DJZBItemVO djzbItemVO = new DJZBItemVO();
			djzbItemVO.setDwbm(corp);//��˾
			djzbItemVO.setFx(1);//����
			djzbItemVO.setWldx(new Integer(1));//�������� 1=��Ӧ��
			djzbItemVO.setBbhl(new UFDouble(rate));//���һ��� // ��Ҫ���ݱ��ֲ�ѯ��ǰ�·ݻ���
			djzbItemVO.setBzbm(bzpk);//���� //��Ҫmdm�ַ���ת��
			if(bzpk.equals("00010000000000000001")){//�����
				djzbHeader.setBbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//���ҽ��//��ǰ�ҹ����ֽ��
				djzbHeader.setYbje(new UFDouble(jsons.getString("PAY_AMOUNT")));//ԭ�ҽ��
				
				djzbItemVO.setBbye(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//�������
				djzbItemVO.setYbye(new UFDouble(jsons.getString("PAY_AMOUNT")));//ԭ�����
				djzbItemVO.setJfbbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//�跽���ҽ��
				djzbItemVO.setJfybje(new UFDouble(jsons.getString("PAY_AMOUNT")));//�跽ԭ�ҽ��
				djzbItemVO.setJfybwsje(new UFDouble(jsons.getString("PAY_AMOUNT")));//�跽ԭ����˰���
				djzbItemVO.setWbfbbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//�跽������˰���
//				djzbItemVO.setJfybwsje(new UFDouble(jsons.getString("PAY_AMOUNT")));//�跽ԭ����˰���
//				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAY_AMOUNT")));//������� ��Ҫ�����˻����ȡ��
//				djzbItemVO.setBbye(new UFDouble(jsons.getString("CHALK_RMB_AMT")));//���ҽ��
			}else{
				djzbHeader.setBbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")).multiply(new UFDouble(rate)));//���ҽ��//��ǰ�ҹ����ֽ��
				djzbHeader.setYbje(new UFDouble(jsons.getString("PAY_AMOUNT")));//ԭ�ҽ��
				
				djzbItemVO.setBbye(new UFDouble(jsons.getString("CHALK_RMB_AMT")).multiply(new UFDouble(rate)));//�������
				djzbItemVO.setYbye(new UFDouble(jsons.getString("PAY_AMOUNT")));//ԭ�����
				djzbItemVO.setJfbbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")).multiply(new UFDouble(rate)));//�跽���ҽ��
				djzbItemVO.setJfybje(new UFDouble(jsons.getString("PAY_AMOUNT")));//�跽ԭ�ҽ��
				djzbItemVO.setJfybwsje(new UFDouble(jsons.getString("PAY_AMOUNT")));//�跽ԭ����˰���
				djzbItemVO.setWbfbbje(new UFDouble(jsons.getString("CHALK_RMB_AMT")).multiply(new UFDouble(rate)));//�跽������˰���
			}
			
			djzbItemVO.setQxrq(fkrq);//��Ч����
			djzbItemVO.setKsbm_cl(ks);//��Ӧ�� 
//			djzbItemVO.setKslb(Integer.valueOf(jsons.getString("DESC17")));//��˰���
			djzbItemVO.setBilldate(fkrq);
			
//			try {
//				String accid = getCustBankId(ks,corp);
//				djzbItemVO.setAccountid(accid);
//			} catch (DAOException e) {
//				return returnMsg(false, "��ѯ�����˻���Ϣ�쳣"+e.getMessage(), null,null);
//			}
			DJZBItemVO items[] = new DJZBItemVO[1];
			items[0] =djzbItemVO;
			djzbvo.setParentVO(djzbHeader);
			djzbvo.setChildrenVO(items);
			List sj = saveConfig(djzbvo,message);
			JSONObject dj=(JSONObject) sj.get(1);
			System.out.println("������"+dj);
			if (dj.getBoolean("s")) {
				message.append("������Ϣ���;");
				msg.put("SYNSTATUS", 0);// 0���ɹ�������1��ʧ�ܣ�
				msg.put("SYNRESULT", dj.getString("m") );
				
			} else {
				msg.put("SYNSTATUS", 1);// 0���ɹ�������1��ʧ�ܣ�
				msg.put("SYNRESULT", dj.getString("m"));
			}
			resarr.add(msg);
//		}
			sfkVO.setDr("0");
			sfkVO.setTs(daterq);
			sfkVO.setPk_corp(corp);
			sfkVO.setBcBillno(bzNo);
			sfkVO.setErpBillno(sj.get(2).toString());
			sfkVO.setMessage(sj.get(0).toString());
			try {
				iVOPersistence.insertVO(sfkVO);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return returnMsg(true, "ִ�����", null,resarr.toString());
	}
	/**
	 * ��װ��ִ
	 * */
	private JSONObject returnMsg(boolean b, String m, String puuid, String d) {
		JSONObject json = new JSONObject();
		if (b) {
			json.put("s", "success");
		} else {
			json.put("s", "error");
		}
		json.put("m", m);
		json.put("puuid", puuid);
		json.put("d", d);
		return json;
	}

	
	/**
	 * ��ѯ���ӱ�����  �ж������Ƿ����  �����ھ��޸�  �����������
	 * @param hvo
	 * @return 
	 */
	public List saveConfig(DJZBVO aggvo,StringBuffer message){
		List list=new ArrayList();
		JSONObject msg=new JSONObject();
		String djbhERP="";
		String erpDjh="";
		if(aggvo==null){
			message.append("�洢������Ϣ��δ��ȡ������;");
			msg=exeReMsg(false,"�洢������Ϣ��δ��ȡ������");
			djbhERP="";
			list.add(message);
			list.add(msg);
			list.add(djbhERP);
			return list;
		}
		DJZBHeaderVO hvo = (DJZBHeaderVO) aggvo.getParentVO();
		DJZBItemVO[] bvos = (DJZBItemVO[]) aggvo.getChildrenVO();
		
		try {
			//ֻ��Ҫ��������
			Object djh=new PfUtilBO().processAction("SAVE", "D3", new UFDate(new Date()).toString(), null, aggvo, null);
			if(djh != null){
				//[0001AA100000000N4THD, nc.vo.ep.dj.DJZBVO@cdf81c]
			     djbhERP=djh.toString().replace("[", "");
			     int num=djbhERP.indexOf(",");
			     djbhERP=djbhERP.substring(0, num);
			     erpDjh=queryDjh(djbhERP);
			}else {
				erpDjh="";
			}
		} catch (Exception e) {
			message.append("�����տ�쳣:"+e.getMessage()+";");
			msg= exeReMsg(false,"�����տ�쳣:"+e.getMessage());
			list.add(message);
			list.add(msg);
			list.add(djbhERP);
			return list;
		}
		
		msg=exeReMsg(true,"������Ϣ��ɣ�");
		list.add(message);
		list.add(msg);
		list.add(erpDjh);
		return list;
			
	}
	
	private  String  queryDjh(String djbhERP){
		  String billno="";
		  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  String check=" select djbh from arap_djzb where vouchid='"+djbhERP+"' and nvl(dr,0)= 0";
	 	  try {
		      billno = (String) receiving.executeQuery(check, new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billno;  
	  }
	private JSONObject exeReMsg(boolean s, String m) {
		JSONObject j = new JSONObject();
		if (s) {
			j.put("s", true);
		} else {
			j.put("s", false);
		}
		j.put("m", m);
		return j;
	}
	
	/**
	 * �ж��ַ�String
	 * */
	private String strFieldHas(JSONObject j, String f) {
		String val = null;
		if (j.has(f)) {
			val = j.getString(f);
		}
		return val;
	}

	/**
	 * �ж��ַ�Integer
	 * */
	private Integer intFieldHas(JSONObject j, String f) {
		Integer val = null;
		if (j.has(f)) {
			val = Integer.valueOf(j.getString(f));
		}
		return val;
	}
	
	/**
	 * �ж��ַ�UFdouble
	 * */

	private UFDouble UfdFieldHas(JSONObject j, String f) {
		UFDouble ufd = new UFDouble();
		if (j.has(f)) {
			ufd = new UFDouble(j.getString(f));
		}
		return ufd;
	}
	
	
	/**
	 * ͨ��Ӧ�����洢�ı�֧�嵥�м���ȡ��Ӧ�Ŀ��̡������˻���Ϣ����˾
	 * @param bzNo
	 * @return
	 * @throws DAOException
	 */
	private HashMap getCustInfo(String bzNo) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_corp, ksbm_cl as ks, gys_account as account ") 
		.append("   from itf_arap_bz bz ") 
		.append("  where billno_bz = '"+bzNo+"' ") 
		.append("    and nvl(dr, 0) = 0 order by ts desc ") ;
		BaseDAO dao = new BaseDAO();
		HashMap map = (HashMap) dao.executeQuery(sql.toString(),new MapProcessor());
		return map;
	}
	
	
	/**
	 * ��ȡ��ǰ��˾�����������
	 * @param corp
	 * @return
	 * @throws DAOException
	 */
	private String getYwbm(String corp) throws DAOException{
		BaseDAO dao = new BaseDAO();	
		StringBuffer sql = new StringBuffer();
		sql.append(" select djlxoid  from arap_djlx ") 
		.append(" where djlxjc = '���' ") 
		.append(" and dwbm = '"+corp+"' ") 
		.append(" and nvl(dr,0) = 0 ");
		Object djlx = dao.executeQuery(sql.toString(),new ColumnProcessor());
		String djlxStr = djlx==null?"":djlx.toString();
		return djlxStr;
	}
	
	/**
	 * ��ȡ������������һ���
	 * @param bz
	 * @return
	 * @throws DAOException 
	 *//*
	private HashMap getBzInfo(String bz,UFDate date,String corp) throws DAOException{
		HashMap map = new HashMap();
		
		if(bz.equals("CNY")){//�����Ĭ��Ϊ����1
			map.put("bz", "00010000000000000001");
			map.put("rate", "1.00000");
		}else{
			String datestr = date.getYear()+"-"+date.getMonth();
			StringBuffer sql = new StringBuffer();
			sql.append(" select cur.rate, cur.pk_currinfo ") 
			.append("   from bd_currinfo info, bd_currrate cur ") 
			.append("  where info.pk_currinfo = cur.pk_currinfo ") 
			.append("    and info.pk_currtype = '00010000000000000001' ") 
			.append("    and info.oppcurrtype = ") 
			.append("        (select pk_currtype from bd_currtype where currtypecode = '"+bz+"') ") 
			.append("    and cur.ratedate like '"+datestr+"%' ") 
			.append("    and nvl(info.dr, 0) = 0 ") 
			.append("    and nvl(cur.dr, 0) = 0 ") 
			.append("    and info.pk_corp = '"+corp+"' ") ;

			BaseDAO dao = new BaseDAO();
			map = (HashMap) dao.executeQuery(sql.toString(),new MapProcessor());
			if(map!=null){	
				if(map.get("bz")==null){
					return null;
				}else{
					String bzpk = map.get("bz").toString();
					if(bzpk.equals("00010000000000000001")){//����һ���Ϊ1
						map.put("rate", "1.00000");
					}else{
						return map;
					}
				}
			}
		}
		
		return map;
	}*/
	
	/**
	 * �����˻���ѯid
	 * @param account
	 * @return
	 * @throws DAOException 
	 */
	public String getAccid(String account,String pk_corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_accid ") 
		.append("   from bd_accid ") 
		.append("  where accidcode = '"+account+"' ") 
		.append("    and pk_corp = '"+pk_corp+"' ") ;
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(sql.toString(),new ColumnProcessor());
		if(obj!=null){
			return obj.toString();
		}else{
			return "";
		}

	}
	
	/**
	 * ��ȡ������������һ���
	 * @param bz
	 * @return
	 * @throws DAOException 
	 */
	private HashMap getBzInfo(String bz,String kjqj,String kjyf) throws DAOException{
		HashMap map = new HashMap();
		
		if(bz.equals("CNY")){//�����Ĭ��Ϊ����1
			map.put("bz", "00010000000000000001");
			map.put("rate", "1.00000");
		}else{
			StringBuffer sql = new StringBuffer();
			sql.append(" select adj.adjustrate as rate,info.oppcurrtype as bz") 
			.append("   from bd_currinfo info, bd_adjustrate adj ") 
			.append("  where info.pk_currinfo = adj.pk_currinfo ") 
			.append("    and info.pk_currtype = '00010000000000000001' ") 
			.append("    and info.oppcurrtype = ") 
			.append("        (select pk_currtype from bd_currtype where currtypecode = '"+bz+"') ") 
			.append("    and adj.pk_accperiod = '"+kjqj+"' ") 
			.append("    and adj.pk_accperiodmonth = '"+kjyf+"' ") 
			.append("    and nvl(info.dr, 0) = 0 ") 
			.append("    and nvl(adj.dr, 0) = 0 ") ;

			BaseDAO dao = new BaseDAO();
			map = (HashMap) dao.executeQuery(sql.toString(),new MapProcessor());
			if(map!=null){	
				if(map.get("bz")==null){
					return null;
				}else{
					String bzpk = map.get("bz").toString();
					if(bzpk.equals("00010000000000000001")){//����һ���Ϊ1
						map.put("rate", "1.00000");
					}else{
						return map;
					}
				}
			}
		}
		
		return map;
	}
	
	/**
	 * ��ȡ����ڼ�����
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String getPeriodPk(String year) throws DAOException{
		
		BaseDAO dao = new BaseDAO();
		String sql = "select pk_accperiod from bd_accperiod where periodyear = '"+year+"'";
		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkacc = obj==null?"":obj.toString();
		
		return pkacc;		
	}
	
	/**
	 * ��ȡ����·�����
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String getPeriodMonthPk(String month,String pkacc) throws DAOException{
		BaseDAO dao = new BaseDAO();
		String sql = "select pk_accperiodmonth from bd_accperiodmonth where month = '"+month+"' and pk_accperiod = '"+pkacc+"'";
		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkmonth = obj==null?"":obj.toString();
		
		return pkmonth;		
	}
	
	
	/**
	 * �����˻���ѯid
	 * @param account
	 * @return
	 * @throws DAOException 
	 */
	public String getCustBankId(String ksid,String corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append("  ") 
		.append(" select cub.account ") 
		.append("   from bd_cumandoc cum ") 
		.append("  inner join bd_cubasdoc bas on bas.pk_cubasdoc = cum.pk_cubasdoc ") 
		.append("  inner join bd_custbank cub on bas.pk_cubasdoc = cub.pk_cubasdoc ") 
		.append("  where cum.pk_cumandoc = '"+ksid+"' ") 
		.append("    and cum.pk_corp = '"+corp+"' ") 
		.append("    and nvl(cum.dr, 0) = 0 ") 
		.append("    and nvl(bas.dr, 0) = 0 ") 
		.append("    and nvl(cub.dr, 0) = 0 ") 
		.append("    and (cum.custflag = '1' OR cum.custflag = '3') ") 
		.append("    and (cum.sealflag is null AND (frozenflag = 'N' or frozenflag is null)) ");

		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(sql.toString(),new ColumnProcessor());
		if(obj!=null){
			return obj.toString();
		}else{
			return "";
		}

	}
}
