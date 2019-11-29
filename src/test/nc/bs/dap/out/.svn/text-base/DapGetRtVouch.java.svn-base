package nc.bs.dap.out;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import nc.bs.dap.out.rtvoucher.SubviewUtil;
import nc.bs.dap.rtvouch.DapFinIndexDMO;
import nc.bs.dap.rtvouch.RtVouchDMO;
import nc.bs.dap.rtvouch.RtVouchException;
import nc.bs.dap.rtvouch.RtvouchBDMO;
import nc.bs.fipf.pub.BsPubUtil;
import nc.bs.fipf.pub.TaskRedirectProxy;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.fi.fip.pub.IUFTypesInitializationValue;
import nc.itf.fi.pub.Accsubj;
import nc.itf.fi.pub.Cumandoc;
import nc.itf.fi.pub.GLOrgBookAcc;
import nc.vo.bd.access.BdinfoVO;
import nc.vo.bd.b02.AccsubjVO;
import nc.vo.bd.b09.CumandocVO;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.dap.inteface.DetailVO;
import nc.vo.dap.inteface.FreevalueVO;
import nc.vo.dap.inteface.IAccountGetBalanceInfo;
import nc.vo.dap.inteface.IFetchCashFlow;
import nc.vo.dap.inteface.VoucherVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.dap.pub.DapBusinessException;
import nc.vo.dap.rtvouch.DapFactorVO;
import nc.vo.dap.rtvouch.DapFinMsgVO;
import nc.vo.dap.subjclass02.SubjviewVO;
import nc.vo.dap.vouchtemp.VouchParaVO;
import nc.vo.dap.vouchtemp.VoucherTemplateVO;
import nc.vo.dap.vouchtemp.VouchtempBVO;
import nc.vo.dap.vouchtemp.VouchtempVO;
import nc.vo.dap.vouchtemp.VouchtempassVO;
import nc.vo.fipf.pub.PfComm;
import nc.vo.gl.cashflowcase.CashflowcaseVO;
import nc.vo.gl.pubvoucher.UserDataVO;
import nc.vo.pf.pub.PfFormException;
import nc.vo.pf.pub.PfFormulaParse;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.plus.FormulaElement;

public class DapGetRtVouch
{
  private VouchtempVO m_vouchTempHeadVo = null;
  private VouchtempBVO[] m_vouchTempDetailVos = null;
  private VouchtempassVO[] m_vouchTempAssVos = null;

  private Hashtable m_tempDetailAssHas = null;

  private VoucherVO m_rtVouchVo = null;
  private DetailVO[] m_rtVouchbVos = null;

  private DapFactorVO[] m_factorVos = null;

  private SubjviewVO[] m_subViewVos = null;

  private SubviewUtil viewUtil = null;

  private AggregatedValueObject m_dataVo = null;

  private CircularlyAccessibleValueObject m_headVo = null;

  private CircularlyAccessibleValueObject[] m_detailVos = null;

  private String m_modifyFlag = null;

  private UFBoolean m_isZero = new UFBoolean("N");

  private Vector m_detailvec = new Vector();

  private static Hashtable m_SubjectAssHas = new Hashtable();

  private static Hashtable m_SubjectStyleHas = new Hashtable();

  public static StringBuffer m_strReport = new StringBuffer();
  private static final int PZREMARKLEN = 100;
  TaskRedirectProxy m_trp = new TaskRedirectProxy();
  private String tmpkey = null;
  private String factorkey = null;
  private String factorHkey = null;
  private String subviewkey = null;
  private String subviewhkey = null;

  private DapFinIndexDMO finindexdmo = null;
  private RtVouchDMO rtvouchdmo = null;
  private RtvouchBDMO rtvouchbdmo = null;
  private DapDMO dapdmo = null;

