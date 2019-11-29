package nc.bs.baoyin.alert;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.rpc.ServiceException;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.baoyin.alert.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pa.Key;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang.StringUtils;

/**
 * 每日产量短信预警.xiaolong_fan.2013-1-4.
 * 
 * @author fans
 * 
 */
public class ProductAlert implements IBusinessPlugin {

	private BaseDAO jdbc = new BaseDAO();
//	private String fmtMsg = "%s%s-%s日转版%s次,日产量%s万罐,月产量%s万罐,年产量%s万罐,总隔离品%s万罐,日销量%s万罐,月销量%s万罐,年销量%s万罐";
	//edit by zwx 2015-10-21 注释转版次，隔离数
	
	private int scale = 1;

	@SuppressWarnings("rawtypes")
	public String implementReturnMessage(Key[] arg0, String arg1, UFDate arg2) throws BusinessException {
		StringBuilder smsContent = new StringBuilder();
		String pk_corp = "1016";
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date timedate = new Date(System.currentTimeMillis());
//		String sfm = dateFormat.format(timedate);
//		String fmtMsg = "%s%s-%s日"+sfm+"时产量%s万罐,月产量%s万罐,年产量%s万罐,日销量%s万罐,月销量%s万罐,年销量%s万罐";
		String fmtMsg = "%s%s-%s日产量%s万罐,月产量%s万罐,年产量%s万罐,日销量%s万罐,月销量%s万罐,年销量%s万罐";
		
		try {
			UFDate date = new UFDate(System.currentTimeMillis());
			UFDate prevDate = date;
//			UFDate prevDate = date.getDateBefore(1);
			Object[] mmData = getMMData("230", date, pk_corp);
			/*smsContent.append(String.format(fmtMsg, new Object[] {
					"钢线", prevDate.getMonth(), prevDate.getDay(), mmData[0], mmData[1], mmData[2], mmData[3], mmData[4], mmData[5], mmData[6], mmData[7]
			}));*/
			//edit by zwx 
			smsContent.append(String.format(fmtMsg, new Object[] {
					"钢线：截止到", prevDate.getMonth(), prevDate.getDay(), mmData[1], mmData[2], mmData[3], mmData[5], mmData[6], mmData[7]
			}));
			//end by zwx
			smsContent.append(";");
			mmData = getMMData("231", date, pk_corp);
			/*smsContent.append(String.format(fmtMsg, new Object[] {
					"铝线", prevDate.getMonth(), prevDate.getDay(), mmData[0], mmData[1], mmData[2], mmData[3], mmData[4], mmData[5], mmData[6], mmData[7]
			}));*/
			//edit by zwx 2015-10-21 
			smsContent.append(String.format(fmtMsg, new Object[] {
					"铝线：截止到", prevDate.getMonth(), prevDate.getDay(), mmData[1], mmData[2], mmData[3], mmData[5], mmData[6], mmData[7]
			}));
			//end by zwx 
			// 发送给哪个手机,手机号必须是在宝钢的短信中心注册过的才可以,后面可能需要修改.
			String telSql = "select a.* from bd_defdoc a, bd_defdoclist b WHERE a.pk_defdoclist = b.pk_defdoclist and doclistcode = 'BZ018' and nvl(sealflag,'N')='N'";
			Object obj = jdbc.executeQuery(telSql, new BeanListProcessor(DefdocVO.class));
			String usernameSql = "select docname from bd_defdoc a, bd_defdoclist b WHERE a.pk_defdoclist = b.pk_defdoclist and doclistcode = 'BZ019' and nvl(sealflag,'N')='N' and doccode ='USERNAME' ";
			Object usernameObj = jdbc.executeQuery(usernameSql, new ColumnProcessor());
			String passwordSql = "select docname from bd_defdoc a, bd_defdoclist b WHERE a.pk_defdoclist = b.pk_defdoclist and doclistcode = 'BZ019' and nvl(sealflag,'N')='N' and doccode ='PASSWORD' ";
			Object passwordObj = jdbc.executeQuery(passwordSql, new ColumnProcessor());
			if (obj != null && obj instanceof ArrayList && usernameObj != null && passwordObj != null) {
				ArrayList list = (ArrayList) obj;
				DefdocVO defdocVO = null;
				String[] mobiles = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					defdocVO = (DefdocVO) list.get(i);
					mobiles[i] = defdocVO.getDoccode();
				}
				sendSMS(String.valueOf(usernameObj), String.valueOf(passwordObj), mobiles, smsContent.toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			smsContent = new StringBuilder("异常:" + ex.getMessage());
		}
		return smsContent.toString();
	}

	private String sendSMS(String username, String password, String[] tels, String body) {
		String BsmsAuthentication = "http://10.251.8.127/bsms/services/BsmsAuthentication?wsdl";
		String BsmsSendProxy = "http://10.251.8.127/bsms/services/BsmsSendProxy?wsdl";
		Service ser = new Service();
		try {
			Call call = (Call) ser.createCall();
			call.setTargetEndpointAddress(BsmsAuthentication);
			call.setOperation("login");
			String result = (String) call.invoke(new Object[] {
					new String(username), new String(password)
			});
			call.setTargetEndpointAddress(BsmsSendProxy);
			call.setOperation("sendMessage");
			if (tels == null || tels.length <= 0) {
				return "手机号为空";
			} else {
				call.invoke(new Object[] {
						tels, body, new Integer(0), new Integer(0), result
				});
				return "发送完成";
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (RemoteException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public Object[] getMMData(String invclasscode, UFDate date, String pk_corp) throws Exception {
		Object[] ret = new Object[8];
		//		UFDate prevDate = date.getDateBefore(1);
		//		UFDate pprevDate = date.getDateBefore(2);
		//		String pprevTime1 = pprevDate.toString() + " 08:30:00";
		//		String pprevTime2 = prevDate.toString() + " 08:30:00";
		//		String prevTime1 = prevDate.toString() + " 08:30:00";
		//		String prevTime2 = date.toString() + " 08:30:00";
		//		String monthTime1 = prevDate.getYear() + "-" + prevDate.getStrMonth() + "-01 08:30:00";
		//		String monthTime2 = new UFDate(prevDate.getYear() + "-" + prevDate.getStrMonth() + "-" + prevDate.getDaysMonth()).getDateAfter(1).toString() + " 08:30:00";
		//		String yearTime1 = prevDate.getYear() + "-01-01 08:30:00";
		//		String yearTime2 = (prevDate.getYear() + 1) + "-01-01 08:30:00";
		int datemonth = date.getMonth();
		String date6 = date.getYear()+"-06-30";    //当年的6月30日
		String date12 = (date.getYear()-1)+"-12-31";   //前一年的12月31日
		UFDate dateelse =  new UFDate(date.getYear() + "-" + date.getStrMonth() + "-01").getDateBefore(1);
		String dateother = dateelse.getYear()+"-"+ dateelse.getStrMonth() +"-29";
		String nowyear = date.getYear()+"-01-01";
		UFDate sydate = new UFDate(date.getYear()+"-"+date.getStrMonth()+"-01");
		String premonth = sydate.getDateBefore(1).toString();
		UFDate prevDate = date.getDateBefore(1);
		UFDate pprevDate = date.getDateBefore(2);
		String pprevTime1 = pprevDate.toString() + " 08:30:00";
		String pprevTime2 = prevDate.toString() + " 08:30:00";
		/*
		String prevTime1 = prevDate.toString() + " 08:30:00";
		String prevTime2 = date.toString() + " 08:30:00";*/
		String prevTime1 = prevDate.toString() ; //当前日期前一天 edit by zwx
		String prevTime2 = date.toString(); //当前日期
		//add by zwx 2015-9-17 获得当前日期上月最后一天
		String beforeMonth = prevDate.getYear() + "-" + prevDate.getStrMonth() + "-01";
		UFDate beforeMonthTime = new UFDate(beforeMonth).getDateBefore(1);
		String beforeMonthsj = beforeMonthTime.toString();
		//获得上一年最后一天
		String yearTime = prevDate.getYear() + "-01-01";
		UFDate yearTimeNew = new UFDate(yearTime).getDateBefore(1);
		String beforeYear = yearTimeNew.toString();
		//当月1号
		String monthDay = prevDate.getYear() + "-" + prevDate.getStrMonth() + "-01";
		//当年1号
		String yearDay  = prevDate.getYear() + "-01-01";
		//end by zwx
		
		String monthTime1 = prevDate.getYear() + "-" + prevDate.getStrMonth() + "-01 08:30:00";
		Calendar cc = Calendar.getInstance();
		cc.setTime(prevDate.toDate());
		cc.add(Calendar.MONTH, 1);
		UFDate monthDate = new UFDate(cc.getTime());
		String monthTime2 = new UFDateTime(monthDate.getYear() + "-" + monthDate.getStrMonth() + "-01 08:30:00").toString();
		String yearTime1 = prevDate.getYear() + "-01-01 08:30:00";
		String yearTime2 = (prevDate.getYear() + 1) + "-01-01 08:30:00";
		StringBuilder sql = new StringBuilder(); 
		
		// 转版次数
		sql.append("select count(distinct A.lh) as rst from mm_glzb_b A");
		sql.append(" inner join mm_glzb B on A.pk_glzb=B.pk_glzb");
		sql.append(" where B.djzt=1 and B.xxrq ||' '|| B.xxsj>='" + prevTime1 + "' and B.xxrq ||' '|| B.xxsj<='" + prevTime2 + "'");
		sql.append(" and B.pk_corp='" + pk_corp + "' and nvl(A.dr,0)=0 and nvl(B.dr,0)=0");
		sql.append(" and A.lh like '" + invclasscode + "%'");
		Integer prevInvCount = (Integer) jdbc.executeQuery(sql.toString(), new ColumnProcessor());
		if (prevInvCount == null) ret[0] = 0;
		else {
			sql = new StringBuilder();
			sql.append(" select lh from (");
			sql.append(" select A.lh from mm_glzb_b A");
			sql.append(" inner join mm_glzb B on A.pk_glzb=B.pk_glzb");
			sql.append(" where B.djzt=1 and B.xxrq ||' '|| B.xxsj>='" + pprevTime1 + "' and B.xxrq ||' '|| B.xxsj<='" + pprevTime2 + "'");
			sql.append(" and B.pk_corp='" + pk_corp + "' and nvl(A.dr,0)=0 and nvl(B.dr,0)=0");
			sql.append(" and A.lh like '" + invclasscode + "%'");
			sql.append(" order by B.ts desc");
			sql.append(" ) T where rownum=1");
			String pprevInv = (String) jdbc.executeQuery(sql.toString(), new ColumnProcessor());
			if (StringUtils.isEmpty(pprevInv)) ret[0] = prevInvCount;
			else {
				sql = new StringBuilder();
				sql.append(" select lh from (");
				sql.append(" select A.lh from mm_glzb_b A");
				sql.append(" inner join mm_glzb B on A.pk_glzb=B.pk_glzb");
				sql.append(" where B.djzt=1 and B.xxrq ||' '|| B.xxsj>='" + prevTime1 + "' and B.xxrq ||' '|| B.xxsj<='" + prevTime2 + "'");
				sql.append(" and B.pk_corp='" + pk_corp + "' and nvl(A.dr,0)=0 and nvl(B.dr,0)=0");
				sql.append(" and A.lh like '" + invclasscode + "%'");
				sql.append(" order by B.ts");
				sql.append(" ) T where rownum=1");
				String prevInv = (String) jdbc.executeQuery(sql.toString(), new ColumnProcessor());
				if (StringUtils.isEmpty(prevInv)) ret[0] = 0;
				else {
					if (pprevInv.equals(prevInv)) ret[0] = prevInvCount - 1;
					else ret[0] = prevInvCount;
				}
			}
		}

		Object obj; 
		// 日产量  成品下线日报表（按料号查询，品控）
		sql = new StringBuilder();
		/*sql.append("select sum(sl) as rst from (");
		sql.append(" select sum(A.xxaglsl) as sl from mm_glzb_b A,mm_glzb B");
		sql.append(" where B.djzt=1 and B.pk_corp='" + pk_corp + "'");
		sql.append(" and B.xxrq ||' '|| B.xxsj>='" + prevTime1 + "' and B.xxrq ||' '|| B.xxsj<='" + prevTime2 + "'");
		sql.append(" and A.lh like '" + invclasscode + "%'");
		sql.append(" and nvl(B.jyjg,0)=0 and nvl(A.dr,0)=0 and nvl(B.dr,0)=0");
		sql.append(" and A.pk_glzb=B.pk_glzb");
		sql.append(" union all");
		sql.append(" select sum(A.hgsl) as sl from mm_glcl_b A,mm_glcl B,mm_glzb_b C,mm_glzb D");
		sql.append(" where D.djzt=1 and A.lydjlx='JYDJ'");
		sql.append(" and B.pk_corp='" + pk_corp + "'");
		sql.append(" and D.xxrq ||' '|| D.xxsj>='" + prevTime1 + "' and D.xxrq ||' '|| D.xxsj<='" + prevTime2 + "'");
		sql.append(" and C.lh like '" + invclasscode + "%'");
		sql.append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0 and nvl(C.dr,0)=0");
		sql.append(" and A.pk_glcl=B.pk_glcl");
		sql.append(" and A.pk_glzb_b=C.pk_glzb_b");
		sql.append(" and C.pk_glzb=D.pk_glzb");
		sql.append(" ) T");*/
		
		//edit by zwx 2015-9-17 昨天到当天的产量

/*		sql.append(" select sum(sl) from (select innum as sl") 
		.append("   from (select d.invcode, ") 
		.append("                d.invname, ") 
		.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
		.append("                nvl(i.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
		.append("           from ic_general_bb1 a ") 
		.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
		.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
		.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
		.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
		.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
		.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
		.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
		.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
		.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
		.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and a.dr = '0' ") 
		.append("            and (i.xxrq || i.xxsj >= '" + prevTime1 + "' || '08:30:00' or '" + prevTime1 + "' is null) ") 
		.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ") 
		.append("            and (d.invcode like '" + invclasscode + "%') ") 
		.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
		.append("          order by d.invcode) ") 
		.append(" union all ") 
		.append(" select rksl as sl ") 
		.append("   from (select bas.invcode, ") 
		.append("                bas.invname, ") 
		.append("                -sum(nvl(icb.noutnum, 0)) as rksl, ") 
		.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(-icb.noutnum / glcl.zdsl, 2), 0)) as rkds ") 
		.append("           from mm_glcl_b glclb ") 
		.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
		.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
		.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
		.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
		.append("                                         icb.vsourcebillcode ") 
		.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and nvl(icb.dr, 0) = 0 ") 
		.append("            and lydjlx = 'isolation' ") 
		.append("            and (glcl.clrq || glcl.xxsj >= '" + prevTime1 + "' || '08:30:00' or ") 
		.append("                '" + prevTime1 + "' is null) ") 
		.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
		.append("                '" + prevTime2 + "' is null) ") 
		.append("            and (bas.invcode like '" + invclasscode + "%') ") 
		.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 
		.append("          order by bas.invcode)) ") ;*/
		
		 
		/*//edit by yqq 2016-10-19  日产量  成品下线日报表（按料号查询，品控）不取负数 
		
		sql.append(" select sum(sl) from (select innum as sl") 
		.append("   from (select d.invcode, ") 
		.append("                d.invname, ") 
		.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
		.append("                nvl(i.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
		.append("           from ic_general_bb1 a ") 
		.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
		.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
		.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
		.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
		.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
		.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
		.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
		.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
		.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
		.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and a.dr = '0' ") 	
		.append("            and (i.xxrq || i.xxsj >= '" + prevTime1 + "' || '08:30:00' or '" + prevTime1 + "' is null) ") 
		.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ") 
		.append("            and (d.invcode like '" + invclasscode + "%') ") 
		.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
        .append("          having sum(nvl(a.ninspacenum, 0))>=0 ") 
		.append("          order by d.invcode) ") 
		.append(" union all ") 
		.append(" select rksl as sl ") 
		.append("   from (select bas.invcode, ") 
		.append("                bas.invname, ") 
		.append("                -sum(nvl(icb.noutnum, 0)) as rksl, ") 
		.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(-icb.noutnum / glcl.zdsl, 2), 0)) as rkds ") 
		.append("           from mm_glcl_b glclb ") 
		.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
		.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
		.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
		.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
		.append("                                         icb.vsourcebillcode ") 
		.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and nvl(icb.dr, 0) = 0 ") 
		.append("            and lydjlx = 'isolation' ") 
		.append("            and (glcl.clrq || glcl.xxsj >= '" + prevTime1 + "' || '08:30:00' or ") 
		.append("                '" + prevTime1 + "' is null) ") 
		.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
		.append("                '" + prevTime2 + "' is null) ") 
		.append("            and (bas.invcode like '" + invclasscode + "%') ") 
		.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 
		.append("          having -sum(nvl(icb.noutnum, 0)) >=0 ") 		
		.append("          order by bas.invcode)) ") ;
		//end by yqq 2016-10-19
*/	
		
		
		
                //edit by yqq 2016-12-08  日产量  成品下线日报表（按料号查询，品控），取出入库表中单据类型为“产成品入库”及入库数量
		
		sql.append(" select sum(sl) from (select innum as sl") 
		.append("   from (select d.invcode, ") 
		.append("                d.invname, ") 
		.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
		.append("                nvl(i.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
		.append("           from ic_general_bb1 a ") 
		.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
		.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
		.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
		.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
		.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
		.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
		.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
		.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
		.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
		.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and a.dr = '0' ") 	
		.append("            and (i.xxrq || i.xxsj >= '" + prevTime1 + "' || '08:30:00' or '" + prevTime1 + "' is null) ") 
		.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ")  
		.append("            and (d.invcode like '" + invclasscode + "%') ") 
		.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
		.append("          order by d.invcode) ") 
		.append(" union all ") 
		.append(" select rksl as sl ") 
		.append("   from (select bas.invcode, ") 
		.append("                bas.invname, ") 
		.append("                sum(nvl(icb.ninnum, 0)) as rksl, ") 
		.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(icb.ninnum / glcl.zdsl, 2), 0)) as rkds ") 
		.append("           from mm_glcl_b glclb ") 
		.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
		.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
		.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
		.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
		.append("                                         icb.vsourcebillcode ") 
		.append("           left join ic_general_h h on h.cgeneralhid = icb.cgeneralhid ")
		.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and nvl(icb.dr, 0) = 0 ") 
		.append("            and lydjlx = 'isolation' ") 
		.append("            and h.cbilltypecode = '46' ") 
		.append("            and (glcl.clrq || glcl.xxsj >= '" + prevTime1 + "' || '08:30:00' or ") 
		.append("                '" + prevTime1 + "' is null) ") 
		.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
		.append("                '" + prevTime2 + "' is null) ")  
		.append("            and (bas.invcode like '" + invclasscode + "%') ") 
		.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 		
		.append("          order by bas.invcode)) ") ;
		//end by yqq 2016-12-08
		
		
		
		
		
		
		
		
		
		obj = jdbc.executeQuery(sql.toString(), new ColumnProcessor());
		ret[1] = obj == null ? 0 : new UFDouble(obj.toString()).div(10000).setScale(scale, UFDouble.ROUND_HALF_UP);
		// 月产量
		sql = new StringBuilder();
		/*sql.append("select sum(sl) as rst from (");
		sql.append(" select sum(A.xxaglsl) as sl from mm_glzb_b A,mm_glzb B");
		sql.append(" where B.djzt=1 and B.pk_corp='" + pk_corp + "'");
		sql.append(" and B.xxrq ||' '|| B.xxsj>='" + monthTime1 + "' and B.xxrq ||' '|| B.xxsj<='" + monthTime2 + "'");
		sql.append(" and A.lh like '" + invclasscode + "%'");
		sql.append(" and nvl(B.jyjg,0)=0 and nvl(A.dr,0)=0 and nvl(B.dr,0)=0");
		sql.append(" and A.pk_glzb=B.pk_glzb");
		sql.append(" union all");
		sql.append(" select sum(A.hgsl) as sl from mm_glcl_b A,mm_glcl B,mm_glzb_b C,mm_glzb D");
		sql.append(" where D.djzt=1 and A.lydjlx='JYDJ'");
		sql.append(" and B.pk_corp='" + pk_corp + "'");
		sql.append(" and D.xxrq ||' '|| D.xxsj>='" + monthTime1 + "' and D.xxrq ||' '|| D.xxsj<='" + monthTime2 + "'");
		sql.append(" and C.lh like '" + invclasscode + "%'");
		sql.append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0 and nvl(C.dr,0)=0");
		sql.append(" and A.pk_glcl=B.pk_glcl");
		sql.append(" and A.pk_glzb_b=C.pk_glzb_b");
		sql.append(" and C.pk_glzb=D.pk_glzb");
		sql.append(" ) T");*/
		
		//edit by zwx 2015-9-17 上月最后一天到当天的产量
		/*sql.append(" select sum(sl) from (select innum as sl") 
		.append("   from (select d.invcode, ") 
		.append("                d.invname, ") 
		.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
		.append("                nvl(i.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
		.append("           from ic_general_bb1 a ") 
		.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
		.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
		.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
		.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
		.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
		.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
		.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
		.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
		.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
		.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and a.dr = '0' ") 
//		.append("            and (i.xxrq || i.xxsj >= '" + beforeMonthsj + "' || '08:30:00' or '" + beforeMonthsj + "' is null) ") 
		
		//eidt by zwx 2016-5-9 
		.append("            and (i.xxrq || i.xxsj >= '2016-04-29' || '08:30:00' or '2016-04-29' is null) ") 
		
		.append("            and (i.xxrq || i.xxsj <= '" + timedate + "' or '" + timedate + "' is null) ") 
		.append("            and (d.invcode like '" + invclasscode + "%') ") 
		.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
		.append("          order by d.invcode) ") 
		.append(" union all ") 
		.append(" select rksl as sl ") 
		.append("   from (select bas.invcode, ") 
		.append("                bas.invname, ") 
		.append("                -sum(nvl(icb.noutnum, 0)) as rksl, ") 
		.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(-icb.noutnum / glcl.zdsl, 2), 0)) as rkds ") 
		.append("           from mm_glcl_b glclb ") 
		.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
		.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
		.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
		.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
		.append("                                         icb.vsourcebillcode ") 
		.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and nvl(icb.dr, 0) = 0 ") 
		.append("            and lydjlx = 'isolation' ") 
		.append("            and (glcl.clrq || glcl.xxsj >= '2016-04-29' || '08:30:00' or ") 
		.append("                '2016-04-29' is null) ") 
		.append("            and (glcl.clrq || glcl.xxsj <= '" + timedate + "' or ") 
		.append("                '" + timedate + "' is null) ") 
		.append("            and (bas.invcode like '" + invclasscode + "%') ") 
		.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 
		.append("          order by bas.invcode)) ") ;*/
		//7月的月产量从6月30日开始取数，1月产量从12月31日取数，其余月份从上月29日开始取数

/*		sql.append(" select sum(sl) from (select innum sl ") 
		.append("   from (select d.invcode, ") 
		.append("                d.invname, ") 
		.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
		.append("                nvl(i.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
		.append("           from ic_general_bb1 a ") 
		.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
		.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
		.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
		.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
		.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
		.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
		.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
		.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
		.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
		.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and a.dr = '0' ") ;
		if(datemonth==7){
			sql.append("            and (i.xxrq || i.xxsj >= '"+date6+"' || '08:30:00' or '"+date6+"' is null) ") ; 
		}else if(datemonth==1){
			sql.append("            and (i.xxrq || i.xxsj >= '"+date12+"' || '08:30:00' or '"+date12+"' is null) ") ;
		}else{
			sql.append("            and (i.xxrq || i.xxsj >= '"+dateother+"' || '08:30:00' or '"+dateother+"' is null) ") ;
		}
		sql.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ")
		.append("            and (d.invcode like '" + invclasscode + "%') ") 
		.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
		.append("          order by d.invcode) ") 
		.append(" union all ") 
		.append(" select rksl sl ") 
		.append("   from (select bas.invcode, ") 
		.append("                bas.invname, ") 
		.append("                -sum(nvl(icb.noutnum, 0)) as rksl, ") 
		.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(-icb.noutnum / glcl.zdsl, 2), 0)) as rkds ") 
		.append("           from mm_glcl_b glclb ") 
		.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
		.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
		.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
		.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
		.append("                                         icb.vsourcebillcode ") 
		.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and nvl(icb.dr, 0) = 0 ") 
		.append("            and lydjlx = 'isolation' ") ;
		if(datemonth==7){
			sql.append("            and (glcl.clrq || glcl.xxsj >= '"+date6+"' || '08:30:00' or '"+date6+"' is null) ") ; 
		}else if(datemonth==1){
			sql.append("            and (glcl.clrq || glcl.xxsj >= '"+date12+"' || '08:30:00' or '"+date12+"' is null) ") ;
		}else{
			sql.append("            and (glcl.clrq || glcl.xxsj >= '"+dateother+"' || '08:30:00' or '"+dateother+"' is null) ") ;
		}
		sql.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
		.append("                '" + prevTime2 + "' is null) ") 
		.append("            and (bas.invcode like '" + invclasscode + "%') ") 
		.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 
		.append("          order by bas.invcode)) ") ;*/
		
		
		
		
		
		
                //edit by yqq 2016-12-08  月产量  成品下线日报表（按料号查询，品控），取出入库表中单据类型为“产成品入库”及入库数量
		        //7月的月产量从6月30日开始取数，1月产量从12月31日取数，其余月份从上月29日开始取数
		
		sql.append(" select sum(sl) from (select innum as sl") 
		.append("   from (select d.invcode, ") 
		.append("                d.invname, ") 
		.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
		.append("                nvl(i.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
		.append("           from ic_general_bb1 a ") 
		.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
		.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
		.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
		.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
		.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
		.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
		.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
		.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
		.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
		.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and a.dr = '0' ");
		
		if(datemonth==7){
			sql.append("            and (i.xxrq || i.xxsj >= '"+date6+"' || '08:30:00' or '"+date6+"' is null) ") ; 
		}else if(datemonth==1){
			sql.append("            and (i.xxrq || i.xxsj >= '"+date12+"' || '08:30:00' or '"+date12+"' is null) ") ;
		}else{
			sql.append("            and (i.xxrq || i.xxsj >= '"+dateother+"' || '08:30:00' or '"+dateother+"' is null) ") ;
		}		
		sql.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ")		
		.append("            and (d.invcode like '" + invclasscode + "%') ") 
		.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
		.append("          order by d.invcode) ") 
		.append(" union all ") 
		.append(" select rksl as sl ") 
		.append("   from (select bas.invcode, ") 
		.append("                bas.invname, ") 
		.append("                sum(nvl(icb.ninnum, 0)) as rksl, ") 
		.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(icb.ninnum / glcl.zdsl, 2), 0)) as rkds ") 
		.append("           from mm_glcl_b glclb ") 
		.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
		.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
		.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
		.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
		.append("                                         icb.vsourcebillcode ") 
		.append("           left join ic_general_h h on h.cgeneralhid = icb.cgeneralhid ")
		.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and nvl(icb.dr, 0) = 0 ") 
		.append("            and lydjlx = 'isolation' ") 
		.append("            and h.cbilltypecode = '46' ");
		
		if(datemonth==7){
			sql.append("            and (glcl.clrq || glcl.xxsj >= '"+date6+"' || '08:30:00' or '"+date6+"' is null) ") ; 
		}else if(datemonth==1){
			sql.append("            and (glcl.clrq || glcl.xxsj >= '"+date12+"' || '08:30:00' or '"+date12+"' is null) ") ;
		}else{
			sql.append("            and (glcl.clrq || glcl.xxsj >= '"+dateother+"' || '08:30:00' or '"+dateother+"' is null) ") ;
		}
		
		sql.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
		.append("                '" + prevTime2 + "' is null) ") 
		.append("            and (bas.invcode like '" + invclasscode + "%') ") 
		.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 		
		.append("          order by bas.invcode)) ") ;
		//end by yqq 2016-12-08		
		
		
		
		
		
		
		
		
		
		
/*		//edit by yqq 2016-10-19 月产量 当月1号到当天  入库汇总表 不显示负数
		sql.append(" select sum(sl) from (select innum sl ") 
		.append("   from (select d.invcode, ") 
		.append("                d.invname, ") 
		.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
		.append("                nvl(i.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
		.append("           from ic_general_bb1 a ") 
		.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
		.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
		.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
		.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
		.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
		.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
		.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
		.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
		.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
		.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and a.dr = '0' ")
		.append("            and a.ninspacenum >='0' ");
		if(datemonth==7){
			sql.append("            and (i.xxrq || i.xxsj >= '"+date6+"' || '08:30:00' or '"+date6+"' is null) ") ; 
		}else if(datemonth==1){
			sql.append("            and (i.xxrq || i.xxsj >= '"+date12+"' || '08:30:00' or '"+date12+"' is null) ") ;
		}else{
			sql.append("            and (i.xxrq || i.xxsj >= '"+dateother+"' || '08:30:00' or '"+dateother+"' is null) ") ;
		}
		sql.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ")
		.append("            and (d.invcode like '" + invclasscode + "%') ") 
		.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
		.append("          having sum(nvl(a.ninspacenum, 0)) >=0 ") 		
		.append("          order by d.invcode) ") 
		.append(" union all ") 
		.append(" select rksl sl ") 
		.append("   from (select bas.invcode, ") 
		.append("                bas.invname, ") 
		.append("                -sum(nvl(icb.noutnum, 0)) as rksl, ") 
		.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(-icb.noutnum / glcl.zdsl, 2), 0)) as rkds ") 
		.append("           from mm_glcl_b glclb ") 
		.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
		.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
		.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
		.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
		.append("                                         icb.vsourcebillcode ") 
		.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and nvl(icb.dr, 0) = 0 ") 
		.append("            and icb.noutnum >= '0' ")		
		.append("            and lydjlx = 'isolation' ") ;
		if(datemonth==7){
			sql.append("            and (glcl.clrq || glcl.xxsj >= '"+date6+"' || '08:30:00' or '"+date6+"' is null) ") ; 
		}else if(datemonth==1){
			sql.append("            and (glcl.clrq || glcl.xxsj >= '"+date12+"' || '08:30:00' or '"+date12+"' is null) ") ;
		}else{
			sql.append("            and (glcl.clrq || glcl.xxsj >= '"+dateother+"' || '08:30:00' or '"+dateother+"' is null) ") ;
		}
		sql.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
		.append("                '" + prevTime2 + "' is null) ") 
		.append("            and (bas.invcode like '" + invclasscode + "%') ") 
		.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 
		.append("          having -sum(nvl(icb.noutnum, 0)) >=0 ") 		
		.append("          order by bas.invcode)) ") ;		
		//end by yqq 2016-10-19
*/		
		
		/*//edit by zwx 2015-12-8  月产量 当月1号到当天  入库汇总表
		
		sql.append(" SELECT SUM(COALESCE(ninnum, 0.0)) ") 
		.append("   FROM ic_general_h h ") 
		.append("  INNER JOIN ic_general_b b ") 
		.append("     ON h.cgeneralhid = b.cgeneralhid ") 
		.append("  INNER JOIN bd_invmandoc man ") 
		.append("     ON b.cinventoryid = man.pk_invmandoc ") 
		.append("  INNER JOIN bd_invbasdoc inv ") 
		.append("     ON man.pk_invbasdoc = inv.pk_invbasdoc ") 
		.append("   LEFT OUTER JOIN bd_invcl invcl ") 
		.append("     ON inv.pk_invcl = invcl.pk_invcl ") 
		.append("  WHERE ((h.cbilltypecode = '46') AND (b.ninnum IS NOT NULL) AND (h.dr = 0) AND ") 
		.append("        (b.dr = 0)) ") 
		.append("    AND dbizdate >= '" + monthDay + "' ") 
		.append("    AND dbizdate <= '" + prevTime2 + "' ") 
		.append(" and h.pk_corp = '" + pk_corp + "'  ") 
		.append("    AND (1 = 1) ") 
		.append("    and (1 = 1) ") 
		.append("    and (invcl.invclasscode LIKE '" + invclasscode + "%') ") 
		.append("    and (1 = 1) ") 
		.append("    and (1 = 1) ") 
		.append("    and (1 = 1) ") 
		.append("    and (1 = 1) ") 
		.append("    and (1 = 1) ");
		//end by zwx 
*/		
		obj = jdbc.executeQuery(sql.toString(), new ColumnProcessor());
		ret[2] = obj == null ? 0 : new UFDouble(obj.toString()).div(10000).setScale(scale, UFDouble.ROUND_HALF_UP);
		// 年产量
		sql = new StringBuilder();
		/*sql.append("select sum(sl) as rst from (");
		sql.append(" select sum(A.xxaglsl) as sl from mm_glzb_b A,mm_glzb B");
		sql.append(" where B.djzt=1 and B.pk_corp='" + pk_corp + "'");
		sql.append(" and B.xxrq ||' '|| B.xxsj>='" + yearTime1 + "' and B.xxrq ||' '|| B.xxsj<='" + yearTime2 + "'");
		sql.append(" and A.lh like '" + invclasscode + "%'");
		sql.append(" and nvl(B.jyjg,0)=0 and nvl(A.dr,0)=0 and nvl(B.dr,0)=0");
		sql.append(" and A.pk_glzb=B.pk_glzb");
		sql.append(" union all");
		sql.append(" select sum(A.hgsl) as sl from mm_glcl_b A,mm_glcl B,mm_glzb_b C,mm_glzb D");
		sql.append(" where D.djzt=1 and A.lydjlx='JYDJ'");
		sql.append(" and B.pk_corp='" + pk_corp + "'");
		sql.append(" and D.xxrq ||' '|| D.xxsj>='" + yearTime1 + "' and D.xxrq ||' '|| D.xxsj<='" + yearTime2 + "'");
		sql.append(" and C.lh like '" + invclasscode + "%'");
		sql.append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0 and nvl(C.dr,0)=0");
		sql.append(" and A.pk_glcl=B.pk_glcl");
		sql.append(" and A.pk_glzb_b=C.pk_glzb_b");
		sql.append(" and C.pk_glzb=D.pk_glzb");
		sql.append(" ) T");*/
		
		//edit by zwx 2015-9-17 上一年最后一天到当天的数量
		//年产量取入库汇总表，查询条件为从1月1日开始到上月最后一天加上当月的月产量取数. 一月的年产量和月产量取数是一样的
      
/*		if(datemonth!=1){
			sql.append(" select sum(sl) from (SELECT SUM(COALESCE(ninnum, 0.0)) as sl ") 
			.append(" FROM ic_general_h h, ") 
			.append("        ic_general_b b, ") 
			.append("        bd_invmandoc man, ") 
			.append("        bd_invbasdoc inv, ") 
			.append("        bd_invcl     invcl ") 
			.append("  where h.cgeneralhid = b.cgeneralhid ") 
			.append("    and b.cinventoryid = man.pk_invmandoc ") 
			.append("    and man.pk_invbasdoc = inv.pk_invbasdoc ") 
			.append("    and inv.pk_invcl = invcl.pk_invcl(+) ") 
			.append("    and ((((h.cbilltypecode = '45') AND (b.ninnum IS NOT NULL) AND ") 
			.append("        (h.dr = 0) AND (b.dr = 0) AND (b.fchecked = 0) AND ") 
			.append("        (h.cbiztype IN ") 
			.append("        (SELECT pk_busitype FROM bd_busitype WHERE verifyrule <> 'J') or ") 
			.append("        h.cbiztype is null)) or ") 
			.append("        ((h.cbilltypecode = '46' or h.cbilltypecode = '47' or ") 
			.append("        h.cbilltypecode = '48' or h.cbilltypecode = '49' or ") 
			.append("        h.cbilltypecode = '4A' or h.cbilltypecode = '4E' or ") 
			.append("        h.cbilltypecode = '4B') and (b.ninnum IS NOT NULL) and (h.dr = 0) and ") 
			.append("        (b.dr = 0))) AND dbizdate >= '" + nowyear + "' AND ") 
			.append("        dbizdate <= '" + premonth + "' and h.pk_corp in ('" + pk_corp + "') AND (1 = 1) and ") 
			.append("        (1 = 1) and (invcl.invclasscode like '" + invclasscode + "%') and (1 = 1) and ") 
		  	.append("        (1 = 1) and (1 = 1) and (1 = 1) and (1 = 1)) ") ;
			sql.append(" union all select sum(sl) as sl from (select innum as sl") 
			.append("   from (select d.invcode, ") 
			.append("                d.invname, ") 
			.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
			.append("                nvl(i.zdsl, 0) as zdsl, ") 
			.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
			.append("           from ic_general_bb1 a ") 
			.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
			.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
			.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
			.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
			.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
			.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
			.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
			.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
			.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
			.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
			.append("            and a.dr = '0' ") 
			.append("            and (i.xxrq || i.xxsj >= '" + sydate.toString() + "' || '08:30:00' or '" + sydate.toString() + "' is null) ") 
			.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ") 
			.append("            and (d.invcode like '" + invclasscode + "%') ") 
			.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
			.append("          order by d.invcode) ") 
			.append(" union all ") 
			.append(" select rksl as sl ") 
			.append("   from (select bas.invcode, ") 
			.append("                bas.invname, ") 
			.append("                -sum(nvl(icb.noutnum, 0)) as rksl, ") 
			.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
			.append("                sum(nvl(round(-icb.noutnum / glcl.zdsl, 2), 0)) as rkds ") 
			.append("           from mm_glcl_b glclb ") 
			.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
			.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
			.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
			.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
			.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
			.append("                                         icb.vsourcebillcode ") 
			.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
			.append("            and nvl(icb.dr, 0) = 0 ") 
			.append("            and lydjlx = 'isolation' ") 
			.append("            and (glcl.clrq || glcl.xxsj >= '" + sydate.toString() + "' || '08:30:00' or ") 
			.append("                '" + sydate.toString() + "' is null) ") 
			.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
			.append("                '" + prevTime2 + "' is null) ") 
			.append("            and (bas.invcode like '" + invclasscode + "%') ") 
			.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 
			.append("          order by bas.invcode))) ") ;
		}else{
			sql.append(" union all select sum(sl) as sl from (select innum as sl") 
			.append("   from (select d.invcode, ") 
			.append("                d.invname, ") 
			.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
			.append("                nvl(i.zdsl, 0) as zdsl, ") 
			.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
			.append("           from ic_general_bb1 a ") 
			.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
			.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
			.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
			.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
			.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
			.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
			.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
			.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
			.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
			.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
			.append("            and a.dr = '0' ") 
			.append("            and (i.xxrq || i.xxsj >= '" + sydate.toString() + "' || '08:30:00' or '" + sydate.toString() + "' is null) ") 
			.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ") 
			.append("            and (d.invcode like '" + invclasscode + "%') ") 
			.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
			.append("          order by d.invcode) ") 
			.append(" union all ") 
			.append(" select rksl as sl ") 
			.append("   from (select bas.invcode, ") 
			.append("                bas.invname, ") 
			.append("                -sum(nvl(icb.noutnum, 0)) as rksl, ") 
			.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
			.append("                sum(nvl(round(-icb.noutnum / glcl.zdsl, 2), 0)) as rkds ") 
			.append("           from mm_glcl_b glclb ") 
			.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
			.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
			.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
			.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
			.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
			.append("                                         icb.vsourcebillcode ") 
			.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
			.append("            and nvl(icb.dr, 0) = 0 ") 
			.append("            and lydjlx = 'isolation' ") 
			.append("            and (glcl.clrq || glcl.xxsj >= '" + sydate.toString() + "' || '08:30:00' or ") 
			.append("                '" + sydate.toString() + "' is null) ") 
			.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
			.append("                '" + prevTime2 + "' is null) ") 
			.append("            and (bas.invcode like '" + invclasscode + "%') ") 
			.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 
			.append("          order by bas.invcode))) ") ;
		}
		//end by zwx 
*/		
	
		
		
		
                //edit by yqq 2016-12-08  年产量  成品下线日报表（按料号查询，品控），取出入库表中单据类型为“产成品入库”及入库数量
		        //从前1年的12月31日开始
		        
		
		sql.append(" select sum(sl) from (select innum as sl") 
		.append("   from (select d.invcode, ") 
		.append("                d.invname, ") 
		.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
		.append("                nvl(i.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
		.append("           from ic_general_bb1 a ") 
		.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
		.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
		.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
		.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
		.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
		.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
		.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
		.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
		.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
		.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and a.dr = '0' ") 	
		.append("            and (i.xxrq || i.xxsj >= '" + date12 + "' || '08:30:00' or '" + date12 + "' is null) ") 
		.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ")  
		.append("            and (d.invcode like '" + invclasscode + "%') ") 		
		.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
		.append("          order by d.invcode) ") 
		.append(" union all ") 
		.append(" select rksl as sl ") 
		.append("   from (select bas.invcode, ") 
		.append("                bas.invname, ") 
		.append("                sum(nvl(icb.ninnum, 0)) as rksl, ") 
		.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
		.append("                sum(nvl(round(icb.ninnum / glcl.zdsl, 2), 0)) as rkds ") 
		.append("           from mm_glcl_b glclb ") 
		.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
		.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
		.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
		.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
		.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
		.append("                                         icb.vsourcebillcode ") 
		.append("           left join ic_general_h h on h.cgeneralhid = icb.cgeneralhid ")
		.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
		.append("            and nvl(icb.dr, 0) = 0 ") 
		.append("            and lydjlx = 'isolation' ") 
		.append("            and h.cbilltypecode = '46' ") 
		.append("            and (glcl.clrq || glcl.xxsj >= '" + date12 + "' || '08:30:00' or ") 
		.append("                '" + date12 + "' is null) ") 
		.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
		.append("                '" + prevTime2 + "' is null) ")  
		.append("            and (bas.invcode like '" + invclasscode + "%') ") 
		.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 		
		.append("          order by bas.invcode)) ") ;
		//end by yqq 2016-12-08
		

		
		
/*		    //edit by yqq 2016-10-19 上一年最后一天到当天的数量
		//年产量取入库汇总表，查询条件为从1月1日开始到上月最后一天加上当月的月产量取数. 一月的年产量和月产量取数是一样的，不显示负数
		
		if(datemonth!=1){
			sql.append(" select sum(sl) from (SELECT SUM(COALESCE(ninnum, 0.0)) as sl ") 
			.append(" FROM ic_general_h h, ") 
			.append("        ic_general_b b, ") 
			.append("        bd_invmandoc man, ") 
			.append("        bd_invbasdoc inv, ") 
			.append("        bd_invcl     invcl ") 
			.append("  where h.cgeneralhid = b.cgeneralhid ") 
			.append("    and b.cinventoryid = man.pk_invmandoc ") 
			.append("    and man.pk_invbasdoc = inv.pk_invbasdoc ") 
			.append("    and inv.pk_invcl = invcl.pk_invcl(+) ") 
			.append("    and ((((h.cbilltypecode = '45') AND (b.ninnum IS NOT NULL) AND ") 
			.append("        (h.dr = 0) AND (b.dr = 0) AND (b.fchecked = 0) AND ") 
			.append("        (h.cbiztype IN ") 
			.append("        (SELECT pk_busitype FROM bd_busitype WHERE verifyrule <> 'J') or ") 
			.append("        h.cbiztype is null)) or ") 
			.append("        ((h.cbilltypecode = '46' or h.cbilltypecode = '47' or ") 
			.append("        h.cbilltypecode = '48' or h.cbilltypecode = '49' or ") 
			.append("        h.cbilltypecode = '4A' or h.cbilltypecode = '4E' or ") 
			.append("        h.cbilltypecode = '4B') and (b.ninnum IS NOT NULL) and (h.dr = 0) and ") 
			.append("        (b.dr = 0))) AND dbizdate >= '" + nowyear + "' AND ") 
			.append("        dbizdate <= '" + premonth + "' and h.pk_corp in ('" + pk_corp + "') AND (1 = 1) and ") 
			.append("        (1 = 1) and (invcl.invclasscode like '" + invclasscode + "%') and (1 = 1) and ") 
		  	.append("        (1 = 1) and (1 = 1) and (1 = 1) and (1 = 1)) ") ;
			sql.append(" union all select sum(sl) as sl from (select innum as sl") 
			.append("   from (select d.invcode, ") 
			.append("                d.invname, ") 
			.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
			.append("                nvl(i.zdsl, 0) as zdsl, ") 
			.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
			.append("           from ic_general_bb1 a ") 
			.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
			.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
			.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
			.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
			.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
			.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
			.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
			.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
			.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
			.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
			.append("            and a.dr = '0' ") 
			.append("            and (i.xxrq || i.xxsj >= '" + sydate.toString() + "' || '08:30:00' or '" + sydate.toString() + "' is null) ") 
			.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ") 
			.append("            and (d.invcode like '" + invclasscode + "%') ") 
			.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
			.append("          having  sum(nvl(a.ninspacenum, 0)) >=0 ") 			
			.append("          order by d.invcode) ") 
			.append(" union all ") 
			.append(" select rksl as sl ") 
			.append("   from (select bas.invcode, ") 
			.append("                bas.invname, ") 
			.append("                -sum(nvl(icb.noutnum, 0)) as rksl, ") 
			.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
			.append("                sum(nvl(round(-icb.noutnum / glcl.zdsl, 2), 0)) as rkds ") 
			.append("           from mm_glcl_b glclb ") 
			.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
			.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
			.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
			.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
			.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
			.append("                                         icb.vsourcebillcode ") 
			.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
			.append("            and nvl(icb.dr, 0) = 0 ") 
			.append("            and lydjlx = 'isolation' ") 
			.append("            and (glcl.clrq || glcl.xxsj >= '" + sydate.toString() + "' || '08:30:00' or ") 
			.append("                '" + sydate.toString() + "' is null) ") 
			.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
			.append("                '" + prevTime2 + "' is null) ") 
			.append("            and (bas.invcode like '" + invclasscode + "%') ") 
			.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 
			.append("          having  -sum(nvl(icb.noutnum, 0))  >=0   ") 	
			.append("          order by bas.invcode))) ") ;
		}else{
			sql.append(" union all select sum(sl) as sl from (select innum as sl") 
			.append("   from (select d.invcode, ") 
			.append("                d.invname, ") 
			.append("                sum(nvl(a.ninspacenum, 0)) as innum, ") 
			.append("                nvl(i.zdsl, 0) as zdsl, ") 
			.append("                sum(nvl(round(b.ninnum / i.zdsl, 2), 0)) as rkds ") 
			.append("           from ic_general_bb1 a ") 
			.append("           left join ic_general_b b on b.cgeneralbid = a.cgeneralbid ") 
			.append("           left join ic_general_h c on c.cgeneralhid = b.cgeneralhid ") 
			.append("           left join bd_invbasdoc d on d.pk_invbasdoc = b.cinvbasid ") 
			.append("           left join bd_cargdoc e on e.pk_cargdoc = a.cspaceid ") 
			.append("           left join bd_billtype f on f.pk_billtypecode = c.cbilltypecode ") 
			.append("           left join bd_stordoc g on g.pk_stordoc = c.cwarehouseid ") 
			.append("           left join mm_glzb_b h on b.vsourcebillcode = h.pk_glzb_b ") 
			.append("           left join mm_glzb i on h.pk_glzb = i.pk_glzb ") 
			.append("           left join mm_glcl_b j on j.pk_glcl_b = b.vsourcebillcode ") 
			.append("          where (a.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
			.append("            and a.dr = '0' ") 
			.append("            and (i.xxrq || i.xxsj >= '" + sydate.toString() + "' || '08:30:00' or '" + sydate.toString() + "' is null) ") 
			.append("            and (i.xxrq || i.xxsj <= '" + prevTime2 + "' || '08:30:00' or '" + prevTime2 + "' is null) ") 
			.append("            and (d.invcode like '" + invclasscode + "%') ") 
			.append("          group by d.invcode, d.invname, nvl(i.zdsl, 0) ") 
			.append("          having sum(nvl(a.ninspacenum, 0)) >=0   ") 	
			.append("          order by d.invcode) ") 
			.append(" union all ") 
			.append(" select rksl as sl ") 
			.append("   from (select bas.invcode, ") 
			.append("                bas.invname, ") 
			.append("                -sum(nvl(icb.noutnum, 0)) as rksl, ") 
			.append("                nvl(glcl.zdsl, 0) as zdsl, ") 
			.append("                sum(nvl(round(-icb.noutnum / glcl.zdsl, 2), 0)) as rkds ") 
			.append("           from mm_glcl_b glclb ") 
			.append("           left join mm_glcl glcl on glcl.pk_glcl = glclb.pk_glcl ") 
			.append("           left join bd_invmandoc man on glcl.cinventoryid = man.pk_invmandoc ") 
			.append("           left join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc ") 
			.append("           left join bd_cargdoc car on glclb.cspaceid = car.pk_cargdoc ") 
			.append("           left join ic_general_b icb on glclb.pk_glcl_b = ") 
			.append("                                         icb.vsourcebillcode ") 
			.append("          where (glcl.pk_corp = '" + pk_corp + "' or '" + pk_corp + "' is null) ") 
			.append("            and nvl(icb.dr, 0) = 0 ") 
			.append("            and lydjlx = 'isolation' ") 
			.append("            and (glcl.clrq || glcl.xxsj >= '" + sydate.toString() + "' || '08:30:00' or ") 
			.append("                '" + sydate.toString() + "' is null) ") 
			.append("            and (glcl.clrq || glcl.xxsj <= '" + prevTime2 + "' || '08:30:00' or ") 
			.append("                '" + prevTime2 + "' is null) ") 
			.append("            and (bas.invcode like '" + invclasscode + "%') ") 
			.append("          group by bas.invcode, bas.invname, nvl(glcl.zdsl, 0) ") 
			.append("          having -sum(nvl(icb.noutnum, 0)) >=0   ") 
			.append("          order by bas.invcode))) ") ;
		}
		
	   //end by yqq 2016-10-19
*/	
		
		obj = jdbc.executeQuery(sql.toString(), new ColumnProcessor());
		ret[3] = obj == null ? 0 : new UFDouble(obj.toString()).div(10000).setScale(scale, UFDouble.ROUND_HALF_UP);
		
		// 总隔离数量
		sql = new StringBuilder();
		//		sql.append("select sum(C.xxaglsl)-sum(A.dclsl) as rst from mm_glcl_b A");
		//		sql.append(" inner join mm_glcl B on A.pk_glcl=B.pk_glcl");
		//		sql.append(" inner join mm_glzb_b C on A.pk_glzb_b=C.pk_glzb_b");
		//		sql.append(" inner join mm_glzb D on C.pk_glzb=D.pk_glzb");
		//		sql.append(" where B.clrq>='2013-01-01' and D.djzt=0 and B.pk_corp='" + pk_corp + "'");
		//		sql.append(" and D.jyjg=1 and C.lh like '" + invclasscode + "%'");
		//		sql.append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0 and nvl(C.dr,0)=0 and nvl(D.dr,0)=0");
		sql.append(" select sum(sl) from (");
		sql.append(" select sum(b.xxaglsl) as sl,b.lh");
		sql.append(" from mm_glzb a, mm_glzb_b b");
		sql.append(" where (a.pk_glzb = b.pk_glzb and a.djzt = 0 and a.jyjg = 1 and");
		sql.append(" a.billsign = 'JYDJ' and a.pk_corp = '" + pk_corp + "' and nvl(b.clzt, 0) != 1 and");
		sql.append(" nvl(a.dr, 0) != '1' and nvl(b.dr, 0) != '1' and");
		sql.append(" b.dh not in (select cldh from mm_glcl_b where nvl(dr, 0) = 0))");
		sql.append(" group by b.lh");
		sql.append(" union all");
		sql.append(" select sum(b.xxaglsl) as sl,b.lh");
		sql.append("   from mm_glzb a, mm_glzb_b b");
		sql.append("  where (a.pk_glzb = b.pk_glzb and nvl(a.djzt, 0) = 0 and");
		sql.append("        a.billsign = 'isolation' and a.pk_corp = '" + pk_corp + "' and");
		sql.append("        nvl(b.clzt, 0) != 1 and nvl(a.dr, 0) != '1' and nvl(b.dr, 0) != '1')");
		sql.append(" group by b.lh");
		sql.append(" ) where lh like '" + invclasscode + "%'");
		obj = jdbc.executeQuery(sql.toString(), new ColumnProcessor());
		ret[4] = obj == null ? 0 : new UFDouble(obj.toString()).div(10000).setScale(scale, UFDouble.ROUND_HALF_UP);
		// 日销量  成品发货明细表（含调拨与订单）
		sql = new StringBuilder();
		/*sql.append("select sum(noutnum) from ic_general_b A");
		sql.append(" inner join ic_general_h B on A.cgeneralhid=B.cgeneralhid");
		sql.append(" inner join bd_invbasdoc C on A.cinvbasid=C.pk_invbasdoc");
		sql.append(" inner join bd_rdcl D on B.cdispatcherid=D.pk_rdcl");
		sql.append(" where B.cregister is not null");
		sql.append(" and B.pk_corp='" + pk_corp + "'");
		sql.append(" and B.cbilltypecode='4C'");
		sql.append(" and D.rdcode='0601'");
		sql.append(" and C.invcode like '" + invclasscode + "%'");
		sql.append(" and B.ts>='" + prevTime1 + "' and B.ts<='" + prevTime2 + "'");
		sql.append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0 and nvl(C.dr,0)=0");*/
		
		//edit by zwx 日销量发短信日的前一天的销售
		sql.append(" select sum(uu.totalnum) ") 
		.append("   from (select s.gysbm, ") 
		.append("                s.gys, ") 
		.append("                s.dbilldate, ") 
		.append("                s.vbillcode, ") 
		.append("                s.busicode, ") 
		.append("                s.sobillcode, ") 
		.append("                s.vuserdef12, ") 
		.append("                s.invcode, ") 
		.append("                s.invname, ") 
		.append("                s.vdiliveraddress, ") 
		.append("                s.noutnum, ") 
		.append("                s.duo, ") 
		.append("                duo * s.noutnum totalnum, ") 
		.append("                (select custcode from bd_cubasdoc where custname = cys) cysbm, ") 
		.append("                s.cys, ") 
		.append("                s.vuserdef7, ") 
		.append("                s.ck, ") 
		.append("                s.drck ") 
		.append("           from (select nov.* from (select tm1.dbilldate, ") 
		.append("                        tm1.vbillcode, ") 
		.append("                        tm1.vuserdef12, ") 
		.append("                        tm1.vuserdef7, ") 
		.append("                        td1.noutnum ninnum, ") 
		.append("                        tb4.custcode gysbm, ") 
		.append("                        tb4.custname gys, ") 
		.append("                        tb3.custcode cysbm, ") 
		.append("                        (CASE nvl(tb3.custname, '1') ") 
		.append("                          when '1' then ") 
		.append("                           tm1.vuserdef13 ") 
		.append("                          ELSE ") 
		.append("                           tb3.custname ") 
		.append("                        END) cys, ") 
		.append("                        tb1.storname ck, ") 
		.append("                        tbs.storname drck, ") 
		.append("                        td1.noutnum, ") 
		.append("                        count(td1.crowno) duo, ") 
		.append("                        tb5.invcode, ") 
		.append("                        tb5.invname, ") 
		.append("                        tb6.def4, ") 
		.append("                        tm1.vdiliveraddress, ") 
		.append("                        td1.vfirstbillcode as sobillcode, ") 
		.append("                        (Case ") 
		.append("          when regexp_like(bus.busicode, '[GS]00[2,7]') then '代加工' ") 
		.append("          when regexp_like(bus.busicode, 'S00[1,3]') then '自产' ") 
		.append("          else '其他' ") 
		.append("          END) busicode ") 
		.append("                   from ic_general_b td1 ") 
		.append("                   left join ic_general_h tm1 on td1.cgeneralhid = ") 
		.append("                                                 tm1.cgeneralhid ") 
		.append("                   left join bd_busitype bus on bus.pk_busitype=tm1.cbiztype ") 
		.append("                   left join bd_stordoc tb1 on tb1.pk_stordoc = ") 
		.append("                                               tm1.cwarehouseid ") 
		.append("                   left join bd_stordoc tbs on tbs.pk_stordoc = tm1.cotherwhid ") 
		.append("                   left join dm_trancust tb2 on tb2.pk_trancust = ") 
		.append("                                                tm1.cwastewarehouseid ") 
		.append("                   left join bd_cubasdoc tb3 on tb3.pk_cubasdoc = ") 
		.append("                                                tb2.pkcusmandoc ") 
		.append("                   left join bd_cumandoc cum on cum.pk_cumandoc=tm1.ccustomerid                                             ") 
		.append("                   left join bd_cubasdoc tb4 on tb4.pk_cubasdoc = ") 
		.append("                                                cum.pk_cubasdoc ") 
		.append("                   left join bd_invbasdoc tb5 on tb5.pk_invbasdoc = ") 
		.append("                                                 td1.cinvbasid ") 
		.append("                   left join bd_invmandoc tb6 on tb6.pk_invmandoc = ") 
		.append("                                                 td1.cinventoryid ") 
		.append("                   left join bd_invcl invcl on tb5.pk_invcl = invcl.pk_invcl ") 
		.append("                  where nvl(td1.dr, 0) = 0 ") 
		.append(" 					and regexp_like(tm1.cbilltypecode, ") 
		.append("                                    (CASE nvl('发货', '1') when '调拨' then '4Y' when '发货' then '4C' when '1' then ") 
		.append("                                     '4[YC]' END)) ") 
		.append("                    and td1.pk_corp = '" + pk_corp + "' ") 
		.append("                    and (invcl.invclasscode like '" + invclasscode + "' || '%' or '" + invclasscode + "' is null) ") 
		.append("                    and (tm1.dbilldate >= '" + prevTime1 + "' or '" + prevTime1 + "' is null) ") 
		.append("                    and (tm1.dbilldate <= '" + prevTime1 + "' or '" + prevTime1 + "' is null) ") 
		.append("                    and nvl(td1.noutnum, 0) <> 0 ") 
		.append("                  group by tm1.dbilldate, ") 
		.append("                           tm1.vbillcode, ") 
		.append("                           tm1.vuserdef12, ") 
		.append("                           tm1.vuserdef7, ") 
		.append("                           tb4.custcode, ") 
		.append("                           tb4.custname, ") 
		.append("                           tb3.custname, ") 
		.append("                           tb3.custcode, ") 
		.append("                           td1.noutnum, ") 
		.append("                           tb1.storname, ") 
		.append("                           tbs.storname, ") 
		.append("                           tb5.invcode, ") 
		.append("                           tb5.invname, ") 
		.append("                           tb6.def4, ") 
		.append("                           tm1.vuserdef13, ") 
		.append("                           tm1.vdiliveraddress, ") 
		.append("                           td1.vfirstbillcode, ") 
		.append("                           bus.busicode) nov ") 
		.append("                           where nov.busicode= '' or '' is null) s ") 
		.append("           left join ic_general_b icb on icb.vsourcebillcode = s.vbillcode ") 
		.append("           left join ic_general_h ich on icb.cgeneralhid = ich.cgeneralhid ") 
		.append("          where nvl(ich.dr, 0) = 0 ") 
		.append("          group by s.gysbm, ") 
		.append("                   s.gys, ") 
		.append("                   s.dbilldate, ") 
		.append("                   s.vbillcode, ") 
		.append("                   s.vuserdef12, ") 
		.append("                   s.invcode, ") 
		.append("                   s.invname, ") 
		.append("                   s.vdiliveraddress, ") 
		.append("                   s.noutnum, ") 
		.append("                   s.duo, ") 
		.append("                   s.cys, ") 
		.append("                   s.vuserdef7, ") 
		.append("                   s.ck, ") 
		.append("                   s.drck, ") 
		.append("                   ich.vbillcode,sobillcode,s.busicode) uu  "); 

		
		obj = jdbc.executeQuery(sql.toString(), new ColumnProcessor());
		ret[5] = obj == null ? 0 : new UFDouble(obj.toString()).div(10000).setScale(scale, UFDouble.ROUND_HALF_UP);
		// 月销量
		sql = new StringBuilder();
		/*sql.append("select sum(noutnum) from ic_general_b A");
		sql.append(" inner join ic_general_h B on A.cgeneralhid=B.cgeneralhid");
		sql.append(" inner join bd_invbasdoc C on A.cinvbasid=C.pk_invbasdoc");
		sql.append(" inner join bd_rdcl D on B.cdispatcherid=D.pk_rdcl");
		sql.append(" where B.cregister is not null");
		sql.append(" and B.pk_corp='" + pk_corp + "'");
		sql.append(" and B.cbilltypecode='4C'");
		sql.append(" and D.rdcode='0601'");
		sql.append(" and C.invcode like '" + invclasscode + "%'");
		sql.append(" and B.ts>='" + monthTime1 + "' and B.ts<='" + monthTime2 + "'");
		sql.append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0 and nvl(C.dr,0)=0");*/
		
		//edit by zwx 2015-9-17 当月1日到发送日前一天

		sql.append(" select sum(uu.totalnum) ") 
		.append("   from (select s.gysbm, ") 
		.append("                s.gys, ") 
		.append("                s.dbilldate, ") 
		.append("                s.vbillcode, ") 
		.append("                s.busicode, ") 
		.append("                s.sobillcode, ") 
		.append("                s.vuserdef12, ") 
		.append("                s.invcode, ") 
		.append("                s.invname, ") 
		.append("                s.vdiliveraddress, ") 
		.append("                s.noutnum, ") 
		.append("                s.duo, ") 
		.append("                duo * s.noutnum totalnum, ") 
		.append("                (select custcode from bd_cubasdoc where custname = cys) cysbm, ") 
		.append("                s.cys, ") 
		.append("                s.vuserdef7, ") 
		.append("                s.ck, ") 
		.append("                s.drck ") 
		.append("           from (select nov.* from (select tm1.dbilldate, ") 
		.append("                        tm1.vbillcode, ") 
		.append("                        tm1.vuserdef12, ") 
		.append("                        tm1.vuserdef7, ") 
		.append("                        td1.noutnum ninnum, ") 
		.append("                        tb4.custcode gysbm, ") 
		.append("                        tb4.custname gys, ") 
		.append("                        tb3.custcode cysbm, ") 
		.append("                        (CASE nvl(tb3.custname, '1') ") 
		.append("                          when '1' then ") 
		.append("                           tm1.vuserdef13 ") 
		.append("                          ELSE ") 
		.append("                           tb3.custname ") 
		.append("                        END) cys, ") 
		.append("                        tb1.storname ck, ") 
		.append("                        tbs.storname drck, ") 
		.append("                        td1.noutnum, ") 
		.append("                        count(td1.crowno) duo, ") 
		.append("                        tb5.invcode, ") 
		.append("                        tb5.invname, ") 
		.append("                        tb6.def4, ") 
		.append("                        tm1.vdiliveraddress, ") 
		.append("                        td1.vfirstbillcode as sobillcode, ") 
		.append("                        (Case ") 
		.append("          when regexp_like(bus.busicode, '[GS]00[2,7]') then '代加工' ") 
		.append("          when regexp_like(bus.busicode, 'S00[1,3]') then '自产' ") 
		.append("          else '其他' ") 
		.append("          END) busicode ") 
		.append("                   from ic_general_b td1 ") 
		.append("                   left join ic_general_h tm1 on td1.cgeneralhid = ") 
		.append("                                                 tm1.cgeneralhid ") 
		.append("                   left join bd_busitype bus on bus.pk_busitype=tm1.cbiztype ") 
		.append("                   left join bd_stordoc tb1 on tb1.pk_stordoc = ") 
		.append("                                               tm1.cwarehouseid ") 
		.append("                   left join bd_stordoc tbs on tbs.pk_stordoc = tm1.cotherwhid ") 
		.append("                   left join dm_trancust tb2 on tb2.pk_trancust = ") 
		.append("                                                tm1.cwastewarehouseid ") 
		.append("                   left join bd_cubasdoc tb3 on tb3.pk_cubasdoc = ") 
		.append("                                                tb2.pkcusmandoc ") 
		.append("                   left join bd_cumandoc cum on cum.pk_cumandoc=tm1.ccustomerid                                             ") 
		.append("                   left join bd_cubasdoc tb4 on tb4.pk_cubasdoc = ") 
		.append("                                                cum.pk_cubasdoc ") 
		.append("                   left join bd_invbasdoc tb5 on tb5.pk_invbasdoc = ") 
		.append("                                                 td1.cinvbasid ") 
		.append("                   left join bd_invmandoc tb6 on tb6.pk_invmandoc = ") 
		.append("                                                 td1.cinventoryid ") 
		.append("                   left join bd_invcl invcl on tb5.pk_invcl = invcl.pk_invcl ") 
		.append("                  where nvl(td1.dr, 0) = 0 ") 
		.append(" 					and regexp_like(tm1.cbilltypecode, ") 
		.append("                                    (CASE nvl('发货', '1') when '调拨' then '4Y' when '发货' then '4C' when '1' then ") 
		.append("                                     '4[YC]' END)) ") 
		.append("                    and td1.pk_corp = '" + pk_corp + "' ") 
		.append("                    and (invcl.invclasscode like '" + invclasscode + "' || '%' or '" + invclasscode + "' is null) ") 
		.append("                    and (tm1.dbilldate >= '" + monthDay + "' or '" + monthDay + "' is null) ") 
		.append("                    and (tm1.dbilldate <= '" + prevTime1 + "' or '" + prevTime1 + "' is null) ") 
		.append("                    and nvl(td1.noutnum, 0) <> 0 ") 
		.append("                  group by tm1.dbilldate, ") 
		.append("                           tm1.vbillcode, ") 
		.append("                           tm1.vuserdef12, ") 
		.append("                           tm1.vuserdef7, ") 
		.append("                           tb4.custcode, ") 
		.append("                           tb4.custname, ") 
		.append("                           tb3.custname, ") 
		.append("                           tb3.custcode, ") 
		.append("                           td1.noutnum, ") 
		.append("                           tb1.storname, ") 
		.append("                           tbs.storname, ") 
		.append("                           tb5.invcode, ") 
		.append("                           tb5.invname, ") 
		.append("                           tb6.def4, ") 
		.append("                           tm1.vuserdef13, ") 
		.append("                           tm1.vdiliveraddress, ") 
		.append("                           td1.vfirstbillcode, ") 
		.append("                           bus.busicode) nov ") 
		.append("                           where nov.busicode= '' or '' is null) s ") 
		.append("           left join ic_general_b icb on icb.vsourcebillcode = s.vbillcode ") 
		.append("           left join ic_general_h ich on icb.cgeneralhid = ich.cgeneralhid ") 
		.append("          where nvl(ich.dr, 0) = 0 ") 
		.append("          group by s.gysbm, ") 
		.append("                   s.gys, ") 
		.append("                   s.dbilldate, ") 
		.append("                   s.vbillcode, ") 
		.append("                   s.vuserdef12, ") 
		.append("                   s.invcode, ") 
		.append("                   s.invname, ") 
		.append("                   s.vdiliveraddress, ") 
		.append("                   s.noutnum, ") 
		.append("                   s.duo, ") 
		.append("                   s.cys, ") 
		.append("                   s.vuserdef7, ") 
		.append("                   s.ck, ") 
		.append("                   s.drck, ") 
		.append("                   ich.vbillcode,sobillcode,s.busicode) uu  "); 
		
		
		obj = jdbc.executeQuery(sql.toString(), new ColumnProcessor());
		ret[6] = obj == null ? 0 : new UFDouble(obj.toString()).div(10000).setScale(scale, UFDouble.ROUND_HALF_UP);
		// 年销量
		sql = new StringBuilder();
		/*sql.append("select sum(noutnum) from ic_general_b A");
		sql.append(" inner join ic_general_h B on A.cgeneralhid=B.cgeneralhid");
		sql.append(" inner join bd_invbasdoc C on A.cinvbasid=C.pk_invbasdoc");
		sql.append(" inner join bd_rdcl D on B.cdispatcherid=D.pk_rdcl");
		sql.append(" where B.cregister is not null");
		sql.append(" and B.pk_corp='" + pk_corp + "'");
		sql.append(" and B.cbilltypecode='4C'");
		sql.append(" and D.rdcode='0601'");
		sql.append(" and C.invcode like '" + invclasscode + "%'");
		sql.append(" and B.ts>='" + yearTime1 + "' and B.ts<='" + yearTime2 + "'");
		sql.append(" and nvl(A.dr,0)=0 and nvl(B.dr,0)=0 and nvl(C.dr,0)=0");*/
		
		//edit by zwx 2015-9-17 当年一日到发送日前一天
		sql.append(" select sum(uu.totalnum) ") 
		.append("   from (select s.gysbm, ") 
		.append("                s.gys, ") 
		.append("                s.dbilldate, ") 
		.append("                s.vbillcode, ") 
		.append("                s.busicode, ") 
		.append("                s.sobillcode, ") 
		.append("                s.vuserdef12, ") 
		.append("                s.invcode, ") 
		.append("                s.invname, ") 
		.append("                s.vdiliveraddress, ") 
		.append("                s.noutnum, ") 
		.append("                s.duo, ") 
		.append("                duo * s.noutnum totalnum, ") 
		.append("                (select custcode from bd_cubasdoc where custname = cys) cysbm, ") 
		.append("                s.cys, ") 
		.append("                s.vuserdef7, ") 
		.append("                s.ck, ") 
		.append("                s.drck ") 
		.append("           from (select nov.* from (select tm1.dbilldate, ") 
		.append("                        tm1.vbillcode, ") 
		.append("                        tm1.vuserdef12, ") 
		.append("                        tm1.vuserdef7, ") 
		.append("                        td1.noutnum ninnum, ") 
		.append("                        tb4.custcode gysbm, ") 
		.append("                        tb4.custname gys, ") 
		.append("                        tb3.custcode cysbm, ") 
		.append("                        (CASE nvl(tb3.custname, '1') ") 
		.append("                          when '1' then ") 
		.append("                           tm1.vuserdef13 ") 
		.append("                          ELSE ") 
		.append("                           tb3.custname ") 
		.append("                        END) cys, ") 
		.append("                        tb1.storname ck, ") 
		.append("                        tbs.storname drck, ") 
		.append("                        td1.noutnum, ") 
		.append("                        count(td1.crowno) duo, ") 
		.append("                        tb5.invcode, ") 
		.append("                        tb5.invname, ") 
		.append("                        tb6.def4, ") 
		.append("                        tm1.vdiliveraddress, ") 
		.append("                        td1.vfirstbillcode as sobillcode, ") 
		.append("                        (Case ") 
		.append("          when regexp_like(bus.busicode, '[GS]00[2,7]') then '代加工' ") 
		.append("          when regexp_like(bus.busicode, 'S00[1,3]') then '自产' ") 
		.append("          else '其他' ") 
		.append("          END) busicode ") 
		.append("                   from ic_general_b td1 ") 
		.append("                   left join ic_general_h tm1 on td1.cgeneralhid = ") 
		.append("                                                 tm1.cgeneralhid ") 
		.append("                   left join bd_busitype bus on bus.pk_busitype=tm1.cbiztype ") 
		.append("                   left join bd_stordoc tb1 on tb1.pk_stordoc = ") 
		.append("                                               tm1.cwarehouseid ") 
		.append("                   left join bd_stordoc tbs on tbs.pk_stordoc = tm1.cotherwhid ") 
		.append("                   left join dm_trancust tb2 on tb2.pk_trancust = ") 
		.append("                                                tm1.cwastewarehouseid ") 
		.append("                   left join bd_cubasdoc tb3 on tb3.pk_cubasdoc = ") 
		.append("                                                tb2.pkcusmandoc ") 
		.append("                   left join bd_cumandoc cum on cum.pk_cumandoc=tm1.ccustomerid                                             ") 
		.append("                   left join bd_cubasdoc tb4 on tb4.pk_cubasdoc = ") 
		.append("                                                cum.pk_cubasdoc ") 
		.append("                   left join bd_invbasdoc tb5 on tb5.pk_invbasdoc = ") 
		.append("                                                 td1.cinvbasid ") 
		.append("                   left join bd_invmandoc tb6 on tb6.pk_invmandoc = ") 
		.append("                                                 td1.cinventoryid ") 
		.append("                   left join bd_invcl invcl on tb5.pk_invcl = invcl.pk_invcl ") 
		.append("                  where nvl(td1.dr, 0) = 0 ") 
		.append(" 					and regexp_like(tm1.cbilltypecode, ") 
		.append("                                    (CASE nvl('发货', '1') when '调拨' then '4Y' when '发货' then '4C' when '1' then ") 
		.append("                                     '4[YC]' END)) ") 
		.append("                    and td1.pk_corp = '" + pk_corp + "' ") 
		.append("                    and (invcl.invclasscode like '" + invclasscode + "' || '%' or '" + invclasscode + "' is null) ") 
		.append("                    and (tm1.dbilldate >= '" + yearDay  + "' or '" + yearDay  + "' is null) ") 
		.append("                    and (tm1.dbilldate <= '" + prevTime1 + "' or '" + prevTime1 + "' is null) ") 
		.append("                    and nvl(td1.noutnum, 0) <> 0 ") 
		.append("                  group by tm1.dbilldate, ") 
		.append("                           tm1.vbillcode, ") 
		.append("                           tm1.vuserdef12, ") 
		.append("                           tm1.vuserdef7, ") 
		.append("                           tb4.custcode, ") 
		.append("                           tb4.custname, ") 
		.append("                           tb3.custname, ") 
		.append("                           tb3.custcode, ") 
		.append("                           td1.noutnum, ") 
		.append("                           tb1.storname, ") 
		.append("                           tbs.storname, ") 
		.append("                           tb5.invcode, ") 
		.append("                           tb5.invname, ") 
		.append("                           tb6.def4, ") 
		.append("                           tm1.vuserdef13, ") 
		.append("                           tm1.vdiliveraddress, ") 
		.append("                           td1.vfirstbillcode, ") 
		.append("                           bus.busicode) nov ") 
		.append("                           where nov.busicode= '' or '' is null) s ") 
		.append("           left join ic_general_b icb on icb.vsourcebillcode = s.vbillcode ") 
		.append("           left join ic_general_h ich on icb.cgeneralhid = ich.cgeneralhid ") 
		.append("          where nvl(ich.dr, 0) = 0 ") 
		.append("          group by s.gysbm, ") 
		.append("                   s.gys, ") 
		.append("                   s.dbilldate, ") 
		.append("                   s.vbillcode, ") 
		.append("                   s.vuserdef12, ") 
		.append("                   s.invcode, ") 
		.append("                   s.invname, ") 
		.append("                   s.vdiliveraddress, ") 
		.append("                   s.noutnum, ") 
		.append("                   s.duo, ") 
		.append("                   s.cys, ") 
		.append("                   s.vuserdef7, ") 
		.append("                   s.ck, ") 
		.append("                   s.drck, ") 
		.append("                   ich.vbillcode,sobillcode,s.busicode) uu  "); 
		
		obj = jdbc.executeQuery(sql.toString(), new ColumnProcessor());
		ret[7] = obj == null ? 0 : new UFDouble(obj.toString()).div(10000).setScale(scale, UFDouble.ROUND_HALF_UP);
		return ret;
	}

	public int getImplmentsType() {
		return 0;
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

	public IAlertMessage implementReturnFormatMsg(Key[] arg0, String arg1, UFDate arg2) throws BusinessException {
		return null;
	}

	public Object implementReturnObject(Key[] arg0, String arg1, UFDate arg2) throws BusinessException {
		return null;
	}

	public boolean implementWriteFile(Key[] arg0, String arg1, String arg2, UFDate arg3) throws BusinessException {
		return false;
	}

}
