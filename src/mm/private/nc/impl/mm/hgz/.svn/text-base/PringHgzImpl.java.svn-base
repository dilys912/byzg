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
 * 2019年8月22日
 * 
 * @author zwx
 * 
 * 合格证条码生成接口实现
 *
 */

public class PringHgzImpl implements IPrintHgz {


	public static final String defaultDir = RuntimeEnv.getInstance().getNCHome()+ "/webapps/nc_web/ncupload/printimage";
	
	/**
	 * 生成条码至NCHome路径下
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
