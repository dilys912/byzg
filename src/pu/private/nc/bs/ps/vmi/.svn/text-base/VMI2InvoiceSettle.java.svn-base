package nc.bs.ps.vmi;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.pu.pub.PubDMO;
import nc.vo.ic.pub.vmi.VmiSumHeaderVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.ps.settle.IinvoiceVO;
import nc.vo.ps.settle.SettlebillHeaderVO;
import nc.vo.ps.settle.SettlebillItemVO;
import nc.vo.ps.settle.SettlebillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import net.sf.jsqlparser.expression.StringValue;


public class VMI2InvoiceSettle {
  private String m_accountYear = null;
  private UFDate m_currentDate = null;
  private String m_operatorID = null;
  private String m_unitCode = null;
  private String m_settlebillCode = null;

  private IinvoiceVO m_invoiceVOs[] = null;
  private VmiSumHeaderVO m_vmiVOs[] = null;
  private SettlebillItemVO m_settlebillItemVOs[] = null;
  private Hashtable m_hashVMI = null;//key cinvoice_bid,value m_vmiVOs[i]

  public VMI2InvoiceSettle(String accountYear,UFDate dCurrDate,String pk_corp,String cOperator,String vBillCode){
    m_accountYear = accountYear;
    m_currentDate = dCurrDate;
    m_operatorID = cOperator;
    m_unitCode = pk_corp;
    m_settlebillCode = vBillCode;
  }
  
  private void initInvoice(InvoiceVO invoiceVO){
    Vector v=new Vector();

    InvoiceHeaderVO header=invoiceVO.getHeadVO();
    InvoiceItemVO items[]=invoiceVO.getBodyVO();
    for(int i=0;i<items.length;i++){
      IinvoiceVO vo=new IinvoiceVO();
      vo.setCdeptid(header.getCdeptid());
      vo.setCoperatorid(header.getCoperator());
      vo.setCvendormangid(header.getCvendormangid());
      vo.setCvendorbaseid(header.getCvendorbaseid());
      vo.setPk_corp(header.getPk_corp());
      vo.setVinvoicecode(header.getVinvoicecode());
      vo.setCinvoice_bid(items[i].getCinvoice_bid());
      vo.setCinvoiceid(items[i].getCinvoiceid());
      vo.setCmangid(items[i].getCmangid());
      vo.setCbaseid(items[i].getCbaseid());
      
      vo.setCupsourcebillid(items[i].getCupsourcebillid());
      vo.setCupsourcebillrowid(items[i].getCupsourcebillrowid());
      vo.setCupsourcebilltype(items[i].getCupsourcebilltype());
      vo.setCsourcebillhid(items[i].getCsourcebillid());
      vo.setCsourcebillbid(items[i].getCsourcebillrowid());
      vo.setCsourcebilltype(items[i].getCsourcebilltype());

      UFDouble d=items[i].getNinvoicenum();
      if(d==null || d.toString().trim().length()==0) d=new UFDouble(0.0);
      vo.setNinvoicenum(d);
      d=items[i].getNmoney();
      if(d==null || d.toString().trim().length()==0) d=new UFDouble(0.0);
      vo.setNmoney(d);

      //发票有可能已部分结算
      UFDouble dd = items[i].getNaccumsettnum();
      if(dd == null || dd.toString().trim().length() == 0) d = new UFDouble(0);
      vo.setNaccumsettlenum(dd);
      vo.setNnosettlenum(new UFDouble(vo.getNinvoicenum().doubleValue() - vo.getNaccumsettlenum().doubleValue()));
      dd = items[i].getNaccumsettmny();
      if(dd == null || dd.toString().trim().length() == 0) d = new UFDouble(0);
      vo.setNaccumsettlemny(dd);
      vo.setNnosettlemny(new UFDouble(vo.getNmoney().doubleValue() - vo.getNaccumsettlemny().doubleValue()));
      
      vo.setNreasonwastenum(items[i].getNreasonwastenum());
      if(items[i].getNinvoicenum() != null && items[i].getNmoney() != null && Math.abs(items[i].getNinvoicenum().doubleValue()) > 0){
        vo.setNprice(items[i].getNmoney().div(items[i].getNinvoicenum()));
      }

      /*如果结算完毕，则不再结算 CZP 2004-06-14 ADD*/
      if (Math.abs(vo.getNnosettlemny().doubleValue()) > 0){
        v.addElement(vo);
      }
    }
    if (v.size() > 0){
      m_invoiceVOs=new IinvoiceVO[v.size()];
      v.copyInto(m_invoiceVOs);
    }else{
      m_invoiceVOs = null;
    }   
  }
  
