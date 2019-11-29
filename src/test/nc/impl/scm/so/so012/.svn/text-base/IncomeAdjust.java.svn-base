package nc.impl.scm.so.so012;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import javax.naming.NamingException;
import nc.bs.pub.SystemException;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.impl.scm.so.pub.GeneralSqlString;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.credit.CreditUtil;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so012.SquareHeaderVO;
import nc.vo.so.so012.SquareItemVO;
import nc.vo.so.so012.SquareVO;

public class IncomeAdjust extends SmartDMO
{
  public static final int UN_STATE = -1;
  public static final int UNEND_UNEND = 1;
  public static final int UNEND_END = 2;
  public static final int END_END = 4;
  public static final int END_UNEND = 8;

  public IncomeAdjust()
    throws NamingException, SystemException
  {
  }

  public void adjustIncomeVO(DJZBVO ysvo, SquareVO invo)
    throws BusinessException, SQLException
  {
    if ((invo == null) || (ysvo == null)) {
      return;
    }
    SquareHeaderVO inHeader = (SquareHeaderVO)invo.getParentVO();
    SquareItemVO[] inItems = (SquareItemVO[])invo.getChildrenVO();
    DJZBHeaderVO ysheader = (DJZBHeaderVO)ysvo.getParentVO();

    if ((inHeader.getBEstimation() != null) && (inHeader.getBEstimation().booleanValue()))
    {
      ysheader.setZgyf(new Integer(1));
    }

    setqxrq(ysvo, invo);
  }

  public void setqxrq(DJZBVO ysvo, SquareVO invo)
    throws BusinessException, SQLException
  {
    if ((invo == null) || (ysvo == null))
      return;
    SquareHeaderVO inHeader = (SquareHeaderVO)invo.getParentVO();

    if (!"32".equals(inHeader.getCreceipttype())) {
      return;
    }
    DJZBItemVO[] ysitems = (DJZBItemVO[])ysvo.getChildrenVO();

    UFDate dbilldate = inHeader.getDbilldate();
    for (int i = 0; i < ysitems.length; i++)
      ysitems[i].setQxrq(dbilldate);
  }

  public String getCfapiaobid(SquareItemVO item, String billtype)
  {
    if ((item == null) || (billtype == null))
      return null;
    if ("4C".equals(billtype)) {
      if ("32".equalsIgnoreCase(item.getCupreceipttype())) {
        return item.getCupbillbodyid();
      }
      return null;
    }if ("32".equals(billtype)) {
      return item.getPrimaryKey();
    }
    return null;
  }

  private HashMap getDDFPCK(SquareVO invo) throws SQLException
  {
    SquareHeaderVO inHeader = (SquareHeaderVO)invo.getParentVO();
    SquareItemVO[] inItems = (SquareItemVO[])invo.getChildrenVO();
    String sBillType = inHeader.getCreceipttype();
    HashMap result = new HashMap(inItems.length);
    ArrayList list = new ArrayList(inItems.length);
    for (int i = 0; i < inItems.length; i++) {
      if ("32".equals(sBillType)) {
        if ("4C".equalsIgnoreCase(inItems[i].getCupreceipttype()))
          list.add(inItems[i].getCupbillid());
      }
      else if ("4C".equals(sBillType))
        list.add(inItems[i].getCsaleid());
    }
    HashMap map = null;
    if (list.size() > 0)
    {
      String[] cgeneralhids = new String[list.size()];
      list.toArray(cgeneralhids);
      map = getvbillcode(cgeneralhids);
    }
    if (map == null) {
      map = new HashMap(1);
    }
    for (int i = 0; i < inItems.length; i++) {
      String[] ss = { getCdingdanbid(inItems[i]), getCfapiaobid(inItems[i], sBillType), getCchukubid(inItems[i], sBillType), getCkcode(inItems[i], sBillType, inHeader, map) };
      result.put(inItems[i].getPrimaryKey(), ss);
    }
    return result;
  }

