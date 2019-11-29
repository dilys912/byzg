package nc.impl.ws.baoyin;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.lock.PKLock;
import nc.bs.uap.oid.OidGenerator;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.ws.baoyin.IPDAServiceExecutor;
import nc.jdbc.framework.SQLParameter;
import nc.pub.rino.MapUtil;
import nc.vo.pub.lang.UFDateTime;

/**
 * @author Administrator
 * @date May 11, 2014 10:53:13 AM
 * @type nc.impl.pub.rino.executor.PDAWebServiceExecutor
 * @corporation �Ϻ�����������޹�˾
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
@SuppressWarnings("unchecked")
public class PDAServiceExecutorImpl implements IPDAServiceExecutor {

	private IPubDMO dmo;

	protected IPubDMO getJdbc() {
		if (dmo == null) {
			dmo = (IPubDMO) NCLocator.getInstance().lookup(IPubDMO.class.getName());
		}
		return dmo;
	}

	public Object execute(Object param) throws Exception {
		Date starttime = Calendar.getInstance().getTime();
		Date endtime;
		String ret;
		int times = 0;
		try {
			Map<String, Object> map = new MapUtil().toMap(param.toString());
			String actiontype = (String) map.get("actiontype");
			actiontype = "do" + Character.toUpperCase(actiontype.charAt(0)) + actiontype.substring(1, actiontype.length());
			Method m = getClass().getMethod(actiontype, Map.class);
			ret = (String) m.invoke(this, map);
		} catch (Exception ex) {
			org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getClass());
			logger.error(ex);
			Logger.error(ex);
			ret = "ERR:" + (ex.getCause() == null ? (ex.getMessage() == null ? "NULL" : ex.getMessage().replaceAll("<", "[").replaceAll(">", "]")) : (ex.getCause().getMessage() == null ? "NULL" : ex.getCause().getMessage().replaceAll("<", "[").replaceAll(">", "]")));
		} finally {
			PKLock.getInstance().releaseDynamicLocks();
			endtime = Calendar.getInstance().getTime();
			times = new Long((endtime.getTime() - starttime.getTime())).intValue();
		}
		String pk_log = OidGenerator.getInstance().nextOid();
		String sql = "insert into pda_log(pk_log,starttime,param,ret,endtime,times,pk_corp,ts,dr) values(?,?,?,?,?,?,?,?,?)";
		BaseDAO dao = new BaseDAO();
		SQLParameter sqlParam = new SQLParameter();
		sqlParam.addParam(pk_log);
		sqlParam.addParam(new UFDateTime(starttime).toString());
		sqlParam.addParam(param == null ? null : (param.toString().length() > 4000 ? param.toString().substring(0, 3999) : param));
		sqlParam.addParam(ret == null ? null : (ret.toString().length() > 4000 ? ret.toString().substring(0, 3999) : ret));
		sqlParam.addParam(new UFDateTime(endtime).toString());
		sqlParam.addParam(times);
		sqlParam.addParam("0001");
		sqlParam.addParam(new UFDateTime(System.currentTimeMillis()).toString());
		sqlParam.addParam(0);
		dao.executeUpdate(sql, sqlParam);
		return ret;
	}

	/** ��¼У��,��¼�ɹ����ȡδͬ���Ļ������� */
	public String doLogin(Map<String, Object> root) throws Exception {
		return new BDService().login(root);
	}

	/** ��ȡδͬ���Ļ������� */
	public String doUpdateBd(Map<String, Object> root) throws Exception {
		return new BDService().updateBd(root);
	}

	// -------------------------------- ��������
	/** �������߼��鵥 */
	public String doAddXXJY(Map<String, Object> root) throws Exception {
		return new MOService().addXXJY(root);
	}

	// -------------------------------- ��������
	/** ���ӵ������ⵥ **/
	public String doAddDBCK(Map<String, Object> root) throws Exception {
		return new TOService().addDBCK(root);
	}

	/** �޸ĵ������ⵥ */
	public String doEditDBCK(Map<String, Object> root) throws Exception {
		return new TOService().editDBCK(root);
	}

	/** ɾ���������ⵥ */
	public String doDeleteDBCK(Map<String, Object> root) throws Exception {
		return new TOService().deleteDBCK(root);
	}

	/** �޸ĵ�����ⵥ */
	public String doEditDBRK(Map<String, Object> root) throws Exception {
		return new TOService().editDBRK(root);
	}

	/** ɾ��������ⵥ */
	public String doDeleteDBRK(Map<String, Object> root) throws Exception {
		return new TOService().deleteDBRK(root);
	}

	// -------------------------------- ��������
	/** �������۳��ⵥ(�ͻ�) */
	public String doAddXSCK_SH(Map<String, Object> root) throws Exception {
		return new SOService().addXSCK_SH(root);
	}

	/** �޸����۳��ⵥ(�ͻ�) */
	public String doEditXSCK_SH(Map<String, Object> root) throws Exception {
		return new SOService().editXSCK_SH(root);
	}

	/** ɾ�����۳��ⵥ(�ͻ�) */
	public String doDeleteXSCK_SH(Map<String, Object> root) throws Exception {
		return new SOService().deleteXSCK_SH(root);
	}

	/** �������۳��ⵥ(����) */
	public String doAddXSCK_ZT(Map<String, Object> root) throws Exception {
		return new SOService().addXSCK_ZT(root);
	}

	/** �޸����۳��ⵥ(����) */
	public String doEditXSCK_ZT(Map<String, Object> root) throws Exception {
		return new SOService().editXSCK_ZT(root);
	}

	/** ɾ�����۳��ⵥ(����) */
	public String doDeleteXSCK_ZT(Map<String, Object> root) throws Exception {
		return new SOService().deleteXSCK_ZT(root);
	}

	/** ���ݲ�ѯͨ�÷��� */
	public String doQueryBill(Map<String, Object> root) throws Exception {
		String sender = (String) root.get("sender");
		String sql = (String) root.get("sql");
		sql = sql.replaceAll("@����@", ">").replaceAll("@С��@", "<");
		Object cnt = getJdbc().getObject("select count(1) from (" + sql + ") TBL_");
		if (cnt != null && Integer.parseInt(cnt.toString()) > 500) throw new Exception("��ѯ���ݳ���500,������������ѯ����");
		List<Map<String, Object>> data = getJdbc().getMapList(sql);
		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(sender).append("</sender>");
		if (data != null && data.size() > 0) {
			ret.append("<bill>");
			for (Map<String, Object> map : data) {
				ret.append("<body>");
				Iterator<Entry<String, Object>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					ret.append("<").append(entry.getKey()).append(">");
					ret.append(entry.getValue() == null ? "" : entry.getValue().toString());
					ret.append("</").append(entry.getKey()).append(">");
				}
				ret.append("</body>");
			}
			ret.append("</bill>");
		}
		ret.append("</root>");
		return ret.toString();
	}

	/** ִ��SQLͨ�÷��� */
	public String doExecuteSQL(Map<String, Object> root) throws Exception {
		String sender = (String) root.get("sender");
		String sql = (String) root.get("sql");
		sql = sql.replaceAll("@����@", ">").replaceAll("@С��@", "<");
		int count = getJdbc().executeUpdate(sql);
		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(sender).append("</sender>");
		ret.append("<count>").append(count).append("</count>");
		ret.append("</root>");
		return ret.toString();
	}

}
