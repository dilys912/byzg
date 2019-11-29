package nc.bs.ep.dj;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import nc.bs.arap.global.ArapClassRunBO;
import nc.bs.arap.global.PubBO;
import nc.bs.arap.pub.CreatJoinSQLTool;
import nc.bs.arap.pub.GlVoucherInfoQueryBO;
import nc.bs.arap.pub.PubDAO;
import nc.bs.arap.pub.PubMethods;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.mw.sqltrans.TempTable;
import nc.bs.pub.SystemException;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.arap.global.CurTime;
import nc.vo.arap.global.DjVOTreaterAid;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.mapping.ArapBaseMappingMeta;
import nc.vo.arap.mapping.Arap_djfbVOMeta;
import nc.vo.arap.mapping.Arap_djzbVOMeta;
import nc.vo.arap.mapping.Arap_fengcunVOMeta;
import nc.vo.arap.mapping.Arap_itemVOMeta;
import nc.vo.arap.mapping.Arap_item_bVOMeta;
import nc.vo.arap.mapping.IArapMappingMeta;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.arap.pub.GlVoucherInfoVO;
import nc.vo.arap.pub.QryCondArrayVO;
import nc.vo.arap.pub.QryCondVO;
import nc.vo.ep.dj.DJFBItemVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DjCondVO;
import nc.vo.ep.dj.DjfkxybVO;
import nc.vo.ep.dj.ShenheException;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.RefResultVO;
import nc.vo.scm.sourcebill.LightBillVO;

public class DJZBDAO
{
  BaseDAO basedao = new BaseDAO();
  PubDAO pubdao = new PubDAO();
  DjfkxybDAO fkdao = new DjfkxybDAO();
  FreeDAO defdao = new FreeDAO();
  Arap_djfbVOMeta fbmeta = new Arap_djfbVOMeta();
  Arap_djzbVOMeta zbmeta = new Arap_djzbVOMeta();
  Arap_itemVOMeta ssmeta = new Arap_itemVOMeta();
  Arap_item_bVOMeta bbmeta = new Arap_item_bVOMeta();
  
  public DJZBHeaderVO findHeaderByPrimaryKey(String key)
    throws DAOException, SQLException
  {
    boolean isUsedGL = false;
    String sqlFromClause = " from ARAP_DJZB zb where zb.dr=0 and zb.vouchid = '" + key + "'";
    







    DJZBHeaderVO dJZBHeader = null;
    Vector vHead = getDJZBHeaderVOsUniversalVector(sqlFromClause, isUsedGL, null);
    if (vHead.size() > 0) {
      dJZBHeader = (DJZBHeaderVO)vHead.elementAt(0);
    }
    return dJZBHeader;
  }
  
  public Vector<DJZBHeaderVO> findHeaderByPrimaryKeys(String[] keys)
    throws DAOException, SQLException, DbException
  {
    String sqlFromClause = null;
    if (keys.length > 150)
    {
      String table = createHeaderTempTable(keys);
      sqlFromClause = " from ARAP_DJZB zb inner join " + table + " temp on zb.vouchid=temp.vouchid_temp where zb.dr=0";
    }
    else
    {
      if (keys.length == 0) {
        return new Vector();
      }
      sqlFromClause = " from ARAP_DJZB zb where zb.dr=0 and zb.vouchid in( ";
      StringBuffer cause = new StringBuffer();
      for (int i = 0; i < keys.length; i++) {
        if (i == keys.length - 1) {
          cause.append("'" + keys[i] + "')");
        } else {
          cause.append("'" + keys[i] + "',");
        }
      }
      sqlFromClause = sqlFromClause + cause.toString();
    }
    boolean isUsedGL = false;
    









    Vector<DJZBHeaderVO> vHead = null;
    
    vHead = getDJZBHeaderVOsUniversalVector(sqlFromClause, isUsedGL, null);
    
    return vHead;
  }
  
  public void cancel_Close_SS(DJZBVO vo)
    throws DAOException
  {
    String ts = CurTime.getCurrentTimeStampString();
    DJZBHeaderVO head = (DJZBHeaderVO)vo.getParentVO();
    String cond = "vouchid= '" + head.getVouchid() + "' and ts='" + head.getts() + "'";
    head.setTs(new UFDateTime(ts));
    head.setDjzt(new Integer(2));
    head.setZdr(null);
    head.setZdrq(null);
    this.pubdao.updateObjectPartly(head, this.ssmeta, new String[] { "djzt", "zdr", "zdrq", "ts" }, cond);
  }
  
  public void cancel_Close_SSItem(DJZBItemVO itemVO)
    throws DAOException
  {
    String cond = "fb_oid='" + itemVO.getFb_oid() + "'";
    this.pubdao.updateObjectPartly(itemVO, this.bbmeta, new String[] { "closer", "closedate" }, cond);
  }
  
  public void close_item(String vouchid, String zdr, UFDate zdrq)
    throws SQLException, DAOException
  {
    DJZBHeaderVO item = new DJZBHeaderVO();
    item.setDjzt(new Integer(5));
    item.setZdr(zdr);
    item.setZdrq(zdrq);
    String cond = "vouchid='" + vouchid + "' " + 
      " and not exists ( select vouchid from arap_item_b where arap_item_b.vouchid=arap_item.vouchid and closer is null)";
    this.pubdao.updateObjectPartly(item, this.ssmeta, new String[] { "djzt", "zdr", "zdrq" }, cond);
  }
  
  public void close_SS(DJZBVO vo)
    throws SQLException, DAOException
  {
    DJZBHeaderVO head = (DJZBHeaderVO)vo.getParentVO();
    String ts = CurTime.getCurrentTimeStampString();
    head.setDjzt(new Integer(5));
    String cond = "vouchid='" + head.getVouchid() + "' and ts='" + head.getts() + "'";
    head.setTs(new UFDateTime(ts));
    this.pubdao.updateObjectPartly(new DJZBHeaderVO[] { head }, this.ssmeta, new String[] { "djzt", "zdr", "zdrq", "ts" }, cond);
  }
  
  public void close_SSItem(DJZBItemVO[] itemVO)
    throws SQLException, DAOException
  {
    this.pubdao.updateObjectPartly(itemVO, this.bbmeta, new String[] { "closer", "closedate" }, null);
  }
  
  public void deleteDjByPks(String[] pks)
    throws DAOException
  {
    PubDAO dao = new PubDAO();
    dao.deleteVOsByPks(this.zbmeta, pks);
  }
  
  public void delete(DJZBVO vo)
    throws DAOException, SQLException
  {
    deleteItemsForHeader(((DJZBHeaderVO)vo.getParentVO()).getPrimaryKey());
    
    deleteHeader((DJZBHeaderVO)vo.getParentVO());
  }
  
  public void delete_SS(DJZBVO vo)
    throws SQLException, DAOException
  {
    deleteItemsForHeader_SS(((DJZBHeaderVO)vo.getParentVO()).getPrimaryKey());
    
    deleteHeader_SS((DJZBHeaderVO)vo.getParentVO());
  }
  
  public void deleteHeader(DJZBHeaderVO vo)
    throws DAOException
  {
    this.pubdao.deleteVOsByPks(this.zbmeta, new String[] { vo.getPrimaryKey() });
  }
  
  public void deleteHeader_SS(DJZBHeaderVO vo)
    throws DAOException
  {
    this.pubdao.deleteVOsByPks(this.ssmeta, new String[] { vo.getPrimaryKey() });
  }
  
  public void deleteItem(DJZBItemVO vo)
    throws DAOException
  {
    this.defdao.deleteFreesForFB(vo.getFb_oid());
    
    this.fkdao.deleteFkxyforFB(vo.getFb_oid());
    
    this.pubdao.deleteVOsByPks(this.fbmeta, new String[] { vo.getFb_oid() });
  }
  
  public void deleteItem_SS(DJZBItemVO vo)
    throws DAOException
  {
    this.defdao.deleteFreesForFB(vo.getFb_oid());
    


    this.pubdao.deleteVOsByPks(this.bbmeta, new String[] { vo.getFb_oid() });
  }
  
  public void deleteItemPK(String Fb_oid)
    throws SQLException, DAOException
  {
    this.defdao.deleteFreesForFB(Fb_oid);
    
    this.fkdao.deleteFkxyforFB(Fb_oid);
    
    this.pubdao.deleteVOsByPks(this.fbmeta, new String[] { Fb_oid });
  }
  
  public void deleteItemsForHeader(String headerKey)
    throws DAOException
  {
    this.defdao.deleteFreesForZB(headerKey);
    
    this.fkdao.deleteFkxyforZB(headerKey);
    this.pubdao.deleteVOsByWhere(this.fbmeta, "WHERE VOUCHID = '" + headerKey + "'");
  }
  
  public void deleteItemsForHeader_SS(String headerKey)
    throws DAOException
  {
    this.defdao.deleteFreesForZB(headerKey);
    this.pubdao.deleteVOsByPks(this.bbmeta, new String[] { headerKey });
  }
  
  public void distributeDjzb_cf(String vouchid, String ts, String djdl, Integer iDjzt)
    throws SQLException, ArapBusinessException, DbException
  {
    String tableName = "arap_djzb";
    if (djdl.equals("ss")) {
      tableName = "arap_item";
    }
    String sql = 
      "update   " + 
      tableName + 
      "  set dr=dr  where vouchid ='" + 
      vouchid + 
      "' and ts='" + 
      ts + 
      "'";
    int count = 0;
    ArapBusinessException dis = new ArapBusinessException();
    ResMessage res = new ResMessage();
    
    res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000503");
    res.isSuccess = false;
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(getds());
      count = pm.getJdbcSession().executeUpdate(sql);
    }
    finally
    {
      pm.release();
    }
    if (count <= 0)
    {
      dis = new ArapBusinessException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000503"));
      res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000503");
      dis.m_ResMessage = res;
      throw dis;
    }
  }
  
  public String distributeDjzb_Item(String vouchid, String ts, String djdl)
    throws SQLException, ArapBusinessException, DbException
  {
    String newts = CurTime.getCurrentTimeStampString();
    String tableName = "arap_djzb";
    if (djdl.equals("ss")) {
      tableName = "arap_item";
    }
    String sql = 
      "update " + tableName + " set ts='" + newts + "'  where vouchid ='" + 
      vouchid + 
      "' and ts='" + 
      ts + 
      "'";
    ArapBusinessException dis = new ArapBusinessException();
    ResMessage res = new ResMessage();
    res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000503");
    res.isSuccess = false;
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(getds());
      int iCount = pm.getJdbcSession().executeUpdate(sql);
      if (iCount <= 0)
      {
        dis = new ArapBusinessException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000503"));
        res.strMessage = NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000503");
        dis.m_ResMessage = res;
        throw dis;
      }
    }
    finally
    {
      pm.release();
    }
    pm.release();
    
    return newts;
  }
  
  public boolean exist_ss(String pk_corp, String[] ywybm, String billCode, String vouchid)
    throws SQLException, DbException
  {
    String cond = "";
    if ((vouchid != null) && (vouchid.trim().length() > 0)) {
      cond = " and arap_item_b.vouchid<>'" + vouchid + "'";
    }
    String ywy = "";
    for (int i = 0; i < ywybm.length; i++) {
      if (i == ywybm.length - 1) {
        ywy = ywy + "'" + ywybm[i] + "'";
      } else {
        ywy = ywy + "'" + ywybm[i] + "',";
      }
    }
    String sql = 
      "select  count(*) from arap_item inner join arap_item_b on arap_item_b.vouchid=arap_item.vouchid  where arap_item_b.ywybm in ( " + 
      

      ywy + 
      ")" + 
      " and arap_item.dr=0 and arap_item_b.ybye>0 and arap_item_b.dr=0 and djzt<5 and arap_item.djlxbm='" + 
      billCode + 
      "' and  arap_item.dwbm='" + 
      pk_corp + 
      "'" + 
      cond;
    
    boolean b = false;
    Integer ret = null;
    PersistenceManager pm = null;
    try
    {

        pm=PersistenceManager.getInstance(getds());
        ret=(Integer) pm.getJdbcSession().executeQuery(sql,new ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = 1635971580636118008L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                Integer i = null;
                if (rs.next()) {
                    // count :
                    i =new Integer( rs.getInt(1));
                }
                return i;
            }

        });

    	
    }
    finally
    {
      pm.release();
    }
    if (ret.intValue() > 0) {
      b = true;
    }
    return b;
  }
  
  public boolean existByBm(String billCode, String pk_corp)
    throws SQLException, DbException
  {
    String sql = "select  count(*) from arap_item where dr=0 and djlxbm='" + billCode + "' and  dwbm='" + pk_corp + "'";
    
    boolean b = false;
    Integer i = null;
    PersistenceManager pm = null;
    try
    {
        pm=PersistenceManager.getInstance(getds());
        i=(Integer)pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = 2244036624513966990L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (rs.next()) {
        			// count :
        			return new Integer(rs.getInt(1));
        		}
                return new Integer(0);
            }
         });
 		//
	
    }
    finally
    {
      pm.release();
    }
    if (i.intValue() > 0) {
      b = true;
    }
    return b;
  }
  
  public boolean existByKey(String key)
    throws SQLException, DbException
  {
    String sql = "select  count(*) from arap_djfb where dr=0 and item_bill_pk='" + key + "'";
    boolean b = false;
    
    PersistenceManager pm = null;
    Integer count;
    try
    {
        pm=PersistenceManager.getInstance(getds());
        count=(Integer)  pm.getJdbcSession().executeQuery(sql,new ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = -1955268192606058190L;

			public Object handleResultSet(ResultSet arg0) throws SQLException {
                //
                if (arg0.next()) {
        			// count :
        			return  new Integer ( arg0.getInt(1));
        		}
                return null;
            }
        });
    }
