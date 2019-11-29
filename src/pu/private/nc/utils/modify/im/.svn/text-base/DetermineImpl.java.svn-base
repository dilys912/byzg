package nc.utils.modify.im;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import nc.bs.framework.common.RuntimeEnv;
import nc.utils.modify.is.IdetermineService;

public class DetermineImpl implements IdetermineService {
	// 判断是否为国内公司
	public Boolean check(String value) {
	
		// 获取国内公司的list集合
		System.out.println("国内公司集合");
		List<String> list = GetValue();
		// 循环遍历 判断是否为国内公司
		for (int i = 0; i < list.size(); i++) {
			System.out.println("国内公司：" + list.get(i) + "-----" + "传输的过来的公司："
					+ value.trim());
			if (list.get(i).trim().equals(value.trim())) {
				return true;
			}
		}

		return false;
	}

	// 获取propertise中的值，返回一个list集合
	public static List<String> GetValue() {
		List<String> list = new ArrayList<String>();
		Properties props = new Properties();
		try {
			 String fileName = RuntimeEnv.getInstance().getNCHome()
			    + File.separator + "resources" + File.separator
				+ "Markingmoney.properties";// 取得属性文件的路径
			InputStream in = new BufferedInputStream(new FileInputStream(fileName));	
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
				list.add(Property);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return list;

	}
}
