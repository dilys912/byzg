package nc.bs.cm.outer;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.cm.cm101.CtcenterBO;
import nc.bs.cm.cm102.SubfacBO;
import nc.bs.cm.cm107.InpricesetBO;
import nc.bs.cm.cm301.WithOuterBO;
import nc.bs.cm.cm302.PullStuffErrBO;
import nc.bs.cm.cm302.StuffBO;
import nc.bs.cm.cm303.HalfprodBO;
import nc.bs.cm.cm306.ServuseBO;
import nc.bs.cm.cm309.ProductBO;
import nc.bs.cm.pub.CommonDataBO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.SystemException;
import nc.bs.pub.billcodemanage.BillcodeRuleBO;
import nc.itf.gl.pub.ICommAccBookPub;
import nc.itf.ia.service.IIAToCA;
import nc.itf.mm.cm.IMmToCm;
import nc.itf.pd.cm.IPdToCm;
import nc.itf.uap.bd.multibook.IGLOrgBookAcc;
import nc.vo.bd.b24.StordocVO;
import nc.vo.cm.cm101.CtcenterItemVO;
import nc.vo.cm.cm101.CtcenterVO;
import nc.vo.cm.cm102.SubFacVO;
import nc.vo.cm.cm302.PullStuffErrVO;
import nc.vo.cm.cm302.StuffHeaderVO;
import nc.vo.cm.cm302.StuffItemVO;
import nc.vo.cm.cm302.StuffVO;
import nc.vo.cm.cm303.HalfprodHeaderVO;
import nc.vo.cm.cm303.HalfprodItemVO;
import nc.vo.cm.cm303.HalfprodVO;
import nc.vo.cm.cm306.ServuseHeaderVO;
import nc.vo.cm.cm306.ServuseItemVO;
import nc.vo.cm.cm306.ServuseVO;
import nc.vo.cm.cm309.ProductHeaderVO;
import nc.vo.cm.cm309.ProductItemVO;
import nc.vo.cm.cm309.ProductVO;
import nc.vo.cm.pub.CMBusinessException;
import nc.vo.cm.pub.CMLogger;
import nc.vo.cm.pub.ConstVO;
import nc.vo.mm.pub.pub1020.DisConditionVO;
import nc.vo.mm.pub.pub1020.DisRouteVO;
import nc.vo.mm.pub.pub1020.DisassembleVO;
import nc.vo.mm.pub.pub1030.CostRtHeaderVO;
import nc.vo.mm.pub.pub1030.CostRtItemVO;
import nc.vo.mm.pub.pub1030.CostRtVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能描述：与外系统交互的接口类 * 作者:崔勇
 * 
 * 创建日期:(2003-4-14 19:21:17)
 * 
 * 修改记录及日期:
 * 
 * 修改人:
 * 
 */
public class OuterBO extends nc.bs.pub.BusinessObject implements nc.bs.sm.install.IUpdateData {
	/**
	 * OuterBO 构造子注解。
	 */
	public OuterBO() {
		super();
	}

