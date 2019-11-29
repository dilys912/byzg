/**
 * 
 */
package nc.impl.mm.hgz;

import java.io.File;

import javax.swing.ImageIcon;

import nc.bs.framework.common.RuntimeEnv;
import nc.itf.mm.hgz.IPrintHgz;
import nc.ui.mo.hgz.JbarcodeUtil;


/**
 * 2019��8��22��
 * 
 * @author zwx
 * 
 * �ϸ�֤�������ɽӿ�ʵ��
 *
 */

public class PringHgzImpl implements IPrintHgz {


	public static final String defaultDir = RuntimeEnv.getInstance().getNCHome()+ "/webapps/nc_web/ncupload/printimage";
	
	/**
	 * ����������NCHome·����
	 */
	public String createQRCode(String sapCode) {
		JbarcodeUtil util = new JbarcodeUtil();
		String path = defaultDir+"/"+sapCode+".png";
		util.createBarcode(sapCode, new File(path), sapCode);
		return path;
	}

	public ImageIcon createImg(String barcode){
		ImageIcon icon = new ImageIcon(barcode);
		return icon;
	}
}
