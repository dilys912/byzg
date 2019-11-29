package nc.bs.baoyin.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import sun.security.action.GetLongAction;

/**
 * @author Administrator
 * @date Apr 5, 2014 10:59:08 AM
 * @type nc.bs.baoyin.alert.XyyArAlert
 * @corporation 上海锐鸟软件有限公司
 * @website www.rino123.com
 * @mail zap_168@163.com
 */
public class XyyArAlert implements IBusinessPlugin {

	@SuppressWarnings("unchecked")
	public String implementReturnMessage(Key[] arg0, String arg1, UFDate arg2) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			String msgRole = "msgRole";
			// 获取未核销的所有应付单 
			StringBuilder sql = new StringBuilder();
			sql.append("select custname,sum(mny) as mny,fpdate,days,zrmobile");
			sql.append(" from(");
			sql.append(" select A.fb_oid,F.custname,A.jfbbje as mny,D.dbilldate as fpdate,");
			sql.append(" nvl(G.dayafterfp,0) as days,decode(H.ct_manageid,null,N.mobile,L.mobile) as zrmobile");
			sql.append(" from");
			sql.append(" arap_djfb A,arap_djzb B,so_saleinvoice_b C,so_saleinvoice D,bd_cumandoc E,bd_cubasdoc F,bd_payterm G,");
			sql.append(" so_saleorder_b H,so_sale I,ct_manage J,sm_userandclerk K,bd_psnbasdoc L,sm_userandclerk M,bd_psnbasdoc N");
			sql.append(" where nvl(A.dr,0)=0");
			sql.append(" and nvl(B.dr,0)=0");
			sql.append(" and nvl(C.dr,0)=0");
			sql.append(" and nvl(D.dr,0)=0");
			sql.append(" and nvl(E.dr,0)=0");
			sql.append(" and nvl(F.dr,0)=0");
			sql.append(" and nvl(G.dr,0)=0");
			sql.append(" and A.vouchid=B.vouchid");
			sql.append(" and A.fphid=C.cinvoice_bid(+)");
			sql.append(" and C.csaleid=D.csaleid");
			sql.append(" and A.ordercusmandoc=E.pk_cumandoc");
			sql.append(" and E.pk_cubasdoc=F.pk_cubasdoc");
			sql.append(" and A.sfkxyh=G.pk_payterm");
			sql.append(" and A.ddhid=H.corder_bid");
			sql.append(" and H.csaleid=I.csaleid");
			sql.append(" and H.ct_manageid=J.pk_ct_manage(+)");
			sql.append(" and J.operid=K.userid(+)");
			sql.append(" and K.pk_psndoc=L.pk_psnbasdoc(+)");
			sql.append(" and I.coperatorid=M.userid(+)");
			sql.append(" and M.pk_psndoc=N.pk_psnbasdoc(+)");
			sql.append(" and B.dwbm='1016'");
			sql.append(" and nvl(A.isverifyfinished,'N')='N'");
			sql.append(" and B.djrq>='2013-11-10'");
			sql.append(" )");
			sql.append(" group by");
			sql.append(" custname,fpdate,days,zrmobile");
			sql.append(" order by custname,fpdate,days");
			List<Map<String, Object>> data = (List<Map<String, Object>>) dao.executeQuery(sql.toString(), new MapListProcessor());
			if (data == null || data.size() <= 0) return null;
			List<String[]> zrrMsg = new ArrayList<String[]>();
			StringBuilder roleMsg = new StringBuilder();
			UFDate currDate = arg2;//取系统登录日期，非当前日期
			int year = currDate.getYear();
			int month = currDate.getMonth();
			int dy = currDate.getDay();
			String newday = year+"年"+month+"月"+dy+"日,";
			String sendMsg = null;
			for (Map<String, Object> map : data) {
				String custname = map.get("custname").toString();//客户
				String mny = map.get("mny").toString();//金额
				UFDate billdate = new UFDate(map.get("fpdate").toString());//开票日期
				int day = Integer.parseInt(map.get("days").toString());//收付款协议天数
				String zrmobile = (String) map.get("zrmobile");//手机号
				UFDate tarDate = billdate.getDateAfter(day);
				//part1:系统登录日期-开票日期 = 5/
				/**
				 *  系统中有一张客户A的100元的销售发票,[开票日期]为2014/9/10,对应的销售订单上的收款协议(基本档案->结算信息->收付款协议)
				 *  的[收到发票后付款天数]为30,
				 *	则预警在10月5号发送短信为:
				 *	截止到2014年10月05日,A在5天后将发生逾期账款100.00元;已逾期账款总额0元
				 */
				if(currDate.after(billdate) && UFDate.getDaysBetween(tarDate, currDate) == 5 && !StringUtils.isEmpty(zrmobile)){
					sendMsg = "截止到"+newday+custname+"在5天后将发生逾期账款:"+mny+"元.已逾期账款总额0元\r\n";
					zrrMsg.add(new String[] {
							zrmobile, sendMsg
					});
				}
				//part2:系统登录日期-开票日期 = 0/
				/**
				 * 预警在10月10号发送短信为:
				 *	截止到2014年10月10日,A在今天发生逾期账款100.00元;已逾期账款总额0元
				 */
				else if(currDate.equals(billdate) && UFDate.getDaysBetween(tarDate, currDate) == 0 && !StringUtils.isEmpty(zrmobile)){
					sendMsg = "截止到"+newday+custname+"在今天发生逾期账款:0元.已逾期账款总额"+mny+"元\r\n";
					zrrMsg.add(new String[] {
							zrmobile, sendMsg
					});
				}
				//part3:系统登录日期-收付款协议截止日期/7=整数
				/**
				 * 如果预警触发日期减去2014/10/10的天数 除以 7,为整数, 则发送短信为:
				 *	截止到<预警触发日期>,A在今天发生逾期账款0元;已逾期账款总额100.00元
				 */
				else if(currDate.after(tarDate) && UFDate.getDaysBetween(tarDate,currDate)%7 == 0 && !StringUtils.isEmpty(zrmobile)){
					sendMsg = "截止到"+newday+custname+"在今天发生逾期账款:0元.已逾期账款总额"+mny+"元\r\n";
					zrrMsg.add(new String[] {
							zrmobile, sendMsg
					});
				}
			}

