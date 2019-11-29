//package nc.bs.baoyin.alert;
//
//import java.io.File;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.io.FileUtils;
//
//import nc.bs.dao.BaseDAO;
//import nc.bs.dao.DAOException;
//import nc.bs.framework.common.RuntimeEnv;
//import nc.bs.logging.Logger;
//import nc.bs.pub.pa.IBusinessPlugin;
//import nc.bs.pub.pa.html.IAlertMessage;
//import nc.jdbc.framework.JdbcSession;
//import nc.jdbc.framework.PersistenceManager;
//import nc.jdbc.framework.exception.DbException;
//import nc.jdbc.framework.processor.ColumnProcessor;
//import nc.jdbc.framework.processor.MapListProcessor;
//import nc.vo.baoyin.alert.BdinfoVO;
//import nc.vo.baoyin.alert.CategoryVO;
//import nc.vo.baoyin.alert.CustBasVO;
//import nc.vo.baoyin.alert.DefdefVO;
//import nc.vo.baoyin.alert.DefdocVO;
//import nc.vo.baoyin.alert.DefdoclistVO;
//import nc.vo.baoyin.alert.FreevalueVO;
//import nc.vo.baoyin.alert.StordocVO;
//import nc.vo.bd.b04.DeptdocVO;
//import nc.vo.bd.b06.PsndocVO;
//import nc.vo.bd.b09.CumandocVO;
//import nc.vo.bd.b120.AccidVO;
//import nc.vo.bd.b18.VouchertypeVO;
//import nc.vo.bd.b20.CurrtypeVO;
//import nc.vo.bd.b23.AccbankVO;
//import nc.vo.bd.b38.JobbasfilVO;
//import nc.vo.bd.b52.GlbookVO;
//import nc.vo.bd.b54.GlorgVO;
//import nc.vo.bd.b54.GlorgbookVO;
//import nc.vo.bd.settle.UserVO;
//import nc.vo.pub.BusinessException;
//import nc.vo.pub.lang.UFDate;
//import nc.vo.pub.lang.UFDateTime;
//import nc.vo.pub.lang.UFDouble;
//import nc.vo.pub.pa.Key;
//import nc.vo.sm.login.NCEnv;
//
///**
// * 凭证接口-后台预警
// * 
// * @author fans
// * @date 2012-11-26
// * 
// */
//public class SynData implements IBusinessPlugin {
//
//	public int getImplmentsType() {
//		return 0;
//	}
//
//	public Key[] getKeys() {
//		return null;
//	}
//
//	public String getTypeDescription() {
//		return null;
//	}
//
//	public String getTypeName() {
//		return null;
//	}
//
//	public IAlertMessage implementReturnFormatMsg(Key[] arg0, String arg1, UFDate arg2) throws BusinessException {
//		return null;
//	}
//
//	public Object implementReturnObject(Key[] arg0, String arg1, UFDate arg2) throws BusinessException {
//		return null;
//	}
//
//	public boolean implementWriteFile(Key[] arg0, String arg1, String arg2, UFDate arg3) throws BusinessException {
//		return false;
//	}
//
//	/*
//	 * @parm key[] 预警配置参数列表
//	 * 
//	 * @parm pk_corp 预警执行的公司primaryKey
//	 * 
//	 * @parm ufdate 预警执行时间
//	 * 
//	 * @return 如有错误则返回错误信息
//	 * 
//	 * @see
//	 * nc.bs.pub.pa.IBusinessPlugin#implementReturnMessage(nc.vo.pub.pa.Key[],
//	 * java.lang.String, nc.vo.pub.lang.UFDate)
//	 */
//	public String implementReturnMessage(Key[] keys, String pk_corp, UFDate clientLoginDate) throws BusinessException {
//		StringBuffer sb = new StringBuffer();
//		try {
//			Logger.info("syndata start");
//
//			// // 获取参数设置的值,跟据设定的天数查询数据
//			// String defaultvalueSql =
//			// "select defaultvalue  from pub_sysinittemp c where c.initcode='syndays'";
//			// Object obj = null;
//			// try {
//			// obj = getBaseDAO().executeQuery(defaultvalueSql,
//			// new ColumnProcessor());
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// UFDouble days = new UFDouble("1");
//			// if (obj != null) {
//			// days = new UFDouble(String.valueOf(obj));
//			// }
//			// Calendar cal = Calendar.getInstance();
//			// cal.add(Calendar.DAY_OF_MONTH, new UFDouble("0").sub(days)
//			// .intValue());
//			// Date date = cal.getTime();
//			// SimpleDateFormat sf = new
//			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			// String formatDate = sf.format(date);
//			// sb.append("同步").append(days).append("天内的数据:<p/>");
//			//
//			// // 相同的尾查询条件
//			// String commonSql =
//			// " to_date(ts, 'yyyy-MM-dd hh24:mi:ss') >=to_date('"
//			// + formatDate + "', 'yyyy-MM-dd hh24:mi:ss')";
//			// // 1.用户
//			// // String userSql = "select * from sm_user ";
//			// Logger.info("syndata user start");
//			// Collection userCol = null;
//			// try {
//			// userCol = getSessionManager().retrieveByClause(UserVO.class,
//			// commonSql);
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// sb.append("同步用户:");
//			// if (null != userCol && userCol.size() > 0) {
//			// Iterator it = userCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// UserVO userVO = (UserVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(UserVO.class,
//			// "user_code='" + userVO.getUser_code() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// userVO.setPrimaryKey(null);
//			// String pk = getExternalDAO().insertVO(userVO);
//			// sb.append(pk).append(",");
//			// }
//			// // else {
//			// // UserVO[] users = new UserVO[tempcol.size()];
//			// // userVO.setPrimaryKey(((UserVO) tempcol.toArray(users)[0])
//			// // .getPrimaryKey());
//			// // getExternalDAO().updateVO(userVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 2.人员
//			// String psndocSql = "select * from bd_psndoc ";
//			// Logger.info("syndata bd_psndoc start");
//			// sb.append("同步人员:");
//			// Collection psndocCol = null;
//			// try {
//			// psndocCol = getSessionManager().retrieveByClause(
//			// PsndocVO.class, commonSql);
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != psndocCol && psndocCol.size() > 0) {
//			// Iterator it = psndocCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// PsndocVO psndocVO = (PsndocVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(PsndocVO.class,
//			// "psncode='" + psndocVO.getPsncode() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// psndocVO.setPrimaryKey(null);
//			// psndocVO.setPk_psnbasdoc(getExtPk_psnbasdoc(psndocVO
//			// .getPk_psnbasdoc()));
//			// psndocVO.setPk_psncl(getExtPk_psnbasdoc(psndocVO
//			// .getPk_psncl()));
//			// String pk = getExternalDAO().insertVO(psndocVO);
//			// sb.append(pk).append(",");
//			// }
//			// // else {
//			// // PsndocVO[] psndocs = new PsndocVO[tempcol.size()];
//			// // psndocVO.setPrimaryKey(((PsndocVO)
//			// // tempcol.toArray(psndocs)[0])
//			// // .getPrimaryKey());
//			// // psndocVO.setPk_psnbasdoc(getExtPk_psnbasdoc(psndocVO
//			// // .getPk_psnbasdoc()));
//			// // psndocVO.setPk_psncl(getExtPk_psncl(psndocVO.getPk_psncl()));
//			// // getExternalDAO().updateVO(psndocVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 3.部门
//			// String deptdocSql = "select * from bd_deptdoc ";
//			// Logger.info("syndata bd_deptdoc start");
//			// sb.append("同步部门:");
//			// Collection deptdocCol = null;
//			// try {
//			// deptdocCol = getSessionManager().retrieveByClause(
//			// DeptdocVO.class, commonSql);
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != deptdocCol && deptdocCol.size() > 0) {
//			// Iterator it = deptdocCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// DeptdocVO deptdocVO = (DeptdocVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(
//			// DeptdocVO.class,
//			// "deptcode='" + deptdocVO.getDeptcode() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// deptdocVO.setPrimaryKey(null);
//			// deptdocVO.setPk_calbody(getExtPk_calbody(deptdocVO
//			// .getPk_calbody()));
//			// deptdocVO.setPk_deptdoc(getExtPk_deptdoc(deptdocVO
//			// .getPk_deptdoc()));
//			// deptdocVO.setPk_fathedept(getExtPk_deptdoc(deptdocVO
//			// .getPk_fathedept()));
//			// deptdocVO.setPk_psndoc(getExtUser(deptdocVO
//			// .getPk_psndoc()));
//			// String pk = getExternalDAO().insertVO(deptdocVO);
//			// sb.append(pk).append(",");
//			// }
//			// // else {
//			// // DeptdocVO[] deptdocs = new DeptdocVO[tempcol.size()];
//			// // deptdocVO.setPrimaryKey(((DeptdocVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// //
//			// deptdocVO.setPk_calbody(getExtPk_calbody(deptdocVO.getPk_calbody()));
//			// //
//			// deptdocVO.setPk_deptdoc(getExtPk_deptdoc(deptdocVO.getPk_deptdoc()));
//			// //
//			// deptdocVO.setPk_fathedept(getExtPk_deptdoc(deptdocVO.getPk_fathedept()));
//			// // deptdocVO.setPk_psndoc(getExtUser(deptdocVO.getPk_psndoc()));
//			// // getExternalDAO().updateVO(deptdocVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 4.账户档案
//			// String accidSql = "select * from bd_accid ";
//			// Logger.info("syndata bd_accid start");
//			// sb.append("同步账户档案:");
//			// Collection accidCol = null;
//			// try {
//			// accidCol = getSessionManager().retrieveByClause(AccidVO.class,
//			// commonSql);
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != accidCol && accidCol.size() > 0) {
//			// Iterator it = accidCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// AccidVO accidVO = (AccidVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(AccidVO.class,
//			// "accidcode='" + accidVO.getAccidcode() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// accidVO.setPrimaryKey(null);
//			// accidVO.setPk_accbank(getExtPk_accbank(accidVO
//			// .getPk_accbank()));
//			// accidVO.setPk_currtype(getExtpk_currtype(accidVO
//			// .getPk_currtype()));
//			// accidVO.setPk_settlecent(getExtPk_settlecent(accidVO
//			// .getPk_settlecent()));
//			// accidVO
//			// .setPk_fim(getExtPk_cubasdoc(accidVO
//			// .getPk_fim()));
//			// // accidVO.setPk_accidstlcent(getExt)
//			// String pk = getExternalDAO().insertVO(accidVO);
//			// sb.append(pk).append(";");
//			// }
//			// // else {
//			// // AccidVO[] deptdocs = new AccidVO[tempcol.size()];
//			// // accidVO.setPrimaryKey(((AccidVO)
//			// // tempcol.toArray(deptdocs)[0])
//			// // .getPrimaryKey());
//			// //
//			// accidVO.setPk_accbank(getExtPk_accbank(accidVO.getPk_accbank()));
//			// //
//			// accidVO.setPk_currtype(getExtpk_currtype(accidVO.getPk_currtype()));
//			// //
//			// accidVO.setPk_settlecent(getExtPk_settlecent(accidVO.getPk_settlecent()));
//			// // accidVO.setPk_fim(getExtPk_cubasdoc(accidVO.getPk_fim()));
//			// // getExternalDAO().updateVO(accidVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 5.项目档案
//			// String jobbasfilSql = "select * from bd_jobbasfil ";
//			// Logger.info("syndata bd_jobbasfil start");
//			// sb.append("同步项目档案:");
//			// Collection jobbasfilCol = null;
//			// try {
//			// jobbasfilCol = getSessionManager().retrieveByClause(
//			// JobbasfilVO.class, commonSql);
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != jobbasfilCol && jobbasfilCol.size() > 0) {
//			// Iterator it = jobbasfilCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// JobbasfilVO jobbasfilVO = (JobbasfilVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(
//			// JobbasfilVO.class,
//			// "jobcode='" + jobbasfilVO.getJobcode() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// jobbasfilVO.setPrimaryKey(null);
//			// jobbasfilVO.setPk_jobtype(getExtPk_jobtype(jobbasfilVO
//			// .getPk_jobtype()));
//			// String pk = getExternalDAO().insertVO(jobbasfilVO);
//			// sb.append(pk).append(";");
//			// }
//			// // else {
//			// // JobbasfilVO[] deptdocs = new JobbasfilVO[tempcol.size()];
//			// // jobbasfilVO.setPrimaryKey(((JobbasfilVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// //
//			// jobbasfilVO.setPk_jobtype(getExtPk_jobtype(jobbasfilVO.getPk_jobtype()));
//			// // getExternalDAO().updateVO(jobbasfilVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 6.自定义档案
//			// String defdoclistSql = "select * from bd_defdoclist ";
//			// Logger.info("syndata bd_defdoclist start");
//			// sb.append("同步自定义档案:");
//			// Collection defdoclistCol = null;
//			// try {
//			// defdoclistCol = getSessionManager().retrieveByClause(
//			// DefdoclistVO.class, commonSql);
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != defdoclistCol && defdoclistCol.size() > 0) {
//			// Iterator it = defdoclistCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// DefdoclistVO defdoclistVO = (DefdoclistVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(
//			// DefdoclistVO.class,
//			// "doclistname='" + defdoclistVO.getDoclistname()
//			// + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// defdoclistVO.setPrimaryKey(null);
//			// defdoclistVO.setDocislevflag(defdoclistVO
//			// .getDocislevflag() == null ? new UFDouble("0")
//			// : defdoclistVO.getDocislevflag());
//			// String pk = getExternalDAO().insertVO(defdoclistVO);
//			// sb.append(pk).append(";");
//			// }
//			// // else {
//			// // DefdoclistVO[] deptdocs = new
//			// // DefdoclistVO[tempcol.size()];
//			// // defdoclistVO.setPrimaryKey(((DefdoclistVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// // getExternalDAO().updateVO(defdoclistVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 7.自定义项档案表
//			// String defdocSql = "select c.* from bd_defdoc c ";
//			// Logger.info("syndata bd_defdoc start");
//			// sb.append("同步自定义项档案表:");
//			// Collection defdocCol = null;
//			// try {
//			// defdocCol = getSessionManager().retrieveByClause(
//			// DefdocVO.class, commonSql);
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != defdocCol && defdocCol.size() > 0) {
//			// Iterator it = defdocCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// DefdocVO defdocVO = (DefdocVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(DefdocVO.class,
//			// "doccode='" + defdocVO.getDoccode() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// defdocVO.setPrimaryKey(null);
//			// defdocVO.setPk_defdoclist(getExtPk_defdoclist(defdocVO
//			// .getPk_defdoclist()));
//			// //
//			// psndocVO.setPk_psnbasdoc(getExtPk_psnbasdoc(psndocVO.getPk_psnbasdoc()));
//			// // psndocVO.setPk_psncl(getExtPk_psncl(psndocVO.getPk_psncl()));
//			// try {
//			// String pk = getExternalDAO().insertVO(defdocVO);
//			// sb.append(pk).append(";");
//			// } catch (Exception e) {
//			// e.printStackTrace();
//			// }
//			// }
//			// // else {
//			// // DefdocVO[] deptdocs = new DefdocVO[tempcol.size()];
//			// // defdocVO.setPrimaryKey(((DefdocVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// // //
//			// //
//			// psndocVO.setPk_psnbasdoc(getExtPk_psnbasdoc(psndocVO.getPk_psnbasdoc()));
//			// // //
//			// // psndocVO.setPk_psncl(getExtPk_psncl(psndocVO.getPk_psncl()));
//			// // getExternalDAO().updateVO(defdocVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 8.自定义项定义
//			// String defdefSql = " select * from bd_defdef ";
//			// Logger.info("syndata bd_defdef start");
//			// sb.append("同步自定义项定义:");
//			// Collection defdefCol = null;
//			// try {
//			// defdefCol = getSessionManager().retrieveByClause(
//			// DefdefVO.class, commonSql);
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != defdefCol && defdefCol.size() > 0) {
//			// Iterator it = defdefCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// DefdefVO defdefVO = (DefdefVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(DefdefVO.class,
//			// "defcode='" + defdefVO.getDefcode() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// defdefVO.setPrimaryKey(null);
//			// defdefVO.setPk_bdinfo(getExtPk_bdinfo(defdefVO
//			// .getPk_bdinfo()));
//			// defdefVO.setPk_defdoclist(getExtPk_defdoclist(defdefVO
//			// .getPk_defdoclist()));
//			// String pk = getExternalDAO().insertVO(defdefVO);
//			// sb.append(pk).append(";");
//			// }
//			// // else {
//			// // DefdefVO[] deptdocs = new DefdefVO[tempcol.size()];
//			// // defdefVO.setPrimaryKey(((DefdefVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// //
//			// defdefVO.setPk_bdinfo(getExtPk_bdinfo(defdefVO.getPk_bdinfo()));
//			// //
//			// defdefVO.setPk_defdoclist(getExtPk_defdoclist(defdefVO.getPk_defdoclist()));
//			// // getExternalDAO().updateVO(defdefVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 9.客商档案
//			// String cubasdocSql = " select * from bd_cubasdoc ";
//			// Logger.info("syndata bd_cubasdoc start");
//			// sb.append("同步客商档案:");
//			// Collection cubasdocCol = null;
//			// try {
//			// cubasdocCol = getSessionManager().retrieveByClause(
//			// CustBasVO.class, commonSql + " and pk_corp='1016'");
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != cubasdocCol && cubasdocCol.size() > 0) {
//			// Iterator it = cubasdocCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// CustBasVO custBasVO = (CustBasVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(
//			// CustBasVO.class,
//			// "custcode='" + custBasVO.getCustcode() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// custBasVO.setPrimaryKey(null);
//			// custBasVO.setPk_areacl(getExtPk_areacl(custBasVO
//			// .getPk_areacl()));
//			// String pk = getExternalDAO().insertVO(custBasVO);
//			// sb.append(pk).append(";");
//			// }
//			// // else {
//			// // CustBasVO[] deptdocs = new CustBasVO[tempcol.size()];
//			// // custBasVO.setPrimaryKey(((CustBasVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// //
//			// custBasVO.setPk_areacl(getExtPk_areacl(custBasVO.getPk_areacl()));
//			// // getExternalDAO().updateVO(custBasVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 10.客商管理档案
//			// String cumandocSql =
//			// " select * from bd_cumandoc where pk_corp='1016' ";
//			// Logger.info("syndata bd_cumandoc start");
//			// sb.append("同步客商管理档案:");
//			// Collection cumandocCol = null;
//			// try {
//			// cumandocCol = getSessionManager().retrieveByClause(
//			// CumandocVO.class, commonSql + " and pk_corp='1016'");
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != cumandocCol && cumandocCol.size() > 0) {
//			// Iterator it = cumandocCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// CumandocVO cumandocVO = (CumandocVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(
//			// CumandocVO.class,
//			// "pk_corp='1016' and pk_cubasdoc='"
//			// + cumandocVO.getPk_cubasdoc() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// cumandocVO.setPrimaryKey(null);
//			// cumandocVO
//			// .setCustflag(cumandocVO.getCustflag() == null
//			// || "".equals(cumandocVO.getCustflag()
//			// .trim()) ? "0" : cumandocVO
//			// .getCustflag());
//			// cumandocVO.setPk_currtype1(getExtpk_currtype(cumandocVO
//			// .getPk_currtype1()));
//			// String pk = getExternalDAO().insertVO(cumandocVO);
//			// sb.append(pk).append(";");
//			// }
//			// // else {
//			// // CumandocVO[] deptdocs = new CumandocVO[tempcol.size()];
//			// // cumandocVO.setPrimaryKey(((AccidVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// //
//			// cumandocVO.setPk_currtype1(getExtpk_currtype(cumandocVO.getPk_currtype1()));
//			// // getExternalDAO().updateVO(cumandocVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 11.开户银行
//			// String accbankSql = "select * from bd_accbank ";
//			// Logger.info("syndata bd_accbank start");
//			// sb.append("同步开户银行:");
//			// Collection accbankCol = null;
//			// try {
//			// accbankCol = getSessionManager().retrieveByClause(
//			// AccbankVO.class, commonSql + " and pk_corp='1016'");
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != accbankCol && accbankCol.size() > 0) {
//			// Iterator it = accbankCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// AccbankVO accbankVO = (AccbankVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(
//			// AccbankVO.class,
//			// "bankacc='" + accbankVO.getBankacc() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// accbankVO.setPrimaryKey(null);
//			// accbankVO.setPk_currtype(getExtpk_currtype(accbankVO
//			// .getPk_currtype()));
//			// String pk = getExternalDAO().insertVO(accbankVO);
//			// sb.append(pk).append(";");
//			// }
//			// // else {
//			// // AccbankVO[] deptdocs = new AccbankVO[tempcol.size()];
//			// // accbankVO.setPrimaryKey(((AccbankVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// //
//			// accbankVO.setPk_currtype(getExtpk_currtype(accbankVO.getPk_currtype()));
//			// // getExternalDAO().updateVO(accbankVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 12.资产类别
//			// String categorySql = "select * from fa_category ";
//			// Logger.info("syndata fa_category start");
//			// sb.append("同步资产类别:");
//			// Collection categoryCol = null;
//			// try {
//			// categoryCol = getSessionManager().retrieveByClause(
//			// CategoryVO.class, commonSql + " and pk_corp='1016'");
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != categoryCol && categoryCol.size() > 0) {
//			// Iterator it = categoryCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// CategoryVO categoryVO = (CategoryVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(
//			// CategoryVO.class,
//			// "cate_code='" + categoryVO.getCate_code() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// categoryVO.setPrimaryKey(null);
//			// categoryVO
//			// .setFk_depmethod(getExtFk_depmethod(categoryVO
//			// .getFk_depmethod()));
//			// getExternalDAO().insertVO(categoryVO);
//			//
//			// }
//			// // else {
//			// // CategoryVO[] deptdocs = new CategoryVO[tempcol.size()];
//			// // categoryVO.setPrimaryKey(((CategoryVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// //
//			// categoryVO.setFk_depmethod(getExtFk_depmethod(categoryVO.getFk_depmethod()));
//			// // getExternalDAO().updateVO(categoryVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 13.仓库档案
//			// String stordocSql = "select * from bd_stordoc ";
//			// Logger.info("syndata bd_stordoc start");
//			// sb.append("同步资产类别:");
//			// Collection stordocCol = null;
//			// try {
//			// stordocCol = getSessionManager().retrieveByClause(
//			// StordocVO.class, commonSql + " and pk_corp='1016'");
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != stordocCol && stordocCol.size() > 0) {
//			// Iterator it = stordocCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// StordocVO stordocVO = (StordocVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(
//			// StordocVO.class,
//			// "storcode='" + stordocVO.getStorcode() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// stordocVO.setPrimaryKey(null);
//			// stordocVO.setPk_calbody(getExtPk_calbody(stordocVO
//			// .getPk_calbody()));
//			// getExternalDAO().insertVO(stordocVO);
//			// }
//			// // else {
//			// // StordocVO[] deptdocs = new StordocVO[tempcol.size()];
//			// // stordocVO.setPrimaryKey(((StordocVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// //
//			// stordocVO.setPk_calbody(getExtPk_calbody(stordocVO.getPk_calbody()));
//			// // getExternalDAO().updateVO(stordocVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//			// // 14.凭证类别
//			// String vouchertypeSql = "select * from bd_vouchertype ";
//			// Logger.info("syndata bd_vouchertype start");
//			// sb.append("同步凭证类别:");
//			// Collection vouchertypeCol = null;
//			// try {
//			// vouchertypeCol = getSessionManager().retrieveByClause(
//			// VouchertypeVO.class, commonSql);
//			// } catch (DbException e1) {
//			// e1.printStackTrace();
//			// }
//			// if (null != vouchertypeCol && vouchertypeCol.size() > 0) {
//			// Iterator it = vouchertypeCol.iterator();
//			// Collection tempcol = null;
//			// while (it.hasNext()) {
//			// VouchertypeVO vouchertypeVO = (VouchertypeVO) it.next();
//			// tempcol = getExternalDAO().retrieveByClause(
//			// VouchertypeVO.class,
//			// "vouchtype='" + vouchertypeVO.getVouchtype() + "'");
//			// if (tempcol == null || tempcol.size() == 0) {
//			// vouchertypeVO.setPrimaryKey(null);
//			// String pk = getExternalDAO().insertVO(vouchertypeVO);
//			// sb.append(pk).append(";");
//			// }
//			// // else {
//			// // VouchertypeVO[] deptdocs = new
//			// // VouchertypeVO[tempcol.size()];
//			// // vouchertypeVO.setPrimaryKey(((VouchertypeVO) tempcol
//			// // .toArray(deptdocs)[0]).getPrimaryKey());
//			// // getExternalDAO().updateVO(vouchertypeVO);
//			// // }
//			// }
//			// }
//			// sb.append("<p/>");
//
//			// 16.凭证
//			Logger.info("syndata gl_voucher start");
//			sb.append("同步凭证:");
//
//			List voucherList = null;
//			UFDate d = new UFDate(System.currentTimeMillis());
//			int year = d.getYear();
//			StringBuilder voucherSql = new StringBuilder();
//			voucherSql.append("select a.pk_voucher    as pk_voucher,");
//			voucherSql.append("        d.glorgbookcode as glorgbookcode,");
//			voucherSql.append("        b.unitcode      as company,");
//			voucherSql.append("        c.vouchtypename     as voucher_type,");
//			voucherSql.append("        a.year          as fiscal_year,");
//			voucherSql.append("        a.period        as accounting_period,");
//			voucherSql.append("        a.no            as voucher_id,");
//			voucherSql.append("        a.attachment    as attachment_number,");
//			voucherSql.append("        a.prepareddate  as prepareddate,");
//			voucherSql.append("        e.user_code     as enter,");
//			voucherSql.append("        a.pk_casher       as cashier,");
//			voucherSql.append("        a.signflag      as signature,");
//			voucherSql.append("        f.user_code      as checker,");
//			voucherSql.append("        a.tallydate     as posting_date,");
//			voucherSql.append("        g.user_code      as posting_person,");
//			voucherSql.append("        a.pk_system       as voucher_making_system,");
//			voucherSql.append("        a.free1         as memo1,");
//			voucherSql.append("        a.free2         as memo2,");
//			voucherSql.append("        a.free3         as reserve1,");
//			voucherSql.append("        a.free4         as reserve2");
//			voucherSql.append("   from gl_voucher a");
//			voucherSql.append("   left outer join bd_corp b on a.pk_corp = b.pk_corp");
//			voucherSql.append("   left outer join bd_vouchertype c on c.pk_vouchertype = a.pk_vouchertype");
//			voucherSql.append("   left outer join bd_glorgbook d on d.pk_glorgbook = a.pk_glorgbook");
//			voucherSql.append("   left outer join sm_user e on a.pk_prepared=e.cuserid");
//			voucherSql.append("   left outer join sm_user f on a.pk_checked=f.cuserid");
//			voucherSql.append("   left outer join sm_user g on a.pk_manager=g.cuserid");
//			voucherSql.append("  where d.glorgbookcode = '10301-0000'");
//			voucherSql.append("    and period = '" + d.getStrMonth() + "'");
//			voucherSql.append("    and year = '" + year + "'");
//			voucherSql.append("    and a.pk_corp = '1016'");
//			voucherSql.append("    and nvl(a.dr, 0) = 0");
//			voucherSql.append("    and a.signflag='Y'");
//			try {
//				voucherList = (List) getBaseDAO().executeQuery(voucherSql.toString(), new MapListProcessor());
//			} catch (DbException e) {
//				e.printStackTrace();
//			}
//			if (voucherList != null && voucherList.size() > 0) {
//				StringBuilder voucherFile = new StringBuilder();
//				StringBuilder voucher;
//				voucherFile.append("<?xml version='1.0' encoding=\"UTF-8\"?>");
//				voucherFile.append("<ufinterface billtype=\"gl\" isexchange=\"Y\" proc=\"add\" receiver=\"10301-0000_TO\" roottag=\"voucher\" replace=\"Y\" sender=\"1001\">");
//				for (int i = 0; i < voucherList.size(); i++) {
//					voucher = new StringBuilder();
//					Map<String, Object> head = (Map<String, Object>) voucherList.get(i);
//					Iterator<String> headIT = head.keySet().iterator();
//					String pk_voucher = (String) head.get("pk_voucher");
//					voucher.append("<voucher id=\"" + pk_voucher + "\">");
//					voucher.append("<voucher_head>");
//					while (headIT.hasNext()) {
//						String headKey = headIT.next();
//						String headValue = head.get(headKey) == null ? "" : head.get(headKey).toString();
//						voucher.append("<").append(headKey).append(">");
//						voucher.append(headValue);
//						voucher.append("</").append(headKey).append(">");
//					}
//					voucher.append("</voucher_head>");
//					// 开始分录
//					List<Map<String, Object>> detailList = getDetail(pk_voucher);
//					if (detailList != null && detailList.size() > 0) {
//						voucher.append("<voucher_body>");
//						for (int j = 0; j < detailList.size(); j++) {
//							voucher.append("<entry>");
//							Map<String, Object> detail = detailList.get(j);
//							String assid = (String) detail.get("assid");
//							Iterator<String> detailIT = detail.keySet().iterator();
//							while (detailIT.hasNext()) {
//								String detailKey = detailIT.next();
//								String detailValue = detail.get(detailKey) == null ? "" : detail.get(detailKey).toString();
//								voucher.append("<").append(detailKey).append(">");
//								voucher.append(detailValue);
//								voucher.append("</").append(detailKey).append(">");
//							}
//							// 开始辅助核算
//							List<Map<String, Object>> fvList = getFreeValue(assid);
//							if (fvList != null && fvList.size() > 0) {
//								voucher.append("<auxiliary_accounting>");
//								for (int k = 0; k < fvList.size(); k++) {
//									Map<String, Object> fv = fvList.get(k);
//									String bdname = (String) fv.get("bdname");
//									String valuecode = (String) fv.get("valuecode");
//									voucher.append("<item name=\"" + bdname + "\">").append(valuecode).append("</item>");
//								}
//								voucher.append("</auxiliary_accounting>");
//							}
//							voucher.append("</entry>");
//						}
//						voucher.append("</voucher_body>");
//					}
//					voucher.append("</voucher>");
//					voucherFile.append(voucher);
//				}
//				voucherFile.append("</ufinterface>");
//				FileUtils.writeStringToFile(new File(RuntimeEnv.getInstance().getNCHome() + "/" + System.currentTimeMillis() + ".xml"), voucherFile.toString(), "UTF-8");
//			}
//		} catch (Exception e) {
//			sb.append(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			if (session != null) {
//				session.closeAll();
//			}
//			if (sessionManager != null) {
//				sessionManager.release();
//			}
//		}
//		System.out.println(new UFDateTime(System.currentTimeMillis()).toString() + ":凭证接口处理完成");
//		return null;
//	}
//
//	/**
//	 * 获取凭证子表
//	 * 
//	 * @param pk_voucher
//	 * @return
//	 */
//	private List<Map<String, Object>> getDetail(String pk_voucher) {
//		StringBuilder sql = new StringBuilder();
//		sql.append("select detailindex as entry_id,");
//		sql.append("        b.subjcode as account_code,");
//		sql.append("        explanation as abstract,");
//		sql.append("        checkstyle as settlement,");
//		sql.append("        checkno as document_id,");
//		sql.append("        checkdate as document_date,");
//		sql.append("        c.currtypename as currency,");
//		sql.append("        price as unit_price,");
//		sql.append("        excrate1 as exchange_rate1,");
//		sql.append("        excrate2 as exchange_rate2,");
//		sql.append("        debitquantity as debit_quantity,");
//		sql.append("        debitamount as primary_debit_amount,");
//		sql.append("        fracdebitamount as secondary_debit_amount,");
//		sql.append("        localdebitamount as natural_debit_currency,");
//		sql.append("        creditquantity as credit_quantity,");
//		sql.append("        creditamount as primary_credit_amount,");
//		sql.append("        fraccreditamount as secondary_credit_amount,");
//		sql.append("        localcreditamount as natural_credit_currency,");
//		sql.append("        assid");
//		sql.append("   from gl_detail a");
//		sql.append(" left outer join bd_accsubj b on a.pk_accsubj = b.pk_accsubj");
//		sql.append("   left outer join bd_currtype c on c.pk_currtype = a.pk_currtype");
//		sql.append("  where a.pk_voucher ='" + pk_voucher + "'");
//		List<Map<String, Object>> detailList = null;
//		try {
//			detailList = (List<Map<String, Object>>) getBaseDAO().executeQuery(sql.toString(), new MapListProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		return detailList;
//	}
//
//	/**
//	 * 查辅助核算
//	 * 
//	 * @param assid
//	 * @return
//	 */
//	private List<Map<String, Object>> getFreeValue(String assid) {
//		StringBuilder sql = new StringBuilder();
//		sql.append("select b.bdname bdname, a.valuename valuecode");
//		sql.append(" from gl_freevalue a");
//		sql.append(" left outer join Bd_Bdinfo b on b.pk_bdinfo = a.checktype");
//		sql.append(" where freevalueid ='" + assid + "'");
//		List<Map<String, Object>> freeValueList = null;
//		try {
//			freeValueList = (List<Map<String, Object>>) getBaseDAO().executeQuery(sql.toString(), new MapListProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		return freeValueList;
//	}
//
//	/**
//	 * 存在返回true,不存在返回false;
//	 * 
//	 * @param freevalueVO
//	 * @return
//	 */
//	private boolean existExtFreevalue(FreevalueVO freevalueVO) {
//		if (freevalueVO == null) { return false; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select freevalueid from gl_freevalue where freevalueid = '" + freevalueVO.getFreevalueid() + "' and valuecode = '" + freevalueVO.getValuecode() + "' and valuename = '" + freevalueVO.getValuename() + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) { return true; }
//
//		return false;
//	}
//
//	/**
//	 * 自定义项
//	 * 
//	 * @param oldChecktype
//	 * @return
//	 * @throws DAOException
//	 */
//	private String insertbdinfo(String oldChecktype) throws DAOException {
//		Object obj = null;
//		try {
//			obj = getSessionManager().retrieveByPK(BdinfoVO.class, oldChecktype);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		String pk = null;
//		if (null != obj && obj instanceof BdinfoVO) {
//			BdinfoVO bdinfoVO = (BdinfoVO) obj;
//			bdinfoVO.setPrimaryKey(null);
//			pk = getExternalDAO().insertVO(bdinfoVO);
//		}
//		return pk;
//	}
//
//	/**
//	 * 币别
//	 * 
//	 * @param pk_currtype
//	 * @return
//	 * @throws DAOException
//	 */
//	private String insertExtcurrtype(String pk_currtype) throws DAOException {
//		Object obj = null;
//		try {
//			obj = getSessionManager().retrieveByPK(CurrtypeVO.class, pk_currtype);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		String pk = null;
//		if (null != obj && obj instanceof CurrtypeVO) {
//			CurrtypeVO currtypeVO = (CurrtypeVO) obj;
//			currtypeVO.setPrimaryKey(null);
//			pk = getExternalDAO().insertVO(currtypeVO);
//		}
//		return pk;
//
//	}
//
//	// /**
//	// * 科目
//	// * @param pk_accsubj
//	// * @return
//	// * @throws DAOException
//	// */
//	// private String insertExtaccsubj(String pk_accsubj) throws DAOException {
//	// Object obj = getBaseDAO().retrieveByPK(AccsubjVO.class,
//	// pk_accsubj);
//	// String pk = null;
//	// if (null != obj && obj instanceof AccsubjVO) {
//	// AccsubjVO AccsubjVO = (AccsubjVO)obj;
//	// AccsubjVO.setPrimaryKey(null);
//	// pk = getExternalDAO().insertVO(AccsubjVO);
//	// }
//	// return pk;
//	//		
//	// }
//
//	/**
//	 * 凭证类别
//	 * 
//	 * @param pk_vouchertype
//	 * @return
//	 * @throws DAOException
//	 */
//	private String insertExtvouchertype(String pk_vouchertype) throws DAOException {
//		Object obj = null;
//		try {
//			obj = getSessionManager().retrieveByPK(VouchertypeVO.class, pk_vouchertype);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		String pk = null;
//		if (null != obj && obj instanceof VouchertypeVO) {
//			VouchertypeVO vouchertypeVO = (VouchertypeVO) obj;
//			vouchertypeVO.setPrimaryKey(null);
//			pk = getExternalDAO().insertVO(vouchertypeVO);
//		}
//		return pk;
//
//	}
//
//	/**
//	 * 主体帐簿
//	 * 
//	 * @param pk_glorgbook
//	 * @return
//	 * @throws DAOException
//	 */
//	private String insertExtglorgbook(String pk_glorgbook) throws DAOException {
//		Object obj = null;
//		try {
//			obj = getSessionManager().retrieveByPK(GlorgbookVO.class, pk_glorgbook);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		String pk = null;
//		if (null != obj && obj instanceof GlorgbookVO) {
//			GlorgbookVO glorgbookVO = (GlorgbookVO) obj;
//			glorgbookVO.setPrimaryKey(null);
//			pk = getExternalDAO().insertVO(glorgbookVO);
//		}
//		return pk;
//
//	}
//
//	/**
//	 * 主体
//	 * 
//	 * @param pk_glorg
//	 * @return
//	 * @throws DAOException
//	 */
//	private String insertExtglorg(String pk_glorg) throws DAOException {
//		Object obj = null;
//		try {
//			obj = getSessionManager().retrieveByPK(GlorgVO.class, pk_glorg);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		String pk = null;
//		if (null != obj && obj instanceof GlorgVO) {
//			GlorgVO glorgVO = (GlorgVO) obj;
//			glorgVO.setPrimaryKey(null);
//			pk = getExternalDAO().insertVO(glorgVO);
//		}
//		return pk;
//	}
//
//	//
//	// /**
//	// * 主体
//	// * @param pk_glorg
//	// * @return
//	// * @throws DAOException
//	// */
//	// private String getExtglorg(String pk_glorg) throws DAOException {
//	// Object obj = getBaseDAO().retrieveByPK(GlorgVO.class,
//	// pk_glorg);
//	// String pk = null;
//	// if (null != obj && obj instanceof GlorgVO) {
//	// GlorgVO glorgVO = (GlorgVO)obj;
//	// glorgVO.setPrimaryKey(null);
//	// pk = getExternalDAO().insertVO(glorgVO);
//	// }
//	// return pk;
//	//		
//	// }
//
//	/**
//	 * 帐簿
//	 * 
//	 * @param pk_glbook
//	 * @return
//	 * @throws DAOException
//	 */
//	private String insertExtglbook(String pk_glbook) throws DAOException {
//		Object obj = null;
//		try {
//			obj = getSessionManager().retrieveByPK(GlbookVO.class, pk_glbook);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		String pk = null;
//		if (null != obj && obj instanceof GlbookVO) {
//			GlbookVO glbookVO = (GlbookVO) obj;
//			glbookVO.setPrimaryKey(null);
//			pk = getExternalDAO().insertVO(glbookVO);
//		}
//		return pk;
//
//	}
//
//	/**
//	 * 向外系统插入用户
//	 * 
//	 * @param pk_casher
//	 * @return
//	 * @throws DAOException
//	 */
//	private String insertExtUser(String pk_casher) throws DAOException {
//		Object obj = null;
//		try {
//			obj = getSessionManager().retrieveByPK(UserVO.class, pk_casher);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		String pk = null;
//		if (null != obj && obj instanceof UserVO) {
//			UserVO userVO = (UserVO) obj;
//			userVO.setPrimaryKey(null);
//			pk = getExternalDAO().insertVO(userVO);
//		}
//		return pk;
//
//	}
//
//	/**
//	 * 科目主键(bd_accsubj[subjname])
//	 * 
//	 * @param pk_accsubj
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_accsubj(String pk_accsubj) throws DAOException {
//		if (pk_accsubj == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select subjname from bd_accsubj where pk_accsubj='" + pk_accsubj + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_accsubj from bd_accsubj where subjname='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 基础数据资源表(凭证辅助核算引用内容来源表)对应的值
//	 * 
//	 * @param oldChecktype
//	 * @param checkvalue
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtCheckvalue(String oldChecktype, String checkvalue) throws DAOException {
//		String bdcode = getBdcode(oldChecktype);
//		String newcheckvalue = null;
//
//		if ("13".equals(bdcode)) {// 账户档案
//			newcheckvalue = getExtPk_accid(checkvalue);
//		} else if ("71".equals(bdcode)) {// 客户辅助核算
//			newcheckvalue = getExtPk_cubasdoc(checkvalue);
//		} else if ("9".equals(bdcode)) {// 仓库档案
//			newcheckvalue = getExtPk_calbody(checkvalue);
//		} else if ("15".equals(bdcode)) {// 项目管理档案
//			newcheckvalue = getExtPk_jobbasfil(checkvalue);
//		} else if ("1".equals(bdcode)) {// 人员档案
//			newcheckvalue = getExtPk_psndoc(checkvalue);
//		} else if ("73".equals(bdcode)) {// 客商辅助核算
//			newcheckvalue = getExtPk_cumandoc(checkvalue);
//		} else if ("4".equals(bdcode)) {// 存货档案
//			newcheckvalue = getExtPk_invmandoc();
//		} else if ("8".equals(bdcode)) {// 开户银行
//			newcheckvalue = getExtPk_accbank(checkvalue);
//		} else if ("72".equals(bdcode)) {// 供应商辅助核算
//			newcheckvalue = getExtPk_cumandoc(checkvalue);
//		} else if ("2".equals(bdcode)) {// 部门档案
//			newcheckvalue = getExtPk_deptdoc(checkvalue);
//		} else if ("6".equals(bdcode)) {// 存货分类
//			newcheckvalue = getExtPk__invcl();
//		} else if ("22".equals(bdcode)) {// 资产类别
//			newcheckvalue = getExtPk_category(checkvalue);
//		} else {// 自定义项定义if("def".equals(bdcode))
//
//		}
//		return newcheckvalue;
//	}
//
//	/**
//	 * 资产类别:fa_category
//	 * 
//	 * @param pk_category
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_category(String pk_category) throws DAOException {
//		if (pk_category == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select cate_code from fa_category where pk_category='" + pk_category + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_category from fa_category where cate_code='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 存货分类bd_invcl
//	 * 
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk__invcl() throws DAOException {
//		Object obj = getExternalDAO().executeQuery("select pk_invcl from bd_invcl", new ColumnProcessor());
//		if (null != obj) {
//			return String.valueOf(obj);
//		} else {
//			return null;
//		}
//	}
//
//	/**
//	 * 存货管理档案
//	 * 
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_invmandoc() throws DAOException {
//		Object obj = getExternalDAO().executeQuery("select pk_cumandoc from bd_invmandoc", new ColumnProcessor());
//		if (null != obj) {
//			return String.valueOf(obj);
//		} else {
//			return null;
//		}
//	}
//
//	/**
//	 * 客商管理档案bd_cumandoc
//	 * 
//	 * @param checkvalue
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_cumandoc(String pk_cumandoc) throws DAOException {
//		if (pk_cumandoc == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select b.custcode from bd_cumandoc a,bd_cubasdoc b  where a.pk_cubasdoc=b.pk_cubasdoc and a.pk_cumandoc='" + pk_cumandoc + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select a.pk_cumandoc from bd_cumandoc a,bd_cubasdoc b  where a.pk_cubasdoc=b.pk_cubasdoc and  b.custcode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 人员档案
//	 * 
//	 * @param pk_psndoc
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_psndoc(String pk_psndoc) throws DAOException {
//		if (pk_psndoc == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select psncode from bd_psndoc where pk_psndoc='" + pk_psndoc + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_psndoc from bd_psndoc where psncode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 项目档案
//	 * 
//	 * @param pk_jobbasfil
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_jobbasfil(String pk_jobbasfil) throws DAOException {
//		if (pk_jobbasfil == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select jobcode from bd_jobbasfil where pk_jobbasfil='" + pk_jobbasfil + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_jobbasfil from bd_jobbasfil where jobcode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 账户档案
//	 * 
//	 * @param pk_accid
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_accid(String pk_accid) throws DAOException {
//		if (pk_accid == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select accidcode from bd_accid where pk_accid='" + pk_accid + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_accid from bd_accid where accidcode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 查询基础数据资源编号
//	 * 
//	 * @param oldChecktype
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getBdcode(String oldChecktype) throws DAOException {
//		if (oldChecktype == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select bdcode from bd_bdinfo where pk_bdinfo='" + oldChecktype + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//
//		return String.valueOf(obj);
//	}
//
//	/**
//	 * 凭证类别主键(bd_vouchertype[vouchtypename])
//	 * 
//	 * @param pk_vouchertype
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_vouchertype(String pk_vouchertype) throws DAOException {
//		if (pk_vouchertype == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select vouchtypename from bd_vouchertype where pk_vouchertype='" + pk_vouchertype + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_vouchertype from bd_vouchertype where vouchtypename='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 主体帐簿 (bd_glorgbook::会计主体账簿[glorgbookcode])
//	 * 
//	 * @param pk_glorgbook
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_glorgbook(String pk_glorgbook) throws DAOException {
//		if (pk_glorgbook == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select glorgbookcode from bd_glorgbook where pk_glorgbook='" + pk_glorgbook + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_glorgbook from bd_glorgbook where glorgbookcode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 主体 (bd_glorg[glorgcode]
//	 * 
//	 * @param pk_glorg
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_glorg(String pk_glorg) throws DAOException {
//		if (pk_glorg == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select glorgcode from bd_glorg where pk_glorg='" + pk_glorg + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_glorg from bd_glorg where glorgcode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 帐簿(bd_glbook[code])
//	 * 
//	 * @param pk_glbook
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_glbook(String pk_glbook) throws DAOException {
//		if (pk_glbook == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select code from bd_glbook where pk_glbook='" + pk_glbook + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_glbook from bd_glbook where code='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 获取外系统折旧方法(fa_depmethod[depmethod_code])
//	 * 
//	 * @param fk_depmethod
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtFk_depmethod(String fk_depmethod) throws DAOException {
//		if (fk_depmethod == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select depmethod_code from fa_depmethod where pk_depmethod='" + fk_depmethod + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_depmethod from fa_depmethod where depmethod_code='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 地区分类(bd_areacl[areaclcode])
//	 * 
//	 * @param pk_areacl
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_areacl(String pk_areacl) throws DAOException {
//		if (pk_areacl == null) { return " "; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select areaclcode from bd_areacl where pk_areacl='" + pk_areacl + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_areacl from bd_areacl where areaclcode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return " ";
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 自定义项档案列表(Bd_defdoclist[doclistname])
//	 * 
//	 * @param pk_defdoclist
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_defdoclist(String pk_defdoclist) throws DAOException {
//		if (pk_defdoclist == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select doclistname from bd_defdoclist where pk_defdoclist='" + pk_defdoclist + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_defdoclist from bd_defdoclist where doclistname='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			Collection defdoccol = null;
//			try {
//				defdoccol = getSessionManager().retrieveByClause(DefdoclistVO.class, " pk_defdoclist='" + pk_defdoclist + "'");
//			} catch (DbException e) {
//				e.printStackTrace();
//			}
//			DefdoclistVO[] vos = (DefdoclistVO[]) defdoccol.toArray(new DefdoclistVO[defdoccol.size()]);
//			for (int i = 0; i < vos.length; i++) {
//				vos[i].setDocislevflag(vos[i].getDocislevflag() == null ? new UFDouble("0") : vos[i].getDocislevflag());
//			}
//			getExternalDAO().insertVOArray(vos);
//			return pk_defdoclist;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 基础数据资源表(Bd_Bdinfo[bdname])
//	 * 
//	 * @param pk_bdinfo
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_bdinfo(String pk_bdinfo) throws DAOException {
//		if (pk_bdinfo == null) { return " "; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select bdcode from bd_bdinfo where pk_bdinfo='" + pk_bdinfo + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_bdinfo from Bd_Bdinfo where bdcode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return " ";
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 项目类型(bd_jobtype[jobtypename])
//	 * 
//	 * @param pk_jobtype
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_jobtype(String pk_jobtype) throws DAOException {
//		if (pk_jobtype == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select jobtypename from bd_jobtype where pk_jobtype='" + pk_jobtype + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_jobtype from bd_jobtype where jobtypename='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 客商档案(bd_cubasdoc)
//	 * 
//	 * @param pk_fim
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_cubasdoc(String pk_cubasdoc) throws DAOException {
//		if (pk_cubasdoc == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select custcode from bd_cubasdoc where pk_cubasdoc='" + pk_cubasdoc + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_cubasdoc from bd_cubasdoc where custcode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 结算中心主健 (bd_settlecenter[settlecentercode ])
//	 * 
//	 * @param pk_settlecent
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_settlecent(String pk_settlecent) throws DAOException {
//		if (pk_settlecent == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select settlecentercode from bd_settlecenter where pk_settlecenter='" + pk_settlecent + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_settlecenter from bd_settlecenter where settlecentercode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 默认交易币种(bd_currtype[currtypename])
//	 * 
//	 * @param pk_currtype
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtpk_currtype(String pk_currtype) throws DAOException {
//		if (pk_currtype == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select currtypename from bd_currtype where pk_currtype='" + pk_currtype + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_currtype from bd_currtype where currtypename='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 开户银行主键(bd_accbank[bankacc])
//	 * 
//	 * @param pk_accbank
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_accbank(String pk_accbank) throws DAOException {
//		if (pk_accbank == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select bankacc from bd_accbank where pk_accbank='" + pk_accbank + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_accbank from bd_accbank where bankacc='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 负责人(sm_user[user_code])
//	 * 
//	 * @param pk_psndoc
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtUser(String pk_psndoc) throws DAOException {
//		if (pk_psndoc == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select user_code from sm_user where cuserid='" + pk_psndoc + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select cuserid  from sm_user where user_code='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 部门(bd_deptdoc[deptcode])
//	 * 
//	 * @param pk_deptdoc
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_deptdoc(String pk_deptdoc) throws DAOException {
//		if (pk_deptdoc == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select deptcode from bd_deptdoc where pk_deptdoc='" + pk_deptdoc + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_deptdoc  from bd_deptdoc where deptcode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 库存组织(bd_calbody[bodyname])
//	 * 
//	 * @param pk_calbody
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_calbody(String pk_calbody) throws DAOException {
//		if (pk_calbody == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select bodyname from bd_calbody where pk_calbody='" + pk_calbody + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_calbody  from bd_calbody where bodyname='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 获取外系统人员类别
//	 * 
//	 * @param pk_psncl
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_psncl(String pk_psncl) throws DAOException {
//		if (pk_psncl == null) { return null; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select psnclasscode from bd_psncl where pk_psncl='" + pk_psncl + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_psncl  from bd_psncl where psnclasscode='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return null;
//		} else {
//			return obj.toString();
//		}
//	}
//
//	/**
//	 * 获取外系统人员基本档案
//	 * 
//	 * @param pk_psnbasdoc
//	 * @return
//	 * @throws DAOException
//	 */
//	private String getExtPk_psnbasdoc(String pk_psnbasdoc) throws DAOException {
//		if (pk_psnbasdoc == null) { return " "; }
//		Object obj = null;
//		try {
//			obj = getBaseDAO().executeQuery("select psnname from bd_psnbasdoc where pk_psnbasdoc='" + pk_psnbasdoc + "'", new ColumnProcessor());
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		if (obj != null) {
//			obj = getExternalDAO().executeQuery("select pk_psnbasdoc  from bd_psnbasdoc where psnname='" + String.valueOf(obj) + "'", new ColumnProcessor());
//		}
//		if (obj == null) {
//			return " ";
//		} else {
//			return obj.toString();
//		}
//	}
//
//	private BaseDAO baseDAO = null; // 数据库操作基类
//	private BaseDAO externalDAO = null; // 数据库操作基类
//
//	private PersistenceManager sessionManager = null;
//
//	/**
//	 * 取得sessionManager的值
//	 * 
//	 * @return sessionManager - PersistenceManager
//	 * @throws SQLException
//	 * @throws DbException
//	 * @throws DbException
//	 */
//	protected PersistenceManager getSessionManager() {
//		try {
//			if (sessionManager == null || sessionManager.getJdbcSession().getConnection().isClosed()) sessionManager = PersistenceManager.getInstance();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DbException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		// sessionManager.getJdbcSession().getConnection();
//		return sessionManager;
//	}
//
//	private JdbcSession session = null;
//
//	/**
//	 * 功能：数据库操作对象
//	 * 
//	 * @return
//	 * @throws SQLException
//	 * @throws DbException
//	 * @throws DbException
//	 */
//	protected JdbcSession getBaseDAO() {
//		try {
//			if (session == null || session.getConnection().isClosed()) {
//				getSessionManager();
//				session = sessionManager.getJdbcSession();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return session;
//	}
//
//	/**
//	 * 功能：数据库操作对象
//	 * 
//	 * @return
//	 */
//	protected BaseDAO getExternalDAO() {
//		if (externalDAO == null) externalDAO = new BaseDAO("voucher");
//		return externalDAO;
//	}
//}

package nc.bs.baoyin.alert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;

import org.apache.commons.io.FileUtils;

/**
 * 凭证接口-后台预警
 * 
 * @author fans
 * @date 2012-11-26
 * 
 */
public class SynData implements IBusinessPlugin {

	BaseDAO dao = new BaseDAO();
	String sender = "1001";

	public String implementReturnMessage(Key[] keys, String pk_corp, UFDate clientLoginDate) throws BusinessException {

		// 暂时取消传输凭证
		String[] exchangeUnitcodes = null; // 凭证传递公司

		String period = null; // 会计期间
		String unitcode = null; // 公司编码
		String[] vouchernos = null; // 凭证号,分号分隔
		UFDate startDate = null; // 制单日期(起)
		UFDate endDate = null; // 制单日期(止)
		String filePath = null; // 文件存储位置
		String queryCondition = null; // 自定义查询条件
		String pk_vouchertype = null;
		if (keys != null && keys.length > 0) {
			for (int i = 0; i < keys.length; i++) {
				String paramName = keys[i].getName();
				if (paramName.equals("period")) {
					period = keys[i].getValue() == null ? null : keys[i].getValue().toString();
				} else if (paramName.equals("unitcode")) {
					unitcode = keys[i].getValue() == null ? null : keys[i].getValue().toString();
				} else if (paramName.equals("vouchernos")) {
					vouchernos = keys[i].getValue() == null ? null : keys[i].getValue().toString().split(";");
				} else if (paramName.equals("startDate")) {
					startDate = keys[i].getValue() == null ? null : new UFDate(keys[i].getValue().toString());
				} else if (paramName.equals("endDate")) {
					endDate = keys[i].getValue() == null ? null : new UFDate(keys[i].getValue().toString());
				} else if (paramName.equals("queryCondition")) {
					queryCondition = keys[i].getValue() == null ? null : keys[i].getValue().toString();
				} else if (paramName.equals("exchangeUnitcodes")) {
					exchangeUnitcodes = keys[i].getValue() == null ? null : keys[i].getValue().toString().split(";");
				} else if (paramName.equals("filePath")) {
					filePath = keys[i].getValue() == null ? null : keys[i].getValue().toString();
				} else if (paramName.equals("voucherType")) {
					pk_vouchertype = keys[i].getValue() == null ? null : keys[i].getValue().toString();
				}
			}
		}

		StringBuilder condition = new StringBuilder();
		if ((filePath == null || "".equals(filePath)) || ((unitcode == null || "".equals(unitcode)) && (exchangeUnitcodes == null || exchangeUnitcodes.length <= 0))) return "不符合预警插件运行条件:[公司编码]或[凭证传递公司]必须有值";
		//		voucherSql.append("    and period = '" + strMonth + "'");
		//		voucherSql.append("    and year = '" + year + "'");
		//		voucherSql.append("    and b.unitcode = '" + unitcode + "'");
		//		voucherSql.append(" and a.no <=5");
		//		voucherSql.append(" and e.user_code ='572751'");
		//		voucherSql.append("    and a.pk_manager<>'N/A'");
		if (unitcode != null && !"".equals(unitcode)) {
			String actPeriod = null;
			condition.append(" and b.unitcode ='" + unitcode + "'");
			condition.append(" and d.glorgbookcode = '" + unitcode + "-0000'");
			if (vouchernos != null && vouchernos.length > 0) {
				condition.append(" and a.no in (");
				for (int i = 0; i < vouchernos.length; i++) {
					condition.append(vouchernos[i]);
					if (i == vouchernos.length - 1) condition.append(")");
					else condition.append(",");
				}
			}
			if (period != null && !"".equals(period)) {
				actPeriod = period;
				condition.append(" and a.year='" + period.substring(0, 4) + "'");
				condition.append(" and a.period='" + period.substring(4, 6) + "'");
			}
			if (startDate != null && !"".equals(startDate)) {
				if (actPeriod == null || "".equals(actPeriod)) actPeriod = startDate.getYear() + startDate.getStrMonth();
				condition.append(" and a.prepareddate >= '" + startDate + "'");
			}
			if (endDate != null && !"".equals(endDate)) {
				if (actPeriod == null || "".equals(actPeriod)) actPeriod = endDate.getYear() + endDate.getStrMonth();
				condition.append(" and a.prepareddate <= '" + endDate + "'");
			}
			if (queryCondition != null && !"".equals(queryCondition)) {
				condition.append(queryCondition);
			}
			if (pk_vouchertype != null && !"".equals(pk_vouchertype)) {
				condition.append(" and a.pk_vouchertype='" + pk_vouchertype + "'");
			}
			try {
				exportVoucherXml(filePath, condition.toString(), actPeriod, unitcode);
				return "导出凭证成功结束";
			} catch (Exception ex) {
				return "导出凭证异常结束:" + ex.getMessage();
			}
		} else {
			UFDate currDate = new UFDate(System.currentTimeMillis());
			UFDate queryDate = currDate.getDateBefore(1);
			String actPeriod = queryDate.getYear() + queryDate.getStrMonth();
			try {
				for (int i = 0; i < exchangeUnitcodes.length; i++) {
					condition = new StringBuilder();
					condition.append(" and b.unitcode ='" + exchangeUnitcodes[i] + "'");
					condition.append(" and d.glorgbookcode = '" + exchangeUnitcodes[i] + "-0000'");
					condition.append(" and a.ts>='" + queryDate + " 00:00:00'");
					condition.append(" and a.ts<='" + queryDate + " 23:59:59'");
					if (queryCondition != null && !"".equals(queryCondition)) {
						condition.append(queryCondition);
					}
					if (pk_vouchertype != null && !"".equals(pk_vouchertype)) {
						condition.append(" and a.pk_vouchertype='" + pk_vouchertype + "'");
					}
					exportVoucherXml(filePath, condition.toString(), actPeriod, exchangeUnitcodes[i]);
				}
				return "导出凭证成功结束";
			} catch (Exception ex) {
				return "导出凭证异常结束:" + ex.getMessage();
			}
		}
	}

	private void exportVoucherXml(String filePath, String condition, String period, String unitcode) throws Exception {
		try {
			List<Map<String, Object>> voucherList = getVoucherHead(condition);
			if (voucherList != null && voucherList.size() > 0) {
				StringBuilder voucherFile = new StringBuilder();
				StringBuilder voucher;
				voucherFile.append("<?xml version='1.0' encoding=\"UTF-8\"?>");
				voucherFile.append("<ufinterface billtype=\"gl\" isexchange=\"Y\" proc=\"add\" receiver=\"" + unitcode + "-0000_TO\" roottag=\"voucher\" replace=\"Y\" sender=\"" + sender + "\">");
				for (int i = 0; i < voucherList.size(); i++) {
					voucher = new StringBuilder();
					Map<String, Object> head = (Map<String, Object>) voucherList.get(i);
					Iterator<String> headIT = head.keySet().iterator();
					String pk_voucher = (String) head.get("pk_voucher");
					voucher.append("<voucher id=\"" + pk_voucher + "\">");
					voucher.append("<voucher_head>");
					while (headIT.hasNext()) {
						String headKey = headIT.next();
						String headValue = head.get(headKey) == null ? "" : head.get(headKey).toString();
						voucher.append("<").append(headKey).append(">");
						voucher.append(headValue);
						voucher.append("</").append(headKey).append(">");
					}
					voucher.append("</voucher_head>");
					// 开始分录
					List<Map<String, Object>> detailList = getDetail(pk_voucher);
					if (detailList != null && detailList.size() > 0) {
						voucher.append("<voucher_body>");
						for (int j = 0; j < detailList.size(); j++) {
							voucher.append("<entry>");
							Map<String, Object> detail = detailList.get(j);
							String assid = (String) detail.get("assid");
							Iterator<String> detailIT = detail.keySet().iterator();
							while (detailIT.hasNext()) {
								String detailKey = detailIT.next();
								String detailValue = detail.get(detailKey) == null ? "" : detail.get(detailKey).toString();
								voucher.append("<").append(detailKey).append(">");
								voucher.append(detailValue);
								voucher.append("</").append(detailKey).append(">");
							}
							// 开始辅助核算
							List<Map<String, Object>> fvList = getFreeValue(assid);
							if (fvList != null && fvList.size() > 0) {
								voucher.append("<auxiliary_accounting>");
								for (int k = 0; k < fvList.size(); k++) {
									Map<String, Object> fv = fvList.get(k);
									String bdname = (String) fv.get("bdname");
									String valuecode = (String) fv.get("valuecode");
									voucher.append("<item name=\"" + bdname + "\">").append(valuecode).append("</item>");
								}
								voucher.append("</auxiliary_accounting>");
							}
							voucher.append("</entry>");
						}
						voucher.append("</voucher_body>");
					}
					voucher.append("</voucher>");
					voucherFile.append(voucher);
				}
				voucherFile.append("</ufinterface>");

				String pattern = "yyyyMMddHHmmss";
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				String nowDt = sdf.format(Calendar.getInstance().getTime());
				File unitDir = new File(filePath + "/" + unitcode);
				FileUtils.forceMkdir(unitDir);
				String file = filePath + "/" + unitcode + "/" + unitcode + "_" + period + "_" + nowDt + ".xml";
				FileUtils.writeStringToFile(new File(file), voucherFile.toString(), "UTF-8");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getVoucherHead(String condition) throws Exception {
		StringBuilder voucherSql = new StringBuilder();
		voucherSql.append("select a.pk_voucher    as pk_voucher,");
		voucherSql.append("        d.glorgbookcode as glorgbookcode,");
		voucherSql.append("        b.unitcode      as company,");
		voucherSql.append("        c.vouchtypename     as voucher_type,");
		voucherSql.append("        a.year          as fiscal_year,");
		voucherSql.append("        a.period        as accounting_period,");
		voucherSql.append("        a.no            as voucher_id,");
		voucherSql.append("        a.attachment    as attachment_number,");
		voucherSql.append("        a.prepareddate  as prepareddate,");
		voucherSql.append("        e.user_code     as enter,");
		voucherSql.append("        a.pk_casher       as cashier,");
		// 取消签字
		// voucherSql.append("        a.signflag      as signature,");
		voucherSql.append("        'N'      as signature,");
		// 取消审核
		//voucherSql.append("        f.user_code      as checker,");
		voucherSql.append("        null      as checker,");
		// 取消记账
		// voucherSql.append("        a.tallydate     as posting_date,");
		// voucherSql.append("        g.user_code      as posting_person,");
		voucherSql.append("        null     as posting_date,");
		voucherSql.append("        null      as posting_person,");

		voucherSql.append("        a.pk_system       as voucher_making_system,");
		voucherSql.append("        a.free1         as memo1,");
		voucherSql.append("        a.free2         as memo2,");
		voucherSql.append("        a.free3         as reserve1,");
		voucherSql.append("        a.free4         as reserve2");
		voucherSql.append("   from gl_voucher a");
		voucherSql.append("   left outer join bd_corp b on a.pk_corp = b.pk_corp");
		voucherSql.append("   left outer join bd_vouchertype c on c.pk_vouchertype = a.pk_vouchertype");
		voucherSql.append("   left outer join bd_glorgbook d on d.pk_glorgbook = a.pk_glorgbook");
		voucherSql.append("   left outer join sm_user e on a.pk_prepared=e.cuserid");
		voucherSql.append("   left outer join sm_user f on a.pk_checked=f.cuserid");
		voucherSql.append("   left outer join sm_user g on a.pk_manager=g.cuserid");
		voucherSql.append("  where nvl(a.dr, 0) = 0");
		voucherSql.append("    and a.pk_manager<>'N/A'");
		voucherSql.append(condition);
		return (List<Map<String, Object>>) dao.executeQuery(voucherSql.toString(), new MapListProcessor());
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getDetail(String pk_voucher) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select detailindex as entry_id,");
		sql.append("        b.subjcode as account_code,");
		sql.append("        explanation as abstract,");
		sql.append("        checkstyle as settlement,");
		sql.append("        checkno as document_id,");
		sql.append("        checkdate as document_date,");
		sql.append("        c.currtypename as currency,");
		sql.append("        price as unit_price,");
		sql.append("        excrate1 as exchange_rate1,");
		sql.append("        excrate2 as exchange_rate2,");
		sql.append("        debitquantity as debit_quantity,");
		sql.append("        debitamount as primary_debit_amount,");
		sql.append("        fracdebitamount as secondary_debit_amount,");
		sql.append("        localdebitamount as natural_debit_currency,");
		sql.append("        creditquantity as credit_quantity,");
		sql.append("        creditamount as primary_credit_amount,");
		sql.append("        fraccreditamount as secondary_credit_amount,");
		sql.append("        localcreditamount as natural_credit_currency,");
		sql.append("        assid");
		sql.append("   from gl_detail a");
		sql.append(" left outer join bd_accsubj b on a.pk_accsubj = b.pk_accsubj");
		sql.append("   left outer join bd_currtype c on c.pk_currtype = a.pk_currtype");
		sql.append("  where a.pk_voucher ='" + pk_voucher + "'");
		Logger.error(sql);
		return (List<Map<String, Object>>) dao.executeQuery(sql.toString(), new MapListProcessor());
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getFreeValue(String assid) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.bdname bdname, a.valuename valuecode");
		sql.append(" from gl_freevalue a");
		sql.append(" left outer join Bd_Bdinfo b on b.pk_bdinfo = a.checktype");
		sql.append(" where freevalueid ='" + assid + "'");
		return (List<Map<String, Object>>) dao.executeQuery(sql.toString(), new MapListProcessor());
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
