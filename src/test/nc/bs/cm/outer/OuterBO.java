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
 * ��������������ϵͳ�����Ľӿ��� * ����:����
 * 
 * ��������:(2003-4-14 19:21:17)
 * 
 * �޸ļ�¼������:
 * 
 * �޸���:
 * 
 */
public class OuterBO extends nc.bs.pub.BusinessObject implements nc.bs.sm.install.IUpdateData {
	/**
	 * OuterBO ������ע�⡣
	 */
	public OuterBO() {
		super();
	}

	/**
	 * ��������:�����ⵥ
	 * 
	 * ����: sSysinfos[0] ----- ��˾ sSysinfos[1] ----- ���� sSysinfos[2] ----- ��ʼ����
	 * sSysinfos[3] ----- �������� sSysinfos[4] ----- ��������,"I3" ����Ʒ��ⵥ "I6" ���ϳ��ⵥ
	 * sSysinfos[5] ----- ��ǰ�û� sSysinfos[6] ----- ��ǰ���� sSysinfos[7] -----
	 * �Ƿ�ɱ�����Y��N�� ��ʶ����sInvs�ǳɱ������ǲ��� sSysinfos[8]-------�����
	 * sSysinfos[9]-------����� String[] sRDCLs ----- ������ String[] sInvs ----- ���
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

			// ȡ��
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

				// ������гɱ������ID,δ��˵ķǳɱ�����ȡ
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

				// ������д���Ļ���ID������ID
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

				// ������еĳɱ�����
				CtcenterVO[] ctcentervos = findAllCTCenters(sCorpID, sCalbodyID);
				HashMap hmapStuffVO = new HashMap();
				ArrayList aryPullStuffErrVO = new ArrayList();
				// ������еĳɱ�Ҫ��
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

				// ѭ������
				for (int i = 0; i < bvos.length; i++) {
					// ��������VO
					nc.vo.ia.bill.BillHeaderVO voBillHeaderVO = (nc.vo.ia.bill.BillHeaderVO) bvos[i].getParentVO();
					nc.vo.ia.bill.BillItemVO[] vosBillItemVO = (nc.vo.ia.bill.BillItemVO[]) bvos[i].getChildrenVO();
					String sErrNote = "";
					for (int j = 0; j < vosBillItemVO.length; j++) {
						
						if(vosBillItemVO[j]==null)
							continue;

						/**
						 * ******************************** 1
						 * δ��˵�ԭ���ϲ�ȡ*******************************************************
						 */
						String pk_invmandoc = vosBillItemVO[j].getCinventoryid();
						if (pk_invmandoc == null || pk_invmandoc.trim().length() == 0) {
							System.out.println("�������:û�еõ�����Ĺ���ID!!");
							continue;
						}
						int iIndex = Integer.parseInt(invHashMap.get(pk_invmandoc).toString());
						String pk_invbasdoc = sAllInvBasProdPKs[iIndex][0];
						String pk_produce = sAllInvBasProdPKs[iIndex][1];
						String pk_invcl = sAllInvBasProdPKs[iIndex][3];

