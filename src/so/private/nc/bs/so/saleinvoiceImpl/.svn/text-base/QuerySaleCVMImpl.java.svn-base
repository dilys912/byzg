package nc.bs.so.saleinvoiceImpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleSendCvmVO;

/**
 * 查询发出商品数据信息 2019年3月12日
 * 
 * @author pengjia
 * 
 */
@SuppressWarnings("all")
public class QuerySaleCVMImpl {

	public void sendCVM(String unitcode) throws DAOException {
		IUAPQueryBS bs = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		/*Log_Exception le=new Log_Exception();
    	String path = "/share/byzghome/NC5011/resources/filezl.txt";
			try {
			le.writeEror_to_txt(path, unitcode+"代码在运行43行，开始查询发出商品");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}*/
		// 公司
		String cbiztype = null;
		if (unitcode.equals("10301")) {
			cbiztype = "1016A21000000000WOPI"; // 成品销售业务流程(上海)
		} else if (unitcode.equals("10307")) {
			cbiztype = "1071A21000000000OTC4"; // 成品销售业务流程(武汉)
		} else if (unitcode.equals("10314")) {
			cbiztype = "1103A2100000000016TZ"; // 成品销售业务流程(哈尔滨)
		} else if (unitcode.equals("10313")) {
			cbiztype = "1097A2100000000018J9"; // 成品销售业务流程(河南)
		} else if (unitcode.equals("10302")) {
			cbiztype = "1017A210000000012Z99"; // 成品销售业务流程(河北)
		} else if (unitcode.equals("10303")) {
			cbiztype = "1018A21000000001HZS9"; // 成品销售业务流程(成都)
		} else if (unitcode.equals("10304")) {
			cbiztype = "1019A210000000013ZXI"; // 成品销售业务流程(佛山)
		}else if(unitcode.equals("10318")){
			cbiztype ="110711100000000000SI"; //成品销售业务流程(宝翼2)
		}
		// 格式为年月 [2019-03]
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date currdate = new Date();
		// 格式化系统时间取年月
		String startDate = format.format(currdate);
		String sdate = startDate.substring(0, 4);
		String edate = startDate.substring(5, 7);
		int benyue = Integer.parseInt(edate)-1;
		int shangyue = Integer.parseInt(edate)-2;
		int lishi = Integer.parseInt(edate)-3;
		//edit  by mcw
		Calendar c = Calendar.getInstance();// 使用默认时区和语言环境获得一个日历
        c.setTime(currdate); // 设置为当前时间
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1); // 设置为上一个月
        String lDate = format.format(c.getTime());
        //end  by  mcw
        
		/*Date dd = null;
		try {
			dd = format.parse(startDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}*/
		
