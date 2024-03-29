package nc.bs.ic.alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.bs.pub.pa.html.IAlertMessage2;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

/**
 * @author HK 
 * @date 
 * 功能：发送PTS系统XML信息预警
 * 修改原因：发送PTS系统XML信息预警
 */
@SuppressWarnings("serial")
public class DownLoadRARAlert implements IBusinessPlugin,IAlertMessage2 {

	//预警类型
	String warntype = "发送PTS系统XML信息预警";
	
	String[][] Allbodyvalues;
	
	List Alllist;
	
	public int getImplmentsType() {
//		return 3; 
		//edit by wbp 
		return 0;
		// edit end by wbp 2017-11-30
	}

	public Key[] getKeys() {
		return null;
	}

	public String getTypeDescription() {
		return null;
	}

	public String getTypeName() {
		return null;
	}
	
	File file=new File("/uftest/ERP");
	//File file=new File("D:/ERP");
	public String implementReturnMessage(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
				
		//add by wbp 2017-11-30   逐步执行返回预警信息
		StringBuffer sb = new StringBuffer();
		
		if("1078".equals(corpPK)||"1108".equals(corpPK)){
			//调用FTP移动文件
			//checkFileCP();
			try {
				FtpUpfile ftp = new FtpUpfile("10.43.66.160",21,"ftpuser","ftpusers");  
				ftp.connectServer();   //连接FTP
				List list = ftp.getFileList("/ERP/");   //连接FTP后，获取ERP文件夹档案
				
				//------记录服务器端文件----------
				int filenum = 0;
				int dfilenum = 0;
				sb.append("已经连接到FTP服务器！");
				for (int i = 0; i < list.size(); i++) {
					String filename = new String(list.get(i).toString().getBytes("UTF-8"), "iso-8859-1").substring(5);//连接FTP后，取得ERP文件夹档案
					
					if("CP".equals(filename)||"KL".equals(filename)||"FJ".equals(filename)||"WS".equals(filename)){
						List xmllist = ftp.getFileList("/ERP/"+filename);   //取得XML档案写入LIST
						sb.append("\n获取文件夹："+filename+"下xml的个数--------------------"+xmllist.size());  //add by wbp 输出预警信息
						for (int j = 0; j < xmllist.size(); j++) {
							String xmlname = new String(xmllist.get(j).toString().getBytes("UTF-8"), "iso-8859-1").substring(8);   //取得XML档案
							if(xmlname.endsWith(".xml")||xmlname.endsWith(".XML")){
								filenum = filenum+1;
								File xmlfile = new File(file+"/"+filename+"/"+xmlname);
								File xmlfile_succ = new File(file+"/"+filename+"/success/"+xmlname);
								File xmlfile_fail = new File(file+"/"+filename+"/faile/"+xmlname);
								//edit by wkf 2017-10-27 必须文件夹及成功（success）和失败(faile)都不存在才下载
								if(!xmlfile.exists()){
									if(!xmlfile_succ.exists()&&!xmlfile_fail.exists()){
										boolean downloadstat =ftp.downloadFile("/ERP/"+filename+"/"+xmlname,file+"/"+filename+"/"+xmlname); //将FTP上的XML下载到本地
										//如果下载成功则转移文件至success
										if(downloadstat){
											dfilenum = dfilenum+1;
											ftp.rename("/ERP/"+filename+"/"+xmlname, "/ERP/"+filename+"/success/"+xmlname);
											//add by wkf 2018-06-25  给本地文件加xmlname节点--
											//addNodeToXml(new File(file+"/"+filename+"/"),xmlname);
										}
									}
								}
							}
						}
					}
				}
				
				sb.append("获得PTS-XML文件"+filenum+"个,其中"+(filenum-dfilenum)+"个本地已存在，成功下载"+dfilenum+"个！");
			} catch (Exception e) {
				e.printStackTrace();				
				throw new BusinessException(e.getMessage());
			}
			Alllist = null;
			sb.append("\n已输出完毕...");  //add by wbp 输出预警信息
		}
		System.out.println("输出到页面-------------"+sb.toString());
		return sb.toString();
//		return null;
		//end by wbp 2017-11-30 
	}
	
