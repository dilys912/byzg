package nc.impl.uap.mdmreceive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.bd.accperiod.IPrivateAccperiodAccessor;
import nc.itf.uap.bd.corp.ICorp;
import nc.itf.uap.itfcheck.IxbusReceive;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.vo.bd.b21.AdjustrateVO;
import nc.vo.bd.b21.CurrinfoExAggVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.bd.b21.CurrrateVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ����MDM���ʴ���ӿ�
 * wy
 * 2019/10/09
 * @author Administrator
 *
 */
public class MDMERP012 implements IxbusReceive{
 	public JSONObject exeScript(JSONObject json) {
		String checkJson = checkJson(json);
		if(checkJson!=null){
			return returnMsg(false,checkJson, null, null);
		}
		JSONArray resarr = new JSONArray();//�洢��ִ��Ϣ
		JSONArray jsonarrays = json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getJSONArray("DATAINFO");
		String puuid = json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getString("PUUID");
		for (int i = 0; i < jsonarrays.size(); i++) {
			JSONObject msg = new JSONObject();
			//�ۺϱ�   CurrinfoExAggVO
			CurrinfoExAggVO aggVO = new CurrinfoExAggVO();
			String tableCodes[] = aggVO.getTableCodes();
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			/*//����CurrinfoVO
			CurrinfoVO infoVO = new CurrinfoVO();
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			String code = obj.getString("CODE").toString();
			String uuid = obj.getString("UUID").toString();
			msg.put("CODE", code);
			msg.put("UUID", uuid);
			infoVO = jsonToInfoVO(infoVO, obj);
			aggVO.setParentVO(infoVO);
			
			AdjustrateVO[] ratearr = new AdjustrateVO[1];//�ڼ����
			AdjustrateVO ratevo = new AdjustrateVO();
			ratevo = jsonToQjRateVO(ratevo, obj);	
			
			//�ӱ�2  CurrrateVO  
			CurrrateVO[] rateVO = new CurrrateVO[jsonarrays.size()];
			for (int j = 0; j < rateVO.length; j++) {
				CurrrateVO rVO = new CurrrateVO();
				rVO = jsonTorateVO(rVO, obj);	
				rateVO[j] = rVO;
			}
			aggVO.setChildrenVO(rateVO);
			aggVO.setTableVO(tableCodes[1], rateVO);*/
//			JSONObject saveConfig = saveConfig(aggVO,tableCodes);
			JSONObject saveConfig = jsonToQjRateVO(obj);
			if(saveConfig.getBoolean("s")){
				msg.put("SYNSTATUS", 0);// 0���ɹ���
				msg.put("SYNRESULT", saveConfig.getString("m"));
			}else {
				msg.put("SYNSTATUS", 1);// 1��ʧ�ܣ�
				msg.put("SYNRESULT", saveConfig.getString("m"));
			}	
			resarr.add(msg);
		}
		return returnMsg(true,"ִ�����",puuid,resarr.toString());
	}
 	