  public String getCchukubid(SquareItemVO item, String billtype)
  {
    if ((item == null) || (billtype == null))
      return null;
    if ("32".equals(billtype)) {
      if ("4C".equalsIgnoreCase(item.getCupreceipttype())) {
        return item.getCupbillbodyid();
      }
      return null;
    }if ("4C".equals(billtype)) {
      return item.getPrimaryKey();
    }
    return null;
  }

  public String getCkcode(SquareItemVO item, String billtype, SquareHeaderVO header, HashMap map)
  {
    if ((item == null) || (billtype == null))
      return null;
    if ("32".equals(billtype)) {
      if ("4C".equalsIgnoreCase(item.getCupreceipttype())) {
        return (String)map.get(item.getCupbillid());
      }
      return null;
    }if ("4C".equals(billtype)) {
      return (String)map.get(item.getCsaleid());
    }
    return null;
  }

  public String getCdingdanbid(SquareItemVO item)
  {
    if (item == null)
      return null;
    if ("30".equals(item.getCreceipttype())) {
      return item.getCsourcebillbodyid();
    }
    return null;
  }

  public HashMap getDbizdate(String[] corderb_ids)
    throws SQLException
  {
    StringBuffer sqlOrder = new StringBuffer();
    sqlOrder.append(" select cgeneralbid,dbizdate ");
    sqlOrder.append(" from ic_general_b ");
    sqlOrder.append(" where ");
    StringBuffer stmp = CreditUtil.getStrPKs("cgeneralbid", corderb_ids);
    if ((stmp == null) || (stmp.length() == 0))
      return new HashMap();
    sqlOrder.append(stmp);
    Object[] o = selectBy2(sqlOrder.toString());
    Object[] oneRow = (Object[])null;
    HashMap result = null;
    if ((o != null) && (o.length > 0))
    {
      result = new HashMap(o.length);
      for (int i = 0; i < o.length; i++) {
        oneRow = (Object[])o[i];
        if ((oneRow == null) || (oneRow.length <= 1))
          continue;
        result.put(oneRow[0], oneRow[1]);
      }
    }

    return result;
  }

  public String[] getCtermprotocolid(String cbillid)
    throws SQLException
  {
    if ((cbillid == null) || (cbillid.length() < 1))
      return new String[2];
    StringBuffer sqlOrder = new StringBuffer();
    sqlOrder.append(" select ctermprotocolid,creceiptcorpid from so_sale ");
    sqlOrder.append(" where dr = 0 and csaleid = '").append(cbillid).append("'");
    Object[] o = selectBy2(sqlOrder.toString());
    if ((o != null) && (o.length > 0))
    {
      Object[] oneRow = (Object[])o[0];
      if ((oneRow != null) && (oneRow.length > 0))
        return new String[] { (String)oneRow[0], (String)oneRow[1] };
    }
    return new String[2];
  }

  public String getClbh()
  {
    return getOID();
  }

  public void endForOrder(SaleOrderVO ordervo)
    throws BusinessException, SQLException
  {
  }

  public void unEndForOrder(SaleOrderVO ordervo)
    throws BusinessException
  {
  }

  public Object[][] get4CIDsByOrderbids(String[] corderb_ids)
    throws SQLException
  {
    StringBuffer sqlOrder = new StringBuffer();
    sqlOrder.append(" select cfirstbillbid,cgeneralbid ");
    sqlOrder.append(" from ic_general_b ");
    sqlOrder.append(" where dr = 0 and ");
    sqlOrder.append(
      CreditUtil.getStrPKs("cfirstbillbid", corderb_ids));
    Object[] o = selectBy2(sqlOrder.toString());
    Object[] oneRow = (Object[])null;
    Object[][] results = (Object[][])null;

    if ((o != null) && (o.length > 0))
    {
      results = new Object[o.length][2];
      for (int i = 0; i < results.length; i++) {
        oneRow = (Object[])o[i];
        if ((oneRow != null) && (oneRow.length > 0))
        {
          results[i][0] = oneRow[0];
          results[i][1] = oneRow[1];
        }
        else
        {
          results[i][0] = "#123456789*123456789";
          results[i][1] = "#123456789*123456789";
        }
      }
    }

    return results;
  }