	/**
	 * 函数功能:检查出库单
	 * 
	 * 参数: sSysinfos[0] ----- 公司 sSysinfos[1] ----- 工厂 sSysinfos[2] ----- 起始日期
	 * sSysinfos[3] ----- 结束日期 sSysinfos[4] ----- 单据类型,"I3" 产成品入库单 "I6" 材料出库单
	 * sSysinfos[5] ----- 当前用户 sSysinfos[6] ----- 当前日期 sSysinfos[7] -----
	 * 是否成本对象（Y，N） 标识参数sInvs是成本对象还是材料 sSysinfos[8]-------会计年
	 * sSysinfos[9]-------会计月 String[] sRDCLs ----- 入库类别 String[] sInvs ----- 存货
	 */
	public String checkIAI6BillsForM2(String[] sSysinfos, String[] sRDCLs) throws nc.vo.cm.pub.CMBusinessException {

		// BOS
		// nc.bs.ia.outter.Outter boIAOutter = null;
		StuffVO[] vosStuffVO = null;
		CommonDataBO boCommonData = null;
		WithOuterBO boWithOuter = null;
		BillcodeRuleBO boBillcodeRuleBO = null;

		try {

			Vector vtItems = new Vector();
			Vector vtHeads = new Vector();
			String sCorpID = sSysinfos[0];
			String sCalbodyID = sSysinfos[1];
			String sBillTypecode = sSysinfos[4];

			boCommonData = getCommonDataBO();
			boWithOuter = new WithOuterBO();

			// 取数
			// nc.bs.ia.outter.OutterHome home = (nc.bs.ia.outter.OutterHome)
			// getBeanHome(nc.bs.ia.outter.OutterHome.class,
			// "nc.bs.ia.outter.OutterBO");
			// boIAOutter = home.create();
			String[] sSysinfoscopy = new String[7];
			System.arraycopy(sSysinfos, 0, sSysinfoscopy, 0, 7);
			IIAToCA iaToCa = (IIAToCA) NCLocator.getInstance().lookup(IIAToCA.class.getName());
			
			String[] sInterfaceData=new String[6];
			sInterfaceData[0] = sSysinfos[0];
			sInterfaceData[1] = sSysinfos[1];
			sInterfaceData[2] = sSysinfos[2];
			sInterfaceData[3] = sSysinfos[3];
			sInterfaceData[4] = sSysinfos[7];
			sInterfaceData[5] = sSysinfos[8];
			
			
			String sSFBHYCJCD=(new CommonDataBO()).getParaValue(sCorpID, sCalbodyID, ConstVO.m_iPara_SFBHYCJCD);
			boolean bSFBHYCJCD="Y".equalsIgnoreCase(sSFBHYCJCD)?true:false;

//			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCPBills(sSysinfoscopy, sRDCLs, null);
			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCCBills(sInterfaceData, sRDCLs, null,bSFBHYCJCD,false);
			if (bvos != null && bvos.length > 0) {

				// 获得所有成本对象的ID,未审核的非成本对象不取
				StringBuffer sSQL = new StringBuffer(300);
				sSQL.append("select pk_produce ");
				sSQL.append("from bd_produce ");
				sSQL.append("where pk_corp = '" + sSysinfos[0] + "' ");
				sSQL.append("and pk_calbody = '" + sSysinfos[1] + "' ");
				sSQL.append("and sfcbdx = 'Y' ");
				String sResult[][] = boCommonData.queryData(sSQL.toString());
				HashSet vInvs = new HashSet(150);
				if (sResult.length != 0) {
					for (int i = 0; i < sResult.length; i++) {
						vInvs.add(sResult[i][0]);
					}
				} // if (sResult.length != 0)

				// 获得所有存货的基本ID和生产ID
				sSQL.setLength(0);
				sSQL.append("select  a.pk_invbasdoc ,  b.pk_produce ,  a.pk_invmandoc,d.pk_invcl ");
				sSQL.append(" from bd_invmandoc a,bd_produce b ,bd_invbasdoc c ,bd_invcl d ");
				sSQL.append(" where  a.pk_invbasdoc=c.pk_invbasdoc ");
				sSQL.append(" and a.pk_invmandoc = b.pk_invmandoc ");
				sSQL.append(" and c.pk_invcl=d.pk_invcl");
				sSQL.append(" and b.pk_corp = '" + sCorpID + "' ");
				sSQL.append(" and b.pk_calbody = '" + sCalbodyID + "' ");
				String[][] sAllInvBasProdPKs = boCommonData.queryData(sSQL.toString());
				HashMap invHashMap = new HashMap();
				for (int i = 0; i < sAllInvBasProdPKs.length; i++) {
					invHashMap.put(sAllInvBasProdPKs[i][2], Integer.toString(i));
				}

				// 获得所有的成本中心
				CtcenterVO[] ctcentervos = findAllCTCenters(sCorpID, sCalbodyID);
				HashMap hmapStuffVO = new HashMap();
				ArrayList aryPullStuffErrVO = new ArrayList();
				// 获得所有的成本要素
				sSQL.setLength(0);

				sSQL.append("select distinct a.pk_subfac,c.pk_subfacset, b.csubfacname,");
				sSQL.append("a.pk_ctcenter,a.pk_rdcl,a.cvalue,a.pk_invbasdoc ");
				sSQL.append("from cm_subfac_b a, cm_subfac b  ,cm_subfacset c ");
				sSQL.append("where b.blastlevelflag = 'Y'  ");
				sSQL.append("and a.pk_subfac = b.pk_subfac  ");
				sSQL.append("and b.pk_subfac=c.pk_subfac ");
				sSQL.append("and a.pk_corp='" + sCorpID + "'  ");
				sSQL.append("and a.citem='INVCL' ");
				sSQL.append("and c.pk_corp=a.pk_corp  ");
				sSQL.append("and c.pk_ctcenter=a.pk_ctcenter ");

				String[][] sAllSubPKs = boCommonData.queryData(sSQL.toString());

				// 循环处理
				for (int i = 0; i < bvos.length; i++) {
					// 存货核算的VO
					nc.vo.ia.bill.BillHeaderVO voBillHeaderVO = (nc.vo.ia.bill.BillHeaderVO) bvos[i].getParentVO();
					nc.vo.ia.bill.BillItemVO[] vosBillItemVO = (nc.vo.ia.bill.BillItemVO[]) bvos[i].getChildrenVO();
					String sErrNote = "";
					for (int j = 0; j < vosBillItemVO.length; j++) {
						
						if(vosBillItemVO[j]==null)
							continue;

						/**
						 * ******************************** 1
						 * 未审核的原材料不取*******************************************************
						 */
						String pk_invmandoc = vosBillItemVO[j].getCinventoryid();
						if (pk_invmandoc == null || pk_invmandoc.trim().length() == 0) {
							System.out.println("程序错误:没有得到存货的管理ID!!");
							continue;
						}
						int iIndex = Integer.parseInt(invHashMap.get(pk_invmandoc).toString());
						String pk_invbasdoc = sAllInvBasProdPKs[iIndex][0];
						String pk_produce = sAllInvBasProdPKs[iIndex][1];
						String pk_invcl = sAllInvBasProdPKs[iIndex][3];

						if (vosBillItemVO[j].getCauditorid() == null && vInvs.contains(pk_produce) == false) {

							sErrNote = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000000")/* @res""原材料未审核"" */;
							// private PullStuffErrVO getPullStuffErrVO(String[]
							// sSysinfos, nc.vo.ia.bill.BillHeaderVO
							// voBillHeaderVO, nc.vo.ia.bill.BillItemVO
							// voBillItemVO,String sErrNote,String pk_invbasdoc)
							// throws Exception {
							PullStuffErrVO voPullStuffErrVO = getPullStuffErrVO(sSysinfos, voBillHeaderVO, vosBillItemVO[j], sErrNote, pk_invbasdoc);
							aryPullStuffErrVO.add(voPullStuffErrVO);
							continue;

						}

						/**
						 * ******************************** 2
						 * 根据工作中心和部门检验成本中心*********************************************************
						 */
						String pk_ctcenter;
						String sCtcenterVOpk = null;
						String sCtcentercode = null;
						String sCtcentername = null;
						String[] sCtcenters = getCTCenterForM2(ctcentervos, voBillHeaderVO.getCdeptid(), vosBillItemVO[j].getCwp());
						if (sCtcenters != null && sCtcenters.length > 0 && sCtcenters[0] != null) {
							sCtcenterVOpk = sCtcenters[0];
							sCtcentercode = sCtcenters[1];
							sCtcentername = sCtcenters[2];
							pk_ctcenter = sCtcenterVOpk;
						} else {
							// 没有获得对应的成本中心
							sErrNote = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000001")/* @res""没有获得对应的成本中心"" */;
							PullStuffErrVO voPullStuffErrVO = getPullStuffErrVO(sSysinfos, voBillHeaderVO, vosBillItemVO[j], sErrNote, pk_invbasdoc);
							aryPullStuffErrVO.add(voPullStuffErrVO);
							continue;

						}
						/**
						 * ********************************* 3
						 * 检验成本要素*****************************************
						 */
						// String[] sFac = boWithOuter.getSubfacForInv(
						// voBillHeaderVO.getPk_corp(),
						// voBillHeaderVO.getCrdcenterid(),
						// sCtcenterVOpk,
						// pk_invbasdoc,
						// voBillHeaderVO.getCdispatchid(),
						// new UFBoolean(false));
						// public String[] getSubfacForInv(
						// String sCorpID,
						// String sCalbodyID,
						// String sCtcenterID,
						// String invbasID,
						// String sRDCLpk,
						// UFBoolean isAssserv)
						// sAllSubPKs[0] pk_subfac
						// sAllSubPKs[1] pk_subfacset
						// sAllSubPKs[2] csubfacname
						// sAllSubPKs[3] pk_ctcenter
						// sAllSubPKs[4] pk_rdcl
						// sAllSubPKs[5] cvalue
						// sAllSubPKs[6]a.pk_invbasdoc
						String[] sFac = null;
						// 先按照物料pK查找成本要素
						for (int m = 0; m < sAllSubPKs.length; m++) {
							if (sAllSubPKs[m][3].equals(sCtcenterVOpk) && sAllSubPKs[m][4].equals(voBillHeaderVO.getCdispatchid()) && sAllSubPKs[m][6] != null
									&& sAllSubPKs[m][6].trim().equals(pk_invbasdoc)) {
								sFac = new String[3];
								sFac[0] = sAllSubPKs[m][0];
								sFac[1] = sAllSubPKs[m][1];
								sFac[2] = sAllSubPKs[m][2];
								break;
							}
						}
						// 然后按照存货分类查找
						if (sFac == null) {
							for (int m = 0; m < sAllSubPKs.length; m++) {
								// if(sAllSubPKs[m][5].trim().equals("chfl0000000000000002"))
								// {String ok="ok";}
								if (sAllSubPKs[m][3].equals(sCtcenterVOpk) && sAllSubPKs[m][4].equals(voBillHeaderVO.getCdispatchid()) && sAllSubPKs[m][5] != null
										&& sAllSubPKs[m][5].trim().equals(pk_invcl)) {
									sFac = new String[3];
									sFac[0] = sAllSubPKs[m][0];
									sFac[1] = sAllSubPKs[m][1];
									sFac[2] = sAllSubPKs[m][2];
									break;
								}
							}
						}

						if (sFac == null) {
							// sErrNote = ("物料在成本中心"+"[" + sCtcentercode + ":" +
							// sCtcentername + "]"+"没有对应的成本要素");
							// getStrByID("10092040","UPP-000002",null,new
							// String[]{name,value+""})/
							sErrNote = (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000002", null, new String[] { "[" + sCtcentercode + ":" + sCtcentername + "]" })/* @res""物料在成本中心{0}没有对应的成本要素"" */);// "+"["
							// +
							// sCtcentercode
							// +
							// ":"
							// +
							// sCtcentername
							// +
							// "]"+"
							PullStuffErrVO voPullStuffErrVO = getPullStuffErrVO(sSysinfos, voBillHeaderVO, vosBillItemVO[j], sErrNote, pk_invbasdoc);
							aryPullStuffErrVO.add(voPullStuffErrVO);
							continue;
						}
						// /************************************4
						// 检验单价和金额是否为空*****************************************************/
						// UFDouble nnum = vosBillItemVO[j].getNnumber();
						// UFDouble nCost = vosBillItemVO[j].getNmoney();
						// if ((nnum == null || nnum.toDouble().doubleValue() ==
						// 0.00) && (nCost == null ||
						// nCost.toDouble().doubleValue() == 0.00)) {
						// sErrNote = ("价格和金额同时为空或者为零!!");
						// PullStuffErrVO voPullStuffErrVO =
						// getPullStuffErrVO(sSysinfos, voBillHeaderVO,
						// vosBillItemVO[j], sErrNote, pk_invbasdoc);
						// aryPullStuffErrVO.add(voPullStuffErrVO);
						// continue;
						// }
						/** ****************** */
						if (vosBillItemVO[j].getVbomcode() == null && vosBillItemVO[j].getVproducebatch() != null) {
							// sErrNote = "物料有订单号"+"[" +
							// vosBillItemVO[j].getVproducebatch() +
							// "]"+"但没有成本对象";
							sErrNote = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000003", null, new String[] { "[" + vosBillItemVO[j].getVproducebatch() + "]" })/* @res""物料有订单号{0}但没有成本对象"" */;// "+"["
							// +
							// vosBillItemVO[j].getVproducebatch()
							// +
							// "]"+"
							PullStuffErrVO voPullStuffErrVO = getPullStuffErrVO(sSysinfos, voBillHeaderVO, vosBillItemVO[j], sErrNote, pk_invbasdoc);
							aryPullStuffErrVO.add(voPullStuffErrVO);
							continue;
						}

					} // for (int j = 0; j < vosBillItemVO.length; j++)
				} // for (int i = 0; i < bvos.length; i++)
				PullStuffErrVO[] vosPullStuffErrVO = null;
				int iarysize = aryPullStuffErrVO.size();
				if (iarysize > 0) {
					vosPullStuffErrVO = new PullStuffErrVO[iarysize];
					vosPullStuffErrVO = (PullStuffErrVO[]) (aryPullStuffErrVO.toArray(vosPullStuffErrVO));
					PullStuffErrBO boPullStuffErrBO = new PullStuffErrBO();
					boPullStuffErrBO.insertArray(vosPullStuffErrVO);
				}
			} // if (bvos != null)
		} catch (CMBusinessException e) {
			throw e;
		} catch (Exception ee) {
			throw new CMBusinessException(ee.getMessage(), ee);
		}
		return null;
	}

	/**
	 * SessionBean接口中的方法。
	 * <p>
	 * 这个方法由EJB Container调用，永远不要在你的程序中调用这个方 法。
	 * <p>
	 * 创建日期：(2001-2-15 16:34:02)
	 * 
	 * @exception nc.vo.cm.pub.CMBusinessException
	 *                异常说明。
	 */
	public void ejbCreate() {
	}

	/***************************************************************************
	 * 函数功能: 获得该公司工厂下的所有成本中心，以便根据 部门或工作中心找其对应的成本中心。
	 * 
	 * 参数: String : sCorpID : 公司 String : sCalbodyID : 工厂
	 * 
	 * 返回值: CtcenterVO[]
	 * 
	 * 异常:
	 * 
	 * 创建日期：(2003-5-29 10:22:53)
	 **************************************************************************/

	private CtcenterVO[] findAllCTCenters(String sCorpID, String sCalbodyID) throws nc.vo.cm.pub.CMBusinessException {
		CtcenterBO cbo = null;
		CtcenterVO[] ctvos = null;

		try {
			cbo = getCtcenterBO();
			ctvos = cbo.queryAll(sCorpID, sCalbodyID);
		} catch (CMBusinessException e) {
			throw e;
		} catch (Exception ee) {
			throw new CMBusinessException(ee.getMessage(), ee);
		} finally {

			if (cbo != null) {
				cbo = null;
			}

		}
		return ctvos;
	}

	// 单据号BO
	private BillcodeRuleBO getBillcodeRuleBO() throws Exception {
		// BillcodeRuleHome home = (BillcodeRuleHome)
		// getBeanHome(BillcodeRuleHome.class,
		// "nc.bs.pub.billcodemanage.BillcodeRuleBO");
		// BillcodeRule boBillcodeRule = home.create();
		//
		// return boBillcodeRule;
		return new BillcodeRuleBO();
	}

	/**
	 * 函数功能:获得BOM值
	 * 
	 * 参数: 公司 库存组织 用户 存货基本ID 日期 计量单位
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 */
	public DisassembleVO[] getBOMNoParas(String[] sSysinfos) throws CMBusinessException {
		CommonDataBO cbo = null;
//		DisassembleBO dsbo = null;

		IPdToCm pdToCm=(IPdToCm)NCLocator.getInstance().lookup(IPdToCm.class.getName());
		DisassembleVO[] dvos = null;
		

		try {
			cbo = getCommonDataBO();
//			dsbo = getDisassembleBO();
			// DisassembleDMO dDMO = new DisassembleDMO();

			String sCorpID = sSysinfos[0];
			String sCalbodyID = sSysinfos[1];
			String sUserID = sSysinfos[2];
			String sInvBasID = sSysinfos[3];
			String sDate = sSysinfos[4];
			String sMeaID = sSysinfos[5];

			DisConditionVO dvo = new DisConditionVO();
			dvo.setPk_corp(sCorpID); // 公司ID
			dvo.setGcbm(sCalbodyID); // 工厂ID
			dvo.setOperid(sUserID); // 操作员ID

			dvo.setWlbmid(sInvBasID); // 物料ID(集团基本档案)
			dvo.setJldwid(sMeaID); // 计量单位ID

			dvo.setId("A"); // A:物料；B:分类
			dvo.setSl(new UFDouble(1)); // 数量
			dvo.setLogdate(new UFDate(sDate)); // 登录日期

			dvo.setYxrq(new UFDate(sDate)); // 有效日期
			dvo.setCm(new Integer(0)); // 层码
			dvo.setSfsh(new UFBoolean("Y")); // 是否考虑损耗
			dvo.setSffp(new UFBoolean("N")); // 是否考虑废品
			dvo.setSfplgz(new UFBoolean("N")); // 是否考虑批量规则

			// 取生产BOM
			dvos = pdToCm.getChildCM(dvo);
		} catch (CMBusinessException ee) {
			throw ee;
		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException(e.getMessage(), e);
		}

		return dvos;
	}

	/**
	 * 函数功能:获得成本的成本路线，返回按顺序的成本中心PK
	 * 
	 * 参数: 公司 库存组织 用户 存货基本ID 计量单位ID 日期
	 * 
	 * 返回值:按顺序的成本中心PK
	 * 
	 * 异常:
	 * 
	 * 创建日期：(2003-6-3 10:42:24)
	 */
	public CtcenterVO[] getCMRoutes(String[] sSysinfos) throws nc.vo.cm.pub.CMBusinessException {
		CommonDataBO cbo = null;
		CtcenterVO[] cvos = null;
		try {
			cbo = getCommonDataBO();

			DisRouteVO[] dvos = getRoutes(sSysinfos);

			// 成本中心类型1：部门 2：工作中心
			String sCTCenterType = cbo.getParaValue(sSysinfos[0], sSysinfos[1], ConstVO.m_iPara_CBZXFW);

			ArrayList vTemp = new ArrayList();
			ArrayList vKeys = new ArrayList();

			for (int i = dvos.length - 1; i >= 0; i--) {
				DisRouteVO dvo = dvos[i];
				CtcenterVO cvo = null;
				String sWK = dvo.getGzzxid();

				if (sCTCenterType.equals(ConstVO.m_sPara_GetCtcenterByWk)) {
					// 成本中心按工作中心定义
					CtcenterVO[] thisvos = getCTCenterForDept(sSysinfos[0], sSysinfos[1], null, sWK);

					if (thisvos != null && thisvos.length != 0) {
						cvo = thisvos[0];
					}
				} else {
					// 成本中心按部门定义
					String sSQL = " select ";
					sSQL = sSQL + " ssbmid ";
					sSQL = sSQL + " from ";
					sSQL = sSQL + " pd_wk ";
					sSQL = sSQL + " where ";
					sSQL = sSQL + " pk_wkid = '" + sWK + "'";

					String[][] sResult = cbo.queryDataNoTranslate(sSQL);

					CtcenterVO[] thisvos = getCTCenterForDept(sSysinfos[0], sSysinfos[1], sResult[0][0], null);
					if (thisvos != null && thisvos.length != 0) {
						cvo = thisvos[0];
					}
				}

				if (cvo != null && vKeys.contains(cvo.getPrimaryKey()) == false) {
					if (vTemp.size() == 0) {
						vTemp.add(cvo);
					} else {
						vTemp.add(0, cvo);
					}

					vKeys.add(cvo.getPrimaryKey());
				}
			}

			if (vTemp.size() != 0) {
				cvos = new CtcenterVO[vTemp.size()];
				cvos = (CtcenterVO[]) vTemp.toArray(cvos);
			}
		} catch (CMBusinessException e) {
			throw e;
		} catch (Exception ee) {
			throw new CMBusinessException(ee.getMessage(), ee);
		} finally {

			if (cbo != null) {
				cbo = null;
			}

		}

		return cvos;
	}

	/**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * @return nc.bs.ia.pub.CommonData
	 */
	private CommonDataBO getCommonDataBO() throws Exception {

		return new CommonDataBO();
	}

	/**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 创建日期：(2003-5-29 10:22:53)
	 */
	private String getCTCenter(CtcenterVO[] ctcentervos, String sDeptID, String sWkID) {
		String spk = null;

		CtcenterVO vo = null;
		if (ctcentervos == null || ctcentervos.length <= 0)
			return null;

		for (int i = 0, iLength = ctcentervos.length; i < iLength; i++) {
			CtcenterItemVO[] vosCtcenterItemVO = (CtcenterItemVO[]) (ctcentervos[i].getChildrenVO());
			if (vosCtcenterItemVO == null) {
				continue;
			}
			for (int j = 0, jLength = vosCtcenterItemVO.length; j < jLength; j++) {
				if (vosCtcenterItemVO[j] == null) {
					continue;
				}
				if (sDeptID != null && vosCtcenterItemVO[j].getpk_deptdoc() != null) {
					if (sDeptID.equals(vosCtcenterItemVO[j].getpk_deptdoc())) {
						spk = vosCtcenterItemVO[j].getpk_ctcenter();
						break;
					}
				} else if (sWkID != null && vosCtcenterItemVO[j].getpk_wkc() != null) {
					if (sWkID.equals(vosCtcenterItemVO[j].getpk_wkc())) {
						spk = vosCtcenterItemVO[j].getpk_ctcenter();
						break;
					}
				}
			}

		}

		return spk;
	}

	/**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 创建日期：(2003-5-29 10:22:53)
	 */
	private String getCTCenter(CtcenterVO[] ctcentervos, String sDeptID, String sWkID, int sfjbscflag// 
	) {
		String spk = null;

		CtcenterVO vo = null;
		if (ctcentervos == null || ctcentervos.length <= 0)
			return null;

		for (int i = 0, iLength = ctcentervos.length; i < iLength; i++) {

			// 是否基本生产
			if (Integer.parseInt(ctcentervos[i].getsfjbscflag().toString()) != sfjbscflag) {
				continue;
			}
			CtcenterItemVO[] vosCtcenterItemVO = (CtcenterItemVO[]) (ctcentervos[i].getChildrenVO());
			for (int j = 0, jLength = vosCtcenterItemVO.length; j < jLength; j++) {
				if (sDeptID != null) {
					if (sDeptID.equals(vosCtcenterItemVO[j].getpk_deptdoc())) {
						spk = vosCtcenterItemVO[j].getpk_ctcenter();
						break;
					}
				} else {
					if (sWkID.equals(vosCtcenterItemVO[j].getpk_wkc())) {
						spk = vosCtcenterItemVO[j].getpk_ctcenter();
						break;
					}
				}
			}

		}

		return spk;
	}

	/**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 创建日期：(2003-5-29 10:22:53)
	 */
	private String getCTCenter(CtcenterVO[] ctcentervos, String sDeptID, String sWkID, int sfjbscflag,//
			String blastlevelflag) {
		String spk = null;

		CtcenterVO vo = null;
		if (ctcentervos == null || ctcentervos.length <= 0)
			return null;

		for (int i = 0, iLength = ctcentervos.length; i < iLength; i++) {
			// 是否末级
			if (!(ctcentervos[i].getBlastlevelflag().toString().trim().equals("Y"))) {
				continue;
			}
			// 是否基本生产
			if (Integer.parseInt(ctcentervos[i].getsfjbscflag().toString()) != sfjbscflag) {
				continue;
			}
			CtcenterItemVO[] vosCtcenterItemVO = (CtcenterItemVO[]) (ctcentervos[i].getChildrenVO());
			for (int j = 0, jLength = vosCtcenterItemVO.length; j < jLength; j++) {
				if (sDeptID != null) {
					if (sDeptID.equals(vosCtcenterItemVO[j].getpk_deptdoc())) {
						spk = vosCtcenterItemVO[j].getpk_ctcenter();
						break;
					}
				} else {
					if (sWkID.equals(vosCtcenterItemVO[j].getpk_wkc())) {
						spk = vosCtcenterItemVO[j].getpk_ctcenter();
						break;
					}
				}
			}

		}

		return spk;
	}

	/**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * @return nc.bs.ia.pub.CommonData
	 */
	private CtcenterBO getCtcenterBO() throws Exception {

		return new CtcenterBO();
	}

	/**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 创建日期：(2003-5-29 10:22:53)
	 */
	public CtcenterVO[] getCTCenterForDept(String sCorpID, String sCalbodyID, String sDeptID, String sWkID) throws nc.vo.cm.pub.CMBusinessException {
		CtcenterBO cbo = null;
		CtcenterVO[] ctvos = null;

		try {
			cbo = getCtcenterBO();

			CtcenterVO ctvo = new CtcenterVO();
			ctvo.setPk_corp(sCorpID);
			ctvo.setPk_calbody(sCalbodyID);
			ctvo.setPk_deptdoc(sDeptID);
			ctvo.setPk_wk(sWkID);

			ctvos = cbo.queryByVO(ctvo, new Boolean(true));
		} catch (CMBusinessException e) {
			throw e;
		} catch (Exception ee) {
			throw new CMBusinessException(ee.getMessage(), ee);
		} finally {

			if (cbo != null) {
				cbo = null;
			}

		}
		return ctvos;
	}

	/**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 创建日期：(2003-5-29 10:22:53)
	 */
	public String[] getCTCenterForM2(CtcenterVO[] ctcentervos, String sDeptID, String sWkID) throws nc.vo.cm.pub.CMBusinessException {
		String spk = null;
		String sctcode = null;
		String sctname = null;
		CtcenterVO vo = null;
		if (ctcentervos == null || ctcentervos.length <= 0)
			return null;
		// 先根据工作中心查询
		for (int i = 0, iLength = ctcentervos.length; i < iLength; i++) {
			CtcenterItemVO[] vosCtcenterItemVO = (CtcenterItemVO[]) (ctcentervos[i].getChildrenVO());
			for (int j = 0, jLength = vosCtcenterItemVO.length; j < jLength; j++) {
				if (sWkID != null && vosCtcenterItemVO[j].getpk_wk() != null) {
					if (sWkID.equals(vosCtcenterItemVO[j].getpk_wkc())) {
						spk = vosCtcenterItemVO[j].getpk_ctcenter();
						sctcode = ctcentervos[i].getCctcentercode();
						sctname = ctcentervos[i].getCctcentername();
						break;
					}
				}
			}

		}

		// 如果没有找到,根据部门查询
		if (spk == null) {
			for (int i = 0, iLength = ctcentervos.length; i < iLength; i++) {
				CtcenterItemVO[] vosCtcenterItemVO = (CtcenterItemVO[]) (ctcentervos[i].getChildrenVO());
				for (int j = 0, jLength = vosCtcenterItemVO.length; j < jLength; j++) {

					if (sDeptID != null && vosCtcenterItemVO[j].getpk_ctcenter() != null) {
						if (sDeptID.equals(vosCtcenterItemVO[j].getpk_deptdoc())) {
							spk = vosCtcenterItemVO[j].getpk_ctcenter();
							sctcode = ctcentervos[i].getCctcentercode();
							sctname = ctcentervos[i].getCctcentername();
							break;
						}
					}
				}

			}
		}

		return new String[] { spk, sctcode, sctname };
	}

	/**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 创建日期：(2003-5-29 10:22:53)
	 */
	private String getCTCenterForM2(String[][] sAllCtcenterPKs, String sDeptID, String sWkID) throws Exception {
		String spk_ctcenter = null;

		if (sAllCtcenterPKs == null || sAllCtcenterPKs.length <= 0)
			return null;
		// 先根据工作中心查询
		for (int i = 0, iLength = sAllCtcenterPKs.length; i < iLength; i++) {
			if (sWkID != null && sAllCtcenterPKs[i][1] != null) {
				if (sWkID.equals(sAllCtcenterPKs[i][1])) {
					spk_ctcenter = sAllCtcenterPKs[i][0];
					break;
				}
			}

		}
		// 如果没有找到,根据部门查询
		if (spk_ctcenter == null) {
			for (int i = 0, iLength = sAllCtcenterPKs.length; i < iLength; i++) {
				if (sDeptID != null && sAllCtcenterPKs[i][2] != null) {
					if (sDeptID.equals(sAllCtcenterPKs[i][2])) {
						spk_ctcenter = sAllCtcenterPKs[i][0];
						break;
					}
				}

			}
		}

		return spk_ctcenter;
	}

	/**
	 * 函数功能:获得总帐的数据
	 * 
	 * 参数: String sCorpID ----- 公司 String sAccSubj ----- 科目 String sCurrType
	 * ----- 币种 String strYear ----- 会计年 String strMonth ----- 会计月
	 * nc.vo.glcom.ass.AssVO[] avos ----- 辅助核算 Integer id ----- 辅助核算的id String
	 * sFlag ----- QC:期初余额 QM:期末余额 JFFS:借方发生额 DFFS:贷方发生额 JE:净发生额
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 
	 */
	public UFDouble getGLData(String sCorpID, String sAccSubj, String sCurrType, String strYear, String strMonth, nc.vo.glcom.ass.AssVO[] avos, Integer id, String sFlag) throws CMBusinessException {
		UFDouble dReturnValue = new UFDouble(0);

		// nc.bs.glcom.balance.GlBalance gbo = null;

		ICommAccBookPub gbo = (ICommAccBookPub) NCLocator.getInstance().lookup(ICommAccBookPub.class.getName());

		try {

			// nc.bs.glcom.balance.GlBalanceHome home =
			// (nc.bs.glcom.balance.GlBalanceHome)
			// getBeanHome(nc.bs.glcom.balance.GlBalanceHome.class,
			// "nc.bs.glcom.balance.GlBalanceBO");
			// gbo = home.create();

			nc.vo.glcom.balance.GlQueryVO queryvo = new nc.vo.glcom.balance.GlQueryVO();
			nc.vo.glcom.balance.GlBalanceVO[] gvos = null;

			// 公司
			String[] sCorps = new String[1];
			sCorps[0] = sCorpID;
			queryvo.setPk_corp(sCorps);

			// 科目
			String[] sSubj = new String[1];
			sSubj[0] = sAccSubj;
			queryvo.setPk_accsubj(sSubj);

			// 币种
			queryvo.setPk_currtype(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002033381", "UPP2002033381-000024")/* "本币" */);

			// 是否按期间查询
			queryvo.setQueryByPeriod(true);

			// 会计年度
			queryvo.setYear(strYear);

			// 会计期间(即会计月)
			queryvo.setPeriod(strMonth);

			// 终止会计期间
			queryvo.setEndPeriod(strMonth);

			// 是否包含未记帐
			queryvo.setIncludeUnTallyed(false);

			// 是否包含实时凭证
			queryvo.setRealtimeVoucher(false);

			// 是否显示无期初无发生额记录
			queryvo.setShowZeroAmountRec(false);

			// 分组信息
			queryvo.setGroupFields(new int[] { nc.vo.glcom.balance.GLQueryKey.K_GLQRY_CORP, nc.vo.glcom.balance.GLQueryKey.K_GLQRY_SUBJ });

			// 辅助项
			queryvo.setAssVos(avos);

			if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLQC)) {
				// 取期初余额
				gvos = gbo.getBeginBalance(queryvo);

				if (gvos != null && gvos.length > 0) {
					// 科目方向 1 : 借方 2 : 贷方
					for (int i = 0; i < gvos.length; i++) {
						Integer iAccOrient = gvos[i].getAccOrient();
						if (iAccOrient.intValue() == 1)
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount().sub(gvos[i].getLocalcreditamount()));
						else
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount().sub(gvos[i].getLocaldebitamount()));
					}
				}
			} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLQM)) {
				// 取期末余额
				gvos = gbo.getEndBalance(queryvo);

				if (gvos != null && gvos.length > 0) {
					// 科目方向 1 : 借方 2 : 贷方
					for (int i = 0; i < gvos.length; i++) {
						Integer iAccOrient = gvos[i].getAccOrient();
						if (iAccOrient.intValue() == 1)
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount().sub(gvos[i].getLocalcreditamount()));
						else
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount().sub(gvos[i].getLocaldebitamount()));
					}
				}
			} else {
				// 取发生额
				gvos = gbo.getOccur(queryvo);

				if (gvos != null && gvos.length > 0) {
					if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLJFFS)) {
						// 借方金额
						for (int i = 0; i < gvos.length; i++) {
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount());
						}
					} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLDFFS)) {
						// 贷方金额
						for (int i = 0; i < gvos.length; i++) {
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount());
						}
					} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLJE)) {
						// 净金额 = 借 － 贷(借) 或者 贷 - 借(贷)
						UFDouble dDebit = new UFDouble(0);
						UFDouble dCrebit = new UFDouble(0);
						for (int i = 0; i < gvos.length; i++) {
							if (gvos[i].getLocaldebitamount() != null) {
								dDebit = gvos[i].getLocaldebitamount();
							}

							if (gvos[i].getLocalcreditamount() != null) {
								dCrebit = gvos[i].getLocalcreditamount();
							}

							// 科目方向 1 : 借方 2 : 贷方
							Integer iAccOrient = gvos[i].getAccOrient();
							if (iAccOrient.intValue() == 1)
								dReturnValue = dReturnValue.add(dDebit.sub(dCrebit));
							else
								dReturnValue = dReturnValue.add(dCrebit.sub(dDebit));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new nc.vo.cm.pub.CMBusinessException(e.getMessage());

		}

		return dReturnValue;

	}

	/**
	 * 函数功能:获得总帐的数据
	 * 
	 * 参数: String sCorpID ----- 公司 String sAccSubj ----- 科目 //String sCurrType
	 * ----- 币种 String strYear ----- 会计年 String strMonth ----- 会计月
	 * nc.vo.glcom.ass.AssVO[] avos ----- 辅助核算 //Integer id ----- 辅助核算的id String
	 * sFlag ----- QC:期初余额 QM:期末余额 JFFS:借方发生额 DFFS:贷方发生额 JE:净发生额
	 * 
	 * UFBoolean bOnlyDebit : 是否只取借方余额 （true ：只取借方余额 false ：只取贷方余额 null ：都取，不限制）
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 
	 */
	public UFDouble getGLData(String sCorpID, String[] sAccSubjs, String strYear, String strMonth, nc.vo.glcom.ass.AssVO[] avos, String sFlag, UFBoolean bOnlyDebit) throws CMBusinessException {
		UFDouble dReturnValue = new UFDouble(0);

		// nc.bs.glcom.balance.GlBalance gbo = null;
		ICommAccBookPub gbo = (ICommAccBookPub) NCLocator.getInstance().lookup(ICommAccBookPub.class.getName());

		try {

			//
			//
			// nc.bs.glcom.balance.GlBalanceHome home =
			// (nc.bs.glcom.balance.GlBalanceHome)
			// getBeanHome(nc.bs.glcom.balance.GlBalanceHome.class,
			// "nc.bs.glcom.balance.GlBalanceBO");
			// gbo = home.create();

			nc.vo.glcom.balance.GlQueryVO queryvo = new nc.vo.glcom.balance.GlQueryVO();
			nc.vo.glcom.balance.GlBalanceVO[] gvos = null;

			// 公司
			String[] sCorps = new String[1];
			sCorps[0] = sCorpID;
			// 总帐更改引起变动，modified by liming
			// queryvo.setPk_corp(sCorps);

			// 从公司pk取主体帐簿pk
			// GLOrgBookAccBO bo = new GLOrgBookAccBO();
			// String pk_glorgbook =
			// bo.getDefaultGlOrgBookVOByPk_EntityOrg(sCorpID).getPrimaryKey();

			IGLOrgBookAcc glOrgBookAcc = (IGLOrgBookAcc) NCLocator.getInstance().lookup(IGLOrgBookAcc.class.getName());
			String pk_glorgbook = glOrgBookAcc.getDefaultGlOrgBookVOByPk_EntityOrg(sCorpID).getPrimaryKey();

			String[] sGlOrgBook = new String[1];
			sGlOrgBook[0] = pk_glorgbook;
			queryvo.setPk_glorgbook(sGlOrgBook);

			queryvo.setPk_accsubj(sAccSubjs);

			// 币种
			queryvo.setPk_currtype(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002033381", "UPP2002033381-000024")/* "本币" */);

			// 是否按期间查询
			queryvo.setQueryByPeriod(true);

			// 会计年度
			queryvo.setYear(strYear);

			// 会计期间(即会计月)
			queryvo.setPeriod(strMonth);

			// 终止会计期间
			queryvo.setEndPeriod(strMonth);

			// 是否包含未记帐
			queryvo.setIncludeUnTallyed(false);

			// 是否包含实时凭证
			queryvo.setRealtimeVoucher(false);

			// 是否显示无期初无发生额记录
			queryvo.setShowZeroAmountRec(false);

			// 分组信息
			// queryvo.setGroupFields(new int[] {
			// nc.vo.glcom.balance.GLQueryKey.K_GLQRY_CORP,
			// nc.vo.glcom.balance.GLQueryKey.K_GLQRY_SUBJ });
			queryvo.setGroupFields(new int[] { nc.vo.glcom.balance.GLQueryKey.K_GLQRY_PK_GLORGBOOK, nc.vo.glcom.balance.GLQueryKey.K_GLQRY_SUBJ });

			// 辅助项
			queryvo.setAssVos(avos);

			if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLQC)) {
				// 取期初余额
				gvos = gbo.getBeginBalance(queryvo);

				if (gvos != null && gvos.length > 0) {
					// 科目方向 1 : 借方 2 : 贷方
					for (int i = 0; i < gvos.length; i++) {
						Integer iAccOrient = gvos[i].getAccOrient();
						if ((bOnlyDebit == null || bOnlyDebit.booleanValue()) && iAccOrient.intValue() == 1)
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount().sub(gvos[i].getLocalcreditamount()));
						else if ((bOnlyDebit == null || !bOnlyDebit.booleanValue()) && iAccOrient.intValue() == 0)
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount().sub(gvos[i].getLocaldebitamount()));
						else
							dReturnValue = new UFDouble(0);
					}
				}
			} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLQM)) {
				// 取期末余额
				gvos = gbo.getEndBalance(queryvo);

				if (gvos != null && gvos.length > 0) {
					// 科目方向 1 : 借方 2 : 贷方
					for (int i = 0; i < gvos.length; i++) {
						Integer iAccOrient = gvos[i].getAccOrient();
						if ((bOnlyDebit == null || bOnlyDebit.booleanValue()) && iAccOrient.intValue() == 1)
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount().sub(gvos[i].getLocalcreditamount()));
						else if ((bOnlyDebit == null || !bOnlyDebit.booleanValue()) && iAccOrient.intValue() == 0)
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount().sub(gvos[i].getLocaldebitamount()));
						else
							dReturnValue = new UFDouble(0);
					}
				}
			} else {
				// 取发生额
				gvos = gbo.getOccur(queryvo);

				if (gvos != null && gvos.length > 0) {
					if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLJFFS)) {
						// 借方金额
						for (int i = 0; i < gvos.length; i++) {
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount());
						}
					} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLDFFS)) {
						// 贷方金额
						for (int i = 0; i < gvos.length; i++) {
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount());
						}
					} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLJE)) {
						// 净金额 = 借 － 贷(借) 或者 贷 - 借(贷)
						UFDouble dDebit = new UFDouble(0);
						UFDouble dCrebit = new UFDouble(0);
						for (int i = 0; i < gvos.length; i++) {
							if (gvos[i].getLocaldebitamount() != null) {
								dDebit = gvos[i].getLocaldebitamount();
							}

							if (gvos[i].getLocalcreditamount() != null) {
								dCrebit = gvos[i].getLocalcreditamount();
							}

							// 科目方向 1 : 借方 2 : 贷方
							Integer iAccOrient = gvos[i].getAccOrient();
							if (iAccOrient.intValue() == 1)
								dReturnValue = dReturnValue.add(dDebit.sub(dCrebit));
							else
								dReturnValue = dReturnValue.add(dCrebit.sub(dDebit));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new nc.vo.cm.pub.CMBusinessException(e.getMessage());

		}

		return dReturnValue;

	}

	// 得到产品的参数信息6是否使用成本中心统计产量,7是否使用工艺路线
	private boolean[] getProducePara(String[][] products, String pk_invmandoc) {
		// a.pk_invmandoc 0, a.pk_invbasdoc1,b.pk_produce2, b.sfcbdx3,
		// c.invname4,c.invcode5,b.isctoutput6,b.isuseroute7
		boolean[] bPara = null;
		for (int i = 0; i < products.length; i++) {
			if (products[i][0].equals(pk_invmandoc)) {
				bPara = new boolean[2];
				bPara[0] = products[i][6].equals("Y") ? true : false;
				bPara[1] = products[i][7].equals("Y") ? true : false;
			}
		}
		return bPara;
	}

	/**
	 * 函数功能:获得存货产成品入库单据
	 * 
	 * 参数: sSysinfos[0] ----- 公司 sSysinfos[1] ----- 工厂 sSysinfos[2] ----- 起始日期
	 * sSysinfos[3] ----- 结束日期 sSysinfos[4] ----- 单据类型,"I3" 产成品入库单 "I6" 材料出库单
	 * sSysinfos[5] ----- 当前用户 sSysinfos[6] ----- 当前日期 sSysinfos[7] ----- 会计年
	 * sSysinfos[8] ----- 会计月 sSysinfos[9] ----- 是否按成本中心统计产量 String[] sRDCLs
	 * ----- 入库类别 String[] sInvs ----- 存货 UFBoolean isWaster ---- 是否取废品
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 
	 */
	public String getIAI3Bills(String[] sSysinfos, String[] sRDCLs, String[] sInvs, UFBoolean isWaster) throws nc.vo.cm.pub.CMBusinessException {

		// nc.bs.ia.outter.Outter boOutter = null;
		IIAToCA iaToCa = null;
		ProductVO[] vosProductVO = null;
		CommonDataBO boCommonData = null;
		BillcodeRuleBO boBillcodeRuleBO = null;
		// WithOuter boWithOuter = null;

		String sErr = "";
		try {
			String sBillTypecode = sSysinfos[4];
			boCommonData = getCommonDataBO();
			// boWithOuter = getWithOuterBO();
			iaToCa = (IIAToCA) NCLocator.getInstance().lookup(IIAToCA.class.getName());
			long t1 = System.currentTimeMillis();
			/**将数据转换为接口所需的数据
			 * 
			 * 	sSysinfos[0] = getPk_Corp();
			sSysinfos[1] = getPk_Calbody();
			sSysinfos[2] = sStartDate;
			sSysinfos[3] = sEndDate;
			sSysinfos[4] = "I3";
			sSysinfos[5] = getPk_User();
			sSysinfos[6] = getBusinessDate().toString();
			sSysinfos[7] = sAcYear;
			sSysinfos[8] = sAcMonth;
*/
			String[] sInterfaceData=new String[6];
			sInterfaceData[0] = sSysinfos[0];
			sInterfaceData[1] = sSysinfos[1];
			sInterfaceData[2] = sSysinfos[2];
			sInterfaceData[3] = sSysinfos[3];
			sInterfaceData[4] = sSysinfos[7];
			sInterfaceData[5] = sSysinfos[8];
			
			
			nc.vo.ia.bill.BillVO[] vosIABillVO = iaToCa.getCCPBills(sInterfaceData, sRDCLs, sInvs);
			long t2 = System.currentTimeMillis();
			String sTime = getTime(t1, t2);
			ConstVO.outputString("存货核算接口耗时:" + sTime);
			// 判断是否启用了订单 String sCorpID,String sModule
			boolean isMoStarted = boCommonData.isModuleStarted(sSysinfos[0], ConstVO.m_sModuleMO).booleanValue();

			if (vosIABillVO != null) {
				String sCorpID = sSysinfos[0];
				String sCalbodyID = sSysinfos[1];
				/** **************************查询出所有的废品仓库*************************************** */
				// 查询出所有的废品仓库
				String sSQL = " select  pk_stordoc  from  bd_stordoc  where  pk_corp = '" + sSysinfos[0] + "' and  pk_calbody = '" + sSysinfos[1] + "' and  gubflag = 'Y' ";
				String[][] sResult = boCommonData.queryData(sSQL);
				HashSet hsWasterStors = new HashSet();
				if (sResult.length != 0) {
					for (int i = 0; i < sResult.length; i++) {
						hsWasterStors.add(sResult[i][0]);
					}
				}

				/** **************************获得所有存货的基本ID和生产ID*************************************** */
				//
				sSQL = " select  a.pk_invmandoc, a.pk_invbasdoc,b.pk_produce, b.sfcbdx,c.invname,c.invcode,b.isctoutput,b.isuseroute  from  bd_invmandoc a,bd_produce b,bd_invbasdoc c";
				sSQL = sSQL + " where  b.sfcbdx='Y' ";
				sSQL = sSQL + " and b.pk_calbody = '" + sSysinfos[1] + "' ";
				sSQL = sSQL + " and a.pk_invmandoc = b.pk_invmandoc and b.pk_invbasdoc=c.pk_invbasdoc ";
				String[][] sInvPKs = boCommonData.queryData(sSQL);
				/** ****************************获得所有投放，完工的订单ID**************************************************************** */
				String[][] sOrders = null;
				if (isMoStarted == true) {
					// 获得所有投放，完工的订单ID
					sSQL = " select mo.scddh, mo.pk_moid,mo.wlbmid,mo.pk_produce ";
					sSQL = sSQL + " from  mm_mo mo inner join bd_produce b on mo.pk_produce=b.pk_produce";
					sSQL = sSQL + " where b.iscostbyorder='Y' and mo.dr = 0";
					sSQL = sSQL + " and mo.pk_corp = '" + sSysinfos[0] + "'";
					sSQL = sSQL + " and  mo.gcbm = '" + sSysinfos[1] + "'";
					sSQL = sSQL + " and  (mo.zt = 'B' or mo.zt = 'C' or mo.zt='D') ";
					// sSQL+=" and sjkgrq<='"+sSysinfos[3]+"'";
					sOrders = boCommonData.queryData(sSQL);
				}

				/** ****************************获得所有的成本中心和部门**************************************************** */

				// 获得所有的成本中心
				sSQL = ("select a.pk_ctcenter,b.pk_wk,b.pk_deptdoc  ");
				sSQL += (" from cm_ctcenter a, cm_ctcenter_b b  ");
				sSQL += (" where a.pk_ctcenter=b.pk_ctcenter ");
				sSQL += (" and	a.pk_corp = '" + sCorpID + "'  ");
				sSQL += (" and 	a.pk_calbody = '" + sCalbodyID + "'  ");
				sSQL += " and a.blastlevelflag = 'Y' and a.jbscflag = 0";
				String[][] sAllCtcenterPKs = boCommonData.queryData(sSQL);
				// 部门
				String[][] sDepts = null;
				sSQL = " select a.deptcode,a.deptname,a.pk_deptdoc from bd_deptdoc a ";
				sDepts = boCommonData.queryData(sSQL);

				/** *******************如果启用了工艺路线,取得所有联副产品对应的主产品********************************************************************* */
				//
				HashMap mapByproducts = null;
				// if (sSysinfos[9].equals("Y") && sSysinfos[10].equals("Y")) {
//				if (sSysinfos[10].equals("Y")) {
					sSQL = "select  b.pk_produce,a.pk_produce ";
					sSQL = sSQL + "from bd_bom a inner join bd_bom_b b on a.pk_bomid=b.pk_bomid ";
					sSQL = sSQL + "where a.pk_corp='" + sSysinfos[0] + "'  ";
					sSQL = sSQL + "and b.zxlx in (5,6) ";
					sSQL = sSQL + "and a.gcbm='" + sSysinfos[1] + "'  ";
					sSQL = sSQL + "and a.sfmr='Y' ";
					sSQL = sSQL + "and b.sdate<='" + sSysinfos[2] + "' ";
					sSQL = sSQL + "and b.edate>='" + sSysinfos[3] + "' ";
					String[][] pk_produces = boCommonData.queryData(sSQL);
					if (pk_produces != null && pk_produces.length > 0) {
						mapByproducts = new HashMap();
						for (int i = 0; i < pk_produces.length; i++) {
							mapByproducts.put(pk_produces[i][0], pk_produces[i][1]);
						}
					}
//				}
				ArrayList aryListvosProductVO = new ArrayList();

				HashMap hmPkAndiPos = new HashMap();
				Integer iPos = null;
				String sOrderID = null;
				String sCtcenterID = null;

				/** ****************************开始数据处理************************************************************* */
				for (int i = 0; i < vosIABillVO.length; i++) {
					nc.vo.ia.bill.BillHeaderVO voBillHeaderVO = (nc.vo.ia.bill.BillHeaderVO) vosIABillVO[i].getParentVO();
					nc.vo.ia.bill.BillItemVO[] vosBillItemVO = (nc.vo.ia.bill.BillItemVO[]) vosIABillVO[i].getChildrenVO();
					// 条件判断
					if (voBillHeaderVO == null) {
						// sErr += "没有得到存货单据表头!!\n";
						continue;
					}

					if (voBillHeaderVO.getCwarehouseid() == null || hsWasterStors.contains(voBillHeaderVO.getCwarehouseid()) != isWaster.booleanValue()) {
						// sErr += "仓库不能为空,且不能为废品库!!\n";
						sErr += voBillHeaderVO.getVbillcode() + ":" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000004")/* @res""仓库不能为空,且不能为废品库""*/ 
								+ "\n";

						continue; // 仓库为空不取；废品取废品库，完工取非废品库
					}

					/** ***********************子表处理*********************************************** */
					for (int j = 0; j < vosBillItemVO.length; j++) {
						ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
						ProductItemVO voProductItemVO = new ProductItemVO();
						/** ***********************存货处理********************************************** */
						// 存货管理ID
						String sProductManID = vosBillItemVO[j].getCinventoryid();
						String sProductBasID = "";
						String sProductProduceID = "";
						String sSFCBDX = "N";
						String sinvname = null;
						String sinvcode = null;
						for (int m = 0, mLength = sInvPKs.length; m < mLength; m++) {
							if (sProductManID.equals(sInvPKs[m][0])) {
								voProductItemVO.setPk_invbasdoc(sInvPKs[m][1]); // 产品基本ID
								voProductItemVO.setPk_produce(sInvPKs[m][2]); // 产品生产ID
								sSFCBDX = sInvPKs[m][3];
								sinvname = sInvPKs[m][4];
								sinvcode = sInvPKs[m][5];
								break;
							}
						}
						// 返回值:bPara[0]是否按照成本中心统计产量bPara[1]是否启用工艺路线
						// private boolean[] getProducePara(String[][] products,
						// String pk_invmandoc)
						boolean[] bPara = getProducePara(sInvPKs, sProductManID);
						if (bPara == null) {
							sErr += (voBillHeaderVO.getVbillcode() + ":[" + sinvname + "][" + sinvcode + "]的生产档案没有定义参数:是否按照成本中心统计产量和是否启用工艺路线" + "\n");
							continue;
						}
						boolean isctoutput = bPara[0];
						boolean isuseroute = bPara[1];

						// 如果不启用工艺路线,并且按照成本中心统计产量则根据部门，工作中心对应成本中心
						if (isctoutput == true && isuseroute == false) {
							String ctcenterid = getCTCenterForM2(sAllCtcenterPKs, voBillHeaderVO.getCdeptid(), vosBillItemVO[j].getCwp());
							if (ctcenterid == null || ctcenterid.length() == 0) {
								String deptcode = null;
								String deptname = null;
								// 查找部门
								for (int m = 0; m < sDepts.length; m++) {
									if (sDepts[m][2].equals(voBillHeaderVO.getCdeptid())) {
										deptcode = sDepts[m][0];
										deptname = sDepts[m][1];
										break;
									}
								}
								// 没有获得对应的成本中心
								// sErr += "部门:" + deptname + "[" + deptcode +
								// "]" + "没有对应的成本中心\n";
								sErr += voBillHeaderVO.getVbillcode() + ":"
										+ nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000005", null, new String[] { "[" + deptname + ":" + deptcode + "]" })/* @res""部门{0}没有对应的成本中心"" */
										+ "\n";

								continue;
							}
							voProductHeaderVO.setPk_ctcenter(ctcenterid); // 成本中心
						} else if (isctoutput == true && isuseroute == true) {
							/** *******************如果按照成本中心统计产量,且启用了工艺路线对应出成本中心********************************************************************* */
							//
							// 获得最后成本中心
							CostRtVO crt = boCommonData.getCostRt(sSysinfos[0], sSysinfos[1], voProductItemVO.getPk_order(), voProductItemVO.getPk_produce(), sSysinfos[6]);

							sCtcenterID = null;
							if (crt != null) {
								CostRtHeaderVO hvo = (CostRtHeaderVO) crt.getParentVO();
								CostRtItemVO[] ivos = (CostRtItemVO[]) crt.getChildrenVO();
								sCtcenterID = ivos[ivos.length - 1].getCtcenterID();

							} else {
								// 去找对应的主产品
								if (mapByproducts != null && mapByproducts.size() > 0) {
									String pk_produce = mapByproducts.get(voProductItemVO.getPk_produce()) == null ? null : mapByproducts.get(voProductItemVO.getPk_produce()).toString();
									crt = boCommonData.getCostRt(sSysinfos[0], sSysinfos[1], voProductItemVO.getPk_order(), pk_produce, sSysinfos[6]);

									sCtcenterID = null;
									if (crt != null) {
										CostRtHeaderVO hvo = (CostRtHeaderVO) crt.getParentVO();
										CostRtItemVO[] ivos = (CostRtItemVO[]) crt.getChildrenVO();
										sCtcenterID = ivos[ivos.length - 1].getCtcenterID();

									} else {
										// sErr += ("[" + sinvname + "][" +
										// sinvcode + "]没有定义工艺路线!!\n");
										sErr += voBillHeaderVO.getVbillcode()
												+ ":"
												+ (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000007", null, new String[] { "[" + sinvname + "][" + sinvcode + "]" })/* @res""{0}没有定义工艺路线"" */+ "\n");

										continue;

									}
								} else {
									sErr += voBillHeaderVO.getVbillcode()
											+ ":"
											+ (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000007", null, new String[] { "[" + sinvname + "][" + sinvcode + "]" })/* @res""{0}没有定义工艺路线"" */+ "\n");
									continue;
								}

							}

							if (sCtcenterID == null) {
								sErr += voBillHeaderVO.getVbillcode() + ":" + "成本中心为空\n";
								continue;
							}

							voProductHeaderVO.setPk_ctcenter(sCtcenterID);
						}
						// 来源信息
						voProductItemVO.setCsourcebillid(voBillHeaderVO.getPrimaryKey());
						voProductItemVO.setCsourcebillitemid(vosBillItemVO[j].getPrimaryKey());
						voProductItemVO.setCsourcebilltype(sBillTypecode);
						voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleIA);
						// 公司
						voProductItemVO.setPk_corp(voBillHeaderVO.getPk_corp());
						// 工厂
						if("1078".equals(voBillHeaderVO.getPk_corp())){							
							voProductItemVO.setPk_calbody(voBillHeaderVO.getCstockrdcenterid());//add by hk
						}
						voProductItemVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
						// 来源单据号
						voProductItemVO.setVsourcebillcode(voBillHeaderVO.getVbillcode());
						// 数量
						if (vosBillItemVO[j].getNnumber() == null)
							continue;

						voProductItemVO.setNnumber(vosBillItemVO[j].getNnumber());

						/** ************************如果启用了订单,根据生产订单号获得主键、产品*************************************************** */
						if (isMoStarted == true) {
							sOrderID = null;

							if (vosBillItemVO[j].getVproducebatch() != null) {

								for (int m = 0, mLength = sOrders.length; m < mLength; m++) {
									if (vosBillItemVO[j].getVproducebatch().equals(sOrders[m][0])) {
										sOrderID = sOrders[m][1]; // 订单ID（生产批号）
										break;
									}
								}
							}
							if (sOrderID == null) {
								voProductItemVO.setPk_order(voProductItemVO.getPk_produce());
							} else {
								voProductItemVO.setPk_order(sOrderID); // 订单ID（生产批号）
							}

						}
						
						voBillHeaderVO.setDbilldate(new UFDate(sSysinfos[6]));

						// 判断是否存在表头
						iPos = (Integer) hmPkAndiPos.get(voProductHeaderVO.getPk_ctcenter() + voBillHeaderVO.getDbilldate());

						if (iPos != null) {
							ProductVO oldProductVO = (ProductVO) aryListvosProductVO.get(iPos.intValue());
							ProductItemVO[] oldItems = (ProductItemVO[]) oldProductVO.getChildrenVO();
							// 合并表体
							boolean isCombine = false;
							for (int m = 0, ilength = oldItems.length; m < ilength; m++) {
								// 判断订单相等
								if ((oldItems[m].getPk_order() == null && voProductItemVO.getPk_order() == null) // 1-同时为空
										|| ((oldItems[m].getPk_order() != null && voProductItemVO.getPk_order() != null) // 2-同时部位为空且
										&& (oldItems[m].getPk_order().trim().equals(voProductItemVO.getPk_order().trim())))) // 订单相等

								{
									// 判断产品是否相等
									if (//
									(oldItems[m].getPk_produce() != null && voProductItemVO.getPk_produce() != null) // 1-都不空且
											&& oldItems[m].getPk_produce().trim().equals(voProductItemVO.getPk_produce().trim()) // 产品相同
									) {
										// 合并ItemVO
										// 更改合并标志
										isCombine = true;
										// 数量相加
										oldItems[m].setNnumber(oldItems[m].getNnumber().add(voProductItemVO.getNnumber()));

									}
								}

							}
							// 如果没有合并,则追加一行
							if (isCombine == false) {
								ProductItemVO[] newItems = new ProductItemVO[oldItems.length + 1];
								System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);
								newItems[oldItems.length] = voProductItemVO;
								oldProductVO.setChildrenVO(newItems);
							}

						} else {

							ProductVO voProductVO = new ProductVO();
							ProductHeaderVO newProductHeaderVO = (ProductHeaderVO) voProductHeaderVO.clone();

							// 公司
							newProductHeaderVO.setPk_corp(voBillHeaderVO.getPk_corp());
							// 工厂（库存组织）
							if("1078".equals(voBillHeaderVO.getPk_corp())){
								newProductHeaderVO.setPk_calbody(voBillHeaderVO.getCstockrdcenterid());//add by hk
							}
							newProductHeaderVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
							// 会计年
							newProductHeaderVO.setCaccountyear(sSysinfos[7]);
							// 会计月
							newProductHeaderVO.setCaccountmonth(sSysinfos[8]);
							// 单据类型如果是废品则
							if (isWaster.booleanValue() == true) {
								newProductHeaderVO.setCbilltypecode(ConstVO.m_sBillFPSSJHS);
							} else {
								newProductHeaderVO.setCbilltypecode(ConstVO.m_sBillWGCPTJ);
							}

							// 单据日期
							newProductHeaderVO.setDbilldate(voBillHeaderVO.getDbilldate());
							// 制单日期
							newProductHeaderVO.setDbillmakedate(new UFDate(sSysinfos[6]));
							// 制单人
							newProductHeaderVO.setCbillmaker(sSysinfos[5]);
							// 备注
							newProductHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063045", "UPP-000113")/* @res""该单据来源于产成品入库单"" */);

							voProductVO.setParentVO(newProductHeaderVO);
							ProductItemVO[] stvos = new ProductItemVO[1];
							stvos[0] = voProductItemVO;
							voProductVO.setChildrenVO(stvos);
							aryListvosProductVO.add(voProductVO);
							hmPkAndiPos.put(newProductHeaderVO.getPk_ctcenter() + newProductHeaderVO.getDbilldate(), new Integer(aryListvosProductVO.size() - 1));

						}
					} // for (int j = 0; j < vosBillItemVO.length; j++) {
				}

				if (!aryListvosProductVO.isEmpty()) {
					/** **************************单据号处理************************************************* */
					boBillcodeRuleBO = getBillcodeRuleBO();
					String[] name = { "库存组织", "操作员" };
					String[] id = { sSysinfos[1].trim(), sSysinfos[5].trim() };
					BillCodeObjValueVO value = new BillCodeObjValueVO();
					value.setAttributeValue(name, id);
					String[] sbillcodes = null;
					// 如果是废品
					if (isWaster.booleanValue() == true) {
						sbillcodes = boBillcodeRuleBO.getBatchBillCodes(ConstVO.m_sBillFPSSJHS, sSysinfos[0], value, aryListvosProductVO.size());
					} else {
						sbillcodes = boBillcodeRuleBO.getBatchBillCodes(ConstVO.m_sBillWGCPTJ, sSysinfos[0], value, aryListvosProductVO.size());
					}

					vosProductVO = new ProductVO[aryListvosProductVO.size()];
					for (int i = 0; i < aryListvosProductVO.size(); i++) {
						vosProductVO[i] = (ProductVO) (aryListvosProductVO.get(i));
						((ProductHeaderVO) (vosProductVO[i].getParentVO())).setVbillcode(sbillcodes[i]);
					}

				}
				if (sErr.length() > 0) {
					ConstVO.outputString(sErr);
				}
				ProductBO boProductBO = new ProductBO();
				boProductBO.insertArray(vosProductVO);
			}

		} catch (CMBusinessException e) {
			throw e;
		} catch (Exception ee) {
			throw new CMBusinessException(ee.getMessage(), ee);
		}

		return sErr;
	}

	/**
	 * 函数功能:更新半成品价格
	 * 
	 * 参数:
	 * 
	 * sSysinfos[0] ----- 公司 sSysinfos[1] ----- 工厂 sSysinfos[2] ----- 起始日期
	 * sSysinfos[3] ----- 结束日期 sSysinfos[4] ----- 单据类型 "I6" 材料出库单 sSysinfos[5]
	 * ----- 当前用户 sSysinfos[6] ----- 当前日期 sSysinfos[7] ----- 会计年 sSysinfos[8]
	 * ----- 会计月 String[] sRDCLs ----- 入库类别 String[] sInvs ----- 存货
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 
	 */
	public String getIAI6Bills(String[] sSysinfos, String[] sRDCLs, String[] sInvs) throws nc.vo.cm.pub.CMBusinessException {

		// 提示信息
		StringBuffer errStrB = new StringBuffer("\n\n\n\n\n");
		// BOS
		// nc.bs.ia.outter.Outter boIAOutter = null;

		CommonDataBO boCommonData = null;
		// WithOuter boWithOuter = null;
		BillcodeRuleBO boBillcodeRuleBO = null;

		IIAToCA iaToCa = (IIAToCA) NCLocator.getInstance().lookup(IIAToCA.class.getName());

		// 转换后的材料
		StuffVO[] vosStuffVO = null;

		try {
			// 存放表体
			Vector vtItems = new Vector();
			// 表体
			Vector vtHeads = new Vector();
			String sCorpID = sSysinfos[0];
			String sCalbodyID = sSysinfos[1];
			String sBillTypecode = sSysinfos[4];

			boCommonData = getCommonDataBO();
			
			/**转换为接口的参数*/
			String[] sInterfaceData = new String[6];
			sInterfaceData[0] = sSysinfos[0];
			sInterfaceData[1] = sSysinfos[1];
			sInterfaceData[2] = sSysinfos[2];
			sInterfaceData[3] = sSysinfos[3];
			sInterfaceData[4] = sSysinfos[7];
			sInterfaceData[5] = sSysinfos[8];
		
			// 取数
//			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCPBills(sSysinfos, sRDCLs, sInvs);
			/**是否包含差异结存单*/
			String sSFBHYCJCD=(new CommonDataBO()).getParaValue(sCorpID, sCalbodyID, ConstVO.m_iPara_SFBHYCJCD);
			boolean bSFBHYCJCD="Y".equalsIgnoreCase(sSFBHYCJCD)?true:false;
//			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCCBills(sSysinfos, sRDCLs, sInvs,bSFBHYCJCD,false);
			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCCBills(sInterfaceData, sRDCLs, sInvs,bSFBHYCJCD,false);
			boolean isByDate = false;

			if (bvos != null && bvos.length > 0) {

				// 获得所有存货的基本ID和生产ID
				StringBuffer sSQL = new StringBuffer(300);
				sSQL.append("select  a.pk_invbasdoc ,  b.pk_produce ,  a.pk_invmandoc ,  b.jhj , ");
				sSQL.append(" c.invname ,  c.invcode,d.pk_invcl ");
				sSQL.append(" from bd_invmandoc a,bd_produce b ,bd_invbasdoc c ,bd_invcl d");
				sSQL.append(" where  a.pk_invbasdoc=c.pk_invbasdoc ");
				sSQL.append(" and a.pk_invmandoc = b.pk_invmandoc ");
				sSQL.append(" and c.pk_invcl=d.pk_invcl");
				sSQL.append(" and b.sfcbdx='Y'");
				sSQL.append(" and b.pk_corp = '" + sCorpID + "' ");
				sSQL.append(" and b.pk_calbody = '" + sCalbodyID + "' ");

				String[][] sAllInvBasProdPKs = boCommonData.queryData(sSQL.toString());
				// 取得所有订单
				sSQL.setLength(0);
				sSQL.append(" select mo.pk_moid, mo.wlbmid, mo.pk_produce, mo.scddh ");
				sSQL.append(" from  mm_mo mo inner join bd_produce b on mo.pk_produce=b.pk_produce");
				sSQL.append(" where b.iscostbyorder='Y' and mo.dr = 0");
				sSQL.append(" and mo.pk_corp = '" + sSysinfos[0] + "'");
				sSQL.append(" and mo.gcbm = '" + sSysinfos[1] + "'");

				String[][] sAllOrderPKs = boCommonData.queryData(sSQL.toString());

				// 获得所有的成本中心
				sSQL.setLength(0);
				sSQL.append("select a.pk_ctcenter,b.pk_wk,b.pk_deptdoc  ");
				sSQL.append(" from cm_ctcenter a, cm_ctcenter_b b  ");
				sSQL.append(" where a.pk_ctcenter=b.pk_ctcenter ");
				sSQL.append(" and	a.pk_corp = '" + sCorpID + "'  ");
				sSQL.append(" and 	a.pk_calbody = '" + sCalbodyID + "'  ");
				String[][] sAllCtcenterPKs = boCommonData.queryData(sSQL.toString());
				HashMap hmapStuffVO = new HashMap();
				// //精度
				// int[0] ----- 数量的精度
				// int[1] ----- 单价的精度
				// int[2] ----- 金额的精度
				Integer[] iDataPrecision = boCommonData.getDataPrecision(sSysinfos[0]);

				// 循环处理
				for (int i = 0; i < bvos.length; i++) {
					// 存货核算的VO
					nc.vo.ia.bill.BillHeaderVO voBillHeaderVO = (nc.vo.ia.bill.BillHeaderVO) bvos[i].getParentVO();
					nc.vo.ia.bill.BillItemVO[] vosBillItemVO = (nc.vo.ia.bill.BillItemVO[]) bvos[i].getChildrenVO();

					for (int j = 0; j < vosBillItemVO.length; j++) {
						StuffVO voStuffVO = new StuffVO();
						StuffItemVO voStuffItemVO = new StuffItemVO();

						/** ****************子表处理******************** */

						// 来源模块
						voStuffItemVO.setCsourcemodulename(ConstVO.m_sModuleIA);
						// 公司
						voStuffItemVO.setPk_corp(voBillHeaderVO.getPk_corp());
						// 工厂
						voStuffItemVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
						// voStuffItemVO.setvsourcebillcode(voBillHeaderVO.getVbillcode());

						// 材料IDa.pk_invbasdoc , b.pk_produce , a.pk_invmandoc ,
						// b.jhj , c.invname , c.invcode,d.pk_invcl
						String sPlanprice = null; // 计划价
						String[] sInvBasProdID = getInvBasProdID(sAllInvBasProdPKs, vosBillItemVO[j].getCinventoryid());
						if (sInvBasProdID != null) {
							voStuffItemVO.setPk_invbasdoc(sInvBasProdID[0]); // 原材料
							voStuffItemVO.setPk_produce(sInvBasProdID[1]);
							// 原材料
							sPlanprice = sInvBasProdID[3];

						} else {
							System.out.println(voBillHeaderVO.getVbillcode() + "没有得到子表产品信息\n");
							// 获得产品ID
							sSQL.setLength(0);
							sSQL.append("select a.pk_invbasdoc,b.pk_produce  ");
							sSQL.append("from bd_invmandoc a left join bd_produce b on a.pk_invbasdoc=b.pk_invbasdoc  ");
							sSQL.append("where a.pk_invmandoc='" + vosBillItemVO[j].getCinventoryid() + "'  ");
							sSQL.append("and b.pk_corp='" + sCorpID + "'  ");
							sSQL.append("and b.pk_calbody='" + sCalbodyID + "'  ");

							String[][] sinvPKs = boCommonData.queryData(sSQL.toString());
							if (sinvPKs != null && sinvPKs[0][0] != null && sinvPKs[0][0].trim().length() > 0) {
								voStuffItemVO.setPk_invbasdoc(sinvPKs[0][0]); // 原材料
								voStuffItemVO.setPk_produce(sinvPKs[0][1]);
							} else {
								// System.out.println(voBillHeaderVO.getVbillcode()+"没有得到子表产品信息\n");
								continue;
							}

						}
						UFDouble nnum = vosBillItemVO[j].getNnumber();
						voStuffItemVO.setNnumber(vosBillItemVO[j].getNnumber()); // 数量
						// 计划
						UFDouble nPlanmoney = null;
						if (sPlanprice != null) {
							UFDouble nplanprice = new UFDouble(sPlanprice);
							voStuffItemVO.setNplanprice(nplanprice); // 计划价

							if (nnum != null) {
								nPlanmoney = nnum.multiply(nplanprice).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP);
							}
							voStuffItemVO.setNplanmoney(nPlanmoney); // 计划金额
						}

						// 成本制度
						String sPvalue = boCommonData.getParaValue(voStuffItemVO.getPk_corp(), voStuffItemVO.getPk_calbody(), ConstVO.m_iPara_CBZD);
						int iParaValue;
						if (sPvalue == null || sPvalue.trim().length() == 0) {
							throw new CMBusinessException("没有得到成本制度参数", new BusinessException("没有得到成本制度参数"));
						} else {
							iParaValue = Integer.parseInt(sPvalue);
						}
						if (iParaValue == ConstVO.m_iPValue_CBZD_STANDARD) {
							voStuffItemVO.setNprice(voStuffItemVO.getNplanprice());// 价格
							voStuffItemVO.setNcost(voStuffItemVO.getNplanmoney()); // 金额
							voStuffItemVO.setNdiff(new UFDouble(0));
						} else {
							// 实际

							// voStuffItemVO.setNcost(vosBillItemVO[j].getNmoney());
							// //金额
							// //华锦变更 金额+费率额
							if (vosBillItemVO[j].getNdrawsummny() == null) {
								voStuffItemVO.setNcost(vosBillItemVO[j].getNmoney());
							} else if (vosBillItemVO[j].getNmoney() != null) {
								voStuffItemVO.setNcost(vosBillItemVO[j].getNmoney().add(vosBillItemVO[j].getNdrawsummny()));
							}

							UFDouble nCost = voStuffItemVO.getNcost();
							if ((nnum == null || nnum.equals(new UFDouble(0))) && (nCost == null || nCost.equals(new UFDouble(0)))) {
								// if (bNotShowTitle == true) {
								// errStrB.append(sErrTitle);
								// }
								// errStrB.append("价格和金额同时为空或者为零"+"\n");
								continue;
							}
							if (vosBillItemVO[j].getNnumber() != null && nCost != null) {
								voStuffItemVO.setNprice((vosBillItemVO[j].getNmoney()).div(vosBillItemVO[j].getNnumber()).setScale(-iDataPrecision[1].intValue(), UFDouble.ROUND_HALF_UP));
							}
							// 差异
							if (nCost != null && nPlanmoney != null) {
								voStuffItemVO.setNdiff(nCost.sub(nPlanmoney));
							}
						}

						/** ****************主表处理******************** */
						StuffHeaderVO voStuffHeaderVO = new StuffHeaderVO();
						// 1先根据工作中心和部门查询成本中心
						String pk_ctcenter = getCTCenterForM2(sAllCtcenterPKs, voBillHeaderVO.getCdeptid(), vosBillItemVO[j].getCwp());
						if (pk_ctcenter != null && pk_ctcenter.length() > 0) {
							voStuffHeaderVO.setPk_ctcenter(pk_ctcenter); // 成本中心
						} else {
							// 没有获得对应的成本中心
							continue;
						}
						// 2产品ID
						String[] sProductInvIDs = null;
						if (vosBillItemVO[j].getVbomcode() != null) {
							sProductInvIDs = getInvBasProdID(sAllInvBasProdPKs, vosBillItemVO[j].getVbomcode());
							if (sProductInvIDs != null && sProductInvIDs.length > 0) {
								voStuffHeaderVO.setPk_invbasdoc(sProductInvIDs[0]); // 产品
								voStuffHeaderVO.setPk_produce(sProductInvIDs[1]); // 产品

							} else {
								System.out.println(voBillHeaderVO.getVbillcode() + "没有得到主表产品信息\n");
								continue;
							}
						}

						// 3根据生产订单号获得主键
						String spk_order = null;
						if (vosBillItemVO[j].getVproducebatch() != null) {
							String strscddh = vosBillItemVO[j].getVproducebatch();
							for (int m = 0; m < sAllOrderPKs.length; m++) {
								if (sAllOrderPKs[m][3].equals(strscddh)) {
									spk_order = sAllOrderPKs[m][0];
								}
							}
							if (spk_order != null && spk_order.length() != 0) {
								voStuffHeaderVO.setPk_order(spk_order); // 订单（生产批号）
							}

						}
						// 4成本特殊处理
						if ((spk_order == null || spk_order.length() == 0) && sProductInvIDs != null && sProductInvIDs.length > 0) {
							voStuffHeaderVO.setPk_order(sProductInvIDs[1]);
						}
						/** ************构建hashcode成本中心＋订单＋产品**************** */
						String shashcode = "";
						String snull = "00000000000000000000";
						if (voStuffHeaderVO.getPk_ctcenter() != null) {
							shashcode += voStuffHeaderVO.getPk_ctcenter();
						}
						if (voStuffHeaderVO.getPk_order() != null) {
							shashcode += voStuffHeaderVO.getPk_order();
						} else {
							shashcode += snull;
						}
						if (voStuffHeaderVO.getPk_produce() != null) {
							shashcode += voStuffHeaderVO.getPk_produce();
						} else {
							shashcode += snull;
						}
						// 是否按照日期汇总
						// boolean isByDate = false;
						voStuffHeaderVO.setDbilldate(voBillHeaderVO.getDbilldate()); // 单据日期
						if (isByDate == true) {
							if (voStuffHeaderVO.getDbilldate() != null) {
								shashcode += voStuffHeaderVO.getDbilldate().toString();
							} else {
								shashcode += "0000000000";
							}
						}
						boolean isNewHeader; // 是否新的Header
						if (hmapStuffVO.get(shashcode) != null) {
							voStuffVO = (StuffVO) (hmapStuffVO.get(shashcode));
							isNewHeader = false;
						} else {
							isNewHeader = true;
						}
						if (isNewHeader == true) {
							if (isByDate == true) {
								voStuffHeaderVO.setDbillmakedate(new UFDate(sSysinfos[6])); // 制单日期
							} else {
								voStuffHeaderVO.setDbilldate(new UFDate(sSysinfos[3]));
								voStuffHeaderVO.setDbillmakedate(new UFDate(sSysinfos[6])); // 制单日期
							}
							// 公司
							voStuffHeaderVO.setPk_corp(voBillHeaderVO.getPk_corp());
							voStuffHeaderVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
							// 工厂（库存组织）
							voStuffHeaderVO.setCbillmaker(sSysinfos[5]); // 制单人

							voStuffHeaderVO.setCbilltypecode(ConstVO.m_sBillCLCBGJ); // 单据类型
							// 会计年
							voStuffHeaderVO.setCaccountyear(sSysinfos[7]);
							// 会计月
							voStuffHeaderVO.setCaccountmonth(sSysinfos[8]);

							// voStuffHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030",
							// "UPP-000009")/* @res""该单据来源于存货核算材料出库单"" */);
							// 如果是新的VO
							voStuffVO.setParentVO(voStuffHeaderVO);
							voStuffVO.setChildrenVO(new StuffItemVO[] { voStuffItemVO });
							hmapStuffVO.put(shashcode, voStuffVO);
						} else { // 如果是旧的VO，Items加上该分录
							StuffItemVO[] vosOldStuffItemVO = (StuffItemVO[]) (voStuffVO.getChildrenVO());
							int ivosOldStuffItemVOlength = vosOldStuffItemVO.length;

							// 合并
							boolean isItemUnion = false;
							for (int k = 0; k < ivosOldStuffItemVOlength; k++) {
								if (vosOldStuffItemVO[k].getPk_invbasdoc().equals(voStuffItemVO.getPk_invbasdoc())) {
									isItemUnion = true;
									UFDouble dOldNum = vosOldStuffItemVO[k].getNnumber();
									UFDouble dOldCost = vosOldStuffItemVO[k].getNcost();
									UFDouble dNewNum = voStuffItemVO.getNnumber();
									UFDouble dNewCost = voStuffItemVO.getNcost();
									if (dOldNum != null && dNewNum != null) {
										vosOldStuffItemVO[k].setNnumber(dOldNum.add(dNewNum));
									}
									if (dOldCost != null && dNewCost != null) {
										vosOldStuffItemVO[k].setNcost(dOldCost.add(dNewCost));
									}
									if (vosOldStuffItemVO[k].getNnumber() != null && vosOldStuffItemVO[k].getNcost() != null) {
										vosOldStuffItemVO[k].setNprice(vosOldStuffItemVO[k].getNcost().div(vosOldStuffItemVO[k].getNnumber()).setScale(-iDataPrecision[1].intValue(),
												UFDouble.ROUND_HALF_UP));
									}
									vosOldStuffItemVO[k].setciaitemid(null);
								}

							}
							if (isItemUnion == false) {
								StuffItemVO[] vosNewStuffItemVO = new StuffItemVO[ivosOldStuffItemVOlength + 1];
								System.arraycopy(vosOldStuffItemVO, 0, vosNewStuffItemVO, 0, ivosOldStuffItemVOlength);
								vosNewStuffItemVO[ivosOldStuffItemVOlength] = voStuffItemVO;
								voStuffVO.setChildrenVO(vosNewStuffItemVO);
								hmapStuffVO.remove(shashcode);
								hmapStuffVO.put(shashcode, voStuffVO);
							}

						} // if(isNewHeader)

					} // for (int j = 0; j < vosBillItemVO.length; j++)

				} // for (int i = 0; i < bvos.length; i++)

				Vector vtSqls = new Vector();

				// 将hashmap转换为数组
				if (hmapStuffVO.size() > 0) {
					// vosStuffVO = new StuffVO[hmapStuffVO.size()];

					Iterator it = hmapStuffVO.values().iterator();

					int l = 0;
					while (it.hasNext()) {
						l++;
						StuffVO svo = (StuffVO) it.next();
						StuffHeaderVO shvo = (StuffHeaderVO) svo.getParentVO();
						StuffItemVO[] sivos = (StuffItemVO[]) svo.getChildrenVO();

						if (sivos == null || sivos.length == 0)
							continue;

						for (int i = 0, iLength = sivos.length; i < iLength; i++) {

							if (sivos[i].getNcost() == null) // 实际金额为空没必要更新
								continue;

							StringBuffer sbSql = new StringBuffer("update cm_stuff_b ");
							sbSql.append("set ndiff = " + sivos[i].getNdiff());
							sbSql.append(", nprice = " + sivos[i].getNprice());
							sbSql.append(", ncost = " + sivos[i].getNcost());
							sbSql.append(" where pk_produce = '" + sivos[i].getPk_produce() + "' ");
							sbSql.append("and csourcemodulename = '" + ConstVO.m_sModuleIA + "' ");
							sbSql.append("and dr = 0 ");
							sbSql.append("and pk_stuff in ( ");
							sbSql.append("select a.pk_stuff from cm_stuff a ");
							sbSql.append("where a.pk_corp = '" + sCorpID + "' ");
							sbSql.append("and a.pk_calbody = '" + sCalbodyID + "' ");
							sbSql.append("and a.caccountyear= '" + shvo.getCaccountyear() + "' ");
							sbSql.append("and a.caccountmonth = '" + shvo.getCaccountmonth() + "' ");
							sbSql.append("and a.pk_ctcenter = '" + shvo.getPk_ctcenter() + "' ");
							if (shvo.getPk_order() != null) {
								sbSql.append("and a.pk_order = '" + shvo.getPk_order() + "' ");
								sbSql.append("and a.pk_produce = '" + shvo.getPk_produce() + "' ");

							} else {
								sbSql.append("and a.pk_order is null ");
								sbSql.append("and a.pk_produce is null ");

							}

							sbSql.append("and a.dbilldate = '" + shvo.getDbilldate() + "' ");
							sbSql.append("and a.dr = 0 ");
							sbSql.append(") ");

							vtSqls.add(sbSql.toString());

						}

					}

					// 批执行sql
					if (!vtSqls.isEmpty()) {
						String[] sqltemps = new String[vtSqls.size()];
						vtSqls.copyInto(sqltemps);

						boCommonData.execDatasNoTranslate(sqltemps);

					}

				}

			} // if (bvos != null)
			System.out.println(errStrB);
		} catch (CMBusinessException e) {
			throw e;
		} catch (Exception ee) {
			throw new CMBusinessException(ee.getMessage(), ee);
		}

		return errStrB.toString();

	}

	/**
	 * 函数功能:取得材料出库成本
	 * 
	 * 参数: sSysinfos[0] ----- 公司 sSysinfos[1] ----- 工厂 sSysinfos[2] ----- 起始日期
	 * sSysinfos[3] ----- 结束日期 sSysinfos[4] ----- 单据类型 "I6" 材料出库单 sSysinfos[5]
	 * ----- 当前用户 sSysinfos[6] ----- 当前日期 sSysinfos[7] ----- 会计年 sSysinfos[8]
	 * ----- 会计月 String[] sRDCLs ----- 入库类别 String[] sInvs ----- 存货
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 
	 */
	public String getIAI6BillsForM2(String[] sSysinfos, String[] sRDCLs) throws nc.vo.cm.pub.CMBusinessException {
		System.out.println("getIAI6BillsForM2 begin");
		// 提示信息
		String isFinish = null;
		UFDate dDbillmakedate = new UFDate(sSysinfos[6]);
		UFDate dDbilldate = new UFDate(sSysinfos[3]);
		// BOS
		// nc.bs.ia.outter.OutterBO boIAOutter = null;
		CommonDataBO boCommonData = null;
		BillcodeRuleBO boBillcodeRuleBO = null;

		IIAToCA iaToCa = (IIAToCA) NCLocator.getInstance().lookup(IIAToCA.class.getName());

		try {

			String sCorpID = sSysinfos[0];
			String sCalbodyID = sSysinfos[1];
//			String sBillTypecode = sSysinfos[4];

			boCommonData = new CommonDataBO();
			// boIAOutter = new nc.bs.ia.outter.OutterBO();

			// 取数
			long t1 = System.currentTimeMillis();
			
			/**将传入数据转换为接口数据
			 * 	sSysinfos[0] = getPk_Corp();
			sSysinfos[1] = getPk_Calbody();
			sSysinfos[2] = sStartDate;
			sSysinfos[3] = sEndDate;
			sSysinfos[4] = "I6";
			sSysinfos[5] = getPk_User();
			sSysinfos[6] = getBusinessDate().toString();
			sSysinfos[7] = getAccountPeriodYear();
			sSysinfos[8] = getAccountPeriodMonth();*/
			
			String[] sInterfaceData=new String[6];
			sInterfaceData[0] = sSysinfos[0];
			sInterfaceData[1] = sSysinfos[1];
			sInterfaceData[2] = sSysinfos[2];
			sInterfaceData[3] = sSysinfos[3];
			sInterfaceData[4] = sSysinfos[7];
			sInterfaceData[5] = sSysinfos[8];
			
			
//			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCPBills(sSysinfos, sRDCLs, null);
			/**是否包含差异结存单*/
			String sSFBHYCJCD=(new CommonDataBO()).getParaValue(sCorpID, sCalbodyID, ConstVO.m_iPara_SFBHYCJCD);
			boolean bSFBHYCJCD="Y".equalsIgnoreCase(sSFBHYCJCD)?true:false;
			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCCBills(sInterfaceData, sRDCLs, null,bSFBHYCJCD,false);
			CMLogger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$boIAOutter.getCCCBills:" + getTime(t1, System.currentTimeMillis()));
			
			/**打印*/
		/*	for (int i = 0; i < bvos.length; i++) {
				// 存货核算的VO
				nc.vo.ia.bill.BillHeaderVO voBillHeaderVO = (nc.vo.ia.bill.BillHeaderVO) bvos[i].getParentVO();
				nc.vo.ia.bill.BillItemVO[] vosBillItemVO = (nc.vo.ia.bill.BillItemVO[]) bvos[i].getChildrenVO();
				System.out.println("###########" + i + " start ################");
				for (int j = 0; j < vosBillItemVO.length; j++) {
					System.out.println("$$$$$$$$$$$$$$" + j + " start $$$$$$$$$$$$$$");
					System.out.println("cbill_bid="+vosBillItemVO[j].getCbill_bid() );
					System.out.println("vbillcode="+vosBillItemVO[j].getVbillcode() );
					System.out.println("cinventoryid="+vosBillItemVO[j].getCinventoryid() );
					System.out.println("pk_corp="+vosBillItemVO[j].getPk_corp() );
					
					System.out.println("money=" + vosBillItemVO[j].getNmoney());
					System.out.println("number=" + vosBillItemVO[j].getNnumber());
					System.out.println("$$$$$$$$$$$$$$" + j + " end $$$$$$$$$$$$$$");
				}
				System.out.println("###########" + i + " end ################");
			}*/
			

			if (bvos != null && bvos.length > 0) {
				t1 = System.currentTimeMillis();
				// 获得所有成本对象基本ID和生产ID--1.2.未审核的原材料不取
				StringBuffer sSQL = new StringBuffer(300);
				sSQL.append("select  a.pk_invbasdoc ,  b.pk_produce ,  a.pk_invmandoc");
				sSQL.append(" from bd_invmandoc a,bd_produce b  ");
				sSQL.append(" where a.pk_invmandoc = b.pk_invmandoc  ");
				sSQL.append(" and b.sfcbdx='Y' ");
				sSQL.append(" and b.pk_corp = '" + sCorpID + "' ");
				sSQL.append(" and b.pk_calbody = '" + sCalbodyID + "' ");
				// 所有成本对象基本ID和生产ID
				String[][] sAllProductPKs = boCommonData.queryData(sSQL.toString());
				//
				HashSet vProducts = new HashSet();
				if (sAllProductPKs.length != 0) {
					for (int i = 0; i < sAllProductPKs.length; i++) {
						vProducts.add(sAllProductPKs[i][1]);
					}
				} // if (sResult.length != 0)

				// 获得所有存货的基本ID和生产ID
				sSQL.setLength(0);
				sSQL.append("select  a.pk_invbasdoc ,  b.pk_produce ,  a.pk_invmandoc ,  b.jhj  ,c.pk_invcl");
				sSQL.append(" from bd_invmandoc a,bd_produce b ,bd_invbasdoc c ");
				sSQL.append(" where  a.pk_invbasdoc=c.pk_invbasdoc ");
				sSQL.append(" and a.pk_invmandoc = b.pk_invmandoc ");
				sSQL.append(" and b.pk_corp = '" + sCorpID + "' ");
				sSQL.append(" and b.pk_calbody = '" + sCalbodyID + "' ");

				String[][] sAllInvBasProdPKs = boCommonData.queryData(sSQL.toString());
				// 取得所有订单
				sSQL.setLength(0);
				sSQL.append(" select mo.pk_moid,  mo.scddh ");
				sSQL.append(" from  mm_mo mo inner join bd_produce b on mo.pk_produce=b.pk_produce");
				sSQL.append(" where b.iscostbyorder='Y' and mo.dr = 0");
				sSQL.append(" and mo.pk_corp = '" + sCorpID + "'");
				// sSQL.append(" and mo.zt!='D' ");
				sSQL.append(" and mo.gcbm = '" + sCalbodyID + "'");
				String[][] sAllOrderPKs = boCommonData.queryData(sSQL.toString());

				// 获得所有的成本要素
				sSQL.setLength(0);
				sSQL.append("select distinct a.pk_subfac,c.pk_subfacset, b.csubfacname,a.pk_ctcenter,a.pk_rdcl,a.cvalue,a.pk_invbasdoc ");
				sSQL.append("from cm_subfac_b a, cm_subfac b  ,cm_subfacset c ");
				sSQL.append("where b.blastlevelflag = 'Y'  ");
				sSQL.append("and a.pk_subfac = b.pk_subfac  ");
				sSQL.append("and b.pk_subfac=c.pk_subfac ");
				sSQL.append("and a.pk_corp='" + sCorpID + "'  ");
				sSQL.append("and a.citem='INVCL' ");
				sSQL.append("and c.pk_corp=a.pk_corp  ");
				sSQL.append("and c.pk_ctcenter=a.pk_ctcenter ");
				String[][] sAllSubPKs = boCommonData.queryData(sSQL.toString());

				// 获得所有的成本中心
				sSQL.setLength(0);
				sSQL.append("select a.pk_ctcenter,b.pk_wk,b.pk_deptdoc  ");
				sSQL.append(" from cm_ctcenter a, cm_ctcenter_b b  ");
				sSQL.append(" where a.pk_ctcenter=b.pk_ctcenter ");
				sSQL.append(" and	a.pk_corp = '" + sCorpID + "'  ");
				sSQL.append(" and 	a.pk_calbody = '" + sCalbodyID + "'  ");
				String[][] sAllCtcenterPKs = boCommonData.queryData(sSQL.toString());

				// //精度
				// int[0] ----- 数量的精度
				// int[1] ----- 单价的精度
				// int[2] ----- 金额的精度
				Integer[] iDataPrecision = boCommonData.getDataPrecision(sCorpID);

				CMLogger.debug("$$$$$$$$$$$$$$$$$$$$$$$$获得所有基本数据:" + getTime(t1, System.currentTimeMillis()));

				HashMap hmapStuffVO = new HashMap();
				// 循环处理
				for (int i = 0; i < bvos.length; i++) {
					// 存货核算的VO
					nc.vo.ia.bill.BillHeaderVO voBillHeaderVO = (nc.vo.ia.bill.BillHeaderVO) bvos[i].getParentVO();
					nc.vo.ia.bill.BillItemVO[] vosBillItemVO = (nc.vo.ia.bill.BillItemVO[]) bvos[i].getChildrenVO();

					for (int j = 0; j < vosBillItemVO.length; j++) {
						
						if(vosBillItemVO[j]==null)
							continue;
						/** 1.检查 */
						/** 1.1.物料有订单号但没有成本对象不取 */
						if ( vosBillItemVO[j].getVbomcode() == null && vosBillItemVO[j].getVproducebatch() != null) {
							continue;
						}
						/** 1.2.未审核的原材料不取** */
						// 根据材料的管理ID得到材料IDa.pk_invbasdoc , b.pk_produce ,
						// a.pk_invmandoc , b.jhj ,d.pk_invcl
						String[] sStuffID = getInvBasProdID(sAllInvBasProdPKs, vosBillItemVO[j].getCinventoryid());
						if (vosBillItemVO[j].getCauditorid() == null && vProducts.contains(sStuffID[1]) == false) {
							continue;
						}
						/** 1.3.数量和金额同时为空或者为零* */
						/*UFDouble nnum = vosBillItemVO[j].getNnumber();
						UFDouble nCost = vosBillItemVO[j].getNmoney();
						if ((nnum == null || nnum.toDouble().doubleValue() == 0.00) && (nCost == null || nCost.toDouble().doubleValue() == 0.00)) {
							continue;
						}*/

						// **2.先判断是否存在该表头*/
						/** ****************************2.1.先根据工作中心和部门查询成本中心******************************* */
						String pk_ctcenter = getCTCenterForM2(sAllCtcenterPKs, voBillHeaderVO.getCdeptid(), vosBillItemVO[j].getCwp());
						if (pk_ctcenter == null || pk_ctcenter.length() == 0) {
							continue;
						}
						/** ***************************2.2.根据订单号获得主键********************************************** */
						String spk_order = null;
						if (vosBillItemVO[j].getVproducebatch() != null) {
							String strscddh = vosBillItemVO[j].getVproducebatch();
							for (int m = 0; m < sAllOrderPKs.length; m++) {
								if (sAllOrderPKs[m][1].equals(strscddh)) {
									spk_order = sAllOrderPKs[m][0];
									break;
								}
							}

						}
						/** ***************************2.3.根据成本对象的管理ID获得基本ID和生产ID********************************************** */
						String[] sProductInvIDs = getInvBasProdID(sAllProductPKs, vosBillItemVO[j].getVbomcode());
						String sProductBasID = null; // 产品基本ID
						String sProductProID = null; // 生产ID
						if (sProductInvIDs != null && sProductInvIDs.length > 0) {
							sProductBasID = sProductInvIDs[0]; // 产品基本ID
							sProductProID = sProductInvIDs[1]; // 产品 生产ID

						}
						/** ***************************2.4.成本中心＋订单＋产品构建hashcode**************** */
						String shashcode = pk_ctcenter;
						String snull = "00000000000000000000";
						if (spk_order != null) {
							shashcode += spk_order;
						} else {
							shashcode += snull;
						}
						if (sProductProID != null) {
							shashcode += sProductProID;
						} else {
							shashcode += snull;
						}
						StuffVO voStuffVO = null;
						/** ***************************2.5.根据hashcode判断表头是否存在**************** */
						if (hmapStuffVO.get(shashcode) != null) { // 表体存在
							voStuffVO = (StuffVO) (hmapStuffVO.get(shashcode));
							// 如果是旧的VO，Items加上该分录
							StuffItemVO[] vosOldStuffItemVO = (StuffItemVO[]) (voStuffVO.getChildrenVO());
							int ivosOldStuffItemVOlength = vosOldStuffItemVO.length;
							boolean isUnion = false; // 如果子表已经存在该材料,则数量和金额累加
							
							for (int k = 0; k < ivosOldStuffItemVOlength; k++) {
								//20070628 jack add subface check
								String[] sFac = getIAI6BillsSubfac(sAllSubPKs, pk_ctcenter, voBillHeaderVO.getCdispatchid(), sStuffID);
								if(sFac == null || sFac.length  == 0 || sFac[0] == null) continue;
								
								if (vosOldStuffItemVO[k].getPk_produce().equals(sStuffID[1]) /**jack added 20070628 */ && vosOldStuffItemVO[k].getPk_subfac().equals(sFac[0]) ) {
									UFDouble dOldNum = vosOldStuffItemVO[k].getNnumber();
									UFDouble dOldCost = vosOldStuffItemVO[k].getNcost();
									UFDouble dNewNum = (vosBillItemVO[j].getNnumber()==null?new UFDouble(0):vosBillItemVO[j].getNnumber());
									UFDouble dNewCost = null;
									// //华锦：金额+费率额
									if (vosBillItemVO[j].getNdrawsummny() == null) {
										dNewCost = vosBillItemVO[j].getNmoney();
									} else if (vosBillItemVO[j].getNmoney() != null) {
										dNewCost = vosBillItemVO[j].getNmoney().add(vosBillItemVO[j].getNdrawsummny());
									}
									// 合并数量和金额
									if (dOldNum != null && dNewNum != null) {
										vosOldStuffItemVO[k].setNnumber(dOldNum.add(dNewNum));
									}

									// 如果计划单价不为空更新计划金额
									if (vosOldStuffItemVO[k].getNplanprice() != null && vosOldStuffItemVO[k].getNnumber() != null) {
										vosOldStuffItemVO[k].setNplanmoney((vosOldStuffItemVO[k].getNplanprice().multiply(vosOldStuffItemVO[k].getNnumber())).setScale(-iDataPrecision[2].intValue(),
												UFDouble.ROUND_HALF_UP));
									}

									// 合并金额
									if (dOldCost != null && dNewCost != null) {
										vosOldStuffItemVO[k].setNcost(dOldCost.add(dNewCost));
									}
									// 如果计划金额和实际金额不为空
									if (vosOldStuffItemVO[k].getNcost() != null && vosOldStuffItemVO[k].getNplanmoney() != null) {
										vosOldStuffItemVO[k].setNdiff(vosOldStuffItemVO[k].getNcost().sub(vosOldStuffItemVO[k].getNplanmoney()));
									}
									// 更新价格
									if (vosOldStuffItemVO[k].getNnumber() != null && vosOldStuffItemVO[k].getNcost() != null) {
										vosOldStuffItemVO[k].setNprice(vosOldStuffItemVO[k].getNcost().div(vosOldStuffItemVO[k].getNnumber()).setScale(-iDataPrecision[1].intValue(),
												UFDouble.ROUND_HALF_UP));
									}
									// vosOldStuffItemVO[k].setciaitemid(null);
									isUnion = true;
									break;
								}

							}
							// 如果子表不存在该材料则生成子表VO
							if (isUnion == false) {
								// 生成子表VO
								/** *************生成表体************************* */
								StuffItemVO voStuffItemVO = getIAI6BillsStuffItemVO(sAllSubPKs, pk_ctcenter, voBillHeaderVO, vosBillItemVO[j], sStuffID, iDataPrecision);
								if (voStuffItemVO == null) {
									continue;
								}
								StuffItemVO[] vosNewStuffItemVO = new StuffItemVO[ivosOldStuffItemVOlength + 1];
								System.arraycopy(vosOldStuffItemVO, 0, vosNewStuffItemVO, 0, ivosOldStuffItemVOlength);
								vosNewStuffItemVO[ivosOldStuffItemVOlength] = voStuffItemVO;
								voStuffVO.setChildrenVO(vosNewStuffItemVO);
								hmapStuffVO.remove(shashcode);
								hmapStuffVO.put(shashcode, voStuffVO);
							}

						} else { // 表头不存在
							/** *************生成表头************************* */
							StuffHeaderVO voStuffHeaderVO = new StuffHeaderVO();
							// 业务日期dbilldate
							voStuffHeaderVO.setDbilldate(dDbilldate);
							// 公司pk_corp
							voStuffHeaderVO.setPk_corp(voBillHeaderVO.getPk_corp());
							// 工厂（库存组织）pk_calbody
							voStuffHeaderVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
							// 单据类型cbilltypecode
							voStuffHeaderVO.setCbilltypecode(ConstVO.m_sBillCLCBGJ);
							// 会计年caccountyear
							voStuffHeaderVO.setCaccountyear(sSysinfos[7]);
							// 会计月caccountmonth
							voStuffHeaderVO.setCaccountmonth(sSysinfos[8]);
							// 成本中心pk_ctcenter
							voStuffHeaderVO.setPk_ctcenter(pk_ctcenter);
							// 订单pk_order
							if (spk_order != null && spk_order.length() > 0) {
								voStuffHeaderVO.setPk_order(spk_order); // 订单（生产批号）
							}
							// 成本特殊处理，如果订单ID为空保存产品生产ID
							else if ((spk_order == null || spk_order.length() == 0) && sProductInvIDs != null && sProductInvIDs.length > 0) {
								voStuffHeaderVO.setPk_order(sProductInvIDs[1]);
							}
							// 产品基本IDpk_invbasdoc
							voStuffHeaderVO.setPk_invbasdoc(sProductBasID);
							// 产品生产IDpk_produce
							voStuffHeaderVO.setPk_produce(sProductProID);
							// 备注vnote
							voStuffHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000009")/* @res""该单据来源于存货核算材料出库单"" */);
							// 制单人 cbillmaker
							voStuffHeaderVO.setCbillmaker(sSysinfos[5]);
							// 制单日期dbillmakedate
							voStuffHeaderVO.setDbillmakedate(dDbillmakedate);
							/** *************生成表体************************* */
							StuffItemVO voStuffItemVO = getIAI6BillsStuffItemVO(sAllSubPKs, pk_ctcenter, voBillHeaderVO, vosBillItemVO[j], sStuffID, iDataPrecision);
							if (voStuffItemVO == null) {
								continue;
							}
							// 如果是新的VO
							voStuffVO = new StuffVO();
							voStuffVO.setParentVO(voStuffHeaderVO);
							voStuffVO.setChildrenVO(new StuffItemVO[] { voStuffItemVO });
							hmapStuffVO.put(shashcode, voStuffVO);

						}

						/** ************************************************************************* */

					} // for (int j = 0; j < vosBillItemVO.length; j++)

				} // for (int i = 0; i < bvos.length; i++)

				// 将hashmap转换为数组
				StuffVO[] vosStuffVO = null;
				CMLogger.debug("$$$$$$$$$$$$$$$$$$$$$$$$数据插入前处理共耗时:" + getTime(t1, System.currentTimeMillis()));
				if (hmapStuffVO.size() > 0) {
					// 单据号处理
					boBillcodeRuleBO = new BillcodeRuleBO();
					String[] name = { "库存组织", "操作员" };
					String[] id = { sCalbodyID.trim(), sSysinfos[5].trim() };
					BillCodeObjValueVO value = new BillCodeObjValueVO();
					value.setAttributeValue(name, id);
					String[] sbillcodes = boBillcodeRuleBO.getBatchBillCodes("M2", sCorpID, value, hmapStuffVO.size());
					vosStuffVO = new StuffVO[hmapStuffVO.size()];
					Iterator it = hmapStuffVO.values().iterator();
					int l = 0;
					while (it.hasNext()) {
						vosStuffVO[l] = (StuffVO) it.next();
						((StuffHeaderVO) (vosStuffVO[l].getParentVO())).setVbillcode(sbillcodes[l]);
						l++;
					}

				}

				if (vosStuffVO != null && vosStuffVO.length > 0) {

					StuffBO boStuffBO = new StuffBO();
					boStuffBO.insertArray(vosStuffVO);
				}
				CMLogger.debug("$$$$$$$$$$$$$$$$$$$$$$$$getIAI6BillsForM2取数共耗时:" + getTime(t1, System.currentTimeMillis()));
			} // if (bvos != null)
			// System.out.println(errStrB);
			isFinish = "ok";
			CMLogger.debug("getIAI6BillsForM2 end");
		} catch (CMBusinessException e) {
			throw e;
		} catch (Exception ee) {
			throw new CMBusinessException(ee.getMessage(), ee);
		}

		return isFinish;
	}

	/**
	 * 函数功能:根据条件生成子表VO
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 
	 */
	private StuffItemVO getIAI6BillsStuffItemVO(String[][] sAllSubPKs, String pk_ctcenter, nc.vo.ia.bill.BillHeaderVO voBillHeaderVO, nc.vo.ia.bill.BillItemVO vosBillItemVO, String[] sStuffID,
			Integer[] iDataPrecision) throws Exception {

		// 生成子表VO
		/** ************生成表体********** */
		StuffItemVO voStuffItemVO = new StuffItemVO();
		// 对应成本要素
		String[] sFac = null;
		sFac = getIAI6BillsSubfac(sAllSubPKs, pk_ctcenter, voBillHeaderVO.getCdispatchid(), sStuffID);

		if (sFac != null) {
			voStuffItemVO.setPk_subfac(sFac[0]); // 成本要素//pk_subfac
			voStuffItemVO.setPk_subfacset(sFac[1]); // 成本要素//pk_subfacset
		} else {
			return null;

		}
		String sPlanprice = null;
		// 材料ID
		if (sStuffID != null) {
			// 原材料基本ID
			voStuffItemVO.setPk_invbasdoc(sStuffID[0]); // pk_invbasdoc
			// 原材料生产ID
			voStuffItemVO.setPk_produce(sStuffID[1]); // pk_produce
			sPlanprice = sStuffID[3]; // 计划价

		}
		// 来源模块
		voStuffItemVO.setCsourcemodulename(ConstVO.m_sModuleIA); // csourcemodulename
		// 公司
		voStuffItemVO.setPk_corp(voBillHeaderVO.getPk_corp()); // pk_calbody
		// 工厂
		voStuffItemVO.setPk_calbody(voBillHeaderVO.getCrdcenterid()); // pk_corp
		// 数量
		voStuffItemVO.setNnumber(vosBillItemVO.getNnumber()==null?new UFDouble(0):vosBillItemVO.getNnumber()); // nnumber
		// 华锦变更 金额+费率额
		if (vosBillItemVO.getNdrawsummny() == null) {
			voStuffItemVO.setNcost(vosBillItemVO.getNmoney());
		} else if (vosBillItemVO.getNmoney() != null) {
			voStuffItemVO.setNcost(vosBillItemVO.getNmoney().add(vosBillItemVO.getNdrawsummny()));
		}

		// 价格
		if (vosBillItemVO.getNnumber() != null && vosBillItemVO.getNmoney() != null)
			voStuffItemVO.setNprice((vosBillItemVO.getNmoney()).div(vosBillItemVO.getNnumber()).setScale(-iDataPrecision[1].intValue(), UFDouble.ROUND_HALF_UP));
		// nprice
		// 计划价格、计划金额
		if (sPlanprice != null) {
			UFDouble nplanprice = new UFDouble(sPlanprice);
			// 计划价
			voStuffItemVO.setNplanprice(nplanprice);
			UFDouble nPlanmoney = null;
			if (vosBillItemVO.getNnumber() != null) {
				nPlanmoney = vosBillItemVO.getNnumber().multiply(nplanprice).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP);
				// nplanmoney
			}
			// 计划金额
			voStuffItemVO.setNplanmoney(nPlanmoney);

			// 差异
			if (vosBillItemVO.getNmoney() != null && nPlanmoney != null) {
				voStuffItemVO.setNdiff(vosBillItemVO.getNmoney().sub(nPlanmoney)); // ndiff
			}

		}

		return voStuffItemVO;
	}

	/**
	 * 函数功能:根据条件对应成本要素
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 
	 */
	private String[] getIAI6BillsSubfac(String[][] sAllSubPKs, String pk_ctcenter, String Cdispatchid, String[] sInvBasProdID) throws Exception {
		// 先按照物料pK查找成本要素
		String[] sFac = null;
		for (int m = 0; m < sAllSubPKs.length; m++) {
			if (sAllSubPKs[m][3].equals(pk_ctcenter) && sAllSubPKs[m][4].equals(Cdispatchid) && sAllSubPKs[m][6] != null && sAllSubPKs[m][6].trim().equals(sInvBasProdID[0].trim())) {
				sFac = new String[3];
				sFac[0] = sAllSubPKs[m][0];
				sFac[1] = sAllSubPKs[m][1];
				sFac[2] = sAllSubPKs[m][2];
				break;
			}
		}
		// 然后按照存货分类查找
		if (sFac == null) {
			for (int m = 0; m < sAllSubPKs.length; m++) {
				if (sAllSubPKs[m][3].equals(pk_ctcenter) && sAllSubPKs[m][4].equals(Cdispatchid) && sAllSubPKs[m][5] != null && sAllSubPKs[m][5].trim().equals(sInvBasProdID[4].trim())) {
					sFac = new String[3];
					sFac[0] = sAllSubPKs[m][0];
					sFac[1] = sAllSubPKs[m][1];
					sFac[2] = sAllSubPKs[m][2];
					break;
				}
			}
		}
		return sFac;
	}

	private String[] getInvBasProdID(String[][] sAllInvs, String sInvmanID) {

		if (sAllInvs == null || sAllInvs.length <= 0 || sInvmanID == null)
			return null;

		for (int i = 0, iLength = sAllInvs.length; i < iLength; i++) {
			if (sInvmanID.equals(sAllInvs[i][2]))
				return sAllInvs[i];
		}

		return null;
	}

	// 单据号BO
	private PullStuffErrVO getPullStuffErrVO(String[] sSysinfos, nc.vo.ia.bill.BillHeaderVO voBillHeaderVO, nc.vo.ia.bill.BillItemVO voBillItemVO, String sErrNote, String pk_invbasdoc)
			throws Exception {
		PullStuffErrVO voPullStuffErrVO = new PullStuffErrVO();
		// vbillcode
		voPullStuffErrVO.setVbillcode(voBillHeaderVO.getVbillcode());
		// sSysinfos[0] ----- 公司
		// sSysinfos[1] ----- 工厂
		// sSysinfos[8]-------会计年
		// sSysinfos[9]-------会计月
		// pk_corp
		voPullStuffErrVO.setPk_corp(sSysinfos[0]);
		// pk_calbody
		voPullStuffErrVO.setPk_calbody(sSysinfos[1]);
		// caccountyear
		voPullStuffErrVO.setCaccountyear(sSysinfos[7]);
		// caccountmonth
		voPullStuffErrVO.setCaccountmonth(sSysinfos[8]);
		// dbilldate
		voPullStuffErrVO.setDbilldate(voBillHeaderVO.getDbilldate());
		// //deptID
		voPullStuffErrVO.setDeptID(voBillHeaderVO.getCdeptid());
		// ordercode
		voPullStuffErrVO.setOrdercode(voBillItemVO.getVproducebatch());
		// irownumber
		voPullStuffErrVO.setIrownumber(voBillItemVO.getIrownumber().toString());
		// pk_invbasdoc
		voPullStuffErrVO.setPk_invbasdoc(pk_invbasdoc);
		// nnumber
		voPullStuffErrVO.setNnumber(voBillItemVO.getNnumber() == null ? null : voBillItemVO.getNnumber().toString());
		// money
		voPullStuffErrVO.setMoney(voBillItemVO.getNmoney() == null ? null : voBillItemVO.getNmoney().toString());
		// pk_wk
		voPullStuffErrVO.setPk_wk(voBillItemVO.getCwp());
		// errnote
		voPullStuffErrVO.setErrnote(sErrNote);
		return voPullStuffErrVO;
	}

	/**
	 * 函数功能:获得成本路线
	 * 
	 * 参数: 公司 库存组织 用户 存货基本ID 计量单位ID 日期
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 */
	private DisRouteVO[] getRoutes(String[] sSysinfos) throws Exception {
		CommonDataBO cbo = null;

		DisRouteVO[] dvos = null;
		IPdToCm pdToCM = null;

		cbo = getCommonDataBO();
		pdToCM = (IPdToCm) NCLocator.getInstance().lookup(IPdToCm.class.getName());

		String sCorpID = sSysinfos[0];
		String sCalbodyID = sSysinfos[1];
		String sUserID = sSysinfos[2];
		String sInvBasID = sSysinfos[3];
		String sMeaID = sSysinfos[4];
		String sDate = sSysinfos[5];

		DisConditionVO dvo = new DisConditionVO();
		dvo.setPk_corp(sCorpID); // 公司ID
		dvo.setGcbm(sCalbodyID); // 工厂ID
		dvo.setOperid(sUserID); // 操作员ID
		dvo.setWlbmid(sInvBasID); // 物料ID(集团基本档案)

		if (sMeaID == null || sMeaID.trim().length() == 0) {
			String sSQL = " select ";
			sSQL = sSQL + " a.pk_measdoc";
			sSQL = sSQL + " from ";
			sSQL = sSQL + " bd_invbasdoc a ";
			sSQL = sSQL + " where ";
			sSQL = sSQL + " a.pk_invbasdoc = '" + sInvBasID + "'";

			String sResult[][] = cbo.queryData(sSQL);
			if (sResult.length != 0) {
				String[] sTemp = sResult[0];
				sMeaID = sTemp[0];

				dvo.setJldwid(sMeaID); // 计量单位ID
			}
		}

		dvo.setId("A"); // A:物料；B:分类
		dvo.setSl(new UFDouble(1)); // 数量
		dvo.setLogdate(new UFDate(sDate)); // 登录日期
		dvo.setYxrq(new UFDate(sDate)); // 有效日期

		dvo.setSfsh(new UFBoolean("Y")); // 是否考虑损耗
		dvo.setSffp(new UFBoolean("N")); // 是否考虑废品
		dvo.setSfplgz(new UFBoolean("N")); // 是否考虑批量规则

//		dvos = dsbo.getRoute(dvo);
		dvos=pdToCM.getRoutesCM(dvo);
		return dvos;
	}

	/**
	 * 函数功能:获得此成本项目下iLevel级的子成本项目
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * 
	 */
	private ArrayList getSubfac(SubfacBO sfbo, String sSubfacID, int iLevel, ArrayList vData) throws Exception {
		ArrayList vReturnData = (ArrayList) vData.clone();

		if (iLevel != 0 || iLevel == -1) {
			SubFacVO concidvo = new SubFacVO();
			concidvo.setParentsubfac(sSubfacID);

			SubFacVO[] sfvos = sfbo.queryByVO(concidvo, new Boolean(true));

			if (sfvos != null) {
				for (int i = 0; i < sfvos.length; i++) {
					vReturnData.add(sfvos[i]);

					if (iLevel - 1 != 0) {
						// 取其下级
						vReturnData = getSubfac(sfbo, sfvos[i].getPrimaryKey(), iLevel - 1, vReturnData);
					} else if (iLevel == -1) {
						// 取其下级
						vReturnData = getSubfac(sfbo, sfvos[i].getPrimaryKey(), -1, vReturnData);
					}
				}
			}
		}

		return vReturnData;
	}

	/**
	 * 函数功能:
	 * 
	 * 参数:
	 * 
	 * 返回值:
	 * 
	 * 异常:
	 * 
	 * @return nc.bs.ia.pub.CommonData
	 */
	private SubfacBO getSubfacBO() throws Exception {

		return new SubfacBO();
	}

	/**
	 * SessionBean接口中的方法。
	 * <p>
	 * 这个方法由EJB Container调用，永远不要在你的程序中调用这个方 法。
	 * <p>
	 * 创建日期：(2001-2-15 16:34:02)
	 * 
	 * @exception nc.vo.cm.pub.CMBusinessException
	 *                异常说明。
	 */
	private String getTime(long l1, long l2) {
		long t2 = l2 - l1;
		int iHour = (int) (t2 / 36000000);
		int iMinutes = (int) ((t2 - iHour * 36000000) / 60000);
		int iSecond = (int) ((t2 - iHour * 36000000 - iMinutes * 60000) / 1000);
		int iOther = (int) (t2 - iHour * 36000000 - iMinutes * 60000 - iSecond * 1000);
		String sTime = "用时：" + iHour + "小时" + iMinutes + "分钟" + iSecond + "秒" + iOther + "毫秒";
		return sTime;
	}

	/***************************************************************************
	 * 函数功能: 判断改存货是否在此工作中心或部门上生产 参数: String[0] sCorpID : 公司 String[1] sCalbodyID :
	 * 工厂 String[2] pk_invbasdoc : 存货基本ID String[3] date : 业务日期 String[4] wkid :
	 * 工作中心ID String[5] deptid : 部门ID
	 * 
	 * boolean isLast : 是否最后一个工作中心
	 * 
	 * 返回值: String[][]
	 * 
	 * 异常:
	 * 
	 * 创建日期：(2003-4-15 14:14:36)
	 * 
	 * @return boolean
	 **************************************************************************/

	public boolean isInvInRoute(String[] sParamList, boolean isLast) throws nc.vo.cm.pub.CMBusinessException {

		// sParamList 说明：
		/***********************************************************************
		 * String[0] sCorpID : 公司 String[1] sCalbodyID : 工厂 String[2]
		 * pk_invbasdoc : 存货基本ID String[3] date : 业务日期 String[4] wkid : 工作中心ID
		 * String[5] deptid : 部门ID
		 **********************************************************************/

		IPdToCm pdToCm = (IPdToCm) NCLocator.getInstance().lookup(IPdToCm.class.getName());
		String[][] sWKs;
		try {
			sWKs = pdToCm.getWkFromRouteCM(sParamList[0], sParamList[1], sParamList[2], sParamList[3], isLast);
		} catch (BusinessException e) {
			CMLogger.error("得到工艺路线对应工作中心错误", e);
			throw new CMBusinessException(e);
		}

		if (sWKs != null && sWKs.length > 0) {

			for (int i = 0; i < sWKs.length; i++) {
				if (sParamList[4] != null) { // 按工作中心
					if (sParamList[4].equals(sWKs[0]))
						return true;
				} else { // 按部门
					if (sParamList[5].equals(sWKs[1]))
						return true;
				}
			}
		}

		return false;

	}

	/**
	 * 产品生产id private String m_pk_produce; //消耗的订单 private String m_scddid; //数量
	 * private UFDouble m_sl;
	 * 
	 * //转出部门 private String m_zcbm; //转出工作中心 private String m_zcgzzx; //产品基本ID
	 * private String m_pk_invbasdoc; //消耗部门 private String m_xhbm; //消耗工作中心
	 * private String m_xhgzzx; //消耗产品基本ID private String m_pk_toinvbasdoc;
	 * //消耗产品生产ID private String m_pk_toProduce;
	 */
	public String queryBcpForM3(String[] sSysinfos, String pk_corp, String pk_calbody, String startDate, String endDate) throws nc.vo.cm.pub.CMBusinessException {
		// 提示信息
		String sErr = "";
		HalfprodVO[] vosHalfprodVO = null;
		// BOS
		WithOuterBO boWithOuter = null;
		BillcodeRuleBO boBillcodeRuleBO = null;
		CommonDataBO boCommonData = null;
		InpricesetBO boInpricesetBO = null;
		try {
			boCommonData = getCommonDataBO();
			boWithOuter = new WithOuterBO();
			boBillcodeRuleBO = getBillcodeRuleBO();

			// 得到CostStaticVO
			nc.vo.me.me1050.CostStaticVO[] vosCostStaticVO = null;
			IMmToCm mmToCM = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCM.getStatisticgxBcpCM(pk_corp, pk_calbody, startDate, endDate);
			if (vosCostStaticVO == null || vosCostStaticVO.length == 0) {
				return sErr;
			}
			int ivosCostStaticVOlength = vosCostStaticVO.length;
			ArrayList aryListHalfprodVO = new ArrayList(); //
			// //精度
			// int[0] ----- 数量的精度
			// int[1] ----- 单价的精度
			// int[2] ----- 金额的精度
			Integer[] iDataPrecision = boCommonData.getDataPrecision(sSysinfos[0]);

			// 循环处理
			if (vosCostStaticVO != null) {

				// 获得所有的成本中心
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);

				for (int i = 0; i < ivosCostStaticVOlength; i++) {
					// 如果数量为NUll或者为0,跳出
					if (vosCostStaticVO[i].getSl() == null || vosCostStaticVO[i].getSl().equals(new UFDouble(0))) {
						continue;
					}

					HalfprodVO voHalfprodVO = new HalfprodVO();
					HalfprodHeaderVO voHalfprodHeaderVO = new HalfprodHeaderVO();
					HalfprodItemVO voHalfprodItemVO = new HalfprodItemVO();
					/** ************************************************* */
					// 先根据转出工作中心查询成本中心
					String serrCT = "";
					// 转出成本中心
					String sOutCtcenterVOpk = null;
					// 转入成本中心
					String sInCtcenterVOpk = null;
					// 标示标题是否输出
					boolean isTitleAdded = false;
					// 标题
					String sErrT = "*****************" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000010")/* @res""介质编码"" */
							+ "：[" + vosCostStaticVO[i].getinvcode() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0001155")/* @res""名称"" */
							+ "：[" + vosCostStaticVO[i].getinvname() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0002282")/* @res""数量"" */
							+ "[" + vosCostStaticVO[i].getSl() + "]***********************\n";
					// 转出部门
					if (vosCostStaticVO[i].getzcbm() != null) {
						voHalfprodHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
					}
					// 转出成本中心
					if (vosCostStaticVO[i].getzcgzzx() != null) {
						sOutCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getzcgzzx(), 0);

						if (sOutCtcenterVOpk != null) {
							voHalfprodHeaderVO.setPk_ctcenter(sOutCtcenterVOpk); // 成本中心
						} else {
							if (isTitleAdded == false) {
								sErr += sErrT;
								isTitleAdded = true;
							}
							sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000011", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""转出工作中心{0}没有对应的基本生产成本中心"" */
									+ "\n";

							if (vosCostStaticVO[i].getzcbm() != null) {
								// 转出部门
								voHalfprodHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
								sOutCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getzcbm(), null, 0);
								if (sOutCtcenterVOpk != null) {
									voHalfprodHeaderVO.setPk_ctcenter(sOutCtcenterVOpk);
								} else {
									if (isTitleAdded == false) {
										sErr += sErrT;
										isTitleAdded = true;
									}
									sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000012", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""转出部门{0}没有对应的基本生产成本中心"" */
											+ "\n";
									continue;

								}
							} else {
								if (isTitleAdded == false) {
									sErr += sErrT;
									isTitleAdded = true;
								}
								sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000013")/* @res""转出部门和转出工作中心同时为空"" */
										+ "\n";
								continue;
							}
						}

					} else {
						if (vosCostStaticVO[i].getzcbm() != null) {
							// 转出部门
							voHalfprodHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
							sOutCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getzcbm(), null, 0);
							if (sOutCtcenterVOpk != null) {
								voHalfprodHeaderVO.setPk_ctcenter(sOutCtcenterVOpk);
							} else {
								if (isTitleAdded == false) {
									sErr += sErrT;
									isTitleAdded = true;
								}
								sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000012", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""转出部门{0}没有对应的基本生产成本中心"" */
										+ "\n";
								continue;

							}
						} else {
							if (isTitleAdded == false) {
								sErr += sErrT;
								isTitleAdded = true;
							}
							sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000013")/* @res""转出部门和转出工作中心同时为空"" */
									+ "\n";
							continue;
						}

					}

					/** ******************转入********************************* */
					// 根据消耗工作中心查询成本中心
					// 转入部门
					if (vosCostStaticVO[i].getxhbm() != null) {
						voHalfprodItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
						voHalfprodItemVO.setpk_difdeptdoc(vosCostStaticVO[i].getxhbm());
					}
					// 转入成本中心
					if (vosCostStaticVO[i].getxhgzzx() != null) {
						sInCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getxhgzzx());
						if (sInCtcenterVOpk == null) {
							if (isTitleAdded == false) {
								sErr += sErrT;
								isTitleAdded = true;
							}
							sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000014", null, new String[] { "[" + vosCostStaticVO[i].getsygzzxmc() + "]" })/* @res""转入工作中心{0}没有对应的成本中心"" */
									+ "\n";
							if (vosCostStaticVO[i].getxhbm() != null) {
								sInCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getxhbm(), null);
								if (sInCtcenterVOpk == null) {
									if (isTitleAdded == false) {
										sErr += sErrT;
										isTitleAdded = true;
									}
									sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""转入部门{0}没有对应的成本中心"" */
											+ "\n";
								}
							}
						}

					} else {
						if (vosCostStaticVO[i].getxhbm() != null) {
							sInCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getxhbm(), null);
							if (sInCtcenterVOpk == null) {
								if (isTitleAdded == false) {
									sErr += sErrT;
									isTitleAdded = true;
								}
								sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""转入部门{0}没有对应的成本中心"" */
										+ "\n";
							}
						}
					}
					// 成本中心不空，保存成本中心，否则保存部门，两者同时为空，不取
					if (sInCtcenterVOpk != null) {
						voHalfprodItemVO.setPk_ctcenter(sInCtcenterVOpk);
						voHalfprodItemVO.setpk_difctcenter(sInCtcenterVOpk);
					} else {

						if (vosCostStaticVO[i].getxhbm() != null) {
							voHalfprodItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
						} else {
							if (isTitleAdded == false) {
								sErr += sErrT;
								isTitleAdded = true;
							}
							sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000016")/* @res""转入部门和成本中心同时为空"" */
									+ "\n";
							continue;
						}
					}
					/** ****************表头处理***************** */
					// 公司
					voHalfprodHeaderVO.setPk_corp(pk_corp);
					// 工厂（库存组织）
					voHalfprodHeaderVO.setPk_calbody(pk_calbody);
					// 会计年
					voHalfprodHeaderVO.setCaccountyear(sSysinfos[2]);
					// 会计月
					voHalfprodHeaderVO.setCaccountmonth(sSysinfos[3]);
					// 单据号
					voHalfprodHeaderVO.setCbilltypecode("M3");
					// 制单日期
					voHalfprodHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6].toString()));
					// 单据日期
					voHalfprodHeaderVO.setDbilldate(new UFDate(endDate));
					// 开始日期
					voHalfprodHeaderVO.setdstartDate(new UFDate(startDate));
					// 结束日期
					voHalfprodHeaderVO.setdendDate(new UFDate(endDate));
					// 制单人
					voHalfprodHeaderVO.setCbillmaker(sSysinfos[5]);
					// 备注
					voHalfprodHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000017")/* @res""来源于计量系统"" */);

					/** ******************表体处理*************************** */
					// 公司
					voHalfprodItemVO.setPk_corp(pk_corp);
					// 工厂
					voHalfprodItemVO.setPk_calbody(pk_calbody);
					// 数量
					voHalfprodItemVO.setNnumber(vosCostStaticVO[i].getSl());
					// 来源模块
					voHalfprodItemVO.setCsourcemodulename(ConstVO.m_sModuleME); //
					// 辅助服务生产ID
					voHalfprodItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce());
					// 辅助服务基本ID
					voHalfprodItemVO.setPk_invbasdoc(vosCostStaticVO[i].getpk_invbasdoc());
					// 计划价,分项结转
					String isfxjz = ""; // 分项结转标记“Y”表示结转
					// 存货计价方式
					String spricemethod = null;
					UFDouble nplanprice = null;
					if (vosCostStaticVO[i].getPk_produce() != null && vosCostStaticVO[i].getPk_produce().trim().length() > 0) {
						StringBuffer sSQL = new StringBuffer(300);
						sSQL.append("select jhj,isfxjz,pricemethod ");
						sSQL.append("from bd_produce ");
						sSQL.append("where pk_corp = '" + sSysinfos[0] + "'");
						sSQL.append("and pk_calbody = '" + sSysinfos[1] + "'");
						sSQL.append("and pk_produce = '" + vosCostStaticVO[i].getPk_produce() + "' ");
						String sResult[][] = boCommonData.queryData(sSQL.toString());
						if (sResult[0][0] != null) {
							nplanprice = new UFDouble(sResult[0][0]);
						}
						if (sResult[0][1] != null) {
							isfxjz = sResult[0][1].trim();
						}
						if (sResult[0][2] != null) {
							spricemethod = sResult[0][2].trim();
						}
					}
					// 查看计价方式
					if (spricemethod == null) {
						if (isTitleAdded == false) {
							sErr += sErrT;
							isTitleAdded = true;
						}
						sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000018")/* @res""没有取到存货计价方式,请检查"" */
								+ "\n";
						continue;
					}
					if (spricemethod.equals("5")) {
						// 内部转移价
						// public String getPrice(String strInCtCenterID, String
						// strOutCtCenterID, String strInvbasdocID)
						String sinprice = null;
						UFDouble dInprice = null;
						if (sInCtcenterVOpk != null && sOutCtcenterVOpk != null) {
							boInpricesetBO = new InpricesetBO();
							sinprice = boInpricesetBO.getPrice(pk_calbody, sInCtcenterVOpk, sOutCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc());
							if (sinprice != null) {
								dInprice = new UFDouble(sinprice);
							}
						}
						if (dInprice != null) {
							nplanprice = dInprice;
						}
						if (nplanprice != null) {
							voHalfprodItemVO.setNplanprice(nplanprice);
							voHalfprodItemVO.setNprice(nplanprice);
							voHalfprodItemVO.setNplanmoney(nplanprice.multiply(vosCostStaticVO[i].getSl()).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP));
							voHalfprodItemVO.setNcost(nplanprice.multiply(vosCostStaticVO[i].getSl()).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP));
						}

					}

					// 成本要素处理；如果有转入成本中心非空，并且不是分项结转
					if (sInCtcenterVOpk != null && isfxjz.equals("N")) {
						String[] sFac = boWithOuter.getSubfacForInv(pk_corp, pk_calbody, sInCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc(), new UFBoolean(false));

						if (sFac == null) {
							sFac = boWithOuter.getSubfacForInv(pk_corp, pk_calbody, sInCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc(), new UFBoolean(true));
							// 没有获得成本次要素信息
							if (sFac == null) {
								if (isTitleAdded == false) {
									sErr += sErrT;
									isTitleAdded = true;
								}
								sErr += (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000019")/* @res""没有获得介质的成本要素"" */+ "\n");
								continue;

							}

						}

						voHalfprodItemVO.setPk_subfac(sFac[0]); // 成本要素
						voHalfprodItemVO.setPk_subfacset(sFac[1]); // 成本要素

					}
					// 保存VO 表头表体不能为空
					if (voHalfprodHeaderVO == null || voHalfprodItemVO == null) {
						continue;
					}
					voHalfprodVO.setParentVO(voHalfprodHeaderVO);
					voHalfprodVO.setChildrenVO(new HalfprodItemVO[] { voHalfprodItemVO });
					aryListHalfprodVO.add(voHalfprodVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// 转换
			if (aryListHalfprodVO.size() != 0) {
				vosHalfprodVO = new HalfprodVO[aryListHalfprodVO.size()];
				vosHalfprodVO = (HalfprodVO[]) aryListHalfprodVO.toArray(vosHalfprodVO);
				if (vosHalfprodVO.length > 0) {
					// 加上单据号
					String[] name = { "库存组织", "操作员" };
					String[] id = { pk_calbody.trim(), sSysinfos[5].trim() };
					BillCodeObjValueVO value = new BillCodeObjValueVO();
					value.setAttributeValue(name, id);
					String[] sbillcodes = boBillcodeRuleBO.getBatchBillCodes("M3", pk_corp, value, vosHalfprodVO.length);
					for (int i = 0; i < vosHalfprodVO.length; i++) {
						((HalfprodHeaderVO) (vosHalfprodVO[i].getParentVO())).setVbillcode(sbillcodes[i]);
					}
					HalfprodBO boHalfprodBO = new HalfprodBO();
					boolean isFinish = boHalfprodBO.insertArrayBatch(vosHalfprodVO);
					if (!isFinish) {
						sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000020")/* @res""数据插入出错"" */
								+ "\n";
					}
				}
			}

			System.out.print(sErr);
		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException("nc.bs.cm.outer.OuterBO.queryStatisticzzfzfwForM3(String, String, UFDate, UFDate) Exception!", e);
		}

		return sErr;
	}

	/**
	 * 从完工报告获取报废数量 取完工报告报废数量 <br>
	 * select scddid,wlbmid,pk_produce,sum(bfsl) from mm_wr_b where dr=0 group
	 * by scddid,wlbmid,pk_produce
	 * 
	 * 
	 * @param pk_corp
	 *            String
	 * @param pk_calbody
	 *            String
	 * @param startDate
	 *            UFDate
	 * @param endDate
	 *            UFDate
	 * @throws SQLException
	 * @return CostStaticVO[] 工作中心 private String m_gzzxid; 物料编码id private
	 *         String m_wlbmid; private String m_pk_produce; 生产订单 private String
	 *         m_scddid; private String m_scddh;
	 * 
	 * 数量(完工数量,报废数量....作业量) private UFDouble m_sl; private UFDouble m_fsl; 工序号
	 * private String m_gxh; private String m_jqGxh; private String m_jhGxh;
	 * 作业类型id 名称 private String m_atid; private String m_atname; 结束日期 private
	 * UFDate m_jsrq;
	 * 
	 * sSysinfos[0] ----- 公司 sSysinfos[1] ----- 工厂 sSysinfos[2] ----- 起始日期
	 * sSysinfos[3] ----- 结束日期 sSysinfos[4] ----- 单据类型,"I3" 产成品入库单 "I6" 材料出库单
	 * sSysinfos[5] ----- 当前用户 sSysinfos[6] ----- 当前日期
	 */
	public ProductVO[] queryBfsl(String[] sSysinfos, String pk_corp, String pk_calbody, UFDate startDate, UFDate endDate) throws nc.vo.cm.pub.CMBusinessException {
		ProductVO[] vosProductVO = null;
		try {
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			ArrayList aryListProductVO = new ArrayList(); //
			nc.vo.mm.pub.pub1030.CostStaticVO[] vosCostStaticVO = null;

			// 得到CostStaticVO
			vosCostStaticVO = mmToCm.queryStatisticBfslCM(pk_corp, pk_calbody, startDate, endDate);
			// 循环处理
			if (vosCostStaticVO != null) {
				ProductVO voProductVO = new ProductVO();
				ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
				ProductItemVO voProductItemVO = new ProductItemVO();
				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// 获得所有的成本中心
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {
					/** ****************表头处理***************** */
					// 先根据工作中心查询成本中心
					String sCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getGzzxid());

					if (sCtcenterVOpk != null) {
						voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); // 成本中心
					} else {
						// 没有获得对应的成本中心
						continue;
					}
					//
					voProductHeaderVO.setPk_corp(pk_corp); // 公司
					voProductHeaderVO.setPk_calbody(pk_calbody); // 工厂（库存组织）
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // 会计年
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // 会计月
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6]));// //
					// //单据日期
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6]));// 制单日期
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // 制单人

					/** ******************表体处理*************************** */
					voProductItemVO.setPk_corp(pk_corp);//
					voProductItemVO.setPk_calbody(pk_calbody);//
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid());// 订单ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // 产品基本ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // 产品生产
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // 数量
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC);//

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// 转换
			if (aryListProductVO.size() != 0) {
				vosProductVO = new ProductVO[aryListProductVO.size()];
				vosProductVO = (ProductVO[]) aryListProductVO.toArray(vosProductVO);
			}

		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException("nc.bs.cm.outer.OuterBO.queryStatisticBfsl(String, String, UFDate, UFDate) Exception!", e);
		}
		return vosProductVO;
	}

	/**
	 * 从完工报告获取完工数量 <br>
	 * 1.发生改判的需要从mm_zjbg取数 <br>
	 * 2.过滤出入库的物料 <br>
	 * 3.完工起止日期以完工报告的结束日期为准
	 * 
	 * @param pk_corp
	 *            String
	 * @param pk_calbody
	 *            String
	 * @param startDate
	 *            UFDate
	 * @param endDate
	 *            UFDate
	 * @throws SQLException
	 * @return CostStaticVO[]
	 * 
	 * 
	 */
	public ProductVO[] queryGXWgsl(String[] sSysinfos, String pk_corp, String pk_calbody, UFDate startDate, UFDate endDate) throws nc.vo.cm.pub.CMBusinessException {
		ProductVO[] vosProductVO = null;
		try {
			IMmToCm mmToCM = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			ArrayList aryListProductVO = new ArrayList(); //
			nc.vo.mm.pub.pub1030.CostStaticVO[] vosCostStaticVO = null;

			// 得到CostStaticVO
			vosCostStaticVO = mmToCM.getWgslToCostCM(pk_corp, pk_calbody, startDate.toString(), endDate.toString());
			// 循环处理
			if (vosCostStaticVO != null) {

				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// 获得所有的成本中心
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {

					ProductVO voProductVO = new ProductVO();
					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************表头处理***************** */
					// 先根据工作中心查询成本中心
					String sCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getGzzxid());

					if (sCtcenterVOpk != null) {
						voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); // 成本中心
					} else {
						// 没有获得对应的成本中心
						continue;
					}
					//
					voProductHeaderVO.setPk_corp(pk_corp); // 公司
					voProductHeaderVO.setPk_calbody(pk_calbody); // 工厂（库存组织）
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // 会计年
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // 会计月
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // //
					// //单据日期
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // 制单日期
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // 制单人

					/** ******************表体处理*************************** */
					voProductItemVO.setPk_corp(pk_corp); //
					voProductItemVO.setPk_calbody(pk_calbody); //
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid()); // 订单ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // 产品基本ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // 产品生产
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // 数量
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC); //

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// 转换
			if (aryListProductVO.size() != 0) {
				vosProductVO = new ProductVO[aryListProductVO.size()];
				vosProductVO = (ProductVO[]) aryListProductVO.toArray(vosProductVO);
			}

		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException("nc.bs.cm.outer.OuterBO.queryStatisticWgsl(String, String, UFDate, UFDate) Exception!", e);
		}
		return vosProductVO;
	}

	/**
	 * 从完工报告获取在产数量 <br>
	 * 1.生产订单的计划完工数量 - sum(完工数量 + 报废数量) <br>
	 * 2.生产订单为投放状态 zt=B <br>
	 * 3.完工报告为生产订单完工 lylx=0 <br>
	 * 
	 * <pre>
	 *     
	 *      
	 *       select scddid,w.wlbmid,w.pk_produce,jhwgsl -isnull(sum(sl),0)-isnull(sum(bfsl),0) from mm_wr_b w
	 *      left outer join mm_mo m on w.scddid=m.pk_moid
	 *      where w.dr=0 and m.dr=0 and m.zt='B' and w.lylx=1
	 *      group by scddid,w.wlbmid,w.pk_produce,jhwgsl
	 *       
	 *      
	 * </pre>
	 * 
	 * @param pk_corp
	 *            String
	 * @param pk_calbody
	 *            String
	 * @param startDate
	 *            UFDate
	 * @param endDate
	 *            UFDate
	 * @throws SQLException
	 * @return CostStaticVO[]
	 */
	public ProductVO[] queryGXZcsl(String[] sSysinfos, String pk_corp, String pk_calbody, UFDate startDate, UFDate endDate) throws nc.vo.cm.pub.CMBusinessException {
		ProductVO[] vosProductVO = null;
		try {

			ArrayList aryListProductVO = new ArrayList(); //
			nc.vo.mm.pub.pub1030.CostStaticVO[] vosCostStaticVO = null;

			// 得到CostStaticVO
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCm.getZzslToCostCM(pk_corp, pk_calbody, startDate.toString(), endDate.toString());
			// 循环处理
			if (vosCostStaticVO != null) {

				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// 获得所有的成本中心
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {

					ProductVO voProductVO = new ProductVO();
					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************表头处理***************** */
					// 先根据工作中心查询成本中心
					String sCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getGzzxid());

					if (sCtcenterVOpk != null) {
						voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); // 成本中心
					} else {
						// 没有获得对应的成本中心
						continue;
					}
					//
					voProductHeaderVO.setPk_corp(pk_corp); // 公司
					voProductHeaderVO.setPk_calbody(pk_calbody); // 工厂（库存组织）
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // 会计年
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // 会计月
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // //
					// //单据日期
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // 制单日期
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // 制单人

					/** ******************表体处理*************************** */
					voProductItemVO.setPk_corp(pk_corp); //
					voProductItemVO.setPk_calbody(pk_calbody); //
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid()); // 订单ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // 产品基本ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // 产品生产
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // 数量
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC); //

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// 转换
			if (aryListProductVO.size() != 0) {
				vosProductVO = new ProductVO[aryListProductVO.size()];
				vosProductVO = (ProductVO[]) aryListProductVO.toArray(vosProductVO);
			}

		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException("nc.bs.cm.outer.OuterBO.queryStatisticZcsl(String, String, UFDate, UFDate) Exception!", e);
		}
		return vosProductVO;
	}

	/**
	 * 产品生产id private String m_pk_produce; 消耗的订单 private String m_scddid; 数量
	 * private UFDouble m_sl; 转出部门 private String m_zcbm; 转出工作中心 private String
	 * m_zcgzzx; 产品基本ID private String m_pk_invbasdoc; 消耗部门 private String
	 * m_xhbm; 消耗工作中心 private String m_xhgzzx; 消耗产品基本ID private String
	 * m_pk_toinvbasdoc; 消耗产品生产ID private String m_pk_toProduce;
	 */
	public String queryWgfzfwForM6(String[] sSysinfos, String pk_corp, String pk_calbody, String startDate, String endDate) throws nc.vo.cm.pub.CMBusinessException {
		String sErr = "";
		CommonDataBO boCommonData = null;
		ServuseVO[] vosServuseVO = null;
		WithOuterBO boWithOuter = null;
		BillcodeRuleBO boBillcodeRuleBO = null;
		try {
			boCommonData = getCommonDataBO();

			ArrayList aryListServuseVO = new ArrayList(); //
			nc.vo.me.me1050.CostStaticVO[] vosCostStaticVO = null;

			// 得到CostStaticVO
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCm.getStatisticgxWgfzfwCM(pk_corp, pk_calbody, startDate, endDate);
			if (vosCostStaticVO == null || vosCostStaticVO.length == 0) {
				return sErr;
			}
			int ivosCostStaticVOlength = vosCostStaticVO.length;
			boWithOuter = new WithOuterBO();
			boBillcodeRuleBO = getBillcodeRuleBO();
			// //精度
			// int[0] ----- 数量的精度
			// int[1] ----- 单价的精度
			// int[2] ----- 金额的精度
			Integer[] iDataPrecision = boCommonData.getDataPrecision(sSysinfos[0]);
			// 循环处理

			if (vosCostStaticVO != null) {

				// 获得所有的成本中心
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {
					// 如果数量为NUll或者为0,跳出
					if (vosCostStaticVO[i].getSl() == null || vosCostStaticVO[i].getSl().equals(new UFDouble(0))) {
						continue;
					}
					ServuseVO voServuseVO = new ServuseVO();
					ServuseHeaderVO voServuseHeaderVO = new ServuseHeaderVO();
					ServuseItemVO voServuseItemVO = new ServuseItemVO();
					/** ****************表头处理***************** */
					String sErrT = "*****************" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000010")/* @res""介质编码"" */
							+ "：[" + vosCostStaticVO[i].getinvcode() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0001155")/* @res""名称"" */
							+ "：[" + vosCostStaticVO[i].getinvname() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0002282")/* @res""数量"" */
							+ "[" + vosCostStaticVO[i].getSl() + "]***********************\n";
					// 先根据转出工作中心查询成本中心
					String serrCT = "";
					// 转出部门
					if (vosCostStaticVO[i].getzcbm() != null) {
						voServuseHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
					}
					// 根据消耗工作中心查询成本中心
					// 转入成本中心
					String sInCtcenterVOpk = null;
					if (vosCostStaticVO[i].getxhgzzx() != null) {
						sInCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getxhgzzx());
						if (sInCtcenterVOpk == null) {
							// sErr += "转入工作中心"+"[" +
							// vosCostStaticVO[i].getsygzzxmc() +
							// "]"+"没有对应的成本中心"+"\n";
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000014", null, new String[] { "[" + vosCostStaticVO[i].getsygzzxmc() + "]" })/* @res""转入工作中心{0}没有对应的成本中心"" */
									+ "\n";// "+"[" +
							// vosCostStaticVO[i].getsygzzxmc() +
							// "]"+"
							if (vosCostStaticVO[i].getxhbm() != null) {
								sInCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getxhbm(), null);
								if (sInCtcenterVOpk == null) {

									sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""转入部门{0}没有对应的成本中心"" */
											+ "\n";// "+"[" +
									// vosCostStaticVO[i].getsybmmc()
									// + "]"+"
								}
							}
						}

					} else {
						if (vosCostStaticVO[i].getxhbm() != null) {
							sInCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getxhbm(), null);
							if (sInCtcenterVOpk == null) {
								// sErr += "转入部门"+"[" +
								// vosCostStaticVO[i].getsybmmc() +
								// "]"+"没有对应的成本中心"+"\n";
								sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""转入部门{0}没有对应的成本中心"" */
										+ "\n";// "+"[" +
								// vosCostStaticVO[i].getsybmmc()
								// + "]"+"
							}
						} else {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000021")/* @res""转入成本中心和转入部门不能同时为空"" */;
						}
					}
					// 成本中心不空，保存成本中心，否则保存部门，两者同时为空，不取
					if (sInCtcenterVOpk != null) {
						voServuseItemVO.setPk_ctcenter(sInCtcenterVOpk);
						voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
						// 差异承担成本中心
						voServuseItemVO.setPk_difctcenter(sInCtcenterVOpk);
						// 差异承担部门
						voServuseItemVO.setPk_difdeptdoc(vosCostStaticVO[i].getxhbm());
					} else {
						if (vosCostStaticVO[i].getxhbm() != null) {
							voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
							// 差异承担部门
							voServuseItemVO.setPk_difdeptdoc(vosCostStaticVO[i].getxhbm());
						} else {
							continue;
						}
					}

					//

					voServuseHeaderVO.setPk_corp(pk_corp); // 公司
					voServuseHeaderVO.setPk_calbody(pk_calbody); // 工厂（库存组织）
					voServuseHeaderVO.setCaccountyear(sSysinfos[2]); // 会计年
					voServuseHeaderVO.setCaccountmonth(sSysinfos[3]); // 会计月
					voServuseHeaderVO.setCbilltypecode("M6");
					// 单据日期
					voServuseHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6].toString()));
					// 制单日期
					voServuseHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6].toString()));

					// 开始日期
					voServuseHeaderVO.setdstartDate(new UFDate(startDate));
					// 结束日期
					voServuseHeaderVO.setdendDate(new UFDate(endDate));
					// 开始日期
					voServuseHeaderVO.setdstartDate(new UFDate(startDate));
					// 结束日期
					voServuseHeaderVO.setdendDate(new UFDate(endDate));

					voServuseHeaderVO.setCbillmaker(sSysinfos[5]); // 制单人
					voServuseHeaderVO.setPk_produce(vosCostStaticVO[i].getScddid()); // 辅助服务订单D
					voServuseHeaderVO.setPk_produce(vosCostStaticVO[i].getPk_produce());
					// 辅助服务生产ID
					voServuseHeaderVO.setPk_invbasdoc(vosCostStaticVO[i].getpk_invbasdoc());
					// 辅助服务基本ID
					voServuseHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000017")/* @res""来源于计量系统"" */);
					/** ******************表体处理*************************** */
					voServuseItemVO.setPk_corp(pk_corp); // 公司
					voServuseItemVO.setPk_calbody(pk_calbody); // 工厂
					// 数量为空则不取
					if (vosCostStaticVO[i].getSl() == null) {
						sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000022")/* @res""数量不能为空"" */;
						continue;
					}

					voServuseItemVO.setNnumber(vosCostStaticVO[i].getSl()); // 数量
					voServuseItemVO.setCsourcemodulename(ConstVO.m_sModuleME); //

					// 计划价
					String spricemethod = null;
					if (vosCostStaticVO[i].getPk_produce() != null && vosCostStaticVO[i].getPk_produce().trim().length() > 0) {
						StringBuffer sSQL = new StringBuffer(200);
						sSQL.append("select jhj,pricemethod ");
						sSQL.append("from bd_produce ");
						sSQL.append("where pk_corp = '" + sSysinfos[0] + "'");
						sSQL.append("and pk_calbody = '" + sSysinfos[1] + "'");
						sSQL.append("and pk_produce = '" + vosCostStaticVO[i].getPk_produce() + "' ");
						String sResult[][] = boCommonData.queryData(sSQL.toString());
						if (sResult == null || sResult.length == 0) {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000023")/* @res""没有得到服务的计划价和计价方式"" */
									+ "\n";
							continue;
						}
						if (sResult[0][1] == null || sResult[0][1].trim().length() == 0) {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000024")/* @res""没有得到服务的计价方式"" */
									+ "\n";
							continue;
						} else {
							spricemethod = sResult[0][1].trim();
						}
						if (spricemethod.equals("5")) {
							if (sResult[0][0] != null) {
								UFDouble nplanprice = new UFDouble(sResult[0][0]);
								voServuseItemVO.setNplanprice(nplanprice);
								//
								voServuseItemVO.setNactualprice(nplanprice);
								voServuseItemVO.setNplancost(nplanprice.multiply(new UFDouble(vosCostStaticVO[i].getSl())).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP));
								//
								voServuseItemVO.setNactualcost(nplanprice.multiply(new UFDouble(vosCostStaticVO[i].getSl())).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP));
							}
						}

					}
					// 成本要素处理
					String[] sFac = null;
					if (sInCtcenterVOpk != null) {
						sFac = boWithOuter.getSubfacForInv(pk_corp, pk_calbody, sInCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc(), new UFBoolean(true));

					}
					if (sFac == null && sInCtcenterVOpk != null) {
						// 没有获得成本次要素信息
						sErr += sErrT + (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000019")/* @res""没有获得介质的成本要素"" */+ "\n");
						continue;
					}
					if (sFac != null) {
						voServuseItemVO.setPk_subfac(sFac[0]); // 成本要素
						voServuseItemVO.setPk_subfacset(sFac[1]); // 成本要素
					}

					voServuseVO.setParentVO(voServuseHeaderVO);
					voServuseVO.setChildrenVO(new ServuseItemVO[] { voServuseItemVO });
					aryListServuseVO.add(voServuseVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)
			else {
				sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000025")/* @res""没有符合条件的数据"" */
						+ "\n";
			}

			// 转换
			if (aryListServuseVO.size() != 0) {
				vosServuseVO = new ServuseVO[aryListServuseVO.size()];
				vosServuseVO = (ServuseVO[]) aryListServuseVO.toArray(vosServuseVO);
				if (vosServuseVO.length > 0) {
					String[] name = { "库存组织", "操作员" };
					String[] id = { pk_calbody.trim(), sSysinfos[5].trim() };
					BillCodeObjValueVO value = new BillCodeObjValueVO();
					value.setAttributeValue(name, id);
					String[] sbillcodes = boBillcodeRuleBO.getBatchBillCodes("M6", pk_corp, value, vosServuseVO.length);
					for (int i = 0; i < vosServuseVO.length; i++) {
						((ServuseHeaderVO) (vosServuseVO[i].getParentVO())).setVbillcode(sbillcodes[i]);
					}
					ServuseBO boServuseBO = new ServuseBO();
					String[] sFinish = boServuseBO.insertArray(vosServuseVO);

				}
			}

			System.out.print(sErr);

			System.out.print(sErr);
		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException("nc.bs.cm.outer.OuterBO.queryStatisticWgfzfwForM6(String, String, UFDate, UFDate) Exception!", e);
		}

		return sErr;
	}

	/**
	 * 从完工报告获取完工数量 <br>
	 * 1.发生改判的需要从mm_zjbg取数 <br>
	 * 2.过滤出入库的物料 <br>
	 * 3.完工起止日期以完工报告的结束日期为准
	 * 
	 * @param pk_corp
	 *            String
	 * @param pk_calbody
	 *            String
	 * @param startDate
	 *            UFDate
	 * @param endDate
	 *            UFDate
	 * @throws SQLException
	 * @return CostStaticVO[]
	 * 
	 */
	public ProductVO[] queryWgsl(String[] sSysinfos, String pk_corp, String pk_calbody, UFDate startDate, UFDate endDate) throws nc.vo.cm.pub.CMBusinessException {
		ProductVO[] vosProductVO = null;
		try {
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			CommonDataBO cbo = new CommonDataBO();

			ArrayList aryListProductVO = new ArrayList(); //
			nc.vo.mm.pub.pub1030.CostStaticVO[] vosCostStaticVO = null;

			// 得到CostStaticVO
			vosCostStaticVO = mmToCm.queryStatisticWgslCM(pk_corp, pk_calbody, startDate, endDate);
			// 循环处理
			if (vosCostStaticVO != null) {

				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// 获得所有的成本中心
				// CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp,
				// pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {

					ProductVO voProductVO = new ProductVO();
					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************表头处理***************** */
					// 获得最后成本中心
					CostRtVO crt = cbo.getCostRt(pk_corp, pk_calbody, vosCostStaticVO[i].getScddid(), vosCostStaticVO[i].getPk_produce(), endDate.toString());

					if (crt != null) {
						CostRtHeaderVO hvo = (CostRtHeaderVO) crt.getParentVO();
						CostRtItemVO[] ivos = (CostRtItemVO[]) crt.getChildrenVO();

						voProductHeaderVO.setPk_ctcenter(ivos[ivos.length - 1].getCtcenterID());

					} else {
						continue;

					}

					// String sCtcenterVOpk =
					// getCTCenter(ctcentervos, null,
					// vosCostStaticVO[i].getGzzxid());

					// if (sCtcenterVOpk != null) {
					// voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); //成本中心
					// } else {
					// //没有获得对应的成本中心
					// continue;
					// }
					//
					voProductHeaderVO.setPk_corp(pk_corp); // 公司
					voProductHeaderVO.setPk_calbody(pk_calbody); // 工厂（库存组织）
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // 会计年
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // 会计月
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // //
					// //单据日期
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // 制单日期
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // 制单人

					/** ******************表体处理*************************** */
					voProductItemVO.setPk_corp(pk_corp); //
					voProductItemVO.setPk_calbody(pk_calbody); //
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid()); // 订单ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // 产品基本ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // 产品生产
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // 数量
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC); //

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// 转换
			if (aryListProductVO.size() != 0) {
				vosProductVO = new ProductVO[aryListProductVO.size()];
				vosProductVO = (ProductVO[]) aryListProductVO.toArray(vosProductVO);
			}

		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException("nc.bs.cm.outer.OuterBO.queryStatisticWgsl(String, String, UFDate, UFDate) Exception!", e);
		}
		return vosProductVO;
	}

	/**
	 * 从完工报告获取在产数量 <br>
	 * 1.生产订单的计划完工数量 - sum(完工数量 + 报废数量) <br>
	 * 2.生产订单为投放状态 zt=B <br>
	 * 3.完工报告为生产订单完工 lylx=0 <br>
	 * 
	 * <pre>
	 *     
	 *      
	 *       select scddid,w.wlbmid,w.pk_produce,jhwgsl -isnull(sum(sl),0)-isnull(sum(bfsl),0) from mm_wr_b w
	 *           left outer join mm_mo m on w.scddid=m.pk_moid
	 *           where w.dr=0 and m.dr=0 and m.zt='B' and w.lylx=1
	 *           group by scddid,w.wlbmid,w.pk_produce,jhwgsl
	 *       
	 *      
	 * </pre>
	 * 
	 * @param pk_corp
	 *            String
	 * @param pk_calbody
	 *            String
	 * @param startDate
	 *            UFDate
	 * @param endDate
	 *            UFDate
	 * @throws SQLException
	 * @return CostStaticVO[]
	 * 
	 * 
	 */
	public ProductVO[] queryZcsl(String[] sSysinfos, String pk_corp, String pk_calbody, UFDate startDate, UFDate endDate) throws nc.vo.cm.pub.CMBusinessException {
		ProductVO[] vosProductVO = null;
		try {

			CommonDataBO cbo = new CommonDataBO();
			ArrayList aryListProductVO = new ArrayList(); //
			nc.vo.mm.pub.pub1030.CostStaticVO[] vosCostStaticVO = null;

			// 得到CostStaticVO
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCm.queryStatisticZcslCM(pk_corp, pk_calbody, startDate, endDate);
			// 循环处理
			if (vosCostStaticVO != null) {

				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// 获得所有的成本中心
				// CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp,
				// pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {

					ProductVO voProductVO = new ProductVO();
					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************表头处理***************** */

					// 获得最后成本中心
					CostRtVO crt = cbo.getCostRt(pk_corp, pk_calbody, vosCostStaticVO[i].getScddid(), vosCostStaticVO[i].getPk_produce(), endDate.toString());

					if (crt != null) {
						CostRtHeaderVO hvo = (CostRtHeaderVO) crt.getParentVO();
						CostRtItemVO[] ivos = (CostRtItemVO[]) crt.getChildrenVO();

						voProductHeaderVO.setPk_ctcenter(ivos[ivos.length - 1].getCtcenterID());

					} else {
						continue;

					}

					// //先根据工作中心查询成本中心
					// String sCtcenterVOpk =
					// getCTCenter(ctcentervos, null,
					// vosCostStaticVO[i].getGzzxid());

					// if (sCtcenterVOpk != null) {
					// voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); //成本中心
					// } else {
					// //没有获得对应的成本中心
					// continue;
					// }
					//
					voProductHeaderVO.setPk_corp(pk_corp); // 公司
					voProductHeaderVO.setPk_calbody(pk_calbody); // 工厂（库存组织）
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // 会计年
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // 会计月
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6]));// //
					// //单据日期
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6]));// 制单日期
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // 制单人

					/** ******************表体处理*************************** */
					voProductItemVO.setPk_corp(pk_corp);//
					voProductItemVO.setPk_calbody(pk_calbody);//
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid());// 订单ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // 产品基本ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // 产品生产
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // 数量
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC);//

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// 转换
			if (aryListProductVO.size() != 0) {
				vosProductVO = new ProductVO[aryListProductVO.size()];
				vosProductVO = (ProductVO[]) aryListProductVO.toArray(vosProductVO);
			}

		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException("nc.bs.cm.outer.OuterBO.queryStatisticZcsl(String, String, UFDate, UFDate) Exception!", e);
		}
		return vosProductVO;
	}

	/**
	 * 10.21 在产品盘点表增加取数接口： 接口内容：获得当期没有签字入库量的生产订单，作为在产品盘点信息供修改 ；
	 * 取数条件：实际开工日期>=当期会计期间第一天 and <=当期会计期间最后一天 and 订单在当期没有签字入库量
	 * 
	 * </pre>
	 * 
	 * @param pk_corp
	 *            String
	 * @param pk_calbody
	 *            String
	 * @param startDate
	 *            UFDate
	 * @param endDate
	 *            UFDate
	 * @throws SQLException
	 * @return CostStaticVO[]
	 * 
	 * 
	 * sSysinfos[0] ----- 公司 sSysinfos[1] ----- 工厂 sSysinfos[2] ----- 起始日期
	 * sSysinfos[3] ----- 结束日期 sSysinfos[4] ----- 单据类型 sSysinfos[5] ----- 当前用户
	 * sSysinfos[6] ----- 当前日期 sSysinfos[7] ----- 会计年 sSysinfos[8] ----- 会计月
	 * sSysinfos[9] ----- 是否按照成本中心统计产量
	 * 
	 * 
	 */

	public String queryZcslForXG(String[] sSysinfos, String pk_corp, String pk_calbody, UFDate startDate, UFDate endDate) throws nc.vo.cm.pub.CMBusinessException {
		String sErr = "";
		ProductVO[] vosProductVO = null;
		try {

			CommonDataBO boCommonData = new CommonDataBO();
			// 制造提供的sql语句
			// StringBuffer strSql = new StringBuffer("select a.pk_moid,
			// a.scddh, a.scbmid,b.gzzxid, a.wlbmid, a.pk_produce, ");
			// strSql.append("(case when a.jhwgsl > a.sjwgsl then a.jhwgsl else
			// a.sjwgsl end - nvl(c.rksl, 0)) as zzl ");
			// strSql.append("from mm_mo a ");
			// strSql.append("inner join mm_mokz b on a.pk_moid = b.pk_moid ");
			// strSql.append("left outer join ");
			// strSql.append("(select icb.cfirstbillhid, sum(icb.ninnum) as rksl
			// from ic_general_b icb, ic_general_h ich ");
			// strSql.append("where icb.cgeneralhid = ich.cgeneralhid ");
			// strSql.append("and ich.dbilldate >= '" +
			// startDate.toString().trim() + "' and icb.csourcetype = 'A4' and
			// ich.fbillflag >= 3 and icb.dr = 0 and ich.dr = 0 ");
			// strSql.append("group by icb.cfirstbillhid) c ");
			// strSql.append("on a.pk_moid = c.cfirstbillhid ");
			// strSql.append("where a.sjkgrq >= '" + startDate.toString().trim()
			// + "' and a.sjkgrq <= '" + endDate.toString().trim() + "' and a.zt
			// = 'B' ");
			// strSql.append("and (a.jhwgsl > nvl(c.rksl, 0) or a.sjwgsl >
			// nvl(c.rksl, 0)) and a.pk_corp = '" + sSysinfos[0] + "' and a.gcbm
			// = '" + sSysinfos[1] + "' and a.dr = 0 ");
			// 根据老郑的要求，转接单已经接收的不算做在制，我改了sql
			// 如下，红颜色的是这次增加的内容

			StringBuffer strSql = new StringBuffer(" select a.pk_moid, a.scddh, a.scbmid, a.wlbmid, a.pk_produce, b.gzzxid,  ");
			strSql.append(" (case sign(a.jhwgsl - a.sjwgsl) when 1 then a.jhwgsl else a.sjwgsl end - isnull(c.rksl, 0) - isnull(d.yjsl, 0)) as zzl ");
			strSql.append(" from mm_mo a  ");
			strSql.append(" inner join mm_mokz b on a.pk_moid = b.pk_moid ");
			strSql.append(" left outer join  ");
			strSql.append(" (select icb.cfirstbillhid, sum(icb.ninnum) as rksl from ic_general_b icb, ic_general_h ich  ");
			strSql.append(" where icb.cgeneralhid = ich.cgeneralhid  ");
			strSql.append(" and ich.dbilldate >= '" + startDate.toString().trim() + "' and icb.csourcetype = 'A4' and ich.fbillflag = 3 and icb.dr = 0 and ich.dr = 0 ");
			strSql.append(" group by icb.cfirstbillhid) c on a.pk_moid = c.cfirstbillhid ");
			strSql.append(" left outer join  ");
			strSql.append(" (select yjscddid, sum(yjsl) as yjsl from sf_handandtake where yjrq > '" + startDate.toString().trim() + "' and zt = 'B' and dr = 0 group by yjscddid) d ");
			strSql.append(" on a.pk_moid = d.yjscddid  ");
			strSql.append(" where a.sjkgrq >= '" + startDate.toString().trim() + "' and a.sjkgrq <= '" + endDate.toString().trim() + "' and a.zt = 'B'  ");
			strSql.append(" and (a.jhwgsl > isnull(c.rksl, 0) or a.sjwgsl > isnull(c.rksl, 0))  ");
			strSql.append(" and (a.jhwgsl > isnull(d.yjsl, 0) or a.sjwgsl > isnull(d.yjsl, 0)) ");
			strSql.append(" and a.pk_corp = '" + sSysinfos[0] + "' and a.gcbm = '" + sSysinfos[1] + "' and a.dr = 0 ");

			String[][] sAllPKs = boCommonData.queryData(strSql.toString());

			HashMap hmPkAndiPos = new HashMap();
			ArrayList aryListProductVO = new ArrayList(); //

			// 获得所有的成本中心
			CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);

			// 循环处理
			if (sAllPKs != null && sAllPKs.length > 0) {

				int iPkslength = sAllPKs.length;

				for (int i = 0; i < iPkslength; i++) {

					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************表头处理***************** */

					// 如果按照成本中心统计产量,根据工作中心和部门查询成本中心
					if (sSysinfos[9].equals("Y")) {
						String[] sCtcenters = getCTCenterForM2(ctcentervos, sAllPKs[i][2], sAllPKs[i][3]);
						if (sCtcenters != null && sCtcenters.length > 0 && sCtcenters[0] != null) {
							voProductHeaderVO.setPk_ctcenter(sCtcenters[0]); // 成本中心
						} else {
							// 没有获得对应的成本中心
							sErr += (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000001")/* @res""没有获得对应的成本中心"" */+ "\n");
							continue;

						}
					}

					// 公司
					voProductHeaderVO.setPk_corp(pk_corp); // 公司
					// 工厂（库存组织）
					voProductHeaderVO.setPk_calbody(pk_calbody);
					// 会计年
					voProductHeaderVO.setCaccountyear(sSysinfos[7]);
					// 会计月
					voProductHeaderVO.setCaccountmonth(sSysinfos[8]);
					// 单据类型
					voProductHeaderVO.setCbilltypecode(ConstVO.m_sBillZCPPD);
					// 部门
					voProductHeaderVO.setPk_deptdoc(sAllPKs[i][2]);
					// 备注
					voProductHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000026")/* @res""来自于生产制造系统"" */);
					/** ******************表尾处理*************************** */
					// 业务日期
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[3])); // //
					// //单据日期
					// 制单日期
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // 制单日期
					// 制单人
					voProductHeaderVO.setCbillmaker(sSysinfos[5]);

					/** ******************表体处理*************************** */
					// a.pk_moid 0,
					// 1 a.scddh,
					// 2 a.scbmid,
					// 3 b.gzzxid,
					// 4 a.wlbmid,
					// 5 a.pk_produce,
					// 6 (c.wgsl - d.rksl)
					// 公司
					voProductItemVO.setPk_corp(pk_corp); //
					// 工厂
					voProductItemVO.setPk_calbody(pk_calbody); //
					// //订单ID
					voProductItemVO.setPk_order(sAllPKs[i][0]);
					// 产品基本ID
					voProductItemVO.setPk_invbasdoc(sAllPKs[i][3]);
					// 产品生产ID
					voProductItemVO.setPk_produce(sAllPKs[i][4]);
					// 数量
					voProductItemVO.setNnumber(new UFDouble(sAllPKs[i][6]));
					// 模块号
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleMO); //
					// 判断是否存在表头
					Integer iPos = (Integer) hmPkAndiPos.get(voProductHeaderVO.getPk_ctcenter() + voProductHeaderVO.getDbilldate());
					if (iPos != null) {
						ProductVO oldProductVO = (ProductVO) aryListProductVO.get(iPos.intValue());
						ProductItemVO[] vosProductItemVO = (ProductItemVO[]) oldProductVO.getChildrenVO();
						ProductItemVO[] vosNewProductItemVO = new ProductItemVO[vosProductItemVO.length + 1];
						System.arraycopy(vosProductItemVO, 0, vosNewProductItemVO, 0, vosProductItemVO.length);
						vosNewProductItemVO[vosProductItemVO.length] = voProductItemVO;
						oldProductVO.setChildrenVO(vosNewProductItemVO);
					} else {
						ProductVO voProductVO = new ProductVO();
						voProductVO.setParentVO(voProductHeaderVO);
						voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
						aryListProductVO.add(voProductVO);
						hmPkAndiPos.put(voProductHeaderVO.getPk_ctcenter() + voProductHeaderVO.getDbilldate(), new Integer(aryListProductVO.size() - 1));
					}

				} // for (int i = 0; i <iPkslength ; i++)
			} // if(vosCostStaticVO!=null)

			// 加上单据号
			if (!aryListProductVO.isEmpty()) {
				/** **************************单据号处理************************************************* */
				BillcodeRuleBO boBillcodeRuleBO = new BillcodeRuleBO();
				String[] name = { "库存组织", "操作员" };
				String[] id = { sSysinfos[1].trim(), sSysinfos[5].trim() };
				BillCodeObjValueVO value = new BillCodeObjValueVO();
				value.setAttributeValue(name, id);
				String[] sbillcodes = null;
				sbillcodes = boBillcodeRuleBO.getBatchBillCodes(ConstVO.m_sBillZCPPD, sSysinfos[0], value, aryListProductVO.size());

				vosProductVO = new ProductVO[aryListProductVO.size()];
				for (int i = 0; i < aryListProductVO.size(); i++) {
					vosProductVO[i] = (ProductVO) (aryListProductVO.get(i));
					((ProductHeaderVO) (vosProductVO[i].getParentVO())).setVbillcode(sbillcodes[i]);
				}

			}
			if (sErr.length() > 0) {
				ConstVO.outputString(sErr);
			}
			ProductBO boProductBO = new ProductBO();
			boProductBO.insertArray(vosProductVO);

		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException("nc.bs.cm.outer.OuterBO.queryStatisticZcsl(String, String, UFDate, UFDate) Exception!", e);
		}
		return sErr;
	}

	/**
	 * 产品生产id private String m_pk_produce; 消耗的订单 private String m_scddid; 数量
	 * private UFDouble m_sl;
	 * 
	 * 转出部门 private String m_zcbm; 转出工作中心 private String m_zcgzzx; 产品基本ID
	 * private String m_pk_invbasdoc; 消耗部门 private String m_xhbm; 消耗工作中心 private
	 * String m_xhgzzx; 消耗产品基本ID private String m_pk_toinvbasdoc; 消耗产品生产ID
	 * private String m_pk_toProduce;
	 */
	public String queryzzfzfwForM7(String[] sSysinfos, String pk_corp, String pk_calbody, String startDate, String endDate) throws nc.vo.cm.pub.CMBusinessException {
		// 提示信息
		String sErr = "";
		ServuseVO[] vosServuseVO = null;
		// BOS
		CommonDataBO boCommonData = null;
		WithOuterBO boWithOuter = null;
		BillcodeRuleBO boBillcodeRuleBO = null;
		InpricesetBO boInpricesetBO = null;
		try {

			ArrayList aryListServuseVO = new ArrayList(); //
			nc.vo.me.me1050.CostStaticVO[] vosCostStaticVO = null;
			boCommonData = getCommonDataBO();
			// 得到CostStaticVO
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCm.getStatisticgxBcpCM(pk_corp, pk_calbody, startDate, endDate);
			if (vosCostStaticVO == null || vosCostStaticVO.length == 0) {
				return sErr;
			}
			int ivosCostStaticVOlength = vosCostStaticVO.length;
			boWithOuter = new WithOuterBO();
			boBillcodeRuleBO = getBillcodeRuleBO();
			// //精度
			// int[0] ----- 数量的精度
			// int[1] ----- 单价的精度
			// int[2] ----- 金额的精度
			Integer[] iDataPrecision = boCommonData.getDataPrecision(sSysinfos[0]);

			// 循环处理
			if (vosCostStaticVO != null) {

				// 获得所有的成本中心
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {
					// 如果数量为NUll或者为0,跳出
					if (vosCostStaticVO[i].getSl() == null || vosCostStaticVO[i].getSl().equals(new UFDouble(0))) {
						continue;
					}
					ServuseVO voServuseVO = new ServuseVO();
					ServuseHeaderVO voServuseHeaderVO = new ServuseHeaderVO();
					ServuseItemVO voServuseItemVO = new ServuseItemVO();
					/** ****************表头处理***************** */
					String sErrT = "*****************" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000010")/* @res""介质编码"" */
							+ "：[" + vosCostStaticVO[i].getinvcode() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0001155")/* @res""名称"" */
							+ "：[" + vosCostStaticVO[i].getinvname() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0002282")/* @res""数量"" */
							+ "[" + vosCostStaticVO[i].getSl() + "]***********************\n";
					// 转出成本中心
					String sOutCtcenterVOpk = null;
					// 转入成本中心
					String sInCtcenterVOpk = null; //

					/** *********转出********************************************* */
					// 先根据工作中心查找成本中心，如果为空，按照部门查找
					if (vosCostStaticVO[i].getzcgzzx() != null) {
						// 先根据工作中心查找对应成本中心
						sOutCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getzcgzzx(), 1, "Y");
						if (sOutCtcenterVOpk != null) {
							voServuseHeaderVO.setPk_ctcenter(sOutCtcenterVOpk); // 成本中心
						} else {
							// sErr += "转出工作中心"+"[" +
							// vosCostStaticVO[i].getgybmmc() +
							// "]"+"没有对应的是辅助生产且为末级的成本中心"+"\n";
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000027", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""转出工作中心{0}没有对应的是辅助生产且为末级的成本中心"" */
									+ "\n";// "+"[" +
							// vosCostStaticVO[i].getgybmmc() +
							// "]"+"
							// 然后根据部门查找成本中心
							if (vosCostStaticVO[i].getzcbm() != null) {
								voServuseHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
								sOutCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getzcbm(), null, 1, "Y");
								if (sOutCtcenterVOpk != null) {
									voServuseHeaderVO.setPk_ctcenter(sOutCtcenterVOpk);
								} else {
									// sErr += "转出部门"+"[" +
									// vosCostStaticVO[i].getgybmmc() +
									// "]"+"没有对应的是辅助生产且为末级成本中心"+"\n";
									sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000028", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""转出部门{0}没有对应的是辅助生产且为末级成本中心"" */
											+ "\n";// "+"[" +
									// vosCostStaticVO[i].getgybmmc()
									// + "]"+"
									continue;
								}
							} else {
								continue;
							}
						}

					} else {
						if (vosCostStaticVO[i].getzcbm() != null) {
							voServuseHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
							// 根据部门查询对应成本中心
							sOutCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getzcbm(), null, 1, "Y");
							if (sOutCtcenterVOpk != null) {
								voServuseHeaderVO.setPk_ctcenter(sOutCtcenterVOpk);
							} else {
								// sErr += "转出部门"+"[" +
								// vosCostStaticVO[i].getgybmmc() +
								// "]"+"没有对应的是辅助生产且为末级成本中心"+"\n";
								sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000028", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""转出部门{0}没有对应的是辅助生产且为末级成本中心"" */
										+ "\n";// "+"[" +
								// vosCostStaticVO[i].getgybmmc()
								// + "]"+"
								continue;

							}
						} else {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000029")/* @res""转出部门和转出工作中心不能同时为空"" */
									+ "\n";
							continue;
						}

					}

					/** ******************转入*********************************************************** */
					// 根据消耗工作中心查询成本中心
					if (vosCostStaticVO[i].getxhgzzx() != null) {
						sInCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getxhgzzx());
						if (sInCtcenterVOpk == null) {
							// sErr += "转入工作中心"+"[" +
							// vosCostStaticVO[i].getsygzzxmc() +
							// "]"+"没有对应的成本中心"+"\n";
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000014", null, new String[] { "[" + vosCostStaticVO[i].getsygzzxmc() + "]" })/* @res""转入工作中心{0}没有对应的成本中心"" */
									+ "\n";// "+"[" +
							// vosCostStaticVO[i].getsygzzxmc() +
							// "]"+"
							if (vosCostStaticVO[i].getxhbm() != null) {
								voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
								sInCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getxhbm(), null);
								if (sInCtcenterVOpk == null) {
									// sErr += "转入部门"+"[" +
									// vosCostStaticVO[i].getsybmmc() +
									// "]"+"没有对应的成本中心"+"\n";
									sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""转入部门{0}没有对应的成本中心"" */
											+ "\n";// "+"[" +
									// vosCostStaticVO[i].getsybmmc()
									// + "]"+"
								}
							}
						}

					} else {
						if (vosCostStaticVO[i].getxhbm() != null) {
							voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
							sInCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getxhbm(), null);
							if (sInCtcenterVOpk == null) {
								// sErr += "转入部门"+"[" +
								// vosCostStaticVO[i].getsybmmc() +
								// "]"+"没有对应的成本中心"+"\n";
								sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""转入部门{0}没有对应的成本中心"" */
										+ "\n";// "+"[" +
								// vosCostStaticVO[i].getsybmmc()
								// + "]"+"
							}
						}
					}
					// 成本中心不空，保存成本中心，否则保存部门，两者同时为空，不取
					if (sInCtcenterVOpk != null) {
						voServuseItemVO.setPk_ctcenter(sInCtcenterVOpk);
						// 差异承担成本中心
						voServuseItemVO.setPk_difctcenter(sInCtcenterVOpk);
						voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
						// 差异承担部门
						voServuseItemVO.setPk_difdeptdoc(vosCostStaticVO[i].getxhbm());
					} else {
						if (vosCostStaticVO[i].getxhbm() != null) {
							voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
							// 差异承担部门
							voServuseItemVO.setPk_difdeptdoc(vosCostStaticVO[i].getxhbm());
						} else {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000021")/* @res""转入成本中心和转入部门不能同时为空"" */;
							continue;
						}
					}
					//

					voServuseHeaderVO.setPk_corp(pk_corp); // 公司
					voServuseHeaderVO.setPk_calbody(pk_calbody); // 工厂（库存组织）
					voServuseHeaderVO.setCaccountyear(sSysinfos[2]); // 会计年
					voServuseHeaderVO.setCaccountmonth(sSysinfos[3]); // 会计月
					voServuseHeaderVO.setCbilltypecode("M7");
					// 单据日期
					voServuseHeaderVO.setDbilldate(new UFDate(sSysinfos[6].toString()));
					// 制单日期
					voServuseHeaderVO.setDbillmakedate(new UFDate(sSysinfos[6].toString()));
					// 开始日期
					voServuseHeaderVO.setdstartDate(new UFDate(startDate));
					// 结束日期
					voServuseHeaderVO.setdendDate(new UFDate(endDate));
					voServuseHeaderVO.setCbillmaker(sSysinfos[5]); // 制单人
					voServuseHeaderVO.setPk_produce(vosCostStaticVO[i].getPk_produce());
					// 辅助服务生产ID
					voServuseHeaderVO.setPk_invbasdoc(vosCostStaticVO[i].getpk_invbasdoc());
					// 辅助服务基本ID
					voServuseHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000017")/* @res""来源于计量系统"" */);
					/** ******************表体处理*************************** */
					voServuseItemVO.setPk_corp(pk_corp); // 公司
					voServuseItemVO.setPk_calbody(pk_calbody); // 工厂
					voServuseItemVO.setNnumber(vosCostStaticVO[i].getSl()); // 数量
					voServuseItemVO.setCsourcemodulename(ConstVO.m_sModuleME); //
					// 计划价
					UFDouble nplanprice = null;
					String spricemethod = null;

					if (vosCostStaticVO[i].getPk_produce() != null && vosCostStaticVO[i].getPk_produce().trim().length() > 0) {
						StringBuffer sSQL = new StringBuffer(200);
						sSQL.append("select jhj,pricemethod ");
						sSQL.append("from bd_produce ");
						sSQL.append("where pk_corp = '" + sSysinfos[0] + "'");
						sSQL.append("and pk_calbody = '" + sSysinfos[1] + "'");
						sSQL.append("and pk_produce = '" + vosCostStaticVO[i].getPk_produce() + "' ");
						String sResult[][] = boCommonData.queryData(sSQL.toString());
						if (sResult == null || sResult.length == 0) {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000023")/* @res""没有得到服务的计划价和计价方式"" */
									+ "\n";
							continue;
						}
						if (sResult[0][1] == null || sResult[0][1].trim().length() == 0) {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000024")/* @res""没有得到服务的计价方式"" */
									+ "\n";
							continue;
						} else {
							spricemethod = sResult[0][1].trim();
						}
						if (sResult[0][0] != null) {
							nplanprice = new UFDouble(sResult[0][0]);
						}

					}
					if (spricemethod.equals("5")) {
						// 内部转移价
						// public String getPrice(String strInCtCenterID, String
						// strOutCtCenterID, String strInvbasdocID)
						String sinprice = null;
						UFDouble dInprice = null;
						if (sInCtcenterVOpk != null && sOutCtcenterVOpk != null) {
							boInpricesetBO = new InpricesetBO();
							sinprice = boInpricesetBO.getPrice(pk_calbody, sInCtcenterVOpk, sOutCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc());
							if (sinprice != null) {
								dInprice = new UFDouble(sinprice);
							}
						}
						if (dInprice != null) {
							nplanprice = dInprice;
						}
						if (nplanprice != null) {
							voServuseItemVO.setNplanprice(nplanprice);
							voServuseItemVO.setNplancost(nplanprice.multiply(vosCostStaticVO[i].getSl()).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP));
							voServuseItemVO.setNactualprice(nplanprice);
							voServuseItemVO.setNactualcost(nplanprice.multiply(vosCostStaticVO[i].getSl()).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP));
						}

					}

					// 成本要素处理
					if (sInCtcenterVOpk != null) {
						String[] sFac = boWithOuter.getSubfacForInv(pk_corp, pk_calbody, sInCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc(), new UFBoolean(true));

						if (sFac == null) {
							// 没有获得成本次要素信息
							sErr += sErrT + (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000019")/* @res""没有获得介质的成本要素"" */+ "\n");
							continue;
						}

						voServuseItemVO.setPk_subfac(sFac[0]); // 成本要素
						voServuseItemVO.setPk_subfacset(sFac[1]); // 成本要素

					}
					//	
					voServuseVO.setParentVO(voServuseHeaderVO);
					voServuseVO.setChildrenVO(new ServuseItemVO[] { voServuseItemVO });
					aryListServuseVO.add(voServuseVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// 转换
			if (aryListServuseVO.size() != 0) {
				vosServuseVO = new ServuseVO[aryListServuseVO.size()];
				vosServuseVO = (ServuseVO[]) aryListServuseVO.toArray(vosServuseVO);
				if (vosServuseVO.length > 0) {
					String[] name = { "库存组织", "操作员" };
					String[] id = { pk_calbody.trim(), sSysinfos[5].trim() };
					BillCodeObjValueVO value = new BillCodeObjValueVO();
					value.setAttributeValue(name, id);
					String[] sbillcodes = boBillcodeRuleBO.getBatchBillCodes("M7", pk_corp, value, vosServuseVO.length);
					for (int i = 0; i < vosServuseVO.length; i++) {
						((ServuseHeaderVO) (vosServuseVO[i].getParentVO())).setVbillcode(sbillcodes[i]);
					}
					ServuseBO boServuseBO = new ServuseBO();
					String[] sFinish = boServuseBO.insertArray(vosServuseVO);

				}
			}

			System.out.print(sErr);
		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException("nc.bs.cm.outer.OuterBO.queryStatisticzzfzfwForM7(String, String, UFDate, UFDate) Exception!", e);
		}
		return sErr;
	}

	/**
	 * 函数功能:处理生产订单的关闭 参数: String sCorpID ----- 公司 String sCalbodyID ----- 工厂
	 * String[] sSourceOrderIDs ----- 生产制造生产订单Id UFBoolean bAccount -----
	 * 月末结帐还是取消月末结帐 * 返回值: * 异常: *
	 * 
	 */
	public void setMOStatus(String sCorpID, String sCalbodyID, String[] sSourceOrderIDs, UFBoolean bAccount) throws CMBusinessException {
		try {
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());

			if (bAccount.booleanValue()) {
				// 月末结帐调用
				mmToCm.afterCoCM(sCorpID, sCalbodyID, sSourceOrderIDs);
			} else {
				// 取消月末结帐调用
				mmToCm.afterCo2CM(sCorpID, sCalbodyID, sSourceOrderIDs);
			}
		} catch (CMBusinessException ee) {
			throw ee;
		} catch (Exception e) {
			reportException(e);
			throw new CMBusinessException(e.getMessage(), e);
		}
	}

	/**
	 * 升级数据。
	 * 
	 * 创建日期：(2001-11-20 11:55:39)
	 * 
	 * @throws CMBusinessException
	 */
	public void updateData(String oldVersionNumber, String newVersionNumber) throws RemoteException {
		System.out.println("－－－－－－－注意：执行升级代码开始－－－－－");

		if (oldVersionNumber == null || oldVersionNumber.trim().length() == 0) {
			return;
		}

		CommonDataBO cbo = null;

		try {
			cbo = getCommonDataBO();

			if (oldVersionNumber.equals("3.0") || oldVersionNumber.equals("3.002")) {

				// 01 设置物料生产档案的是否使用工艺路线 为“Y”
				String sql = "select distinct isuseroute from  bd_produce ";
				String[][] sResult = cbo.queryDataNoTranslate(sql);

				if (sResult.length == 1) { // 主要是为邢钢，邢钢升级不需要更新此参数

					sql = "update bd_produce set isuseroute = 'Y' where sfcbdx = 'Y' and pk_calbody in (select distinct pk_calbody from mm_kzcs)";
					cbo.execDataNoTranslate(sql);

				}

				// 02 根据原“是否按成本中心统计产量”参数值设置物料生产档案上是否按成本中心统计产量属性
				sql = "select distinct isctoutput from  bd_produce ";
				sResult = cbo.queryDataNoTranslate(sql);

				if (sResult.length == 1) { // 主要是为邢钢，邢钢升级不需要更新此参数
					sql = "update bd_produce set isctoutput = (select case when csz2 = '是' then 'Y' else 'N' end from mm_kzcs where csbm = 'sfacbzx' and gcbm = bd_produce.pk_calbody) "
							+ "where sfcbdx = 'Y' and pk_calbody in (select distinct pk_calbody from mm_kzcs)";
					cbo.execData(sql);

				}

				// 03 去掉“总帐数据传输”和“总帐会计科目设置”功能点
				sql = "delete from sm_funcregister where fun_code in('30066010','30066020','30063080','30063085')";
				cbo.execDataNoTranslate(sql);
				sql = "delete from sm_butnregister where fun_code like '30063085%'";
				cbo.execDataNoTranslate(sql);

				// 04 删除实际成本多余的按钮
				sql = "delete from sm_butnregister where cfunid in ('CM01AA10000000039ADG','CM01AA10000000039ADJ','CM01AA10000000039ADK','CM01AA10000000039ADN','CM01AA10000000039ADP')";
				cbo.execDataNoTranslate(sql);

				// 05 删除以前的230清除工具中没有删除的客商档案注册脚本
				sql = "delete from bd_interfaceexec where pk_interface = 'CA500000000000000001'";
				cbo.execDataNoTranslate(sql);

			}

		} catch (Exception e) {
			CMLogger.error("升级数据失败",e);
			throw new RemoteException("升级数据失败",e);
		} finally {
			System.out.println("－－－－－－－注意：执行升级代码结束－－－－－");
		}
	}
	
	public StordocVO[] queryStordocVO(StordocVO condStordocVO, Boolean isAnd)throws CMBusinessException{
		OuterDMO dmo=null;
		try {
			dmo = new OuterDMO();
		} catch (SystemException e) {
			throw new CMBusinessException("取仓库信息出错",e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			throw new CMBusinessException("取仓库信息出错",e);
		}
		try {
			return dmo.queryByVO(condStordocVO, isAnd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new CMBusinessException("取仓库信息出错",e);
		}
		
		
	} 
}