package nc.impl.mm.mm6600;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
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
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.pub.rino.IPubDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.mm.bzfs.BzfsVO;
import nc.vo.mm.pub.pub1030.MoHeaderVO;
import nc.vo.mm.pub.pub1030.MoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

public class SaveMo6600Impl implements IHttpServletAdaptor {
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
        String result = saveMo6600(sb.toString());
        br.close();
        response.getOutputStream().write(result.getBytes("UTF-8"));
    }
	IUAPQueryBS uap =(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	IUifService uif = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());
	public String saveMo6600(String json) {
		// TODO Auto-generated method stub
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//获取数据源
		InvocationInfoProxy.getInstance().setUserDataSource(source);//设置数据源
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
		StringBuffer allInfo = new StringBuffer();
		allInfo.append("[");
		for(int j = 0;j<lsmap.size();j++){
			StringBuffer all = new StringBuffer();
			String error = "";
			MoVO aggvo = new MoVO();//聚合VO
			MoHeaderVO hvo = new MoHeaderVO();//主表VO
			Map mls = lsmap.get(j);
			String cvmid = mls.get("cvmid")==null?"":mls.get("cvmid").toString();//CVM
			if(StringUtils.isBlank(cvmid)){
				error = "{\"status\":\"error\",\"message\":\"CVMID为空\"}"; 
				all.append(error+",");
			}
			String applyCorp=mls.get("corp")==null?"":mls.get("corp").toString();//申请公司
			if(StringUtils.isBlank(applyCorp)){
				error = "{\"status\":\"error\",\"message\":\"公司为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			//add by zwx 2019-3-28 增加cvmid作为唯一标识，防止生产订单重复导入
			String result = isExist(cvmid,applyCorp);
			if(result.length()>0){
				return result;
			}
			//end by zwx
			String username=mls.get("username")==null?"":mls.get("username").toString();//用户名
			if(StringUtils.isNotBlank(username)){
				if(!username.equals("baosteel")){
					error =  "{\"status\":\"error\",\"message\":\"用户名错误\",\"cvmid\":\""+cvmid+"\"}"; 
					all.append(error+",");
				}
			} else {
				error = "{\"status\":\"error\",\"message\":\"用户名为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String pwd=mls.get("password")==null?"":mls.get("password").toString();//密码
			if(StringUtils.isNotBlank(pwd)){
				if(!pwd.equals("123456")){
					error = "{\"status\":\"error\",\"message\":\"密码错误\",\"cvmid\":\""+cvmid+"\"}"; 
					all.append(error+",");
				}
			} else {
				error = "{\"status\":\"error\",\"message\":\"密码为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			
			String corpSql = "select a.pk_corp from bd_corp a where a.unitcode = '"+applyCorp+"' and nvl(a.dr,0)=0";
			List corpList = null;
			try {
				corpList = (List) uap.executeQuery(corpSql, new MapListProcessor());
				if(corpList == null || corpList.size() == 0){
					error = "{\"status\":\"error\",\"message\":\"公司在NC系统不存在，请检查\",\"cvmid\":\""+cvmid+"\"}"; 
					all.append(error+",");
				}
			} catch (BusinessException e4) {
				e4.printStackTrace();
				error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e4.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			Map map = (Map) corpList.get(0);
			String pk_corp = map.get("pk_corp").toString();
			String scxname = mls.get("scxname")==null?"":mls.get("scxname").toString();//生产线名称
			if(StringUtils.isBlank(scxname)){
				scxname = "生产一线";//生产一线:铝一线    生产二线:铝二线
			}
			String scxid = "";
			try {
				scxid = getScxId(pk_corp, scxname);
			} catch (BusinessException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
				error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e3.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			if(StringUtils.isBlank(scxid)){
				error = "{\"status\":\"error\",\"message\":\"生产线名称在NC系统不存在或不属于该公司\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String custcode = mls.get("custcode") == null?"":mls.get("custcode").toString();
			if(StringUtils.isBlank(custcode)){
				error = "{\"status\":\"error\",\"message\":\"客商编码为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String invcode = mls.get("invcode") ==null?"":mls.get("invcode").toString();
			if(StringUtils.isBlank(invcode)){
				error = "{\"status\":\"error\",\"message\":\"物料编码为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String invid ="" ;
			try {
				invid = getInvId(invcode);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			if(StringUtils.isBlank(invid)){
				error = "{\"status\":\"error\",\"message\":\"物料编码"+invcode+"在NC不存在或未分配给该公司，请去集团分配\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			String version = "";
			try {
				version = getVersion(invid, pk_corp);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e1.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			if(StringUtils.isBlank(version)){
				error = "{\"status\":\"error\",\"message\":\"该物料没有bom信息\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
				//version = "1.0";
			}
			String jhkgrq = mls.get("jhkgrq") == null?"":mls.get("jhkgrq").toString();
			if(StringUtils.isBlank(jhkgrq)){
				error = "{\"status\":\"error\",\"message\":\"计划开工日期为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String jhwgrq = mls.get("jhwgrq") == null?"":mls.get("jhwgrq").toString();
			if(StringUtils.isBlank(jhwgrq)){
				error = "{\"status\":\"error\",\"message\":\"计划完工日期为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			if(new UFDateTime(jhwgrq).before(new UFDateTime(jhkgrq))){
				error = "{\"status\":\"error\",\"message\":\"计划完工日期不得小于计划开工日期\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String jhksrq = "";
			String jhkssj = "";
			String jhjsrq = "";
			String jhjssj = "";
			if(StringUtils.isNotBlank(jhkgrq) && StringUtils.isNotBlank(jhwgrq)){
				jhksrq = jhkgrq.substring(0, 10);
				jhkssj = jhkgrq.substring(jhkgrq.length()-8);
				jhjsrq = jhwgrq.substring(0, 10);
				jhjssj = jhwgrq.substring(jhkgrq.length()-8);
			}
			String jhwgsl = mls.get("jhsl") == null?"":mls.get("jhsl").toString();
			if(StringUtils.isBlank(jhwgsl)){
				error = "{\"status\":\"error\",\"message\":\"计划完工数量为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String usercode = mls.get("usercode")==null?"":mls.get("usercode").toString();//制单人编码
			String operatid = "";
			try {
				operatid = getUserId(usercode);
			} catch (BusinessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e2.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			if(StringUtils.isBlank(operatid)){
				error = "{\"status\":\"error\",\"message\":\"制单人编码在NC系统不存在，请检查\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String meanDoc=mls.get("meandoc")==null?"":mls.get("meandoc").toString();//主计量单位编码
			if(StringUtils.isBlank(meanDoc)){
				error = "{\"status\":\"error\",\"message\":\"主计量单位为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String meanDocid = "";
			try {
				meanDocid = getMeasDocId(meanDoc);
			} catch (BusinessException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
				error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e4.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			if(StringUtils.isBlank(meanDocid)){
				error = "{\"status\":\"error\",\"message\":\"主计量单位在NC系统不存在，请检查\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			String pk_calbody = "";
			try {
				pk_calbody = getCalBodyId(pk_corp);
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e1.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			
			StringBuffer cust = new StringBuffer();
			cust.append(" select b.pk_cumandoc,a.custname from  bd_cubasdoc a ") 
			.append("  left join bd_cumandoc b on a.pk_cubasdoc = b.pk_cubasdoc ") 
			.append("  where (nvl(a.dr,0)=0 and nvl(b.dr,0)=0) ") 
			.append("  and (b.custflag='0' or b.custflag='1' or b.custflag='2') ") 
			.append("  and b.pk_corp='"+pk_corp+"' ") 
			.append("  and a.custcode='"+custcode+"' ");
            List custlist = null;
            try {
				custlist = (List) uap.executeQuery(cust.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			Map custmap = null;
			if(custlist!=null&&custlist.size()>0){
			    custmap = (Map) custlist.get(0);
			}else{
				error = "{\"status\":\"error\",\"message\":\"客户编码"+custcode+"在NC系统不存在或未分配给该公司，请去集团分配\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			String pk_cumandoc = "";
			String custname = "";
			if(custmap != null && custmap.size()>0){
				pk_cumandoc = custmap.get("pk_cumandoc")==null?"":custmap.get("pk_cumandoc").toString();
				custname = custmap.get("custname")==null?"":custmap.get("custname").toString();
			}
			StringBuffer inv = new StringBuffer();
			inv.append(" select a.pk_invbasdoc,a.invname ,b.pk_produce from ") 
			.append("   bd_invbasdoc a  ") 
			.append("   left join bd_produce b on a.pk_invbasdoc = b.pk_invbasdoc ") 
			.append("   where a.invcode = '"+invcode+"' and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and b.pk_corp = '"+pk_corp+"' ") ;
			List invlist = null;
            try {
            	invlist = (List) uap.executeQuery(inv.toString(), new MapListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			Map invmap = null;
			if(invlist!=null&&invlist.size()>0){
				invmap = (Map) invlist.get(0);
			}else{
				error = "{\"status\":\"error\",\"message\":\"物料编码"+invcode+"在NC系统不存在或该物料未分配给该公司，请去集团分配\",\"cvmid\":\""+cvmid+"\"}";
				all.append(error+",");
			}
			String pk_invbasdoc = "";
			String invname = "";
			String pk_produce = "";
			if(invmap !=  null && invmap.size()>0){
				pk_invbasdoc = invmap.get("pk_invbasdoc")==null?"":invmap.get("pk_invbasdoc").toString();
				invname = invmap.get("invname")==null?"":invmap.get("invname").toString();
				pk_produce = invmap.get("pk_produce")==null?"":invmap.get("pk_produce").toString();
			}
			String zdsl = mls.get("ninspacenum")==null?"":mls.get("ninspacenum").toString();//整垛数量
			if(StringUtils.isBlank(zdsl)){
				error = "{\"status\":\"error\",\"message\":\"整垛数量为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String bzwtype = mls.get("bzfs")==null?"":mls.get("bzfs").toString();//包装物方式
			if(StringUtils.isBlank(bzwtype)){
				error = "{\"status\":\"error\",\"message\":\"包装物方式为空\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String memo = mls.get("memo")==null?"":mls.get("memo").toString();//备注
			int num =StringUtils.countMatches(bzwtype, "/");
			if(num != 2){
				error = "{\"status\":\"error\",\"message\":\"请检查包装物类型格式(XXX/XXX/XXX)\",\"cvmid\":\""+cvmid+"\"}"; 
				all.append(error+",");
			}
			String tuopan = bzwtype.substring(0, bzwtype.indexOf("/"));//托盘
			String dinggai = bzwtype.substring(bzwtype.indexOf("/")+1, bzwtype.lastIndexOf("/"));//顶盖
			String dianguanzhi = bzwtype.substring(bzwtype.lastIndexOf("/")+1);//垫罐纸
			hvo.setBomver(version);//BOM版本
			hvo.setDczt(0);//倒冲状态
			hvo.setDdlx(0);//订单类型
			hvo.setMemo(memo);//备注
			hvo.setAttributeValue("dr", 0);
			hvo.setDyzt(0);//打印状态
			hvo.setGzzxid(scxid);//生产线主键
			hvo.setGzzxmc(scxname);//生产线名称
			hvo.setGcbm(pk_calbody);//工厂
			hvo.setJhkgrq(new UFDate(jhksrq));//计划开工日期
			hvo.setJhkssj(jhkssj);//计划开始时间
			hvo.setJhwgrq(new UFDate(jhjsrq));//计划完工日期
			hvo.setJhjssj(jhjssj);//计划结束时间
			hvo.setJhwgsl(new UFDouble(jhwgsl));//计划完工数量
			hvo.setJldwid(meanDocid);//计量单位ID
			hvo.setKsid(pk_cumandoc);//客商ID
			hvo.setKsbm(custcode);//客商编码
			hvo.setKsmc(custname);//客商名称
			hvo.setPk_corp(pk_corp);//公司主键
			hvo.setPk_produce(pk_produce);//物料PK
			if(pk_corp.equals("1016")){
				hvo.setScbmid("1016A210000000000482");//生产部门ID(上海)
			}else if(pk_corp.equals("1071")){
				hvo.setScbmid("1071A21000000000V2MR");//生产部门ID(武汉)
			}else if(pk_corp.equals("1103")){
				hvo.setScbmid("1103A21000000000034G");//生产部门ID(哈尔滨)
			}else if(pk_corp.equals("1097")){
				hvo.setScbmid("1097A210000000001DIF");//生产部门ID(河南)
			}else if(pk_corp.equals("1017")){
				hvo.setScbmid("1017A2100000000003QF");//生产部门ID(河北)
			}else if(pk_corp.equals("1018")){
				hvo.setScbmid("1018A21000000000045F");//生产部门ID(成都)
			}else if(pk_corp.equals("1019")){
				hvo.setScbmid("1019A2100000000003R9");//生产部门ID(佛山)
			}else if(pk_corp.equals("1107")){
				hvo.setScbmid("110711100000000004P2");//生产部门ID(宝翼2)
			}
			hvo.setSfdc(new UFBoolean("N"));//是否倒冲
			hvo.setSfjj(new UFBoolean("N"));//是否加急
			hvo.setSfypg(new UFBoolean("N"));//是否已派工
			hvo.setSjwgsl(new UFDouble(0));//实际完工数量
			hvo.setWlbmid(pk_invbasdoc);//物料编码ID
			hvo.setWlbm(invcode);
			hvo.setWlmc(invname);
			hvo.setZdrq(new UFDate(new Date()));//制单日期
			hvo.setZdrid(operatid);//制单人
			hvo.setZt("A");//单据状态
			hvo.setBusiDate(new UFDate(new Date()));
			hvo.setStatus(2);
			hvo.setZyx1(zdsl);
			hvo.setUserid(operatid);
			hvo.setPch(Getnum());//批次号
//			hvo.setZdy3(bzwtype);
//			hvo.setZdy4(zdsl);
			aggvo.setParentVO(hvo);
			aggvo.setChildrenVO(null);
			ArrayList list = null;
			if(all.length()>0){
				allInfo.append(all);
			}else{
				try {
					InvocationInfoProxy.getInstance().setUserCode(operatid);
					InvocationInfoProxy.getInstance().setCorpCode(pk_corp);
					list = (ArrayList) new PfUtilBO().processAction("SAVE", "A2", new UFDate(new Date()).toString(), null, aggvo, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					error = "{\"status\":\"error\",\"message\":\"生产订单保存失败:"+e.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
				    all.append(error+",");
				    allInfo.append(all);
				    continue;
				}
			}
			
			//add by zwx 2019-3-28 增加cvmid标识
			if(list!=null){
				StringBuffer updsql = new StringBuffer();
				updsql.append(" update mm_mo set vdef9 = '"+cvmid+"'  ") 
				  .append("  where pk_moid = '"+list.get(0)+"' ") 
				  .append("    and pk_corp = '"+pk_corp+"' ") 
				  .append("    and nvl(dr, 0) = 0 ") ;

				IPubDMO ipubdmo = (IPubDMO)NCLocator.getInstance().lookup(IPubDMO.class.getName());
		    	try {
					ipubdmo.executeUpdate(updsql.toString());
				} catch (BusinessException e1) {
					e1.printStackTrace();
					error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e1.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
					all.append(error+",");
					allInfo.append(all);
				    continue;
				} 
			}
			
			//end by zwx
			if(list != null && list.size()>0){
				//error = "{\"status\":\"success\",\"message\":\"生产订单保存成功\",\"cvmid\":\""+cvmid+"\",\"billcode\":\""+list.get(1)+"\"}";
				MoVO tfaggvo = null;
				try {
					tfaggvo = getAggVO(list.get(1).toString(), pk_corp);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					error = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
					all.append(error+",");
					allInfo.append(all);
				    continue;
				}
				String ctime = new UFDateTime(System.currentTimeMillis()).getTime();
				tfaggvo.getHeadVO().setZt("B");
				tfaggvo.getHeadVO().setSjkgrq(new UFDate(new Date()));
				tfaggvo.getHeadVO().setSjkssj(ctime);
				tfaggvo.getHeadVO().setShrid(operatid);
				tfaggvo.getHeadVO().setShrq(
						new UFDate(new Date()));
				tfaggvo.getHeadVO().setBusiDate(new UFDate(new Date()));
				ArrayList tflist = null;
				try {
					tflist = (ArrayList) new PfUtilBO().processAction("MOPUT", "A2", new UFDate(new Date()).toString(), null, tfaggvo, null);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					if(e2 instanceof ClassCastException){
						
					}else{
						error = "{\"status\":\"error\",\"message\":\"生产订单"+list.get(1)+"投放失败:"+e2.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
						all.append(error+",");
						allInfo.append(all);
					    continue;
					}
				}
				error = "{\"status\":\"success\",\"message\":\"生产订单"+list.get(1)+"投放成功\",\"cvmid\":\""+cvmid+"\",\"billcode\":\""+list.get(1)+"\"}";
				all.append(error+",");
				allInfo.append(all);
				BzfsVO bzfsvo = getBzfsVO(tuopan, dinggai, dianguanzhi, list.get(0).toString(),list.get(1).toString(),pk_corp,invcode,zdsl);
				try {
					uif.insert(bzfsvo);
				} catch (UifException e) {
					e.printStackTrace();
				}
			}
		}
		allInfo.deleteCharAt(allInfo.length()-1);
		allInfo.append("]");
		return allInfo.toString();
	}
	public BzfsVO getBzfsVO(String tuopan,String dinggai,String dianguanzhi,String pk_moid,String scddh,String pk_corp,String invcode,String zdsl){
		BzfsVO bzfsvo = new BzfsVO();
		bzfsvo.setPk_moid(pk_moid);
		bzfsvo.setScddh(scddh);
		bzfsvo.setPk_corp(pk_corp);
		bzfsvo.setTuopan(tuopan);
		bzfsvo.setDinggai(dinggai);
		bzfsvo.setDianguanzhi(dianguanzhi);
		bzfsvo.setDr(0);
		bzfsvo.setInvcode(invcode);
		bzfsvo.setDef1(zdsl);
		return bzfsvo;
	}
	/**
   	 *根据制单人编码获取制单人主键
   	 *by src 2017年12月12日10:58:50
   	 * @throws BusinessException 
   	 */
   	public String getUserId(String usercode) throws BusinessException{
   		String operateid = "";
   		String sql = "select cuserid from sm_user where user_code = '"+usercode+"' and nvl(dr,0)=0";
		operateid = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return operateid;
   	}
   	/**
   	 * 根据计量单位编码获取计量单位主键
   	 * by src 2017年12月12日11:06:41
   	 * @throws BusinessException 
   	 */
   	public String getMeasDocId(String shortname) throws BusinessException{
   		String pk_measdoc = "";
   		String sql = "select pk_measdoc from bd_measdoc where measname='"+shortname+"' and nvl(dr,0)=0 ";
		pk_measdoc = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return pk_measdoc;
   	}
   	/**
   	 * 根据公司主键获取库存组织主键
   	 * by src 2017-12-12 11:45:11
   	 * @throws BusinessException 
   	 */
   	public String getCalBodyId(String corp) throws BusinessException{
   		String pk_calbody = "";
   		String sql = "select pk_calbody from bd_calbody where pk_corp = '"+corp+"' and  nvl(dr,0)=0 ";
		pk_calbody = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return pk_calbody;
   	}
   	/**
	 * 根据物料编码获取物料主键
	 * by src 2018年1月17日18:06:27
	 * @throws BusinessException 
	 */
	public String getInvId(String invcode) throws BusinessException{
		String invid = "";
		String sql = "select pk_invbasdoc from bd_invbasdoc where invcode='"+invcode+"' and nvl(dr,0)=0";
		invid = (String) uap.executeQuery(sql, new ColumnProcessor());
		return invid;
	}
	/**
   	 * 如果该物料没有bom信息，则版本号为1.0 反之取版本号最高的+0.1
   	 * @throws BusinessException 
   	 */
   	public String getVersion(String invid ,String pk_corp) throws BusinessException{
   		String version = "";
   		String sql = "select version from (select version from bd_bom where pk_corp = '"+pk_corp+"' and wlbmid='"+invid+"' and nvl(dr,0)=0 order by version desc) where rownum = 1";
   		version = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return version;
   	}
   	/**
   	 * 根据生产线名称获取生产线主键
   	 * @throws BusinessException 
   	 */
   	public String getScxId(String pk_corp ,String scxname) throws BusinessException{
   		String scxid = "";
   		String sql = "select a.pk_wkid from pd_wk a  where a.pk_corp = '"+pk_corp+"' and a.gzzxmc = '"+scxname+"' and nvl(a.dr,0)=0";
   		scxid = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return scxid;
   	}
   	
   	/**
     * 获取生成订单聚合VO
     * src 2017年11月28日10:46:59
     * @throws BusinessException 
     */
	public MoVO getAggVO(String billcode,String corp) throws BusinessException{
		MoVO aggvo = new MoVO();
		MoHeaderVO hvo = new MoHeaderVO();
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from mm_mo a where a.scddh = '"+billcode+"' and a.pk_corp='"+corp+"' and nvl(a.dr,0)=0");
		List<MoHeaderVO> hvoList = null;
			hvoList = (List) uap.executeQuery(sb.toString(), new BeanListProcessor(MoHeaderVO.class));		
		if(hvoList != null && hvoList.size()>0){
			hvo = hvoList.get(0);
			hvo.setGzzxid(getScxId(corp,""));
			hvo.setUserid(hvo.getZdrid());
			aggvo.setParentVO(hvo);
			aggvo.setChildrenVO(null);
		}else{
			aggvo=null;
		}
		return aggvo;
	}
	
	/** 
     * 获取现在时间 
     * @return返回字符串格式yyMMdd
     */  
      public static String getStringDate() {  
             Date currentTime = new Date();  
             SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");  
             String dateString = formatter.format(currentTime);  
             return dateString;  
          }  
      /** 
       * 由年月日时分秒+4位随机数 
       * 生成流水号 
       * @return 
       */  
      public static String Getnum(){  
          String t = getStringDate();  
          int x=(int)(Math.random()*9000)+1000;  
          String serial = t + x;  
          return serial;  
      } 
      
      //add by zwx 2019-3-28 判断是否存在cvmid一致的数据
      public String isExist(String cvmid,String corp){
    	  StringBuffer sql = new StringBuffer();
    	  sql.append(" select mm.scddh from mm_mo mm ") 
    	  .append(" inner join bd_corp corp ") 
    	  .append(" on mm.pk_corp = corp.pk_corp ") 
    	  .append(" where mm.vdef9 = '"+cvmid+"' ") 
    	  .append(" and corp.unitcode = '"+corp+"' ") 
    	  .append(" and nvl(mm.dr,0) = 0 ") 
    	  .append(" and nvl(corp.dr,0) = 0 ") ;
    	  
    	  String msg = "";//返回提示信息

    	  List list = null;
			try {
				list = (List) uap.executeQuery(sql.toString(), new MapListProcessor());
				if(list == null || list.size() == 0){
					return msg;//不存在重复
				}else{
					Map map = (Map) list.get(0);
					String scddh = map.get("scddh").toString();	
					msg = "{\"status\":\"error\",\"message\":\"公司："+corp+"已生成生产订单,订单号："+scddh+"\",\"cvmid\":\""+cvmid+"\"}";
				}
			} catch (BusinessException e4) {
				e4.printStackTrace();
				msg = "{\"status\":\"error\",\"message\":\"数据库操作异常，请稍后重试:"+e4.getMessage()+"\",\"cvmid\":\""+cvmid+"\"}";
				
			}
			return msg;

      }
      //end by zwx
}