	public void checkFileCP() throws BusinessException{
		  File cpfile = new File("D://ERP/CP");
		  String[] filstr = cpfile.list();
		  List fileList = new ArrayList();
		  for(int j = 0 ; j<filstr.length ; j++){
			  String xmlname = filstr[j];
			  if(xmlname.endsWith(".XML")){
				  fileList.add(xmlname);
			  }
		  }
		  IUAPQueryBS uAPQueryBSQ = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  StringBuffer sb = new StringBuffer();//去除这两天导的数据
		  sb.append(" select b.vuserdef15 from ic_general_b b where (b.pk_corp='1078' or b.pk_corp='1108') and b.vuserdef15 is not null and ts>'2018-06-23 00:00:00' "); 
		  List all = (List) uAPQueryBSQ.executeQuery(sb.toString(), new MapListProcessor());
		  List dblst = new ArrayList();
		  if(all.size()>0){
			  for(int i =0 ; i<all.size() ; i++){
				  Map map = (Map) all.get(i);
				  String filename = map.get("vuserdef15").toString();//取出文件名
				  dblst.add(filename);
			  }
		  }
		 // List butong = new ArrayList();
		  //File newfile = new File("F://uftest/ERP/CP/success/");
	      for(Object o : fileList){
	    	  if(dblst.contains(o)){
	    		  File twins = new File("D://ERP/CP/"+o.toString());
	    		  twins.renameTo(new File("D://ERP/CP/success/"+o.toString()));
//	    		  File twins = new File("F://uftest/ERP/WS/"+o.toString());
//	    		  twins.renameTo(new File("F://uftest/ERP/WS/success/"+o.toString()));
	    		  //butong.add(o);
	    	  }
	      }
	      System.out.println(0);
	  }
	
	
	//add by zwx 2017-6-12 在xml文件中增加xml文件名节点
	public void addNodeToXml(File cpfile, String filename){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(cpfile + "/" + filename);
			Element root = document.getDocumentElement(); 
		    NodeList childList = root.getElementsByTagName("ic_bill_head");
	    	Node node = childList.item(0);
	    	NodeList subNodeList = node.getChildNodes();
		    boolean flag = false;
		    for(int m = 0;m<subNodeList.getLength();m++){
		    	Node subnode = subNodeList.item(m);
		    	if(subnode.getNodeName().equals("xmlname")){
		    		flag = true;
		    	}
		    }
		    if(flag==false){//如果已经存在xmlname节点则不再添加
		    	Element elem =document.createElement("xmlname");
				elem.setTextContent(filename);
				childList.item(0).appendChild(elem);
				// 将内存中的Document对象写到xml文件中  
 				TransformerFactory tf = TransformerFactory.newInstance();  
		        Transformer former = tf.newTransformer();  
		        former.setParameter("encoding", "UTF-8");  
		        // 将整个Document对象作为要写入xml文件的数据源  
		        DOMSource xmlSource = new DOMSource(document);  
		        // 要写入的目标文件  
		        StreamResult outputTarget = new StreamResult(new File(cpfile + "/" + filename));  
		        former.transform(xmlSource, outputTarget);
		    }
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	int[] BodyColumnType = new int[]{
			IAlertMessage.TYPE_STRING,
			IAlertMessage.TYPE_STRING,
			IAlertMessage.TYPE_STRING
	};

	

	private String getStrValue(Object obj) {
		if(obj==null){
			return "";
		}
		return obj.toString();
	}

	public Object implementReturnObject(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
		
		return null;
	}

	public boolean implementWriteFile(Key[] keys, String fileName,
			String corpPK, UFDate clientLoginDate) throws BusinessException {
		
		return false;
	}

	public String[] getBodyFields() {
		
		return new String[]{
				"xml文件编号",
				"单据日期",
				"成功/失败"
		};
	}

	public String[][] getBodyValue() {
		Allbodyvalues = new String[Alllist.size()][3];
		for (int i = 0; i < Alllist.size(); i++) {
			Map b = (Map) Alllist.get(i);
			Allbodyvalues[i][0] =  b.get("xmlcode").toString();
			Allbodyvalues[i][1] =  b.get("dbilldate").toString();
			Allbodyvalues[i][2] =  b.get("result").toString();
		}
		if(Alllist.size() <=0){
			return null;
		}
		return null;
	}

	public float[] getBodyWidths() {
		
		return null;
	}

	public String[] getBottom() {
		
		return new String[]{
		};
	} 

	public String getTitle() {
		
		return warntype;
	}

	public String[] getTop() {
		
		return new String[]{
				"发送PTS系统XML信息预警："
		};
	}

	public int[] getBodyColumnType() {
		
		return BodyColumnType;
	}

	public String getNullPresent() {
		
		return null;
	}

	public String getOmitPresent() {
		
		return null;
	}

	public IAlertMessage implementReturnFormatMsg(Key[] arg0, String arg1,
			UFDate arg2) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * add by wbp 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
         try {
             Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
             Matcher m = p.matcher(strName);
             String after = m.replaceAll("");
             String temp = after.replaceAll("\\p{P}", "");
             char[] ch = temp.trim().toCharArray();

             int length = (ch != null) ? ch.length : 0;
             for (int i = 0; i < length; i++) {
                 char c = ch[i];
                 if (!Character.isLetterOrDigit(c)) {
                     String str = "" + ch[i];
                     if (!str.matches("[\u4e00-\u9fa5]+")) {
                         return true;
                     }
                 }
             }
         } catch (Exception e) {
        	 System.out.println("验证乱码不通过。。。");
             e.printStackTrace();
         }

         return false;
 
    }
	
}

//			FTPModel ftpm = new FTPModel("ftpuser","ftpusers","10.43.66.160",21,"/ERP/");
//			FTPCommon ftpc = new FTPCommon(ftpm);
//			try {
//				ftpc.ftpLogin();
//				String[] list = ftpc.getListFiels();
//				for (int i = 0; i < list.length; i++) {					
//					String filename = list[i];
//					System.out.println(filename);
//					if("CP".equals(filename)||"KL".equals(filename)||"FJ".equals(filename)||"WS".equals(filename)){
//						ftpc.changeDir("/ERP/"+filename);
//						String[] xmllist = ftpc.getListFiels();
//						for (int j = 0; j < xmllist.length; j++) {
//							String xmlname = xmllist[j];
//							System.out.println(xmlname);
//							if(xmlname.endsWith(".xml")||xmlname.endsWith(".XML")){
//								ftpc.isOpenFTPConnection();
//								File xmlfile = new File(file+"/"+filename+"/"+xmlname);
//								File xmlfile_succ = new File(file+"/"+filename+"/success/"+xmlname);
//								File xmlfile_fail = new File(file+"/"+filename+"/faile/"+xmlname);
//								if(!(xmlfile.exists()||xmlfile_succ.exists()||xmlfile_fail.exists())){										
//									boolean downloadstat = ftpc.downloadFile(file+"/"+filename+"/"+xmlname,xmlname);
////									if(downloadstat){
////										ftpc.getFtpClient().deleteFile(xmlname);
////									}
//								}
//							}
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new BusinessException(e.getMessage());
//			}
//2.