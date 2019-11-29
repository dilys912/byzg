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

  //面板初始化
  public UiManagerAr20060301(FramePanel panel)
  {
    super(panel);
  }
  
  //数据初始化
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
   * 应收传标财发送XBUS
   * wy
   * 2019年9月10日 10：12
   * @param bo
   */
  @SuppressWarnings("restriction")
	public void sendXbus(ButtonObject bo){
	  String corp = PoPublicUIClass.getLoginPk_corp();
	  IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
	  Boolean result = idetermineService.check(corp);
	  if(result){//判断当前公司是否为国内公司，否则不执行
	  	 IUiPanel iPanel = getCurrentPanel();
		 @SuppressWarnings("unused")
		 DjPanel djPanel = null;//单据面板
		   if ((iPanel instanceof DjPanel))
		    {
		      djPanel = (DjPanel)iPanel;
		    }
		    List<DJZBVO> vos = getSelectedRowVO();//获取选中行数
	        try {
	        	getCurrentPanel().onButtonClicked(bo);
			}catch (Exception e) {
				showErrorMessage(e.getMessage());
				e.printStackTrace();
				return ;
			}			
			StringBuffer str1 = new StringBuffer();	//存未发送成功的单据编号
			StringBuffer str2 = new StringBuffer();	//存是否为审核单据
			StringBuffer str3 = new StringBuffer();	//存已发送成功的单据编号
			StringBuffer str4 = new StringBuffer();	//存缺少字段的单据编号
			//判断是列表界面还是卡片界面
			boolean showing = djPanel.getArapDjPanel1().getBillCardPanelDj().isShowing();
			if(showing == false && vos.size()==0){
				showWarningMessage("列表下未选中要传标财的数据");
				return;							
			}else if(showing == false && vos.size()>0){//如果选中的数据大于零	        	
	        	for (int j = 0; j < vos.size(); j++) {
	        		DJZBHeaderVO hvo = (DJZBHeaderVO)vos.get(j).getParentVO();
	        		String djbh = vos.get(j).getParentVO().getAttributeValue("djbh").toString();
	        		String judgeDJBH = judgeDJBH(djbh);
	        		if(judgeDJBH == null){
	        			DJZBItemVO[] bvo = (DJZBItemVO[])vos.get(j).getChildrenVO();
	    				String judgeCurrency = judgeCurrency(bvo[0].getBzbm());//判断币种
	    				Map custcode = custcode(hvo);//客商编码
    				    String queryLRR = queryLRR(hvo.getLrr());
    				    UFBoolean prepay = hvo.getPrepay();//结算方式 ==预收付标志
	      				String jsfs = String.valueOf(prepay);
	      				if("Y".equals(jsfs)){
	      					jsfs = "D";
	      				}else if ("N".equals(jsfs)) {
	      					jsfs = "C";
	      				}
    				    Integer djzt = hvo.getDjzt();
    				    if(djzt == 2){//单据状态 == 2   已审核
    				    	if ("CNY".endsWith(judgeCurrency)) {
	  							boolean flag = nxfp(hvo,str4,bvo,djbh,custcode,judgeCurrency,jsfs,queryLRR);//销售发票   		
	  							if(flag==true){
	  								str1.append(djbh+",");
	  							}
    				    	}else {
	  							//判断一下海关编码是否有  有则传标财  没有请核对	
	  							Map kpTaxCode = kpTaxCode(hvo.getDwbm());
	  							String chCode = selectInvl(hvo);//存货分类
	  							Map queryInv = QueryInv(chCode);					
	  							if(queryInv.get("memo")==null||queryInv.get("memo") ==""){
	  								str4.append(djbh+"存货分类中备注（海关编码）需要维护");
	  							}else {
	  								boolean wxfp = wxfp(hvo,str4,bvo,kpTaxCode,chCode,queryInv,djbh,custcode,judgeCurrency,jsfs,queryLRR);//外销发票
	  								if (wxfp==true) {
	  									str1.append(djbh+",");
									}
	  							}   
    				    	}      				    			      				  
    				    }else{
    				    	str2.append(djbh+",");//未审核
    				    }     				          				   
	        		}else {
	        			str3.append(djbh+",");//已发送成功
					} 	        		
	        	}
	        	if(str1 != null && str1.length()>0 && str2 != null&&str2.length()>0 && str3 != null&&str3.length()>0 && str4!=null&&str4.length()>0){
				    showWarningMessage(str1+"单据发送成功。"+"\n"+str2+"单据是未审核单据,发送失败。"+"\n"+str3+"单据已发送过。"+"\n"+str4);
	        	}else if (str1 != null && str1.length()>0 && str2 != null&&str2.length()>0 && str3 != null&&str3.length()>0) {
	        		 showWarningMessage(str1+"单据发送成功。"+"\n"+str2+"单据是未审核单据,发送失败。"+"\n"+str3+"单据已发送过。");
				}else if (str1 != null && str1.length()>0 && str2 != null&&str2.length()>0  && str4!=null&&str4.length()>0) {
					showWarningMessage(str1+"单据发送成功。"+"\n"+str2+"单据是未审核单据,发送失败。"+"\n"+str4);
				}else if (str2 != null&&str2.length()>0 && str3 != null&&str3.length()>0 && str4!=null&&str4.length()>0) {
					 showWarningMessage(str2+"单据是未审核单据,发送失败。"+"\n"+str3+"单据已发送过。"+"\n"+str4);
				}else if (str1 != null && str1.length()>0 && str3 != null&&str3.length()>0 && str4!=null&&str4.length()>0) {
					 showWarningMessage(str1+"单据发送成功。"+"\n"+str3+"单据已发送过。"+"\n"+str4);
				}else if (str1 != null && str1.length()>0 && str2 != null && str2.length()>0 ) {
	        		 showWarningMessage(str1+"单据发送成功。"+"\n"+str2+"单据是未审核单据,发送失败。");
				}else if(str1 != null && str1.length()>0  && str3 != null && str3.length()>0){
					 showWarningMessage(str1+"单据发送成功。"+"\n"+str3+"单据已发送过。");
				}else if (str2 != null && str2.length()>0 && str3 != null && str3.length()>0 ) {
					  showWarningMessage(str2+"单据是未审核单据,发送失败。"+"\n"+str3+"单据已发送过。");
	        	}else if (str1 != null && str1.length()>0 && str4!=null&&str4.length()>0) {
					 showWarningMessage(str1+"单据发送成功。"+"\n"+str4);
				} else if (str2 != null && str2.length()>0 && str4!=null&&str4.length()>0) {
					 showWarningMessage(str2+"单据是未审核单据,发送失败。"+"\n"+str4);
				}else if (str3!= null && str3.length()>0 && str4!=null&&str4.length()>0) {
					showWarningMessage(str3+"单据已发送过。"+"\n"+str4);
				}else if(str1 != null && str1.length()>0){
				    showWarningMessage(str1+"单据发送成功"+"。");
				}else if(str2 != null && str2.length()>0 ){
				    showWarningMessage(str2+"单据是未审核单据,发送失败。");
				}else if(str3!= null && str3.length()>0){
					showWarningMessage(str3+"单据已发送过。");
				}else if (str4!=null&&str4.length()>0) {
					showWarningMessage(str4+"。");
				}         	
	        }else if(showing){
	        	//卡片界面
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
	      		if(judgeDJBH == null){//判断单据是否已传过
	      			String judgeCurrency = judgeCurrency(bvo[0].getBzbm());//判断币种
	  				Map custcode = custcode(hvo);//客商编码
	  				String queryLRR = queryLRR(hvo.getLrr());
	  				UFBoolean prepay = hvo.getPrepay();//结算方式 ==预收付标志
	  				String jsfs = String.valueOf(prepay);
	  				if("Y".equals(jsfs)){
	  					jsfs = "D";
	  				}else if ("N".equals(jsfs)) {
	  					jsfs = "C";
	  				}
	  				Integer djzt = hvo.getDjzt();
				    if(djzt == 2){//单据状态 == 2   已审核
		  				if ("CNY".endsWith(judgeCurrency)) {
		  					boolean nxfp = nxfp(hvo,str4,bvo,djbh,custcode,judgeCurrency,jsfs,queryLRR);//销售发票
		  					if(nxfp==true){
		  						showWarningMessage(djbh+"单据发送成功");
		  					}else{
		  						showWarningMessage(str4+"。");
							}
		  				}else {
		  					//判断一下海关编码是否有  有则传标财  没有请核对	
		  					Map kpTaxCode = kpTaxCode(hvo.getDwbm());
		  					String chCode = selectInvl(hvo);//存货分类
		  					Map queryInv = QueryInv(chCode);						
		  					if(queryInv.get("memo")==null||queryInv.get("memo")==""){
		  						showWarningMessage(djbh+"存货分类中备注（海关编码）需要维护");
		  					}else {
		  						boolean wxfp = wxfp(hvo,str4,bvo,kpTaxCode,chCode,queryInv,djbh,custcode,judgeCurrency,jsfs,queryLRR);//外销发票		  						
		  						if(wxfp == true){
		  							showWarningMessage(djbh+"单据发送成功");
		  						}else {
		  							showWarningMessage(str4+"。");
								}
		  					}
		  				}
				    }else {
				    	showWarningMessage(djbh+"单据是未审核单据,发送失败。");	
					}
	      		}else {
	      			showWarningMessage(djbh+"已发送过！");
				}	
			}				  
		}   
  	}
  
  /**
   * 销售发票（内销）
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
	    if("代加工成品销售业务流程".equals(ywlx)){
	    	ywlx = "DX";
		}else {
			ywlx = "ZX";
		}
	  	Map BodyMap = selectCubasdoc(djbh);
	    Map kpTaxCode = kpTaxCode(hvo.getDwbm());
	    String chCode = selectInvl(hvo); //存货分类code
	    Map queryInv = QueryInv(chCode);
	    Map queryHT = queryHT(chCode); //合同
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nyr = sdf.format(date);	//获取当前系统时间	
		String nx = "销售发票（内销）";
		String djlx = DJLX(nx); //单据类型
		String queryZjldw = queryZjldw((String)queryInv.get("pk_measdoc"));
		UFDouble jfshl = new UFDouble();//发票重量	
		for (int i = 0; i < bvo.length; i++) {
			 UFDouble jfsh = bvo[i].getJfshl();
			 jfshl = jfshl.add(jfsh);
		}
		System.out.println("总发票重量="+jfshl);

		//税率转换
		int intValue = bvo[0].getSl().intValue();
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		String sl = df.format((float)intValue/100);//返回的是String类型  	
		
		UFDouble Jfybwsje = new UFDouble(); //发票金额(不含税)= 总货款  
		for (int i = 0; i < bvo.length; i++) {
			UFDouble jfybwsje2 = bvo[i].getJfybwsje();
			Jfybwsje = Jfybwsje.add(jfybwsje2);
		}
		System.out.println("发票金额(不含税)="+Jfybwsje);
		
		/*bvo[t].getJfybsj()税额 =发票税金额  */
		UFDouble fpsje = new UFDouble();
		for (int i = 0; i < bvo.length; i++) {
			UFDouble jfybsj = bvo[i].getJfybsj();
			fpsje = fpsje.add(jfybsj);
		}
		System.out.println("发票税金额 ="+fpsje);
		
		UFDouble Jfybje = new UFDouble();//发票价税合计金额  (= 总税额  + 总货款 )   
		for (int i = 0; i < bvo.length; i++) {
			UFDouble jfybje2 = bvo[i].getJfybje();
			Jfybje = Jfybje.add(jfybje2);
		}
		
		UFDouble dj = new UFDouble();//发票价格（不含税）
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
		String strh = djbh.substring(djbh.length() -3,djbh.length());   //截取
		
		//销售发票头
		JSONObject val = new JSONObject();
			 val.put("COMPANY_ID",(String) kpTaxCode.get("def8"));//公司别                                                     
			 val.put("COST_CENTER",queryzrzx);//责任中心 (业务)     
			 System.out.println("责任中心 (业务)=="+queryzrzx(bvo[0].getDeptid()));
			 val.put("BILL_TYPE",djlx);//单据类型                        
			 val.put("BILL_NO", hvo.getDjbh()==null?null:hvo.getDjbh());//业务单据号           应收单号        		  
			 val.put("INVOICE_NO", hvo.getZyx17()); //发票号      
			 val.put("INVOICE_CODE",BodyMap.get("ccreditnum").toString().substring(0, 12));//发票代码                                    
			 val.put("INVOICE_SORT",fplx(hvo.getZyx28()));//发票种类                                                       
			 val.put("USER_TYPE", "K");//结算用户类型        
			 val.put("SETTLE_USER_CODE", custcode.get("custcode").toString().substring(0, 6));//结算用户代码                               
			 val.put("FIN_USER_CODE", custcode.get("custcode").toString().substring(0, 6));//最终用户代码                                       
			 val.put("FINA_COST_CENTER",financeCode(hvo.getDwbm(),queryzrzx));//财务责任中心              部门   
			 System.out.println("财务责任中心=="+financeCode(hvo.getDwbm(),queryzrzx)); 
			 val.put("INVOICE_WT",jfshl==null?null:String.format("%.6f",Double.valueOf(jfshl.toString())));//发票重量           发票数量                       
			 val.put("INVOICE_AMT",Jfybwsje == null?null:String.format("%.2f",Double.valueOf(Jfybwsje.toString())));//发票金额(不含税)  
			 val.put("INVOICE_PRICE",dj==null?null:String.format("%.2f",Double.valueOf(dj.toString())));//发票价格（不含税）			 
			 val.put("INVOICE_TAX_AMT",fpsje==null?null:String.format("%.2f",Double.valueOf(fpsje.toString())));//发票税金额 
			 val.put("INVOICE_SUM_AMT",Jfybje==null?null:String.format("%.2f",Double.valueOf(Jfybje.toString())));//发票价税合计金额     
			 val.put("TAX_RATE",sl==null?null:String.format("%.2f",Double.valueOf(sl.toString())));//税率  
			 System.out.println("表头税率1= "+sl);
			 val.put("PAPER_DREW_DATE", hvo.getDjrq()==null?null:hvo.getDjrq().toString().replaceAll("-", ""));//开票日期 
			 val.put("PAPER_DREW_NAME",queryLRR);//开票人    制单人
			 val.put("END_DREW_DATE",hvo.getDjrq()==null?null:hvo.getDjrq().toString().replaceAll("-", ""));//截止开票日期       	
			 val.put("DREW_DEPT_NAME",(String) kpTaxCode.get("unitname"));//开票单位名称     
			 val.put("DREW_DEPT_TAX_NO",(String) kpTaxCode.get("taxcode"));//开票单位税号       
			 val.put("DREW_ADDR_TEL",null);//开票地址电话
			 val.put("DREW_BANK_NAME",hvo.getSkyhmc()==null?null:hvo.getSkyhmc());//开票银行名称   	
			 val.put("DREW_BANK_ACCT",hvo.getSkyhzh()==null?null:hvo.getSkyhzh());//开票银行帐号    				
			 val.put("PUR_TAX_NO",custcode.get("taxpayerid"));//购方单位税号 
			 val.put("PUR_ADDR_TEL", null);//购方地址电话  
			 val.put("PUR_BANK_NAME",BodyMap.get("accname"));//购方银行名称  
			 val.put("PUR_ACCT_NO",BodyMap.get("account"));//购方银行帐号   
			 val.put("SETTLE_METHOD",jsfs);//结算方式    	收付款标志prepay	  结算方式  "C"
			 val.put("SALE_MODE","0");//（销售）贸易方式   
			 val.put("SETTLE_ID",person(queryLRR,hvo.getDwbm()));//结算人工号         开票人工号
			 val.put("DESTINE_ACCT_PERIOD",nyr.replaceAll("-", "").substring(0, 6));//预设会计期    当前系统截取6位
			 val.put("VOUCHER_DESC",BodyMap.get("vnote")==null?" ":BodyMap.get("vnote"));//发票摘要     备注  
			 val.put("CURRENCY_CODE",judgeCurrency);//币种代码
			 val.put("AFFIX_NUM",null);//附件张数
			 val.put("CARRIER",null);//承运人
			 val.put("RECIPIENT",null);//接收人
			 val.put("SYSTEM_ID","JC");//系统别
			 val.put("FREIGHT_AMT","0.0");//运费额
			 
		   //销售发票明细信息
			 JSONArray bvals = new JSONArray();			
			 for (int t = 0; t < bvo.length; t++) {
				 int x=(int)(Math.random()*900)+100;
				 List judgeNXFPB = judgeNXFPB(hvo,bvo,nyr,djbh,custcode,judgeCurrency,jsfs,queryLRR,t);
				 if(judgeNXFPB.get(0).equals("0")){
					str.append(djbh+judgeNXFPB.get(1).toString());	
					return flag;
				 }
				 JSONObject bval = new JSONObject();
				 bval.put("COMPANY_ID",(String) kpTaxCode.get("def8"));//公司别
				 bval.put("COST_CENTER",queryzrzx(bvo[t].getDeptid()));//责任中心     部门   deptid
				 bval.put("BILL_TYPE",djlx);//单据类型
				 bval.put("BILL_NO",hvo.getDjbh()==null?null:hvo.getDjbh());//业务单据号     应收单号
				 bval.put("INVOICE_NO",hvo.getZyx17());//发票号    invoiceno	发票号     hvo.getZyx17()==null?"fph123":hvo.getZyx17().toString()
				 bval.put("INVOICE_CODE",BodyMap.get("ccreditnum").toString().substring(0, 12));//发票代码
				 bval.put("FACTORY_SEQ_ID",(String) kpTaxCode.get("def8"));//制造单元组织机构（到厂别）
				 bval.put("THREE_READY_NO",bvo[t].getDdh()+strh+x+(t+1));//三单号(业务单据子项号)   业务单据号（订单号）+行号
				 System.out.println("三单号(业务单据子项号)="+bvo[t].getDdh()+strh+x+(t+1));
				 bval.put("PRODUCT_CODE", queryInv.get("invclasscode")+""+ywlx);//产副品代码/费用类型  "8987"  25012401  queryInv.get("invclasscode")
				 bval.put("PRODUCT_CODE_NAME",queryInv.get("invclassname"));//费用类型名称    存货名称
				 bval.put("CONTRACT_TYPE",queryHT.get("typecode"));//合同类型编码
				 bval.put("CONTRACT_TYPE_NAME",queryHT.get("typename"));//合同类型名称
				 bval.put("BALANCE_DUE","");//尾款标志     
				 bval.put("SETTLE_METHOD",jsfs);//结算方式
				 bval.put("CONTRACT_NO","");//合约号
				 bval.put("PROJECT_NO","");//项目号
				 bval.put("PROJECT_NAME","");//项目名称
				 bval.put("ORDER_NO","");//合同号
				 bval.put("BUSY_TYPE",queryInv.get("invclasscode").toString().substring(0, 4));//业务类型 
				 bval.put("THREE_READY_WT",bvo[t].getJfshl());//三单量  
				 System.out.println("三单量  ="+bvo[t].getJfshl());  
				 bval.put("DREW_QUANTITY",bvo[t].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[t].getJfshl().toString())));//开票数量
				 bval.put("DREW_MEASURE_UNIT","0");//开票计量单位
				 bval.put("GOOD_AMT",bvo[t].getJfybwsje()==null?null:String.format("%.2f",Double.valueOf(bvo[t].getJfybwsje().toString())));//货款
				 System.out.println("货款1=="+bvo[t].getJfybwsje());
				 bval.put("TAX_AMT",bvo[t].getJfybsj()==null?null:String.format("%.2f",Double.valueOf(bvo[t].getJfybsj().toString())));//税额 
				 System.out.println("税额1=="+bvo[t].getJfybsj());
				 bval.put("SETTLE_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//结算用户代码
				 bval.put("FIN_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//最终用户代码
				 bval.put("UPLOAD_DATE",nyr.replace("-", "").replace(" ", "").replace(":", ""));//上抛日期                                                 系统日期
				 bval.put("THREE_READY_DATE",nyr.replace("-", "").replace(" ", "").replace(":", ""));//结齐日期(入帐日期)    系统日期
				 int intValue1 = bvo[t].getSl().intValue();
				 DecimalFormat df1 = new DecimalFormat("0.00");//格式化小数  
				 String sl1 = df1.format((float)intValue1/100);//返回的是String类型  	   
				 bval.put("TAX_RATE",sl1);//税率 
				 System.out.println("表体税率= "+sl1);
				 bval.put("SHOPSIGN","");//牌号
				 bval.put("MODEL",bvo[t].getGgxh()==null?" ":bvo[t].getGgxh());//规格
				 bval.put("SALE_UNIT_PRICE", bvo[t].getHsdj()==null?null:String.format("%.2f",Double.valueOf( bvo[t].getHsdj().toString())));//销售单价（不含税）
				 bval.put("STATUS","");//状态
				 bval.put("SYSTEM_ID","JC");//系统别 
				 bval.put("FREIGHT_AMT","0.0");//运费额
				 bval.put("PROD_CNAME","");//品名中文名称      类型为N(数值)
				 bval.put("THREE_QUANTITY","");//三单运费行数
				 bval.put("IMAGE_URL","");//影像调阅地址
				 bvals.add(bval);
			 }		       
			 val.put("bodylist",bvals );
			 //开票信息
			 JSONArray bval = new JSONArray();				 
			 for (int i = 0; i < bvo.length; i++) {  
				 List judgeNXFPMX = judgeNXFPMX(hvo, bvo, nyr, djbh, custcode, judgeCurrency, jsfs, queryLRR, i);
				 if(judgeNXFPMX.get(0).equals("0")){
					 str.append(djbh+judgeNXFPMX.get(1).toString());	
					return flag;
				 }
				 JSONObject bva = new JSONObject();
 	        		bva.put("COMPANY_ID",(String) kpTaxCode.get("def8"));//公司别
 	        		bva.put("COST_CENTER",queryzrzx(bvo[i].getDeptid()));//责任中心
 	        		System.out.println("责任中心3=="+queryzrzx(bvo[i].getDeptid()));
 	        		bva.put("BILL_TYPE",djlx);//单据类型
 	        		bva.put("BILL_NO",hvo.getDjbh()==null?null:hvo.getDjbh());//业务单据号
 	        		bva.put("SERIAL_NO",i+1);//序号
 	        		bva.put("PRODUCT_CODE",queryInv.get("invclassname"));//品名       物料名称
 	        		bva.put("DREW_QUANTITY",bvo[i].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//数量 	jfshl
 	        		bva.put("SALE_UNIT_PRICE",bvo[i].getDj()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getDj().toString())));//单价
 	        		bva.put("GOOD_AMT",bvo[i].getJfybwsje()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybwsje().toString())));//货款金额（不含税金额）
 	        		System.out.println("货款=="+bvo[i].getJfybwsje());
 	        		bva.put("TAX_AMT",bvo[i].getJfybsj()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybsj().toString())));//税额
 	        		System.out.println("税额=="+bvo[i].getJfybsj());
 	        		bva.put("SETTLE_AMT",bvo[i].getJfybsj()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybsj().toString())));//含税金额
 	        		bva.put("DREW_MEASURE_UNIT",queryZjldw);//单位  	主计量单位     pk_measdoc
 	        		int intValue2 = bvo[i].getSl().intValue();
 					DecimalFormat df2 = new DecimalFormat("0.00");//格式化小数  
 					String sl3 = df2.format((float)intValue2/100);//返回的是String类型  	   
 	        		bva.put("TAX_RATE",sl3);//税率
 	        		System.out.println("开票明细税率="+sl3);
 	        		bva.put("MODEL",bvo[i].getGgxh()==null?" ":bvo[i].getGgxh()); //规格型号
 	        		bva.put("SYSTEM_ID","JC");//系统别
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
			    	 showWarningMessage(djbh+"发送失败");
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
		         JSONObject jsObject =(JSONObject)jsonArrays.get(0);//销售发票表头
		         JSONArray jArray = new JSONArray();
		         for (int i = 1; i < jsonArrays.size(); i++) {
		        	 JSONObject jsObject1 = (JSONObject)jsonArrays.get(i);  
		        	 jArray.add(jsObject1); //销售发票明细信息	
				 }
		         JSONArray jArrayHead = new JSONArray();
		         jArrayHead.add(jsObject);		         
		         boolean send1 = send(bodyarr, djbh);//开票明细  排序1
		         boolean send2 = send(jArray, djbh);//2
		         boolean send3 = send(jArrayHead, djbh);//3
		         if(send1 == true && send2 == true && send3 == true){
		        	 updateDJBH(djbh,nyr);	
		        	 flag = true;
		         }else {
		        	 showWarningMessage(djbh+"发送失败");
			    	 flag = false;
				 }		             
		     }else{
		    	 showWarningMessage(djbh+"发送失败");
		    	 flag = false;
		     }
			return flag;
  		}
  /**
   * 内销发送传标财
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
   * 销售发票（外销）
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
	  if("成品销售业务流程".equals(ywlx)){
		ywlx = "ZX";
	  }else if("代加工成品销售业务流程".equals(ywlx)){
		ywlx = "DX";
	  }
	  UFBoolean prepay = hvo.getPrepay();//结算方式 ==预收付标志
	  String jsfs1 = String.valueOf(prepay);
	  if("Y".equals(jsfs1)){
		  jsfs1 = "6";
	  }else if ("N".equals(jsfs1)) {
		  jsfs1 = "1";
	  }	
	  Date date = new Date();
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  String nyr = sdf.format(date);	//获取当前系统时间	
	  
	  String wx = "销售发票（外销）";
	  UFDouble Jfybsj = new UFDouble();//总税额
	  for (int i = 0; i < bvo.length; i++) {
		  UFDouble jfybsj2 = bvo[i].getJfybsj();
		  Jfybsj = Jfybsj.add(jfybsj2);
	  }			  
	  //借方原本无税金额   jfybwsje = 货款       借方原币金额    jfybje =价税合计        借方原币税金   Jfybsj = 税额	  	  
	  UFDouble Jfybwsje = new UFDouble();//总货款 
	  for (int i = 0; i < bvo.length; i++) {
		  UFDouble jfybwsje2 = bvo[i].getJfybwsje();
		  Jfybwsje = Jfybwsje.add(jfybwsje2);
	  }
	  
	  UFDouble zje = new UFDouble();//发票金额（发票总金额）   = 总税额 + 总货款
	  zje = Jfybsj.add(Jfybwsje);		  
	  
	  UFDouble jfshl = new UFDouble();//发票重量
	  for (int i = 0; i < bvo.length; i++) {
		  UFDouble jfsh = bvo[i].getJfshl();
		  jfshl = jfshl.add(jfsh);
	  }	
	  UFDouble Jfybje = new UFDouble();//发票价税合计金额    
	  for (int i = 0; i < bvo.length; i++) {
		  UFDouble jfybje2 = bvo[i].getJfybje();
		  Jfybje = Jfybje.add(jfybje2);
	  }			
	  Map queryHT = queryHT(chCode); //合同
	  List judgeWXPFT = judgeWXPFT( hvo, bvo, kpTaxCode, chCode, queryInv, custcode, judgeCurrency, jsfs, queryLRR, jfshl, Jfybsj, zje, Jfybje);
	  if(judgeWXPFT.get(0).equals("0")){
		  str.append(djbh+judgeWXPFT.get(1).toString());	
			return flag;
	  }	
	 //外销发票头信息接口
	  JSONObject val = new JSONObject();
	    val.put("PROC_FLAG","0");//处理标志
	    val.put("BILL_TYPE",DJLX(wx));//单据类型
	    val.put("F_INVOICE_NO",hvo.getZyx17()==null?null:hvo.getZyx17());//外销发票号
	    val.put("COMPANY_CODE",(String) kpTaxCode.get("def8")==null?"1016":(String) kpTaxCode.get("def8"));//帐套
	    val.put("COST_CENTER",queryzrzx(bvo[0].getDeptid()));//责任中心
	    val.put("CONTRACT_NO",null);//代表合约号  --不传
	    val.put("SETTLE_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//结算用户代码--客商代码
	    val.put("SALE_MODE","0");//（销售）贸易方式
	    val.put("SETTLE_METHOD",jsfs);//结算方式
	    val.put("TRADE_TERMS",null);//价格术语
	    val.put("CURRENCY_CODE",judgeCurrency);//币种代码
	    val.put("LC_SERIAL_NO",null);//信用证流水号--不传
	    val.put("LC_NO",null);//信用证号--不传
	    val.put("PROD_CODE", queryInv.get("invclasscode").toString().substring(0, 2));//品名代码--存货分类编码
	    val.put("PLATE_OR_COIL","");//板卷类型
	    val.put("PROD_CNAME",queryInv.get("invclassname"));//品名中文名称--存货分类名称
	    val.put("PROD_ENAME","");//品名英文名称
	    val.put("PAPER_DREW_DATE", hvo.getDjrq()==null?null:hvo.getDjrq().toString().replaceAll("-", ""));//开票日期 
	    val.put("INVOICE_AMT",zje == null?null:String.format("%.2f",Double.valueOf(zje.toString())));//发票金额
	    val.put("FOB_AMT",Jfybje==null?null:String.format("%.2f",Double.valueOf(Jfybje.toString())));//金额
	    val.put("FREIGHT_AMT",null);//运费额--不传
	    val.put("INSU_AMT",null);//保费金额--不传
	    val.put("INVOICE_WT",jfshl==null?null:String.format("%.6f",Double.valueOf(jfshl.toString())));//发票重量 --发票数量
	    val.put("TAX_AMT",Jfybsj==null?null:String.format("%.2f",Double.valueOf(Jfybsj.toString())));//税额
	    val.put("NATION_CODE",null);//国别代码--不传
	    val.put("CAV_CODE",null);//出口核销单号--不传
	    val.put("CUSTOM_NO",null);//报关单号--不传
	    val.put("EXPORT_DATE",null);//出口日期--不传
	    val.put("CUSTOM_DECLARE_DATE",null);//报关申报日期--不传
	    val.put("CUSTOM_CHECKED_DATE",null);//海关验迄日期--不传
	    val.put("CUSTOM_BACK_DATE",null);//报关返回日期--不传
	    val.put("CUSTOM_INPUT_DATE",null);//报关单输入日期--不传
	    val.put("CUSTOM_INPUT_ID",null);//报关单输入人--不传
	    val.put("CUSTOM_INPUT_NAME",null);//报关单输入人姓名--不传
	    val.put("SHIP_LOT_NO",null);//船批号--不传
	    val.put("DATE_PORT_LEAVE",null);//离港日期--不传
	    val.put("T_PORT_LEAVE",null);//离港时间--不传
	    val.put("LOADING_PORT_NAME",null);//发货港名称--不传
	    val.put("PROFIT_NUM",null);//受票人--不传
	    val.put("PROFIT_SITE",null);//受票人地址--不传
	    val.put("BILL_OF_LADING_NO",null);//提货单号--不传
	    val.put("GET_BILL_DATE",null);//提单日期--不传
	    val.put("MAIL_DATE",null);//寄单日期--不传
	    val.put("NEG_BILL_GATH_ID",null);//议付单据集齐人工号--不传
	    val.put("NEG_BILL_GATH_NAME",null);//议付单据集齐人姓名--不传
	    val.put("NEG_BILL_GATH_DATE",null);//议付单据集齐日期--不传
	    val.put("NEG_BILL_GATH_TIME",null);//议付 单据集齐时间--不传
	    val.put("AS_BUSI_MAIN_CODE",null);//应收业务大类代码--不传
	    val.put("AS_BUSI_DETL_CODE",null);//应收业务细类代码--不传
	    val.put("SYSTEM_ID","JC");//系统别
	    val.put("CONTRACT_TYPE",null);//合同类型--不传
	    val.put("PRODUCT_CODE",queryInv.get("invclasscode")+""+ywlx);//产副品代码/费用类型--存货分类68
	    val.put("PROC_STATUS",null);//处理状态--不传
	    val.put("PROC_MASSAGE",null);//处理说明--不传
	    val.put("CREATE_NAME",null);//录入人--不传
	    val.put("CREATE_ID",null);//录入人工号--不传
	    val.put("CREATE_DATE",null);//录入日期--不传
	    val.put("MODIFY_NAME",null);//修改人--不传
	    val.put("MODIFY_ID",null);//修改人工号--不传
	    val.put("MODIFY_NAME",null);//修改日期--不传
		//外销发票明细信息接口
	    JSONArray bvals = new JSONArray();				    
	    for (int i = 0; i < bvo.length; i++) {
	    	List judgeWXFPB = judgeWXFPB(hvo,bvo,i,kpTaxCode,chCode,queryInv,custcode,judgeCurrency, jsfs, queryLRR, zje,BodyMap);
	    	if(judgeWXFPB.get(0).equals("0")){
	    		str.append(djbh+judgeWXFPB.get(1).toString());	
				return flag;
			}	
	    	JSONObject val2 = new JSONObject();
	    	val2.put("PROC_FLAG","0");//处理标志
	    	val2.put("BILL_TYPE",DJLX(wx));//单据类型
	    	val2.put("F_INVOICE_NO",hvo.getZyx17()==null?null:hvo.getZyx17());//外销发票号
	    	val2.put("COMPANY_CODE",(String) kpTaxCode.get("def8")==null?"1016":(String) kpTaxCode.get("def8"));//帐套
	    	val2.put("COST_CENTER",queryzrzx(bvo[i].getDeptid()));//责任中心
	    	val2.put("INVOICE_SEQ_NO",i+1);//发票明细号
	    	val2.put("ORDER_NO",queryHT.get("ct_code").toString().substring(0, 10));//合同号
	    	val2.put("CONTRACT_NO","");//合约号
	    	val2.put("CUSTOMS_PRODUCT_CODE",queryInv.get("memo"));//海关编码--提供默认值  "76129090"  queryInv.get("avgprice")
	    	val2.put("INVOICE_WT",bvo[i].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//发票重量（净重）
	    	val2.put("SALE_PRICE", bvo[i].getHsdj()==null?"0.0":String.format("%.2f",Double.valueOf( bvo[i].getHsdj().toString())));//销售单价
	    	val2.put("INV_AND_COMMIS_AMT",zje==null?null:String.format("%.2f",Double.valueOf(zje.toString())));//含佣金额   不含税金额
	    	val2.put("COMMIS_UNIT","0.0");//单位佣金
	    	val2.put("SETTLE_PRICE",bvo[i].getDj()==null?"0.0":String.format("%.2f",Double.valueOf(bvo[i].getDj().toString())));//去佣单价--同单价
	    	val2.put("INVOICE_AMT",zje == null?null:String.format("%.2f",Double.valueOf(zje.toString())));//去佣金额--同金额
	    	val2.put("TAX_AMT",bvo[i].getJfybsj()==null?"":bvo[i].getJfybsj());//税额
	    	val2.put("SYSTEM_ID","JC");//系统别
	    	val2.put("CREATE_NAME",queryLRR);//录入人--制单人名称
	    	val2.put("CREATE_ID",person(queryLRR,hvo.getDwbm()));//录入人工号--制单人工号，MDM
	    	val2.put("CREATE_DATE",BodyMap.get("dmakedate"));//录入日期--制单日期
	    	val2.put("MODIFY_NAME","");//修改人
	    	val2.put("MODIFY_ID","");//修改人工号
	    	val2.put("MODIFY_NAME","");//修改日期
	    	bvals.add(val2);
	    }
		 val.put("bodylist", bvals);
		 JSONArray bval = new JSONArray();	//外销发票海关拆分明细信息接口	
		 for (int i = 0; i < bvo.length; i++) {
			 List judgeWXHG = judgeWXHG( hvo, bvo, i, kpTaxCode, chCode, queryInv, custcode, judgeCurrency, jsfs, queryLRR,zje,BodyMap);
			 if(judgeWXHG.get(0).equals("0")){
				 str.append(djbh+judgeWXHG.get(1).toString());
				 return flag;
			 }	
			 JSONObject val3 = new JSONObject();
			    val3.put("PROC_FLAG","0");//处理标志
			    val3.put("BILL_TYPE",DJLX(wx));//单据类型
			    val3.put("F_INVOICE_NO",hvo.getZyx17()==null?"":hvo.getZyx17());//外销发票号
			    val3.put("COMPANY_CODE",(String) kpTaxCode.get("def8")==null?"1016":(String) kpTaxCode.get("def8"));//帐套
			    val3.put("COST_CENTER",queryzrzx(bvo[i].getDeptid()));//责任中心
			    val3.put("SERIAL_NO",i+1);//序号
			    val3.put("CUSTOMS_PRODUCT_CODE",queryInv.get("memo"));//出口商品代码--海关编码HSCODE     用def20来存海光编码
			    val3.put("INVOICE_WT",bvo[i].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//发票重量--数量
			    val3.put("SALE_PRICE", bvo[i].getHsdj()==null?"0.0":String.format("%.2f",Double.valueOf( bvo[i].getHsdj().toString())));//销售单价
			    val3.put("INV_AND_COMMIS_AMT",bvo[i].getJfybwsje()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybwsje().toString())));//含佣金额
			    val3.put("SETTLE_PRICE",bvo[i].getDj()==null?"0.0":String.format("%.2f",Double.valueOf(bvo[i].getDj().toString())));//结算价格
			    val3.put("INVOICE_AMT",zje == null?null:String.format("%.2f",Double.valueOf(zje.toString())));//发票金额
			    val3.put("TAX_AMT",bvo[i].getJfybsj()==null?"":bvo[i].getJfybsj());//税额
			    val3.put("INVOICE_NO",hvo.getZyx17()==null?"":hvo.getZyx17());//监制发票号
			    val3.put("INVOICE_CODE",BodyMap.get("ccreditnum").toString().substring(0, 12));//发票代码--增加字段
			    val3.put("INVOICE_DATE",hvo.getDjrq()==null?"":hvo.getDjrq().toString().substring(0, 8));//发票日期
			    val3.put("SYSTEM_ID","JC");//系统别
			    val3.put("CREATE_NAME","");//录入人
			    val3.put("CREATE_ID","");//录入人工号
			    val3.put("CREATE_DATE","");//录入日期
			    val3.put("MODIFY_NAME","");//修改人
			    val3.put("MODIFY_ID","");//修改人工号
			    val3.put("MODIFY_NAME","");//修改日期
			    bval.add(val3);
		 }
		 String strh = djbh.substring(djbh.length() -3,djbh.length());   //截取
		 //外销三单信息接口 			 
		 JSONArray bval2 = new JSONArray();
		 for (int i = 0; i < bvo.length; i++) {
			 int x=(int)(Math.random()*900)+100;
			 List judgeWXSD = judgeWXSD( hvo, bvo, i, djbh, kpTaxCode, chCode, queryInv, custcode, judgeCurrency, jsfs, queryLRR, zje, jsfs1);
			 if(judgeWXSD.get(0).equals("0")){
				 str.append(djbh+judgeWXSD.get(1).toString());
				 return flag;
			 }	
			 JSONObject val4 = new JSONObject();
			    val4.put("COMPANY_CODE",(String) kpTaxCode.get("def8")==null?"1016":(String) kpTaxCode.get("def8"));//帐套
			    val4.put("COST_CENTER", queryzrzx(bvo[i].getDeptid()));//责任中心
			    val4.put("BILL_TYPE",DJLX(wx));//单据类型
			    val4.put("THREE_READY_NO",bvo[i].getDdh()+strh+x);//三单号(业务单据子项号--出库单号
			    val4.put("F_INVOICE_NO",hvo.getZyx17()==null?"":hvo.getZyx17());//外销发票号
			    val4.put("CONTRACT_NO","");//合约号
			    val4.put("ORDER_NO",queryHT.get("ct_code").toString().substring(0, 10));//合同号
			    val4.put("PROJECT_NO","");//项目号
			    val4.put("PROJECT_NAME",bvo[i].getXm_name()==null?"":bvo[i].getXm_name());//项目名称
			    val4.put("THREE_READY_WT",String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//三单量
			    val4.put("DREW_QUANTITY",bvo[i].getJfshl()==null?null:String.format("%.6f",Double.valueOf(bvo[i].getJfshl().toString())));//开票数量
			    val4.put("SALE_PRICE",bvo[i].getDj()==null?"0.0":String.format("%.2f",Double.valueOf(bvo[i].getDj().toString())));//单价
			    val4.put("DREW_MEASURE_UNIT",queryZjldw((String)queryInv.get("pk_measdoc")));//开票计量单位
			    val4.put("GOOD_AMT",bvo[i].getJfybwsje()==null?null:String.format("%.2f",Double.valueOf(bvo[i].getJfybwsje().toString())));//货款     
			    int intValue1 = bvo[i].getSl().intValue();
				DecimalFormat df1 = new DecimalFormat("0.00");//格式化小数  
			    val4.put("TAX_RATE",df1.format((float)intValue1/100));//税率
			    val4.put("TAX_AMT",bvo[i].getJfybsj()==null?"":bvo[i].getJfybsj());//税额
			    val4.put("SETTLE_METHOD",jsfs1);//结算方式
			    val4.put("SETTLE_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//结算用户代码
			    val4.put("FIN_USER_CODE",custcode.get("custcode").toString().substring(0, 6));//最终用户代码      
			    val4.put("PRODUCT_CODE", queryInv.get("invclasscode"));//产副品代码、费用类型
			    val4.put("FACTORY_SEQ_ID","");//制造单元组织机构
			    val4.put("AS_BUSI_MAIN_CODE","");//应收业务大类 
			    val4.put("AS_BUSI_DETL_CODE","");//应收业务细类
			    val4.put("ACCT_PERIOD_NO",BodyMap.get("djkjnd")+""+BodyMap.get("djkjqj"));//会计期    必填  前六位
			    val4.put("PROD_NAME",queryInv.get("invclasscode"));//品名
			    val4.put("CUSTOMS_PRODUCT_CODE",queryInv.get("memo"));//出口商品代码
			    val4.put("UPLOAD_DATE",new UFDateTime(System.currentTimeMillis()).toString().replace("-", "").replace(" ", "").replace(":", "").substring(0, 8));//上抛日期     必填
			    val4.put("SYSTEM_ID","JC");//系统别
			    val4.put("CONTRACT_TYPE",queryInv.get("invclasscode").toString().substring(0, 4));//合同类型 （放业务类型）
			    val4.put("CREATE_NAME","");//录入人
			    val4.put("CREATE_ID","");//录入人工号
			    val4.put("CREATE_DATE","");//录入日期
			    val4.put("MODIFY_NAME","");//修改人
			    val4.put("MODIFY_ID","");//修改人工号
			    val4.put("MODIFY_NAME","");//修改日期
			    bval2.add(val4);
		 }
		IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
		JSONObject s = ifc.assembleItfData(val, "EXTERNALSALEINVOICE_H_SEND","EXTERNALSALEINVOICE_B_SEND"); 		 
		JSONArray bodyarr = new JSONArray();
		String content1 = null;
		for (int i = 0; i < bvo.length; i++) {//外销发票海关拆分明细信息接口
			JSONObject s1 = ifc.assembleItfData(bval.getJSONObject(i), "EXTERNALSALEINVOICE_CUSTOMSSPLIT_H_SEND",null); 
			String states = (String)s1.get("state"); 
			if("success".equals(states)){					 
				content1 = s1.getString("content");
				content1=content1.replace("[", "").replace("]", "");
				bodyarr.add(content1);
		    }else{
		    	showWarningMessage(djbh+"发送失败");
		    	flag = false;
		    }
		 }		
		JSONArray bodyarr2 = new JSONArray();
		String content2 = null;
		for (int i = 0; i < bvo.length; i++) {//外销三单信息接口 
			JSONObject s2 = ifc.assembleItfData(bval2.getJSONObject(i), "EXTERNALSALEINVOICE_THREEORDERS_H_SEND",null); 
			String states = (String)s2.get("state"); 
			if("success".equals(states)){					 
				content2 = s2.getString("content");
				content2=content2.replace("[", "").replace("]", "");
				bodyarr2.add(content2);
		    }else{
		    	showWarningMessage(djbh+"发送失败");
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
	        JSONObject jsObject =(JSONObject)jsonArrays.get(0);//外销发票头信息接口
	        JSONArray jArrayHead = new JSONArray();
	        jArrayHead.add(jsObject);
	        JSONArray jsObjectBody = new JSONArray();
	        for (int i = 1; i < jsonArrays.size(); i++) {
	        	 JSONObject jb =(JSONObject)jsonArrays.get(i);//外销发票明细信息接口
	        	 jsObjectBody.add(jb);
			}	        	        		 	                
	        boolean send1 = send(bodyarr2, djbh);////排序   外销三单  1
	        boolean send2 = send(bodyarr, djbh);//外销发票海关拆分明细信息接口 2
	        boolean send3 = send(jsObjectBody, djbh);//外销发票明细信息接口  3
	        boolean send4 = send(jArrayHead, djbh);//外销发票头信息接口  4	
	        if(send1 == true && send2 == true && send3 == true && send4 == true){
	        	updateDJBH(djbh,nyr);	
	        	flag = true;
	        }else {
	        	showWarningMessage(djbh+"发送失败");
		    	flag = false;
			}	
		}else{
	    	 showWarningMessage(djbh+"发送失败");
	    	 flag = false;
	     }
		return flag;
	 }
  //end
  /**
   * 外销发送标财
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
  
//add by zy 2019-10-15 添加标财回撤方法
//  List<String> list = new ArrayList<String>();
  @SuppressWarnings({ "restriction", "unchecked" })
  private void bcRetreat(){
	  List list = new ArrayList();
	  String corp = PoPublicUIClass.getLoginPk_corp();
	  IdetermineService idetermineService =(IdetermineService) NCLocator.getInstance().lookup(IdetermineService.class.getName());
	  Boolean result = idetermineService.check(corp);
	  if(result){//判断当前公司是否为国内公司，否则不执行
	  	 IUiPanel iPanel = getCurrentPanel();
		 @SuppressWarnings("unused")
		 DjPanel djPanel = null;//单据面板
		   if ((iPanel instanceof DjPanel))
		    {
		      djPanel = (DjPanel)iPanel;
		    }
		    List<DJZBVO> vos = getSelectedRowVO();//获取选中行数
			StringBuffer str = new StringBuffer();
			StringBuffer err = new StringBuffer();//接收提示信息
			//判断是列表界面还是卡片界面
			boolean showing = djPanel.getArapDjPanel1().getBillCardPanelDj().isShowing();
			if(showing == false && vos.size()==0){
				showWarningMessage("列表下未选中要传标财的数据");
				return;
			}else if(showing == false && vos.size()>0){//如果选中的数据大于零
	        	//列表界面
	        	for (int j = 0; j < vos.size(); j++) {
	        		DJZBHeaderVO hvo = (DJZBHeaderVO)vos.get(j).getParentVO();
	        		String djbh = vos.get(j).getParentVO().getAttributeValue("djbh").toString();
	        		String yn=judgeDJBH(djbh);
	        		if("Y".equals(yn)){
	        			System.out.println("单据编号"+djbh);
		        		Integer djzt = hvo.getDjzt();
		        		if(djzt == 2){//单据状态 == 2   已审核
		        			if(list.contains(djbh)){
								str.append(djbh+",");
								continue;
							}else {
								  JSONObject val = new JSONObject();
								  DJZBItemVO[] bvo = (DJZBItemVO[])vos.get(j).getChildrenVO();
								  String cxlx="";
								  String judgeCurrency = judgeCurrency(bvo[0].getBzbm());//判断币种
								  if("CNY".equals(judgeCurrency)){
									  cxlx="C";
								  }else{
									  cxlx="F";
								  }
							      val.put("CANCLE_TYPE",cxlx);//撤销类型
							      String zt=(String) kpTaxCode(bvo[0].getDwbm()).get("def8");
							      val.put("COMPANY_ID",zt);//账套
							      val.put("SYSTEM_ID","JC");//系统别
							      Map map=queryFph(djbh);
							      System.out.println("map::::::::"+map);
							      val.put("BILL_NO",hvo.getDjbh());//业务单据号 -- 业务单据号
							      val.put("INVOICE_NO",map.get("vdef17")==null?"":map.get("vdef17"));//发票号 -- 纸质发票号
							      val.put("INVOICE_CODE",map.get("ccreditnum")==null?"":map.get("ccreditnum"));//发票代码
							      val.put("RESULT_DESC",cxlx);//撤销描述
							      val.put("REMARK",cxlx);//备注
							      
//								  【接口调用示例代码---供参考】
								  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
								  JSONObject s = ifc.assembleItfData(val, "JCBCS8",null); 
								  System.out.println(s);
								  //JSONObject js = ifc.disassembleItfData(xbuss, "BCJCS1");//【解析接口示例】 
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
							                  //成功逻辑 
							            	  System.out.println("val:::::::"+val);
							            	  updateHC(djbh);
							            	  System.out.println("########发送成功########");
							              /*}else{ 
							                  //失败逻辑 
							              } */
								      } 
								  } 
							}
		        			if (str.length()==0 || str==null) {
//	      		        		showWarningMessage("发送成功");
	      		        		err.append("单据"+str+"回撤成功\n");
	      					}else {
//	      						showWarningMessage("发送成功,"+str+"已发送过！");
	      						err.append("单据"+str+"已回撤过！\n");
	      					}
		        		}else {
//						  	showWarningMessage("请选择已审核单据！");
						  	err.append("单据"+djbh+"未审核，请选择已审核单据！\n");
						}
	        		}else if("H".equals(yn)){
//	        			showWarningMessage("单据"+djbh+"已回撤过");
	        			err.append("单据"+djbh+"已回撤过\n");
	        		}else{
//	        			showWarningMessage("单据"+djbh+"未传过标财，不能回撤！");
	        			err.append("单据"+djbh+"未传过标财，不能回撤！\n");
	        		}
	        			
    				
	        	}	  
	        	if(err!=null || err.length()>0){
					showWarningMessage(err.toString());
				}
	        }else if(showing){
	        	//卡片界面
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
    				if(djzt == 2){//单据状态 == 2   已审核
    					JSONObject val = new JSONObject();
    					  String cxlx="";
    					  String judgeCurrency = judgeCurrency(bvo[0].getBzbm());//判断币种
    					  
    					  if("CNY".equals(judgeCurrency)){
    						  cxlx="C";
    					  }else{
    						  cxlx="F";
    					  }
    				      val.put("CANCLE_TYPE",cxlx);//撤销类型
    				      String zt=(String) kpTaxCode(bvo[0].getDwbm()).get("def8");
    				      val.put("COMPANY_ID",zt);//账套
    				      val.put("SYSTEM_ID","JC");//系统别
    				      Map map=queryFph(djbh);
    				      val.put("BILL_NO",djbh);//业务单据号
    				      val.put("INVOICE_NO",map.get("vdef17")==null?"":map.get("vdef17"));//发票号
    				      val.put("INVOICE_CODE",map.get("ccreditnum")==null?"":map.get("ccreditnum"));//发票代码
    				      val.put("RESULT_DESC",cxlx);//撤销描述
    				      val.put("REMARK",cxlx);//备注
    				      
//    					  【接口调用示例代码---供参考】
    					  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
    					  JSONObject s = ifc.assembleItfData(val, "JCBCS8",null); 
    					  System.out.println(s);
    					  //JSONObject js = ifc.disassembleItfData(xbuss, "BCJCS1");//【解析接口示例】 
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
    				                  //成功逻辑 
    					          	  updateHC(djbh);
    				            	  System.out.println("val:::::::"+val);
    				            	  System.out.println("########卡片发送成功########");
    				              /*}else{ 
    				                  //失败逻辑 
    				              } */
    					      } 
    					  } 
    					
    					if (str.length()==0 || str==null) {
    		        		showWarningMessage("发送成功");
    					}else {
    						showWarningMessage("发送成功,"+str+"已发送过！");
    					} 	
    				}else {
    				  	showWarningMessage("请选择已审核单据！");
    				}
        		}else{
        			showWarningMessage("单据"+djbh+"未传过标财，不能回撤！");
        		}
			}
			
		}   
  }
