package nc.bs.ic.pub.bill;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ic.pub.InvATPDMO;
import nc.bs.ic.pub.InvOnHandDMO;
import nc.bs.ic.pub.monthsum.MonthServ;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.OnhandnumHeaderVO;
import nc.vo.ic.pub.bill.OnhandnumItemVO;
import nc.vo.ic.pub.bill.OnhandnumVO;
import nc.vo.ic.pub.bill.Timer;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.ic.ATPVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.merge.DefaultVOMerger;
import nc.vo.scm.pub.sort.SortMethod;

public class OnhandnumDMO extends DataManageObject
{
  private final String sqlInsertB = "insert into ic_onhandnum_b(pk_onhandnum_b,ccalbodyidb, cwarehouseidb, cinventoryidb, vlotb, castunitidb, vfreeb10, vfreeb9, vfreeb8, vfreeb7, vfreeb6, vfreeb5, vfreeb4, vfreeb3, vfreeb2, vfreeb1,cspaceid, nnum, nastnum,pk_onhandnum,cvendorid,hsl,ngrossnum,pk_corp,cinvbasid) values(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";

  private final String sqlInsertH = "insert into ic_onhandnum(pk_onhandnum, pk_corp, ccalbodyid, cwarehouseid, cinventoryid, vlot, cvendorid, castunitid, vfree10, vfree9, vfree8, vfree7, vfree6, vfree5, vfree4, vfree3, vfree2, vfree1, nonhandnum, nonhandastnum,nnum1,nastnum1,nnum2,nastnum2,hsl,ngrossnum,cinvbasid,ngrossnum1,ngrossnum2) values(?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

  private String sqlUpdatePK = "UPDATE ic_onhandnum_b set pk_onhandnum=(select pk_onhandnum from ic_onhandnum where ic_onhandnum_b.cinventoryidb = ic_onhandnum.cinventoryid AND ic_onhandnum_b.cwarehouseidb = ic_onhandnum.cwarehouseid AND ic_onhandnum_b.ccalbodyidb=ic_onhandnum.ccalbodyid AND (ic_onhandnum_b.pk_corp=ic_onhandnum.pk_corp) AND (ic_onhandnum_b.cvendorid=ic_onhandnum.cvendorid or ic_onhandnum_b.cvendorid is null and ic_onhandnum.cvendorid is null) AND (ic_onhandnum_b.hsl=ic_onhandnum.hsl or ic_onhandnum_b.hsl is null and ic_onhandnum.hsl is null) AND (ic_onhandnum_b.vlotb=ic_onhandnum.vlot or ic_onhandnum_b.vlotb is null and ic_onhandnum.vlot is null) AND (ic_onhandnum_b.castunitidb=ic_onhandnum.castunitid or ic_onhandnum_b.castunitidb is null and ic_onhandnum.castunitid is null) AND (ic_onhandnum_b.vfreeb1 = ic_onhandnum.vfree1 or ic_onhandnum_b.vfreeb1 is null and ic_onhandnum.vfree1 is null)AND (ic_onhandnum_b.vfreeb2 = ic_onhandnum.vfree2 or ic_onhandnum_b.vfreeb2 is null and ic_onhandnum.vfree2 is null)AND (ic_onhandnum_b.vfreeb3 = ic_onhandnum.vfree3 or ic_onhandnum_b.vfreeb3 is null and ic_onhandnum.vfree3 is null)AND (ic_onhandnum_b.vfreeb4 = ic_onhandnum.vfree4 or ic_onhandnum_b.vfreeb4 is null and ic_onhandnum.vfree4 is null)AND (ic_onhandnum_b.vfreeb5 = ic_onhandnum.vfree5 or ic_onhandnum_b.vfreeb5 is null and ic_onhandnum.vfree5 is null)AND (ic_onhandnum_b.vfreeb6 = ic_onhandnum.vfree6 or ic_onhandnum_b.vfreeb6 is null and ic_onhandnum.vfree6 is null)AND (ic_onhandnum_b.vfreeb7 = ic_onhandnum.vfree7 or ic_onhandnum_b.vfreeb7 is null and ic_onhandnum.vfree7 is null)AND (ic_onhandnum_b.vfreeb8 = ic_onhandnum.vfree8 or ic_onhandnum_b.vfreeb8 is null and ic_onhandnum.vfree8 is null)AND (ic_onhandnum_b.vfreeb9 = ic_onhandnum.vfree9 or ic_onhandnum_b.vfreeb9 is null and ic_onhandnum.vfree9 is null)AND (ic_onhandnum_b.vfreeb10 = ic_onhandnum.vfree10 or ic_onhandnum_b.vfreeb10 is null and ic_onhandnum.vfree10 is null)) where pk_onhandnum ='" + GenMethod.STRING_NULL + "'";

  private static String[] m_keys = { "cinventoryid", "cwarehouseid", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "castunitid", "vlot", "cvendorid", "hsl", "cspaceid" };

  HashMap m_hmIsNotCheck = new HashMap();
  boolean isCheckAtp = true;
  private final String sSymbol = "=^-^=";

  public OnhandnumDMO()
    throws NamingException, SystemException
  {
  }

  public OnhandnumDMO(String dbName)
    throws NamingException, SystemException
  {
    super(dbName);
  }

  public static CircularlyAccessibleValueObject[] mergeOnhandVO(CircularlyAccessibleValueObject[] vos) throws BusinessException {
    DefaultVOMerger m = new DefaultVOMerger();
    m.setMergeAttrs(m_keys, new String[] { "nonhandnum", "nonhandastnum", "nnum1", "nastnum1", "nnum2", "nastnum2", "ngrossnum", "nnum", "nastnum", "ngrossnum1", "ngrossnum2" }, null, null, null);

    return m.mergeByGroup(vos);
  }

  private String insertHeader(OnhandnumHeaderVO headVO)
    throws SQLException, SystemException
  {
    return insertHeader("insert into ic_onhandnum(pk_onhandnum, pk_corp, ccalbodyid, cwarehouseid, cinventoryid, vlot, cvendorid, castunitid, vfree10, vfree9, vfree8, vfree7, vfree6, vfree5, vfree4, vfree3, vfree2, vfree1, nonhandnum, nonhandastnum,nnum1,nastnum1,nnum2,nastnum2,hsl,ngrossnum,cinvbasid,ngrossnum1,ngrossnum2) values(?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)", new OnhandnumHeaderVO[] { headVO });
  }

  private String insertItem(OnhandnumItemVO voItem) throws SQLException, SystemException
  {
    String[] keys = insertItems("insert into ic_onhandnum_b(pk_onhandnum_b,ccalbodyidb, cwarehouseidb, cinventoryidb, vlotb, castunitidb, vfreeb10, vfreeb9, vfreeb8, vfreeb7, vfreeb6, vfreeb5, vfreeb4, vfreeb3, vfreeb2, vfreeb1,cspaceid, nnum, nastnum,pk_onhandnum,cvendorid,hsl,ngrossnum,pk_corp,cinvbasid) values(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)", new OnhandnumItemVO[] { voItem });
    if ((keys != null) && (keys.length > 0)) {
      return keys[0];
    }
    return null;
  }