  public Vector doBalance(InvoiceVO invoiceVO) throws BusinessException{
    initInvoice(invoiceVO);
    
    if(m_invoiceVOs == null || m_invoiceVOs.length == 0) return null;
    
    Vector vTemp = new Vector();
    for(int i = 0; i < m_invoiceVOs.length; i++) vTemp.addElement(m_invoiceVOs[i].getCupsourcebillid());
    String keys[] = new String[vTemp.size()];
    vTemp.copyInto(keys);
    try{
      m_vmiVOs = new VMIDMO().queryVMIHeadersByKeys(keys);
     }catch(Exception e){
      PubDMO.throwBusinessException(e);
    }
    
    if(m_vmiVOs == null || m_vmiVOs.length == 0) return null;
    vTemp = new Vector();
    for(int i = 0; i < m_vmiVOs.length; i++){
      if(m_vmiVOs[i].getBsettleendflag() == null || !m_vmiVOs[i].getBsettleendflag().booleanValue()) vTemp.addElement(m_vmiVOs[i]);
    }
    m_vmiVOs = new VmiSumHeaderVO[vTemp.size()];
    vTemp.copyInto(m_vmiVOs);
    if(m_vmiVOs == null || m_vmiVOs.length == 0) return null;
    if(m_invoiceVOs.length != m_vmiVOs.length) return null;
    
    //m_invoiceVOs, m_vmiVOs的匹配
    m_hashVMI = new Hashtable();
    for(int i = 0; i < m_invoiceVOs.length; i++){
      for(int j = 0; j < m_vmiVOs.length; j++){
        if(m_invoiceVOs[i].getCupsourcebillid().equals(m_vmiVOs[j].getCvmihid()) && !m_hashVMI.containsKey(m_invoiceVOs[i].getCinvoice_bid())){
          m_hashVMI.put(m_invoiceVOs[i].getCinvoice_bid(), m_vmiVOs[j]);
          break;
        }
      }
    }
    
    //设置表体
    vTemp = new Vector();
    SettlebillItemVO body[] = new SettlebillItemVO[m_invoiceVOs.length];
    VmiSumHeaderVO vmiVO = null;
    for (int i = 0; i < body.length; i++) {
      if(m_hashVMI.get(m_invoiceVOs[i].getCinvoice_bid()) != null){
        vmiVO = (VmiSumHeaderVO)m_hashVMI.get(m_invoiceVOs[i].getCinvoice_bid());
      }else{
        vmiVO = null;
      }
      body[i] = new SettlebillItemVO();
      body[i].setPk_corp(m_unitCode);
      body[i].setCmangid(m_invoiceVOs[i].getCmangid());
      body[i].setCbaseid(m_invoiceVOs[i].getCbaseid());
      
      if(vmiVO != null) body[i].setCvmiid(vmiVO.getCvmihid());
      
      body[i].setCinvoiceid(m_invoiceVOs[i].getCinvoiceid());
      body[i].setCinvoice_bid(m_invoiceVOs[i].getCinvoice_bid());
      if(vmiVO != null) body[i].setVbillcode(vmiVO.getVbillcode());
      body[i].setVinvoicecode(m_invoiceVOs[i].getVinvoicecode());

      //结算数量为发票未结算数量
      body[i].setNsettlenum(m_invoiceVOs[i].getNnosettlenum());
          
      //计算结算金额=发票结算金额=发票本币单价*(发票数量)
      UFDouble d = m_invoiceVOs[i].getNnosettlenum();
      if(d != null){
        if(d.equals(m_invoiceVOs[i].getNnosettlenum())){
          //如果本次结算数量=发票未结算数量,结算金额=发票未结算金额;否则,结算金额=发票单价*发票数量
          body[i].setNmoney(m_invoiceVOs[i].getNnosettlemny());
        }else if(m_invoiceVOs[i].getNprice() != null){
          body[i].setNmoney(m_invoiceVOs[i].getNprice().multiply(d));
        }       
       }
      
      //结算单价=结算金额/结算数量
      if(body[i].getNmoney() != null && body[i].getNsettlenum() != null && Math.abs(body[i].getNsettlenum().doubleValue()) > 0){
        body[i].setNprice(body[i].getNmoney().div(body[i].getNsettlenum()));
      }

      //计算暂估金额
      if(body[i].getNsettlenum() != null && vmiVO != null && vmiVO.getNprice() != null){
        body[i].setNgaugemny(body[i].getNsettlenum().multiply(vmiVO.getNprice()));
       }
          
      vTemp.addElement(body[i]);
    }

    if(vTemp.size() == 0) return null;
    m_settlebillItemVOs = new SettlebillItemVO[vTemp.size()];
    vTemp.copyInto(m_settlebillItemVOs);

    //修改VMI和发票数据的累计结算数量和累计结算金额
    SettlebillVO settlebillVO = null;
    try {
      settlebillVO = doModification();
    } catch (Exception e) {
      PubDMO.throwBusinessException(e);
    }
    
    if (settlebillVO != null) {
      Vector vReturn = new Vector();
      vReturn.addElement(m_invoiceVOs);
      vReturn.addElement(m_vmiVOs);
      vReturn.addElement(settlebillVO);
      return vReturn;
    } else return null;
  }
  
