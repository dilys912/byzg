package nc.vo.iuforeport.businessquery;

import nc.vo.pub.ValueObject;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.querymodel.QueryBaseVO;
import nc.vo.pub.querymodel.RotateCrossVO;
import nc.vo.pub.querymodel.SimpleCrossVO;

public class QueryBaseDef extends QueryDef
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

  public void addField(SelectFldVO[] sfs)
  {
    int iLen = getSelectFlds() == null ? 0 : getSelectFlds().length;
    int iNewLen = sfs == null ? 0 : sfs.length;
    SelectFldVO[] newsfs = new SelectFldVO[iLen + iNewLen];
    for (int i = 0; i < iLen; i++)
      newsfs[i] = getSelectFlds()[i];
    for (int i = iLen; i < iLen + iNewLen; i++)
      newsfs[i] = sfs[(i - iLen)];
    setSelectFlds(newsfs);
  }

  public void addGroup(GroupbyFldVO gf)
  {
    ValueObject[] vos = addToVOs(getGroupbyFlds(), gf);
    GroupbyFldVO[] newgfs = new GroupbyFldVO[vos.length];
    for (int i = 0; i < vos.length; i++)
      newgfs[i] = ((GroupbyFldVO)vos[i]);
    setGroupbyFlds(newgfs);
  }

  public void addJoin(JoinCondVO jc)
  {
    ValueObject[] vos = addToVOs(getJoinConds(), jc);
    JoinCondVO[] newjcs = new JoinCondVO[vos.length];
    for (int i = 0; i < vos.length; i++)
      newjcs[i] = ((JoinCondVO)vos[i]);
    setJoinConds(newjcs);
  }

  public void addOrder(OrderbyFldVO of)
  {
    ValueObject[] vos = addToVOs(getOrderbyFlds(), of);
    OrderbyFldVO[] newofs = new OrderbyFldVO[vos.length];
    for (int i = 0; i < vos.length; i++)
      newofs[i] = ((OrderbyFldVO)vos[i]);
    setOrderbyFlds(newofs);
  }

  public void addTable(FromTableVO ft)
  {
    ValueObject[] vos = addToVOs(getFromTables(), ft);
    FromTableVO[] newfts = new FromTableVO[vos.length];
    for (int i = 0; i < vos.length; i++)
      newfts[i] = ((FromTableVO)vos[i]);
    setFromTables(newfts);
  }

  public void addWhere(WhereCondVO wc)
  {
    ValueObject[] vos = addToVOs(getWhereConds(), wc);
    WhereCondVO[] newwcs = new WhereCondVO[vos.length];
    for (int i = 0; i < vos.length; i++)
      newwcs[i] = ((WhereCondVO)vos[i]);
    setWhereConds(newwcs);
  }

  public Object clone()
  {
    QueryBaseDef qbd = new QueryBaseDef();

    qbd.setPkCorp(this.m_pkCorp);
    qbd.setTemptablename(this.m_temptablename);
    qbd.setNote(this.m_note);
    qbd.setHandSql(this.m_handSql);
    qbd.setTempletId(this.m_templetId);
    qbd.setDsName(this.dsName);
    qbd.setIEnvParamClass(this.iEnvParamClass);

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
      qbd.setNode((ObjectNode)getNode().clone());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return qbd;
  }

  public String getDsName()
  {
    if ((this.dsName != null) && (this.dsName.trim().equals("")))
      this.dsName = null;
    return this.dsName;
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

  public QueryBaseVO getQueryBaseVO()
  {
    QueryBaseVO qb = new QueryBaseVO();
    qb.setPkCorp(this.m_pkCorp);
    qb.setTemptablename(this.m_temptablename);
    qb.setNote(this.m_note);
    qb.setHandSql(this.m_handSql);
    qb.setSelectFlds(this.m_selectFlds);
    qb.setFromTables(this.m_fromTables);
    qb.setJoinConds(this.m_joinConds);
    qb.setWhereConds(this.m_whereConds);
    qb.setGroupbyFlds(this.m_groupbyFlds);
    qb.setDsName(this.dsName);
    qb.setOrderbyFlds(this.m_orderbyFlds);
    qb.setScs(this.m_scs);
    qb.setRotateCross(this.m_rotateCross);
    qb.setHavingConds(this.m_havingConds);
    qb.setTempletId(this.m_templetId);
    qb.setIEnvParamClass(this.iEnvParamClass);

    return qb;
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

  public String getIEnvParamClass()
  {
    return this.iEnvParamClass;
  }

  public void setIEnvParamClass(String newIEnvParamClass)
  {
    this.iEnvParamClass = newIEnvParamClass;
  }

  public ValueObject[] addToVOs(ValueObject[] oldVOs, ValueObject addVO)
  {
    int iLen = oldVOs == null ? 0 : oldVOs.length;
    ValueObject[] newVOs = new ValueObject[iLen + 1];
    for (int i = 0; i < iLen; i++) {
      newVOs[i] = oldVOs[i];
    }
    newVOs[iLen] = addVO;
    return newVOs;
  }
}