//        catch(SQLException e){
//        Logger.warn(e,this.getClass(),e.getMessage());
//        throw new BusinessRuntimeException(e.getMessage());
//    }
    finally
    {
//      Integer count;
      pm.release();
    }
    if ((count != null) && (count.intValue() > 0)) {
      b = true;
    }
    return b;
  }
  
  public DJZBVO[] findByPk_bankrecive(String key)
    throws DAOException, SQLException
  {
    DJZBVO[] vos = (DJZBVO[])null;
    
    DJZBHeaderVO[] headers = findHeaderByPk_bankrecive(key);
    Vector v = new Vector();
    if (headers != null) {
      for (int i = 0; i < headers.length; i++)
      {
        DJZBItemVO[] items = findItemsForHeader(headers[i].getPrimaryKey());
        DJZBVO vo = new DJZBVO();
        vo.setParentVO(headers[i]);
        vo.setChildrenVO(items);
        v.addElement(vo);
      }
    }
    if (v.size() > 0)
    {
      vos = new DJZBVO[v.size()];
      v.copyInto(vos);
    }
    return vos;
  }
  
  public DJZBVO findByPrimaryKey(String key)
    throws DAOException, SQLException
  {
    DJZBVO vo = new DJZBVO();
    
    DJZBHeaderVO header = findHeaderByPrimaryKey(key);
    DJZBItemVO[] items = (DJZBItemVO[])null;
    if (header != null)
    {
      items = findItemsForHeader(header.getPrimaryKey());
      for (int i = 0; i < items.length; i++)
      {
        DJFBItemVO[] frees = this.defdao.findItemsForHeader(items[i].getFb_oid());
        items[i].items = frees;
      }
    }
    else
    {
      return null;
    }
    vo.setParentVO(header);
    vo.setChildrenVO(items);
    
    return vo;
  }
  
  public DJZBVO[] findByPrimaryKeys(String[] keys)
    throws DAOException, SQLException
  {
    List ret = new ArrayList();
    
    int k = 0;
    for (int size = keys.length; k < size; k++)
    {
      DJZBVO vo = new DJZBVO();
      DJZBHeaderVO header = findHeaderByPrimaryKey(keys[k]);
      DJZBItemVO[] items = (DJZBItemVO[])null;
      if (header != null)
      {
        items = findItemsForHeader(header.getPrimaryKey());
        for (int i = 0; i < items.length; i++)
        {
          DJFBItemVO[] frees = this.defdao.findItemsForHeader(items[i].getFb_oid());
          items[i].items = frees;
        }
      }
      if (header != null)
      {
        vo.setParentVO(header);
        vo.setChildrenVO(items);
        ret.add(vo);
      }
    }
    return ret.size() == 0 ? null : (DJZBVO[])ret.toArray(new DJZBVO[0]);
  }
  
  public DJZBVO findByPrimaryKey_SS(String key)
    throws DAOException
  {
    DJZBVO vo = new DJZBVO();
    
    DJZBHeaderVO header = findHeaderByPrimaryKey_SS(key);
    DJZBItemVO[] items = (DJZBItemVO[])null;
    if (header != null) {
      items = findItemsForHeader_SS(header.getPrimaryKey());
    }
    vo.setParentVO(header);
    vo.setChildrenVO(items);
    




    return vo;
  }
  
  public DJZBItemVO[] finItem_Upgrade(String key)
    throws SQLException, DAOException
  {
    DJZBItemVO[] dJZBItems = (DJZBItemVO[])null;
    String strWhereClause = " where dr=0 and item_bill_pk is not null and  vouchid ='" + key + "' order by flbh";
    dJZBItems = getDJZBItemVOUniversalArray(strWhereClause);
    return dJZBItems;
  }
  
  public DJZBVO[] findDj_sell()
    throws ShenheException, DAOException, SQLException
  {
    String sqlFromClause = " from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid where  zb.dr=0 and fb.dr=0  and fb.ybye>0 and zb.djzt=2 and zb.djdl='sk'";
    




    DJZBHeaderVO[] djzbheader = (DJZBHeaderVO[])null;
    
    djzbheader = getDJZBHeaderVOsUniversalArray(sqlFromClause, false, null);
    Vector v = new Vector();
    for (int i = 0; (djzbheader != null) && (i < djzbheader.length); i++)
    {
      DJZBVO vo = new DJZBVO();
      vo.setParentVO(djzbheader[i]);
      DJZBItemVO[] items = (DJZBItemVO[])null;
      items = finItem_Upgrade(djzbheader[i].getVouchid());
      vo.setChildrenVO(items);
      v.addElement(vo);
    }
    DJZBVO[] vos = (DJZBVO[])null;
    if (v.size() > 0)
    {
      vos = new DJZBVO[v.size()];
      v.copyInto(vos);
    }
    return vos;
  }
  
  public DJZBVO[] findDjByPrimaryKeys(String[] keys)
    throws DbException, DAOException, SQLException
  {
    String cond = "";
    for (int i = 0; i < keys.length; i++)
    {
      cond = cond + "'" + keys[i] + "'";
      if (i != keys.length - 1) {
        cond = cond + ",";
      }
    }
    String sqlFromClause = " from arap_djzb zb where zb.dr=0 and zb.vouchid in ( " + 
      cond + 
      " )" + 
      " order by zb.vouchid";
    DJZBVO[] vos = (DJZBVO[])null;
    Vector v = new Vector();
    if (keys.length > 50)
    {
      String strTempTableName = createHeaderTempTable(keys);
      sqlFromClause = " from arap_djzb zb," + strTempTableName + " temp where zb.dr=0 and temp.vouchid_temp =zb.vouchid order by zb.vouchid";
    }
    v = getDJZBHeaderVOsUniversalVector(sqlFromClause, false, null);
    vos = getDjVObyHeaders(v);
    return vos;
  }
  
  public DJZBVO[] findDjByPrimaryKeys_SS(String[] keys)
    throws DAOException
  {
    String cond = "";
    for (int i = 0; i < keys.length; i++)
    {
      cond = cond + "'" + keys[i] + "'";
      if (i != keys.length - 1) {
        cond = cond + ",";
      }
    }
    String conds = "dr=0 and vouchid in ( " + cond + " )" + " order by vouchid";
    String conds1 = "dr=0 and vouchid in ( " + cond + " )" + " order by vouchid,flbh";
    List zb = (List)this.pubdao.queryVOsByWhereClause(DJZBHeaderVO.class, this.ssmeta, null, conds);
    List fb = (List)this.pubdao.queryVOsByWhereClause(DJZBItemVO.class, this.bbmeta, null, conds1);
    DJZBHeaderVO[] headers = (DJZBHeaderVO[])zb.toArray(new DJZBHeaderVO[0]);
    DJZBItemVO[] dJZBItems = (DJZBItemVO[])fb.toArray(new DJZBItemVO[0]);
    DJZBVO[] vos = (DJZBVO[])null;
    if ((headers != null) && 
      (headers.length > 0) && 
      (dJZBItems != null) && 
      (dJZBItems.length > 0))
    {
      vos = new DJZBVO[headers.length];
      int k = 0;
      for (int i = 0; i < headers.length; i++)
      {
        DJZBVO dj = new DJZBVO();
        dj.setParentVO(headers[i]);
        Vector items_temp = new Vector();
        for (int j = k; j < dJZBItems.length; j++) {
          if (headers[i].getVouchid().equals(dJZBItems[j].getVouchid()))
          {
            items_temp.addElement(dJZBItems[j]);
          }
          else
          {
            k = j;
            break;
          }
        }
        if (items_temp.size() > 0)
        {
          DJZBItemVO[] items = new DJZBItemVO[items_temp.size()];
          items_temp.copyInto(items);
          dj.setChildrenVO(items);
        }
        vos[i] = dj;
      }
    }
    return vos;
  }
  
  public DJZBItemVO findfengcunByKey(DJZBItemVO item)
    throws SQLException, DAOException
  {
    Arap_fengcunVOMeta meta = new Arap_fengcunVOMeta();
    ArapBaseMappingMeta defaultmeta = new ArapBaseMappingMeta();
    defaultmeta.setTabName(meta.getTableName());
    defaultmeta.setPk(meta.getPrimaryKey());
    defaultmeta.setAttributes(new String[] { "pk_arap_fengcun", "shenpi" });
    defaultmeta.setCols(new String[] { "pk_arap_fengcun", "shenpi" });
    defaultmeta.setDataTypes(meta.getDataTypesByAttrNames(new String[] { "pk_arap_fengcun", "shenpi" }));
    String cond = 
      "dr=0 and is_fc='N' and pk_curr='" + 
      item.getBzbm() + 
      "' and  pk_corp='" + 
      item.getDwbm() + 
      "' and pk_sz_item='" + 
      item.getSzxmid() + 
      "' and pk_dept='" + 
      item.getDeptid() + 
      "' and  jebegin<=" + 
      item.getJfybje() + 
      " and jeend>=" + 
      item.getJfybje();
    List ret = (List)this.pubdao.queryVOsByWhereClause(DJZBItemVO.class, defaultmeta, null, cond);
    if ((ret != null) && (ret.size() > 0))
    {
      DJZBItemVO it = (DJZBItemVO)ret.toArray(new DJZBItemVO[0])[0];
      item.setPk_arap_fengcun(it.getPk_arap_fengcun());
      item.setShenpi(it.getShenpi());
    }
    return item;
  }
  
  public DJZBHeaderVO[] findHeaderByPk_bankrecive(String key)
    throws DAOException, SQLException
  {
    DJZBHeaderVO[] dJZBHeaders = (DJZBHeaderVO[])null;
    boolean isUsedGL = false;
    String sqlFromClause = " from ARAP_DJZB zb inner join arap_djfb fb on zb.vouchid=fb.vouchid  where zb.dr=0 and fb.tbbh = '" + 
      key + "'";
    dJZBHeaders = getDJZBHeaderVOsUniversalArray(sqlFromClause, isUsedGL, null);
    



    return dJZBHeaders;
  }
  
  public DJZBHeaderVO findHeaderByPrimaryKey_SS(String key)
    throws DAOException
  {
    Collection col = this.basedao.retrieveByClause(DJZBHeaderVO.class, this.ssmeta, " dr=0 and vouchid =  '" + key + "'");
    return (DJZBHeaderVO)col.iterator().next();
  }
  
  public DJZBItemVO[] findItem_b(String key)
    throws DAOException
  {
    String sql = "select fb_oid,ybye,fbye,bbye from ARAP_item h left outer join arap_item_b b  on h.vouchid=b.vouchid " + 
      key;
    sql = sql + " and b.closer is null and b.dr=0 ";
    
    ArapBaseMappingMeta meta = new ArapBaseMappingMeta();
    meta.setTabName(this.fbmeta.getTableName());
    meta.setPk(this.fbmeta.getPrimaryKey());
    meta.setAttributes(new String[] { "fb_oid", "ybye", "fbye", "bbye" });
    meta.setDataTypes(this.fbmeta.getDataTypesByAttrNames(new String[] { "fb_oid", "ybye", "fbye", "bbye" }));
    List ret = (List)this.pubdao.queryVOsBySql(DJZBItemVO.class, meta, sql);
    return (DJZBItemVO[])ret.toArray(new DJZBItemVO[0]);
  }
  
  public DJZBItemVO findItemByPrimaryKey(String key)
    throws DAOException
  {
    if ((key == null) || (key.length() == 0)) {
      return null;
    }
    Vector v = findItemsByCondition(" where dr=0 and  fb_oid ='" + key + "'");
    if ((v != null) && (v.size() > 0)) {
      return (DJZBItemVO)v.elementAt(0);
    }
    return null;
  }
  
  public DJZBItemVO[] findItemsForHeader(String key)
    throws DAOException
  {
    if ((key == null) || (key.length() == 0)) {
      return null;
    }
    DJZBItemVO[] dJZBItems = (DJZBItemVO[])null;
    Vector v = findItemsByCondition(" where dr=0 and xgbh =" + ArapConstant.UNITACCOUNTSTAT_DEFAULT + " and vouchid = '" + key + "'");
    if (v.size() > 0)
    {
      dJZBItems = new DJZBItemVO[v.size()];
      v.copyInto(dJZBItems);
    }
    return dJZBItems;
  }
  
  public DJZBItemVO[] findItemsForHeader_Hz(String key, DjCondVO djcond)
    throws DAOException
  {
    String cond_wldx = "";
    if (djcond.m_Wldx != null) {
      cond_wldx = " and wldx=" + djcond.m_Wldx.intValue();
    }
    String cond_ksbm_cl = "";
    if ((djcond.m_Ksbm_cl != null) && (djcond.m_Ksbm_cl.trim().length() > 0)) {
      cond_ksbm_cl = " and ksbm_cl='" + djcond.m_Ksbm_cl + "'";
    }
    String cond_ywybm = "";
    if ((djcond.m_ywybm != null) && (djcond.m_ywybm.trim().length() > 0)) {
      cond_ywybm = " and ywybm='" + djcond.m_ywybm + "'";
    }
    String cond_dept = "";
    if ((djcond.m_deptid != null) && (djcond.m_deptid.trim().length() > 0)) {
      cond_dept = " and deptid='" + djcond.m_deptid + "'";
    }
    String cond_curr = "";
    if ((djcond.m_Bz != null) && (djcond.m_Bz.trim().length() > 0)) {
      cond_curr = " and bzbm='" + djcond.m_Bz + "'";
    }
    String sql = " where dr=0 and ybye>0 and  vouchid = '" + 
      key + "'" + 
      cond_wldx + 
      cond_ksbm_cl + 
      cond_ywybm + 
      cond_dept + 
      cond_curr;
    
    DJZBItemVO[] dJZBItems = (DJZBItemVO[])null;
    Vector v = findItemsByCondition(sql);
    if (v.size() > 0)
    {
      dJZBItems = new DJZBItemVO[v.size()];
      v.copyInto(dJZBItems);
    }
    return dJZBItems;
  }
  
  public DJZBItemVO[] findItemsForHeader_SS(String key)
    throws DAOException
  {
    Collection col = this.basedao.retrieveByClause(DJZBItemVO.class, this.bbmeta, "dr=0  and  vouchid =  '" + key + "'order by flbh");
    return (DJZBItemVO[])col.toArray(new DJZBItemVO[0]);
  }
  
  public DJZBItemVO[] findItemsForHeader_SS4(String key)
    throws DAOException
  {
    Collection col = this.basedao.retrieveByClause(DJZBItemVO.class, this.bbmeta, "dr=0  and closer is null and  vouchid =  '" + key + "'order by flbh");
    return (DJZBItemVO[])col.toArray(new DJZBItemVO[0]);
  }
  
  public DJZBItemVO[] findItemsForHeader_SS_4(String key)
    throws DAOException
  {
    Collection col = this.basedao.retrieveByClause(DJZBItemVO.class, this.bbmeta, "ybye>0 and dr=0  and closer is null and  vouchid = '" + key + "'order by flbh");
    return (DJZBItemVO[])col.toArray(new DJZBItemVO[0]);
  }
  
  public int findMatchingSSItem(String key)
    throws SQLException, DbException
  {
    String sql = "select count(b.fb_oid) from ARAP_item h left outer join arap_item_b b  on h.vouchid=b.vouchid " + 
    
      key;
    sql = sql + " and b.closer is null and b.dr=0 ";
    
    Integer intSSItemCount = null;
    PersistenceManager pm = null;
    try
    {
        pm=PersistenceManager.getInstance(getds());
        intSSItemCount=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = 5278474825149770699L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (null!=rs&&rs.next()) {
          			 return new Integer( rs.getInt(1));
          		}
                return new Integer(0);
            }
        });
 }
    finally
    {
      pm.release();
    }
    return intSSItemCount.intValue();
  }
  
  public String getAreaNameByCode(String area_code)
    throws SQLException, DbException
  {
    String sql = "select areaname from bd_bankarea where dr=0 and areacode='" + area_code + "'";
    String area_name = "";
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(getds());
      area_name=(String) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

          /**
			 *
			 */
			private static final long serialVersionUID = 9177635729896443485L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
              //
              if (rs.next()) {
      			// count :
      			return rs.getString("areaname");
      		}
              return "";
          }

      });

  	
    }
    finally
    {
      pm.release();
    }
    return area_name;
  }
  
  public Integer getCheckflag(String vouchid)
    throws SQLException, DbException
  {
    String sql = "select  count(*) from arap_djfb where dr=0 and checkflag>0 and vouchid ='" + vouchid + "'";
    Integer checkflag = null;
    PersistenceManager pm = null;
    try
    {
    	pm=PersistenceManager.getInstance(getds());
        checkflag=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = 5117431304740801362L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (null!=rs&&rs.next()) {
        			// count :
        			return (Integer)rs.getObject(1);
        		}
                return null;
            }

        });
    	
    }
    finally
    {
      pm.release();
    }
    return checkflag;
  }
  
  public Integer getClbzByPkey(String key)
    throws SQLException, DbException
  {
    Integer clbz = null;
    String sql = "select clbz from arap_djclb where dr=0 and vouchid='" + key + "'";
    PersistenceManager pm = null;
    try
    {
        pm=PersistenceManager.getInstance(getds());
        clbz=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){
            /**
			 *
			 */
			private static final long serialVersionUID = 5164862123033678514L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (null!=rs&&rs.next()) {
        			// clbz :
        			return  (Integer)rs.getObject(1);
        		}
                return null;
            }

        });

    	
    }
    finally
    {
      pm.release();
    }
    return clbz;
  }
  
  public Integer getCountByDjbh(String djbh, String pk_corp)
    throws SQLException, DbException
  {
    String sql = "select  count(*) from arap_djzb where dr=0 and (dwbm='" + pk_corp + "') and djbh = '" + djbh + "'";
    Integer count = null;
    PersistenceManager pm = null;
    try
    {

        pm=PersistenceManager.getInstance(getds());
         count=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (rs.next()) {
        			// count :
        			return (Integer)rs.getObject(1);
        		}
                return null;
            }

        });

    	
    }
    finally
    {
      pm.release();
    }
    return count;
  }
  
  public Integer getCountByZyx19(String zyx19)
    throws SQLException, DbException
  {
    String sql = "select  count(*) from arap_djzb where dr=0  and item_bill_pk = '" + zyx19 + "'";
    Integer count = null;
    PersistenceManager pm = null;
    try
    {
    	 pm=PersistenceManager.getInstance(getds());
         count=(Integer) pm.getJdbcSession().executeQuery(sql,new ResultSetProcessor(){

             /**
				 *
				 */
				private static final long serialVersionUID = 6146862989116928453L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                 //
                 if (rs.next()) {
         			// count :
         			return  (Integer)rs.getObject(1);
         		}
                 return null;
             }

         });

     	
    }
    finally
    {
      pm.release();
    }
    return count;
  }
  
  public String[] getFreePropertys(String m_szxmid)
    throws SQLException, DbException
  {
    String sql = "";
    if ((m_szxmid == null) || (m_szxmid.trim().length() < 1)) {
      return null;
    }
    sql = 
    
      "select free1,free2,free3,free4,free5,free6,free7,free8,free9,free10 from bd_costsubj  where dr=0 and pk_costsubj= '" + m_szxmid + "'";
    
    PersistenceManager pm = null;
    try
    {
    	pm=PersistenceManager.getInstance(getds());
        return (String[]) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

           /**
			 *
			 */
			private static final long serialVersionUID = 3304006085908676712L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
               //
               String[] frees = new String[10];
               while (rs.next()) {

        			//自由项1
        			String free1 = rs.getString("free1");
        			if (free1 != null)
        				frees[0] = free1;

        			//自由项2
        			String free2 = rs.getString("free2");
        			if (free2 != null)
        				frees[1] = free2;

        			//自由项3
        			String free3= rs.getString("free3");
        			if (free3 != null)
        				frees[2] = free3;

        			//自由项4
        			String free4 = rs.getString("free4");
        			if (free4 != null)
        				frees[3] = free4;

        			//自由项5
        			String free5 = rs.getString("free5");
        			if (free5 != null)
        				frees[4] = free5;

        			//自由项6
        			String free6 = rs.getString("free6");
        			if (free6 != null)
        				frees[5] = free6;

        			//自由项7
        			String free7 = rs.getString("free7");
        			if (free7 != null)
        				frees[6] = free7;

        			//自由项8
        			String free8 = rs.getString("free8");
        			if (free8 != null)
        				frees[7] = free8;

        			//自由项9
        			String free9 = rs.getString("free9");
        			if (free9 != null)
        				frees[8] = free9;

        				//自由项10
        			String free10 = rs.getString("free10");
        			if (free10 != null)
        				frees[9] = free10;
        		}
               return frees;
           }

        });

    	
    }
    finally
    {
      pm.release();
    }
  }
  
  public String getOfficialprintuser(String key)
    throws SQLException, DbException
  {
    String sql = "select Officialprintuser from ARAP_DJZB where  vouchid = '" + key + "'";
    String Officialprintuser = null;
    PersistenceManager pm = null;
    try
    {
    	pm=PersistenceManager.getInstance(getds());
        Officialprintuser=(String) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

           /**
			 *
			 */
			private static final long serialVersionUID = 7691733286799842695L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
               //
               if (null!=rs&&rs.next()) {
        			return  rs.getString(1);
        		}
               return null;
           }

        });

    	
    }
    finally
    {
      pm.release();
    }
    return Officialprintuser;
  }
  
  public String getSPZTByPk(String tablename, String key)
    throws SQLException, DbException
  {
    String sql = "select spzt from  " + tablename + " where dr=0 and vouchid = '" + key + "'";
    String spzt = null;
    PersistenceManager pm = null;
    try
    {
    	pm=PersistenceManager.getInstance(getds());
        spzt=(String) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = 8133087689358276393L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (rs.next()) {
        			// spzt :
        			return  rs.getString(1);
        		}
                return null;
            }

        });

    	
    }
    finally
    {
      pm.release();
    }
    return spzt;
  }
  
  public String getTsByPrimaryKey(String key, String tableName)
    throws SQLException, DbException
  {
    String ts = null;
    String sql = "select  ts from  " + tableName + "  where   vouchid ='" + key + "'";
    PersistenceManager pm = null;
    try
    {

        pm=PersistenceManager.getInstance(getds());
        ts=(String)pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor()
                {
                    /**
					 *
					 */
					private static final long serialVersionUID = 4314866937739791621L;

					public Object handleResultSet(ResultSet arg0) throws SQLException {
                        //
                        if (null!=arg0&&arg0.next()) {
                			return  arg0.getString(1);
                		}
                        return null;
                    }
                });
    	
    }
    finally
    {
      pm.release();
    }
    return ts;
  }
  
  public Integer getXTCountBYpk(String vouchid)
    throws SQLException, DbException
  {
    String sql = "select  count(*) from arap_djzb inner join arap_djfb on arap_djfb.vouchid=arap_djzb.vouchid where arap_djzb.dr=0 and arap_djfb.ddlx='" + vouchid + "'  and arap_djzb.djzt > 0 and arap_djzb.lybz=9 ";
    Integer count = null;
    PersistenceManager pm = null;
    try
    {
    	pm=PersistenceManager.getInstance(getds());
        count=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = 7295881919604135476L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (rs.next()) {
        			// count :
        			return (Integer)rs.getObject(1);
        		}
                return null;
            }

        });

    	
    }
    finally
    {
      pm.release();
    }
    return count;
  }
  
  public boolean getXTCountBYPkandCorp(String vouchid, String corp)
    throws SQLException, DbException
  {
    String sql = "select  count(*) from arap_djzb inner join arap_djfb on arap_djfb.vouchid=arap_djzb.vouchid where arap_djzb.dr=0 and arap_djfb.ddlx='" + vouchid + "' and arap_djzb.lybz=9 and arap_djzb.dwbm= '" + corp + "'";
    PersistenceManager pm = null;
    Integer count = null;
    try
    {

        pm=PersistenceManager.getInstance(getds());
        count=(Integer) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = 5942145008916976253L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (null!=rs&&rs.next()) {
        			// count :
        			return  (Integer)rs.getObject(1);
        		}
                return null;
            }

        });
		//

	
    }
    finally
    {
      pm.release();
    }
    if ((count == null) || (count.intValue() != 0)) {
      return true;
    }
    return false;
  }
  
  public List getXtMsgBypk(String vouchid)
    throws SQLException, DbException
  {
    String sql = "select  corp.unitname, zb.vouchid from arap_djzb zb inner join arap_djfb fb on fb.vouchid= zb.vouchid left outer join bd_corp corp on zb.dwbm=corp.pk_corp where  zb.dr=0 and  fb.ddlx='" + vouchid + "' and  zb.lybz=9 ";
    PersistenceManager pm = null;
    try
    {
    	pm=PersistenceManager.getInstance(getds());
        return (List) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = -2084283254540215529L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                List<String[]> retLst=new ArrayList<String[]>();
            	List<String> temp=new ArrayList<String>();
                while (rs.next()) {
        		    String[] str=new String[2];
        		    str[0]=rs.getString(1);
        		    str[1]=rs.getString(2);
        		    if(!temp.contains(str[1])){
        				retLst.add(str);
        				temp.add(str[1]);
        		    }
        		}
                return retLst;
            }

        });


	
    }
    finally
    {
      pm.release();
    }
  }
  
  public UFDouble getYbyeByKey(String key)
    throws SQLException, DbException
  {
    String sql = "select sum(b.jfybje+b.dfybje) from ARAP_DJzb h left outer join arap_djfb b  on h.vouchid=b.vouchid " + 
      key;
    UFDouble ybye = null;
    PersistenceManager pm = null;
    try
    {

        pm=PersistenceManager.getInstance(getds());
        ybye=(UFDouble) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = -7140665989863356645L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (rs.next()) {
                    BigDecimal ybye_t = (BigDecimal)rs.getBigDecimal(1);
        			return ybye_t == null ? null : new UFDouble(ybye_t);
        		}
                return null;
            }

        });

    	
    }
    finally
    {
      pm.release();
    }
    return ybye;
  }
  
  public UFDouble getYbyeByKey_SS(String key)
    throws SQLException, DbException
  {
    String sql = "select sum(b.ybye) from ARAP_item h left outer join arap_item_b b  on h.vouchid=b.vouchid " + 
      key;
    sql = sql + " and b.closer is null and b.dr=0 ";
    UFDouble ybye = null;
    PersistenceManager pm = null;
    try
    {
    	 pm=PersistenceManager.getInstance(getds());
         ybye=(UFDouble) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

             /**
				 *
				 */
				private static final long serialVersionUID = -2543580326762414150L;

				public Object handleResultSet(ResultSet rs) throws SQLException {
                 //
                 if (rs.next()) {
                     BigDecimal ybye_t = (BigDecimal)rs.getBigDecimal(1);
         			return ybye_t == null ? null : new UFDouble(ybye_t);
         		}
                 return null;
             }

         });

     	
    }
    finally
    {
      pm.release();
    }
    return ybye;
  }
  
  public DJZBVO insert(DJZBVO vo)
    throws DAOException
  {
    convertDJZBVO(vo);
    DJZBHeaderVO head = (DJZBHeaderVO)vo.getParentVO();
    head = insertHeader(head);
    vo.setParentVO(head);
    
    DJZBItemVO[] items = (DJZBItemVO[])vo.getChildrenVO();
    items = insertItems(items, head);
    vo.setChildrenVO(items);
    return vo;
  }
  
  public DJZBVO insert_SS(DJZBVO vo)
    throws DAOException
  {
    convertDJZBVO(vo);
    DJZBHeaderVO head = (DJZBHeaderVO)vo.getParentVO();
    head = insertHeader_SS(head);
    String key = head.getPrimaryKey();
    vo.setParentVO(head);
    
    DJZBItemVO[] items = (DJZBItemVO[])vo.getChildrenVO();
    for (int i = 0; i < items.length; i++)
    {
      items[i].setDjbh(head.getDjbh());
      insertItem_SS(items[i], key);
      items[i].setStatus(1);
    }
    return vo;
  }
  
  public static void convertDJZBVO(DJZBVO vo)
  {
    convertHeaderVO((DJZBHeaderVO)vo.getParentVO());
    DJZBItemVO[] items = (DJZBItemVO[])vo.getChildrenVO();
    if (items != null)
    {
      int i = 0;
      for (int size = items.length; i < size; i++) {
        DjVOTreaterAid.convertItemVO(items[i]);
      }
    }
  }
  
  public static void convertHeaderVO(DJZBHeaderVO clone)
  {
    if (clone.getDr() == null) {
      clone.setDr(new Integer(0));
    }
    if (clone.getZzzt() == null) {
      clone.setZzzt(new Integer(0));
    }
    if (clone.getSxbz() == null) {
      clone.setSxbz(new Integer(0));
    }
    UFDateTime ts = new UFDateTime(CurTime.getCurrentTimeStampString());
    clone.setTs(ts);
  }
  
  private DJZBHeaderVO insertHeader(DJZBHeaderVO dJZBHeader)
    throws DAOException
  {
    String key = this.basedao.insertObject(dJZBHeader, this.zbmeta);
    dJZBHeader.setPrimaryKey(key);
    return dJZBHeader;
  }
  
  private DJZBHeaderVO insertHeader_SS(DJZBHeaderVO dJZBHeader)
    throws DAOException
  {
    String key = this.basedao.insertObject(dJZBHeader, this.ssmeta);
    dJZBHeader.setPrimaryKey(key);
    return dJZBHeader;
  }
  
  private String insertItem(DJZBItemVO dJZBItem)
    throws DAOException
  {
    if (dJZBItem == null) {
      return null;
    }
    DJZBItemVO item = insertItems(new DJZBItemVO[] { dJZBItem })[0];
    String key = item.getFb_oid();
    
    DjfkxybVO[] fkxybs = dJZBItem.fkxyvos;
    if (fkxybs != null) {
      for (int i = 0; i < fkxybs.length; i++) {
        if (fkxybs[i] != null)
        {
          fkxybs[i].setVouchid(dJZBItem.getVouchid());
          fkxybs[i].setFb_oid(key);
          fkxybs[i].setPrimaryKey(this.fkdao.insert(new DjfkxybVO[] { fkxybs[i] })[0]);
        }
      }
    }
    return key;
  }
  
  private String insertItem_SS(DJZBItemVO dJZBItem)
    throws DAOException
  {
    String key = this.basedao.insertObject(dJZBItem, this.bbmeta);
    dJZBItem.setPrimaryKey(key);
    return key;
  }
  
  private String insertItem_SS(DJZBItemVO dJZBItem, String foreignKey)
    throws DAOException
  {
    dJZBItem.setVouchid(foreignKey);
    String key = insertItem_SS(dJZBItem);
    
    DJFBItemVO[] frees = dJZBItem.items;
    if (frees != null) {
      for (int i = 0; i < frees.length; i++)
      {
        frees[i].setVouchid(foreignKey);
        frees[i].setFb_oid(key);
        this.defdao.insertFree(frees[i]);
      }
    }
    return key;
  }
  
  private DJZBItemVO[] insertItems(DJZBItemVO[] dJZBItems)
    throws DAOException
  {
    String[] keys = this.basedao.insertObject(dJZBItems, this.fbmeta);
    int i = 0;
    for (int size = keys.length; i < size; i++) {
      dJZBItems[i].setPrimaryKey(keys[i]);
    }
    return dJZBItems;
  }
  
  private DJZBItemVO[] insertItems(DJZBItemVO[] dJZBItems, DJZBHeaderVO head)
    throws DAOException
  {
    if (dJZBItems == null) {
      return null;
    }
    Vector vFrees = new Vector();
    Vector vFkxy = new Vector();
    for (int i = 0; i < dJZBItems.length; i++)
    {
      dJZBItems[i].setDjbh(head.getDjbh());
      dJZBItems[i].setVouchid(head.getVouchid());
    }
    DJZBItemVO[] items = insertItems(dJZBItems);
    
    DJFBItemVO[] frees = (DJFBItemVO[])null;
    DjfkxybVO[] fkxybs = (DjfkxybVO[])null;
    for (int k = 0; k < items.length; k++)
    {
      items[k].setStatus(1);
      
      frees = items[k].items;
      if (frees != null) {
        for (int i = 0; i < frees.length; i++)
        {
          frees[i].setVouchid(head.getVouchid());
          frees[i].setFb_oid(items[k].getFb_oid());
          
          vFrees.addElement(frees[i]);
        }
      }
      fkxybs = items[k].fkxyvos;
      if (fkxybs != null) {
        for (int i = 0; i < fkxybs.length; i++) {
          if (fkxybs[i] != null)
          {
            fkxybs[i].setVouchid(head.getVouchid());
            fkxybs[i].setFb_oid(items[k].getFb_oid());
            vFkxy.addElement(fkxybs[i]);
          }
        }
      }
    }
    if (vFrees != null)
    {
      frees = new DJFBItemVO[vFrees.size()];
      vFrees.copyInto(frees);
      this.defdao.insertFrees(frees);
    }
    if (vFkxy != null)
    {
      fkxybs = new DjfkxybVO[vFkxy.size()];
      vFkxy.copyInto(fkxybs);
      this.fkdao.insert(fkxybs);
    }
    return items;
  }
  
  public DJZBItemVO[] insertItemsForBZ(DJZBItemVO[] dJZBItems)
    throws DAOException
  {
    if (dJZBItems == null) {
      return null;
    }
    Vector vFrees = new Vector();
    Vector vFkxy = new Vector();
    DJZBItemVO[] items = insertItems(dJZBItems);
    
    DJFBItemVO[] frees = (DJFBItemVO[])null;
    DjfkxybVO[] fkxybs = (DjfkxybVO[])null;
    for (int k = 0; k < items.length; k++)
    {
      frees = items[k].items;
      if (frees != null) {
        for (int i = 0; i < frees.length; i++)
        {
          frees[i].setVouchid(items[k].getVouchid());
          frees[i].setFb_oid(items[k].getFb_oid());
          
          vFrees.addElement(frees[i]);
        }
      }
      fkxybs = items[k].fkxyvos;
      if (fkxybs != null) {
        for (int i = 0; i < fkxybs.length; i++) {
          if (fkxybs[i] != null)
          {
            fkxybs[i].setVouchid(items[k].getVouchid());
            fkxybs[i].setFb_oid(items[k].getFb_oid());
            vFkxy.addElement(fkxybs[i]);
          }
        }
      }
    }
    if (vFrees != null)
    {
      frees = new DJFBItemVO[vFrees.size()];
      vFrees.copyInto(frees);
      this.defdao.insertFrees(frees);
    }
    if (vFkxy != null)
    {
      fkxybs = new DjfkxybVO[vFkxy.size()];
      vFkxy.copyInto(fkxybs);
      this.fkdao.insert(fkxybs);
    }
    return items;
  }
  
  public boolean officialPrint(DJZBHeaderVO dJZBHeader)
    throws DAOException
  {
    this.pubdao.updateObjectPartly(dJZBHeader, this.zbmeta, new String[] { "officialprintuser", "officialprintdate" });
    return true;
  }
  
  public String pausetransact(String key, String pausetransact)
    throws DAOException
  {
    DJZBItemVO item = new DJZBItemVO();
    item.setPrimaryKey(key);
    item.setPausetransact(new UFBoolean(pausetransact.substring(0, 1)));
    this.pubdao.updateObjectPartly(item, this.fbmeta, new String[] { "pausetransact" });
    
    String strCurrentTS = CurTime.getCurrentTimeStampString();
    DJZBHeaderVO header = new DJZBHeaderVO();
    header.setTs(new UFDateTime(strCurrentTS));
    String cond = "where vouchid in (select vouchid from arap_djfb where fb_oid='" + key + "') and ts='" + pausetransact.substring(1) + "'";
    this.pubdao.updateObjectPartly(header, this.zbmeta, new String[] { "ts" }, cond);
    
    return strCurrentTS;
  }
  
  public DJZBVO[] queryDjAll_Hz(DjCondVO djcond)
    throws Exception
  {
    DJZBVO vo = new DJZBVO();
    DJZBVO[] vos = (DJZBVO[])null;
    DJZBHeaderVO[] headers = (DJZBHeaderVO[])null;
    DJZBHeaderVO header = null;
    String key = djcond.getSqlWhere();
    
    headers = queryHead_hz(key, true);
    if ((headers == null) || (headers.length < 1)) {
      return null;
    }
    vos = new DJZBVO[headers.length];
    
    DJZBItemVO[] items = (DJZBItemVO[])null;
    for (int j = 0; j < headers.length; j++)
    {
      vo = new DJZBVO();
      header = new DJZBHeaderVO();
      header = headers[j];
      if ((header != null) && (header.getPrimaryKey() != null)) {
        items = findItemsForHeader_Hz(header.getPrimaryKey(), djcond);
      }
      for (int i = 0; i < items.length; i++)
      {
        DJFBItemVO[] frees = this.defdao.findItemsForHeader(items[i].getFb_oid());
        items[i].items = frees;
      }
      vo.setParentVO(header);
      vo.setChildrenVO(items);
      vos[j] = vo;
    }
    return vos;
  }
  
  public Vector queryDjLb_djcond(Integer initPos, Integer count, DjCondVO djcond)
    throws SQLException, Exception
  {
    int syscode = djcond.syscode;
    QryCondArrayVO[] norCondVos = djcond.m_NorCondVos;
    ConditionVO[] defCondVos = djcond.m_DefCondVos;
    String zgyfWhere = "";
    if (djcond.syscode == 2)
    {
      int zgyf = 0;
      int i = 0;
      for (int size = defCondVos.length; i < size; i++) {
        if (("zb.zgyf".equalsIgnoreCase(defCondVos[i].getFieldCode())) && (defCondVos[i].getValue() != null) && (defCondVos[i].getValue().length() > 0))
        {
          if (Integer.valueOf(defCondVos[i].getValue()).intValue() > 2) {
            zgyf = 1;
          } else {
            zgyf = 2;
          }
          defCondVos[i].setValue(String.valueOf(Integer.valueOf(defCondVos[i].getValue()).intValue() % 3));
        }
      }
      if (zgyf == 1) {
        zgyfWhere = "and zb.djdl ='ys'";
      } else if (zgyf == 2) {
        zgyfWhere = "and zb.djdl ='yf'";
      }
    }
    String condLybz = "";
    if (syscode == -999) {
      condLybz = "   and zb.pzglh=2";
    } else if (syscode == -9) {
      condLybz = "  and zb.pzglh=0";
    } else if (syscode == -99) {
      condLybz = "  and zb.pzglh=1";
    }
    String sqlFromClause = null;
    

    sqlFromClause = " from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid" + 
      new PubMethods().getBillQuerySubSql_Dj(
      norCondVos, 
      defCondVos, 
      djcond.pk_corp, 
      djcond.operator) + 
      
      " and fb.dr=0 " + 
      condLybz + 
      zgyfWhere;
    


















    sqlFromClause = sqlFromClause + " order by zb.djrq,zb.djlxbm,  zb.djbh ";
    Vector djzbheader = null;
    djzbheader = getDJZBHeaderVOsUniversalVector(initPos, count, sqlFromClause, djcond.isLinkPz, djcond.VoucherFlag);
    return djzbheader;
  }
  
  public Vector queryDjLb_Yhrq(Integer initPos, Integer count, DjCondVO djcond)
    throws DAOException, SQLException
  {
    QryCondArrayVO[] norCondVos = djcond.m_NorCondVos;
    ConditionVO[] defCondVos = djcond.m_DefCondVos;
    String strPKCorp = djcond.m_Dwbm;
    String strPKUser = djcond.operator;
    String sqlFromClause = " from arap_djzb zb inner join arap_djfb on arap_djfb.vouchid =zb.vouchid  inner join arap_djlx on arap_djlx.djlxoid=zb.ywbm and arap_djlx.isqr='Y'  where zb.dr=0 and zb.vouchid in " + 
    


      PubMethods.getBillQuerySubSql1(norCondVos, defCondVos, strPKCorp, strPKUser) + 
      " and COALESCE(zb.hzbz,' ')<>'1'" + 
      " and COALESCE(zb.hzbz,' ')<>'0'" + 
      


      " and (djzt=2 or djzt=3)";
    




    sqlFromClause = sqlFromClause + " order by  zb.djrq, zb.djlxbm,zb.djbh ";
    Vector djzbheader = null;
    djzbheader = getDJZBHeaderVOsUniversalVector(initPos, count, sqlFromClause, djcond.isLinkPz, djcond.VoucherFlag);
    return djzbheader;
  }
  
  public Vector queryDjLbQ_djcond(Integer initPos, Integer count, DjCondVO djcond)
    throws SQLException, Exception
  {
    QryCondArrayVO[] norCondVos = djcond.m_NorCondVos;
    ConditionVO[] defCondVos = djcond.m_DefCondVos;
    Vector vHead = new Vector();
    String sqlFromClause = null;
    
    sqlFromClause = " from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid";
    










    String strSql = null;
    PubMethods pubmethods = new PubMethods();
    
    String zgyfWhere = "";
    int zgyf = 0;
    int i = 0;
    for (int size = defCondVos.length; i < size; i++) {
      if (("zb.zgyf".equalsIgnoreCase(defCondVos[i].getFieldCode())) && (defCondVos[i].getValue() != null) && (defCondVos[i].getValue().length() > 0))
      {
        if (Integer.valueOf(defCondVos[i].getValue()).intValue() > 2) {
          zgyf = 1;
        } else {
          zgyf = 2;
        }
        defCondVos[i].setValue(String.valueOf(Integer.valueOf(defCondVos[i].getValue()).intValue() % 3));
      }
    }
    if (zgyf == 1) {
      zgyfWhere = "and zb.djdl ='ys'";
    } else if (zgyf == 2) {
      zgyfWhere = "and zb.djdl ='yf'";
    }
    CreatJoinSQLTool jointool = new CreatJoinSQLTool();
    if (jointool.checkLegal(djcond.pk_corp))
    {
      strSql = sqlFromClause + pubmethods.getBillQuerySubSql_Dj(norCondVos, defCondVos, djcond.pk_corp, djcond.operator) + zgyfWhere;
      


      strSql = strSql + " order by zb.djlxbm, zb.djrq, zb.djbh ";
      vHead = getDJZBHeaderVOsUniversalVector(initPos, count, strSql, djcond.isLinkPz, djcond.VoucherFlag);
    }
    else
    {
      QryCondArrayVO condCorp = pubmethods.findCorpCond(norCondVos);
      String[] pk_corps = djcond.pk_corp;
      if (condCorp != null) {
        condCorp.setItems(new QryCondVO[] { condCorp.getItems()[0] });
      }
      for (int i1 = 0; i1 < pk_corps.length; i1++)
      {
        if (condCorp != null) {
          condCorp.getItems()[0].setValue(pk_corps[i1]);
        }
        strSql = sqlFromClause + pubmethods.getBillQuerySubSql_Dj(norCondVos, defCondVos, new String[] { pk_corps[i1] }, djcond.operator) + zgyfWhere;
        


        strSql = strSql + " order by zb.djlxbm, zb.djrq, zb.djbh ";
        Vector vTemp = getDJZBHeaderVOsUniversalVector(initPos, count, strSql, djcond.isLinkPz, djcond.VoucherFlag);
        vHead.addAll(vTemp);
      }
    }
    return vHead;
  }
  
  public Vector queryDjLbQ_Wszz(Integer initPos, Integer count, QryCondArrayVO[] norCondVos, ConditionVO[] defCondVos, boolean isCHz, RefResultVO[] refs, String pk_corp, String cond)
    throws DAOException, SQLException
  {
    String condNHzfs = "";String condNHzsh = "";
    if (!isCHz)
    {
      condNHzfs = " and COALESCE(zb.hzbz,' ')<>'1'";
      condNHzsh = " and COALESCE(zb.hzbz,' ')<>'0'";
    }
    String condDwbm = "";
    String sqlFromClause = " from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid" + 
    
      PubMethods.getBillQuerySubSql_Dj(norCondVos, defCondVos) + 
      cond + 
      condNHzfs + 
      condNHzsh + 
      condDwbm + 
      
      " and fb.xgbh =" + ArapConstant.UNITACCOUNTSTAT_DEFAULT;
    Vector djzbheader = null;
    sqlFromClause = sqlFromClause + " order by zb.djrq,zb.djlxbm,  zb.djbh ";
    djzbheader = getDJZBHeaderVOsUniversalVector(initPos, count, sqlFromClause, false, null);
    return djzbheader;
  }
  
  public DJZBHeaderVO[] queryHead(String key)
    throws DAOException, SQLException
  {
    String where = StringUtil.replaceIgnoreCase(key, "arap_djzb.", "zb.");
    String sqlFromClause = null;
    sqlFromClause = " from ARAP_DJZB zb " + where + " ";
    DJZBHeaderVO[] djzbheader = (DJZBHeaderVO[])null;
    djzbheader = getDJZBHeaderVOsUniversalArray(sqlFromClause, false, null);
    return djzbheader;
  }
  
  public DJZBHeaderVO[] queryHeadbyTS(String key)
    throws DAOException, SQLException
  {
    String sqlFromClause = key;
    DJZBHeaderVO[] djzbheader = (DJZBHeaderVO[])null;
    djzbheader = getDJZBHeaderVOsUniversalArray(sqlFromClause, false, null);
    return djzbheader;
  }
  
  public void setBankRecivePk(String fboid, String pk_bankrecive, String ts)
    throws SQLException, BusinessException, DbException
  {
    DJZBItemVO item = new DJZBItemVO();
    item.setPrimaryKey(fboid);
    if (pk_bankrecive != null) {
      item.setTbbh(pk_bankrecive);
    }
    String cond = " fb_oid = '" + fboid + "' and dr = 0 and ts = '" + ts + "'";
    this.pubdao.updateObjectPartly(item, this.fbmeta, new String[] { "tbbh" }, cond);
    String sql = "update arap_djzb set zyx1 = zyx1 where vouchid in (select vouchid from arap_djfb where fb_oid = '" + fboid + "')";
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(getds());
      pm.getJdbcSession().executeUpdate(sql);
    }
    finally
    {
      pm.release();
    }
  }
  
  public void setOtherSysFlag(String fb_oid, String othersysflag, UFBoolean pausetransact)
    throws DAOException, DbException
  {
    DJZBItemVO item = new DJZBItemVO();
    item.setPrimaryKey(fb_oid);
    item.setOthersysflag(othersysflag);
    item.setPausetransact(pausetransact);
    this.pubdao.updateObjectPartly(item, this.fbmeta, new String[] { "othersysflag", "pausetransact" });
    
    String sqlHead = "update arap_djzb set dr=dr where vouchid = (select vouchid from arap_djfb where fb_oid='" + fb_oid + "')";
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(getds());
      pm.getJdbcSession().executeUpdate(sqlHead);
    }
    finally
    {
      pm.release();
    }
  }
  
  public DJZBVO update(DJZBVO vo)
    throws SQLException, BusinessException, SystemException, Exception
  {
    convertDJZBVO(vo);
    

    ArapClassRunBO executeBo = new ArapClassRunBO();
    DJZBItemVO[] items = (DJZBItemVO[])vo.getChildrenVO();
    DJZBItemVO[] newItems = (DJZBItemVO[])null;
    String itemkey = "";
    int delCount = 0;int k = 0;
    for (int i = 0; i < items.length; i++) {
      switch (items[i].getStatus())
      {
      case 2: 
        itemkey = insertItem(items[i]);
        items[i].setPrimaryKey(itemkey);
        items[i].setStatus(1);
        break;
      case 1: 
        updateItem(items[i]);
        break;
      case 3: 
        deleteItem(items[i]);
        
        PubBO pub = new PubBO();
        if (pub.costIsUsed())
        {
          Class[] paramtype = { String.class, String.class };
          Object[] param = { items[i].getVouchid(), items[i].getFb_oid() };
          executeBo.runMethod("nc.bs.bank.costContent.CostcontentDMO", "delete", paramtype, param);
        }
        delCount++;
      }
    }
    DJZBHeaderVO head = (DJZBHeaderVO)vo.getParentVO();
    head = updateHeader(head);
    String ts = getTsByPrimaryKey(head.getPrimaryKey(), "arap_djzb");
    head.setTs(new UFDateTime(ts));
    vo.setParentVO(head);
    if (delCount < 1)
    {
      newItems = items;
    }
    else if (delCount == items.length)
    {
      newItems = (DJZBItemVO[])null;
    }
    else
    {
      newItems = new DJZBItemVO[items.length - delCount];
      for (int i = 0; i < items.length; i++) {
        if (items[i].getStatus() == 3) {
          k++;
        } else {
          newItems[(i - k)] = items[i];
        }
      }
    }
    vo.setChildrenVO(newItems);
    return vo;
  }
  
  public DJZBVO update_SS(DJZBVO vo)
    throws SQLException, BusinessException, SystemException, Exception
  {
    convertDJZBVO(vo);
    DJZBItemVO[] items = (DJZBItemVO[])vo.getChildrenVO();
    DJZBItemVO[] newItems = (DJZBItemVO[])null;
    String itemkey = "";
    int delCount = 0;int k = 0;
    for (int i = 0; i < items.length; i++) {
      switch (items[i].getStatus())
      {
      case 2: 
        itemkey = insertItem_SS(items[i]);
        items[i].setPrimaryKey(itemkey);
        items[i].setStatus(1);
        break;
      case 1: 
        updateItem_SS(items[i]);
        break;
      case 3: 
        deleteItem_SS(items[i]);
        
        PubBO pub = new PubBO();
        if (pub.costIsUsed())
        {
          ArapClassRunBO executeBo = new ArapClassRunBO();
          
          Class[] paramtype = { String.class, String.class };
          Object[] param = { items[i].getVouchid(), items[i].getFb_oid() };
          executeBo.runMethod("nc.bs.bank.costContent.CostcontentDMO", "delete", paramtype, param);
        }
        delCount++;
      }
    }
    DJZBHeaderVO head = (DJZBHeaderVO)vo.getParentVO();
    updateHeader_SS(head);
    String ts = getTsByPrimaryKey(head.getPrimaryKey(), "arap_item");
    head.setTs(new UFDateTime(ts));
    vo.setParentVO(head);
    if (delCount < 1)
    {
      newItems = items;
    }
    else if (delCount == items.length)
    {
      newItems = (DJZBItemVO[])null;
    }
    else
    {
      newItems = new DJZBItemVO[items.length - delCount];
      for (int i = 0; i < items.length; i++) {
        if (items[i].getStatus() == 3) {
          k++;
        } else {
          newItems[(i - k)] = items[i];
        }
      }
    }
    vo.setChildrenVO(newItems);
    return vo;
  }
  
  public DJZBHeaderVO updateHeader(DJZBHeaderVO dJZBHeaders)
    throws DAOException
  {
    this.pubdao.updateObject(new DJZBHeaderVO[] { dJZBHeaders }, this.zbmeta, null);
    return dJZBHeaders;
  }
  
  private DJZBHeaderVO updateHeader_SS(DJZBHeaderVO dJZBHeader)
    throws DAOException
  {
    this.pubdao.updateObject(new DJZBHeaderVO[] { dJZBHeader }, this.ssmeta, null);
    return dJZBHeader;
  }
  
  private void updateItem(DJZBItemVO dJZBItem)
    throws DAOException
  {
    FreeDAO defdao = new FreeDAO();
    DjfkxybDAO fkdao = new DjfkxybDAO();
    DJFBItemVO[] items = (DJFBItemVO[])dJZBItem.getChildrenVO();
    if (items != null) {
      for (int i = 0; i < items.length; i++) {
        switch (items[i].getStatus())
        {
        case 2: 
          defdao.insertFree(items[i]);
          break;
        case 1: 
          defdao.updateFrees(new DJFBItemVO[] { items[i] });
          break;
        case 3: 
          defdao.deleteFree(items[i]);
        }
      }
    }
    fkdao.deleteFkxyforFB(dJZBItem.getFb_oid());
    

    DjfkxybVO[] fkxybs = dJZBItem.fkxyvos;
    if (fkxybs != null)
    {
      for (int i = 0; i < fkxybs.length; i++) {
        if (fkxybs[i] != null)
        {
          fkxybs[i].setVouchid(dJZBItem.getVouchid());
          fkxybs[i].setFb_oid(dJZBItem.getFb_oid());
        }
      }
      fkdao.insert(fkxybs);
    }
    this.basedao.updateObject(dJZBItem, this.fbmeta);
  }
  
  private void updateItem_b(DJZBItemVO dJZBItem)
    throws DAOException
  {
    this.pubdao.updateObjectPartly(dJZBItem, this.bbmeta, new String[] { "ybye", "fbye", "bbye", "closedate", "closer" });
  }
  
  public void updateItem_b_array(DJZBItemVO[] dJZBItems)
    throws DAOException, SQLException
  {
    for (int i = 0; i < dJZBItems.length; i++)
    {
      DjVOTreaterAid.convertItemVO(dJZBItems[i]);
      updateItem_b(dJZBItems[i]);
      if (dJZBItems[i].getYbye().doubleValue() == 0.0D) {
        close_SSItem(new DJZBItemVO[] { dJZBItems[i] });
      }
    }
  }
  
  private void updateItem_SS(DJZBItemVO dJZBItem)
    throws BusinessException
  {
    FreeDAO defdao = new FreeDAO();
    defdao.updateZYX(dJZBItem);
    this.pubdao.updateObject(new DJZBItemVO[] { dJZBItem }, this.bbmeta, null);
  }
  
  private void updateItem_SS(DJZBItemVO[] dJZBItems)
    throws BusinessException
  {
    FreeDAO defdao = new FreeDAO();
    int i = 0;
    for (int size = dJZBItems.length; i < size; i++) {
      defdao.updateZYX(dJZBItems[i]);
    }
    this.pubdao.updateObject(dJZBItems, this.bbmeta, null);
  }
  
  public void wszz(DJZBVO djzb)
    throws DAOException
  {
    DJZBHeaderVO head = (DJZBHeaderVO)djzb.getParentVO();
    head.setJszxzf(Integer.valueOf(5));
    this.pubdao.updateObjectPartly(head, this.zbmeta, new String[] { "zzzt", "jszxzf" });
    DJZBItemVO[] items = (DJZBItemVO[])djzb.getChildrenVO();
    wszzFB(items);
  }
  
  public void wszzFB(DJZBItemVO[] items)
    throws DAOException
  {
    this.pubdao.updateObjectPartly(items, this.fbmeta, new String[] { "payflag" }, null);
  }
  
  public void ysfDj(String vouchid)
    throws DAOException
  {
    DJZBHeaderVO vo = new DJZBHeaderVO();
    vo.setPrepay(UFBoolean.TRUE);
    vo.setPrimaryKey(vouchid);
    this.pubdao.updateObjectPartly(vo, this.zbmeta, new String[] { "prepay" });
  }
  
  public Vector queryDjTemplatePK(String pk_djlx, int syscode)
    throws Exception
  {
    String sql = "select pk_billtemplet,djlxmc from arap_djlxtemplet a inner join arap_djlx b on a.pk_djlx=b.djlxoid where pk_djlx=? and syscode=? and a.dr=0 and b.dr=0";
    
    PersistenceManager pm = null;
    ResultSet rs = null;
    try
    {

        pm=PersistenceManager.getInstance(getds());
        return (Vector) pm.getJdbcSession().executeQuery(sql,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = -8315714240534632150L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                Vector<String> result=new Vector<String>();
                while(rs.next())
        		{
        			result.add(rs.getString(1));
        			result.add(rs.getString(2));
        		}
                return result;
            }

        });

    	
    }
    finally
    {
      if (rs != null) {
        rs.close();
      }
      pm.release();
    }
  }
  
  public DJZBHeaderVO[] getDJZBHeaderVOsUniversalArray(String strSqlFromClause, boolean bWithGL, Integer bGLChalked)
    throws DAOException, SQLException
  {
    DJZBHeaderVO[] headerVOArray = (DJZBHeaderVO[])null;
    Vector vHeadVOs = getDJZBHeaderVOsUniversalVector(strSqlFromClause, bWithGL, bGLChalked);
    if (vHeadVOs != null)
    {
      headerVOArray = new DJZBHeaderVO[vHeadVOs.size()];
      vHeadVOs.copyInto(headerVOArray);
    }
    return headerVOArray;
  }
  
  private Vector<DJZBHeaderVO> getDJZBHeaderVOsUniversalVector(String strSqlFromClause, boolean bWithGL, Integer bGLChalked)
    throws DAOException, SQLException
  {
    Vector<DJZBHeaderVO> ret = getDJZBHeaderVOsUniversalVector(new Integer(-1), new Integer(-1), strSqlFromClause, bWithGL, bGLChalked);
    return ret;
  }
  
  private String getDJZBHeaderPKString(Vector vHeadVO)
  {
    String strPKs = "";
    for (int i = 0; i < vHeadVO.size(); i++)
    {
      DJZBHeaderVO head = (DJZBHeaderVO)vHeadVO.elementAt(i);
      strPKs = strPKs + "'" + head.getVouchid() + "',";
    }
    return strPKs;
  }
  
  private Vector getDJZBHeaderPKVector(Vector vHeadVO)
  {
    Vector vPK = new Vector();
    for (int i = 0; i < vHeadVO.size(); i++)
    {
      DJZBHeaderVO head = (DJZBHeaderVO)vHeadVO.elementAt(i);
      vPK.addElement(head.getVouchid());
    }
    return vPK;
  }
  
  private String createHeaderTempTable(String[] strHeaderPK)
    throws SQLException, DbException
  {
    Connection con = PersistenceManager.getInstance(getds()).getJdbcSession().getConnection();
    PreparedStatement stmt_temp = null;
    try
    {
      TempTable tmptab = new TempTable();
      String tablename = tmptab.createTempTable(con, 
        "arap_djzb_temptable", 
        "vouchid_temp char(20),ts char(19)", 
        "vouchid_temp");
      String sql_temp = " insert into  " + tablename + " (vouchid_temp) values(?)";
      stmt_temp = con.prepareStatement(sql_temp);
      for (int i = 0; i < strHeaderPK.length; i++)
      {
        stmt_temp.setString(1, strHeaderPK[i]);
        stmt_temp.addBatch();
      }
      stmt_temp.executeBatch();
      
      return tablename;
    }
    finally
    {
      if (stmt_temp != null) {
        stmt_temp.close();
      }
      if (con != null) {
        con.close();
      }
    }
  }
  
  public Vector findItemsByCondition(String strCond)
    throws DAOException
  {
    String strWhereClause = (strCond == null ? "" : strCond) + " order by vouchid,flbh ";
    return getDJZBItemVOUniversalVector(strWhereClause);
  }
  
  public DJZBVO[] getDjVObyHeaderVos(DJZBHeaderVO[] voHeads)
    throws SQLException
  {
    if ((voHeads == null) || (voHeads.length == 0)) {
      return null;
    }
    Vector vHead = new Vector();
    for (int i = 0; i < voHeads.length; i++) {
      vHead.addElement(voHeads[i]);
    }
    DJZBVO[] vos = getDjVObyHeaders(vHead);
    return vos;
  }
  
  public DJZBVO[] getDjVObyHeaders(Vector vHead)
    throws SQLException
  {
    if ((vHead == null) || (vHead.size() <= 0)) {
      return null;
    }
    DJZBVO[] vos = (DJZBVO[])null;
    try
    {
      String strSubSql = "";
      Vector vItems;
      if (vHead.size() > 150)
      {
        Vector pk_v = getDJZBHeaderPKVector(vHead);
        String[] sPks = new String[pk_v.size()];
        pk_v.copyInto(sPks);
        String tablename = createHeaderTempTable(sPks);
        strSubSql = 
          "  arap_djfb inner join " + 
          tablename + 
          " tmp on tmp.vouchid_temp=arap_djfb.vouchid";
        String where = " where dr=0 and xgbh =" + ArapConstant.UNITACCOUNTSTAT_DEFAULT + " ";
        String strWhereClause = (where == null ? "" : where) + " order by vouchid,flbh ";
        List ret = (List)this.pubdao.queryVOsByWhereClause(DJZBItemVO.class, this.fbmeta, strSubSql, strWhereClause);
        vItems = new Vector(ret);
      }
      else
      {
        String pks = getDJZBHeaderPKString(vHead);
        strSubSql = " where dr=0 and xgbh =" + ArapConstant.UNITACCOUNTSTAT_DEFAULT + " and vouchid in ( " + 
          pks.substring(0, pks.length() - 1) + 
          ") ";
        vItems = findItemsByCondition(strSubSql);
      }
      vos = ARAPDjBSUtil.distributeDjzbVOs(vHead, vItems);
    }
    catch (Exception e)
    {
      Logger.error(e, getClass(), e.getMessage());
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000506") + e.getMessage());
    }
    Vector vItems;
    return vos;
  }
  
  private Vector getGLInfo(Vector vHeaderVO, boolean bWithGL, Integer voucherFlag)
    throws SQLException
  {
    String[] strHeaderPKArray = new String[vHeaderVO.size()];
    for (int i = 0; i < vHeaderVO.size(); i++)
    {
      DJZBHeaderVO headerVO = (DJZBHeaderVO)vHeaderVO.elementAt(i);
      strHeaderPKArray[i] = headerVO.getVouchid();
    }
    GlVoucherInfoQueryBO glInfoBO = new GlVoucherInfoQueryBO();
    HashMap mapGLInfoVO = null;
    try
    {
      mapGLInfoVO = glInfoBO.getGlVoucherInfoBySourcePks(strHeaderPKArray);
    }
    catch (Exception e)
    {
      throw new SQLException(NCLangResOnserver.getInstance().getStrByID("2006030102", "UPP2006030102-000507") + e.getMessage());
    }
    Vector vHeaderVOWithGLInfo = new Vector();
    for (int i = 0; i < vHeaderVO.size(); i++)
    {
      DJZBHeaderVO headerVO = (DJZBHeaderVO)vHeaderVO.elementAt(i);
      GlVoucherInfoVO glInfoVO = (GlVoucherInfoVO)mapGLInfoVO.get(headerVO.getVouchid());
      if ((bWithGL) && 
        (glInfoVO != null)) {
        headerVO.setVouchertypeno(glInfoVO.getVoucherno());
      }
      if ((DjCondVO.Voucher_All.equals(voucherFlag)) || (voucherFlag == null)) {
        vHeaderVOWithGLInfo.add(headerVO);
      } else if (glInfoVO == null)
      {
        if (DjCondVO.Voucher_NotCreated.equals(voucherFlag)) {
          vHeaderVOWithGLInfo.add(headerVO);
        }
      }
      else if (DjCondVO.Voucher_Created.equals(voucherFlag)) {
        vHeaderVOWithGLInfo.add(headerVO);
      } else if ((DjCondVO.Voucher_Singed.equals(voucherFlag)) && 
        (glInfoVO.isAduited().booleanValue())) {
        vHeaderVOWithGLInfo.add(headerVO);
      }
    }
    return vHeaderVOWithGLInfo;
  }
  
  public DJZBItemVO[] getDJZBItemVOUniversalArray(String strWhereClause)
    throws DAOException
  {
    DJZBItemVO[] dJZBItems = (DJZBItemVO[])null;
    Vector v = getDJZBItemVOUniversalVector(strWhereClause);
    if (v.size() > 0)
    {
      dJZBItems = new DJZBItemVO[v.size()];
      v.copyInto(dJZBItems);
    }
    return dJZBItems;
  }
  
  private Vector getDJZBItemVOUniversalVector(String strWhereClause)
    throws DAOException
  {
    List ret = (List)this.pubdao.queryVOsByWhereClause(DJZBItemVO.class, this.fbmeta, null, strWhereClause);
    return new Vector(ret);
  }
  
  public void update_SS_array(DJZBVO[] vos)
    throws SQLException, BusinessException, SystemException, Exception
  {
    for (int i = 0; i < vos.length; i++)
    {
      convertDJZBVO(vos[i]);
      DJZBItemVO[] items = (DJZBItemVO[])vos[i].getChildrenVO();
      updateItem_SS(items);
      DJZBHeaderVO head = (DJZBHeaderVO)vos[i].getParentVO();
      
      updateHeader_SS(head);
    }
  }
  
  public String hasBanked(String billpk)
    throws DAOException, SQLException
  {
    String sql = "select zb.djbh,fb.tbbh from arap_djfb fb inner join arap_djzb zb on fb.vouchid=zb.vouchid where  fb_oid=? and fb.dr=0";
    
    SQLParameter parm = new SQLParameter();
    parm.addParam(billpk);
    PersistenceManager pm = null;
    try
    {

        pm=PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
         return (String)pm.getJdbcSession().executeQuery(sql,parm,new  ResultSetProcessor(){

            /**
			 *
			 */
			private static final long serialVersionUID = -3563585813077052610L;

			public Object handleResultSet(ResultSet rs) throws SQLException {
                //
                if (rs.next()) {
                    String billno = rs.getString(1);
                    String zyx = rs.getString(2);
                    if (zyx == null || zyx.length() == 0)
                        return null;
                    else
                        return billno;
                }
                return null;
            }

         });

    } catch (Exception e)
    {
      throw new DAOException(e);
    }
    finally
    {
      pm.release();
    }
  }
  
  private String getds()
  {
    return InvocationInfoProxy.getInstance().getUserDataSource();
  }
  
  public DJZBHeaderVO[] queryHead_hz(String key, boolean isHz)
    throws Exception
  {
    String sql = "";
    if (isHz) {
      sql = 
      





        "select max(arap_djzb.vouchid) , max(arap_djzb.prepay), max(arap_djzb.dwbm), max(arap_djzb.djbh), max(arap_djzb.ywbm), max(arap_djzb.djrq), max(arap_djzb.pzglh), max(arap_djzb.qcbz), max(arap_djzb.lybz), max(arap_djzb.djzt), max(arap_djzb.lrr), max(arap_djzb.shr), max(arap_djzb.shrq), max(arap_djzb.zdr), max(arap_djzb.zdrq), max(arap_djzb.dzrq), max(arap_djzb.xslxbm), max(arap_djzb.fktjbm), max(arap_djzb.zyx1), max(arap_djzb.zyx2), max(arap_djzb.zyx3), max(arap_djzb.zyx4), max(arap_djzb.zyx5), max(arap_djzb.zyx6), max(arap_djzb.zyx7), max(arap_djzb.zyx8),max(arap_djzb.zyx9),max(arap_djzb.zyx10),max(arap_djzb.zyx11),max(arap_djzb.zyx12),max(arap_djzb.zyx13),max(arap_djzb.zyx14),max(arap_djzb.zyx15),max(arap_djzb.zyx16), max(arap_djzb.zyx17), max(arap_djzb.zyx18), max(arap_djzb.zyx19), max(arap_djzb.zyx20), max(arap_djzb.ddhbbm), max(arap_djzb.djkjnd), max(arap_djzb.djkjqj), max(arap_djzb.shkjnd), max(arap_djzb.shkjqj), max(arap_djzb.hzbz), max(arap_djzb.pj_oid), max(arap_djzb.sfkr), max(arap_djzb.kmbm), max(arap_djzb.pj_num), max(arap_djzb.pj_jsfs), max(arap_djzb.qrr), max(arap_djzb.yhqrr), max(arap_djzb.scomment),max(arap_djzb.djdl),max(arap_djzb.fj), max(arap_djzb.ybje),max(arap_djzb.fbje),max(arap_djzb.bbje) ,max(arap_djzb.vouchid),sum(case arap_djfb.dr when 0 then   arap_djfb.ybye  else 0 end),sum(case arap_djfb.dr when 0 then   arap_djfb.fbye  else 0 end),sum(case arap_djfb.dr when 0 then   arap_djfb.bbye  else 0 end),max(arap_djzb.djlxbm)   from ARAP_DJZB left outer join arap_djfb on arap_djfb.vouchid=arap_djzb.vouchid " + key + " and arap_djzb.dr=0" + " and (arap_djfb.pausetransact='N' or arap_djfb.pausetransact is null)  and arap_djzb.zgyf=0 " + " group by arap_djzb.vouchid";
    } else {
      sql = 
      


        "select arap_djzb.vouchid, arap_djzb.prepay, arap_djzb.dwbm, arap_djzb.djbh, arap_djzb.ywbm, arap_djzb.djrq, arap_djzb.pzglh,arap_djzb.qcbz, arap_djzb.lybz, arap_djzb.djzt, arap_djzb.lrr, arap_djzb.shr, arap_djzb.shrq, arap_djzb.zdr, arap_djzb.zdrq, arap_djzb.dzrq, arap_djzb.xslxbm, arap_djzb.fktjbm, arap_djzb.zyx1, arap_djzb.zyx2, arap_djzb.zyx3, arap_djzb.zyx4, arap_djzb.zyx5, arap_djzb.zyx6, arap_djzb.zyx7, arap_djzb.zyx8,arap_djzb.zyx9,arap_djzb.zyx10,arap_djzb.zyx11,arap_djzb.zyx12,arap_djzb.zyx13,arap_djzb.zyx14,arap_djzb.zyx15,arap_djzb.zyx16, arap_djzb.zyx17, arap_djzb.zyx18, arap_djzb.zyx19, arap_djzb.zyx20, arap_djzb.ddhbbm, arap_djzb.djkjnd, arap_djzb.djkjqj, arap_djzb.shkjnd, arap_djzb.shkjqj, arap_djzb.hzbz, arap_djzb.pj_oid, arap_djzb.sfkr, arap_djzb.kmbm, arap_djzb.pj_num, arap_djzb.pj_jsfs,arap_djzb.qrr, arap_djzb.yhqrr, arap_djzb.scomment,arap_djzb.djdl,arap_djzb.fj, arap_djzb.ybje,arap_djzb.fbje,arap_djzb.bbje ,arap_djzb.vouchid,0,0,0,arap_djzb.djlxbm  from ARAP_DJZB left outer join arap_djfb on arap_djfb.vouchid=arap_djzb.vouchid " + key + " and arap_djzb.dr=0";
    }
    DJZBHeaderVO dJZBHeader = null;
    Connection con = null;
    PreparedStatement stmt = null;
    DJZBHeaderVO[] djzbheader = (DJZBHeaderVO[])null;
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
      con = pm.getJdbcSession().getConnection();
      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();
      
      Vector vHead = new Vector();
      while (rs.next())
      {
        dJZBHeader = new DJZBHeaderVO(key);
        
        rs.getString(1);
        

        String prepay = rs.getString(2);
        dJZBHeader.setPrepay(prepay == null ? null : new UFBoolean(prepay.trim()));
        
        String dwbm = rs.getString(3);
        dJZBHeader.setDwbm(dwbm == null ? null : dwbm.trim());
        
        String djbh = rs.getString(4);
        dJZBHeader.setDjbh(djbh == null ? null : djbh.trim());
        
        String ywbm = rs.getString(5);
        dJZBHeader.setYwbm(ywbm == null ? null : ywbm.trim());
        
        String djrq = rs.getString(6);
        dJZBHeader.setDjrq(djrq == null ? null : new UFDate(djrq.trim()));
        
        Integer pzglh = (Integer)rs.getObject(7);
        dJZBHeader.setPzglh(pzglh == null ? null : pzglh);
        
        String qcbz = rs.getString(8);
        dJZBHeader.setQcbz(qcbz == null ? null : new UFBoolean(qcbz.trim()));
        
        Integer lybz = (Integer)rs.getObject(9);
        dJZBHeader.setLybz(lybz == null ? null : lybz);
        
        Integer djzt = (Integer)rs.getObject(10);
        dJZBHeader.setDjzt(djzt == null ? null : djzt);
        
        String lrr = rs.getString(11);
        dJZBHeader.setLrr(lrr == null ? null : lrr.trim());
        
        String shr = rs.getString(12);
        dJZBHeader.setShr(shr == null ? null : shr.trim());
        
        String shrq = rs.getString(13);
        dJZBHeader.setShrq(shrq == null ? null : new UFDate(shrq.trim()));
        
        String zdr = rs.getString(14);
        dJZBHeader.setZdr(zdr == null ? null : zdr.trim());
        
        String zdrq = rs.getString(15);
        dJZBHeader.setZdrq(zdrq == null ? null : new UFDate(zdrq.trim()));
        
        String dzrq = rs.getString(16);
        dJZBHeader.setDzrq(dzrq == null ? null : new UFDate(dzrq.trim()));
        
        String xslxbm = rs.getString(17);
        dJZBHeader.setXslxbm(xslxbm == null ? null : xslxbm.trim());
        
        String fktjbm = rs.getString(18);
        dJZBHeader.setFktjbm(fktjbm == null ? null : fktjbm.trim());
        
        String zyx1 = rs.getString(19);
        dJZBHeader.setZyx1(zyx1 == null ? null : zyx1.trim());
        
        String zyx2 = rs.getString(20);
        dJZBHeader.setZyx2(zyx2 == null ? null : zyx2.trim());
        
        String zyx3 = rs.getString(21);
        dJZBHeader.setZyx3(zyx3 == null ? null : zyx3.trim());
        
        String zyx4 = rs.getString(22);
        dJZBHeader.setZyx4(zyx4 == null ? null : zyx4.trim());
        
        String zyx5 = rs.getString(23);
        dJZBHeader.setZyx5(zyx5 == null ? null : zyx5.trim());
        
        String zyx6 = rs.getString(24);
        dJZBHeader.setZyx6(zyx6 == null ? null : zyx6.trim());
        
        String zyx7 = rs.getString(25);
        dJZBHeader.setZyx7(zyx7 == null ? null : zyx7.trim());
        
        String zyx8 = rs.getString(26);
        dJZBHeader.setZyx8(zyx8 == null ? null : zyx8.trim());
        
        String zyx9 = rs.getString(27);
        dJZBHeader.setZyx9(zyx9 == null ? null : zyx9.trim());
        
        String zyx10 = rs.getString(28);
        dJZBHeader.setZyx10(zyx10 == null ? null : zyx10.trim());
        
        String zyx11 = rs.getString(29);
        dJZBHeader.setZyx11(zyx11 == null ? null : zyx11.trim());
        
        String zyx12 = rs.getString(30);
        dJZBHeader.setZyx12(zyx12 == null ? null : zyx12.trim());
        
        String zyx13 = rs.getString(31);
        dJZBHeader.setZyx13(zyx13 == null ? null : zyx13.trim());
        
        String zyx14 = rs.getString(32);
        dJZBHeader.setZyx14(zyx14 == null ? null : zyx14.trim());
        
        String zyx15 = rs.getString(33);
        dJZBHeader.setZyx15(zyx15 == null ? null : zyx15.trim());
        
        String zyx16 = rs.getString(34);
        dJZBHeader.setZyx16(zyx16 == null ? null : zyx16.trim());
        
        String zyx17 = rs.getString(35);
        dJZBHeader.setZyx17(zyx17 == null ? null : zyx17.trim());
        
        String zyx18 = rs.getString(36);
        dJZBHeader.setZyx18(zyx18 == null ? null : zyx18.trim());
        
        String zyx19 = rs.getString(37);
        dJZBHeader.setZyx19(zyx19 == null ? null : zyx19.trim());
        
        String zyx20 = rs.getString(38);
        dJZBHeader.setZyx20(zyx20 == null ? null : zyx20.trim());
        
        String ddhbbm = rs.getString(39);
        dJZBHeader.setDdhbbm(ddhbbm == null ? null : ddhbbm.trim());
        
        String djkjnd = rs.getString(40);
        dJZBHeader.setDjkjnd(djkjnd == null ? null : djkjnd.trim());
        
        String djkjqj = rs.getString(41);
        dJZBHeader.setDjkjqj(djkjqj == null ? null : djkjqj.trim());
        
        String shkjnd = rs.getString(42);
        dJZBHeader.setShkjnd(shkjnd == null ? null : shkjnd.trim());
        
        String shkjqj = rs.getString(43);
        dJZBHeader.setShkjqj(shkjqj == null ? null : shkjqj.trim());
        
        String hzbz = rs.getString(44);
        dJZBHeader.setHzbz(hzbz == null ? null : hzbz.trim());
        
        String pj_oid = rs.getString(45);
        dJZBHeader.setPj_oid(pj_oid == null ? null : pj_oid.trim());
        
        String sfkr = rs.getString(46);
        dJZBHeader.setSfkr(sfkr == null ? null : sfkr.trim());
        
        String kmbm = rs.getString(47);
        dJZBHeader.setKmbm(kmbm == null ? null : kmbm.trim());
        
        String pj_num = rs.getString(48);
        dJZBHeader.setPj_num(pj_num == null ? null : pj_num.trim());
        
        String pj_jsfs = rs.getString(49);
        dJZBHeader.setPj_jsfs(pj_jsfs == null ? null : pj_jsfs.trim());
        
        String qrr = rs.getString(50);
        dJZBHeader.setQrr(qrr == null ? null : qrr.trim());
        
        String yhqrr = rs.getString(51);
        dJZBHeader.setYhqrr(yhqrr == null ? null : yhqrr.trim());
        
        String scomment = rs.getString(52);
        dJZBHeader.setScomment(scomment == null ? null : scomment.trim());
        
        String djdl = rs.getString(53);
        dJZBHeader.setDjdl(djdl == null ? null : djdl.trim());
        
        Integer fj = (Integer)rs.getObject(54);
        dJZBHeader.setFj(fj == null ? null : fj);
        
        BigDecimal ybje = rs.getBigDecimal(55);
        dJZBHeader.setYbje(ybje == null ? null : new UFDouble(ybje));
        
        BigDecimal fbje = rs.getBigDecimal(56);
        dJZBHeader.setFbje(fbje == null ? null : new UFDouble(fbje));
        
        BigDecimal bbje = rs.getBigDecimal(57);
        dJZBHeader.setBbje(bbje == null ? null : new UFDouble(bbje));
        
        String vouchid = rs.getString(58);
        dJZBHeader.setVouchid(vouchid == null ? null : vouchid.trim());
        


        BigDecimal djzbhzybye = rs.getBigDecimal(59);
        dJZBHeader.setHzybye(djzbhzybye == null ? null : new UFDouble(djzbhzybye));
        

        BigDecimal djzbhzfbye = rs.getBigDecimal(60);
        dJZBHeader.setHzfbye(djzbhzfbye == null ? null : new UFDouble(djzbhzfbye));
        

        BigDecimal djzbhzbbye = rs.getBigDecimal(61);
        dJZBHeader.setHzbbye(djzbhzbbye == null ? null : new UFDouble(djzbhzbbye));
        


        String djlxbm = rs.getString(62);
        dJZBHeader.setDjlxbm(djlxbm == null ? null : djlxbm.trim());
        

        vHead.addElement(dJZBHeader);
      }
      djzbheader = new DJZBHeaderVO[vHead.size()];
      vHead.copyInto(djzbheader);
    }
    finally
    {
      try
      {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (Exception localException) {}
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception localException1) {}
    }
    return djzbheader;
  }
  
  private Vector<DJZBHeaderVO> getDJZBHeaderVOsUniversalVector(Integer initPos, Integer count, String strSqlFromClause, boolean bWithGL, Integer voucherFlag)
    throws DAOException, SQLException
  {
    String strSQL = getSelectSQL(this.zbmeta);
    

    strSQL = strSQL + strSqlFromClause;
    Logger.error("before-------" + System.currentTimeMillis());
    List ret; 
    if ((bWithGL) || (voucherFlag != null)) {
      ret = (List)this.pubdao.queryVOsBySql(DJZBHeaderVO.class, this.zbmeta, strSQL, initPos.intValue(), count.intValue(), new DJZBVOGLRSChecker(bWithGL, voucherFlag));
    } else {
      ret = (List)this.pubdao.queryVOsBySql(DJZBHeaderVO.class, this.zbmeta, strSQL, initPos.intValue(), count.intValue());
    }
    Vector<DJZBHeaderVO> vHeadVOs = new Vector(ret);
    
















    return vHeadVOs;
  }
  
  public Vector queryDjLbP_SS(Integer initPos, Integer count, DjCondVO djcond)
    throws Exception
  {
    QryCondArrayVO[] norCondVos = djcond.m_NorCondVos;
    ConditionVO[] defCondVos = djcond.m_DefCondVos;
    String sql = getSelectSQL(this.ssmeta);
    












    sql = sql + " from arap_item zb inner join arap_item_b fb on zb.vouchid=fb.vouchid";
    sql = sql + new PubMethods().getBillQuerySubSql_Dj(norCondVos, defCondVos, djcond.pk_corp, djcond.operator);
    sql = sql + " and zb.dr=0 and fb.dr=0";
    sql = sql + " order by zb.djrq,zb.djlxbm,  zb.djbh ";
    List ret = (List)this.pubdao.queryVOsBySql(DJZBHeaderVO.class, this.ssmeta, sql, initPos.intValue(), count.intValue());
    






    Vector vHead = new Vector(ret);
    







    return vHead;
  }
  
  public Vector queryDjLbP_SS_4(Integer initPos, Integer count, DjCondVO djcond, boolean isCHz)
    throws Exception
  {
    QryCondArrayVO[] norCondVos = djcond.m_NorCondVos;
    ConditionVO[] defCondVos = djcond.m_DefCondVos;
    String key = " from arap_item zb inner join arap_item_b fb on zb.vouchid=fb.vouchid ";
    
    key = key + new PubMethods().getBillQuerySubSql_Dj(norCondVos, defCondVos, djcond.pk_corp, djcond.operator);
    key = key.replaceAll("arap_djzb", "arap_item");
    key = key.replaceAll("arap_djfb", "arap_item_b");
    String condNHzfs = "";String condNHzsh = "";
    if (!isCHz)
    {
      condNHzfs = " and COALESCE(zb.hzbz,' ')<>'1'";
      condNHzsh = " and COALESCE(zb.hzbz,' ')<>'0'";
    }
    String sql = getSelectSQL(this.ssmeta) + " " + 
    



      key + 
      



      " and zb.djzt=2" + 
      " and fb.ybye>0" + 
      " and fb.dr=0" + 
      " and zb.dr=0" + 
      condNHzfs + 
      condNHzsh + 
      
      " order by zb.djrq, zb.djlxbm, zb.djbh ";
    List ret = (List)this.pubdao.queryVOsBySql(DJZBHeaderVO.class, this.ssmeta, sql, initPos.intValue(), count.intValue());
    







    Vector vHead = new Vector(ret);
    






    return vHead;
  }
  
  private String getSelectSQL(IArapMappingMeta meta)
  {
    StringBuffer buf = new StringBuffer("SELECT DISTINCT zb.").append("vouchid");
    int i = 0;
    for (int size = meta.getColumns().length; i < size; i++) {
      if (!"vouchid".equalsIgnoreCase(meta.getColumns()[i])) {
        buf.append(", zb.").append(meta.getColumns()[i]);
      }
    }
    return buf.toString();
  }
  
  public LightBillVO[] getForwardBills(String curBillType, String curBillID, String forwardBillType)
    throws DAOException
  {
    String sql = "select distinct zb.vouchid ,zb.djbh from arap_djzb zb inner join arap_djfb fb on fb.vouchid=zb.vouchid where fb.ddlx= '" + 
      curBillID + "' and zb.djdl = '" + forwardBillType + "' and zb.dr=0";
    PersistenceManager pm = null;
    Map ret = null;
    List lst = new ArrayList();
    try
    {
      pm = PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
      ret = (Map)pm.getJdbcSession().executeQuery(sql, new MapProcessor());
      Iterator it = ret.keySet().iterator();
      while (it.hasNext())
      {
        LightBillVO vo = new LightBillVO();
        String key = (String)it.next();
        vo.setID(key);
        vo.setCode((String)ret.get(key));
        vo.setType(forwardBillType);
        lst.add(vo);
      }
    }
    catch (Exception e)
    {
      throw new DAOException(e);
    }
    finally
    {
      pm.release();
    }
    return (LightBillVO[])lst.toArray(new LightBillVO[0]);
  }
  
  public DJZBVO[] getDJByXXID(String key, String value)
    throws SQLException, DbException, DAOException
  {
    PubDAO dao = new PubDAO();
    List ret = (List)dao.queryVOsByWhereClause(DJZBItemVO.class, this.fbmeta, null, " " + key + " = '" + value + "' and dr=0");
    List<String> vouchids = new ArrayList();
    
    Map<String, List<DJZBItemVO>> map = new HashMap();
    for (int i = 0; i < ret.size(); i++)
    {
      String vd = ((DJZBItemVO)ret.get(i)).getVouchid();
      if (!vouchids.contains(vd))
      {
        vouchids.add(vd);
        map.put(vd, new ArrayList());
      }
      ((List)map.get(vd)).add((DJZBItemVO)ret.get(i));
    }
    Vector<DJZBHeaderVO> heads = findHeaderByPrimaryKeys((String[])vouchids.toArray(new String[0]));
    DJZBVO[] djzbvos = new DJZBVO[vouchids.size()];
    int i = 0;
    for (DJZBHeaderVO head : heads)
    {
      DJZBVO djzbvo = new DJZBVO();
      if ((head.getZgyf() != null) && (head.getZgyf().intValue() != 0))
      {
        String zyx29 = (head.getZyx29() == null) || (head.getZyx29().equals("")) ? "N" : head.getZyx29().toString();
        String zyx30 = (head.getZyx30() == null) || (head.getZyx30().equals("")) ? "N" : head.getZyx30().toString();
        if ((!zyx29.equals("Y")) && (!zyx30.equals("Y")))
        {
          djzbvo.setParentVO(head);
          djzbvo.setChildrenVO((CircularlyAccessibleValueObject[])((List)map.get(head.getVouchid())).toArray(new DJZBItemVO[0]));
          djzbvos[(i++)] = djzbvo;
        }
      }
    }
    return djzbvos;
  }
  
  public void updateItemByEncode(String pk, String encode)
    throws SQLException, DbException
  {
    String sql = "update ARAP_DJFB set encode = '" + encode + "' where fb_oid='" + pk + "'";
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
      pm.getJdbcSession().executeUpdate(sql);
    }
    finally
    {
      pm.release();
    }
  }
  
  public void updatePayMan(String[] headdatas, String[][] bodydatas)
    throws BusinessException
  {
    String sql = "update ARAP_DJZB set payman = '" + headdatas[0] + 
      "',paydate='" + headdatas[1] + "' where vouchid='" + headdatas[2] + "'";
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
      pm.getJdbcSession().executeUpdate(sql);
      int i = 0;
      for (int size = bodydatas.length; i < size; i++)
      {
        String[] data = bodydatas[i];
        sql = "update ARAP_DJFB set payman = '" + data[0] + 
          "',paydate='" + data[1] + "' where fb_oid='" + data[2] + "'";
        pm.getJdbcSession().addBatch(sql);
      }
      pm.getJdbcSession().executeBatch();
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }
    finally
    {
      pm.release();
    }
  }
  
  public void updateXtFlag(List<String> fbpks, Integer flag)
    throws BusinessException
  {
    if ((fbpks == null) || (fbpks.size() == 0)) {
      return;
    }
    StringBuffer where = new StringBuffer();
    //edit by zwx 2017-6-29 超过1000分开
//    for (String fbpk : fbpks) {
//      where.append("'").append(fbpk).append("',");
//    }
    StringBuffer wheremore = new StringBuffer();
    for(int i = 0;i<fbpks.size();i++){
    	String fbpk = fbpks.get(i);
    	if(i<999){
    		where.append("'").append(fbpk).append("',");
    	}else{
    		wheremore.append("'").append(fbpk).append("',");
    	}
    }
    //end by zwx
    flag = flag == null ? new Integer(0) : flag;

    //edit by zwx 2017-6-29 超过1000分开
//    String sql = "update ARAP_DJFB set djxtflag = " + flag.intValue() + " where fb_oid in ( " + where.substring(0, where.length() - 1) + ")";
    String sql = "";
    if(wheremore.toString().length()>0){
    	sql = "update ARAP_DJFB set djxtflag = " + flag.intValue() + " where fb_oid in ( " + where.substring(0, where.length() - 1) + ") or fb_oid in ( " + wheremore.substring(0, wheremore.length() - 1)+")";
    }else{
        sql = "update ARAP_DJFB set djxtflag = " + flag.intValue() + " where fb_oid in ( " + where.substring(0, where.length() - 1) + ")";
    }
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
      pm.getJdbcSession().executeUpdate(sql);
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }
    finally
    {
      pm.release();
    }
  }
  
  public void updatePayInfo(String[] headdatas, String[][] bodydatas, int jszxzf)
    throws BusinessException
  {
    String sql = "update ARAP_DJZB set payman = '" + headdatas[0] + 
      "',paydate='" + headdatas[1] + "' ,jszxzf=" + jszxzf + ",zzzt=1  where vouchid='" + headdatas[2] + "'";
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(InvocationInfoProxy.getInstance().getUserDataSource());
      pm.getJdbcSession().executeUpdate(sql);
      int i = 0;
      for (int size = bodydatas.length; i < size; i++)
      {
        String[] data = bodydatas[i];
        sql = "update ARAP_DJFB set payman = '" + data[0] + 
          "',paydate='" + data[1] + "' ,payflag=" + jszxzf + "  where fb_oid='" + data[2] + "'";
        pm.getJdbcSession().addBatch(sql);
      }
      pm.getJdbcSession().executeBatch();
    }
    catch (Exception e)
    {
      Log.getInstance(getClass()).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage(), e);
    }
    finally
    {
      pm.release();
    }
  }
  
  public String[] getDjPksBySSPk(String sspk)
    throws DbException
  {
    String sql = "select fb.vouchid from  arap_djfb fb inner join arap_item ss on fb.item_bill_pk=ss.vouchid  where fb.dr=0 and ss.vouchid='" + 
      sspk + "' group by fb.vouchid";
    PersistenceManager pm = null;
    try
    {
      pm = PersistenceManager.getInstance(getds());
      return (String[])pm.getJdbcSession().executeQuery(sql, new ArrayProcessor());
    }
    finally
    {
      pm.release();
    }
  }
}
