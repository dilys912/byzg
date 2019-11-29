package nc.impl.pub.fyrplan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.uap.sfapp.util.SFAppServiceUtil;
import nc.itf.dm.dm102.IDeliverydailyplan;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.dm.pub.DMVO;
import nc.vo.dm.pub.DailyPlanStatus;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

import org.apache.commons.lang.StringUtils;
/**
 * 发运日计划
 * @author src
 *
 */
public class SendFyrPlanImpl implements IHttpServletAdaptor {
	IUAPQueryBS uap = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	public void doAction(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		InputStream in = request.getInputStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  
        String s = "";  
        StringBuffer sb = new StringBuffer();  
        while ((s = br.readLine()) != null) {  
            sb.append(s);  
        }  
        String result = sendFyrPlan(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }
	public String sendFyrPlan(String json) {
		InvocationInfoProxy.getInstance().setUserDataSource("nc5011");
		String source = InvocationInfoProxy.getInstance().getUserDataSource();
		InvocationInfoProxy.getInstance().setUserDataSource(source);
		nc.net.sf.json.JSONArray jsonarray = null;
		try {
			jsonarray = new nc.net.sf.json.JSONArray().fromObject(json);
		} catch (Exception e) {
			return "[{\"status\":\"error\",\"message\":\"json解析错误-请检查json格式:"+e.getMessage()+"\"}]";
		}
		
		List<Map> lsmap = new ArrayList<Map>();
		if(jsonarray != null&&jsonarray.size()>0){
			for(int i =0;i<jsonarray.size();i++){
				Map map = new HashMap();
				nc.net.sf.json.JSONObject jsonobject = jsonarray.getJSONObject(i);
				String key = null;
				String value = null;
				Iterator it = jsonobject.keys();
				while(it.hasNext()){
					key = (String) it.next();
					value = jsonobject.getString(key);
					map.put(key, value);
				}
				lsmap.add(map);
			}
		}
		StringBuffer allinfo = new StringBuffer();
		StringBuffer all = new StringBuffer();
		allinfo.append("[");
		String error = "";
		Map mls = lsmap.get(0);
		String username=mls.get("username")==null?"":mls.get("username").toString();//用户名
		if(StringUtils.isNotBlank(username)){
			if(!username.equals("baosteel")){
				error= "{\"status\":\"error\",\"message\":\"用户名错误\"}"; 
				all.append(error+",");
			}
		} else {
			error= "{\"status\":\"error\",\"message\":\"用户名为空\"}"; 
			all.append(error+",");
		}
		String pwd=mls.get("password")==null?"":mls.get("password").toString();//密码
		if(StringUtils.isNotBlank(pwd)){
			if(!pwd.equals("123456")){
				error= "{\"status\":\"error\",\"message\":\"密码错误\"}"; 
				all.append(error+",");
			}
		} else {
			error= "{\"status\":\"error\",\"message\":\"密码为空\"}"; 
			all.append(error+",");
		}
		String billcode = mls.get("billcode")==null?"":mls.get("billcode").toString();//销售订单号
		if(StringUtils.isBlank(billcode)){
			error= "{\"status\":\"error\",\"message\":\"发运订单号为空\"}";
			all.append(error+",");
		}
		String applyCorp=mls.get("corp")==null?"":mls.get("corp").toString();//申请公司
		if(StringUtils.isBlank(applyCorp)){
			error= "{\"status\":\"error\",\"message\":\"公司为空\"}"; 
			all.append(error+",");
		}
		String corpSql = "select a.pk_corp from bd_corp a where a.unitcode = '"+applyCorp+"' and nvl(a.dr,0)=0";
		List corpList = null;
		try {
			corpList = (List) uap.executeQuery(corpSql, new MapListProcessor());
			if(corpList == null || corpList.size() == 0){
				error= "{\"status\":\"error\",\"message\":\"公司在NC系统不存在，请检查\"}"; 
				all.append(error+",");
			}
		} catch (BusinessException e4) {
			e4.printStackTrace();
			error= "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e4.getMessage()+"\"}";
			all.append(error+",");
		}
		Map map1 = (Map) corpList.get(0);
		String pk_corp = map1.get("pk_corp").toString();
		String tsms = mls.get("tsms")==null?"":mls.get("tsms").toString();//途损模式
		String fhdd = mls.get("fhdd")==null?"":mls.get("fhdd").toString();//发货地点
		String jhfs = mls.get("jhfs")==null?"":mls.get("jhfs").toString();//交货方式
		SaleOrderVO approveaggvo = new SaleOrderVO();
    	try {
			approveaggvo = getAggVO(billcode, pk_corp);
		} catch (BusinessException e) {
			e.printStackTrace();
			error= "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e.getMessage()+"\"}";
			all.append(error+",");
		}
		if(approveaggvo == null){
			error= "{\"status\":\"error\",\"message\":\"销售订单"+billcode+"在NC系统不存在或已被删除\"}";
			all.append(error+",");
		}
		AggregatedValueObject[] sourceBillVOs = new AggregatedValueObject[1];
		sourceBillVOs[0] = approveaggvo;
		DMDataVO[] dmdataSO = (DMDataVO[])null;
		try {
			dmdataSO = convertSourceVO(sourceBillVOs, "30", "7D");
		} catch (BusinessException e) {
			e.printStackTrace();
			error= "{\"status\":\"error\",\"message\":\"销售订单"+billcode+"推送发运日计划失败:"+e.getMessage()+"\"}";
			all.append(error+",");
		}
		String pk_delivorg ="";//发运组织
		for(int i  = 0 ; i<dmdataSO.length ; i++){
			String rjhcode = "";//日计划单据号
			BillCodeObjValueVO bcovo = null;
			bcovo = getBillCodeObjVO(dmdataSO[i]);
			try {
				rjhcode = SFAppServiceUtil.getBillcodeRuleService().getBillCode_RequiresNew("7D", pk_corp, null, bcovo);
			} catch (ValidationException e) {
				e.printStackTrace();
				error= "{\"status\":\"error\",\"message\":\"获取发运日计划单据号异常:"+e.getMessage()+"\"}";
				all.append(error+",");
			} catch (BusinessException e) {
				e.printStackTrace();
				error= "{\"status\":\"error\",\"message\":\"获取发运日计划单据号异常:"+e.getMessage()+"\"}";
				all.append(error+",");
			}
			dmdataSO[i].setAttributeValue("borderreturn", new UFBoolean("N"));//原订单是否退货单
			dmdataSO[i].setAttributeValue("iplanstatus", new Integer(
			          DailyPlanStatus.Free));//计划状态
			dmdataSO[i].setAttributeValue("pkplanperson", approveaggvo.getHeadVO().getCoperatorid());//计划人
			dmdataSO[i].setAttributeValue("plandate", new UFDate(new Date()));//日计划日期
			dmdataSO[i].setAttributeValue("vdelivdayplcode", rjhcode);//日计划单据号
			String addrname = "";
			if(StringUtils.isNotBlank(fhdd)){
				try {
					addrname = getAreaclID(fhdd, pk_corp);
				} catch (BusinessException e) {
					e.printStackTrace();
					error= "{\"status\":\"error\",\"message\":\"获取发货地点异常:"+e.getMessage()+"\"}";
					all.append(error+",");
				}
				if(StringUtils.isBlank(addrname)){
					return "[{\"status\":\"error\",\"message\":\"发货地点在NC系统中未找，请检查\"}]";
				}
			}
			//发运组织 table:bd_delivorg
			//上海
			if(pk_corp.equals("1016")){
				if(jhfs.equals("送货")){
					pk_delivorg="1016A21000000000WLSK";//发运组织(SBY销售【送货】及储运【调拨】业务)
				}else{
					pk_delivorg="1016A21000000004W3IA";//SBY销售【自提】业务
				}
			}
			//武汉	
			else if(pk_corp.equals("1071")){
				if(jhfs.equals("送货")){
					pk_delivorg="1071A21000000000V2N6";//发运组织:WHZG销售【送货】及储运【调拨】业务
				}else{
					pk_delivorg="1071A21000000000V2N9";//WHZG销售【自提】业务
				}
			}
			//哈尔滨	
			else if(pk_corp.equals("1103")){
				if(jhfs.equals("送货")){
					pk_delivorg="0001A2100000000CNP7X";//发运组织:HRBZG销售【送货】及储运【调拨】业务
				}else{
					pk_delivorg="0001A2100000000CNP88";//HRBZG销售【自提】业务
				}
			}
			//河南
			else if(pk_corp.equals("1097")){
				if(jhfs.equals("送货")){
					pk_delivorg="1097A210000000008LVF";//发运组织:HNZG销售【送货】及储运【调拨】业务
				}else{
					pk_delivorg="1097A210000000008LVY";//HNZG销售【自提】业务
				}
			}
			//河北
			else if(pk_corp.equals("1017")){
				if(jhfs.equals("送货")){
					pk_delivorg="1017A2100000000147BJ";//发运组织:HBZG销售【送货】及储运【调拨】业务
				}else{
					pk_delivorg="1017A2100000000147BM";//HBZG销售【自提】业务
				}
			}
			//成都
			else if(pk_corp.equals("1018")){
				if(jhfs.equals("送货")){
					pk_delivorg="1018A21000000001IIW4";//发运组织:CD销售【送货】及储运【调拨】业务
				}else{
					pk_delivorg="1018A2100000000563UK";//CD成都【自提】发运组织
				}
			}
			//佛山
			else if(pk_corp.equals("1019")){
				if(jhfs.equals("送货")){
					pk_delivorg="1019A21000000001459O";//发运组织:FS销售【送货】及储运【调拨】业务
				}else{
					pk_delivorg="1019A21000000001459R";//FS销售【自提】业务
				}
			}
			//宝翼2
			else if(pk_corp.equals("1107")){
				if(jhfs.equals("送货")){
					pk_delivorg="110711100000000006YO";//发运组织:SBY销售【送货】及储运【调拨】业务
				}else{
					pk_delivorg="110711100000000006YR";//SBY销售【自提】业务
				}
			}
			
			dmdataSO[i].setAttributeValue("vsendaddr", addrname);//发货地址
			dmdataSO[i].setAttributeValue("vuserdef_b_12", tsms);//途损模式
			dmdataSO[i].setAttributeValue("userid", approveaggvo.getHeadVO().getCoperatorid());
			dmdataSO[i].setStatus(VOStatus.NEW);
			dmdataSO[i].setAttributeValue("sourcebillts", approveaggvo.getHeadVO().getTs());
			dmdataSO[i].setAttributeValue("pksendaddress", fhdd);//发货地址主键
			//dmdataSO[i].setAttributeValue("cvmflag", "CVM");
			dmdataSO[i].setAttributeValue("snddate", new UFDate(new Date()));
			dmdataSO[i].setAttributeValue("pkcorp", approveaggvo.getHeadVO().getPk_corp());
			dmdataSO[i].setAttributeValue("pkdelivorg", pk_delivorg); // 发运组织(SBY销售【送货】及储运【调拨】业务)
		}
//		nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
//		freeVOParse.setFreeVO(dmdataSO, null, "pkinv", false);
        DMDataVO[] savevos = null;
        savevos = new DMDataVO[dmdataSO.length];
        for (int i = 0; i < dmdataSO.length; i++) {
            savevos[i] = dmdataSO[i];
            savevos[i].setStatus(VOStatus.NEW);
            savevos[i].setAttributeValue("sourcebillts", approveaggvo.getHeadVO().getTs());
            //savevos[i].setAttributeValue("cvmflag", "CVM");
            savevos[i].setAttributeValue("userid", approveaggvo.getHeadVO().getCoperatorid()); // 操作人
            savevos[i].setAttributeValue("pkdelivorg", pk_delivorg); // 发运组织(SBY销售【送货】及储运【调拨】业务)
          }
        
        DMDataVO[] ordervos = getWriteOrderData(savevos);
        for(int i = 0; i < ordervos.length; i++){
        	ordervos[i].setStatus(VOStatus.NEW);
        	ordervos[i].setAttributeValue("sourcebillts", approveaggvo.getHeadVO().getTs());
        	//ordervos[i].setAttributeValue("cvmflag", "CVM");
 	        ordervos[i].setAttributeValue("orderstatus", "Y");
 	       // ordervos[i].setAttributeValue("ndelivernum", new UFDouble(200000));
 	        ordervos[i].setAttributeValue("pkdelivorg", pk_delivorg); // 发运组织(SBY销售【送货】及储运【调拨】业务)
        }
        if(all.length()>0){
        	all = all.deleteCharAt(all.length()-1);
        	allinfo.append(all);
        	allinfo.append("]");
        	return allinfo.toString();
        }
        for(int i = 0; i<dmdataSO.length; i++){
        	SaleOrderVO newaggvo = new SaleOrderVO();
        	try {
				newaggvo = getAggVO(billcode, pk_corp);
			} catch (BusinessException e) {
				e.printStackTrace();
				error= "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e.getMessage()+"\"}";
				all.append(error+",");
			}
        	DMDataVO[] ordervos1 = new DMDataVO[1];
	        DMDataVO[] savevos1 = new DMDataVO[1];
        	ordervos1[0] = ordervos[i];
        	savevos1[0] = savevos[i];
        	if(i == 0){
        		ordervos1[0].setAttributeValue("sourcebillts", approveaggvo.getHeadVO().getTs());
            	savevos1[0].setAttributeValue("sourcebillts", approveaggvo.getHeadVO().getTs());
            	//ordervos1[0].setAttributeValue("cvmflag", "CVM");
            	//savevos1[0].setAttributeValue("cvmflag", "CVM");
        	}else{
        		ordervos1[0].setAttributeValue("sourcebillts", newaggvo.getHeadVO().getTs());
            	savevos1[0].setAttributeValue("sourcebillts", newaggvo.getHeadVO().getTs());
            	//ordervos1[0].setAttributeValue("cvmflag", "CVM");
            	//savevos1[0].setAttributeValue("cvmflag", "CVM");
        	}
        	IDeliverydailyplan bo = (IDeliverydailyplan)NCLocator.getInstance().lookup(IDeliverydailyplan.class.getName());
	        DMDataVO o[] = null;
	        String invcode = "";
        	try {
				invcode = getInvcode(dmdataSO[i].getAttributeValue("pkinv").toString(), pk_corp);
			} catch (BusinessException e1) {
				error= "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e1.getMessage()+"\"}";
				all.append(error+",");
			}
	        try {
				o = bo.saveAndWrite(ordervos1, savevos1);
			} catch (BusinessException e) {
				e.printStackTrace();
				error= "{\"status\":\"error\",\"message\":\"物料号为"+invcode+"的销售订单"+billcode+"推送发运日计划失败:"+e.getMessage()+"\"}"; 
				all.append(error+",");
			}
			if(o != null && o.length>0){
				savevos1[0].setAttributeValue("ts", o[0].getAttributeValue("ts"));
//				DMDataVO[] newvo = null;
//				try {
//					newvo = getDMAggvo(savevos1[0].getAttributeValue("vdelivdayplcode").toString(), pk_corp);
//				} catch (BusinessException e1) {
//					error= "{\"status\":\"error\",\"message\":\"数据库操作异常\"}"; 
//					all.append(error+",");
//				}
//				if(newvo == null || newvo.length==0){
//					error= "{\"status\":\"error\",\"message\":\"获取保存成功的发运日计划失败\"}"; 
//					all.append(error+",");
//				}else if(newvo != null && newvo.length>0){
				   savevos1[0].setAttributeValue("iplanstatus", new Integer(
				            DailyPlanStatus.Audit));
				   savevos1[0].setAttributeValue("pkapprperson", approveaggvo.getHeadVO().getCoperatorid()); // 审核人
				   savevos1[0].setAttributeValue("apprdate", new UFDate(new Date()));
				   savevos1[0].setAttributeValue("pkcorp", pk_corp);
				   savevos1[0].setAttributeValue("userid", approveaggvo.getHeadVO().getCoperatorid()); // 操作人
				   savevos1[0].setStatus(VOStatus.UPDATED);
					DMDataVO oo[] = null;
					IDeliverydailyplan au = (IDeliverydailyplan)NCLocator.getInstance().lookup(IDeliverydailyplan.class.getName());
					try {
						oo = au.auditDeliverydailyplan(savevos1);
					} catch (BusinessException e) {
						error= "{\"status\":\"error\",\"message\":\"物料号为"+invcode+"的销售订单"+billcode+"推送发运日计划审核失败:"+e.getMessage()+"\"}"; 
						all.append(error+",");
					}
					if(oo != null && oo.length>0){
						error= "{\"status\":\"success\",\"message\":\"物料号为"+invcode+"的销售订单"+billcode+"推送发运日计划审核成功\"}";
						all.append(error+",");
					}
			  //}
			}else{
				error= "{\"status\":\"error\",\"message\":\"物料号为"+invcode+"的销售订单"+billcode+"推送发运日计划失败\"}"; 
				all.append(error+",");
			}
        }
        if(all.length()>0){
        	all = all.deleteCharAt(all.length()-1);
        	allinfo.append(all);
        	allinfo.append("]");
        	InvocationInfoProxy.getInstance().setUserDataSource("design");
        	return allinfo.toString();
        }
        return null;
        //修改类 nc.impl.dm.dm102.DeliverydailyplanImpl.saveAndWriteAndDelInterface（） 2139
	}
	public SaleOrderVO getAggVO (String billcode,String corp) throws BusinessException{
		SaleOrderVO aggvo = new SaleOrderVO();
		StringBuffer sbh = new StringBuffer();
		StringBuffer sbb = new StringBuffer();
		sbh.append("select * from so_sale a where a.vreceiptcode='"+billcode+"' and nvl(a.dr,0)=0 and a.pk_corp='"+corp+"'");
		List<SaleorderHVO> listhvo = null;
		List<SaleorderBVO> listbvo = null;
		listhvo = (List<SaleorderHVO>) uap.executeQuery(sbh.toString(), new BeanListProcessor(SaleorderHVO.class));
		if(listhvo != null&&listhvo.size()>0){
			String csaleid = listhvo.get(0).getCsaleid();
			sbb.append("select * from so_saleorder_b b where b.csaleid ='"+csaleid+"' and nvl(b.dr,0)=0");
			listbvo = (List<SaleorderBVO>) uap.executeQuery(sbb.toString(), new BeanListProcessor(SaleorderBVO.class));
			if(listbvo != null&&listbvo.size()>0){
				SaleorderHVO hvo = listhvo.get(0);
				SaleorderBVO [] bvo = new SaleorderBVO[listbvo.size()];
				bvo=listbvo.toArray(new SaleorderBVO[listbvo.size()]);
				aggvo.setParentVO(hvo);
				aggvo.setChildrenVO(bvo);
				return aggvo;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	public DMDataVO[] convertSourceVO(AggregatedValueObject[] vos, String sourceBillType, String destBillType) throws BusinessException
   {
    if ((vos == null) || (vos.length == 0)) {
      return null;
    }
    ArrayList v = new ArrayList();

    DMVO[] dvo = (DMVO[])null;
    dvo = (DMVO[])PfUtilTools.runChangeDataAry(sourceBillType, destBillType, vos);
    for (int i = 0; i < dvo.length; i++) {
      for (int j = 0; j < dvo[i].getChildrenVO().length; j++) {
        v.add(dvo[i].getChildrenVO()[j]);
      }
    }
    DMDataVO[] dmdvos = new DMDataVO[v.size()];
    dmdvos = (DMDataVO[])v.toArray(dmdvos);
    v = null;

//    if (sourceBillType.equals("21")) {
//      return pushsavePrepareDailyPlanFromPO(dmdvos);
//    }
    for (int i = 0; i < dmdvos.length; i++) {
      if (!dmdvos[i].getAttributeValue("vbilltype").equals("30"))
        continue;
      if ((dmdvos[i].getAttributeValue("pkdeststoreorg") == null) || (dmdvos[i].getAttributeValue("pkdeststoreorg").toString().trim().length() <= 0))
        continue;
      dmdvos[i].setAttributeValue("pksendstoreorg", dmdvos[i].getAttributeValue("pkdeststoreorg"));
      dmdvos[i].setAttributeValue("pkdeststoreorg", null);

      if ((dmdvos[i].getAttributeValue("pkdeststore") != null) && (dmdvos[i].getAttributeValue("pkdeststore").toString().trim().length() > 0))
      {
        dmdvos[i].setAttributeValue("pksendstore", dmdvos[i].getAttributeValue("pkdeststore"));
        dmdvos[i].removeAttributeName("pkdeststore");
      }

      if ((dmdvos[i].getAttributeValue("pkdeststore") == null) || (dmdvos[i].getAttributeValue("pkdeststore").toString().trim().length() <= 0))
        continue;
      dmdvos[i].setAttributeValue("pksendstore", dmdvos[i].getAttributeValue("pkdeststore"));
      dmdvos[i].removeAttributeName("pkdeststore");
    }

    SuperVOUtil.execFormulaWithVOs(dmdvos, new String[] { "pksendarea->getColValue(bd_calbody,pk_areacl,pk_calbody,pksendstoreorg)", "pksendaddress->iif(getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore) = \"\",getColValue(bd_calbody,pk_address,pk_calbody,pksendstoreorg),getColValue(bd_stordoc,pk_address,pk_stordoc,pksendstore))" }, null);

    return dmdvos;
  }
	 /**
     * 获得回写销售订单的数据。 功能： 参数： 返回： 例外： 日期：(2002-8-6 13:12:37) 修改日期，修改人，修改原因，注释标志：
     * 
     * @param vos
     *          nc.vo.dm.pub.DMDataVO[]
     */
    public DMDataVO[] getWriteOrderData(DMDataVO[] vos) {
      if (vos == null || vos.length == 0)
        return null;
      DMDataVO[] ordervos = new DMDataVO[vos.length];

      String pkdelivdaypl;
      UFDouble dnum;

      for (int i = 0; i < vos.length; i++) {

        ordervos[i] = new DMDataVO();
        ordervos[i].setStatus(vos[i].getStatus());
        pkdelivdaypl = (String) vos[i].getAttributeValue("pk_delivdaypl");

        Object o = vos[i].getAttributeValue("dnum");
        dnum = (o == null ? new UFDouble(0) : new UFDouble(o.toString()));

        ordervos[i].setAttributeValue("pkbillh", vos[i]
            .getAttributeValue("pkbillh"));
        ordervos[i].setAttributeValue("pkbillb", vos[i]
            .getAttributeValue("pkbillb"));
        ordervos[i].setAttributeValue("vbilltype", vos[i]
            .getAttributeValue("vbilltype"));
        ordervos[i].setAttributeValue("pksalecorp", vos[i]
            .getAttributeValue("pksalecorp"));

        ordervos[i].setAttributeValue("orderstatus", vos[i]
            .getAttributeValue("delautocanceloder"));

        if (ordervos[i].getStatus() == VOStatus.DELETED)
          ordervos[i].setAttributeValue("ndelivernum", dnum.multiply(-1));

        else if (ordervos[i].getStatus() == VOStatus.NEW)
          ordervos[i].setAttributeValue("ndelivernum", dnum);
      }
      return ordervos;
    }
    
    private BillCodeObjValueVO getBillCodeObjVO(CircularlyAccessibleValueObject vo)
    {
      BillCodeObjValueVO bcovo = new BillCodeObjValueVO();
      String[] names = vo.getAttributeNames();
      String[] values = new String[names.length];
      for (int j = 0; j < names.length; j++) {
        values[j] = (vo.getAttributeValue(names[j]) == null ? "" : vo.getAttributeValue(names[j]).toString());

        if (names[j].equals("pkcorp"))
        {
          names[j] = "公司";
        }
        else if (names[j].equals("pkinv")) {
          names[j] = "存货";
        }
        else if (names[j].equals("pksalecorp")) {
          names[j] = "公司";
        }
      }
      bcovo.setAttributeValue(names, values);
      return bcovo;
    }
    /**
   	 * 根据地点档案主键获取地点名称
   	 * @param vos
   	 * @param sourceBillType
   	 * @param destBillType
   	 * @return
   	 * @throws BusinessException 
   	 * @throws BusinessException
   	 */
   	public String getAreaclID(String pk_address,String pk_corp) throws BusinessException{
   		String addrname = "";
   		String sql = "select b.addrname from bd_address b where b.pk_address='"+pk_address+"' and b.pk_corp='0001' and nvl(b.dr,0)=0";
   		addrname = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return addrname;
   	}
   	/**
   	 * 根据物料管理档案主键获取物料编码
   	 * @param pkinv
   	 * @param pk_corp
   	 * @return
   	 * @throws BusinessException
   	 */
   	String getInvcode(String pkinv ,String pk_corp) throws BusinessException{
   		String invcode = "";
   		StringBuffer sb = new StringBuffer();
   		sb.append(" select a.invcode from bd_invbasdoc a  ") 
   		.append(" left join bd_invmandoc b on a.pk_invbasdoc = b.pk_invbasdoc ") 
   		.append(" where (nvl(a.dr,0)=0 and nvl(b.dr,0)=0) ") 
   		.append(" and (a.pk_corp='0001' or a.pk_corp='"+pk_corp+"') and b.pk_corp='"+pk_corp+"' ") 
   		.append(" and b.pk_invmandoc = '"+pkinv+"' ");
   		invcode = (String) uap.executeQuery(sb.toString(), new ColumnProcessor());
   		return invcode;
   	}
   	/**
   	 * 获取保存的发运日计划
   	 * @throws BusinessException 
   	 */
   	public DMDataVO[] getDMAggvo(String billcode,String pk_corp) throws BusinessException{
   		DMDataVO[] dm = new DMDataVO[1];
   		DMDataVO aggvo = new DMDataVO();
   		String sql = "select * from dm_delivdaypl a where a.vdelivdayplcode='"+billcode+"' and a.pksalecorp='"+pk_corp+"' and nvl(a.dr,0)=0";
   		List<DMDataVO> listvo = (List<DMDataVO>) uap.executeQuery(sql, new BeanListProcessor(DMDataVO.class));
   		if(listvo != null && listvo.size()>0){
   			aggvo = listvo.get(0);
   			dm[0] = aggvo;
   			return dm;
   		}else{
   			return null;
   		}
   	}
	
}
