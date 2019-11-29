package nc.impl.uap.xbusreceive;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.taskdefs.UpToDate;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.itf.pub.rino.IPubDMO;
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
 * �տ���Ϣ����Ӧ��
 *
 */
public class RGJC14  implements IxbusReceive {
	
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
			djzbHeader.setPzglh(0);
			djzbHeader.setDjdl("sk");
			String subcode = jsons.getString("SUB_RECEIPT_CODE");//�������ţ����ں�����
			String bzNo = jsons.getString("RECEIPT_CODE");//�տ�������
			String daterq = jsons.getString("VOUCHER_DATE").toString();
			String corpCode =  jsons.getString("COMPANY_CODE")==null?"":jsons.getString("COMPANY_CODE");//����
			if(corpCode.equals("")){
				message.append("δ��ȡ�����ױ���;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "δ��ȡ�����ױ���", null,null);
			}
			String corpid = null;
			try {
				corpid = getCorp(corpCode);
			} catch (DAOException e1) {
				message.append("ERP��ѯ����"+corpCode+"��Ӧ�Ĺ�˾�쳣��"+e1.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERP��ѯ���׶�Ӧ�Ĺ�˾�쳣��"+e1.getMessage(), null,null);
			}
			if(corpid.equals("")){
				message.append("ERPδ��ѯ����ǰ���ױ��룺"+corpCode+"��Ӧ�Ĺ�˾;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERPδ��ѯ����ǰ���ױ��룺"+corpCode+"��Ӧ�Ĺ�˾", null,null);
			}
			if(daterq == null || daterq.length()==0){
				daterq=dqsj.substring(0, 10);
			}else{
				daterq = daterq.substring(0, 4)+"-"+daterq.substring(4, 6)+"-"+daterq.substring(6, 8);
			}
			UFDate skrq = new UFDate(daterq);//����ƾ֤����
			String yearStr = daterq.substring(0, 4);
			String monthStr = daterq.substring(5, 7);
			String kjqj = null;
			
			try {
				kjqj = getPeriodPk(yearStr);
				
			} catch (DAOException e2) {
				message.append("��ѯERP���û���ڼ䣺"+kjqj+"�쳣"+","+e2.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
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
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
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
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
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
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
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
			
			/*HashMap map;
			try {
				map = getCustInfo(bzNo);
			} catch (DAOException e) {
				return returnMsg(false, "��ѯ�տ��Ӧ�Ŀ��̼������˻���Ϣ�쳣", null,null);
			}
			if(map==null){
				return returnMsg(false, "δ��ѯ���տ��Ӧ�Ŀ��̼������˻���Ϣ", null,null);
			}
			String corp = map.get("pk_corp")==null?"":map.get("pk_corp").toString();
			String ks = map.get("ks")==null?"":map.get("ks").toString();
			String account = map.get("account")==null?"":map.get("account").toString();
			*/
//			djzbHeader.setDjbh(bzNo);//���ݺ� 
			
			djzbHeader.setScomment(subcode);
			djzbHeader.setDjkjnd(String.valueOf(skrq.getYear()));
			djzbHeader.setDjkjqj(String.valueOf(skrq.getMonth()));
			djzbHeader.setDjlxbm("D2");
			djzbHeader.setQcbz(new UFBoolean(false));
			
			String ywbm;
			try {
				ywbm = getYwbm(corpid);
			} catch (DAOException e) {
				message.append("��ѯ�տ���������쳣��"+e.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "��ѯ�տ���������쳣��"+e.getMessage(), null,null);
			}
			if(ywbm.equals("")){
				message.append("ERPδ��ѯ����ǰ���ף�"+corpCode+"��Ӧ���տҵ������;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERPδ��ѯ����ǰ���ף�"+corpCode+"��Ӧ���տҵ������", null,null);
			}
			djzbHeader.setYwbm(ywbm);//�������� //��Ҫ���ݹ�˾����ѯ�õ���Ӧ�Ĺ�˾���տ����
			djzbHeader.setDjrq(skrq);//��������
			djzbHeader.setDwbm(corpid);//��˾
			
//			String voucher_no = jsons.getString("VOUCHER_NO");//ƾ֤��
//			djzbHeader.setVouchertypeno(voucher_no);
			djzbHeader.setEffectdate(skrq);//��Ч����
			String kscode = jsons.getString("SETTLE_USER_CODE")==null?"":jsons.get("SETTLE_USER_CODE").toString();//�����û�����
			if(kscode.equals("")){
				message.append("δ������û��������;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "δ������û��������", null,null);
			}
			String ksid = null;
			try {
				ksid = getKsid(kscode,corpid);//����MDM���̱����ȡ
			} catch (DAOException e) {
				message.append("ERP��ѯ���������쳣��"+e.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "ERP��ѯ���������쳣��"+e.getMessage(), null,null);
			}	
			if(ksid.equals("")){
				message.append("ERPδ��ѯ����ǰ���̱��룺"+kscode+"��Ӧ������������䵽��ǰ��˾"+corpid+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERPδ��ѯ����ǰ���̱��룺"+kscode+"��Ӧ������������䵽��ǰ��˾"+corpid+"", null,null);
			}
			djzbHeader.setKsbm_cl(ksid);//��Ӧ��  //���ݱ�֧�嵥�Ż��Ӧ�����еĿ���
		
			String bankcode = jsons.getString("BANK_CODE")==null?"":jsons.get("BANK_CODE").toString();//�����˻�����
			if(bankcode.equals("")){
				message.append("δ��ȡ�������˻�����;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "δ��ȡ�������˻�����", null,null);
			}
			String accid = null;
			try {
				accid = getAccountid(bankcode,corpid);
			} catch (DAOException e1) {
				message.append("ERP��ѯ�����˻������쳣��"+e1.getMessage()+";");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERP��ѯ�����˻������쳣��"+e1.getMessage(), null,null);
			}
			if(accid.equals("")){
				message.append("ERPδ��ѯ����ǰ�����˻����룺"+bankcode+"��Ӧ������;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "ERPδ��ѯ����ǰ�����˻����룺"+bankcode+"��Ӧ������", null,null);
			}
//			String bankname = jsons.getString("BANK_NAME")==null?"":jsons.get("BANK_NAME").toString();//��������
			djzbHeader.setAccountid(accid);//�����˻� ����
			
			djzbHeader.setYbje(new UFDouble(jsons.getString("PAPER_AMT")));//ԭ�ҽ��	
			String bz = jsons.getString("CURRENCY_CODE");
			HashMap bzMap;
			try {
				bzMap = getBzInfo(bz, kjqj,kjyf);
			} catch (DAOException e) {
				message.append("��ѯ����"+bz+"��Ӧ����һ�����Ϣ�쳣;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				return returnMsg(false, "��ѯ����"+bz+"��Ӧ����һ�����Ϣ�쳣", null,null);
			}
			if(bzMap==null){
				message.append("δ��ѯ��"+yearStr+monthStr+"�ڼ����"+bz+"��Ӧ����һ�����Ϣ;");
				List sj = saveConfig(djzbvo,message);
				sfkVO.setDr("0");
				sfkVO.setTs(daterq);
				sfkVO.setPk_corp(corpid);
				sfkVO.setBcBillno(bzNo);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(sj.get(0).toString());
					try {
							iVOPersistence.insertVO(sfkVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				return returnMsg(false, "δ��ѯ��"+yearStr+monthStr+"�ڼ����"+bz+"��Ӧ����һ�����Ϣ", null,null);
			}
			String bzpk = bzMap.get("bz")==null?"":bzMap.get("bz").toString();
			String rate = bzMap.get("rate")==null?"":bzMap.get("rate").toString();
			djzbHeader.setBbhl(new UFDouble(rate));//���һ��� // ��Ҫ���ݱ��ֲ�ѯ��ǰ�·ݻ���
			
			djzbHeader.setBzbm(bzpk);//���� //��Ҫmdm�ַ���ת��
			//�����VO����
			DJZBItemVO djzbItemVO = new DJZBItemVO();
			djzbItemVO.setDwbm(corpid);//��˾
			djzbItemVO.setFx(-1);//(���ݸ���)����
			djzbItemVO.setWldx(new Integer(0));//�������� 0=�ͻ�
			djzbItemVO.setBbhl(new UFDouble(rate));//���һ��� // ��Ҫ���ݱ��ֲ�ѯ��ǰ�·ݻ���
			djzbItemVO.setKsbm_cl(ksid);
			djzbItemVO.setAccountid(accid);
			djzbItemVO.setBzbm(bzpk);//���� //��Ҫmdm�ַ���ת��
			if(bzpk.equals("00010000000000000001")){//�����
				djzbHeader.setBbje(new UFDouble(jsons.getString("PAPER_AMT")));//���ҽ��//��ǰ�ҹ����ֽ��
				djzbItemVO.setYbye(new UFDouble(jsons.getString("PAPER_AMT")));//ԭ�����
				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAPER_AMT")));//�������
				
				djzbItemVO.setDfbbje(new UFDouble(jsons.getString("PAPER_AMT")));//�������ҽ��
				djzbItemVO.setDfybje(new UFDouble(jsons.getString("PAPER_AMT")));//����ԭ�ҽ�� 
				djzbItemVO.setDfybwsje(new UFDouble(jsons.getString("PAPER_AMT")));//����ԭ����˰���
				djzbItemVO.setDfbbwsje(new UFDouble(jsons.getString("PAPER_AMT")));//����������˰���
				
				
//				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAPER_AMT")));//������� ��Ҫ�����˻����ȡ��
			}else{//������ң����ҽ����Ҫת��
				djzbHeader.setBbje(new UFDouble(jsons.getString("PAPER_AMT")).multiply(new UFDouble(rate)));//���ҽ��//��ǰ�ҹ����ֽ��
				djzbItemVO.setYbye(new UFDouble(jsons.getString("PAPER_AMT")));//ԭ�����
				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAPER_AMT")).multiply(new UFDouble(rate)));//�������
				
				djzbItemVO.setDfbbje(new UFDouble(jsons.getString("PAPER_AMT")).multiply(new UFDouble(rate)));//�������ҽ��
				djzbItemVO.setDfybje(new UFDouble(jsons.getString("PAPER_AMT")));//����ԭ�ҽ��
				djzbItemVO.setDfybwsje(new UFDouble(jsons.getString("PAPER_AMT")));//����ԭ����˰���
				djzbItemVO.setDfbbwsje(new UFDouble(jsons.getString("PAPER_AMT")).multiply(new UFDouble(rate)));//����������˰���
				
//				djzbItemVO.setBbye(new UFDouble(jsons.getString("PAPER_AMT")));//������� 
			}

			
			djzbItemVO.setQxrq(skrq);//��Ч����
			djzbItemVO.setBilldate(skrq);
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
				message.append("�����տ�쳣;");
				msg.put("SYNSTATUS", 1);// 0���ɹ�������1��ʧ�ܣ�
				msg.put("SYNRESULT", dj.getString("m"));
			}
			resarr.add(msg);
//		}
		sfkVO.setDr("0");
		sfkVO.setTs(daterq);
		sfkVO.setPk_corp(corpid);
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
			Object djh=new PfUtilBO().processAction("SAVE", "D2", new UFDate(new Date()).toString(), null, aggvo, null);
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
		//message.append("������Ϣ��ɣ�");
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
		.append("    and nvl(dr, 0) = 0 ") ;
		BaseDAO dao = new BaseDAO();
		HashMap map = (HashMap) dao.executeQuery(sql.toString(),new MapProcessor());
		return map;
	}
	
	
	/**
	 * ��ȡ��ǰ��˾�տ��������
	 * @param corp
	 * @return
	 * @throws DAOException
	 */
	private String getYwbm(String corp) throws DAOException{
		BaseDAO dao = new BaseDAO();	
		StringBuffer sql = new StringBuffer();
		sql.append(" select djlxoid  from arap_djlx ") 
		.append(" where djlxjc = '�տ' ") 
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
	 * �������ױ����ö�Ӧ��˾����
	 * @param corp
	 * @return
	 * @throws DAOException
	 */
	public String getCorp(String corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_corp from bd_corp ") 
		.append(" where def8 = '"+corp+"' ") 
		.append(" and nvl(dr,0) = 0 ") ;
		BaseDAO dao = new BaseDAO();	
		Object corpid = dao.executeQuery(sql.toString(),new ColumnProcessor());
		String corpStr = corpid==null?"":corpid.toString();
		return corpStr;

	}
	
	/**
	 * ���ݼ��ſ��̱��롢��˾���ERP����id
	 * @param kscode
	 * @param corp
	 * @return
	 * @throws DAOException
	 */
	public String getKsid(String kscode,String corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct man.pk_cumandoc as cmanid ") 
		.append("   from bd_cubasdoc  doc ") 
		.append("  left join  bd_cumandoc man  on  doc.pk_cubasdoc=man.pk_cubasdoc ") 
		.append("  where man.PK_CORP = '"+corp+"' ") 
		.append("    and doc.custcode = '"+kscode+"' ") 
		.append("    and nvl(man.dr, 0) = 0 ") 
		.append(" and (man.custflag = '1' OR man.custflag = '3') ") 
		.append("    and nvl(doc.dr, 0) = 0 ") ;
		BaseDAO dao = new BaseDAO();	
		Object ksid = dao.executeQuery(sql.toString(),new ColumnProcessor());
		String ksidStr = ksid==null?"":ksid.toString();
		return ksidStr;

	}
	
	/**
	 * ����MDM�����˺ű�����
	 * @param accode
	 * @return
	 * @throws DAOException
	 */
	public String getAccountid(String accode,String corp) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_accid from bd_accid where def11 = '"+accode+"' and nvl(dr,0) = 0 and pk_corp = '"+corp+"'") ;
		BaseDAO dao = new BaseDAO();	
		Object accid = dao.executeQuery(sql.toString(),new ColumnProcessor());
		String accidStr = accid==null?"":accid.toString();
		return accidStr;

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

	
	
}