 	/**
 	 * �ж�CODE�Ǵ���  �������޸�  �����������
 	 * @param aggVO
 	 * @param tableCodes
 	 * @return
 	 *//*
	@SuppressWarnings("unchecked")
	public JSONObject saveConfig(CurrinfoExAggVO aggVO,String tableCodes[]){
		if(aggVO == null){
			return exeReMsg(false,"�洢������Ϣ��δ��ȡ������");
		}
		CurrinfoVO hvo = (CurrinfoVO)aggVO.getParentVO();
		CurrrateVO[] bvo = (CurrrateVO[]) aggVO.getTableVO(tableCodes[1]);
		
		String judgeCurr = judgeCurrType(hvo.getPk_currtype());
		if(judgeCurr == null){
			return exeReMsg(false,"�Ҳ�����Դ���֣�");
		}
		hvo.setPk_currtype(judgeCurr);
		String judgeOppCurrType = judgeOppCurrType(hvo.getOppcurrtype());
		if(judgeOppCurrType == null){
			return exeReMsg(false,"�Ҳ�����Ŀ����֣�");
		}
		hvo.setOppcurrtype(judgeOppCurrType);
		
		String code = bvo[0].getCode();
		String sql = "select * from bd_currrate where code = '"+code+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List<CurrrateVO> list = (List) dao.executeQuery(sql.toString(),new BeanListProcessor(CurrrateVO.class));
			if(list.size()==0){
				dao.insertVO(hvo);
				bvo[0].setPk_currinfo(hvo.getPk_currinfo());
				dao.insertVOArrayWithPK(bvo);
				return exeReMsg(true,"������Ϣ��ɣ�");
			}else {
				CurrrateVO cVO = null;
				for (int i = 0; i < list.size(); i++) {
					cVO = list.get(i);
					cVO.setCode(bvo[0].getCode());
					cVO.setRate(bvo[0].getRate());
					cVO.setRatedate(bvo[0].getRatedate());
					cVO.setRatetype(bvo[0].getRatetype());
					cVO.setOpprate(bvo[0].getOpprate());
					dao.updateVO(cVO);
				}
				String pk_currinfo = cVO.getPk_currinfo();				
				//����bd_currrate �е� pk_currinfo ���޸� bd_currinfo �� ����Ϊ pk_currinfo ������
				UpdateDataVO(pk_currinfo,hvo);
				return exeReMsg(true,"������Ϣ��ɣ�");
			}
		} catch (BusinessException e) {
			return exeReMsg(false,"�洢������Ϣ�쳣��"+e.getMessage());
		}
	}
	
	*//**
	 * ��������
	 * @param infoVO
	 * @param obj
	 * @return
	 *//*
	public CurrinfoVO jsonToInfoVO(CurrinfoVO infoVO, JSONObject obj){
		infoVO.setPk_currtype(strFieldHas(obj, "DESC1"));//Դ����
		infoVO.setOppcurrtype(strFieldHas(obj,"DESC2"));//Ŀ�ı���
		infoVO.setDr(intFieldHas(obj,"DESC7"));//
		return infoVO;
	}*/
	
	/**
	 * �ӱ�����  CurrrateVO
	 * @param rateVO
	 * @param obj
	 * @return
	 *//*
	String strFieldHas = null;
	public CurrrateVO jsonTorateVO(CurrrateVO rateVO, JSONObject obj){
		rateVO.setCode(strFieldHas(obj, "CODE"));//����
		strFieldHas = strFieldHas(obj, "DESC3");
		String a = strFieldHas.substring(0,4);	
		String g = strFieldHas.substring(4,6);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nyr = sdf.format(date).substring(8, 10);	
		String d = a+"-"+g+"-"+nyr;
		rateVO.setRatedate(new UFDate(d));//�����·�  DESC3
		rateVO.setRatetype(strFieldHas(obj, "DESC4"));//��������
		rateVO.setRate(new UFDouble(strFieldHas(obj, "DESC5")));//���ʾ��Ǽ��˻���    
		rateVO.setOpprate(new UFDouble(strFieldHas(obj, "DESC6")));//�������
		return rateVO;
	}*/
	
