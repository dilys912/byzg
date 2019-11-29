package nc.bs.pts.delreturnfile;

import java.io.File;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

public class DelPTSReturnFile implements IBusinessPlugin {

	public int getImplmentsType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Key[] getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAlertMessage implementReturnFormatMsg(Key[] arg0, String arg1,
			UFDate arg2) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
    
	/**
	 * add by src 2019年3月7日13:29:09 定时删除PTS的回执文件
	 */
	public String implementReturnMessage(Key[] arg0, String arg1, UFDate arg2)
			throws BusinessException {
		// TODO Auto-generated method stub
		String[] filePath = new String[2];
		String indocsPath = RuntimeEnv.getInstance().getNCHome()+ "/pfxx/pfxxtemp/indocs";
		String responsesPath = RuntimeEnv.getInstance().getNCHome()+ "/pfxx/pfxxtemp/responses";
		filePath[0] = indocsPath;
		filePath[1] = responsesPath;
		for (int i = 0 ; i < filePath.length ; i++) {
			File file = new File(filePath[i]);
			if (file.isDirectory()) {
				File[] Files = file.listFiles();
				if (Files.length > 0) {
					for (int j = 0; j < Files.length; j++) {
						String filename = Files[j].getName();
						if (i == 0) {
							if (filename.endsWith("pts.xml")) {
								new File(indocsPath+"/"+filename).delete();
							}
						} else {
							if (filename.contains("pts_") && filename.endsWith(".xml")) {
								new File(responsesPath+"/"+filename).delete();
							}
						} 
					}
				}
			}
		}
		return null;
	}

	public Object implementReturnObject(Key[] arg0, String arg1, UFDate arg2)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean implementWriteFile(Key[] arg0, String arg1, String arg2,
			UFDate arg3) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}

}
