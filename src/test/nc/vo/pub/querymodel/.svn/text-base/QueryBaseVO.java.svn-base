package nc.vo.pub.querymodel;

import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.GroupbyFldVO;
import nc.vo.iuforeport.businessquery.HavingCondVO;
import nc.vo.iuforeport.businessquery.JoinCondVO;
import nc.vo.iuforeport.businessquery.OrderbyFldVO;
import nc.vo.iuforeport.businessquery.QEValueObject;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.iuforeport.businessquery.WhereCondVO;
import nc.vo.pub.ValidationException;

public class QueryBaseVO extends QEValueObject
{
  private static final long serialVersionUID = 1111L;
  private String m_pkCorp;
  private String m_temptablename;
  private String m_note;
  private SelectFldVO[] m_selectFlds;
  private FromTableVO[] m_fromTables;
  private JoinCondVO[] m_joinConds;
  private WhereCondVO[] m_whereConds;
  private GroupbyFldVO[] m_groupbyFlds;
  private HavingCondVO[] m_havingConds;
  private OrderbyFldVO[] m_orderbyFlds;
  private SimpleCrossVO[] m_scs;
  private RotateCrossVO m_rotateCross;
  private String m_handSql;
  private String m_templetId;
  private String dsName;
  private String iEnvParamClass;

  public Object clone()
  {
    QueryBaseVO qb = new QueryBaseVO();

    qb.setPkCorp(this.m_pkCorp);
    qb.setTemptablename(this.m_temptablename);
    qb.setNote(this.m_note);
    qb.setHandSql(this.m_handSql);
    qb.setTempletId(this.m_templetId);
    qb.setDsName(this.dsName);
    qb.setIEnvParamClass(this.iEnvParamClass);

    SelectFldVO[] newSfs = null;
    int iLen = getSelectFlds() == null ? 0 : getSelectFlds().length;
    if (iLen != 0) {
      newSfs = new SelectFldVO[iLen];
      for (int i = 0; i < iLen; i++)
        newSfs[i] = ((SelectFldVO)getSelectFlds()[i].clone());
    }
    qb.setSelectFlds(newSfs);

    FromTableVO[] newFts = null;
    iLen = getFromTables() == null ? 0 : getFromTables().length;
    if (iLen != 0) {
      newFts = new FromTableVO[iLen];
      for (int i = 0; i < iLen; i++)
        newFts[i] = ((FromTableVO)getFromTables()[i].clone());
    }
    qb.setFromTables(newFts);

    JoinCondVO[] newJcs = null;
    iLen = getJoinConds() == null ? 0 : getJoinConds().length;
    if (iLen != 0) {
      newJcs = new JoinCondVO[iLen];
      for (int i = 0; i < iLen; i++)
        newJcs[i] = ((JoinCondVO)getJoinConds()[i].clone());
    }
    qb.setJoinConds(newJcs);

    WhereCondVO[] newWcs = null;
    iLen = getWhereConds() == null ? 0 : getWhereConds().length;
    if (iLen != 0) {
      newWcs = new WhereCondVO[iLen];
      for (int i = 0; i < iLen; i++)
        newWcs[i] = ((WhereCondVO)getWhereConds()[i].clone());
    }
    qb.setWhereConds(newWcs);

    OrderbyFldVO[] newOfs = null;
    iLen = getOrderbyFlds() == null ? 0 : getOrderbyFlds().length;
    if (iLen != 0) {
      newOfs = new OrderbyFldVO[iLen];
      for (int i = 0; i < iLen; i++)
        newOfs[i] = ((OrderbyFldVO)getOrderbyFlds()[i].clone());
    }
    qb.setOrderbyFlds(newOfs);

    GroupbyFldVO[] newGfs = null;
    iLen = getGroupbyFlds() == null ? 0 : getGroupbyFlds().length;
    if (iLen != 0) {
      newGfs = new GroupbyFldVO[iLen];
      for (int i = 0; i < iLen; i++)
        newGfs[i] = ((GroupbyFldVO)getGroupbyFlds()[i].clone());
    }
    qb.setGroupbyFlds(newGfs);

    HavingCondVO[] newHcs = null;
    iLen = getHavingConds() == null ? 0 : getHavingConds().length;
    if (iLen != 0) {
      newHcs = new HavingCondVO[iLen];
      for (int i = 0; i < iLen; i++)
        newHcs[i] = ((HavingCondVO)getHavingConds()[i].clone());
    }
    qb.setHavingConds(newHcs);

    SimpleCrossVO[] newScs = null;
    iLen = getScs() == null ? 0 : getScs().length;
    if (iLen != 0) {
      newScs = new SimpleCrossVO[iLen];
      for (int i = 0; i < iLen; i++)
        newScs[i] = ((SimpleCrossVO)getScs()[i].clone());
    }
    qb.setScs(newScs);

    if (getRotateCross() == null)
      qb.setRotateCross(null);
    else {
      qb.setRotateCross((RotateCrossVO)getRotateCross().clone());
    }
    return qb;
  }

