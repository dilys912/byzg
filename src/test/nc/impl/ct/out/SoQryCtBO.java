package nc.impl.ct.out;

import java.util.ArrayList;
import java.util.Hashtable;
import nc.bs.scm.pub.bill.ScmImpl;
import nc.itf.ct.service.ICtToSo_QueryCt;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.ctpo.RetCtToPoQueryVO;
import nc.vo.scm.pub.SCMEnv;

public class SoQryCtBO extends ScmImpl
  implements ICtToSo_QueryCt
{
  public RetCtToPoQueryVO[][] queryForCntAll(String sPk_corp, String[] sInvIDs, String[] sCustIDs, UFDate date)
    throws BusinessException
  {
    ArrayList alInv = new ArrayList();
    RetCtToPoQueryVO[] retInvNullVO = null;
    RetCtToPoQueryVO[][] retVO = (RetCtToPoQueryVO[][])null;
    try
    {
      SoQryCtDMO soDmo = new SoQryCtDMO();
      alInv = soDmo.querySoCtInfoAllBatch(sPk_corp, sInvIDs, sCustIDs[0], date);

      retInvNullVO = soDmo.queryInvCtrlNullCnt(sPk_corp, sCustIDs[0], date);

      if ((retInvNullVO != null) && (retInvNullVO.length > 0)) {
        int itemp = alInv.size();
        retVO = new RetCtToPoQueryVO[itemp + 1][];
        for (int i = 0; i < itemp; i++)
          retVO[i] = ((RetCtToPoQueryVO[])(RetCtToPoQueryVO[])alInv.get(i));
        retVO[itemp] = retInvNullVO;
      } else {
        int itemp = alInv.size();
        retVO = new RetCtToPoQueryVO[itemp][];
        for (int i = 0; i < itemp; i++)
          retVO[i] = ((RetCtToPoQueryVO[])(RetCtToPoQueryVO[])alInv.get(i));
      }
    } catch (BusinessException e) {
      throw e;
    } catch (Exception e) {
      reportException(e);
      throw new BusinessException("Caused by: ", e);
    }
    return retVO;
  }

  protected void reportException(Exception e)
  {
    SCMEnv.out(e);
  }

  public Hashtable queryCtCodeAndNameByIDSql(String sCtIDSubSQL)
    throws BusinessException
  {
    if ((sCtIDSubSQL == null) || (sCtIDSubSQL.trim().length() == 0)) {
      return null;
    }
    Hashtable htReturn = null;
    try
    {
      SoQryCtDMO soDmo = new SoQryCtDMO();
      htReturn = soDmo.queryCtCodeAndNameByIDSql(sCtIDSubSQL);
    }
    catch (Exception e) {
      reportException(e);
      throw new BusinessException("Caused by: ", e);
    }

    return htReturn;
  }
}