package nc.pub.rino;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author Administrator
 * @date Apr 1, 2014 7:35:15 PM
 * @type nc.ws.baoyin.MapService
 * @corporation 上海锐鸟软件有限公司
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
@SuppressWarnings("unchecked")
public class MapUtil {
	public Map<String, Object> toMap(String xml) throws Exception {
		if (xml == null || "".equals(xml)) return null;
		InputStream is = null;
		Map<String, Object> map = null;
		try {
			SAXReader saxReader = new SAXReader();
			is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			Document doc = saxReader.read(is);
			map = new HashMap<String, Object>();
			Element root = doc.getRootElement();
			for (Iterator<Element> iterator = root.elementIterator(); iterator.hasNext();) {
				Element e = iterator.next();
				List<Element> list = e.elements();
				if (list.size() > 0) {
					map.put(e.getName(), toMap(e));
				} else {
					map.put(e.getName(), e.getText());
				}
			}
		} finally {
			if (is != null) is.close();
		}
		return map;
	}

	public Map<String, Object> toMap(Element e) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Element> list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List<Object> mapList = new ArrayList<Object>();
				if (iter.elements().size() > 0) {
					Map<String, Object> m = toMap(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList<Object>();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List<Object>) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList<Object>();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List<Object>) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else map.put(iter.getName(), iter.getText());
				}
			}
		} else {
			map.put(e.getName(), e.getText());
		}
		return map;
	}
}
