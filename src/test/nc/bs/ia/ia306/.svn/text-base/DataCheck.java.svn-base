package nc.bs.ia.ia306;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.ia.pub.IATool;
import nc.vo.ia.ia306.AccountCheckVO;
import nc.vo.ia.ia306.GLVO;
import nc.vo.ia.ia306.ParamVO308;
import nc.vo.ia.pub.Log;
import nc.vo.ia.pub.SystemAccessException;
import nc.vo.pub.lang.UFDouble;

public class DataCheck
{
  public AccountCheckVO[] checkData(ParamVO308 para)
  {
    AccountCheckVO[] vos = new AccountCheckVO[0];
    List list = new ArrayList();

    Map map = getGenealAccount(para);
    ParamVO308[] voaSum = getDetailAccount(para);
    if ((voaSum == null) || (voaSum.length == 0)) {
      return vos;
    }
    if (map.size() == 0) {
      return vos;
    }

    int size = voaSum.length;
    for (int i = 0; i < size; ++i) {
      String key = getAuditKey(voaSum[i]);
      GLVO glvo = (GLVO)map.get(key);
      if (glvo == null) {
        Log.info("+++++++++++++++++++++++++++++++" + key);
      }
      else {
        AccountCheckVO vo = checkByAuditKey(voaSum[i], glvo);
        if (vo != null)
          System.out.println("´æ»õ±àÂëÎª:"+vo.getInvCode());
          list.add(vo);
      }
    }
    vos = new AccountCheckVO[list.size()];
    vos = (AccountCheckVO[])(AccountCheckVO[])list.toArray(vos);
    return vos;
  }

  private AccountCheckVO checkByAuditKey(ParamVO308 detailAccount, GLVO generalAccount)
  {
    boolean equal = true;
    int errorType = 0;
    if (!(checkInMmy(detailAccount, generalAccount))) {
      equal = false;
      errorType = 0;
    }
    else if (!(checkInNum(detailAccount, generalAccount))) {
      equal = false;
      errorType = 1;
    }
    else if (!(checkOutMny(detailAccount, generalAccount))) {
      equal = false;
      errorType = 2;
    }
    else if (!(checkOutNum(detailAccount, generalAccount))) {
      equal = false;
      errorType = 8;
    }
    else if (!(checkNabNum(detailAccount, generalAccount))) {
      equal = false;
      errorType = 6;
    }
    else if (!(checkNabMny(detailAccount, generalAccount))) {
      equal = false;
      errorType = 5;
    }
    else if (!(checkInVaryMny(detailAccount, generalAccount))) {
      equal = false;
      errorType = 3;
    }
    else if (!(checkOutVaryMny(detailAccount, generalAccount))) {
      equal = false;
      errorType = 4;
    }
    else if (!(checkNabVaryMny(detailAccount, generalAccount))) {
      equal = false;
      errorType = 7;
    }
    AccountCheckVO vo = null;
    if (!(equal)) {
      vo = create(detailAccount, generalAccount, errorType);
      isEqual(detailAccount.getInMny(), generalAccount.getNinmny());
    }
    return vo;
  }

  private boolean isEqual(UFDouble d1, UFDouble d2) {
    boolean equal = false;
    UFDouble d3 = IATool.getInstance().sub(d2, d1);
    if (d3.doubleValue() == 0.0D) {
      equal = true;
    }
    return equal;
  }

  private Map getGenealAccount(ParamVO308 para) {
    GLVO[] voaGL = null;
    try {
      CDMO dmo = new CDMO();
      voaGL = dmo.queryAll(para.getPkCorp());
    }
    catch (Exception ex) {
      throw new SystemAccessException(ex);
    }
    Map map = new HashMap();
    if (voaGL == null) {
      return map;
    }

    int size = voaGL.length;
    for (int i = 0; i < size; ++i) {
      String key = getAuditKey(voaGL[i]);
      map.put(key, voaGL[i]);
    }
    return map;
  }

