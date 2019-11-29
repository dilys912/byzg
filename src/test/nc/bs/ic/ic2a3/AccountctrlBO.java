package nc.bs.ic.ic2a3;

import java.util.ArrayList;
import java.util.Vector;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.ic.pub.GenMethod;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.sm.createcorp.CreatecorpDMO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.ic.ic2a3.AccountctrlHeaderVO;
import nc.vo.ic.ic2a3.AccountctrlItemVO;
import nc.vo.ic.ic2a3.AccountctrlVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.query.ConditionVO;

public class AccountctrlBO
{
  public String insertAccountCtrl(AccountctrlVO accountctrl)
    throws BusinessException
  {
    try
    {
      AccountctrlDMO dmo = new AccountctrlDMO();
      String key = dmo.insert(accountctrl);
      return key;
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException("Caused by:", e);
    }
   
  }

  public void deleteAccountCtrl(AccountctrlVO vo)
    throws BusinessException
  {
    try
    {
      AccountctrlDMO dmo = new AccountctrlDMO();
      dmo.delete(vo);
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
  }

  private AccperiodmonthVO[] getAccPeriod(String sStartTime, String sEndTime)
    throws BusinessException
  {
    AccperiodmonthVO[] voAp = null;

    UFDate ufdStart = new UFDate(sStartTime);
    UFDate ufdEnd = new UFDate(sEndTime);

    if (ufdStart.after(ufdEnd)) {
      ufdEnd = ufdStart;
    }

    try
    {
      AccountCalendar calendar1 = AccountCalendar.getInstance();
      calendar1.setDate(ufdStart);

      ufdStart = calendar1.getMonthVO().getBegindate();

      AccountCalendar calendar2 = AccountCalendar.getInstance();
      calendar2.setDate(ufdEnd);
      ufdEnd = calendar2.getMonthVO().getEnddate();

      voAp = calendar1.getMonthVOsByDates(ufdStart, ufdEnd);

      if ((voAp != null) && (voAp.length > 0)) {
        int i = voAp.length;

        if (ufdStart.after(voAp[0].getBegindate()))
          voAp[0].setBegindate(ufdStart);
        if ((voAp[(i - 1)].getEnddate() != null) && (voAp[(i - 1)].getEnddate().after(ufdEnd)))
          voAp[(i - 1)].setEnddate(ufdEnd);
      } else {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000099"));
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }

    return voAp;
  }

  private UFDateTime getNextDateTime(UFDateTime sDateTime)
  {
    UFDateTime ufdPre = sDateTime;
    if ((ufdPre == null) || (ufdPre.toString().trim().length() == 0))
      ufdPre = new UFDateTime(System.currentTimeMillis());
    long nowMillis = ufdPre.getMillis() + 1000L;
    return new UFDateTime(nowMillis);
  }

  private String getSQL(ConditionVO[] voCons, String pk_corp)
    throws Exception
  {
    StringBuffer sSQL = new StringBuffer("SELECT ");

    sSQL.append("acc.pk_accountctrl,acc.pk_calbody,cal.bodyname,acc.tstarttime,");
    sSQL.append("acc.tendtime,acc.faccountflag,acc.coperatorid,us.user_name,");
    sSQL.append("acc.vremark,acc.ts,acc.cwarehouseid,wh.storname");

    sSQL.append(" FROM ");
    sSQL.append("ic_accountctrl acc LEFT OUTER JOIN bd_calbody cal ");
    sSQL.append("ON acc.pk_calbody = cal.pk_calbody LEFT OUTER JOIN bd_stordoc wh ON acc.cwarehouseid=wh.pk_stordoc LEFT OUTER JOIN sm_user us ");
    sSQL.append("ON acc.coperatorid = us.cuserid ");

    sSQL.append(" WHERE ");
    sSQL.append("1=1 AND acc.dr=0 ");

    sSQL.append(getSubSQL(voCons, pk_corp));
    sSQL.append(" ORDER BY acc.tstarttime");
    return sSQL.toString();
  }

  private String getSubSQL(ConditionVO[] voCons, String pk_corp)
    throws Exception
  {
    String sAccFlag = "Y";

    sAccFlag = getSysParam(pk_corp, "IC055");

    if (sAccFlag == null)
      sAccFlag = "N";
    StringBuffer sWhere = new StringBuffer(" ");

    String sStartTime = null;
    String sEndTime = null;
    if (sAccFlag.equals("Y"))
    {
      sStartTime = "00:00:00";
      sEndTime = "23:59:59";
    }
    for (int i = 0; i < voCons.length; i++) {
      if (voCons[i].getFieldCode().equals("pk_calbody")) {
        sWhere.append(" AND acc.pk_calbody " + voCons[i].getOperaCode() + "'" + voCons[i].getValue().toString() + "'");
      }
      if (voCons[i].getFieldCode().equals("cwarehouseid")) {
        sWhere.append(" AND acc.cwarehouseid " + voCons[i].getOperaCode() + "'" + voCons[i].getValue().toString() + "'");
      }
      if (voCons[i].getFieldCode().equals("tstarttime")) {
        if (sAccFlag.equals("Y"))
          sWhere.append(" AND acc.tstarttime " + voCons[i].getOperaCode() + "'" + voCons[i].getValue().toString() + " " + sStartTime + "'");
        else {
          sWhere.append(" AND acc.tstarttime like '" + voCons[i].getValue().toString() + "%'");
        }
      }
      if (voCons[i].getFieldCode().equals("tendtime")) {
        if (sAccFlag.equals("Y"))
          sWhere.append(" AND acc.tendtime " + voCons[i].getOperaCode() + "'" + voCons[i].getValue().toString() + " " + sEndTime + "'");
        else {
          sWhere.append(" AND acc.tendtime like '" + voCons[i].getValue().toString() + "%'");
        }
      }
      if (voCons[i].getFieldCode().equals("faccountflag"))
        sWhere.append(" AND acc.faccountflag " + voCons[i].getOperaCode() + "'" + voCons[i].getValue().toString() + "'");
      if (voCons[i].getFieldCode().equals("coperatorid"))
        sWhere.append(" AND acc.coperatorid " + voCons[i].getOperaCode() + "'" + voCons[i].getValue().toString() + "'");
    }
    return sWhere.toString();
  }

  private String getSysParam(String sCorpID, String sParam)
    throws BusinessException
  {
    String sAccPeriod = "N";

    sAccPeriod = GenMethod.getParaString(sCorpID, sParam);

    return sAccPeriod;
  }

  private AccountctrlHeaderVO[] processAccPeriod(AccountctrlHeaderVO headerVO)
    throws BusinessException
  {
    AccountctrlHeaderVO[] voRet = null;
    try
    {
      UFDateTime ufdEndTime = headerVO.getTendtime();
      UFDateTime ufdPeriodEndTime = null;

      AccountCalendar calend1 = AccountCalendar.getInstance();
      calend1.setDate(ufdEndTime.getDate());

      if (ufdEndTime != null) {
        String sEnddate = calend1.getMonthVO().getEnddate().toString();
        ufdPeriodEndTime = new UFDateTime(sEnddate + " 23:59:59");
      }

      if (ufdEndTime.before(ufdPeriodEndTime))
      {
        voRet = processBeforePeriodEndTime(headerVO, ufdEndTime, ufdPeriodEndTime);
      }
      else {
        voRet = processEqualPeriodEndTime(headerVO, ufdEndTime);
      }
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }

    return voRet;
  }

  private AccountctrlHeaderVO[] processBeforePeriodEndTime(AccountctrlHeaderVO voHeader, UFDateTime ufdEndTime, UFDateTime ufdPeriodEndTime)
    throws BusinessException
  {
    AccountctrlHeaderVO[] voRet = null;
    try
    {
      if (voHeader != null) {
        String sAccPK = voHeader.getPk_accountctrl();
        String sOperatorID = voHeader.getCoperatorid();
        UFBoolean bStatus = voHeader.getFaccountflag();
        AccountctrlDMO dmo = new AccountctrlDMO();

        UFDateTime ts = new UFDateTime(System.currentTimeMillis());
        voHeader.setTs(ts);
        dmo.updateHeader(voHeader);
        voRet = new AccountctrlHeaderVO[2];
        voRet[0] = voHeader;

        AccountctrlItemVO voItem = new AccountctrlItemVO();
        voItem.setPk_accountctrl(sAccPK);
        voItem.setCoperatorid(sOperatorID);
        voItem.setFactionflag(bStatus);
        dmo.insertItem(voItem);

        UFDateTime ufdStartTime = getNextDateTime(ufdEndTime);
        UFBoolean bY = new UFBoolean("Y");
        voRet[1] = new AccountctrlHeaderVO();
        voRet[1] = ((AccountctrlHeaderVO)voRet[0].clone());
        voRet[1].setTstarttime(ufdStartTime);
        voRet[1].setTendtime(ufdPeriodEndTime);
        voRet[1].setFaccountflag(bY);

        String sPK = dmo.insertHeader(voRet[1]);
        voRet[1].setPk_accountctrl(sPK);

        if (sPK != null) {
          voItem.setPk_accountctrl(sPK);
          voItem.setFactionflag(bY);
          dmo.insertItem(voItem);
        }
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
    return voRet;
  }

  private AccountctrlHeaderVO[] processEqualPeriodEndTime(AccountctrlHeaderVO voHeader, UFDateTime ufdEndTime)
    throws BusinessException
  {
    AccountctrlHeaderVO[] voRet = null;
    try
    {
      if (voHeader != null) {
        String sAccPK = voHeader.getPk_accountctrl();
        String sOperatorID = voHeader.getCoperatorid();
        UFBoolean bStatus = voHeader.getFaccountflag();
        UFDateTime ufdPeriodEndTime = null;

        UFDate ufdtemp = getNextDateTime(ufdEndTime).getDate();

        AccountctrlDMO dmo = new AccountctrlDMO();
        UFDateTime ts = new UFDateTime(System.currentTimeMillis());
        voHeader.setTs(ts);
        dmo.updateHeader(voHeader);
        voRet = new AccountctrlHeaderVO[2];
        voRet[0] = voHeader;

        AccountctrlItemVO voItem = new AccountctrlItemVO();
        voItem.setPk_accountctrl(sAccPK);
        voItem.setCoperatorid(sOperatorID);
        voItem.setFactionflag(bStatus);
        dmo.insertItem(voItem);

        AccountCalendar calend1 = AccountCalendar.getInstance();
        calend1.setDate(ufdtemp);
        String sEnddate = calend1.getMonthVO().getEnddate().toString();
        ufdPeriodEndTime = new UFDateTime(sEnddate + " 23:59:59");

        UFBoolean bY = new UFBoolean("Y");
        voRet[1] = new AccountctrlHeaderVO();
        voRet[1] = ((AccountctrlHeaderVO)voRet[0].clone());
        voRet[1].setTstarttime(getNextDateTime(ufdEndTime));

        voRet[1].setTendtime(ufdPeriodEndTime);
        voRet[1].setFaccountflag(bY);

        String sPK = dmo.insertHeader(voRet[1]);
        voRet[1].setPk_accountctrl(sPK);

        if (sPK != null) {
          voItem.setPk_accountctrl(sPK);
          voItem.setFactionflag(bY);
          dmo.insertItem(voItem);
        }
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
    return voRet;
  }

  private AccountctrlHeaderVO[] processNotAccPeriod(AccountctrlHeaderVO voHeader)
    throws BusinessException
  {
    AccountctrlHeaderVO[] voRet = null;
    try
    {
      if (voHeader != null) {
        String sAccPK = voHeader.getPk_accountctrl();
        String sOperatorID = voHeader.getCoperatorid();
        UFBoolean bStatus = voHeader.getFaccountflag();
        UFDateTime ufdEndTime = voHeader.getTendtime();
        AccountctrlDMO dmo = new AccountctrlDMO();

        UFDateTime ts = new UFDateTime(System.currentTimeMillis());
        voHeader.setTs(ts);
        dmo.updateHeader(voHeader);
        voRet = new AccountctrlHeaderVO[2];
        voRet[0] = voHeader;

        AccountctrlItemVO voItem = new AccountctrlItemVO();
        voItem.setPk_accountctrl(sAccPK);
        voItem.setCoperatorid(sOperatorID);
        voItem.setFactionflag(bStatus);
        dmo.insertItem(voItem);

        UFDateTime ufdStartTime = getNextDateTime(ufdEndTime);
        UFBoolean bY = new UFBoolean("Y");
        voRet[1] = new AccountctrlHeaderVO();
        voRet[1] = ((AccountctrlHeaderVO)voRet[0].clone());
        voRet[1].setTstarttime(ufdStartTime);
        voRet[1].setTendtime(null);
        voRet[1].setFaccountflag(bY);

        String sPK = dmo.insertHeader(voRet[1]);
        voRet[1].setPk_accountctrl(sPK);

        if (sPK != null) {
          voItem.setPk_accountctrl(sPK);
          voItem.setFactionflag(bY);
          dmo.insertItem(voItem);
        }
      }
    } catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
    return voRet;
  }

  public AccountctrlVO[] queryAccountCtrl(ConditionVO[] voCons, String sCorpID, String sUserID)
    throws BusinessException
  {
    if ((voCons == null) || (voCons.length <= 0))
      return null;
    AccountctrlVO[] accVO = null;
    String sCalbodyPK = null;
    String sWhPK = null;
    int iByType = 0;

    for (int i = 0; i < voCons.length; i++) {
      if (voCons[i].getFieldCode().equals("pk_calbody")) {
        sCalbodyPK = voCons[i].getValue();
        iByType = 1;
      } else if (voCons[i].getFieldCode().equals("cwarehouseid")) {
        sWhPK = voCons[i].getValue();
        iByType = 2;
      }
    }
    if ((sCalbodyPK == null) && (sWhPK == null))
      return null;
    if ((sCalbodyPK != null) && (sWhPK != null))
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000101"));
    try
    {
      AccountctrlDMO dmo = new AccountctrlDMO();
      if (iByType == 1)
      {
        boolean bisinit = dmo.isinitCalbody(sCalbodyPK);

        if (bisinit)
        {
          accVO = dmo.queryAcc(getSQL(voCons, sCorpID));
        }
        else {
          accVO = initAccDataCal(sCalbodyPK, sCorpID, sUserID);

          if (accVO != null) {
            for (int i = 0; i < accVO.length; i++) {
              insertAccountCtrl(accVO[i]);
            }
          }
          if (accVO != null)
            accVO = dmo.queryAcc(getSQL(voCons, sCorpID));
        }
      } else if (iByType == 2)
      {
        boolean bisinit = dmo.isinitWarehouse(sWhPK);

        if (bisinit)
        {
          accVO = dmo.queryAcc(getSQL(voCons, sCorpID));
        }
        else {
          accVO = initAccDataWh(sWhPK, sCorpID, sUserID);

          if (accVO != null) {
            for (int i = 0; i < accVO.length; i++) {
              insertAccountCtrl(accVO[i]);
            }
          }
          if (accVO != null)
            accVO = dmo.queryAcc(getSQL(voCons, sCorpID));
        }
      }
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }

    return accVO;
  }

  public AccountctrlItemVO[] queryAccountCtrlRecord(String sAccPK)
    throws BusinessException
  {
    try
    {
      AccountctrlDMO dmo = new AccountctrlDMO();
      AccountctrlItemVO[] accItemVO = dmo.findItemsForHeader(sAccPK);
      return accItemVO;
    } catch (Exception e) {
      if ((e instanceof BusinessException))
        throw ((BusinessException)e);
      throw new BusinessException("Caused by:", e);
    }
    
  }

  private String querySysDate(String sCorpID, String sProdID, boolean isNeedTime)
    throws Exception
  {
    String[] sdate = null;
    String sStartdate = null;

    CreatecorpDMO dmo = new CreatecorpDMO();
    sdate = dmo.queryEnabledPeriod(sCorpID, "IC");
    if ((sdate != null) && 
      (sdate != null) && (sdate.length > 2)) {
      AccountCalendar calendar1 = AccountCalendar.getInstance();
      calendar1.set(sdate[0], sdate[1]);
      sStartdate = calendar1.getMonthVO().getBegindate().toString();

      if ((isNeedTime) && (sStartdate != null)) {
        sStartdate = sStartdate + " 00:00:00";
      }

    }

    return sStartdate;
  }

  private String[][] resetAccperiodmonthVO(AccperiodmonthVO[] periodVO)
  {
    String[][] saPeriod = (String[][])null;
    if (periodVO != null) {
      saPeriod = new String[periodVO.length][2];
      for (int i = 0; i < periodVO.length; i++) {
        if (periodVO[i].getBegindate() != null) {
          String sStartdate = periodVO[i].getBegindate().toString();
          sStartdate = sStartdate + " 00:00:00";
          saPeriod[i][0] = sStartdate;
        }
        if (periodVO[i].getEnddate() != null) {
          String sEnddate = periodVO[i].getEnddate().toString();
          sEnddate = sEnddate + " 23:59:59";
          saPeriod[i][1] = sEnddate;
        }
      }
    }
    return saPeriod;
  }

  public AccountctrlHeaderVO[] updateAccountCtrl(AccountctrlHeaderVO voHeader, String sCorpID)
    throws BusinessException
  {
    AccountctrlHeaderVO[] voaReturn = null;
    try {
      String sCalID = voHeader.getPk_calbody();
      String sWhID = voHeader.getPk_stordoc();
      if ((sCalID == null) && (sWhID == null)) {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000102"));
      }
      if (sWhID != null) {
        return updateWh(voHeader, sCorpID);
      }
      if (sCalID != null) {
        return updateCal(voHeader, sCorpID);
      }
    }
    catch (Exception e1)
    {
      GenMethod.throwBusiException(e1);
    }
    return voaReturn;
  }

  private AccountctrlHeaderVO[] findWhByCal(AccountctrlHeaderVO voHeader)
    throws Exception
  {
    String sCalID = voHeader.getPk_calbody();
    String sUserID = voHeader.getCoperatorid();

    AccountctrlDMO dmo = new AccountctrlDMO();
    ArrayList alWhs = dmo.findAllWh(null);

    boolean isWhtoCal = false;
    String sWh = null;
    ArrayList alWhFilt = new ArrayList();

    for (int i = 0; i < alWhs.size(); i++) {
      isWhtoCal = false;

      sWh = (String)(String)alWhs.get(i);
      if (sWh == null) {
        continue;
      }
      isWhtoCal = dmo.isWhBelongCal(sWh, sCalID);
      if (isWhtoCal == true) {
        alWhFilt.add(sWh);
      }
    }

    int size = alWhFilt.size();
    if (size == 0) {
      return null;
    }
    String[] saWh = new String[size];
    alWhFilt.toArray(saWh);

    ArrayList alHeaders = new ArrayList();
    AccountctrlHeaderVO voHeadWh = null;
    AccountctrlHeaderVO voHeadCal = null;

    for (int i = 0; i < saWh.length; i++) {
      AccountctrlVO[] voaWh = dmo.queryAccWh(saWh[i], sUserID);
      AccountctrlVO[] voaCal = dmo.queryAccCal(voHeader.getPk_calbody(), sUserID);
      if (voaWh == null) return null;
      if (voaCal == null) throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000103"));

      for (int m = 0; m < voaCal.length; m++) {
        if (((AccountctrlHeaderVO)(AccountctrlHeaderVO)voaCal[m].getParentVO()).getTstarttime().compareTo(voHeader.getTstarttime()) == 0) {
          voHeadCal = (AccountctrlHeaderVO)(AccountctrlHeaderVO)voaCal[m].getParentVO();
          break;
        }

      }

      for (int j = 0; j < voaWh.length; j++) {
        voHeadWh = (AccountctrlHeaderVO)voaWh[j].getParentVO();
        AccountctrlHeaderVO[] voaHeaders;
        if (voHeadWh != null) {
          boolean bStart = voHeadCal.getTstarttime().toString().trim().equalsIgnoreCase(voHeadWh.getTstarttime().toString().trim());

          boolean bEnd = voHeadCal.getTendtime().toString().trim().equalsIgnoreCase(voHeadWh.getTendtime().toString().trim());
          if ((bStart) && (bEnd)) {
            alHeaders.add(voHeadWh);
            voHeadWh.setTendtime(voHeader.getTendtime());
            break;
          }
        }
      }

    }

    for (int i = 0; i < alHeaders.size(); i++) {
      ((AccountctrlHeaderVO)alHeaders.get(i)).setCoperatorid(voHeader.getCoperatorid());
      ((AccountctrlHeaderVO)alHeaders.get(i)).setFaccountflag(voHeader.getFaccountflag());
    }

    if (alHeaders.size() > 0) {
    	AccountctrlHeaderVO  voaHeaders[] = new AccountctrlHeaderVO[alHeaders.size()];
      alHeaders.toArray(voaHeaders);
      return voaHeaders;
    }

    return null;
  }

  public AccountctrlVO[] freshAccountCtrl(String sCalbodyPK, String sWarehousePK, String sCorpID, String sUserID)
    throws BusinessException
  {
    if ((sCalbodyPK == null) && (sWarehousePK == null)) {
      throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000104"));
    }

    AccountctrlVO[] accVO = null;
    try {
      boolean bisinitWh = false;
      boolean bisinit = false;
      AccountctrlDMO dmo = new AccountctrlDMO();

      if (sWarehousePK != null)
        bisinitWh = dmo.isinitWarehouse(sWarehousePK);
      else {
        bisinit = dmo.isinitCalbody(sCalbodyPK);
      }

      if (bisinitWh) {
        accVO = dmo.queryAccWh(sWarehousePK, sUserID);
        return accVO;
      }

      if (bisinit) {
        accVO = dmo.queryAccCal(sCalbodyPK, sUserID);
        initAccDataWhinCal(sCalbodyPK, sCorpID, sUserID);
        return accVO;
      }

      if (sWarehousePK != null) {
        String sPK2 = null;
        accVO = initAccDataWh(sWarehousePK, sCorpID, sUserID);
        if (accVO != null) {
          for (int i = 0; i < accVO.length; i++) {
            sPK2 = dmo.insert(accVO[i]);
          }
        }
        if ((accVO != null) && (sPK2 != null))
          accVO = dmo.queryAccWh(sWarehousePK, sUserID);
        return accVO;
      }

      if (sCalbodyPK != null)
      {
        String sPK1 = null;
        accVO = initAccDataCal(sCalbodyPK, sCorpID, sUserID);

        if (accVO != null) {
          for (int i = 0; i < accVO.length; i++) {
            sPK1 = dmo.insert(accVO[i]);
          }
        }
        if ((accVO != null) && (sPK1 != null))
        {
          accVO = dmo.queryAccCal(sCalbodyPK, sUserID);
        }
        initAccDataWhinCal(sCalbodyPK, sCorpID, sUserID);
        return accVO;
      }
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
    return accVO;
  }

  private AccountctrlVO[] initAccDataCal(String sCalbodyPK, String sCorpID, String sUserID)
    throws BusinessException
  {
    AccperiodmonthVO[] periodVO = null;
    AccountctrlVO[] accVO = null;
    Vector accVector = new Vector();
    try
    {
      String sAccFlag = getSysParam(sCorpID, "IC055");
      if (sAccFlag == null) {
        sAccFlag = "N";
      }
      if (sAccFlag.equals("Y"))
      {
        String sStartdate = querySysDate(sCorpID, "4008", false);

        String sEnddate = new UFDateTime(System.currentTimeMillis()).getDate().toString();

        periodVO = getAccPeriod(sStartdate, sEnddate);
        String[][] saPeriod = resetAccperiodmonthVO(periodVO);
        if (saPeriod != null)
        {
          for (int i = 0; i < saPeriod.length; i++) {
            AccountctrlVO tempvo = new AccountctrlVO();
            AccountctrlHeaderVO tempheadvo = new AccountctrlHeaderVO();
            AccountctrlItemVO[] tempitemvo = new AccountctrlItemVO[1];
            tempheadvo.setPk_calbody(sCalbodyPK);
            tempheadvo.setCoperatorid(sUserID);
            tempheadvo.setFaccountflag(new UFBoolean("Y"));
            tempheadvo.setTstarttime(new UFDateTime(saPeriod[i][0]));
            tempheadvo.setTendtime(new UFDateTime(saPeriod[i][1]));
            tempvo.setParentVO(tempheadvo);
            tempitemvo[0] = new AccountctrlItemVO();
            tempitemvo[0].setCoperatorid(sUserID);
            tempitemvo[0].setFactionflag(new UFBoolean("Y"));
            tempvo.setChildrenVO(tempitemvo);
            accVector.addElement(tempvo);
          }

          if (accVector != null) {
            accVO = new AccountctrlVO[accVector.size()];
            accVector.copyInto(accVO);
          }
        }
      }
      else {
        String sStartdate = querySysDate(sCorpID, "4008", true);
        accVO = new AccountctrlVO[1];
        accVO[0] = new AccountctrlVO();
        AccountctrlHeaderVO tempheadvo = new AccountctrlHeaderVO();
        AccountctrlItemVO[] tempitemvo = new AccountctrlItemVO[1];
        tempheadvo.setPk_calbody(sCalbodyPK);
        tempheadvo.setCoperatorid(sUserID);
        tempheadvo.setFaccountflag(new UFBoolean("Y"));
        tempheadvo.setTstarttime(new UFDateTime(sStartdate));
        accVO[0].setParentVO(tempheadvo);
        tempitemvo[0] = new AccountctrlItemVO();
        tempitemvo[0].setCoperatorid(sUserID);
        tempitemvo[0].setFactionflag(new UFBoolean("Y"));
        accVO[0].setChildrenVO(tempitemvo);
      }
    }
    catch (Exception e)
    {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
    return accVO;
  }

  private AccountctrlVO[] initAccDataWh(String sWhPK, String sCorpID, String sUserID)
    throws Exception
  {
    AccountctrlDMO dmo = new AccountctrlDMO();
    String sCalID = dmo.getCalByWh(sWhPK, sCorpID);

    AccountctrlVO[] voaAcc = null;
    boolean bInitCal = dmo.isinitCalbody(sCalID);
    if (bInitCal == true) {
      voaAcc = dmo.queryAccCal(sCalID, sUserID);
      if (voaAcc == null) throw new BusinessException("AccoutctrlDMO::queryAccCal error!");
      for (int i = 0; i < voaAcc.length; i++) {
        ((AccountctrlHeaderVO)(AccountctrlHeaderVO)voaAcc[i].getParentVO()).setPk_calbody(null);
        ((AccountctrlHeaderVO)(AccountctrlHeaderVO)voaAcc[i].getParentVO()).setVcalbodyname(null);
        ((AccountctrlHeaderVO)(AccountctrlHeaderVO)voaAcc[i].getParentVO()).setPk_stordoc(sWhPK);

        AccountctrlItemVO[] tempitemvo = new AccountctrlItemVO[1];
        tempitemvo[0] = new AccountctrlItemVO();
        tempitemvo[0].setCoperatorid(sUserID);
        tempitemvo[0].setFactionflag(new UFBoolean("Y"));

        voaAcc[i].setChildrenVO(tempitemvo);
      }

      return voaAcc;
    }

    AccperiodmonthVO[] periodVO = null;
    AccountctrlVO[] accVO = null;
    Vector accVector = new Vector();

    String sAccFlag = getSysParam(sCorpID, "IC055");
    if (sAccFlag == null) {
      sAccFlag = "N";
    }
    if (sAccFlag.equals("Y"))
    {
      String sStartdate = querySysDate(sCorpID, "4008", false);

      String sEnddate = new UFDateTime(System.currentTimeMillis()).getDate().toString();

      periodVO = getAccPeriod(sStartdate, sEnddate);
      String[][] saPeriod = resetAccperiodmonthVO(periodVO);
      if (saPeriod != null)
      {
        for (int i = 0; i < saPeriod.length; i++) {
          AccountctrlVO tempvo = new AccountctrlVO();
          AccountctrlHeaderVO tempheadvo = new AccountctrlHeaderVO();
          AccountctrlItemVO[] tempitemvo = new AccountctrlItemVO[1];
          tempheadvo.setPk_calbody(null);
          tempheadvo.setPk_stordoc(sWhPK);
          tempheadvo.setCoperatorid(sUserID);
          tempheadvo.setFaccountflag(new UFBoolean("Y"));
          tempheadvo.setTstarttime(new UFDateTime(saPeriod[i][0]));
          tempheadvo.setTendtime(new UFDateTime(saPeriod[i][1]));
          tempvo.setParentVO(tempheadvo);
          tempitemvo[0] = new AccountctrlItemVO();
          tempitemvo[0].setCoperatorid(sUserID);
          tempitemvo[0].setFactionflag(new UFBoolean("Y"));
          tempvo.setChildrenVO(tempitemvo);
          accVector.addElement(tempvo);
        }

        if (accVector != null) {
          accVO = new AccountctrlVO[accVector.size()];
          accVector.copyInto(accVO);
        }
      }
    }
    else {
      String sStartdate = querySysDate(sCorpID, "4008", true);
      accVO = new AccountctrlVO[1];
      accVO[0] = new AccountctrlVO();
      AccountctrlHeaderVO tempheadvo = new AccountctrlHeaderVO();
      AccountctrlItemVO[] tempitemvo = new AccountctrlItemVO[1];
      tempheadvo.setPk_calbody(null);
      tempheadvo.setPk_stordoc(sWhPK);

      tempheadvo.setCoperatorid(sUserID);
      tempheadvo.setFaccountflag(new UFBoolean("Y"));
      tempheadvo.setTstarttime(new UFDateTime(sStartdate));
      accVO[0].setParentVO(tempheadvo);
      tempitemvo[0] = new AccountctrlItemVO();
      tempitemvo[0].setCoperatorid(sUserID);
      tempitemvo[0].setFactionflag(new UFBoolean("Y"));
      accVO[0].setChildrenVO(tempitemvo);
    }

    return accVO;
  }

  private void initAccDataWhinCal(String sCalPK, String sCorpID, String sUserID)
    throws Exception
  {
    AccountctrlDMO dmo = new AccountctrlDMO();
    ArrayList alWh = dmo.getWhByCal(sCalPK, sCorpID);
    AccountctrlVO[] accVO = null;

    for (int i = 0; i < alWh.size(); i++) {
      accVO = initAccDataWh((String)alWh.get(i), sCorpID, sUserID);
      if (accVO != null)
        for (int j = 0; j < accVO.length; j++)
          dmo.insert(accVO[j]);
    }
  }

  private AccountctrlHeaderVO[] updateCal(AccountctrlHeaderVO voHeader, String sCorpID)
    throws Exception
  {
    AccountctrlHeaderVO[] voaWhHeaders = findWhByCal(voHeader);

    if (voaWhHeaders != null) {
      for (int i = 0; i < voaWhHeaders.length; i++) {
        updateWh_Ex(voaWhHeaders[i], sCorpID);
      }
    }

    return updateCal_base(voHeader, sCorpID);
  }

  private AccountctrlHeaderVO[] updateCal_base(AccountctrlHeaderVO voHeader, String sCorpID)
    throws BusinessException
  {
    AccountctrlHeaderVO[] voRet = null;
    try
    {
      String sisAccpriod = getSysParam(sCorpID, "IC055");

      UFDateTime sStartTime = null;

      UFDateTime sEndTime = null;

      String sAccPK = null;

      String sOperatorID = null;

      UFBoolean bStatus = null;

      String sCalbodyID = null;

      AccountctrlDMO dmo = new AccountctrlDMO();
      if (voHeader != null) {
        sAccPK = voHeader.getPk_accountctrl();
        sOperatorID = voHeader.getCoperatorid();
        bStatus = voHeader.getFaccountflag();
        sStartTime = voHeader.getTstarttime();
        sEndTime = voHeader.getTendtime();
        sCalbodyID = voHeader.getPk_calbody();

        ArrayList alNeedDetailPK = dmo.getNeedDetailPK(voHeader, 1);

        if (bStatus.toString().equals("N"))
        {
          String sTime = dmo.getStartTime(sCalbodyID, sStartTime.toString(), 1);
          if (sTime == null) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000105"));
          }
          boolean bisHaveFreeBill = dmo.isHaveFreeBill(sCalbodyID, new UFDateTime(sTime), sEndTime, 1);
          if (bisHaveFreeBill == true) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000106"));
          }
        }

        boolean bisLast = dmo.isLastRecord(sAccPK, sCalbodyID, 1);
        if (!bisLast)
        {
          dmo.updateHeader(voHeader);
          voRet = new AccountctrlHeaderVO[1];
          voRet[0] = voHeader;

          AccountctrlItemVO voItem = new AccountctrlItemVO();
          voItem.setPk_accountctrl(sAccPK);
          voItem.setCoperatorid(sOperatorID);
          voItem.setFactionflag(bStatus);
          dmo.insertItem(voItem);
        }
        else if ("Y".equals(sisAccpriod))
        {
          voRet = processAccPeriod(voHeader);
        }
        else {
          voRet = processNotAccPeriod(voHeader);
        }

        if (alNeedDetailPK != null) {
          int iLen = alNeedDetailPK.size();
          AccountctrlItemVO[] voaItem = new AccountctrlItemVO[iLen];
          for (int j = 0; j < iLen; j++) {
            voaItem[j] = new AccountctrlItemVO();
            voaItem[j].setPk_accountctrl((String)alNeedDetailPK.get(j));
            voaItem[j].setCoperatorid(sOperatorID);
            voaItem[j].setFactionflag(bStatus);
            dmo.insertItem(voaItem[j]);
          }
        }
      }
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
    return voRet;
  }

  private AccountctrlHeaderVO[] updateWh(AccountctrlHeaderVO voHeaderEx, String sCorpID)
    throws BusinessException
  {
    AccountctrlHeaderVO voHeader = (AccountctrlHeaderVO)voHeaderEx.clone();
    voHeader.setPk_calbody(null);
    AccountctrlHeaderVO[] voRet = null;
    try
    {
      String sisAccpriod = getSysParam(sCorpID, "IC055");

      UFDateTime sStartTime = null;

      UFDateTime sEndTime = null;

      String sAccPK = null;

      String sOperatorID = null;

      UFBoolean bStatus = null;

      String sWhID = null;

      AccountctrlDMO dmo = new AccountctrlDMO();
      if (voHeader != null) {
        sAccPK = voHeader.getPk_accountctrl();
        sOperatorID = voHeader.getCoperatorid();
        bStatus = voHeader.getFaccountflag();
        sStartTime = voHeader.getTstarttime();
        sEndTime = voHeader.getTendtime();
        sWhID = voHeader.getPk_stordoc();

        ArrayList alNeedDetailPK = dmo.getNeedDetailPK(voHeader, 2);

        if (bStatus.toString().equals("N"))
        {
          String sTime = dmo.getStartTime(sWhID, sStartTime.toString(), 2);
          if (sTime == null) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000105"));
          }
          boolean bisHaveFreeBill = dmo.isHaveFreeBill(sWhID, new UFDateTime(sTime), sEndTime, 2);
          if (bisHaveFreeBill == true) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000106"));
          }
        }

        boolean bisLast = dmo.isLastRecord(sAccPK, sWhID, 2);
        if (!bisLast)
        {
          dmo.updateHeader(voHeader);
          voRet = new AccountctrlHeaderVO[1];
          voRet[0] = voHeader;

          AccountctrlItemVO voItem = new AccountctrlItemVO();
          voItem.setPk_accountctrl(sAccPK);
          voItem.setCoperatorid(sOperatorID);
          voItem.setFactionflag(bStatus);
          dmo.insertItem(voItem);
        }
        else if ("Y".equals(sisAccpriod))
        {
          voRet = processAccPeriod(voHeader);
        }
        else {
          voRet = processNotAccPeriod(voHeader);
        }

        if (alNeedDetailPK != null) {
          int iLen = alNeedDetailPK.size();
          AccountctrlItemVO[] voaItem = new AccountctrlItemVO[iLen];
          for (int j = 0; j < iLen; j++) {
            voaItem[j] = new AccountctrlItemVO();
            voaItem[j].setPk_accountctrl((String)alNeedDetailPK.get(j));
            voaItem[j].setCoperatorid(sOperatorID);
            voaItem[j].setFactionflag(bStatus);
            dmo.insertItem(voaItem[j]);
          }
        }
      }
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
    return voRet;
  }

  private AccountctrlHeaderVO[] updateWh_Ex(AccountctrlHeaderVO voHeader, String sCorpID)
    throws BusinessException
  {
    AccountctrlHeaderVO[] voRet = null;
    try
    {
      String sisAccpriod = getSysParam(sCorpID, "IC055");

      UFDateTime sStartTime = null;

      UFDateTime sEndTime = null;

      String sAccPK = null;

      String sOperatorID = null;

      UFBoolean bStatus = null;

      String sWhID = null;

      AccountctrlDMO dmo = new AccountctrlDMO();
      if (voHeader != null) {
        sAccPK = voHeader.getPk_accountctrl();
        sOperatorID = voHeader.getCoperatorid();
        bStatus = voHeader.getFaccountflag();
        sStartTime = voHeader.getTstarttime();
        sEndTime = voHeader.getTendtime();
        sWhID = voHeader.getPk_stordoc();

        ArrayList alNeedDetailPK = dmo.getNeedDetailPK(voHeader, 2);

        if (bStatus.toString().equals("N"))
        {
          boolean bisHaveFreeBill = dmo.isHaveFreeBill(sWhID, sStartTime, sEndTime, 2);
          if (bisHaveFreeBill == true) {
            throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4008spec", "UPP4008spec-000106"));
          }
        }

        boolean bisLast = dmo.isLastRecord(sAccPK, sWhID, 2);
        if (!bisLast)
        {
          dmo.updateHeader(voHeader);
          voRet = new AccountctrlHeaderVO[1];
          voRet[0] = voHeader;

          AccountctrlItemVO voItem = new AccountctrlItemVO();
          voItem.setPk_accountctrl(sAccPK);
          voItem.setCoperatorid(sOperatorID);
          voItem.setFactionflag(bStatus);
          dmo.insertItem(voItem);
        }
        else if ("Y".equals(sisAccpriod))
        {
          voRet = processAccPeriod(voHeader);
        }
        else {
          voRet = processNotAccPeriod(voHeader);
        }

        if (alNeedDetailPK != null) {
          int iLen = alNeedDetailPK.size();
          AccountctrlItemVO[] voaItem = new AccountctrlItemVO[iLen];
          for (int j = 0; j < iLen; j++) {
            voaItem[j] = new AccountctrlItemVO();
            voaItem[j].setPk_accountctrl((String)alNeedDetailPK.get(j));
            voaItem[j].setCoperatorid(sOperatorID);
            voaItem[j].setFactionflag(bStatus);
            dmo.insertItem(voaItem[j]);
          }
        }
      }
    }
    catch (Exception e) {
      if ((e instanceof BusinessException)) {
        throw ((BusinessException)e);
      }
      throw new BusinessException("Caused by:", e);
    }
    return voRet;
  }
}