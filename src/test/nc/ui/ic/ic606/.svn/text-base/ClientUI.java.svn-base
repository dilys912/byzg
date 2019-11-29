package nc.ui.ic.ic606;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import nc.ui.ic.pub.bill.query.ICheckCondition;
import nc.ui.ic.pub.bill.query.QueryConditionDlg;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.ic.pub.report.IcBaseReport;
import nc.ui.ic.pub.scale.ScaleInit;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic605.CargcardHeaderVO;
import nc.vo.ic.ic605.CargcardVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.pub.report.QryOrderVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class ClientUI extends IcBaseReport
  implements ICheckCondition
{
  private QueryConditionDlg ivjQueryConditionDlg = null;
  private ReportBaseClass ivjReportBase = null;

  private ButtonObject m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 2, "查询");

  private Hashtable m_htShowFlag = new Hashtable();

  private ButtonObject[] m_MainButtonGroup = { this.m_boQuery };
  private String m_sCorpID = null;

  private String[] m_sFormulas = { "billtypename->getColValue(bd_billtype,billtypename,pk_billtypecode,cbilltypecode)", "pk_invbasdoc->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)", "pk_invcl->getColValue(bd_invbasdoc,pk_invcl,pk_invbasdoc,pk_invbasdoc)", "pk_measdoc->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,pk_invbasdoc)", "invcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,pk_invbasdoc)", "invname->getColValue(bd_invbasdoc,invname,pk_invbasdoc,pk_invbasdoc)", "invspec->getColValue(bd_invbasdoc,invspec,pk_invbasdoc,pk_invbasdoc)", "invtype->getColValue(bd_invbasdoc,invtype,pk_invbasdoc,pk_invbasdoc)", "invclasscode->getColValue(bd_invcl,invclasscode,pk_invcl,pk_invcl)", "invclassname->getColValue(bd_invcl,invclassname,pk_invcl,pk_invcl)", "mainmeasname->getColValue(bd_measdoc,measname,pk_measdoc,pk_measdoc)", "castunitname->getColValue(bd_measdoc,measname,pk_measdoc,castunitid)", "cscode->getColValue(bd_cargdoc,cscode,pk_cargdoc,cspaceid)", "csname->getColValue(bd_cargdoc,csname,pk_cargdoc,cspaceid)" };

  private String m_sHeaderVOName = "nc.vo.ic.ic605.CargcardHeaderVO";
  private String m_sItemVOName = "nc.vo.ic.ic605.CargcardItemVO";
  private UFDate m_sLogDate = null;
  private String m_sPNodeCode = "40083010";
  private String m_sQTempletID = "11113206400000314606";

  private String m_sRNodeName = "40083010SYS";
  private String m_sTitle = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000027");
  private String m_sUnitCode = "2011";
  private String m_sUnitName = "2011";
  private String m_sUserID = "";
  private String m_sVOName = "nc.vo.ic.ic605.CargcardVO";
  private AggregatedValueObject m_voReport = null;

  public ClientUI()
  {
    initialize();
  }

  public ClientUI(FramePanel ff)
  {
    setFrame(ff);
    initialize();
  }

  public String checkICCondition(ConditionVO[] voCons)
  {
    try
    {
      getConditionDlg().checkBracket(voCons);
      getConditionDlg().checkOncetime(voCons, new String[] { "pk_corp", "pk_stordoc", "cscodeflag" });
    }
    catch (BusinessException be) {
      return be.getMessage();
    }

    return null;
  }

  public ConditionVO[] adjustCondition(ConditionVO[] voCons)
  {
    Vector v = new Vector();

    boolean bhasUnit = false;
    boolean bhasEmptyCarg = false;

    String sdbizdate = this.m_sLogDate.toString();

    for (int i = 0; i < voCons.length; i++)
    {
      if (voCons[i].getFieldCode().equals("cscodeflag")) {
        bhasEmptyCarg = true;
      }

      v.addElement(voCons[i]);
    }

    if (!bhasEmptyCarg) {
      ConditionVO voCon = new ConditionVO();
      voCon.setLogic(true);
      voCon.setFieldCode("cscodeflag");
      voCon.setFieldName(NCLangRes.getInstance().getStrByID("4008report", "UPT40083010-000001"));
      voCon.setOperaCode("=");
      voCon.setDataType(0);
      voCon.setValue("Y");

      v.addElement(voCon);
    }

    if (v.size() > 0) {
      ConditionVO[] voReCons = new ConditionVO[v.size()];
      v.copyInto(voReCons);

      return getConditionDlg().getExpandVOs(voReCons);
    }

    return getConditionDlg().getExpandVOs(voCons);
  }

  protected void getCEnvInfo()
  {
    try
    {
      ClientEnvironment ce = ClientEnvironment.getInstance();
      try {
        this.m_sUserID = ce.getUser().getPrimaryKey();
      }
      catch (Exception e)
      {
      }

      try
      {
        this.m_sCorpID = ce.getCorporation().getPrimaryKey();
        this.m_sUnitCode = ce.getCorporation().getUnitcode();
        this.m_sUnitName = ce.getCorporation().getUnitname();
      }
      catch (Exception e)
      {
      }
      try
      {
        if (ce.getDate() != null)
          this.m_sLogDate = ce.getDate();
      }
      catch (Exception e)
      {
      }
    }
    catch (Exception e)
    {
    }
  }

  private QueryConditionDlg getConditionDlg()
  {
    if (this.ivjQueryConditionDlg == null) {
      this.ivjQueryConditionDlg = new QueryConditionDlg(this);

      this.ivjQueryConditionDlg.setTempletID(this.m_sCorpID, getPNodeCode(), this.m_sUserID, null);
      ArrayList alCorpIDs = new ArrayList();
      try {
        alCorpIDs = ICReportHelper.queryCorpIDs(this.m_sUserID);
      } catch (Exception e) {
        SCMEnv.error(e);
      }

      this.ivjQueryConditionDlg.addICheckCondition(this);
      this.ivjQueryConditionDlg.initCorpRef("pk_corp", this.m_sCorpID, alCorpIDs);

      String[] sThenClear = { "vfree0", "vbatchcode", "bd_measdoc2.measname" };

      this.ivjQueryConditionDlg.setAutoClear("invcode", sThenClear);

      this.ivjQueryConditionDlg.setAutoClear("pk_corp", new String[] { "pk_stordoc", "cscode" });

      this.ivjQueryConditionDlg.setAutoClear("pk_stordoc", new String[] { "cscode" });

      this.ivjQueryConditionDlg.setRefInitWhereClause("pk_stordoc", "仓库档案", "csflag='Y' and gubflag='N'  and pk_corp=", "pk_corp");

      this.ivjQueryConditionDlg.setCorpRefs("pk_corp", new String[] { "pk_stordoc", "invcode", "invclasscode" });

      this.ivjQueryConditionDlg.setRefInitWhereClause("invcode", "存货档案", " bd_invmandoc.pk_corp=", "pk_corp");

      this.ivjQueryConditionDlg.setRefInitWhereClause("cscode", "货位档案", "bd_cargdoc.endflag='Y' and bd_cargdoc.pk_stordoc=", "pk_stordoc");

      this.ivjQueryConditionDlg.setRefInitWhereClause("bodycode", "库存组织", " bd_calbody.pk_corp=", "pk_corp");
    }

    return this.ivjQueryConditionDlg;
  }

  public String getCorpID()
  {
    return this.m_sCorpID;
  }

  public String getDefaultPNodeCode()
  {
    return this.m_sPNodeCode;
  }

  public CargcardHeaderVO getHeader(ConditionVO[] voCons)
  {
    CargcardHeaderVO voHead = new CargcardHeaderVO();

    for (int i = 0; i < voCons.length; i++)
    {
      if (!voCons[i].getFieldCode().equals("pk_corp"))
        continue;
      RefResultVO ref = voCons[i].getRefResult();
      if (ref == null)
        continue;
      voHead.setUnitname(ref.getRefName());
    }

    voHead.setQuerydate(this.m_sLogDate);

    voHead.setQuerycondition(getConditionDlg().getChText());
    return voHead;
  }

  public ReportBaseClass getReportBaseClass()
  {
    if (this.ivjReportBase == null) {
      try {
        this.ivjReportBase = new ReportBaseClass();
        this.ivjReportBase.setName("ReportBase");
      }
      catch (Throwable ivjExc)
      {
      }

    }

    return this.ivjReportBase;
  }

  public AggregatedValueObject getReportVO()
  {
    CargcardVO vo = (CargcardVO)getReportBaseClass().getBillValueVO(this.m_sVOName, this.m_sHeaderVOName, this.m_sItemVOName);

    if (null == vo) {
      vo = new CargcardVO();
    }
    if (null == vo.getParentVO()) {
      vo.setParentVO(new CargcardHeaderVO());
    }
    return vo;
  }

  public String getTitle()
  {
    if (getReportBaseClass().getReportTitle() != null) {
      return getReportBaseClass().getReportTitle();
    }
    return NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000027");
  }

  public String getUserID()
  {
    return this.m_sUserID;
  }

  private void initialize()
  {
    setName("ClientUI");
    setLayout(new BorderLayout());
    setSize(774, 419);
    add(getReportBaseClass(), "Center");
    getCEnvInfo();

    setButtons(getButtonArray(this.m_MainButtonGroup));

    initReportTemplet(this.m_sRNodeName);
    getReportBaseClass().setRowNOShow(true);
    getReportBaseClass().setTatolRowShow(true);
    getReportBaseClass().setMaxLenOfHeadItem("unitname", 100);
    getReportBaseClass().setMaxLenOfHeadItem("querycondition", 500);

    ScaleInit si = new ScaleInit(this.m_sCorpID, this.m_sCorpID);
    ArrayList alParam = new ArrayList();
    alParam.add(new String[] { "num", "ninnum", "noutnum", "nfreezenum" });
    alParam.add(new String[] { "assistnum", "ninassistnum", "noutassistnum" });
    alParam.add(new String[] { "nprice" });
    alParam.add(new String[] { "ninmny", "noutmny", "nmny" });
    alParam.add(new String[] { "hsl" });
    try {
      si.setScale(getReportBaseClass(), alParam);
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000003") + e.getMessage());
    }

    getReportBaseClass().getBillTable().addSortListener();
  }

  public void initReportTemplet(String sNodeName)
  {
    try
    {
      getReportBaseClass().setTempletID(this.m_sCorpID, getPNodeCode(), this.m_sUserID, null);
    } catch (Exception e) {
      showErrorMessage(NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000019"));
      return;
    }
    BillData bd = getReportBaseClass().getBillData();
    if (bd == null) {
      SCMEnv.out("--> billdata null.");
      return;
    }

    String[] strBodyFields = getReportBaseClass().getBodyFields();
    String[] strHeadFields = getReportBaseClass().getHeadFields();
    String[] strTailFields = getReportBaseClass().getTailFields();

    QryOrderVO[] groupVOs = getReportBaseClass().getGroupVOs();
    QryOrderVO[] orderVOs = getReportBaseClass().getOrderVOs();
    String[] strSums = getReportBaseClass().getSums();

    String[] strTitles = { getReportBaseClass().getReportTitle(), getReportBaseClass().getReportSubtitle() };

    if (strBodyFields != null)
      for (int i = 0; i < strBodyFields.length; i++)
        this.m_htShowFlag.put(strBodyFields[i], new Boolean(true));
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_boQuery)
      onQuery(true);
    else
      super.onButtonClicked(bo);
  }

  public void onQuery(boolean bQuery)
  {
    getConditionDlg().hideNormal();
    if ((bQuery) || (!this.m_bEverQry)) {
      getConditionDlg().showModal();
      this.m_bEverQry = true;
    } else {
      getConditionDlg().onButtonConfig();
    }

    if (!getConditionDlg().isCloseOK())
      return;
    setDlgSubTotal(null);
    try {
      ConditionVO[] voCons = getConditionDlg().getConditionVO();
      getReportBaseClass().setHeadDataVO(getHeader(voCons));
      getReportBaseClass().getBillModel().clearBodyData();

      CargcardVO vo = CargDisHelper.queryCargDis(adjustCondition(voCons));
      if (vo == null)
        return;
      SuperVOUtil.execFormulaWithVOs(vo.getChildrenVO(), this.m_sFormulas, null);

      getReportBaseClass().setBodyDataVO(vo.getChildrenVO());

      calculateTotal();
    }
    catch (Exception e) {
      SCMEnv.error(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000004") + e.toString());
    }
  }
}