  public HashMap getvbillcode(String[] corderb_ids)
    throws SQLException
  {
    StringBuffer sqlOrder = new StringBuffer();
    sqlOrder.append(" select cgeneralhid,vbillcode ");
    sqlOrder.append(" from ic_general_h ");
    sqlOrder.append(" where dr = 0 and ");
    sqlOrder.append(
      CreditUtil.getStrPKs("cgeneralhid", corderb_ids));
    Object[] o = selectBy2(sqlOrder.toString());
    Object[] oneRow = (Object[])null;
    HashMap results = null;

    if ((o != null) && (o.length > 0))
    {
      results = new HashMap(o.length);
      for (int i = 0; i < o.length; i++) {
        oneRow = (Object[])o[i];
        if ((oneRow == null) || (oneRow.length <= 0))
          continue;
        results.put(oneRow[0], oneRow[1]);
      }
    }

    return results;
  }

  public void adjustIncomeVO(SquareVO invo) throws BusinessException, SQLException
  {
    if (invo == null) {
      return;
    }
    SquareHeaderVO inHeader = (SquareHeaderVO)invo.getParentVO();
    SquareItemVO[] inItems = (SquareItemVO[])invo.getChildrenVO();

    String sBillType = inHeader.getCreceipttype();
    String sUpperType = inItems[0].getCupreceipttype();

    boolean b4C = "4C".equalsIgnoreCase(sBillType == null ? "" : sBillType.trim());

    boolean b32 = "32".equalsIgnoreCase(sBillType == null ? "" : sBillType.trim());

    if ("4453".equalsIgnoreCase(sBillType == null ? "" : sBillType.trim()));
    boolean b4453 = "4C".equalsIgnoreCase(sUpperType == null ? "" : sUpperType.trim());

    HashMap map = null;

    boolean bInvoiceFirst = true;

    HashMap SO32to4C = null;

    if ((b32) || (b4453))
    {
      if (b32)
        bInvoiceFirst = CreditUtil.isInvoiceFirst(inHeader.getPk_corp(), inHeader.getCbiztype());
      if ((!bInvoiceFirst) || (b4453))
      {
        String[] ID4CS = new String[inItems.length];
        SO32to4C = new HashMap(inItems.length);
        for (int i = 0; i < inItems.length; i++) {
          ID4CS[i] = inItems[i].getCupbillbodyid();
          SO32to4C.put(inItems[i].getCorder_bid(), inItems[i].getCupbillbodyid());
        }
        map = getDbizdate(ID4CS);
      }
    } else if (b4C)
    {
      if ((map == null) && (inItems != null) && (inItems.length > 0))
        map = new HashMap(inItems.length);
      for (int i = 0; i < inItems.length; i++) {
        map.put(inItems[i].getCorder_bid(), inItems[i].getDbizdate());
      }
    }

    HashMap ddfpck = getDDFPCK(invo);
    for (int i = 0; i < inItems.length; i++) {
      UFDate bizDate = null;
      if ((b32) || (b4453)) {
        if ((map != null) && (SO32to4C != null)) {
          Object IC4CID = SO32to4C.get(inItems[i].getCorder_bid());
          if (IC4CID != null)
            bizDate = (UFDate)map.get(IC4CID);
        }
      }
      else if ((b4C) && 
        (map != null) && 
        (inItems[i].getCorder_bid() != null)) {
        bizDate = (UFDate)map.get(inItems[i].getCorder_bid());
      }

      if (bizDate == null) {
        bizDate = new UFDate(new Date());
      }
      inItems[i].setQxrq(bizDate);

      if ((ddfpck != null) && (inItems[i].getCorder_bid() != null) && (inItems[i].getCorder_bid().trim().length() > 0)) {
        String[] ss = (String[])null;

        ss = (String[])ddfpck.get(inItems[i].getCorder_bid());

        if ((ss == null) || (ss.length <= 3))
          continue;
        inItems[i].setDdhid(ss[0]);
        inItems[i].setFphid(ss[1]);
        inItems[i].setCkdid(ss[2]);

        inItems[i].setAttributeValue("ckdh", ss[3]);
      }

    }

    if ((inItems.length > 0) && (inItems[0].getQxrq() != null))
      inHeader.setQxrq(inItems[0].getQxrq());
    else
      inHeader.setQxrq(new UFDate(new Date()));
  }

