package nc.vo.pub.querymodel;

import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.pub.core.BizObject;
import nc.vo.pub.cquery.FldgroupVO;

public class QueryModelDef extends BizObject
{
  private static final long serialVersionUID = 1111L;
  private ParamVO[] m_params;
  private QueryBaseVO m_qb;
  private SqlRepairVO m_sr;
  private DataProcessVO m_dp;
  private PenetrateRuleVO[] m_prs;
  private MergeHeaderVO m_mh;
  private String dsName;
  private String createInfo;
  private ExtendModelVO extendModel;
  private transient FldgroupVO[] fldgroups;

  public QueryModelDef cloneQmd()
  {
    QueryModelDef newQmd = new QueryModelDef();

    if (this.m_params != null) {
      ParamVO[] newParams = new ParamVO[this.m_params.length];
      int i = 0; for (int pn = this.m_params.length; i < pn; i++) {
        newParams[i] = ((ParamVO)this.m_params[i].clone());
      }
      newQmd.setParamVOs(newParams);
    }

    if (this.m_qb != null) {
      QueryBaseVO newqb = (QueryBaseVO)this.m_qb.clone();
      newQmd.setQueryBaseVO(newqb);
    }

    if (this.m_sr != null) {
      SqlRepairVO newsr = (SqlRepairVO)this.m_sr.clone();
      newQmd.setSqlRepairVO(newsr);
    }

    if (this.m_dp != null) {
      DataProcessVO newdp = (DataProcessVO)this.m_dp.clone();
      newQmd.setDataProcessVO(newdp);
    }

    if (this.m_prs != null) {
      PenetrateRuleVO[] newPrs = new PenetrateRuleVO[this.m_prs.length];
      int i = 0; for (int pn = this.m_prs.length; i < pn; i++) {
        newPrs[i] = ((PenetrateRuleVO)this.m_prs[i].clone());
      }
      newQmd.setPenetrateRules(newPrs);
    }

    if (this.m_mh != null) {
      MergeHeaderVO newmh = (MergeHeaderVO)this.m_mh.clone();
      newQmd.setMh(newmh);
    }

    QueryModelNode node = (QueryModelNode)getNode();

    QueryModelNode newQnode = new QueryModelNode();
    newQnode.setID(node.getID());
    newQnode.setGUID(node.getGUID());
    newQnode.setDisplayName(node.getDisplayName());
    newQnode.setKind(node.getKind());
    newQnode.setParentGUID(node.getParentGUID());
    newQnode.setNote(node.getNote());
    newQmd.setNode(newQnode);

    newQmd.setDsName(this.dsName);

    newQmd.setCreateInfo(this.createInfo);

    newQmd.setExtendModel(this.extendModel);

    return newQmd;
  }

  public String getCreateInfo()
  {
    return this.createInfo;
  }

  public DataProcessVO getDataProcessVO()
  {
    return this.m_dp;
  }

  public String getDsName()
  {
    if ((this.dsName != null) && (this.dsName.trim().equals(""))) {
      this.dsName = null;
    }

    return this.dsName;
  }

  public FldgroupVO[] getFldgroups()
  {
    return this.fldgroups;
  }

  public MergeHeaderVO getMh()
  {
    return this.m_mh;
  }

  public ParamVO[] getParamVOs()
  {
    return this.m_params;
  }

  public PenetrateRuleVO[] getPenetrateRules()
  {
    return this.m_prs;
  }

  public QueryBaseDef getQueryBaseDef()
  {
    if (this.m_qb == null)             //yqq19
      this.m_qb = new QueryBaseVO();
    return this.m_qb.toQueryBaseDef((QueryModelNode)getNode());
  }

  public QueryBaseVO getQueryBaseVO()
  {
    return this.m_qb;
  }

  public RotateCrossVO getRotateCross()
  {
    if (this.m_qb == null) {
      return null;
    }
    return this.m_qb.getRotateCross();
  }

  public SimpleCrossVO[] getScs()
  {
    if (this.m_qb == null) {
      return null;
    }
    return this.m_qb.getScs();
  }

  public SqlRepairVO getSqlRepairVO()
  {
    return this.m_sr;
  }

  public void setCreateInfo(String newCreateInfo)
  {
    this.createInfo = newCreateInfo;
  }

  public void setDataProcessVO(DataProcessVO dp)
  {
    this.m_dp = dp;
  }

  public void setDsName(String newDsName)
  {
    this.dsName = newDsName;
  }

  public void setFldgroups(FldgroupVO[] newFldgroups)
  {
    this.fldgroups = newFldgroups;
  }

  public void setMh(MergeHeaderVO newMh)
  {
    this.m_mh = newMh;
  }

  public void setParamVOs(ParamVO[] params)
  {
    this.m_params = params;
  }

  public void setPenetrateRules(PenetrateRuleVO[] prs)
  {
    this.m_prs = prs;
  }

  public void setQueryBaseVO(QueryBaseDef qbd)
  {
    this.m_qb = qbd.getQueryBaseVO();
  }

  public void setQueryBaseVO(QueryBaseVO qb)
  {
    this.m_qb = qb;
  }

  /** @deprecated */
  public void setScs(SimpleCrossVO[] newScs)
  {
    if (this.m_qb != null)
      this.m_qb.setScs(newScs);
  }

  public void setSqlRepairVO(SqlRepairVO sr)
  {
    this.m_sr = sr;
  }

  public String getID()
  {
    if (getNode() == null) {
      String result = null;
      String tempName = getQueryBaseDef().getTemptablename();
      if ((tempName != null) && (tempName.startsWith("TEMQ_"))) {
        result = tempName.substring(5, tempName.length());
      }
      return result;
    }
    return super.getID();
  }

  public String toString()
  {
    if (getNode() == null) {
      return "";
    }
    return getDisplayName();
  }

  public ExtendModelVO getExtendModel()
  {
    return this.extendModel;
  }

  public void setExtendModel(ExtendModelVO newExtendModel)
  {
    this.extendModel = newExtendModel;
  }
}