		/*
		 * c.setTime(dd); //本月[当前时间月份] String bmon = startDate; //上月[当前时间前一个月]
		 * c.setTime(dd); c.add(Calendar.MONTH, -1); Date sm = c.getTime();
		 * String smon = format.format(sm); //历史[当前时间前两个之前] c.setTime(dd);
		 * c.add(Calendar.MONTH, -2); Date lm = c.getTime(); String lmon =
		 * format.format(lm);
		 */
/*
		// edit by zwx 查询月份=当前系统月份-1
		// 本月[当前时间月份-1]
		 Calendar c = Calendar.getInstance();// 使用默认时区和语言环境获得一个日历
        c.setTime(currdate); // 设置为当前时间
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1); // 设置为上一个月c.setTime(dd);
		c.add(Calendar.MONTH, -1);
		Date bm = c.getTime();
		String bmon = format.format(bm);
		// 上月[前一个月]
		c.setTime(dd);
		c.add(Calendar.MONTH, -2);
		Date sm = c.getTime();
		String smon = format.format(sm);
		// 历史[当前时间前两个之前]
		c.setTime(dd);
		c.add(Calendar.MONTH, -3);
		Date lm = c.getTime();
		String lmon = format.format(lm);
		// end by zwx
*/		
		/*StringBuffer sb = new StringBuffer();
		 * sb.append(" select a.custcode, ")
				.append("        a.invclasscode, ")
				.append("        a.unitcode, ")
				.append("        a.vdiliveraddress, ")
				.append("        sum(a.ls) lishi, ")
				.append("        sum(a.sy) shangyue, ")
				.append("        sum(a.byue) benyue, ")
				.append("        sum(a.byuewk) byuewk ")
				.append("   from (( select d.custcode, ")
				.append("                 g.invclasscode, ")
				.append("                 corp.unitcode, ")
				.append("                 h.vdiliveraddress, ")
				.append("                 sum(0) sy, ")
				.append("                 sum(nvl(b.noutnum, 0) - nvl(sa2.nnumber, 0)) ls, ")
				.append("                 sum(0) byue, ")
				.append("                 sum(0) byuewk ")
				.append("   from ic_general_h h ")
				.append("   left join ic_general_b b ")
				.append("     on b.cgeneralhid = h.cgeneralhid ")
				.append("   left join so_saleinvoice_b sa2 ")
				.append("     on b.cgeneralbid = sa2.cupsourcebillbodyid ")
				.append("   left join so_saleinvoice sa1 ")
				.append("     on sa1.csaleid = sa2.csaleid ")
				.append("   left join bd_cumandoc cum ")
				.append("     on h.ccustomerid = cum.pk_cumandoc ")
				.append("   left join bd_cubasdoc d ")
				.append("     on d.pk_cubasdoc = cum.pk_cubasdoc ")
				.append("   left outer join bd_invbasdoc e ")
				.append("     on e.pk_invbasdoc = b.cinvbasid ")
				.append("   left outer join bd_busitype f ")
				.append("     on f.pk_busitype = h.cbiztype ")
				.append("   left outer join bd_invcl g ")
				.append("     on e.pk_invcl = g.pk_invcl ")
				.append("   left join bd_corp corp ")
				.append("     on h.pk_corp = corp.pk_corp ")
				.append("  where f.pk_busitype = '" + cbiztype + "' ")
				.append("    and substr(h.dbilldate,0,7)<='" + lmon + "'")
				.append("    and corp.unitcode = '" + unitcode + "' ")
				.append("    and substr(g.invclasscode ,0,2) in ( '21' , '22' , '23') ")
				.append("    and nvl(sa1.dr, 0) = 0 ")
				.append("    and nvl(sa2.dr, 0) = 0 ")
				.append("    and  nvl(h.dr, 0) = 0 ")
				.append("    and nvl(b.dr, 0) = 0 ")
				.append("    and h.cbilltypecode = '4C' ")
				.append("  group by d.custcode, ")
				.append("                    g.invclasscode, ")
				.append("                    corp.unitcode, ")
				.append("                    h.vdiliveraddress) ")
				.append("  union all ")
				.append("                ( select d.custcode, ")
				.append("                 g.invclasscode, ")
				.append("                 corp.unitcode, ")
				.append("                 h.vdiliveraddress, ")
				.append("                 sum(nvl(b.noutnum, 0) - nvl(sa2.nnumber, 0)) sy, ")
				.append("                 sum(0) ls, ")
				.append("                 sum(0) byue, ")
				.append("                 sum(0) byuewk ")
				.append("   from ic_general_h h ")
				.append("   left join ic_general_b b ")
				.append("     on b.cgeneralhid = h.cgeneralhid ")
				.append("   left join so_saleinvoice_b sa2 ")
				.append("     on b.cgeneralbid = sa2.cupsourcebillbodyid ")
				.append("   left join so_saleinvoice sa1 ")
				.append("     on sa1.csaleid = sa2.csaleid ")
				.append("   left join bd_cumandoc cum ")
				.append("     on h.ccustomerid = cum.pk_cumandoc ")
				.append("   left join bd_cubasdoc d ")
				.append("     on d.pk_cubasdoc = cum.pk_cubasdoc ")
				.append("   left outer join bd_invbasdoc e ")
				.append("     on e.pk_invbasdoc = b.cinvbasid ")
				.append("   left outer join bd_busitype f ")
				.append("     on f.pk_busitype = h.cbiztype ")
				.append("   left outer join bd_invcl g ")
				.append("     on e.pk_invcl = g.pk_invcl ")
				.append("   left join bd_corp corp ")
				.append("     on h.pk_corp = corp.pk_corp ")
				.append("  where f.pk_busitype = '" + cbiztype + "' ")
				.append("    and substr(h.dbilldate,0,7)='" + smon + "'")
				.append("    and corp.unitcode = '" + unitcode + "' ")
				.append("    and substr(g.invclasscode ,0,2) in ( '21' , '22' , '23') ")
				.append("    and nvl(sa1.dr, 0) = 0 ")
				.append("    and nvl(sa2.dr, 0) = 0 ")
				.append("    and  nvl(h.dr, 0) = 0 ")
				.append("    and nvl(b.dr, 0) = 0 ")
				.append("    and h.cbilltypecode = '4C' ")
				.append("  group by d.custcode, ")
				.append("                    g.invclasscode, ")
				.append("                    corp.unitcode, ")
				.append("                    h.vdiliveraddress) ")
				.append(" union all ")
				.append("                     ( select d.custcode, ")
				.append("                 g.invclasscode, ")
				.append("                 corp.unitcode, ")
				.append("                 h.vdiliveraddress, ")
				.append("                 sum(0) sy, ")
				.append("                 sum(0) ls, ")
				.append("                 sum(sa2.nnumber) byue, ")
				.append("                 sum(nvl(b.noutnum, 0) - nvl(sa2.nnumber, 0)) byuewk ")
				.append("           from ic_general_h h ")
				.append("            left join ic_general_b b ")
				.append("              on b.cgeneralhid = h.cgeneralhid ")
				.append("            left join so_saleinvoice_b sa2 ")
				.append("              on b.cgeneralbid = sa2.cupsourcebillbodyid ")
				.append("            left join so_saleinvoice sa1 ")
				.append("              on sa1.csaleid = sa2.csaleid ")
				.append("            left join bd_cumandoc cum ")
				.append("              on h.ccustomerid = cum.pk_cumandoc ")
				.append("            left join bd_cubasdoc d ")
				.append("              on d.pk_cubasdoc = cum.pk_cubasdoc ")
				.append("            left outer join bd_invbasdoc e ")
				.append("              on e.pk_invbasdoc = b.cinvbasid ")
				.append("            left outer join bd_busitype f ")
				.append("              on f.pk_busitype = h.cbiztype ")
				.append("            left outer join bd_invcl g ")
				.append("              on e.pk_invcl = g.pk_invcl ")
				.append("            left join bd_corp corp ")
				.append("              on h.pk_corp = corp.pk_corp ")
				.append("           where f.pk_busitype = '" + cbiztype + "' ")
				.append("    			and substr(h.dbilldate,0,7)='" + bmon + "'")
				.append("             and corp.unitcode = '" + unitcode + "' ")
				.append("             and substr(g.invclasscode,0,2)in('21','22','23') ")
				.append("             and nvl(sa1.dr, 0) = 0 ")
				.append("             and nvl(sa2.dr, 0) = 0 ")
				.append("             and nvl(h.dr, 0) = 0 ")
				.append("             and nvl(b.dr, 0) = 0 ")
				.append("             and h.cbilltypecode = '4C' ")
				.append("           group by d.custcode, ")
				.append("                    g.invclasscode, ")
				.append("                    corp.unitcode, ")
				.append("                    h.vdiliveraddress))a ")
				.append(
						"  group by a.custcode, a.invclasscode, a.unitcode, a.vdiliveraddress ");*/
		
