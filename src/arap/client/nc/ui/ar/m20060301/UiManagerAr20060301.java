package nc.ui.ar.m20060301;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.java_cup.internal.internal_error;



import nc.bs.framework.common.NCLocator;
import nc.com.google.gson.Gson;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.itfcheck.IInterfaceCheck;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.net.sf.json.JSONArray;
import nc.net.sf.json.JSONObject;
import nc.ui.baoyin.buttons.selfdef1Btn;
import nc.ui.ep.dj.DjPanel;
import nc.ui.glpub.IUiPanel;
import nc.ui.glpub.UiManager;
import nc.ui.po.pub.PoPublicUIClass;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.FramePanel;
import nc.ui.pub.ILinkOperateable;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.utils.modify.is.IdetermineService;
import nc.vo.ar.m20060301.ITFBZVO;
import nc.vo.ar.m20060301.ITFSNVO;

import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.msg.MessageVO;

public class UiManagerAr20060301 extends UiManager
  implements ILinkOperateable, ILinkApprove, ILinkQuery, ILinkAdd, ILinkMaintain
{
  private static final long serialVersionUID = 4290750874731813570L;

  //����ʼ��
  public UiManagerAr20060301(FramePanel panel)
  {
    super(panel);
  }
  
  //���ݳ�ʼ��
  public void initData(MessageVO msgvo)
  {
    IUiPanel iPanel = getCurrentPanel();
    if ((iPanel instanceof DjPanel))
    {
      DjPanel djPanel = (DjPanel)iPanel;
      djPanel.postInit();
    }
  }

  public void doApproveAction(ILinkApproveData approvedata)
  {
    IUiPanel iPanel = getCurrentPanel();
    if ((iPanel instanceof DjPanel)) {
      DjPanel djPanel = (DjPanel)iPanel;
      djPanel.doApproveAction(approvedata);
    }
  }

  public void doQueryAction(ILinkQueryData querydata)
  {
    IUiPanel iPanel = getCurrentPanel();
    if ((iPanel instanceof DjPanel)) {
      DjPanel djPanel = (DjPanel)iPanel;
      djPanel.doQueryAction(querydata);
    }
  }

  public void doAddAction(ILinkAddData adddata)
  {
    IUiPanel iPanel = getCurrentPanel();
    if ((iPanel instanceof DjPanel)) {
      DjPanel djPanel = (DjPanel)iPanel;
      djPanel.doAddAction(adddata);
    }
  }

  public void setLinkData(Object[] arg0)
  {
    IUiPanel iPanel = getCurrentPanel();
    if ((iPanel instanceof DjPanel)) {
      DjPanel djPanel = (DjPanel)iPanel;
      djPanel.setLinkData(arg0); 
    }
  }

  public void doMaintainAction(ILinkMaintainData maintaindata) {
    IUiPanel iPanel = getCurrentPanel();
    if ((iPanel instanceof DjPanel)) {
      DjPanel djPanel = (DjPanel)iPanel;
      djPanel.doMaintainAction(maintaindata);
    }
  }

  public void postInit() {
    IUiPanel iPanel = getCurrentPanel();
    if ((iPanel instanceof DjPanel)) {
      DjPanel djPanel = (DjPanel)iPanel;
      djPanel.postInit();
    }
  }
  
  /**
   * Ӧ�մ���Ʒ���XBUS
   * wy
   * 2019��9��10�� 10��12
   * @param bo
   */
  @SuppressWarnings("restriction")
	public void sendXbus(ButtonObject bo){
	  String corp = PoPublicUIClass.getLoginPk_corp();
	  IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
	  Boolean result = idetermineService.check(corp);
	  if(result){//�жϵ�ǰ��˾�Ƿ�Ϊ���ڹ�˾������ִ��
	  	 IUiPanel iPanel = getCurrentPanel();
		 @SuppressWarnings("unused")
		 DjPanel djPanel = null;//�������
		   if ((iPanel instanceof DjPanel))
		    {
		      djPanel = (DjPanel)iPanel;
		    }
		    List<DJZBVO> vos = getSelectedRowVO();//��ȡѡ������
	        try {
	        	getCurrentPanel().onButtonClicked(bo);
			}catch (Exception e) {
				showErrorMessage(e.getMessage());
				e.printStackTrace();
				return ;
			}			
			StringBuffer str1 = new StringBuffer();	//��δ���ͳɹ��ĵ��ݱ��
			StringBuffer str2 = new StringBuffer();	//���Ƿ�Ϊ��˵���
			StringBuffer str3 = new StringBuffer();	//���ѷ��ͳɹ��ĵ��ݱ��
			StringBuffer str4 = new StringBuffer();	//��ȱ���ֶεĵ��ݱ��
			//�ж����б���滹�ǿ�Ƭ����
			boolean showing = djPanel.getArapDjPanel1().getBillCardPanelDj().isShowing();
			if(showing == false && vos.size()==0){
				showWarningMessage("�б���δѡ��Ҫ����Ƶ�����");
				return;							
			}else if(showing == false && vos.size()>0){//���ѡ�е����ݴ�����	        	
	        	for (int j = 0; j < vos.size(); j++) {
	        		DJZBHeaderVO hvo = (DJZBHeaderVO)vos.get(j).getParentVO();
	        		String djbh = vos.get(j).getParentVO().getAttributeValue("djbh").toString();
	        		String judgeDJBH = judgeDJBH(djbh);
	        		if(judgeDJBH == null){
	        			DJZBItemVO[] bvo = (DJZBItemVO[])vos.get(j).getChildrenVO();
	    				String judgeCurrency = judgeCurrency(bvo[0].getBzbm());//�жϱ���
	    				Map custcode = custcode(hvo);//���̱���
    				    String queryLRR = queryLRR(hvo.getLrr());
    				    UFBoolean prepay = hvo.getPrepay();//���㷽ʽ ==Ԥ�ո���־
	      				String jsfs = String.valueOf(prepay);
	      				if("Y".equals(jsfs)){
	      					jsfs = "D";
	      				}else if ("N".equals(jsfs)) {
	      					jsfs = "C";
	      				}
    				    Integer djzt = hvo.getDjzt();
    				    if(djzt == 2){//����״̬ == 2   �����
    				    	if ("CNY".endsWith(judgeCurrency)) {
	  							boolean flag = nxfp(hvo,str4,bvo,djbh,custcode,judgeCurrency,jsfs,queryLRR);//���۷�Ʊ   		
	  							if(flag==true){
	  								str1.append(djbh+",");
	  							}
    				    	}else {
	  							//�ж�һ�º��ر����Ƿ���  ���򴫱��  û����˶�	
	  							Map kpTaxCode = kpTaxCode(hvo.getDwbm());
	  							String chCode = selectInvl(hvo);//�������
	  							Map queryInv = QueryInv(chCode);					
	  							if(queryInv.get("memo")==null||queryInv.get("memo") ==""){
	  								str4.append(djbh+"��������б�ע�����ر��룩��Ҫά��");
	  							}else {
	  								boolean wxfp = wxfp(hvo,str4,bvo,kpTaxCode,chCode,queryInv,djbh,custcode,judgeCurrency,jsfs,queryLRR);//������Ʊ
	  								if (wxfp==true) {
	  									str1.append(djbh+",");
									}
	  							}   
    				    	}      				    			      				  
    				    }else{
    				    	str2.append(djbh+",");//δ���
    				    }     				          				   
	        		}else {
	        			str3.append(djbh+",");//�ѷ��ͳɹ�
					} 	        		
	        	}
	        	if(str1 != null && str1.length()>0 && str2 != null&&str2.length()>0 && str3 != null&&str3.length()>0 && str4!=null&&str4.length()>0){
				    showWarningMessage(str1+"���ݷ��ͳɹ���"+"\n"+str2+"������δ��˵���,����ʧ�ܡ�"+"\n"+str3+"�����ѷ��͹���"+"\n"+str4);
	        	}else if (str1 != null && str1.length()>0 && str2 != null&&str2.length()>0 && str3 != null&&str3.length()>0) {
	        		 showWarningMessage(str1+"���ݷ��ͳɹ���"+"\n"+str2+"������δ��˵���,����ʧ�ܡ�"+"\n"+str3+"�����ѷ��͹���");
				}else if (str1 != null && str1.length()>0 && str2 != null&&str2.length()>0  && str4!=null&&str4.length()>0) {
					showWarningMessage(str1+"���ݷ��ͳɹ���"+"\n"+str2+"������δ��˵���,����ʧ�ܡ�"+"\n"+str4);
				}else if (str2 != null&&str2.length()>0 && str3 != null&&str3.length()>0 && str4!=null&&str4.length()>0) {
					 showWarningMessage(str2+"������δ��˵���,����ʧ�ܡ�"+"\n"+str3+"�����ѷ��͹���"+"\n"+str4);
				}else if (str1 != null && str1.length()>0 && str3 != null&&str3.length()>0 && str4!=null&&str4.length()>0) {
					 showWarningMessage(str1+"���ݷ��ͳɹ���"+"\n"+str3+"�����ѷ��͹���"+"\n"+str4);
				}else if (str1 != null && str1.length()>0 && str2 != null && str2.length()>0 ) {
	        		 showWarningMessage(str1+"���ݷ��ͳɹ���"+"\n"+str2+"������δ��˵���,����ʧ�ܡ�");
				}else if(str1 != null && str1.length()>0  && str3 != null && str3.length()>0){
					 showWarningMessage(str1+"���ݷ��ͳɹ���"+"\n"+str3+"�����ѷ��͹���");
				}else if (str2 != null && str2.length()>0 && str3 != null && str3.length()>0 ) {
					  showWarningMessage(str2+"������δ��˵���,����ʧ�ܡ�"+"\n"+str3+"�����ѷ��͹���");
	        	}else if (str1 != null && str1.length()>0 && str4!=null&&str4.length()>0) {
					 showWarningMessage(str1+"���ݷ��ͳɹ���"+"\n"+str4);
				} else if (str2 != null && str2.length()>0 && str4!=null&&str4.length()>0) {
					 showWarningMessage(str2+"������δ��˵���,����ʧ�ܡ�"+"\n"+str4);
				}else if (str3!= null && str3.length()>0 && str4!=null&&str4.length()>0) {
					showWarningMessage(str3+"�����ѷ��͹���"+"\n"+str4);
				}else if(str1 != null && str1.length()>0){
				    showWarningMessage(str1+"���ݷ��ͳɹ�"+"��");
				}else if(str2 != null && str2.length()>0 ){
				    showWarningMessage(str2+"������δ��˵���,����ʧ�ܡ�");
				}else if(str3!= null && str3.length()>0){
					showWarningMessage(str3+"�����ѷ��͹���");
				}else if (str4!=null&&str4.length()>0) {
					showWarningMessage(str4+"��");
				}         	
	        }else if(showing){
	        	//��Ƭ����
	        	DJZBVO vo = new DJZBVO();
				try {
					vo = (DJZBVO)djPanel.getArapDjPanel1().getBillCardPanelDj().getBillData ().getBillValueVO(DJZBVO.class.getName(), DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName());
				} catch (Exception e) {
					showErrorMessage(e.getMessage().toString());
					e.printStackTrace();
					return;
				}
				DJZBHeaderVO hvo = (DJZBHeaderVO) vo.getParentVO();
				DJZBItemVO[] bvo = (DJZBItemVO[]) vo.getChildrenVO();
				String djbh = hvo.getAttributeValue("djbh").toString();				
				String judgeDJBH = judgeDJBH(djbh);
	      		if(judgeDJBH == null){//�жϵ����Ƿ��Ѵ���
	      			String judgeCurrency = judgeCurrency(bvo[0].getBzbm());//�жϱ���
	  				Map custcode = custcode(hvo);//���̱���
	  				String queryLRR = queryLRR(hvo.getLrr());
	  				UFBoolean prepay = hvo.getPrepay();//���㷽ʽ ==Ԥ�ո���־
	  				String jsfs = String.valueOf(prepay);
	  				if("Y".equals(jsfs)){
	  					jsfs = "D";
	  				}else if ("N".equals(jsfs)) {
	  					jsfs = "C";
	  				}
	  				Integer djzt = hvo.getDjzt();
				    if(djzt == 2){//����״̬ == 2   �����
		  				if ("CNY".endsWith(judgeCurrency)) {
		  					boolean nxfp = nxfp(hvo,str4,bvo,djbh,custcode,judgeCurrency,jsfs,queryLRR);//���۷�Ʊ
		  					if(nxfp==true){
		  						showWarningMessage(djbh+"���ݷ��ͳɹ�");
		  					}else{
		  						showWarningMessage(str4+"��");
							}
		  				}else {
		  					//�ж�һ�º��ر����Ƿ���  ���򴫱��  û����˶�	
		  					Map kpTaxCode = kpTaxCode(hvo.getDwbm());
		  					String chCode = selectInvl(hvo);//�������
		  					Map queryInv = QueryInv(chCode);						
		  					if(queryInv.get("memo")==null||queryInv.get("memo")==""){
		  						showWarningMessage(djbh+"��������б�ע�����ر��룩��Ҫά��");
		  					}else {
		  						boolean wxfp = wxfp(hvo,str4,bvo,kpTaxCode,chCode,queryInv,djbh,custcode,judgeCurrency,jsfs,queryLRR);//������Ʊ		  						
		  						if(wxfp == true){
		  							showWarningMessage(djbh+"���ݷ��ͳɹ�");
		  						}else {
		  							showWarningMessage(str4+"��");
								}
		  					}
		  				}
				    }else {
				    	showWarningMessage(djbh+"������δ��˵���,����ʧ�ܡ�");	
					}
	      		}else {
	      			showWarningMessage(djbh+"�ѷ��͹���");
				}	
			}				  
		}   
  	}
  
  /**
   * ���۷�Ʊ��������
   * wy
   * 2019/10/11
   * @param hvo
   * @param bvo
   * @param djbh
   * @param custcode
   * @param judgeCurrency
   */
  @SuppressWarnings({ "unchecked", "static-access" })
  public boolean nxfp(DJZBHeaderVO hvo,StringBuffer str,DJZBItemVO[] bvo,String djbh,Map custcode,String judgeCurrency,String jsfs,String queryLRR){	    	  	
	  	boolean flag = false;
	  	String ywlx = ywlx(hvo.getXslxbm());
	    if("���ӹ���Ʒ����ҵ������".equals(ywlx)){
	    	ywlx = "DX";
		}else {
			ywlx = "ZX";
		}
	  	Map BodyMap = selectCubasdoc(djbh);
	    Map kpTaxCode = kpTaxCode(hvo.getDwbm());
	    String chCode = selectInvl(hvo); //�������code
	    Map queryInv = QueryInv(chCode);
	    Map queryHT = queryHT(chCode); //��ͬ
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nyr = sdf.format(date);	//��ȡ��ǰϵͳʱ��	
		String nx = "���۷�Ʊ��������";
		String djlx = DJLX(nx); //��������
		String queryZjldw = queryZjldw((String)queryInv.get("pk_measdoc"));
		UFDouble jfshl = new UFDouble();//��Ʊ����	
		for (int i = 0; i < bvo.length; i++) {
			 UFDouble jfsh = bvo[i].getJfshl();
			 jfshl = jfshl.add(jfsh);
		}
		System.out.println("�ܷ�Ʊ����="+jfshl);

		//˰��ת��
		int intValue = bvo[0].getSl().intValue();
		DecimalFormat df = new DecimalFormat("0.00");//��ʽ��С��  
		String sl = df.format((float)intValue/100);//���ص���String����  	
		
		UFDouble Jfybwsje = new UFDouble(); //��Ʊ���(����˰)= �ܻ���  
		for (int i = 0; i < bvo.length; i++) {
			UFDouble jfybwsje2 = bvo[i].getJfybwsje();
			Jfybwsje = Jfybwsje.add(jfybwsje2);
		}
		System.out.println("��Ʊ���(����˰)="+Jfybwsje);
		
		/*bvo[t].getJfybsj()˰�� =��Ʊ˰���  */
		UFDouble fpsje = new UFDouble();
		for (int i = 0; i < bvo.length; i++) {
			UFDouble jfybsj = bvo[i].getJfybsj();
			fpsje = fpsje.add(jfybsj);
		}
		System.out.println("��Ʊ˰��� ="+fpsje);
		
		UFDouble Jfybje = new UFDouble();//��Ʊ��˰�ϼƽ��  (= ��˰��  + �ܻ��� )   
		for (int i = 0; i < bvo.length; i++) {
			UFDouble jfybje2 = bvo[i].getJfybje();
			Jfybje = Jfybje.add(jfybje2);
		}
		
		UFDouble dj = new UFDouble();//��Ʊ�۸񣨲���˰��
		for (int i = 0; i < bvo.length; i++) {
			UFDouble dj2 = bvo[i].getDj();
			dj = dj.add(dj2);
		}	
		
		List judgeNXFPT = judgeNXFPT(hvo,bvo,nyr,djbh, custcode,judgeCurrency,jsfs,queryLRR,jfshl,Jfybwsje,dj,Jfybje,sl);
		if(judgeNXFPT.get(0).equals("0")){
			str.append(djbh+judgeNXFPT.get(1).toString());		
			return flag;
		}		
		String queryzrzx = queryzrzx(bvo[0].getDeptid());
		String strh = djbh.substring(djbh.length() -3,djbh.length());   //��ȡ
		
		//���۷�Ʊͷ
		JSONObject val = new JSONObject();
			 val.put("COMPANY_ID",(String) kpTaxCode.get("def8"));//��˾��                                                     
			 val.put("COST_CENTER",queryzrzx);//�������� (ҵ��)     
			 System.out.println("�������� (ҵ��)=="+queryzrzx(bvo[0].getDeptid()));
			 val.put("BILL_TYPE",djlx);//��������                        
			 val.put("BILL_NO", hvo.getDjbh()==null?null:hvo.getDjbh());//ҵ�񵥾ݺ�           Ӧ�յ���        		  
			 val.put("INVOICE_NO", hvo.getZyx17()); //��Ʊ��      
			 val.put("INVOICE_CODE",BodyMap.get("ccreditnum").toString().substring(0, 12));//��Ʊ����                                    
			 val.put("INVOICE_SORT",fplx(hvo.getZyx28()));//��Ʊ����                                                       
			 val.put("USER_TYPE", "K");//�����û�����        
			 val.put("SETTLE_USER_CODE", custcode.get("custcode").toString().substring(0, 6));//�����û�����                               
			 val.put("FIN_USER_CODE", custcode.get("custcode").toString().substring(0, 6));//�����û�����                                       
			 val.put("FINA_COST_CENTER",financeCode(hvo.getDwbm(),queryzrzx));//������������              ����   
			 System.out.println("������������=="+financeCode(hvo.getDwbm(),queryzrzx)); 
			 val.put("INVOICE_WT",jfshl==null?null:String.format("%.6f",Double.valueOf(jfshl.toString())));//��Ʊ����           ��Ʊ����                       
			 val.put("INVOICE_AMT",Jfybwsje == null?null:String.format("%.2f",Double.valueOf(Jfybwsje.toString())));//��Ʊ���(����˰)  
			 val.put("INVOICE_PRICE",dj==null?null:String.format("%.2f",Double.valueOf(dj.toString())));//��Ʊ�۸񣨲���˰��			 
			 val.put("INVOICE_TAX_AMT",fpsje==null?null:String.format("%.2f",Double.valueOf(fpsje.toString())));//��Ʊ˰��� 
			 val.put("INVOICE_SUM_AMT",Jfybje==null?null:String.format("%.2f",Double.valueOf(Jfybje.toString())));//��Ʊ��˰�ϼƽ��     
			 val.put("TAX_RATE",sl==null?null:String.format("%.2f",Double.valueOf(sl.toString())));//˰��  
			 System.out.println("��ͷ˰��1= "+sl);
			 val.put("PAPER_DREW_DATE", hvo.getDjrq()==null?null:hvo.getDjrq().toString().replaceAll("-", ""));//��Ʊ���� 
			 val.put("PAPER_DREW_NAME",queryLRR);//��Ʊ��    �Ƶ���
			 val.put("END_DREW_DATE",hvo.getDjrq()==null?null:hvo.getDjrq().toString().replaceAll("-", ""));//��ֹ��Ʊ����       	
			 val.put("DREW_DEPT_NAME",(String) kpTaxCode.get("unitname"));//��Ʊ��λ����     
			 val.put("DREW_DEPT_TAX_NO",(String) kpTaxCode.get("taxcode"));//��Ʊ��λ˰��       
			 val.put("DREW_ADDR_TEL",null);//��Ʊ��ַ�绰
			 val.put("DREW_BANK_NAME",hvo.getSkyhmc()==null?null:hvo.getSkyhmc());//��Ʊ��������   	
			 val.put("DREW_BANK_ACCT",hvo.getSkyhzh()==null?null:hvo.getSkyhzh());//��Ʊ�����ʺ�    				
			 val.put("PUR_TAX_NO",custcode.get("taxpayerid"));//������λ˰�� 
			 val.put("PUR_ADDR_TEL", null);//������ַ�绰  
			 val.put("PUR_BANK_NAME",BodyMap.get("accname"));//������������  
			 val.put("PUR_ACCT_NO",BodyMap.get("account"));//���������ʺ�   
			 val.put("SETTLE_METHOD",jsfs);//���㷽ʽ    	�ո����־prepay	  ���㷽ʽ  "C"
			 val.put("SALE_MODE","0");//�����ۣ�ó�׷�ʽ   
			 val.put("SETTLE_ID",person(queryLRR,hvo.getDwbm()));//�����˹���         ��Ʊ�˹���
			 val.put("DESTINE_ACCT_PERIOD",nyr.replaceAll("-", "").substring(0, 6));//Ԥ������    ��ǰϵͳ��ȡ6λ
			 val.put("VOUCHER_DESC",BodyMap.get("vnote")==null?" ":BodyMap.get("vnote"));//��ƱժҪ     ��ע  
			 val.put("CURRENCY_CODE",judgeCurrency);//���ִ���
			 val.put("AFFIX_NUM",null);//��������
			 val.put("CARRIER",null);//������
			 val.put("RECIPIENT",null);//������
			 val.put("SYSTEM_ID","JC");//ϵͳ��
			 val.put("FREIGHT_AMT","0.0");//�˷Ѷ�
			 
		   //���۷�Ʊ��ϸ��Ϣ
			 JSONArray bvals = new JSONArray();			
			 for (int t = 0; t < bvo.length; t++) {
				 int x=(int)(Math.random()*900)+100;
				 List judgeNXFPB = judgeNXFPB(hvo,bvo,nyr,djbh,custcode,judgeCurrency,jsfs,queryLRR,t);
				 if(judgeNXFPB.get(0).equals("0")){
					str.append(djbh+judgeNXFPB.get(1).toString());	
					return flag;
				 }
				 JSONObject bval = new JSONObject();
				 bval.put("COMPANY_ID",(String) kpTaxCode.get("def8"));//��˾��
				 bval.put("COST_CENTER",queryzrzx(bvo[t].getDeptid()));//��������     ����   deptid
				 bval.put("BILL_TYPE",djlx);//��������
				 bval.put("BILL_NO",hvo.getDjbh()==null?null:hvo.getDjbh());//ҵ�񵥾ݺ�     Ӧ�յ���
				 bval.put("INVOICE_NO",hvo.getZyx17());//��Ʊ��    invoiceno	��Ʊ��     hvo.getZyx17()==null?"fph123":hvo.getZyx17().toString()
				 bval.put("INVOICE_CODE",BodyMap.get("ccreditnum").toString().substring(0, 12));//��Ʊ����
				 bval.put("FACTORY_SEQ_ID",(String) kpTaxCode.get("def8"));//���쵥Ԫ��֯������������
				 bval.put("THREE_READY_NO",bvo[t].getDdh()+strh+x+(t+1));//������(ҵ�񵥾������)   ҵ�񵥾ݺţ������ţ�+�к�
				 System.out.println("������(ҵ�񵥾������)="+bvo[t].getDdh()+strh+x+(t+1));
				 bval.put("PRODUCT_CODE", queryInv.get("invclasscode")+""+ywlx);//����Ʒ����/��������  "8987"  25012401  queryInv.get("invclasscode")
				 bval.put("PRODUCT_CODE_NAME",queryInv.get("invclassname"));//������������    �������
				 bval.put("CONTRACT_TYPE",queryHT.get("typecode"));//��ͬ���ͱ���
				 bval.put("CONTRACT_TYPE_NAME",queryHT.get("typename"));//��ͬ��������
				 bval.put("BALANCE_DUE","");//β���־     
				 bval.put("SETTLE_METHOD",jsfs);//���㷽ʽ
				 bval.put("CONTRACT_NO","");//��Լ��
				 bval.put("PROJECT_NO","");//��Ŀ��
				 bval.put("PROJECT_NAME","");//��Ŀ����
				 bval.put("ORDER_NO","");//��ͬ��
				 bval.put("BUSY_TYPE",queryInv.get("invclasscode").toString().substring(0, 4));//ҵ������ 
				 bval.put("THREE_READY_WT",bvo[t].getJfshl());//������  
				 System.out.println("������  ="+bvo[t].getJfshl());  
				 bval.put("DREW_QUANTITY",bvo[t].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[t].getJfshl().toString())));//��Ʊ����
				 bval.put("DREW_MEASURE_UNIT","0");//��Ʊ������λ
				 bval.put("GOOD_AMT",bvo[t].getJfybwsje()==null?null:String.format("%.2f",Double.valueOf(bvo[t].getJfybwsje().toString())));//����
				 System.out.println("����1=="+bvo[t].getJfybwsje());
				 bval.put("TAX_AMT",bvo[t].getJfybsj()==null?null:String.format("%.2f",Double.valueOf(bvo[t].getJfybsj().toString())));//˰�� 
				 System.out.println("˰��1=="+bvo[t].getJfybsj());
				 bval.put("SETTLE_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//�����û�����
				 bval.put("FIN_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//�����û�����
				 bval.put("UPLOAD_DATE",nyr.replace("-", "").replace(" ", "").replace(":", ""));//��������                                                 ϵͳ����
				 bval.put("THREE_READY_DATE",nyr.replace("-", "").replace(" ", "").replace(":", ""));//��������(��������)    ϵͳ����
				 int intValue1 = bvo[t].getSl().intValue();
				 DecimalFormat df1 = new DecimalFormat("0.00");//��ʽ��С��  
				 String sl1 = df1.format((float)intValue1/100);//���ص���String����  	   
				 bval.put("TAX_RATE",sl1);//˰�� 
				 System.out.println("����˰��= "+sl1);
				 bval.put("SHOPSIGN","");//�ƺ�
				 bval.put("MODEL",bvo[t].getGgxh()==null?" ":bvo[t].getGgxh());//���
				 bval.put("SALE_UNIT_PRICE", bvo[t].getHsdj()==null?null:String.format("%.2f",Double.valueOf( bvo[t].getHsdj().toString())));//���۵��ۣ�����˰��
				 bval.put("STATUS","");//״̬
				 bval.put("SYSTEM_ID","JC");//ϵͳ�� 
				 bval.put("FREIGHT_AMT","0.0");//�˷Ѷ�
				 bval.put("PROD_CNAME","");//Ʒ����������      ����ΪN(��ֵ)
				 bval.put("THREE_QUANTITY","");//�����˷�����
				 bval.put("IMAGE_URL","");//Ӱ����ĵ�ַ
				 bvals.add(bval);
			 }		       
			 val.put("bodylist",bvals );
			 //��Ʊ��Ϣ
			 JSONArray bval = new JSONArray();				 
			 for (int i = 0; i < bvo.length; i++) {  
				 List judgeNXFPMX = judgeNXFPMX(hvo, bvo, nyr, djbh, custcode, judgeCurrency, jsfs, queryLRR, i);
				 if(judgeNXFPMX.get(0).equals("0")){
					 str.append(djbh+judgeNXFPMX.get(1).toString());	
					return flag;
				 }
				 JSONObject bva = new JSONObject();
 	        		bva.put("COMPANY_ID",(String) kpTaxCode.get("def8"));//��˾��
 	        		bva.put("COST_CENTER",queryzrzx(bvo[i].getDeptid()));//��������
 	        		System.out.println("��������3=="+queryzrzx(bvo[i].getDeptid()));
 	        		bva.put("BILL_TYPE",djlx);//��������
 	        		bva.put("BILL_NO",hvo.getDjbh()==null?null:hvo.getDjbh());//ҵ�񵥾ݺ�
 	        		bva.put("SERIAL_NO",i+1);//���
 	        		bva.put("PRODUCT_CODE",queryInv.get("invclassname"));//Ʒ��       ��������
 	        		bva.put("DREW_QUANTITY",bvo[i].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//���� 	jfshl
 	        		bva.put("SALE_UNIT_PRICE",bvo[i].getDj()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getDj().toString())));//����
 	        		bva.put("GOOD_AMT",bvo[i].getJfybwsje()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybwsje().toString())));//���������˰��
 	        		System.out.println("����=="+bvo[i].getJfybwsje());
 	        		bva.put("TAX_AMT",bvo[i].getJfybsj()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybsj().toString())));//˰��
 	        		System.out.println("˰��=="+bvo[i].getJfybsj());
 	        		bva.put("SETTLE_AMT",bvo[i].getJfybsj()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybsj().toString())));//��˰���
 	        		bva.put("DREW_MEASURE_UNIT",queryZjldw);//��λ  	��������λ     pk_measdoc
 	        		int intValue2 = bvo[i].getSl().intValue();
 					DecimalFormat df2 = new DecimalFormat("0.00");//��ʽ��С��  
 					String sl3 = df2.format((float)intValue2/100);//���ص���String����  	   
 	        		bva.put("TAX_RATE",sl3);//˰��
 	        		System.out.println("��Ʊ��ϸ˰��="+sl3);
 	        		bva.put("MODEL",bvo[i].getGgxh()==null?" ":bvo[i].getGgxh()); //����ͺ�
 	        		bva.put("SYSTEM_ID","JC");//ϵͳ��
 	        		bval.add(bva);
			}			 
			 JSONArray JSONArray = new JSONArray();
			 JSONArray bodyarr = new JSONArray();	
			 IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName());
			 JSONObject s = ifc.assembleItfData(val, "SALEINVOICE_H_SEND","SALEINVOICE_B_SEND");
			 String contents = null;
			 for (int i = 0; i < bvo.length; i++) {
				 JSONObject a = ifc.assembleItfData(bval.getJSONObject(i), "SALEINVOICE_BILLDEATILS_H_SEND",null);	
				 String states = (String)a.get("state"); 
				 if("success".equals(states)){					 
					 contents = a.getString("content");
					 contents=contents.replace("[", "").replace("]", "");
					 bodyarr.add(contents);
			     }else{
			    	 showWarningMessage(djbh+"����ʧ��");
			    	 flag = false;
			     }
			 }
			 System.out.println(s);
			 System.out.println(bodyarr);
			 String state = (String)s.get("state"); 		     		    
		     if("success".equals(state)){ 
 		    	 String content = s.getString("content"); 		    	 
		         JSONArray jsonArrays = new JSONArray(); 
		         jsonArrays = JSONArray.fromObject(content);		    
		         JSONObject jsObject =(JSONObject)jsonArrays.get(0);//���۷�Ʊ��ͷ
		         JSONArray jArray = new JSONArray();
		         for (int i = 1; i < jsonArrays.size(); i++) {
		        	 JSONObject jsObject1 = (JSONObject)jsonArrays.get(i);  
		        	 jArray.add(jsObject1); //���۷�Ʊ��ϸ��Ϣ	
				 }
		         JSONArray jArrayHead = new JSONArray();
		         jArrayHead.add(jsObject);		         
		         boolean send1 = send(bodyarr, djbh);//��Ʊ��ϸ  ����1
		         boolean send2 = send(jArray, djbh);//2
		         boolean send3 = send(jArrayHead, djbh);//3
		         if(send1 == true && send2 == true && send3 == true){
		        	 updateDJBH(djbh,nyr);	
		        	 flag = true;
		         }else {
		        	 showWarningMessage(djbh+"����ʧ��");
			    	 flag = false;
				 }		             
		     }else{
		    	 showWarningMessage(djbh+"����ʧ��");
		    	 flag = false;
		     }
			return flag;
  		}
  /**
   * �������ʹ����
   * wy
   * 2019/11/14
   * @param jsonarray
   * @param djbh
   * @return
   */
  public boolean send(JSONArray jsonarray,String djbh){
	  boolean flag = false;
	  for (int i = 0; i < jsonarray.size(); i++) {
		IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName());
		JSONObject rjb = (JSONObject) jsonarray.get(i); 
        JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
        if("success".equals(rs.getString("state"))){ 
       	 	flag = true;
        }else{ 
       	 	flag = false;
        } 
	 }
	return flag;
  }
  
  /**
   * ���۷�Ʊ��������
   * wy
   * 2019/10/11
   * @param hvo
   * @param bvo
   * @param djbh
   * @param custcode
   * @param judgeCurrency
   */
  @SuppressWarnings("unchecked")
	public boolean wxfp(DJZBHeaderVO hvo,StringBuffer str,DJZBItemVO[] bvo,Map kpTaxCode,String chCode,Map queryInv,String djbh,Map custcode,String judgeCurrency,String jsfs,String queryLRR){
	  boolean flag = false;
	  Map BodyMap = selectCubasdoc(djbh);
	  String ywlx = ywlx(hvo.getXslxbm());
	  if("��Ʒ����ҵ������".equals(ywlx)){
		ywlx = "ZX";
	  }else if("���ӹ���Ʒ����ҵ������".equals(ywlx)){
		ywlx = "DX";
	  }
	  UFBoolean prepay = hvo.getPrepay();//���㷽ʽ ==Ԥ�ո���־
	  String jsfs1 = String.valueOf(prepay);
	  if("Y".equals(jsfs1)){
		  jsfs1 = "6";
	  }else if ("N".equals(jsfs1)) {
		  jsfs1 = "1";
	  }	
	  Date date = new Date();
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  String nyr = sdf.format(date);	//��ȡ��ǰϵͳʱ��	
	  
	  String wx = "���۷�Ʊ��������";
	  UFDouble Jfybsj = new UFDouble();//��˰��
	  for (int i = 0; i < bvo.length; i++) {
		  UFDouble jfybsj2 = bvo[i].getJfybsj();
		  Jfybsj = Jfybsj.add(jfybsj2);
	  }			  
	  //�跽ԭ����˰���   jfybwsje = ����       �跽ԭ�ҽ��    jfybje =��˰�ϼ�        �跽ԭ��˰��   Jfybsj = ˰��	  	  
	  UFDouble Jfybwsje = new UFDouble();//�ܻ��� 
	  for (int i = 0; i < bvo.length; i++) {
		  UFDouble jfybwsje2 = bvo[i].getJfybwsje();
		  Jfybwsje = Jfybwsje.add(jfybwsje2);
	  }
	  
	  UFDouble zje = new UFDouble();//��Ʊ����Ʊ�ܽ�   = ��˰�� + �ܻ���
	  zje = Jfybsj.add(Jfybwsje);		  
	  
	  UFDouble jfshl = new UFDouble();//��Ʊ����
	  for (int i = 0; i < bvo.length; i++) {
		  UFDouble jfsh = bvo[i].getJfshl();
		  jfshl = jfshl.add(jfsh);
	  }	
	  UFDouble Jfybje = new UFDouble();//��Ʊ��˰�ϼƽ��    
	  for (int i = 0; i < bvo.length; i++) {
		  UFDouble jfybje2 = bvo[i].getJfybje();
		  Jfybje = Jfybje.add(jfybje2);
	  }			
	  Map queryHT = queryHT(chCode); //��ͬ
	  List judgeWXPFT = judgeWXPFT( hvo, bvo, kpTaxCode, chCode, queryInv, custcode, judgeCurrency, jsfs, queryLRR, jfshl, Jfybsj, zje, Jfybje);
	  if(judgeWXPFT.get(0).equals("0")){
		  str.append(djbh+judgeWXPFT.get(1).toString());	
			return flag;
	  }	
	 //������Ʊͷ��Ϣ�ӿ�
	  JSONObject val = new JSONObject();
	    val.put("PROC_FLAG","0");//�����־
	    val.put("BILL_TYPE",DJLX(wx));//��������
	    val.put("F_INVOICE_NO",hvo.getZyx17()==null?null:hvo.getZyx17());//������Ʊ��
	    val.put("COMPANY_CODE",(String) kpTaxCode.get("def8")==null?"1016":(String) kpTaxCode.get("def8"));//����
	    val.put("COST_CENTER",queryzrzx(bvo[0].getDeptid()));//��������
	    val.put("CONTRACT_NO",null);//�����Լ��  --����
	    val.put("SETTLE_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//�����û�����--���̴���
	    val.put("SALE_MODE","0");//�����ۣ�ó�׷�ʽ
	    val.put("SETTLE_METHOD",jsfs);//���㷽ʽ
	    val.put("TRADE_TERMS",null);//�۸�����
	    val.put("CURRENCY_CODE",judgeCurrency);//���ִ���
	    val.put("LC_SERIAL_NO",null);//����֤��ˮ��--����
	    val.put("LC_NO",null);//����֤��--����
	    val.put("PROD_CODE", queryInv.get("invclasscode").toString().substring(0, 2));//Ʒ������--����������
	    val.put("PLATE_OR_COIL","");//�������
	    val.put("PROD_CNAME",queryInv.get("invclassname"));//Ʒ����������--�����������
	    val.put("PROD_ENAME","");//Ʒ��Ӣ������
	    val.put("PAPER_DREW_DATE", hvo.getDjrq()==null?null:hvo.getDjrq().toString().replaceAll("-", ""));//��Ʊ���� 
	    val.put("INVOICE_AMT",zje == null?null:String.format("%.2f",Double.valueOf(zje.toString())));//��Ʊ���
	    val.put("FOB_AMT",Jfybje==null?null:String.format("%.2f",Double.valueOf(Jfybje.toString())));//���
	    val.put("FREIGHT_AMT",null);//�˷Ѷ�--����
	    val.put("INSU_AMT",null);//���ѽ��--����
	    val.put("INVOICE_WT",jfshl==null?null:String.format("%.6f",Double.valueOf(jfshl.toString())));//��Ʊ���� --��Ʊ����
	    val.put("TAX_AMT",Jfybsj==null?null:String.format("%.2f",Double.valueOf(Jfybsj.toString())));//˰��
	    val.put("NATION_CODE",null);//�������--����
	    val.put("CAV_CODE",null);//���ں�������--����
	    val.put("CUSTOM_NO",null);//���ص���--����
	    val.put("EXPORT_DATE",null);//��������--����
	    val.put("CUSTOM_DECLARE_DATE",null);//�����걨����--����
	    val.put("CUSTOM_CHECKED_DATE",null);//������������--����
	    val.put("CUSTOM_BACK_DATE",null);//���ط�������--����
	    val.put("CUSTOM_INPUT_DATE",null);//���ص���������--����
	    val.put("CUSTOM_INPUT_ID",null);//���ص�������--����
	    val.put("CUSTOM_INPUT_NAME",null);//���ص�����������--����
	    val.put("SHIP_LOT_NO",null);//������--����
	    val.put("DATE_PORT_LEAVE",null);//�������--����
	    val.put("T_PORT_LEAVE",null);//���ʱ��--����
	    val.put("LOADING_PORT_NAME",null);//����������--����
	    val.put("PROFIT_NUM",null);//��Ʊ��--����
	    val.put("PROFIT_SITE",null);//��Ʊ�˵�ַ--����
	    val.put("BILL_OF_LADING_NO",null);//�������--����
	    val.put("GET_BILL_DATE",null);//�ᵥ����--����
	    val.put("MAIL_DATE",null);//�ĵ�����--����
	    val.put("NEG_BILL_GATH_ID",null);//�鸶���ݼ����˹���--����
	    val.put("NEG_BILL_GATH_NAME",null);//�鸶���ݼ���������--����
	    val.put("NEG_BILL_GATH_DATE",null);//�鸶���ݼ�������--����
	    val.put("NEG_BILL_GATH_TIME",null);//�鸶 ���ݼ���ʱ��--����
	    val.put("AS_BUSI_MAIN_CODE",null);//Ӧ��ҵ��������--����
	    val.put("AS_BUSI_DETL_CODE",null);//Ӧ��ҵ��ϸ�����--����
	    val.put("SYSTEM_ID","JC");//ϵͳ��
	    val.put("CONTRACT_TYPE",null);//��ͬ����--����
	    val.put("PRODUCT_CODE",queryInv.get("invclasscode")+""+ywlx);//����Ʒ����/��������--�������68
	    val.put("PROC_STATUS",null);//����״̬--����
	    val.put("PROC_MASSAGE",null);//����˵��--����
	    val.put("CREATE_NAME",null);//¼����--����
	    val.put("CREATE_ID",null);//¼���˹���--����
	    val.put("CREATE_DATE",null);//¼������--����
	    val.put("MODIFY_NAME",null);//�޸���--����
	    val.put("MODIFY_ID",null);//�޸��˹���--����
	    val.put("MODIFY_NAME",null);//�޸�����--����
		//������Ʊ��ϸ��Ϣ�ӿ�
	    JSONArray bvals = new JSONArray();				    
	    for (int i = 0; i < bvo.length; i++) {
	    	List judgeWXFPB = judgeWXFPB(hvo,bvo,i,kpTaxCode,chCode,queryInv,custcode,judgeCurrency, jsfs, queryLRR, zje,BodyMap);
	    	if(judgeWXFPB.get(0).equals("0")){
	    		str.append(djbh+judgeWXFPB.get(1).toString());	
				return flag;
			}	
	    	JSONObject val2 = new JSONObject();
	    	val2.put("PROC_FLAG","0");//�����־
	    	val2.put("BILL_TYPE",DJLX(wx));//��������
	    	val2.put("F_INVOICE_NO",hvo.getZyx17()==null?null:hvo.getZyx17());//������Ʊ��
	    	val2.put("COMPANY_CODE",(String) kpTaxCode.get("def8")==null?"1016":(String) kpTaxCode.get("def8"));//����
	    	val2.put("COST_CENTER",queryzrzx(bvo[i].getDeptid()));//��������
	    	val2.put("INVOICE_SEQ_NO",i+1);//��Ʊ��ϸ��
	    	val2.put("ORDER_NO",queryHT.get("ct_code").toString().substring(0, 10));//��ͬ��
	    	val2.put("CONTRACT_NO","");//��Լ��
	    	val2.put("CUSTOMS_PRODUCT_CODE",queryInv.get("memo"));//���ر���--�ṩĬ��ֵ  "76129090"  queryInv.get("avgprice")
	    	val2.put("INVOICE_WT",bvo[i].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//��Ʊ���������أ�
	    	val2.put("SALE_PRICE", bvo[i].getHsdj()==null?"0.0":String.format("%.2f",Double.valueOf( bvo[i].getHsdj().toString())));//���۵���
	    	val2.put("INV_AND_COMMIS_AMT",zje==null?null:String.format("%.2f",Double.valueOf(zje.toString())));//��Ӷ���   ����˰���
	    	val2.put("COMMIS_UNIT","0.0");//��λӶ��
	    	val2.put("SETTLE_PRICE",bvo[i].getDj()==null?"0.0":String.format("%.2f",Double.valueOf(bvo[i].getDj().toString())));//ȥӶ����--ͬ����
	    	val2.put("INVOICE_AMT",zje == null?null:String.format("%.2f",Double.valueOf(zje.toString())));//ȥӶ���--ͬ���
	    	val2.put("TAX_AMT",bvo[i].getJfybsj()==null?"":bvo[i].getJfybsj());//˰��
	    	val2.put("SYSTEM_ID","JC");//ϵͳ��
	    	val2.put("CREATE_NAME",queryLRR);//¼����--�Ƶ�������
	    	val2.put("CREATE_ID",person(queryLRR,hvo.getDwbm()));//¼���˹���--�Ƶ��˹��ţ�MDM
	    	val2.put("CREATE_DATE",BodyMap.get("dmakedate"));//¼������--�Ƶ�����
	    	val2.put("MODIFY_NAME","");//�޸���
	    	val2.put("MODIFY_ID","");//�޸��˹���
	    	val2.put("MODIFY_NAME","");//�޸�����
	    	bvals.add(val2);
	    }
		 val.put("bodylist", bvals);
		 JSONArray bval = new JSONArray();	//������Ʊ���ز����ϸ��Ϣ�ӿ�	
		 for (int i = 0; i < bvo.length; i++) {
			 List judgeWXHG = judgeWXHG( hvo, bvo, i, kpTaxCode, chCode, queryInv, custcode, judgeCurrency, jsfs, queryLRR,zje,BodyMap);
			 if(judgeWXHG.get(0).equals("0")){
				 str.append(djbh+judgeWXHG.get(1).toString());
				 return flag;
			 }	
			 JSONObject val3 = new JSONObject();
			    val3.put("PROC_FLAG","0");//�����־
			    val3.put("BILL_TYPE",DJLX(wx));//��������
			    val3.put("F_INVOICE_NO",hvo.getZyx17()==null?"":hvo.getZyx17());//������Ʊ��
			    val3.put("COMPANY_CODE",(String) kpTaxCode.get("def8")==null?"1016":(String) kpTaxCode.get("def8"));//����
			    val3.put("COST_CENTER",queryzrzx(bvo[i].getDeptid()));//��������
			    val3.put("SERIAL_NO",i+1);//���
			    val3.put("CUSTOMS_PRODUCT_CODE",queryInv.get("memo"));//������Ʒ����--���ر���HSCODE     ��def20���溣�����
			    val3.put("INVOICE_WT",bvo[i].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//��Ʊ����--����
			    val3.put("SALE_PRICE", bvo[i].getHsdj()==null?"0.0":String.format("%.2f",Double.valueOf( bvo[i].getHsdj().toString())));//���۵���
			    val3.put("INV_AND_COMMIS_AMT",bvo[i].getJfybwsje()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybwsje().toString())));//��Ӷ���
			    val3.put("SETTLE_PRICE",bvo[i].getDj()==null?"0.0":String.format("%.2f",Double.valueOf(bvo[i].getDj().toString())));//����۸�
			    val3.put("INVOICE_AMT",zje == null?null:String.format("%.2f",Double.valueOf(zje.toString())));//��Ʊ���
			    val3.put("TAX_AMT",bvo[i].getJfybsj()==null?"":bvo[i].getJfybsj());//˰��
			    val3.put("INVOICE_NO",hvo.getZyx17()==null?"":hvo.getZyx17());//���Ʒ�Ʊ��
			    val3.put("INVOICE_CODE",BodyMap.get("ccreditnum").toString().substring(0, 12));//��Ʊ����--�����ֶ�
			    val3.put("INVOICE_DATE",hvo.getDjrq()==null?"":hvo.getDjrq().toString().substring(0, 8));//��Ʊ����
			    val3.put("SYSTEM_ID","JC");//ϵͳ��
			    val3.put("CREATE_NAME","");//¼����
			    val3.put("CREATE_ID","");//¼���˹���
			    val3.put("CREATE_DATE","");//¼������
			    val3.put("MODIFY_NAME","");//�޸���
			    val3.put("MODIFY_ID","");//�޸��˹���
			    val3.put("MODIFY_NAME","");//�޸�����
			    bval.add(val3);
		 }
		 String strh = djbh.substring(djbh.length() -3,djbh.length());   //��ȡ
		 //����������Ϣ�ӿ� 			 
		 JSONArray bval2 = new JSONArray();
		 for (int i = 0; i < bvo.length; i++) {
			 int x=(int)(Math.random()*900)+100;
			 List judgeWXSD = judgeWXSD( hvo, bvo, i, djbh, kpTaxCode, chCode, queryInv, custcode, judgeCurrency, jsfs, queryLRR, zje, jsfs1);
			 if(judgeWXSD.get(0).equals("0")){
				 str.append(djbh+judgeWXSD.get(1).toString());
				 return flag;
			 }	
			 JSONObject val4 = new JSONObject();
			    val4.put("COMPANY_CODE",(String) kpTaxCode.get("def8")==null?"1016":(String) kpTaxCode.get("def8"));//����
			    val4.put("COST_CENTER", queryzrzx(bvo[i].getDeptid()));//��������
			    val4.put("BILL_TYPE",DJLX(wx));//��������
			    val4.put("THREE_READY_NO",bvo[i].getDdh()+strh+x);//������(ҵ�񵥾������--���ⵥ��
			    val4.put("F_INVOICE_NO",hvo.getZyx17()==null?"":hvo.getZyx17());//������Ʊ��
			    val4.put("CONTRACT_NO","");//��Լ��
			    val4.put("ORDER_NO",queryHT.get("ct_code").toString().substring(0, 10));//��ͬ��
			    val4.put("PROJECT_NO","");//��Ŀ��
			    val4.put("PROJECT_NAME",bvo[i].getXm_name()==null?"":bvo[i].getXm_name());//��Ŀ����
			    val4.put("THREE_READY_WT",String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//������
			    val4.put("DREW_QUANTITY",bvo[i].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//��Ʊ����
			    val4.put("SALE_PRICE",bvo[i].getDj()==null?"0.0":String.format("%.2f",Double.valueOf(bvo[i].getDj().toString())));//����
			    val4.put("DREW_MEASURE_UNIT",queryZjldw((String)queryInv.get("pk_measdoc")));//��Ʊ������λ
			    val4.put("GOOD_AMT",bvo[i].getJfybwsje()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybwsje().toString())));//����     
			    int intValue1 = bvo[i].getSl().intValue();
				DecimalFormat df1 = new DecimalFormat("0.00");//��ʽ��С��  
			    val4.put("TAX_RATE",df1.format((float)intValue1/100));//˰��
			    val4.put("TAX_AMT",bvo[i].getJfybsj()==null?"":bvo[i].getJfybsj());//˰��
			    val4.put("SETTLE_METHOD",jsfs1);//���㷽ʽ
			    val4.put("SETTLE_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//�����û�����
			    val4.put("FIN_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//�����û�����      
			    val4.put("PRODUCT_CODE", queryInv.get("invclasscode"));//����Ʒ���롢��������
			    val4.put("FACTORY_SEQ_ID","");//���쵥Ԫ��֯����
			    val4.put("AS_BUSI_MAIN_CODE","");//Ӧ��ҵ����� 
			    val4.put("AS_BUSI_DETL_CODE","");//Ӧ��ҵ��ϸ��
			    val4.put("ACCT_PERIOD_NO",BodyMap.get("djkjnd")+""+BodyMap.get("djkjqj"));//�����    ����  ǰ��λ
			    val4.put("PROD_NAME",queryInv.get("invclasscode"));//Ʒ��
			    val4.put("CUSTOMS_PRODUCT_CODE",queryInv.get("memo"));//������Ʒ����
			    val4.put("UPLOAD_DATE",new UFDateTime(System.currentTimeMillis()).toString().replace("-", "").replace(" ", "").replace(":", "").substring(0, 8));//��������     ����
			    val4.put("SYSTEM_ID","JC");//ϵͳ��
			    val4.put("CONTRACT_TYPE",queryInv.get("invclasscode").toString().substring(0, 4));//��ͬ���� ����ҵ�����ͣ�
			    val4.put("CREATE_NAME","");//¼����
			    val4.put("CREATE_ID","");//¼���˹���
			    val4.put("CREATE_DATE","");//¼������
			    val4.put("MODIFY_NAME","");//�޸���
			    val4.put("MODIFY_ID","");//�޸��˹���
			    val4.put("MODIFY_NAME","");//�޸�����
			    bval2.add(val4);
		 }
		IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
		JSONObject s = ifc.assembleItfData(val, "EXTERNALSALEINVOICE_H_SEND","EXTERNALSALEINVOICE_B_SEND"); 		 
		JSONArray bodyarr = new JSONArray();
		String content1 = null;
		for (int i = 0; i < bvo.length; i++) {//������Ʊ���ز����ϸ��Ϣ�ӿ�
			JSONObject s1 = ifc.assembleItfData(bval.getJSONObject(i), "EXTERNALSALEINVOICE_CUSTOMSSPLIT_H_SEND",null); 
			String states = (String)s1.get("state"); 
			if("success".equals(states)){					 
				content1 = s1.getString("content");
				content1=content1.replace("[", "").replace("]", "");
				bodyarr.add(content1);
		    }else{
		    	showWarningMessage(djbh+"����ʧ��");
		    	flag = false;
		    }
		 }		
		JSONArray bodyarr2 = new JSONArray();
		String content2 = null;
		for (int i = 0; i < bvo.length; i++) {//����������Ϣ�ӿ� 
			JSONObject s2 = ifc.assembleItfData(bval2.getJSONObject(i), "EXTERNALSALEINVOICE_THREEORDERS_H_SEND",null); 
			String states = (String)s2.get("state"); 
			if("success".equals(states)){					 
				content2 = s2.getString("content");
				content2=content2.replace("[", "").replace("]", "");
				bodyarr2.add(content2);
		    }else{
		    	showWarningMessage(djbh+"����ʧ��");
		    	flag = false;
		    }
		 }
		System.out.println(s);
		System.out.println(bodyarr);
		System.out.println(bodyarr2);		
		String state = (String) s.get("state"); 
		if("success".equals(state)){ 
		    String content = s.getString("content"); 		  
	        JSONArray jsonArrays = new JSONArray();
	        jsonArrays = JSONArray.fromObject(content);
	        JSONObject jsObject =(JSONObject)jsonArrays.get(0);//������Ʊͷ��Ϣ�ӿ�
	        JSONArray jArrayHead = new JSONArray();
	        jArrayHead.add(jsObject);
	        JSONArray jsObjectBody = new JSONArray();
	        for (int i = 1; i < jsonArrays.size(); i++) {
	        	 JSONObject jb =(JSONObject)jsonArrays.get(i);//������Ʊ��ϸ��Ϣ�ӿ�
	        	 jsObjectBody.add(jb);
			}	        	        		 	                
	        boolean send1 = send(bodyarr2, djbh);////����   ��������  1
	        boolean send2 = send(bodyarr, djbh);//������Ʊ���ز����ϸ��Ϣ�ӿ� 2
	        boolean send3 = send(jsObjectBody, djbh);//������Ʊ��ϸ��Ϣ�ӿ�  3
	        boolean send4 = send(jArrayHead, djbh);//������Ʊͷ��Ϣ�ӿ�  4	
	        if(send1 == true && send2 == true && send3 == true && send4 == true){
	        	updateDJBH(djbh,nyr);	
	        	flag = true;
	        }else {
	        	showWarningMessage(djbh+"����ʧ��");
		    	flag = false;
			}	
		}else{
	    	 showWarningMessage(djbh+"����ʧ��");
	    	 flag = false;
	     }
		return flag;
	 }
  //end
  /**
   * �������ͱ��
   * wy
   * 2019/11/14
   */
  public boolean wx(JSONArray jArray,String djbh){
	  boolean flag = false;
	  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
	  for (int i = 0; i < jArray.size(); i++) { 
          JSONObject rjb = (JSONObject)jArray.get(i); 
          JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
          if("success".equals(rs.getString("state"))){ 
          	 flag = true;
          }else{ 
          	 flag = false;
          } 
      } 	  
	  return flag;
  }
  
//add by zy 2019-10-15 ��ӱ�ƻس�����
//  List<String> list = new ArrayList<String>();
  @SuppressWarnings({ "restriction", "unchecked" })
  private void bcRetreat(){
	  List list = new ArrayList();
	  String corp = PoPublicUIClass.getLoginPk_corp();
	  IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
	  Boolean result = idetermineService.check(corp);
	  if(result){//�жϵ�ǰ��˾�Ƿ�Ϊ���ڹ�˾������ִ��
	  	 IUiPanel iPanel = getCurrentPanel();
		 @SuppressWarnings("unused")
		 DjPanel djPanel = null;//�������
		   if ((iPanel instanceof DjPanel))
		    {
		      djPanel = (DjPanel)iPanel;
		    }
		    List<DJZBVO> vos = getSelectedRowVO();//��ȡѡ������
			StringBuffer str = new StringBuffer();
			StringBuffer err = new StringBuffer();//������ʾ��Ϣ
			//�ж����б���滹�ǿ�Ƭ����
			boolean showing = djPanel.getArapDjPanel1().getBillCardPanelDj().isShowing();
			if(showing == false && vos.size()==0){
				showWarningMessage("�б���δѡ��Ҫ����Ƶ�����");
				return;
			}else if(showing == false && vos.size()>0){//���ѡ�е����ݴ�����
	        	//�б����
	        	for (int j = 0; j < vos.size(); j++) {
	        		DJZBHeaderVO hvo = (DJZBHeaderVO)vos.get(j).getParentVO();
	        		String djbh = vos.get(j).getParentVO().getAttributeValue("djbh").toString();
	        		String yn=judgeDJBH(djbh);
	        		if("Y".equals(yn)){
	        			System.out.println("���ݱ��"+djbh);
		        		Integer djzt = hvo.getDjzt();
		        		if(djzt == 2){//����״̬ == 2   �����
		        			if(list.contains(djbh)){
								str.append(djbh+",");
								continue;
							}else {
								  JSONObject val = new JSONObject();
								  DJZBItemVO[] bvo = (DJZBItemVO[])vos.get(j).getChildrenVO();
								  String cxlx="";
								  String judgeCurrency = judgeCurrency(bvo[0].getBzbm());//�жϱ���
								  if("CNY".equals(judgeCurrency)){
									  cxlx="C";
								  }else{
									  cxlx="F";
								  }
							      val.put("CANCLE_TYPE",cxlx);//��������
							      String zt=(String) kpTaxCode(bvo[0].getDwbm()).get("def8");
							      val.put("COMPANY_ID",zt);//����
							      val.put("SYSTEM_ID","JC");//ϵͳ��
							      Map map=queryFph(djbh);
							      System.out.println("map::::::::"+map);
							      val.put("BILL_NO",hvo.getDjbh());//ҵ�񵥾ݺ� -- ҵ�񵥾ݺ�
							      val.put("INVOICE_NO",map.get("vdef17")==null?"":map.get("vdef17"));//��Ʊ�� -- ֽ�ʷ�Ʊ��
							      val.put("INVOICE_CODE",map.get("ccreditnum")==null?"":map.get("ccreditnum"));//��Ʊ����
							      val.put("RESULT_DESC",cxlx);//��������
							      val.put("REMARK",cxlx);//��ע
							      
//								  ���ӿڵ���ʾ������---���ο���
								  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
								  JSONObject s = ifc.assembleItfData(val, "JCBCS8",null); 
								  System.out.println(s);
								  //JSONObject js = ifc.disassembleItfData(xbuss, "BCJCS1");//�������ӿ�ʾ���� 
								  String state = (String) s.get("state"); 
								  if("success".equals(state)){ 
								      String content = s.getString("content"); 
								      JSONArray sjrr =JSONArray.fromObject(content); //s.get("content"); 
								      if(sjrr.size()>0){ 
								          for (int i = 0; i < sjrr.size(); i++) { 
								              JSONObject rjb = (JSONObject) sjrr.get(i); 
								              JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
								          }
//								          if("success".equals(rs.getString("state"))){ 
							                  //�ɹ��߼� 
							            	  System.out.println("val:::::::"+val);
							            	  updateHC(djbh);
							            	  System.out.println("########���ͳɹ�########");
							              /*}else{ 
							                  //ʧ���߼� 
							              } */
								      } 
								  } 
							}
		        			if (str.length()==0 || str==null) {
//	      		        		showWarningMessage("���ͳɹ�");
	      		        		err.append("����"+str+"�س��ɹ�\n");
	      					}else {
//	      						showWarningMessage("���ͳɹ�,"+str+"�ѷ��͹���");
	      						err.append("����"+str+"�ѻس�����\n");
	      					}
		        		}else {
//						  	showWarningMessage("��ѡ������˵��ݣ�");
						  	err.append("����"+djbh+"δ��ˣ���ѡ������˵��ݣ�\n");
						}
	        		}else if("H".equals(yn)){
//	        			showWarningMessage("����"+djbh+"�ѻس���");
	        			err.append("����"+djbh+"�ѻس���\n");
	        		}else{
//	        			showWarningMessage("����"+djbh+"δ������ƣ����ܻس���");
	        			err.append("����"+djbh+"δ������ƣ����ܻس���\n");
	        		}
	        			
    				
	        	}	  
	        	if(err!=null || err.length()>0){
					showWarningMessage(err.toString());
				}
	        }else if(showing){
	        	//��Ƭ����
	        	DJZBVO vo = new DJZBVO();
				try {
					vo = (DJZBVO)djPanel.getArapDjPanel1().getBillCardPanelDj().getBillData().getBillValueVO(DJZBVO.class.getName(), DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName());
				} catch (Exception e) {
					showErrorMessage(e.getMessage().toString());
					e.printStackTrace();
					return;
				}
				DJZBHeaderVO hvo = (DJZBHeaderVO) vo.getParentVO();
				DJZBItemVO[] bvo = (DJZBItemVO[]) vo.getChildrenVO();
				String djbh=hvo.getDjbh().toString();
				String yn=judgeDJBH(djbh);
        		if("Y".equals(yn)){
        			Integer djzt = hvo.getDjzt();		
    				if(djzt == 2){//����״̬ == 2   �����
    					JSONObject val = new JSONObject();
    					  String cxlx="";
    					  String judgeCurrency = judgeCurrency(bvo[0].getBzbm());//�жϱ���
    					  
    					  if("CNY".equals(judgeCurrency)){
    						  cxlx="C";
    					  }else{
    						  cxlx="F";
    					  }
    				      val.put("CANCLE_TYPE",cxlx);//��������
    				      String zt=(String) kpTaxCode(bvo[0].getDwbm()).get("def8");
    				      val.put("COMPANY_ID",zt);//����
    				      val.put("SYSTEM_ID","JC");//ϵͳ��
    				      Map map=queryFph(djbh);
    				      val.put("BILL_NO",djbh);//ҵ�񵥾ݺ�
    				      val.put("INVOICE_NO",map.get("vdef17")==null?"":map.get("vdef17"));//��Ʊ��
    				      val.put("INVOICE_CODE",map.get("ccreditnum")==null?"":map.get("ccreditnum"));//��Ʊ����
    				      val.put("RESULT_DESC",cxlx);//��������
    				      val.put("REMARK",cxlx);//��ע
    				      
//    					  ���ӿڵ���ʾ������---���ο���
    					  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
    					  JSONObject s = ifc.assembleItfData(val, "JCBCS8",null); 
    					  System.out.println(s);
    					  //JSONObject js = ifc.disassembleItfData(xbuss, "BCJCS1");//�������ӿ�ʾ���� 
    					  String state = (String) s.get("state"); 
    					  if("success".equals(state)){ 
    					      String content = s.getString("content"); 
    					      JSONArray sjrr =JSONArray.fromObject(content); //s.get("content"); 
    					      if(sjrr.size()>0){ 
    					          for (int i = 0; i < sjrr.size(); i++) { 
    					              JSONObject rjb = (JSONObject) sjrr.get(i); 
    					              JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
    					          }
//    					          if("success".equals(rs.getString("state"))){ 
    				                  //�ɹ��߼� 
    					          	  updateHC(djbh);
    				            	  System.out.println("val:::::::"+val);
    				            	  System.out.println("########��Ƭ���ͳɹ�########");
    				              /*}else{ 
    				                  //ʧ���߼� 
    				              } */
    					      } 
    					  } 
    					
    					if (str.length()==0 || str==null) {
    		        		showWarningMessage("���ͳɹ�");
    					}else {
    						showWarningMessage("���ͳɹ�,"+str+"�ѷ��͹���");
    					} 	
    				}else {
    				  	showWarningMessage("��ѡ������˵��ݣ�");
    				}
        		}else{
        			showWarningMessage("����"+djbh+"δ������ƣ����ܻس���");
        		}
			}
			
		}   
  }
//  end
  
  
  
  
  
  /**
   * ����տ����cvm
   * pengjiajia
   * 2018��5��4��14:21:31
   */
  @SuppressWarnings("all")
  public void onButtonClicked(ButtonObject bo)
  {
	 //add Ӧ�մ���ư�ť�����¼�    wy  2019��9��10�� 
	  if (bo.getName().equals("Ӧ�մ����")) {
		  sendXbus(bo);
	   } 	
	  //end
	  if(bo.getName().equals("���")){
		  IUiPanel iPanel = getCurrentPanel();
		  DjPanel djPanel = null;//�������
		    if ((iPanel instanceof DjPanel))
		    {
		      djPanel = (DjPanel)iPanel;
		    }
		    List<DJZBVO> vos = getSelectedRowVO();
	        try {
	        	getCurrentPanel().onButtonClicked(bo);
			} catch (Exception e) {
				showErrorMessage(e.getMessage());
				e.printStackTrace();
				return;
			}			
	        if(vos.size()>0){//���ѡ�е�������ݴ���0
	        	 for(DJZBVO vo:vos){
	 	        	String djdl = (String)vo.getParentVO().getAttributeValue("djdl");//djdl�����ݴ��ࣨ�������ͣ�
	 	        	String corp = (String) vo.getParentVO().getAttributeValue("dwbm");//dwbm����λ����
	 	      		Object flag = vo.getParentVO().getAttributeValue("djzt");//djzt������״̬��1����δ��ˣ�2���������
	 	      		
	 	      		if (djdl.equals("sk")//sk�տ
							&& (corp.equals("1016") || corp.equals("1071")
									|| corp.equals("1103")
									|| corp.equals("1097")
									|| corp.equals("1017")
									|| corp.equals("1018") 
									|| corp.equals("1019")
									|| corp.equals("1107")
							)) {
	 	      			if(flag.equals(2)){
	 	      				String result = sendGather(vo);
	 	      				showHintMessage("�տ����CVM:"+result); 
	 	      			}
	 	      		} else if (djdl.equals("ys")//ysӦ�յ�
							&& (corp.equals("1016") || corp.equals("1071")
									|| corp.equals("1103")
									|| corp.equals("1097")
									|| corp.equals("1017") 
									|| corp.equals("1018")
									|| corp.equals("1019")
									|| corp.equals("1107")
							)) {
	 	      			if(flag.equals(2)){
	 	      			    String result = saleSendcvm(vo);
	 	      			    showHintMessage("Ӧ�յ�����CVM:"+result);
	 	      			}
	 	      		}	 	      			 	      	
	 	        }
	        }else{
	        	DJZBVO vo = new DJZBVO();
				try {
					vo = (DJZBVO)djPanel.getArapDjPanel1().getBillCardPanelDj().getBillData().getBillValueVO(DJZBVO.class.getName(), DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName());
				} catch (Exception e) {
					showErrorMessage(e.getMessage().toString());
					e.printStackTrace();
					return;
				}
	        	String djdl = (String)vo.getParentVO().getAttributeValue("djdl");
 	      	    String corp = (String) vo.getParentVO().getAttributeValue("dwbm");
 	      		Object flag = vo.getParentVO().getAttributeValue("djzt");
 	      		if (djdl.equals("sk")
						&& (corp.equals("1016") || corp.equals("1071")
								|| corp.equals("1103") || corp.equals("1097")
								|| corp.equals("1017") || corp.equals("1018") || corp.equals("1019")
								|| corp.equals("1107")
						)) {
 	      			if(flag.equals(2)){
 	      				String result = sendGather(vo);
 	      			    showHintMessage("�տ����CVM:"+result);
 	      			}
 	      		} else if (djdl.equals("ys")
						&& (corp.equals("1016") || corp.equals("1071")
								|| corp.equals("1103") || corp.equals("1097")
								|| corp.equals("1017") || corp.equals("1018")|| corp.equals("1019")
								|| corp.equals("1107"))) {
 	      			if(flag.equals(2)){
 	      			    String result = saleSendcvm(vo);
 	      			    showHintMessage("Ӧ�յ�����CVM:"+result);
 	      			}
 	      		}
	        }
	  }else if(bo.getName().equals("ɾ��")||bo.getName().equals("���ݲ���")){
		  List<DJZBVO> vos = getSelectedRowVO();
		  String pk_corp = (String) vos.get(0).getParentVO().getAttributeValue("dwbm");
		  if (pk_corp.equals("1016") || pk_corp.equals("1071")
					|| pk_corp.equals("1103") || pk_corp.equals("1097")
					|| pk_corp.equals("1017") || pk_corp.equals("1018")|| pk_corp.equals("1019")|| pk_corp.equals("1107")) {
			  showErrorMessage("������ɾ������,���öԳ嵥�ݽ��!");
			  return;
		  }
		  getCurrentPanel().onButtonClicked(bo);
	  
	  
	  }else if(bo.getName().equals("Ӧ�������")){//add by zsh 2019-10-15 ���Ӧ������ư�ť�����¼�
			try { 
				  String corps = PoPublicUIClass.getLoginPk_corp();
				IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
				  Boolean result = idetermineService.check(corps);
					if(result){
						transmission();
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(bo.getName().equals("��ƻس�")){//add by zy 2019-10-15 ��ӱ�ƻس���ť�����¼�
			  bcRetreat();
		}else{
		 StringBuffer str = new StringBuffer();
			  IUiPanel iPanel = getCurrentPanel();
			  DjPanel djPanel = null;//�������
			    if ((iPanel instanceof DjPanel))
			    {
			      djPanel = (DjPanel)iPanel;
			    }
			    List<DJZBVO> vos = getSelectedRowVO();
			    if(vos.size()>0){
			    	 for(DJZBVO vo:vos){
				String djbh =(String) vo.getParentVO().getAttributeValue("djbh");
			    String value =judgeDJBH(djbh);
	      		if(value!=null){
	      			str.append("\n"+"����+"+djbh+"�Ѿ����ͳɹ�,�޷������");
	      			}
			    	 }
			    }else{
			    	DJZBVO vo = new DJZBVO();					
					vo = (DJZBVO)djPanel.getArapDjPanel1().getBillCardPanelDj().getBillData().getBillValueVO(DJZBVO.class.getName(), DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName());
					String djbh =(String) vo.getParentVO().getAttributeValue("djbh");
				    String value =judgeDJBH(djbh);
		      		if(value!=null){
		      			str.append("\n"+"����+"+djbh+"�Ѿ����ͳɹ�,�޷������");
		      			}
			    }
			    if(bo.getName().equals("�����")&&str.length()>0&&str!=null){
			    	 showErrorMessage(str.toString());
			    	 return;
				}	
			    getCurrentPanel().onButtonClicked(bo);
	  }

  }
  
  /**
   * add by zsh 2019-10-15 ���Ӧ������ư�ť�����¼�
   */
	@SuppressWarnings({ "restriction", "unchecked" })
	public void transmission(){
		 IUiPanel iPanel = getCurrentPanel();
		 DjPanel djPanel = null;
	    if ((iPanel instanceof DjPanel))
	    {
	      djPanel = (DjPanel)iPanel;
	    }
	  
	    List<DJZBVO> vos = getSelectedRowVO();//��ȡѡ������
	    boolean show = djPanel.getArapDjPanel1().getBillCardPanelDj().isShowing();
	    if(vos.size()==0&&show==false){
	    	 showWarningMessage("��ѡ�񵥾�");
	    }
	   
	    if(vos.size()>0&&show==false){
	    	CxbusList(vos, djPanel);
	    }
	    else if(show){
	    	//��Ƭ����
        	DJZBVO vo = new DJZBVO();
			try {
				vo = (DJZBVO)djPanel.getArapDjPanel1().getBillCardPanelDj().getBillData ().getBillValueVO(DJZBVO.class.getName(), DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName());
				 CxbusCard(vo, djPanel);
			} catch (Exception e) {
				showErrorMessage(e.getMessage().toString());
				e.printStackTrace();
				return;
			}
	    }
	   
	}
	
	public static String BalnkVal(String val){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
  	  String value = "select pk_cubasdoc from bd_cumandoc  where pk_cumandoc ='"+val+"'";
  	  String result = new String();
  	  try {
			 result =(String) bs.executeQuery(value, new BeanProcessor(String.class));
			 return result;
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	

	/**
	   * ���ߵ��ݿͻ������ѯ��������Ϣ�Ϳ��̵�ַ��Ϣ
	   * add by zsh 2019-10-16
	   * @param bm
	   * @return
	   */
	@SuppressWarnings("unchecked")
	public static List<String> CubasdocVal(String val){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String value = "select DISTINCT nvl(doc.custcode,0) as custcode,nvl(doc.def13,0) as custprop,nvl(addr.addrname,'A') as addrname,nvl(addr.phone,0) as phone,nvl(doc.custname,0) as custname ,nvl(doc.Taxpayerid,0) as  bp2   from bd_cubasdoc doc " +
//						" inner join bd_custbank bank on doc.pk_cubasdoc = bank.pk_cubasdoc " +
						"	left join bd_custaddr addr  on  addr.pk_cubasdoc = doc.pk_cubasdoc where  doc.custcode ='"+val+"'";
						
		List<String> list = new ArrayList<String>();
		List<Map> lists = new ArrayList<Map>();
		 try {
			 lists = (List<Map>) bs.executeQuery(value, new MapListProcessor());
			 if(lists==null||lists.size()==0){
				 list.add(0,"");
				 list.add(1,"");
				 list.add(2,"");
				 list.add(3,"");
				 list.add(4,"");
				 list.add(5,"");
			 }else{
			 if(lists.get(0).get("custcode").equals("0")){
				 list.add(0,"");
			 }else{
			String custcode = lists.get(0).get("custcode").toString()==null?" ":lists.get(0).get("custcode").toString();
			 list.add(0,custcode);
			 }
			 if(lists.get(0).get("custprop").toString()==null){
				 list.add(1,"");
			 }else{
			String  custprop = lists.get(0).get("custprop").toString()==null?" ":lists.get(0).get("custprop").toString();
			 list.add(1,custprop);
			 }
			 if(lists.get(0).get("addrname").toString().equals("0")){
				 list.add(2,""); 
			 }else{
			String  addrname = lists.get(0).get("addrname").toString()==null?" ":lists.get(0).get("addrname").toString();
			 list.add(2,addrname);
			 }
			 if(lists.get(0).get("phone").toString().equals("0")){
				 list.add(3,"");
			 }else{
			 String  phone = lists.get(0).get("phone").toString();
			 list.add(3,phone);
			 }
			 if(lists.get(0).get("custname").toString().equals("0")){
				 list.add(4,""); 
			 }else{
			String  custname = lists.get(0).get("custname").toString()==null?" ":lists.get(0).get("custname").toString();
			 list.add(4,custname);
			 }
			 if(lists.get(0).get("bp2").equals("0")){
				 list.add(5,"");
			 }else{
			
			 list.add(5,lists.get(0).get("bp2").toString());
			 }
			 }
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
		/**
	   * ��ѯ������Ϣ
	   * add by zsh 2019-10-16
	   * @param bm
	   * @return
	   */
	  
	  @SuppressWarnings("unchecked")
	private List queryks(String res,String val ){
			//ǰ̨���ݲ�ѯ�������õ�����һ���ӿ�
		  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//�����տ����VO��õ�������
		  StringBuffer sb = new StringBuffer();
			sb.append(" select * from arap_djfb zb where zb.vouchid ='"+res+"' and nvl(zb.dr,0)=0");
			List Bodylist = new ArrayList();
			Map BodyMap = new HashMap();
			try {
				Bodylist = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
				for(int a =0;a<Bodylist.size();a++){
					BodyMap = (Map) Bodylist.get(a);
				}
			} catch (BusinessException e2) {
				e2.printStackTrace();
			}
			//����
			String custcode = (String) BodyMap.get("ksbm_cl");
		  List lists = new ArrayList();
		  String custsql = "select cubasdoc.custcode from bd_cumandoc cumandoc " +
			"left join bd_cubasdoc cubasdoc on cumandoc.pk_cubasdoc = cubasdoc.pk_cubasdoc " +
			"where cumandoc.pk_cumandoc = '"+custcode+"' and cumandoc.pk_corp ='"+val+"' and nvl(cumandoc.dr,0)=0";
	 		 Map custMap = new HashMap();
	 		 try {
			List list = (List) bs.executeQuery(custsql, new MapListProcessor());
			if(list.size()>0){
			custMap = (Map) list.get(0);
			lists.add(custMap.get("custcode"));
			return lists;
			}
	 		 } catch (BusinessException e) {
					e.printStackTrace();
					lists.add(e.getMessage().toString());
					return lists;
				}
		return null;
	  }
	 /**
	   * �ж��Ƿ������ˮ��
	   * add by zsh 2019-10-16
	   * @param bm
	   * @return
	   */
	@SuppressWarnings("unchecked")
	private List  reval(String res){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		StringBuffer sb = new StringBuffer();
		sb.append(" select BILLNO from itf_serial_numbers  where BILLNO ='"+res+"'");
		List Bodylist = new ArrayList();
		try {
			Bodylist = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			return Bodylist;
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return Bodylist;
	 }
	/**
	   * ��ѯ��ˮ��
	   * add by zsh 2019-10-16
	   * @param bm
	   * @return
	   */
	 @SuppressWarnings({ "unchecked", "null" })
	private String   SERIALNUM(String res){
		 String resval = "";
		 IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
	 	 StringBuffer sb = new StringBuffer();
	 	 sb.append(" select serialnum from itf_serial_numbers  where BILLNO ='"+res+"'");
	 	 List Bodylist = new ArrayList();
	 	 Map custMap = new HashMap();
 		try {
			Bodylist = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			if(Bodylist!=null||Bodylist.size()!=0){
				custMap = (Map) Bodylist.get(0);
			String val =custMap.get("serialnum").toString();
			System.out.println("��ˮ��"+val+"����"+val);
			return val.trim();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return resval;
	 }
	/**
	   * ��֧��λ��֯����
	   * add by zsh 2019-10-16
	   * @param bm
	   * @returnBD
	   */
	 @SuppressWarnings({ "unchecked", "null" })
	private String   BzCode(String res){
		String resval = "";
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		String value = "select def8 from bd_corp where pk_corp ='"+res+"'"; 
		List Bodylist = new ArrayList();
		 Map custMap = new HashMap();
		try {
			Bodylist = (List) bs.executeQuery(value.toString(), new MapListProcessor());
			if(Bodylist!=null||Bodylist.size()!=0){
				custMap = (Map) Bodylist.get(0);
			String val =custMap.get("def8").toString();
			return val.trim();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return resval;
	 }
	/**
	   * ��ѯ�����˺ź�����
	   * add by zsh 2019-10-16
	   * @param bm
	   * @returnBD
	   */
	 @SuppressWarnings({ "unchecked", "null" })
	public List SelectBank(String res,String corp){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		List resval = new ArrayList();
		List Bodylists = new ArrayList();
		Map custMaps = new HashMap();
		try {			
			String resv ="select cub.accname,cub.account from bd_cumandoc cum inner join bd_cubasdoc bas on bas.pk_cubasdoc = cum.pk_cubasdoc inner join bd_custbank cub on bas.pk_cubasdoc = cub.pk_cubasdoc where " +
					"cum.pk_cumandoc = '"+res+"'   and cum.pk_corp = '"+corp+"'  " +
					" and nvl(cum.dr, 0) = 0   and nvl(bas.dr, 0) = 0  and nvl(cub.dr, 0) = 0   and (cum.custflag = '1' OR cum.custflag = '3')   and (cum.sealflag is null AND (frozenflag = 'N' or frozenflag is null))";
			Bodylists = (List) bs.executeQuery(resv.toString(), new MapListProcessor());
			System.out.println(Bodylists.size());
			if(Bodylists!=null&&Bodylists.size()>0){
				custMaps = 	(Map) Bodylists.get(0);
			resval.add(0,custMaps.get("accname").toString());
			resval.add(1,custMaps.get("account").toString());
			return resval;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		resval.add(0,"���˺�����");
		return resval;
	 }
	/**
	   * ��ѯ���
	   * add by zsh 2019-10-16
	   * @param bm
	   * @returnBD
	   */ 	 
	@SuppressWarnings({ "unchecked", "null" })
	public String  invcl(String res){
		 String resval = "";
		  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		String value = "select cl.invclasscode from bd_invcl cl inner join bd_invbasdoc doc on cl.pk_invcl  =doc.pk_invcl    where doc.invcode ='"+res+"'"; 
		List Bodylist = new ArrayList();
		 Map custMap = new HashMap();
		try {
			Bodylist = (List) bs.executeQuery(value.toString(), new MapListProcessor());
			if(Bodylist!=null&&Bodylist.size()>0){
				custMap = (Map) Bodylist.get(0);				
			String val =custMap.get("invclasscode").toString();
			String va = val.trim().substring(0,2);
			if("21".equals(va)||"22".equals(va)||"F1".equals(va)||"F3".equals(va)){
				return val.trim().substring(0,2);
			}
			if(val.length()>=4){
			return val.trim().substring(0,4);
			}else{
				return val;
			}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return resval;
	}
/** 
 * ��ѯ��ͬ�� ��ͬ���� ���� ��Ʊ��
 * add by zsh 2019-10-16
 * @param bm
 * @returnBD
 */  
	@SuppressWarnings({ "unchecked", "null" })
	public List listVal(String res){
		 List list = new ArrayList(); 
		 @SuppressWarnings("unused")
		String resval = "";
		  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		String value = "SELECT	nvl(iv.vinvoicecode,0) as vinvoicecode,nvl(cur.currtypecode,0) as currtypecode,nvl(ct.ct_code,0) as ct_code,nvl(ct.ct_name,0) as ct_name,nvl(iv.vdef13,0) as vdef13 FROM	arap_djzb zb" +
				"	left JOIN arap_djfb fb ON zb.vouchid = fb.vouchid	left JOIN po_invoice iv ON iv.cinvoiceid = fb.ddlx" +
				"	left JOIN po_invoice_b ivb ON ivb.cinvoiceid = iv.cinvoiceid	left JOIN po_order_b pob ON pob.corderid = ivb.csourcebillid" +
				"	left JOIN ct_manage ct ON ct.pk_ct_manage = pob.ccontractid	" +
				"	left JOIN bd_currtype cur ON ivb.ccurrencytypeid = cur.pk_currtype WHERE	zb.djbh = '"+res+"'"; 
		List Bodylist = new ArrayList();
		 Map custMap = new HashMap();
		try {
			Bodylist = (List) bs.executeQuery(value.toString(), new MapListProcessor());
			if(Bodylist!=null&&Bodylist.size()>0){
				custMap = (Map) Bodylist.get(0);
			if(custMap.get("currtypecode").toString().equals("0")){
				list.add(0,"");
			}else{
			list.add(0,custMap.get("currtypecode").toString());	//����
			}
			if(custMap.get("ct_code").toString().equals("0")){
				list.add(1,"");
			}else{
			list.add(1,custMap.get("ct_code").toString());//��ͬ��
			}
			if(custMap.get("ct_name").toString().equals("0")){
				list.add(2,"");
			}else{
			list.add(2,custMap.get("ct_name").toString());//��ͬ����
			}
			if(custMap.get("vinvoicecode").toString().equals("0")){
				list.add(3,"");
			}else{
			list.add(3,custMap.get("vinvoicecode").toString());//��Ʊ��
			}
			if(custMap.get("vdef13").toString().equals("0")){
				list.add(4,"");
			}else{
			list.add(4,custMap.get("vdef13").toString());//��Ʊ����
			}
			return list;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		list.add(0,"A");
		return list;
		 
	}
	/** 
	 * ��Ʊ����
	 * add by zsh 2019-10-16
	 * @param bm
	 * @returnBD
	 */  
	@SuppressWarnings("unchecked")
	public String Fptype(String res){
		 @SuppressWarnings("unused")
		List list = new ArrayList();
		 String resval = "";
		  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		  String val ="	select typeno from MDMINVOICETYPE where pk_invoice ='"+res+"'";
		  List Bodylist = new ArrayList();
		  Map custMap = new HashMap();
			try {
				Bodylist = (List) bs.executeQuery(val.toString(), new MapListProcessor());
				if(Bodylist!=null&&Bodylist.size()!=0){
					custMap = (Map) Bodylist.get(0);
				resval =custMap.get("typeno").toString();
				
				return resval;
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		return res;
		 
	}
	/** 
	 * ��Ʊ����
	 * add by zsh 2019-10-16
	 * @param bm
	 * @returnBD
	 */
	
	@SuppressWarnings("unchecked")
	public String  FatherCorp(String res,String resval){
		 IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 String val ="select FATHERCORP from bd_corp  where pk_corp ='"+res+"'";
		 String vals = "select cl.invclasscode from bd_invcl cl inner join bd_invbasdoc doc on cl.pk_invcl  =doc.pk_invcl    where doc.invname ='"+resval+"'";
		try {
			List list = (List) bs.executeQuery(val.toString(), new MapListProcessor());
			@SuppressWarnings("unused")
			String fathercorp =	((Map)list.get(0)).get("FATHERCORP").toString();
			List lists = (List)bs.executeQuery(vals.toString(), new MapListProcessor());
			String invCode = ((Map)lists.get(0)).get("invclasscode").toString();
			if("21".equals(invCode.substring(0, 2))||"22".equals(invCode.substring(0, 2))){
				return invCode.substring(0, 4);
			}else{
				return invCode;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return res;
		
		}

	//��ѯ����   add by gt 2019-10-24
	private  String  checking(String djPK,String corp){
		  int zt = 0;
		  String billno = "";
		  IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  String check="select zyx38 from ARAP_djzb where djbh='"+djPK+"' and dwbm='"+corp+"' and nvl(dr,0)=0";
		  try {
		   billno = (String) receiving.executeQuery(check, new ColumnProcessor());
		   if(billno !=null && "Y".equals(billno)){
			   return  billno;
		   }else{
			   return billno;
		   }
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return billno;  
	}

	//���뷽�� add by gt 2019-10-24
	@SuppressWarnings("unused")
	private void inserting(String djPK,String corp) {
		  IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		  String update="update ARAP_djzb set zyx38='Y' where djbh='"+djPK+"' and dwbm='"+corp+"' and nvl(dr,0)=0";
	    try {
	  	  ipubdmo.executeUpdate(update);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * ������ϸ
	 * add by zsh 2019-10-16
	 * @param bm
	 * @returnBD
	 */
	
	@SuppressWarnings({ "unchecked", "null" })
	public String  invcls(String res){
			 String resval = "";
			  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			String value = "select cl.invclasscode from bd_invcl cl inner join bd_invbasdoc doc on cl.pk_invcl  =doc.pk_invcl    where doc.invcode ='"+res+"'"; 
			List Bodylist = new ArrayList();
			 Map custMap = new HashMap();
			try {
				Bodylist = (List) bs.executeQuery(value.toString(), new MapListProcessor());
				if(Bodylist!=null||Bodylist.size()!=0){
					custMap = (Map) Bodylist.get(0);
				String val =custMap.get("invclasscode").toString();
				return val.trim();
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return resval;
	}
	/** 
	 * ˰��
	 * add by zsh 2019-10-16
	 * @param bm
	 * @returnBD
	 */
	@SuppressWarnings({ "unchecked", "null" })
	public String  Sl (String res){
			 String resval = "";
			  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			String value = "select fb.sl from arap_djfb fb inner join arap_djzb zb on zb.vouchid = fb.vouchid  where zb.DJBH ='"+res+"'";
	
			List Bodylist = new ArrayList();
			 Map custMap = new HashMap();
			try {
				Bodylist = (List) bs.executeQuery(value.toString(), new MapListProcessor());
				if(Bodylist!=null||Bodylist.size()!=0){
					custMap = (Map) Bodylist.get(0);
				String val =custMap.get("sl").toString();
				return val.trim();
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return resval;
			 
		 }
	  

  
  
  /**
   * ��ȡ�б�ѡ������
   * ��Ѽ� 2018��5��11��17:08:12
   * @return
   */
	  @SuppressWarnings("restriction")
	public List<DJZBVO> getSelectedRowVO()
	  {
		  IUiPanel iPanel = getCurrentPanel();
		  DjPanel djPanel = null;
		    if ((iPanel instanceof DjPanel))
		    {
		      djPanel = (DjPanel)iPanel;
		    }
		    List<DJZBVO> vos = new ArrayList<DJZBVO>();
		    DJZBVO vo = new DJZBVO();
		    for (int row = 0; row < djPanel.getBillListPanel1().getHeadBillModel().getRowCount(); row++) {
		      if (djPanel.getBillListPanel1().getHeadBillModel().getValueAt(row, "isselected") == null) {
		        continue;
		      }
		      if (((Boolean)djPanel.getBillListPanel1().getHeadBillModel().getValueAt(row, "isselected")).booleanValue()) {
		         String strHeadPK = djPanel.getBillListPanel1().getHeadBillModel().getValueAt(row, "vouchid").toString();
		         vo = djPanel.getDjDataBuffer().getDJZBVO(strHeadPK);
		         vos.add(vo);
		      }
	      }
	    return vos;
	  }
  
	  /**
	   * �õ����̱���
	   * wy
	   * 2019/10/11
	   * @param vo
	   * @return
	   */
	  @SuppressWarnings("unchecked")
	 public  Map custcode(DJZBHeaderVO hvo){
		  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			//����
		  	String pk = hvo.getVouchid();
		  	StringBuffer sb = new StringBuffer();
			sb.append(" select * from arap_djfb zb where zb.vouchid ='"+pk+"' and nvl(zb.dr,0)=0");
			List Bodylist = new ArrayList();
			Map BodyMap = new HashMap();
			try {
				Bodylist = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
				for(int a =0;a<Bodylist.size();a++){
					BodyMap = (Map) Bodylist.get(0);
				}
			} catch (BusinessException e2) {
				e2.printStackTrace();
			}
			//��˾����
			String corp = hvo.getDwbm();
			//���̴���
			String code = (String) BodyMap.get("ksbm_cl");
			Map custcode = this.QueryCustCoded(code,corp);	  
		return custcode;
	  }
	   /**
		 * ��ѯ�ͻ����롢�ͻ�˰��
		 * wy
		 * 2019/10/29
		 * @param pk_cust
		 * @return 
		 */
	    @SuppressWarnings("all")
		public Map QueryCustCoded(String pk_cust,String pk_corp){
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			StringBuffer sb = new StringBuffer();
			String custcode = null;
			sb.append(" select bas.custcode,bas.taxpayerid") 
			.append("   from bd_cumandoc man  ") 
			.append("   left join bd_cubasdoc bas ") 
			.append("     on man.pk_cubasdoc = bas.pk_cubasdoc ") 
			.append("   where man.pk_cumandoc ='"+pk_cust+"' and  man.pk_corp = '"+pk_corp+"' and nvl(man.dr,0)=0 "); 
			Map map = new HashMap();
			try {
				List list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
				map=(Map) list.get(0);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return map;
		}
 
  
  /**
   * Ӧ�յ���cvm
   * @param vo
   * @return
   */
  @SuppressWarnings("all")
  private String saleSendcvm(DJZBVO vo){
	  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	  DJZBHeaderVO hvo = (DJZBHeaderVO) vo.getParentVO();
		//����
	  	String pk = hvo.getVouchid();
	  	StringBuffer sb = new StringBuffer();
		sb.append(" select * from arap_djfb zb where zb.vouchid ='"+pk+"' and nvl(zb.dr,0)=0");
		List Bodylist = new ArrayList();
		Map BodyMap = new HashMap();
		try {
			Bodylist = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			for(int a =0;a<Bodylist.size();a++){
				BodyMap = (Map) Bodylist.get(0);
			}
		} catch (BusinessException e2) {
			e2.printStackTrace();
		}
		
		//��Ʊ����
		String vreceiptcode =BodyMap.get("fph")==null?"":BodyMap.get("fph").toString();
		
		//Ӧ�յ���
		String yscode = hvo.getDjbh();
		
		//ֽ�ʷ�Ʊ��
		String invoice = hvo.getZyx17()==null?"":hvo.getZyx17().toString();
		
		//��˾����
		String corp = hvo.getDwbm();
		String unitcode = this.QueryCorp(corp);
		
		//���̴���
		String code = (String) BodyMap.get("ksbm_cl");
		String custcode = this.QueryCustCode(code,corp);
				
		
		//��������
		String dbilldate = String.valueOf(hvo.getDjrq());
		UFDouble fpje = new UFDouble(0);
		Map cmap = new HashMap();
		for(int j = 0;j<Bodylist.size();j++){
			cmap = (Map) Bodylist.get(j);
			Object numsummny = cmap.get("jfbbje");
			fpje = fpje.add(new UFDouble(numsummny.toString()));
		}
		Map jsonMap = new HashMap();
		//add by zwx 2019-11-18 cvm���Ӵ����ֶ�
		HashMap mapFp = getFpInfo(hvo.getVouchid());
		if(mapFp==null){
			jsonMap.put("invoicecode", "");// ֽ�ʷ�Ʊ����
			jsonMap.put("costCenter", "");// ��������
		}else{
			jsonMap.put("invoicecode", mapFp.get("invoicecode")==null?"": mapFp.get("invoicecode").toString().trim());// ֽ�ʷ�Ʊ����
			jsonMap.put("costCenter", mapFp.get("costcenter")==null?"": mapFp.get("costcenter").toString().trim());// ��������
		}
		//end by zwx
		jsonMap.put("username", "baosteel");//�Խ��û���
		jsonMap.put("password", "123456");//�Խ�����
		jsonMap.put("vreceiptcode", vreceiptcode);// ��Ʊ����
		jsonMap.put("yscode", yscode);//Ӧ�յ���
		jsonMap.put("invoice", invoice);// ֽ�ʷ�Ʊ��
		jsonMap.put("vendedorcode", custcode);// ���̴���
		jsonMap.put("unitcode", unitcode);// ��˾����
		jsonMap.put("dbilldate",dbilldate);// ��Ʊ����
		jsonMap.put("kpje", String.valueOf(fpje));//��Ʊ�ܽ��
		List jsonList = new ArrayList();
		Map bmap = new HashMap();
		for(int i = 0;i<Bodylist.size();i++){
			bmap = (Map) Bodylist.get(i);
			//���������Ŀ����	
			String inv =bmap.get("cinventoryid")==null?"":bmap.get("cinventoryid").toString();
			Map map = this.QueryInvl(inv,corp);
			
			//����
			String nnumber = String.valueOf(bmap.get("jfshl"));
			
			//��˰�ϼ�
			String noriginalcursummny = String.valueOf(bmap.get("jfbbje"));
			
			Map jsonBody = new  HashMap();
			jsonBody.put("invcode", map.get("invcode")==null?"":map.get("invcode").toString());// �������
			jsonBody.put("mainmeasrate", map.get("invclasscode")==null?"":map.get("invclasscode").toString());// �������
			jsonBody.put("nnumber", nnumber);//����
			jsonBody.put("noriginalcursummny", noriginalcursummny);// ��˰�ϼ�
			jsonList.add(jsonBody);
		}
		jsonMap.put("bodylist", jsonList);
		Gson gson = new Gson();
		Object ss = gson.toJson(jsonMap);
		StringBuffer jsonsb = new StringBuffer();
		jsonsb.append("[").append(ss).append("]");
		URL url;
		String reustle = null;
		String hid=null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String startTime=null;
		String endTime=null;
		try {
//			hid=recordLogs(vo);
			startTime=df.format(new Date());//��ȡ���ýӿ�ǰ��ϵͳʱ��
//			url = new URL("http://10.70.26.23/cvm/CVMService/NC002");//��ʽ
			url = new URL("http://10.70.76.9:90/cvm/CVMService/NC002");//���Ի���
			reustle  = this.httpconnect(url, jsonsb);
		} catch (MalformedURLException e) {
			endTime=df.format(new Date());
			e.printStackTrace();
			updateLogs(hid, false, e.getMessage(),startTime,endTime);
			return "[{\"status\":\"error\",\"message\":\"����CVM�ӿ��쳣(URLЭ��)'"+e.getMessage()+"'\"}]";
		}catch(Exception e){
			endTime=df.format(new Date());
			e.printStackTrace();
			updateLogs(hid, false,e.getMessage(),startTime,endTime);
			return "[{\"status\":\"error\",\"message\":\"����CVM�ӿ��쳣(URLЭ��)'"+e.getMessage()+"'\"}]";
		}	
		// ����������쳣�����
		endTime=df.format(new Date());
		updateLogs(hid,true,reustle,startTime,endTime);
		return reustle;
	}
  
  
  /**
	 * ��ѯ�ͻ�����
	 * @param pk_cust
	 * @return
	 */
    @SuppressWarnings("all")
	public String QueryCustCode(String pk_cust,String pk_corp){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer sb = new StringBuffer();
		String custcode = null;
		sb.append(" select bas.custcode ") 
		.append("   from bd_cumandoc man ") 
		.append("   left join bd_cubasdoc bas ") 
		.append("     on man.pk_cubasdoc = bas.pk_cubasdoc ") 
		.append("   where man.pk_cumandoc ='"+pk_cust+"' and  man.pk_corp = '"+pk_corp+"' and nvl(man.dr,0)=0 "); 
		try {
			List list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			Map<String,String> map = new HashMap<String, String>();
			map=(Map<String, String>) list.get(0);
		    custcode = map.get("custcode");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return custcode;
	}
	
	/**
	 * ��ѯ��˾����
	 * @param corp
	 * @return
	 */
    @SuppressWarnings("all")
	public String QueryCorp(String corp){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		StringBuffer sb = new StringBuffer();
		String unitcode = null;
		sb.append(" select unitcode from bd_corp where pk_corp = '"+corp+"' and nvl(dr,0)=0 "); 
		try {
			List list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			Map<String,String> map = new HashMap<String, String>();
			map=(Map<String, String>) list.get(0);
			unitcode = map.get("unitcode");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return unitcode;
	}
	
	/**
	 * ��ѯ�������ʹ������
	 * @param invcode
	 * @return
	 */
    @SuppressWarnings("all")
	public Map QueryInvl(String invcode,String corp){
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Map<String,String> map = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select bas.invcode,invcl.invclasscode ") 
		.append("   from bd_invmandoc man ") 
		.append("   left join bd_invbasdoc bas ") 
		.append("     on man.pk_invbasdoc = bas.pk_invbasdoc ") 
		.append("   left join bd_invcl invcl ") 
		.append("    on bas.pk_invcl = invcl.pk_invcl ") 
		.append("     where bas.pk_invbasdoc ='"+invcode+"' and nvl(man.dr,0)=0 and man.pk_corp = '"+corp+"'; "); 
		try {
			List list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			if(list.size()>0){
				map=(Map<String, String>) list.get(0);
			}else{
				Map map1 = new HashMap();
				map1.put("invcode", "");
				map1.put("invclasscode", "");
				return map1;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return map;
	}
  
  /**
   * �տ����CVM(�ⲿ�ӿ�)
   * @param vo
   * @return
   */
    @SuppressWarnings("all")
  public String sendGather(DJZBVO vo){
		String reustle = null;
		//ǰ̨���ݲ�ѯ�������õ�����һ���ӿ�
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//�տ����VO
		DJZBHeaderVO hvo = (DJZBHeaderVO) vo.getParentVO();
		//�����տ����VO��õ�������
		String pk = hvo.getVouchid();
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from arap_djfb zb where zb.vouchid ='"+pk+"' and nvl(zb.dr,0)=0");
		List Bodylist = new ArrayList();
		Map BodyMap = new HashMap();
		try {
			Bodylist = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
			for(int a =0;a<Bodylist.size();a++){
				BodyMap = (Map) Bodylist.get(a);
			}
		} catch (BusinessException e2) {
			e2.printStackTrace();
		}
		//������������
		String item_bill_pk = hvo.getItem_bill_pk()==null?"":hvo.getItem_bill_pk();
		//Ԥ�ո���־
		String prepay = hvo.getPrepay().toString();
		String flag = null;
		if(prepay.equals("Y")){
			flag = "Ԥ��";
		}else if(prepay.equals("N")){
			flag = "Ӧ��";
		}
		//���ݺ�
		String djbh = hvo.getDjbh();
		//��������
		String	djrq = hvo.getDjrq().toString();
		//ԭ�ҽ��
		String ybje = String.valueOf(hvo.getYbje());
		//���ҽ��
		String bbje = String.valueOf(hvo.getBbje());
	    //���һ���
		String bbhl= String.valueOf(BodyMap.get("bbhl"));
		//��˾
		String unitcode = hvo.getDwbm();
		//����
		String custcode = (String) BodyMap.get("ksbm_cl");
		String custsql = "select cubasdoc.custcode from bd_cumandoc cumandoc " +
				"left join bd_cubasdoc cubasdoc on cumandoc.pk_cubasdoc = cubasdoc.pk_cubasdoc " +
				"where cumandoc.pk_cumandoc = '"+custcode+"' and cumandoc.pk_corp ='"+unitcode+"' and nvl(cumandoc.dr,0)=0";
		Map custMap = new HashMap();
		try {
			List list = (List) bs.executeQuery(custsql, new MapListProcessor());
			if(list.size()>0){
				custMap = (Map) list.get(0);	
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			return e.getMessage().toString();
		}
		//����
		String pk_currtype = (String) BodyMap.get("bzbm");
		//��������
		String qxrq =(String) BodyMap.get("qxrq");
		//��ѯ��˾����
		String corpsql = "select unitcode from bd_corp where pk_corp = '"+unitcode+"'";
		//��ѯ��������
		String sql="select currtypename from bd_currtype where pk_currtype ='"+pk_currtype+"'";
		Map map = null;
		Map map1= null;
		try {
			List list = (List) bs.executeQuery(sql, new MapListProcessor());
			if(list.size()>0){
			    map = (Map) list.get(0);	
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		try {
			List list = (List) bs.executeQuery(corpsql, new MapListProcessor());
			if(list.size()>0){
			    map1 = (Map) list.get(0);	
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			return e.getMessage().toString();
		}
		Map jsonObject = new HashMap();
		jsonObject.put("username","baosteel");//�Խ��û���
		jsonObject.put("password", "123456");//�Խ�����
		jsonObject.put("unitcode", map1.get("unitcode"));//��˾
		jsonObject.put("custcode", custMap.get("custcode")==null?"":custMap.get("custcode").toString());//����
		jsonObject.put("item_bill_pk", item_bill_pk);//������������
		jsonObject.put("prepay", flag);//Ԥ�ո���־
		jsonObject.put("djbh", djbh);//���ݱ��
		jsonObject.put("djrq", djrq);//��������
		jsonObject.put("ybje", ybje);//ԭ�ҽ��
		jsonObject.put("bbje", bbje);//���ҽ��
		jsonObject.put("bbhl", bbhl);//���һ���
		jsonObject.put("bz", map.get("currtypename"));//����
		jsonObject.put("dzrq", qxrq);//��������
		Gson gson = new Gson();
		Object ss = gson.toJson(jsonObject);
		StringBuffer jsonsb = new StringBuffer();
		jsonsb.append("[").append(ss).append("]");
		URL url;
		String hid = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String startTime=null;
		String endTime=null;
		try {
//			hid = recordLogs(vo);
			//����CVM HTTP�ӿ�
//			url = new URL("http://10.70.26.23/cvm/CVMService/NC001");//��ʽ
			url = new URL("http://10.70.76.9:90/cvm/CVMService/NC001");//���Ի���
			startTime=df.format(new Date());//��ȡ���ýӿ�ǰ��ϵͳʱ��
			//��ȡ���ز���
			reustle  = this.httpconnect(url, jsonsb);
		} catch (MalformedURLException e) {
			endTime=df.format(new Date());
			e.printStackTrace();
			updateLogs(hid,false,e.getMessage(),startTime,endTime);
			return "[{\"status\":\"error\",\"message\":\"����CVM�ӿ��쳣(URLЭ��)'"+e.getMessage()+"'\"}]";
		}catch(Exception e1){
			endTime=df.format(new Date());
			e1.printStackTrace();
			updateLogs(hid,false,e1.getMessage(),startTime,endTime);
			return "[{\"status\":\"error\",\"message\":\"����CVM�ӿ��쳣(URLЭ��)'"+e1.getMessage()+"'\"}]";
		}
		updateLogs(hid,true,reustle,startTime,endTime);
		// ����������쳣�����ӡ�����óɹ����ϵͳʱ��
		return reustle;
	}
    
    
    
  /**
   * �������ݿ��е�lContext�ֶ�  
   * @param hid
   * @param b
   * @param message
 * @param endTime 
 * @param startTime 
 * @throws BusinessException 
   */
  private void updateLogs(String hid, boolean b, String message, String startTime, String endTime) {
	  IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
	  if(hid!=null){
		try {
			StringBuffer sqlupd = new StringBuffer();
			sqlupd.append("update itfLog set LContext ='" + message
					+ "',start_time='" + startTime + "',end_time='" + endTime + "'"
					+ "where pk_log= '" + hid + "'");
			ipubdmo.executeUpdate(sqlupd.toString());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	  }
  }

/**
   * ����cvm�ӿ�
   * @param url
   * @param json
   * @return
   * @throws Exception
   */
	@SuppressWarnings("unchecked")
	private String httpconnect(URL url,Object json) throws Exception {
			String reustle =null;
	        //����HTTP����
	        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	    
	        //��������ķ�������
	        httpURLConnection.setRequestMethod("POST");
	        
	        //�����������������
	        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
	        
	        //���÷�������
	        httpURLConnection.setDoOutput(true);
	        //���ý�������
	        httpURLConnection.setDoInput(true);
	        
	        //��������,ʹ�������
	        OutputStream outputStream = httpURLConnection.getOutputStream();
	        //���͵�soapЭ�������
	        String content = "jsonData="+URLEncoder.encode(json.toString(), "UTF-8");
	        //��������
	        outputStream.write(content.getBytes());
	    
	        //��������
	        InputStream inputStream = httpURLConnection.getInputStream();
	        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
	        StringBuffer buffer = new StringBuffer();  
	        String line = "";  
	        while ((line = in.readLine()) != null){  
	          buffer.append(line);  
	        }  
	        
	        reustle = buffer.toString();
	        in.close();
	        //����CVM����JSON����
	        Gson gson = new Gson();
	        Map<String, Object> map = new HashMap<String, Object>();
	        map = gson.fromJson(reustle, map.getClass());
	        String status = (String) map.get("status")==null?"":(String) map.get("status");
	        String message = (String) map.get("message")==null?"":(String) map.get("message");
	        if(status.equals("error")){
	        	return message;
	        }else{
	        	return message;
	        }
	        
	  }

//	/**
//	 * ��¼��־
//	 * @param vo
//	 */
//	public String recordLogs(DJZBVO vo){
//		String hid = null;
//			//---------------------------------------------------------
//			LogVO logVO = new LogVO();
//		    Integer dr=(Integer) vo.getParentVO().getAttributeValue("dr");//��ȡɾ����ʶ
//		    String shr=(String) vo.getParentVO().getAttributeValue("shr");//��ȡ���������
//			String  headBillID=(String)vo.getParentVO().getAttributeValue("vouchid");//��õ�ǰ��˵ĵ��ݵ�����id��
//			String dwbm=(String)vo.getParentVO().getAttributeValue("dwbm");//��ȡ����ǰ����˵��ݵĵ�λ����
//			String djlx=(String)vo.getParentVO().getAttributeValue("djdl");//��ȡ��˵��ݵĵ�������
//			String djbx=(String)vo.getParentVO().getAttributeValue("djbh");//��ȡ�������
//			logVO.setDr(dr);
//			logVO.setDjlx(djlx);//�ŵ�������
//			logVO.setDjbx(djbx);//�������
//			logVO.setLUser(shr);//�����
//			logVO.setPk_corp(dwbm);//��λ����
//			logVO.setHeadBill_id(headBillID);//��ѯ�����ĵ��ݱ�ͷ������
//			logVO.setTs(new UFDateTime());
//			logVO.setLContext("��������˶���");//����˺����־��¼�����ݿ����־�����ֶ���
//			//logVO.setBodyBill_id(bodyBillID);
//			IVOPersistence ivo = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
//			//��LogVO���뵽���ݿ�
//  			try {
//  				hid = ivo.insertVO(logVO);
//			} catch (BusinessException e) {
//				e.printStackTrace();
//			}
//			//---------------------------------------------------------
//			return hid;
//	}

	/**
	 * �ж�������Ǳ��һ������
	 * wy
	 * 2019��9��10�� 
	 * @param bzbm
	 * @return
	 */
	public String  judgeCurrency(String bzbm){
		if (bzbm == null) {
			return "";
		}
		IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		//��ѯ��������
		String sql="select currtypecode from bd_currtype where pk_currtype ='"+bzbm+"' and nvl(dr,0)=0";
		String string = "";
		try {
			Object list = iquery .executeQuery(sql.toString(),new ColumnProcessor());
			string = list.toString();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return string;
	}
	/**
	 * �жϵ���������������������	 
	 * wy
	 * 2019��9��10�� 
	 * @return
	 */
	public String DJLX(String nx){
		if(nx == null){
			return "";
		}
		IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		String sql = "select typeno from MDMBillType where instructions = '"+nx+"' and nvl(dr,0)=0";
		String string = "";
		try {
			Object list = iquery .executeQuery(sql.toString(),new ColumnProcessor());
			string = list.toString();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	   * ���ݲ���id��ѯMDM��������
	   * wy
	   * 2019��9��10�� 
	   * @param bm
	   * @return
	   */
	  private String queryzrzx(String bm){
		  if(bm == null){
			  return "";
		  }
	      String deptcode = "";
	      String sql = "select deptcode from bd_deptdoc where pk_deptdoc='"+bm+"' and nvl(dr,0)=0";
	      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	      try {
	       deptcode = (String) receiving.executeQuery(sql, new ColumnProcessor());
	       System.out.println(deptcode.length());
	      } catch (BusinessException e) {
	    	  e.printStackTrace();
	      }
	   return deptcode;
	  }
	
	  
	  /**
	   * ���� ���ݱ�� �����˰�š����������˺š������������ơ���Ʊ���롢��ע���Ƶ����ڡ����ݻ����ȡ����ݻ���ڼ�
	   *  wy
	   * 2019��9��10�� 
	   * @return
	   */
	  @SuppressWarnings("unchecked")
	public Map selectCubasdoc(String djbh){
		  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  Map<String,String> map = new HashMap<String, String>();
		  StringBuffer sb = new StringBuffer();
		  sb.append("select cu.accname,cu.account,so.ccreditnum,so.vnote,so.dmakedate,zb.djkjnd,zb.djkjqj from arap_djzb zb");
		  sb.append(" left join arap_djfb fb on fb.vouchid = zb.vouchid");
		  sb.append(" left join so_saleinvoice so on so.csaleid = fb.ddlx");
		  sb.append(" left join bd_custbank cu on cu.pk_custbank = so.ccustbankid");
		  sb.append(" where zb.djbh = '"+djbh+"' and nvl(zb.dr,0)=0 and nvl(cu.dr,0)=0 and nvl(so.dr,0)=0 and nvl(fb.dr,0)=0");		
		  try {
				List list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
				if(list.size()>0){
					map=(Map<String, String>) list.get(0);
				}else{
					Map map1 = new HashMap();
					map1.put("accname", "");
					map1.put("account", "");
					map1.put("ccreditnum", "");
					map1.put("djkjnd", "");
					map1.put("djkjqj", "");
					return map1;
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return map;
	  }
	  /**
		 * ���� ��˾ �� 
		 * ��Ʊ��λ����unitname,��Ʊ��λ˰��taxcode������˰�˵ǼǺ�,��˾�� == ����
		 * wy
	     * 2019��9��10��
		 */
		@SuppressWarnings("unchecked")
		public Map kpTaxCode(String dwbm){
			IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			String sql = "select b.taxcode,b.unitname, b.def8 from bd_corp b where b.pk_corp = '"+dwbm+"' and nvl(dr,0)=0";
			List Bodylist = new ArrayList();
			Map BodyMap = new HashMap();
			try {
				Bodylist = (List) iquery.executeQuery(sql.toString(), new MapListProcessor());
				for(int a =0;a<Bodylist.size();a++){
					BodyMap = (Map) Bodylist.get(0);
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return BodyMap;
		}
		
		/**
		 * ��cinventoryid == pk_invbasdoc  1020A21000000000K87A
		 * wy
		 * 2019��10��20�� 
		 * @param hvo
		 * @return
		 */

		@SuppressWarnings("unchecked")
		private String selectInvl(DJZBHeaderVO hvo){
			  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				//����
			  	String pk = hvo.getVouchid();
			  	StringBuffer sb = new StringBuffer();
				sb.append(" select * from arap_djfb zb where zb.vouchid ='"+pk+"' and nvl(zb.dr,0)=0");
				List Bodylist = new ArrayList();
				Map BodyMap = new HashMap();
				try {
					Bodylist = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
					for(int a =0;a<Bodylist.size();a++){
						BodyMap = (Map) Bodylist.get(0);
					}
				} catch (BusinessException e2) {
					e2.printStackTrace();
				}
				String inv =BodyMap.get("cinventoryid")==null?"":BodyMap.get("cinventoryid").toString();					
				return inv;
		}
		/**
		 * ���ݴ������������
		 * ���������롢�������ơ���������λid�����ر��루memo��
		 * wy
		 * 2019��10��20�� 
		 * @param invcode
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public Map QueryInv(String invcode){
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			Map<String,String> map = new HashMap<String, String>();
			StringBuffer sb = new StringBuffer();
			sb.append(" select inv.invclasscode,inv.invclassname,bas.pk_measdoc,inv.memo") 
			.append("   from bd_invbasdoc bas ") 
			.append("   left join bd_invcl inv ") 
			.append("   on bas.pk_invcl = inv.pk_invcl ") 
			.append("   where bas.pk_invbasdoc = '"+invcode+"' ") ; 
			try {
				List list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
				if(list.size()>0){
					map=(Map<String, String>) list.get(0);
				}else{
					Map map1 = new HashMap();
					map1.put("invclassname", "");
					map1.put("invclasscode", "");
					map1.put("pk_measdoc", "");
					return map1;
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return map;
		}
		/**
		 * ����������id����������λ
		 * wy
		 * 2019��10��20�� 
		 * select * from bd_measdoc where pk_measdoc = '0001A11000000000BAPJ'
		 * @return
		 */
		public String queryZjldw(String zjldw){
			  if(zjldw == null){
				  return "";
			  }
		      String zjldw1 = "";
		      String sql = "select measname from bd_measdoc where pk_measdoc = '"+zjldw+"' and nvl(dr,0)=0";
		      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		      try {
		    	  zjldw1 = (String) receiving.executeQuery(sql, new ColumnProcessor());
		      } catch (BusinessException e) {
		    	  e.printStackTrace();
		      }
		   return zjldw1;
		}
		/**
		 * ��ѯ¼����
		 * wy
		 * 2019��10��20�� 
		 * @param lrr
		 * @return
		 */
		public String queryLRR(String lrr){
			  if(lrr == null){
				  return "";
			  }
		      String lrr1 = "";
		      String sql = "select user_name from sm_user where cuserid  ='"+lrr+"' and nvl(dr,0)=0";
		      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		      try {
		    	  lrr1 = (String) receiving.executeQuery(sql, new ColumnProcessor());
		      } catch (BusinessException e) {
		    	  e.printStackTrace();
		      }
		   return lrr1;
		}
		
		/**
		 * ���ͬ���ͱ���ͺ�ͬ��������,��ͬ���
		 * wy
		 * 2019��10��20�� 
		 * @param pk_invbasdoc
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public Map queryHT(String pk_invbasdoc){
			IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			Map<String,String> map = new HashMap<String, String>();
			StringBuffer sb = new StringBuffer();
			sb.append(" select distinct type.typecode,type.typename,m.ct_code") 
			.append("   from ct_type type left join ct_manage m on type.pk_ct_type = m.pk_ct_type ") 
			.append("   left join ct_manage_b mb on m.pk_ct_manage = mb.pk_ct_manage ") 
			.append("  left join bd_invmandoc d on mb.invid = d.pk_invmandoc ") 
			.append("   left join bd_invbasdoc inv on d.pk_invbasdoc = inv.pk_invbasdoc ")
			.append("  where inv.pk_invbasdoc ='"+pk_invbasdoc+"' ") ; 
			
			try {
				List list = (List) bs.executeQuery(sb.toString(), new MapListProcessor());
				if(list.size()>0){
					map=(Map<String, String>) list.get(0);
				}else{
					Map map1 = new HashMap();
					map1.put("typecode", "");
					map1.put("typename", "");
					return map1;
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return map;
		}
		
		/**
		 * ��ѯ��Ʊ����
		 * wy
		 * 2019��10��20�� 
		 * @return
		 */
		public String fplx(String fplx1){			 
		      String fplx = "";
		      String sql = "select typeno  from MDMInvoiceType where pk_invoice = '"+fplx1+"' and nvl(dr,0)=0";
		      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		      try {
		    	  fplx = (String) receiving.executeQuery(sql, new ColumnProcessor());
		      } catch (BusinessException e) {
		    	  e.printStackTrace();
		      }
		   return fplx;
		}
		
		/**
		 * ����ҵ�����ͱ��� ��  ҵ����������  
		 * if ���� = ���ӹ���Ʒ����ҵ������ dx 
		 * else zx
		 * wy
		 * 2019��10��20�� 
		 */
		public String ywlx(String ywlxbm1){
			if (ywlxbm1 == null) {
				return "";
			}
			String ywlxbm = "";
			StringBuffer sb = new StringBuffer();
			sb.append(" select distinct busi.businame from bd_busitype busi");
			sb.append(" inner join so_saleinvoice inv on busi.pk_busitype = inv.cbiztype ");
			sb.append(" where nvl(busi.dr,0) = 0 and nvl(inv.dr,0) = 0 and pk_busitype = '"+ywlxbm1+"'");
		    IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		    try {
		    	ywlxbm = (String) receiving.executeQuery(sb.toString(), new ColumnProcessor());
		    } catch (BusinessException e) {
		    	e.printStackTrace();
		    }
			return ywlxbm;
		}		
		
	
		/**
		 * �ж� ARAP_djzb �� zyx38 �Ƿ�ΪY,���ΪY,�����ٴ��ڶ���,���Ϊ�պ�N�򻹿����ٴ�
		 * wy
		 * 2019��10��20�� 
		 */
		public String judgeDJBH(String dbh){
		    IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			String sql = "select zyx38  from ARAP_djzb where djbh = '"+dbh+"' and nvl(dr,0)=0";
			String djbh = "";
			try {
				djbh = (String) receiving.executeQuery(sql, new ColumnProcessor());		
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			return djbh;
		}	
		/**
		 *  wy
		 * 2019��10��20�� 
		 * @param djbh
		 */
		public void updateDJBH(String djbh,String ts){			
			IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());			 
		    String sql = "update ARAP_djzb set zyx38 = 'Y',zyx36 = '�ϴ���',zyx48='"+ts+"' where djbh = '"+djbh+"' and nvl(dr,0)=0";
		    try {
				ipubdmo.executeUpdate(sql);
				System.out.println(djbh+" zyx38�޸ĳɹ�");
			} catch (BusinessException e) {
				e.printStackTrace();
				System.out.println(djbh+" zyx38�޸�ʧ��");
			}		
		}
		/**
		 * zy
		 * 2019/10/30
		 * @param djbh
		 */
		public void updateHC(String djbh){			
			IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());			 
		    String sql = "update ARAP_djzb set zyx38 = '' where djbh = '"+djbh+"' and nvl(dr,0)=0";
		    try {
				ipubdmo.executeUpdate(sql);
				System.out.println(djbh+" zyx38�޸ĳɹ�");
			} catch (BusinessException e) {
				e.printStackTrace();
				System.out.println(djbh+" zyx38�޸�ʧ��");
			}		
		}
		
		/**
		   * ����Ӧ�����ݺŲ�ѯ��Ʊ�š�ֽ�ʷ�Ʊ�š���Ʊ����
		   * add by zy 2019-10-18
		   * @param 
		   * @return
		   */
		  private Map queryFph(String djh){
			  List list = null;
			  Map<String,String> map=new HashMap<String,String>();
		      String sql = "select s.vreceiptcode,s.vdef17,s.ccreditnum from arap_djzb z " +
		      		"inner join arap_djfb f on z.vouchid=f.vouchid " +
		      		"inner join so_saleinvoice s on f.ddlx=s.csaleid " +
		      		"where z.djbh='"+djh+"'";
		      IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		      try {
		    	  list = (List)receiving.executeQuery(sql, new MapListProcessor());
		      } catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		      }
		      if(list.size()==0){
		    	 return map;
		      }
		      map = (Map) list.get(0);
			  return map;
		  }
		  
		//���б�	ZSH	  
		  public void CxbusList(List<DJZBVO> vos,DjPanel djPanel){
			  Date date = new Date();
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  String nyr = sdf.format(date);	//��ȡ��ǰϵͳʱ��	
			  StringBuffer str = new StringBuffer();//��ȡ���ͳɹ�
			  StringBuffer str1 = new StringBuffer();//��ȡ�Ѿ����ͳɹ�			 
			  StringBuffer str3 = new StringBuffer();
			  ITFBZVO ItfbzVO = new ITFBZVO();
			    String kszh = new String();
				@SuppressWarnings("unused")
				DJZBVO vo = new DJZBVO();				
				vo = (DJZBVO)djPanel.getArapDjPanel1().getBillCardPanelDj().getBillData().getBillValueVO(DJZBVO.class.getName(), DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName());							   				
			     List<String> list  = new ArrayList<String>();			      
				for(int i=0;i<vos.size();i++){
				StringBuffer str2 = new StringBuffer();//��ȡ����ʧ��
				DJZBHeaderVO dJZBHeaderVOs = (DJZBHeaderVO)vos.get(i).getParentVO();//��ȡѡ�������ı�ͷ
				DJZBItemVO[] dJZBItemVOs = (DJZBItemVO[])vos.get(i).getChildrenVO();//��ȡѡ���еı���
				if(dJZBHeaderVOs.getDjbh()==null||dJZBHeaderVOs.getDjbh().equals("")||dJZBHeaderVOs.getDjbh().toString().length()==0){
//					str2.append("\n"+"���ݺ�δ��д");
					showWarningMessage("\n"+"���ݺ�δ��д");
					continue;
				}
					 //У����������Ƿ񴫹�
				 String billno=judgeDJBH(dJZBHeaderVOs.getDjbh().toString());
				 System.out.println("billno:"+billno);
				if(billno == null){		
				vo =vos.get(i);
				DJZBHeaderVO DJZBHeaderVO = (DJZBHeaderVO)vos.get(i).getParentVO();//��ȡѡ�������ı�ͷ
				DJZBItemVO[] DJZBItemVO = (DJZBItemVO[])vos.get(i).getChildrenVO();//��ȡѡ���еı���
				//У������
				StringBuffer resval2 =	CheckData( DJZBHeaderVO, DJZBItemVO);
				
				if(resval2!=null&&resval2.toString().length()>0){
					str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"���ݺ���"+resval2.toString());
//					showWarningMessage("��"+DJZBHeaderVO.getDjbh()+"���ݺ�����"+resval2.get(1).toString());
//					continue;
				}
				String bzCode = BzCode(DJZBHeaderVO.getDwbm());	
				List listress =SelectBank(DJZBItemVO[0].getKsbm_cl(),DJZBHeaderVO.getDwbm());
				if(listress.get(0).equals("���˺�����")){
					  str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"���ݺ��¿����˺�����δ��д");
//					  showWarningMessage("�����˺�����δ��д");
//					  continue;
				}
				List listval2 =	queryks(DJZBHeaderVO.getVouchid(),DJZBHeaderVO.getDwbm());	
				ITFSNVO itfsnvo = new ITFSNVO();
				List revallist = reval(DJZBHeaderVO.getDjbh());
				if(revallist ==null ||revallist.size()==0){
				IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
				itfsnvo.setPK_CORP(DJZBHeaderVO.getDwbm());
				itfsnvo.setDR("0");
				itfsnvo.setBILLTYPE("YF");
				itfsnvo.setBILLNO(DJZBHeaderVO.getDjbh());			
				 try {
				iVOPersistence.insertVO(itfsnvo);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				List<String> listvall = CubasdocVal(listval2.get(0).toString());
				StringBuffer lists3 =ListCheck(listvall);//У����������Ƿ�Ϊ��
			
				if(lists3!=null&&lists3.toString().length()>0){
					str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+lists3.toString());
//					 showWarningMessage(lists3.get(1).toString());
//					 continue;
					}
				
//				List<String> lists =ListVal(DJZBHeaderVO.getDjbh());
				List<String> listsval =listVal(DJZBHeaderVO.getDjbh());
				if(listsval.get(0).equals("A")){
					str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"�����£����֣���ͬ�ţ���ͬ���ƣ���Ʊ�ţ���Ʊ����Ϊ�գ�����д");
				}else{
				StringBuffer listsval2 = checkListval(listsval);
				if(listsval2!=null&listsval2.toString().length()>0){
					str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"���ݺ���"+listsval2);
//					 showWarningMessage(lists3.get(1).toString());
//					 continue;
				}
				}
				JSONObject val = new JSONObject();
				String resval =	SERIALNUM(DJZBHeaderVO.getDjbh());
			    val.put("EXPANSE_REPORT_ID",resval);//��֧�嵥��
			    val.put("COMPANY_CODE",bzCode);//����
			    val.put("BILL_TYPE_CODE","P04001");//�������� DJZBHeaderVO.getYwbm()
			    System.out.println(DJZBHeaderVO.getVouchid());
			    val.put("OWER_SYS_ID","JC");//��Դϵͳ
			   if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
				val.put("INVOICE_NO","");//��Ʊ��
			    }else{
			    val.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//��Ʊ��
			    }
			   if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
				val.put("INVOICE_CODE","");//��Ʊ����
			   }else{
			    val.put("INVOICE_CODE",listsval.get(4));//��Ʊ����
			   }
			   String pflx =fplx(DJZBHeaderVO.getDjbh());
			    String Ftype = Fptype(pflx);
			    if(Ftype==null){
			    
			    	 val.put("INVOICE_TYPE","");//��Ʊ����
			    }else{
			    val.put("INVOICE_TYPE",Ftype);//��Ʊ����
			    }
			    val.put("INVOICE_DATE",DJZBHeaderVO.getDjrq().toString().replace("-", ""));//��Ʊ����
			   String sl = Sl(DJZBHeaderVO.getDjbh());
			    val.put("TAX_RATE",Double.valueOf(sl));//˰�� 1
			    UFDouble dJZBItemVO = new UFDouble();
			    UFDouble Dfybsj = new UFDouble();
			    for(int j=0;j<DJZBItemVO.length;j++){
			    	 dJZBItemVO = DJZBItemVO[j].getDfbbwsje().add(dJZBItemVO) ;
			    	 Dfybsj =DJZBItemVO[j].getDfybsj().add(Dfybsj);
			    }
			   UFDouble dfybje =  DJZBItemVO[0].getDfybje();
			   UFDouble dfybsj =  DJZBItemVO[0].getDfybsj();
			   UFDouble fpje = dfybsj.add(dfybje);
//			    val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())/100));//��Ʊ���
//			    val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())/100));//��Ʊ˰��
			    val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//��Ʊ���
			    val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//��Ʊ˰��
			    val.put("PREPAY_DATE","");//Ԥ�Ƹ�������
			    val.put("SETTLEMENT_TYPE","A10");//���㷽ʽ
			    val.put("COUNTERACT_AMOUNT",0.00);//����Ԥ������lists.get(2)
			    val.put("CAN_END_AMT_CUR",0.00);//��β����
			    val.put("PAYEE_CODE",listvall.get(0));//�ܿ��˴���  1
			    val.put("PAYEE_TYPE",listvall.get(1));//�ܿ�������
			    val.put("PAYEE_NAME",listvall.get(4));//�ܿ�������
			    if(listress.get(0).equals("���˺�����")||listress.get(1).toString().length()==0&&listress.get(1)==null){
			    val.put("PAYEE_ACCOUNT","");//�ܿ����ʺ�
			    }else{
			    val.put("PAYEE_ACCOUNT",listress.get(1));//�ܿ����ʺ�
			    }
			    val.put("PAYEE_ACCOUNT_ID","");//�ܿ����ʺ����
			    val.put("PAYEE_TAX_NO",listvall.get(5));//�ܿ���˰��
			    if(listress.get(0).equals("���˺�����")||listress.get(0).toString().length()==0&&listress.get(0)==null){
			    val.put("PAYEE_ACCOUNT_NAME","");//�ܿ��˿���������
			    }else{
			    val.put("PAYEE_ACCOUNT_NAME",listress.get(0));//�ܿ��˿���������
			    }
			    val.put("PAYEE_BANK_TYPE","");//�ܿ����������
			    val.put("PAYEE_BANK_CODE","");//�ܿ������е�����
			    val.put("COUNTRY","");//�ܿ���ʡ��/����
			    val.put("PAYEE_ADDRESS",listvall.get(2));//�ܿ����ܿ��˵�ַ
			    val.put("PAYEE_ZIP_CODE","");//�ܿ�����������
			    val.put("PAYEE_PHONE",listvall.get(3));//�ܿ�����ϵ�绰
			    val.put("PAYEE_EMAIL","");//�ܿ���E-mail
			    val.put("LC_NO","");//����֤��
			    val.put("DETAIL_NUM",DJZBItemVO.length);//��Ʊ��ϸ��¼�� 
			    val.put("TEMP1","");//�����ַ�1
			    val.put("TEMP2","");//�����ַ�2
			    val.put("TEMP3","");//�����ַ�3
			    val.put("TEMP4","");//�����ַ�4
			    val.put("TEMP5","");//�����ַ�5
			    JSONArray bvals = new JSONArray();
//			    int rows = 2; //���rows�ĳ�����������
			    
			    for (int j = 0; j < DJZBItemVO.length; j++) {	
			    	
			    	JSONObject val1 = new JSONObject();
			    	String resval1 =	SERIALNUM(DJZBHeaderVO.getDjbh());
			    	System.out.println("��֧��"+resval1);
			        val1.put("BILL_NO",resval1);//��֧�嵥��
			        val1.put("COMPANY_CODE",bzCode);//����}
			        val1.put("BILL_TYPE_CODE","P04001");//��������
			        val1.put("OWER_SYS_ID","JC");//��Դϵͳ
//			        val1.put("INVOICE_CODE",listsval.get(4));//��Ʊ����
			        if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
			        	val1.put("INVOICE_CODE","");//��Ʊ����
					   }else{
					    val1.put("INVOICE_CODE",listsval.get(4));//��Ʊ����
					   }
			        if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
			        	val1.put("INVOICE_NO","");//��Ʊ��
					    }else{
					    val1.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//��Ʊ��
					    }
//			        val1.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//��Ʊ��
			        val1.put("INVOICE_DETAIL_ID",j+1);//��Ʊ��ϸ�����		        
			        String zrzx = queryzrzx(DJZBItemVO[0].getDeptid());
			        if(zrzx.length()>8){
			        	if(j<1){
			        	str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"������,������ѡ�����µĲ���");
			        	}
			        	val1.put("COST_CENTER","");//��������			        	
			        }else{
			        val1.put("COST_CENTER",zrzx);//��������
			        }
			        val1.put("AGENTED_FLAG","");//������־
			        val1.put("AGENTED_CODE","");//������λ����
			        val1.put("AGENT_FLAG","");//������
			        val1.put("AGENT_CODE","");//�����û�����
			        val1.put("AGENT_TYPE","");//�����û����
			        val1.put("AGENT_ACCOUNT","");//�����û��˺�
			        val1.put("UNIT_PRICE",DJZBItemVO[j].getHsdj()==null?16:String.format("%.2f",Double.valueOf(DJZBItemVO[j].getHsdj().toString())));//����
			        val1.put("PRE_AMOUNT",0.00);//����Ԥ������lists.get(2)
			        val1.put("QANTITY",DJZBItemVO[j].getDfshl()==null?0.0:Double.valueOf(String.format("%.5f",Double.valueOf(DJZBItemVO[j].getDfshl().toString()))));//����			   
			        val1.put("AMOUNT",DJZBItemVO[j].getDfbbwsje());//���
			        val1.put("TAX_AMOUNT",DJZBItemVO[j].getDfybsj()==null?"":String.format("%.2f",Double.valueOf(DJZBItemVO[j].getDfybsj().toString())));//˰��			      
//			        val1.put("ORDER_NO",listsval.get(1));//��ͬ��2
			        if( listsval.get(0).equals("A")||listsval.get(1)==null&&listsval.get(1).length()==0){
			        	  val1.put("ORDER_NO","");//��ͬ��
					   }else{
						   String djlx =  djlx(DJZBHeaderVO.getDjbh());
						   if("���Ļ���".equals(djlx.trim())){
							   val1.put("ORDER_NO","");  
						   }else{
						   val1.put("ORDER_NO",listsval.get(1));//��ͬ��2
						   }
					   }			      
			        if( listsval.get(0).equals("A")||listsval.get(2)==null&&listsval.get(2).length()==0){
			        	  val1.put("ORDER_DESC","");//��ͬ����
					   }else{
						   String djlx =  djlx(DJZBHeaderVO.getDjbh());
						   if("���Ļ���".equals(djlx.trim())){
							   val1.put("ORDER_DESC","");   
						   }else{
						   val1.put("ORDER_DESC",listsval.get(2));//��ͬ����
						   }
					   }
			        val1.put("PROJECT_NO",DJZBItemVO[j].getXm()==null?"":DJZBItemVO[j].getXm());//��Ŀ��
			        val1.put("PRJECT_NAME",DJZBItemVO[j].getXm_name()==null?"":DJZBItemVO[j].getXm_name());//��Ŀ����
			        val1.put("BPO_PROPERTY","");//��ͬ���Դ���
			        val1.put("BPO_TYPE","");//�ɹ����ʹ���
			        Map map = this.QueryInvl(DJZBItemVO[j].getCinventoryid(),DJZBHeaderVO.getDwbm().trim());
			        String chbm = map.get("invcode").toString();
			        String invc =  invcl(chbm);
			        String invcs = invcls(chbm);
			        val1.put("FEE_TYPE",invc);//�������ʹ���  ��ȡ���ϱ����ǰ��λ
			        val1.put("FEE_DESC",invcs);//������ϸ1		       
			        val1.put("MR_TYPE_SUB1",invcl(chbm));//ҵ����������1
			        val1.put("MR_TYPE_SUB2","");//ҵ����������2
			        val1.put("PROJECT_SPEC1","");//��Ŀ����1
			        val1.put("PROJECT_SPEC2","");//��Ŀ����2
			        val1.put("BUSI_TYPE1","");//ҵ�����һ
			        val1.put("BUSI_TYPE2","");//ҵ������
			        val1.put("MAT_TYPE","");//�������
			        
			        val1.put("MAT_TYPE1",invc);//���ϴ���
			        val1.put("MAT_TYPE2","");//��������
			        val1.put("MAT_TYPE3","");//����ϸ��
			        
			        val1.put("MAT_CODE","");//���ϴ���
			        val1.put("TASK_CODE","");//��ҵ������
			        val1.put("REPORT_TYPE","");//��֧����
			        val1.put("REPORT_TYPE_D","");//��֧ϸ��
			        val1.put("TEMP1","");//�����ַ�1
			        val1.put("TEMP2","");//�����ַ�2
			        val1.put("TEMP3","");//�����ַ�3
			        val1.put("TEMP4","");//�����ַ�4
			        val1.put("TEMP5","");//�����ַ�5
			        val1.put("TEMP6","");//�����ַ�6
			        val1.put("TEMP7","");//�����ַ�7
			        val1.put("TEMP8","");//�����ַ�8
			        val1.put("TEMP9","");//�����ַ�9
			        val1.put("TEMP10","");//�����ַ�10
			        val1.put("TEMP11",0.00);//�����ַ�11
			        val1.put("TEMP12",0.00);//�����ַ�12
			        val1.put("TEMP13",0.00);//�����ַ�13
			        val1.put("TEMP14",0.00);//�����ַ�14
			        val1.put("TEMP15",0.00);//�����ַ�15
			        bvals.add(val1);
			    }
			  val.put("bodylist", bvals);
			  JSONArray jarry = new JSONArray();
			  JSONObject js = new JSONObject();
			  for(int j=0;j<DJZBItemVO.length;j++){
				  List listres = queryks(DJZBHeaderVO.getVouchid(),DJZBHeaderVO.getDwbm());
				  List<String> listval = CubasdocVal(listres.get(0).toString());
				  js.put("EXPANSE_REPORT_ID",resval);//��֧�嵥��
					js.put("BILL_TYPE_CODE","P04001");//��������
				    js.put("OWER_SYS_ID","JC");//��Դϵͳ
				    js.put("COMPANY_CODE",bzCode);//����
				    js.put("RECEIPT_NO","");//���ӵ���
				    js.put("RESP_UNIT_ID","");//���쵥λ
				    js.put("REPORT_DATE",DJZBHeaderVO.getDjrq()==null?"":DJZBHeaderVO.getDjrq().toString().replace("-", ""));//��֧����
				    js.put("BILL_DESC","��֧ժҪ");//��֧˵��			  				 
				    js.put("REPORT_UNIT_ID",bzCode);//��֧��λ��֯���� 1
				    js.put("REPORTOR_CODE","L00031");//�ύ�˹���1  
				    js.put("INPUT_VOUCHER_ID","");//����˹��� ��ȡ
				    js.put("INPUT_VOUCHER_TIME",DJZBHeaderVO.getShrq()==null?"":DJZBHeaderVO.getShrq().toString().replace("-", ""));//�������
				    js.put("SUPPLIER_CODE",listval.get(0));//��Ӧ�̴���  
				    js.put("SUPPLIER_TYPE",listval.get(1));//��Ӧ������
				    js.put("SUPPLIER_NAME",listval.get(4));//��Ӧ������
				    if(listress.get(0).equals("���˺�����")||listress.get(1).toString()==null&&listress.get(1).toString().length()==0){
				    	 kszh="";
				    	 js.put("SUPPLIER_ACCOUNT","");//��Ӧ���ʺ�  �����˺�1
				    }else{
				    	 kszh = listress.get(1).toString() ;
				    	 js.put("SUPPLIER_ACCOUNT",listress.get(1));//��Ӧ���ʺ�  �����˺�1
				    }				   				    
				    js.put("SUPPLIER_ACCOUNT_ID","");//��Ӧ���ʺ����
				    js.put("SUPPLIER_TAX_NO",listval.get(5));//��Ӧ��˰��    1
				    if(listress.get(0).equals("���˺�����")||listress.get(0).toString()==null&&listress.get(0).toString().length()==0){
				    js.put("SUPPLIER_BANK_NAME","");//��Ӧ�̿���������
				    }else{
				    js.put("SUPPLIER_BANK_NAME",listress.get(0));//��Ӧ�̿���������
				    }
				    js.put("SUPPLIER_BANK_TYPE","");//��Ӧ���������
				    js.put("SUPPLIER_BANK_CODE","");//��Ӧ�����е�����
				    js.put("SUPPLIER_ADDRESS",listval.get(2));//��Ӧ�̵�ַ
				    js.put("SUPPLIER_ZIP_CODE","");//��Ӧ����������
				    js.put("SUPPLIER_PHONE",listval.get(3));//��Ӧ����ϵ�绰
				    js.put("SUPPLIER_EMAIL","");//��Ӧ��E-mail
				    if( listsval.get(0).equals("A")||listsval.get(0)==null&&listsval.get(0).length()==0){
			        	  js.put("CURRENCY_CODE","");//��ͬ����
					   }else{
						   js.put("CURRENCY_CODE",listsval.get(0));//����
					   }
				    
				    js.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//��֧�ܽ��
				    js.put("Invoice_Tax_Amount",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//��֧��˰��
				    int fpsl = FPsl(DJZBHeaderVO.getDjbh());
				    js.put("Invoice_Num",fpsl);//��Ʊ����
				    js.put("PREPAY_DATE","");//Ԥ�ƺ������� 
				    js.put("COUNTERACT_AMOUNT",0.00);//���ܺ���Ԥ������
				    js.put("REMAIN_AMOUNT",0.00);//������β���ܶ�
				    js.put("ATTACH_DOC_NUM",0);//��������1
				    js.put("AGENTED_FLAG","");//������־
				    js.put("AGENTED_CODE","");//������λ����
				    js.put("TEMP1","");//�����ַ�1
				    js.put("TEMP2","");//�����ַ�2
				    js.put("TEMP3","");//�����ַ�3
				    js.put("TEMP4","");//�����ַ�4
				    js.put("TEMP5","");//�����ַ�5
				    jarry.add(js);
			  }
			  //�����Ԥ���Ļ�����P4
			List listt =  hxmoney(DJZBHeaderVO.getDjbh());
			  JSONObject vals = new JSONObject();
			  if("Y".equals(listt.get(0))){			  
			    vals.put("BILL_NO",resval);//��֧�嵥��
			    vals.put("COMPANY_CODE",bzCode);//����
			    vals.put("BILL_TYPE_CODE","P04001");//��������
			    vals.put("PRE_BILL_NO","");//�����嵥��    Ԥ֧�嵥��
			    vals.put("PRE_INV_NO","");//����Ԥ���Ʊ��   ����
			    vals.put("PRE_INV_CODE","");//����Ԥ���Ʊ����   ����
			    if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
			    	vals.put("INVOICE_NO","");//��Ʊ��
				    }else{
				    	vals.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//��Ʊ��
				    }
				   if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
					   vals.put("INVOICE_CODE","");//��Ʊ����
				   }else{
					   vals.put("INVOICE_CODE",listsval.get(4));//��Ʊ����
				   }
//			    val1.put("INVOICE_NO","");//��Ʊ��
//			    val1.put("INVOICE_CODE","");//��Ʊ����
			    vals.put("ORDER_NUM","");//������ͬ��
			    vals.put("PROJECT_NO","");//������Ŀ��
			    vals.put("CAV_AMT",listt.get(1));//�������
			    vals.put("CAV_DATE",listt.get(2));//��������
			    vals.put("TEMP1","");//�����ַ�1
			    vals.put("TEMP2","");//�����ַ�2
			    vals.put("TEMP3","");//�����ַ�3
			    vals.put("TEMP4","");//�����ַ�4
			    vals.put("TEMP5","");//�����ַ�5
			  }
			  if(str2.length()>0){
				 str3.append(str2); 
				  continue;
			  	}
			  if(DJZBHeaderVO.getZgyf()!=0){
					str2.append("\n"+"�ݹ�Ӧ������ȷ��Ӧ��");
				}
			  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName());
			  JSONArray jsrr = new JSONArray();
			  for(int k=0;k<DJZBItemVO.length;k++){
			  JSONObject b = ifc.assembleItfData(bvals.getJSONObject(k),"PUINVOICE_B_SEND",null);//����
			  jsrr.add(b);
			  }
			  JSONObject s = ifc.assembleItfData(val, "PUINVOICE_H_SEND","PUINVOICE_B_SEND"); //��ͷ
			  
			  JSONObject a = ifc.assembleItfData(js, "REPROTLIST_SEND",null);//��֧
			  List list1 = new ArrayList();//p4�ӿ�
			  StringBuffer str2s = new StringBuffer();
			  if("Y".equals(listt.get(0))){	
				  JSONObject b = ifc.assembleItfData(vals, "ADVANCEPAYMENT",null); //P4
				  String state = (String) b.get("state");
				  list1.add(0,b);
				  if(!"success".equals(state)){
						str2s.append("err");
					
					}
			  }
			  //JSONObject js = ifc.disassembleItfData(xbuss, "BCJCS1");//�������ӿ�ʾ���� 
			  String state = (String) s.get("state");
			  String states = (String) a.get("state");
			  StringBuffer strs = new StringBuffer();
			  for(int b=0;b<jsrr.size();b++){
				String state1 = (String) jsrr.getJSONObject(b).get("state");
				if(!"success".equals(state1)){
					strs.append("err");
					continue;
				}
				
			  }
			  String content = s.getString("content"); 		      
		 	  String contents = a.getString("content");
			  if("success".equals(state)&&"success".equals(states)&&strs.length()==0&&str2s.length()==0){ 				
		  	   //JSONArray jArray = new JSONArray();
		  	   JSONArray sjrr = new JSONArray();
		       JSONArray JSONArrays = new JSONArray();
		       JSONArray Jar = new JSONArray();
		       JSONArrays = JSONArray.fromObject(content);
		       Jar = JSONArray.fromObject(contents);
		      
		       JSONObject jsObject =(JSONObject)JSONArrays.get(0);//�ɹ���Ʊ��ͷ
		       JSONObject jsObject1 =(JSONObject)JSONArrays.get(1);//�ɹ���Ʊ��ϸ��Ϣ
		       JSONObject jsObject2 =(JSONObject)Jar.get(0);//��֧�嵥��Ϣ�ӿ�	        
		       sjrr.add(0, jsObject2);		       
		       sjrr.add(1, jsObject);
		       sjrr.add(2, jsrr);
		       if("Y".equals(listt.get(0))){	
		    	String val2s =   JSONObject.fromObject(list1.get(0)).getString("content");
		    	JSONArray jars =  JSONArray.fromObject(val2s);
		    	sjrr.add(2,(JSONObject)jars.get(0));//Ԥ������
		       }
		       System.out.println(str1.toString()+str2);
			      if(sjrr.size()>0&&str1.length()==0&&str2.length()==0){ 
			          for (int z = 0; z < sjrr.size(); z++) {
			        	  if(z==2){
			        		  for(int c=0;c<jsrr.size();c++){
			        			  JSONObject rjb = (JSONObject) jsrr.get(c); 
			        			  String contentt = rjb.getString("content");
			        			  JSONArray jss =new JSONArray();
			        			  jss = JSONArray.fromObject(contentt);
			        			  JSONObject jsObjectt =(JSONObject)jss.get(0);
					              JSONObject rs = ifc.sendRequest(jsObjectt, "XBUS"); 
					              if("success".equals(rs.getString("state"))){ 
					            	  list1.add(rs.getString("state"));
					            	  updateDJBH(DJZBHeaderVO.getDjbh().toString(),nyr);			            	
					              }else{ 
					                  //ʧ���߼� 
					            	  showWarningMessage("����ʧ��");
					              } 
			        		  }
			        		 continue;
			        	  }
			              JSONObject rjb = (JSONObject) sjrr.get(z); 
			              JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
			              if("success".equals(rs.getString("state"))){ 
			            	  list1.add(rs.getString("state"));
			            	  updateDJBH(DJZBHeaderVO.getDjbh().toString(),nyr);			            	
			              }else{ 
			                  //ʧ���߼� 
			            	  showWarningMessage("����ʧ��");
			              } 
			          } 
			      } 
			  } else if(!"success".equals(state)&&"success".equals(states)){
				  str2.append("\n"+"ʧ��"+content);
			  }else if("success".equals(state)&&!"success".equals(states)){
				  str2.append("\n"+"ʧ��"+contents);
			  }else{
				  str2.append("\n"+"ʧ��"+content+"  "+contents);
			  }
				
			  //�ɹ��߼�	
			  if(list1.size()!=0){
			  str.append("\n"+"���ݺ�"+DJZBHeaderVO.getDjbh()+"�ɹ�");
			  IUAPQueryBS bss = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			  String vall =BalnkVal(dJZBItemVOs[0].getKsbm_cl());
			  String value = "select account from bd_custbank  where pk_cubasdoc ='"+vall+"' ";            	  
			  try {
				String val1 = SERIALNUM(dJZBHeaderVOs.getDjbh());
				  @SuppressWarnings("unused")
				String result =(String) bss.executeQuery(value, new BeanProcessor(String.class));
				  IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
				  ItfbzVO.setBillno_bz(val1);//��֧�嵥��
		    	  ItfbzVO.setBillno_yf(dJZBHeaderVOs.getDjbh());//Ӧ������
		    	  dJZBHeaderVOs.getKsbm_cl();
		    	  dJZBItemVOs[0].getKsbm_cl();
		    	  ItfbzVO.setVouchid(dJZBHeaderVOs.getVouchid());//Ӧ��������
		    	  ItfbzVO.setKsbm_cl(dJZBItemVOs[0].getKsbm_cl());//���̹�������
		    	  ItfbzVO.setGys_account(kszh);//��Ӧ�������˺�
		    	  ItfbzVO.setPk_corp(dJZBHeaderVOs.getDwbm());//��˾		            									
				  iVOPersistence.insertVO(ItfbzVO);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
			  }
				 }else{
					  str1.append("���ݱ��"+dJZBHeaderVOs.getDjbh()+"�Ѿ����ͳɹ��������ٴη���");
//					  showWarningMessage("�Ѿ����ͳɹ��������ٴη���");
				  }
				}
				if(str1!=null&&str1.length()>0&&str3!=null&&str3.length()>0&&str!=null&&str.toString().length()>0){
					showWarningMessage(str+"\n"+str1+"��"+"\n"+str3);
				} else if(str1!=null&&str1.length()>0&&str!=null&&str.toString().length()>0){
					showWarningMessage(str+"\n"+str1.toString());
				}else if(str3!=null&&str3.length()>0&&str!=null&&str.toString().length()>0){
					showWarningMessage(str+"\n"+str3.toString());
				}else if (str1!=null&&str1.length()>0&&str3!=null&&str3.length()>0){
					showWarningMessage(str1+"\n"+str3.toString());
				}else if(str!=null&&str.toString().length()>0){
					showWarningMessage(str.toString());
				}else if(str3!=null&&str3.length()>0){
					showWarningMessage(str3.toString());
				}else if(str1!=null&&str1.length()>0){
					showWarningMessage(str1.toString());
				}
						  
		  }
		  
		//��Ƭģʽ��ZSH
			public void CxbusCard(DJZBVO vo,DjPanel djPanel){
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String nyr = sdf.format(date);	//��ȡ��ǰϵͳʱ��	
				List list = new ArrayList();
				String kszh = new String();
				StringBuffer str1 = new StringBuffer();//�Ѵ�����
				StringBuffer str2 = new StringBuffer();//�쳣
				 ITFBZVO ItfbzVO = new ITFBZVO();
//			    DJZBHeaderVO dJZBHeaderVOs = (DJZBHeaderVO)vos.get(0).getParentVO();//��ȡѡ�������ı�ͷ
//				DJZBItemVO[] dJZBItemVOs = (DJZBItemVO[])vos.get(0).getChildrenVO();//��ȡѡ���еı���
				DJZBHeaderVO DJZBHeaderVO = (DJZBHeaderVO)vo.getParentVO();
				DJZBItemVO[] DJZBItemVO = (DJZBItemVO[])vo.getChildrenVO();			
				
				
					if(DJZBHeaderVO.getDjbh()==null||DJZBHeaderVO.getDjbh().equals("")||DJZBHeaderVO.getDjbh().toString().length()==0){
						showWarningMessage("���ݺ�δ��д");
						return;
					}
				String billno=judgeDJBH(DJZBHeaderVO.getDjbh().toString());
				  System.out.println("billno:"+billno);
				  if(billno == null){	
					
						//У������
						StringBuffer resval2 =	CheckData( DJZBHeaderVO, DJZBItemVO);
						if(resval2!=null&&resval2.toString().length()>0){
							str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"���ݺ���"+resval2.toString());
//							showWarningMessage("��"+DJZBHeaderVO.getDjbh()+"���ݺ�����"+resval2.get(1).toString());
//							continue;
						}
						String bzCode = BzCode(DJZBHeaderVO.getDwbm());	
						List listress =SelectBank(DJZBItemVO[0].getKsbm_cl(),DJZBHeaderVO.getDwbm());
						if(listress.get(0).equals("���˺�����")){
							  str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"���ݺ��¿����˺�����δ��д");
//							  showWarningMessage("�����˺�����δ��д");
//							  continue;
						}
						List listval2 =	queryks(DJZBHeaderVO.getVouchid(),DJZBHeaderVO.getDwbm());	
						ITFSNVO itfsnvo = new ITFSNVO();
						List revallist = reval(DJZBHeaderVO.getDjbh());
						if(revallist ==null ||revallist.size()==0){
						IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
						itfsnvo.setPK_CORP(DJZBHeaderVO.getDwbm());
						itfsnvo.setDR("0");
						itfsnvo.setBILLTYPE("YF");
						itfsnvo.setBILLNO(DJZBHeaderVO.getDjbh());			
						 try {
						iVOPersistence.insertVO(itfsnvo);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
						List<String> listvall = CubasdocVal(listval2.get(0).toString());
						StringBuffer lists3 =ListCheck(listvall);//У����������Ƿ�Ϊ��
					
						if(lists3!=null&&lists3.toString().length()>0){
							str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+lists3.toString());
//							 showWarningMessage(lists3.get(1).toString());
//							 continue;
							}
						
//						List<String> lists =ListVal(DJZBHeaderVO.getDjbh());
						List<String> listsval =listVal(DJZBHeaderVO.getDjbh());
						if(listsval.get(0).equals("A")){
							str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"�����£����֣���ͬ�ţ���ͬ���ƣ���Ʊ�ţ���Ʊ����Ϊ�գ�����д");
						}else{
						StringBuffer listsval2 = checkListval(listsval);
						if(listsval2!=null&listsval2.toString().length()>0){
							str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"���ݺ���"+listsval2);
//							 showWarningMessage(lists3.get(1).toString());
//							 continue;
						}
						}
						JSONObject val = new JSONObject();
						String resval =	SERIALNUM(DJZBHeaderVO.getDjbh());
					    val.put("EXPANSE_REPORT_ID",resval);//��֧�嵥��
					    val.put("COMPANY_CODE",bzCode);//����
					    val.put("BILL_TYPE_CODE","P04001");//�������� DJZBHeaderVO.getYwbm()
					    System.out.println(DJZBHeaderVO.getVouchid());
					    val.put("OWER_SYS_ID","JC");//��Դϵͳ
					   if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
						val.put("INVOICE_NO","");//��Ʊ��
					    }else{
					    val.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//��Ʊ��
					    }
					   if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
						val.put("INVOICE_CODE","");//��Ʊ����
					   }else{
					    val.put("INVOICE_CODE",listsval.get(4));//��Ʊ����
					   }
					    String Ftype = Fptype(DJZBHeaderVO.getZyx28());
					    if(Ftype==null){
//					    	str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"���ݺ��·�Ʊ����Ϊ��");
					    	 val.put("INVOICE_TYPE","");//��Ʊ����
					    }else{
					    val.put("INVOICE_TYPE",Ftype);//��Ʊ����
					    }
//					    val.put("INVOICE_TYPE",Ftype);//��Ʊ����
					    val.put("INVOICE_DATE",DJZBHeaderVO.getDjrq().toString().replace("-", ""));//��Ʊ����
					   String sl = Sl(DJZBHeaderVO.getDjbh());
					    val.put("TAX_RATE",Double.valueOf(sl));//˰�� 1
					    UFDouble dJZBItemVO = new UFDouble();
					    UFDouble Dfybsj = new UFDouble();
					    for(int j=0;j<DJZBItemVO.length;j++){
					    	 dJZBItemVO = DJZBItemVO[j].getDfbbwsje().add(dJZBItemVO) ;
					    	 Dfybsj =DJZBItemVO[j].getDfybsj().add(Dfybsj);
					    }
					    
//					    val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())/100));//��Ʊ���
//					    val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())/100));//��Ʊ˰��
					    UFDouble dfybje =  DJZBItemVO[0].getDfybje();
						UFDouble dfybsj =  DJZBItemVO[0].getDfybsj();
						UFDouble fpje = dfybsj.add(dfybje);
						val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//��Ʊ���
						val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//��Ʊ˰��
//						val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(fpje.toString())));//��Ʊ���
//					    val.put("INVOICE_TAX_AMOUNT",DJZBItemVO[0].getDfybsj());//��Ʊ˰��
					    val.put("PREPAY_DATE","");//Ԥ�Ƹ�������
					    val.put("SETTLEMENT_TYPE","A10");//���㷽ʽ
					    val.put("COUNTERACT_AMOUNT",0.00);//����Ԥ������lists.get(2)
					    val.put("CAN_END_AMT_CUR",0.00);//��β����
					    val.put("PAYEE_CODE",listvall.get(0));//�ܿ��˴���  1
					    val.put("PAYEE_TYPE",listvall.get(1));//�ܿ�������
					    val.put("PAYEE_NAME",listvall.get(4));//�ܿ�������
					    if(listress.get(0).equals("���˺�����")||listress.get(1).toString().length()==0&&listress.get(1)==null){
					    val.put("PAYEE_ACCOUNT","");//�ܿ����ʺ�
					    }else{
					    val.put("PAYEE_ACCOUNT",listress.get(1));//�ܿ����ʺ�
					    }
					    val.put("PAYEE_ACCOUNT_ID","");//�ܿ����ʺ����
					    val.put("PAYEE_TAX_NO",listvall.get(5));//�ܿ���˰��
					    if(listress.get(0).equals("���˺�����")||listress.get(0).toString().length()==0&&listress.get(0)==null){
					    val.put("PAYEE_ACCOUNT_NAME","");//�ܿ��˿���������
					    }else{
					    val.put("PAYEE_ACCOUNT_NAME",listress.get(0));//�ܿ��˿���������
					    }
					    val.put("PAYEE_BANK_TYPE","");//�ܿ����������
					    val.put("PAYEE_BANK_CODE","");//�ܿ������е�����
					    val.put("COUNTRY","");//�ܿ���ʡ��/����
					    val.put("PAYEE_ADDRESS",listvall.get(2));//�ܿ����ܿ��˵�ַ
					    val.put("PAYEE_ZIP_CODE","");//�ܿ�����������
					    val.put("PAYEE_PHONE",listvall.get(3));//�ܿ�����ϵ�绰
					    val.put("PAYEE_EMAIL","");//�ܿ���E-mail
					    val.put("LC_NO","");//����֤��
					    val.put("DETAIL_NUM",DJZBItemVO.length);//��Ʊ��ϸ��¼�� 1
					    val.put("TEMP1","");//�����ַ�1
					    val.put("TEMP2","");//�����ַ�2
					    val.put("TEMP3","");//�����ַ�3
					    val.put("TEMP4","");//�����ַ�4
					    val.put("TEMP5","");//�����ַ�5
					    JSONArray bvals = new JSONArray();
//					    int rows = 2; //���rows�ĳ�����������
					    for (int j = 0; j < DJZBItemVO.length; j++) {	
					    	
					    	JSONObject val1 = new JSONObject();
					    	String resval1 =SERIALNUM(DJZBHeaderVO.getDjbh());
					    	System.out.println("��֧��"+resval1);
					        val1.put("BILL_NO",resval1);//��֧�嵥��
					        val1.put("COMPANY_CODE",bzCode);//����}
					        val1.put("BILL_TYPE_CODE","P04001");//��������
					        val1.put("OWER_SYS_ID","JC");//��Դϵͳ
//					        val1.put("INVOICE_CODE",listsval.get(4));//��Ʊ����
					        if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
					        	val1.put("INVOICE_CODE","");//��Ʊ����
							   }else{
								   val1.put("INVOICE_CODE",listsval.get(4));//��Ʊ����
							   }
					        if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
					        	val1.put("INVOICE_NO","");//��Ʊ��
							    }else{
							    val1.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//��Ʊ��
							    }
//					        val1.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//��Ʊ��
					        val1.put("INVOICE_DETAIL_ID",j+1);//��Ʊ��ϸ�����		        
					        String zrzx = queryzrzx(DJZBItemVO[j].getDeptid());
					        if(zrzx.length()>8){
					        	if(j<1){
					        	str2.append("\n"+"��"+DJZBHeaderVO.getDjbh()+"������,������ѡ�����µĲ���");
					        	}
					        	val1.put("COST_CENTER","");//��������			        	
					        }else{
					        val1.put("COST_CENTER",zrzx);//��������
					        }
					        val1.put("COST_CENTER",zrzx);//��������
					        val1.put("AGENTED_FLAG","");//������־
					        val1.put("AGENTED_CODE","");//������λ����
					        val1.put("AGENT_FLAG","");//������
					        val1.put("AGENT_CODE","");//�����û�����
					        val1.put("AGENT_TYPE","");//�����û����
					        val1.put("AGENT_ACCOUNT","");//�����û��˺�
					        val1.put("UNIT_PRICE",DJZBItemVO[j].getHsdj()==null?16:String.format("%.2f",Double.valueOf(DJZBItemVO[j].getHsdj().toString())));//����
					        val1.put("PRE_AMOUNT",0.00);//����Ԥ������lists.get(2)
					        val1.put("QANTITY",DJZBItemVO[j].getDfshl()==null?0.0:Double.valueOf(String.format("%.5f",Double.valueOf(DJZBItemVO[j].getDfshl().toString()))));//����
					        val1.put("AMOUNT",DJZBItemVO[j].getDfbbwsje());//���
					        val1.put("TAX_AMOUNT",DJZBItemVO[j].getDfybsj()==null?"":String.format("%.2f",Double.valueOf(DJZBItemVO[j].getDfybsj().toString())));//˰��			      
//					        val1.put("ORDER_NO",listsval.get(1));//��ͬ��2
					        if( listsval.get(0).equals("A")||listsval.get(1)==null&&listsval.get(1).length()==0){
					        	  val1.put("ORDER_NO","");//��ͬ����
							   }else{
								   String djlx =  djlx(DJZBHeaderVO.getDjbh());
								   if("���Ļ���".equals(djlx.trim())){
									 val1.put("ORDER_NO","");//��ͬ����
								   }else{
								   val1.put("ORDER_NO",listsval.get(1));//��ͬ��2
								   }
							   }			      
					        if( listsval.get(0).equals("A")||listsval.get(2)==null&&listsval.get(2).length()==0){
					        	  val1.put("ORDER_DESC","");//��ͬ����
							   }else{
								   String djlx =  djlx(DJZBHeaderVO.getDjbh());
								   if("���Ļ���".equals(djlx.trim())){
									   val1.put("ORDER_DESC","");//��ͬ����  
								   }else{
								   val1.put("ORDER_DESC",listsval.get(2));//��ͬ����
								   }
							   }
					        val1.put("PROJECT_NO",DJZBItemVO[j].getXm()==null?"":DJZBItemVO[j].getXm());//��Ŀ��
					        val1.put("PRJECT_NAME",DJZBItemVO[j].getXm_name()==null?"":DJZBItemVO[j].getXm_name());//��Ŀ����
					        val1.put("BPO_PROPERTY","");//��ͬ���Դ���
					        val1.put("BPO_TYPE","");//�ɹ����ʹ���
					        Map map = this.QueryInvl(DJZBItemVO[j].getCinventoryid(),DJZBHeaderVO.getDwbm().trim());
					        String chbm = map.get("invcode").toString();
					        String invc =  invcl(chbm);
					        String invcs = invcls(chbm);
					        val1.put("FEE_TYPE",invc);//�������ʹ���  ��ȡ���ϱ����ǰ��λ
					        val1.put("FEE_DESC",invcs);//������ϸ1
					     
					       
					        val1.put("MR_TYPE_SUB1",invcl(chbm));//ҵ����������1
					        val1.put("MR_TYPE_SUB2","");//ҵ����������2
					        val1.put("PROJECT_SPEC1","");//��Ŀ����1
					        val1.put("PROJECT_SPEC2","");//��Ŀ����2
					        val1.put("BUSI_TYPE1","");//ҵ�����һ
					        val1.put("BUSI_TYPE2","");//ҵ������
					        val1.put("MAT_TYPE","");//�������
					        
					        val1.put("MAT_TYPE1",invc);//���ϴ���
					        val1.put("MAT_TYPE2","");//��������
					        val1.put("MAT_TYPE3","");//����ϸ��
					        
					        val1.put("MAT_CODE","");//���ϴ���
					        val1.put("TASK_CODE","");//��ҵ������
					        val1.put("REPORT_TYPE","");//��֧����
					        val1.put("REPORT_TYPE_D","");//��֧ϸ��
					        val1.put("TEMP1","");//�����ַ�1
					        val1.put("TEMP2","");//�����ַ�2
					        val1.put("TEMP3","");//�����ַ�3
					        val1.put("TEMP4","");//�����ַ�4
					        val1.put("TEMP5","");//�����ַ�5
					        val1.put("TEMP6","");//�����ַ�6
					        val1.put("TEMP7","");//�����ַ�7
					        val1.put("TEMP8","");//�����ַ�8
					        val1.put("TEMP9","");//�����ַ�9
					        val1.put("TEMP10","");//�����ַ�10
					        val1.put("TEMP11",0.00);//�����ַ�11
					        val1.put("TEMP12",0.00);//�����ַ�12
					        val1.put("TEMP13",0.00);//�����ַ�13
					        val1.put("TEMP14",0.00);//�����ַ�14
					        val1.put("TEMP15",0.00);//�����ַ�15
					        bvals.add(val1);
					    }
					  val.put("bodylist", bvals);
					  JSONArray jarry = new JSONArray();
					  JSONObject js = new JSONObject();
					  for(int j=0;j<DJZBItemVO.length;j++){
						  List listres = queryks(DJZBHeaderVO.getVouchid(),DJZBHeaderVO.getDwbm());
						  List<String> listval = CubasdocVal(listres.get(0).toString());
						  js.put("EXPANSE_REPORT_ID",resval);//��֧�嵥��
							js.put("BILL_TYPE_CODE","P04001");//��������
						    js.put("OWER_SYS_ID","JC");//��Դϵͳ
						    js.put("COMPANY_CODE",bzCode);//����
						    js.put("RECEIPT_NO","");//���ӵ���
						    js.put("RESP_UNIT_ID","");//���쵥λ
						    js.put("REPORT_DATE",DJZBHeaderVO.getDjrq()==null?"":DJZBHeaderVO.getDjrq().toString().replace("-", ""));//��֧����
						    js.put("BILL_DESC","��֧ժҪ");//��֧˵��			  
						 
						    js.put("REPORT_UNIT_ID",bzCode);//��֧��λ��֯���� 1
						    js.put("REPORTOR_CODE","L00031");//�ύ�˹���1  
						    js.put("INPUT_VOUCHER_ID","");//����˹��� ��ȡ
						    js.put("INPUT_VOUCHER_TIME",DJZBHeaderVO.getShrq()==null?"":DJZBHeaderVO.getShrq().toString().replace("-", ""));//�������
						    js.put("SUPPLIER_CODE",listval.get(0));//��Ӧ�̴���  
						    js.put("SUPPLIER_TYPE",listval.get(1));//��Ӧ������
						    js.put("SUPPLIER_NAME",listval.get(4));//��Ӧ������
						    if(listress.get(0).equals("���˺�����")||listress.get(1).toString()==null&&listress.get(1).toString().length()==0){
						    	 kszh="";
						    	 js.put("SUPPLIER_ACCOUNT","");//��Ӧ���ʺ�  �����˺�1
						    }else{
						    	 kszh = listress.get(1).toString() ;
						    	 js.put("SUPPLIER_ACCOUNT",listress.get(1));//��Ӧ���ʺ�  �����˺�1
						    }				   				    
						    js.put("SUPPLIER_ACCOUNT_ID","");//��Ӧ���ʺ����
						    js.put("SUPPLIER_TAX_NO",listval.get(5));//��Ӧ��˰��    1
						    if(listress.get(0).equals("���˺�����")||listress.get(0).toString()==null&&listress.get(0).toString().length()==0){
						    js.put("SUPPLIER_BANK_NAME","");//��Ӧ�̿���������
						    }else{
						    js.put("SUPPLIER_BANK_NAME",listress.get(0));//��Ӧ�̿���������
						    }
						    js.put("SUPPLIER_BANK_TYPE","");//��Ӧ���������
						    js.put("SUPPLIER_BANK_CODE","");//��Ӧ�����е�����
						    js.put("SUPPLIER_ADDRESS",listval.get(2));//��Ӧ�̵�ַ
						    js.put("SUPPLIER_ZIP_CODE","");//��Ӧ����������
						    js.put("SUPPLIER_PHONE",listval.get(3));//��Ӧ����ϵ�绰
						    js.put("SUPPLIER_EMAIL","");//��Ӧ��E-mail
						    if( listsval.get(0).equals("A")||listsval.get(0)==null&&listsval.get(0).length()==0){
					        	  js.put("CURRENCY_CODE","");//��ͬ����
							   }else{
								   js.put("CURRENCY_CODE",listsval.get(0));//����
							   }
//						    val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//��Ʊ���
//						    val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//��Ʊ˰��
						    js.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//��֧�ܽ��
						    js.put("Invoice_Tax_Amount",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//��֧��˰��
						    int fpsl = FPsl(DJZBHeaderVO.getDjbh());
						    js.put("Invoice_Num",fpsl);//��Ʊ����
						    js.put("PREPAY_DATE","");//Ԥ�ƺ������� 
						    js.put("COUNTERACT_AMOUNT",0.00);//���ܺ���Ԥ������
						    js.put("REMAIN_AMOUNT",0.00);//������β���ܶ�
						    js.put("ATTACH_DOC_NUM",0);//��������1
						    js.put("AGENTED_FLAG","");//������־
						    js.put("AGENTED_CODE","");//������λ����
						    js.put("TEMP1","");//�����ַ�1
						    js.put("TEMP2","");//�����ַ�2
						    js.put("TEMP3","");//�����ַ�3
						    js.put("TEMP4","");//�����ַ�4
						    js.put("TEMP5","");//�����ַ�5
						    jarry.add(js);
					  }
					  JSONObject vals = new JSONObject();
					  List listt =  hxmoney(DJZBHeaderVO.getDjbh());
					  if("Y".equals(listt.get(0))){
					 
					    vals.put("BILL_NO",resval);//��֧�嵥��
					    vals.put("COMPANY_CODE",bzCode);//����
					    vals.put("BILL_TYPE_CODE","P04001");//��������
					    vals.put("PRE_BILL_NO","");//�����嵥��
					    vals.put("PRE_INV_NO","");//����Ԥ���Ʊ��
					    vals.put("PRE_INV_CODE","");//����Ԥ���Ʊ����
					    if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
					    	vals.put("INVOICE_NO","");//��Ʊ��
						    }else{
						    	vals.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//��Ʊ��
						    }
						   if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
							   vals.put("INVOICE_CODE","");//��Ʊ����
						   }else{
							   vals.put("INVOICE_CODE",listsval.get(4));//��Ʊ����
						   }
//					    val1.put("INVOICE_NO","");//��Ʊ��
//					    val1.put("INVOICE_CODE","");//��Ʊ����
					    vals.put("ORDER_NUM","");//������ͬ��
					    vals.put("PROJECT_NO","");//������Ŀ��
					    vals.put("CAV_AMT",listt.get(1));//�������
					    vals.put("CAV_DATE",listt.get(2));//��������
					    vals.put("TEMP1","");//�����ַ�1
					    vals.put("TEMP2","");//�����ַ�2
					    vals.put("TEMP3","");//�����ַ�3
					    vals.put("TEMP4","");//�����ַ�4
					    vals.put("TEMP5","");//�����ַ�5

					  }
					  
					  
					if(str2.length()>0&&str2!=null){
						showWarningMessage(str2.toString());
						return;
					}
					if(DJZBHeaderVO.getZgyf()!=0){
						str2.append("\n"+"�ݹ�Ӧ������ȷ��Ӧ��");
					}
						  
					  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
					  JSONObject s = ifc.assembleItfData(val, "PUINVOICE_H_SEND","PUINVOICE_B_SEND");//��ͷ
					  JSONArray jsrr = new JSONArray();
					  for(int k=0;k<DJZBItemVO.length;k++){
						  JSONObject b = ifc.assembleItfData(bvals.getJSONObject(k),"PUINVOICE_B_SEND",null);//����
						  jsrr.add(b);
						  }
					  JSONObject a = ifc.assembleItfData(js, "REPROTLIST_SEND",null);//��֧
					  List list1 = new ArrayList();//p4�ӿ�
					  StringBuffer str2s = new StringBuffer();
					  if("Y".equals(listt.get(0))){	
						  JSONObject b = ifc.assembleItfData(vals, "ADVANCEPAYMENT",null); //P4
						  String state = (String) b.get("state");
						  list1.add(0,b);
						  if(!"success".equals(state)){
								str2s.append("err");
							
							}
					  }
					  //JSONObject js = ifc.disassembleItfData(xbuss, "BCJCS1");//�������ӿ�ʾ���� 
					  String state = (String) s.get("state");
					  String states = (String) a.get("state");
					  String content = s.getString("content"); 		      
				 	  String contents = a.getString("content");
				 	 StringBuffer strs = new StringBuffer();
				 	 for(int b=0;b<jsrr.size();b++){
							String state1 = (String) jsrr.getJSONObject(b).get("state");
							if(!"success".equals(state1)){
								strs.append("err");
								continue;
							}
							
						  }
					  if("success".equals(state)&&"success".equals(states)&&strs.length()==0){ 				
				  	   //JSONArray jArray = new JSONArray();
				  	   JSONArray sjrr = new JSONArray();
				       JSONArray JSONArrays = new JSONArray();
				       JSONArray Jar = new JSONArray();
				       JSONArrays = JSONArray.fromObject(content);
				       Jar = JSONArray.fromObject(contents);
				       JSONObject jsObject =(JSONObject)JSONArrays.get(0);//�ɹ���Ʊ��ͷ
				       JSONObject jsObject1 =(JSONObject)JSONArrays.get(1);//�ɹ���Ʊ��ϸ��Ϣ
				       JSONObject jsObject2 =(JSONObject)Jar.get(0);//��֧�嵥��Ϣ�ӿ�	        
				       sjrr.add(0, jsObject2);
				       sjrr.add(1, jsObject);
				       sjrr.add(2, jsrr);
				       if("Y".equals(listt.get(0))){	
					    	String val2s =   JSONObject.fromObject(list1.get(0)).getString("content");
					    	JSONArray jars =  JSONArray.fromObject(val2s);
					    	sjrr.add(2,(JSONObject)jars.get(0));//Ԥ������
					       }
					      if(sjrr.size()>0&&str1.length()==0&&str2.length()==0&&str2s.length()==0){ 
					          for (int z = 0; z < sjrr.size(); z++) {
					        	  if(z==2){
					        		  for(int c=0;c<jsrr.size();c++){
					        			  JSONObject rjb = (JSONObject) jsrr.get(c); 
					        			  String contentt = rjb.getString("content");
					        			  JSONArray jss =new JSONArray();
					        			  jss = JSONArray.fromObject(contentt);
					        			  JSONObject jsObjectt =(JSONObject)jss.get(0);
							              JSONObject rs = ifc.sendRequest(jsObjectt, "XBUS"); 
							              if("success".equals(rs.getString("state"))){ 
							            	  list.add(rs.getString("state"));
							            	  updateDJBH(DJZBHeaderVO.getDjbh().toString(),nyr);			            	
							              }else{ 
							                  //ʧ���߼� 
							            	  showWarningMessage("����ʧ��");
							              } 
					        		  }
					        		 continue;
					        	  }
					              JSONObject rjb = (JSONObject) sjrr.get(z); 
					              JSONObject rs = ifc.sendRequest(rjb, "XBUS"); 
					              if("success".equals(rs.getString("state"))){ 
					            	  list.add(rs.getString("state"));
					            	  updateDJBH(DJZBHeaderVO.getDjbh().toString(),nyr);			            	
					              }else{ 
					                  //ʧ���߼� 
					            	  showWarningMessage("����ʧ��");
					              } 
					          } 
					      } 
					  } else if(!"success".equals(state)&&"success".equals(states)){
						  str2.append("\n"+"ʧ��"+content);
					  }else if("success".equals(state)&&!"success".equals(states)){
						  str2.append("\n"+"ʧ��"+contents);
					  }else{
						  str2.append("\n"+"ʧ��"+content+"  "+contents);
					  }
						
					  }
					  //�ɹ��߼�	
					  if(list.size()!=0){
					 showWarningMessage("���ݺ�"+DJZBHeaderVO.getDjbh()+"���ͳɹ�");
					  IUAPQueryBS bss = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					  String vall =BalnkVal(DJZBItemVO[0].getKsbm_cl());
					  String value = "select account from bd_custbank  where pk_cubasdoc ='"+vall+"' ";            	  
					  try {
						String val1 = SERIALNUM(DJZBHeaderVO.getDjbh());
						  @SuppressWarnings("unused")
						String result =(String) bss.executeQuery(value, new BeanProcessor(String.class));
						  IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
						  ItfbzVO.setBillno_bz(val1);//��֧�嵥��
				    	  ItfbzVO.setBillno_yf(DJZBHeaderVO.getDjbh());//Ӧ������
				    	  DJZBHeaderVO.getKsbm_cl();
				    	  DJZBItemVO[0].getKsbm_cl();
				    	  ItfbzVO.setVouchid(DJZBHeaderVO.getVouchid());//Ӧ��������
				    	  ItfbzVO.setKsbm_cl(DJZBItemVO[0].getKsbm_cl());//���̹�������
				    	  ItfbzVO.setGys_account(kszh);//��Ӧ�������˺�
				    	  ItfbzVO.setPk_corp(DJZBHeaderVO.getDwbm());//��˾		            									
						  iVOPersistence.insertVO(ItfbzVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							}
					  
					  
						 }else{
		       	  showWarningMessage("�Ѿ����ͳɹ��������ٴη���");
				 }
				 			  
				
				 if(str1!=null&&str1.length()>0&str2!=null&&str2.length()>0){
						showWarningMessage(str1+"��"+"\n"+str2);
					}else if(str1!=null&&str1.length()>0){
						showWarningMessage(str1.toString());
					}
				  }
		
		//У���ֶε�ֵ�Ƿ�Ϊ��
		public StringBuffer CheckData(DJZBHeaderVO DJZBHeaderVO,DJZBItemVO[] DJZBItemVO){
			StringBuffer str = new StringBuffer();
		
			if(DJZBHeaderVO.getZyx28()==null||DJZBHeaderVO.getZyx28().equals("")||DJZBHeaderVO.getZyx28().length()<=0){
				str.append("\n"+"��Ʊ����Ϊ��,����д");
//				list.add(0,"0");
//				list.add(1,"��Ʊ����Ϊ��,����д");
//				return list;
			}else if(DJZBHeaderVO.getVouchid()==null||DJZBHeaderVO.getVouchid().equals("")||DJZBHeaderVO.getVouchid().length()<=0){	
				str.append("\n"+"��Ʊ����Ϊ��,����д");
//				list.add(0,"0");
//				list.add(1,"��Ʊ����Ϊ��,����д");
//				return list;
			}else if(DJZBHeaderVO.getDjbh()==null||DJZBHeaderVO.getDjbh().equals("")||DJZBHeaderVO.getDjbh().length()<=0){
				str.append("\n"+"���ݱ��Ϊ�գ�����д");
//				list.add(0,"0");
//				list.add(1,"���ݱ��Ϊ��,����д");
//				return list;
			}else if(DJZBHeaderVO.getDwbm()==null||DJZBHeaderVO.getDwbm().equals("")||DJZBHeaderVO.getDwbm().length()<=0){
				str.append("\n"+"��˾Ϊ��,����д");
//				list.add(0,"0");
//				list.add(1,"��˾Ϊ��,����д");
//				return list;
			}else if(DJZBItemVO[0].getKsbm_cl()==null||DJZBItemVO[0].getKsbm_cl().equals("")||DJZBItemVO[0].getKsbm_cl().length()<=0){
				str.append("\n"+"��������Ϊ��,����д");
//				list.add(0,"0");
//				list.add(1,"��������Ϊ��,����д");
//				return list;
			}else if(DJZBHeaderVO.getDjrq()==null||DJZBHeaderVO.getDjbh().equals("")||DJZBHeaderVO.getDjbh().length()<=0){
				str.append("\n"+"��������Ϊ��,����д");
//				list.add(0,"0");
//				list.add(1,"��������Ϊ��,����д");
//				return list;
			}else if(DJZBItemVO[0].getDfybje()==null||DJZBItemVO[0].getDfybje().equals("")||DJZBItemVO[0].getDfybje().toString().length()<=0){
				str.append("\n"+"���Ϊ��,����д");
//				list.add(0,"0");
//				list.add(1,"���Ϊ��,����д");
//				return list;
			}else if(DJZBItemVO[0].getDfybsj()==null||DJZBItemVO[0].getDfybsj().equals("")||DJZBItemVO[0].getDfybsj().toString().length()<=0){
				str.append("\n"+"˰��Ϊ��,����д");
//				list.add(0,"0");
//				list.add(1,"˰��Ϊ��,����д");
			}else if(DJZBItemVO[0].getDeptid()==null||DJZBItemVO[0].getDeptid().equals("")||DJZBItemVO[0].getDeptid().toString().length()<=0){
				str.append("\n"+"����IDΪ��,����д");
//				showWarningMessage("����IDΪ��,����д");
//				list.add(0,"0");
//				list.add(1,"����IDΪ��,����д");
//				return list;
			}else if(DJZBItemVO[0].getCinventoryid()==null||DJZBItemVO[0].getCinventoryid().equals("")||DJZBItemVO[0].getCinventoryid().toString().length()<=0){
				str.append("\n"+"�����������pkΪ��,����д");
//				list.add(0,"0");
//				list.add(1,"�����������pkΪ��,����д");
//				return list;
			}else if(DJZBItemVO[0].getHsdj()==null||DJZBItemVO[0].getHsdj().equals("")||DJZBItemVO[0].getHsdj().toString().length()<=0){
				str.append("\n"+"��˰����Ϊ��,����д");
//				list.add(0,"0");
//				list.add(1,"��˰����Ϊ��,����д");
//				return list;
			}else if(DJZBHeaderVO.getShrq()==null||DJZBHeaderVO.getShrq().equals("")){
				str.append("\n"+"�������Ϊ��,����д");
//				list.add(0,"0");
//				list.add(1,"�������Ϊ��,����д");
//				return list;
//			}else if(DJZBItemVO[0].getZy()==null||DJZBItemVO[0].getZy().length()<=0||DJZBItemVO[0].getZy().equals("")){
//				str.append("\n"+"ժҪΪ�գ�����д");
//			}
			}
			return str;
		
		}
		//�жϿ����Ƿ�Ϊ��
		public StringBuffer ListCheck(List list){
			List lists= new ArrayList();
			StringBuffer str2 = new StringBuffer();
			if(list.get(0).equals("")||list.get(0).toString().length()!=6){
				str2.append("û�ж�Ӧ��MDM���̱���,����д");
//				lists.add(0,"0");
//				lists.add(1,"û�ж�Ӧ��MDM���̱���,����д");
//				return lists;
			}else if(list.get(1).equals(""))
			{
				str2.append(" ��������Ϊ��,����д");
//				lists.add(0,"0");
//				lists.add(1,"��������Ϊ��,����д");
//				return lists;
			}else if(list.get(2).equals("")){
				str2.append(" ������ַ����Ϊ��,����д");
//				lists.add(0,"0");
//				lists.add(1," ������ַ����Ϊ��,����д");
//				return lists;
			}else if(list.get(4).equals("")){
				str2.append(" ���̱���Ϊ��,����д");
//				lists.add(0,"0");
//				lists.add(1,"���̱���Ϊ��,����д");
//				return lists;
			}else if(list.get(5).equals("")){
				str2.append(" ����˰��Ϊ��,����д");
//				lists.add(0,"0");
//				lists.add(1,"����˰��Ϊ��,����д");
//				return lists;
			}
			return str2;
			
		}
		/**
		 * �����ֶ��Ƿ�Ϊ��
		 * ���۷�Ʊͷ
		 * wy
		 * 2019/10/29
		 */
		  public List judgeNXFPT(DJZBHeaderVO hvo,DJZBItemVO[] bvo,String nyr,String djbh,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble jfshl,UFDouble Jfybwsje,UFDouble dj,UFDouble Jfybje,String sl){
			  List<String> list = new ArrayList<String>();
			  Map kpTaxCode = kpTaxCode(hvo.getDwbm());
			  Map BodyMap = selectCubasdoc(djbh);
			  String nx = "���۷�Ʊ��������";
			  list.add(0,"1");
			  if(kpTaxCode.get("def8")==null||"".equals(kpTaxCode.get("def8"))||kpTaxCode.get("def8").toString().length()<=0){
				  list.add(0,"0");
				  list.add(1,"��˾��(���״���)Ϊ�գ���ά��");
				  return list;
			  }else if(queryzrzx(bvo[0].getDeptid())== null ||"".equals(queryzrzx(bvo[0].getDeptid()))){
				  list.add(0,"0");
				  list.add(1,"��������Ϊ�գ���ά�� ");
				  return list;
			  }else if(DJLX(nx)==null || "".equals(DJLX(nx)==null)){
				  list.add(0,"0");
				  list.add(1,"��������Ϊ�գ���ά�� ");
				  return list;
			  }else if (hvo.getDjbh()==null || "".equals(hvo.getDjbh())) {
				  list.add(0,"0");
				  list.add(1,"ҵ�񵥾ݺ� Ϊ�գ���ά�� ");
				  return list;
			  }else if (hvo.getZyx17()==null || "".equals(hvo.getZyx17())) {
				  list.add(0,"0");
				  list.add(1,"ֽ�ʷ�Ʊ��Ϊ�գ���ά�� ");
				  return list;
			  }else if (BodyMap.get("ccreditnum")==null || "".equals(BodyMap.get("ccreditnum"))) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ����Ϊ�գ���ά�� ");
				  return list;
			  }else if (fplx(hvo.getZyx28())==null || "".equals(fplx(hvo.getZyx28()))) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ���� Ϊ�գ���ά�� ");
				  return list;
			  }else if (custcode==null || "".equals(custcode)) {
				  list.add(0,"0");
				  list.add(1,"�����û����� Ϊ�գ���ά�� ");
				  return list;
			  }else if (custcode==null || "".equals(custcode)) {
				  list.add(0,"0");
				  list.add(1,"�����û����� Ϊ�գ���ά�� ");
				  return list;
			  }else if(financeCode(hvo.getDwbm(),queryzrzx(bvo[0].getDeptid()))== null ||"".equals(financeCode(hvo.getDwbm(),queryzrzx(bvo[0].getDeptid())))){
				  list.add(0,"0");
				  list.add(1,"������������Ϊ�գ���ά�� ");
				  return list;
			  }else if (jfshl == null || "".equals(jfshl)) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ����Ϊ�գ���ά�� ");
				  return list;
			  }else if (Jfybwsje == null || "".equals(Jfybwsje)) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ���(����˰)Ϊ�գ���ά�� ");
				  return list;
			  }else if (dj == null || "".equals(dj)) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ�۸񣨲���˰��Ϊ�գ���ά�� ");
				  return list;
			  }else if (Jfybje == null || "".equals(Jfybje)) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ˰���Ϊ�գ���ά�� ");
				  return list;
			  }else if (Jfybje == null || "".equals(Jfybje)) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ��˰�ϼƽ��Ϊ�գ���ά�� ");
				  return list;
			  }else if (sl == null || "".equals(sl)) {
				  list.add(0,"0");
				  list.add(1,"˰�� Ϊ�գ���ά�� ");
				  return list;
			  }else if (hvo.getDjrq() == null || "".equals(hvo.getDjrq())) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ����  Ϊ�գ���ά�� ");
				  return list;
			  }else if (queryLRR == null || "".equals(queryLRR)) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ��  Ϊ�գ���ά�� ");
				  return list;
			  }else if (kpTaxCode.get("unitname") == null || "".equals(kpTaxCode.get("unitname"))) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ��λ���� Ϊ�գ���ά�� ");
			  }else if (kpTaxCode.get("taxcode") == null || "".equals( kpTaxCode.get("taxcode"))) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ��λ˰��Ϊ�գ���ά�� ");
			  }	else if ("".equals(custcode.get("taxpayerid"))||custcode.get("taxpayerid")==null) {
				  list.add(0,"0");
				  list.add(1,"������λ˰��Ϊ�գ���ά�� ");
			  }	else if (BodyMap.get("accname")==null || "".equals(BodyMap.get("accname"))) {
				  list.add(0,"0");
				  list.add(1," ������������Ϊ�գ���ά�� ");
			  }	else if (BodyMap.get("account")==null||"".equals(BodyMap.get("account"))) {
				  list.add(0,"0");
				  list.add(1," ���������ʺ�Ϊ�գ���ά�� ");
			  }	else if (jsfs==null||"".equals(jsfs)) {
				  list.add(0,"0");
				  list.add(1,"���㷽ʽ Ϊ�գ���ά�� ");
			 }else if (person(queryLRR,hvo.getDwbm())== null || "".equals(person(queryLRR,hvo.getDwbm()))) {
				  list.add(0,"0");
				  list.add(1,"�����˹���(��Ʊ�˹���)Ϊ�գ���ά�� ");
			  }else if (nyr == null||"".equals(nyr)) {
				  list.add(0,"0");
				  list.add(1," Ԥ������ Ϊ�գ���ά�� ");
			  }else if (judgeCurrency ==null||"".equals(judgeCurrency)) {
				  list.add(0,"0");
				  list.add(1,"���ִ���Ϊ�գ���ά�� ");
			  }			
			  return list;
		  }
		  
		  /**
			 * �����ֶ��Ƿ�Ϊ��
			 * ���۷�Ʊ��
			 *  wy
			 * 2019/10/29
			 */
		 public List judgeNXFPB(DJZBHeaderVO hvo,DJZBItemVO[] bvo,String nyr,String djbh,Map custcode,String judgeCurrency,String jsfs,String queryLRR,int t){
			  List<String> list = new ArrayList<String>();
			  Map kpTaxCode = kpTaxCode(hvo.getDwbm());
			  Map BodyMap = selectCubasdoc(djbh);
			  String chCode = selectInvl(hvo); //�������code
			  Map queryInv = QueryInv(chCode);
			  Map queryHT = queryHT(chCode); //��ͬ
			  String nx = "���۷�Ʊ��������";
			  list.add(0,"1");
			  if(kpTaxCode.get("def8")==null||"".equals(kpTaxCode.get("def8"))||kpTaxCode.get("def8").toString().length()<=0){
				  list.add(0,"0");
				  list.add(1,"��˾��(���״���)Ϊ�գ���ά��");
				  return list;
			  }else if(queryzrzx(bvo[0].getDeptid())== null ||"".equals(queryzrzx(bvo[0].getDeptid()))){
				  list.add(0,"0");
				  list.add(1,"��������Ϊ�գ���ά�� ");
				  return list;
			  }else if (DJLX(nx)==null||"".equals(DJLX(nx))) {
				  list.add(0,"0");
				  list.add(1,"��������Ϊ�գ���ά�� ");
				  return list;
			  }else if (hvo.getDjbh()==null ||"".equals(hvo.getDjbh())) {
				  list.add(0,"0");
				  list.add(1,"ҵ�񵥾ݺ�Ϊ�գ���ά�� ");
				  return list;
			  }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17())) {
				  list.add(0,"0");
				  list.add(1,"ֽ�ʷ�Ʊ��Ϊ�գ���ά�� ");
				  return list;
			  }else if (BodyMap.get("ccreditnum")== null||"".equals(BodyMap.get("ccreditnum"))) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ����Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[t].getDdh()==null||"".equals(bvo[t].getDdh())) {
				  list.add(0,"0");
				  list.add(1,"������(ҵ�񵥾������)Ϊ�գ���ά�� ");
				  return list;
			  }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				  list.add(0,"0");
				  list.add(1,"����Ʒ����/��������Ϊ�գ���ά�� ");
				  return list;
			  }else if (queryInv.get("invclassname")==null||"".equals(queryInv.get("invclassname"))) {
				  list.add(0,"0");
				  list.add(1,"������������(�������)Ϊ�գ���ά�� ");
				  return list;			  
			  }else if (jsfs==null||"".equals(jsfs)) {
				  list.add(0,"0");
				  list.add(1,"���㷽ʽΪ�գ���ά�� ");
				  return list;
			  }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				  list.add(0,"0");
				  list.add(1,"ҵ������Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[t].getJfshl()==null||"".equals(bvo[t].getJfshl())) {
				  list.add(0,"0");
				  list.add(1,"������ Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[t].getJfshl()==null||"".equals(bvo[t].getJfshl())) {
				  list.add(0,"0");
				  list.add(1,"��Ʊ����Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[t].getJfybwsje()==null||"".equals(bvo[t].getJfybwsje())) {
				  list.add(0,"0");
				  list.add(1,"����Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[t].getJfybsj()==null||"".equals(bvo[t].getJfybsj())) {
				  list.add(0,"0");
				  list.add(1,"˰��Ϊ�գ���ά�� ");
				  return list;
			  }else if (custcode==null||"".equals(custcode)) {
				  list.add(0,"0");
				  list.add(1,"�����û�����Ϊ�գ���ά�� ");
				  return list;
			  }else if (custcode==null||"".equals(custcode)) {
				  list.add(0,"0");
				  list.add(1,"�����û�����Ϊ�գ���ά�� ");
				  return list;
			  }else if (nyr==null||"".equals(nyr)) {
				  list.add(0,"0");
				  list.add(1,"��������Ϊ�գ���ά�� ");
				  return list;
			  }else if (nyr==null||"".equals(nyr)) {
				  list.add(0,"0");
				  list.add(1,"��������(��������)Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[t].getSl()==null||"".equals(bvo[t].getSl())) {
				  list.add(0,"0");
				  list.add(1,"˰��Ϊ�գ���ά�� ");
				  return list;
			  }
			  return list; 
		  }
		 	/**
			 * �����ֶ��Ƿ�Ϊ��
			 * ���۷�Ʊ����Ʊ��ϸ��
			 *  wy
		     * 2019/10/29
			 */
		 public List judgeNXFPMX(DJZBHeaderVO hvo,DJZBItemVO[] bvo,String nyr,String djbh,Map custcode,String judgeCurrency,String jsfs,String queryLRR,int i){
			  List<String> list = new ArrayList<String>();
			  Map kpTaxCode = kpTaxCode(hvo.getDwbm());
			  Map BodyMap = selectCubasdoc(djbh);
			  String chCode = selectInvl(hvo); //�������code
			  Map queryInv = QueryInv(chCode);
			  Map queryHT = queryHT(chCode); //��ͬ
			  String queryZjldw = queryZjldw((String)queryInv.get("pk_measdoc"));
			  String nx = "���۷�Ʊ��������";
			  list.add(0,"1");
			  if(kpTaxCode.get("def8")==null||"".equals(kpTaxCode.get("def8"))||kpTaxCode.get("def8").toString().length()<=0){
				  list.add(0,"0");
				  list.add(1,"��˾�� �����״��룩Ϊ�գ���ά��");
				  return list;
			  }else if(queryzrzx(bvo[0].getDeptid())== null ||"".equals(queryzrzx(bvo[0].getDeptid()))){
				  list.add(0,"0");
				  list.add(1,"��������Ϊ�գ���ά�� ");
				  return list;
			  }else if (DJLX(nx)==null||"".equals(DJLX(nx))) {
				  list.add(0,"0");
				  list.add(1,"��������Ϊ�գ���ά�� ");
				  return list;
			  }else if (hvo.getDjbh()==null ||"".equals(hvo.getDjbh())) {
				  list.add(0,"0");
				  list.add(1,"ҵ�񵥾ݺ�Ϊ�գ���ά�� ");
				  return list;
			  }else if (queryInv.get("invclassname")==null||"".equals(queryInv.get("invclassname"))) {
				  list.add(0,"0");
				  list.add(1,"Ʒ��(��������)Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				  list.add(0,"0");
				  list.add(1,"����Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[i].getDj()==null||"".equals(bvo[i].getDj())) {
				  list.add(0,"0");
				  list.add(1,"����Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[i].getJfybwsje()==null||"".equals(bvo[i].getJfybwsje())) {
				  list.add(0,"0");
				  list.add(1," ���������˰��Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				  list.add(0,"0");
				  list.add(1," ˰��Ϊ�գ���ά�� ");
				  return list;
			  }else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				  list.add(0,"0");
				  list.add(1,"��˰���Ϊ�գ���ά�� ");
				  return list;
			  }else if (queryZjldw==null||"".equals(queryZjldw)) {
				  list.add(0,"0");
				  list.add(1," ��λΪ�գ���ά�� ");
				  return list;
			  }else if (bvo[i].getSl() == null||"".equals(bvo[i].getSl())) {
				  list.add(0,"0");
				  list.add(1,"˰��Ϊ�գ���ά�� ");
				  return list;
			  }
			 return list;			 
		 }
		 
		 /**
		  * У��������Ʊͷ
		  *  wy
		  * 2019/10/29
		  */
		 public List judgeWXPFT(DJZBHeaderVO hvo,DJZBItemVO[] bvo,Map kpTaxCode,String chCode,Map queryInv,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble jfshl,UFDouble Jfybsj,UFDouble zje,UFDouble Jfybje){
			 List<String> list = new ArrayList<String>();
			 String wx = "���۷�Ʊ��������";
			 list.add(0,"1");
			 if(DJLX(wx)==null||"".equals(DJLX(wx))){
				 list.add(0,"0");
				 list.add(1,"��������Ϊ�գ���ά��");
				 return list;
			 }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17()==null)) {
				 list.add(0,"0");
				 list.add(1,"������Ʊ��Ϊ�գ���ά��");
				 return list;
			 }else if (kpTaxCode.get("def8") ==null||"".equals(kpTaxCode.get("def8")==null)) {
				 list.add(0,"0");
				 list.add(1,"����Ϊ�գ���ά��");
				 return list;
			 }else if (queryzrzx(bvo[0].getDeptid())==null||"".equals(queryzrzx(bvo[0].getDeptid()))) {
				 list.add(0,"0");
				 list.add(1,"��������Ϊ�գ���ά��");
				 return list;
			 }else if (custcode==null||"".equals(custcode)) {
				 list.add(0,"0");
				 list.add(1,"�����û�����--���̴���Ϊ�գ���ά��");
				 return list;
			 }else if (jsfs==null|"".equals(jsfs)) {
				 list.add(0,"0");
				 list.add(1,"���㷽ʽΪ�գ���ά��");
				 return list;
			 }else if (judgeCurrency==null|"".equals(judgeCurrency)) {
				 list.add(0,"0");
				 list.add(1,"���ִ���Ϊ�գ���ά��");
				 return list;
			 }else if (queryInv.get("invclasscode")==null|"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"Ʒ������-����������Ϊ�գ���ά��");
				 return list;
			 }else if (queryInv.get("invclassname")==null|"".equals(queryInv.get("invclassname"))) {
				 list.add(0,"0");
				 list.add(1,"Ʒ����������-�����������Ϊ�գ���ά��");
				 return list;
			 }else if (hvo.getDjrq()==null|"".equals(hvo.getDjrq())) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ���� Ϊ�գ���ά��");
				 return list;
			 }else if (zje==null|"".equals(zje)) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ���Ϊ�գ���ά��");
				 return list;
			 }else if (Jfybje==null|"".equals(Jfybje)) {
				 list.add(0,"0");
				 list.add(1,"���Ϊ�գ���ά��");
				 return list;
			 }else if (jfshl==null|"".equals(jfshl)) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ���� --��Ʊ����Ϊ�գ���ά��");
				 return list;
			 }else if (Jfybsj==null|"".equals(Jfybsj)) {
				 list.add(0,"0");
				 list.add(1,"˰��Ϊ�գ���ά��");
				 return list;
			 }else if (queryInv.get("invclasscode")==null|"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"����Ʒ����/��������--�������Ϊ�գ���ά��");
				 return list;
			 }
			return list;
		 }
		 /**
		  * У��������Ʊ��ϸ
		  *  wy
		  * 2019/10/29
		  */
		 @SuppressWarnings("unchecked")
		public List judgeWXFPB(DJZBHeaderVO hvo,DJZBItemVO[] bvo,int i,Map kpTaxCode,String chCode,Map queryInv,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble zje,Map BodyMap){
			 List<String> list = new ArrayList<String>();
			 String wx = "���۷�Ʊ��������";
			 Map queryHT = queryHT(chCode); //��ͬ
			 list.add(0,"1");
			 if(DJLX(wx)==null||"".equals(DJLX(wx))){
				 list.add(0,"0");
				 list.add(1,"��������Ϊ�գ���ά��");
				 return list;
			 }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17()==null)) {
				 list.add(0,"0");
				 list.add(1,"����ֽ�ʷ�Ʊ��Ϊ�գ���ά��");
				 return list;
			 }else if (kpTaxCode.get("def8") ==null||"".equals(kpTaxCode.get("def8")==null)) {
				 list.add(0,"0");
				 list.add(1,"����Ϊ�գ���ά��");
				 return list;
			 }else if (queryzrzx(bvo[0].getDeptid())==null||"".equals(queryzrzx(bvo[0].getDeptid()))) {
				 list.add(0,"0");
				 list.add(1,"��������Ϊ�գ���ά��");
				 return list;
			 }else if (queryHT.get("ct_code")==null||"".equals(queryHT.get("ct_code"))) {
				 list.add(0,"0");
				 list.add(1,"��ͬ��Ϊ�գ���ά��");
				 return list;
			 }else if (queryInv.get("memo")==null||"".equals(queryInv.get("memo"))) {
				 list.add(0,"0");
				 list.add(1,"���ر���Ϊ�գ���ά��");
				 return list;
			 }else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ���������أ�Ϊ�գ���ά��");
				 return list;
			 }else if ( bvo[i].getHsdj()==null||"".equals(bvo[i].getHsdj())) {
				 list.add(0,"0");
				 list.add(1,"���۵���Ϊ�գ���ά��");
				 return list;
			 }else if (zje==null||"".equals(zje)) {
				 list.add(0,"0");
				 list.add(1,"ȥӶ���--ͬ���Ϊ�գ���ά��");
				 return list;
			 }else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				 list.add(0,"0");
				 list.add(1,"˰��Ϊ�գ���ά��");
				 return list;
			 }else if (queryLRR==null||"".equals(queryLRR)) {
				 list.add(0,"0");
				 list.add(1,"¼����--�Ƶ�������Ϊ�գ���ά��");
				 return list;
			 }else if(person(queryLRR,hvo.getDwbm())==null||"".equals(person(queryLRR,hvo.getDwbm()))){
				 list.add(0,"0");
				 list.add(1,"¼���˹���--�Ƶ��˹���Ϊ�գ���ά��");
				 return list;
			 }else if (BodyMap.get("dmakedate")==null||"".equals(BodyMap.get("dmakedate"))) {
				 list.add(0,"0");
				 list.add(1,"¼������--�Ƶ�����Ϊ�գ���ά��");
				 return list;
			 } 
			return list;
		 }
		 
		 /**
		  * У��������Ʊ���ز����ϸ��Ϣ�ӿ�
		  *  wy
		  * 2019/10/29
		  */
		 public List judgeWXHG(DJZBHeaderVO hvo,DJZBItemVO[] bvo,int i,Map kpTaxCode,String chCode,Map queryInv,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble zje,Map BodyMap){
			 List<String> list = new ArrayList<String>();
			 String wx = "���۷�Ʊ��������";
			 Map queryHT = queryHT(chCode); //��ͬ
			 list.add(0,"1");
			 if(DJLX(wx)==null||"".equals(DJLX(wx))){
				 list.add(0,"0");
				 list.add(1,"��������Ϊ�գ���ά��");
				 return list;
			 }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17()==null)) {
				 list.add(0,"0");
				 list.add(1,"����ֽ�ʷ�Ʊ��Ϊ�գ���ά��");
				 return list;
			 }else if (kpTaxCode.get("def8") ==null||"".equals(kpTaxCode.get("def8")==null)) {
				 list.add(0,"0");
				 list.add(1,"����Ϊ�գ���ά��");
				 return list;
			 }else if (queryzrzx(bvo[0].getDeptid())==null||"".equals(queryzrzx(bvo[0].getDeptid()))) {
				 list.add(0,"0");
				 list.add(1,"��������Ϊ�գ���ά��");
				 return list;
			 }else if (queryInv.get("memo")==null||"".equals(queryInv.get("memo"))) {
				 list.add(0,"0");
				 list.add(1,"������Ʒ����Ϊ�գ���ά��");
				 return list;
			}else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ����--����Ϊ�գ���ά��");
				 return list;
			}else if (bvo[i].getHsdj()==null||"".equals(bvo[i].getHsdj())) {
				 list.add(0,"0");
				 list.add(1,"���۵���Ϊ�գ���ά��");
				 return list;
			}else if (bvo[i].getJfybwsje()==null||"".equals(bvo[i].getJfybwsje())) {
				 list.add(0,"0");
				 list.add(1,"��Ӷ���Ϊ�գ���ά��");
				 return list;
			}else if (zje==null||"".equals(zje)) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ���Ϊ�գ���ά��");
				 return list;
			}else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				 list.add(0,"0");
				 list.add(1,"˰��Ϊ�գ���ά��");
				 return list;
			}else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17())) {
				 list.add(0,"0");
				 list.add(1,"���Ʒ�Ʊ��Ϊ�գ���ά��");
				 return list;
			}else if (BodyMap.get("ccreditnum")==null||"".equals(BodyMap.get("ccreditnum"))) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ����Ϊ�գ���ά��");
				 return list;
			}else if (hvo.getDjrq()==null||"".equals(hvo.getDjrq())) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ����Ϊ�գ���ά��");
				 return list;
			}
			return list;
		 }
		 
		 /**
		  * У������������Ϣ�ӿ� 	
		  *  wy
		  * 2019/10/29	
		  */
		 public List judgeWXSD(DJZBHeaderVO hvo,DJZBItemVO[] bvo,int i,String djbh,Map kpTaxCode,String chCode,Map queryInv,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble zje,String jsfs1){
			 List<String> list = new ArrayList<String>();
			 String wx = "���۷�Ʊ��������";
			 Map BodyMap = selectCubasdoc(djbh);
			 Map queryHT = queryHT(chCode); //��ͬ
			 list.add(0,"1");
			 if (kpTaxCode.get("def8") ==null||"".equals(kpTaxCode.get("def8")==null)) {
				 list.add(0,"0");
				 list.add(1,"����Ϊ�գ���ά��");
				 return list;
			 }else if (queryzrzx(bvo[0].getDeptid())==null||"".equals(queryzrzx(bvo[0].getDeptid()))) {
				 list.add(0,"0");
				 list.add(1,"��������Ϊ�գ���ά��");
				 return list;
			 }else if(DJLX(wx)==null||"".equals(DJLX(wx))){
				 list.add(0,"0");
				 list.add(1,"��������Ϊ�գ���ά��");
				 return list;
			 }else if (bvo[i].getDdh()==null||"".equals(bvo[i].getDdh())) {
				 list.add(0,"0");
				 list.add(1,"������Ϊ�գ���ά��");
				 return list;
			 }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17())) {
				 list.add(0,"0");
				 list.add(1,"����ֽ�ʷ�Ʊ��Ϊ�գ���ά��");
				 return list;
			 }else if (queryHT.get("ct_code")==null||"".equals(queryHT.get("ct_code"))) {
				 list.add(0,"0");
				 list.add(1,"��ͬ��Ϊ�գ���ά��");
				 return list;
			 }else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				 list.add(0,"0");
				 list.add(1,"������Ϊ�գ���ά��");
				 return list;
			 }else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ����Ϊ�գ���ά��");
				 return list;
			 }else if (bvo[i].getDj()==null||"".equals(bvo[i].getDj())) {
				 list.add(0,"0");
				 list.add(1,"����Ϊ�գ���ά��");
				 return list;
			 }else if (queryZjldw((String)queryInv.get("pk_measdoc"))==null||"".equals(queryZjldw((String)queryInv.get("pk_measdoc")))) {
				 list.add(0,"0");
				 list.add(1,"��Ʊ������λΪ�գ���ά��");
				 return list;
			 }else if (bvo[i].getJfybwsje()==null||"".equals(bvo[i].getJfybwsje())) {
				 list.add(0,"0");
				 list.add(1,"����Ϊ�գ���ά��");
				 return list;
			 }else if (bvo[i].getSl()==null||"".equals(bvo[i].getSl())) {
				 list.add(0,"0");
				 list.add(1,"˰��Ϊ�գ���ά��");
				 return list;
			 }else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				 list.add(0,"0");
				 list.add(1,"˰��Ϊ�գ���ά��");
				 return list;
			 }else if (jsfs1==null||"".equals(jsfs1)) {
				 list.add(0,"0");
				 list.add(1,"���㷽ʽΪ�գ���ά��");
				 return list;
			 }else if (custcode==null||"".equals(custcode)) {
				 list.add(0,"0");
				 list.add(1,"�����û�����Ϊ�գ���ά��");
				 return list;
			 }else if (custcode==null||"".equals(custcode)) {
				 list.add(0,"0");
				 list.add(1,"�����û�����   Ϊ�գ���ά��");
				 return list;
			 }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"����Ʒ���롢��������Ϊ�գ���ά��");
				 return list;
			 }else if (BodyMap.get("djkjnd")==null||"".equals(BodyMap.get("djkjnd"))||BodyMap.get("djkjqj")==null||"".equals(BodyMap.get("djkjqj"))) {
				 list.add(0,"0");
				 list.add(1,"�����Ϊ�գ���ά��");
				 return list;
			 }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"Ʒ��Ϊ�գ���ά��");
				 return list;
			 }else if (queryInv.get("memo")==null||"".equals(queryInv.get("memo"))) {
				 list.add(0,"0");
				 list.add(1,"������Ʒ����Ϊ�գ���ά��");
				 return list;
			 }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"��ͬ���� ����ҵ�����ͣ�Ϊ�գ���ά��");
				 return list;
			 }
			return list;			 
		 }
		 public StringBuffer checkListval(List list){
			 StringBuffer str = new StringBuffer();
			 List lists = new ArrayList();
				lists.add(0,"1");
				if(list.get(0).equals("")){
					str.append("����Ϊ��,����д");
//					lists.add(0,"0");
//					lists.add(1,"����Ϊ��,����д");
//					return lists;
				} if(list.get(1).equals(""))
				{
					str.append(" ��ͬ��Ϊ��,����д");
//					lists.add(0,"0");
//					lists.add(1,"��ͬ��Ϊ��,����д");
//					return lists;
				} if(list.get(2).equals("")){
					str.append(" ��ͬ����Ϊ��,����д");
//					lists.add(0,"0");
//					lists.add(1,"��ͬ����Ϊ��,����д");
//					return lists;
				} if(list.get(3).equals("")){
					str.append(",��Ʊ��Ϊ��,����д");
//					lists.add(0,"0");
//					lists.add(1,"��Ʊ��Ϊ��,����д");
//					return lists;
				} if(list.get(4).equals("")){
					str.append(",��Ʊ����Ϊ��,����д");
//					lists.add(0,"0");
//					lists.add(1,"��Ʊ����Ϊ��,����д");
//					return lists;
				}
				return str;			 	 
			
		 }
		 
		 public   int  FPsl(String res){
			 	int  result = 0;
				IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				String value ="select count(DISTINCT iv.vinvoicecode) as sul  from arap_djzb zb left join arap_djfb fb ON zb.vouchid = fb.vouchid left JOIN po_invoice iv ON iv.cinvoiceid = fb.ddlx where zb.djbh ='"+res+"'";
				try {
					List	Bodylist = (List) bs.executeQuery(value.toString(), new MapListProcessor());
					if(Bodylist!=null||Bodylist.size()>0){
					Object BodyMap = ((Map) Bodylist.get(0)).get("sul");
					result = Integer.valueOf(String.valueOf(BodyMap));
					return result;
					}
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result; 
		 }

		 /**
		  * ���ݲ��� �� ��˾���� ����񲿱���
		  * wy
		  * 2019/11/14
		  */
		 public String financeCode(String pk_corp,String queryzrzx ){
			 System.out.println("pk_corp="+pk_corp+"   "+"queryzrzx=="+queryzrzx);
			if (pk_corp == null) {
				return "";
			}
			String deptcode = "";
			String sql = "select def2 from bd_deptdoc where  pk_corp = '"+pk_corp+"' and deptcode = '"+queryzrzx+"' and nvl(dr,0)=0";
			IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		      try {
		         deptcode = (String) receiving.executeQuery(sql, new ColumnProcessor());
		      } catch (BusinessException e) { 
		    	  e.printStackTrace();
		      }
			 return deptcode;
		 }
		  
		 /**
		  * �����Ƶ������Ƶ��˹���
		  * wy
		  * 2019/11/7
		  */
		public String person(String name,String pk_corp){
			if(name == null){
				return "";
			}
			String psncode = "";
		    String sql = "select psncode from bd_psndoc where psnname = '"+name+"' and pk_corp = '"+pk_corp+"' and nvl(dr,0)=0";
		    IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	        try {
	    	   psncode = (String) receiving.executeQuery(sql, new ColumnProcessor());
	        } catch (BusinessException e) { 
	    	   e.printStackTrace();
	        }
		   return psncode;
		} 	 
		
		/**
		  * ��ѯ�������,�Ƿ�Ԥ������������
		  * zsh
		  * 2019/11/7
		  */
		public List hxmoney(String val){
			List lists = new ArrayList();
			String psncode = "";
		    String sql = "SELECT nvl(po.vdef17,0) as t, nvl(fkd.zfje,0)  je," +
		    		"	nvl(fkd.jhzfrq,0)  rq FROM	arap_djzb zb" +
		    		"	LEFT JOIN arap_djfb fb ON zb.vouchid = fb.vouchid" +
		    		"	left JOIN po_invoice iv ON iv.cinvoiceid = fb.ddlx	LEFT JOIN po_invoice_b ivb ON ivb.cinvoiceid = iv.cinvoiceid" +
		    		"	left JOIN po_order_b pob ON pob.corderid = ivb.csourcebillid	left join po_order po on po.corderid=pob.corderid" +
		    		"	left join arap_fksqd  fkd on po.corderid =fkd.vdef1	left join bd_billtype djlx on djlx.pk_billtypecode  = ivb.csourcebilltype" +
		    		"	 WHERE	zb.djbh = '"+val+"'";
		    IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	        try {
	        	List Bodylists = (List) receiving.executeQuery(sql.toString(), new MapListProcessor());
	        	Map custMaps = new HashMap();
	        	custMaps =(Map) Bodylists.get(0);
	        String t =	(String) custMaps.get("t");
	        String je =	(String) custMaps.get("je");
	        String rq =	(String) custMaps.get("rq");
	        lists.add(0,t);
	        lists.add(1,je);
	        lists.add(2,rq);
	
	        } catch (BusinessException e) { 
	    	   e.printStackTrace();
	        }
			return lists;
			
		}
		 /**
		  * ��������
		  * zsh
		  * 2019/11/7
		  */
		public String djlx(String val){
			if(val == null){
				return "";
			}
			String psncode = "";
		    String sql = "select djlx.billtypename   from arap_djzb zb  LEFT JOIN arap_djfb fb ON zb.vouchid = fb.vouchid " +
		    		" 	left JOIN po_invoice iv ON iv.cinvoiceid = fb.ddlx" +
		    		"	LEFT JOIN po_invoice_b ivb ON ivb.cinvoiceid = iv.cinvoiceid " +
		    		"	LEFT JOIN bd_billtype djlx on djlx.pk_billtypecode  = ivb.csourcebilltype " +
		    		"   where zb.djbh = '"+val+"' ";
		    IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	        try {
	    	   psncode = (String) receiving.executeQuery(sql, new ColumnProcessor());
	        } catch (BusinessException e) { 
	    	   e.printStackTrace();
	        }
		   return psncode;
		} 
		 /**
		  * fpzl
		  * zsh
		  * 2019/11/7
		  */
		public String fpzl(String val){
			if(val == null){
				return "";
			}
			String psncode = "";
		    String sql = "select iv.vdef14   from arap_djzb zb  LEFT JOIN arap_djfb fb ON zb.vouchid = fb.vouchid " +
		    		" 	left JOIN po_invoice iv ON iv.cinvoiceid = fb.ddlx" +
		    		"	LEFT JOIN po_invoice_b ivb ON ivb.cinvoiceid = iv.cinvoiceid " +
		    		"	LEFT JOIN bd_billtype djlx on djlx.pk_billtypecode  = ivb.csourcebilltype " +
		    		"   where zb.djbh = '"+val+"' ";
		    IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	        try {
	    	   psncode = (String) receiving.executeQuery(sql, new ColumnProcessor());
	        } catch (BusinessException e) { 
	    	   e.printStackTrace();
	        }
		   return psncode;
		} 
		
		//add by zwx 2019-11-18 CVM����ֽ�ʷ�Ʊ���롢��������
		public HashMap getFpInfo(String vouchid){
			if(vouchid==null||vouchid.length()==0){
				return null;
			}
			StringBuffer sql = new StringBuffer();
			sql.append(" select so.ccreditnum as invoicecode,dept.def5 as costcenter from arap_djzb zb ") 
			.append(" inner join arap_djfb fb ") 
			.append(" on fb.vouchid = zb.vouchid ") 
			.append(" inner join so_saleinvoice so ") 
			.append(" on so.csaleid = fb.ddlx ") 
			.append(" inner join bd_deptdoc dept ") 
			.append(" on dept.pk_deptdoc = so.cdeptid ") 
			.append(" where zb.vouchid = '"+vouchid+"' ") 
			.append(" and nvl(fb.dr,0) = 0 ") 
			.append(" and nvl(so.dr,0) = 0 ") 
			.append(" and nvl(dept.dr,0) = 0 ") ;
			HashMap map = new HashMap();
			 IUAPQueryBS receiving = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	         try {
	        	 map = (HashMap) receiving.executeQuery(sql.toString(), new MapProcessor());
	        	 System.out.println(sql.toString());
	         } catch (BusinessException e) { 
	    	    e.printStackTrace();
	         }
	         return map;
		}
		//end by zwx
}