  public HashMap<String, UFDouble> getWF4CCostBalNum(String[] icids) throws SQLException
  {
    HashMap result = null;
    StringBuffer sb = new StringBuffer();
    sb
      .append(" select sob.corder_bid,sum(nbalancenum) from so_square_b sob,so_square so ");
    sb.append(" where sob.csaleid= so.csaleid  ");
    sb.append(" and so.dr = 0 and sob.dr=0  and so.bcostflag = 'Y' ");

    sb.append(GeneralSqlString.formInSQL("sob.corder_bid", icids));
    sb.append(" group by sob.corder_bid ");
    Object[] o = selectBy2(sb.toString());
    if ((o != null) && (o.length > 0)) {
      result = new HashMap(o.length);
      for (int i = 0; i < o.length; i++) {
        Object[] onerow = (Object[])o[i];
        if ((onerow != null) && (onerow.length >= 1))
          result.put(CreditUtil.convertObjToString(onerow[0]), 
            CreditUtil.convertObjToUFDouble(onerow[1]));
      }
    } else {
      result = new HashMap(1);
    }
    return result;
  }

  public HashMap<String, UFDouble> getWF32CostBalNum(String[] icids)
    throws SQLException
  {
    HashMap result = null;
    StringBuffer sb = new StringBuffer();

    sb.append(" select sob.cupbillbodyid,sum(nbalancenum) from so_square_b sob,so_square so  ");
    sb.append(" where sob.csaleid= so.csaleid   ");
    sb.append(" and so.dr = 0 and sob.dr=0  and so.bcostflag = 'Y' and so.creceipttype = '32'  ");

    sb.append(GeneralSqlString.formInSQL("sob.cupbillbodyid", icids));
    sb.append(" group by sob.cupbillbodyid  ");

    Object[] o = selectBy2(sb.toString());
    if ((o != null) && (o.length > 0)) {
      result = new HashMap(o.length);
      for (int i = 0; i < o.length; i++) {
        Object[] onerow = (Object[])o[i];
        if ((onerow != null) && (onerow.length >= 1))
          result.put(CreditUtil.convertObjToString(onerow[0]), 
            CreditUtil.convertObjToUFDouble(onerow[1]));
      }
    } else {
      result = new HashMap(1);
    }
    return result;
  }

  public HashMap<String, UFDouble> getWF4453CostBalNum(String[] icids)
    throws SQLException
  {
    HashMap result = null;
    StringBuffer sb = new StringBuffer();

    sb
      .append(" select sob.cupbillbodyid,sum(nbalancenum) from so_square_b sob,so_square so  ");
    sb.append(" where sob.csaleid= so.csaleid   ");
    sb
      .append(" and so.dr = 0 and sob.dr=0  and so.bcostflag = 'Y' and so.creceipttype = '4453'  ");

    sb.append(GeneralSqlString.formInSQL("sob.cupbillbodyid", icids));
    sb.append(" group by sob.cupbillbodyid  ");

    Object[] o = selectBy2(sb.toString());
    if ((o != null) && (o.length > 0)) {
      result = new HashMap(o.length);
      for (int i = 0; i < o.length; i++) {
        Object[] onerow = (Object[])o[i];
        if ((onerow != null) && (onerow.length >= 1))
          result.put(CreditUtil.convertObjToString(onerow[0]), 
            CreditUtil.convertObjToUFDouble(onerow[1]));
      }
    } else {
      result = new HashMap(1);
    }
    return result;
  }