  private String insertHeader(String sql, OnhandnumHeaderVO[] headVOs)
    throws SQLException, SystemException
  {
    if ((headVOs == null) || (headVOs.length == 0)) {
      return null;
    }
    CrossDBConnection con = null;
    PreparedStatement stmt = null;
    String key = null;
    try {
      con = (CrossDBConnection)getConnection();
      stmt = prepareStatement(con, sql);

      OnhandnumHeaderVO headVO = null;
      for (int i = 0; i < headVOs.length; i++) {
        key = headVOs[i].getPk_onhandnum();
        if (key == null) {
          key = getOID();
          headVOs[i].setPk_onhandnum(key);
        }

        headVO = headVOs[i];
        int iINDEX = 1;
        GenMethod.setStmtString(stmt, key, iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getPk_corp(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getCcalbodyid(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getCwarehouseid(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getCinventoryid(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVlot(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getCvendorid(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getCastunitid(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree10(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree9(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree8(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree7(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree6(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree5(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree4(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree3(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree2(), iINDEX++);
        GenMethod.setStmtString(stmt, headVO.getVfree1(), iINDEX++);
        GenMethod.setStmtBigDecimalZero(stmt, headVO.getNonhandnum(), iINDEX++);
        GenMethod.setStmtBigDecimalZero(stmt, headVO.getNonhandastnum(), iINDEX++);
        GenMethod.setStmtBigDecimalZero(stmt, headVO.getNnum1(), iINDEX++);
        GenMethod.setStmtBigDecimalZero(stmt, headVO.getNastnum1(), iINDEX++);
        GenMethod.setStmtBigDecimalZero(stmt, headVO.getNnum2(), iINDEX++);
        GenMethod.setStmtBigDecimalZero(stmt, headVO.getNastnum2(), iINDEX++);

        GenMethod.setStmtBigDecimal(stmt, headVO.getHsl(), iINDEX++);

        GenMethod.setStmtBigDecimalZero(stmt, headVO.getNgrossnum(), iINDEX++);

        GenMethod.setStmtString(stmt, headVO.getCinvbasid(), iINDEX++);
        GenMethod.setStmtBigDecimalZero(stmt, headVO.getNgrossnum1(), iINDEX++);
        GenMethod.setStmtBigDecimalZero(stmt, headVO.getNgrossnum2(), iINDEX++);

        executeUpdate(stmt);
        System.out.println(sql);
      }
      executeBatch(stmt);
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    return headVOs[0].getPk_onhandnum();
  }

  private String[] insertItems(String sql, OnhandnumItemVO[] voItems)
    throws SQLException, SystemException
  {
    if ((voItems == null) || (voItems.length == 0)) {
      return null;
    }
    Connection con = null;
    PreparedStatement stmt = null;

    String[] keys = getOIDs(voItems.length);
    try {
      String key = null;
      con = getConnection();
      stmt = prepareStatement(con, sql);

      OnhandnumItemVO voItem = null;
      for (int i = 0; i < voItems.length; i++) {
        key = keys[i];
        voItem = voItems[i];
        int iINDEX = 1;
        GenMethod.setStmtString(stmt, key, iINDEX++);

        GenMethod.setStmtString(stmt, voItem.getCcalbodyid(), iINDEX++);

        GenMethod.setStmtString(stmt, voItem.getCwarehouseid(), iINDEX++);

        GenMethod.setStmtString(stmt, voItem.getCinventoryid(), iINDEX++);

        GenMethod.setStmtString(stmt, voItem.getVlot(), iINDEX++);

        GenMethod.setStmtString(stmt, voItem.getCastunitid(), iINDEX++);

        GenMethod.setStmtString(stmt, voItem.getVfree10(), iINDEX++);
        GenMethod.setStmtString(stmt, voItem.getVfree9(), iINDEX++);
        GenMethod.setStmtString(stmt, voItem.getVfree8(), iINDEX++);
        GenMethod.setStmtString(stmt, voItem.getVfree7(), iINDEX++);
        GenMethod.setStmtString(stmt, voItem.getVfree6(), iINDEX++);
        GenMethod.setStmtString(stmt, voItem.getVfree5(), iINDEX++);
        GenMethod.setStmtString(stmt, voItem.getVfree4(), iINDEX++);
        GenMethod.setStmtString(stmt, voItem.getVfree3(), iINDEX++);
        GenMethod.setStmtString(stmt, voItem.getVfree2(), iINDEX++);
        GenMethod.setStmtString(stmt, voItem.getVfree1(), iINDEX++);

        GenMethod.setStmtString(stmt, voItem.getCspaceid(), iINDEX++);

        GenMethod.setStmtBigDecimal(stmt, voItem.getNnum(), iINDEX++);

        GenMethod.setStmtBigDecimal(stmt, voItem.getNastnum(), iINDEX++);

        if (voItem.getPk_onhandnum() == null)
          stmt.setString(iINDEX++, GenMethod.STRING_NULL);
        else {
          stmt.setString(iINDEX++, voItem.getPk_onhandnum());
        }

        GenMethod.setStmtString(stmt, voItem.getCvendorid(), iINDEX++);

        GenMethod.setStmtBigDecimal(stmt, voItem.getHsl(), iINDEX++);

        GenMethod.setStmtBigDecimalZero(stmt, voItem.getNgrossnum(), iINDEX++);

        GenMethod.setStmtString(stmt, voItem.getPk_corp(), iINDEX++);

        GenMethod.setStmtString(stmt, voItem.getCinvbasid(), iINDEX++);

        executeUpdate(stmt);
      }
      executeBatch(stmt);
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception e) {
      }
      try {
        if (con != null) {
          con.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    return keys;
  }

  private Hashtable getHashOnHand(GeneralBillVO voPre, boolean isStatus)
  {
    if (voPre == null) {
      return new Hashtable();
    }
    Hashtable htOld = new Hashtable();
    GeneralBillHeaderVO voHeadPre = null;
    GeneralBillItemVO[] voItems = null;
    String key = null;
    String key1 = null;
    boolean bGross = false;
    boolean bVendor = false;

    bVendor = voPre.getWh().getIsgathersettle() == null ? false : voPre.getWh().getIsgathersettle().booleanValue();
    
    //货位调整无法删除--wkf---2014-11-13--start--
    String wh = voPre.getWh().getPrimaryKey();
    String corp = voPre.getWh().getPk_corp();
    StringBuffer bvsql = new StringBuffer();
    bvsql.append(" select st.isgathersettle from bd_stordoc st ") 
    .append("         inner join bd_corp co ") 
    .append("         on st.pk_corp = co.pk_corp ") 
    .append("          where nvl(st.dr,0)=0  ") 
    .append("          and co.fathercorp = '1095' ") 
    .append("          and  st.pk_stordoc = '"+wh+"'  ")
    .append("          and st.pk_corp = '"+corp+"' ") ;

    BaseDAO baseDAO = new BaseDAO();
    try {
		Object wer = baseDAO.executeQuery(bvsql.toString(), new ColumnProcessor());
		if (wer!=null && "Y".equals(wer)) {
			bVendor = true;
		}
	} catch (DAOException e) {
		e.printStackTrace();
	}
    //货位调整无法删除--wkf---2014-11-13--end--
    
    OnhandnumHeaderVO voHead = null;
    OnhandnumItemVO voItem = null;

    UFDouble ZERO = new UFDouble(0.0D);

    boolean isInBill = true;

    if (voPre.getBillInOutFlag() == -1) {
      isInBill = false;
    }
    LocatorVO[] voLocs = null;

    if ((voPre.getParentVO() != null) && (voPre.getChildrenVO() != null)) {
      voHeadPre = voPre.getHeaderVO();
      voItems = (GeneralBillItemVO[])voPre.getChildrenVO();

      for (int i = 0; i < voItems.length; i++) {
        if ((!isStatus) || (voItems[i].getStatus() != 3))
        {
          key = getKey(voItems[i], voHeadPre.getCwarehouseid(), bVendor);
          bGross = (voItems[i].getInv().getIsmngstockbygrswt() != null) && (voItems[i].getInv().getIsmngstockbygrswt().intValue() == 1);

          if (!htOld.containsKey(key)) {
            voHead = new OnhandnumHeaderVO();
            voHead.setPk_corp(voHeadPre.getPk_corp());
            voHead.setCwarehouseid(voHeadPre.getCwarehouseid());
            voHead.setCcalbodyid(voHeadPre.getPk_calbody());
            voHead.setCinventoryid(voItems[i].getCinventoryid());
            voHead.setCinvbasid(voItems[i].getCinvbasid());
            voHead.setVlot(voItems[i].getVbatchcode());
            voHead.setCastunitid(voItems[i].getCastunitid());
            if ((bVendor) || ((voItems[i].getInv().getIssupplierstock() != null) && (voItems[i].getInv().getIssupplierstock().intValue() == 1)))
              voHead.setCvendorid(voItems[i].getCvendorid());
            else {
              voHead.setCvendorid(null);
            }

            if ((voItems[i].getIsStoreByConvert() != null) && (voItems[i].getIsStoreByConvert().intValue() == 1))
              voHead.setHsl(voItems[i].getHsl());
            else {
              voHead.setHsl(null);
            }

            voHead.setVfree1(voItems[i].getVfree1());
            voHead.setVfree2(voItems[i].getVfree2());
            voHead.setVfree3(voItems[i].getVfree3());
            voHead.setVfree4(voItems[i].getVfree4());
            voHead.setVfree5(voItems[i].getVfree5());
            voHead.setVfree6(voItems[i].getVfree6());
            voHead.setVfree7(voItems[i].getVfree7());
            voHead.setVfree8(voItems[i].getVfree8());
            voHead.setVfree9(voItems[i].getVfree9());
            voHead.setVfree10(voItems[i].getVfree10());
            voHead.setNonhandnum(ZERO);
            voHead.setNonhandastnum(ZERO);
            voHead.setNgrossnum(ZERO);
            htOld.put(key, voHead);
          }
          else {
            voHead = (OnhandnumHeaderVO)htOld.get(key);
          }
          if (isInBill) {
            if (GenMethod.isNotEqualsZero(voItems[i].getNinnum())) {
              voHead.setNonhandnum(voItems[i].getNinnum().add(voHead.getNonhandnum()));
            }
            if (GenMethod.isNotEqualsZero(voItems[i].getNinassistnum())) {
              voHead.setNonhandastnum(voItems[i].getNinassistnum().add(voHead.getNonhandastnum()));
            }
            if ((bGross) && (GenMethod.isNotEqualsZero(voItems[i].getNingrossnum())))
              voHead.setNgrossnum(voItems[i].getNingrossnum().add(voHead.getNgrossnum()));
          }
          else {
            if (GenMethod.isNotEqualsZero(voItems[i].getNoutnum())) {
              voHead.setNonhandnum(voHead.getNonhandnum().sub(voItems[i].getNoutnum()));
            }
            if (GenMethod.isNotEqualsZero(voItems[i].getNoutassistnum())) {
              voHead.setNonhandastnum(voHead.getNonhandastnum().sub(voItems[i].getNoutassistnum()));
            }
            if ((bGross) && (GenMethod.isNotEqualsZero(voItems[i].getNoutgrossnum()))) {
              voHead.setNgrossnum(voHead.getNgrossnum().sub(voItems[i].getNoutgrossnum()));
            }
          }

          if ((voItems[i].getLocator() != null) && (voItems[i].getLocator().length > 0)) {
            voLocs = (LocatorVO[])voItems[i].getLocator();
            for (int j = 0; j < voLocs.length; j++)
            {
              key1 = key + "=^-^=" + voLocs[j].getCspaceid();
              if (!htOld.containsKey(key1)) {
                voItem = new OnhandnumItemVO();
                voItem.setNnum(ZERO);
                voItem.setNastnum(ZERO);
                voItem.setNgrossnum(ZERO);
                voItem.setCspaceid(voLocs[j].getCspaceid());

                voItem.setPk_corp(voHeadPre.getPk_corp());
                voItem.setCwarehouseid(voHeadPre.getCwarehouseid());
                voItem.setCcalbodyid(voHeadPre.getPk_calbody());
                voItem.setCinventoryid(voItems[i].getCinventoryid());
                voItem.setCinvbasid(voItems[i].getCinvbasid());
                voItem.setVlot(voItems[i].getVbatchcode());
                voItem.setCastunitid(voItems[i].getCastunitid());

                if ((bVendor) || ((voItems[i].getInv().getIssupplierstock() != null) && (voItems[i].getInv().getIssupplierstock().intValue() == 1)))
                  voItem.setCvendorid(voItems[i].getCvendorid());
                else {
                  voItem.setCvendorid(null);
                }
                if ((voItems[i].getIsStoreByConvert() != null) && (voItems[i].getIsStoreByConvert().intValue() == 1))
                  voItem.setHsl(voItems[i].getHsl());
                else {
                  voItem.setHsl(null);
                }
                voItem.setVfree1(voItems[i].getVfree1());
                voItem.setVfree2(voItems[i].getVfree2());
                voItem.setVfree3(voItems[i].getVfree3());
                voItem.setVfree4(voItems[i].getVfree4());
                voItem.setVfree5(voItems[i].getVfree5());
                voItem.setVfree6(voItems[i].getVfree6());
                voItem.setVfree7(voItems[i].getVfree7());
                voItem.setVfree8(voItems[i].getVfree8());
                voItem.setVfree9(voItems[i].getVfree9());
                voItem.setVfree10(voItems[i].getVfree10());
                htOld.put(key1, voItem);
              }
              else {
                voItem = (OnhandnumItemVO)htOld.get(key1);
              }
              if (GenMethod.isNotEqualsZero(voLocs[j].getNinspacenum())) {
                voItem.setNnum(voItem.getNnum().add(voLocs[j].getNinspacenum()));
              }
              if (GenMethod.isNotEqualsZero(voLocs[j].getNinspaceassistnum())) {
                voItem.setNastnum(voItem.getNastnum().add(voLocs[j].getNinspaceassistnum()));
              }
              if ((bGross) && (GenMethod.isNotEqualsZero(voLocs[j].getNingrossnum()))) {
                voItem.setNgrossnum(voItem.getNgrossnum().add(voLocs[j].getNingrossnum()));
              }
              if (GenMethod.isNotEqualsZero(voLocs[j].getNoutspacenum())) {
                voItem.setNnum(voItem.getNnum().sub(voLocs[j].getNoutspacenum()));
              }
              if (GenMethod.isNotEqualsZero(voLocs[j].getNoutspaceassistnum())) {
                voItem.setNastnum(voItem.getNastnum().sub(voLocs[j].getNoutspaceassistnum()));
              }
              if ((bGross) && (GenMethod.isNotEqualsZero(voLocs[j].getNoutgrossnum()))) {
                voItem.setNgrossnum(voItem.getNgrossnum().sub(voLocs[j].getNoutgrossnum()));
              }
            }
          }
        }
      }
    }
    return htOld;
  }

  private String getKey(GeneralBillItemVO vo, String cwhid, boolean bVendor)
  {
    StringBuffer key = new StringBuffer();
    key.append(cwhid);
    key.append(vo.getCinventoryid());
    key.append(vo.getCastunitid());
    key.append(vo.getVfree1());
    key.append(vo.getVfree2());
    key.append(vo.getVfree3());
    key.append(vo.getVfree4());
    key.append(vo.getVfree5());
    key.append(vo.getVfree6());
    key.append(vo.getVfree7());
    key.append(vo.getVfree8());
    key.append(vo.getVfree9());
    key.append(vo.getVfree10());
    key.append(vo.getVbatchcode());

    if ((bVendor) || ((vo.getInv().getIssupplierstock() != null) && (vo.getInv().getIssupplierstock().intValue() == 1)))
      key.append(vo.getCvendorid());
    else {
      key.append("null");
    }

    if ((vo.getIsStoreByConvert() != null) && (vo.getIsStoreByConvert().intValue() == 1))
      key.append(vo.getHsl());
    else {
      key.append("null");
    }

    return key.toString();
  }

  private String getKey(OnhandnumHeaderVO vo)
  {
    StringBuffer key = new StringBuffer();
    key.append(vo.getCwarehouseid());
    key.append(vo.getCinventoryid());
    key.append(vo.getCastunitid());
    key.append(vo.getVfree1());
    key.append(vo.getVfree2());
    key.append(vo.getVfree3());
    key.append(vo.getVfree4());
    key.append(vo.getVfree5());
    key.append(vo.getVfree6());
    key.append(vo.getVfree7());
    key.append(vo.getVfree8());
    key.append(vo.getVfree9());
    key.append(vo.getVfree10());
    key.append(vo.getVlot());

    key.append(vo.getCvendorid());
    key.append(vo.getHsl());
    return key.toString();
  }

  private OnhandnumVO[] getOnHandNumVOs(GeneralBillVO voCur, GeneralBillVO voPre)
  {
    if ((voCur == null) && (voPre == null)) {
      return null;
    }
    String key = null;
    String key1 = null;

    OnhandnumVO voOnhand = null;
    OnhandnumHeaderVO voHead = null;
    OnhandnumItemVO voItem = null;
    OnhandnumHeaderVO voHeadOld = null;
    OnhandnumItemVO voItemOld = null;

    Hashtable htOld = getHashOnHand(voPre, false);

    Hashtable htCur = getHashOnHand(voCur, true);

    Hashtable htItem = new Hashtable();

    Object oOnhand = null;
    UFDouble ZERO = new UFDouble(0);
    ArrayList alHead = new ArrayList();
    ArrayList alItem = null;

    if ((htCur != null) && (htCur.size() > 0))
    {
      Enumeration keys = htCur.keys();
      while (keys.hasMoreElements())
      {
        key = (String)keys.nextElement();
        oOnhand = htCur.get(key);

        if ((oOnhand instanceof OnhandnumHeaderVO)) {
          voHead = (OnhandnumHeaderVO)oOnhand;
          voHeadOld = (OnhandnumHeaderVO)htOld.get(key);

          if (voHeadOld != null) {
            voHead.setNonhandnum(voHead.getNonhandnum().sub(voHeadOld.getNonhandnum()));
            voHead.setNonhandastnum(voHead.getNonhandastnum().sub(voHeadOld.getNonhandastnum()));
            voHead.setNgrossnum(voHead.getNgrossnum().sub(voHeadOld.getNgrossnum()));

            htOld.remove(key);
          }

          alHead.add(voHead);
          htCur.remove(key);
        }

        if ((oOnhand instanceof OnhandnumItemVO)) {
          voItem = (OnhandnumItemVO)oOnhand;
          voItemOld = (OnhandnumItemVO)htOld.get(key);

          if (voItemOld != null) {
            voItem.setNnum(voItem.getNnum().sub(voItemOld.getNnum()));
            voItem.setNastnum(voItem.getNastnum().sub(voItemOld.getNastnum()));
            voItem.setNgrossnum(voItem.getNgrossnum().sub(voItemOld.getNgrossnum()));
            htOld.remove(key);
          }
          if (((voItem.getNnum() != null) && (voItem.getNnum().compareTo(ZERO) != 0)) || ((voItem.getNastnum() != null) && (voItem.getNastnum().compareTo(ZERO) != 0)) || ((voItem.getNgrossnum() != null) && (voItem.getNgrossnum().compareTo(ZERO) != 0)))
          {
            key1 = key.substring(0, key.indexOf("=^-^="));
            alItem = (ArrayList)htItem.get(key1);
            if (alItem == null) {
              alItem = new ArrayList();
              htItem.put(key1, alItem);
            }
            if ((voItem.getNnum().compareTo(ZERO) != 0) || (voItem.getNastnum().compareTo(ZERO) != 0) || (voItem.getNgrossnum().compareTo(ZERO) != 0)) {
              alItem.add(voItem);
            }
          }
        }

      }

    }

    if ((htOld != null) && (htOld.size() > 0)) {
      UFDouble NEG = new UFDouble(-1);
      Enumeration keys = htOld.keys();
      while (keys.hasMoreElements()) {
        key = (String)keys.nextElement();
        oOnhand = htOld.get(key);

        if ((oOnhand instanceof OnhandnumHeaderVO)) {
          voHead = (OnhandnumHeaderVO)oOnhand;
          if (voHead.getNonhandnum() != null)
            voHead.setNonhandnum(voHead.getNonhandnum().multiply(NEG));
          if (voHead.getNonhandastnum() != null)
            voHead.setNonhandastnum(voHead.getNonhandastnum().multiply(NEG));
          if (voHead.getNgrossnum() != null)
            voHead.setNgrossnum(voHead.getNgrossnum().multiply(NEG));
          alHead.add(voHead);
        }

        if ((oOnhand instanceof OnhandnumItemVO)) {
          voItem = (OnhandnumItemVO)oOnhand;
          if (voItem.getNnum() != null)
            voItem.setNnum(voItem.getNnum().multiply(NEG));
          if ((voItem.getNastnum() != null) && (voItem.getNastnum().compareTo(ZERO) != 0))
            voItem.setNastnum(voItem.getNastnum().multiply(NEG));
          if (voItem.getNgrossnum() != null) {
            voItem.setNgrossnum(voItem.getNgrossnum().multiply(NEG));
          }

          key1 = key.substring(0, key.indexOf("=^-^="));
          alItem = (ArrayList)htItem.get(key1);
          if (alItem == null) {
            alItem = new ArrayList();
            htItem.put(key1, alItem);
          }
          if ((voItem.getNnum().compareTo(ZERO) != 0) || (voItem.getNastnum().compareTo(ZERO) != 0) || (voItem.getNgrossnum().compareTo(ZERO) != 0)) {
            alItem.add(voItem);
          }
        }
      }
    }

    ArrayList alIndexs = SortMethod.sortByKeys(m_keys, null, alHead);
    int[] indexs = (int[])alIndexs.get(0);

    ArrayList alRet = null;
    if (alHead.size() > 0) {
      alRet = new ArrayList();
      OnhandnumItemVO[] voBody = null;
      int index = 0;
      for (int i = 0; i < alHead.size(); i++) {
        index = indexs[i];
        voHead = (OnhandnumHeaderVO)alHead.get(index);
        voOnhand = new OnhandnumVO();
        voOnhand.setParentVO(voHead);
        key = getKey(voHead);
        alItem = (ArrayList)htItem.get(key);
        if ((alItem != null) && (alItem.size() > 0)) {
          voBody = new OnhandnumItemVO[alItem.size()];
          alItem.toArray(voBody);
          voOnhand.setChildrenVO(voBody);
        }

        if (((alItem != null) && (alItem.size() > 0)) || ((voHead.getNonhandnum() != null) && (voHead.getNonhandnum().compareTo(ZERO) != 0)) || ((voHead.getNonhandastnum() != null) && (voHead.getNonhandastnum().compareTo(ZERO) != 0)) || ((voHead.getNgrossnum() != null) && (voHead.getNgrossnum().compareTo(ZERO) != 0)))
        {
          alRet.add(voOnhand);
        }
      }
    }
    if ((alRet != null) && (alRet.size() > 0)) {
      OnhandnumVO[] voOnhands = new OnhandnumVO[alRet.size()];
      alRet.toArray(voOnhands);
      return voOnhands;
    }
    return null;
  }

  private String getWhereByItemVOB(OnhandnumItemVO voItem)
  {
    StringBuffer sbWhere = new StringBuffer();

    if (voItem.getCinventoryid() != null)
      sbWhere.append(" cinventoryidb='");
    sbWhere.append(voItem.getCinventoryid());
    sbWhere.append("' ");
    sbWhere.append(" AND ");
    if (voItem.getCspaceid() != null) {
      sbWhere.append(" cspaceid='");
      sbWhere.append(voItem.getCspaceid());
      sbWhere.append("'");
    } else {
      sbWhere.append(" cspaceid IS NULL ");
    }if ((voItem.getCwarehouseid() != null) && (voItem.getCwarehouseid().length() > 0)) {
      sbWhere.append(" AND cwarehouseidb = '");
      sbWhere.append(voItem.getCwarehouseid());
      sbWhere.append("'");
    }
    if ((voItem.getCcalbodyid() != null) && (voItem.getCcalbodyid().length() > 0)) {
      sbWhere.append(" AND ccalbodyidb = '");
      sbWhere.append(voItem.getCcalbodyid());
      sbWhere.append("'");
    }

    if ((voItem.getPk_corp() != null) && (voItem.getPk_corp().length() > 0)) {
      sbWhere.append(" AND pk_corp = '");
      sbWhere.append(voItem.getPk_corp());
      sbWhere.append("'");
    }

    sbWhere.append(" AND  ");
    if (voItem.getVlot() != null) {
      sbWhere.append(" vlotb='");
      sbWhere.append(voItem.getVlot());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vlotb IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getHsl() != null) {
      sbWhere.append(" hsl=");
      sbWhere.append(voItem.getHsl());
    } else {
      sbWhere.append(" hsl IS NULL ");
    }
    sbWhere.append(" AND  ");
    if (voItem.getCastunitid() != null) {
      sbWhere.append(" castunitidb='");
      sbWhere.append(voItem.getCastunitid());
      sbWhere.append("'");
    } else {
      sbWhere.append(" castunitidb IS NULL ");
    }
    sbWhere.append(" AND  ");
    if (voItem.getCvendorid() != null) {
      sbWhere.append(" cvendorid='");
      sbWhere.append(voItem.getCvendorid());
      sbWhere.append("'");
    } else {
      sbWhere.append(" cvendorid IS NULL ");
    }

    sbWhere.append(" AND  ");
    if (voItem.getVfree1() != null) {
      sbWhere.append(" vfreeb1='");
      sbWhere.append(voItem.getVfree1());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb1 IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getVfree2() != null) {
      sbWhere.append(" vfreeb2='");
      sbWhere.append(voItem.getVfree2());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb2 IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getVfree3() != null) {
      sbWhere.append(" vfreeb3='");
      sbWhere.append(voItem.getVfree3());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb3 IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getVfree4() != null) {
      sbWhere.append(" vfreeb4='");
      sbWhere.append(voItem.getVfree4());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb4 IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getVfree5() != null) {
      sbWhere.append(" vfreeb5='");
      sbWhere.append(voItem.getVfree5());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb5 IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getVfree6() != null) {
      sbWhere.append(" vfreeb6='");
      sbWhere.append(voItem.getVfree6());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb6 IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getVfree7() != null) {
      sbWhere.append(" vfreeb7='");
      sbWhere.append(voItem.getVfree7());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb7 IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getVfree8() != null) {
      sbWhere.append(" vfreeb8='");
      sbWhere.append(voItem.getVfree8());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb8 IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getVfree9() != null) {
      sbWhere.append(" vfreeb9='");
      sbWhere.append(voItem.getVfree9());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb9 IS NULL ");
    }sbWhere.append(" AND  ");
    if (voItem.getVfree10() != null) {
      sbWhere.append(" vfreeb10='");
      sbWhere.append(voItem.getVfree10());
      sbWhere.append("'");
    } else {
      sbWhere.append(" vfreeb10 IS NULL");
    }
    sbWhere.append(" AND dr=0");
    return sbWhere.toString();
  }

  private boolean isBorrow(GeneralBillVO vo)
  {
    boolean isBL = false;
    if ((vo != null) && (vo.getHeaderVO() != null)) {
      String billtype = vo.getHeaderVO().getCbilltypecode();
      if ((BillTypeConst.m_initBorrow.equals(billtype)) || (BillTypeConst.m_borrowIn.equals(billtype)) || (BillTypeConst.m_borrowOut.equals(billtype)))
      {
        isBL = true;
      }
    }
    return isBL;
  }

  private boolean isChgDate(UFDate d1, UFDate d2)
  {
    if ((d1 == null) && (d2 == null)) {
      return false;
    }
    if ((d1 == null) && (d2 != null)) {
      return true;
    }
    if ((d1 != null) && (d2 == null)) {
      return true;
    }
    if (d1.toString().equals(d2.toString())) {
      return false;
    }
    return true;
  }

  private boolean isLend(GeneralBillVO vo)
  {
    boolean isBL = false;
    if ((vo != null) && (vo.getHeaderVO() != null)) {
      String billtype = vo.getHeaderVO().getCbilltypecode();
      if ((BillTypeConst.m_initLend.equals(billtype)) || (BillTypeConst.m_lendIn.equals(billtype)) || (BillTypeConst.m_lendOut.equals(billtype)))
      {
        isBL = true;
      }
    }
    return isBL;
  }

  public void modifyOnhandNum(GeneralBillVO voBill, GeneralBillVO voDbBill)
    throws Exception
  {
    MonthServ ms = new MonthServ();
    ms.modifyMonthData(voBill, voDbBill);

    Timer timer = new Timer();
    timer.start("@@@@modifyOnhandNum Start:");

    String sLogdate = null;
    if ((voBill != null) && (voBill.getItemVOs() != null) && (voBill.getItemVOs().length > 0)) {
      sLogdate = voBill.getHeaderVO().getClogdatenow();
      if ((voBill.getItemVOs()[0].getAttributeValue("ischeckatp") != null) && (!((UFBoolean)voBill.getItemVOs()[0].getAttributeValue("ischeckatp")).booleanValue()))
      {
        this.isCheckAtp = false;
      }
      if ((voBill.getItemVOs()[0].getCsourcetype() != null) && (!voBill.getItemVOs()[0].getCsourcetype().startsWith("4")))
        this.isCheckAtp = false;
    } else if ((voDbBill != null) && (voDbBill.getItemVOs() != null) && (voDbBill.getItemVOs().length > 0)) {
      sLogdate = voDbBill.getHeaderVO().getClogdatenow();
      if ((voDbBill.getItemVOs()[0].getAttributeValue("ischeckatp") != null) && (!((UFBoolean)voDbBill.getItemVOs()[0].getAttributeValue("ischeckatp")).booleanValue()))
      {
        this.isCheckAtp = false;
      }if ((voDbBill.getItemVOs()[0].getCsourcetype() != null) && (!voDbBill.getItemVOs()[0].getCsourcetype().startsWith("4"))) {
        this.isCheckAtp = false;
      }
    }
    OnhandnumVO[] vos = getOnHandNumVOs(voBill, voDbBill);
    timer.showExecuteTime("@@@@:汇总新单据数量，不管删除的行");
    modifyOnhandNumDirectly(vos);

    if ((vos == null) || (vos.length == 0) || (!this.isCheckAtp)) {
      return;
    }
    InvATPDMO dmo2 = new InvATPDMO();

    if (!"不通过".equals(dmo2.getICParam(vos[0].getHeaderVO().getPk_corp()))) {
      return;
    }
    ATPVO voAtp = null;
    UFDouble nonhand = null;
    UFDouble ZERO = new UFDouble(0.0D);
    ArrayList alvos = new ArrayList();
    InvOnHandDMO dmo1 = new InvOnHandDMO();

    for (int i = 0; i < vos.length; i++) {
      nonhand = vos[i].getHeaderVO().getNonhandnum();
      if ((nonhand != null) && (nonhand.compareTo(ZERO) < 0))
      {
        if (vos[i].getHeaderVO().getCcalbodyid() != null)
        {
          if (dmo1.isAffectAtp(vos[i].getHeaderVO().getCwarehouseid()).booleanValue())
          {
            voAtp = new ATPVO();
            voAtp.setPk_corp(vos[i].getHeaderVO().getPk_corp());
            voAtp.setCcalbodyid(vos[i].getHeaderVO().getCcalbodyid());
            voAtp.setCinventoryid(vos[i].getHeaderVO().getCinventoryid());
            voAtp.setVfree(new String[] { vos[i].getHeaderVO().getVfree1(), vos[i].getHeaderVO().getVfree2(), vos[i].getHeaderVO().getVfree3(), vos[i].getHeaderVO().getVfree4(), vos[i].getHeaderVO().getVfree5() });

            voAtp.setCwarehouseid(vos[i].getHeaderVO().getCwarehouseid());
            voAtp.setVbatchcode(vos[i].getHeaderVO().getVlot());

            alvos.add(voAtp);
          }
        }
      }
    }
    if ((alvos.size() > 0) && (this.isCheckAtp))
    {
      ATPVO[] voAtps = new ATPVO[alvos.size()];
      alvos.toArray(voAtps);
      InvATPDMO dmo = new InvATPDMO();
      dmo.checkATP(voAtps, sLogdate);
    }
  }

  public void modifyOnhandNumBrwLnd(GeneralBillVO voBill, GeneralBillVO voDbBill)
    throws SQLException, SystemException, BusinessException
  {
    boolean isBrw = false;
    boolean isLnd = false;
    if (voBill != null) {
      isBrw = isBorrow(voBill);
      isLnd = isLend(voBill);
    } else {
      isBrw = isBorrow(voDbBill);
      isLnd = isLend(voDbBill);
    }
    if ((!isBrw) && (!isLnd)) {
      return;
    }

    OnhandnumVO[] vos = getOnHandNumVOs(voBill, voDbBill);
    if ((vos == null) || (vos.length == 0)) {
      return;
    }
    for (int i = 0; i < vos.length; i++) {
      vos[i].setChildrenVO(null);
      if (isBrw) {
        vos[i].getHeaderVO().setNnum1(vos[i].getHeaderVO().getNonhandnum());
        vos[i].getHeaderVO().setNastnum1(vos[i].getHeaderVO().getNonhandastnum());
        vos[i].getHeaderVO().setNgrossnum1(vos[i].getHeaderVO().getNgrossnum());
      }
      if (isLnd) {
        if (vos[i].getHeaderVO().getNonhandnum() != null)
          vos[i].getHeaderVO().setNnum2(vos[i].getHeaderVO().getNonhandnum().multiply(-1.0D));
        if (vos[i].getHeaderVO().getNonhandastnum() != null)
          vos[i].getHeaderVO().setNastnum2(vos[i].getHeaderVO().getNonhandastnum().multiply(-1.0D));
        if (vos[i].getHeaderVO().getNgrossnum() != null) {
          vos[i].getHeaderVO().setNgrossnum2(vos[i].getHeaderVO().getNgrossnum().multiply(-1.0D));
        }
      }
      vos[i].getHeaderVO().setNonhandnum(null);
      vos[i].getHeaderVO().setNonhandastnum(null);
      vos[i].getHeaderVO().setNgrossnum(null);
    }

    modifyOnhandNumDirectly(vos);
  }

  public void modifyOnhandNumDirectly(OnhandnumVO[] vos)
    throws SQLException, SystemException, BusinessException
  {
    Timer timer = new Timer();
    timer.start("@@@@modifyOnhandNum Start:");
    OnhandnumItemVO[] voItems = null;
    ArrayList alnewid = new ArrayList();
    if ((vos != null) && (vos.length > 0)) {
      for (int i = 0; i < vos.length; i++) {
        if (updateHeader(vos[i].getHeaderVO()) == 0) {
          try {
            insertHeader(vos[i].getHeaderVO());
          } catch (Exception e) {
            if (updateHeader(vos[i].getHeaderVO()) == 0) {
              throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000220") + e.getMessage());
            }
          }
        }

        voItems = vos[i].getItemVOs();
        if ((voItems != null) && (voItems.length > 0)) {
          for (int j = 0; j < voItems.length; j++)
          {
            if (updateItem(voItems[j]) == 0) {
              try {
                alnewid.add(insertItem(voItems[j]));
              } catch (Exception e) {
                if (updateItem(voItems[j]) == 0) {
                  throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000221"));
                }
              }
            }
          }
        }
      }

    }

    if (alnewid.size() > 0) {
      Connection con = null;
      PreparedStatement stmtUpdatePK = null;
      try
      {
        con = getConnection();
        stmtUpdatePK = con.prepareStatement(this.sqlUpdatePK);
        stmtUpdatePK.executeUpdate();
        timer.showExecuteTime(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000222"));
      } finally {
        try {
          if (stmtUpdatePK != null)
            stmtUpdatePK.close();
        }
        catch (Exception e) {
        }
        try {
          if (con != null)
            con.close();
        }
        catch (Exception e)
        {
        }
      }
    }
  }

  public Hashtable queryBusitype(String sPk_corp)
    throws SQLException
  {
    String sql = "SELECT pk_busitype FROM bd_busitype WHERE (verifyrule='J' OR  verifyrule='C') AND pk_corp=?";

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Hashtable htBiztype = new Hashtable();
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      stmt.setString(1, sPk_corp);
      rs = stmt.executeQuery();

      String sBusitype = null;
      while (rs.next()) {
        sBusitype = rs.getString(1);
        if ((sBusitype != null) && (!htBiztype.containsKey(htBiztype)))
          htBiztype.put(sBusitype, "");
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    return htBiztype;
  }

  public void synOnhandNum()
    throws BusinessException
  {
    if (!lockSyn()) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000224"));
    }

    try
    {
      updateStatus(true);

      OnhandnumVO[] vos = queryOnhandVO(new String[] { "ic_onhandnum_f", "ic_onhandnum_bf" }, new String[] { " istatus=1 ", " istatus=1 " });
      modifyOnhandNumDirectly(vos);
      updateStatus(false);
    }
    catch (Exception e) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000225") + e.getMessage());
    }
    finally
    {
      freeSyn();
    }
  }

  private boolean lockSyn()
  {
    boolean isOk = true;
    try {
      updateCtrl(true);
    }
    catch (Exception e) {
      isOk = false;
    }
    return isOk;
  }

  private boolean freeSyn() {
    boolean isOk = true;
    try {
      updateCtrl(false);
    }
    catch (Exception e) {
      isOk = false;
    }
    return isOk;
  }

  private void updateCtrl(boolean isY) throws SQLException
  {
    String sql = "update ic_onhandnum_ctrl set bstarted=? where pk_onhandnum_ctrl='SCMICONHANDCTRL00001' and bstarted=?  ";
    Connection con = null;
    PreparedStatement stmt = null;
    try {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (isY) {
        stmt.setString(1, "Y");
        stmt.setString(2, "N");
      }
      else {
        stmt.setString(1, "N");
        stmt.setString(2, "Y");
      }
      int count = stmt.executeUpdate();
      if (count != 1)
        throw new SQLException(NCLangResOnserver.getInstance().getStrByID("4008bill", "UPP4008bill-000226"));
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  private void updateStatus(boolean isStart)
    throws SQLException
  {
    String sql = "update ic_onhandnum_f set istatus=? where istatus=?";
    String sql2 = "update ic_onhandnum_bf set istatus=? where istatus=?";

    String sqldel = "delete ic_onhandnum_f where istatus=2";
    String sqldel2 = "delete ic_onhandnum_bf where istatus=2";

    Connection con = null;
    PreparedStatement stmt = null;
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql);
      if (isStart) {
        stmt.setInt(1, 1);
        stmt.setInt(2, 0);
      }
      else {
        stmt.setInt(1, 2);
        stmt.setInt(2, 1);
      }
      stmt.executeUpdate();

      if (stmt != null) {
        stmt.close();
      }
      stmt = con.prepareStatement(sql2);
      if (isStart) {
        stmt.setInt(1, 1);
        stmt.setInt(2, 0);
      }
      else {
        stmt.setInt(1, 2);
        stmt.setInt(2, 1);
      }
      stmt.executeUpdate();
      if (stmt != null) {
        stmt.close();
      }
      if (!isStart) {
        stmt = con.prepareStatement(sqldel);
        stmt.executeUpdate();
        stmt = con.prepareStatement(sqldel2);
        stmt.executeUpdate();
      }
    }
    finally
    {
      try
      {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  public OnhandnumVO[] queryOnhandVO(String[] tables, String[] sWheres)
    throws SQLException
  {
    if ((tables == null) || (tables.length != 1)) {
      return null;
    }
    String headwhere = null;
    String bodywhere = null;
    if ((sWheres != null) && (sWheres.length > 0))
      headwhere = sWheres[0];
    if ((sWheres != null) && (sWheres.length > 1)) {
      bodywhere = sWheres[1];
    }

    OnhandnumVO[] vos = null;
    OnhandnumHeaderVO[] voheads = queryOnhandHeaderVO(tables[0], headwhere);
    OnhandnumItemVO[] voItems = queryOnhandItemVO(tables[1], bodywhere);
    if ((voheads != null) && (voheads.length > 0)) {
      vos = new OnhandnumVO[voheads.length];
      for (int i = 0; i < voheads.length; i++) {
        vos[i] = new OnhandnumVO();
        vos[i].setParentVO(voheads[i]);
      }
      if ((voItems != null) && (voItems.length > 0)) {
        vos[(voheads.length - 1)].setChildrenVO(voItems);
      }
    }
    return vos;
  }

  public OnhandnumHeaderVO[] queryOnhandHeaderVO(String tablename, String where)
    throws SQLException
  {
    String[] fields = { "pk_corp", "ccalbodyid", "cwarehouseid", "cinventoryid", "vlot", "dvalidate", "castunitid", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9", "vfree10", "cvendorid", "hsl" };
    String[] fieldnums = { "nonhandnum", "nonhandastnum", "nnum1", "nastnum1", "nnum2", "nastnum2", "ngrossnum1", "ngrossnum2" };
    StringBuffer sql = new StringBuffer("select ");
    StringBuffer select = new StringBuffer();
    for (int i = 0; i < fields.length; i++) {
      select.append(fields[i]);
      if (i < fields.length - 1) {
        select.append(",");
      }
    }
    sql.append(select).append(" ,sum(isnull(nonhandnum,0.0)) as nonhandnum, sum(isnull(nonhandastnum,0.0)) as nonhandastnum, sum(isnull(nnum1,0.0)) as nnum1, sum(isnull(nastnum1,0.0)) as nastnum1, sum(isnull(nnum2,0.0)) as nnum2, sum(isnull(nastnum2,0.0)) as nastnum2,sum(isnull(ngrossnum1,0.0)) as ngrossnum1,sum(isnull(ngrossnum2,0.0)) as ngrossnum2 ").append(" FROM " + tablename);

    if (where != null) {
      sql.append(" WHERE 0=0 " + where);
    }
    sql.append(" group by ").append(select);

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList alvo = new ArrayList();
    try
    {
      con = getConnection();

      stmt = con.prepareStatement(sql.toString());

      rs = stmt.executeQuery();

      OnhandnumHeaderVO voHeader = null;
      while (rs.next()) {
        voHeader = new OnhandnumHeaderVO();
        for (int i = 1; i <= fields.length; i++) {
          voHeader.setAttributeValue(fields[(i - 1)], rs.getString(i));
        }
        for (int i = fields.length + 1; i <= fields.length + fieldnums.length; i++) {
          voHeader.setAttributeValue(fieldnums[(i - fields.length - 1)], rs.getString(i));
        }
        alvo.add(voHeader);
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    OnhandnumHeaderVO[] vos = null;
    if (alvo.size() > 0) {
      vos = new OnhandnumHeaderVO[alvo.size()];
      alvo.toArray(vos);
    }

    return vos;
  }

  public OnhandnumItemVO[] queryOnhandItemVO(String tablename, String where)
    throws SQLException
  {
    String[] fields = { "ccalbodyidb", "cwarehouseidb", "cinventoryidb", "cspaceid", "vlotb", "castunitidb", "vfreeb1", "vfreeb2", "vfreeb3", "vfreeb4", "vfreeb5", "vfreeb6", "vfreeb7", "vfreeb8", "vfreeb9", "vfreeb10", "cvendorid", "hsl" };
    String[] field2s = { "ccalbodyid", "cwarehouseid", "cinventoryid", "cspaceid", "vlot", "castunitid", "vfree1", "vfree2", "vfree3", "vfree4", "vfree5", "vfree6", "vfree7", "vfree8", "vfree9", "vfree10", "cvendorid", "hsl" };
    String[] fieldnums = { "nnum", "nastnum" };
    StringBuffer sql = new StringBuffer("select ");
    StringBuffer select = new StringBuffer();
    for (int i = 0; i < fields.length; i++) {
      select.append(fields[i] + " as " + field2s[i]);
      if (i < fields.length - 1) {
        select.append(",");
      }
    }
    StringBuffer group = new StringBuffer();
    for (int i = 0; i < fields.length; i++) {
      group.append(fields[i]);
      if (i < fields.length - 1) {
        group.append(",");
      }
    }
    sql.append(select).append(" ,sum(isnull(nnum,0.0)) as nnum, sum(isnull(nastnum,0.0)) as nastnum ").append(" FROM " + tablename);

    if (where != null) {
      sql.append(" WHERE " + where);
    }
    sql.append(" group by ").append(group);

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList alvo = new ArrayList();
    try
    {
      con = getConnection();
      stmt = con.prepareStatement(sql.toString());

      rs = stmt.executeQuery();

      OnhandnumItemVO voItem = null;
      while (rs.next()) {
        voItem = new OnhandnumItemVO();
        for (int i = 1; i <= field2s.length; i++) {
          voItem.setAttributeValue(field2s[(i - 1)], rs.getString(i));
        }
        for (int i = field2s.length + 1; i <= field2s.length + fieldnums.length; i++) {
          voItem.setAttributeValue(fieldnums[(i - field2s.length - 1)], rs.getString(i));
        }
        alvo.add(voItem);
      }
    } finally {
      try {
        if (rs != null)
          rs.close();
      }
      catch (Exception e) {
      }
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e)
      {
      }
    }
    OnhandnumItemVO[] vos = null;
    if (alvo.size() > 0) {
      vos = new OnhandnumItemVO[alvo.size()];
      alvo.toArray(vos);
    }

    return vos;
  }

  private int updateHeader(OnhandnumHeaderVO voHead)
    throws SQLException, BusinessException, SystemException
  {
    StringBuffer sbUpdateSQL = new StringBuffer();
    String sUpdateSQL = "UPDATE ic_onhandnum SET  nonhandnum = COALESCE(nonhandnum,0.0)+?, nonhandastnum = COALESCE(nonhandastnum,0.0)+? , nnum1 = COALESCE(nnum1,0.0)+?, nastnum1 = COALESCE(nastnum1,0.0)+?,nnum2 = COALESCE(nnum2,0.0)+?, nastnum2 = COALESCE(nastnum2,0.0)+?,ngrossnum = COALESCE(ngrossnum,0.0)+?,ngrossnum1 = COALESCE(ngrossnum1,0.0)+?,ngrossnum2 = COALESCE(ngrossnum2,0.0)+?";

    StringBuffer sbWhere = new StringBuffer();
    sbWhere.append(" cinventoryid=? and  cwarehouseid =? and ccalbodyid =? ");

    if (voHead.getVlot() != null)
      sbWhere.append(" and vlot=? ");
    else {
      sbWhere.append(" and vlot IS NULL ");
    }
    if (voHead.getCastunitid() != null)
      sbWhere.append(" and castunitid=? ");
    else {
      sbWhere.append(" and castunitid IS NULL ");
    }
    if (voHead.getCvendorid() != null)
      sbWhere.append(" and cvendorid=? ");
    else {
      sbWhere.append(" and cvendorid IS NULL ");
    }
    if (voHead.getHsl() != null)
      sbWhere.append(" and abs(hsl-?)<0.000000001 ");
    else {
      sbWhere.append(" and hsl IS NULL ");
    }

    if (voHead.getVfree1() != null)
      sbWhere.append(" and vfree1=? ");
    else {
      sbWhere.append(" and vfree1 IS NULL ");
    }
    if (voHead.getVfree2() != null)
      sbWhere.append(" and vfree2=? ");
    else {
      sbWhere.append(" and vfree2 IS NULL ");
    }
    if (voHead.getVfree3() != null)
      sbWhere.append(" and vfree3=? ");
    else {
      sbWhere.append(" and vfree3 IS NULL ");
    }
    if (voHead.getVfree4() != null)
      sbWhere.append("and vfree4=? ");
    else {
      sbWhere.append(" and vfree4 IS NULL ");
    }
    if (voHead.getVfree5() != null)
      sbWhere.append(" and vfree5=? ");
    else {
      sbWhere.append("and vfree5 IS NULL ");
    }
    if (voHead.getVfree6() != null)
      sbWhere.append("and vfree6=? ");
    else {
      sbWhere.append(" and vfree6 IS NULL ");
    }
    if (voHead.getVfree7() != null)
      sbWhere.append("and vfree7=? ");
    else {
      sbWhere.append(" and vfree7 IS NULL ");
    }
    if (voHead.getVfree8() != null)
      sbWhere.append(" and vfree8=? ");
    else {
      sbWhere.append(" and vfree8 IS NULL ");
    }
    if (voHead.getVfree9() != null)
      sbWhere.append(" and vfree9=? ");
    else {
      sbWhere.append("and  vfree9 IS NULL ");
    }
    if (voHead.getVfree10() != null)
      sbWhere.append(" and vfree10=? ");
    else {
      sbWhere.append("and  vfree10 IS NULL");
    }
    sbWhere.append(" AND dr=0");

    CrossDBConnection con = null;
    PreparedStatement stmt = null;
    int iResult = 0;
    try {
      con = (CrossDBConnection)getConnection();
      sbUpdateSQL.append(sUpdateSQL);
      sbUpdateSQL.append(" where ").append(sbWhere);

      stmt = con.prepareStatement(sbUpdateSQL.toString());

      int iINDEX = 1;

      GenMethod.setStmtBigDecimalZero(stmt, voHead.getNonhandnum(), iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, voHead.getNonhandastnum(), iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, voHead.getNnum1(), iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, voHead.getNastnum1(), iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, voHead.getNnum2(), iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, voHead.getNastnum2(), iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, voHead.getNgrossnum(), iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, voHead.getNgrossnum1(), iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, voHead.getNgrossnum2(), iINDEX++);

      GenMethod.setStmtString(stmt, voHead.getCinventoryid(), iINDEX++);
      GenMethod.setStmtString(stmt, voHead.getCwarehouseid(), iINDEX++);
      GenMethod.setStmtString(stmt, voHead.getCcalbodyid(), iINDEX++);

      if (voHead.getVlot() != null) {
        GenMethod.setStmtString(stmt, voHead.getVlot(), iINDEX++);
      }
      if (voHead.getCastunitid() != null) {
        GenMethod.setStmtString(stmt, voHead.getCastunitid(), iINDEX++);
      }
      if (voHead.getCvendorid() != null) {
        GenMethod.setStmtString(stmt, voHead.getCvendorid(), iINDEX++);
      }
      if (voHead.getHsl() != null) {
        GenMethod.setStmtBigDecimal(stmt, voHead.getHsl(), iINDEX++);
      }
      if (voHead.getVfree1() != null)
        GenMethod.setStmtString(stmt, voHead.getVfree1(), iINDEX++);
      if (voHead.getVfree2() != null)
        GenMethod.setStmtString(stmt, voHead.getVfree2(), iINDEX++);
      if (voHead.getVfree3() != null)
        GenMethod.setStmtString(stmt, voHead.getVfree3(), iINDEX++);
      if (voHead.getVfree4() != null)
        GenMethod.setStmtString(stmt, voHead.getVfree4(), iINDEX++);
      if (voHead.getVfree5() != null)
        GenMethod.setStmtString(stmt, voHead.getVfree5(), iINDEX++);
      if (voHead.getVfree6() != null)
        GenMethod.setStmtString(stmt, voHead.getVfree6(), iINDEX++);
      if (voHead.getVfree7() != null)
        GenMethod.setStmtString(stmt, voHead.getVfree7(), iINDEX++);
      if (voHead.getVfree8() != null)
        GenMethod.setStmtString(stmt, voHead.getVfree8(), iINDEX++);
      if (voHead.getVfree9() != null)
        GenMethod.setStmtString(stmt, voHead.getVfree9(), iINDEX++);
      if (voHead.getVfree10() != null) {
        GenMethod.setStmtString(stmt, voHead.getVfree10(), iINDEX++);
      }

      iResult = stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return iResult;
  }

  private int updateItem(OnhandnumItemVO voItem)
    throws SQLException, BusinessException, SystemException
  {
    StringBuffer sbUpdateSQL = new StringBuffer();
    String sUpdateSQL = "UPDATE ic_onhandnum_b SET  nnum = COALESCE(nnum,0.0)+?, nastnum = COALESCE(nastnum,0.0)+?,ngrossnum=COALESCE(ngrossnum,0.0)+? WHERE ";

    String sWhere = null;

    UFDouble ufdnum = voItem.getNnum();
    UFDouble ufdastnum = voItem.getNastnum();
    UFDouble ufdgrossnum = voItem.getNgrossnum();
    Connection con = null;
    PreparedStatement stmt = null;

    int iResult = 0;
    try {
      con = getConnection();

      sbUpdateSQL.append(sUpdateSQL);

      sWhere = getWhereByItemVOB(voItem);
      sbUpdateSQL.append(sWhere);

      stmt = con.prepareStatement(sbUpdateSQL.toString());

      int iINDEX = 1;
      GenMethod.setStmtBigDecimalZero(stmt, ufdnum, iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, ufdastnum, iINDEX++);
      GenMethod.setStmtBigDecimalZero(stmt, ufdgrossnum, iINDEX++);

      iResult = stmt.executeUpdate();
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
      }
      catch (Exception e) {
      }
      try {
        if (con != null)
          con.close();
      }
      catch (Exception e) {
      }
    }
    return iResult;
  }
}