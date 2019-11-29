package nc.ui.ep.dj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nc.bs.logging.Log;
import nc.pub.fi.framework.common.command.ICommand;
import nc.pub.fi.framework.common.manager.ICommandManager;
import nc.pub.fi.framework.platform.IPlatformUI;
import nc.pub.fi.framework.platform.internal.DefaultPlatformUI;
import nc.pub.fi.framework.platform.manager.IStatusManager;
import nc.ui.arap.global.ArapBtnRes;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.arap.pubdj.INodeCode;
import nc.ui.arap.templetcache.DjlxTempletCacheNew;
import nc.ui.glpub.IParent;
import nc.ui.glpub.IUiPanel;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.pub.StrResPorxy;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uap.security.Authenticator;
import nc.vo.uap.security.AuthenticatorFactory;

public abstract class DjBasePanel extends ToftPanel
  implements INodeCode
{
  public ButtonObject m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000888"), 5, "��ѯ");

  public ButtonObject m_boDjOperation = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000019"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000889"), 5, "���ݲ���");

  public ButtonObject m_boAdd = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000002"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000823"), 5, "����");

  public ButtonObject m_boCopy = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000043"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000824"), 5, "����");

  public ButtonObject m_boDelDj = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000039"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000825"), 5, "ɾ��");

  public ButtonObject m_boEdit = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000045"), NCLangRes.getInstance().getStrByID("common", "UC001-0000045"), 5, "�޸�");

  public ButtonObject m_boDjlx = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC000-0000807"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000826"), 5, "��������");

  public ButtonObject m_boYsf = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000036"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000919"), 5, "Ԥ�ո�");

  public ButtonObject m_boWszz = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000816"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000816"), 5, "����ת��");

  public ButtonObject m_boHandPay = new ButtonObject(NCLangRes.getInstance().getStrByID("2006", "UPP2006-v51-000271"), NCLangRes.getInstance().getStrByID("2006", "UPP2006-v51-000271"), 5, "�ֹ�֧��");

  public ButtonObject m_boShenheOrNo = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000020"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000020"), 5, "��˲���");

  public ButtonObject m_boShenhe = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000051"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000051"), 5, "���");

  public ButtonObject m_boUnShenhe = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000047"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000047"), 5, "�����");

  public ButtonObject m_boQROrNo = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000920"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000920"), 5, "ȷ��");

  public ButtonObject m_boQR = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000920"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000920"), 5, "ȷ��");

  public ButtonObject m_boUnQR = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000921"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000921"), 5, "ȡ��ȷ��");

  public ButtonObject m_boYhQROrNo = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000412"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000412"), 5, "ǩ��ȷ��");

  public ButtonObject m_boCloseOrNo = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000922"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000922"), 5, "�رղ���");

  public ButtonObject m_boClose = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000923"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000923"), 5, "�ر�");

  public ButtonObject m_boCancelClose = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000924"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000924"), 5, "ȡ���ر�");

  public ButtonObject m_boCloseRow = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000925"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000926"), 5, "�йر�");

  public ButtonObject m_boCancelCloseRow = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000927"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000926"), 5, "ȡ���йر�");

  public ButtonObject m_boNewByBudget = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000928"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000928"), 5, "����Ԥ������");

  public ButtonObject m_boNewByBudgetYT = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000929"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000929"), 5, "����Ԥ�ᵥ����");

  public ButtonObject m_boSplitSSItem = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000930"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000930"), 5, "��ֱ�����");

  public ButtonObject m_boQcCloseOrNo = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000922"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000922"), 5, "�رղ���");

  public ButtonObject m_boQcClose = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-001095"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000923"), 5, "�ڳ��ر�");

  public ButtonObject m_boQcCancelClose = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000924"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000924"), 5, "ȡ���ر�");

  public ButtonObject m_boVerify = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000053"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000053"), 5, "��ʱ����");

  public ButtonObject m_boVerify_Bc = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000040"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000040"), 5, "����");

  public ButtonObject m_boVerify_Fp = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000038"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000038"), 5, "����");

  public ButtonObject m_boVerify_Hx = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000039"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000039"), 5, "����");

  public ButtonObject m_boVerify_Filter = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000037"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000037"), 5, "����");

  public ButtonObject m_boVerify_Search = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000931"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000931"), 5, "���������");

  public ButtonObject m_boVerify_Search_Total = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000932"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000932"), 5, "�����ݺ���");

  public ButtonObject m_boVerify_All = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), 5, "ȫѡ");

  public ButtonObject m_boVerify_N = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), 5, "ȫ��");

  public ButtonObject m_boVerify_GoBack = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), 5, "����");

  public ButtonObject m_boVerify_ShowDj = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000041"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000041"), 5, "���鵥��");

  public ButtonObject m_boVerify_ConfirmBc = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000044"), NCLangRes.getInstance().getStrByID("common", "UC001-0000044"), 5, "ȷ��");

  public ButtonObject m_boVerify_CancelBc = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), 5, "ȡ��");

  public ButtonObject m_boYhQR = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000412"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000412"), 5, "ǩ��ȷ��");

  public ButtonObject m_boUnYhQR = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000934"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000934"), 5, "ȡ��ǩ��ȷ��");

  public ButtonObject m_boSave = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), 5, "����");

  public ButtonObject m_boCancel = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), 5, "ȡ��");

  public ButtonObject m_boAddRow = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000012"), NCLangRes.getInstance().getStrByID("common", "UC001-0000012"), 5, "����");

  public ButtonObject m_boDelRow = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), 5, "ɾ��");

  public ButtonObject m_boChangePage = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000022"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000022"), 5, "���");

  public ButtonObject m_boFirstPage = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000042"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000042"), 5, "��ҳ");

  public ButtonObject m_boUpPage = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000043"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000043"), 5, "��һҳ");

  public ButtonObject m_boDownPage = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000044"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000044"), 5, "��һҳ");

  public ButtonObject m_boLastPage = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000045"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000045"), 5, "ĩҳ");

  public ButtonObject m_boRefreshPage = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), NCLangRes.getInstance().getStrByID("common", "UC001-0000009"), 5, "ˢ��");

  public ButtonObject m_boNextPage = new ButtonObject(NCLangRes.getInstance().getStrByID("200602", "UPT200602-v35-000013"), NCLangRes.getInstance().getStrByID("200602", "UPT200602-v35-000013"), 5, "�·�");

  public ButtonObject m_boPreviousPage = new ButtonObject(NCLangRes.getInstance().getStrByID("200602", "UPT200602-v35-000012"), NCLangRes.getInstance().getStrByID("200602", "UPT200602-v35-000012"), 5, "�Ϸ�");

  public ButtonObject m_boSelectAll = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), NCLangRes.getInstance().getStrByID("common", "UC001-0000041"), 5, "ȫѡ");

  public ButtonObject m_boUnSelectAll = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), NCLangRes.getInstance().getStrByID("common", "UC001-0000042"), 5, "ȫ��");

  public ButtonObject m_boPausetransactP = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000050"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000050"), 5, "�������");

  public ButtonObject m_boPausetransact = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000034"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000034"), 5, "����");

  public ButtonObject m_boUnPausetransact = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000035"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000035"), 5, "ȡ������");

  public ButtonObject m_boAddZyx = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000890"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000827"), 5, "����");

  public ButtonObject m_boDelZyx = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000891"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000828"), 5, "ɾ��");

  public ButtonObject m_boEditZyx = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000892"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000829"), 5, "�޸�");

  public ButtonObject m_boAddRowZyx = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000893"), NCLangRes.getInstance().getStrByID("common", "UC001-0000012"), 5, "����");

  public ButtonObject m_boDelRowZyx = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000894"), NCLangRes.getInstance().getStrByID("common", "UC001-0000013"), 5, "ɾ��");

  public ButtonObject m_boSaveZyx = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000895"), NCLangRes.getInstance().getStrByID("common", "UC001-0000001"), 5, "����");

  public ButtonObject m_boCancelZyx = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000896"), NCLangRes.getInstance().getStrByID("common", "UC001-0000008"), 5, "ȡ��");

  public ButtonObject m_boAssistant = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000036"), NCLangRes.getInstance().getStrByID("common", "UC001-0000036"), 5, "����");

  public ButtonObject m_boRent = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000028"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000028"), 5, "���");

  public ButtonObject m_boPf = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000030"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000030"), 5, "����ƾ֤");

  public ButtonObject m_boXm = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000024"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000024"), 5, "��Ŀ����");

  public ButtonObject m_boLinkQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000052"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000052"), 5, "����");

  public ButtonObject m_boApprove = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000026"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000026"), 5, "�������");

  public ButtonObject m_boParticularQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000935"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000935"), 5, "�������");

  public ButtonObject m_boProvide = new ButtonObject(NCLangRes.getInstance().getStrByID("200602", "UPT200602-v35-000016"), NCLangRes.getInstance().getStrByID("200602", "UPT200602-v35-000016"), 5, "���鷢Ʊ");

  public ButtonObject m_boPJ = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000937"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000937"), 5, "����Ʊ��");
  public ButtonObject m_boSFK = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC000-0002200"), NCLangRes.getInstance().getStrByID("common", "UC000-0002200"), 5, "�ո���Э��");
  public ButtonObject m_boBudgetImpl = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000430"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000430"), 5, "Ԥ��ִ�����");
  public ButtonObject m_boLinkCredit = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000938"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000938"), 5, "�����Ŵ�");

  public ButtonObject m_boPrint = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000031"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000031"), 5, "��ӡ�б�");

  public ButtonObject m_boPrint_All = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), NCLangRes.getInstance().getStrByID("common", "UC001-0000007"), 5, "��ӡ");

  public ButtonObject m_boPrint_OfficialP = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000939"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000939"), 5, "��ӡ����");

  public ButtonObject m_boPrint_Official = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000032"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000032"), 5, "��ʽ��ӡ");

  public ButtonObject m_boPrint_Official_Cancel = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000033"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000033"), 5, "ȡ����ʽ��ӡ");

  public ButtonObject m_boNext = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UCH022"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000897"), 5, "�б�");

  public ButtonObject m_btDocMng = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000835"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000835"), 5, "�ĵ�����");

  public ButtonObject m_boArapTofts = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000940"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000940"), 2, "ί�и���");

  public ButtonObject m_boPrintZYX = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000046"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000046"), 2, "��ӡ������");

  public ButtonObject m_red = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000108"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000108"), 2, "���");

  public ButtonObject m_boWTSK = new ButtonObject(StrResPorxy.getWTSKBtnName(), StrResPorxy.getWTSKBtnName(), 2, "ί���տ�");

  public ButtonObject m_boTempSave = new ButtonObject(StrResPorxy.getTempSaveBtnName(), StrResPorxy.getTempSaveBtnName(), 5, "�ݴ�");
  public ButtonObject m_boXtQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000109"), NCLangRes.getInstance().getStrByID("2006030102", "UPT2006030102-000109"), 5, "����Эͬ����");

  public ButtonObject m_boRelDjBack = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), NCLangRes.getInstance().getStrByID("common", "UC001-0000038"), 5, "����");

  public ButtonObject m_boQueryFTSPayment = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-001073"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-001073"), 5, "����ί�и�����");

  public ButtonObject m_boQueryAccount = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "uarappub-v35-000373"), NCLangRes.getInstance().getStrByID("common", "uarappub-v35-000373"), 5, "�����˻�Ԥ�����");

  public ButtonObject m_boQueryList = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "uarappub-v35-000372"), NCLangRes.getInstance().getStrByID("common", "uarappub-v35-000372"), 5, "��������");

  public ButtonObject m_boQueryFTSReceiver = new ButtonObject(NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-001085"), NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-001085"), 5, "����ί���տ���");
  public ButtonObject m_boCombinedPayment = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "uarappub-v50-000070"), NCLangRes.getInstance().getStrByID("common", "uarappub-v50-000070"), 5, "�ϲ�֧��");

  public ButtonObject m_boCreatFABill = new ButtonObject(NCLangRes.getInstance().getStrByID("200602", "UPP200602-v501-000001"), NCLangRes.getInstance().getStrByID("200602", "UPP200602-v501-000001"), 5, "���ɱ䶯��");

  public ButtonObject m_boSSQryDJ = new ButtonObject(NCLangRes.getInstance().getStrByID("2006", "UPT2006-v51-000002"), NCLangRes.getInstance().getStrByID("2006", "UPT2006-v51-000002"), 5, "���鵥��");

  public ButtonObject m_boDJQrySS = new ButtonObject(NCLangRes.getInstance().getStrByID("2006", "UPT2006-v51-000003"), NCLangRes.getInstance().getStrByID("2006", "UPT2006-v51-000003"), 5, "��������������");

  //add by zwx 2019-9-5 ����Ӧ�մ���ư�ť
  public ButtonObject m_boSynBC = new ButtonObject("Ӧ�մ����" , "Ӧ�մ����");
  
  //add by zsh 2019-9-5 ����Ӧ������Ʋư�ť
  public ButtonObject m_boCBC = new ButtonObject("Ӧ�������", "Ӧ�������");
  
  //add by zy 2019-10-15 ���ӱ�ƻس�
  public ButtonObject m_boBCRetreat = new ButtonObject("��ƻس�" , "��ƻس�");
  
  public DjlxTempletCacheNew cache = null;

  protected ARAPDjDataBuffer m_djDataBuffer = null;
  protected ARAPDjSettingParam m_djSettingParam = null;
  protected ARAPDjUIController[] m_arrayControllers = null;
  protected ButtonObject[] m_aryButtonGroup = null;
  public IParent m_parent = null;
  private IPlatformUI platform = null;
  public IUiPanel m_cardView = null;

  public ARAPDjDataBuffer getDjDataBuffer()
  {
    if (this.m_djDataBuffer == null) {
      this.m_djDataBuffer = new ARAPDjDataBuffer();
    }
    return this.m_djDataBuffer;
  }

  public ARAPDjSettingParam getDjSettingParam() {
    if (this.m_djSettingParam == null) {
      this.m_djSettingParam = new ARAPDjSettingParam();
    }

    return this.m_djSettingParam; }

  public boolean checkID(ButtonObject bo) {
    if ((bo == this.m_boAdd) || (bo == this.m_boCopy) || (bo == this.m_boEdit) || (bo == this.m_boQR) || (bo == this.m_boWszz) || (bo == this.m_boCombinedPayment))
    {
      boolean checked;
      try {
        checked = checkAuthen();
      } catch (Exception e) {
        showErrorMessage(e.getMessage());
        return false;
      }
      if (!(checked)) {
        showErrorMessage(NCLangRes.getInstance().getStrByID("2008", "UPP2008-000102"));
        return false;
      }
    }
    return true; }

  public void onButtonClicked(ButtonObject bo) {
    String tag = bo.getTag();
    try {
      if (null != tag)
        getPlatform().getExtendCommandManager().getCommand(tag).execute();
    }
    catch (Exception e)
    {
      Log.getInstance(super.getClass()).debug(e.getMessage());
      showErrorMessage(e.getMessage());
    }
  }

  protected abstract ButtonObject[] getDjButtons();

  protected abstract ARAPDjUIController[] getUIControllers();

  public IPlatformUI getPlatform()
  {
    if (this.platform == null) {
      this.platform = new DefaultPlatformUI();
      this.platform.setRoot(this);
    }
    return this.platform;
  }

  public void setButtons(ButtonObject[] buttons) {
    ButtonObject[] extendButtons = getPlatform().getExtendStatusManager().getButtonObjects();
    if (extendButtons != null) {
      List buttonsList = new ArrayList();
      buttonsList.addAll(Arrays.asList(buttons));
      buttonsList.addAll(Arrays.asList(extendButtons));
      super.setButtons((ButtonObject[])(ButtonObject[])buttonsList.toArray(new ButtonObject[0]));
    } else {
      super.setButtons(buttons);
    }
  }

  public void refreshBtnStats()
  {
  }

  public abstract String getNodeCode();

  public boolean checkAuthen()
    throws Exception
  {
    String djdl = getDjDataBuffer().getCurrentDjdl();
    if ((!("fj".equals(djdl))) && (!("fk".equals(djdl))) && (!("wf".equals(djdl))) && (!("hj".equals(djdl)))) {
      return true;
    }
    String pk_corp = getDjSettingParam().getPk_corp();
    String ywbm = getDjDataBuffer().getCurrentDjlxoid();

    DjLXVO djlx = MyClientEnvironment.getDjlxVOByPK(pk_corp, ywbm);
    if ((null != djlx.getIscasign()) && (djlx.getIscasign().booleanValue())) {
      boolean bl = AuthenticatorFactory.getInstance().getAuthenticator(getDjSettingParam().getPk_user()).validateUserOptional();
      return bl;
    }
    return true; }

  protected void beginPressBtn(ButtonObject bo) {
    showHintMessage("");
    if ((null == bo) || (ArapBtnRes.getBtnRes(bo.getCode()) == null) || (null == ArapBtnRes.getBtnRes(bo.getCode())[0])) return;
    try {
      showHintMessage(NCLangRes.getInstance().getStrByID("2008", ArapBtnRes.getBtnRes(bo.getCode())[0]));
    }
    catch (Exception e)
    {
    }
  }

  public void endPressBtn(ButtonObject bo) {
    showHintMessage("");
    if ((null == bo) || (ArapBtnRes.getBtnRes(bo.getCode()) == null) || (null == ArapBtnRes.getBtnRes(bo.getCode())[1])) return;
    try {
      showHintMessage(NCLangRes.getInstance().getStrByID("2008", ArapBtnRes.getBtnRes(bo.getCode())[1]));
    }
    catch (Exception e)
    {
    }
  }
}