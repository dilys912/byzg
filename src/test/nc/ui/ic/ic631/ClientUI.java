package nc.ui.ic.ic631;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import nc.ui.ic.pub.bill.query.QueryConditionDlg;
import nc.ui.ic.pub.report.ICReportHelper;
import nc.ui.ic.pub.report.IcBaseReport;
import nc.ui.ic.pub.scale.ScaleInit;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.bd.CorpVO;
import nc.vo.ic.ic631.NumAnlyzHeaderVO;
import nc.vo.ic.ic631.NumAnlyzVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVOUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.sm.UserVO;

public class ClientUI extends IcBaseReport
{
  private QueryConditionDlg ivjQueryConditionDlg = null;
  private ReportBaseClass ivjReportBase = null;

  private ButtonObject m_boQuery = new ButtonObject(NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), NCLangRes.getInstance().getStrByID("common", "UC001-0000006"), 2, "查询");

  private Hashtable m_htShowFlag = new Hashtable();

  private ButtonObject[] m_MainButtonGroup = { this.m_boQuery };

  private String[] m_sBodyFormulas = { "unitname->getColValue(bd_corp,unitname,pk_corp,pk_corp)", "bodyname->getColValue(bd_calbody,bodyname,pk_calbody,pk_calbody)", "storname->getColValue(bd_stordoc,storname,pk_stordoc,cwarehouseid)", "invbasid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)", "invcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,invbasid)", "invname->getColValue(bd_invbasdoc,invname,pk_invbasdoc,invbasid)", "invspec->getColValue(bd_invbasdoc,invspec,pk_invbasdoc,invbasid)", "invtype->getColValue(bd_invbasdoc,invtype,pk_invbasdoc,invbasid)", "pk_measdoc->getColValue(bd_invbasdoc,pk_measdoc,pk_invbasdoc,invbasid)", "mainmeasname->getColValue(bd_measdoc,measname,pk_measdoc,pk_measdoc)" };

  private String m_sCorpID = "";
  private String m_sHeaderVOName = "nc.vo.ic.ic631.NumAnlyzHeaderVO";
  private String m_sItemVOName = "nc.vo.ic.ic631.NumAnlyzItemVO";
  private UFDate m_sLogDate = null;
  private String m_sPNodeCode = "40083402";
  private String m_sQTempletID = "11113206400000314631";

  private String m_sRNodeName = "40083402SYS";
  private String m_sTitle = NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000069");
  private String m_sUnitCode = "2011";
  private String m_sUnitName = "2011";
  private String m_sUserID = "";
  private String m_sVOName = "nc.vo.ic.ic631.NumAnlyzVO";
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

  public ConditionVO[] adjustCondition(ConditionVO[] voCons)
  {
    Vector v = new Vector();
    boolean bhasDate = false;
    boolean bhasUnit = false;

    String sdbizdate = this.m_sLogDate.toString();

    for (int i = 0; i < voCons.length; i++)
    {
      if (voCons[i].getFieldCode().equals("unitcode")) {
        bhasUnit = true;
      }

      v.addElement(voCons[i]);
    }

    if (!bhasUnit) {
      ConditionVO voCon = new ConditionVO();
      voCon.setLogic(true);
      voCon.setFieldCode("unitcode");
      voCon.setFieldName(NCLangRes.getInstance().getStrByID("common", "UC000-0000787"));
      voCon.setOperaCode("=");
      voCon.setDataType(0);
      voCon.setValue(this.m_sUnitCode);
      RefResultVO ref = new RefResultVO();
      ref.setRefPK(this.m_sCorpID);
      voCon.setRefResult(ref);

      v.addElement(voCon);
    }

    if (!bhasDate) {
      ConditionVO voCon = new ConditionVO();
      voCon.setLogic(true);
      voCon.setFieldCode("logdate");
      voCon.setFieldName(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000153"));
      voCon.setOperaCode("=");
      voCon.setDataType(0);
      voCon.setValue(this.m_sLogDate.toString());
      v.addElement(voCon);
    }
    if (v.size() > 0) {
      ConditionVO[] voReCons = new ConditionVO[v.size()];
      v.copyInto(voReCons);

      return voReCons;
    }

    return voCons;
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
      this.ivjQueryConditionDlg.setDefaultCloseOperation(1);

      this.ivjQueryConditionDlg.setTempletID(this.m_sCorpID, getPNodeCode(), this.m_sUserID, null);
      ArrayList alCorpIDs = new ArrayList();
      try {
        alCorpIDs = ICReportHelper.queryCorpIDs(this.m_sUserID);
      } catch (Exception e) {
        SCMEnv.error(e);
      }

      this.ivjQueryConditionDlg.initQueryDlgRef();
      this.ivjQueryConditionDlg.initCorpRef("unitcode", this.m_sCorpID, alCorpIDs);

      this.ivjQueryConditionDlg.setRefInitWhereClause("storcode", "仓库档案", "gubflag='N'  and pk_corp=", "unitcode");

      this.ivjQueryConditionDlg.setRefInitWhereClause("invcode", "存货档案", " bd_invmandoc.pk_corp=", "unitcode");

      this.ivjQueryConditionDlg.setRefInitWhereClause("bodycode", "库存组织", " bd_calbody.pk_corp=", "unitcode");

      this.ivjQueryConditionDlg.setCorpRefs("unitcode", new String[] { "bodycode", "storcode", "invclasscode", "invcode" });
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

  public NumAnlyzHeaderVO getHeader(ConditionVO[] voCons)
  {
    NumAnlyzHeaderVO voHead = new NumAnlyzHeaderVO();

    voHead.setUnitname(this.m_sUnitName);

    voHead.setQuerydate(this.m_sLogDate);

    for (int i = 0; i < voCons.length; i++)
    {
      if (!voCons[i].getFieldCode().equals("unitcode"))
        continue;
      RefResultVO ref = voCons[i].getRefResult();
      if (ref == null)
        break;
      voHead.setUnitname(ref.getRefName());
      break;
    }

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
    NumAnlyzVO vo = (NumAnlyzVO)getReportBaseClass().getBillValueVO(this.m_sVOName, this.m_sHeaderVOName, this.m_sItemVOName);

    if (null == vo) {
      vo = new NumAnlyzVO();
    }
    if (null == vo.getParentVO()) {
      vo.setParentVO(new NumAnlyzHeaderVO());
    }
    return vo;
  }

  public String getTitle()
  {
    if (getReportBaseClass().getReportTitle() != null) {
      return getReportBaseClass().getReportTitle();
    }
    return NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000069");
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

    ScaleInit si = new ScaleInit(this.m_sUserID, this.m_sCorpID);
    ArrayList alParam = new ArrayList();
    alParam.add(new String[] { "num", "innum", "outnum", "defnum", "balancenum" });
    alParam.add(null);
    alParam.add(new String[] { "nprice" });
    alParam.add(new String[] { "balancemny" });
    alParam.add(null);
    try {
      si.setScale(getReportBaseClass(), alParam);
    } catch (Exception e) {
      showHintMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000003") + e.getMessage());
    }
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
    if (strBodyFields != null)
      for (int i = 0; i < strBodyFields.length; i++)
        this.m_htShowFlag.put(strBodyFields[i], new Boolean(true));
  }

  public void onButtonClicked(ButtonObject bo)
  {
    if (bo == this.m_boQuery) {
      onQuery(true);
    }
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
      getConditionDlg().checkCondition(voCons);
      NumAnlyzVO vo = NumAnlyzHelper.queryNumAnlyz(adjustCondition(voCons));
      getReportBaseClass().setHeadDataVO(getHeader(voCons));

      if ((vo.getChildrenVO() != null) && (vo.getChildrenVO().length > 0)) {
        SuperVOUtil.execFormulaWithVOs(vo.getChildrenVO(), this.m_sBodyFormulas, null);
        getReportBaseClass().setBodyDataVO(vo.getChildrenVO());
      }
      else {
        getReportBaseClass().getBillModel().clearBodyData();
      }
      calculateTotal();
      if ((vo.getChildrenVO() != null) && (vo.getChildrenVO().length > 0)) {
        Object cwarehouseid = vo.getChildrenVO()[0].getAttributeValue("cwarehouseid");
        if (cwarehouseid == null) {
          setShowFlag("storname", false);
          setShowFlag("bodyname", true);
        } else {
          setShowFlag("storname", true);
          setShowFlag("bodyname", false);
        }

      }

      String msg = (String)vo.getParentVO().getAttributeValue("msg");
      if ((msg != null) && (msg.toString().trim().length() > 0))
        showErrorMessage(msg);
    }
    catch (Exception e) {
      SCMEnv.error(e);
      showErrorMessage(NCLangRes.getInstance().getStrByID("4008report", "UPP4008report-000050") + e.getMessage());
    }
  }

  public void setShowFlag(String strKey, boolean flag)
  {
    if (flag) {
      if ((this.m_htShowFlag.containsKey(strKey)) && 
        (!((Boolean)this.m_htShowFlag.get(strKey)).booleanValue()))
      {
        getReportBaseClass().showBodyTableCol(strKey);

        this.m_htShowFlag.put(strKey, new Boolean(true));
      }

    }
    else if ((this.m_htShowFlag.containsKey(strKey)) && 
      (((Boolean)this.m_htShowFlag.get(strKey)).booleanValue()))
    {
      getReportBaseClass().hideBodyTableCol(strKey);

      this.m_htShowFlag.put(strKey, new Boolean(false));
    }
  }
}