  public String getDsName()
  {
    if ((this.dsName != null) && (this.dsName.trim().equals("")))
      this.dsName = null;
    return this.dsName;
  }

  public String getEntityName()
  {
    return "QueryBaseVO";
  }

  public SelectFldVO[] getFieldVOs()
  {
    return getSelectFlds();
  }

  public FromTableVO[] getFromTables()
  {
    return this.m_fromTables;
  }

  public GroupbyFldVO[] getGroupbyFlds()
  {
    return this.m_groupbyFlds;
  }

  public String getHandSql()
  {
    return this.m_handSql;
  }

  public HavingCondVO[] getHavingConds()
  {
    return this.m_havingConds;
  }

  public JoinCondVO[] getJoinConds()
  {
    return this.m_joinConds;
  }

  public String getNote()
  {
    return this.m_note;
  }

  public OrderbyFldVO[] getOrderbyFlds()
  {
    return this.m_orderbyFlds;
  }

  public String getPkCorp()
  {
    return this.m_pkCorp;
  }

  public RotateCrossVO getRotateCross()
  {
    return this.m_rotateCross;
  }

  public SimpleCrossVO[] getScs()
  {
    return this.m_scs;
  }

  public SelectFldVO[] getSelectFlds()
  {
    return this.m_selectFlds;
  }

  public String getTempletId()
  {
    return this.m_templetId;
  }

  public String getTemptablename()
  {
    return this.m_temptablename;
  }

  public WhereCondVO[] getWhereConds()
  {
    return this.m_whereConds;
  }

  public void setDsName(String newDsName)
  {
    this.dsName = newDsName;
  }

  public void setFromTables(FromTableVO[] fromTables)
  {
    this.m_fromTables = fromTables;
  }

  public void setGroupbyFlds(GroupbyFldVO[] groupbyFlds)
  {
    this.m_groupbyFlds = groupbyFlds;
  }

  public void setHandSql(String handSql)
  {
    this.m_handSql = handSql;
  }

  public void setHavingConds(HavingCondVO[] havingConds)
  {
    this.m_havingConds = havingConds;
  }

  public void setJoinConds(JoinCondVO[] joinConds)
  {
    this.m_joinConds = joinConds;
  }

  public void setNote(String note)
  {
    this.m_note = note;
  }

  public void setOrderbyFlds(OrderbyFldVO[] orderbyFlds)
  {
    this.m_orderbyFlds = orderbyFlds;
  }

  public void setPkCorp(String pkCorp)
  {
    this.m_pkCorp = pkCorp;
  }

  public void setRotateCross(RotateCrossVO newRotateCross)
  {
    this.m_rotateCross = newRotateCross;
  }

  public void setScs(SimpleCrossVO[] newScs)
  {
    this.m_scs = newScs;
  }

  public void setSelectFlds(SelectFldVO[] selectFlds)
  {
    this.m_selectFlds = selectFlds;
  }

  public void setTempletId(String templetId)
  {
    this.m_templetId = templetId;
  }

  public void setTemptablename(String temptablename)
  {
    this.m_temptablename = temptablename;
  }

  public void setWhereConds(WhereCondVO[] whereConds)
  {
    this.m_whereConds = whereConds;
  }

