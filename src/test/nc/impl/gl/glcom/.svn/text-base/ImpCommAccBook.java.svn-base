package nc.impl.gl.glcom;

import nc.bs.gl.accbook.SubjAssembleQueryVoucherBO;
import nc.bs.glcom.balance.GlBalanceBO;
import nc.itf.gl.pub.ICommAccBookPub;
import nc.vo.bd.b02.AccsubjVO;
import nc.vo.gl.balancebooks.BalanceResultVO;
import nc.vo.gl.detailbooks.DetailResultVO;
import nc.vo.glcom.balance.GlBalanceVO;
import nc.vo.glcom.balance.GlDetailVO;
import nc.vo.glcom.balance.GlQueryVO;
import nc.vo.glcom.balance.SubjAssembleQryVO;
import nc.vo.glcom.balance.SubjAssembleVO;
import nc.vo.glcom.exception.GLBusinessException;
import nc.vo.glcom.glperiod.GlPeriodVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public class ImpCommAccBook
  implements ICommAccBookPub
{
  public GlBalanceVO[] getBeginBalance(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getBeginBalance(arg0);
  }

  public GlDetailVO[] getDetailBook(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getDetailBook(arg0);
  }

  public GlBalanceVO[] getEndBalance(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getEndBalance(arg0);
  }

  public GlBalanceVO[] getOccur(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getOccur(arg0);
  }

  public GlBalanceVO[] getAccountDetail(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getAccountDetail(arg0);
  }

  public SubjAssembleVO[] getAssembleFromVoucher(SubjAssembleQryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getAssembleFromVoucher(arg0);
  }

  public BalanceResultVO getBalance(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getBalance(arg0);
  }

  public AccsubjVO[] getCarryOverSubjs(String arg0, String arg1, String arg2)
    throws GLBusinessException
  {
    return new GlBalanceBO().getCarryOverSubjs(arg0, arg1, arg2);
  }

  public BalanceResultVO getDayBalance(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getDayBalance(arg0);
  }

  public DetailResultVO getDetailBooksData(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getDetailBooksData(arg0);
  }

  public GlPeriodVO getGlPeriod(UFDate arg0)
    throws GLBusinessException
  {
    return null;
  }

  public GlBalanceVO[] getOccurByRtVoucherPk(String[] arg0, String[] arg1, String arg2, int[] arg3)
    throws GLBusinessException
  {
    return new GlBalanceBO().getOccurByRtVoucherPk(arg0, arg1, arg2, arg3);
  }

  public GlBalanceVO[] getOccurByVoucherPk(String[] arg0, String[] arg1, String arg2, int[] arg3)
    throws GLBusinessException
  {
    return new GlBalanceBO().getOccurByVoucherPk(arg0, arg1, arg2, arg3);
  }

  public String[][] getOccurSubjs(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getOccurSubjs(arg0);
  }

  public GlBalanceVO[] getSumBigAmount(GlQueryVO arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().getSumBigAmount(arg0);
  }

  public String[] isBalanceDetailEqual(String arg0, String arg1, String arg2)
    throws GLBusinessException
  {
    return new GlBalanceBO().isBalanceDetailEqual(arg0, arg1, arg2);
  }

  public Boolean recalculateBalance(String arg0)
    throws GLBusinessException
  {
    return new GlBalanceBO().recalculateBalance(arg0);
  }

  public String[] getVoucherScope(SubjAssembleQryVO arg0)
    throws BusinessException
  {
    return new SubjAssembleQueryVoucherBO().getVoucherScope(arg0);
  }
}