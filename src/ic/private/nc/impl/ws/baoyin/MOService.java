package nc.impl.ws.baoyin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.itf.mm.scm.mm6600.IMo6600;
import nc.vo.mo.mo6600.GlBillVO;
import nc.vo.mo.mo6600.GlHeadVO;
import nc.vo.mo.mo6600.GlItemBVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

import org.apache.commons.lang.StringUtils;

/**
 * ���߼��������
 * @author Administrator
 * @date Mar 31, 2014 3:33:14 PM
 * @type nc.ws.baoyin.CRService
 * @corporation �Ϻ�����������޹�˾
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
@SuppressWarnings("unchecked")
public class MOService extends BaseService {

	/**
	 * ���ﲻ֪��Ϊʲô,û�������������,�˺�������ΪEJB���õ�,connectionӦ����Ϊ��һ������
	 * ����BUG:��������ű���,��������������ʾ����,���Ѿ��������߼��鵥,��Ȼû�м����ִ���,��Ӱ����,
	 * ���Ǿ������ݺ�������˶�žͻᱨ��:����Ѿ�����
	 * 
	 * edit by zip: 2014/10/7
	 * 1, ����������ֳֻ����ݹ���
	 * 2, ���������Լ���ⵥ�Ƶ����ڻ�ȡϵͳ��ǰ����
	 */
	public String addXXJY(Map<String, Object> root) throws Exception {
		// ----------------------------- start SAVE --------------------------
		Map<String, Object> billMap = (Map<String, Object>) root.get("bill");
		Map<String, Object> headMap = (Map<String, Object>) billMap.get("head");
		String pk_corp = (String) headMap.get("pk_corp");
		String pk_user = (String) headMap.get("lrr");
		String rkrq = (String)headMap.get("rkrq");
		UFDate nowDate = new UFDate(System.currentTimeMillis());
		Object bodyObj = ((Map<String,Object>)billMap.get("bodys")).get("body");
		List<Map<String, Object>> bodyMapList;
		if (bodyObj instanceof Map) {
			bodyMapList = new ArrayList<Map<String, Object>>();
			bodyMapList.add((Map<String, Object>) bodyObj);
		} else {
			bodyMapList = (List<Map<String, Object>>) bodyObj;
		}
		String invcode = (String) bodyMapList.get(0).get("lh");
		GlBillVO billVO = new GlBillVO();
		GlHeadVO headVO = new GlHeadVO();
		headVO.setLogindate(new UFDate(rkrq));
		setAttrValue(headVO, "scddh", headMap.get("pk_moid"));
		//setAttrValue(headVO, "xxrq", headMap.get("xxrq"));
		setAttrValue(headVO, "xxrq", nowDate);
		setAttrValue(headVO, "phrq", headMap.get("xxrq"));
		setAttrValue(headVO, "xxsj", headMap.get("xxsj"));
		setAttrValue(headVO, "djzt", 0);
		setAttrValue(headVO, "pk_corp", pk_corp);
		setAttrValue(headVO, "jyjg", 0);
		setAttrValue(headVO, "vbillstatus", 8);
		setAttrValue(headVO, "billsign", "JYDJ");
		setAttrValue(headVO, "billno", new BillcodeGenerater().getBillCode("JYDJ", pk_corp, null, null));
		// ---------------- start convert head ---------------------
		String sql1 = "select ksid,jhwgsl,rksl,nvl(jhwgsl,0)-nvl(rksl,0) as ddwwgsl from mm_mo where pk_moid='" + headMap.get("pk_moid") + "'";
		Map<String, Object> map1 = (Map<String, Object>) getJdbc().getMapList(sql1).get(0);
		setAttrValue(headVO, "ks", map1.get("ksid"));
		setAttrValue(headVO, "ddsl", map1.get("jhwgsl"));
		setAttrValue(headVO, "ddwgsl", map1.get("jhwgsl"));
		setAttrValue(headVO, "ddwwgsl", map1.get("ddwwgsl"));
		String sql_head_scx = "select gzzxid from mm_mokz where pk_moid='" + headMap.get("pk_moid") + "'";
		setAttrValue(headVO, "scx", getJdbc().getObject(sql_head_scx));
		String sql_head_zdsl = "select vdef11 from so_sale where nvl(dr,0)=0 and vreceiptcode=(select xsddh from mm_mo where pk_moid='" + headMap.get("pk_moid") + "') and pk_corp='" + pk_corp + "'";
		setAttrValue(headVO, "zdsl", getJdbc().getObject(sql_head_zdsl));
		setAttrValue(headVO, "cp", getInvManPK(invcode, pk_corp));
		setAttrValue(headVO, "bz", getBZ((String) headMap.get("bz"), pk_corp));
		setAttrValue(headVO, "ck", getCK((String) headMap.get("ck"), pk_corp));
		setAttrValue(headVO, "lrr", pk_user);
//		setAttrValue(headVO, "xxry", getJdbc().getObject("select pk_psndoc from bd_psndoc where nvl(dr,0)=0 and pk_corp='"+pk_corp+"' and pk_psnbasdoc=(select pk_psndoc from sm_userandclerk where userid='"+pk_user+"')"));
		// ---------------- end convert head -----------------------
		GlItemBVO[] bodyVOs = new GlItemBVO[bodyMapList.size()];
		for (int i = 0; i < bodyMapList.size(); i++) {
			bodyVOs[i] = new GlItemBVO();
			Map<String, Object> bodyMap = bodyMapList.get(i);
			setAttrValue(bodyVOs[i], "xxaglsl", bodyMap.get("sl"));
			setAttrValue(bodyVOs[i], "ph", bodyMap.get("ph"));
			setAttrValue(bodyVOs[i], "dh", bodyMap.get("dh"));
			// -------------- start convert body -----------------
			String pk_cargdoc = getHW((String) bodyMap.get("hw"), headVO.getCk());
			setAttrValue(bodyVOs[i], "sfltd", "Y");
			setAttrValue(bodyVOs[i], "pk_corp", pk_corp);
			setAttrValue(bodyVOs[i], "hw", getJdbc().getObject("select cscode from bd_cargdoc where pk_cargdoc='" + pk_cargdoc + "'"));
			setAttrValue(bodyVOs[i], "cp", getJdbc().getObject("select invname from bd_invbasdoc where invcode='" + invcode + "'"));
			setAttrValue(bodyVOs[i], "cinventoryid", getInvManPK(invcode, pk_corp));
			setAttrValue(bodyVOs[i], "cspaceid", pk_cargdoc);
			// -------------- end convert body -------------------
		}
		billVO.setParentVO(headVO);
		billVO.setChildrenVO(bodyVOs);

		if (StringUtils.isEmpty(headVO.getScddh())) throw new Exception("���������Ų���Ϊ��");
		List<String> tempDh = new ArrayList<String>();
		int i = 0;
		for (GlItemBVO temp : bodyVOs) {
			if (StringUtils.isEmpty(temp.getDh())) throw new Exception("��[" + (i + 1) + "]�ж�Ų���Ϊ��!");
			if (tempDh.contains(temp.getDh())) throw new Exception("���[" + temp.getDh() + "]�ظ�");
			StringBuilder sql = new StringBuilder("select dh from mm_glzb_b where nvl(dr,0)=0 and dh ='").append(temp.getDh().trim() + "' ");
			sql.append(" union all select hcldh from mm_glcl_b where nvl(dr,0)=0 and hcldh ='").append(temp.getDh().trim()).append("'");
			List<Map<String, Object>> voList = (List<Map<String, Object>>) getJdbc().getMapList(sql.toString());
			if (voList != null && voList.size() > 0) { throw new Exception("���[" + temp.getDh() + "]�ظ�"); }
			if (temp.getXxaglsl() == null || temp.getXxaglsl() == 0) { throw new Exception("���߻������������Ϊ��"); }
			if (temp.getDh().length() != 10) throw new Exception("��ų��ȷ�10λ");
			Pattern pattern = Pattern.compile("\\d{10}");
			Matcher matcher = pattern.matcher(temp.getDh());
			if (!matcher.matches()) throw new Exception("��Ÿ�ʽ����");
			tempDh.add(temp.getDh());
			i++;
		}
		i = 0;
		int currXxsl = 0;
		for (GlItemBVO temp : bodyVOs) {
			if (temp.getXxaglsl() != headVO.getDdsl()) {
				temp.setSfltd(new UFBoolean(true));
			} else {
				temp.setSfltd(new UFBoolean(false));
			}
			if (temp.getStatus() == VOStatus.UNCHANGED) {
				temp.setStatus(VOStatus.UPDATED);
			}
			currXxsl += temp.getXxaglsl();
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select a.scddh, sum(nvl(b.xxaglsl, 0.0)) as completedqty");
		sql.append("   from mm_glzb a, mm_glzb_b b");
		sql.append("  where nvl(b.dr, 0) = 0");
		sql.append("    and nvl(a.djzt, 0) = 1");
		sql.append("    and a.pk_glzb = b.pk_glzb");
		sql.append("    and a.billsign = 'JYDJ'");
		sql.append("    and nvl(scddh, '') = '" + headVO.getScddh() + "'");
		sql.append("    and nvl(a.jyjg, 0) = 0");
		sql.append("  group by a.scddh");
		int totalnum = 0;

		List<Map<String, Object>> mapList = getJdbc().getMapList(sql.toString());
		Map<String, Object> hm = null;
		if (mapList != null && mapList.size() > 0) hm = mapList.get(0);
		if (hm != null && !hm.isEmpty()) {
			String comnum = String.valueOf(hm.get("completedqty"));
			comnum = comnum == null || comnum.equals("") || comnum.equalsIgnoreCase("null") ? "0" : comnum;
			totalnum = currXxsl + new BigDecimal(comnum).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			sql.setLength(0);
			sql.append("select nvl(jhwgsl,0.0)*1.01 as plannum from mm_mo where pk_moid='" + headVO.getScddh() + "'");
			hm.clear();
			hm = ((List<Map<String, Object>>) getJdbc().getMapList(sql.toString())).get(0);
			String plannum = String.valueOf(hm.get("plannum"));
			if (plannum == null || plannum.equals("") || plannum.equalsIgnoreCase("null")) {
				throw new Exception("���������ļƻ���������Ϊ��");
			} else {
				if (totalnum > new BigDecimal(plannum).setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) throw new Exception("���������Ѿ��������������ļƻ�����������1%");
			}
		}
		IMo6600 moService = (IMo6600) NCLocator.getInstance().lookup(IMo6600.class.getName());
		billVO = moService.saveBill(billVO);
		// ----------------------------- end SAVE --------------------------

		// ----------------------------- start CONFIRM  --------------------
		GlHeadVO cfmHead = (GlHeadVO) billVO.getParentVO();
		GlItemBVO[] cfmItemVOs = (GlItemBVO[]) billVO.getChildrenVO();
		for (GlItemBVO cfmItemVO : cfmItemVOs) {
			cfmItemVO.setStatus(VOStatus.UPDATED);
		}
		cfmHead.setLogindate(new UFDate(headMap.get("xxrq").toString()));
		if (cfmHead.getDjzt() == 1) throw new Exception("�����Ѵ���");
		if (cfmHead.getCk() == null) throw new Exception("�ֿⲻ��Ϊ��");
		if (cfmHead.getJyjg() != 0) throw new Exception("ֻ�кϸ�ĵ��ݲ���Ҫȷ��");
		if (cfmHead.getZdsl() == null || cfmHead.getZdsl() == 0) throw new Exception("������������Ϊ��");
		cfmHead.setLogindate(new UFDate(rkrq));
		moService.confirm(billVO);
		// ----------------------------- end CONFIRM --------------------------

		StringBuilder ret = new StringBuilder();
		ret.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ret.append("<root>");
		ret.append("<actiontype>").append(root.get("actiontype")).append("</actiontype>");
		ret.append("<sender>").append(root.get("sender")).append("</sender>");
		ret.append("<billid>").append(billVO.getParentVO().getPrimaryKey()).append("</billid>");
		ret.append("<billno>").append(billVO.getParentVO().getAttributeValue("billno")).append("</billno>");
		ret.append("</root>");
		return ret.toString();
	}

}