  public QueryBaseDef toQueryBaseDef(QueryModelNode node)
  {
    QueryBaseDef qbd = new QueryBaseDef();

    qbd.setPkCorp(this.m_pkCorp);
    qbd.setTemptablename(this.m_temptablename);
    qbd.setNote(this.m_note);
    qbd.setHandSql(this.m_handSql);

    SelectFldVO[] newSfs = null;
    int iLen = getSelectFlds() == null ? 0 : getSelectFlds().length;
    if (iLen != 0) {
      newSfs = new SelectFldVO[iLen];
      for (int i = 0; i < iLen; i++)
        newSfs[i] = ((SelectFldVO)getSelectFlds()[i].clone());
    }
    qbd.setSelectFlds(newSfs);

    FromTableVO[] newFts = null;
    iLen = getFromTables() == null ? 0 : getFromTables().length;
    if (iLen != 0) {
      newFts = new FromTableVO[iLen];
      for (int i = 0; i < iLen; i++)
        newFts[i] = ((FromTableVO)getFromTables()[i].clone());
    }
    qbd.setFromTables(newFts);

    JoinCondVO[] newJcs = null;
    iLen = getJoinConds() == null ? 0 : getJoinConds().length;
    if (iLen != 0) {
      newJcs = new JoinCondVO[iLen];
      for (int i = 0; i < iLen; i++)
        newJcs[i] = ((JoinCondVO)getJoinConds()[i].clone());
    }
    qbd.setJoinConds(newJcs);

    WhereCondVO[] newWcs = null;
    iLen = getWhereConds() == null ? 0 : getWhereConds().length;
    if (iLen != 0) {
      newWcs = new WhereCondVO[iLen];
      for (int i = 0; i < iLen; i++)
        newWcs[i] = ((WhereCondVO)getWhereConds()[i].clone());
    }
    qbd.setWhereConds(newWcs);

    OrderbyFldVO[] newOfs = null;
    iLen = getOrderbyFlds() == null ? 0 : getOrderbyFlds().length;
    if (iLen != 0) {
      newOfs = new OrderbyFldVO[iLen];
      for (int i = 0; i < iLen; i++)
        newOfs[i] = ((OrderbyFldVO)getOrderbyFlds()[i].clone());
    }
    qbd.setOrderbyFlds(newOfs);

    GroupbyFldVO[] newGfs = null;
    iLen = getGroupbyFlds() == null ? 0 : getGroupbyFlds().length;
    if (iLen != 0) {
      newGfs = new GroupbyFldVO[iLen];
      for (int i = 0; i < iLen; i++)
        newGfs[i] = ((GroupbyFldVO)getGroupbyFlds()[i].clone());
    }
    qbd.setGroupbyFlds(newGfs);

    HavingCondVO[] newHcs = null;
    iLen = getHavingConds() == null ? 0 : getHavingConds().length;
    if (iLen != 0) {
      newHcs = new HavingCondVO[iLen];
      for (int i = 0; i < iLen; i++)
        newHcs[i] = ((HavingCondVO)getHavingConds()[i].clone());
    }
    qbd.setHavingConds(newHcs);

    SimpleCrossVO[] newScs = null;
    iLen = getScs() == null ? 0 : getScs().length;
    if (iLen != 0) {
      newScs = new SimpleCrossVO[iLen];
      for (int i = 0; i < iLen; i++)
        newScs[i] = ((SimpleCrossVO)getScs()[i].clone());
    }
    qbd.setScs(newScs);

    if (getRotateCross() == null)
      qbd.setRotateCross(null);
    else {
      qbd.setRotateCross((RotateCrossVO)getRotateCross().clone());
    }
    try
    {
      qbd.setNode(node);
    } catch (Exception e) {
      e.printStackTrace();
    }

    qbd.setTempletId(this.m_templetId);
    qbd.setDsName(this.dsName);
    qbd.setIEnvParamClass(this.iEnvParamClass);
    return qbd;
  }

  public void validate()
    throws ValidationException
  {
  }

  public String getIEnvParamClass()
  {
    return this.iEnvParamClass;
  }

  public void setIEnvParamClass(String newIEnvParamClass)
  {
    this.iEnvParamClass = newIEnvParamClass;
  }
}