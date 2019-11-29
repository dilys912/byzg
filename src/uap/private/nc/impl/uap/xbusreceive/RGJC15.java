package nc.impl.uap.xbusreceive;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


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
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.bs.pub.pf.PfUtilBO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.uap.xbusreceive.SFKMessageVO;
/**
 * �տ�����ϢӦ��
 *
 */
public class RGJC15  implements IxbusReceive {
	
	public JSONObject exeScript(JSONObject jsonarray) {
			System.out.println("json��ʽ��"+jsonarray);
			JSONObject jsons = jsonarray.getJSONObject("content");
			JSONArray resarr = new JSONArray();//�洢��ִ��Ϣ
			JSONObject msg = new JSONObject();
			JSONObject dj = new JSONObject();
			SFKMessageVO sfkVO=new SFKMessageVO();
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			String itfcorp ="0001";
			String itfbccode = "";
			String itfmsg = "";
			String zzlx = jsons.getString("PROC_TYPE").toString();//R-��壻Z-ת�ˣ�B-�˿
			if(zzlx.equals("R")||zzlx.equals("B")||zzlx.equals("Z")){//�������������ɵġ��˿���ֱ������һ���˿
				DJZBVO djzbvo = new DJZBVO();
				String subcode = jsons.getString("SUB_RECEIPT_CODE");//��������
				itfbccode = subcode;
				UFDouble dealMoney = jsons.get("PROC_AMT")==null?new UFDouble(0.0):new UFDouble(jsons.get("PROC_AMT").toString());//�տ��������
				//��ͷ��VO����
				DJZBHeaderVO djzbHeader = null;
				try {
					djzbHeader = getHeaderVO(subcode);
				} catch (DAOException e1) {
					itfmsg = "ERP��ѯ�����[��������]��"+subcode+"��Ӧ���տ�쳣��"+e1.getMessage();
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				if(djzbHeader==null){
					itfmsg = "ERPδ��ѯ�����[��������]��"+subcode+"��Ӧ���տ��";
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				itfcorp = djzbHeader.getDwbm();
				
				DJZBItemVO items[] = new DJZBItemVO[1];
				DJZBItemVO bodyvo = null;
				try {
					bodyvo = getBodyVO(djzbHeader.getVouchid());
				} catch (DAOException e) {
					itfmsg = "ERPδ��ѯ�����[��������]��"+subcode+"��Ӧ���տ�����쳣��"+e.getMessage();
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				if(bodyvo==null){
					itfmsg ="ERPδ��ѯ�����[��������]��"+subcode+"��Ӧ���տ���壡";
					return returnMsg(false,itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				
				String bzpk = bodyvo.getBzbm()==null?"":bodyvo.getBzbm();//���� 
				if(bzpk.equals("")){
					itfmsg = "δ��ȡ��ԭ���ݣ�"+djzbHeader.getDjbh()+"������Ϣ��";
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				UFDouble rate = bodyvo.getBbhl()==null?new UFDouble(0.0):bodyvo.getBbhl();//����
				if(rate.doubleValue()==0){
					itfmsg = "ERPԭ�������ݣ�"+djzbHeader.getDjbh()+"��Ӧ������ϢΪ0/�գ���ȷ�ϣ�";
					return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
				}
				bodyvo.setVouchid("");
				bodyvo.setFb_oid("");
				
				if(bzpk.equals("00010000000000000001")){//�����
					djzbHeader.setBbje(dealMoney.multiply(-1));//���ҽ��//��ǰ�ҹ����ֽ��
					bodyvo.setYbye(dealMoney.multiply(-1));//ԭ�����
					bodyvo.setBbye(dealMoney.multiply(-1));//�������
					
					bodyvo.setDfbbje(dealMoney.multiply(-1));//�������ҽ��
					bodyvo.setDfybje(dealMoney.multiply(-1));//����ԭ�ҽ�� 
					bodyvo.setDfybwsje(dealMoney.multiply(-1));//����ԭ����˰���
					bodyvo.setDfbbwsje(dealMoney.multiply(-1));//����������˰���
				}else{
					djzbHeader.setBbje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//���ҽ��//��ǰ�ҹ����ֽ��
					bodyvo.setYbye(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//ԭ�����
					bodyvo.setBbye(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//�������
					
					bodyvo.setDfbbje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//�������ҽ��
					bodyvo.setDfybje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//����ԭ�ҽ�� 
					bodyvo.setDfybwsje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//����ԭ����˰���
					bodyvo.setDfbbwsje(dealMoney.multiply(-1).multiply(new UFDouble(rate)));//����������˰���
				}
				
				djzbHeader.setScomment(subcode);
				items[0] = bodyvo;
				djzbHeader.setVouchid("");
				djzbHeader.setDjbh("");
				djzbvo.setParentVO(djzbHeader);
				djzbvo.setChildrenVO(items);
				List sj = saveConfig(djzbvo);
				dj=(JSONObject) sj.get(1);
				sfkVO.setPk_corp(djzbHeader.getDwbm());
				sfkVO.setBcBillno(subcode);
				sfkVO.setErpBillno(sj.get(2).toString());
				sfkVO.setMessage(dj.toString());
				try {
					iVOPersistence.insertVO(sfkVO);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				itfmsg = "ERP�տ���Ϣ���֧�����ͣ�R-��壻B-�˿Z-ת�ˣ�";
				return returnMsg(false, itfmsg, null,null,itfcorp,itfbccode,itfmsg);
			}
			
			System.out.println("������"+dj);
			if (dj.getBoolean("s")) {
				msg.put("SYNSTATUS", 0);// 0���ɹ�������1��ʧ�ܣ�
				msg.put("SYNRESULT", dj.getString("m") );
				
			} else {
				msg.put("SYNSTATUS", 1);// 0���ɹ�������1��ʧ�ܣ�
				msg.put("SYNRESULT", dj.getString("m"));
			}
			resarr.add(msg);
//		}
		return returnMsg(true, "ִ�����", null,resarr.toString(),itfcorp,itfbccode,itfmsg);
	}
	/**
	 * ��װ��ִ
	 * */
	private JSONObject returnMsg(boolean b, String m, String puuid, String d,String corp,String bccode,String msg) {
		JSONObject json = new JSONObject();
		if (b) {
			json.put("s", "success");
		} else {
			json.put("s", "error");
		}
		json.put("m", m);
		json.put("puuid", puuid);
		json.put("d", d);
		
		if(!m.equals("ִ�����")){
			SFKMessageVO sfkVO=new SFKMessageVO();
			IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			
			sfkVO.setPk_corp(corp);
			sfkVO.setBcBillno(bccode);
			sfkVO.setErpBillno("");
			sfkVO.setMessage(msg);
			try {
				iVOPersistence.insertVO(sfkVO);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return json;
	}

	
	/**
	 * ��ѯ���ӱ�����  �ж������Ƿ����  �����ھ��޸�  �����������
	 * @param hvo
	 * @return 
	 */
	public List saveConfig(DJZBVO aggvo){
		List list=new ArrayList();
		JSONObject msg=new JSONObject();
		String djbhERP="";
		String erpDjh="";
		StringBuffer message= new StringBuffer();
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
	 * ͨ��Ӧ�����洢�ı�֧�嵥�м���ȡ��Ӧ�Ŀ��̡������˻���Ϣ����˾
	 * @param bzNo
	 * @return
	 * @throws DAOException
	 *//*
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
	
	
	*//**
	 * ��ȡ��ǰ��˾�տ��������
	 * @param corp
	 * @return
	 * @throws DAOException
	 *//*
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
	
	*//**
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
	}
	
	*/
	
	/**
	 * ��ñ�ͷ��Ϣ��ת������
	 * @param pzh
	 * @return
	 * @throws DAOException
	 */
	public DJZBHeaderVO getHeaderVO(String subcode) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from arap_djzb where scomment = '"+subcode+"' and nvl(dr,0) = 0 and bbje>0 order by ts asc ") ;
		BaseDAO dao = new BaseDAO();
		DJZBHeaderVO vo = (DJZBHeaderVO) dao.executeQuery(sql.toString(), new BeanProcessor(DJZBHeaderVO.class));
		if(vo!=null){
			vo.setBbje(vo.getBbje().multiply(-1));//���ҽ��//��ǰ�ҹ����ֽ��
			vo.setYbje(vo.getYbje().multiply(-1));//ԭ�ҽ��
			return vo;
		}else {
			return null;
		}
		

	}
	
	/**
	 * ��ñ�����Ϣ��ת������
	 * @param pzh
	 * @return
	 * @throws DAOException
	 */
	public DJZBItemVO getBodyVO(String vouchid) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from arap_djfb where vouchid = '"+vouchid+"' and nvl(dr,0) =0 order by ts asc ") ;
		BaseDAO dao = new BaseDAO();
		DJZBItemVO vo = (DJZBItemVO) dao.executeQuery(sql.toString(), new BeanProcessor(DJZBItemVO.class));
		/*if(vo!=null){
//			vo.setDfbbje(vo.getDfbbje().multiply(-1));//�������ҽ��
//			vo.setBbye(vo.getBbye().multiply(-1));//�������//��Ƹ�Ǯ����Ҫ�������
//			vo.setDfybje(vo.getDfybje().multiply(-1));//����ԭ�ҽ��
			return vo;
		}else {
			return null;
		}*/
		return vo;
	}
	
	/**
	 * ��ѯ���ݺ�
	 * @param djbhERP
	 * @return
	 */
	 private  String  queryDjh(String djbhERP){
		  String billno="";
		  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  String check=" select djbh from arap_djzb where vouchid='"+djbhERP+"' and nvl(dr,0)= 0";
	 	  try {
		      billno =  receiving.executeQuery(check, new ColumnProcessor())==null?"":(String)receiving.executeQuery(check, new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return billno;  
	  }
}