  public void checkWFCostNum(String[] icids)
    throws SQLException, BusinessException
  {
    if ((icids == null) || (icids.length <= 0))
      return;
    HashMap CostNum4C = getWF4CCostBalNum(icids);
    HashMap CostNum32 = getWF32CostBalNum(icids);
    HashMap CostNum4453 = getWF4453CostBalNum(icids);
    for (int i = 0; i < icids.length; i++) {
      UFDouble num1 = CreditUtil.convertObjToUFDouble(CostNum4C.get(icids[i]));
      UFDouble num2 = CreditUtil.convertObjToUFDouble(CostNum32.get(icids[i]));
      UFDouble num3 = CreditUtil.convertObjToUFDouble(CostNum4453.get(icids[i]));
      if (num1.compareTo(CreditUtil.ZERO) > 0) {
        if (num3.add(num1).compareTo(num2) < 0) {
          throw new BusinessException("分期收款委托代销明细帐余额不足，请先做销售发票反结算或录入红冲发票进行结算");
        }

      }
      else if (num3.add(num1).compareTo(num2) > 0)
        throw new BusinessException("分期收款委托代销明细帐余额不足，请先做销售发票反结算或录入红冲发票进行结算");
    }
  }

  public HashMap<String, UFDouble> getAdjustSubNum(String[] icids)
    throws SQLException
  {
    HashMap result = null;
    StringBuffer sb = new StringBuffer();

    sb
      .append(" select sob.cupbillbodyid,sum(case when so.creceipttype = '32' then nbalancenum else -nbalancenum end) from so_square_b sob,so_square so  ");
    sb.append(" where sob.csaleid= so.csaleid   ");
    sb
      .append(" and so.dr = 0 and sob.dr=0  and so.bincomeflag = 'Y' and so.creceipttype in ('32','4453')  ");

    sb.append(GeneralSqlString.formInSQL("sob.cupbillbodyid", icids));
    sb.append(" group by sob.cupbillbodyid  ");

    Object[] o = selectBy2(sb.toString());
    if ((o != null) && (o.length > 0)) {
      result = new HashMap(o.length);
      for (int i = 0; i < o.length; i++) {
        Object[] onerow = (Object[])o[i];
        if ((onerow != null) && (onerow.length >= 1))
          result.put(CreditUtil.convertObjToString(onerow[0]), 
            CreditUtil.convertObjToUFDouble(onerow[1]));
      }
    } else {
      result = new HashMap(1);
    }return result;
  }

  public HashMap<String, UFDouble> getAdjust4CBalNum(String[] icids)
    throws SQLException
  {
    HashMap result = null;
    StringBuffer sb = new StringBuffer();
    sb
      .append(" select sob.corder_bid,sum(nbalancenum) from so_square_b sob,so_square so ");
    sb.append(" where sob.csaleid= so.csaleid  ");
    sb.append(" and so.dr = 0 and sob.dr=0  and so.bincomeflag = 'Y' ");

    sb.append(GeneralSqlString.formInSQL("sob.corder_bid", icids));
    sb.append(" group by sob.corder_bid ");
    Object[] o = selectBy2(sb.toString());
    if ((o != null) && (o.length > 0)) {
      result = new HashMap(o.length);
      for (int i = 0; i < o.length; i++) {
        Object[] onerow = (Object[])o[i];
        if ((onerow != null) && (onerow.length >= 1))
          result.put(CreditUtil.convertObjToString(onerow[0]), 
            CreditUtil.convertObjToUFDouble(onerow[1]));
      }
    } else {
      result = new HashMap(1);
    }return result;
  }