		StringBuffer sb = new StringBuffer();
/*	  sb.append(" select a.custcode, ") 
	  .append("        a.invclasscode, ") 
	  .append("        a.unitcode, ") 
	  .append("        a.vdiliveraddress, ") 
	  .append("        sum(a.ls) lishi, ") 
	  .append("        sum(a.sy) shangyue, ") 
	  .append("        sum(a.byue) benyue, ") 
	  .append("        sum(a.byuewk) byuewk ") 
	  .append("   from (( select d.custcode, ") 
	  .append("                 g.invclasscode, ") 
	  .append("                 corp.unitcode, ") 
	  .append("                 h.vdiliveraddress, ") 
	  .append("                 sum(0) sy, ") 
	  .append("                 sum(nvl(b.noutnum, 0) - nvl(sa2.nnumber, 0)) ls, ") 
	  .append("                 sum(0) byue, ") 
	  .append("                 sum(0) byuewk ") 
	  .append("   from ic_general_h h ") 
	  .append("   left join ic_general_b b ") 
	  .append("     on b.cgeneralhid = h.cgeneralhid ") 
	  .append("   left join so_saleinvoice_b sa2 ") 
	  .append("     on b.cgeneralbid = sa2.cupsourcebillbodyid ") 
	  .append("   left join so_saleinvoice sa1 ") 
	  .append("     on sa1.csaleid = sa2.csaleid ") 
	  .append("   left join bd_cumandoc cum ") 
	  .append("     on h.ccustomerid = cum.pk_cumandoc ") 
	  .append("   left join bd_cubasdoc d ") 
	  .append("     on d.pk_cubasdoc = cum.pk_cubasdoc ") 
	  .append("   left outer join bd_invbasdoc e ") 
	  .append("     on e.pk_invbasdoc = b.cinvbasid ") 
	  .append("   left outer join bd_busitype f ") 
	  .append("     on f.pk_busitype = h.cbiztype ") 
	  .append("   left outer join bd_invcl g ") 
	  .append("     on e.pk_invcl = g.pk_invcl ") 
	  .append("   left join bd_corp corp ") 
	  .append("     on h.pk_corp = corp.pk_corp ") 
	  .append("  where f.pk_busitype = '"+cbiztype+"' ") 
	  .append("    and extract(year from(to_date(h.dbilldate, 'YYYY-MM-DD'))) = '"+sdate+"' ")
	  .append("    and extract(month from(to_date(h.dbilldate, 'YYYY-MM-DD'))) <= '"+lishi+"' ") 
	  .append("    and corp.unitcode = '" + unitcode + "' ") 
	  .append("    and substr(g.invclasscode ,0,2) in ( '21' , '22' , '23') ") 
	  .append("    and nvl(sa1.dr, 0) = 0 ") 
	  .append("    and nvl(sa2.dr, 0) = 0 ") 
	  .append("    and  nvl(h.dr, 0) = 0 ") 
	  .append("    and nvl(b.dr, 0) = 0 ") 
	  .append("    and h.cbilltypecode = '4C' ") 
	  .append("  group by d.custcode, ") 
	  .append("                    g.invclasscode, ") 
	  .append("                    corp.unitcode, ") 
	  .append("                    h.vdiliveraddress) ") 
	  .append("  union all ") 
	  .append("                ( select d.custcode, ") 
	  .append("                 g.invclasscode, ") 
	  .append("                 corp.unitcode, ") 
	  .append("                 h.vdiliveraddress, ") 
	  .append("                 sum(nvl(b.noutnum, 0) - nvl(sa2.nnumber, 0)) sy, ") 
	  .append("                 sum(0) ls, ") 
	  .append("                 sum(0) byue, ") 
	  .append("                 sum(0) byuewk ") 
	  .append("   from ic_general_h h ") 
	  .append("   left join ic_general_b b ") 
	  .append("     on b.cgeneralhid = h.cgeneralhid ") 
	  .append("   left join so_saleinvoice_b sa2 ") 
	  .append("     on b.cgeneralbid = sa2.cupsourcebillbodyid ") 
	  .append("   left join so_saleinvoice sa1 ") 
	  .append("     on sa1.csaleid = sa2.csaleid ") 
	  .append("   left join bd_cumandoc cum ") 
	  .append("     on h.ccustomerid = cum.pk_cumandoc ") 
	  .append("   left join bd_cubasdoc d ") 
	  .append("     on d.pk_cubasdoc = cum.pk_cubasdoc ") 
	  .append("   left outer join bd_invbasdoc e ") 
	  .append("     on e.pk_invbasdoc = b.cinvbasid ") 
	  .append("   left outer join bd_busitype f ") 
	  .append("     on f.pk_busitype = h.cbiztype ") 
	  .append("   left outer join bd_invcl g ") 
	  .append("     on e.pk_invcl = g.pk_invcl ") 
	  .append("   left join bd_corp corp ") 
	  .append("     on h.pk_corp = corp.pk_corp ") 
	  .append("  where f.pk_busitype = '"+cbiztype+"' ") 
	  .append("    and extract(year from(to_date(h.dbilldate, 'YYYY-MM-DD'))) = '"+sdate+"' ") 
	  .append("    and extract(month from(to_date(h.dbilldate, 'YYYY-MM-DD'))) = '"+shangyue+"' ") 
	  .append("    and corp.unitcode = '" + unitcode + "' ") 
	  .append("    and substr(g.invclasscode ,0,2) in ( '21' , '22' , '23') ") 
	  .append("    and nvl(sa1.dr, 0) = 0 ") 
	  .append("    and nvl(sa2.dr, 0) = 0 ") 
	  .append("    and  nvl(h.dr, 0) = 0 ") 
	  .append("    and nvl(b.dr, 0) = 0 ") 
	  .append("    and h.cbilltypecode = '4C' ") 
	  .append("  group by d.custcode, ") 
	  .append("                    g.invclasscode, ") 
	  .append("                    corp.unitcode, ") 
	  .append("                    h.vdiliveraddress) ") 
	  .append(" union all ") 
	  .append("                     ( select d.custcode, ") 
	  .append("                 g.invclasscode, ") 
	  .append("                 corp.unitcode, ") 
	  .append("                 h.vdiliveraddress, ") 
	  .append("                 sum(0) sy, ") 
	  .append("                 sum(0) ls, ") 
	  .append("                 sum(sa2.nnumber) byue, ") 
	  .append("                 sum(nvl(b.noutnum, 0) - nvl(sa2.nnumber, 0)) byuewk ") 
	  .append("           from ic_general_h h ") 
	  .append("            left join ic_general_b b ") 
	  .append("              on b.cgeneralhid = h.cgeneralhid ") 
	  .append("            left join so_saleinvoice_b sa2 ") 
	  .append("              on b.cgeneralbid = sa2.cupsourcebillbodyid ") 
	  .append("            left join so_saleinvoice sa1 ") 
	  .append("              on sa1.csaleid = sa2.csaleid ") 
	  .append("            left join bd_cumandoc cum ") 
	  .append("              on h.ccustomerid = cum.pk_cumandoc ") 
	  .append("            left join bd_cubasdoc d ") 
	  .append("              on d.pk_cubasdoc = cum.pk_cubasdoc ") 
	  .append("            left outer join bd_invbasdoc e ") 
	  .append("              on e.pk_invbasdoc = b.cinvbasid ") 
	  .append("            left outer join bd_busitype f ") 
	  .append("              on f.pk_busitype = h.cbiztype ") 
	  .append("            left outer join bd_invcl g ") 
	  .append("              on e.pk_invcl = g.pk_invcl ") 
	  .append("            left join bd_corp corp ") 
	  .append("              on h.pk_corp = corp.pk_corp ") 
	  .append("           where f.pk_busitype = '"+cbiztype+"' ") 
	  .append("             and extract(year from(to_date(h.dbilldate, 'YYYY-MM-DD'))) =  '"+sdate+"' ") 
	  .append("             and extract(month from(to_date(h.dbilldate, 'YYYY-MM-DD'))) = '"+benyue+"' ") 
	  .append("             and corp.unitcode = '" + unitcode + "' ") 
	  .append("             and substr(g.invclasscode,0,2)in('21','22','23') ") 
	  .append("             and nvl(sa1.dr, 0) = 0 ") 
	  .append("             and nvl(sa2.dr, 0) = 0 ") 
	  .append("             and nvl(h.dr, 0) = 0 ") 
	  .append("             and nvl(b.dr, 0) = 0 ") 
	  .append("             and h.cbilltypecode = '4C' ") 
	  .append("           group by d.custcode, ") 
	  .append("                    g.invclasscode, ") 
	  .append("                    corp.unitcode, ") 
	  .append("                    h.vdiliveraddress))a ") 
	  .append("  group by a.custcode, a.invclasscode, a.unitcode, a.vdiliveraddress ");*/
		sb.append(" select DD.custcode, ")
		.append("        DD.invclasscode, ")
		.append("        DD.unitcode, ")
		.append("        DD.vdiliveraddress, ")
		.append("        SUM(DD.ls) lishi, ")
		.append("        SUM(DD.sy) shangyue, ")
		.append("        SUM(DD.byuewk) byuewk, ")
		.append("        SUM(DD.byue) benyue ")
		.append("   FROM ((select CC.custcode, ")
		.append("                 CC.invclasscode, ")
		.append("                 CC.unitcode, ")
		.append("                 CC.vdiliveraddress, ")
		.append("                 sum(CC.ls) ls, ")
		.append("                 sum(0) sy, ")
		.append("                 sum(0) byuewk, ")
		.append("                 SUM(0) byue ")
		.append("            from (select AA.cgeneralbid, ")
		.append("                         AA.custcode, ")
		.append("                         AA.invclasscode, ")
		.append("                         AA.unitcode, ")
		.append("                         AA.vdiliveraddress, ")
		.append("                         nvl(AA.noutnum, 0) - SUM(nvl(BB.nnumber, 0)) ls, ")
		.append("                         sum(0) sy, ")
		.append("                         sum(0) byuewk, ")
		.append("                         SUM(0) byue ")
		.append("                    from (select b.cgeneralbid, ")
		.append("                                 b.noutnum, ")
		.append("                                 d.custcode, ")
		.append("                                 g.invclasscode, ")
		.append("                                 corp.unitcode, ")
		.append("                                 h.vdiliveraddress ")
		.append("                            from ic_general_b b ")
		.append("                            left join ic_general_h h ")
		.append("                              on b.cgeneralhid = h.cgeneralhid ")
		.append("                            left join bd_cumandoc cum ")
		.append("                              on h.ccustomerid = cum.pk_cumandoc ")
		.append("                            left join bd_cubasdoc d ")
		.append("                              on d.pk_cubasdoc = cum.pk_cubasdoc ")
		.append("                            left outer join bd_invbasdoc e ")
		.append("                              on e.pk_invbasdoc = b.cinvbasid ")
		.append("                            left outer join bd_busitype f ")
		.append("                              on f.pk_busitype = h.cbiztype ")
		.append("                            left outer join bd_invcl g ")
		.append("                              on e.pk_invcl = g.pk_invcl ")
		.append("                            left join bd_corp corp ")
		.append("                              on h.pk_corp = corp.pk_corp ")
		.append("                           where f.pk_busitype = '"+cbiztype+"' ")
		.append("                             and extract(year from(to_date(h.dbilldate, 'YYYY-MM-DD'))) ='"+sdate+"' ")
		.append("                             and extract(month from(to_date(h.dbilldate, 'YYYY-MM-DD'))) < = '"+lishi+"' ")
		.append("                             and corp.unitcode = '" + unitcode + "' ")
		.append("                             and substr(g.invclasscode, 0, 2) in ")
		.append("                                 ('21', '22', '23') ")
		.append("                             and nvl(h.dr, 0) = 0 ")
		.append("                             and nvl(b.dr, 0) = 0 ")
		.append("                             and h.cbilltypecode = '4C' ")
		.append("                           group by b.cgeneralbid, ")
		.append("                                    b.noutnum, ")
		.append("                                    d.custcode, ")
		.append("                                    g.invclasscode, ")
		.append("                                    corp.unitcode, ")
		.append("                                    h.vdiliveraddress) AA ")
		.append("                    left join (select sa2.nnumber, sa2.cupsourcebillbodyid ")
		.append("                                from so_saleinvoice_b sa2 ")
		.append("                                left join so_saleinvoice sa1 ")
		.append("                                  on sa1.csaleid = sa2.csaleid ")
		.append("                                left join bd_corp corp ")
		.append("                                  on sa1.pk_corp = corp.pk_corp ")
		.append("                               where nvl(sa1.dr, 0) = 0 ")
		.append("                                 and nvl(sa2.dr, 0) = 0 ")
		.append("                                 and corp.unitcode = '" + unitcode + "') BB ")
		.append("                      on AA.cgeneralbid = BB.cupsourcebillbodyid ")
		.append("                   group by AA.cgeneralbid, ")
		.append("                            AA.noutnum, ")
		.append("                            AA.custcode, ")
		.append("                            AA.invclasscode, ")
		.append("                            AA.unitcode, ")
		.append("                            AA.vdiliveraddress) CC ")
		.append("           GROUP BY CC.custcode, ")
		.append("                    CC.invclasscode, ")
		.append("                    CC.unitcode, ")
		.append("                    CC.vdiliveraddress) union all ")
		.append("         (select CC.custcode, ")
		.append("                 CC.invclasscode, ")
		.append("                 CC.unitcode, ")
		.append("                 CC.vdiliveraddress, ")
		.append("                 sum(0) ls, ")
		.append("                 sum(CC.sy) sy, ")
		.append("                 sum(0) byuewk, ")
		.append("                 SUM(0) byue ")
		.append("            from (select AA.cgeneralbid, ")
		.append("                         AA.custcode, ")
		.append("                         AA.invclasscode, ")
		.append("                         AA.unitcode, ")
		.append("                         AA.vdiliveraddress, ")
		.append("                         sum(0) ls, ")
		.append("                         nvl(AA.noutnum, 0) - SUM(nvl(BB.nnumber, 0)) sy, ")
		.append("                         sum(0) byuewk, ")
		.append("                         SUM(0) byue ")
		.append("                    from (select b.cgeneralbid, ")
		.append("                                 b.noutnum, ")
		.append("                                 d.custcode, ")
		.append("                                 g.invclasscode, ")
		.append("                                 corp.unitcode, ")
		.append("                                 h.vdiliveraddress ")
		.append("                            from ic_general_b b ")
		.append("                            left join ic_general_h h ")
		.append("                              on b.cgeneralhid = h.cgeneralhid ")
		.append("                            left join bd_cumandoc cum ")
		.append("                              on h.ccustomerid = cum.pk_cumandoc ")
		.append("                            left join bd_cubasdoc d ")
		.append("                              on d.pk_cubasdoc = cum.pk_cubasdoc ")
		.append("                            left outer join bd_invbasdoc e ")
		.append("                              on e.pk_invbasdoc = b.cinvbasid ")
		.append("                            left outer join bd_busitype f ")
		.append("                              on f.pk_busitype = h.cbiztype ")
		.append("                            left outer join bd_invcl g ")
		.append("                              on e.pk_invcl = g.pk_invcl ")
		.append("                            left join bd_corp corp ")
		.append("                              on h.pk_corp = corp.pk_corp ")
		.append("                           where f.pk_busitype = '"+cbiztype+"' ")
		.append("                             and extract(year from(to_date(h.dbilldate, 'YYYY-MM-DD'))) ='"+sdate+"' ")
		.append("                             and extract(month from(to_date(h.dbilldate, 'YYYY-MM-DD'))) ='"+shangyue+"' ")
		.append("                             and corp.unitcode = '" + unitcode + "' ")
		.append("                             and substr(g.invclasscode, 0, 2) in ")
		.append("                                 ('21', '22', '23') ")
		.append("                             and nvl(h.dr, 0) = 0 ")
		.append("                             and nvl(b.dr, 0) = 0 ")
		.append("                             and h.cbilltypecode = '4C' ")
		.append("                           group by b.cgeneralbid, ")
		.append("                                    b.noutnum, ")
		.append("                                    d.custcode, ")
		.append("                                    g.invclasscode, ")
		.append("                                    corp.unitcode, ")
		.append("                                    h.vdiliveraddress) AA ")
		.append("                    left join (select sa2.nnumber, sa2.cupsourcebillbodyid ")
		.append("                                from so_saleinvoice_b sa2 ")
		.append("                                left join so_saleinvoice sa1 ")
		.append("                                  on sa1.csaleid = sa2.csaleid ")
		.append("                                left join bd_corp corp ")
		.append("                                  on sa1.pk_corp = corp.pk_corp ")
		.append("                               where nvl(sa1.dr, 0) = 0 ")
		.append("                                 and nvl(sa2.dr, 0) = 0 ")
		.append("                                 and corp.unitcode = '" + unitcode + "') BB ")
		.append("                      on AA.cgeneralbid = BB.cupsourcebillbodyid ")
		.append("                   group by AA.cgeneralbid, ")
		.append("                            AA.noutnum, ")
		.append("                            AA.custcode, ")
		.append("                            AA.invclasscode, ")
		.append("                            AA.unitcode, ")
		.append("                            AA.vdiliveraddress) CC ")
		.append("           GROUP BY CC.custcode, ")
		.append("                    CC.invclasscode, ")
		.append("                    CC.unitcode, ")
		.append("                    CC.vdiliveraddress) union all ")
		.append("         (select CC.custcode, ")
		.append("                 CC.invclasscode, ")
		.append("                 CC.unitcode, ")
		.append("                 CC.vdiliveraddress, ")
		.append("                 sum(0) ls, ")
		.append("                 sum(0) sy, ")
		.append("                 sum(CC.byuewk) byuewk, ")
		.append("                 SUM(CC.byue) byue ")
		.append("            from (select AA.cgeneralbid, ")
		.append("                         AA.custcode, ")
		.append("                         AA.invclasscode, ")
		.append("                         AA.unitcode, ")
		.append("                         AA.vdiliveraddress, ")
		.append("                         sum(0) ls, ")
		.append("                         sum(0) sy, ")
		.append("                         nvl(AA.noutnum, 0) - SUM(nvl(BB.nnumber, 0)) byuewk, ")
		.append("                         SUM(BB.nnumber) byue ")
		.append("                    from (select b.cgeneralbid, ")
		.append("                                 b.noutnum, ")
		.append("                                 d.custcode, ")
		.append("                                 g.invclasscode, ")
		.append("                                 corp.unitcode, ")
		.append("                                 h.vdiliveraddress ")
		.append("                            from ic_general_b b ")
		.append("                            left join ic_general_h h ")
		.append("                              on b.cgeneralhid = h.cgeneralhid ")
		.append("                            left join bd_cumandoc cum ")
		.append("                              on h.ccustomerid = cum.pk_cumandoc ")
		.append("                            left join bd_cubasdoc d ")
		.append("                              on d.pk_cubasdoc = cum.pk_cubasdoc ")
		.append("                            left outer join bd_invbasdoc e ")
		.append("                              on e.pk_invbasdoc = b.cinvbasid ")
		.append("                            left outer join bd_busitype f ")
		.append("                              on f.pk_busitype = h.cbiztype ")
		.append("                            left outer join bd_invcl g ")
		.append("                              on e.pk_invcl = g.pk_invcl ")
		.append("                            left join bd_corp corp ")
		.append("                              on h.pk_corp = corp.pk_corp ")
		.append("                           where f.pk_busitype = '"+cbiztype+"' ")
		.append("                             and extract(year from(to_date(h.dbilldate, 'YYYY-MM-DD'))) = '"+sdate+"' ")
		.append("                             and extract(month from(to_date(h.dbilldate, 'YYYY-MM-DD'))) = '"+benyue+"' ")
		.append("                             and corp.unitcode = '" + unitcode + "' ")
		.append("                             and substr(g.invclasscode, 0, 2) in ")
		.append("                                 ('21', '22', '23') ")
		.append("                             and nvl(h.dr, 0) = 0 ")
		.append("                             and nvl(b.dr, 0) = 0 ")
		.append("                             and h.cbilltypecode = '4C' ")
		.append("                           group by b.cgeneralbid, ")
		.append("                                    b.noutnum, ")
		.append("                                    d.custcode, ")
		.append("                                    g.invclasscode, ")
		.append("                                    corp.unitcode, ")
		.append("                                    h.vdiliveraddress) AA ")
		.append("                    left join (select sa2.nnumber, sa2.cupsourcebillbodyid ")
		.append("                                from so_saleinvoice_b sa2 ")
		.append("                                left join so_saleinvoice sa1 ")
		.append("                                  on sa1.csaleid = sa2.csaleid ")
		.append("                                left join bd_corp corp ")
		.append("                                  on sa1.pk_corp = corp.pk_corp ")
		.append("                               where nvl(sa1.dr, 0) = 0 ")
		.append("                                 and nvl(sa2.dr, 0) = 0 ")
		.append("                                 and corp.unitcode = '" + unitcode + "') BB ")
		.append("                      on AA.cgeneralbid = BB.cupsourcebillbodyid ")
		.append("                   group by AA.cgeneralbid, ")
		.append("                            AA.noutnum, ")
		.append("                            AA.custcode, ")
		.append("                            AA.invclasscode, ")
		.append("                            AA.unitcode, ")
		.append("                            AA.vdiliveraddress) CC ")
		.append("           GROUP BY CC.custcode, ")
		.append("                    CC.invclasscode, ")
		.append("                    CC.unitcode, ")
		.append("                    CC.vdiliveraddress)) DD ")
		.append("  group by DD.custcode, DD.invclasscode, DD.unitcode, DD.vdiliveraddress ");
		List ListData = new ArrayList();
		try {
			ListData = (List) bs.executeQuery(sb.toString(),new MapListProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ListData.size() > 0) {
			HashMap map = null;
			// 存放发出商品数据
			HashMap<String, Object> NumMap = new HashMap<String, Object>();
			List<SaleSendCvmVO> voList = new ArrayList<SaleSendCvmVO>();
			for (int k = 0; k < ListData.size(); k++) {
				// 本月开票
				UFDouble sumNowA = new UFDouble(0);
				// 上月未开票
				UFDouble sumLastA = new UFDouble(0);
				// 历史未开票
				UFDouble sumHistoryA = new UFDouble(0);

				map = (HashMap) ListData.get(k);

				// 上月未对账
				String bywdznum = map.get("shangyue") == null ? "0" : map.get(
						"shangyue").toString();
				// 历史未对账
				String lswdznum = map.get("lishi") == null ? "0" : map.get(
						"lishi").toString();
				// 本月开票
				String bykpnum = map.get("benyue") == null ? "0" : map.get(
						"benyue").toString();
				// 本月未开票
				String byuewk = map.get("byuewk") == null ? "0" : map.get(
						"byuewk").toString();
				if (bywdznum.equals("0") && lswdznum.equals("0")
						&& bykpnum.equals("0") && byuewk.equals("0")) {
					continue;
				}
				SaleSendCvmVO svo = new SaleSendCvmVO();
				String AddressName = map.get("vdiliveraddress") == null ? ""
						: map.get("vdiliveraddress").toString();
				String addrName = AddressName.replace(" ", "");
				svo.setCustcode(map.get("custcode") == null ? "" : map.get(
						"custcode").toString());
				svo.setUnitcode(map.get("unitcode") == null ? "" : map.get(
						"unitcode").toString());
				svo.setChFl(map.get("invclasscode") == null ? "" : map.get(
						"invclasscode").toString());
				svo.setVdiliveraddress(QueryAddress(addrName));
				// svo.setJsDate(startDate);// edit by zwx 2019-3-20 日期改为当前时间-1
				svo.setJsDate(lDate);//edit by mcw 
				svo.setBykp(bykpnum);
				svo.setBywkp(byuewk);
				svo.setLswdz(lswdznum);
				svo.setSywdz(bywdznum);
				svo.setDr(0);
				voList.add(svo);
			}
			if (voList.size() > 0) {
				try {
					new BaseDAO().insertVOList(voList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 查询客商收货地址档案主键 2018年3月26日14:22:24
	 * 
	 * @param AddressName
	 *            地址名称
	 * @return
	 * @throws DAOException
	 */
	private static String QueryAddress(String AddressName) throws DAOException {
		BaseDAO dao = new BaseDAO();
		StringBuffer sb = new StringBuffer();
		sb.append(" select addr.pk_address ")
				.append("   from bd_address addr ").append(
						"  where addr.addrname = '" + AddressName + "' ")
				.append("    and nvl(addr.dr,0)=0 and pk_corp ='0001'");
		List list = null;
		Map map = new HashMap();
		String pk_addr = null;
		list = (List) dao.executeQuery(sb.toString(), new MapListProcessor());
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				map = (Map) list.get(0);
				pk_addr = (String) map.get("pk_address");
			}
		} else {
			return null;
		}
		return pk_addr;
	}
	
	/**
	 * Edit by mcw on 2019/7/1.
	 */
/*	public static class Log_Exception {
	    *//**
	     * @将错误信息输入到txt中
	     * @param path
	     * @throws IOException
	     *//*
	    public static void writeEror_to_txt(String path,String content) throws IOException{
	 
	        File F=new File(path);
	        //如果文件不存在,就动态创建文件
	        if(!F.exists()){
	            F.createNewFile();
	        }
	        FileWriter fw=null;
	        String writeDate="时间:"+new Date()+"---"+":"+content;
	        try {
	            //设置为:True,表示写入的时候追加数据
	            fw=new FileWriter(F, true);
	            //回车并换行
	            fw.write(writeDate+"\r\n");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally{
	            if(fw!=null){
	                fw.close();
	            }
	        }
	 
	    }
	
	    
	  
	    *//**
	     * @获取系统当前时间
	     * @return
	     *//*
	    public  String get_nowDate(){
	 
	        Calendar D=Calendar.getInstance();
	        int year=0;
	        int moth=0;
	        int day=0;
	        year=D.get(Calendar.YEAR);
	        moth=D.get(Calendar.MONTH)+1;
	        day=D.get(Calendar.DAY_OF_MONTH);
	        String now_date=String.valueOf(year)+"-"+String.valueOf(moth)+"-"+String.valueOf(day);
	        return now_date;
	    }
	    String path="E:/filezl.txt";
	    String content = null;
	    Log_Exception le=new Log_Exception();
        le.writeEror_to_txt(path, content);
	}
  */
	
}
