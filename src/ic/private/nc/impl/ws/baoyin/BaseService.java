package nc.impl.ws.baoyin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.itf.pub.rino.IPubDMO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.SuperVOGetterSetter;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;

import org.apache.commons.lang.StringUtils;

/**
 * @author Administrator
 * @date May 30, 2014 12:53:54 PM
 * @type nc.ws.baoyin.BaseService
 * @corporation 上海锐鸟软件有限公司
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
public class BaseService {
	private IPubDMO dmo;
	String[] monthArray = new String[] {
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
	};

	protected IPubDMO getJdbc() {
		if (dmo == null) {
			dmo = (IPubDMO) NCLocator.getInstance().lookup(IPubDMO.class.getName());
		}
		return dmo;
	}

	public void setAttrValue(CircularlyAccessibleValueObject vo, String attrName, Object value) {
		if (vo instanceof SuperVO) {
			Method[] ms = SuperVOGetterSetter.getGetMethods(vo.getClass(), new String[] {
				attrName
			});
			Class<?> c = ms[0].getReturnType();
			String valueStr = value == null || StringUtils.isEmpty(value.toString()) ? null : value.toString();
			if (c == Integer.class) vo.setAttributeValue(attrName, valueStr == null ? null : new Integer(valueStr));
			else if (c == UFDouble.class) vo.setAttributeValue(attrName, valueStr == null ? null : new UFDouble(valueStr));
			else if (c == UFBoolean.class) vo.setAttributeValue(attrName, valueStr == null ? null : new UFBoolean(valueStr));
			else if (c == UFDate.class) vo.setAttributeValue(attrName, valueStr == null ? null : new UFDate(valueStr));
			else if (c == UFDateTime.class) vo.setAttributeValue(attrName, valueStr == null ? null : new UFDateTime(valueStr));
			else if (c == UFTime.class) vo.setAttributeValue(attrName, valueStr == null ? null : new UFTime(valueStr));
			else vo.setAttributeValue(attrName, valueStr);
		} else {
			vo.setAttributeValue(attrName, value);
		}
	}

	public String getCK(String name, String pk_corp) throws Exception {
		return (String) getJdbc().getObject("select pk_stordoc from bd_stordoc where nvl(dr,0)=0 and pk_corp='" + pk_corp + "' and storname='" + name + "'");
	}

	public String getCYS(String name) throws Exception {
		return (String) getJdbc().getObject("select A.pk_trancust from dm_trancust A,bd_cubasdoc B where A.pkcusmandoc=B.pk_cubasdoc and B.custname='" + name + "' and nvl(A.dr,0)=0 and nvl(B.dr,0)=0");
	}

	public String getBZ(String name, String pk_corp) throws Exception {
		return (String) getJdbc().getObject("select pk_pgaid from pd_pga where nvl(dr,0)=0 and pk_corp='" + pk_corp + "' and bzbm='" + name + "'");
	}

	public String getHW(String code, String pk_stordoc) throws Exception {
		return (String) getJdbc().getObject("select pk_cargdoc from bd_cargdoc where cscode='" + code + "' and pk_stordoc='" + pk_stordoc + "'");
	}

	public String getCustBasePK(String pk_cumandoc) throws Exception {
		return (String) getJdbc().getObject("select pk_cubasdoc from bd_cumandoc where pk_cumandoc='" + pk_cumandoc + "'");
	}

	public String getInvBasePK(String pk_invmandoc) throws Exception {
		return (String) getJdbc().getObject("select pk_invbasdoc from bd_invmandoc where pk_invmandoc='" + pk_invmandoc + "'");
	}

	public String getInvManPK(String invcode, String pk_corp) throws Exception {
		return (String) getJdbc().getObject("select pk_invmandoc from bd_invmandoc where pk_corp='" + pk_corp + "' and pk_invbasdoc=(select pk_invbasdoc from bd_invbasdoc where invcode='" + invcode + "')");
	}

	public UFDouble getSalePrice(String corder_bid) throws Exception {
		Object obj = getJdbc().getObject("select nqttaxnetprc from so_saleorder_b where corder_bid='" + corder_bid + "'");
		return obj == null ? new UFDouble(0) : new UFDouble(obj.toString());
	}

	public UFDouble getSaleRate(String corder_bid) throws Exception {
		Object obj = getJdbc().getObject("select nquoteunitrate from so_saleorder_b where corder_bid='" + corder_bid + "'");
		return obj == null ? new UFDouble(0) : new UFDouble(obj.toString());
	}

	public String getSaleRowNo(String corder_bid) throws Exception {
		return (String) getJdbc().getObject("select crowno from so_saleorder_b where corder_bid='" + corder_bid + "'");
	}

	public String getTranRowNo(String cbill_bid) throws Exception {
		return (String) getJdbc().getObject("select crowno from to_bill_b where cbill_bid='" + cbill_bid + "'");
	}

	public String getCSpaceid(String vfree1, String pk_stordoc, String pk_corp, String pk_invbasdoc) throws Exception {
		return (String) getJdbc().getObject("select cspaceid from v_ic_onhandnum6 where vfree1='" + vfree1 + "' and cwarehouseid='" + pk_stordoc + "' and pk_corp='" + pk_corp + "' and cinvbasid='" + pk_invbasdoc + "' and cspaceid<>'_________N/A________'");
	}

	@SuppressWarnings("unchecked")
	public void checkXJXC(String[] dhs) throws Exception {
		String dhWhere = " vfree1 in (";
		for (String dh : dhs) {
			dhWhere += "'" + dh + "',";
		}
		dhWhere = dhWhere.substring(0, dhWhere.length() - 1) + ")";
		String dhSQL = "select ccalbodyid as kczz,cinvbasid as lh,cwarehouseid as ck,vlot from ic_onhandnum where " + dhWhere;
		List<Map<String, Object>> d1 = getJdbc().getMapList(dhSQL);
		Map<String, Integer> d1Map = new HashMap<String, Integer>();
		for (Map<String, Object> map : d1) {
			String key = map.get("kczz") + "," + map.get("lh") + "," + map.get("ck");
			Object vlotObj = map.get("vlot");
			if (vlotObj == null) continue;
			try {
				String vlot = vlotObj.toString();
				int year = Integer.parseInt(vlot.substring(1, 3));
				int month = -1;
				String monthStr = vlot.substring(3, 4);
				for (int i = 0; i < monthArray.length; i++) {
					if (monthArray[i].equals(monthStr)) {
						month = i + 1;
						break;
					}
				}
				if (month == -1) continue;
				int day = Integer.parseInt(vlot.substring(4, 6));
				Integer v = new Integer(year + "" + month + "" + (day < 10 ? ("0" + day) : day));
				if (d1Map.containsKey(key)) {
					Integer srcV = d1Map.get(key);
					if(v.intValue() < srcV.intValue()) {
						d1Map.put(key, v);
					}
				}else {
					d1Map.put(key, v);
				}
			} catch (Exception ex) {

			}
		}
		Iterator<Entry<String, Integer>> d1It = d1Map.entrySet().iterator();
		String checkSQL;
		while (d1It.hasNext()) {
			Entry<String, Integer> next = d1It.next();
			String key = next.getKey();
			String[] keys = key.split(",");
			String kczz = keys[0];
			String lh = keys[1];
			String ck = keys[2];
			checkSQL = "select vfree1 from ic_onhandnum where nvl(dr,0)=0 and nonhandnum>0 and ccalbodyid='" + kczz + "' and cinvbasid='" + lh + "' and cwarehouseid='" + ck + 
					"' and to_number(substr(vlot,2,2)||decode(substr(vlot,4,1),'A',1,'B',2,'C',3,'D',4,'E',5,'F',6,'G',7,'H',8,'I',9,'J',10,'K',11,'L',12,-1)||substr(vlot,5,2)) <'" 
					+ next.getValue().toString() + "' order by to_number(substr(vlot,2,2)||decode(substr(vlot,4,1),'A',1,'B',2,'C',3,'D',4,'E',5,'F',6,'G',7,'H',8,'I',9,'J',10,'K',11,'L',12,-1)||substr(vlot,5,2))";
			Object checkObj = getJdbc().getObject(checkSQL);
			if (checkObj != null) {
				String ckName = (String) getJdbc().getObject("select storname from bd_stordoc where pk_stordoc='" + ck + "'");
				String lhCode = (String) getJdbc().getObject("select invcode from bd_invbasdoc where pk_invbasdoc='" + lh + "'");
				throw new Exception("先进先出错误![仓库:" + ckName + "][料号:" + lhCode + "][建议垛号:" + checkObj + "]");
			}
		}

	}
}
