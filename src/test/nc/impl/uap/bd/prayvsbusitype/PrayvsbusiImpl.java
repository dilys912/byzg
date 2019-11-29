package nc.impl.uap.bd.prayvsbusitype;

import java.sql.SQLException;
import javax.naming.NamingException;
import nc.bs.bd.b999.PrayvsbusiDMO;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.SystemException;
import nc.bs.uap.bd.BDRuntimeException;
import nc.itf.uap.bd.prayvsbusitype.IPrayvsBusiQry;
import nc.vo.bd.MultiLangTrans;
import nc.vo.bd.b999.PrayvsbusiVO;
import nc.vo.pub.BusinessException;

public class PrayvsbusiImpl
  implements IPrayvsBusiQry
{
  public PrayvsbusiVO[] queryByPrayvsBusitypeVO(PrayvsbusiVO condPrayvsbusiVO, Boolean isAnd)
    throws BusinessException
  {
    PrayvsbusiVO[] prayvsbusis = null;
    try {
      PrayvsbusiDMO dmo = new PrayvsbusiDMO();
      prayvsbusis = dmo.queryByVO(condPrayvsbusiVO, isAnd);
    }
    catch (SystemException e)
    {
      e.printStackTrace();
      throw new BDRuntimeException(e.getMessage(), e);
    } catch (NamingException e) {
      e.printStackTrace();
      throw new BDRuntimeException(e.getMessage(), e);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new BusinessException(e.getMessage(), e);
    }
    return prayvsbusis;
  }

  public Object[] getBusitypeDrp1(String pk_corp)
    throws BusinessException
  {
    Object[] types = null;
    try {
      PrayvsbusiDMO dmo = new PrayvsbusiDMO();
      types = dmo.getBusitypeDrp1(pk_corp);
    }
    catch (SystemException e) {
      e.printStackTrace();
      throw new BDRuntimeException(e.getMessage(), e);
    } catch (NamingException e) {
      e.printStackTrace();
      throw new BDRuntimeException(e.getMessage(), e);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new BusinessException(MultiLangTrans.getTransStr("MD4", new String[] { NCLangResOnserver.getInstance().getStrByID("10082010", "UC001-0000003") }), e);
    }

    return types;
  }

  public String getBusitypeProd(int praytype, String pk_corp)
    throws BusinessException
  {
    String busitype = null;
    try {
      PrayvsbusiDMO dmo = new PrayvsbusiDMO();
      busitype = dmo.getBusitypeProd(praytype, pk_corp);
    }
    catch (SystemException e) {
      e.printStackTrace();
      throw new BDRuntimeException(e.getMessage(), e);
    } catch (NamingException e) {
      e.printStackTrace();
      throw new BDRuntimeException(e.getMessage(), e);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new BusinessException(MultiLangTrans.getTransStr("MD4", new String[] { NCLangResOnserver.getInstance().getStrByID("10082010", "UC001-0000003") }), e);
    }

    return busitype;
  }

  public Object[] getPrayAndBusiType(String pk_corp, String cpraysource)
    throws BusinessException
  {
    Object[] busitypes = null;
    try {
      PrayvsbusiDMO dmo = new PrayvsbusiDMO();
      busitypes = dmo.getPrayAndBusiType(pk_corp, cpraysource);
    }
    catch (SystemException e) {
      e.printStackTrace();
      throw new BDRuntimeException(e.getMessage(), e);
    }
    catch (NamingException e) {
      e.printStackTrace();
      throw new BDRuntimeException(e.getMessage(), e);
    } catch (SQLException e) {
      throw new BusinessException(e.getMessage(), e);
    }
    return busitypes;
  }
}