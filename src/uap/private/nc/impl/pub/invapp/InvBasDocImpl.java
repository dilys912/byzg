package nc.impl.pub.invapp;

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

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.mp.mp4020.ProduceDMO;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.uap.sfapp.util.SFAppServiceUtil;
import nc.itf.ic.tool.Produce;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.b431.ProduceBO_Client;
import nc.ui.pub.ClientEnvironment;
import nc.vo.bd.b431.ProduceMapping;
import nc.vo.bd.b431.ProduceVO;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.by.invapp.h0h002.InvappdocVO;
import nc.vo.by.invapp.h0h002.MyExAggVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;

import org.apache.commons.lang.StringUtils;
/**
 * CVM������������ӿ�
 * by src 2017��10��31��14:40:42
 */
@SuppressWarnings("all")
public class InvBasDocImpl implements IHttpServletAdaptor {
	IUAPQueryBS uap =(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	 public void doAction(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	// TODO Auto-generated method stub
		    request.setCharacterEncoding("UTF-8");
			InputStream in = request.getInputStream();  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));  
	        String s = "";  
	        StringBuffer sb = new StringBuffer();  
	        while ((s = br.readLine()) != null) {  
	            sb.append(s);  
	        }
	        String result = sendInvCodeToVMS(sb.toString());
	        br.close();
	        response.getOutputStream().write(result.getBytes("UTF-8"));
	    }
	public String sendInvCodeToVMS(String json) {
		String source = InvocationInfoProxy.getInstance().getUserDataSource();//��ȡ����Դ
		InvocationInfoProxy.getInstance().setUserDataSource(source);//��������Դ
		nc.net.sf.json.JSONArray jsonarray = null;
		try {
			jsonarray = new nc.net.sf.json.JSONArray().fromObject(json);
		} catch (Exception e) {
			return "[{\"status\":\"error\",\"message\":\"json��������-����json��ʽ:"+e.getMessage()+"\"}]";
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
		for(int i =0 ; i<lsmap.size() ; i++){
			Map jsonMap = lsmap.get(i);
			String flag = jsonMap.get("flag")==null?"":jsonMap.get("flag").toString();//������ʶ
			if(StringUtils.isBlank(flag)){
				return "[{\"status\":\"error\",\"message\":\"������ʶflagΪ��\"}]"; 
			}
			String username=jsonMap.get("username")==null?"":jsonMap.get("username").toString();//�û���
			if(StringUtils.isNotBlank(username)){
				if(!username.equals("baosteel")){
					return "[{\"status\":\"error\",\"message\":\"�û�������\"}]"; 
				}
			} else {
				return "[{\"status\":\"error\",\"message\":\"�û���Ϊ��\"}]"; 
			}
			String pwd=jsonMap.get("password")==null?"":jsonMap.get("password").toString();//����
			if(StringUtils.isNotBlank(pwd)){
				if(!pwd.equals("123456")){
					return "[{\"status\":\"error\",\"message\":\"�������\"}]"; 
				}
			} else {
				return "[{\"status\":\"error\",\"message\":\"����Ϊ��\"}]"; 
			}
			if(flag.equals("add")){
				//���ɴ�����뵥�����ֶ�
				MyExAggVO aggvo = new MyExAggVO();//�ۺ�VO
				InvappdocVO appvo = new InvappdocVO();//������뵥VO
				String applyCorp=jsonMap.get("corp")==null?"":jsonMap.get("corp").toString();//���빫˾
				if(StringUtils.isBlank(applyCorp)){
					return "[{\"status\":\"error\",\"message\":\"���빫˾Ϊ��\"}]"; 
				}
				String corpSql = "select a.pk_corp from bd_corp a where a.unitcode = '"+applyCorp+"' and nvl(a.dr,0)=0";
				List corpList = null;
				try {
					corpList = (List) uap.executeQuery(corpSql, new MapListProcessor());
					if(corpList == null || corpList.size() == 0){
						return "[{\"status\":\"error\",\"message\":\"���빫˾��NCϵͳ�����ڣ�����\"}]"; 
					}
				} catch (BusinessException e4) {
					e4.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e4.getMessage()+"\"}]"; 
				}
				Map map = (Map) corpList.get(0);
				String pk_corp = map.get("pk_corp").toString();
				String calBody = "";
				try {
					calBody = getCalBodyId(pk_corp);
				} catch (BusinessException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e3.getMessage()+"\"}]";
				}//�����֯
				String invcl=jsonMap.get("invcl")==null?"":jsonMap.get("invcl").toString();//�������
				if(StringUtils.isBlank(invcl)){
					return "[{\"status\":\"error\",\"message\":\"�������Ϊ��\"}]"; 
				}
				String invclid = "";
				try {
					invclid = getInvclassId(invcl, pk_corp);
				} catch (BusinessException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e4.getMessage()+"\"}]"; 
				}
				if(StringUtils.isBlank(invclid)){
					return "[{\"status\":\"error\",\"message\":\"���������NCϵͳ�����ڣ�����\"}]"; 
				}
				String endflag = "error";
				try {
					endflag = isendflog(invclid,pk_corp);
				} catch (BusinessException e4) {
					e4.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e4.getMessage()+"\"}]"; 
				}
				if(endflag.equals("error")){
					return "[{\"status\":\"error\",\"message\":\"������಻��ĩ����������ѡ��\"}]"; 
				}				
				String invcode=jsonMap.get("invcode")==null?"":jsonMap.get("invcode").toString();//�������
				if(StringUtils.isBlank(invcode)){
					//invcode = invcl+Getnum();//�������
					try {
						invcode = getInvcode(invclid, invcl);
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]"; 
					}
				}
				String invname=jsonMap.get("invname")==null?"":jsonMap.get("invname").toString();//�������
				if(StringUtils.isBlank(invname)){
					return "[{\"status\":\"error\",\"message\":\"�������Ϊ��\"}]"; 
				}
				String invspec=jsonMap.get("invspec")==null?"":jsonMap.get("invspec").toString();//���
				String invtype=jsonMap.get("invtype")==null?"":jsonMap.get("invtype").toString();//�ͺ�
				String meanDoc=jsonMap.get("meandoc")==null?"":jsonMap.get("meandoc").toString();//��������λ����
				if(StringUtils.isBlank(meanDoc)){
					return "[{\"status\":\"error\",\"message\":\"��������λΪ��\"}]"; 
				}
				String meanDocid = "";
				try {
					meanDocid = getMeasDocId(meanDoc);
				} catch (BusinessException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e4.getMessage()+"\"}]"; 
				}
				if(StringUtils.isBlank(meanDocid)){
					return "[{\"status\":\"error\",\"message\":\"��������λ��NCϵͳ�����ڣ�����\"}]";
				}
				BillcodeGenerater bc= new BillcodeGenerater();
				String billcode="";//��ȡ���ݺ�
				try {
					billcode = SFAppServiceUtil.getBillcodeRuleService().getBillCode_RequiresNew("TA02", pk_corp,
					        null, null);
				} catch (ValidationException e2) {
					e2.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"��ȡ���ݺ��쳣:"+e2.getMessage()+"\"}]";
				} catch (BusinessException e2) {
					e2.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"��ȡ���ݺ��쳣:"+e2.getMessage()+"\"}]";
				}
				String usercode = jsonMap.get("usercode")==null?"":jsonMap.get("usercode").toString();//�Ƶ��˱���
				if(StringUtils.isBlank(usercode)){
					return "[{\"status\":\"error\",\"message\":\"�Ƶ��˱���Ϊ��\"}]";
				}
				String operatid = "";
				try {
					operatid = getUserId(usercode);
				} catch (BusinessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e2.getMessage()+"\"}]";
				}
				if(StringUtils.isBlank(operatid)){
					return "[{\"status\":\"error\",\"message\":\"�Ƶ��˱�����NCϵͳ�����ڣ�����\"}]"; 
				}
				String vfree1 = jsonMap.get("vfree1")==null?"":jsonMap.get("vfree1").toString();//����������1
				String defid = "";
				try {
					defid = getDefId(vfree1);
				} catch (BusinessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e2.getMessage()+"\"}]";
				}
				if(StringUtils.isBlank(defid)){
					return "[{\"status\":\"error\",\"message\":\"������1��NCϵͳ�����ڣ�����\"}]"; 
				}
				String matertype = jsonMap.get("matertype")==null?"":jsonMap.get("matertype").toString();//��������
				String matertypename = "";
				if(StringUtils.isBlank(matertype)){
					matertype = "0";
				}
				if(matertype.equals("0")){
					matertypename = "�����";
				}else{
					matertypename = "�ɹ���";
				}
				String custcode = jsonMap.get("custcode")==null?"":jsonMap.get("custcode").toString();//Ĭ�Ͽͻ�
				StringBuffer cust = new StringBuffer();
				cust.append(" select b.pk_cumandoc,a.pk_cubasdoc from  bd_cubasdoc a ") 
				.append("  left join bd_cumandoc b on a.pk_cubasdoc = b.pk_cubasdoc ") 
				.append("  where (nvl(a.dr,0)=0 and nvl(b.dr,0)=0) ") 
				.append("  and (b.custflag='0' or b.custflag='1' or b.custflag='2') ") 
				//.append("  and (a.pk_corp='0001' and b.pk_corp='"+pk_corp+"') ") //������̻���������˾��������0001������Ҫȥ��������
				.append("  and (b.pk_corp='"+pk_corp+"') ")
				.append("  and a.custcode='"+custcode+"' ");
	            List custlist = null;
	            try {
					custlist = (List) uap.executeQuery(cust.toString(), new MapListProcessor());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return  "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
				}
				Map custmap = null;
				if(custlist!=null&&custlist.size()>0){
				    custmap = (Map) custlist.get(0);
				}else{
					return "[{\"status\":\"error\",\"message\":\"�ͻ�������NCϵͳ�����ڻ�δ������ù�˾������\"}]";
				}
				String pk_cubasdoc = "";
				if(custmap != null && custmap.size()>0){
					pk_cubasdoc = custmap.get("pk_cubasdoc")==null?"":custmap.get("pk_cubasdoc").toString();
				}
				String def1 = jsonMap.get("cpdl")==null?"":jsonMap.get("cpdl").toString();//��Ʒ����
				String def13 = jsonMap.get("cpxt")==null?"":jsonMap.get("cpxt").toString();//��Ʒϵͳ
				String def14 = jsonMap.get("cpxl")==null?"":jsonMap.get("cpxl").toString();//��Ʒϵ��
				String def15 = jsonMap.get("cpgy")==null?"":jsonMap.get("cpgy").toString();//��Ʒ����
				String def16 = jsonMap.get("gwzz")==null?"":jsonMap.get("gwzz").toString();//��������
				String def1id = "";
				String def13id = "";
				String def14id = "";
				String def15id = "";
				String def16id = "";
				try {
					def1id = getDefIds(def1, "BY22");
					def13id = getDefIds(def13, "BZ007");
					def14id = getDefIds(def14, "BZ008");
					def15id = getDefIds(def15, "BZ009");
					def16id = getDefIds(def16, "BZ010");
				} catch (BusinessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					return  "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e2.getMessage()+"\"}]";
				}
				String def8 = jsonMap.get("sapinvcode")==null?"":jsonMap.get("sapinvcode").toString();//sap���ϱ���
				String gldef10 = jsonMap.get("gldef10")==null?"":jsonMap.get("gldef10").toString();//��Ŀ����
				String gldef11 = jsonMap.get("gldef11")==null?"":jsonMap.get("gldef11").toString();//��Ŀ����1
				String gldef12 = jsonMap.get("gldef12")==null?"":jsonMap.get("gldef12").toString();//������ۿ�Ʊ��
				String gldef16 = jsonMap.get("gldef16")==null?"":jsonMap.get("gldef16").toString();//��񣨺ϸ�֤��ӡ��
				String gldef10id = null;
				String gldef11id = null;
				String gldef12id = null;
				String gldef16id = null;
				if(StringUtils.isNotBlank(gldef10)){
					try {
						gldef10id = getJobManId(gldef10, pk_corp);
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return  "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
					}
				}
				if(StringUtils.isNotBlank(gldef11)){
					try {
						gldef11id = getJobManId(gldef11, pk_corp);
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return  "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
					}
				}
				try {
					gldef12id = getDefIds(gldef12, "BZ017");
					gldef16id = getDefIds(gldef16, "BZ016");
				} catch (BusinessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					return  "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e2.getMessage()+"\"}]";
				}
				appvo.setPk_corp(pk_corp);//��˾����
				appvo.setPk_corp_app(pk_corp);//���빫˾
				appvo.setVbillstatus(8);//8����̬ 1��� 3 �ύ
				appvo.setVbillcode(billcode);//���ݺ�
				appvo.setPk_billtype("TA02");//��������
				appvo.setVoperatorid(operatid);//�Ƶ���
				appvo.setDr(0);//ɾ����ʶ
				appvo.setInvcode(invcode);//�������
				appvo.setInvname(invname);//�������
				appvo.setPk_invcl(invclid);//�������
				appvo.setInvspec(invspec);//���
				appvo.setInvtype(invtype);//�ͺ�
				appvo.setPk_measdoc(meanDocid);//��������λ
				appvo.setAbctype("A");//abc����
				appvo.setOutpriority(Integer.valueOf(0));//�������ȼ�
				appvo.setPurchasestge(Integer.valueOf(1));//�ɹ�����
				appvo.setInvlifeperiod(Integer.valueOf(0));//�����������
				appvo.setDmakedate(new UFDate(new Date()));//��������
				appvo.setUnitvolume(new UFDouble("0.00000000"));
				appvo.setUnitweight(new UFDouble("0.00000000"));
				appvo.setExpaybacktax(new UFDouble("0.00000000"));
				appvo.setKeepwasterate(new UFDouble("0.00000000"));
				appvo.setLowestprice(new UFDouble("0.00000000"));
				appvo.setMaxprice(new UFDouble("0.00000000"));
				appvo.setPurwasterate(new UFDouble("0.00000000"));
				appvo.setRefsaleprice(new UFDouble("0.00000000"));
				appvo.setMinmulnum(new UFDouble("1.00000000"));
				appvo.setRoundingnum(new UFDouble("0.50000000"));
				appvo.setAssistunit(new UFBoolean("N"));
				appvo.setAutobalancemeas(new UFBoolean("Y"));
				appvo.setAccflag(new UFBoolean("N"));
				appvo.setIsappendant(new UFBoolean("N"));
				appvo.setIsinvretfreeofchk(new UFBoolean("Y"));
				appvo.setIsinvretinstobychk(new UFBoolean("N"));
				appvo.setDiscountflag(new UFBoolean("N"));
				appvo.setIsmngstockbygrswt(new UFBoolean("N"));
				appvo.setIsstorebyconvert(new UFBoolean("N"));
				appvo.setLaborflag(new UFBoolean("N"));
				appvo.setSetpartsflag(new UFBoolean("N"));
				appvo.setIsautoatpcheck(new UFBoolean("Y"));
				appvo.setIscancalculatedinvcost(new UFBoolean("Y"));
				appvo.setIscanpurchased(new UFBoolean("Y"));
				appvo.setIscansaleinvoice(new UFBoolean("Y"));
				appvo.setIscansold(new UFBoolean("Y"));
				appvo.setIsconfigable(new UFBoolean("N"));
				appvo.setIsctlprodplanprice(new UFBoolean("N"));
				appvo.setIsfatherofbom(new UFBoolean("N"));
				appvo.setIsinvreturned(new UFBoolean("Y"));
				appvo.setIsnoconallowed(new UFBoolean("Y"));
				appvo.setIsprimarybarcode(new UFBoolean("N"));
				appvo.setIssalable(new UFBoolean("N"));
				appvo.setIssecondarybarcode(new UFBoolean("N"));
				appvo.setIsselfapprsupplier(new UFBoolean("Y"));
				appvo.setIsspecialty(new UFBoolean("N"));
				appvo.setIssupplierstock(new UFBoolean("N"));
				appvo.setIsvirtual(new UFBoolean("N"));
				appvo.setNegallowed(new UFBoolean("N"));
				appvo.setOuttrackin(new UFBoolean("N"));
				appvo.setQualitymanflag(new UFBoolean("N"));
				appvo.setSellproxyflag(new UFBoolean("N"));
				appvo.setSerialmanaflag(new UFBoolean("N"));
				appvo.setWholemanaflag(new UFBoolean("Y"));//���ι���
				appvo.setPk_taxitems("0001A21000000004PMKG");//˰Ŀ���ƹ�ͨ�ã�
				appvo.setAbcFundeg("C");
				appvo.setAbcGrosspft("C");
				appvo.setAbcPurchase("C");
				appvo.setAbssales("C");
				appvo.setChkFreeFlag(new UFBoolean("Y"));//�Ƿ����
				appvo.setCombineflag(new UFBoolean("N"));
				appvo.setConverseflag(new UFBoolean("N"));
				appvo.setFcpclgsfa(0);
				appvo.setIscostbyorder(new UFBoolean("N"));
				appvo.setIscreatesonprodorder(new UFBoolean("N"));
				appvo.setIsctlbyfixperiod(new UFBoolean("N"));
				appvo.setIsctoutput(new UFBoolean("Y"));
				appvo.setIselementcheck(new UFBoolean("N"));
				appvo.setIsfxjz(new UFBoolean("N"));
				appvo.setIssend(new UFBoolean("Y"));
				appvo.setIssendbydatum(new UFBoolean("N"));
				appvo.setIsuseroute(new UFBoolean("N"));
				appvo.setIswholesetsend(new UFBoolean("Y"));
				appvo.setJyrhzdyw(0);
				appvo.setLowlevelcode(0);
				appvo.setOuttype("0");
				appvo.setPchscscd(0);
				appvo.setPricemethod(4);
				appvo.setPrimaryflag(new UFBoolean("N"));
				appvo.setProducemethod(0);
				appvo.setRoadType(0);
				appvo.setScscddms(0);
				appvo.setScxybzsfzk(new UFBoolean("N"));
				appvo.setSfcbdx(new UFBoolean("N"));
				appvo.setSffzfw(new UFBoolean("N"));
				appvo.setSfpchs(new UFBoolean("N"));
				appvo.setSfzb(new UFBoolean("N"));
				appvo.setSfzzcp(new UFBoolean("N"));
				appvo.setStockByCheck(new UFBoolean("N"));
				appvo.setSupplytype("0");
				appvo.setVirtualflag(new UFBoolean("N"));
				appvo.setWghxcl(0);
				appvo.setIsrecurrentcheck(new UFBoolean("N"));
				appvo.setIsused(new UFBoolean("N"));
				appvo.setBomtype(0);
				appvo.setMaterclass(0);
				appvo.setMatertype(matertype);//��������
				appvo.setMatertype_show(matertypename);
				appvo.setFree1(defid);//������1
				appvo.setGldef10(gldef10id);//��Ŀ����
				appvo.setGldef11(gldef11id);//��Ŀ����2
				appvo.setGldef12(gldef12id);//������ۿ�Ʊ��
				appvo.setGldef15(gldef11id);//��Ŀ����2
				appvo.setGldef16(gldef16id);//��񣨺ϸ�֤��ӡ��
				appvo.setGldef17(pk_cubasdoc);//Ĭ�Ͽͻ�
				appvo.setDef1(def1id);
				appvo.setDef8(def8);//SAP���ϱ���
				appvo.setDef13(def13id);
				appvo.setDef14(def14id);
				appvo.setDef15(def15id);
				appvo.setDef16(def16id);
				appvo.setDef18("0001A21000000004EISF");//Ĭ��Ϊ��
				aggvo.setParentVO(appvo);
				aggvo.setChildrenVO(null);
				String strTime = ClientEnvironment.getServerTime().toString();
				ArrayList appSaveList = null;//���뵥���淵�ؽ��
				ArrayList appCheckList = null;//���뵥��˷��ؽ��
				//�����������뵥
				try {
					InvocationInfoProxy.getInstance().setCorpCode(pk_corp);//��������Դ
					InvocationInfoProxy.getInstance().setUserCode(operatid);//�����Ƶ���
					appSaveList = (ArrayList) new PfUtilBO().processAction("WRITE", "TA02", new UFDate(new Date()).toString(), null, aggvo, null);
				} catch (Exception e) {
					e.printStackTrace();
					if(e instanceof ClassCastException){
						
					}else{
						return "[{\"status\":\"error\",\"message\":\"�������뵥����ʧ��:"+e.getMessage()+"\"}]";
					}
				}
				InvappdocVO newAppVo = new InvappdocVO();
				MyExAggVO newAggvo = new MyExAggVO();
				String sql = "select *  from bd_invappdoc a where a.invcode='"+appvo.getInvcode()+"' and nvl(a.dr,0)=0";
				List<InvappdocVO> newAppVoList=null;
					try {
						InvocationInfoProxy.getInstance().setCorpCode(pk_corp);//��������Դ
						InvocationInfoProxy.getInstance().setUserCode(operatid);//�����Ƶ���
						newAppVoList = (List) uap.executeQuery(sql, new BeanListProcessor(InvappdocVO.class));
					} catch (BusinessException e1) {
						e1.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e1.getMessage()+"\"}]";
					}
					newAppVo = newAppVoList.get(0);
					newAppVo.setVbillstatus(3);
					newAppVo.setVapproveid(operatid);
					newAppVo.setDapprovedate(new UFDate(new Date()));
					newAggvo.setParentVO(newAppVo);
					newAggvo.setChildrenVO(null);
					newAggvo.setIsBillLock(true);
					newAggvo.setSendMessage(false);
				//�����������뵥
				if(appSaveList.size()>0){
			    	try {
			    		InvocationInfoProxy.getInstance().setCorpCode(pk_corp);//��������Դ
						InvocationInfoProxy.getInstance().setUserCode(operatid);//�����Ƶ���
			    		appCheckList = (ArrayList) new PfUtilBO().processAction("APPROVE", "TA02", new UFDate(new Date()).toString(), null, newAggvo, null);
					} catch (Exception e) {
						e.printStackTrace();
						if(e instanceof ClassCastException){
							
						}else{
							return "[{\"status\":\"error\",\"message\":\"�������뵥����ʧ��:"+e.getMessage()+"\"}]";
						}
					}
					Map invmap = new HashMap();
					try {
						invmap = getInvpk(invcode, pk_corp);
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						return "[{\"status\":\"error\",\"message\":\"���ϱ���ɹ������Ƿ��䵽��������ʧ�ܣ����ֶ�������\"}]";
					}
					String pk_invbasdoc = "";
					String pk_invmandoc = "";
					if(invmap.size()>0){
						pk_invbasdoc = invmap.get("pk_invbasdoc")==null?"":invmap.get("pk_invbasdoc").toString();
						pk_invmandoc = invmap.get("pk_invmandoc")==null?"":invmap.get("pk_invmandoc").toString();
						ProduceVO pvo = getProduceVO(pk_corp, calBody, pk_invmandoc, pk_invbasdoc, matertype);
						try {
							new BaseDAO().insertObject(pvo, new ProduceMapping());
						} catch (DAOException e) {
							return "[{\"status\":\"error\",\"message\":\"���ϱ���ɹ������Ƿ��䵽��������ʧ�ܣ����ֶ�������\"}]";
						}
					}
					return "[{\"status\":\"success\",\"message\":\"���������������ɹ����Ѿ��Զ����䵽��������\",\"invcode\":\""+invcode+"\"}]";
			    }
			}else if(flag.equals("update")){
				String def1 = jsonMap.get("cpdl")==null?"":jsonMap.get("cpdl").toString();//��Ʒ����
				String def13 = jsonMap.get("cpxt")==null?"":jsonMap.get("cpxt").toString();//��Ʒϵͳ
				String def14 = jsonMap.get("cpxl")==null?"":jsonMap.get("cpxl").toString();//��Ʒϵ��
				String def15 = jsonMap.get("cpgy")==null?"":jsonMap.get("cpgy").toString();//��Ʒ����
				String def16 = jsonMap.get("gwzz")==null?"":jsonMap.get("gwzz").toString();//��������
				String sealflag = jsonMap.get("sealflag")==null?"N":jsonMap.get("sealflag").toString(); //����־
				String corp = jsonMap.get("corp").toString();
				String cunhuocode = jsonMap.get("invcode").toString();
				String pk_corp = getPk_corp(corp);
				String pk_invmandoc = getPk_invmandoc(cunhuocode, pk_corp);
				String def1id = "";
				String def13id = "";
				String def14id = "";
				String def15id = "";
				String def16id = "";
				try {
					def1id  = getDefIds(def1, "BY22");//��Ʒ����
					def13id = getDefIds(def13, "BZ007");//��Ʒϵͳ
					def14id = getDefIds(def14, "BZ008");//��Ʒϵ��
					def15id = getDefIds(def15, "BZ009");//��Ʒ����
					def16id = getDefIds(def16, "BZ010");//��������
				} catch (BusinessException e2) {
					e2.printStackTrace();
					return  "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e2.getMessage()+"\"}]";
				}
				
				String custcode = jsonMap.get("custcode")==null?"":jsonMap.get("custcode").toString();//Ĭ�Ͽͻ�
				StringBuffer cust = new StringBuffer();
				cust.append(" select a.pk_cubasdoc from  bd_cubasdoc a ") 
				.append("  left join bd_cumandoc b on a.pk_cubasdoc = b.pk_cubasdoc ") 
				.append("  where (nvl(a.dr,0)=0 and nvl(b.dr,0)=0) ") 
				.append("  and (b.custflag='0' or b.custflag='1' or b.custflag='2') ") 
				.append("  and (a.pk_corp='0001' and b.pk_corp='"+pk_corp+"') ")
				.append("  and a.custcode='"+custcode+"' ");
	            Object pk_cubasdoc = null;
	            try {
	            	//�޸�Ĭ�Ͽͻ�
	            	pk_cubasdoc = uap.executeQuery(cust.toString(), new ColumnProcessor());
	            	String khsql  ="update bd_invmandoc set def17 ='"+pk_cubasdoc+"' where pk_invmandoc ='"+pk_invmandoc+"' and pk_corp ='"+pk_corp+"'";
					new BaseDAO().executeUpdate(khsql);
				} catch (BusinessException e) {
					e.printStackTrace();
					return  "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
				}catch (Exception e) {
					e.printStackTrace();
					return  "[{\"status\":\"error\",\"message\":\"���ݿ�����쳣�����Ժ�����:"+e.getMessage()+"\"}]";
				}
				//����Ϻ�
				if(sealflag.equals("Y")){
					String	 sql ="update bd_invmandoc set sealflag ='"+sealflag+"' where pk_invmandoc ='"+pk_invmandoc+"' and pk_corp ='"+pk_corp+"'";
					try {
						new BaseDAO().executeUpdate(sql);
					} catch (DAOException e) {
						e.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"�޸��쳣:"+e.getMessage()+"\"}]"; 
					}
			    //���
				}else if(sealflag.equals("N")){
					String  sql1 ="update bd_invmandoc set sealflag ='"+sealflag+"' where pk_invmandoc ='"+pk_invmandoc+"' and pk_corp ='"+pk_corp+"'";
					try {
						new BaseDAO().executeUpdate(sql1);
					} catch (DAOException e) {
						e.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"�޸��쳣:"+e.getMessage()+"\"}]"; 
					}
 				} 
				//�޸ļ����Զ�����
				if(sealflag.equals("N")){
					String sql2 ="update bd_invbasdoc set def1='"+def1id+"',def13='"+def13id+"',def14='"+def14id+"',def15='"+def15id+"',def16='"+def16id+"' where invcode ='"+cunhuocode+"';";
					try {
						new BaseDAO().executeUpdate(sql2);
					} catch (DAOException e) {
						e.printStackTrace();
						return "[{\"status\":\"error\",\"message\":\"�޸��쳣:"+e.getMessage()+"\"}]"; 
					}
 				}
			}else{
				return "[{\"status\":\"error\",\"message\":\"������ʶflag��ֵ����\"}]"; 
			}
			return "[{\"status\":\"success\",\"massage\":\"�޸ĳɹ�\",\"bodylist\":[{\"invcode\":\""+jsonMap.get("invcode").toString()+"\"}]}]";		
		}
		return null;
	}
	
	/** 
     * ��ȡ����ʱ�� 
     * @return�����ַ�����ʽyyMMdd
     */  
      public static String getStringDate() {  
             Date currentTime = new Date();  
             SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");  
             String dateString = formatter.format(currentTime);  
             return dateString;  
          }  
      /** 
       * ��������ʱ����+4λ����� 
       * ������ˮ�� 
       * @return 
       */  
      public static String Getnum(){  
          String t = getStringDate();  
          int x=(int)(Math.random()*9000)+1000;  
          String serial = t + x;  
          return serial;  
      } 
  //���������ݸ÷����µ����������+1 by src 2018��4��24��13:11:39
    public String getInvcode(String pk_invcl,String invclasscode) throws BusinessException{
    	String invcode = "";
    	String sql = "select  max(invcode)+1 as invcode  from bd_invbasdoc where pk_invcl='"+pk_invcl+"' and substr(invcode,0,8)='"+invclasscode+"' and nvl(dr,0)=0";
    	List list = (List) uap.executeQuery(sql, new MapListProcessor());
    	if(list.size()>0 ){
    		Map map = (Map) list.get(0);
    		if(map.get("invcode")!=null){
    			invcode = map.get("invcode").toString();
    		}else{
        		invcode = invclasscode+"000001";
        	}
    	}else{
    		invcode = invclasscode+"000001";
    	}
    	return invcode;
    }
    //�ж��Ǵ�������Ƿ�ĩ��
   	public String isendflog(String pk_invcl,String corp) throws BusinessException {
  		// TODO Auto-generated method stub
   		String endflog = "error";
   		if(pk_invcl != null){
   			String esql = "select invclasscode from bd_invcl where nvl(dr,0)=0 and pk_invcl = '"+pk_invcl+"' and (pk_corp='"+corp+"' or pk_corp='0001')";
   			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
  			MapListProcessor alp = new MapListProcessor();
  			ArrayList invclasscode = null;
  			invclasscode = (ArrayList) query.executeQuery(esql.toString(), alp);
  			String value = null;
  			if (invclasscode != null && invclasscode.size() > 0) {
  				for (Object map : invclasscode) {
  					Map row = (Map) map;
  					Iterator<String> keys = row.keySet().iterator();
  					while(keys.hasNext()){
  						String key = keys.next(); 
  						value = row.get(key).toString();
  					}
  				}
  				String issql = "select invclasscode from bd_invcl where invclasscode like '"+value+"%' and invclasscode != '"+value+"' and (pk_corp='"+corp+"' or pk_corp='0001')";
  				IUAPQueryBS query1 = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
  				MapListProcessor alp1 = new MapListProcessor();
  				ArrayList  isend= null;
  				isend = (ArrayList) query.executeQuery(issql.toString(), alp1);
  				if(isend !=null && isend.size()>0){
  					endflog = "error";
  				}else{
  					endflog = "success";
  				}
  			}
   		}
   		return endflog;
  	}
   	/**
   	 *�����Ƶ��˱����ȡ�Ƶ�������
   	 *by src 2017��12��12��10:58:50
   	 * @throws BusinessException 
   	 */
   	public String getUserId(String usercode) throws BusinessException{
   		String operateid = "";
   		String sql = "select cuserid from sm_user where user_code = '"+usercode+"' and nvl(dr,0)=0";
		operateid = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return operateid;
   	}
   	/**
   	 * ������Ŀ�����ȡ��Ŀ����������
   	 * by src 2018��5��10��20:07:29
   	 * @param jobcode
   	 * @param pk_corp
   	 * @return
   	 * @throws BusinessException
   	 */
   	public String getJobManId(String jobcode,String pk_corp) throws BusinessException{
   		String pk_jobmanid = "";
   		StringBuffer sb = new StringBuffer();
   		sb.append(" select b.pk_jobmngfil from bd_jobbasfil a  ") 
   		.append(" left join bd_jobmngfil b on a.pk_jobbasfil = b.pk_jobbasfil ") 
   		.append(" where nvl(a.dr,0)=0 and nvl(b.dr,0)=0 ") 
   		.append(" and b.pk_corp='"+pk_corp+"' and a.jobcode='"+jobcode+"' ") ;
   		pk_jobmanid = (String) uap.executeQuery(sb.toString(), new ColumnProcessor());
   		return pk_jobmanid;
   	}
   	/**
   	 * ���ݴ����������ȡ�����������
   	 * by src 2017��12��12��11:06:41
   	 * @throws BusinessException 
   	 */
   	public String getInvclassId(String invclasscode,String corp) throws BusinessException{
   		String invclassid = "";
   		String sql = "select pk_invcl from bd_invcl where invclasscode='"+invclasscode+"' and (pk_corp = '"+corp+"' or pk_corp='0001') and nvl(dr,0)=0";
		invclassid = (String) uap.executeQuery(sql, new ColumnProcessor());				
   		return invclassid;
   	}
   	
   	/**
   	 * ���ݼ�����λ�����ȡ������λ����
   	 * by src 2017��12��12��11:06:41
   	 * @throws BusinessException 
   	 */
   	public String getMeasDocId(String shortname) throws BusinessException{
   		String pk_measdoc = "";
   		String sql = "select pk_measdoc from bd_measdoc where measname='"+shortname+"' and nvl(dr,0)=0 ";
		pk_measdoc = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return pk_measdoc;
   	}
   	/**
   	 * ���ݹ�˾������ȡ�����֯����
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
   	 * ���ݼ���������1�����ȡ����
   	 * by src 2018��1��16��13:00:01
   	 */
   	public String getDefId(String defcode) throws BusinessException{
   		String defid = "";
   		String sql = "select a.pk_defdef from bd_defdef a where a.defcode='"+defcode+"' and  nvl(dr,0)=0 ";
   		defid = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return defid;
   	}
   	/**
   	 * ���ݱ����ȡ�Զ���������
   	 * by src 2018��4��9��12:24:28
   	 */
   	public String getDefIds(String defcode,String code) throws BusinessException{
   		String defid = "";
   		String sql = "select distinct pk_defdoc from bd_defdoc left outer join bd_defdef on bd_defdoc.pk_defdoclist=bd_defdef.pk_defdoclist   where 11=11   and bd_defdoc.pk_defdoclist = (select pk_defdoclist from bd_defdoclist where doclistcode = '"+code+"' ) and (sealflag is null or sealflag<>'Y') and doccode='"+defcode+"'  order by doccode";
   		defid = (String) uap.executeQuery(sql, new ColumnProcessor());
   		return defid;
   	}
   	/**
   	 * �������ϱ������˾��ȡ���ϻ�����������������������
   	 * @throws BusinessException 
   	 */
   	public Map getInvpk(String invcode,String pk_corp) throws BusinessException{
   		Map map = new HashMap();
   		StringBuffer sb  = new StringBuffer();
   		sb.append(" select a.pk_invbasdoc,b.pk_invmandoc from bd_invbasdoc a ") 
   		.append(" left join bd_invmandoc b on a.pk_invbasdoc = b.pk_invbasdoc ") 
   		.append(" where a.invcode = '"+invcode+"' and b.pk_corp='"+pk_corp+"' ") ;
        List list = (List) uap.executeQuery(sb.toString(), new MapListProcessor());
        if(list.size()>0){
        	map = (Map) list.get(0);
        	return map;
        }else{
        	map = null;
        	return map;
        }
   	}
   	/**
   	 * ��ȡ��˾����
   	 * @param corp
   	 * @return
   	 * @throws BusinessException
   	 */
   	String getPk_corp (String corp) {
   		String pk_corp =null;
   		String sql ="select pk_corp from bd_corp where unitcode ='"+corp+"'";
   		try {
			pk_corp = (String) uap.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return pk_corp;
   		
   	}
   	String getPk_invmandoc (String invcode, String pk_corp){
   		String Pk_invmandoc =null;
   		StringBuffer sb = new StringBuffer();
   		sb.append(" select b.pk_invmandoc from bd_invbasdoc a ") 
   		.append(" left join bd_invmandoc b on a.pk_invbasdoc = b.pk_invbasdoc ") 
   		.append(" where a.invcode = '"+invcode+"' and b.pk_corp='"+pk_corp+"' ") ;
   		try {
   			Pk_invmandoc = (String) uap.executeQuery(sb.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
   		return Pk_invmandoc;
   	}
   	/**
   	 * ������������VO
   	 */
   	public ProduceVO getProduceVO(String pk_corp,String kczz,String pk_invmandoc,String pk_invbasdoc,String matertype){
   		ProduceVO pvo = new ProduceVO();
   		pvo.setAbcfl("A");
   		pvo.setAbcFundeg("C");
   		pvo.setAbcGrosspft("C");
   		pvo.setAbcPurchase("C");
   		pvo.setAbcsales("C");
   		pvo.setBatchrule("ZJ");
   		pvo.setBomtype(0);
   		pvo.setChkFreeFlag(new UFBoolean(true));
   		pvo.setCksj(new UFDouble("0.00000000"));
   		pvo.setCombineflag(new UFBoolean(false));
   		pvo.setConverseflag(new UFBoolean(false));
   		pvo.setDr(0);
   		pvo.setFcpclgsfa(0);
   		pvo.setIscostbyorder(new UFBoolean(false));
   		pvo.setIscreatesonprodorder(new UFBoolean(false));
   		pvo.setIsctlbyfixperiod(new UFBoolean(false));
   		pvo.setIsctlbyprimarycode(new UFBoolean(false));
   		pvo.setIsctlbysecondarycode(new UFBoolean(false));
   		pvo.setIsctoutput(new UFBoolean(true));
   		pvo.setIselementcheck(new UFBoolean(false));
   		pvo.setIsfxjz(new UFBoolean(false));
   		pvo.setIssend(new UFBoolean(true));
   		pvo.setIssendbydatum(new UFBoolean(false));
   		pvo.setIsused(new UFBoolean(true));
   		pvo.setIsuseroute(new UFBoolean(false));
   		pvo.setIswholesetsend(new UFBoolean(true));
   		pvo.setJyrhzdyw(0);
   		pvo.setLowlevelcode(0);
   		pvo.setMaterclass(0);
   		pvo.setMaterstate(2);
   		if(matertype.equals("0")){
   			pvo.setMatertype("PR");//pr����� mr �ɹ���
   		}else{
   			pvo.setMatertype("MR");//mr����� mr �ɹ���
   		}
   		pvo.setMinmulnum(new UFDouble("1.00000000"));
   		pvo.setOuttype("OA");
   		pvo.setPchscscd(0);
   		pvo.setPk_calbody(kczz);
   		pvo.setPk_corp(pk_corp);
   		pvo.setPk_invbasdoc(pk_invbasdoc);
   		pvo.setPk_invmandoc(pk_invmandoc);
   		pvo.setPricemethod(4);
   		pvo.setPrimaryflag(new UFBoolean(false));
   		pvo.setProducemethod(0);
   		pvo.setRationwtnum(new UFDouble("0.50000000"));
   		pvo.setRoadType(0);
   		pvo.setRoundingnum(new UFDouble("0.50000000"));
   		pvo.setScscddms(0);
   		pvo.setScxybzsfzk(new UFBoolean(false));
   		pvo.setSealflag(new UFBoolean(false));
   		pvo.setSfbj(new UFBoolean(false));
   		pvo.setSfcbdx(new UFBoolean(false));
   		pvo.setSffzfw(new UFBoolean(false));
   		pvo.setSfpchs(new UFBoolean(false));
   		pvo.setSfscx(new UFBoolean(false));
   		pvo.setSfzb(new UFBoolean(false));
   		pvo.setSfzzcp(new UFBoolean(false));
   		pvo.setStockByCheck(new UFBoolean(false));
   		pvo.setSupplytype("0");
   		pvo.setUsableAmount("1111111111111011");
   		pvo.setUsableamountbyfree("NNN");
   		pvo.setVirtualflag(new UFBoolean(false));
   		pvo.setWghxcl(0);
   		return pvo;
   	}
}