			String msgPlatformUserNameSQL = "select docname from bd_defdoc a, bd_defdoclist b WHERE a.pk_defdoclist = b.pk_defdoclist and doclistcode = 'BZ019' and nvl(sealflag,'N')='N' and doccode ='USERNAME' ";
			String msgPlatformUserName = (String) dao.executeQuery(msgPlatformUserNameSQL, new ColumnProcessor());
			String msgPlatformUserPwdSQL = "select docname from bd_defdoc a, bd_defdoclist b WHERE a.pk_defdoclist = b.pk_defdoclist and doclistcode = 'BZ019' and nvl(sealflag,'N')='N' and doccode ='PASSWORD' ";
			String msgPlatformUserPwd = (String) dao.executeQuery(msgPlatformUserPwdSQL, new ColumnProcessor());

			// 为责任人发短信
			String BsmsAuthentication = "http://10.251.8.127/bsms/services/BsmsAuthentication?wsdl";
			String BsmsSendProxy = "http://10.251.8.127/bsms/services/BsmsSendProxy?wsdl";
			Service ser = new Service();
			Call call = (Call) ser.createCall();
			call.setTargetEndpointAddress(BsmsAuthentication);
			call.setOperation("login");
			String result = (String) call.invoke(new Object[] {
					msgPlatformUserName, msgPlatformUserPwd
			});
			call.setTargetEndpointAddress(BsmsSendProxy);
			call.setOperation("sendMessage");
			if (zrrMsg.size() > 0) {
				for (String[] ss : zrrMsg) {
					call.invoke(new Object[] {
							new String[] {
								ss[0]
							}, ss[1], new Integer(0), new Integer(0), result
					});
				}
			}
			// 为角色发短信
			String[] phones = null;
			if (roleMsg.length() > 0) {
				sql = new StringBuilder();
				sql.append("select D.mobile,D.psnname from");
				sql.append(" sm_user_role A,sm_role B,sm_userandclerk C,bd_psnbasdoc D");
				sql.append(" where nvl(A.dr,0)=0");
				sql.append(" and nvl(B.dr,0)=0");
				sql.append(" and nvl(C.dr,0)=0");
				sql.append(" and nvl(D.dr,0)=0");
				sql.append(" and A.pk_role=B.pk_role");
				sql.append(" and A.cuserid=C.userid");
				sql.append(" and C.pk_psndoc=D.pk_psnbasdoc");
				sql.append(" and D.mobile is not null");
				sql.append(" and B.role_code='" + msgRole + "'");
				data = (List<Map<String, Object>>) dao.executeQuery(sql.toString(), new MapListProcessor());
				if (data != null && data.size() > 0) {
					for (Map<String, Object> map : data) {
						if(map.get("mobile") != null && !"".equals(map.get("mobile").toString())) {
							phones = (String[]) ArrayUtils.add(phones, (String) map.get("mobile"));
						}
					}
				}
				if (phones != null && phones.length > 0) {
					call.invoke(new Object[] {
							phones, roleMsg.toString(), new Integer(0), new Integer(0), result
					});
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "收款预警";
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
