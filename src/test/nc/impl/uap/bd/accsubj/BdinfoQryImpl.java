package nc.impl.uap.bd.accsubj;

import nc.bs.bd.b02.BdinfoDAO;
import nc.itf.uap.bd.accsubj.IBdinfoQry;
import nc.vo.bd.b02.BdinfoVO;
import nc.vo.pub.BusinessException;

public class BdinfoQryImpl
  implements IBdinfoQry
{
  private BdinfoDAO dao;

  public BdinfoVO findSubjBdinfoVOByPrimaryKey(String key)
    throws BusinessException
  {
    return getBaseDao().findByPrimaryKey(key);
  }

  public String insert(BdinfoVO bdinfo)
    throws BusinessException
  {
    return getBaseDao().insert(bdinfo);
  }

  public String[] insertArray(BdinfoVO[] bdinfos)
    throws BusinessException
  {
    return getBaseDao().insertArray(bdinfos);
  }

  public void delete(BdinfoVO vo)
    throws BusinessException
  {
    getBaseDao().delete(vo);
  }

  public void update(BdinfoVO bdinfo)
    throws BusinessException
  {
    getBaseDao().update(bdinfo);
  }

  public BdinfoVO[] querySubjBdinfoVos(String pk_corp)
    throws BusinessException
  {
    return getBaseDao().queryAll(pk_corp, true);
  }

  public BdinfoVO[] queryBySubjBdinfoVOs(BdinfoVO condBdinfoVO, Boolean isAnd)
    throws BusinessException
  {
    return getBaseDao().queryByVO(condBdinfoVO, isAnd);
  }

  public BdinfoVO[] querySubjBdinfoVos(String pk_corp, String book)
    throws BusinessException
  {
    return getBaseDao().queryAll(pk_corp, false);
  }

  public BdinfoVO[] queryBySubjBdinfoVOs(BdinfoVO condBdinfoVO, Boolean isAnd, String pk_corp)
    throws BusinessException
  {
    return getBaseDao().queryByVO(condBdinfoVO, isAnd, pk_corp);
  }

  private BdinfoDAO getBaseDao()
  {
    if (this.dao == null) {
      this.dao = new BdinfoDAO();
    }
    return this.dao;
  }

  public BdinfoVO[] querySubjBdinfoDetail(String[] pk_bdinfos)
    throws BusinessException
  {
    String pk_ins = "";
    int len = pk_bdinfos == null ? 0 : pk_bdinfos.length;
    for (int i = 0; i < len; i++) {
      if (i == len - 1)
        pk_ins = pk_ins + "'" + pk_bdinfos[i] + "' ";
      else {
        pk_ins = pk_ins + "'" + pk_bdinfos[i] + "',";
      }
    }

    return getBaseDao().queryDetail(pk_ins);
  }

  public BdinfoVO[] querySubjBdinfoVosByGlorgbook(String pk_glorgbook, String pk_subjscheme)
    throws BusinessException
  {
    return getBaseDao().queryAllByGlorgbook(pk_glorgbook, pk_subjscheme);
  }
}