  private DapDMO getDapDMO() {
    if (this.dapdmo == null) {
      try {
        this.dapdmo = new DapDMO();
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    return this.dapdmo;
  }

  private RtVouchDMO getRtVouchDMO() {
    if (this.rtvouchdmo == null) {
      try {
        this.rtvouchdmo = new RtVouchDMO();
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    return this.rtvouchdmo;
  }

  private RtvouchBDMO getRtvouchBDMO() {
    if (this.rtvouchbdmo == null) {
      try {
        this.rtvouchbdmo = new RtvouchBDMO();
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    return this.rtvouchbdmo;
  }

  private DapFinIndexDMO getFinindexDMO() {
    if (this.finindexdmo == null) {
      try {
        this.finindexdmo = new DapFinIndexDMO();
      } catch (Exception ex) {
        Logger.error(ex);
      }
    }
    return this.finindexdmo;
  }

  private void calBalance(DapFinMsgVO msgVo)
    throws DapBusinessException, RtVouchException
  {
    try
    {
      boolean isCredit = true;
      UFDouble creditMoney = IUFTypesInitializationValue.UZERO; UFDouble debitMoney = IUFTypesInitializationValue.UZERO; UFDouble remainder = IUFTypesInitializationValue.UZERO;
      if (this.m_detailvec.size() < 1) {
        return;
      }

      for (int i = 0; i < this.m_detailvec.size(); i++) {
        DetailVO tmpDetailVo = (DetailVO)this.m_detailvec.get(i);
        if (tmpDetailVo == null)
          continue;
        setGLVouchId(tmpDetailVo);

        setMoneySubInfo(tmpDetailVo);
        creditMoney = creditMoney.add(tmpDetailVo.getLocalcreditamount().doubleValue());
        debitMoney = debitMoney.add(tmpDetailVo.getLocaldebitamount().doubleValue());
      }

      remainder = debitMoney.sub(creditMoney);

      UFDouble tmpDbl = IUFTypesInitializationValue.UZERO;

      DetailVO lastDetailVo = (DetailVO)this.m_detailvec.get(this.m_detailvec.size() - 1);

      if (lastDetailVo.getOppositesubj().equals("Y")) {
        tmpDbl = lastDetailVo.getLocaldebitamount().sub(remainder);
        lastDetailVo.setLocaldebitamount(tmpDbl);
        lastDetailVo.setLocalcreditamount(IUFTypesInitializationValue.UZERO);
        if (msgVo.getDestSystem() == 1)
        {
          lastDetailVo.setDebitamount(tmpDbl);
          lastDetailVo.setCreditamount(IUFTypesInitializationValue.UZERO);
        }
      } else {
        tmpDbl = lastDetailVo.getLocalcreditamount().add(remainder);
        lastDetailVo.setLocalcreditamount(tmpDbl);
        lastDetailVo.setLocaldebitamount(IUFTypesInitializationValue.UZERO);
        if (msgVo.getDestSystem() == 1)
        {
          lastDetailVo.setDebitamount(IUFTypesInitializationValue.UZERO);
          lastDetailVo.setCreditamount(tmpDbl);
        }
      }

      if ((lastDetailVo.getLocalcreditamount().doubleValue() == 0.0D) && 
        (lastDetailVo.getLocaldebitamount().doubleValue() == 0.0D)) {
        this.m_detailvec.remove(this.m_detailvec.size() - 1);
      }
      if (this.m_detailvec.size() > 0) {
        this.m_rtVouchbVos = new DetailVO[this.m_detailvec.size()];
        this.m_detailvec.copyInto(this.m_rtVouchbVos);
      }
    } catch (Exception e) {
      if ((e instanceof NullPointerException))
        throw new RtVouchException(NCLangResOnserver.getInstance()
          .getStrByID("fidap", 
          "UPPfidap-000057"), -1);
      if ((e instanceof DapBusinessException))
        throw ((DapBusinessException)e);
      if ((e instanceof RtVouchException)) {
        throw ((RtVouchException)e);
      }
      throw new RtVouchException(NCLangResOnserver.getInstance()
        .getStrByID("fidap", 
        "UPPfidap-000058") + 
        e.getMessage(), -1);
    }
  }

  private FreevalueVO[] createDetailAssAndAddMainAss(String detailNo, int detailIndex, Hashtable mainAssHas)
    throws DapBusinessException, RtVouchException
  {
    FreevalueVO[] retVos = (FreevalueVO[])null;
    String strValue = null;
    Object tmpObj = null;
    Hashtable assHas = new Hashtable();
    if (mainAssHas != null)
      assHas = (Hashtable)mainAssHas.clone();
    try {
      if (this.m_tempDetailAssHas != null)
      {
        Object o = this.m_tempDetailAssHas.get(detailNo);
        if (o != null) {
          Vector v = (Vector)o;
          FreevalueVO tmpVo = null;
          for (int i = 0; i < v.size(); i++) {
            VouchtempassVO assVo = (VouchtempassVO)v.elementAt(i);

            strValue = assVo.getAssform();
            if ((strValue != null) && (!strValue.equals(""))) {
              tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
            }
            tmpVo = new FreevalueVO();

            tmpVo.setPk_freevalue(assVo.getAssname());

            tmpVo.setChecktype(assVo.getPk_ass());
            if (tmpObj != null) {
              if (assVo.getPk_ass().equals("00010000000000000032")) {
                tmpVo.setCheckvalue(getSpecialValue(tmpObj == null ? null : tmpObj.toString()));
              }
              else
                tmpVo.setCheckvalue(tmpObj == null ? null : tmpObj.toString());
            }
            else {
              tmpVo.setCheckvalue(null);
            }

            tmpVo.setIcount(new Integer(i));
            assHas.put(assVo.getPk_ass(), tmpVo);
          }
        }
      }
      if (assHas.size() == 0)
        return null;
      int j = 0;
      retVos = new FreevalueVO[assHas.size()];
      Enumeration e = assHas.elements();
      while (e.hasMoreElements())
        retVos[(j++)] = ((FreevalueVO)e.nextElement());
    }
    catch (Exception ex) {
      Logger.error("生成辅助核算的时候出错！", ex);
      if (ex.getMessage() != null) {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000059", null, 
          new String[] { ex.getMessage(), strValue });
        strValue = message;
      } else {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000060", null, 
          new String[] { strValue });
        strValue = message;
      }
      throw new RtVouchException(strValue, 1);
    }
    return retVos;
  }

  private DetailVO createDetailExt(DetailVO rtbv, FormulaElement[] m_formulas, int detailIndex)
    throws Exception
  {
    Object tmpObj = null;

    if (m_formulas == null) {
      throw new RtVouchException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000061"), 
        1);
    }

    String strDEF1 = m_formulas[0].getFormula();
    String strDEF2 = m_formulas[1].getFormula();
    String strDEF3 = m_formulas[2].getFormula();
    String strDEF4 = m_formulas[3].getFormula();
    String strDEF5 = m_formulas[4].getFormula();
    String strDEF6 = m_formulas[5].getFormula();
    String strDEF7 = m_formulas[6].getFormula();
    String strDEF8 = m_formulas[7].getFormula();
    String strDEF9 = m_formulas[8].getFormula();
    String strDEF10 = m_formulas[9].getFormula();
    String strDEF11 = m_formulas[10].getFormula();
    String strDEF12 = m_formulas[11].getFormula();
    String strDEF13 = m_formulas[12].getFormula();
    String strDEF14 = m_formulas[13].getFormula();
    String strDEF15 = m_formulas[14].getFormula();
    String strDEF16 = m_formulas[15].getFormula();
    String strDEF17 = m_formulas[16].getFormula();
    String strDEF18 = m_formulas[17].getFormula();
    String strDEF19 = m_formulas[18].getFormula();
    String strDEF20 = m_formulas[19].getFormula();
    String strDEF21 = m_formulas[20].getFormula();
    String strDEF22 = m_formulas[21].getFormula();
    String strDEF23 = m_formulas[22].getFormula();
    String strDEF24 = m_formulas[23].getFormula();
    String strDEF25 = m_formulas[24].getFormula();
    String strDEF26 = m_formulas[25].getFormula();
    String strDEF27 = m_formulas[26].getFormula();
    String strDEF28 = m_formulas[27].getFormula();
    String strDEF29 = m_formulas[28].getFormula();
    String strDEF30 = m_formulas[29].getFormula();
    String strFREEVALUE1 = m_formulas[30].getFormula();
    String strFREEVALUE2 = m_formulas[31].getFormula();
    String strFREEVALUE3 = m_formulas[32].getFormula();
    String strFREEVALUE4 = m_formulas[33].getFormula();
    String strFREEVALUE5 = m_formulas[34].getFormula();
    String strFREEVALUE6 = m_formulas[35].getFormula();
    String strFREEVALUE7 = m_formulas[36].getFormula();
    String strFREEVALUE8 = m_formulas[37].getFormula();
    String strFREEVALUE9 = m_formulas[38].getFormula();
    String strFREEVALUE10 = m_formulas[39].getFormula();
    String strFREEVALUE11 = m_formulas[40].getFormula();
    String strFREEVALUE12 = m_formulas[41].getFormula();
    String strFREEVALUE13 = m_formulas[42].getFormula();
    String strFREEVALUE14 = m_formulas[43].getFormula();
    String strFREEVALUE15 = m_formulas[44].getFormula();
    String strFREEVALUE16 = m_formulas[45].getFormula();
    String strFREEVALUE17 = m_formulas[46].getFormula();
    String strFREEVALUE18 = m_formulas[47].getFormula();
    String strFREEVALUE19 = m_formulas[48].getFormula();
    String strFREEVALUE20 = m_formulas[49].getFormula();
    String strFREEVALUE21 = m_formulas[50].getFormula();
    String strFREEVALUE22 = m_formulas[51].getFormula();
    String strFREEVALUE23 = m_formulas[52].getFormula();
    String strFREEVALUE24 = m_formulas[53].getFormula();
    String strFREEVALUE25 = m_formulas[54].getFormula();
    String strFREEVALUE26 = m_formulas[55].getFormula();
    String strFREEVALUE27 = m_formulas[56].getFormula();
    String strFREEVALUE28 = m_formulas[57].getFormula();
    String strFREEVALUE29 = m_formulas[58].getFormula();
    String strFREEVALUE30 = m_formulas[59].getFormula();

    if ((strDEF1 != null) && (!strDEF1.trim().equals(""))) {
      tmpObj = getParseValue(strDEF1, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree1(tmpObj.toString());
      }
    }
    if ((strDEF2 != null) && (!strDEF2.trim().equals(""))) {
      tmpObj = getParseValue(strDEF2, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree2(tmpObj.toString());
      }
    }
    if ((strDEF3 != null) && (!strDEF3.trim().equals(""))) {
      tmpObj = getParseValue(strDEF3, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree3(tmpObj.toString());
      }
    }

    if ((strDEF4 != null) && (!strDEF4.trim().equals(""))) {
      tmpObj = getParseValue(strDEF4, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree4(tmpObj.toString());
      }
    }

    if ((strDEF5 != null) && (!strDEF5.trim().equals(""))) {
      tmpObj = getParseValue(strDEF5, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree5(tmpObj.toString());
      }
    }

    if ((strDEF6 != null) && (!strDEF6.trim().equals(""))) {
      tmpObj = getParseValue(strDEF6, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree6(tmpObj.toString());
      }
    }

    if ((strDEF7 != null) && (!strDEF7.trim().equals(""))) {
      tmpObj = getParseValue(strDEF7, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree7(tmpObj.toString());
      }
    }

    if ((strDEF8 != null) && (!strDEF8.trim().equals(""))) {
      tmpObj = getParseValue(strDEF8, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree8(tmpObj.toString());
      }
    }

    if ((strDEF9 != null) && (!strDEF9.trim().equals(""))) {
      tmpObj = getParseValue(strDEF9, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree9(tmpObj.toString());
      }
    }
    if ((strDEF10 != null) && (!strDEF10.trim().equals(""))) {
      tmpObj = getParseValue(strDEF10, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree10(tmpObj.toString());
      }
    }

    if ((strDEF11 != null) && (!strDEF11.trim().equals(""))) {
      tmpObj = getParseValue(strDEF11, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree11(tmpObj.toString());
      }
    }

    if ((strDEF12 != null) && (!strDEF12.trim().equals(""))) {
      tmpObj = getParseValue(strDEF12, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree12(tmpObj.toString());
      }
    }

    if ((strDEF13 != null) && (!strDEF13.trim().equals(""))) {
      tmpObj = getParseValue(strDEF13, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree13(tmpObj.toString());
      }
    }

    if ((strDEF14 != null) && (!strDEF14.trim().equals(""))) {
      tmpObj = getParseValue(strDEF14, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree14(tmpObj.toString());
      }
    }

    if ((strDEF15 != null) && (!strDEF15.trim().equals(""))) {
      tmpObj = getParseValue(strDEF15, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree15(tmpObj.toString());
      }
    }

    if ((strDEF16 != null) && (!strDEF16.trim().equals(""))) {
      tmpObj = getParseValue(strDEF16, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree16(tmpObj.toString());
      }
    }

    if ((strDEF17 != null) && (!strDEF17.trim().equals(""))) {
      tmpObj = getParseValue(strDEF17, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree17(tmpObj.toString());
      }
    }

    if ((strDEF18 != null) && (!strDEF18.trim().equals(""))) {
      tmpObj = getParseValue(strDEF18, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree18(tmpObj.toString());
      }
    }

    if ((strDEF19 != null) && (!strDEF19.trim().equals(""))) {
      tmpObj = getParseValue(strDEF19, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree19(tmpObj.toString());
      }
    }
    if ((strDEF20 != null) && (!strDEF20.trim().equals(""))) {
      tmpObj = getParseValue(strDEF20, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree20(tmpObj.toString());
      }
    }
    if ((strDEF21 != null) && (!strDEF21.trim().equals(""))) {
      tmpObj = getParseValue(strDEF21, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree21(tmpObj.toString());
      }
    }

    if ((strDEF22 != null) && (!strDEF22.trim().equals(""))) {
      tmpObj = getParseValue(strDEF22, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree22(tmpObj.toString());
      }
    }

    if ((strDEF23 != null) && (!strDEF23.trim().equals(""))) {
      tmpObj = getParseValue(strDEF23, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree23(tmpObj.toString());
      }
    }

    if ((strDEF24 != null) && (!strDEF24.trim().equals(""))) {
      tmpObj = getParseValue(strDEF24, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree24(tmpObj.toString());
      }
    }

    if ((strDEF25 != null) && (!strDEF25.trim().equals(""))) {
      tmpObj = getParseValue(strDEF25, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree25(tmpObj.toString());
      }
    }

    if ((strDEF26 != null) && (!strDEF26.trim().equals(""))) {
      tmpObj = getParseValue(strDEF26, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree26(tmpObj.toString());
      }
    }

    if ((strDEF27 != null) && (!strDEF27.trim().equals(""))) {
      tmpObj = getParseValue(strDEF27, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree27(tmpObj.toString());
      }
    }

    if ((strDEF28 != null) && (!strDEF28.trim().equals(""))) {
      tmpObj = getParseValue(strDEF28, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree28(tmpObj.toString());
      }
    }

    if ((strDEF29 != null) && (!strDEF29.trim().equals(""))) {
      tmpObj = getParseValue(strDEF29, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree29(tmpObj.toString());
      }
    }
    if ((strDEF30 != null) && (!strDEF30.trim().equals(""))) {
      tmpObj = getParseValue(strDEF30, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree30(tmpObj.toString());
      }
    }

    if ((strFREEVALUE1 != null) && (!strFREEVALUE1.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE1, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue1(tmpObj.toString());
      }
    }

    if ((strFREEVALUE2 != null) && (!strFREEVALUE2.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE2, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue2(tmpObj.toString());
      }
    }

    if ((strFREEVALUE3 != null) && (!strFREEVALUE3.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE3, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue3(tmpObj.toString());
      }
    }

    if ((strFREEVALUE4 != null) && (!strFREEVALUE4.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE4, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue4(tmpObj.toString());
      }
    }

    if ((strFREEVALUE5 != null) && (!strFREEVALUE5.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE5, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue5(tmpObj.toString());
      }
    }

    if ((strFREEVALUE6 != null) && (!strFREEVALUE6.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE6, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue6(tmpObj.toString());
      }
    }

    if ((strFREEVALUE7 != null) && (!strFREEVALUE7.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE7, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue7(tmpObj.toString());
      }
    }

    if ((strFREEVALUE8 != null) && (!strFREEVALUE8.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE8, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue8(tmpObj.toString());
      }
    }

    if ((strFREEVALUE9 != null) && (!strFREEVALUE9.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE9, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue9(tmpObj.toString());
      }
    }
    if ((strFREEVALUE10 != null) && (!strFREEVALUE10.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE10, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue10(tmpObj.toString());
      }
    }

    if ((strFREEVALUE11 != null) && (!strFREEVALUE11.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE11, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue11(tmpObj.toString());
      }
    }

    if ((strFREEVALUE12 != null) && (!strFREEVALUE12.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE12, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue12(tmpObj.toString());
      }
    }

    if ((strFREEVALUE13 != null) && (!strFREEVALUE13.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE13, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue13(tmpObj.toString());
      }
    }

    if ((strFREEVALUE14 != null) && (!strFREEVALUE14.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE14, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue14(tmpObj.toString());
      }
    }

    if ((strFREEVALUE15 != null) && (!strFREEVALUE15.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE15, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue15(tmpObj.toString());
      }
    }

    if ((strFREEVALUE16 != null) && (!strFREEVALUE16.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE16, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue16(tmpObj.toString());
      }
    }

    if ((strFREEVALUE17 != null) && (!strFREEVALUE17.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE17, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue17(tmpObj.toString());
      }
    }

    if ((strFREEVALUE18 != null) && (!strFREEVALUE18.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE18, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue18(tmpObj.toString());
      }
    }

    if ((strFREEVALUE19 != null) && (!strFREEVALUE19.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE19, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue19(tmpObj.toString());
      }
    }
    if ((strFREEVALUE20 != null) && (!strFREEVALUE20.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE20, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue20(tmpObj.toString());
      }
    }
    if ((strFREEVALUE21 != null) && (!strFREEVALUE21.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE21, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue21(tmpObj.toString());
      }
    }

    if ((strFREEVALUE22 != null) && (!strFREEVALUE22.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE22, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue22(tmpObj.toString());
      }
    }

    if ((strFREEVALUE23 != null) && (!strFREEVALUE23.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE23, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue23(tmpObj.toString());
      }
    }

    if ((strFREEVALUE24 != null) && (!strFREEVALUE24.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE24, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue24(tmpObj.toString());
      }
    }

    if ((strFREEVALUE25 != null) && (!strFREEVALUE25.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE25, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue25(tmpObj.toString());
      }
    }

    if ((strFREEVALUE26 != null) && (!strFREEVALUE26.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE26, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue26(tmpObj.toString());
      }
    }

    if ((strFREEVALUE27 != null) && (!strFREEVALUE27.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE27, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue27(tmpObj.toString());
      }
    }

    if ((strFREEVALUE28 != null) && (!strFREEVALUE28.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE28, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue28(tmpObj.toString());
      }
    }

    if ((strFREEVALUE29 != null) && (!strFREEVALUE29.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE29, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue29(tmpObj.toString());
      }
    }
    if ((strFREEVALUE30 != null) && (!strFREEVALUE30.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE30, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue30(tmpObj.toString());
      }
    }

    return rtbv;
  }

  public VoucherVO createRtVouch(DapFinMsgVO msgVo, AggregatedValueObject dataVo, boolean isExistMsg, boolean isExistReflect)
    throws DapBusinessException, RtVouchException
  {
    VoucherVO retVo = null;
    String m_pkfinindex = null;

    this.m_dataVo = dataVo;
    this.m_headVo = dataVo.getParentVO();
    this.m_detailVos = dataVo.getChildrenVO();
    try
    {
      this.m_modifyFlag = this.m_trp.queryBillContrl(DapFinMsgVO.changeMsgVO(msgVo));
      if (isExistReflect) {
        m_pkfinindex = msgVo.getPkFinmsg();

        int res = getFinindexDMO().updateFlagRtVoucher(m_pkfinindex, 1);
        if (res == 0)
          throw new RtVouchException(NCLangResOnserver.getInstance()
            .getStrByID("fidap", 
            "UPPfidap-000062"), 1);
      }
      else {
        String im = (String)CacheManager.getInstance(Thread.currentThread()).getValue(CacheManager.EXCUTTYPE, 
          CacheManager.EXCUTTYPE);
        if ((im != null) && (!im.equals("Y"))) {
          msgVo.setFlag(CacheManager.RTVINT);
          m_pkfinindex = getFinindexDMO().insert(msgVo);
          msgVo.setPkFinmsg(m_pkfinindex);
        }

      }

      getVouchTemp(msgVo);

      createRtVouchHead(msgVo);

      getSystemFactorAndView();

      createRtVouchDetails(msgVo);

      reduceZeroRtVouch();
      if (this.m_isZero.booleanValue())
      {
        if (this.m_vouchTempHeadVo.getSumzeroctl().equals("Y")) {
          throw new RtVouchException(NCLangResOnserver.getInstance()
            .getStrByID("fidap", 
            "UPPfidap-000063"), 1);
        }

        String im = (String)CacheManager.getInstance(Thread.currentThread()).getValue(
          CacheManager.EXCUTTYPE, CacheManager.EXCUTTYPE);
        if ((im != null) && (!im.equals("Y"))) {
          delReflect(m_pkfinindex);
        }
        else if (isExistReflect) {
          getFinindexDMO().updateFlagRtVoucher(m_pkfinindex, 0);
        }

        return null;
      }

      calBalance(msgVo);

      if ((isExistMsg) && (msgVo != null) && (!msgVo.IsLogCreate())) {
        DapMsgVO tmpVo = new DapMsgVO();
        tmpVo.setCorp(msgVo.getCorp());
        tmpVo.setSys(msgVo.getSys());
        tmpVo.setProc(msgVo.getProc());
        tmpVo.setBusiType(msgVo.getBusiType());
        tmpVo.setProcMsg(msgVo.getProcMsg());
        tmpVo.setDestSystem(msgVo.getDestSystem());
        tmpVo.setPkAccOrg(msgVo.getPkAccOrg());
        tmpVo.setPkAccount(msgVo.getPkAccount());
        deleteMessage(tmpVo);
      }

      if (isExistReflect) {
        getFinindexDMO().updateFlagRtVoucher(m_pkfinindex, 2);
      }
      retVo = this.m_rtVouchVo;
      retVo.setDetails(this.m_rtVouchbVos);
      retVo.setDeleteclass(m_pkfinindex);
    } catch (Exception ex) {
      debug("生成实时凭证出错！但不应该影响正常的业务！", msgVo, ex);
      if ((ex instanceof RtVouchException)) {
        throw ((RtVouchException)ex);
      }
      throw new RtVouchException(ex.getMessage(), 1);
    }

    return retVo;
  }

  private Hashtable createRtVouchAss(int detailIndex)
    throws DapBusinessException, RtVouchException
  {
    String strValue = null;
    Object tmpObj = null;
    if (this.m_vouchTempAssVos == null) {
      return null;
    }

    Hashtable tmpHas = new Hashtable();
    FreevalueVO tmpVo = null;
    try {
      for (int i = 0; i < this.m_vouchTempAssVos.length; i++)
      {
        strValue = this.m_vouchTempAssVos[i].getAssform();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
        }

        tmpVo = new FreevalueVO();

        tmpVo.setPk_freevalue(this.m_vouchTempAssVos[i].getAssname());

        tmpVo.setChecktype(this.m_vouchTempAssVos[i].getPk_ass());

        if (this.m_vouchTempAssVos[i].getPk_ass().equals("00010000000000000032")) {
          tmpVo.setCheckvalue(getSpecialValue(tmpObj == null ? null : tmpObj.toString()));
        }
        else {
          tmpVo.setCheckvalue(tmpObj == null ? null : tmpObj.toString());
        }

        tmpVo.setIcount(new Integer(i));
        tmpHas.put(tmpVo.getChecktype(), tmpVo);
      }
    } catch (Exception ex) {
      Logger.error("生成凭证辅助核算出错！", ex);
      if (ex.getMessage() != null) {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000064", null, 
          new String[] { ex.getMessage(), strValue });
        strValue = message;
      } else {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000060", null, 
          new String[] { strValue });
        strValue = message;
      }
      throw new RtVouchException(strValue, 1);
    }
    return tmpHas;
  }