  private String getAuditKey(GLVO glvo) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(glvo.getCrdcenterid());
    buffer.append(glvo.getCinventoryid());
    buffer.append((glvo.getVbatch() == null) ? "" : glvo.getVbatch());
    return buffer.toString();
  }

  private String getAuditKey(ParamVO308 vo) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(vo.getPkCalbody());
    buffer.append(vo.getPkInv());
    if (vo.getVBatch() != null)
    {
      buffer.append((vo.getVBatch().equals("NULL")) ? "" : vo.getVBatch());
    }
    return buffer.toString();
  }

  private ParamVO308[] getDetailAccount(ParamVO308 para) {
    CDMO dmo = null;
    ParamVO308 voRP = null;
    try {
      dmo = new CDMO();
      voRP = dmo.getSumIOL(para);
    }
    catch (Exception ex) {
      throw new SystemAccessException(ex);
    }
    ParamVO308[] voaSum = voRP.getVOA();
    return voaSum;
  }

  private boolean checkInMmy(ParamVO308 detailAccount, GLVO generalAccount) {
    UFDouble inMny = detailAccount.getInMny();
    UFDouble generalInMny = generalAccount.getNinmny();
    return isEqual(inMny, generalInMny);
  }

  private boolean checkInNum(ParamVO308 detailAccount, GLVO generalAccount) {
    UFDouble inNum = detailAccount.getInNum();
    UFDouble generalInNum = generalAccount.getNinnum();
    return isEqual(inNum, generalInNum);
  }

  private boolean checkOutNum(ParamVO308 detailAccount, GLVO generalAccount) {
    UFDouble outNum = detailAccount.getOutNum();
    UFDouble generalOutNum = generalAccount.getNoutnum();
    return isEqual(outNum, generalOutNum);
  }

  private boolean checkOutMny(ParamVO308 detailAccount, GLVO generalAccount) {
    UFDouble outMny = detailAccount.getOutMny();
    UFDouble generalOutMny = generalAccount.getNoutmny();
    return isEqual(outMny, generalOutMny);
  }

  private boolean checkNabMny(ParamVO308 detailAccount, GLVO generalAccount) {
    UFDouble nabMny = detailAccount.getNabMny();
    UFDouble generalNabMny = generalAccount.getNabmny();
    return isEqual(nabMny, generalNabMny);
  }

  private boolean checkNabNum(ParamVO308 detailAccount, GLVO generalAccount) {
    UFDouble nabNum = detailAccount.getNabNum();
    UFDouble generalNabNum = generalAccount.getNabnum();
    return isEqual(nabNum, generalNabNum);
  }

  private boolean checkInVaryMny(ParamVO308 detailAccount, GLVO generalAccount) {
    UFDouble inVaryMny = detailAccount.getInvarymny();
    UFDouble generalInVaryMny = generalAccount.getNinvarymny();
    return isEqual(inVaryMny, generalInVaryMny);
  }

  private boolean checkOutVaryMny(ParamVO308 detailAccount, GLVO generalAccount) {
    UFDouble outVaryMny = detailAccount.getOutvarymny();
    UFDouble generalOutVaryMny = generalAccount.getNoutvarymny();
    return isEqual(outVaryMny, generalOutVaryMny);
  }

  private boolean checkNabVaryMny(ParamVO308 detailAccount, GLVO generalAccount) {
    UFDouble nabVaryMny = detailAccount.getAbvarymny();
    UFDouble generalNabVaryMny = generalAccount.getNabvarymny();
    return isEqual(nabVaryMny, generalNabVaryMny);
  }

  private AccountCheckVO create(ParamVO308 detailAccount, GLVO generalAccount, int errorType)
  {
    AccountCheckVO vo = new AccountCheckVO();
    vo.setErrorType(errorType);
    vo.setPk_corp(generalAccount.getPk_corp());
    vo.setCrdcenterid(generalAccount.getCrdcenterid());
    vo.setCinventoryid(generalAccount.getCinventoryid());
    vo.setVbatch(generalAccount.getVbatch());

    vo.setInMny(generalAccount.getNinmny());
    vo.setOutMny(generalAccount.getNoutmny());
    vo.setInNum(generalAccount.getNinnum());
    vo.setOutNum(generalAccount.getNoutnum());
    vo.setNabMny(generalAccount.getNabmny());
    vo.setNabNum(generalAccount.getNabnum());
    vo.setInVaryMny(generalAccount.getNinvarymny());
    vo.setOutVaryMny(generalAccount.getNoutvarymny());
    vo.setNabVaryMny(generalAccount.getNabvarymny());

    vo.setDetailInMny(detailAccount.getInMny());
    vo.setDetailInNum(detailAccount.getInNum());
    vo.setDetailInVaryMny(detailAccount.getInvarymny());
    vo.setDetailOutMny(detailAccount.getOutMny());
    vo.setDetailOutNum(detailAccount.getOutNum());
    vo.setDetailOutVaryMny(detailAccount.getOutvarymny());
    vo.setDetailNabMny(detailAccount.getNabMny());
    vo.setDetailNabNum(detailAccount.getNabNum());
    vo.setDetailNabVaryMny(detailAccount.getAbvarymny());

    UFDouble mny = IATool.getInstance().sub(detailAccount.getInMny(), detailAccount.getOutMny());

    mny = IATool.getInstance().sub(detailAccount.getNabMny(), mny);
    vo.setBeginMny(mny);

    UFDouble num = IATool.getInstance().sub(detailAccount.getInNum(), detailAccount.getOutNum());

    num = IATool.getInstance().sub(detailAccount.getNabNum(), num);
    vo.setBeginNum(num);

    UFDouble varyMny = IATool.getInstance().sub(detailAccount.getInvarymny(), detailAccount.getOutvarymny());

    varyMny = IATool.getInstance().sub(detailAccount.getAbvarymny(), varyMny);
    vo.setBeginVaryMny(varyMny);

    return vo;
  }
}