//  end
  
  
  
  
  
  /**
   * 审核收款单发送cvm
   * pengjiajia
   * 2018年5月4日14:21:31
   */
  @SuppressWarnings("all")
  public void onButtonClicked(ButtonObject bo)
  {
	 //add 应收传标财按钮触发事件    wy  2019年9月10日 
	  if (bo.getName().equals("应收传标财")) {
		  sendXbus(bo);
	   } 	
	  //end
	  if(bo.getName().equals("审核")){
		  IUiPanel iPanel = getCurrentPanel();
		  DjPanel djPanel = null;//单据面板
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
	        if(vos.size()>0){//如果选中的审核数据大于0
	        	 for(DJZBVO vo:vos){
	 	        	String djdl = (String)vo.getParentVO().getAttributeValue("djdl");//djdl代表单据大类（单据类型）
	 	        	String corp = (String) vo.getParentVO().getAttributeValue("dwbm");//dwbm代表单位编码
	 	      		Object flag = vo.getParentVO().getAttributeValue("djzt");//djzt代表单据状态，1代表未审核，2代表已审核
	 	      		
	 	      		if (djdl.equals("sk")//sk收款单
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
	 	      				showHintMessage("收款单推送CVM:"+result); 
	 	      			}
	 	      		} else if (djdl.equals("ys")//ys应收单
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
	 	      			    showHintMessage("应收单推送CVM:"+result);
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
 	      			    showHintMessage("收款单推送CVM:"+result);
 	      			}
 	      		} else if (djdl.equals("ys")
						&& (corp.equals("1016") || corp.equals("1071")
								|| corp.equals("1103") || corp.equals("1097")
								|| corp.equals("1017") || corp.equals("1018")|| corp.equals("1019")
								|| corp.equals("1107"))) {
 	      			if(flag.equals(2)){
 	      			    String result = saleSendcvm(vo);
 	      			    showHintMessage("应收单推送CVM:"+result);
 	      			}
 	      		}
	        }
	  }else if(bo.getName().equals("删除")||bo.getName().equals("单据操作")){
		  List<DJZBVO> vos = getSelectedRowVO();
		  String pk_corp = (String) vos.get(0).getParentVO().getAttributeValue("dwbm");
		  if (pk_corp.equals("1016") || pk_corp.equals("1071")
					|| pk_corp.equals("1103") || pk_corp.equals("1097")
					|| pk_corp.equals("1017") || pk_corp.equals("1018")|| pk_corp.equals("1019")|| pk_corp.equals("1107")) {
			  showErrorMessage("不允许删除单据,请用对冲单据解决!");
			  return;
		  }
		  getCurrentPanel().onButtonClicked(bo);
	  
	  
	  }else if(bo.getName().equals("应付传标财")){//add by zsh 2019-10-15 添加应付传标财按钮触发事件
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
		}else if(bo.getName().equals("标财回撤")){//add by zy 2019-10-15 添加标财回撤按钮触发事件
			  bcRetreat();
		}else{
		 StringBuffer str = new StringBuffer();
			  IUiPanel iPanel = getCurrentPanel();
			  DjPanel djPanel = null;//单据面板
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
	      			str.append("\n"+"单据+"+djbh+"已经发送成功,无法反审核");
	      			}
			    	 }
			    }else{
			    	DJZBVO vo = new DJZBVO();					
					vo = (DJZBVO)djPanel.getArapDjPanel1().getBillCardPanelDj().getBillData().getBillValueVO(DJZBVO.class.getName(), DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName());
					String djbh =(String) vo.getParentVO().getAttributeValue("djbh");
				    String value =judgeDJBH(djbh);
		      		if(value!=null){
		      			str.append("\n"+"单据+"+djbh+"已经发送成功,无法反审核");
		      			}
			    }
			    if(bo.getName().equals("反审核")&&str.length()>0&&str!=null){
			    	 showErrorMessage(str.toString());
			    	 return;
				}	
			    getCurrentPanel().onButtonClicked(bo);
	  }

  }
  
  /**
   * add by zsh 2019-10-15 添加应付传标财按钮触发事件
   */
	@SuppressWarnings({ "restriction", "unchecked" })
	public void transmission(){
		 IUiPanel iPanel = getCurrentPanel();
		 DjPanel djPanel = null;
	    if ((iPanel instanceof DjPanel))
	    {
	      djPanel = (DjPanel)iPanel;
	    }
	  
	    List<DJZBVO> vos = getSelectedRowVO();//获取选中行数
	    boolean show = djPanel.getArapDjPanel1().getBillCardPanelDj().isShowing();
	    if(vos.size()==0&&show==false){
	    	 showWarningMessage("请选择单据");
	    }
	   
	    if(vos.size()>0&&show==false){
	    	CxbusList(vos, djPanel);
	    }
	    else if(show){
	    	//卡片界面
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
	   * 更具单据客户编码查询客商主信息和客商地址信息
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
	   * 查询客商信息
	   * add by zsh 2019-10-16
	   * @param bm
	   * @return
	   */
	  
	  @SuppressWarnings("unchecked")
	private List queryks(String res,String val ){
			//前台数据查询操作，得到的是一个接口
		  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//根据收款单主表VO获得单据主键
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
			//客商
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
	   * 判断是否插入流水号
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
	   * 查询流水号
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
			System.out.println("流水号"+val+"长度"+val);
			return val.trim();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return resval;
	 }
	/**
	   * 报支单位组织代码
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
	   * 查询银行账号和名称
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
		resval.add(0,"无账号密码");
		return resval;
	 }
	/**
	   * 查询存货
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
 * 查询合同号 合同名称 币种 发票号
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
			list.add(0,custMap.get("currtypecode").toString());	//币种
			}
			if(custMap.get("ct_code").toString().equals("0")){
				list.add(1,"");
			}else{
			list.add(1,custMap.get("ct_code").toString());//合同号
			}
			if(custMap.get("ct_name").toString().equals("0")){
				list.add(2,"");
			}else{
			list.add(2,custMap.get("ct_name").toString());//合同名称
			}
			if(custMap.get("vinvoicecode").toString().equals("0")){
				list.add(3,"");
			}else{
			list.add(3,custMap.get("vinvoicecode").toString());//发票号
			}
			if(custMap.get("vdef13").toString().equals("0")){
				list.add(4,"");
			}else{
			list.add(4,custMap.get("vdef13").toString());//发票代码
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
	 * 发票种类
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
	 * 发票种类
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

	//查询方法   add by gt 2019-10-24
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

	//插入方法 add by gt 2019-10-24
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
	 * 费用明细
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
	 * 税率
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
   * 获取列表选中数据
   * 彭佳佳 2018年5月11日17:08:12
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
	   * 拿到客商编码
	   * wy
	   * 2019/10/11
	   * @param vo
	   * @return
	   */
	  @SuppressWarnings("unchecked")
	 public  Map custcode(DJZBHeaderVO hvo){
		  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			//主键
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
			//公司编码
			String corp = hvo.getDwbm();
			//客商代码
			String code = (String) BodyMap.get("ksbm_cl");
			Map custcode = this.QueryCustCoded(code,corp);	  
		return custcode;
	  }
	   /**
		 * 查询客户编码、客户税号
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
   * 应收单传cvm
   * @param vo
   * @return
   */
  @SuppressWarnings("all")
  private String saleSendcvm(DJZBVO vo){
	  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	  DJZBHeaderVO hvo = (DJZBHeaderVO) vo.getParentVO();
		//主键
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
		
		//开票单号
		String vreceiptcode =BodyMap.get("fph")==null?"":BodyMap.get("fph").toString();
		
		//应收单号
		String yscode = hvo.getDjbh();
		
		//纸质发票号
		String invoice = hvo.getZyx17()==null?"":hvo.getZyx17().toString();
		
		//公司编码
		String corp = hvo.getDwbm();
		String unitcode = this.QueryCorp(corp);
		
		//客商代码
		String code = (String) BodyMap.get("ksbm_cl");
		String custcode = this.QueryCustCode(code,corp);
				
		
		//单据日期
		String dbilldate = String.valueOf(hvo.getDjrq());
		UFDouble fpje = new UFDouble(0);
		Map cmap = new HashMap();
		for(int j = 0;j<Bodylist.size();j++){
			cmap = (Map) Bodylist.get(j);
			Object numsummny = cmap.get("jfbbje");
			fpje = fpje.add(new UFDouble(numsummny.toString()));
		}
		Map jsonMap = new HashMap();
		//add by zwx 2019-11-18 cvm增加传输字段
		HashMap mapFp = getFpInfo(hvo.getVouchid());
		if(mapFp==null){
			jsonMap.put("invoicecode", "");// 纸质发票代码
			jsonMap.put("costCenter", "");// 责任中心
		}else{
			jsonMap.put("invoicecode", mapFp.get("invoicecode")==null?"": mapFp.get("invoicecode").toString().trim());// 纸质发票代码
			jsonMap.put("costCenter", mapFp.get("costcenter")==null?"": mapFp.get("costcenter").toString().trim());// 责任中心
		}
		//end by zwx
		jsonMap.put("username", "baosteel");//对接用户名
		jsonMap.put("password", "123456");//对接密码
		jsonMap.put("vreceiptcode", vreceiptcode);// 开票单号
		jsonMap.put("yscode", yscode);//应收单号
		jsonMap.put("invoice", invoice);// 纸质发票号
		jsonMap.put("vendedorcode", custcode);// 客商代码
		jsonMap.put("unitcode", unitcode);// 公司编码
		jsonMap.put("dbilldate",dbilldate);// 开票日期
		jsonMap.put("kpje", String.valueOf(fpje));//发票总金额
		List jsonList = new ArrayList();
		Map bmap = new HashMap();
		for(int i = 0;i<Bodylist.size();i++){
			bmap = (Map) Bodylist.get(i);
			//存货编码项目主键	
			String inv =bmap.get("cinventoryid")==null?"":bmap.get("cinventoryid").toString();
			Map map = this.QueryInvl(inv,corp);
			
			//数量
			String nnumber = String.valueOf(bmap.get("jfshl"));
			
			//价税合计
			String noriginalcursummny = String.valueOf(bmap.get("jfbbje"));
			
			Map jsonBody = new  HashMap();
			jsonBody.put("invcode", map.get("invcode")==null?"":map.get("invcode").toString());// 存货编码
			jsonBody.put("mainmeasrate", map.get("invclasscode")==null?"":map.get("invclasscode").toString());// 存货分类
			jsonBody.put("nnumber", nnumber);//数量
			jsonBody.put("noriginalcursummny", noriginalcursummny);// 价税合计
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
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String startTime=null;
		String endTime=null;
		try {
//			hid=recordLogs(vo);
			startTime=df.format(new Date());//获取调用接口前的系统时间
//			url = new URL("http://10.70.26.23/cvm/CVMService/NC002");//正式
			url = new URL("http://10.70.76.9:90/cvm/CVMService/NC002");//测试环境
			reustle  = this.httpconnect(url, jsonsb);
		} catch (MalformedURLException e) {
			endTime=df.format(new Date());
			e.printStackTrace();
			updateLogs(hid, false, e.getMessage(),startTime,endTime);
			return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
		}catch(Exception e){
			endTime=df.format(new Date());
			e.printStackTrace();
			updateLogs(hid, false,e.getMessage(),startTime,endTime);
			return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
		}	
		// 如果调用无异常的情况
		endTime=df.format(new Date());
		updateLogs(hid,true,reustle,startTime,endTime);
		return reustle;
	}
  
  
  /**
	 * 查询客户编码
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
	 * 查询公司编码
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
	 * 查询存货编码和存货分类
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
   * 收款单发送CVM(外部接口)
   * @param vo
   * @return
   */
    @SuppressWarnings("all")
  public String sendGather(DJZBVO vo){
		String reustle = null;
		//前台数据查询操作，得到的是一个接口
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		//收款单主表VO
		DJZBHeaderVO hvo = (DJZBHeaderVO) vo.getParentVO();
		//根据收款单主表VO获得单据主键
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
		//事项审批单号
		String item_bill_pk = hvo.getItem_bill_pk()==null?"":hvo.getItem_bill_pk();
		//预收付标志
		String prepay = hvo.getPrepay().toString();
		String flag = null;
		if(prepay.equals("Y")){
			flag = "预收";
		}else if(prepay.equals("N")){
			flag = "应收";
		}
		//单据号
		String djbh = hvo.getDjbh();
		//单据日期
		String	djrq = hvo.getDjrq().toString();
		//原币金额
		String ybje = String.valueOf(hvo.getYbje());
		//本币金额
		String bbje = String.valueOf(hvo.getBbje());
	    //本币汇率
		String bbhl= String.valueOf(BodyMap.get("bbhl"));
		//公司
		String unitcode = hvo.getDwbm();
		//客商
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
		//币种
		String pk_currtype = (String) BodyMap.get("bzbm");
		//到账日期
		String qxrq =(String) BodyMap.get("qxrq");
		//查询公司代码
		String corpsql = "select unitcode from bd_corp where pk_corp = '"+unitcode+"'";
		//查询币种类型
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
		jsonObject.put("username","baosteel");//对接用户名
		jsonObject.put("password", "123456");//对接密码
		jsonObject.put("unitcode", map1.get("unitcode"));//公司
		jsonObject.put("custcode", custMap.get("custcode")==null?"":custMap.get("custcode").toString());//客商
		jsonObject.put("item_bill_pk", item_bill_pk);//事项审批单号
		jsonObject.put("prepay", flag);//预收付标志
		jsonObject.put("djbh", djbh);//单据编号
		jsonObject.put("djrq", djrq);//单据日期
		jsonObject.put("ybje", ybje);//原币金额
		jsonObject.put("bbje", bbje);//本币金额
		jsonObject.put("bbhl", bbhl);//本币汇率
		jsonObject.put("bz", map.get("currtypename"));//币种
		jsonObject.put("dzrq", qxrq);//到账日期
		Gson gson = new Gson();
		Object ss = gson.toJson(jsonObject);
		StringBuffer jsonsb = new StringBuffer();
		jsonsb.append("[").append(ss).append("]");
		URL url;
		String hid = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String startTime=null;
		String endTime=null;
		try {
//			hid = recordLogs(vo);
			//调用CVM HTTP接口
//			url = new URL("http://10.70.26.23/cvm/CVMService/NC001");//正式
			url = new URL("http://10.70.76.9:90/cvm/CVMService/NC001");//测试环境
			startTime=df.format(new Date());//获取调用接口前的系统时间
			//获取返回参数
			reustle  = this.httpconnect(url, jsonsb);
		} catch (MalformedURLException e) {
			endTime=df.format(new Date());
			e.printStackTrace();
			updateLogs(hid,false,e.getMessage(),startTime,endTime);
			return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e.getMessage()+"'\"}]";
		}catch(Exception e1){
			endTime=df.format(new Date());
			e1.printStackTrace();
			updateLogs(hid,false,e1.getMessage(),startTime,endTime);
			return "[{\"status\":\"error\",\"message\":\"连接CVM接口异常(URL协议)'"+e1.getMessage()+"'\"}]";
		}
		updateLogs(hid,true,reustle,startTime,endTime);
		// 如果调用无异常，会打印出调用成功后的系统时间
		return reustle;
	}
    
    
    
  /**
   * 更新数据库中的lContext字段  
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
   * 调用cvm接口
   * @param url
   * @param json
   * @return
   * @throws Exception
   */
	@SuppressWarnings("unchecked")
	private String httpconnect(URL url,Object json) throws Exception {
			String reustle =null;
	        //创建HTTP链接
	        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	    
	        //设置请求的方法类型
	        httpURLConnection.setRequestMethod("POST");
	        
	        //设置请求的内容类型
	        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
	        
	        //设置发送数据
	        httpURLConnection.setDoOutput(true);
	        //设置接受数据
	        httpURLConnection.setDoInput(true);
	        
	        //发送数据,使用输出流
	        OutputStream outputStream = httpURLConnection.getOutputStream();
	        //发送的soap协议的数据
	        String content = "jsonData="+URLEncoder.encode(json.toString(), "UTF-8");
	        //发送数据
	        outputStream.write(content.getBytes());
	    
	        //接收数据
	        InputStream inputStream = httpURLConnection.getInputStream();
	        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
	        StringBuffer buffer = new StringBuffer();  
	        String line = "";  
	        while ((line = in.readLine()) != null){  
	          buffer.append(line);  
	        }  
	        
	        reustle = buffer.toString();
	        in.close();
	        //解析CVM返回JSON数据
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
//	 * 记录日志
//	 * @param vo
//	 */
//	public String recordLogs(DJZBVO vo){
//		String hid = null;
//			//---------------------------------------------------------
//			LogVO logVO = new LogVO();
//		    Integer dr=(Integer) vo.getParentVO().getAttributeValue("dr");//获取删除标识
//		    String shr=(String) vo.getParentVO().getAttributeValue("shr");//获取审核人名称
//			String  headBillID=(String)vo.getParentVO().getAttributeValue("vouchid");//获得当前审核的单据的主键id号
//			String dwbm=(String)vo.getParentVO().getAttributeValue("dwbm");//获取到当前需审核单据的单位编码
//			String djlx=(String)vo.getParentVO().getAttributeValue("djdl");//获取审核单据的单据类型
//			String djbx=(String)vo.getParentVO().getAttributeValue("djbh");//获取订单编号
//			logVO.setDr(dr);
//			logVO.setDjlx(djlx);//放单据类型
//			logVO.setDjbx(djbx);//订单编号
//			logVO.setLUser(shr);//审核人
//			logVO.setPk_corp(dwbm);//单位编码
//			logVO.setHeadBill_id(headBillID);//查询出来的单据表头的主键
//			logVO.setTs(new UFDateTime());
//			logVO.setLContext("您正在审核订单");//将审核后的日志记录到数据库表日志描述字段中
//			//logVO.setBodyBill_id(bodyBillID);
//			IVOPersistence ivo = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
//			//将LogVO插入到数据库
//  			try {
//  				hid = ivo.insertVO(logVO);
//			} catch (BusinessException e) {
//				e.printStackTrace();
//			}
//			//---------------------------------------------------------
//			return hid;
//	}

	/**
	 * 判断人民币是本币还是外币
	 * wy
	 * 2019年9月10日 
	 * @param bzbm
	 * @return
	 */
	public String  judgeCurrency(String bzbm){
		if (bzbm == null) {
			return "";
		}
		IUAPQueryBS iquery = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		//查询币种类型
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
	 * 判断单据类型是内销还是外销	 
	 * wy
	 * 2019年9月10日 
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
	   * 根据部门id查询MDM责任中心
	   * wy
	   * 2019年9月10日 
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
	   * 根据 单据编号 查客商税号、客商银行账号、客商银行名称、发票代码、备注、制单日期、单据会计年度、单据会计期间
	   *  wy
	   * 2019年9月10日 
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
		 * 根据 公司 查 
		 * 开票单位名称unitname,开票单位税号taxcode就是纳税人登记号,公司别 == 账套
		 * wy
	     * 2019年9月10日
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
		 * 查cinventoryid == pk_invbasdoc  1020A21000000000K87A
		 * wy
		 * 2019年10月20日 
		 * @param hvo
		 * @return
		 */

		@SuppressWarnings("unchecked")
		private String selectInvl(DJZBHeaderVO hvo){
			  IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				//主键
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
		 * 根据存货基本档案查
		 * 存货分类编码、中文名称、主计量单位id、海关编码（memo）
		 * wy
		 * 2019年10月20日 
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
		 * 根据主计量id查主计量单位
		 * wy
		 * 2019年10月20日 
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
		 * 查询录入人
		 * wy
		 * 2019年10月20日 
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
		 * 查合同类型编码和合同类型名称,合同编号
		 * wy
		 * 2019年10月20日 
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
		 * 查询发票种类
		 * wy
		 * 2019年10月20日 
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
		 * 根据业务类型编码 查  业务类型名称  
		 * if 名称 = 代加工成品销售业务流程 dx 
		 * else zx
		 * wy
		 * 2019年10月20日 
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
		 * 判断 ARAP_djzb 中 zyx38 是否为Y,如果为Y,则不能再传第二次,如过为空和N则还可以再传
		 * wy
		 * 2019年10月20日 
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
		 * 2019年10月20日 
		 * @param djbh
		 */
		public void updateDJBH(String djbh,String ts){			
			IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());			 
		    String sql = "update ARAP_djzb set zyx38 = 'Y',zyx36 = '上传中',zyx48='"+ts+"' where djbh = '"+djbh+"' and nvl(dr,0)=0";
		    try {
				ipubdmo.executeUpdate(sql);
				System.out.println(djbh+" zyx38修改成功");
			} catch (BusinessException e) {
				e.printStackTrace();
				System.out.println(djbh+" zyx38修改失败");
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
				System.out.println(djbh+" zyx38修改成功");
			} catch (BusinessException e) {
				e.printStackTrace();
				System.out.println(djbh+" zyx38修改失败");
			}		
		}
		
		/**
		   * 根据应付单据号查询发票号、纸质发票号、发票代码
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
		  
		//走列表	ZSH	  
		  public void CxbusList(List<DJZBVO> vos,DjPanel djPanel){
			  Date date = new Date();
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  String nyr = sdf.format(date);	//获取当前系统时间	
			  StringBuffer str = new StringBuffer();//存取发送成功
			  StringBuffer str1 = new StringBuffer();//存取已经发送成功			 
			  StringBuffer str3 = new StringBuffer();
			  ITFBZVO ItfbzVO = new ITFBZVO();
			    String kszh = new String();
				@SuppressWarnings("unused")
				DJZBVO vo = new DJZBVO();				
				vo = (DJZBVO)djPanel.getArapDjPanel1().getBillCardPanelDj().getBillData().getBillValueVO(DJZBVO.class.getName(), DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName());							   				
			     List<String> list  = new ArrayList<String>();			      
				for(int i=0;i<vos.size();i++){
				StringBuffer str2 = new StringBuffer();//存取发送失败
				DJZBHeaderVO dJZBHeaderVOs = (DJZBHeaderVO)vos.get(i).getParentVO();//获取选中行数的表头
				DJZBItemVO[] dJZBItemVOs = (DJZBItemVO[])vos.get(i).getChildrenVO();//获取选中行的表体
				if(dJZBHeaderVOs.getDjbh()==null||dJZBHeaderVOs.getDjbh().equals("")||dJZBHeaderVOs.getDjbh().toString().length()==0){
//					str2.append("\n"+"单据号未填写");
					showWarningMessage("\n"+"单据号未填写");
					continue;
				}
					 //校验该条单据是否传过
				 String billno=judgeDJBH(dJZBHeaderVOs.getDjbh().toString());
				 System.out.println("billno:"+billno);
				if(billno == null){		
				vo =vos.get(i);
				DJZBHeaderVO DJZBHeaderVO = (DJZBHeaderVO)vos.get(i).getParentVO();//获取选中行数的表头
				DJZBItemVO[] DJZBItemVO = (DJZBItemVO[])vos.get(i).getChildrenVO();//获取选中行的表体
				//校验数据
				StringBuffer resval2 =	CheckData( DJZBHeaderVO, DJZBItemVO);
				
				if(resval2!=null&&resval2.toString().length()>0){
					str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据号下"+resval2.toString());
//					showWarningMessage("在"+DJZBHeaderVO.getDjbh()+"单据号下下"+resval2.get(1).toString());
//					continue;
				}
				String bzCode = BzCode(DJZBHeaderVO.getDwbm());	
				List listress =SelectBank(DJZBItemVO[0].getKsbm_cl(),DJZBHeaderVO.getDwbm());
				if(listress.get(0).equals("无账号密码")){
					  str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据号下客商账号密码未填写");
//					  showWarningMessage("客商账号密码未填写");
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
				StringBuffer lists3 =ListCheck(listvall);//校验客商数据是否为空
			
				if(lists3!=null&&lists3.toString().length()>0){
					str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+lists3.toString());
//					 showWarningMessage(lists3.get(1).toString());
//					 continue;
					}
				
//				List<String> lists =ListVal(DJZBHeaderVO.getDjbh());
				List<String> listsval =listVal(DJZBHeaderVO.getDjbh());
				if(listsval.get(0).equals("A")){
					str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据下，币种，合同号，合同名称，发票号，发票代码为空，请填写");
				}else{
				StringBuffer listsval2 = checkListval(listsval);
				if(listsval2!=null&listsval2.toString().length()>0){
					str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据号下"+listsval2);
//					 showWarningMessage(lists3.get(1).toString());
//					 continue;
				}
				}
				JSONObject val = new JSONObject();
				String resval =	SERIALNUM(DJZBHeaderVO.getDjbh());
			    val.put("EXPANSE_REPORT_ID",resval);//报支清单号
			    val.put("COMPANY_CODE",bzCode);//帐套
			    val.put("BILL_TYPE_CODE","P04001");//单据类型 DJZBHeaderVO.getYwbm()
			    System.out.println(DJZBHeaderVO.getVouchid());
			    val.put("OWER_SYS_ID","JC");//来源系统
			   if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
				val.put("INVOICE_NO","");//发票号
			    }else{
			    val.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//发票号
			    }
			   if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
				val.put("INVOICE_CODE","");//发票代码
			   }else{
			    val.put("INVOICE_CODE",listsval.get(4));//发票代码
			   }
			   String pflx =fplx(DJZBHeaderVO.getDjbh());
			    String Ftype = Fptype(pflx);
			    if(Ftype==null){
			    
			    	 val.put("INVOICE_TYPE","");//发票种类
			    }else{
			    val.put("INVOICE_TYPE",Ftype);//发票种类
			    }
			    val.put("INVOICE_DATE",DJZBHeaderVO.getDjrq().toString().replace("-", ""));//开票日期
			   String sl = Sl(DJZBHeaderVO.getDjbh());
			    val.put("TAX_RATE",Double.valueOf(sl));//税率 1
			    UFDouble dJZBItemVO = new UFDouble();
			    UFDouble Dfybsj = new UFDouble();
			    for(int j=0;j<DJZBItemVO.length;j++){
			    	 dJZBItemVO = DJZBItemVO[j].getDfbbwsje().add(dJZBItemVO) ;
			    	 Dfybsj =DJZBItemVO[j].getDfybsj().add(Dfybsj);
			    }
			   UFDouble dfybje =  DJZBItemVO[0].getDfybje();
			   UFDouble dfybsj =  DJZBItemVO[0].getDfybsj();
			   UFDouble fpje = dfybsj.add(dfybje);
//			    val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())/100));//发票金额
//			    val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())/100));//发票税额
			    val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//发票金额
			    val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//发票税额
			    val.put("PREPAY_DATE","");//预计付款日期
			    val.put("SETTLEMENT_TYPE","A10");//结算方式
			    val.put("COUNTERACT_AMOUNT",0.00);//核销预付款金额lists.get(2)
			    val.put("CAN_END_AMT_CUR",0.00);//留尾款金额
			    val.put("PAYEE_CODE",listvall.get(0));//受款人代码  1
			    val.put("PAYEE_TYPE",listvall.get(1));//受款人类型
			    val.put("PAYEE_NAME",listvall.get(4));//受款人名称
			    if(listress.get(0).equals("无账号密码")||listress.get(1).toString().length()==0&&listress.get(1)==null){
			    val.put("PAYEE_ACCOUNT","");//受款人帐号
			    }else{
			    val.put("PAYEE_ACCOUNT",listress.get(1));//受款人帐号
			    }
			    val.put("PAYEE_ACCOUNT_ID","");//受款人帐号序号
			    val.put("PAYEE_TAX_NO",listvall.get(5));//受款人税号
			    if(listress.get(0).equals("无账号密码")||listress.get(0).toString().length()==0&&listress.get(0)==null){
			    val.put("PAYEE_ACCOUNT_NAME","");//受款人开户行名称
			    }else{
			    val.put("PAYEE_ACCOUNT_NAME",listress.get(0));//受款人开户行名称
			    }
			    val.put("PAYEE_BANK_TYPE","");//受款人银行类别
			    val.put("PAYEE_BANK_CODE","");//受款人银行地区码
			    val.put("COUNTRY","");//受款人省市/国别
			    val.put("PAYEE_ADDRESS",listvall.get(2));//受款人受款人地址
			    val.put("PAYEE_ZIP_CODE","");//受款人邮政编码
			    val.put("PAYEE_PHONE",listvall.get(3));//受款人联系电话
			    val.put("PAYEE_EMAIL","");//受款人E-mail
			    val.put("LC_NO","");//信用证号
			    val.put("DETAIL_NUM",DJZBItemVO.length);//发票明细记录数 
			    val.put("TEMP1","");//备用字符1
			    val.put("TEMP2","");//备用字符2
			    val.put("TEMP3","");//备用字符3
			    val.put("TEMP4","");//备用字符4
			    val.put("TEMP5","");//备用字符5
			    JSONArray bvals = new JSONArray();
//			    int rows = 2; //请把rows改成你表体的行数
			    
			    for (int j = 0; j < DJZBItemVO.length; j++) {	
			    	
			    	JSONObject val1 = new JSONObject();
			    	String resval1 =	SERIALNUM(DJZBHeaderVO.getDjbh());
			    	System.out.println("报支："+resval1);
			        val1.put("BILL_NO",resval1);//报支清单号
			        val1.put("COMPANY_CODE",bzCode);//帐套}
			        val1.put("BILL_TYPE_CODE","P04001");//单据类型
			        val1.put("OWER_SYS_ID","JC");//来源系统
//			        val1.put("INVOICE_CODE",listsval.get(4));//发票代码
			        if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
			        	val1.put("INVOICE_CODE","");//发票代码
					   }else{
					    val1.put("INVOICE_CODE",listsval.get(4));//发票代码
					   }
			        if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
			        	val1.put("INVOICE_NO","");//发票号
					    }else{
					    val1.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//发票号
					    }
//			        val1.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//发票号
			        val1.put("INVOICE_DETAIL_ID",j+1);//发票明细行序号		        
			        String zrzx = queryzrzx(DJZBItemVO[0].getDeptid());
			        if(zrzx.length()>8){
			        	if(j<1){
			        	str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据下,部门请选择最新的部门");
			        	}
			        	val1.put("COST_CENTER","");//责任中心			        	
			        }else{
			        val1.put("COST_CENTER",zrzx);//责任中心
			        }
			        val1.put("AGENTED_FLAG","");//代付标志
			        val1.put("AGENTED_CODE","");//代付单位代码
			        val1.put("AGENT_FLAG","");//代理标记
			        val1.put("AGENT_CODE","");//代理用户代码
			        val1.put("AGENT_TYPE","");//代理用户类别
			        val1.put("AGENT_ACCOUNT","");//代理用户账号
			        val1.put("UNIT_PRICE",DJZBItemVO[j].getHsdj()==null?16:String.format("%.2f",Double.valueOf(DJZBItemVO[j].getHsdj().toString())));//单价
			        val1.put("PRE_AMOUNT",0.00);//核销预付款金额lists.get(2)
			        val1.put("QANTITY",DJZBItemVO[j].getDfshl()==null?0.0:Double.valueOf(String.format("%.5f",Double.valueOf(DJZBItemVO[j].getDfshl().toString()))));//数量			   
			        val1.put("AMOUNT",DJZBItemVO[j].getDfbbwsje());//金额
			        val1.put("TAX_AMOUNT",DJZBItemVO[j].getDfybsj()==null?"":String.format("%.2f",Double.valueOf(DJZBItemVO[j].getDfybsj().toString())));//税额			      
//			        val1.put("ORDER_NO",listsval.get(1));//合同号2
			        if( listsval.get(0).equals("A")||listsval.get(1)==null&&listsval.get(1).length()==0){
			        	  val1.put("ORDER_NO","");//合同号
					   }else{
						   String djlx =  djlx(DJZBHeaderVO.getDjbh());
						   if("消耗汇总".equals(djlx.trim())){
							   val1.put("ORDER_NO","");  
						   }else{
						   val1.put("ORDER_NO",listsval.get(1));//合同号2
						   }
					   }			      
			        if( listsval.get(0).equals("A")||listsval.get(2)==null&&listsval.get(2).length()==0){
			        	  val1.put("ORDER_DESC","");//合同名称
					   }else{
						   String djlx =  djlx(DJZBHeaderVO.getDjbh());
						   if("消耗汇总".equals(djlx.trim())){
							   val1.put("ORDER_DESC","");   
						   }else{
						   val1.put("ORDER_DESC",listsval.get(2));//合同名称
						   }
					   }
			        val1.put("PROJECT_NO",DJZBItemVO[j].getXm()==null?"":DJZBItemVO[j].getXm());//项目号
			        val1.put("PRJECT_NAME",DJZBItemVO[j].getXm_name()==null?"":DJZBItemVO[j].getXm_name());//项目名称
			        val1.put("BPO_PROPERTY","");//合同属性代码
			        val1.put("BPO_TYPE","");//采购类型代码
			        Map map = this.QueryInvl(DJZBItemVO[j].getCinventoryid(),DJZBHeaderVO.getDwbm().trim());
			        String chbm = map.get("invcode").toString();
			        String invc =  invcl(chbm);
			        String invcs = invcls(chbm);
			        val1.put("FEE_TYPE",invc);//费用类型代码  截取物料编码的前四位
			        val1.put("FEE_DESC",invcs);//费用明细1		       
			        val1.put("MR_TYPE_SUB1",invcl(chbm));//业务申请类型1
			        val1.put("MR_TYPE_SUB2","");//业务申请类型2
			        val1.put("PROJECT_SPEC1","");//项目属性1
			        val1.put("PROJECT_SPEC2","");//项目属性2
			        val1.put("BUSI_TYPE1","");//业务类别一
			        val1.put("BUSI_TYPE2","");//业务类别二
			        val1.put("MAT_TYPE","");//存货类型
			        
			        val1.put("MAT_TYPE1",invc);//物料大类
			        val1.put("MAT_TYPE2","");//物料中类
			        val1.put("MAT_TYPE3","");//物料细类
			        
			        val1.put("MAT_CODE","");//物料代码
			        val1.put("TASK_CODE","");//作业区代码
			        val1.put("REPORT_TYPE","");//报支大类
			        val1.put("REPORT_TYPE_D","");//报支细类
			        val1.put("TEMP1","");//备用字符1
			        val1.put("TEMP2","");//备用字符2
			        val1.put("TEMP3","");//备用字符3
			        val1.put("TEMP4","");//备用字符4
			        val1.put("TEMP5","");//备用字符5
			        val1.put("TEMP6","");//备用字符6
			        val1.put("TEMP7","");//备用字符7
			        val1.put("TEMP8","");//备用字符8
			        val1.put("TEMP9","");//备用字符9
			        val1.put("TEMP10","");//备用字符10
			        val1.put("TEMP11",0.00);//备用字符11
			        val1.put("TEMP12",0.00);//备用字符12
			        val1.put("TEMP13",0.00);//备用字符13
			        val1.put("TEMP14",0.00);//备用字符14
			        val1.put("TEMP15",0.00);//备用字符15
			        bvals.add(val1);
			    }
			  val.put("bodylist", bvals);
			  JSONArray jarry = new JSONArray();
			  JSONObject js = new JSONObject();
			  for(int j=0;j<DJZBItemVO.length;j++){
				  List listres = queryks(DJZBHeaderVO.getVouchid(),DJZBHeaderVO.getDwbm());
				  List<String> listval = CubasdocVal(listres.get(0).toString());
				  js.put("EXPANSE_REPORT_ID",resval);//报支清单号
					js.put("BILL_TYPE_CODE","P04001");//单据类型
				    js.put("OWER_SYS_ID","JC");//来源系统
				    js.put("COMPANY_CODE",bzCode);//帐套
				    js.put("RECEIPT_NO","");//交接单号
				    js.put("RESP_UNIT_ID","");//主办单位
				    js.put("REPORT_DATE",DJZBHeaderVO.getDjrq()==null?"":DJZBHeaderVO.getDjrq().toString().replace("-", ""));//报支日期
				    js.put("BILL_DESC","报支摘要");//报支说明			  				 
				    js.put("REPORT_UNIT_ID",bzCode);//报支单位组织代码 1
				    js.put("REPORTOR_CODE","L00031");//提交人工号1  
				    js.put("INPUT_VOUCHER_ID","");//审核人工号 不取
				    js.put("INPUT_VOUCHER_TIME",DJZBHeaderVO.getShrq()==null?"":DJZBHeaderVO.getShrq().toString().replace("-", ""));//审核日期
				    js.put("SUPPLIER_CODE",listval.get(0));//供应商代码  
				    js.put("SUPPLIER_TYPE",listval.get(1));//供应商类型
				    js.put("SUPPLIER_NAME",listval.get(4));//供应商名称
				    if(listress.get(0).equals("无账号密码")||listress.get(1).toString()==null&&listress.get(1).toString().length()==0){
				    	 kszh="";
				    	 js.put("SUPPLIER_ACCOUNT","");//供应商帐号  银行账号1
				    }else{
				    	 kszh = listress.get(1).toString() ;
				    	 js.put("SUPPLIER_ACCOUNT",listress.get(1));//供应商帐号  银行账号1
				    }				   				    
				    js.put("SUPPLIER_ACCOUNT_ID","");//供应商帐号序号
				    js.put("SUPPLIER_TAX_NO",listval.get(5));//供应商税号    1
				    if(listress.get(0).equals("无账号密码")||listress.get(0).toString()==null&&listress.get(0).toString().length()==0){
				    js.put("SUPPLIER_BANK_NAME","");//供应商开户行名称
				    }else{
				    js.put("SUPPLIER_BANK_NAME",listress.get(0));//供应商开户行名称
				    }
				    js.put("SUPPLIER_BANK_TYPE","");//供应商银行类别
				    js.put("SUPPLIER_BANK_CODE","");//供应商银行地区码
				    js.put("SUPPLIER_ADDRESS",listval.get(2));//供应商地址
				    js.put("SUPPLIER_ZIP_CODE","");//供应商邮政编码
				    js.put("SUPPLIER_PHONE",listval.get(3));//供应商联系电话
				    js.put("SUPPLIER_EMAIL","");//供应商E-mail
				    if( listsval.get(0).equals("A")||listsval.get(0)==null&&listsval.get(0).length()==0){
			        	  js.put("CURRENCY_CODE","");//合同名称
					   }else{
						   js.put("CURRENCY_CODE",listsval.get(0));//币种
					   }
				    
				    js.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//报支总金额
				    js.put("Invoice_Tax_Amount",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//报支总税额
				    int fpsl = FPsl(DJZBHeaderVO.getDjbh());
				    js.put("Invoice_Num",fpsl);//发票数量
				    js.put("PREPAY_DATE","");//预计核销日期 
				    js.put("COUNTERACT_AMOUNT",0.00);//汇总核销预付款金额
				    js.put("REMAIN_AMOUNT",0.00);//汇总留尾款总额
				    js.put("ATTACH_DOC_NUM",0);//附件张数1
				    js.put("AGENTED_FLAG","");//代付标志
				    js.put("AGENTED_CODE","");//代付单位代码
				    js.put("TEMP1","");//备用字符1
				    js.put("TEMP2","");//备用字符2
				    js.put("TEMP3","");//备用字符3
				    js.put("TEMP4","");//备用字符4
				    js.put("TEMP5","");//备用字符5
				    jarry.add(js);
			  }
			  //如果是预付的话加上P4
			List listt =  hxmoney(DJZBHeaderVO.getDjbh());
			  JSONObject vals = new JSONObject();
			  if("Y".equals(listt.get(0))){			  
			    vals.put("BILL_NO",resval);//报支清单号
			    vals.put("COMPANY_CODE",bzCode);//帐套
			    vals.put("BILL_TYPE_CODE","P04001");//单据类型
			    vals.put("PRE_BILL_NO","");//核销清单号    预支清单号
			    vals.put("PRE_INV_NO","");//核销预付款发票号   虚拟
			    vals.put("PRE_INV_CODE","");//核销预付款发票代码   虚拟
			    if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
			    	vals.put("INVOICE_NO","");//发票号
				    }else{
				    	vals.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//发票号
				    }
				   if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
					   vals.put("INVOICE_CODE","");//发票代码
				   }else{
					   vals.put("INVOICE_CODE",listsval.get(4));//发票代码
				   }
//			    val1.put("INVOICE_NO","");//发票号
//			    val1.put("INVOICE_CODE","");//发票代码
			    vals.put("ORDER_NUM","");//核销合同号
			    vals.put("PROJECT_NO","");//核销项目号
			    vals.put("CAV_AMT",listt.get(1));//核销金额
			    vals.put("CAV_DATE",listt.get(2));//核销日期
			    vals.put("TEMP1","");//备用字符1
			    vals.put("TEMP2","");//备用字符2
			    vals.put("TEMP3","");//备用字符3
			    vals.put("TEMP4","");//备用字符4
			    vals.put("TEMP5","");//备用字符5
			  }
			  if(str2.length()>0){
				 str3.append(str2); 
				  continue;
			  	}
			  if(DJZBHeaderVO.getZgyf()!=0){
					str2.append("\n"+"暂估应付处请确认应付");
				}
			  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName());
			  JSONArray jsrr = new JSONArray();
			  for(int k=0;k<DJZBItemVO.length;k++){
			  JSONObject b = ifc.assembleItfData(bvals.getJSONObject(k),"PUINVOICE_B_SEND",null);//表体
			  jsrr.add(b);
			  }
			  JSONObject s = ifc.assembleItfData(val, "PUINVOICE_H_SEND","PUINVOICE_B_SEND"); //表头
			  
			  JSONObject a = ifc.assembleItfData(js, "REPROTLIST_SEND",null);//报支
			  List list1 = new ArrayList();//p4接口
			  StringBuffer str2s = new StringBuffer();
			  if("Y".equals(listt.get(0))){	
				  JSONObject b = ifc.assembleItfData(vals, "ADVANCEPAYMENT",null); //P4
				  String state = (String) b.get("state");
				  list1.add(0,b);
				  if(!"success".equals(state)){
						str2s.append("err");
					
					}
			  }
			  //JSONObject js = ifc.disassembleItfData(xbuss, "BCJCS1");//【解析接口示例】 
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
		      
		       JSONObject jsObject =(JSONObject)JSONArrays.get(0);//采购发票表头
		       JSONObject jsObject1 =(JSONObject)JSONArrays.get(1);//采购发票明细信息
		       JSONObject jsObject2 =(JSONObject)Jar.get(0);//报支清单信息接口	        
		       sjrr.add(0, jsObject2);		       
		       sjrr.add(1, jsObject);
		       sjrr.add(2, jsrr);
		       if("Y".equals(listt.get(0))){	
		    	String val2s =   JSONObject.fromObject(list1.get(0)).getString("content");
		    	JSONArray jars =  JSONArray.fromObject(val2s);
		    	sjrr.add(2,(JSONObject)jars.get(0));//预付核销
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
					                  //失败逻辑 
					            	  showWarningMessage("发送失败");
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
			                  //失败逻辑 
			            	  showWarningMessage("发送失败");
			              } 
			          } 
			      } 
			  } else if(!"success".equals(state)&&"success".equals(states)){
				  str2.append("\n"+"失败"+content);
			  }else if("success".equals(state)&&!"success".equals(states)){
				  str2.append("\n"+"失败"+contents);
			  }else{
				  str2.append("\n"+"失败"+content+"  "+contents);
			  }
				
			  //成功逻辑	
			  if(list1.size()!=0){
			  str.append("\n"+"单据号"+DJZBHeaderVO.getDjbh()+"成功");
			  IUAPQueryBS bss = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			  String vall =BalnkVal(dJZBItemVOs[0].getKsbm_cl());
			  String value = "select account from bd_custbank  where pk_cubasdoc ='"+vall+"' ";            	  
			  try {
				String val1 = SERIALNUM(dJZBHeaderVOs.getDjbh());
				  @SuppressWarnings("unused")
				String result =(String) bss.executeQuery(value, new BeanProcessor(String.class));
				  IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
				  ItfbzVO.setBillno_bz(val1);//报支清单号
		    	  ItfbzVO.setBillno_yf(dJZBHeaderVOs.getDjbh());//应付单号
		    	  dJZBHeaderVOs.getKsbm_cl();
		    	  dJZBItemVOs[0].getKsbm_cl();
		    	  ItfbzVO.setVouchid(dJZBHeaderVOs.getVouchid());//应付单主键
		    	  ItfbzVO.setKsbm_cl(dJZBItemVOs[0].getKsbm_cl());//客商管理主键
		    	  ItfbzVO.setGys_account(kszh);//供应商银行账号
		    	  ItfbzVO.setPk_corp(dJZBHeaderVOs.getDwbm());//公司		            									
				  iVOPersistence.insertVO(ItfbzVO);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
			  }
				 }else{
					  str1.append("单据编号"+dJZBHeaderVOs.getDjbh()+"已经发送成功。无需再次发送");
//					  showWarningMessage("已经发送成功。无需再次发送");
				  }
				}
				if(str1!=null&&str1.length()>0&&str3!=null&&str3.length()>0&&str!=null&&str.toString().length()>0){
					showWarningMessage(str+"\n"+str1+"。"+"\n"+str3);
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
		  
		//卡片模式下ZSH
			public void CxbusCard(DJZBVO vo,DjPanel djPanel){
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String nyr = sdf.format(date);	//获取当前系统时间	
				List list = new ArrayList();
				String kszh = new String();
				StringBuffer str1 = new StringBuffer();//已传单据
				StringBuffer str2 = new StringBuffer();//异常
				 ITFBZVO ItfbzVO = new ITFBZVO();
//			    DJZBHeaderVO dJZBHeaderVOs = (DJZBHeaderVO)vos.get(0).getParentVO();//获取选中行数的表头
//				DJZBItemVO[] dJZBItemVOs = (DJZBItemVO[])vos.get(0).getChildrenVO();//获取选中行的表体
				DJZBHeaderVO DJZBHeaderVO = (DJZBHeaderVO)vo.getParentVO();
				DJZBItemVO[] DJZBItemVO = (DJZBItemVO[])vo.getChildrenVO();			
				
				
					if(DJZBHeaderVO.getDjbh()==null||DJZBHeaderVO.getDjbh().equals("")||DJZBHeaderVO.getDjbh().toString().length()==0){
						showWarningMessage("单据号未填写");
						return;
					}
				String billno=judgeDJBH(DJZBHeaderVO.getDjbh().toString());
				  System.out.println("billno:"+billno);
				  if(billno == null){	
					
						//校验数据
						StringBuffer resval2 =	CheckData( DJZBHeaderVO, DJZBItemVO);
						if(resval2!=null&&resval2.toString().length()>0){
							str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据号下"+resval2.toString());
//							showWarningMessage("在"+DJZBHeaderVO.getDjbh()+"单据号下下"+resval2.get(1).toString());
//							continue;
						}
						String bzCode = BzCode(DJZBHeaderVO.getDwbm());	
						List listress =SelectBank(DJZBItemVO[0].getKsbm_cl(),DJZBHeaderVO.getDwbm());
						if(listress.get(0).equals("无账号密码")){
							  str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据号下客商账号密码未填写");
//							  showWarningMessage("客商账号密码未填写");
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
						StringBuffer lists3 =ListCheck(listvall);//校验客商数据是否为空
					
						if(lists3!=null&&lists3.toString().length()>0){
							str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+lists3.toString());
//							 showWarningMessage(lists3.get(1).toString());
//							 continue;
							}
						
//						List<String> lists =ListVal(DJZBHeaderVO.getDjbh());
						List<String> listsval =listVal(DJZBHeaderVO.getDjbh());
						if(listsval.get(0).equals("A")){
							str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据下，币种，合同号，合同名称，发票号，发票代码为空，请填写");
						}else{
						StringBuffer listsval2 = checkListval(listsval);
						if(listsval2!=null&listsval2.toString().length()>0){
							str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据号下"+listsval2);
//							 showWarningMessage(lists3.get(1).toString());
//							 continue;
						}
						}
						JSONObject val = new JSONObject();
						String resval =	SERIALNUM(DJZBHeaderVO.getDjbh());
					    val.put("EXPANSE_REPORT_ID",resval);//报支清单号
					    val.put("COMPANY_CODE",bzCode);//帐套
					    val.put("BILL_TYPE_CODE","P04001");//单据类型 DJZBHeaderVO.getYwbm()
					    System.out.println(DJZBHeaderVO.getVouchid());
					    val.put("OWER_SYS_ID","JC");//来源系统
					   if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
						val.put("INVOICE_NO","");//发票号
					    }else{
					    val.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//发票号
					    }
					   if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
						val.put("INVOICE_CODE","");//发票代码
					   }else{
					    val.put("INVOICE_CODE",listsval.get(4));//发票代码
					   }
					    String Ftype = Fptype(DJZBHeaderVO.getZyx28());
					    if(Ftype==null){
//					    	str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据号下发票种类为空");
					    	 val.put("INVOICE_TYPE","");//发票种类
					    }else{
					    val.put("INVOICE_TYPE",Ftype);//发票种类
					    }
//					    val.put("INVOICE_TYPE",Ftype);//发票种类
					    val.put("INVOICE_DATE",DJZBHeaderVO.getDjrq().toString().replace("-", ""));//开票日期
					   String sl = Sl(DJZBHeaderVO.getDjbh());
					    val.put("TAX_RATE",Double.valueOf(sl));//税率 1
					    UFDouble dJZBItemVO = new UFDouble();
					    UFDouble Dfybsj = new UFDouble();
					    for(int j=0;j<DJZBItemVO.length;j++){
					    	 dJZBItemVO = DJZBItemVO[j].getDfbbwsje().add(dJZBItemVO) ;
					    	 Dfybsj =DJZBItemVO[j].getDfybsj().add(Dfybsj);
					    }
					    
//					    val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())/100));//发票金额
//					    val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())/100));//发票税额
					    UFDouble dfybje =  DJZBItemVO[0].getDfybje();
						UFDouble dfybsj =  DJZBItemVO[0].getDfybsj();
						UFDouble fpje = dfybsj.add(dfybje);
						val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//发票金额
						val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//发票税额
//						val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(fpje.toString())));//发票金额
//					    val.put("INVOICE_TAX_AMOUNT",DJZBItemVO[0].getDfybsj());//发票税额
					    val.put("PREPAY_DATE","");//预计付款日期
					    val.put("SETTLEMENT_TYPE","A10");//结算方式
					    val.put("COUNTERACT_AMOUNT",0.00);//核销预付款金额lists.get(2)
					    val.put("CAN_END_AMT_CUR",0.00);//留尾款金额
					    val.put("PAYEE_CODE",listvall.get(0));//受款人代码  1
					    val.put("PAYEE_TYPE",listvall.get(1));//受款人类型
					    val.put("PAYEE_NAME",listvall.get(4));//受款人名称
					    if(listress.get(0).equals("无账号密码")||listress.get(1).toString().length()==0&&listress.get(1)==null){
					    val.put("PAYEE_ACCOUNT","");//受款人帐号
					    }else{
					    val.put("PAYEE_ACCOUNT",listress.get(1));//受款人帐号
					    }
					    val.put("PAYEE_ACCOUNT_ID","");//受款人帐号序号
					    val.put("PAYEE_TAX_NO",listvall.get(5));//受款人税号
					    if(listress.get(0).equals("无账号密码")||listress.get(0).toString().length()==0&&listress.get(0)==null){
					    val.put("PAYEE_ACCOUNT_NAME","");//受款人开户行名称
					    }else{
					    val.put("PAYEE_ACCOUNT_NAME",listress.get(0));//受款人开户行名称
					    }
					    val.put("PAYEE_BANK_TYPE","");//受款人银行类别
					    val.put("PAYEE_BANK_CODE","");//受款人银行地区码
					    val.put("COUNTRY","");//受款人省市/国别
					    val.put("PAYEE_ADDRESS",listvall.get(2));//受款人受款人地址
					    val.put("PAYEE_ZIP_CODE","");//受款人邮政编码
					    val.put("PAYEE_PHONE",listvall.get(3));//受款人联系电话
					    val.put("PAYEE_EMAIL","");//受款人E-mail
					    val.put("LC_NO","");//信用证号
					    val.put("DETAIL_NUM",DJZBItemVO.length);//发票明细记录数 1
					    val.put("TEMP1","");//备用字符1
					    val.put("TEMP2","");//备用字符2
					    val.put("TEMP3","");//备用字符3
					    val.put("TEMP4","");//备用字符4
					    val.put("TEMP5","");//备用字符5
					    JSONArray bvals = new JSONArray();
//					    int rows = 2; //请把rows改成你表体的行数
					    for (int j = 0; j < DJZBItemVO.length; j++) {	
					    	
					    	JSONObject val1 = new JSONObject();
					    	String resval1 =SERIALNUM(DJZBHeaderVO.getDjbh());
					    	System.out.println("报支："+resval1);
					        val1.put("BILL_NO",resval1);//报支清单号
					        val1.put("COMPANY_CODE",bzCode);//帐套}
					        val1.put("BILL_TYPE_CODE","P04001");//单据类型
					        val1.put("OWER_SYS_ID","JC");//来源系统
//					        val1.put("INVOICE_CODE",listsval.get(4));//发票代码
					        if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
					        	val1.put("INVOICE_CODE","");//发票代码
							   }else{
								   val1.put("INVOICE_CODE",listsval.get(4));//发票代码
							   }
					        if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
					        	val1.put("INVOICE_NO","");//发票号
							    }else{
							    val1.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//发票号
							    }
//					        val1.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//发票号
					        val1.put("INVOICE_DETAIL_ID",j+1);//发票明细行序号		        
					        String zrzx = queryzrzx(DJZBItemVO[j].getDeptid());
					        if(zrzx.length()>8){
					        	if(j<1){
					        	str2.append("\n"+"在"+DJZBHeaderVO.getDjbh()+"单据下,部门请选择最新的部门");
					        	}
					        	val1.put("COST_CENTER","");//责任中心			        	
					        }else{
					        val1.put("COST_CENTER",zrzx);//责任中心
					        }
					        val1.put("COST_CENTER",zrzx);//责任中心
					        val1.put("AGENTED_FLAG","");//代付标志
					        val1.put("AGENTED_CODE","");//代付单位代码
					        val1.put("AGENT_FLAG","");//代理标记
					        val1.put("AGENT_CODE","");//代理用户代码
					        val1.put("AGENT_TYPE","");//代理用户类别
					        val1.put("AGENT_ACCOUNT","");//代理用户账号
					        val1.put("UNIT_PRICE",DJZBItemVO[j].getHsdj()==null?16:String.format("%.2f",Double.valueOf(DJZBItemVO[j].getHsdj().toString())));//单价
					        val1.put("PRE_AMOUNT",0.00);//核销预付款金额lists.get(2)
					        val1.put("QANTITY",DJZBItemVO[j].getDfshl()==null?0.0:Double.valueOf(String.format("%.5f",Double.valueOf(DJZBItemVO[j].getDfshl().toString()))));//数量
					        val1.put("AMOUNT",DJZBItemVO[j].getDfbbwsje());//金额
					        val1.put("TAX_AMOUNT",DJZBItemVO[j].getDfybsj()==null?"":String.format("%.2f",Double.valueOf(DJZBItemVO[j].getDfybsj().toString())));//税额			      
//					        val1.put("ORDER_NO",listsval.get(1));//合同号2
					        if( listsval.get(0).equals("A")||listsval.get(1)==null&&listsval.get(1).length()==0){
					        	  val1.put("ORDER_NO","");//合同名称
							   }else{
								   String djlx =  djlx(DJZBHeaderVO.getDjbh());
								   if("消耗汇总".equals(djlx.trim())){
									 val1.put("ORDER_NO","");//合同名称
								   }else{
								   val1.put("ORDER_NO",listsval.get(1));//合同号2
								   }
							   }			      
					        if( listsval.get(0).equals("A")||listsval.get(2)==null&&listsval.get(2).length()==0){
					        	  val1.put("ORDER_DESC","");//合同名称
							   }else{
								   String djlx =  djlx(DJZBHeaderVO.getDjbh());
								   if("消耗汇总".equals(djlx.trim())){
									   val1.put("ORDER_DESC","");//合同名称  
								   }else{
								   val1.put("ORDER_DESC",listsval.get(2));//合同名称
								   }
							   }
					        val1.put("PROJECT_NO",DJZBItemVO[j].getXm()==null?"":DJZBItemVO[j].getXm());//项目号
					        val1.put("PRJECT_NAME",DJZBItemVO[j].getXm_name()==null?"":DJZBItemVO[j].getXm_name());//项目名称
					        val1.put("BPO_PROPERTY","");//合同属性代码
					        val1.put("BPO_TYPE","");//采购类型代码
					        Map map = this.QueryInvl(DJZBItemVO[j].getCinventoryid(),DJZBHeaderVO.getDwbm().trim());
					        String chbm = map.get("invcode").toString();
					        String invc =  invcl(chbm);
					        String invcs = invcls(chbm);
					        val1.put("FEE_TYPE",invc);//费用类型代码  截取物料编码的前四位
					        val1.put("FEE_DESC",invcs);//费用明细1
					     
					       
					        val1.put("MR_TYPE_SUB1",invcl(chbm));//业务申请类型1
					        val1.put("MR_TYPE_SUB2","");//业务申请类型2
					        val1.put("PROJECT_SPEC1","");//项目属性1
					        val1.put("PROJECT_SPEC2","");//项目属性2
					        val1.put("BUSI_TYPE1","");//业务类别一
					        val1.put("BUSI_TYPE2","");//业务类别二
					        val1.put("MAT_TYPE","");//存货类型
					        
					        val1.put("MAT_TYPE1",invc);//物料大类
					        val1.put("MAT_TYPE2","");//物料中类
					        val1.put("MAT_TYPE3","");//物料细类
					        
					        val1.put("MAT_CODE","");//物料代码
					        val1.put("TASK_CODE","");//作业区代码
					        val1.put("REPORT_TYPE","");//报支大类
					        val1.put("REPORT_TYPE_D","");//报支细类
					        val1.put("TEMP1","");//备用字符1
					        val1.put("TEMP2","");//备用字符2
					        val1.put("TEMP3","");//备用字符3
					        val1.put("TEMP4","");//备用字符4
					        val1.put("TEMP5","");//备用字符5
					        val1.put("TEMP6","");//备用字符6
					        val1.put("TEMP7","");//备用字符7
					        val1.put("TEMP8","");//备用字符8
					        val1.put("TEMP9","");//备用字符9
					        val1.put("TEMP10","");//备用字符10
					        val1.put("TEMP11",0.00);//备用字符11
					        val1.put("TEMP12",0.00);//备用字符12
					        val1.put("TEMP13",0.00);//备用字符13
					        val1.put("TEMP14",0.00);//备用字符14
					        val1.put("TEMP15",0.00);//备用字符15
					        bvals.add(val1);
					    }
					  val.put("bodylist", bvals);
					  JSONArray jarry = new JSONArray();
					  JSONObject js = new JSONObject();
					  for(int j=0;j<DJZBItemVO.length;j++){
						  List listres = queryks(DJZBHeaderVO.getVouchid(),DJZBHeaderVO.getDwbm());
						  List<String> listval = CubasdocVal(listres.get(0).toString());
						  js.put("EXPANSE_REPORT_ID",resval);//报支清单号
							js.put("BILL_TYPE_CODE","P04001");//单据类型
						    js.put("OWER_SYS_ID","JC");//来源系统
						    js.put("COMPANY_CODE",bzCode);//帐套
						    js.put("RECEIPT_NO","");//交接单号
						    js.put("RESP_UNIT_ID","");//主办单位
						    js.put("REPORT_DATE",DJZBHeaderVO.getDjrq()==null?"":DJZBHeaderVO.getDjrq().toString().replace("-", ""));//报支日期
						    js.put("BILL_DESC","报支摘要");//报支说明			  
						 
						    js.put("REPORT_UNIT_ID",bzCode);//报支单位组织代码 1
						    js.put("REPORTOR_CODE","L00031");//提交人工号1  
						    js.put("INPUT_VOUCHER_ID","");//审核人工号 不取
						    js.put("INPUT_VOUCHER_TIME",DJZBHeaderVO.getShrq()==null?"":DJZBHeaderVO.getShrq().toString().replace("-", ""));//审核日期
						    js.put("SUPPLIER_CODE",listval.get(0));//供应商代码  
						    js.put("SUPPLIER_TYPE",listval.get(1));//供应商类型
						    js.put("SUPPLIER_NAME",listval.get(4));//供应商名称
						    if(listress.get(0).equals("无账号密码")||listress.get(1).toString()==null&&listress.get(1).toString().length()==0){
						    	 kszh="";
						    	 js.put("SUPPLIER_ACCOUNT","");//供应商帐号  银行账号1
						    }else{
						    	 kszh = listress.get(1).toString() ;
						    	 js.put("SUPPLIER_ACCOUNT",listress.get(1));//供应商帐号  银行账号1
						    }				   				    
						    js.put("SUPPLIER_ACCOUNT_ID","");//供应商帐号序号
						    js.put("SUPPLIER_TAX_NO",listval.get(5));//供应商税号    1
						    if(listress.get(0).equals("无账号密码")||listress.get(0).toString()==null&&listress.get(0).toString().length()==0){
						    js.put("SUPPLIER_BANK_NAME","");//供应商开户行名称
						    }else{
						    js.put("SUPPLIER_BANK_NAME",listress.get(0));//供应商开户行名称
						    }
						    js.put("SUPPLIER_BANK_TYPE","");//供应商银行类别
						    js.put("SUPPLIER_BANK_CODE","");//供应商银行地区码
						    js.put("SUPPLIER_ADDRESS",listval.get(2));//供应商地址
						    js.put("SUPPLIER_ZIP_CODE","");//供应商邮政编码
						    js.put("SUPPLIER_PHONE",listval.get(3));//供应商联系电话
						    js.put("SUPPLIER_EMAIL","");//供应商E-mail
						    if( listsval.get(0).equals("A")||listsval.get(0)==null&&listsval.get(0).length()==0){
					        	  js.put("CURRENCY_CODE","");//合同名称
							   }else{
								   js.put("CURRENCY_CODE",listsval.get(0));//币种
							   }
//						    val.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//发票金额
//						    val.put("INVOICE_TAX_AMOUNT",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//发票税额
						    js.put("INVOICE_AMOUNT",String.format("%.2f",Double.valueOf(dJZBItemVO.toString())));//报支总金额
						    js.put("Invoice_Tax_Amount",String.format("%.2f",Double.valueOf(Dfybsj.toString())));//报支总税额
						    int fpsl = FPsl(DJZBHeaderVO.getDjbh());
						    js.put("Invoice_Num",fpsl);//发票数量
						    js.put("PREPAY_DATE","");//预计核销日期 
						    js.put("COUNTERACT_AMOUNT",0.00);//汇总核销预付款金额
						    js.put("REMAIN_AMOUNT",0.00);//汇总留尾款总额
						    js.put("ATTACH_DOC_NUM",0);//附件张数1
						    js.put("AGENTED_FLAG","");//代付标志
						    js.put("AGENTED_CODE","");//代付单位代码
						    js.put("TEMP1","");//备用字符1
						    js.put("TEMP2","");//备用字符2
						    js.put("TEMP3","");//备用字符3
						    js.put("TEMP4","");//备用字符4
						    js.put("TEMP5","");//备用字符5
						    jarry.add(js);
					  }
					  JSONObject vals = new JSONObject();
					  List listt =  hxmoney(DJZBHeaderVO.getDjbh());
					  if("Y".equals(listt.get(0))){
					 
					    vals.put("BILL_NO",resval);//报支清单号
					    vals.put("COMPANY_CODE",bzCode);//帐套
					    vals.put("BILL_TYPE_CODE","P04001");//单据类型
					    vals.put("PRE_BILL_NO","");//核销清单号
					    vals.put("PRE_INV_NO","");//核销预付款发票号
					    vals.put("PRE_INV_CODE","");//核销预付款发票代码
					    if( listsval.get(0).equals("A")||listsval.get(3)==null&&listsval.get(3).length()==0){
					    	vals.put("INVOICE_NO","");//发票号
						    }else{
						    	vals.put("INVOICE_NO",listsval.get(3)==null?"":listsval.get(3));//发票号
						    }
						   if( listsval.get(0).equals("A")||listsval.get(4)==null&&listsval.get(4).length()==0){
							   vals.put("INVOICE_CODE","");//发票代码
						   }else{
							   vals.put("INVOICE_CODE",listsval.get(4));//发票代码
						   }
//					    val1.put("INVOICE_NO","");//发票号
//					    val1.put("INVOICE_CODE","");//发票代码
					    vals.put("ORDER_NUM","");//核销合同号
					    vals.put("PROJECT_NO","");//核销项目号
					    vals.put("CAV_AMT",listt.get(1));//核销金额
					    vals.put("CAV_DATE",listt.get(2));//核销日期
					    vals.put("TEMP1","");//备用字符1
					    vals.put("TEMP2","");//备用字符2
					    vals.put("TEMP3","");//备用字符3
					    vals.put("TEMP4","");//备用字符4
					    vals.put("TEMP5","");//备用字符5

					  }
					  
					  
					if(str2.length()>0&&str2!=null){
						showWarningMessage(str2.toString());
						return;
					}
					if(DJZBHeaderVO.getZgyf()!=0){
						str2.append("\n"+"暂估应付处请确认应付");
					}
						  
					  IInterfaceCheck ifc = (IInterfaceCheck)NCLocator.getInstance().lookup(IInterfaceCheck.class.getName()); 
					  JSONObject s = ifc.assembleItfData(val, "PUINVOICE_H_SEND","PUINVOICE_B_SEND");//表头
					  JSONArray jsrr = new JSONArray();
					  for(int k=0;k<DJZBItemVO.length;k++){
						  JSONObject b = ifc.assembleItfData(bvals.getJSONObject(k),"PUINVOICE_B_SEND",null);//表体
						  jsrr.add(b);
						  }
					  JSONObject a = ifc.assembleItfData(js, "REPROTLIST_SEND",null);//报支
					  List list1 = new ArrayList();//p4接口
					  StringBuffer str2s = new StringBuffer();
					  if("Y".equals(listt.get(0))){	
						  JSONObject b = ifc.assembleItfData(vals, "ADVANCEPAYMENT",null); //P4
						  String state = (String) b.get("state");
						  list1.add(0,b);
						  if(!"success".equals(state)){
								str2s.append("err");
							
							}
					  }
					  //JSONObject js = ifc.disassembleItfData(xbuss, "BCJCS1");//【解析接口示例】 
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
				       JSONObject jsObject =(JSONObject)JSONArrays.get(0);//采购发票表头
				       JSONObject jsObject1 =(JSONObject)JSONArrays.get(1);//采购发票明细信息
				       JSONObject jsObject2 =(JSONObject)Jar.get(0);//报支清单信息接口	        
				       sjrr.add(0, jsObject2);
				       sjrr.add(1, jsObject);
				       sjrr.add(2, jsrr);
				       if("Y".equals(listt.get(0))){	
					    	String val2s =   JSONObject.fromObject(list1.get(0)).getString("content");
					    	JSONArray jars =  JSONArray.fromObject(val2s);
					    	sjrr.add(2,(JSONObject)jars.get(0));//预付核销
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
							                  //失败逻辑 
							            	  showWarningMessage("发送失败");
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
					                  //失败逻辑 
					            	  showWarningMessage("发送失败");
					              } 
					          } 
					      } 
					  } else if(!"success".equals(state)&&"success".equals(states)){
						  str2.append("\n"+"失败"+content);
					  }else if("success".equals(state)&&!"success".equals(states)){
						  str2.append("\n"+"失败"+contents);
					  }else{
						  str2.append("\n"+"失败"+content+"  "+contents);
					  }
						
					  }
					  //成功逻辑	
					  if(list.size()!=0){
					 showWarningMessage("单据号"+DJZBHeaderVO.getDjbh()+"发送成功");
					  IUAPQueryBS bss = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					  String vall =BalnkVal(DJZBItemVO[0].getKsbm_cl());
					  String value = "select account from bd_custbank  where pk_cubasdoc ='"+vall+"' ";            	  
					  try {
						String val1 = SERIALNUM(DJZBHeaderVO.getDjbh());
						  @SuppressWarnings("unused")
						String result =(String) bss.executeQuery(value, new BeanProcessor(String.class));
						  IVOPersistence iVOPersistence = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
						  ItfbzVO.setBillno_bz(val1);//报支清单号
				    	  ItfbzVO.setBillno_yf(DJZBHeaderVO.getDjbh());//应付单号
				    	  DJZBHeaderVO.getKsbm_cl();
				    	  DJZBItemVO[0].getKsbm_cl();
				    	  ItfbzVO.setVouchid(DJZBHeaderVO.getVouchid());//应付单主键
				    	  ItfbzVO.setKsbm_cl(DJZBItemVO[0].getKsbm_cl());//客商管理主键
				    	  ItfbzVO.setGys_account(kszh);//供应商银行账号
				    	  ItfbzVO.setPk_corp(DJZBHeaderVO.getDwbm());//公司		            									
						  iVOPersistence.insertVO(ItfbzVO);
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							}
					  
					  
						 }else{
		       	  showWarningMessage("已经发送成功。无需再次发送");
				 }
				 			  
				
				 if(str1!=null&&str1.length()>0&str2!=null&&str2.length()>0){
						showWarningMessage(str1+"。"+"\n"+str2);
					}else if(str1!=null&&str1.length()>0){
						showWarningMessage(str1.toString());
					}
				  }
		
		//校验字段的值是否为空
		public StringBuffer CheckData(DJZBHeaderVO DJZBHeaderVO,DJZBItemVO[] DJZBItemVO){
			StringBuffer str = new StringBuffer();
		
			if(DJZBHeaderVO.getZyx28()==null||DJZBHeaderVO.getZyx28().equals("")||DJZBHeaderVO.getZyx28().length()<=0){
				str.append("\n"+"发票种类为空,请填写");
//				list.add(0,"0");
//				list.add(1,"发票种类为空,请填写");
//				return list;
			}else if(DJZBHeaderVO.getVouchid()==null||DJZBHeaderVO.getVouchid().equals("")||DJZBHeaderVO.getVouchid().length()<=0){	
				str.append("\n"+"发票主键为空,请填写");
//				list.add(0,"0");
//				list.add(1,"发票主键为空,请填写");
//				return list;
			}else if(DJZBHeaderVO.getDjbh()==null||DJZBHeaderVO.getDjbh().equals("")||DJZBHeaderVO.getDjbh().length()<=0){
				str.append("\n"+"单据编号为空，请填写");
//				list.add(0,"0");
//				list.add(1,"单据编号为空,请填写");
//				return list;
			}else if(DJZBHeaderVO.getDwbm()==null||DJZBHeaderVO.getDwbm().equals("")||DJZBHeaderVO.getDwbm().length()<=0){
				str.append("\n"+"公司为空,请填写");
//				list.add(0,"0");
//				list.add(1,"公司为空,请填写");
//				return list;
			}else if(DJZBItemVO[0].getKsbm_cl()==null||DJZBItemVO[0].getKsbm_cl().equals("")||DJZBItemVO[0].getKsbm_cl().length()<=0){
				str.append("\n"+"单据日期为空,请填写");
//				list.add(0,"0");
//				list.add(1,"单据日期为空,请填写");
//				return list;
			}else if(DJZBHeaderVO.getDjrq()==null||DJZBHeaderVO.getDjbh().equals("")||DJZBHeaderVO.getDjbh().length()<=0){
				str.append("\n"+"单据日期为空,请填写");
//				list.add(0,"0");
//				list.add(1,"单据日期为空,请填写");
//				return list;
			}else if(DJZBItemVO[0].getDfybje()==null||DJZBItemVO[0].getDfybje().equals("")||DJZBItemVO[0].getDfybje().toString().length()<=0){
				str.append("\n"+"金额为空,请填写");
//				list.add(0,"0");
//				list.add(1,"金额为空,请填写");
//				return list;
			}else if(DJZBItemVO[0].getDfybsj()==null||DJZBItemVO[0].getDfybsj().equals("")||DJZBItemVO[0].getDfybsj().toString().length()<=0){
				str.append("\n"+"税额为空,请填写");
//				list.add(0,"0");
//				list.add(1,"税额为空,请填写");
			}else if(DJZBItemVO[0].getDeptid()==null||DJZBItemVO[0].getDeptid().equals("")||DJZBItemVO[0].getDeptid().toString().length()<=0){
				str.append("\n"+"部门ID为空,请填写");
//				showWarningMessage("部门ID为空,请填写");
//				list.add(0,"0");
//				list.add(1,"部门ID为空,请填写");
//				return list;
			}else if(DJZBItemVO[0].getCinventoryid()==null||DJZBItemVO[0].getCinventoryid().equals("")||DJZBItemVO[0].getCinventoryid().toString().length()<=0){
				str.append("\n"+"存货基本档案pk为空,请填写");
//				list.add(0,"0");
//				list.add(1,"存货基本档案pk为空,请填写");
//				return list;
			}else if(DJZBItemVO[0].getHsdj()==null||DJZBItemVO[0].getHsdj().equals("")||DJZBItemVO[0].getHsdj().toString().length()<=0){
				str.append("\n"+"含税单价为空,请填写");
//				list.add(0,"0");
//				list.add(1,"含税单价为空,请填写");
//				return list;
			}else if(DJZBHeaderVO.getShrq()==null||DJZBHeaderVO.getShrq().equals("")){
				str.append("\n"+"审核日期为空,请填写");
//				list.add(0,"0");
//				list.add(1,"审核日期为空,请填写");
//				return list;
//			}else if(DJZBItemVO[0].getZy()==null||DJZBItemVO[0].getZy().length()<=0||DJZBItemVO[0].getZy().equals("")){
//				str.append("\n"+"摘要为空，请填写");
//			}
			}
			return str;
		
		}
		//判断客商是否为空
		public StringBuffer ListCheck(List list){
			List lists= new ArrayList();
			StringBuffer str2 = new StringBuffer();
			if(list.get(0).equals("")||list.get(0).toString().length()!=6){
				str2.append("没有对应的MDM客商编码,请填写");
//				lists.add(0,"0");
//				lists.add(1,"没有对应的MDM客商编码,请填写");
//				return lists;
			}else if(list.get(1).equals(""))
			{
				str2.append(" 客商类型为空,请填写");
//				lists.add(0,"0");
//				lists.add(1,"客商类型为空,请填写");
//				return lists;
			}else if(list.get(2).equals("")){
				str2.append(" 发货地址名称为空,请填写");
//				lists.add(0,"0");
//				lists.add(1," 发货地址名称为空,请填写");
//				return lists;
			}else if(list.get(4).equals("")){
				str2.append(" 客商编码为空,请填写");
//				lists.add(0,"0");
//				lists.add(1,"客商编码为空,请填写");
//				return lists;
			}else if(list.get(5).equals("")){
				str2.append(" 客商税号为空,请填写");
//				lists.add(0,"0");
//				lists.add(1,"客商税号为空,请填写");
//				return lists;
			}
			return str2;
			
		}
		/**
		 * 检验字段是否为空
		 * 销售发票头
		 * wy
		 * 2019/10/29
		 */
		  public List judgeNXFPT(DJZBHeaderVO hvo,DJZBItemVO[] bvo,String nyr,String djbh,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble jfshl,UFDouble Jfybwsje,UFDouble dj,UFDouble Jfybje,String sl){
			  List<String> list = new ArrayList<String>();
			  Map kpTaxCode = kpTaxCode(hvo.getDwbm());
			  Map BodyMap = selectCubasdoc(djbh);
			  String nx = "销售发票（内销）";
			  list.add(0,"1");
			  if(kpTaxCode.get("def8")==null||"".equals(kpTaxCode.get("def8"))||kpTaxCode.get("def8").toString().length()<=0){
				  list.add(0,"0");
				  list.add(1,"公司别(账套代码)为空，请维护");
				  return list;
			  }else if(queryzrzx(bvo[0].getDeptid())== null ||"".equals(queryzrzx(bvo[0].getDeptid()))){
				  list.add(0,"0");
				  list.add(1,"责任中心为空，请维护 ");
				  return list;
			  }else if(DJLX(nx)==null || "".equals(DJLX(nx)==null)){
				  list.add(0,"0");
				  list.add(1,"单据类型为空，请维护 ");
				  return list;
			  }else if (hvo.getDjbh()==null || "".equals(hvo.getDjbh())) {
				  list.add(0,"0");
				  list.add(1,"业务单据号 为空，请维护 ");
				  return list;
			  }else if (hvo.getZyx17()==null || "".equals(hvo.getZyx17())) {
				  list.add(0,"0");
				  list.add(1,"纸质发票号为空，请维护 ");
				  return list;
			  }else if (BodyMap.get("ccreditnum")==null || "".equals(BodyMap.get("ccreditnum"))) {
				  list.add(0,"0");
				  list.add(1,"发票代码为空，请维护 ");
				  return list;
			  }else if (fplx(hvo.getZyx28())==null || "".equals(fplx(hvo.getZyx28()))) {
				  list.add(0,"0");
				  list.add(1,"发票种类 为空，请维护 ");
				  return list;
			  }else if (custcode==null || "".equals(custcode)) {
				  list.add(0,"0");
				  list.add(1,"结算用户代码 为空，请维护 ");
				  return list;
			  }else if (custcode==null || "".equals(custcode)) {
				  list.add(0,"0");
				  list.add(1,"最终用户代码 为空，请维护 ");
				  return list;
			  }else if(financeCode(hvo.getDwbm(),queryzrzx(bvo[0].getDeptid()))== null ||"".equals(financeCode(hvo.getDwbm(),queryzrzx(bvo[0].getDeptid())))){
				  list.add(0,"0");
				  list.add(1,"财务责任中心为空，请维护 ");
				  return list;
			  }else if (jfshl == null || "".equals(jfshl)) {
				  list.add(0,"0");
				  list.add(1,"发票重量为空，请维护 ");
				  return list;
			  }else if (Jfybwsje == null || "".equals(Jfybwsje)) {
				  list.add(0,"0");
				  list.add(1,"发票金额(不含税)为空，请维护 ");
				  return list;
			  }else if (dj == null || "".equals(dj)) {
				  list.add(0,"0");
				  list.add(1,"发票价格（不含税）为空，请维护 ");
				  return list;
			  }else if (Jfybje == null || "".equals(Jfybje)) {
				  list.add(0,"0");
				  list.add(1,"发票税金额为空，请维护 ");
				  return list;
			  }else if (Jfybje == null || "".equals(Jfybje)) {
				  list.add(0,"0");
				  list.add(1,"发票价税合计金额为空，请维护 ");
				  return list;
			  }else if (sl == null || "".equals(sl)) {
				  list.add(0,"0");
				  list.add(1,"税率 为空，请维护 ");
				  return list;
			  }else if (hvo.getDjrq() == null || "".equals(hvo.getDjrq())) {
				  list.add(0,"0");
				  list.add(1,"开票日期  为空，请维护 ");
				  return list;
			  }else if (queryLRR == null || "".equals(queryLRR)) {
				  list.add(0,"0");
				  list.add(1,"开票人  为空，请维护 ");
				  return list;
			  }else if (kpTaxCode.get("unitname") == null || "".equals(kpTaxCode.get("unitname"))) {
				  list.add(0,"0");
				  list.add(1,"开票单位名称 为空，请维护 ");
			  }else if (kpTaxCode.get("taxcode") == null || "".equals( kpTaxCode.get("taxcode"))) {
				  list.add(0,"0");
				  list.add(1,"开票单位税号为空，请维护 ");
			  }	else if ("".equals(custcode.get("taxpayerid"))||custcode.get("taxpayerid")==null) {
				  list.add(0,"0");
				  list.add(1,"购方单位税号为空，请维护 ");
			  }	else if (BodyMap.get("accname")==null || "".equals(BodyMap.get("accname"))) {
				  list.add(0,"0");
				  list.add(1," 购方银行名称为空，请维护 ");
			  }	else if (BodyMap.get("account")==null||"".equals(BodyMap.get("account"))) {
				  list.add(0,"0");
				  list.add(1," 购方银行帐号为空，请维护 ");
			  }	else if (jsfs==null||"".equals(jsfs)) {
				  list.add(0,"0");
				  list.add(1,"结算方式 为空，请维护 ");
			 }else if (person(queryLRR,hvo.getDwbm())== null || "".equals(person(queryLRR,hvo.getDwbm()))) {
				  list.add(0,"0");
				  list.add(1,"结算人工号(开票人工号)为空，请维护 ");
			  }else if (nyr == null||"".equals(nyr)) {
				  list.add(0,"0");
				  list.add(1," 预设会计期 为空，请维护 ");
			  }else if (judgeCurrency ==null||"".equals(judgeCurrency)) {
				  list.add(0,"0");
				  list.add(1,"币种代码为空，请维护 ");
			  }			
			  return list;
		  }
		  
		  /**
			 * 检验字段是否为空
			 * 销售发票体
			 *  wy
			 * 2019/10/29
			 */
		 public List judgeNXFPB(DJZBHeaderVO hvo,DJZBItemVO[] bvo,String nyr,String djbh,Map custcode,String judgeCurrency,String jsfs,String queryLRR,int t){
			  List<String> list = new ArrayList<String>();
			  Map kpTaxCode = kpTaxCode(hvo.getDwbm());
			  Map BodyMap = selectCubasdoc(djbh);
			  String chCode = selectInvl(hvo); //存货分类code
			  Map queryInv = QueryInv(chCode);
			  Map queryHT = queryHT(chCode); //合同
			  String nx = "销售发票（内销）";
			  list.add(0,"1");
			  if(kpTaxCode.get("def8")==null||"".equals(kpTaxCode.get("def8"))||kpTaxCode.get("def8").toString().length()<=0){
				  list.add(0,"0");
				  list.add(1,"公司别(账套代码)为空，请维护");
				  return list;
			  }else if(queryzrzx(bvo[0].getDeptid())== null ||"".equals(queryzrzx(bvo[0].getDeptid()))){
				  list.add(0,"0");
				  list.add(1,"责任中心为空，请维护 ");
				  return list;
			  }else if (DJLX(nx)==null||"".equals(DJLX(nx))) {
				  list.add(0,"0");
				  list.add(1,"单据类型为空，请维护 ");
				  return list;
			  }else if (hvo.getDjbh()==null ||"".equals(hvo.getDjbh())) {
				  list.add(0,"0");
				  list.add(1,"业务单据号为空，请维护 ");
				  return list;
			  }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17())) {
				  list.add(0,"0");
				  list.add(1,"纸质发票号为空，请维护 ");
				  return list;
			  }else if (BodyMap.get("ccreditnum")== null||"".equals(BodyMap.get("ccreditnum"))) {
				  list.add(0,"0");
				  list.add(1,"发票代码为空，请维护 ");
				  return list;
			  }else if (bvo[t].getDdh()==null||"".equals(bvo[t].getDdh())) {
				  list.add(0,"0");
				  list.add(1,"三单号(业务单据子项号)为空，请维护 ");
				  return list;
			  }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				  list.add(0,"0");
				  list.add(1,"产副品代码/费用类型为空，请维护 ");
				  return list;
			  }else if (queryInv.get("invclassname")==null||"".equals(queryInv.get("invclassname"))) {
				  list.add(0,"0");
				  list.add(1,"费用类型名称(存货名称)为空，请维护 ");
				  return list;			  
			  }else if (jsfs==null||"".equals(jsfs)) {
				  list.add(0,"0");
				  list.add(1,"结算方式为空，请维护 ");
				  return list;
			  }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				  list.add(0,"0");
				  list.add(1,"业务类型为空，请维护 ");
				  return list;
			  }else if (bvo[t].getJfshl()==null||"".equals(bvo[t].getJfshl())) {
				  list.add(0,"0");
				  list.add(1,"三单量 为空，请维护 ");
				  return list;
			  }else if (bvo[t].getJfshl()==null||"".equals(bvo[t].getJfshl())) {
				  list.add(0,"0");
				  list.add(1,"开票数量为空，请维护 ");
				  return list;
			  }else if (bvo[t].getJfybwsje()==null||"".equals(bvo[t].getJfybwsje())) {
				  list.add(0,"0");
				  list.add(1,"货款为空，请维护 ");
				  return list;
			  }else if (bvo[t].getJfybsj()==null||"".equals(bvo[t].getJfybsj())) {
				  list.add(0,"0");
				  list.add(1,"税额为空，请维护 ");
				  return list;
			  }else if (custcode==null||"".equals(custcode)) {
				  list.add(0,"0");
				  list.add(1,"结算用户代码为空，请维护 ");
				  return list;
			  }else if (custcode==null||"".equals(custcode)) {
				  list.add(0,"0");
				  list.add(1,"最终用户代码为空，请维护 ");
				  return list;
			  }else if (nyr==null||"".equals(nyr)) {
				  list.add(0,"0");
				  list.add(1,"上抛日期为空，请维护 ");
				  return list;
			  }else if (nyr==null||"".equals(nyr)) {
				  list.add(0,"0");
				  list.add(1,"结齐日期(入帐日期)为空，请维护 ");
				  return list;
			  }else if (bvo[t].getSl()==null||"".equals(bvo[t].getSl())) {
				  list.add(0,"0");
				  list.add(1,"税率为空，请维护 ");
				  return list;
			  }
			  return list; 
		  }
		 	/**
			 * 检验字段是否为空
			 * 销售发票（开票明细）
			 *  wy
		     * 2019/10/29
			 */
		 public List judgeNXFPMX(DJZBHeaderVO hvo,DJZBItemVO[] bvo,String nyr,String djbh,Map custcode,String judgeCurrency,String jsfs,String queryLRR,int i){
			  List<String> list = new ArrayList<String>();
			  Map kpTaxCode = kpTaxCode(hvo.getDwbm());
			  Map BodyMap = selectCubasdoc(djbh);
			  String chCode = selectInvl(hvo); //存货分类code
			  Map queryInv = QueryInv(chCode);
			  Map queryHT = queryHT(chCode); //合同
			  String queryZjldw = queryZjldw((String)queryInv.get("pk_measdoc"));
			  String nx = "销售发票（内销）";
			  list.add(0,"1");
			  if(kpTaxCode.get("def8")==null||"".equals(kpTaxCode.get("def8"))||kpTaxCode.get("def8").toString().length()<=0){
				  list.add(0,"0");
				  list.add(1,"公司别 （账套代码）为空，请维护");
				  return list;
			  }else if(queryzrzx(bvo[0].getDeptid())== null ||"".equals(queryzrzx(bvo[0].getDeptid()))){
				  list.add(0,"0");
				  list.add(1,"责任中心为空，请维护 ");
				  return list;
			  }else if (DJLX(nx)==null||"".equals(DJLX(nx))) {
				  list.add(0,"0");
				  list.add(1,"单据类型为空，请维护 ");
				  return list;
			  }else if (hvo.getDjbh()==null ||"".equals(hvo.getDjbh())) {
				  list.add(0,"0");
				  list.add(1,"业务单据号为空，请维护 ");
				  return list;
			  }else if (queryInv.get("invclassname")==null||"".equals(queryInv.get("invclassname"))) {
				  list.add(0,"0");
				  list.add(1,"品名(物料名称)为空，请维护 ");
				  return list;
			  }else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				  list.add(0,"0");
				  list.add(1,"数量为空，请维护 ");
				  return list;
			  }else if (bvo[i].getDj()==null||"".equals(bvo[i].getDj())) {
				  list.add(0,"0");
				  list.add(1,"单价为空，请维护 ");
				  return list;
			  }else if (bvo[i].getJfybwsje()==null||"".equals(bvo[i].getJfybwsje())) {
				  list.add(0,"0");
				  list.add(1," 货款金额（不含税金额）为空，请维护 ");
				  return list;
			  }else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				  list.add(0,"0");
				  list.add(1," 税额为空，请维护 ");
				  return list;
			  }else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				  list.add(0,"0");
				  list.add(1,"含税金额为空，请维护 ");
				  return list;
			  }else if (queryZjldw==null||"".equals(queryZjldw)) {
				  list.add(0,"0");
				  list.add(1," 单位为空，请维护 ");
				  return list;
			  }else if (bvo[i].getSl() == null||"".equals(bvo[i].getSl())) {
				  list.add(0,"0");
				  list.add(1,"税率为空，请维护 ");
				  return list;
			  }
			 return list;			 
		 }
		 
		 /**
		  * 校验外销发票头
		  *  wy
		  * 2019/10/29
		  */
		 public List judgeWXPFT(DJZBHeaderVO hvo,DJZBItemVO[] bvo,Map kpTaxCode,String chCode,Map queryInv,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble jfshl,UFDouble Jfybsj,UFDouble zje,UFDouble Jfybje){
			 List<String> list = new ArrayList<String>();
			 String wx = "销售发票（外销）";
			 list.add(0,"1");
			 if(DJLX(wx)==null||"".equals(DJLX(wx))){
				 list.add(0,"0");
				 list.add(1,"单据类型为空，请维护");
				 return list;
			 }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17()==null)) {
				 list.add(0,"0");
				 list.add(1,"外销发票号为空，请维护");
				 return list;
			 }else if (kpTaxCode.get("def8") ==null||"".equals(kpTaxCode.get("def8")==null)) {
				 list.add(0,"0");
				 list.add(1,"帐套为空，请维护");
				 return list;
			 }else if (queryzrzx(bvo[0].getDeptid())==null||"".equals(queryzrzx(bvo[0].getDeptid()))) {
				 list.add(0,"0");
				 list.add(1,"责任中心为空，请维护");
				 return list;
			 }else if (custcode==null||"".equals(custcode)) {
				 list.add(0,"0");
				 list.add(1,"结算用户代码--客商代码为空，请维护");
				 return list;
			 }else if (jsfs==null|"".equals(jsfs)) {
				 list.add(0,"0");
				 list.add(1,"结算方式为空，请维护");
				 return list;
			 }else if (judgeCurrency==null|"".equals(judgeCurrency)) {
				 list.add(0,"0");
				 list.add(1,"币种代码为空，请维护");
				 return list;
			 }else if (queryInv.get("invclasscode")==null|"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"品名代码-存货分类编码为空，请维护");
				 return list;
			 }else if (queryInv.get("invclassname")==null|"".equals(queryInv.get("invclassname"))) {
				 list.add(0,"0");
				 list.add(1,"品名中文名称-存货分类名称为空，请维护");
				 return list;
			 }else if (hvo.getDjrq()==null|"".equals(hvo.getDjrq())) {
				 list.add(0,"0");
				 list.add(1,"开票日期 为空，请维护");
				 return list;
			 }else if (zje==null|"".equals(zje)) {
				 list.add(0,"0");
				 list.add(1,"发票金额为空，请维护");
				 return list;
			 }else if (Jfybje==null|"".equals(Jfybje)) {
				 list.add(0,"0");
				 list.add(1,"金额为空，请维护");
				 return list;
			 }else if (jfshl==null|"".equals(jfshl)) {
				 list.add(0,"0");
				 list.add(1,"发票重量 --发票数量为空，请维护");
				 return list;
			 }else if (Jfybsj==null|"".equals(Jfybsj)) {
				 list.add(0,"0");
				 list.add(1,"税额为空，请维护");
				 return list;
			 }else if (queryInv.get("invclasscode")==null|"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"产副品代码/费用类型--存货分类为空，请维护");
				 return list;
			 }
			return list;
		 }
		 /**
		  * 校验外销发票明细
		  *  wy
		  * 2019/10/29
		  */
		 @SuppressWarnings("unchecked")
		public List judgeWXFPB(DJZBHeaderVO hvo,DJZBItemVO[] bvo,int i,Map kpTaxCode,String chCode,Map queryInv,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble zje,Map BodyMap){
			 List<String> list = new ArrayList<String>();
			 String wx = "销售发票（外销）";
			 Map queryHT = queryHT(chCode); //合同
			 list.add(0,"1");
			 if(DJLX(wx)==null||"".equals(DJLX(wx))){
				 list.add(0,"0");
				 list.add(1,"单据类型为空，请维护");
				 return list;
			 }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17()==null)) {
				 list.add(0,"0");
				 list.add(1,"外销纸质发票号为空，请维护");
				 return list;
			 }else if (kpTaxCode.get("def8") ==null||"".equals(kpTaxCode.get("def8")==null)) {
				 list.add(0,"0");
				 list.add(1,"帐套为空，请维护");
				 return list;
			 }else if (queryzrzx(bvo[0].getDeptid())==null||"".equals(queryzrzx(bvo[0].getDeptid()))) {
				 list.add(0,"0");
				 list.add(1,"责任中心为空，请维护");
				 return list;
			 }else if (queryHT.get("ct_code")==null||"".equals(queryHT.get("ct_code"))) {
				 list.add(0,"0");
				 list.add(1,"合同号为空，请维护");
				 return list;
			 }else if (queryInv.get("memo")==null||"".equals(queryInv.get("memo"))) {
				 list.add(0,"0");
				 list.add(1,"海关编码为空，请维护");
				 return list;
			 }else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				 list.add(0,"0");
				 list.add(1,"发票重量（净重）为空，请维护");
				 return list;
			 }else if ( bvo[i].getHsdj()==null||"".equals(bvo[i].getHsdj())) {
				 list.add(0,"0");
				 list.add(1,"销售单价为空，请维护");
				 return list;
			 }else if (zje==null||"".equals(zje)) {
				 list.add(0,"0");
				 list.add(1,"去佣金额--同金额为空，请维护");
				 return list;
			 }else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				 list.add(0,"0");
				 list.add(1,"税额为空，请维护");
				 return list;
			 }else if (queryLRR==null||"".equals(queryLRR)) {
				 list.add(0,"0");
				 list.add(1,"录入人--制单人名称为空，请维护");
				 return list;
			 }else if(person(queryLRR,hvo.getDwbm())==null||"".equals(person(queryLRR,hvo.getDwbm()))){
				 list.add(0,"0");
				 list.add(1,"录入人工号--制单人工号为空，请维护");
				 return list;
			 }else if (BodyMap.get("dmakedate")==null||"".equals(BodyMap.get("dmakedate"))) {
				 list.add(0,"0");
				 list.add(1,"录入日期--制单日期为空，请维护");
				 return list;
			 } 
			return list;
		 }
		 
		 /**
		  * 校验外销发票海关拆分明细信息接口
		  *  wy
		  * 2019/10/29
		  */
		 public List judgeWXHG(DJZBHeaderVO hvo,DJZBItemVO[] bvo,int i,Map kpTaxCode,String chCode,Map queryInv,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble zje,Map BodyMap){
			 List<String> list = new ArrayList<String>();
			 String wx = "销售发票（外销）";
			 Map queryHT = queryHT(chCode); //合同
			 list.add(0,"1");
			 if(DJLX(wx)==null||"".equals(DJLX(wx))){
				 list.add(0,"0");
				 list.add(1,"单据类型为空，请维护");
				 return list;
			 }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17()==null)) {
				 list.add(0,"0");
				 list.add(1,"外销纸质发票号为空，请维护");
				 return list;
			 }else if (kpTaxCode.get("def8") ==null||"".equals(kpTaxCode.get("def8")==null)) {
				 list.add(0,"0");
				 list.add(1,"帐套为空，请维护");
				 return list;
			 }else if (queryzrzx(bvo[0].getDeptid())==null||"".equals(queryzrzx(bvo[0].getDeptid()))) {
				 list.add(0,"0");
				 list.add(1,"责任中心为空，请维护");
				 return list;
			 }else if (queryInv.get("memo")==null||"".equals(queryInv.get("memo"))) {
				 list.add(0,"0");
				 list.add(1,"出口商品代码为空，请维护");
				 return list;
			}else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				 list.add(0,"0");
				 list.add(1,"发票重量--数量为空，请维护");
				 return list;
			}else if (bvo[i].getHsdj()==null||"".equals(bvo[i].getHsdj())) {
				 list.add(0,"0");
				 list.add(1,"销售单价为空，请维护");
				 return list;
			}else if (bvo[i].getJfybwsje()==null||"".equals(bvo[i].getJfybwsje())) {
				 list.add(0,"0");
				 list.add(1,"含佣金额为空，请维护");
				 return list;
			}else if (zje==null||"".equals(zje)) {
				 list.add(0,"0");
				 list.add(1,"发票金额为空，请维护");
				 return list;
			}else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				 list.add(0,"0");
				 list.add(1,"税额为空，请维护");
				 return list;
			}else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17())) {
				 list.add(0,"0");
				 list.add(1,"监制发票号为空，请维护");
				 return list;
			}else if (BodyMap.get("ccreditnum")==null||"".equals(BodyMap.get("ccreditnum"))) {
				 list.add(0,"0");
				 list.add(1,"发票代码为空，请维护");
				 return list;
			}else if (hvo.getDjrq()==null||"".equals(hvo.getDjrq())) {
				 list.add(0,"0");
				 list.add(1,"发票日期为空，请维护");
				 return list;
			}
			return list;
		 }
		 
		 /**
		  * 校验外销三单信息接口 	
		  *  wy
		  * 2019/10/29	
		  */
		 public List judgeWXSD(DJZBHeaderVO hvo,DJZBItemVO[] bvo,int i,String djbh,Map kpTaxCode,String chCode,Map queryInv,Map custcode,String judgeCurrency,String jsfs,String queryLRR,UFDouble zje,String jsfs1){
			 List<String> list = new ArrayList<String>();
			 String wx = "销售发票（外销）";
			 Map BodyMap = selectCubasdoc(djbh);
			 Map queryHT = queryHT(chCode); //合同
			 list.add(0,"1");
			 if (kpTaxCode.get("def8") ==null||"".equals(kpTaxCode.get("def8")==null)) {
				 list.add(0,"0");
				 list.add(1,"帐套为空，请维护");
				 return list;
			 }else if (queryzrzx(bvo[0].getDeptid())==null||"".equals(queryzrzx(bvo[0].getDeptid()))) {
				 list.add(0,"0");
				 list.add(1,"责任中心为空，请维护");
				 return list;
			 }else if(DJLX(wx)==null||"".equals(DJLX(wx))){
				 list.add(0,"0");
				 list.add(1,"单据类型为空，请维护");
				 return list;
			 }else if (bvo[i].getDdh()==null||"".equals(bvo[i].getDdh())) {
				 list.add(0,"0");
				 list.add(1,"三单号为空，请维护");
				 return list;
			 }else if (hvo.getZyx17()==null||"".equals(hvo.getZyx17())) {
				 list.add(0,"0");
				 list.add(1,"外销纸质发票号为空，请维护");
				 return list;
			 }else if (queryHT.get("ct_code")==null||"".equals(queryHT.get("ct_code"))) {
				 list.add(0,"0");
				 list.add(1,"合同号为空，请维护");
				 return list;
			 }else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				 list.add(0,"0");
				 list.add(1,"三单量为空，请维护");
				 return list;
			 }else if (bvo[i].getJfshl()==null||"".equals(bvo[i].getJfshl())) {
				 list.add(0,"0");
				 list.add(1,"开票数量为空，请维护");
				 return list;
			 }else if (bvo[i].getDj()==null||"".equals(bvo[i].getDj())) {
				 list.add(0,"0");
				 list.add(1,"单价为空，请维护");
				 return list;
			 }else if (queryZjldw((String)queryInv.get("pk_measdoc"))==null||"".equals(queryZjldw((String)queryInv.get("pk_measdoc")))) {
				 list.add(0,"0");
				 list.add(1,"开票计量单位为空，请维护");
				 return list;
			 }else if (bvo[i].getJfybwsje()==null||"".equals(bvo[i].getJfybwsje())) {
				 list.add(0,"0");
				 list.add(1,"货款为空，请维护");
				 return list;
			 }else if (bvo[i].getSl()==null||"".equals(bvo[i].getSl())) {
				 list.add(0,"0");
				 list.add(1,"税率为空，请维护");
				 return list;
			 }else if (bvo[i].getJfybsj()==null||"".equals(bvo[i].getJfybsj())) {
				 list.add(0,"0");
				 list.add(1,"税额为空，请维护");
				 return list;
			 }else if (jsfs1==null||"".equals(jsfs1)) {
				 list.add(0,"0");
				 list.add(1,"结算方式为空，请维护");
				 return list;
			 }else if (custcode==null||"".equals(custcode)) {
				 list.add(0,"0");
				 list.add(1,"结算用户代码为空，请维护");
				 return list;
			 }else if (custcode==null||"".equals(custcode)) {
				 list.add(0,"0");
				 list.add(1,"最终用户代码   为空，请维护");
				 return list;
			 }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"产副品代码、费用类型为空，请维护");
				 return list;
			 }else if (BodyMap.get("djkjnd")==null||"".equals(BodyMap.get("djkjnd"))||BodyMap.get("djkjqj")==null||"".equals(BodyMap.get("djkjqj"))) {
				 list.add(0,"0");
				 list.add(1,"会计期为空，请维护");
				 return list;
			 }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"品名为空，请维护");
				 return list;
			 }else if (queryInv.get("memo")==null||"".equals(queryInv.get("memo"))) {
				 list.add(0,"0");
				 list.add(1,"出口商品代码为空，请维护");
				 return list;
			 }else if (queryInv.get("invclasscode")==null||"".equals(queryInv.get("invclasscode"))) {
				 list.add(0,"0");
				 list.add(1,"合同类型 （放业务类型）为空，请维护");
				 return list;
			 }
			return list;			 
		 }
		 public StringBuffer checkListval(List list){
			 StringBuffer str = new StringBuffer();
			 List lists = new ArrayList();
				lists.add(0,"1");
				if(list.get(0).equals("")){
					str.append("币种为空,请填写");
//					lists.add(0,"0");
//					lists.add(1,"币种为空,请填写");
//					return lists;
				} if(list.get(1).equals(""))
				{
					str.append(" 合同号为空,请填写");
//					lists.add(0,"0");
//					lists.add(1,"合同号为空,请填写");
//					return lists;
				} if(list.get(2).equals("")){
					str.append(" 合同名称为空,请填写");
//					lists.add(0,"0");
//					lists.add(1,"合同名称为空,请填写");
//					return lists;
				} if(list.get(3).equals("")){
					str.append(",发票号为空,请填写");
//					lists.add(0,"0");
//					lists.add(1,"发票号为空,请填写");
//					return lists;
				} if(list.get(4).equals("")){
					str.append(",发票代码为空,请填写");
//					lists.add(0,"0");
//					lists.add(1,"发票代码为空,请填写");
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
		  * 根据财务部 和 公司编码 查财务部编码
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
		  * 根据制单人找制单人工号
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
		  * 查询核销金额,是否预付，核销日期
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
		  * 单据类型
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
		
		//add by zwx 2019-11-18 CVM增加纸质发票代码、责任中心
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