  private DetailVO[] createRtVouchDetail(CircularlyAccessibleValueObject detailVo, DapFinMsgVO msgVo, int detailIndex, VouchParaVO paraVo, Hashtable mainAssHas)
    throws DapBusinessException, RtVouchException
  {
    DetailVO[] retVos = (DetailVO[])null;
    String subjectNo = null; String strValue = null;
    Object tmpObj = null;
    int indexTemp = 0;
    try {
      retVos = new DetailVO[this.m_vouchTempDetailVos.length];
      DetailVO tmpVo = null;
      for (int i = 0; i < this.m_vouchTempDetailVos.length; i++) {
        indexTemp++;

        tmpVo = new DetailVO();
        tmpVo.setPk_glorg(msgVo.getPkAccOrg());
        tmpVo.setPk_glbook(msgVo.getPkAccount());
        subjectNo = createRtVouchDetailClass(msgVo, this.m_vouchTempDetailVos[i], detailVo);

        tmpVo.setPk_corp(this.m_vouchTempHeadVo.getPk_corp());

        tmpVo.setPk_accsubj(subjectNo);

        strValue = this.m_vouchTempDetailVos[i].getBriefform();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
          if ((tmpObj == null) || (tmpObj.toString().trim().length() == 0))
            tmpObj = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000065");
        }
        else {
          tmpObj = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000065");
        }
        String strRemark = String.valueOf(tmpObj);
        strRemark = PfComm.sub_txt(strRemark, 100);
        tmpVo.setExplanation(strRemark);

        tmpVo = createDetailExt(tmpVo, this.m_vouchTempDetailVos[i].getFormulas(), detailIndex);

        String pkCurrentType = null;
        strValue = this.m_vouchTempDetailVos[i].getCurrtype();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
          if (tmpObj != null) {
            pkCurrentType = tmpObj.toString();
          }
        }
        if (pkCurrentType == null)
        {
          pkCurrentType = paraVo.getCurrency();
        }
        tmpVo.setPk_currtype(pkCurrentType);
        if ((tmpVo.getPk_currtype() == null) || (tmpVo.getPk_currtype().trim().length() == 0)) {
          throw new RtVouchException(NCLangResOnserver.getInstance()
            .getStrByID("fidap", 
            "UPPfidap-000066"), 1);
        }

        strValue = this.m_vouchTempDetailVos[i].getDiscassirate();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
          tmpVo.setExcrate1(tmpObj == null ? IUFTypesInitializationValue.UZERO : 
            new UFDouble(tmpObj.toString()));
        }