	/**
	 * �ӱ�����  CurrrateVO
	 * @param rateVO
	 * @param obj
	 * @return
	 */
	public JSONObject jsonToQjRateVO(JSONObject obj){
		//����
		CurrinfoVO infoVO = new CurrinfoVO();
		if(strFieldHas(obj, "DESC1")==null||strFieldHas(obj, "DESC1").toString().equals("")){
			return exeReMsg(false,"δ��ȡ��DESC1��Ӧֵ!");
		}
		String pkybz="";
		try {
			pkybz = judgeCurrType(strFieldHas(obj, "DESC1"));
		} catch (DAOException e) {
			return exeReMsg(false,"��ѯDESC1�����쳣"+e.getMessage());
		}
		if(pkybz.equals("")){
			return exeReMsg(false,"δ��ѯ��DESC1����"+strFieldHas(obj, "DESC1")+"������ͬ������");
		}
		infoVO.setPk_currtype(pkybz);//Դ����
		
		if(strFieldHas(obj, "DESC2")==null||strFieldHas(obj, "DESC2").toString().equals("")){
			return exeReMsg(false,"δ��ȡ��DESC2��Ӧֵ!");
		}
		String mdbz="";
		try {
			mdbz = judgeCurrType(strFieldHas(obj, "DESC2"));
		} catch (DAOException e) {
			return exeReMsg(false,"��ѯDESC2�����쳣"+e.getMessage());
		}
		if(mdbz.equals("")){
			return exeReMsg(false,"δ��ѯ��DESC2����"+strFieldHas(obj, "DESC2")+"������ͬ������");
		}
		infoVO.setOppcurrtype(mdbz);//Ŀ�ı���
//		infoVO.setDr(intFieldHas(obj,"DESC7"));//
		
		//�»����ӱ�
		AdjustrateVO rateVO = new AdjustrateVO();
		rateVO.setPk_accperiodscheme("0001AA00000000000001");//����ڼ䷽������
		if(strFieldHas(obj, "DESC5")==null||strFieldHas(obj, "DESC5").toString().equals("")){
			return exeReMsg(false,"δ��ȡ��DESC5��Ӧֵ!");
		}
		if(strFieldHas(obj, "DESC3")==null||strFieldHas(obj, "DESC3").toString().equals("")){
			return exeReMsg(false,"δ��ȡ��DESC3��Ӧֵ!");
		}
		if(strFieldHas(obj, "DESC3").toString().length()<6){
			return exeReMsg(false,"DESC3�����·ݳ��Ȳ���С��6λ��");
		}
		rateVO.setAdjustrate(new UFDouble(strFieldHas(obj, "DESC5")));//����
		rateVO.setPk_corp("0001");//��ʱĬ�ϼ���ͳһ����
		String dateStr = strFieldHas(obj, "DESC3");
		String yearStr = dateStr.substring(0,4);//��ȡ��
		String monthStr = dateStr.substring(4,6);//��ȡ
		String pkacc="";
		try {
			pkacc = getPeriodPk(yearStr);
		} catch (DAOException e2) {
			return exeReMsg(false,"��ѯ��ǰ��ݶ�Ӧ�Ļ���ڼ��쳣"+e2.getMessage());
		}
		if(pkacc.equals("")){
			return exeReMsg(false,"δ��ѯ����ǰ��ݶ�Ӧ�Ļ���ڼ䣡��ҪERPά��!");
		}
		String pkmonth="";
		try {
			pkmonth = getPeriodMonthPk(monthStr, pkacc);
		} catch (DAOException e1) {
			return exeReMsg(false,"��ѯ��ǰ�·ݶ�Ӧ�Ļ���·��쳣"+e1.getMessage());
		}
		if(pkmonth.equals("")){
			return exeReMsg(false,"δ��ѯ����ǰ�·ݶ�Ӧ�Ļ���·ݣ���ҪERPά��!");
		}
		rateVO.setRatemonth(monthStr);
		rateVO.setPk_accperiod(pkacc);
		rateVO.setPk_accperiodmonth(pkmonth);
		String pkcurrinfo="";
		try {
			pkcurrinfo = getPkCurr(pkybz,mdbz);
		} catch (DAOException e) {
			return exeReMsg(false,"��ѯԴ������Ŀ����ֶ�Ӧ�Ļ�����Ϣ�쳣��"+e.getMessage());
		}
		if(pkcurrinfo.length()>0){
			infoVO.setPk_currinfo(pkcurrinfo);
			rateVO.setPk_currinfo(pkcurrinfo);	
			rateVO.setDr(intFieldHas(obj,"DESC7"));//
			String pkrate;
			try {
				pkrate = isExistRate(rateVO);
			} catch (DAOException e) {
				return exeReMsg(false,"��ѯ�ڼ�����쳣��"+e.getMessage());
			}
			BaseDAO dao = new BaseDAO();
			if(pkrate.length()==0){
				try {
					dao.insertVO(rateVO);
				} catch (DAOException e) {
					return exeReMsg(false,"��������쳣��"+e.getMessage());
				}
			}else{
				rateVO.setPrimaryKey(pkrate);
				try {
					dao.updateVO(rateVO);
				} catch (DAOException e) {
					return exeReMsg(false,"���»����쳣��"+e.getMessage());
				}
			}
		}else{
			return exeReMsg(false,"������ERP����ά��Դ������Ŀ����ֶ�Ӧ�Ĺ�ϵ��");//ֻ������»���
		}
		return exeReMsg(true,"���»��ʳɹ���");		
		
	}

	/**
	 * У��json �ڵ�
	 * */
	private String checkJson(JSONObject json) {
		// check
		if (!json.has("ESB")) {
			return "JSON������δ��ȡ��ESB�ڵ�;";
		}
		if (!json.getJSONObject("ESB").has("DATA")) {
			return "JSON������δ��ȡ��DATA�ڵ�;";
		}
		if (!json.getJSONObject("ESB").getJSONObject("DATA").has("DATAINFOS")) {
			return "JSON������δ��ȡ��DATAINFOS�ڵ�;";
		}
		if (!json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").has("DATAINFO")) {
			return "JSON������δ��ȡ��DATAINFO����;";
		}
		JSONArray jsonarrays = json.getJSONObject("ESB").getJSONObject("DATA").getJSONObject("DATAINFOS").getJSONArray("DATAINFO");
		for (int i = 0; i < jsonarrays.size(); i++) {
			JSONObject obj = JSONObject.fromObject(jsonarrays.get(i));
			if (!obj.has("CODE")) {
				return "JSON������"+(i+1)+"����δ��ȡ��CODE����;";
			}
			if (!obj.has("UUID")) {
				return "JSON������"+(i+1)+"����δ��ȡ��UUID����;";
			}
		}
		return null;
	}
	
