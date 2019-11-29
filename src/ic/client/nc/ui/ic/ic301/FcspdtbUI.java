package nc.ui.ic.ic301;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import nc.ui.pub.FramePanel;
import nc.vo.pub.lang.UFDouble;

/**
 * 发出商品动态表
 * @author Administrator
 * @date May 13, 2014 11:39:37 AM
 * @type nc.ui.ic.ic301.FcspdtbUI
 * @corporation 上海锐鸟软件有限公司
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
@SuppressWarnings("serial")
public class FcspdtbUI extends nc.ui.pub.querymodel.QEReportBase {

	public FcspdtbUI(FramePanel fp) {
		super(fp);
	}

	@Override
	protected void onQuery() throws Exception {
		super.onQuery();
		Vector data = getReportBase().getBillModel().getDataVector();
		if (data == null || data.size() <= 0) return;
		Vector d1 = new Vector();
		Vector d2 = new Vector();
		Vector d3 = new Vector();
		Map<String, UFDouble[]> d3Map = new HashMap<String, UFDouble[]>();
		for (int i = 0; i < data.size(); i++) {
			Vector v = (Vector) data.elementAt(i);
			String ywlx = (String) v.get(2);
			if ("代加工成品销售业务流程".equals(ywlx)) {
				d3.add(v);
			} else {
				d1.add(v);
			}
			String chfl = (String) v.get(3);
			String gg = (String) v.get(1);
			String yt = chfl.substring(0, 2);
			if ("23".equals(yt)) {
				String rl = gg.substring(0, 3);
				String cz = gg.substring(3, 4);
				String pp = chfl.substring(chfl.length() - 2, chfl.length());
				if ("01".equals(pp)) pp = "可乐";
				else if ("02".equals(pp)) pp = "百事";
				else if ("03".equals(pp)) pp = "品牌啤酒";
				else if ("04".equals(pp)) pp = "品牌茶";
				else pp = "小客户";
				String key = rl + "-" + pp + "-" + cz;
				UFDouble[] d3Value;
				if (d3Map.containsKey(key)) {
					d3Value = d3Map.get(key);
				} else {
					d3Value = new UFDouble[8];
				}
				for (int j = 0; j < d3Value.length; j++) {
					if (d3Value[j] == null) d3Value[j] = new UFDouble(0);
					d3Value[j] = d3Value[j].add(new UFDouble((v.get(j + 4) == null || "".equals(v.get(j + 4))) ? "0" : v.get(j + 4).toString()));
				}
				d3Map.put(key, d3Value);
			} else {
				String key = gg;
				UFDouble[] d3Value;
				if (d3Map.containsKey(key)) {
					d3Value = d3Map.get(key);
				} else {
					d3Value = new UFDouble[8];
				}
				for (int j = 0; j < d3Value.length; j++) {
					if (d3Value[j] == null) d3Value[j] = new UFDouble(0);
					d3Value[j] = d3Value[j].add(new UFDouble((v.get(j + 4) == null || "".equals(v.get(j + 4))) ? "0" : v.get(j + 4).toString()));
				}
				d3Map.put(key, d3Value);
			}

		}
		Vector totalData = new Vector();
		totalData.addAll(d1);
		totalData.addAll(d3);
		Iterator<Entry<String, UFDouble[]>> d3It = d3Map.entrySet().iterator();
		while (d3It.hasNext()) {
			Entry<String, UFDouble[]> entry = d3It.next();
			Vector v = new Vector();
			v.add(entry.getKey());
			v.add("");
			v.add("");
			v.add("");
			UFDouble[] d3Value = entry.getValue();
			for (int i = 0; i < d3Value.length; i++) {
				v.add(d3Value[i]);
			}
			v.add(new UFDouble(0));
			v.add(new UFDouble(0));
			totalData.add(v);
		}
		getReportBase().getBillModel().clearBodyData();
		getReportBase().getBillModel().setDataVector(totalData);
	}
}
