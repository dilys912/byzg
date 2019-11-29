package nc.bs.arap.verify;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import nc.bs.ar.hz.HZYEBO;
import nc.bs.arap.accountpub.HXDMO;
import nc.bs.arap.agiotage.AgiotageDMO;
import nc.bs.arap.callouter.FipCallFacade;
import nc.bs.arap.global.ArapExtInfRunBO;
import nc.bs.arap.outer.ArapPubUnVerifyInterface;
import nc.bs.arap.outer.ArapPubVerifyInterface;
import nc.bs.ep.dj.DJZBBO;
import nc.bs.ep.dj.DJZBDAO;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.ntb.services.BudgetControlServiceGetter;
import nc.itf.ntb.IBudgetControl;
import nc.vo.arap.accountpub.DjclDapVO;
import nc.vo.arap.agiotage.AgiotageVO;
import nc.vo.arap.global.BusiTransVO;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.arap.transaction.RemoteTransferVO;
import nc.vo.arap.verify.DJCLBVO;
import nc.vo.arap.verify.ForDapMsgParaVO;
import nc.vo.arap.verify.InputDataVO;
import nc.vo.arap.verify.VITFilterCondVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJ_IAccessableBusiVO;
import nc.vo.ntb.outer.BudgetInfo;
import nc.vo.ntb.outer.NtbCtlInfoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class VerifyBO
{
  public DJCLBVO afterCancelVerifyInf(BusiTransVO[] busitransvos, DJCLBVO paraVO)
    throws BusinessException
  {
    DapMsgVO PfStateVO = getDapmsgVO(paraVO);
    PfStateVO.setMsgType(0);
    DjclDapVO dapvo = getDapVO(paraVO, PfStateVO);

    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubUnVerifyInterface)busitransvos[i].getInfClass()).afterUnVerifyAct(dapvo);
        }
        catch (Exception e)
        {
          Log.getInstance(VerifyBO.class).error(e.getMessage(), e);

          throw new BusinessException(busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage());
        }
      }

    }

    return paraVO;
  }

  public DJCLBVO[] afterVerifyInf(BusiTransVO[] busitransvos, DJCLBVO[] clbvos, DJCLBVO paraVO, boolean bLinkPFInterFace)
    throws BusinessException
  {
    DapMsgVO PfStateVO = getDapmsgVO(paraVO);
    PfStateVO.setMsgType(0);
    DjclDapVO dapvo = getDapVO(paraVO, PfStateVO);

    if (clbvos[0] != null)
      dapvo.setOptype(clbvos[0].getOptype());
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubVerifyInterface)busitransvos[i].getInfClass()).afterVerifyAct(dapvo);
        }
        catch (Exception e)
        {
          Log.getInstance(VerifyBO.class).error(e.getMessage(), e);
          throw new BusinessException(busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage());
        }

      }

    }

    if (bLinkPFInterFace) {
      try {
        new FipCallFacade().sendMessage(PfStateVO, dapvo);
      } catch (Exception e) {
        Log.getInstance(getClass()).error(e.getMessage(), e);
        if ((e instanceof BusinessException)) {
          throw ((BusinessException)e);
        }
        throw new BusinessException(e.getMessage(), e);
      }
    }

    return clbvos;
  }

  public DJCLBVO[] beforeCancelVerifyInf(BusiTransVO[] busitransvos, DJCLBVO[] djclbvo)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubUnVerifyInterface)busitransvos[i].getInfClass()).beforeUnVerifyAct(djclbvo);
        }
        catch (Exception e)
        {
          Log.getInstance(VerifyBO.class).error(e.getMessage(), e);

          throw new BusinessException(busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage());
        }
      }

    }

    return djclbvo;
  }

  public DJCLBVO[] beforeVerifyInf(BusiTransVO[] busitransvos, DJCLBVO[] djclbvo)
    throws BusinessException
  {
    if (busitransvos != null) {
      for (int i = 0; i < busitransvos.length; i++) {
        try {
          ((ArapPubVerifyInterface)busitransvos[i].getInfClass()).beforeVerifyAct(djclbvo);
        }
        catch (Exception e)
        {
          Log.getInstance(getClass()).error(e.getMessage(), e);

          throw new BusinessException(busitransvos[i].getUsesystemname() + busitransvos[i].getNote() + "\n" + e.getMessage());
        }
      }

    }

    return djclbvo;
  }

  private boolean createTab(InputDataVO vo)
    throws BusinessException
  {
    boolean create = true;
    try
    {
      VerifyDMO dmo = new VerifyDMO();
      create = dmo.createTab(vo);
    }
    catch (Exception ex) {
      Log.getInstance(VerifyBO.class).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
    return create;
  }

  private boolean dropTab(InputDataVO vo)
    throws BusinessException
  {
    boolean drop = true;
    try
    {
      VerifyDMO dmo = new VerifyDMO();
      drop = dmo.dropTab(vo);
    }
    catch (Exception ex) {
      Log.getInstance(VerifyBO.class).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
    return drop;
  }

  private Long getClbh()
    throws BusinessException
  {
    long t = 0L;
    try {
      long time = System.currentTimeMillis();
      Calendar calendar = Calendar.getInstance();
      Date d = new Date(time);
      calendar.setTime(d);
      int year = calendar.get(1);
      int month = calendar.get(2) + 1;
      int date = calendar.get(5);
      int hour = calendar.get(11);
      int minute = calendar.get(12);
      int second = calendar.get(13);
      int millisecond = calendar.get(14);

      t += year;
      t *= 100L;
      t += month;
      t *= 100L;
      t += date;
      t *= 100L;
      t += hour;
      t *= 100L;
      t += minute;
      t *= 100L;
      t += second;
      t *= 1000L;
      t += millisecond;
    }
    catch (Exception ex) {
      Log.getInstance(VerifyBO.class).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }

    return new Long(t);
  }

  private DapMsgVO getDapmsgVO(DJCLBVO infoVO)
    throws BusinessException
  {
    DapMsgVO PfStateVO = new DapMsgVO();

    ForDapMsgParaVO paraVO = getDataForDapMsgVO(infoVO);

    PfStateVO.setCorp(infoVO.getDwbm());
    PfStateVO.setSys("EC");
    PfStateVO.setProc(paraVO.getDjlx());
    PfStateVO.setBusiType(null);
    PfStateVO.setBusiName(paraVO.getDjlxmc());
    PfStateVO.setProcMsg(infoVO.getClbh());
    PfStateVO.setBillCode(infoVO.getClbh());
    PfStateVO.setBusiDate(infoVO.getClrq());
    PfStateVO.setOperator(infoVO.getClr());
    PfStateVO.setCurrency(paraVO.getBzmc());
    PfStateVO.setMoney(infoVO.getJfclybje());
    PfStateVO.setComment("");
    PfStateVO.setChecker(infoVO.getClr());
    PfStateVO.setSettleMode("");
    PfStateVO.setDocNum("");
    PfStateVO.setDocDate(null);

    Logger.info(NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000010") + infoVO.getDwbm() + " Clbh:" + infoVO.getClbh() + ")*******");

    return PfStateVO;
  }

  private DjclDapVO getDapVO(DJCLBVO vo, DapMsgVO msgVO)
    throws BusinessException
  {
    DjclDapVO dapVO = new DjclDapVO();
    try {
      HXDMO hd = new HXDMO();
      dapVO = hd.queryData(msgVO.getProc(), vo.getClbh());
    } catch (Exception e) {
      Log.getInstance(VerifyBO.class).error(e.getMessage(), e);
      throw new BusinessException(e.getMessage());
    }

    return dapVO;
  }

  private ForDapMsgParaVO getDataForDapMsgVO(DJCLBVO infoVO)
    throws BusinessException
  {
    ForDapMsgParaVO paraVO = new ForDapMsgParaVO();
    String djlxmc = null;
    String djlx = null;
    try
    {
      VerifyDMO dmo = new VerifyDMO();

      paraVO = dmo.getDataForDapMsgVO(infoVO);

      if ((paraVO.getClbz() == 0) || (paraVO.getClbz() == -1) || (paraVO.getClbz() == 2) || (paraVO.getClbz() == -2) || (paraVO.getClbz() == 10)) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000011");
        djlx = "D9";
      }
      else if (((paraVO.getClbz() == 5) || (paraVO.getClbz() == 6)) && (paraVO.getWldx() == 0) && (paraVO.getDjdl().equals("ys"))) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000012");
        djlx = "DA";
      }
      else if (((paraVO.getClbz() == 5) || (paraVO.getClbz() == 6)) && (paraVO.getWldx() == 1) && (paraVO.getDjdl().equals("yf"))) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000013");
        djlx = "DB";
      }
      else if (((paraVO.getClbz() == 5) || (paraVO.getClbz() == 6)) && (paraVO.getWldx() == 0) && (paraVO.getDjdl().equals("sk"))) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000014");
        djlx = "DN";
      }
      else if (((paraVO.getClbz() == 5) || (paraVO.getClbz() == 6)) && (paraVO.getWldx() == 1) && (paraVO.getDjdl().equals("fk"))) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000015");
        djlx = "DO";
      }
      else if (((paraVO.getClbz() == 5) || (paraVO.getClbz() == 6)) && (paraVO.getWldx() > 1) && (paraVO.getDjdl().equals("ys"))) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000016");
        djlx = "DC";
      }
      else if (((paraVO.getClbz() == 5) || (paraVO.getClbz() == 6)) && (paraVO.getWldx() > 1) && (paraVO.getDjdl().equals("yf"))) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000017");
        djlx = "DD";
      }
      else if (((paraVO.getClbz() == 5) || (paraVO.getClbz() == 6)) && (paraVO.getWldx() > 1) && (paraVO.getDjdl().equals("sk"))) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000018");
        djlx = "DP";
      }
      else if (((paraVO.getClbz() == 5) || (paraVO.getClbz() == 6)) && (paraVO.getWldx() > 1) && (paraVO.getDjdl().equals("fk"))) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000019");
        djlx = "DQ";
      }
      else if ((paraVO.getClbz() == 3) && (paraVO.getWldx() == 0)) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000020");
        djlx = "DH";
      }
      else if ((paraVO.getClbz() == 3) && (paraVO.getWldx() > 1)) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000021");
        djlx = "DI";
      }
      else if ((paraVO.getClbz() == 4) && (paraVO.getWldx() == 0)) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000022");
        djlx = "DL";
      }
      else if ((paraVO.getClbz() == 4) && (paraVO.getWldx() > 1)) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000023");
        djlx = "DM";
      }
      else if (paraVO.getClbz() == 1) {
        djlxmc = NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000024");
        djlx = "DU";
      }
      paraVO.setDjlx(djlx);
      paraVO.setDjlxmc(djlxmc);
    }
    catch (SQLException ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    } catch (Exception ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
    return paraVO;
  }

  private String getRandomNum()
    throws BusinessException
  {
    long t = 0L;
    try
    {
      long time = System.currentTimeMillis();
      Calendar calendar = Calendar.getInstance();
      Date d = new Date(time);
      calendar.setTime(d);
      int hour = calendar.get(11);
      int minute = calendar.get(12);
      int second = calendar.get(13);
      int millisecond = calendar.get(14);
      t += hour;
      t *= 100L;
      t += minute;
      t *= 100L;
      t += second;
      t *= 1000L;
      t += millisecond;
    } catch (Exception ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }

    return new Long(t).toString();
  }

  private int getVerifyClid(String sDwbm)
    throws BusinessException
  {
    int sClid = -1;
    try {
      VerifyDMO dmo = new VerifyDMO();
      sClid = dmo.getVerifyClid(sDwbm);
    }
    catch (Exception ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
    return sClid;
  }

  private Vector getVerifyDataForVIT(VITFilterCondVO condVO)
    throws BusinessException
  {
    Vector DataResult = new Vector();
    try
    {
      VerifyDMO dmo = new VerifyDMO();

      DataResult = dmo.getVerifyDataForVIT(condVO);
    } catch (Exception ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
    return DataResult;
  }

  public RemoteTransferVO getVerifyDetailRecBySum(DJCLBVO vo)
    throws BusinessException
  {
    RemoteTransferVO remoteVO = new RemoteTransferVO();
    try {
      VerifyDMO dmo = new VerifyDMO();
      String dwbm = vo.getDwbm();
      String sign = "Verify";
      AgiotageDMO dmoAg = new AgiotageDMO();
      AgiotageVO voAg = new AgiotageVO();
      voAg.setDwbm(dwbm);
      voAg.setSign(sign);
      Hashtable hBzDig = dmoAg.getAllBzbmOfHisRecord(voAg);

      Vector hzvRecordData = dmo.getVerifyDetailRecBySum(vo, hBzDig);
      remoteVO.setTranData1(hzvRecordData);
    }
    catch (Exception ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
    return remoteVO;
  }

  private boolean insertIntoTabForVIT(VITFilterCondVO condVO)
    throws BusinessException
  {
    boolean create = false;
    try {
      VerifyDMO dmo = new VerifyDMO();

      create = dmo.insertIntoTabForVIT(condVO);

      return create;
    }
    catch (Exception ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
    throw new BusinessException(ex.getMessage());
    }
  }

  public void onCancelAgiotage(AgiotageVO vo)
    throws BusinessException
  {
    try
    {
      String[] sArrErr = new String[2];
      Vector dJCLv = vo.getSelBzbm();

      sArrErr = onCancelVerify(dJCLv);
      if ((sArrErr[1] != null) && (sArrErr[1].length() != 0)) {
        throw new BusinessException(sArrErr[1]);
      }

    }
    catch (Exception ex)
    {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
  }

  public String[] onCancelVerify(Vector dJCLv)
    throws BusinessException
  {
    String sUnErrData = "";
    String sPfErrData = "";
    String[] sArrErr = new String[2];
    UFDouble zero = new UFDouble(0);
    Hashtable hdjcljeA = new Hashtable();
    Hashtable hdjcljeB = new Hashtable();
    try
    {
      Vector arrDjcl = new Vector();
      Vector arrDel = new Vector();

      Vector vYSvos = new Vector();
      DJCLBDMO clbdmo = new DJCLBDMO();
      VerifyDMO dmo = new VerifyDMO();
      DJZBDAO zbdmo = new DJZBDAO();
      DJZBBO djzbbo = new DJZBBO();
      DJZBItemVO item = null;

      ArapExtInfRunBO extbo = new ArapExtInfRunBO();

      BusiTransVO[] busitransvos = extbo.initBusiTrans("unverify", new Integer(777));

      Hashtable hash = new Hashtable();
      Hashtable hashErr = new Hashtable();

      Hashtable ts = new Hashtable();

      for (int i = 0; i < dJCLv.size(); i++)
      {
        DJCLBVO dVo = (DJCLBVO)dJCLv.elementAt(i);

        ts.put(dVo.getClbh(), dVo.getClbh());
      }

      HZYEBO.compareDr(ts, "arap_djclb", "clbh");

      for (int i = 0; i < dJCLv.size(); i++)
      {
        DJCLBVO dVo = (DJCLBVO)dJCLv.elementAt(i);

        if (hash.get(dVo.getClbh()) != null) {
          continue;
        }
        hash.put(dVo.getClbh(), dVo.getClbh());

        int iClbz = dVo.getClbz().intValue();

        if (iClbz == 0)
        {
          arrDjcl = clbdmo.findByclbh(dVo.getClbh());
        }

        if ((iClbz == 10) || (iClbz == 11))
        {
          arrDjcl = clbdmo.findByclbh(dVo.getClbh());
        }

        if (iClbz == 1)
        {
          arrDjcl = clbdmo.getAgiotageRecord(dVo);
        }

        if (iClbz == 2) {
          String sErrDataOT = clbdmo.getUniteAccountErrData(dVo);
          if (sErrDataOT.trim().length() != 0) {
            sUnErrData = sUnErrData + sErrDataOT;

            hashErr.put(dVo.getClbh(), "erro");
          }
          else {
            arrDjcl = clbdmo.getUniteAccountRecord(dVo);
            arrDel = clbdmo.getDelUNHisRecord(dVo);
          }
        }
        else {
          if ((iClbz == 3) || (iClbz == 4)) {
            arrDjcl = clbdmo.getHzRecord(dVo);
          }

          if (arrDjcl.size() == 0) {
            continue;
          }
          DJCLBVO[] clvos = new DJCLBVO[arrDjcl.size()];

          clvos = (DJCLBVO[])(DJCLBVO[])arrDjcl.toArray(clvos);

          beforeCancelVerifyInf(busitransvos, clvos);

          UFDouble ZERO = new UFDouble(0);
          UFDouble sumyb = ZERO;
          UFDouble sumfb = ZERO;
          UFDouble sumbb = ZERO;
          UFDouble sumsl = ZERO;

          UFDouble dYBFse = ZERO;
          UFDouble dFBfse = ZERO;
          UFDouble dBBfse = ZERO;
          UFDouble dSl = ZERO;

          UFDouble fksumyb = ZERO;
          UFDouble fksumfb = ZERO;
          UFDouble fksumbb = ZERO;
          UFDouble fksumsl = ZERO;

          UFDouble fkdYBFse = ZERO;
          UFDouble fkdFBfse = ZERO;
          UFDouble fkdBBfse = ZERO;
          UFDouble fkdSl = ZERO;

          for (int j = 0; j < arrDjcl.size(); j++)
          {
            DJCLBVO dJCLvo = (DJCLBVO)arrDjcl.elementAt(j);

            Object DListA = null;

            DListA = hdjcljeA.get(dJCLvo.getFb_oid());

            if (DListA == null) {
              DListA = new ArrayList();
            }
            else {
              sumyb = (UFDouble)((ArrayList)DListA).get(2);
              sumfb = (UFDouble)((ArrayList)DListA).get(3);
              sumbb = (UFDouble)((ArrayList)DListA).get(4);
              sumsl = (UFDouble)((ArrayList)DListA).get(5);
            }

            dYBFse = dJCLvo.getJfclybje().add(dJCLvo.getDfclybje());
            sumyb = sumyb.sub(dYBFse);

            dFBfse = dJCLvo.getJfclfbje().add(dJCLvo.getDfclfbje());
            sumfb = sumfb.sub(dFBfse);

            dBBfse = dJCLvo.getJfclbbje().add(dJCLvo.getDfclbbje());
            sumbb = sumbb.sub(dBBfse);

            dSl = dJCLvo.getJfclshl().add(dJCLvo.getDfclshl());
            sumsl = sumsl.sub(dSl);

            ((ArrayList)DListA).add(0, dJCLvo.getVouchid());
            ((ArrayList)DListA).add(1, dJCLvo.getFb_oid());
            ((ArrayList)DListA).add(2, sumyb);
            ((ArrayList)DListA).add(3, sumfb);
            ((ArrayList)DListA).add(4, sumbb);
            ((ArrayList)DListA).add(5, sumsl);

            hdjcljeA.put(dJCLvo.getFb_oid(), DListA);

            Object DListB = null;

            DListB = hdjcljeB.get(dJCLvo.getFkxyb_oid());

            if (DListB == null) {
              DListB = new ArrayList();
            }
            else {
              fksumyb = (UFDouble)((ArrayList)DListB).get(1);
              fksumfb = (UFDouble)((ArrayList)DListB).get(2);
              fksumbb = (UFDouble)((ArrayList)DListB).get(3);
              fksumsl = (UFDouble)((ArrayList)DListB).get(4);
            }

            fkdYBFse = dJCLvo.getJfclybje().add(dJCLvo.getDfclybje());
            fksumyb = fksumyb.sub(fkdYBFse);

            fkdFBfse = dJCLvo.getJfclfbje().add(dJCLvo.getDfclfbje());
            fksumfb = fksumfb.sub(fkdFBfse);

            fkdBBfse = dJCLvo.getJfclbbje().add(dJCLvo.getDfclbbje());
            fksumbb = fksumbb.sub(fkdBBfse);

            fkdSl = dJCLvo.getJfclshl().add(dJCLvo.getDfclshl());
            fksumsl = fksumsl.sub(fkdSl);

            ((ArrayList)DListB).add(0, dJCLvo.getFkxyb_oid());
            ((ArrayList)DListB).add(1, fksumyb);
            ((ArrayList)DListB).add(2, fksumfb);
            ((ArrayList)DListB).add(3, fksumbb);
            ((ArrayList)DListB).add(4, fksumsl);

            hdjcljeB.put(dJCLvo.getFkxyb_oid(), DListB);

            sumyb = zero;
            sumfb = zero;
            sumbb = zero;
            sumsl = zero;
            dYBFse = zero;
            dFBfse = zero;
            dBBfse = zero;
            dSl = zero;
            fksumyb = zero;
            fksumfb = zero;
            fksumbb = zero;
            fksumsl = zero;
            fkdYBFse = zero;
            fkdFBfse = zero;
            fkdBBfse = zero;
            fkdSl = zero;
          }

          DJCLBVO paraVO = (DJCLBVO)dVo.clone();
          long t2 = System.currentTimeMillis();
          afterCancelVerifyInf(busitransvos, paraVO);

          if (arrDel == null) {
            continue;
          }
          for (int k = 0; k < arrDel.size(); k++) {
            DJCLBVO voDel = (DJCLBVO)arrDel.elementAt(k);
            String fboid = voDel.getFb_oid();

            item = zbdmo.findItemByPrimaryKey(fboid);
            if (item.getFx().intValue() == -1) {
              item.setYbye(item.getDfybje());
              item.setFbye(item.getDffbje());
              item.setBbye(item.getDfbbje());
            } else {
              item.setYbye(item.getJfybje());
              item.setFbye(item.getJffbje());
              item.setBbye(item.getJfbbje());
            }
            DJZBHeaderVO head = djzbbo.findHeaderByPrimaryKey(item.getVouchid());
            DJ_IAccessableBusiVO psTemp = new DJ_IAccessableBusiVO();
            psTemp.setIscontrary(true);
            DJZBVO temp_dj = new DJZBVO();
            temp_dj.setParentVO(head);
            temp_dj.setChildrenVO(new DJZBItemVO[] { item });
            psTemp.setDj(temp_dj);
            vYSvos.add(psTemp);
            zbdmo.deleteItemPK(fboid);
          }
        }

      }

      if (hdjcljeA.size() != 0)
      {
        dmo.updateDJFB(hdjcljeA, false, "");
      }
      if (hdjcljeB.size() != 0)
      {
        dmo.updateDJFKXYB(hdjcljeB);
      }

      ResMessage res = YSControl(vYSvos);
      if (!res.isSuccess) {
        throw new Exception(res.strMessage);
      }
      hash.clear();
      for (int i = 0; i < dJCLv.size(); i++)
      {
        DJCLBVO pfdvo = (DJCLBVO)dJCLv.elementAt(i);
        if ((hash.get(pfdvo.getClbh()) != null) || (hashErr.get(pfdvo.getClbh()) != null)) {
          continue;
        }
        hash.put(pfdvo.getClbh(), pfdvo.getClbh());

        onLinkPFForDisOper(pfdvo);

        clbdmo.deleteUnHisRecord(pfdvo);
      }

      new CheckBalanceDMO().checkye(hdjcljeA);

      sArrErr[0] = sUnErrData;
      sArrErr[1] = sPfErrData;
    } catch (Exception ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
    return sArrErr;
  }

  public void onLinkPFForDisOper(DJCLBVO infoVO)
    throws BusinessException
  {
    try
    {
      DapMsgVO PfStateVO = getDapmsgVO(infoVO);
      PfStateVO.setRequestNewTranscation(false);

      PfStateVO.setMsgType(1);

      Logger.info(NCLangResOnserver.getInstance().getStrByID("2006030201", "UPP2006030201-000029"));

      new FipCallFacade().sendMessage(PfStateVO, null);
    }
    catch (Exception ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
  }

  public String onVerify(Vector vdata)
    throws BusinessException
  {
    String clbh = null;
    boolean bLinkPFInterFace = true;
    Connection con = null;
    String dwbm = null;
    String clr = null;
    UFDate clrq = null;
    UFDouble clje = new UFDouble(0);
    int clbz = -1;
    UFDouble zero = new UFDouble(0);
    Hashtable ts = new Hashtable();
    try
    {
      VerifyDMO dmo = new VerifyDMO();
      DJCLBDMO cldmo = new DJCLBDMO();

      con = dmo.getDBConnection();

      DJCLBVO[] clvos = new DJCLBVO[vdata.size()];
      clvos = (DJCLBVO[])(DJCLBVO[])vdata.toArray(clvos);
      if ((clvos[0].getClbh() == null) || (clvos[0].getClbh().length() == 0))
      {
        clbh = getClbh().toString();
      }
      else
      {
        clbh = clvos[0].getClbh();
      }

      long t1 = System.currentTimeMillis();
      ArapExtInfRunBO extbo = new ArapExtInfRunBO();

      BusiTransVO[] busitransvos = extbo.initBusiTrans("verify", new Integer(777));
      beforeVerifyInf(busitransvos, clvos);
      Logger.info("外接口核销单据前动作前所用时间:" + (System.currentTimeMillis() - t1));

      adjustDJCLBVO(clvos);
      cldmo.insert(con, clvos, clbh);

      Hashtable hdjcljeA = new Hashtable();
      Hashtable hdjcljeB = new Hashtable();

      UFDouble ZERO = new UFDouble(0);
      UFDouble sumyb = ZERO;
      UFDouble sumfb = ZERO;
      UFDouble sumbb = ZERO;
      UFDouble sumsl = ZERO;

      UFDouble dYBFse = ZERO;
      UFDouble dFBfse = ZERO;
      UFDouble dBBfse = ZERO;
      UFDouble dSl = ZERO;

      UFDouble fksumyb = ZERO;
      UFDouble fksumfb = ZERO;
      UFDouble fksumbb = ZERO;
      UFDouble fksumsl = ZERO;

      UFDouble fkdYBFse = ZERO;
      UFDouble fkdFBfse = ZERO;
      UFDouble fkdBBfse = ZERO;
      UFDouble fkdSl = ZERO;

      for (int i = 0; i < vdata.size(); i++) {
        DJCLBVO dJCLvo = new DJCLBVO();
        dJCLvo = (DJCLBVO)vdata.elementAt(i);

        if (((dJCLvo.getClbz().intValue() != 5) || (dJCLvo.getHxbh().intValue() != 1)) && ((dJCLvo.getClbz().intValue() != 6) || (dJCLvo.getHxbh().intValue() != 0)))
        {
          ts.put(dJCLvo.getVouchid(), dJCLvo.getM_ts());
        }

        dwbm = dJCLvo.getDwbm();
        clr = dJCLvo.getClr();
        clrq = dJCLvo.getClrq();
        clbz = dJCLvo.getClbz().intValue();

        if ((dJCLvo.getHxbh().intValue() == 0) && (clbz != 6)) {
          UFDouble temje = dJCLvo.getJfclybje().add(dJCLvo.getDfclybje());
          clje = clje.add(temje.abs());
        }

        if (dJCLvo.getClbz().intValue() == 100)
        {
          dJCLvo.setClbz(new Integer(5));
        }
        else
        {
          dJCLvo.setClbz(new Integer(-1));

          Object DListA = null;

          DListA = hdjcljeA.get(dJCLvo.getFb_oid());

          if (DListA == null) {
            DListA = new ArrayList();
          }
          else {
            sumyb = (UFDouble)((ArrayList)DListA).get(2);
            sumfb = (UFDouble)((ArrayList)DListA).get(3);
            sumbb = (UFDouble)((ArrayList)DListA).get(4);
            sumsl = (UFDouble)((ArrayList)DListA).get(5);
          }

          dYBFse = dJCLvo.getJfclybje().add(dJCLvo.getDfclybje());
          sumyb = sumyb.add(dYBFse);

          dFBfse = dJCLvo.getJfclfbje().add(dJCLvo.getDfclfbje());
          sumfb = sumfb.add(dFBfse);

          dBBfse = dJCLvo.getJfclbbje().add(dJCLvo.getDfclbbje());
          sumbb = sumbb.add(dBBfse);

          dSl = dJCLvo.getJfclshl().add(dJCLvo.getDfclshl());
          sumsl = sumsl.add(dSl);

          ((ArrayList)DListA).add(0, dJCLvo.getVouchid());
          ((ArrayList)DListA).add(1, dJCLvo.getFb_oid());
          ((ArrayList)DListA).add(2, sumyb);
          ((ArrayList)DListA).add(3, sumfb);
          ((ArrayList)DListA).add(4, sumbb);
          ((ArrayList)DListA).add(5, sumsl);

          hdjcljeA.put(dJCLvo.getFb_oid(), DListA);

          Object DListB = null;

          DListB = hdjcljeB.get(dJCLvo.getFkxyb_oid());

          if (DListB == null) {
            DListB = new ArrayList();
          }
          else {
            fksumyb = (UFDouble)((ArrayList)DListB).get(1);
            fksumfb = (UFDouble)((ArrayList)DListB).get(2);
            fksumbb = (UFDouble)((ArrayList)DListB).get(3);
            fksumsl = (UFDouble)((ArrayList)DListB).get(4);
          }

          fkdYBFse = dJCLvo.getJfclybje().add(dJCLvo.getDfclybje());
          fksumyb = fksumyb.add(fkdYBFse);

          fkdFBfse = dJCLvo.getJfclfbje().add(dJCLvo.getDfclfbje());
          fksumfb = fksumfb.add(fkdFBfse);

          fkdBBfse = dJCLvo.getJfclbbje().add(dJCLvo.getDfclbbje());
          fksumbb = fksumbb.add(fkdBBfse);

          fkdSl = dJCLvo.getJfclshl().add(dJCLvo.getDfclshl());
          fksumsl = fksumsl.add(fkdSl);

          ((ArrayList)DListB).add(0, dJCLvo.getFkxyb_oid());
          ((ArrayList)DListB).add(1, fksumyb);
          ((ArrayList)DListB).add(2, fksumfb);
          ((ArrayList)DListB).add(3, fksumbb);
          ((ArrayList)DListB).add(4, fksumsl);

          hdjcljeB.put(dJCLvo.getFkxyb_oid(), DListB);

          sumyb = zero;
          sumfb = zero;
          sumbb = zero;
          sumsl = zero;

          dYBFse = zero;
          dFBfse = zero;
          dBBfse = zero;
          dSl = zero;

          fksumyb = zero;
          fksumfb = zero;
          fksumbb = zero;
          fksumsl = zero;
          fkdYBFse = zero;
          fkdFBfse = zero;
          fkdBBfse = zero;
          fkdSl = zero;
        }
      }

      HZYEBO.compareTS(ts, "arap_djzb", "vouchid");

      dmo.updateDJFB(hdjcljeA, true, clrq.toString());
      dmo.updateDJFKXYB(hdjcljeB);

      new CheckBalanceDMO().checkye(hdjcljeA);

      if (clbz == 6)
        clbz = 5;
      if (clbz == -2)
        clbz = 0;
      DJCLBVO paraVO = new DJCLBVO();
      paraVO.setDwbm(dwbm);
      paraVO.setClbz(new Integer(clbz));
      paraVO.setClbh(clbh.toString());
      paraVO.setJfclybje(clje);
      paraVO.setClrq(clrq);
      paraVO.setClr(clr);

      if ((clvos != null) && (clvos.length > 0))
      {
        afterVerifyInf(busitransvos, clvos, paraVO, bLinkPFInterFace);
      }

    }
    catch (Exception ex)
    {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    } finally {
      try {
        if (con != null)
          con.close();
      } catch (Exception e) {
      }
    }
    return clbh;
  }

  public RemoteTransferVO onVITSelectData(VITFilterCondVO vo)
    throws BusinessException
  {
    RemoteTransferVO remoteVO = new RemoteTransferVO();
    InputDataVO condVO = new InputDataVO();
    String serNum = null;
    try {
      serNum = getRandomNum();

      condVO.setIsDebit(true);
      condVO.setTemTabNm("Debit" + serNum);
      vo.setIsDebit(true);
      vo.setTemTabNm("Debit" + serNum);
      createTab(condVO);
      insertIntoTabForVIT(vo);
      Vector vDebitData = getVerifyDataForVIT(vo);
      dropTab(condVO);

      condVO.setIsDebit(false);
      condVO.setTemTabNm("Credit" + serNum);
      vo.setIsDebit(false);
      vo.setTemTabNm("Credit" + serNum);
      createTab(condVO);
      insertIntoTabForVIT(vo);
      Vector vCreditData = getVerifyDataForVIT(vo);
      dropTab(condVO);

      int iClid = getVerifyClid(vo.getDwbm());

      remoteVO.setTranData1(vDebitData);
      remoteVO.setTranData2(vCreditData);
      remoteVO.setString1(String.valueOf(iClid));
    }
    catch (Exception ex) {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
    return remoteVO;
  }
  public ResMessage YSControl(Vector v) throws Exception {
    ResMessage res = new ResMessage();
    if ((v == null) || (v.size() < 1))
      return res;
    if (!BudgetInfo.isInUse(((DJ_IAccessableBusiVO)v.get(0)).getHead().getDwbm())) {
      return res;
    }
    ArapBusinessException YsControlE = new ArapBusinessException();
    try
    {
      DJ_IAccessableBusiVO[] ps = null;

      ps = new DJ_IAccessableBusiVO[v.size()];
      v.copyInto(ps);

      NtbCtlInfoVO tpcontrolvo = BudgetControlServiceGetter.getIBudgetControl().getControlInfo(ps);

      if ((tpcontrolvo != null) && (tpcontrolvo.isControl()))
      {
        res.isSuccess = false;
        res.intValue = 7;
        String[] infos = tpcontrolvo.getControlInfos();
        for (int j = 0; j < infos.length; j++) {
          res.strMessage = (res.strMessage + "\n" + infos[j]);
        }
        YsControlE = new ArapBusinessException(res.strMessage);
        YsControlE.m_ResMessage = res;

        throw YsControlE;
      }

      if ((tpcontrolvo != null) && (tpcontrolvo.isAlarm()))
      {
        res.isSuccess = true;
        String[] seminfos = tpcontrolvo.getAlarmInfos();
        for (int j = 0; j < seminfos.length; j++)
          res.strMessage = (res.strMessage + "\n" + seminfos[j]);
      }
    }
    catch (Exception ex)
    {
      Log.getInstance(getClass()).error(ex.getMessage(), ex);
      throw new BusinessException(ex.getMessage());
    }
    return res;
  }

  private DJCLBVO[] adjustDJCLBVO(DJCLBVO[] vos)
  {
    if ((vos == null) || (vos.length == 0))
    {
      return vos;
    }

    for (int i = 0; i < vos.length; i++)
    {
      int clbz = vos[i].getClbz().intValue();

      if ((clbz == 0) || (clbz == -1))
      {
        vos[i].setJfclybje2(vos[i].getDfclybje());
        vos[i].setJfclfbje2(vos[i].getDfclfbje());
        vos[i].setJfclbbje2(vos[i].getDfclbbje());
        vos[i].setDfclybje2(vos[i].getJfclybje());
        vos[i].setDfclfbje2(vos[i].getJfclfbje());
        vos[i].setDfclbbje2(vos[i].getJfclbbje());
        vos[i].setJfclshl2(vos[i].getDfclshl());
        vos[i].setDfclshl2(vos[i].getJfclshl());
        if (clbz == 0)
        {
          vos[i].setZy("UPP20060504-000237");
        }
        else if (clbz == -1)
        {
          vos[i].setZy("UPP20060504-000236");
        }
        else if (clbz == 4)
        {
          vos[i].setZy("UPP20060504-000240");
        }

      }
      else if (clbz == 3)
      {
        if (vos[i].getJfclybje().doubleValue() > 0.0D)
        {
          vos[i].setJfclybje2(vos[i].getJfclybje().multiply(-1.0D));
          vos[i].setJfclfbje2(vos[i].getJfclfbje().multiply(-1.0D));
          vos[i].setJfclbbje2(vos[i].getJfclbbje().multiply(-1.0D));
          vos[i].setDfclybje2(vos[i].getDfclybje().multiply(-1.0D));
          vos[i].setDfclfbje2(vos[i].getDfclfbje().multiply(-1.0D));
          vos[i].setDfclbbje2(vos[i].getDfclbbje().multiply(-1.0D));
          vos[i].setJfclshl2(vos[i].getJfclshl().multiply(-1.0D));
          vos[i].setDfclshl2(vos[i].getDfclshl().multiply(-1.0D));
          vos[i].setZy("UPP20060504-000239");
          vos[(i + 1)].setJfclybje2(new UFDouble(0));
          vos[(i + 1)].setJfclfbje2(new UFDouble(0));
          vos[(i + 1)].setJfclbbje2(new UFDouble(0));
          vos[(i + 1)].setDfclybje2(new UFDouble(0));
          vos[(i + 1)].setDfclfbje2(new UFDouble(0));
          vos[(i + 1)].setDfclbbje2(new UFDouble(0));
          vos[(i + 1)].setJfclshl2(new UFDouble(0));
          vos[(i + 1)].setDfclshl2(new UFDouble(0));
          vos[(i + 1)].setZy("UPP20060504-000239");
        }
        else
        {
          vos[(i + 1)].setJfclybje2(vos[(i + 1)].getJfclybje().multiply(-1.0D));
          vos[(i + 1)].setJfclfbje2(vos[(i + 1)].getJfclfbje().multiply(-1.0D));
          vos[(i + 1)].setJfclbbje2(vos[(i + 1)].getJfclbbje().multiply(-1.0D));
          vos[(i + 1)].setDfclybje2(vos[(i + 1)].getDfclybje().multiply(-1.0D));
          vos[(i + 1)].setDfclfbje2(vos[(i + 1)].getDfclfbje().multiply(-1.0D));
          vos[(i + 1)].setDfclbbje2(vos[(i + 1)].getDfclbbje().multiply(-1.0D));
          vos[(i + 1)].setJfclshl2(vos[(i + 1)].getJfclshl().multiply(-1.0D));
          vos[(i + 1)].setDfclshl2(vos[(i + 1)].getDfclshl().multiply(-1.0D));
          vos[(i + 1)].setZy("UPP20060504-000239");
          vos[i].setJfclybje2(new UFDouble(0));
          vos[i].setJfclfbje2(new UFDouble(0));
          vos[i].setJfclbbje2(new UFDouble(0));
          vos[i].setDfclybje2(new UFDouble(0));
          vos[i].setDfclfbje2(new UFDouble(0));
          vos[i].setDfclbbje2(new UFDouble(0));
          vos[i].setJfclshl2(new UFDouble(0));
          vos[i].setDfclshl2(new UFDouble(0));
          vos[i].setZy("UPP20060504-000239");
        }

        i++;
      }
      else if (clbz == 4)
      {
        vos[i].setJfclybje2(vos[i].getDfclybje());
        vos[i].setJfclfbje2(vos[i].getDfclfbje());
        vos[i].setJfclbbje2(vos[i].getDfclbbje());
        vos[i].setDfclybje2(vos[i].getJfclybje());
        vos[i].setDfclfbje2(vos[i].getJfclfbje());
        vos[i].setDfclbbje2(vos[i].getJfclbbje());
        vos[i].setJfclshl2(vos[i].getDfclshl());
        vos[i].setDfclshl2(vos[i].getJfclshl());

        if ((vos[i].getDfclybje().doubleValue() > 0.0D) && (vos[i].getJfclybje().doubleValue() == 0.0D)) {
          vos[i].setZy("UPP20060504-000240");
        }

        if ((vos[i].getJfclybje().doubleValue() > 0.0D) && (vos[i].getDfclybje().doubleValue() == 0.0D)) {
          vos[i].setZy("UPP20060504-000237");
        }

      }
      else if (clbz == -2)
      {
        vos[i].setJfclybje2(vos[i].getDfclybje());
        vos[i].setJfclfbje2(vos[i].getDfclfbje());
        vos[i].setJfclbbje2(vos[i].getDfclbbje());
        vos[i].setDfclybje2(vos[i].getJfclybje());
        vos[i].setDfclfbje2(vos[i].getJfclfbje());
        vos[i].setDfclbbje2(vos[i].getJfclbbje());
        vos[i].setJfclshl2(vos[i].getDfclshl());
        vos[i].setDfclshl2(vos[i].getJfclshl());

        vos[i].setZy("UPP20060504-000301");
      }
      else
      {
        vos[i].setJfclybje2(vos[i].getJfclybje().multiply(-1.0D));
        vos[i].setJfclfbje2(vos[i].getJfclfbje().multiply(-1.0D));
        vos[i].setJfclbbje2(vos[i].getJfclbbje().multiply(-1.0D));
        vos[i].setDfclybje2(vos[i].getDfclybje().multiply(-1.0D));
        vos[i].setDfclfbje2(vos[i].getDfclfbje().multiply(-1.0D));
        vos[i].setDfclbbje2(vos[i].getDfclbbje().multiply(-1.0D));
        vos[i].setJfclshl2(vos[i].getJfclshl().multiply(-1.0D));
        vos[i].setDfclshl2(vos[i].getDfclshl().multiply(-1.0D));
        if (clbz == 10)
        {
          vos[i].setZy("UPP20060504-000297");
        }
        else if (clbz == 11)
        {
          vos[i].setZy("UPP20060504-000298");
        }
        else if (clbz == 1)
        {
          vos[i].setZy("UPP20060504-000060");
        }
        else if (clbz == 2)
        {
          vos[i].setZy("UPP20060504-000238");
        }
        else if (clbz == 5)
        {
          vos[i].setZy("UPP20060504-000058");
        }
        else if (clbz == 6)
        {
          vos[i].setZy("UPP20060504-000059");
        }
        else if (clbz == 100)
        {
          vos[i].setZy("UPP20060504-000058");
        }

      }

      if (clbz != 100)
        continue;
      vos[i].setClbz(new Integer(5));
    }

    return vos;
  }
}