						if (vosBillItemVO[j].getCauditorid() == null && vInvs.contains(pk_produce) == false) {

							sErrNote = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000000")/* @res""ԭ����δ���"" */;
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
						 * ���ݹ������ĺͲ��ż���ɱ�����*********************************************************
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
							// û�л�ö�Ӧ�ĳɱ�����
							sErrNote = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000001")/* @res""û�л�ö�Ӧ�ĳɱ�����"" */;
							PullStuffErrVO voPullStuffErrVO = getPullStuffErrVO(sSysinfos, voBillHeaderVO, vosBillItemVO[j], sErrNote, pk_invbasdoc);
							aryPullStuffErrVO.add(voPullStuffErrVO);
							continue;

						}
						/**
						 * ********************************* 3
						 * ����ɱ�Ҫ��*****************************************
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
						// �Ȱ�������pK���ҳɱ�Ҫ��
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
						// Ȼ���մ���������
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
							// sErrNote = ("�����ڳɱ�����"+"[" + sCtcentercode + ":" +
							// sCtcentername + "]"+"û�ж�Ӧ�ĳɱ�Ҫ��");
							// getStrByID("10092040","UPP-000002",null,new
							// String[]{name,value+""})/
							sErrNote = (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000002", null, new String[] { "[" + sCtcentercode + ":" + sCtcentername + "]" })/* @res""�����ڳɱ�����{0}û�ж�Ӧ�ĳɱ�Ҫ��"" */);// "+"["
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
						// ���鵥�ۺͽ���Ƿ�Ϊ��*****************************************************/
						// UFDouble nnum = vosBillItemVO[j].getNnumber();
						// UFDouble nCost = vosBillItemVO[j].getNmoney();
						// if ((nnum == null || nnum.toDouble().doubleValue() ==
						// 0.00) && (nCost == null ||
						// nCost.toDouble().doubleValue() == 0.00)) {
						// sErrNote = ("�۸�ͽ��ͬʱΪ�ջ���Ϊ��!!");
						// PullStuffErrVO voPullStuffErrVO =
						// getPullStuffErrVO(sSysinfos, voBillHeaderVO,
						// vosBillItemVO[j], sErrNote, pk_invbasdoc);
						// aryPullStuffErrVO.add(voPullStuffErrVO);
						// continue;
						// }
						/** ****************** */
						if (vosBillItemVO[j].getVbomcode() == null && vosBillItemVO[j].getVproducebatch() != null) {
							// sErrNote = "�����ж�����"+"[" +
							// vosBillItemVO[j].getVproducebatch() +
							// "]"+"��û�гɱ�����";
							sErrNote = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000003", null, new String[] { "[" + vosBillItemVO[j].getVproducebatch() + "]" })/* @res""�����ж�����{0}��û�гɱ�����"" */;// "+"["
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
	 * SessionBean�ӿ��еķ�����
	 * <p>
	 * ���������EJB Container���ã���Զ��Ҫ����ĳ����е�������� ����
	 * <p>
	 * �������ڣ�(2001-2-15 16:34:02)
	 * 
	 * @exception nc.vo.cm.pub.CMBusinessException
	 *                �쳣˵����
	 */
	public void ejbCreate() {
	}

	/***************************************************************************
	 * ��������: ��øù�˾�����µ����гɱ����ģ��Ա���� ���Ż������������Ӧ�ĳɱ����ġ�
	 * 
	 * ����: String : sCorpID : ��˾ String : sCalbodyID : ����
	 * 
	 * ����ֵ: CtcenterVO[]
	 * 
	 * �쳣:
	 * 
	 * �������ڣ�(2003-5-29 10:22:53)
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

	// ���ݺ�BO
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
	 * ��������:���BOMֵ
	 * 
	 * ����: ��˾ �����֯ �û� �������ID ���� ������λ
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
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
			dvo.setPk_corp(sCorpID); // ��˾ID
			dvo.setGcbm(sCalbodyID); // ����ID
			dvo.setOperid(sUserID); // ����ԱID

			dvo.setWlbmid(sInvBasID); // ����ID(���Ż�������)
			dvo.setJldwid(sMeaID); // ������λID

			dvo.setId("A"); // A:���ϣ�B:����
			dvo.setSl(new UFDouble(1)); // ����
			dvo.setLogdate(new UFDate(sDate)); // ��¼����

			dvo.setYxrq(new UFDate(sDate)); // ��Ч����
			dvo.setCm(new Integer(0)); // ����
			dvo.setSfsh(new UFBoolean("Y")); // �Ƿ������
			dvo.setSffp(new UFBoolean("N")); // �Ƿ��Ƿ�Ʒ
			dvo.setSfplgz(new UFBoolean("N")); // �Ƿ�����������

			// ȡ����BOM
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
	 * ��������:��óɱ��ĳɱ�·�ߣ����ذ�˳��ĳɱ�����PK
	 * 
	 * ����: ��˾ �����֯ �û� �������ID ������λID ����
	 * 
	 * ����ֵ:��˳��ĳɱ�����PK
	 * 
	 * �쳣:
	 * 
	 * �������ڣ�(2003-6-3 10:42:24)
	 */
	public CtcenterVO[] getCMRoutes(String[] sSysinfos) throws nc.vo.cm.pub.CMBusinessException {
		CommonDataBO cbo = null;
		CtcenterVO[] cvos = null;
		try {
			cbo = getCommonDataBO();

			DisRouteVO[] dvos = getRoutes(sSysinfos);

			// �ɱ���������1������ 2����������
			String sCTCenterType = cbo.getParaValue(sSysinfos[0], sSysinfos[1], ConstVO.m_iPara_CBZXFW);

			ArrayList vTemp = new ArrayList();
			ArrayList vKeys = new ArrayList();

			for (int i = dvos.length - 1; i >= 0; i--) {
				DisRouteVO dvo = dvos[i];
				CtcenterVO cvo = null;
				String sWK = dvo.getGzzxid();

				if (sCTCenterType.equals(ConstVO.m_sPara_GetCtcenterByWk)) {
					// �ɱ����İ��������Ķ���
					CtcenterVO[] thisvos = getCTCenterForDept(sSysinfos[0], sSysinfos[1], null, sWK);

					if (thisvos != null && thisvos.length != 0) {
						cvo = thisvos[0];
					}
				} else {
					// �ɱ����İ����Ŷ���
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
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * @return nc.bs.ia.pub.CommonData
	 */
	private CommonDataBO getCommonDataBO() throws Exception {

		return new CommonDataBO();
	}

	/**
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * �������ڣ�(2003-5-29 10:22:53)
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
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * �������ڣ�(2003-5-29 10:22:53)
	 */
	private String getCTCenter(CtcenterVO[] ctcentervos, String sDeptID, String sWkID, int sfjbscflag// 
	) {
		String spk = null;

		CtcenterVO vo = null;
		if (ctcentervos == null || ctcentervos.length <= 0)
			return null;

		for (int i = 0, iLength = ctcentervos.length; i < iLength; i++) {

			// �Ƿ��������
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
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * �������ڣ�(2003-5-29 10:22:53)
	 */
	private String getCTCenter(CtcenterVO[] ctcentervos, String sDeptID, String sWkID, int sfjbscflag,//
			String blastlevelflag) {
		String spk = null;

		CtcenterVO vo = null;
		if (ctcentervos == null || ctcentervos.length <= 0)
			return null;

		for (int i = 0, iLength = ctcentervos.length; i < iLength; i++) {
			// �Ƿ�ĩ��
			if (!(ctcentervos[i].getBlastlevelflag().toString().trim().equals("Y"))) {
				continue;
			}
			// �Ƿ��������
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
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * @return nc.bs.ia.pub.CommonData
	 */
	private CtcenterBO getCtcenterBO() throws Exception {

		return new CtcenterBO();
	}

	/**
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * �������ڣ�(2003-5-29 10:22:53)
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
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * �������ڣ�(2003-5-29 10:22:53)
	 */
	public String[] getCTCenterForM2(CtcenterVO[] ctcentervos, String sDeptID, String sWkID) throws nc.vo.cm.pub.CMBusinessException {
		String spk = null;
		String sctcode = null;
		String sctname = null;
		CtcenterVO vo = null;
		if (ctcentervos == null || ctcentervos.length <= 0)
			return null;
		// �ȸ��ݹ������Ĳ�ѯ
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

		// ���û���ҵ�,���ݲ��Ų�ѯ
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
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * �������ڣ�(2003-5-29 10:22:53)
	 */
	private String getCTCenterForM2(String[][] sAllCtcenterPKs, String sDeptID, String sWkID) throws Exception {
		String spk_ctcenter = null;

		if (sAllCtcenterPKs == null || sAllCtcenterPKs.length <= 0)
			return null;
		// �ȸ��ݹ������Ĳ�ѯ
		for (int i = 0, iLength = sAllCtcenterPKs.length; i < iLength; i++) {
			if (sWkID != null && sAllCtcenterPKs[i][1] != null) {
				if (sWkID.equals(sAllCtcenterPKs[i][1])) {
					spk_ctcenter = sAllCtcenterPKs[i][0];
					break;
				}
			}

		}
		// ���û���ҵ�,���ݲ��Ų�ѯ
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
	 * ��������:������ʵ�����
	 * 
	 * ����: String sCorpID ----- ��˾ String sAccSubj ----- ��Ŀ String sCurrType
	 * ----- ���� String strYear ----- ����� String strMonth ----- �����
	 * nc.vo.glcom.ass.AssVO[] avos ----- �������� Integer id ----- ���������id String
	 * sFlag ----- QC:�ڳ���� QM:��ĩ��� JFFS:�跽������ DFFS:���������� JE:��������
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
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

			// ��˾
			String[] sCorps = new String[1];
			sCorps[0] = sCorpID;
			queryvo.setPk_corp(sCorps);

			// ��Ŀ
			String[] sSubj = new String[1];
			sSubj[0] = sAccSubj;
			queryvo.setPk_accsubj(sSubj);

			// ����
			queryvo.setPk_currtype(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002033381", "UPP2002033381-000024")/* "����" */);

			// �Ƿ��ڼ��ѯ
			queryvo.setQueryByPeriod(true);

			// ������
			queryvo.setYear(strYear);

			// ����ڼ�(�������)
			queryvo.setPeriod(strMonth);

			// ��ֹ����ڼ�
			queryvo.setEndPeriod(strMonth);

			// �Ƿ����δ����
			queryvo.setIncludeUnTallyed(false);

			// �Ƿ����ʵʱƾ֤
			queryvo.setRealtimeVoucher(false);

			// �Ƿ���ʾ���ڳ��޷������¼
			queryvo.setShowZeroAmountRec(false);

			// ������Ϣ
			queryvo.setGroupFields(new int[] { nc.vo.glcom.balance.GLQueryKey.K_GLQRY_CORP, nc.vo.glcom.balance.GLQueryKey.K_GLQRY_SUBJ });

			// ������
			queryvo.setAssVos(avos);

			if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLQC)) {
				// ȡ�ڳ����
				gvos = gbo.getBeginBalance(queryvo);

				if (gvos != null && gvos.length > 0) {
					// ��Ŀ���� 1 : �跽 2 : ����
					for (int i = 0; i < gvos.length; i++) {
						Integer iAccOrient = gvos[i].getAccOrient();
						if (iAccOrient.intValue() == 1)
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount().sub(gvos[i].getLocalcreditamount()));
						else
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount().sub(gvos[i].getLocaldebitamount()));
					}
				}
			} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLQM)) {
				// ȡ��ĩ���
				gvos = gbo.getEndBalance(queryvo);

				if (gvos != null && gvos.length > 0) {
					// ��Ŀ���� 1 : �跽 2 : ����
					for (int i = 0; i < gvos.length; i++) {
						Integer iAccOrient = gvos[i].getAccOrient();
						if (iAccOrient.intValue() == 1)
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount().sub(gvos[i].getLocalcreditamount()));
						else
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount().sub(gvos[i].getLocaldebitamount()));
					}
				}
			} else {
				// ȡ������
				gvos = gbo.getOccur(queryvo);

				if (gvos != null && gvos.length > 0) {
					if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLJFFS)) {
						// �跽���
						for (int i = 0; i < gvos.length; i++) {
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount());
						}
					} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLDFFS)) {
						// �������
						for (int i = 0; i < gvos.length; i++) {
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount());
						}
					} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLJE)) {
						// ����� = �� �� ��(��) ���� �� - ��(��)
						UFDouble dDebit = new UFDouble(0);
						UFDouble dCrebit = new UFDouble(0);
						for (int i = 0; i < gvos.length; i++) {
							if (gvos[i].getLocaldebitamount() != null) {
								dDebit = gvos[i].getLocaldebitamount();
							}

							if (gvos[i].getLocalcreditamount() != null) {
								dCrebit = gvos[i].getLocalcreditamount();
							}

							// ��Ŀ���� 1 : �跽 2 : ����
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
	 * ��������:������ʵ�����
	 * 
	 * ����: String sCorpID ----- ��˾ String sAccSubj ----- ��Ŀ //String sCurrType
	 * ----- ���� String strYear ----- ����� String strMonth ----- �����
	 * nc.vo.glcom.ass.AssVO[] avos ----- �������� //Integer id ----- ���������id String
	 * sFlag ----- QC:�ڳ���� QM:��ĩ��� JFFS:�跽������ DFFS:���������� JE:��������
	 * 
	 * UFBoolean bOnlyDebit : �Ƿ�ֻȡ�跽��� ��true ��ֻȡ�跽��� false ��ֻȡ������� null ����ȡ�������ƣ�
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
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

			// ��˾
			String[] sCorps = new String[1];
			sCorps[0] = sCorpID;
			// ���ʸ�������䶯��modified by liming
			// queryvo.setPk_corp(sCorps);

			// �ӹ�˾pkȡ�����ʲ�pk
			// GLOrgBookAccBO bo = new GLOrgBookAccBO();
			// String pk_glorgbook =
			// bo.getDefaultGlOrgBookVOByPk_EntityOrg(sCorpID).getPrimaryKey();

			IGLOrgBookAcc glOrgBookAcc = (IGLOrgBookAcc) NCLocator.getInstance().lookup(IGLOrgBookAcc.class.getName());
			String pk_glorgbook = glOrgBookAcc.getDefaultGlOrgBookVOByPk_EntityOrg(sCorpID).getPrimaryKey();

			String[] sGlOrgBook = new String[1];
			sGlOrgBook[0] = pk_glorgbook;
			queryvo.setPk_glorgbook(sGlOrgBook);

			queryvo.setPk_accsubj(sAccSubjs);

			// ����
			queryvo.setPk_currtype(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2002033381", "UPP2002033381-000024")/* "����" */);

			// �Ƿ��ڼ��ѯ
			queryvo.setQueryByPeriod(true);

			// ������
			queryvo.setYear(strYear);

			// ����ڼ�(�������)
			queryvo.setPeriod(strMonth);

			// ��ֹ����ڼ�
			queryvo.setEndPeriod(strMonth);

			// �Ƿ����δ����
			queryvo.setIncludeUnTallyed(false);

			// �Ƿ����ʵʱƾ֤
			queryvo.setRealtimeVoucher(false);

			// �Ƿ���ʾ���ڳ��޷������¼
			queryvo.setShowZeroAmountRec(false);

			// ������Ϣ
			// queryvo.setGroupFields(new int[] {
			// nc.vo.glcom.balance.GLQueryKey.K_GLQRY_CORP,
			// nc.vo.glcom.balance.GLQueryKey.K_GLQRY_SUBJ });
			queryvo.setGroupFields(new int[] { nc.vo.glcom.balance.GLQueryKey.K_GLQRY_PK_GLORGBOOK, nc.vo.glcom.balance.GLQueryKey.K_GLQRY_SUBJ });

			// ������
			queryvo.setAssVos(avos);

			if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLQC)) {
				// ȡ�ڳ����
				gvos = gbo.getBeginBalance(queryvo);

				if (gvos != null && gvos.length > 0) {
					// ��Ŀ���� 1 : �跽 2 : ����
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
				// ȡ��ĩ���
				gvos = gbo.getEndBalance(queryvo);

				if (gvos != null && gvos.length > 0) {
					// ��Ŀ���� 1 : �跽 2 : ����
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
				// ȡ������
				gvos = gbo.getOccur(queryvo);

				if (gvos != null && gvos.length > 0) {
					if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLJFFS)) {
						// �跽���
						for (int i = 0; i < gvos.length; i++) {
							dReturnValue = dReturnValue.add(gvos[i].getLocaldebitamount());
						}
					} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLDFFS)) {
						// �������
						for (int i = 0; i < gvos.length; i++) {
							dReturnValue = dReturnValue.add(gvos[i].getLocalcreditamount());
						}
					} else if (sFlag.equals(ConstVO.DATASRC_FORNAME_GLJE)) {
						// ����� = �� �� ��(��) ���� �� - ��(��)
						UFDouble dDebit = new UFDouble(0);
						UFDouble dCrebit = new UFDouble(0);
						for (int i = 0; i < gvos.length; i++) {
							if (gvos[i].getLocaldebitamount() != null) {
								dDebit = gvos[i].getLocaldebitamount();
							}

							if (gvos[i].getLocalcreditamount() != null) {
								dCrebit = gvos[i].getLocalcreditamount();
							}

							// ��Ŀ���� 1 : �跽 2 : ����
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

	// �õ���Ʒ�Ĳ�����Ϣ6�Ƿ�ʹ�óɱ�����ͳ�Ʋ���,7�Ƿ�ʹ�ù���·��
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
	 * ��������:��ô������Ʒ��ⵥ��
	 * 
	 * ����: sSysinfos[0] ----- ��˾ sSysinfos[1] ----- ���� sSysinfos[2] ----- ��ʼ����
	 * sSysinfos[3] ----- �������� sSysinfos[4] ----- ��������,"I3" ����Ʒ��ⵥ "I6" ���ϳ��ⵥ
	 * sSysinfos[5] ----- ��ǰ�û� sSysinfos[6] ----- ��ǰ���� sSysinfos[7] ----- �����
	 * sSysinfos[8] ----- ����� sSysinfos[9] ----- �Ƿ񰴳ɱ�����ͳ�Ʋ��� String[] sRDCLs
	 * ----- ������ String[] sInvs ----- ��� UFBoolean isWaster ---- �Ƿ�ȡ��Ʒ
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
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
			/**������ת��Ϊ�ӿ����������
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
			ConstVO.outputString("�������ӿں�ʱ:" + sTime);
			// �ж��Ƿ������˶��� String sCorpID,String sModule
			boolean isMoStarted = boCommonData.isModuleStarted(sSysinfos[0], ConstVO.m_sModuleMO).booleanValue();

			if (vosIABillVO != null) {
				String sCorpID = sSysinfos[0];
				String sCalbodyID = sSysinfos[1];
				/** **************************��ѯ�����еķ�Ʒ�ֿ�*************************************** */
				// ��ѯ�����еķ�Ʒ�ֿ�
				String sSQL = " select  pk_stordoc  from  bd_stordoc  where  pk_corp = '" + sSysinfos[0] + "' and  pk_calbody = '" + sSysinfos[1] + "' and  gubflag = 'Y' ";
				String[][] sResult = boCommonData.queryData(sSQL);
				HashSet hsWasterStors = new HashSet();
				if (sResult.length != 0) {
					for (int i = 0; i < sResult.length; i++) {
						hsWasterStors.add(sResult[i][0]);
					}
				}

				/** **************************������д���Ļ���ID������ID*************************************** */
				//
				sSQL = " select  a.pk_invmandoc, a.pk_invbasdoc,b.pk_produce, b.sfcbdx,c.invname,c.invcode,b.isctoutput,b.isuseroute  from  bd_invmandoc a,bd_produce b,bd_invbasdoc c";
				sSQL = sSQL + " where  b.sfcbdx='Y' ";
				sSQL = sSQL + " and b.pk_calbody = '" + sSysinfos[1] + "' ";
				sSQL = sSQL + " and a.pk_invmandoc = b.pk_invmandoc and b.pk_invbasdoc=c.pk_invbasdoc ";
				String[][] sInvPKs = boCommonData.queryData(sSQL);
				/** ****************************�������Ͷ�ţ��깤�Ķ���ID**************************************************************** */
				String[][] sOrders = null;
				if (isMoStarted == true) {
					// �������Ͷ�ţ��깤�Ķ���ID
					sSQL = " select mo.scddh, mo.pk_moid,mo.wlbmid,mo.pk_produce ";
					sSQL = sSQL + " from  mm_mo mo inner join bd_produce b on mo.pk_produce=b.pk_produce";
					sSQL = sSQL + " where b.iscostbyorder='Y' and mo.dr = 0";
					sSQL = sSQL + " and mo.pk_corp = '" + sSysinfos[0] + "'";
					sSQL = sSQL + " and  mo.gcbm = '" + sSysinfos[1] + "'";
					sSQL = sSQL + " and  (mo.zt = 'B' or mo.zt = 'C' or mo.zt='D') ";
					// sSQL+=" and sjkgrq<='"+sSysinfos[3]+"'";
					sOrders = boCommonData.queryData(sSQL);
				}

				/** ****************************������еĳɱ����ĺͲ���**************************************************** */

				// ������еĳɱ�����
				sSQL = ("select a.pk_ctcenter,b.pk_wk,b.pk_deptdoc  ");
				sSQL += (" from cm_ctcenter a, cm_ctcenter_b b  ");
				sSQL += (" where a.pk_ctcenter=b.pk_ctcenter ");
				sSQL += (" and	a.pk_corp = '" + sCorpID + "'  ");
				sSQL += (" and 	a.pk_calbody = '" + sCalbodyID + "'  ");
				sSQL += " and a.blastlevelflag = 'Y' and a.jbscflag = 0";
				String[][] sAllCtcenterPKs = boCommonData.queryData(sSQL);
				// ����
				String[][] sDepts = null;
				sSQL = " select a.deptcode,a.deptname,a.pk_deptdoc from bd_deptdoc a ";
				sDepts = boCommonData.queryData(sSQL);

				/** *******************��������˹���·��,ȡ������������Ʒ��Ӧ������Ʒ********************************************************************* */
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

				/** ****************************��ʼ���ݴ���************************************************************* */
				for (int i = 0; i < vosIABillVO.length; i++) {
					nc.vo.ia.bill.BillHeaderVO voBillHeaderVO = (nc.vo.ia.bill.BillHeaderVO) vosIABillVO[i].getParentVO();
					nc.vo.ia.bill.BillItemVO[] vosBillItemVO = (nc.vo.ia.bill.BillItemVO[]) vosIABillVO[i].getChildrenVO();
					// �����ж�
					if (voBillHeaderVO == null) {
						// sErr += "û�еõ�������ݱ�ͷ!!\n";
						continue;
					}

					if (voBillHeaderVO.getCwarehouseid() == null || hsWasterStors.contains(voBillHeaderVO.getCwarehouseid()) != isWaster.booleanValue()) {
						// sErr += "�ֿⲻ��Ϊ��,�Ҳ���Ϊ��Ʒ��!!\n";
						sErr += voBillHeaderVO.getVbillcode() + ":" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000004")/* @res""�ֿⲻ��Ϊ��,�Ҳ���Ϊ��Ʒ��""*/ 
								+ "\n";

						continue; // �ֿ�Ϊ�ղ�ȡ����Ʒȡ��Ʒ�⣬�깤ȡ�Ƿ�Ʒ��
					}

					/** ***********************�ӱ���*********************************************** */
					for (int j = 0; j < vosBillItemVO.length; j++) {
						ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
						ProductItemVO voProductItemVO = new ProductItemVO();
						/** ***********************�������********************************************** */
						// �������ID
						String sProductManID = vosBillItemVO[j].getCinventoryid();
						String sProductBasID = "";
						String sProductProduceID = "";
						String sSFCBDX = "N";
						String sinvname = null;
						String sinvcode = null;
						for (int m = 0, mLength = sInvPKs.length; m < mLength; m++) {
							if (sProductManID.equals(sInvPKs[m][0])) {
								voProductItemVO.setPk_invbasdoc(sInvPKs[m][1]); // ��Ʒ����ID
								voProductItemVO.setPk_produce(sInvPKs[m][2]); // ��Ʒ����ID
								sSFCBDX = sInvPKs[m][3];
								sinvname = sInvPKs[m][4];
								sinvcode = sInvPKs[m][5];
								break;
							}
						}
						// ����ֵ:bPara[0]�Ƿ��ճɱ�����ͳ�Ʋ���bPara[1]�Ƿ����ù���·��
						// private boolean[] getProducePara(String[][] products,
						// String pk_invmandoc)
						boolean[] bPara = getProducePara(sInvPKs, sProductManID);
						if (bPara == null) {
							sErr += (voBillHeaderVO.getVbillcode() + ":[" + sinvname + "][" + sinvcode + "]����������û�ж������:�Ƿ��ճɱ�����ͳ�Ʋ������Ƿ����ù���·��" + "\n");
							continue;
						}
						boolean isctoutput = bPara[0];
						boolean isuseroute = bPara[1];

						// ��������ù���·��,���Ұ��ճɱ�����ͳ�Ʋ�������ݲ��ţ��������Ķ�Ӧ�ɱ�����
						if (isctoutput == true && isuseroute == false) {
							String ctcenterid = getCTCenterForM2(sAllCtcenterPKs, voBillHeaderVO.getCdeptid(), vosBillItemVO[j].getCwp());
							if (ctcenterid == null || ctcenterid.length() == 0) {
								String deptcode = null;
								String deptname = null;
								// ���Ҳ���
								for (int m = 0; m < sDepts.length; m++) {
									if (sDepts[m][2].equals(voBillHeaderVO.getCdeptid())) {
										deptcode = sDepts[m][0];
										deptname = sDepts[m][1];
										break;
									}
								}
								// û�л�ö�Ӧ�ĳɱ�����
								// sErr += "����:" + deptname + "[" + deptcode +
								// "]" + "û�ж�Ӧ�ĳɱ�����\n";
								sErr += voBillHeaderVO.getVbillcode() + ":"
										+ nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000005", null, new String[] { "[" + deptname + ":" + deptcode + "]" })/* @res""����{0}û�ж�Ӧ�ĳɱ�����"" */
										+ "\n";

								continue;
							}
							voProductHeaderVO.setPk_ctcenter(ctcenterid); // �ɱ�����
						} else if (isctoutput == true && isuseroute == true) {
							/** *******************������ճɱ�����ͳ�Ʋ���,�������˹���·�߶�Ӧ���ɱ�����********************************************************************* */
							//
							// ������ɱ�����
							CostRtVO crt = boCommonData.getCostRt(sSysinfos[0], sSysinfos[1], voProductItemVO.getPk_order(), voProductItemVO.getPk_produce(), sSysinfos[6]);

							sCtcenterID = null;
							if (crt != null) {
								CostRtHeaderVO hvo = (CostRtHeaderVO) crt.getParentVO();
								CostRtItemVO[] ivos = (CostRtItemVO[]) crt.getChildrenVO();
								sCtcenterID = ivos[ivos.length - 1].getCtcenterID();

							} else {
								// ȥ�Ҷ�Ӧ������Ʒ
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
										// sinvcode + "]û�ж��幤��·��!!\n");
										sErr += voBillHeaderVO.getVbillcode()
												+ ":"
												+ (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000007", null, new String[] { "[" + sinvname + "][" + sinvcode + "]" })/* @res""{0}û�ж��幤��·��"" */+ "\n");

										continue;

									}
								} else {
									sErr += voBillHeaderVO.getVbillcode()
											+ ":"
											+ (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000007", null, new String[] { "[" + sinvname + "][" + sinvcode + "]" })/* @res""{0}û�ж��幤��·��"" */+ "\n");
									continue;
								}

							}

							if (sCtcenterID == null) {
								sErr += voBillHeaderVO.getVbillcode() + ":" + "�ɱ�����Ϊ��\n";
								continue;
							}

							voProductHeaderVO.setPk_ctcenter(sCtcenterID);
						}
						// ��Դ��Ϣ
						voProductItemVO.setCsourcebillid(voBillHeaderVO.getPrimaryKey());
						voProductItemVO.setCsourcebillitemid(vosBillItemVO[j].getPrimaryKey());
						voProductItemVO.setCsourcebilltype(sBillTypecode);
						voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleIA);
						// ��˾
						voProductItemVO.setPk_corp(voBillHeaderVO.getPk_corp());
						// ����
						if("1078".equals(voBillHeaderVO.getPk_corp())){							
							voProductItemVO.setPk_calbody(voBillHeaderVO.getCstockrdcenterid());//add by hk
						}
						voProductItemVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
						// ��Դ���ݺ�
						voProductItemVO.setVsourcebillcode(voBillHeaderVO.getVbillcode());
						// ����
						if (vosBillItemVO[j].getNnumber() == null)
							continue;

						voProductItemVO.setNnumber(vosBillItemVO[j].getNnumber());

						/** ************************��������˶���,�������������Ż����������Ʒ*************************************************** */
						if (isMoStarted == true) {
							sOrderID = null;

							if (vosBillItemVO[j].getVproducebatch() != null) {

								for (int m = 0, mLength = sOrders.length; m < mLength; m++) {
									if (vosBillItemVO[j].getVproducebatch().equals(sOrders[m][0])) {
										sOrderID = sOrders[m][1]; // ����ID���������ţ�
										break;
									}
								}
							}
							if (sOrderID == null) {
								voProductItemVO.setPk_order(voProductItemVO.getPk_produce());
							} else {
								voProductItemVO.setPk_order(sOrderID); // ����ID���������ţ�
							}

						}
						
						voBillHeaderVO.setDbilldate(new UFDate(sSysinfos[6]));

						// �ж��Ƿ���ڱ�ͷ
						iPos = (Integer) hmPkAndiPos.get(voProductHeaderVO.getPk_ctcenter() + voBillHeaderVO.getDbilldate());

						if (iPos != null) {
							ProductVO oldProductVO = (ProductVO) aryListvosProductVO.get(iPos.intValue());
							ProductItemVO[] oldItems = (ProductItemVO[]) oldProductVO.getChildrenVO();
							// �ϲ�����
							boolean isCombine = false;
							for (int m = 0, ilength = oldItems.length; m < ilength; m++) {
								// �ж϶������
								if ((oldItems[m].getPk_order() == null && voProductItemVO.getPk_order() == null) // 1-ͬʱΪ��
										|| ((oldItems[m].getPk_order() != null && voProductItemVO.getPk_order() != null) // 2-ͬʱ��λΪ����
										&& (oldItems[m].getPk_order().trim().equals(voProductItemVO.getPk_order().trim())))) // �������

								{
									// �жϲ�Ʒ�Ƿ����
									if (//
									(oldItems[m].getPk_produce() != null && voProductItemVO.getPk_produce() != null) // 1-��������
											&& oldItems[m].getPk_produce().trim().equals(voProductItemVO.getPk_produce().trim()) // ��Ʒ��ͬ
									) {
										// �ϲ�ItemVO
										// ���ĺϲ���־
										isCombine = true;
										// �������
										oldItems[m].setNnumber(oldItems[m].getNnumber().add(voProductItemVO.getNnumber()));

									}
								}

							}
							// ���û�кϲ�,��׷��һ��
							if (isCombine == false) {
								ProductItemVO[] newItems = new ProductItemVO[oldItems.length + 1];
								System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);
								newItems[oldItems.length] = voProductItemVO;
								oldProductVO.setChildrenVO(newItems);
							}

						} else {

							ProductVO voProductVO = new ProductVO();
							ProductHeaderVO newProductHeaderVO = (ProductHeaderVO) voProductHeaderVO.clone();

							// ��˾
							newProductHeaderVO.setPk_corp(voBillHeaderVO.getPk_corp());
							// �����������֯��
							if("1078".equals(voBillHeaderVO.getPk_corp())){
								newProductHeaderVO.setPk_calbody(voBillHeaderVO.getCstockrdcenterid());//add by hk
							}
							newProductHeaderVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
							// �����
							newProductHeaderVO.setCaccountyear(sSysinfos[7]);
							// �����
							newProductHeaderVO.setCaccountmonth(sSysinfos[8]);
							// ������������Ƿ�Ʒ��
							if (isWaster.booleanValue() == true) {
								newProductHeaderVO.setCbilltypecode(ConstVO.m_sBillFPSSJHS);
							} else {
								newProductHeaderVO.setCbilltypecode(ConstVO.m_sBillWGCPTJ);
							}

							// ��������
							newProductHeaderVO.setDbilldate(voBillHeaderVO.getDbilldate());
							// �Ƶ�����
							newProductHeaderVO.setDbillmakedate(new UFDate(sSysinfos[6]));
							// �Ƶ���
							newProductHeaderVO.setCbillmaker(sSysinfos[5]);
							// ��ע
							newProductHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063045", "UPP-000113")/* @res""�õ�����Դ�ڲ���Ʒ��ⵥ"" */);

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
					/** **************************���ݺŴ���************************************************* */
					boBillcodeRuleBO = getBillcodeRuleBO();
					String[] name = { "�����֯", "����Ա" };
					String[] id = { sSysinfos[1].trim(), sSysinfos[5].trim() };
					BillCodeObjValueVO value = new BillCodeObjValueVO();
					value.setAttributeValue(name, id);
					String[] sbillcodes = null;
					// ����Ƿ�Ʒ
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
	 * ��������:���°��Ʒ�۸�
	 * 
	 * ����:
	 * 
	 * sSysinfos[0] ----- ��˾ sSysinfos[1] ----- ���� sSysinfos[2] ----- ��ʼ����
	 * sSysinfos[3] ----- �������� sSysinfos[4] ----- �������� "I6" ���ϳ��ⵥ sSysinfos[5]
	 * ----- ��ǰ�û� sSysinfos[6] ----- ��ǰ���� sSysinfos[7] ----- ����� sSysinfos[8]
	 * ----- ����� String[] sRDCLs ----- ������ String[] sInvs ----- ���
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * 
	 */
	public String getIAI6Bills(String[] sSysinfos, String[] sRDCLs, String[] sInvs) throws nc.vo.cm.pub.CMBusinessException {

		// ��ʾ��Ϣ
		StringBuffer errStrB = new StringBuffer("\n\n\n\n\n");
		// BOS
		// nc.bs.ia.outter.Outter boIAOutter = null;

		CommonDataBO boCommonData = null;
		// WithOuter boWithOuter = null;
		BillcodeRuleBO boBillcodeRuleBO = null;

		IIAToCA iaToCa = (IIAToCA) NCLocator.getInstance().lookup(IIAToCA.class.getName());

		// ת����Ĳ���
		StuffVO[] vosStuffVO = null;

		try {
			// ��ű���
			Vector vtItems = new Vector();
			// ����
			Vector vtHeads = new Vector();
			String sCorpID = sSysinfos[0];
			String sCalbodyID = sSysinfos[1];
			String sBillTypecode = sSysinfos[4];

			boCommonData = getCommonDataBO();
			
			/**ת��Ϊ�ӿڵĲ���*/
			String[] sInterfaceData = new String[6];
			sInterfaceData[0] = sSysinfos[0];
			sInterfaceData[1] = sSysinfos[1];
			sInterfaceData[2] = sSysinfos[2];
			sInterfaceData[3] = sSysinfos[3];
			sInterfaceData[4] = sSysinfos[7];
			sInterfaceData[5] = sSysinfos[8];
		
			// ȡ��
//			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCPBills(sSysinfos, sRDCLs, sInvs);
			/**�Ƿ���������浥*/
			String sSFBHYCJCD=(new CommonDataBO()).getParaValue(sCorpID, sCalbodyID, ConstVO.m_iPara_SFBHYCJCD);
			boolean bSFBHYCJCD="Y".equalsIgnoreCase(sSFBHYCJCD)?true:false;
//			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCCBills(sSysinfos, sRDCLs, sInvs,bSFBHYCJCD,false);
			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCCBills(sInterfaceData, sRDCLs, sInvs,bSFBHYCJCD,false);
			boolean isByDate = false;

			if (bvos != null && bvos.length > 0) {

				// ������д���Ļ���ID������ID
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
				// ȡ�����ж���
				sSQL.setLength(0);
				sSQL.append(" select mo.pk_moid, mo.wlbmid, mo.pk_produce, mo.scddh ");
				sSQL.append(" from  mm_mo mo inner join bd_produce b on mo.pk_produce=b.pk_produce");
				sSQL.append(" where b.iscostbyorder='Y' and mo.dr = 0");
				sSQL.append(" and mo.pk_corp = '" + sSysinfos[0] + "'");
				sSQL.append(" and mo.gcbm = '" + sSysinfos[1] + "'");

				String[][] sAllOrderPKs = boCommonData.queryData(sSQL.toString());

				// ������еĳɱ�����
				sSQL.setLength(0);
				sSQL.append("select a.pk_ctcenter,b.pk_wk,b.pk_deptdoc  ");
				sSQL.append(" from cm_ctcenter a, cm_ctcenter_b b  ");
				sSQL.append(" where a.pk_ctcenter=b.pk_ctcenter ");
				sSQL.append(" and	a.pk_corp = '" + sCorpID + "'  ");
				sSQL.append(" and 	a.pk_calbody = '" + sCalbodyID + "'  ");
				String[][] sAllCtcenterPKs = boCommonData.queryData(sSQL.toString());
				HashMap hmapStuffVO = new HashMap();
				// //����
				// int[0] ----- �����ľ���
				// int[1] ----- ���۵ľ���
				// int[2] ----- ���ľ���
				Integer[] iDataPrecision = boCommonData.getDataPrecision(sSysinfos[0]);

				// ѭ������
				for (int i = 0; i < bvos.length; i++) {
					// ��������VO
					nc.vo.ia.bill.BillHeaderVO voBillHeaderVO = (nc.vo.ia.bill.BillHeaderVO) bvos[i].getParentVO();
					nc.vo.ia.bill.BillItemVO[] vosBillItemVO = (nc.vo.ia.bill.BillItemVO[]) bvos[i].getChildrenVO();

					for (int j = 0; j < vosBillItemVO.length; j++) {
						StuffVO voStuffVO = new StuffVO();
						StuffItemVO voStuffItemVO = new StuffItemVO();

						/** ****************�ӱ���******************** */

						// ��Դģ��
						voStuffItemVO.setCsourcemodulename(ConstVO.m_sModuleIA);
						// ��˾
						voStuffItemVO.setPk_corp(voBillHeaderVO.getPk_corp());
						// ����
						voStuffItemVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
						// voStuffItemVO.setvsourcebillcode(voBillHeaderVO.getVbillcode());

						// ����IDa.pk_invbasdoc , b.pk_produce , a.pk_invmandoc ,
						// b.jhj , c.invname , c.invcode,d.pk_invcl
						String sPlanprice = null; // �ƻ���
						String[] sInvBasProdID = getInvBasProdID(sAllInvBasProdPKs, vosBillItemVO[j].getCinventoryid());
						if (sInvBasProdID != null) {
							voStuffItemVO.setPk_invbasdoc(sInvBasProdID[0]); // ԭ����
							voStuffItemVO.setPk_produce(sInvBasProdID[1]);
							// ԭ����
							sPlanprice = sInvBasProdID[3];

						} else {
							System.out.println(voBillHeaderVO.getVbillcode() + "û�еõ��ӱ��Ʒ��Ϣ\n");
							// ��ò�ƷID
							sSQL.setLength(0);
							sSQL.append("select a.pk_invbasdoc,b.pk_produce  ");
							sSQL.append("from bd_invmandoc a left join bd_produce b on a.pk_invbasdoc=b.pk_invbasdoc  ");
							sSQL.append("where a.pk_invmandoc='" + vosBillItemVO[j].getCinventoryid() + "'  ");
							sSQL.append("and b.pk_corp='" + sCorpID + "'  ");
							sSQL.append("and b.pk_calbody='" + sCalbodyID + "'  ");

							String[][] sinvPKs = boCommonData.queryData(sSQL.toString());
							if (sinvPKs != null && sinvPKs[0][0] != null && sinvPKs[0][0].trim().length() > 0) {
								voStuffItemVO.setPk_invbasdoc(sinvPKs[0][0]); // ԭ����
								voStuffItemVO.setPk_produce(sinvPKs[0][1]);
							} else {
								// System.out.println(voBillHeaderVO.getVbillcode()+"û�еõ��ӱ��Ʒ��Ϣ\n");
								continue;
							}

						}
						UFDouble nnum = vosBillItemVO[j].getNnumber();
						voStuffItemVO.setNnumber(vosBillItemVO[j].getNnumber()); // ����
						// �ƻ�
						UFDouble nPlanmoney = null;
						if (sPlanprice != null) {
							UFDouble nplanprice = new UFDouble(sPlanprice);
							voStuffItemVO.setNplanprice(nplanprice); // �ƻ���

							if (nnum != null) {
								nPlanmoney = nnum.multiply(nplanprice).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP);
							}
							voStuffItemVO.setNplanmoney(nPlanmoney); // �ƻ����
						}

						// �ɱ��ƶ�
						String sPvalue = boCommonData.getParaValue(voStuffItemVO.getPk_corp(), voStuffItemVO.getPk_calbody(), ConstVO.m_iPara_CBZD);
						int iParaValue;
						if (sPvalue == null || sPvalue.trim().length() == 0) {
							throw new CMBusinessException("û�еõ��ɱ��ƶȲ���", new BusinessException("û�еõ��ɱ��ƶȲ���"));
						} else {
							iParaValue = Integer.parseInt(sPvalue);
						}
						if (iParaValue == ConstVO.m_iPValue_CBZD_STANDARD) {
							voStuffItemVO.setNprice(voStuffItemVO.getNplanprice());// �۸�
							voStuffItemVO.setNcost(voStuffItemVO.getNplanmoney()); // ���
							voStuffItemVO.setNdiff(new UFDouble(0));
						} else {
							// ʵ��

							// voStuffItemVO.setNcost(vosBillItemVO[j].getNmoney());
							// //���
							// //������� ���+���ʶ�
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
								// errStrB.append("�۸�ͽ��ͬʱΪ�ջ���Ϊ��"+"\n");
								continue;
							}
							if (vosBillItemVO[j].getNnumber() != null && nCost != null) {
								voStuffItemVO.setNprice((vosBillItemVO[j].getNmoney()).div(vosBillItemVO[j].getNnumber()).setScale(-iDataPrecision[1].intValue(), UFDouble.ROUND_HALF_UP));
							}
							// ����
							if (nCost != null && nPlanmoney != null) {
								voStuffItemVO.setNdiff(nCost.sub(nPlanmoney));
							}
						}

						/** ****************������******************** */
						StuffHeaderVO voStuffHeaderVO = new StuffHeaderVO();
						// 1�ȸ��ݹ������ĺͲ��Ų�ѯ�ɱ�����
						String pk_ctcenter = getCTCenterForM2(sAllCtcenterPKs, voBillHeaderVO.getCdeptid(), vosBillItemVO[j].getCwp());
						if (pk_ctcenter != null && pk_ctcenter.length() > 0) {
							voStuffHeaderVO.setPk_ctcenter(pk_ctcenter); // �ɱ�����
						} else {
							// û�л�ö�Ӧ�ĳɱ�����
							continue;
						}
						// 2��ƷID
						String[] sProductInvIDs = null;
						if (vosBillItemVO[j].getVbomcode() != null) {
							sProductInvIDs = getInvBasProdID(sAllInvBasProdPKs, vosBillItemVO[j].getVbomcode());
							if (sProductInvIDs != null && sProductInvIDs.length > 0) {
								voStuffHeaderVO.setPk_invbasdoc(sProductInvIDs[0]); // ��Ʒ
								voStuffHeaderVO.setPk_produce(sProductInvIDs[1]); // ��Ʒ

							} else {
								System.out.println(voBillHeaderVO.getVbillcode() + "û�еõ������Ʒ��Ϣ\n");
								continue;
							}
						}

						// 3�������������Ż������
						String spk_order = null;
						if (vosBillItemVO[j].getVproducebatch() != null) {
							String strscddh = vosBillItemVO[j].getVproducebatch();
							for (int m = 0; m < sAllOrderPKs.length; m++) {
								if (sAllOrderPKs[m][3].equals(strscddh)) {
									spk_order = sAllOrderPKs[m][0];
								}
							}
							if (spk_order != null && spk_order.length() != 0) {
								voStuffHeaderVO.setPk_order(spk_order); // �������������ţ�
							}

						}
						// 4�ɱ����⴦��
						if ((spk_order == null || spk_order.length() == 0) && sProductInvIDs != null && sProductInvIDs.length > 0) {
							voStuffHeaderVO.setPk_order(sProductInvIDs[1]);
						}
						/** ************����hashcode�ɱ����ģ���������Ʒ**************** */
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
						// �Ƿ������ڻ���
						// boolean isByDate = false;
						voStuffHeaderVO.setDbilldate(voBillHeaderVO.getDbilldate()); // ��������
						if (isByDate == true) {
							if (voStuffHeaderVO.getDbilldate() != null) {
								shashcode += voStuffHeaderVO.getDbilldate().toString();
							} else {
								shashcode += "0000000000";
							}
						}
						boolean isNewHeader; // �Ƿ��µ�Header
						if (hmapStuffVO.get(shashcode) != null) {
							voStuffVO = (StuffVO) (hmapStuffVO.get(shashcode));
							isNewHeader = false;
						} else {
							isNewHeader = true;
						}
						if (isNewHeader == true) {
							if (isByDate == true) {
								voStuffHeaderVO.setDbillmakedate(new UFDate(sSysinfos[6])); // �Ƶ�����
							} else {
								voStuffHeaderVO.setDbilldate(new UFDate(sSysinfos[3]));
								voStuffHeaderVO.setDbillmakedate(new UFDate(sSysinfos[6])); // �Ƶ�����
							}
							// ��˾
							voStuffHeaderVO.setPk_corp(voBillHeaderVO.getPk_corp());
							voStuffHeaderVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
							// �����������֯��
							voStuffHeaderVO.setCbillmaker(sSysinfos[5]); // �Ƶ���

							voStuffHeaderVO.setCbilltypecode(ConstVO.m_sBillCLCBGJ); // ��������
							// �����
							voStuffHeaderVO.setCaccountyear(sSysinfos[7]);
							// �����
							voStuffHeaderVO.setCaccountmonth(sSysinfos[8]);

							// voStuffHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030",
							// "UPP-000009")/* @res""�õ�����Դ�ڴ��������ϳ��ⵥ"" */);
							// ������µ�VO
							voStuffVO.setParentVO(voStuffHeaderVO);
							voStuffVO.setChildrenVO(new StuffItemVO[] { voStuffItemVO });
							hmapStuffVO.put(shashcode, voStuffVO);
						} else { // ����Ǿɵ�VO��Items���ϸ÷�¼
							StuffItemVO[] vosOldStuffItemVO = (StuffItemVO[]) (voStuffVO.getChildrenVO());
							int ivosOldStuffItemVOlength = vosOldStuffItemVO.length;

							// �ϲ�
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

				// ��hashmapת��Ϊ����
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

							if (sivos[i].getNcost() == null) // ʵ�ʽ��Ϊ��û��Ҫ����
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

					// ��ִ��sql
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
	 * ��������:ȡ�ò��ϳ���ɱ�
	 * 
	 * ����: sSysinfos[0] ----- ��˾ sSysinfos[1] ----- ���� sSysinfos[2] ----- ��ʼ����
	 * sSysinfos[3] ----- �������� sSysinfos[4] ----- �������� "I6" ���ϳ��ⵥ sSysinfos[5]
	 * ----- ��ǰ�û� sSysinfos[6] ----- ��ǰ���� sSysinfos[7] ----- ����� sSysinfos[8]
	 * ----- ����� String[] sRDCLs ----- ������ String[] sInvs ----- ���
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * 
	 */
	public String getIAI6BillsForM2(String[] sSysinfos, String[] sRDCLs) throws nc.vo.cm.pub.CMBusinessException {
		System.out.println("getIAI6BillsForM2 begin");
		// ��ʾ��Ϣ
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

			// ȡ��
			long t1 = System.currentTimeMillis();
			
			/**����������ת��Ϊ�ӿ�����
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
			/**�Ƿ���������浥*/
			String sSFBHYCJCD=(new CommonDataBO()).getParaValue(sCorpID, sCalbodyID, ConstVO.m_iPara_SFBHYCJCD);
			boolean bSFBHYCJCD="Y".equalsIgnoreCase(sSFBHYCJCD)?true:false;
			nc.vo.ia.bill.BillVO[] bvos = iaToCa.getCCCBills(sInterfaceData, sRDCLs, null,bSFBHYCJCD,false);
			CMLogger.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$boIAOutter.getCCCBills:" + getTime(t1, System.currentTimeMillis()));
			
			/**��ӡ*/
		/*	for (int i = 0; i < bvos.length; i++) {
				// ��������VO
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
				// ������гɱ��������ID������ID--1.2.δ��˵�ԭ���ϲ�ȡ
				StringBuffer sSQL = new StringBuffer(300);
				sSQL.append("select  a.pk_invbasdoc ,  b.pk_produce ,  a.pk_invmandoc");
				sSQL.append(" from bd_invmandoc a,bd_produce b  ");
				sSQL.append(" where a.pk_invmandoc = b.pk_invmandoc  ");
				sSQL.append(" and b.sfcbdx='Y' ");
				sSQL.append(" and b.pk_corp = '" + sCorpID + "' ");
				sSQL.append(" and b.pk_calbody = '" + sCalbodyID + "' ");
				// ���гɱ��������ID������ID
				String[][] sAllProductPKs = boCommonData.queryData(sSQL.toString());
				//
				HashSet vProducts = new HashSet();
				if (sAllProductPKs.length != 0) {
					for (int i = 0; i < sAllProductPKs.length; i++) {
						vProducts.add(sAllProductPKs[i][1]);
					}
				} // if (sResult.length != 0)

				// ������д���Ļ���ID������ID
				sSQL.setLength(0);
				sSQL.append("select  a.pk_invbasdoc ,  b.pk_produce ,  a.pk_invmandoc ,  b.jhj  ,c.pk_invcl");
				sSQL.append(" from bd_invmandoc a,bd_produce b ,bd_invbasdoc c ");
				sSQL.append(" where  a.pk_invbasdoc=c.pk_invbasdoc ");
				sSQL.append(" and a.pk_invmandoc = b.pk_invmandoc ");
				sSQL.append(" and b.pk_corp = '" + sCorpID + "' ");
				sSQL.append(" and b.pk_calbody = '" + sCalbodyID + "' ");

				String[][] sAllInvBasProdPKs = boCommonData.queryData(sSQL.toString());
				// ȡ�����ж���
				sSQL.setLength(0);
				sSQL.append(" select mo.pk_moid,  mo.scddh ");
				sSQL.append(" from  mm_mo mo inner join bd_produce b on mo.pk_produce=b.pk_produce");
				sSQL.append(" where b.iscostbyorder='Y' and mo.dr = 0");
				sSQL.append(" and mo.pk_corp = '" + sCorpID + "'");
				// sSQL.append(" and mo.zt!='D' ");
				sSQL.append(" and mo.gcbm = '" + sCalbodyID + "'");
				String[][] sAllOrderPKs = boCommonData.queryData(sSQL.toString());

				// ������еĳɱ�Ҫ��
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

				// ������еĳɱ�����
				sSQL.setLength(0);
				sSQL.append("select a.pk_ctcenter,b.pk_wk,b.pk_deptdoc  ");
				sSQL.append(" from cm_ctcenter a, cm_ctcenter_b b  ");
				sSQL.append(" where a.pk_ctcenter=b.pk_ctcenter ");
				sSQL.append(" and	a.pk_corp = '" + sCorpID + "'  ");
				sSQL.append(" and 	a.pk_calbody = '" + sCalbodyID + "'  ");
				String[][] sAllCtcenterPKs = boCommonData.queryData(sSQL.toString());

				// //����
				// int[0] ----- �����ľ���
				// int[1] ----- ���۵ľ���
				// int[2] ----- ���ľ���
				Integer[] iDataPrecision = boCommonData.getDataPrecision(sCorpID);

				CMLogger.debug("$$$$$$$$$$$$$$$$$$$$$$$$������л�������:" + getTime(t1, System.currentTimeMillis()));

				HashMap hmapStuffVO = new HashMap();
				// ѭ������
				for (int i = 0; i < bvos.length; i++) {
					// ��������VO
					nc.vo.ia.bill.BillHeaderVO voBillHeaderVO = (nc.vo.ia.bill.BillHeaderVO) bvos[i].getParentVO();
					nc.vo.ia.bill.BillItemVO[] vosBillItemVO = (nc.vo.ia.bill.BillItemVO[]) bvos[i].getChildrenVO();

					for (int j = 0; j < vosBillItemVO.length; j++) {
						
						if(vosBillItemVO[j]==null)
							continue;
						/** 1.��� */
						/** 1.1.�����ж����ŵ�û�гɱ�����ȡ */
						if ( vosBillItemVO[j].getVbomcode() == null && vosBillItemVO[j].getVproducebatch() != null) {
							continue;
						}
						/** 1.2.δ��˵�ԭ���ϲ�ȡ** */
						// ���ݲ��ϵĹ���ID�õ�����IDa.pk_invbasdoc , b.pk_produce ,
						// a.pk_invmandoc , b.jhj ,d.pk_invcl
						String[] sStuffID = getInvBasProdID(sAllInvBasProdPKs, vosBillItemVO[j].getCinventoryid());
						if (vosBillItemVO[j].getCauditorid() == null && vProducts.contains(sStuffID[1]) == false) {
							continue;
						}
						/** 1.3.�����ͽ��ͬʱΪ�ջ���Ϊ��* */
						/*UFDouble nnum = vosBillItemVO[j].getNnumber();
						UFDouble nCost = vosBillItemVO[j].getNmoney();
						if ((nnum == null || nnum.toDouble().doubleValue() == 0.00) && (nCost == null || nCost.toDouble().doubleValue() == 0.00)) {
							continue;
						}*/

						// **2.���ж��Ƿ���ڸñ�ͷ*/
						/** ****************************2.1.�ȸ��ݹ������ĺͲ��Ų�ѯ�ɱ�����******************************* */
						String pk_ctcenter = getCTCenterForM2(sAllCtcenterPKs, voBillHeaderVO.getCdeptid(), vosBillItemVO[j].getCwp());
						if (pk_ctcenter == null || pk_ctcenter.length() == 0) {
							continue;
						}
						/** ***************************2.2.���ݶ����Ż������********************************************** */
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
						/** ***************************2.3.���ݳɱ�����Ĺ���ID��û���ID������ID********************************************** */
						String[] sProductInvIDs = getInvBasProdID(sAllProductPKs, vosBillItemVO[j].getVbomcode());
						String sProductBasID = null; // ��Ʒ����ID
						String sProductProID = null; // ����ID
						if (sProductInvIDs != null && sProductInvIDs.length > 0) {
							sProductBasID = sProductInvIDs[0]; // ��Ʒ����ID
							sProductProID = sProductInvIDs[1]; // ��Ʒ ����ID

						}
						/** ***************************2.4.�ɱ����ģ���������Ʒ����hashcode**************** */
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
						/** ***************************2.5.����hashcode�жϱ�ͷ�Ƿ����**************** */
						if (hmapStuffVO.get(shashcode) != null) { // �������
							voStuffVO = (StuffVO) (hmapStuffVO.get(shashcode));
							// ����Ǿɵ�VO��Items���ϸ÷�¼
							StuffItemVO[] vosOldStuffItemVO = (StuffItemVO[]) (voStuffVO.getChildrenVO());
							int ivosOldStuffItemVOlength = vosOldStuffItemVO.length;
							boolean isUnion = false; // ����ӱ��Ѿ����ڸò���,�������ͽ���ۼ�
							
							for (int k = 0; k < ivosOldStuffItemVOlength; k++) {
								//20070628 jack add subface check
								String[] sFac = getIAI6BillsSubfac(sAllSubPKs, pk_ctcenter, voBillHeaderVO.getCdispatchid(), sStuffID);
								if(sFac == null || sFac.length  == 0 || sFac[0] == null) continue;
								
								if (vosOldStuffItemVO[k].getPk_produce().equals(sStuffID[1]) /**jack added 20070628 */ && vosOldStuffItemVO[k].getPk_subfac().equals(sFac[0]) ) {
									UFDouble dOldNum = vosOldStuffItemVO[k].getNnumber();
									UFDouble dOldCost = vosOldStuffItemVO[k].getNcost();
									UFDouble dNewNum = (vosBillItemVO[j].getNnumber()==null?new UFDouble(0):vosBillItemVO[j].getNnumber());
									UFDouble dNewCost = null;
									// //���������+���ʶ�
									if (vosBillItemVO[j].getNdrawsummny() == null) {
										dNewCost = vosBillItemVO[j].getNmoney();
									} else if (vosBillItemVO[j].getNmoney() != null) {
										dNewCost = vosBillItemVO[j].getNmoney().add(vosBillItemVO[j].getNdrawsummny());
									}
									// �ϲ������ͽ��
									if (dOldNum != null && dNewNum != null) {
										vosOldStuffItemVO[k].setNnumber(dOldNum.add(dNewNum));
									}

									// ����ƻ����۲�Ϊ�ո��¼ƻ����
									if (vosOldStuffItemVO[k].getNplanprice() != null && vosOldStuffItemVO[k].getNnumber() != null) {
										vosOldStuffItemVO[k].setNplanmoney((vosOldStuffItemVO[k].getNplanprice().multiply(vosOldStuffItemVO[k].getNnumber())).setScale(-iDataPrecision[2].intValue(),
												UFDouble.ROUND_HALF_UP));
									}

									// �ϲ����
									if (dOldCost != null && dNewCost != null) {
										vosOldStuffItemVO[k].setNcost(dOldCost.add(dNewCost));
									}
									// ����ƻ�����ʵ�ʽ�Ϊ��
									if (vosOldStuffItemVO[k].getNcost() != null && vosOldStuffItemVO[k].getNplanmoney() != null) {
										vosOldStuffItemVO[k].setNdiff(vosOldStuffItemVO[k].getNcost().sub(vosOldStuffItemVO[k].getNplanmoney()));
									}
									// ���¼۸�
									if (vosOldStuffItemVO[k].getNnumber() != null && vosOldStuffItemVO[k].getNcost() != null) {
										vosOldStuffItemVO[k].setNprice(vosOldStuffItemVO[k].getNcost().div(vosOldStuffItemVO[k].getNnumber()).setScale(-iDataPrecision[1].intValue(),
												UFDouble.ROUND_HALF_UP));
									}
									// vosOldStuffItemVO[k].setciaitemid(null);
									isUnion = true;
									break;
								}

							}
							// ����ӱ����ڸò����������ӱ�VO
							if (isUnion == false) {
								// �����ӱ�VO
								/** *************���ɱ���************************* */
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

						} else { // ��ͷ������
							/** *************���ɱ�ͷ************************* */
							StuffHeaderVO voStuffHeaderVO = new StuffHeaderVO();
							// ҵ������dbilldate
							voStuffHeaderVO.setDbilldate(dDbilldate);
							// ��˾pk_corp
							voStuffHeaderVO.setPk_corp(voBillHeaderVO.getPk_corp());
							// �����������֯��pk_calbody
							voStuffHeaderVO.setPk_calbody(voBillHeaderVO.getCrdcenterid());
							// ��������cbilltypecode
							voStuffHeaderVO.setCbilltypecode(ConstVO.m_sBillCLCBGJ);
							// �����caccountyear
							voStuffHeaderVO.setCaccountyear(sSysinfos[7]);
							// �����caccountmonth
							voStuffHeaderVO.setCaccountmonth(sSysinfos[8]);
							// �ɱ�����pk_ctcenter
							voStuffHeaderVO.setPk_ctcenter(pk_ctcenter);
							// ����pk_order
							if (spk_order != null && spk_order.length() > 0) {
								voStuffHeaderVO.setPk_order(spk_order); // �������������ţ�
							}
							// �ɱ����⴦���������IDΪ�ձ����Ʒ����ID
							else if ((spk_order == null || spk_order.length() == 0) && sProductInvIDs != null && sProductInvIDs.length > 0) {
								voStuffHeaderVO.setPk_order(sProductInvIDs[1]);
							}
							// ��Ʒ����IDpk_invbasdoc
							voStuffHeaderVO.setPk_invbasdoc(sProductBasID);
							// ��Ʒ����IDpk_produce
							voStuffHeaderVO.setPk_produce(sProductProID);
							// ��עvnote
							voStuffHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000009")/* @res""�õ�����Դ�ڴ��������ϳ��ⵥ"" */);
							// �Ƶ��� cbillmaker
							voStuffHeaderVO.setCbillmaker(sSysinfos[5]);
							// �Ƶ�����dbillmakedate
							voStuffHeaderVO.setDbillmakedate(dDbillmakedate);
							/** *************���ɱ���************************* */
							StuffItemVO voStuffItemVO = getIAI6BillsStuffItemVO(sAllSubPKs, pk_ctcenter, voBillHeaderVO, vosBillItemVO[j], sStuffID, iDataPrecision);
							if (voStuffItemVO == null) {
								continue;
							}
							// ������µ�VO
							voStuffVO = new StuffVO();
							voStuffVO.setParentVO(voStuffHeaderVO);
							voStuffVO.setChildrenVO(new StuffItemVO[] { voStuffItemVO });
							hmapStuffVO.put(shashcode, voStuffVO);

						}

						/** ************************************************************************* */

					} // for (int j = 0; j < vosBillItemVO.length; j++)

				} // for (int i = 0; i < bvos.length; i++)

				// ��hashmapת��Ϊ����
				StuffVO[] vosStuffVO = null;
				CMLogger.debug("$$$$$$$$$$$$$$$$$$$$$$$$���ݲ���ǰ������ʱ:" + getTime(t1, System.currentTimeMillis()));
				if (hmapStuffVO.size() > 0) {
					// ���ݺŴ���
					boBillcodeRuleBO = new BillcodeRuleBO();
					String[] name = { "�����֯", "����Ա" };
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
				CMLogger.debug("$$$$$$$$$$$$$$$$$$$$$$$$getIAI6BillsForM2ȡ������ʱ:" + getTime(t1, System.currentTimeMillis()));
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
	 * ��������:�������������ӱ�VO
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * 
	 */
	private StuffItemVO getIAI6BillsStuffItemVO(String[][] sAllSubPKs, String pk_ctcenter, nc.vo.ia.bill.BillHeaderVO voBillHeaderVO, nc.vo.ia.bill.BillItemVO vosBillItemVO, String[] sStuffID,
			Integer[] iDataPrecision) throws Exception {

		// �����ӱ�VO
		/** ************���ɱ���********** */
		StuffItemVO voStuffItemVO = new StuffItemVO();
		// ��Ӧ�ɱ�Ҫ��
		String[] sFac = null;
		sFac = getIAI6BillsSubfac(sAllSubPKs, pk_ctcenter, voBillHeaderVO.getCdispatchid(), sStuffID);

		if (sFac != null) {
			voStuffItemVO.setPk_subfac(sFac[0]); // �ɱ�Ҫ��//pk_subfac
			voStuffItemVO.setPk_subfacset(sFac[1]); // �ɱ�Ҫ��//pk_subfacset
		} else {
			return null;

		}
		String sPlanprice = null;
		// ����ID
		if (sStuffID != null) {
			// ԭ���ϻ���ID
			voStuffItemVO.setPk_invbasdoc(sStuffID[0]); // pk_invbasdoc
			// ԭ��������ID
			voStuffItemVO.setPk_produce(sStuffID[1]); // pk_produce
			sPlanprice = sStuffID[3]; // �ƻ���

		}
		// ��Դģ��
		voStuffItemVO.setCsourcemodulename(ConstVO.m_sModuleIA); // csourcemodulename
		// ��˾
		voStuffItemVO.setPk_corp(voBillHeaderVO.getPk_corp()); // pk_calbody
		// ����
		voStuffItemVO.setPk_calbody(voBillHeaderVO.getCrdcenterid()); // pk_corp
		// ����
		voStuffItemVO.setNnumber(vosBillItemVO.getNnumber()==null?new UFDouble(0):vosBillItemVO.getNnumber()); // nnumber
		// ������� ���+���ʶ�
		if (vosBillItemVO.getNdrawsummny() == null) {
			voStuffItemVO.setNcost(vosBillItemVO.getNmoney());
		} else if (vosBillItemVO.getNmoney() != null) {
			voStuffItemVO.setNcost(vosBillItemVO.getNmoney().add(vosBillItemVO.getNdrawsummny()));
		}

		// �۸�
		if (vosBillItemVO.getNnumber() != null && vosBillItemVO.getNmoney() != null)
			voStuffItemVO.setNprice((vosBillItemVO.getNmoney()).div(vosBillItemVO.getNnumber()).setScale(-iDataPrecision[1].intValue(), UFDouble.ROUND_HALF_UP));
		// nprice
		// �ƻ��۸񡢼ƻ����
		if (sPlanprice != null) {
			UFDouble nplanprice = new UFDouble(sPlanprice);
			// �ƻ���
			voStuffItemVO.setNplanprice(nplanprice);
			UFDouble nPlanmoney = null;
			if (vosBillItemVO.getNnumber() != null) {
				nPlanmoney = vosBillItemVO.getNnumber().multiply(nplanprice).setScale(-iDataPrecision[2].intValue(), UFDouble.ROUND_HALF_UP);
				// nplanmoney
			}
			// �ƻ����
			voStuffItemVO.setNplanmoney(nPlanmoney);

			// ����
			if (vosBillItemVO.getNmoney() != null && nPlanmoney != null) {
				voStuffItemVO.setNdiff(vosBillItemVO.getNmoney().sub(nPlanmoney)); // ndiff
			}

		}

		return voStuffItemVO;
	}

	/**
	 * ��������:����������Ӧ�ɱ�Ҫ��
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * 
	 */
	private String[] getIAI6BillsSubfac(String[][] sAllSubPKs, String pk_ctcenter, String Cdispatchid, String[] sInvBasProdID) throws Exception {
		// �Ȱ�������pK���ҳɱ�Ҫ��
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
		// Ȼ���մ���������
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

	// ���ݺ�BO
	private PullStuffErrVO getPullStuffErrVO(String[] sSysinfos, nc.vo.ia.bill.BillHeaderVO voBillHeaderVO, nc.vo.ia.bill.BillItemVO voBillItemVO, String sErrNote, String pk_invbasdoc)
			throws Exception {
		PullStuffErrVO voPullStuffErrVO = new PullStuffErrVO();
		// vbillcode
		voPullStuffErrVO.setVbillcode(voBillHeaderVO.getVbillcode());
		// sSysinfos[0] ----- ��˾
		// sSysinfos[1] ----- ����
		// sSysinfos[8]-------�����
		// sSysinfos[9]-------�����
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
	 * ��������:��óɱ�·��
	 * 
	 * ����: ��˾ �����֯ �û� �������ID ������λID ����
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
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
		dvo.setPk_corp(sCorpID); // ��˾ID
		dvo.setGcbm(sCalbodyID); // ����ID
		dvo.setOperid(sUserID); // ����ԱID
		dvo.setWlbmid(sInvBasID); // ����ID(���Ż�������)

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

				dvo.setJldwid(sMeaID); // ������λID
			}
		}

		dvo.setId("A"); // A:���ϣ�B:����
		dvo.setSl(new UFDouble(1)); // ����
		dvo.setLogdate(new UFDate(sDate)); // ��¼����
		dvo.setYxrq(new UFDate(sDate)); // ��Ч����

		dvo.setSfsh(new UFBoolean("Y")); // �Ƿ������
		dvo.setSffp(new UFBoolean("N")); // �Ƿ��Ƿ�Ʒ
		dvo.setSfplgz(new UFBoolean("N")); // �Ƿ�����������

//		dvos = dsbo.getRoute(dvo);
		dvos=pdToCM.getRoutesCM(dvo);
		return dvos;
	}

	/**
	 * ��������:��ô˳ɱ���Ŀ��iLevel�����ӳɱ���Ŀ
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
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
						// ȡ���¼�
						vReturnData = getSubfac(sfbo, sfvos[i].getPrimaryKey(), iLevel - 1, vReturnData);
					} else if (iLevel == -1) {
						// ȡ���¼�
						vReturnData = getSubfac(sfbo, sfvos[i].getPrimaryKey(), -1, vReturnData);
					}
				}
			}
		}

		return vReturnData;
	}

	/**
	 * ��������:
	 * 
	 * ����:
	 * 
	 * ����ֵ:
	 * 
	 * �쳣:
	 * 
	 * @return nc.bs.ia.pub.CommonData
	 */
	private SubfacBO getSubfacBO() throws Exception {

		return new SubfacBO();
	}

	/**
	 * SessionBean�ӿ��еķ�����
	 * <p>
	 * ���������EJB Container���ã���Զ��Ҫ����ĳ����е�������� ����
	 * <p>
	 * �������ڣ�(2001-2-15 16:34:02)
	 * 
	 * @exception nc.vo.cm.pub.CMBusinessException
	 *                �쳣˵����
	 */
	private String getTime(long l1, long l2) {
		long t2 = l2 - l1;
		int iHour = (int) (t2 / 36000000);
		int iMinutes = (int) ((t2 - iHour * 36000000) / 60000);
		int iSecond = (int) ((t2 - iHour * 36000000 - iMinutes * 60000) / 1000);
		int iOther = (int) (t2 - iHour * 36000000 - iMinutes * 60000 - iSecond * 1000);
		String sTime = "��ʱ��" + iHour + "Сʱ" + iMinutes + "����" + iSecond + "��" + iOther + "����";
		return sTime;
	}

	/***************************************************************************
	 * ��������: �жϸĴ���Ƿ��ڴ˹������Ļ��������� ����: String[0] sCorpID : ��˾ String[1] sCalbodyID :
	 * ���� String[2] pk_invbasdoc : �������ID String[3] date : ҵ������ String[4] wkid :
	 * ��������ID String[5] deptid : ����ID
	 * 
	 * boolean isLast : �Ƿ����һ����������
	 * 
	 * ����ֵ: String[][]
	 * 
	 * �쳣:
	 * 
	 * �������ڣ�(2003-4-15 14:14:36)
	 * 
	 * @return boolean
	 **************************************************************************/

	public boolean isInvInRoute(String[] sParamList, boolean isLast) throws nc.vo.cm.pub.CMBusinessException {

		// sParamList ˵����
		/***********************************************************************
		 * String[0] sCorpID : ��˾ String[1] sCalbodyID : ���� String[2]
		 * pk_invbasdoc : �������ID String[3] date : ҵ������ String[4] wkid : ��������ID
		 * String[5] deptid : ����ID
		 **********************************************************************/

		IPdToCm pdToCm = (IPdToCm) NCLocator.getInstance().lookup(IPdToCm.class.getName());
		String[][] sWKs;
		try {
			sWKs = pdToCm.getWkFromRouteCM(sParamList[0], sParamList[1], sParamList[2], sParamList[3], isLast);
		} catch (BusinessException e) {
			CMLogger.error("�õ�����·�߶�Ӧ�������Ĵ���", e);
			throw new CMBusinessException(e);
		}

		if (sWKs != null && sWKs.length > 0) {

			for (int i = 0; i < sWKs.length; i++) {
				if (sParamList[4] != null) { // ����������
					if (sParamList[4].equals(sWKs[0]))
						return true;
				} else { // ������
					if (sParamList[5].equals(sWKs[1]))
						return true;
				}
			}
		}

		return false;

	}

	/**
	 * ��Ʒ����id private String m_pk_produce; //���ĵĶ��� private String m_scddid; //����
	 * private UFDouble m_sl;
	 * 
	 * //ת������ private String m_zcbm; //ת���������� private String m_zcgzzx; //��Ʒ����ID
	 * private String m_pk_invbasdoc; //���Ĳ��� private String m_xhbm; //���Ĺ�������
	 * private String m_xhgzzx; //���Ĳ�Ʒ����ID private String m_pk_toinvbasdoc;
	 * //���Ĳ�Ʒ����ID private String m_pk_toProduce;
	 */
	public String queryBcpForM3(String[] sSysinfos, String pk_corp, String pk_calbody, String startDate, String endDate) throws nc.vo.cm.pub.CMBusinessException {
		// ��ʾ��Ϣ
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

			// �õ�CostStaticVO
			nc.vo.me.me1050.CostStaticVO[] vosCostStaticVO = null;
			IMmToCm mmToCM = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCM.getStatisticgxBcpCM(pk_corp, pk_calbody, startDate, endDate);
			if (vosCostStaticVO == null || vosCostStaticVO.length == 0) {
				return sErr;
			}
			int ivosCostStaticVOlength = vosCostStaticVO.length;
			ArrayList aryListHalfprodVO = new ArrayList(); //
			// //����
			// int[0] ----- �����ľ���
			// int[1] ----- ���۵ľ���
			// int[2] ----- ���ľ���
			Integer[] iDataPrecision = boCommonData.getDataPrecision(sSysinfos[0]);

			// ѭ������
			if (vosCostStaticVO != null) {

				// ������еĳɱ�����
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);

				for (int i = 0; i < ivosCostStaticVOlength; i++) {
					// �������ΪNUll����Ϊ0,����
					if (vosCostStaticVO[i].getSl() == null || vosCostStaticVO[i].getSl().equals(new UFDouble(0))) {
						continue;
					}

					HalfprodVO voHalfprodVO = new HalfprodVO();
					HalfprodHeaderVO voHalfprodHeaderVO = new HalfprodHeaderVO();
					HalfprodItemVO voHalfprodItemVO = new HalfprodItemVO();
					/** ************************************************* */
					// �ȸ���ת���������Ĳ�ѯ�ɱ�����
					String serrCT = "";
					// ת���ɱ�����
					String sOutCtcenterVOpk = null;
					// ת��ɱ�����
					String sInCtcenterVOpk = null;
					// ��ʾ�����Ƿ����
					boolean isTitleAdded = false;
					// ����
					String sErrT = "*****************" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000010")/* @res""���ʱ���"" */
							+ "��[" + vosCostStaticVO[i].getinvcode() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0001155")/* @res""����"" */
							+ "��[" + vosCostStaticVO[i].getinvname() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0002282")/* @res""����"" */
							+ "[" + vosCostStaticVO[i].getSl() + "]***********************\n";
					// ת������
					if (vosCostStaticVO[i].getzcbm() != null) {
						voHalfprodHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
					}
					// ת���ɱ�����
					if (vosCostStaticVO[i].getzcgzzx() != null) {
						sOutCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getzcgzzx(), 0);

						if (sOutCtcenterVOpk != null) {
							voHalfprodHeaderVO.setPk_ctcenter(sOutCtcenterVOpk); // �ɱ�����
						} else {
							if (isTitleAdded == false) {
								sErr += sErrT;
								isTitleAdded = true;
							}
							sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000011", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""ת����������{0}û�ж�Ӧ�Ļ��������ɱ�����"" */
									+ "\n";

							if (vosCostStaticVO[i].getzcbm() != null) {
								// ת������
								voHalfprodHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
								sOutCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getzcbm(), null, 0);
								if (sOutCtcenterVOpk != null) {
									voHalfprodHeaderVO.setPk_ctcenter(sOutCtcenterVOpk);
								} else {
									if (isTitleAdded == false) {
										sErr += sErrT;
										isTitleAdded = true;
									}
									sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000012", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""ת������{0}û�ж�Ӧ�Ļ��������ɱ�����"" */
											+ "\n";
									continue;

								}
							} else {
								if (isTitleAdded == false) {
									sErr += sErrT;
									isTitleAdded = true;
								}
								sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000013")/* @res""ת�����ź�ת����������ͬʱΪ��"" */
										+ "\n";
								continue;
							}
						}

					} else {
						if (vosCostStaticVO[i].getzcbm() != null) {
							// ת������
							voHalfprodHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
							sOutCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getzcbm(), null, 0);
							if (sOutCtcenterVOpk != null) {
								voHalfprodHeaderVO.setPk_ctcenter(sOutCtcenterVOpk);
							} else {
								if (isTitleAdded == false) {
									sErr += sErrT;
									isTitleAdded = true;
								}
								sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000012", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""ת������{0}û�ж�Ӧ�Ļ��������ɱ�����"" */
										+ "\n";
								continue;

							}
						} else {
							if (isTitleAdded == false) {
								sErr += sErrT;
								isTitleAdded = true;
							}
							sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000013")/* @res""ת�����ź�ת����������ͬʱΪ��"" */
									+ "\n";
							continue;
						}

					}

					/** ******************ת��********************************* */
					// �������Ĺ������Ĳ�ѯ�ɱ�����
					// ת�벿��
					if (vosCostStaticVO[i].getxhbm() != null) {
						voHalfprodItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
						voHalfprodItemVO.setpk_difdeptdoc(vosCostStaticVO[i].getxhbm());
					}
					// ת��ɱ�����
					if (vosCostStaticVO[i].getxhgzzx() != null) {
						sInCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getxhgzzx());
						if (sInCtcenterVOpk == null) {
							if (isTitleAdded == false) {
								sErr += sErrT;
								isTitleAdded = true;
							}
							sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000014", null, new String[] { "[" + vosCostStaticVO[i].getsygzzxmc() + "]" })/* @res""ת�빤������{0}û�ж�Ӧ�ĳɱ�����"" */
									+ "\n";
							if (vosCostStaticVO[i].getxhbm() != null) {
								sInCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getxhbm(), null);
								if (sInCtcenterVOpk == null) {
									if (isTitleAdded == false) {
										sErr += sErrT;
										isTitleAdded = true;
									}
									sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""ת�벿��{0}û�ж�Ӧ�ĳɱ�����"" */
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
								sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""ת�벿��{0}û�ж�Ӧ�ĳɱ�����"" */
										+ "\n";
							}
						}
					}
					// �ɱ����Ĳ��գ�����ɱ����ģ����򱣴沿�ţ�����ͬʱΪ�գ���ȡ
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
							sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000016")/* @res""ת�벿�źͳɱ�����ͬʱΪ��"" */
									+ "\n";
							continue;
						}
					}
					/** ****************��ͷ����***************** */
					// ��˾
					voHalfprodHeaderVO.setPk_corp(pk_corp);
					// �����������֯��
					voHalfprodHeaderVO.setPk_calbody(pk_calbody);
					// �����
					voHalfprodHeaderVO.setCaccountyear(sSysinfos[2]);
					// �����
					voHalfprodHeaderVO.setCaccountmonth(sSysinfos[3]);
					// ���ݺ�
					voHalfprodHeaderVO.setCbilltypecode("M3");
					// �Ƶ�����
					voHalfprodHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6].toString()));
					// ��������
					voHalfprodHeaderVO.setDbilldate(new UFDate(endDate));
					// ��ʼ����
					voHalfprodHeaderVO.setdstartDate(new UFDate(startDate));
					// ��������
					voHalfprodHeaderVO.setdendDate(new UFDate(endDate));
					// �Ƶ���
					voHalfprodHeaderVO.setCbillmaker(sSysinfos[5]);
					// ��ע
					voHalfprodHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000017")/* @res""��Դ�ڼ���ϵͳ"" */);

					/** ******************���崦��*************************** */
					// ��˾
					voHalfprodItemVO.setPk_corp(pk_corp);
					// ����
					voHalfprodItemVO.setPk_calbody(pk_calbody);
					// ����
					voHalfprodItemVO.setNnumber(vosCostStaticVO[i].getSl());
					// ��Դģ��
					voHalfprodItemVO.setCsourcemodulename(ConstVO.m_sModuleME); //
					// ������������ID
					voHalfprodItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce());
					// �����������ID
					voHalfprodItemVO.setPk_invbasdoc(vosCostStaticVO[i].getpk_invbasdoc());
					// �ƻ���,�����ת
					String isfxjz = ""; // �����ת��ǡ�Y����ʾ��ת
					// ����Ƽ۷�ʽ
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
					// �鿴�Ƽ۷�ʽ
					if (spricemethod == null) {
						if (isTitleAdded == false) {
							sErr += sErrT;
							isTitleAdded = true;
						}
						sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000018")/* @res""û��ȡ������Ƽ۷�ʽ,����"" */
								+ "\n";
						continue;
					}
					if (spricemethod.equals("5")) {
						// �ڲ�ת�Ƽ�
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

					// �ɱ�Ҫ�ش��������ת��ɱ����ķǿգ����Ҳ��Ƿ����ת
					if (sInCtcenterVOpk != null && isfxjz.equals("N")) {
						String[] sFac = boWithOuter.getSubfacForInv(pk_corp, pk_calbody, sInCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc(), new UFBoolean(false));

						if (sFac == null) {
							sFac = boWithOuter.getSubfacForInv(pk_corp, pk_calbody, sInCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc(), new UFBoolean(true));
							// û�л�óɱ���Ҫ����Ϣ
							if (sFac == null) {
								if (isTitleAdded == false) {
									sErr += sErrT;
									isTitleAdded = true;
								}
								sErr += (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000019")/* @res""û�л�ý��ʵĳɱ�Ҫ��"" */+ "\n");
								continue;

							}

						}

						voHalfprodItemVO.setPk_subfac(sFac[0]); // �ɱ�Ҫ��
						voHalfprodItemVO.setPk_subfacset(sFac[1]); // �ɱ�Ҫ��

					}
					// ����VO ��ͷ���岻��Ϊ��
					if (voHalfprodHeaderVO == null || voHalfprodItemVO == null) {
						continue;
					}
					voHalfprodVO.setParentVO(voHalfprodHeaderVO);
					voHalfprodVO.setChildrenVO(new HalfprodItemVO[] { voHalfprodItemVO });
					aryListHalfprodVO.add(voHalfprodVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// ת��
			if (aryListHalfprodVO.size() != 0) {
				vosHalfprodVO = new HalfprodVO[aryListHalfprodVO.size()];
				vosHalfprodVO = (HalfprodVO[]) aryListHalfprodVO.toArray(vosHalfprodVO);
				if (vosHalfprodVO.length > 0) {
					// ���ϵ��ݺ�
					String[] name = { "�����֯", "����Ա" };
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
						sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000020")/* @res""���ݲ������"" */
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
	 * ���깤�����ȡ�������� ȡ�깤���汨������ <br>
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
	 * @return CostStaticVO[] �������� private String m_gzzxid; ���ϱ���id private
	 *         String m_wlbmid; private String m_pk_produce; �������� private String
	 *         m_scddid; private String m_scddh;
	 * 
	 * ����(�깤����,��������....��ҵ��) private UFDouble m_sl; private UFDouble m_fsl; �����
	 * private String m_gxh; private String m_jqGxh; private String m_jhGxh;
	 * ��ҵ����id ���� private String m_atid; private String m_atname; �������� private
	 * UFDate m_jsrq;
	 * 
	 * sSysinfos[0] ----- ��˾ sSysinfos[1] ----- ���� sSysinfos[2] ----- ��ʼ����
	 * sSysinfos[3] ----- �������� sSysinfos[4] ----- ��������,"I3" ����Ʒ��ⵥ "I6" ���ϳ��ⵥ
	 * sSysinfos[5] ----- ��ǰ�û� sSysinfos[6] ----- ��ǰ����
	 */
	public ProductVO[] queryBfsl(String[] sSysinfos, String pk_corp, String pk_calbody, UFDate startDate, UFDate endDate) throws nc.vo.cm.pub.CMBusinessException {
		ProductVO[] vosProductVO = null;
		try {
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			ArrayList aryListProductVO = new ArrayList(); //
			nc.vo.mm.pub.pub1030.CostStaticVO[] vosCostStaticVO = null;

			// �õ�CostStaticVO
			vosCostStaticVO = mmToCm.queryStatisticBfslCM(pk_corp, pk_calbody, startDate, endDate);
			// ѭ������
			if (vosCostStaticVO != null) {
				ProductVO voProductVO = new ProductVO();
				ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
				ProductItemVO voProductItemVO = new ProductItemVO();
				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// ������еĳɱ�����
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {
					/** ****************��ͷ����***************** */
					// �ȸ��ݹ������Ĳ�ѯ�ɱ�����
					String sCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getGzzxid());

					if (sCtcenterVOpk != null) {
						voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); // �ɱ�����
					} else {
						// û�л�ö�Ӧ�ĳɱ�����
						continue;
					}
					//
					voProductHeaderVO.setPk_corp(pk_corp); // ��˾
					voProductHeaderVO.setPk_calbody(pk_calbody); // �����������֯��
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // �����
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // �����
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6]));// //
					// //��������
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6]));// �Ƶ�����
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // �Ƶ���

					/** ******************���崦��*************************** */
					voProductItemVO.setPk_corp(pk_corp);//
					voProductItemVO.setPk_calbody(pk_calbody);//
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid());// ����ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // ��Ʒ����ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // ��Ʒ����
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // ����
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC);//

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// ת��
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
	 * ���깤�����ȡ�깤���� <br>
	 * 1.�������е���Ҫ��mm_zjbgȡ�� <br>
	 * 2.���˳��������� <br>
	 * 3.�깤��ֹ�������깤����Ľ�������Ϊ׼
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

			// �õ�CostStaticVO
			vosCostStaticVO = mmToCM.getWgslToCostCM(pk_corp, pk_calbody, startDate.toString(), endDate.toString());
			// ѭ������
			if (vosCostStaticVO != null) {

				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// ������еĳɱ�����
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {

					ProductVO voProductVO = new ProductVO();
					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************��ͷ����***************** */
					// �ȸ��ݹ������Ĳ�ѯ�ɱ�����
					String sCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getGzzxid());

					if (sCtcenterVOpk != null) {
						voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); // �ɱ�����
					} else {
						// û�л�ö�Ӧ�ĳɱ�����
						continue;
					}
					//
					voProductHeaderVO.setPk_corp(pk_corp); // ��˾
					voProductHeaderVO.setPk_calbody(pk_calbody); // �����������֯��
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // �����
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // �����
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // //
					// //��������
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // �Ƶ�����
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // �Ƶ���

					/** ******************���崦��*************************** */
					voProductItemVO.setPk_corp(pk_corp); //
					voProductItemVO.setPk_calbody(pk_calbody); //
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid()); // ����ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // ��Ʒ����ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // ��Ʒ����
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // ����
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC); //

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// ת��
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
	 * ���깤�����ȡ�ڲ����� <br>
	 * 1.���������ļƻ��깤���� - sum(�깤���� + ��������) <br>
	 * 2.��������ΪͶ��״̬ zt=B <br>
	 * 3.�깤����Ϊ���������깤 lylx=0 <br>
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

			// �õ�CostStaticVO
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCm.getZzslToCostCM(pk_corp, pk_calbody, startDate.toString(), endDate.toString());
			// ѭ������
			if (vosCostStaticVO != null) {

				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// ������еĳɱ�����
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {

					ProductVO voProductVO = new ProductVO();
					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************��ͷ����***************** */
					// �ȸ��ݹ������Ĳ�ѯ�ɱ�����
					String sCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getGzzxid());

					if (sCtcenterVOpk != null) {
						voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); // �ɱ�����
					} else {
						// û�л�ö�Ӧ�ĳɱ�����
						continue;
					}
					//
					voProductHeaderVO.setPk_corp(pk_corp); // ��˾
					voProductHeaderVO.setPk_calbody(pk_calbody); // �����������֯��
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // �����
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // �����
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // //
					// //��������
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // �Ƶ�����
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // �Ƶ���

					/** ******************���崦��*************************** */
					voProductItemVO.setPk_corp(pk_corp); //
					voProductItemVO.setPk_calbody(pk_calbody); //
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid()); // ����ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // ��Ʒ����ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // ��Ʒ����
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // ����
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC); //

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// ת��
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
	 * ��Ʒ����id private String m_pk_produce; ���ĵĶ��� private String m_scddid; ����
	 * private UFDouble m_sl; ת������ private String m_zcbm; ת���������� private String
	 * m_zcgzzx; ��Ʒ����ID private String m_pk_invbasdoc; ���Ĳ��� private String
	 * m_xhbm; ���Ĺ������� private String m_xhgzzx; ���Ĳ�Ʒ����ID private String
	 * m_pk_toinvbasdoc; ���Ĳ�Ʒ����ID private String m_pk_toProduce;
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

			// �õ�CostStaticVO
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCm.getStatisticgxWgfzfwCM(pk_corp, pk_calbody, startDate, endDate);
			if (vosCostStaticVO == null || vosCostStaticVO.length == 0) {
				return sErr;
			}
			int ivosCostStaticVOlength = vosCostStaticVO.length;
			boWithOuter = new WithOuterBO();
			boBillcodeRuleBO = getBillcodeRuleBO();
			// //����
			// int[0] ----- �����ľ���
			// int[1] ----- ���۵ľ���
			// int[2] ----- ���ľ���
			Integer[] iDataPrecision = boCommonData.getDataPrecision(sSysinfos[0]);
			// ѭ������

			if (vosCostStaticVO != null) {

				// ������еĳɱ�����
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {
					// �������ΪNUll����Ϊ0,����
					if (vosCostStaticVO[i].getSl() == null || vosCostStaticVO[i].getSl().equals(new UFDouble(0))) {
						continue;
					}
					ServuseVO voServuseVO = new ServuseVO();
					ServuseHeaderVO voServuseHeaderVO = new ServuseHeaderVO();
					ServuseItemVO voServuseItemVO = new ServuseItemVO();
					/** ****************��ͷ����***************** */
					String sErrT = "*****************" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000010")/* @res""���ʱ���"" */
							+ "��[" + vosCostStaticVO[i].getinvcode() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0001155")/* @res""����"" */
							+ "��[" + vosCostStaticVO[i].getinvname() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0002282")/* @res""����"" */
							+ "[" + vosCostStaticVO[i].getSl() + "]***********************\n";
					// �ȸ���ת���������Ĳ�ѯ�ɱ�����
					String serrCT = "";
					// ת������
					if (vosCostStaticVO[i].getzcbm() != null) {
						voServuseHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
					}
					// �������Ĺ������Ĳ�ѯ�ɱ�����
					// ת��ɱ�����
					String sInCtcenterVOpk = null;
					if (vosCostStaticVO[i].getxhgzzx() != null) {
						sInCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getxhgzzx());
						if (sInCtcenterVOpk == null) {
							// sErr += "ת�빤������"+"[" +
							// vosCostStaticVO[i].getsygzzxmc() +
							// "]"+"û�ж�Ӧ�ĳɱ�����"+"\n";
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000014", null, new String[] { "[" + vosCostStaticVO[i].getsygzzxmc() + "]" })/* @res""ת�빤������{0}û�ж�Ӧ�ĳɱ�����"" */
									+ "\n";// "+"[" +
							// vosCostStaticVO[i].getsygzzxmc() +
							// "]"+"
							if (vosCostStaticVO[i].getxhbm() != null) {
								sInCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getxhbm(), null);
								if (sInCtcenterVOpk == null) {

									sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""ת�벿��{0}û�ж�Ӧ�ĳɱ�����"" */
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
								// sErr += "ת�벿��"+"[" +
								// vosCostStaticVO[i].getsybmmc() +
								// "]"+"û�ж�Ӧ�ĳɱ�����"+"\n";
								sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""ת�벿��{0}û�ж�Ӧ�ĳɱ�����"" */
										+ "\n";// "+"[" +
								// vosCostStaticVO[i].getsybmmc()
								// + "]"+"
							}
						} else {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000021")/* @res""ת��ɱ����ĺ�ת�벿�Ų���ͬʱΪ��"" */;
						}
					}
					// �ɱ����Ĳ��գ�����ɱ����ģ����򱣴沿�ţ�����ͬʱΪ�գ���ȡ
					if (sInCtcenterVOpk != null) {
						voServuseItemVO.setPk_ctcenter(sInCtcenterVOpk);
						voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
						// ����е��ɱ�����
						voServuseItemVO.setPk_difctcenter(sInCtcenterVOpk);
						// ����е�����
						voServuseItemVO.setPk_difdeptdoc(vosCostStaticVO[i].getxhbm());
					} else {
						if (vosCostStaticVO[i].getxhbm() != null) {
							voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
							// ����е�����
							voServuseItemVO.setPk_difdeptdoc(vosCostStaticVO[i].getxhbm());
						} else {
							continue;
						}
					}

					//

					voServuseHeaderVO.setPk_corp(pk_corp); // ��˾
					voServuseHeaderVO.setPk_calbody(pk_calbody); // �����������֯��
					voServuseHeaderVO.setCaccountyear(sSysinfos[2]); // �����
					voServuseHeaderVO.setCaccountmonth(sSysinfos[3]); // �����
					voServuseHeaderVO.setCbilltypecode("M6");
					// ��������
					voServuseHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6].toString()));
					// �Ƶ�����
					voServuseHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6].toString()));

					// ��ʼ����
					voServuseHeaderVO.setdstartDate(new UFDate(startDate));
					// ��������
					voServuseHeaderVO.setdendDate(new UFDate(endDate));
					// ��ʼ����
					voServuseHeaderVO.setdstartDate(new UFDate(startDate));
					// ��������
					voServuseHeaderVO.setdendDate(new UFDate(endDate));

					voServuseHeaderVO.setCbillmaker(sSysinfos[5]); // �Ƶ���
					voServuseHeaderVO.setPk_produce(vosCostStaticVO[i].getScddid()); // �������񶩵�D
					voServuseHeaderVO.setPk_produce(vosCostStaticVO[i].getPk_produce());
					// ������������ID
					voServuseHeaderVO.setPk_invbasdoc(vosCostStaticVO[i].getpk_invbasdoc());
					// �����������ID
					voServuseHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000017")/* @res""��Դ�ڼ���ϵͳ"" */);
					/** ******************���崦��*************************** */
					voServuseItemVO.setPk_corp(pk_corp); // ��˾
					voServuseItemVO.setPk_calbody(pk_calbody); // ����
					// ����Ϊ����ȡ
					if (vosCostStaticVO[i].getSl() == null) {
						sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000022")/* @res""��������Ϊ��"" */;
						continue;
					}

					voServuseItemVO.setNnumber(vosCostStaticVO[i].getSl()); // ����
					voServuseItemVO.setCsourcemodulename(ConstVO.m_sModuleME); //

					// �ƻ���
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
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000023")/* @res""û�еõ�����ļƻ��ۺͼƼ۷�ʽ"" */
									+ "\n";
							continue;
						}
						if (sResult[0][1] == null || sResult[0][1].trim().length() == 0) {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000024")/* @res""û�еõ�����ļƼ۷�ʽ"" */
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
					// �ɱ�Ҫ�ش���
					String[] sFac = null;
					if (sInCtcenterVOpk != null) {
						sFac = boWithOuter.getSubfacForInv(pk_corp, pk_calbody, sInCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc(), new UFBoolean(true));

					}
					if (sFac == null && sInCtcenterVOpk != null) {
						// û�л�óɱ���Ҫ����Ϣ
						sErr += sErrT + (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000019")/* @res""û�л�ý��ʵĳɱ�Ҫ��"" */+ "\n");
						continue;
					}
					if (sFac != null) {
						voServuseItemVO.setPk_subfac(sFac[0]); // �ɱ�Ҫ��
						voServuseItemVO.setPk_subfacset(sFac[1]); // �ɱ�Ҫ��
					}

					voServuseVO.setParentVO(voServuseHeaderVO);
					voServuseVO.setChildrenVO(new ServuseItemVO[] { voServuseItemVO });
					aryListServuseVO.add(voServuseVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)
			else {
				sErr += nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000025")/* @res""û�з�������������"" */
						+ "\n";
			}

			// ת��
			if (aryListServuseVO.size() != 0) {
				vosServuseVO = new ServuseVO[aryListServuseVO.size()];
				vosServuseVO = (ServuseVO[]) aryListServuseVO.toArray(vosServuseVO);
				if (vosServuseVO.length > 0) {
					String[] name = { "�����֯", "����Ա" };
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
	 * ���깤�����ȡ�깤���� <br>
	 * 1.�������е���Ҫ��mm_zjbgȡ�� <br>
	 * 2.���˳��������� <br>
	 * 3.�깤��ֹ�������깤����Ľ�������Ϊ׼
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

			// �õ�CostStaticVO
			vosCostStaticVO = mmToCm.queryStatisticWgslCM(pk_corp, pk_calbody, startDate, endDate);
			// ѭ������
			if (vosCostStaticVO != null) {

				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// ������еĳɱ�����
				// CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp,
				// pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {

					ProductVO voProductVO = new ProductVO();
					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************��ͷ����***************** */
					// ������ɱ�����
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
					// voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); //�ɱ�����
					// } else {
					// //û�л�ö�Ӧ�ĳɱ�����
					// continue;
					// }
					//
					voProductHeaderVO.setPk_corp(pk_corp); // ��˾
					voProductHeaderVO.setPk_calbody(pk_calbody); // �����������֯��
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // �����
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // �����
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // //
					// //��������
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // �Ƶ�����
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // �Ƶ���

					/** ******************���崦��*************************** */
					voProductItemVO.setPk_corp(pk_corp); //
					voProductItemVO.setPk_calbody(pk_calbody); //
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid()); // ����ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // ��Ʒ����ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // ��Ʒ����
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // ����
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC); //

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// ת��
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
	 * ���깤�����ȡ�ڲ����� <br>
	 * 1.���������ļƻ��깤���� - sum(�깤���� + ��������) <br>
	 * 2.��������ΪͶ��״̬ zt=B <br>
	 * 3.�깤����Ϊ���������깤 lylx=0 <br>
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

			// �õ�CostStaticVO
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCm.queryStatisticZcslCM(pk_corp, pk_calbody, startDate, endDate);
			// ѭ������
			if (vosCostStaticVO != null) {

				int ivosCostStaticVOlength = vosCostStaticVO.length;
				// ������еĳɱ�����
				// CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp,
				// pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {

					ProductVO voProductVO = new ProductVO();
					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************��ͷ����***************** */

					// ������ɱ�����
					CostRtVO crt = cbo.getCostRt(pk_corp, pk_calbody, vosCostStaticVO[i].getScddid(), vosCostStaticVO[i].getPk_produce(), endDate.toString());

					if (crt != null) {
						CostRtHeaderVO hvo = (CostRtHeaderVO) crt.getParentVO();
						CostRtItemVO[] ivos = (CostRtItemVO[]) crt.getChildrenVO();

						voProductHeaderVO.setPk_ctcenter(ivos[ivos.length - 1].getCtcenterID());

					} else {
						continue;

					}

					// //�ȸ��ݹ������Ĳ�ѯ�ɱ�����
					// String sCtcenterVOpk =
					// getCTCenter(ctcentervos, null,
					// vosCostStaticVO[i].getGzzxid());

					// if (sCtcenterVOpk != null) {
					// voProductHeaderVO.setPk_ctcenter(sCtcenterVOpk); //�ɱ�����
					// } else {
					// //û�л�ö�Ӧ�ĳɱ�����
					// continue;
					// }
					//
					voProductHeaderVO.setPk_corp(pk_corp); // ��˾
					voProductHeaderVO.setPk_calbody(pk_calbody); // �����������֯��
					voProductHeaderVO.setCaccountyear(sSysinfos[2]); // �����
					voProductHeaderVO.setCaccountmonth(sSysinfos[3]); // �����
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[6]));// //
					// //��������
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6]));// �Ƶ�����
					voProductHeaderVO.setCbillmaker(sSysinfos[5]); // �Ƶ���

					/** ******************���崦��*************************** */
					voProductItemVO.setPk_corp(pk_corp);//
					voProductItemVO.setPk_calbody(pk_calbody);//
					voProductItemVO.setPk_order(vosCostStaticVO[i].getScddid());// ����ID
					voProductItemVO.setPk_invbasdoc(vosCostStaticVO[i].getWlbmid()); // ��Ʒ����ID
					voProductItemVO.setPk_produce(vosCostStaticVO[i].getPk_produce()); // ��Ʒ����
					voProductItemVO.setNnumber(vosCostStaticVO[i].getSl()); // ����
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleSFC);//

					voProductVO.setParentVO(voProductHeaderVO);
					voProductVO.setChildrenVO(new ProductItemVO[] { voProductItemVO });
					aryListProductVO.add(voProductVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// ת��
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
	 * 10.21 �ڲ�Ʒ�̵������ȡ���ӿڣ� �ӿ����ݣ���õ���û��ǩ���������������������Ϊ�ڲ�Ʒ�̵���Ϣ���޸� ��
	 * ȡ��������ʵ�ʿ�������>=���ڻ���ڼ��һ�� and <=���ڻ���ڼ����һ�� and �����ڵ���û��ǩ�������
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
	 * sSysinfos[0] ----- ��˾ sSysinfos[1] ----- ���� sSysinfos[2] ----- ��ʼ����
	 * sSysinfos[3] ----- �������� sSysinfos[4] ----- �������� sSysinfos[5] ----- ��ǰ�û�
	 * sSysinfos[6] ----- ��ǰ���� sSysinfos[7] ----- ����� sSysinfos[8] ----- �����
	 * sSysinfos[9] ----- �Ƿ��ճɱ�����ͳ�Ʋ���
	 * 
	 * 
	 */

	public String queryZcslForXG(String[] sSysinfos, String pk_corp, String pk_calbody, UFDate startDate, UFDate endDate) throws nc.vo.cm.pub.CMBusinessException {
		String sErr = "";
		ProductVO[] vosProductVO = null;
		try {

			CommonDataBO boCommonData = new CommonDataBO();
			// �����ṩ��sql���
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
			// ������֣��Ҫ��ת�ӵ��Ѿ����յĲ��������ƣ��Ҹ���sql
			// ���£�����ɫ����������ӵ�����

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

			// ������еĳɱ�����
			CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);

			// ѭ������
			if (sAllPKs != null && sAllPKs.length > 0) {

				int iPkslength = sAllPKs.length;

				for (int i = 0; i < iPkslength; i++) {

					ProductHeaderVO voProductHeaderVO = new ProductHeaderVO();
					ProductItemVO voProductItemVO = new ProductItemVO();

					/** ****************��ͷ����***************** */

					// ������ճɱ�����ͳ�Ʋ���,���ݹ������ĺͲ��Ų�ѯ�ɱ�����
					if (sSysinfos[9].equals("Y")) {
						String[] sCtcenters = getCTCenterForM2(ctcentervos, sAllPKs[i][2], sAllPKs[i][3]);
						if (sCtcenters != null && sCtcenters.length > 0 && sCtcenters[0] != null) {
							voProductHeaderVO.setPk_ctcenter(sCtcenters[0]); // �ɱ�����
						} else {
							// û�л�ö�Ӧ�ĳɱ�����
							sErr += (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000001")/* @res""û�л�ö�Ӧ�ĳɱ�����"" */+ "\n");
							continue;

						}
					}

					// ��˾
					voProductHeaderVO.setPk_corp(pk_corp); // ��˾
					// �����������֯��
					voProductHeaderVO.setPk_calbody(pk_calbody);
					// �����
					voProductHeaderVO.setCaccountyear(sSysinfos[7]);
					// �����
					voProductHeaderVO.setCaccountmonth(sSysinfos[8]);
					// ��������
					voProductHeaderVO.setCbilltypecode(ConstVO.m_sBillZCPPD);
					// ����
					voProductHeaderVO.setPk_deptdoc(sAllPKs[i][2]);
					// ��ע
					voProductHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000026")/* @res""��������������ϵͳ"" */);
					/** ******************��β����*************************** */
					// ҵ������
					voProductHeaderVO.setDbilldate(new nc.vo.pub.lang.UFDate(sSysinfos[3])); // //
					// //��������
					// �Ƶ�����
					voProductHeaderVO.setDbillmakedate(new nc.vo.pub.lang.UFDate(sSysinfos[6])); // �Ƶ�����
					// �Ƶ���
					voProductHeaderVO.setCbillmaker(sSysinfos[5]);

					/** ******************���崦��*************************** */
					// a.pk_moid 0,
					// 1 a.scddh,
					// 2 a.scbmid,
					// 3 b.gzzxid,
					// 4 a.wlbmid,
					// 5 a.pk_produce,
					// 6 (c.wgsl - d.rksl)
					// ��˾
					voProductItemVO.setPk_corp(pk_corp); //
					// ����
					voProductItemVO.setPk_calbody(pk_calbody); //
					// //����ID
					voProductItemVO.setPk_order(sAllPKs[i][0]);
					// ��Ʒ����ID
					voProductItemVO.setPk_invbasdoc(sAllPKs[i][3]);
					// ��Ʒ����ID
					voProductItemVO.setPk_produce(sAllPKs[i][4]);
					// ����
					voProductItemVO.setNnumber(new UFDouble(sAllPKs[i][6]));
					// ģ���
					voProductItemVO.setCsourcemodulename(ConstVO.m_sModuleMO); //
					// �ж��Ƿ���ڱ�ͷ
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

			// ���ϵ��ݺ�
			if (!aryListProductVO.isEmpty()) {
				/** **************************���ݺŴ���************************************************* */
				BillcodeRuleBO boBillcodeRuleBO = new BillcodeRuleBO();
				String[] name = { "�����֯", "����Ա" };
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
	 * ��Ʒ����id private String m_pk_produce; ���ĵĶ��� private String m_scddid; ����
	 * private UFDouble m_sl;
	 * 
	 * ת������ private String m_zcbm; ת���������� private String m_zcgzzx; ��Ʒ����ID
	 * private String m_pk_invbasdoc; ���Ĳ��� private String m_xhbm; ���Ĺ������� private
	 * String m_xhgzzx; ���Ĳ�Ʒ����ID private String m_pk_toinvbasdoc; ���Ĳ�Ʒ����ID
	 * private String m_pk_toProduce;
	 */
	public String queryzzfzfwForM7(String[] sSysinfos, String pk_corp, String pk_calbody, String startDate, String endDate) throws nc.vo.cm.pub.CMBusinessException {
		// ��ʾ��Ϣ
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
			// �õ�CostStaticVO
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());
			vosCostStaticVO = mmToCm.getStatisticgxBcpCM(pk_corp, pk_calbody, startDate, endDate);
			if (vosCostStaticVO == null || vosCostStaticVO.length == 0) {
				return sErr;
			}
			int ivosCostStaticVOlength = vosCostStaticVO.length;
			boWithOuter = new WithOuterBO();
			boBillcodeRuleBO = getBillcodeRuleBO();
			// //����
			// int[0] ----- �����ľ���
			// int[1] ----- ���۵ľ���
			// int[2] ----- ���ľ���
			Integer[] iDataPrecision = boCommonData.getDataPrecision(sSysinfos[0]);

			// ѭ������
			if (vosCostStaticVO != null) {

				// ������еĳɱ�����
				CtcenterVO[] ctcentervos = findAllCTCenters(pk_corp, pk_calbody);
				for (int i = 0; i < ivosCostStaticVOlength; i++) {
					// �������ΪNUll����Ϊ0,����
					if (vosCostStaticVO[i].getSl() == null || vosCostStaticVO[i].getSl().equals(new UFDouble(0))) {
						continue;
					}
					ServuseVO voServuseVO = new ServuseVO();
					ServuseHeaderVO voServuseHeaderVO = new ServuseHeaderVO();
					ServuseItemVO voServuseItemVO = new ServuseItemVO();
					/** ****************��ͷ����***************** */
					String sErrT = "*****************" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000010")/* @res""���ʱ���"" */
							+ "��[" + vosCostStaticVO[i].getinvcode() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0001155")/* @res""����"" */
							+ "��[" + vosCostStaticVO[i].getinvname() + "]" + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UC000-0002282")/* @res""����"" */
							+ "[" + vosCostStaticVO[i].getSl() + "]***********************\n";
					// ת���ɱ�����
					String sOutCtcenterVOpk = null;
					// ת��ɱ�����
					String sInCtcenterVOpk = null; //

					/** *********ת��********************************************* */
					// �ȸ��ݹ������Ĳ��ҳɱ����ģ����Ϊ�գ����ղ��Ų���
					if (vosCostStaticVO[i].getzcgzzx() != null) {
						// �ȸ��ݹ������Ĳ��Ҷ�Ӧ�ɱ�����
						sOutCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getzcgzzx(), 1, "Y");
						if (sOutCtcenterVOpk != null) {
							voServuseHeaderVO.setPk_ctcenter(sOutCtcenterVOpk); // �ɱ�����
						} else {
							// sErr += "ת����������"+"[" +
							// vosCostStaticVO[i].getgybmmc() +
							// "]"+"û�ж�Ӧ���Ǹ���������Ϊĩ���ĳɱ�����"+"\n";
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000027", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""ת����������{0}û�ж�Ӧ���Ǹ���������Ϊĩ���ĳɱ�����"" */
									+ "\n";// "+"[" +
							// vosCostStaticVO[i].getgybmmc() +
							// "]"+"
							// Ȼ����ݲ��Ų��ҳɱ�����
							if (vosCostStaticVO[i].getzcbm() != null) {
								voServuseHeaderVO.setPk_deptdoc(vosCostStaticVO[i].getzcbm());
								sOutCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getzcbm(), null, 1, "Y");
								if (sOutCtcenterVOpk != null) {
									voServuseHeaderVO.setPk_ctcenter(sOutCtcenterVOpk);
								} else {
									// sErr += "ת������"+"[" +
									// vosCostStaticVO[i].getgybmmc() +
									// "]"+"û�ж�Ӧ���Ǹ���������Ϊĩ���ɱ�����"+"\n";
									sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000028", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""ת������{0}û�ж�Ӧ���Ǹ���������Ϊĩ���ɱ�����"" */
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
							// ���ݲ��Ų�ѯ��Ӧ�ɱ�����
							sOutCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getzcbm(), null, 1, "Y");
							if (sOutCtcenterVOpk != null) {
								voServuseHeaderVO.setPk_ctcenter(sOutCtcenterVOpk);
							} else {
								// sErr += "ת������"+"[" +
								// vosCostStaticVO[i].getgybmmc() +
								// "]"+"û�ж�Ӧ���Ǹ���������Ϊĩ���ɱ�����"+"\n";
								sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000028", null, new String[] { "[" + vosCostStaticVO[i].getgybmmc() + "]" })/* @res""ת������{0}û�ж�Ӧ���Ǹ���������Ϊĩ���ɱ�����"" */
										+ "\n";// "+"[" +
								// vosCostStaticVO[i].getgybmmc()
								// + "]"+"
								continue;

							}
						} else {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000029")/* @res""ת�����ź�ת���������Ĳ���ͬʱΪ��"" */
									+ "\n";
							continue;
						}

					}

					/** ******************ת��*********************************************************** */
					// �������Ĺ������Ĳ�ѯ�ɱ�����
					if (vosCostStaticVO[i].getxhgzzx() != null) {
						sInCtcenterVOpk = getCTCenter(ctcentervos, null, vosCostStaticVO[i].getxhgzzx());
						if (sInCtcenterVOpk == null) {
							// sErr += "ת�빤������"+"[" +
							// vosCostStaticVO[i].getsygzzxmc() +
							// "]"+"û�ж�Ӧ�ĳɱ�����"+"\n";
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000014", null, new String[] { "[" + vosCostStaticVO[i].getsygzzxmc() + "]" })/* @res""ת�빤������{0}û�ж�Ӧ�ĳɱ�����"" */
									+ "\n";// "+"[" +
							// vosCostStaticVO[i].getsygzzxmc() +
							// "]"+"
							if (vosCostStaticVO[i].getxhbm() != null) {
								voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
								sInCtcenterVOpk = getCTCenter(ctcentervos, vosCostStaticVO[i].getxhbm(), null);
								if (sInCtcenterVOpk == null) {
									// sErr += "ת�벿��"+"[" +
									// vosCostStaticVO[i].getsybmmc() +
									// "]"+"û�ж�Ӧ�ĳɱ�����"+"\n";
									sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""ת�벿��{0}û�ж�Ӧ�ĳɱ�����"" */
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
								// sErr += "ת�벿��"+"[" +
								// vosCostStaticVO[i].getsybmmc() +
								// "]"+"û�ж�Ӧ�ĳɱ�����"+"\n";
								sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000015", null, new String[] { "[" + vosCostStaticVO[i].getsybmmc() + "]" })/* @res""ת�벿��{0}û�ж�Ӧ�ĳɱ�����"" */
										+ "\n";// "+"[" +
								// vosCostStaticVO[i].getsybmmc()
								// + "]"+"
							}
						}
					}
					// �ɱ����Ĳ��գ�����ɱ����ģ����򱣴沿�ţ�����ͬʱΪ�գ���ȡ
					if (sInCtcenterVOpk != null) {
						voServuseItemVO.setPk_ctcenter(sInCtcenterVOpk);
						// ����е��ɱ�����
						voServuseItemVO.setPk_difctcenter(sInCtcenterVOpk);
						voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
						// ����е�����
						voServuseItemVO.setPk_difdeptdoc(vosCostStaticVO[i].getxhbm());
					} else {
						if (vosCostStaticVO[i].getxhbm() != null) {
							voServuseItemVO.setPk_deptdoc(vosCostStaticVO[i].getxhbm());
							// ����е�����
							voServuseItemVO.setPk_difdeptdoc(vosCostStaticVO[i].getxhbm());
						} else {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000021")/* @res""ת��ɱ����ĺ�ת�벿�Ų���ͬʱΪ��"" */;
							continue;
						}
					}
					//

					voServuseHeaderVO.setPk_corp(pk_corp); // ��˾
					voServuseHeaderVO.setPk_calbody(pk_calbody); // �����������֯��
					voServuseHeaderVO.setCaccountyear(sSysinfos[2]); // �����
					voServuseHeaderVO.setCaccountmonth(sSysinfos[3]); // �����
					voServuseHeaderVO.setCbilltypecode("M7");
					// ��������
					voServuseHeaderVO.setDbilldate(new UFDate(sSysinfos[6].toString()));
					// �Ƶ�����
					voServuseHeaderVO.setDbillmakedate(new UFDate(sSysinfos[6].toString()));
					// ��ʼ����
					voServuseHeaderVO.setdstartDate(new UFDate(startDate));
					// ��������
					voServuseHeaderVO.setdendDate(new UFDate(endDate));
					voServuseHeaderVO.setCbillmaker(sSysinfos[5]); // �Ƶ���
					voServuseHeaderVO.setPk_produce(vosCostStaticVO[i].getPk_produce());
					// ������������ID
					voServuseHeaderVO.setPk_invbasdoc(vosCostStaticVO[i].getpk_invbasdoc());
					// �����������ID
					voServuseHeaderVO.setVnote(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000017")/* @res""��Դ�ڼ���ϵͳ"" */);
					/** ******************���崦��*************************** */
					voServuseItemVO.setPk_corp(pk_corp); // ��˾
					voServuseItemVO.setPk_calbody(pk_calbody); // ����
					voServuseItemVO.setNnumber(vosCostStaticVO[i].getSl()); // ����
					voServuseItemVO.setCsourcemodulename(ConstVO.m_sModuleME); //
					// �ƻ���
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
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000023")/* @res""û�еõ�����ļƻ��ۺͼƼ۷�ʽ"" */
									+ "\n";
							continue;
						}
						if (sResult[0][1] == null || sResult[0][1].trim().length() == 0) {
							sErr += sErrT + nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000024")/* @res""û�еõ�����ļƼ۷�ʽ"" */
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
						// �ڲ�ת�Ƽ�
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

					// �ɱ�Ҫ�ش���
					if (sInCtcenterVOpk != null) {
						String[] sFac = boWithOuter.getSubfacForInv(pk_corp, pk_calbody, sInCtcenterVOpk, vosCostStaticVO[i].getpk_invbasdoc(), new UFBoolean(true));

						if (sFac == null) {
							// û�л�óɱ���Ҫ����Ϣ
							sErr += sErrT + (nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("30063030", "UPP-000019")/* @res""û�л�ý��ʵĳɱ�Ҫ��"" */+ "\n");
							continue;
						}

						voServuseItemVO.setPk_subfac(sFac[0]); // �ɱ�Ҫ��
						voServuseItemVO.setPk_subfacset(sFac[1]); // �ɱ�Ҫ��

					}
					//	
					voServuseVO.setParentVO(voServuseHeaderVO);
					voServuseVO.setChildrenVO(new ServuseItemVO[] { voServuseItemVO });
					aryListServuseVO.add(voServuseVO);

				} // for (int i = 0; i <ivosCostStaticVOlength ; i++)
			} // if(vosCostStaticVO!=null)

			// ת��
			if (aryListServuseVO.size() != 0) {
				vosServuseVO = new ServuseVO[aryListServuseVO.size()];
				vosServuseVO = (ServuseVO[]) aryListServuseVO.toArray(vosServuseVO);
				if (vosServuseVO.length > 0) {
					String[] name = { "�����֯", "����Ա" };
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
	 * ��������:�������������Ĺر� ����: String sCorpID ----- ��˾ String sCalbodyID ----- ����
	 * String[] sSourceOrderIDs ----- ����������������Id UFBoolean bAccount -----
	 * ��ĩ���ʻ���ȡ����ĩ���� * ����ֵ: * �쳣: *
	 * 
	 */
	public void setMOStatus(String sCorpID, String sCalbodyID, String[] sSourceOrderIDs, UFBoolean bAccount) throws CMBusinessException {
		try {
			IMmToCm mmToCm = (IMmToCm) NCLocator.getInstance().lookup(IMmToCm.class.getName());

			if (bAccount.booleanValue()) {
				// ��ĩ���ʵ���
				mmToCm.afterCoCM(sCorpID, sCalbodyID, sSourceOrderIDs);
			} else {
				// ȡ����ĩ���ʵ���
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
	 * �������ݡ�
	 * 
	 * �������ڣ�(2001-11-20 11:55:39)
	 * 
	 * @throws CMBusinessException
	 */
	public void updateData(String oldVersionNumber, String newVersionNumber) throws RemoteException {
		System.out.println("��������������ע�⣺ִ���������뿪ʼ����������");

		if (oldVersionNumber == null || oldVersionNumber.trim().length() == 0) {
			return;
		}

		CommonDataBO cbo = null;

		try {
			cbo = getCommonDataBO();

			if (oldVersionNumber.equals("3.0") || oldVersionNumber.equals("3.002")) {

				// 01 �������������������Ƿ�ʹ�ù���·�� Ϊ��Y��
				String sql = "select distinct isuseroute from  bd_produce ";
				String[][] sResult = cbo.queryDataNoTranslate(sql);

				if (sResult.length == 1) { // ��Ҫ��Ϊ�ϸ֣��ϸ���������Ҫ���´˲���

					sql = "update bd_produce set isuseroute = 'Y' where sfcbdx = 'Y' and pk_calbody in (select distinct pk_calbody from mm_kzcs)";
					cbo.execDataNoTranslate(sql);

				}

				// 02 ����ԭ���Ƿ񰴳ɱ�����ͳ�Ʋ���������ֵ�������������������Ƿ񰴳ɱ�����ͳ�Ʋ�������
				sql = "select distinct isctoutput from  bd_produce ";
				sResult = cbo.queryDataNoTranslate(sql);

				if (sResult.length == 1) { // ��Ҫ��Ϊ�ϸ֣��ϸ���������Ҫ���´˲���
					sql = "update bd_produce set isctoutput = (select case when csz2 = '��' then 'Y' else 'N' end from mm_kzcs where csbm = 'sfacbzx' and gcbm = bd_produce.pk_calbody) "
							+ "where sfcbdx = 'Y' and pk_calbody in (select distinct pk_calbody from mm_kzcs)";
					cbo.execData(sql);

				}

				// 03 ȥ�����������ݴ��䡱�͡����ʻ�ƿ�Ŀ���á����ܵ�
				sql = "delete from sm_funcregister where fun_code in('30066010','30066020','30063080','30063085')";
				cbo.execDataNoTranslate(sql);
				sql = "delete from sm_butnregister where fun_code like '30063085%'";
				cbo.execDataNoTranslate(sql);

				// 04 ɾ��ʵ�ʳɱ�����İ�ť
				sql = "delete from sm_butnregister where cfunid in ('CM01AA10000000039ADG','CM01AA10000000039ADJ','CM01AA10000000039ADK','CM01AA10000000039ADN','CM01AA10000000039ADP')";
				cbo.execDataNoTranslate(sql);

				// 05 ɾ����ǰ��230���������û��ɾ���Ŀ��̵���ע��ű�
				sql = "delete from bd_interfaceexec where pk_interface = 'CA500000000000000001'";
				cbo.execDataNoTranslate(sql);

			}

		} catch (Exception e) {
			CMLogger.error("��������ʧ��",e);
			throw new RemoteException("��������ʧ��",e);
		} finally {
			System.out.println("��������������ע�⣺ִ�����������������������");
		}
	}
	
	public StordocVO[] queryStordocVO(StordocVO condStordocVO, Boolean isAnd)throws CMBusinessException{
		OuterDMO dmo=null;
		try {
			dmo = new OuterDMO();
		} catch (SystemException e) {
			throw new CMBusinessException("ȡ�ֿ���Ϣ����",e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			throw new CMBusinessException("ȡ�ֿ���Ϣ����",e);
		}
		try {
			return dmo.queryByVO(condStordocVO, isAnd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new CMBusinessException("ȡ�ֿ���Ϣ����",e);
		}
		
		
	} 
}