	/**
	 * ������Ϣ
	 * @param s
	 * @param m
	 * @return
	 */
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
	 * �޸� bd_currinfo ��Ϣ
	 * @param pk_currinfo
	 * @param cvo
	 * @return
	 */
	public JSONObject UpdateDataVO(String pk_currinfo,CurrinfoVO cvo){
		if(pk_currinfo == null || cvo == null){
			return exeReMsg(false,"�洢������Ϣ��δ��ȡ�����ݣ�");
		}
		String sql = "select * from bd_currinfo where pk_currinfo = '"+pk_currinfo+"'";
		BaseDAO dao = new BaseDAO();
		try {
			List<CurrinfoVO> list = (List) dao.executeQuery(sql.toString(),new BeanListProcessor(CurrinfoVO.class));
			if(list == null){
				return exeReMsg(false,"�洢������Ϣ��δ��ȡ��pk_currinfo���ݣ�");
			}else {
				for (int i = 0; i < list.size(); i++) {
					CurrinfoVO hvo = list.get(i);
					hvo.setPk_currinfo(pk_currinfo);
					hvo.setOppcurrtype(cvo.getOppcurrtype());
					hvo.setPk_currtype(cvo.getPk_currtype());
					hvo.setDr(cvo.getDr());
					dao.updateVO(hvo);
				}
			} 			
		} catch (DAOException e) {
			return exeReMsg(false,"�洢������Ϣ�쳣��"+e.getMessage());
		}
		return null;
	}
	


	/**
	 * �ж�Դ����
	 * @param ybz
	 * @return
	 * @throws DAOException 
	 */
	public String judgeCurrType(String ybz) throws DAOException{
		BaseDAO dao = new BaseDAO();
		String sql = "select pk_currtype from bd_currtype where currtypecode = '"+ybz+"'";
		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkcurtype = obj==null?"":obj.toString();
		
		return pkcurtype;		
	}
	
	/**
	 * �ж�Ŀ�����
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String judgeOppCurrType(String bbbz) throws DAOException{
		
		BaseDAO dao = new BaseDAO();
		String sql = "select pk_currtype from bd_currtype where currtypecode = '"+bbbz+"'";
		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkcurtype = obj==null?"":obj.toString();
		
		return pkcurtype;		
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
	 * ��ȡ����ڼ�����
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
	 * ��ȡԴ���֡�Ŀ����ֶ�Ӧ����
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String getPkCurr(String ysbz,String mbbz) throws DAOException{
		
		BaseDAO dao = new BaseDAO();
		StringBuffer sql = new StringBuffer();
		sql.append("  select pk_currinfo ") 
		.append("           from bd_currinfo ") 
		.append("          where oppcurrtype = '"+mbbz+"' ") 
		.append("            and pk_currtype = '"+ysbz+"' ") 
		.append("            and pk_corp = '0001' ") 
		.append("            and nvl(dr, 0) = 0 ") ;

		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkCurr = obj==null?"":obj.toString();
		
		return pkCurr;		
	}
	
	
	/**
	 * �жϵ��������Ƿ����
	 * @param bbbz
	 * @return
	 * @throws DAOException 
	 */
	public String isExistRate(AdjustrateVO vo) throws DAOException{
		
		BaseDAO dao = new BaseDAO();
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_adjustrate from bd_adjustrate ") 
		.append(" where pk_accperiod = '"+vo.getPk_accperiod()+"' ") 
		.append(" and pk_accperiodmonth = '"+vo.getPk_accperiodmonth()+"' ") 
		.append(" and pk_corp = '0001' ") 
		.append(" and ratemonth = '"+vo.getRatemonth()+"' ") ;


		Object obj = dao .executeQuery(sql.toString(),new ColumnProcessor());
		String pkrate = obj==null?"":obj.toString();
		
		return pkrate;		
	}
	
	
	
}
