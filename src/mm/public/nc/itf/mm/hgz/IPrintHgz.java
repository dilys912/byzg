/**
 * 
 */
package nc.itf.mm.hgz;

import javax.swing.ImageIcon;

/**
 * 2019��8��22��
 * 
 * @author zwx
 * 
 * �ϸ�֤�������ɽӿ�
 *
 */
public interface IPrintHgz {

	public String createQRCode(String sapCode);
	
	public ImageIcon createImg(String barcode);
}
