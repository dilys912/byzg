package nc.vo.so.so002;

import java.util.Hashtable;
import java.util.Vector;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.IscmDefCheckVO;

public class SaleinvoiceVO extends AggregatedValueObject
  implements IscmDefCheckVO
{
  protected SaleVO headVO = null;

  protected SaleinvoiceBVO[] bodyVO = null;

  private int m_status = 0;
  long currentTimestamp;
  long initialTimestamp;
  private UFDate dClientDate = null;

  private String curuserid = null;

  private Hashtable hsSelectedARSubHVO = null;

  private UFBoolean bstrikeflag = null;

  private SaleinvoiceVO allinvoicevo = null;

  private Hashtable hsArsubAcct = null;

  private SaleinvoiceVO allSaleOrderVO = null;

  private int iAction = -1;

  private SaleinvoiceVO oldSaleOrderVO = null;

  private String operatorid = null;

  public SaleinvoiceVO()
  {
  }

  public static SaleinvoiceVO mergeCheck(SaleinvoiceVO[] vosNew, boolean bParaSO30, String sBusiType)
    throws BusinessException
  {
    String salecorpid0 = ((SaleVO)vosNew[0].getParentVO()).getCsalecorpid();

    String receiptcorpid0 = ((SaleVO)vosNew[0].getParentVO()).getCreceiptcorpid();

    SaleinvoiceBVO[] salebodyVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])vosNew[0].getChildrenVO();
    String currencyid0 = salebodyVO[0].getCcurrencytypeid();

    String calbodyid0 = ((SaleVO)vosNew[0].getParentVO()).getCcalbodyid();
    int j;
    if (vosNew.length > 1) {
      for (int i = 1; i < salebodyVO.length; ++i) {
        String currencyid = salebodyVO[i].getCcurrencytypeid();
        if (!(currencyid.trim().equals(currencyid0.trim()))) {
          throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000125"));
        }
      }

      for (int i = 1; i < vosNew.length; ++i) {
        String salecorpid = ((SaleVO)vosNew[i].getParentVO()).getCsalecorpid();
        String receiptcorpid = ((SaleVO)vosNew[i].getParentVO()).getCreceiptcorpid();

        String calbodyid = ((SaleVO)vosNew[i].getParentVO()).getCcalbodyid();

        if ((!(bParaSO30)) && 
          (!(receiptcorpid.trim().equals(receiptcorpid0.trim())))) {
          throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000114"));
        }

        if (!(salecorpid.trim().equals(salecorpid0.trim()))) {
          throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000115"));
        }

        salebodyVO = (SaleinvoiceBVO[])(SaleinvoiceBVO[])vosNew[i].getChildrenVO();
        for (j = 0; j < salebodyVO.length; ++j) {
          String currencyid = salebodyVO[j].getCcurrencytypeid();
          if (!(currencyid.trim().equals(currencyid0.trim()))) {
            throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000117"));
          }
        }
      }

    }

    SaleinvoiceVO voReturn = new SaleinvoiceVO();
    if (vosNew.length == 1) {
      voReturn = vosNew[0];
    } else {
      voReturn.setParentVO(vosNew[0].getParentVO());

      ((SaleVO)voReturn.getParentVO()).setCbiztype(sBusiType);
      Vector vBodys = new Vector();

      for (int i = 0; i < vosNew.length; ++i) {
        SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])vosNew[i].getChildrenVO();
        for (j = 0; j < items.length; ++j)
          vBodys.addElement(items[j]);
      }
      if (vBodys.size() > 0)
      {
        SaleinvoiceBVO[] bodyItems = new SaleinvoiceBVO[vBodys.size()];
        vBodys.copyInto(bodyItems);
        voReturn.setChildrenVO(bodyItems);
        ((SaleVO)voReturn.getParentVO()).setCcurrencyid(bodyItems[0].getCcurrencytypeid());
      }
    }

    return voReturn;
  }

  public SaleinvoiceVO(int bodynum)
  {
    this.headVO = new SaleVO();
    this.bodyVO = new SaleinvoiceBVO[bodynum];

    for (int i = 0; i < bodynum; ++i)
      this.bodyVO[i] = new SaleinvoiceBVO();
  }

  public SaleinvoiceVO(SaleVO headVO, SaleinvoiceBVO[] bodyVO)
  {
    this.headVO = headVO;
    this.bodyVO = bodyVO;
  }

  public SaleinvoiceVO(SaleVO headVO, Vector vbodyVO)
  {
    this.headVO = headVO;
    this.bodyVO = new SaleinvoiceBVO[vbodyVO.size()];
    for (int i = 0; i < vbodyVO.size(); ++i)
      this.bodyVO[i] = ((SaleinvoiceBVO)vbodyVO.elementAt(i));
  }

  public SaleinvoiceVO getAllinvoicevo()
  {
    return this.allinvoicevo;
  }

  public static SaleinvoiceVO convToOppVO(SaleinvoiceVO oldvo, UFDate date)
  {
    SaleinvoiceVO lastvo = new SaleinvoiceVO();
    SaleVO headold = (SaleVO)oldvo.getParentVO();
    SaleVO headnew = (SaleVO)headold.clone();
    lastvo.setParentVO(headnew);
    SaleVO head = (SaleVO)lastvo.getParentVO();
    SaleinvoiceBVO[] itemsnew = new SaleinvoiceBVO[oldvo.getChildrenVO().length];

    String[] strFieldnames = { "nnumber", "npacknumber", "nbalancenumber", "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny", "noriginalcurdiscountmny", "ntaxmny", "nmny", "nsummny", "ndiscountmny", "nsimulatecostmny", "ncostmny", "ntotalpaymny", "ntotalreceiptnumber", "ntotalinvoicenumber", "ntotalinventorynumber", "nassistcurdiscountmny", "nassistcursummny", "nassistcurmny", "nassistcurtaxmny", "nquotenumber", "nsubsummny", "nsubcursummny" };

    if (head.getNnetmny() != null)
      head.setNnetmny(head.getNnetmny().multiply(-1.0D));
    if (head.getNstrikemny() != null)
      head.setNstrikemny(head.getNstrikemny().multiply(-1.0D));
    if (head.getNtotalsummny() != null)
      head.setNtotalsummny(head.getNtotalsummny().multiply(-1.0D));
    head.setFcounteractflag(new Integer(2));
    head.setCsaleid(null);
    head.setVreceiptcode(null);
    head.setDbilldate(date);
    head.setFstatus(new Integer(1));
    head.setCapproveid(null);
    head.setDapprovedate(null);

    SaleinvoiceBVO[] items = (SaleinvoiceBVO[])(SaleinvoiceBVO[])oldvo.getChildrenVO();
    for (int i = 0; i < items.length; ++i) {
      itemsnew[i] = ((SaleinvoiceBVO)items[i].clone());
      for (int j = 0; j < strFieldnames.length; ++j) {
        if (items[i].getAttributeValue(strFieldnames[j]) != null) {
          UFDouble ufd = (UFDouble)items[i].getAttributeValue(strFieldnames[j]);

          itemsnew[i].setAttributeValue(strFieldnames[j], ufd.multiply(-1.0D));
        }
      }
      itemsnew[i].setCupreceipttype(headold.getCreceipttype());
      itemsnew[i].setCupsourcebillid(items[i].getCsaleid());
      itemsnew[i].setCupsourcebillbodyid(items[i].getPrimaryKey());
      itemsnew[i].setCupsourcebillcode(headold.getVreceiptcode());
      itemsnew[i].setTs(headold.getTs());

      itemsnew[i].setCsaleid(null);
      itemsnew[i].setCsale_bid(null);
    }

    lastvo.setChildrenVO(itemsnew);
    return lastvo;
  }

  public UFBoolean getBstrikeflag()
  {
    return this.bstrikeflag;
  }

  public CircularlyAccessibleValueObject[] getChildrenVO() {
    return this.bodyVO;
  }

  public Hashtable getHsArsubAcct()
  {
    return this.hsArsubAcct;
  }

  public Hashtable getHsSelectedARSubHVO()
  {
    return this.hsSelectedARSubHVO;
  }

  public CircularlyAccessibleValueObject getParentVO() {
    return this.headVO;
  }

  public String getPrimaryKey()
    throws BusinessException
  {
    return getParentVO().getPrimaryKey();
  }

  public int getStatus()
  {
    return this.m_status;
  }

  public void setAllinvoicevo(SaleinvoiceVO newAllinvoicevo)
  {
    this.allinvoicevo = newAllinvoicevo;
  }

  public void setBstrikeflag(UFBoolean newBstrikeflag)
  {
    this.bstrikeflag = newBstrikeflag;
  }

  public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
    this.bodyVO = ((SaleinvoiceBVO[])(SaleinvoiceBVO[])children);
  }

  public void setHsArsubAcct(Hashtable newHsArsubAcct)
  {
    this.hsArsubAcct = newHsArsubAcct;
  }

  public void setHsSelectedARSubHVO(Hashtable newHsSelectedARSubHVO)
  {
    this.hsSelectedARSubHVO = newHsSelectedARSubHVO;
  }

  public void setParentVO(CircularlyAccessibleValueObject parent) {
    this.headVO = ((SaleVO)parent);
  }

  public void setPrimaryKey(String key)
    throws BusinessException
  {
    getParentVO().setPrimaryKey(key);
  }

  public void setStatus(int status)
  {
    this.m_status = status;
  }

  public void validate()
    throws ValidationException
  {
    getParentVO().validate();

    if ((getChildrenVO() == null) || (getChildrenVO().length == 0)) {
      if (this.m_status != 2)
        return;
      throw new ValidationException(NCLangRes4VoTransl.getNCLangRes().getStrByID("40060501", "UPP40060501-000150"));
    }

    for (int i = 0; i < getChildrenVO().length; ++i)
      getChildrenVO()[i].validate();
  }

  public UFDate getDClientDate()
  {
    return this.dClientDate;
  }

  public void setDClientDate(UFDate clientDate)
  {
    this.dClientDate = clientDate;
  }

  public String getCuruserid()
  {
    return this.curuserid;
  }

  public void setCuruserid(String curuserid)
  {
    this.curuserid = curuserid;
  }

  public int getIAction()
  {
    return this.iAction;
  }

  public void setIAction(int newIAction)
  {
    this.iAction = newIAction;
  }

  public SaleinvoiceVO getAllSaleOrderVO()
  {
    return this.allSaleOrderVO;
  }

  public SaleVO getHeadVO() {
    return ((SaleVO)getParentVO());
  }

  public SaleinvoiceBVO[] getItemVOs() {
    return ((SaleinvoiceBVO[])(SaleinvoiceBVO[])getChildrenVO());
  }

  public void setAllSaleOrderVO(SaleinvoiceVO newAllSaleOrderVO)
  {
    this.allSaleOrderVO = newAllSaleOrderVO;
  }

  public SaleinvoiceVO getOldSaleOrderVO()
  {
    return this.oldSaleOrderVO;
  }

  public void setOldSaleOrderVO(SaleinvoiceVO newOldSaleOrderVO)
  {
    this.oldSaleOrderVO = newOldSaleOrderVO;
  }

  public String getOperatorid()
  {
    if (this.operatorid == null) {
      this.operatorid = getCuruserid();
      if (this.operatorid == null) {
        this.operatorid = this.headVO.getCoperatorid();
      }
    }
    return this.operatorid;
  }

  public void setLockOperatorid(String optid)
  {
    if (this.operatorid == null)
      this.operatorid = optid;
  }

  public Object[] getBodyDefValues(int iserial)
  {
    if (this.bodyVO == null)
      return null;
    Object[] o = new Object[this.bodyVO.length];
    for (int i = 0; i < this.bodyVO.length; ++i) {
      o[i] = this.bodyVO[i].getAttributeValue("vdef" + iserial);
    }
    return o;
  }

  public String getCbilltypecode() {
    return "32";
  }

  public Object getHeadDefValue(int iserial) {
    if (this.headVO == null) {
      return null;
    }
    return this.headVO.getAttributeValue("vdef" + iserial);
  }

  public String getPk_corp() {
    if (this.headVO == null) {
      return null;
    }
    return this.headVO.getPk_corp();
  }

  public String getCbilltypedef()
  {
    return "32";
  }
}