  private SettlebillVO doModification() throws nc.vo.pub.BusinessException{
    VmiSumHeaderVO vmiVO = null;
    try{
      for (int i = 0; i < m_invoiceVOs.length; i++) {
        if(m_hashVMI.get(m_invoiceVOs[i].getCinvoice_bid()) != null){
          vmiVO = (VmiSumHeaderVO)m_hashVMI.get(m_invoiceVOs[i].getCinvoice_bid());
        }else{
          vmiVO = null;
        }

        m_invoiceVOs[i].setNaccumsettlenum(m_invoiceVOs[i].getNinvoicenum());
        m_invoiceVOs[i].setNaccumsettlemny(m_invoiceVOs[i].getNmoney());
        double d = 0;
        if(vmiVO != null && vmiVO.getNaccountmny() != null) d = vmiVO.getNaccountnum().doubleValue();
        d += m_settlebillItemVOs[i].getNsettlenum().doubleValue();
        
        //比较VMI的累计结算数量和出库数量
        //ncm begin zhujha 201311291430099855_深圳南玻_发票参照消耗汇总报累计结算数量错误
//        if (Math.abs(d) > Math.abs(
//            PuPubVO.getUFDouble_NullAsZero(vmiVO.getNoutnum()).sub(
//                PuPubVO.getUFDouble_NullAsZero(vmiVO.getNoutinnum())).doubleValue()) + VMIDMO.getDigitRMB(m_unitCode).doubleValue()) {
        if (Math.abs(d) > Math.abs(PuPubVO.getUFDouble_NullAsZero(vmiVO.getNoutnum()).sub(PuPubVO.getUFDouble_NullAsZero(vmiVO.getNoutinnum())).doubleValue())) {
        	//ncm end zhujha 201311291430099855_深圳南玻_发票参照消耗汇总报累计结算数量错误
        //  throw new nc.vo.pub.BusinessException("累计结算数量大于出库数量!");               add by yqq 2016-12-28  加个提示
          throw new nc.vo.pub.BusinessException("第"+(i+1)+"行"+"累计结算数量大于出库数量!");

        }
        if(vmiVO != null) vmiVO.setNaccountnum(new UFDouble(d));
    
        d = 0;
        if(vmiVO != null && vmiVO.getNaccountmny() != null) d = vmiVO.getNaccountmny().doubleValue();
        //获得暂估金额
        if(vmiVO != null && vmiVO.getNmoney() != null) d+= vmiVO.getNmoney().doubleValue();
        //设置VMI的累计结算金额
        if(vmiVO != null) vmiVO.setNaccountmny(new UFDouble(d));
        
        for(int j = 0; j < m_vmiVOs.length; j++){
          if(vmiVO != null && vmiVO.getCvmihid().equals(m_vmiVOs[j].getCvmihid())){
            m_vmiVOs[j] = vmiVO;
            break;
          }
        }
      }
    }catch(Exception e){
      throw new nc.vo.pub.BusinessException(e.getMessage());
    }

    //设置表头
    SettlebillHeaderVO head = new SettlebillHeaderVO();
    head.setPk_corp(m_unitCode);
    head.setCaccountyear(m_accountYear);
    head.setDsettledate(m_currentDate);
    head.setIbillstatus(new Integer(0));
    head.setCbilltype(ScmConst.PO_SettleBill);
    head.setCoperator(m_operatorID);
    head.setTmaketime((new UFDateTime(new Date())).toString());
    //生成结算单号
    head.setVsettlebillcode(m_settlebillCode);

    SettlebillVO settlebillVO = new SettlebillVO(m_settlebillItemVOs.length);
    settlebillVO.setParentVO(head);
    settlebillVO.setChildrenVO(m_settlebillItemVOs);

    return settlebillVO;
 }
}
