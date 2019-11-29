package nc.impl.uap.itfcheck;

import java.io.File;
import java.util.List;


import nc.vo.uap.itfcheck.XmlAggEntity;
import nc.vo.uap.itfcheck.XmlBEntity;
import nc.vo.uap.itfcheck.XmlHEntity;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



/**
 * 获取接口配置文件
 * 读取路径
 * 返回配置实体类
 * */
public class ReadItfConfigXml {

	//配置文件路径
	private String XML_PATH = "/resources/itfconfig/";
	
	/**
	 * 获取配置文件
	 * @return 
	 * @throws DocumentException 
	 * 
	 * */
	public XmlAggEntity getConfigXml(String itfcode) throws DocumentException {
		// TODO Auto-generated method stub
		String cfgpath = getNChomePath()+XML_PATH+itfcode+".xml";
		
		XmlAggEntity cfgxml = parseXml(cfgpath);
		
		return cfgxml;
	}
	
	/**
	 * 读取xml
	 * @throws DocumentException 
	 * */
	private XmlAggEntity parseXml(String cfgpath) throws DocumentException {
		XmlAggEntity aggvo = new XmlAggEntity();
		XmlHEntity hvo = new XmlHEntity();
		XmlBEntity[] bvos = null;
		// 解析XML文件
		SAXReader reader = new SAXReader();
		File file = new File(cfgpath);
		Document doc = reader.read(file);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		List<Element> rowList;
		for (int i = 0; i < list.size(); i++) {
			if ("bodylist".equals(list.get(i).getName())) {
				List<Element> bodyList = list.get(i).elements();
				
				bvos = new XmlBEntity[bodyList.size()];
				for (int j = 0; j < bodyList.size(); j++) {
					if ("row".equals(bodyList.get(j).getName())) {
						rowList = bodyList.get(j).elements();
						XmlBEntity bvo = new XmlBEntity();
						for (int k = 0; k < rowList.size(); k++) {
							bvo.setAttributeValue(rowList.get(k).getName(), rowList.get(k).getText());
						}
						bvos[j]=bvo;
					}
				}
			} else {
				hvo.setAttributeValue(list.get(i).getName(),list.get(i).getText());
			}
		}
		aggvo.setParentVO(hvo);
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}
	
	
	/**
	 * 获取NChome根目录路径
	 * */
	private String getNChomePath() {
		String homepath = System.getProperty("nc.server.location", System.getProperty("user.dir"));
		return homepath;
	}
	

}
