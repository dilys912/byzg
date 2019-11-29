package nc.bs.so.alert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.core.dao.BaseDao;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.bs.pub.pa.html.IAlertMessage2;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

/**
 * @author HK 
 * @date 
 * 功能：账期查询收款提醒
 * 修改原因：账期查询收款提醒
 */
@SuppressWarnings("serial")
public class EndDateAlert implements IBusinessPlugin,IAlertMessage2 {

	//预警类型
	String warntype = "账期查询收款提醒";
	
	String[][] Allbodyvalues;
	
	List Alllist;
	
	public int getImplmentsType() {
		return 3; 
	}

	public Key[] getKeys() {
		return null;
	}

	public String getTypeDescription() {
		return null;
	}

	public String getTypeName() {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public IAlertMessage implementReturnFormatMsg(Key[] keys, String corpPK,UFDate clientLoginDate) throws BusinessException {
		
		if("1078".equals(corpPK)){
		BaseDAO dao = new BaseDAO();
		
		Date nowdate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		UFDate fdate = new UFDate(format.format(nowdate).substring(0, 10));
		
		StringBuffer sqlsb = new StringBuffer();
		sqlsb.append(" select a.vreceiptcode,c.custname,a.dbilldate,nvl(b.acclimit,30) acclimit,a.csaleid ") 
		.append(" from so_saleinvoice a  ") 
		.append(" left join bd_cumandoc b on a.creceiptcorpid = b.pk_cumandoc and nvl(b.dr,0)=0 ") 
		.append(" left join bd_cubasdoc c on b.pk_cubasdoc = c.pk_cubasdoc and nvl(b.dr,0)=0 ") 
		.append(" where nvl(a.dr,0)=0 and a.dbilldate > '"+fdate.getDateBefore(61)+"' ");
		
		Alllist = (List)dao.executeQuery(sqlsb.toString(), new MapListProcessor());
		
		List  list = new ArrayList<Map>();
		if(Alllist != null&&Alllist.size() > 0){
			for (int i = 0; i < Alllist.size(); i++) {
				Map m =(Map) Alllist.get(i);
				UFDate kprq = new UFDate(m.get("dbilldate").toString());//开票日期
				Integer zq = Integer.valueOf(m.get("acclimit").toString());//账期
				Integer qz = Integer.valueOf(keys[0].getValue().toString());//阙值
				UFDate jzrq = kprq.getDateAfter(zq-qz);
				m.put("enddate", jzrq);
				if(fdate.compareTo(jzrq) >= 0){
					String sql = "select *  from arap_djfb where ddlx = '"+m.get("csaleid").toString()+"' and nvl(isverifyfinished,'N') <> 'Y'";
					List a = (List) dao.executeQuery(sql.toString(), new MapListProcessor());
					if(a == null||a.size() <= 0){
						continue;
					}
					list.add(m);
				}
			}
		}
		Alllist = list;
		
		}
		return this;
	}
	

	
	int[] BodyColumnType = new int[]{
			IAlertMessage.TYPE_STRING,
			IAlertMessage.TYPE_STRING,
			IAlertMessage.TYPE_STRING,
			IAlertMessage.TYPE_STRING,
			IAlertMessage.TYPE_STRING
	};

	public String implementReturnMessage(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
		return null;
	}

	private String getStrValue(Object obj) {
		if(obj==null){
			return "";
		}
		return obj.toString();
	}

	public Object implementReturnObject(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
		
		return null;
	}

	public boolean implementWriteFile(Key[] keys, String fileName,
			String corpPK, UFDate clientLoginDate) throws BusinessException {
		
		return false;
	}

	public String[] getBodyFields() {
		
		return new String[]{
				"发票编号",
				"客户名称",
				"开票日期",
				"账期",
				"截止日期"
		};
	}

	public String[][] getBodyValue() {
		Allbodyvalues = new String[Alllist.size()][5];
		for (int i = 0; i < Alllist.size(); i++) {
			Map b = (Map) Alllist.get(i);
			Allbodyvalues[i][0] =  b.get("vreceiptcode").toString();
			Allbodyvalues[i][1] =  b.get("custname").toString();
			Allbodyvalues[i][2] =  b.get("dbilldate").toString();
			Allbodyvalues[i][3] =  b.get("acclimit").toString();
			Allbodyvalues[i][4] =  b.get("enddate").toString();
		}
		if(Alllist.size() <=0){
			return null;
		}
		return Allbodyvalues;
	}

	public float[] getBodyWidths() {
		
//		return new float[]{
//				0.1f,
//				0.2f,
//				0.3f,
//				0.4f,
//				0.5f,
//				0.6f,
//				0.7f,
//				0.8f,
//				0.9f,
//				0.10f,
//				0.10f,
//				0.10f,
//				0.10f,
//				0.10f
//		};
		return null;
	}

	public String[] getBottom() {
		
		return new String[]{
		};
	} 

	public String getTitle() {
		
		return warntype;
	}

	public String[] getTop() {
		
		return new String[]{
				"账期查询收款提醒："
		};
	}

	public int[] getBodyColumnType() {
		
		return BodyColumnType;
	}

	public String getNullPresent() {
		
		return null;
	}

	public String getOmitPresent() {
		
		return null;
	}
	
}