  public ArrayList checkAdjust(SquareVO invo)
    throws BusinessException, SQLException
  {
    ArrayList result = new ArrayList(3);
    if ((invo == null) || (invo.getParentVO() == null) || 
      (invo.getChildrenVO() == null) || 
      (invo.getChildrenVO().length < 1))
      return result;
    SquareHeaderVO headerVO = (SquareHeaderVO)invo.getParentVO();
    if ("4C".equalsIgnoreCase(headerVO.getCreceipttype()))
      return result;
    if ((!"4453".equalsIgnoreCase(headerVO.getCreceipttype())) && 
      (headerVO.getBEstimation() != null) && (!headerVO.getBEstimation().booleanValue()))
      return result;
    SquareItemVO[] itemVOs = (SquareItemVO[])invo.getChildrenVO();
    HashMap map = new HashMap(itemVOs.length);
    for (int i = 0; i < itemVOs.length; i++) {
      if ("4C".equals(itemVOs[i].getCupreceipttype()))
        map.put(itemVOs[i].getCupbillbodyid(), itemVOs[i].getCupbillbodyid());
    }
    String[] ids = new String[map.size()];
    map.keySet().toArray(ids);

    HashMap oldSubMap = getAdjustSubNum(ids);
    HashMap ic4CNumMap = getAdjustSubNum(ids);

    HashMap allMap = new HashMap(itemVOs.length);
    for (int i = 0; i < itemVOs.length; i++) {
      String key = itemVOs[i].getCupbillbodyid();
      UFDouble ic4CNum = (UFDouble)ic4CNumMap.get(key);
      if ((ic4CNum == null) || (ic4CNum.compareTo(CreditUtil.ZERO) == 0))
        continue;
      Object[] num = (Object[])allMap.get(key);
      if ((num == null) || (num.length <= 0))
      {
        num = new Object[4];
        num[0] = ic4CNum;
        num[1] = CreditUtil.convertObjToUFDouble(oldSubMap.get(key));
        num[2] = CreditUtil.convertObjToUFDouble(itemVOs[i].getNnewbalancenum());
        num[3] = Integer.valueOf(-1);
      }
      else {
        num[2] = ((UFDouble)num[2]).add(CreditUtil.convertObjToUFDouble(itemVOs[i].getNnewbalancenum()));
      }num[3] = Integer.valueOf(getRowStateOrgAndNow(new UFDouble[] { (UFDouble)num[0], (UFDouble)num[1], (UFDouble)num[2] }));
      allMap.put(key, num);
    }

    int size = allMap.size();
    ArrayList unend_UNEND = new ArrayList(size);
    ArrayList unend_END = new ArrayList(size);
    ArrayList end_UNEND = new ArrayList(size);
    ArrayList end_END = new ArrayList(size);

    String[] keys = new String[size];
    allMap.keySet().toArray(keys);
    for (int i = 0; i < keys.length; i++) {
      Object[] ones = (Object[])allMap.get(keys[i]);
      if (((Integer)ones[3]).intValue() == 1)
        unend_UNEND.add(keys[i]);
      else if (((Integer)ones[3]).intValue() == 2)
        unend_END.add(keys[i]);
      else if (((Integer)ones[3]).intValue() == 4)
        end_END.add(keys[i]);
      else
        end_UNEND.add(keys[i]);
    }
    result.add(unend_UNEND);
    result.add(unend_END);
    result.add(end_UNEND);
    result.add(end_END);

    return result;
  }

  public static int getRowStateOrgAndNow(UFDouble[] num)
  {
    UFDouble orgNumleft = num[0].sub(num[1]);
    UFDouble CurrNumleft = num[0].sub(num[1]).sub(num[2]);
    boolean orgEnd = false;

    if (orgNumleft.multiply(num[0]).compareTo(CreditUtil.ZERO) <= 0)
    {
      orgEnd = true;
    }

    boolean currEnd = false;
    if (CurrNumleft.multiply(num[0]).compareTo(CreditUtil.ZERO) <= 0)
    {
      currEnd = true;
    }

    if ((!orgEnd) && (!currEnd))
      return 1;
    if ((orgEnd) && (!currEnd))
      return 8;
    if ((!orgEnd) && (!currEnd)) {
      return 2;
    }
    return 4;
  }
}