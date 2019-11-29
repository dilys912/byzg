package nc.impl.uap.busibean.license;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import nc.bs.logging.Logger;
import nc.bs.pub.install.SecurityLicCtl;
import nc.itf.uap.license.ILicenseService;

public class LicenseImpl implements ILicenseService {
	@SuppressWarnings("unchecked")
	public int getProductLicense(String product) {
		Map licMap = Collections.synchronizedMap(SecurityLicCtl.getInstance().getProductLicenses());
		//add 2019-11-1
		licMap.put("8001", 100);
		licMap.put("8002", 100);
		licMap.put("8003", 100);
		licMap.put("8004", 100);
		licMap.put("8005", 100);
		licMap.put("8006", 100);
		licMap.put("8007", 100);
		licMap.put("8008", 100);
		licMap.put("8009", 100);
		//end 
		
		// add by wang 2019-05-23 屏蔽license对印铁成本模块校验 start
		//获取license配置文件信息
		Properties prop = getNCLicensePro();
		for (Object key : licMap.keySet()) {
			Logger.debug("LicenseImplTest++++"+key);
			if (key != null) {
				if (prop.getProperty(product) != null) {
					String slicnum = prop.getProperty(product);
					int licnum = 0;
					if(isNumeric(slicnum)){
						licnum = Integer.parseInt(slicnum);
						licMap.put(product,licnum);
					}
				}
			};
		}
		// add by wang 2019-05-23 屏蔽license对印铁成本模块校验 end
		if (licMap.containsKey(product))
			return ((Integer) licMap.get(product)).intValue();
		if (licMap.containsKey("*")) {
			if (product.equals("corp")) {
				return 500;
			}
			return ((Integer) licMap.get("*")).intValue();
		}
		return -1;
	}
	
	/**
	 * 字符串数字判断
	 * wang
	 * */
	public boolean isNumeric(String str)
	{
		if(str.trim().length()==0){
			return false;
		}
		for (int i = 0; i < str.length(); i++){
			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获取配置文件
	 * wang
	 * */
	public Properties getNCLicensePro()
    {
        String homepath = System.getProperty("nc.server.location", System.getProperty("user.dir"));
        Properties pro = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(homepath+"\\resources\\license.properties");
			pro.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pro;
    }
	

	public String getProductCode(String moduleCode) {
		return SecurityLicCtl.getInstance().getProductCode(moduleCode);
	}

	public boolean isUsedGLBook() {
		return SecurityLicCtl.getInstance().isUsedGLBook();
	}

	@SuppressWarnings("unchecked")
	public boolean isNCDEMO() {
		Map licMap = Collections.synchronizedMap(SecurityLicCtl.getInstance()
				.getProductLicenses());

		return licMap.containsKey("*");
	}
}