        strValue = this.m_vouchTempDetailVos[i].getDiscbaserate();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
          tmpVo.setExcrate2(tmpObj == null ? IUFTypesInitializationValue.UZERO : 
            new UFDouble(tmpObj.toString()));
        }

        tmpVo.setPrice(IUFTypesInitializationValue.UZERO);

        if ((this.m_vouchTempDetailVos[i].m_modifyflag == null) || 
          (this.m_vouchTempDetailVos[i].m_modifyflag.trim().equals("")))
          tmpVo.setModifyflag(this.m_modifyFlag);
        else {
          tmpVo.setModifyflag(this.m_vouchTempDetailVos[i].m_modifyflag);
        }

        if (this.m_vouchTempDetailVos[i].getDirection().booleanValue()) {
          tmpVo.setOppositesubj("Y");

          strValue = this.m_vouchTempDetailVos[i].getQuanform();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
            Logger.error(tmpObj == null ? "<NULL>" : tmpObj.toString());
            tmpVo.setDebitquantity(
              tmpObj == null ? IUFTypesInitializationValue.UZERO : tmpObj == null ? IUFTypesInitializationValue.UZERO : 
              new UFDouble(tmpObj.toString()));
          }

          strValue = this.m_vouchTempDetailVos[i].getAssicurrform();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
            Logger.error(tmpObj == null ? "<NULL>" : tmpObj.toString());
            tmpVo.setFracdebitamount(tmpObj == null ? IUFTypesInitializationValue.UZERO : 
              new UFDouble(tmpObj.toString()));
          }

          strValue = this.m_vouchTempDetailVos[i].getBasecurrform();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
            Logger.error(tmpObj == null ? "<NULL>" : tmpObj.toString());
            tmpVo.setLocaldebitamount(tmpObj == null ? IUFTypesInitializationValue.UZERO : 
              new UFDouble(tmpObj.toString()));
          }

          strValue = this.m_vouchTempDetailVos[i].getOrigcurrform();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
            Logger.error(tmpObj == null ? "<NULL>" : tmpObj.toString());
            tmpVo.setDebitamount(tmpObj == null ? IUFTypesInitializationValue.UZERO : 
              new UFDouble(tmpObj.toString()));
          }
        }
        else {
          tmpVo.setOppositesubj("N");

          strValue = this.m_vouchTempDetailVos[i].getQuanform();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
            Logger.error(tmpObj == null ? "<NULL>" : tmpObj.toString());
            tmpVo.setCreditquantity(tmpObj == null ? IUFTypesInitializationValue.UZERO : 
              new UFDouble(tmpObj.toString()));
          }

          strValue = this.m_vouchTempDetailVos[i].getAssicurrform();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
            Logger.error(tmpObj == null ? "<NULL>" : tmpObj.toString());
            tmpVo.setFraccreditamount(tmpObj == null ? IUFTypesInitializationValue.UZERO : 
              new UFDouble(tmpObj.toString()));
          }

          strValue = this.m_vouchTempDetailVos[i].getBasecurrform();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
            Logger.error(tmpObj == null ? "<NULL>" : tmpObj.toString());
            tmpVo.setLocalcreditamount(tmpObj == null ? IUFTypesInitializationValue.UZERO : 
              new UFDouble(tmpObj.toString()));
          }

          strValue = this.m_vouchTempDetailVos[i].getOrigcurrform();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 1);
            Logger.error(tmpObj == null ? "<NULL>" : tmpObj.toString());
            tmpVo.setCreditamount(tmpObj == null ? IUFTypesInitializationValue.UZERO : 
              new UFDouble(tmpObj.toString()));
          }
        }

        strValue = this.m_vouchTempDetailVos[i].getAssiNo();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
          tmpVo.setFree1(tmpObj == null ? null : String.valueOf(tmpObj));
        }

        strValue = this.m_vouchTempDetailVos[i].getFormula(2);
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
          tmpVo.setFree2(tmpObj == null ? null : String.valueOf(tmpObj));
        }

        strValue = this.m_vouchTempDetailVos[i].getTradeNo();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
          tmpVo.setTradeNo(tmpObj == null ? null : String.valueOf(tmpObj));
        }

        strValue = this.m_vouchTempDetailVos[i].getTradeDate();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
          tmpVo.setTradeDate(tmpObj == null ? null : String.valueOf(tmpObj));
        }

        strValue = this.m_vouchTempDetailVos[i].getPk_InnerCorp();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
          tmpVo.setPk_innercorp(tmpObj == null ? null : String.valueOf(tmpObj));
        }
        AccsubjVO accsubj = getAccSubjVO(tmpVo.getPk_accsubj());

        boolean isBankKind = (accsubj != null) && (accsubj.getCashbankflag() != null);

        if (((detailVo instanceof IAccountGetBalanceInfo)) && (isBankKind)) {
          IAccountGetBalanceInfo balanceInfo = (IAccountGetBalanceInfo)detailVo;

          tmpVo.setCheckstyle(balanceInfo.getJsfs());

          tmpVo.setCheckdate(balanceInfo.getCheckDate());

          tmpVo.setCheckno(balanceInfo.getCheckNo());
          if ((tmpVo.getCheckno() != null) && (tmpVo.getCheckno().trim().length() == 0)) {
            tmpVo.setCheckno(null);
          }

          tmpVo.setFree3(balanceInfo.getNotetype());
          tmpVo.setBankNo(balanceInfo.getBankCode());
        }
        else {
          tmpVo.setCheckstyle(null);

          tmpVo.setCheckdate(null);

          tmpVo.setCheckno(null);

          tmpVo.setFree3(null);
        }

        if (msgVo.getDestSystem() == 1)
        {
          strValue = this.m_vouchTempDetailVos[i].getPk_Account();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
            tmpVo.setPk_Account(tmpObj == null ? null : String.valueOf(tmpObj));
          }

          strValue = this.m_vouchTempDetailVos[i].getPk_Contract();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
            tmpVo.setPk_Contract(tmpObj == null ? null : String.valueOf(tmpObj));
          }

          strValue = this.m_vouchTempDetailVos[i].getStartRestDate();
          if ((strValue != null) && (!strValue.equals(""))) {
            tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 2);
            tmpVo.setStartRestDate(tmpObj == null ? null : new UFDate(String.valueOf(tmpObj)));
          }

        }
        else if ((msgVo.getSys().equals("EC")) && (isCashFlow(tmpVo.getPk_accsubj()))) {
          CashflowcaseVO vo = new CashflowcaseVO();
          vo.setFlag(new Integer(1));
          vo.setPk_corp(msgVo.getCorp());
          vo.setPk_glorgbook(getGlorgbook(msgVo.getPkAccOrg(), msgVo.getPkAccount()).getPrimaryKey());
          String strPK = (String)detailVo.getAttributeValue("cashitem");
          if ((strPK != null) && (strPK.trim().length() > 0)) {
            vo.setPk_cashflow(strPK);
            Integer str = (Integer)detailVo.getAttributeValue("fx");
            if (str.intValue() == 1)
            {
              vo.setMoney((UFDouble)detailVo.getAttributeValue("jfbbje"));
              vo.setMoneyass((UFDouble)detailVo.getAttributeValue("jffbje"));
              vo.setMoneymain((UFDouble)detailVo.getAttributeValue("jfybje"));
            }
            else {
              vo.setMoney((UFDouble)detailVo.getAttributeValue("dfbbje"));
              vo.setMoneyass((UFDouble)detailVo.getAttributeValue("dffbje"));
              vo.setMoneymain((UFDouble)detailVo.getAttributeValue("dfybje"));
            }
            Vector vec = new Vector();
            UserDataVO udvo = new UserDataVO();
            udvo.setDatatype("现金流量");
            udvo.setUserdata(new CashflowcaseVO[] { vo });
            vec.addElement(udvo);
            tmpVo.setOtheruserdata(vec);
          }
        }
        else if (((detailVo instanceof IFetchCashFlow)) && (isCashFlow(tmpVo.getPk_accsubj()))) {
          IFetchCashFlow fetch = (IFetchCashFlow)detailVo;

          CashflowcaseVO vo = new CashflowcaseVO();
          vo.setFlag(fetch.getFLag());
          vo.setPk_corp(msgVo.getCorp());
          vo.setPk_glorgbook(getGlorgbook(msgVo.getPkAccOrg(), msgVo.getPkAccount()).getPrimaryKey());
          String cashItem = fetch.getCashItem();
          if ((cashItem != null) && (cashItem.length() > 0)) {
            vo.setPk_cashflow(cashItem);
            vo.setMoney(fetch.getMoney());
            vo.setMoneymain(fetch.getMoneyRaw());
            vo.setMoneyass(fetch.getMoneyAss());
            Vector vec = new Vector();
            UserDataVO udvo = new UserDataVO();
            udvo.setDatatype("现金流量");
            udvo.setUserdata(new CashflowcaseVO[] { vo });
            vec.addElement(udvo);
            tmpVo.setOtheruserdata(vec);
          }

        }

        strValue = this.m_vouchTempDetailVos[i].getCreditSign();
        if ((strValue != null) && (!strValue.equals(""))) {
          tmpObj = getParseValue(strValue, this.m_dataVo, detailIndex, 0);
          tmpVo.setM_creditsign(tmpObj == null ? null : String.valueOf(tmpObj));
        }

        FreevalueVO[] assVos = createDetailAssAndAddMainAss(
          String.valueOf(this.m_vouchTempDetailVos[i].getEntryno()), detailIndex, mainAssHas);

        tmpVo.setAss(assVos);

        retVos[i] = tmpVo;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      String error = "生成凭证模板第" + String.valueOf(indexTemp) + "条分录，单据的第" + String.valueOf(detailIndex + 1) + 
        "条表体数据时出错！";
      debug(error, msgVo, ex);
      if (ex.getMessage() != null) {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000059", null, 
          new String[] { ex.getMessage(), strValue });
        strValue = message;
      } else {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000060", null, 
          new String[] { strValue });
        strValue = message;
      }
      if ((ex instanceof RtVouchException)) {
        error = error + ((RtVouchException)ex).getMessage();
        throw new RtVouchException(error, 1);
      }
      throw new RtVouchException(strValue, 1);
    }

    return retVos;
  }

  private String createRtVouchDetailClass(DapFinMsgVO msgVo, VouchtempBVO vouchTempBVo, CircularlyAccessibleValueObject detailVo)
    throws DapBusinessException, RtVouchException
  {
    DapFactorVO AccsubjFactor = null;
    DapFactorVO tmpFactorVo = null;
    DapFactorVO[] factorVos = (DapFactorVO[])null;
    Vector vecFactor = new Vector();
    Vector vecView = new Vector();
    String retSubject = null;

    String headFlag = null;
    Object tmpObj = null;
    String attrName = null;
    if ((vouchTempBVo.getPk_insubjclass() == null) || (vouchTempBVo.getPk_insubjclass().trim().length() == 0)) {
      String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000878", null, 
        new String[] { attrName });
      throw new RtVouchException(message, 1);
    }
    try
    {
      if (this.m_factorVos != null) {
        String key = null;
        Hashtable h = (Hashtable)CacheManager.getInstance(Thread.currentThread()).getValue(
          CacheManager.FACTORH, this.factorHkey);
        HashMap tbexist = new HashMap();
        Vector fv = (Vector)(Vector)h.get(vouchTempBVo.getPk_insubjclass());
        if (fv != null)
          for (int i = 0; i < fv.size(); i++) {
            tmpFactorVo = (DapFactorVO)fv.elementAt(i);
            if (tmpFactorVo.getPkSubjClass().equals(vouchTempBVo.getPk_insubjclass())) {
              if (tmpFactorVo.getHeadFlag() != null) {
                headFlag = tmpFactorVo.getHeadFlag().trim();
              }

              attrName = tmpFactorVo.getAttrName();
              if (attrName != null) {
                attrName = attrName.trim();
                if (headFlag.equals("Y"))
                  tmpObj = this.m_headVo.getAttributeValue(attrName);
                else {
                  tmpObj = detailVo.getAttributeValue(attrName);
                }
                if (tmpObj != null)
                  tmpFactorVo.setFactorValue(tmpObj.toString());
                else {
                  tmpFactorVo.setFactorValue(null);
                }

                if (tmpFactorVo.getBdInfoPk().equals("00010000000000000034")) {
                  AccsubjFactor = AccsubjFactor == null ? tmpFactorVo : AccsubjFactor;
                }
              }

              key = tmpFactorVo.getAttrName() + tmpFactorVo.getFactorValue();
              if (tbexist.get(key) == null) {
                tbexist.put(key, tmpFactorVo);
                vecFactor.addElement(tmpFactorVo);
              }
            }
          }
      }
    }
    catch (Exception ex) {
      Logger.error("取得科目影响因素的时候出错！", ex);
      if ((ex instanceof RtVouchException)) {
        throw ((RtVouchException)ex);
      }
      String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000068", null, 
        new String[] { attrName });
      throw new RtVouchException(message, 1);
    }

    factorVos = new DapFactorVO[vecFactor.size()];
    if (vecFactor.size() > 0) {
      vecFactor.copyInto(factorVos);
    }

    SubjviewVO[] subviewVos = (SubjviewVO[])null;
    if (this.m_subViewVos != null) {
      Hashtable h = (Hashtable)CacheManager.getInstance(Thread.currentThread()).getValue(CacheManager.SUBVIEWH, 
        this.subviewhkey);
      if (h != null) {
        vecView = (Vector)(Vector)h.get(vouchTempBVo.getPk_insubjclass());
      }
    }
    if ((vecView != null) && (vecView.size() > 0)) {
      subviewVos = new SubjviewVO[vecView.size()];
      vecView.copyInto(subviewVos);
    }

    if (subviewVos != null) {
      retSubject = getSubviewUtil().getMatchViewPk(factorVos, subviewVos);
    }
    if (retSubject == null) {
      retSubject = factorVos[0].getDefaultSubject();
    }
    if ((retSubject == null) || (retSubject.trim().equals(""))) {
      if (AccsubjFactor != null)
      {
        tmpFactorVo = AccsubjFactor;

        if (msgVo.getDestSystem() == 0) {
          boolean valueisNull = (tmpFactorVo.getFactorValue() == null) || 
            (tmpFactorVo.getFactorValue().trim().length() == 0);
          boolean isDefaultGLORGBOOK = false;
          try {
            isDefaultGLORGBOOK = isCorpBaseBook(msgVo.getCorp(), msgVo.getPkAccOrg(), msgVo.getPkAccount());
          } catch (Exception ex) {
            return null;
          }
          if (isDefaultGLORGBOOK)
          {
            if (valueisNull) {
              return tmpFactorVo.getDefaultSubject();
            }
            return tmpFactorVo.getFactorValue();
          }

          if (!valueisNull) {
            AccsubjVO accsubVO = getBookAccsubjVO(msgVo.getPkAccOrg(), msgVo.getPkAccount(), 
              tmpFactorVo.getFactorValue());
            if (accsubVO != null) {
              return accsubVO.getPk_accsubj();
            }
            return tmpFactorVo.getDefaultSubject();
          }

          return tmpFactorVo.getDefaultSubject();
        }

        return tmpFactorVo.getFactorValue();
      }

      throw new RtVouchException(NCLangResOnserver.getInstance()
        .getStrByID("fidap", 
        "UPPfidap-000069"), 1);
    }

    return retSubject;
  }

  private SubviewUtil getSubviewUtil() {
    if (this.viewUtil == null) {
      this.viewUtil = new SubviewUtil();
    }
    return this.viewUtil;
  }

  private void createRtVouchDetails(DapFinMsgVO msgVo)
    throws DapBusinessException, RtVouchException
  {
    this.m_rtVouchbVos = new DetailVO[this.m_detailVos.length * this.m_vouchTempDetailVos.length];
    DetailVO[] RtVouchbVos = (DetailVO[])null;
    try {
      FreevalueVO[] inAssVos = (FreevalueVO[])null;
      if ((this.m_dataVo instanceof IAccountGetAssInfos)) {
        inAssVos = ((IAccountGetAssInfos)this.m_dataVo).getAssVos();
      }

      String pkCurrentType = this.m_trp.queryCurrType(msgVo.getCorp());
      VouchParaVO vo = new VouchParaVO();
      vo.setCurrency(pkCurrentType);
      Hashtable mainAssHas = null;

      for (int i = 0; i < this.m_detailVos.length; i++)
      {
        if (!canCreateRtVouchDetail(this.m_headVo, this.m_detailVos[i], msgVo))
          continue;
        mainAssHas = createRtVouchAss(i);

        RtVouchbVos = createRtVouchDetail(this.m_detailVos[i], msgVo, i, vo, mainAssHas);

        for (int j = 0; j < RtVouchbVos.length; j++)
        {
          dealWithInAss(inAssVos, RtVouchbVos[j]);

          getAssBySubject(RtVouchbVos[j]);

          this.m_rtVouchbVos[(i + this.m_detailVos.length * j)] = RtVouchbVos[j];
        }
      }
    }
    catch (Exception ex) {
      Logger.error("生成多条分录的时候出错！", ex);
      if ((ex instanceof RtVouchException)) {
        throw ((RtVouchException)ex);
      }
      throw new RtVouchException(ex.getMessage(), 1);
    }
  }

  private boolean canCreateRtVouchDetail(CircularlyAccessibleValueObject headvo, CircularlyAccessibleValueObject detailvo, DapFinMsgVO msg)
  {
    if (!BsPubUtil.isEnableGLBook()) {
      return true;
    }
    if (!this.m_vouchTempHeadVo.getIsDetailEntry().booleanValue()) {
      return true;
    }
    String pk_glorg = msg.getPkAccOrg();
    String pk_glbook = msg.getPkAccount();

    boolean b1 = false;
    boolean b2 = false;
    try {
      String glorgkey = "pk_glorgsfordetail" + headvo.getPrimaryKey() + detailvo.getPrimaryKey();
      String glbookkey = "pk_glbooksfordetail" + headvo.getPrimaryKey() + detailvo.getPrimaryKey();

      List glorgs = this.m_trp.getGlorgOrGlbookForDetail("pk_glorgsfordetail", glorgkey, 
        DapFinMsgVO.changeMsgVO(msg), this.m_dataVo);
      List glbooks = this.m_trp.getGlorgOrGlbookForDetail("pk_glbooksfordetail", glbookkey, 
        DapFinMsgVO.changeMsgVO(msg), this.m_dataVo);

      if (glorgs != null) {
        if (glorgs.contains(pk_glorg))
          b1 = true;
        else
          b1 = false;
      }
      else {
        b1 = true;
      }
      if (glbooks != null) {
        if (glbooks.contains(pk_glbook))
          b2 = true;
        else
          b2 = false;
      }
      else
        b2 = true;
    }
    catch (BusinessException e) {
      Logger.error("确定能否表体生成凭证的时候出错！", e);
    }

    return (b1) && (b2);
  }

  private void createRtVouchHead(DapFinMsgVO msgVo)
    throws DapBusinessException, RtVouchException
  {
    String strValue = null;
    Object tmpObj = null;
    try {
      this.m_rtVouchVo = new VoucherVO();

      this.m_rtVouchVo.setPk_billtype(msgVo.getProc());

      this.m_rtVouchVo.setDestSystem(msgVo.getDestSystem());

      this.m_rtVouchVo.setNo(IUFTypesInitializationValue.IZERO);

      this.m_rtVouchVo.setPk_corp(this.m_vouchTempHeadVo.getPk_corp());

      this.m_rtVouchVo.setPk_glorg(msgVo.getPkAccOrg());

      this.m_rtVouchVo.setPk_glbook(msgVo.getPkAccount());
      GlorgbookVO glorgbook = getGlorgbook(msgVo.getPkAccOrg(), msgVo.getPkAccount());

      this.m_rtVouchVo.setPk_glorgbook(glorgbook.getPrimaryKey());

      strValue = this.m_vouchTempHeadVo.getMakebilldateform();
      if ((strValue != null) && (!strValue.equals(""))) {
        tmpObj = getParseValue(strValue, this.m_dataVo, 0, 2);
      }
      if ((this.m_vouchTempHeadVo.getExectype().intValue() == 1) || 
        (this.m_vouchTempHeadVo.getExectype().intValue() == 2))
      {
        if (tmpObj == null) {
          tmpObj = msgVo.getBusiDate();
        }
        if (tmpObj == null) {
          throw new RtVouchException(NCLangResOnserver.getInstance()
            .getStrByID("fidap", 
            "UPPfidap-000070"), 1);
        }
        this.m_rtVouchVo.setPrepareddate(new UFDate(tmpObj.toString()));
      }
      else if ((this.m_vouchTempHeadVo.getExectype().intValue() == 4) || 
        (this.m_vouchTempHeadVo.getExectype().intValue() == 5))
      {
        if (tmpObj == null)
          throw new RtVouchException(NCLangResOnserver.getInstance()
            .getStrByID("fidap", 
            "UPPfidap-000070"), 1);
      }
      else {
        this.m_rtVouchVo.setPrepareddate(tmpObj == null ? null : new UFDate(tmpObj.toString()));
      }

      if ((tmpObj != null) && (tmpObj.toString().length() > 7)) {
        AccperiodVO vo = this.m_trp.getAccperiodVOByDateAndOrgbook(this.m_rtVouchVo.getPk_glorgbook(), tmpObj);
        if (vo != null) {
          this.m_rtVouchVo.setPeriod(vo.getVosMonth()[0].getMonth());
          this.m_rtVouchVo.setYear(vo.getPeriodyear());
        }
      }
      tmpObj = null;

      strValue = this.m_vouchTempHeadVo.getBillnum();
      if ((strValue != null) && (!strValue.trim().equals(""))) {
        tmpObj = getParseValue(strValue, this.m_dataVo, 0, 3);
        if (tmpObj != null)
          this.m_rtVouchVo.setAttachment(new Integer(tmpObj.toString()));
        else
          this.m_rtVouchVo.setAttachment(IUFTypesInitializationValue.IZERO);
      } else {
        this.m_rtVouchVo.setAttachment(IUFTypesInitializationValue.IZERO);
      }

      strValue = this.m_vouchTempHeadVo.getAssino();
      if ((strValue != null) && (!strValue.trim().equals(""))) {
        tmpObj = getParseValue(strValue, this.m_dataVo, 0, 0);
        if (tmpObj == null)
          this.m_rtVouchVo.setAssino(null);
        else {
          this.m_rtVouchVo.setAssino(tmpObj.toString());
        }

      }

      strValue = this.m_vouchTempHeadVo.getOperatorform();
      if ((strValue != null) && (!strValue.equals("")))
      {
        tmpObj = getParseValue(strValue, this.m_dataVo, 0, 0);
        String str = (String)tmpObj;
        if ((str == null) || (str.trim().length() == 0))
          tmpObj = null;
      }
      else
      {
        tmpObj = null;
      }

      if ((this.m_vouchTempHeadVo.getExectype().intValue() == 1) || 
        (this.m_vouchTempHeadVo.getExectype().intValue() == 2))
      {
        if (tmpObj == null) {
          tmpObj = msgVo.getOperator();
        }
        if (tmpObj == null) {
          throw new RtVouchException(NCLangResOnserver.getInstance()
            .getStrByID("fidap", 
            "UPPfidap-000071"), 1);
        }
        this.m_rtVouchVo.setPk_prepared(tmpObj.toString());
      }
      else if ((this.m_vouchTempHeadVo.getExectype().intValue() == 4) || 
        (this.m_vouchTempHeadVo.getExectype().intValue() == 5))
      {
        if (tmpObj == null)
          throw new RtVouchException(NCLangResOnserver.getInstance()
            .getStrByID("fidap", 
            "UPPfidap-000071"), 1);
      }
      else {
        this.m_rtVouchVo.setPk_prepared(tmpObj == null ? null : tmpObj.toString());
      }

      this.m_rtVouchVo.setModifyflag(this.m_modifyFlag);

      this.m_rtVouchVo.setPk_system(this.m_vouchTempHeadVo.getPk_sys());

      if ((this.m_vouchTempHeadVo.getVouchtype() == null) || (this.m_vouchTempHeadVo.getVouchtype().trim().length() == 0)) {
        Logger.error(msgVo.getBillCode() + "......" + msgVo.getPkAccount() + "......凭证类别没有设置！");
        throw new RtVouchException(NCLangResOnserver.getInstance()
          .getStrByID("fidap", 
          "UPPfidap-000072"), 1);
      }

      Object obj = getParseValue(this.m_vouchTempHeadVo.getVouchtype(), this.m_dataVo, 0, 0);
      String snewVouchType = obj == null ? null : obj.toString();
      Logger.info("###DAP DEBUG ###" + msgVo.getBillCode() + "......" + msgVo.getPkAccount() + 
        "......凭证类别计算分录0为" + snewVouchType + "！");
      int iDtlCount = this.m_dataVo.getChildrenVO().length;
      for (int i = 1; i < iDtlCount; i++) {
        obj = getParseValue(this.m_vouchTempHeadVo.getVouchtype(), this.m_dataVo, i, 0);
        String stmpVouchType = obj == null ? null : obj.toString();
        if ((stmpVouchType != null) && (stmpVouchType.trim().length() > 0)) {
          Logger.info("###DAP DEBUG ###" + msgVo.getBillCode() + "......" + msgVo.getPkAccount() + 
            "......凭证类别计算分录" + String.valueOf(i) + "为" + stmpVouchType + "！");
          if ((snewVouchType != null) && (snewVouchType.trim().length() > 0) && 
            (!stmpVouchType.equals(snewVouchType))) {
            Logger.error(msgVo.getBillCode() + "......" + msgVo.getPkAccount() + "......凭证类别计算为空！");
            throw new RtVouchException(NCLangResOnserver.getInstance()
              .getStrByID("fidap", 
              "UPPfidap-000073") + 
              this.m_vouchTempHeadVo.getVouchtype(), 1);
          }
          snewVouchType = stmpVouchType;
        }
      }

      if ((snewVouchType == null) || (snewVouchType.trim().length() == 0)) {
        Logger.error(msgVo.getBillCode() + "......" + msgVo.getPkAccount() + "......凭证类别最终计算结果为空！");
        throw new RtVouchException(NCLangResOnserver.getInstance()
          .getStrByID("fidap", 
          "UPPfidap-000074") + 
          this.m_vouchTempHeadVo.getVouchtype(), 1);
      }
      this.m_rtVouchVo.setPk_vouchertype(snewVouchType.trim());

      this.m_rtVouchVo.setVoucherkind(new Integer(0));

      this.m_rtVouchVo.setDetailmodflag(IUFTypesInitializationValue.UBFALS);
      this.m_rtVouchVo = createHeadExt(this.m_rtVouchVo, this.m_vouchTempHeadVo.getFormulas(), 0);
    }
    catch (Throwable ex) {
      debug("生成凭证表头数据出错！", msgVo, ex);
      if (ex.getMessage() != null) {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000059", null, 
          new String[] { ex.getMessage(), strValue });
        strValue = message;
      } else {
        String message = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000075", null, 
          new String[] { strValue });
        strValue = message;
      }
      throw new RtVouchException(strValue, 1);
    }
  }

  private void dealWithInAss(FreevalueVO[] inAssVos, DetailVO detailVo)
    throws RtVouchException
  {
    try
    {
      if ((inAssVos == null) || (inAssVos.length == 0)) {
        return;
      }
      FreevalueVO[] detailAssVos = detailVo.getAss();
      if ((detailAssVos != null) && (detailAssVos.length != 0)) {
        Hashtable tmpHas = new Hashtable();
        for (int i = 0; i < detailAssVos.length; i++) {
          tmpHas.put(detailAssVos[i].getChecktype(), detailAssVos[i]);
        }
        for (int i = 0; i < inAssVos.length; i++) {
          tmpHas.put(inAssVos[i].getChecktype(), inAssVos[i]);
        }
        if (tmpHas.size() == 0) {
          detailAssVos = (FreevalueVO[])null;
        } else {
          int j = 0;
          detailAssVos = new FreevalueVO[tmpHas.size()];
          Enumeration e = tmpHas.elements();
          while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if ((o instanceof FreevalueVO))
              detailAssVos[(j++)] = ((FreevalueVO)o);
          }
        }
      }
      else {
        detailAssVos = inAssVos;
      }
      detailVo.setAss(detailAssVos);
    } catch (Exception ex) {
      Logger.error("处理辅助核算的时候出错！", ex);
      String strValue = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000076");
      if (ex.getMessage() != null) {
        strValue = strValue + ":" + ex.getMessage();
      }
      throw new RtVouchException(strValue, 1);
    }
  }

  private void deleteMessage(DapMsgVO inVo)
    throws DapBusinessException
  {
    try
    {
      getDapDMO().deleteMessage(inVo);
    } catch (Exception ex) {
      Logger.error("删除消息的时候出错！");
      if ((ex instanceof DapBusinessException)) {
        throw ((DapBusinessException)ex);
      }
      throw new DapBusinessException(ex.getMessage(), ex);
    }
  }

  private void delReflect(String pk_finIndex)
    throws DapBusinessException
  {
    DapFinMsgVO vo = new DapFinMsgVO();
    vo.setPkFinmsg(pk_finIndex);
    try
    {
      getFinindexDMO().delete(vo);
    } catch (Exception ex) {
      Logger.error("删除对照表出错！", ex);
      if ((ex instanceof DapBusinessException)) {
        throw ((DapBusinessException)ex);
      }
      throw new DapBusinessException(ex.getMessage(), ex);
    }
  }

  private void getAssBySubject(DetailVO rtVo)
    throws DapBusinessException, RtVouchException
  {
    FreevalueVO[] retVos = (FreevalueVO[])null;
    FreevalueVO[] freeVos = rtVo.getAss();
    if (freeVos == null) {
      return;
    }
    String subjectNo = rtVo.getPk_accsubj();

    Hashtable subHas = this.m_trp.queryAssBySubjPKs(subjectNo);
    Vector tmpVec = new Vector();
    try {
      for (int i = 0; i < freeVos.length; i++) {
        if (!subHas.containsKey(freeVos[i].getChecktype()))
          continue;
        tmpVec.addElement(freeVos[i]);
      }

      if (tmpVec.size() > 0) {
        retVos = new FreevalueVO[tmpVec.size()];
        tmpVec.copyInto(retVos);
      }

      rtVo.setAss(retVos);
    } catch (Exception ex) {
      String strValue = null;
      Logger.error("读取科目的辅助核算出错！", ex);
      if (ex.getMessage() != null)
        strValue = ex.getMessage() + 
          NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000078");
      else {
        strValue = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000078");
      }
      throw new RtVouchException(strValue, 1);
    }
  }

  private String getSpecialValue(String value)
    throws Exception
  {
    if (value == null)
      return null;
    CumandocVO mandoc = Cumandoc.findByPrimaryKey(value);
    if (mandoc.getCustflag().equals("3")) {
      CumandocVO filter = new CumandocVO();
      filter.setPk_cubasdoc(mandoc.getPk_cubasdoc());
      filter.setPk_corp(mandoc.getPk_corp());
      filter.setCustflag("2");
      CumandocVO[] newmandocs = Cumandoc.queryByVO(filter, new Boolean(true));
      if ((newmandocs != null) && (newmandocs.length >= 1))
        return newmandocs[0].getPrimaryKey();
    }
    return value;
  }

  private void getSystemFactorAndView()
    throws RtVouchException
  {
    StringBuffer grpInsubjClassS = new StringBuffer("(");
    String grpInsubjClass = null;
    Hashtable h = new Hashtable();
    int len = this.m_vouchTempDetailVos.length - 1;
    for (int i = 0; i < len; i++) {
      if (!h.containsKey(this.m_vouchTempDetailVos[i].getPk_insubjclass())) {
        grpInsubjClassS.append("'").append(this.m_vouchTempDetailVos[i].getPk_insubjclass()).append("',");
      }
    }
    if (!h.containsKey(this.m_vouchTempDetailVos[len].getPk_insubjclass())) {
      grpInsubjClassS.append("'").append(this.m_vouchTempDetailVos[len].getPk_insubjclass()).append("')");
    }
    grpInsubjClass = grpInsubjClassS.toString();
    try {
      this.m_factorVos = this.m_trp
        .queryFactor(this.m_vouchTempHeadVo.getPk_proc(), this.m_vouchTempHeadVo.getPkAccOrg(), 
        this.m_vouchTempHeadVo.getPkAccount(), grpInsubjClass, this.factorkey, this.factorHkey);
    }
    catch (Exception ex) {
      Logger.error("读取缓存的影响因素出错！", ex);
      String errString = null;
      if (ex.getMessage() != null)
        errString = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000080") + 
          "\n" + ex.getMessage();
      else {
        errString = NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000080");
      }
      throw new RtVouchException(errString, 1);
    }
    try
    {
      this.m_subViewVos = this.m_trp
        .querySubjView(grpInsubjClass, this.m_vouchTempHeadVo.getPkAccOrg(), 
        this.m_vouchTempHeadVo.getPkAccount(), this.subviewkey, this.subviewhkey);
    } catch (Exception ex) {
      Logger.error("读取缓存的科目对照表出错!", ex);
      throw new RtVouchException(
        NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000081"), 
        1);
    }
  }

  private void getVouchTemp(DapFinMsgVO msgVo)
    throws DapBusinessException, RtVouchException
  {
    try
    {
      StringBuffer sb = new StringBuffer().append(msgVo.getCorp()).append(msgVo.getSys()).append(msgVo.getProc())
        .append(msgVo.getBusiType()).append(msgVo.getPkAccOrg()).append(msgVo.getPkAccount());
      this.tmpkey = sb.toString();
      this.subviewkey = CacheManager.SUBVIEWAPP;
      this.subviewhkey = (this.tmpkey + CacheManager.SUBVIEWHAPP);
      this.factorkey = (this.tmpkey + CacheManager.FACTORAPP);
      this.factorHkey = (this.tmpkey + CacheManager.FACTORHAPP);
      VoucherTemplateVO vouchTemplateVo = this.m_trp.queryVoucherTemplet(DapFinMsgVO.changeMsgVO(msgVo), this.tmpkey);
      if (vouchTemplateVo == null) {
        throw new RtVouchException(NCLangResOnserver.getInstance()
          .getStrByID("fidap", 
          "UPPfidap-000082"), 1);
      }

      this.m_vouchTempHeadVo = vouchTemplateVo.getTempVO();

      Vector tmpVec = vouchTemplateVo.getTempDtl();
      if (tmpVec.size() > 0) {
        this.m_vouchTempDetailVos = new VouchtempBVO[tmpVec.size()];
        tmpVec.copyInto(this.m_vouchTempDetailVos);
      }

      tmpVec = vouchTemplateVo.getTempAss();
      if (tmpVec.size() > 0) {
        this.m_vouchTempAssVos = new VouchtempassVO[tmpVec.size()];
        tmpVec.copyInto(this.m_vouchTempAssVos);
      }

      this.m_tempDetailAssHas = vouchTemplateVo.getDetailAss();
    } catch (Exception ex) {
      Logger.error("读取缓存的凭证模板出错！", ex);
      if ((ex instanceof RtVouchException)) {
        throw ((RtVouchException)ex);
      }
      throw new DapBusinessException(ex.getMessage(), new Exception(ex.getMessage()));
    }
  }

  private void reduceZeroRtVouch()
  {
    List tmplist = new ArrayList();
    for (int i = 0; i < this.m_rtVouchbVos.length; i++) {
      if (this.m_rtVouchbVos[i] != null) {
        tmplist.add(this.m_rtVouchbVos[i]);
      }
    }
    this.m_rtVouchbVos = new DetailVO[tmplist.size()];
    tmplist.toArray(this.m_rtVouchbVos);

    for (int i = 0; i < this.m_rtVouchbVos.length - 1; i++) {
      if ((this.m_rtVouchbVos[i] == null) || (
        ((this.m_rtVouchbVos[i].getLocalcreditamount() == null) || (
        this.m_rtVouchbVos[i].getLocalcreditamount().doubleValue() == 0.0D)) && (
        (this.m_rtVouchbVos[i].getLocaldebitamount() == null) || (
        this.m_rtVouchbVos[i].getLocaldebitamount().doubleValue() == 0.0D)))) continue;
      this.m_detailvec.addElement(this.m_rtVouchbVos[i]);
    }

    if (this.m_detailvec.size() > 0) {
      this.m_isZero = IUFTypesInitializationValue.UBFALS;
      this.m_detailvec.addElement(this.m_rtVouchbVos[(this.m_rtVouchbVos.length - 1)]);
    } else {
      this.m_isZero = IUFTypesInitializationValue.UBTRUE;
    }
  }

  private void setGLVouchId(DetailVO tmpVo)
    throws RtVouchException, DapBusinessException
  {
    try
    {
      String m_Id = null;
      FreevalueVO[] assVos = (FreevalueVO[])null;
      Vector tmpVec = new Vector();

      StringBuffer key = new StringBuffer();
      String[] strAry = (String[])null;
      StringBuffer key1 = new StringBuffer();
      if (tmpVo.getAss() != null) {
        FreevalueVO assVo = null;
        for (int i = 0; i < tmpVo.getAss().length; i++) {
          if (tmpVo.getAss()[i] != null) {
            assVo = new FreevalueVO();
            assVo.setChecktype(tmpVo.getAss()[i].getChecktype());
            assVo.setCheckvalue(tmpVo.getAss()[i].getCheckvalue());

            BdinfoVO defVo = this.m_trp.getBdInfoVO(assVo.getChecktype());
            try {
              if ((defVo != null) && (defVo.getBdcode().startsWith("D"))) {
                key1.append(defVo.getPk_defdef()).append(assVo.m_checkvalue).append(
                  this.m_vouchTempHeadVo.getPk_corp());
                strAry = this.m_trp.queryDefPK(key1.toString(), defVo.getPk_defdef(), assVo.m_checkvalue, 
                  this.m_vouchTempHeadVo.getPk_corp());
                assVo.setCheckvalue(strAry[0]);
                assVo.m_checkvaluecode = strAry[1];
                assVo.m_checkvaluename = strAry[2];
                key1 = new StringBuffer();
              }
            }
            catch (NullPointerException localNullPointerException) {
            }
            key.append(assVo.m_checktype).append(assVo.m_checkvalue);
            tmpVec.addElement(assVo);
          }
        }
      }

      if (tmpVec.size() > 0) {
        assVos = new FreevalueVO[tmpVec.size()];
        tmpVec.copyInto(assVos);
        m_Id = this.m_trp.queryAssIds(assVos, key.toString());

        tmpVo.setAssid(m_Id == null ? null : m_Id.trim());
      } else {
        tmpVo.setAssid(null);
      }
    } catch (Exception ex) {
      Logger.error("保存总账辅助核算信息出错！", ex);
      throw new RtVouchException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000087") + 
        ex.getMessage(), 1);
    }
  }

  private void setMoneySubInfo(DetailVO tmpVo)
    throws RtVouchException
  {
    try
    {
      if (!m_SubjectStyleHas.containsKey(tmpVo.getPk_accsubj())) {
        AccsubjVO vo = Accsubj.findByPrimaryKey(tmpVo.getPk_accsubj());
        if (vo != null)
          m_SubjectStyleHas.put(tmpVo.getPk_accsubj(), vo.getCashbankflag());
        else {
          m_SubjectStyleHas.put(tmpVo.getPk_accsubj(), IUFTypesInitializationValue.IZERO);
        }
      }
      Integer tmpInt = (Integer)m_SubjectStyleHas.get(tmpVo.getPk_accsubj());
      if (tmpInt.intValue() == 0)
      {
        tmpVo.setCheckstyle(null);

        tmpVo.setCheckdate(null);

        tmpVo.setCheckno(null);
      }
    } catch (Exception ex) {
      Logger.error("保存票据信息出错！", ex);
      throw new RtVouchException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000088") + 
        ex.getMessage(), 1);
    }
  }

  private GlorgbookVO getGlorgbook(String pk_glorg, String pk_glbook)
    throws BusinessException
  {
    String key = pk_glorg + pk_glbook + "pk_glorgbook";
    GlorgbookVO vo = (GlorgbookVO)CacheManager.getInstance(Thread.currentThread()).getValue(
      "pk_glorgbook", key);
    if (vo == null) {
      vo = GLOrgBookAcc.getGlOrgBookVOByPk_GlorgAndPk_Glbook(pk_glorg, pk_glbook);
      if (vo != null) {
        CacheManager.getInstance(Thread.currentThread()).putValue("pk_glorgbook", key, vo);
      }
    }
    return vo;
  }

  private String getDefaultGlorgbook(String pk_corp)
    throws BusinessException
  {
    String key = pk_corp + "pk_glorgbook";
    String glorgbook = (String)CacheManager.getInstance(Thread.currentThread()).getValue(
      "pk_glorgbook", key);
    if (glorgbook == null) {
      glorgbook = GLOrgBookAcc.getDefaultPk_GLOrgBook(pk_corp);
      CacheManager.getInstance(Thread.currentThread()).putValue("pk_glorgbook", key, glorgbook);
    }
    return glorgbook;
  }

  private boolean isCashFlow(String pk_accsubj)
    throws DapBusinessException, RtVouchException
  {
    AccsubjVO vo = getAccSubjVO(pk_accsubj);
    if (vo == null)
      throw new RtVouchException(
        NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000069"), 
        1);
    return vo.getCashbankflag().intValue() > 0;
  }

  private AccsubjVO getAccSubjVO(String pk_accsubj) throws RtVouchException {
    String key = "pk_accsubj" + pk_accsubj;
    AccsubjVO vo = (AccsubjVO)CacheManager.getInstance(Thread.currentThread()).getValue("pk_accsubj", 
      key);
    if (vo == null) {
      vo = null;
      try {
        vo = Accsubj.findByPrimaryKey(pk_accsubj);
      } catch (BusinessException e) {
        Logger.error("读取科目信息出错！", e);
        throw new RtVouchException(e.getMessage(), 1);
      }
      if (vo != null) {
        CacheManager.getInstance(Thread.currentThread()).putValue("pk_accsubj", key, vo);
      }
    }
    return vo;
  }

  private VoucherVO createHeadExt(VoucherVO rtbv, FormulaElement[] m_formulas, int detailIndex)
    throws Exception
  {
    Object tmpObj = null;

    if (m_formulas == null) {
      throw new RtVouchException(NCLangResOnserver.getInstance().getStrByID("fidap", "UPPfidap-000061"), 
        1);
    }

    String strDEF1 = m_formulas[0].getFormula();
    String strDEF2 = m_formulas[1].getFormula();
    String strDEF3 = m_formulas[2].getFormula();
    String strDEF4 = m_formulas[3].getFormula();
    String strDEF5 = m_formulas[4].getFormula();
    String strDEF6 = m_formulas[5].getFormula();
    String strDEF7 = m_formulas[6].getFormula();
    String strDEF8 = m_formulas[7].getFormula();
    String strDEF9 = m_formulas[8].getFormula();
    String strDEF10 = m_formulas[9].getFormula();
    String strFREEVALUE1 = m_formulas[10].getFormula();
    String strFREEVALUE2 = m_formulas[11].getFormula();
    String strFREEVALUE3 = m_formulas[12].getFormula();
    String strFREEVALUE4 = m_formulas[13].getFormula();
    String strFREEVALUE5 = m_formulas[14].getFormula();

    if ((strDEF1 != null) && (!strDEF1.trim().equals(""))) {
      tmpObj = getParseValue(strDEF1, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree1(tmpObj.toString());
      }
    }
    if ((strDEF2 != null) && (!strDEF2.trim().equals(""))) {
      tmpObj = getParseValue(strDEF2, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree2(tmpObj.toString());
      }
    }
    if ((strDEF3 != null) && (!strDEF3.trim().equals(""))) {
      tmpObj = getParseValue(strDEF3, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree3(tmpObj.toString());
      }
    }

    if ((strDEF4 != null) && (!strDEF4.trim().equals(""))) {
      tmpObj = getParseValue(strDEF4, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree4(tmpObj.toString());
      }
    }

    if ((strDEF5 != null) && (!strDEF5.trim().equals(""))) {
      tmpObj = getParseValue(strDEF5, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree5(tmpObj.toString());
      }
    }

    if ((strDEF6 != null) && (!strDEF6.trim().equals(""))) {
      tmpObj = getParseValue(strDEF6, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree6(tmpObj.toString());
      }
    }

    if ((strDEF7 != null) && (!strDEF7.trim().equals(""))) {
      tmpObj = getParseValue(strDEF7, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree7(tmpObj.toString());
      }
    }

    if ((strDEF8 != null) && (!strDEF8.trim().equals(""))) {
      tmpObj = getParseValue(strDEF8, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree8(tmpObj.toString());
      }
    }

    if ((strDEF9 != null) && (!strDEF9.trim().equals(""))) {
      tmpObj = getParseValue(strDEF9, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree9(tmpObj.toString());
      }
    }
    if ((strDEF10 != null) && (!strDEF10.trim().equals(""))) {
      tmpObj = getParseValue(strDEF10, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFree10(tmpObj.toString());
      }
    }

    if ((strFREEVALUE1 != null) && (!strFREEVALUE1.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE1, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue1(tmpObj.toString());
      }
    }

    if ((strFREEVALUE2 != null) && (!strFREEVALUE2.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE2, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue2(tmpObj.toString());
      }
    }

    if ((strFREEVALUE3 != null) && (!strFREEVALUE3.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE3, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue3(tmpObj.toString());
      }
    }

    if ((strFREEVALUE4 != null) && (!strFREEVALUE4.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE4, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue4(tmpObj.toString());
      }
    }

    if ((strFREEVALUE5 != null) && (!strFREEVALUE5.trim().equals(""))) {
      tmpObj = getParseValue(strFREEVALUE5, this.m_dataVo, detailIndex, 0);
      if (tmpObj != null) {
        rtbv.setFreevalue5(tmpObj.toString());
      }
    }
    return rtbv;
  }

  private AccsubjVO getBookAccsubjVO(String pk_glorg, String pk_glbook, String pk_accsubj_src)
    throws RtVouchException
  {
    AccsubjVO srcvo = getAccSubjVO(pk_accsubj_src);
    if (srcvo == null)
      throw new RtVouchException("根据科目主键取得科目VO为空！，导致根据科目直接生成凭证科目无法完成", 1);
    return getAccsubjVOByCode(pk_glorg, pk_glbook, srcvo.getSubjcode());
  }

  private boolean isCorpBaseBook(String pk_corp, String pk_glorg, String pk_glbook)
    throws BusinessException
  {
    String defaultGLorgbook = GLOrgBookAcc.getDefaultPk_GLOrgBook(pk_corp);
    String pk_glorgbook = getGlorgbook(pk_glorg, pk_glbook).getPrimaryKey();
    if (defaultGLorgbook != null)
      return defaultGLorgbook.equalsIgnoreCase(pk_glorgbook);
    if (pk_glorgbook != null) {
      return pk_glorgbook.equalsIgnoreCase(defaultGLorgbook);
    }
    return false;
  }

  private AccsubjVO getAccsubjVOByCode(String pk_glorg, String pk_glbook, String accsubjCode)
    throws RtVouchException
  {
    String pk_glorgbook = null;
    try {
      pk_glorgbook = getGlorgbook(pk_glorg, pk_glbook).getPrimaryKey();
    } catch (Exception ex) {
      Logger.error("读取主体账簿数据出错！", ex);
    }
    String key = "pk_glorgbook_accsubj" + pk_glorgbook;
    AccsubjVO resVO = null;

    HashMap hAccsubj = (HashMap)CacheManager.getInstance(Thread.currentThread()).getValue(
      "pk_glorgbook_accsubj", key);
    if (hAccsubj == null) {
      hAccsubj = new HashMap();
    }

    resVO = (AccsubjVO)hAccsubj.get(accsubjCode);
    if (resVO == null) {
      try
      {
        AccsubjVO vos = Accsubj.queryByCode(pk_glorgbook, null, accsubjCode);

        if (vos != null)
          hAccsubj.put(vos.getSubjcode(), vos);
      }
      catch (Exception ex) {
        Logger.error("读取主体账簿的科目数据出错！", ex);
      }
    }
    resVO = (AccsubjVO)hAccsubj.get(accsubjCode);

    return resVO;
  }

  public void debug(String error, DapFinMsgVO msgVo, Throwable ex) {
    Logger.debug("##DAP DEBUG### 计算单据生成凭证时出现错误，单据的输入信息如下：");
    Logger.debug("##DAP DEBUG### 公司主键：" + msgVo.getCorp() + "###单据号:" + msgVo.getBillCode());
    Logger.debug("##DAP DEBUG### 系统类型：" + msgVo.getSys() + "###单据类型:" + msgVo.getProc());
    if (msgVo.getBusiType() != null)
      Logger.debug("##DAP DEBUG### 业务类型：" + msgVo.getBusiType() + "###业务类型名称:" + msgVo.getBusiName());
    if (msgVo.getPkAccOrg() != null)
      Logger.debug("##DAP DEBUG### 会计主体：" + msgVo.getPkAccOrg() + "###核算账簿:" + msgVo.getPkAccount());
    if (ex != null)
      Logger.error(error, ex);
  }

  public Object getParseValue(String formula, AggregatedValueObject dataVO, int lineNumber, int returnType)
    throws PfFormException
  {
    Object obj = null;
    PfFormulaParse parser = (PfFormulaParse)CacheManager.getInstance(null).getValue(CacheManager.FORMULAPARSE, 
      "PfformulaParser");
    if (parser == null) {
      parser = new PfFormulaParse();
      CacheManager.getInstance(null).putValue(CacheManager.FORMULAPARSE, "PfformulaParser", parser);
    }
    obj = parser.getParseValueFip(formula, dataVO, lineNumber, returnType);
    return obj;
  }
}