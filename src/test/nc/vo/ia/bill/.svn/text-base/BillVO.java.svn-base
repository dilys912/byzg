package nc.vo.ia.bill;

import java.util.Arrays;
import nc.vo.ia.ia306.IOLVO;
import nc.vo.ia.pub.ConstVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pub.IBillCode;
import nc.vo.scm.pub.IscmDefCheckVO;

public class BillVO extends AggregatedValueObject
  implements IBillCode, IscmDefCheckVO
{
  private static final long serialVersionUID = 3911429639892148335L;
  private BillItemVO[] m_voChildren = null;

  private BillHeaderVO m_voParent = null;

  private String sWarningMsg = null;

  public BillVO()
  {
  }

  public CircularlyAccessibleValueObject[] getChildrenVO()
  {
    return this.m_voChildren;
  }

  public CircularlyAccessibleValueObject getParentVO()
  {
    return this.m_voParent;
  }

  public void setChildrenVO(CircularlyAccessibleValueObject[] voaChildren)
  {
    this.m_voChildren = ((BillItemVO[])(BillItemVO[])voaChildren);
  }

  public void setParentVO(CircularlyAccessibleValueObject voParent)
  {
    this.m_voParent = ((BillHeaderVO)voParent);
  }

  public BillVO(int iRowCount)
  {
    this.m_voParent = new BillHeaderVO();
    this.m_voChildren = new BillItemVO[iRowCount];
    for (int i = 0; i < iRowCount; i++)
      this.m_voChildren[i] = new BillItemVO();
  }

  public Object clone()
    throws CloneNotSupportedException
  {
    BillVO bill = new BillVO();

    if ((this.m_voParent != null) && (this.m_voChildren != null)) {
      BillHeaderVO bhvo = (BillHeaderVO)this.m_voParent.clone();
      BillItemVO[] btvos = new BillItemVO[this.m_voChildren.length];

      for (int i = 0; i < btvos.length; i++) {
        btvos[i] = ((BillItemVO)this.m_voChildren[i].clone());
      }

      bill.setParentVO(bhvo);
      bill.setChildrenVO(btvos);
    }

    return bill;
  }

  public String getSWarningMsg() {
    return this.sWarningMsg;
  }

  public void setSWarningMsg(String newSWarningMsg)
  {
    this.sWarningMsg = newSWarningMsg;
  }

  public IOLVO[] changeToView()
  {
    IOLVO[] bills = new IOLVO[this.m_voChildren.length];

    for (int i = 0; i < this.m_voChildren.length; i++) {
      IOLVO bill = new IOLVO();

      BillItemVO billitem = this.m_voChildren[i];

      bill.setCbillid(this.m_voParent.getCbillid());

      bill.setPk_corp(this.m_voParent.getPk_corp());

      bill.setCbilltypecode(this.m_voParent.getCbilltypecode());

      bill.setVbillcode(this.m_voParent.getVbillcode());

      bill.setCaccountyear(this.m_voParent.getCaccountyear());

      bill.setCaccountmonth(this.m_voParent.getCaccountmonth());

      bill.setDbilldate(this.m_voParent.getDbilldate());

      bill.setCsourcemodulename(this.m_voParent.getCsourcemodulename());

      bill.setFdispatchflag(this.m_voParent.getFdispatchflag());

      bill.setCdispatchid(this.m_voParent.getCdispatchid());

      bill.setCbiztypeid(this.m_voParent.getCbiztypeid());

      bill.setCbusinessbillid(this.m_voParent.getCbusinessbillid());

      bill.setCrdcenterid(this.m_voParent.getCrdcenterid());

      bill.setCwarehouseid(this.m_voParent.getCwarehouseid());

      bill.setCdeptid(this.m_voParent.getCdeptid());

      bill.setCemployeeid(this.m_voParent.getCemployeeid());

      bill.setCcustomvendorid(this.m_voParent.getCcustomvendorid());

      bill.setVnote(this.m_voParent.getVnote());

      bill.setVadjpricefilecode(this.m_voParent.getVadjpricefilecode());

      bill.setBestimateflag(this.m_voParent.getBestimateflag());

      bill.setBwithdrawalflag(this.m_voParent.getBwithdrawalflag());

      bill.setVcheckbillcode(this.m_voParent.getVcheckbillcode());

      bill.setDcheckdate(this.m_voParent.getDcheckdate());

      bill.setCagentid(this.m_voParent.getCagentid());

      bill.setCwarehousemanagerid(this.m_voParent.getCwarehousemanagerid());

      bill.setCoperatorid(this.m_voParent.getCoperatorid());

      if (this.m_voParent.getBauditedflag() != null) {
        bill.setBauditedflag(this.m_voParent.getBauditedflag());
      }

      bill.setBdisableflag(this.m_voParent.getBdisableflag());

      bill.setVdef1(this.m_voParent.getVdef1());

      bill.setVdef2(this.m_voParent.getVdef2());

      bill.setVdef3(this.m_voParent.getVdef3());

      bill.setVdef4(this.m_voParent.getVdef4());

      bill.setVdef5(this.m_voParent.getVdef5());

      bill.setVdef6(this.m_voParent.getVdef6());

      bill.setVdef7(this.m_voParent.getVdef7());

      bill.setVdef8(this.m_voParent.getVdef8());

      bill.setVdef9(this.m_voParent.getVdef9());

      bill.setVdef10(this.m_voParent.getVdef10());

      bill.setVdef11(this.m_voParent.getVdef11());

      bill.setVdef12(this.m_voParent.getVdef12());

      bill.setVdef13(this.m_voParent.getVdef13());

      bill.setVdef14(this.m_voParent.getVdef14());

      bill.setVdef15(this.m_voParent.getVdef15());

      bill.setVdef16(this.m_voParent.getVdef16());

      bill.setVdef17(this.m_voParent.getVdef17());

      bill.setVdef18(this.m_voParent.getVdef18());

      bill.setVdef19(this.m_voParent.getVdef19());

      bill.setVdef20(this.m_voParent.getVdef20());

      bill.setNcost(this.m_voParent.getNcost());

      bill.setPk_corp(billitem.getPk_corp());

      bill.setCbillid(billitem.getCbillid());

      bill.setCbill_bid(billitem.getCbill_bid());

      bill.setVbillcode(billitem.getVbillcode());

      bill.setCbilltypecode(billitem.getCbilltypecode());

      bill.setCsourcebilltypecode(billitem.getCsourcebilltypecode());

      bill.setVsourcebillcode(billitem.getVsourcebillcode());

      bill.setCsourcebillid(billitem.getCsourcebillid());

      bill.setCsourcebillitemid(billitem.getCsourcebillitemid());

      bill.setCsaleadviceid(billitem.getCsaleadviceid());

      bill.setCcsaleadviceitemid(billitem.getCcsaleadviceitemid());

      bill.setFpricemodeflag(billitem.getFpricemodeflag());

      bill.setCinventoryid(billitem.getCinventoryid());

      bill.setVbatch(billitem.getVbatch());

      bill.setCprojectid(billitem.getCprojectid());

      bill.setVfree1(billitem.getVfree1());

      bill.setVfree2(billitem.getVfree2());

      bill.setVfree3(billitem.getVfree3());

      bill.setVfree4(billitem.getVfree4());

      bill.setVfree5(billitem.getVfree5());

      bill.setNnumber(billitem.getNnumber());

      bill.setNprice(billitem.getNprice());

      bill.setNmoney(billitem.getNmoney());

      bill.setNsimulatemny(billitem.getNsimulatemny());

      bill.setNplanedprice(billitem.getNplanedprice());

      bill.setNplanedmny(billitem.getNplanedmny());

      bill.setNoriginalprice(billitem.getNoriginalprice());

      bill.setCinbillitemid(billitem.getCinbillitemid());

      bill.setNsettledsendnum(billitem.getNsettledsendnum());

      bill.setNsettledretractnum(billitem.getNsettledretractnum());

      bill.setDdrawrate(billitem.getDdrawrate());

      bill.setNdrawsummny(billitem.getNdrawsummny());

      bill.setNinvarymny(billitem.getNinvarymny());

      bill.setNoutvarymny(billitem.getNoutvarymny());

      bill.setCcrspbillitemid(billitem.getCcrspbillitemid());

      bill.setCadjustbillid(billitem.getCadjustbillid());

      bill.setCadjustbillitemid(billitem.getCadjustbillitemid());

      bill.setFoutadjustableflag(billitem.getFoutadjustableflag());

      bill.setBadjustedItemflag(billitem.getBadjustedItemflag());

      bill.setFdatagetmodelflag(billitem.getFdatagetmodelflag());

      bill.setCauditorid(billitem.getCauditorid());

      bill.setDauditdate(billitem.getDauditdate());

      bill.setIauditsequence(billitem.getIauditsequence());

      bill.setVbomcode(billitem.getVbomcode());

      bill.setVproducebatch(billitem.getVproducebatch());

      bill.setBdef1(billitem.getVdef1());

      bill.setBdef2(billitem.getVdef2());

      bill.setBdef3(billitem.getVdef3());

      bill.setBdef4(billitem.getVdef4());

      bill.setBdef5(billitem.getVdef5());

      bill.setBdef6(billitem.getVdef6());

      bill.setBdef7(billitem.getVdef7());

      bill.setBdef8(billitem.getVdef8());

      bill.setBdef9(billitem.getVdef9());

      bill.setBdef10(billitem.getVdef10());

      bill.setBdef11(billitem.getVdef11());

      bill.setBdef12(billitem.getVdef12());

      bill.setBdef13(billitem.getVdef13());

      bill.setBdef14(billitem.getVdef14());

      bill.setBdef15(billitem.getVdef15());

      bill.setBdef16(billitem.getVdef16());

      bill.setBdef17(billitem.getVdef17());

      bill.setBdef18(billitem.getVdef18());

      bill.setBdef19(billitem.getVdef19());

      bill.setBdef20(billitem.getVdef20());

      bill.setIrownumber(billitem.getIrownumber());

      bill.setCprojectphase(billitem.getCprojectphase());

      bill.setCastunitid(billitem.getCastunitid());

      bill.setNassistnum(billitem.getNassistnum());

      bill.setCfirstbilltypecode(billitem.getCfirstbilltypecode());

      bill.setCfirstbillid(billitem.getCfirstbillid());

      bill.setCfirstbillitemid(billitem.getCfirstbillitemid());

      bill.setNchangerate(billitem.getNchangerate());

      bill.setCwp(billitem.getCwp());

      bill.setBlargessflag(billitem.getBlargessflag());

      bill.setCinvclid(billitem.getCinvclid());

      bill.setCfacardid(billitem.getCfacardid());

      bill.setCfadeviceid(billitem.getCfadeviceid());

      bill.setCvoucherid(billitem.getCvoucherid());

      bill.setInvcode(billitem.getCinventorycode());
      bill.setInvname(billitem.getCinventoryname());
      bill.setInvtype(billitem.getCinventorytype());
      bill.setInvspec(billitem.getCinventoryspec());
      bill.setMeasname(billitem.getCinventorymeasname());

      bill.setCsaleaudititemid(billitem.getCsaleaudititemid());

      bill.setBauditbatchflag(billitem.getBauditbatchflag());

      bill.setBts(billitem.getTs());
      bills[i] = bill;
    }

    return bills;
  }

  public boolean equals(Object other)
  {
    if (other == null) {
      return false;
    }

    BillHeaderVO leftHeader = (BillHeaderVO)getParentVO();
    BillHeaderVO rightHeader = (BillHeaderVO)((BillVO)other).getParentVO();
    BillItemVO[] leftItems = (BillItemVO[])(BillItemVO[])getChildrenVO();
    BillItemVO[] rightItems = (BillItemVO[])(BillItemVO[])((BillVO)other).getChildrenVO();
    boolean result = false;

    if ((leftHeader != null) && (rightHeader != null)) {
      if (leftHeader.equals(rightHeader)) {
        result = true;
      }
      else {
        result = false;
      }
    }
    else if (((leftHeader != null) && (rightHeader == null)) || ((leftHeader == null) && (rightHeader != null)))
    {
      result = false;
    }
    else {
      result = true;
    }

    if (result == true) {
      if ((leftItems != null) && (rightItems != null))
      {
        result = Arrays.equals(leftItems, rightItems);
      }
      else if (((leftItems != null) && (rightItems == null)) || ((leftItems == null) && (rightItems != null)))
      {
        result = false;
      }
      else {
        result = true;
      }

    }

    return result;
  }

  public BillCodeObjValueVO getBillCodeObjVO()
  {
    BillCodeObjValueVO voBCOV = new BillCodeObjValueVO();
    if (this.m_voParent != null) {
      voBCOV.setAttributeValue("公司", this.m_voParent.getPk_corp() == null ? "" : this.m_voParent.getPk_corp());

      voBCOV.setAttributeValue("库存组织", this.m_voParent.getCrdcenterid() == null ? "" : this.m_voParent.getCrdcenterid());

      voBCOV.setAttributeValue("操作员", this.m_voParent.getCoperatorid() == null ? "" : this.m_voParent.getCoperatorid());

      voBCOV.setAttributeValue("制单日期", this.m_voParent.getDbilldate() == null ? "" : this.m_voParent.getDbilldate().toString());
    }

    return voBCOV;
  }

  public String getBillTypeCode()
  {
    if (this.m_voParent != null) {
      return this.m_voParent.getCbilltypecode();
    }

    return null;
  }

  public String getPk_corp()
  {
    if (this.m_voParent != null) {
      return this.m_voParent.getPk_corp();
    }

    return null;
  }

  public int getStatus()
  {
    if (this.m_voParent != null) {
      if ((this.m_voParent.getStatus() == 2) || (this.m_voParent.getCbillid() == null) || (this.m_voParent.getCbillid().trim().length() == 0))
      {
        return 2;
      }

      return -1;
    }

    return -1;
  }

  public String getVBillCode()
  {
    if (this.m_voParent != null) {
      return this.m_voParent.getVbillcode();
    }

    return null;
  }

  public void setPrimaryKey(String sPK)
  {
    try
    {
      if (this.m_voParent != null)
        this.m_voParent.setPrimaryKey(sPK);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void setStatus(int iStatus)
  {
    this.m_voParent.setStatus(iStatus);
  }

  public void setVBillCode(String vCode)
  {
    if (this.m_voParent != null) {
      this.m_voParent.setVbillcode(vCode);
    }
    if ((this.m_voChildren != null) && (this.m_voChildren.length != 0))
      for (int i = 0; i < this.m_voChildren.length; i++)
        if (this.m_voChildren[i] != null)
          this.m_voChildren[i].setVbillcode(vCode);
  }

  public void verify()
    throws ValidationException
  {
    if (this.m_voParent != null) {
      this.m_voParent.verify();
    }

    if (this.m_voChildren != null)
      for (int i = 0; i < this.m_voChildren.length; i++)
        this.m_voChildren[i].verify();
  }

  public Object[] getBodyDefValues(int iserial)
  {
    if (this.m_voChildren != null) {
      String[] DefValues = new String[this.m_voChildren.length];
      for (int i = 0; i < this.m_voChildren.length; i++)
        DefValues[i] = ((String)this.m_voChildren[i].getAttributeValue("vdef" + iserial));
      return DefValues;
    }
    return null;
  }

  public String getCbilltypecode()
  {
    if (this.m_voParent != null) {
      return this.m_voParent.getCbilltypecode();
    }
    return null;
  }

  public Object getHeadDefValue(int iserial)
  {
    if (this.m_voParent != null) {
      return this.m_voParent.getAttributeValue("vdef" + iserial);
    }
    return null;
  }

  public String getCbilltypedef() {
    return ConstVO.IADEF;
  }
}