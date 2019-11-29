package nc.impl.bh;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.bh.bd.CustBasDocDAO;
import nc.bs.bh.bd.DivideCustBasDAO;
import nc.bs.bh.pub.PubDAO;
import nc.bs.bh.pub.SaleInvoiceDMO;
import nc.bs.bh.pub.StatusDAO;
import nc.bs.dao.BaseDAO;
import nc.bs.util.PropertiesUtils;
import nc.itf.bh.INcBhItf;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.vo.bd.b08.CubasdocVO;
import nc.vo.bd.b08.CustAddrVO;
import nc.vo.bd.b08.CustBankVO;
import nc.vo.bd.b08.CustBasVO;
import nc.vo.cu.SteelCubasdocVO;
import nc.vo.cu.SteelCustAddrVO;
import nc.vo.cu.SteelCustBankVO;
import nc.vo.cu.SteelCustMultiBillVO;
import nc.vo.pub.BusinessException;
import org.springframework.beans.BeanUtils;

public class NcBhImpl
  implements INcBhItf
{
  public String QueryStatus()
    throws RemoteException
  {
    try
    {
      return new StatusDAO().queryStatus();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException(e.getMessage());
    }
  }

  public String updateTaxNoImport(String[][] TaxInfo) throws RemoteException {
    String results = "";
    try {
      SaleInvoiceDMO dmo = new SaleInvoiceDMO();
      results = dmo.updateTaxNoImport(TaxInfo);
    } catch (Exception e) {
      throw new RemoteException("Remote Call", e);
    }
    return results;
  }

  public String updateInvoiceNo(String invoicePK, String jsNo)
    throws RemoteException
  {
    try
    {
      new StatusDAO().updateInvoiceNo(invoicePK, jsNo);
    } catch (Exception e) {
      throw new RemoteException("Remote Call", e);
    }

    return null;
  }

  public String insertCust(SteelCustMultiBillVO vo)
    throws RemoteException
  {
    return save(vo);
  }

  public static synchronized String save(SteelCustMultiBillVO vo)
    throws RemoteException
  {
    String key = "";
    try {
      SteelCubasdocVO steelCubasdocVO = (SteelCubasdocVO)vo
        .getParentVO();

      String custCode = getAutoCustCodeByAre(steelCubasdocVO
        .getPk_areacl());
      if (custCode != null)
        steelCubasdocVO.setCustcode(custCode);
      else {
        throw new BusinessException("生成客商编码出错!");
      }

      String code = getAutoPlatCustCodeByAre();
      if (code != null)
        steelCubasdocVO.setDef1(code);
      else {
        throw new BusinessException("生成集团统一流水号出错!");
      }

      CubasdocVO cubasdocVO = new CubasdocVO();
      CustBasVO header = new CustBasVO();
      BeanUtils.copyProperties(steelCubasdocVO, header);
      header.setPk_corp("0001");
      cubasdocVO.setParentVO(header);

      SteelCustBankVO[] steelCustBankVO = (SteelCustBankVO[])vo
        .getTableVO("BANK");
      CustBankVO[] banks;
      if (steelCustBankVO != null) {
        banks = new CustBankVO[steelCustBankVO.length];
        for (int i = 0; i < steelCustBankVO.length; ++i) {
          CustBankVO custBankVO = new CustBankVO();
          BeanUtils.copyProperties(steelCustBankVO[i], custBankVO);
          custBankVO.setPk_corp("0001");

          custBankVO.setMemo(steelCustBankVO[i].getBankowner());
          banks[i] = custBankVO;
        }
        cubasdocVO.setCustBankVOs(banks);
      } else {
        banks = new CustBankVO[0];
        cubasdocVO.setCustBankVOs(banks);
      }

      SteelCustAddrVO[] steelCustAddrVO = (SteelCustAddrVO[])vo
        .getTableVO("ADDR");
      CustAddrVO[] items;
      if (steelCustAddrVO != null) {
        items = new CustAddrVO[steelCustAddrVO.length];
        for (int i = 0; i < steelCustAddrVO.length; ++i) {
          CustAddrVO custAddrVO = new CustAddrVO();
          BeanUtils.copyProperties(steelCustAddrVO[i], custAddrVO);
          items[i] = custAddrVO;
        }
        cubasdocVO.setCustAddrVOs(items);
      } else {
        items = new CustAddrVO[0];
        cubasdocVO.setCustAddrVOs(items);
      }

      CustBasDocDAO dmo = new CustBasDocDAO();
      key = dmo.insert(cubasdocVO, false, null);
      CacheProxy.fireDataInserted("bd_cubasdoc", null);
      CacheProxy.fireDataInserted("bd_custaddr", null);
      CacheProxy.fireDataInserted("bd_custbank", null);

      String sql = "update steel_cubasdoc set authflag=1,custcode='" + 
        steelCubasdocVO.getCustcode() + "' where pk_cubasdoc='" + 
        steelCubasdocVO.getPk_cubasdoc() + "'";
      PubDAO pubDAO = new PubDAO();
      pubDAO.getIfBaseDao().executeUpdate(sql);
      DivideCustBasDAO divideCustBasDAO = new DivideCustBasDAO();
      steelCubasdocVO.setPk_cubasdoc(key);
      divideCustBasDAO.setCumanDoc(steelCubasdocVO);
    } catch (Exception e) {
      throw new RemoteException("Remote Call", e);
    }
    return key;
  }

  private static String getAutoCustCodeByAre(String pk)
    throws Exception
  {
    String arecode = getAreCode(pk);

    String sql = "select max( custcode ) from bd_cubasdoc t where nvl(t.dr,0)=0 and t.custcode like '" + 
      arecode + "%'  and t.pk_areacl='" + pk + "'";
    Object[] ob = (Object[])new BaseDAO().executeQuery(sql, 
      new ArrayProcessor());
    if ((ob == null) || (ob.length == 0) || (ob[0] == null))
    {
      String r = arecode + "000000000";
      return r.substring(0, 8) + 1;
    }

    try
    {
      BigDecimal dec = new BigDecimal(ob[0].toString());
      return dec.add(new BigDecimal("1")).toString();
    }
    catch (NumberFormatException ex) {
      throw new Exception("生成客商编码出错：发现客商基本档中存在不合法客商编码：[" + ob[0].toString() + "]含有非数字型字符,计算客商编码失败，请去掉该客商编码中的非数字型字符，重新审批");
    }
  }

  private static String getAreCode(String pk)
    throws BusinessException
  {
    String sql = "select a.areaclcode from bd_areacl a where nvl(a.dr,0)=0 and  a.pk_areacl='" + 
      pk + "' ";
    Object[] o = (Object[])new BaseDAO().executeQuery(sql, 
      new ArrayProcessor());
    if ((o == null) || (o.length == 0))
      throw new BusinessException("没有找到对应的地区分类！");
    return o[0].toString();
  }

  private static String getAutoPlatCustCodeByAre()
    throws Exception
  {
    String tag = PropertiesUtils.getInstance().getString("tag", "S");

    String sql = "select max( t.def1 ) from bd_cubasdoc t where nvl(t.dr,0)=0 ";

    Object[] ob = (Object[])new BaseDAO().executeQuery(sql, 
      new ArrayProcessor());
    if ((ob != null) && (ob.length != 0) && (ob[0] != null) && (ob[0].toString().trim().length() != 0))
    {
      String r = ob[0].toString();
      r = r.substring(1);
      int code = Integer.parseInt(r) + 1;
      r = "00000" + code;
      return tag + r.substring(r.length() - 5);
    }

    return tag + "00001";
  }
}